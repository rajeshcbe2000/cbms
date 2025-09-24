/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * DailyLoanTransUI.java
 * Created on December 24, 2012, 11:27 AM
 *
 */
package com.see.truetransact.ui.termloan.dailyLoanTrans;

/**
 *
 * @author Suresh
 *
 **/
import java.awt.*;
import java.text.ParseException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.util.List;
import javax.swing.table.*;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.transferobject.agent.AgentTO;
import com.see.truetransact.ui.common.viewall.AuthorizeListUI;
import com.see.truetransact.ui.common.viewall.NewAuthorizeListUI;
import com.see.truetransact.ui.common.viewall.TextUI;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;

public class DailyLoanTransUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer, UIMandatoryField {

    private final static ClientParseException parseException = ClientParseException.getInstance();
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.mdsapplication.MDSApplicationRB", ProxyParameters.LANGUAGE);
    private HashMap mandatoryMap;
    final String AUTHORIZE = "Authorize";
    private String viewType = new String();
    private List finalList = null;
    private List adjustmentList = null;
    public int selectedRow = -1;
    private boolean isFilled = false;
    DailyLoanTransOB observable = null;
    private Date curDate = null;
    AuthorizeListUI authorizeListUI = null;
    boolean fromAuthorizeUI = false;
    private int rejectFlag = 0;
    private String type = "";
    private double commPercentage = 0;
    NewAuthorizeListUI newauthorizeListUI = null;
    boolean fromNewAuthorizeUI = false;
    
    /** Creates new form BeanForm */
    public DailyLoanTransUI() {
        initComponents();
        settingupUI();
        tabDailyLoanTransaction.resetVisits();
    }

    private void settingupUI() {
        setFieldNames();
        observable = new DailyLoanTransOB();
        initComponentData();
        setMaximumLength();
        setButtonEnableDisable();
        ClientUtil.enableDisable(panDailyLoanTransaction, false);
        ClientUtil.enableDisable(panLoanAdjustment, false);
        lblAgentNameVal.setText("                                ");
        lblLoanAdjustmentAgentNameVal.setText("                                ");
        curDate = ClientUtil.getCurrentDate();
        btnDisplay.setEnabled(false);
        btnLoanAdjustmentDisplay.setEnabled(false);
    }

    /* Auto Generated Method - setFieldNames()
    This method assigns name for all the components.
    Other functions are working based on this name. */
    private void setFieldNames() {
    }

    /** Auto Generated Method - setMandatoryHashMap()
     * This method list out all the Input Fields available in the UI.
     * It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
    }

    /** Auto Generated Method - getMandatoryHashMap()
     * Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    private void initComponentData() {
        try {
            cboAgentType.setModel(observable.getCbmAgentType());
            cboLoanAdjustmentAgentType.setModel(observable.getCbmLoanAdjustmentAgentType());
            TableCellEditor editor = new DefaultCellEditor(txtPayment);
            SimpleTableModel stm = new SimpleTableModel((ArrayList) observable.getTableList(), (ArrayList) observable.getTableTitle());
            tblDailyLoanTransactionTable.setModel(stm);
            tblDailyLoanTransactionTable.getColumnModel().getColumn(10).setCellEditor(editor);
            tblDailyLoanTransactionTable.revalidate();
            tblLoanAdjustmentTable.setModel(observable.getTblLoanAdjustmentTabls());
        } catch (ClassCastException e) {
            parseException.logException(e, true);
        }
    }

    private void setMaximumLength() {
        txtTotalPayment.setValidation(new CurrencyValidation());
        txtTotalCommission.setValidation(new CurrencyValidation());
        txtLoanAdjustmentTotalPayment.setValidation(new CurrencyValidation());
        txtPayment.setAllowAll(true);
    }

    /** Auto Generated Method - update()
     * This method called by Observable. It updates the UI with
     * Observable's data. If needed add/Remove RadioButtons
     * method need to be added.*/
    public void update(Observable observed, Object arg) {
    }

    /** Auto Generated Method - updateOBFields()
     * This method called by Save option of UI.
     * It updates the OB with UI data.*/
    public void updateOBFields() {
    }

