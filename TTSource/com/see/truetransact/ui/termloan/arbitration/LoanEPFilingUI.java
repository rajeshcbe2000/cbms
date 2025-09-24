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
import com.see.truetransact.ui.common.viewall.ViewAll;

import com.see.truetransact.ui.common.viewall.TableDialogUI;
import com.see.truetransact.ui.TrueTransactMain;

/**
 * @author balachandar
 */
public class LoanEPFilingUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer, ListSelectionListener {
//    private final AuthorizeRB resourceBundle = new AuthorizeRB();

    private LoanEPFilingOB observable;
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
    int FROMACTNO = 1, TOACTNO = 2;
    private final static Logger log = Logger.getLogger(LoanEPFilingUI.class);
    private boolean isEdit=false;
    /**
     * Creates new form AuthorizeUI
     */
    public LoanEPFilingUI() {
        setupInit();
        setupScreen();
        //tblData.setAutoCreateRowSorter(true);
lblProdid.setVisible(false);
cboProdId.setVisible(false);
    }

    /**
     * Creates new form AuthorizeUI
     */
    public LoanEPFilingUI(CInternalFrame parent, HashMap paramMap) {
        this.parent = parent;
        this.paramMap = paramMap;
        setupInit();
        setupScreen();

    }

    private void setupInit() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        //   addtoBtnGp();
        internationalize();
        setObservable();
          createCboProdType();
        createCboNoticeType();
//        txtNoOfInstallments.setValidation(new com.see.truetransact.uivalidation.NumericValidation());
//        txtNoOfInstallments.setMaxLength(3);
//        txtLastNoticeSentBefore.setValidation(new com.see.truetransact.uivalidation.NumericValidation());
//        txtLastNoticeSentBefore.setMaxLength(3);
        bankName = ((String) TrueTransactMain.BANKINFO.get("BANK_NAME")).toUpperCase();

//        txtNoticeCharge.setAllowAll(true);
//        txtPostageCharge.setAllowAll(true);

//        txtArbRate.setAllowAll(true);
//        btnsms.setVisible(false);

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
            cboProdType.addItem("OVERDRAFT");
//            cboProdType.addItem("Gold Loans");
//            cboProdType.addItem("Other Loans");
//            cboProdType.addItem("MDS Loans");
//            cboProdType.addItem("MDS");
        }
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
            observable = new LoanEPFilingOB();
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
        // String prodType = String.valueOf(cboProdType.getSelectedItem());

        previousRow = rowIndexSelected;
        calTotalAmount();
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
        panMultiSearch1 = new com.see.truetransact.uicomponent.CPanel();
        panArbit = new com.see.truetransact.uicomponent.CPanel();
        lblFilingDt = new com.see.truetransact.uicomponent.CLabel();
        tdtFilingDt = new com.see.truetransact.uicomponent.CDateField();
        cboProdType = new com.see.truetransact.uicomponent.CComboBox();
        lblProdid = new com.see.truetransact.uicomponent.CLabel();
        lblProdType1 = new com.see.truetransact.uicomponent.CLabel();
        cboProdId = new com.see.truetransact.uicomponent.CComboBox();
        panPost = new com.see.truetransact.uicomponent.CPanel();
        btnProcess = new com.see.truetransact.uicomponent.CButton();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        panTable = new com.see.truetransact.uicomponent.CPanel();
        chkSelectAll = new com.see.truetransact.uicomponent.CCheckBox();
        srcTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblData = new com.see.truetransact.uicomponent.CTable();
        lblToDate1 = new com.see.truetransact.uicomponent.CLabel();
        lblNoOfRecords = new com.see.truetransact.uicomponent.CLabel();
        lblNoOfRecordsVal = new com.see.truetransact.uicomponent.CLabel();
        panSearch = new com.see.truetransact.uicomponent.CPanel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        btnClear1 = new com.see.truetransact.uicomponent.CButton();
        btnPrintNotice = new com.see.truetransact.uicomponent.CButton();
        sptLine = new com.see.truetransact.uicomponent.CSeparator();
        cPanel1 = new com.see.truetransact.uicomponent.CPanel();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        lblSelectedRecords = new com.see.truetransact.uicomponent.CLabel();
        cLabel3 = new com.see.truetransact.uicomponent.CLabel();
        lblTotalEPFee = new com.see.truetransact.uicomponent.CLabel();
        cLabel6 = new com.see.truetransact.uicomponent.CLabel();

        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setTitle("EP Filing");
        setMinimumSize(new java.awt.Dimension(800, 630));
        setPreferredSize(new java.awt.Dimension(800, 630));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        panSearchCondition.setMinimumSize(new java.awt.Dimension(574, 150));
        panSearchCondition.setPreferredSize(new java.awt.Dimension(574, 150));
        panSearchCondition.setLayout(new java.awt.GridBagLayout());

        panMultiSearch1.setMaximumSize(new java.awt.Dimension(450, 150));
        panMultiSearch1.setMinimumSize(new java.awt.Dimension(450, 150));
        panMultiSearch1.setPreferredSize(new java.awt.Dimension(450, 150));
        panMultiSearch1.setLayout(new java.awt.GridBagLayout());

        panArbit.setMinimumSize(new java.awt.Dimension(400, 65));
        panArbit.setPreferredSize(new java.awt.Dimension(400, 65));
        panArbit.setLayout(new java.awt.GridBagLayout());

        lblFilingDt.setText("Filing Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panArbit.add(lblFilingDt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panArbit.add(tdtFilingDt, gridBagConstraints);

        cboProdType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdType.setPopupWidth(160);
        cboProdType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 11, 2, 4);
        panArbit.add(cboProdType, gridBagConstraints);

        lblProdid.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panArbit.add(lblProdid, gridBagConstraints);

        lblProdType1.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panArbit.add(lblProdType1, gridBagConstraints);

        cboProdId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdId.setPopupWidth(160);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 11, 2, 4);
        panArbit.add(cboProdId, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panMultiSearch1.add(panArbit, gridBagConstraints);

        panPost.setMaximumSize(new java.awt.Dimension(200, 50));
        panPost.setMinimumSize(new java.awt.Dimension(200, 50));
        panPost.setPreferredSize(new java.awt.Dimension(200, 50));
        panPost.setLayout(new java.awt.GridBagLayout());

        btnProcess.setText("Show ");
        btnProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panPost.add(btnProcess, gridBagConstraints);

        btnEdit.setText("Edit");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panPost.add(btnEdit, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        panMultiSearch1.add(panPost, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        panSearchCondition.add(panMultiSearch1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(panSearchCondition, gridBagConstraints);

        panTable.setMinimumSize(new java.awt.Dimension(600, 350));
        panTable.setPreferredSize(new java.awt.Dimension(600, 350));
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

        srcTable.setViewport(srcTable.getRowHeader());

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
        tblData.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tblDataPropertyChange(evt);
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

        lblToDate1.setText("Loan Account Holders List");
        lblToDate1.setMaximumSize(new java.awt.Dimension(230, 85));
        lblToDate1.setMinimumSize(new java.awt.Dimension(186, 18));
        lblToDate1.setPreferredSize(new java.awt.Dimension(186, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 4);
        panTable.add(lblToDate1, gridBagConstraints);

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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(panTable, gridBagConstraints);

        panSearch.setLayout(new java.awt.GridBagLayout());

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        panSearch.add(btnSave, gridBagConstraints);

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
        gridBagConstraints.gridx = 4;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearch.add(btnClear1, gridBagConstraints);

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
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        panSearch.add(btnPrintNotice, gridBagConstraints);

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

        cPanel1.setMinimumSize(new java.awt.Dimension(800, 35));
        cPanel1.setPreferredSize(new java.awt.Dimension(800, 35));
        cPanel1.setLayout(new java.awt.GridBagLayout());

        cLabel1.setText("Total Records Selected : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 30, 9, 0);
        cPanel1.add(cLabel1, gridBagConstraints);

        lblSelectedRecords.setText("cLabel2");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 10, 9, 0);
        cPanel1.add(lblSelectedRecords, gridBagConstraints);

        cLabel3.setText("Total EP Fee :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 41, 9, 0);
        cPanel1.add(cLabel3, gridBagConstraints);

        lblTotalEPFee.setText("cLabel4");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 27, 9, 310);
        cPanel1.add(lblTotalEPFee, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        cPanel1.add(cLabel6, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        getContentPane().add(cPanel1, gridBagConstraints);

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
//   
    }

    private void btnClear1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClear1ActionPerformed
        // TODO add your handling code here:
        ClientUtil.clearAll(this);
        //   btnsms.setVisible(false);
//        btnPostCharges.setEnabled(true);
        // lblTotalChargeAmountVal.setText("");
        //lblSelectedRecordVal.setText("");
        lblNoOfRecordsVal.setText("");
        isEdit=false;
    }//GEN-LAST:event_btnClear1ActionPerformed

    private void tblDataMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseReleased
        // TODO add your handling code here:
        if (/*(evt.getClickCount() == 2) && */(evt.getModifiers() == 16)) {
            whenTableRowSelected();
            setSelectedRecord();
        }
    }//GEN-LAST:event_tblDataMouseReleased
    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
        System.out.println("Hash: " + hash);
        if (isEdit) {
            HashMap viewMap = new HashMap();
            HashMap whereMap = new HashMap();
            viewMap.put(CommonConstants.MAP_NAME, "getArbitrationAwardEditDetails");
            whereMap.put("EP_ID", CommonUtil.convertObjToStr(hash.get("EP_ID")));
            whereMap.put("TODAY_DT", getProperDate(currDt));
            whereMap.put("CURR_DATE", getProperDate(currDt));
            whereMap.put("EDIT", "Y");
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            try {
                log.info("populateData...");
                ArrayList heading = observable.populateData(viewMap, tblData);
                lblNoOfRecordsVal.setText(String.valueOf(tblData.getRowCount()));
                heading = null;
            } catch (Exception e) {
                System.err.println("Exception " + e.toString() + "Caught");
                e.printStackTrace();
            }
            viewMap = null;
            whereMap = null;
        }
    }

    private void popUp(int field) {
        final HashMap viewMap = new HashMap();
        viewType = field;
        HashMap hash = new HashMap();


        new ViewAll(this, viewMap).show();
    }

    private void tblDataMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseMoved
        Point p = evt.getPoint();
        String tip =
                String.valueOf(
                tblData.getModel().getValueAt(
                tblData.rowAtPoint(p),
                tblData.columnAtPoint(p)));
        tblData.setToolTipText(tip);
        //calTotalAmount();
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
        //calTotalAmount();
    }//GEN-LAST:event_tblDataMousePressed

    private void chkSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSelectAllActionPerformed
        // Add your handling code here:
        observable.setSelectAll(tblData, new Boolean(chkSelectAll.isSelected()));
        setSelectedRecord();
        calTotalAmount();
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

    public void populateData() {
//        updateOBFields();
        String behavesLike = "";
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        boolean isOK = true;
        System.out.println("chkng");
        

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

            List lst = ClientUtil.executeQuery("TermLoan.getProdHead", whereMap);
            if (lst != null && lst.size() > 0) {
                whereMap = (HashMap) lst.get(0);
                behavesLike = CommonUtil.convertObjToStr(whereMap.get("BEHAVES_LIKE"));
            }
            String prodId = "";
            if (!isEdit) {
                String prod_Type = CommonUtil.convertObjToStr(cboProdType.getSelectedItem());

                if (cboProdId.getSelectedItem() != null) {
                    prodId = CommonUtil.convertObjToStr(((ComboBoxModel) cboProdId.getModel()).getKeyForSelected().toString());
                }
                if (prod_Type != null && prod_Type.equals("MDS")) {
                    if (isEdit) {
                        viewMap.put(CommonConstants.MAP_NAME, "getArbitrationAwardMDSEditDetails");
                    } else {
                        viewMap.put(CommonConstants.MAP_NAME, "getArbitrationAwardMDSDetails");
                    }
                }else if (prod_Type != null && prod_Type.equals("OVERDRAFT")) { //KD-3449
                    if (isEdit) {
                        //viewMap.put(CommonConstants.MAP_NAME, "getArbitrationAwardMDSEditDetails");
                    } else {
                        viewMap.put(CommonConstants.MAP_NAME, "getOverDraftArbitrationAwardDetails");
                    }
                } else {
                    if (isEdit) {
                        viewMap.put(CommonConstants.MAP_NAME, "getArbitrationAwardEditDetails");
                    } else {
                        viewMap.put(CommonConstants.MAP_NAME, "getArbitrationAwardDetails");
                    }
                }
            }
            //                whereMap.put("ACT_NUM", observable.getTxtAccNo());


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

            if (tdtFilingDt.getDateValue() != null && tdtFilingDt.getDateValue().length() > 0) {
                whereMap.put("FILING_DT", getProperDate(DateUtil.getDateMMDDYYYY(tdtFilingDt.getDateValue())));
            }

            whereMap.put("PROD_ID", prodId);
            whereMap.put("TODAY_DT", getProperDate(currDt));
            whereMap.put("CURR_DATE", getProperDate(currDt));
            if(isEdit)
               whereMap.put("EDIT","Y");
            else
              whereMap.put("EDIT","N");   
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
             if(isEdit){
                    viewMap.put(CommonConstants.MAP_NAME, "getArbitrationAwardViewDetails");
                      new ViewAll(this, viewMap).show();
                }
            
           
            try {
                log.info("populateData...");
                // observable.removeRowsFromGuarantorTable(tblGuarantorData);
            //
                if (!isEdit) {
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
                }
            } catch (Exception e) {
                System.err.println("Exception " + e.toString() + "Caught");
                e.printStackTrace();
            }
        }
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

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here: 

        //  generateNotice();
        ArrayList lstTotAward = new ArrayList();
        if (tblData.getRowCount() > 0) {
            for (int i = 0; i < tblData.getRowCount(); i++) {
                ArrayList lstAward = new ArrayList();
                if (((Boolean) tblData.getValueAt(i, 0)).booleanValue()) {
                    for (int j = 1; j < tblData.getColumnCount(); j++) {
                        lstAward.add(tblData.getValueAt(i, j));
                    }
                    lstTotAward.add(lstAward);
                }

            }
        }
         String prodType = String.valueOf(cboProdType.getSelectedItem());
        HashMap arbMap = new HashMap();
        arbMap.put("EP_LIST", lstTotAward);
        arbMap.put("PROD_TYPE", prodType);
        arbMap.put("FILING_DT", getProperDate(DateUtil.getDateMMDDYYYY(tdtFilingDt.getDateValue())));
        HashMap resultMap = observable.insertCharges(arbMap);
        if(isEdit)
        {
            ClientUtil.showMessageWindow("File No updated Succesfully");
            observable.removeRowsFromGuarantorTable(tblData);
            ClientUtil.clearAll(this);
        }
        else
        {
        if (observable.getResult().equals(ClientConstants.RESULT_STATUS[2])) {
            ClientUtil.showMessageWindow("Filing Completed \n"+resultMap.get("ARC_ID"));
            displayPrintDetails(CommonUtil.convertObjToStr(resultMap.get("ARC_ID")));
            observable.removeRowsFromGuarantorTable(tblData);
            ClientUtil.clearAll(this);
        }
        }
        isEdit=false;
//        if (btnPostCharges.isEnabled()) {
//            int confirm = ClientUtil.confirmationAlert("Postage Charges Not Yet Applied" + "\n" + "Do you want to Apply");
//            if (confirm == 0) {
//                btnPostChargesActionPerformed(null);
//            }
//        }

    }//GEN-LAST:event_btnSaveActionPerformed
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

        populateData();
    }//GEN-LAST:event_btnProcessActionPerformed

     public void calTotalAmount(){        
        if(tblData.getRowCount()>0){    
            int count = tblData.getRowCount();
            double total = 0.0;
            double selectedCount =  0.0;
            double totalCharge = 0.0;
            double totalArcFee = 0.0;
            for(int i=0; i < count; i++){
                if((Boolean)tblData.getValueAt(i, 0)){
                    selectedCount ++;
                    totalCharge += CommonUtil.convertObjToDouble(tblData.getValueAt(i, 12));                  
                }
            }
            lblSelectedRecords.setText(CommonUtil.convertObjToStr(selectedCount));  
            lblTotalEPFee.setText(CommonUtil.convertObjToStr(totalCharge));            
        }
    }
    
    
    
    private void tblDataPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tblDataPropertyChange
        // TODO add your handling code here:
         double amt=0.0;
        if(tblData.getSelectedRowCount()==1 && (tblData.getEditingColumn()==3 || tblData.getEditingColumn()==4 || tblData.getEditingColumn()==5 ||
                tblData.getEditingColumn()==6 ||tblData.getEditingColumn()==9 ||tblData.getEditingColumn()==10 ||tblData.getEditingColumn()==11 ||tblData.getEditingColumn()==12)){
            amt=CommonUtil.convertObjToDouble(tblData.getValueAt(tblData.getSelectedRow(), 4))+CommonUtil.convertObjToDouble(tblData.getValueAt(tblData.getSelectedRow(), 5))+
                    CommonUtil.convertObjToDouble(tblData.getValueAt(tblData.getSelectedRow(), 6))+CommonUtil.convertObjToDouble(tblData.getValueAt(tblData.getSelectedRow(), 7))
                    +CommonUtil.convertObjToDouble(tblData.getValueAt(tblData.getSelectedRow(), 3))+CommonUtil.convertObjToDouble(tblData.getValueAt(tblData.getSelectedRow(), 9))
                    +CommonUtil.convertObjToDouble(tblData.getValueAt(tblData.getSelectedRow(), 10))+CommonUtil.convertObjToDouble(tblData.getValueAt(tblData.getSelectedRow(), 11))
                    +CommonUtil.convertObjToDouble(tblData.getValueAt(tblData.getSelectedRow(), 12));
            tblData.setValueAt(amt, tblData.getSelectedRow(), 13);
        }
       // calTotalAmount();
    }//GEN-LAST:event_tblDataPropertyChange

    private void cboProdTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdTypeActionPerformed
        // TODO add your handling code here:
           String prodType = String.valueOf(cboProdType.getSelectedItem());
        if (prodType.length() > 0) {
            observable.fillDropDown(prodType);
            cboProdId.setModel(observable.getCbmProdId());
        }
        if (prodType.equals("MDS")) {
           // setVisibleFields(false);
            //    chkLoneeOnly.setText("Chittal Only");
         //   chkPrized.setVisible(true);
         //   chkNonPrized.setVisible(true);
        //    cPanel2.setVisible(false);
        //    cPanel3.setVisible(false);
       //     lblActHoldersList.setText("MDS Account Holders List");
            //    lblToDate2.setText("MDS Surities List");
        } else {
        //    setVisibleFields(true);
            //       chkLoneeOnly.setText("Loanee Only");
        //    cPanel2.setVisible(true);
         //   cPanel3.setVisible(true);
         //   lblActHoldersList.setText("Loan Account Holders List");
            //      lblToDate2.setText("Loan Surities List");
        }
    }//GEN-LAST:event_cboProdTypeActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        lblNoOfRecordsVal.setText("");
        isEdit=true;
        populateData();
    }//GEN-LAST:event_btnEditActionPerformed

