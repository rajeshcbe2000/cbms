/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * RtgsTransactionDetailsUI.java
 *
 * Created on October 26th, 2015, 03:40 PM
 */
package com.see.truetransact.ui.remittance.rtgs;

/**
 *
 * @author Suresh R
 */
import java.util.*;
import javax.swing.*;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.common.viewall.TextUI;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.ui.customer.CheckCustomerIdUI;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.ui.termloan.loanrebate.LoanRebateUI;
import com.see.truetransact.clientutil.PrintPreview;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CTable;
import com.see.truetransact.clientutil.TableSorter;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.clientutil.ComboBoxModel;


import java.awt.Font;
import java.awt.Color;
import java.awt.Component;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import javax.swing.table.*;
import java.text.MessageFormat;
import java.awt.print.PrinterJob;

public class RtgsTransactionDetailsUI extends CInternalFrame implements Observer {

    /**
     * Vairable Declarations
     */
    private final static ClientParseException parseException = ClientParseException.getInstance();//Instance of ClientParseException to log the Exceptions
    RtgsTransactionDetailsOB observable = null;
    private boolean selectMode = false;
    private Date currDate = null;
    private HashMap returnMap = null;
    private int viewType = -1;
    private final int FROM_ACC_NUM = 1, TO_ACC_NUM = 2, FROM_UTR_NO = 3, TO_UTR_NO = 4;
    ArrayList colourList = new ArrayList();
    /**
     * Creates new form RtgsTransactionDetailsUI
     */
    public RtgsTransactionDetailsUI() {
        returnMap = null;
        currDate = ClientUtil.getCurrentDate();
        initForm();
    }

    /**
     * Method which is used to initialize the form TokenConfig
     */
    private void initForm() {
        initComponents();
        observable = new RtgsTransactionDetailsOB();
        initTableData();
        initComponentData();
        setMaxLength();
        enableDisable();
        btnPrint.setEnabled(false);
        panSelectAll.setVisible(false);
    }

    private void enableDisable() {
        ClientUtil.enableDisable(panProductDetails, true);
        //txtFromUTRNumber.setEnabled(false);
        //txtToUTRNumber.setEnabled(false);
        btnFromUTRNumber.setEnabled(true);
        btnToUTRNumber.setEnabled(true);
        txtFromAccNo.setEnabled(false);
        txtToAccNo.setEnabled(false);
        btnFromAccNo.setEnabled(true);
        btnToAccNo.setEnabled(true);
        btnDisplay.setEnabled(true);
        setSizeTableData();
    }

    private void setMaxLength() {
        txtFromUTRNumber.setAllowAll(true);
        txtToUTRNumber.setAllowAll(true);
        txtBranchCode.setAllowAll(true);
        txtFromAmount.setValidation(new CurrencyValidation(14, 2));
        txtToAmount.setValidation(new CurrencyValidation(14, 2));
    }

    private void initComponentData() {
        cboProdType.setModel(observable.getCbmProdType());
        cboFileStatus.setModel(observable.getCbmFileStatus());
    }

    private void initTableData() {
        tblRTGSDetails.setModel(observable.getTblRTGSDetails());
    }


    /*
     * Auto Generated Method - update() This method called by Observable. It
     * updates the UI with Observable's data. If needed add/Remove RadioButtons
     * method need to be added.
     */
    public void update(Observable observed, Object arg) {
    }

    /*
     * Auto Generated Method - updateOBFields() This method called by Save
     * option of UI. It updates the OB with UI data.
     */
    public void updateOBFields() {
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
    }

