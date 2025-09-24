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
package com.see.truetransact.ui.termloan.arbitration;

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
import com.see.truetransact.clientutil.*;
import com.see.truetransact.commonutil.ParseException;
import com.see.truetransact.ui.common.viewall.ViewAll;

import com.see.truetransact.ui.common.viewall.TableDialogUI;
import com.see.truetransact.ui.TrueTransactMain;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.swing.JDialog;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

/**
 * @author balachandar
 */
public class LoanArbitrationUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer, ListSelectionListener {
//    private final AuthorizeRB resourceBundle = new AuthorizeRB();

    private LoanArbitrationOB observable;
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
    String bankName = "";
    boolean generateNotice = false;
    int viewType = 0;
    int FROMACTNO = 1, TOACTNO = 2,EDITVIEW=3, PROCESSEDITVIEW=4; // Added PROCESSEDITVIEW by nithya on 10-03-2016 
    private final static Logger log = Logger.getLogger(LoanArbitrationUI.class);
    private String actionType ="NEW";
    private String arc_Id ="";
    
    
    
    /**
     * Creates new form AuthorizeUI
     */
    public LoanArbitrationUI() {
       
        setupInit();
        setupScreen();
      //   tblData.setAutoCreateRowSorter(true);
    }

    /**
     * Creates new form AuthorizeUI
     */
    public LoanArbitrationUI(CInternalFrame parent, HashMap paramMap) {
        this.parent = parent;
        this.paramMap = paramMap;
        setupInit();
        setupScreen();
    }

    private void setupInit() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        addtoBtnGp();
        internationalize();
        setObservable();
        createCboProdType();
        createCboNoticeType();
        setButtonEnableDisable();
        btnGenerateNotice.setEnabled(false);
        
//        txtNoOfInstallments.setValidation(new com.see.truetransact.uivalidation.NumericValidation());
//        txtNoOfInstallments.setMaxLength(3);
//        txtLastNoticeSentBefore.setValidation(new com.see.truetransact.uivalidation.NumericValidation());
//        txtLastNoticeSentBefore.setMaxLength(3);

        bankName = ((String) TrueTransactMain.BANKINFO.get("BANK_NAME")).toUpperCase();
//        txtNoticeCharge.setAllowAll(true);
//        txtPostageCharge.setAllowAll(true);
        txtFromAccountno.setAllowAll(true);
        txtTOAccountno.setAllowAll(true);
        txtArbRate.setAllowAll(true);
        txtResNo.setAllowAll(true);
        cPanel1.setVisible(false);
//        btnsms.setVisible(false);
        if (bankName.lastIndexOf("MAHILA") != -1) {
            lblFromDate.setVisible(false);
            tdtFromDate.setVisible(false);
            lblToDate.setVisible(false);
            tdtToDate.setVisible(false);
//            lblNoticeCharge.setVisible(false);
//            txtNoticeCharge.setVisible(false);
//            lblPostageCharge.setVisible(false);
//            txtPostageCharge.setVisible(false);
//            txtNoOfInstallments.setVisible(false);
//            lblNoOfInstallments.setVisible(false);
//            chkFulldue.setVisible(false);
//            btnPostCharges.setVisible(false);
//            btnPostCharges.setText("Insert Charge Details");
        } else {
            lblFromDate.setVisible(true);
            tdtFromDate.setVisible(true);
            lblToDate.setVisible(true);
            tdtToDate.setVisible(true);
//            lblNoticeCharge.setVisible(true);
//            txtNoticeCharge.setVisible(true);
//            lblPostageCharge.setVisible(true);
//            txtPostageCharge.setVisible(true);
//            txtNoOfInstallments.setVisible(true);
//            lblNoOfInstallments.setVisible(true);
//            chkFulldue.setVisible(true);
            chkPrized.setVisible(false);
            chkIncludeClosed.setVisible(false);
            chkDueLoans.setVisible(false); // Added by nithya on 15-09-2017 for 7535
            chkNonPrized.setVisible(false);
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
        panMultiSearch.setEnabled(false);
        panMultiSearch1.setEnabled(false);
        enableDisableComponants(false);
        btnDelete.setEnabled(false);
        btnProcessEdit.setEnabled(true); // added by nithya on 10-03-2016
        chkIncludeClosed.setVisible(true);
        chkIncludeClosed.setSelected(false);
        chkDueLoans.setVisible(true); // Added by nithya on 15-09-2017 for 7535
        chkDueLoans.setSelected(false);
    }

