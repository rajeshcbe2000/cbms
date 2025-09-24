/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 *
 * Authorize.java
 *
 * Created on March 3, 2004, 1:46 PM
 */
package com.see.truetransact.ui.termloan.riskfund;

import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.ui.termloan.notices.*;
import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.Observable;

import java.awt.Point;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.event.ListSelectionListener;
//import javax.swing.DefaultListModel;

import org.apache.log4j.Logger;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.EnhancedComboBoxModel;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.ui.common.viewall.ViewAll;

import com.see.truetransact.ui.common.viewall.TableDialogUI;
import com.see.truetransact.ui.TrueTransactMain;
import java.text.*;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.commonutil.TTException;
import javax.swing.DefaultCellEditor;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

/**
 * @author balachandar
 */
public class RiskFundUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer, ListSelectionListener {
//    private final AuthorizeRB resourceBundle = new AuthorizeRB();

    private RiskFundOB observable;
    HashMap paramMap = null;
    CInternalFrame parent = null;
    javax.swing.JList lstSearch;
    java.util.ArrayList arrLst = new java.util.ArrayList();
    Date currDt = null;
    TTIntegration ttIntegration = null;
    int previousRow = -1;
    HashMap accountNumberMap = null;
    HashMap guarantorMemberMap = null;
    HashMap accountChargeMap = null;
    HashMap guarantorChargeMap = null;
    private HashMap returnMap = null;
    String bankName = "";
    boolean generateNotice = false;
    int viewType = 0;
    int FROMACTNO = 1, TOACTNO = 3;
    private boolean selectMode = false;
    final int ACCTHDID = 4, ACCNO = 2;
    ArrayList lstTotRisk = new ArrayList();
    //Date d1="01-05-13";
    private final static Logger log = Logger.getLogger(LoanNoticeUI.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private TableModelListener tableModelListener;
    

    /**
     * Creates new form AuthorizeUI
     */
    public RiskFundUI() {
        setupInit();
        setupScreen();
    }

    /**
     * Creates new form AuthorizeUI
     */
    public RiskFundUI(CInternalFrame parent, HashMap paramMap) {
        this.parent = parent;
        this.paramMap = paramMap;
        setupInit();
        setupScreen();
    }

    private void setupInit() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        internationalize();
        setObservable();
        createCboProdType();
        createCboChargeTyp();
        creatComboBranch();
        createCboProdTypeCr();
        chkSelectAll.setEnabled(false);
//        txtNoOfInstallments.setValidation(new com.see.truetransact.uivalidation.NumericValidation());
//        txtNoOfInstallments.setMaxLength(3);
//        txtLastNoticeSentBefore.setValidation(new com.see.truetransact.uivalidation.NumericValidation());
//        txtLastNoticeSentBefore.setMaxLength(3);

        bankName = ((String) TrueTransactMain.BANKINFO.get("BANK_NAME")).toUpperCase();
//        txtNoticeCharge.setAllowAll(true);
//        txtPostageCharge.setAllowAll(true);
//        txtFromAccountno.setAllowAll(true);
//        txtTOAccountno.setAllowAll(true);
//        btnsms.setVisible(false);
        if (bankName.lastIndexOf("MAHILA") != -1) {
//            lblFromDate.setVisible(false);
//            tdtFromDate.setVisible(false);
//            lblToDate.setVisible(false);
//            tdtToDate.setVisible(false);
//            lblNoticeCharge.setVisible(false);
//            txtNoticeCharge.setVisible(false);
//            lblPostageCharge.setVisible(false);
//            txtPostageCharge.setVisible(false);
//            txtNoOfInstallments.setVisible(false);
//            lblNoOfInstallments.setVisible(false);
//            chkFulldue.setVisible(false);
//            btnPostCharges.setVisible(false);
            btnPostCharges.setText("Insert Charge Details");
        } else {
//            lblFromDate.setVisible(true);
//            tdtFromDate.setVisible(true);
//            lblToDate.setVisible(true);
//            tdtToDate.setVisible(true);
//            lblNoticeCharge.setVisible(true);
//            txtNoticeCharge.setVisible(true);
//            lblPostageCharge.setVisible(true);
//            txtPostageCharge.setVisible(true);
//            txtNoOfInstallments.setVisible(true);
//            lblNoOfInstallments.setVisible(true);
//            chkFulldue.setVisible(true);
//            chkPrized.setVisible(false);
//            chkNonPrized.setVisible(false);
//            btnPostCharges.setVisible(true);
        }
//        cboProdId.setModel(observable.getCbmProdId());
//        populateData(paramMap);
//        panMultiSearch.setVisible(false);
//        cboAddFind.setVisible(true);
//        btnRealize.setVisible(false);
//        btnAuthorize.setVisible(false);
//        btnException.setVisible(false);
//        btnReject.setVisible(false);
//        if (!paramMap.containsKey("MULTISELECT"))
//            chkSelectAll.setVisible(false);
        txtChrgAmt.setEnabled(false);
    }

    private void createCboProdTypeCr() {
        cboProdTypeCr.setModel(observable.getCbmProdTypCr());
    }

    private void setupScreen() {
//        setModal(true);

        /* Calculate the screen size */
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        /* Center frame on the screen */
        Dimension frameSize = this.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }

    private void setObservable() {
        try {
            observable = new RiskFundOB();
            observable.addObserver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void populateData(HashMap mapID) {
//        try {
//            log.info("populateData...");
//            ArrayList heading = observable.populateData(mapID, tblData);
//            if (heading != null && heading.size() > 0) {
//                EnhancedComboBoxModel cboModel = new EnhancedComboBoxModel(heading);
////                cboSearchCol.setModel(cboModel);
//            }
//        } catch (Exception e) {
//            System.err.println("Exception " + e.toString() + "Caught");
//            e.printStackTrace();
//        }
    }

//    public void show() {
//        if (observable.isAvailable()) {
//            super.show();
//        } else {
//            if (parent != null) parent.setModified(false);
//            ClientUtil.noDataAlert();
//        }
//    }
//    public void setVisible(boolean visible) {
//        if (observable.isAvailable()) {
//            super.setVisible(visible);
//        }
//    }
    /**
     * Bring up and populate the temporary project detail screen.
     */
    private void whenTableRowSelected() {
        int rowIndexSelected = tblData.getSelectedRow();
        if (previousRow != -1) {
            if (((Boolean) tblData.getValueAt(previousRow, 0)).booleanValue()) {
//                int guarantorRowIndexSelected = tblGuarantorData.getSelectedRow();
                if (accountNumberMap == null) {
                    accountNumberMap = new HashMap();
                }
                if (guarantorMemberMap == null) {
                    guarantorMemberMap = new HashMap();
                }
                if (previousRow != -1 && previousRow != rowIndexSelected) {
//                    isSelectedRowTicked(tblGuarantorData);
                }
            } else {
//                observable.setSelectAll(tblGuarantorData, new Boolean(false));
            }
        }
        //Changed By Suresh
//        String prodType = String.valueOf(cboProdType.getSelectedItem());
//        if (prodType.equals("MDS")) {
//            observable.populateGuarantorTable(String.valueOf(tblData.getValueAt(rowIndexSelected, 2)), tblGuarantorData);
//        } else {
//            observable.populateGuarantorTable(String.valueOf(tblData.getValueAt(rowIndexSelected, 1)), tblGuarantorData);
//        }
//        previousRow = rowIndexSelected;
    }

    private void whenGuarantorTableRowSelected() {
        int rowIndexSelected = tblData.getSelectedRow();
        if (!((Boolean) tblData.getValueAt(rowIndexSelected, 0)).booleanValue()) {
//            if (isSelectedRowTicked(tblGuarantorData)) {
//                ClientUtil.displayAlert("Loanee Record not selected...");
//                observable.setSelectAll(tblGuarantorData, new Boolean(false));
//            }
        }
    }

    private boolean isSelectedRowTicked(com.see.truetransact.uicomponent.CTable table) {
        boolean selected = false;
        for (int i = 0, j = table.getRowCount(); i < j; i++) {
            selected = ((Boolean) table.getValueAt(i, 0)).booleanValue();
            if (selected) {
                //                if (isGuarantor) {
                //                    guarantorMemberMap.put(table.getValueAt(i, 3),null);
                //                } else {
                //                    accountNumberMap.put(table.getValueAt(i, 1),null);
                //                }
                break;
            }
        }
        return selected;
    }
    
    public void calcCharge() {
        double totInvAmt = 0;
        if (tblData.getRowCount() > 0) {
            for (int i = 0; i < tblData.getRowCount(); i++) {
                //System.out.println("trueeeeeeeeeeeeeeeeeeeeeee"+tblData.getValueAt(i, 0));
                if (CommonUtil.convertObjToStr(tblData.getValueAt(i, 0)).equals("true")) {
                    //System.out.println("trueeeeeeeeeeeeeeeeeeeeeee");
                    totInvAmt = totInvAmt + CommonUtil.convertObjToDouble(tblData.getValueAt(i, 7).toString()).doubleValue();
                lblTotalTransactionAmtVal.setText(String.valueOf(totInvAmt));
                }
            }

        }
        //System.out.println("lblTotalTransactionAmtVal.getText()###" + lblTotalTransactionAmtVal.getText());        
    }

    public void calcRiskFund() {
        double totInvAmt = 0;
        if (tblData.getRowCount() > 0) {
            for (int i = 0; i < tblData.getRowCount(); i++) {
                if (((Boolean) tblData.getValueAt(i, 0)).booleanValue()) {
                    //System.out.println("trueeeeeeeeeeeeeeeeeeeeeee");
                totInvAmt = totInvAmt + CommonUtil.convertObjToDouble(tblData.getValueAt(i, 6).toString()).doubleValue();
                lblTotalTransactionAmtVal.setText(String.valueOf(totInvAmt));
                }
            }

        }
       // System.out.println("lblTotalTransactionAmtVal.getText()###" + lblTotalTransactionAmtVal.getText());        
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panSearchCondition = new com.see.truetransact.uicomponent.CPanel();
        panMultiSearch3 = new com.see.truetransact.uicomponent.CPanel();
        lblFromDate = new com.see.truetransact.uicomponent.CLabel();
        tdtFromDate = new com.see.truetransact.uicomponent.CDateField();
        lblToDate = new com.see.truetransact.uicomponent.CLabel();
        tdtToDate = new com.see.truetransact.uicomponent.CDateField();
        lblProdName = new com.see.truetransact.uicomponent.CLabel();
        cboChargeType = new com.see.truetransact.uicomponent.CComboBox();
        lblChargeType = new com.see.truetransact.uicomponent.CLabel();
        cboProdName = new com.see.truetransact.uicomponent.CComboBox();
        btnProcess = new com.see.truetransact.uicomponent.CButton();
        lblProdId = new com.see.truetransact.uicomponent.CLabel();
        cboProdId = new com.see.truetransact.uicomponent.CComboBox();
        panMultiSearch1 = new com.see.truetransact.uicomponent.CPanel();
        lblTOAccountno = new com.see.truetransact.uicomponent.CLabel();
        lblFromAccountno = new com.see.truetransact.uicomponent.CLabel();
        cPanel2 = new com.see.truetransact.uicomponent.CPanel();
        txtFromAccountno = new com.see.truetransact.uicomponent.CTextField();
        btnFromAccountno = new com.see.truetransact.uicomponent.CButton();
        cPanel3 = new com.see.truetransact.uicomponent.CPanel();
        txtTOAccountno = new com.see.truetransact.uicomponent.CTextField();
        btnTOAccountno = new com.see.truetransact.uicomponent.CButton();
        chkFulldue = new com.see.truetransact.uicomponent.CCheckBox();
        txtNoOfInstallments = new com.see.truetransact.uicomponent.CTextField();
        lblNoOfInstallments1 = new com.see.truetransact.uicomponent.CLabel();
        lblBranch = new com.see.truetransact.uicomponent.CLabel();
        cboBranchCode = new com.see.truetransact.uicomponent.CComboBox();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        txtChrgAmt = new com.see.truetransact.uicomponent.CTextField();
        chkNoTrans = new com.see.truetransact.uicomponent.CCheckBox();
        panTable = new com.see.truetransact.uicomponent.CPanel();
        chkSelectAll = new com.see.truetransact.uicomponent.CCheckBox();
        srcTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblData = new com.see.truetransact.uicomponent.CTable();
        lblTotalTransactionAmt = new com.see.truetransact.uicomponent.CLabel();
        lblTotalTransactionAmtVal = new com.see.truetransact.uicomponent.CLabel();
        panSearch = new com.see.truetransact.uicomponent.CPanel();
        btnPostCharges = new com.see.truetransact.uicomponent.CButton();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        btnClear1 = new com.see.truetransact.uicomponent.CButton();
        sptLine = new com.see.truetransact.uicomponent.CSeparator();
        panMultiSearch = new com.see.truetransact.uicomponent.CPanel();
        cboProdTypeCr = new com.see.truetransact.uicomponent.CComboBox();
        lblProdTypeCr = new com.see.truetransact.uicomponent.CLabel();
        lblAccNo = new com.see.truetransact.uicomponent.CLabel();
        lblAccHd = new com.see.truetransact.uicomponent.CLabel();
        cboProdIdCr = new com.see.truetransact.uicomponent.CComboBox();
        lblProdIdCr = new com.see.truetransact.uicomponent.CLabel();
        panAccHd = new com.see.truetransact.uicomponent.CPanel();
        txtAccNo = new com.see.truetransact.uicomponent.CTextField();
        btnAccNo = new com.see.truetransact.uicomponent.CButton();
        panAcctNo = new com.see.truetransact.uicomponent.CPanel();
        txtAccHd = new com.see.truetransact.uicomponent.CTextField();
        btnAccHd = new com.see.truetransact.uicomponent.CButton();
        lblActHeadDesc = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setTitle("Loan Notices");
        setMinimumSize(new java.awt.Dimension(800, 630));
        setPreferredSize(new java.awt.Dimension(800, 630));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        panSearchCondition.setMinimumSize(new java.awt.Dimension(574, 170));
        panSearchCondition.setPreferredSize(new java.awt.Dimension(574, 170));
        panSearchCondition.setLayout(new java.awt.GridBagLayout());

        panMultiSearch3.setMinimumSize(new java.awt.Dimension(275, 160));
        panMultiSearch3.setPreferredSize(new java.awt.Dimension(275, 160));
        panMultiSearch3.setLayout(new java.awt.GridBagLayout());

        lblFromDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblFromDate.setText("From Date");
        lblFromDate.setMaximumSize(new java.awt.Dimension(126, 18));
        lblFromDate.setMinimumSize(new java.awt.Dimension(126, 18));
        lblFromDate.setPreferredSize(new java.awt.Dimension(126, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 4);
        panMultiSearch3.add(lblFromDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 11, 2, 4);
        panMultiSearch3.add(tdtFromDate, gridBagConstraints);

        lblToDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblToDate.setText("To Date");
        lblToDate.setMaximumSize(new java.awt.Dimension(230, 85));
        lblToDate.setMinimumSize(new java.awt.Dimension(116, 18));
        lblToDate.setPreferredSize(new java.awt.Dimension(116, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panMultiSearch3.add(lblToDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 11, 2, 4);
        panMultiSearch3.add(tdtToDate, gridBagConstraints);

        lblProdName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblProdName.setText("Product Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panMultiSearch3.add(lblProdName, gridBagConstraints);

        cboChargeType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboChargeType.setPopupWidth(160);
        cboChargeType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboChargeTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 11, 2, 4);
        panMultiSearch3.add(cboChargeType, gridBagConstraints);

        lblChargeType.setText("Charge Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panMultiSearch3.add(lblChargeType, gridBagConstraints);

        cboProdName.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdNameActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 11, 2, 4);
        panMultiSearch3.add(cboProdName, gridBagConstraints);

        btnProcess.setText("Process");
        btnProcess.setMaximumSize(new java.awt.Dimension(93, 27));
        btnProcess.setMinimumSize(new java.awt.Dimension(93, 27));
        btnProcess.setPreferredSize(new java.awt.Dimension(93, 27));
        btnProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        panMultiSearch3.add(btnProcess, gridBagConstraints);

        lblProdId.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panMultiSearch3.add(lblProdId, gridBagConstraints);

        cboProdId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdId.setPopupWidth(160);
        cboProdId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 11, 2, 4);
        panMultiSearch3.add(cboProdId, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSearchCondition.add(panMultiSearch3, gridBagConstraints);

        panMultiSearch1.setMinimumSize(new java.awt.Dimension(275, 145));
        panMultiSearch1.setPreferredSize(new java.awt.Dimension(275, 145));
        panMultiSearch1.setLayout(new java.awt.GridBagLayout());

        lblTOAccountno.setText("To Acct No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panMultiSearch1.add(lblTOAccountno, gridBagConstraints);

        lblFromAccountno.setText("From Acct No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panMultiSearch1.add(lblFromAccountno, gridBagConstraints);

        cPanel2.setMinimumSize(new java.awt.Dimension(130, 22));
        cPanel2.setPreferredSize(new java.awt.Dimension(130, 22));
        cPanel2.setLayout(new java.awt.GridBagLayout());

        txtFromAccountno.setAllowAll(true);
        txtFromAccountno.setMinimumSize(new java.awt.Dimension(100, 21));
        txtFromAccountno.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFromAccountnoFocusLost(evt);
            }
        });
        cPanel2.add(txtFromAccountno, new java.awt.GridBagConstraints());

        btnFromAccountno.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnFromAccountno.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnFromAccountno.setMaximumSize(new java.awt.Dimension(21, 21));
        btnFromAccountno.setMinimumSize(new java.awt.Dimension(21, 21));
        btnFromAccountno.setPreferredSize(new java.awt.Dimension(21, 21));
        btnFromAccountno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFromAccountnoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        cPanel2.add(btnFromAccountno, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 9, 2, 4);
        panMultiSearch1.add(cPanel2, gridBagConstraints);

        cPanel3.setMinimumSize(new java.awt.Dimension(130, 22));
        cPanel3.setPreferredSize(new java.awt.Dimension(130, 22));
        cPanel3.setLayout(new java.awt.GridBagLayout());

        txtTOAccountno.setAllowAll(true);
        txtTOAccountno.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTOAccountno.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTOAccountnoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        cPanel3.add(txtTOAccountno, gridBagConstraints);

        btnTOAccountno.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnTOAccountno.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnTOAccountno.setMaximumSize(new java.awt.Dimension(21, 21));
        btnTOAccountno.setMinimumSize(new java.awt.Dimension(21, 21));
        btnTOAccountno.setPreferredSize(new java.awt.Dimension(21, 21));
        btnTOAccountno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTOAccountnoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        cPanel3.add(btnTOAccountno, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 9, 2, 4);
        panMultiSearch1.add(cPanel3, gridBagConstraints);

        chkFulldue.setText("Full Due");
        chkFulldue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkFulldueActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMultiSearch1.add(chkFulldue, gridBagConstraints);

        txtNoOfInstallments.setAllowAll(true);
        txtNoOfInstallments.setMinimumSize(new java.awt.Dimension(30, 21));
        txtNoOfInstallments.setPreferredSize(new java.awt.Dimension(30, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        panMultiSearch1.add(txtNoOfInstallments, gridBagConstraints);

        lblNoOfInstallments1.setText("Installments");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 5, 4, 35);
        panMultiSearch1.add(lblNoOfInstallments1, gridBagConstraints);

        lblBranch.setText("Branch Code");
        panMultiSearch1.add(lblBranch, new java.awt.GridBagConstraints());

        cboBranchCode.setPreferredSize(new java.awt.Dimension(120, 21));
        panMultiSearch1.add(cboBranchCode, new java.awt.GridBagConstraints());

        cLabel1.setText("Charge Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        panMultiSearch1.add(cLabel1, gridBagConstraints);

        txtChrgAmt.setAllowNumber(true);
        txtChrgAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtChrgAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        panMultiSearch1.add(txtChrgAmt, gridBagConstraints);

        chkNoTrans.setText("No Transaction");
        chkNoTrans.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkNoTransActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        panMultiSearch1.add(chkNoTrans, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSearchCondition.add(panMultiSearch1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(panSearchCondition, gridBagConstraints);

        panTable.setMinimumSize(new java.awt.Dimension(600, 250));
        panTable.setPreferredSize(new java.awt.Dimension(600, 250));
        panTable.setLayout(new java.awt.GridBagLayout());

        chkSelectAll.setText("Select All");
        chkSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSelectAllActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panTable.add(chkSelectAll, gridBagConstraints);

        srcTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                srcTableMouseClicked(evt);
            }
        });

        tblData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Investment ID", "A/c Ref No", "Principal Amount", "Inv.Prd.Years", "Inv.Prd.Months", "Inv.Prd.Days", "Int Pay Freq", "Rate of Interest", "Interest", "Maturity Amount", "Eff.Date"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblData.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_NEXT_COLUMN);
        tblData.setMinimumSize(new java.awt.Dimension(100, 35));
        tblData.setOpaque(false);
        tblData.setPreferredScrollableViewportSize(new java.awt.Dimension(450000, 400000));
        tblData.setPreferredSize(new java.awt.Dimension(450000, 400000));
        tblData.setReorderingAllowed(true);
        tblData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDataMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                tblDataMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblDataMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblDataMouseReleased(evt);
            }
        });
        tblData.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tblDataFocusLost(evt);
            }
        });
        tblData.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblDataKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tblDataKeyTyped(evt);
            }
        });
        srcTable.setViewportView(tblData);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panTable.add(srcTable, gridBagConstraints);

        lblTotalTransactionAmt.setText("Total Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 2, 112);
        panTable.add(lblTotalTransactionAmt, gridBagConstraints);

        lblTotalTransactionAmtVal.setMinimumSize(new java.awt.Dimension(100, 21));
        lblTotalTransactionAmtVal.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 2);
        panTable.add(lblTotalTransactionAmtVal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(panTable, gridBagConstraints);

        panSearch.setLayout(new java.awt.GridBagLayout());

        btnPostCharges.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/regularisation.gif"))); // NOI18N
        btnPostCharges.setText("Post Charges");
        btnPostCharges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPostChargesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        panSearch.add(btnPostCharges, gridBagConstraints);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        panSearch.add(btnClose, gridBagConstraints);

        btnClear1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnClear1.setText("Clear");
        btnClear1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClear1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearch.add(btnClear1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(panSearch, gridBagConstraints);

        sptLine.setMinimumSize(new java.awt.Dimension(2, 2));
        sptLine.setPreferredSize(new java.awt.Dimension(2, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(sptLine, gridBagConstraints);

        panMultiSearch.setBorder(javax.swing.BorderFactory.createTitledBorder("Credit Details"));
        panMultiSearch.setMinimumSize(new java.awt.Dimension(700, 80));
        panMultiSearch.setPreferredSize(new java.awt.Dimension(700, 80));
        panMultiSearch.setLayout(new java.awt.GridBagLayout());

        cboProdTypeCr.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdTypeCr.setPopupWidth(160);
        cboProdTypeCr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdTypeCrActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMultiSearch.add(cboProdTypeCr, gridBagConstraints);

        lblProdTypeCr.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMultiSearch.add(lblProdTypeCr, gridBagConstraints);

        lblAccNo.setText("Account No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMultiSearch.add(lblAccNo, gridBagConstraints);

        lblAccHd.setText("Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMultiSearch.add(lblAccHd, gridBagConstraints);

        cboProdIdCr.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdIdCr.setPopupWidth(160);
        cboProdIdCr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdIdCrActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMultiSearch.add(cboProdIdCr, gridBagConstraints);

        lblProdIdCr.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMultiSearch.add(lblProdIdCr, gridBagConstraints);

        panAccHd.setMinimumSize(new java.awt.Dimension(121, 21));
        panAccHd.setPreferredSize(new java.awt.Dimension(21, 200));
        panAccHd.setLayout(new java.awt.GridBagLayout());

        txtAccNo.setAllowAll(true);
        txtAccNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAccNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAccNoActionPerformed(evt);
            }
        });
        txtAccNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAccNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccHd.add(txtAccNo, gridBagConstraints);

        btnAccNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccNo.setToolTipText("Account Number");
        btnAccNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAccNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccNoActionPerformed(evt);
            }
        });
        panAccHd.add(btnAccNo, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMultiSearch.add(panAccHd, gridBagConstraints);

        panAcctNo.setMinimumSize(new java.awt.Dimension(121, 21));
        panAcctNo.setPreferredSize(new java.awt.Dimension(21, 200));
        panAcctNo.setLayout(new java.awt.GridBagLayout());

        txtAccHd.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAccHd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAccHdActionPerformed(evt);
            }
        });
        txtAccHd.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAccHdFocusLost(evt);
            }
        });
        panAcctNo.add(txtAccHd, new java.awt.GridBagConstraints());

        btnAccHd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccHd.setToolTipText("Account No.");
        btnAccHd.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAccHd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccHdActionPerformed(evt);
            }
        });
        panAcctNo.add(btnAccHd, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMultiSearch.add(panAcctNo, gridBagConstraints);

        lblActHeadDesc.setMaximumSize(new java.awt.Dimension(160, 14));
        lblActHeadDesc.setMinimumSize(new java.awt.Dimension(160, 14));
        lblActHeadDesc.setPreferredSize(new java.awt.Dimension(160, 14));
        panMultiSearch.add(lblActHeadDesc, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(panMultiSearch, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void setVisibleForAuctionNotice(boolean val) {
//        lblAuctionDate.setVisible(val);
//        tdtAuctionDate.setVisible(val);
    }

    private void setVisibleForDemandNotice(boolean val) {
//        lblOverDueDate.setVisible(val);
//        tdtOverDueDate.setVisible(val);
//        chkFulldue.setVisible(val);
//        lblNoOfInstallments.setVisible(val);
//        txtNoOfInstallments.setVisible(val);
//        if (val) {
//            lblFromDate.setText("Sanction From Date");
//            lblToDate.setText("Sanction To Date");
//        } else {
//            lblFromDate.setText("Demand From Date");
//            lblToDate.setText("Demand To Date");
//            tdtOverDueDate.setDateValue("");
//            chkFulldue.setSelected(false);
//            txtNoOfInstallments.setText("");
//        }
    }

    private void clearTable() {
        observable.resetForm();
        if (tblData.getRowCount() > 0) {
            ((DefaultTableModel) tblData.getModel()).setRowCount(0);
        }
    }
    
    private void btnClear1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClear1ActionPerformed
        // TODO add your handling code here:
        ClientUtil.clearAll(this);
        observable.resetForm();
        lblTotalTransactionAmtVal.setText("");
        clearTable();
//        btnsms.setVisible(false);
//        btnPostCharges.setEnabled(true);
//        lblTotalChargeAmountVal.setText("");
//        lblSelectedRecordVal.setText("");
        //lblNoOfRecordsVal.setText("");
    }//GEN-LAST:event_btnClear1ActionPerformed
    public void fillData(Object param) {

        final HashMap hash = (HashMap) param;
        System.out.println("Hash: " + hash);

        if (viewType == ACCNO) {
            if(hash.containsKey("ACT_NUM")){
                hash.put("ACCOUNTNO", hash.get("ACT_NUM"));
            }
            txtAccNo.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
        }
        if (viewType == ACCTHDID) {
            String acHdId = hash.get("A/C HEAD").toString();
            String acHdDesc = hash.get("A/C HEAD DESCRIPTION").toString();
            String bankType = CommonConstants.BANK_TYPE;
            //system.out.println("bankType" + bankType);
            String customerAllow = "";
            String hoAc = "";
            //cboProdId.setSelectedItem("");
            this.txtAccNo.setText("");
            txtAccHd.setText(acHdId);
            lblActHeadDesc.setText(acHdDesc);
            HashMap hmap = new HashMap();
            hmap.put("ACHEAD", acHdId);
            List list = ClientUtil.executeQuery("getCustomerAlloowProperty", hmap);
            if (list != null && list.size() > 0) {
                hmap = (HashMap) list.get(0);
                customerAllow = CommonUtil.convertObjToStr(hmap.get("ALLOW_CUSTOMER_ACNUM"));
                hoAc = CommonUtil.convertObjToStr(hmap.get("HO_ACCT"));
            }
            if (bankType.equals("DCCB")) {
                if (hoAc.equals("Y")) {
                } else {
                }
            }
            //system.out.println("customerAllow>>>>" + customerAllow);
            if (customerAllow.equals("Y")) {
            }

        } else if (viewType == FROMACTNO) {
            if(hash.containsKey("ACT_NUM")){
                hash.put("ACCOUNTNO", hash.get("ACT_NUM"));
            }
            txtFromAccountno.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
        } else if (viewType == TOACTNO) {
            if(hash.containsKey("ACT_NUM")){
                hash.put("ACCOUNTNO", hash.get("ACT_NUM"));
            }
            txtTOAccountno.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
        }

    }

    private void setVisibleFields(boolean flag) {
//        lblFromDate.setVisible(flag);
//        lblToDate.setVisible(flag);
//        tdtFromDate.setVisible(flag);
//        lblFromAccountno.setVisible(flag);
//        txtFromAccountno.setVisible(flag);
//        lblTOAccountno.setVisible(flag);
//        txtTOAccountno.setVisible(flag);
//        btnFromAccountno.setVisible(flag);
//        btnTOAccountno.setVisible(flag);
//        tdtToDate.setVisible(flag);
//        lblOverDueDate.setVisible(flag);
//        tdtOverDueDate.setVisible(flag);
//        chkPrized.setVisible(false);
//        chkNonPrized.setVisible(false);
    }

    private void popUp(int field) {
        //updateOBFields();
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        viewType = field;
        if (field == ACCNO) {
            if (observable.getProdType().equals("TD") || observable.getProdType().equals("TL") || observable.getProdType().equals("AB")) {
//                if (rdoTransactionType_Debit.isSelected()) {
//                    if (observable.getProdType().equals("TL") || observable.getProdType().equals("AB")) {
//                        whereMap.put("PAYMENT", "PAYMENT");
//                        viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList"
//                                + ((ComboBoxModel) cboProdType.getModel()).getKeyForSelected().toString());
//                    } else {
//                        viewMap.put(CommonConstants.MAP_NAME, "getDepositHoldersInterest");
//
//                        transDetails.setIsDebitSelect(true);
//                    }
//                } else 
//                if (rdoTransactionType_Credit.isSelected()) {
                if (observable.getProdType().equals("TL") || observable.getProdType().equals("AB")) {
                    whereMap.put("RECEIPT", "RECEIPT");
                }
                viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList"
                        + ((ComboBoxModel) cboProdTypeCr.getModel()).getKeyForSelected().toString());
//                }
//                else {
//                    ClientUtil.showMessageWindow("Select Payment or Receipt ");
//                    return;
//                }
            } else {
                viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList"
                        + ((ComboBoxModel) cboProdTypeCr.getModel()).getKeyForSelected().toString());
            }
            whereMap.put("PROD_ID", ((ComboBoxModel) cboProdIdCr.getModel()).getKeyForSelected());
            whereMap.put("SELECTED_BRANCH", ProxyParameters.BRANCH_ID);
        } else {
             String prodName = String.valueOf(cboProdName.getSelectedItem());
            // String prodName = ((ComboBoxModel) cboProdName.getModel()).getKeyForSelected().toString();
            String prodId = ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected().toString();
           
            whereMap.put("prodId", prodId);
            if(prodName.equals("MDS")){
                if (!observable.getCbmbranch().getKeyForSelected().equals("") && observable.getCbmbranch().getKeyForSelected()!=null) {
                    whereMap.put("BRANCH_CODE", observable.getCbmbranch().getKeyForSelected());
                }else{
                    whereMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID); 
                } 
                viewMap.put(CommonConstants.MAP_NAME, "getMDS_AccountList");
            }else{
                List lst = ClientUtil.executeQuery("TermLoan.getProdHead", whereMap);
                if (lst != null && lst.size() > 0) {
                    whereMap = (HashMap) lst.get(0);
                    
                    String behavesLike = CommonUtil.convertObjToStr(whereMap.get("BEHAVES_LIKE"));
                    if (!observable.getCbmbranch().getKeyForSelected().equals("") && observable.getCbmbranch().getKeyForSelected()!=null) {
                        whereMap.put("SELECTED_BRANCH", observable.getCbmbranch().getKeyForSelected());
                    }else{
                        whereMap.put("SELECTED_BRANCH", ProxyParameters.BRANCH_ID); 
                    } 
                    if (behavesLike.equals("OD")) {
                        viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountListAD");
                        //whereMap.put("SELECTED_BRANCH", com.see.truetransact.ui.TrueTransactMain.BRANCH_ID);
                    } else {
                        viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountListTL");
                        //whereMap.put("SELECTED_BRANCH", com.see.truetransact.ui.TrueTransactMain.BRANCH_ID);
                    }
                }
            }
//        else  if(prodType.equals("TD")){
//            viewMap.put(CommonConstants.MAP_NAME, "TDCharges.getAcctList");
//        } else  if(prodType.equals("SH")){
//            if(((ComboBoxModel)cboProductId.getModel()).getKeyForSelected().toString().length()>0)
//                hash.put("SHARE_TYPE", ((ComboBoxModel)cboProductId.getModel()).getKeyForSelected().toString());
//            hash.put("DIVIDEND_PAY_MODE", "TRANSFER'");
//            
//            viewMap.put(CommonConstants.MAP_NAME, "getSelectDividendUnclaimedTransferList");
//        }else {
//            viewMap.put(CommonConstants.MAP_NAME, "OACharges.getAcctList");
//        }

            whereMap.put("PROD_ID", prodId);
            whereMap.put(CommonConstants.BRANCH_ID, com.see.truetransact.ui.TrueTransactMain.BRANCH_ID);
            //        if(viewType==TO){
            //            hash.put("ACCT_NO", txtFromAccount.getText());
        }//        }
        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        new ViewAll(this, viewMap).show();
    }
    private void btnPostChargesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPostChargesActionPerformed
        // Add your handling code here:
//        String prodType = String.valueOf(cboProdType.getSelectedItem());
        try {
            
            if(!chkNoTrans.isSelected()){
             if(cboProdTypeCr.getSelectedItem().equals("")) {                
                    ClientUtil.showMessageWindow("Please select Credit Product type!!!");
                    return;               
             
            }else{
                 if(cboProdTypeCr.getSelectedItem().equals("General Ledger")) {   
                    if(txtAccHd.getText().equals("")){
                        ClientUtil.showMessageWindow("Please select Credit account!!!");
                        return;
                    }
                 }else{
                     if(cboProdIdCr.getSelectedItem().equals("")) {   
                        ClientUtil.showMessageWindow("Please select productId!!!");                         
                        return;                       
                    }else{
                         if(txtAccNo.getText().equals("") || txtAccNo.getText().length()<=4 ){
                                ClientUtil.showMessageWindow("Please select Credit account!!!");
                            return;
                        }
                     }
                 }
             }
            }
             if(tblData.getRowCount()<=0){
                  ClientUtil.showMessageWindow("Please process the details!!!");
                   return;
             }             
             boolean actChk = false;
             for (int i = 0; i < tblData.getRowCount(); i++) {
                    if (((Boolean) tblData.getValueAt(i, 0)).booleanValue()) {
                        actChk = true;
                    }
             }
             if(!actChk){
                 ClientUtil.showMessageWindow("Please select any accounts!!!");
                   return;
             }
             
             boolean chk = false;
             for (int i = 0; i < tblData.getRowCount(); i++) {
                    if (((Boolean) tblData.getValueAt(i, 0)).booleanValue()) {
                        System.out.println("CommonUtil.convertObjToDouble(tblData.getValueAt(i, 7))^#^#^#^#^"+CommonUtil.convertObjToDouble(tblData.getValueAt(i, 7)));
                      if(CommonUtil.convertObjToDouble(tblData.getValueAt(i, 7))<=0){
                         chk = true; 
                      } 
                   }
             }             
             if(chk){
                 ClientUtil.showMessageWindow("Please enter the charge amount!!!\n Please un-check the accouts without charges");
                   return;
             }
             
            lstTotRisk = new ArrayList();
            Double riskFindTot = 0.0;
            updateOBFields(); 
            ArrayList lstRisk = new ArrayList();
            if (tblData.getRowCount() > 0) {
                for (int i = 0; i < tblData.getRowCount(); i++) {
                    lstRisk = new ArrayList();
                    if (((Boolean) tblData.getValueAt(i, 0)).booleanValue()) {
                        for (int j = 1; j < tblData.getColumnCount(); j++) {
                            lstRisk.add(tblData.getValueAt(i, j));
                        }
                        riskFindTot = riskFindTot + CommonUtil.convertObjToDouble(tblData.getValueAt(i, 7));
                        lstTotRisk.add(lstRisk);
                    }
                }
            }
            System.out.println("mapRisk$^$^^$^$^$^$^$^  " + observable.getCboChargeType());
            observable.setRiskFundTot(riskFindTot);
            observable.executeOB(lstTotRisk);
            if (observable.getProxyReturnMap() != null && observable.getProxyReturnMap().size()>0) {
                if(observable.getProxyReturnMap().containsKey("STATUS") && observable.getProxyReturnMap().get("STATUS") != null && observable.getProxyReturnMap().get("STATUS").equals("SUCCESS")){
                    ClientUtil.showMessageWindow("Process Completed Successfully !!!");
                }else{
                displayTransDetailNew(observable.getProxyReturnMap());
            }  
            }  
            btnClear1ActionPerformed(evt);
        } catch (Exception e) {
            e.printStackTrace();
            ClientUtil.showMessageWindow(e.getMessage());
        }

    }//GEN-LAST:event_btnPostChargesActionPerformed

     public void  displayTransDetailNew(HashMap returnMap){
         String cashDisplayStr = "Cash Transaction Details...\n";
        String transferDisplayStr = "Transfer Transaction Details...\n";
        String displayStr = "";
        String transId = "";
        String transType = "";
      //  System.out.println("jhj>>>>>>>");
        Object keys[] = returnMap.keySet().toArray();
        System.out.println("jhj>>>>>>>adad");
        //System.out.println("keeeeeeeeeeeyyy>>>>>>>>>>>"+keys[]);
        int cashCount = 0;
        int transferCount = 0;
        List tempList = null;
        HashMap transMap = null;
        String actNum = "";
        HashMap transIdMap = new HashMap();
        HashMap transTypeMap = new HashMap();
       // System.out.println("keeeeeeeeeeeyyy>>>>>>>>>>>" + keys.length);
        for (int i = 0; i < keys.length; i++) {
            System.out.println("jhj>>>>>>>adad1211222@@@@" + (returnMap.get(keys[i]) instanceof String));
            if (returnMap.get(keys[i]) instanceof String) {
          //      System.out.println("hdfdasd");
                continue;
            }

          //  System.out.println("hdfdasd@@@@@");
            tempList = (List) returnMap.get(keys[i]);
          //  System.out.println("hdfdasd@@@@@>>>>>" + tempList);
            if (CommonUtil.convertObjToStr(keys[i]).indexOf("CASH") != -1) {
              //  System.out.println("haaaiii11....>>>");
                for (int j = 0; j < tempList.size(); j++) {
                  //  System.out.println("haaaiii11....>>>aa");
                    transMap = (HashMap) tempList.get(j);
                    if (j == 0) {
                       // System.out.println("haaaiii11....>>>bb");
                        transId = (String) transMap.get("SINGLE_TRANS_ID");
                    }
                    cashDisplayStr += "Trans Id : " + transMap.get("TRANS_ID")
                            + "   Trans Type : " + transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if (actNum != null && !actNum.equals("")) {
                      //  System.out.println("haaaiii11....>>>cc");
                        cashDisplayStr += "   Account No : " + transMap.get("ACT_NUM")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    } else {
                      //  System.out.println("haaaiii11....>>>dd");
                        cashDisplayStr += "   Ac Hd Desc : " + transMap.get("AC_HD_ID")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    }
                    transTypeMap.put(transMap.get("SINGLE_TRANS_ID"), transMap.get("TRANS_TYPE"));
                    transIdMap.put(transMap.get("SINGLE_TRANS_ID"), "CASH");
                }
                cashCount++;
            } else if (CommonUtil.convertObjToStr(keys[i]).indexOf("TRANSFER") != -1) {
                //System.out.println("haaaiii22....>>>");
                for (int j = 0; j < tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j == 0) {
                      //  System.out.println("haaaiii22....>>>aa");
                        transId = (String) transMap.get("SINGLE_TRANS_ID");
                    }
                    transferDisplayStr += "Trans Id : " + transMap.get("TRANS_ID")
                            + "   Batch Id : " + transMap.get("BATCH_ID")
                            + "   Trans Type : " + transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if (actNum != null && !actNum.equals("")) {
                       // System.out.println("haaaiii22....>>>bb");
                        transferDisplayStr += "   Account No : " + transMap.get("ACT_NUM")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    } else {
                      //  System.out.println("haaaiii22....>>>cc");
                        transferDisplayStr += "   Ac Hd Desc : " + transMap.get("AC_HD_ID")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    }
                    transIdMap.put(transMap.get("SINGLE_TRANS_ID"), "TRANSFER");
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
        if (!displayStr.equals("")) {
            ClientUtil.showMessageWindow("" + displayStr);
        }
        int yesNo = 0;
        String[] options = {"Yes", "No"};
        yesNo = COptionPane.showOptionDialog(null,"Do you want to print?", CommonConstants.WARNINGTITLE,
        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
        null, options, options[0]);
        System.out.println("#$#$$ yesNo : "+yesNo);
        if (yesNo==0) {
            TTIntegration ttIntgration = null;
            HashMap paramMap = new HashMap();
            //paramMap.put("TransId", transId);
            paramMap.put("TransDt", currDt);
            paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
            Object keys1[] = transIdMap.keySet().toArray();            
             for (int i = 0; i < keys1.length; i++) {
                            paramMap.put("TransId", keys1[i]);
                            ttIntgration.setParam(paramMap);
                            //                        if (((String)TrueTransactMain.BANKINFO.get("BANK_NAME")).toUpperCase().lastIndexOf("POLPULLY")!=-1) {
                            if (CommonUtil.convertObjToStr(transIdMap.get(keys1[i])).equals("TRANSFER")) {
                                ttIntgration.integrationForPrint("ReceiptPayment",false);
                            } 
                            else if (CommonUtil.convertObjToStr(transTypeMap.get(keys1[i])).equals("DEBIT")) {
                                    ttIntgration.integrationForPrint("CashPayment", false);
                            }else {
                                ttIntgration.integrationForPrint("CashReceipt", false);
                            }
                        }     
        }
        
    }
            
    public void updateOBFields() {
        observable.setCboProdName(cboProdName.getSelectedItem().toString());
        observable.setCboProdId(CommonUtil.convertObjToStr(observable.getCbmProdId().getKeyForSelected()));
        observable.setTdtFromDate(tdtFromDate.getDateValue());
        observable.setTdtToDate(tdtToDate.getDateValue());
        //observable.setCboChargeType(cboChargeType.getSelectedItem().toString());
        observable.setCboProdTypCr(((ComboBoxModel) cboProdTypeCr.getModel()).getKeyForSelected().toString());
        if (!chkNoTrans.isSelected() && !(((ComboBoxModel) cboProdTypeCr.getModel()).getKeyForSelected().toString()).equals("GL")) {
            observable.setCboProdIdCr(((ComboBoxModel) cboProdIdCr.getModel()).getKeyForSelected().toString());
            observable.setTxtActNo(txtAccNo.getText());
        }
        observable.setTxtAccHd(txtAccHd.getText());
        if(chkNoTrans.isSelected()){
            observable.setChkNoTrans("Y");
        }else{
            observable.setChkNoTrans("N");
        }
    }
    private void chkSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSelectAllActionPerformed
        // Add your handling code here:
        observable.setSelectAll(tblData, new Boolean(chkSelectAll.isSelected()));
        setSelectedRecord();
    }//GEN-LAST:event_chkSelectAllActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
//        observable.removeRowsFromGuarantorTable(tblData);
//        observable.removeRowsFromGuarantorTable(tblGuarantorData);
        dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    //Added By Suresh
    private void setSelectedRecord() {
        int count = 0;
        if (tblData.getRowCount() > 0) {
            for (int i = 0, j = tblData.getRowCount(); i < j; i++) {
                if (((Boolean) tblData.getValueAt(i, 0)).booleanValue()) {
                    count += 1;
                }
            }
        }
//        lblSelectedRecordVal.setText(String.valueOf(count));
        calculateTotalApplCharges();
    }

    private void calculateTotalApplCharges() {
//        double totalChargeAmount = (CommonUtil.convertObjToDouble(txtNoticeCharge.getText())
//                + CommonUtil.convertObjToDouble(txtPostageCharge.getText()));
//        lblTotalChargeAmountVal.setText(String.valueOf(totalChargeAmount * CommonUtil.convertObjToDouble(lblSelectedRecordVal.getText())));
    }

    public void populateData() {
//        updateOBFields();
//        String behavesLike = "";
//        HashMap viewMap = new HashMap();
//        HashMap whereMap = new HashMap();
//        boolean isOK = false;
//        if (!CommonUtil.convertObjToStr(cboProdType.getSelectedItem()).equals("")) {
//            isOK = true;
//        } else {
//            isOK = false;
//            ClientUtil.displayAlert("Select Product Type...");
//        }
//        if (!CommonUtil.convertObjToStr(cboNoticeType.getSelectedItem()).equals("")) {
//            isOK = true;
//        } else {
//            isOK = false;
//            ClientUtil.displayAlert("Select Notice Type...");
//        }
//
//        if (bankName.lastIndexOf("MAHILA") == -1) {
//            if (!CommonUtil.convertObjToStr(cboNoticeType.getSelectedItem()).equals("Demand Notice")) {
//                if (chkFulldue.isSelected() || CommonUtil.convertObjToInt(txtNoOfInstallments.getText()) > 0) {
//                    isOK = true;
//                } else {
//                    isOK = false;
//                    ClientUtil.displayAlert("Select Full Due or Enter No. of Installments...");
//                }
//            } else {
//                isOK = true;
//            }
//        }
//
//        if (isOK) {
//
//            String prodType = String.valueOf(cboProdType.getSelectedItem());
//            String prodId = ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected().toString();
//            whereMap.put("prodId", prodId);
//            List lst = ClientUtil.executeQuery("TermLoan.getProdHead", whereMap);
//            if (lst != null && lst.size() > 0) {
//                whereMap = (HashMap) lst.get(0);
//                behavesLike = CommonUtil.convertObjToStr(whereMap.get("BEHAVES_LIKE"));
//            }
//
//            if (prodType.equals("MDS")) {
//                viewMap.put(CommonConstants.MAP_NAME, "getAccountsForMDSLoanNotice");
//            } else if (behavesLike.equals("OD")) {
//                viewMap.put(CommonConstants.MAP_NAME, "getAccountsForLoanNoticeAD");
//            } else {
//                viewMap.put(CommonConstants.MAP_NAME, "getAccountsForLoanNotice");
//            }
//            //                whereMap.put("ACT_NUM", observable.getTxtAccNo());
//            if (prodType.length() > 0) {
//                whereMap.put("PROD_TYPE", prodType);
//            }
//            if (String.valueOf(observable.getCbmProdId().getKeyForSelected()).length() > 0) {
//                whereMap.put("PROD_ID", observable.getCbmProdId().getKeyForSelected());
//            }
//            if (tdtFromDate.getDateValue() != null && tdtFromDate.getDateValue().length() > 0) {
//                if (CommonUtil.convertObjToStr(cboNoticeType.getSelectedItem()).equals("Demand Notice")) {
//                    whereMap.put("FROM_DUE_DT", getProperDate(DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue())));
//                } else {
//                    whereMap.put("FROM_DT", getProperDate(DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue())));
//                }
//            }
//            if (tdtToDate.getDateValue() != null && tdtToDate.getDateValue().length() > 0) {
//                if (CommonUtil.convertObjToStr(cboNoticeType.getSelectedItem()).equals("Demand Notice")) {
//                    whereMap.put("TO_DUE_DT", getProperDate(DateUtil.getDateMMDDYYYY(tdtToDate.getDateValue())));
//                } else {
//                    whereMap.put("TO_DT", getProperDate(DateUtil.getDateMMDDYYYY(tdtToDate.getDateValue())));
//                }
//            } else {
//                if (CommonUtil.convertObjToStr(cboNoticeType.getSelectedItem()).equals("Demand Notice")) {
//                    whereMap.put("TO_DUE_DT", currDt);
//                } else {
//                    whereMap.put("TO_DT", currDt);
//                }
//            }
//            if (CommonUtil.convertObjToStr(txtFromAccountno.getText()).length() > 0) {
//                whereMap.put("FROM_ACCT_NUM", txtFromAccountno.getText());
//            }
//
//            if (CommonUtil.convertObjToStr(txtTOAccountno.getText()).length() > 0) {
//                whereMap.put("TO_ACCT_NUM", txtTOAccountno.getText());
//            }
//            if (CommonUtil.convertObjToStr(txtLastNoticeSentBefore.getText()).length() > 0) {
//                Date lastNoticeDate = (Date) currDt.clone();
//                lastNoticeDate = DateUtil.addDays(lastNoticeDate, -CommonUtil.convertObjToInt(txtLastNoticeSentBefore.getText()));
//                whereMap.put("LAST_NOTICE_SENT_DT", lastNoticeDate);
//            }
//            if (chkFulldue.isSelected()) {
//                whereMap.put("FULL_DUE", "FULL_DUE");
//            }
//            if (!chkLoneeOnly.isSelected()) {
//                whereMap.put("GUARANTOR", "GUARANTOR");
//            }
//            if (CommonUtil.convertObjToInt(txtNoOfInstallments.getText()) > 0) {
//                whereMap.put("NO_OF_INSTALLMENTS", txtNoOfInstallments.getText());
//            }
//            //Added By Suresh
//            if (chkPrized.isSelected() == true) {
//                whereMap.put("PRIZED", "PRIZED");
//            }
//            if (chkNonPrized.isSelected() == true) {
//                whereMap.put("NON_PRIZED", "NON_PRIZED");
//            }
//            if (tdtOverDueDate.getDateValue() != null && tdtOverDueDate.getDateValue().length() > 0) {
//                whereMap.put("OVER_DUE_DT", getProperDate(DateUtil.getDateMMDDYYYY(tdtOverDueDate.getDateValue())));
//            }
//            whereMap.put("NOTICE_TYPE", cboNoticeType.getSelectedItem());
//            whereMap.put("TODAY_DT", currDt);
//            whereMap.put("CURR_DATE", currDt);
//            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
//            try {
//                log.info("populateData...");
//                observable.removeRowsFromGuarantorTable(tblGuarantorData);
//                ArrayList heading = observable.populateData(viewMap, tblData);
//                txtNoticeCharge.setText(observable.getTxtNoticeCharge());
//                txtPostageCharge.setText(observable.getTxtPostageCharge());
//                lblNoOfRecordsVal.setText(String.valueOf(tblData.getRowCount()));
//                if (tblData.getRowCount() > 0) {
//                    btnsms.setVisible(true);
//                    btnsms.setEnabled(true);
//                } else {
//                    btnsms.setVisible(false);
//                }
//                heading = null;
//            } catch (Exception e) {
//                System.err.println("Exception " + e.toString() + "Caught");
//                e.printStackTrace();
//            }
//        }
//        viewMap = null;
//        whereMap = null;
    }

//     private String validateAccNo(){
//        String from = txtFromAccountno.getText();
//        String to = txtTOAccountno.getText();
//        String message = "";
//        if(!(from.equalsIgnoreCase("")|| to.equalsIgnoreCase(""))){
//            if(from.compareTo(to) > 0){
////                message = resourceBundle.getString("ACCOUNTWARNING");
//            }
//        }
//        if(((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString().equals("TL") || ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString().equals("AD"))
//        {
//            HashMap hash=new HashMap();
//         hash.put(PROD_ID,((ComboBoxModel)cboProdId.getModel()).getKeyForSelected());
//         hash.put("ACT_NUM",txtFromAccount.getText());
//         if(txtTOAccountno !=null && (! txtTOAccountno.getText().equals(""))) {
//             hash.put("ACT_NUM",txtTOAccountno.getText());
//         }
//         hash.put("SELECTED_BRANCH",ProxyParameters.BRANCH_ID);
//         List actlst=ClientUtil.executeQuery("getActNotCLOSEDTL",hash);
//         if(actlst != null &&  actlst.size()>0 ){
//         } else{
//             ClientUtil.displayAlert("Enter the correct Number");
//             txtFromAccountno.setText("");
//             txtTOAccountno.setText("");
//             return message;
//         }
//        }
//        return message;
//    }
    public void generateNotice() {
        //        updateOBFields();
//        HashMap viewMap = new HashMap();
//        HashMap whereMap = new HashMap();
//        String param1 = observable.getSelected();
////        String param2 = observable.getSelectCharges();
////        String prodType = String.valueOf(cboProdType.getSelectedItem());
//        if (param1.length() > 0) {
//            int yesNo = 0;
//            String[] options = {"Yes", "No"};
//            yesNo = COptionPane.showOptionDialog(null, "Generate Notice?", CommonConstants.WARNINGTITLE,
//                    COptionPane.YES_NO_OPTION, COptionPane.QUESTION_MESSAGE,
//                    null, options, options[0]);
//            System.out.println("#$#$$ yesNo : " + yesNo);
////            if (yesNo==0) { //GENERATE NOTICE APPLIED MAY BE ARE MAY NOT BE BUT POST CHARGE CAN APPLY
//            boolean selected = false;
//            accountNumberMap = new HashMap();
//            guarantorMemberMap = new HashMap();
//            accountChargeMap = new HashMap();
//            java.util.Map guarantorMap = observable.getGuarantorMap();
//            ArrayList totalList = null;
//            ArrayList rowList = null;
//            String actNum;
//            ArrayList tempList = null;
//            observable.getTableModel(tblData);
//            for (int i = 0, j = tblData.getRowCount(); i < j; i++) {
//                selected = ((Boolean) tblData.getValueAt(i, 0)).booleanValue();
//                if (selected) {
//                    tempList = new ArrayList();
//                    tempList.add((ArrayList) observable.getTableModel(tblData).get(i));
////                    if (prodType.equals("MDS")) {
////                        actNum = String.valueOf(tblData.getValueAt(i, 2));
////                    } else {
////                        actNum = String.valueOf(tblData.getValueAt(i, 1));
////                    }
////                    accountNumberMap.put(actNum, null);
////                    if (guarantorMap != null && guarantorMap.size() > 0) {
////                        totalList = (ArrayList) guarantorMap.get(actNum);
////                        if (totalList != null && totalList.size() > 0) {
////                            for (int g = 0; g < totalList.size(); g++) {
////                                rowList = (ArrayList) totalList.get(g);
////                                selected = ((Boolean) rowList.get(0)).booleanValue();
////                                if (selected) {
////                                    tempList.add(rowList);
////                                    guarantorMemberMap.put(actNum + rowList.get(3), null);
////                                }
////                            }
////                        }
////                    }
//                    accountChargeMap.put(actNum, tempList);
//                }
//            }
//
//            ttIntegration = null;
//            HashMap paramMap = new HashMap();
//            paramMap.put("BranchCode", ProxyParameters.BRANCH_ID);
//            paramMap.put("ProductId", observable.getCbmProdId().getKeyForSelected());
//            paramMap.put("CURR_DATE", currDt);
//            paramMap.put("Param1", param1);
////                paramMap.put("param3", param2);
//            paramMap.put("Param2", guarantorGetSelected());
//            if (CommonUtil.convertObjToStr(txtFromAccountno.getText()).length() > 0) {
//                paramMap.put("FROM_ACCT_NUM", txtFromAccountno.getText());
//            }
//
//            if (CommonUtil.convertObjToStr(txtTOAccountno.getText()).length() > 0) {
//                paramMap.put("TO_ACCT_NUM", txtTOAccountno.getText());
//            }
//            if (CommonUtil.convertObjToStr(txtLastNoticeSentBefore.getText()).length() > 0) {
//                Date lastNoticeDate = (Date) currDt.clone();
//                lastNoticeDate = DateUtil.addDays(lastNoticeDate, -CommonUtil.convertObjToInt(txtLastNoticeSentBefore.getText()));
//                paramMap.put("LAST_NOTICE_SENT_DT", lastNoticeDate);
//            }
//            //Added By Suresh
//            if (tdtOverDueDate.getDateValue() != null && tdtOverDueDate.getDateValue().length() > 0) {
//                paramMap.put("OverDueDt", getProperDate(DateUtil.getDateMMDDYYYY(tdtOverDueDate.getDateValue())));
//            } else {
//                paramMap.put("OverDueDt", currDt);
//            }
//            String noticeType = CommonUtil.convertObjToStr(cboNoticeType.getSelectedItem());
//            if (prodType.equals("GOLD_LOAN")) {
//                if (noticeType.equals("Auction Notice")) {
//                    paramMap.put("AuctionDt", getProperDate(DateUtil.getDateMMDDYYYY(tdtAuctionDate.getDateValue())));
//                }
//            }
//            ttIntegration.setParam(paramMap);
//            generateNotice = true;
//            //  String noticeType = CommonUtil.convertObjToStr(cboNoticeType.getSelectedItem());
//            if (bankName.lastIndexOf("MAHILA") != -1) {
//                String reportName = "LoanNoticeFirst";
//                if (noticeType.equals("First Notice")) {
//                    reportName = "LoanNoticeFirst";
//                } else if (noticeType.equals("Second Notice")) {
//                    reportName = "LoanNoticeSecond";
//                } else if (noticeType.equals("Third Notice")) {
//                    reportName = "LoanNoticeThird";
//                }
//                if (yesNo == 0) {
//                    ttIntegration.integrationForPrint(reportName, true);
//                }
//            } else {
//                String reportName = "";
//
//                if (prodType.equals("GOLD_LOAN")) {
//                    if (noticeType.equals("Demand Notice")) {
//                        reportName = "GoldLoanNoticeDemand";
//                    } else if (noticeType.equals("Ordinary Notice")) {
//                        reportName = "GoldLoanNoticeFirst";
//                    } else if (noticeType.equals("Registered Notice")) {
//                        reportName = "GoldLoanNoticeSecond";
//                    } else if (noticeType.equals("Auction Notice")) {
//                        reportName = "GoldLoanNoticeThird";
//                    }
//                } else {
//
//                    if (prodType.equals("MDS")) {
//                        reportName = "MDSNoticeFirst";
//                    } else {
//                        reportName = "LoanNoticeFirst";
//                    }
//                    if (noticeType.equals("Demand Notice")) {
//                        if (prodType.equals("MDS")) {
//                            reportName = "MDSNoticeDemand";
//                        } else {
//                            reportName = "LoanNoticeDemand";
//                        }
//                    } else if (noticeType.equals("Ordinary Notice")) {
//                        if (prodType.equals("MDS")) {
//                            reportName = "MDSNoticeFirst";
//                        } else {
//                            reportName = "LoanNoticeFirst";
//                        }
//                    } else if (noticeType.equals("Registered Notice")) {
//                        if (prodType.equals("MDS")) {
//                            reportName = "MDSNoticeSecond";
//                        } else {
//                            reportName = "LoanNoticeSecond";
//                        }
//                    } else if (noticeType.equals("Auction Notice")) {
//                        if (prodType.equals("MDS")) {
//                            reportName = "MDSNoticeThird";
//                        } else {
//                            reportName = "LoanNoticeThird";
//                        }
//                    }
//                }
//                if (yesNo == 0) {
//                    ttIntegration.integrationForPrint(reportName, false);
//                }
//            }
////            }
//
//        } else {
//            generateNotice = false;
//            ClientUtil.displayAlert("No Records found...");
//        }
//        viewMap = null;
//        whereMap = null;
    }

    public String guarantorGetSelected() {
//        Boolean bln;
//        ArrayList arrRow;
//        HashMap selectedMap;
        //        ArrayList selectedList = new ArrayList();
        String selected = "";
        Object obj[] = guarantorMemberMap.keySet().toArray();
        for (int i = 0, j = obj.length; i < j; i++) {
            selected += "'" + obj[i];
            selected += "',";
        }
        // If no guarantor selected also records should be selected from other than guarantor.
        selected = selected.equals("") ? "'aa'" : selected.substring(0, selected.length() - 1);
        System.out.println("#$#$ guaranter selected : " + selected);
        return selected;
    }

    private void createCboChargeTyp() {
        cboChargeType.setModel(observable.getCbmChargeType());
        cboChargeType.addItem("Risk Fund");
    }

    private void createCboProdType() {
        if (((String) TrueTransactMain.BANKINFO.get("BANK_NAME")).toUpperCase().lastIndexOf("MAHILA") != -1) {
            cboProdName.addItem("");
            cboProdName.addItem("Gold Loans");
            cboProdName.addItem("Other Loans");
        } else {
            cboProdName.addItem("");
            //Added By Suresh
            HashMap whereMap = new HashMap();
            List loanProductLst = ClientUtil.executeQuery("getSelectLoanProducts", whereMap);
            if (loanProductLst != null && loanProductLst.size() > 0) {
                for (int i = 0; i < loanProductLst.size(); i++) {
                    whereMap = (HashMap) loanProductLst.get(i);
                    String product_type = CommonUtil.convertObjToStr(whereMap.get("PROD_TYPE"));
                    cboProdName.addItem(product_type);
                }

            }
            cboProdName.addItem("MDS");
            cboProdName.addItem("ALL_PRODUCT");
//            cboProdType.addItem("Gold Loans");
//            cboProdType.addItem("Other Loans");
//            cboProdType.addItem("MDS Loans");
//            cboProdType.addItem("MDS");
        }
    }

    private void creatComboBranch() {
        cboBranchCode.setModel(observable.getCbmbranch());
    }

    private Date getProperDate(Date sourceDate) {
        Date targetDate = (Date) currDt.clone();
        targetDate.setDate(sourceDate.getDate());
        targetDate.setMonth(sourceDate.getMonth());
        targetDate.setYear(sourceDate.getYear());
        return targetDate;
    }

    private ComboBoxModel getListModel() {
        ComboBoxModel listData = new ComboBoxModel();
        return listData;
    }

    public void valueChanged(javax.swing.event.ListSelectionEvent e) {
        // Add your handling code here://GEN-FIRST:event_btnCancelActionPerformed
//        if ( < 0) {
//            COptionPane.showMessageDialog(null,
//                resourceBundle.getString("SelectRow"), 
//                resourceBundle.getString("SelectRowHeading"),
//                COptionPane.OK_OPTION + COptionPane.INFORMATION_MESSAGE);
//        } 
//        if (parent != null) parent.setModified(false);
//        this.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void updateDBStatus(String status) {
        //observable.updateStatus(paramMap, status);
        HashMap screenParamMap = new HashMap();
//        screenParamMap.put(CommonConstants.AUTHORIZEDATA, observable.getSelected());
        screenParamMap.put(CommonConstants.AUTHORIZESTATUS, status);

//        observable.insertCharges(paramMap);
        ClientUtil.showMessageWindow(observable.getResult());
        if (observable.getResult().equals(ClientConstants.RESULT_STATUS[4])) {
//            observable.removeRowsFromGuarantorTable(tblData);
//            observable.removeRowsFromGuarantorTable(tblGuarantorData);
        }

    }

    private void cboProdNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdNameActionPerformed
        // TODO add your handling code here:
        if (cboProdName.getSelectedIndex() > 0) {
            String prodType = String.valueOf(cboProdName.getSelectedItem());
            System.out.println("prodType===" + prodType);
             if( cboProdName.getSelectedItem().equals("MDS") && (cboChargeType.getSelectedItem().equals("Arbitrary Charges")||cboChargeType.getSelectedItem().equals("EP Cost")||
                    cboChargeType.getSelectedItem().equals("Execution Decree Charges")||cboChargeType.getSelectedItem().equals("Notice Charges")
                     ||cboChargeType.getSelectedItem().equals("Postage Charges")||cboChargeType.getSelectedItem().equals("Risk Fund"))){
                ClientUtil.showMessageWindow("Please Select any one of charge type as insurance charges, leagal charges, other charges, miscellaneous charges...");
                cboProdName.setSelectedItem("");
                cboProdId.setSelectedItem("");
                return;
            }
            if (prodType.length() > 0) {
                observable.fillDropDown1(prodType);
                cboProdId.setModel(observable.getCbmProdId());
            }
            txtFromAccountno.setText("");
            txtTOAccountno.setText("");
       }


    }//GEN-LAST:event_cboProdNameActionPerformed

    private void txtAccNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAccNoActionPerformed
        // TODO add your handling code here:
        //        if(txtAccHdId.getText()!=null && !txtAccHdId.getText().equalsIgnoreCase("")){
        //         HashMap hmap = new HashMap();
        //         String customerAllow="";
        //            hmap.put("ACHEAD", txtAccHdId.getText());
        //            List list = ClientUtil.executeQuery("getCustomerAlloowProperty", hmap);
        //            if (list != null && list.size() > 0) {
        //                hmap = (HashMap) list.get(0);
        //                customerAllow = CommonUtil.convertObjToStr(hmap.get("ALLOW_CUSTOMER_ACNUM"));
        //              //  hoAc = CommonUtil.convertObjToStr(hmap.get("HO_ACCT"));
        //            }
        ////            if (bankType.equals("DCCB")) {
        ////                if (hoAc.equals("Y")) {
        ////                    btnOrgOrResp.setVisible(true);
        ////                    observable.setHoAccount(true);
        ////                } else {
        ////                    observable.setHoAccount(false);
        ////                    btnOrgOrResp.setVisible(false);
        ////                }
        ////            }
        //            System.out.println("customerAllow>>>>"+customerAllow);
        //        if (customerAllow.equals("Y")) {
        //                System.out.println("innnnn");
        //                CInternalFrame frm = new CInternalFrame();
        //                frm = new com.see.truetransact.ui.transaction.cash.GLAccountNumberListUI(this);
        //                frm.setSelectedBranchID(getSelectedBranchID());
        //               //frm.setSize(1000,1000);
        //                TrueTransactMain.showScreen(frm);
        //
        ////               final CInternalFrame frame = new CInternalFrame();
        ////               CDesktopPane desktop = new CDesktopPane();
        ////               glAccountNumberListUI=new GLAccountNumberListUI();
        ////        //frame.setLocation(xOffset * openFrameCount, yOffset * openFrameCount);
        ////        frame.setSize(200, 100);
        ////        frame.setVisible(true);
        ////        frame.getContentPane().add(glAccountNumberListUI);
        ////        desktop.add(frame);
        //
        ////                GLAccountNumberListUI glAccNo = new GLAccountNumberListUI();
        ////                glAccNo.show();
        ////                glAccNo.setVisible(true);
        ////                String AccNo = COptionPane.showInputDialog(this, "Enter Acc no");
        ////                hmap.put("ACC_NUM", AccNo);
        ////                List chkList = ClientUtil.executeQuery("checkAccStatus", hmap);
        ////                if (chkList != null && chkList.size() > 0) {
        ////                    hmap = (HashMap) chkList.get(0);
        ////                    observable.setLblAccName(CommonUtil.convertObjToStr(hmap.get("NAME")));
        ////                    observable.setTxtAccNo(AccNo);
        ////                    observable.setClosedAccNo(AccNo);
        ////                } else {
        ////                    ClientUtil.displayAlert("Invalid Account number");
        ////                    txtAccHdId.setText("");
        ////                    return;
        ////                }
        //            }
        //        }
    }//GEN-LAST:event_txtAccNoActionPerformed

    private Date setProperDtFormat(Date dt) {
        Date tempDt = (Date) currDt.clone();
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }

    public Date getProperDateFormat(Object obj) {
        Date currDate = null;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            //System.out.println("currDt in properdateformat>>>>>" + currDt);
            currDate = (Date) currDt.clone();
            currDate.setDate(tempDt.getDate());
            currDate.setMonth(tempDt.getMonth());
            currDate.setYear(tempDt.getYear());
        }
        return currDate;
    }
    private void txtAccNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccNoFocusLost
        // TODO add your handling code here:
        if (txtAccNo.getText() != null && !txtAccNo.getText().equalsIgnoreCase("")) {
            HashMap hmap = new HashMap();
            String customerAllow = "";
            hmap.put("ACHEAD", txtAccNo.getText());
            List list = ClientUtil.executeQuery("getCustomerAlloowProperty", hmap);
            if (list != null && list.size() > 0) {
                hmap = (HashMap) list.get(0);
                customerAllow = CommonUtil.convertObjToStr(hmap.get("ALLOW_CUSTOMER_ACNUM"));
                //  hoAc = CommonUtil.convertObjToStr(hmap.get("HO_ACCT"));
            }
            //            if (bankType.equals("DCCB")) {
            //                if (hoAc.equals("Y")) {
            //                    btnOrgOrResp.setVisible(true);
            //                    observable.setHoAccount(true);
            //                } else {
            //                    observable.setHoAccount(false);
            //                    btnOrgOrResp.setVisible(false);
            //                }
            //            }
            System.out.println("customerAllow>>>>" + customerAllow);
            if (customerAllow.equals("Y")) {
                System.out.println("innnnn");
                CInternalFrame frm = new CInternalFrame();
//                frm = new com.see.truetransact.ui.transaction.cash.GLAccountNumberListUI(this);
                frm.setSelectedBranchID(getSelectedBranchID());
                //frm.setSize(1000,1000);
                TrueTransactMain.showScreen(frm);

                //               final CInternalFrame frame = new CInternalFrame();
                //               CDesktopPane desktop = new CDesktopPane();
                //               glAccountNumberListUI=new GLAccountNumberListUI();
                //        //frame.setLocation(xOffset * openFrameCount, yOffset * openFrameCount);
                //        frame.setSize(200, 100);
                //        frame.setVisible(true);
                //        frame.getContentPane().add(glAccountNumberListUI);
                //        desktop.add(frame);

                //                GLAccountNumberListUI glAccNo = new GLAccountNumberListUI();
                //                glAccNo.show();
                //                glAccNo.setVisible(true);
                //                String AccNo = COptionPane.showInputDialog(this, "Enter Acc no");
                //                hmap.put("ACC_NUM", AccNo);
                //                List chkList = ClientUtil.executeQuery("checkAccStatus", hmap);
                //                if (chkList != null && chkList.size() > 0) {
                //                    hmap = (HashMap) chkList.get(0);
                //                    observable.setLblAccName(CommonUtil.convertObjToStr(hmap.get("NAME")));
                //                    observable.setTxtAccNo(AccNo);
                //                    observable.setClosedAccNo(AccNo);
                //                } else {
                //                    ClientUtil.displayAlert("Invalid Account number");
                //                    txtAccHdId.setText("");
                //                    return;
                //                }
            }
        }
    }//GEN-LAST:event_txtAccNoFocusLost

    private void btnAccNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccNoActionPerformed
        popUp(ACCNO);
