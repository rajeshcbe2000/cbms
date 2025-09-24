/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * FixedAssetsTransUI.java
 *
 * Created on Jan 25, 2009, 10:53 AM
 */
package com.see.truetransact.ui.deposit.interestprocessing;

import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Observable;
import com.see.truetransact.ui.deposit.CommonMethods;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import javax.swing.JDialog;
import javax.swing.SwingWorker;

/**
 * This form is used to manipulate FixedAssetsUI related functionality
 *
 * @author nikhil
 */
//public class ShareDividendCalculationUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer, UIMandatoryField {
public class InterestProcessingUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer, UIMandatoryField {

    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.deposit.interestprocessing.InterestProcessingRB", ProxyParameters.LANGUAGE);
    InterestProcessingMRB objMandatoryRB = new InterestProcessingMRB();
    private HashMap mandatoryMap;
    private InterestProcessingOB observable;
    private Date curDate = null;
    private ArrayList getDepositDetails;
    final int EDIT = 0, DELETE = 8, ACCNOCHEQUE = 2, ACCNOSTOP = 3, ACCNOLOOSE = 4, VIEW = 10, ECSSTOP = 7;
    private int viewType = -1;
    private int DEBIT_GL = 10, PAYABLE_GL = 11;
    private int RESERVE_FUND_GL = 12;
    boolean isFilled = false;
    int updateTab = -1;
    boolean flag = false;
    final int ShareDividendCalculation = 0;
    int pan = -1;
    int panEditDelete = -1;
    int view = -1;

    public InterestProcessingUI() {
        initComponents();
        initStartup();
    }