    /**
     * Used to set Maximum possible lenghts for TextFields
     */
    private void setMaxLengths() {
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
     * Method used to showPopup ViewAll by Executing a Query
     */
    private void popUp(int field) {
        final HashMap viewMap = new HashMap();
        final HashMap where_map = new HashMap();
        viewType = field;
        if (field == FROM_ACC_NUM || field == TO_ACC_NUM) {
            viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList" + observable.getCbmProdType().getKeyForSelected().toString());
            where_map.put("PROD_ID", observable.getCbmProdId().getKeyForSelected());
            where_map.put("SELECTED_BRANCH", ProxyParameters.BRANCH_ID);
            where_map.put("FILTERED_LIST", "");
            viewMap.put(CommonConstants.MAP_WHERE, where_map);
        } else if (field == FROM_UTR_NO || field == TO_UTR_NO) {
            viewMap.put(CommonConstants.MAP_NAME, "getUTRNumber");
            if (rdoTypeRTGS.isSelected()) {
                where_map.put("PRODUCT_ID", "RTGS");
            } else if (rdoTypeNEFT.isSelected()) {
                where_map.put("PRODUCT_ID", "NEFT");
            }
            where_map.put("SELECTED_BRANCH", ProxyParameters.BRANCH_ID);
            where_map.put("FILTERED_LIST", "");     //Added By Suresh R
            viewMap.put(CommonConstants.MAP_WHERE, where_map);
        }
        new ViewAll(this, viewMap).show();
    }

    /*
     * Fills up the HashMap with data when user selects the row in ViewAll
     * screen
     */
    public void fillData(Object map) {
        try {
            HashMap hash = (HashMap) map;
            System.out.println("#@@# Hash :" + hash);
            if (viewType == FROM_ACC_NUM) {
                txtFromAccNo.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
                txtToAccNo.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
            } else if (viewType == TO_ACC_NUM) {
                txtToAccNo.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
            } else if (viewType == FROM_UTR_NO) {
                txtFromUTRNumber.setText(CommonUtil.convertObjToStr(hash.get("UTR_NUMBER")));
            } else if (viewType == TO_UTR_NO) {
                txtToUTRNumber.setText(CommonUtil.convertObjToStr(hash.get("UTR_NUMBER")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panRTGSDetails = new com.see.truetransact.uicomponent.CPanel();
        panProductDetails = new com.see.truetransact.uicomponent.CPanel();
        panRTGSInsideActDetails = new com.see.truetransact.uicomponent.CPanel();
        cboProdId = new com.see.truetransact.uicomponent.CComboBox();
        lblFromAccNo = new com.see.truetransact.uicomponent.CLabel();
        panFromAccNo = new com.see.truetransact.uicomponent.CPanel();
        txtFromAccNo = new com.see.truetransact.uicomponent.CTextField();
        btnFromAccNo = new com.see.truetransact.uicomponent.CButton();
        lblProdType = new com.see.truetransact.uicomponent.CLabel();
        cboProdType = new com.see.truetransact.uicomponent.CComboBox();
        lblProdId = new com.see.truetransact.uicomponent.CLabel();
        lblToAccNo = new com.see.truetransact.uicomponent.CLabel();
        panToAccNo = new com.see.truetransact.uicomponent.CPanel();
        txtToAccNo = new com.see.truetransact.uicomponent.CTextField();
        btnToAccNo = new com.see.truetransact.uicomponent.CButton();
        lblToAmount = new com.see.truetransact.uicomponent.CLabel();
        txtToAmount = new com.see.truetransact.uicomponent.CTextField();
        lblFromAmount = new com.see.truetransact.uicomponent.CLabel();
        txtFromAmount = new com.see.truetransact.uicomponent.CTextField();
        lblFileStatus = new com.see.truetransact.uicomponent.CLabel();
        cboFileStatus = new com.see.truetransact.uicomponent.CComboBox();
        panRTGSInsideDetails = new com.see.truetransact.uicomponent.CPanel();
        panType = new com.see.truetransact.uicomponent.CPanel();
        rdoTypeRTGS = new com.see.truetransact.uicomponent.CRadioButton();
        rdoTypeNEFT = new com.see.truetransact.uicomponent.CRadioButton();
        lblTYpe = new com.see.truetransact.uicomponent.CLabel();
        lblToDate = new com.see.truetransact.uicomponent.CLabel();
        tdtToDate = new com.see.truetransact.uicomponent.CDateField();
        lblFromDate = new com.see.truetransact.uicomponent.CLabel();
        tdtFromDate = new com.see.truetransact.uicomponent.CDateField();
        panToUTRNumber = new com.see.truetransact.uicomponent.CPanel();
        txtToUTRNumber = new com.see.truetransact.uicomponent.CTextField();
        btnToUTRNumber = new com.see.truetransact.uicomponent.CButton();
        lblToUTRNumber = new com.see.truetransact.uicomponent.CLabel();
        panFromUTRNumber = new com.see.truetransact.uicomponent.CPanel();
        txtFromUTRNumber = new com.see.truetransact.uicomponent.CTextField();
        btnFromUTRNumber = new com.see.truetransact.uicomponent.CButton();
        lblFromUTRNumber = new com.see.truetransact.uicomponent.CLabel();
        panClearingType = new com.see.truetransact.uicomponent.CPanel();
        rdoClearingTypeInward = new com.see.truetransact.uicomponent.CRadioButton();
        rdoClearingTypeOutward = new com.see.truetransact.uicomponent.CRadioButton();
        lblBranchCode = new com.see.truetransact.uicomponent.CLabel();
        txtBranchCode = new com.see.truetransact.uicomponent.CTextField();
        btnDisplay = new com.see.truetransact.uicomponent.CButton();
        panProductTableData = new com.see.truetransact.uicomponent.CPanel();
        srpDepositInterestApplication = new com.see.truetransact.uicomponent.CScrollPane();
        tblRTGSDetails = new com.see.truetransact.uicomponent.CTable();
        panSelectAll = new com.see.truetransact.uicomponent.CPanel();
        lblSelectAll = new com.see.truetransact.uicomponent.CLabel();
        chkSelectAll = new com.see.truetransact.uicomponent.CCheckBox();
        panProcess = new com.see.truetransact.uicomponent.CPanel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        btnClear = new com.see.truetransact.uicomponent.CButton();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMaximumSize(new java.awt.Dimension(860, 635));
        setMinimumSize(new java.awt.Dimension(860, 635));
        setPreferredSize(new java.awt.Dimension(860, 635));

        panRTGSDetails.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panRTGSDetails.setMaximumSize(new java.awt.Dimension(870, 630));
        panRTGSDetails.setMinimumSize(new java.awt.Dimension(870, 630));
        panRTGSDetails.setPreferredSize(new java.awt.Dimension(870, 630));
        panRTGSDetails.setLayout(new java.awt.GridBagLayout());

        panProductDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("RTGS/NEFT Details"));
        panProductDetails.setMinimumSize(new java.awt.Dimension(850, 250));
        panProductDetails.setPreferredSize(new java.awt.Dimension(850, 250));
        panProductDetails.setLayout(new java.awt.GridBagLayout());

        panRTGSInsideActDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panRTGSInsideActDetails.setMinimumSize(new java.awt.Dimension(360, 195));
        panRTGSInsideActDetails.setPreferredSize(new java.awt.Dimension(360, 195));
        panRTGSInsideActDetails.setLayout(new java.awt.GridBagLayout());

        cboProdId.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboProdId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdId.setPopupWidth(250);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panRTGSInsideActDetails.add(cboProdId, gridBagConstraints);

        lblFromAccNo.setText("From Account No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 21, 4, 4);
        panRTGSInsideActDetails.add(lblFromAccNo, gridBagConstraints);

        panFromAccNo.setLayout(new java.awt.GridBagLayout());

        txtFromAccNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtFromAccNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFromAccNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panFromAccNo.add(txtFromAccNo, gridBagConstraints);

        btnFromAccNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnFromAccNo.setToolTipText("Select Customer");
        btnFromAccNo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnFromAccNo.setMaximumSize(new java.awt.Dimension(22, 21));
        btnFromAccNo.setMinimumSize(new java.awt.Dimension(22, 21));
        btnFromAccNo.setPreferredSize(new java.awt.Dimension(22, 21));
        btnFromAccNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFromAccNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panFromAccNo.add(btnFromAccNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panRTGSInsideActDetails.add(panFromAccNo, gridBagConstraints);

        lblProdType.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRTGSInsideActDetails.add(lblProdType, gridBagConstraints);

        cboProdType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboProdType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdType.setPopupWidth(150);
        cboProdType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panRTGSInsideActDetails.add(cboProdType, gridBagConstraints);

        lblProdId.setText("Product ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRTGSInsideActDetails.add(lblProdId, gridBagConstraints);

        lblToAccNo.setText("To Account No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 21, 4, 4);
        panRTGSInsideActDetails.add(lblToAccNo, gridBagConstraints);

        panToAccNo.setLayout(new java.awt.GridBagLayout());

        txtToAccNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtToAccNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtToAccNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panToAccNo.add(txtToAccNo, gridBagConstraints);

        btnToAccNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnToAccNo.setToolTipText("Select Customer");
        btnToAccNo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnToAccNo.setMaximumSize(new java.awt.Dimension(22, 21));
        btnToAccNo.setMinimumSize(new java.awt.Dimension(22, 21));
        btnToAccNo.setPreferredSize(new java.awt.Dimension(22, 21));
        btnToAccNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnToAccNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panToAccNo.add(btnToAccNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panRTGSInsideActDetails.add(panToAccNo, gridBagConstraints);

        lblToAmount.setText("To Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRTGSInsideActDetails.add(lblToAmount, gridBagConstraints);

        txtToAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtToAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtToAmountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRTGSInsideActDetails.add(txtToAmount, gridBagConstraints);

        lblFromAmount.setText("From Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRTGSInsideActDetails.add(lblFromAmount, gridBagConstraints);

        txtFromAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtFromAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFromAmountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRTGSInsideActDetails.add(txtFromAmount, gridBagConstraints);

        lblFileStatus.setText("File Status");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRTGSInsideActDetails.add(lblFileStatus, gridBagConstraints);

        cboFileStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboFileStatus.setMinimumSize(new java.awt.Dimension(100, 21));
        cboFileStatus.setPopupWidth(150);
        cboFileStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboFileStatusActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panRTGSInsideActDetails.add(cboFileStatus, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panProductDetails.add(panRTGSInsideActDetails, gridBagConstraints);

        panRTGSInsideDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panRTGSInsideDetails.setMinimumSize(new java.awt.Dimension(360, 195));
        panRTGSInsideDetails.setPreferredSize(new java.awt.Dimension(360, 195));
        panRTGSInsideDetails.setLayout(new java.awt.GridBagLayout());

        panType.setMaximumSize(new java.awt.Dimension(78, 23));
        panType.setMinimumSize(new java.awt.Dimension(78, 23));
        panType.setPreferredSize(new java.awt.Dimension(78, 23));
        panType.setLayout(new java.awt.GridBagLayout());

        rdoTypeRTGS.setText("RTGS");
        rdoTypeRTGS.setMaximumSize(new java.awt.Dimension(65, 27));
        rdoTypeRTGS.setMinimumSize(new java.awt.Dimension(65, 27));
        rdoTypeRTGS.setPreferredSize(new java.awt.Dimension(65, 27));
        rdoTypeRTGS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoTypeRTGSActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panType.add(rdoTypeRTGS, gridBagConstraints);

        rdoTypeNEFT.setText("NEFT");
        rdoTypeNEFT.setMargin(new java.awt.Insets(2, 5, 2, 2));
        rdoTypeNEFT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoTypeNEFTActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 20);
        panType.add(rdoTypeNEFT, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 59;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panRTGSInsideDetails.add(panType, gridBagConstraints);

        lblTYpe.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTYpe.setText("Type");
        lblTYpe.setMaximumSize(new java.awt.Dimension(150, 18));
        lblTYpe.setMinimumSize(new java.awt.Dimension(100, 18));
        lblTYpe.setName("lblTransactionID"); // NOI18N
        lblTYpe.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panRTGSInsideDetails.add(lblTYpe, gridBagConstraints);

        lblToDate.setText("To Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRTGSInsideDetails.add(lblToDate, gridBagConstraints);

        tdtToDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtToDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRTGSInsideDetails.add(tdtToDate, gridBagConstraints);

        lblFromDate.setText("From Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRTGSInsideDetails.add(lblFromDate, gridBagConstraints);

        tdtFromDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtFromDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRTGSInsideDetails.add(tdtFromDate, gridBagConstraints);

        panToUTRNumber.setLayout(new java.awt.GridBagLayout());

        txtToUTRNumber.setMinimumSize(new java.awt.Dimension(150, 21));
        txtToUTRNumber.setPreferredSize(new java.awt.Dimension(150, 21));
        txtToUTRNumber.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtToUTRNumberFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panToUTRNumber.add(txtToUTRNumber, gridBagConstraints);

        btnToUTRNumber.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnToUTRNumber.setMinimumSize(new java.awt.Dimension(21, 21));
        btnToUTRNumber.setPreferredSize(new java.awt.Dimension(21, 21));
        btnToUTRNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnToUTRNumberActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panToUTRNumber.add(btnToUTRNumber, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRTGSInsideDetails.add(panToUTRNumber, gridBagConstraints);

        lblToUTRNumber.setText("To UTR Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRTGSInsideDetails.add(lblToUTRNumber, gridBagConstraints);

        panFromUTRNumber.setLayout(new java.awt.GridBagLayout());

        txtFromUTRNumber.setMinimumSize(new java.awt.Dimension(150, 21));
        txtFromUTRNumber.setPreferredSize(new java.awt.Dimension(150, 21));
        txtFromUTRNumber.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFromUTRNumberFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panFromUTRNumber.add(txtFromUTRNumber, gridBagConstraints);

        btnFromUTRNumber.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnFromUTRNumber.setMinimumSize(new java.awt.Dimension(21, 21));
        btnFromUTRNumber.setPreferredSize(new java.awt.Dimension(21, 21));
        btnFromUTRNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFromUTRNumberActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panFromUTRNumber.add(btnFromUTRNumber, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRTGSInsideDetails.add(panFromUTRNumber, gridBagConstraints);

        lblFromUTRNumber.setText("From UTR Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRTGSInsideDetails.add(lblFromUTRNumber, gridBagConstraints);

        panClearingType.setMaximumSize(new java.awt.Dimension(140, 23));
        panClearingType.setMinimumSize(new java.awt.Dimension(140, 23));
        panClearingType.setPreferredSize(new java.awt.Dimension(140, 23));
        panClearingType.setLayout(new java.awt.GridBagLayout());

        rdoClearingTypeInward.setText("INWARD");
        rdoClearingTypeInward.setMaximumSize(new java.awt.Dimension(85, 27));
        rdoClearingTypeInward.setMinimumSize(new java.awt.Dimension(85, 27));
        rdoClearingTypeInward.setPreferredSize(new java.awt.Dimension(85, 27));
        rdoClearingTypeInward.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoClearingTypeInwardActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panClearingType.add(rdoClearingTypeInward, gridBagConstraints);

        rdoClearingTypeOutward.setText("OUTWARD");
        rdoClearingTypeOutward.setMargin(new java.awt.Insets(2, 5, 2, 2));
        rdoClearingTypeOutward.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoClearingTypeOutwardActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 20);
        panClearingType.add(rdoClearingTypeOutward, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 59;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panRTGSInsideDetails.add(panClearingType, gridBagConstraints);

        lblBranchCode.setText("Branch Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRTGSInsideDetails.add(lblBranchCode, gridBagConstraints);

        txtBranchCode.setMinimumSize(new java.awt.Dimension(100, 21));
        txtBranchCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBranchCodeFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRTGSInsideDetails.add(txtBranchCode, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panProductDetails.add(panRTGSInsideDetails, gridBagConstraints);

        btnDisplay.setForeground(new java.awt.Color(0, 102, 0));
        btnDisplay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnDisplay.setText("Display");
        btnDisplay.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        btnDisplay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDisplayActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panProductDetails.add(btnDisplay, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panRTGSDetails.add(panProductDetails, gridBagConstraints);

        panProductTableData.setBorder(javax.swing.BorderFactory.createTitledBorder("Transaction Details"));
        panProductTableData.setMinimumSize(new java.awt.Dimension(850, 250));
        panProductTableData.setPreferredSize(new java.awt.Dimension(850, 250));
        panProductTableData.setLayout(new java.awt.GridBagLayout());

        srpDepositInterestApplication.setMinimumSize(new java.awt.Dimension(840, 225));
        srpDepositInterestApplication.setPreferredSize(new java.awt.Dimension(840, 225));
        srpDepositInterestApplication.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                srpDepositInterestApplicationMouseClicked(evt);
            }
        });

        tblRTGSDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Select", "Cust ID", "Account No", "Name", "Dep AmT", "Dep Date"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblRTGSDetails.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblRTGSDetails.setMaximumSize(new java.awt.Dimension(2147483647, 64));
        tblRTGSDetails.setMinimumSize(new java.awt.Dimension(975, 0));
        tblRTGSDetails.setPreferredScrollableViewportSize(new java.awt.Dimension(806, 221));
        tblRTGSDetails.setSelectionBackground(new java.awt.Color(153, 153, 255));
        srpDepositInterestApplication.setViewportView(tblRTGSDetails);

        panProductTableData.add(srpDepositInterestApplication, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panRTGSDetails.add(panProductTableData, gridBagConstraints);

        panSelectAll.setMinimumSize(new java.awt.Dimension(102, 27));
        panSelectAll.setPreferredSize(new java.awt.Dimension(102, 27));
        panSelectAll.setLayout(new java.awt.GridBagLayout());

        lblSelectAll.setText("Select All");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 19, 5, 0);
        panSelectAll.add(lblSelectAll, gridBagConstraints);

        chkSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSelectAllActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 5, 1);
        panSelectAll.add(chkSelectAll, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panRTGSDetails.add(panSelectAll, gridBagConstraints);

        panProcess.setMinimumSize(new java.awt.Dimension(780, 30));
        panProcess.setPreferredSize(new java.awt.Dimension(780, 30));
        panProcess.setLayout(new java.awt.GridBagLayout());

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProcess.add(btnClose, gridBagConstraints);

        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProcess.add(btnClear, gridBagConstraints);

        btnPrint.setForeground(new java.awt.Color(255, 0, 51));
        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setText("Print");
        btnPrint.setToolTipText("Print");
        btnPrint.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProcess.add(btnPrint, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panRTGSDetails.add(panProcess, gridBagConstraints);

        getContentPane().add(panRTGSDetails, java.awt.BorderLayout.CENTER);

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

    private void srpDepositInterestApplicationMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_srpDepositInterestApplicationMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_srpDepositInterestApplicationMouseClicked
    private Date getProperDate(Date sourceDate) {
        Date targetDate = (Date) currDate.clone();
        targetDate.setDate(sourceDate.getDate());
        targetDate.setMonth(sourceDate.getMonth());
        targetDate.setYear(sourceDate.getYear());
        return targetDate;
    }
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        observable.resetForm();
        observable.resetTableValues();
        observable.setFinalList(null);
        ClientUtil.enableDisable(panRTGSDetails, false);
        ClientUtil.clearAll(this);
        btnPrint.setEnabled(false);
        enableDisable();
    }//GEN-LAST:event_btnClearActionPerformed

    private void chkSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSelectAllActionPerformed
        // TODO add your handling code here:
        boolean flag;
        if (chkSelectAll.isSelected() == true) {
            flag = true;
        } else {
            flag = false;
        }
        double totAmount = 0;
        for (int i = 0; i < tblRTGSDetails.getRowCount(); i++) {
            tblRTGSDetails.setValueAt(new Boolean(flag), i, 0);
            if (CommonUtil.convertObjToStr(tblRTGSDetails.getValueAt(i, 0)).equals("true")) {
                totAmount = totAmount + CommonUtil.convertObjToDouble(tblRTGSDetails.getValueAt(i, 9)).doubleValue();
            }
        }
        // lblTotalTransactionAmtVal.setText(CurrencyValidation.formatCrore(String.valueOf(totAmount)));
    }//GEN-LAST:event_chkSelectAllActionPerformed

    private Date setProperDtFormat(Date dt) {
        Date tempDt = (Date) currDate.clone();
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }

    //Added By Suresh 
    private void setSizeTableData() {
        tblRTGSDetails.getColumnModel().getColumn(0).setPreferredWidth(75);
        tblRTGSDetails.getColumnModel().getColumn(1).setPreferredWidth(110);
        tblRTGSDetails.getColumnModel().getColumn(2).setPreferredWidth(75);
        tblRTGSDetails.getColumnModel().getColumn(3).setPreferredWidth(75);
        tblRTGSDetails.getColumnModel().getColumn(4).setPreferredWidth(60);
        tblRTGSDetails.getColumnModel().getColumn(5).setPreferredWidth(110);
        tblRTGSDetails.getColumnModel().getColumn(6).setPreferredWidth(110);
        tblRTGSDetails.getColumnModel().getColumn(7).setPreferredWidth(260);
        tblRTGSDetails.getColumnModel().getColumn(8).setPreferredWidth(90);
        tblRTGSDetails.getColumnModel().getColumn(9).setPreferredWidth(120);
        tblRTGSDetails.getColumnModel().getColumn(10).setPreferredWidth(120);
        tblRTGSDetails.getColumnModel().getColumn(11).setPreferredWidth(110);
        tblRTGSDetails.getColumnModel().getColumn(12).setPreferredWidth(120);
        tblRTGSDetails.getColumnModel().getColumn(13).setPreferredWidth(110);
        tblRTGSDetails.getColumnModel().getColumn(14).setPreferredWidth(110);
        tblRTGSDetails.getColumnModel().getColumn(15).setPreferredWidth(75);
        tblRTGSDetails.getColumnModel().getColumn(16).setPreferredWidth(100);
        tblRTGSDetails.getColumnModel().getColumn(17).setPreferredWidth(150);
        tblRTGSDetails.getColumnModel().getColumn(18).setPreferredWidth(150);
        tblRTGSDetails.getColumnModel().getColumn(19).setPreferredWidth(150);
        tblRTGSDetails.getColumnModel().getColumn(20).setPreferredWidth(150);
        tblRTGSDetails.getColumnModel().getColumn(21).setPreferredWidth(150);
    }

    private void setRightAlignment(int col) {
        javax.swing.table.DefaultTableCellRenderer r = new javax.swing.table.DefaultTableCellRenderer();
        r.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        tblRTGSDetails.getColumnModel().getColumn(col).setCellRenderer(r);
        tblRTGSDetails.getColumnModel().getColumn(col).sizeWidthToFit();
    }

    private void rdoTypeRTGSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoTypeRTGSActionPerformed
        // TODO add your handling code here:
        rdoTypeRTGS.setSelected(true);
        rdoTypeNEFT.setSelected(false);
    }//GEN-LAST:event_rdoTypeRTGSActionPerformed

    private void rdoTypeNEFTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoTypeNEFTActionPerformed
        // TODO add your handling code here:
        rdoTypeNEFT.setSelected(true);
        rdoTypeRTGS.setSelected(false);
    }//GEN-LAST:event_rdoTypeNEFTActionPerformed

    private void tdtToDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtToDateFocusLost
        // TODO add your handling code here:
        checkDateValidation();
}//GEN-LAST:event_tdtToDateFocusLost
    private void checkDateValidation() {
        if (tdtFromDate.getDateValue().length() > 0 && tdtToDate.getDateValue().length() > 0) {
            java.util.Date fromDate = (java.util.Date) DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue());
            java.util.Date toDate = (java.util.Date) DateUtil.getDateMMDDYYYY(tdtToDate.getDateValue());
            if (DateUtil.dateDiff(toDate, fromDate) > 0) {
                ClientUtil.showMessageWindow("To Date Should not be Greater than From Date !!!");
                tdtToDate.setDateValue("");
                return;
            }
        }
    }

    private void checkAmountValidation() {
        if (txtFromAmount.getText().length() > 0 && txtToAmount.getText().length() > 0) {
            if (CommonUtil.convertObjToDouble(txtFromAmount.getText()) - CommonUtil.convertObjToDouble(txtToAmount.getText()) > 0) {
                ClientUtil.showMessageWindow("To Amount Should not be Greater than From Amount !!!");
                txtToAmount.setText("");
                return;
            }
        }
    }
    private void tdtFromDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtFromDateFocusLost
        // TODO add your handling code here:
        checkDateValidation();
}//GEN-LAST:event_tdtFromDateFocusLost

    private void txtToUTRNumberFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtToUTRNumberFocusLost
        // TODO add your handling code here:
        if (rdoTypeRTGS.isSelected() == true || rdoTypeNEFT.isSelected() == true) { //Added By Suresh R
            if (txtToUTRNumber.getText().length() > 0) {
                HashMap whereMap = new HashMap();
                whereMap.put("UTR_NO", txtToUTRNumber.getText());
                if (rdoTypeRTGS.isSelected()) {
                    whereMap.put("PRODUCT_ID", "RTGS");
                } else if (rdoTypeNEFT.isSelected()) {
                    whereMap.put("PRODUCT_ID", "NEFT");
                }
                List UTRList = ClientUtil.executeQuery("getUTRNumber", whereMap);
                if (UTRList != null && UTRList.size() > 0) {
                } else {
                    ClientUtil.showMessageWindow("Invalid UTR Number !!! ");
                    txtToUTRNumber.setText("");
                    return;
                }
            }
        } else {
            ClientUtil.showMessageWindow("Please Select RTGS/NEFT !!!");
            txtToUTRNumber.setText("");
            return;
        }
    }//GEN-LAST:event_txtToUTRNumberFocusLost

    private void btnToUTRNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnToUTRNumberActionPerformed
        // TODO add your handling code here:
        if (rdoTypeRTGS.isSelected() == true || rdoTypeNEFT.isSelected() == true) {
            popUp(TO_UTR_NO);
        } else {
            ClientUtil.showMessageWindow("Please Select RTGS/NEFT !!!");
            return;
        }
    }//GEN-LAST:event_btnToUTRNumberActionPerformed

    private void txtFromUTRNumberFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFromUTRNumberFocusLost
        // TODO add your handling code here:
        if (rdoTypeRTGS.isSelected() == true || rdoTypeNEFT.isSelected() == true) { //Added By Suresh R
            if (txtFromUTRNumber.getText().length() > 0) {
                HashMap whereMap = new HashMap();
                whereMap.put("UTR_NO", txtFromUTRNumber.getText());
                if (rdoTypeRTGS.isSelected()) {
                    whereMap.put("PRODUCT_ID", "RTGS");
                } else if (rdoTypeNEFT.isSelected()) {
                    whereMap.put("PRODUCT_ID", "NEFT");
                }
                List UTRList = ClientUtil.executeQuery("getUTRNumber", whereMap);
                if (UTRList != null && UTRList.size() > 0) {
                } else {
                    ClientUtil.showMessageWindow("Invalid UTR Number !!! ");
                    txtFromUTRNumber.setText("");
                    return;
                }
            }
        } else {
            ClientUtil.showMessageWindow("Please Select RTGS/NEFT !!!");
            txtFromUTRNumber.setText("");
            return;
        }
    }//GEN-LAST:event_txtFromUTRNumberFocusLost

    private void btnFromUTRNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFromUTRNumberActionPerformed
        // TODO add your handling code here:
        if (rdoTypeRTGS.isSelected() == true || rdoTypeNEFT.isSelected() == true) {
            popUp(FROM_UTR_NO);
        } else {
            ClientUtil.showMessageWindow("Please Select RTGS/NEFT !!!");
            return;
        }
    }//GEN-LAST:event_btnFromUTRNumberActionPerformed

    private void txtFromAccNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFromAccNoFocusLost
        // TODO add your handling code here:
}//GEN-LAST:event_txtFromAccNoFocusLost

    private void btnFromAccNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFromAccNoActionPerformed
        // TODO add your handling code here:
        if (cboProdType.getSelectedIndex() > 0) {
            if (cboProdId.getSelectedIndex() > 0) {
                popUp(FROM_ACC_NUM);
            } else {
                ClientUtil.showMessageWindow("Please Select Product ID !!!");
                return;
            }
        } else {
            ClientUtil.showMessageWindow("Please Select Product Type !!!");
            return;
        }
}//GEN-LAST:event_btnFromAccNoActionPerformed

    private void cboProdTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdTypeActionPerformed
        // TODO add your handling code here:
        if (cboProdType.getSelectedIndex() > 0) {
            populateProdId();
        }
}//GEN-LAST:event_cboProdTypeActionPerformed
    private void populateProdId() {
        String prodType = CommonUtil.convertObjToStr(((ComboBoxModel) cboProdType.getModel()).getKeyForSelected());
        observable.setCbmProdId(prodType);
        cboProdId.setModel(observable.getCbmProdId());
    }
    private void txtToAccNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtToAccNoFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtToAccNoFocusLost

    private void btnToAccNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnToAccNoActionPerformed
        // TODO add your handling code here:
        if (cboProdType.getSelectedIndex() > 0) {
            if (cboProdId.getSelectedIndex() > 0) {
                popUp(TO_ACC_NUM);
            } else {
                ClientUtil.showMessageWindow("Please Select Product ID !!!");
                return;
            }
        } else {
            ClientUtil.showMessageWindow("Please Select Product Type !!!");
            return;
        }
    }//GEN-LAST:event_btnToAccNoActionPerformed

    private void btnDisplayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDisplayActionPerformed
        // TODO add your handling code here:
        if (rdoTypeRTGS.isSelected() == true || rdoTypeNEFT.isSelected() == true) {
            if (rdoClearingTypeInward.isSelected() == true || rdoClearingTypeOutward.isSelected() == true) {
                if (tdtFromDate.getDateValue().length() > 0) {
                    if (tdtToDate.getDateValue().length() > 0) {
                        HashMap whereMap = new HashMap();
                        if (rdoTypeRTGS.isSelected()) {
                            whereMap.put("TYPE", "RTGS");
                        } else if (rdoTypeNEFT.isSelected()) {
                            whereMap.put("TYPE", "NEFT");
                        }
                        if (rdoClearingTypeInward.isSelected()) {
                            whereMap.put("CLEARING_TYPE", "INWARD");
                        } else if (rdoClearingTypeOutward.isSelected()) {
                            whereMap.put("CLEARING_TYPE", "OUTWARD");
                        }
                        if (tdtFromDate.getDateValue().length() > 0) {
                            java.util.Date fromDate = (java.util.Date) DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue());
                            whereMap.put("FROM_DATE", setProperDtFormat(fromDate));
                        }
                        if (tdtToDate.getDateValue().length() > 0) {
                            java.util.Date toDate = (java.util.Date) DateUtil.getDateMMDDYYYY(tdtToDate.getDateValue());
                            whereMap.put("TO_DATE", setProperDtFormat(toDate));
                        }
                        if (txtFromUTRNumber.getText().length() > 0) {
                            whereMap.put("FROM_UTR_NO", txtFromUTRNumber.getText());
                        }
                        if (txtToUTRNumber.getText().length() > 0) {
                            whereMap.put("TO_UTR_NO", txtToUTRNumber.getText());
                        }
                        if (txtFromAccNo.getText().length() > 0) {
                            whereMap.put("FROM_ACT_NO", txtFromAccNo.getText());
                        }
                        if (txtToAccNo.getText().length() > 0) {
                            whereMap.put("TO_ACT_NO", txtToAccNo.getText());
                        }
                        if (txtFromAmount.getText().length() > 0) {
                            whereMap.put("FROM_AMOUNT", CommonUtil.convertObjToDouble(txtFromAmount.getText()));
                        }
                        if (txtToAmount.getText().length() > 0) {
                            whereMap.put("TO_AMOUNT", CommonUtil.convertObjToDouble(txtToAmount.getText()));
                        }
                        if (cboFileStatus.getSelectedIndex()>0) {
                            whereMap.put("FILE_STATUS", CommonUtil.convertObjToStr(cboFileStatus.getSelectedItem()));
                        }
                        if (txtBranchCode.getText().length() > 0) {
                            whereMap.put("BRANCH_CODE", txtBranchCode.getText());
                        }
                        observable.resetTableValues();
                        observable.setFinalList(null);
                        observable.displayTableData(whereMap);
                        tableDataAlignment();
                        setSizeTableData();
                        tblRTGSDetails.revalidate();
                        setColorList();     //Added BY Suresh R
                        setColour();
                        btnPrint.setEnabled(false);
                        if (tblRTGSDetails.getRowCount() > 0) {
                            ClientUtil.enableDisable(panProductDetails, false);
                            btnFromUTRNumber.setEnabled(false);
                            btnToUTRNumber.setEnabled(false);
                            btnFromAccNo.setEnabled(false);
                            btnToAccNo.setEnabled(false);
                            btnDisplay.setEnabled(false);
                            btnPrint.setEnabled(true);
                            setRightAlignment(8);
                            setRightAlignment(9);
                            setRightAlignment(10);
                        }
                    } else {
                        ClientUtil.showMessageWindow("Please Select To Date !!!!");
                        return;
                    }
                } else {
                    ClientUtil.showMessageWindow("Please Select From Date !!!");
                    return;
                }
            } else {
                ClientUtil.showMessageWindow("Please Select Inward/Outward !!!");
                return;
            }
        } else {
            ClientUtil.showMessageWindow("Please Select RTGS/NEFT !!!");
            return;
        }
    }//GEN-LAST:event_btnDisplayActionPerformed
    private void tableDataAlignment() {
        ArrayList _heading = new ArrayList();
        List tabList = observable.getFinalList();
        if (tabList != null && tabList.size() > 0) {
            TableSorter tableSorter = new TableSorter();
            tableSorter.addMouseListenerToHeaderInTable(tblRTGSDetails);
            TableModel tableModel = new TableModel();
            _heading.add("TYPE");
            _heading.add("RTGS_ID");
            _heading.add("BATCH_ID");
            _heading.add("BATCH_DT");
            _heading.add("PROD_ID");
            _heading.add("SENDER IFSC_CODE");
            _heading.add("SENDER ACT_NO");
            _heading.add("SENDER NAME");
            _heading.add("AMOUNT");
            _heading.add("BENEF_BANK_CODE");
            _heading.add("BENEF_BRANCH_CODE");
            _heading.add("BENEF_IFSC_CODE");
            _heading.add("BENEF_ACT_NO");
            _heading.add("BENEFICIARY_NAME");
            _heading.add("UTR NUMBER");
            _heading.add("SEQ NUMBER");
            _heading.add("PROCESS_STATUS");
            _heading.add("STATUS_DATE");
            _heading.add("INWARD_FAIL_ACK_UTR");
            _heading.add("F27_STATUS_DT");
            _heading.add("N09_STATUS_DT");
            _heading.add("N10_STATUS_DT");
            tableModel.setHeading(_heading);
            tableModel.setData((ArrayList) tabList);
            tableSorter.setModel(tableModel);
            tableSorter.fireTableDataChanged();
            tblRTGSDetails.setAutoResizeMode(0);
            tblRTGSDetails.doLayout();
            tblRTGSDetails.setModel(tableSorter);
            tblRTGSDetails.revalidate();
        }
    }
    
    private void setColorList() {       //Added By Suresh R
        if (tblRTGSDetails.getRowCount() > 0) {
            colourList = new ArrayList();
            for (int i = 0; i < tblRTGSDetails.getRowCount(); i++) {
                if (CommonUtil.convertObjToStr(tblRTGSDetails.getValueAt(i, 16)).equals("FAIL")) {
                    colourList.add(String.valueOf(i));
                }
            }
        }
        //System.out.println("############ colourList : "+colourList);
    }
    
    private void setColour() {      //Added By Suresh  R
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (colourList.contains(String.valueOf(row))) {
                    setForeground(Color.RED);
                } else {
                    setForeground(Color.BLACK);
                }
                // Set oquae
                this.setOpaque(true);
                return this;
            }
        };
        tblRTGSDetails.setDefaultRenderer(Object.class, renderer);
    }
    
    private void txtToAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtToAmountFocusLost
        // Add your handling code here:
        checkAmountValidation();
}//GEN-LAST:event_txtToAmountFocusLost