//            termLoanDetailsFlag = false;
//            termLoansDetailsMap = null;
    }//GEN-LAST:event_btnAccNoActionPerformed

    private void txtAccHdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAccHdActionPerformed
        //        // TODO add your handling code here:
        //        HashMap hash = new HashMap();
        //        String ACCOUNTNO = (String) txtAccNo.getText();
        //        if( observable.getProdType().equals("TD")){
        //            if (ACCOUNTNO.lastIndexOf("_")!=-1){
        //                hash.put("ACCOUNTNO", txtAccNo.getText());
        //            }else
        //                hash.put("ACCOUNTNO", txtAccNo.getText()+"_1");
        //        }else{
        //            hash.put("ACCOUNTNO", txtAccNo.getText());
        //        }
        //        hash.put("ACT_NUM", hash.get("ACCOUNTNO"));
        //        hash.put("PROD_ID", ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
        //        hash.put("SELECTED_BRANCH",ProxyParameters.BRANCH_ID);
        //        List actlst=null;
        //        List lst=null;
        //        HashMap notClosedMap = new HashMap();
        //        if( observable.getProdType().equals("TD")){
        //            actlst=ClientUtil.executeQuery("getNotClosedDeposits",hash);
        //            if(actlst!=null && actlst.size()>0){
        //                notClosedMap =(HashMap)actlst.get(0);
        //            }
        //        }
        //
        //        if( observable.getProdType().equals("TL"))
        //            actlst=ClientUtil.executeQuery("getActNotCLOSEDTL",hash);
        //
        //        if( observable.getProdType().equals("OA"))
        //            observable.setAccountName(ACCOUNTNO);
        //
        //        if(observable.getProdType().equals("TD") || observable.getProdType().equals("TL")){
        //            if(rdoTransactionType_Debit.isSelected() || rdoTransactionType_Credit.isSelected()){
        //                if(observable.getProdType().equals("TL")){
        //                    if(actlst!=null && actlst.size()>0){
        //                        viewType = ACCNO;
        //                        updateOBFields();
        //                        hash.put("PROD_ID", ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
        //                        hash.put("SELECTED_BRANCH",ProxyParameters.BRANCH_ID);
        //                        if( observable.getProdType().equals("TL")) {
        //                            if(rdoTransactionType_Debit.isSelected()) {
        //                                hash.put("PAYMENT","PAYMENT");
        //                                lst=ClientUtil.executeQuery("Cash.getAccountList"
        //                                + ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString(),hash);
        //                            }else if(rdoTransactionType_Credit.isSelected()){
        //                                if(observable.getProdType().equals("TL"))
        //                                    hash.put("RECEIPT","RECEIPT");
        //                                //system.out.println("hash"+hash);
        //                                lst=ClientUtil.executeQuery("Cash.getAccountList"
        //                                + ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString(),hash);
        //                            }
        //                            fillData(hash);
        //                        }
        //                    }else{
        //                        ClientUtil.showAlertWindow(" Invalid Number Choose correct number");
        //                        txtAccNo.setText("");
        ////                        txtAccNo.requestFocus();
        //                    }
        //                }else if(observable.getProdType().equals("TD")){
        //                    viewType = ACCNO;
        //                    updateOBFields();
        //                    hash.put("PROD_ID", ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
        //                    hash.put("SELECTED_BRANCH",ProxyParameters.BRANCH_ID);
        //                    if(actlst!=null && actlst.size()>0){
        //                        if(observable.getProdType().equals("TD")){
        //                            hash.put("RECEIPT","RECEIPT");
        //                            if(rdoTransactionType_Debit.isSelected()) {
        //                                //                                if(observable.getProdType().equals("TD")){
        //                                //                                    hash.put("PAYMENT","PAYMENT");
        //                                //                                    lst=ClientUtil.executeQuery("Cash.getAccountList"
        //                                //                                    + ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString(),hash);
        //                                //                                }else{
        //                                lst=ClientUtil.executeQuery("getDepositHoldersInterest",hash);
        //                                transDetails.setIsDebitSelect(true);
        //                            }else if(rdoTransactionType_Credit.isSelected()){
        //                                lst=ClientUtil.executeQuery("Cash.getAccountList"
        //                                + ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString(),hash);
        //                            }
        //                            hash.put("PRODUCTTYPE",notClosedMap.get("BEHAVES_LIKE"));
        //                            hash.put("TYPE",notClosedMap.get("BEHAVES_LIKE"));
        //                            hash.put("AMOUNT",notClosedMap.get("DEPOSIT_AMT"));
        //                            fillData(hash);
        //                        }
        //                    }else{
        //                        ClientUtil.showAlertWindow(" Invalid Number Choose correct number");
        //                        txtAccNo.setText("");
        //                    }
        //                }
        //            }else{
        //                ClientUtil.showMessageWindow("Select Payment or Receipt ");
        //                txtAccNo.setText("");
        ////                txtAccNo.requestFocus();
        //                return;
        //            }
        //        }else if(observable.getProdType().equals("OA")){
        //            viewType = ACCNO;
        //            HashMap listMap = new HashMap();
        //            if(observable.getLblAccName().length()>0){
        //                updateOBFields();
        //                hash.put("PROD_ID", ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
        //                hash.put("SELECTED_BRANCH",ProxyParameters.BRANCH_ID);
        //                lst=ClientUtil.executeQuery("Cash.getAccountList"
        //                + ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString(),hash);
        //                fillData(hash);
        //                observable.setLblAccName("");
        //            }else{
        //                ClientUtil.showAlertWindow(" Invalid Number Choose correct number");
        //                txtAccNo.setText("");
        //            }
        //        }
    }//GEN-LAST:event_txtAccHdActionPerformed

    private void txtAccHdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccHdFocusLost
        // TODO add your handling code here:
//        txtAccNoActionPerformed();
//        if(rdoTransactionType_Debit.isSelected()){
//            cboInstrumentType.setEnabled(true);
//            txtInstrumentNo1.setEnabled(true);
//            txtInstrumentNo2.setEnabled(true);
//            tdtInstrumentDate.setEnabled(true);
//            txtParticulars.setText("To");
//            cboInstrumentType.setSelectedIndex(1);
//            instrumentTypeFocus();
//        }
    }//GEN-LAST:event_txtAccHdFocusLost

    private void btnAccHdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccHdActionPerformed
        viewType = ACCTHDID;
        final HashMap viewMap = new HashMap();
        viewMap.put(CommonConstants.MAP_NAME, "Cash.getSelectAcctHead");
        new ViewAll(this, viewMap).show();
    }//GEN-LAST:event_btnAccHdActionPerformed
       
    public void LoanChargeTableData(HashMap map) {
        // model=new javax.swing.table.DefaultTableModel();

        tblData.setModel(new javax.swing.table.DefaultTableModel(
                setLoanChargeTableData(map),
                new String[]{
                    "Select", "Account No", "Loan Type", "Membership No", "Name", "Principle Due", "Balance", "Charge Amount","Narration"}) {

            Class[] types = new Class[]{
                java.lang.Boolean.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                true, false, false, false, false, false, false, true,true
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {               
                return canEdit[columnIndex];
            }
        });
        //setFont();
        //setSizeTableData();
        //setColour();
        //setUpComboBox(tblBalanceUpdate,tblBalanceUpdate.getColumnModel().getColumn(2));
        tblData.setCellSelectionEnabled(true);
        tblData.addPropertyChangeListener(new java.beans.PropertyChangeListener() {        
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                //  tblBalanceUpdatePropertyChange(evt);
            }
        });
        //setSizeTableData();
        setTableModelListener();
        //calcCharge();
    }
   
    
        public void RiskFundTableData(HashMap map) {
        // model=new javax.swing.table.DefaultTableModel();

        tblData.setModel(new javax.swing.table.DefaultTableModel(
                setRiskFundTableData(map),
                new String[]{
                   "Select", "Account No",  "Membership No", "Name", "Loan No", "Product ID", "Limit","Risk Fund","Narration"}) {

            Class[] types = new Class[]{
                java.lang.Boolean.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                true, false, false, false, false, false, false,true,true
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
//               if (columnIndex == 6) {
//                    tblData.getColumnModel().getColumn(columnIndex).setCellEditor(new DefaultCellEditor(txtNoOfInstallments));
//                    tblData.setColumnSelectionAllowed(true);
//                    return true;
//                }
               return canEdit[columnIndex];
            }
        });
        //setFont();
        //setSizeTableData();
        //setColour();
        //setUpComboBox(tblBalanceUpdate,tblBalanceUpdate.getColumnModel().getColumn(2));
        tblData.setCellSelectionEnabled(true);
        tblData.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                //  tblBalanceUpdatePropertyChange(evt);
            }
        });
        //setSizeTableData();
        setTableModelListener();
        calcRiskFund();
    }
        
    private void setTableModelListener() {
        try {
            tableModelListener = new TableModelListener() {

                public void tableChanged(TableModelEvent e) {
                    if (e.getType() == TableModelEvent.UPDATE) {
                        int row = e.getFirstRow();
                        int column = e.getColumn();
                        double recTotal = 0;
                        int RowCount = tblData.getRowCount();
                        System.out.println("row#####" + row);
                        System.out.println("column#####" + column);
                        if (column == 7 || column == 0) {
                            if (tblData.getValueAt(row, 7) != null && !tblData.getValueAt(row, 7).toString().equals("") && !isNumeric(tblData.getValueAt(row, 7).toString())) {
                                System.out.println("4356346436346====");
                                ClientUtil.showAlertWindow("Please enter Numeric Value!!!");
                                tblData.setValueAt("", row, 7);
                                return;
                            }
                            calcCharge() ;
                        }

                    }
                }
            };
            tblData.getModel().addTableModelListener(tableModelListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

        private Object[][] setRiskFundTableData(HashMap map) {
        HashMap whereMap = new HashMap();
        HashMap dataMap = new HashMap();
        //whereMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);   
        whereMap.putAll(map);
        List loanList = ClientUtil.executeQuery("getRiskFundDet", whereMap);
        if (loanList != null && loanList.size() > 0) {
            Object totalList[][] = new Object[loanList.size()][8];
            for (int j = 0; j < loanList.size(); j++) {
                dataMap = (HashMap) loanList.get(j);
                System.out.println("processMap####" + dataMap); 
                totalList[j][0] = new Boolean(false);
                totalList[j][1] = CommonUtil.convertObjToStr(dataMap.get("ACCT_NUM"));
                totalList[j][2] = CommonUtil.convertObjToStr(dataMap.get("MEMBERSHIP_NO"));
                totalList[j][3] = CommonUtil.convertObjToStr(dataMap.get("FNAME"));
                totalList[j][4] = CommonUtil.convertObjToStr(dataMap.get("LOANNO"));
                totalList[j][5] = CommonUtil.convertObjToStr(dataMap.get("PROD_ID"));
                totalList[j][6] = CommonUtil.convertObjToStr(CommonUtil.convertObjToDouble(dataMap.get("LIMIT")));
                totalList[j][7] = CommonUtil.convertObjToStr(CommonUtil.convertObjToDouble(dataMap.get("RISK_FUND")));
            }
            observable.setFinalList(loanList);
            return totalList;
        } else {
            ClientUtil.displayAlert("No Data!!! ");

        }
        return null;
    }
        
    private Object[][] setLoanChargeTableData(HashMap map) {
        HashMap whereMap = new HashMap();
        HashMap dataMap = new HashMap();
        //whereMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);   
        whereMap.putAll(map);
        List loanList = null;
        if (whereMap.containsKey("BEHAVES_LIKE")) {
            if (whereMap.get("BEHAVES_LIKE").equals("OD")) {
            //loanList = ClientUtil.executeQuery("getAccountsForTeramLoanCharge", whereMap);
              loanList = ClientUtil.executeQuery("getAccountsForAdvanceCharge", whereMap); //Zoho - 74846
            } else {
                loanList = ClientUtil.executeQuery("getAccountsForTeramLoanCharge", whereMap);
            }
        }else{
            loanList = ClientUtil.executeQuery("getMDS_AccountList", whereMap);
        }
        //List loanList = ClientUtil.executeQuery("getGroupLoanTableDetails", whereMap);        
        if (loanList != null && loanList.size() > 0) {
            Object totalList[][] = new Object[loanList.size()][9];
            for (int j = 0; j < loanList.size(); j++) {
                dataMap = (HashMap) loanList.get(j);
                totalList[j][0] = new Boolean(false);
                totalList[j][1] = CommonUtil.convertObjToStr(dataMap.get("ACT_NUM"));
                totalList[j][2] = CommonUtil.convertObjToStr(dataMap.get("TYPE"));
                totalList[j][3] = CommonUtil.convertObjToStr(dataMap.get("MEM_NO"));
                totalList[j][4] = CommonUtil.convertObjToStr(dataMap.get("NAME"));
                totalList[j][5] = CommonUtil.convertObjToStr(CommonUtil.convertObjToDouble(dataMap.get("PRINCIPAL_DUE")));
                totalList[j][6] = CommonUtil.convertObjToStr(CommonUtil.convertObjToDouble(dataMap.get("BALANCE")));
                totalList[j][7] = CommonUtil.convertObjToStr("0");
            }
            observable.setFinalList(loanList);
            return totalList;
        } else {
            ClientUtil.displayAlert("No Data!!! ");

        }
        return null;
    }

    private void btnProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessActionPerformed
        // TODO add your handling code here:
        try {
            if(cboChargeType.getSelectedItem().equals("Risk Fund")) {
                if (tdtFromDate.getDateValue() != null && tdtFromDate.getDateValue().length() > 0 && (tdtToDate.getDateValue() != null && tdtToDate.getDateValue().length() > 0)) {
                
                }else{
                    ClientUtil.showMessageWindow("Please select from Date and To date for Risk fund details!!!");
                    return;
                }
             
            }
            if(cboChargeType.getSelectedItem().equals("")) {
                ClientUtil.showMessageWindow("Please select any charge types!!!");
                return;
            }
            if(cboProdName.getSelectedItem().equals("")) {
                ClientUtil.showMessageWindow("Please select any products!!!");
                return;
            }
            if(cboProdId.getSelectedItem().equals("")) {
                ClientUtil.showMessageWindow("Please select any product id!!!");
                return;
            }
            String prod_id = (((ComboBoxModel) (cboProdId).getModel()).getKeyForSelected()).toString();
            HashMap mapRisk = new HashMap();
            HashMap whereMap = new HashMap();
            String behavesLike = "";
//            System.out.println("mapRisk "+mapRisk);
            if (cboChargeType.getSelectedItem().equals("Risk Fund")) {
                mapRisk.put(CommonConstants.MAP_NAME, "getRiskFundDet");
                observable.setCboChargeType("Risk Fund");
            }else  if (cboProdName.getSelectedItem().equals("MDS")) {
                observable.setCboChargeType(CommonUtil.convertObjToStr(observable.getCbmChargeType().getKeyForSelected()));
                mapRisk.put(CommonConstants.MAP_NAME, "getMDS_AccountList");
                System.out.println("mapRisk in "+mapRisk);
            }
            else {
                    observable.setCboChargeType(CommonUtil.convertObjToStr(observable.getCbmChargeType().getKeyForSelected()));
                    whereMap.put("prodId", prod_id);
                    List lst = ClientUtil.executeQuery("TermLoan.getProdHead", whereMap);
                    if (lst != null && lst.size() > 0) {
                        whereMap = (HashMap) lst.get(0);
                        behavesLike = CommonUtil.convertObjToStr(whereMap.get("BEHAVES_LIKE"));                   
                    }
                    if (behavesLike.equals("OD")) {
                        mapRisk.put("BEHAVES_LIKE", "OD");
                        observable.setDebitProdType("AD");
                    } else {
                        mapRisk.put("BEHAVES_LIKE", "TL");
                        observable.setDebitProdType("TL");
                    }
                }
            
            if (prod_id == null || prod_id.equals("")) {
                System.out.println("hiiii");
                mapRisk.put("PROD_ID", "ALL_PRODUCT");
            } else {
                mapRisk.put("PROD_ID", prod_id);
            }

            if (tdtFromDate.getDateValue() != null && tdtFromDate.getDateValue().length() > 0) {
                mapRisk.put("FROM_DATE", DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue()));
            }
            if (tdtToDate.getDateValue() != null && tdtToDate.getDateValue().length() > 0) {
                mapRisk.put("TO_DT", getProperDate(DateUtil.getDateMMDDYYYY(tdtToDate.getDateValue())));
            } else {
                if (tdtFromDate.getDateValue() != null && tdtFromDate.getDateValue().length() > 0) {
                    mapRisk.put("TO_DT", getProperDate(currDt));
                }
            }
            if (CommonUtil.convertObjToStr(txtFromAccountno.getText()).length() > 0) {
                mapRisk.put("FROM_ACCT_NUM", txtFromAccountno.getText());
            }

            if (CommonUtil.convertObjToStr(txtTOAccountno.getText()).length() > 0) {
                mapRisk.put("TO_ACCT_NUM", txtTOAccountno.getText());
            }else{
                if (CommonUtil.convertObjToStr(txtFromAccountno.getText()).length() > 0) {
                        txtTOAccountno.setText(txtFromAccountno.getText());
                        mapRisk.put("TO_ACCT_NUM", txtTOAccountno.getText());
                }
            }
            if (chkFulldue.isSelected()) {
                mapRisk.put("FULL_DUE", "FULL_DUE");
            }
            if (CommonUtil.convertObjToInt(txtNoOfInstallments.getText()) > 0) {
                mapRisk.put("NO_OF_INSTALLMENTS", CommonUtil.convertObjToInt(txtNoOfInstallments.getText()));
            }
            if (!observable.getCbmbranch().getKeyForSelected().equals("") && observable.getCbmbranch().getKeyForSelected()!=null) {
                mapRisk.put("BRANCH_CODE", observable.getCbmbranch().getKeyForSelected());
            }else{
                mapRisk.put("BRANCH_CODE", ProxyParameters.BRANCH_ID); 
            }            
            System.out.println("mapRisk$^$^^$^$^$^$^$^  " + observable.getCboChargeType());
            if (observable.getCboChargeType() != null && observable.getCboChargeType().equalsIgnoreCase("Risk Fund")) {
                RiskFundTableData(mapRisk);
            } else {
                LoanChargeTableData(mapRisk);
            }
            selectMode = true;
            chkSelectAll.setEnabled(true);
            txtChrgAmt.setEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnProcessActionPerformed

    private void cboProdTypeCrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdTypeCrActionPerformed
        // TODO add your handling code here:
        //cboProdIdCr.setSelectedIndex(0);
        txtAccHd.setText("");
        txtAccNo.setText("");
        lblActHeadDesc.setText("");
        if (cboProdTypeCr.getSelectedIndex() > 0) {
            String prodTypeCr = (((ComboBoxModel) cboProdTypeCr.getModel()).getKeyForSelected()).toString();
            System.out.println("prodtyp cr=====" + prodTypeCr);
            if (!prodTypeCr.equals("AB")) {
                txtAccNo.setText(CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID));
            }
            observable.setProdType(prodTypeCr);
//         if(prodType.equals("GL"))
//        {
//           cboProdIdCr.setEnabled(false); 
//           txtAccNo.setEnabled(false);
//           btnAccNo.setEnabled(false);
//           
//        }
            if (prodTypeCr.equals("GL")) {
                if (TrueTransactMain.BRANCH_ID.equals(CommonUtil.convertObjToStr(TrueTransactMain.selBranch))) {
                    //                observable.setCbmProdId(prodType);
                    //                cboProdId.setModel(observable.getCbmProdId());
                    cboProdIdCr.setEnabled(false);
                    txtAccNo.setEnabled(false);
                    // btnAccNo.setEnabled(false);
                    txtAccNo.setText("");
                    btnAccHd.setEnabled(true);
                    setProdEnable(false);
                    cboProdId.setEnabled(true);
                    cboProdIdCr.setSelectedItem("");
                } else {
                    ClientUtil.displayAlert("InterBranch Transactions Not Allowed For GL");
                    //observable.resetForm();
                }
                txtAccHd.setEnabled(true);
                btnAccNo.setEnabled(false);
            } else {
//                if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW
//                        || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
//                    productBased();
//                }
                setProdEnable(true);
                observable.setCbmProdIdCr(prodTypeCr);
                cboProdIdCr.setModel(observable.getCbmProdIdCr());
//                observable.getCbmProdId().setKeyForSelected(observable.getCbmProdId().getDataForKey(observable.getCboProdId()));
//                txtAccHd.setEnabled(false);
//                btnAccNo.setEnabled(false);
                btnAccHd.setEnabled(true);
            }
            if (!prodTypeCr.equals("GL")) {
                txtAccHd.setEnabled(false);
                btnAccHd.setEnabled(false);
                btnAccNo.setEnabled(false);
                cboProdIdCr.setEnabled(true);
            }
            // btnViewTermLoanDetails.setEnabled(true);

        }
    }//GEN-LAST:event_cboProdTypeCrActionPerformed

    private void srcTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_srcTableMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_srcTableMouseClicked

private void cboProdIdCrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdIdCrActionPerformed
// TODO add your handling code here:
    txtAccNo.setText("");
    btnAccNo.setEnabled(true);
    if (cboProdIdCr.getSelectedIndex() > 0) {
        String prodType = ((ComboBoxModel) cboProdTypeCr.getModel()).getKeyForSelected().toString();
        if (!prodType.equals("GL")) {
            if (!(prodType.equals("GL") || prodType.equals("AB"))) {
                String prodId = "";
                prodId = ((ComboBoxModel) cboProdIdCr.getModel()).getKeyForSelected().toString();
                //txtAccNo.setText(CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID) + prodId);
            }
//        if(!prodType.equals("AB"))
//        {
//          txtAccNo.setText(CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID));
//        }
            System.out.println("prodType====" + prodType);
            observable.setProdType(prodType);
            // if (observable.getCboProdId().length() > 0) {
            observable.setAccountHead();
            txtAccHd.setText(observable.getTxtAccHd());
            lblActHeadDesc.setText(observable.getLblActHeadDesc());
            //  }
        }

    }
}//GEN-LAST:event_cboProdIdCrActionPerformed

