/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AgentCommisionForAllProductsUI.java
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
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.uicomponent.COptionPane;

import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.uivalidation.NumericValidation;
import java.awt.Checkbox;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Observer;
import java.util.Observable;
import java.util.List;
import java.util.Date;
import javax.swing.event.TableModelListener;

/**
 *
 * @author 152721
 */
public class AgentCommisionForAllProductsUI extends CInternalFrame implements java.util.Observer, UIMandatoryField {

    HashMap mandatoryMap;
    AgentCommisionDisbursalOB observable;
    //    AgentRB resourceBundle = new AgentRB();
    //    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.transaction.AgentCommisionDisbursalRB", ProxyParameters.LANGUAGE);
    java.util.ResourceBundle resourceBundle, objMandatoryRB;
    final int EDIT = 0, DELETE = 1, AUTHORIZE = 2, AGENTID = 3, ACCNO = 4, DEPOSITNO = 5;
    int viewType = -1;
    boolean cbo = false;
    private Date curr_dt = null;   
    int updateTab=-1;
    private TableModelListener tableModelListener;
    /**
     * Creates new form AgentUI
     */
    public AgentCommisionForAllProductsUI(String are) {
        initComponents();
        initSetup();
        btnAdd.setEnabled(false);
    }

    public AgentCommisionForAllProductsUI() {
        initComponents();
        initSetup();
        btnAdd.setEnabled(false);

    }

    private void initSetup() {
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setObservable();
        setMaxLenths();
        initComponentsData();       
        //        lblCommision.setE(false);
        ClientUtil.enableDisable(this, false); //__ Disables all when the screen appears for the 1st time
        setButtonEnableDisable();
        //__ Enables/Disables the necessary buttons and menu items...
        txtTdsCommission.setAllowNumber(true);
        observable.resetForm();  
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
        tblAgentCommission.getModel().addTableModelListener(tableModelListener);
        btnAdd.setEnabled(false);
        cboAgentId.setEnabled(false);
        txtTdsCommission.setEnabled(false);
        tdtCalcCommUpto.setDateValue(DateUtil.getStringDate(curr_dt));
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
        cboAgentId.setName("cboAgentId");
        //cboProdtype.setName("cboProdtype");
        txtTdsCommission.setName("txtTdsCommission");
        btnAdd.setName("btnPayDetails");
        panAgentData.setName("panAgentData");

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
        btnAdd.setText(resourceBundle.getString("btnPayDetails"));


    }

    public void initComponentsData() {
        cboAgentId.setModel(observable.getCbmAgentId());
      //  cboProdtype.setModel(observable.getCbmProdtype());
    }

    /*
     * Auto Generated Method - update() This method called by Observable. It
     * updates the UI with Observable's data. If needed add/Remove RadioButtons
     * method need to be added.
     */
    public void update(Observable observed, Object arg) {
        cboAgentId.setSelectedItem(observable.getCboAgentId());
    }

    /*
     * Auto Generated Method - updateOBFields() This method called by Save
     * option of UI. It updates the OB with UI data.
     */
    public void updateOBFields() {
        observable.setScreen(getScreen());
        observable.setModule(getModule());
        observable.setSelectedBranchID(getSelectedBranchID());
        observable.setTdtCalcCommUpTo(tdtCalcCommUpto.getDateValue()); // Added by nithya on 26-10-2016 for 3177
        if (chkTransfer.isSelected()) {
            observable.setTransType(CommonConstants.TX_TRANSFER);
        } else if (chkCash.isSelected()) {
            observable.setTransType(CommonConstants.TX_CASH);
        }
        observable.setAgentId(((ComboBoxModel)cboAgentId.getModel()).getKeyForSelected().toString());        
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
        //        cboAgentId.setHelpMessage(lblMsg, objMandatoryRB.getString("cboAgentId"));
        //        txtDepositCreditedTo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDepositCreditedTo"));

    }