    private void addtoBtnGp() {
        rdbArbit.add(rdoArbFiling);
        rdbArbit.add(rdoArbPosting);
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
            observable = new LoanArbitrationOB();
            observable.addObserver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void populateData(HashMap mapID) {
        try {
            log.info("populateData...");
            ArrayList heading = observable.populateData(mapID, tblData);
            if (heading != null && heading.size() > 0) {
                EnhancedComboBoxModel cboModel = new EnhancedComboBoxModel(heading);
//                cboSearchCol.setModel(cboModel);
            }
        } catch (Exception e) {
            System.err.println("Exception " + e.toString() + "Caught");
            e.printStackTrace();
        }
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
                //    int guarantorRowIndexSelected = tblGuarantorData.getSelectedRow();
                if (accountNumberMap == null) {
                    accountNumberMap = new HashMap();
                }
                if (guarantorMemberMap == null) {
                    guarantorMemberMap = new HashMap();
                }
                if (previousRow != -1 && previousRow != rowIndexSelected) {
                    // isSelectedRowTicked(tblGuarantorData);
                }
            } else {
                // observable.setSelectAll(tblGuarantorData, new Boolean(false));
            }
        }
        //Changed By Suresh
        String prodType = String.valueOf(cboProdType.getSelectedItem());
        if (prodType.equals("MDS")) {
            //  observable.populateGuarantorTable(String.valueOf(tblData.getValueAt(rowIndexSelected, 2)), tblGuarantorData);
        } else {
            // observable.populateGuarantorTable(String.valueOf(tblData.getValueAt(rowIndexSelected, 1)), tblGuarantorData);
        }
        previousRow = rowIndexSelected;
        //Added by sreekrishnan
        int selectedColomn = tblData.getSelectedColumn();
        System.out.println("selectedColomn%@%@%"+selectedColomn);
        if(selectedColomn!=-1)
        {
            
        }
        calTotalArc();
    }
    
    // Modified by nithya on 21-03-2016 for 4025
    public void calTotalArc(){
        TableSorter tblSorter = (TableSorter) tblData.getModel();
        TableModel tblModel = (TableModel) tblSorter.getModel();
        if(tblData.getRowCount()>0){    
            int count = tblData.getRowCount();
            double total = 0.0;
            double selectedCount =  0.0;
           if(tblModel.getColumnName(9).equalsIgnoreCase("ARC Fee")){            
            for(int i=0;i<count;i++){
                if((Boolean)tblData.getValueAt(i, 0)){ 
                    //tblData.getco
                    selectedCount = selectedCount+1;
                    total = total + CommonUtil.convertObjToDouble(tblData.getValueAt(i, 9));
                }
            }
            lblTotalAmountVal.setText(CommonUtil.formatCrore(CommonUtil.convertObjToStr(total)));
            lblSelectedCountVal.setText(CommonUtil.convertObjToStr(selectedCount));      
            }else if (cboProdType.getSelectedItem().equals("ROOMS")) {
                for (int i = 0; i < count; i++) {
                    if ((Boolean) tblData.getValueAt(i, 0)) {
                        //tblData.getco
                        selectedCount = selectedCount + 1;
                        total = total + CommonUtil.convertObjToDouble(tblData.getValueAt(i, 6));
                    }
                }
                lblTotalAmountVal.setText(CommonUtil.formatCrore(CommonUtil.convertObjToStr(total)));
                lblSelectedCountVal.setText(CommonUtil.convertObjToStr(selectedCount));
            }else{             
             for(int i=0;i<count;i++){
                if((Boolean)tblData.getValueAt(i, 0)){ 
                    //tblData.getco
                    selectedCount = selectedCount+1;
                    total = total + CommonUtil.convertObjToDouble(tblData.getValueAt(i, 8));
                }
             }
            lblTotalAmountVal.setText(CommonUtil.formatCrore(CommonUtil.convertObjToStr(total)));
            lblSelectedCountVal.setText(CommonUtil.convertObjToStr(selectedCount));   
           }                  
        }
        tblModel = null;
        tblSorter = null;
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdbArbit = new com.see.truetransact.uicomponent.CButtonGroup();
        panSearchCondition = new com.see.truetransact.uicomponent.CPanel();
        panMultiSearch = new com.see.truetransact.uicomponent.CPanel();
        lblFromDate = new com.see.truetransact.uicomponent.CLabel();
        tdtFromDate = new com.see.truetransact.uicomponent.CDateField();
        lblToDate = new com.see.truetransact.uicomponent.CLabel();
        tdtToDate = new com.see.truetransact.uicomponent.CDateField();
        lblProdId = new com.see.truetransact.uicomponent.CLabel();
        cboProdId = new com.see.truetransact.uicomponent.CComboBox();
        lblProdType = new com.see.truetransact.uicomponent.CLabel();
        cboProdType = new com.see.truetransact.uicomponent.CComboBox();
        chkNonPrized = new com.see.truetransact.uicomponent.CCheckBox();
        chkPrized = new com.see.truetransact.uicomponent.CCheckBox();
        lblTOAccountno = new com.see.truetransact.uicomponent.CLabel();
        lblFromAccountno = new com.see.truetransact.uicomponent.CLabel();
        cPanel2 = new com.see.truetransact.uicomponent.CPanel();
        txtFromAccountno = new com.see.truetransact.uicomponent.CTextField();
        btnFromAccountno = new com.see.truetransact.uicomponent.CButton();
        cPanel3 = new com.see.truetransact.uicomponent.CPanel();
        txtTOAccountno = new com.see.truetransact.uicomponent.CTextField();
        btnTOAccountno = new com.see.truetransact.uicomponent.CButton();
        chkIncludeClosed = new com.see.truetransact.uicomponent.CCheckBox();
        panMultiSearch1 = new com.see.truetransact.uicomponent.CPanel();
        panArbit = new com.see.truetransact.uicomponent.CPanel();
        lblArbDt = new com.see.truetransact.uicomponent.CLabel();
        tdtArbDt = new com.see.truetransact.uicomponent.CDateField();
        lblArbRate = new com.see.truetransact.uicomponent.CLabel();
        txtArbRate = new com.see.truetransact.uicomponent.CTextField();
        panRes = new com.see.truetransact.uicomponent.CPanel();
        lblResDt = new com.see.truetransact.uicomponent.CLabel();
        lblResNo = new com.see.truetransact.uicomponent.CLabel();
        txtResNo = new com.see.truetransact.uicomponent.CTextField();
        tdtResDt = new com.see.truetransact.uicomponent.CDateField();
        panPost = new com.see.truetransact.uicomponent.CPanel();
        btnProcess = new com.see.truetransact.uicomponent.CButton();
        cPanel1 = new com.see.truetransact.uicomponent.CPanel();
        rdoArbPosting = new com.see.truetransact.uicomponent.CRadioButton();
        rdoArbFiling = new com.see.truetransact.uicomponent.CRadioButton();
        lblArcId = new com.see.truetransact.uicomponent.CLabel();
        valArcId = new com.see.truetransact.uicomponent.CLabel();
        lblReportingDate = new com.see.truetransact.uicomponent.CLabel();
        tdtReportingDate = new com.see.truetransact.uicomponent.CDateField();
        chkDueLoans = new com.see.truetransact.uicomponent.CCheckBox();
        panTable = new com.see.truetransact.uicomponent.CPanel();
        chkSelectAll = new com.see.truetransact.uicomponent.CCheckBox();
        srcTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblData = new com.see.truetransact.uicomponent.CTable();
        lblActHoldersList = new com.see.truetransact.uicomponent.CLabel();
        lblNoOfRecords = new com.see.truetransact.uicomponent.CLabel();
        lblNoOfRecordsVal = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        panSearch = new com.see.truetransact.uicomponent.CPanel();
        btnGenerateNotice = new com.see.truetransact.uicomponent.CButton();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        btnClear = new com.see.truetransact.uicomponent.CButton();
        btnPrintNotice = new com.see.truetransact.uicomponent.CButton();
        lblTotal = new com.see.truetransact.uicomponent.CLabel();
        lblTotalAmountVal = new com.see.truetransact.uicomponent.CLabel();
        lblSelectedCount = new com.see.truetransact.uicomponent.CLabel();
        lblSelectedCountVal = new com.see.truetransact.uicomponent.CLabel();
        btnFilePrint = new com.see.truetransact.uicomponent.CButton();
        btnRecalculate = new com.see.truetransact.uicomponent.CButton();
        sptLine = new com.see.truetransact.uicomponent.CSeparator();
        tbrTokenConfig = new com.see.truetransact.uicomponent.CToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lbSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace70 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lbSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace71 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace72 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace73 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace74 = new com.see.truetransact.uicomponent.CLabel();
        btnClose1 = new com.see.truetransact.uicomponent.CButton();
        lblSpace75 = new com.see.truetransact.uicomponent.CLabel();
        btnProcessEdit = new com.see.truetransact.uicomponent.CButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Loan Arbitration");
        setMaximumSize(new java.awt.Dimension(850, 630));
        setMinimumSize(new java.awt.Dimension(850, 630));
        setPreferredSize(new java.awt.Dimension(850, 630));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        panSearchCondition.setMaximumSize(new java.awt.Dimension(650, 150));
        panSearchCondition.setMinimumSize(new java.awt.Dimension(650, 150));
        panSearchCondition.setPreferredSize(new java.awt.Dimension(650, 160));
        panSearchCondition.setLayout(new java.awt.GridBagLayout());

        panMultiSearch.setMinimumSize(new java.awt.Dimension(275, 145));
        panMultiSearch.setPreferredSize(new java.awt.Dimension(275, 145));
        panMultiSearch.setLayout(new java.awt.GridBagLayout());

        lblFromDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblFromDate.setText("Sanction From Date");
        lblFromDate.setMaximumSize(new java.awt.Dimension(126, 18));
        lblFromDate.setMinimumSize(new java.awt.Dimension(126, 18));
        lblFromDate.setPreferredSize(new java.awt.Dimension(126, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 4);
        panMultiSearch.add(lblFromDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 11, 2, 4);
        panMultiSearch.add(tdtFromDate, gridBagConstraints);

        lblToDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblToDate.setText("Sanction To Date");
        lblToDate.setMaximumSize(new java.awt.Dimension(230, 85));
        lblToDate.setMinimumSize(new java.awt.Dimension(116, 18));
        lblToDate.setPreferredSize(new java.awt.Dimension(116, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panMultiSearch.add(lblToDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 11, 2, 4);
        panMultiSearch.add(tdtToDate, gridBagConstraints);

        lblProdId.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblProdId.setText("Product Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panMultiSearch.add(lblProdId, gridBagConstraints);

        cboProdId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdId.setPopupWidth(160);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 11, 2, 4);
        panMultiSearch.add(cboProdId, gridBagConstraints);

        lblProdType.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panMultiSearch.add(lblProdType, gridBagConstraints);

        cboProdType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 11, 2, 4);
        panMultiSearch.add(cboProdType, gridBagConstraints);

        chkNonPrized.setText("Non Prized");
        chkNonPrized.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkNonPrizedActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 11, 2, 2);
        panMultiSearch.add(chkNonPrized, gridBagConstraints);

        chkPrized.setText("Prized");
        chkPrized.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkPrizedActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 11, 2, 2);
        panMultiSearch.add(chkPrized, gridBagConstraints);

        lblTOAccountno.setText("To Acct No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panMultiSearch.add(lblTOAccountno, gridBagConstraints);

        lblFromAccountno.setText("From Acct No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panMultiSearch.add(lblFromAccountno, gridBagConstraints);

        cPanel2.setMinimumSize(new java.awt.Dimension(130, 22));
        cPanel2.setPreferredSize(new java.awt.Dimension(130, 22));
        cPanel2.setLayout(new java.awt.GridBagLayout());

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
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 9, 2, 4);
        panMultiSearch.add(cPanel2, gridBagConstraints);

        cPanel3.setMinimumSize(new java.awt.Dimension(130, 22));
        cPanel3.setPreferredSize(new java.awt.Dimension(130, 22));
        cPanel3.setLayout(new java.awt.GridBagLayout());

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
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 9, 2, 4);
        panMultiSearch.add(cPanel3, gridBagConstraints);

        chkIncludeClosed.setText("Include Closed ");
        chkIncludeClosed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkIncludeClosedActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 0, 0);
        panMultiSearch.add(chkIncludeClosed, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipady = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 59, 11, 0);
        panSearchCondition.add(panMultiSearch, gridBagConstraints);

        panMultiSearch1.setMaximumSize(new java.awt.Dimension(420, 150));
        panMultiSearch1.setMinimumSize(new java.awt.Dimension(420, 150));
        panMultiSearch1.setPreferredSize(new java.awt.Dimension(420, 150));
        panMultiSearch1.setLayout(new java.awt.GridBagLayout());

        panArbit.setMinimumSize(new java.awt.Dimension(200, 65));
        panArbit.setPreferredSize(new java.awt.Dimension(200, 65));
        panArbit.setLayout(new java.awt.GridBagLayout());

        lblArbDt.setText("Arbitration Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panArbit.add(lblArbDt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panArbit.add(tdtArbDt, gridBagConstraints);

        lblArbRate.setText("Arbitration Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panArbit.add(lblArbRate, gridBagConstraints);

        txtArbRate.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panArbit.add(txtArbRate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panMultiSearch1.add(panArbit, gridBagConstraints);

        panRes.setMinimumSize(new java.awt.Dimension(200, 65));
        panRes.setPreferredSize(new java.awt.Dimension(200, 65));
        panRes.setLayout(new java.awt.GridBagLayout());

        lblResDt.setText("Resolution Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRes.add(lblResDt, gridBagConstraints);

        lblResNo.setText("Resolution No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRes.add(lblResNo, gridBagConstraints);

        txtResNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRes.add(txtResNo, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRes.add(tdtResDt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panMultiSearch1.add(panRes, gridBagConstraints);

        panPost.setMinimumSize(new java.awt.Dimension(400, 80));
        panPost.setPreferredSize(new java.awt.Dimension(400, 80));
        panPost.setLayout(new java.awt.GridBagLayout());

        btnProcess.setText("Process");
        btnProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 169, 0, 2);
        panPost.add(btnProcess, gridBagConstraints);

        cPanel1.setMaximumSize(new java.awt.Dimension(400, 40));
        cPanel1.setMinimumSize(new java.awt.Dimension(385, 40));
        cPanel1.setPreferredSize(new java.awt.Dimension(385, 40));
        cPanel1.setLayout(new java.awt.GridBagLayout());

        rdoArbPosting.setText("List for Arbitration Posting");
        cPanel1.add(rdoArbPosting, new java.awt.GridBagConstraints());

        rdoArbFiling.setText("Post Arbitration Filing List");
        cPanel1.add(rdoArbFiling, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.ipady = -4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 0, 8);
        panPost.add(cPanel1, gridBagConstraints);

        lblArcId.setText("ARC ID:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        panPost.add(lblArcId, gridBagConstraints);

        valArcId.setMaximumSize(new java.awt.Dimension(70, 21));
        valArcId.setMinimumSize(new java.awt.Dimension(70, 21));
        valArcId.setPreferredSize(new java.awt.Dimension(70, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPost.add(valArcId, gridBagConstraints);

        lblReportingDate.setText("Reporting Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 49, 0, 5);
        panPost.add(lblReportingDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 67);
        panPost.add(tdtReportingDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        panMultiSearch1.add(panPost, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 58);
        panSearchCondition.add(panMultiSearch1, gridBagConstraints);

        chkDueLoans.setText("All Accounts");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        panSearchCondition.add(chkDueLoans, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 164;
        gridBagConstraints.ipady = 24;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 0);
        getContentPane().add(panSearchCondition, gridBagConstraints);

        panTable.setMaximumSize(new java.awt.Dimension(650, 350));
        panTable.setMinimumSize(new java.awt.Dimension(650, 350));
        panTable.setPreferredSize(new java.awt.Dimension(650, 350));
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

        tblData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblData.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblData.setMinimumSize(new java.awt.Dimension(350, 80));
        tblData.setPreferredScrollableViewportSize(new java.awt.Dimension(450000, 400000));
        tblData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDataMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblDataMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblDataMouseReleased(evt);
            }
        });
        tblData.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                tblDataMouseMoved(evt);
            }
        });
        tblData.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tblDataFocusLost(evt);
            }
        });
        tblData.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tblDataPropertyChange(evt);
            }
        });
        tblData.addKeyListener(new java.awt.event.KeyAdapter() {
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

        lblActHoldersList.setText("Loan Account Holders List");
        lblActHoldersList.setMaximumSize(new java.awt.Dimension(230, 85));
        lblActHoldersList.setMinimumSize(new java.awt.Dimension(186, 18));
        lblActHoldersList.setPreferredSize(new java.awt.Dimension(186, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 4);
        panTable.add(lblActHoldersList, gridBagConstraints);

        lblNoOfRecords.setText("No. of Records Found : ");
        lblNoOfRecords.setMaximumSize(new java.awt.Dimension(140, 18));
        lblNoOfRecords.setMinimumSize(new java.awt.Dimension(140, 18));
        lblNoOfRecords.setPreferredSize(new java.awt.Dimension(140, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panTable.add(lblNoOfRecords, gridBagConstraints);

        lblNoOfRecordsVal.setMaximumSize(new java.awt.Dimension(230, 85));
        lblNoOfRecordsVal.setMinimumSize(new java.awt.Dimension(80, 18));
        lblNoOfRecordsVal.setPreferredSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panTable.add(lblNoOfRecordsVal, gridBagConstraints);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        panTable.add(btnDelete, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 156;
        gridBagConstraints.ipady = -103;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 0, 0);
        getContentPane().add(panTable, gridBagConstraints);

        panSearch.setLayout(new java.awt.GridBagLayout());

        btnGenerateNotice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnGenerateNotice.setText("Post TR");
        btnGenerateNotice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerateNoticeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 10, 0, 0);
        panSearch.add(btnGenerateNotice, gridBagConstraints);

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
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 10, 0, 0);
        panSearch.add(btnClose, gridBagConstraints);

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
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 0, 0);
        panSearch.add(btnClear, gridBagConstraints);

        btnPrintNotice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrintNotice.setText("Print");
        btnPrintNotice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintNoticeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.ipadx = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        panSearch.add(btnPrintNotice, gridBagConstraints);

        lblTotal.setText("Total Amount :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 10, 0, 0);
        panSearch.add(lblTotal, gridBagConstraints);

        lblTotalAmountVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblTotalAmountVal.setPreferredSize(new java.awt.Dimension(100, 21));
        lblTotalAmountVal.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.ipady = 21;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 1, 0, 0);
        panSearch.add(lblTotalAmountVal, gridBagConstraints);

        lblSelectedCount.setText("Selected Items :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 47, 0, 0);
        panSearch.add(lblSelectedCount, gridBagConstraints);

        lblSelectedCountVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblSelectedCountVal.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipadx = 35;
        gridBagConstraints.ipady = 21;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 6, 0, 0);
        panSearch.add(lblSelectedCountVal, gridBagConstraints);

        btnFilePrint.setText("Filing");
        btnFilePrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFilePrintActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 10, 0, 0);
        panSearch.add(btnFilePrint, gridBagConstraints);

        btnRecalculate.setText("Recalculate");
        btnRecalculate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRecalculateActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 7, 0, 0);
        panSearch.add(btnRecalculate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = -186;
        gridBagConstraints.ipady = -21;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 0, 0);
        getContentPane().add(panSearch, gridBagConstraints);

        sptLine.setMinimumSize(new java.awt.Dimension(2, 2));
        sptLine.setPreferredSize(new java.awt.Dimension(2, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.ipadx = 804;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 10, 0, 0);
        getContentPane().add(sptLine, gridBagConstraints);

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setEnabled(false);
        btnView.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView.setPreferredSize(new java.awt.Dimension(21, 21));
        tbrTokenConfig.add(btnView);

        lbSpace3.setText("     ");
        tbrTokenConfig.add(lbSpace3);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnNew);

        lblSpace70.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace70.setText("     ");
        lblSpace70.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace70.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace70.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace70);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnEdit);

        lbSpace2.setText("     ");
        tbrTokenConfig.add(lbSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnSave);

        lblSpace71.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace71.setText("     ");
        lblSpace71.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace71.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace71.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace71);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnCancel);

        lblSpace3.setText("     ");
        tbrTokenConfig.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnAuthorize);

        lblSpace72.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace72.setText("     ");
        lblSpace72.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace72.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace72.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace72);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        tbrTokenConfig.add(btnException);

        lblSpace73.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace73.setText("     ");
        lblSpace73.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace73);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        tbrTokenConfig.add(btnReject);

        lblSpace5.setText("     ");
        tbrTokenConfig.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnPrint);

        lblSpace74.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace74.setText("     ");
        lblSpace74.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace74);

        btnClose1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose1.setToolTipText("Close");
        btnClose1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClose1ActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnClose1);

        lblSpace75.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace75.setText("     ");
        lblSpace75.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace75);

        btnProcessEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/ledger.jpg"))); // NOI18N
        btnProcessEdit.setToolTipText("Processed Files");
        btnProcessEdit.setFocusable(false);
        btnProcessEdit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnProcessEdit.setMinimumSize(new java.awt.Dimension(37, 30));
        btnProcessEdit.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnProcessEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessEditActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnProcessEdit);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(tbrTokenConfig, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtTOAccountnoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTOAccountnoFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTOAccountnoFocusLost

    private void txtFromAccountnoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFromAccountnoFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFromAccountnoFocusLost

    private void btnTOAccountnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTOAccountnoActionPerformed
        // TODO add your handling code here:
        popUp(TOACTNO);
    }//GEN-LAST:event_btnTOAccountnoActionPerformed

    private void btnFromAccountnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFromAccountnoActionPerformed
        // TODO add your handling code here:
        popUp(FROMACTNO);
    }//GEN-LAST:event_btnFromAccountnoActionPerformed

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
        if (val) {
            lblFromDate.setText("Sanction From Date");
            lblToDate.setText("Sanction To Date");
        } else {
            lblFromDate.setText("Demand From Date");
            lblToDate.setText("Demand To Date");
//            tdtOverDueDate.setDateValue("");
//            chkFulldue.setSelected(false);
//            txtNoOfInstallments.setText("");
        }
    }

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        ClientUtil.clearAll(this);
        enableDisableComponants(false);
        setButtonEnableDisable();
        //   btnsms.setVisible(false);