private void txtFromAccountnoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFromAccountnoFocusLost
    // TODO add your handling code here:
}//GEN-LAST:event_txtFromAccountnoFocusLost

private void btnFromAccountnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFromAccountnoActionPerformed
    // TODO add your handling code here:
    popUp(FROMACTNO);
}//GEN-LAST:event_btnFromAccountnoActionPerformed

private void txtTOAccountnoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTOAccountnoFocusLost
    // TODO add your handling code here:
}//GEN-LAST:event_txtTOAccountnoFocusLost

private void btnTOAccountnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTOAccountnoActionPerformed
    // TODO add your handling code here:
    popUp(TOACTNO);
}//GEN-LAST:event_btnTOAccountnoActionPerformed

private void chkFulldueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkFulldueActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_chkFulldueActionPerformed

private void tblDataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseClicked
    // TODO add your handling code here:
    //checkingPaymentAmount(selectedRow);
}//GEN-LAST:event_tblDataMouseClicked

private void tblDataMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseExited
    // TODO add your handling code here:
    //System.out.println("jdjfs777");
    //checkingPaymentAmount(selectedRow);
}//GEN-LAST:event_tblDataMouseExited

private void tblDataMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMousePressed
    // TODO add your handling code here:
    //System.out.println("jdjfs888");
    //if(observable.duePayYN(txtAcctNum.getText(),CommonUtil.convertObjToStr(tblSHGDetails.getValueAt(selectedRow, 1).toString()))){
    //   ClientUtil.showMessageWindow("Payment not allowed if due...");
    //   return;
    //}
    //checkingPaymentAmount(selectedRow);        
}//GEN-LAST:event_tblDataMousePressed