    private void setMaxLenths() {
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
        panAgent = new com.see.truetransact.uicomponent.CPanel();
        panAgentData = new com.see.truetransact.uicomponent.CPanel();
        lblAgentName = new com.see.truetransact.uicomponent.CLabel();
        cboAgentId = new com.see.truetransact.uicomponent.CComboBox();
        lblAgentID = new com.see.truetransact.uicomponent.CLabel();
        lblNameForAgent = new com.see.truetransact.uicomponent.CLabel();
        btnAdd = new com.see.truetransact.uicomponent.CButton();
        lblTdsCommission = new com.see.truetransact.uicomponent.CLabel();
        txtTdsCommission = new com.see.truetransact.uicomponent.CTextField();
        chkTransfer = new com.see.truetransact.uicomponent.CCheckBox();
        chkCash = new com.see.truetransact.uicomponent.CCheckBox();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        lblCommUpto = new com.see.truetransact.uicomponent.CLabel();
        tdtCalcCommUpto = new com.see.truetransact.uicomponent.CDateField();
        panAgentDisplay = new com.see.truetransact.uicomponent.CPanel();
        srpPrintPan = new com.see.truetransact.uicomponent.CScrollPane();
        tblAgentCommission = new com.see.truetransact.uicomponent.CTable();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus1 = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        tbrLoantProduct = new javax.swing.JToolBar();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace70 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace71 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace72 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace73 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace74 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace75 = new com.see.truetransact.uicomponent.CLabel();
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
        panAgent.setMinimumSize(new java.awt.Dimension(412, 500));
        panAgent.setPreferredSize(new java.awt.Dimension(412, 500));
        panAgent.setLayout(new java.awt.GridBagLayout());

        panAgentData.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panAgentData.setMinimumSize(new java.awt.Dimension(500, 300));
        panAgentData.setPreferredSize(new java.awt.Dimension(550, 150));
        panAgentData.setLayout(new java.awt.GridBagLayout());

        lblAgentName.setText("Agent Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 175, 0, 0);
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
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 10, 0, 0);
        panAgentData.add(cboAgentId, gridBagConstraints);

        lblAgentID.setText("Agent ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 197, 0, 0);
        panAgentData.add(lblAgentID, gridBagConstraints);

        lblNameForAgent.setMaximumSize(new java.awt.Dimension(100, 21));
        lblNameForAgent.setMinimumSize(new java.awt.Dimension(100, 21));
        lblNameForAgent.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 81;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 10, 0, 0);
        panAgentData.add(lblNameForAgent, gridBagConstraints);

        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/Down_Arrow.gif"))); // NOI18N
        btnAdd.setText("Calculate Commision");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 10, 9, 0);
        panAgentData.add(btnAdd, gridBagConstraints);

