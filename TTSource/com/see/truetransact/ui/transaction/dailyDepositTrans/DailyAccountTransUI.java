/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * DailyAccountTransUI.java
 * Created on December 24, 2012, 11:27 AM
 *
 */
package com.see.truetransact.ui.transaction.dailyDepositTrans;

/**
 *
 * @author Suresh
 *
 **/
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.ui.termloan.dailyLoanTrans.*;
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
import com.see.truetransact.ui.common.viewall.TextUI;
import com.see.truetransact.ui.deposit.CommonMethods;
import com.see.truetransact.uicomponent.CButtonGroup;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import javax.swing.text.StyledEditorKit.BoldAction;

public class DailyAccountTransUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer, UIMandatoryField {

    private final static ClientParseException parseException = ClientParseException.getInstance();
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.mdsapplication.MDSApplicationRB", ProxyParameters.LANGUAGE);
    private HashMap mandatoryMap;
    final String AUTHORIZE = "Authorize";
    private String viewType = new String();
    private List finalList = null;
    private List adjustmentList = null;
    public int selectedRow = -1;
    private boolean isFilled = false;
    DailyAccountTransOB observable = null;
    private Date curDate = null;
    AuthorizeListUI authorizeListUI = null;
    boolean fromAuthorizeUI = false;
    private int rejectFlag = 0;
    private String type = "";
    private double commPercentage = 0;
    private String path = "";
    
    /** Creates new form BeanForm */
    public DailyAccountTransUI() {
        initComponents();
        settingupUI();
        tabDailyLoanTransaction.resetVisits();
    }

    private void settingupUI() {
        setFieldNames();
        observable = new DailyAccountTransOB();
        initComponentData();
        setMaximumLength();
        setButtonEnableDisable();
        addRadioButtons();
        ClientUtil.enableDisable(panDailyLoanTransaction, false);
        ClientUtil.enableDisable(panLoadFromFile, false);
        lblAgentNameVal.setText("                                ");
        lblLoanAdjustmentAgentNameVal.setText("                                ");
        curDate = ClientUtil.getCurrentDate();
        btnSaveFile.setVisible(false);
        btnSaveFile.setEnabled(false);
        btnDeleteFile.setVisible(false);
        btnDeleteFile.setEnabled(false);
        btnBrowse.setEnabled(false);
        btnLoanAdjustmentDisplay.setEnabled(false);
        tabDailyLoanTransaction.remove(1);
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

    private void addRadioButtons() {
        rgpDataMode = new CButtonGroup();
        rgpDataMode.add((rdoImport));
        rgpDataMode.add((rdoExport));
    }

    private void removeRadioButtons() {
        rgpDataMode.remove((rdoImport));
        rgpDataMode.remove((rdoExport));
    }
    
    //KD-3590 : daily collection double entry- issue
     private void deletescreenLock() {
        HashMap map = new HashMap();
        map.put("USER_ID", ProxyParameters.USER_ID);
        map.put("TRANS_DT", curDate.clone());
        map.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
        ClientUtil.execute("DELETE_SCREEN_LOCK", map);
    }

    private void insertScreenLock() {
        HashMap hash = new HashMap();
        hash.put("USER_ID", ProxyParameters.USER_ID);
        hash.put("TRANS_ID", cboAgentType.getSelectedItem());
        hash.put("MODE_OF_OPERATION", viewType);
        hash.put("TRANS_DT", curDate.clone());
        hash.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
        ClientUtil.execute("insertauthorizationLock", hash);
    }

    private boolean validateScreenLock() {
        HashMap authDataMap = new HashMap();
        authDataMap.put("TRANS_ID", cboAgentType.getSelectedItem());
        authDataMap.put("USER_ID", ProxyParameters.USER_ID);
        authDataMap.put("TRANS_DT", curDate.clone());
        authDataMap.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);

        List lst = ClientUtil.executeQuery("selectauthorizationLock", authDataMap);
        StringBuffer open = new StringBuffer();
        if (lst != null && lst.size() > 0) {
            for (int i = 0; i < lst.size(); i++) {
                HashMap map = (HashMap) lst.get(i);
                open.append("\n" + "User Id  :" + " ");
                open.append(CommonUtil.convertObjToStr(map.get("OPEN_BY")) + "\n");
                open.append("Mode Of Operation  :" + " ");
                open.append(CommonUtil.convertObjToStr(map.get("MODE_OF_OPERATION")) + " ");
            }
            ClientUtil.showMessageWindow("Already opened by" + open);
            return true;
        }
        return false;
    }
    
    
    private void initComponentData() {
        try {
            cboAgentType.setModel(observable.getCbmAgentType());
//            cboLoanAdjustmentAgentType.setModel(observable.getCbmLoanAdjustmentAgentType());
//            TableCellEditor editor = new DefaultCellEditor(txtPayment);
//            SimpleTableModel stm = new SimpleTableModel((ArrayList) observable.getTableList(), (ArrayList) observable.getTableTitle());
//            tblDailyLoanTransactionTable.setModel(stm);
//            tblDailyLoanTransactionTable.getColumnModel().getColumn(10).setCellEditor(editor);
//            tblDailyLoanTransactionTable.revalidate();
//            tblLoanAdjustmentTable.setModel(observable.getTblLoanAdjustmentTabls());
        } catch (ClassCastException e) {
            parseException.logException(e, true);
        }
    }

