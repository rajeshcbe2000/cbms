/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * InterestSubventionAdjustmentUI.java
 * Created on June 13, 2013, 11:27 AM
 *
 */
package com.see.truetransact.ui.termloan.kcctopacs;

/**
 * @author Suresh R
 */
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.ui.common.transaction.TransactionUI;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.uivalidation.NumericValidation;
import java.util.ArrayList;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.util.List;
import javax.swing.table.*;

public class InterestSubventionAdjustmentUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer, UIMandatoryField {

    private final static ClientParseException parseException = ClientParseException.getInstance();
    //    private RemittanceProductRB resourceBundle = new RemittanceProductRB();
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.mdsapplication.MDSApplicationRB", ProxyParameters.LANGUAGE);
//    final MDSApplicationMRB objMandatoryMRB = new MDSApplicationMRB();
    TransactionUI transactionUI = new TransactionUI();
    private HashMap mandatoryMap;
    InterestSubventionAdjustmentOB observable = null;
    final String AUTHORIZE = "Authorize";
    private String viewType = new String();
    private List finalList = null;
    public int selectedRow = -1;
    private boolean isFilled = false;

    /**
     * Creates new form BeanForm
     */
    public InterestSubventionAdjustmentUI() {
        initComponents();
        settingupUI();
        tabInterestSubventionAdjustment.resetVisits();
        panTransactionDetails.add(transactionUI);
        transactionUI.setSourceScreen("INTEREST_SUBVENTION");
        observable.setTransactionOB(transactionUI.getTransactionOB());
    }

    private void settingupUI() {
        setFieldNames();
        observable = new InterestSubventionAdjustmentOB();
        initComponentData();
        setMaximumLength();
        setButtonEnableDisable();
        ClientUtil.enableDisable(panInterestSubventionAdjustmentDetails, false);
        lblCustomerName.setText("");
        btnCustomerID.setEnabled(false);
    }

    /*
     * Auto Generated Method - setFieldNames() This method assigns name for all
     * the components. Other functions are working based on this name.
     */
    private void setFieldNames() {
    }