//        btnPostCharges.setEnabled(true);
        // lblTotalChargeAmountVal.setText("");
        //lblSelectedRecordVal.setText("");
        lblNoOfRecordsVal.setText("");
     //    setupInit();
     //   setupScreen();
      //   tblData.revalidate();//tblData.rep
        lblTotalAmountVal.setText("");
        lblSelectedCountVal.setText("");
    }//GEN-LAST:event_btnClearActionPerformed

    private void chkNonPrizedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkNonPrizedActionPerformed
        // TODO add your handling code here:
        if (chkNonPrized.isSelected() == true) {
            chkPrized.setSelected(false);
            chkIncludeClosed.setSelected(false);
        }
    }//GEN-LAST:event_chkNonPrizedActionPerformed

    private void chkPrizedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkPrizedActionPerformed
        // TODO add your handling code here:
        if (chkPrized.isSelected() == true) {
            chkNonPrized.setSelected(false);
            chkIncludeClosed.setSelected(false);
        }
    }//GEN-LAST:event_chkPrizedActionPerformed

    private void tblDataMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseReleased
        // TODO add your handling code here:
        if (/*(evt.getClickCount() == 2) && */(evt.getModifiers() == 16)) {
            whenTableRowSelected();
            setSelectedRecord();
            calculatetot();
        }
    }//GEN-LAST:event_tblDataMouseReleased
   public void calculatetot(){
       
   }
    
    public void fillData(Object param) {

        final HashMap hash = (HashMap) param;
        System.out.println("Hash: " + hash);
        if (viewType == FROMACTNO) {
            if(cboProdType.getSelectedItem().equals("ROOMS")){
              txtFromAccountno.setText(CommonUtil.convertObjToStr(hash.get("ROOM_NO")));  
            }else{
            txtFromAccountno.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
            }
        } else if (viewType == TOACTNO) {
            if(cboProdType.getSelectedItem().equals("ROOMS")){
              txtTOAccountno.setText(CommonUtil.convertObjToStr(hash.get("ROOM_NO")));  
            }else{
            txtTOAccountno.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
        }
        }
         else if (viewType == EDITVIEW || viewType == PROCESSEDITVIEW ) {  // added by nithya on 10-03-2016
            arc_Id = CommonUtil.convertObjToStr(hash.get("ARC_ID"));
            valArcId.setText(arc_Id);
//            ((ComboBoxModel) cboProdId.getModel()).setKeyForSelected(CommonUtil.convertObjToStr(hash.get("PROD_ID")));
          //  System.out.println("cboProdId --- :"+cboProdId.getSelectedItem());
            populateData();
            tblData.getColumnModel().getColumn(1).setPreferredWidth(100);
        }

    }

    private void setVisibleFields(boolean flag) {
        lblFromDate.setVisible(flag);
        lblToDate.setVisible(flag);
        tdtFromDate.setVisible(flag);
        lblFromAccountno.setVisible(flag);
        txtFromAccountno.setVisible(flag);
        lblTOAccountno.setVisible(flag);
        txtTOAccountno.setVisible(flag);
        btnFromAccountno.setVisible(flag);
        btnTOAccountno.setVisible(flag);
        tdtToDate.setVisible(flag);
        //       lblOverDueDate.setVisible(flag);
        //       tdtOverDueDate.setVisible(flag);
        chkPrized.setVisible(false);
        chkIncludeClosed.setSelected(false);
        chkDueLoans.setSelected(false); // Added by nithya on 15-09-2017 for 7535
        chkNonPrized.setVisible(false);
    }
    private void cboProdTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdTypeActionPerformed
        // TODO add your handling code here:
        String prodType = String.valueOf(cboProdType.getSelectedItem());
        if (prodType.length() > 0) {
            observable.fillDropDown(prodType);
            cboProdId.setModel(observable.getCbmProdId());
        }
        if (prodType.equals("MDS")) {
            setVisibleFields(false);
            //    chkLoneeOnly.setText("Chittal Only");
            chkPrized.setVisible(true);           
            chkIncludeClosed.setVisible(true);
            chkIncludeClosed.setSelected(false);
            chkDueLoans.setVisible(false); // Added by nithya on 15-09-2017 for 7535
            chkNonPrized.setVisible(true);
            cPanel2.setVisible(false);
            cPanel3.setVisible(false);
            lblActHoldersList.setText("MDS Account Holders List");
            //    lblToDate2.setText("MDS Surities List");
        } else if(prodType.equals("ROOMS")){
            setVisibleFields(true);
            chkIncludeClosed.setVisible(false);
            chkDueLoans.setVisible(true); // Added by nithya on 15-09-2017 for 7535
            chkDueLoans.setSelected(false);
            //       chkLoneeOnly.setText("Loanee Only");
            cPanel2.setVisible(true);
            cPanel3.setVisible(true);
            lblFromDate.setVisible(false);
            tdtFromDate.setVisible(false);
            lblToDate.setVisible(false);
            tdtToDate.setVisible(false);
            lblActHoldersList.setText("Loan Account Holders List");
        } else {
            setVisibleFields(true);
            chkIncludeClosed.setVisible(false);
            chkDueLoans.setVisible(true); // Added by nithya on 15-09-2017 for 7535
            chkDueLoans.setSelected(false);
            //       chkLoneeOnly.setText("Loanee Only");
            cPanel2.setVisible(true);
            cPanel3.setVisible(true);
            lblActHoldersList.setText("Loan Account Holders List");
            //      lblToDate2.setText("Loan Surities List");
        }
    }//GEN-LAST:event_cboProdTypeActionPerformed

    private void popUp(int field) {
        final HashMap viewMap = new HashMap();
        viewType = field;
        HashMap hash = new HashMap();
        HashMap whereMap = new HashMap();
        if(viewType!=EDITVIEW && viewType!=PROCESSEDITVIEW){  // added by nithya on 10-03-2016
        String productType = CommonUtil.convertObjToStr(cboProdType.getSelectedItem());    
        String prodId = ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected().toString();
        whereMap.put("prodId", prodId);
        if(productType.equals("ROOMS")){
            viewMap.put(CommonConstants.MAP_NAME, "getAllRoomsForArbitration");
            hash.put("SELECTED_BRANCH", TrueTransactMain.selBranch);
        }else{
        List lst = ClientUtil.executeQuery("TermLoan.getProdHead", whereMap);
        if (lst != null && lst.size() > 0) {
            whereMap = (HashMap) lst.get(0);
            String behavesLike = CommonUtil.convertObjToStr(whereMap.get("BEHAVES_LIKE"));
            if (behavesLike.equals("OD")) {
                viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountListAD");
//                hash.put("SELECTED_BRANCH", com.see.truetransact.ui.TrueTransactMain.BRANCH_ID);
                hash.put("SELECTED_BRANCH", TrueTransactMain.selBranch);
            } else {
                viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountListTL");
//                hash.put("SELECTED_BRANCH", com.see.truetransact.ui.TrueTransactMain.BRANCH_ID);
                  hash.put("SELECTED_BRANCH", TrueTransactMain.selBranch);
            }
        }
        }
        hash.put("PROD_ID", prodId);
        }
       if(viewType==EDITVIEW){ 
//            hash.put("SELECTED_BRANCH", com.see.truetransact.ui.TrueTransactMain.BRANCH_ID);
            hash.put("SELECTED_BRANCH", TrueTransactMain.selBranch);
            viewMap.put(CommonConstants.MAP_NAME, "getArbitrationDetailsForEdit");   // Condition to be changed by nithya in xml
       } 
       if(viewType==PROCESSEDITVIEW){  // added by nithya on 10-03-2016
//           hash.put("SELECTED_BRANCH", com.see.truetransact.ui.TrueTransactMain.BRANCH_ID);
            hash.put("SELECTED_BRANCH", TrueTransactMain.selBranch);
            viewMap.put(CommonConstants.MAP_NAME, "getArbitrationDetailsProcessing"); 
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
            hash.put(CommonConstants.BRANCH_ID, TrueTransactMain.selBranch);
//            hash.put(CommonConstants.BRANCH_ID, com.see.truetransact.ui.TrueTransactMain.BRANCH_ID);
            //        if(viewType==TO){
            //            hash.put("ACCT_NO", txtFromAccount.getText());
            //        }
            viewMap.put(CommonConstants.MAP_WHERE, hash);

            new ViewAll(this, viewMap).show();
       
    }
    private void tblDataMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseMoved
       // Point p = evt.getPoint();
       // String tip =
        //        String.valueOf(
        //        tblData.getModel().getValueAt(
        ////        tblData.rowAtPoint(p),
        //        tblData.columnAtPoint(p)));
       // tblData.setToolTipText(tip);
    }//GEN-LAST:event_tblDataMouseMoved

    private void tblDataMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMousePressed
//        if ((evt.getClickCount() == 2) && (evt.getModifiers() == 16)) {
//            HashMap whereMap = new HashMap();
//            whereMap.put("ACT_NUM", tblData.getValueAt(tblData.getSelectedRow(), 1));
//
//            TableDialogUI tableData = new TableDialogUI("getNoticeChargeDetails", whereMap);
//            tableData.setTitle("Notice Sent Details for " + tblData.getValueAt(tblData.getSelectedRow(), 1));
//            tableData.setPreferredSize(new Dimension(750, 450));
//            tableData.show();
//        }
    }//GEN-LAST:event_tblDataMousePressed

    private void chkSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSelectAllActionPerformed
        // Add your handling code here:
        observable.setSelectAll(tblData, new Boolean(chkSelectAll.isSelected()));
        setSelectedRecord();
        calTotalArc(); // added by nithya on 10-03-2016
    }//GEN-LAST:event_chkSelectAllActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        observable.removeRowsFromGuarantorTable(tblData);
        //  observable.removeRowsFromGuarantorTable(tblGuarantorData);
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
        //   lblSelectedRecordVal.setText(String.valueOf(count));
        calculateTotalApplCharges();
    }

    private void calculateTotalApplCharges() {
//        double totalChargeAmount = (CommonUtil.convertObjToDouble(txtNoticeCharge.getText())
//                + CommonUtil.convertObjToDouble(txtPostageCharge.getText()));
//        lblTotalChargeAmountVal.setText(String.valueOf(totalChargeAmount * CommonUtil.convertObjToDouble(lblSelectedRecordVal.getText())));
    }