    private void initStartup() {
        curDate = ClientUtil.getCurrentDate();
        setFieldNames();
        internationalize();
        setHelpMessage();
        setMandatoryHashMap();
        setObservable();
        initTableData();
        setMaximumLength();
        initComponentData();
        resetUI();
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panDepositInterestCalculationDetails, false);
        btnCancelActionPerformed();
        btnNewActionPerformed();
        btnCalculate.setEnabled(true);
        btnSaveTransaction.setEnabled(false);
        setModified(false);
    }

    private void initComponentData() {
        cboDepositProduct.setModel(observable.getCbmDepositProduct());
    }

    public void update(Observable observed, Object arg) {
        System.out.println("inside update , but no use");
    }
    ///* Auto Generated Method - getMandatoryHashMap()
    //   Getter method for setMandatoryHashMap().*/

    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    private void setMaximumLength() {
        txtTotalAmount.setMaxLength(14);
        txtResolutionNo.setMaxLength(14);
        txtRemarks.setAllowAll(true);
        txtResolutionNo.setAllowAll(true);
        txtDebitGl.setAllowAll(true);
        txtPayableGl.setAllowAll(true);
        txtTotalAmount.setValidation(new CurrencyValidation());
        txtInterestPercent.setValidation(new NumericValidation(3, 2));
        txtReserveFundPercent.setValidation(new NumericValidation(3, 2));
    }

    private void initTableData() {
        tblDepositInterestCalculation.setModel(observable.getTblDepositInterestCalculation());

    }

    private void setObservable() {
        /*
         * Singleton pattern can't be implemented as there are two observers
         * using the same observable
         */
        // The parameter '1' indicates that the customer type is INDIVIDUAL
        observable = new InterestProcessingOB(1);
        observable.addObserver(this);
    }
    /*
     * Auto Generated Method - setMandatoryHashMap()
     *
     * //ADD: implements com.see.truetransact.uimandatory.UIMandatoryField //
     * This method list out all the Input Fields available in the UI. It needs a
     * class level HashMap variable mandatoryMap.
     */

    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtDrfTransMemberNo", new Boolean(true));
        mandatoryMap.put("txtDrfTransName", new Boolean(true));
        mandatoryMap.put("txtDrfTransAmount", new Boolean(true));
        mandatoryMap.put("cboDrfTransProdID", new Boolean(true));

    }
    /*
     * Auto Generated Method - setHelpMessage() This method shows tooltip help
     * for all the input fields available in the UI. It needs the Mandatory
     * Resource Bundle object. Help display Label name should be lblMsg.
     */

    public void setHelpMessage() {
        objMandatoryRB = new InterestProcessingMRB();
    }

    /*
     * Auto Generated Method - setFieldNames() This method assigns name for all
     * the components. Other functions are working based on this name.
     */
    private void setFieldNames() {
        lblMsg.setName("lblMsg");
        lblSpace1.setName("lblSpace1");
    }

    private void internationalize() {
        resourceBundle = new InterestProcessingRB();
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
    }

    private void enableDisableReserveFund(boolean value) {
        txtReserveFundGl.setVisible(value);
        lblReserveFundGl.setVisible(value);
        btnReserveFundGl.setVisible(value);
        lblReserveFundPercent.setVisible(value);
        txtReserveFundPercent.setVisible(value);
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoDrfTransaction = new com.see.truetransact.uicomponent.CButtonGroup();
        panDepositInterestCalculationDetails = new com.see.truetransact.uicomponent.CPanel();
        tabDepositInterestCalculation = new com.see.truetransact.uicomponent.CTabbedPane();
        panDepositInterestCalculation = new com.see.truetransact.uicomponent.CPanel();
        panDrfTransList = new com.see.truetransact.uicomponent.CPanel();
        panInterestProductDetails = new com.see.truetransact.uicomponent.CPanel();
        lblDepositProduct = new com.see.truetransact.uicomponent.CLabel();
        cboDepositProduct = new com.see.truetransact.uicomponent.CComboBox();
        panProcessType = new com.see.truetransact.uicomponent.CPanel();
        lblToPeriod = new com.see.truetransact.uicomponent.CLabel();
        tdtFromPeriod = new com.see.truetransact.uicomponent.CDateField();
        lblFromPeriod = new com.see.truetransact.uicomponent.CLabel();
        tdtToPeriod = new com.see.truetransact.uicomponent.CDateField();
        panDebitGl = new com.see.truetransact.uicomponent.CPanel();
        txtDebitGl = new com.see.truetransact.uicomponent.CTextField();
        btnDebitGl = new com.see.truetransact.uicomponent.CButton();
        lblDebitGl = new com.see.truetransact.uicomponent.CLabel();
        panPayableGl = new com.see.truetransact.uicomponent.CPanel();
        txtPayableGl = new com.see.truetransact.uicomponent.CTextField();
        btnPayableGl = new com.see.truetransact.uicomponent.CButton();
        lblPayableGl = new com.see.truetransact.uicomponent.CLabel();
        lblReserveFundGl = new com.see.truetransact.uicomponent.CLabel();
        txtReserveFundGl = new com.see.truetransact.uicomponent.CTextField();
        btnReserveFundGl = new com.see.truetransact.uicomponent.CButton();
        cboSchemName = new com.see.truetransact.uicomponent.CComboBox();
        lblProdId = new com.see.truetransact.uicomponent.CLabel();
        panInterestRateDetails = new com.see.truetransact.uicomponent.CPanel();
        lblResolutionNo = new com.see.truetransact.uicomponent.CLabel();
        lblInterestPercent = new com.see.truetransact.uicomponent.CLabel();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        txtRemarks = new com.see.truetransact.uicomponent.CTextField();
        txtInterestPercent = new com.see.truetransact.uicomponent.CTextField();
        txtResolutionNo = new com.see.truetransact.uicomponent.CTextField();
        tdtResolutionDate = new com.see.truetransact.uicomponent.CDateField();
        lblResolutionDate = new com.see.truetransact.uicomponent.CLabel();
        lblReserveFundPercent = new com.see.truetransact.uicomponent.CLabel();
        txtReserveFundPercent = new com.see.truetransact.uicomponent.CTextField();
        btnCalculate = new com.see.truetransact.uicomponent.CButton();
        btnClear = new com.see.truetransact.uicomponent.CButton();
        panProcessType1 = new com.see.truetransact.uicomponent.CPanel();
        btnSaveTransaction = new com.see.truetransact.uicomponent.CButton();
        txtTotalAmount = new com.see.truetransact.uicomponent.CTextField();
        lblTotalAmount = new com.see.truetransact.uicomponent.CLabel();
        srpDepositInterestCalculation = new com.see.truetransact.uicomponent.CScrollPane();
        tblDepositInterestCalculation = new com.see.truetransact.uicomponent.CTable();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setMaximumSize(new java.awt.Dimension(800, 625));
        setMinimumSize(new java.awt.Dimension(800, 625));
        setPreferredSize(new java.awt.Dimension(800, 625));

        panDepositInterestCalculationDetails.setMaximumSize(new java.awt.Dimension(650, 520));
        panDepositInterestCalculationDetails.setMinimumSize(new java.awt.Dimension(650, 520));
        panDepositInterestCalculationDetails.setPreferredSize(new java.awt.Dimension(650, 520));
        panDepositInterestCalculationDetails.setLayout(new java.awt.GridBagLayout());

        panDepositInterestCalculation.setMinimumSize(new java.awt.Dimension(830, 313));
        panDepositInterestCalculation.setPreferredSize(new java.awt.Dimension(830, 313));
        panDepositInterestCalculation.setLayout(new java.awt.GridBagLayout());

        panDrfTransList.setMinimumSize(new java.awt.Dimension(750, 170));
        panDrfTransList.setPreferredSize(new java.awt.Dimension(750, 170));
        panDrfTransList.setLayout(new java.awt.GridBagLayout());

        panInterestProductDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Interest Calculation"));
        panInterestProductDetails.setMinimumSize(new java.awt.Dimension(400, 130));
        panInterestProductDetails.setPreferredSize(new java.awt.Dimension(400, 130));
        panInterestProductDetails.setLayout(new java.awt.GridBagLayout());

        lblDepositProduct.setText("Deposit Product");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panInterestProductDetails.add(lblDepositProduct, gridBagConstraints);

        cboDepositProduct.setMinimumSize(new java.awt.Dimension(100, 21));
        cboDepositProduct.setName("cboProfession"); // NOI18N
        cboDepositProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboDepositProductActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, -12, 1, 4);
        panInterestProductDetails.add(cboDepositProduct, gridBagConstraints);

        panProcessType.setMinimumSize(new java.awt.Dimension(380, 25));
        panProcessType.setName("panMaritalStatus"); // NOI18N
        panProcessType.setPreferredSize(new java.awt.Dimension(380, 25));
        panProcessType.setLayout(new java.awt.GridBagLayout());

        lblToPeriod.setText("To Period");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 5, 1, 4);
        panProcessType.add(lblToPeriod, gridBagConstraints);

        tdtFromPeriod.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtFromPeriodFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 15);
        panProcessType.add(tdtFromPeriod, gridBagConstraints);

        lblFromPeriod.setText("From Period");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panProcessType.add(lblFromPeriod, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 8);
        panProcessType.add(tdtToPeriod, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(10, 9, 5, 0);
        panInterestProductDetails.add(panProcessType, gridBagConstraints);

        panDebitGl.setMinimumSize(new java.awt.Dimension(130, 21));
        panDebitGl.setPreferredSize(new java.awt.Dimension(130, 21));
        panDebitGl.setLayout(new java.awt.GridBagLayout());

        txtDebitGl.setEditable(false);
        txtDebitGl.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panDebitGl.add(txtDebitGl, gridBagConstraints);

        btnDebitGl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnDebitGl.setToolTipText("From Account");
        btnDebitGl.setMinimumSize(new java.awt.Dimension(21, 21));
        btnDebitGl.setPreferredSize(new java.awt.Dimension(21, 21));
        btnDebitGl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDebitGlActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panDebitGl.add(btnDebitGl, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 0, 0);
        panInterestProductDetails.add(panDebitGl, gridBagConstraints);

        lblDebitGl.setText("Interest Debit GL");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panInterestProductDetails.add(lblDebitGl, gridBagConstraints);

        panPayableGl.setMinimumSize(new java.awt.Dimension(130, 21));
        panPayableGl.setPreferredSize(new java.awt.Dimension(130, 21));
        panPayableGl.setLayout(new java.awt.GridBagLayout());

        txtPayableGl.setEditable(false);
        txtPayableGl.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panPayableGl.add(txtPayableGl, gridBagConstraints);

        btnPayableGl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnPayableGl.setToolTipText("From Account");
        btnPayableGl.setMinimumSize(new java.awt.Dimension(21, 21));
        btnPayableGl.setPreferredSize(new java.awt.Dimension(21, 21));
        btnPayableGl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPayableGlActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPayableGl.add(btnPayableGl, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 0, 0);
        panInterestProductDetails.add(panPayableGl, gridBagConstraints);

        lblPayableGl.setText("Interest Payable GL");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panInterestProductDetails.add(lblPayableGl, gridBagConstraints);

        lblReserveFundGl.setText("Reserve Fund GL");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        panInterestProductDetails.add(lblReserveFundGl, gridBagConstraints);

        txtReserveFundGl.setEditable(false);
        txtReserveFundGl.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 15);
        panInterestProductDetails.add(txtReserveFundGl, gridBagConstraints);

        btnReserveFundGl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnReserveFundGl.setMinimumSize(new java.awt.Dimension(21, 21));
        btnReserveFundGl.setPreferredSize(new java.awt.Dimension(21, 21));
        btnReserveFundGl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReserveFundGlActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(0, 106, 0, 0);
        panInterestProductDetails.add(btnReserveFundGl, gridBagConstraints);

        cboSchemName.setMinimumSize(new java.awt.Dimension(100, 21));
        cboSchemName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboSchemNameActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panInterestProductDetails.add(cboSchemName, gridBagConstraints);

        lblProdId.setText("SchemeName");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, -50, 0, 0);
        panInterestProductDetails.add(lblProdId, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panDrfTransList.add(panInterestProductDetails, gridBagConstraints);

        panInterestRateDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Interest Calculation"));
        panInterestRateDetails.setMinimumSize(new java.awt.Dimension(350, 130));
        panInterestRateDetails.setPreferredSize(new java.awt.Dimension(350, 130));
        panInterestRateDetails.setLayout(new java.awt.GridBagLayout());

        lblResolutionNo.setText("Resolution No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panInterestRateDetails.add(lblResolutionNo, gridBagConstraints);

        lblInterestPercent.setText("Interest Percent");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panInterestRateDetails.add(lblInterestPercent, gridBagConstraints);

        lblRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panInterestRateDetails.add(lblRemarks, gridBagConstraints);

        txtRemarks.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 12, 1, 0);
        panInterestRateDetails.add(txtRemarks, gridBagConstraints);

        txtInterestPercent.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 12, 1, 0);
        panInterestRateDetails.add(txtInterestPercent, gridBagConstraints);

        txtResolutionNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 12, 1, 0);
        panInterestRateDetails.add(txtResolutionNo, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 12, 1, 8);
        panInterestRateDetails.add(tdtResolutionDate, gridBagConstraints);

        lblResolutionDate.setText("Resolution Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 5, 1, 0);
        panInterestRateDetails.add(lblResolutionDate, gridBagConstraints);

        lblReserveFundPercent.setText("Reserve Fund Percent");
        lblReserveFundPercent.setMaximumSize(new java.awt.Dimension(92, 18));
        lblReserveFundPercent.setMinimumSize(new java.awt.Dimension(92, 18));
        lblReserveFundPercent.setPreferredSize(new java.awt.Dimension(92, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 36;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panInterestRateDetails.add(lblReserveFundPercent, gridBagConstraints);

        txtReserveFundPercent.setMinimumSize(new java.awt.Dimension(100, 21));
        txtReserveFundPercent.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtReserveFundPercentFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 12, 1, 0);
        panInterestRateDetails.add(txtReserveFundPercent, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        panDrfTransList.add(panInterestRateDetails, gridBagConstraints);

        btnCalculate.setText("Load");
        btnCalculate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalculateActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 11);
        panDrfTransList.add(btnCalculate, gridBagConstraints);

        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnClear.setToolTipText("Cancel");
        btnClear.setMaximumSize(new java.awt.Dimension(51, 27));
        btnClear.setMinimumSize(new java.awt.Dimension(51, 27));
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 14, 0, 0);
        panDrfTransList.add(btnClear, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panDepositInterestCalculation.add(panDrfTransList, gridBagConstraints);

        panProcessType1.setMinimumSize(new java.awt.Dimension(380, 25));
        panProcessType1.setName("panMaritalStatus"); // NOI18N
        panProcessType1.setPreferredSize(new java.awt.Dimension(380, 25));
        panProcessType1.setLayout(new java.awt.GridBagLayout());

        btnSaveTransaction.setText("Save");
        btnSaveTransaction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveTransactionActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 73, 4, 11);
        panProcessType1.add(btnSaveTransaction, gridBagConstraints);

        txtTotalAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 12, 1, 0);
        panProcessType1.add(txtTotalAmount, gridBagConstraints);

        lblTotalAmount.setText("Total");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panProcessType1.add(lblTotalAmount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 9, 0, 0);
        panDepositInterestCalculation.add(panProcessType1, gridBagConstraints);

        srpDepositInterestCalculation.setMinimumSize(new java.awt.Dimension(750, 255));
        srpDepositInterestCalculation.setPreferredSize(new java.awt.Dimension(750, 335));

        tblDepositInterestCalculation.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Select", "Cust ID", "Account No", "Name", "Dep AmT", "Dep Date", "Mat Date", "From Date", "To Date", "Interest", "SI A/c No"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblDepositInterestCalculation.setPreferredScrollableViewportSize(new java.awt.Dimension(806, 331));
        srpDepositInterestCalculation.setViewportView(tblDepositInterestCalculation);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panDepositInterestCalculation.add(srpDepositInterestCalculation, gridBagConstraints);

        tabDepositInterestCalculation.addTab("Deposit Interest Calculation", panDepositInterestCalculation);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panDepositInterestCalculationDetails.add(tabDepositInterestCalculation, gridBagConstraints);
        tabDepositInterestCalculation.getAccessibleContext().setAccessibleName("Deposit Interest Calculation");

        getContentPane().add(panDepositInterestCalculationDetails, java.awt.BorderLayout.CENTER);

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

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        btnCancelActionPerformed();
        btnNewActionPerformed();
        btnSaveTransaction.setEnabled(false);
        btnCalculate.setEnabled(true);
        setModified(false);
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnSaveTransactionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveTransactionActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        btnSaveActionPerformed();
    }//GEN-LAST:event_btnSaveTransactionActionPerformed

    private void btnCalculateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalculateActionPerformed
        boolean status = btnCalculateActionPerformed();
        if (status) {
            cboDepositProduct.setEnabled(false);
            cboSchemName.setEnabled(false);
            txtDebitGl.setEnabled(false);
            txtPayableGl.setEnabled(false);
            txtInterestPercent.setEnabled(false);
            txtReserveFundGl.setEnabled(false);
            txtReserveFundPercent.setEnabled(false);
            txtResolutionNo.setEnabled(false);
            txtRemarks.setEnabled(false);
            btnDebitGl.setEnabled(false);
            btnPayableGl.setEnabled(false);
            btnCalculate.setEnabled(false);
            tdtFromPeriod.setEnabled(false);
            tdtToPeriod.setEnabled(false);
            tdtResolutionDate.setEnabled(false);
            btnSaveTransaction.setEnabled(true);
        } else {
            return;
        }
    }//GEN-LAST:event_btnCalculateActionPerformed

    private void tdtFromPeriodFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtFromPeriodFocusLost
        // TODO add your handling code here:
        System.out.println("is coming here now and then");
        if (tdtFromPeriod.getDateValue() != null && !tdtFromPeriod.getDateValue().equals("")) {
            if (getDepositDetails != null && getDepositDetails.size() > 0) {
                HashMap shareDetsMap = (HashMap) getDepositDetails.get(0);
                if (shareDetsMap.containsKey("INT_APPL_FREQ")) {
                    int calcFreq = (int) CommonUtil.convertObjToInt(shareDetsMap.get("INT_APPL_FREQ"));
                    Date fromPeriod = DateUtil.getDateMMDDYYYY(tdtFromPeriod.getDateValue());
                    int month = fromPeriod.getMonth();
                    int year = fromPeriod.getYear();
                    int days = fromPeriod.getDay();
                    if (calcFreq == 30) {
                        fromPeriod.setMonth(month + 1);
                    } else if (calcFreq == 90) {
                        fromPeriod.setMonth(month + 3);
                    } else if (calcFreq == 180) {
                        fromPeriod.setMonth(month + 6);
                    } else {
                        fromPeriod.setYear(year + 1);
                    }
                    System.out.println("@#$@#$@#$@#$fromPeriod" + fromPeriod);
                    tdtToPeriod.setDateValue(DateUtil.getStringDate(fromPeriod));
                }
            }
        }
    }//GEN-LAST:event_tdtFromPeriodFocusLost

    private void btnPayableGlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPayableGlActionPerformed
        // TODO add your handling code here:
        viewType = PAYABLE_GL;
        callView(PAYABLE_GL);
    }//GEN-LAST:event_btnPayableGlActionPerformed

    private void btnDebitGlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDebitGlActionPerformed
        // TODO add your handling code here:
        viewType = DEBIT_GL;
        callView(DEBIT_GL);
    }//GEN-LAST:event_btnDebitGlActionPerformed

    private void cboDepositProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboDepositProductActionPerformed
        // TODO add your handling code here:
        String behavesLike = CommonUtil.convertObjToStr(observable.getCbmDepositProduct().getKeyForSelected());
        getDepositDetails = new ArrayList();
        if (behavesLike != null && !behavesLike.equals("")) {
            HashMap shareDetailsMap = new HashMap();
            shareDetailsMap.put("BEHAVES_LIKE", behavesLike);
            System.out.println("#@$@#$@#$shareClass" + behavesLike);
            getDepositDetails = (ArrayList) ClientUtil.executeQuery("getThriftBenevolentDepsositIntPay", shareDetailsMap);
             HashMap retrieve = new HashMap();
            ArrayList keyList = new ArrayList();
            keyList.add("");
            ArrayList valList = new ArrayList();
            valList.add(""); 
            for (int i = getDepositDetails .size() - 1, j = 0; i >= 0; --i, ++j) {
                // If the result contains atleast one record
                retrieve = (HashMap) getDepositDetails .get(j);
                keyList.add(retrieve.get("PROD_ID"));
                valList.add(retrieve.get("PROD_DESC"));
            }  
            observable.setCbmSchemName(new ComboBoxModel(keyList, valList));
            cboSchemName.setModel(observable.getCbmSchemName());
            tdtToPeriod.setEnabled(false);
            if (behavesLike.equals("THRIFT")) {
                enableDisableReserveFund(false);
                txtDebitGl.setText("");
                txtPayableGl.setText("");                
            }
            if (behavesLike.equals("BENEVOLENT")) {
                enableDisableReserveFund(true);
                txtDebitGl.setText("");
                txtPayableGl.setText("");
                txtReserveFundGl.setText("");
            }
        }
    }//GEN-LAST:event_cboDepositProductActionPerformed

    private void btnReserveFundGlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReserveFundGlActionPerformed
        // TODO add your handling code here:
        viewType = RESERVE_FUND_GL;
        callView(RESERVE_FUND_GL);
    }//GEN-LAST:event_btnReserveFundGlActionPerformed

    private void txtReserveFundPercentFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtReserveFundPercentFocusLost
        // TODO add your handling code here:
        double interestPercent = CommonUtil.convertObjToDouble(txtInterestPercent.getText());
        double reserveFundPercent = CommonUtil.convertObjToDouble(txtReserveFundPercent.getText());
        if (interestPercent <= 0) {
            ClientUtil.showAlertWindow("Please enter the interest percentage");
            txtReserveFundPercent.setText("");
            return;
        }