    private void setMaximumLength() {
        txtTotalPayment.setValidation(new CurrencyValidation());
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
        rgpDataMode = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrAdvances = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace6 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace26 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace27 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace28 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace29 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace30 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace31 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        tabDailyLoanTransaction = new com.see.truetransact.uicomponent.CTabbedPane();
        panDailyLoanTransaction = new com.see.truetransact.uicomponent.CPanel();
        panDailyLoanTransactionTable = new com.see.truetransact.uicomponent.CPanel();
        srpDailyLoanTransactionTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblDailyLoanTransactionTable = new com.see.truetransact.uicomponent.CTable();
        srpProcess = new javax.swing.JScrollPane();
        txtProcess = new com.see.truetransact.uicomponent.CTextArea();
        panAgentDetails = new com.see.truetransact.uicomponent.CPanel();
        panLoadFromFile = new com.see.truetransact.uicomponent.CPanel();
        txtFileName = new com.see.truetransact.uicomponent.CTextField();
        lblFileName = new com.see.truetransact.uicomponent.CLabel();
        btnBrowse = new com.see.truetransact.uicomponent.CButton();
        btnSaveFile = new com.see.truetransact.uicomponent.CButton();
        btnDeleteFile = new com.see.truetransact.uicomponent.CButton();
        cPanel1 = new com.see.truetransact.uicomponent.CPanel();
        lblAgentId = new com.see.truetransact.uicomponent.CLabel();
        lblAgentNameVal = new com.see.truetransact.uicomponent.CLabel();
        lblColDt = new com.see.truetransact.uicomponent.CLabel();
        tdtCollectionDt = new com.see.truetransact.uicomponent.CDateField();
        cboAgentType = new com.see.truetransact.uicomponent.CComboBox();
        cPanel2 = new com.see.truetransact.uicomponent.CPanel();
        rdoImport = new com.see.truetransact.uicomponent.CRadioButton();
        rdoExport = new com.see.truetransact.uicomponent.CRadioButton();
        panTotalPayment = new com.see.truetransact.uicomponent.CPanel();
        txtTotalPayment = new com.see.truetransact.uicomponent.CTextField();
        lblTotalPayment = new com.see.truetransact.uicomponent.CLabel();
        txtPayment = new com.see.truetransact.uicomponent.CTextField();
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

        lblSpace26.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace26.setText("     ");
        lblSpace26.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace26);

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

