/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * TransAllUI.java
 *
 * Created on August 6, 2003, 10:53 AM
 */
package com.see.truetransact.ui.salaryrecovery;

import java.util.HashMap;
import javax.swing.table.TableModel;
import javax.swing.event.TableModelEvent;
import java.util.Observable;
import javax.swing.table.DefaultTableModel;
import com.see.truetransact.commonutil.interestcalc.Rounding;
import java.util.Date;
import java.text.SimpleDateFormat;
import javax.swing.event.TableModelListener;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.clientexception.ClientParseException;
import org.apache.log4j.Logger;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.common.transaction.TransactionUI;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.transferobject.product.operativeacct.OperativeAcctProductTO;
import com.see.truetransact.ui.product.operativeacct.OperativeAcctProductOB;
import com.see.truetransact.ui.salaryrecovery.TransAllOB;
import com.see.truetransact.ui.salaryrecovery.TransAllMRB;
import com.see.truetransact.transferobject.operativeaccount.AccountClosingTO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.transaction.common.TransDetailsUI;
import java.awt.event.MouseAdapter;
import java.text.DecimalFormat;

/**
 * This form
 *
 * @author TransAllUI
 */
public class TransAllUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer, UIMandatoryField {

    private TransAllOB observable;
    private TransAllMRB objMandatoryRB = new TransAllMRB();//Instance for the MandatoryResourceBundle
    private HashMap mandatoryMap;//Map for putting up MandatoryFields in the UI
    private String viewType = "";//Variable used to store ActionType(New,Edit,Delete)
    private final static ClientParseException parseException = ClientParseException.getInstance();//Instance of ClientParseException to log the Exceptions
    private final String AUTHORIZE = "Authorize";//Variable used when btnAuthorize is clicked
    private String strBorrowingNo = "";
    //On disbursal transaction
    private TableModelListener tableModelListener;
    private boolean rdoPrizedMember_Yes = false;
    private boolean rdoPrizedMember_No = false;
    private Date curr_dt = null;
    HashMap finalMap = new HashMap();
    DefaultTableModel model = null;
    double totalAmount = 0.0;
    private int rejectFlag = 0;
    double sanctionAmount = 0.0;
    double avalBalance = 0.0;
    double balanceAmt = 0.0;
    TransactionUI transactionUI = new TransactionUI();
    double amtBorrow = 0.0;
    String multiDis = "";
    List globalList = new ArrayList();
    HashMap termLoanMap = null;
    HashMap termTdMap = null;
    HashMap termSaMap = null;
    HashMap termMdsMap = null;
    HashMap termMdsChittalMap = null;
    HashMap termGlMap = null;
    HashMap hash = null;
    int count = 0;
    private boolean chittalFlag = false;
    private TransDetailsUI transDetailsUI = null;
    double interest = 0.0;
    boolean flag = false;
    DecimalFormat df = new DecimalFormat("#.##");
    private boolean isFilled = false;
    boolean fromCashierAuthorizeUI = false;
    boolean fromManagerAuthorizeUI = false;
    AuthorizeListCreditUI CashierauthorizeListUI=null;
    AuthorizeListDebitUI ManagerauthorizeListUI=null;
    
    public TransAllUI() {
        initComponents();
        setFieldNames();
        setObservable();
        observable.resetForm();
        setMaxLengths();
        initComponentData();
        curr_dt = ClientUtil.getCurrentDate();
        setMandatoryHashMap();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panTransAll, getMandatoryHashMap());
        panTrans.add(transactionUI);
        transactionUI.setSourceScreen("TRANSALL");
        observable.setTransactionOB(transactionUI.getTransactionOB());
        ClientUtil.enableDisable(panTransAll, false);
        setButtonEnableDisable();
        transactionUI.setProdType();
        transDetailsUI = new TransDetailsUI(cPanel2);
        observable.resetForm();
        txtMemberNo.setVisible(false);
        cPanel2.setVisible(false);
        chkSelectAll.setSelected(true);
    }

    private void setMaxLengths() {
        txtTotPrincipal.setValidation(new CurrencyValidation(16, 2));
        txtTotInterest.setValidation(new CurrencyValidation(16, 2));
        txtTotPenel.setValidation(new CurrencyValidation(16, 2));
        txtTotOthers.setValidation(new CurrencyValidation(16, 2));
        txtGrandTotal.setValidation(new CurrencyValidation(16, 2));
    }

    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtClockNo", new Boolean(true));
    }

    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    private void setObservable() {
        try {
            observable = TransAllOB.getInstance();
            observable.addObserver(this);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        rdoTransactionType = new com.see.truetransact.uicomponent.CButtonGroup();
        panAccountInfo = new com.see.truetransact.uicomponent.CPanel();
        panTransAll = new com.see.truetransact.uicomponent.CPanel();
        pan1 = new com.see.truetransact.uicomponent.CPanel();
        lblClockNo = new com.see.truetransact.uicomponent.CLabel();
        lblAccountHeadDisplay = new com.see.truetransact.uicomponent.CLabel();
        lblName = new com.see.truetransact.uicomponent.CLabel();
        lblCustomerNameDisplay = new com.see.truetransact.uicomponent.CLabel();
        panAcctHead = new com.see.truetransact.uicomponent.CPanel();
        lblMemberNo = new com.see.truetransact.uicomponent.CLabel();
        panAcctNum = new com.see.truetransact.uicomponent.CPanel();
        txtMemberNo = new com.see.truetransact.uicomponent.CTextField();
        btnClockHead = new com.see.truetransact.uicomponent.CButton();
        btnFind = new com.see.truetransact.uicomponent.CButton();
        txtName = new com.see.truetransact.uicomponent.CTextField();
        txtMemNo = new com.see.truetransact.uicomponent.CTextField();
        txtClockNo = new com.see.truetransact.uicomponent.CTextField();
        chkRetired = new com.see.truetransact.uicomponent.CCheckBox();
        panTrans = new com.see.truetransact.uicomponent.CPanel();
        srpTransaction = new com.see.truetransact.uicomponent.CScrollPane();
        tblTransaction = new com.see.truetransact.uicomponent.CTable();
        panTotal = new com.see.truetransact.uicomponent.CPanel();
        lblTotPenel = new com.see.truetransact.uicomponent.CLabel();
        txtTotPenel = new com.see.truetransact.uicomponent.CTextField();
        lblTotInterest = new com.see.truetransact.uicomponent.CLabel();
        txtTotInterest = new com.see.truetransact.uicomponent.CTextField();
        lblTotOthers = new com.see.truetransact.uicomponent.CLabel();
        txtTotOthers = new com.see.truetransact.uicomponent.CTextField();
        lblGrandTotal = new com.see.truetransact.uicomponent.CLabel();
        txtGrandTotal = new com.see.truetransact.uicomponent.CTextField();
        lblTotPrin = new com.see.truetransact.uicomponent.CLabel();
        txtTotPrincipal = new com.see.truetransact.uicomponent.CTextField();
        cPanel1 = new com.see.truetransact.uicomponent.CPanel();
        txtACHDID = new com.see.truetransact.uicomponent.CTextField();
        lblAcHdId = new com.see.truetransact.uicomponent.CLabel();
        btnAcHead = new com.see.truetransact.uicomponent.CButton();
        cButton1 = new com.see.truetransact.uicomponent.CButton();
        txtACHDDESC = new com.see.truetransact.uicomponent.CTextField();
        lblParticulars = new com.see.truetransact.uicomponent.CLabel();
        lblClearBalance = new com.see.truetransact.uicomponent.CLabel();
        lblClearBalanceAmt = new com.see.truetransact.uicomponent.CLabel();
        lblSelectAll = new com.see.truetransact.uicomponent.CLabel();
        chkSelectAll = new com.see.truetransact.uicomponent.CCheckBox();
        txtParticulars = new com.see.truetransact.uicomponent.CTextField();
        lblAcHdId2 = new com.see.truetransact.uicomponent.CLabel();
        cPanel2 = new com.see.truetransact.uicomponent.CPanel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        tbrOperativeAcctProduct = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace56 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace57 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace58 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace59 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace60 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace61 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        mbrCustomer = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptNew = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptSave = new javax.swing.JSeparator();
        mitPrint = new javax.swing.JMenuItem();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setMaximumSize(new java.awt.Dimension(950, 610));
        setMinimumSize(new java.awt.Dimension(950, 610));
        setPreferredSize(new java.awt.Dimension(950, 610));

        panAccountInfo.setMaximumSize(new java.awt.Dimension(736, 300));
        panAccountInfo.setMinimumSize(new java.awt.Dimension(736, 300));
        panAccountInfo.setPreferredSize(new java.awt.Dimension(736, 300));
        panAccountInfo.setLayout(null);

        panTransAll.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        panTransAll.setMinimumSize(new java.awt.Dimension(728, 280));
        panTransAll.setPreferredSize(new java.awt.Dimension(728, 280));
        panTransAll.setLayout(null);

        pan1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pan1.setMinimumSize(new java.awt.Dimension(900, 40));
        pan1.setPreferredSize(new java.awt.Dimension(900, 40));
        pan1.setLayout(null);

        lblClockNo.setText("Clock No");
        pan1.add(lblClockNo);
        lblClockNo.setBounds(10, 10, 90, 18);

        lblAccountHeadDisplay.setForeground(new java.awt.Color(0, 51, 204));
        lblAccountHeadDisplay.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblAccountHeadDisplay.setMaximumSize(new java.awt.Dimension(2250, 21));
        lblAccountHeadDisplay.setMinimumSize(new java.awt.Dimension(225, 21));
        lblAccountHeadDisplay.setPreferredSize(new java.awt.Dimension(225, 21));
        pan1.add(lblAccountHeadDisplay);
        lblAccountHeadDisplay.setBounds(112, 77, 225, 21);

        lblName.setText("Name");
        pan1.add(lblName);
        lblName.setBounds(480, 10, 34, 18);

        lblCustomerNameDisplay.setMinimumSize(new java.awt.Dimension(225, 21));
        lblCustomerNameDisplay.setPreferredSize(new java.awt.Dimension(225, 21));
        pan1.add(lblCustomerNameDisplay);
        lblCustomerNameDisplay.setBounds(112, 127, 225, 21);

        panAcctHead.setLayout(new java.awt.GridBagLayout());
        pan1.add(panAcctHead);
        panAcctHead.setBounds(110, 50, 0, 0);

        lblMemberNo.setText("Member Number");
        pan1.add(lblMemberNo);
        lblMemberNo.setBounds(246, 10, 120, 18);

        panAcctNum.setLayout(new java.awt.GridBagLayout());
        pan1.add(panAcctNum);
        panAcctNum.setBounds(110, 100, 0, 0);

        txtMemberNo.setMaxLength(10);
        txtMemberNo.setMinimumSize(new java.awt.Dimension(100, 21));
        pan1.add(txtMemberNo);
        txtMemberNo.setBounds(370, 10, 100, 21);

        btnClockHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnClockHead.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnClockHead.setMaximumSize(new java.awt.Dimension(21, 21));
        btnClockHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnClockHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnClockHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClockHeadActionPerformed(evt);
            }
        });
        pan1.add(btnClockHead);
        btnClockHead.setBounds(200, 10, 21, 21);

        btnFind.setText("Find");
        btnFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindActionPerformed(evt);
            }
        });
        pan1.add(btnFind);
        btnFind.setBounds(820, 10, 70, 20);
        pan1.add(txtName);
        txtName.setBounds(520, 10, 160, 21);

        txtMemNo.setEditable(false);
        txtMemNo.setMaxLength(10);
        txtMemNo.setMinimumSize(new java.awt.Dimension(100, 21));
        pan1.add(txtMemNo);
        txtMemNo.setBounds(370, 10, 100, 21);

        txtClockNo.setBackground(new java.awt.Color(220, 220, 220));
        txtClockNo.setAllowAll(true);
        txtClockNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtClockNoActionPerformed(evt);
            }
        });
        txtClockNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtClockNoFocusLost(evt);
            }
        });
        pan1.add(txtClockNo);
        txtClockNo.setBounds(100, 10, 100, 21);

        chkRetired.setText("Retired");
        pan1.add(chkRetired);
        chkRetired.setBounds(690, 10, 120, 20);

        panTransAll.add(pan1);
        pan1.setBounds(0, 0, 900, 40);

        panTrans.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panTrans.setMinimumSize(new java.awt.Dimension(900, 300));
        panTrans.setPreferredSize(new java.awt.Dimension(900, 300));
        panTrans.setLayout(new java.awt.GridBagLayout());
        panTransAll.add(panTrans);
        panTrans.setBounds(0, 290, 900, 210);

        srpTransaction.setMaximumSize(new java.awt.Dimension(900, 190));
        srpTransaction.setMinimumSize(new java.awt.Dimension(900, 90));
        srpTransaction.setPreferredSize(new java.awt.Dimension(900, 90));

        tblTransaction.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblTransaction.setPreferredScrollableViewportSize(new java.awt.Dimension(806, 331));
        tblTransaction.setSelectionBackground(new java.awt.Color(204, 204, 255));
        tblTransaction.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTransactionMouseClicked(evt);
            }
        });
        tblTransaction.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tblTransactionPropertyChange(evt);
            }
        });
        srpTransaction.setViewportView(tblTransaction);

        panTransAll.add(srpTransaction);
        srpTransaction.setBounds(0, 40, 900, 170);

        panTotal.setBorder(javax.swing.BorderFactory.createTitledBorder("Total"));
        panTotal.setMaximumSize(new java.awt.Dimension(900, 40));
        panTotal.setMinimumSize(new java.awt.Dimension(900, 40));
        panTotal.setLayout(null);

        lblTotPenel.setText(" Penal");
        panTotal.add(lblTotPenel);
        lblTotPenel.setBounds(380, 10, 40, 18);

        txtTotPenel.setEnabled(false);
        txtTotPenel.setMaxLength(10);
        txtTotPenel.setMaximumSize(new java.awt.Dimension(100, 21));
        txtTotPenel.setMinimumSize(new java.awt.Dimension(100, 21));
        panTotal.add(txtTotPenel);
        txtTotPenel.setBounds(420, 10, 100, 21);

        lblTotInterest.setText("Interest");
        panTotal.add(lblTotInterest);
        lblTotInterest.setBounds(210, 10, 50, 18);

        txtTotInterest.setEnabled(false);
        txtTotInterest.setMaxLength(10);
        txtTotInterest.setMinimumSize(new java.awt.Dimension(100, 21));
        panTotal.add(txtTotInterest);
        txtTotInterest.setBounds(260, 10, 100, 21);

        lblTotOthers.setText("Others");
        panTotal.add(lblTotOthers);
        lblTotOthers.setBounds(550, 10, 40, 18);

        txtTotOthers.setEnabled(false);
        txtTotOthers.setMaxLength(10);
        txtTotOthers.setMaximumSize(new java.awt.Dimension(100, 21));
        txtTotOthers.setMinimumSize(new java.awt.Dimension(100, 21));
        panTotal.add(txtTotOthers);
        txtTotOthers.setBounds(590, 10, 90, 21);

        lblGrandTotal.setText("Grand Total");
        panTotal.add(lblGrandTotal);
        lblGrandTotal.setBounds(700, 10, 70, 18);

        txtGrandTotal.setEnabled(false);
        txtGrandTotal.setMaxLength(10);
        txtGrandTotal.setMaximumSize(new java.awt.Dimension(100, 21));
        txtGrandTotal.setMinimumSize(new java.awt.Dimension(100, 21));
        txtGrandTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtGrandTotalActionPerformed(evt);
            }
        });
        panTotal.add(txtGrandTotal);
        txtGrandTotal.setBounds(770, 10, 110, 21);

        lblTotPrin.setText("Principal");
        panTotal.add(lblTotPrin);
        lblTotPrin.setBounds(30, 10, 50, 18);

        txtTotPrincipal.setEnabled(false);
        txtTotPrincipal.setMaxLength(10);
        txtTotPrincipal.setMinimumSize(new java.awt.Dimension(100, 21));
        panTotal.add(txtTotPrincipal);
        txtTotPrincipal.setBounds(80, 10, 100, 21);

        panTransAll.add(panTotal);
        panTotal.setBounds(0, 250, 900, 40);

        cPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        cPanel1.setMaximumSize(new java.awt.Dimension(900, 40));
        cPanel1.setMinimumSize(new java.awt.Dimension(900, 40));
        cPanel1.setPreferredSize(new java.awt.Dimension(900, 40));
        cPanel1.setLayout(null);

        txtACHDID.setEditable(false);
        cPanel1.add(txtACHDID);
        txtACHDID.setBounds(160, 10, 110, 21);

        lblAcHdId.setText("AC HD ID");
        cPanel1.add(lblAcHdId);
        lblAcHdId.setBounds(100, 10, 60, 18);

        btnAcHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAcHead.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnAcHead.setMaximumSize(new java.awt.Dimension(21, 21));
        btnAcHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnAcHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAcHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcHeadActionPerformed(evt);
            }
        });
        cPanel1.add(btnAcHead);
        btnAcHead.setBounds(270, 10, 21, 21);

        cButton1.setText("Add");
        cButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cButton1ActionPerformed(evt);
            }
        });
        cPanel1.add(cButton1);
        cButton1.setBounds(700, 10, 60, 20);

        txtACHDDESC.setEditable(false);
        cPanel1.add(txtACHDDESC);
        txtACHDDESC.setBounds(390, 10, 120, 21);

        lblParticulars.setText("Particulars");
        cPanel1.add(lblParticulars);
        lblParticulars.setBounds(520, 10, 70, 18);

        lblClearBalance.setForeground(new java.awt.Color(255, 51, 51));
        lblClearBalance.setText("Balance");
        cPanel1.add(lblClearBalance);
        lblClearBalance.setBounds(770, 10, 50, 20);
        cPanel1.add(lblClearBalanceAmt);
        lblClearBalanceAmt.setBounds(820, 10, 70, 20);

        lblSelectAll.setText("Select All");
        cPanel1.add(lblSelectAll);
        lblSelectAll.setBounds(10, 10, 56, 18);

        chkSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSelectAllActionPerformed(evt);
            }
        });
        cPanel1.add(chkSelectAll);
        chkSelectAll.setBounds(70, 10, 21, 21);

        txtParticulars.setEditable(false);
        cPanel1.add(txtParticulars);
        txtParticulars.setBounds(590, 10, 100, 21);

        lblAcHdId2.setText("AC HD DESC");
        cPanel1.add(lblAcHdId2);
        lblAcHdId2.setBounds(300, 10, 80, 18);

        panTransAll.add(cPanel1);
        cPanel1.setBounds(0, 210, 900, 40);
        panTransAll.add(cPanel2);
        cPanel2.setBounds(1353, 4, 10, 10);

        panAccountInfo.add(panTransAll);
        panTransAll.setBounds(10, 10, 900, 500);
        panAccountInfo.add(lblMsg);
        lblMsg.setBounds(140, 510, 780, 22);

        lblSpace1.setText(" Status :");
        panAccountInfo.add(lblSpace1);
        lblSpace1.setBounds(0, 510, 50, 22);

        lblStatus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblStatus.setText("                      ");
        panAccountInfo.add(lblStatus);
        lblStatus.setBounds(50, 510, 92, 22);

        getContentPane().add(panAccountInfo, java.awt.BorderLayout.CENTER);

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
        tbrOperativeAcctProduct.add(btnView);

        lblSpace5.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace5);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnNew);

        lblSpace56.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace56.setText("     ");
        lblSpace56.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace56.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace56.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace56);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnEdit);

        lblSpace57.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace57.setText("     ");
        lblSpace57.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace57.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace57.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace57);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnDelete);

        lblSpace2.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnSave);

        lblSpace58.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace58.setText("     ");
        lblSpace58.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace58.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace58.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace58);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnCancel);

        lblSpace3.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnAuthorize);

        lblSpace59.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace59.setText("     ");
        lblSpace59.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace59.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace59.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace59);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnException);

        lblSpace60.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace60.setText("     ");
        lblSpace60.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace60.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace60.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace60);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnReject);

        lblSpace4.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnPrint);

        lblSpace61.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace61.setText("     ");
        lblSpace61.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace61.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace61.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace61);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnClose);

        getContentPane().add(tbrOperativeAcctProduct, java.awt.BorderLayout.NORTH);

        panStatus.setLayout(new java.awt.GridBagLayout());
        getContentPane().add(panStatus, java.awt.BorderLayout.SOUTH);

        mbrCustomer.setName("mbrCustomer"); // NOI18N

        mnuProcess.setText("Transaction");
        mnuProcess.setName("mnuProcess"); // NOI18N

        mitNew.setText("New");
        mitNew.setName("mitNew"); // NOI18N
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mitEdit.setName("mitEdit"); // NOI18N
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mitDelete.setName("mitDelete"); // NOI18N
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);

        sptNew.setName("sptNew"); // NOI18N
        mnuProcess.add(sptNew);

        mitSave.setText("Save");
        mitSave.setName("mitSave"); // NOI18N
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancel");
        mitCancel.setName("mitCancel"); // NOI18N
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess.add(mitCancel);

        sptSave.setName("sptSave"); // NOI18N
        mnuProcess.add(sptSave);

        mitPrint.setText("Print");
        mitPrint.setName("mitPrint"); // NOI18N
        mnuProcess.add(mitPrint);

        mitClose.setText("Close");
        mitClose.setName("mitClose"); // NOI18N
        mnuProcess.add(mitClose);

        mbrCustomer.add(mnuProcess);

        setJMenuBar(mbrCustomer);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtClockNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtClockNoFocusLost
        // TODO add your handling code here:
        txtClockNoActionPerformed(null);
    }//GEN-LAST:event_txtClockNoFocusLost

     private void txtClockNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtClockNoActionPerformed
         // TODO add your handling code here:
         String clockNo = txtClockNo.getText();
         if (clockNo != null && !clockNo.equalsIgnoreCase("")) {
             HashMap dMap = new HashMap();
             dMap.put("CLOCK_NO", clockNo);
             List datList = ClientUtil.executeQuery("TransAll.getSelectClockListFocus", dMap);
             System.out.println("datList : " + datList);
             if (datList != null && datList.size() > 0) {
                 for (int i = 0; i < datList.size(); i++) {
                     HashMap map = (HashMap) datList.get(i);
                     String CNo = "", CName = "", custName = "", custId = "", memNo = "";
                     if (map.get("CLOCK_NO") != null) {
                         CNo = map.get("CLOCK_NO").toString();
                     }
                     if (map.get("MEMBER_NO") != null) {
                         custId = map.get("MEMBER_NO").toString();
                     }
                     if (map.get("NAME") != null) {
                         CName = map.get("NAME").toString();
                     }
                     if (map.get("MEM_NO") != null) {
                         memNo = map.get("MEM_NO").toString();
                     }
                     txtClockNo.setText(CNo);
                     txtName.setText(CName);
                     txtMemberNo.setText(custId);
                     txtMemNo.setText(memNo);
                 }
             } else {
                 displayAlert("Clock No does not exists!!!");
                 return;
             }
         } else {
             //  displayAlert("Please enter clock No!!!");
             //  return;
         }
    }//GEN-LAST:event_txtClockNoActionPerformed


    private void cButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cButton1ActionPerformed
        //add ACHD id in the table
        // String amtAcHdId=txtAmount.getText();
        String ac_hd_id = txtACHDID.getText();
        String particulars = txtParticulars.getText();
        if (ac_hd_id.equals("") || ac_hd_id == null) {
            displayAlert("Please enter Account head id!!!");
            return;
        }
        if (particulars.equals("") || particulars == null) {
            displayAlert("Please enter Particulars!!!");
            return;
        }
        String acHdDesc = txtACHDDESC.getText();
        for (int j = 0; j < tblTransaction.getRowCount(); j++) {
            if (tblTransaction.getValueAt(j, 1).equals(ac_hd_id)) {
                displayAlert("Already added...!!!!");
                txtACHDID.setText("");
                txtACHDDESC.setText("");
                return;
            }
        }
        ((DefaultTableModel) tblTransaction.getModel()).addRow(new Object[]{true, "GL", acHdDesc, ac_hd_id, "", "", "", "", particulars, "", "", "", "", ""});
        txtACHDID.setText("");
        txtACHDDESC.setText("");
        txtParticulars.setText("");
        count++;
    }//GEN-LAST:event_cButton1ActionPerformed

    private void tblTransactionPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tblTransactionPropertyChange
        // TODO add your handling code here:
        //setTableModelListener();
    }//GEN-LAST:event_tblTransactionPropertyChange

    private void btnFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindActionPerformed
        // TODO add your handling code here:
        String clockNo = txtClockNo.getText();
        if (clockNo == null || clockNo.equals("")) {
            ClientUtil.showAlertWindow("Please enter Clock No");
        } else {
            
            HashMap whereMap = new HashMap();
            whereMap.put("MEMBER_NO", txtMemberNo.getText().toString());
            whereMap.put("EXPORT_LIST", "EXPORT_LIST");
            observable.setTxtMemberNo(txtMemberNo.getText());
            observable.setTxtClockNo(txtClockNo.getText());
            observable.setTxtName(txtName.getText());
            if (chkRetired.isSelected()) {
                observable.setchkRetired("YES");
            } else {
                observable.setchkRetired("NO");
            }
            initTableData();
            chkSelectAll.setEnabled(true);
            chkSelectAll.setSelected(true);
            HashMap memMap = new HashMap();
            memMap.put("SUSPENSE_MEMBER_NO", txtMemNo.getText());
            List balanceList = ClientUtil.executeQuery("getSuspenseClearBalance", memMap);
            System.out.println("balanceList === " + balanceList);
            if (balanceList != null) {
                if (balanceList.size() > 0 && balanceList.get(0) != null) {
                    HashMap clearMap = (HashMap) balanceList.get(0);
                    lblClearBalanceAmt.setText(clearMap.get("CLEAR_BALANCE").toString());
                }
            }
            btnClockHead.setEnabled(false);
            if (tblTransaction.getRowCount() > 0) {
                txtClockNo.setEnabled(false);
                btnFind.setEnabled(false);
                btnClockHead.setEnabled(false);
            } else {
                txtName.setText("");
            }
        }
    }//GEN-LAST:event_btnFindActionPerformed

    private Object[][] setTableData() {
        HashMap whereMap = null;
        List lst = observable.getTableData();
        globalList = lst;
        if (lst != null && lst.size() > 0) {
            observable.setFinalList(lst);
            //   model= new DefaultTableModel((ArrayList)lst);
            Object totalList[][] = new Object[lst.size()][16];
            if (lst != null && lst.size() > 0) {
                for (int i = 0; i < lst.size(); i++) {
                    //  whereMap = (HashMap)lst.get(i);
                    ArrayList aList = (ArrayList) lst.get(i);
                    //  globalList=aList;
                    //   return aList;
//                 for (int j = 0; j < aList.size(); j++) {
                        totalList[i][0] = true;
                        totalList[i][1] = aList.get(0).toString();
                        totalList[i][2] = aList.get(1).toString();
                        totalList[i][3] = aList.get(2).toString();
                        totalList[i][4] = aList.get(3).toString();
                        totalList[i][5] = aList.get(4).toString();
                        totalList[i][6] = aList.get(5).toString();
                        totalList[i][7] = aList.get(6).toString();
                        totalList[i][8] = aList.get(7).toString();
                        totalList[i][9] = aList.get(8).toString();
                        totalList[i][10] = aList.get(9).toString();
                        totalList[i][11] = aList.get(10).toString();
                        totalList[i][12] = aList.get(11).toString();
                        totalList[i][13] = aList.get(12).toString();
                        totalList[i][14] = aList.get(13).toString();
                        totalList[i][15] = aList.get(14).toString();
                        // //System.out.println("DATAA==="+aList.get(j));
                        if (aList.get(0) != null && aList.get(0).toString().equals("TL")) {
                            getLoanDetails1(aList.get(0).toString(), aList.get(1).toString(), aList.get(2).toString(), aList.get(3).toString(), aList.get(4).toString(), aList.get(5).toString(), aList.get(6).toString(), aList.get(7).toString(), aList.get(8).toString(), aList.get(9).toString(), aList.get(10).toString(), aList.get(11).toString(), aList.get(12).toString(), aList.get(13).toString());
                        }
                        if (aList.get(0) != null && aList.get(0).toString().equals("MDS")) {
                            calcEachChittal1(aList.get(0).toString(), aList.get(1).toString(), aList.get(2).toString(), aList.get(3).toString(), aList.get(4).toString(), aList.get(5).toString(), aList.get(6).toString(), aList.get(7).toString(), aList.get(8).toString(), aList.get(9).toString(), aList.get(10).toString(), aList.get(11).toString(), aList.get(12).toString(), aList.get(13).toString());
                        }
                        if (aList.get(0) != null && aList.get(0).toString().equals("AD")) {
                            //System.out.println("Ad");
                            getADDetails(aList.get(0).toString(), aList.get(1).toString(), aList.get(2).toString(), aList.get(3).toString(), aList.get(4).toString(), aList.get(5).toString(), aList.get(6).toString(), aList.get(7).toString(), aList.get(8).toString(), aList.get(9).toString(), aList.get(10).toString(), aList.get(11).toString(), aList.get(12).toString(), aList.get(13).toString());
                        }
                        if (aList.get(0) != null && aList.get(0).toString().equals("SA")) {
                            getSADetails(aList.get(0).toString(), aList.get(1).toString(), aList.get(2).toString(), aList.get(3).toString(), aList.get(4).toString(), aList.get(5).toString(), aList.get(6).toString(), aList.get(7).toString(), aList.get(8).toString(), aList.get(9).toString(), aList.get(10).toString(), aList.get(11).toString(), aList.get(12).toString(), aList.get(13).toString());
                        }
                        if (aList.get(0) != null && aList.get(0).toString().equals("TD")) {
                            getTDDetails(aList.get(0).toString(), aList.get(1).toString(), aList.get(2).toString(), aList.get(3).toString(), aList.get(4).toString(), aList.get(5).toString(), aList.get(6).toString(), aList.get(7).toString(), aList.get(8).toString(), aList.get(9).toString(), aList.get(10).toString(), aList.get(11).toString(), aList.get(12).toString(), aList.get(13).toString());
                        }
//                    }
                }
            }
            return totalList;
        } else {
            ClientUtil.displayAlert("No Data!!! ");
            observable.resetForm();
            txtName.setText("");
        }
        return null;

    }

    public void initTableData() {
        // model=new javax.swing.table.DefaultTableModel();
        tblTransaction.setModel(new javax.swing.table.DefaultTableModel(
                setTableData(),
                new String[]{
                    "Select", "Sch", "Description", "A/C No", "Paying", "Principal", "Penal", "Interest", "Bonus", "Others", "Notice", "Arbitration", "prodId", "Total", "InstDue", "InstAmt"}
        ) {
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
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                true, false, false, false, true, false, false, false, false, false, true, true, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                if (columnIndex == 4 || columnIndex == 6 || columnIndex == 7 || columnIndex == 10 || columnIndex == 11) {
                    return true;
                }
                return canEdit[columnIndex];
            }
        });
        setWidthColumns();

        tblTransaction.setCellSelectionEnabled(true);
        tblTransaction.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tblTransactionPropertyChange(evt);
            }
        });
        setTableModelListener();
        if (tblTransaction.getRowCount() > 0) {
            // boolean chk=((Boolean)tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 0)).booleanValue(); 
            //System.out.println("calcTotal inuit=====");
            calcTotal(true, tblTransaction.getSelectedRow(), 0);
        }
    }

    public void setWidthColumns() {
        tblTransaction.getColumnModel().getColumn(13).setMinWidth(0);
        tblTransaction.getColumnModel().getColumn(13).setMaxWidth(0);
        tblTransaction.getColumnModel().getColumn(13).setWidth(0);
        tblTransaction.getColumnModel().getColumn(1).setMinWidth(0);
        tblTransaction.getColumnModel().getColumn(1).setMaxWidth(0);
        tblTransaction.getColumnModel().getColumn(1).setWidth(0);
        tblTransaction.getColumnModel().getColumn(12).setMinWidth(0);
        tblTransaction.getColumnModel().getColumn(12).setMaxWidth(0);
        tblTransaction.getColumnModel().getColumn(12).setWidth(0);

        tblTransaction.getColumnModel().getColumn(0).setPreferredWidth(60);
        tblTransaction.getColumnModel().getColumn(2).setPreferredWidth(200);
        tblTransaction.getColumnModel().getColumn(3).setPreferredWidth(130);
        tblTransaction.getColumnModel().getColumn(4).setPreferredWidth(70);
        tblTransaction.getColumnModel().getColumn(5).setPreferredWidth(70);
        tblTransaction.getColumnModel().getColumn(6).setPreferredWidth(70);
        tblTransaction.getColumnModel().getColumn(7).setPreferredWidth(70);
        tblTransaction.getColumnModel().getColumn(8).setPreferredWidth(70);
        tblTransaction.getColumnModel().getColumn(9).setPreferredWidth(70);
        tblTransaction.getColumnModel().getColumn(10).setPreferredWidth(70);
        tblTransaction.getColumnModel().getColumn(11).setPreferredWidth(70);
        //  tblTransaction.getColumnModel().getColumn(11).setPreferredWidth(70);
        tblTransaction.getColumnModel().getColumn(14).setPreferredWidth(70);
        tblTransaction.getColumnModel().getColumn(15).setPreferredWidth(70);

    }

    private void setTableModelListener() {
        flag = false;
        try {
            tableModelListener = new TableModelListener() {
                public void tableChanged(TableModelEvent e) {
                    if (e.getType() == TableModelEvent.UPDATE && flag == false) {
                        System.out.println("Cell " + e.getFirstRow() + ", " + e.getColumn() + " changed. The new value: " + tblTransaction.getModel().getValueAt(e.getFirstRow(), e.getColumn()));
                        int row = e.getFirstRow();
                        int column = e.getColumn();
                        int selectedRow = tblTransaction.getSelectedRow();
                        boolean chk = ((Boolean) tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 0)).booleanValue();
                        String scheme = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 1));
                        switch (column) {
                            case 4:
                                //if (column == 4 && chk) {//|| column ==6
                                //System.out.println("fi45555555555column==" + column);
                                TableModel model = tblTransaction.getModel();
                                String acc_no = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 3));
                                String noOfInsPay = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 4));
                                //System.out.println("AC NO=" + acc_no + " finalMap BEDFORE====" + finalMap);
                                if (scheme.equals("TL")) {
                                    System.out.println("11111111111111111");
                                    getLoanDetails("3", column, chk, "empty", selectedRow);
                                    //System.out.println("column =========== " + column);
                                } else if (scheme.equals("TD")) {
                                    System.out.println("222222222222222222222222");
                                    getTDDetails(column, chk, "empty", selectedRow);
                                } else if (scheme.equals("SA")) {
                                    System.out.println("333333333333333333");
                                    //if(columnNo==4){
                                        getSADetails(column, chk, "empty", selectedRow);
                                    //}
                                } else if (scheme.equals("GL")) {
                                    System.out.println("4444444444444444444444");
                                    getSBDetails(column, chk, "empty", selectedRow);
                                } else if (scheme.equals("MDS")) {
                                    System.out.println("555555555555555555555");
                                    if (acc_no.indexOf("_") != -1) {
                                        acc_no = acc_no.substring(0, acc_no.indexOf("_"));
                                    }
                                    java.awt.event.MouseEvent evt = null;
                                    calcEachChittal(column, chk, "empty", selectedRow);//e.
                                    //mdsSplitUp();
                                    if (column == 3) {
                                        //System.out.println("in table mousclickkkkkkk " + column);
                                        tblTransaction.addMouseListener(new java.awt.event.MouseAdapter() {

                                            public void mouseClicked(java.awt.event.MouseEvent evt) {
                                                //     //System.out.println("in table mousclickkkkkkk "+column);
//                                                  tblTransactionMouseClicked(evt);
                                            }
                                        });
                                    }
                                    // tblTransactionMouseClicked(null); 
                           /*
                                     * if(chittalFlag==false){ String chittalNo
                                     * ="";
                                     * finalMap.put(chittalNo,termMdsChittalMap);
                                     * }
                                     chittalFlag=false;
                                     */
                                    //  calcTotal();
                                    // }
                                } else if (scheme.equals("AD")) {
                                    getADDetails(column, chk, "empty", selectedRow);
                                } else if (column == 3) {
                                    getSBDetails(column, chk, "empty", selectedRow);
                                }
                                //System.out.println("calacToatal sel======" + selectedRow + " chk==" + chk + " column===" + column);
                                calcTotal(chk, selectedRow, column);
                                break;
                            case 6:
                                if(scheme.equals("TL") || scheme.equals("AD")){
                                    TLGetDetails(column,scheme);
                                }
                                if(scheme.equals("TD")){
                                     getTDDetails(column, chk, "empty", selectedRow);
                                }
                                calcTotal(chk, selectedRow, column);
                                break;
                            case 0:
                                //}else if (column == 0 && chk) {
                                //boolean chk = ((Boolean) tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 0)).booleanValue();
                                //int selectedRow=tblTransaction.getSelectedRow(); 
                                calcTotal(chk, selectedRow, column);
                                acc_no = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 3));
                                if (scheme.equals("MDS")) {
                                    if (acc_no.indexOf("_") != -1) {
                                        acc_no = acc_no.substring(0, acc_no.indexOf("_"));
                                    }
                                }
