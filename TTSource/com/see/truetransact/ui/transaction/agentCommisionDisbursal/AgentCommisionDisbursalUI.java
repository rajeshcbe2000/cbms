/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AgentCommisionDisbursalUI.java
 *
 * Created on February 2, 2005, 12:20 PM
 */
package com.see.truetransact.ui.transaction.agentCommisionDisbursal;

import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.uicomponent.COptionPane;

import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.uicomponent.CButtonGroup;
import com.see.truetransact.uivalidation.NumericValidation;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Observer;
import java.util.Observable;
import java.util.List;
import java.util.Date;

/**
 *
 * @author 152721
 */
public class AgentCommisionDisbursalUI extends CInternalFrame implements java.util.Observer, UIMandatoryField {

    HashMap mandatoryMap;
    AgentCommisionDisbursalOB observable;
    //    AgentRB resourceBundle = new AgentRB();
    //    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.transaction.AgentCommisionDisbursalRB", ProxyParameters.LANGUAGE);
    java.util.ResourceBundle resourceBundle, objMandatoryRB;
    final int EDIT = 0, DELETE = 1, AUTHORIZE = 2, AGENTID = 3, ACCNO = 4, DEPOSITNO = 5;
    int viewType = -1;
    boolean cbo = false;
    private Date curr_dt = null;    
    /**
     * Creates new form AgentUI
     */
    public AgentCommisionDisbursalUI(String are) {
        initComponents();
        initSetup();
        btnPayDetails.setEnabled(false);
        txtCommTD.setText("");
        txtCommToOA.setText("");
        txtTDS.setText("");
    }

    public AgentCommisionDisbursalUI() {
        initComponents();
        initSetup();
        btnPayDetails.setEnabled(false);

    }

    private void initSetup() {
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setObservable();
        setMaxLenths();
        initComponentsData();
        txtCommisionDuringThePeriod.setVisible(false);
        //        lblCommision.setE(false);
        ClientUtil.enableDisable(this, false); //__ Disables all when the screen appears for the 1st time
        setButtonEnableDisable();
        txtAgentcomm.setAllowNumber(true);//__ Enables/Disables the necessary buttons and menu items...
        txtTdsCommission.setAllowNumber(true);
        observable.resetForm();                //__ Resets the Data in the Form...
        txtCommTD.setText("");
        txtCommToOA.setText("");
        txtTDS.setText("");
        //        observable.resetTable();
        observable.resetStatus();              //__ to reset the status...
        btnAuthorize.setVisible(false);
        btnSave.setVisible(true);
        btnEdit.setVisible(false);
        //        btnCancel.setVisible(false);
        btnPrint.setVisible(false);
        btnDelete.setVisible(false);

        //        btnAgentID.setEnabled(false);
        //        btnCommisionCreditedTo.setEnabled(false);
        //        btnDepositCreditedTo.setEnabled(false);
        //        srpAgentCommision.setVisible(false);
        observable.ttNotifyObservers();
        curr_dt = ClientUtil.getCurrentDate();
        rdoSlab.setVisible(false);
        rdoPercent.setVisible(false);
        rdoDefaultPercentage.setVisible(false);
        lblCommisionOpenedAct.setVisible(false);
        txtCommisionOpenedAct.setVisible(false);
    }

    private void setObservable() {
        observable = AgentCommisionDisbursalOB.getInstance();
        observable.addObserver(this);
    }

    /*
     * Auto Generated Method - setFieldNames() This method assigns name for all
     * the components. Other functions are working based on this name.
     */
    private void setFieldNames() {
        //        btnAgentID.setName("btnAgentID");
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
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblStatus1.setName("lblStatus1");;
        lblAgentName.setName("lblAgentName");
        lblAgentID.setName("lblAgentID");
        lblNameForAgent.setName("lblNameForAgent");
        lblFromDate.setName("lblFromDate");
        lblToDate.setName("lblToDate");
        lblCommision.setName("lblCommision");
        lblCollectionsDuringThePeriod.setName("lblCollectionsDuringThePeriod");
        lblCommisionForThePeriod.setName("lblCommisionForThePeriod");
        cboAgentId.setName("cboAgentId");
        cboProdtype.setName("cboProdtype");
        txtAgentcomm.setName("txtAgentcomm");
        txtTdsCommission.setName("txtTdsCommission");
        tdtFromDate.setName("tdtFromDate");
        tdtToDate.setName("tdtToDate");
        txtCommision.setName("txtCommision");
        txtCommisionDuringThePeriod.setName("txtCommisionDuringThePeriod");
        txtCommisionForThePeriod.setName("txtCommisionForThePeriod");
        btnPayDetails.setName("btnPayDetails");
        panAgentData.setName("panAgentData");
        lblComtoOA.setName("lblComtoOA");
        lblCommTD.setName("lblCommTD");
        lblTDS.setName("lblTDS");
        txtCommTD.setName("txtCommTD");
        txtCommToOA.setName("txtCommToOA");
        txtTDS.setName("txtTDS");

    }

    /*
     * Auto Generated Method - internationalize() This method used to assign
     * display texts from the Resource Bundle File.
     */
    private void internationalize() {
        AgentCommisionDisbursalRB resourceBundle = new AgentCommisionDisbursalRB();
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        //        lblApptDate.setText(resourceBundle.getString("lblApptDate"));
        //        btnClose.setText(resourceBundle.getString("btnClose"));
        //        lblName.setText(resourceBundle.getString("lblName"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        lblStatus1.setText(resourceBundle.getString("lblStatus1"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblAgentName.setText(resourceBundle.getString("lblAgentName"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));

        lblAgentName.setText(resourceBundle.getString("lblAgentName"));
        lblAgentID.setText(resourceBundle.getString("lblAgentID"));
        lblNameForAgent.setText(resourceBundle.getString("lblNameForAgent"));
        lblFromDate.setText(resourceBundle.getString("lblFromDate"));
        lblToDate.setText(resourceBundle.getString("lblToDate"));
        lblCommision.setText(resourceBundle.getString("lblCommision"));
        lblCollectionsDuringThePeriod.setText(resourceBundle.getString("lblCollectionsDuringThePeriod"));
        lblCommisionForThePeriod.setText(resourceBundle.getString("lblCommisionForThePeriod"));
        btnPayDetails.setText(resourceBundle.getString("btnPayDetails"));
        lblTDS.setText(resourceBundle.getString("lblTDS"));
        lblComtoOA.setText(resourceBundle.getString("lblComtoOA"));
        lblCommTD.setText(resourceBundle.getString("lblCommTD"));


    }

    public void initComponentsData() {
        cboAgentId.setModel(observable.getCbmAgentId());
        cboProdtype.setModel(observable.getCbmProdtype());
    }

    /*
     * Auto Generated Method - update() This method called by Observable. It
     * updates the UI with Observable's data. If needed add/Remove RadioButtons
     * method need to be added.
     */
    public void update(Observable observed, Object arg) {
        txtCommision.setText(observable.getTxtCommision());
        txtCommisionDuringThePeriod.setText(observable.getTxtCommisionDuringThePeriod());
        txtCommisionForThePeriod.setText(observable.getTxtCommisionForThePeriod());
        tdtFromDate.setDateValue(observable.getTdtFromDate());
        tdtToDate.setDateValue(observable.getTdtToDate());
        cboAgentId.setSelectedItem(observable.getCboAgentId());
        txtCommTD.setText(CommonUtil.convertObjToStr(new Double(observable.getTotCommToTD())));
        txtCommToOA.setText(CommonUtil.convertObjToStr(new Double(observable.getTotCommToOA())));
        txtTDS.setText(CommonUtil.convertObjToStr(new Double(observable.getTdsAmt())));

    }