        lblSpace27.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace27.setText("     ");
        lblSpace27.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace27);

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

        lblSpace28.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace28.setText("     ");
        lblSpace28.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace28);

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

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace29);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.setFocusable(false);
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnException);

        lblSpace30.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace30.setText("     ");
        lblSpace30.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace30);

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

        lblSpace31.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace31.setText("     ");
        lblSpace31.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace31);

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
                "Loan No", "Name", "Limit", "Last Int Date", "Balance", "Principal Due", "Interest", "Penal", "Charge", "Total Due", "Repayment"
            }
        ));
        tblDailyLoanTransactionTable.setPreferredScrollableViewportSize(new java.awt.Dimension(796, 196));
        tblDailyLoanTransactionTable.setPreferredSize(new java.awt.Dimension(800, 8000));
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

        srpProcess.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Data Exported..", 0, 0, new java.awt.Font("Tahoma", 1, 12))); // NOI18N
        srpProcess.setPreferredSize(new java.awt.Dimension(700, 250));

        txtProcess.setColumns(20);
        txtProcess.setEditable(false);
        txtProcess.setRows(15);
        txtProcess.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtProcess.setEnabled(false);
        srpProcess.setViewportView(txtProcess);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panDailyLoanTransactionTable.add(srpProcess, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 2, 0);
        panDailyLoanTransaction.add(panDailyLoanTransactionTable, gridBagConstraints);

        panAgentDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Agent Details"));
        panAgentDetails.setMinimumSize(new java.awt.Dimension(825, 110));
        panAgentDetails.setPreferredSize(new java.awt.Dimension(825, 110));
        panAgentDetails.setLayout(new java.awt.GridBagLayout());

        panLoadFromFile.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panLoadFromFile.setMinimumSize(new java.awt.Dimension(420, 50));
        panLoadFromFile.setPreferredSize(new java.awt.Dimension(420, 50));
        panLoadFromFile.setLayout(new java.awt.GridBagLayout());

        txtFileName.setMinimumSize(new java.awt.Dimension(200, 21));
        txtFileName.setPreferredSize(new java.awt.Dimension(200, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panLoadFromFile.add(txtFileName, gridBagConstraints);

        lblFileName.setText("File");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLoadFromFile.add(lblFileName, gridBagConstraints);

        btnBrowse.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_OPENFLD.jpg"))); // NOI18N
        btnBrowse.setText("Browse");
        btnBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panLoadFromFile.add(btnBrowse, gridBagConstraints);

        btnSaveFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSaveFile.setText("Save");
        btnSaveFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveFileActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 5);
        panLoadFromFile.add(btnSaveFile, gridBagConstraints);

        btnDeleteFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDeleteFile.setText("Delete");
        btnDeleteFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteFileActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 5);
        panLoadFromFile.add(btnDeleteFile, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        panAgentDetails.add(panLoadFromFile, gridBagConstraints);

        cPanel1.setMinimumSize(new java.awt.Dimension(320, 85));
        cPanel1.setPreferredSize(new java.awt.Dimension(320, 85));
        cPanel1.setLayout(new java.awt.GridBagLayout());

        lblAgentId.setText("AgentId");
        cPanel1.add(lblAgentId, new java.awt.GridBagConstraints());

        lblAgentNameVal.setForeground(new java.awt.Color(0, 0, 153));
        lblAgentNameVal.setFont(new java.awt.Font("MS Sans Serif", 1, 14)); // NOI18N
        lblAgentNameVal.setPreferredSize(new java.awt.Dimension(280, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 8, 0);
        cPanel1.add(lblAgentNameVal, gridBagConstraints);

        lblColDt.setText("collection Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        cPanel1.add(lblColDt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 102);
        cPanel1.add(tdtCollectionDt, gridBagConstraints);

        cboAgentType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboAgentTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 105);
        cPanel1.add(cboAgentType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        panAgentDetails.add(cPanel1, gridBagConstraints);

        cPanel2.setMinimumSize(new java.awt.Dimension(400, 35));
        cPanel2.setPreferredSize(new java.awt.Dimension(400, 35));
        cPanel2.setLayout(new java.awt.GridBagLayout());

        rdoImport.setText("Import to CBMS");
        rdoImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoImportActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        cPanel2.add(rdoImport, gridBagConstraints);

        rdoExport.setText("Export to Device");
        rdoExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoExportActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 17, 0, 0);
        cPanel2.add(rdoExport, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        panAgentDetails.add(cPanel2, gridBagConstraints);

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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panDailyLoanTransaction.add(panTotalPayment, gridBagConstraints);

        tabDailyLoanTransaction.addTab("Daily Account Transactions", panDailyLoanTransaction);

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

    private void btnDisplaydetails() throws Exception {
        //if(cboAgentType.getSelectedIndex()>0){
        clearTable();
        HashMap deleteMap = new HashMap();
        deleteMap.put("AGENT_ID", cboAgentType.getSelectedItem());
        if (txtFileName.getText() != null) {
            try {
                observable.setCollectionDt(DateUtil.getDateMMDDYYYY(tdtCollectionDt.getDateValue()));
                if(observable.setTableDetails(totCustomerList, CommonUtil.convertObjToStr(cboAgentType.getSelectedItem()))){
                    if(observable.getImportedAgentExistData(CommonUtil.convertObjToStr(cboAgentType.getSelectedItem()))){
                        if(!observable.getUnauthorizedAgentData(CommonUtil.convertObjToStr(cboAgentType.getSelectedItem()))){
                            setTableData();  
                            calc() ;
                            btnBrowse.setEnabled(false);
                        }else{
                            ClientUtil.showMessageWindow("Authorization Pending For Selected Agent!!"); 
                            deleteMap.put("COMMAND", CommonConstants.TOSTATUS_DELETE);
                            deleteMap.put("AGENT_ID", cboAgentType.getSelectedItem());
                            deleteMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                            deleteMap.put("COL_DATE", DateUtil.getDateMMDDYYYY(tdtCollectionDt.getDateValue()));
                            observable.doAction(deleteMap);
                            btnCancelActionPerformed(null);
                            return;
                        }
                    }else{
                        ClientUtil.showMessageWindow("Selected Agent does't match with ImportList!!"); 
                        deleteMap.put("COMMAND", CommonConstants.TOSTATUS_DELETE);
                        deleteMap.put("AGENT_ID", cboAgentType.getSelectedItem());
                        deleteMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                        deleteMap.put("COL_DATE", DateUtil.getDateMMDDYYYY(tdtCollectionDt.getDateValue()));
                        observable.doAction(deleteMap);
                        btnCancelActionPerformed(null);
                        return;
                    }
                }else{
                    ClientUtil.showMessageWindow("Please check Importing Data !!");  
                    btnBrowse.setEnabled(true);
                    ClientUtil.clearAll(panLoadFromFile);
                    ClientUtil.clearAll(cPanel2);
                    return;
                }
            } catch (Exception ex) {
                Logger.getLogger(DailyAccountTransUI.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            }
        }
        //else
        //observable.getTableDetails(loanMap);
        //txtTotalPayment.setText("0.0");
        txtPayment.setEnabled(true);
        tblDailyLoanTransactionTable.setEnabled(true);
        ClientUtil.enableDisable(panAgentDetails, false);
        btnSaveFile.setEnabled(false);
        btnDeleteFile.setEnabled(true);
        //setSizeTableData();
        //((SimpleTableModel) tblDailyLoanTransactionTable.getModel()).fireTableDataChanged();
        //return;
        // }
    }    public void checkingPaymentAmount(int selectedRow) {
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
                totPayment = totPayment + CommonUtil.convertObjToDouble(tblDailyLoanTransactionTable.getValueAt(i, 2));
                txtTotalPayment.setText(CurrencyValidation.formatCrore(String.valueOf(totPayment)));
            }
        }
    }
    
    public void calcAuth() {
        double totPayment = 0;
        double totCommision = 0;
        if (tblDailyLoanTransactionTable.getRowCount() > 0) {
            for (int i = 0; i < tblDailyLoanTransactionTable.getRowCount(); i++) {                
                totPayment = totPayment + CommonUtil.convertObjToDouble(tblDailyLoanTransactionTable.getValueAt(i, 6)).doubleValue();
                txtTotalPayment.setText(CurrencyValidation.formatCrore(String.valueOf(totPayment)));
            }
        }
    }
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
            if (col != 10) {
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
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("AUTHORIZED_BY", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("AUTHORIZED_DT", ClientUtil.getCurrentDateWithTime());
            authorizeMap.put("BATCHID", observable.getBatchId());
            authorizeMap.put("TRANS_DT", curDate.clone());
            authorizeMap.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
            singleAuthorizeMap.put("BATCH_ID", observable.getBatchId());
            arrList = (ArrayList) ClientUtil.executeQuery("getBatchTxTransferTOs", authorizeMap);            
            singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorize(singleAuthorizeMap);          
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
            mapParam.put(CommonConstants.MAP_NAME, "getDailyDepositTransForAuthorize1");            
            isFilled = false;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
            observable.setStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }

    public void authorize(HashMap map) {
        System.out.println("Authorize Map : " + map);
        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setAuthorizeMap(map);
            map.put("COMMAND",CommonConstants.AUTHORIZESTATUS);
            map.put("COL_DATE", DateUtil.getDateMMDDYYYY(tdtCollectionDt.getDateValue()));
            map.put("AGENT_ID", cboAgentType.getSelectedItem());
            observable.doAction(map);
            btnCancelActionPerformed(null);
        }
        observable.setStatus();
        observable.setResultStatus();
        lblStatus.setText(observable.getLblStatus());
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
                observable.setBatchId(CommonUtil.convertObjToStr(hashMap.get("BATCH_ID")));
                cboAgentType.setSelectedItem(CommonUtil.convertObjToStr(hashMap.get("AGENT_ID")));
                 if (validateScreenLock()) {
                        return;
                    }
                insertScreenLock();
                if(cboAgentType.getSelectedItem()!=null){
                    cboAgentTypeActionPerformed(null);
                }
                observable.getData(hashMap);
                setTableData();
                calcAuth();
            }
            if (panDailyLoanTransaction.isShowing() == true) {
                btnSaveFile.setEnabled(false);
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
                btnSaveFile.setEnabled(false);
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
        btnBrowse.setEnabled(true);
        addRadioButtons(); 
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
            HashMap dataMap = new HashMap();   
            StringBuffer strBMandatory = new StringBuffer();
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                if(!(cboAgentType.getSelectedItem()!=null && !cboAgentType.getSelectedItem().equals(""))){
                    strBMandatory.append("Please select Agent Type!!! ");
                    strBMandatory.append("\n");
                }else if(!(tblDailyLoanTransactionTable.getRowCount()>0)){
                    strBMandatory.append("Please Browse the Import File!!! ");
                    strBMandatory.append("\n");
                }
                String message = strBMandatory.toString();
                if (message.trim().length() > 0) {
                    CommonMethods.displayAlert(message);
                    return;
                }else{
                    try{
                    dataMap.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
                    dataMap.put("AGENT_ID", cboAgentType.getSelectedItem());
                    dataMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                    dataMap.put("COL_DATE", DateUtil.getDateMMDDYYYY(tdtCollectionDt.getDateValue()));
                    //observable.setTableFinalMap();
                    observable.doAction(dataMap);
                    }catch(Exception e){
                        e.printStackTrace();
                        ClientUtil.showAlertWindow(e.getMessage());
                    }
                }   
            }
            if (observable.getProxyReturnMap()!=null && observable.getProxyReturnMap().size()>0) {
               displayTransDetail(observable.getProxyReturnMap());
            }
            btnCancelActionPerformed(null);
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
        if(observable.getProxyReturnMap().get("CASH_TRANS_ID")!=null && !observable.getProxyReturnMap().get("CASH_TRANS_ID").equals("")){
            yesNo = COptionPane.showOptionDialog(null,"Transaction completed,Transfer Batch ID :"+observable.getProxyReturnMap().get("TRANS_ID")+"\n"+"Cash Trans ID :"+ observable.getProxyReturnMap().get("CASH_TRANS_ID")+"\n"+"Do you want to print?", CommonConstants.WARNINGTITLE,
            COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
            null, options, options[0]);
        }else{
            yesNo = COptionPane.showOptionDialog(null,"Transaction completed,Transfer Batch ID :"+observable.getProxyReturnMap().get("TRANS_ID")+"\n Do you want to print?", CommonConstants.WARNINGTITLE,
            COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
            null, options, options[0]);
        }
        System.out.println("#$#$$ yesNo : "+yesNo);
        if (yesNo==0) {
            if (returnMap != null) {
                if (returnMap.containsKey("TRANS_ID")) {
                    HashMap paramMap = new HashMap();
                    TTIntegration ttIntgration = null;
                    paramMap.put("TransId", returnMap.get("SINGLE_TRANS_ID"));
                    paramMap.put("TransDt", curDate.clone());
                    paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
                    ttIntgration.setParam(paramMap);
                    ttIntgration.integrationForPrint("ReceiptPayment", false);                  
                }
                if (observable.getProxyReturnMap().containsKey("CASH_TRANS_ID")) {
                    HashMap paramMap = new HashMap();
                    TTIntegration ttIntgration = null;
                    paramMap.put("TransId", returnMap.get("SINGLE_TRANS_ID"));
                    paramMap.put("TransDt", curDate.clone());
                    paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
                    ttIntgration.setParam(paramMap);
                    ttIntgration.integrationForPrint("CashReceipt", false);
                }
            }
       }
 }

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        deletescreenLock();
        System.out.println("observable.getProxyReturnMap()$^$^#^"+observable.getProxyReturnMap());
        if(observable.getActionType()!=ClientConstants.ACTIONTYPE_AUTHORIZE){
            if (!(observable.getProxyReturnMap()!=null && observable.getProxyReturnMap().size()>0)) {
                    if(tblDailyLoanTransactionTable.getRowCount()>0){
                        ClientUtil.showMessageWindow("Save or Delete Imported Data!!!");
                        return;
                    }
            }
        }
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
        btnSaveFile.setEnabled(false);
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
        if (fromAuthorizeUI) {
            this.dispose();
            authorizeListUI.setFocusToTable();
        }
        //__ Make the Screen Closable..
        btnSaveFile.setVisible(false);
        btnSaveFile.setEnabled(false);
        btnDeleteFile.setVisible(false);
        btnDeleteFile.setEnabled(false);
        btnBrowse.setEnabled(false);
        srpDailyLoanTransactionTable.setVisible(true);
        srpProcess.setVisible(false);
        setModified(false);
        removeRadioButtons();
        
    }//GEN-LAST:event_btnCancelActionPerformed
    private void clearTable() {
        //observable.resetForm();        
        observable.clearTable();
        tblDailyLoanTransactionTable.setModel(observable.getClearTable());  
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
    }//GEN-LAST:event_btnLoanAdjustmentDisplayActionPerformed

    private void btnLoanAdjustmentClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoanAdjustmentClearActionPerformed
        // TODO add your handling code here:        
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
   if(rdoExport.isSelected()||rdoImport.isSelected()){
        browseActionPerformed();        
    }else{
        ClientUtil.showMessageWindow("Please select Import or Export!!!");
        return;
    }
}//GEN-LAST:event_btnBrowseActionPerformed

