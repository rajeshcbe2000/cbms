/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AgentCommisionSlabRateUI.java
 *
 * Created on February 2, 2005, 12:20 PM
 */
package com.see.truetransact.ui.agent.agentcommisionslabrate;

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
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.customer.CheckCustomerIdUI;
import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Observer;
import java.util.Observable;
import java.util.List;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uivalidation.NumericValidation;

/**
 *
 * @author 152721
 */
public class AgentCommisionSlabRateUI extends CInternalFrame implements java.util.Observer, UIMandatoryField {

    HashMap mandatoryMap;
    AgentCommisionSlabRateOB observable;
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.agent.agentcommisionslabrate.AgentCommisionSlabRateRB", ProxyParameters.LANGUAGE);
    final int EDIT = 0, DELETE = 1, AUTHORIZE = 2, AGENTID = 3, ACCNO = 4, DEPOSITNO = 5, VIEW = 10, EDIT_LEAVE_DETAILS = 12;
    final int COMM_CREDIT_ACHD = 6, COMM_COLLECT_ACHD = 7, ACT_INTRO_ACHD = 8, TDS_ACHD = 9;
    int viewType = -1;
    private Date currDt = null;
    private boolean updateAgentCommisionMode = false;
    int updateTab = -1;

    /**
     * Creates new form AgentCommisionSlabRateUI
     */
    public AgentCommisionSlabRateUI() {
        initComponents();
        initSetup();
        initComponentData();
    }

    private void initSetup() {
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setObservable();
        setMaxLenths();
        ClientUtil.enableDisable(this, false); //__ Disables all when the screen appears for the 1st time
        setButtonEnableDisable();              //__ Enables/Disables the necessary buttons and menu items...
        observable.resetForm();                //__ Resets the Data in the Form...
        observable.resetTable();
        observable.resetStatus();              //__ to reset the status...
        currDt = ClientUtil.getCurrentDate();
        cboProdType.setModel(observable.getCbmProductType());
        cboInstType.setVisible(false);
        lblInstType.setVisible(false);
        btnTabNew.setEnabled(false);
        btnTabSave.setEnabled(false);
        btnTabDelete.setEnabled(false);
        btnCommisionCreditedHd.setEnabled(false);
        btnCommisionCollectHd.setEnabled(false);
        btnActIntroHd.setEnabled(false);
        btnTdsHd.setEnabled(false);
    }