//                        if(finalMap!=null && finalMap.containsKey(acc_no) ) {
//                            if(chk==false){
//                             
//                                finalMap.remove(acc_no);
//                            }
//                        }
                                break;
                            case 7:
                                //}else if (column == 7 && chk) {
                                //boolean chk = ((Boolean) tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 0)).booleanValue();
                                if (scheme.equals("SA")) {
                                     //if(columnNo==7){
                                        getSADetails(column, chk, "empty", selectedRow);
                                     //}
                                }
//                    if (scheme.equals("TL")) {
//                        getLoanDetails("3", 4, chk, "empty", selectedRow);
//                        //System.out.println("column =========== " + column);
//                    } else if (scheme.equals("AD")) {
//                        getADDetails(4, chk, "empty", selectedRow);
//                    }
                                if(scheme.equals("TL") || scheme.equals("AD")){
                                    TLGetDetails(column,scheme);
                                }
                                calcTotal(chk, selectedRow, column);
                                break;
                            default:
                                calcTotal(chk, selectedRow, column);
                                break;
                            //}
                        }
                    }
                }
            };
            tblTransaction.getModel().addTableModelListener(tableModelListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void getADDetails(int column, boolean chk, String selectAll, int selectRow) {
        flag = true;
        try {
            String pay = CommonUtil.convertObjToStr(tblTransaction.getValueAt(selectRow, 4));
            String act_num = CommonUtil.convertObjToStr(tblTransaction.getValueAt(selectRow, 3));
            double principal = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 5));
            double interest = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 7));
            double total = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 14));
            String prod_id = CommonUtil.convertObjToStr(tblTransaction.getValueAt(selectRow, 12));
            String description = CommonUtil.convertObjToStr(tblTransaction.getValueAt(selectRow, 2));
            String acNo = CommonUtil.convertObjToStr(tblTransaction.getValueAt(selectRow, 3));
            String prodType = CommonUtil.convertObjToStr(tblTransaction.getValueAt(selectRow, 1));
            String scheme_Name = CommonUtil.convertObjToStr(tblTransaction.getValueAt(selectRow, 1));
            double transAmt = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 4)).doubleValue();
            double doubleval = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 4)).doubleValue();
            double penal = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 6)).doubleValue();
            double charges = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 9)).doubleValue();
            double totalAmt = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 13)).doubleValue();//13
            double clearBal = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 14)).doubleValue();//12
            clearBal = Math.abs(clearBal);
            double prin = CommonUtil.convertObjToDouble(principal);
            double paying = CommonUtil.convertObjToDouble(pay);
            double intr = CommonUtil.convertObjToDouble(interest);
            double tot = CommonUtil.convertObjToDouble(total);
            //System.out.println("paying==" + paying);
            //System.out.println("tot==" + tot);
            int selectedRow = selectRow;
            if (pay == null || pay.equals("") || CommonUtil.convertObjToDouble(pay) <= 0.0) {
                double principal2 = 0;
                double interest2 = 0;
                double penal2 = 0;
                double charges2 = 0;
                for (int i = 0; i < globalList.size(); i++) {
                    ArrayList aList2 = (ArrayList) globalList.get(i);
                    System.out.println("aList2IN===" + aList2);
                    for (int j = 0; j < aList2.size(); j++) {
                        String PID = aList2.get(11).toString();
                        System.out.println("PID IN===" + PID + "prod_id ==" + prod_id);
                        if (PID.equals(prod_id)) {
                            principal2 = Double.parseDouble(aList2.get(4).toString());
                            interest2 = Double.parseDouble(aList2.get(6).toString());
                            penal2 = Double.parseDouble(aList2.get(5).toString());
                            charges2 = Double.parseDouble(aList2.get(8).toString());
                        }
                    }
                }
                System.out.println("principal2 IN===" + principal2 + "interest2 ==" + interest2);
                tblTransaction.setValueAt(new Double(principal2), selectRow, 5);
                tblTransaction.setValueAt(new Double(interest2), selectRow, 7);
                tblTransaction.setValueAt(new Double(penal2), selectRow, 6);
                tblTransaction.setValueAt(new Double(charges2), selectRow, 9);
                return;
            }
            double paidPrincipal = 0;
            double paidInterest = 0;
            double paidPenal = 0;
            double paidCharges = 0;
            if (pay != null || !pay.equals("") || CommonUtil.convertObjToDouble(pay) > 0.0) {
                System.out.println("LOAN scheme_Name=" + scheme_Name + " prod_id=" + prod_id + " acNo=" + acNo + " transAmt=" + transAmt);
//                for (int i = 0; i < globalList.size(); i++) {
//                    ArrayList aList1 = (ArrayList) globalList.get(i);
//                    for (int j = 0; j < aList1.size(); j++) {
//                        String PID = aList1.get(11).toString();
//                        if (PID.equals(prod_id) && selectRow == j) {
//                            principal = Double.parseDouble(aList1.get(4).toString());
//                            interest = Double.parseDouble(aList1.get(6).toString());
//                            penal = Double.parseDouble(aList1.get(5).toString());
//                            charges = Double.parseDouble(aList1.get(8).toString());
//                        }
//                    }
//                }
                double currInt = 0.0;
                HashMap hashList = new HashMap();
                hashList.put(CommonConstants.MAP_WHERE, prod_id);
                List appList = ClientUtil.executeQuery("selectAppropriatTransaction", hashList);
                HashMap appropriateMap = new HashMap();
                if (appList != null && appList.size() > 0) {
                    appropriateMap = (HashMap) appList.get(0);
                    appropriateMap.remove("PROD_ID");
                } else {
                    throw new TTException("Please Enter Hierachy of Transaction  in This Product ");
                }
                System.out.println("appropriateMap####" + appropriateMap);
                java.util.Collection collectedValues = appropriateMap.values();
                java.util.Iterator it = collectedValues.iterator();
                //CashTransactionTO objCashTO =new CashTransactionTO();
                int appTranValue = 0;
                while (it.hasNext()) {
                    appTranValue++;
                    String hierachyValue = CommonUtil.convertObjToStr(it.next());
                    System.out.println("hierachyValue####" + hierachyValue);
                    //objCashTO = setCashTransaction(objCashTransactionTO);
                    if (hierachyValue.equals("CHARGES")) {
                        if (transAmt > 0 && charges > 0) {
                            if (transAmt >= charges) {
                                transAmt -= charges;
                                paidCharges = charges;
                            } else {
                                paidCharges = transAmt;
                                transAmt -= charges;
                            }
                        } else {
                            paidCharges = 0;
                        }
                    }
                    if (hierachyValue.equals("PENALINTEREST")) {
                        //penal interest
                        if (transAmt > 0 && penal > 0) {
                            if (transAmt >= penal) {
                                transAmt -= penal;
                                paidPenal = penal;
                            } else {
                                paidPenal = transAmt;
                                transAmt -= penal;
                            }
                        } else {
                            paidPenal = 0;
                        }
                    }
                    if (hierachyValue.equals("INTEREST")) {
                        //interest
                        if (transAmt > 0 && interest > 0) {
                            if (transAmt >= interest) {
                                transAmt -= interest;
                                paidInterest = interest;
                            } else {
                                paidInterest = transAmt;
                                transAmt -= interest;
                            }
                        } else {
                            paidInterest = 0;
                        }
                    }
                    if (hierachyValue.equals("PRINCIPAL")) {
                        if (transAmt > 0 && principal > 0) {
                            if (transAmt >= principal) {
                                //transAmt -= principal;
                                paidPrincipal = transAmt;
                            } else {
                                paidPrincipal = transAmt;
                                transAmt -= principal;
                            }
                        } else {
                            paidPrincipal = transAmt;
                        }
                    }
                }
                currInt = paidInterest;
            }
            tblTransaction.setValueAt(new Double(paidPrincipal), tblTransaction.getSelectedRow(), 5);
            tblTransaction.setValueAt(new Double(paidInterest), selectRow, 7);
            tblTransaction.setValueAt(new Double(paidPenal), selectRow, 6);
            tblTransaction.setValueAt(new Double(paidCharges), selectRow, 9);
            System.out.println("paidPrincipal : " + paidPrincipal + "paidInterest : " + paidInterest + " paidPenal : " + paidPenal);
                                     //Check loan authorization pending
            //   HashMap pendingMap = new HashMap();
            //  pendingMap.put("ACT_NUM",act_num);
            //  pendingMap.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
            //   List pendingAuthlst=ClientUtil.executeQuery("checkPendingAuthTransaction", pendingMap);
            //  //System.out.println("######### pendingAuthlst4534543="+pendingAuthlst.size());
            // if(pendingAuthlst!=null && pendingAuthlst.size()>0){
            //  HashMap  CountMap = (HashMap)pendingAuthlst.get(0);
            //  int countM=CommonUtil.convertObjToInt(CountMap.get("COUNT"));
            //  //System.out.println("########countM"+countM);
            //  if(countM>0)
            //  {
            //      //System.out.println("#######################"+selectedRow);
            //      countM=0;
            //      pendingAuthlst=null;
            //   ClientUtil.showMessageWindow(" Transaction pending for this Account number... Please Authorize OR Reject first  !!! ");
            //   tblTransaction.setValueAt("", selectedRow, 4);
            //    tblTransaction.setValueAt(false, selectedRow, 0);
            //  tblTransaction.setF

            //   return;
            // }
            // }
//        }
//            //System.out.println("NOT IN ==" + tot);
//            if (column == 4) {
            //if(paying>tot)
            // {