private void rdoExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoExportActionPerformed
// TODO add your handling code here:
    if(rdoExport.isSelected()){
        btnSaveFile.setEnabled(true);
        btnSaveFile.setVisible(true);
        btnDeleteFile.setEnabled(false);
        btnDeleteFile.setVisible(false);
        try {
            if (observable.getUnauthorizedAgentData(CommonUtil.convertObjToStr(cboAgentType.getSelectedItem()))) {
                ClientUtil.showMessageWindow("Authorization Pending For Selected Agent!!");
                return;
            }
        } catch (Exception ex) {
            Logger.getLogger(DailyAccountTransUI.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }
}//GEN-LAST:event_rdoExportActionPerformed

private void rdoImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoImportActionPerformed
// TODO add your handling code here:
    if(rdoImport.isSelected()){
        btnSaveFile.setEnabled(false);
        btnSaveFile.setVisible(false);
        btnDeleteFile.setEnabled(true);
        btnDeleteFile.setVisible(true);
    }
}//GEN-LAST:event_rdoImportActionPerformed

private void btnSaveFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveFileActionPerformed
// TODO add your handling code here:
    if (!CommonUtil.convertObjToStr(cboAgentType.getSelectedItem()).equals("") && CommonUtil.convertObjToStr(cboAgentType.getSelectedItem())!=null) {
        if (!CommonUtil.convertObjToStr(path).equals("")) {
                writeExportFile();
        }else {
            ClientUtil.showMessageWindow("Please select the file!!!");
            return;
        }
    }else{
        ClientUtil.showMessageWindow("Please select the AgentId!!!");
        return;
    }    
}//GEN-LAST:event_btnSaveFileActionPerformed

