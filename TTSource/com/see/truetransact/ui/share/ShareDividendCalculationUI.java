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

//import java.util.Map;
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
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;


/** This form is used to manipulate FixedAssetsUI related functionality
 * @author nikhil
 */
//public class ShareDividendCalculationUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer, UIMandatoryField {
public class ShareDividendCalculationUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer, UIMandatoryField{
    
    
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.share.ShareDividendCalculationRB", ProxyParameters.LANGUAGE);
    ShareDividendCalculationMRB objMandatoryRB = new ShareDividendCalculationMRB();
    private HashMap mandatoryMap;
    private ShareDividendCalculationOB observable;
    private Date curDate = null;
    private ArrayList getShareDetails;
    final int EDIT=0,DELETE=8,ACCNOCHEQUE=2,ACCNOSTOP=3,ACCNOLOOSE=4,VIEW=10,ECSSTOP=7;
    private int viewType=-1;
    private int BREAKAGE_ID = 1,MOVEMENT_ID = 2,FROM_ID =3,TO_ID =4,SALE_ID =5, AUTHORIZE=6;
    private int DEBIT_GL = 10,PAYABLE_GL = 11;
    boolean isFilled = false;
    int updateTab=-1;
    private boolean updateMode = false;
    private double amount = 0.0;
    private double productAmount = 0.0;
    private double paymentAmount = 0.0;
    private double productPaymentAmount = 0.0;
    boolean flag = false;
    private String viewTypeStr = ClientConstants.VIEW_TYPE_CANCEL;
    final int ShareDividendCalculation=0;
    int pan=-1;
    int panEditDelete=-1;
    int view = -1;
    private String saveMode = "";
    