//                prin = paying - intr;//tot;
//                //System.out.println("prin -valllllll IN ==" + prin);
//                if (prin > 0) {
//                    tblTransaction.setValueAt(prin, selectRow, 5);
//                } else {
//                    tblTransaction.setValueAt("0.0", selectRow, 6);
//                }
//            }
            if ((column == 0 || column == 4) && chk) {
                //Data pass to Dao
                HashMap dataMap = new HashMap();
                dataMap.put("CHARGES", String.valueOf(paidCharges));
                dataMap.put("PRINCIPAL", String.valueOf(paidPrincipal));
                dataMap.put("INTEREST", String.valueOf(CommonUtil.convertObjToStr(tblTransaction.getValueAt(selectRow, 7))));
                dataMap.put("PENAL", String.valueOf(paidPenal));
                double totalDemand = paidPrincipal + paidInterest + paidPenal + paidCharges;
                dataMap.put("TOTAL_DEMAND", new Double(totalDemand));
                dataMap.put("PROD_TYPE", prodType);
                dataMap.put("ACT_NUM", acNo);
                dataMap.put("RECOVERED_AMOUNT", pay);
                dataMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
                dataMap.put("USER_ID", TrueTransactMain.USER_ID);
                dataMap.put("PROD_ID", prod_id);
                dataMap.put("CLOCK_NO", txtClockNo.getText());
                dataMap.put("MEMBER_NO", txtMemberNo.getText());
                dataMap.put("CUST_NAME", txtName.getText());
                dataMap.put("TOT_PRIN", txtTotPrincipal.getText());
                dataMap.put("TOT_PENAL", txtTotPenel.getText());
                dataMap.put("TOT_INT", txtTotInterest.getText());
                dataMap.put("TOT_OTHERS", txtTotOthers.getText());
                dataMap.put("TOT_GRAND", txtGrandTotal.getText());
                dataMap.put("NOTICE_AMOUNT", new Double(0));
                dataMap.put("ARBITRATION_AMOUNT", new Double(0));
                dataMap.put("PROD_DESCRIPTION", description);
                dataMap.put("BONUS", new Double(0));
                dataMap.put("AMTORNOOFINST", pay);
                //System.out.println("CommonConstants.TOSTATUS_INSERT==" + CommonConstants.TOSTATUS_INSERT);
                dataMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);

                dataMap.put("TOTAL", pay);
                if (termLoanMap == null) {
                    termLoanMap = new HashMap();
                }
                //System.out.println("doubleval==" + pay);
                if (CommonUtil.convertObjToDouble(pay) > 0) {
                    termLoanMap = new HashMap();
                    termLoanMap.put("TL", dataMap);
                    if (chkRetired != null && chkRetired.isSelected()) {
                        transDetailsUI.setTransDetails("TL", ProxyParameters.BRANCH_ID, acNo);
                        termLoanMap.put("TOTAL_AMOUNT", transDetailsUI.getTermLoanCloseCharge());
                    }
                    //System.out.println("acNo==" + acNo + "termLoanMap =====" + termLoanMap);
                    finalMap.put(acNo, termLoanMap);
                }
                dataMap = null;
                termLoanMap = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getTDDetails(String ent0, String ent1, String ent2, String ent3, String ent4, String ent5, String ent6, String ent7, String ent8, String ent9, String ent10, String ent11, String ent12, String ent13) {
        String prod_id = "";
        String amt;
        System.out.println("ent0"+ent0+"ent1"+ent1+"ent2"+ent2+"ent3"+ent3+"ent4"+ent4+"ent5"+ent5+"ent6"+ent6+"ent7"+ent7+"ent8"+ent8+"ent9"+ent9+"ent10"+ent10+"ent11"+ent11+"ent12"+ent12+"ent13"+ent13);
        String pay = CommonUtil.convertObjToStr(ent3);
        String scheme_Name = CommonUtil.convertObjToStr(ent0);
        prod_id = CommonUtil.convertObjToStr(ent11);
        String prodDescription = CommonUtil.convertObjToStr(ent1);
        String acNo = CommonUtil.convertObjToStr(ent2);
        double instM = CommonUtil.convertObjToDouble(ent3).doubleValue();
        double principal = CommonUtil.convertObjToDouble(ent4).doubleValue();
        double interest = CommonUtil.convertObjToDouble(ent6).doubleValue();
        double penal = CommonUtil.convertObjToDouble(ent5).doubleValue();
        double charges = CommonUtil.convertObjToDouble(ent8).doubleValue();
        double totalInst = CommonUtil.convertObjToDouble(ent13).doubleValue();
        double prin_old = CommonUtil.convertObjToDouble(ent12).doubleValue();
        //System.out.println("ASIZEEE=====================" + globalList.size());
//        for (int i = 0; i < globalList.size(); i++) {
//            ArrayList aList1 = (ArrayList) globalList.get(i);
//            for (int j = 0; j < aList1.size(); j++) {
//                String PID = aList1.get(11).toString();
//                if (PID.equals(prod_id)) {
//                    //  if(aList.get(3)!=null)
//                    principal = Double.parseDouble(aList1.get(4).toString());
//                    //  if(aList.get(5)!=null)
//                    interest = Double.parseDouble(aList1.get(6).toString());
//                    //   if(aList.get(4)!=null)
//                    penal = Double.parseDouble(aList1.get(5).toString());
//                    //  if(aList.get(7)!=null)
//                    charges = Double.parseDouble(aList1.get(8).toString());
//                    // displayAlert("principal ="+principal +" interest="+interest+" penal="+penal);
//                }
//            }
//        }
        //System.out.println("prin_old ========" + prin_old);
        //System.out.println("instM ========" + instM);
        //System.out.println("principal ========" + principal);
        //System.out.println("chkRetired.isSelected() ========" + chkRetired.isSelected());
        if (pay != null || !pay.equals("") || instM != 0) {

        } else {
            double principal1 = 0;
            double interest1 = 0;
            double penal1 = 0;
            double charges1 = 0;
//            for (int i = 0; i < globalList.size(); i++) {
//                ArrayList aList1 = (ArrayList) globalList.get(i);
//                for (int j = 0; j < aList1.size(); j++) {
//                    String PID = aList1.get(11).toString();
//                    if (PID.equals(prod_id)) {
//                        principal1 = Double.parseDouble(aList1.get(4).toString());
//                        interest1 = Double.parseDouble(aList1.get(6).toString());
//                        penal1 = Double.parseDouble(aList1.get(5).toString());
//                        charges1 = Double.parseDouble(aList1.get(8).toString());
//                    }
//                }
//            }
            //System.out.println("principal1=====" + principal1);
            //  tblTransaction.setValueAt(principal1, tblTransaction.getSelectedRow(),5);
        }
        HashMap dataMap = new HashMap();
        dataMap.put("PROD_TYPE", "TD");
        dataMap.put("ACT_NUM", acNo);
        dataMap.put("RECOVERED_AMOUNT", principal + interest + penal);
        dataMap.put("PROD_ID", prod_id);
        dataMap.put("USER_ID", TrueTransactMain.BRANCH_ID);

        dataMap.put("PRINCIPAL", String.valueOf(principal));
        dataMap.put("PENAL", String.valueOf(penal));
        dataMap.put("TOTAL_DEMAND", principal + interest + penal);
        dataMap.put("DEPOSIT_PENAL_MONTH", String.valueOf("0"));
        dataMap.put("INTEREST", new Double(0));
        dataMap.put("CHARGES", new Double(0));
        dataMap.put("CLOCK_NO", txtClockNo.getText());
        dataMap.put("MEMBER_NO", txtMemberNo.getText());
        dataMap.put("CUST_NAME", txtName.getText());
        dataMap.put("TOT_PRIN", txtTotPrincipal.getText());
        dataMap.put("TOT_PENAL", txtTotPenel.getText());
        dataMap.put("TOT_INT", txtTotInterest.getText());
        dataMap.put("TOT_OTHERS", txtTotOthers.getText());
        dataMap.put("TOT_GRAND", txtGrandTotal.getText());
        dataMap.put("BONUS", new Double(0));
        dataMap.put("NOTICE_AMOUNT", new Double(0));
        dataMap.put("ARBITRATION_AMOUNT", new Double(0));
        dataMap.put("AMTORNOOFINST", instM);
        dataMap.put("TOTAL", instM);
        dataMap.put("PROD_DESCRIPTION", prodDescription);
        if (CommonUtil.convertObjToDouble(dataMap.get("TOTAL_DEMAND")).doubleValue() <= 0.0) {
            dataMap = null;
        }
        if (instM > 0) {
            termTdMap = new HashMap();
            termTdMap.put("TD", dataMap);
            finalMap.put(acNo, termTdMap);
        }
    }

    public void getSADetails(String ent0, String ent1, String ent2, String ent3, String ent4, String ent5, String ent6, String ent7, String ent8, String ent9, String ent10, String ent11, String ent12, String ent13) {
        String prod_id = "";
        String amt;
        boolean fl = false;
        String scheme_Name = CommonUtil.convertObjToStr(ent0);
        prod_id = CommonUtil.convertObjToStr(ent11);
        String acNo = CommonUtil.convertObjToStr(ent2);
        String prodDescription = CommonUtil.convertObjToStr(ent1);
        double instM = CommonUtil.convertObjToDouble(ent3).doubleValue();
        double principal = CommonUtil.convertObjToDouble(ent4).doubleValue();
        double interest = CommonUtil.convertObjToDouble(ent6).doubleValue();
        double penal = CommonUtil.convertObjToDouble(ent5).doubleValue();
        double charges = CommonUtil.convertObjToDouble(ent8).doubleValue();
        double totalInst = CommonUtil.convertObjToDouble(ent12).doubleValue();
        double interest1 = CommonUtil.convertObjToDouble(ent6).doubleValue();
        if (String.valueOf(instM) != null && !String.valueOf(instM).equals("")) {
            //System.out.println("ASIZEEE=====================" + interest);
        }
        //if (instM > totalInst) {
            // fl=true;
            //   ClientUtil.showAlertWindow("Exceeds the Total Amount !!! ");
            //  tblTransaction.setValueAt("", tblTransaction.getSelectedRow(),3);
            // tblTransaction.
            //return;
        //}
        //For Loop
        //   //System.out.println("ASIZEEE====================="+globalList.size());
        for (int i = 0; i < globalList.size(); i++) {
            ArrayList aList1 = (ArrayList) globalList.get(i);
            for (int j = 0; j < aList1.size(); j++) {
                String PID = aList1.get(11).toString();
                if (PID.equals(prod_id)) {
                    //  if(aList.get(3)!=null)
                    principal = Double.parseDouble(aList1.get(4).toString());
                    //  if(aList.get(5)!=null)
                    interest = Double.parseDouble(aList1.get(6).toString());
                    //   if(aList.get(4)!=null)
                    penal = Double.parseDouble(aList1.get(5).toString());
                    //  if(aList.get(7)!=null)
                    charges = Double.parseDouble(aList1.get(8).toString());
                    // displayAlert("principal ="+principal +" interest="+interest+" penal="+penal);
                }
            }
        }

        //  if(instM>totalInst)
        // {
        //     interst= instM-totalInst;
        //  }
        //               if(interest1>0)
        //               {
        totalInst = instM - interest1;
        // }
        //System.out.println("interest ==" + interest1 + " totalInst==" + totalInst + " instM==" + instM);
        //if (instM > 0) {
            //  tblTransaction.setValueAt(totalInst, tblTransaction.getSelectedRow(),4); //totalInst
            //tblTransaction.setValueAt(interst, tblTransaction.getSelectedRow(),6); //totalInst
        //}
        double interst = CommonUtil.convertObjToDouble(ent6).doubleValue();
        HashMap dataMap = new HashMap();
        dataMap.put("RECOVERED_AMOUNT", new Double(instM));//new Double(totalInst));
        dataMap.put("ACT_NUM", acNo);
        dataMap.put("PROD_ID", prod_id);
        dataMap.put("PROD_TYPE", "SA");
        dataMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
        dataMap.put("USER_ID", TrueTransactMain.USER_ID);

        dataMap.put("BONUS", new Double(0));
        dataMap.put("PRINCIPAL", totalInst);
        dataMap.put("TOTAL_DEMAND", new Double(instM));
        dataMap.put("INTEREST", interst);
        dataMap.put("PENAL", new Double(0));
        dataMap.put("CHARGES", new Double(0));
        dataMap.put("CLOCK_NO", txtClockNo.getText());
        dataMap.put("MEMBER_NO", txtMemberNo.getText());
        dataMap.put("CUST_NAME", txtName.getText());
        dataMap.put("TOT_PRIN", txtTotPrincipal.getText());
        dataMap.put("TOT_PENAL", txtTotPenel.getText());
        dataMap.put("TOT_INT", txtTotInterest.getText());
        dataMap.put("TOT_OTHERS", txtTotOthers.getText());
        dataMap.put("TOT_GRAND", txtGrandTotal.getText());
        dataMap.put("NOTICE_AMOUNT", new Double(0));
        dataMap.put("ARBITRATION_AMOUNT", new Double(0));
        dataMap.put("AMTORNOOFINST", instM);
        dataMap.put("PROD_DESCRIPTION", prodDescription);
        // //System.out.println("acNo====IN===="+acNo+"termSaMap=="+termSaMap);
        if (instM > 0) {
            termSaMap = new HashMap();
            termSaMap.put("SA", dataMap);
            //System.out.println("acNo====IN====" + acNo + "termSaMap==" + termSaMap);
            finalMap.put(acNo, termSaMap);
        } else {
            dataMap = null;
        }
    }

    public void getADDetails(String ent0, String ent1, String ent2, String ent3, String ent4, String ent5, String ent6, String ent7, String ent8, String ent9, String ent10, String ent11, String ent12, String ent13) {
        String pay = CommonUtil.convertObjToStr(ent3);
        String prodDescription = CommonUtil.convertObjToStr(ent1);
        String act_num = CommonUtil.convertObjToStr(ent2);
        String principal = CommonUtil.convertObjToStr(ent4);
        String interest = CommonUtil.convertObjToStr(ent6);
        String total = CommonUtil.convertObjToStr(ent13);
        String prod_id = CommonUtil.convertObjToStr(ent11);
        String penal = CommonUtil.convertObjToStr(ent5);
        String acNo = CommonUtil.convertObjToStr(ent2);
        String prodType = CommonUtil.convertObjToStr(ent0);
        double prin = CommonUtil.convertObjToDouble(principal);
        double paying = CommonUtil.convertObjToDouble(pay);
        double intr = CommonUtil.convertObjToDouble(interest);
        double tot = CommonUtil.convertObjToDouble(total);
        //System.out.println("paying==" + paying);
        //System.out.println("tot==" + tot);
        int selectedRow = tblTransaction.getSelectedRow();

        if (pay == null || pay.equals("") || CommonUtil.convertObjToDouble(pay) <= 0.0) {
            double principal2 = 0;
            double interest2 = 0;
            double penal2 = 0;
            double charges2 = 0;
            for (int i = 0; i < globalList.size(); i++) {
                ArrayList aList2 = (ArrayList) globalList.get(i);
                System.out.println("aList2IN===" + aList2);
                for (int j = 0; j < aList2.size(); j++) {
                    String PID = aList2.get(11).toString();
                    //System.out.println("PID IN===" + PID + "prod_id ==" + prod_id);
                    if (PID.equals(prod_id)) {
                        principal2 = Double.parseDouble(aList2.get(4).toString());
                        interest2 = Double.parseDouble(aList2.get(6).toString());
                        penal2 = Double.parseDouble(aList2.get(5).toString());
                        charges2 = Double.parseDouble(aList2.get(8).toString());
                    }
                }
            }
            //System.out.println("principal2 IN===" + principal2 + "interest2 ==" + interest2);
            //  tblTransaction.setValueAt(new Double(principal2), tblTransaction.getSelectedRow(),4);
            // tblTransaction.setValueAt(new Double(interest2), tblTransaction.getSelectedRow(),6);
            // tblTransaction.setValueAt(new Double(penal2), tblTransaction.getSelectedRow(),5);
            // tblTransaction.setValueAt(new Double(charges2), tblTransaction.getSelectedRow(),8);
            return;
        }
        //else
        //   {
        if (pay != null || !pay.equals("") || CommonUtil.convertObjToDouble(pay) > 0.0) {
            //Check loan authorization pending
            HashMap pendingMap = new HashMap();
            pendingMap.put("ACT_NUM", act_num);
            pendingMap.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
            List pendingAuthlst = ClientUtil.executeQuery("checkPendingAuthTransaction", pendingMap);
            //System.out.println("######### pendingAuthlst4534543=" + pendingAuthlst.size());
            if (pendingAuthlst != null && pendingAuthlst.size() > 0) {
                HashMap CountMap = (HashMap) pendingAuthlst.get(0);
                int countM = CommonUtil.convertObjToInt(CountMap.get("COUNT"));
                //System.out.println("########countM" + countM);
                if (countM > 0) {
                                           //   //System.out.println("#######################"+selectedRow);
                    //  countM=0;
                    //  pendingAuthlst=null;
                    //   ClientUtil.showMessageWindow(" Transaction pending for this Account number... Please Authorize OR Reject first  !!! ");
                    //  tblTransaction.setValueAt("", selectedRow, 3);
                    //  tblTransaction.setF

                    //   return;
                }
            }
        }
        //System.out.println("NOT IN ==" + tot);
        //Data pass to Dao
        HashMap dataMap = new HashMap();
        dataMap.put("CHARGES", "0");
        dataMap.put("PRINCIPAL", String.valueOf(principal));
        dataMap.put("INTEREST", String.valueOf(interest));
        dataMap.put("PENAL", penal);
        double totalDemand = Double.parseDouble(principal) + Double.parseDouble(interest)+ CommonUtil.convertObjToDouble(penal);
        dataMap.put("TOTAL_DEMAND", new Double(totalDemand));
        dataMap.put("PROD_TYPE", prodType);
        dataMap.put("ACT_NUM", acNo);
        dataMap.put("RECOVERED_AMOUNT", pay);
        dataMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
        dataMap.put("USER_ID", TrueTransactMain.USER_ID);
        dataMap.put("PROD_ID", prod_id);
        dataMap.put("CLOCK_NO", txtClockNo.getText());
        dataMap.put("MEMBER_NO", txtMemberNo.getText());
        dataMap.put("CUST_NAME", txtName.getText());
        dataMap.put("TOT_PRIN", txtTotPrincipal.getText());
        dataMap.put("TOT_PENAL", txtTotPenel.getText());
        dataMap.put("TOT_INT", txtTotInterest.getText());
        dataMap.put("TOT_OTHERS", txtTotOthers.getText());
        dataMap.put("TOT_GRAND", txtGrandTotal.getText());
        dataMap.put("NOTICE_AMOUNT", new Double(0));
        dataMap.put("ARBITRATION_AMOUNT", new Double(0));
        dataMap.put("BONUS", new Double(0));
        dataMap.put("AMTORNOOFINST", pay);
        //System.out.println("CommonConstants.TOSTATUS_INSERT==" + CommonConstants.TOSTATUS_INSERT);
        dataMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
        dataMap.put("PROD_DESCRIPTION", prodDescription);
        dataMap.put("TOTAL", pay);
        if (termLoanMap == null) {
            termLoanMap = new HashMap();
        }
        //System.out.println("doubleval==" + pay);
        if (CommonUtil.convertObjToDouble(pay) > 0) {
            termLoanMap = new HashMap();
            termLoanMap.put("TL", dataMap);

            if (chkRetired != null && chkRetired.isSelected()) {
                transDetailsUI.setTransDetails("TL", ProxyParameters.BRANCH_ID, acNo);
                termLoanMap.put("TOTAL_AMOUNT", transDetailsUI.getTermLoanCloseCharge());
            }
            //System.out.println("acNo==" + acNo + "termADMap =ddd====" + termLoanMap);
            finalMap.put(acNo, termLoanMap);
        }

        dataMap = null;
        termLoanMap = null;

    }

    public void setChangesInterest() {
        try {
            String pay = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 4));
            String act_num = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 3));
            String principal = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 5));
            String interest = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 7));
            String total = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 14));
            String prod_id = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 12));
            String acNo = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 3));
            String prodType = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 1));
            double prin = CommonUtil.convertObjToDouble(principal);
            double paying = CommonUtil.convertObjToDouble(pay);
            double intr = CommonUtil.convertObjToDouble(interest);
            double tot = CommonUtil.convertObjToDouble(total);
            //vcal 0
            //System.out.println("paying==" + paying);
            //System.out.println("tot==" + tot);
            int selectedRow = tblTransaction.getSelectedRow();
            if (intr > paying) {
                // prin=paying-tot;//intr;
                //  //System.out.println("prin -valllllll IN =="+prin);
                tblTransaction.setValueAt(intr, tblTransaction.getSelectedRow(), 4);
                tblTransaction.setValueAt(0, tblTransaction.getSelectedRow(), 5);
            }
            if (intr <= paying) {
                tblTransaction.setValueAt(intr, tblTransaction.getSelectedRow(), 4);
                tblTransaction.setValueAt(0, tblTransaction.getSelectedRow(), 5);
            }
        } catch (Exception e) {
            //System.out.println("Exp:=" + e);
        }
    }

    private void calcEachChittal(int column, boolean chk, String selectAll, int selectRow) {
        flag = true;
        try {
            //System.out.println("in ISTTTTTTT");
            String chittalNo = "", subNo = "";
            String calculateIntOn = "";
            String prId = CommonUtil.convertObjToStr(tblTransaction.getValueAt(selectRow, 12));
            chittalNo = CommonUtil.convertObjToStr(tblTransaction.getValueAt(selectRow, 3));
            String description = CommonUtil.convertObjToStr(tblTransaction.getValueAt(selectRow, 2));
            HashMap dataMap = new HashMap();
            dataMap.put("ACT_NUM", chittalNo);
            if (chittalNo.indexOf("_") != -1) {
                subNo = chittalNo.substring(chittalNo.indexOf("_") + 1, chittalNo.length());
                chittalNo = chittalNo.substring(0, chittalNo.indexOf("_"));
            } else {
                subNo = "1";
            }
            String noOfInsPay = CommonUtil.convertObjToStr(tblTransaction.getValueAt(selectRow, 4));
            String prod_id = CommonUtil.convertObjToStr(tblTransaction.getValueAt(selectRow, 14));
            double pendDueIn = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 13));
            double currIn = CommonUtil.convertObjToDouble(noOfInsPay).doubleValue();
            double remainInst = pendDueIn - currIn;
            //System.out.println("remainInst ====================== " + remainInst);
            if (remainInst < 0) {
                remainInst = 0;
            }
            //For Loop
            int selectedRow = selectRow;
            double principal1 = 0;
            double interest1 = 0;
            double penal1 = 0;
            double charges1 = 0;
            double bonus1 = 0;
            for (int i = 0; i < globalList.size(); i++) {
                ArrayList aList1 = (ArrayList) globalList.get(i);
                for (int j = 0; j < aList1.size(); j++) {
                    String PID = aList1.get(11).toString();
                    if (PID.equals(prod_id)) {
                        //  if(aList.get(3)!=null)
                        principal1 = CommonUtil.convertObjToDouble(aList1.get(4).toString()).doubleValue();
                        //  if(aList.get(5)!=null)
                        interest1 = CommonUtil.convertObjToDouble(aList1.get(6).toString()).doubleValue();
                        //   if(aList.get(4)!=null)
                        penal1 = CommonUtil.convertObjToDouble(aList1.get(5).toString()).doubleValue();
                        //  if(aList.get(7)!=null)
                        charges1 = CommonUtil.convertObjToDouble(aList1.get(8).toString()).doubleValue();
                        bonus1 = CommonUtil.convertObjToDouble(aList1.get(7).toString()).doubleValue();
//                      displayAlert("principal ="+principal1 +" interest="+interest+" penal="+penal1);
                    }
                }
            }
            //System.out.println("#####noOfInsPayst=" + noOfInsPay);
            if (noOfInsPay == null || noOfInsPay.equals("")) {
                //System.out.println("#####I(*&&^%%%$#$$#@##@@22222222222222222=" + noOfInsPay);
                tblTransaction.setValueAt(principal1, selectedRow, 5);
                tblTransaction.setValueAt(penal1, selectedRow, 6);//penalAmount
                tblTransaction.setValueAt(interest1, selectedRow, 7);//instAmount
                tblTransaction.setValueAt(bonus1, selectedRow, 8);
                return;
            }
            //End

            String noticAmt = CommonUtil.convertObjToStr(tblTransaction.getValueAt(selectRow, 10));
            String arbitAmt = CommonUtil.convertObjToStr(tblTransaction.getValueAt(selectRow, 11));
            // String insDue = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(),3));

            //String inter = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(),6));
            // String pene = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(),5));
            String insDue = "";

            HashMap pendingMap = new HashMap();
            pendingMap.put("SCHEME_NAME", prId);
            pendingMap.put("CHITTAL_NO", chittalNo);
            //System.out.println("######### subNo" + subNo);
            pendingMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
            //            //System.out.println("######### pendingMap"+pendingMap);
            List pendingAuthlst = ClientUtil.executeQuery("checkPendingForAuthorization", pendingMap);
            //System.out.println("######### pendingAuthlst=" + pendingAuthlst.size());
            //System.out.println("######### noOfInsPay=" + noOfInsPay);
            if (pendingAuthlst != null && pendingAuthlst.size() > 0) {
                //  ClientUtil.showMessageWindow(" Transaction pending for this Chittal... Please Authorize OR Reject first  !!! ");
                // tblTransaction.setValueAt("", selectedRow, 4);
                // tblTransaction.setValueAt(false, selectedRow, 0);
            } else if (CommonUtil.convertObjToDouble(noOfInsPay).doubleValue() >= 1) {
                //System.out.println("######### noOfInsPay IN=" + noOfInsPay);
                HashMap whereMap = new HashMap();

                HashMap productMap = new HashMap();

                long diffDayPending = 0;
                int noOfInsPaid = 0;
                Date currDate = (Date) curr_dt.clone();
                Date instDate = null;
                boolean bonusAvailabe = true;
                long noOfInstPay = CommonUtil.convertObjToLong(noOfInsPay);
                int instDay = 1;
                int totIns = 0;
                Date startDate = null;
                Date endDate = null;
                Date insDate = null;
                int startMonth = 0;
                int insMonth = 0;
                int curInsNo = 0;
                HashMap installmentMap = new HashMap();
                whereMap.put("SCHEME_NAME", prId);
                List lst = ClientUtil.executeQuery("getSelectSchemeAcctHead", whereMap);
                //System.out.println("#######lst =ooo==" + lst.size() + " LST----" + lst);
                if (lst != null && lst.size() > 0) {
                    productMap = (HashMap) lst.get(0);
                    totIns = CommonUtil.convertObjToInt(productMap.get("NO_OF_INSTALLMENTS"));
                    startDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(productMap.get("SCHEME_START_DT")));
                    endDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(productMap.get("SCHEME_END_DT")));
                    insDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(productMap.get("SCHEME_START_DT")));
                    startMonth = insDate.getMonth();
                    // Added by Rajesh For checking BONUS_FIRST_INSTALLMENT. Based on this for loop initial value will be changed for Penal calculation.
                    //                    String bonusFirstInst = CommonUtil.convertObjToStr(productMap.get("BONUS_FIRST_INSTALLMENT"));
                    String bonusFirstInst = CommonUtil.convertObjToStr(productMap.get("ADVANCE_COLLECTION"));
                    int startNoForPenal = 0;
                    int addNo = 1;
                    int firstInst_No = -1;
                    if (bonusFirstInst.equals("Y")) {
                        startNoForPenal = 1;
                        addNo = 0;
                        firstInst_No = 0;
                    }
                    bonusAvailabe = true;
                    double insAmt = 0.0;
                    //                    double insAmt = CommonUtil.convertObjToDouble(productMap.get("INSTALLMENT_AMOUNT")).doubleValue();
                    long pendingInst = 0;
                    int divNo = 0;
                    long count = 0;
                    long insDueAmt = 0;
                    whereMap.put("CHITTAL_NO", chittalNo);
                    whereMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
                    List insList = ClientUtil.executeQuery("getNoOfInstalmentsPaid", whereMap);
                    //System.out.println("#######insList ===" + insList.size() + "insList ===" + insList);
                    if (insList != null && insList.size() > 0) {
                        whereMap = (HashMap) insList.get(0);
                        noOfInsPaid = CommonUtil.convertObjToInt(whereMap.get("NO_OF_INST"));
                        count = CommonUtil.convertObjToLong(whereMap.get("NO_OF_INST"));
                    }

                    HashMap chittalMap = new HashMap();
                    chittalMap.put("CHITTAL_NO", chittalNo);
                    chittalMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
                    List chitLst = ClientUtil.executeQuery("getSelctApplnReceiptDetails", chittalMap);
                    //System.out.println("#######chitLst ===" + chitLst.size() + "chitLst ===" + chitLst);
                    if (chitLst != null && chitLst.size() > 0) {
                        chittalMap = (HashMap) chitLst.get(0);
                        //System.out.println("#######chittalMap ===" + chittalMap);
                        instDay = CommonUtil.convertObjToInt(chittalMap.get("INSTALLMENT_DAY"));
                        divNo = CommonUtil.convertObjToInt(chittalMap.get("DIVISION_NO"));
                        dataMap.put("BRANCH_CODE", CommonUtil.convertObjToStr(chittalMap.get("BRANCH_CODE")));
                        insAmt = CommonUtil.convertObjToDouble(chittalMap.get("INST_AMT")).doubleValue();
                    }

                    HashMap insDateMap = new HashMap();
                    insDateMap.put("DIVISION_NO", String.valueOf(divNo));
                    insDateMap.put("SCHEME_NAME", prId);
                    insDateMap.put("CURR_DATE", curr_dt.clone());
                    //                    insDateMap.put("ADD_MONTHS", "0");
                    insDateMap.put("ADD_MONTHS", "-1");
                    List insDateLst = ClientUtil.executeQuery("getTransAllMDSCurrentInsDate", insDateMap);
                    System.out.println("#######insDateLst ===" + insDateLst.size() + "insDateLst ===" + insDateLst);
                    if (insDateLst != null && insDateLst.size() > 0) {
                        insDateMap = (HashMap) insDateLst.get(0);
                        //System.out.println("#######insDateMap ===" + insDateMap);
                        curInsNo = CommonUtil.convertObjToInt(insDateMap.get("INSTALLMENT_NO"));
                        pendingInst = curInsNo - noOfInsPaid;
                        if (instDay < currDate.getDate()) {
                            pendingInst = pendingInst + 1;
                        } else {
                            pendingInst = pendingInst;
                        }
                        if (pendingInst < 0) {
                            pendingInst = 0;
                        }
                        insMonth = startMonth + curInsNo;
                        insDate.setMonth(insMonth);
                    }

                    HashMap prizedMap = new HashMap();
                    double bonusAval = 0;
                    prizedMap.put("SCHEME_NAME", prId);
                    prizedMap.put("DIVISION_NO", String.valueOf(divNo));
                    prizedMap.put("CHITTAL_NO", chittalNo);
                    prizedMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
                    lst = ClientUtil.executeQuery("getSelectPrizedDetailsEntryRecords", prizedMap);
                    //System.out.println("#######prizedMap ===" + lst.size() + "prizedMap ===" + lst);
                    if (lst != null && lst.size() > 0) {
                        prizedMap = (HashMap) lst.get(0);
                        if (prizedMap.get("DRAW") != null && !prizedMap.get("DRAW").equals("") && prizedMap.get("DRAW").equals("Y")) {
                            setRdoPrizedMember_Yes(true);
                        }
                        if (prizedMap.get("AUCTION") != null && !prizedMap.get("AUCTION").equals("") && prizedMap.get("AUCTION").equals("Y")) {
                            setRdoPrizedMember_Yes(true);
                        }
                        bonusAval = CommonUtil.convertObjToDouble(prizedMap.get("PRIZED_AMOUNT"));
                    } else {
                        setRdoPrizedMember_No(true);
                    }
                    //System.out.println("#######totIns 1===" + totIns + " noOfInsPaid==" + noOfInsPaid);
                    int balanceIns = totIns - noOfInsPaid;
                    if (balanceIns >= noOfInstPay) {
                        long totDiscAmt = 0;
                        long penalAmt = 0;
                        double netAmt = 0;
                        double insAmtPayable = 0;
                        double totBonusAmt = 0;
                        double bonusAmt = 0;
                        String penalIntType = "";
                        long penalValue = 0;
                        String penalGraceType = "";
                        long penalGraceValue = 0;
                        String penalCalcBaseOn = "";
                        if (pendingInst > 0) {              //pending installment calculation starts...
                            insDueAmt = (long) insAmt * pendingInst;
                            int totPendingInst = (int) pendingInst;
                            double calc = 0;
                            long totInst = pendingInst;
                            penalCalcBaseOn = CommonUtil.convertObjToStr(productMap.get("PENAL_CALC"));
                            System.out.println("#######penalCalcBaseOn ===" + penalCalcBaseOn +"calculateIntOn :"+calculateIntOn);
                            if (getRdoPrizedMember_Yes() == true) {
                                if (productMap.containsKey("PENEL_PZ_INT_FULL_AMT_INST_AMT") && productMap.get("PENEL_PZ_INT_FULL_AMT_INST_AMT") != null) {
                                    calculateIntOn = CommonUtil.convertObjToStr(productMap.get("PENEL_PZ_INT_FULL_AMT_INST_AMT"));
                                }
                                penalIntType = CommonUtil.convertObjToStr(productMap.get("PENAL_PRIZED_INT_TYPE"));
                                penalValue = CommonUtil.convertObjToLong(productMap.get("PENAL_PRIZED_INT_AMT"));
                                penalGraceType = CommonUtil.convertObjToStr(productMap.get("PENAL_PRIZED_GRACE_PERIOD_TYPE"));
                                penalGraceValue = CommonUtil.convertObjToLong(productMap.get("PENAL_PRIZED_GRACE_PERIOD"));
                            } else if (getRdoPrizedMember_No() == true) {
                                if (productMap.containsKey("PENEL_INT_FULL_AMT_INST_AMT") && productMap.get("PENEL_INT_FULL_AMT_INST_AMT") != null) {
                                    calculateIntOn = CommonUtil.convertObjToStr(productMap.get("PENEL_INT_FULL_AMT_INST_AMT"));
                                }
                                penalIntType = CommonUtil.convertObjToStr(productMap.get("PENAL_INT_TYPE"));
                                penalValue = CommonUtil.convertObjToLong(productMap.get("PENAL_INT_AMT"));
                                penalGraceType = CommonUtil.convertObjToStr(productMap.get("PENAL_GRACE_PERIOD_TYPE"));
                                penalGraceValue = CommonUtil.convertObjToLong(productMap.get("PENAL_GRACE_PERIOD"));
                            }
                            List bonusAmout = new ArrayList();
                            System.out.println("calculateIntOn"+calculateIntOn+" inst amt :"+productMap.get("INSTALLMENT_AMOUNT"));
                            if (calculateIntOn != null && calculateIntOn.equals("Installment Amount")) {
                                //double instAmount = 0.0;
                                HashMap nextInstMaps = null;
                                for (int i = startNoForPenal; i <= noOfInstPay - addNo; i++) {
                                    nextInstMaps = new HashMap();
                                    nextInstMaps.put("SCHEME_NAME", prId);
                                    nextInstMaps.put("DIVISION_NO", divNo);
                                    nextInstMaps.put("SL_NO", new Double(i + noOfInsPaid + addNo + firstInst_No));
                                    List listRec = ClientUtil.executeQuery("getSelectBonusAndNextInstDateWithoutDivision", nextInstMaps);
                                    if (listRec != null && listRec.size() > 0) {
                                        nextInstMaps = (HashMap) listRec.get(0);
                                    }
                                    System.out.println("nextInstMaps"+nextInstMaps);
                                    if (nextInstMaps != null && nextInstMaps.containsKey("NEXT_BONUS_AMOUNT")) {
                                        bonusAmout.add(CommonUtil.convertObjToDouble(nextInstMaps.get("NEXT_BONUS_AMOUNT")));
                                    } else {
                                        bonusAmout.add(CommonUtil.convertObjToDouble(0));
                                    }
                                }
                            }
                            for (int j = startNoForPenal; j < noOfInstPay + startNoForPenal; j++) {
                                if (calculateIntOn != null && calculateIntOn.equals("Installment Amount")) {
                                    insAmt = 0.0;
                                    double instAmount = CommonUtil.convertObjToDouble(productMap.get("INSTALLMENT_AMOUNT"));
                                    if (bonusAmout != null && bonusAmout.size() > 0) {
                                        instAmount -= CommonUtil.convertObjToDouble(bonusAmout.get(j - 1));
                                    }
                                    insAmt = instAmount;
                                }
                                HashMap nextInstMap = new HashMap();
                                nextInstMap.put("SCHEME_NAME", prId);
                                nextInstMap.put("DIVISION_NO", String.valueOf(divNo));
                                nextInstMap.put("SL_NO", new Double(j + noOfInsPaid));
                                List listRec = ClientUtil.executeQuery("getSelectNextInstDate", nextInstMap);
                                if (listRec != null && listRec.size() > 0) {
                                    double penal = 0;
                                    nextInstMap = (HashMap) listRec.get(0);
                                    instDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextInstMap.get("NEXT_INSTALLMENT_DATE")));
                                    if (instDay > 0) {
                                        instDate.setDate(instDate.getDate() + instDay - 1);
                                    }
                                    diffDayPending = DateUtil.dateDiff(instDate, currDate);
                                    //Holiday Checking - Added By Suresh
                                    HashMap holidayMap = new HashMap();
                                    boolean checkHoliday = true;
                                    //System.out.println("instDate   " + instDate);
                                    instDate = setProperDtFormat(instDate);
                                    //System.out.println("instDate   " + instDate);
                                    holidayMap.put("NEXT_DATE", instDate);
                                    holidayMap.put("BRANCH_CODE", getSelectedBranchID());
                                    while (checkHoliday) {
                                        boolean tholiday = false;
                                        List Holiday = ClientUtil.executeQuery("checkHolidayDateOD", holidayMap);
                                        List weeklyOf = ClientUtil.executeQuery("checkWeeklyOffOD", holidayMap);
                                        boolean isHoliday = Holiday.size() > 0 ? true : false;
                                        boolean isWeekOff = weeklyOf.size() > 0 ? true : false;
                                        if (isHoliday || isWeekOff) {
                                            //System.out.println("#### diffDayPending Holiday True : " + diffDayPending);
                                            if (CommonUtil.convertObjToStr(productMap.get("HOLIDAY_INT")).equals("any next working day")) {
                                                diffDayPending -= 1;
                                                instDate.setDate(instDate.getDate() + 1);
                                            } else {
                                                diffDayPending += 1;
                                                instDate.setDate(instDate.getDate() - 1);
                                            }
                                            holidayMap.put("NEXT_DATE", instDate);
                                            checkHoliday = true;
                                            //System.out.println("#### holidayMap : " + holidayMap);
                                        } else {
                                            //System.out.println("#### diffDay Holiday False : " + diffDayPending);
                                            checkHoliday = false;
                                        }
                                    }
                                    //System.out.println("#### diffDayPending Final : " + diffDayPending);
                                    if (penalCalcBaseOn != null && !penalCalcBaseOn.equals("") && penalCalcBaseOn.equals("Days")) {
                                        if (penalGraceType != null && !penalGraceType.equals("") && penalGraceType.equals("Days")) {
                                            if (diffDayPending > penalGraceValue) {
                                                if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Percent")) {
                                                    calc += (diffDayPending * penalValue * insAmt) / 36500;
                                                } else if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Absolute")) {
                                                    calc += penalValue;
                                                }
                                            }
                                        } else if (penalGraceType != null && !penalGraceType.equals("") && penalGraceType.equals("Installments")) {
                                            // To be written
                                            if (diffDayPending > penalGraceValue) {
                                                if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Percent")) {
                                                    //                                                calc = insAmt*(((pendingInst+1)*pendingInst/2)*penalValue)/1200;
                                                    calc += (double) ((insAmt * penalValue) / 1200.0) * pendingInst--;
                                                } else if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Absolute")) {
                                                    calc += penalValue;
                                                }
                                            }
                                        }
                                    } else if (penalCalcBaseOn != null && !penalCalcBaseOn.equals("") && penalCalcBaseOn.equals("Installments")) {
                                        if (penalGraceType != null && !penalGraceType.equals("") && penalGraceType.equals("Days")) {
                                            if (diffDayPending > penalGraceValue) {
                                                if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Percent")) {
                                                    calc += ((insAmt * penalValue) / 1200.0) * pendingInst--;
                                                } else if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Absolute") && totInst <= noOfInstPay) {
                                                    calc += penalValue;
                                                }
                                            }
                                        } else if (penalGraceType != null && !penalGraceType.equals("") && penalGraceType.equals("Installments")) {
                                            // To be written
                                            if (diffDayPending > penalGraceValue) {
                                                if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Percent")) {
                                                    //                                                calc = insAmt*(((pendingInst+1)*pendingInst/2)*penalValue)/1200;
                                                    calc += (double) ((insAmt * penalValue) / 1200.0) * pendingInst--;
                                                } else if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Absolute")) {
                                                    calc += penalValue;
                                                }
                                            }
                                        }
                                    }
                                    penal = (calc + 0.5) - penal;
                                    nextInstMap.put("PENAL", String.valueOf(penal));
                                    installmentMap.put(String.valueOf(j + noOfInsPaid + addNo), nextInstMap);
                                    penal = calc + 0.5;
                                }
                            }
                            if (calc > 0) {
                                penalAmt = (long) (calc + 0.5);

                            }
                        }//pending installment calculation ends...

                        //Discount calculation details Starts...
                        for (int k = 0; k < noOfInstPay; k++) {
                            HashMap nextInstMap = new HashMap();
                            nextInstMap.put("SCHEME_NAME", prId);
                            nextInstMap.put("DIVISION_NO", String.valueOf(divNo));
                            nextInstMap.put("SL_NO", new Double(k + noOfInsPaid));
                            List listRec = ClientUtil.executeQuery("getSelectNextInstDate", nextInstMap);
                            if (listRec == null || listRec.size() == 0) {
                                Date curDate = (Date) curr_dt.clone();
                                int curMonth = curDate.getMonth();
                                curDate.setMonth(curMonth + 1);
                                curDate.setDate(instDay);
                                listRec = new ArrayList();
                                nextInstMap.put("NEXT_INSTALLMENT_DATE", curDate);
                                listRec.add(nextInstMap);
                            }
                            if (listRec != null && listRec.size() > 0) {
                                long discountAmt = 0;
                                nextInstMap = (HashMap) listRec.get(0);
                                instDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextInstMap.get("NEXT_INSTALLMENT_DATE")));
                                long diffDay = DateUtil.dateDiff(instDate, currDate);
                                if (productMap.get("BONUS_ALLOWED") != null && !productMap.get("BONUS_ALLOWED").equals("") && (productMap.get("BONUS_ALLOWED").equals("Y")
                                        || productMap.get("BONUS_ALLOWED").equals("N"))) {
                                    String discount = CommonUtil.convertObjToStr(productMap.get("DISCOUNT_ALLOWED"));
                                    if (discount != null && !discount.equals("") && discount.equals("Y")) {
                                        String discountType = CommonUtil.convertObjToStr(productMap.get("DISCOUNT_RATE_TYPE"));
                                        long discountValue = CommonUtil.convertObjToLong(productMap.get("DISCOUNT_RATE_AMT"));
                                        if (getRdoPrizedMember_Yes() == true) {//discount calculation for prized prerson...
                                            String discountPrizedDays = CommonUtil.convertObjToStr(productMap.get("DIS_PRIZED_GRACE_PERIOD_DAYS"));
                                            String discountPrizedMonth = CommonUtil.convertObjToStr(productMap.get("DIS_PRIZED_GRACE_PERIOD_MONTHS"));
                                            String discountPrizedAfter = CommonUtil.convertObjToStr(productMap.get("DIS_PRIZED_GRACE_PERIOD_AFTER"));
                                            String discountPrizedEnd = CommonUtil.convertObjToStr(productMap.get("DIS_PRIZED_GRACE_PERIOD_END"));
                                            long discountPrizedValue = CommonUtil.convertObjToLong(productMap.get("DIS_PRIZED_GRACE_PERIOD"));
                                            if (discountPrizedDays != null && !discountPrizedDays.equals("") && discountPrizedDays.equals("D") && diffDay <= discountPrizedValue) {
                                                if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                                    long calc = discountValue * (long) insAmt / 100;
                                                    if (diffDay <= discountPrizedValue) {
                                                        totDiscAmt = totDiscAmt + calc;
                                                    }
                                                } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                                    if (diffDay <= discountPrizedValue) {
                                                        totDiscAmt = totDiscAmt + discountValue;
                                                    }
                                                }
                                            } else if (discountPrizedMonth != null && !discountPrizedMonth.equals("") && discountPrizedMonth.equals("M") && diffDay <= (discountPrizedValue * 30)) {
                                                if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                                    long calc = discountValue * (long) insAmt / 100;
                                                    totDiscAmt = totDiscAmt + calc;
                                                } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                                    totDiscAmt = totDiscAmt + discountValue;
                                                }
                                            } else if (discountPrizedAfter != null && !discountPrizedAfter.equals("") && discountPrizedAfter.equals("A") && currDate.getDate() <= discountPrizedValue) {
                                                if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                                    long calc = discountValue * (long) insAmt / 100;
                                                    totDiscAmt = totDiscAmt + calc;
                                                } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                                    totDiscAmt = totDiscAmt + discountValue;
                                                }
                                            } else if (discountPrizedEnd != null && !discountPrizedEnd.equals("") && discountPrizedEnd.equals("E") && pendingInst < noOfInstPay) {
                                                if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                                    long calc = discountValue * (long) insAmt / 100;
                                                    totDiscAmt = totDiscAmt + calc;
                                                } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                                    totDiscAmt = totDiscAmt + discountValue;
                                                }
                                            } else {
                                                totDiscAmt = 0;
                                            }
                                        } else if (getRdoPrizedMember_No() == true) {//discount calculation non prized person...
                                            String discountGraceDays = CommonUtil.convertObjToStr(productMap.get("DIS_GRACE_PERIOD_DAYS"));
                                            String discountGraceMonth = CommonUtil.convertObjToStr(productMap.get("DIS_GRACE_PERIOD_MONTHS"));
                                            String discountGraceAfter = CommonUtil.convertObjToStr(productMap.get("DIS_GRACE_PERIOD_AFTER"));
                                            String discountGraceEnd = CommonUtil.convertObjToStr(productMap.get("DIS_GRACE_PERIOD_END"));
                                            long discountGraceValue = CommonUtil.convertObjToLong(productMap.get("DIS_GRACE_PERIOD"));
                                            if (discountGraceDays != null && !discountGraceDays.equals("") && discountGraceDays.equals("D")) {
                                                if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                                    long calc = discountValue * (long) insAmt / 100;
                                                    if (diffDay <= discountGraceValue) {
                                                        totDiscAmt = totDiscAmt + calc;
                                                    }
                                                } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                                    if (diffDay <= discountGraceValue) {
                                                        totDiscAmt = totDiscAmt + discountValue;
                                                    }
                                                } else {
                                                    totDiscAmt = 0;
                                                }
                                            } else if (discountGraceDays != null && !discountGraceDays.equals("") && discountGraceDays.equals("M") && diffDay <= discountGraceValue * 30 && pendingInst < noOfInstPay) {
                                                if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                                    long calc = discountValue * (long) insAmt / 100;
                                                    totDiscAmt = totDiscAmt + calc;
                                                } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                                    totDiscAmt = totDiscAmt + discountValue;
                                                }
                                            } else if (discountGraceDays != null && !discountGraceDays.equals("") && discountGraceDays.equals("A") && currDate.getDate() <= discountGraceValue && pendingInst < noOfInstPay) {
                                                if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                                    long calc = discountValue * (long) insAmt / 100;
                                                    totDiscAmt = totDiscAmt + calc;
                                                } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                                    totDiscAmt = totDiscAmt + discountValue;
                                                }
                                            } else if (discountGraceDays != null && !discountGraceDays.equals("") && discountGraceDays.equals("E") && pendingInst < noOfInstPay) {
                                                if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                                    long calc = discountValue * (long) insAmt / 100;
                                                    totDiscAmt = totDiscAmt + calc;
                                                } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                                    totDiscAmt = totDiscAmt + discountValue;
                                                }
                                            } else {
                                                totDiscAmt = 0;
                                            }
                                        }
                                    } else if (discount != null && !discount.equals("") && discount.equals("N")) {
                                        totDiscAmt = 0;
                                    }
                                    discountAmt = totDiscAmt - discountAmt;
                                    HashMap instMap = new HashMap();
                                    if (installmentMap.containsKey(String.valueOf(k + noOfInsPaid + 1))) {
                                        instMap = (HashMap) installmentMap.get(String.valueOf(k + noOfInsPaid + 1));
                                        instMap.put("DISCOUNT", String.valueOf(discountAmt));
                                        installmentMap.put(String.valueOf(k + noOfInsPaid + 1), instMap);
                                    }
                                    discountAmt = totDiscAmt;
                                }

                            }
                        }

                        //Bonus calculation details Starts...
                        for (int l = startNoForPenal; l <= noOfInstPay - addNo; l++) {
                            //                    for(int l = 1;l<=noOfInstPay;l++){
                            HashMap nextInstMap = new HashMap();
                            nextInstMap.put("SCHEME_NAME", prId);
                            nextInstMap.put("DIVISION_NO", divNo);
                            nextInstMap.put("SL_NO", new Double(l + noOfInsPaid + addNo + firstInst_No));
                            List listRec = ClientUtil.executeQuery("getSelectBonusAndNextInstDateWithoutDivision", nextInstMap);
                            if (listRec == null || listRec.size() == 0) {
                                Date curDate = (Date) curr_dt.clone();
                                int curMonth = curDate.getMonth();
                                curDate.setMonth(curMonth + 1);
                                curDate.setDate(instDay);
                                listRec = new ArrayList();
                                nextInstMap.put("NEXT_INSTALLMENT_DATE", curDate);
                                listRec.add(nextInstMap);
                                bonusAvailabe = false;
                            }
                            if (listRec != null && listRec.size() > 0) {
                                nextInstMap = (HashMap) listRec.get(0);
                                instDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextInstMap.get("NEXT_INSTALLMENT_DATE")));
                                bonusAmt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(nextInstMap.get("NEXT_BONUS_AMOUNT"))).doubleValue();
                                if (!productMap.get("MULTIPLE_MEMBER").equals("") && (productMap.get("MULTIPLE_MEMBER").equals("Y"))) {
                                    whereMap = new HashMap();
                                    int noOfCoChittal = 0;
                                    whereMap.put("SCHEME_NAME", prId);
                                    whereMap.put("CHITTAL_NUMBER", chittalNo);
                                    List applicationLst = ClientUtil.executeQuery("getSelectChitNoNotinMasterDetails", whereMap); // Count No Of Co-Chittals
                                    if (applicationLst != null && applicationLst.size() > 0) {
                                        noOfCoChittal = applicationLst.size();
                                        bonusAmt = bonusAmt / noOfCoChittal;
                                    }
                                }
                                if (productMap.get("BONUS_ROUNDING") != null && !productMap.get("BONUS_ROUNDING").equals("") && productMap.get("BONUS_ROUNDING").equals("Y")
                                        && bonusAmt > 0) {
                                    Rounding rod = new Rounding();
                                    bonusAmt = (double) rod.getNearest((long) (bonusAmt * 100), 100) / 100;
                                }
                                long diffDay = DateUtil.dateDiff(instDate, currDate);
                                //                            String hoildayInt = CommonUtil.convertObjToStr(productMap.get("HOLIDAY_INT"));
                                if (productMap.get("BONUS_ALLOWED") != null && !productMap.get("BONUS_ALLOWED").equals("") && (productMap.get("BONUS_ALLOWED").equals("Y"))) {
                                    if (bonusAvailabe == true) {
                                        if (getRdoPrizedMember_Yes() == true) {
                                            String bonusPrizedDays = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_DAYS"));
                                            String bonusPrizedMonth = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_MNTH"));
                                            String bonusPrizedAfter = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_AFT"));
                                            String bonusPrizedEnd = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_END"));
                                            long bonusPrizedValue = CommonUtil.convertObjToLong(productMap.get("BONUS_PRIZED_GRACE_PERIOD"));
                                            if (bonusPrizedDays != null && !bonusPrizedDays.equals("") && bonusPrizedDays.equals("D") && diffDay <= bonusPrizedValue) {
                                                totBonusAmt = totBonusAmt + bonusAmt;
                                            } else if (bonusPrizedMonth != null && !bonusPrizedMonth.equals("") && bonusPrizedMonth.equals("M") && diffDay <= (bonusPrizedValue * 30)) {
                                                totBonusAmt = totBonusAmt + bonusAmt;
                                            } else if (bonusPrizedAfter != null && !bonusPrizedAfter.equals("") && bonusPrizedAfter.equals("A") && currDate.getDate() <= bonusPrizedValue) {
                                                totBonusAmt = totBonusAmt + bonusAmt;
                                            } else if (bonusPrizedEnd != null && !bonusPrizedEnd.equals("") && bonusPrizedEnd.equals("E")) {

                                            } else {

                                            }
                                        } else if (getRdoPrizedMember_No() == true) {
                                            String bonusGraceDays = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_DAYS"));
                                            String bonusGraceMonth = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_MONTHS"));
                                            String bonusGraceOnAfter = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_AFTER"));
                                            String bonusGraceEnd = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_END"));
                                            long bonusGraceValue = CommonUtil.convertObjToLong(productMap.get("BONUS_GRACE_PERIOD"));
                                            if (bonusGraceDays != null && !bonusGraceDays.equals("") && bonusGraceDays.equals("D") && diffDay <= bonusGraceValue) {
                                                totBonusAmt = totBonusAmt + bonusAmt;
                                            } else if (bonusGraceMonth != null && !bonusGraceMonth.equals("") && bonusGraceMonth.equals("M") && diffDay <= (bonusGraceValue * 30)) {
                                                totBonusAmt = totBonusAmt + bonusAmt;
                                            } else if (bonusGraceOnAfter != null && !bonusGraceOnAfter.equals("") && bonusGraceOnAfter.equals("A") && currDate.getDate() <= bonusGraceValue) {
                                                totBonusAmt = totBonusAmt + bonusAmt;
                                            } else if (bonusGraceEnd != null && !bonusGraceEnd.equals("") && bonusGraceEnd.equals("E")) {

                                            } else {
                                            }
                                        }
                                    }
                                    HashMap instMap = new HashMap();
                                    if (installmentMap.containsKey(String.valueOf(l + noOfInsPaid + addNo))) {
                                        Rounding rod = new Rounding();
                                        instMap = (HashMap) installmentMap.get(String.valueOf(l + noOfInsPaid + addNo));
                                        //Added By Suresh
                                        if (productMap.get("BONUS_ROUNDING") != null && !productMap.get("BONUS_ROUNDING").equals("") && productMap.get("BONUS_ROUNDING").equals("Y")
                                                && bonusAmt > 0) {
                                            bonusAmt = (double) rod.getNearest((long) (bonusAmt * 100), 100) / 100;
                                        }
                                        instMap.put("BONUS", String.valueOf(bonusAmt));
                                        installmentMap.put(String.valueOf(l + noOfInsPaid + addNo), instMap);
                                    }
                                }
                            }
                            bonusAmt = 0;
                        }
                        if (productMap.get("BONUS_ROUNDING") != null && !productMap.get("BONUS_ROUNDING").equals("") && productMap.get("BONUS_ROUNDING").equals("Y")
                                && totBonusAmt > 0) {
                            Rounding rod = new Rounding();
                            totBonusAmt = (double) rod.getNearest((long) (totBonusAmt * 100), 100) / 100;
                        }

                        int insDay = 0;
                        Date paidUpToDate = null;
                        HashMap instDateMap = new HashMap();
                        instDateMap.put("SCHEME_NAME", prId);
                        instDateMap.put("DIVISION_NO", divNo);
                        instDateMap.put("INSTALLMENT_NO", CommonUtil.convertObjToInt(String.valueOf(count)));
                        lst = ClientUtil.executeQuery("getSelectInstUptoPaid", instDateMap);
                        if (lst != null && lst.size() > 0) {
                            instDateMap = (HashMap) lst.get(0);
                            paidUpToDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(instDateMap.get("NEXT_INSTALLMENT_DATE")));
                        } else {
                            Date startedDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(chittalMap.get("SCHEME_START_DT")));
                            insDay = CommonUtil.convertObjToInt(chittalMap.get("INSTALLMENT_DAY"));
                            startedDate.setDate(insDay);
                            int stMonth = startedDate.getMonth();
                            startedDate.setMonth(stMonth + (int) count - 1);
                            paidUpToDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(startedDate));
                        }

                        String narration = "";
                        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM/yyyy");
                        int noInstPay = CommonUtil.convertObjToInt(noOfInsPay);
                        if (noInstPay == 1) {
                            narration = "Inst#" + (noOfInsPaid + 1);
                            Date dt = DateUtil.addDays(paidUpToDate, 30);
                            narration += " " + sdf.format(dt);
                        } else if (noInstPay > 1) {
                            narration = "Inst#" + (noOfInsPaid + 1);
                            narration += "-" + (noOfInsPaid + noInstPay);
                            Date dt = DateUtil.addDays(paidUpToDate, 30);
                            narration += " " + sdf.format(dt);
                            dt = DateUtil.addDays(paidUpToDate, 30 * noInstPay);
                            narration += " To " + sdf.format(dt);
                        }
                        //System.out.println("#$#$# narration :" + narration);
                        //  penalAmt= Long.parseLong(pene);//added by babu 22-Apr-2013
                        if (calculateIntOn != null && calculateIntOn.equals("Installment Amount")) {
                            insAmt = CommonUtil.convertObjToDouble(productMap.get("INSTALLMENT_AMOUNT"));
                        }
                        double instAmt = insAmt * CommonUtil.convertObjToDouble(noOfInsPay).doubleValue();
                        double totalPayable = instAmt - (totBonusAmt + totDiscAmt);
                        //System.out.println("#$#$# totalPayable :" + totalPayable);
                        netAmt = totalPayable + penalAmt + CommonUtil.convertObjToDouble(noticAmt).doubleValue()
                                + CommonUtil.convertObjToDouble(arbitAmt).doubleValue();
                        String totalPayableAmount = String.valueOf(totalPayable);
                        totalPayableAmount = CurrencyValidation.formatCrore(totalPayableAmount).replaceAll(",", "");
                        String penalAmount = String.valueOf(penalAmt);

                        // String penalAmount = String.valueOf(inter);
                        penalAmount = CurrencyValidation.formatCrore(penalAmount).replaceAll(",", "");

                        /**
                         * ****************
                         */
                        // chittalFlag=false;
                        //                    //System.out.println("###### insAmt        : "+insAmt);
                        //                    //System.out.println("###### insAmtPayable : "+instAmt);
                        //                    //System.out.println("###### totBonusAmt   : "+totBonusAmt);
                        //                    //System.out.println("###### totDiscAmt    : "+totDiscAmt);
                        //                    //System.out.println("###### totalPayable  : "+totalPayable);
                        //                    //System.out.println("###### penalAmt      : "+penalAmt);
                        //                    //System.out.println("###### netAmt        : "+netAmt);
                        //                    //System.out.println("###### dataMap         : "+dataMap);
                        //System.out.println("###### finalMap        : " + finalMap);
                        //System.out.println("###### installmentMap  :" + installmentMap);
                        String instAmount = String.valueOf(instAmt);
                        instAmount = CurrencyValidation.formatCrore(instAmount).replaceAll(",", "");

                        String totBonusAmount = String.valueOf(totBonusAmt);
                        totBonusAmount = CurrencyValidation.formatCrore(totBonusAmount).replaceAll(",", "");

                        String totDiscAmount = String.valueOf(totDiscAmt);
                        totDiscAmount = CurrencyValidation.formatCrore(totDiscAmount).replaceAll(",", "");

                        String netAmount = String.valueOf(netAmt);
                        netAmount = CurrencyValidation.formatCrore(netAmount).replaceAll(",", "");
                        //System.out.println("VALITYYUUUUUUUUUUUUUUUUUUUU===" + totalPayableAmount);
                        if (CommonUtil.convertObjToDouble(noOfInsPay).doubleValue() != 0) {
                            tblTransaction.setValueAt(totalPayableAmount, selectedRow, 5);
                            tblTransaction.setValueAt("0.0", selectedRow, 6);//penalAmount
                            tblTransaction.setValueAt(penalAmount, selectedRow, 7);//instAmount
                            tblTransaction.setValueAt(totBonusAmount, selectedRow, 8);
                        } else {
                            tblTransaction.setValueAt(principal1, selectedRow, 5);
                            tblTransaction.setValueAt("0.0", selectedRow, 6);//penalAmount
                            tblTransaction.setValueAt(interest1, selectedRow, 7);//instAmount
                            tblTransaction.setValueAt(bonus1, selectedRow, 8);
                        }
                        //  tblTransaction.setValueAt(instAmount, selectedRow, 5);
                        //  tblTransaction.setValueAt(totBonusAmount, selectedRow, 6);
                        //  tblTransaction.setValueAt(totDiscAmount, selectedRow, 7);
                        //  tblTransaction.setValueAt(totalPayableAmount, selectedRow, 8);
                        //  tblTransaction.setValueAt(penalAmount, selectedRow, 9);
                        //  tblTransaction.setValueAt(netAmount, selectedRow, 12);

                        //System.out.println("column ==== " + column + " chk=====" + chk);
                        if (chk)//column==0 &&
                        {
                            //babu
                            dataMap.put("SCHEME_NAME", prId);
                            dataMap.put("CHITTAL_NO", chittalNo);
                            dataMap.put("PRINCIPAL", CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 5)));//totalPayableAmount);
                            dataMap.put("INTEREST", CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 7)));
                            dataMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
                            dataMap.put("MEMBER_NAME", txtName.getText());//lblMemberName.getText()
                            dataMap.put("DIVISION_NO", String.valueOf(divNo));
                            dataMap.put("CHIT_START_DT", startDate);
                            dataMap.put("INSTALLMENT_DATE", insDate);
                            dataMap.put("NO_OF_INSTALLMENTS", noOfInsPay);//String.valueOf(remainInst));//String.valueOf(totIns));
                            dataMap.put("CURR_INST", String.valueOf(curInsNo));
                            dataMap.put("PENDING_INST", insDue);
                            dataMap.put("PENDING_DUE_AMT", String.valueOf(insDueAmt));
                            dataMap.put("NO_OF_INST_PAY", noOfInsPay);
                            dataMap.put("PROD_DESCRIPTION", description);
                            dataMap.put("INST_AMT_PAYABLE", String.valueOf(totalPayable));
                            dataMap.put("PAID_INST", String.valueOf(noOfInsPaid));
                            dataMap.put("TOTAL_PAYABLE", String.valueOf(instAmt));
                            dataMap.put("PAID_DATE", currDate);
                            dataMap.put("INST_AMT", String.valueOf(insAmt));
                            dataMap.put("SCHEME_END_DT", endDate);
                            if (getRdoPrizedMember_Yes() == true) {
                                dataMap.put("PRIZED_MEMBER", "Y");
                            } else {
                                dataMap.put("PRIZED_MEMBER", "N");
                            }
                            dataMap.put("BONUS_AVAL", String.valueOf(bonusAval));
                            dataMap.put("BONUS", CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 8)));
                            dataMap.put("DISCOUNT", String.valueOf(totDiscAmt));
                            dataMap.put("PENAL", String.valueOf(penalAmt));
                            dataMap.put("NOTICE_AMOUNT", noticAmt);
                            dataMap.put("ARBITRATION_AMOUNT", arbitAmt);
                            dataMap.put("NET_AMOUNT", String.valueOf(netAmt));
                            dataMap.put("TOTAL_DEMAND", CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 5)) + CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 7))
                                    + CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 9)));//String.valueOf(netAmt));
                            ////
                            dataMap.put("NARRATION", narration);
                            dataMap.put("EACH_MONTH_DATA", installmentMap);
                            dataMap.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
                            dataMap.put("PROD_TYPE", "MDS");
                            dataMap.put("TOTAL", noOfInsPay);
                            dataMap.put("AMTORNOOFINST", noOfInsPay);
                            dataMap.put("CLOCK_NO", txtClockNo.getText());
                            dataMap.put("MEMBER_NO", txtMemberNo.getText());
                            dataMap.put("CUST_NAME", txtName.getText());
                            dataMap.put("TOT_PRIN", CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 5)));
                            dataMap.put("TOT_PENAL", "0.0");
                            dataMap.put("TOT_INT", CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 7)));
                            dataMap.put("TOT_OTHERS", CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 9)));
                            dataMap.put("TOT_GRAND", CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 5)) + CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 7))
                                    + CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 9)));
                            dataMap.put("CHARGES", new Double(0));
                            System.out.println("noOfInsPay ===456436546==" + tblTransaction.getValueAt(selectRow, 7));
                            if (CommonUtil.convertObjToDouble(noOfInsPay) > 0) {// && !chittalFlag
                                // termMdsChittalMap=new HashMap();
                                //  termMdsChittalMap.put("MDS",dataMap);
                                //                            finalMap.put(chittalNo,termMdsMap);

                                termMdsMap = new HashMap();
                                termMdsMap.put("MDS", dataMap);
                                finalMap.put(chittalNo, termMdsMap);
                                //System.out.println("finalMap====IIII===" + finalMap);
                            } else {
                                dataMap = null;
                            }
                            //Enf
                        }
                        tblTransaction.revalidate();
                        ((DefaultTableModel) tblTransaction.getModel()).fireTableDataChanged();

                    } else {
                        ClientUtil.showAlertWindow("Exceeds The No Of Total Installment !!! ");
                        tblTransaction.setValueAt("", selectRow, 4);
                        tblTransaction.setValueAt("", selectRow, 5);
                        tblTransaction.setValueAt("", selectRow, 6);
                        tblTransaction.setValueAt("", selectRow, 7);
                        tblTransaction.setValueAt("", selectRow, 8);
                        tblTransaction.setValueAt("", selectRow, 9);
                        tblTransaction.setValueAt("", selectRow, 10);
                        tblTransaction.setValueAt("", selectRow, 11);
                        // tblTransaction.setValueAt("",tblTransaction.getSelectedRow(),12);
                    }
                }
            }
            System.out.println("end of function new");
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private Date setProperDtFormat(Date dt) {
        Date tempDt = (Date) curr_dt.clone();
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }

    private void calcTotal(boolean chk, int selectRow, int column) {
        double totprincipal = 0;
        double totpenal = 0;
        double totinterest = 0;
        double totOthers = 0;
        double totNetAmt = 0;
        double totNotice = 0;
        double totArb = 0;
        //System.out.println("dsfsdf");
        if (tblTransaction.getRowCount() > 0) {
            for (int i = 0; i < tblTransaction.getRowCount(); i++) {
                //System.out.println("BGHHYHH===BV=======" + ((Boolean) tblTransaction.getValueAt(i, 0)).booleanValue());
                //System.out.println("BGHHYHH===CHK=====" + chk);
                //System.out.println("BGHHYHH===column=====" + column);
                chk = ((Boolean) tblTransaction.getValueAt(i, 0)).booleanValue();
                // if(chk)//CommonUtil.convertObjToDouble(tblTransaction.getValueAt(i,4))>0)//tblTransaction.getValueAt(i, 2)!=null)
                //System.out.println("CommonUtil.convertObjToDouble(tblTransaction.getValueAt(i,4)):::" + CommonUtil.convertObjToDouble(tblTransaction.getValueAt(i, 4)));
                if (column == 0 && chk && CommonUtil.convertObjToDouble(tblTransaction.getValueAt(i, 4)) > 0) {
                    totprincipal = totprincipal + CommonUtil.convertObjToDouble(tblTransaction.getValueAt(i, 5)).doubleValue();
                    //CommonUtil.convertObjToDouble(tblTransaction.getValueAt(i, 3).toString()).doubleValue();
                    totpenal = totpenal + CommonUtil.convertObjToDouble(tblTransaction.getValueAt(i, 6)).doubleValue();
                    //CommonUtil.convertObjToDouble(tblTransaction.getValueAt(i, 4).toString()).doubleValue();
                    totinterest = totinterest + CommonUtil.convertObjToDouble(tblTransaction.getValueAt(i, 7)).doubleValue();
                    //CommonUtil.convertObjToDouble(tblTransaction.getValueAt(i, 5).toString()).doubleValue();
                    totOthers = totOthers + CommonUtil.convertObjToDouble(tblTransaction.getValueAt(i, 9)).doubleValue();
                    //CommonUtil.convertObjToDouble(tblTransaction.getValueAt(i, 7).toString()).doubleValue();
                    if (tblTransaction.getValueAt(i, 9) != null) {
                        totOthers = totOthers + CommonUtil.convertObjToDouble(tblTransaction.getValueAt(i, 10)).doubleValue();
                    }
                    //CommonUtil.convertObjToDouble(tblTransaction.getValueAt(i, 8).toString()).doubleValue();
                    if (tblTransaction.getValueAt(i, 10) != null) {
                        totOthers = totOthers + CommonUtil.convertObjToDouble(tblTransaction.getValueAt(i, 11)).doubleValue();
                    }

                    if (!CommonUtil.convertObjToStr(tblTransaction.getValueAt(i, 1)).equals("MDS")) {
                        if (tblTransaction.getValueAt(i, 7) != null) {
                            totOthers = totOthers + CommonUtil.convertObjToDouble(tblTransaction.getValueAt(i, 8)).doubleValue();
                        }
                    }
                }
            }
            totNetAmt = totprincipal + totpenal + totinterest + totOthers;
        }
        txtTotPrincipal.setText(String.valueOf(totprincipal));
        txtTotInterest.setText(String.valueOf(totinterest));
        txtTotPenel.setText(String.valueOf(totpenal));
        txtTotOthers.setText(String.valueOf(totOthers));
        txtGrandTotal.setText(String.valueOf(totNetAmt));
        transactionUI.cancelAction(false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.resetObjects();
        transactionUI.setCallingAmount(txtGrandTotal.getText());
        transactionUI.setCallingApplicantName(txtName.getText());
    }

    public void getTDDetails(int column, boolean chk, String selectAll, int selectRow) {
        flag = true;
        String prod_id = "";
        String amt;
        //get a/c no scheme,
        String pay = CommonUtil.convertObjToStr(tblTransaction.getValueAt(selectRow, 4));
        String scheme_Name = CommonUtil.convertObjToStr(tblTransaction.getValueAt(selectRow, 1));
        String description = CommonUtil.convertObjToStr(tblTransaction.getValueAt(selectRow, 2));
        prod_id = CommonUtil.convertObjToStr(tblTransaction.getValueAt(selectRow, 12));
        String acNo = CommonUtil.convertObjToStr(tblTransaction.getValueAt(selectRow, 3));
        double instM = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 4)).doubleValue();
        double principal = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 5)).doubleValue();
        System.out.println("principle = "+principal);
        double interest = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 7)).doubleValue();
        double penal = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 6)).doubleValue();
        double charges = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 9)).doubleValue();
        double totalInst = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 14)).doubleValue();
        double prin_old = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 15)).doubleValue();

        //System.out.println("ASIZEEE=====================" + globalList.size());
        //System.out.println("globallist"+globalList);