private void cboAgentTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboAgentTypeActionPerformed
// TODO add your handling code here:
      if (cboAgentType.getSelectedIndex() > 0) {
            HashMap agentMap = new HashMap();
            agentMap.put("AGENT_ID", CommonUtil.convertObjToStr(cboAgentType.getSelectedItem()));
            List lst = ClientUtil.executeQuery("getAgentDetailsName", agentMap);
            agentMap = null;
            if (lst != null && lst.size() > 0) {
                agentMap = (HashMap) lst.get(0);
                observable.setAgentMap(agentMap);
                lblAgentNameVal.setText(CommonUtil.convertObjToStr(agentMap.get("AGENT_NAME")));
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
        }
        
}//GEN-LAST:event_cboAgentTypeActionPerformed

private void btnDeleteFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteFileActionPerformed
// TODO add your handling code here:
     //In delete
    setModified(false);
    HashMap deleteMap = new HashMap();
    if (tblDailyLoanTransactionTable.getRowCount() > 0) {
        int yesNo = 0;
        String[] options = {"Yes", "No"};
        yesNo = COptionPane.showOptionDialog(null, "Do you want to Delete Imported Data?", CommonConstants.WARNINGTITLE,
                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                null, options, options[0]);
        if (yesNo == 0) {
            try {
                deleteMap.put("COMMAND", CommonConstants.TOSTATUS_DELETE);
                deleteMap.put("AGENT_ID", cboAgentType.getSelectedItem());
                deleteMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                deleteMap.put("COL_DATE", DateUtil.getDateMMDDYYYY(tdtCollectionDt.getDateValue()));
                observable.doAction(deleteMap);
                //arrearDetails();
                clearTable();
                //btnCancel
                btnCancelActionPerformed(null);
            } catch (Exception ex) {
                ex.printStackTrace();
                ClientUtil.showMessageWindow(ex.getMessage());
            }
        }
    } else {
        ClientUtil.showMessageWindow("Please Import Data  First!!!");
    }
}//GEN-LAST:event_btnDeleteFileActionPerformed
    private void browseActionPerformed() {
        // TODO add your handling code here:

        try {
            //cboAgentType.removeAllItems();
            //observable.getTbmTransfer().setRowCount(0);
            String defaultPath = "";
            String exePath = "";
            if(rdoImport.isSelected()){
                defaultPath = CommonUtil.convertObjToStr(observable.getAgentMap().get("IMPORT_FILE_PATH"));
                exePath = CommonUtil.convertObjToStr(observable.getAgentMap().get("IMPORT_BAT_FILE"));                
            }else{
                defaultPath = CommonUtil.convertObjToStr(observable.getAgentMap().get("EXPORT_FILE_PATH")); 
                exePath = CommonUtil.convertObjToStr(observable.getAgentMap().get("EXPORT_BAT_FILE"));
            }
            if(exePath!=null && exePath.length()>0){
                try {
                    String[] command = {"cmd.exe", "/C", "Start", "E:\\100 Degree Celcius\\Transfer 1 1 master.trn"};
                    Process p =  Runtime.getRuntime().exec(command);           
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            javax.swing.JFileChooser fc = new javax.swing.JFileChooser(defaultPath);
            int returnVal = fc.showOpenDialog(null);
            if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
                java.io.File file = fc.getSelectedFile();
                String fname = file.getName();
                fname = fname.toUpperCase();
                if (fname != null || !fname.equalsIgnoreCase("")) {
                    if (fname.substring(fname.indexOf('.') + 1, fname.length()).equals("TXT") || fname.substring(fname.indexOf('.') + 1, fname.length()).equals("MST") || fname.substring(fname.indexOf('.') + 1, fname.length()).equals("DAT")) {
                        txtFileName.setText(fc.getSelectedFile().toString());
                        path = fc.getSelectedFile().getAbsolutePath();
                        System.out.println("path%#$%$%$#"+path);
                    } else {
                        displayAlert("File should be .txt OR .MST format");
                        return;
                    }
                }else {
                        displayAlert("File name should not empty!!");
                        return;
                }
                if(rdoImport.isSelected()){
                    fillAgentName(file);
                    //Display table
                    //setTableData();
                }
            }
        } catch (Exception e) {
            displayAlert(e.getMessage());
        }
    }
    
    private void setTableData(){
        HashMap whereMap = new HashMap();
        HashMap dataMap = new HashMap();
        whereMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        whereMap.put("AGENT_ID", CommonUtil.convertObjToStr(cboAgentType.getSelectedItem()));   
        whereMap.put("TABLE_NAME", observable.getAgentMap().get("IMP_QUERY"));  
        System.out.println("whereMap^#^#^#^#^"+whereMap);
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            dataMap.put(CommonConstants.MAP_NAME, "getImportDailyAccountData");
        }else if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ) {
            whereMap.put(CommonConstants.INITIATED_BRANCH, ProxyParameters.BRANCH_ID);
            whereMap.put("BATCH_ID", observable.getBatchId());
            whereMap.put("TRANS_DT", curDate.clone());
            dataMap.put(CommonConstants.MAP_NAME, "getDailyAuthorizeData");
        }
        dataMap.put(CommonConstants.MAP_WHERE, whereMap);
        try {
            System.out.println("dataMap^#^#^#^#^"+dataMap);
            observable.populateData(dataMap, tblDailyLoanTransactionTable);
            tblDailyLoanTransactionTable.setModel(observable.getTblImportData());
        } catch (Exception e) {
            System.err.println("Exception " + e.toString() + "Caught");
            e.printStackTrace();
        }
    }
    
    private void writeExportFile() {
            HashMap dataMap = new HashMap();
            StringBuffer buffer = new StringBuffer();
            dataMap.put("AGENT_ID", CommonUtil.convertObjToStr(cboAgentType.getSelectedItem()));
            dataMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            dataMap.put("TABLE_NAME", observable.getAgentMap().get("EXP_QUERY")); 
             System.out.println("dataMap####writeExportFile" + dataMap);
            List lst = ClientUtil.executeQuery("getDailyAccountTransData", dataMap);
            if (lst != null && lst.size() > 0) {                
                srpDailyLoanTransactionTable.setVisible(false);
                srpProcess.setVisible(true);
                try {
                    java.io.FileWriter write = new java.io.FileWriter(path, false);
                    java.io.PrintWriter print_line = new java.io.PrintWriter(write);
                    for (int i = 0; i < lst.size(); i++) {
                        HashMap resultMap = (HashMap) lst.get(i);
                        print_line.print(resultMap.get("MASTDATA"));
                        print_line.println();
                        txtProcess.append(CommonUtil.convertObjToStr(resultMap.get("MASTDATA")));
                        txtProcess.append("\n");
                    }
                    txtProcess.append("Export completed...\nFile path is :" +path);
                    ClientUtil.enableDisable(panAgentDetails, false);
                    ClientUtil.enableDisable(panLoadFromFile, false);
                    btnSaveFile.setEnabled(false);
                    btnBrowse.setEnabled(false);
                    btnSave.setEnabled(false);
                    if (print_line != null) {
                        print_line.close();
                    }
                    if (write != null) {
                        write.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //displayAlert("Export completed...\nFile path is :" + path);
            }else{
                ClientUtil.showMessageWindow("Please check the export TABLE_NAME or Data!!");
            }
            //btnCancelActionPerformed(null);
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
            String separator = "";
            String qualifeir = "";
            boolean skippLine = false;
            if(observable.getAgentMap()!=null && observable.getAgentMap().size()>0){
                if(observable.getAgentMap().containsKey("COLSEP") && observable.getAgentMap().get("COLSEP")!=null){
                   separator = CommonUtil.convertObjToStr(observable.getAgentMap().get("COLSEP"));                        
                }else{
                    ClientUtil.showMessageWindow("Please check the text separator !!");
                    return;
                }
                if(observable.getAgentMap().containsKey("TXT_QUALIFIER") && observable.getAgentMap().get("TXT_QUALIFIER")!=null){
                    qualifeir = CommonUtil.convertObjToStr(observable.getAgentMap().get("TXT_QUALIFIER"));
                }
                if(observable.getAgentMap().containsKey("SKIP_FIRST_LINE") && observable.getAgentMap().get("SKIP_FIRST_LINE")!=null){
                    if(observable.getAgentMap().get("SKIP_FIRST_LINE").equals("Y")){
                       skippLine = true; 
                    }else{
                       skippLine = false;
                    }
                }else{
                    ClientUtil.showMessageWindow("Please check the SKIP_FIRST_LINE parameter !!");
                    return;
                }
            }
            String line = "";
            int x = 0;
            while (dis.available() > 0 && (line = dis.readLine()).length() > 0) {            
                String numberParameter[] = (String[]) line.split(separator);
                customerList = new ArrayList();
                HashMap dataMap = new HashMap();
                for (int i = 0; i < numberParameter.length; i++) {;
                    if (skippLine) {
                        agentMap.put("AGENT_ID", numberParameter[0]);
                        skippLine = false;
                        break;
                    } else { 
                        agentMap.put("AGENT_ID", numberParameter[0]);
                        if (numberParameter[i].contains("/")) {
                            numberParameter[i] = numberParameter[i].replaceAll("/", "-");
                        }
                        if (qualifeir!=null && !qualifeir.equals("") && qualifeir.length()>0) {
                            System.out.println("numberParameter[i]$^&$&"+numberParameter[i]);                            
                            if (numberParameter[i].contains(qualifeir)) {
                                numberParameter[i] = numberParameter[i].replaceAll(qualifeir, "");
                            }
                        }                        
                        customerList.add(numberParameter[i]);
                    }
                }
                if (customerList!=null && customerList.size()>0) {
                    totCustomerList.add(customerList);
                }
            }
            if (!(totCustomerList!=null && totCustomerList.size()>0)) {
                  ClientUtil.showMessageWindow("Can't Read the File!!");
                  return;
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
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDeleteFile;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnLoanAdjustmentClear;
    private com.see.truetransact.uicomponent.CButton btnLoanAdjustmentDisplay;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnSaveFile;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CPanel cPanel1;
    private com.see.truetransact.uicomponent.CPanel cPanel2;
    private com.see.truetransact.uicomponent.CComboBox cboAgentType;
    private com.see.truetransact.uicomponent.CComboBox cboLoanAdjustmentAgentType;
    private com.see.truetransact.uicomponent.CCheckBox chkSelectAll;
    private com.see.truetransact.uicomponent.CLabel lblAgentId;
    private com.see.truetransact.uicomponent.CLabel lblAgentNameVal;
    private com.see.truetransact.uicomponent.CLabel lblColDt;
    private com.see.truetransact.uicomponent.CLabel lblFileName;
    private com.see.truetransact.uicomponent.CLabel lblLoanAdjustmentAgentName;
    private com.see.truetransact.uicomponent.CLabel lblLoanAdjustmentAgentNameVal;
    private com.see.truetransact.uicomponent.CLabel lblLoanAdjustmentAgentType;
    private com.see.truetransact.uicomponent.CLabel lblLoanAdjustmentCollectionDt;
    private com.see.truetransact.uicomponent.CLabel lblLoanAdjustmentTotalPayment;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblSelectAll;
    private com.see.truetransact.uicomponent.CLabel lblSpace;
    private com.see.truetransact.uicomponent.CLabel lblSpace26;
    private com.see.truetransact.uicomponent.CLabel lblSpace27;
    private com.see.truetransact.uicomponent.CLabel lblSpace28;
    private com.see.truetransact.uicomponent.CLabel lblSpace29;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace30;
    private com.see.truetransact.uicomponent.CLabel lblSpace31;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace6;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
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
    private com.see.truetransact.uicomponent.CRadioButton rdoExport;
    private com.see.truetransact.uicomponent.CRadioButton rdoImport;
    private com.see.truetransact.uicomponent.CButtonGroup rgpDataMode;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private javax.swing.JSeparator sptException;
    private com.see.truetransact.uicomponent.CScrollPane srpDailyLoanTransactionTable;
    private com.see.truetransact.uicomponent.CScrollPane srpLoanAdjustmentTable;
    private javax.swing.JScrollPane srpProcess;
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
    private com.see.truetransact.uicomponent.CTextArea txtProcess;
    private com.see.truetransact.uicomponent.CTextField txtTotalPayment;
    // End of variables declaration//GEN-END:variables

    public static void main(String[] arg) {
        try {
            javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Throwable th) {
            th.printStackTrace();
        }
        javax.swing.JFrame jf = new javax.swing.JFrame();
        DailyAccountTransUI gui = new DailyAccountTransUI();
        jf.getContentPane().add(gui);
        jf.setSize(536, 566);
        jf.show();
        gui.show();
    }
}