    public ShareDividendCalculationUI() {
        
        
        initComponents();
        initStartup();
    }
    private void initStartup() {
        curDate = ClientUtil.getCurrentDate();
        setFieldNames();
        internationalize();
        setHelpMessage();
        setMandatoryHashMap();
        setObservable();
        initTableData();
        setButtonEnableDisable();
        setMaximumLength();
        initComponentData();
        resetUI();
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panShareDividendCalculationDetails, false);
        tbrOperativeAcctProduct.setVisible(false);
        btnCancelActionPerformed();
        btnNewActionPerformed();
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(true);
        btnCalculate.setEnabled(true);
        btnSaveTransaction.setEnabled(false);
        mbrCustomer.setVisible(false);
        setModified(false);
        //        chkDueAmtPayment.setSelected(false);
    }
    private void initComponentData() {
        cboShareClass.setModel(observable.getCbmShareClass());
        //        System.out.println("@#$@#$@#$cboDrfTransProdID.getItemAt(0)"+cboDrfTransProdID.getItemAt(0));
        //        cboDrfTransProdID.setSelectedItem(cboDrfTransProdID.getItemAt(0));
    }
    public void update(Observable observed, Object arg) {
//        commented by nikhil
//        updateDividendCalcUI();
    }
    ///* Auto Generated Method - getMandatoryHashMap()
    //   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    private void setMaximumLength(){
        txtTotalAmount.setMaxLength(14);
        txtResolutionNo.setMaxLength(14);
        txtRemarks.setAllowAll(true);
        txtResolutionNo.setAllowAll(true);
        txtDebitGl.setAllowAll(true);
        txtPayableGl.setAllowAll(true);
        txtTotalAmount.setValidation(new CurrencyValidation());
        txtDividendPercent.setValidation(new NumericValidation(3,2));
    }
    
    private void initTableData(){
        tblShareDividendCalculation.setModel(observable.getTblShareDividendCalculation());
        
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
        observable = new ShareDividendCalculationOB(1);
        observable.addObserver(this);
    }
     /* Auto Generated Method - setMandatoryHashMap()
      
//ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
//
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtDrfTransMemberNo", new Boolean(true));
        mandatoryMap.put("txtDrfTransName", new Boolean(true));
        mandatoryMap.put("txtDrfTransAmount", new Boolean(true));
        mandatoryMap.put("cboDrfTransProdID",new Boolean(true));
        
    }
    /* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        objMandatoryRB = new ShareDividendCalculationMRB();
        //        txtDrfTransAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDrfTransAmount"));
        //        cboDrfTransProdID.setHelpMessage(lblMsg,objMandatoryRB.getString("cboDrfTransProdID"));
        //        txtDrfTransName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDrfTransName"));
        //        txtDrfTransMemberNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDrfTransMemberNo"));
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
        //        lblStatus.setName("lblStatus");
        //        panStatus.setName("panStatus");
        //        tblShareDividendCalculation.setName("tblShareDividendCalculation");
        //        txtDrfTransMemberNo.setName("txtDrfTransMemberNo");
        //        txtDrfTransName.setName("txtDrfTransName");
        //        txtDrfTransAmount.setName("txtDrfTransAmount");
        //        cboDrfTransProdID.setName("cboDrfTransProdID");
        //        lblDrfTransProdID.setName("lblDrfTransProdID");
    }
    
    private void internationalize() {
        resourceBundle = new ShareDividendCalculationRB();
        btnClose.setText(resourceBundle.getString("btnClose"));
        //        lblDrfTransAmount.setText(resourceBundle.getString("lblDrfTransAmount"));
        //        lblDrfTransAddress.setText(resourceBundle.getString("lblDrfTransAddress"));
        //        lblDrfTransMemberNo.setText(resourceBundle.getString("lblDrfTransMemberNo"));
        //        lblDueAmtPayment.setText(resourceBundle.getString("lblDueAmtPayment"));
        //        ((javax.swing.border.TitledBorder)panDrfTransDetails.getBorder()).setTitle(resourceBundle.getString("panDrfTransDetails"));
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
    }
    
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoDrfTransaction = new com.see.truetransact.uicomponent.CButtonGroup();
        panShareDividendCalculationDetails = new com.see.truetransact.uicomponent.CPanel();
        tabShareDividendCalculation = new com.see.truetransact.uicomponent.CTabbedPane();
        panShareDividendCalculation = new com.see.truetransact.uicomponent.CPanel();
        panDrfTransList = new com.see.truetransact.uicomponent.CPanel();
        panDrfTransDetails = new com.see.truetransact.uicomponent.CPanel();
        lblShareClass = new com.see.truetransact.uicomponent.CLabel();
        cboShareClass = new com.see.truetransact.uicomponent.CComboBox();
        panProcessType = new com.see.truetransact.uicomponent.CPanel();
        lblToPeriod = new com.see.truetransact.uicomponent.CLabel();
        tdtFromPeriod = new com.see.truetransact.uicomponent.CDateField();
        lblFromPeriod = new com.see.truetransact.uicomponent.CLabel();
        tdtToPeriod = new com.see.truetransact.uicomponent.CDateField();
        panDebitGl = new com.see.truetransact.uicomponent.CPanel();
        txtDebitGl = new com.see.truetransact.uicomponent.CTextField();
        btnDebitGl = new com.see.truetransact.uicomponent.CButton();
        lblDebitGl = new com.see.truetransact.uicomponent.CLabel();
        panPayableGl = new com.see.truetransact.uicomponent.CPanel();
        txtPayableGl = new com.see.truetransact.uicomponent.CTextField();
        btnPayableGl = new com.see.truetransact.uicomponent.CButton();
        lblPayableGl = new com.see.truetransact.uicomponent.CLabel();
        panDrfTransDetails1 = new com.see.truetransact.uicomponent.CPanel();
        lblResolutionNo = new com.see.truetransact.uicomponent.CLabel();
        lblDividendPercent = new com.see.truetransact.uicomponent.CLabel();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        txtRemarks = new com.see.truetransact.uicomponent.CTextField();
        txtDividendPercent = new com.see.truetransact.uicomponent.CTextField();
        txtResolutionNo = new com.see.truetransact.uicomponent.CTextField();
        tdtResolutionDate = new com.see.truetransact.uicomponent.CDateField();
        lblResolutionDate = new com.see.truetransact.uicomponent.CLabel();
        btnCalculate = new com.see.truetransact.uicomponent.CButton();
        btnClear = new com.see.truetransact.uicomponent.CButton();
        chkClosedMem = new com.see.truetransact.uicomponent.CCheckBox();
        panProcessType1 = new com.see.truetransact.uicomponent.CPanel();
        btnSaveTransaction = new com.see.truetransact.uicomponent.CButton();
        txtTotalAmount = new com.see.truetransact.uicomponent.CTextField();
        lblTotalAmount = new com.see.truetransact.uicomponent.CLabel();
        srpShareDividendCalculation = new com.see.truetransact.uicomponent.CScrollPane();
        tblShareDividendCalculation = new com.see.truetransact.uicomponent.CTable();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        tbrOperativeAcctProduct = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace51 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace52 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace53 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace54 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace55 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace56 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        lblSpace57 = new com.see.truetransact.uicomponent.CLabel();
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
        setMaximumSize(new java.awt.Dimension(800, 625));
        setMinimumSize(new java.awt.Dimension(800, 625));
        setPreferredSize(new java.awt.Dimension(800, 625));

        panShareDividendCalculationDetails.setMaximumSize(new java.awt.Dimension(650, 520));
        panShareDividendCalculationDetails.setMinimumSize(new java.awt.Dimension(650, 520));
        panShareDividendCalculationDetails.setPreferredSize(new java.awt.Dimension(650, 520));
        panShareDividendCalculationDetails.setLayout(new java.awt.GridBagLayout());

        panShareDividendCalculation.setMinimumSize(new java.awt.Dimension(830, 313));
        panShareDividendCalculation.setPreferredSize(new java.awt.Dimension(830, 313));
        panShareDividendCalculation.setLayout(new java.awt.GridBagLayout());

        panDrfTransList.setMinimumSize(new java.awt.Dimension(750, 170));
        panDrfTransList.setPreferredSize(new java.awt.Dimension(750, 170));
        panDrfTransList.setLayout(new java.awt.GridBagLayout());

        panDrfTransDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Dividend Calculation"));
        panDrfTransDetails.setMinimumSize(new java.awt.Dimension(400, 130));
        panDrfTransDetails.setPreferredSize(new java.awt.Dimension(400, 130));
        panDrfTransDetails.setLayout(new java.awt.GridBagLayout());

        lblShareClass.setText("Share Class");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panDrfTransDetails.add(lblShareClass, gridBagConstraints);

        cboShareClass.setMinimumSize(new java.awt.Dimension(100, 21));
        cboShareClass.setName("cboProfession"); // NOI18N
        cboShareClass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboShareClassActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 11, 1, 4);
        panDrfTransDetails.add(cboShareClass, gridBagConstraints);

        panProcessType.setMinimumSize(new java.awt.Dimension(380, 25));
        panProcessType.setName("panMaritalStatus"); // NOI18N
        panProcessType.setPreferredSize(new java.awt.Dimension(380, 25));
        panProcessType.setLayout(new java.awt.GridBagLayout());

        lblToPeriod.setText("To Period");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 5, 1, 4);
        panProcessType.add(lblToPeriod, gridBagConstraints);

        tdtFromPeriod.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtFromPeriodFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 15);
        panProcessType.add(tdtFromPeriod, gridBagConstraints);

        lblFromPeriod.setText("From Period");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panProcessType.add(lblFromPeriod, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 8);
        panProcessType.add(tdtToPeriod, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(10, 9, 5, 0);
        panDrfTransDetails.add(panProcessType, gridBagConstraints);

        panDebitGl.setMinimumSize(new java.awt.Dimension(130, 21));
        panDebitGl.setPreferredSize(new java.awt.Dimension(130, 21));
        panDebitGl.setLayout(new java.awt.GridBagLayout());

        txtDebitGl.setEditable(false);
        txtDebitGl.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panDebitGl.add(txtDebitGl, gridBagConstraints);

        btnDebitGl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnDebitGl.setToolTipText("From Account");
        btnDebitGl.setMinimumSize(new java.awt.Dimension(21, 21));
        btnDebitGl.setPreferredSize(new java.awt.Dimension(21, 21));
        btnDebitGl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDebitGlActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panDebitGl.add(btnDebitGl, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 0, 0);
        panDrfTransDetails.add(panDebitGl, gridBagConstraints);

        lblDebitGl.setText("Dividend Debit GL");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panDrfTransDetails.add(lblDebitGl, gridBagConstraints);

        panPayableGl.setMinimumSize(new java.awt.Dimension(130, 21));
        panPayableGl.setPreferredSize(new java.awt.Dimension(130, 21));
        panPayableGl.setLayout(new java.awt.GridBagLayout());

        txtPayableGl.setEditable(false);
        txtPayableGl.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panPayableGl.add(txtPayableGl, gridBagConstraints);

        btnPayableGl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnPayableGl.setToolTipText("From Account");
        btnPayableGl.setMinimumSize(new java.awt.Dimension(21, 21));
        btnPayableGl.setPreferredSize(new java.awt.Dimension(21, 21));
        btnPayableGl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPayableGlActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPayableGl.add(btnPayableGl, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 0, 0);
        panDrfTransDetails.add(panPayableGl, gridBagConstraints);

        lblPayableGl.setText("Dividend Payable GL");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panDrfTransDetails.add(lblPayableGl, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panDrfTransList.add(panDrfTransDetails, gridBagConstraints);

        panDrfTransDetails1.setBorder(javax.swing.BorderFactory.createTitledBorder("Dividend Calculation"));
        panDrfTransDetails1.setMinimumSize(new java.awt.Dimension(350, 130));
        panDrfTransDetails1.setPreferredSize(new java.awt.Dimension(350, 130));
        panDrfTransDetails1.setLayout(new java.awt.GridBagLayout());

        lblResolutionNo.setText("Resolution No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panDrfTransDetails1.add(lblResolutionNo, gridBagConstraints);

        lblDividendPercent.setText("Dividend Percent");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panDrfTransDetails1.add(lblDividendPercent, gridBagConstraints);

        lblRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panDrfTransDetails1.add(lblRemarks, gridBagConstraints);

        txtRemarks.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 12, 1, 0);
        panDrfTransDetails1.add(txtRemarks, gridBagConstraints);

        txtDividendPercent.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 12, 1, 0);
        panDrfTransDetails1.add(txtDividendPercent, gridBagConstraints);

        txtResolutionNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 12, 1, 0);
        panDrfTransDetails1.add(txtResolutionNo, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 12, 1, 8);
        panDrfTransDetails1.add(tdtResolutionDate, gridBagConstraints);

        lblResolutionDate.setText("Resolution Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 5, 1, 0);
        panDrfTransDetails1.add(lblResolutionDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        panDrfTransList.add(panDrfTransDetails1, gridBagConstraints);

        btnCalculate.setText("Load");
        btnCalculate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalculateActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 11);
        panDrfTransList.add(btnCalculate, gridBagConstraints);

        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnClear.setToolTipText("Cancel");
        btnClear.setMaximumSize(new java.awt.Dimension(51, 27));
        btnClear.setMinimumSize(new java.awt.Dimension(51, 27));
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 14, 0, 0);
        panDrfTransList.add(btnClear, gridBagConstraints);

        chkClosedMem.setText("Dividend To Closed Member's");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 14, 0, 0);
        panDrfTransList.add(chkClosedMem, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panShareDividendCalculation.add(panDrfTransList, gridBagConstraints);

        panProcessType1.setMinimumSize(new java.awt.Dimension(380, 25));
        panProcessType1.setName("panMaritalStatus"); // NOI18N
        panProcessType1.setPreferredSize(new java.awt.Dimension(380, 25));
        panProcessType1.setLayout(new java.awt.GridBagLayout());

        btnSaveTransaction.setText("Save");
        btnSaveTransaction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveTransactionActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 73, 4, 11);
        panProcessType1.add(btnSaveTransaction, gridBagConstraints);

        txtTotalAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 12, 1, 0);
        panProcessType1.add(txtTotalAmount, gridBagConstraints);

        lblTotalAmount.setText("Total");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panProcessType1.add(lblTotalAmount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 9, 0, 0);
        panShareDividendCalculation.add(panProcessType1, gridBagConstraints);

        srpShareDividendCalculation.setMinimumSize(new java.awt.Dimension(750, 255));
        srpShareDividendCalculation.setPreferredSize(new java.awt.Dimension(750, 335));

        tblShareDividendCalculation.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Select", "Cust ID", "Account No", "Name", "Dep AmT", "Dep Date", "Mat Date", "From Date", "To Date", "Interest", "SI A/c No"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblShareDividendCalculation.setPreferredScrollableViewportSize(new java.awt.Dimension(806, 331));
        srpShareDividendCalculation.setViewportView(tblShareDividendCalculation);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panShareDividendCalculation.add(srpShareDividendCalculation, gridBagConstraints);

        tabShareDividendCalculation.addTab("Share Divident Calculation", panShareDividendCalculation);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panShareDividendCalculationDetails.add(tabShareDividendCalculation, gridBagConstraints);

        getContentPane().add(panShareDividendCalculationDetails, java.awt.BorderLayout.CENTER);

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

        lblSpace51.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace51.setText("     ");
        lblSpace51.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace51.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace51.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace51);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnEdit);

        lblSpace52.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace52.setText("     ");
        lblSpace52.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace52.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace52.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace52);

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

        lblSpace53.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace53.setText("     ");
        lblSpace53.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace53.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace53.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace53);

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

        lblSpace54.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace54.setText("     ");
        lblSpace54.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace54.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace54.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace54);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnException);

        lblSpace55.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace55.setText("     ");
        lblSpace55.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace55.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace55.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace55);

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

        lblSpace56.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace56.setText("     ");
        lblSpace56.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace56.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace56.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace56);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnClose);

        lblSpace57.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace57.setText("     ");
        lblSpace57.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace57.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace57.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace57);

        btnDateChange.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/denomination.gif"))); // NOI18N
        btnDateChange.setToolTipText("Exception");
        tbrOperativeAcctProduct.add(btnDateChange);

        getContentPane().add(tbrOperativeAcctProduct, java.awt.BorderLayout.NORTH);

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

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        btnCancelActionPerformed();
        btnNewActionPerformed();
        btnSaveTransaction.setEnabled(false);
        btnCalculate.setEnabled(true);
        setModified(false);
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnSaveTransactionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveTransactionActionPerformed
        // TODO add your handling code here:
        saveMode = "SAVE";
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
//        System.out.println("@#$@#$@#$observable.getGetShareDivMap()"+observable.getGetShareDetailsMap());
        btnSaveActionPerformed();
    }//GEN-LAST:event_btnSaveTransactionActionPerformed

    private void btnCalculateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalculateActionPerformed
        // TODO add your handling code here:
        saveMode = "LOAD";
        btnSaveActionPerformed();
        tblShareDividendCalculation.setModel(observable.getTblShareDividendCalculation());
        txtTotalAmount.setText(observable.getTxtTotalAmount());
        btnSaveTransaction.setEnabled(true);
        btnCalculate.setEnabled(false);
    }//GEN-LAST:event_btnCalculateActionPerformed

    private void tdtFromPeriodFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtFromPeriodFocusLost
        // TODO add your handling code here:
//        System.out.println("#@$@#$@$##getShareDetails"+getShareDetails);
        if(getShareDetails != null && getShareDetails.size() > 0){
            HashMap shareDetsMap = (HashMap) getShareDetails.get(0);
//            System.out.println("#@$@#$@$@$shareDetsMap"+shareDetsMap);
            if (shareDetsMap.containsKey("DIVIDEND_CALC_FREQUENCY")) {
                int calcFreq = (int) CommonUtil.convertObjToInt(shareDetsMap.get("DIVIDEND_CALC_FREQUENCY"));
                Date fromPeriod = DateUtil.getDateMMDDYYYY(tdtFromPeriod.getDateValue());
                if (fromPeriod != null) {
                    int month = fromPeriod.getMonth();
                    int year = fromPeriod.getYear();
                    int days = fromPeriod.getDay();
                    if (calcFreq == 30) {
                        fromPeriod.setMonth(month + 1);
                    } else if (calcFreq == 90) {
                        fromPeriod.setMonth(month + 3);
                    } else if (calcFreq == 180) {
                        fromPeriod.setMonth(month + 6);
                    } else {
                        fromPeriod.setYear(year + 1);
                    }
                    System.out.println("@#$@#$@#$@#$fromPeriod" + fromPeriod);
                    tdtToPeriod.setDateValue(DateUtil.getStringDate(fromPeriod));
                }
                System.out.println("@#$@#$@#$@#$fromPeriod"+fromPeriod);
                tdtToPeriod.setDateValue(DateUtil.getStringDate(fromPeriod));
            }
        }
    }//GEN-LAST:event_tdtFromPeriodFocusLost

    private void btnPayableGlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPayableGlActionPerformed
        // TODO add your handling code here:
        viewType = PAYABLE_GL;
        callView(PAYABLE_GL);
    }//GEN-LAST:event_btnPayableGlActionPerformed

    private void btnDebitGlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDebitGlActionPerformed
        // TODO add your handling code here:
        viewType = DEBIT_GL;
        callView(DEBIT_GL);
    }//GEN-LAST:event_btnDebitGlActionPerformed

    private void cboShareClassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboShareClassActionPerformed
        // TODO add your handling code here:
        String shareClass = CommonUtil.convertObjToStr(observable.getCbmShareClass().getKeyForSelected());
        getShareDetails = new ArrayList();
        if (shareClass.length()>0) {
            HashMap shareDetailsMap = new HashMap();
            shareDetailsMap.put("SHARE_TYPE",shareClass);
            System.out.println("#@$@#$@#$shareClass"+shareClass);
            getShareDetails =(ArrayList) ClientUtil.executeQuery("getShareDetailsForDivCalc", shareDetailsMap); 
//            System.out.println("@#$@#$getShareDetails:"+getShareDetails);
            tdtToPeriod.setEnabled(false);
//            if(getShareDetails!= null && getShareDetails.size() > 0) {
//                shareDetailsMap = (HashMap) getShareDetails.get(0);
//                amount = CommonUtil.convertObjToDouble(shareDetailsMap.get("AMOUNT")).doubleValue();
//                productAmount = amount;
//                paymentAmount = CommonUtil.convertObjToDouble(shareDetailsMap.get("PAYMENT")).doubleValue();
//                productPaymentAmount = paymentAmount;
//            }
        }
    }//GEN-LAST:event_cboShareClassActionPerformed
            
    /* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
//        observable.setTxtDrfTransAmount(txtDrfTransAmount.getText());
//        observable.setCboDrfTransProdID(CommonUtil.convertObjToStr(cboDrfTransProdID.getSelectedItem()));
        if (chkClosedMem.isSelected()) {
            observable.setChkClosedMem("Y");
        } else {
            observable.setChkClosedMem("N");
        }
        observable.setTxtDebitGl(txtDebitGl.getText());
        observable.setTxtDividendPercent(txtDividendPercent.getText());
        observable.setTxtPayableGl(txtPayableGl.getText());
        observable.setTxtTotalAmount(txtTotalAmount.getText());
        observable.setTxtRemarks(txtRemarks.getText());
        observable.setTxtResolutionNo(txtResolutionNo.getText());
        observable.setTdtFromPeriod(tdtFromPeriod.getDateValue());
        observable.setTdtResolutionDate(tdtResolutionDate.getDateValue());
        observable.setTdtToPeriod(tdtToPeriod.getDateValue());
        observable.setCboShareClass(CommonUtil.convertObjToStr(observable.getCbmShareClass().getKeyForSelected()));
    }
    private void updateDividendCalcUI(){ 
        cboShareClass.setSelectedItem(observable.getCboShareClass());
        txtDebitGl.setText(observable.getTxtDebitGl());
        txtPayableGl.setText(observable.getTxtPayableGl());
        txtTotalAmount.setText(observable.getTxtTotalAmount());
        tdtFromPeriod.setDateValue(observable.getTdtFromPeriod());
        tdtToPeriod.setDateValue(observable.getTdtToPeriod());
        txtDividendPercent.setText(observable.getTxtDividendPercent());
        txtResolutionNo.setText(observable.getTxtResolutionNo());
        tdtResolutionDate.setDateValue(observable.getTdtResolutionDate());
        txtRemarks.setText(observable.getTxtRemarks());
    }
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        
//        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_VIEW);
//        transactionUI.cancelAction(false);
        resetUI();
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        observable.setStatus();
        callView(EDIT);
        lblStatus.setText("Delete");
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        ClientUtil.enableDisable(panDrfTransDetails,false);
        btnSave.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
        btnEdit.setEnabled(false);
    }//GEN-LAST:event_btnViewActionPerformed
    private void resetUI(){
        observable.resetDividendCalcDetails();
        observable.resetDividendCalcListTable();
    }
    
    private void callView(int viewType){
        observable.setStatus();
        final HashMap viewMap = new HashMap();
        ArrayList lst = new ArrayList();
        HashMap whereMap = new HashMap();
        whereMap.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
        if(viewType == DEBIT_GL || viewType == PAYABLE_GL){
            viewMap.put(CommonConstants.MAP_NAME, "getSelectAccountHead");
        }
        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        new ViewAll(this,viewMap).show();
    }
    
    public void fillData(Object obj){
        try{
            HashMap hashMap=(HashMap)obj;
//            System.out.println("### fillData Hash : "+hashMap);
//            System.out.println("### fillData viewType: "+viewType);
            if(viewType == DEBIT_GL){
                txtDebitGl.setText(CommonUtil.convertObjToStr(hashMap.get("AC_HD_ID")));
            }
            if(viewType == PAYABLE_GL){
                txtPayableGl.setText(CommonUtil.convertObjToStr(hashMap.get("AC_HD_ID")));
            }
            hashMap = null;
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
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
          viewType = ClientConstants.ACTIONTYPE_REJECT;
          authorizeStatus(CommonConstants.STATUS_REJECTED);
          btnReject.setEnabled(true);
          btnException.setEnabled(false);
          btnSave.setEnabled(false);
          btnCancel.setEnabled(true);
          btnAuthorize.setEnabled(false);
    }//GEN-LAST:event_btnRejectActionPerformed
    private void resetTransactionUI(){
//        transactionUI.setButtonEnableDisable(true);
//        transactionUI.cancelAction(false);
//        transactionUI.resetObjects();
    }
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
         setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        viewType = ClientConstants.ACTIONTYPE_DELETE;
        observable.setStatus();
//        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_DELETE);
//        transactionUI.cancelAction(false);
        resetUI();
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        observable.setStatus();
        callView(DELETE);
//        lblStatus.setText("Delete");
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        btnSave.setEnabled(true);
        btnDelete.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
//        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_VIEW);
//        transactionUI.cancelAction(false);
        resetUI();
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        observable.setStatus();
        callView(EDIT); 
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        viewType = ClientConstants.ACTIONTYPE_EDIT;
        observable.setStatus();
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        ClientUtil.enableDisable(panDrfTransDetails,false);
        btnSave.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
        btnEdit.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
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
        if (viewType== ClientConstants.ACTIONTYPE_AUTHORIZE && isFilled){
                
                ArrayList arrList = new ArrayList();
                HashMap authorizeMap = new HashMap();
                HashMap singleAuthorizeMap = new HashMap();
                singleAuthorizeMap.put("STATUS", authorizeStatus);
                singleAuthorizeMap.put("AUTH_BY", TrueTransactMain.USER_ID);
                singleAuthorizeMap.put("AUTH_DT", curDate);
                singleAuthorizeMap.put("DRF_TRANS_ID", observable.getDrfTransID());
                singleAuthorizeMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
                arrList.add(singleAuthorizeMap);
                authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
                authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
                authorize(authorizeMap,observable.getDrfTransID()); 
                viewType = -1;
                super.setOpenForEditBy(observable.getStatusBy());
                singleAuthorizeMap = null;
                authorizeMap = null;
            
        }else {
            //__ To Save the data in the Internal Frame...
            HashMap whereMap = new HashMap();
            setModified(true);
            whereMap.put(CommonConstants.MAP_NAME, "getDrfTransferAuthMode");
            panEditDelete = ShareDividendCalculation;
            pan = ShareDividendCalculation;
            whereMap.put(CommonConstants.HIERARCHY_ID, ProxyParameters.HIERARCHY_ID);
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            viewType = AUTHORIZE;
            isFilled = false;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, whereMap);
            authorizeUI.show();
            whereMap = null;
        }
    }
    
    public void authorize(HashMap map,String id) {
//        System.out.println("Authorize Map : " + map);
        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
            observable.set_authorizeMap(map);
            observable.doAction(saveMode);
            if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED){
//                super.setOpenForEditBy(observable.getStatusBy());
//                super.removeEditLock(id);
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
        
        
        resourceBundle = new ShareDividendCalculationRB();
        final String shareAcctMandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panDrfTransDetails);
        StringBuffer strBMandatory = new StringBuffer();
        
        int transactionSize = 0 ;
        if(shareAcctMandatoryMessage.length()>0){
            strBMandatory.append(shareAcctMandatoryMessage);
        }
        String strMandatory = strBMandatory.toString();
        //--- checks whether the Mandatory fields are entered
        if(strMandatory.length()>0){        //--- if all the mandatory fields are not entered,
            CommonMethods.displayAlert(strMandatory);     //--- display the alert
        }else if(strMandatory.length()==0){ //--- if all the values are entered, save the data
            //Call transaction screen here
            //If transactions exist, proceed to save them
            if (transactionSize  > 0/* && rdoSharewithDrawal.isSelected()==true*/) {
            } else {
                savePerformed();
                observable.setStatus();
                observable.setResultStatus();
                lblStatus.setText(observable.getLblStatus());
            }resourceBundle = null;
        } else {
            CommonMethods.displayAlert(resourceBundle.getString("saveAcctDet"));
        }
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void btnSaveActionPerformed() {
        // TODO add your handling code here:
        resourceBundle = new ShareDividendCalculationRB();
        final String shareAcctMandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panDrfTransDetails);
        StringBuffer strBMandatory = new StringBuffer();
        int transactionSize = 0 ;
        if(shareAcctMandatoryMessage.length()>0){
            strBMandatory.append(shareAcctMandatoryMessage);
        }
        String strMandatory = strBMandatory.toString();
        //--- checks whether the Mandatory fields are entered
        if(strMandatory.length()>0){        //--- if all the mandatory fields are not entered,
            CommonMethods.displayAlert(strMandatory);     //--- display the alert
        }else if(strMandatory.length()==0){ //--- if all the values are entered, save the data
            //Call transaction screen here
            //If transactions exist, proceed to save them
            if (transactionSize  > 0/* && rdoSharewithDrawal.isSelected()==true*/) {
            } else {
                savePerformed();
                observable.setStatus();
                observable.setResultStatus();
                lblStatus.setText(observable.getLblStatus());
            }resourceBundle = null;
        } else {
            CommonMethods.displayAlert(resourceBundle.getString("saveAcctDet"));
        }
    }
    
    
    private void savePerformed(){
        try{
            updateOBFields();
//            System.out.println("#@$@$#@#getShareDetails"+getShareDetails);
            if(getShareDetails!= null && getShareDetails.size() >0 && saveMode.equals("LOAD")){
                HashMap getShareDetailsMap = (HashMap) getShareDetails.get(0);
                observable.setGetShareDetailsMap(getShareDetailsMap);
            }
            observable.setResult(observable.getActionType());
            observable.doAction(saveMode);
            if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED) {
                if (observable.getProxyReturnMap()!=null) {
                    if (observable.getProxyReturnMap().containsKey("FIRST_TRANS_ID") || observable.getProxyReturnMap().containsKey("LAST_TRANS_ID")) {
                        displayReportDetail(observable.getProxyReturnMap());
                    }
                }
            }
            if(saveMode !="LOAD"){
                observable.makeToNull();
                btnCancelActionPerformed(null);
            }
            observable.ttNotifyObservers();
            observable.setResultStatus();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void displayReportDetail(HashMap proxyResultMap) {
//        System.out.println("###proxyResultMap : "+proxyResultMap);
        int yesNo = 0;
        String[] options = {"Yes", "No"};
        yesNo = COptionPane.showOptionDialog(null,"Do you want to print?", CommonConstants.WARNINGTITLE,
        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
        null, options, options[0]);
        System.out.println("#$#$$ yesNo : "+yesNo);
        if (yesNo==0) {
            TTIntegration ttIntgration = null;
            HashMap paramMap = new HashMap();
            paramMap.put("FromTransId", proxyResultMap.get("FIRST_TRANS_ID"));
            paramMap.put("ToTransId", proxyResultMap.get("LAST_TRANS_ID"));
            paramMap.put("TransDt", curDate);
            paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
            ttIntgration.setParam(paramMap);
            String reportName = "";
            reportName = "ReceiptPayment";
            ttIntgration.integrationForPrint(reportName, false);
        }
    }
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        setModified(true);
        resetUI();               // to Reset all the Fields and Status in UI...
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panShareDividendCalculationDetails, true);
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
//        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
//        transactionUI.setButtonEnableDisable(true);
//        transactionUI.cancelAction(false);
//        transactionUI.resetObjects();
//        transactionUI.setCallingApplicantName("");
//        transactionUI.setCallingAmount("");
    }//GEN-LAST:event_btnNewActionPerformed
    private void btnNewActionPerformed() {
//        setModified(true);
        resetUI();               // to Reset all the Fields and Status in UI...
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panShareDividendCalculationDetails, true);
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
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW); 
        setModified(false);
    }
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
//        deletescreenLock();
        observable.resetForm();
        observable.setAuthorizeStatus("");
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panShareDividendCalculationDetails, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
//        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_CANCEL);
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
        saveMode = "";
