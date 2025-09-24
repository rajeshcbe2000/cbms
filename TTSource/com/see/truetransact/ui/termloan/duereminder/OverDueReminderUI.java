/*
 * Copyright 2019 Fincuro Solutions (P) Ltd. All rights reserved.
 *
 * This software is the proprietary information of Fincuro Solutions (P) Ltd..  
 * 
 *
 * OverDueReminderUI.java
 *
 * Created on JAN 311, 2019, 10:16 AM
 */
package com.see.truetransact.ui.termloan.duereminder;

import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.EnhancedComboBoxModel;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.viewall.TableDialogUI;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uicomponent.COptionPane;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.*;
import javax.swing.JDialog;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionListener;
import org.apache.log4j.Logger;

/**
 * @author Rishad M.P
 */
public class OverDueReminderUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer, ListSelectionListener {

    private OverDueReminderOB observable;
    HashMap paramMap = null;
    CInternalFrame parent = null;
    javax.swing.JList lstSearch;
    java.util.ArrayList arrLst = new java.util.ArrayList();
    Date currDt = null;
    TTIntegration ttIntegration = null;
    int previousRow = -1;
    String bankName = "";
    int viewType = 0;
    int FROMACTNO = 1, TOACTNO = 2;
    int CHITTAL_NO = 3;
    private final static Logger log = Logger.getLogger(OverDueReminderUI.class);
    int count = 0;
    int suritycount = 0;
    ArrayList countlist = new ArrayList();
    HashMap counthashMap = new HashMap();

    /**
     * Creates new form OverDueReminderUI
     */
    public OverDueReminderUI() {
        setupInit();
        setupScreen();
//        selectSuritiesOnly(false);

    }