private void tblDataMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseReleased
    // TODO add your handling code here:
    //System.out.println("jdjfs555");
    ///if(observable.duePayYN(txtAcctNum.getText(),CommonUtil.convertObjToStr(tblSHGDetails.getValueAt(selectedRow, 1).toString()))){
    //   ClientUtil.showMessageWindow("Payment not allowed if due...");
    //   return;
    //}
    //checkingPaymentAmount(selectedRow);
}//GEN-LAST:event_tblDataMouseReleased

private void tblDataFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblDataFocusLost
    // TODO add your handling code here:
    //System.out.println("jdjfs666");
    //if(observable.duePayYN(txtAcctNum.getText(),CommonUtil.convertObjToStr(tblSHGDetails.getValueAt(selectedRow, 1).toString()))){
    //   ClientUtil.showMessageWindow("Payment not allowed if due...");
    //  return;
    //}
    //checkingPaymentAmount(selectedRow);
}//GEN-LAST:event_tblDataFocusLost

private void tblDataKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblDataKeyReleased
    // TODO add your handling code here:
    //System.out.println("jdjfs111");
    //if(observable.duePayYN(txtAcctNum.getText(),CommonUtil.convertObjToStr(tblSHGDetails.getValueAt(selectedRow, 1).toString()))){
    //   ClientUtil.showMessageWindow("Payment not allowed if due...");
    //   return;
    //}
    //checkingPaymentAmount(selectedRow);
}//GEN-LAST:event_tblDataKeyReleased