//        for (int i = 0; i < globalList.size(); i++) {
//            ArrayList aList1 = (ArrayList) globalList.get(i);
//            for (int j = 0; j < aList1.size(); j++) {
//                String PID = aList1.get(11).toString();
//                if (PID.equals(prod_id)) {
//                    //  if(aList.get(3)!=null)
//                    principal = Double.parseDouble(aList1.get(4).toString());
//                    //  if(aList.get(5)!=null)
//                    interest = Double.parseDouble(aList1.get(6).toString());
//                    //   if(aList.get(4)!=null)
//                    penal = Double.parseDouble(aList1.get(5).toString());
//                    //  if(aList.get(7)!=null)
//                    charges = Double.parseDouble(aList1.get(8).toString());
//                    // displayAlert("principal ="+principal +" interest="+interest+" penal="+penal);
//                }
//            }
//        }
        
        
        //Set of code added by Jeffn John For Mantis : 10163 on 07-01-2015
        HashMap installmentMap = new HashMap();
        installmentMap.put("DEPOSIT_NO", CommonUtil.convertObjToStr(acNo.substring(0, acNo.indexOf("_"))));
        installmentMap.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
        List installmentList = ClientUtil.executeQuery("getProductIdForDeposits", installmentMap);
        if(installmentList != null && installmentList.size()>0){
            installmentMap = (HashMap) installmentList.get(0);
            if(installmentMap != null && installmentMap.size()>0){
                int totalInstallment = CommonUtil.convertObjToInt(installmentMap.get("TOTAL_INSTALLMENTS"));
                int totalInstPaid = CommonUtil.convertObjToInt(installmentMap.get("TOTAL_INSTALL_PAID"));
                int balanceInst = totalInstallment - totalInstPaid;
                if(instM>balanceInst){
                    ClientUtil.showAlertWindow("No of installments exceeds the total no of installments, Please pay balance installments or pending installments");
                    tblTransaction.setValueAt(totalInst, selectRow, 4);
                    setTableModelListener();
                    return;
                }
            } 
        }
        //Ends here
        
        System.out.println("prin_old ========" + prin_old);
        System.out.println("instM ========" + instM);
        System.out.println("principal ========" + principal);
        System.out.println("chkRetired.isSelected() ========" + chkRetired.isSelected());
        if (pay != null || !pay.equals("") || instM != 0) {
            if (chkRetired.isSelected()) {
                tblTransaction.setValueAt(prin_old * instM, selectRow, 5);
                tblTransaction.setValueAt(instM, selectRow, 4);
                principal = prin_old * instM;
        } else {
                tblTransaction.setValueAt(instM, selectRow, 4);
                tblTransaction.setValueAt(prin_old * instM, selectRow, 5);
                principal = prin_old * instM;
            }
        } else {
            System.out.println("inside else");
            double principal1 = 0;
            double interest1 = 0;
            double penal1 = 0;
            double charges1 = 0;
            for (int i = 0; i < globalList.size(); i++) {
                ArrayList aList1 = (ArrayList) globalList.get(i);
                for (int j = 0; j < aList1.size(); j++) {
                    String PID = aList1.get(11).toString();
                    if (PID.equals(prod_id)) {
                        principal1 = Double.parseDouble(aList1.get(4).toString());
                        interest1 = Double.parseDouble(aList1.get(6).toString());
                        penal1 = Double.parseDouble(aList1.get(5).toString());
                        charges1 = Double.parseDouble(aList1.get(8).toString());
                    }
                }
            }
            //System.out.println("principal1=====" + principal1);
            tblTransaction.setValueAt(instM, selectRow, 4);
            tblTransaction.setValueAt(principal1, selectRow, 5);
            principal = prin_old * instM;
        }
        
        
        //Set of code added by Jeffin John for Mantis : 9967 on 07-01-2015
        if (instM > 0 && column == 4) {
            List lst = null;
            HashMap lastMap = new HashMap();
            HashMap accountMap = new HashMap();
            if (acNo.contains("_")) {
                String tempActNum = CommonUtil.convertObjToStr(acNo.substring(0, acNo.indexOf("_")));
                lastMap.put("DEPOSIT_NO", tempActNum);
                lst = ClientUtil.executeQuery("getInterestDeptIntTable", lastMap);
                if (lst != null && lst.size() > 0) {
                    lastMap = (HashMap) lst.get(0);
                }
                lst = null;
                accountMap.put("DEPOSIT_NO", tempActNum);
                accountMap.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
                lst = ClientUtil.executeQuery("getProductIdForDeposits", accountMap);
                if (lst != null && lst.size() > 0) {
                    accountMap = (HashMap) lst.get(0);
                }
            }
            lst = null;
            double depAmt = prin_old;
            HashMap delayMap = new HashMap();
            double roi = 0.0;
            delayMap.put("PROD_ID", accountMap.get("PROD_ID"));
            delayMap.put("DEPOSIT_AMT", accountMap.get("DEPOSIT_AMT"));
            lst = ClientUtil.executeQuery("getSelectDelayedRate", delayMap);
            if (lst != null && lst.size() > 0) {
                delayMap = (HashMap) lst.get(0);
                roi = CommonUtil.convertObjToDouble(delayMap.get("PENAL_INT"));
            }
            penal = 0.0;
            double tot_Inst_paid = CommonUtil.convertObjToDouble(lastMap.get("TOTAL_INSTALL_PAID"));
            HashMap depRecMap = new HashMap();
            depRecMap.put("DEPOSIT_NO", acNo);
            depRecMap.put("DEPOSIT_DT", lastMap.get("DEPOSIT_DT"));
            depRecMap.put("CURR_DT", curr_dt);
            depRecMap.put("SL_NO", String.valueOf(tot_Inst_paid));
            List lstRec = ClientUtil.executeQuery("getDepTransRecurr", depRecMap);
            int size = CommonUtil.convertObjToInt(lstRec.size());
            if (lstRec != null && lstRec.size() > 0) {
                if (instM <= size) {
                    for (int i = 0; i < instM; i++) {
                        depRecMap = (HashMap) lstRec.get(i);
                        double amount = depAmt * (i + 1);
                        Date dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depRecMap.get("DUE_DATE")));
                        if (DateUtil.dateDiff(dueDate, curr_dt) > 0) {
                            double diff = DateUtil.dateDiff(dueDate, curr_dt) - 1;
                            if(diff>0){
                                penal = penal + ((amount * roi * diff) / 36500);
                            }
                        }
                    }
                }
                if (instM > size) {
                    for (int i = 0; i < size; i++) {
                        depRecMap = (HashMap) lstRec.get(i);
                        double amount = depAmt * (i + 1);
                        Date dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depRecMap.get("DUE_DATE")));
                        if (DateUtil.dateDiff(dueDate, curr_dt) > 0) {
                            double diff = DateUtil.dateDiff(dueDate, curr_dt) - 1;
                            if (diff > 0) {
                                penal = penal + ((amount * roi * diff) / 36500);
                            }
                        }
                    }
                }
            }
            penal = Math.round(penal);
            if (penal>=0){
                tblTransaction.setValueAt(penal, selectRow, 6);
            }
        }
        //Ends here

        if ((column == 0 || column == 4 || column == 6) && chk) {
            HashMap dataMap = new HashMap();
            dataMap.put("PROD_TYPE", "TD");
            dataMap.put("ACT_NUM", acNo);
            dataMap.put("RECOVERED_AMOUNT", principal + interest + penal);
            dataMap.put("PROD_ID", prod_id);
            dataMap.put("USER_ID", TrueTransactMain.BRANCH_ID);
            dataMap.put("PRINCIPAL", String.valueOf(principal));
            dataMap.put("PENAL", String.valueOf(penal));
            dataMap.put("INTEREST", CommonUtil.convertObjToDouble(0.0));
            dataMap.put("TOTAL_DEMAND", principal + interest + penal);
            dataMap.put("DEPOSIT_PENAL_MONTH", String.valueOf("0"));
            dataMap.put("CHARGES", new Double(0));
            dataMap.put("CLOCK_NO", txtClockNo.getText());
            dataMap.put("MEMBER_NO", txtMemberNo.getText());
            dataMap.put("CUST_NAME", txtName.getText());
            dataMap.put("TOT_PRIN", txtTotPrincipal.getText());
            dataMap.put("TOT_PENAL", String.valueOf(penal));
            dataMap.put("TOT_INT", CommonUtil.convertObjToDouble(0.0));
            dataMap.put("TOT_OTHERS", txtTotOthers.getText());
            dataMap.put("TOT_GRAND", txtGrandTotal.getText());
            dataMap.put("BONUS", new Double(0));
            dataMap.put("NOTICE_AMOUNT", new Double(0));
            dataMap.put("ARBITRATION_AMOUNT", new Double(0));
            dataMap.put("PROD_DESCRIPTION",description);
            dataMap.put("AMTORNOOFINST", instM);
            if (CommonUtil.convertObjToDouble(dataMap.get("TOTAL_DEMAND")).doubleValue() <= 0.0) {
                dataMap = null;
            }
            dataMap.put("TOTAL", instM);
            if (instM > 0) {
                System.out.println("inside instM>0");
                termTdMap = new HashMap();
                termTdMap.put("TD", dataMap);
                finalMap.put(acNo, termTdMap);
                System.out.println("finallllllllllllllllll"+finalMap);
            }
        }
    }

    //For loan
    public void getSADetails(int column, boolean chk, String selectAll, int selectRow) {
        String prod_id = "";
        String amt;
        boolean fl = false;
        String scheme_Name = CommonUtil.convertObjToStr(tblTransaction.getValueAt(selectRow, 1));
        prod_id = CommonUtil.convertObjToStr(tblTransaction.getValueAt(selectRow, 12));
        String acNo = CommonUtil.convertObjToStr(tblTransaction.getValueAt(selectRow, 3));
        String description = CommonUtil.convertObjToStr(tblTransaction.getValueAt(selectRow, 2));
        double instM = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 4)).doubleValue();
        double principal = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 5)).doubleValue();
        double interest = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 7)).doubleValue();
        double penal = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 6)).doubleValue();
        double charges = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 9)).doubleValue();
        double totalInst = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 13)).doubleValue();
        //double interest1 = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 7)).doubleValue();
        if (String.valueOf(instM) != null && !String.valueOf(instM).equals("")) {
            //System.out.println("ASIZEEE=====================" + interest);
        }