private void btnPrintNoticeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintNoticeActionPerformed
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
        paramMap.put("AcctNum", param1);
        System.out.println("paramMap======= :"+paramMap);
        ttIntegration.setParam(paramMap);
        generateNotice = true;
        String reportName = "EPForm";
        if (yesNo == 0) {
            ttIntegration.integrationForPrint(reportName, true);
        }
    } else {
        generateNotice = false;
        ClientUtil.displayAlert("No Records found...");
    }
}//GEN-LAST:event_btnPrintNoticeActionPerformed

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
    private com.see.truetransact.uicomponent.CButton btnClear1;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnPrintNotice;
    private com.see.truetransact.uicomponent.CButton btnProcess;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CLabel cLabel3;
    private com.see.truetransact.uicomponent.CLabel cLabel6;
    private com.see.truetransact.uicomponent.CPanel cPanel1;
    private com.see.truetransact.uicomponent.CComboBox cboProdId;
    private com.see.truetransact.uicomponent.CComboBox cboProdType;
    private com.see.truetransact.uicomponent.CCheckBox chkSelectAll;
    private com.see.truetransact.uicomponent.CLabel lblFilingDt;
    private com.see.truetransact.uicomponent.CLabel lblNoOfRecords;
    private com.see.truetransact.uicomponent.CLabel lblNoOfRecordsVal;
    private com.see.truetransact.uicomponent.CLabel lblProdType1;
    private com.see.truetransact.uicomponent.CLabel lblProdid;
    private com.see.truetransact.uicomponent.CLabel lblSelectedRecords;
    private com.see.truetransact.uicomponent.CLabel lblToDate1;
    private com.see.truetransact.uicomponent.CLabel lblTotalEPFee;
    private com.see.truetransact.uicomponent.CPanel panArbit;
    private com.see.truetransact.uicomponent.CPanel panMultiSearch1;
    private com.see.truetransact.uicomponent.CPanel panPost;
    private com.see.truetransact.uicomponent.CPanel panSearch;
    private com.see.truetransact.uicomponent.CPanel panSearchCondition;
    private com.see.truetransact.uicomponent.CPanel panTable;
    private com.see.truetransact.uicomponent.CButtonGroup rdbArbit;
    private com.see.truetransact.uicomponent.CSeparator sptLine;
    private com.see.truetransact.uicomponent.CScrollPane srcTable;
    private com.see.truetransact.uicomponent.CTable tblData;
    private com.see.truetransact.uicomponent.CDateField tdtFilingDt;
    // End of variables declaration//GEN-END:variables
}