//        transactionUI.setButtonEnableDisable(true);
//        transactionUI.cancelAction(false);
//        transactionUI.resetObjects();
//        transactionUI.setCallingApplicantName("");
//        transactionUI.setCallingAmount("");
        isFilled = false;
//        chkDueAmtPayment.setSelected(false);
    }//GEN-LAST:event_btnCancelActionPerformed
    private void btnCancelActionPerformed() {
        // TODO add your handling code here:
        setModified(false);
        deletescreenLock();
        observable.resetForm();
        observable.setAuthorizeStatus("");
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panShareDividendCalculationDetails, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
//        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_CANCEL);
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
        saveMode = "";
//        transactionUI.setButtonEnableDisable(true);
//        transactionUI.cancelAction(false);
//        transactionUI.resetObjects();
//        transactionUI.setCallingApplicantName("");
//        transactionUI.setCallingAmount("");
        isFilled = false;
//        chkDueAmtPayment.setSelected(false);
    }
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
    private com.see.truetransact.uicomponent.CButton btnCalculate;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClear;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDateChange;
    private com.see.truetransact.uicomponent.CButton btnDebitGl;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPayableGl;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnSaveTransaction;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboShareClass;
    private com.see.truetransact.uicomponent.CCheckBox chkClosedMem;
    private com.see.truetransact.uicomponent.CLabel lblDebitGl;
    private com.see.truetransact.uicomponent.CLabel lblDividendPercent;
    private com.see.truetransact.uicomponent.CLabel lblFromPeriod;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblPayableGl;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
    private com.see.truetransact.uicomponent.CLabel lblResolutionDate;
    private com.see.truetransact.uicomponent.CLabel lblResolutionNo;
    private com.see.truetransact.uicomponent.CLabel lblShareClass;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace51;
    private com.see.truetransact.uicomponent.CLabel lblSpace52;
    private com.see.truetransact.uicomponent.CLabel lblSpace53;
    private com.see.truetransact.uicomponent.CLabel lblSpace54;
    private com.see.truetransact.uicomponent.CLabel lblSpace55;
    private com.see.truetransact.uicomponent.CLabel lblSpace56;
    private com.see.truetransact.uicomponent.CLabel lblSpace57;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblToPeriod;
    private com.see.truetransact.uicomponent.CLabel lblTotalAmount;
    private com.see.truetransact.uicomponent.CMenuBar mbrCustomer;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panDebitGl;
    private com.see.truetransact.uicomponent.CPanel panDrfTransDetails;
    private com.see.truetransact.uicomponent.CPanel panDrfTransDetails1;
    private com.see.truetransact.uicomponent.CPanel panDrfTransList;
    private com.see.truetransact.uicomponent.CPanel panPayableGl;
    private com.see.truetransact.uicomponent.CPanel panProcessType;
    private com.see.truetransact.uicomponent.CPanel panProcessType1;
    private com.see.truetransact.uicomponent.CPanel panShareDividendCalculation;
    private com.see.truetransact.uicomponent.CPanel panShareDividendCalculationDetails;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CButtonGroup rdoDrfTransaction;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CScrollPane srpShareDividendCalculation;
    private com.see.truetransact.uicomponent.CTabbedPane tabShareDividendCalculation;
    private com.see.truetransact.uicomponent.CTable tblShareDividendCalculation;
    private javax.swing.JToolBar tbrOperativeAcctProduct;
    private com.see.truetransact.uicomponent.CDateField tdtFromPeriod;
    private com.see.truetransact.uicomponent.CDateField tdtResolutionDate;
    private com.see.truetransact.uicomponent.CDateField tdtToPeriod;
    private com.see.truetransact.uicomponent.CTextField txtDebitGl;
    private com.see.truetransact.uicomponent.CTextField txtDividendPercent;
    private com.see.truetransact.uicomponent.CTextField txtPayableGl;
    private com.see.truetransact.uicomponent.CTextField txtRemarks;
    private com.see.truetransact.uicomponent.CTextField txtResolutionNo;
    private com.see.truetransact.uicomponent.CTextField txtTotalAmount;
    // End of variables declaration//GEN-END:variables
}