//        if (instM > totalInst) {
//            // fl=true;
//            System.out.println("before print");
//            ClientUtil.showAlertWindow("Exceeds the Total Amount !!! ");
//            tblTransaction.setValueAt("0.0", selectRow, 4);
//
//            // ClientUtil.showAlertWindow("Exceeds the Total Amount !!! ");
//            //ClientUtil.showMessageWindow("Exceeds the Total Amount !!! ");
//            //tblTransaction.setValueAt("0", selectRow,4);
//            // tblTransaction.
//            return;
//        }
        //For Loop
        //   //System.out.println("ASIZEEE====================="+globalList.size());
        if (instM <= 0) {
            flag = true;
            for (int i = 0; i < globalList.size(); i++) {
                ArrayList aList1 = (ArrayList) globalList.get(i);
                for (int j = 0; j < aList1.size(); j++) {
                    String PID = aList1.get(11).toString();
                    if (PID.equals(prod_id)) {
                        principal = Double.parseDouble(aList1.get(4).toString());
                        interest = Double.parseDouble(aList1.get(6).toString());
                        penal = Double.parseDouble(aList1.get(5).toString());
                        charges = Double.parseDouble(aList1.get(8).toString());
                    }
                }
            }
            tblTransaction.setValueAt(new Double(principal+interest), selectRow, 4);
            tblTransaction.setValueAt(new Double(principal), selectRow, 5);
            tblTransaction.setValueAt(new Double(interest), selectRow, 7);
            tblTransaction.setValueAt(new Double(penal), selectRow, 6);
            tblTransaction.setValueAt(new Double(charges), selectRow, 9);
        } else {
            double interst = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 7)).doubleValue();
            totalInst = instM - interest;
            // }
            //System.out.println("interest ==" + interest1 + " totalInst==" + totalInst + " instM==" + instM);
            if (instM > 0) {
                if (column == 4 && flag == false) {
                    flag = true;
                    double oldPayingAmt = principal + interest;
                    double value = 0.0;
                    if (instM < oldPayingAmt) {
                        value = oldPayingAmt - instM;
                        if (instM >= interest) {
                            value = instM - interest;
							//interest = 0.0;
                            principal = value;
                            //tblTransaction.setValueAt(interest, selectRow, 7);
                            tblTransaction.setValueAt(principal, selectRow, 5);
                        }
                        if (interest >= instM) {
                            interest = instM;
                            if (interest >= 0) {
                                principal = 0.0;
                                tblTransaction.setValueAt(principal, selectRow, 5);
                                tblTransaction.setValueAt(interest, selectRow, 7);
                            } else {
                                interest = 0.0;
                                principal = 0.0;
                                tblTransaction.setValueAt(principal, selectRow, 5);
                                tblTransaction.setValueAt(interest, selectRow, 7);
                            }
                        }
                    }
                    if (instM > oldPayingAmt) {
                        value = instM - oldPayingAmt;
                        principal += value;
                        tblTransaction.setValueAt(principal, selectRow, 5);
                    }
                }
                if (column == 7 && flag == false) {
                    principal = instM - interest;
                    flag = true;
                    tblTransaction.setValueAt(principal, selectRow, 5);
                }
                //tblTransaction.setValueAt(interst, tblTransaction.getSelectedRow(),6); //totalInst
            }
            //instM = instM + interst - interest1;
            if (instM > 0) {
                //((DefaultTableModel) tblTransaction.getModel()).removeTableModelListener(tableModelListener);
                //tblTransaction.setValueAt(instM,selectRow,4); //totalInst
                //tblTransaction.setValueAt(interst, tblTransaction.getSelectedRow(),6); //totalInst
            }
            //System.out.println("totalInst+interst;" + totalInst + interst);
            if ((column == 0 || column == 4 || column == 7) && chk) {
                HashMap dataMap = new HashMap();
                dataMap.put("RECOVERED_AMOUNT", new Double(instM));//new Double(totalInst));
                dataMap.put("ACT_NUM", acNo);
                dataMap.put("PROD_ID", prod_id);
                dataMap.put("PROD_TYPE", "SA");
                dataMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
                dataMap.put("USER_ID", TrueTransactMain.USER_ID);

                dataMap.put("BONUS", new Double(0));
                dataMap.put("PRINCIPAL", principal);
                dataMap.put("TOTAL_DEMAND", new Double(instM));
                dataMap.put("INTEREST", interest);
                dataMap.put("PENAL", new Double(0));
                dataMap.put("CHARGES", new Double(0));
                dataMap.put("CLOCK_NO", txtClockNo.getText());
                dataMap.put("MEMBER_NO", txtMemberNo.getText());
                dataMap.put("CUST_NAME", txtName.getText());
                dataMap.put("TOT_PRIN", txtTotPrincipal.getText());
                dataMap.put("TOT_PENAL", txtTotPenel.getText());
                dataMap.put("TOT_INT", txtTotInterest.getText());
                dataMap.put("TOT_OTHERS", txtTotOthers.getText());
                dataMap.put("TOT_GRAND", txtGrandTotal.getText());
                dataMap.put("NOTICE_AMOUNT", new Double(0));
                dataMap.put("ARBITRATION_AMOUNT", new Double(0));
                dataMap.put("AMTORNOOFINST", instM);
                dataMap.put("PROD_DESCRIPTION", description);
                // //System.out.println("acNo====IN===="+acNo+"termSaMap=="+termSaMap);
                if (instM > 0) {
                    termSaMap = new HashMap();
                    termSaMap.put("SA", dataMap);
                    //System.out.println("acNo====IN====" + acNo + "termSaMap==" + termSaMap);
                    finalMap.put(acNo, termSaMap);
                } else {
                    dataMap = null;
                }
            }
        }
        // if(fl)
        //    tblTransaction.setValueAt("", tblTransaction.getSelectedRow(),2);
    }

    public void getSBDetails(int column, boolean chk, String selectAll, int selectRow) {
        flag = true;
        String prod_id = "";
        String amt;
        boolean fl = false;
        String scheme_Name = CommonUtil.convertObjToStr(tblTransaction.getValueAt(selectRow, 1));
    
        prod_id = CommonUtil.convertObjToStr(tblTransaction.getValueAt(selectRow, 12));
        String acNo = CommonUtil.convertObjToStr(tblTransaction.getValueAt(selectRow, 3));
        String description = CommonUtil.convertObjToStr(tblTransaction.getValueAt(selectRow, 2));
        double instM = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 4)).doubleValue();
        double principal = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 5)).doubleValue();
        double interest = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 7)).doubleValue();
        double penal = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 6)).doubleValue();
        double charges = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 9)).doubleValue();
        double totalInst = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 13)).doubleValue();
        String particulars = CommonUtil.convertObjToStr(tblTransaction.getValueAt(selectRow, 8));
        System.out.println("scheme_Name,prod_id,acNo,instM,principal,interest,penal"+ scheme_Name+"  "+prod_id+" "+acNo+"  "+instM+"  "+principal+" "+interest+"sdygs"+penal);
        if (String.valueOf(instM) != null && !String.valueOf(instM).equals("")) {
            if (instM < totalInst) {
                // fl=true;
                // ClientUtil.showAlertWindow("Enter the Total Amount !!! ");
                // tblTransaction.setValueAt("", tblTransaction.getSelectedRow(),2);
                // tblTransaction.
                //  return;
            }
        }
        //For Loop
        //System.out.println("ASIZEEE=====================" + globalList.size());
        for (int i = 0; i < globalList.size(); i++) {
            ArrayList aList1 = (ArrayList) globalList.get(i);
            for (int j = 0; j < aList1.size(); j++) {
                String PID = aList1.get(11).toString();
                if (PID.equals(prod_id)) {
                    //  if(aList.get(3)!=null)
                    //   principal=Double.parseDouble(aList1.get(4).toString());
                    //  if(aList.get(5)!=null)
                    //    interest=Double.parseDouble(aList1.get(6).toString());
                    //   if(aList.get(4)!=null)
                    //    penal=Double.parseDouble(aList1.get(5).toString());
                    //  if(aList.get(7)!=null)
                    //    charges=Double.parseDouble(aList1.get(8).toString());
                    // displayAlert("principal ="+principal +" interest="+interest+" penal="+penal);
                }
            }
        }
        tblTransaction.setValueAt(instM, selectRow, 5);
        tblTransaction.setValueAt(instM, selectRow, 13);
        if ((column == 0 || column == 4) && chk) {
            HashMap dataMap = new HashMap();
            dataMap.put("RECOVERED_AMOUNT", new Double(instM));//new Double(totalInst));
            dataMap.put("ACT_NUM", acNo);
            dataMap.put("PROD_ID", prod_id);
            dataMap.put("PROD_TYPE", scheme_Name);
            dataMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            dataMap.put("USER_ID", TrueTransactMain.USER_ID);

            dataMap.put("PRINCIPAL", new Double(totalInst));
            dataMap.put("TOTAL_DEMAND", new Double(totalInst));
            dataMap.put("INTEREST", new Double(0));
            dataMap.put("PENAL", new Double(0));
            dataMap.put("CHARGES", new Double(0));
            dataMap.put("BONUS", new Double(0));
            dataMap.put("CLOCK_NO", txtClockNo.getText());
            dataMap.put("MEMBER_NO", txtMemberNo.getText());
            dataMap.put("CUST_NAME", txtName.getText());
            dataMap.put("TOT_PRIN", txtTotPrincipal.getText());
            dataMap.put("TOT_PENAL", txtTotPenel.getText());
            dataMap.put("TOT_INT", txtTotInterest.getText());
            dataMap.put("TOT_OTHERS", txtTotOthers.getText());
            dataMap.put("TOT_GRAND", txtGrandTotal.getText());
            dataMap.put("NOTICE_AMOUNT", new Double(0));
            dataMap.put("ARBITRATION_AMOUNT", new Double(0));
            dataMap.put("AMTORNOOFINST", instM);
            dataMap.put("PARTICULARS",particulars);
            dataMap.put("PROD_DESCRIPTION",description);
            if (instM > 0) {
                termGlMap = new HashMap();
                termGlMap.put("GL", dataMap);
                finalMap.put(acNo, termGlMap);
                // finalMap.put("GL",dataMap);
            } else {
                dataMap = null;
            }
        }
    }

    public void getLoanDetails(String key, int column, boolean chk, String selectAll, int selectRow) {
        flag=true;
        String prod_id = "";
        String amt;
        double totalDemand = 0.0;
        double doubleval = 0.0;
        String scheme_Name = CommonUtil.convertObjToStr(tblTransaction.getValueAt(selectRow, 1));
        String acNo = CommonUtil.convertObjToStr(tblTransaction.getValueAt(selectRow, 3));
        String prodType = CommonUtil.convertObjToStr(tblTransaction.getValueAt(selectRow, 1));
        prod_id = CommonUtil.convertObjToStr(tblTransaction.getValueAt(selectRow, 12));
        String description = CommonUtil.convertObjToStr(tblTransaction.getValueAt(selectRow, 2));
        //  String acNo = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(),1));
        double transAmt = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 4)).doubleValue();
        doubleval = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 4)).doubleValue();
        double principal = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 5)).doubleValue();
        double interest = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 7)).doubleValue();
        double penal = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 6)).doubleValue();
        double charges = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 9)).doubleValue();
        double totalAmt = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 13)).doubleValue();//13
        double clearBal = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 14)).doubleValue();//12
        clearBal = Math.abs(clearBal);
        System.out.println("");
        int selectedRow = selectRow;
        //System.out.println("totalAmt === " + totalAmt + " clearBal =====" + clearBal + " transAmt ===" + transAmt);
        if (clearBal < transAmt) {
            transAmt = doubleval;
        }
        if (transAmt <= 0) {
//            double principal1 = 0;
//            double interest1 = 0;
//            double penal1 = 0;
//            double charges1 = 0;
            for (int i = 0; i < globalList.size(); i++) {
                ArrayList aList1 = (ArrayList) globalList.get(i);
                for (int j = 0; j < aList1.size(); j++) {
                    String PID = aList1.get(11).toString();
                    if (PID.equals(prod_id)) {
                        principal = Double.parseDouble(aList1.get(4).toString());
                        interest = Double.parseDouble(aList1.get(6).toString());
                        penal = Double.parseDouble(aList1.get(5).toString());
                        charges = Double.parseDouble(aList1.get(8).toString());
                    }
                }
            }
            tblTransaction.setValueAt(new Double(principal), selectRow, 5);
            tblTransaction.setValueAt(new Double(interest), selectRow, 7);
            tblTransaction.setValueAt(new Double(penal), selectRow, 6);
            tblTransaction.setValueAt(new Double(charges), selectRow, 9);
        } else {
            double paidPrincipal = 0;
            double paidInterest = 0;
            double paidPenal = 0;
            double paidCharges = 0;
            System.out.println("LOAN scheme_Name=" + scheme_Name + " prod_id=" + prod_id + " acNo=" + acNo + " transAmt=" + transAmt);
            for (int i = 0; i < globalList.size(); i++) {
                ArrayList aList1 = (ArrayList) globalList.get(i);
                for (int j = 0; j < aList1.size(); j++) {
                    String PID = aList1.get(11).toString();
                    if (PID.equals(prod_id) && selectRow == j) {
                        System.out.println("inside setting value");
                        principal = Double.parseDouble(aList1.get(4).toString());
                        interest = Double.parseDouble(aList1.get(6).toString());
                        penal = Double.parseDouble(aList1.get(5).toString());
                        charges = Double.parseDouble(aList1.get(8).toString());
                        System.out.println("prin"+principal+"inter"+interest+"pena"+penal+"cahr"+charges);
                    }
                }
            }
            try {
                double currInt = 0.0;
                if (key.equals("3") || key.equals("0")) {
                    System.out.println("inside key 3");
                    //               double transAmt=Double.parseDouble(loanAmt);
                    HashMap ALL_LOAN_AMOUNT = new HashMap();
                    HashMap hashList = new HashMap();
                    hashList.put(CommonConstants.MAP_WHERE, prod_id);
                    List appList = ClientUtil.executeQuery("selectAppropriatTransaction", hashList);
                    HashMap appropriateMap = new HashMap();
                    if (appList != null && appList.size() > 0) {
                        appropriateMap = (HashMap) appList.get(0);
                        appropriateMap.remove("PROD_ID");
                    } else {
                        throw new TTException("Please Enter Hierachy of Transaction  in This Product ");
                    }
                    //System.out.println("appropriateMap####" + appropriateMap);
                    java.util.Collection collectedValues = appropriateMap.values();
                    System.out.println("collectedValues"+collectedValues);
                    java.util.Iterator it = collectedValues.iterator();
                    //CashTransactionTO objCashTO =new CashTransactionTO();
                    int appTranValue = 0;
                    while (it.hasNext()) {
                        appTranValue++;
                        String hierachyValue = CommonUtil.convertObjToStr(it.next());
                        if (hierachyValue.equals("CHARGES")) {
                            if (transAmt > 0 && charges > 0) {
                                if (transAmt >= charges) {
                                    transAmt -= charges;
                                    principal = transAmt;
                                } else {
                                    charges = transAmt;
                                    transAmt -= charges;
                                }
                            } else {
                                charges = 0;
                            }
                        }
                        if (hierachyValue.equals("PENALINTEREST")) {
                            //penal interest
                            if (transAmt > 0 && penal > 0) {
                                if (transAmt >= penal) {
                                    transAmt -= penal;
                                    principal = transAmt;
                                } else {
                                    penal = transAmt;
                                    transAmt -= penal;
                                }
                            } else {
                                penal = 0;
                            }
                        }
                        if (hierachyValue.equals("INTEREST")) {
                            //interest
                            if (transAmt > 0 && interest > 0) {
                                if (transAmt >= interest) {
                                    transAmt -= interest;
                                    principal = transAmt; 
                               } else {
                                    interest = transAmt;
                                    transAmt -= interest;
                                }
                            } else {
                                interest = 0;
                            }
                        }
                        if (hierachyValue.equals("PRINCIPAL")) {
                            if (transAmt > 0 && principal > 0) {
                                if (doubleval >= (clearBal+penal+interest)) {
                                    principal = clearBal;
                                    interest = doubleval - clearBal - penal;
                                } else {
                                    principal = transAmt;
                                    transAmt -= principal;
                                }
                            } else {
                                principal = 0;
                            }
                        }
                    }
                    currInt = interest;
                }
                System.out.println("after calculation penalAmount" + paidPenal + "PrincipleAmount" + paidPrincipal + "InterestAmount" + paidInterest+"TransAmount"+transAmt);
                //end of key 3 
                //System.out.println("transAmt INOOOOOOOOOOOOO============================" + transAmt);
                if (totalAmt <= 0 && transAmt <= clearBal) {
                    if (key.equals("3")) {
                        tblTransaction.setValueAt(new Double(transAmt), selectRow, 5);
                    }
                } else {
                    if (key.equals("3")) {
//                        if (clearBal == transAmt) {
//                            tblTransaction.setValueAt(new Double(paidPrincipal), tblTransaction.getSelectedRow(), 4);
//                        }
                        tblTransaction.setValueAt(new Double(principal), tblTransaction.getSelectedRow(), 5);
                        tblTransaction.setValueAt(new Double(interest), selectRow, 7);
                        tblTransaction.setValueAt(new Double(penal), selectRow, 6);
                        tblTransaction.setValueAt(new Double(charges), selectRow, 9);
                        //System.out.println("paidInterest ==" + paidInterest + " paidPenal==" + paidPenal);
                    }
                }
                paidPrincipal = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 5)).doubleValue();
                paidInterest = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 7)).doubleValue();
                paidPenal = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 6)).doubleValue();
                paidCharges = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 9)).doubleValue();
                transAmt = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(selectRow, 4));
                System.out.println("penalAmount" + paidPenal + "PrincipleAmount" + paidPrincipal + "InterestAmount" + paidInterest+"TransAmount"+transAmt);
                HashMap dataMap = null;
                if ((column == 0 || column == 4)) {
                    dataMap = new HashMap();
                    if (paidCharges > 0) {
                        dataMap.put("CHARGES", String.valueOf(paidCharges));
                    } else {
                        dataMap.put("CHARGES", "0");
                    }
                    dataMap.put("PRINCIPAL", String.valueOf(paidPrincipal));
                    dataMap.put("INTEREST", String.valueOf(paidInterest));
                    dataMap.put("PENAL", String.valueOf(paidPenal));
                    //System.out.println("paidPrincipal####" + paidPrincipal + "paidInterest ==" + paidInterest + "paidPenal==" + paidPenal + "paidCharges==" + paidCharges);
                    totalDemand = paidPrincipal + paidInterest + paidPenal + paidCharges;
                    //System.out.println("totalDemand==" + totalDemand);
                    dataMap.put("TOTAL_DEMAND", new Double(totalDemand));
                    dataMap.put("PROD_TYPE", prodType);
                    dataMap.put("ACT_NUM", acNo);
                    dataMap.put("RECOVERED_AMOUNT", doubleval);
                    dataMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
                    dataMap.put("USER_ID", TrueTransactMain.USER_ID);
                    dataMap.put("PROD_ID", prod_id);
                    dataMap.put("CLOCK_NO", txtClockNo.getText());
                    dataMap.put("MEMBER_NO", txtMemberNo.getText());
                    dataMap.put("CUST_NAME", txtName.getText());
                    dataMap.put("TOT_PRIN", txtTotPrincipal.getText());
                    dataMap.put("TOT_PENAL", txtTotPenel.getText());
                    dataMap.put("TOT_INT", txtTotInterest.getText());
                    dataMap.put("TOT_OTHERS", txtTotOthers.getText());
                    dataMap.put("TOT_GRAND", txtGrandTotal.getText());
                    dataMap.put("NOTICE_AMOUNT", new Double(0));
                    dataMap.put("ARBITRATION_AMOUNT", new Double(0));
                    dataMap.put("BONUS", new Double(0));
                    dataMap.put("PROD_DESCRIPTION",description);
                    dataMap.put("AMTORNOOFINST", doubleval);
                    //System.out.println("CommonConstants.TOSTATUS_INSERT==" + CommonConstants.TOSTATUS_INSERT);
                    dataMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                    dataMap.put("TOTAL", doubleval);
                    if (termLoanMap == null) {
                        termLoanMap = new HashMap();
                    }
                    //System.out.println("doubleval==" + doubleval);
                    if (doubleval > 0) {
                        termLoanMap = new HashMap();
                        termLoanMap.put("TL", dataMap);
                        if (chkRetired != null && chkRetired.isSelected()) {
                            transDetailsUI.setTransDetails("TL", ProxyParameters.BRANCH_ID, acNo);
                            termLoanMap.put("TOTAL_AMOUNT", transDetailsUI.getTermLoanCloseCharge());
                        }
                        //System.out.println("acNo==" + acNo + "termLoanMap =====" + termLoanMap);
                        finalMap.put(acNo, termLoanMap);
                    }
                    System.out.println("finalMAAAAAAP"+finalMap);
                }
                dataMap = null;
                termLoanMap = null;
                //System.out.println("finalMap 8888888: " + finalMap);
            } catch (Exception e) {
                //System.out.println("Exception in getLoanDetails: " + e);
            }
        }
//        } else {
//            displayAlert("Please enter the amount less than " + clearBal);
//            //return;
//        }
    }
    
    public void TLGetDetails(int column,String scheme){
        double penalAmt = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 6));
        double principalAmt = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 5));
        double transAmt = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 4));
        double interestAmt = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 7));
        System.out.println("penal" + penalAmt + "Principle" + principalAmt + "transAmt" + transAmt +"interestAmt"+interestAmt);
        //double newPrinciple = transAmt - (penalAmt + interestAmt);
        //tblTransaction.setValueAt(CommonUtil.convertObjToStr(newPrinciple), tblTransaction.getSelectedRow(), 5);
        if(column == 6 && flag == false){
            flag = true;
//            interestAmt = transAmt - (penalAmt + principalAmt);
//            tblTransaction.setValueAt(CommonUtil.convertObjToStr(interestAmt), tblTransaction.getSelectedRow(), 7);
            if(transAmt == penalAmt){
                interestAmt = 0.0;
                principalAmt = 0.0;
            }else if(transAmt == interestAmt){
                penalAmt = 0.0;
                principalAmt = 0.0;
            }else if(transAmt == principalAmt){
                interestAmt = 0.0;
                penalAmt = 0.0;
            }else{
                if (scheme.equals("TL") || scheme.equals("AD")) {
                    double doubleval = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 4));
                    double principal = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 5)).doubleValue();
                    double interest = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 7)).doubleValue();
                    double penal = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 6)).doubleValue();
                    double charges = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 9)).doubleValue();
                    double totalAmt = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 13)).doubleValue();//13
                    double clearBal = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 14)).doubleValue();//12        
                    String prod_id = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 12));
                    clearBal = Math.abs(clearBal);
                    for (int i = 0; i < globalList.size(); i++) {
                        ArrayList aList1 = (ArrayList) globalList.get(i);
                        for (int j = 0; j < aList1.size(); j++) {
                            String PID = aList1.get(11).toString();
                            if (PID.equals(prod_id) && tblTransaction.getSelectedRow() == j) {
                                System.out.println("inside setting value");
                                principal = Double.parseDouble(aList1.get(4).toString());
                                interest = Double.parseDouble(aList1.get(6).toString());
                                penal = Double.parseDouble(aList1.get(5).toString());
                                charges = Double.parseDouble(aList1.get(8).toString());
                                System.out.println("prin"+principal+"inter"+interest+"pena"+penal+"cahr"+charges);
                            }
                        }
                    }
                    if (doubleval >= (clearBal+interest+penal)) {
                        principalAmt = clearBal;
                        penalAmt = doubleval - clearBal - interestAmt;
                    } else {
                        principalAmt = transAmt - (penalAmt + interestAmt);
                    }
                }else{
                    principalAmt = transAmt - (penalAmt + interestAmt);
                }
            }
//            principalAmt = transAmt - (penalAmt + interestAmt);
            tblTransaction.setValueAt(CommonUtil.convertObjToStr(interestAmt), tblTransaction.getSelectedRow(), 7);
            tblTransaction.setValueAt(CommonUtil.convertObjToStr(penalAmt), tblTransaction.getSelectedRow(), 6);
            tblTransaction.setValueAt(CommonUtil.convertObjToStr(principalAmt), tblTransaction.getSelectedRow(), 5);
        }
        if(column == 7 && flag == false){
            flag = true;
            if(transAmt == penalAmt){
                interestAmt = 0.0;
                principalAmt = 0.0;
            }else if(transAmt == interestAmt){
                penalAmt = 0.0;
                principalAmt = 0.0;
            }else if(transAmt == principalAmt){
                interestAmt = 0.0;
                penalAmt = 0.0;
            }else{
                if (scheme.equals("TL") || scheme.equals("AD")) {
                    double doubleval = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 4));
                    double principal = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 5)).doubleValue();
                    double interest = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 7)).doubleValue();
                    double penal = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 6)).doubleValue();
                    double charges = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 9)).doubleValue();
                    double totalAmt = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 13)).doubleValue();//13
                    double clearBal = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 14)).doubleValue();//12        
                    String prod_id = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 12));
                    clearBal = Math.abs(clearBal);
                    for (int i = 0; i < globalList.size(); i++) {
                        ArrayList aList1 = (ArrayList) globalList.get(i);
                        for (int j = 0; j < aList1.size(); j++) {
                            String PID = aList1.get(11).toString();
                            if (PID.equals(prod_id) && tblTransaction.getSelectedRow() == j) {
                                System.out.println("inside setting value");
                                principal = Double.parseDouble(aList1.get(4).toString());
                                interest = Double.parseDouble(aList1.get(6).toString());
                                penal = Double.parseDouble(aList1.get(5).toString());
                                charges = Double.parseDouble(aList1.get(8).toString());
                                System.out.println("prin"+principal+"inter"+interest+"pena"+penal+"cahr"+charges);
                            }
                        }
                    }
                    if (doubleval >= (clearBal+interest+penal)) {
                        principalAmt = clearBal;
                        interestAmt = doubleval - clearBal - penal;
                    } else {
                        principalAmt = transAmt - (penalAmt + interestAmt);
                    }
                }else{
                    principalAmt = transAmt - (penalAmt + interestAmt);
                }
            }