    /**
     * Auto Generated Method - setMandatoryHashMap() This method list out all
     * the Input Fields available in the UI. It needs a class level HashMap
     * variable mandatoryMap.
     */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
    }

    /**
     * Auto Generated Method - getMandatoryHashMap() Getter method for
     * setMandatoryHashMap().
     */
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    private void initComponentData() {
        try {
            cboAgencyName.setModel(observable.getCbmAgencyName());
        } catch (ClassCastException e) {
            parseException.logException(e, true);
        }
    }

    private void setMaximumLength() {
        txtCustomerID.setAllowAll(true);
        txtReleaseRefNo.setAllowAll(true);
        txtFinancialYear.setMaxLength(4);
        txtFinancialYearEnd.setMaxLength(4);
        txtFinancialYear.setValidation(new NumericValidation());
        txtFinancialYearEnd.setValidation(new NumericValidation());
        txtClaimedAmount.setValidation(new CurrencyValidation(14, 2));
        txtReceivedAmount.setValidation(new CurrencyValidation(14, 2));
    }

    /**
     * Auto Generated Method - update() This method called by Observable. It
     * updates the UI with Observable's data. If needed add/Remove RadioButtons
     * method need to be added.
     */
    public void update(Observable observed, Object arg) {
    }

    /**
     * Auto Generated Method - updateOBFields() This method called by Save
     * option of UI. It updates the OB with UI data.
     */
    public void updateOBFields() {
        observable.setCboAgencyName(CommonUtil.convertObjToStr(cboAgencyName.getSelectedItem()));
        observable.setTxtReleaseRefNo(txtReleaseRefNo.getText());
        observable.setTdtReleaseDate(tdtReleaseDate.getDateValue());
        observable.setTxtFinancialYear(txtFinancialYear.getText());
        observable.setTxtFinancialYearEnd(txtFinancialYearEnd.getText());
        observable.setTxtClaimedAmount(txtClaimedAmount.getText());
        observable.setTxtReceivedAmount(txtReceivedAmount.getText());
        observable.setTxtCustomerID(txtCustomerID.getText());
    }

    public void update() {
        cboAgencyName.setSelectedItem(observable.getCboAgencyName());
        txtReleaseRefNo.setText(observable.getTxtReleaseRefNo());
        tdtReleaseDate.setDateValue(observable.getTdtReleaseDate());
        txtFinancialYear.setText(observable.getTxtFinancialYear());
        txtFinancialYearEnd.setText(observable.getTxtFinancialYearEnd());
        txtClaimedAmount.setText(observable.getTxtClaimedAmount());
        txtReceivedAmount.setText(observable.getTxtReceivedAmount());
        txtCustomerID.setText(observable.getTxtCustomerID());
    }

    /**
     * Auto Generated Method - setHelpMessage() This method shows tooltip help
     * for all the input fields available in the UI. It needs the Mandatory
     * Resource Bundle object. Help display Label name should be lblMsg.
     */
    public void setHelpMessage() {
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdgIsLapsedGR = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgEFTProductGR = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgPayableBranchGR = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgPrintServicesGR = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgSeriesGR = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrAdvances = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace6 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace70 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace71 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace72 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace73 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace74 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace75 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        tabInterestSubventionAdjustment = new com.see.truetransact.uicomponent.CTabbedPane();
        panInterestSubventionAdjustment = new com.see.truetransact.uicomponent.CPanel();
        panInterestSubventionAdjustmentDetails = new com.see.truetransact.uicomponent.CPanel();
        panInsideIntSubventionDetails = new com.see.truetransact.uicomponent.CPanel();
        lblAgencyName = new com.see.truetransact.uicomponent.CLabel();
        cboAgencyName = new com.see.truetransact.uicomponent.CComboBox();
        lblReleaseRefNo = new com.see.truetransact.uicomponent.CLabel();
        txtReleaseRefNo = new com.see.truetransact.uicomponent.CTextField();
        lblReleaseDate = new com.see.truetransact.uicomponent.CLabel();
        tdtReleaseDate = new com.see.truetransact.uicomponent.CDateField();
        lblFinancialYear = new com.see.truetransact.uicomponent.CLabel();
        panFinancialYear = new com.see.truetransact.uicomponent.CPanel();
        txtFinancialYear = new com.see.truetransact.uicomponent.CTextField();
        lblDash = new com.see.truetransact.uicomponent.CLabel();
        txtFinancialYearEnd = new com.see.truetransact.uicomponent.CTextField();
        panIntSubventionDetails = new com.see.truetransact.uicomponent.CPanel();
        lblCustomerName = new com.see.truetransact.uicomponent.CLabel();
        txtReceivedAmount = new com.see.truetransact.uicomponent.CTextField();
        txtClaimedAmount = new com.see.truetransact.uicomponent.CTextField();
        lblClaimedAmount = new com.see.truetransact.uicomponent.CLabel();
        lblReceivedAmount = new com.see.truetransact.uicomponent.CLabel();
        lblCustomerID = new com.see.truetransact.uicomponent.CLabel();
        txtCustomerID = new com.see.truetransact.uicomponent.CTextField();
        btnCustomerID = new com.see.truetransact.uicomponent.CButton();
        panTransactionDetails = new com.see.truetransact.uicomponent.CPanel();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        mbrMain = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptDelete = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitAuthorize = new javax.swing.JMenuItem();
        mitReject = new javax.swing.JMenuItem();
        mitException = new javax.swing.JMenuItem();
        sptException = new javax.swing.JSeparator();
        mitPrint = new javax.swing.JMenuItem();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setMinimumSize(new java.awt.Dimension(860, 620));
        setPreferredSize(new java.awt.Dimension(860, 620));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setEnabled(false);
        btnView.setMinimumSize(new java.awt.Dimension(25, 27));
        btnView.setPreferredSize(new java.awt.Dimension(25, 27));
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnView);

        lblSpace6.setText("     ");
        tbrAdvances.add(lblSpace6);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnNew);

        lblSpace70.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace70.setText("     ");
        lblSpace70.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace70.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace70.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace70);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnEdit);

        lblSpace71.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace71.setText("     ");
        lblSpace71.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace71.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace71.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace71);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnDelete);

        lblSpace3.setText("     ");
        tbrAdvances.add(lblSpace3);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnSave);

        lblSpace72.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace72.setText("     ");
        lblSpace72.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace72.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace72.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace72);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnCancel);

        lblSpace4.setText("     ");
        tbrAdvances.add(lblSpace4);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnAuthorize);

        lblSpace73.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace73.setText("     ");
        lblSpace73.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace73);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnException);

        lblSpace74.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace74.setText("     ");
        lblSpace74.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace74);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnReject);

        lblSpace5.setText("     ");
        tbrAdvances.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrAdvances.add(btnPrint);

        lblSpace75.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace75.setText("     ");
        lblSpace75.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace75);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnClose);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(tbrAdvances, gridBagConstraints);

        tabInterestSubventionAdjustment.setMinimumSize(new java.awt.Dimension(850, 480));
        tabInterestSubventionAdjustment.setPreferredSize(new java.awt.Dimension(850, 480));

        panInterestSubventionAdjustment.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panInterestSubventionAdjustment.setMinimumSize(new java.awt.Dimension(850, 450));
        panInterestSubventionAdjustment.setPreferredSize(new java.awt.Dimension(850, 450));
        panInterestSubventionAdjustment.setLayout(new java.awt.GridBagLayout());

        panInterestSubventionAdjustmentDetails.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panInterestSubventionAdjustmentDetails.setMinimumSize(new java.awt.Dimension(820, 215));
        panInterestSubventionAdjustmentDetails.setPreferredSize(new java.awt.Dimension(820, 215));
        panInterestSubventionAdjustmentDetails.setLayout(new java.awt.GridBagLayout());

        panInsideIntSubventionDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panInsideIntSubventionDetails.setMinimumSize(new java.awt.Dimension(390, 175));
        panInsideIntSubventionDetails.setPreferredSize(new java.awt.Dimension(390, 175));
        panInsideIntSubventionDetails.setLayout(new java.awt.GridBagLayout());

        lblAgencyName.setText("Name of the Agency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideIntSubventionDetails.add(lblAgencyName, gridBagConstraints);

        cboAgencyName.setMinimumSize(new java.awt.Dimension(100, 21));
        cboAgencyName.setPopupWidth(150);
        cboAgencyName.setPreferredSize(new java.awt.Dimension(100, 20));
        cboAgencyName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboAgencyNameActionPerformed(evt);
            }
        });
        cboAgencyName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboAgencyNameFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideIntSubventionDetails.add(cboAgencyName, gridBagConstraints);

        lblReleaseRefNo.setText("Interest Subvention  Release Ref No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideIntSubventionDetails.add(lblReleaseRefNo, gridBagConstraints);

        txtReleaseRefNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideIntSubventionDetails.add(txtReleaseRefNo, gridBagConstraints);

        lblReleaseDate.setText("Interest Subvention Release Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideIntSubventionDetails.add(lblReleaseDate, gridBagConstraints);

        tdtReleaseDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtReleaseDate.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideIntSubventionDetails.add(tdtReleaseDate, gridBagConstraints);

        lblFinancialYear.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblFinancialYear.setText("Interest Subvention for the Financial Year");
        lblFinancialYear.setMaximumSize(new java.awt.Dimension(250, 18));
        lblFinancialYear.setMinimumSize(new java.awt.Dimension(250, 18));
        lblFinancialYear.setPreferredSize(new java.awt.Dimension(250, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideIntSubventionDetails.add(lblFinancialYear, gridBagConstraints);

        panFinancialYear.setMinimumSize(new java.awt.Dimension(120, 21));
        panFinancialYear.setPreferredSize(new java.awt.Dimension(120, 21));
        panFinancialYear.setLayout(new java.awt.GridBagLayout());

        txtFinancialYear.setMinimumSize(new java.awt.Dimension(50, 21));
        txtFinancialYear.setPreferredSize(new java.awt.Dimension(50, 21));
        txtFinancialYear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFinancialYearActionPerformed(evt);
            }
        });
        txtFinancialYear.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFinancialYearFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFinancialYear.add(txtFinancialYear, gridBagConstraints);

        lblDash.setText("-");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFinancialYear.add(lblDash, gridBagConstraints);

        txtFinancialYearEnd.setMinimumSize(new java.awt.Dimension(50, 21));
        txtFinancialYearEnd.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFinancialYear.add(txtFinancialYearEnd, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 3, 3);
        panInsideIntSubventionDetails.add(panFinancialYear, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panInterestSubventionAdjustmentDetails.add(panInsideIntSubventionDetails, gridBagConstraints);

        panIntSubventionDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panIntSubventionDetails.setMinimumSize(new java.awt.Dimension(390, 175));
        panIntSubventionDetails.setPreferredSize(new java.awt.Dimension(390, 175));
        panIntSubventionDetails.setLayout(new java.awt.GridBagLayout());

        lblCustomerName.setForeground(new java.awt.Color(0, 51, 204));
        lblCustomerName.setText("PACS Name");
        lblCustomerName.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panIntSubventionDetails.add(lblCustomerName, gridBagConstraints);

        txtReceivedAmount.setMaximumSize(new java.awt.Dimension(100, 21));
        txtReceivedAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtReceivedAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtReceivedAmountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIntSubventionDetails.add(txtReceivedAmount, gridBagConstraints);

        txtClaimedAmount.setMaximumSize(new java.awt.Dimension(100, 21));
        txtClaimedAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtClaimedAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtClaimedAmountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIntSubventionDetails.add(txtClaimedAmount, gridBagConstraints);

        lblClaimedAmount.setText("Interest Subvention Amount  Claimed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIntSubventionDetails.add(lblClaimedAmount, gridBagConstraints);

        lblReceivedAmount.setText("Interest Subvention Amount Received");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIntSubventionDetails.add(lblReceivedAmount, gridBagConstraints);

        lblCustomerID.setText("PACS Customer ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIntSubventionDetails.add(lblCustomerID, gridBagConstraints);

        txtCustomerID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCustomerID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCustomerIDFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 0);
        panIntSubventionDetails.add(txtCustomerID, gridBagConstraints);

        btnCustomerID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnCustomerID.setToolTipText("Select Customer");
        btnCustomerID.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCustomerID.setMaximumSize(new java.awt.Dimension(22, 21));
        btnCustomerID.setMinimumSize(new java.awt.Dimension(22, 21));
        btnCustomerID.setPreferredSize(new java.awt.Dimension(22, 21));
        btnCustomerID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustomerIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panIntSubventionDetails.add(btnCustomerID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panInterestSubventionAdjustmentDetails.add(panIntSubventionDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterestSubventionAdjustment.add(panInterestSubventionAdjustmentDetails, gridBagConstraints);

        panTransactionDetails.setMinimumSize(new java.awt.Dimension(830, 230));
        panTransactionDetails.setPreferredSize(new java.awt.Dimension(830, 230));
        panTransactionDetails.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterestSubventionAdjustment.add(panTransactionDetails, gridBagConstraints);

        tabInterestSubventionAdjustment.addTab("Interest Subvention Adjustment Details", panInterestSubventionAdjustment);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(tabInterestSubventionAdjustment, gridBagConstraints);

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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(panStatus, gridBagConstraints);

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
        mnuProcess.add(sptDelete);

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

        mitAuthorize.setText("Authorize");
        mnuProcess.add(mitAuthorize);

        mitReject.setText("Rejection");
        mnuProcess.add(mitReject);

        mitException.setText("Exception");
        mnuProcess.add(mitException);
        mnuProcess.add(sptException);

        mitPrint.setText("Print");
        mnuProcess.add(mitPrint);

        mitClose.setText("Close");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrMain.add(mnuProcess);

        setJMenuBar(mbrMain);
    }// </editor-fold>//GEN-END:initComponents

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_VIEW);
        popUp("Enquiry");
        ClientUtil.enableDisable(panInterestSubventionAdjustment, false);
        lblStatus.setText("Enquiry");
        btnSave.setEnabled(false);
        btnView.setEnabled(false);
    }            //    private void enableDisableAliasBranchTable(boolean flag) {//GEN-LAST:event_btnViewActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // Add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_REJECT);
        updateAuthorizeStatus(CommonConstants.STATUS_REJECTED);
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        updateAuthorizeStatus(CommonConstants.STATUS_EXCEPTION);
        btnCancel.setEnabled(true);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // Add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
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
            singleAuthorizeMap.put("ADJUSTMENT_NO", observable.getSubventionAdjustNo());
            singleAuthorizeMap.put("AUTHORIZED_BY", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("AUTHORIZED_DT", ClientUtil.getCurrentDateWithTime());
            arrList.add(singleAuthorizeMap);
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorize(authorizeMap, observable.getSubventionAdjustNo());
            viewType = "";
            singleAuthorizeMap = null;
            arrList = null;
            authorizeMap = null;
        } else {
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put("USER_ID", TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            mapParam.put(CommonConstants.MAP_NAME, "getSubventionTransAuthorize");
            isFilled = false;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
            observable.setStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }

    public void authorize(HashMap map, String id) {
        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
            observable.set_authorizeMap(map);
            if (transactionUI.getOutputTO().size() > 0) {
                observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
            }
            observable.doAction();
            btnCancelActionPerformed(null);
            observable.setStatus();
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
        ClientUtil.clearAll(this);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        ClientUtil.enableDisable(panInterestSubventionAdjustmentDetails, true);
        transactionUI.cancelAction(false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.resetObjects();
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
        btnCustomerID.setEnabled(true);
        lblCustomerName.setText("");
        txtFinancialYearEnd.setEnabled(false);
        setModified(true);
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_VIEW);
        popUp("Edit");
        lblStatus.setText("Edit");
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_VIEW);
        popUp("Delete");
        lblStatus.setText("Delete");
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        ClientUtil.enableDisable(panInterestSubventionAdjustment, false);
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
// Add your handling code here:
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT
                || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
            int transactionSize = 0;
            if (transactionUI.getOutputTO().size() == 0 && CommonUtil.convertObjToDouble(txtReceivedAmount.getText()).doubleValue() > 0) {
                ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS));
                return;
            } else {
                if (CommonUtil.convertObjToDouble(txtReceivedAmount.getText()).doubleValue() > 0) {
                    transactionSize = (transactionUI.getOutputTO()).size();
                    if (transactionSize != 1 && CommonUtil.convertObjToDouble(txtReceivedAmount.getText()).doubleValue() > 0) {
                        ClientUtil.showAlertWindow("Multiple Transactions are Not allowed, Make it one Transaction");
                        return;
                    } else {
                        observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                    }
                } else if (transactionUI.getOutputTO().size() > 0) {
                    observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                }
            }
            if (transactionSize == 0 && CommonUtil.convertObjToDouble(txtReceivedAmount.getText()).doubleValue() > 0) {
                ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS));
                return;
            } else if (transactionSize != 0) {
                if (!transactionUI.isBtnSaveTransactionDetailsFlag() && observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                    ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.SAVE_TX_DETAILS));
                    return;
                }
                if (transactionUI.getOutputTO().size() > 0) {
                    observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                    savePerformed();
                }
            }
        }
        //__ Make the Screen Closable..
        setModified(false);
    }//GEN-LAST:event_btnSaveActionPerformed
    private void savePerformed() {
        updateOBFields();
        observable.doAction();
        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            if (observable.getProxyReturnMap() != null && observable.getProxyReturnMap().size() > 0) {
                if (observable.getProxyReturnMap().containsKey("TRANSFER_TRANS_LIST")) {
                    displayTransDetail(observable.getProxyReturnMap());
                }
            }
            btnCancelActionPerformed(null);
            lblStatus.setText(ClientConstants.RESULT_STATUS[observable.getResult()]);
        }
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
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
                        transferDisplayStr += "   Ac Hd Desc : " + transMap.get("AC_HD_ID")
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

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        viewType = "CANCEL";
        lblStatus.setText("               ");
        ClientUtil.clearAll(this);
        observable.resetForm();
        ClientUtil.enableDisable(this, false);
        btnNew.setEnabled(true);
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        btnSave.setEnabled(false);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
        btnView.setEnabled(true);
        btnCancel.setEnabled(false);
        isFilled = false;
        transactionUI.setCallingApplicantName("");
        transactionUI.setCallingAmount("");
        transactionUI.setCallingTransAcctNo("");
        transactionUI.setCallingProdID("");
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
        btnCustomerID.setEnabled(false);
        lblCustomerName.setText("");
        ClientUtil.enableDisable(this, false);
        setModified(false);
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
        //        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // Add your handling code here:
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed

    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // Add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed

    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // Add your handling code here:
        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed

    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // Add your handling code here:
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed

    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // Add your handling code here:
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed

    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnCloseActionPerformed(evt);
        //        this.dispose();
    }//GEN-LAST:event_mitCloseActionPerformed

    private void cboAgencyNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboAgencyNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboAgencyNameActionPerformed

    private void cboAgencyNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboAgencyNameFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_cboAgencyNameFocusLost

    private void txtFinancialYearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFinancialYearActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFinancialYearActionPerformed

    private void txtClaimedAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtClaimedAmountFocusLost
   }//GEN-LAST:event_txtClaimedAmountFocusLost

    private void txtReceivedAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtReceivedAmountFocusLost
        if (txtReceivedAmount.getText().length() > 0) {
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                if ((CommonUtil.convertObjToDouble(txtClaimedAmount.getText()).doubleValue()) < (CommonUtil.convertObjToDouble(txtReceivedAmount.getText()).doubleValue())) {
                    ClientUtil.showMessageWindow("Received Amount should not be Greater than Claimed Amount...");
                    txtReceivedAmount.setText("");
                }
                transactionUI.cancelAction(false);
                transactionUI.setButtonEnableDisable(true);
                transactionUI.resetObjects();
                transactionUI.setCallingTransType("TRANSFER");
                transactionUI.setCallingAmount(txtReceivedAmount.getText());
            }
        }
   }//GEN-LAST:event_txtReceivedAmountFocusLost

    private void txtCustomerIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCustomerIDFocusLost
        // TODO add your handling code here:
        if (txtCustomerID.getText().length() > 0) {
            HashMap whereMap = new HashMap();
            whereMap.put("CUST_ID", txtCustomerID.getText());
            List lst = ClientUtil.executeQuery("getPACSCustID", whereMap);
            if (lst != null && lst.size() > 0) {
                viewType = "PACS_CUST_ID";
                whereMap = (HashMap) lst.get(0);
                fillData(whereMap);
                lst = null;
                whereMap = null;
            } else {
                ClientUtil.showMessageWindow("Invalid Customer ID !!! ");
                txtCustomerID.setText("");
                lblCustomerName.setText("");
            }
        }
    }//GEN-LAST:event_txtCustomerIDFocusLost

    private void btnCustomerIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustomerIDActionPerformed
        // TODO add your handling code here:
        popUp("PACS_CUST_ID");
    }//GEN-LAST:event_btnCustomerIDActionPerformed

    private void txtFinancialYearFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFinancialYearFocusLost
        // TODO add your handling code here:
        if (txtFinancialYear.getText().length() > 0) {
            int startYear = CommonUtil.convertObjToInt(txtFinancialYear.getText());
            txtFinancialYearEnd.setText(CommonUtil.convertObjToStr(startYear + 1));
        } else {
            txtFinancialYearEnd.setText("");
        }
    }//GEN-LAST:event_txtFinancialYearFocusLost
    /**
     * To display a popUp window for viewing existing data
     */
    private void popUp(String currAction) {
        viewType = currAction;
        HashMap viewMap = new HashMap();
        if (currAction.equalsIgnoreCase("Edit") || currAction.equalsIgnoreCase("Delete")) {
            HashMap map = new HashMap();
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getSubventionTransEditDelete");
        } else if (currAction.equalsIgnoreCase("Enquiry")) {
            HashMap map = new HashMap();
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getSubventionTransEnquiry");
        } else if (currAction.equals("PACS_CUST_ID")) {
            HashMap map = new HashMap();
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getPACSCustID");
        }
        new ViewAll(this, viewMap).show();
    }

    public void fillData(Object obj) {
        try {
            HashMap hashMap = (HashMap) obj;
            System.out.println("### fillData Hash : " + hashMap);
            isFilled = true;
            if (viewType == "PACS_CUST_ID") {
                txtCustomerID.setText(CommonUtil.convertObjToStr(hashMap.get("CUST_ID")));
                lblCustomerName.setText(CommonUtil.convertObjToStr(hashMap.get("CUSTOMER_NAME")));
            } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
                this.setButtonEnableDisable();
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT
                        || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
                    hashMap.put("DISPLAY_TRANS_DETAILS", "DISPLAY_TRANS_DETAILS");
                    ClientUtil.enableDisable(panInsideIntSubventionDetails, true);
                }
                observable.setSubventionAdjustNo(CommonUtil.convertObjToStr(hashMap.get("ADJUSTMENT_NO")));
                observable.getData(hashMap);
                update();
                displayCustName();
                if (observable.getProxyReturnMap() != null && observable.getProxyReturnMap().size() > 0) {
                    if (observable.getProxyReturnMap().containsKey("TRANSFER_TRANS_LIST")) {
                        displayTransDetail(observable.getProxyReturnMap());
                        observable.setProxyReturnMap(null);
                    }
                }
            } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
                this.setButtonEnableDisable();
                hashMap.put("DISPLAY_TRANS_DETAILS", "DISPLAY_TRANS_DETAILS");
                observable.setSubventionAdjustNo(CommonUtil.convertObjToStr(hashMap.get("ADJUSTMENT_NO")));
                observable.getData(hashMap);
                update();
                displayCustName();
                if (observable.getProxyReturnMap() != null && observable.getProxyReturnMap().size() > 0) {
                    if (observable.getProxyReturnMap().containsKey("TRANSFER_TRANS_LIST")) {
                        displayTransDetail(observable.getProxyReturnMap());
                        observable.setProxyReturnMap(null);
                    }
                }
            }
            if (viewType == AUTHORIZE) {
                btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                ClientUtil.enableDisable(this, false);
            }
            hashMap = null;
            btnCancel.setEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }

    private void displayCustName() {
        if (txtCustomerID.getText().length() > 0) {
            HashMap custMap = new HashMap();
            custMap.put("CUST_ID", txtCustomerID.getText());
            List namelst = ClientUtil.executeQuery("getCustNameForDeposit", custMap);
            if (namelst.size() > 0) {
                custMap = (HashMap) namelst.get(0);
                lblCustomerName.setText(CommonUtil.convertObjToStr(custMap.get("CUSTOMER_NAME")));
            }
        }
    }

    private void displayAlert(String message) {
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }

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

        btnAuthorize.setEnabled(btnNew.isEnabled());
        btnReject.setEnabled(btnNew.isEnabled());
        btnException.setEnabled(btnNew.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnCustomerID;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboAgencyName;
    private com.see.truetransact.uicomponent.CLabel lblAgencyName;
    private com.see.truetransact.uicomponent.CLabel lblClaimedAmount;
    private com.see.truetransact.uicomponent.CLabel lblCustomerID;
    private com.see.truetransact.uicomponent.CLabel lblCustomerName;
    private com.see.truetransact.uicomponent.CLabel lblDash;
    private com.see.truetransact.uicomponent.CLabel lblFinancialYear;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblReceivedAmount;
    private com.see.truetransact.uicomponent.CLabel lblReleaseDate;
    private com.see.truetransact.uicomponent.CLabel lblReleaseRefNo;
    private com.see.truetransact.uicomponent.CLabel lblSpace;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace6;
    private com.see.truetransact.uicomponent.CLabel lblSpace70;
    private com.see.truetransact.uicomponent.CLabel lblSpace71;
    private com.see.truetransact.uicomponent.CLabel lblSpace72;
    private com.see.truetransact.uicomponent.CLabel lblSpace73;
    private com.see.truetransact.uicomponent.CLabel lblSpace74;
    private com.see.truetransact.uicomponent.CLabel lblSpace75;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitAuthorize;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitException;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitReject;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panFinancialYear;
    private com.see.truetransact.uicomponent.CPanel panInsideIntSubventionDetails;
    private com.see.truetransact.uicomponent.CPanel panIntSubventionDetails;
    private com.see.truetransact.uicomponent.CPanel panInterestSubventionAdjustment;
    private com.see.truetransact.uicomponent.CPanel panInterestSubventionAdjustmentDetails;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTransactionDetails;
    private com.see.truetransact.uicomponent.CButtonGroup rdgEFTProductGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgIsLapsedGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgPayableBranchGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgPrintServicesGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgSeriesGR;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private javax.swing.JSeparator sptException;
    private com.see.truetransact.uicomponent.CTabbedPane tabInterestSubventionAdjustment;
    private javax.swing.JToolBar tbrAdvances;
    private com.see.truetransact.uicomponent.CDateField tdtReleaseDate;
    private com.see.truetransact.uicomponent.CTextField txtClaimedAmount;
    private com.see.truetransact.uicomponent.CTextField txtCustomerID;
    private com.see.truetransact.uicomponent.CTextField txtFinancialYear;
    private com.see.truetransact.uicomponent.CTextField txtFinancialYearEnd;
    private com.see.truetransact.uicomponent.CTextField txtReceivedAmount;
    private com.see.truetransact.uicomponent.CTextField txtReleaseRefNo;
    // End of variables declaration//GEN-END:variables

    public static void main(String[] arg) {
        try {
            javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Throwable th) {
            th.printStackTrace();
        }
        javax.swing.JFrame jf = new javax.swing.JFrame();
        InterestSubventionAdjustmentUI gui = new InterestSubventionAdjustmentUI();
        jf.getContentPane().add(gui);
        jf.setSize(536, 566);
        jf.show();
        gui.show();
    }
}