/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * PFTransferUI.java
 *
 * 
 */
package com.see.truetransact.ui.payroll.pftransfer;

import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.ui.common.transaction.TransactionUI;
import com.see.truetransact.ui.common.viewall.AuthorizeListUI;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uicomponent.CButtonGroup;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.CurrencyValidation;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;
/**
 *
 * @author anjuanand
 */
public class PFTransferUI extends CInternalFrame implements java.util.Observer, UIMandatoryField {

    HashMap mandatoryMap;
    PFTransferOB observable;
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.payroll.pftransfer.PFTransferRB", ProxyParameters.LANGUAGE);
    final int EDIT = 0, DELETE = 1, AUTHORIZE = 2, PFACCOUNT = 3;
    int viewType = -1;
    boolean isFilled = false;
    private TransactionUI transactionUI = new TransactionUI();
    double transamt = 0.0;
    boolean fromAuthorizeUI = false;
    AuthorizeListUI authorizeListUI = null;
    private Date currDt = null;
    /**
     * Creates new form PFTransferUI
     */
    public PFTransferUI() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        initSetup();
        setValidation();
        addRadioButtons();
        setRadioButtons();
        transactionUI.setSourceScreen("ACT_CLOSING");
        transactionUI.addToScreen(panTransaction);
        transactionUI.setTransactionMode(CommonConstants.DEBIT);
        observable.setTransactionOB(transactionUI.getTransactionOB());
        txtAmount.setAllowAll(true);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnException.setEnabled(false);
        btnPrint.setEnabled(false);
    }

    private void setValidation() {
        txtBalance.setValidation(new CurrencyValidation(14, 2));
        txtAmount.setValidation(new CurrencyValidation(14, 2));
    }

    private void initSetup() {
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setHelpMessage();
        setObservable();
        ClientUtil.enableDisable(this, false); //__ Disables all when the screen appears for the 1st time
        setButtonEnableDisable();              //__ Enables/Disables the necessary buttons and menu items...
        setEnableDisableButtons(false);
        observable.resetForm();                //__ Resets the Data in the Form...
        observable.resetStatus();              //__ to reset the status...
    }

    private void setObservable() {
        observable = new PFTransferOB();
        observable.addObserver(this);
        observable.setTransactionOB(transactionUI.getTransactionOB());
    }

    /*
     * Auto Generated Method - setFieldNames() This method assigns name for all
     * the components. Other functions are working based on this name.
     */
    private void setFieldNames() {
        btnSearchPfNo.setName("btnSearchPfNo");
        btnAuthorize.setName("btnAuthorize");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        lblPfNo.setName("lblPfNo");
        lblBalance.setName("lblBalance");
        lblMsg.setName("lblMsg");
        lblAmount.setName("lblAmount");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblStatus1.setName("lblStatus1");
        mbrPfTransfer.setName("mbrPfTransfer");
        panPfTransfer.setName("panPfTransfer");
        panStatus.setName("panStatus");
        txtPfNo.setName("txtPfNo");
        txtBalance.setName("txtBalance");
        txtAmount.setName("txtAmount");
        rdoTransactionType_Credit.setName("rdoTransactionType_Credit");
        rdoTransactionType_Debit.setName("rdoTransactionType_Debit");
        lblTransactionType.setName("lblTransactionType");
        panTransactionType.setName("panTransactionType");
    }

    /*
     * Auto Generated Method - internationalize() This method used to assign
     * display texts from the Resource Bundle File.
     */
    private void internationalize() {
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblPfNo.setText(resourceBundle.getString("lblPfNo"));
        lblBalance.setText(resourceBundle.getString("lblBalance"));
        lblStatus1.setText(resourceBundle.getString("lblStatus1"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        lblAmount.setText(resourceBundle.getString("lblAmount"));
        txtPfNo.setText(resourceBundle.getString("txtPfNo"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        txtBalance.setText(resourceBundle.getString("txtBalance"));
        txtAmount.setText(resourceBundle.getString("txtAmount"));
        btnSearchPfNo.setText(resourceBundle.getString("btnSearchPfNo"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        rdoTransactionType_Debit.setText(resourceBundle.getString("rdoTransactionType_Debit"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        rdoTransactionType_Credit.setText(resourceBundle.getString("rdoTransactionType_Credit"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblTransactionType.setText(resourceBundle.getString("lblTransactionType"));
    }

    /*
     * Auto Generated Method - update() This method called by Observable. It
     * updates the UI with Observable's data. If needed add/Remove RadioButtons
     * method need to be added.
     */
    public void update(Observable observed, Object arg) {
        txtPfNo.setText(observable.getTxtPfNo());
        txtAmount.setText(CommonUtil.convertObjToStr(observable.getTxtAmount()));
        txtBalance.setText(CommonUtil.convertObjToStr(observable.getTxtBalance()));
        this.rdoTransactionType_Debit.setSelected(observable.isRdoTransactionType_Debit());
        this.rdoTransactionType_Credit.setSelected(observable.isRdoTransactionType_Credit());
        this.addRadioButtons();
        //To set the Status...
        lblStatus1.setText(observable.getLblStatus());

    }

    private void removeRadioButtons() {
        //---Account---
        rdoTransactionType.remove(rdoTransactionType_Credit);
        rdoTransactionType.remove(rdoTransactionType_Debit);
    }

    private void addRadioButtons() {
        //---Account---
        rdoTransactionType = new CButtonGroup();
        rdoTransactionType.add(rdoTransactionType_Credit);
        rdoTransactionType.add(rdoTransactionType_Debit);
    }

    private void setRadioButtons() {
        this.removeRadioButtons();
        this.rdoTransactionType_Credit.setEnabled(true);
        this.rdoTransactionType_Debit.setEnabled(true);
        this.rdoTransactionType_Debit.setSelected(false);
        this.rdoTransactionType_Credit.setSelected(false);
        this.addRadioButtons();
    }

    /*
     * Auto Generated Method - updateOBFields() This method called by Save
     * option of UI. It updates the OB with UI data.
     */
    public void updateOBFields() {
        observable.setScreen(getScreen());
        observable.setModule(getModule());
        observable.setTxtAmount(CommonUtil.convertObjToDouble(txtAmount.getText()));
        observable.setTxtBalance(CommonUtil.convertObjToDouble(txtBalance.getText()));
        observable.setTxtPfNo(txtPfNo.getText());
        observable.setRdoTransactionType_Debit(rdoTransactionType_Debit.isSelected());
        observable.setRdoTransactionType_Credit(rdoTransactionType_Credit.isSelected());
    }

    /*
     * Auto Generated Method - setMandatoryHashMap()
     *
     * ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
     *
     * This method list out all the Input Fields available in the UI. It needs a
     * class level HashMap variable mandatoryMap.
     */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtPfNo", new Boolean(true));
        mandatoryMap.put("txtBalance", new Boolean(true));
        mandatoryMap.put("txtAmount", new Boolean(true));
        mandatoryMap.put("rdoReceipt", new Boolean(true));
        mandatoryMap.put("rdoPayment", new Boolean(true));

    }

    /*
     * Auto Generated Method - getMandatoryHashMap() Getter method for
     * setMandatoryHashMap().
     */
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    /*
     * Auto Generated Method - setHelpMessage() This method shows tooltip help
     * for all the input fields available in the UI. It needs the Mandatory
     * Resource Bundle object. Help display Label name should be lblMsg.
     */
    public void setHelpMessage() {
        PFTransferMRB objMandatoryRB = new PFTransferMRB();
        txtPfNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPfNo"));
        txtBalance.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBalance"));
        txtAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAmount"));
        rdoTransactionType_Debit.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoTransactionType_Debit"));
        rdoTransactionType_Credit.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoTransactionType_Credit"));
    }

    // To set The Value of the Buttons Depending on the Value or Condition...
    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        btnDelete.setEnabled(!btnDelete.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());
        btnSave.setEnabled(!btnSave.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
//        rdoReceipt.setEnabled(rdoReceipt.isEnabled());
//        rdoPayment.setEnabled(rdoReceipt.isEnabled());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoTransactionType = new com.see.truetransact.uicomponent.CButtonGroup();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus1 = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        tbrLoantProduct = new javax.swing.JToolBar();
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
        panPfClose = new com.see.truetransact.uicomponent.CPanel();
        panPfTransfer = new com.see.truetransact.uicomponent.CPanel();
        lblAmount = new com.see.truetransact.uicomponent.CLabel();
        lblBalance = new com.see.truetransact.uicomponent.CLabel();
        txtBalance = new com.see.truetransact.uicomponent.CTextField();
        panShareNo = new com.see.truetransact.uicomponent.CPanel();
        txtPfNo = new com.see.truetransact.uicomponent.CTextField();
        btnSearchPfNo = new com.see.truetransact.uicomponent.CButton();
        lblPfNo = new com.see.truetransact.uicomponent.CLabel();
        lblBatchIdValue = new com.see.truetransact.uicomponent.CLabel();
        txtAmount = new com.see.truetransact.uicomponent.CTextField();
        lblTransactionType = new com.see.truetransact.uicomponent.CLabel();
        panTransactionType = new com.see.truetransact.uicomponent.CPanel();
        rdoTransactionType_Debit = new com.see.truetransact.uicomponent.CRadioButton();
        rdoTransactionType_Credit = new com.see.truetransact.uicomponent.CRadioButton();
        panShareCloseTrans = new com.see.truetransact.uicomponent.CPanel();
        panTransaction = new com.see.truetransact.uicomponent.CPanel();
        mbrPfTransfer = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptProcess = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitPrint = new javax.swing.JMenuItem();
        sptPrint = new javax.swing.JSeparator();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMaximumSize(new java.awt.Dimension(825, 575));
        setMinimumSize(new java.awt.Dimension(825, 575));
        setPreferredSize(new java.awt.Dimension(825, 575));

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace1.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace1, gridBagConstraints);

        lblStatus1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblStatus1.setText("                      ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblStatus1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblMsg, gridBagConstraints);

        getContentPane().add(panStatus, java.awt.BorderLayout.SOUTH);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnNew);

        lblSpace70.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace70.setText("     ");
        lblSpace70.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace70.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace70.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace70);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnEdit);

        lblSpace71.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace71.setText("     ");
        lblSpace71.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace71.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace71.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace71);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnDelete);

        lblSpace2.setText("     ");
        tbrLoantProduct.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnSave);

        lblSpace72.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace72.setText("     ");
        lblSpace72.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace72.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace72.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace72);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnCancel);

        lblSpace3.setText("     ");
        tbrLoantProduct.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnAuthorize);

        lblSpace73.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace73.setText("     ");
        lblSpace73.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace73);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnException);

        lblSpace74.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace74.setText("     ");
        lblSpace74.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace74);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnReject);

        lblSpace4.setText("     ");
        tbrLoantProduct.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnPrint);

        lblSpace75.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace75.setText("     ");
        lblSpace75.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace75);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnClose);

        getContentPane().add(tbrLoantProduct, java.awt.BorderLayout.NORTH);

        panPfClose.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panPfClose.setMaximumSize(new java.awt.Dimension(815, 350));
        panPfClose.setMinimumSize(new java.awt.Dimension(815, 350));
        panPfClose.setPreferredSize(new java.awt.Dimension(815, 350));
        panPfClose.setLayout(new java.awt.GridBagLayout());

        panPfTransfer.setMaximumSize(new java.awt.Dimension(320, 207));
        panPfTransfer.setMinimumSize(new java.awt.Dimension(320, 207));
        panPfTransfer.setPreferredSize(new java.awt.Dimension(320, 207));
        panPfTransfer.setLayout(new java.awt.GridBagLayout());

        lblAmount.setText("Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panPfTransfer.add(lblAmount, gridBagConstraints);

        lblBalance.setText("Balance");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panPfTransfer.add(lblBalance, gridBagConstraints);

        txtBalance.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 5, 4, 10);
        panPfTransfer.add(txtBalance, gridBagConstraints);

        panShareNo.setLayout(new java.awt.GridBagLayout());

        txtPfNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panShareNo.add(txtPfNo, gridBagConstraints);

        btnSearchPfNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnSearchPfNo.setToolTipText("Share No");
        btnSearchPfNo.setEnabled(false);
        btnSearchPfNo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnSearchPfNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnSearchPfNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchPfNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panShareNo.add(btnSearchPfNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPfTransfer.add(panShareNo, gridBagConstraints);

        lblPfNo.setText("PF No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 0);
        panPfTransfer.add(lblPfNo, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPfTransfer.add(lblBatchIdValue, gridBagConstraints);

        txtAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAmountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 9);
        panPfTransfer.add(txtAmount, gridBagConstraints);

        lblTransactionType.setText("Transaction Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        panPfTransfer.add(lblTransactionType, gridBagConstraints);

        panTransactionType.setMinimumSize(new java.awt.Dimension(160, 23));
        panTransactionType.setPreferredSize(new java.awt.Dimension(150, 23));
        panTransactionType.setLayout(new java.awt.GridBagLayout());

        rdoTransactionType.add(rdoTransactionType_Debit);
        rdoTransactionType_Debit.setText("Payment");
        rdoTransactionType_Debit.setMinimumSize(new java.awt.Dimension(85, 27));
        rdoTransactionType_Debit.setPreferredSize(new java.awt.Dimension(85, 27));
        rdoTransactionType_Debit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoTransactionType_DebitActionPerformed(evt);
            }
        });
        panTransactionType.add(rdoTransactionType_Debit, new java.awt.GridBagConstraints());

        rdoTransactionType.add(rdoTransactionType_Credit);
        rdoTransactionType_Credit.setText("Receipt");
        rdoTransactionType_Credit.setMargin(new java.awt.Insets(2, 5, 2, 2));
        rdoTransactionType_Credit.setMaximumSize(new java.awt.Dimension(69, 27));
        rdoTransactionType_Credit.setMinimumSize(new java.awt.Dimension(85, 27));
        rdoTransactionType_Credit.setPreferredSize(new java.awt.Dimension(85, 27));
        panTransactionType.add(rdoTransactionType_Credit, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 59;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 3, 4, 4);
        panPfTransfer.add(panTransactionType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 96;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(25, 20, 0, 0);
        panPfClose.add(panPfTransfer, gridBagConstraints);

        panShareCloseTrans.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panPfClose.add(panShareCloseTrans, gridBagConstraints);

        panTransaction.setMaximumSize(new java.awt.Dimension(785, 250));
        panTransaction.setMinimumSize(new java.awt.Dimension(785, 250));
        panTransaction.setPreferredSize(new java.awt.Dimension(785, 250));
        panTransaction.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 16, 20, 15);
        panPfClose.add(panTransaction, gridBagConstraints);

        getContentPane().add(panPfClose, java.awt.BorderLayout.CENTER);

        mnuProcess.setText("Process");

        mitNew.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        mitNew.setMnemonic('N');
        mitNew.setText("New");
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

        mitEdit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        mitEdit.setMnemonic('E');
        mitEdit.setText("Edit");
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        mitDelete.setMnemonic('D');
        mitDelete.setText("Delete");
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);
        mnuProcess.add(sptProcess);

        mitSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        mitSave.setMnemonic('S');
        mitSave.setText("Save");
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setMnemonic('C');
        mitCancel.setText("Cancel");
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess.add(mitCancel);
        mnuProcess.add(sptCancel);

        mitPrint.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        mitPrint.setMnemonic('P');
        mitPrint.setText("Print");
        mitPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitPrintActionPerformed(evt);
            }
        });
        mnuProcess.add(mitPrint);
        mnuProcess.add(sptPrint);

        mitClose.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        mitClose.setMnemonic('l');
        mitClose.setText("Close");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrPfTransfer.add(mnuProcess);

        setJMenuBar(mbrPfTransfer);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
        btnReject.setEnabled(true);
        setRadioButtons();
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
        //setRadioButtons();
    }//GEN-LAST:event_btnAuthorizeActionPerformed

    public void authorizeStatus(String authorizeStatus) {
        if (viewType == AUTHORIZE && isFilled) {
            try {
                HashMap singleAuthorizeMap = new HashMap();
                ArrayList arrList = new ArrayList();
                HashMap authorizeMap = new HashMap();
                getPfDetails();
                singleAuthorizeMap.put("EMP_ID", observable.getEmpId());
                singleAuthorizeMap.put("PF_NO", txtPfNo.getText());
                singleAuthorizeMap.put("STATUS", authorizeStatus);
                singleAuthorizeMap.put("AUTHORIZED_BY", TrueTransactMain.USER_ID);
                singleAuthorizeMap.put("TRAN_DT", currDt.clone());
                singleAuthorizeMap.put("AMOUNT", txtAmount.getText());
                singleAuthorizeMap.put("AUTHORIZE_STATUS", authorizeStatus);
                singleAuthorizeMap.put("TRANS_TYPE", observable.getTransType());
                singleAuthorizeMap.put("TRANS_MODE", observable.getTransMode());
                singleAuthorizeMap.put("PF_TRANS_TYPE", observable.getPfTransType());
                singleAuthorizeMap.put("BALANCE", txtBalance.getText());
                arrList.add(singleAuthorizeMap);
                authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
                authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
                observable.setAuthorizeMap(authorizeMap);
                observable.setAuthMap(authorizeMap);
                observable.doAction();
                btnCancelActionPerformed(null);
            } catch (Exception ex) {
                Logger.getLogger(PFTransferUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            HashMap mapParam = new HashMap();
            //__ To Save the data in the Internal Frame...
            setModified(true);
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            whereMap.put(CommonConstants.STATUS, authorizeStatus);
            whereMap.put("TRAN_DT", currDt.clone());
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            mapParam.put(CommonConstants.MAP_NAME, "getPFAuthorizeList");
            viewType = AUTHORIZE;
            isFilled = false;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);

            //__ If there's no data to be Authorized, call Cancel action...
            if (!isModified()) {
                setButtonEnableDisable();
                btnCancelActionPerformed(null);
            }
        }
    }

    public void authorize(HashMap map) {
        observable.setAuthorizeMap(map);
        observable.doAction();

        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            isFilled = false;
            btnSave.setEnabled(true);
            btnCancelActionPerformed(null);
            observable.setResultStatus();
        }
        observable.setAuthorizeMap(null);
    }
    //__ To avoid the looping of the testCase...
    boolean isTest = false;

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
        cashTransfer = false;
    }
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
        HashMap reportParamMap = new HashMap();
        com.see.truetransact.clientutil.ttrintegration.LinkReport.getReports(getScreenID(), reportParamMap);
    }//GEN-LAST:event_btnPrintActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        observable.resetForm();                 //__ Reset the fields in the UI to null...
        btnSave.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnException.setEnabled(false);
        btnPrint.setEnabled(false);
        resetLables();
        ClientUtil.enableDisable(this, false);  //__ Disables the panel...
        setButtonEnableDisable();               //__ Enables or Disables the buttons and menu Items depending on their previous state...
        setEnableDisableButtons(false);
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_CANCEL);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);  //__ Sets the Action Type to be performed...
        observable.setStatus();
        txtPfNo.setEnabled(false);
        txtBalance.setEnabled(false);
        txtAmount.setEnabled(false);
        //__ To set the Value of lblStatus..

        transactionUI.setCallingAmount("0.0");
        viewType = -1;
        resetTransactionUI();
        //__ Make the Screen Closable..
        setModified(false);
        setRadioButtons();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void resetTransactionUI() {
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
    }
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        final String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panPfClose);
        if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0) {
            displayAlert(mandatoryMessage);
        } else {
            updateOBFields();
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                int transactionSize = 0;
                if (transactionUI.getOutputTO().size() == 0 && CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue() > 0) {
                    ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS));
                    return;
                } else {
                    if (CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue() > 0) {
                        transamt = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
                        transactionSize = (transactionUI.getOutputTO()).size();
                        if (transactionSize != 1 && CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue() > 0) {
                            ClientUtil.showAlertWindow("Multiple Transactions are Not allowed, Make it one Transaction");
                            return;
                        } else {
                            observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                        }
                    } else if (transactionUI.getOutputTO().size() > 0) {
                        observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                    }
                }
                if (transactionSize == 0 && CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue() > 0) {
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
        }
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
    }//GEN-LAST:event_btnSaveActionPerformed

    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE); //__ Sets the Action Type to be performed...
        popUp(DELETE);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        lblStatus1.setText("Delete");
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT); //__ Sets the Action Type to be performed...
        popUp(EDIT);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        lblStatus1.setText("Edit");
    }//GEN-LAST:event_btnEditActionPerformed

    private void popUp(int field) {
        final HashMap viewMap = new HashMap();
        viewType = field;
        if (field == EDIT || field == DELETE || field == AUTHORIZE) {
            HashMap whereMap = new HashMap();
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            viewMap.put(CommonConstants.MAP_NAME, "viewPFData");
            whereMap = null;
            new ViewAll(this, viewMap).show();
        } else if (field == PFACCOUNT) {
            HashMap whereMap = new HashMap();
            whereMap.put("PF_ACT_NO", txtPfNo.getText());
            whereMap.put("OPEN_BAL", txtBalance.getText());
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            viewMap.put(CommonConstants.MAP_NAME, "getPFTransData");
            whereMap = null;
            new ViewAll(this, viewMap, true).show();
        }
    }

    // this method is called automatically from ViewAll...
    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
        if (viewType == EDIT || viewType == DELETE || viewType == AUTHORIZE) {
            try {
                HashMap where = new HashMap();
                where.put("PfNo", hash.get("PF_NO"));
                where.put("TRAN_DT", hash.get("TRAN_DT"));
                where.put("TRANS_TYPE", hash.get("TRANS_TYPE"));
                where.put("PF_TRANS_TYPE", hash.get("PF_TRANS_TYPE"));
                hash.put(CommonConstants.MAP_WHERE, where);
                observable.populateData(hash);     //__ Called to display the Data in the UI fields...
                //__ If the Action type is Delete...
                fromAuthorizeUI = true;
                authorizeListUI = (AuthorizeListUI) hash.get("PARENT");
                hash.remove("PARENT");
                viewType = AUTHORIZE;
                observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
                observable.setStatus();
                btnReject.setEnabled(false);
                //   rejectFlag = 1;
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || viewType == AUTHORIZE) {
                    ClientUtil.enableDisable(this, false);     //__ Disables the panel...
                    setEnableDisableButtons(false);
                    //__ If the Action Type is Edit...
                } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                    ClientUtil.enableDisable(this, false);     //__ Ennable the panel...
                    setEnableDisableButtons(false);
                }
                observable.setStatus();             //__ To set the Value of lblStatus...
                setButtonEnableDisable();           //__ Enables or Disables the buttons and menu Items depending on their previous state...
                if (viewType == AUTHORIZE) {
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                }
                this.isFilled = true;
            } catch (Exception ex) {
                Logger.getLogger(PFTransferUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (viewType == PFACCOUNT) {
            String pfActNo = CommonUtil.convertObjToStr(hash.get("PF_ACT_NO"));
            HashMap dataMap = new HashMap();
            dataMap.put("PFNO", pfActNo);
            List pfList = ClientUtil.executeQuery("getPFAuthDetails", dataMap);
            if (pfList != null && pfList.size() > 0) {
                ClientUtil.showMessageWindow("Authoriztaion Pending for this PF Account Number!!!");
                txtPfNo.setText("");
                return;
            } else {
                observable.setTxtPfNo(pfActNo);
                observable.setTxtBalance(CommonUtil.convertObjToDouble(hash.get("TOTAL")));
                txtPfNo.setText(CommonUtil.convertObjToStr(hash.get("PF_ACT_NO")));
                txtBalance.setText(CommonUtil.convertObjToStr(hash.get("TOTAL")));
                txtPfNo.setEditable(false);
                txtBalance.setEditable(false);
                btnSearchPfNo.setEnabled(false);
            }
        }
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        observable.resetForm();               // to Reset all the Fields and Status in UI...
        resetLables();
        setButtonEnableDisable();             // Enables/Disables the necessary buttons and menu items...
        setEnableDisableButtons(true);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        ClientUtil.enableDisable(this, true); // Enables the panel...
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);   //Sets the Action Type to be performed...
        observable.setStatus();               // To set the Value of lblStatus...
        btnSearchPfNo.setEnabled(true);
        btnSave.setEnabled(true);
        transactionUI.cancelAction(false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.resetObjects();
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
        rdoTransactionType_Debit.setEnabled(true);
        rdoTransactionType_Credit.setEnabled(true);
        addRadioButtons();
        setRadioButtons();
        txtPfNo.setEnabled(false);
        txtBalance.setEnabled(false);
        txtAmount.setAllowAll(true);
        lblStatus1.setText("New");
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }//GEN-LAST:event_btnNewActionPerformed
    private void setEnableDisableButtons(boolean value) {
        btnSearchPfNo.setEnabled(value);
        rdoTransactionType_Debit.setEnabled(value);
        rdoTransactionType_Credit.setEnabled(value);
    }

    private void resetLables() {
        txtPfNo.setText("");
        txtBalance.setText("");
        txtAmount.setText("");
    }

    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // TODO add your handling code here:
        btnCloseActionPerformed(null);
    }//GEN-LAST:event_mitCloseActionPerformed

    private void mitPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitPrintActionPerformed
        // TODO add your handling code here:
        btnPrintActionPerformed(null);
    }//GEN-LAST:event_mitPrintActionPerformed

    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // TODO add your handling code here:
        btnCancelActionPerformed(null);
    }//GEN-LAST:event_mitCancelActionPerformed

    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // TODO add your handling code here:
        btnSaveActionPerformed(null);
    }//GEN-LAST:event_mitSaveActionPerformed

    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // TODO add your handling code here:
        btnDeleteActionPerformed(null);
    }//GEN-LAST:event_mitDeleteActionPerformed

    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // TODO add your handling code here:
        btnEditActionPerformed(null);
    }//GEN-LAST:event_mitEditActionPerformed

    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // TODO add your handling code here:
        btnEditActionPerformed(null);
    }//GEN-LAST:event_mitNewActionPerformed
    /**
     * Exit the Application
     */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm

private void btnSearchPfNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchPfNoActionPerformed
    // TODO add your handling code here:
    popUp(PFACCOUNT);
}//GEN-LAST:event_btnSearchPfNoActionPerformed

private void rdoTransactionType_DebitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoTransactionType_DebitActionPerformed
    Double amount = CommonUtil.convertObjToDouble(txtAmount.getText());
    Double balance = CommonUtil.convertObjToDouble(txtBalance.getText());
    if (amount > balance) {
        ClientUtil.showAlertWindow("Amount exceeds balance..!!!");
        txtAmount.setText("");
    }
}//GEN-LAST:event_rdoTransactionType_DebitActionPerformed

private void txtAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAmountFocusLost
    // TODO add your handling code here:
    if (rdoTransactionType_Debit.isSelected()) {
        Double amount = CommonUtil.convertObjToDouble(txtAmount.getText());
        Double balance = CommonUtil.convertObjToDouble(txtBalance.getText());
        if (amount > balance) {
            ClientUtil.showAlertWindow("Amount exceeds balance..!!!");
            txtAmount.setText("");
        }
    }
    transactionUI.setCallingAmount(txtAmount.getText());
}//GEN-LAST:event_txtAmountFocusLost

    private void savePerformed() {
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            if (txtPfNo.getText().equals("") && txtBalance.getText().equals("") && txtAmount.getText().equals("") && (!(rdoTransactionType_Credit.isSelected() || rdoTransactionType_Debit.isSelected()))) {
                ClientUtil.showMessageWindow("Please enter all the necessary details!!!");
            } else {

                getPfDetails();
                observable.doAction();
                if (observable.getProxyReturnMap() != null) {
                    if (observable.getProxyReturnMap().containsKey("CASH_TRANS_LIST") || observable.getProxyReturnMap().containsKey("TRANSFER_TRANS_LIST")) {
                        displayTransDetail(observable.getProxyReturnMap());
                    }
                }
                lblStatus1.setText("Success");
            }
        }
        resetTransactionUI();
        setbtndisabled();
        setRadioButtons();
        clear();
    }

    private void getPfDetails() {
        try {
            HashMap pfMap = new HashMap();
            pfMap.put("PF_ACT_NO", txtPfNo.getText());
            HashMap resultMap = new HashMap();
            resultMap = observable.getPfDetails(pfMap);
            String pfId = "";
            String empId = "";
            pfId = CommonUtil.convertObjToStr(resultMap.get("PF_ID"));
            empId = CommonUtil.convertObjToStr(resultMap.get("EMP_ID"));
            observable.setPfId(pfId);
            observable.setEmpId(empId);
        } catch (SQLException ex) {
            Logger.getLogger(PFTransferUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setbtndisabled() {
        txtPfNo.setEnabled(false);
        txtBalance.setEnabled(false);
        txtAmount.setEnabled(false);
        rdoTransactionType_Debit.setSelected(false);
        rdoTransactionType_Credit.setSelected(false);
    }

    private void clear() {
        txtPfNo.setText("");
        txtBalance.setText("");
        txtAmount.setText("");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new PFTransferUI().show();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
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
    private com.see.truetransact.uicomponent.CButton btnSearchPfNo;
    private com.see.truetransact.uicomponent.CLabel lblAmount;
    private com.see.truetransact.uicomponent.CLabel lblBalance;
    private com.see.truetransact.uicomponent.CLabel lblBatchIdValue;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblPfNo;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace70;
    private com.see.truetransact.uicomponent.CLabel lblSpace71;
    private com.see.truetransact.uicomponent.CLabel lblSpace72;
    private com.see.truetransact.uicomponent.CLabel lblSpace73;
    private com.see.truetransact.uicomponent.CLabel lblSpace74;
    private com.see.truetransact.uicomponent.CLabel lblSpace75;
    private com.see.truetransact.uicomponent.CLabel lblStatus1;
    private com.see.truetransact.uicomponent.CLabel lblTransactionType;
    private com.see.truetransact.uicomponent.CMenuBar mbrPfTransfer;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panPfClose;
    private com.see.truetransact.uicomponent.CPanel panPfTransfer;
    private com.see.truetransact.uicomponent.CPanel panShareCloseTrans;
    private com.see.truetransact.uicomponent.CPanel panShareNo;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTransaction;
    private com.see.truetransact.uicomponent.CPanel panTransactionType;
    private com.see.truetransact.uicomponent.CButtonGroup rdoTransactionType;
    private com.see.truetransact.uicomponent.CRadioButton rdoTransactionType_Credit;
    private com.see.truetransact.uicomponent.CRadioButton rdoTransactionType_Debit;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptPrint;
    private javax.swing.JSeparator sptProcess;
    private javax.swing.JToolBar tbrLoantProduct;
    private com.see.truetransact.uicomponent.CTextField txtAmount;
    private com.see.truetransact.uicomponent.CTextField txtBalance;
    private com.see.truetransact.uicomponent.CTextField txtPfNo;
    // End of variables declaration//GEN-END:variables
}