private void tblDataKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblDataKeyTyped
    // TODO add your handling code here:
    //System.out.println("jdjfs222");
    //if(observable.duePayYN(txtAcctNum.getText(),CommonUtil.convertObjToStr(tblSHGDetails.getValueAt(selectedRow, 1).toString()))){
    //   ClientUtil.showMessageWindow("Payment not allowed if due...");
    //  return;
    // }//
    //checkingPaymentAmount(selectedRow);
}//GEN-LAST:event_tblDataKeyTyped

private void cboChargeTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboChargeTypeActionPerformed
    cboProdName.setSelectedItem("");
    cboProdId.setSelectedItem("");
    txtFromAccountno.setText("");
    txtTOAccountno.setText("");
// TODO add your handling code here:
}//GEN-LAST:event_cboChargeTypeActionPerformed

    private void txtChrgAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtChrgAmtFocusLost
        // TODO add your handling code here:
        double chargeAmt = CommonUtil.convertObjToDouble(txtChrgAmt.getText());
        System.out.println("charge Amount :: " + chargeAmt);
        for(int i=0; i<tblData.getRowCount(); i++){
          tblData.setValueAt(chargeAmt, i, 7);  
        }
    }//GEN-LAST:event_txtChrgAmtFocusLost

    private void cboProdIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboProdIdActionPerformed

    private void chkNoTransActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkNoTransActionPerformed
        // TODO add your handling code here:
        if(chkNoTrans.isSelected()){
            panMultiSearch.setVisible(false);
        }else{
            panMultiSearch.setVisible(true);
        }
    }//GEN-LAST:event_chkNoTransActionPerformed

    private void setProdEnable(boolean isEnable) {
        cboProdId.setEnabled(isEnable);
        txtAccNo.setEnabled(isEnable);
        btnAccNo.setEnabled(isEnable);

        btnAccHd.setEnabled(!isEnable);


    }

    private void internationalize() {
//        lblSearch.setText(resourceBundle.getString("lblSearch"));
//        btnSearch.setText(resourceBundle.getString("btnSearch"));
//        chkCase.setText(resourceBundle.getString("chkCase"));
//        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
//        btnCancel.setText(resourceBundle.getString("btnCancel"));
    }

    public static boolean isNumeric(String str) {
        try {
            //Integer.parseInt(str);
            Float.parseFloat(str);
            //   System.out.println("ddd"+d);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean isLessThanZero(Double amount) {
        boolean flag = false;
        System.out.println("amount------------#" + amount);
        try {
            if (amount < 0) {
                flag = true;
            }
        } catch (Exception nfe) {
            nfe.printStackTrace();
        }
        System.out.println("falg 22222222222222" + flag);
        return flag;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        HashMap mapParam = new HashMap();

        HashMap whereMap = new HashMap();
        whereMap.put("USER_ID", "sysadmin1");
        //getSelectOperativeAcctProductAuthorizeTOList
        whereMap.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
        whereMap.put("OUTWARD_DT", ClientUtil.getCurrentDate());
        mapParam.put(CommonConstants.MAP_NAME, "getSelectOutwardClearingRealizeTOList");
        mapParam.put(CommonConstants.MAP_WHERE, whereMap);

        mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeOperativeAcctProduct");

//        AuthorizeUI authorizeUI = new AuthorizeUI(mapParam);
//        authorizeUI.setAuthorize(true);
//        authorizeUI.setException(false);
//        authorizeUI.setReject(false);
//        authorizeUI.setRealize(true);
//        authorizeUI.show();
    }

    public void update(Observable o, Object arg) {
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAccHd;
    private com.see.truetransact.uicomponent.CButton btnAccNo;
    private com.see.truetransact.uicomponent.CButton btnClear1;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnFromAccountno;
    private com.see.truetransact.uicomponent.CButton btnPostCharges;
    private com.see.truetransact.uicomponent.CButton btnProcess;
    private com.see.truetransact.uicomponent.CButton btnTOAccountno;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CPanel cPanel2;
    private com.see.truetransact.uicomponent.CPanel cPanel3;
    private com.see.truetransact.uicomponent.CComboBox cboBranchCode;
    private com.see.truetransact.uicomponent.CComboBox cboChargeType;
    private com.see.truetransact.uicomponent.CComboBox cboProdId;
    private com.see.truetransact.uicomponent.CComboBox cboProdIdCr;
    private com.see.truetransact.uicomponent.CComboBox cboProdName;
    private com.see.truetransact.uicomponent.CComboBox cboProdTypeCr;
    private com.see.truetransact.uicomponent.CCheckBox chkFulldue;
    private com.see.truetransact.uicomponent.CCheckBox chkNoTrans;
    private com.see.truetransact.uicomponent.CCheckBox chkSelectAll;
    private com.see.truetransact.uicomponent.CLabel lblAccHd;
    private com.see.truetransact.uicomponent.CLabel lblAccNo;
    private javax.swing.JLabel lblActHeadDesc;
    private com.see.truetransact.uicomponent.CLabel lblBranch;
    private com.see.truetransact.uicomponent.CLabel lblChargeType;
    private com.see.truetransact.uicomponent.CLabel lblFromAccountno;
    private com.see.truetransact.uicomponent.CLabel lblFromDate;
    private com.see.truetransact.uicomponent.CLabel lblNoOfInstallments1;
    private com.see.truetransact.uicomponent.CLabel lblProdId;
    private com.see.truetransact.uicomponent.CLabel lblProdIdCr;
    private com.see.truetransact.uicomponent.CLabel lblProdName;
    private com.see.truetransact.uicomponent.CLabel lblProdTypeCr;
    private com.see.truetransact.uicomponent.CLabel lblTOAccountno;
    private com.see.truetransact.uicomponent.CLabel lblToDate;
    private com.see.truetransact.uicomponent.CLabel lblTotalTransactionAmt;
    private com.see.truetransact.uicomponent.CLabel lblTotalTransactionAmtVal;
    private com.see.truetransact.uicomponent.CPanel panAccHd;
    private com.see.truetransact.uicomponent.CPanel panAcctNo;
    private com.see.truetransact.uicomponent.CPanel panMultiSearch;
    private com.see.truetransact.uicomponent.CPanel panMultiSearch1;
    private com.see.truetransact.uicomponent.CPanel panMultiSearch3;
    private com.see.truetransact.uicomponent.CPanel panSearch;
    private com.see.truetransact.uicomponent.CPanel panSearchCondition;
    private com.see.truetransact.uicomponent.CPanel panTable;
    private com.see.truetransact.uicomponent.CSeparator sptLine;
    private com.see.truetransact.uicomponent.CScrollPane srcTable;
    private com.see.truetransact.uicomponent.CTable tblData;
    private com.see.truetransact.uicomponent.CDateField tdtFromDate;
    private com.see.truetransact.uicomponent.CDateField tdtToDate;
    private com.see.truetransact.uicomponent.CTextField txtAccHd;
    private com.see.truetransact.uicomponent.CTextField txtAccNo;
    private com.see.truetransact.uicomponent.CTextField txtChrgAmt;
    private com.see.truetransact.uicomponent.CTextField txtFromAccountno;
    private com.see.truetransact.uicomponent.CTextField txtNoOfInstallments;
    private com.see.truetransact.uicomponent.CTextField txtTOAccountno;
    // End of variables declaration//GEN-END:variables
}
