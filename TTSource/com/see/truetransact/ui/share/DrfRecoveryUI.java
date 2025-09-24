/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * FixedAssetsTransUI.java
 *
 * Created on Jan 25, 2009, 10:53 AM
 */

package com.see.truetransact.ui.share;

import java.util.Map;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Observable;

import com.see.truetransact.ui.deposit.CommonMethods;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.DefaultValidation;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.ui.common.transaction.TransactionUI;
import com.see.truetransact.clientexception.ClientParseException;

import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.ui.customer.deathmarking.DeathMarkingUI;
import com.see.truetransact.clientutil.ComboBoxModel;

/** This form is used to manipulate FixedAssetsUI related functionality
 * @author
 */
//public class DrfTransactionUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer, UIMandatoryField {
public class DrfRecoveryUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer, UIMandatoryField{
    
    
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.share.DrfRecoveryRB", ProxyParameters.LANGUAGE);
    DrfRecoveryMRB objMandatoryRB = new DrfRecoveryMRB();
    private HashMap mandatoryMap;
    private DrfRecoveryOB observable;
    private TransactionUI transactionUI = new TransactionUI();
    
    private Date curDate = null;
    
    final int EDIT=0,DELETE=8,ACCNOCHEQUE=2,ACCNOSTOP=3,ACCNOLOOSE=4,VIEW=10,ECSSTOP=7;
    private int viewType=-1;
    private int BREAKAGE_ID = 1,MOVEMENT_ID = 2,FROM_ID =3,TO_ID =4,SALE_ID =5, AUTHORIZE=6;
    boolean isFilled = false;
    int updateTab=-1;
    private boolean updateMode = false;
    private double amount = 0.0;
    private double productAmount = 0.0;
    private double paymentAmount = 0.0;
    private double productPaymentAmount = 0.0;
    boolean flag = false;
    private String viewTypeStr = ClientConstants.VIEW_TYPE_CANCEL;
    final int DRFTRANSACTION=0;
    int pan=-1;
    int panEditDelete=-1;
    int view = -1;
    String DRF="";
    private String link_id= "";
    private boolean selectMode= false;
    ArrayList newList= new ArrayList();
    
    
    public DrfRecoveryUI() {
        
        
        initComponents();
        initStartup();
        transactionUI.setSourceScreen("DRF RECOVERY");
        transactionUI.addToScreen(panTransaction);
        transactionUI.setTransactionMode(CommonConstants.DEBIT);
        observable.setTransactionOB(transactionUI.getTransactionOB());
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
    }
    private void initStartup() {
        curDate = ClientUtil.getCurrentDate();
        setFieldNames();
        internationalize();
        setHelpMessage();
        setMandatoryHashMap();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panDrfTransDetails, getMandatoryHashMap());
        setObservable();
        initTableData();
        setButtonEnableDisable();
        setMaximumLength();
        initComponentData();
        resetUI();
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panDrfTransactionDetails, false);
        btnCancelActionPerformed(null);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(true);
        txtDrfTransAmount.setEnabled(false);
        //chkDueAmtPayment.setSelected(false);
    }
    private void initComponentData() {
        cboDrfTransProdID.setModel(observable.getCbmDrfTransProdID());
        System.out.println("@#$@#$@#$cboDrfTransProdID.getItemAt(0)"+cboDrfTransProdID.getItemAt(0));
        cboDrfTransProdID.setSelectedItem(cboDrfTransProdID.getItemAt(0));
    }
    public void update(Observable observed, Object arg) {
        updateDrfTransUI();
    }
    ///* Auto Generated Method - getMandatoryHashMap()
    //   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    
    private void setMaximumLength() {
        txtDrfTransMemberNo.setMaxLength(16);
        txtDrfTransMemberNo.setAllowAll(true);
        txtDrfTransName.setMaxLength(128);
        txtDrfTransName.setAllowAll(true);
        txtDrfTransAmount.setMaxLength(14);
    }
    private void initTableData() {
        tblDrfTransaction.setModel(observable.getTblDrfTransaction());
    }
    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(!btnDelete.isEnabled());
        btnAuthorize.setEnabled(!btnAuthorize.isEnabled());
        btnReject.setEnabled(!btnReject.isEnabled());
        btnException.setEnabled(!btnException.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());
        
        btnSave.setEnabled(!btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
        btnClose.setEnabled(true);
    }
    
    private void setObservable(){
        /* Singleton pattern can't be implemented as there are two observers using the same observable*/
        // The parameter '1' indicates that the customer type is INDIVIDUAL
        observable = new DrfRecoveryOB(1);
        observable.addObserver(this);
    }
     /* Auto Generated Method - setMandatoryHashMap()
      
//ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
//
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboDrfTransProdID",new Boolean(true));
        mandatoryMap.put("txtDrfTransMemberNo", new Boolean(true));
        
    }
    /* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        objMandatoryRB = new DrfRecoveryMRB();
        //txtDrfTransAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDrfTransAmount"));
        cboDrfTransProdID.setHelpMessage(lblMsg,objMandatoryRB.getString("cboDrfTransProdID"));
        //txtDrfTransName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDrfTransName"));
        txtDrfTransMemberNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDrfTransMemberNo"));
    }
    
    
     /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
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
        btnView.setName("btnView");
        lblMsg.setName("lblMsg");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblSpace5.setName("lblSpace5");
        lblStatus.setName("lblStatus");
        panStatus.setName("panStatus");
        tblDrfTransaction.setName("tblDrfTransaction");
        lblDrfTransMemberNo.setName("lblDrfTransMemberNo");
        txtDrfTransMemberNo.setName("txtDrfTransMemberNo");
        txtDrfTransName.setName("txtDrfTransName");
        txtDrfTransAmount.setName("txtDrfTransAmount");
        cboDrfTransProdID.setName("cboDrfTransProdID");
        lblDrfTransProdID.setName("lblDrfTransProdID");
    }
    
    private void internationalize() {
        resourceBundle = new DrfTransactionRB();
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblDrfTransAmount.setText(resourceBundle.getString("lblDrfTransAmount"));
        lblDrfTransAddress.setText(resourceBundle.getString("lblDrfTransAddress"));
        lblDrfTransMemberNo.setText(resourceBundle.getString("lblDrfTransMemberNo"));
        // lblDueAmtPayment.setText(resourceBundle.getString("lblDueAmtPayment"));
        ((javax.swing.border.TitledBorder)panDrfTransDetails.getBorder()).setTitle(resourceBundle.getString("panDrfTransDetails"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        btnView.setText(resourceBundle.getString("btnView"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        lblSpace5.setText(resourceBundle.getString("lblSpace5"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        txtDrfTransMemberNo.setName("txtDrfTransMemberNo");
        txtDrfTransName.setName("txtDrfTransName");
        txtDrfTransAmount.setName("txtDrfTransAmount");
        cboDrfTransProdID.setName("cboDrfTransProdID");
    }
    
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoDrfTransaction = new com.see.truetransact.uicomponent.CButtonGroup();
        panDrfTransactionDetails = new com.see.truetransact.uicomponent.CPanel();
        tabDrfTransaction = new com.see.truetransact.uicomponent.CTabbedPane();
        panDrfTransaction = new com.see.truetransact.uicomponent.CPanel();
        panDrfTransList = new com.see.truetransact.uicomponent.CPanel();
        srpDrfTransaction = new com.see.truetransact.uicomponent.CScrollPane();
        tblDrfTransaction = new com.see.truetransact.uicomponent.CTable();
        panDrfTransDetails = new com.see.truetransact.uicomponent.CPanel();
        lblDrfTransName = new com.see.truetransact.uicomponent.CLabel();
        lblDrfTransAddress = new com.see.truetransact.uicomponent.CLabel();
        lblDrfTransMemberNo = new com.see.truetransact.uicomponent.CLabel();
        lblDrfTransAmount = new com.see.truetransact.uicomponent.CLabel();
        txtDrfTransAmount = new com.see.truetransact.uicomponent.CTextField();
        txtDrfTransMemberNo = new com.see.truetransact.uicomponent.CTextField();
        txtDrfTransName = new com.see.truetransact.uicomponent.CTextField();
        lblDrfTransAddressCont = new com.see.truetransact.uicomponent.CLabel();
        lblDrfTransProdID = new com.see.truetransact.uicomponent.CLabel();
        cboDrfTransProdID = new com.see.truetransact.uicomponent.CComboBox();
        btnMemberNo = new com.see.truetransact.uicomponent.CButton();
        panTransaction = new com.see.truetransact.uicomponent.CPanel();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        tbrOperativeAcctProduct = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace65 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace66 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace67 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace68 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace69 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace70 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        lblSpace71 = new com.see.truetransact.uicomponent.CLabel();
        btnDateChange = new com.see.truetransact.uicomponent.CButton();
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
        setMaximumSize(new java.awt.Dimension(800, 670));
        setMinimumSize(new java.awt.Dimension(800, 670));
        setPreferredSize(new java.awt.Dimension(800, 670));

        panDrfTransactionDetails.setMaximumSize(new java.awt.Dimension(650, 520));
        panDrfTransactionDetails.setMinimumSize(new java.awt.Dimension(650, 520));
        panDrfTransactionDetails.setPreferredSize(new java.awt.Dimension(650, 520));
        panDrfTransactionDetails.setLayout(new java.awt.GridBagLayout());

        tabDrfTransaction.setMinimumSize(new java.awt.Dimension(845, 342));
        tabDrfTransaction.setPreferredSize(new java.awt.Dimension(845, 342));

        panDrfTransaction.setMinimumSize(new java.awt.Dimension(830, 313));
        panDrfTransaction.setPreferredSize(new java.awt.Dimension(830, 313));
        panDrfTransaction.setLayout(new java.awt.GridBagLayout());

        panDrfTransList.setMinimumSize(new java.awt.Dimension(900, 300));
        panDrfTransList.setPreferredSize(new java.awt.Dimension(900, 300));
        panDrfTransList.setLayout(new java.awt.GridBagLayout());

        srpDrfTransaction.setMaximumSize(new java.awt.Dimension(470, 245));
        srpDrfTransaction.setMinimumSize(new java.awt.Dimension(400, 215));
        srpDrfTransaction.setPreferredSize(new java.awt.Dimension(400, 225));

        tblDrfTransaction.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Select", "Paid Date", "Amount Due"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblDrfTransaction.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblDrfTransaction.setMaximumSize(new java.awt.Dimension(2147483647, 10000));
        tblDrfTransaction.setMinimumSize(new java.awt.Dimension(280, 600));
        tblDrfTransaction.setPreferredScrollableViewportSize(new java.awt.Dimension(280, 600));
        tblDrfTransaction.setPreferredSize(new java.awt.Dimension(280, 600));
        tblDrfTransaction.setOpaque(false);
        tblDrfTransaction.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDrfTransactionMouseClicked(evt);
            }
        });
        srpDrfTransaction.setViewportView(tblDrfTransaction);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 20, 0, 0);
        panDrfTransList.add(srpDrfTransaction, gridBagConstraints);

        panDrfTransDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("DRF Transaction"));
        panDrfTransDetails.setMinimumSize(new java.awt.Dimension(320, 245));
        panDrfTransDetails.setPreferredSize(new java.awt.Dimension(320, 235));
        panDrfTransDetails.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                panDrfTransDetailsFocusGained(evt);
            }
        });
        panDrfTransDetails.setLayout(new java.awt.GridBagLayout());

        lblDrfTransName.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panDrfTransDetails.add(lblDrfTransName, gridBagConstraints);

        lblDrfTransAddress.setText("Address");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 42, 0);
        panDrfTransDetails.add(lblDrfTransAddress, gridBagConstraints);

        lblDrfTransMemberNo.setText("Member No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panDrfTransDetails.add(lblDrfTransMemberNo, gridBagConstraints);

        lblDrfTransAmount.setText("Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panDrfTransDetails.add(lblDrfTransAmount, gridBagConstraints);

        txtDrfTransAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDrfTransAmount.setValidation(new NumericValidation());
        txtDrfTransAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDrfTransAmountFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDrfTransAmountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 12, 1, 0);
        panDrfTransDetails.add(txtDrfTransAmount, gridBagConstraints);

        txtDrfTransMemberNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDrfTransMemberNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDrfTransMemberNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 12, 1, 0);
        panDrfTransDetails.add(txtDrfTransMemberNo, gridBagConstraints);

        txtDrfTransName.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDrfTransName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDrfTransNameFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 12, 1, 0);
        panDrfTransDetails.add(txtDrfTransName, gridBagConstraints);

        lblDrfTransAddressCont.setMaximumSize(new java.awt.Dimension(100, 60));
        lblDrfTransAddressCont.setMinimumSize(new java.awt.Dimension(100, 60));
        lblDrfTransAddressCont.setPreferredSize(new java.awt.Dimension(100, 60));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        panDrfTransDetails.add(lblDrfTransAddressCont, gridBagConstraints);

        lblDrfTransProdID.setText("Product ID ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panDrfTransDetails.add(lblDrfTransProdID, gridBagConstraints);

        cboDrfTransProdID.setMinimumSize(new java.awt.Dimension(100, 21));
        cboDrfTransProdID.setName("cboProfession");
        cboDrfTransProdID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboDrfTransProdIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 12, 1, 4);
        panDrfTransDetails.add(cboDrfTransProdID, gridBagConstraints);

        btnMemberNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnMemberNo.setToolTipText("Customer Data");
        btnMemberNo.setMinimumSize(new java.awt.Dimension(25, 25));
        btnMemberNo.setPreferredSize(new java.awt.Dimension(25, 25));
        btnMemberNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMemberNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panDrfTransDetails.add(btnMemberNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panDrfTransList.add(panDrfTransDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panDrfTransaction.add(panDrfTransList, gridBagConstraints);

        panTransaction.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        panTransaction.setMinimumSize(new java.awt.Dimension(900, 95));
        panTransaction.setPreferredSize(new java.awt.Dimension(900, 95));
        panTransaction.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panDrfTransaction.add(panTransaction, gridBagConstraints);

        tabDrfTransaction.addTab("Death Relief Fund", panDrfTransaction);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panDrfTransactionDetails.add(tabDrfTransaction, gridBagConstraints);

        getContentPane().add(panDrfTransactionDetails, java.awt.BorderLayout.CENTER);

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

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView.setPreferredSize(new java.awt.Dimension(21, 21));
        btnView.setEnabled(false);
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

        lblSpace65.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace65.setText("     ");
        lblSpace65.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace65.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace65.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace65);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnEdit);

        lblSpace66.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace66.setText("     ");
        lblSpace66.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace66.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace66.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace66);

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

        lblSpace67.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace67.setText("     ");
        lblSpace67.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace67.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace67.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace67);

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

        lblSpace68.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace68.setText("     ");
        lblSpace68.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace68.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace68.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace68);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnException);

        lblSpace69.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace69.setText("     ");
        lblSpace69.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace69.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace69.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace69);

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
        tbrOperativeAcctProduct.add(btnPrint);

        lblSpace70.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace70.setText("     ");
        lblSpace70.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace70.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace70.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace70);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnClose);

        lblSpace71.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace71.setText("     ");
        lblSpace71.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace71.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace71.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace71);

        btnDateChange.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/denomination.gif"))); // NOI18N
        btnDateChange.setToolTipText("Exception");
        tbrOperativeAcctProduct.add(btnDateChange);

        getContentPane().add(tbrOperativeAcctProduct, java.awt.BorderLayout.NORTH);

        mbrCustomer.setName("mbrCustomer");

        mnuProcess.setText("Process");
        mnuProcess.setName("mnuProcess");

        mitNew.setText("New");
        mitNew.setName("mitNew");
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

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

        sptNew.setName("sptNew");
        mnuProcess.add(sptNew);

        mitSave.setText("Save");
        mitSave.setName("mitSave");
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancel");
        mitCancel.setName("mitCancel");
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess.add(mitCancel);

        sptSave.setName("sptSave");
        mnuProcess.add(sptSave);

        mitPrint.setText("Print");
        mitPrint.setName("mitPrint");
        mnuProcess.add(mitPrint);

        mitClose.setText("Close");
        mitClose.setName("mitClose");
        mnuProcess.add(mitClose);

        mbrCustomer.add(mnuProcess);

        setJMenuBar(mbrCustomer);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void txtDrfTransAmountFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDrfTransAmountFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDrfTransAmountFocusGained
    
    private void panDrfTransDetailsFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_panDrfTransDetailsFocusGained
        // TODO add your handling code here:
        
    }//GEN-LAST:event_panDrfTransDetailsFocusGained
    
    private void tblDrfTransactionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDrfTransactionMouseClicked
        // TODO add your handling code here:
        if(selectMode == true) {
            String st=CommonUtil.convertObjToStr(tblDrfTransaction.getValueAt(tblDrfTransaction.getSelectedRow(),0));
            if(st.equals("true")) {
                tblDrfTransaction.setValueAt(new Boolean(false),tblDrfTransaction.getSelectedRow(),0);
            }
            else {
                tblDrfTransaction.setValueAt(new Boolean(true),tblDrfTransaction.getSelectedRow(),0);
            }
            double totAmount = 0;
            for (int i=0;i<tblDrfTransaction.getRowCount();i++) {
                st=CommonUtil.convertObjToStr(tblDrfTransaction.getValueAt(i,0));
                if(st.equals("true")){
                    totAmount = totAmount+CommonUtil.convertObjToDouble(tblDrfTransaction.getValueAt(i, 3)).doubleValue();
                }
            }
            txtDrfTransAmount.setText(CurrencyValidation.formatCrore(String.valueOf(totAmount)));
            transactionUI.setCallingAmount(txtDrfTransAmount.getText());
            transactionUI.setCallingApplicantName(txtDrfTransName.getText());
            
        }
    }//GEN-LAST:event_tblDrfTransactionMouseClicked
    
    private void txtDrfTransNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDrfTransNameFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDrfTransNameFocusLost
    
    private void btnMemberNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMemberNoActionPerformed
        // TODO add your handling code here:
        callView("MemberNo");
    }//GEN-LAST:event_btnMemberNoActionPerformed
    
    private void txtDrfTransAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDrfTransAmountFocusLost
        // TODO add your handling code here:
        transactionUI.setCallingAmount(txtDrfTransAmount.getText());
        transactionUI.setCallingApplicantName(txtDrfTransName.getText());
    }//GEN-LAST:event_txtDrfTransAmountFocusLost
    
    private void txtDrfTransMemberNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDrfTransMemberNoFocusLost
        
    }//GEN-LAST:event_txtDrfTransMemberNoFocusLost
    
    private void cboDrfTransProdIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboDrfTransProdIDActionPerformed
        // TODO add your handling code here:
        String prodID = CommonUtil.convertObjToStr(cboDrfTransProdID.getSelectedItem());
        if (prodID.length()>0) {
            HashMap drfProdDetailsMap = new HashMap();
            drfProdDetailsMap.put("PROD_ID",prodID);
            drfProdDetailsMap.put("CURRENT_DATE",curDate);
            List getDrfProdDetails = ClientUtil.executeQuery("getDrfProductDetailsForTrans", drfProdDetailsMap);
            if(getDrfProdDetails!= null && getDrfProdDetails.size() > 0) {
                drfProdDetailsMap = (HashMap) getDrfProdDetails.get(0);
                amount = CommonUtil.convertObjToDouble(drfProdDetailsMap.get("AMOUNT")).doubleValue();
                productAmount = amount;
                paymentAmount = CommonUtil.convertObjToDouble(drfProdDetailsMap.get("PAYMENT")).doubleValue();
                productPaymentAmount = paymentAmount;
            }
        }
        btnMemberNo.setEnabled(true);
    }//GEN-LAST:event_cboDrfTransProdIDActionPerformed
    
    /* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setTxtDrfTransAmount(txtDrfTransAmount.getText());
        observable.setDrfProdPaymentAmt(String.valueOf(productPaymentAmount));
        observable.setDrfProductAmount(String.valueOf(productAmount));
        observable.setTxtDrfTransName(txtDrfTransName.getText());
        observable.setTxtDrfTransMemberNo(txtDrfTransMemberNo.getText());
        observable.setLblDrfTransAddressCont(lblDrfTransAddressCont.getText());
        observable.setCboDrfTransProdID(CommonUtil.convertObjToStr(cboDrfTransProdID.getSelectedItem()));
    }
    private void updateDrfTransUI() {
        txtDrfTransAmount.setText(observable.getTxtDrfTransAmount());
        cboDrfTransProdID.setSelectedItem(observable.getCboDrfTransProdID());
        txtDrfTransName.setText(observable.getTxtDrfTransName());
        txtDrfTransMemberNo.setText(observable.getTxtDrfTransMemberNo());
        lblDrfTransAddressCont.setText(observable.getLblDrfTransAddressCont());
    }
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_VIEW);
        transactionUI.cancelAction(false);
        resetUI();
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        observable.setStatus();
        //        callView(EDIT);
        lblStatus.setText("Delete");
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        ClientUtil.enableDisable(panTransaction,false);
        ClientUtil.enableDisable(panDrfTransDetails,false);
        btnSave.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
        btnEdit.setEnabled(false);
    }//GEN-LAST:event_btnViewActionPerformed
    private void resetUI() {
        observable.resetDrfTransDetails();
        observable.resetDrfTransListTable();
        lblDrfTransAddressCont.setText("");
    }
    private void callView(String currField) {
        observable.setStatus();
        HashMap viewMap = new HashMap();
        //final HashMap viewMap = new HashMap();
        
        
        if(currField.equals("MemberNo")) {
            HashMap where = new HashMap();
            String PRODUCT_ID = CommonUtil.convertObjToStr(((ComboBoxModel) cboDrfTransProdID.getModel()).getKeyForSelected());
            where.put("PRODUCT_ID", PRODUCT_ID);
            viewMap.put(CommonConstants.MAP_NAME, "getDrfMemberDetails");
            viewMap.put(CommonConstants.MAP_WHERE, where);
            where = null;
        }
        new ViewAll(this,viewMap).show();
    }
    
    public void fillData(Object obj){
        try {
            HashMap hashMap=(HashMap)obj;
            HashMap returnMap = null;
            HashMap memberDetMap = new HashMap();
            memberDetMap.put("MEMBERSHIP_NO",CommonUtil.convertObjToStr(hashMap.get("MEMBER_NO")));
            //            this query is to see if the member no entered is correct
            List memberDetails = ClientUtil.executeQuery("getMemberDetailsForDrf", memberDetMap);
            if(memberDetails != null& memberDetails.size() > 0) {
                memberDetMap = new HashMap();
                memberDetMap = (HashMap) memberDetails.get(0);
                
                txtDrfTransName.setText(CommonUtil.convertObjToStr(memberDetMap.get("NAME")));
                txtDrfTransName.setEnabled(false);
                lblDrfTransAddressCont.setText(CommonUtil.convertObjToStr(memberDetMap.get("ADDRESS")));
            }
            txtDrfTransAmount.setText(CommonUtil.convertObjToStr(hashMap.get("AMOUNT")));
            txtDrfTransAmount.setEnabled(false);
            txtDrfTransMemberNo.setText(CommonUtil.convertObjToStr(hashMap.get("MEMBER_NO")));
            
            
            HashMap memberDetailsMap = new HashMap();
            memberDetailsMap.put("MEMBERSHIP_NO",CommonUtil.convertObjToStr(txtDrfTransMemberNo.getText()));
            memberDetailsMap.put("PROD_ID",CommonUtil.convertObjToStr(cboDrfTransProdID.getSelectedItem()));
            
            //to load values due details to
            List memberDrfDueDetails = ClientUtil.executeQuery("getMemberDrfDueDetails",memberDetailsMap);
            if(memberDrfDueDetails != null && memberDrfDueDetails.size() >0) {
                observable.populateDrfTransTable(memberDrfDueDetails);
                selectMode=true;
                
            }
            observable.setDrfTransID(CommonUtil.convertObjToStr(hashMap.get("DRF_TRANS_ID")));
            
            if (observable.getActionType() == (ClientConstants.ACTIONTYPE_EDIT)||observable.getActionType() ==(ClientConstants.ACTIONTYPE_DELETE)|| observable.getActionType() ==(ClientConstants.ACTIONTYPE_AUTHORIZE) || observable.getActionType()==(ClientConstants.ACTIONTYPE_REJECT)) {
                isFilled = true;
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT
                || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType()==ClientConstants.ACTIONTYPE_REJECT){
                    actionEditDelete(hashMap);
                    panEditDelete=DRFTRANSACTION;
                    link_id=CommonUtil.convertObjToStr(hashMap.get("TRANS_ID"));
                    //hashMap.put(CommonConstants.MAP_WHERE, where);
                    observable.populateData(hashMap);
                    observable.setDrfTransID(CommonUtil.convertObjToStr(hashMap.get("TRANS_ID")));
                    observable.populateDrfTransData(String.valueOf(hashMap.get("TRANS_ID")),panEditDelete);
                    observable.setDrfTransID(CommonUtil.convertObjToStr(hashMap.get("DRF_TRANS_ID")));
                    initTableData();
                    btnCancel.setEnabled(true);
                    observable.ttNotifyObservers();
                    
                }
                //--- disable the customerSelection in Edit Mode
            }
            
            hashMap = null;
            returnMap = null;
            btnMemberNo.setEnabled(false);
        }catch(Exception e){
            e.printStackTrace();
        }
        
        //__ To Save the data in the Internal Frame...
        setModified(true);
        
    }
    
    private void actionEditDelete(HashMap hash){
        //fromActionEditHash = true;
        observable.resetForm();
        observable.setStatus();
        setButtonEnableDisable();
    }
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        DRF="REJECT";
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        viewType = ClientConstants.ACTIONTYPE_AUTHORIZE;
        String rejected_status="";
        authorizeStatus(rejected_status);
        //authorizeStatus(CommonConstants.STATUS_REJECTED);
        btnReject.setEnabled(true);
        btnException.setEnabled(false);
        btnSave.setEnabled(false);
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(false);
        
    }//GEN-LAST:event_btnRejectActionPerformed
    private void resetTransactionUI(){
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
    }
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        //DRF="AUTHORIZE";
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        viewType = ClientConstants.ACTIONTYPE_AUTHORIZE;
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnSave.setEnabled(false);
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(true);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    
    
    public void authorizeStatus(String authorizeStatus) {
        if (viewType== ClientConstants.ACTIONTYPE_AUTHORIZE && isFilled) {
            
            ArrayList arrList = new ArrayList();
            HashMap authorizeMap = new HashMap();
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("AUTH_BY", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("AUTH_DT", curDate);
            singleAuthorizeMap.put("DRF_TRANS_ID", link_id);
            singleAuthorizeMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            arrList.add(singleAuthorizeMap);
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            if(transactionUI.getOutputTO().size()>0){
                observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
            }
            authorize(authorizeMap,link_id);
            viewType = -1;
            super.setOpenForEditBy(observable.getStatusBy());
            singleAuthorizeMap = null;
            authorizeMap = null;
            
        }
        else {
            //__ To Save the data in the Internal Frame...
            HashMap whereMap = new HashMap();
            //setModified(true);
            whereMap.put(CommonConstants.MAP_NAME, "getDrfRecAuth");
            panEditDelete = DRFTRANSACTION;
            pan = DRFTRANSACTION;
            whereMap.put(CommonConstants.HIERARCHY_ID, ProxyParameters.HIERARCHY_ID);
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            viewType = AUTHORIZE;
            isFilled = false;
            //AuthorizeUI authorizeUI = new AuthorizeUI(this, whereMap);
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, whereMap);
            authorizeUI.show();
            whereMap = null;
            
        }
    }
    
    public void authorize(HashMap map,String id) {
        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
            observable.set_authorizeMap(map);
            observable.doAction();
            
            if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED){
                super.setOpenForEditBy(observable.getStatusBy());
                super.removeEditLock(id);
            }
            
            btnCancelActionPerformed(null);
            observable.setStatus();
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
        }
        if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED) {
            isFilled = false;
            super.setOpenForEditBy(observable.getStatusBy());
            observable.setResultStatus();
        }
    }
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        final String mandatoryMessage = checkMandatory(panDrfTransDetails);
        StringBuffer message = new StringBuffer(mandatoryMessage);
        
        
        resourceBundle = new DrfTransactionRB();
        //final String shareAcctMandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panDrfTransDetails);
        
        StringBuffer strBMandatory = new StringBuffer();
        if(cboDrfTransProdID.getSelectedIndex()==0) {
            message.append(objMandatoryRB.getString("cboDrfTransProdID"));
        }
        if(txtDrfTransMemberNo.getText().equals("")) {
            message.append(objMandatoryRB.getString("txtDrfTransMemberNo"));
        }
        if(txtDrfTransName.getText().equals("")) {
            message.append(objMandatoryRB.getString("txtDrfTransName"));
        }
        if(lblDrfTransAddressCont.getText().equals("")) {
            message.append(objMandatoryRB.getString("lblDrfTransAddressCont"));
        }
        if(txtDrfTransAmount.getText().equals("")) {
            message.append(objMandatoryRB.getString("txtDrfTransAmount"));
        }
        if(message.length() > 0 ) {
            displayAlert(message.toString());
            return;
        }
        
        if(txtDrfTransAmount.getText().equals("0.00")) {
            message.append(objMandatoryRB.getString("tblDrfTransaction"));
        }
        int transactionSize = 0 ;
        if(/*rdoSharewithDrawal.isSelected()==true && */(transactionUI.getOutputTO() == null || (transactionUI.getOutputTO()).size() == 0)) {
            strBMandatory.append(resourceBundle.getString("NoRecords"));
            strBMandatory.append("\n");
        }
        else {
            transactionSize = (transactionUI.getOutputTO()).size();
            observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
        }
        
        String strMandatory = strBMandatory.toString();
        
        
        //--- checks whether the Mandatory fields are entered
        if(strMandatory.length()>0) {        //--- if all the mandatory fields are not entered,
            CommonMethods.displayAlert(strMandatory);     //--- display the alert
        }
        else if(strMandatory.length()==0) {
            
            //--- if all the values are entered, save the data
            //Call transaction screen here
            //If transactions exist, proceed to save them
            if (transactionSize  > 0/* && rdoSharewithDrawal.isSelected()==true*/) {
                if (!transactionUI.isBtnSaveTransactionDetailsFlag()) {
                    observable.showAlertWindow(resourceBundle.getString("saveInTxDetailsTable"));
                }
                else if (transactionUI.isBtnSaveTransactionDetailsFlag()) {
                    //                        int noOfShares = getNoOfShares();
                    
                    double transTotalAmt = CommonUtil.convertObjToDouble(transactionUI.getTransactionOB().getLblTotalTransactionAmtVal()).doubleValue();
                    if (ClientUtil.checkTotalAmountTallied(CommonUtil.convertObjToDouble(txtDrfTransAmount.getText()).doubleValue(), transTotalAmt) == false)
                        ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NOT_TALLY));
                    else {
                        savePerformed();
                        observable.setStatus();
                        observable.setResultStatus();
                        lblStatus.setText(observable.getLblStatus());
                    }
                }
            }
            else {
                savePerformed();
                observable.setStatus();
                observable.setResultStatus();
                lblStatus.setText(observable.getLblStatus());
            }
            resourceBundle = null;
        }
        else {
            CommonMethods.displayAlert(resourceBundle.getString("saveAcctDet"));
        }
        
    }//GEN-LAST:event_btnSaveActionPerformed
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    private void savePerformed() {
        try {
            updateOBFields();
            observable.setResult(observable.getActionType());
            ArrayList listAdd = null;
            for(int i=0;i<tblDrfTransaction.getRowCount();i++) {
                if(((Boolean)tblDrfTransaction.getValueAt(i,0)).booleanValue()) {
                    listAdd = new ArrayList();
                    listAdd.add(tblDrfTransaction.getValueAt(i,1));
                    listAdd.add(tblDrfTransaction.getValueAt(i,2));
                    listAdd.add(tblDrfTransaction.getValueAt(i,3));
                    newList.add(listAdd);
                }
                
            }
            observable.setTblList(newList);
            observable.doAction();
            
            if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED) {
                if (observable.getProxyReturnMap()!=null) {
                    if (observable.getProxyReturnMap().containsKey("CASH_TRANS_LIST") || observable.getProxyReturnMap().containsKey("TRANSFER_TRANS_LIST")) {
                        displayTransDetail(observable.getProxyReturnMap());
                    }
                }
            }
            
            observable.makeToNull();
            btnCancelActionPerformed(null);
            //            btnCancelActionPerformed(null);
            observable.ttNotifyObservers();
            observable.setResultStatus();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    private String checkMandatory(javax.swing.JComponent component){
        //          return new MandatoryCheck().checkMandatory(getClass().getName(), component, getMandatoryHashMap());
        return "";
        //validation error
    }
    
    private void displayTransDetail(HashMap proxyResultMap) {
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
        for (int i=0; i<keys.length; i++) {
            if (proxyResultMap.get(keys[i]) instanceof String) {
                continue;
            }
            tempList = (List)proxyResultMap.get(keys[i]);
            if (CommonUtil.convertObjToStr(keys[i]).indexOf("CASH")!=-1) {
                for (int j=0; j<tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j==0) {
                        transId = (String)transMap.get("TRANS_ID");
                    }
                    cashDisplayStr += "Trans Id : "+transMap.get("TRANS_ID")+
                    "   Trans Type : "+transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if(actNum != null && !actNum.equals("")){
                        cashDisplayStr +="   Account No : "+transMap.get("ACT_NUM")+
                        "   Amount : "+transMap.get("AMOUNT")+"\n";
                    }else{
                        cashDisplayStr += "   Ac Hd Desc : "+transMap.get("AC_HD_ID")+
                        "   Amount : "+transMap.get("AMOUNT")+"\n";
                    }
                }
                cashCount++;
            } else if (CommonUtil.convertObjToStr(keys[i]).indexOf("TRANSFER")!=-1) {
                for (int j=0; j<tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j==0) {
                        transId = (String)transMap.get("BATCH_ID");
                    }
                    transferDisplayStr += "Trans Id : "+transMap.get("TRANS_ID")+
                    "   Batch Id : "+transMap.get("BATCH_ID")+
                    "   Trans Type : "+transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if(actNum != null && !actNum.equals("")){
                        transferDisplayStr +="   Account No : "+transMap.get("ACT_NUM")+
                        "   Amount : "+transMap.get("AMOUNT")+"\n";
                    }else{
                        transferDisplayStr += "   Ac Hd Desc : "+transMap.get("AC_HD_ID")+
                        "   Amount : "+transMap.get("AMOUNT")+"\n";
                    }
                }
                transferCount++;
            }
        }
        if(cashCount>0){
            displayStr+=cashDisplayStr;
        }
        if(transferCount>0){
            displayStr+=transferDisplayStr;
        }
        //        ClientUtil.showMessageWindow(""+displayStr);
        
        int yesNo = 0;
        String[] options = {"Yes", "No"};
        yesNo = COptionPane.showOptionDialog(null,"Do you want to print?", CommonConstants.WARNINGTITLE,
        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
        null, options, options[0]);
        if (yesNo==0) {
            TTIntegration ttIntgration = null;
            HashMap paramMap = new HashMap();
            paramMap.put("TransId", transId);
            paramMap.put("TransDt", curDate);
            paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
            ttIntgration.setParam(paramMap);
            //            if (((String)TrueTransactMain.BANKINFO.get("BANK_NAME")).toUpperCase().lastIndexOf("POLPULLY")!=-1) {
            //                    ttIntgration.integrationForPrint("ReceiptPayment", false);
            //            } else {
            String reportName = "";
            if(transferCount>0){
                reportName = "ReceiptPayment";
            } else if (observable.getRdoDrfTransaction().equals("PAYMENT")) {
                reportName = "CashPayment";
            } else {
                reportName = "CashReceipt";
            }
            ttIntgration.integrationForPrint(reportName, false);
        }
    }
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        setModified(true);
        resetUI();               // to Reset all the Fields and Status in UI...
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panDrfTransactionDetails, true);
        setButtonEnableDisable();
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnPrint.setEnabled(false);
        observable.resetForm();
        observable.setStatus();
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        viewType = ClientConstants.ACTIONTYPE_NEW;
        observable.setStatus();
        btnView.setEnabled(false);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);   //Sets the Action Type to be performed...
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
        transactionUI.setCallingApplicantName("");
        transactionUI.setCallingAmount("");
        DRF="NEW";
    }//GEN-LAST:event_btnNewActionPerformed
    
    private void deletescreenLock(){
        HashMap map=new HashMap();
        map.put("USER_ID",ProxyParameters.USER_ID);
        map.put("TRANS_DT",  curDate);
        map.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
        ClientUtil.execute("DELETE_SCREEN_LOCK", map);
    }
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        setModified(false);
        deletescreenLock();
        observable.resetForm();
        observable.setAuthorizeStatus("");
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panDrfTransactionDetails, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_CANCEL);
        setButtonEnableDisable();
        resetUI();
        observable.resetForm();
        lblStatus.setText("             ");
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        btnNew.setEnabled(true);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(true);
        btnSave.setEnabled(false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
        transactionUI.setCallingApplicantName("");
        transactionUI.setCallingAmount("");
        isFilled = false;
        //        chkDueAmtPayment.setSelected(false);
        DRF="";
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {
    }
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        
    }//GEN-LAST:event_mitDeleteActionPerformed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        
    }//GEN-LAST:event_mitEditActionPerformed
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        
    }//GEN-LAST:event_mitNewActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        
    }//GEN-LAST:event_mitSaveActionPerformed
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDateChange;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnMemberNo;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboDrfTransProdID;
    private com.see.truetransact.uicomponent.CLabel lblDrfTransAddress;
    private com.see.truetransact.uicomponent.CLabel lblDrfTransAddressCont;
    private com.see.truetransact.uicomponent.CLabel lblDrfTransAmount;
    private com.see.truetransact.uicomponent.CLabel lblDrfTransMemberNo;
    private com.see.truetransact.uicomponent.CLabel lblDrfTransName;
    private com.see.truetransact.uicomponent.CLabel lblDrfTransProdID;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace65;
    private com.see.truetransact.uicomponent.CLabel lblSpace66;
    private com.see.truetransact.uicomponent.CLabel lblSpace67;
    private com.see.truetransact.uicomponent.CLabel lblSpace68;
    private com.see.truetransact.uicomponent.CLabel lblSpace69;
    private com.see.truetransact.uicomponent.CLabel lblSpace70;
    private com.see.truetransact.uicomponent.CLabel lblSpace71;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CMenuBar mbrCustomer;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panDrfTransDetails;
    private com.see.truetransact.uicomponent.CPanel panDrfTransList;
    private com.see.truetransact.uicomponent.CPanel panDrfTransaction;
    private com.see.truetransact.uicomponent.CPanel panDrfTransactionDetails;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTransaction;
    private com.see.truetransact.uicomponent.CButtonGroup rdoDrfTransaction;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CScrollPane srpDrfTransaction;
    private com.see.truetransact.uicomponent.CTabbedPane tabDrfTransaction;
    private com.see.truetransact.uicomponent.CTable tblDrfTransaction;
    private javax.swing.JToolBar tbrOperativeAcctProduct;
    private com.see.truetransact.uicomponent.CTextField txtDrfTransAmount;
    private com.see.truetransact.uicomponent.CTextField txtDrfTransMemberNo;
    private com.see.truetransact.uicomponent.CTextField txtDrfTransName;
    // End of variables declaration//GEN-END:variables
}