//            principalAmt = transAmt - (penalAmt + interestAmt);
            tblTransaction.setValueAt(CommonUtil.convertObjToStr(interestAmt), tblTransaction.getSelectedRow(), 7);
            tblTransaction.setValueAt(CommonUtil.convertObjToStr(penalAmt), tblTransaction.getSelectedRow(), 6);
            tblTransaction.setValueAt(CommonUtil.convertObjToStr(principalAmt), tblTransaction.getSelectedRow(), 5);
        }
        double totalDemand = 0.0;
        HashMap dataMap = null;
        dataMap = new HashMap();
        String description = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 2));
        double doubleval = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 4));
        String prod_id = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 12));
        String acNo = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 3));
        String prodType = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 1));
        dataMap.put("CHARGES", "0");
        dataMap.put("PRINCIPAL", String.valueOf(principalAmt));
        dataMap.put("INTEREST", String.valueOf(interestAmt));
        dataMap.put("PENAL", String.valueOf(penalAmt));
        //System.out.println("paidPrincipal####" + paidPrincipal + "paidInterest ==" + paidInterest + "paidPenal==" + paidPenal + "paidCharges==" + paidCharges);
        totalDemand = principalAmt + interestAmt + penalAmt;
        //System.out.println("totalDemand==" + totalDemand);
        dataMap.put("TOTAL_DEMAND", new Double(totalDemand));
        dataMap.put("PROD_TYPE", prodType);
        dataMap.put("ACT_NUM", acNo);
        dataMap.put("RECOVERED_AMOUNT", doubleval);
        dataMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
        dataMap.put("USER_ID", TrueTransactMain.USER_ID);
        dataMap.put("PROD_ID", prod_id);
        dataMap.put("CLOCK_NO", txtClockNo.getText());
        dataMap.put("MEMBER_NO", txtMemberNo.getText());
        dataMap.put("CUST_NAME", txtName.getText());
        dataMap.put("TOT_PRIN", txtTotPrincipal.getText());
        dataMap.put("TOT_PENAL", txtTotPenel.getText());
        dataMap.put("TOT_INT", txtTotInterest.getText());
        dataMap.put("TOT_OTHERS", txtTotOthers.getText());
        dataMap.put("TOT_GRAND", txtGrandTotal.getText());
        dataMap.put("NOTICE_AMOUNT", new Double(0));
        dataMap.put("ARBITRATION_AMOUNT", new Double(0));
        dataMap.put("BONUS", new Double(0));
        dataMap.put("AMTORNOOFINST", doubleval);
        dataMap.put("PROD_DESCRIPTION",description);
        //System.out.println("CommonConstants.TOSTATUS_INSERT==" + CommonConstants.TOSTATUS_INSERT);
        dataMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
        dataMap.put("TOTAL", doubleval);
        if (termLoanMap == null) {
            termLoanMap = new HashMap();
        }
        //System.out.println("doubleval==" + doubleval);
        if (doubleval > 0) {
            termLoanMap = new HashMap();
            termLoanMap.put("TL", dataMap);
            if (chkRetired != null && chkRetired.isSelected()) {
                transDetailsUI.setTransDetails("TL", ProxyParameters.BRANCH_ID, acNo);
                termLoanMap.put("TOTAL_AMOUNT", transDetailsUI.getTermLoanCloseCharge());
            }
            //System.out.println("acNo==" + acNo + "termLoanMap =====" + termLoanMap);
            finalMap.put(acNo, termLoanMap);
         }

        dataMap = null;
        termLoanMap = null;
    }
    //edited by Nidhin when mouse clicked
    public void calcEachChittal1(String ent0, String ent1, String ent2, String ent3, String ent4, String ent5, String ent6, String ent7, String ent8, String ent9, String ent10, String ent11, String ent12, String ent13) {
        try {
            System.out.println("2NDDDDD");
            String chittalNo = "", subNo = "";
            String calculateIntOn = "";
            //VALUES\
            String scheme_Name = CommonUtil.convertObjToStr(ent0);
            String prodDescription = CommonUtil.convertObjToStr(ent1);
            String acNo = CommonUtil.convertObjToStr(ent2);
            String prodType = CommonUtil.convertObjToStr(ent0);
            String prod_id = CommonUtil.convertObjToStr(ent11);
            String notice = CommonUtil.convertObjToStr(ent9);
            String arb = CommonUtil.convertObjToStr(ent10);
            double noOfInst = CommonUtil.convertObjToDouble(ent3).doubleValue();
            double principal = CommonUtil.convertObjToDouble(ent4).doubleValue();
            double interest = CommonUtil.convertObjToDouble(ent6).doubleValue();
            double penalO = CommonUtil.convertObjToDouble(ent5).doubleValue();
            double charges = CommonUtil.convertObjToDouble(ent8).doubleValue();
            double totalInst = CommonUtil.convertObjToDouble(ent13).doubleValue();
            double clear_bal = CommonUtil.convertObjToDouble(ent12).doubleValue();
            //

            String prId = CommonUtil.convertObjToStr(prod_id);
            chittalNo = CommonUtil.convertObjToStr(acNo);
            HashMap dataMap = new HashMap();
            dataMap.put("ACT_NUM", chittalNo);
            //  if (scheme_Name.equals("MDS"))
            //    {
            if (chittalNo.indexOf("_") != -1) {
                subNo = chittalNo.substring(chittalNo.indexOf("_") + 1, chittalNo.length());
                chittalNo = chittalNo.substring(0, chittalNo.indexOf("_"));
            } else {
                subNo = "1";
            }

            //   }
            String noOfInsPay = CommonUtil.convertObjToStr(noOfInst);
            //String  prod_id= CommonUtil.convertObjToStr(prod_id);
            double pendDueIn = CommonUtil.convertObjToDouble(clear_bal);
            double currIn = CommonUtil.convertObjToDouble(noOfInsPay).doubleValue();
            double remainInst = pendDueIn - currIn;
            //System.out.println("remainInst ====================== " + remainInst);
            if (remainInst < 0) {
                remainInst = 0;
            }
            //For Loop
        /*    int selectedRow = tblTransaction.getSelectedRow();
             double principal1=0;double interest1=0;double penal1=0;double charges1=0;double bonus1=0;
             for(int i = 0;i<globalList.size();i++){
             ArrayList aList1=(ArrayList)globalList.get(i);
             for(int j = 0;j<aList1.size();j++){
             String PID=aList1.get(11).toString();
             if(PID.equals(prod_id)) {
             //  if(aList.get(3)!=null)
             principal1=CommonUtil.convertObjToDouble(aList1.get(4).toString()).doubleValue();
             //  if(aList.get(5)!=null)
             interest1=CommonUtil.convertObjToDouble(aList1.get(6).toString()).doubleValue();
             //   if(aList.get(4)!=null)
             penal1=CommonUtil.convertObjToDouble(aList1.get(5).toString()).doubleValue();
             //  if(aList.get(7)!=null)
             charges1=CommonUtil.convertObjToDouble(aList1.get(8).toString()).doubleValue();
             bonus1=CommonUtil.convertObjToDouble(aList1.get(7).toString()).doubleValue();
             // displayAlert("principal ="+principal +" interest="+interest+" penal="+penal);
             }
             }
             }*/
            /* //System.out.println("#####noOfInsPayst="+noOfInsPay);
             if(noOfInsPay==null || noOfInsPay.equals("")) {
             //System.out.println("#####I(*&&^%%%$#$$#@##@@22222222222222222="+noOfInsPay);
             tblTransaction.setValueAt(principal1, selectedRow, 4);
             tblTransaction.setValueAt(penal1, selectedRow, 5);//penalAmount
             tblTransaction.setValueAt(interest1, selectedRow, 6);//instAmount
             tblTransaction.setValueAt(bonus1, selectedRow, 7);
             return;
             }*/
            //End

            String noticAmt = CommonUtil.convertObjToStr(notice);
            String arbitAmt = CommonUtil.convertObjToStr(arb);
            String insDue = "";

            HashMap pendingMap = new HashMap();
            pendingMap.put("SCHEME_NAME", prId);
            pendingMap.put("CHITTAL_NO", chittalNo);
            //System.out.println("######### subNo" + subNo);
            pendingMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
            //            //System.out.println("######### pendingMap"+pendingMap);
            List pendingAuthlst = ClientUtil.executeQuery("checkPendingForAuthorization", pendingMap);
            //System.out.println("######### pendingAuthlst=" + pendingAuthlst.size());
            //System.out.println("######### noOfInsPay=" + noOfInsPay);
            if (pendingAuthlst != null && pendingAuthlst.size() > 0) {
                //   ClientUtil.showMessageWindow(" Transaction pending for this Chittal... Please Authorize OR Reject first  !!! ");
                //    tblTransaction.setValueAt("", selectedRow, 3);
            } else if (CommonUtil.convertObjToDouble(noOfInsPay).doubleValue() >= 1) {
                //System.out.println("######### noOfInsPay IN=" + noOfInsPay);
                HashMap whereMap = new HashMap();

                HashMap productMap = new HashMap();

                long diffDayPending = 0;
                int noOfInsPaid = 0;
                Date currDate = (Date) curr_dt.clone();
                Date instDate = null;
                boolean bonusAvailabe = true;
                long noOfInstPay = CommonUtil.convertObjToLong(noOfInsPay);
                int instDay = 1;
                int totIns = 0;
                Date startDate = null;
                Date endDate = null;
                Date insDate = null;
                int startMonth = 0;
                int insMonth = 0;
                int curInsNo = 0;
                HashMap installmentMap = new HashMap();
                whereMap.put("SCHEME_NAME", prId);
                List lst = ClientUtil.executeQuery("getSelectSchemeAcctHead", whereMap);
                //System.out.println("#######lst ===" + lst.size() + " LST----" + lst);
                if (lst != null && lst.size() > 0) {
                    productMap = (HashMap) lst.get(0);
                    totIns = CommonUtil.convertObjToInt(productMap.get("NO_OF_INSTALLMENTS"));
                    startDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(productMap.get("SCHEME_START_DT")));
                    endDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(productMap.get("SCHEME_END_DT")));
                    insDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(productMap.get("SCHEME_START_DT")));
                    startMonth = insDate.getMonth();
                    // Added by Rajesh For checking BONUS_FIRST_INSTALLMENT. Based on this for loop initial value will be changed for Penal calculation.
                    //                    String bonusFirstInst = CommonUtil.convertObjToStr(productMap.get("BONUS_FIRST_INSTALLMENT"));
                    String bonusFirstInst = CommonUtil.convertObjToStr(productMap.get("ADVANCE_COLLECTION"));
                    int startNoForPenal = 0;
                    int addNo = 1;
                    int firstInst_No = -1;
                    if (bonusFirstInst.equals("Y")) {
                        startNoForPenal = 1;
                        addNo = 0;
                        firstInst_No = 0;
                    }
                    bonusAvailabe = true;
                    double insAmt = 0.0;
                    //                    double insAmt = CommonUtil.convertObjToDouble(productMap.get("INSTALLMENT_AMOUNT")).doubleValue();
                    long pendingInst = 0;
                    int divNo = 0;
                    long count = 0;
                    long insDueAmt = 0;
                    whereMap.put("CHITTAL_NO", chittalNo);
                    whereMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
                    List insList = ClientUtil.executeQuery("getNoOfInstalmentsPaid", whereMap);
                    //System.out.println("#######insList ===" + insList.size() + "insList ===" + insList);
                    if (insList != null && insList.size() > 0) {
                        whereMap = (HashMap) insList.get(0);
                        noOfInsPaid = CommonUtil.convertObjToInt(whereMap.get("NO_OF_INST"));
                        count = CommonUtil.convertObjToLong(whereMap.get("NO_OF_INST"));
                    }

                    HashMap chittalMap = new HashMap();
                    chittalMap.put("CHITTAL_NO", chittalNo);
                    chittalMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
                    List chitLst = ClientUtil.executeQuery("getSelctApplnReceiptDetails", chittalMap);
                    //System.out.println("#######chitLst ===" + chitLst.size() + "chitLst ===" + chitLst);
                    if (chitLst != null && chitLst.size() > 0) {
                        chittalMap = (HashMap) chitLst.get(0);
                        //System.out.println("#######chittalMap ===" + chittalMap);
                        instDay = CommonUtil.convertObjToInt(chittalMap.get("INSTALLMENT_DAY"));
                        divNo = CommonUtil.convertObjToInt(chittalMap.get("DIVISION_NO"));
                        dataMap.put("BRANCH_CODE", CommonUtil.convertObjToStr(chittalMap.get("BRANCH_CODE")));
                        insAmt = CommonUtil.convertObjToDouble(chittalMap.get("INST_AMT")).doubleValue();
                    }

                    HashMap insDateMap = new HashMap();
                    insDateMap.put("DIVISION_NO", String.valueOf(divNo));
                    insDateMap.put("SCHEME_NAME", prId);
                    insDateMap.put("CURR_DATE", curr_dt.clone());
                    //                    insDateMap.put("ADD_MONTHS", "0");
                    insDateMap.put("ADD_MONTHS", "-1");
                    List insDateLst = ClientUtil.executeQuery("getTransAllMDSCurrentInsDate", insDateMap);
                    //System.out.println("#######insDateLst ===" + insDateLst.size() + "insDateLst ===" + insDateLst);
                    if (insDateLst != null && insDateLst.size() > 0) {
                        insDateMap = (HashMap) insDateLst.get(0);
                        //System.out.println("#######insDateMap ===" + insDateMap);
                        curInsNo = CommonUtil.convertObjToInt(insDateMap.get("INSTALLMENT_NO"));
                        pendingInst = curInsNo - noOfInsPaid;
                        if (instDay < currDate.getDate()) {
                            pendingInst = pendingInst + 1;
                        } else {
                            pendingInst = pendingInst;
                        }
                        if (pendingInst < 0) {
                            pendingInst = 0;
                        }
                        insMonth = startMonth + curInsNo;
                        insDate.setMonth(insMonth);
                    }

                    HashMap prizedMap = new HashMap();
                    double bonusAval = 0;
                    prizedMap.put("SCHEME_NAME", prId);
                    prizedMap.put("DIVISION_NO", String.valueOf(divNo));
                    prizedMap.put("CHITTAL_NO", chittalNo);
                    prizedMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
                    lst = ClientUtil.executeQuery("getSelectPrizedDetailsEntryRecords", prizedMap);
                    //System.out.println("#######prizedMap ===" + lst.size() + "prizedMap ===" + lst);
                    if (lst != null && lst.size() > 0) {
                        prizedMap = (HashMap) lst.get(0);
                        if (prizedMap.get("DRAW") != null && !prizedMap.get("DRAW").equals("") && prizedMap.get("DRAW").equals("Y")) {
                            setRdoPrizedMember_Yes(true);
                        }
                        if (prizedMap.get("AUCTION") != null && !prizedMap.get("AUCTION").equals("") && prizedMap.get("AUCTION").equals("Y")) {
                            setRdoPrizedMember_Yes(true);
                        }
                        bonusAval = CommonUtil.convertObjToDouble(prizedMap.get("PRIZED_AMOUNT"));
                    } else {
                        setRdoPrizedMember_No(true);
                    }
                    //System.out.println("#######totIns 2===" + totIns + " noOfInsPaid==" + noOfInsPaid);
                    int balanceIns = totIns - noOfInsPaid;
                    //System.out.println("balanceInsTrans" + balanceIns);
                    if (balanceIns >= noOfInstPay) {
                        long totDiscAmt = 0;
                        long penalAmt = 0;
                        double netAmt = 0;
                        double insAmtPayable = 0;
                        double totBonusAmt = 0;
                        double bonusAmt = 0;
                        String penalIntType = "";
                        long penalValue = 0;
                        String penalGraceType = "";
                        long penalGraceValue = 0;
                        String penalCalcBaseOn = "";
                        //System.out.println("pendingInstTrans" + pendingInst);
                        if (pendingInst > 0) {              //pending installment calculation starts...
                            insDueAmt = (long) insAmt * pendingInst;
                            int totPendingInst = (int) pendingInst;
                            double calc = 0;
                            long totInst = pendingInst;
                            //System.out.println("Productmap Trans " + productMap);
                            penalCalcBaseOn = CommonUtil.convertObjToStr(productMap.get("PENAL_CALC"));
                            //System.out.println("#######penalCalcBaseOn ===" + penalCalcBaseOn);
                            if (getRdoPrizedMember_Yes() == true) {
                                if (productMap.containsKey("PENEL_PZ_INT_FULL_AMT_INST_AMT") && productMap.get("PENEL_PZ_INT_FULL_AMT_INST_AMT") != null) {
                                    calculateIntOn = CommonUtil.convertObjToStr(productMap.get("PENEL_PZ_INT_FULL_AMT_INST_AMT"));
                                }
                                penalIntType = CommonUtil.convertObjToStr(productMap.get("PENAL_PRIZED_INT_TYPE"));
                                penalValue = CommonUtil.convertObjToLong(productMap.get("PENAL_PRIZED_INT_AMT"));
                                penalGraceType = CommonUtil.convertObjToStr(productMap.get("PENAL_PRIZED_GRACE_PERIOD_TYPE"));
                                penalGraceValue = CommonUtil.convertObjToLong(productMap.get("PENAL_PRIZED_GRACE_PERIOD"));
                            } else if (getRdoPrizedMember_No() == true) {
                                if (productMap.containsKey("PENEL_INT_FULL_AMT_INST_AMT") && productMap.get("PENEL_INT_FULL_AMT_INST_AMT") != null) {
                                    calculateIntOn = CommonUtil.convertObjToStr(productMap.get("PENEL_INT_FULL_AMT_INST_AMT"));
                                }
                                penalIntType = CommonUtil.convertObjToStr(productMap.get("PENAL_INT_TYPE"));
                                penalValue = CommonUtil.convertObjToLong(productMap.get("PENAL_INT_AMT"));
                                penalGraceType = CommonUtil.convertObjToStr(productMap.get("PENAL_GRACE_PERIOD_TYPE"));
                                penalGraceValue = CommonUtil.convertObjToLong(productMap.get("PENAL_GRACE_PERIOD"));
                            }
                            List bonusAmout = new ArrayList();
                            System.out.println("calculateIntOn" + calculateIntOn + " inst amt :" + productMap.get("INSTALLMENT_AMOUNT"));
                            if (calculateIntOn != null && calculateIntOn.equals("Installment Amount")) {
                                //double instAmount = 0.0;
                                HashMap nextInstMaps = null;
                                for (int i = startNoForPenal; i <= noOfInstPay - addNo; i++) {
                                    nextInstMaps = new HashMap();
                                    nextInstMaps.put("SCHEME_NAME", prId);
                                    nextInstMaps.put("DIVISION_NO", divNo);
                                    nextInstMaps.put("SL_NO", new Double(i + noOfInsPaid + addNo + firstInst_No));
                                    List listRec = ClientUtil.executeQuery("getSelectBonusAndNextInstDateWithoutDivision", nextInstMaps);
                                    if (listRec != null && listRec.size() > 0) {
                                        nextInstMaps = (HashMap) listRec.get(0);
                                    }
                                    System.out.println("nextInstMaps" + nextInstMaps);
                                    if (nextInstMaps != null && nextInstMaps.containsKey("NEXT_BONUS_AMOUNT")) {
                                        bonusAmout.add(CommonUtil.convertObjToDouble(nextInstMaps.get("NEXT_BONUS_AMOUNT")));
                                    } else {
                                        bonusAmout.add(CommonUtil.convertObjToDouble(0));
                                    }
                                }
                            }
                            for (int j = startNoForPenal; j < noOfInstPay + startNoForPenal; j++) {
                                if (calculateIntOn != null && calculateIntOn.equals("Installment Amount")) {
                                    insAmt = 0.0;
                                    double instAmount = CommonUtil.convertObjToDouble(productMap.get("INSTALLMENT_AMOUNT"));
                                    if (bonusAmout != null && bonusAmout.size() > 0) {
                                        instAmount -= CommonUtil.convertObjToDouble(bonusAmout.get(j - 1));
                                    }
                                    insAmt = instAmount;
                                }
                                HashMap nextInstMap = new HashMap();
                                nextInstMap.put("SCHEME_NAME", prId);
                                nextInstMap.put("DIVISION_NO", String.valueOf(divNo));
                                nextInstMap.put("SL_NO", new Double(j + noOfInsPaid));
                                List listRec = ClientUtil.executeQuery("getSelectNextInstDate", nextInstMap);
                                if (listRec != null && listRec.size() > 0) {
                                    double penal = 0;
                                    nextInstMap = (HashMap) listRec.get(0);
                                    instDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextInstMap.get("NEXT_INSTALLMENT_DATE")));
                                    if (instDay > 0) {
                                        instDate.setDate(instDate.getDate() + instDay - 1);
                                    }
                                    diffDayPending = DateUtil.dateDiff(instDate, currDate);
                                    //Holiday Checking - Added By Suresh
                                    HashMap holidayMap = new HashMap();
                                    boolean checkHoliday = true;
                                    //System.out.println("instDate   " + instDate);
                                    instDate = setProperDtFormat(instDate);
                                    //System.out.println("instDate   " + instDate);
                                    holidayMap.put("NEXT_DATE", instDate);
                                    holidayMap.put("BRANCH_CODE", getSelectedBranchID());
                                    while (checkHoliday) {
                                        boolean tholiday = false;
                                        List Holiday = ClientUtil.executeQuery("checkHolidayDateOD", holidayMap);
                                        List weeklyOf = ClientUtil.executeQuery("checkWeeklyOffOD", holidayMap);
                                        boolean isHoliday = Holiday.size() > 0 ? true : false;
                                        boolean isWeekOff = weeklyOf.size() > 0 ? true : false;
                                        if (isHoliday || isWeekOff) {
                                            //System.out.println("#### diffDayPending Holiday True : " + diffDayPending);
                                            if (CommonUtil.convertObjToStr(productMap.get("HOLIDAY_INT")).equals("any next working day")) {
                                                diffDayPending -= 1;
                                                instDate.setDate(instDate.getDate() + 1);
                                            } else {
                                                diffDayPending += 1;
                                                instDate.setDate(instDate.getDate() - 1);
                                            }
                                            holidayMap.put("NEXT_DATE", instDate);
                                            checkHoliday = true;
                                            //System.out.println("#### holidayMap : " + holidayMap);
                                        } else {
                                            //System.out.println("#### diffDay Holiday False : " + diffDayPending);
                                            checkHoliday = false;
                                        }
                                    }
                                    //System.out.println("#### diffDayPending Final : " + diffDayPending);
                                    if (penalCalcBaseOn != null && !penalCalcBaseOn.equals("") && penalCalcBaseOn.equals("Days")) {
                                        if (penalGraceType != null && !penalGraceType.equals("") && penalGraceType.equals("Days")) {
                                            if (diffDayPending > penalGraceValue) {
                                                if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Percent")) {
                                                    calc += (diffDayPending * penalValue * insAmt) / 36500;
                                                } else if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Absolute")) {
                                                    calc += penalValue;
                                                }
                                            }
                                        } else if (penalGraceType != null && !penalGraceType.equals("") && penalGraceType.equals("Installments")) {
                                            // To be written
                                            if (diffDayPending > penalGraceValue) {
                                                if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Percent")) {
                                                    //                                                calc = insAmt*(((pendingInst+1)*pendingInst/2)*penalValue)/1200;
                                                    calc += (double) ((insAmt * penalValue) / 1200.0) * pendingInst--;
                                                } else if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Absolute")) {
                                                    calc += penalValue;
                                                }
                                            }
                                        }
                                    } else if (penalCalcBaseOn != null && !penalCalcBaseOn.equals("") && penalCalcBaseOn.equals("Installments")) {
                                        if (penalGraceType != null && !penalGraceType.equals("") && penalGraceType.equals("Days")) {
                                            if (diffDayPending > penalGraceValue) {
                                                if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Percent")) {
                                                    calc += ((insAmt * penalValue) / 1200.0) * pendingInst--;
                                                } else if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Absolute") && totInst <= noOfInstPay) {
                                                    calc += penalValue;
                                                }
                                            }
                                        } else if (penalGraceType != null && !penalGraceType.equals("") && penalGraceType.equals("Installments")) {
                                            // To be written
                                            if (diffDayPending > penalGraceValue) {
                                                if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Percent")) {
                                                    //                                                calc = insAmt*(((pendingInst+1)*pendingInst/2)*penalValue)/1200;
                                                    calc += (double) ((insAmt * penalValue) / 1200.0) * pendingInst--;
                                                } else if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Absolute")) {
                                                    calc += penalValue;
                                                }
                                            }
                                        }
                                    }
                                    penal = (calc + 0.5) - penal;
                                    nextInstMap.put("PENAL", String.valueOf(penal));
                                    installmentMap.put(String.valueOf(j + noOfInsPaid + addNo), nextInstMap);
                                    penal = calc + 0.5;
                                }
                            }
                            if (calc > 0) {
                                penalAmt = (long) (calc + 0.5);

                            }
                        }//pending installment calculation ends...

                        //System.out.println("Pending Trans OVer");
                        //Discount calculation details Starts...
                        for (int k = 0; k < noOfInstPay; k++) {
                            HashMap nextInstMap = new HashMap();
                            nextInstMap.put("SCHEME_NAME", prId);
                            nextInstMap.put("DIVISION_NO", String.valueOf(divNo));
                            nextInstMap.put("SL_NO", new Double(k + noOfInsPaid));
                            List listRec = ClientUtil.executeQuery("getSelectNextInstDate", nextInstMap);
                            //System.out.println("listRec Trans" + listRec);
                            if (listRec == null || listRec.size() == 0) {
                                Date curDate = (Date) curr_dt.clone();
                                int curMonth = curDate.getMonth();
                                curDate.setMonth(curMonth + 1);
                                curDate.setDate(instDay);
                                listRec = new ArrayList();
                                nextInstMap.put("NEXT_INSTALLMENT_DATE", curDate);
                                listRec.add(nextInstMap);
                            }
                            //System.out.println("nextInstMapTrans" + nextInstMap + "productMapTrans" + productMap);
                            if (listRec != null && listRec.size() > 0) {
                                long discountAmt = 0;
                                nextInstMap = (HashMap) listRec.get(0);
                                instDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextInstMap.get("NEXT_INSTALLMENT_DATE")));
                                long diffDay = DateUtil.dateDiff(instDate, currDate);
                                if (productMap.get("BONUS_ALLOWED") != null && !productMap.get("BONUS_ALLOWED").equals("") && (productMap.get("BONUS_ALLOWED").equals("Y")
                                        || productMap.get("BONUS_ALLOWED").equals("N"))) {
                                    String discount = CommonUtil.convertObjToStr(productMap.get("DISCOUNT_ALLOWED"));
                                    if (discount != null && !discount.equals("") && discount.equals("Y")) {
                                        String discountType = CommonUtil.convertObjToStr(productMap.get("DISCOUNT_RATE_TYPE"));
                                        long discountValue = CommonUtil.convertObjToLong(productMap.get("DISCOUNT_RATE_AMT"));
                                        if (getRdoPrizedMember_Yes() == true) {//discount calculation for prized prerson...
                                            String discountPrizedDays = CommonUtil.convertObjToStr(productMap.get("DIS_PRIZED_GRACE_PERIOD_DAYS"));
                                            String discountPrizedMonth = CommonUtil.convertObjToStr(productMap.get("DIS_PRIZED_GRACE_PERIOD_MONTHS"));
                                            String discountPrizedAfter = CommonUtil.convertObjToStr(productMap.get("DIS_PRIZED_GRACE_PERIOD_AFTER"));
                                            String discountPrizedEnd = CommonUtil.convertObjToStr(productMap.get("DIS_PRIZED_GRACE_PERIOD_END"));
                                            long discountPrizedValue = CommonUtil.convertObjToLong(productMap.get("DIS_PRIZED_GRACE_PERIOD"));
                                            if (discountPrizedDays != null && !discountPrizedDays.equals("") && discountPrizedDays.equals("D") && diffDay <= discountPrizedValue) {
                                                if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                                    long calc = discountValue * (long) insAmt / 100;
                                                    if (diffDay <= discountPrizedValue) {
                                                        totDiscAmt = totDiscAmt + calc;
                                                    }
                                                } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                                    if (diffDay <= discountPrizedValue) {
                                                        totDiscAmt = totDiscAmt + discountValue;
                                                    }
                                                }
                                            } else if (discountPrizedMonth != null && !discountPrizedMonth.equals("") && discountPrizedMonth.equals("M") && diffDay <= (discountPrizedValue * 30)) {
                                                if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                                    long calc = discountValue * (long) insAmt / 100;
                                                    totDiscAmt = totDiscAmt + calc;
                                                } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                                    totDiscAmt = totDiscAmt + discountValue;
                                                }
                                            } else if (discountPrizedAfter != null && !discountPrizedAfter.equals("") && discountPrizedAfter.equals("A") && currDate.getDate() <= discountPrizedValue) {
                                                if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                                    long calc = discountValue * (long) insAmt / 100;
                                                    totDiscAmt = totDiscAmt + calc;
                                                } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                                    totDiscAmt = totDiscAmt + discountValue;
                                                }
                                            } else if (discountPrizedEnd != null && !discountPrizedEnd.equals("") && discountPrizedEnd.equals("E") && pendingInst < noOfInstPay) {
                                                if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                                    long calc = discountValue * (long) insAmt / 100;
                                                    totDiscAmt = totDiscAmt + calc;
                                                } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                                    totDiscAmt = totDiscAmt + discountValue;
                                                }
                                            } else {
                                                totDiscAmt = 0;
                                            }
                                        } else if (getRdoPrizedMember_No() == true) {//discount calculation non prized person...
                                            String discountGraceDays = CommonUtil.convertObjToStr(productMap.get("DIS_GRACE_PERIOD_DAYS"));
                                            String discountGraceMonth = CommonUtil.convertObjToStr(productMap.get("DIS_GRACE_PERIOD_MONTHS"));
                                            String discountGraceAfter = CommonUtil.convertObjToStr(productMap.get("DIS_GRACE_PERIOD_AFTER"));
                                            String discountGraceEnd = CommonUtil.convertObjToStr(productMap.get("DIS_GRACE_PERIOD_END"));
                                            long discountGraceValue = CommonUtil.convertObjToLong(productMap.get("DIS_GRACE_PERIOD"));
                                            if (discountGraceDays != null && !discountGraceDays.equals("") && discountGraceDays.equals("D")) {
                                                if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                                    long calc = discountValue * (long) insAmt / 100;
                                                    if (diffDay <= discountGraceValue) {
                                                        totDiscAmt = totDiscAmt + calc;
                                                    }
                                                } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                                    if (diffDay <= discountGraceValue) {
                                                        totDiscAmt = totDiscAmt + discountValue;
                                                    }
                                                } else {
                                                    totDiscAmt = 0;
                                                }
                                            } else if (discountGraceDays != null && !discountGraceDays.equals("") && discountGraceDays.equals("M") && diffDay <= discountGraceValue * 30 && pendingInst < noOfInstPay) {
                                                if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                                    long calc = discountValue * (long) insAmt / 100;
                                                    totDiscAmt = totDiscAmt + calc;
                                                } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                                    totDiscAmt = totDiscAmt + discountValue;
                                                }
                                            } else if (discountGraceDays != null && !discountGraceDays.equals("") && discountGraceDays.equals("A") && currDate.getDate() <= discountGraceValue && pendingInst < noOfInstPay) {
                                                if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                                    long calc = discountValue * (long) insAmt / 100;
                                                    totDiscAmt = totDiscAmt + calc;
                                                } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                                    totDiscAmt = totDiscAmt + discountValue;
                                                }
                                            } else if (discountGraceDays != null && !discountGraceDays.equals("") && discountGraceDays.equals("E") && pendingInst < noOfInstPay) {
                                                if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                                    long calc = discountValue * (long) insAmt / 100;
                                                    totDiscAmt = totDiscAmt + calc;
                                                } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                                    totDiscAmt = totDiscAmt + discountValue;
                                                }
                                            } else {
                                                totDiscAmt = 0;
                                            }
                                        }
                                    } else if (discount != null && !discount.equals("") && discount.equals("N")) {
                                        totDiscAmt = 0;
                                    }
                                    discountAmt = totDiscAmt - discountAmt;
                                    HashMap instMap = new HashMap();
                                    if (installmentMap.containsKey(String.valueOf(k + noOfInsPaid + 1))) {
                                        instMap = (HashMap) installmentMap.get(String.valueOf(k + noOfInsPaid + 1));
                                        instMap.put("DISCOUNT", String.valueOf(discountAmt));
                                        installmentMap.put(String.valueOf(k + noOfInsPaid + 1), instMap);
                                    }
                                    discountAmt = totDiscAmt;
                                }

                            }
                            //System.out.println("hoo ends here");
                        }

                        //Bonus calculation details Starts...
                        for (int l = startNoForPenal; l <= noOfInstPay - addNo; l++) {
                            //                    for(int l = 1;l<=noOfInstPay;l++){
                            HashMap nextInstMap = new HashMap();
                            nextInstMap.put("SCHEME_NAME", prId);
                            nextInstMap.put("DIVISION_NO", divNo);
                            nextInstMap.put("SL_NO", new Double(l + noOfInsPaid + addNo + firstInst_No));
                            List listRec = ClientUtil.executeQuery("getSelectBonusAndNextInstDateWithoutDivision", nextInstMap);
                            if (listRec == null || listRec.size() == 0) {
                                Date curDate = (Date) curr_dt.clone();
                                int curMonth = curDate.getMonth();
                                curDate.setMonth(curMonth + 1);
                                curDate.setDate(instDay);
                                listRec = new ArrayList();
                                nextInstMap.put("NEXT_INSTALLMENT_DATE", curDate);
                                listRec.add(nextInstMap);
                                bonusAvailabe = false;
                            }
                            if (listRec != null && listRec.size() > 0) {
                                nextInstMap = (HashMap) listRec.get(0);
                                instDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextInstMap.get("NEXT_INSTALLMENT_DATE")));
                                bonusAmt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(nextInstMap.get("NEXT_BONUS_AMOUNT"))).doubleValue();
                                if (!productMap.get("MULTIPLE_MEMBER").equals("") && (productMap.get("MULTIPLE_MEMBER").equals("Y"))) {
                                    whereMap = new HashMap();
                                    int noOfCoChittal = 0;
                                    whereMap.put("SCHEME_NAME", prId);
                                    whereMap.put("CHITTAL_NUMBER", chittalNo);
                                    List applicationLst = ClientUtil.executeQuery("getSelectChitNoNotinMasterDetails", whereMap); // Count No Of Co-Chittals
                                    if (applicationLst != null && applicationLst.size() > 0) {
                                        noOfCoChittal = applicationLst.size();
                                        bonusAmt = bonusAmt / noOfCoChittal;
                                    }
                                }
                                if (productMap.get("BONUS_ROUNDING") != null && !productMap.get("BONUS_ROUNDING").equals("") && productMap.get("BONUS_ROUNDING").equals("Y")
                                        && bonusAmt > 0) {
                                    Rounding rod = new Rounding();
                                    bonusAmt = (double) rod.getNearest((long) (bonusAmt * 100), 100) / 100;
                                }
                                long diffDay = DateUtil.dateDiff(instDate, currDate);
                                //                            String hoildayInt = CommonUtil.convertObjToStr(productMap.get("HOLIDAY_INT"));
                                if (productMap.get("BONUS_ALLOWED") != null && !productMap.get("BONUS_ALLOWED").equals("") && (productMap.get("BONUS_ALLOWED").equals("Y"))) {
                                    if (bonusAvailabe == true) {
                                        if (getRdoPrizedMember_Yes() == true) {
                                            String bonusPrizedDays = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_DAYS"));
                                            String bonusPrizedMonth = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_MNTH"));
                                            String bonusPrizedAfter = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_AFT"));
                                            String bonusPrizedEnd = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_END"));
                                            long bonusPrizedValue = CommonUtil.convertObjToLong(productMap.get("BONUS_PRIZED_GRACE_PERIOD"));
                                            if (bonusPrizedDays != null && !bonusPrizedDays.equals("") && bonusPrizedDays.equals("D") && diffDay <= bonusPrizedValue) {
                                                totBonusAmt = totBonusAmt + bonusAmt;
                                            } else if (bonusPrizedMonth != null && !bonusPrizedMonth.equals("") && bonusPrizedMonth.equals("M") && diffDay <= (bonusPrizedValue * 30)) {
                                                totBonusAmt = totBonusAmt + bonusAmt;
                                            } else if (bonusPrizedAfter != null && !bonusPrizedAfter.equals("") && bonusPrizedAfter.equals("A") && currDate.getDate() <= bonusPrizedValue) {
                                                totBonusAmt = totBonusAmt + bonusAmt;
                                            } else if (bonusPrizedEnd != null && !bonusPrizedEnd.equals("") && bonusPrizedEnd.equals("E")) {

                                            } else {

                                            }
                                        } else if (getRdoPrizedMember_No() == true) {
                                            String bonusGraceDays = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_DAYS"));
                                            String bonusGraceMonth = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_MONTHS"));
                                            String bonusGraceOnAfter = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_AFTER"));
                                            String bonusGraceEnd = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_END"));
                                            long bonusGraceValue = CommonUtil.convertObjToLong(productMap.get("BONUS_GRACE_PERIOD"));
                                            if (bonusGraceDays != null && !bonusGraceDays.equals("") && bonusGraceDays.equals("D") && diffDay <= bonusGraceValue) {
                                                totBonusAmt = totBonusAmt + bonusAmt;
                                            } else if (bonusGraceMonth != null && !bonusGraceMonth.equals("") && bonusGraceMonth.equals("M") && diffDay <= (bonusGraceValue * 30)) {
                                                totBonusAmt = totBonusAmt + bonusAmt;
                                            } else if (bonusGraceOnAfter != null && !bonusGraceOnAfter.equals("") && bonusGraceOnAfter.equals("A") && currDate.getDate() <= bonusGraceValue) {
                                                totBonusAmt = totBonusAmt + bonusAmt;
                                            } else if (bonusGraceEnd != null && !bonusGraceEnd.equals("") && bonusGraceEnd.equals("E")) {

                                            } else {
                                            }
                                        }
                                    }
                                    HashMap instMap = new HashMap();
                                    if (installmentMap.containsKey(String.valueOf(l + noOfInsPaid + addNo))) {
                                        Rounding rod = new Rounding();
                                        instMap = (HashMap) installmentMap.get(String.valueOf(l + noOfInsPaid + addNo));
                                        //Added By Suresh
                                        if (productMap.get("BONUS_ROUNDING") != null && !productMap.get("BONUS_ROUNDING").equals("") && productMap.get("BONUS_ROUNDING").equals("Y")
                                                && bonusAmt > 0) {
                                            bonusAmt = (double) rod.getNearest((long) (bonusAmt * 100), 100) / 100;
                                        }
                                        instMap.put("BONUS", String.valueOf(bonusAmt));
                                        installmentMap.put(String.valueOf(l + noOfInsPaid + addNo), instMap);
                                    }
                                }
                            }
                            bonusAmt = 0;
                        }
                        //System.out.println("bonus endsss");
                        if (productMap.get("BONUS_ROUNDING") != null && !productMap.get("BONUS_ROUNDING").equals("") && productMap.get("BONUS_ROUNDING").equals("Y")
                                && totBonusAmt > 0) {
                            Rounding rod = new Rounding();
                            totBonusAmt = (double) rod.getNearest((long) (totBonusAmt * 100), 100) / 100;
                        }

                        //System.out.println("bbbnss111");
                        int insDay = 0;
                        Date paidUpToDate = null;
                        HashMap instDateMap = new HashMap();
                        instDateMap.put("SCHEME_NAME", prId);
                        instDateMap.put("DIVISION_NO", divNo);
                        instDateMap.put("INSTALLMENT_NO", CommonUtil.convertObjToInt(String.valueOf(count)));
                        lst = ClientUtil.executeQuery("getSelectInstUptoPaid", instDateMap);
                        if (lst != null && lst.size() > 0) {
                            instDateMap = (HashMap) lst.get(0);
                            paidUpToDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(instDateMap.get("NEXT_INSTALLMENT_DATE")));
                        } else {
                            //System.out.println("mappppp....mmdsjjdasj...." + chittalMap);
                            //System.out.println("eppo kittii" + chittalMap.get("SCHEME_START_DT"));
                            Date startedDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(chittalMap.get("SCHEME_START_DT")));
                            insDay = CommonUtil.convertObjToInt(chittalMap.get("INSTALLMENT_DAY"));
                            //System.out.println("yoooo" + insDay + "dsfs" + chittalMap.get("INSTALLMENT_DAY"));
                            startedDate.setDate(insDay);
                            int stMonth = startedDate.getMonth();
                            //System.out.println("montheeee" + stMonth);
                            startedDate.setMonth(stMonth + (int) count - 1);
                            //System.out.println("startedDate" + startedDate);
                            paidUpToDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(startedDate));
                        }
                        //System.out.println("ivide thanne...");
                        String narration = "";
                        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM/yyyy");
                        int noInstPay = CommonUtil.convertObjToInt(noOfInsPay);
                        if (noInstPay == 1) {
                            narration = "Inst#" + (noOfInsPaid + 1);
                            //System.out.println("pidikittiii1");
                            Date dt = DateUtil.addDays(paidUpToDate, 30);
                            //System.out.println("pidikittiii2");
                            narration += " " + sdf.format(dt);
                        } else if (noInstPay > 1) {
                            narration = "Inst#" + (noOfInsPaid + 1);
                            narration += "-" + (noOfInsPaid + noInstPay);
                            //System.out.println("pidikittiii3");
                            Date dt = DateUtil.addDays(paidUpToDate, 30);
                            //System.out.println("pidikittiii4");
                            narration += " " + sdf.format(dt);
                            dt = DateUtil.addDays(paidUpToDate, 30 * noInstPay);
                            //System.out.println("pidikittiii5");
                            narration += " To " + sdf.format(dt);
                        }
                        //System.out.println("#$#$# narration :" + narration);

                        double instAmt = insAmt * CommonUtil.convertObjToDouble(noOfInsPay).doubleValue();
                        double totalPayable = instAmt - (totBonusAmt + totDiscAmt);
                        netAmt = totalPayable + penalAmt + CommonUtil.convertObjToDouble(noticAmt).doubleValue()
                                + CommonUtil.convertObjToDouble(arbitAmt).doubleValue();
                        String totalPayableAmount = String.valueOf(totalPayable);
                        totalPayableAmount = CurrencyValidation.formatCrore(totalPayableAmount).replaceAll(",", "");
                        String penalAmount = String.valueOf(penalAmt);
                        penalAmount = CurrencyValidation.formatCrore(penalAmount).replaceAll(",", "");

                        dataMap.put("SCHEME_NAME", prId);
                        dataMap.put("CHITTAL_NO", chittalNo);
                        dataMap.put("PRINCIPAL", CommonUtil.convertObjToDouble(ent4));//totalPayableAmount);
                        dataMap.put("INTEREST", CommonUtil.convertObjToDouble(ent6));
                        dataMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
                        dataMap.put("MEMBER_NAME", txtName.getText());//lblMemberName.getText()
                        dataMap.put("DIVISION_NO", String.valueOf(divNo));
                        dataMap.put("CHIT_START_DT", startDate);
                        dataMap.put("INSTALLMENT_DATE", insDate);
                        dataMap.put("NO_OF_INSTALLMENTS", noOfInsPay);//String.valueOf(remainInst));//String.valueOf(totIns));
                        dataMap.put("CURR_INST", String.valueOf(curInsNo));
                        dataMap.put("PENDING_INST", insDue);
                        dataMap.put("PENDING_DUE_AMT", String.valueOf(insDueAmt));
                        dataMap.put("NO_OF_INST_PAY", noOfInsPay);
                        dataMap.put("INST_AMT_PAYABLE", String.valueOf(totalPayable));
                        dataMap.put("PAID_INST", String.valueOf(noOfInsPaid));
                        dataMap.put("TOTAL_PAYABLE", String.valueOf(instAmt));
                        dataMap.put("PAID_DATE", currDate);
                        dataMap.put("INST_AMT", String.valueOf(insAmt));
                        dataMap.put("SCHEME_END_DT", endDate);
                        if (getRdoPrizedMember_Yes() == true) {
                            dataMap.put("PRIZED_MEMBER", "Y");
                        } else {
                            dataMap.put("PRIZED_MEMBER", "N");
                        }
                        dataMap.put("BONUS_AVAL", String.valueOf(bonusAval));
                        dataMap.put("BONUS", CommonUtil.convertObjToDouble(ent7));
                        dataMap.put("DISCOUNT", String.valueOf(totDiscAmt));
                        dataMap.put("PENAL", String.valueOf(penalAmt));
                        dataMap.put("NOTICE_AMOUNT", noticAmt);
                        dataMap.put("ARBITRATION_AMOUNT", arbitAmt);
                        dataMap.put("NET_AMOUNT", String.valueOf(netAmt));
                        dataMap.put("TOTAL_DEMAND", CommonUtil.convertObjToDouble(ent4) + CommonUtil.convertObjToDouble(ent6)
                                + CommonUtil.convertObjToDouble(ent8));//String.valueOf(netAmt));
                        ////
                        dataMap.put("NARRATION", narration);
                        dataMap.put("EACH_MONTH_DATA", installmentMap);
                        dataMap.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
                        dataMap.put("PROD_TYPE", "MDS");
                        dataMap.put("TOTAL", noOfInsPay);
                        dataMap.put("AMTORNOOFINST", noOfInsPay);
                        dataMap.put("CLOCK_NO", txtClockNo.getText());
                        dataMap.put("MEMBER_NO", txtMemberNo.getText());
                        dataMap.put("CUST_NAME", txtName.getText());
                        dataMap.put("TOT_PRIN", CommonUtil.convertObjToDouble(ent4));
                        dataMap.put("TOT_PENAL", "0.0");
                        dataMap.put("TOT_INT", CommonUtil.convertObjToDouble(ent6));
                        dataMap.put("TOT_OTHERS", CommonUtil.convertObjToDouble(ent8));
                        dataMap.put("TOT_GRAND", CommonUtil.convertObjToDouble(ent4) + CommonUtil.convertObjToDouble(ent6)
                                + CommonUtil.convertObjToDouble(ent8));
                        dataMap.put("CHARGES", new Double(0));
                        dataMap.put("PROD_DESCRIPTION", prodDescription);
                        System.out.println("noOfInsPay ===456436546==" + dataMap);
                        if (CommonUtil.convertObjToDouble(noOfInsPay) > 0) {// && !chittalFlag
                            termMdsMap = new HashMap();
                            termMdsMap.put("MDS", dataMap);
                            finalMap.put(chittalNo, termMdsMap);
                            //System.out.println("finalMap====IIII===" + finalMap);
                        } else {
                            dataMap = null;
                        }
                    }
                }
            }
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public void getLoanDetails1(String ent0, String ent1, String ent2, String ent3, String ent4, String ent5, String ent6, String ent7, String ent8, String ent9, String ent10, String ent11, String ent12, String ent13) {
        String prod_id = "";
        String amt;

        //  HashMap otherChargesMap=null;
        //   String penalWaiveOff="";
        //get a/c no scheme,
        double totalDemand = 0.0;
        double doubleval = 0.0;
        String scheme_Name = CommonUtil.convertObjToStr(ent0);
        String acNo = CommonUtil.convertObjToStr(ent2);
        String prodType = CommonUtil.convertObjToStr(ent0);
        prod_id = CommonUtil.convertObjToStr(ent11);
        String prodDescription = CommonUtil.convertObjToStr(ent1);
        //  String acNo = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(),1));
        double transAmt = CommonUtil.convertObjToDouble(ent3).doubleValue();
        doubleval = CommonUtil.convertObjToDouble(ent3).doubleValue();
        double principal = CommonUtil.convertObjToDouble(ent4).doubleValue();
        double interest = CommonUtil.convertObjToDouble(ent6).doubleValue();
        double penal = CommonUtil.convertObjToDouble(ent5).doubleValue();
        double charges = CommonUtil.convertObjToDouble(ent8).doubleValue();
        double totalAmt = CommonUtil.convertObjToDouble(ent12).doubleValue();
        double clear_bal = CommonUtil.convertObjToDouble(ent13).doubleValue();
        clear_bal = Math.abs(clear_bal);
        //System.out.println("totalAmt === " + totalAmt + " clear_bal=====" + clear_bal + " transAmt ===" + transAmt);
        
        
        //Commeneted the below two lines after confirming with prasanth, The condition check is not required
//        if (clear_bal < transAmt) {
//            transAmt = clear_bal;
//        }
        
        
        if (transAmt <= 0) {
            double principal1 = 0;
            double interest1 = 0;
            double penal1 = 0;
            double charges1 = 0;
        } else {
            double paidPrincipal = 0;
            double paidInterest = 0;
            double paidPenal = 0;
            double paidCharges = 0;
            //System.out.println("LOAN scheme_Name=" + scheme_Name + " prod_id=" + prod_id + " acNo=" + acNo + " transAmt=" + transAmt);
            try {
                HashMap ALL_LOAN_AMOUNT = new HashMap();
                HashMap hashList = new HashMap();
                hashList.put(CommonConstants.MAP_WHERE, prod_id);
                List appList = ClientUtil.executeQuery("selectAppropriatTransaction", hashList);
                HashMap appropriateMap = new HashMap();
                if (appList != null && appList.size() > 0) {
                    appropriateMap = (HashMap) appList.get(0);
                    appropriateMap.remove("PROD_ID");
                } else {
                    throw new TTException("Please Enter Hierachy of Transaction  in This Product ");
                }
                //System.out.println("appropriateMap####" + appropriateMap);
                java.util.Collection collectedValues = appropriateMap.values();
                java.util.Iterator it = collectedValues.iterator();
                //CashTransactionTO objCashTO =new CashTransactionTO();
                int appTranValue = 0;
                while (it.hasNext()) {
                    appTranValue++;
                    String hierachyValue = CommonUtil.convertObjToStr(it.next());
                    //System.out.println("hierachyValue####" + hierachyValue);
                    //objCashTO = setCashTransaction(objCashTransactionTO);
                    if (hierachyValue.equals("CHARGES")) {
                        if (transAmt > 0 && charges > 0) {
                            if (transAmt >= charges) {
                                transAmt -= charges;
                                paidCharges = charges;
                            } else {
                                paidCharges = transAmt;
                                transAmt -= charges;
                            }
                        } else {
                            paidCharges = 0;
                        }
                    }
                    if (hierachyValue.equals("PENALINTEREST")) {
                        //penal interest
                        if (transAmt > 0 && penal > 0) {
                            if (transAmt >= penal) {
                                transAmt -= penal;
                                paidPenal = penal;
                            } else {
                                paidPenal = transAmt;
                                transAmt -= penal;
                            }
                        } else {
                            paidPenal = 0;
                        }
                    }
                    if (hierachyValue.equals("INTEREST")) {
                        //interest
                        if (transAmt > 0 && interest > 0) {
                            if (transAmt >= interest) {
                                transAmt -= interest;
                                paidInterest = interest;
                            } else {
                                paidInterest = transAmt;
                                transAmt -= interest;
                            }
                        } else {
                            paidInterest = 0;
                        }
                    }
                    if (hierachyValue.equals("PRINCIPAL")) {
                        if (transAmt > 0 && principal > 0) {
                            if (transAmt >= principal) {
                                transAmt -= principal;
                                paidPrincipal = principal;
                            } else {
                                paidPrincipal = transAmt;
                                transAmt -= principal;
                            }
                             //
                        } else {
                            paidPrincipal = 0;
                        }
                    }
                }
                //System.out.println("transAmt INOOOOOOOOOOOOO============================" + transAmt);
                // if(transAmt!=0){

                if (totalAmt <= 0 && transAmt <= clear_bal) {
                    //  tblTransaction.setValueAt(new Double(transAmt), tblTransaction.getSelectedRow(),4);
                } else {

                    //  tblTransaction.setValueAt(new Double(paidPrincipal), tblTransaction.getSelectedRow(),4);
                    //  tblTransaction.setValueAt(new Double(paidInterest), tblTransaction.getSelectedRow(),6);
                    //  tblTransaction.setValueAt(new Double(paidPenal), tblTransaction.getSelectedRow(),5);
                    //  tblTransaction.setValueAt(new Double(paidCharges), tblTransaction.getSelectedRow(),8);
                }
                // }
            /* else
                 {
                 double principal1=0;double interest1=0;double penal1=0;double charges1=0;
                 for(int i = 0;i<globalList.size();i++){
                 ArrayList aList1=(ArrayList)globalList.get(i);
                 for(int j = 0;j<aList1.size();j++){
                 String PID=aList1.get(10).toString();
                 if(PID.equals(prod_id)) {
                 principal1=Double.parseDouble(aList1.get(3).toString());
                 interest1=Double.parseDouble(aList1.get(5).toString());
                 penal1=Double.parseDouble(aList1.get(4).toString());
                 charges1=Double.parseDouble(aList1.get(7).toString());
                 }
                 }
                 }
                 tblTransaction.setValueAt(new Double(principal1), tblTransaction.getSelectedRow(),3);
                 tblTransaction.setValueAt(new Double(interest1), tblTransaction.getSelectedRow(),5);
                 tblTransaction.setValueAt(new Double(penal1), tblTransaction.getSelectedRow(),4);
                 tblTransaction.setValueAt(new Double(charges1), tblTransaction.getSelectedRow(),7);
                 }*/
                HashMap dataMap = new HashMap();
                // dataMap.put("BRANCH_CODE",CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID));
                if (paidCharges > 0) {
                    dataMap.put("CHARGES", String.valueOf(paidCharges));
                } else {
                    dataMap.put("CHARGES", "0");
                }
                dataMap.put("PRINCIPAL", String.valueOf(paidPrincipal));
                dataMap.put("INTEREST", String.valueOf(paidInterest));
                dataMap.put("PENAL", String.valueOf(paidPenal));
                //System.out.println("paidPrincipal####" + paidPrincipal + "paidInterest ==" + paidInterest + "paidPenal==" + paidPenal + "paidCharges==" + paidCharges);
                totalDemand = paidPrincipal + paidInterest + paidPenal + paidCharges;
                //System.out.println("totalDemand==" + totalDemand);
                dataMap.put("TOTAL_DEMAND", new Double(totalDemand));
                dataMap.put("PROD_TYPE", prodType);
                dataMap.put("ACT_NUM", acNo);
                dataMap.put("RECOVERED_AMOUNT", doubleval);
                dataMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
                dataMap.put("USER_ID", TrueTransactMain.USER_ID);
                dataMap.put("PROD_ID", prod_id);
                dataMap.put("CLOCK_NO", txtClockNo.getText());
                dataMap.put("MEMBER_NO", txtMemberNo.getText());
                dataMap.put("CUST_NAME", txtName.getText());
                dataMap.put("TOT_PRIN", txtTotPrincipal.getText());
                dataMap.put("TOT_PENAL", txtTotPenel.getText());
                dataMap.put("TOT_INT", txtTotInterest.getText());
                dataMap.put("TOT_OTHERS", txtTotOthers.getText());
                dataMap.put("TOT_GRAND", txtGrandTotal.getText());
                dataMap.put("NOTICE_AMOUNT", new Double(0));
                dataMap.put("ARBITRATION_AMOUNT", new Double(0));
                dataMap.put("BONUS", new Double(0));
                dataMap.put("AMTORNOOFINST", doubleval);
                //System.out.println("CommonConstants.TOSTATUS_INSERT==" + CommonConstants.TOSTATUS_INSERT);
                dataMap.put("STATUS", CommonConstants.TOSTATUS_INSERT);
                dataMap.put("PROD_DESCRIPTION", prodDescription);
                dataMap.put("TOTAL", doubleval);
                if (termLoanMap == null) {
                    termLoanMap = new HashMap();
                }
                //System.out.println("doubleval==" + doubleval);
                if (doubleval > 0) {
                    termLoanMap = new HashMap();
                    termLoanMap.put("TL", dataMap);
                    //System.out.println("acNo==" + acNo + "termLoanMap =====" + termLoanMap);
                    finalMap.put(acNo, termLoanMap);
                }
                //else
                dataMap = null;
                termLoanMap = null;
                //System.out.println("finalMap 8888888: " + finalMap);

            } catch (Exception e) {
                //System.out.println("Exception in getLoanDetails: " + e);
            }
        }//end else
    }

    //end loan
    /////////////////////////////////////////////////////
    private void btnAcHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAcHeadActionPerformed
        // TODO add your handling code here:
        callView("AC_HD_ID");
    }//GEN-LAST:event_btnAcHeadActionPerformed

    private void txtGrandTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtGrandTotalActionPerformed
        // TODO add your handling code here:
        transactionUI.setCallingAmount(txtGrandTotal.getText());
        transactionUI.setCallingTransType("TRANSFER");
        //transactionUI.setName(txtName.getText());
        //System.out.println("txtName.getText()========" + txtName.getText());

        double amountBorrowed = CommonUtil.convertObjToDouble(txtGrandTotal.getText()).doubleValue();
        // amountBorrowed = amountBorrowed + totalAmount;
       /* if(amountBorrowed > sanctionAmount){
         ClientUtil.showAlertWindow("Amount Exceeds the Disbursment Limit!!");
         txtAmtBorrowed.setText("");
         amountBorrowed = 0.0;
         }*/
        //Added BY Suresh
        transactionUI.cancelAction(false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.resetObjects();
        transactionUI.setCallingAmount(txtGrandTotal.getText());
        transactionUI.setCallingTransType("TRANSFER");
        transactionUI.setCallingApplicantName(txtName.getText());
    }//GEN-LAST:event_txtGrandTotalActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed

    }//GEN-LAST:event_btnViewActionPerformed

    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
        btnView.setEnabled(false);
        btnPrint.setEnabled(false);
    }//GEN-LAST:event_btnAuthorizeActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {
        cifClosingAlert();
    }

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        callView(ClientConstants.ACTION_STATUS[3]);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_DELETE);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnEdit.setEnabled(false);
        btnView.setEnabled(false);
        btnPrint.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        callView(ClientConstants.ACTION_STATUS[2]);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_VIEW);
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnEdit.setEnabled(false);
        btnView.setEnabled(false);
        btnPrint.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed

    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
    public void clear() {
        observable.resetForm();
        //txtNoOfTokens.setText("");
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panTransAll, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        setButtonEnableDisable();
        viewType = "";
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
        transactionUI.setCallingApplicantName("");
        transactionUI.setCallingAmount("");
        transactionUI.resetObjects();
        txtName.setText("");
//        btnAuthorize.setEnabled(false);
        txtClockNo.setEnabled(false);
        chkRetired.setEnabled(false);
        lblClearBalanceAmt.setText(" ");
        txtName.setText("");
        observable.setTxtName("");
        btnNew.setEnabled(true);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
        btnView.setEnabled(true);
        //((DefaultTableModel) tblTransaction.getModel()).setRowCount(0);
        transDetailsUI.setTransDetails(null, null, null);
        // tblTransaction.setR
        observable.resetForm();
        finalMap = null;
        chkSelectAll.setEnabled(false);
        setModified(false);
        tblTransaction.setModel(observable.getTblSalaryRecoveryList());
    }
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        clear();
        //tblTransaction.
    }//GEN-LAST:event_btnCancelActionPerformed
    private void savePerformed() {
        // //System.out.println("IN savePerformed");
        String action = "";
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            action = CommonConstants.TOSTATUS_INSERT;
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            action = CommonConstants.TOSTATUS_UPDATE;
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
            action = CommonConstants.TOSTATUS_DELETE;
        }
        saveAction(action);
    }

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        ////System.out.println("observable.getActionType() : " + observable.getActionType());
        String clockNo = txtClockNo.getText();
        if (clockNo == null || clockNo.equals("")) {
            ClientUtil.showAlertWindow("Please enter Clock No!!!");
            return;
        }
        if (tblTransaction.getRowCount() == 0) {
            ClientUtil.showAlertWindow("Please find the scheme details!!!");
            return;
        }
        String gtot = txtGrandTotal.getText();
        if (gtot == null || gtot.equals("") || CommonUtil.convertObjToDouble(txtGrandTotal.getText()).doubleValue() == 0) {
            ClientUtil.showAlertWindow("Please enter atleast one paying entry!!!");
            return;
        }
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            //System.out.println("final map keys" + finalMap.keySet());
            for (int i = 0; i < tblTransaction.getRowCount(); i++) {
                // boolean chknew=false;
                if (!((Boolean) tblTransaction.getValueAt(i, 0)).booleanValue()) {
                    //chknew=true;
                    String actNum1 = "";
                    String schmeName = CommonUtil.convertObjToStr(tblTransaction.getValueAt(i, 1));
                    String actNum = CommonUtil.convertObjToStr(tblTransaction.getValueAt(i, 3));
                    //System.out.println("schmeName" + schmeName + "actnum" + actNum);
                    if (schmeName.equals("MDS")) {
                        if (actNum.indexOf("_") != -1) {
                            actNum = actNum.substring(0, actNum.indexOf("_"));
                        }
                    }
                    //System.out.println("actNum1" + actNum1);
                    if (finalMap != null && (finalMap.containsKey(actNum))) {
                        finalMap.remove(actNum);
                    }
                }
            }
            //System.out.println("finalMap latest " + finalMap.keySet());
        }
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            int transactionSize = 0;
            if (transactionUI.getOutputTO().size() == 0 && CommonUtil.convertObjToDouble(txtGrandTotal.getText()).doubleValue() > 0) {
                ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS));
                return;
            } else {
                //System.out.println("amt" + CommonUtil.convertObjToDouble(txtGrandTotal.getText()).doubleValue());
                if (CommonUtil.convertObjToDouble(txtGrandTotal.getText()).doubleValue() > 0) {
                    amtBorrow = CommonUtil.convertObjToDouble(txtGrandTotal.getText()).doubleValue();
                    transactionSize = (transactionUI.getOutputTO()).size();
                    if (transactionSize != 1 && CommonUtil.convertObjToDouble(txtGrandTotal.getText()).doubleValue() > 0) {
                        ClientUtil.showAlertWindow("Multiple Transactions are Not allowed, Make it one Transaction");
                        return;
                    } else {
                        observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                    }
                } else if (transactionUI.getOutputTO().size() > 0) {
                    observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                }
            }

            if (transactionSize == 0 && CommonUtil.convertObjToDouble(txtGrandTotal.getText()).doubleValue() > 0) {
                ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS));
                return;
            } else if (transactionSize != 0) {
                if (!transactionUI.isBtnSaveTransactionDetailsFlag()) {
                    ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.SAVE_TX_DETAILS));
                    return;
                }
                if (transactionUI.getOutputTO().size() > 0) {
                    observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                    savePerformed();
                }
            }
        }
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        txtClockNo.setEnabled(false);
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        //observable.resetForm();
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
        transactionUI.cancelAction(false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.resetObjects();
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
        txtGrandTotal.setEnabled(false);
        // txtAmtBorrowed.setText("0");
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnSave.setEnabled(true);
        //txtAmount.setEnabled(true);
        txtClockNo.setEnabled(true);
        chkRetired.setEnabled(true);
        chkSelectAll.setSelected(true);
        chkSelectAll.setEnabled(true);
        finalMap = new HashMap();
        setModified(true);
    }//GEN-LAST:event_btnNewActionPerformed

    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed

    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed

    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed

    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed

    private void btnClockHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClockHeadActionPerformed
        callView("CLOCK_NO");
    }//GEN-LAST:event_btnClockHeadActionPerformed
    public void mdsSplitUp() {
        System.out.println("start function");
        try {
            String scheme = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 1));
            String noOfInsPay = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 4));
            String chittalNo = (CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 3)));
            String prod_id = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 12));
            String principal = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 5));
            String description = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 2));
            String interest = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 7));
            ArrayList newList = new ArrayList();
            //System.out.println("clkno is " + txtClockNo.getText());
            if (scheme.equals("MDS") && (!noOfInsPay.equals(""))) {
                System.out.println("inside MDS scheme");
                //System.out.println("IN tblTransactionMouseClicked");
                String acc_no1 = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 3));
                if (scheme.equals("MDS")) {
                    if (acc_no1.indexOf("_") != -1) {
                        acc_no1 = acc_no1.substring(0, acc_no1.indexOf("_"));
                    }
                }
                if (finalMap != null && finalMap.containsKey(acc_no1)) {
                    finalMap.remove(acc_no1);
                }
                newList.add(noOfInsPay);
                newList.add(CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 3)));
                newList.add(CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 12)));
                newList.add(CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 10)));
                newList.add(CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 11)));
                newList.add(CommonUtil.convertObjToDouble(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 13)));
                newList.add(CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 2)));
                //System.out.println("newList newList" + newList);
                // MDSDetailsUI mdsUi=
                //new MDSDetailsUI().getMdsdetail(newList);
                //System.out.println("clkno is1 " + txtClockNo.getText());
                System.out.println("newList"+newList);
                String clkno = txtClockNo.getText();
                MDSDetailsUI mds = new MDSDetailsUI(newList, false);
                chittalFlag = true;
                // mds.show();
                ////System.out.println("clkno is 2"+txtClockNo.getText());
                //new  MDSDetailsUI(newList).show();
                // //System.out.println("intre2 intre2 "+mds.getInterest());
                ////System.out.println("getNewTot getNewTot "+mds.getNewTot());
                // //System.out.println("clkno is 3"+txtClockNo.getText());
                txtClockNo.setText(clkno);
                if (mds.getInterest() > 0) {
                    tblTransaction.setValueAt(mds.getInterest(), tblTransaction.getSelectedRow(), 7);
                }
                //txtGrandTotal.setText(String.valueOf((Double.parseDouble(txtGrandTotal.getText()))+mds.getInterest()-(Double.parseDouble(txtTotInterest.getText()))));
                //txtTotInterest.setText(String.valueOf(mds.getInterest()));
                boolean chk = ((Boolean) tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 0)).booleanValue();
                //calcTotal(chk,tblTransaction.getSelectedRow(),0);
                double mdsPrinctot = 0.0;
                double mdsPenaltot = 0.0;
                double mdsInttot = 0.0;
                double mdsOthrtot = 0.0;
                double mdsgrandtot = 0.0;
                mdsPrinctot = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 5));
                mdsPenaltot = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 6));
                mdsInttot = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 7));
                mdsOthrtot = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 9));
                mdsgrandtot = mdsPrinctot + mdsPenaltot + mdsInttot + mdsOthrtot;
                termMdsMap = new HashMap();
                HashMap mdssplitMap = new HashMap();
                mdssplitMap.put("CLOCK_NO", txtClockNo.getText());
                mdssplitMap.put("MEMBER_NO", txtMemberNo.getText());
                mdssplitMap.put("CUST_NAME", txtName.getText());
                mdssplitMap.put("TOT_PRIN", mdsPrinctot);
                mdssplitMap.put("TOT_PENAL", mdsPenaltot);
                mdssplitMap.put("TOT_INT", mdsInttot);
                mdssplitMap.put("TOT_OTHERS", mdsOthrtot);
                mdssplitMap.put("TOT_GRAND", mdsgrandtot);
                mdssplitMap.put("CHARGES", new Double(0));
                mdssplitMap.put("ACT_NUM", chittalNo);
                mdssplitMap.put("PROD_TYPE", "MDS");
                mdssplitMap.put("SPLIT_DETAILS", mds.getNewTot());
                mdssplitMap.put("PROD_ID", prod_id);
                mdssplitMap.put("PROD_DESCRIPTION",description);
                if (getRdoPrizedMember_Yes() == true) {
                    mdssplitMap.put("PRIZED_MEMBER", "Y");
                } else {
                    mdssplitMap.put("PRIZED_MEMBER", "N");
                }
                //System.out.println("mdssplitMap" + mdssplitMap);
                termMdsMap.put("MDS", mdssplitMap);
                finalMap.put(chittalNo, termMdsMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
private void tblTransactionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTransactionMouseClicked
// TODO add your handling code here:
    try {
        
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW && evt.getClickCount() > 0) {
            setTableModelListener();
            String scheme1 = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 0));
            String noOfInsPay1 = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 4));
            String principal1 = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 5));
            String interest1 = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 7));
            String description = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 2));
            if (scheme1.equals("AD") && (!noOfInsPay1.equals(""))) {
                double prin = CommonUtil.convertObjToDouble(principal1);
                double paying = CommonUtil.convertObjToDouble(noOfInsPay1);
                double intr = CommonUtil.convertObjToDouble(interest1);
                if (intr > 0) {
                    prin = paying - intr;
                    tblTransaction.setValueAt(prin, tblTransaction.getSelectedRow(), 5);
                }
            } else {
                if (evt.getClickCount() > 0 && evt.getClickCount() == 2) {
                    String scheme = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 1));
                    String noOfInsPay = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 4));
                    String chittalNo = (CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 3)));
                    String prod_id = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 14));
                    String principal = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 5));
                    String interest = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 7));
                    ArrayList newList = new ArrayList();
                    //System.out.println("clkno is " + txtClockNo.getText());
                    if (scheme.equals("MDS") && (!noOfInsPay.equals(""))) {
                        //System.out.println("IN tblTransactionMouseClicked");
                        String acc_no1 = CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 3));
                        if (scheme.equals("MDS")) {
                            if (acc_no1.indexOf("_") != -1) {
                                acc_no1 = acc_no1.substring(0, acc_no1.indexOf("_"));
                            }
                        }
                        if (finalMap != null && finalMap.containsKey(acc_no1)) {
                            finalMap.remove(acc_no1);
                        }
                        newList.add(noOfInsPay);
                        newList.add(CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 3)));
                        newList.add(CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 12)));
                        newList.add(CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 10)));
                        newList.add(CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 11)));
                        newList.add(CommonUtil.convertObjToDouble(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 13)));
                        newList.add(CommonUtil.convertObjToStr(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 2)));
                        //System.out.println("newList newList" + newList);
                        // MDSDetailsUI mdsUi=
                        //new MDSDetailsUI().getMdsdetail(newList);
                        //System.out.println("clkno is1 " + txtClockNo.getText());
                        String clkno = txtClockNo.getText();
                        MDSDetailsUI mds = new MDSDetailsUI(newList);
                        chittalFlag = true;
                        mds.show();
                        ////System.out.println("clkno is 2"+txtClockNo.getText());
                        //new  MDSDetailsUI(newList).show();
                        // //System.out.println("intre2 intre2 "+mds.getInterest());
                        ////System.out.println("getNewTot getNewTot "+mds.getNewTot());
                        // //System.out.println("clkno is 3"+txtClockNo.getText());
                        txtClockNo.setText(clkno);
                        if (mds.getInterest() >= 0) {
                            tblTransaction.setValueAt(mds.getInterest(), tblTransaction.getSelectedRow(), 7);
                        } else {
                            tblTransaction.setValueAt(interest, tblTransaction.getSelectedRow(), 7);
                        }
                        //txtGrandTotal.setText(String.valueOf((Double.parseDouble(txtGrandTotal.getText()))+mds.getInterest()-(Double.parseDouble(txtTotInterest.getText()))));
                        //txtTotInterest.setText(String.valueOf(mds.getInterest()));
                        boolean chk = ((Boolean) tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 0)).booleanValue();
                        calcTotal(chk, tblTransaction.getSelectedRow(), 0);
                        double mdsPrinctot = 0.0;
                        double mdsPenaltot = 0.0;
                        double mdsInttot = 0.0;
                        double mdsOthrtot = 0.0;
                        double mdsgrandtot = 0.0;
                        mdsPrinctot = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 5));
                        mdsPenaltot = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 6));
                        mdsInttot = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 7));
                        mdsOthrtot = CommonUtil.convertObjToDouble(tblTransaction.getValueAt(tblTransaction.getSelectedRow(), 9));
                        mdsgrandtot = mdsPrinctot + mdsPenaltot + mdsInttot + mdsOthrtot;
                        termMdsMap = new HashMap();
                        HashMap mdssplitMap = new HashMap();
                        mdssplitMap.put("CLOCK_NO", txtClockNo.getText());
                        mdssplitMap.put("MEMBER_NO", txtMemberNo.getText());
                        mdssplitMap.put("CUST_NAME", txtName.getText());
                        mdssplitMap.put("TOT_PRIN", mdsPrinctot);
                        mdssplitMap.put("TOT_PENAL", mdsPenaltot);
                        mdssplitMap.put("TOT_INT", mdsInttot);
                        mdssplitMap.put("TOT_OTHERS", mdsOthrtot);
                        mdssplitMap.put("TOT_GRAND", mdsgrandtot);
                        mdssplitMap.put("CHARGES", new Double(0));
                        mdssplitMap.put("ACT_NUM", chittalNo);
                        mdssplitMap.put("PROD_TYPE", "MDS");
                        mdssplitMap.put("SPLIT_DETAILS", mds.getNewTot());
                        mdssplitMap.put("PROD_ID", prod_id);
                        mdssplitMap.put("PROD_DESCRIPTION",description);
                        if (getRdoPrizedMember_Yes() == true) {
                            mdssplitMap.put("PRIZED_MEMBER", "Y");
                        } else {
                            mdssplitMap.put("PRIZED_MEMBER", "N");
                        }
                        termMdsMap.put("MDS", mdssplitMap);
                        finalMap.put(chittalNo, termMdsMap);
                    }
                }
            }
            //selectedScheme();
            calcTotal(true, tblTransaction.getSelectedRow(), 0);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}//GEN-LAST:event_tblTransactionMouseClicked