    /*
     * Auto Generated Method - updateOBFields() This method called by Save
     * option of UI. It updates the OB with UI data.
     */
    public void updateOBFields() {
        observable.setScreen(getScreen());
        observable.setModule(getModule());
        observable.setTxtCommision(txtCommision.getText());
        observable.setTxtCommisionForThePeriod(txtCommisionForThePeriod.getText());
        observable.setTxtCommisionDuringThePeriod(txtCommisionDuringThePeriod.getText());
        observable.setTdtFromDate(tdtFromDate.getDateValue());
        observable.setTdtToDate(tdtToDate.getDateValue());
        observable.setSelectedBranchID(getSelectedBranchID());
        observable.setTotCommToOA(CommonUtil.convertObjToDouble(txtCommToOA.getText()).doubleValue());
        observable.setTotCommToTD(CommonUtil.convertObjToDouble(txtCommTD.getText()).doubleValue());
        observable.setTdsAmt(CommonUtil.convertObjToDouble(txtTDS.getText()).doubleValue());
        if (chkTransfer.isSelected()) {
            observable.setTransType(CommonConstants.TX_TRANSFER);
        } else if (chkCash.isSelected()) {
            observable.setTransType(CommonConstants.TX_CASH);
        }
        observable.setCboProdtype(((ComboBoxModel)cboProdtype.getModel()).getKeyForSelected().toString());
        observable.setCboProdId(((ComboBoxModel)cboProductID.getModel()).getKeyForSelected().toString());
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
        mandatoryMap.put("txtCommision", new Boolean(true));
        mandatoryMap.put("txtCommisionDuringThePeriod", new Boolean(true));
        mandatoryMap.put("txCommisionForThePeriod", new Boolean(true));
        mandatoryMap.put("cboAgentId", new Boolean(true));
        //        mandatoryMap.put("txtDepositCreditedTo", new Boolean(true));
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
        AgentCommisionDisbursalMRB objMandatoryRB = new AgentCommisionDisbursalMRB();
        txtCommision.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCommision"));
        txtCommisionDuringThePeriod.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCommisionDuringThePeriod"));
        txtCommisionForThePeriod.setHelpMessage(lblMsg, objMandatoryRB.getString("txCommisionForThePeriod"));
        //        cboAgentId.setHelpMessage(lblMsg, objMandatoryRB.getString("cboAgentId"));
        //        txtDepositCreditedTo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDepositCreditedTo"));

    }

    private void setMaxLenths() {
        txtCommision.setMaxLength(16);
        txtCommision.setValidation(new NumericValidation());
        txtCommisionDuringThePeriod.setMaxLength(16);
        txtCommisionDuringThePeriod.setValidation(new NumericValidation());
        txtCommisionForThePeriod.setMaxLength(16);
        txtCommisionForThePeriod.setValidation(new NumericValidation());
    }

    // To set The Value of the Buttons Depending on the Value or Condition...
    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnClose.setEnabled(!btnClose.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        btnDelete.setEnabled(!btnDelete.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());

        btnSave.setEnabled(!btnSave.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        chkTranstypeGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        btnGrpCommType = new com.see.truetransact.uicomponent.CButtonGroup();
        panAgent = new com.see.truetransact.uicomponent.CPanel();
        panAgentData = new com.see.truetransact.uicomponent.CPanel();
        lblAgentName = new com.see.truetransact.uicomponent.CLabel();
        cboAgentId = new com.see.truetransact.uicomponent.CComboBox();
        lblAgentID = new com.see.truetransact.uicomponent.CLabel();
        lblNameForAgent = new com.see.truetransact.uicomponent.CLabel();
        lblFromDate = new com.see.truetransact.uicomponent.CLabel();
        tdtFromDate = new com.see.truetransact.uicomponent.CDateField();
        lblToDate = new com.see.truetransact.uicomponent.CLabel();
        tdtToDate = new com.see.truetransact.uicomponent.CDateField();
        lblCommision = new com.see.truetransact.uicomponent.CLabel();
        txtCommision = new com.see.truetransact.uicomponent.CTextField();
        lblCollectionsDuringThePeriod = new com.see.truetransact.uicomponent.CLabel();
        txtCommisionDuringThePeriod = new com.see.truetransact.uicomponent.CTextField();
        lblCommisionForThePeriod = new com.see.truetransact.uicomponent.CLabel();
        txtCommisionForThePeriod = new com.see.truetransact.uicomponent.CTextField();
        btnPayDetails = new com.see.truetransact.uicomponent.CButton();
        txtCommToOA = new com.see.truetransact.uicomponent.CTextField();
        lblComtoOA = new com.see.truetransact.uicomponent.CLabel();
        txtCommTD = new com.see.truetransact.uicomponent.CTextField();
        lblCommTD = new com.see.truetransact.uicomponent.CLabel();
        lblTDS = new com.see.truetransact.uicomponent.CLabel();
        txtTDS = new com.see.truetransact.uicomponent.CTextField();
        lblProdtype = new com.see.truetransact.uicomponent.CLabel();
        cboProdtype = new com.see.truetransact.uicomponent.CComboBox();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        txtAgentcomm = new com.see.truetransact.uicomponent.CTextField();
        lblTdsCommission = new com.see.truetransact.uicomponent.CLabel();
        txtTdsCommission = new com.see.truetransact.uicomponent.CTextField();
        chkTransfer = new com.see.truetransact.uicomponent.CCheckBox();
        chkCash = new com.see.truetransact.uicomponent.CCheckBox();
        lblAgentPercentage = new com.see.truetransact.uicomponent.CLabel();
        cboProductID = new com.see.truetransact.uicomponent.CComboBox();
        lblProdId = new com.see.truetransact.uicomponent.CLabel();
        lblCommisionOpenedAct = new com.see.truetransact.uicomponent.CLabel();
        txtCommisionOpenedAct = new com.see.truetransact.uicomponent.CTextField();
        panStatus1 = new com.see.truetransact.uicomponent.CPanel();
        rdoAmtBasedCollected = new com.see.truetransact.uicomponent.CRadioButton();
        rdoAccountBased = new com.see.truetransact.uicomponent.CRadioButton();
        panStatus2 = new com.see.truetransact.uicomponent.CPanel();
        rdoPercent = new com.see.truetransact.uicomponent.CRadioButton();
        rdoDefaultPercentage = new com.see.truetransact.uicomponent.CRadioButton();
        rdoSlab = new com.see.truetransact.uicomponent.CRadioButton();
        panAgentDisplay = new com.see.truetransact.uicomponent.CPanel();
        srpDailyDepositDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblAgentCommission = new com.see.truetransact.uicomponent.CTable();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus1 = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        tbrLoantProduct = new javax.swing.JToolBar();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace29 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace30 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace31 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace32 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace33 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace34 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        mbrAgent = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptProcess = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitPrint = new javax.swing.JMenuItem();
        sptPrint = new javax.swing.JSeparator();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMinimumSize(new java.awt.Dimension(700, 620));
        setPreferredSize(new java.awt.Dimension(700, 620));

        panAgent.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panAgent.setMinimumSize(new java.awt.Dimension(412, 700));
        panAgent.setPreferredSize(new java.awt.Dimension(412, 700));
        panAgent.setLayout(new java.awt.GridBagLayout());

        panAgentData.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panAgentData.setMinimumSize(new java.awt.Dimension(675, 350));
        panAgentData.setPreferredSize(new java.awt.Dimension(650, 350));
        panAgentData.setLayout(new java.awt.GridBagLayout());

        lblAgentName.setText("Agent Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentData.add(lblAgentName, gridBagConstraints);

        cboAgentId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboAgentId.setPopupWidth(150);
        cboAgentId.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboAgentIdItemStateChanged(evt);
            }
        });
        cboAgentId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboAgentIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentData.add(cboAgentId, gridBagConstraints);