//        if (reserveFundPercent > interestPercent) {
//            ClientUtil.showAlertWindow("Reserve fund percentage cant be greater than interest percent");
//            txtReserveFundPercent.setText("");
//            return;
//        }
    }//GEN-LAST:event_txtReserveFundPercentFocusLost

    private void cboSchemNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboSchemNameActionPerformed
        // TODO add your handling code here:
        if (cboSchemName.getSelectedIndex() > 0) {
            String intDebitHead = "";
            String intPayableHead = "";
            String intReverseHead = "";
            String prodAchd = "";
            String prodId = CommonUtil.convertObjToStr(observable.getCbmSchemName().getKeyForSelected());
            HashMap whereMap = new HashMap();
            whereMap.put("PROD_ID", prodId);
            List headLst = ClientUtil.executeQuery("getThriftBenevolentAchds", whereMap);
            if (headLst != null && headLst.size() > 0) {
                whereMap = (HashMap) headLst.get(0);
                intDebitHead = CommonUtil.convertObjToStr(whereMap.get("INT_DEBIT"));
                intPayableHead = CommonUtil.convertObjToStr(whereMap.get("INT_PAY"));
                intReverseHead = CommonUtil.convertObjToStr(whereMap.get("INT_RESERVE"));       
                prodAchd = CommonUtil.convertObjToStr(whereMap.get("ACCT_HEAD"));  
            }
            String behavesLike = CommonUtil.convertObjToStr(observable.getCbmDepositProduct().getKeyForSelected());
            if (behavesLike.equalsIgnoreCase("THRIFT")) {
                txtDebitGl.setText(intDebitHead);
                txtPayableGl.setText(prodAchd);
            }
            if (behavesLike.equalsIgnoreCase("BENEVOLENT")) {
                txtDebitGl.setText(intDebitHead);
                txtPayableGl.setText(intPayableHead);
                txtReserveFundGl.setText(intReverseHead);
            }
            
        }           
    }//GEN-LAST:event_cboSchemNameActionPerformed

    /*
     * Auto Generated Method - updateOBFields() This method called by Save
     * option of UI. It updates the OB with UI data.
     */
    public void updateOBFields() {
        observable.setTxtDebitGl(txtDebitGl.getText());
        observable.setTxtDepositInterestPercent(txtInterestPercent.getText());
        observable.setTxtPayableGl(txtPayableGl.getText());
        observable.setTxtTotalAmount(txtTotalAmount.getText());
        observable.setTxtRemarks(txtRemarks.getText());
        observable.setTxtResolutionNo(txtResolutionNo.getText());
        observable.setTxtReserveFundGl(txtReserveFundGl.getText());
        observable.setTxtReservePercent(txtReserveFundPercent.getText());
        observable.setTdtFromPeriod(tdtFromPeriod.getDateValue());
        observable.setTdtResolutionDate(tdtResolutionDate.getDateValue());
        observable.setTdtToPeriod(tdtToPeriod.getDateValue());
        observable.setcboDepositProduct(CommonUtil.convertObjToStr(observable.getCbmDepositProduct().getKeyForSelected()));
        observable.setTxtProdId(CommonUtil.convertObjToStr(observable.getCbmSchemName().getKeyForSelected()));
        observable.setScreen(this.getScreen());
    }

    private void resetUI() {
        observable.resetDividendCalcDetails();
        observable.resetDividendCalcListTable();
    }

    private void callView(int viewType) {
        observable.setStatus();
        final HashMap viewMap = new HashMap();
        ArrayList lst = new ArrayList();
        HashMap whereMap = new HashMap();
        whereMap.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
        if (viewType == DEBIT_GL || viewType == PAYABLE_GL || viewType == RESERVE_FUND_GL) {
            viewMap.put(CommonConstants.MAP_NAME, "getSelectAccountHead");
        }
        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        new ViewAll(this, viewMap).show();
    }

    public void fillData(Object obj) {
        try {
            HashMap hashMap = (HashMap) obj;
            if (viewType == DEBIT_GL) {
                txtDebitGl.setText(CommonUtil.convertObjToStr(hashMap.get("AC_HD_ID")));
            }
            if (viewType == PAYABLE_GL) {
                txtPayableGl.setText(CommonUtil.convertObjToStr(hashMap.get("AC_HD_ID")));
            }
            if (viewType == RESERVE_FUND_GL) {
                txtReserveFundGl.setText(CommonUtil.convertObjToStr(hashMap.get("AC_HD_ID")));
            }
            hashMap = new HashMap();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setModified(true);
    }

    private boolean btnCalculateActionPerformed() {
        System.out.println("inside calculate action performmed");
        boolean status = false;
        String behavesLike = "";
        if (cboDepositProduct.getSelectedItem() != null || !cboDepositProduct.getSelectedItem().equals("")) {
            behavesLike = CommonUtil.convertObjToStr(observable.getCbmDepositProduct().getKeyForSelected());
        }
         if (cboSchemName.getSelectedItem() == null || cboSchemName.getSelectedItem().equals("")) {
            ClientUtil.showAlertWindow("SchmeName Cannot be empty!!! ");
        }
        if (behavesLike.equals("BENEVOLENT")) {
            if (txtReserveFundGl.getText() == null || txtReserveFundGl.getText().equals("")) {
                ClientUtil.showAlertWindow("Reserve Fund GL Cannot be empty!!! ");
            }
            if (txtReserveFundPercent.getText() == null || txtReserveFundPercent.getText().equals("")) {
                ClientUtil.showAlertWindow("Reserve Fund Percentage should not be empty!!! ");
            }
        }
        if (cboDepositProduct.getSelectedItem() == null || cboDepositProduct.getSelectedItem().equals("")) {
            ClientUtil.showAlertWindow("Please Select Deposit Product!!! ");
        } else if (txtDebitGl.getText() == null || txtDebitGl.getText().equals("") || txtPayableGl.getText() == null || txtPayableGl.getText().equals("")) {
            ClientUtil.showAlertWindow("Debit GL and Payable GL should not be empty!!! ");
        } else if (tdtFromPeriod.getDateValue() == null || tdtFromPeriod.getDateValue().equals("") || tdtToPeriod.getDateValue() == null || tdtToPeriod.getDateValue().equals("")) {
            ClientUtil.showAlertWindow("From Date and To Date should not be empty!!! ");
        } else if (txtInterestPercent.getText() == null || txtInterestPercent.getText().equals("")) {
            ClientUtil.showAlertWindow("Interest Percentage should not be empty!!! ");
        } else if (txtResolutionNo.getText() == null || txtResolutionNo.getText().equals("")) {
            ClientUtil.showAlertWindow("Resolution No should not be empty!!! ");
        } else if (tdtResolutionDate.getDateValue() == null || tdtResolutionDate.getDateValue().equals("")) {
            ClientUtil.showAlertWindow("Resolution Date should not be empty!!! ");
        } else {
            status = true;
            updateOBFields();
            observable.insertTableData();
            tblDepositInterestCalculation.setModel(observable.getTblDepositInterestCalculation());
            int rowSize = tblDepositInterestCalculation.getRowCount();
            int columnSize = tblDepositInterestCalculation.getColumnCount();
            System.out.println("tblDepositInterestCalculation.getValueAt(row, column)" + tblDepositInterestCalculation.getValueAt(0, 0));
            System.out.println("rowSize" + rowSize + "columnSize" + columnSize);
            double totalAmt = 0.0;
            if (behavesLike.equals("THRIFT")) {
                for (int row = 0; row < tblDepositInterestCalculation.getRowCount(); row++) {
                    totalAmt += CommonUtil.convertObjToDouble(tblDepositInterestCalculation.getValueAt(row, 2));
                }
            }
            if (behavesLike.equals("BENEVOLENT")) {
                for (int row = 0; row < tblDepositInterestCalculation.getRowCount(); row++) {
                    totalAmt += CommonUtil.convertObjToDouble(tblDepositInterestCalculation.getValueAt(row, 2));
                           
                }
            }
            txtTotalAmount.setText(CommonUtil.convertObjToStr(totalAmt));
            txtTotalAmount.setEnabled(false);
        }
        return status;
    }

    private void btnSaveActionPerformed() {
        resourceBundle = new InterestProcessingRB();
        final String shareAcctMandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panInterestProductDetails);
        System.out.println("shareAcctMandatoryMessage" + shareAcctMandatoryMessage);
        StringBuffer strBMandatory = new StringBuffer();
        if (shareAcctMandatoryMessage.length() > 0) {
            strBMandatory.append(shareAcctMandatoryMessage);
        }
        String strMandatory = strBMandatory.toString();
        System.out.println("strMandatory" + strMandatory);
        if (strMandatory.length() > 0) {
            CommonMethods.displayAlert(strMandatory);
        } else if (strMandatory.length() == 0) {
            savePerformed();
            observable.setStatus();
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
            resourceBundle = null;
        } else {
            CommonMethods.displayAlert(resourceBundle.getString("saveAcctDet"));
        }
    }

    private void savePerformed() {
        try {
            updateOBFields();
            List totalList = new ArrayList();
            for (int row = 0; row < tblDepositInterestCalculation.getRowCount(); row++) {
                ArrayList tableData = new ArrayList();
                for (int column = 0; column < tblDepositInterestCalculation.getColumnCount(); column++) {
                    tableData.add(tblDepositInterestCalculation.getValueAt(row, column));                    
                }
                tableData.add(observable.getTxtProdId());
                totalList.add(tableData);
            }
            observable.setInterestDetailsList(totalList);
            observable.setResult(observable.getActionType());
            
            
            CommonUtil comm = new CommonUtil();
                    final JDialog loading = comm.addProgressBar();
                    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                        @Override
                        protected Void doInBackground() throws InterruptedException /** Execute some operation */
                        {
                            observable.doAction();              // To perform the necessary operation depending on the Action type...
                            return null;
                        }

                        @Override
                        protected void done() {
                            loading.dispose();
                        }
                    };
                    worker.execute();
                    loading.show();
                    try {
                        worker.get();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
            
            
            
            //observable.doAction();
            if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                btnClearActionPerformed(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void btnNewActionPerformed() {
        resetUI();               // to Reset all the Fields and Status in UI...
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panDepositInterestCalculationDetails, true);
        observable.resetForm();
        observable.setStatus();
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        viewType = ClientConstants.ACTIONTYPE_NEW;
        observable.setStatus();
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setModified(false);
    }

    private void btnCancelActionPerformed() {
        // TODO add your handling code here:
        setModified(false);
        observable.resetForm();
        observable.setAuthorizeStatus("");
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panDepositInterestCalculationDetails, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        resetUI();
        observable.resetForm();
        lblStatus.setText("             ");
        btnDebitGl.setEnabled(true);
        btnPayableGl.setEnabled(true);
        isFilled = false;
        enableDisableReserveFund(false);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnCalculate;
    private com.see.truetransact.uicomponent.CButton btnClear;
    private com.see.truetransact.uicomponent.CButton btnDebitGl;
    private com.see.truetransact.uicomponent.CButton btnPayableGl;
    private com.see.truetransact.uicomponent.CButton btnReserveFundGl;
    private com.see.truetransact.uicomponent.CButton btnSaveTransaction;
    private com.see.truetransact.uicomponent.CComboBox cboDepositProduct;
    private com.see.truetransact.uicomponent.CComboBox cboSchemName;
    private com.see.truetransact.uicomponent.CLabel lblDebitGl;
    private com.see.truetransact.uicomponent.CLabel lblDepositProduct;
    private com.see.truetransact.uicomponent.CLabel lblFromPeriod;
    private com.see.truetransact.uicomponent.CLabel lblInterestPercent;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblPayableGl;
    private com.see.truetransact.uicomponent.CLabel lblProdId;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
    private com.see.truetransact.uicomponent.CLabel lblReserveFundGl;
    private com.see.truetransact.uicomponent.CLabel lblReserveFundPercent;
    private com.see.truetransact.uicomponent.CLabel lblResolutionDate;
    private com.see.truetransact.uicomponent.CLabel lblResolutionNo;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblToPeriod;
    private com.see.truetransact.uicomponent.CLabel lblTotalAmount;
    private com.see.truetransact.uicomponent.CPanel panDebitGl;
    private com.see.truetransact.uicomponent.CPanel panDepositInterestCalculation;
    private com.see.truetransact.uicomponent.CPanel panDepositInterestCalculationDetails;
    private com.see.truetransact.uicomponent.CPanel panDrfTransList;
    private com.see.truetransact.uicomponent.CPanel panInterestProductDetails;
    private com.see.truetransact.uicomponent.CPanel panInterestRateDetails;
    private com.see.truetransact.uicomponent.CPanel panPayableGl;
    private com.see.truetransact.uicomponent.CPanel panProcessType;
    private com.see.truetransact.uicomponent.CPanel panProcessType1;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CButtonGroup rdoDrfTransaction;
    private com.see.truetransact.uicomponent.CScrollPane srpDepositInterestCalculation;
    private com.see.truetransact.uicomponent.CTabbedPane tabDepositInterestCalculation;
    private com.see.truetransact.uicomponent.CTable tblDepositInterestCalculation;
    private com.see.truetransact.uicomponent.CDateField tdtFromPeriod;
    private com.see.truetransact.uicomponent.CDateField tdtResolutionDate;
    private com.see.truetransact.uicomponent.CDateField tdtToPeriod;
    private com.see.truetransact.uicomponent.CTextField txtDebitGl;
    private com.see.truetransact.uicomponent.CTextField txtInterestPercent;
    private com.see.truetransact.uicomponent.CTextField txtPayableGl;
    private com.see.truetransact.uicomponent.CTextField txtRemarks;
    private com.see.truetransact.uicomponent.CTextField txtReserveFundGl;
    private com.see.truetransact.uicomponent.CTextField txtReserveFundPercent;
    private com.see.truetransact.uicomponent.CTextField txtResolutionNo;
    private com.see.truetransact.uicomponent.CTextField txtTotalAmount;
    // End of variables declaration//GEN-END:variables
}