private void chkSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSelectAllActionPerformed
    // TODO add your handling code here:
    boolean flags;
    if (chkSelectAll.isSelected() == true) {
        flags = true;
    } else {
        flags = false;
    }
    ((DefaultTableModel) tblTransaction.getModel()).removeTableModelListener(tableModelListener);
    for (int i = 0; i < tblTransaction.getRowCount(); i++) {
        tblTransaction.setValueAt(new Boolean(flags), i, 0);
        ////System.out.println("value at 7"+tblTransExceptionDetails.getValueAt(i, 7).toString());
        // if()
    }
    //  ( (DefaultTableModel) tblTransaction.getModel() ).addTableModelListener(tableModelListener); 

    //System.out.println("flags=============" + flags);
    if (!flags) {
        //System.out.println("flag" + flags);
    } else {
        // setTableModelListener();
        for (int i = 0; i < tblTransaction.getRowCount(); i++) {
            String scheme = CommonUtil.convertObjToStr(tblTransaction.getValueAt(i, 1));
            //System.out.println("i : " + i);
            if (scheme.equals("TL")) {
                //System.out.println("TL i : " + i);
                getLoanDetails("3", 0, true, "selectAll", i);
            } else if (scheme.equals("TD")) {
                getTDDetails(0, true, "selectAll", i);
                // calcTotal();
            } else if (scheme.equals("SA")) {
                getSADetails(0, true, "selectAll", i);
                // calcTotal();
            } else if (scheme.equals("MDS")) {
                calcEachChittal(0, true, "selectAll", i);//e.
                //mdsSplitUp();
            } else if (scheme.equals("AD")) {
                getADDetails(0, true, "selectAll", i);
            } else {
                getSBDetails(0, true, "selectAll", i);
            }
            //System.out.println("calcTotal ======1211==========" + i);
            calcTotal(true, i, 0);
        }
    }
    if (chkSelectAll.isSelected() == false) {
        calcTotal(false, 0, 0);
    }
}//GEN-LAST:event_chkSelectAllActionPerformed