        lblAgentID.setText("Agent ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentData.add(lblAgentID, gridBagConstraints);

        lblNameForAgent.setMaximumSize(new java.awt.Dimension(100, 21));
        lblNameForAgent.setMinimumSize(new java.awt.Dimension(100, 21));
        lblNameForAgent.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentData.add(lblNameForAgent, gridBagConstraints);

        lblFromDate.setText("From Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentData.add(lblFromDate, gridBagConstraints);

        tdtFromDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtFromDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentData.add(tdtFromDate, gridBagConstraints);

        lblToDate.setText("To Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentData.add(lblToDate, gridBagConstraints);

        tdtToDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtToDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentData.add(tdtToDate, gridBagConstraints);

        lblCommision.setText("Commision During The Period");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentData.add(lblCommision, gridBagConstraints);

        txtCommision.setMaximumSize(new java.awt.Dimension(100, 21));
        txtCommision.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentData.add(txtCommision, gridBagConstraints);

        lblCollectionsDuringThePeriod.setText("d");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentData.add(lblCollectionsDuringThePeriod, gridBagConstraints);

        txtCommisionDuringThePeriod.setMaximumSize(new java.awt.Dimension(100, 21));
        txtCommisionDuringThePeriod.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentData.add(txtCommisionDuringThePeriod, gridBagConstraints);

        lblCommisionForThePeriod.setText("Commison For The Period");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentData.add(lblCommisionForThePeriod, gridBagConstraints);

        txtCommisionForThePeriod.setMaximumSize(new java.awt.Dimension(100, 21));
        txtCommisionForThePeriod.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentData.add(txtCommisionForThePeriod, gridBagConstraints);

        btnPayDetails.setText("Pay Details");
        btnPayDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPayDetailsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentData.add(btnPayDetails, gridBagConstraints);

        txtCommToOA.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtCommToOA.setMaximumSize(new java.awt.Dimension(100, 21));
        txtCommToOA.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentData.add(txtCommToOA, gridBagConstraints);

        lblComtoOA.setText("Commision OA A/c");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentData.add(lblComtoOA, gridBagConstraints);

        txtCommTD.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtCommTD.setMaximumSize(new java.awt.Dimension(100, 21));
        txtCommTD.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentData.add(txtCommTD, gridBagConstraints);

        lblCommTD.setText("CommisionTD A/C");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentData.add(lblCommTD, gridBagConstraints);

        lblTDS.setText("TDS");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentData.add(lblTDS, gridBagConstraints);

        txtTDS.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTDS.setMaximumSize(new java.awt.Dimension(100, 21));
        txtTDS.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentData.add(txtTDS, gridBagConstraints);

        lblProdtype.setText("ProductType");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentData.add(lblProdtype, gridBagConstraints);

        cboProdtype.setMaximumSize(new java.awt.Dimension(100, 21));
        cboProdtype.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdtype.setPopupWidth(150);
        cboProdtype.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdtypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentData.add(cboProdtype, gridBagConstraints);

        cLabel1.setText("Agent Commission");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentData.add(cLabel1, gridBagConstraints);

        txtAgentcomm.setMaximumSize(new java.awt.Dimension(100, 21));
        txtAgentcomm.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentData.add(txtAgentcomm, gridBagConstraints);

        lblTdsCommission.setText("TDSCommision");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentData.add(lblTdsCommission, gridBagConstraints);

        txtTdsCommission.setMaximumSize(new java.awt.Dimension(100, 21));
        txtTdsCommission.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentData.add(txtTdsCommission, gridBagConstraints);

        chkTranstypeGroup.add(chkTransfer);
        chkTransfer.setText("Transfer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentData.add(chkTransfer, gridBagConstraints);

        chkTranstypeGroup.add(chkCash);
        chkCash.setSelected(true);
        chkCash.setText("Cash");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentData.add(chkCash, gridBagConstraints);

        lblAgentPercentage.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 106, 2, 5);
        panAgentData.add(lblAgentPercentage, gridBagConstraints);

        cboProductID.setMaximumSize(new java.awt.Dimension(100, 21));
        cboProductID.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProductID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProductIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentData.add(cboProductID, gridBagConstraints);

        lblProdId.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentData.add(lblProdId, gridBagConstraints);

        lblCommisionOpenedAct.setText("Commision for Accounts Opened");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentData.add(lblCommisionOpenedAct, gridBagConstraints);

        txtCommisionOpenedAct.setMaximumSize(new java.awt.Dimension(100, 21));
        txtCommisionOpenedAct.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentData.add(txtCommisionOpenedAct, gridBagConstraints);

        panStatus1.setLayout(new java.awt.GridBagLayout());

        btnGrpCommType.add(rdoAmtBasedCollected);
        rdoAmtBasedCollected.setText("Commision based on amount collected ");
        rdoAmtBasedCollected.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoAmtBasedCollectedActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        panStatus1.add(rdoAmtBasedCollected, gridBagConstraints);

        btnGrpCommType.add(rdoAccountBased);
        rdoAccountBased.setText("Commision based on account opened");
        rdoAccountBased.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoAccountBasedActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        panStatus1.add(rdoAccountBased, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentData.add(panStatus1, gridBagConstraints);

        panStatus2.setLayout(new java.awt.GridBagLayout());

        btnGrpCommType.add(rdoPercent);
        rdoPercent.setText("Manual Percentage");
        rdoPercent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoPercentActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        panStatus2.add(rdoPercent, gridBagConstraints);

        btnGrpCommType.add(rdoDefaultPercentage);
        rdoDefaultPercentage.setText("Default Percentage");
        rdoDefaultPercentage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDefaultPercentageActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        panStatus2.add(rdoDefaultPercentage, gridBagConstraints);

        btnGrpCommType.add(rdoSlab);
        rdoSlab.setText("Slab Wise Commisiion");
        rdoSlab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoSlabActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        panStatus2.add(rdoSlab, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentData.add(panStatus2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(15, 20, 15, 20);
        panAgent.add(panAgentData, gridBagConstraints);

        panAgentDisplay.setMinimumSize(new java.awt.Dimension(400, 150));
        panAgentDisplay.setPreferredSize(new java.awt.Dimension(400, 150));
        panAgentDisplay.setLayout(new java.awt.GridBagLayout());

        srpDailyDepositDetails.setMaximumSize(new java.awt.Dimension(10, 5));
        srpDailyDepositDetails.setMinimumSize(new java.awt.Dimension(10, 5));
        srpDailyDepositDetails.setPreferredSize(new java.awt.Dimension(25, 25));

        tblAgentCommission.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Period", "Comm. Rate", "Comm. Amt", "Commission"
            }
        ));
        tblAgentCommission.setMinimumSize(new java.awt.Dimension(60, 35));
        tblAgentCommission.setPreferredScrollableViewportSize(new java.awt.Dimension(300, 35));
        srpDailyDepositDetails.setViewportView(tblAgentCommission);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAgentDisplay.add(srpDailyDepositDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAgent.add(panAgentDisplay, gridBagConstraints);

        getContentPane().add(panAgent, java.awt.BorderLayout.CENTER);

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace1.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace1, gridBagConstraints);

        lblStatus1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblStatus1.setText("                      ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblStatus1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblMsg, gridBagConstraints);

        getContentPane().add(panStatus, java.awt.BorderLayout.SOUTH);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnNew);

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace29);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.setOpaque(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnEdit);

        lblSpace30.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace30.setText("     ");
        lblSpace30.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace30);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnDelete);