    /** Auto Generated Method - setHelpMessage()
     * This method shows tooltip help for all the input fields
     * available in the UI. It needs the Mandatory Resource Bundle
     * object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
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
        lblSpace62 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace63 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace64 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace65 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace66 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace67 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        tabDailyLoanTransaction = new com.see.truetransact.uicomponent.CTabbedPane();
        panDailyLoanTransaction = new com.see.truetransact.uicomponent.CPanel();
        panDailyLoanTransactionTable = new com.see.truetransact.uicomponent.CPanel();
        srpDailyLoanTransactionTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblDailyLoanTransactionTable = new com.see.truetransact.uicomponent.CTable();
        panAgentDetails = new com.see.truetransact.uicomponent.CPanel();
        lblAgentType = new com.see.truetransact.uicomponent.CLabel();
        cboAgentType = new com.see.truetransact.uicomponent.CComboBox();
        lblAgentName = new com.see.truetransact.uicomponent.CLabel();
        lblAgentNameVal = new com.see.truetransact.uicomponent.CLabel();
        lblCollectionDt = new com.see.truetransact.uicomponent.CLabel();
        tdtCollectionDt = new com.see.truetransact.uicomponent.CDateField();
        btnDisplay = new com.see.truetransact.uicomponent.CButton();
        btnClear = new com.see.truetransact.uicomponent.CButton();
        panLoadFromFile = new com.see.truetransact.uicomponent.CPanel();
        txtFileName = new com.see.truetransact.uicomponent.CTextField();
        lblFileName = new com.see.truetransact.uicomponent.CLabel();
        btnBrowse = new com.see.truetransact.uicomponent.CButton();
        panTotalPayment = new com.see.truetransact.uicomponent.CPanel();
        txtTotalPayment = new com.see.truetransact.uicomponent.CTextField();
        lblTotalPayment = new com.see.truetransact.uicomponent.CLabel();
        txtPayment = new com.see.truetransact.uicomponent.CTextField();
        txtTotalCommission = new com.see.truetransact.uicomponent.CTextField();
        lblTotalCommission = new com.see.truetransact.uicomponent.CLabel();
        panLoanAdjustment = new com.see.truetransact.uicomponent.CPanel();
        panLoanAdjustmentTable = new com.see.truetransact.uicomponent.CPanel();
        srpLoanAdjustmentTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblLoanAdjustmentTable = new com.see.truetransact.uicomponent.CTable();
        panLoanAdjustmentAgentDetails = new com.see.truetransact.uicomponent.CPanel();
        lblLoanAdjustmentAgentType = new com.see.truetransact.uicomponent.CLabel();
        cboLoanAdjustmentAgentType = new com.see.truetransact.uicomponent.CComboBox();
        lblLoanAdjustmentAgentName = new com.see.truetransact.uicomponent.CLabel();
        lblLoanAdjustmentAgentNameVal = new com.see.truetransact.uicomponent.CLabel();
        lblLoanAdjustmentCollectionDt = new com.see.truetransact.uicomponent.CLabel();
        tdtLoanAdjustmentCollectionDt = new com.see.truetransact.uicomponent.CDateField();
        btnLoanAdjustmentDisplay = new com.see.truetransact.uicomponent.CButton();
        btnLoanAdjustmentClear = new com.see.truetransact.uicomponent.CButton();
        panLoanAdjustmentTotalPayment = new com.see.truetransact.uicomponent.CPanel();
        txtLoanAdjustmentTotalPayment = new com.see.truetransact.uicomponent.CTextField();
        lblLoanAdjustmentTotalPayment = new com.see.truetransact.uicomponent.CLabel();
        txtLoanAdjustmentPayment = new com.see.truetransact.uicomponent.CTextField();
        panSelectAll = new com.see.truetransact.uicomponent.CPanel();
        lblSelectAll = new com.see.truetransact.uicomponent.CLabel();
        chkSelectAll = new com.see.truetransact.uicomponent.CCheckBox();
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
        setMinimumSize(new java.awt.Dimension(850, 600));
        setPreferredSize(new java.awt.Dimension(850, 600));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setEnabled(false);
        btnView.setFocusable(false);
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
        btnNew.setFocusable(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnNew);

        lblSpace62.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace62.setText("     ");
        lblSpace62.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace62.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace62.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace62);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.setFocusable(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnEdit);

        lblSpace63.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace63.setText("     ");
        lblSpace63.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace63.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace63.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace63);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.setFocusable(false);
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
        btnSave.setFocusable(false);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnSave);

        lblSpace64.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace64.setText("     ");
        lblSpace64.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace64.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace64.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace64);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.setEnabled(false);
        btnCancel.setFocusable(false);
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

        lblSpace65.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace65.setText("     ");
        lblSpace65.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace65.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace65.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace65);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.setFocusable(false);
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnException);

        lblSpace66.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace66.setText("     ");
        lblSpace66.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace66.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace66.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace66);

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
        btnPrint.setFocusable(false);
        tbrAdvances.add(btnPrint);

        lblSpace67.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace67.setText("     ");
        lblSpace67.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace67.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace67.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace67);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.setFocusable(false);
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

        tabDailyLoanTransaction.setMinimumSize(new java.awt.Dimension(850, 480));
        tabDailyLoanTransaction.setPreferredSize(new java.awt.Dimension(850, 480));

        panDailyLoanTransaction.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panDailyLoanTransaction.setMinimumSize(new java.awt.Dimension(840, 450));
        panDailyLoanTransaction.setPreferredSize(new java.awt.Dimension(840, 715));
        panDailyLoanTransaction.setLayout(new java.awt.GridBagLayout());

        panDailyLoanTransactionTable.setBorder(javax.swing.BorderFactory.createTitledBorder("Loan Details"));
        panDailyLoanTransactionTable.setMinimumSize(new java.awt.Dimension(825, 300));
        panDailyLoanTransactionTable.setPreferredSize(new java.awt.Dimension(825, 300));
        panDailyLoanTransactionTable.setLayout(new java.awt.GridBagLayout());

        srpDailyLoanTransactionTable.setMinimumSize(new java.awt.Dimension(800, 270));
        srpDailyLoanTransactionTable.setPreferredSize(new java.awt.Dimension(800, 270));

        tblDailyLoanTransactionTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Loan No", "Name", "Limit", "Last Int Date", "Balance", "Principal Due", "Interest", "Penal", "Charge", "Total Due", "Repayment", "Commission"
            }
        ));
        tblDailyLoanTransactionTable.setPreferredScrollableViewportSize(new java.awt.Dimension(796, 196));
        tblDailyLoanTransactionTable.setPreferredSize(new java.awt.Dimension(800, 2000));
        tblDailyLoanTransactionTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDailyLoanTransactionTableMouseClicked(evt);
            }
        });
        tblDailyLoanTransactionTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblDailyLoanTransactionTableKeyReleased(evt);
            }
        });
        srpDailyLoanTransactionTable.setViewportView(tblDailyLoanTransactionTable);

        panDailyLoanTransactionTable.add(srpDailyLoanTransactionTable, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 2, 0);
        panDailyLoanTransaction.add(panDailyLoanTransactionTable, gridBagConstraints);

        panAgentDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Agent Details"));
        panAgentDetails.setMinimumSize(new java.awt.Dimension(825, 110));
        panAgentDetails.setPreferredSize(new java.awt.Dimension(825, 110));
        panAgentDetails.setLayout(null);

        lblAgentType.setText("Agent Id");
        panAgentDetails.add(lblAgentType);
        lblAgentType.setBounds(220, 13, 48, 20);

        cboAgentType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboAgentType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboAgentTypeActionPerformed(evt);
            }
        });
        panAgentDetails.add(cboAgentType);
        cboAgentType.setBounds(280, 15, 100, 20);

        lblAgentName.setText("Agent Name");
        panAgentDetails.add(lblAgentName);
        lblAgentName.setBounds(200, 40, 72, 18);

        lblAgentNameVal.setForeground(new java.awt.Color(0, 51, 204));
        lblAgentNameVal.setText("AgentNamVal");
        lblAgentNameVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblAgentNameVal.setMaximumSize(new java.awt.Dimension(250, 18));
        lblAgentNameVal.setMinimumSize(new java.awt.Dimension(250, 18));
        lblAgentNameVal.setPreferredSize(new java.awt.Dimension(250, 18));
        panAgentDetails.add(lblAgentNameVal);
        lblAgentNameVal.setBounds(280, 40, 268, 18);

        lblCollectionDt.setText("Collection Date");
        panAgentDetails.add(lblCollectionDt);
        lblCollectionDt.setBounds(180, 70, 88, 18);

        tdtCollectionDt.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtCollectionDt.setPreferredSize(new java.awt.Dimension(21, 200));
        panAgentDetails.add(tdtCollectionDt);
        tdtCollectionDt.setBounds(280, 70, 100, 21);

        btnDisplay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnDisplay.setText("DISPLAY");
        btnDisplay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDisplayActionPerformed(evt);
            }
        });
        panAgentDetails.add(btnDisplay);
        btnDisplay.setBounds(490, 70, 113, 29);

        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        panAgentDetails.add(btnClear);
        btnClear.setBounds(630, 70, 87, 29);

        panLoadFromFile.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panLoadFromFile.setMinimumSize(new java.awt.Dimension(200, 70));
        panLoadFromFile.setPreferredSize(new java.awt.Dimension(200, 90));
        panLoadFromFile.setLayout(new java.awt.GridBagLayout());

        txtFileName.setMinimumSize(new java.awt.Dimension(280, 21));
        txtFileName.setPreferredSize(new java.awt.Dimension(280, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = -140;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 30, 0, 0);
        panLoadFromFile.add(txtFileName, gridBagConstraints);

        lblFileName.setText("File");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        panLoadFromFile.add(lblFileName, gridBagConstraints);

        btnBrowse.setText("Browse");
        btnBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 20, 10, 95);
        panLoadFromFile.add(btnBrowse, gridBagConstraints);

        panAgentDetails.add(panLoadFromFile);
        panLoadFromFile.setBounds(420, 10, 360, 50);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        panDailyLoanTransaction.add(panAgentDetails, gridBagConstraints);

        panTotalPayment.setMinimumSize(new java.awt.Dimension(825, 25));
        panTotalPayment.setPreferredSize(new java.awt.Dimension(825, 25));
        panTotalPayment.setLayout(new java.awt.GridBagLayout());

        txtTotalPayment.setBackground(new java.awt.Color(204, 204, 204));
        txtTotalPayment.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panTotalPayment.add(txtTotalPayment, gridBagConstraints);

        lblTotalPayment.setText("Total Payment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 100, 1, 0);
        panTotalPayment.add(lblTotalPayment, gridBagConstraints);

        txtPayment.setBackground(new java.awt.Color(204, 204, 204));
        txtPayment.setBorder(null);
        txtPayment.setMinimumSize(new java.awt.Dimension(10, 21));
        txtPayment.setPreferredSize(new java.awt.Dimension(10, 21));
        txtPayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPaymentActionPerformed(evt);
            }
        });
        txtPayment.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPaymentFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panTotalPayment.add(txtPayment, gridBagConstraints);

        txtTotalCommission.setBackground(new java.awt.Color(204, 204, 204));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        panTotalPayment.add(txtTotalCommission, gridBagConstraints);

        lblTotalCommission.setText("Total Commision");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 35, 0, 0);
        panTotalPayment.add(lblTotalCommission, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panDailyLoanTransaction.add(panTotalPayment, gridBagConstraints);

        tabDailyLoanTransaction.addTab("Daily Collection Loan Details", panDailyLoanTransaction);

        panLoanAdjustment.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panLoanAdjustment.setMinimumSize(new java.awt.Dimension(840, 450));
        panLoanAdjustment.setPreferredSize(new java.awt.Dimension(840, 715));
        panLoanAdjustment.setLayout(new java.awt.GridBagLayout());

        panLoanAdjustmentTable.setBorder(javax.swing.BorderFactory.createTitledBorder("Loan Details"));
        panLoanAdjustmentTable.setMinimumSize(new java.awt.Dimension(825, 280));
        panLoanAdjustmentTable.setPreferredSize(new java.awt.Dimension(825, 280));
        panLoanAdjustmentTable.setLayout(new java.awt.GridBagLayout());

        srpLoanAdjustmentTable.setMinimumSize(new java.awt.Dimension(800, 250));
        srpLoanAdjustmentTable.setPreferredSize(new java.awt.Dimension(800, 250));

        tblLoanAdjustmentTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Select", "Loan No", "Name", "Limit", "Last Int Date", "Balance", "Principal Due", "Interest", "Penal", "Charge", "Total Due", "Repayment"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblLoanAdjustmentTable.setPreferredScrollableViewportSize(new java.awt.Dimension(796, 196));
        tblLoanAdjustmentTable.setPreferredSize(new java.awt.Dimension(800, 2000));
        tblLoanAdjustmentTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblLoanAdjustmentTableMouseClicked(evt);
            }
        });
        tblLoanAdjustmentTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblLoanAdjustmentTableKeyReleased(evt);
            }
        });
        srpLoanAdjustmentTable.setViewportView(tblLoanAdjustmentTable);

        panLoanAdjustmentTable.add(srpLoanAdjustmentTable, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panLoanAdjustment.add(panLoanAdjustmentTable, gridBagConstraints);

        panLoanAdjustmentAgentDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Agent Details"));
        panLoanAdjustmentAgentDetails.setMinimumSize(new java.awt.Dimension(825, 110));
        panLoanAdjustmentAgentDetails.setPreferredSize(new java.awt.Dimension(825, 110));
        panLoanAdjustmentAgentDetails.setLayout(new java.awt.GridBagLayout());

        lblLoanAdjustmentAgentType.setText("Agent Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLoanAdjustmentAgentDetails.add(lblLoanAdjustmentAgentType, gridBagConstraints);

        cboLoanAdjustmentAgentType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboLoanAdjustmentAgentType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboLoanAdjustmentAgentTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLoanAdjustmentAgentDetails.add(cboLoanAdjustmentAgentType, gridBagConstraints);

        lblLoanAdjustmentAgentName.setText("Agent Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLoanAdjustmentAgentDetails.add(lblLoanAdjustmentAgentName, gridBagConstraints);

        lblLoanAdjustmentAgentNameVal.setForeground(new java.awt.Color(0, 51, 204));
        lblLoanAdjustmentAgentNameVal.setText("AgentNamVal");
        lblLoanAdjustmentAgentNameVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblLoanAdjustmentAgentNameVal.setMaximumSize(new java.awt.Dimension(250, 18));
        lblLoanAdjustmentAgentNameVal.setMinimumSize(new java.awt.Dimension(250, 18));
        lblLoanAdjustmentAgentNameVal.setPreferredSize(new java.awt.Dimension(250, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 18;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLoanAdjustmentAgentDetails.add(lblLoanAdjustmentAgentNameVal, gridBagConstraints);

        lblLoanAdjustmentCollectionDt.setText("Collection Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 175, 6, 4);
        panLoanAdjustmentAgentDetails.add(lblLoanAdjustmentCollectionDt, gridBagConstraints);

        tdtLoanAdjustmentCollectionDt.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtLoanAdjustmentCollectionDt.setPreferredSize(new java.awt.Dimension(21, 200));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 6, 4);
        panLoanAdjustmentAgentDetails.add(tdtLoanAdjustmentCollectionDt, gridBagConstraints);

        btnLoanAdjustmentDisplay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnLoanAdjustmentDisplay.setText("DISPLAY");
        btnLoanAdjustmentDisplay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoanAdjustmentDisplayActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLoanAdjustmentAgentDetails.add(btnLoanAdjustmentDisplay, gridBagConstraints);

        btnLoanAdjustmentClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnLoanAdjustmentClear.setText("Clear");
        btnLoanAdjustmentClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoanAdjustmentClearActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLoanAdjustmentAgentDetails.add(btnLoanAdjustmentClear, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        panLoanAdjustment.add(panLoanAdjustmentAgentDetails, gridBagConstraints);

        panLoanAdjustmentTotalPayment.setMinimumSize(new java.awt.Dimension(825, 25));
        panLoanAdjustmentTotalPayment.setPreferredSize(new java.awt.Dimension(825, 25));
        panLoanAdjustmentTotalPayment.setLayout(new java.awt.GridBagLayout());

        txtLoanAdjustmentTotalPayment.setBackground(new java.awt.Color(204, 204, 204));
        txtLoanAdjustmentTotalPayment.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panLoanAdjustmentTotalPayment.add(txtLoanAdjustmentTotalPayment, gridBagConstraints);

        lblLoanAdjustmentTotalPayment.setText("Total Payment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 540, 1, 0);
        panLoanAdjustmentTotalPayment.add(lblLoanAdjustmentTotalPayment, gridBagConstraints);

        txtLoanAdjustmentPayment.setBackground(new java.awt.Color(204, 204, 204));
        txtLoanAdjustmentPayment.setBorder(null);
        txtLoanAdjustmentPayment.setMinimumSize(new java.awt.Dimension(10, 21));
        txtLoanAdjustmentPayment.setPreferredSize(new java.awt.Dimension(10, 21));
        txtLoanAdjustmentPayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLoanAdjustmentPaymentActionPerformed(evt);
            }
        });
        txtLoanAdjustmentPayment.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtLoanAdjustmentPaymentFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panLoanAdjustmentTotalPayment.add(txtLoanAdjustmentPayment, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panLoanAdjustment.add(panLoanAdjustmentTotalPayment, gridBagConstraints);

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
        panLoanAdjustment.add(panSelectAll, gridBagConstraints);

        tabDailyLoanTransaction.addTab("Loan Adjustment", panLoanAdjustment);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(tabDailyLoanTransaction, gridBagConstraints);

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

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        clearTable();
        observable.resetForm();
        setSizeTableData();
        ClientUtil.clearAll(this);
        btnNewActionPerformed(null);
        setButtonEnableDisable();
        setModified(false);
    }//GEN-LAST:event_btnClearActionPerformed
    private void setSizeTableData() {
        tblDailyLoanTransactionTable.getColumnModel().getColumn(0).setPreferredWidth(105);
        tblDailyLoanTransactionTable.getColumnModel().getColumn(1).setPreferredWidth(135);
        tblDailyLoanTransactionTable.getColumnModel().getColumn(2).setPreferredWidth(65);
        tblDailyLoanTransactionTable.getColumnModel().getColumn(3).setPreferredWidth(85);
        tblDailyLoanTransactionTable.getColumnModel().getColumn(4).setPreferredWidth(75);
        tblDailyLoanTransactionTable.getColumnModel().getColumn(5).setPreferredWidth(75);
        tblDailyLoanTransactionTable.getColumnModel().getColumn(6).setPreferredWidth(65);
        tblDailyLoanTransactionTable.getColumnModel().getColumn(7).setPreferredWidth(65);
        tblDailyLoanTransactionTable.getColumnModel().getColumn(8).setPreferredWidth(65);
        tblDailyLoanTransactionTable.getColumnModel().getColumn(9).setPreferredWidth(95);
        tblDailyLoanTransactionTable.getColumnModel().getColumn(10).setPreferredWidth(100);
        tblDailyLoanTransactionTable.getColumnModel().getColumn(11).setPreferredWidth(70);
    }

    private void setSizeAdjustmentTableData() {
        tblLoanAdjustmentTable.getColumnModel().getColumn(0).setPreferredWidth(45);
        tblLoanAdjustmentTable.getColumnModel().getColumn(1).setPreferredWidth(105);
        tblLoanAdjustmentTable.getColumnModel().getColumn(2).setPreferredWidth(135);
        tblLoanAdjustmentTable.getColumnModel().getColumn(3).setPreferredWidth(65);
        tblLoanAdjustmentTable.getColumnModel().getColumn(4).setPreferredWidth(85);
        tblLoanAdjustmentTable.getColumnModel().getColumn(5).setPreferredWidth(75);
        tblLoanAdjustmentTable.getColumnModel().getColumn(6).setPreferredWidth(75);
        tblLoanAdjustmentTable.getColumnModel().getColumn(7).setPreferredWidth(65);
        tblLoanAdjustmentTable.getColumnModel().getColumn(8).setPreferredWidth(65);
        tblLoanAdjustmentTable.getColumnModel().getColumn(9).setPreferredWidth(65);
        tblLoanAdjustmentTable.getColumnModel().getColumn(10).setPreferredWidth(95);
        tblLoanAdjustmentTable.getColumnModel().getColumn(11).setPreferredWidth(100);
    }

    private void btnDisplaydetails() {
        System.out.println("ggggggggggggggggggg===" + cboAgentType.getSelectedIndex());
        //if(cboAgentType.getSelectedIndex()>0){
        clearTable();
        HashMap loanMap = new HashMap();
        loanMap.put("AGENT_ID", cboAgentType.getSelectedItem());
        System.out.println("vvvv======" + cboAgentType.getSelectedItem().toString());
        if (txtFileName.getText() != null) {
            try {
                System.out.println("bbbbbb======" + totCustomerList);
                observable.getTableDetails(totCustomerList, cboAgentType.getSelectedItem().toString());                
            } catch (ParseException ex) {
                Logger.getLogger(DailyLoanTransUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //else
        //observable.getTableDetails(loanMap);
        txtTotalPayment.setText("0.0");
        txtPayment.setEnabled(true);
        tblDailyLoanTransactionTable.setEnabled(true);
        ClientUtil.enableDisable(panAgentDetails, false);
        btnDisplay.setEnabled(false);
        setSizeTableData();
        ((SimpleTableModel) tblDailyLoanTransactionTable.getModel()).fireTableDataChanged();
        calc();
        return;
        // }
    }
    private void btnDisplayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDisplayActionPerformed
        // TODO add your handling code here:
        System.out.println("ggggggggggggggggggg===" + cboAgentType.getSelectedIndex());
        if (cboAgentType.getSelectedIndex() > 0) {
            clearTable();
            HashMap loanMap = new HashMap();
            loanMap.put("AGENT_ID", cboAgentType.getSelectedItem());
            System.out.println("vvvv======" + cboAgentType.getSelectedItem().toString());

            observable.getTableDetails(loanMap);            
            txtTotalPayment.setText("0.0");
            txtPayment.setEnabled(true);
            tblDailyLoanTransactionTable.setEnabled(true);
            ClientUtil.enableDisable(panAgentDetails, false);
            btnDisplay.setEnabled(false);
            setSizeTableData();
            ((SimpleTableModel) tblDailyLoanTransactionTable.getModel()).fireTableDataChanged();
            calc();
            return;
        }
    }//GEN-LAST:event_btnDisplayActionPerformed
    public void checkingPaymentAmount(int selectedRow) {
        double paymentAmt = 0;
        double totPayableAmt = 0;
        paymentAmt = CommonUtil.convertObjToDouble(tblDailyLoanTransactionTable.getValueAt(selectedRow, 10).toString()).doubleValue();
        if (paymentAmt > 0) {
            totPayableAmt = CommonUtil.convertObjToDouble(tblDailyLoanTransactionTable.getValueAt(selectedRow, 4).toString()).doubleValue()
                    + CommonUtil.convertObjToDouble(tblDailyLoanTransactionTable.getValueAt(selectedRow, 6).toString()).doubleValue()
                    + CommonUtil.convertObjToDouble(tblDailyLoanTransactionTable.getValueAt(selectedRow, 7).toString()).doubleValue()
                    + CommonUtil.convertObjToDouble(tblDailyLoanTransactionTable.getValueAt(selectedRow, 8).toString()).doubleValue();
            if (totPayableAmt >= paymentAmt) {
                if (totPayableAmt == paymentAmt) {
                    int yesNo = 0;
                    String[] options = {"Yes", "No"};
                    yesNo = COptionPane.showOptionDialog(null, "Do you want to Close the Account?", CommonConstants.WARNINGTITLE,
                            COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                            null, options, options[0]);
                    System.out.println("#$#$$ yesNo : " + yesNo);
                    if (yesNo == 0) {
                    } else {
                        tblDailyLoanTransactionTable.setValueAt("", selectedRow, 10);
                    }
                }
                calc();
            } else {
                ClientUtil.showMessageWindow("Payment Amount Should not Exceeds  Rs. " + totPayableAmt);
                tblDailyLoanTransactionTable.setValueAt("", selectedRow, 10);
                ((SimpleTableModel) tblDailyLoanTransactionTable.getModel()).fireTableDataChanged();
                calc();
            }
        }
    }

    public void calc() {
        double totPayment = 0;
        double totCommision = 0;
        if (tblDailyLoanTransactionTable.getRowCount() > 0) {
            for (int i = 0; i < tblDailyLoanTransactionTable.getRowCount(); i++) {                
                totPayment = totPayment + CommonUtil.convertObjToDouble(tblDailyLoanTransactionTable.getValueAt(i, 10)).doubleValue();
                totCommision = totCommision + CommonUtil.convertObjToDouble(tblDailyLoanTransactionTable.getValueAt(i, 11)).doubleValue();
                txtTotalPayment.setText(CurrencyValidation.formatCrore(String.valueOf(totPayment)));
                txtTotalCommission.setText(CurrencyValidation.formatCrore(String.valueOf(totCommision)));
            }
        }
    }
    private void cboAgentTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboAgentTypeActionPerformed
        // TODO add your handling code here:
        if (cboAgentType.getSelectedIndex() > 0) {
            HashMap agentMap = new HashMap();
            agentMap.put("AGENT_ID", cboAgentType.getSelectedItem());
            List lst = ClientUtil.executeQuery("getAgentDetailsName", agentMap);
            agentMap = null;
            if (lst != null && lst.size() > 0) {
                agentMap = (HashMap) lst.get(0);
                lblAgentNameVal.setText(CommonUtil.convertObjToStr(agentMap.get("AGENT_NAME")));
                btnDisplay.setEnabled(true);
            }
            //List commlst = ClientUtil.executeQuery("getAgentCommision", agentMap);
            //agentMap = null;
            //if (commlst != null && commlst.size() > 0) {
            //    agentMap = (HashMap) commlst.get(0);
            //    commPercentage = CommonUtil.convertObjToDouble(agentMap.get("COMMISION_PERCENTAGE"));
            //}
           // System.out.println("commPercentage#####"+commPercentage);
            
        } else {
            lblAgentNameVal.setText("                                ");
            btnDisplay.setEnabled(false);
        }
        if (observable.getActionType() != ClientConstants.ACTIONTYPE_NEW) {
            btnDisplay.setEnabled(false);
        }
    }//GEN-LAST:event_cboAgentTypeActionPerformed

    private void tblDailyLoanTransactionTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblDailyLoanTransactionTableKeyReleased
        // TODO add your handling code here:
        checkingPaymentAmount(selectedRow);
    }//GEN-LAST:event_tblDailyLoanTransactionTableKeyReleased

    private void txtPaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPaymentActionPerformed
    }//GEN-LAST:event_txtPaymentActionPerformed

    private void txtPaymentFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPaymentFocusLost
    }//GEN-LAST:event_txtPaymentFocusLost
    public class SimpleTableModel extends AbstractTableModel {

        private ArrayList dataVector;
        private ArrayList headingVector;

        public SimpleTableModel(ArrayList dataVector, ArrayList headingVector) {
            this.dataVector = dataVector;
            this.headingVector = headingVector;
        }

        public int getColumnCount() {
            return headingVector.size();
        }

        public int getRowCount() {
            return dataVector.size();
        }

        public Object getValueAt(int row, int col) {
            ArrayList rowVector = (ArrayList) dataVector.get(row);
            return rowVector.get(col);
        }

        public String getColumnName(int column) {
            return headingVector.get(column).toString();
        }

        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        public boolean isCellEditable(int row, int col) {
            if (col != 10 && col != 11) {
                return false;
            } else {
                selectedRow = row;
                return true;
            }
        }

        public void setValueAt(Object aValue, int row, int col) {
            ArrayList rowVector = (ArrayList) dataVector.get(row);
            rowVector.set(col, aValue);
        }

        public void deleteData() {
            if (dataVector != null && dataVector.size() > 0) {
                dataVector.clear();
                fireTableDataChanged();
            }
        }
    }

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        callView("Enquiry");
        lblStatus.setText("Enquiry");
        btnDelete.setEnabled(false);
        btnNew.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnSave.setEnabled(false);
        btnView.setEnabled(false);
    }            //    private void enableDisableAliasBranchTable(boolean flag) {//GEN-LAST:event_btnViewActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // Add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        updateAuthorizeStatus(CommonConstants.STATUS_REJECTED);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        btnReject.setEnabled(true);
        btnView.setEnabled(false);
        btnNew.setEnabled(false);
        btnEdit.setEnabled(false);
        btnSave.setEnabled(false);
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // Add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        updateAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(false);
        btnReject.setEnabled(false);
        btnView.setEnabled(false);
        btnNew.setEnabled(false);
        btnEdit.setEnabled(false);
        btnSave.setEnabled(false);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    private void updateAuthorizeStatus(String authorizeStatus) {
        if (viewType == AUTHORIZE && isFilled) {
            ArrayList arrList = new ArrayList();
            HashMap authorizeMap = new HashMap();
            HashMap singleAuthorizeMap = new HashMap();
            if (tblDailyLoanTransactionTable.getRowCount() > 0 && cboAgentType.getSelectedIndex() > 0) {
                singleAuthorizeMap.put("LOAN_COLLECTION_NO", observable.getLoanCollectionNo());
            } else if (tblLoanAdjustmentTable.getRowCount() > 0 && cboLoanAdjustmentAgentType.getSelectedIndex() > 0) {
                singleAuthorizeMap.put("LOAN_ADJUSTMENT_NO", observable.getLoanAdjustmentNo());
            }
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("AUTHORIZED_BY", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("AUTHORIZED_DT", ClientUtil.getCurrentDateWithTime());
            arrList.add(singleAuthorizeMap);
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            if (tblDailyLoanTransactionTable.getRowCount() > 0 && cboAgentType.getSelectedIndex() > 0) {
                authorize(authorizeMap, observable.getAgentID());
            } else if (tblLoanAdjustmentTable.getRowCount() > 0 && cboLoanAdjustmentAgentType.getSelectedIndex() > 0) {
                authorize(authorizeMap, observable.getAdjustmentAgentID());
            }
            viewType = "";
            singleAuthorizeMap = null;
            arrList = null;
            authorizeMap = null;
        } else {
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put("USER_ID", TrueTransactMain.USER_ID);
            whereMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
            whereMap.put("TRANS_DT",curDate.clone());
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            if (panDailyLoanTransaction.isShowing() == true) {
                mapParam.put(CommonConstants.MAP_NAME, "getDailyLoanTransAuthorize");
            } else if (panLoanAdjustment.isShowing() == true) {
                mapParam.put(CommonConstants.MAP_NAME, "getAdjustmentDailyLoanTransAuthorize");
            }
            isFilled = false;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
            observable.setStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }

    public void authorize(HashMap map, String id) {
        System.out.println("Authorize Map : " + map);

        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
            observable.set_authorizeMap(map);
            String tabName = "";
            if (tblDailyLoanTransactionTable.getRowCount() > 0 && cboAgentType.getSelectedIndex() > 0) {
                tabName = "DAILY_LOAN_COLLECTION";
            } else if (tblLoanAdjustmentTable.getRowCount() > 0 && cboLoanAdjustmentAgentType.getSelectedIndex() > 0) {
                tabName = "LOAN_ADJUSTMENT";
            }
            observable.doAction(id, tabName);
            if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                if (fromNewAuthorizeUI) {
                    newauthorizeListUI.removeSelectedRow();
                    this.dispose();
                    newauthorizeListUI.setFocusToTable();
                    if(type.equals("loanAdjustments"))
                        newauthorizeListUI.displayDetails("Daily Loan Collection - Loan Adjstment");
                    if(type.equals("suspenseCollection"))
                        newauthorizeListUI.displayDetails("Daily Loan Collection - Suspense collection");
                    
                }
                if (fromAuthorizeUI) {
                    authorizeListUI.removeSelectedRow();
                    this.dispose();
                    authorizeListUI.setFocusToTable();
                    if(type.equals("loanAdjustments"))
                        authorizeListUI.displayDetails("Daily Loan Collection - Loan Adjstment");
                    if(type.equals("suspenseCollection"))
                        authorizeListUI.displayDetails("Daily Loan Collection - Suspense collection");
                    
                }
            }
            btnCancelActionPerformed(null);
            observable.setStatus();
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }

    /** This method helps in filling the data frm the data base to respective txt fields
     * @param obj param The selected data from the viewAll() is passed as a param
     */
    public void fillData(Object obj) {
        try {
            HashMap hashMap = (HashMap) obj;
            System.out.println("### fillData Hash : " + hashMap);
            isFilled = true;
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                this.setButtonEnableDisable();
                hashMap.put("EDIT", "EDIT");
                if (panDailyLoanTransaction.isShowing() == true) {
                    hashMap.put("DAILY_LOAN_COLLECTION", "DAILY_LOAN_COLLECTION");
                    observable.getData(hashMap);
                    ClientUtil.enableDisable(panAgentDetails, false);
                    calc();
                } else {
                    hashMap.put("LOAN_ADJUSTMENT", "LOAN_ADJUSTMENT");
                    observable.getData(hashMap);
                    tblLoanAdjustmentTable.setModel(observable.getTblLoanAdjustmentTabls());
                    ClientUtil.enableDisable(panLoanAdjustmentTable, false);
                    setEditTotalAmount();
                    tblLoanAdjustmentTable.getColumnModel().getColumn(0).setPreferredWidth(105);
                    ClientUtil.enableDisable(panLoanAdjustmentAgentDetails, false);
                }
            } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
                this.setButtonEnableDisable();
                if (panDailyLoanTransaction.isShowing() == true) {
                    hashMap.put("DAILY_LOAN_COLLECTION", "DAILY_LOAN_COLLECTION");
                    observable.getData(hashMap);
                    ClientUtil.enableDisable(panAgentDetails, false);
                    calc();
                } else {
                    hashMap.put("LOAN_ADJUSTMENT", "LOAN_ADJUSTMENT");
                    observable.getData(hashMap);
                    tblLoanAdjustmentTable.setModel(observable.getTblLoanAdjustmentTabls());
                    panLoanAdjustmentTable.setEnabled(false);
                    ClientUtil.enableDisable(panLoanAdjustmentTable, false);
                    setEditTotalAmount();
                    tblLoanAdjustmentTable.getColumnModel().getColumn(0).setPreferredWidth(105);
                    ClientUtil.enableDisable(panLoanAdjustmentAgentDetails, false);
                }
            }
            if (panDailyLoanTransaction.isShowing() == true) {
                btnDisplay.setEnabled(false);
                tdtCollectionDt.setDateValue(DateUtil.getStringDate(curDate));
                cboAgentType.setSelectedItem(CommonUtil.convertObjToStr(hashMap.get("AGENT_ID")));
                observable.setLoanCollectionNo(CommonUtil.convertObjToStr(hashMap.get("LOAN_COLLECTION_NO")));
                observable.setAgentID(CommonUtil.convertObjToStr(hashMap.get("AGENT_ID")));
            } else {
                btnLoanAdjustmentDisplay.setEnabled(false);
                tdtLoanAdjustmentCollectionDt.setDateValue(DateUtil.getStringDate(curDate));
                cboLoanAdjustmentAgentType.setSelectedItem(CommonUtil.convertObjToStr(hashMap.get("AGENT_ID")));
                observable.setLoanAdjustmentNo(CommonUtil.convertObjToStr(hashMap.get("LOAN_ADJUSTMENT_NO")));
                observable.setAdjustmentAgentID(CommonUtil.convertObjToStr(hashMap.get("AGENT_ID")));
            }
            if (viewType == AUTHORIZE) {
                btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                ClientUtil.enableDisable(this, false);
                btnAuthorize.setEnabled(true);
                btnAuthorize.requestFocusInWindow();
                btnAuthorize.setFocusable(true);
            } else {
                btnSave.setEnabled(false);
            }
            if (panDailyLoanTransaction.isShowing() == true) {
                ((SimpleTableModel) tblDailyLoanTransactionTable.getModel()).fireTableDataChanged();
                setSizeTableData();
            }
            if (hashMap.containsKey("NEW_FROM_AUTHORIZE_LIST_UI_LOAN_ADJUSTMENT")) {
                type = "loanAdjustments";
                fromNewAuthorizeUI = true;
               newauthorizeListUI = (NewAuthorizeListUI) hashMap.get("PARENT");
                hashMap.remove("PARENT");
                viewType = AUTHORIZE;
                observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
                observable.setStatus();
                btnReject.setEnabled(false);
                rejectFlag = 1;
                btnLoanAdjustmentDisplay.setEnabled(false);
                tdtLoanAdjustmentCollectionDt.setDateValue(DateUtil.getStringDate(curDate));
                cboLoanAdjustmentAgentType.setSelectedItem(CommonUtil.convertObjToStr(hashMap.get("AGENT_ID")));
                observable.setLoanAdjustmentNo(CommonUtil.convertObjToStr(hashMap.get("LOAN_ADJUSTMENT_NO")));
                observable.setAdjustmentAgentID(CommonUtil.convertObjToStr(hashMap.get("AGENT_ID")));
                hashMap.put("LOAN_ADJUSTMENT", "LOAN_ADJUSTMENT");
                observable.getData(hashMap);
                tblLoanAdjustmentTable.setModel(observable.getTblLoanAdjustmentTabls());
                ClientUtil.enableDisable(panDailyLoanTransaction, false);
                setEditTotalAmount();
                tblLoanAdjustmentTable.getColumnModel().getColumn(0).setPreferredWidth(105);
                ClientUtil.enableDisable(panAgentDetails, false);
                tabDailyLoanTransaction.remove(panDailyLoanTransaction);
                btnAuthorize.setEnabled(true);
                btnAuthorize.requestFocusInWindow();
                btnAuthorize.setFocusable(true);
            }
            if (hashMap.containsKey("FROM_AUTHORIZE_LIST_UI_LOAN_ADJUSTMENT")) {
                type = "loanAdjustments";
                fromAuthorizeUI = true;
                authorizeListUI = (AuthorizeListUI) hashMap.get("PARENT");
                hashMap.remove("PARENT");
                viewType = AUTHORIZE;
                observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
                observable.setStatus();
                btnReject.setEnabled(false);
                rejectFlag = 1;
                btnLoanAdjustmentDisplay.setEnabled(false);
                tdtLoanAdjustmentCollectionDt.setDateValue(DateUtil.getStringDate(curDate));
                cboLoanAdjustmentAgentType.setSelectedItem(CommonUtil.convertObjToStr(hashMap.get("AGENT_ID")));
                observable.setLoanAdjustmentNo(CommonUtil.convertObjToStr(hashMap.get("LOAN_ADJUSTMENT_NO")));
                observable.setAdjustmentAgentID(CommonUtil.convertObjToStr(hashMap.get("AGENT_ID")));
                hashMap.put("LOAN_ADJUSTMENT", "LOAN_ADJUSTMENT");
                observable.getData(hashMap);
                tblLoanAdjustmentTable.setModel(observable.getTblLoanAdjustmentTabls());
                ClientUtil.enableDisable(panDailyLoanTransaction, false);
                setEditTotalAmount();
                tblLoanAdjustmentTable.getColumnModel().getColumn(0).setPreferredWidth(105);
                ClientUtil.enableDisable(panAgentDetails, false);
                tabDailyLoanTransaction.remove(panDailyLoanTransaction);
                btnAuthorize.setEnabled(true);
                btnAuthorize.requestFocusInWindow();
                btnAuthorize.setFocusable(true);
            }
             if (hashMap.containsKey("NEW_FROM_AUTHORIZE_LIST_UI_SUSPENSE_COLLECTION")) {
                type = "suspenseCollection";
                fromNewAuthorizeUI = true;
                newauthorizeListUI = (NewAuthorizeListUI) hashMap.get("PARENT");
                hashMap.remove("PARENT");
                viewType = AUTHORIZE;
                observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
                observable.setStatus();
                btnReject.setEnabled(false);
                rejectFlag = 1;
                btnDisplay.setEnabled(false);
                tdtCollectionDt.setDateValue(DateUtil.getStringDate(curDate));
                cboAgentType.setSelectedItem(CommonUtil.convertObjToStr(hashMap.get("AGENT_ID")));
                observable.setLoanCollectionNo(CommonUtil.convertObjToStr(hashMap.get("LOAN_COLLECTION_NO")));
                observable.setAgentID(CommonUtil.convertObjToStr(hashMap.get("AGENT_ID")));
                hashMap.put("DAILY_LOAN_COLLECTION", "DAILY_LOAN_COLLECTION");
                observable.getData(hashMap);
                tblLoanAdjustmentTable.setModel(observable.getTblLoanAdjustmentTabls());
                ClientUtil.enableDisable(panLoanAdjustment, false);
                calc();
                tblLoanAdjustmentTable.getColumnModel().getColumn(0).setPreferredWidth(105);
                ClientUtil.enableDisable(panLoanAdjustmentAgentDetails, false);
                tabDailyLoanTransaction.remove(panLoanAdjustment);
                btnAuthorize.setEnabled(true);
                btnAuthorize.requestFocusInWindow();
                btnAuthorize.setFocusable(true);
            }
            if (hashMap.containsKey("FROM_AUTHORIZE_LIST_UI_SUSPENSE_COLLECTION")) {
                type = "suspenseCollection";
                fromAuthorizeUI = true;
                authorizeListUI = (AuthorizeListUI) hashMap.get("PARENT");
                hashMap.remove("PARENT");
                viewType = AUTHORIZE;
                observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
                observable.setStatus();
                btnReject.setEnabled(false);
                rejectFlag = 1;
                btnDisplay.setEnabled(false);
                tdtCollectionDt.setDateValue(DateUtil.getStringDate(curDate));
                cboAgentType.setSelectedItem(CommonUtil.convertObjToStr(hashMap.get("AGENT_ID")));
                observable.setLoanCollectionNo(CommonUtil.convertObjToStr(hashMap.get("LOAN_COLLECTION_NO")));
                observable.setAgentID(CommonUtil.convertObjToStr(hashMap.get("AGENT_ID")));
                hashMap.put("DAILY_LOAN_COLLECTION", "DAILY_LOAN_COLLECTION");
                observable.getData(hashMap);
                tblLoanAdjustmentTable.setModel(observable.getTblLoanAdjustmentTabls());
                ClientUtil.enableDisable(panLoanAdjustment, false);
                calc();
                tblLoanAdjustmentTable.getColumnModel().getColumn(0).setPreferredWidth(105);
                ClientUtil.enableDisable(panLoanAdjustmentAgentDetails, false);
                tabDailyLoanTransaction.remove(panLoanAdjustment);
                btnAuthorize.setEnabled(true);
                btnAuthorize.requestFocusInWindow();
                btnAuthorize.setFocusable(true);
            }
            hashMap = null;
            btnCancel.setEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
        ClientUtil.clearAll(this);
        if (panDailyLoanTransaction.isShowing() == true) {
            ClientUtil.enableDisable(panAgentDetails, true);
            tdtCollectionDt.setDateValue(DateUtil.getStringDate(curDate));
        } else if (panLoanAdjustment.isShowing() == true) {
            ClientUtil.enableDisable(panLoanAdjustmentAgentDetails, true);
            tdtLoanAdjustmentCollectionDt.setDateValue(DateUtil.getStringDate(curDate));
        }
        lblAgentNameVal.setText("                                ");
        lblLoanAdjustmentAgentNameVal.setText("                                ");
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        callView("Enquiry");
        lblStatus.setText("Enquiry");
    }//GEN-LAST:event_btnEditActionPerformed
    /** To display a popUp window for viewing existing data */
    private void callView(String currAction) {
        viewType = currAction;
        HashMap viewMap = new HashMap();
        if (currAction.equalsIgnoreCase("Enquiry")) {
            if (panDailyLoanTransaction.isShowing() == true) {
                viewMap.put(CommonConstants.MAP_NAME, "getDailyLoanTransEnquiry");
            } else if (panLoanAdjustment.isShowing() == true) {
                viewMap.put(CommonConstants.MAP_NAME, "getAdjustmentDailyLoanTransEnquiry");
            }
        }
        new ViewAll(this, viewMap).show();
    }
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        callView("Enquiry");
        lblStatus.setText("Enquiry");
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        btnSave.setEnabled(false);
        ClientUtil.enableDisable(panAgentDetails, false);
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        String tabName = "";
        if (tblDailyLoanTransactionTable.getRowCount() > 0 && (cboAgentType.getSelectedIndex() > 0 || txtFileName.getText() != null)) {
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                setTableFinalList();
            }
            //for setting total loan amount and total commision amount
            if(txtTotalPayment.getText()!=null && txtTotalPayment.getText().length()>0){
                observable.setTotalPaymentAmount(CommonUtil.convertObjToDouble(txtTotalPayment.getText()));    
            }
            if(txtTotalCommission.getText()!=null && txtTotalCommission.getText().length()>0){
                observable.setCommAmount(CommonUtil.convertObjToDouble(txtTotalCommission.getText()));
            }            
            tabName = "DAILY_LOAN_COLLECTION";
            String agentID = CommonUtil.convertObjToStr(cboAgentType.getSelectedItem());
            observable.doAction(agentID, tabName);
            if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                btnCancelActionPerformed(null);
                btnCancel.setEnabled(true);
                lblStatus.setText(ClientConstants.RESULT_STATUS[observable.getResult()]);
            }
        } else if (tblLoanAdjustmentTable.getRowCount() > 0 && cboLoanAdjustmentAgentType.getSelectedIndex() > 0) {
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                adjustmentList = observable.getAdjustmentList();
                HashMap loanMap = new HashMap();
                if (adjustmentList != null && adjustmentList.size() > 0) {
                    System.out.println("#$@$#@$@$@ FinalList : " + adjustmentList);
                    for (int i = 0; i < adjustmentList.size(); i++) {
                        String actNum = "";
                        loanMap = (HashMap) adjustmentList.get(i);
                        actNum = CommonUtil.convertObjToStr(loanMap.get("ACT_NUM"));
                        for (int j = 0; j < tblLoanAdjustmentTable.getRowCount(); j++) {
                            if (CommonUtil.convertObjToStr(tblLoanAdjustmentTable.getValueAt(j, 1)).equals(actNum)
                                    && !((Boolean) tblLoanAdjustmentTable.getValueAt(j, 0)).booleanValue()) {
                                adjustmentList.remove(i--);
                            }
                        }
                    }
                    System.out.println("#$#$$# final List:" + adjustmentList);
                }
                if (adjustmentList != null && adjustmentList.size() > 0) {
                    observable.setAdjustmentList(adjustmentList);
                    tabName = "LOAN_ADJUSTMENT";
                    String agentID = CommonUtil.convertObjToStr(cboLoanAdjustmentAgentType.getSelectedItem());
                    int result = ClientUtil.confirmationAlert("Do you Want to Waive Penal Interest");
                    if (result == 0) {
                        observable.setPenalwaiveoff("Y");
                    } else {
                        observable.setPenalwaiveoff("N");
                    }
                    observable.doAction(agentID, tabName);
                    if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                        btnCancelActionPerformed(null);
                        btnCancel.setEnabled(true);
                        lblStatus.setText(ClientConstants.RESULT_STATUS[observable.getResult()]);
                    }
                }
            }
        }
        if (observable.getProxyReturnMap()!=null) {
                        displayTransDetail(observable.getProxyReturnMap());
        }
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
    }//GEN-LAST:event_btnSaveActionPerformed
  public void  displayTransDetail(HashMap returnMap){
        System.out.println("returnMap##########"+returnMap);
        String transId = CommonUtil.convertObjToStr(returnMap.get("SINGLE_TRANS_ID"));
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
        int yesNo = 0;
        String[] options = {"Yes", "No"};
        yesNo = COptionPane.showOptionDialog(null,"Transaction completed!! Do you want to print?", CommonConstants.WARNINGTITLE,
        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
        null, options, options[0]);
        System.out.println("#$#$$ yesNo : "+yesNo);
        if (yesNo==0) {
            TTIntegration ttIntgration = null;
            HashMap paramMap = new HashMap();
            paramMap.put("TransId", transId);
            paramMap.put("TransDt", curDate);
            paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
            ttIntgration.setParam(paramMap);
            if(CommonUtil.convertObjToStr(returnMap.get("SINGLE_TRANS_ID"))!= null) {
                    ttIntgration.integrationForPrint("MDSReceiptsTransfer");
           }
       }
 }