private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
      HashMap reportParamMap = new HashMap();
      System.out.println("getScreenID()"+getScreenID());
      com.see.truetransact.clientutil.ttrintegration.LinkReport.getReports(getScreenID(), reportParamMap);
}//GEN-LAST:event_btnPrintActionPerformed
    public void selectedScheme() {
        for (int i = 0; i < tblTransaction.getRowCount(); i++) {
            String scheme = CommonUtil.convertObjToStr(tblTransaction.getValueAt(i, 1));
            String sel = CommonUtil.convertObjToStr(tblTransaction.getValueAt(i, 0));
            //System.out.println("sel ......" + sel);
            if (sel.equals("true")) {
                if (scheme.equals("TL")) {
                    getLoanDetails("3", 0, true, "selectAll", i);
                } else if (scheme.equals("TD")) {
                    getTDDetails(0, true, "selectAll", i);
                } else if (scheme.equals("SA")) {
                    getSADetails(0, true, "selectAll", i);
                } else if (scheme.equals("MDS")) {
                    mdsSplitUp();
                } else if (scheme.equals("AD")) {
                    getADDetails(0, true, "selectAll", i);
                } else {
                    getSBDetails(0, true, "selectAll", i);
                }
            }
        }
    }

    /**
     * To populate Comboboxes
     */
    private void initComponentData() {
        tblTransaction.setModel(observable.getTblSalaryRecoveryList());
    }

    private void callView(String currField) {
        viewType = currField;
        HashMap viewMap = new HashMap();
        ArrayList lst = new ArrayList();
        if (viewType.equals(ClientConstants.ACTION_STATUS[2])
                || viewType.equals(ClientConstants.ACTION_STATUS[3])) {
            lst.add("TRANS_ALL_ID");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            viewMap.put(CommonConstants.MAP_NAME, "getTransAllEditList");
        } else if (viewType.equals(AUTHORIZE) || viewType.equals(ClientConstants.ACTION_STATUS[17])) {
            lst.add("TRANS_ALL_ID");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            viewMap.put(CommonConstants.MAP_NAME, "getTransAllAuthorizeList");
        } else if (viewType.equals("CLOCK_NO")) {
            // lst.add("TRANS_ALL_ID");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            viewMap.put(CommonConstants.MAP_NAME, "TransAll.getSelectClockList");
        } else if (viewType.equals("AC_HD_ID")) {
            lst.add("TRANS_ALL_ID");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            viewMap.put(CommonConstants.MAP_NAME, "Borrowings.getSelectAcctHeadTOList");
        }
        HashMap where = new HashMap();
        where.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
        viewMap.put(CommonConstants.MAP_WHERE, where);
        where = null;
        //System.out.println("viewMap--inmmmmmmmmmmmmmmmmm-----" + viewMap);
        new ViewAll(this, viewMap).show();
        // new ViewAll(this,
    }

    public void authorizeStatus(String authorizeStatus) {
        if (viewType == AUTHORIZE && isFilled) {
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, curr_dt.clone());
            singleAuthorizeMap.put("TRANS_ALL_ID", observable.getTransallNo());//observable.getDisbursalNo()
            singleAuthorizeMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            observable.setAuthMap(singleAuthorizeMap);
            //Added By Suresh
            if (transactionUI.getOutputTO().size() > 0) {
                observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
            }
            observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
            observable.execute(authorizeStatus);
            viewType = "";
            btnCancelActionPerformed(null);
        } else {
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put("STATUS_DT", curr_dt);
            whereMap.put("AUTHORIZED_BY", TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            mapParam.put(CommonConstants.MAP_NAME, "getTransAllAuthorizeList");
            //mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeTransAll");
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            observable.setStatus();
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
        }
        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            super.setOpenForEditBy(observable.getStatusBy());
            if (fromCashierAuthorizeUI) {
                CashierauthorizeListUI.removeSelectedRow();
                this.dispose();
            }
            if (fromManagerAuthorizeUI) {
                ManagerauthorizeListUI.removeSelectedRow();
                this.dispose();
            }
        }
    }

    /**
     * Called by the Popup window created thru popUp method
     */
    public void fillData(Object map) {
        //System.out.println("map===========" + map);
        hash = (HashMap) map;
        HashMap where = new HashMap();
         if (hash.containsKey("FROM_CASHIER_AUTHORIZE_LIST_UI")) {
            fromCashierAuthorizeUI = true;
            CashierauthorizeListUI = (AuthorizeListCreditUI) hash.get("PARENT");
            hash.remove("PARENT");
            viewType = AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setStatus();
            //btnSaveDisable();
            btnReject.setEnabled(false);
            rejectFlag = 1;
        }
           if (hash.containsKey("FROM_MANAGER_AUTHORIZE_LIST_UI")) {
            fromManagerAuthorizeUI = true;
            ManagerauthorizeListUI = (AuthorizeListDebitUI) hash.get("PARENT");
            hash.remove("PARENT");
            viewType = AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setStatus();
            //btnSaveDisable();
            btnReject.setEnabled(false);
            rejectFlag = 1;
        }
        if (viewType.equals("CLOCK_NO")) {
            where.put("MEMBER_NO", hash.get("MEMBER_NO"));
            txtClockNo.setText(hash.get("CLOCK_NO").toString());
            txtMemberNo.setText(hash.get("MEMBER_NO").toString());
            txtMemNo.setText(hash.get("MEM_NO").toString());
            txtName.setText(hash.get("NAME").toString());
        }
        if (viewType.equals("AC_HD_ID")) {
            txtACHDID.setText(hash.get("AC_HD_ID").toString());
            txtACHDDESC.setText(hash.get("AC_HD_DESC").toString());
            txtParticulars.setEnabled(true);
        }
        if (viewType != null) {
            if (viewType.equals("CLOCK_NO") || viewType.equals(ClientConstants.ACTION_STATUS[2]) || viewType.equals(ClientConstants.ACTION_STATUS[2])
                    || viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) || viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                where.put("TRANS_ALL_ID", hash.get("TRANS_ALL_ID"));
                observable.setTransallNo(CommonUtil.convertObjToStr(hash.get("TRANS_ALL_ID")));
                hash.put(CommonConstants.MAP_WHERE, where);
                observable.populateData(hash);
                tblTransaction.setModel(observable.getTblSalaryRecoveryList());
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
                    ClientUtil.enableDisable(panTransAll, false);
                } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                    ClientUtil.enableDisable(panTransAll, false);
                }
                if (viewType.equals(AUTHORIZE)) {
                    ClientUtil.enableDisable(panTransAll, false);
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                    isFilled = true;
                }
                if(observable.getTxtClockNo()!=null && observable.getTxtClockNo().length()>0){
                    txtClockNo.setText(observable.getTxtClockNo());
                    txtMemberNo.setText(observable.getTxtMemberNo());
                    txtName.setText(observable.getTxtName());
                    txtTotPrincipal.setText(CommonUtil.convertObjToStr(observable.getTotprincipal()));
                    txtTotInterest.setText(CommonUtil.convertObjToStr(observable.getTotInterest()));
                    txtTotPenel.setText(CommonUtil.convertObjToStr(observable.getTotPenel()));
                    txtTotOthers.setText(CommonUtil.convertObjToStr(observable.getTotOthers()));
                    txtGrandTotal.setText(CommonUtil.convertObjToStr(observable.getGrandTotal()));
                }
            }
        }
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            int yesNo1 = 0;
            String[] options = {"Yes", "No"};
            yesNo1 = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                    COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                    null, options, options[0]);
            //System.out.println("#$#$$ yesNo : " + yesNo1);
            if (yesNo1 == 0) {
                com.see.truetransact.clientutil.ttrintegration.TTIntegration ttIntgration = null;
                HashMap reportTransIdMap = new HashMap();
                //(CommonUtil.convertObjToStr(hash.get("TRANS_ALL_ID")));
                reportTransIdMap.put("TRANS_ALL_ID", hash.get("TRANS_ALL_ID"));
                System.out.println("reportTransIdMap" + reportTransIdMap);
                ttIntgration.setParam(reportTransIdMap);
                String transType = "";
                ttIntgration.integrationForPrint("TransallReport");
            }
        }
         if (rejectFlag == 1) {
            btnReject.setEnabled(false);
        }
    }

    private void saveAction(String status) {
        //To check mandtoryness of the Terminal panAcHdDetails,panAcHeadDetails panel and diplay appropriate
        //error message, else proceed
        final String mandatoryMessage = checkMandatory(panTransAll);
        StringBuffer message = new StringBuffer(mandatoryMessage);
        if (txtClockNo.getText().equals("")) {
            message.append(objMandatoryRB.getString("txtClockNo"));
        }
        //setExpDateOnCalculation();
        if (message.length() > 0) {
            displayAlert(message.toString());
        } else {
            updateOBFields();
            //    updateAvalBalance();
            //System.out.println("finalMap ==========================" + finalMap + "status : " + status);
            observable.setFinalMap(finalMap);
            System.out.println("final map"+finalMap);
            observable.execute(status);
            HashMap returnMap = observable.getProxyReturnMap();
            ArrayList list = new ArrayList();
            if (returnMap.containsKey("list")) {
                list = (ArrayList) returnMap.get("list");
                if (list != null && list.size() > 0) {
                    System.out.println("list++" + list);
                    if (list.contains("IB")) {
                        ClientUtil.showMessageWindow("Transaction Not Completed Due To Insufficent Balance ");
                    }
                }
            }
            String returnStatus = "";
            if (returnMap.get("SUCCESS_STATUS") != null) {
                returnStatus = returnMap.get("STATUS").toString();
            }
            if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                if (returnStatus != null && !returnStatus.equals("") && returnMap.get("SUCCESS_STATUS").equals("SUCCESS")) {
                    returnStatus = returnStatus.replaceAll("null", "");
                    displayAlert("Saved Successfully TransIds:" + returnStatus);
                }
                //}
                //System.out.println("observable.getProxyReturnMap()=============" + observable.getProxyReturnMap());
                int yesNo1 = 0;
                String[] options = {"Yes", "No"};
                yesNo1 = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                        null, options, options[0]);
                //System.out.println("#$#$$ yesNo : " + yesNo1);
                if (yesNo1 == 0) {
                    com.see.truetransact.clientutil.ttrintegration.TTIntegration ttIntgration = null;
                    HashMap reportTransIdMap = new HashMap();
                    reportTransIdMap.put("TRANS_ALL_ID", returnStatus);
                    ttIntgration.setParam(reportTransIdMap);
                    String transType = "";
                    ttIntgration.integrationForPrint("TransallReport");
                }
                if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                    HashMap lockMap = new HashMap();
                    ArrayList lst = new ArrayList();
                    lst.add("TRANS_ALL_ID");
                    lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
                    if (observable.getProxyReturnMap() != null) {
                        if (observable.getProxyReturnMap().containsKey("CONFIG_ID")) {
                            lockMap.put("CONFIG_ID", observable.getProxyReturnMap().get("CONFIG_ID"));
                        }
                        //   displayTransDetail(observable.getProxyReturnMap());
                    }
                    if (status == CommonConstants.TOSTATUS_UPDATE) {
                        lockMap.put("TRANS_ALL_ID", observable.getTransallNo());
                    }
                    settings();
                }
            }
        }
        clear();
    }

    private void displayTransDetail(HashMap proxyResultMap) {
        //System.out.println("@#$@@$@@@$ proxyResultMap : " + proxyResultMap);
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
        HashMap transIdMap = new HashMap();
        HashMap transTypeMap = new HashMap();
        for (int i = 0; i < keys.length; i++) {
            if (proxyResultMap.get(keys[i]) instanceof String) {
                continue;
            }
            tempList = (List) proxyResultMap.get(keys[i]);
            if (CommonUtil.convertObjToStr(keys[i]).indexOf("CASH") != -1) {
                for (int j = 0; j < tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j == 0) {
                        transId = (String) transMap.get("TRANS_ID");
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
                        if (actNum != null && !actNum.equals("")) {
                            cashDisplayStr += "   Account No : " + transMap.get("ACT_NUM")
                                    + "   Amount : " + transMap.get("AMOUNT") + "\n";
                        } else {
                            cashDisplayStr += "   Ac Hd Desc : " + transMap.get("AC_HD_ID")
                                    + "   Amount : " + transMap.get("AMOUNT") + "\n";
                        }
                        transTypeMap.put(transMap.get("TRANS_ID"), transMap.get("TRANS_TYPE"));
                        transIdMap.put(transMap.get("TRANS_ID"), "CASH");
                    }
                }
                cashCount++;
            } else if (CommonUtil.convertObjToStr(keys[i]).indexOf("TRANSFER") != -1) {
                for (int j = 0; j < tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j == 0) {
                        transId = (String) transMap.get("BATCH_ID");
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
                        if (actNum != null && !actNum.equals("")) {
                            transferDisplayStr += "   Account No : " + transMap.get("ACT_NUM")
                                    + "   Amount : " + transMap.get("AMOUNT") + "\n";
                        } else {
                            transferDisplayStr += "   Ac Hd Desc : " + transMap.get("AC_HD_ID")
                                    + "   Amount : " + transMap.get("AMOUNT") + "\n";
                        }
                        transIdMap.put(transMap.get("BATCH_ID"), "TRANSFER");
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
        int yesNo = 0;
        String[] options = {"Yes", "No"};
        yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                null, options, options[0]);
        //System.out.println("#$#$$ yesNo : " + yesNo);
        if (yesNo == 0) {
            TTIntegration ttIntgration = null;
            HashMap printParamMap = new HashMap();
            printParamMap.put("TransDt", observable.getCurrDt());
            printParamMap.put("BranchId", ProxyParameters.BRANCH_ID);
            Object keys1[] = transIdMap.keySet().toArray();
            for (int i = 0; i < keys1.length; i++) {
                printParamMap.put("TransId", keys1[i]);
                ttIntgration.setParam(printParamMap);
                if (CommonUtil.convertObjToStr(transIdMap.get(keys1[i])).equals("TRANSFER")) {
                    ttIntgration.integrationForPrint("ReceiptPayment");
                } else if (CommonUtil.convertObjToStr(transTypeMap.get(keys1[i])).equals("DEBIT")) {
                    ttIntgration.integrationForPrint("CashPayment", false);
                } else {
                    ttIntgration.integrationForPrint("CashReceipt", false);
                }
            }
        }
    }

    /**
     * Method used to check whether the Mandatory Fields in the Form are Filled
     * or not
     */
    private String checkMandatory(javax.swing.JComponent component) {
        // return new MandatoryCheck().checkMandatory(getClass().getName(), component, getMandatoryHashMap());
        return "";
        //validation error
    }

    private void settings() {
        btnCancelActionPerformed(null);
        observable.setResultStatus();
    }

    public Date getDateValue(String date1) {
        java.text.DateFormat formatter;
        Date date = null;
        try {
            /*  String str_date=date1;
             formatter = new java.text.SimpleDateFormat("MM/dd/yyyy");
             date = (Date)formatter.parse(str_date);*/
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy");

            date = new Date(sdf2.format(sdf1.parse(date1)));

        } catch (java.text.ParseException e) {
            //System.out.println("Error in getDateValue():" + e);
        }
        return date;
    }

    /**
     * To display customer related details based on account number
     */
    private void enableDisable(boolean yesno) {
        ClientUtil.enableDisable(this, yesno);

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
        btnFind.setEnabled(!btnNew.isEnabled());
        btnAcHead.setEnabled(!btnNew.isEnabled());
        cButton1.setEnabled(!btnNew.isEnabled());
        btnClockHead.setEnabled(!btnNew.isEnabled());
        //   btnAccountNumber.setEnabled(!btnNew.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
    }

    public void update(Observable observed, Object arg) {
        txtClockNo.setText(observable.getTxtClockNo());
        txtMemberNo.setText(observable.getTxtMemberNo());
        txtName.setText(observable.getTxtName());
        if (observable.getTotprincipal() != null && !observable.getTotprincipal().equals("")) {
            txtTotPrincipal.setText(String.valueOf(observable.getTotprincipal()));
        }
        if (observable.getTotInterest() != null && !observable.getTotInterest().equals("")) {
            txtTotInterest.setText(String.valueOf(observable.getTotInterest()));
        }
        if (observable.getTotPenel() != null && !observable.getTotPenel().equals("")) {
            txtTotPenel.setText(String.valueOf(observable.getTotPenel()));
        }
        if (observable.getGrandTotal() != null && !observable.getGrandTotal().equals("")) {
            txtGrandTotal.setText(String.valueOf(observable.getGrandTotal()));
        }
        if (observable.getTotOthers() != null && !observable.getTotOthers().equals("")) {
            txtTotOthers.setText(String.valueOf(observable.getTotOthers()));
        }
        lblStatus.setText(observable.getLblStatus());
    }

    public void updateOBFields() {
        observable.setTxtClockNo(txtClockNo.getText());
        observable.setTxtMemberNo(txtMemberNo.getText());
        observable.setTxtName(txtName.getText());
        observable.setTotprincipal(Double.parseDouble(txtTotPrincipal.getText()));
        observable.setTotInterest(Double.parseDouble(txtTotInterest.getText()));
        observable.setTotOthers(Double.parseDouble(txtTotOthers.getText()));
        observable.setTotPenel(Double.parseDouble(txtTotPenel.getText()));
        observable.setGrandTotal(Double.parseDouble(txtGrandTotal.getText()));
    }

    private void setFieldNames() {
        lblClockNo.setName("lblClockNo");
        lblMemberNo.setName("lblMemberNo");
        lblName.setName("lblName");
        lblAcHdId.setName("lblAcHdId");
        //       lblAmount.setName("lblAmount");
        lblTotPenel.setName("lblTotPenel");
        lblTotInterest.setName("lblTotInterest");
        lblTotOthers.setName("lblTotOthers");
        lblGrandTotal.setName("lblGrandTotal");
        txtClockNo.setName("txtClockNo");
        txtMemberNo.setName("txtMemberNo");
        txtMemNo.setName("txtMemNo");
        txtName.setName("txtName");
        txtACHDID.setName("txtACHDID");
        // txtAmount.setName("txtAmount");
        txtTotPenel.setName("txtTotPenel");
        txtTotInterest.setName("txtTotInterest");
        txtTotOthers.setName("txtTotOthers");
        txtGrandTotal.setName("txtGrandTotal");
    }

    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }

    /**
     * Getter for property rdoPrizedMember_Yes.
     *
     * @return Value of property rdoPrizedMember_Yes.
     */
    public boolean getRdoPrizedMember_Yes() {
        return rdoPrizedMember_Yes;
    }

    /**
     * Setter for property rdoPrizedMember_Yes.
     *
     * @param rdoPrizedMember_Yes New value of property rdoPrizedMember_Yes.
     */
    public void setRdoPrizedMember_Yes(boolean rdoPrizedMember_Yes) {
        this.rdoPrizedMember_Yes = rdoPrizedMember_Yes;
    }

    /**
     * Getter for property rdoPrizedMember_No.
     *
     * @return Value of property rdoPrizedMember_No.
     */
    public boolean getRdoPrizedMember_No() {
        return rdoPrizedMember_No;
    }

    /**
     * Setter for property rdoPrizedMember_No.
     *
     * @param rdoPrizedMember_No New value of property rdoPrizedMember_No.
     */
    public void setRdoPrizedMember_No(boolean rdoPrizedMember_No) {
        this.rdoPrizedMember_No = rdoPrizedMember_No;
    }

    /**
     * Getter for property interest.
     *
     * @return Value of property interest.
     */
    public double getInterest() {
        return interest;
    }

    /**
     * Setter for property interest.
     *
     * @param interest New value of property interest.
     */
    public void setInterest(double interest) {
        this.interest = interest;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAcHead;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClockHead;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnFind;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CButton cButton1;
    private com.see.truetransact.uicomponent.CPanel cPanel1;
    private com.see.truetransact.uicomponent.CPanel cPanel2;
    private com.see.truetransact.uicomponent.CCheckBox chkRetired;
    private com.see.truetransact.uicomponent.CCheckBox chkSelectAll;
    private com.see.truetransact.uicomponent.CLabel lblAcHdId;
    private com.see.truetransact.uicomponent.CLabel lblAcHdId2;
    private com.see.truetransact.uicomponent.CLabel lblAccountHeadDisplay;
    private com.see.truetransact.uicomponent.CLabel lblClearBalance;
    private com.see.truetransact.uicomponent.CLabel lblClearBalanceAmt;
    private com.see.truetransact.uicomponent.CLabel lblClockNo;
    private com.see.truetransact.uicomponent.CLabel lblCustomerNameDisplay;
    private com.see.truetransact.uicomponent.CLabel lblGrandTotal;
    private com.see.truetransact.uicomponent.CLabel lblMemberNo;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblName;
    private com.see.truetransact.uicomponent.CLabel lblParticulars;
    private com.see.truetransact.uicomponent.CLabel lblSelectAll;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace56;
    private com.see.truetransact.uicomponent.CLabel lblSpace57;
    private com.see.truetransact.uicomponent.CLabel lblSpace58;
    private com.see.truetransact.uicomponent.CLabel lblSpace59;
    private com.see.truetransact.uicomponent.CLabel lblSpace60;
    private com.see.truetransact.uicomponent.CLabel lblSpace61;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTotInterest;
    private com.see.truetransact.uicomponent.CLabel lblTotOthers;
    private com.see.truetransact.uicomponent.CLabel lblTotPenel;
    private com.see.truetransact.uicomponent.CLabel lblTotPrin;
    private com.see.truetransact.uicomponent.CMenuBar mbrCustomer;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel pan1;
    private com.see.truetransact.uicomponent.CPanel panAccountInfo;
    private com.see.truetransact.uicomponent.CPanel panAcctHead;
    private com.see.truetransact.uicomponent.CPanel panAcctNum;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTotal;
    private com.see.truetransact.uicomponent.CPanel panTrans;
    private com.see.truetransact.uicomponent.CPanel panTransAll;
    private com.see.truetransact.uicomponent.CButtonGroup rdoTransactionType;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CScrollPane srpTransaction;
    private com.see.truetransact.uicomponent.CTable tblTransaction;
    private javax.swing.JToolBar tbrOperativeAcctProduct;
    private com.see.truetransact.uicomponent.CTextField txtACHDDESC;
    private com.see.truetransact.uicomponent.CTextField txtACHDID;
    private com.see.truetransact.uicomponent.CTextField txtClockNo;
    private com.see.truetransact.uicomponent.CTextField txtGrandTotal;
    private com.see.truetransact.uicomponent.CTextField txtMemNo;
    private com.see.truetransact.uicomponent.CTextField txtMemberNo;
    private com.see.truetransact.uicomponent.CTextField txtName;
    private com.see.truetransact.uicomponent.CTextField txtParticulars;
    private com.see.truetransact.uicomponent.CTextField txtTotInterest;
    private com.see.truetransact.uicomponent.CTextField txtTotOthers;
    private com.see.truetransact.uicomponent.CTextField txtTotPenel;
    private com.see.truetransact.uicomponent.CTextField txtTotPrincipal;
    // End of variables declaration//GEN-END:variables

}