        lblSpace2.setText("     ");
        tbrLoantProduct.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnSave);

        lblSpace31.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace31.setText("     ");
        lblSpace31.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace31);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnCancel);

        lblSpace3.setText("     ");
        tbrLoantProduct.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.setEnabled(false);
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnAuthorize);

        lblSpace32.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace32.setText("     ");
        lblSpace32.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace32.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace32.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace32);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.setEnabled(false);
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnException);

        lblSpace33.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace33.setText("     ");
        lblSpace33.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace33.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace33.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace33);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.setEnabled(false);
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnReject);

        lblSpace4.setText("     ");
        tbrLoantProduct.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.setEnabled(false);
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnPrint);

        lblSpace34.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace34.setText("     ");
        lblSpace34.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace34);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.setEnabled(false);
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnClose);

        getContentPane().add(tbrLoantProduct, java.awt.BorderLayout.NORTH);

        mnuProcess.setText("Process");

        mitNew.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        mitNew.setMnemonic('N');
        mitNew.setText("New");
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

        mitEdit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        mitEdit.setMnemonic('E');
        mitEdit.setText("Edit");
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        mitDelete.setMnemonic('D');
        mitDelete.setText("Delete");
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);
        mnuProcess.add(sptProcess);

        mitSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        mitSave.setMnemonic('S');
        mitSave.setText("Save");
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setMnemonic('C');
        mitCancel.setText("Cancel");
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess.add(mitCancel);
        mnuProcess.add(sptCancel);

        mitPrint.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        mitPrint.setMnemonic('P');
        mitPrint.setText("Print");
        mitPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitPrintActionPerformed(evt);
            }
        });
        mnuProcess.add(mitPrint);
        mnuProcess.add(sptPrint);

        mitClose.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        mitClose.setMnemonic('l');
        mitClose.setText("Close");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrAgent.add(mnuProcess);

        setJMenuBar(mbrAgent);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tdtToDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtToDateFocusLost
        // TODO add your handling code here:
        Date fromDt = DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue());
        Date toDt = DateUtil.getDateMMDDYYYY(tdtToDate.getDateValue());
        Date dayBeginDt = DateUtil.getDateWithoutMinitues((Date) curr_dt.clone());
        if (fromDt != null && toDt != null) {
            if (DateUtil.dateDiff(fromDt, toDt) < 0) {
                ClientUtil.displayAlert("From Date is greater then To date");
                tdtToDate.setDateValue("");
            } else if (DateUtil.dateDiff(toDt, dayBeginDt) < 0) {
                ClientUtil.displayAlert("To Date is greater then Daybegin Date");
                tdtToDate.setDateValue("");
            }
        } else {
            ClientUtil.displayAlert("Set The Commission Date");
        }
    }//GEN-LAST:event_tdtToDateFocusLost

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        cbo = true;
        observable.resetForm();              // to Reset all the Fields and Status in UI...
        //        txtCommTD.setText("");
        //        txtCommToOA.setText("");
        //        txtTDS.setText("");
        btnPayDetails.setEnabled(true);
        //        observable.resetTable();
        setButtonEnableDisable();             // Enables/Disables the necessary buttons and menu items...
        ClientUtil.enableDisable(this, true); // Enables the panel...
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);   //Sets the Action Type to be performed...
        observable.setStatus();               // To set the Value of lblStatus...
        //        tdtFromDate.setDateValue(DateUtil.getStringDate(observable.calcuateFirstDateOfMonth()));
        //        tdtToDate.setDateValue(DateUtil.getStringDate(observable.calcuateLastDateOfMonth()));
        System.out.println("tdtToDate : " + tdtToDate);
        //        txtAgentID.setEnabled(false);
        //        btnAgentID.setEnabled(true);
        //        btnCommisionCreditedTo.setEnabled(true);
        //        btnDepositCreditedTo.setEnabled(true);



        //__ To Save the data in the Internal Frame...
        txtCommTD.setText("");
        txtCommToOA.setText("");
        txtTDS.setText("");
        txtAgentcomm.setText("");
        txtTdsCommission.setText("0");
        setModified(true);
        btnGrpCommType = new CButtonGroup();
        btnGrpCommType.add(rdoSlab);
        btnGrpCommType.add(rdoPercent);
        btnGrpCommType.add(rdoDefaultPercentage);
        txtAgentcomm.setEnabled(false);
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        //                observable.setActionType(ClientConstants.ACTIONTYPE_EDIT); //__ Sets the Action Type to be performed...
        //        popUp(EDIT);
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        //        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE); //__ Sets the Action Type to be performed...
        //        popUp(DELETE);
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        //        // TODO add your handling code here:
        //        updateOBFields();
        //        //To display an alert if the mandatory fields are not properly inputted, else proceed with normal operation
        //        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panAgent);
        //
        //        if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0 ){
        //            displayAlert(mandatoryMessage);
        //        }else{
        //            observable.doAction();// To perform the necessary operation depending on the Action type...
        //            //__ If the Operation is Not Failed, Clear the Screen...
        //            if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED) {
        //
        //                if( viewType == EDIT ){
        ////                    super.removeEditLock(txtAgentID.getText());
        //                }
        //
        //                observable.resetForm();                    //__ Reset the fields in the UI to null...
        ////                observable.resetTable();
        //                ClientUtil.enableDisable(this, false);     //__ Disables the panel...
        ////                btnAgentID.setEnabled(false);
        //                setButtonEnableDisable();                  //__ Enables or Disables the buttons and menu Items depending on their previous state...
        //                observable.setResultStatus();              //__ To Reset the Value of lblStatus...
        //
        //
        //            }
        //        }
        //
        //         //__ Make the Screen Closable..
        //        setModified(false);

        updateOBFields();
        int yesno = COptionPane.showConfirmDialog(this, "Do you want to Post This Agent  " + lblNameForAgent.getText() + "\n Agent  Commission ?", "Note", COptionPane.YES_NO_OPTION);
        if (yesno == COptionPane.YES_OPTION) {
            observable.doAction();
            if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                lblStatus1.setText("POSTED");
                if (observable.getProxyReturnMap() != null && observable.getProxyReturnMap().size() > 0) {
                    if (observable.getProxyReturnMap().containsKey("SINGLE_TANS_ID")) {
                        displayTransDetail(observable.getProxyReturnMap());
                    }
                }
                btnCancelActionPerformed(null);

            }
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:

        if (viewType == EDIT) {
            //            super.removeEditLock(txtAgentID.getText());
        }

        observable.resetForm();                 //__ Reset the fields in the UI to null...
        updateTable();
        //        txtCommTD.setText("");
        //        txtCommToOA.setText("");
        //        txtTDS.setText("");
        //        observable.resetTable();
        ClientUtil.enableDisable(this, false);  //__ Disables the panel...
        //        btnAgentID.setEnabled(false);
        //        btnDepositCreditedTo.setEnabled(false);
        //        btnCommisionCreditedTo.setEnabled(false);


        if (!btnSave.isEnabled()) {
            btnSave.setEnabled(true);
        }
        setButtonEnableDisable();               //__ Enables or Disables the buttons and menu Items depending on their previous state...
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);//Sets the Action Type to be performed...
        observable.setStatus();                 //__ To set the Value of lblStatus..

        viewType = -1;

        //__ Make the Screen Closable..
        setModified(false);
        btnClose.setEnabled(true);
        txtCommTD.setText("");
        txtCommToOA.setText("");
        txtTDS.setText("");
        lblNameForAgent.setText("");
        txtAgentcomm.setText("");
        txtTdsCommission.setText("");
        cboProdtype.setSelectedItem("");
        cboProductID.setSelectedItem("");
        btnGrpCommType.remove(rdoSlab);
        btnGrpCommType.remove(rdoPercent);
        btnGrpCommType.remove(rdoDefaultPercentage);
        rdoSlab.setSelected(false);
        rdoPercent.setSelected(false);
        txtCommisionOpenedAct.setText("");
        rdoAmtBasedCollected.setSelected(false);
        rdoAccountBased.setSelected(false);
        rdoDefaultPercentage.setSelected(false);
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
    }//GEN-LAST:event_btnAuthorizeActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        authorizeStatus(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPrintActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        cifClosingAlert();
        //        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
  private void displayTransDetail(HashMap proxyResultMap) {
        System.out.println("@#$@@$@@@$ proxyResultMap : " + proxyResultMap);
      int yesNo = 0;
      String[] options = {"Yes", "No"};
      yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
              COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
              null, options, options[0]);
      if (yesNo == 0) {
          TTIntegration ttIntgration = null;
          HashMap paramMap = null;
          String toTransId = "";
          String fromTransId = "";
          String toCashId = "";
          String fromCashId = "";
          if (proxyResultMap.containsKey("TRANSFER") && proxyResultMap.containsKey("SINGLE_TANS_ID")) {
              if (proxyResultMap.containsKey("SINGLE_TANS_ID")) {
                  fromTransId = CommonUtil.convertObjToStr(proxyResultMap.get("SINGLE_TANS_ID"));
              }
//                if (proxyResultMap.containsKey("SINGLE_TANS_ID")) {
//                    toTransId = CommonUtil.convertObjToStr(proxyResultMap.get("SINGLE_TANS_ID"));
//                }
              paramMap = new HashMap();
              paramMap.put("TransId", fromTransId);
//                paramMap.put("ToTransId", toTransId);
              paramMap.put("TransDt", curr_dt);
              paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
              System.out.println("#$#$$ paramMap : " + paramMap);
              ttIntgration.setParam(paramMap);
              ttIntgration.integrationForPrint("ReceiptPayment");
          }
          if (proxyResultMap.containsKey("CASH") && proxyResultMap.containsKey("SINGLE_TANS_ID")) {
              if (proxyResultMap.containsKey("SINGLE_TANS_ID")) {
                  fromCashId = CommonUtil.convertObjToStr(proxyResultMap.get("SINGLE_TANS_ID"));
              }
//                if (proxyResultMap.containsKey("CASH_TO_ID")) {
//                    toCashId = CommonUtil.convertObjToStr(proxyResultMap.get("CASH_TO_ID"));
//                }
              paramMap = new HashMap();
              paramMap.put("TransId", fromCashId);
//              paramMap.put("ToTransId", toCashId);
              paramMap.put("TransDt", curr_dt);
              paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
              System.out.println("#$#$$ paramMap : " + paramMap);
              ttIntgration.setParam(paramMap);
              ttIntgration.integrationForPrint("CashPayment");
          }
      }
    }
    private void tdtFromDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtFromDateFocusLost

        if (CommonUtil.convertObjToStr(tdtToDate.getDateValue()).equalsIgnoreCase("")) {
            tdtToDate.setDateValue(DateUtil.getStringDate((Date) curr_dt.clone()));
        } else {
            ClientUtil.validateFromDate(tdtFromDate, tdtToDate.getDateValue());
        }

    }//GEN-LAST:event_tdtFromDateFocusLost

    private void btnPayDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPayDetailsActionPerformed
        // TODO add your handling code here:
        //        updateOBFields();
        //To display an alert if the mandatory fields are not properly inputted, else proceed with normal operation
        //        String mandatoryAgentListMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panAgentData);
        //        String startDate = tdtFromDate.getDateValue();
        //        String endDate = tdtToDate.getDateValue();
        //        if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryAgentListMessage.length() > 0 ){
        //            displayAlert(mandatoryAgentListMessage);
        //        }else{
        //            tdtToDateFocusLost(null);
        //            observable.doAction();
        //
        //        }
        //        displayAlert("Create a Deposit From The Operative Account.....");
        tdtToDateFocusLost(null);
        if (DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue()) != null && DateUtil.getDateMMDDYYYY(tdtToDate.getDateValue()) != null) {
            if(!rdoAmtBasedCollected.isSelected() && !rdoAccountBased.isSelected()){
                ClientUtil.showAlertWindow("Please select Commision based on amount collected or based on account opened");
                return;
            }
            if(rdoAmtBasedCollected.isSelected() && (!rdoSlab.isSelected() && !rdoPercent.isSelected() && !rdoDefaultPercentage.isSelected())){
                ClientUtil.showAlertWindow("Please select type of commision");
                return;
            }
                if(rdoPercent.isSelected()){
                    if(!(txtAgentcomm.getText()!=null && txtAgentcomm.getText().length()>0)){
                        ClientUtil.displayAlert("Please Enter Agent Commission Percentage");
                        return;
                    }
                }
                observable.setTdtFromDate(tdtFromDate.getDateValue());
                observable.setTdtToDate(tdtToDate.getDateValue());
                observable.setTxtAgentcomm(CommonUtil.convertObjToDouble(txtAgentcomm.getText()));
                observable.setTxtTdsCommission(CommonUtil.convertObjToDouble(txtTdsCommission.getText()));
                observable.setCbmProdtype(((ComboBoxModel) cboProdtype.getModel()));
                if (chkTransfer.isSelected()) {
                    observable.setTransType(CommonConstants.TX_TRANSFER);
                } else if (chkCash.isSelected()) {
                    observable.setTransType(CommonConstants.TX_CASH);
                }
                //   observable.setCboProdtype(CommonUtil.convertObjToStr(cboProdtype.getSelectedItem())); 
                String agt = observable.getCboAgentId();
    //            observable.setCboAgentId(agt);
                if(rdoAmtBasedCollected.isSelected() && rdoPercent.isSelected()){
                    observable.commisionCalculation();
                }else if(rdoAmtBasedCollected.isSelected() && rdoDefaultPercentage.isSelected()){
                    double totalAmt = observable.AgentCommisionSlabCalculation();
                    System.out.println("totalaccount : "+totalAmt);                    
                }else if(rdoAccountBased.isSelected()){
                    double totalaccount = observable.AgentCommisionSlabCalculation();
                    System.out.println("totalaccount : "+totalaccount);
                    if(totalaccount>0){
                        txtCommisionOpenedAct.setText(CommonUtil.convertObjToStr(totalaccount));
                        observable.setTxtCommisionOpenedAct(totalaccount);
                    }else{
                        ClientUtil.displayAlert("Agent doesn't have any account opned for selected period");
                        return;
                    }
                }else{
                    String agentCommSlabRequired = "N";
                    String prodId = ((ComboBoxModel)cboProductID.getModel()).getKeyForSelected().toString();                    
                    HashMap prodMap = new HashMap();
                    prodMap.put("PROD_ID",prodId);
                    List slabLst = ClientUtil.executeQuery("getAgentCommSlabRequiredForProd", prodMap);
                    if(slabLst != null && slabLst.size() > 0){
                        HashMap slabMap = (HashMap)slabLst.get(0);
                        if(slabMap.containsKey("SLAB_REQUIRED") && slabMap.get("SLAB_REQUIRED") != null){
                            agentCommSlabRequired = CommonUtil.convertObjToStr(slabMap.get("SLAB_REQUIRED"));
                        }
                    }
                    if(agentCommSlabRequired.equalsIgnoreCase("Y")){
                        observable.calcAgentCommissionUsingSlabPercentage();
                    }else{
                    observable.SlabCommisionCalculation();
                    }
                }
                
                updateTable();
                btnPayDetails.setEnabled(false);
                ClientUtil.enableDisable(this, false);
                //           Double txtamt = (Double) observable.agentsAmount(agt);
                //           String totalAmt = String.valueOf(txtamt);
                //                txtCommision.setText(totalAmt);
                //                updateOBFields();
                //                if(! totalAmt.equals(null)) {
                //                    double commisionAmt =observable.commisionCalculation();
                //                }            
        } else {
            ClientUtil.displayAlert("Set the Commission Date");
        }

    }//GEN-LAST:event_btnPayDetailsActionPerformed

    private void updateTable() {
        this.tblAgentCommission.setModel(observable.getTbmAgentCommission());
        this.tblAgentCommission.revalidate();
    }

    private void cboAgentIdItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboAgentIdItemStateChanged
        //        observable.agentName();
        //        lblNameForAgent.setText; // TODO add your handling code here:
    }//GEN-LAST:event_cboAgentIdItemStateChanged

    private void cboAgentIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboAgentIdActionPerformed

        String prodId = "";
        String agentId = "";
        if(cboAgentId.getSelectedItem()!=null && !cboAgentId.getSelectedItem().equals("")){
            if(!rdoAmtBasedCollected.isSelected() && !rdoAccountBased.isSelected()){
                ClientUtil.showAlertWindow("Please select Commision based on amount collected or Commision based on account opened");
                cboAgentId.setSelectedItem("");
                return;
            }
            agentId = ((ComboBoxModel)cboAgentId.getModel()).getKeyForSelected().toString();
            System.out.println("agentId^$^$^$^^^$^"+agentId);
            lblNameForAgent.setText(CommonUtil.convertObjToStr(((ComboBoxModel)cboAgentId.getModel()).getKeyForSelected()));
            if(cboProductID.getSelectedItem()!=null && !cboProductID.getSelectedItem().equals("")){
                prodId = ((ComboBoxModel)cboProductID.getModel()).getKeyForSelected().toString();
                observable.setCboProdId(prodId);
                HashMap standMap = new HashMap();
                standMap =observable.standingProcessedDetails(prodId, agentId); 
                if(standMap!=null && !standMap.equals("null") && standMap.size()>0 ){
                    int yesNo = 0;
                    String[] options = {"Yes", "No"};
                    yesNo = COptionPane.showOptionDialog(null, "Last Processed Date is :"+DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(standMap.get("LAST_DT"))) +"  \n Do you want to continue?", CommonConstants.WARNINGTITLE,
                    COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                    null, options, options[0]);
                    if (yesNo == 0) { 
                        tdtFromDate.setDateValue(DateUtil.getStringDate(observable.calcuateFirstDateOfMonth(prodId,agentId)));                
                        if(!(tdtFromDate.getDateValue()!=null && !tdtFromDate.getDateValue().equals(""))){
                            cboProductID.setSelectedItem("");
                            tdtToDate.setDateValue(null);
                        }else{
                            tdtToDate.setDateValue(DateUtil.getStringDate(observable.calcuateLastDateOfMonth(getProperDateFormat(tdtFromDate.getDateValue()))));
                            observable.setTdtFromDate(tdtFromDate.getDateValue());
                            observable.setTdtToDate(tdtToDate.getDateValue());
                        }
                        tdtFromDate.setEnabled(false); 
                    }else{
                        btnCancelActionPerformed(null);
                    }
                }else{
                    int yesNo = 0;
                    String[] options = {"Yes", "No"};
                    yesNo = COptionPane.showOptionDialog(null, "No Standing instruction details Found!! \n Do you want to continue?", CommonConstants.WARNINGTITLE,
                    COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                    null, options, options[0]);
                    if (yesNo == 0) { 
                        tdtFromDate.setDateValue(DateUtil.getStringDate(observable.calcuateFirstDateOfMonth(prodId,agentId)));                
                        if(!(tdtFromDate.getDateValue()!=null && !tdtFromDate.getDateValue().equals(""))){
                            cboProductID.setSelectedItem("");
                            tdtToDate.setDateValue(null);
                        }else{
                            tdtToDate.setDateValue(DateUtil.getStringDate(observable.calcuateLastDateOfMonth(getProperDateFormat(tdtFromDate.getDateValue()))));
                            observable.setTdtFromDate(tdtFromDate.getDateValue());
                            observable.setTdtToDate(tdtToDate.getDateValue());
                        }
                        tdtFromDate.setEnabled(false); 
                    }else{
                        btnCancelActionPerformed(null);
                    }
                }
            }else{
                ClientUtil.showMessageWindow("Please select any Product Id!!");
                return;
            }
        }else{            
            lblNameForAgent.setText("");
        }
    }//GEN-LAST:event_cboAgentIdActionPerformed
    public void authorizeStatus(String authorizeStatus) {
        if (viewType != AUTHORIZE) {
            viewType = AUTHORIZE;

            //__ To Save the data in the Internal Frame...
            setModified(true);
            HashMap mapParam = new HashMap();
            mapParam.put(CommonConstants.MAP_NAME, "getAgentAuthorizeList");

            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            //            whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);

            //            whereMap.put("BRANCHID",TrueTransactMain.BRANCH_ID);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeAgentData");

            whereMap = null;

            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);

            //__ If there's no data to be Authorized, call Cancel action...
            if (!isModified()) {
                setButtonEnableDisable();
                btnCancelActionPerformed(null);
            }
        } else if (viewType == AUTHORIZE) {
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            //            singleAuthorizeMap.put("AGENT ID",txtAgentID.getText());
            //            singleAuthorizeMap.put(CommonConstants.BRANCH_ID,TrueTransactMain.BRANCH_ID);
            singleAuthorizeMap.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, curr_dt.clone());

            System.out.println("singleAuthorizeMap: " + singleAuthorizeMap);

            ClientUtil.execute("authorizeAgentData", singleAuthorizeMap);
            viewType = -1;
            btnSave.setEnabled(true);
            btnCancelActionPerformed(null);
        }
    }

    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }

    private void popUp(int field) {
        final HashMap viewMap = new HashMap();
        viewType = field;
        HashMap whereMap = new HashMap();
        HashMap operativeMap = new HashMap();
        HashMap depositMap = new HashMap();

        whereMap.put("BRANCHID", getSelectedBranchID());

        if (field == EDIT || field == DELETE || field == AUTHORIZE) { //Edit=0 and Delete=1

            ArrayList lst = new ArrayList();
            lst.add("AGENT ID");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;

            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            viewMap.put(CommonConstants.MAP_NAME, "viewAgentData");
            new ViewAll(this, viewMap, false).show();

            //            viewMap.put(CommonConstants.MAP_WHERE, operativeMap);
            //            viewMap.put(CommonConstants.MAP_NAME, "viewOperativeDetails");
            //            new ViewAll(this, viewMap, false).show();
            //
            //            viewMap.put(CommonConstants.MAP_WHERE, depositMap);
            //            viewMap.put(CommonConstants.MAP_NAME, "viewDepositIdForCustomer");
            //            new ViewAll(this, viewMap, false).show();

        } else if (field == AGENTID) {
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            viewMap.put(CommonConstants.MAP_NAME, "getAgentDetails");
            new ViewAll(this, viewMap, true).show();
        } else if (field == ACCNO) {
            //            operativeMap.put("AGENT_ID",txtAgentID.getText());
            viewMap.put(CommonConstants.MAP_WHERE, operativeMap);
            viewMap.put(CommonConstants.MAP_NAME, "getOperativeDetails");
            new ViewAll(this, viewMap, true).show();
            System.out.println("getOperativeDetails :" + operativeMap);
        } else if (field == DEPOSITNO) {
            //            depositMap.put("AGENT_ID",txtAgentID.getText());
            viewMap.put(CommonConstants.MAP_WHERE, depositMap);
            System.out.println("depositMap : " + depositMap);
            viewMap.put(CommonConstants.MAP_NAME, "getDepositIdForCustomer");
            new ViewAll(this, viewMap, true).show();

        }
    }

    public Date getProperDateFormat(Object obj) {
        Date currDate = null;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            currDate = (Date) curr_dt.clone();
            currDate.setDate(tempDt.getDate());
            currDate.setMonth(tempDt.getMonth());
            currDate.setYear(tempDt.getYear());
        }
        return currDate;
    }
    
    // this method is called automatically from ViewAll...
    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
        HashMap OAprodDescMap = new HashMap();
        HashMap depProdDescMap = new HashMap();

        System.out.println("hash:  " + hash);

        if (viewType == EDIT || viewType == DELETE || viewType == AUTHORIZE) {
            hash.put("AGENTID", hash.get("AGENT ID"));
            hash.put("BRANCHID", getSelectedBranchID());

            observable.populateData(hash);     //__ Called to display the Data in the UI fields...

            //__ To Set the Value of the Agent Name...
            //            observable.setLblName(CommonUtil.convertObjToStr(hash.get("AGENT NAME")));
            //            observable.setOperativeAcc(CommonUtil.convertObjToStr(hash.get("OA_ACT_NUM")));
            //            observable.setLblDepositName(CommonUtil.convertObjToStr(depProdDescMap.get("PROD_DESC")));
            //            observable.setLblProdIdlName(CommonUtil.convertObjToStr(OAprodDescMap.get("PROD_DESC")));
            //            observable.setAgentTabData();

            //__ If the Action type is Delete...
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || viewType == AUTHORIZE) {
                ClientUtil.enableDisable(this, false);     //__ Disables the panel...

                //__ If the Action Type is Edit...
            } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                ClientUtil.enableDisable(this, true);     //__ Anable the panel...

                //__ Get the Auth Status...
                //                final String AUTHSTATUS = observable.getAuthStatus();
                //                if(AUTHSTATUS.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)){
                //                    //__ Make Remarks as Editable an rest all UnEditable...
                //                    setPanEnable();
                //
                //                }else{
                ////                    txtAgentID.setEditable(false);
                ////                    txtCommisionCreditedTo.setEditable(false);
                ////                    txtDepositCreditedTo.setEditable(true);
                //                }
            }

            //            btnAgentID.setEnabled(false);
            observable.setStatus();             //__ To set the Value of lblStatus...
            setButtonEnableDisable();           //__ Enables or Disables the buttons and menu Items depending on their previous state...

        } else if (viewType == AGENTID) {
            //__ To Set the Data Regarding the Customer in the Screen...
            //            txtAgentID.setText(CommonUtil.convertObjToStr(hash.get("AGENT ID")));
            //            lblNameValue.setText(CommonUtil.convertObjToStr(hash.get("AGENT NAME")));
            //            observable.setTxtAgentID(CommonUtil.convertObjToStr(hash.get("AGENT ID")));
            //            observable.setLblName(CommonUtil.convertObjToStr(hash.get("AGENT NAME")));
            //            observable.setAgentTabData();
            //
            //            observable.ttNotifyObservers();
        } else if (viewType == ACCNO) {
            //            txtCommisionCreditedTo.setText(CommonUtil.convertObjToStr(hash.get("OA_ACT_NUM")));
            //            OAprodDescMap.put("PROD_DESC", hash.get("PROD_ID"));
            List lst = ClientUtil.executeQuery("getOAProdDescription", hash);
            OAprodDescMap = (HashMap) lst.get(0);
            //            lblProdIdlName.setText(CommonUtil.convertObjToStr(OAprodDescMap.get("PROD_DESC")));
        } else if (viewType == DEPOSITNO) {
            //            depProdDescMap.put("PROD_DESC", hash.get("PROD_ID"));
            List lst = ClientUtil.executeQuery("getDepositProdDescription", hash);
            depProdDescMap = (HashMap) lst.get(0);
            //            txtDepositCreditedTo.setText(CommonUtil.convertObjToStr(hash.get("DEP_ACT_NUM")));
            //            lblDepositName.setText(CommonUtil.convertObjToStr(depProdDescMap.get("PROD_DESC")));
        }

        //__ To Save the data in the Internal Frame...
        setModified(true);
    }

    private void setPanEnable() {
        //        txtAgentID.setEditable(false);
        //        txtAgentID.setEnabled(false);
        //        tdtApptDate.setEnabled(false);
        //        txtRemarks.setEnabled(true);
        //        txtRemarks.setEditable(true);
    }
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // TODO add your handling code here:
        btnCloseActionPerformed(null);
    }//GEN-LAST:event_mitCloseActionPerformed

    private void mitPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitPrintActionPerformed
        // TODO add your handling code here:
        btnPrintActionPerformed(null);
    }//GEN-LAST:event_mitPrintActionPerformed

    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // TODO add your handling code here:
        btnCancelActionPerformed(null);
    }//GEN-LAST:event_mitCancelActionPerformed

    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // TODO add your handling code here:
        btnSaveActionPerformed(null);
    }//GEN-LAST:event_mitSaveActionPerformed

    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // TODO add your handling code here:
        btnDeleteActionPerformed(null);
    }//GEN-LAST:event_mitDeleteActionPerformed

    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // TODO add your handling code here:
        btnEditActionPerformed(null);
    }//GEN-LAST:event_mitEditActionPerformed

    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // TODO add your handling code here:
        btnNewActionPerformed(null);
    }//GEN-LAST:event_mitNewActionPerformed

    /**
     * Exit the Application
     */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm

private void cboProductIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProductIDActionPerformed
        // TODO add your handling code here:
    String prodId = ((ComboBoxModel)cboProductID.getModel()).getKeyForSelected().toString();
    String agentId = "";
    if(prodId!=null && prodId.length()>0){
        observable.setCboProdId(prodId);
        if(!rdoAmtBasedCollected.isSelected() && !rdoAccountBased.isSelected()){
            ClientUtil.showAlertWindow("Please select Commision based on amount collected or Commision based on account opened");
            cboProductID.setSelectedItem("");
            return;
        }
            if(cboAgentId.getSelectedItem()!=null && !cboAgentId.getSelectedItem().equals("")){            
                agentId = ((ComboBoxModel)cboAgentId.getModel()).getKeyForSelected().toString();
                observable.setAgentId(agentId);
                HashMap standMap = new HashMap();
                standMap =observable.standingProcessedDetails(prodId, agentId); 
                if(standMap!=null && !standMap.equals("null") && standMap.size()>0 ){
                    int yesNo = 0;
                    String[] options = {"Yes", "No"};
                    yesNo = COptionPane.showOptionDialog(null, "Last Processed Date is :"+DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(standMap.get("LAST_DT")))  +"  \n Do you want to continue?", CommonConstants.WARNINGTITLE,
                    COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                    null, options, options[0]);
                    if (yesNo == 0) { 
                        tdtFromDate.setDateValue(DateUtil.getStringDate(observable.calcuateFirstDateOfMonth(prodId,agentId)));                
                        if(!(tdtFromDate.getDateValue()!=null && !tdtFromDate.getDateValue().equals(""))){
                            cboProductID.setSelectedItem("");
                            tdtToDate.setDateValue(null);
                        }else{
                            tdtToDate.setDateValue(DateUtil.getStringDate(observable.calcuateLastDateOfMonth(getProperDateFormat(tdtFromDate.getDateValue()))));
                            observable.setTdtFromDate(tdtFromDate.getDateValue());
                            observable.setTdtToDate(tdtToDate.getDateValue());
                        }
                        tdtFromDate.setEnabled(false); 
                    }else{
                        btnCancelActionPerformed(null);
                    }
                }else{
                    int yesNo = 0;
                    String[] options = {"Yes", "No"};
                    yesNo = COptionPane.showOptionDialog(null, "No Standing instruction details Found!! \n Do you want to continue?", CommonConstants.WARNINGTITLE,
                    COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                    null, options, options[0]);
                    if (yesNo == 0) { 
                        tdtFromDate.setDateValue(DateUtil.getStringDate(observable.calcuateFirstDateOfMonth(prodId,agentId)));                
                        if(!(tdtFromDate.getDateValue()!=null && !tdtFromDate.getDateValue().equals(""))){
                            cboProductID.setSelectedItem("");
                            tdtToDate.setDateValue(null);
                        }else{
                            tdtToDate.setDateValue(DateUtil.getStringDate(observable.calcuateLastDateOfMonth(getProperDateFormat(tdtFromDate.getDateValue()))));
                            observable.setTdtFromDate(tdtFromDate.getDateValue());
                            observable.setTdtToDate(tdtToDate.getDateValue());
                        }
                        tdtFromDate.setEnabled(false); 
                    }else{
                        btnCancelActionPerformed(null);
                    }
                }
            }else{
                ClientUtil.showMessageWindow("Please select any Agent!!");
                lblNameForAgent.setText("");
                return;
            }
    }
}//GEN-LAST:event_cboProductIDActionPerformed

