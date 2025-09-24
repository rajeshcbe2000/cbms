/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * MultiLevelControlUI.java
 *
 * Created on September 9, 2004, 12:18 PM
 */

package com.see.truetransact.ui.sysadmin.levelcontrol.multilevel;

/**
 *
 * @author  Pinky
 */

import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.CurrencyValidation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import com.see.truetransact.ui.TrueTransactMain;

public class MultiLevelControlUI extends CInternalFrame implements Observer, UIMandatoryField  {
    
    private MultiLevelControlRB resourceBundle;
    private HashMap mandatoryMap;
    private MultiLevelControlMRB objMandatoryRB;
    private MultiLevelControlOB observable;
    
    private static int LEVEL_ID = 500;
    private static int AUTHORIZE = 501;
    
    private static int viewType;
    
    //private static boolean isValid=false;
    private static boolean isFilled=false;
    
    /** Creates new form BeanForm */
    public MultiLevelControlUI() {
        initComponents();
        initSetup();
    }
    /** Initial set up */
    private void initSetup(){
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setObservable();
        setHelpMessage();
        setMaximumLength();
        initComponentData();
        
        ClientUtil.enableDisable(this, false);
        enableDisableButtons(false);
        setupMenuToolBarPanel();
        
        this.resetUI();
    }
    /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        btnAND.setName("btnAND");
        btnAdd.setName("btnAdd");
        btnAuthorize.setName("btnAuthorize");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnDelete1.setName("btnDelete1");
        btnDone.setName("btnDone");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnLevelID.setName("btnLevelID");
        btnOR.setName("btnOR");
        btnRejection.setName("btnRejection");
        btnReport.setName("btnReport");
        btnSave.setName("btnSave");
        cInternalFrame1.setName("cInternalFrame1");
        cboCondition.setName("cboCondition");
        chkCashCredit.setName("chkCashCredit");
        chkCashDebit.setName("chkCashDebit");
        chkClearingCredit.setName("chkClearingCredit");
        chkClearingDebit.setName("chkClearingDebit");
        chkTransferCredit.setName("chkTransferCredit");
        chkTransferDebit.setName("chkTransferDebit");
        lblAmount.setName("lblAmount");
        lblCondition.setName("lblCondition");
        lblExpression.setName("lblExpression");
        lblLevelNameValue.setName("lblLevelNameValue");
        lblLevelID.setName("lblLevelID");
        lblLevelName.setName("lblLevelName");
        lblMsg.setName("lblMsg");
        lblNoOfPersons.setName("lblNoOfPersons");
        lblSep3.setName("lblSep3");
        lblStatus.setName("lblStatus");
        lblStatusValue.setName("lblStatusValue");
        lblTransaction.setName("lblTransaction");
        panCommand.setName("panCommand");
        panLevel.setName("panLevel");
        panLevelID.setName("panLevelID");
        panLevelTable.setName("panLevelTable");
        panMainLevel.setName("panMainLevel");
        panMultiLevel.setName("panMultiLevel");
        panOperation.setName("panOperation");
        panStatus.setName("panStatus");
        panTransaction.setName("panTransaction");
        srpLevel.setName("srpLevel");
        tblLevel.setName("tblLevel");
        txtAmount.setName("txtAmount");
        txtExpression.setName("txtExpression");
        txtLevelID.setName("txtLevelID");
        txtNoOfPersons.setName("txtNoOfPersons");
    }
    /* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        resourceBundle = new MultiLevelControlRB();        
        chkClearingCredit.setText(resourceBundle.getString("chkClearingCredit"));
        btnRejection.setText(resourceBundle.getString("btnRejection"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        btnOR.setText(resourceBundle.getString("btnOR"));
        btnAdd.setText(resourceBundle.getString("btnAdd"));
        chkCashDebit.setText(resourceBundle.getString("chkCashDebit"));
        ((TitledBorder)panMainLevel.getBorder()).setTitle(resourceBundle.getString("panMainLevel"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        chkClearingDebit.setText(resourceBundle.getString("chkClearingDebit"));
        btnReport.setText(resourceBundle.getString("btnReport"));
        lblCondition.setText(resourceBundle.getString("lblCondition"));
        chkTransferCredit.setText(resourceBundle.getString("chkTransferCredit"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        ((TitledBorder)panMultiLevel.getBorder()).setTitle(resourceBundle.getString("panMultiLevel"));
        lblExpression.setText(resourceBundle.getString("lblExpression"));
        btnAND.setText(resourceBundle.getString("btnAND"));
        lblTransaction.setText(resourceBundle.getString("lblTransaction"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblLevelNameValue.setText(resourceBundle.getString("lblLevelNameValue"));
        chkTransferDebit.setText(resourceBundle.getString("chkTransferDebit"));
        lblAmount.setText(resourceBundle.getString("lblAmount"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblNoOfPersons.setText(resourceBundle.getString("lblNoOfPersons"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        chkCashCredit.setText(resourceBundle.getString("chkCashCredit"));
        btnLevelID.setText(resourceBundle.getString("btnLevelID"));
        lblStatusValue.setText(resourceBundle.getString("lblStatusValue"));
        lblLevelName.setText(resourceBundle.getString("lblLevelName"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        btnDone.setText(resourceBundle.getString("btnDone"));
        btnDelete1.setText(resourceBundle.getString("btnDelete1"));
        lblLevelID.setText(resourceBundle.getString("lblLevelID"));
        lblSep3.setText(resourceBundle.getString("lblSep3"));
    }    
    /* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        objMandatoryRB = new  MultiLevelControlMRB();
        txtAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAmount"));
        cboCondition.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCondition"));
        chkCashCredit.setHelpMessage(lblMsg, objMandatoryRB.getString("chkCashCredit"));
        chkCashDebit.setHelpMessage(lblMsg, objMandatoryRB.getString("chkCashDebit"));
        chkTransferCredit.setHelpMessage(lblMsg, objMandatoryRB.getString("chkTransferCredit"));
        chkTransferDebit.setHelpMessage(lblMsg, objMandatoryRB.getString("chkTransferDebit"));
        chkClearingCredit.setHelpMessage(lblMsg, objMandatoryRB.getString("chkClearingCredit"));
        chkClearingDebit.setHelpMessage(lblMsg, objMandatoryRB.getString("chkClearingDebit"));
        txtNoOfPersons.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNoOfPersons"));
        txtLevelID.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLevelID"));
        txtExpression.setHelpMessage(lblMsg, objMandatoryRB.getString("txtExpression"));
    }    
    private void enableDisableButtons(boolean value){
        this.btnLevelID.setEnabled(value);
        this.btnAND.setEnabled(value);
        this.btnOR.setEnabled(value);
        this.btnDone.setEnabled(value);
        this.btnDelete.setEnabled(value);
    }
    private void setupMenuToolBarPanel() {
        // setup toolbar
        if (observable.getOperation() == ClientConstants.ACTIONTYPE_CANCEL ||
                observable.getOperation() == ClientConstants.ACTIONTYPE_FAILED) {
            btnAdd.setEnabled(true);
            btnEdit.setEnabled(true);
            btnDelete1.setEnabled(true);
            btnSave.setEnabled(false);
            btnCancel.setEnabled(false);
            
            btnAuthorize.setEnabled(true);
            btnException.setEnabled(true);
            btnRejection.setEnabled(true);
            
            mitAdd.setEnabled(true);
            mitEdit.setEnabled(true);
            mitDelete.setEnabled(true);
            mitAuthorize.setEnabled(true);
            mitExceptions.setEnabled(true);
            mitRejection.setEnabled(true);
            
        } else if (observable.getOperation() == ClientConstants.ACTIONTYPE_EDIT ||
                    observable.getOperation() == ClientConstants.ACTIONTYPE_DELETE ||
                    observable.getOperation() == ClientConstants.ACTIONTYPE_NEW) {
            btnAdd.setEnabled(false);
            btnEdit.setEnabled(false);
            btnDelete1.setEnabled(false);
            btnSave.setEnabled(true);
            btnCancel.setEnabled(true);
            
            btnAuthorize.setEnabled(false);
            btnException.setEnabled(false);
            btnRejection.setEnabled(false);
            
            mitAdd.setEnabled(false);
            mitEdit.setEnabled(false);
            mitDelete.setEnabled(false);
            mitAuthorize.setEnabled(false);
            mitExceptions.setEnabled(false);
            mitRejection.setEnabled(false);
        }
        this.txtExpression.setEditable(false);
    }
    private void initComponentData() {        
        this.cboCondition.setModel(observable.getCbmCondition());
        this.tblLevel.setModel(observable.getTbmLevel());
    }
    /** Set observable */
    private void setObservable() {
        observable = MultiLevelControlOB.getInstance();
        observable.addObserver(this);
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        cInternalFrame1 = new com.see.truetransact.uicomponent.CInternalFrame();
        tbrTransfer = new com.see.truetransact.uicomponent.CToolBar();
        btnAdd = new com.see.truetransact.uicomponent.CButton();
        lblSpace24 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace25 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete1 = new com.see.truetransact.uicomponent.CButton();
        lblTBSep1 = new javax.swing.JLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace26 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblTBSep2 = new javax.swing.JLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace27 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace28 = new com.see.truetransact.uicomponent.CLabel();
        btnRejection = new com.see.truetransact.uicomponent.CButton();
        lblSep3 = new com.see.truetransact.uicomponent.CLabel();
        btnReport = new com.see.truetransact.uicomponent.CButton();
        lblSpace29 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panMultiLevel = new com.see.truetransact.uicomponent.CPanel();
        lblAmount = new com.see.truetransact.uicomponent.CLabel();
        txtAmount = new com.see.truetransact.uicomponent.CTextField();
        lblCondition = new com.see.truetransact.uicomponent.CLabel();
        cboCondition = new com.see.truetransact.uicomponent.CComboBox();
        lblTransaction = new com.see.truetransact.uicomponent.CLabel();
        panTransaction = new com.see.truetransact.uicomponent.CPanel();
        chkCashCredit = new com.see.truetransact.uicomponent.CCheckBox();
        chkCashDebit = new com.see.truetransact.uicomponent.CCheckBox();
        chkTransferCredit = new com.see.truetransact.uicomponent.CCheckBox();
        chkTransferDebit = new com.see.truetransact.uicomponent.CCheckBox();
        chkClearingCredit = new com.see.truetransact.uicomponent.CCheckBox();
        chkClearingDebit = new com.see.truetransact.uicomponent.CCheckBox();
        panMainLevel = new com.see.truetransact.uicomponent.CPanel();
        panLevel = new com.see.truetransact.uicomponent.CPanel();
        lblLevelID = new com.see.truetransact.uicomponent.CLabel();
        lblNoOfPersons = new com.see.truetransact.uicomponent.CLabel();
        txtNoOfPersons = new com.see.truetransact.uicomponent.CTextField();
        panOperation = new com.see.truetransact.uicomponent.CPanel();
        btnAND = new com.see.truetransact.uicomponent.CButton();
        btnOR = new com.see.truetransact.uicomponent.CButton();
        lblLevelNameValue = new com.see.truetransact.uicomponent.CLabel();
        lblLevelName = new com.see.truetransact.uicomponent.CLabel();
        panLevelID = new com.see.truetransact.uicomponent.CPanel();
        txtLevelID = new com.see.truetransact.uicomponent.CTextField();
        btnLevelID = new com.see.truetransact.uicomponent.CButton();
        panLevelTable = new com.see.truetransact.uicomponent.CPanel();
        srpLevel = new com.see.truetransact.uicomponent.CScrollPane();
        tblLevel = new com.see.truetransact.uicomponent.CTable();
        txtExpression = new com.see.truetransact.uicomponent.CTextField();
        lblExpression = new com.see.truetransact.uicomponent.CLabel();
        panCommand = new com.see.truetransact.uicomponent.CPanel();
        btnDone = new com.see.truetransact.uicomponent.CButton();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblStatusValue = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        mbrTransfer = new javax.swing.JMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitAdd = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptProcess1 = new javax.swing.JSeparator();
        mitAuthorize = new javax.swing.JMenuItem();
        mitRejection = new javax.swing.JMenuItem();
        mitExceptions = new javax.swing.JMenuItem();
        sptProcess2 = new javax.swing.JSeparator();
        mitClose = new javax.swing.JMenuItem();

        cInternalFrame1.setVisible(true);

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);

        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnAdd.setName("btnAdd");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        tbrTransfer.add(btnAdd);

        lblSpace24.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace24.setText("     ");
        lblSpace24.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTransfer.add(lblSpace24);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setName("btnEdit");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrTransfer.add(btnEdit);

        lblSpace25.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace25.setText("     ");
        lblSpace25.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace25.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace25.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTransfer.add(lblSpace25);

        btnDelete1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete1.setName("btnDelete");
        btnDelete1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelete1ActionPerformed(evt);
            }
        });
        tbrTransfer.add(btnDelete1);

        lblTBSep1.setMaximumSize(new java.awt.Dimension(15, 15));
        lblTBSep1.setMinimumSize(new java.awt.Dimension(15, 15));
        lblTBSep1.setPreferredSize(new java.awt.Dimension(15, 15));
        tbrTransfer.add(lblTBSep1);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setName("btnSave");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrTransfer.add(btnSave);

        lblSpace26.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace26.setText("     ");
        lblSpace26.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTransfer.add(lblSpace26);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setName("btnCancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrTransfer.add(btnCancel);

        lblTBSep2.setMaximumSize(new java.awt.Dimension(15, 15));
        lblTBSep2.setMinimumSize(new java.awt.Dimension(15, 15));
        lblTBSep2.setPreferredSize(new java.awt.Dimension(15, 15));
        tbrTransfer.add(lblTBSep2);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setPreferredSize(new java.awt.Dimension(28, 28));
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrTransfer.add(btnAuthorize);

        lblSpace27.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace27.setText("     ");
        lblSpace27.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTransfer.add(lblSpace27);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrTransfer.add(btnException);

        lblSpace28.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace28.setText("     ");
        lblSpace28.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTransfer.add(lblSpace28);

        btnRejection.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnRejection.setMaximumSize(new java.awt.Dimension(28, 28));
        btnRejection.setMinimumSize(new java.awt.Dimension(28, 28));
        btnRejection.setPreferredSize(new java.awt.Dimension(28, 28));
        btnRejection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectionActionPerformed(evt);
            }
        });
        tbrTransfer.add(btnRejection);

        lblSep3.setToolTipText("");
        lblSep3.setMaximumSize(new java.awt.Dimension(15, 15));
        lblSep3.setMinimumSize(new java.awt.Dimension(15, 15));
        lblSep3.setPreferredSize(new java.awt.Dimension(15, 15));
        tbrTransfer.add(lblSep3);

        btnReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnReport.setName("btnReport");
        btnReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReportActionPerformed(evt);
            }
        });
        tbrTransfer.add(btnReport);

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTransfer.add(lblSpace29);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setName("btnClose");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrTransfer.add(btnClose);

        getContentPane().add(tbrTransfer, java.awt.BorderLayout.NORTH);

        panMultiLevel.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panMultiLevel.setMinimumSize(new java.awt.Dimension(550, 400));
        panMultiLevel.setPreferredSize(new java.awt.Dimension(550, 400));
        panMultiLevel.setLayout(new java.awt.GridBagLayout());

        lblAmount.setText("Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMultiLevel.add(lblAmount, gridBagConstraints);

        txtAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAmount.setValidation(new CurrencyValidation());
        txtAmount.setVerifyInputWhenFocusTarget(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMultiLevel.add(txtAmount, gridBagConstraints);

        lblCondition.setText("Condition");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMultiLevel.add(lblCondition, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMultiLevel.add(cboCondition, gridBagConstraints);

        lblTransaction.setText("Transaction");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMultiLevel.add(lblTransaction, gridBagConstraints);

        panTransaction.setLayout(new java.awt.GridBagLayout());

        chkCashCredit.setText("Cash Credit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransaction.add(chkCashCredit, gridBagConstraints);

        chkCashDebit.setText("Cash Debit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransaction.add(chkCashDebit, gridBagConstraints);

        chkTransferCredit.setText("Transfer Credit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransaction.add(chkTransferCredit, gridBagConstraints);

        chkTransferDebit.setText("Transfer Debit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransaction.add(chkTransferDebit, gridBagConstraints);

        chkClearingCredit.setText("Clearing Credit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransaction.add(chkClearingCredit, gridBagConstraints);

        chkClearingDebit.setText("Clearing Debit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransaction.add(chkClearingDebit, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panMultiLevel.add(panTransaction, gridBagConstraints);

        panMainLevel.setBorder(javax.swing.BorderFactory.createTitledBorder("Level"));
        panMainLevel.setLayout(new java.awt.GridBagLayout());

        panLevel.setMinimumSize(new java.awt.Dimension(150, 26));
        panLevel.setPreferredSize(new java.awt.Dimension(150, 26));
        panLevel.setLayout(new java.awt.GridBagLayout());

        lblLevelID.setText("Level ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLevel.add(lblLevelID, gridBagConstraints);

        lblNoOfPersons.setText("No of Persons");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLevel.add(lblNoOfPersons, gridBagConstraints);

        txtNoOfPersons.setText("1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLevel.add(txtNoOfPersons, gridBagConstraints);

        panOperation.setLayout(new java.awt.GridBagLayout());

        btnAND.setText("AND");
        btnAND.setMaximumSize(new java.awt.Dimension(58, 28));
        btnAND.setMinimumSize(new java.awt.Dimension(58, 28));
        btnAND.setPreferredSize(new java.awt.Dimension(58, 28));
        btnAND.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnANDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOperation.add(btnAND, gridBagConstraints);

        btnOR.setText("OR");
        btnOR.setMaximumSize(new java.awt.Dimension(58, 28));
        btnOR.setMinimumSize(new java.awt.Dimension(58, 28));
        btnOR.setPreferredSize(new java.awt.Dimension(58, 28));
        btnOR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnORActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOperation.add(btnOR, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panLevel.add(panOperation, gridBagConstraints);

        lblLevelNameValue.setMaximumSize(new java.awt.Dimension(100, 16));
        lblLevelNameValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblLevelNameValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLevel.add(lblLevelNameValue, gridBagConstraints);

        lblLevelName.setText("Level Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLevel.add(lblLevelName, gridBagConstraints);

        panLevelID.setLayout(new java.awt.GridBagLayout());

        txtLevelID.setEditable(false);
        txtLevelID.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLevelID.add(txtLevelID, gridBagConstraints);

        btnLevelID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnLevelID.setMinimumSize(new java.awt.Dimension(25, 25));
        btnLevelID.setPreferredSize(new java.awt.Dimension(25, 25));
        btnLevelID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLevelIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLevelID.add(btnLevelID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panLevel.add(panLevelID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        panMainLevel.add(panLevel, gridBagConstraints);

        panLevelTable.setMinimumSize(new java.awt.Dimension(200, 22));
        panLevelTable.setPreferredSize(new java.awt.Dimension(200, 403));
        panLevelTable.setLayout(new java.awt.GridBagLayout());

        tblLevel.setModel(new javax.swing.table.DefaultTableModel(
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
        tblLevel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblLevelMouseClicked(evt);
            }
        });
        srpLevel.setViewportView(tblLevel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLevelTable.add(srpLevel, gridBagConstraints);

        txtExpression.setEditable(false);
        txtExpression.setMinimumSize(new java.awt.Dimension(100, 21));
        txtExpression.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtExpressionKeyTyped(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLevelTable.add(txtExpression, gridBagConstraints);

        lblExpression.setText("Expression");
        lblExpression.setMaximumSize(new java.awt.Dimension(100, 16));
        lblExpression.setMinimumSize(null);
        lblExpression.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLevelTable.add(lblExpression, gridBagConstraints);

        panCommand.setLayout(new java.awt.GridBagLayout());

        btnDone.setText("Done");
        btnDone.setMinimumSize(new java.awt.Dimension(70, 26));
        btnDone.setPreferredSize(new java.awt.Dimension(70, 26));
        btnDone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDoneActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCommand.add(btnDone, gridBagConstraints);

        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCommand.add(btnDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        panLevelTable.add(panCommand, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 1.0;
        panMainLevel.add(panLevelTable, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panMultiLevel.add(panMainLevel, gridBagConstraints);

        getContentPane().add(panMultiLevel, java.awt.BorderLayout.CENTER);

        panStatus.setPreferredSize(new java.awt.Dimension(65, 19));
        panStatus.setLayout(new java.awt.GridBagLayout());

        lblStatus.setText("Status :");
        lblStatus.setName("lblStatus");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panStatus.add(lblStatus, gridBagConstraints);

        lblStatusValue.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblStatusValue.setMaximumSize(new java.awt.Dimension(70, 20));
        lblStatusValue.setMinimumSize(new java.awt.Dimension(70, 20));
        lblStatusValue.setName("lblStatusValue");
        lblStatusValue.setPreferredSize(new java.awt.Dimension(70, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblStatusValue, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblMsg, gridBagConstraints);

        getContentPane().add(panStatus, java.awt.BorderLayout.SOUTH);

        mnuProcess.setText("Process");
        mnuProcess.setName("mnuProcess");

        mitAdd.setText("Add");
        mitAdd.setName("mitAdd");
        mitAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitAddActionPerformed(evt);
            }
        });
        mnuProcess.add(mitAdd);

        mitEdit.setText("Edit");
        mitEdit.setName("mitEdit");
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mitDelete.setName("mitDelete");
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);
        mnuProcess.add(sptProcess1);

        mitAuthorize.setText("Authorize");
        mitAuthorize.setName("mitAuthorize");
        mitAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitAuthorizeActionPerformed(evt);
            }
        });
        mnuProcess.add(mitAuthorize);

        mitRejection.setText("Rejection");
        mitRejection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitRejectionActionPerformed(evt);
            }
        });
        mnuProcess.add(mitRejection);

        mitExceptions.setText("Exceptions");
        mitExceptions.setName("mitExceptions");
        mitExceptions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitExceptionsActionPerformed(evt);
            }
        });
        mnuProcess.add(mitExceptions);
        mnuProcess.add(sptProcess2);

        mitClose.setText("Close");
        mitClose.setName("mitClose");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrTransfer.add(mnuProcess);

        setJMenuBar(mbrTransfer);
    }// </editor-fold>//GEN-END:initComponents

    private void txtExpressionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtExpressionKeyTyped
        // TODO add your handling code here:               
        if(evt.getKeyChar()!='(' && evt.getKeyChar()!=')'
            && evt.getKeyChar()!=evt.VK_BACK_SPACE 
            && evt.getKeyChar()!=evt.VK_DELETE )            
            evt.consume();
    }//GEN-LAST:event_txtExpressionKeyTyped
    
    private void tblLevelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblLevelMouseClicked
        // TODO add your handling code here:     
        //if(!isValid) {
        if(observable.getOperation()==ClientConstants.ACTIONTYPE_EDIT || 
            observable.getOperation()==ClientConstants.ACTIONTYPE_NEW) {
            ClientUtil.enableDisable(this.panLevel, true);
            enableDisableButtons(true);
        }        
        observable.populate(this.tblLevel.getSelectedRow());
        this.updateUILevelFields();
        this.lblLevelNameValue.setText(observable.getLevelName(this.txtLevelID.getText()));
        ///}
    }//GEN-LAST:event_tblLevelMouseClicked
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        this.btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void mitExceptionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitExceptionsActionPerformed
        // Add your handling code here:
        this.btnExceptionActionPerformed(evt);
    }//GEN-LAST:event_mitExceptionsActionPerformed
    
    private void mitRejectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitRejectionActionPerformed
        // Add your handling code here:
        this.btnRejectionActionPerformed(evt);
    }//GEN-LAST:event_mitRejectionActionPerformed
    
    private void mitAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitAuthorizeActionPerformed
        // Add your handling code here:
        this.btnAuthorizeActionPerformed(evt);
    }//GEN-LAST:event_mitAuthorizeActionPerformed
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // Add your handling code here:
        this.btnDelete1ActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // Add your handling code here:
        this.btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed
    
    private void mitAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitAddActionPerformed
        // Add your handling code here:
        this.btnAddActionPerformed(evt);
    }//GEN-LAST:event_mitAddActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void btnReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReportActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_btnReportActionPerformed
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // Add your handling code here:
        observable.setOperation(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION); 
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnRejectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectionActionPerformed
        // Add your handling code here:
        observable.setOperation(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED); 
    }//GEN-LAST:event_btnRejectionActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // Add your handling code here:
        observable.setOperation(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);    
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    public void authorizeStatus(String authorizeStatus){
        observable.setStatus();
        lblStatusValue.setText(observable.getLblStatus());
        if (viewType == AUTHORIZE && isFilled) {          
            HashMap singleAuthorizeMap = new HashMap();
            ArrayList arrList = new ArrayList();
            HashMap authDataMap = new HashMap();
            
            authDataMap.put("LEVEL_MULTI_ID",observable.getMultiLevelID());
            authDataMap.put("STATUS", observable.getMultiLevelMasterTOStatus());            
            arrList.add(authDataMap);
            
            singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);          
            authorize(singleAuthorizeMap);
        } else {                      
            HashMap mapParam = new HashMap();
            HashMap whereParam = new HashMap();
            whereParam.put("USER_ID", TrueTransactMain.USER_ID);
            mapParam.put(CommonConstants.MAP_NAME, "selectMultiLevelAuthorize");
            mapParam.put(CommonConstants.MAP_WHERE, whereParam);
            viewType = AUTHORIZE;
            isFilled = false;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
        }
    }
     public void authorize(HashMap map) {
        System.out.println("Authorize Map : " + map);        
        observable.setAuthorizeMap(map);
        observable.doAction();
        if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED) {
            btnCancelActionPerformed(null);
            observable.setResultStatus();
            lblStatusValue.setText(observable.getLblStatus());
        }
    }
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        observable.resetForm();
        //observable.resetLevelFields();
        updateUILevelFields();
        this.resetUI();
        ClientUtil.enableDisable(this,false);
        observable.setOperation(ClientConstants.ACTIONTYPE_CANCEL);
        this.setupMenuToolBarPanel();
        this.enableDisableButtons(false);
        observable.setStatus();
        lblStatusValue.setText(observable.getLblStatus());
    }//GEN-LAST:event_btnCancelActionPerformed
    private boolean checkBoxesSelection() {
        if(!chkCashCredit.isSelected() && 
            !this.chkCashDebit.isSelected() && 
            !this.chkClearingCredit.isSelected() && 
            !this.chkClearingDebit.isSelected() && 
            !this.chkTransferCredit.isSelected() && 
            !this.chkTransferDebit.isSelected() )
            return false;
        return true;
    }
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        boolean isValid = true;
        String errorMsg = "";
        if(tblLevel.getRowCount()>0){        
        //if(isValid || observable.getExpression()!=-1){
        if(observable.getExpression()!=-1){
            //this.txtExpression.setText(observable.getTxtExpression());
            final String mandatoryMessage = checkMandatory(this.panMultiLevel);
            if(mandatoryMessage.length() > 0 ){
                displayAlert(mandatoryMessage);
                return;
            }
            if(!checkBoxesSelection()) {
                errorMsg = resourceBundle.getString("WARNING_ATLEAST_TRANSACTION");
                displayAlert(errorMsg);
                return;
            }
            this.updateOBFields();
            if(!observable.validateExpression()) {
                errorMsg = resourceBundle.getString("WARNING_INVALID_EXP");
                //COptionPane.showMessageDialog(this,"ImproperExpression!!!");
                isValid=false;
             //   this.btnDone.setEnabled(true);
//                return;
            }            
        }else {
            errorMsg = resourceBundle.getString("WARNING_INVALID_EXP");
            //COptionPane.showMessageDialog(this,"ImproperExpression!!!");
            //this.btnDone.setEnabled(true);
            isValid=false;
            //return;
        }
        }else {
            errorMsg = resourceBundle.getString("WARNING_MINIMUM_ENTRY");
            //COptionPane.showMessageDialog(this,"Minimum one level is required!!!");
            //ClientUtil.enableDisable(this,true);
            //this.enableDisableButtons(true);      
            isValid=false;
            //return;
        }
        if(isValid) {
            observable.doAction();
            if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED) {
                this.btnCancelActionPerformed(null);
                observable.setResultStatus();
                lblStatusValue.setText(observable.getLblStatus());
            }        
        }else {
            COptionPane.showMessageDialog(this,errorMsg);
            ClientUtil.enableDisable(this,true);
            this.enableDisableButtons(true);      
            //this.isValid=false;
            return;
        }
    }//GEN-LAST:event_btnSaveActionPerformed
    private String checkMandatory(JComponent component){
        return new MandatoryCheck().checkMandatory(getClass().getName(),component);
    }
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    private void btnDelete1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete1ActionPerformed
        // Add your handling code here:
        observable.setOperation(ClientConstants.ACTIONTYPE_DELETE);
        observable.setStatus();
        lblStatusValue.setText(observable.getLblStatus());
        callView(ClientConstants.ACTIONTYPE_DELETE);
    }//GEN-LAST:event_btnDelete1ActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        observable.setOperation(ClientConstants.ACTIONTYPE_EDIT);
        observable.setStatus();
        lblStatusValue.setText(observable.getLblStatus());
        callView(ClientConstants.ACTIONTYPE_EDIT);
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // Add your handling code here:
        observable.setOperation(ClientConstants.ACTIONTYPE_NEW);
        observable.setStatus();
        lblStatusValue.setText(observable.getLblStatus());
        
        /* now setup the menu, as it should be based on the current operation,
         * which at startup is "NOOP"
         */
        setupMenuToolBarPanel();
        ClientUtil.enableDisable(this,true);
        enableDisableButtons(true);
    }//GEN-LAST:event_btnAddActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:        
        observable.delete(tblLevel.getSelectedRow());
        this.updateTable();
        observable.resetLevelFields();
        this.updateUILevelFields();
        this.txtExpression.setText(observable.getTxtExpression());     
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnDoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDoneActionPerformed
        // Add your handling code here:
        String levelID = this.txtLevelID.getText();
        if(levelID!=null && levelID.length()>0) {
            observable.setCondition("");
            addToTable();
            /*this.updateOBLevelFields();
            observable.setCondition("");
            observable.addLevel(tblLevel.getSelectedRow());
            updateTable();
            observable.resetLevelFields();
            updateUILevelFields();*/
        }        
        if(observable.getExpression()<0){
            COptionPane.showMessageDialog(this,resourceBundle.getString("WARNING_INVALID_EXP"));
            //this.isValid = false;
        }else {
            ClientUtil.enableDisable(this.panLevel, false);
            /*this.btnAND.setEnabled(false);
            this.btnOR.setEnabled(false);
            this.lblLevelID.setEnabled(false);*/
            this.enableDisableButtons(false);
            //this.isValid=true;
            this.txtExpression.setEditable(true);
            this.txtExpression.setEnabled(true);
            this.btnDone.setEnabled(true);
            //this.tblLevel.setRowSelectionAllowed(false);
        }               
        this.txtExpression.setText(observable.getTxtExpression());        
    }//GEN-LAST:event_btnDoneActionPerformed
    
    private void btnLevelIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLevelIDActionPerformed
        // Add your handling code here:
        this.callView(LEVEL_ID);
    }//GEN-LAST:event_btnLevelIDActionPerformed
    
    private void btnORActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnORActionPerformed
        // Add your handling code here:
        String levelID = this.txtLevelID.getText();
        if(levelID!=null && levelID.length()>0) {
            observable.setCondition("||");
            addToTable();
            /*
            this.updateOBLevelFields();
            observable.setCondition("||");
            observable.addLevel(tblLevel.getSelectedRow());
            updateTable();
            observable.resetLevelFields();
            updateUILevelFields();*/
        }
    }//GEN-LAST:event_btnORActionPerformed
    private void addToTable(){
        this.updateOBLevelFields();        
        observable.addLevel(tblLevel.getSelectedRow());
        updateTable();
        observable.resetLevelFields();
        updateUILevelFields();
    }
    private void btnANDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnANDActionPerformed
        // Add your handling code here:
        String levelID = this.txtLevelID.getText();
        if(levelID!=null && levelID.length()>0) {
            observable.setCondition("&&");
            addToTable();
            /*
            this.updateOBLevelFields();
            observable.setCondition("&&");
            observable.addLevel(tblLevel.getSelectedRow());
            updateTable();
            observable.resetLevelFields();
            updateUILevelFields();*/
        }
    }//GEN-LAST:event_btnANDActionPerformed
    
    public void update(Observable o, Object arg) {
        this.txtAmount.setText(observable.getTxtAmount());
        ((ComboBoxModel)this.cboCondition.getModel()).setKeyForSelected(observable.getCboCondition());
        
        this.chkCashCredit.setSelected(observable.getChkCashCredit().equalsIgnoreCase("Y"));
        this.chkCashDebit.setSelected(observable.getChkCashDebit().equalsIgnoreCase("Y"));
        this.chkClearingCredit.setSelected(observable.getChkClearingCredit().equalsIgnoreCase("Y"));
        this.chkClearingDebit.setSelected(observable.getChkClearingDebit().equalsIgnoreCase("Y"));
        this.chkTransferCredit.setSelected(observable.getChkTransCredit().equalsIgnoreCase("Y"));
        this.chkTransferDebit.setSelected(observable.getChkTransDebit().equalsIgnoreCase("Y"));
        
        this.txtExpression.setText(observable.getTxtExpression());
        
        this.updateTable();
    }
    private void updateOBFields(){
        observable.setTxtAmount(this.txtAmount.getText());
        observable.setCboCondition((String)((ComboBoxModel)this.cboCondition.getModel()).getKeyForSelected());
        observable.setChkCashCredit((this.chkCashCredit.isSelected()==true)?"Y":"N");
        observable.setChkCashDebit((this.chkCashDebit.isSelected()==true)?"Y":"N");
        
        observable.setChkTransCredit((this.chkTransferCredit.isSelected()==true)?"Y":"N");
        observable.setChkTransDebit((this.chkTransferDebit.isSelected()==true)?"Y":"N");
        
        observable.setChkClearingCredit((this.chkClearingCredit.isSelected()==true)?"Y":"N");
        observable.setChkClearingDebit((this.chkClearingDebit.isSelected()==true)?"Y":"N");
        
        observable.setTxtExpression(this.txtExpression.getText());
    }
   /* Auto Generated Method - setMandatoryHashMap()
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtAmount", new Boolean(true));
        mandatoryMap.put("cboCondition", new Boolean(true));
        mandatoryMap.put("chkCashCredit", new Boolean(true));
        mandatoryMap.put("chkCashDebit", new Boolean(true));
        mandatoryMap.put("chkTransferCredit", new Boolean(true));
        mandatoryMap.put("chkTransferDebit", new Boolean(true));
        mandatoryMap.put("chkClearingCredit", new Boolean(true));
        mandatoryMap.put("chkClearingDebit", new Boolean(true));
        mandatoryMap.put("txtNoOfPersons", new Boolean(true));
        mandatoryMap.put("txtLevelID", new Boolean(true));
        mandatoryMap.put("txtExpression", new Boolean(true));
    }
    
    private void setMaximumLength(){
        txtAmount.setMaxLength(16);
        txtNoOfPersons.setMaxLength(2);
    }
    
/* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    private void callView(int currField) {
        HashMap viewMap = new HashMap();
        viewType = currField;
        if (currField == LEVEL_ID) {
            this.txtLevelID.setText("");
            viewMap.put(CommonConstants.MAP_NAME, "Multi.viewLevelControl");
            HashMap whereListMap = new HashMap();          
            
            ArrayList presentLevelIDs = new ArrayList();
            int rowCount = this.tblLevel.getRowCount();
            
            if(rowCount!=0) {
                for(int i=0;i<rowCount;i++){
                    presentLevelIDs.add(CommonUtil.convertObjToStr(this.tblLevel.getValueAt(i,1)));
                }
            }            
            whereListMap.put("LEVEL ID",presentLevelIDs);
            viewMap.put(CommonConstants.MAP_WHERE, whereListMap);
        }
        else if(currField == ClientConstants.ACTIONTYPE_EDIT ||
        currField == ClientConstants.ACTIONTYPE_DELETE) {
            viewMap.put(CommonConstants.MAP_NAME, "selectMultiLevel");
        }
        new ViewAll(this, viewMap).show();
    }
    private void updateOBLevelFields() {
        observable.setTxtLevelID(this.txtLevelID.getText());
        observable.setTxtNoOfPersons(this.txtNoOfPersons.getText());
    }
    private void updateUILevelFields() {
        this.txtLevelID.setText(observable.getTxtLevelID());
        this.txtNoOfPersons.setText(observable.getTxtNoOfPersons());
        this.lblLevelNameValue.setText("");
    }
    private void updateTable() {
        this.tblLevel.setModel(observable.getTbmLevel());
        this.tblLevel.revalidate();
    }
    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;        
        if (viewType==LEVEL_ID) {
            this.txtLevelID.setText((String)hash.get("LEVEL_ID"));
            this.lblLevelNameValue.setText((String)hash.get("LEVEL NAME"));
        }else if(viewType == ClientConstants.ACTIONTYPE_EDIT || 
                    viewType == ClientConstants.ACTIONTYPE_DELETE || 
                    viewType == this.AUTHORIZE) {
            observable.populateData((String)hash.get("LEVEL_MULTI_ID"));
            if (viewType == ClientConstants.ACTIONTYPE_EDIT) {
                ClientUtil.enableDisable(this, true);
                //ClientUtil.enableDisable(this.panLevel, false);
                enableDisableButtons(true);
            }
            setupMenuToolBarPanel();
            if(viewType == this.AUTHORIZE){
                this.isFilled = true;
                setAuthorizeButtons();
            }
        }
    }
    private void setAuthorizeButtons(){
        this.btnDelete1.setEnabled(false);
        this.btnAdd.setEnabled(false);
        this.btnEdit.setEnabled(false);
        this.btnSave.setEnabled(false);
        this.btnCancel.setEnabled(true);
    }
    public void resetUI(){
        this.viewType = -1;
       // this.isValid = false;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAND;
    private com.see.truetransact.uicomponent.CButton btnAdd;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDelete1;
    private com.see.truetransact.uicomponent.CButton btnDone;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnLevelID;
    private com.see.truetransact.uicomponent.CButton btnOR;
    private com.see.truetransact.uicomponent.CButton btnRejection;
    private com.see.truetransact.uicomponent.CButton btnReport;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CInternalFrame cInternalFrame1;
    private com.see.truetransact.uicomponent.CComboBox cboCondition;
    private com.see.truetransact.uicomponent.CCheckBox chkCashCredit;
    private com.see.truetransact.uicomponent.CCheckBox chkCashDebit;
    private com.see.truetransact.uicomponent.CCheckBox chkClearingCredit;
    private com.see.truetransact.uicomponent.CCheckBox chkClearingDebit;
    private com.see.truetransact.uicomponent.CCheckBox chkTransferCredit;
    private com.see.truetransact.uicomponent.CCheckBox chkTransferDebit;
    private com.see.truetransact.uicomponent.CLabel lblAmount;
    private com.see.truetransact.uicomponent.CLabel lblCondition;
    private com.see.truetransact.uicomponent.CLabel lblExpression;
    private com.see.truetransact.uicomponent.CLabel lblLevelID;
    private com.see.truetransact.uicomponent.CLabel lblLevelName;
    private com.see.truetransact.uicomponent.CLabel lblLevelNameValue;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNoOfPersons;
    private com.see.truetransact.uicomponent.CLabel lblSep3;
    private com.see.truetransact.uicomponent.CLabel lblSpace24;
    private com.see.truetransact.uicomponent.CLabel lblSpace25;
    private com.see.truetransact.uicomponent.CLabel lblSpace26;
    private com.see.truetransact.uicomponent.CLabel lblSpace27;
    private com.see.truetransact.uicomponent.CLabel lblSpace28;
    private com.see.truetransact.uicomponent.CLabel lblSpace29;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblStatusValue;
    private javax.swing.JLabel lblTBSep1;
    private javax.swing.JLabel lblTBSep2;
    private com.see.truetransact.uicomponent.CLabel lblTransaction;
    private javax.swing.JMenuBar mbrTransfer;
    private javax.swing.JMenuItem mitAdd;
    private javax.swing.JMenuItem mitAuthorize;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitExceptions;
    private javax.swing.JMenuItem mitRejection;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panCommand;
    private com.see.truetransact.uicomponent.CPanel panLevel;
    private com.see.truetransact.uicomponent.CPanel panLevelID;
    private com.see.truetransact.uicomponent.CPanel panLevelTable;
    private com.see.truetransact.uicomponent.CPanel panMainLevel;
    private com.see.truetransact.uicomponent.CPanel panMultiLevel;
    private com.see.truetransact.uicomponent.CPanel panOperation;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTransaction;
    private javax.swing.JSeparator sptProcess1;
    private javax.swing.JSeparator sptProcess2;
    private com.see.truetransact.uicomponent.CScrollPane srpLevel;
    private com.see.truetransact.uicomponent.CTable tblLevel;
    private com.see.truetransact.uicomponent.CToolBar tbrTransfer;
    private com.see.truetransact.uicomponent.CTextField txtAmount;
    private com.see.truetransact.uicomponent.CTextField txtExpression;
    private com.see.truetransact.uicomponent.CTextField txtLevelID;
    private com.see.truetransact.uicomponent.CTextField txtNoOfPersons;
    // End of variables declaration//GEN-END:variables
    public static void main(String[] arg){
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Throwable th) {
            th.printStackTrace();
        }
        JFrame jf = new JFrame();
        MultiLevelControlUI gui = new MultiLevelControlUI();
        jf.getContentPane().add(gui);
        jf.setSize(550, 500);
        jf.show();
        gui.show();
    }
}