    /**
     * Creates new form OverDueReminderUI
     */
    public OverDueReminderUI(CInternalFrame parent, HashMap paramMap) {
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
        txtODInstallment.setValidation(new com.see.truetransact.uivalidation.NumericValidation());
        txtODInstallment.setMaxLength(3);
        txtInstFreq.setValidation(new com.see.truetransact.uivalidation.NumericValidation());
        txtInstFreq.setMaxLength(1);
        txtODFromPrinciple.setValidation(new com.see.truetransact.uivalidation.NumericValidation());
        txtODToPrinciple.setValidation(new com.see.truetransact.uivalidation.NumericValidation());
        txtODFromInterest.setValidation(new com.see.truetransact.uivalidation.NumericValidation());
        txtODToInterest.setValidation(new com.see.truetransact.uivalidation.NumericValidation());
        txtFromAccountno.setAllowAll(true);
        txtTOAccountno.setAllowAll(true);
        bankName = ((String) TrueTransactMain.BANKINFO.get("BANK_NAME")).toUpperCase();
        btnsms.setVisible(false);
//        if (bankName.lastIndexOf("MAHILA") != -1) {
//            lblFromDate.setVisible(false);
//            tdtFromDate.setVisible(false);
//            lblToDate.setVisible(false);
//            tdtToDate.setVisible(false);
//        } else {
//            lblFromDate.setVisible(true);
//            tdtFromDate.setVisible(true);
//            lblToDate.setVisible(true);
//            tdtToDate.setVisible(true);
//        }
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
            observable = new OverDueReminderOB();
            observable.addObserver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Bring up and populate the temporary project detail screen.
     */
    private void whenTableRowSelected() {
    }

    private void whenGuarantorTableRowSelected() {
    }

    private void whenSuritiesTableRowSelected() {
        int totalsuritcount = 0;
        boolean selected = false;

        if (totalsuritcount > 0) {
            selected = true;
            lblSelectedRecordVal.setText(CommonUtil.convertObjToStr(totalsuritcount));
            calculateTotalApplCharges();
        }
        selected = false;
    }

    private boolean isSelectedRowTicked(com.see.truetransact.uicomponent.CTable table) {
        boolean selected = false;
        for (int i = 0, j = table.getRowCount(); i < j; i++) {
            selected = ((Boolean) table.getValueAt(i, 0)).booleanValue();
            if (selected) {
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
        lblTOAccountno = new com.see.truetransact.uicomponent.CLabel();
        lblFromAccountno = new com.see.truetransact.uicomponent.CLabel();
        cPanel2 = new com.see.truetransact.uicomponent.CPanel();
        txtFromAccountno = new com.see.truetransact.uicomponent.CTextField();
        btnFromAccountno = new com.see.truetransact.uicomponent.CButton();
        cPanel3 = new com.see.truetransact.uicomponent.CPanel();
        txtTOAccountno = new com.see.truetransact.uicomponent.CTextField();
        btnTOAccountno = new com.see.truetransact.uicomponent.CButton();
        panLoanMultiSearch = new com.see.truetransact.uicomponent.CPanel();
        lblInstOD = new com.see.truetransact.uicomponent.CLabel();
        txtODInstallment = new com.see.truetransact.uicomponent.CTextField();
        txtODFromInterest = new com.see.truetransact.uicomponent.CTextField();
        lblODFromInt = new com.see.truetransact.uicomponent.CLabel();
        lblODFromPrinciple = new com.see.truetransact.uicomponent.CLabel();
        txtODFromPrinciple = new com.see.truetransact.uicomponent.CTextField();
        lblODToPrinciple = new com.see.truetransact.uicomponent.CLabel();
        txtODToPrinciple = new com.see.truetransact.uicomponent.CTextField();
        lblODToInt = new com.see.truetransact.uicomponent.CLabel();
        txtODToInterest = new com.see.truetransact.uicomponent.CTextField();
        panMultiSearch2 = new com.see.truetransact.uicomponent.CPanel();
        btnProcess = new com.see.truetransact.uicomponent.CButton();
        chkLoanDue = new com.see.truetransact.uicomponent.CCheckBox();
        txtInstFreq = new com.see.truetransact.uicomponent.CTextField();
        lblInstFreq = new com.see.truetransact.uicomponent.CLabel();
        panTable = new com.see.truetransact.uicomponent.CPanel();
        chkSelectAll = new com.see.truetransact.uicomponent.CCheckBox();
        srcTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblData = new com.see.truetransact.uicomponent.CTable();
        lblNoOfRecords = new com.see.truetransact.uicomponent.CLabel();
        lblNoOfRecordsVal = new com.see.truetransact.uicomponent.CLabel();
        lblSelectedRecordVal = new com.see.truetransact.uicomponent.CLabel();
        lblSelectedRecord = new com.see.truetransact.uicomponent.CLabel();
        panSearch = new com.see.truetransact.uicomponent.CPanel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        btnClear1 = new com.see.truetransact.uicomponent.CButton();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        sptLine = new com.see.truetransact.uicomponent.CSeparator();
        panGuarantor = new com.see.truetransact.uicomponent.CPanel();
        lblFromSmsParticulars = new com.see.truetransact.uicomponent.CLabel();
        lblToSmsParticulars = new com.see.truetransact.uicomponent.CLabel();
        btnsms = new com.see.truetransact.uicomponent.CButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtFromSmsDescription = new com.see.truetransact.uicomponent.CTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtTOSmsDescription = new com.see.truetransact.uicomponent.CTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        lstItem = new com.see.truetransact.uicomponent.CList();

        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setTitle("OverDueReminder");
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

        panLoanMultiSearch.setMinimumSize(new java.awt.Dimension(230, 145));
        panLoanMultiSearch.setPreferredSize(new java.awt.Dimension(230, 145));
        panLoanMultiSearch.setLayout(new java.awt.GridBagLayout());

        lblInstOD.setText("Installment OD");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panLoanMultiSearch.add(lblInstOD, gridBagConstraints);

        txtODInstallment.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panLoanMultiSearch.add(txtODInstallment, gridBagConstraints);

        txtODFromInterest.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panLoanMultiSearch.add(txtODFromInterest, gridBagConstraints);

        lblODFromInt.setDisplayedMnemonic('O');
        lblODFromInt.setText("OD From Interest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panLoanMultiSearch.add(lblODFromInt, gridBagConstraints);

        lblODFromPrinciple.setDisplayedMnemonic('O');
        lblODFromPrinciple.setText("OD From  Principle");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panLoanMultiSearch.add(lblODFromPrinciple, gridBagConstraints);

        txtODFromPrinciple.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panLoanMultiSearch.add(txtODFromPrinciple, gridBagConstraints);

        lblODToPrinciple.setDisplayedMnemonic('O');
        lblODToPrinciple.setText("OD To  Principle");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panLoanMultiSearch.add(lblODToPrinciple, gridBagConstraints);

        txtODToPrinciple.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panLoanMultiSearch.add(txtODToPrinciple, gridBagConstraints);

        lblODToInt.setDisplayedMnemonic('O');
        lblODToInt.setText("OD To Interest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panLoanMultiSearch.add(lblODToInt, gridBagConstraints);

        txtODToInterest.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panLoanMultiSearch.add(txtODToInterest, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSearchCondition.add(panLoanMultiSearch, gridBagConstraints);

        panMultiSearch2.setMinimumSize(new java.awt.Dimension(210, 145));
        panMultiSearch2.setPreferredSize(new java.awt.Dimension(210, 145));
        panMultiSearch2.setLayout(new java.awt.GridBagLayout());

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
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        panMultiSearch2.add(btnProcess, gridBagConstraints);

        chkLoanDue.setText("Loan Due Split");
        chkLoanDue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkLoanDueActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        panMultiSearch2.add(chkLoanDue, gridBagConstraints);

        txtInstFreq.setMinimumSize(new java.awt.Dimension(30, 21));
        txtInstFreq.setPreferredSize(new java.awt.Dimension(30, 21));
        txtInstFreq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtInstFreqActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panMultiSearch2.add(txtInstFreq, gridBagConstraints);

        lblInstFreq.setText("Installments Freq");
        lblInstFreq.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 15);
        panMultiSearch2.add(lblInstFreq, gridBagConstraints);

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

        panTable.setMinimumSize(new java.awt.Dimension(600, 360));
        panTable.setPreferredSize(new java.awt.Dimension(600, 360));
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
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
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
        srcTable.setViewportView(tblData);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panTable.add(srcTable, gridBagConstraints);

        lblNoOfRecords.setText("No. of Records Found : ");
        lblNoOfRecords.setMaximumSize(new java.awt.Dimension(140, 18));
        lblNoOfRecords.setMinimumSize(new java.awt.Dimension(140, 18));
        lblNoOfRecords.setPreferredSize(new java.awt.Dimension(140, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panTable.add(lblNoOfRecords, gridBagConstraints);

        lblNoOfRecordsVal.setMaximumSize(new java.awt.Dimension(230, 85));
        lblNoOfRecordsVal.setMinimumSize(new java.awt.Dimension(80, 18));
        lblNoOfRecordsVal.setPreferredSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panTable.add(lblNoOfRecordsVal, gridBagConstraints);

        lblSelectedRecordVal.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblSelectedRecordVal.setMaximumSize(new java.awt.Dimension(230, 85));
        lblSelectedRecordVal.setMinimumSize(new java.awt.Dimension(80, 18));
        lblSelectedRecordVal.setPreferredSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panTable.add(lblSelectedRecordVal, gridBagConstraints);

        lblSelectedRecord.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSelectedRecord.setText("Selected Record");
        lblSelectedRecord.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblSelectedRecord.setMaximumSize(new java.awt.Dimension(100, 18));
        lblSelectedRecord.setMinimumSize(new java.awt.Dimension(100, 18));
        lblSelectedRecord.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panTable.add(lblSelectedRecord, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(panTable, gridBagConstraints);

        panSearch.setLayout(new java.awt.GridBagLayout());

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

        btnPrint.setText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panSearch.add(btnPrint, gridBagConstraints);

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

        lblFromSmsParticulars.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblFromSmsParticulars.setText("First Description");
        lblFromSmsParticulars.setToolTipText("");
        lblFromSmsParticulars.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblFromSmsParticulars.setMaximumSize(new java.awt.Dimension(150, 18));
        lblFromSmsParticulars.setMinimumSize(new java.awt.Dimension(150, 18));
        lblFromSmsParticulars.setPreferredSize(new java.awt.Dimension(150, 18));

        lblToSmsParticulars.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblToSmsParticulars.setText("Second Description");
        lblToSmsParticulars.setToolTipText("");
        lblToSmsParticulars.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblToSmsParticulars.setMaximumSize(new java.awt.Dimension(150, 18));
        lblToSmsParticulars.setMinimumSize(new java.awt.Dimension(150, 18));
        lblToSmsParticulars.setPreferredSize(new java.awt.Dimension(150, 18));

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

        jScrollPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        txtFromSmsDescription.setToolTipText("");
        txtFromSmsDescription.setPreferredSize(new java.awt.Dimension(4, 18));
        jScrollPane1.setViewportView(txtFromSmsDescription);

        txtTOSmsDescription.setToolTipText("");
        txtTOSmsDescription.setName("");
        txtTOSmsDescription.setPreferredSize(new java.awt.Dimension(4, 18));
        jScrollPane2.setViewportView(txtTOSmsDescription);

        lstItem.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "INSTOD", "ODINT", "PRNOD" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(lstItem);

        javax.swing.GroupLayout panGuarantorLayout = new javax.swing.GroupLayout(panGuarantor);
        panGuarantor.setLayout(panGuarantorLayout);
        panGuarantorLayout.setHorizontalGroup(
            panGuarantorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(panGuarantorLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(lblFromSmsParticulars, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblToSmsParticulars, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnsms, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
        );
        panGuarantorLayout.setVerticalGroup(
            panGuarantorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panGuarantorLayout.createSequentialGroup()
                .addGroup(panGuarantorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panGuarantorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(panGuarantorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panGuarantorLayout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addComponent(btnsms, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panGuarantorLayout.createSequentialGroup()
                                .addGap(33, 33, 33)
                                .addComponent(lblToSmsParticulars, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panGuarantorLayout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(lblFromSmsParticulars, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(113, Short.MAX_VALUE))
        );

        jScrollPane2.getAccessibleContext().setAccessibleName("");

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

    private void btnsmsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsmsActionPerformed
        // TODO add your handling code here:
  
        if (txtFromSmsDescription.getText().length() == 0) {
            ClientUtil.displayAlert("Please Enter First part Description Then  send SMS");
            return;
        }
        if (txtTOSmsDescription.getText().length() == 0) {
            ClientUtil.displayAlert("Please Enter Second part Description Then  send SMS");
            return;
        }
        boolean count = false;
        for (int i = 0; i < tblData.getRowCount(); i++) {
            if ((Boolean) tblData.getValueAt(i, 0)) {
                count = true;
            }
        }
    if (!count) {
        ClientUtil.showMessageWindow(" NO Rows Selected !!! ");
        count = false;
        return;
    }
        CommonUtil comm = new CommonUtil();
        final JDialog loading = comm.addProgressBar();
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws InterruptedException /**
             * Execute some operation
             */
            {
                try {
                    HashMap smsMap = new HashMap();
                    if (tblData.getRowCount() > 0) {
                        if (chkLoanDue.isSelected()) {
                            for (int i = 0; i < tblData.getRowCount(); i++) {
                                if (((Boolean) tblData.getValueAt(i, 0)).booleanValue()) {
                                    HashMap rowMap = new HashMap();
                                    String actNo = "";
                                    String schemeName = "";
                                    double interest = 0.0;
                                    String description = "";
                                    interest = CommonUtil.convertObjToDouble(tblData.getValueAt(i, 6)).doubleValue();
                                    actNo = CommonUtil.convertObjToStr(tblData.getValueAt(i, 1));
                                    schemeName = CommonUtil.convertObjToStr(cboProdId.getSelectedItem());
                                    rowMap.put("ACT_NUM", actNo);
                                    rowMap.put("SCHEME_NAME", schemeName);
                                    String smsDataString = "  OutStanding Amount is : " + CommonUtil.convertObjToStr(tblData.getValueAt(i, 5)) + "  Interest Amount is : " + interest + "";
                                    description += txtFromSmsDescription.getText();
                                    description += smsDataString;
                                    description += txtTOSmsDescription.getText();
                                    rowMap.put("MESSAGE", description);
                                    rowMap.put("PHONE_NUMBER", CommonUtil.convertObjToStr(tblData.getValueAt(i, 8)));
                                    rowMap.put("SMS_MODULE", "OVER DUE REMINDER");
                                    rowMap.put("BRANCH_ID", com.see.truetransact.ui.TrueTransactMain.BRANCH_ID);
                                    smsMap.put(actNo, rowMap);
                                }
                            }
                        } else {
                            int[] selectedIx = lstItem.getSelectedIndices();
                            // Get all the selected items using the indices
                           
                            Boolean instFlag = false;
                            Boolean odIntFlag = false;
                            Boolean prnODFlag = false;
                            for (int j = 0; j < selectedIx.length; j++) {
                                Object sel = lstItem.getModel().getElementAt(selectedIx[j]);
//                                System.out.println("sel" + sel);
                                if (sel.equals("INSTOD")) {
                                    instFlag = true;
                                    //     middleString ="INSTOD : "+CommonUtil.convertObjToStr(tblData.getValueAt(i, 8));
                                }
                                if (sel.equals("ODINT")) {
                                    odIntFlag = true;
                                }
                                if (sel.equals("PRNOD")) {
                                    prnODFlag = true;
                                }
                            }
                            for (int i = 0; i < tblData.getRowCount(); i++) {
                                if (((Boolean) tblData.getValueAt(i, 0)).booleanValue()) {
                                    HashMap rowMap = new HashMap();
                                    String actNo = "";
                                    String schemeName = "";
                                    String description = "";
                                    String middleString = " ";
                                    actNo = CommonUtil.convertObjToStr(tblData.getValueAt(i, 3));
                                    schemeName = CommonUtil.convertObjToStr(cboProdId.getSelectedItem());
                                    rowMap.put("ACT_NUM", actNo);
                                    rowMap.put("SCHEME_NAME", schemeName);
//                                    String smsDataString = "  OutStanding Amount is : " + CommonUtil.convertObjToStr(tblData.getValueAt(i, 5)) + "  Interest Amount is : " + interest + "";
                                    if (instFlag) {
                                        middleString += "INSTOD : " + CommonUtil.convertObjToStr(tblData.getValueAt(i, 27));
                                    }
                                    if (odIntFlag) {
                                        middleString += "OD INT : " + CommonUtil.convertObjToStr(tblData.getValueAt(i, 16));
                                    }
                                    if (prnODFlag) {
                                        middleString += "Principle OD : " + CommonUtil.convertObjToStr(tblData.getValueAt(i, 15));
                                    }
                                    description += txtFromSmsDescription.getText();

                                    description += txtTOSmsDescription.getText();
                                    rowMap.put("MESSAGE", description);
                                    rowMap.put("PHONE_NUMBER", CommonUtil.convertObjToStr(tblData.getValueAt(i, 8)));
                                    rowMap.put("SMS_MODULE", "OVER DUE REMINDER");
                                    rowMap.put("BRANCH_ID", com.see.truetransact.ui.TrueTransactMain.BRANCH_ID);
                                    smsMap.put(actNo, rowMap);
                                }
                            }
                        }     
                        if (smsMap.size() > 0) {
                            HashMap smsDataMap = new HashMap();
                            smsDataMap.put("SMS", smsMap);
                            smsDataMap.put("LOAN_OVER_DUE", "LOAN_OVER_DUE");
                            smsDataMap.put(CommonConstants.BRANCH_ID, com.see.truetransact.ui.TrueTransactMain.BRANCH_ID);
                            observable.SendSMS(smsDataMap);
                            btnsms.setEnabled(false);
                        }
                    }
                    smsMap = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

    }//GEN-LAST:event_btnsmsActionPerformed


    private void btnClear1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClear1ActionPerformed
        // TODO add your handling code here:
        ClientUtil.clearAll(this); 
        btnsms.setVisible(false);
        lblSelectedRecordVal.setText("");
        lblNoOfRecordsVal.setText("");
//        selectSuritiesOnly(false);
        counthashMap=new HashMap();
        counthashMap=null;
        suritycount = 0;
    }//GEN-LAST:event_btnClear1ActionPerformed

    private void tblDataMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseReleased

    }//GEN-LAST:event_tblDataMouseReleased
    
 
    public void fillData(Object param) {

        final HashMap hash = (HashMap) param;
        System.out.println("Hash: " + hash);
        if (viewType == FROMACTNO) {
            txtFromAccountno.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
        } else if (viewType == TOACTNO) {
            txtTOAccountno.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
        }

    }

    private void setVisibleFields(boolean flag) {
        lblFromDate.setVisible(flag);
        lblToDate.setVisible(flag);
        tdtFromDate.setVisible(flag);
//        lblFromAccountno.setVisible(flag);
//        txtFromAccountno.setVisible(flag);
//        lblTOAccountno.setVisible(flag);
//        txtTOAccountno.setVisible(flag);
//        btnFromAccountno.setVisible(flag);
//        btnTOAccountno.setVisible(flag);
        tdtToDate.setVisible(flag);
    }
    private void cboProdTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdTypeActionPerformed
        // TODO add your handling code here:
        String prodType = String.valueOf(cboProdType.getSelectedItem());
        if (prodType.length() > 0) {
            observable.fillDropDown(prodType);
            cboProdId.setModel(observable.getCbmProdId());
            
        }
 setVisibleFields(true);
    }//GEN-LAST:event_cboProdTypeActionPerformed

    private void btnProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessActionPerformed
        // TODO add your handling code here:
        lblSelectedRecordVal.setText("");
        lblNoOfRecordsVal.setText("");
        boolean isOK = false;
        if (!CommonUtil.convertObjToStr(cboProdType.getSelectedItem()).equals("")) {
            isOK = true;
        } else {
            isOK = false;
            ClientUtil.displayAlert("Select Product ...");
            return;
        }
        if (!CommonUtil.convertObjToStr(cboProdId.getSelectedItem()).equals("")) {
            isOK = true;
        } else {
            isOK = false;
            ClientUtil.displayAlert("Select Product Type...");
            return;
        }
        if (isOK) {
            populateData();
        }
      
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

            hash.put("PROD_ID", prodId);
            hash.put(CommonConstants.BRANCH_ID, com.see.truetransact.ui.TrueTransactMain.BRANCH_ID);
        }
        viewMap.put(CommonConstants.MAP_WHERE, hash);

        new ViewAll(this, viewMap).show();

    }

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

   
    private void chkSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSelectAllActionPerformed
        // Add your handling code here:
       observable.setSelectAll(tblData, new Boolean(chkSelectAll.isSelected()));
        setSelectedRecord();
    }//GEN-LAST:event_chkSelectAllActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

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
        calculateTotalApplCharges();
    }

    private void calculateTotalApplCharges() {
    }

    public void populateData() {
//        updateOBFields();
        String behavesLike = "";
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        boolean isOK = false;
        if (chkLoanDue.isSelected()) {
            viewMap.put(CommonConstants.MAP_NAME, "GetTLDueSplit");
            if (CommonUtil.convertObjToStr(txtInstFreq.getText()).length() > 0) {
                whereMap.put("FRQ", txtInstFreq.getText());
            } else {
                whereMap.put("FRQ", 1);
            }
        } else {
            viewMap.put(CommonConstants.MAP_NAME, "GetTLOverDueList");
        }
        if (String.valueOf(observable.getCbmProdId().getKeyForSelected()).length() > 0) {
            whereMap.put("PROD_ID", observable.getCbmProdId().getKeyForSelected());
        }
        if (tdtFromDate.getDateValue() != null && tdtFromDate.getDateValue().length() > 0) {
            whereMap.put("FROM_DT", getProperDate(DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue())));
        }
        if (tdtToDate.getDateValue() != null && tdtToDate.getDateValue().length() > 0) {
            whereMap.put("TO_DT", getProperDate(DateUtil.getDateMMDDYYYY(tdtToDate.getDateValue())));
        }
        if (CommonUtil.convertObjToStr(txtFromAccountno.getText()).length() > 0) {
            whereMap.put("FROM_ACCT_NUM", txtFromAccountno.getText());
        }
        if (CommonUtil.convertObjToStr(txtTOAccountno.getText()).length() > 0) {
            whereMap.put("TO_ACCT_NUM", txtTOAccountno.getText());
        }
        if(!chkLoanDue.isSelected()){
        if (CommonUtil.convertObjToStr(txtODInstallment.getText()).length() > 0) {
            whereMap.put("OD_INST", txtODInstallment.getText());
        }
        if (CommonUtil.convertObjToStr(txtODFromPrinciple.getText()).length() > 0) {
            whereMap.put("OD_FROM_PRINCIPLE", txtODFromPrinciple.getText());
        }
        if (CommonUtil.convertObjToStr(txtODToPrinciple.getText()).length() > 0) {
            whereMap.put("OD_TO_PRINCIPLE", txtODToPrinciple.getText());
        }
        if (CommonUtil.convertObjToStr(txtODFromInterest.getText()).length() > 0) {
            whereMap.put("OD_FROM_INTEREST", txtODFromInterest.getText());
        }
        if (CommonUtil.convertObjToStr(txtODToInterest.getText()).length() > 0) {
            whereMap.put("OD_TO_INTEREST", txtODToInterest.getText());
        }
        }
        
        whereMap.put("AsOnDate", getProperDate(currDt));
        whereMap.put("BranchId", TrueTransactMain.BRANCH_ID);
        if (String.valueOf(observable.getCbmProdId().getKeyForSelected()).length() > 0) {
            whereMap.put("ProdDesc", observable.getCbmProdId().getDataForKey(observable.getCbmProdId().getKeyForSelected()));
        } else {
            whereMap.put("ProdDesc", observable.getCbmProdId().getDataForKey(observable.getCbmProdId().getKeyForSelected()));
        }
        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        try {
            log.info("populateData...");
            ArrayList heading = observable.populateData(viewMap, tblData);
            lblNoOfRecordsVal.setText(String.valueOf(tblData.getRowCount()));
            if (tblData.getRowCount() > 0) {
                btnsms.setVisible(true);
                btnsms.setEnabled(true);
            } else {
                ClientUtil.showMessageWindow(" No Data !!! ");
                btnsms.setVisible(false);
            }
            heading = null;
        } catch (Exception e) {
            System.err.println("Exception " + e.toString() + "Caught");
            e.printStackTrace();
        }
        viewMap = null;
        whereMap = null;
    }

    private void createCboProdType() {
        if (((String) TrueTransactMain.BANKINFO.get("BANK_NAME")).toUpperCase().lastIndexOf("MAHILA") != -1) {
            cboProdType.addItem("");
            cboProdType.addItem("Gold Loans");
            cboProdType.addItem("Other Loans");
        } else {
            cboProdType.addItem("");
            HashMap whereMap = new HashMap();
            List loanProductLst = ClientUtil.executeQuery("getSelectLoanProducts", whereMap);
            if (loanProductLst != null && loanProductLst.size() > 0) {
                for (int i = 0; i < loanProductLst.size(); i++) {
                    whereMap = (HashMap) loanProductLst.get(i);
                    String product_type = CommonUtil.convertObjToStr(whereMap.get("PROD_TYPE"));
                    cboProdType.addItem(product_type);
                }

            }
//            cboProdType.addItem("MDS");
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
        ClientUtil.showMessageWindow(observable.getResult());

    }

    private void btnTOAccountnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTOAccountnoActionPerformed
        // TODO add your handling code here:
        popUp(TOACTNO);
    }//GEN-LAST:event_btnTOAccountnoActionPerformed

    private void txtTOAccountnoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTOAccountnoFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTOAccountnoFocusLost

    private void btnFromAccountnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFromAccountnoActionPerformed
        // TODO add your handling code here:
        popUp(FROMACTNO);
    }//GEN-LAST:event_btnFromAccountnoActionPerformed

    private void txtFromAccountnoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFromAccountnoFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFromAccountnoFocusLost

    private void chkLoanDueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkLoanDueActionPerformed
        // TODO add your handling code here:
        if (chkLoanDue.isSelected() == true) {
            ClientUtil.enableDisable(panLoanMultiSearch, false);
            panLoanMultiSearch.setVisible(false);
            lstItem.setVisible(false);
            jScrollPane3.setVisible(false);
            btnPrint.setEnabled(false);
        } else {
            ClientUtil.enableDisable(panLoanMultiSearch, true);
            panLoanMultiSearch.setVisible(true);
            jScrollPane3.setVisible(true);
            lstItem.setVisible(true);
             btnPrint.setEnabled(true);
        }
    }//GEN-LAST:event_chkLoanDueActionPerformed

    private void txtInstFreqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtInstFreqActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtInstFreqActionPerformed

    private void tblDataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblDataMouseClicked

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        try {
            HashMap whereMap = new HashMap();
            if (String.valueOf(observable.getCbmProdId().getKeyForSelected()).length() > 0) {
                whereMap.put("PROD_ID", observable.getCbmProdId().getKeyForSelected());
            }
            if (tdtFromDate.getDateValue() != null && tdtFromDate.getDateValue().length() > 0) {
                whereMap.put("FromDt", getProperDate(DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue())));
            }
            if (tdtToDate.getDateValue() != null && tdtToDate.getDateValue().length() > 0) {
                whereMap.put("ToDt", getProperDate(DateUtil.getDateMMDDYYYY(tdtToDate.getDateValue())));
            }
            if (CommonUtil.convertObjToStr(txtFromAccountno.getText()).length() > 0) {
                whereMap.put("FrmAcctNum", txtFromAccountno.getText());
            } 
            if (CommonUtil.convertObjToStr(txtTOAccountno.getText()).length() > 0) {
                whereMap.put("ToAcctNum", txtTOAccountno.getText());
            } 
            if (!chkLoanDue.isSelected()) {
                
                if (CommonUtil.convertObjToStr(txtODInstallment.getText()).length() > 0) {
                    whereMap.put("DueNos", txtODInstallment.getText());
                } 
                if (CommonUtil.convertObjToStr(txtODFromPrinciple.getText()).length() > 0) {
                    whereMap.put("FrmPrincipalDue", txtODFromPrinciple.getText());
                } 
                if (CommonUtil.convertObjToStr(txtODToPrinciple.getText()).length() > 0) {
                    whereMap.put("ToPrincipalDue", txtODToPrinciple.getText());
                } 
                if (CommonUtil.convertObjToStr(txtODFromInterest.getText()).length() > 0) {
                    whereMap.put("FrmInterestDue", txtODFromInterest.getText());
                } 
                if (CommonUtil.convertObjToStr(txtODToInterest.getText()).length() > 0) {
                    whereMap.put("ToInterestDue", txtODToInterest.getText());
                } 
                whereMap.put("AsOnDate", getProperDate(currDt));
                whereMap.put("BranchId", TrueTransactMain.BRANCH_ID);
                if (String.valueOf(observable.getCbmProdId().getKeyForSelected()).length() > 0) {
                    whereMap.put("ProdDesc", observable.getCbmProdId().getDataForKey(observable.getCbmProdId().getKeyForSelected()));
                } else {
                    whereMap.put("ProdDesc", observable.getCbmProdId().getDataForKey(observable.getCbmProdId().getKeyForSelected()));
                }
                String repName = "TL_OverdueList_App";
                callTTIntergration(repName, whereMap);
            }
        } catch (Exception e) {
            System.out.println("Exception" + e);
        }
    }//GEN-LAST:event_btnPrintActionPerformed
   
     public void callTTIntergration(String repName,HashMap parMap){
            System.out.println("Here is the param map :: " + parMap);
            TTIntegration ttIntgration = null;
            ttIntgration.setParam(parMap);
           ttIntgration.integrationForPrint(repName,true);
//            ttIntgration.integrationForPrintToPDF(repName);
        }
     
    private void internationalize() {
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
    private com.see.truetransact.uicomponent.CButton btnFromAccountno;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnProcess;
    private com.see.truetransact.uicomponent.CButton btnTOAccountno;
    private com.see.truetransact.uicomponent.CButton btnsms;
    private com.see.truetransact.uicomponent.CPanel cPanel2;
    private com.see.truetransact.uicomponent.CPanel cPanel3;
    private com.see.truetransact.uicomponent.CComboBox cboProdId;
    private com.see.truetransact.uicomponent.CComboBox cboProdType;
    private com.see.truetransact.uicomponent.CCheckBox chkLoanDue;
    private com.see.truetransact.uicomponent.CCheckBox chkSelectAll;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private com.see.truetransact.uicomponent.CLabel lblFromAccountno;
    private com.see.truetransact.uicomponent.CLabel lblFromDate;
    private com.see.truetransact.uicomponent.CLabel lblFromSmsParticulars;
    private com.see.truetransact.uicomponent.CLabel lblInstFreq;
    private com.see.truetransact.uicomponent.CLabel lblInstOD;
    private com.see.truetransact.uicomponent.CLabel lblNoOfRecords;
    private com.see.truetransact.uicomponent.CLabel lblNoOfRecordsVal;
    private com.see.truetransact.uicomponent.CLabel lblODFromInt;
    private com.see.truetransact.uicomponent.CLabel lblODFromPrinciple;
    private com.see.truetransact.uicomponent.CLabel lblODToInt;
    private com.see.truetransact.uicomponent.CLabel lblODToPrinciple;
    private com.see.truetransact.uicomponent.CLabel lblProdId;
    private com.see.truetransact.uicomponent.CLabel lblProdType;
    private com.see.truetransact.uicomponent.CLabel lblSelectedRecord;
    private com.see.truetransact.uicomponent.CLabel lblSelectedRecordVal;
    private com.see.truetransact.uicomponent.CLabel lblTOAccountno;
    private com.see.truetransact.uicomponent.CLabel lblToDate;
    private com.see.truetransact.uicomponent.CLabel lblToSmsParticulars;
    private com.see.truetransact.uicomponent.CList lstItem;
    private com.see.truetransact.uicomponent.CPanel panGuarantor;
    private com.see.truetransact.uicomponent.CPanel panLoanMultiSearch;
    private com.see.truetransact.uicomponent.CPanel panMultiSearch;
    private com.see.truetransact.uicomponent.CPanel panMultiSearch2;
    private com.see.truetransact.uicomponent.CPanel panSearch;
    private com.see.truetransact.uicomponent.CPanel panSearchCondition;
    private com.see.truetransact.uicomponent.CPanel panTable;
    private com.see.truetransact.uicomponent.CSeparator sptLine;
    private com.see.truetransact.uicomponent.CScrollPane srcTable;
    private com.see.truetransact.uicomponent.CTable tblData;
    private com.see.truetransact.uicomponent.CDateField tdtFromDate;
    private com.see.truetransact.uicomponent.CDateField tdtToDate;
    private com.see.truetransact.uicomponent.CTextField txtFromAccountno;
    private com.see.truetransact.uicomponent.CTextArea txtFromSmsDescription;
    private com.see.truetransact.uicomponent.CTextField txtInstFreq;
    private com.see.truetransact.uicomponent.CTextField txtODFromInterest;
    private com.see.truetransact.uicomponent.CTextField txtODFromPrinciple;
    private com.see.truetransact.uicomponent.CTextField txtODInstallment;
    private com.see.truetransact.uicomponent.CTextField txtODToInterest;
    private com.see.truetransact.uicomponent.CTextField txtODToPrinciple;
    private com.see.truetransact.uicomponent.CTextField txtTOAccountno;
    private com.see.truetransact.uicomponent.CTextArea txtTOSmsDescription;
    // End of variables declaration//GEN-END:variables
}