private void cboProdtypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdtypeActionPerformed
// TODO add your handling code here:
    if(cboProdtype.getSelectedIndex() > 0) {
        if(!rdoAmtBasedCollected.isSelected() && !rdoAccountBased.isSelected()){
            ClientUtil.showAlertWindow("Please select Commision based on amount collected or Commision based on account opened");
            cboProdtype.setSelectedItem("");
            return;
        }
            String prodType = ((ComboBoxModel)cboProdtype.getModel()).getKeyForSelected().toString();
            observable.setCboProdtype(prodType);
            if (prodType!=null && prodType.equals("GL")) {
                cboProductID.setSelectedItem("");
                cboProductID.setEnabled(false);
                observable.setCboProdtype(prodType);
            } else {
                observable.setCboProdtype(prodType);
                observable.cboCreditProductId();
                cboProductID.setModel(observable.getCbmProdId());
                cboProductID.setEnabled(true);
                cboProductID.setSelectedItem(observable.getCbmProdId().getDataForKey(observable.getCboProdId()));
            }
        }   
}//GEN-LAST:event_cboProdtypeActionPerformed

private void rdoSlabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoSlabActionPerformed
// TODO add your handling code here:
    if(rdoSlab.isSelected()){
        txtAgentcomm.setEnabled(false);
        observable.setRdoDefaultPercentage(false);
        observable.setRdoManualPercentage(false);
        observable.setRdoSlabWise(true);
    }
}//GEN-LAST:event_rdoSlabActionPerformed

