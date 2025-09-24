/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * PFMasterUI.java
 *
 * 
 */
package com.see.truetransact.ui.payroll.pfMaster;

import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uivalidation.PercentageValidation;
import java.util.HashMap;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author anjuanand
 */
public class PFMasterUI extends CInternalFrame implements java.util.Observer, UIMandatoryField {

    HashMap mandatoryMap;
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.payroll.pfMaster.PFMasterRB", ProxyParameters.LANGUAGE);
    final int EDIT = 0, DELETE = 1, EMPLOYEE = 2;
    int viewType = -1;
    boolean isFilled = false;
    PFMasterOB observable;
    private EmpDetailsUI empDetailsUI = null;
    private String view = "";

    /**
     * Creates new form PFMasterUI
     */
    public PFMasterUI() {
        initComponents();
        initSetup();
    }

    private void initComponentData() {
        txtPfAccountNo.setAllowAll(true);
        txtOpeningBalance.setAllowNumber(true);
        txtPfRateOfInterest.setAllowNumber(true);
        txtEmployerContribution.setAllowNumber(true);
    }

    private void setValidation() {
        txtOpeningBalance.setValidation(new CurrencyValidation(14, 2));
        txtPfRateOfInterest.setValidation(new PercentageValidation());
        txtEmployerContribution.setValidation(new CurrencyValidation(14, 2));
    }

    private void initSetup() {
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setHelpMessage();
        setObservable();
        initComponentData();
        setValidation();
        ClientUtil.enableDisable(this, false); //__ Disables all when the screen appears for the 1st time
        setButtonEnableDisable();              //__ Enables/Disables the necessary buttons and menu items...
        setEnableDisableButtons(false);
        observable.resetForm();                //__ Resets the Data in the Form...
        observable.resetStatus();              //__ to reset the status...
        empDetailsUI = new EmpDetailsUI(panEmpDetails);
        empDetailsUI.updateEmployeeInfo(null);
    }

    private void setObservable() {
        observable = new PFMasterOB();
        observable.addObserver(this);
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
        lblMsg.setName("lblMsg");
        lblSpace.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblStatus.setName("lblStatus1");
        mbrPfMaster.setName("mbrPfMaster");
        panPfMaster.setName("panPfMaster");
        panStatus.setName("panStatus");
        btnEmployeeId.setName("btnEmployeeId");
        lblEmployeeId.setName("lblEmployeeId");
        lblPfAccountNo.setName("lblPfAccountNo");
        lblPfDate.setName("lblPfDate");
        lblPfOpeningDate.setName("lblPfOpeningDate");
        lblOpeningBalance.setName("lblOpeningBalance");
        lblPfRateOfInterest.setName("lblPfRateOfInterest");
        lblLastInterestDate.setName("lblLastInterestDate");
        lblPfNomineeName.setName("lblPfNomineeName");
        lblPfNomineeRelation.setName("lblPfNomineeRelation");
        lblEmployerContribution.setName("lblEmployerContribution");
        txtEmployeeId.setName("txtEmployeeId");
        txtPfAccountNo.setName("txtPfAccountNo");
        tdtPfDate.setName("tdtPfDate");
        tdtPfOpeningDate.setName("tdtPfOpeningDate");
        txtOpeningBalance.setName("txtOpeningBalance");
        txtPfRateOfInterest.setName("txtPfRateOfInterest");
        tdtLastInterestDate.setName("tdtLastInterestDate");
        txtPfNomineeName.setName("txtPfNomineeName");
        txtPfNomineeRelation.setName("txtPfNomineeRelation");
        txtEmployerContribution.setName("txtEmployerContribution");
    }