public Date ConvertDate(Date date){

    DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
    String s = df.format(date);
    String result = s;
    try {
        date=df.parse(result);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return date;
}
 
/* Fills up the HashMap with data when user selects the row in ViewAll screen  */

    public void populateData() {
//        updateOBFields();
        String behavesLike = "";
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        boolean isOK = false;
        if (!CommonUtil.convertObjToStr(cboProdType.getSelectedItem()).equals("")) {
            isOK = true;
        } else {
            isOK = false;
            if (!actionType.equals("EDIT") && !actionType.equals("PROCESS_EDIT")) {
                ClientUtil.displayAlert("Select Product Type...");
            }
        }
//        if (!CommonUtil.convertObjToStr(cboNoticeType.getSelectedItem()).equals("")) {
//            isOK = true;
//        } else {
//            isOK = false;
//            ClientUtil.displayAlert("Select Notice Type...");
//        }
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

        if (isOK) {
            String prodType = String.valueOf(cboProdType.getSelectedItem());
            String prodId = ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected().toString();
            whereMap.put("prodId", prodId);
            List lst = ClientUtil.executeQuery("TermLoan.getProdHead", whereMap);
            if (lst != null && lst.size() > 0) {
                whereMap = (HashMap) lst.get(0);
                behavesLike = CommonUtil.convertObjToStr(whereMap.get("BEHAVES_LIKE"));
            }

            if (prodType.equals("MDS")) {
                viewMap.put(CommonConstants.MAP_NAME, "getAccountsForMDSLoanArbitration");
            }else if (prodType.equals("ROOMS")) {
                whereMap.put("ASONDT",currDt.clone());
                viewMap.put(CommonConstants.MAP_NAME, "getDetailsForRoomDueArbitration");
            } else if (behavesLike.equals("OD")) {
                //viewMap.put(CommonConstants.MAP_NAME, "getAccountsForLoanNoticeAD");
                viewMap.put(CommonConstants.MAP_NAME, "getAccountsForArbitrationAD"); // Added by nithya on 17-08-2016
            } else {
                viewMap.put(CommonConstants.MAP_NAME, "getAccountsForArbitration");
            }
            //                whereMap.put("ACT_NUM", observable.getTxtAccNo());
            if (prodType.length() > 0) {
                whereMap.put("PROD_TYPE", prodType);
            }
            if (String.valueOf(observable.getCbmProdId().getKeyForSelected()).length() > 0) {
                whereMap.put("PROD_ID", observable.getCbmProdId().getKeyForSelected());
            }
            if (tdtFromDate.getDateValue() != null && tdtFromDate.getDateValue().length() > 0) {
                //   if (CommonUtil.convertObjToStr(cboNoticeType.getSelectedItem()).equals("Demand Notice")) {
                whereMap.put("FROM_DUE_DT", getProperDate(DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue())));
                // } 
            }
            if (tdtToDate.getDateValue() != null && tdtToDate.getDateValue().length() > 0) {
                //  if (CommonUtil.convertObjToStr(cboNoticeType.getSelectedItem()).equals("Demand Notice")) {
                whereMap.put("TO_DUE_DT", getProperDate(DateUtil.getDateMMDDYYYY(tdtToDate.getDateValue())));
                // } 
            } else {
                //if (CommonUtil.convertObjToStr(cboNoticeType.getSelectedItem()).equals("Demand Notice")) {
                whereMap.put("TO_DUE_DT", currDt);
                // } 
            }
            if (CommonUtil.convertObjToStr(txtFromAccountno.getText()).length() > 0) {
                whereMap.put("FROM_ACCT_NUM", txtFromAccountno.getText());
            }

            if (CommonUtil.convertObjToStr(txtTOAccountno.getText()).length() > 0) {
                whereMap.put("TO_ACCT_NUM", txtTOAccountno.getText());
            }
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
            //Added By Suresh                  
            if (chkPrized.isSelected() == true) {
                whereMap.put("PRIZED", "PRIZED");
                whereMap.put("EXCLUDE_CLOSED_MDS", "EXCLUDE_CLOSED_MDS");
            }
            if (chkNonPrized.isSelected() == true) {
                whereMap.put("PRIZED", "NON PRIZED");
                whereMap.put("EXCLUDE_CLOSED_MDS", "EXCLUDE_CLOSED_MDS");
            }
            if (chkPrized.isSelected() == false && chkNonPrized.isSelected() == false && chkIncludeClosed.isSelected() == false) {
                whereMap.put("EXCLUDE_CLOSED_MDS", "EXCLUDE_CLOSED_MDS");
            }
            if (tdtArbDt.getDateValue() != null && tdtArbDt.getDateValue().length() > 0) {
                whereMap.put("OVER_DUE_DT", getProperDate(DateUtil.getDateMMDDYYYY(tdtArbDt.getDateValue())));
                whereMap.put("OVER_DUE_DT1", getProperDate(DateUtil.getDateMMDDYYYY(tdtArbDt.getDateValue())));
                whereMap.put("OVER_DUE_DT2", tdtArbDt.getDateValue());
            }
            // Added by nithya on 15-09-2017 for 7535
            if (!chkDueLoans.isSelected()) {
                whereMap.put("LOANS_WITH_NO_DUE", "LOANS_WITH_NO_DUE");
            } else {
                whereMap.put("SHOW_PRINCIPAL_BAL", "SHOW_PRINCIPAL_BAL");
            }

            /*
             * if (tdtArbDt.getDateValue() != null &&
             * tdtArbDt.getDateValue().length() > 0) {
             * whereMap.put("OVER_DUE_DT1", tdtArbDt.getDateValue()); }
             */
//            whereMap.put("NOTICE_TYPE", cboNoticeType.getSelectedItem());
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yyyy");
            Date curDate = null;
            try {
                curDate = DateUtil.getDateWithoutMinitues(currDt);
            } catch (Exception e) {
                e.printStackTrace();
            }
            SimpleDateFormat formatter5 = new SimpleDateFormat("MM-dd-yyyy");
            String formats1 = formatter5.format(ConvertDate(currDt));
            whereMap.put("TODAY_DT", getProperDate(currDt));
            whereMap.put("CURR_DATE", getProperDate(currDt));
            whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.selBranch);
            whereMap.put("BEHAVES_LIKE",behavesLike); // Added by nithya on 25-03-2020 for KD-1674
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            
            try {
                log.info("populateData...");
                // observable.removeRowsFromGuarantorTable(tblGuarantorData);
                ArrayList heading = observable.populateData(viewMap, tblData);
//                txtNoticeCharge.setText(observable.getTxtNoticeCharge());
//                txtPostageCharge.setText(observable.getTxtPostageCharge());
                lblNoOfRecordsVal.setText(String.valueOf(tblData.getRowCount()));
//                if (tblData.getRowCount() > 0) {
//                    btnsms.setVisible(true);
//                    btnsms.setEnabled(true);
//                } else {
//                    btnsms.setVisible(false);
//                }
                heading = null;
            } catch (Exception e) {
                System.err.println("Exception " + e.toString() + "Caught");
                e.printStackTrace();
            }
        }

        if (actionType != null && actionType.equals("EDIT") && arc_Id != null && arc_Id.length() > 0) {
            whereMap.put("ARC_ID", arc_Id);
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            viewMap.put(CommonConstants.MAP_NAME, "getArbitrationDetailsForView");
            try {
                log.info("populateData.ddddddddddd..");
                ArrayList heading = observable.populateEditData(viewMap, tblData);
                lblNoOfRecordsVal.setText(String.valueOf(tblData.getRowCount()));
                heading = null;
            } catch (Exception e) {
                System.err.println("Exception " + e.toString() + "Caught");
                e.printStackTrace();
            }
        }
        // added by nithya on 10-03-2016

        if (actionType != null && actionType.equals("PROCESS_EDIT") && arc_Id != null && arc_Id.length() > 0) {
            whereMap.put("ARC_ID", arc_Id);
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            viewMap.put(CommonConstants.MAP_NAME, "getArbitrationDetailsProcessingEdit");
            try {
                log.info("populateData.ddddddddddd..");
                ArrayList heading = observable.populateEditData(viewMap, tblData);
                lblNoOfRecordsVal.setText(String.valueOf(tblData.getRowCount()));
                heading = null;
            } catch (Exception e) {
                System.err.println("Exception " + e.toString() + "Caught");
                e.printStackTrace();
            }
        }
        // End
        viewMap = null;
        whereMap = null;
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
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        String param1 = observable.getSelected();
//        String param2 = observable.getSelectCharges();
        String prodType = String.valueOf(cboProdType.getSelectedItem());
        if (param1.length() > 0) {
            int yesNo = 0;
            String[] options = {"Yes", "No"};
            yesNo = COptionPane.showOptionDialog(null, "Generate Notice?", CommonConstants.WARNINGTITLE,
                    COptionPane.YES_NO_OPTION, COptionPane.QUESTION_MESSAGE,
                    null, options, options[0]);
            System.out.println("#$#$$ yesNo : " + yesNo);
//            if (yesNo==0) { //GENERATE NOTICE APPLIED MAY BE ARE MAY NOT BE BUT POST CHARGE CAN APPLY
            boolean selected = false;
            accountNumberMap = new HashMap();
            guarantorMemberMap = new HashMap();
            accountChargeMap = new HashMap();
            java.util.Map guarantorMap = observable.getGuarantorMap();
            ArrayList totalList = null;
            ArrayList rowList = null;
            String actNum;
            ArrayList tempList = null;
            observable.getTableModel(tblData);
            for (int i = 0, j = tblData.getRowCount(); i < j; i++) {
                selected = ((Boolean) tblData.getValueAt(i, 0)).booleanValue();
                if (selected) {
                    tempList = new ArrayList();
                    tempList.add((ArrayList) observable.getTableModel(tblData).get(i));
                    if (prodType.equals("MDS")) {
                        actNum = String.valueOf(tblData.getValueAt(i, 2));
                    } else {
                        actNum = String.valueOf(tblData.getValueAt(i, 1));
                    }
                    accountNumberMap.put(actNum, null);
                    if (guarantorMap != null && guarantorMap.size() > 0) {
                        totalList = (ArrayList) guarantorMap.get(actNum);
                        if (totalList != null && totalList.size() > 0) {
                            for (int g = 0; g < totalList.size(); g++) {
                                rowList = (ArrayList) totalList.get(g);
                                selected = ((Boolean) rowList.get(0)).booleanValue();
                                if (selected) {
                                    tempList.add(rowList);
                                    guarantorMemberMap.put(actNum + rowList.get(3), null);
                                }
                            }
                        }
                    }
                    accountChargeMap.put(actNum, tempList);
                }
            }

            ttIntegration = null;
            HashMap paramMap = new HashMap();
            paramMap.put("BranchCode", ProxyParameters.BRANCH_ID);
            paramMap.put("ProductId", observable.getCbmProdId().getKeyForSelected());
            paramMap.put("CURR_DATE", currDt);
            paramMap.put("Param1", param1);
//                paramMap.put("param3", param2);
            paramMap.put("Param2", guarantorGetSelected());
            if (CommonUtil.convertObjToStr(txtFromAccountno.getText()).length() > 0) {
                paramMap.put("FROM_ACCT_NUM", txtFromAccountno.getText());
            }

            if (CommonUtil.convertObjToStr(txtTOAccountno.getText()).length() > 0) {
                paramMap.put("TO_ACCT_NUM", txtTOAccountno.getText());
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
                ttIntegration.setParam(paramMap);
                generateNotice = true;
                //  String noticeType = CommonUtil.convertObjToStr(cboNoticeType.getSelectedItem());
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
                if (yesNo == 0) {
                    //ttIntegration.integrationForPrint(reportName, false);
                }
            }
//            }

        } else {
            generateNotice = false;
            ClientUtil.displayAlert("No Records found...");
        }
        viewMap = null;
        whereMap = null;
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

    private void createCboProdType() {
        if (((String) TrueTransactMain.BANKINFO.get("BANK_NAME")).toUpperCase().lastIndexOf("MAHILA") != -1) {
            cboProdType.addItem("");
            cboProdType.addItem("Gold Loans");
            cboProdType.addItem("Other Loans");
        } else {
            cboProdType.addItem("");
            //Added By Suresh
            HashMap whereMap = new HashMap();
            List loanProductLst = ClientUtil.executeQuery("getSelectLoanProducts", whereMap);
            if (loanProductLst != null && loanProductLst.size() > 0) {
                for (int i = 0; i < loanProductLst.size(); i++) {
                    whereMap = (HashMap) loanProductLst.get(i);
                    String product_type = CommonUtil.convertObjToStr(whereMap.get("PROD_TYPE"));
                    cboProdType.addItem(product_type);
                }

            }
            cboProdType.addItem("MDS");
            cboProdType.addItem("ROOMS");
//            cboProdType.addItem("Gold Loans");
//            cboProdType.addItem("Other Loans");
//            cboProdType.addItem("MDS Loans");
//            cboProdType.addItem("MDS");
        }
    }

    private void createCboNoticeType() {
//      
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
        
    }
    private void updateDBStatus(String status) {
        //observable.updateStatus(paramMap, status);
        HashMap screenParamMap = new HashMap();
        screenParamMap.put(CommonConstants.AUTHORIZEDATA, observable.getSelected());
        screenParamMap.put(CommonConstants.AUTHORIZESTATUS, status);

//        observable.insertCharges(paramMap);
        ClientUtil.showMessageWindow(observable.getResult());
        if (observable.getResult().equals(ClientConstants.RESULT_STATUS[4])) {
            observable.removeRowsFromGuarantorTable(tblData);
            lblNoOfRecordsVal.setText("");
            //observable.removeRowsFromGuarantorTable(tblGuarantorData);
        }

    }
     // modified by nithya on 10-03-2016
    private void btnGenerateNoticeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerateNoticeActionPerformed
       if (! actionType.equals("EDIT")) {
           String prodId =null;
           String productType = null;
           ArrayList arbList = new ArrayList();
           for (int i = 0; i < tblData.getRowCount(); i++) {
              if (((Boolean) tblData.getValueAt(i, 0)).booleanValue()) {
                ArrayList list = new ArrayList();
                list.add(tblData.getValueAt(i, 1));//chit no
                list.add(tblData.getValueAt(i, 2));//men-no
                list.add(tblData.getValueAt(i, 4));//no of
                list.add(tblData.getValueAt(i, 5));//due amt
                list.add(tblData.getValueAt(i, 6));//penal
                list.add(tblData.getValueAt(i, 7));//chrge
		list.add(tblData.getValueAt(i, 8));//arc fee
                list.add(tblData.getValueAt(i, 9));//total arc
                list.add(tblData.getValueAt(i, 10));//total arc
                list.add(tblData.getValueAt(i, 11));//file   
                list.add(tblData.getValueAt(i, 12));//misc charge
                list.add(tblData.getValueAt(i, 14)); // productId
                list.add(tblData.getValueAt(i, 15)); // productType
                prodId = tblData.getValueAt(i, 14).toString();
                productType = tblData.getValueAt(i, 15).toString();
                arbList.add(list);
            }
          }
                
        HashMap arbMap = new HashMap();
        arbMap.put("ARBITRATION_POST_LIST", arbList);
        arbMap.put("PROD_ID", prodId);
        
        if (productType.equals("MDS")) {
            arbMap.put("PROD_TYPE", "MDS");
        }else if (productType.equals("ROOMS")) {
            arbMap.put("PROD_TYPE", "ROOMS");
        } else {
            arbMap.put("PROD_TYPE", "TL");
        }        
        
       arbMap.put("ARC_ID",valArcId.getText()); // nithya    
       HashMap resultMap = observable.updateLoanArbitrationAfterProcess(arbMap);
        
        if (observable.getResult().equals(ClientConstants.RESULT_STATUS[2])) {
            ClientUtil.showMessageWindow("Posting Success :\n" + valArcId.getText());            
            displayPrintDetails(CommonUtil.convertObjToStr(valArcId.getText()));
            observable.removeRowsFromGuarantorTable(tblData);
            ClientUtil.clearAll(this);
            lblTotalAmountVal.setText("");
            lblSelectedCountVal.setText("");
            valArcId.setText("");
        }

   }

    }//GEN-LAST:event_btnGenerateNoticeActionPerformed
    public void displayPrintDetails(String arc_id)
    {
        try{
            System.out.println("transIdMap=11=="+arc_id);
            HashMap transTypeMap = new HashMap();
            HashMap transMap = new HashMap();
            HashMap transCashMap = new HashMap();
            String reportName = "";
            transCashMap.put("BATCH_ID",arc_id);
            transCashMap.put("TRANS_DT", currDt);
            transCashMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            HashMap transIdMap = new HashMap();
            List list = null; 
            list = ClientUtil.executeQuery("getTransferDetails", transCashMap);
                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        transMap = (HashMap) list.get(i);
                        transIdMap.put(transMap.get("SINGLE_TRANS_ID"), "TRANSFER");
                    }
                }
                System.out.println("transIdMap==="+transIdMap);
             int yesNo = 0;
            String[] voucherOptions = {"Yes", "No"};
              if (list != null && list.size() > 0) {
                yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                        null, voucherOptions, voucherOptions[0]);
                if (yesNo == 0) {
                    com.see.truetransact.clientutil.ttrintegration.TTIntegration ttIntgration = null;
                    HashMap paramMap = new HashMap();
                    paramMap.put("TransDt", currDt);
                    paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
                     Object keys1[] = transIdMap.keySet().toArray();
                    for (int i = 0; i < keys1.length; i++) {
                        paramMap.put("TransId", keys1[i]);
                        ttIntgration.setParam(paramMap);
                        reportName = "ReceiptPayment";
                        ttIntgration.integrationForPrint(reportName, false);
                    }
                }
              }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    private void btnProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessActionPerformed
        // TODO add your handling code here:
        lblNoOfRecordsVal.setText("");
        observable.setTxtArbRate(txtArbRate.getText());
        CommonUtil comm = new CommonUtil();
        final JDialog loading = comm.addProgressBar();
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws InterruptedException /**
             * Execute some operation
             */
            {
                populateData();
                tblData.getColumnModel().getColumn(1).setPreferredWidth(100);
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
    }//GEN-LAST:event_btnProcessActionPerformed

    private void tblDataPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tblDataPropertyChange
        // TODO add your handling code here:
        double amt=0.0;
        if (cboProdType.getSelectedItem().equals("ROOMS")) {
            if (tblData.getSelectedRowCount() == 1 && (tblData.getEditingColumn() == 3 || tblData.getEditingColumn() == 4 || tblData.getEditingColumn() == 6)) {
                amt = CommonUtil.convertObjToDouble(tblData.getValueAt(tblData.getSelectedRow(), 3)) + CommonUtil.convertObjToDouble(tblData.getValueAt(tblData.getSelectedRow(), 4));
                tblData.setValueAt(amt, tblData.getSelectedRow(), 5);
                tblData.setValueAt((amt + CommonUtil.convertObjToDouble(tblData.getValueAt(tblData.getSelectedRow(), 6))), tblData.getSelectedRow(), 7);
            }     
        }else if (cboProdType.getSelectedItem().equals("MDS")) { //Added for KD-3342
            if (tblData.getSelectedRowCount() == 1 && (tblData.getEditingColumn() == 5 || tblData.getEditingColumn() == 6 || tblData.getEditingColumn() == 7)) {
             amt = CommonUtil.convertObjToDouble(tblData.getValueAt(tblData.getSelectedRow(), 5))+
                    CommonUtil.convertObjToDouble(tblData.getValueAt(tblData.getSelectedRow(), 6))+CommonUtil.convertObjToDouble(tblData.getValueAt(tblData.getSelectedRow(), 7));
            tblData.setValueAt(amt, tblData.getSelectedRow(), 8);
            tblData.setValueAt((amt+ CommonUtil.convertObjToDouble(tblData.getValueAt(tblData.getSelectedRow(), 9))), tblData.getSelectedRow(), 10);
           
            }     
        }else{        
        if(tblData.getSelectedRowCount()==1 && (tblData.getEditingColumn()==7 || tblData.getEditingColumn()==4 || tblData.getEditingColumn()==5 ||
                tblData.getEditingColumn()==6 ||tblData.getEditingColumn()==9)){
            amt=CommonUtil.convertObjToDouble(tblData.getValueAt(tblData.getSelectedRow(), 4))+CommonUtil.convertObjToDouble(tblData.getValueAt(tblData.getSelectedRow(), 5))+
                    CommonUtil.convertObjToDouble(tblData.getValueAt(tblData.getSelectedRow(), 6))+CommonUtil.convertObjToDouble(tblData.getValueAt(tblData.getSelectedRow(), 7));
            tblData.setValueAt(amt, tblData.getSelectedRow(), 8);
            tblData.setValueAt((amt+ CommonUtil.convertObjToDouble(tblData.getValueAt(tblData.getSelectedRow(), 9))), tblData.getSelectedRow(), 10);
        }       
        } 
    }//GEN-LAST:event_tblDataPropertyChange