private void rdoPercentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoPercentActionPerformed
// TODO add your handling code here:
    if(rdoPercent.isSelected()){
        txtAgentcomm.setEnabled(true);
        observable.setRdoDefaultPercentage(false);
        observable.setRdoManualPercentage(true);
        observable.setRdoSlabWise(false);
    }
}//GEN-LAST:event_rdoPercentActionPerformed

    private void rdoDefaultPercentageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDefaultPercentageActionPerformed
        // TODO add your handling code here:
        observable.setRdoDefaultPercentage(true);
        observable.setRdoManualPercentage(false);
        observable.setRdoSlabWise(false);
    }//GEN-LAST:event_rdoDefaultPercentageActionPerformed

    private void rdoAmtBasedCollectedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoAmtBasedCollectedActionPerformed
        // TODO add your handling code here:
        rdoSlab.setVisible(true);
        rdoPercent.setVisible(true);
        rdoDefaultPercentage.setVisible(true);
        lblCommisionOpenedAct.setVisible(false);
        txtCommisionOpenedAct.setVisible(false);
        txtAgentcomm.setVisible(true);
        cLabel1.setVisible(true);
        lblAgentPercentage.setVisible(true);
        txtCommision.setVisible(true);
        lblCommision.setVisible(true);
        txtCommisionForThePeriod.setVisible(true);
        lblCommisionForThePeriod.setVisible(true);
        observable.setRdoAccountBased(false);
        observable.setRdoAmountBased(true);
        rdoAccountBased.setSelected(false);
        rdoAmtBasedCollected.setSelected(true);

    }//GEN-LAST:event_rdoAmtBasedCollectedActionPerformed

    private void rdoAccountBasedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoAccountBasedActionPerformed
        // TODO add your handling code here:
        rdoSlab.setVisible(false);
        rdoPercent.setVisible(false);
        rdoDefaultPercentage.setVisible(false);
        lblCommisionOpenedAct.setVisible(true);
        txtCommisionOpenedAct.setVisible(true);
        txtCommisionOpenedAct.setEnabled(false);
        txtAgentcomm.setVisible(false);
        cLabel1.setVisible(false);
        lblAgentPercentage.setVisible(false);
        txtCommision.setVisible(false);
        lblCommision.setVisible(false);
        txtCommisionForThePeriod.setVisible(false);
        lblCommisionForThePeriod.setVisible(false);
        observable.setRdoAccountBased(true);
        observable.setRdoAmountBased(false);
        rdoAccountBased.setSelected(true);
        rdoAmtBasedCollected.setSelected(false);
    }//GEN-LAST:event_rdoAccountBasedActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new AgentCommisionDisbursalUI().show();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButtonGroup btnGrpCommType;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPayDetails;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CComboBox cboAgentId;
    private com.see.truetransact.uicomponent.CComboBox cboProdtype;
    private com.see.truetransact.uicomponent.CComboBox cboProductID;
    private com.see.truetransact.uicomponent.CCheckBox chkCash;
    private com.see.truetransact.uicomponent.CCheckBox chkTransfer;
    private com.see.truetransact.uicomponent.CButtonGroup chkTranstypeGroup;
    private com.see.truetransact.uicomponent.CLabel lblAgentID;
    private com.see.truetransact.uicomponent.CLabel lblAgentName;
    private com.see.truetransact.uicomponent.CLabel lblAgentPercentage;
    private com.see.truetransact.uicomponent.CLabel lblCollectionsDuringThePeriod;
    private com.see.truetransact.uicomponent.CLabel lblCommTD;
    private com.see.truetransact.uicomponent.CLabel lblCommision;
    private com.see.truetransact.uicomponent.CLabel lblCommisionForThePeriod;
    private com.see.truetransact.uicomponent.CLabel lblCommisionOpenedAct;
    private com.see.truetransact.uicomponent.CLabel lblComtoOA;
    private com.see.truetransact.uicomponent.CLabel lblFromDate;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNameForAgent;
    private com.see.truetransact.uicomponent.CLabel lblProdId;
    private com.see.truetransact.uicomponent.CLabel lblProdtype;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace29;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace30;
    private com.see.truetransact.uicomponent.CLabel lblSpace31;
    private com.see.truetransact.uicomponent.CLabel lblSpace32;
    private com.see.truetransact.uicomponent.CLabel lblSpace33;
    private com.see.truetransact.uicomponent.CLabel lblSpace34;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblStatus1;
    private com.see.truetransact.uicomponent.CLabel lblTDS;
    private com.see.truetransact.uicomponent.CLabel lblTdsCommission;
    private com.see.truetransact.uicomponent.CLabel lblToDate;
    private com.see.truetransact.uicomponent.CMenuBar mbrAgent;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAgent;
    private com.see.truetransact.uicomponent.CPanel panAgentData;
    private com.see.truetransact.uicomponent.CPanel panAgentDisplay;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panStatus1;
    private com.see.truetransact.uicomponent.CPanel panStatus2;
    private com.see.truetransact.uicomponent.CRadioButton rdoAccountBased;
    private com.see.truetransact.uicomponent.CRadioButton rdoAmtBasedCollected;
    private com.see.truetransact.uicomponent.CRadioButton rdoDefaultPercentage;
    private com.see.truetransact.uicomponent.CRadioButton rdoPercent;
    private com.see.truetransact.uicomponent.CRadioButton rdoSlab;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptPrint;
    private javax.swing.JSeparator sptProcess;
    private com.see.truetransact.uicomponent.CScrollPane srpDailyDepositDetails;
    private com.see.truetransact.uicomponent.CTable tblAgentCommission;
    private javax.swing.JToolBar tbrLoantProduct;
    private com.see.truetransact.uicomponent.CDateField tdtFromDate;
    private com.see.truetransact.uicomponent.CDateField tdtToDate;
    private com.see.truetransact.uicomponent.CTextField txtAgentcomm;
    private com.see.truetransact.uicomponent.CTextField txtCommTD;
    private com.see.truetransact.uicomponent.CTextField txtCommToOA;
    private com.see.truetransact.uicomponent.CTextField txtCommision;
    private com.see.truetransact.uicomponent.CTextField txtCommisionDuringThePeriod;
    private com.see.truetransact.uicomponent.CTextField txtCommisionForThePeriod;
    private com.see.truetransact.uicomponent.CTextField txtCommisionOpenedAct;
    private com.see.truetransact.uicomponent.CTextField txtTDS;
    private com.see.truetransact.uicomponent.CTextField txtTdsCommission;
    // End of variables declaration//GEN-END:variables
}
