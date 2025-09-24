/*
 * Copyright 2012 Fincuro Solutions(p) Ltd. All rights reserved.
 *
 * This software is the proprietary information of Fincuro Solutions (P) Ltd..
 * 
 *
 * LoanTransactionUI.java
 *
 * Created on Jan 6, 2019, 10:53 AM
 */
package com.see.truetransact.ui.termloan.loantransaction;

import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.interestcalc.Rounding;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.ui.common.servicetax.ServiceTaxCalculation;
import com.see.truetransact.ui.common.transaction.TransactionUI;
import com.see.truetransact.ui.common.viewall.AuthorizeListUI;
import com.see.truetransact.ui.common.viewall.NewAuthorizeListUI;
import com.see.truetransact.ui.common.viewall.RejectionApproveUI;
import com.see.truetransact.ui.customer.SmartCustomerUI;
import com.see.truetransact.ui.salaryrecovery.AuthorizeListCreditUI;
import com.see.truetransact.ui.salaryrecovery.AuthorizeListDebitUI;
import com.see.truetransact.ui.transaction.common.TransDetailsUI;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.CurrencyValidation;
import java.util.*;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import org.apache.log4j.Logger;

/**
 * This form is used to manipulate LoanTransaction related functionality
 *
 * @author Rishad M.P
 */
public class LoanTransactionUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer, UIMandatoryField {

    final int AUTHORIZE = 3, CANCEL = 0;
    int viewType = -1;
    private HashMap mandatoryMap;
    private   LoanTransactionOB observable;
    private final static Logger log = Logger.getLogger(LoanTransactionUI.class);
    private TransactionUI transactionUI = new TransactionUI();
    private TransDetailsUI transDetailsUI = null;
    LoanTransactionRB loanTransactionRB = new LoanTransactionRB();
    private String prodType = "";
    private String transProdId = "";
    private boolean tableFlag = false;
    private LinkedHashMap transactionDetailsTO = null;
    private HashMap calMap = new HashMap();
    private boolean isAuthRecord = false;
    private double payableBalance = 0.0;
    private JTable table = null;
    public String prodDesc = "";
    private Date actOpenDt = null;
    private Date actToDate = null;
    private Date currDt = null;
    Rounding rd = new Rounding();
    boolean fromAuthorizeUI = false;
    boolean fromManagerAuthorizeUI = false;
    boolean fromCashierAuthorizeUI = false;
    AuthorizeListUI authorizeListUI = null;
    AuthorizeListDebitUI ManagerauthorizeListUI = null;
    AuthorizeListCreditUI CashierauthorizeListUI = null;
    boolean fromSmartCustUI = false;
    SmartCustomerUI smartUI = null;
    private int rejectFlag = 0;
    private Date asOn = null;
    private ServiceTaxCalculation objServiceTax;
    public HashMap serviceTax_Map;
    private Double serviceTaxAmt;
    HashMap returnWaiveMap = null;
    RejectionApproveUI rejectionApproveUI=null;
    boolean isTransaction=false;
    private String lockAccount=null;
    NewAuthorizeListUI newauthorizeListUI = null;
    boolean fromNewAuthorizeUI = false;
    public HashMap serviceTaxIdMap = new HashMap();
     java.util.ResourceBundle resourceBundle, objMandatoryMRB;
    
    public LoanTransactionUI(String prodType) {
        currDt = ClientUtil.getCurrentDate();
        this.prodType = prodType;
        initComponents();
        initStartUp();
        if (prodType.equals("TermLoan")) {
            transactionUI.setSourceScreen("LOAN_FUTURE_TRANSACTION");
        } 
        transactionUI.addToScreen(panTransaction);
        transactionUI.setTransactionMode(CommonConstants.DEBIT);
        transDetailsUI = new TransDetailsUI(panAccountHead);
        transDetailsUI.setSourceScreen("LOAN_FUTURE_TRANSACTION");
        observable.setTransactionOB(transactionUI.getTransactionOB());
        btnDelete.setVisible(false);
    }

