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
package com.see.truetransact.ui.termloan.notices;

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
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.ui.common.viewall.AuthorizeListUI;
import com.see.truetransact.ui.common.viewall.NewAuthorizeListUI;
import java.util.Iterator;
import java.util.Map;

/**
 * @author balachandar
 */
public class LoanNoticeUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer, ListSelectionListener {
//    private final AuthorizeRB resourceBundle = new AuthorizeRB();

    private LoanNoticeOB observable;
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
    int CHITTAL_NO = 3;
    private final static Logger log = Logger.getLogger(LoanNoticeUI.class);
    int count = 0;
    int suritycount = 0;
    ArrayList countlist = new ArrayList();
    HashMap counthashMap = new HashMap();
    private final int AUTHORIZE= 4;//Variable used when btnAuthorize is clicked
    
    boolean fromAuthorizeUI = false;
    AuthorizeListUI authorizeListUI = null;
    boolean fromNewAuthorizeUI = false;
    NewAuthorizeListUI newauthorizeListUI = null;
    private int rejectFlag = 0;
    
    HashMap suretyCountHashMap = new HashMap();
    
    /**
     * Creates new form AuthorizeUI
     */
    public LoanNoticeUI() {
        setupInit();
        setupScreen();
        selectSuritiesOnly(false);
    }