    private void txtFromAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFromAmountFocusLost
        // TODO add your handling code here:
        checkAmountValidation();
    }//GEN-LAST:event_txtFromAmountFocusLost

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
        if (tblRTGSDetails.getRowCount()>0) {
            HashMap paramMap = new HashMap();
            TTIntegration ttIntgration = null;
            if (rdoTypeRTGS.isSelected()) {
                paramMap.put("Type", "RTGS");
            } else if (rdoTypeNEFT.isSelected()) {
                paramMap.put("Type", "NEFT");
            }
            if (tdtFromDate.getDateValue().length() > 0) {
                java.util.Date fromDate = (java.util.Date) DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue());
                paramMap.put("FromDate", setProperDtFormat(fromDate));
            }
            if (tdtToDate.getDateValue().length() > 0) {
                java.util.Date toDate = (java.util.Date) DateUtil.getDateMMDDYYYY(tdtToDate.getDateValue());
                paramMap.put("ToDate", setProperDtFormat(toDate));
            }
            if (txtFromUTRNumber.getText().length() > 0) {
                paramMap.put("FromUtrNo", txtFromUTRNumber.getText());
            }
            if (txtToUTRNumber.getText().length() > 0) {
                paramMap.put("ToUtrNo", txtToUTRNumber.getText());
            }
            if (txtFromAccNo.getText().length() > 0) {
                paramMap.put("FromActNo", txtFromAccNo.getText());
            }
            if (txtToAccNo.getText().length() > 0) {
                paramMap.put("ToActNo", txtToAccNo.getText());
            }
            if (txtFromAmount.getText().length() > 0) {
                paramMap.put("FromAmount", txtFromAmount.getText());
            }
            if (txtToAmount.getText().length() > 0) {
                paramMap.put("ToAmount", txtToAmount.getText());
            }
            if (cboFileStatus.getSelectedIndex()>0) {
                paramMap.put("FileStatus", CommonUtil.convertObjToStr(cboFileStatus.getSelectedItem()));
            }
            if (txtBranchCode.getText().length() > 0) {
                paramMap.put("BranchCode", txtBranchCode.getText());
            }
            ttIntgration.setParam(paramMap);
            ttIntgration.integrationForPrint("RTGS_Transaction_details");
            ClientUtil.clearAll(this);
        } else {
            ClientUtil.showMessageWindow("No Record in Table!!!");
        }
    }//GEN-LAST:event_btnPrintActionPerformed

    private void rdoClearingTypeInwardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoClearingTypeInwardActionPerformed
        // TODO add your handling code here:
        rdoClearingTypeInward.setSelected(true);
        rdoClearingTypeOutward.setSelected(false);
    }//GEN-LAST:event_rdoClearingTypeInwardActionPerformed

    private void rdoClearingTypeOutwardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoClearingTypeOutwardActionPerformed
        // TODO add your handling code here:
        rdoClearingTypeInward.setSelected(false);
        rdoClearingTypeOutward.setSelected(true);
    }//GEN-LAST:event_rdoClearingTypeOutwardActionPerformed

    private void cboFileStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboFileStatusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboFileStatusActionPerformed

    private void txtBranchCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBranchCodeFocusLost
        // TODO add your handling code here:
        if (txtBranchCode.getText().length() > 0) {
            HashMap whereMap = new HashMap();
            whereMap.put("BRANCH_CODE", txtBranchCode.getText());
            List branchLst = ClientUtil.executeQuery("getBranchNamePayRoll", whereMap);
            if (!(branchLst != null && branchLst.size() > 0)) {
                ClientUtil.showMessageWindow("Invalid Branch Code !!!");
                txtBranchCode.setText("");
                return;
            }
        }
    }//GEN-LAST:event_txtBranchCodeFocusLost

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnClear;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDisplay;
    private com.see.truetransact.uicomponent.CButton btnFromAccNo;
    private com.see.truetransact.uicomponent.CButton btnFromUTRNumber;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnToAccNo;
    private com.see.truetransact.uicomponent.CButton btnToUTRNumber;
    private com.see.truetransact.uicomponent.CComboBox cboFileStatus;
    private com.see.truetransact.uicomponent.CComboBox cboProdId;
    private com.see.truetransact.uicomponent.CComboBox cboProdType;
    private com.see.truetransact.uicomponent.CCheckBox chkSelectAll;
    private com.see.truetransact.uicomponent.CLabel lblBranchCode;
    private com.see.truetransact.uicomponent.CLabel lblFileStatus;
    private com.see.truetransact.uicomponent.CLabel lblFromAccNo;
    private com.see.truetransact.uicomponent.CLabel lblFromAmount;
    private com.see.truetransact.uicomponent.CLabel lblFromDate;
    private com.see.truetransact.uicomponent.CLabel lblFromUTRNumber;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblProdId;
    private com.see.truetransact.uicomponent.CLabel lblProdType;
    private com.see.truetransact.uicomponent.CLabel lblSelectAll;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTYpe;
    private com.see.truetransact.uicomponent.CLabel lblToAccNo;
    private com.see.truetransact.uicomponent.CLabel lblToAmount;
    private com.see.truetransact.uicomponent.CLabel lblToDate;
    private com.see.truetransact.uicomponent.CLabel lblToUTRNumber;
    private com.see.truetransact.uicomponent.CPanel panClearingType;
    private com.see.truetransact.uicomponent.CPanel panFromAccNo;
    private com.see.truetransact.uicomponent.CPanel panFromUTRNumber;
    private com.see.truetransact.uicomponent.CPanel panProcess;
    private com.see.truetransact.uicomponent.CPanel panProductDetails;
    private com.see.truetransact.uicomponent.CPanel panProductTableData;
    private com.see.truetransact.uicomponent.CPanel panRTGSDetails;
    private com.see.truetransact.uicomponent.CPanel panRTGSInsideActDetails;
    private com.see.truetransact.uicomponent.CPanel panRTGSInsideDetails;
    private com.see.truetransact.uicomponent.CPanel panSelectAll;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panToAccNo;
    private com.see.truetransact.uicomponent.CPanel panToUTRNumber;
    private com.see.truetransact.uicomponent.CPanel panType;
    private com.see.truetransact.uicomponent.CRadioButton rdoClearingTypeInward;
    private com.see.truetransact.uicomponent.CRadioButton rdoClearingTypeOutward;
    private com.see.truetransact.uicomponent.CRadioButton rdoTypeNEFT;
    private com.see.truetransact.uicomponent.CRadioButton rdoTypeRTGS;
    private com.see.truetransact.uicomponent.CScrollPane srpDepositInterestApplication;
    private com.see.truetransact.uicomponent.CTable tblRTGSDetails;
    private com.see.truetransact.uicomponent.CDateField tdtFromDate;
    private com.see.truetransact.uicomponent.CDateField tdtToDate;
    private com.see.truetransact.uicomponent.CTextField txtBranchCode;
    private com.see.truetransact.uicomponent.CTextField txtFromAccNo;
    private com.see.truetransact.uicomponent.CTextField txtFromAmount;
    private com.see.truetransact.uicomponent.CTextField txtFromUTRNumber;
    private com.see.truetransact.uicomponent.CTextField txtToAccNo;
    private com.see.truetransact.uicomponent.CTextField txtToAmount;
    private com.see.truetransact.uicomponent.CTextField txtToUTRNumber;
    // End of variables declaration//GEN-END:variables

    public static void main(String[] args) {
        RtgsTransactionDetailsUI fad = new RtgsTransactionDetailsUI();
        javax.swing.JFrame j = new javax.swing.JFrame();
        j.getContentPane().add(fad);
        j.show();
        fad.show();
    }
}