        lblTdsCommission.setText("TDSCommission");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 150, 0, 0);
        panAgentData.add(lblTdsCommission, gridBagConstraints);

        txtTdsCommission.setMaximumSize(new java.awt.Dimension(100, 21));
        txtTdsCommission.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 10, 0, 0);
        panAgentData.add(txtTdsCommission, gridBagConstraints);

        chkTranstypeGroup.add(chkTransfer);
        chkTransfer.setText("Transfer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 1, 0, 0);
        panAgentData.add(chkTransfer, gridBagConstraints);

        chkTranstypeGroup.add(chkCash);
        chkCash.setText("Cash");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 0, 87);
        panAgentData.add(chkCash, gridBagConstraints);

        cLabel1.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 2, 0, 0);
        panAgentData.add(cLabel1, gridBagConstraints);

        lblCommUpto.setText("Calc Commision Upto");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 121, 0, 0);
        panAgentData.add(lblCommUpto, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 10, 0, 0);
        panAgentData.add(tdtCalcCommUpto, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(15, 20, 15, 20);
        panAgent.add(panAgentData, gridBagConstraints);

        panAgentDisplay.setMinimumSize(new java.awt.Dimension(400, 150));
        panAgentDisplay.setPreferredSize(new java.awt.Dimension(620, 300));
        panAgentDisplay.setLayout(new java.awt.GridBagLayout());

        srpPrintPan.setMinimumSize(new java.awt.Dimension(800, 350));
        srpPrintPan.setPreferredSize(new java.awt.Dimension(600, 280));

        tblAgentCommission.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Select", "TransId", "Product", "Name", "Amount", "Account No", "Trans Type", "Module", "Single TransID"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblAgentCommission.setPreferredSize(new java.awt.Dimension(600, 600));
        tblAgentCommission.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblAgentCommissionMouseClicked(evt);
            }
        });
        srpPrintPan.setViewportView(tblAgentCommission);

        panAgentDisplay.add(srpPrintPan, new java.awt.GridBagConstraints());

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

        lblSpace70.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace70.setText("     ");
        lblSpace70.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace70.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace70.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace70);

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

        lblSpace71.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace71.setText("     ");
        lblSpace71.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace71.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace71.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace71);

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

        lblSpace72.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace72.setText("     ");
        lblSpace72.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace72.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace72.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace72);

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

        lblSpace73.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace73.setText("     ");
        lblSpace73.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace73);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.setEnabled(false);
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnException);

        lblSpace74.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace74.setText("     ");
        lblSpace74.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace74);

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

        lblSpace75.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace75.setText("     ");
        lblSpace75.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace75);

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

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        cbo = true;
        observable.resetForm();              // to Reset all the Fields and Status in UI...
        //        txtCommTD.setText("");
        //        txtCommToOA.setText("");
        //        txtTDS.setText("");
        btnAdd.setEnabled(true);
        //        observable.resetTable();
        setButtonEnableDisable();             // Enables/Disables the necessary buttons and menu items...
        ClientUtil.enableDisable(this, true); // Enables the panel...
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);   //Sets the Action Type to be performed...
        observable.setStatus(); 
        btnAdd.setEnabled(true);
        cboAgentId.setEnabled(true);
        txtTdsCommission.setEnabled(true);
        txtTdsCommission.setText("0");
        setModified(true);
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
        
        boolean countSelect = false;
        if(tblAgentCommission.getRowCount()>0){
            for (int i = 0; i < tblAgentCommission.getRowCount(); i++) {
                if ((Boolean) tblAgentCommission.getValueAt(i, 0)) {
                    countSelect = true;                    
                }
            }
            if (!countSelect) {
                ClientUtil.showMessageWindow(" NO Rows Selected !!! ");
                countSelect = false;
                return;
            }else{
                if(chkCash.isSelected() || chkTransfer.isSelected()){
                    updateOBFields();
                    HashMap dataMap =null;
                    int yesno = COptionPane.showConfirmDialog(this, "Do you want to Post This Agent  " + lblNameForAgent.getText() + "\n Agent  Commission ?", "Note", COptionPane.YES_NO_OPTION);
                    if (yesno == COptionPane.YES_OPTION) {
                        int count = tblAgentCommission.getRowCount();
                        if(count > 0){                
                            for(int i=0;i<count;i++){
                                if((Boolean)tblAgentCommission.getValueAt(i, 0)){
                                    dataMap = new HashMap();
                                    dataMap.put("COMMISION_PROD_ID", CommonUtil.convertObjToStr(tblAgentCommission.getValueAt(i, 2)));
                                    dataMap.put("COMMISION_AMT", CommonUtil.convertObjToStr(tblAgentCommission.getValueAt(i, 5)));
                                    dataMap.put("FROM_DT", CommonUtil.convertObjToStr(tblAgentCommission.getValueAt(i, 3)));
                                    dataMap.put("VAT_AMT", CommonUtil.convertObjToStr(tblAgentCommission.getValueAt(i, 6)));
                                    dataMap.put("COLLECTION_AMT", CommonUtil.convertObjToStr(tblAgentCommission.getValueAt(i, 4)));
                                    if(chkTransfer.isSelected()){
                                        dataMap.put("FROM_AGENT_COMM_SCREEN","FROM_AGENT_COMM_SCREEN");
                                    }
                                    System.out.println("dataMap#^#^^#^"+dataMap);
                                    observable.doAllAction(dataMap);
                                }
                            }                
                        }            
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
                }else{
                    ClientUtil.showMessageWindow("Please Select Cash or Transfer!!");
                    return;
                }
            }
        }else{
             ClientUtil.showMessageWindow("Please Calculate Commision!!");
            return;
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
        lblNameForAgent.setText("");
        txtTdsCommission.setText("");
        btnNew.setEnabled(true);
        btnAdd.setEnabled(false);
        cboAgentId.setEnabled(false);
        txtTdsCommission.setEnabled(false);
        //cboProdtype.setSelectedItem("");
        //cboProductID.setSelectedItem("");
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
    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        if(cboAgentId.getSelectedItem()!=null && !cboAgentId.getSelectedItem().equals("")){             
            setAgentCommisionTable();
        }else{
            ClientUtil.showMessageWindow("Please select AgentID!!");
            return;
        }   
    }//GEN-LAST:event_btnAddActionPerformed

    
    private void setAgentCommisionTable() {
        // btnReject.setVisible(true);
        HashMap shareTypeMap = new HashMap();
        HashMap whereMap = new HashMap();
        //whereMap.put("CURR_DT", curr_dt.clone());
        Date calcCommUpTo = CommonUtil.getProperDate(curr_dt,DateUtil.getDateMMDDYYYY(tdtCalcCommUpto.getDateValue()));
        whereMap.put("CURR_DT", calcCommUpTo);
        whereMap.put("AGENT_ID", CommonUtil.convertObjToStr(observable.getCbmAgentId().getKeyForSelected()));  
        whereMap.put("VAT_PER", CommonUtil.convertObjToDouble(txtTdsCommission.getText()));           
        shareTypeMap.put(CommonConstants.MAP_NAME, "getAgentAllProductCollectionAmt");    
        shareTypeMap.put(CommonConstants.MAP_WHERE, whereMap);
        try {           
           System.out.println("shareTypeMap=======" + shareTypeMap);
           if(observable.populateData(shareTypeMap, tblAgentCommission)){
               tblAgentCommission.setModel(observable.getTbmAgentCommission());  
               tblAgentCommission.editCellAt(0, 0);
               tblAgentCommission.setEditingColumn(0);
           }else{
               ClientUtil.showMessageWindow("No Commision Details Found!!");
               return;
           }
        } catch (Exception e) {
            System.err.println("Exception " + e.toString() + "Caught");
            e.printStackTrace();
        } 
    }
    
    private void updateTable() {
        tblAgentCommission.setModel(observable.getTbmAgentCommission());
     }

   private Date setProperDtFormat(Date dt) {
        Date tempDt = (Date) curr_dt.clone();
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }
    private void cboAgentIdItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboAgentIdItemStateChanged
        //        observable.agentName();
        //        lblNameForAgent.setText; // TODO add your handling code here:
    }//GEN-LAST:event_cboAgentIdItemStateChanged

    private void cboAgentIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboAgentIdActionPerformed
        String prodId = "";
        String agentId = "";
        if(cboAgentId.getSelectedItem()!=null && !cboAgentId.getSelectedItem().equals("")){
            agentId = ((ComboBoxModel)cboAgentId.getModel()).getKeyForSelected().toString();
            System.out.println("agentId^$^$^$^^^$^"+agentId);
            lblNameForAgent.setText(CommonUtil.convertObjToStr(((ComboBoxModel)cboAgentId.getModel()).getKeyForSelected()));           
            observable.setCboAgentId(agentId);
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

private void tblAgentCommissionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAgentCommissionMouseClicked
// TODO add your handling code here:
    if(evt!=null){
        if(tblAgentCommission.getRowCount()>0){
            if(!(Boolean)tblAgentCommission.getValueAt(tblAgentCommission.getSelectedRow(), 0)){
                tblAgentCommission.setValueAt(Boolean.TRUE, tblAgentCommission.getSelectedRow(), 0);
            }else{
                tblAgentCommission.setValueAt(Boolean.FALSE, tblAgentCommission.getSelectedRow(), 0);
            }
        }
    }
}//GEN-LAST:event_tblAgentCommissionMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new AgentCommisionForAllProductsUI().show();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAdd;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CComboBox cboAgentId;
    private com.see.truetransact.uicomponent.CCheckBox chkCash;
    private com.see.truetransact.uicomponent.CCheckBox chkTransfer;
    private com.see.truetransact.uicomponent.CButtonGroup chkTranstypeGroup;
    private com.see.truetransact.uicomponent.CLabel lblAgentID;
    private com.see.truetransact.uicomponent.CLabel lblAgentName;
    private com.see.truetransact.uicomponent.CLabel lblCommUpto;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNameForAgent;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace70;
    private com.see.truetransact.uicomponent.CLabel lblSpace71;
    private com.see.truetransact.uicomponent.CLabel lblSpace72;
    private com.see.truetransact.uicomponent.CLabel lblSpace73;
    private com.see.truetransact.uicomponent.CLabel lblSpace74;
    private com.see.truetransact.uicomponent.CLabel lblSpace75;
    private com.see.truetransact.uicomponent.CLabel lblStatus1;
    private com.see.truetransact.uicomponent.CLabel lblTdsCommission;
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
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptPrint;
    private javax.swing.JSeparator sptProcess;
    private com.see.truetransact.uicomponent.CScrollPane srpPrintPan;
    private com.see.truetransact.uicomponent.CTable tblAgentCommission;
    private javax.swing.JToolBar tbrLoantProduct;
    private com.see.truetransact.uicomponent.CDateField tdtCalcCommUpto;
    private com.see.truetransact.uicomponent.CTextField txtTdsCommission;
    // End of variables declaration//GEN-END:variables
}