    /**
     * Creates new form AuthorizeUI
     */
    public LoanNoticeUI(CInternalFrame parent, HashMap paramMap) {
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
        createCboNoticeType();
        btnChittalNo.setEnabled(true);
        txtNoOfInstallments.setValidation(new com.see.truetransact.uivalidation.NumericValidation());
        txtNoOfInstallments.setMaxLength(3);
        txtLastNoticeSentBefore.setValidation(new com.see.truetransact.uivalidation.NumericValidation());
        txtLastNoticeSentBefore.setMaxLength(3);
        bankName = ((String) TrueTransactMain.BANKINFO.get("BANK_NAME")).toUpperCase();
        txtNoticeCharge.setAllowAll(true);
        txtPostageCharge.setAllowAll(true);
        txtFromAccountno.setAllowAll(true);
        txtTOAccountno.setAllowAll(true);
      //  tblData.setAutoCreateRowSorter(true);
        btnsms.setVisible(false);
        batchId.setVisible(false);
        batchId1.setVisible(false);
        String autoAuthorizeAllowed = CommonUtil.convertObjToStr(TrueTransactMain.CBMSPARAMETERS.get("LOAN_NOTICE_AUTO_AUTHORIZE")); 
        System.out.println("autoAuthorizeAllowed : "+autoAuthorizeAllowed);
        if (bankName.lastIndexOf("MAHILA") != -1) {
            lblFromDate.setVisible(false);
            tdtFromDate.setVisible(false);
            lblToDate.setVisible(false);
            tdtToDate.setVisible(false);
            lblNoticeCharge.setVisible(false);
            txtNoticeCharge.setVisible(false);
            lblPostageCharge.setVisible(false);
            txtPostageCharge.setVisible(false);
            txtNoOfInstallments.setVisible(false);
            lblNoOfInstallments.setVisible(false);
            chkFulldue.setVisible(false);
//            btnPostCharges.setVisible(false);
            btnPostCharges.setText("Insert Charge Details");
        } else {
            lblFromDate.setVisible(true);
            tdtFromDate.setVisible(true);
            lblToDate.setVisible(true);
            tdtToDate.setVisible(true);
            lblNoticeCharge.setVisible(true);
            txtNoticeCharge.setVisible(true);
            lblPostageCharge.setVisible(true);
            txtPostageCharge.setVisible(true);
            txtNoOfInstallments.setVisible(true);
            lblNoOfInstallments.setVisible(true);
            chkFulldue.setVisible(true);
            chkPrized.setVisible(false);
            chkNonPrized.setVisible(false);
//            btnPostCharges.setVisible(true);
        }
        if(autoAuthorizeAllowed != null && autoAuthorizeAllowed.equals("N")){
            btnAuthorize.setVisible(true);
            btnReject.setVisible(true);
        }else{
            btnAuthorize.setVisible(false);
            btnReject.setVisible(false);
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
     /* tblData.setAutoscrolls(false);tblData.setAutoCreateRowSorter(false);
        tblData.setAutoResizeMode(0);
        tblData.setReorderingAllowed(false);
        tblData.setRowSorter(null);*/
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
            observable = new LoanNoticeOB();
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
        counthashMap=new HashMap();
        if (previousRow != -1) {
            if (((Boolean) tblData.getValueAt(previousRow, 0)).booleanValue()) {
                int guarantorRowIndexSelected = tblGuarantorData.getSelectedRow();
                if (accountNumberMap == null) {
                    accountNumberMap = new HashMap();
                }
                if (guarantorMemberMap == null) {
                    guarantorMemberMap = new HashMap();
                }
                if (previousRow != -1 && previousRow != rowIndexSelected) {
                    isSelectedRowTicked(tblGuarantorData);
                }
            } else {
                observable.setSelectAll(tblGuarantorData, new Boolean(false));
            }
        }
        //Changed By Suresh
        String prodType = String.valueOf(cboProdType.getSelectedItem());
        if (prodType.equals("MDS")) {
            observable.populateGuarantorTable(String.valueOf(tblData.getValueAt(rowIndexSelected, 2)), tblGuarantorData);
        } else {
            observable.populateGuarantorTable(String.valueOf(tblData.getValueAt(rowIndexSelected, 1)), tblGuarantorData);
        }
        previousRow = rowIndexSelected;
    }

    private void whenGuarantorTableRowSelected() {
        int rowIndexSelected = tblData.getSelectedRow();
        if (!((Boolean) tblData.getValueAt(rowIndexSelected, 0)).booleanValue()) {
            if (isSelectedRowTicked(tblGuarantorData)) {
                ClientUtil.displayAlert("Loanee Record not selected...");
                observable.setSelectAll(tblGuarantorData, new Boolean(false));
            }
        }
    }
    private void whenSuritiesTableRowSelected() {
        int totalsuritcount = 0;
        boolean selected = false;
        if (chkSuritiesOnly.isSelected()) {
            Iterator it = counthashMap.entrySet().iterator();

            List<Integer> valueList = new ArrayList<Integer>(counthashMap.values());
            for (Integer temp : valueList) {
                totalsuritcount += temp;
            }
        }
        if (totalsuritcount > 0) {
            selected = true;
            lblSelectedRecordVal.setText(CommonUtil.convertObjToStr(totalsuritcount));
            calculateTotalApplCharges();
        }
        if (!selected) {
            ClientUtil.displayAlert("Surities Record not selected...");
            observable.setSelectAll(tblGuarantorData, new Boolean(false));
            chkSuritiesOnly.setSelected(false);
        }
        selected = false;
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
        panMultiSearch1 = new com.see.truetransact.uicomponent.CPanel();
        lblNoticeCharge = new com.see.truetransact.uicomponent.CLabel();
        lblPostageCharge = new com.see.truetransact.uicomponent.CLabel();
        lblNoticeType = new com.see.truetransact.uicomponent.CLabel();
        cboNoticeType = new com.see.truetransact.uicomponent.CComboBox();
        txtNoticeCharge = new com.see.truetransact.uicomponent.CTextField();
        txtPostageCharge = new com.see.truetransact.uicomponent.CTextField();
        tdtOverDueDate = new com.see.truetransact.uicomponent.CDateField();
        lblOverDueDate = new com.see.truetransact.uicomponent.CLabel();
        cPanel1 = new com.see.truetransact.uicomponent.CPanel();
        lblLastNoticeSentBefore = new com.see.truetransact.uicomponent.CLabel();
        txtLastNoticeSentBefore = new com.see.truetransact.uicomponent.CTextField();
        lblLastNoticeSentBeforeShow = new com.see.truetransact.uicomponent.CLabel();
        tdtAuctionDate = new com.see.truetransact.uicomponent.CDateField();
        lblAuctionDate = new com.see.truetransact.uicomponent.CLabel();
        panMultiSearch2 = new com.see.truetransact.uicomponent.CPanel();
        lblNoOfInstallments = new com.see.truetransact.uicomponent.CLabel();
        txtNoOfInstallments = new com.see.truetransact.uicomponent.CTextField();
        chkLoneeOnly = new com.see.truetransact.uicomponent.CCheckBox();
        chkFulldue = new com.see.truetransact.uicomponent.CCheckBox();
        btnProcess = new com.see.truetransact.uicomponent.CButton();
        btnsms = new com.see.truetransact.uicomponent.CButton();
        lblChittalNo = new com.see.truetransact.uicomponent.CLabel();
        txtChittalNo = new com.see.truetransact.uicomponent.CTextField();
        btnChittalNo = new com.see.truetransact.uicomponent.CButton();
        chkAllAccounts = new com.see.truetransact.uicomponent.CCheckBox();
        panTable = new com.see.truetransact.uicomponent.CPanel();
        chkSelectAll = new com.see.truetransact.uicomponent.CCheckBox();
        srcTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblData = new com.see.truetransact.uicomponent.CTable();
        lblToDate1 = new com.see.truetransact.uicomponent.CLabel();
        lblNoOfRecords = new com.see.truetransact.uicomponent.CLabel();
        lblNoOfRecordsVal = new com.see.truetransact.uicomponent.CLabel();
        batchId1 = new com.see.truetransact.uicomponent.CLabel();
        batchId = new com.see.truetransact.uicomponent.CLabel();
        panSearch = new com.see.truetransact.uicomponent.CPanel();
        btnPostCharges = new com.see.truetransact.uicomponent.CButton();
        btnGenerateNotice = new com.see.truetransact.uicomponent.CButton();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        btnClear1 = new com.see.truetransact.uicomponent.CButton();
        chkSuritiesOnly = new com.see.truetransact.uicomponent.CCheckBox();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        sptLine = new com.see.truetransact.uicomponent.CSeparator();
        panGuarantor = new com.see.truetransact.uicomponent.CPanel();
        chkSelectAllGuarantor = new com.see.truetransact.uicomponent.CCheckBox();
        srcGuarantorTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblGuarantorData = new com.see.truetransact.uicomponent.CTable();
        lblToDate2 = new com.see.truetransact.uicomponent.CLabel();
        lblSelectedRecord = new com.see.truetransact.uicomponent.CLabel();
        lblSelectedRecordVal = new com.see.truetransact.uicomponent.CLabel();
        lblTotalChargeAmount = new com.see.truetransact.uicomponent.CLabel();
        lblTotalChargeAmountVal = new com.see.truetransact.uicomponent.CLabel();

        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setTitle("Loan Notices");
        setMinimumSize(new java.awt.Dimension(800, 630));
        setPreferredSize(new java.awt.Dimension(800, 630));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        panSearchCondition.setMinimumSize(new java.awt.Dimension(574, 150));
        panSearchCondition.setPreferredSize(new java.awt.Dimension(574, 150));
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
        cboProdId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdIdActionPerformed(evt);
            }
        });
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
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSearchCondition.add(panMultiSearch, gridBagConstraints);

        panMultiSearch1.setMinimumSize(new java.awt.Dimension(230, 145));
        panMultiSearch1.setPreferredSize(new java.awt.Dimension(230, 145));
        panMultiSearch1.setLayout(new java.awt.GridBagLayout());

        lblNoticeCharge.setText("Notice Charge");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMultiSearch1.add(lblNoticeCharge, gridBagConstraints);

        lblPostageCharge.setText("Postage Charge");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMultiSearch1.add(lblPostageCharge, gridBagConstraints);

        lblNoticeType.setText("Notice Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMultiSearch1.add(lblNoticeType, gridBagConstraints);

        cboNoticeType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboNoticeType.setPopupWidth(110);
        cboNoticeType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboNoticeTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMultiSearch1.add(cboNoticeType, gridBagConstraints);

        txtNoticeCharge.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMultiSearch1.add(txtNoticeCharge, gridBagConstraints);

        txtPostageCharge.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMultiSearch1.add(txtPostageCharge, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMultiSearch1.add(tdtOverDueDate, gridBagConstraints);

        lblOverDueDate.setText("OverDue Date");
        lblOverDueDate.setMaximumSize(new java.awt.Dimension(89, 18));
        lblOverDueDate.setMinimumSize(new java.awt.Dimension(89, 18));
        lblOverDueDate.setPreferredSize(new java.awt.Dimension(89, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMultiSearch1.add(lblOverDueDate, gridBagConstraints);

        cPanel1.setMinimumSize(new java.awt.Dimension(220, 35));
        cPanel1.setPreferredSize(new java.awt.Dimension(220, 35));
        cPanel1.setLayout(new java.awt.GridBagLayout());

        lblLastNoticeSentBefore.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblLastNoticeSentBefore.setText("Last Notice Sent Before");
        lblLastNoticeSentBefore.setMaximumSize(new java.awt.Dimension(143, 18));
        lblLastNoticeSentBefore.setMinimumSize(new java.awt.Dimension(143, 18));
        lblLastNoticeSentBefore.setPreferredSize(new java.awt.Dimension(143, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        cPanel1.add(lblLastNoticeSentBefore, gridBagConstraints);

        txtLastNoticeSentBefore.setMinimumSize(new java.awt.Dimension(25, 21));
        txtLastNoticeSentBefore.setPreferredSize(new java.awt.Dimension(25, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        cPanel1.add(txtLastNoticeSentBefore, gridBagConstraints);

        lblLastNoticeSentBeforeShow.setText("days");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        cPanel1.add(lblLastNoticeSentBeforeShow, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        panMultiSearch1.add(cPanel1, gridBagConstraints);

        tdtAuctionDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtAuctionDate.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMultiSearch1.add(tdtAuctionDate, gridBagConstraints);

        lblAuctionDate.setText("Auction Date");
        lblAuctionDate.setMaximumSize(new java.awt.Dimension(89, 18));
        lblAuctionDate.setMinimumSize(new java.awt.Dimension(89, 18));
        lblAuctionDate.setPreferredSize(new java.awt.Dimension(89, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMultiSearch1.add(lblAuctionDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSearchCondition.add(panMultiSearch1, gridBagConstraints);

        panMultiSearch2.setMinimumSize(new java.awt.Dimension(210, 145));
        panMultiSearch2.setPreferredSize(new java.awt.Dimension(210, 145));
        panMultiSearch2.setLayout(new java.awt.GridBagLayout());

        lblNoOfInstallments.setText("Installments");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 0, 0);
        panMultiSearch2.add(lblNoOfInstallments, gridBagConstraints);

        txtNoOfInstallments.setMinimumSize(new java.awt.Dimension(30, 21));
        txtNoOfInstallments.setPreferredSize(new java.awt.Dimension(30, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 7, 0, 0);
        panMultiSearch2.add(txtNoOfInstallments, gridBagConstraints);

        chkLoneeOnly.setText("Loanee Only");
        chkLoneeOnly.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkLoneeOnlyActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 6, 9, 0);
        panMultiSearch2.add(chkLoneeOnly, gridBagConstraints);

        chkFulldue.setText("Full Due");
        chkFulldue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkFulldueActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 0);
        panMultiSearch2.add(chkFulldue, gridBagConstraints);

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
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 3);
        panMultiSearch2.add(btnProcess, gridBagConstraints);

        btnsms.setBackground(new java.awt.Color(255, 102, 0));
        btnsms.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/send_sms.jpg"))); // NOI18N
        btnsms.setToolTipText("SMS");
        btnsms.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        btnsms.setMaximumSize(new java.awt.Dimension(38, 32));
        btnsms.setMinimumSize(new java.awt.Dimension(38, 32));
        btnsms.setPreferredSize(new java.awt.Dimension(38, 32));
        btnsms.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsmsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 55, 9, 3);
        panMultiSearch2.add(btnsms, gridBagConstraints);

        lblChittalNo.setText("Chittal No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(16, 6, 0, 0);
        panMultiSearch2.add(lblChittalNo, gridBagConstraints);

        txtChittalNo.setMinimumSize(new java.awt.Dimension(100, 18));
        txtChittalNo.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(16, 7, 0, 0);
        panMultiSearch2.add(txtChittalNo, gridBagConstraints);

        btnChittalNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnChittalNo.setEnabled(false);
        btnChittalNo.setMaximumSize(new java.awt.Dimension(21, 21));
        btnChittalNo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnChittalNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnChittalNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChittalNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(17, 2, 0, 3);
        panMultiSearch2.add(btnChittalNo, gridBagConstraints);

        chkAllAccounts.setText("All Accounts");
        chkAllAccounts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkAllAccountsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 0, 0);
        panMultiSearch2.add(chkAllAccounts, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSearchCondition.add(panMultiSearch2, gridBagConstraints);

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

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblData.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblData.setFocusable(false);
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
        srcTable.setViewportView(tblData);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 6;
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

        batchId1.setForeground(new java.awt.Color(51, 0, 255));
        batchId1.setText("BATCH ID : ");
        batchId1.setMaximumSize(new java.awt.Dimension(75, 18));
        batchId1.setMinimumSize(new java.awt.Dimension(75, 18));
        batchId1.setPreferredSize(new java.awt.Dimension(75, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panTable.add(batchId1, gridBagConstraints);

        batchId.setForeground(new java.awt.Color(51, 0, 255));
        batchId.setMaximumSize(new java.awt.Dimension(230, 85));
        batchId.setMinimumSize(new java.awt.Dimension(80, 18));
        batchId.setPreferredSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panTable.add(batchId, gridBagConstraints);

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
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        panSearch.add(btnPostCharges, gridBagConstraints);

        btnGenerateNotice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnGenerateNotice.setText("Generate Notice");
        btnGenerateNotice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerateNoticeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
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

        chkSuritiesOnly.setText("Surities Only");
        chkSuritiesOnly.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSuritiesOnlyActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panSearch.add(chkSuritiesOnly, gridBagConstraints);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        panSearch.add(btnAuthorize, gridBagConstraints);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        panSearch.add(btnReject, gridBagConstraints);

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

        panGuarantor.setMaximumSize(new java.awt.Dimension(600, 200));
        panGuarantor.setMinimumSize(new java.awt.Dimension(600, 200));
        panGuarantor.setPreferredSize(new java.awt.Dimension(600, 200));
        panGuarantor.setLayout(new java.awt.GridBagLayout());

        chkSelectAllGuarantor.setText("Select All");
        chkSelectAllGuarantor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSelectAllGuarantorActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panGuarantor.add(chkSelectAllGuarantor, gridBagConstraints);

        srcGuarantorTable.setViewport(srcTable.getRowHeader());

        tblGuarantorData.setModel(new javax.swing.table.DefaultTableModel(
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
        tblGuarantorData.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblGuarantorData.setFocusable(false);
        tblGuarantorData.setPreferredScrollableViewportSize(new java.awt.Dimension(450000, 400000));
        tblGuarantorData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblGuarantorDataMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblGuarantorDataMouseReleased(evt);
            }
        });
        srcGuarantorTable.setViewportView(tblGuarantorData);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panGuarantor.add(srcGuarantorTable, gridBagConstraints);

        lblToDate2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblToDate2.setText("Loan Surities List");
        lblToDate2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblToDate2.setMaximumSize(new java.awt.Dimension(230, 85));
        lblToDate2.setMinimumSize(new java.awt.Dimension(150, 18));
        lblToDate2.setPreferredSize(new java.awt.Dimension(150, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panGuarantor.add(lblToDate2, gridBagConstraints);

        lblSelectedRecord.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSelectedRecord.setText("Selected Record");
        lblSelectedRecord.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblSelectedRecord.setMaximumSize(new java.awt.Dimension(100, 18));
        lblSelectedRecord.setMinimumSize(new java.awt.Dimension(100, 18));
        lblSelectedRecord.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panGuarantor.add(lblSelectedRecord, gridBagConstraints);

        lblSelectedRecordVal.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblSelectedRecordVal.setMaximumSize(new java.awt.Dimension(230, 85));
        lblSelectedRecordVal.setMinimumSize(new java.awt.Dimension(80, 18));
        lblSelectedRecordVal.setPreferredSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panGuarantor.add(lblSelectedRecordVal, gridBagConstraints);

        lblTotalChargeAmount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalChargeAmount.setText("Total Charge Amount : Rs ");
        lblTotalChargeAmount.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblTotalChargeAmount.setMaximumSize(new java.awt.Dimension(155, 18));
        lblTotalChargeAmount.setMinimumSize(new java.awt.Dimension(155, 18));
        lblTotalChargeAmount.setPreferredSize(new java.awt.Dimension(155, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panGuarantor.add(lblTotalChargeAmount, gridBagConstraints);

        lblTotalChargeAmountVal.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblTotalChargeAmountVal.setMaximumSize(new java.awt.Dimension(230, 85));
        lblTotalChargeAmountVal.setMinimumSize(new java.awt.Dimension(80, 18));
        lblTotalChargeAmountVal.setPreferredSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panGuarantor.add(lblTotalChargeAmountVal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(panGuarantor, gridBagConstraints);

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

    private void btnsmsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsmsActionPerformed
        // TODO add your handling code here:
        //Added By Suresh
        HashMap smsMap = new HashMap();
        if (tblData.getRowCount() > 0) {
            for (int i = 0; i < tblData.getRowCount(); i++) {
                if (((Boolean) tblData.getValueAt(i, 0)).booleanValue()) {
                    HashMap rowMap = new HashMap();
                    String smsString = "";
                    String actNo = "";
                    String dueDate = "";
                    String schemeName = "";
                    double totalDue = 0.0;
                    if(CommonUtil.convertObjToStr(cboProdType.getSelectedItem()).equalsIgnoreCase("MDS")){
                            totalDue = CommonUtil.convertObjToDouble(tblData.getValueAt(i, 6)).doubleValue()
                            + CommonUtil.convertObjToDouble(tblData.getValueAt(i, 7)).doubleValue();
                            smsString = CommonUtil.convertObjToStr(smsMap.get("SMS"));
                            actNo = CommonUtil.convertObjToStr(tblData.getValueAt(i, 2));
                            //dueDate = CommonUtil.convertObjToStr(tblData.getValueAt(i, 10));
                            dueDate = DateUtil.getStringDate(currDt);
                            schemeName = CommonUtil.convertObjToStr(cboProdId.getSelectedItem());
                    }else{
                            totalDue = CommonUtil.convertObjToDouble(tblData.getValueAt(i, 9)).doubleValue()
                            + CommonUtil.convertObjToDouble(tblData.getValueAt(i, 11)).doubleValue()
                            + CommonUtil.convertObjToDouble(tblData.getValueAt(i, 12)).doubleValue()
                            + CommonUtil.convertObjToDouble(tblData.getValueAt(i, 13)).doubleValue()
                            + CommonUtil.convertObjToDouble(txtNoticeCharge.getText()).doubleValue()
                            + CommonUtil.convertObjToDouble(txtPostageCharge.getText()).doubleValue();
                    smsString = CommonUtil.convertObjToStr(smsMap.get("SMS"));
                    actNo = CommonUtil.convertObjToStr(tblData.getValueAt(i, 1));
                    dueDate = CommonUtil.convertObjToStr(tblData.getValueAt(i, 10));
                    schemeName = CommonUtil.convertObjToStr(cboProdId.getSelectedItem());
                    }
                    rowMap.put("ACT_NUM", actNo);
                    rowMap.put("DUE_DT", dueDate);
                    rowMap.put("TOTAL_DUE", String.valueOf(totalDue));
                    rowMap.put("SCHEME_NAME", schemeName);
                    smsMap.put(actNo, rowMap);
                }
            }
            if (smsMap.size() > 0) {
                HashMap smsDataMap = new HashMap();
                smsDataMap.put("SMS", smsMap);
                observable.printSMS(smsDataMap);
                btnsms.setEnabled(false);
            }
        }
        smsMap = null;
    }//GEN-LAST:event_btnsmsActionPerformed

    private void cboNoticeTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboNoticeTypeActionPerformed
        // TODO add your handling code here:
        String noticeType = CommonUtil.convertObjToStr(cboNoticeType.getSelectedItem());
        if (bankName.lastIndexOf("MAHILA") != -1) {
            setVisibleForDemandNotice(true);
        } else {
            if (noticeType.equals("Demand Notice")) {
                setVisibleForDemandNotice(false);
            } else {
                setVisibleForDemandNotice(true);
            }
        }
        if (noticeType.equals("Auction Notice")) {
            setVisibleForAuctionNotice(true);
        } else {
            setVisibleForAuctionNotice(false);
        }
    }//GEN-LAST:event_cboNoticeTypeActionPerformed

    private void setVisibleForAuctionNotice(boolean val) {
        lblAuctionDate.setVisible(val);
        tdtAuctionDate.setVisible(val);
    }

    private void setVisibleForDemandNotice(boolean val) {
        lblOverDueDate.setVisible(val);
        tdtOverDueDate.setVisible(val);
        chkFulldue.setVisible(val);
        lblNoOfInstallments.setVisible(val);
        txtNoOfInstallments.setVisible(val);
        if (val) {
            lblFromDate.setText("Sanction From Date");
            lblToDate.setText("Sanction To Date");
        } else {
            lblFromDate.setText("Demand From Date");
            lblToDate.setText("Demand To Date");
            tdtOverDueDate.setDateValue("");
            chkFulldue.setSelected(false);
            txtNoOfInstallments.setText("");
        }
    }

    private void btnClear1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClear1ActionPerformed
        // TODO add your handling code here:
        observable.removeRowsFromGuarantorTable(tblData);
        ClientUtil.clearAll(this);
    
        btnsms.setVisible(false);
        btnPostCharges.setEnabled(true);
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnClose.setEnabled(true);
        lblTotalChargeAmountVal.setText("");
        lblSelectedRecordVal.setText("");
        lblNoOfRecordsVal.setText("");
        selectSuritiesOnly(false);
        counthashMap=new HashMap();
        counthashMap=null;
        suritycount = 0;
        viewType = 0;
        batchId.setVisible(false);
        batchId1.setVisible(false);
        ClientUtil.enableDisable(panSearchCondition, true);
        if (fromNewAuthorizeUI) {
                this.dispose();
                fromNewAuthorizeUI = false;
                newauthorizeListUI.setFocusToTable();
            }
            if (fromAuthorizeUI) {
                this.dispose();
                fromAuthorizeUI = false;
                authorizeListUI.setFocusToTable();
            }
        suretyCountHashMap = null;
    }//GEN-LAST:event_btnClear1ActionPerformed

    private void chkNonPrizedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkNonPrizedActionPerformed
        // TODO add your handling code here:
        if (chkNonPrized.isSelected() == true) {
            chkPrized.setSelected(false);
        }
    }//GEN-LAST:event_chkNonPrizedActionPerformed

    private void chkPrizedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkPrizedActionPerformed
        // TODO add your handling code here:
        if (chkPrized.isSelected() == true) {
            chkNonPrized.setSelected(false);
        }
    }//GEN-LAST:event_chkPrizedActionPerformed

    private void chkFulldueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkFulldueActionPerformed
        // TODO add your handling code here:
        if (chkFulldue.isSelected()) {
            txtNoOfInstallments.setText("");
            txtNoOfInstallments.setEnabled(false);
            chkAllAccounts.setSelected(false); //KD-3320
            chkAllAccounts.setEnabled(false);
        } else {
            txtNoOfInstallments.setEnabled(true);
            chkAllAccounts.setEnabled(true);
        }
    }//GEN-LAST:event_chkFulldueActionPerformed

    private void tblGuarantorDataMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGuarantorDataMouseReleased
        // TODO add your handling code here:
        if (/*(evt.getClickCount() == 2) && */(evt.getModifiers() == 16)) {
            whenGuarantorTableRowSelected();
           setSelectedSuRecord();
        }
    }//GEN-LAST:event_tblGuarantorDataMouseReleased

    private void tblDataMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseReleased
        // TODO add your handling code here:
        if (/*(evt.getClickCount() == 2) && */(evt.getModifiers() == 16)) {
            whenTableRowSelected();
            setSelectedRecord();
           //added by rishad
            if(((Boolean) tblData.getValueAt(tblData.getSelectedRow(), 0)).booleanValue())
            {
             chkSelectAllGuarantor.setSelected(((Boolean) tblData.getValueAt(tblData.getSelectedRow(), 0)).booleanValue());
             chkSelectAllGuarantorActionPerformed();}
             else
            {
             chkSelectAllGuarantor.setSelected(((Boolean) tblData.getValueAt(tblData.getSelectedRow(), 0)).booleanValue());
             chkSelectAllGuarantorActionPerformed();
            }
        }
    }//GEN-LAST:event_tblDataMouseReleased

    private void chkSelectAllGuarantorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSelectAllGuarantorActionPerformed
        // TODO add your handling code here:
       chkSelectAllGuarantorActionPerformed();
    }//GEN-LAST:event_chkSelectAllGuarantorActionPerformed
    
    private void chkSelectAllGuarantorActionPerformed()
    {
         observable.setSelectAll(tblGuarantorData, new Boolean(chkSelectAllGuarantor.isSelected()));
         setSelectedSuRecord();
    }
    private void tblGuarantorDataMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGuarantorDataMousePressed
        // TODO add your handling code here:
//        if (/*(evt.getClickCount() == 2) && */(evt.getModifiers() == 16)) {
//            whenGuarantorTableRowSelected();
//        }
    }//GEN-LAST:event_tblGuarantorDataMousePressed
    public void fillData(Object param) {

        final HashMap hash = (HashMap) param;
        System.out.println("Hash: " + hash);
       if (hash.containsKey("FROM_AUTHORIZE_LIST_UI")) {
            fromAuthorizeUI = true;
            authorizeListUI = (AuthorizeListUI) hash.get("PARENT");
            hash.remove("PARENT");
            viewType = AUTHORIZE;
            observable.setOperation(ClientConstants.ACTIONTYPE_AUTHORIZE);
            btnReject.setEnabled(false);
            rejectFlag = 1;
        }
        if (hash.containsKey("NEW_FROM_AUTHORIZE_LIST_UI")) {
            fromNewAuthorizeUI = true;
            newauthorizeListUI = (NewAuthorizeListUI) hash.get("PARENT");
            hash.remove("PARENT");
            viewType = AUTHORIZE;
            observable.setOperation(ClientConstants.ACTIONTYPE_AUTHORIZE);
            btnReject.setEnabled(false);
            rejectFlag = 1;
        }
        if (viewType == FROMACTNO) {
            txtFromAccountno.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
        } else if (viewType == TOACTNO) {
            txtTOAccountno.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
        } else if (viewType == 3) {
            txtChittalNo.setText(CommonUtil.convertObjToStr(hash.get("CHITTAL_NO")));
        } else if (viewType == AUTHORIZE) {
            
            HashMap whereMap = new HashMap();
            HashMap viewMap = new HashMap();
            String batchID = (String) hash.get("BATCH_ID");
            whereMap.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
            whereMap.put("CHARGE_DATE", currDt.clone());
            whereMap.put("BATCH_ID", batchID);
            
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            viewMap.put(CommonConstants.MAP_NAME, "getAccAuthLoanNoticeList");
            observable.populateData(viewMap, tblData);
            batchId.setVisible(true);
            batchId1.setVisible(true);
            batchId.setText(batchID);
            ClientUtil.enableDisable(panSearchCondition, false);
            btnPostCharges.setEnabled(false);
            btnGenerateNotice.setEnabled(false);
            btnClose.setEnabled(false);
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
        lblOverDueDate.setVisible(flag);
        tdtOverDueDate.setVisible(flag);
        chkPrized.setVisible(false);
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
            chkLoneeOnly.setText("Chittal Only");
            chkPrized.setVisible(true);
            chkNonPrized.setVisible(true);
            cPanel2.setVisible(false);
            cPanel3.setVisible(false);
            lblToDate1.setText("MDS Account Holders List");
            lblToDate2.setText("MDS Surities List");
            chkAllAccounts.setVisible(false); // KD-3320
        } else {
            setVisibleFields(true);
            chkLoneeOnly.setText("Loanee Only");
            cPanel2.setVisible(true);
            cPanel3.setVisible(true);
            lblToDate1.setText("Loan Account Holders List");
            lblToDate2.setText("Loan Surities List");
            txtFromAccountno.setText("");
            txtTOAccountno.setText("");
            chkAllAccounts.setVisible(true); //KD-3320
            chkAllAccounts.setEnabled(true);
       }
    }//GEN-LAST:event_cboProdTypeActionPerformed

    private void btnProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessActionPerformed
        // TODO add your handling code here:
        lblTotalChargeAmountVal.setText("");
        lblSelectedRecordVal.setText("");
        lblNoOfRecordsVal.setText("");
        populateData();
        generateNotice = false;
        selectSuritiesOnly(true);
        
        suretyCountHashMap = new HashMap();       
    }//GEN-LAST:event_btnProcessActionPerformed

    private void popUp(int field) {
        final HashMap viewMap = new HashMap();
        viewType = field;
        HashMap hash = new HashMap();
        HashMap whereMap = new HashMap();
        if (viewType == 3) {
            viewMap.put(CommonConstants.MAP_NAME, "getChittalNoForLoanNotice");
            hash.put("PROD_ID", observable.getCbmProdId().getKeyForSelected());
        } else {
            String prodId = ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected().toString();
            whereMap.put("prodId", prodId);
            List lst = ClientUtil.executeQuery("TermLoan.getProdHead", whereMap);
            if (lst != null && lst.size() > 0) {
                whereMap = (HashMap) lst.get(0);
                String behavesLike = CommonUtil.convertObjToStr(whereMap.get("BEHAVES_LIKE"));
                if (behavesLike.equals("OD")) {
                    viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountListAD");
                    hash.put("SELECTED_BRANCH", com.see.truetransact.ui.TrueTransactMain.BRANCH_ID);
                } else {
                    viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountListTL");
                    hash.put("SELECTED_BRANCH", com.see.truetransact.ui.TrueTransactMain.BRANCH_ID);
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

            hash.put("PROD_ID", prodId);
            hash.put(CommonConstants.BRANCH_ID, com.see.truetransact.ui.TrueTransactMain.BRANCH_ID);
            //        if(viewType==TO){
            //            hash.put("ACCT_NO", txtFromAccount.getText());
        }//        }
        viewMap.put(CommonConstants.MAP_WHERE, hash);

        new ViewAll(this, viewMap).show();

    }
    public void selectSuritiesOnly(boolean flag){
        chkSuritiesOnly.setSelected(false);
        chkSuritiesOnly.setEnabled(flag);
    }
    private void chkLoneeOnlyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkLoneeOnlyActionPerformed
        // TODO add your handling code here:
//        panMultiSearch.setVisible(chkMultiSearch.isSelected());
//        lstSearch = new javax.swing.JList(arrLst.toArray());
//        lstSearch.setMaximumSize(new java.awt.Dimension(200, 40));
//        lstSearch.setMinimumSize(new java.awt.Dimension(200, 40));
//        lstSearch.setPreferredSize(new java.awt.Dimension(200, 40));
//        cScrollPane1.setViewportView(lstSearch);
//        arrLst = new ArrayList();
        selectSuritiesOnly(false);
    }//GEN-LAST:event_chkLoneeOnlyActionPerformed

    private void tblDataMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseMoved
     /*   Point p = evt.getPoint();
        String tip =
                String.valueOf(
                tblData.getModel().getValueAt(
                tblData.rowAtPoint(p),
                tblData.columnAtPoint(p)));
        tblData.setToolTipText(tip);*/
    }//GEN-LAST:event_tblDataMouseMoved

    private void tblDataMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMousePressed
        if ((evt.getClickCount() == 2) && (evt.getModifiers() == 16)) {
            HashMap whereMap = new HashMap();
            whereMap.put("ACT_NUM", tblData.getValueAt(tblData.getSelectedRow(), 1));

            TableDialogUI tableData = new TableDialogUI("getNoticeChargeDetails", whereMap);
            tableData.setTitle("Notice Sent Details for " + tblData.getValueAt(tblData.getSelectedRow(), 1));
            tableData.setPreferredSize(new Dimension(750, 450));
            tableData.show();
        }
    }//GEN-LAST:event_tblDataMousePressed

    private void btnPostChargesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPostChargesActionPerformed
        // Add your handling code here:
        String prodType = String.valueOf(cboProdType.getSelectedItem());
        observable.setStrSurityOnly("N");
        observable.setModule(getModule());	
        observable.setScreen(getScreen());
        if(chkSuritiesOnly.isSelected()){
            observable.setStrSurityOnly("Y");
        }
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

        HashMap tempMap = new HashMap();
        String param1 = observable.getSelected();
        if (param1.length() > 0) {
//        if (generateNotice) {
            if (btnPostCharges.getText().equals("Insert Charge Details")) {
                observable.insertCharges(accountChargeMap, true, CommonUtil.convertObjToStr(cboNoticeType.getSelectedItem()), tempMap);
            } else {
                if (cboNoticeType.getSelectedItem().equals("Auction Notice")) {
                    tempMap.put("AUCTION_DT", DateUtil.getDateMMDDYYYY(tdtAuctionDate.getDateValue()));
                } else {
                    tempMap.put("AUCTION_DT", null);
                }
                observable.insertCharges(accountChargeMap, false, CommonUtil.convertObjToStr(cboNoticeType.getSelectedItem()), tempMap);
            }
            ArrayList baranchId = getTBTId(observable.getProxyReturnMap());
            String tbtId = "";
            for(int i=0;i<baranchId.size();i++){
              tbtId +=" "+ baranchId.get(i) ;
            }
            ClientUtil.showMessageWindow(observable.getResult()+tbtId);
            //print purpose
            
             displayTransDetail(observable.getProxyReturnMap(),prodType);
            if (observable.getResult().equals(ClientConstants.RESULT_STATUS[4])) {
                observable.removeRowsFromGuarantorTable(tblData);
                observable.removeRowsFromGuarantorTable(tblGuarantorData);
            }
            btnPostCharges.setEnabled(false);

//        } else {
//            ClientUtil.showAlertWindow("Click 'Generate Notice' First...");
//        }
        } else {
            ClientUtil.showAlertWindow("Please Select Record then Apply Postage Charges");
        }
        if(evt != null){// Added by nithya on 01-02-2020 for KD-1377
           btnClear1ActionPerformed(null);
        }
    }//GEN-LAST:event_btnPostChargesActionPerformed
    private void displayTransDetail(HashMap proxyResultMap, String prodType) {
        try {
            HashMap proxyResultPostage = null;
            HashMap proxyResultNotice = null;
            if (proxyResultMap != null && proxyResultMap.containsKey("TRANS_POSTAGE")) {
                proxyResultPostage = (HashMap) proxyResultMap.get("TRANS_POSTAGE");
                getPostagePrint(proxyResultPostage, prodType);
            }
            if (proxyResultMap != null && proxyResultMap.containsKey("TRANS_NOTICE")) {
                proxyResultNotice = (HashMap) proxyResultMap.get("TRANS_NOTICE");
                getNoticePrint(proxyResultNotice, prodType);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private ArrayList getTBTId(HashMap proxyResultMap) {
        ArrayList batch_id = new ArrayList();
        try {
            HashMap proxyResultPostage = null;
            HashMap proxyResultNotice = null;

            if (proxyResultMap != null && proxyResultMap.containsKey("TRANS_POSTAGE")) {
                proxyResultPostage = (HashMap) proxyResultMap.get("TRANS_POSTAGE");
                Object keys[] = proxyResultPostage.keySet().toArray();

                for (int i = 0; i < keys.length; i++) {
                    if (proxyResultMap.get(keys[i]) instanceof String) {
                        continue;
                    }
                    List tempList = (List) proxyResultPostage.get(keys[i]);
                    if (CommonUtil.convertObjToStr(keys[i]).indexOf("CASH") != -1) {
                        for (int j = 0; j < tempList.size(); j++) {
                            HashMap transMap = (HashMap) tempList.get(j);
                            if (!batch_id.contains(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")))) {
                                batch_id.add(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                            }
                        }
                    } else if (CommonUtil.convertObjToStr(keys[i]).indexOf("TRANSFER") != -1) {
                        for (int j = 0; j < tempList.size(); j++) {
                            HashMap transMap = (HashMap) tempList.get(j);
                            if (!batch_id.contains(CommonUtil.convertObjToStr(transMap.get("BATCH_ID")))) {
                                batch_id.add(CommonUtil.convertObjToStr(transMap.get("BATCH_ID")));
                            }
                        }

                    }
                }
            }
            if (proxyResultMap != null && proxyResultMap.containsKey("TRANS_NOTICE")) {
                proxyResultNotice = (HashMap) proxyResultMap.get("TRANS_NOTICE");
                Object keys[] = proxyResultNotice.keySet().toArray();

                for (int i = 0; i < keys.length; i++) {
                    if (proxyResultMap.get(keys[i]) instanceof String) {
                        continue;
                    }
                    List tempList = (List) proxyResultNotice.get(keys[i]);
                    if (CommonUtil.convertObjToStr(keys[i]).indexOf("CASH") != -1) {
                        for (int j = 0; j < tempList.size(); j++) {
                            HashMap transMap = (HashMap) tempList.get(j);
                            if (!batch_id.contains(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")))) {
                                batch_id.add(CommonUtil.convertObjToStr(transMap.get("TRANS_ID")));
                            }
                        }
                    } else if (CommonUtil.convertObjToStr(keys[i]).indexOf("TRANSFER") != -1) {
                        for (int j = 0; j < tempList.size(); j++) {
                            HashMap transMap = (HashMap) tempList.get(j);
                            if (!batch_id.contains(CommonUtil.convertObjToStr(transMap.get("BATCH_ID")))) {
                                batch_id.add(CommonUtil.convertObjToStr(transMap.get("BATCH_ID")));
                            }
                        }

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return batch_id;
    }
    public void getNoticePrint(HashMap proxyResultMap, String prodType) {
        System.out.println("@#$@@$@@@$ proxyResultMap notice: " + proxyResultMap);
        String cashDisplayStr = "Cash Transaction Details...\n";
        String transferDisplayStr = "Transfer Transaction Details...\n";
        String displayStr = "";
        String transId = "";
        String transType = "";
        Object keys[] = proxyResultMap.keySet().toArray();
        int cashCount = 0;
        int transferCount = 0;
        List tempList = null;
        HashMap transMap = null;
        String actNum = "";
        ArrayList crList = new ArrayList();
        ArrayList drList = new ArrayList();

        for (int i = 0; i < keys.length; i++) {
            if (proxyResultMap.get(keys[i]) instanceof String) {
                continue;
            }
            tempList = (List) proxyResultMap.get(keys[i]);
            if (CommonUtil.convertObjToStr(keys[i]).indexOf("CASH") != -1) {
                for (int j = 0; j < tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j == 0) {
                        transId = (String) transMap.get("SINGLE_TRANS_ID");
                    }
                    cashDisplayStr += "Trans Id : " + transMap.get("TRANS_ID")
                            + "   Trans Type : " + transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if (actNum != null && !actNum.equals("")) {
                        cashDisplayStr += "   Account No : " + transMap.get("ACT_NUM")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    } else {
                        cashDisplayStr += "   Ac Hd Desc : " + transMap.get("AC_HD_ID")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    }
                }
//                cashDisplayStr += "Trans Id : " + transMap.get("TRANS_ID");
                cashCount++;
            } else if (CommonUtil.convertObjToStr(keys[i]).indexOf("TRANSFER") != -1) {
                for (int j = 0; j < tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j == 0) {
                        transId = (String) transMap.get("SINGLE_TRANS_ID");
                    }
//                    transferDisplayStr += "   Batch Id : " + transMap.get("BATCH_ID");//"Trans Id : " + transMap.get("TRANS_ID")
                    break;

                }
                transferDisplayStr += "   Batch Id : " + transMap.get("BATCH_ID");
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
            //ClientUtil.showMessageWindow("" + displayStr);
        }

        int yesNo = 0;
        String[] options = {"Yes", "No"};
        yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE, COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                null, options, options[0]);
        System.out.println("#$#$$ yesNo : " + yesNo);
        if (yesNo == 0) {
            TTIntegration ttIntgration = null;
            HashMap paramMap = new HashMap();
           // paramMap.put("TransId", transId);
            paramMap.put("TransDt", ClientUtil.getCurrentDateProperFormat());
           paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
            
            
             for (int i = 0; i < keys.length; i++) {
                 System.out.println("proxyResultMap ==="+proxyResultMap);
            if (proxyResultMap.get(keys[i]) instanceof String) {
                continue;
            }
            tempList = (List) proxyResultMap.get(keys[i]);
            if (CommonUtil.convertObjToStr(keys[i]).indexOf("TRANSFER") != -1) {
                for (int j = 0; j < tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j == 0) {
                        transId = (String) transMap.get("SINGLE_TRANS_ID");
                         paramMap.put("TransId",transId);
                    }
//                    transferDisplayStr += "   Batch Id : " + transMap.get("BATCH_ID");//"Trans Id : " + transMap.get("TRANS_ID")
                    break;
                }
             }
                
            }
                          
            System.out.println("paramMap==="+paramMap);
            ttIntgration.setParam(paramMap);
            String reportName = "";
//            transType = cboSupplier.getSelectedItem() + "";
            if (transferCount > 0) {
                if (prodType != null && prodType.equals("MDS")) {
                    reportName = "ReceiptPayment";
                } else {
                    reportName = "ReceiptPayment";
                }
            }
            ttIntgration.integrationForPrint(reportName, true);
        }
    }
    public void getPostagePrint(HashMap proxyResultMap, String prodType) {
        System.out.println("@#$@@$@@@$ proxyResultMap postage: " + proxyResultMap);
        String cashDisplayStr = "Cash Transaction Details...\n";
        String transferDisplayStr = "Transfer Transaction Details...\n";
        String displayStr = "";
        String transId = "";
        String transType = "";
        Object keys[] = proxyResultMap.keySet().toArray();
        int cashCount = 0;
        int transferCount = 0;
        List tempList = null;
        HashMap transMap = null;
        String actNum = "";
        ArrayList crList=new ArrayList();
        ArrayList drList=new ArrayList();
        
        for (int i = 0; i < keys.length; i++) {
            if (proxyResultMap.get(keys[i]) instanceof String) {
                continue;
            }
            tempList = (List) proxyResultMap.get(keys[i]);
            if (CommonUtil.convertObjToStr(keys[i]).indexOf("CASH") != -1) {
                for (int j = 0; j < tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j == 0) {
                        transId = (String) transMap.get("SINGLE_TRANS_ID");
                    }
                    cashDisplayStr += "Trans Id : " + transMap.get("TRANS_ID")
                            + "   Trans Type : " + transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if (actNum != null && !actNum.equals("")) {
                        cashDisplayStr += "   Account No : " + transMap.get("ACT_NUM")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    } else {
                        cashDisplayStr += "   Ac Hd Desc : " + transMap.get("AC_HD_ID")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    }
                }
//                cashDisplayStr += "Trans Id : " + transMap.get("TRANS_ID");
                cashCount++;
            } else if (CommonUtil.convertObjToStr(keys[i]).indexOf("TRANSFER") != -1) {
                for (int j = 0; j < tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j == 0) {
                        transId = (String) transMap.get("SINGLE_TRANS_ID");
                    }
//                    transferDisplayStr += "   Batch Id : " + transMap.get("BATCH_ID");//"Trans Id : " + transMap.get("TRANS_ID")
                    break;

                }
                transferDisplayStr += "   Batch Id : " + transMap.get("BATCH_ID");
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
          //  ClientUtil.showMessageWindow("" + displayStr);
        }

        int yesNo = 0;
        String[] options = {"Yes", "No"};
        yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE, COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                null, options, options[0]);
        System.out.println("#$#$$ yesNo : " + yesNo);
        if (yesNo == 0) {
            TTIntegration ttIntgration = null;
            HashMap paramMap = new HashMap();
           // paramMap.put("TransId", transId);
            paramMap.put("TransDt", ClientUtil.getCurrentDateProperFormat());
           paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
            
            
             for (int i = 0; i < keys.length; i++) {
                 System.out.println("proxyResultMap ==="+proxyResultMap);
            if (proxyResultMap.get(keys[i]) instanceof String) {
                continue;
            }
            tempList = (List) proxyResultMap.get(keys[i]);
            if (CommonUtil.convertObjToStr(keys[i]).indexOf("TRANSFER") != -1) {
                for (int j = 0; j < tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j == 0) {
                        transId = (String) transMap.get("SINGLE_TRANS_ID");
                         paramMap.put("TransId",transId);
                    }
//                    transferDisplayStr += "   Batch Id : " + transMap.get("BATCH_ID");//"Trans Id : " + transMap.get("TRANS_ID")
                    break;
                }
             }
                
            }
                          
            System.out.println("paramMap==="+paramMap);
            ttIntgration.setParam(paramMap);
            String reportName = "";
//            transType = cboSupplier.getSelectedItem() + "";
            if (transferCount > 0) {
              //  if (prodType != null && prodType.equals("MDS")) {
                    reportName = "ReceiptPayment";
              //  } else {
               //     reportName = "PostageLoan";
               // }
            }
            ttIntgration.integrationForPrint(reportName, true);
        }
     }
    private void chkSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSelectAllActionPerformed
        // Add your handling code here:
        observable.setSelectAll(tblData, new Boolean(chkSelectAll.isSelected()));
        setSelectedRecord();
    }//GEN-LAST:event_chkSelectAllActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        observable.removeRowsFromGuarantorTable(tblData);
        observable.removeRowsFromGuarantorTable(tblGuarantorData);
        dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    //Added By Suresh,modified by rishad  28/05/2015
    private void setSelectedRecord() {
        count = 0;
        if (tblData.getRowCount() > 0) {
            for (int i = 0, j = tblData.getRowCount(); i < j; i++) {
                if (((Boolean) tblData.getValueAt(i, 0)).booleanValue()) {
                    count += 1;
                } else {
                    if (counthashMap != null && counthashMap.containsKey(tblData.getValueAt(i, 1))) {
                        counthashMap.remove(tblData.getValueAt(i, 1));
                    }
                }
            }
        }
        int totalsuritcount = 0;
        if (counthashMap != null) {
            Iterator it = counthashMap.entrySet().iterator();

            List<Integer> valueList = new ArrayList<Integer>(counthashMap.values());            
            for (Integer temp : valueList) {
                totalsuritcount += temp;
            }
        }
        if (chkSuritiesOnly.isSelected()) {
            lblSelectedRecordVal.setText(String.valueOf(totalsuritcount));
        } else {
            lblSelectedRecordVal.setText(String.valueOf(count + totalsuritcount));
        }
        calculateTotalApplCharges();
    }
    private void setSelectedSuRecord() {
        suritycount = 0;
        if (tblGuarantorData.getRowCount() > 0) {
            for (int i = 0, j = tblGuarantorData.getRowCount(); i < j; i++) {
                if (((Boolean) tblGuarantorData.getValueAt(i, 0)).booleanValue()) {
                    suritycount += 1;
                }else{
                    suritycount -= 1;
                }
            }            
        }         
        //System.out.println("suritycount.. "+ suritycount);
        // Modified by nithya for KDSA 121 - 0010393: LOAN NOTICE PROCESS ISSUE
        if (tblGuarantorData.getRowCount() > 0) {
            if(suritycount < 0)
                suritycount = 0;
            counthashMap.put(tblGuarantorData.getValueAt(0, 1), suritycount);
        }else{
            if(suritycount < 0)
                suritycount = 0;
            counthashMap.put(tblData.getValueAt(tblData.getSelectedRow(), 1), suritycount);
        }    
        //System.out.println("counthashMap .. "+ counthashMap);
        Iterator it = counthashMap.entrySet().iterator();
        int totalsuritcount = 0;
        List<Integer> valueList = new ArrayList<Integer>(counthashMap.values());        
        for (Integer temp : valueList) {
            totalsuritcount += temp;
        }
        //System.out.println("count.. "+ count);
        //System.out.println("totalsuritcount..."+ totalsuritcount);
        lblSelectedRecordVal.setText(String.valueOf(count + totalsuritcount));        
        calculateTotalApplCharges();
        
        claculateSelectedCountAndAmount();
        
    }
    private void calculateTotalApplCharges() {
        double totalChargeAmount = (CommonUtil.convertObjToDouble(txtNoticeCharge.getText())
                + CommonUtil.convertObjToDouble(txtPostageCharge.getText()));
        lblTotalChargeAmountVal.setText(String.valueOf(totalChargeAmount * CommonUtil.convertObjToDouble(lblSelectedRecordVal.getText())));
    }
    
    
    
    // Added by nithya on 25 jun 2025 for KD-4162 : LOAN SURITY NOTICE AMOUNT NOT CORRECT
    private void claculateSelectedCountAndAmount() {
        int loaneeCount = 0;
        int securityCount = 0;
        if (tblData.getRowCount() > 0) {
            for (int i = 0; i < tblData.getRowCount(); i++) {
                if (((Boolean) tblData.getValueAt(i, 0)).booleanValue()) {
                    loaneeCount += 1;
                }
            }
        }
        System.out.println("loaneeCount :: claculateSelectedCountAndAmount :: " + loaneeCount);
        if (tblGuarantorData.getRowCount() > 0) {
            for (int j = 0; j < tblGuarantorData.getRowCount(); j++) {
                if (((Boolean) tblGuarantorData.getValueAt(j, 0)).booleanValue()) {
                    securityCount += 1;
                }
                suretyCountHashMap.put(tblGuarantorData.getValueAt(0, 1), securityCount);
            }
        }
        System.out.println("counthashMap :: claculateSelectedCountAndAmount ::  " + suretyCountHashMap);
        
        Iterator it = suretyCountHashMap.entrySet().iterator();
        int totalsuritcount = 0;
        List<Integer> valueList = new ArrayList<Integer>(suretyCountHashMap.values());        
        for (Integer temp : valueList) {
            totalsuritcount += temp;
        }
        lblSelectedRecordVal.setText(String.valueOf(loaneeCount + totalsuritcount));        
        calculateTotalApplCharges();
    }
    
    

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
            ClientUtil.displayAlert("Select Product Type...");
        }
        if (!CommonUtil.convertObjToStr(cboNoticeType.getSelectedItem()).equals("")) {
            isOK = true;
        } else {
            isOK = false;
            ClientUtil.displayAlert("Select Notice Type...");
        }

        if (bankName.lastIndexOf("MAHILA") == -1) {
            if (!CommonUtil.convertObjToStr(cboNoticeType.getSelectedItem()).equals("Demand Notice")) {
                if (chkFulldue.isSelected() || CommonUtil.convertObjToInt(txtNoOfInstallments.getText()) > 0) {
                    isOK = true;
                } else {
                    if(chkAllAccounts.isSelected()){ //Added by nithya for KD-3320
                       isOK = true; 
                    }else{
                    isOK = false;
                    String displayMsg = "";
                    if(!CommonUtil.convertObjToStr(cboProdType.getSelectedItem()).equals("MDS")){
                      displayMsg = "or All Accounts";   
                    }
                    ClientUtil.displayAlert("Select Full Due or Enter No. of Installments "+displayMsg +" ...");
                    }
                }
            } else {
                isOK = true;
            }
        }

        if (isOK) {
            String chittalNo = String.valueOf(txtChittalNo.getText());
            String prodType = String.valueOf(cboProdType.getSelectedItem());
            String prodId = ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected().toString();
            whereMap.put("prodId", prodId);
            List lst = ClientUtil.executeQuery("TermLoan.getProdHead", whereMap);
            if (lst != null && lst.size() > 0) {
                whereMap = (HashMap) lst.get(0);
                behavesLike = CommonUtil.convertObjToStr(whereMap.get("BEHAVES_LIKE"));
            }

            if (prodType.equals("MDS")) {
                if (chittalNo.length() > 0) {
                    viewMap.put(CommonConstants.MAP_NAME, "getAccountsForMDSLoanNoticeForParticularChittal");
                    if (CommonUtil.convertObjToInt(lblNoOfRecordsVal.getText()) == 0) {
                        ClientUtil.showMessageWindow("No data");
                    }
                } else {

                    viewMap.put(CommonConstants.MAP_NAME, "getAccountsForMDSLoanNotice");
                }
            } else if (behavesLike.equals("OD")) {
                viewMap.put(CommonConstants.MAP_NAME, "getAccountsForLoanNoticeAD");
            } else {
                viewMap.put(CommonConstants.MAP_NAME, "getAccountsForLoanNotice");
            }
            //                whereMap.put("ACT_NUM", observable.getTxtAccNo());
            if (prodType.length() > 0) {
                whereMap.put("PROD_TYPE", prodType);
            }
            if (String.valueOf(observable.getCbmProdId().getKeyForSelected()).length() > 0) {
                whereMap.put("PROD_ID", observable.getCbmProdId().getKeyForSelected());
            }
            if (tdtFromDate.getDateValue() != null && tdtFromDate.getDateValue().length() > 0) {
                if (CommonUtil.convertObjToStr(cboNoticeType.getSelectedItem()).equals("Demand Notice")) {
                    whereMap.put("FROM_DUE_DT", getProperDate(DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue())));
                } else {
                    whereMap.put("FROM_DT", getProperDate(DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue())));
                }
            }
            if (tdtToDate.getDateValue() != null && tdtToDate.getDateValue().length() > 0) {
                if (CommonUtil.convertObjToStr(cboNoticeType.getSelectedItem()).equals("Demand Notice")) {
                    whereMap.put("TO_DUE_DT", getProperDate(DateUtil.getDateMMDDYYYY(tdtToDate.getDateValue())));
                } else {
                    whereMap.put("TO_DT", getProperDate(DateUtil.getDateMMDDYYYY(tdtToDate.getDateValue())));
                }
            } else {
                if (CommonUtil.convertObjToStr(cboNoticeType.getSelectedItem()).equals("Demand Notice")) {
                    whereMap.put("TO_DUE_DT", getProperDate(currDt));
                } else {
                    whereMap.put("TO_DT", getProperDate(currDt));
                }
            }
            if (CommonUtil.convertObjToStr(txtFromAccountno.getText()).length() > 0) {
                whereMap.put("FROM_ACCT_NUM", txtFromAccountno.getText());
            }

            if (CommonUtil.convertObjToStr(txtTOAccountno.getText()).length() > 0) {
                whereMap.put("TO_ACCT_NUM", txtTOAccountno.getText());
            }
            if (CommonUtil.convertObjToStr(txtLastNoticeSentBefore.getText()).length() > 0) {
                Date lastNoticeDate = (Date) currDt.clone();
                lastNoticeDate = DateUtil.addDays(lastNoticeDate, -CommonUtil.convertObjToInt(txtLastNoticeSentBefore.getText()));
                whereMap.put("LAST_NOTICE_SENT_DT", lastNoticeDate);
            }
            if (chkFulldue.isSelected()) {
                whereMap.put("FULL_DUE", "FULL_DUE");
            }
            if (!chkLoneeOnly.isSelected()) {
                whereMap.put("GUARANTOR", "GUARANTOR");
            }
            if (CommonUtil.convertObjToInt(txtNoOfInstallments.getText()) > 0) {
                whereMap.put("NO_OF_INSTALLMENTS", CommonUtil.convertObjToInt(txtNoOfInstallments.getText()));
            }
            //Added By Suresh
            if (chkPrized.isSelected() == true) {
                whereMap.put("PRIZED", "PRIZED");
            }
            if (chkNonPrized.isSelected() == true) {
                whereMap.put("NON_PRIZED", "NON_PRIZED");
            }
            if (tdtOverDueDate.getDateValue() != null && tdtOverDueDate.getDateValue().length() > 0) {
                whereMap.put("OVER_DUE_DT", getProperDate(DateUtil.getDateMMDDYYYY(tdtOverDueDate.getDateValue())));
            }
            whereMap.put("NOTICE_TYPE", cboNoticeType.getSelectedItem());
            whereMap.put("TODAY_DT", getProperDate(currDt));
            whereMap.put("CURR_DATE", getProperDate(currDt));
            whereMap.put("CHITTAL_NO", txtChittalNo.getText());
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            try {
                log.info("populateData...");
                observable.removeRowsFromGuarantorTable(tblGuarantorData);
                ArrayList heading = observable.populateData(viewMap, tblData);
                txtNoticeCharge.setText(observable.getTxtNoticeCharge());
                txtPostageCharge.setText(observable.getTxtPostageCharge());
                lblNoOfRecordsVal.setText(String.valueOf(tblData.getRowCount()));
                if (tblData.getRowCount() > 0) {
                    btnsms.setVisible(true);
                    btnsms.setEnabled(true);
                } else {
                    btnsms.setVisible(false);
                }
                //added for surities case
                if(chkLoneeOnly.isSelected()){
                    chkSuritiesOnly.setSelected(false);
                    chkSuritiesOnly.setEnabled(false);
                }
                else{
                  // chkSuritiesOnly.setSelected(true);
                   chkSuritiesOnly.setEnabled(true); 
                }
                heading = null;
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
        System.out.println("chk sel mode------------------>"+chkSuritiesOnly.isSelected());
        HashMap loaneeMap = null;
        if(!chkSuritiesOnly.isSelected()){
        //        updateOBFields();
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        String param1 = observable.getSelected();
//        String param2 = observable.getSelectCharges();
        String prodType = String.valueOf(cboProdType.getSelectedItem());
        if (param1.length() > 0) {
            int yesNo = 0;
            String[] options = {"Yes", "No"};
            yesNo = COptionPane.showOptionDialog(null, "Generate Loanee Notice?", CommonConstants.WARNINGTITLE,
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
            loaneeMap = accountNumberMap;
            ttIntegration = null;
            HashMap paramMap = new HashMap();
            paramMap.put("BranchCode", ProxyParameters.BRANCH_ID);
            paramMap.put("ProductId", observable.getCbmProdId().getKeyForSelected());
            paramMap.put("CURR_DATE", currDt);
            paramMap.put("Param1", param1);
//                paramMap.put("param3", param2);
            if (!prodType.equals("MDS")) {
               paramMap.put("Param2", guarantorGetSelected());
            }
            if (CommonUtil.convertObjToStr(txtFromAccountno.getText()).length() > 0) {
                paramMap.put("FROM_ACCT_NUM", txtFromAccountno.getText());
            }

            if (CommonUtil.convertObjToStr(txtTOAccountno.getText()).length() > 0) {
                paramMap.put("TO_ACCT_NUM", txtTOAccountno.getText());
            }
            if (CommonUtil.convertObjToStr(txtLastNoticeSentBefore.getText()).length() > 0) {
                Date lastNoticeDate = (Date) currDt.clone();
                lastNoticeDate = DateUtil.addDays(lastNoticeDate, -CommonUtil.convertObjToInt(txtLastNoticeSentBefore.getText()));
                paramMap.put("LAST_NOTICE_SENT_DT", lastNoticeDate);
            }
            //Added By Suresh
            if (tdtOverDueDate.getDateValue() != null && tdtOverDueDate.getDateValue().length() > 0) {
                paramMap.put("OverDueDt", getProperDate(DateUtil.getDateMMDDYYYY(tdtOverDueDate.getDateValue())));
            } else {
                paramMap.put("OverDueDt", currDt);
            }
            String noticeType = CommonUtil.convertObjToStr(cboNoticeType.getSelectedItem());
            if (prodType.equals("GOLD_LOAN")) {
                if (noticeType.equals("Auction Notice")) {
                    paramMap.put("AuctionDt", getProperDate(DateUtil.getDateMMDDYYYY(tdtAuctionDate.getDateValue())));
                }
            }
            ttIntegration.setParam(paramMap);
            generateNotice = true;
            //  String noticeType = CommonUtil.convertObjToStr(cboNoticeType.getSelectedItem());
            if (bankName.lastIndexOf("MAHILA") != -1) {
                String reportName = "LoanNoticeFirst";
                if (noticeType.equals("First Notice")) {
                    reportName = "LoanNoticeFirst";
                } else if (noticeType.equals("Second Notice")) {
                    reportName = "LoanNoticeSecond";
                } else if (noticeType.equals("Third Notice")) {
                    reportName = "LoanNoticeThird";
                }
                if (yesNo == 0) {
                    ttIntegration.integrationForPrint(reportName, true);
                }
            } else {
                String reportName = "";

                if (prodType.equals("GOLD_LOAN")) {
                    if (noticeType.equals("Demand Notice")) {
                        reportName = "GoldLoanNoticeDemand";
                    } else if (noticeType.equals("Ordinary Notice")) {
                        reportName = "GoldLoanNoticeFirst";
                    } else if (noticeType.equals("Registered Notice")) {
                        reportName = "GoldLoanNoticeSecond";
                    } else if (noticeType.equals("Auction Notice")) {
                        reportName = "GoldLoanNoticeThird";
                    }
                } else {

                    if (prodType.equals("MDS")) {
                        reportName = "MDSNoticeFirst";
                    } else {
                        reportName = "LoanNoticeFirst";
                    }
                    if (noticeType.equals("Demand Notice")) {
                        if (prodType.equals("MDS")) {
                            reportName = "MDSNoticeDemand";
                        } else {
                            reportName = "LoanNoticeDemand";
                        }
                    } else if (noticeType.equals("Ordinary Notice")) {
                        if (prodType.equals("MDS")) {
                            reportName = "MDSNoticeFirst";
                        } else {
                            reportName = "LoanNoticeFirst";
                        }
                    } else if (noticeType.equals("Registered Notice")) {
                        if (prodType.equals("MDS")) {
                            reportName = "MDSNoticeSecond";
                        } else {
                            reportName = "LoanNoticeSecond";
                        }
                    } else if (noticeType.equals("Auction Notice")) {
                        if (prodType.equals("MDS")) {
                            reportName = "MDSNoticeThird";
                        } else {
                            reportName = "LoanNoticeThird";
                        }
                    }
                }
                if (yesNo == 0) {
                    ttIntegration.integrationForPrint(reportName, true);
                }
            }
//            }

        } else {
            generateNotice = false;
            ClientUtil.displayAlert("No Records found...");
        }
        viewMap = null;
        whereMap = null;
        }else{

            boolean selected = false;
            accountNumberMap = new HashMap();
            guarantorMemberMap = new HashMap();
            accountChargeMap = new HashMap();
            java.util.Map guarantorMap = observable.getGuarantorMap();
            ArrayList totalList = null;
            ArrayList rowList = null;
            String actNum;
            ArrayList tempList = null;
            String prodType = String.valueOf(cboProdType.getSelectedItem());
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
            loaneeMap = accountNumberMap;
        }
        boolean selected= false;
          for (int i = 0, j = tblGuarantorData.getRowCount(); i < j; i++) {
            selected = ((Boolean) tblGuarantorData.getValueAt(i, 0)).booleanValue();
            if (selected) {
                selected=true;
                break;
            }
        }
        System.out.println("selected-------->"+selected);  
        if(selected){  
             generateNoticeForAssurity(loaneeMap);
        }
        selected=false;
    }

    public void generateNoticeForAssurity(HashMap loaneeMap) {
        //        updateOBFields();
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        String param1 = observable.getSelected();
//        String param2 = observable.getSelectCharges();
        String prodType = String.valueOf(cboProdType.getSelectedItem());
        if (param1.length() > 0) {
            int yesNo = 0;
            String[] options = {"Yes", "No"};
            yesNo = COptionPane.showOptionDialog(null, "Generate Surity Notice?", CommonConstants.WARNINGTITLE,
                    COptionPane.YES_NO_OPTION, COptionPane.QUESTION_MESSAGE,
                    null, options, options[0]);
            System.out.println("#$#$$ yesNo : " + yesNo);
//            if (yesNo==0) { //GENERATE NOTICE APPLIED MAY BE ARE MAY NOT BE BUT POST CHARGE CAN APPLY
            boolean selected = true;
            accountNumberMap = new HashMap();
            guarantorMemberMap = new HashMap();
            accountChargeMap = new HashMap();
            java.util.Map guarantorMap = observable.getGuarantorMap();
            ArrayList totalList = null;
            ArrayList rowList = null;
            String actNum;
            ArrayList tempList = null;
            observable.getTableModel(tblGuarantorData);
            
            // Commenting the block
            /*
            for (int i = 0, j = tblGuarantorData.getRowCount(); i < j; i++) {
                selected = ((Boolean) tblGuarantorData.getValueAt(i, 0)).booleanValue();
                if (selected) {
                    tempList = new ArrayList();
                    tempList.add((ArrayList) observable.getTableModel(tblGuarantorData).get(i));
                    if (prodType.equals("MDS")) {
                        actNum = String.valueOf(tblGuarantorData.getValueAt(i, 2));
                    } else {
                        actNum = String.valueOf(tblGuarantorData.getValueAt(i, 1));
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
            */
            
            // Adding new block of code by nithya for solving surety notice print issue
            System.out.println("guarantorMap checking :: " + guarantorMap);       
            
            if (null != loaneeMap && loaneeMap.size() > 0) {
                Iterator iterate = loaneeMap.entrySet().iterator();
                tempList = new ArrayList();
                ArrayList list = new ArrayList();
                while (iterate.hasNext()) {
                    Map.Entry entry = (Map.Entry) iterate.next();
                    Object key1 = (Object) entry.getKey();
                    actNum = CommonUtil.convertObjToStr(key1);
                    if (guarantorMap != null && guarantorMap.size() > 0) {
                        totalList = (ArrayList) guarantorMap.get(actNum);
                        if (totalList != null && totalList.size() > 0) {
                            for (int g = 0; g < totalList.size(); g++) {
                                rowList = (ArrayList) totalList.get(g);
                                selected = ((Boolean) rowList.get(0)).booleanValue();
                                if (selected) {
                                    tempList.add(rowList);
                                    guarantorMemberMap.put(actNum + rowList.get(3), null);
                                    System.out.println("guarantorMemberMap in surety :: " + guarantorMemberMap);
                                }
                            }
                        }
                    }
                    accountChargeMap.put(actNum, tempList);
                }
            }
            // end
            

            ttIntegration = null;
            HashMap paramMap = new HashMap();
            paramMap.put("BranchCode", ProxyParameters.BRANCH_ID);
            paramMap.put("ProductId", observable.getCbmProdId().getKeyForSelected());
            paramMap.put("CURR_DATE", currDt);
            if (!prodType.equals("MDS")) {
               paramMap.put("Param1", param1);
            }
//                paramMap.put("param3", param2);
            paramMap.put("Param2", guarantorGetSelected());
            if (CommonUtil.convertObjToStr(txtFromAccountno.getText()).length() > 0) {
                paramMap.put("FROM_ACCT_NUM", txtFromAccountno.getText());
            }

            if (CommonUtil.convertObjToStr(txtTOAccountno.getText()).length() > 0) {
                paramMap.put("TO_ACCT_NUM", txtTOAccountno.getText());
            }
            if (CommonUtil.convertObjToStr(txtLastNoticeSentBefore.getText()).length() > 0) {
                Date lastNoticeDate = (Date) currDt.clone();
                lastNoticeDate = DateUtil.addDays(lastNoticeDate, -CommonUtil.convertObjToInt(txtLastNoticeSentBefore.getText()));
                paramMap.put("LAST_NOTICE_SENT_DT", lastNoticeDate);
            }
            //Added By Suresh
            if (tdtOverDueDate.getDateValue() != null && tdtOverDueDate.getDateValue().length() > 0) {
                paramMap.put("OverDueDt", getProperDate(DateUtil.getDateMMDDYYYY(tdtOverDueDate.getDateValue())));
            } else {
                paramMap.put("OverDueDt", currDt);
            }
            String noticeType = CommonUtil.convertObjToStr(cboNoticeType.getSelectedItem());
            if (prodType.equals("GOLD_LOAN")) {
                if (noticeType.equals("Auction Notice")) {
                    paramMap.put("AuctionDt", getProperDate(DateUtil.getDateMMDDYYYY(tdtAuctionDate.getDateValue())));
                }
            }
            
            // removing loanee - if only surety report needed
            
            if(paramMap.containsKey("Param1") && chkSuritiesOnly.isSelected()){
                paramMap.remove("Param1");
            }
            
            
            ttIntegration.setParam(paramMap);
            generateNotice = true;
            //  String noticeType = CommonUtil.convertObjToStr(cboNoticeType.getSelectedItem());
            if (bankName.lastIndexOf("MAHILA") != -1) {
                String reportName = "LoanNoticeFirst";
                if (noticeType.equals("First Notice")) {
                    reportName = "LoanNoticeFirst";
                } else if (noticeType.equals("Second Notice")) {
                    reportName = "LoanNoticeSecond";
                } else if (noticeType.equals("Third Notice")) {
                    reportName = "LoanNoticeThird";
                }
                if (yesNo == 0) {
                    ttIntegration.integrationForPrint(reportName, true);
                }
            } else {
                String reportName = "";

                if (prodType.equals("GOLD_LOAN")) {
                    if (noticeType.equals("Demand Notice")) {
                        reportName = "GoldLoanNoticeDemand";
                    } else if (noticeType.equals("Ordinary Notice")) {
                        reportName = "GoldLoanNoticeFirst";
                    } else if (noticeType.equals("Registered Notice")) {
                        reportName = "GoldLoanNoticeSecond";
                    } else if (noticeType.equals("Auction Notice")) {
                        reportName = "GoldLoanNoticeThird";
                    }
                } else {

                    if (prodType.equals("MDS")) {
                        reportName = "MDSNoticeFirst";
                    } else {
                        reportName = "LoanNoticeFirst";
                    }
                    if (noticeType.equals("Demand Notice")) {
                        if (prodType.equals("MDS")) {
                            reportName = "MDSNoticeDemand";
                        } else {
                            reportName = "LoanNoticeDemand";
                        }
                    } else if (noticeType.equals("Ordinary Notice")) {
                        if (prodType.equals("MDS")) {
                            reportName = "MDSNoticeFirst";
                        } else {
                            reportName = "LoanNoticeFirst";
                        }
                    } else if (noticeType.equals("Registered Notice")) {
                        if (prodType.equals("MDS")) {
                            reportName = "MDSNoticeSecond";
                        } else {
                            reportName = "LoanNoticeSecond";
                        }
                    } else if (noticeType.equals("Auction Notice")) {
                        if (prodType.equals("MDS")) {
                            reportName = "MDSNoticeThird";
                        } else {
                            reportName = "LoanNoticeThird";
                        }
                    }
                }
                if (yesNo == 0) {
                    //ttIntegration.integrationForPrint(reportName, false);
                    ttIntegration.integrationForPrint(reportName, true); // Added by nithya on 23-12-2020 for KD-2549
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
//            cboProdType.addItem("Gold Loans");
//            cboProdType.addItem("Other Loans");
//            cboProdType.addItem("MDS Loans");
//            cboProdType.addItem("MDS");
        }
    }

    private void createCboNoticeType() {
        if (((String) TrueTransactMain.BANKINFO.get("BANK_NAME")).toUpperCase().lastIndexOf("MAHILA") != -1) {
            cboNoticeType.addItem("");
            cboNoticeType.addItem("First Notice");
            cboNoticeType.addItem("Second Notice");
            cboNoticeType.addItem("Third Notice");
        } else {
            cboNoticeType.addItem("");
            cboNoticeType.addItem("Demand Notice");
            cboNoticeType.addItem("Ordinary Notice");
            cboNoticeType.addItem("Registered Notice");
            cboNoticeType.addItem("Auction Notice");
        }
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
            observable.removeRowsFromGuarantorTable(tblGuarantorData);
        }

    }

    private void btnGenerateNoticeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerateNoticeActionPerformed
        // Add your handling code here: 

       
       System.out.println("ff==="+btnPostCharges.isEnabled());
        observable.setStrSurityOnly("N");
        if(chkSuritiesOnly.isSelected()){
            observable.setStrSurityOnly("Y");
        }
        if (btnPostCharges.isEnabled()) {
            int confirm = ClientUtil.confirmationAlert("Postage Charges Not Yet Applied" + "\n" + "Do you want to Apply");
            if (confirm == 0) {
                btnPostChargesActionPerformed(null);
            }
        }
        generateNotice();
        counthashMap=new HashMap();
        counthashMap=null;
    }//GEN-LAST:event_btnGenerateNoticeActionPerformed

private void btnChittalNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChittalNoActionPerformed
    // TODO add your handling code here:
    popUp(CHITTAL_NO);
//       HashMap where11 = new HashMap();
//       where11.put("PROD_ID", observable.getCbmProdId().getKeyForSelected());
//            List lst2 = ClientUtil.executeQuery("getChittalNoForLoanNotice", where11);
//            System.out.println("lkhsdukh" + lst2);
//            HashMap Map1 = new HashMap();
//            Map1 = (HashMap) lst2.get(0);

}//GEN-LAST:event_btnChittalNoActionPerformed

private void chkSuritiesOnlyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSuritiesOnlyActionPerformed
    // TODO add your handling code here:
    whenSuritiesTableRowSelected();
}//GEN-LAST:event_chkSuritiesOnlyActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        
        //observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        updateAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnClear1.setEnabled(true);
        btnReject.setEnabled(false);
//        btnException.setEnabled(false);
    }//GEN-LAST:event_btnAuthorizeActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        updateAuthorizeStatus(CommonConstants.STATUS_REJECTED);
        btnClear1.setEnabled(true);
        btnAuthorize.setEnabled(false);
    }//GEN-LAST:event_btnRejectActionPerformed

    private void cboProdIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdIdActionPerformed
        // TODO add your handling code here:
        txtFromAccountno.setText("");
        txtTOAccountno.setText("");
    }//GEN-LAST:event_cboProdIdActionPerformed

    private void chkAllAccountsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkAllAccountsActionPerformed
        // TODO add your handling code here:
        //KD-3320
        if (chkAllAccounts.isSelected()) {
            txtNoOfInstallments.setText("");
            txtNoOfInstallments.setEnabled(false);
            chkFulldue.setSelected(false);
            chkFulldue.setEnabled(false);
        } else {
            txtNoOfInstallments.setEnabled(true);
            chkFulldue.setEnabled(true);
        }
    }//GEN-LAST:event_chkAllAccountsActionPerformed

    private void updateAuthorizeStatus(String authorizeStatus) {
        if (viewType != AUTHORIZE){
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put("USER_ID", TrueTransactMain.USER_ID);
            whereMap.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
            whereMap.put("CHARGE_DATE", currDt.clone());
            whereMap.put("AUTHORIZE_MODE","AUTHORIZE_MODE");
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            mapParam.put(CommonConstants.MAP_NAME, "getAuthorizeLoanNoticeList");
            viewType = AUTHORIZE;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            //            isFilled = false;
            authorizeUI.show();
            //btnSave.setEnabled(false);
            //observable.setStatus();
            //lblStatus.setText(observable.getLblStatus());
            
        } else if (viewType == AUTHORIZE){ 
            ArrayList arrList = new ArrayList();
            HashMap authorizeMap = new HashMap();
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("AUTHORIZE_STATUS", authorizeStatus);
            singleAuthorizeMap.put("AUTHORIZE_BY", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("AUTHORIZE_DATE",ClientUtil.getCurrentDateWithTime());
            singleAuthorizeMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            String batchIdd = batchId.getText();
            singleAuthorizeMap.put("BATCH_ID", batchIdd);
            arrList.add(singleAuthorizeMap);
            singleAuthorizeMap.put("USER_ID",TrueTransactMain.USER_ID);
         
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorize(authorizeMap);
            btnClear1ActionPerformed(null);
            
        }
    }
    
    public void authorize(HashMap map) {
        System.out.println("Authorize Map : " + map);
        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
            String prodType = String.valueOf(cboProdType.getSelectedItem());
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

            HashMap tempMap = new HashMap();
            String param1 = observable.getSelected();
                if (param1.length() > 0) {
                   map.put("ACT_NUM", tblData.getValueAt(tblData.getSelectedRow(), 1));
                   observable.insertAuthCharge(accountChargeMap, map);
                }else {
                ClientUtil.showAlertWindow("Please Select Record then Apply");
            }           
        }
        if (fromNewAuthorizeUI) {
            newauthorizeListUI.removeSelectedRow();
            this.dispose();
            newauthorizeListUI.setFocusToTable();
            newauthorizeListUI.displayDetails("Loan Notice");
        }
        if (fromAuthorizeUI) {
            authorizeListUI.removeSelectedRow();
            this.dispose();
            authorizeListUI.setFocusToTable();
            authorizeListUI.displayDetails("Loan Notice");
        }
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
    private com.see.truetransact.uicomponent.CLabel batchId;
    private com.see.truetransact.uicomponent.CLabel batchId1;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnChittalNo;
    private com.see.truetransact.uicomponent.CButton btnClear1;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnFromAccountno;
    private com.see.truetransact.uicomponent.CButton btnGenerateNotice;
    private com.see.truetransact.uicomponent.CButton btnPostCharges;
    private com.see.truetransact.uicomponent.CButton btnProcess;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnTOAccountno;
    private com.see.truetransact.uicomponent.CButton btnsms;
    private com.see.truetransact.uicomponent.CPanel cPanel1;
    private com.see.truetransact.uicomponent.CPanel cPanel2;
    private com.see.truetransact.uicomponent.CPanel cPanel3;
    private com.see.truetransact.uicomponent.CComboBox cboNoticeType;
    private com.see.truetransact.uicomponent.CComboBox cboProdId;
    private com.see.truetransact.uicomponent.CComboBox cboProdType;
    private com.see.truetransact.uicomponent.CCheckBox chkAllAccounts;
    private com.see.truetransact.uicomponent.CCheckBox chkFulldue;
    private com.see.truetransact.uicomponent.CCheckBox chkLoneeOnly;
    private com.see.truetransact.uicomponent.CCheckBox chkNonPrized;
    private com.see.truetransact.uicomponent.CCheckBox chkPrized;
    private com.see.truetransact.uicomponent.CCheckBox chkSelectAll;
    private com.see.truetransact.uicomponent.CCheckBox chkSelectAllGuarantor;
    private com.see.truetransact.uicomponent.CCheckBox chkSuritiesOnly;
    private com.see.truetransact.uicomponent.CLabel lblAuctionDate;
    private com.see.truetransact.uicomponent.CLabel lblChittalNo;
    private com.see.truetransact.uicomponent.CLabel lblFromAccountno;
    private com.see.truetransact.uicomponent.CLabel lblFromDate;
    private com.see.truetransact.uicomponent.CLabel lblLastNoticeSentBefore;
    private com.see.truetransact.uicomponent.CLabel lblLastNoticeSentBeforeShow;
    private com.see.truetransact.uicomponent.CLabel lblNoOfInstallments;
    private com.see.truetransact.uicomponent.CLabel lblNoOfRecords;
    private com.see.truetransact.uicomponent.CLabel lblNoOfRecordsVal;
    private com.see.truetransact.uicomponent.CLabel lblNoticeCharge;
    private com.see.truetransact.uicomponent.CLabel lblNoticeType;
    private com.see.truetransact.uicomponent.CLabel lblOverDueDate;
    private com.see.truetransact.uicomponent.CLabel lblPostageCharge;
    private com.see.truetransact.uicomponent.CLabel lblProdId;
    private com.see.truetransact.uicomponent.CLabel lblProdType;
    private com.see.truetransact.uicomponent.CLabel lblSelectedRecord;
    private com.see.truetransact.uicomponent.CLabel lblSelectedRecordVal;
    private com.see.truetransact.uicomponent.CLabel lblTOAccountno;
    private com.see.truetransact.uicomponent.CLabel lblToDate;
    private com.see.truetransact.uicomponent.CLabel lblToDate1;
    private com.see.truetransact.uicomponent.CLabel lblToDate2;
    private com.see.truetransact.uicomponent.CLabel lblTotalChargeAmount;
    private com.see.truetransact.uicomponent.CLabel lblTotalChargeAmountVal;
    private com.see.truetransact.uicomponent.CPanel panGuarantor;
    private com.see.truetransact.uicomponent.CPanel panMultiSearch;
    private com.see.truetransact.uicomponent.CPanel panMultiSearch1;
    private com.see.truetransact.uicomponent.CPanel panMultiSearch2;
    private com.see.truetransact.uicomponent.CPanel panSearch;
    private com.see.truetransact.uicomponent.CPanel panSearchCondition;
    private com.see.truetransact.uicomponent.CPanel panTable;
    private com.see.truetransact.uicomponent.CSeparator sptLine;
    private com.see.truetransact.uicomponent.CScrollPane srcGuarantorTable;
    private com.see.truetransact.uicomponent.CScrollPane srcTable;
    private com.see.truetransact.uicomponent.CTable tblData;
    private com.see.truetransact.uicomponent.CTable tblGuarantorData;
    private com.see.truetransact.uicomponent.CDateField tdtAuctionDate;
    private com.see.truetransact.uicomponent.CDateField tdtFromDate;
    private com.see.truetransact.uicomponent.CDateField tdtOverDueDate;
    private com.see.truetransact.uicomponent.CDateField tdtToDate;
    private com.see.truetransact.uicomponent.CTextField txtChittalNo;
    private com.see.truetransact.uicomponent.CTextField txtFromAccountno;
    private com.see.truetransact.uicomponent.CTextField txtLastNoticeSentBefore;
    private com.see.truetransact.uicomponent.CTextField txtNoOfInstallments;
    private com.see.truetransact.uicomponent.CTextField txtNoticeCharge;
    private com.see.truetransact.uicomponent.CTextField txtPostageCharge;
    private com.see.truetransact.uicomponent.CTextField txtTOAccountno;
    // End of variables declaration//GEN-END:variables
}