private void setTableFinalList() {
        finalList = observable.getFinalList();
        HashMap memMap = new HashMap();
        ArrayList finalDailyList = new ArrayList();
        if (finalList != null && finalList.size() > 0) {
            System.out.println("#$@$#@$@$@ FinalList : " + finalList);
            for (int i = 0; i < finalList.size(); i++) {
                String actNo = "";
                memMap = (HashMap) finalList.get(i);
                actNo = CommonUtil.convertObjToStr(memMap.get("ACT_NUM"));
                System.out.println("$#@@$@$#$@$ ActNo : " + memMap);
                for (int j = 0; j < tblDailyLoanTransactionTable.getRowCount(); j++) {
                    if (CommonUtil.convertObjToDouble(tblDailyLoanTransactionTable.getValueAt(j, 10))>0) {
                        if (CommonUtil.convertObjToStr(tblDailyLoanTransactionTable.getValueAt(j, 0)).equals(actNo)) {
                            memMap.put("COMMISSION", (tblDailyLoanTransactionTable.getValueAt(j, 11)));
                            memMap.put("PAYMENT", (tblDailyLoanTransactionTable.getValueAt(j, 10)));
                            memMap.put("TOTAL_DUE", CommonUtil.convertObjToDouble(tblDailyLoanTransactionTable.getValueAt(j, 9)).doubleValue());
                            memMap.put("CHARGES", CommonUtil.convertObjToDouble(tblDailyLoanTransactionTable.getValueAt(j, 8)).doubleValue());
                            memMap.put("PENAL", CommonUtil.convertObjToDouble(tblDailyLoanTransactionTable.getValueAt(j, 7)).doubleValue());
                            memMap.put("INT_DUE", CommonUtil.convertObjToDouble(tblDailyLoanTransactionTable.getValueAt(j, 6)).doubleValue());
                            memMap.put("PRINC_DUE", CommonUtil.convertObjToDouble(tblDailyLoanTransactionTable.getValueAt(j, 5)).doubleValue());
                            memMap.put("BALANCE", CommonUtil.convertObjToDouble(tblDailyLoanTransactionTable.getValueAt(j, 4)).doubleValue());
                            finalDailyList.add(memMap);
                        }
                    }
                }
            }
            System.out.println("############# finalList11 : " + finalDailyList);     
            System.out.println("############# finalList : " + finalList);            
            observable.setFinalList(finalDailyList);
        }
    }
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        viewType = "CANCEL";
        isFilled = false;
        lblStatus.setText("               ");
        ClientUtil.clearAll(this);
        btnNew.setEnabled(true);
        btnSave.setEnabled(false);
        btnEdit.setEnabled(true);
        btnView.setEnabled(true);
        btnDelete.setEnabled(true);
        btnCancel.setEnabled(false);
        btnReject.setEnabled(true);
        btnDisplay.setEnabled(false);
        btnLoanAdjustmentDisplay.setEnabled(false);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        ClientUtil.enableDisable(this, false);
        lblAgentNameVal.setText("                                ");
        lblLoanAdjustmentAgentNameVal.setText("                                ");
        clearTable();
        chkSelectAll.setEnabled(false);
        observable.resetAdjustmentTableValues();
        observable.resetForm();
        if (fromNewAuthorizeUI) {
            this.dispose();
            newauthorizeListUI.setFocusToTable();
        }
        if (fromAuthorizeUI) {
            this.dispose();
            authorizeListUI.setFocusToTable();
        }
        //__ Make the Screen Closable..
        setModified(false);
    }//GEN-LAST:event_btnCancelActionPerformed
    private void clearTable() {
        observable.resetForm();
        if (tblDailyLoanTransactionTable.getRowCount() > 0) {
            ((SimpleTableModel) tblDailyLoanTransactionTable.getModel()).deleteData();
        }
    }
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
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

    private void tblLoanAdjustmentTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblLoanAdjustmentTableKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tblLoanAdjustmentTableKeyReleased

    private void cboLoanAdjustmentAgentTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboLoanAdjustmentAgentTypeActionPerformed
        // TODO add your handling code here:
        if (cboLoanAdjustmentAgentType.getSelectedIndex() > 0) {
            HashMap agentMap = new HashMap();
            agentMap.put("AGENT_ID", cboLoanAdjustmentAgentType.getSelectedItem());
            List lst = ClientUtil.executeQuery("getAgentDetailsName", agentMap);
            agentMap = null;
            if (lst != null && lst.size() > 0) {
                agentMap = (HashMap) lst.get(0);
                lblLoanAdjustmentAgentNameVal.setText(CommonUtil.convertObjToStr(agentMap.get("AGENT_NAME")));
                btnLoanAdjustmentDisplay.setEnabled(true);
            }
        } else {
            lblLoanAdjustmentAgentNameVal.setText("                                ");
            btnLoanAdjustmentDisplay.setEnabled(false);
        }
        if (observable.getActionType() != ClientConstants.ACTIONTYPE_NEW) {
            btnLoanAdjustmentDisplay.setEnabled(false);
        }
    }//GEN-LAST:event_cboLoanAdjustmentAgentTypeActionPerformed

    private void btnLoanAdjustmentDisplayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoanAdjustmentDisplayActionPerformed
        // TODO add your handling code here:
        if (cboLoanAdjustmentAgentType.getSelectedIndex() > 0) {
            observable.resetAdjustmentTableValues();
            HashMap loanMap = new HashMap();
            loanMap.put("AGENT_ID", cboLoanAdjustmentAgentType.getSelectedItem());
            observable.insertAdjustmentTableData(loanMap);
            tblLoanAdjustmentTable.setModel(observable.getTblLoanAdjustmentTabls());
            if (tblLoanAdjustmentTable.getRowCount() == 0) {
                chkSelectAll.setEnabled(false);
                ClientUtil.showMessageWindow(" No Data !!! ");
                btnLoanAdjustmentDisplay.setEnabled(true);
            } else {
                ClientUtil.enableDisable(panLoanAdjustmentAgentDetails, false);
                btnLoanAdjustmentDisplay.setEnabled(false);
                chkSelectAll.setEnabled(true);
            }
            setSizeAdjustmentTableData();
        } else {
            ClientUtil.showMessageWindow("Please Select Agent ID ...!!! ");
            return;
        }
    }//GEN-LAST:event_btnLoanAdjustmentDisplayActionPerformed

    private void btnLoanAdjustmentClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoanAdjustmentClearActionPerformed
        // TODO add your handling code here:
        observable.resetAdjustmentTableValues();
        observable.resetForm();
        setSizeAdjustmentTableData();
        ClientUtil.clearAll(this);
        btnNewActionPerformed(null);
        setButtonEnableDisable();
        chkSelectAll.setEnabled(false);
        setModified(false);
    }//GEN-LAST:event_btnLoanAdjustmentClearActionPerformed

    private void txtLoanAdjustmentPaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLoanAdjustmentPaymentActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLoanAdjustmentPaymentActionPerformed

    private void txtLoanAdjustmentPaymentFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtLoanAdjustmentPaymentFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLoanAdjustmentPaymentFocusLost

    private void chkSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSelectAllActionPerformed
        // TODO add your handling code here:
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            boolean flag;
            if (chkSelectAll.isSelected() == true) {
                flag = true;
            } else {
                flag = false;
            }
            for (int i = 0; i < tblLoanAdjustmentTable.getRowCount(); i++) {
                tblLoanAdjustmentTable.setValueAt(new Boolean(flag), i, 0);
            }
            setTotalAmount();
        }
    }//GEN-LAST:event_chkSelectAllActionPerformed
    private void setTotalAmount() {
        double totAmount = 0;
        String st = "";
        for (int i = 0; i < tblLoanAdjustmentTable.getRowCount(); i++) {
            st = CommonUtil.convertObjToStr(tblLoanAdjustmentTable.getValueAt(i, 0));
            if (st.equals("true")) {
                totAmount = totAmount + CommonUtil.convertObjToDouble((CommonUtil.convertObjToStr(tblLoanAdjustmentTable.getValueAt(i, 11))).replaceAll(",", "")).doubleValue();
            }
        }
        txtLoanAdjustmentTotalPayment.setText(String.valueOf(totAmount));
    }

    private void setEditTotalAmount() {
        double totAmount = 0;
        String st = "";
        for (int i = 0; i < tblLoanAdjustmentTable.getRowCount(); i++) {
            totAmount = totAmount + CommonUtil.convertObjToDouble((CommonUtil.convertObjToStr(tblLoanAdjustmentTable.getValueAt(i, 10))).replaceAll(",", "")).doubleValue();
        }
        txtLoanAdjustmentTotalPayment.setText(String.valueOf(totAmount));
    }
    private void tblLoanAdjustmentTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblLoanAdjustmentTableMouseClicked
        // TODO add your handling code here:
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            String st = CommonUtil.convertObjToStr(tblLoanAdjustmentTable.getValueAt(tblLoanAdjustmentTable.getSelectedRow(), 0));
            if (st.equals("true")) {
                tblLoanAdjustmentTable.setValueAt(new Boolean(false), tblLoanAdjustmentTable.getSelectedRow(), 0);
            } else {
                tblLoanAdjustmentTable.setValueAt(new Boolean(true), tblLoanAdjustmentTable.getSelectedRow(), 0);
            }
            setTotalAmount();
        }
    }//GEN-LAST:event_tblLoanAdjustmentTableMouseClicked

    private void tblDailyLoanTransactionTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDailyLoanTransactionTableMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() >= 2) {
            if (tblDailyLoanTransactionTable.getSelectedColumn() == 4 || tblDailyLoanTransactionTable.getSelectedColumn() == 5 || tblDailyLoanTransactionTable.getSelectedColumn() == 6 || tblDailyLoanTransactionTable.getSelectedColumn() == 7
                    || tblDailyLoanTransactionTable.getSelectedColumn() == 8 || tblDailyLoanTransactionTable.getSelectedColumn() == 9) {
                changeAmt();
            }
        }
    }//GEN-LAST:event_tblDailyLoanTransactionTableMouseClicked