    /**
     * Creates new form AccountClosingUI
     */
    public LoanTransactionUI() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        initStartUp();
         btnWaive.setVisible(false);
        transactionUI.setSourceScreen("LOAN_FUTURE_TRANSACTION");
        transactionUI.addToScreen(panTransaction);
        transactionUI.setTransactionMode(CommonConstants.DEBIT);
        transDetailsUI = new TransDetailsUI(panAccountHead);
        observable.setTransactionOB(transactionUI.getTransactionOB());
        btnDelete.setVisible(false);
    }

    private void initStartUp() {
        setMandatoryHashMap();
        setFieldNames();
        internationalize();

        observable = new LoanTransactionOB();
        observable.setProdType(prodType);
        observable.addObserver(this);
        update(observable, null);
        initComponentData();
        enableDisable(false);
        setButtonEnableDisable();
        setMaxLength();
        objMandatoryMRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.termloan.loantransaction.LoanTransactionMRB", ProxyParameters.LANGUAGE);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panAccountInfo);
        btnDelete.setVisible(false);
        
    }

    private void setMaxLength() {
        txtAccountNumber.setMaxLength(16);
        txtAccountNumber.setAllowAll(true);
        txtPayableAmount.setAllowAll(true);
        txtPayableAmount.setValidation(new CurrencyValidation(14, 2));
    }

    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboProductID", new Boolean(true));
        mandatoryMap.put("txtAccountNumber", new Boolean(true));
        mandatoryMap.put("txtPayableAmount", new Boolean(true));
        mandatoryMap.put("tdtFuture", new Boolean(true));
        mandatoryMap.put("cboProdType", new Boolean(true));
    }

    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    /**
     * **************** NEW METHODS ****************
     */
    public void authorizeStatus(String authorizeStatus) {

        updateAuthorizeStatus(authorizeStatus);

    }

    private void updateAuthorizeStatus(String authorizeStatus) {
        btnDelete.setVisible(false);
        if (viewType != AUTHORIZE) {
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put("USER_ID", TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            whereMap.put("CASHIER_AUTH_ALLOWED", TrueTransactMain.CASHIER_AUTH_ALLOWED);
            whereMap.put("TRANS_DT", currDt.clone());
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            if (prodType.equals("TermLoan")) {
                if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                    mapParam.put(CommonConstants.MAP_NAME, "getSelectLoanFutureTransactionCashierAuthorizeTOList");
                } else {
                    mapParam.put(CommonConstants.MAP_NAME, "getSelectLoanFutureTransactionAuthorizeTOList");
                }
            } 
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
           
            observable.setStatus();
        } else if (viewType == AUTHORIZE) {
         
            ArrayList arrList = new ArrayList();
            final HashMap authorizeMap = new HashMap();
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("ACCOUNTNO", observable.getTxtAccountNumber());
            arrList.add(singleAuthorizeMap);         
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            if (TrueTransactMain.SERVICE_TAX_REQ.equals("Y")) {
                HashMap serauthMap = new HashMap();
                serauthMap.put("ACCT_NUM", observable.getTxtAccountNumber());
                serauthMap.put("STATUS", authorizeStatus);
                serauthMap.put("USER_ID", TrueTransactMain.USER_ID);
                serauthMap.put("AUTHORIZEDT", currDt);
                authorizeMap.put("SER_TAX_AUTH", serauthMap);
            }
            if (isAuthRecord) {
                CommonUtil comm = new CommonUtil();
                final JDialog loading = comm.addProgressBar();
                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws InterruptedException /** Execute some operation */
                    {
                        try {
                            authorize(authorizeMap);
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
                //authorize(authorizeMap);
                isAuthRecord = false;
            } else {
                viewType = -1;
                return;
            }
            super.setOpenForEditBy(observable.getStatusBy());
            
            btnCancelActionPerformed(null);
            lblStatus.setText(authorizeStatus);
        }
        
       
    }

    private String isItMDSLoan() {
        HashMap mdsLoanmap = new HashMap();
        String prodName = "";
        String prod_id = ((ComboBoxModel) cboProductID.getModel()).getKeyForSelected().toString();
        mdsLoanmap.put("PROD_ID", CommonUtil.convertObjToStr(prod_id));
        List mdsRemList = ClientUtil.executeQuery("getMdsAuthorizeRemark", mdsLoanmap);
        if (mdsRemList != null && mdsRemList.size() > 0) {
            mdsLoanmap = (HashMap) mdsRemList.get(0);
            if (mdsLoanmap != null && mdsLoanmap.containsKey("AUTHORIZE_REMARK")) {
                prodName = CommonUtil.convertObjToStr(mdsLoanmap.get("AUTHORIZE_REMARK"));
            }
        }
        return prodName;
    }

    public void authorize(HashMap map) { 
        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
            observable.setResult(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setAuthorizeMap(map);
            observable.doAction();
            observable.setAuthorizeMap(null);
            observable.setResultStatus();
            if (fromNewAuthorizeUI) {
                newauthorizeListUI.removeSelectedRow();
                this.dispose();
                newauthorizeListUI.setFocusToTable();
                if (prodType.equalsIgnoreCase("TermLoan")) {
                    newauthorizeListUI.displayDetails("LOAN FUTURE TRANSACTION");
                }
            }
            if (fromAuthorizeUI) {
                authorizeListUI.removeSelectedRow();
                this.dispose();
                authorizeListUI.setFocusToTable();
                if (prodType.equalsIgnoreCase("TermLoan")) {
                    authorizeListUI.displayDetails("LOAN FUTURE TRANSACTION");
                } 
            }
            if (fromManagerAuthorizeUI) {
                ManagerauthorizeListUI.removeSelectedRow();
                this.dispose();
                ManagerauthorizeListUI.setFocusToTable();
            }
            if (fromCashierAuthorizeUI) {
                CashierauthorizeListUI.removeSelectedRow();
                this.dispose();
                CashierauthorizeListUI.setFocusToTable();
            }
        }
    }
  
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoTransactionType = new com.see.truetransact.uicomponent.CButtonGroup();
        panAccountInfo = new com.see.truetransact.uicomponent.CPanel();
        panAccountInfoInner = new com.see.truetransact.uicomponent.CPanel();
        panProductID = new com.see.truetransact.uicomponent.CPanel();
        lblProductID = new com.see.truetransact.uicomponent.CLabel();
        cboProductID = new com.see.truetransact.uicomponent.CComboBox();
        lblAccountNumber = new com.see.truetransact.uicomponent.CLabel();
        lblCustomerNameDisplay = new com.see.truetransact.uicomponent.CLabel();
        panAcctNum = new com.see.truetransact.uicomponent.CPanel();
        txtAccountNumber = new com.see.truetransact.uicomponent.CTextField();
        btnAccountNumber = new com.see.truetransact.uicomponent.CButton();
        lblHouseName = new com.see.truetransact.uicomponent.CLabel();
        btnWaive = new com.see.truetransact.uicomponent.CButton();
        lblServiceTax = new com.see.truetransact.uicomponent.CLabel();
        lblServiceTaxval = new com.see.truetransact.uicomponent.CLabel();
        tdtFutureDate = new com.see.truetransact.uicomponent.CDateField();
        lblToDate = new com.see.truetransact.uicomponent.CLabel();
        txtPayableAmount = new com.see.truetransact.uicomponent.CTextField();
        lblTransAmount = new com.see.truetransact.uicomponent.CLabel();
        cboProdType = new com.see.truetransact.uicomponent.CComboBox();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        sptAccountInfo = new com.see.truetransact.uicomponent.CSeparator();
        panAccountHead = new com.see.truetransact.uicomponent.CPanel();
        panTransaction = new com.see.truetransact.uicomponent.CPanel();
        tbrOperativeAcctProduct = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace70 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace71 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace72 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace73 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace74 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace75 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
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
        setMinimumSize(new java.awt.Dimension(858, 660));
        setPreferredSize(new java.awt.Dimension(858, 660));

        panAccountInfo.setMinimumSize(new java.awt.Dimension(1000, 500));
        panAccountInfo.setPreferredSize(new java.awt.Dimension(1000, 500));
        panAccountInfo.setLayout(new java.awt.GridBagLayout());

        panAccountInfoInner.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panAccountInfoInner.setMinimumSize(new java.awt.Dimension(728, 340));
        panAccountInfoInner.setPreferredSize(new java.awt.Dimension(728, 340));
        panAccountInfoInner.setLayout(new java.awt.GridBagLayout());

        panProductID.setMinimumSize(new java.awt.Dimension(280, 330));
        panProductID.setPreferredSize(new java.awt.Dimension(280, 330));
        panProductID.setLayout(new java.awt.GridBagLayout());

        lblProductID.setText("Product ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panProductID.add(lblProductID, gridBagConstraints);

        cboProductID.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProductID.setNextFocusableComponent(txtAccountNumber);
        cboProductID.setPopupWidth(210);
        cboProductID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProductIDActionPerformed(evt);
            }
        });
        cboProductID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboProductIDFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panProductID.add(cboProductID, gridBagConstraints);

        lblAccountNumber.setText("Account Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panProductID.add(lblAccountNumber, gridBagConstraints);

        lblCustomerNameDisplay.setForeground(new java.awt.Color(0, 51, 204));
        lblCustomerNameDisplay.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCustomerNameDisplay.setMinimumSize(new java.awt.Dimension(150, 21));
        lblCustomerNameDisplay.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panProductID.add(lblCustomerNameDisplay, gridBagConstraints);

        panAcctNum.setLayout(new java.awt.GridBagLayout());

        txtAccountNumber.setAllowAll(true);
        txtAccountNumber.setAllowNumber(true);
        txtAccountNumber.setMaxLength(10);
        txtAccountNumber.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAccountNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAccountNumberActionPerformed(evt);
            }
        });
        txtAccountNumber.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAccountNumberFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAcctNum.add(txtAccountNumber, gridBagConstraints);

        btnAccountNumber.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccountNumber.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnAccountNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccountNumberActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAcctNum.add(btnAccountNumber, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panProductID.add(panAcctNum, gridBagConstraints);

        lblHouseName.setForeground(new java.awt.Color(0, 51, 204));
        lblHouseName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHouseName.setMaximumSize(new java.awt.Dimension(150, 21));
        lblHouseName.setMinimumSize(new java.awt.Dimension(150, 21));
        lblHouseName.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panProductID.add(lblHouseName, gridBagConstraints);

        btnWaive.setBackground(new java.awt.Color(0, 0, 0));
        btnWaive.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/waive4.jpg"))); // NOI18N
        btnWaive.setToolTipText("Waive");
        btnWaive.setFont(new java.awt.Font("MS Sans Serif", 1, 10)); // NOI18N
        btnWaive.setMaximumSize(new java.awt.Dimension(25, 25));
        btnWaive.setMinimumSize(new java.awt.Dimension(25, 25));
        btnWaive.setPreferredSize(new java.awt.Dimension(25, 25));
        btnWaive.setRequestFocusEnabled(false);
        btnWaive.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWaiveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        panProductID.add(btnWaive, gridBagConstraints);

        lblServiceTax.setText("Service Tax     ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        panProductID.add(lblServiceTax, gridBagConstraints);

        lblServiceTaxval.setMaximumSize(new java.awt.Dimension(110, 21));
        lblServiceTaxval.setMinimumSize(new java.awt.Dimension(110, 21));
        lblServiceTaxval.setPreferredSize(new java.awt.Dimension(110, 21));
        lblServiceTaxval.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                lblServiceTaxvalFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panProductID.add(lblServiceTaxval, gridBagConstraints);

        tdtFutureDate.setVerifyInputWhenFocusTarget(false);
        tdtFutureDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtFutureDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panProductID.add(tdtFutureDate, gridBagConstraints);

        lblToDate.setText("Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panProductID.add(lblToDate, gridBagConstraints);

        txtPayableAmount.setMaxLength(16);
        txtPayableAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPayableAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPayableAmountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panProductID.add(txtPayableAmount, gridBagConstraints);

        lblTransAmount.setText("Amount");
        lblTransAmount.setMaximumSize(new java.awt.Dimension(87, 18));
        lblTransAmount.setMinimumSize(new java.awt.Dimension(87, 18));
        lblTransAmount.setOpaque(true);
        lblTransAmount.setPreferredSize(new java.awt.Dimension(87, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        panProductID.add(lblTransAmount, gridBagConstraints);

        cboProdType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdTypeActionPerformed(evt);
            }
        });
        cboProdType.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboProdTypeFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 30;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panProductID.add(cboProdType, gridBagConstraints);

        cLabel1.setText("ProdType");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panProductID.add(cLabel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panAccountInfoInner.add(panProductID, gridBagConstraints);

        sptAccountInfo.setOrientation(javax.swing.SwingConstants.VERTICAL);
        sptAccountInfo.setPreferredSize(new java.awt.Dimension(5, 5));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weightx = 1.0;
        panAccountInfoInner.add(sptAccountInfo, gridBagConstraints);

        panAccountHead.setMinimumSize(new java.awt.Dimension(200, 175));
        panAccountHead.setPreferredSize(new java.awt.Dimension(200, 175));
        panAccountHead.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panAccountInfoInner.add(panAccountHead, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountInfo.add(panAccountInfoInner, gridBagConstraints);

        panTransaction.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountInfo.add(panTransaction, gridBagConstraints);

        getContentPane().add(panAccountInfo, java.awt.BorderLayout.CENTER);

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setEnabled(false);
        btnView.setFocusable(false);
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
        btnNew.setFocusable(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnNew);

        lblSpace70.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace70.setText("     ");
        lblSpace70.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace70.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace70.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace70);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.setFocusable(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnEdit);

        lblSpace71.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace71.setText("     ");
        lblSpace71.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace71.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace71.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace71);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.setFocusable(false);
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
        btnSave.setFocusable(false);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnSave);

        lblSpace72.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace72.setText("     ");
        lblSpace72.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace72.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace72.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace72);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.setFocusable(false);
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
        btnAuthorize.setNextFocusableComponent(btnAuthorize);
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnAuthorize);

        lblSpace73.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace73.setText("     ");
        lblSpace73.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace73);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.setFocusable(false);
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnException);

        lblSpace74.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace74.setText("     ");
        lblSpace74.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace74);

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
        btnPrint.setFocusable(false);
        tbrOperativeAcctProduct.add(btnPrint);

        lblSpace75.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace75.setText("     ");
        lblSpace75.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace75);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.setFocusable(false);
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnClose);

        getContentPane().add(tbrOperativeAcctProduct, java.awt.BorderLayout.NORTH);

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

        mbrCustomer.setName("mbrCustomer"); // NOI18N

        mnuProcess.setText("Process");
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

    private void cboProductIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboProductIDFocusLost

        btnAccountNumber.grabFocus();
        btnAccountNumber.requestFocus();
    }//GEN-LAST:event_cboProductIDFocusLost

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        popUp();
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed
//    public void modifyTransData(Object objTextUI) {
//       
//        totRecivable = transDetailsUI.calculatetotalRecivableAmountFromAccountClosing();
//        if (observable.isWaiveoffPenal() && observable.getPenalWaiveAmount() > 0) {
//            totRecivable -= observable.getInterestWaiveAmount();
//        }
//        if (observable.isWaiveOffInterest() && observable.getInterestWaiveAmount() > 0) {
//            totRecivable -= observable.getInterestWaiveAmount();
//        }
//        if (observable.isNoticeWaiveoff() && observable.getNoticeWaiveAmount() > 0) {
//            totRecivable -= observable.getNoticeWaiveAmount();
//        }
//        if (observable.isPrincipalwaiveoff() && observable.getPrincipalWaiveAmount() > 0) {
//            totRecivable -= observable.getPrincipalWaiveAmount();
//        }
////        calcAndDisplayAvailableBalance();
//        totRecivable = 0;
//    }

    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        updateAuthorizeStatus(CommonConstants.STATUS_EXCEPTION);
        btnCancel.setEnabled(true);
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        updateAuthorizeStatus(CommonConstants.STATUS_REJECTED);

    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        updateAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
       

    }//GEN-LAST:event_btnAuthorizeActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        mitCloseActionPerformed(evt);
    }//GEN-LAST:event_btnCloseActionPerformed

    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {
        cifClosingAlert();
    }

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        observable.resetForm();
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_DELETE);
        popUp();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        observable.resetForm();
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_EDIT);
        popUp();
        ClientUtil.enableDisable(panAccountInfo, false);
        transactionUI.cancelAction(false);
    }//GEN-LAST:event_btnEditActionPerformed

    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        if (observable.getAuthorizeStatus() != null) {
            super.removeEditLock(txtAccountNumber.getText());
        }
        if (observable.getActionType() != ClientConstants.ACTIONTYPE_AUTHORIZE) {
            observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
            transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_CANCEL);
        }
        viewType = CANCEL;
        isAuthRecord = false;
        setModified(false);
        observable.resetForm();
        transDetailsUI.setTransDetails(null, null, null);
        enableDisable(false);
        setButtonEnableDisable();
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
        transactionUI.setCallingApplicantName("");
        transactionUI.setCallingAmount("");
        if (observable.getActionType() != ClientConstants.ACTIONTYPE_AUTHORIZE) {
            observable.setStatus();
        }
        returnWaiveMap=new HashMap();
        returnWaiveMap=null;
        setModified(false);
        observable.setAuthorizeMap(new HashMap());
        calMap = null;
        btnSave.setEnabled(false);
        btnAccountNumber.setEnabled(false);
        if (fromNewAuthorizeUI) {
            this.dispose();
            fromNewAuthorizeUI= false;
            newauthorizeListUI.setFocusToTable();
        }
        if (fromAuthorizeUI) {
            this.dispose();
            fromAuthorizeUI = false;
            authorizeListUI.setFocusToTable();
        }
        if (fromManagerAuthorizeUI) {
            this.dispose();
            fromManagerAuthorizeUI = false;
            ManagerauthorizeListUI.setFocusToTable();
        }
        if (fromCashierAuthorizeUI) {
            this.dispose();
            fromCashierAuthorizeUI = false;
            CashierauthorizeListUI.setFocusToTable();
        }
        cboProductID.setSelectedIndex(0);
        lblServiceTaxval.setText("");
        tdtFutureDate.setDateValue("");
        txtPayableAmount.setText("");
        serviceTax_Map = null;
        rejectionApproveUI = null;
        if (fromSmartCustUI) {
            this.dispose();
            fromSmartCustUI = false;
        }
    }//GEN-LAST:event_btnCancelActionPerformed


    private void resetUIForm() {
        txtAccountNumber.setText("");
        lblCustomerNameDisplay.setText("");
        txtPayableAmount.setText("");
        payableBalance = 0;
        observable.setTxtPayableBalance("");
        transactionUI.cancelAction(false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.setCallingApplicantName("");
        transactionUI.setCallingAmount("");
        if (observable.getChargelst() != null) {
            observable.getChargelst().clear();
            observable.setChargelst(null);
        }
        lblHouseName.setText("");
        lblServiceTaxval.setText("");
        serviceTax_Map=null;
    }

    private void btnCheck() {
        btnCancel.setEnabled(true);
        btnSave.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnEdit.setEnabled(false);
    }

    private void savePerformed() {
        updateOBFields();
        try {
            isTransaction = true;
            observable.doAction();
        } catch (Exception e) {
            HashMap lockMap = new HashMap();
            lockMap.put("RECORD_KEY", observable.getTxtAccountNumber());
            lockMap.put("CUR_DATE", currDt.clone());
            lockMap.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
            ClientUtil.execute("deleteLock", lockMap);
        }
        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            HashMap lockMap = new HashMap();
            ArrayList lst = new ArrayList();
            lst.add("ACCOUNT NUMBER");
            lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                if (observable.getProxyReturnMap() != null) {
                    if (observable.getProxyReturnMap().containsKey("ISSUE_ID")) {
                        lockMap.put("ACCOUNT NUMBER", observable.getProxyReturnMap().get("ACT_NUM"));
                    }
                }
                if (observable.getProxyReturnMap() != null) {
                    if (observable.getProxyReturnMap().containsKey("CASH_TRANS_LIST") || observable.getProxyReturnMap().containsKey("TRANSFER_TRANS_LIST")) {
                        displayTransDetail(observable.getProxyReturnMap());
                    }
                }
            }
           if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
               lockMap.put("ACCOUNT NUMBER", observable.getTxtAccountNumber());
           }
            setEditLockMap(lockMap);
            setEditLock();
            ClientUtil.enableDisable(this, false);
            setButtonEnableDisable();
            observable.resetForm();
            observable.setResultStatus();
            transactionUI.resetObjects();
            transDetailsUI.setTransDetails(null, null, null);
            btnCancelActionPerformed(null);
        } else {
            HashMap lockMap = new HashMap();
           lockMap.put("RECORD_KEY", observable.getTxtAccountNumber());
            lockMap.put("CUR_DATE", currDt.clone());
            lockMap.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
            ClientUtil.execute("deleteLock", lockMap);
            
            btnNew.enable(false);
            btnEdit.enable(false);
            btnDelete.enable(false);
            btnSave.enable(true);
            btnCancel.enable(true);
        }

    }

    private void displayTransDetail(HashMap proxyResultMap) {
        String cashDisplayStr = "Cash Transaction Details...\n";
        String transferDisplayStr = "Transfer Transaction Details...\n";
        String displayStr = "";
        String transId = "";
        String cashId = "";
        String transType = "";
        Object keys[] = proxyResultMap.keySet().toArray();
        int cashCount = 0;
        int transferCount = 0;
        List tempList = null;
        HashMap transMap = null;
        String actNum = "";
        boolean cashTransfer = false;
        HashMap transIdMap = new HashMap();
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
                        cashId = transId;
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
                cashCount++;
            } else if (CommonUtil.convertObjToStr(keys[i]).indexOf("TRANSFER") != -1) {
                cashTransfer = true;
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
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            HashMap transTypeMap = new HashMap();
            transMap = new HashMap();
            HashMap transCashMap = new HashMap();
            String reportName = "";
           transCashMap.put("BATCH_ID", observable.getTxtAccountNumber());
            transCashMap.put("TRANS_DT", currDt);
            transCashMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            transCashMap.put("INSTRUMENT_NO2", "INSTRUMENT_NO2");
            if (prodType.equals("TermLoan")) {
                transCashMap.put("AUTHORIZE_STATUS", "AUTHORIZE_STATUS");
                transCashMap.put("SCREEN_NAME", "LOAN FUTURE TRANSACTION");
            }
            transIdMap = new HashMap();
            List list = null;
            boolean TransferWithCash = false;
            boolean rebateCashTrans=false;
            if (cashCount == 0 && transferCount > 0) {
                list = ClientUtil.executeQuery("getTransferDetails", transCashMap);
                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        transMap = (HashMap) list.get(i);
                        transIdMap.put(transMap.get("SINGLE_TRANS_ID"), "TRANSFER");
                    }                    
                }
            } else {
                list = ClientUtil.executeQuery("getTransferDetails", transCashMap);
                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        transMap = (HashMap) list.get(i);
                         if (prodType.equals("TermLoan")) {
                        transIdMap.put(transMap.get("SINGLE_TRANS_ID"), "TRANSFER");
                         }else{
                             transIdMap.put(transMap.get("SINGLE_TRANS_ID")+"_1", "TRANSFER");
                         }
                         }
                    TransferWithCash = true;
                }
                list = ClientUtil.executeQuery("getCashDetails", transCashMap);
                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        transMap = (HashMap) list.get(i);
                        transIdMap.put(transMap.get("SINGLE_TRANS_ID"), "CASH");
                        if(transMap.containsKey("INSTRUMENT_NO1")&&transMap.get("INSTRUMENT_NO1")!=null&&transMap.get("INSTRUMENT_NO1").equals("REBATE_INTEREST") )
                        {
                        rebateCashTrans=true;
                        }
                        transTypeMap.put(transMap.get("SINGLE_TRANS_ID"), transMap.get("TRANS_TYPE"));
                       }
                }
            }
          
            int yesNo = 0;
            String[] voucherOptions = {"Yes", "No"};
            if (list != null && list.size() > 0 && observable.getActionType() != ClientConstants.ACTIONTYPE_AUTHORIZE && observable.getActionType() != ClientConstants.ACTIONTYPE_REJECT) {
                yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                        null, voucherOptions, voucherOptions[0]);
                if (yesNo == 0) {
                    com.see.truetransact.clientutil.ttrintegration.TTIntegration ttIntgration = null;
                    HashMap paramMap = new HashMap();
                    paramMap.put("TransDt", currDt);
                    paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
                    Object keys1[] = transIdMap.keySet().toArray();
                    System.out.println("keys1R%@#@#%"+keys1.length);
                    for (int i = 0; i < keys1.length; i++) {
                        String transId1 = CommonUtil.convertObjToStr(keys1[i]);
                        System.out.println("transId1 :"+transId1);
                        if(transId1.contains("_")){
                         transId1 = transId1.substring(0,transId1.indexOf("_"))   ;
                          System.out.println("transId1 aft33333:"+transId1);
                        }
                         paramMap.put("TransId",transId1);
                         ttIntgration.setParam(paramMap);
                       if (CommonUtil.convertObjToStr(transIdMap.get(keys1[i])).equals("TRANSFER") && keys1.length==1) {
                            reportName = "ReceiptPayment";
                            paramMap.put("TransId",transId1);
                            ttIntgration.setParam(paramMap);
                            // Added condition for solving print duplication issue
                            if(TransferWithCash)
                            ttIntgration.integrationForPrint(reportName, false);
                        } else if (prodType.equals("TermLoan") && observable.getLoanBehaves().equals("OD")) {
                            if (observable.getBalCrDR() != null && observable.getBalCrDR().equals("Cr")) {
                                reportName = "CashPayment";
                            }
                            if (observable.getBalCrDR() != null && observable.getBalCrDR().equals("Dr")) {
                                reportName = "CashReceipt";
                                if (TransferWithCash)
                                {
                                    paramMap.put("TransId",transId1);
                                    ttIntgration.setParam(paramMap);
                                    ttIntgration.integrationForPrint(reportName, false);
                                    ttIntgration.integrationForPrint("ReceiptPayment", false);
                                }
                            }
                        } else if (prodType.equals("TermLoan")) {
                            if (CommonUtil.convertObjToStr(transIdMap.get(keys1[i])).equals("CASH")) {
                                if(rebateCashTrans)
                                {
                                 reportName = "CashPayment"; 
                                paramMap.put("TransId",transId1);
                                ttIntgration.setParam(paramMap);
                                ttIntgration.integrationForPrint(reportName, false);
                                }
                                reportName = "CashReceipt"; 
                                paramMap.put("TransId",transId1);
                                ttIntgration.setParam(paramMap);
                                // Added condition for solving print duplication issue
                                if(TransferWithCash)
                                ttIntgration.integrationForPrint(reportName, false);
                            }                                                       
                            if (CommonUtil.convertObjToStr(transIdMap.get(keys1[i])).equals("TRANSFER")) {  
                                paramMap.put("TransId",transId1);
                                ttIntgration.setParam(paramMap);
                                // Added condition for solving print duplication issue
                                if(TransferWithCash)
                                ttIntgration.integrationForPrint("ReceiptPayment", false);                                                         
                            }
                        } else {
                            if (CommonUtil.convertObjToStr(transIdMap.get(keys1[i])).equals("CASH")) {
                                reportName = "CashPayment";
                                paramMap.put("TransId",transId1);
                                ttIntgration.setParam(paramMap);
                                ttIntgration.integrationForPrint(reportName, false);
                            } 
                           if (CommonUtil.convertObjToStr(transIdMap.get(keys1[i])).equals("TRANSFER"))
                            {
                                paramMap.put("TransId",transId1);
                                ttIntgration.setParam(paramMap);
                                ttIntgration.integrationForPrint("ReceiptPayment", false);
                            }
                        }
                       if(!TransferWithCash)
                        ttIntgration.integrationForPrint(reportName, false);
                    }
                }
            }

        }
        cashTransfer = false;
    }

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        btnSave.setEnabled(false);
        String Acno = txtAccountNumber.getText();
        HashMap hashmap = new HashMap();
        hashmap.put("ACNO", Acno);
        observable.setAuthorizeBy2("");
       double payableAmount = CommonUtil.convertObjToDouble(txtPayableAmount.getText()).doubleValue();
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW && (CommonUtil.convertObjToDouble(transDetailsUI.getShadowCredit()).doubleValue() != 0
                || CommonUtil.convertObjToDouble(transDetailsUI.getShadowDebit()).doubleValue() != 0)) {
           displayAlert(loanTransactionRB.getString("shadowCrDr"));
            return;
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW && (CommonUtil.convertObjToDouble(transDetailsUI.getFreezeAmount()).doubleValue() > 0)) {
            displayAlert(loanTransactionRB.getString("freezeAmount"));
            return;
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW && (CommonUtil.convertObjToDouble(transDetailsUI.getLienAmount()).doubleValue() > 0)) {
           displayAlert(loanTransactionRB.getString("lienAmount"));
            return;
        }
        else if ((observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) && (!CommonUtil.convertObjToStr(transDetailsUI.getUnclearBalance()).equals("0.00"))) {
            displayAlert(loanTransactionRB.getString("UnclearBalance"));
            return;
        } else {
            int transactionSize = 0;
            if (transactionUI.getOutputTO() == null && payableAmount != 0) {
                ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS));
            } else {
                transactionSize = (transactionUI.getOutputTO()).size();
                observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
            }

            if (transactionSize == 0 && payableAmount != 0) {
                ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS));
            } else if (transactionSize != 0) {
                    updateOBFields();
                    double transTotalAmt = CommonUtil.convertObjToDouble(transactionUI.getTransactionOB().getLblTotalTransactionAmtVal()).doubleValue();
                    if (ClientUtil.checkTotalAmountTallied(payableAmount, transTotalAmt) == false && payableAmount != 0) {
                        ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NOT_TALLY));
                    } else {
                        if (moreThanLoanAmountAlert()) {
                            return;
                        }
                        final LoanTransactionRB loanTransactionRB = new LoanTransactionRB();
                        //To display an alert if the mandatory fields are not properly inputted, else proceed with normal operation
                        StringBuffer mandatoryMessage = new StringBuffer();
                        mandatoryMessage.append(new MandatoryCheck().checkMandatory(getClass().getName(), panAccountInfo));
                        if (!CommonUtil.convertObjToStr(observable.getTxtProdType()).equals("GL")
                                && CommonUtil.convertObjToStr(observable.getCboProductID()).length() == 0) {
                            mandatoryMessage.append(objMandatoryMRB.getString("cboProductID") + "\n");
                        }
                        if (CommonUtil.convertObjToStr(observable.getTxtProdType()).length() == 0) {
                            mandatoryMessage.append(objMandatoryMRB.getString("cboProdType") + "\n");
                        }
                        if (observable.getFutureDate() == null || observable.getFutureDate().equals(null)) {
                            mandatoryMessage.append(objMandatoryMRB.getString("tdtFutureDate") + "\n");
                        }
                        if (observable.getTxtTransAmount() == 0) {
                            mandatoryMessage.append(objMandatoryMRB.getString("txtPayableAmount") + "\n");
                        }
                        if (observable.getTxtAccountNumber().equals(null) || observable.getTxtAccountNumber().equals("")) {
                            mandatoryMessage.append(objMandatoryMRB.getString("txtAccountNumber") + "\n");
                        }
                        if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0) {
                            displayAlert(mandatoryMessage.toString());
                            return;
                        } 
                        else{
                            CommonUtil comm = new CommonUtil();
                            final JDialog loading = comm.addProgressBar();
                            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                                @Override
                                protected Void doInBackground() throws InterruptedException /** Execute some operation */
                                {
                                    savePerformed();
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
                        setModified(false);
                    }
                    }
            }
           
        }
        TrueTransactMain.populateBranches();
        TrueTransactMain.selBranch = ProxyParameters.BRANCH_ID;
        observable.setSelectedBranchID(ProxyParameters.BRANCH_ID);
        setSelectedBranchID(ProxyParameters.BRANCH_ID);
        calMap = null;
    }//GEN-LAST:event_btnSaveActionPerformed
    private boolean moreThanLoanAmountAlert() {
        double totWaiveamt = 0;
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW ) {
                double totalLoanAmt = setEMIAmount();
                HashMap allAmtMap = observable.getTotalLoanAmount();
                if (allAmtMap.containsKey("CURR_MONTH_PRINCEPLE") && CommonUtil.convertObjToDouble(allAmtMap.get("CURR_MONTH_PRINCEPLE")).doubleValue() > 0) {
                    totalLoanAmt -= CommonUtil.convertObjToDouble(allAmtMap.get("CURR_MONTH_PRINCEPLE")).doubleValue();
                }
                if (allAmtMap.containsKey("CLEAR_BALANCE") && CommonUtil.convertObjToDouble(allAmtMap.get("CLEAR_BALANCE")).doubleValue() < 0) {
                    totalLoanAmt += -CommonUtil.convertObjToDouble(allAmtMap.get("CLEAR_BALANCE")).doubleValue();
                }
               
                double actualAmt = CommonUtil.convertObjToDouble(txtPayableAmount.getText()).doubleValue();
                if (CommonUtil.convertObjToDouble(lblServiceTaxval.getText()) > 0) {
                    totalLoanAmt += CommonUtil.convertObjToDouble(lblServiceTaxval.getText());
                    actualAmt += CommonUtil.convertObjToDouble(lblServiceTaxval.getText());
                }
                if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                    totWaiveamt = CommonUtil.convertObjToDouble(returnWaiveMap.get("Total_WaiveAmt"));
                    if (totWaiveamt > 0) {
                        actualAmt = actualAmt + totWaiveamt;
                    }
                }
                if (actualAmt >= totalLoanAmt) {
                    int message = ClientUtil.confirmationAlert("Entered Transaction Amount is equal to/more than the Outstanding Loan Amount," + "\n" + "Do You Want to Close the A/c?");
                    if (message == 0) {
                        HashMap hash = new HashMap();
                        CInternalFrame frm = new com.see.truetransact.ui.operativeaccount.AccountClosingUI("TermLoan");
                        frm.setSelectedBranchID(getSelectedBranchID());
                        TrueTransactMain.showScreen(frm);
                        hash.put("FROM_TRANSACTION_SCREEN", "FROM_TRANSACTION_SCREEN");
                        hash.put("ACCOUNT NUMBER", txtAccountNumber.getText());
                        hash.put("PROD_ID", CommonUtil.convertObjToStr(cboProductID.getSelectedItem()));
                        frm.fillData(hash);
                    }
                    return true;
                }
        }
        return false;
    }
    private double setEMIAmount() {
        HashMap allAmtMap = new HashMap();
        double totEmiAmount = 0.0;
        allAmtMap = observable.getTotalLoanAmount();
        if (allAmtMap != null && allAmtMap.size() > 0) {
            if (allAmtMap.containsKey("POSTAGE CHARGES") && CommonUtil.convertObjToDouble(allAmtMap.get("POSTAGE CHARGES")).doubleValue() > 0) {
                totEmiAmount = CommonUtil.convertObjToDouble(allAmtMap.get("POSTAGE CHARGES")).doubleValue();
            }
            if (allAmtMap.containsKey("ARBITRARY CHARGES") && CommonUtil.convertObjToDouble(allAmtMap.get("ARBITRARY CHARGES")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("ARBITRARY CHARGES")).doubleValue();
            }
            if (allAmtMap.containsKey("LEGAL CHARGES") && CommonUtil.convertObjToDouble(allAmtMap.get("LEGAL CHARGES")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("LEGAL CHARGES")).doubleValue();
            }
            if (allAmtMap.containsKey("INSURANCE CHARGES") && CommonUtil.convertObjToDouble(allAmtMap.get("INSURANCE CHARGES")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("INSURANCE CHARGES")).doubleValue();
            }
            if (allAmtMap.containsKey("NOTICE CHARGES") && CommonUtil.convertObjToDouble(allAmtMap.get("NOTICE CHARGES")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("NOTICE CHARGES"));
            }
            if (allAmtMap.containsKey("MISCELLANEOUS CHARGES") && CommonUtil.convertObjToDouble(allAmtMap.get("MISCELLANEOUS CHARGES")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("MISCELLANEOUS CHARGES")).doubleValue();
            }
            if (allAmtMap.containsKey("EXECUTION DECREE CHARGES") && CommonUtil.convertObjToDouble(allAmtMap.get("EXECUTION DECREE CHARGES")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("EXECUTION DECREE CHARGES")).doubleValue();
            }
            if (allAmtMap.containsKey("ADVERTISE CHARGES") && CommonUtil.convertObjToDouble(allAmtMap.get("ADVERTISE CHARGES")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("ADVERTISE CHARGES")).doubleValue();
            }
            if (allAmtMap.containsKey("PENAL_INT") && CommonUtil.convertObjToDouble(allAmtMap.get("PENAL_INT")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("PENAL_INT")).doubleValue();
            }
            if (allAmtMap.containsKey("CURR_MONTH_INT") && CommonUtil.convertObjToDouble(allAmtMap.get("CURR_MONTH_INT")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("CURR_MONTH_INT")).doubleValue();
            }
            if (allAmtMap.containsKey("CURR_MONTH_PRINCEPLE") && CommonUtil.convertObjToDouble(allAmtMap.get("CURR_MONTH_PRINCEPLE")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("CURR_MONTH_PRINCEPLE")).doubleValue();
            }
            if (allAmtMap.containsKey("OTHER_CHARGES") && CommonUtil.convertObjToDouble(allAmtMap.get("OTHER_CHARGES")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("OTHER_CHARGES")).doubleValue();
            }
            if (allAmtMap.containsKey("EA_COST") && CommonUtil.convertObjToDouble(allAmtMap.get("EA_COST")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("EA_COST")).doubleValue();
            }
            if (allAmtMap.containsKey("EA_EXPENCE") && CommonUtil.convertObjToDouble(allAmtMap.get("EA_EXPENCE")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("EA_EXPENCE")).doubleValue();
            }
            if (allAmtMap.containsKey("ARC_COST") && CommonUtil.convertObjToDouble(allAmtMap.get("ARC_COST")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("ARC_COST")).doubleValue();
            }
            if (allAmtMap.containsKey("ARC_EXPENCE") && CommonUtil.convertObjToDouble(allAmtMap.get("ARC_EXPENCE")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("ARC_EXPENCE")).doubleValue();
            }
            if (allAmtMap.containsKey("EP_COST") && CommonUtil.convertObjToDouble(allAmtMap.get("EP_COST")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("EP_COST")).doubleValue();
            }
            if (allAmtMap.containsKey("EP_EXPENCE") && CommonUtil.convertObjToDouble(allAmtMap.get("EP_EXPENCE")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("EP_EXPENCE")).doubleValue();
            }
            if (allAmtMap.containsKey("EMI_OVERDUE_CHARGE") && allAmtMap.get("EMI_OVERDUE_CHARGE") != null && CommonUtil.convertObjToDouble(allAmtMap.get("EMI_OVERDUE_CHARGE")).doubleValue() > 0) {
                totEmiAmount += CommonUtil.convertObjToDouble(allAmtMap.get("EMI_OVERDUE_CHARGE")).doubleValue();                
            }
        }
        return totEmiAmount;
    }
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        try {
            setMode(ClientConstants.ACTIONTYPE_NEW);
            enableDisable(true);
            observable.fillDropdown();
            cboProductID.setModel(observable.getCbmProductID());
            txtAccountNumber.setEnabled(true);
            txtAccountNumber.setEditable(true);
            setButtonEnableDisable();
            observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
            setModified(true);
            observable.resetForm();
            observable.setStatus();
            transactionUI.cancelAction(false);
            transactionUI.setButtonEnableDisable(true);
            transactionUI.resetObjects();
            transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
            transactionUI.setCallingApplicantName("");
            transactionUI.setCallingAmount("");
            txtAccountNumber.setText(CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID));
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void txtAccountNumberFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccountNumberFocusLost

        String actNum = CommonUtil.convertObjToStr(txtAccountNumber.getText());
        if (actNum.length() > 0) {
            actNum = observable.checkAcNoWithoutProdType(actNum);
            txtAccountNumber.setText(actNum);
            accountViewMap(actNum);
        }

    }//GEN-LAST:event_txtAccountNumberFocusLost

    private boolean chkAuthorizationPending() {
        boolean chkFlag = true;
        String actNum = CommonUtil.convertObjToStr(txtAccountNumber.getText());
        if (actNum.length() > 0) {
            actNum = observable.checkAcNoWithoutProdType(actNum);
            if (prodType.equals("TermLoan")) {
                HashMap transMap = new HashMap();
                HashMap transCashMap = new HashMap();
                String authRemarks = "";
                transCashMap.put("BATCH_ID", actNum);
                transCashMap.put("TRANS_DT", currDt);
                transCashMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                transCashMap.put("AUTHORIZE_STATUS", "AUTHORIZE_STATUS");
                List transferList = ClientUtil.executeQuery("getTransferDetails", transCashMap);
                if (transferList.size() > 0) {
                    ClientUtil.displayAlert("Transaction is pending for authorization!!");
                    txtAccountNumber.setText("");
                   return false;
                }
                List cashList = ClientUtil.executeQuery("getCashDetails", transCashMap);
                if (cashList.size() > 0) {
                    ClientUtil.displayAlert("Transaction is pending for authorization!!");
                    txtAccountNumber.setText("");
                 return false;
                }
            }
        }
        return chkFlag;
    }    
    // Start
    private void calculateServiceTaxAmt() {
        double taxAmt = 0;
        List taxSettingsList =  new ArrayList();
        if (prodType.equals("TermLoan")) {
            String prodId = CommonUtil.convertObjToStr(((ComboBoxModel) cboProductID.getModel()).getKeyForSelected());
            String actNum = CommonUtil.convertObjToStr(txtAccountNumber.getText());
            //taxAmt = observable.calcServiceTaxAmount(actNum, prodId);
            taxSettingsList = observable.calcServiceTaxAmount(actNum, prodId);
        }
         if (taxSettingsList != null && taxSettingsList.size() > 0) {
            HashMap ser_Tax_Val = new HashMap();
            ser_Tax_Val.put(ServiceTaxCalculation.CURR_DT, currDt);
            ser_Tax_Val.put(ServiceTaxCalculation.TOT_AMOUNT, taxAmt);
            try {
                objServiceTax = new ServiceTaxCalculation();
                ser_Tax_Val.put("SERVICE_TAX_DATA", taxSettingsList); 
                serviceTax_Map = objServiceTax.calculateServiceTax(ser_Tax_Val);
                if (serviceTax_Map != null && serviceTax_Map.containsKey(ServiceTaxCalculation.TOT_TAX_AMT)) {
                    String amt = CommonUtil.convertObjToStr(serviceTax_Map.get(ServiceTaxCalculation.TOT_TAX_AMT));
                    serviceTaxAmt = CommonUtil.convertObjToDouble(objServiceTax.roundOffAmt(amt, "NEAREST_VALUE"));                    
                    lblServiceTaxval.setText(amt);
                    observable.setLblServiceTaxval(lblServiceTaxval.getText());
                    serviceTax_Map.put(ServiceTaxCalculation.TOT_TAX_AMT, amt);
                    
                } else {
                    lblServiceTaxval.setText("0.00");
                    serviceTax_Map = new HashMap();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } else {
            lblServiceTaxval.setText("0.00");
            observable.setLblServiceTaxval("0.00");
            observable.setServiceTax_Map(new HashMap());
            serviceTax_Map = new HashMap();
        }
        double tr_amt = CommonUtil.convertObjToDouble(observable.getTxtPayableBalance());
        tr_amt = CommonUtil.convertObjToDouble(observable.getTxtPayableBalance()) + CommonUtil.convertObjToDouble(lblServiceTaxval.getText());
        transactionUI.setCallingAmount(CommonUtil.convertObjToStr(tr_amt));
        observable.setTxtPayableBalance(CommonUtil.convertObjToStr(tr_amt));
    }
    
    // End
    public HashMap checkServiceTaxApplicable(String accheadId) {
        String checkFlag = "N";
        HashMap checkForTaxMap = new HashMap();
        if (TrueTransactMain.SERVICE_TAX_REQ.equals("Y")) {
            HashMap whereMap = new HashMap();
            whereMap.put("AC_HD_ID", accheadId);
            List accHeadList = ClientUtil.executeQuery("getCheckServiceTaxApplicableForShare", whereMap);                  
            if (accHeadList != null && accHeadList.size() > 0) {
                HashMap accHeadMap = (HashMap) accHeadList.get(0);
                if (accHeadMap != null && accHeadMap.containsKey("SERVICE_TAX_APPLICABLE")&& accHeadMap.containsKey("SERVICE_TAX_ID")) {
                    checkFlag = CommonUtil.convertObjToStr(accHeadMap.get("SERVICE_TAX_APPLICABLE"));
                    checkForTaxMap.put("SERVICE_TAX_APPLICABLE",accHeadMap.get("SERVICE_TAX_APPLICABLE"));
                    checkForTaxMap.put("SERVICE_TAX_ID",accHeadMap.get("SERVICE_TAX_ID"));
                }
            }
        }
        return checkForTaxMap;
    }
    
   // End
    
    private void btnAccountNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccountNumberActionPerformed
        String prodId = CommonUtil.convertObjToStr(cboProductID.getSelectedItem());
        System.out.println("product id is"+prodId);
        if (prodId.length() > 0) {
            resetUIForm();
            popUp();  
        }
    }//GEN-LAST:event_btnAccountNumberActionPerformed

    private int getRoundOffType(String roundOff) {
        int returnVal = 0;
        if (roundOff.equals("Nearest Value")) {
            returnVal = 1 * 100;
        } else if (roundOff.equals("Nearest Hundreds")) {
            returnVal = 100 * 100;
        } else if (roundOff.equals("Nearest Tens")) {
            returnVal = 10 * 100;
        }
        return returnVal;
    }   
    private void cboProductIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProductIDActionPerformed
        //To get the AccountHead details for a proper ProductID
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            log.info("Inside cboProductIDActionPerformed");
            updateOBFields();
        }
        if (observable.getCboProductID().length() > 0) {
            observable.setCboProductID((String) (((ComboBoxModel) (cboProductID).getModel())).getKeyForSelected());
            if (prodType.equals("TermLoan")) {
                HashMap hash = new HashMap();
                hash.put("PROD_ID", observable.getCboProductID());
                List lst = ClientUtil.executeQuery("getLoanBehaves", hash);
                if (lst.size() > 0) {
                    hash = (HashMap) lst.get(0);
                    if ((CommonUtil.convertObjToStr(hash.get("AUTHORIZE_REMARK")).equals("GOLD_LOAN") && !TrueTransactMain.BRANCH_ID.equalsIgnoreCase(TrueTransactMain.selBranch))) {
                        ClientUtil.showAlertWindow("Interbranch Not allowed For Gold Loan");
                        btnCancelActionPerformed(null);
                        return;
                    }
                    observable.setLoanBehaves(CommonUtil.convertObjToStr(hash.get("BEHAVES_LIKE")));
                }
            }
        }
        if (observable.getActionType()  == ClientConstants.ACTIONTYPE_NEW && !prodType.equals("GL")) {
            String prodId = "";
            prodId = ((ComboBoxModel) cboProductID.getModel()).getKeyForSelected().toString();
           txtAccountNumber.setText(CommonUtil.convertObjToStr(TrueTransactMain.selBranch) + prodId);
        }
    }//GEN-LAST:event_cboProductIDActionPerformed

    private void txtAccountNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAccountNumberActionPerformed
        // TODO add your handling code here:
        String actNum = CommonUtil.convertObjToStr(txtAccountNumber.getText());
        if (actNum.length() > 0) {
            clearPreviousAccountDetails();
            resetUIForm();
            actNum = observable.checkAcNoWithoutProdType(actNum);
            txtAccountNumber.setText(actNum);
        }
    }//GEN-LAST:event_txtAccountNumberActionPerformed

    private void btnWaiveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWaiveActionPerformed

        HashMap totalLoanAmount = observable.getTotalLoanAmount();
        int waiveconfirm = -1, penalwaiveconfirm = -1;
        observable.setAvailableBalance(CommonUtil.convertObjToStr(totalLoanAmount.get("CLEAR_BALANCE")));
        returnWaiveMap = new HashMap();        
        if (CommonUtil.convertObjToStr(totalLoanAmount.get("INTEREST_WAIVER")).equals("Y") || CommonUtil.convertObjToStr(totalLoanAmount.get("PENAL_WAIVER")).equals("Y")
                || CommonUtil.convertObjToStr(totalLoanAmount.get("PRINCIPAL_WAIVER")).equals("Y") || CommonUtil.convertObjToStr(totalLoanAmount.get("NOTICE_WAIVER")).equals("Y") || CommonUtil.convertObjToStr(totalLoanAmount.get("ARC_WAIVER")).equals("Y")
                || CommonUtil.convertObjToStr(totalLoanAmount.get("ARBITRARY_WAIVER")).equals("Y") || CommonUtil.convertObjToStr(totalLoanAmount.get("POSTAGE_WAIVER")).equals("Y") || CommonUtil.convertObjToStr(totalLoanAmount.get("LEGAL_WAIVER")).equals("Y")
                || CommonUtil.convertObjToStr(totalLoanAmount.get("INSURANCE_WAIVER")).equals("Y") || CommonUtil.convertObjToStr(totalLoanAmount.get("EP_COST_WAIVER")).equals("Y") || CommonUtil.convertObjToStr(totalLoanAmount.get("DECREE_WAIVER")).equals("Y")
                || CommonUtil.convertObjToStr(totalLoanAmount.get("MISCELLANEOUS_WAIVER")).equals("Y") || CommonUtil.convertObjToStr(totalLoanAmount.get("ADVERTISE_WAIVER")).equals("Y")) {
            waiveconfirm = ClientUtil.confirmationAlert("Do you want to WAIVE");
            if (waiveconfirm == 0) {
                observable.showEditWaiveTableUI(totalLoanAmount);
                returnWaiveMap = observable.waiveOffEditInterestAmt();
                System.out.println("returnWaiveMap$@#$@#$@"+returnWaiveMap);
                double penalWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("PENAL"));
                if (CommonUtil.convertObjToStr(totalLoanAmount.get("PENAL_WAIVER")).equals("Y") && penalWaiveAmt > 0) {
                    observable.setWaiveoffPenal(true);
                    observable.setPenalWaiveAmount(penalWaiveAmt);
                } else {
                    observable.setWaiveoffPenal(false);
                    observable.setPenalWaiveAmount(0);
                }
                double interestWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("INTEREST"));
                if (CommonUtil.convertObjToStr(totalLoanAmount.get("INTEREST_WAIVER")).equals("Y") && interestWaiveAmt > 0) {
                    observable.setInterestWaiveoff(true);
                    observable.setInterestWaiveAmount(interestWaiveAmt);
                } else {
                    observable.setInterestWaiveoff(false);
                    observable.setInterestWaiveAmount(0);

                }
                double noticeWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("NOTICE CHARGES"));
                if (CommonUtil.convertObjToStr(totalLoanAmount.get("NOTICE_WAIVER")).equals("Y") && noticeWaiveAmt > 0) {
                    observable.setNoticeWaiveoff(true);
                    observable.setNoticeWaiveAmount(noticeWaiveAmt);
                } else {
                    observable.setNoticeWaiveoff(false);
                    observable.setNoticeWaiveAmount(0);
                }
                double principalWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("PRINCIPAL"));
                if (CommonUtil.convertObjToStr(totalLoanAmount.get("PRINCIPAL_WAIVER")).equals("Y") && principalWaiveAmt > 0) {
                    observable.setPrincipalwaiveoff(true);
                    observable.setPrincipalWaiveAmount(principalWaiveAmt);
                } else {
                    observable.setPrincipalwaiveoff(false);
                    observable.setPrincipalWaiveAmount(0);
                }
                double arcWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("ARC_COST"));
                if (CommonUtil.convertObjToStr(totalLoanAmount.get("ARC_WAIVER")).equals("Y") && arcWaiveAmt > 0) {
                    observable.setArcWaiveOff(true);
                    observable.setArcWaiveAmount(arcWaiveAmt);
                } else {
                    observable.setArcWaiveOff(false);
                    observable.setArcWaiveAmount(0);

                }
                if (totalLoanAmount != null && totalLoanAmount.containsKey("ARBITRARY_WAIVER") && CommonUtil.convertObjToStr(totalLoanAmount.get("ARBITRARY_WAIVER")).equals("Y")) {
                    if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                        double arbitaryWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("ARBITRARY CHARGES"));
                        if (arbitaryWaiveAmt > 0) {
                            observable.setArbitraryWaiveOff(true);
                            observable.setArbitarayWaivwAmount(arbitaryWaiveAmt);
                        } else {
                            observable.setArbitraryWaiveOff(false);
                        }
                    }
                }
                if (totalLoanAmount != null && totalLoanAmount.containsKey("POSTAGE_WAIVER") && CommonUtil.convertObjToStr(totalLoanAmount.get("POSTAGE_WAIVER")).equals("Y")) {
                    if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                        double postageWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("POSTAGE CHARGES"));
                        if (postageWaiveAmt > 0) {
                            observable.setPostageWaiveOff(true);
                            observable.setPostageWaiveAmount(postageWaiveAmt);
                        } else {
                            observable.setPostageWaiveOff(false);
                        }
                    }
                }
                if (totalLoanAmount != null && totalLoanAmount.containsKey("LEGAL_WAIVER") && CommonUtil.convertObjToStr(totalLoanAmount.get("LEGAL_WAIVER")).equals("Y")) {
                    if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                        double legalWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("LEGAL CHARGES"));
                        if (legalWaiveAmt > 0) {
                            observable.setLegalWaiveOff(true);
                            observable.setLegalWaiveAmount(legalWaiveAmt);
                        } else {
                            observable.setLegalWaiveOff(false);
                        }
                    }
                }
                if (totalLoanAmount != null && totalLoanAmount.containsKey("INSURANCE_WAIVER") && CommonUtil.convertObjToStr(totalLoanAmount.get("INSURANCE_WAIVER")).equals("Y")) {
                    if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                        double insurenceWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("INSURANCE CHARGES"));
                        if (insurenceWaiveAmt > 0) {
                            observable.setInsuranceWaiveOff(true);
                            observable.setInsuranceWaiveAmont(insurenceWaiveAmt);
                        } else {
                            observable.setInsuranceWaiveOff(false);
                        }
                    }
                }
                if (totalLoanAmount != null && totalLoanAmount.containsKey("EP_COST_WAIVER") && CommonUtil.convertObjToStr(totalLoanAmount.get("EP_COST_WAIVER")).equals("Y")) {
                    if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                        double epWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("EP_COST"));
                        if (epWaiveAmt > 0) {
                            observable.setEpCostWaiveOff(true);
                            observable.setEpCostWaiveAmount(epWaiveAmt);
                        } else {
                            observable.setEpCostWaiveOff(false);
                        }
                    }
                }
                if (totalLoanAmount != null && totalLoanAmount.containsKey("DECREE_WAIVER") && CommonUtil.convertObjToStr(totalLoanAmount.get("DECREE_WAIVER")).equals("Y")) {
                    if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                        double degreeWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("EXECUTION DECREE CHARGES"));
                        if (degreeWaiveAmt > 0) {
                            observable.setDecreeWaiveOff(true);
                            observable.setDecreeWaiveAmount(degreeWaiveAmt);
                        } else {
                            observable.setDecreeWaiveOff(false);
                        }
                    }
                }
                if (totalLoanAmount != null && totalLoanAmount.containsKey("MISCELLANEOUS_WAIVER") && CommonUtil.convertObjToStr(totalLoanAmount.get("MISCELLANEOUS_WAIVER")).equals("Y")) {
                    if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                        double miseWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("MISCELLANEOUS CHARGES"));
                        if (miseWaiveAmt > 0) {
                            observable.setMiscellaneousWaiveOff(true);
                            observable.setMiscellaneousWaiveAmount(miseWaiveAmt);
                        } else {
                            observable.setMiscellaneousWaiveOff(false);
                        }
                    }
                }
                if (totalLoanAmount != null && totalLoanAmount.containsKey("ADVERTISE_WAIVER") && CommonUtil.convertObjToStr(totalLoanAmount.get("ADVERTISE_WAIVER")).equals("Y")) {
                    if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                        double advertiseWaiveAmt = CommonUtil.convertObjToDouble(returnWaiveMap.get("ADVERTISE CHARGES"));
                        if (advertiseWaiveAmt > 0) {
                            observable.setAdvertiseWaiveOff(true);
                            observable.setAdvertiseWaiveAmount(advertiseWaiveAmt);
                        } else {
                            observable.setAdvertiseWaiveOff(false);
                        }
                    }
                }
            }
        }
    }//GEN-LAST:event_btnWaiveActionPerformed

    private void txtPayableAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPayableAmountFocusLost
        // TODO add your handling code here:
                double totWaiveamt = 0;
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW && observable.getTxtProdType().equals("TL")) {
                double totalLoanAmt = setEMIAmount();
                HashMap allAmtMap = observable.getTotalLoanAmount();
                if (allAmtMap.containsKey("CURR_MONTH_PRINCEPLE") && CommonUtil.convertObjToDouble(allAmtMap.get("CURR_MONTH_PRINCEPLE")).doubleValue() > 0) {
                    totalLoanAmt -= CommonUtil.convertObjToDouble(allAmtMap.get("CURR_MONTH_PRINCEPLE")).doubleValue();
                }
                double actualAmt = CommonUtil.convertObjToDouble(txtPayableAmount.getText()).doubleValue();
                if (CommonUtil.convertObjToDouble(lblServiceTaxval.getText()) > 0) {
                    totalLoanAmt += CommonUtil.convertObjToDouble(lblServiceTaxval.getText());
                    actualAmt += CommonUtil.convertObjToDouble(lblServiceTaxval.getText());
                }
                if (returnWaiveMap != null && returnWaiveMap.size() > 0) {
                    totWaiveamt = CommonUtil.convertObjToDouble(returnWaiveMap.get("Total_WaiveAmt"));
                    if (totWaiveamt > 0) {
                        actualAmt = actualAmt + totWaiveamt;
                    }
                }
                if (actualAmt < totalLoanAmt) {
                     ClientUtil.showAlertWindow("Entered Transaction Amount must be greater than or equal to (penal_interest+charges) : "+totalLoanAmt);
                 txtPayableAmount.setText("");
                }
                else
                {
                transactionUI.setCallingAmount(txtPayableAmount.getText());
                transactionUI.setCallingApplicantName(lblCustomerNameDisplay.getText());  
                }
            }          
    }//GEN-LAST:event_txtPayableAmountFocusLost

    private void lblServiceTaxvalFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_lblServiceTaxvalFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_lblServiceTaxvalFocusLost

    private void cboProdTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdTypeActionPerformed
        // TODO add your handling code here:
        String prodType = String.valueOf(cboProdType.getSelectedItem());
        observable.setCbmProdId(prodType);
        cboProductID.setModel(observable.getCbmProductID());
 
   }//GEN-LAST:event_cboProdTypeActionPerformed

    private void cboProdTypeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboProdTypeFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_cboProdTypeFocusLost

    private void tdtFutureDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtFutureDateFocusLost
        // TODO add your handling code here:
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW && tdtFutureDate.getDateValue()!=null) {
            ClientUtil.validateToDate(tdtFutureDate, ClientUtil.getCurrentDateinDDMMYYYY());
        }
    }//GEN-LAST:event_tdtFutureDateFocusLost

    /**
     * To populate Comboboxes
     */
    private void initComponentData() {
       createCboProdType();
    }

    /**
     * To display a popUp window for viewing existing data
     */
    private void popUp() {
        HashMap testMap = null;
        //To display customer info based on the selected ProductID
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            testMap = accountViewMap();
            clearPreviousAccountDetails();
        } //To display the existing accounts which are set to closed
        else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
            testMap = accountEditMap();
        }
        new com.see.truetransact.ui.common.viewall.ViewAll(this, testMap).show();

    }

    private void clearPreviousAccountDetails() {
        this.txtAccountNumber.setText("");
        this.lblCustomerNameDisplay.setText("");
        observable.setCustomerName("");
        observable.setAvailableBalance("");
        observable.setTxtInterestPayable("");
        transDetailsUI.setTransDetails(null, null, null);
    }
    public void statusDep() {
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
    }
    private void createCboProdType() {
        cboProdType.addItem("");
//        cboProdType.addItem("Advances");
        cboProdType.addItem("Term Loans");
        cboProdType.setSelectedIndex(1);
    }

    public void doUpdate() {
        initComponentData();
        update1(observable, null);

    }
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
    /**
     * Called by the Popup window created thru popUp method
     */
   public void fillData(Object obj) {
       
        try {
            final HashMap hash = (HashMap) obj;
             if(hash.containsKey("PROD_DESC")&& hash.get("PROD_DESC") != null){
                 cboProductID.setSelectedItem(CommonUtil.convertObjToStr(hash.get("PROD_DESC")));
                }
                 if (hash.containsKey("NEW_FROM_AUTHORIZE_LIST_UI")) {
                     fromNewAuthorizeUI = true;
                     newauthorizeListUI = (NewAuthorizeListUI) hash.get("PARENT");
                     hash.remove("PARENT");
                     viewType = AUTHORIZE;
                     observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
                     observable.setStatus();
                     btnReject.setEnabled(false);
                     rejectFlag = 1;
                 }
                 if (hash.containsKey("FROM_AUTHORIZE_LIST_UI")) {
                     fromAuthorizeUI = true;
                     authorizeListUI = (AuthorizeListUI) hash.get("PARENT");
                     hash.remove("PARENT");
                     viewType = AUTHORIZE;
                     observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
                     observable.setStatus();
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
                     btnReject.setEnabled(false);
                     rejectFlag = 1;
                 }
                if (hash.containsKey("FROM_CASHIER_AUTHORIZE_LIST_UI")) {
                    fromCashierAuthorizeUI = true;
                    CashierauthorizeListUI = (AuthorizeListCreditUI) hash.get("PARENT");
                    hash.remove("PARENT");
                    viewType = AUTHORIZE;
                    observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
                    observable.setStatus();
                    btnReject.setEnabled(false);
                    rejectFlag = 1;
                }
                if (hash.containsKey("FROM_CASHIER_APPROVAL_REJ_UI")) {
                    fromAuthorizeUI = false;
                    fromManagerAuthorizeUI = false;
                    fromCashierAuthorizeUI = false;
                    viewType = AUTHORIZE;
                    observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
                    observable.setStatus();
                    //btnSaveDisable();
                }
                if (hash.containsKey("FROM_SMART_CUSTOMER_UI")) {
                    fromSmartCustUI= true;
                    smartUI = (SmartCustomerUI) hash.get("PARENT");
                    hash.remove("PARENT");
                    btnNewActionPerformed(null);
                    //viewType = ACCNO;
                    cboProductID.setSelectedItem(CommonUtil.convertObjToStr(hash.get("PROD_DESC")));
                    txtAccountNumber.setText(CommonUtil.convertObjToStr(hash.get("ACT_NUM")));
                    txtAccountNumberFocusLost(null);
                    observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
                    observable.setStatus();
                    viewType = -1;
                }
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || hash.containsKey("FROM_TRANSACTION_SCREEN")) {
                    if (hash.containsKey("FROM_TRANSACTION_SCREEN")) {
                        btnNewActionPerformed(null);
                        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
                        cboProductID.setSelectedItem(CommonUtil.convertObjToStr(hash.get("PROD_ID")));
                    }
                    if(!hash.containsKey("FROM_SMART_CUSTOMER_UI")) {
                   	fillDataNew(hash);
                    }                    
                } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
                    fillDataEdit(hash);
                    setButtonEnableDisable();
                    enableDisable(true);
                    btnAccountNumber.setEnabled(false);
                    observable.setStatus();
                    if (observable.getLinkMap() == null) {
                        checkForEdit();
                    }
                    transactionUI.enableDisableForActClosing(false);
                    cboProductID.setEnabled(false);
                    if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
                        ClientUtil.enableDisable(panProductID, false);
                    }
                } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION) {
                    hash.put("ACT_NUM", hash.get("ACCOUNTNO"));
                    fillDataEdit(hash);
                    setButton4Authorize();
                    transactionUI.setCallingUiMode(observable.getActionType());
                    isAuthRecord = true;
                    viewType = AUTHORIZE;
                }
                transactionUI.setSourceAccountNumber(txtAccountNumber.getText());
                transactionUI.setCallingApplicantName(lblCustomerNameDisplay.getText());
                if (viewType == AUTHORIZE) {
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                     btnAuthorize.requestDefaultFocus();
                }
                if (observable.getLinkMap() != null && observable.getLinkMap().containsKey("AS_CUSTOMER_COMES")
                        && observable.getLinkMap().get("AS_CUSTOMER_COMES").equals("Y") && observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                    btnSave.setEnabled(false);
                }
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE) {
                    HashMap transTypeMap = new HashMap();
                    HashMap transMap = new HashMap();
                    HashMap transCashMap = new HashMap();
                    String reportName = "";
                    String transferDisplayStr = "";
                    String cashDisplayStr = "";
                    String displayStr = "";
                    String actNum = "";
                    transCashMap.put("BATCH_ID", observable.getTxtAccountNumber());
                    transCashMap.put("TRANS_DT", currDt);
                    transCashMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                    if (prodType.equals("TermLoan")) {
                      transCashMap.put("AUTHORIZE_STATUS", "AUTHORIZE_STATUS");
                      transCashMap.put("SCREEN_NAME", "LOAN FUTURE TRANSACTION");
                    }
                    HashMap transIdMap = new HashMap();
                    List transferList = ClientUtil.executeQuery("getTransferDetails", transCashMap);
                    if (transferList != null && transferList.size() > 0) {
                        transferDisplayStr += "Transfer Transaction Details...\n";
                        for (int i = 0; i < transferList.size(); i++) {
                            transMap = (HashMap) transferList.get(i);
                            transIdMap.put(transMap.get("SINGLE_TRANS_ID"), "TRANSFER");
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
                    }
                    List cashList = ClientUtil.executeQuery("getCashDetails", transCashMap);
                        if (cashList != null && cashList.size() > 0) {
                            cashDisplayStr += "Cash Transaction Details...\n";
                            for (int i = 0; i < cashList.size(); i++) {
                                transMap = (HashMap) cashList.get(i);
                                transIdMap.put(transMap.get("SINGLE_TRANS_ID"), "CASH");
                                transTypeMap.put(transMap.get("SINGLE_TRANS_ID"), transMap.get("TRANS_TYPE"));
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
                        }
                    if (!cashDisplayStr.equals("")) {
                        displayStr += cashDisplayStr;
                    }
                    if (!transferDisplayStr.equals("")) {
                        displayStr += transferDisplayStr;
                    }
                    if (!displayStr.equals("")) {
                        ClientUtil.showMessageWindow("" + displayStr);
                    }
                    int yesNo = 0;
                    String[] voucherOptions = {"Yes", "No"};
                    if ((transferList != null && transferList.size() > 0)||(cashList != null && cashList.size() > 0)) {
                        if(observable.getActionType() != ClientConstants.ACTIONTYPE_AUTHORIZE && observable.getActionType() != ClientConstants.ACTIONTYPE_REJECT){
                        yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                                null, voucherOptions, voucherOptions[0]);
                        if (yesNo == 0) {
                            com.see.truetransact.clientutil.ttrintegration.TTIntegration ttIntgration = null;
                            HashMap paramMap = new HashMap();
                            paramMap.put("TransDt", currDt);
                            paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
                            Object keys[] = transIdMap.keySet().toArray();
                            for (int i = 0; i < keys.length; i++) {
                                paramMap.put("TransId", keys[i]);
                                ttIntgration.setParam(paramMap);
                                if (CommonUtil.convertObjToStr(transIdMap.get(keys[i])).equals("TRANSFER")) {
                                    reportName = "ReceiptPayment";
                                } else if (prodType.equals("TermLoan")) {
                                    reportName = "CashReceipt";
                                } else {
                                    reportName = "CashPayment";
                                }
                                ttIntgration.integrationForPrint(reportName, false);
                            }
                        }
                    }
                  }
                }
                if (hash.containsKey("FROM_AUTHORIZE_LIST_UI")) {
                    btnReject.setEnabled(true);
                    btnCancel.setEnabled(true);
                    btnView.setEnabled(false);
                }
                if (hash.containsKey("FROM_MANAGER_AUTHORIZE_LIST_UI")) {
                    btnReject.setEnabled(true);
                    btnCancel.setEnabled(true);
                    btnView.setEnabled(false);
                }
                if (hash.containsKey("FROM_CASHIER_AUTHORIZE_LIST_UI")) {
                    btnReject.setEnabled(true);
                    btnCancel.setEnabled(true);
                    btnView.setEnabled(false);
                }
                if (hash.containsKey("LOAN_LIABILITY")) {
                    if (hash.get("ACT_NUM").toString() != null) {
                        txtAccountNumber.setText(hash.get("ACT_NUM").toString()); 
                         HashMap supMap = new HashMap();
                        supMap.put("ACCT_NUM", txtAccountNumber.getText());
                        txtAccountNumberFocusLost(null);
                    }
                }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e);
        }
        if (rejectFlag == 1) {
            btnReject.setEnabled(false);
        }
        
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ){
	        btnAuthorize.setEnabled(true);
	        btnAuthorize.requestFocusInWindow();
            btnAuthorize.requestFocus(true);
       } 
    }

    private void setAsAnCustomerDetails() {
        //AS AN WHEN CUSTOMER
        if (prodType.equals("TermLoan")) {
            HashMap asAndWhenMap = new HashMap();
            if (observable.getTxtAccountNumber() != null && observable.getTxtAccountNumber().length() > 0) {
                HashMap mapHash = observable.asAnWhenCustomerComesYesNO(observable.getTxtAccountNumber());
                if (mapHash.containsKey("AS_CUSTOMER_COMES") && mapHash.get("AS_CUSTOMER_COMES") != null && mapHash.get("AS_CUSTOMER_COMES").equals("Y")) {
                    asAndWhenMap = interestCalculationTLAD(observable.getTxtAccountNumber());
                    if (asAndWhenMap != null && asAndWhenMap.size() > 0) {
                        if (mapHash != null && mapHash.size() > 0) {
                            asAndWhenMap.put("INSTALL_TYPE", mapHash.get("INSTALL_TYPE"));
                        }
                        transDetailsUI.setAsAndWhenMap(asAndWhenMap);
                    }
                }
            }
        }
    }

    private void checkForEdit() {
        // To enable disable controls for EDIT operation
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT
                || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
            enableDisable(true);
            cboProductID.setEnabled(false);
            txtAccountNumber.setEnabled(false);
        }
    }
    /**
     * To fillData for a new entry
     */
    private void fillDataNew(HashMap hash) {
        if (!hash.isEmpty()) {
            txtAccountNumber.setText((String) hash.get("ACCOUNT NUMBER"));            
            HashMap supMap = new HashMap();
            supMap.put("ACCT_NUM", txtAccountNumber.getText());        
            observable.setTxtAccountNumber(txtAccountNumber.getText());
                if (hash != null && hash.containsKey("CUSTOMER NAME")) {
                    observable.setCustomerName(CommonUtil.convertObjToStr(hash.get("CUSTOMER NAME")));
                }
                if (hash != null && hash.containsKey("HOUSE_NAME")) {
                    observable.setCustomerStreet(CommonUtil.convertObjToStr(hash.get("HOUSE_NAME")));
                }
            if (txtAccountNumber.getText().length() > 0) {
                HashMap where = new HashMap();
                where.put("ACT_NUM", txtAccountNumber.getText());
                 // interest details in Loan Trans Details
                 setAsAnCustomerDetails();
                if (prodType.equals("TermLoan")&& observable.getFutureDate() != null) {
                    String od = (String) observable.getCbmProductID().getKeyForSelected();
                      transDetailsUI.setIsDebitSelect(true);
                        transDetailsUI.setTransDetails("TL", ProxyParameters.BRANCH_ID, txtAccountNumber.getText());
                        transDetailsUI.setSourceFrame(this);
                        if (observable.getActionType() != ClientConstants.ACTIONTYPE_AUTHORIZE && observable.getActionType() != ClientConstants.ACTIONTYPE_REJECT) {
                            if (chkAuthorizationPending()) {
                            }else{
                                txtAccountNumber.setText("");
                                return;
                            }
                        }
                } 
            }
            showCustomerName();
        } else {
            this.txtAccountNumber.setText("");
            transDetailsUI.setTransDetails(null, null, null);
            transDetailsUI.setSourceFrame(null);
        }
       observable.ttNotifyObservers();
    }
    private HashMap interestCalculationTLAD(String accountNo) {
        HashMap map = new HashMap();
        HashMap hash = null;
        try {
            if (observable.getFutureDate() == null) {
               ClientUtil.showAlertWindow(objMandatoryMRB.getString("tdtFutureDate"));
                btnCancelActionPerformed(null);
                hash = new HashMap();
                return hash;
            }
            map.put("ACT_NUM", accountNo);
            String prod_id = ((ComboBoxModel) cboProductID.getModel()).getKeyForSelected().toString();
            map.put("PROD_ID", prod_id); 
            map.put("TRANS_DT", observable.getFutureDate());
            map.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
            List lst = null;
            if (cboProdType.getSelectedItem().equals("Advances")) {
                lst = ClientUtil.executeQuery("IntCalculationDetailAD", map);
            } else {
                lst = ClientUtil.executeQuery("IntCalculationDetail", map);
            }
            if (lst != null && lst.size() > 0) {
                hash = (HashMap) lst.get(0);
                if (hash.get("AS_CUSTOMER_COMES") != null && hash.get("AS_CUSTOMER_COMES").equals("N")) {
                    hash = new HashMap();
                    return hash;
                }
                map.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
                map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
                map.putAll(hash);
                map.put("LOAN_ACCOUNT_CLOSING", "LOAN_ACCOUNT_CLOSING");
                map.put("CURR_DATE", observable.getFutureDate());
                map.put("PREMATURE_ONEMONTH_INT", "PREMATURE_ONEMONTH_INT");
                map.put("SOURCE_SCREEN", "LOAN_CLOSING");
                 map.put("TRANS_DT", observable.getFutureDate());
                hash = observable.loanInterestCalculationAsAndWhen(map);
                if (hash == null) {
                    hash = new HashMap();
                }
                hash.put("AS_CUSTOMER_COMES", map.get("AS_CUSTOMER_COMES"));
                actOpenDt = (Date) map.get("ACCT_OPEN_DT");
                System.out.println("mapppppppppp$@#$@#"+map);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return hash;
    }

    /**
     * To fillData for existing entry
     */
    private void fillDataEdit(HashMap hash) {
        String acctNum = null;
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION) {
            acctNum = hash.get("ACT_NUM").toString();
        } else {
            acctNum = hash.get("ACCOUNT NUMBER").toString();
        }
        txtAccountNumber.setText(acctNum);
        observable.setTxtAccountNumber(txtAccountNumber.getText());
        HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, acctNum);
        observable.getData(where);
        setAsAnCustomerDetails();
        if (prodType.equals("TermLoan")) {
            transDetailsUI.setTransDetails("TL", ProxyParameters.BRANCH_ID, acctNum);
        } 
        transDetailsUI.setSourceFrame(this);

        observable.setTotalLoanAmount(transDetailsUI.getTermLoanCloseCharge());
//        setCreditIntOD();
        setModified(true);
        observable.ttNotifyObservers();
        updateOBFields();
        observable.ttNotifyObservers();
        if (prodType.equals("TermLoan")) {
            observable.showTransaction();
        }
        btnAuthorize.requestFocus();
        grabFocus();
    }
    /**
     * To get popUp data for a new entry
     */
    private HashMap accountViewMap() {
        final HashMap testMap = new HashMap();
        //        prodType="TermLoan";
        final HashMap whereMap = new HashMap();
        testMap.put("MAPNAME", "getSelectTermLoanList");
        whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.selBranch);
        testMap.put(CommonConstants.MAP_WHERE, whereMap);
        log.info("productID : " + observable.getCboProductID());
        whereMap.put("PRODID", observable.getCboProductID());
        testMap.put(CommonConstants.MAP_WHERE, whereMap);
        return testMap;
    }

    /**
     * To get popUp data for a new entry
     */
    private HashMap accountViewMap(String actNum) {
        HashMap testMap = new HashMap();
        String checkval = "";
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW && !prodType.equals("GL")) {
            String prodId = "";
            prodId = ((ComboBoxModel) cboProductID.getModel()).getKeyForSelected().toString();
            checkval = CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID) + prodId;
        }
        //        prodType="TermLoan";
        if (actNum != null && actNum.length() > 0) {
            if (!txtAccountNumber.getText().equals(CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID))) {
                String mapName = null;
                if (prodType.equals("TermLoan")) {
                    mapName = "getSelectTermLoanList";
                }
                final HashMap whereMap = new HashMap();
                whereMap.put("PRODID", observable.getCboProductID());
                whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                whereMap.put(CommonConstants.ACT_NUM, actNum);
                List lst = ClientUtil.executeQuery(mapName, whereMap);
                if (lst != null && lst.size() > 0) {
                    testMap = (HashMap) lst.get(0);
                    fillData(testMap);
                } else {
                    if (!actNum.equals(checkval)) {
                        ClientUtil.showMessageWindow("Invalid Account No");
                        txtAccountNumber.setText("");
                        txtAccountNumber.setText(CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID));
                    }
                }
            }
        }
        return testMap;
    }

    /**
     * To get popUp data for already existing entries for modification
     */
    private HashMap accountEditMap() {
        final HashMap testMap = new HashMap();
        ArrayList lst = new ArrayList();
        lst.add("ACCOUNT NUMBER");
        testMap.put(ClientConstants.RECORD_KEY_COL, lst);
        lst = null;
        if (prodType.equals("TermLoan")) {
            testMap.put("MAPNAME", "getSelectClosingAccountListTL");
        } 
        HashMap whereMap = new HashMap();
        whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
        testMap.put(CommonConstants.MAP_WHERE, whereMap);
        return testMap;
    }

    /**
     * To display customer related details based on account number
     */
    private void showCustomerName() {
        updateOBFields();
        observable.setTotalLoanAmount(transDetailsUI.getTermLoanCloseCharge());
    }

    private void enableDisable(boolean yesno) {
        ClientUtil.enableDisable(this, yesno);
        txtAccountNumber.setEnabled(false);
        txtAccountNumber.setEditable(false);        
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

        btnAccountNumber.setEnabled(!btnNew.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
    }

    private void setButton4Authorize() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(btnNew.isEnabled());
        btnDelete.setEnabled(btnNew.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());

        btnSave.setEnabled(btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());

        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
    }

    public void update(Observable observed, Object arg) {
        //cboProductID.setSelectedItem( (observable.getCbmProductID()).getDataForKey(observable.getCboProductID()));
        if (observable.getActionType() == 1 || observable.getActionType() == 2 || observable.getActionType() == 8 || observable.getActionType() == 10) {
            if (cboProductID.getSelectedItem() == null || cboProductID.getSelectedItem().equals("")) {
                cboProductID.setSelectedItem((observable.getCbmProductID()).getDataForKey(observable.getCboProductID()));
            }
        }
        txtAccountNumber.setText(observable.getTxtAccountNumber());
        if (cboProductID.getSelectedItem() == null || cboProductID.getSelectedItem().equals("")) {
            cboProductID.setSelectedItem((observable.getCbmProductID()).getDataForKey(observable.getCboProductID()));
        }
        lblCustomerNameDisplay.setText(observable.getCustomerName());
        lblHouseName.setText(observable.getCustomerStreet());
        lblStatus.setText(observable.getLblStatus());
        lblServiceTaxval.setText(observable.getLblServiceTaxval());
        tdtFutureDate.setDateValue(CommonUtil.convertObjToStr(observable.getFutureDate()));
        txtPayableAmount.setText(CommonUtil.convertObjToStr(observable.getTxtTransAmount()));
    }

    public void update1(Observable observed, Object arg) {
        //cboProductID.setSelectedItem( (observable.getCbmProductID()).getDataForKey(observable.getCboProductID()));
        txtAccountNumber.setText(observable.getTxtAccountNumber());
        lblCustomerNameDisplay.setText(observable.getCustomerName());
        lblHouseName.setText(observable.getCustomerStreet());
        lblStatus.setText(observable.getLblStatus());
        lblServiceTaxval.setText(observable.getLblServiceTaxval());
    }
    
    public void updateOBFields() {
        if (CommonUtil.convertObjToStr(cboProductID.getSelectedItem()).length() > 0) {
            observable.setTxtAccountNumber(txtAccountNumber.getText());
            observable.setFutureDate(DateUtil.getDateMMDDYYYY(tdtFutureDate.getDateValue()));
        }
        observable.setCboProductID((String) (((ComboBoxModel) (cboProductID).getModel())).getKeyForSelected());
        observable.setLblServiceTaxval(lblServiceTaxval.getText());
        observable.setServiceTax_Map(serviceTax_Map);
        observable.setScreen(this.getScreen());
        if (cboProdType.getSelectedItem().equals("Advances")) {
            observable.setTxtProdType("AD");
        } else {
            observable.setTxtProdType("TL");
        }
        observable.setTxtTransAmount(CommonUtil.convertObjToDouble(txtPayableAmount.getText()));
    }

    public void updateSave() {
//        observable.setTxtAccountClosingCharges(txtAccountClosingCharges.getText());
    }

    private void setFieldNames() {
        btnAccountNumber.setName("btnAccountNumber");
        btnSave.setName("btnSave");
        cboProductID.setName("cboProductID");
        lblAccountNumber.setName("lblAccountNumber");
        lblCustomerNameDisplay.setName("lblCustomerNameDisplay");
        lblProductID.setName("lblProductID");
        mbrCustomer.setName("mbrCustomer");
        panAccountHead.setName("panAccountHead");
        panAccountInfo.setName("panAccountInfo");
        panProductID.setName("panProductID");
        sptAccountInfo.setName("sptAccountInfo");
        txtAccountNumber.setName("txtAccountNumber");
        lblHouseName.setName("lblHouseName");
         tdtFutureDate.setName("tdtFromDate");
    }

    private void internationalize() {
       LoanTransactionRB resourceBundle = new LoanTransactionRB();
        lblAccountNumber.setText(resourceBundle.getString("lblAccountNumber"));
        btnAccountNumber.setText(resourceBundle.getString("btnAccountNumber"));
        lblProductID.setText(resourceBundle.getString("lblProductID"));
        lblCustomerNameDisplay.setText(resourceBundle.getString("lblCustomerNameDisplay"));
    }

    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAccountNumber;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CButton btnWaive;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CComboBox cboProdType;
    private com.see.truetransact.uicomponent.CComboBox cboProductID;
    private com.see.truetransact.uicomponent.CLabel lblAccountNumber;
    private com.see.truetransact.uicomponent.CLabel lblCustomerNameDisplay;
    private com.see.truetransact.uicomponent.CLabel lblHouseName;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblProductID;
    private com.see.truetransact.uicomponent.CLabel lblServiceTax;
    private com.see.truetransact.uicomponent.CLabel lblServiceTaxval;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace70;
    private com.see.truetransact.uicomponent.CLabel lblSpace71;
    private com.see.truetransact.uicomponent.CLabel lblSpace72;
    private com.see.truetransact.uicomponent.CLabel lblSpace73;
    private com.see.truetransact.uicomponent.CLabel lblSpace74;
    private com.see.truetransact.uicomponent.CLabel lblSpace75;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblToDate;
    private com.see.truetransact.uicomponent.CLabel lblTransAmount;
    private com.see.truetransact.uicomponent.CMenuBar mbrCustomer;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAccountHead;
    private com.see.truetransact.uicomponent.CPanel panAccountInfo;
    private com.see.truetransact.uicomponent.CPanel panAccountInfoInner;
    private com.see.truetransact.uicomponent.CPanel panAcctNum;
    private com.see.truetransact.uicomponent.CPanel panProductID;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTransaction;
    private com.see.truetransact.uicomponent.CButtonGroup rdoTransactionType;
    private com.see.truetransact.uicomponent.CSeparator sptAccountInfo;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private javax.swing.JToolBar tbrOperativeAcctProduct;
    private com.see.truetransact.uicomponent.CDateField tdtFutureDate;
    private com.see.truetransact.uicomponent.CTextField txtAccountNumber;
    private com.see.truetransact.uicomponent.CTextField txtPayableAmount;
    // End of variables declaration//GEN-END:variables
    private javax.swing.JScrollPane srpChargeDetails;

    public static void main(String[] args) {
        LoanTransactionUI ac = new LoanTransactionUI();
        javax.swing.JFrame j = new javax.swing.JFrame();
        j.getContentPane().add(ac);
        j.show();
        ac.show();
    }

    
    public int getViewType() {
        return viewType;
    }

    
    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
    private void callServiceTaxCalculation(String calAmt) {
        calMap = new HashMap();
        calMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
        calMap.put("CAL_AMT", calAmt);
        calMap.put("ST_CAL", "ST_CAL");
        calMap.put("CHARGE_TYPE", "ACT_CLOSING_CHG");
        HashMap viewMap = new HashMap();
        viewMap.put("SERVICE_TAX", calMap);
        List clLst = ClientUtil.executeQuery("", viewMap);
        if (clLst != null && clLst.size() > 0) {
            calMap = (HashMap) clLst.get(0);
            if (CommonUtil.convertObjToDouble(calMap.get("SERVICE_TAX")).doubleValue() > 0
                    || CommonUtil.convertObjToDouble(calMap.get("CESS1_TAX")).doubleValue() > 0
                    || CommonUtil.convertObjToDouble(calMap.get("CESS2_TAX")).doubleValue() > 0) {
                ClientUtil.showAlertWindow("Service Tax :   " + calMap.get("SERVICE_TAX") + "\n"
                        + "Cess1 Tax :    " + calMap.get("CESS1_TAX") + "\n"
                        + "Cess2 Tax :    " + calMap.get("CESS2_TAX"));
            }
           
        }
    }
}