private void btnPrintNoticeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintNoticeActionPerformed
    //Added by Nithya on 30-04-2025 for KD-3963
    if(cboProdType.getSelectedIndex() == 0){
        observable.setProdType("");
    }
    
    String param1 = observable.getSelected();
    String prodType = String.valueOf(cboProdType.getSelectedItem());
    if (param1.length() > 0) {
        int yesNo = 0;
        String[] options = {"Yes", "No"};
        yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                COptionPane.YES_NO_OPTION, COptionPane.QUESTION_MESSAGE,
                null, options, options[0]);
        System.out.println("#$#$$ yesNo : " + yesNo);
        java.util.Map guarantorMap = observable.getGuarantorMap();
        ArrayList totalList = null;
        ArrayList rowList = null;
        String actNum;
        ArrayList tempList = null;
        boolean selected = false;
        observable.getTableModel(tblData);
        accountNumberMap = new HashMap();
        guarantorMemberMap = new HashMap();
        accountChargeMap = new HashMap();
        for (int i = 0, j = tblData.getRowCount(); i < j; i++) {
            selected = ((Boolean) tblData.getValueAt(i, 0)).booleanValue();
            if (selected) {
                tempList = new ArrayList();
                tempList.add((ArrayList) observable.getTableModel(tblData).get(i));
                if (prodType.equals("MDS")) {
                    actNum = String.valueOf(tblData.getValueAt(i, 2));
                } else {
                    actNum = String.valueOf(tblData.getValueAt(i, 1));
                }
                accountNumberMap.put(actNum, null);
                if (guarantorMap != null && guarantorMap.size() > 0) {
                    totalList = (ArrayList) guarantorMap.get(actNum);
                    if (totalList != null && totalList.size() > 0) {
                        for (int g = 0; g < totalList.size(); g++) {
                            rowList = (ArrayList) totalList.get(g);
                            selected = ((Boolean) rowList.get(0)).booleanValue();
                            if (selected) {
                                tempList.add(rowList);
                                guarantorMemberMap.put(actNum + rowList.get(3), null);
                            }
                        }
                    }
                }
                accountChargeMap.put(actNum, tempList);
            }
        }
        HashMap paramMap = new HashMap();
        paramMap.put("BranchCode", ProxyParameters.BRANCH_ID);
      //  paramMap.put("ProductId", observable.getCbmProdId().getKeyForSelected());
        paramMap.put("CURR_DATE", currDt);
        paramMap.put("AcctNum", param1);
        if (!prodType.equals("MDS")) {
        //    paramMap.put("AcctNum", guarantorGetSelected());
        }
        if (CommonUtil.convertObjToStr(txtFromAccountno.getText()).length() > 0) {
            paramMap.put("FROM_ACCT_NUM", txtFromAccountno.getText());
        }

        if (CommonUtil.convertObjToStr(txtTOAccountno.getText()).length() > 0) {
            paramMap.put("TO_ACCT_NUM", txtTOAccountno.getText());
        }
        TTIntegration ttIntegration1 = null;
        TTIntegration ttIntegration2 = null;
        ttIntegration.setParam(paramMap);
        ttIntegration1.setParam(paramMap);
        ttIntegration2.setParam(paramMap);
        System.out.println("param map for report : " + paramMap);
        generateNotice = true;
        String reportName = "ARCForm1";
        String reportName1 = "ARCForm2";
        String reportName2 = "ARCForm3";
        if (yesNo == 0) {
            ttIntegration.integrationForPrint(reportName, true);
            ttIntegration1.integrationForPrint(reportName1, true);
            ttIntegration2.integrationForPrint(reportName2, true);
        }
    } else {
        generateNotice = false;
        ClientUtil.displayAlert("No Records found...");
    }
}//GEN-LAST:event_btnPrintNoticeActionPerformed

private void btnClose1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClose1ActionPerformed
    observable.removeRowsFromGuarantorTable(tblData);
    dispose();
}//GEN-LAST:event_btnClose1ActionPerformed

private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
    ClientUtil.clearAll(this);
    panMultiSearch.setEnabled(false);
    panMultiSearch1.setEnabled(false);
    lblNoOfRecordsVal.setText("");
    setButtonEnableDisable();
    enableDisableComponants(false);
    valArcId.setText(""); // Added by nithya on 05-03-2016 for 0003914
    btnProcessEdit.setEnabled(true); // nithya
    lblTotalAmountVal.setText("");
    lblSelectedCountVal.setText("");
}//GEN-LAST:event_btnCancelActionPerformed

private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
    actionType = "NEW";
    panMultiSearch.setEnabled(true);
    panMultiSearch1.setEnabled(true);
    setButtonEnableDisable();    
    btnFilePrint.setEnabled(true);
    enableDisableComponants(true);
    btnGenerateNotice.setEnabled(false);
    chkIncludeClosed.setVisible(false);
}//GEN-LAST:event_btnNewActionPerformed
private void enableDisableComponants(boolean flag){
    cboProdId.setEnabled(flag);
    cboProdType.setEnabled(flag);
    txtFromAccountno.setEnabled(flag);
    btnFromAccountno.setEnabled(flag);
    txtTOAccountno.setEnabled(flag);
    btnTOAccountno.setEnabled(flag);
    tdtFromDate.setEnabled(flag);
    tdtToDate.setEnabled(flag);
    chkPrized.setEnabled(flag);
    chkIncludeClosed.setEnabled(flag);
    tdtArbDt.setEnabled(flag);
    txtArbRate.setEnabled(flag);
    tdtResDt.setEnabled(flag);
    txtResNo.setEnabled(flag);
    btnProcess.setEnabled(flag);
    btnGenerateNotice.setEnabled(flag);
    tdtReportingDate.setEnabled(flag); // Added by nithya on 05-03-2016 for 0003914
}
private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
actionType ="EDIT";
panMultiSearch.setEnabled(true);
panMultiSearch1.setEnabled(true);
setButtonEnableDisable();
enableDisableComponants(false);
btnProcessEdit.setEnabled(false); // nithya
btnPrint.setEnabled(true); // nithya
btnFilePrint.setEnabled(false);
popUp(EDITVIEW);
}//GEN-LAST:event_btnEditActionPerformed

private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_btnAuthorizeActionPerformed

private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
    if (actionType != null && actionType.equals("EDIT")) {
        ArrayList arbList = new ArrayList();
        for (int i = 0; i < tblData.getRowCount(); i++) {
            if (((Boolean) tblData.getValueAt(i, 0)).booleanValue()) {
                ArrayList list = new ArrayList();
                list.add(tblData.getValueAt(i, 1));
                list.add(tblData.getValueAt(i, 11));
                arbList.add(list);
            }
        }
        HashMap arbMap = new HashMap();
        arbMap.put("ARBITRATION_EDIT_LIST", arbList);
        arbMap.put("ARC_ID", valArcId.getText());
        HashMap resultMap = observable.updateCharges(arbMap);
        if (resultMap != null) {
            ClientUtil.showMessageWindow("Success \n" + resultMap.get("ARC_ID"));
            observable.removeRowsFromGuarantorTable(tblData);
           btnClearActionPerformed(null);
            valArcId.setText("");
        }
    }else{
        btnFilePrintActionPerformed(null);  // added by nithya on 10-03-2016
    }
}//GEN-LAST:event_btnSaveActionPerformed

private void tblDataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseClicked
// TODO add your handling code here:
}//GEN-LAST:event_tblDataMouseClicked

private void tblDataFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblDataFocusLost
// TODO add your handling code here:
    System.out.println("hererereerrererere");
    calTotalArc();
}//GEN-LAST:event_tblDataFocusLost