    private void setObservable() {
        try {
            observable = new AgentCommisionSlabRateOB();
            observable.addObserver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        lblMsg.setName("lblMsg");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblStatus1.setName("lblStatus1");
        mbrAgent.setName("mbrAgent");
        panStatus.setName("panStatus");
    }

    /*
     * Auto Generated Method - internationalize() This method used to assign
     * display texts from the Resource Bundle File.
     */
    private void internationalize() {
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnClose.setText(resourceBundle.getString("btnClose"));
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
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
    }

    /*
     * Auto Generated Method - update() This method called by Observable. It
     * updates the UI with Observable's data. If needed add/Remove RadioButtons
     * method need to be added.
     */
    public void update(Observable observed, Object arg) {
        //To set the Status...
        lblStatus1.setText(observable.getLblStatus());
    }

    /*
     * Auto Generated Method - updateOBFields() This method called by Save
     * option of UI. It updates the OB with UI data.
     */
    public void updateOBFields() {
        //__ To update the BranchSelected...
        observable.setSelectedBranchID(getSelectedBranchID());
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
        mandatoryMap.put("txtRemarks", new Boolean(false));
        mandatoryMap.put("txtAgentID", new Boolean(false));
        mandatoryMap.put("tdtApptDate", new Boolean(false));
    }

    /*
     * Auto Generated Method - getMandatoryHashMap() Getter method for
     * setMandatoryHashMap().
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
        AgentCommisionSlabRateMRB objMandatoryRB = new AgentCommisionSlabRateMRB();

    }

    private void setMaxLenths() {
        txtCommFromBank.setValidation(new NumericValidation(2, 2));
        txtCommFromActHolder.setValidation(new NumericValidation(2, 2));
        txtActIntroCommisionAmt.setValidation(new NumericValidation(14, 2));
        txtTds.setValidation(new NumericValidation(2, 2));
        txtFromAmt.setValidation(new NumericValidation(14, 2));
        txtToAmount.setValidation(new NumericValidation(14, 2));
    }

    // To set The Value of the Buttons Depending on the Value or Condition...
    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        btnDelete.setEnabled(!btnDelete.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());
        btnSave.setEnabled(!btnSave.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus1 = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        tbrLoantProduct = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
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
        tabAgent = new com.see.truetransact.uicomponent.CTabbedPane();
        panFloatingRateAccount = new com.see.truetransact.uicomponent.CPanel();
        panAgentsCalculations = new com.see.truetransact.uicomponent.CPanel();
        lblCommisionCollectHd = new com.see.truetransact.uicomponent.CLabel();
        tdtFromDate = new com.see.truetransact.uicomponent.CDateField();
        lblTdsHd = new com.see.truetransact.uicomponent.CLabel();
        tdtToDate = new com.see.truetransact.uicomponent.CDateField();
        lblCommFromBank = new com.see.truetransact.uicomponent.CLabel();
        lblCommFromActHolder = new com.see.truetransact.uicomponent.CLabel();
        lblActIntroCommisionHd = new com.see.truetransact.uicomponent.CLabel();
        lblFromDate = new com.see.truetransact.uicomponent.CLabel();
        lblFromAmount = new com.see.truetransact.uicomponent.CLabel();
        panButtons = new com.see.truetransact.uicomponent.CPanel();
        btnTabNew = new com.see.truetransact.uicomponent.CButton();
        btnTabSave = new com.see.truetransact.uicomponent.CButton();
        btnTabDelete = new com.see.truetransact.uicomponent.CButton();
        txtFromAmt = new com.see.truetransact.uicomponent.CTextField();
        lblToDate = new com.see.truetransact.uicomponent.CLabel();
        lblActIntroCommisionAmt = new com.see.truetransact.uicomponent.CLabel();
        lblToAmount = new com.see.truetransact.uicomponent.CLabel();
        lblInstType = new com.see.truetransact.uicomponent.CLabel();
        cboInstType = new com.see.truetransact.uicomponent.CComboBox();
        lblProdType = new com.see.truetransact.uicomponent.CLabel();
        cboProdType = new com.see.truetransact.uicomponent.CComboBox();
        lblProdId = new com.see.truetransact.uicomponent.CLabel();
        cboProdId = new com.see.truetransact.uicomponent.CComboBox();
        panOA = new com.see.truetransact.uicomponent.CPanel();
        txtCommisionCollectTo = new com.see.truetransact.uicomponent.CTextField();
        btnCommisionCollectHd = new com.see.truetransact.uicomponent.CButton();
        lblCommisionCreditedTo = new com.see.truetransact.uicomponent.CLabel();
        panTds = new com.see.truetransact.uicomponent.CPanel();
        txtTdsHd = new com.see.truetransact.uicomponent.CTextField();
        btnTdsHd = new com.see.truetransact.uicomponent.CButton();
        panCommision = new com.see.truetransact.uicomponent.CPanel();
        txtCommisionCreditedTo = new com.see.truetransact.uicomponent.CTextField();
        btnCommisionCreditedHd = new com.see.truetransact.uicomponent.CButton();
        panCommFromBank = new com.see.truetransact.uicomponent.CPanel();
        lblDelayedFromAmt1 = new com.see.truetransact.uicomponent.CLabel();
        txtCommFromBank = new com.see.truetransact.uicomponent.CTextField();
        txtToAmount = new com.see.truetransact.uicomponent.CTextField();
        panActIntroCommHead = new com.see.truetransact.uicomponent.CPanel();
        txtActIntroCommisionHd = new com.see.truetransact.uicomponent.CTextField();
        btnActIntroHd = new com.see.truetransact.uicomponent.CButton();
        panTdsValue = new com.see.truetransact.uicomponent.CPanel();
        lblDelayedFromAmt2 = new com.see.truetransact.uicomponent.CLabel();
        txtTds = new com.see.truetransact.uicomponent.CTextField();
        panActIntroValue = new com.see.truetransact.uicomponent.CPanel();
        lblDelayedFromAmt3 = new com.see.truetransact.uicomponent.CLabel();
        txtActIntroCommisionAmt = new com.see.truetransact.uicomponent.CTextField();
        lblTds = new com.see.truetransact.uicomponent.CLabel();
        panCommFromActHolder = new com.see.truetransact.uicomponent.CPanel();
        lblDelayedFromAmt4 = new com.see.truetransact.uicomponent.CLabel();
        txtCommFromActHolder = new com.see.truetransact.uicomponent.CTextField();
        lblCommCollectValue = new com.see.truetransact.uicomponent.CLabel();
        lblActIntroValue = new com.see.truetransact.uicomponent.CLabel();
        lblTdsValue = new com.see.truetransact.uicomponent.CLabel();
        lblCommCreditValue = new com.see.truetransact.uicomponent.CLabel();
        srpInterestTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblAgentCommisionTable = new com.see.truetransact.uicomponent.CTable();
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
        setMinimumSize(new java.awt.Dimension(825, 650));
        setPreferredSize(new java.awt.Dimension(825, 650));

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
        tbrLoantProduct.add(btnView);

        lblSpace5.setText("     ");
        tbrLoantProduct.add(lblSpace5);

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
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnClose);

        getContentPane().add(tbrLoantProduct, java.awt.BorderLayout.NORTH);

        tabAgent.setMinimumSize(new java.awt.Dimension(635, 650));
        tabAgent.setPreferredSize(new java.awt.Dimension(800, 650));

        panFloatingRateAccount.setMinimumSize(new java.awt.Dimension(861, 408));
        panFloatingRateAccount.setLayout(new java.awt.GridBagLayout());

        panAgentsCalculations.setLayout(new java.awt.GridBagLayout());

        lblCommisionCollectHd.setText("Commision Collect Ac Hd");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentsCalculations.add(lblCommisionCollectHd, gridBagConstraints);

        tdtFromDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtFromDate.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 39;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentsCalculations.add(tdtFromDate, gridBagConstraints);

        lblTdsHd.setText("Tds Ac Hd");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 28;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentsCalculations.add(lblTdsHd, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 41;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentsCalculations.add(tdtToDate, gridBagConstraints);

        lblCommFromBank.setText("Comm From Bank");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 31;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentsCalculations.add(lblCommFromBank, gridBagConstraints);

        lblCommFromActHolder.setText("Comm From Act Holder");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 32;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentsCalculations.add(lblCommFromActHolder, gridBagConstraints);

        lblActIntroCommisionHd.setText("Act Intro Commision Ac Hd");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 24;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentsCalculations.add(lblActIntroCommisionHd, gridBagConstraints);

        lblFromDate.setText("From Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 39;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentsCalculations.add(lblFromDate, gridBagConstraints);

        lblFromAmount.setText("From Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 43;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentsCalculations.add(lblFromAmount, gridBagConstraints);

        panButtons.setLayout(new java.awt.GridBagLayout());

        btnTabNew.setText("New");
        btnTabNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTabNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButtons.add(btnTabNew, gridBagConstraints);

        btnTabSave.setText("Save");
        btnTabSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTabSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButtons.add(btnTabSave, gridBagConstraints);

        btnTabDelete.setText("Delete");
        btnTabDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTabDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButtons.add(btnTabDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 46;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAgentsCalculations.add(panButtons, gridBagConstraints);

        txtFromAmt.setEnabled(false);
        txtFromAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtFromAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFromAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 43;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentsCalculations.add(txtFromAmt, gridBagConstraints);

        lblToDate.setText("To Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 41;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentsCalculations.add(lblToDate, gridBagConstraints);

        lblActIntroCommisionAmt.setText("Act Intro Commision Amt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 33;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentsCalculations.add(lblActIntroCommisionAmt, gridBagConstraints);

        lblToAmount.setText("To Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 45;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentsCalculations.add(lblToAmount, gridBagConstraints);

        lblInstType.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblInstType.setText("Installment Type");
        lblInstType.setMaximumSize(new java.awt.Dimension(105, 18));
        lblInstType.setMinimumSize(new java.awt.Dimension(105, 18));
        lblInstType.setPreferredSize(new java.awt.Dimension(105, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentsCalculations.add(lblInstType, gridBagConstraints);

        cboInstType.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        cboInstType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentsCalculations.add(cboInstType, gridBagConstraints);

        lblProdType.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblProdType.setText("Product Type");
        lblProdType.setMaximumSize(new java.awt.Dimension(105, 18));
        lblProdType.setMinimumSize(new java.awt.Dimension(105, 18));
        lblProdType.setPreferredSize(new java.awt.Dimension(105, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentsCalculations.add(lblProdType, gridBagConstraints);

        cboProdType.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        cboProdType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentsCalculations.add(cboProdType, gridBagConstraints);

        lblProdId.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblProdId.setText("Product Id");
        lblProdId.setMaximumSize(new java.awt.Dimension(105, 18));
        lblProdId.setMinimumSize(new java.awt.Dimension(105, 18));
        lblProdId.setPreferredSize(new java.awt.Dimension(105, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentsCalculations.add(lblProdId, gridBagConstraints);

        cboProdId.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        cboProdId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentsCalculations.add(cboProdId, gridBagConstraints);

        panOA.setMinimumSize(new java.awt.Dimension(121, 29));
        panOA.setLayout(new java.awt.GridBagLayout());

        txtCommisionCollectTo.setEditable(false);
        txtCommisionCollectTo.setAllowAll(true);
        txtCommisionCollectTo.setEnabled(false);
        txtCommisionCollectTo.setOpaque(false);
        txtCommisionCollectTo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCommisionCollectToActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panOA.add(txtCommisionCollectTo, gridBagConstraints);

        btnCommisionCollectHd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnCommisionCollectHd.setToolTipText("Agent ID");
        btnCommisionCollectHd.setMinimumSize(new java.awt.Dimension(21, 21));
        btnCommisionCollectHd.setPreferredSize(new java.awt.Dimension(21, 21));
        btnCommisionCollectHd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCommisionCollectHdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panOA.add(btnCommisionCollectHd, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentsCalculations.add(panOA, gridBagConstraints);

        lblCommisionCreditedTo.setText("Commision Credited To");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentsCalculations.add(lblCommisionCreditedTo, gridBagConstraints);

        panTds.setMinimumSize(new java.awt.Dimension(121, 29));
        panTds.setLayout(new java.awt.GridBagLayout());

        txtTdsHd.setEditable(false);
        txtTdsHd.setAllowAll(true);
        txtTdsHd.setEnabled(false);
        txtTdsHd.setOpaque(false);
        txtTdsHd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTdsHdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panTds.add(txtTdsHd, gridBagConstraints);

        btnTdsHd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnTdsHd.setToolTipText("Agent ID");
        btnTdsHd.setMinimumSize(new java.awt.Dimension(21, 21));
        btnTdsHd.setPreferredSize(new java.awt.Dimension(21, 21));
        btnTdsHd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTdsHdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panTds.add(btnTdsHd, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 28;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentsCalculations.add(panTds, gridBagConstraints);

        panCommision.setMinimumSize(new java.awt.Dimension(121, 29));
        panCommision.setLayout(new java.awt.GridBagLayout());

        txtCommisionCreditedTo.setEditable(false);
        txtCommisionCreditedTo.setAllowAll(true);
        txtCommisionCreditedTo.setEnabled(false);
        txtCommisionCreditedTo.setOpaque(false);
        txtCommisionCreditedTo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCommisionCreditedToActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panCommision.add(txtCommisionCreditedTo, gridBagConstraints);

        btnCommisionCreditedHd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnCommisionCreditedHd.setToolTipText("Agent ID");
        btnCommisionCreditedHd.setMinimumSize(new java.awt.Dimension(21, 21));
        btnCommisionCreditedHd.setPreferredSize(new java.awt.Dimension(21, 21));
        btnCommisionCreditedHd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCommisionCreditedHdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCommision.add(btnCommisionCreditedHd, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentsCalculations.add(panCommision, gridBagConstraints);

        panCommFromBank.setMinimumSize(new java.awt.Dimension(121, 29));
        panCommFromBank.setLayout(new java.awt.GridBagLayout());

        lblDelayedFromAmt1.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCommFromBank.add(lblDelayedFromAmt1, gridBagConstraints);

        txtCommFromBank.setEditable(false);
        txtCommFromBank.setAllowAll(true);
        txtCommFromBank.setEnabled(false);
        txtCommFromBank.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCommFromBankActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panCommFromBank.add(txtCommFromBank, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 31;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentsCalculations.add(panCommFromBank, gridBagConstraints);

        txtToAmount.setEnabled(false);
        txtToAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtToAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtToAmountActionPerformed(evt);
            }
        });
        txtToAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtToAmountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 45;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentsCalculations.add(txtToAmount, gridBagConstraints);

        panActIntroCommHead.setMinimumSize(new java.awt.Dimension(121, 29));
        panActIntroCommHead.setLayout(new java.awt.GridBagLayout());

        txtActIntroCommisionHd.setEditable(false);
        txtActIntroCommisionHd.setAllowAll(true);
        txtActIntroCommisionHd.setEnabled(false);
        txtActIntroCommisionHd.setOpaque(false);
        txtActIntroCommisionHd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtActIntroCommisionHdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panActIntroCommHead.add(txtActIntroCommisionHd, gridBagConstraints);

        btnActIntroHd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnActIntroHd.setToolTipText("Agent ID");
        btnActIntroHd.setMinimumSize(new java.awt.Dimension(21, 21));
        btnActIntroHd.setPreferredSize(new java.awt.Dimension(21, 21));
        btnActIntroHd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActIntroHdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panActIntroCommHead.add(btnActIntroHd, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 24;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentsCalculations.add(panActIntroCommHead, gridBagConstraints);

        panTdsValue.setMinimumSize(new java.awt.Dimension(121, 29));
        panTdsValue.setLayout(new java.awt.GridBagLayout());

        lblDelayedFromAmt2.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panTdsValue.add(lblDelayedFromAmt2, gridBagConstraints);

        txtTds.setEditable(false);
        txtTds.setAllowAll(true);
        txtTds.setEnabled(false);
        txtTds.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTdsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panTdsValue.add(txtTds, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 37;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentsCalculations.add(panTdsValue, gridBagConstraints);

        panActIntroValue.setMinimumSize(new java.awt.Dimension(121, 29));
        panActIntroValue.setLayout(new java.awt.GridBagLayout());

        lblDelayedFromAmt3.setText("Rs");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panActIntroValue.add(lblDelayedFromAmt3, gridBagConstraints);

        txtActIntroCommisionAmt.setEditable(false);
        txtActIntroCommisionAmt.setAllowAll(true);
        txtActIntroCommisionAmt.setEnabled(false);
        txtActIntroCommisionAmt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtActIntroCommisionAmtActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panActIntroValue.add(txtActIntroCommisionAmt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 33;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentsCalculations.add(panActIntroValue, gridBagConstraints);

        lblTds.setText("Tds");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 37;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentsCalculations.add(lblTds, gridBagConstraints);

        panCommFromActHolder.setMinimumSize(new java.awt.Dimension(121, 29));
        panCommFromActHolder.setLayout(new java.awt.GridBagLayout());

        lblDelayedFromAmt4.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panCommFromActHolder.add(lblDelayedFromAmt4, gridBagConstraints);

        txtCommFromActHolder.setEditable(false);
        txtCommFromActHolder.setAllowAll(true);
        txtCommFromActHolder.setEnabled(false);
        txtCommFromActHolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCommFromActHolderActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panCommFromActHolder.add(txtCommFromActHolder, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 32;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentsCalculations.add(panCommFromActHolder, gridBagConstraints);

        lblCommCollectValue.setForeground(new java.awt.Color(0, 0, 204));
        lblCommCollectValue.setText("                                                 ");
        lblCommCollectValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentsCalculations.add(lblCommCollectValue, gridBagConstraints);

        lblActIntroValue.setForeground(new java.awt.Color(0, 0, 204));
        lblActIntroValue.setText("                                        ");
        lblActIntroValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 27;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentsCalculations.add(lblActIntroValue, gridBagConstraints);

        lblTdsValue.setForeground(new java.awt.Color(0, 0, 204));
        lblTdsValue.setText("                                         ");
        lblTdsValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 30;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentsCalculations.add(lblTdsValue, gridBagConstraints);

        lblCommCreditValue.setForeground(new java.awt.Color(0, 0, 204));
        lblCommCreditValue.setText("                                      ");
        lblCommCreditValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentsCalculations.add(lblCommCreditValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panFloatingRateAccount.add(panAgentsCalculations, gridBagConstraints);

        srpInterestTable.setMinimumSize(new java.awt.Dimension(500, 400));
        srpInterestTable.setPreferredSize(new java.awt.Dimension(500, 400));

        tblAgentCommisionTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Comm From Bank", "Comm From Act Holder", "Act Intro Comm", "Tds %", "From Dt", "To Dt", "From Amt", "To Amt", "Status", "Authorize Status"
            }
        ));
        tblAgentCommisionTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblAgentCommisionTableMousePressed(evt);
            }
        });
        srpInterestTable.setViewportView(tblAgentCommisionTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panFloatingRateAccount.add(srpInterestTable, gridBagConstraints);

        tabAgent.addTab("Agent's Commission Rate Maintenance", panFloatingRateAccount);

        getContentPane().add(tabAgent, java.awt.BorderLayout.LINE_END);

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

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus1.setText(observable.getLblStatus());
        popUp(VIEW);
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed

    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
        btnTabNew.setEnabled(false);
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnAuthorize.setEnabled(true);
        btnTabNew.setEnabled(false);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
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

    public void authorizeStatus(String authorizeStatus) {
        if (viewType != AUTHORIZE) {
            viewType = AUTHORIZE;
            //__ To Save the data in the Internal Frame...
            setModified(true);
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            if (tabAgent.getSelectedIndex() == 0) {
                mapParam.put(CommonConstants.MAP_NAME, "viewAgentAuthorizeCommisionData");
                whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
                whereMap.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
                whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
                mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            }
            whereMap = null;
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();

            btnSave.setEnabled(false);
            cboProdType.setEnabled(false);
            cboProdId.setEnabled(false);
            txtCommFromBank.setEnabled(false);
            txtCommFromActHolder.setEnabled(false);
            txtActIntroCommisionAmt.setEnabled(false);
            txtTds.setEnabled(false);
            tdtFromDate.setEnabled(false);
            tdtToDate.setEnabled(false);
            txtFromAmt.setEnabled(false);
            txtToAmount.setEnabled(false);
            //__ If there's no data to be Authorized, call Cancel action...  
            if (!isModified()) {
                setButtonEnableDisable();
                btnCancelActionPerformed(null);
            }

        } else if (viewType == AUTHORIZE) {
//            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            HashMap singleAuthorizeMap = new HashMap();
            if (tabAgent.getSelectedIndex() == 0) {
                try {
                    singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
                    singleAuthorizeMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
                    singleAuthorizeMap.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
                    singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDt.clone());
//                System.out.println("singleAuthorizeMap: " + singleAuthorizeMap);
//                ClientUtil.execute("authorizeAgentData", singleAuthorizeMap);
                    observable.setAuthorizeMap(singleAuthorizeMap);
                    observable.doActionPerform();
                    super.setOpenForEditBy(observable.getStatusBy());
                    viewType = -1;
                    btnSave.setEnabled(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            btnCancelActionPerformed(null);
            lblStatus1.setText(authorizeStatus);
        }
    }
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        cifClosingAlert();
//        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPrintActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        ClientUtil.clearAll(this);
        setModified(false);
        observable.resetForm();                 //__ Reset the fields in the UI to null...
//        observable.resetTable();
        observable.resetProdTable();
        ClientUtil.enableDisable(this, false);  //__ Disables the panel...
        if (!btnSave.isEnabled()) {
            btnSave.setEnabled(true);
        }
        setButtonEnableDisable();               //__ Enables or Disables the buttons and menu Items depending on their previous state...
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);//Sets the Action Type to be performed...
        observable.setStatus();                 //__ To set the Value of lblStatus..
        viewType = -1;
        //__ Make the Screen Closable..
        setModified(false);
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        lblCommCreditValue.setText("");
        lblCommCollectValue.setText("");
        lblActIntroValue.setText("");
        lblTdsValue.setText("");
        cboProdId.setSelectedItem("");
        cboProdType.setSelectedItem("");
        txtCommisionCreditedTo.setText("");
        txtCommisionCollectTo.setText("");
        txtActIntroCommisionHd.setText("");
        txtTdsHd.setText("");
        txtCommFromBank.setText("");
        txtCommFromActHolder.setText("");
        txtActIntroCommisionAmt.setText("");
        txtTds.setText("");
        txtFromAmt.setText("");
        txtToAmount.setText("");
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        if (tabAgent.getSelectedIndex() == 0) {
            updateOBFields();
            //To display an alert if the mandatory fields are not properly inputted, else proceed with normal operation
            String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panFloatingRateAccount);
            if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0) {
                displayAlert(mandatoryMessage);
                return;
            } else {
                double toAmt = CommonUtil.convertObjToDouble(tblAgentCommisionTable.getValueAt(tblAgentCommisionTable.getRowCount() - 1, 7));
                if (toAmt != 999999999) {
                    ClientUtil.showAlertWindow("Max amount not reached,Please select last record then enter to amount as max amount of 999999999.");
                    return;
                } else {
                    observable.doAction();// To perform the necessary operation depending on the Action type...
                    //__ If the Operation is Not Failed, Clear the Screen...
                    if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                        HashMap lockMap = new HashMap();
                        ArrayList lst = new ArrayList();
                        lst.add("AGENT ID");
                        lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
                        if (observable.getProxyReturnMap() != null) {
                            if (observable.getProxyReturnMap().containsKey("AGENT ID")) {
                                lockMap.put("AGENT ID", observable.getProxyReturnMap().get("AGENT ID"));
                            }
                        }
                        setEditLockMap(lockMap);
                        setEditLock();
                        btnCancelActionPerformed(null);
                    }
                }
            }
        }
        //__ Make the Screen Closable..
        setModified(false);
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
    }//GEN-LAST:event_btnSaveActionPerformed
    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE); //__ Sets the Action Type to be performed...
        popUp(DELETE);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT); //__ Sets the Action Type to be performed...
        if (tabAgent.getSelectedIndex() == 0) {
            popUp(EDIT);
            btnReject.setEnabled(false);
            btnAuthorize.setEnabled(false);
            btnException.setEnabled(false);
            btnSave.setEnabled(true);
            btnCancel.setEnabled(true);
            enableDisableAction();
        }
    }//GEN-LAST:event_btnEditActionPerformed
    private void popUp(int field) {
        final HashMap viewMap = new HashMap();
        viewType = field;
        HashMap whereMap = new HashMap();
        whereMap.put("BRANCHID", getSelectedBranchID());
        if (field == EDIT || field == DELETE || field == AUTHORIZE || field == VIEW) { //Edit=0 and Delete=1
            ArrayList lst = new ArrayList();
            lst.add("AGENT ID");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            viewMap.put(CommonConstants.MAP_NAME, "viewAgentCommisionData");
            new ViewAll(this, viewMap, false).show();
        } else if (field == COMM_CREDIT_ACHD || field == COMM_COLLECT_ACHD || field == ACT_INTRO_ACHD
                || field == TDS_ACHD) {
            whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.selBranch);
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            viewMap.put(CommonConstants.MAP_NAME, "Cash.getSelectAcctHead");
            new ViewAll(this, viewMap).show();
        }
    }

    // this method is called automatically from ViewAll...
    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
        HashMap OAprodDescMap = new HashMap();
        HashMap depProdDescMap = new HashMap();

        System.out.println("hash:  " + hash);

        if (viewType == EDIT || viewType == DELETE || viewType == AUTHORIZE || viewType == VIEW) {
            if (tabAgent.getSelectedIndex() == 0) {
                hash.put("AGENTID", hash.get("AGENT ID"));
                hash.put("BRANCHID", getSelectedBranchID());
                observable.populateData(hash);
                tblAgentCommisionTable.setModel(observable.getTblAgentCommision());
            }
            //__ If the Action type is Delete...
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || viewType == AUTHORIZE || viewType == VIEW) {
                ClientUtil.enableDisable(this, false);     //__ Disables the panel...

                //__ If the Action Type is Edit...
            } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                ClientUtil.enableDisable(this, true);     //__ Anable the panel...

                //__ Get the Auth Status...
                final String AUTHSTATUS = "";//observable.getAuthStatus();
                if (AUTHSTATUS.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)) {
                    //__ Make Remarks as Editable an rest all UnEditable...
                }
            }
            if (viewType == AUTHORIZE) {
                btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
            }
            observable.setStatus();             //__ To set the Value of lblStatus...
            setButtonEnableDisable();           //__ Enables or Disables the buttons and menu Items depending on their previous state...

        } else if (viewType == COMM_CREDIT_ACHD) {
            txtCommisionCreditedTo.setText(hash.get("A/C HEAD").toString());
            observable.setTxtCommisionCreditedTo(hash.get("A/C HEAD").toString());
            lblCommCreditValue.setText(hash.get("A/C HEAD DESCRIPTION").toString());
        } else if (viewType == COMM_COLLECT_ACHD) {
            txtCommisionCollectTo.setText(hash.get("A/C HEAD").toString());
            observable.setTxtCommisionCollectTo(hash.get("A/C HEAD").toString());
            lblCommCollectValue.setText(hash.get("A/C HEAD DESCRIPTION").toString());
        } else if (viewType == ACT_INTRO_ACHD) {
            txtActIntroCommisionHd.setText(hash.get("A/C HEAD").toString());
            observable.setTxtActIntroCommisionHd(hash.get("A/C HEAD").toString());
            lblActIntroValue.setText(hash.get("A/C HEAD DESCRIPTION").toString());
        } else if (viewType == TDS_ACHD) {
            txtTdsHd.setText(hash.get("A/C HEAD").toString());
            observable.setTxtTdsHd(hash.get("A/C HEAD").toString());
            lblTdsValue.setText(hash.get("A/C HEAD DESCRIPTION").toString());
        }
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }

    private void enableDisableAction() {
        btnTabNew.setEnabled(true);
        btnTabSave.setEnabled(false);
        btnTabDelete.setEnabled(false);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        cboProdType.setEnabled(false);
        cboProdId.setEnabled(false);
        tdtFromDate.setEnabled(false);
        tdtToDate.setEnabled(false);
        txtFromAmt.setEnabled(false);
        txtToAmount.setEnabled(false);
        btnCommisionCreditedHd.setEnabled(false);
        btnCommisionCollectHd.setEnabled(false);
        btnActIntroHd.setEnabled(false);
        btnTdsHd.setEnabled(false);
        txtCommFromBank.setEnabled(false);
        txtCommFromActHolder.setEnabled(false);
        txtActIntroCommisionAmt.setEnabled(false);
        txtTds.setEnabled(false);
    }

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        if (tabAgent.getSelectedIndex() == 0) {
            observable.resetForm();               // to Reset all the Fields and Status in UI...
            observable.resetTable();
            setButtonEnableDisable();             // Enables/Disables the necessary buttons and menu items...
            ClientUtil.enableDisable(this, true); // Enables the panel...
            observable.setActionType(ClientConstants.ACTIONTYPE_NEW);   //Sets the Action Type to be performed...
            observable.setStatus();
            enableDisableAction();
        }
        setModified(true);
    }//GEN-LAST:event_btnNewActionPerformed

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
    public void populateAgentCommisionDetails() {
        txtCommisionCollectTo.setText(observable.getTxtCommisionCollectTo());
        txtCommisionCreditedTo.setText(observable.getTxtCommisionCreditedTo());
        txtActIntroCommisionHd.setText(observable.getTxtActIntroCommisionHd());
        txtTdsHd.setText(observable.getTxtTdsHd());
        txtCommFromBank.setText(observable.getTxtCommFromBank());
        txtCommFromActHolder.setText(observable.getTxtCommFromActHolder());
        txtActIntroCommisionAmt.setText(observable.getTxtActIntroCommisionAmt());
        txtTds.setText(observable.getTxtTds());
        tdtFromDate.setDateValue(observable.getTdtFromDate());
        tdtToDate.setDateValue(observable.getTdtToDate());
        cboProdType.setSelectedItem(((ComboBoxModel) cboProdType.getModel()).getDataForKey(observable.getCboProductType()));
        cboProdId.setSelectedItem(((ComboBoxModel) cboProdId.getModel()).getDataForKey(observable.getCboProdId()));
        txtFromAmt.setText(CommonUtil.convertObjToStr(observable.getTxtFromAmount()));
        txtToAmount.setText(observable.getTxtToAmount());
        lblCommCreditValue.setText(observable.getLblCommCollectValue());
        lblCommCollectValue.setText(observable.getLblCommCreditValue());
        lblTdsValue.setText(observable.getLblTdsValue());
        lblActIntroValue.setText(observable.getLblActIntroValue());
    }

    private void resetCommisionDetails() {
        txtCommFromBank.setText("");
        txtCommFromActHolder.setText("");
        txtActIntroCommisionAmt.setText("");
        txtTds.setText("");
        tdtToDate.setDateValue("");
        tdtFromDate.setDateValue("");
        tdtToDate.setDateValue("");
        txtFromAmt.setText("");
        txtToAmount.setText("");
    }

    private void btnTabNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTabNewActionPerformed
        updateOBFields();
        observable.ttNotifyObservers();
        ClientUtil.enableDisable(panAgentsCalculations, true);
        if (updateAgentCommisionMode) {
            observable.setNewData(false);
        } else {
            observable.setNewData(true);
        }
        updateTab = -1;
        btnTabNew.setEnabled(false);
        btnTabSave.setEnabled(true);
        btnTabDelete.setEnabled(false);
        updateAgentCommisionMode = false;
        if (tblAgentCommisionTable.getRowCount() > 0) {
            String fromDt = CommonUtil.convertObjToStr(tblAgentCommisionTable.getValueAt(tblAgentCommisionTable.getRowCount() - 1, 4));
            String authorizeStatus = CommonUtil.convertObjToStr(tblAgentCommisionTable.getValueAt(tblAgentCommisionTable.getRowCount() - 1, 9));
            double toAmt = CommonUtil.convertObjToDouble(tblAgentCommisionTable.getValueAt(tblAgentCommisionTable.getRowCount() - 1, 7));
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT && txtCommisionCreditedTo.getText().length() == 0) {
                observable.populateAgentCommisionDetailsPartial(CommonUtil.convertObjToStr(tblAgentCommisionTable.getRowCount() - 1));
                populateAgentCommisionDetails();
            }
            if (authorizeStatus != null && authorizeStatus.length() > 0 && toAmt == 999999999) {
                tdtFromDate.setEnabled(true);
                txtFromAmt.setEnabled(true);
                txtCommFromBank.setText("");
                txtCommFromActHolder.setText("");
                txtActIntroCommisionAmt.setText("");
                txtTds.setText("");
                tdtFromDate.setDateValue("");
                txtFromAmt.setText("");
                txtToAmount.setText("");
            } else {
                tdtFromDate.setDateValue(fromDt);
                tdtFromDate.setEnabled(false);
                tdtToDate.setEnabled(false);
                txtFromAmt.setText(CommonUtil.convertObjToStr(toAmt + 1));
                txtFromAmt.setEnabled(false);
                cboProdType.setEnabled(false);
                cboProdId.setEnabled(false);
                btnCommisionCreditedHd.setEnabled(false);
                btnCommisionCollectHd.setEnabled(false);
                btnActIntroHd.setEnabled(false);
                btnTdsHd.setEnabled(false);
                txtCommFromBank.setEnabled(true);
                txtCommFromActHolder.setEnabled(true);
                txtActIntroCommisionAmt.setEnabled(true);
                txtTds.setEnabled(true);
                txtFromAmt.setEnabled(false);
                txtToAmount.setEnabled(true);
                tdtFromDate.setEnabled(false);
                tdtToDate.setEnabled(true);
            }
        } else {
            cboProdType.setEnabled(true);
            cboProdId.setEnabled(true);
            btnCommisionCreditedHd.setEnabled(true);
            btnCommisionCollectHd.setEnabled(true);
            btnActIntroHd.setEnabled(true);
            btnTdsHd.setEnabled(true);
            txtFromAmt.setEnabled(true);
            txtCommFromBank.setEnabled(true);
            txtCommFromActHolder.setEnabled(true);
            txtActIntroCommisionAmt.setEnabled(true);
            txtTds.setEnabled(true);
            tdtFromDate.setEnabled(true);
            txtFromAmt.setEnabled(true);
            txtToAmount.setEnabled(true);
        }
    }//GEN-LAST:event_btnTabNewActionPerformed

    private void btnTabSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTabSaveActionPerformed
        int result = 0;
        updateOBFields();
        try {
            String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panAgentsCalculations);
            StringBuffer strBAlert = new StringBuffer();
            int count = tblAgentCommisionTable.getRowCount();
            double toAmt = CommonUtil.convertObjToDouble(txtToAmount.getText()).doubleValue();
            if (cboProdType.getSelectedItem().equals("")) {
                ClientUtil.showAlertWindow("Please select product type");
                return;
            } else if (cboProdId.getSelectedItem().equals("")) {
                ClientUtil.showAlertWindow("Please select product id");
                return;
            } else if (txtCommisionCreditedTo.getText().length() == 0) {
                ClientUtil.showAlertWindow("Please select commision credit head");
                return;
            } else if (txtCommisionCollectTo.getText().length() == 0) {
                ClientUtil.showAlertWindow("Please select commision collect head");
                return;
            } else if (txtActIntroCommisionHd.getText().length() == 0) {
                ClientUtil.showAlertWindow("Please select account intro commision head");
                return;
            } else if (txtTdsHd.getText().length() == 0) {
                ClientUtil.showAlertWindow("Please select tds account head");
                return;
            } else if (txtCommFromBank.getText().length() == 0) {
                ClientUtil.showAlertWindow("Please enter commision from bank");
                return;
            } else if (txtCommFromActHolder.getText().length() == 0) {
                ClientUtil.showAlertWindow("Please enter commison from account holder");
                return;
            } else if (txtActIntroCommisionAmt.getText().length() == 0) {
                ClientUtil.showAlertWindow("Please enter account intro commision amount");
                return;
            } else if (txtTds.getText().length() == 0) {
                ClientUtil.showAlertWindow("Please enter tds percentage");
                return;
            } else if (txtFromAmt.getText().length() == 0) {
                ClientUtil.showAlertWindow("Please enter from amount");
                return;
            } else if (txtToAmount.getText().length() == 0) {
                ClientUtil.showAlertWindow("Please enter to amount");
                return;
            } else {
                updateAgentCommisionOBFields();
                observable.addToAgentCommisionDetailsTable(updateTab, updateAgentCommisionMode);
                tblAgentCommisionTable.setModel(observable.getTblAgentCommision());
                observable.resetAgentCommisionDetails();
                double toAmount = CommonUtil.convertObjToDouble(txtToAmount.getText()).doubleValue();
                resetCommisionDetails();
//                enableDisableButton(false);
                btnTabNew.setEnabled(true);
                btnTabSave.setEnabled(false);
                btnTabDelete.setEnabled(false);
                updateAgentCommisionMode = false;
                txtCommFromBank.setEnabled(false);
                txtCommFromActHolder.setEnabled(false);
                txtActIntroCommisionAmt.setEnabled(false);
                txtTds.setEnabled(false);
                tdtFromDate.setEnabled(false);
                tdtToDate.setEnabled(false);
                txtFromAmt.setEnabled(false);
                txtToAmount.setEnabled(false);
//                txtFromAmt.setText(CommonUtil.convertObjToStr(toAmount+1));
            }
        } catch (Exception e) {
//            System.out.println("Error in btnTabSaveActionPerformed");
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnTabSaveActionPerformed

    private void updateAgentCommisionOBFields() {
        observable.setTxtCommisionCollectTo(txtCommisionCollectTo.getText());
        observable.setTxtCommisionCreditedTo(txtCommisionCreditedTo.getText());
        observable.setTxtActIntroCommisionHd(txtActIntroCommisionHd.getText());
        observable.setTxtTdsHd(txtTdsHd.getText());
        observable.setTxtCommFromBank(txtCommFromBank.getText());
        observable.setTxtCommFromActHolder(txtCommFromActHolder.getText());
        observable.setTxtActIntroCommisionAmt(txtActIntroCommisionAmt.getText());
        observable.setTxtTds(txtTds.getText());
        if (tdtFromDate.getDateValue() != null) {
            observable.setTdtFromDate(tdtFromDate.getDateValue());
        } else {
            observable.setTdtFromDate("");
        }
        if (tdtToDate.getDateValue() != null) {
            observable.setTdtToDate(tdtToDate.getDateValue());
        } else {
            observable.setTdtToDate("");
        }
        observable.setCboProductType(((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString());
        observable.setCboProdId(((ComboBoxModel) cboProdId.getModel()).getKeyForSelected().toString());
        observable.setTxtFromAmount(CommonUtil.convertObjToStr(txtFromAmt.getText()));
        observable.setTxtToAmount(txtToAmount.getText());
    }
    private void btnTabDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTabDeleteActionPerformed
        // TODO add your handling code here:
        String status = CommonUtil.convertObjToStr(tblAgentCommisionTable.getValueAt(tblAgentCommisionTable.getSelectedRow(), 9));
        if (status != null && status.length() > 0 && status.equals("AUTHORIZED")) {
            ClientUtil.showAlertWindow("Selected records already authorized");
            return;
        } else {
            observable.deleteAgentCommisionTableData(tblAgentCommisionTable.getSelectedRow());
            observable.resetAgentCommisionDetails();
            resetCommisionDetails();
            btnTabSave.setEnabled(false);
            btnTabNew.setEnabled(true);
            btnTabDelete.setEnabled(false);
            txtCommFromBank.setEnabled(false);
            txtCommFromActHolder.setEnabled(false);
            txtActIntroCommisionAmt.setEnabled(false);
            txtTds.setEnabled(false);
            tdtFromDate.setEnabled(false);
            tdtToDate.setEnabled(false);
            tdtFromDate.setEnabled(false);
            tdtToDate.setEnabled(false);
            txtFromAmt.setEnabled(false);
            txtToAmount.setEnabled(false);
        }
    }//GEN-LAST:event_btnTabDeleteActionPerformed

    private void txtFromAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFromAmtFocusLost
        // TODO add your handling code here:        
    }//GEN-LAST:event_txtFromAmtFocusLost

    private void txtCommisionCollectToActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCommisionCollectToActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCommisionCollectToActionPerformed

    private void btnCommisionCollectHdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCommisionCollectHdActionPerformed
        popUp(COMM_COLLECT_ACHD);        // TODO add your handling code here:         
    }//GEN-LAST:event_btnCommisionCollectHdActionPerformed

    private void txtTdsHdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTdsHdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTdsHdActionPerformed

    private void btnTdsHdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTdsHdActionPerformed
        // TODO add your handling code here:
        popUp(TDS_ACHD);
    }//GEN-LAST:event_btnTdsHdActionPerformed

    private void txtCommisionCreditedToActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCommisionCreditedToActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCommisionCreditedToActionPerformed

    private void btnCommisionCreditedHdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCommisionCreditedHdActionPerformed
        popUp(COMM_CREDIT_ACHD);
    }//GEN-LAST:event_btnCommisionCreditedHdActionPerformed

    private void txtCommFromBankActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCommFromBankActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCommFromBankActionPerformed

    private void txtActIntroCommisionHdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtActIntroCommisionHdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtActIntroCommisionHdActionPerformed

    private void btnActIntroHdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActIntroHdActionPerformed
        // TODO add your handling code here:
        popUp(ACT_INTRO_ACHD);
    }//GEN-LAST:event_btnActIntroHdActionPerformed

    private void txtTdsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTdsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTdsActionPerformed

    private void txtActIntroCommisionAmtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtActIntroCommisionAmtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtActIntroCommisionAmtActionPerformed

    private void txtCommFromActHolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCommFromActHolderActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCommFromActHolderActionPerformed

    private void cboProdTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdTypeActionPerformed
        // TODO add your handling code here:
        if (cboProdType.getSelectedIndex() > 0) {
            String prodType = ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();
            observable.setCboProductType(prodType);
            if (prodType != null && prodType.equals("GL")) {
                cboProdId.setSelectedItem("");
                cboProdId.setEnabled(false);
            } else {
                observable.setCboProductType(prodType);
                observable.setCbmProdId(prodType);
                cboProdId.setModel(observable.getCbmProdId());
                cboProdId.setEnabled(true);
            }
        }
    }//GEN-LAST:event_cboProdTypeActionPerformed

    private void txtToAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtToAmountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtToAmountActionPerformed

private void cboProdIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdIdActionPerformed
    // TODO add your handling code here:
    if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
        String prodType = ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString();
        String prodId = ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected().toString();
        HashMap prodTypeExistsorNoMap = new HashMap();
        prodTypeExistsorNoMap.put("PROD_TYPE", prodType);
        prodTypeExistsorNoMap.put("PROD_ID", prodId);
        List prodTypeExistsorNoList = ClientUtil.executeQuery("getSelectProdTypeExistorNot", prodTypeExistsorNoMap);
        if (prodTypeExistsorNoList != null && prodTypeExistsorNoList.size() > 0) {
            prodTypeExistsorNoMap = (HashMap) prodTypeExistsorNoList.get(0);
            ClientUtil.showAlertWindow("This product id is already exist, Please open in edit mode and enter new records...");
            cboProdId.setSelectedItem("");
        }
    }
}//GEN-LAST:event_cboProdIdActionPerformed

    private void txtToAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtToAmountFocusLost
        // TODO add your handling code here:
        double fromAmt = CommonUtil.convertObjToDouble(txtFromAmt.getText()).doubleValue();
        double toAmt = CommonUtil.convertObjToDouble(txtToAmount.getText()).doubleValue();
        if (fromAmt > toAmt) {
//            System.out.println("$$$$$$$$");
            ClientUtil.displayAlert("Enter to amount is greater than from amt...");
            txtToAmount.setText("");
        }
    }//GEN-LAST:event_txtToAmountFocusLost

    private void tblAgentCommisionTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAgentCommisionTableMousePressed
        // Add your handling code here:
        try {
            updateAgentCommisionMode = true;
            updateTab = tblAgentCommisionTable.getSelectedRow();
            observable.setNewData(false);
            String st = CommonUtil.convertObjToStr(tblAgentCommisionTable.getValueAt(tblAgentCommisionTable.getSelectedRow(), 0));
            observable.populateAgentCommisionDetails(CommonUtil.convertObjToStr(tblAgentCommisionTable.getSelectedRow()));
            populateAgentCommisionDetails();
            String status = CommonUtil.convertObjToStr(tblAgentCommisionTable.getValueAt(tblAgentCommisionTable.getSelectedRow(), 9));
            if (status != null && status.length() > 0 && status.equals("AUTHORIZED")) {
                enableDisableAction();
                btnTabNew.setEnabled(true);
                btnTabSave.setEnabled(false);
                btnTabDelete.setEnabled(false);
//                resetCommisionDetails();
                txtCommFromBank.setEnabled(false);
                txtCommFromActHolder.setEnabled(false);
                txtActIntroCommisionAmt.setEnabled(false);
                txtTds.setEnabled(false);
                tdtFromDate.setEnabled(false);
                tdtToDate.setEnabled(false);
                txtFromAmt.setEnabled(false);
                txtToAmount.setEnabled(false);
            } else {
                btnTabNew.setEnabled(false);
                btnTabSave.setEnabled(true);
                btnTabDelete.setEnabled(true);
                txtCommFromBank.setEnabled(true);
                txtCommFromActHolder.setEnabled(true);
                txtActIntroCommisionAmt.setEnabled(true);
                txtTds.setEnabled(true);
                tdtFromDate.setEnabled(false);
                tdtToDate.setEnabled(true);
                txtFromAmt.setEnabled(false);
                txtToAmount.setEnabled(true);
            }
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REALIZE) {
                enableDisableAction();
                btnTabNew.setEnabled(false);
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE) {
                    btnAuthorize.setEnabled(true);
                } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
                    btnReject.setEnabled(true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_tblAgentCommisionTableMousePressed

    private void initComponentData() {
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new AgentCommisionSlabRateUI().show();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnActIntroHd;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnCommisionCollectHd;
    private com.see.truetransact.uicomponent.CButton btnCommisionCreditedHd;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnTabDelete;
    private com.see.truetransact.uicomponent.CButton btnTabNew;
    private com.see.truetransact.uicomponent.CButton btnTabSave;
    private com.see.truetransact.uicomponent.CButton btnTdsHd;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboInstType;
    private com.see.truetransact.uicomponent.CComboBox cboProdId;
    private com.see.truetransact.uicomponent.CComboBox cboProdType;
    private com.see.truetransact.uicomponent.CLabel lblActIntroCommisionAmt;
    private com.see.truetransact.uicomponent.CLabel lblActIntroCommisionHd;
    private com.see.truetransact.uicomponent.CLabel lblActIntroValue;
    private com.see.truetransact.uicomponent.CLabel lblCommCollectValue;
    private com.see.truetransact.uicomponent.CLabel lblCommCreditValue;
    private com.see.truetransact.uicomponent.CLabel lblCommFromActHolder;
    private com.see.truetransact.uicomponent.CLabel lblCommFromBank;
    private com.see.truetransact.uicomponent.CLabel lblCommisionCollectHd;
    private com.see.truetransact.uicomponent.CLabel lblCommisionCreditedTo;
    private com.see.truetransact.uicomponent.CLabel lblDelayedFromAmt1;
    private com.see.truetransact.uicomponent.CLabel lblDelayedFromAmt2;
    private com.see.truetransact.uicomponent.CLabel lblDelayedFromAmt3;
    private com.see.truetransact.uicomponent.CLabel lblDelayedFromAmt4;
    private com.see.truetransact.uicomponent.CLabel lblFromAmount;
    private com.see.truetransact.uicomponent.CLabel lblFromDate;
    private com.see.truetransact.uicomponent.CLabel lblInstType;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblProdId;
    private com.see.truetransact.uicomponent.CLabel lblProdType;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace70;
    private com.see.truetransact.uicomponent.CLabel lblSpace71;
    private com.see.truetransact.uicomponent.CLabel lblSpace72;
    private com.see.truetransact.uicomponent.CLabel lblSpace73;
    private com.see.truetransact.uicomponent.CLabel lblSpace74;
    private com.see.truetransact.uicomponent.CLabel lblSpace75;
    private com.see.truetransact.uicomponent.CLabel lblStatus1;
    private com.see.truetransact.uicomponent.CLabel lblTds;
    private com.see.truetransact.uicomponent.CLabel lblTdsHd;
    private com.see.truetransact.uicomponent.CLabel lblTdsValue;
    private com.see.truetransact.uicomponent.CLabel lblToAmount;
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
    private com.see.truetransact.uicomponent.CPanel panActIntroCommHead;
    private com.see.truetransact.uicomponent.CPanel panActIntroValue;
    private com.see.truetransact.uicomponent.CPanel panAgentsCalculations;
    private com.see.truetransact.uicomponent.CPanel panButtons;
    private com.see.truetransact.uicomponent.CPanel panCommFromActHolder;
    private com.see.truetransact.uicomponent.CPanel panCommFromBank;
    private com.see.truetransact.uicomponent.CPanel panCommision;
    private com.see.truetransact.uicomponent.CPanel panFloatingRateAccount;
    private com.see.truetransact.uicomponent.CPanel panOA;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTds;
    private com.see.truetransact.uicomponent.CPanel panTdsValue;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptPrint;
    private javax.swing.JSeparator sptProcess;
    private com.see.truetransact.uicomponent.CScrollPane srpInterestTable;
    private com.see.truetransact.uicomponent.CTabbedPane tabAgent;
    private com.see.truetransact.uicomponent.CTable tblAgentCommisionTable;
    private javax.swing.JToolBar tbrLoantProduct;
    private com.see.truetransact.uicomponent.CDateField tdtFromDate;
    private com.see.truetransact.uicomponent.CDateField tdtToDate;
    private com.see.truetransact.uicomponent.CTextField txtActIntroCommisionAmt;
    private com.see.truetransact.uicomponent.CTextField txtActIntroCommisionHd;
    private com.see.truetransact.uicomponent.CTextField txtCommFromActHolder;
    private com.see.truetransact.uicomponent.CTextField txtCommFromBank;
    private com.see.truetransact.uicomponent.CTextField txtCommisionCollectTo;
    private com.see.truetransact.uicomponent.CTextField txtCommisionCreditedTo;
    private com.see.truetransact.uicomponent.CTextField txtFromAmt;
    private com.see.truetransact.uicomponent.CTextField txtTds;
    private com.see.truetransact.uicomponent.CTextField txtTdsHd;
    private com.see.truetransact.uicomponent.CTextField txtToAmount;
    // End of variables declaration//GEN-END:variables
}