    /*
     * Auto Generated Method - internationalize() This method used to assign
     * display texts from the Resource Bundle File.
     */
    private void internationalize() {
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblSpace.setText(resourceBundle.getString("lblSpace"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnEmployeeId.setText(resourceBundle.getString("btnEmployeeId"));
        lblEmployeeId.setText(resourceBundle.getString("lblEmployeeId"));
        lblPfAccountNo.setText(resourceBundle.getString("lblPfAccountNo"));
        lblPfDate.setText(resourceBundle.getString("lblPfDate"));
        lblPfOpeningDate.setText(resourceBundle.getString("lblPfOpeningDate"));
        lblOpeningBalance.setText(resourceBundle.getString("lblOpeningBalance"));
        lblPfRateOfInterest.setText(resourceBundle.getString("lblPfRateOfInterest"));
        lblLastInterestDate.setText(resourceBundle.getString("lblLastInterestDate"));
        lblPfNomineeName.setText(resourceBundle.getString("lblPfNomineeName"));
        lblPfNomineeRelation.setText(resourceBundle.getString("lblPfNomineeRelation"));
        lblEmployerContribution.setText(resourceBundle.getString("lblEmployerContribution"));
    }

    /*
     * Auto Generated Method - update() This method called by Observable. It
     * updates the UI with Observable's data. If needed add/Remove RadioButtons
     * method need to be added.
     */
    public void update(Observable observed, Object arg) {
        txtEmployerContribution.setText(observable.getTxtEmployerContribution());
        tdtLastInterestDate.setDateValue(CommonUtil.convertObjToStr(observable.getTdtLastInterestDate()));
        txtOpeningBalance.setText(observable.getTxtOpeningBalance());
        txtPfAccountNo.setText(observable.getTxtPfAccountNo());
        txtPfNomineeName.setText(observable.getTxtPfNomineeName());
        txtPfNomineeRelation.setText(observable.getTxtPfNomineeRelation());
        txtPfRateOfInterest.setText(observable.getTxtPfRateOfInterest());
        tdtPfDate.setDateValue(CommonUtil.convertObjToStr(observable.getTdtPfDate()));
        tdtPfOpeningDate.setDateValue(CommonUtil.convertObjToStr(observable.getTdtPfOpeningDate()));
    }

    /*
     * Auto Generated Method - updateOBFields() This method called by Save
     * option of UI. It updates the OB with UI data.
     */
    public void updateOBFields() {
        observable.setTxtEmployeeId(txtEmployeeId.getText());
        observable.setTxtPfAccountNo(txtPfAccountNo.getText());
        observable.setTdtPfDate(DateUtil.getDateMMDDYYYY(tdtPfDate.getDateValue()));
        observable.setTdtPfOpeningDate(DateUtil.getDateMMDDYYYY(tdtPfOpeningDate.getDateValue()));
        observable.setTxtOpeningBalance(txtOpeningBalance.getText());
        observable.setTxtPfRateOfInterest(txtPfRateOfInterest.getText());
        observable.setTdtLastInterestDate(DateUtil.getDateMMDDYYYY(tdtLastInterestDate.getDateValue()));
        observable.setTxtPfNomineeName(txtPfNomineeName.getText());
        observable.setTxtPfNomineeRelation(txtPfNomineeRelation.getText());
        observable.setTxtEmployerContribution(txtEmployerContribution.getText());
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
        mandatoryMap.put("txtPfNo", new Boolean(true));
        mandatoryMap.put("txtBalance", new Boolean(true));
        mandatoryMap.put("txtAmount", new Boolean(true));
        mandatoryMap.put("rdoReceipt", new Boolean(true));
        mandatoryMap.put("rdoPayment", new Boolean(true));
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
        PFMasterMRB objMandatoryRB = new PFMasterMRB();
        txtEmployeeId.setHelpMessage(lblMsg, objMandatoryRB.getString("txtEmployeeId"));
        txtPfAccountNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPfAccountNo"));
        tdtPfDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtPfDate"));
        tdtPfOpeningDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtPfOpeningDate"));
        txtOpeningBalance.setHelpMessage(lblMsg, objMandatoryRB.getString("txtOpeningBalance"));
        txtPfRateOfInterest.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPfRateOfInterest"));
        tdtLastInterestDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtLastInterestDate"));
        txtPfNomineeName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPfNomineeName"));
        txtPfNomineeRelation.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPfNomineeRelation"));
        txtEmployerContribution.setHelpMessage(lblMsg, objMandatoryRB.getString("txtEmployerContribution"));
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
        lblSpace = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        tbrPfMaster = new javax.swing.JToolBar();
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
        panPfClose = new com.see.truetransact.uicomponent.CPanel();
        panPfMaster = new com.see.truetransact.uicomponent.CPanel();
        lblPfDate = new com.see.truetransact.uicomponent.CLabel();
        lblPfAccountNo = new com.see.truetransact.uicomponent.CLabel();
        txtPfAccountNo = new com.see.truetransact.uicomponent.CTextField();
        lblBatchIdValue = new com.see.truetransact.uicomponent.CLabel();
        lblOpeningBalance = new com.see.truetransact.uicomponent.CLabel();
        tdtLastInterestDate = new com.see.truetransact.uicomponent.CDateField();
        lblPfOpeningDate = new com.see.truetransact.uicomponent.CLabel();
        tdtPfDate = new com.see.truetransact.uicomponent.CDateField();
        txtOpeningBalance = new com.see.truetransact.uicomponent.CTextField();
        lblPfRateOfInterest = new com.see.truetransact.uicomponent.CLabel();
        lblLastInterestDate = new com.see.truetransact.uicomponent.CLabel();
        lblPfNomineeName = new com.see.truetransact.uicomponent.CLabel();
        lblPfNomineeRelation = new com.see.truetransact.uicomponent.CLabel();
        lblEmployerContribution = new com.see.truetransact.uicomponent.CLabel();
        txtPfRateOfInterest = new com.see.truetransact.uicomponent.CTextField();
        txtPfNomineeName = new com.see.truetransact.uicomponent.CTextField();
        txtPfNomineeRelation = new com.see.truetransact.uicomponent.CTextField();
        txtEmployerContribution = new com.see.truetransact.uicomponent.CTextField();
        tdtPfOpeningDate = new com.see.truetransact.uicomponent.CDateField();
        lblPfRate = new javax.swing.JLabel();
        panShareCloseTrans = new com.see.truetransact.uicomponent.CPanel();
        cPanel1 = new com.see.truetransact.uicomponent.CPanel();
        lblEmployeeId = new com.see.truetransact.uicomponent.CLabel();
        panEmpDetails = new com.see.truetransact.uicomponent.CPanel();
        panEmployeeId = new com.see.truetransact.uicomponent.CPanel();
        txtEmployeeId = new com.see.truetransact.uicomponent.CTextField();
        btnEmployeeId = new com.see.truetransact.uicomponent.CButton();
        mbrPfMaster = new com.see.truetransact.uicomponent.CMenuBar();
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
        setMaximumSize(new java.awt.Dimension(825, 575));
        setMinimumSize(new java.awt.Dimension(825, 575));
        setPreferredSize(new java.awt.Dimension(825, 575));
        getContentPane().setLayout(new java.awt.BorderLayout());

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace, gridBagConstraints);

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

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrPfMaster.add(btnNew);

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrPfMaster.add(lblSpace29);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrPfMaster.add(btnEdit);

        lblSpace30.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace30.setText("     ");
        lblSpace30.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrPfMaster.add(lblSpace30);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrPfMaster.add(btnDelete);

        lblSpace2.setText("     ");
        tbrPfMaster.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrPfMaster.add(btnSave);

        lblSpace31.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace31.setText("     ");
        lblSpace31.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrPfMaster.add(lblSpace31);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrPfMaster.add(btnCancel);

        lblSpace3.setText("     ");
        tbrPfMaster.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrPfMaster.add(btnAuthorize);

        lblSpace32.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace32.setText("     ");
        lblSpace32.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace32.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace32.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrPfMaster.add(lblSpace32);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        tbrPfMaster.add(btnException);

        lblSpace33.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace33.setText("     ");
        lblSpace33.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace33.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace33.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrPfMaster.add(lblSpace33);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrPfMaster.add(btnReject);

        lblSpace4.setText("     ");
        tbrPfMaster.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrPfMaster.add(btnPrint);

        lblSpace34.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace34.setText("     ");
        lblSpace34.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrPfMaster.add(lblSpace34);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrPfMaster.add(btnClose);

        getContentPane().add(tbrPfMaster, java.awt.BorderLayout.NORTH);

        panPfClose.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panPfClose.setMaximumSize(new java.awt.Dimension(815, 350));
        panPfClose.setMinimumSize(new java.awt.Dimension(815, 350));
        panPfClose.setPreferredSize(new java.awt.Dimension(1000, 1000));
        panPfClose.setLayout(new java.awt.GridBagLayout());

        panPfMaster.setBorder(javax.swing.BorderFactory.createTitledBorder("PFMasterDetails"));
        panPfMaster.setMaximumSize(new java.awt.Dimension(320, 207));
        panPfMaster.setMinimumSize(new java.awt.Dimension(300, 400));
        panPfMaster.setPreferredSize(new java.awt.Dimension(300, 500));
        panPfMaster.setLayout(new java.awt.GridBagLayout());

        lblPfDate.setText("PF Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        panPfMaster.add(lblPfDate, gridBagConstraints);

        lblPfAccountNo.setText("PF Account No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        panPfMaster.add(lblPfAccountNo, gridBagConstraints);

        txtPfAccountNo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtPfAccountNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPfAccountNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPfAccountNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panPfMaster.add(txtPfAccountNo, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPfMaster.add(lblBatchIdValue, gridBagConstraints);

        lblOpeningBalance.setText("Opening Balance");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        panPfMaster.add(lblOpeningBalance, gridBagConstraints);

        tdtLastInterestDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtLastInterestDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panPfMaster.add(tdtLastInterestDate, gridBagConstraints);

        lblPfOpeningDate.setText("PF Opening Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        panPfMaster.add(lblPfOpeningDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panPfMaster.add(tdtPfDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panPfMaster.add(txtOpeningBalance, gridBagConstraints);

        lblPfRateOfInterest.setText("PF Rate of Interest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        panPfMaster.add(lblPfRateOfInterest, gridBagConstraints);

        lblLastInterestDate.setText("Last Interest Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        panPfMaster.add(lblLastInterestDate, gridBagConstraints);

        lblPfNomineeName.setText("PF Nominee Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        panPfMaster.add(lblPfNomineeName, gridBagConstraints);

        lblPfNomineeRelation.setText("PF Nominee Relation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        panPfMaster.add(lblPfNomineeRelation, gridBagConstraints);

        lblEmployerContribution.setText("Employer Contribution");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        panPfMaster.add(lblEmployerContribution, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panPfMaster.add(txtPfRateOfInterest, gridBagConstraints);

        txtPfNomineeName.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panPfMaster.add(txtPfNomineeName, gridBagConstraints);

        txtPfNomineeRelation.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panPfMaster.add(txtPfNomineeRelation, gridBagConstraints);

        txtEmployerContribution.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panPfMaster.add(txtEmployerContribution, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panPfMaster.add(tdtPfOpeningDate, gridBagConstraints);

        lblPfRate.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 0);
        panPfMaster.add(lblPfRate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 96;
        gridBagConstraints.insets = new java.awt.Insets(25, 20, 0, 0);
        panPfClose.add(panPfMaster, gridBagConstraints);

        panShareCloseTrans.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panPfClose.add(panShareCloseTrans, gridBagConstraints);

        cPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("EmployeeDetails"));
        cPanel1.setMinimumSize(new java.awt.Dimension(320, 380));
        cPanel1.setLayout(new java.awt.GridBagLayout());

        lblEmployeeId.setText("Employee ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 16, 0, 0);
        cPanel1.add(lblEmployeeId, gridBagConstraints);

        panEmpDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("EmployeeData"));
        panEmpDetails.setMinimumSize(new java.awt.Dimension(230, 265));
        panEmpDetails.setPreferredSize(new java.awt.Dimension(230, 265));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(20, 0, 0, 9);
        cPanel1.add(panEmpDetails, gridBagConstraints);

        panEmployeeId.setLayout(new java.awt.GridBagLayout());

        txtEmployeeId.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtEmployeeId.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panEmployeeId.add(txtEmployeeId, gridBagConstraints);

        btnEmployeeId.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnEmployeeId.setToolTipText("Share No");
        btnEmployeeId.setEnabled(false);
        btnEmployeeId.setMinimumSize(new java.awt.Dimension(21, 21));
        btnEmployeeId.setPreferredSize(new java.awt.Dimension(21, 21));
        btnEmployeeId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmployeeIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panEmployeeId.add(btnEmployeeId, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 19, 2, 18);
        cPanel1.add(panEmployeeId, gridBagConstraints);

        panPfClose.add(cPanel1, new java.awt.GridBagConstraints());

        getContentPane().add(panPfClose, java.awt.BorderLayout.CENTER);

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

        mbrPfMaster.add(mnuProcess);

        setJMenuBar(mbrPfMaster);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    //__ To avaoid the looping of the testCase...
    boolean isTest = false;

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        observable.resetForm();                 //__ Reset the fields in the UI to null...
        setTxtEnabled(false);
        resetTextFields();
        ClientUtil.enableDisable(this, false);  //__ Disables the panel...
        setButtonEnableDisable();               //__ Enables or Disables the buttons and menu Items depending on their previous state...
        setEnableDisableButtons(false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);  //__ Sets the Action Type to be performed...
        observable.setStatus();
        viewType = -1;
        lblStatus.setText("Cancel");
        btnSave.setEnabled(false);
        btnNew.setEnabled(true);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
        //__ Make the Screen Closable..
        setModified(false);
        empDetailsUI.updateEmployeeInfo(null);
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        final String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panPfClose);
        if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0) {
            displayAlert(mandatoryMessage);
        } else {
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                boolean chkEmpId = observable.chkEmpIdExists(txtEmployeeId.getText());
                if (chkEmpId == true) {
                    ClientUtil.showMessageWindow("This Employee already exists!!!");
                    return;
                }
            }
            updateOBFields();
            observable.doAction();
            if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                setTxtEnabled(false);
                lblStatus.setText("Success");
                resetTextFields();
                observable.resetForm();
            } else if (observable.getResult() == ClientConstants.ACTIONTYPE_FAILED) {
                lblStatus.setText("Failed");
                setTxtEnabled(false);
                resetTextFields();
                observable.resetForm();
            }
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        if (viewType == ClientConstants.ACTIONTYPE_DELETE && isFilled == false) {
            view = "DELETE";
            isFilled = true;
            observable.doAction();
            if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                lblStatus.setText("Deleted");
                resetTextFields();
                observable.resetForm();
            } else if (observable.getResult() == ClientConstants.ACTIONTYPE_FAILED) {
                lblStatus.setText("Failed");
                resetTextFields();
                observable.resetForm();
            }
        } else {
            observable.setActionType(ClientConstants.ACTIONTYPE_DELETE); //__ Sets the Action Type to be performed...
            callView(ClientConstants.ACTIONTYPE_DELETE);
            lblStatus.setText("Delete");
            btnDelete.setEnabled(true);
            btnNew.setEnabled(false);
            btnEdit.setEnabled(false);
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT); //__ Sets the Action Type to be performed...
        popUp(EDIT);
        setTxtEnabled(true);       
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
        btnSave.setEnabled(true);
        lblStatus.setText("Edit");
    }//GEN-LAST:event_btnEditActionPerformed

    private void popUp(int field) {
        viewType = field;
        if (field == EMPLOYEE) {
            final HashMap viewMap = new HashMap();
            viewMap.put(CommonConstants.MAP_NAME, "getEmployeeDet");
            new ViewAll(this, viewMap).show();
        } else if (field == EDIT) {
            final HashMap viewMap = new HashMap();
            viewMap.put(CommonConstants.MAP_NAME, "getEmpPfDetails");
            new ViewAll(this, viewMap).show();
        }
    }

    private void callView(int viewType) {
        HashMap viewMap = new HashMap();
        this.viewType = viewType;
        if (viewType == ClientConstants.ACTIONTYPE_DELETE) {
            viewMap.put(CommonConstants.MAP_NAME, "getEmpPfDetails");
            isFilled = false;
        }
        new ViewAll(this, viewMap).show();
    }
    // this method is called automatically from ViewAll...

    public void fillData(Object param) {
        try {
            final HashMap hash = (HashMap) param;
            if (viewType == EMPLOYEE) {
                txtEmployeeId.setText(CommonUtil.convertObjToStr(hash.get("EMPLOYEEID")));
                empDetailsUI.updateEmployeeInfo(txtEmployeeId.getText());
            } else if (viewType == EDIT || viewType == ClientConstants.ACTIONTYPE_DELETE) {
                txtEmployeeId.setText(CommonUtil.convertObjToStr(hash.get("EMP_ID")));
                empDetailsUI.updateEmployeeInfo(txtEmployeeId.getText());
                observable.populateOB(hash);
                if (viewType == ClientConstants.ACTIONTYPE_DELETE) {
                    setTxtEnabled(false);
                }
            }
            observable.ttNotifyObservers();
            //__ To Save the data in the Internal Frame...
            setModified(true);
        } catch (Exception ex) {
            Logger.getLogger(PFMasterUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setTxtEnabled(true);
        btnCancel.setEnabled(true);
        btnSave.setEnabled(true);
        btnNew.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        lblStatus.setText("New");
        //__ To Save the data in the Internal Frame...
        setModified(true);
        empDetailsUI.updateEmployeeInfo(null);
    }//GEN-LAST:event_btnNewActionPerformed
    private void setEnableDisableButtons(boolean value) {
        btnEmployeeId.setEnabled(value);
    }

    private void resetTextFields() {
        txtEmployeeId.setText("");
        txtPfAccountNo.setText("");
        tdtPfDate.setDateValue(null);
        tdtLastInterestDate.setDateValue(null);
        tdtPfOpeningDate.setDateValue(null);
        txtEmployerContribution.setText("");
        txtOpeningBalance.setText("");
        txtPfNomineeName.setText("");
        txtPfNomineeRelation.setText("");
        txtPfRateOfInterest.setText("");
        empDetailsUI.updateEmployeeInfo(null);
    }

    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // TODO add your handling code here:
        btnCloseActionPerformed(null);
    }//GEN-LAST:event_mitCloseActionPerformed

    private void mitPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitPrintActionPerformed
        // TODO add your handling code here:
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
        btnEditActionPerformed(null);
    }//GEN-LAST:event_mitNewActionPerformed

    /**
     * Exit the Application
     */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm

private void btnEmployeeIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmployeeIdActionPerformed
    // TODO add your handling code here:
    popUp(EMPLOYEE);
}//GEN-LAST:event_btnEmployeeIdActionPerformed

    private void tdtLastInterestDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtLastInterestDateFocusLost
        // TODO add your handling code here:
        ClientUtil.validateToDate(tdtLastInterestDate, tdtPfOpeningDate.getDateValue());
    }//GEN-LAST:event_tdtLastInterestDateFocusLost

    private void txtPfAccountNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPfAccountNoFocusLost
        // TODO add your handling code here:
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            boolean chkPfAct = observable.chkPFActNoExists(txtPfAccountNo.getText());
            if (chkPfAct == true) {
                ClientUtil.showMessageWindow("This Account No already exists!!!");
                txtPfAccountNo.setText("");
            }
        }
    }//GEN-LAST:event_txtPfAccountNoFocusLost

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAuthorizeActionPerformed

    public void setTxtEnabled(boolean flag) {
        btnEmployeeId.setEnabled(flag);
        txtPfAccountNo.setEnabled(flag);
        tdtPfDate.setEnabled(flag);
        tdtPfOpeningDate.setEnabled(flag);
        txtOpeningBalance.setEnabled(flag);
        txtPfRateOfInterest.setEnabled(flag);
        tdtLastInterestDate.setEnabled(flag);
        txtPfNomineeName.setEnabled(flag);
        txtPfNomineeRelation.setEnabled(flag);
        txtEmployerContribution.setEnabled(flag);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new PFMasterUI().show();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnEmployeeId;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CPanel cPanel1;
    private com.see.truetransact.uicomponent.CLabel lblBatchIdValue;
    private com.see.truetransact.uicomponent.CLabel lblEmployeeId;
    private com.see.truetransact.uicomponent.CLabel lblEmployerContribution;
    private com.see.truetransact.uicomponent.CLabel lblLastInterestDate;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblOpeningBalance;
    private com.see.truetransact.uicomponent.CLabel lblPfAccountNo;
    private com.see.truetransact.uicomponent.CLabel lblPfDate;
    private com.see.truetransact.uicomponent.CLabel lblPfNomineeName;
    private com.see.truetransact.uicomponent.CLabel lblPfNomineeRelation;
    private com.see.truetransact.uicomponent.CLabel lblPfOpeningDate;
    private javax.swing.JLabel lblPfRate;
    private com.see.truetransact.uicomponent.CLabel lblPfRateOfInterest;
    private com.see.truetransact.uicomponent.CLabel lblSpace;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace29;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace30;
    private com.see.truetransact.uicomponent.CLabel lblSpace31;
    private com.see.truetransact.uicomponent.CLabel lblSpace32;
    private com.see.truetransact.uicomponent.CLabel lblSpace33;
    private com.see.truetransact.uicomponent.CLabel lblSpace34;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CMenuBar mbrPfMaster;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panEmpDetails;
    private com.see.truetransact.uicomponent.CPanel panEmployeeId;
    private com.see.truetransact.uicomponent.CPanel panPfClose;
    private com.see.truetransact.uicomponent.CPanel panPfMaster;
    private com.see.truetransact.uicomponent.CPanel panShareCloseTrans;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptPrint;
    private javax.swing.JSeparator sptProcess;
    private javax.swing.JToolBar tbrPfMaster;
    private com.see.truetransact.uicomponent.CDateField tdtLastInterestDate;
    private com.see.truetransact.uicomponent.CDateField tdtPfDate;
    private com.see.truetransact.uicomponent.CDateField tdtPfOpeningDate;
    private com.see.truetransact.uicomponent.CTextField txtEmployeeId;
    private com.see.truetransact.uicomponent.CTextField txtEmployerContribution;
    private com.see.truetransact.uicomponent.CTextField txtOpeningBalance;
    private com.see.truetransact.uicomponent.CTextField txtPfAccountNo;
    private com.see.truetransact.uicomponent.CTextField txtPfNomineeName;
    private com.see.truetransact.uicomponent.CTextField txtPfNomineeRelation;
    private com.see.truetransact.uicomponent.CTextField txtPfRateOfInterest;
    // End of variables declaration//GEN-END:variables
}