private void tblDataKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblDataKeyTyped
// TODO add your handling code here:
    System.out.println("hererereerrererere  tblDataKeyTyped");
    calTotalArc();
}//GEN-LAST:event_tblDataKeyTyped

    // Added by nithya
    private void btnProcessEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessEditActionPerformed
        // TODO add your handling code here:
        actionType ="PROCESS_EDIT";
        btnGenerateNotice.setEnabled(true);
        btnFilePrint.setEnabled(false);
        btnDelete.setEnabled(true); 
        btnEdit.setEnabled(false);
        popUp(PROCESSEDITVIEW);
    }//GEN-LAST:event_btnProcessEditActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPrintActionPerformed
 // added by nithya on 10-03-2016 for 4025
    private void btnFilePrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFilePrintActionPerformed
        // TODO add your handling code here:        
        btnSave.setEnabled(true);
        if (tblData.getRowCount() > 0) {
            if (!actionType.equals("EDIT")) {
                //  generateNotice();
                HashMap whereMap1 = new HashMap();
                whereMap1.put("CHARGE_TYPE", "ARC");
                List aList = ClientUtil.executeQuery("getArbChargeslab", whereMap1);
                ArrayList arbList = new ArrayList();
                String prodType = String.valueOf(cboProdType.getSelectedItem());
                if (prodType.equals("MDS")) {
                    for (int i = 0; i < tblData.getRowCount(); i++) {
                        if (((Boolean) tblData.getValueAt(i, 0)).booleanValue()) {
                            ArrayList list = new ArrayList();
                            list.add(tblData.getValueAt(i, 1));//chit no
                            list.add(tblData.getValueAt(i, 2));//men-no
                            list.add(tblData.getValueAt(i, 4));//no of
                            list.add(tblData.getValueAt(i, 5));//due amt
                            list.add(tblData.getValueAt(i, 6));//penal
                            list.add(tblData.getValueAt(i, 7));//chrge
                            list.add(tblData.getValueAt(i, 9));//arc fee
                            list.add(tblData.getValueAt(i, 10));//total arc
                            list.add(tblData.getValueAt(i, 11));//file
                            if (aList != null && aList.size() > 0) {
                                for (int j = 0; j < aList.size(); j++) {
                                    double amt = CommonUtil.convertObjToDouble(tblData.getValueAt(i, 10));
                                    whereMap1 = (HashMap) aList.get(j);
                                    double FROM_SLAB_AMT = CommonUtil.convertObjToDouble(whereMap1.get("FROM_SLAB_AMT"));
                                    double TO_SLAB_AMT = CommonUtil.convertObjToDouble(whereMap1.get("TO_SLAB_AMT"));
                                    if (amt >= FROM_SLAB_AMT && amt <= TO_SLAB_AMT) {
                                        String CHARGE_RATE = CommonUtil.convertObjToStr(whereMap1.get("CHARGE_RATE"));
                                        list.add(CHARGE_RATE);//rate
                                    }
                                }
                            } else {
                                list.add("0");//rate  
                            }                //
                            list.add(tblData.getValueAt(i, 12));//misc charge
                            arbList.add(list);
                        }
                    }
                } else if (prodType.equals("ROOMS")) {
                    for (int i = 0; i < tblData.getRowCount(); i++) {
                        if (((Boolean) tblData.getValueAt(i, 0)).booleanValue()) {
                            ArrayList list = new ArrayList();
                            list.add(tblData.getValueAt(i, 1));// room no
                            list.add(tblData.getValueAt(i, 2));// name
                            list.add(tblData.getValueAt(i, 3));// due
                            list.add(tblData.getValueAt(i, 4));// penal
                            list.add(tblData.getValueAt(i, 5));// total due
                            list.add(tblData.getValueAt(i, 6));// arc fee
                            list.add(tblData.getValueAt(i, 7));// total arc
                            list.add(tblData.getValueAt(i, 8));//file                       
                            if (aList != null && aList.size() > 0) {
                                for (int j = 0; j < aList.size(); j++) {
                                    double amt = CommonUtil.convertObjToDouble(tblData.getValueAt(i, 7));
                                    whereMap1 = (HashMap) aList.get(j);
                                    double FROM_SLAB_AMT = CommonUtil.convertObjToDouble(whereMap1.get("FROM_SLAB_AMT"));
                                    double TO_SLAB_AMT = CommonUtil.convertObjToDouble(whereMap1.get("TO_SLAB_AMT"));
                                    if (amt >= FROM_SLAB_AMT && amt <= TO_SLAB_AMT) {
                                        String CHARGE_RATE = CommonUtil.convertObjToStr(whereMap1.get("CHARGE_RATE"));
                                        list.add(CHARGE_RATE);//rate
                                    }
                                }
                            } else {
                                list.add("0");//rate  
                            }                //
                            list.add(tblData.getValueAt(i, 9));//misc charge
                            arbList.add(list);
                        }
                    }
                } else {
                    for (int i = 0; i < tblData.getRowCount(); i++) {
                        if (((Boolean) tblData.getValueAt(i, 0)).booleanValue()) {
                            ArrayList list = new ArrayList();
                            list.add(tblData.getValueAt(i, 1));
                            list.add(tblData.getValueAt(i, 2));
                            list.add(tblData.getValueAt(i, 4));
                            list.add(tblData.getValueAt(i, 5));
                            list.add(tblData.getValueAt(i, 6));
                            list.add(tblData.getValueAt(i, 7));
                            list.add(tblData.getValueAt(i, 9));
                            list.add(tblData.getValueAt(i, 10));
                            list.add(tblData.getValueAt(i, 11));
                            //
                            if (aList != null && aList.size() > 0) {
                                for (int j = 0; j < aList.size(); j++) {
                                    double amt = CommonUtil.convertObjToDouble(tblData.getValueAt(i, 10));
                                    whereMap1 = (HashMap) aList.get(j);
                                    double FROM_SLAB_AMT = CommonUtil.convertObjToDouble(whereMap1.get("FROM_SLAB_AMT"));
                                    double TO_SLAB_AMT = CommonUtil.convertObjToDouble(whereMap1.get("TO_SLAB_AMT"));
                                    if (amt >= FROM_SLAB_AMT && amt <= TO_SLAB_AMT) {
                                        String CHARGE_RATE = CommonUtil.convertObjToStr(whereMap1.get("CHARGE_RATE"));
                                        list.add(CHARGE_RATE);
                                    }
                                }
                            } else {
                                list.add("0");//rate  
                            }                //
                            list.add(tblData.getValueAt(i, 12));//misc charge
                            arbList.add(list);
                        }
                    }
                }
                HashMap arbMap = new HashMap();
                arbMap.put("ARBITRATION_POST_LIST", arbList);
                if (cboProdId.getSelectedIndex() > 0) { // nithya
                    String prodId = ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected().toString();
                    arbMap.put("PROD_ID", prodId);
                }
                if (prodType.equals("MDS")) {
                    arbMap.put("PROD_TYPE", "MDS");
                } else if (prodType.equals("ROOMS")) {
                    arbMap.put("PROD_TYPE", "ROOMS");
                } else {
                    arbMap.put("PROD_TYPE", "TL");
                }
                arbMap.put("ARC_ID", arc_Id); // nithya
                actionType = "";
                observable.setTdtAuctionDate(DateUtil.getDateMMDDYYYY(tdtArbDt.getDateValue()));
                observable.setTxtArbRate(txtArbRate.getText());
                observable.setReportingDate(DateUtil.getDateMMDDYYYY(tdtReportingDate.getDateValue())); // Added by nithya on 05-03-2016 for 0003914
                HashMap resultMap = observable.insertCharges(arbMap);
                if (observable.getResult().equals(ClientConstants.RESULT_STATUS[1])) {
                    ClientUtil.showMessageWindow("Filing Success :\n" + resultMap.get("ARC_ID"));
                    observable.removeRowsFromGuarantorTable(tblData);
                    ClientUtil.clearAll(this);
                    lblTotalAmountVal.setText("");
                    lblSelectedCountVal.setText("");
                    valArcId.setText("");
                }
            }
        }
    }//GEN-LAST:event_btnFilePrintActionPerformed

     // added by nithya on 10-03-2016
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        HashMap deleteWhereMap = new HashMap();
        ArrayList accountNoList = new ArrayList();
        deleteWhereMap.put("ARC_ID",valArcId.getText());
        deleteWhereMap.put("DELETE_ARC","DELETE_ARC");
        for (int i = 0; i < tblData.getRowCount(); i++) {
          if (((Boolean) tblData.getValueAt(i, 0)).booleanValue()) {               
                accountNoList.add(tblData.getValueAt(i, 1));
           }
        }        
        deleteWhereMap.put("ACCOUNT_LIST",accountNoList);       
        HashMap resultMap = observable.deleteAccountsFromARC(deleteWhereMap);
        TableSorter tblSorter = (TableSorter) tblData.getModel();
        TableModel tblModel = (TableModel) tblSorter.getModel();
        if (observable.getResult().equals(ClientConstants.RESULT_STATUS[3])) {
            ClientUtil.showMessageWindow("Successfully deleted" + accountNoList +" from :" + deleteWhereMap.get("ARC_ID"));            
            for (int i = 0; i < tblData.getRowCount(); i++) {
                for(int j=0; j < accountNoList.size(); j++){
                    if(tblData.getValueAt(i, 1).equals(accountNoList.get(j))){
                        tblModel.removeRow(i);
                        
                    }
                }
                
            }
            tblModel = null;
            tblSorter = null;
            lblNoOfRecordsVal.setText(String.valueOf(tblData.getRowCount()));
            lblTotalAmountVal.setText("");
            lblSelectedCountVal.setText("");
        }
        
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnRecalculateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRecalculateActionPerformed
        // TODO add your handling code here:
        String arcFeeRoundOff = "";
        String finalFee = "";
        HashMap whereMap1 = new HashMap();
        whereMap1.put("CHARGE_TYPE", "ARC");
        List aList = ClientUtil.executeQuery("getArbChargeslab", whereMap1);
        if (aList == null && aList.size() <= 0) {
            ClientUtil.showMessageWindow("Configure Arbitration Charge details.. ");
        }
        if (aList != null && aList.size() > 0) {
            HashMap arbMap = (HashMap) aList.get(0);
            arcFeeRoundOff = CommonUtil.convertObjToStr(arbMap.get("ARC_FEE_ROUNDOFF"));
        }
        if(!actionType.equals("PROCESS_EDIT")){
            for (int i = 0; i < tblData.getRowCount(); i++) {
                double fee = 0;
                double amount = 0.0;
                if (cboProdType.getSelectedItem().equals("MDS")) {
                 amount = CommonUtil.convertObjToDouble(tblData.getValueAt(i, 5))+
                    CommonUtil.convertObjToDouble(tblData.getValueAt(i, 6))+CommonUtil.convertObjToDouble(tblData.getValueAt(i, 7)) + CommonUtil.convertObjToDouble(tblData.getValueAt(i, 12)) ; 
                }else{
                amount = CommonUtil.convertObjToDouble(tblData.getValueAt(i, 4)) + CommonUtil.convertObjToDouble(tblData.getValueAt(i, 5))
                        + CommonUtil.convertObjToDouble(tblData.getValueAt(i, 6)) + CommonUtil.convertObjToDouble(tblData.getValueAt(i, 7)) + CommonUtil.convertObjToDouble(tblData.getValueAt(i, 12));
                } 
                if (aList != null && aList.size() > 0) {
                    for (int j = 0; j < aList.size(); j++) {
                        whereMap1 = (HashMap) aList.get(j);
                        double FROM_SLAB_AMT = CommonUtil.convertObjToDouble(whereMap1.get("FROM_SLAB_AMT"));
                        double TO_SLAB_AMT = CommonUtil.convertObjToDouble(whereMap1.get("TO_SLAB_AMT"));
                        if (amount >= FROM_SLAB_AMT && amount <= TO_SLAB_AMT) {
                            double CHARGE_RATE = CommonUtil.convertObjToDouble(whereMap1.get("CHARGE_RATE"));
                            double MIN_CHARGE_AMOUNT = CommonUtil.convertObjToDouble(whereMap1.get("MIN_CHARGE_AMOUNT"));
                            double MAX_CHARGE_AMOUNT = CommonUtil.convertObjToDouble(whereMap1.get("MAX_CHARGE_AMOUNT"));
                            fee = (amount * CHARGE_RATE) / 100;
                            if (fee <= MIN_CHARGE_AMOUNT) {
                                fee = MIN_CHARGE_AMOUNT;
                            }
                            if (fee >= MAX_CHARGE_AMOUNT) {
                                fee = MAX_CHARGE_AMOUNT;
                            }
                        }
                    }
                } else {
                    fee = (amount * CommonUtil.convertObjToDouble(observable.getTxtArbRate())) / 100;
                }
                if (arcFeeRoundOff.equalsIgnoreCase("Higher Value")) {// Added by nithya on 12-12-2019 for KD-1066
                    finalFee = String.valueOf(Math.ceil(fee));                    
                }else{
                    finalFee = observable.getRoundVal(fee);
                }
                tblData.setValueAt(finalFee, i, 9);
                //tblData.setValueAt(observable.getRoundVal(fee), i, 9);
                if (cboProdType.getSelectedItem().equals("MDS")) {
                    amount = CommonUtil.convertObjToDouble(tblData.getValueAt(i, 5))
                            + CommonUtil.convertObjToDouble(tblData.getValueAt(i, 6)) + CommonUtil.convertObjToDouble(tblData.getValueAt(i, 7)) + CommonUtil.convertObjToDouble(tblData.getValueAt(i, 12));
                    System.out.println("amount :: " + amount);
                    tblData.setValueAt(amount, i, 8);
                    tblData.setValueAt((amount + CommonUtil.convertObjToDouble(tblData.getValueAt(i, 9))), i, 10);
                }else{
                amount = CommonUtil.convertObjToDouble(tblData.getValueAt(i, 4)) + CommonUtil.convertObjToDouble(tblData.getValueAt(i, 5))
                        + CommonUtil.convertObjToDouble(tblData.getValueAt(i, 6)) + CommonUtil.convertObjToDouble(tblData.getValueAt(i, 7)) + CommonUtil.convertObjToDouble(tblData.getValueAt(i, 12));
                tblData.setValueAt(amount, i, 8);
                tblData.setValueAt((amount + CommonUtil.convertObjToDouble(tblData.getValueAt(i, 9))), i, 10);
                }
            }
        }else if(actionType.equals("PROCESS_EDIT")){
           for (int i = 0; i < tblData.getRowCount(); i++) {
                double fee = 0;
                double amount = 0.0;
                 if (cboProdType.getSelectedItem().equals("MDS")) {
                 amount = CommonUtil.convertObjToDouble(tblData.getValueAt(i, 5))+
                    CommonUtil.convertObjToDouble(tblData.getValueAt(i, 6))+CommonUtil.convertObjToDouble(tblData.getValueAt(i, 7)) + CommonUtil.convertObjToDouble(tblData.getValueAt(i, 12)); 
                }else{
                amount = CommonUtil.convertObjToDouble(tblData.getValueAt(i, 4)) + CommonUtil.convertObjToDouble(tblData.getValueAt(i, 5))
                        + CommonUtil.convertObjToDouble(tblData.getValueAt(i, 6)) + CommonUtil.convertObjToDouble(tblData.getValueAt(i, 7)) + CommonUtil.convertObjToDouble(tblData.getValueAt(i, 12));
                 }  
                if (aList != null && aList.size() > 0) {
                    for (int j = 0; j < aList.size(); j++) {
                        whereMap1 = (HashMap) aList.get(j);
                        double FROM_SLAB_AMT = CommonUtil.convertObjToDouble(whereMap1.get("FROM_SLAB_AMT"));
                        double TO_SLAB_AMT = CommonUtil.convertObjToDouble(whereMap1.get("TO_SLAB_AMT"));
                        if (amount >= FROM_SLAB_AMT && amount <= TO_SLAB_AMT) {
                            double CHARGE_RATE = CommonUtil.convertObjToDouble(whereMap1.get("CHARGE_RATE"));
                            double MIN_CHARGE_AMOUNT = CommonUtil.convertObjToDouble(whereMap1.get("MIN_CHARGE_AMOUNT"));
                            double MAX_CHARGE_AMOUNT = CommonUtil.convertObjToDouble(whereMap1.get("MAX_CHARGE_AMOUNT"));
                            fee = (amount * CHARGE_RATE) / 100;
                            if (fee <= MIN_CHARGE_AMOUNT) {
                                fee = MIN_CHARGE_AMOUNT;
                            }
                            if (fee >= MAX_CHARGE_AMOUNT) {
                                fee = MAX_CHARGE_AMOUNT;
                            }
                        }
                    }
                } else {
                    fee = (amount * CommonUtil.convertObjToDouble(observable.getTxtArbRate())) / 100;
                }
                if (arcFeeRoundOff.equalsIgnoreCase("Higher Value")) {// Added by nithya on 12-12-2019 for KD-1066
                    finalFee = String.valueOf(Math.ceil(fee));                    
                }else{
                    finalFee = observable.getRoundVal(fee);
                }
                tblData.setValueAt(finalFee, i, 8);
                //tblData.setValueAt(observable.getRoundVal(fee), i, 8);
               if (cboProdType.getSelectedItem().equals("MDS")) { //Added for KD-3342
                      amount = CommonUtil.convertObjToDouble(tblData.getValueAt(i, 5))
                               + CommonUtil.convertObjToDouble(tblData.getValueAt(i, 6)) + CommonUtil.convertObjToDouble(tblData.getValueAt(i, 7)) + CommonUtil.convertObjToDouble(tblData.getValueAt(i, 12));
                       tblData.setValueAt(amount, i, 8);
                       tblData.setValueAt((amount + CommonUtil.convertObjToDouble(tblData.getValueAt(i, 9))), i, 10);
                   
               }else{
                amount = CommonUtil.convertObjToDouble(tblData.getValueAt(i, 4)) + CommonUtil.convertObjToDouble(tblData.getValueAt(i, 5))
                        + CommonUtil.convertObjToDouble(tblData.getValueAt(i, 6)) + CommonUtil.convertObjToDouble(tblData.getValueAt(i, 7)) + CommonUtil.convertObjToDouble(tblData.getValueAt(i, 12));
               // tblData.setValueAt(amount, i, 8);
                tblData.setValueAt((amount + CommonUtil.convertObjToDouble(tblData.getValueAt(i, 8))), i, 9);
               }
            } 
        }
    }//GEN-LAST:event_btnRecalculateActionPerformed

    private void chkIncludeClosedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkIncludeClosedActionPerformed
        // TODO add your handling code here:
        if(chkIncludeClosed.isSelected()){
            chkPrized.setSelected(false);
            chkNonPrized.setSelected(false);
        }
    }//GEN-LAST:event_chkIncludeClosedActionPerformed
 private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        btnDelete.setEnabled(!btnDelete.isEnabled());
        btnSave.setEnabled(!btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
        btnProcessEdit.setEnabled(!btnView.isEnabled()); // Added by nithya
    }
 
    private void internationalize() {
//        lblSearch.setText(resourceBundle.getString("lblSearch"));
//        btnSearch.setText(resourceBundle.getString("btnSearch"));
//        chkCase.setText(resourceBundle.getString("chkCase"));
//        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
//        btnCancel.setText(resourceBundle.getString("btnCancel"));
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
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClear;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnClose1;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnFilePrint;
    private com.see.truetransact.uicomponent.CButton btnFromAccountno;
    private com.see.truetransact.uicomponent.CButton btnGenerateNotice;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnPrintNotice;
    private com.see.truetransact.uicomponent.CButton btnProcess;
    private com.see.truetransact.uicomponent.CButton btnProcessEdit;
    private com.see.truetransact.uicomponent.CButton btnRecalculate;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnTOAccountno;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CPanel cPanel1;
    private com.see.truetransact.uicomponent.CPanel cPanel2;
    private com.see.truetransact.uicomponent.CPanel cPanel3;
    private com.see.truetransact.uicomponent.CComboBox cboProdId;
    private com.see.truetransact.uicomponent.CComboBox cboProdType;
    private com.see.truetransact.uicomponent.CCheckBox chkDueLoans;
    private com.see.truetransact.uicomponent.CCheckBox chkIncludeClosed;
    private com.see.truetransact.uicomponent.CCheckBox chkNonPrized;
    private com.see.truetransact.uicomponent.CCheckBox chkPrized;
    private com.see.truetransact.uicomponent.CCheckBox chkSelectAll;
    private com.see.truetransact.uicomponent.CLabel lbSpace2;
    private com.see.truetransact.uicomponent.CLabel lbSpace3;
    private com.see.truetransact.uicomponent.CLabel lblActHoldersList;
    private com.see.truetransact.uicomponent.CLabel lblArbDt;
    private com.see.truetransact.uicomponent.CLabel lblArbRate;
    private com.see.truetransact.uicomponent.CLabel lblArcId;
    private com.see.truetransact.uicomponent.CLabel lblFromAccountno;
    private com.see.truetransact.uicomponent.CLabel lblFromDate;
    private com.see.truetransact.uicomponent.CLabel lblNoOfRecords;
    private com.see.truetransact.uicomponent.CLabel lblNoOfRecordsVal;
    private com.see.truetransact.uicomponent.CLabel lblProdId;
    private com.see.truetransact.uicomponent.CLabel lblProdType;
    private com.see.truetransact.uicomponent.CLabel lblReportingDate;
    private com.see.truetransact.uicomponent.CLabel lblResDt;
    private com.see.truetransact.uicomponent.CLabel lblResNo;
    private com.see.truetransact.uicomponent.CLabel lblSelectedCount;
    private com.see.truetransact.uicomponent.CLabel lblSelectedCountVal;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace70;
    private com.see.truetransact.uicomponent.CLabel lblSpace71;
    private com.see.truetransact.uicomponent.CLabel lblSpace72;
    private com.see.truetransact.uicomponent.CLabel lblSpace73;
    private com.see.truetransact.uicomponent.CLabel lblSpace74;
    private com.see.truetransact.uicomponent.CLabel lblSpace75;
    private com.see.truetransact.uicomponent.CLabel lblTOAccountno;
    private com.see.truetransact.uicomponent.CLabel lblToDate;
    private com.see.truetransact.uicomponent.CLabel lblTotal;
    private com.see.truetransact.uicomponent.CLabel lblTotalAmountVal;
    private com.see.truetransact.uicomponent.CPanel panArbit;
    private com.see.truetransact.uicomponent.CPanel panMultiSearch;
    private com.see.truetransact.uicomponent.CPanel panMultiSearch1;
    private com.see.truetransact.uicomponent.CPanel panPost;
    private com.see.truetransact.uicomponent.CPanel panRes;
    private com.see.truetransact.uicomponent.CPanel panSearch;
    private com.see.truetransact.uicomponent.CPanel panSearchCondition;
    private com.see.truetransact.uicomponent.CPanel panTable;
    private com.see.truetransact.uicomponent.CButtonGroup rdbArbit;
    private com.see.truetransact.uicomponent.CRadioButton rdoArbFiling;
    private com.see.truetransact.uicomponent.CRadioButton rdoArbPosting;
    private com.see.truetransact.uicomponent.CSeparator sptLine;
    private com.see.truetransact.uicomponent.CScrollPane srcTable;
    private com.see.truetransact.uicomponent.CTable tblData;
    private com.see.truetransact.uicomponent.CToolBar tbrTokenConfig;
    private com.see.truetransact.uicomponent.CDateField tdtArbDt;
    private com.see.truetransact.uicomponent.CDateField tdtFromDate;
    private com.see.truetransact.uicomponent.CDateField tdtReportingDate;
    private com.see.truetransact.uicomponent.CDateField tdtResDt;
    private com.see.truetransact.uicomponent.CDateField tdtToDate;
    private com.see.truetransact.uicomponent.CTextField txtArbRate;
    private com.see.truetransact.uicomponent.CTextField txtFromAccountno;
    private com.see.truetransact.uicomponent.CTextField txtResNo;
    private com.see.truetransact.uicomponent.CTextField txtTOAccountno;
    private com.see.truetransact.uicomponent.CLabel valArcId;
    // End of variables declaration//GEN-END:variables
}