private void btnBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowseActionPerformed
    // TODO add your handling code here:
    browseActionPerformed();
}//GEN-LAST:event_btnBrowseActionPerformed
    private void browseActionPerformed() {
        // TODO add your handling code here:

        try {
            cboAgentType.removeAllItems();
            //observable.getTbmTransfer().setRowCount(0);
            javax.swing.JFileChooser fc = new javax.swing.JFileChooser();;
            int returnVal = fc.showOpenDialog(null);
            if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
                java.io.File file = fc.getSelectedFile();
                String fname = file.getName();
                fname = fname.toUpperCase();
                if (fname != null || !fname.equalsIgnoreCase("")) {
                    if (fname.substring(fname.indexOf('.') + 1, fname.length()).equals("TXT") ||
                            fname.substring(fname.indexOf('.') + 1, fname.length()).equals("MST") ) {
                        txtFileName.setText(fc.getSelectedFile().toString());
                    } else {
                        displayAlert("File should be .txt OR .MST format");
                        return;
                    }
                }
                fillAgentName(file);
            }
        } catch (Exception e) {
            displayAlert("File should be .txt OR .MST format");
        }
    }
    private HashMap agentMap = null;
    private ArrayList totCustomerList = null;

    public void fillAgentName(java.io.File file) {
        try {

            agentMap = new HashMap();
            ArrayList customerList = new ArrayList();
            totCustomerList = new ArrayList();
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            DataInputStream dis = new DataInputStream(bis);
            String line = "";
            int x = 0;
            while (dis.available() > 0 && (line = dis.readLine()).length() > 0) {
                //            line =dis.readLine();
                System.out.println("line####" + line);
                String numberParameter[] = (String[]) line.split(",");
                System.out.println("numberParameter####" + numberParameter);
                customerList = new ArrayList();
                System.out.println("line####" + line);
                HashMap dataMap = new HashMap();
                for (int i = 0; i < numberParameter.length; i++) {
                    if (x == 0) {
                        agentMap.put("AGENT_ID", numberParameter[i]);
                        agentMap.put("NO_TRANS", numberParameter[i + 1]);
                        agentMap.put("TOT_AMT", numberParameter[i + 2]);
                        x++;
                        break;

                    } else {
                        if(CommonUtil.convertObjToStr(numberParameter[0]).equalsIgnoreCase("L")){
                            if (numberParameter[i].contains("/")) {
                                numberParameter[i] = numberParameter[i].replaceAll("/", "-");
                            }
                            customerList.add(numberParameter[i]);
//                          customerList.add(numberParameter[3]);
//                          customerList.add(numberParameter[1]);
//                          break;
                            }
                    }
//                    System.out.println("dataMap####"+dataMap);
                }
                if (!customerList.isEmpty()) {
                    totCustomerList.add(customerList);
                }

            }
            if (dis != null) {
                dis.close();
            }
            if (bis != null) {
                bis.close();
            }
            if (fis != null) {
                fis.close();
            }
            System.out.println("totCustomerList####" + totCustomerList);
//            observable.insertTableDataFromFile(totCustomerList);
            cboAgentType.removeAllItems();
            //            String filepath= txtFileName.getText();
            //            String dbUserName="Admin";
            //            String dbPassword="sil123";
            //            getConnection(filepath,dbUserName,dbPassword);
            //            java.sql.Statement s = con.createStatement();
            //            java.sql.ResultSet rs = s.executeQuery("SELECT DISTINCT Ag_Code FROM TRANSACT ");
//            List lst= ClientUtil.executeQuery(""
            ArrayList key = new ArrayList();
            ArrayList value = new ArrayList();
            key.add("");
            value.add("");
            HashMap map = new HashMap();
            agentMap.put("AGENTID", agentMap.get("AGENT_ID"));
            String agentCuId = "";
            List lst = ClientUtil.executeQuery("getSelectAgentTO1", agentMap);
            if (lst != null && lst.size() > 0) {
                AgentTO agTo = new AgentTO();
                agTo = (AgentTO) lst.get(0);
                String agCode = CommonUtil.convertObjToStr(agentMap.get("AGENT_ID"));
                agentCuId = agTo.getAgentId();
                key.add(agTo.getAgentId());//agCode);//map.get("AGENT_ID").toString());//babu 1
                value.add(agTo.getAgentId());
                cboAgentType.addItem("");
                cboAgentType.addItem(agTo.getAgentId());
                observable.setCbmLoanAdjustmentAgentType(new com.see.truetransact.clientutil.ComboBoxModel(key, value));
                //  observable.setCbmAgentType(new ComboBoxModel(key,value));
                cboAgentType.setModel(observable.getCbmAgentType());
                // cboAgentType.setSelectedItem(agTo.getAgentId());

            } else {
                ClientUtil.displayAlert("Invlid Agent Id Please check input file");
                return;
            }
            //fill Agent Name
            // String agentType= ((com.see.truetransact.clientutil.ComboBoxModel) cboAgentType.getModel()).getKeyForSelected().toString();
            agentMap.put("AGENT_ID", agentCuId);// cboAgentType.getSelectedItem());
            List lst1 = ClientUtil.executeQuery("getAgentDetailsName", agentMap);
            agentMap = null;
            cboAgentType.setSelectedItem(agentCuId);
            if (lst1 != null && lst1.size() > 0) {
                agentMap = (HashMap) lst1.get(0);
                lblAgentNameVal.setText(CommonUtil.convertObjToStr(agentMap.get("AGENT_NAME")));

                ArrayList singleList = (ArrayList) totCustomerList.get(0);
                String stringDate = CommonUtil.convertObjToStr(singleList.get(5));
                tdtCollectionDt.setDateValue(stringDate);
                // tdtCollectionDt.setEnabled(false);
                // cboAgentType.setEnabled(false);
                if (txtFileName.getText() != null) {
                    System.out.println("chkLoadFromFile.isSelected() ===" + txtFileName.getText());
                    //setDataToTable();
                }
            }
            System.out.println("btnDisplayActionPerformed===");
            btnDisplaydetails();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("getDataFromAccessDB error : " + e);
        }
    }

    public void changeAmt() {
        HashMap amountMap = new HashMap();
        if (CommonUtil.convertObjToStr(CommonConstants.OPERATE_MODE).equals(CommonConstants.IMPLEMENTATION)) {
            String tolerance_amt = CommonUtil.convertObjToStr(CommonConstants.TOLERANCE_AMT);
            if (tolerance_amt.length() == 0) {
                ClientUtil.displayAlert("Please Add Tolerance Property in  TT property");
                return;
            }
            int c = tblDailyLoanTransactionTable.getSelectedColumn();
            int r = tblDailyLoanTransactionTable.getSelectedRow();
            String selectedAmt = CommonUtil.convertObjToStr(tblDailyLoanTransactionTable.getValueAt(r, c));

            selectedAmt = selectedAmt.replaceAll(",", "");
            amountMap.put("TOLERANCE_AMT", CommonConstants.TOLERANCE_AMT);
            amountMap.put("SELECTED_AMT", selectedAmt);
            amountMap.put("TITLE", "TransExcemption");
            amountMap.put("CALCULATED_AMT", selectedAmt);

            System.out.println("amountMap####" + amountMap);
            TextUI textUI = new TextUI(this, this, amountMap);

        }

    }

    @Override
    public void modifyTransData(Object objData) {
        TextUI obj = (TextUI) objData;
        //System.out.println("obj.getTxtData()"+obj.getTxtData());
        String enteredData = obj.getTxtData();
        int c = tblDailyLoanTransactionTable.getSelectedColumn();
        int r = tblDailyLoanTransactionTable.getSelectedRow();
        tblDailyLoanTransactionTable.setValueAt(Double.parseDouble(enteredData), r, c);
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
    private com.see.truetransact.uicomponent.CButton btnBrowse;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClear;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDisplay;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnLoanAdjustmentClear;
    private com.see.truetransact.uicomponent.CButton btnLoanAdjustmentDisplay;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboAgentType;
    private com.see.truetransact.uicomponent.CComboBox cboLoanAdjustmentAgentType;
    private com.see.truetransact.uicomponent.CCheckBox chkSelectAll;
    private com.see.truetransact.uicomponent.CLabel lblAgentName;
    private com.see.truetransact.uicomponent.CLabel lblAgentNameVal;
    private com.see.truetransact.uicomponent.CLabel lblAgentType;
    private com.see.truetransact.uicomponent.CLabel lblCollectionDt;
    private com.see.truetransact.uicomponent.CLabel lblFileName;
    private com.see.truetransact.uicomponent.CLabel lblLoanAdjustmentAgentName;
    private com.see.truetransact.uicomponent.CLabel lblLoanAdjustmentAgentNameVal;
    private com.see.truetransact.uicomponent.CLabel lblLoanAdjustmentAgentType;
    private com.see.truetransact.uicomponent.CLabel lblLoanAdjustmentCollectionDt;
    private com.see.truetransact.uicomponent.CLabel lblLoanAdjustmentTotalPayment;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblSelectAll;
    private com.see.truetransact.uicomponent.CLabel lblSpace;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace6;
    private com.see.truetransact.uicomponent.CLabel lblSpace62;
    private com.see.truetransact.uicomponent.CLabel lblSpace63;
    private com.see.truetransact.uicomponent.CLabel lblSpace64;
    private com.see.truetransact.uicomponent.CLabel lblSpace65;
    private com.see.truetransact.uicomponent.CLabel lblSpace66;
    private com.see.truetransact.uicomponent.CLabel lblSpace67;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTotalCommission;
    private com.see.truetransact.uicomponent.CLabel lblTotalPayment;
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
    private com.see.truetransact.uicomponent.CPanel panAgentDetails;
    private com.see.truetransact.uicomponent.CPanel panDailyLoanTransaction;
    private com.see.truetransact.uicomponent.CPanel panDailyLoanTransactionTable;
    private com.see.truetransact.uicomponent.CPanel panLoadFromFile;
    private com.see.truetransact.uicomponent.CPanel panLoanAdjustment;
    private com.see.truetransact.uicomponent.CPanel panLoanAdjustmentAgentDetails;
    private com.see.truetransact.uicomponent.CPanel panLoanAdjustmentTable;
    private com.see.truetransact.uicomponent.CPanel panLoanAdjustmentTotalPayment;
    private com.see.truetransact.uicomponent.CPanel panSelectAll;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTotalPayment;
    private com.see.truetransact.uicomponent.CButtonGroup rdgEFTProductGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgIsLapsedGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgPayableBranchGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgPrintServicesGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgSeriesGR;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private javax.swing.JSeparator sptException;
    private com.see.truetransact.uicomponent.CScrollPane srpDailyLoanTransactionTable;
    private com.see.truetransact.uicomponent.CScrollPane srpLoanAdjustmentTable;
    private com.see.truetransact.uicomponent.CTabbedPane tabDailyLoanTransaction;
    private com.see.truetransact.uicomponent.CTable tblDailyLoanTransactionTable;
    private com.see.truetransact.uicomponent.CTable tblLoanAdjustmentTable;
    private javax.swing.JToolBar tbrAdvances;
    private com.see.truetransact.uicomponent.CDateField tdtCollectionDt;
    private com.see.truetransact.uicomponent.CDateField tdtLoanAdjustmentCollectionDt;
    private com.see.truetransact.uicomponent.CTextField txtFileName;
    private com.see.truetransact.uicomponent.CTextField txtLoanAdjustmentPayment;
    private com.see.truetransact.uicomponent.CTextField txtLoanAdjustmentTotalPayment;
    private com.see.truetransact.uicomponent.CTextField txtPayment;
    private com.see.truetransact.uicomponent.CTextField txtTotalCommission;
    private com.see.truetransact.uicomponent.CTextField txtTotalPayment;
    // End of variables declaration//GEN-END:variables

    public static void main(String[] arg) {
        try {
            javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Throwable th) {
            th.printStackTrace();
        }
        javax.swing.JFrame jf = new javax.swing.JFrame();
        DailyLoanTransUI gui = new DailyLoanTransUI();
        jf.getContentPane().add(gui);
        jf.setSize(536, 566);
        jf.show();
        gui.show();
    }
}