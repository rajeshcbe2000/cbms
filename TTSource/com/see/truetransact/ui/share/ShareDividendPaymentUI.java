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
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.ui.common.transaction.TransactionUI;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.ui.common.customer.MembershipLiabilityUI;
import com.see.truetransact.ui.common.report.PrintClass;
import com.see.truetransact.ui.common.viewall.AuthorizeListUI;
import com.see.truetransact.ui.common.viewall.NewAuthorizeListUI;
import com.see.truetransact.ui.salaryrecovery.AuthorizeListDebitUI;
import java.util.LinkedHashMap;
/** This form is used to manipulate FixedAssetsUI related functionality
 * @author nikhil
 */
//public class ShareDividendCalculationUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer, UIMandatoryField {
public class ShareDividendPaymentUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer, UIMandatoryField{
    
    
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.share.ShareDividendPaymentRB", ProxyParameters.LANGUAGE);
    ShareDividendPaymentMRB objMandatoryRB = new ShareDividendPaymentMRB();
    private HashMap mandatoryMap;
    private final static ClientParseException parseException = ClientParseException.getInstance();//Instance of ClientParseException to log the Exceptions
    private ShareDividendPaymentOB observable;
    private boolean selectMode = false;
    private Date curDate = null;
    private ArrayList getShareDetails;
    final int EDIT=0,DELETE=8,ACCNOCHEQUE=2,ACCNOSTOP=3,ACCNOLOOSE=4,VIEW=10,ECSSTOP=7;
    private int viewType=-1;
    private int BREAKAGE_ID = 1,MOVEMENT_ID = 2,FROM_ID =3,TO_ID =4,SALE_ID =5, AUTHORIZE=6;
    private int MEMBER_NO = 10,PAYABLE_GL = 11,MEMBER_NO_CLOSED=33;
    boolean isFilled = false;
    int updateTab=-1;
    private TransactionUI transactionUI = new TransactionUI();
    private boolean updateMode = false;
    private double amount = 0.0;
    private double productAmount = 0.0;
    private double paymentAmount = 0.0;
    private double productPaymentAmount = 0.0;
    boolean flag = false;
    private String viewTypeStr = ClientConstants.VIEW_TYPE_CANCEL;
    final int ShareDividendPayment=0;
    int pan=-1;
    int panEditDelete=-1;
    int view = -1;
    private String saveMode = "";
    private String paymentID = "";
    private String singleTransId = "";
    private int rejectFlag=0;
    boolean fromAuthorizeUI = false;
    boolean fromManagerAuthorizeUI = false;
    AuthorizeListUI authorizeListUI = null;
    AuthorizeListDebitUI ManagerauthorizeListUI=null;
    PrintClass print=new PrintClass();
    NewAuthorizeListUI newauthorizeListUI = null;
    boolean fromNewAuthorizeUI = false;
    
    public ShareDividendPaymentUI() {
        initComponents();
        initStartup();
        transactionUI.setSourceScreen("SHARE_DIVIDEND_PAYMENT");
        transactionUI.addToScreen(panTransaction);
        observable.setTransactionOB(transactionUI.getTransactionOB());
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
        transactionUI.setProdType();
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
        btnCancelActionPerformed();
        btnNew.setEnabled(true);
        btnDebitGl.setEnabled(false);
        btnView.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        txtDivAmount.setEnabled(false);
        txtDeviDate.setEnabled(false);
        txtTotalNoOfShare.setEnabled(false);
        txtTotalShareAmount.setEnabled(false);
        txtDivAmount.setValidation(new CurrencyValidation());
        txtTotalShareAmount.setValidation(new CurrencyValidation());
        panReprint.setVisible(false);
        btnRePrint.setVisible(true);
        btnMembershipLia.setEnabled(false);
    }
    private void initComponentData() {
        cboShareClass.setModel(observable.getCbmShareClass());
    }
    public void update(Observable observed, Object arg) {
        updateDividendCalcUI();
    }
    ///* Auto Generated Method - getMandatoryHashMap()
    //   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    
    private void setMaximumLength(){
        txtTotalAmount.setValidation(new CurrencyValidation());
        txtMemberNo.setAllowAll(true);
    }
    private void initTableData(){
        tblShareDividendCalculation.setModel(observable.getTblShareDividendPayment());
        
    }
    private void setButtonEnableDisable() {
    }
    
    private void setObservable(){
        /* Singleton pattern can't be implemented as there are two observers using the same observable*/
        // The parameter '1' indicates that the customer type is INDIVIDUAL
        observable = new ShareDividendPaymentOB(1);
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
        objMandatoryRB = new ShareDividendPaymentMRB();
    }
    
    
     /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        lblMsg.setName("lblMsg");
        lblSpace1.setName("lblSpace1");
    }
    
    private void internationalize() {
        //        resourceBundle = new ShareDividendPaymentRB();
        //        btnClose.setText(resourceBundle.getString("btnClose"));
        ////        lblDrfTransAmount.setText(resourceBundle.getString("lblDrfTransAmount"));
        ////        lblDrfTransAddress.setText(resourceBundle.getString("lblDrfTransAddress"));
        ////        lblDrfTransMemberNo.setText(resourceBundle.getString("lblDrfTransMemberNo"));
        ////        lblDueAmtPayment.setText(resourceBundle.getString("lblDueAmtPayment"));
        ////        ((javax.swing.border.TitledBorder)panDrfTransDetails.getBorder()).setTitle(resourceBundle.getString("panDrfTransDetails"));
        //        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        //        btnException.setText(resourceBundle.getString("btnException"));
        //        lblMsg.setText(resourceBundle.getString("lblMsg"));
        //        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        //        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        //        btnView.setText(resourceBundle.getString("btnView"));
        //        btnSave.setText(resourceBundle.getString("btnSave"));
        //        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        //        lblStatus.setText(resourceBundle.getString("lblStatus"));
        //        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        //        lblSpace5.setText(resourceBundle.getString("lblSpace5"));
        //        btnDelete.setText(resourceBundle.getString("btnDelete"));
        //        btnReject.setText(resourceBundle.getString("btnReject"));
        //        btnEdit.setText(resourceBundle.getString("btnEdit"));
        //        btnNew.setText(resourceBundle.getString("btnNew"));
        //        btnCancel.setText(resourceBundle.getString("btnCancel"));
        //        btnPrint.setText(resourceBundle.getString("btnPrint"));
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
        panDebitGl = new com.see.truetransact.uicomponent.CPanel();
        txtMemberNo = new com.see.truetransact.uicomponent.CTextField();
        btnDebitGl = new com.see.truetransact.uicomponent.CButton();
        lblDebitGl = new com.see.truetransact.uicomponent.CLabel();
        lblMemberName = new com.see.truetransact.uicomponent.CLabel();
        lblMemberStreet = new com.see.truetransact.uicomponent.CLabel();
        lblMemberArea = new com.see.truetransact.uicomponent.CLabel();
        chkClosedMem = new com.see.truetransact.uicomponent.CCheckBox();
        lblChkClosedMemNo = new com.see.truetransact.uicomponent.CLabel();
        panDrfTransDetails2 = new com.see.truetransact.uicomponent.CPanel();
        srpShareDividendCalculation = new com.see.truetransact.uicomponent.CScrollPane();
        tblShareDividendCalculation = new com.see.truetransact.uicomponent.CTable();
        panProcessType1 = new com.see.truetransact.uicomponent.CPanel();
        txtTotalAmount = new com.see.truetransact.uicomponent.CTextField();
        lblTotalAmount = new com.see.truetransact.uicomponent.CLabel();
        panDrfTransDetails1 = new com.see.truetransact.uicomponent.CPanel();
        lblShareClass1 = new com.see.truetransact.uicomponent.CLabel();
        lblDebitGl1 = new com.see.truetransact.uicomponent.CLabel();
        txtTotalNoOfShare = new com.see.truetransact.uicomponent.CTextField();
        txtTotalShareAmount = new com.see.truetransact.uicomponent.CTextField();
        panDrfTransDetails3 = new com.see.truetransact.uicomponent.CPanel();
        lblShareClass2 = new com.see.truetransact.uicomponent.CLabel();
        lblDebitGl2 = new com.see.truetransact.uicomponent.CLabel();
        txtDeviDate = new com.see.truetransact.uicomponent.CTextField();
        txtDivAmount = new com.see.truetransact.uicomponent.CTextField();
        btnRePrint = new com.see.truetransact.uicomponent.CButton();
        btnMembershipLia = new com.see.truetransact.uicomponent.CButton();
        panTransaction = new com.see.truetransact.uicomponent.CPanel();
        panReprint = new com.see.truetransact.uicomponent.CPanel();
        panReprintBtn = new com.see.truetransact.uicomponent.CPanel();
        btnCancel1 = new com.see.truetransact.uicomponent.CButton();
        btnPrint1 = new com.see.truetransact.uicomponent.CButton();
        btnReprintClose = new com.see.truetransact.uicomponent.CButton();
        panReprintDetails = new com.see.truetransact.uicomponent.CPanel();
        lblFromTransID = new com.see.truetransact.uicomponent.CLabel();
        lblToTransID = new com.see.truetransact.uicomponent.CLabel();
        lblTransDate = new com.see.truetransact.uicomponent.CLabel();
        tdtTransDate = new com.see.truetransact.uicomponent.CDateField();
        txtFromTransID = new com.see.truetransact.uicomponent.CTextField();
        txtToTransID = new com.see.truetransact.uicomponent.CTextField();
        panReprintTransType = new com.see.truetransact.uicomponent.CPanel();
        chkReprintCash = new com.see.truetransact.uicomponent.CCheckBox();
        chkReprintTransfer = new com.see.truetransact.uicomponent.CCheckBox();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        tbrShareProduct = new javax.swing.JToolBar();
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
        mbrMain = new com.see.truetransact.uicomponent.CMenuBar();
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
        setMaximumSize(new java.awt.Dimension(850, 655));
        setMinimumSize(new java.awt.Dimension(890, 655));
        setPreferredSize(new java.awt.Dimension(850, 655));

        panShareDividendCalculationDetails.setMaximumSize(new java.awt.Dimension(650, 520));
        panShareDividendCalculationDetails.setMinimumSize(new java.awt.Dimension(650, 520));
        panShareDividendCalculationDetails.setPreferredSize(new java.awt.Dimension(650, 520));
        panShareDividendCalculationDetails.setLayout(new java.awt.GridBagLayout());

        panShareDividendCalculation.setMinimumSize(new java.awt.Dimension(830, 313));
        panShareDividendCalculation.setPreferredSize(new java.awt.Dimension(830, 313));
        panShareDividendCalculation.setLayout(new java.awt.GridBagLayout());

        panDrfTransList.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panDrfTransList.setMinimumSize(new java.awt.Dimension(785, 320));
        panDrfTransList.setPreferredSize(new java.awt.Dimension(785, 320));
        panDrfTransList.setLayout(new java.awt.GridBagLayout());

        panDrfTransDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Member Details"));
        panDrfTransDetails.setMinimumSize(new java.awt.Dimension(270, 190));
        panDrfTransDetails.setPreferredSize(new java.awt.Dimension(270, 190));
        panDrfTransDetails.setLayout(new java.awt.GridBagLayout());

        lblShareClass.setText("Share Class");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
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
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDrfTransDetails.add(cboShareClass, gridBagConstraints);

        panDebitGl.setMinimumSize(new java.awt.Dimension(130, 21));
        panDebitGl.setPreferredSize(new java.awt.Dimension(130, 21));
        panDebitGl.setLayout(new java.awt.GridBagLayout());

        txtMemberNo.setEditable(false);
        txtMemberNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMemberNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMemberNoActionPerformed(evt);
            }
        });
        txtMemberNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMemberNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panDebitGl.add(txtMemberNo, gridBagConstraints);

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
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panDrfTransDetails.add(panDebitGl, gridBagConstraints);

        lblDebitGl.setText("Member No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 2, 0);
        panDrfTransDetails.add(lblDebitGl, gridBagConstraints);

        lblMemberName.setForeground(new java.awt.Color(0, 51, 204));
        lblMemberName.setText("         ");
        lblMemberName.setMaximumSize(new java.awt.Dimension(46, 18));
        lblMemberName.setMinimumSize(new java.awt.Dimension(46, 18));
        lblMemberName.setPreferredSize(new java.awt.Dimension(46, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 4, 4);
        panDrfTransDetails.add(lblMemberName, gridBagConstraints);

        lblMemberStreet.setForeground(new java.awt.Color(0, 51, 204));
        lblMemberStreet.setText("         ");
        lblMemberStreet.setMaximumSize(new java.awt.Dimension(46, 18));
        lblMemberStreet.setMinimumSize(new java.awt.Dimension(46, 18));
        lblMemberStreet.setPreferredSize(new java.awt.Dimension(46, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 4, 4);
        panDrfTransDetails.add(lblMemberStreet, gridBagConstraints);

        lblMemberArea.setForeground(new java.awt.Color(0, 51, 204));
        lblMemberArea.setText("         ");
        lblMemberArea.setMaximumSize(new java.awt.Dimension(67, 18));
        lblMemberArea.setMinimumSize(new java.awt.Dimension(67, 18));
        lblMemberArea.setPreferredSize(new java.awt.Dimension(67, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 4, 4);
        panDrfTransDetails.add(lblMemberArea, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 26);
        panDrfTransDetails.add(chkClosedMem, gridBagConstraints);

        lblChkClosedMemNo.setText("Consider Closed MemNo");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panDrfTransDetails.add(lblChkClosedMemNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panDrfTransList.add(panDrfTransDetails, gridBagConstraints);

        panDrfTransDetails2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panDrfTransDetails2.setMinimumSize(new java.awt.Dimension(465, 180));
        panDrfTransDetails2.setPreferredSize(new java.awt.Dimension(465, 180));
        panDrfTransDetails2.setLayout(new java.awt.GridBagLayout());

        srpShareDividendCalculation.setMaximumSize(new java.awt.Dimension(450, 225));
        srpShareDividendCalculation.setMinimumSize(new java.awt.Dimension(450, 225));
        srpShareDividendCalculation.setPreferredSize(new java.awt.Dimension(450, 125));

        tblShareDividendCalculation.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Select", "DividendID", "From Period", "To Period", "Amount"
            }
        ));
        tblShareDividendCalculation.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblShareDividendCalculation.setMaximumSize(new java.awt.Dimension(2147483647, 10000));
        tblShareDividendCalculation.setMinimumSize(new java.awt.Dimension(450, 260));
        tblShareDividendCalculation.setPreferredScrollableViewportSize(new java.awt.Dimension(500, 10000));
        tblShareDividendCalculation.setPreferredSize(new java.awt.Dimension(450, 260));
        tblShareDividendCalculation.setOpaque(false);
        tblShareDividendCalculation.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblShareDividendCalculationMouseClicked(evt);
            }
        });
        srpShareDividendCalculation.setViewportView(tblShareDividendCalculation);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panDrfTransDetails2.add(srpShareDividendCalculation, gridBagConstraints);

        panProcessType1.setMinimumSize(new java.awt.Dimension(380, 30));
        panProcessType1.setName("panMaritalStatus"); // NOI18N
        panProcessType1.setPreferredSize(new java.awt.Dimension(380, 30));
        panProcessType1.setLayout(new java.awt.GridBagLayout());

        txtTotalAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 7, 1, 0);
        panProcessType1.add(txtTotalAmount, gridBagConstraints);

        lblTotalAmount.setText("Total Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 239, 0, 0);
        panProcessType1.add(lblTotalAmount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 0, 1);
        panDrfTransDetails2.add(panProcessType1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 0);
        panDrfTransList.add(panDrfTransDetails2, gridBagConstraints);

        panDrfTransDetails1.setBorder(javax.swing.BorderFactory.createTitledBorder("Share Details"));
        panDrfTransDetails1.setMinimumSize(new java.awt.Dimension(270, 90));
        panDrfTransDetails1.setPreferredSize(new java.awt.Dimension(270, 90));
        panDrfTransDetails1.setLayout(new java.awt.GridBagLayout());

        lblShareClass1.setText("No Of Shares");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panDrfTransDetails1.add(lblShareClass1, gridBagConstraints);

        lblDebitGl1.setText("Share Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panDrfTransDetails1.add(lblDebitGl1, gridBagConstraints);

        txtTotalNoOfShare.setBackground(new java.awt.Color(212, 208, 200));
        txtTotalNoOfShare.setEditable(false);
        txtTotalNoOfShare.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtTotalNoOfShare.setAllowAll(true);
        txtTotalNoOfShare.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 4);
        panDrfTransDetails1.add(txtTotalNoOfShare, gridBagConstraints);

        txtTotalShareAmount.setBackground(new java.awt.Color(212, 208, 200));
        txtTotalShareAmount.setEditable(false);
        txtTotalShareAmount.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTotalShareAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 5, 4);
        panDrfTransDetails1.add(txtTotalShareAmount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 0);
        panDrfTransList.add(panDrfTransDetails1, gridBagConstraints);
        panDrfTransDetails1.getAccessibleContext().setAccessibleName("Share Amount");

        panDrfTransDetails3.setBorder(javax.swing.BorderFactory.createTitledBorder("Dividend Details"));
        panDrfTransDetails3.setMinimumSize(new java.awt.Dimension(270, 90));
        panDrfTransDetails3.setPreferredSize(new java.awt.Dimension(270, 90));
        panDrfTransDetails3.setLayout(new java.awt.GridBagLayout());

        lblShareClass2.setText("Last Dividend Paid On");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDrfTransDetails3.add(lblShareClass2, gridBagConstraints);

        lblDebitGl2.setText("Dividend Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDrfTransDetails3.add(lblDebitGl2, gridBagConstraints);

        txtDeviDate.setBackground(new java.awt.Color(212, 208, 200));
        txtDeviDate.setEditable(false);
        txtDeviDate.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtDeviDate.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDrfTransDetails3.add(txtDeviDate, gridBagConstraints);

        txtDivAmount.setBackground(new java.awt.Color(212, 208, 200));
        txtDivAmount.setEditable(false);
        txtDivAmount.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtDivAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDrfTransDetails3.add(txtDivAmount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        panDrfTransList.add(panDrfTransDetails3, gridBagConstraints);

        btnRePrint.setForeground(new java.awt.Color(255, 0, 51));
        btnRePrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnRePrint.setText("Reprint");
        btnRePrint.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        btnRePrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRePrintActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        panDrfTransList.add(btnRePrint, gridBagConstraints);

        btnMembershipLia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/members2.jpg"))); // NOI18N
        btnMembershipLia.setToolTipText("View MemberShip Liability");
        btnMembershipLia.setMaximumSize(new java.awt.Dimension(30, 30));
        btnMembershipLia.setMinimumSize(new java.awt.Dimension(30, 30));
        btnMembershipLia.setPreferredSize(new java.awt.Dimension(30, 30));
        btnMembershipLia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMembershipLiaActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panDrfTransList.add(btnMembershipLia, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panShareDividendCalculation.add(panDrfTransList, gridBagConstraints);

        panTransaction.setMinimumSize(new java.awt.Dimension(795, 235));
        panTransaction.setPreferredSize(new java.awt.Dimension(795, 235));
        panTransaction.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panShareDividendCalculation.add(panTransaction, gridBagConstraints);

        panReprint.setMinimumSize(new java.awt.Dimension(830, 250));
        panReprint.setPreferredSize(new java.awt.Dimension(830, 250));
        panReprint.setLayout(new java.awt.GridBagLayout());

        panReprintBtn.setMinimumSize(new java.awt.Dimension(515, 35));
        panReprintBtn.setPreferredSize(new java.awt.Dimension(515, 35));
        panReprintBtn.setLayout(new java.awt.GridBagLayout());

        btnCancel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel1.setText("CANCEL");
        btnCancel1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancel1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReprintBtn.add(btnCancel1, gridBagConstraints);

        btnPrint1.setForeground(new java.awt.Color(255, 0, 51));
        btnPrint1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint1.setText("Print");
        btnPrint1.setToolTipText("Print");
        btnPrint1.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        btnPrint1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrint1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReprintBtn.add(btnPrint1, gridBagConstraints);

        btnReprintClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnReprintClose.setText("Close");
        btnReprintClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReprintCloseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReprintBtn.add(btnReprintClose, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(30, 0, 0, 0);
        panReprint.add(panReprintBtn, gridBagConstraints);

        panReprintDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Reprint Details"));
        panReprintDetails.setMinimumSize(new java.awt.Dimension(415, 145));
        panReprintDetails.setPreferredSize(new java.awt.Dimension(415, 145));
        panReprintDetails.setLayout(new java.awt.GridBagLayout());

        lblFromTransID.setText("From Trans ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReprintDetails.add(lblFromTransID, gridBagConstraints);

        lblToTransID.setText("To Trans ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReprintDetails.add(lblToTransID, gridBagConstraints);

        lblTransDate.setText("Trans Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReprintDetails.add(lblTransDate, gridBagConstraints);

        tdtTransDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtTransDate.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReprintDetails.add(tdtTransDate, gridBagConstraints);

        txtFromTransID.setAllowAll(true);
        txtFromTransID.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 1);
        panReprintDetails.add(txtFromTransID, gridBagConstraints);

        txtToTransID.setAllowAll(true);
        txtToTransID.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 1);
        panReprintDetails.add(txtToTransID, gridBagConstraints);

        panReprintTransType.setMinimumSize(new java.awt.Dimension(130, 25));
        panReprintTransType.setPreferredSize(new java.awt.Dimension(130, 25));
        panReprintTransType.setLayout(new java.awt.GridBagLayout());

        chkReprintCash.setText("Cash");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panReprintTransType.add(chkReprintCash, gridBagConstraints);

        chkReprintTransfer.setText("Transfer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panReprintTransType.add(chkReprintTransfer, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panReprintDetails.add(panReprintTransType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panReprint.add(panReprintDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShareDividendCalculation.add(panReprint, gridBagConstraints);

        tabShareDividendCalculation.addTab("Share Dividend Payment", panShareDividendCalculation);

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
        tbrShareProduct.add(btnView);

        lblSpace5.setText("     ");
        tbrShareProduct.add(lblSpace5);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.setFocusable(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnNew);

        lblSpace51.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace51.setText("     ");
        lblSpace51.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace51.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace51.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareProduct.add(lblSpace51);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        tbrShareProduct.add(btnEdit);

        lblSpace52.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace52.setText("     ");
        lblSpace52.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace52.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace52.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareProduct.add(lblSpace52);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        tbrShareProduct.add(btnDelete);

        lblSpace2.setText("     ");
        tbrShareProduct.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnSave);

        lblSpace53.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace53.setText("     ");
        lblSpace53.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace53.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace53.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareProduct.add(lblSpace53);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.setEnabled(false);
        btnCancel.setFocusable(false);
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnCancel);

        lblSpace3.setText("     ");
        tbrShareProduct.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnAuthorize);

        lblSpace54.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace54.setText("     ");
        lblSpace54.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace54.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace54.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareProduct.add(lblSpace54);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        tbrShareProduct.add(btnException);

        lblSpace55.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace55.setText("     ");
        lblSpace55.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace55.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace55.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareProduct.add(lblSpace55);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnReject);

        lblSpace4.setText("     ");
        tbrShareProduct.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrShareProduct.add(btnPrint);

        lblSpace56.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace56.setText("     ");
        lblSpace56.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace56.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace56.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareProduct.add(lblSpace56);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnClose);

        getContentPane().add(tbrShareProduct, java.awt.BorderLayout.NORTH);

        mnuProcess.setText("Process");
        mnuProcess.setName("mnuProcess"); // NOI18N

        mitNew.setText("New");
        mitNew.setName("mitNew"); // NOI18N
        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mitEdit.setName("mitEdit"); // NOI18N
        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mitDelete.setName("mitDelete"); // NOI18N
        mnuProcess.add(mitDelete);

        sptNew.setName("sptNew"); // NOI18N
        mnuProcess.add(sptNew);

        mitSave.setText("Save");
        mitSave.setName("mitSave"); // NOI18N
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancel");
        mitCancel.setName("mitCancel"); // NOI18N
        mnuProcess.add(mitCancel);

        sptSave.setName("sptSave"); // NOI18N
        mnuProcess.add(sptSave);

        mitPrint.setText("Print");
        mitPrint.setName("mitPrint"); // NOI18N
        mnuProcess.add(mitPrint);

        mitClose.setText("Close");
        mitClose.setName("mitClose"); // NOI18N
        mnuProcess.add(mitClose);

        mbrMain.add(mnuProcess);

        setJMenuBar(mbrMain);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancel1ActionPerformed
        // TODO add your handling code here:
        ClientUtil.clearAll(this);
        btnRePrint.setEnabled(true);
        panReprint.setVisible(false);
        panDrfTransList.setVisible(true);
        panTransaction.setVisible(true);
    }//GEN-LAST:event_btnCancel1ActionPerformed

    private void btnReprintCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReprintCloseActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnReprintCloseActionPerformed

    private void btnPrint1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrint1ActionPerformed
        // TODO add your handling code here:
        //Added By Suresh
        if (!chkReprintCash.isSelected() && !chkReprintTransfer.isSelected()) {
            ClientUtil.showMessageWindow("Please select Cash or Transfer...");
            return;
        }
        if(txtFromTransID.getText().length()>0 && txtToTransID.getText().length()>0 && tdtTransDate.getDateValue().length()>0){
            HashMap paramMap = new HashMap();
            TTIntegration ttIntgration = null;
            paramMap.put("FromTransId", txtFromTransID.getText());
            paramMap.put("ToTransId", txtToTransID.getText());
            paramMap.put("TransDt", getProperDate(DateUtil.getDateMMDDYYYY(tdtTransDate.getDateValue())));
            paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
            System.out.println("####### paramMap"+paramMap);
            ttIntgration.setParam(paramMap);
            if (chkReprintCash.isSelected()) {
                ttIntgration.integrationForPrint("ShareDividendCashVoucher");
            } else if (chkReprintTransfer.isSelected()) {
                ttIntgration.integrationForPrint("ShareDividendTransferVoucher");
            }
            ClientUtil.enableDisable(panReprintDetails,false);
            btnPrint.setEnabled(false);
            ClientUtil.clearAll(this);
        }else{
            ClientUtil.showMessageWindow("Please Enter all Details of Reprint !!!");
        }
    }//GEN-LAST:event_btnPrint1ActionPerformed
          
    private Date getProperDate(Date sourceDate) {
        Date targetDate = (Date) curDate.clone();
        targetDate.setDate(sourceDate.getDate());
        targetDate.setMonth(sourceDate.getMonth());
        targetDate.setYear(sourceDate.getYear());
        return targetDate;
    }
    
    private void btnRePrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRePrintActionPerformed
        // TODO add your handling code here:
        //Added By Suresh
        btnPrint.setEnabled(true);
        panReprint.setVisible(true);
        panDrfTransList.setVisible(false);
        panTransaction.setVisible(false);
        ClientUtil.enableDisable(panReprintDetails,true);
        tdtTransDate.setDateValue(DateUtil.getStringDate((Date)curDate.clone()));
    }//GEN-LAST:event_btnRePrintActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        srpShareDividendCalculation.setEnabled(true);
        tblShareDividendCalculation.setEnabled(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        viewType = ClientConstants.ACTIONTYPE_AUTHORIZE;
        authorizeStatus(CommonConstants.STATUS_REJECTED);
        btnReject.setEnabled(true);
        btnException.setEnabled(false);
        btnSave.setEnabled(false);
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(false);
        btnNew.setEnabled(false);
    }//GEN-LAST:event_btnRejectActionPerformed

    private void txtMemberNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMemberNoFocusLost
        // TODO add your handling code here:
        HashMap hash=new HashMap();
      
        boolean result = false;
        if (chkClosedMem.isSelected()) {
            result = true;
        } else {
            result = observable.setAccountName(txtMemberNo.getText());
        }
        if(result){
        if(txtMemberNo.getText().length()>0){
            HashMap whereMap = new HashMap();
            //Changed By Suresh
            //            whereMap.put("SHARE_CLASS", CommonUtil.convertObjToStr(cboShareClass.getSelectedItem()));
            whereMap.put("SHARE_CLASS", CommonUtil.convertObjToStr(observable.getCbmShareClass().getKeyForSelected()));
            whereMap.put("MEMBER_NUMBER", txtMemberNo.getText());
            if(chkClosedMem.isSelected()){
                   whereMap.put("CLOSED","CLOSED");
            }else{
                whereMap.put("NOTCLOSED","NOTCLOSED"); 
            }
            List lst=ClientUtil.executeQuery("getSelectMemberUnpaidDividend",whereMap);
            if(lst !=null && lst.size()>0){
                viewType = MEMBER_NO;
                whereMap=(HashMap)lst.get(0);
                fillData(whereMap);
                lst=null;
                whereMap=null;
                displayTableData();
                displayShareAmt();
//                added by nikhil
                transactionUI.setButtonEnableDisable(true);
                transactionUI.resetObjects();
                //added by rishad 07/04/2014
                btnMembershipLia.setEnabled(true);
            }else{
                ClientUtil.displayAlert("divident allready paid !!! ");
                txtMemberNo.setText("");
            }
        }}
    }//GEN-LAST:event_txtMemberNoFocusLost

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        viewType = ClientConstants.ACTIONTYPE_AUTHORIZE;
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnSave.setEnabled(false);
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnNew.setEnabled(false);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
 //Nidhin
    public void authorizeStatus(String authorizeStatus) {
        if (viewType== ClientConstants.ACTIONTYPE_AUTHORIZE && isFilled){
                ArrayList arrList = new ArrayList();
                HashMap authorizeMap = new HashMap();
                HashMap singleAuthorizeMap = new HashMap();
                singleAuthorizeMap.put("STATUS", authorizeStatus);
                singleAuthorizeMap.put("AUTH_BY", TrueTransactMain.USER_ID);
                singleAuthorizeMap.put("AUTH_DT", curDate);
                singleAuthorizeMap.put("MEMBER_NO", observable.getTxtMemberNo());
                singleAuthorizeMap.put("PAYMENT_ID", paymentID);
                singleAuthorizeMap.put("SINGLE_TRANS_ID", singleTransId);
                singleAuthorizeMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
                arrList.add(singleAuthorizeMap);
                authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
                authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
                authorize(authorizeMap,observable.getTxtMemberNo()); 
                viewType = -1;
                super.setOpenForEditBy(observable.getStatusBy());
                singleAuthorizeMap = null;
                authorizeMap = null;
            
        }else {
            //__ To Save the data in the Internal Frame...
            HashMap whereMap = new HashMap();
            HashMap mapParam = new HashMap();
            setModified(true);
            //whereMap.put(CommonConstants.MAP_NAME, "getDividendPaymentTransferAuthMode");
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                mapParam.put(CommonConstants.MAP_NAME, "getDividendPaymentTransferAuthMode");
            } else //bb
            {
                mapParam.put(CommonConstants.MAP_NAME, "getDividendPaymentAuthWithOutCashier");
            }
            whereMap.put("TRANS_DT", curDate);
            whereMap.put(CommonConstants.HIERARCHY_ID, ProxyParameters.HIERARCHY_ID);
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            viewType = AUTHORIZE;
            isFilled = false;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            whereMap = null;
        }
    }
 
 public void authorize(HashMap map,String id) {
     System.out.println("Iam Here");
     HashMap rejectMap = new HashMap();
     if(map.containsKey(CommonConstants.AUTHORIZESTATUS) && map.get(CommonConstants.AUTHORIZESTATUS) != null && map.get(CommonConstants.AUTHORIZESTATUS).equals(CommonConstants.STATUS_REJECTED)){
	     int rowCount = CommonUtil.convertObjToInt(tblShareDividendCalculation.getRowCount());
	     for (int i = 0; i < rowCount; i++) {
		     ArrayList rejectList = new ArrayList();
		     rejectList.add(tblShareDividendCalculation.getValueAt(i, 1));
			 rejectMap.put(i, rejectList);
	     }
	     map.put("MEM_NO", CommonUtil.convertObjToStr(txtMemberNo.getText()));
	     map.put("REJECT_MAP", rejectMap);
     }
        System.out.println("Authorize Map : " + map);
        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
            observable.set_authorizeMap(map);
            //Added By Suresh
            if(transactionUI.getOutputTO().size()>0){
                observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
            }
            observable.doAction("AUTHORIZE");
            if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED){
//                super.setOpenForEditBy(observable.getStatusBy());
//                super.removeEditLock(id);
                if (fromAuthorizeUI) {
                    authorizeListUI.removeSelectedRow();
                    this.dispose();
                    authorizeListUI.setFocusToTable();
                    authorizeListUI.displayDetails("Share Dividend Payment");
                }
                if (fromNewAuthorizeUI) {
                    newauthorizeListUI.removeSelectedRow();
                    this.dispose();
                    newauthorizeListUI.setFocusToTable();
                    newauthorizeListUI.displayDetails("Share Dividend Payment");
                }
                if (fromManagerAuthorizeUI) {
                    ManagerauthorizeListUI.removeSelectedRow();
                    this.dispose();
                    ManagerauthorizeListUI.setFocusToTable();
                }
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
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
         cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        btnCancelActionPerformed();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        btnSaveActionPerformed();
    }//GEN-LAST:event_btnSaveActionPerformed
    private void btnSaveActionPerformed() {
        // TODO add your handling code here:
        resourceBundle = new ShareDividendCalculationRB();
        final String shareAcctMandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panDrfTransDetails);
        StringBuffer strBMandatory = new StringBuffer();
        int transactionSize = 0 ;
        if(/*rdoSharewithDrawal.isSelected()==true && */(transactionUI.getOutputTO() == null || (transactionUI.getOutputTO()).size() == 0)){
            strBMandatory.append(resourceBundle.getString("NoRecords"));
            strBMandatory.append("\n");
        }
        else{ //if(rdoShareAddition.isSelected()==false){
            transactionSize = (transactionUI.getOutputTO()).size();
            observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
        }
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
                if (!transactionUI.isBtnSaveTransactionDetailsFlag()) {
                    observable.showAlertWindow(resourceBundle.getString("saveInTxDetailsTable"));
                } else if (transactionUI.isBtnSaveTransactionDetailsFlag()) {
                    //                        int noOfShares = getNoOfShares();
                    double transTotalAmt = CommonUtil.convertObjToDouble(transactionUI.getTransactionOB().getLblTotalTransactionAmtVal()).doubleValue();
                    if (ClientUtil.checkTotalAmountTallied(CommonUtil.convertObjToDouble(txtTotalAmount.getText()).doubleValue(), transTotalAmt) == false)
                        ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NOT_TALLY));
                    else {
                        boolean interbranchFlag = false;
                        if(transactionUI.getTransactionOB().getSelectedTxnType() != null && observable.getTxtPaymentBranch() != null){
                            if(transactionUI.getTransactionOB().getSelectedTxnType().equals(CommonConstants.TX_TRANSFER)){
                                if(ProxyParameters.BRANCH_ID.equals(observable.getTxtPaymentBranch())){
                                    interbranchFlag = false;
                                }else if(ProxyParameters.BRANCH_ID.equals(transactionUI.getTransactionOB().getSelectedTxnBranchId())){
                                    interbranchFlag = false;
                                }else {
                                    interbranchFlag = true;
                                }
                            }else{
                                interbranchFlag = false;
                            }
                            if(interbranchFlag){
                                ClientUtil.displayAlert("Incase of interbranch transaction either "+"\n"+"Dr or Cr account of the transaction should be of own branch");
                            }else{
                                savePerformed();
                                observable.setStatus();
                                observable.setResultStatus();
                                lblStatus.setText(observable.getLblStatus());
                            }
                        }
                    }
                }
            }else {
                savePerformed();
                observable.setStatus();
                observable.setResultStatus();
                lblStatus.setText(observable.getLblStatus());
            }
            
            //End of Transaction call
            
            
            resourceBundle = null;
        } else {
            CommonMethods.displayAlert(resourceBundle.getString("saveAcctDet"));
        }
        TrueTransactMain.populateBranches();
        TrueTransactMain.selBranch = ProxyParameters.BRANCH_ID;
        observable.setSelectedBranchID(ProxyParameters.BRANCH_ID);
        setSelectedBranchID(ProxyParameters.BRANCH_ID);
    }
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        btnNewActionPerformed();
    }//GEN-LAST:event_btnNewActionPerformed
    
    private void btnDebitGlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDebitGlActionPerformed
        // TODO add your handling code here:
        viewType = MEMBER_NO;
        callView(MEMBER_NO);
        //added by rishad 07/04/2014
                btnMembershipLia.setEnabled(true); 
        displayTableData();
        displayShareAmt();
    }//GEN-LAST:event_btnDebitGlActionPerformed
    private void displayShareAmt(){
        HashMap hmap=new HashMap();
        hmap.put("MEMBER_NO",txtMemberNo.getText());
        List list=ClientUtil.executeQuery("getShareAmount",hmap);
        Date cashTranDt=null;
        Date transTranDt=null;
        String cashAmt="";
        String transAmt="";
        if(list!=null && list.size()>0){
            hmap=(HashMap)list.get(0);
            String shares=CommonUtil.convertObjToStr(hmap.get("NO_OF_SHARES"));
            String shareAmt=CommonUtil.convertObjToStr(hmap.get("TOTAL_SHARE_AMOUNT"));
            txtTotalNoOfShare.setText(shares);
            txtTotalShareAmount.setText(shareAmt);
            
        }
        hmap.put("MEMBER_NO",txtMemberNo.getText());
        List cashList=ClientUtil.executeQuery("getCashDetailsForShareDevident",hmap);
       
        if(cashList.size()>0 && cashList!=null){
            hmap=(HashMap)cashList.get(0);
            cashTranDt=DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hmap.get("BATCH_DT")));
            cashAmt=CommonUtil.convertObjToStr(hmap.get("TRANS_AMT"));
            txtDivAmount.setText(cashAmt);
            txtDeviDate.setText(CommonUtil.convertObjToStr(cashTranDt));
            
        }
         
     
            
       
        
    }
    
    private void displayTableData(){
        //Changed By Suresh
        //        String shareClass = CommonUtil.convertObjToStr(cboShareClass.getSelectedItem());
        String shareClass = CommonUtil.convertObjToStr(observable.getCbmShareClass().getKeyForSelected());
        String shareType = CommonUtil.convertObjToStr(observable.getCbmShareClass().getKeyForSelected());
        String memberNo = CommonUtil.convertObjToStr(txtMemberNo.getText());
        getShareDetails = new ArrayList();
        if (shareClass.length()>0 && memberNo.length() >0) {
            HashMap shareConfDetailsMap = new HashMap();
            HashMap shareDetailsMap = new HashMap();
            //            added By Nikhil
            ArrayList shareConfDetails = new ArrayList();
            txtTotalAmount.setEnabled(false);
            shareDetailsMap.put("SHARE_CLASS",shareClass);
            shareDetailsMap.put("SHARE_TYPE",shareType);
            shareDetailsMap.put("MEMBER_NO",memberNo);
            System.out.println("#@$@#$@#$shareClass"+shareClass);
            
            
            shareConfDetails =(ArrayList) ClientUtil.executeQuery("getSelectDividendCalProd", shareDetailsMap);
            if(shareConfDetails!= null && shareConfDetails.size() >0){
                shareConfDetailsMap = (HashMap) shareConfDetails.get(0);
                int unclaimedLimit = CommonUtil.convertObjToInt(shareConfDetailsMap.get("UNCLAIMED_DIVIDEND_PERIOD"));
                String unclaimedLimitPerion = CommonUtil.convertObjToStr(shareConfDetailsMap.get("UNCLAIMED_DIVIDEND_PERIOD_TYPE"));
                System.out.println("@#$@#$@#$unclaimedLimit:"+unclaimedLimit+ " :unclaimedLimitPerion :"+unclaimedLimitPerion+ ":currdate" +curDate);
                Date unclaimedDt = null;
                Date currDt = (Date) curDate.clone();
                if(unclaimedLimitPerion.equals("D")){
                    currDt.setMonth(2);
                    currDt.setDate(31);              
                    unclaimedDt = (Date) DateUtil.addDays(currDt,-unclaimedLimit);
                }else if(unclaimedLimitPerion.equals("Y")){
                    unclaimedLimit = unclaimedLimit*365;
                    currDt.setMonth(2);
                    currDt.setDate(31);
                    unclaimedDt = (Date) DateUtil.addDays(currDt,-unclaimedLimit);
                }else if(unclaimedLimitPerion.equals("M")){
                    unclaimedLimit = unclaimedLimit*30;
                    currDt.setMonth(2);
                    currDt.setDate(31);
                    unclaimedDt = (Date) DateUtil.addDays(currDt,-unclaimedLimit);
                }
                System.out.println("#$@#$@#$@#4unclaimedDt:"+unclaimedDt);
                shareDetailsMap.put("TO_PERIOD",unclaimedDt);
                if (chkClosedMem.isSelected()) {
                     getShareDetails =(ArrayList) ClientUtil.executeQuery("getShareDetailsForDivPaymentForClosed", shareDetailsMap);
                } else {
                   getShareDetails =(ArrayList) ClientUtil.executeQuery("getShareDetailsForDivPayment", shareDetailsMap);
                }
               
                System.out.println("@#$@#$getShareDetails:"+getShareDetails);
                if(getShareDetails!= null && getShareDetails.size() > 0) {
                    observable.populateSalaryDetails(getShareDetails);
                    tblShareDividendCalculation.setModel(observable.getTblShareDividendPayment());
                    txtTotalAmount.setText(observable.getTxtTotalAmount());
                    lblMemberName.setText(observable.getTxtMemberName());
                    lblMemberArea.setText(observable.getTxtArea());
                    lblMemberStreet.setText(observable.getTxtStreet()+"-"+observable.getTxtPinCode());
                    transactionUI.setCallingAmount(observable.getTxtTotalAmount());
                    transactionUI.setCallingApplicantName(observable.getTxtMemberName());
                }else{
                    
                     getShareDetails =(ArrayList) ClientUtil.executeQuery("getShareDetailsForDivPaymentPeriod", shareDetailsMap);
                     HashMap getShareMap =new HashMap();
                     if(getShareDetails !=null && getShareDetails.size()>0){
                         getShareMap =(HashMap)getShareDetails.get(0);
                     }
                    ClientUtil.showAlertWindow("Dividend Already Paid for this particular Share Class!!"+"\n"+
                    "Payment on Date was :"+ CommonUtil.convertObjToStr(getShareMap.get("TO_PERIOD")));
                    observable.resetDividendCalcListTable();
                    txtMemberNo.setText("");
                    observable.setTxtMemberNo("");
                }
            }
        }
    }
    private void tblShareDividendCalculationMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblShareDividendCalculationMouseClicked
        // TODO add your handling code here:
        String st=CommonUtil.convertObjToStr(tblShareDividendCalculation.getValueAt(tblShareDividendCalculation.getSelectedRow(),0));
        double dividendAmt = 0.0;
        if(st.equals("true")){
            tblShareDividendCalculation.setValueAt(new Boolean(false),tblShareDividendCalculation.getSelectedRow(),0);
        }else{
            tblShareDividendCalculation.setValueAt(new Boolean(true),tblShareDividendCalculation.getSelectedRow(),0);
        }
        if(tblShareDividendCalculation.getRowCount() > 0){
            for(int i=0; i <tblShareDividendCalculation.getRowCount() ; i++){
                if(CommonUtil.convertObjToStr(tblShareDividendCalculation.getValueAt(i,0)).equals("true")){
//                    dividendAmt += CommonUtil.convertObjToDouble(tblShareDividendCalculation.getValueAt(i,4)).doubleValue();
//                    changed by nikhil
                    String divAmtStr =  (CommonUtil.convertObjToStr(tblShareDividendCalculation.getValueAt(i,4))).replaceAll(",", "");
                    dividendAmt += CommonUtil.convertObjToDouble(divAmtStr).doubleValue();
                }
            }
            txtTotalAmount.setEnabled(false);
            System.out.println("@#$@#$@#$@#$dividendAmt:"+dividendAmt);
            observable.setTxtTotalAmount(String.valueOf(dividendAmt));
            txtTotalAmount.setText(observable.getTxtTotalAmount());
            //            added by nikhil
            transactionUI.setButtonEnableDisable(true);
            transactionUI.resetObjects();
            transactionUI.setCallingAmount(observable.getTxtTotalAmount());
        }
        
    }//GEN-LAST:event_tblShareDividendCalculationMouseClicked
    public static String formatCrore(String str) {
        java.text.DecimalFormat numberFormat = new java.text.DecimalFormat();
        numberFormat.applyPattern("########################0.00");
        
        double currData = Double.parseDouble(str.replaceAll(",",""));
        str = numberFormat.format( currData );
        
        String num = str.substring(0,str.lastIndexOf(".")).replaceAll(",","");
        String dec = str.substring(str.lastIndexOf("."));
        
        String sign = "";
        if (num.substring(0,1).equals("-")) {
            sign = num.substring(0,1);
            num = num.substring(1,num.length());
        }
        char[] chrArr = num.toCharArray();
        StringBuffer fmtStrB = new StringBuffer();
        
        for (int i=chrArr.length-1, j=0, k=0; i >= 0; i--) {
            if ((j==3 && k==3) || (j==2 && k==5) || (j==2 && k==7)) {
                fmtStrB.insert(0, ",");
                if (k==7) k = 0;
                j=0;
            }
            j++; k++;
            
            fmtStrB.insert(0, chrArr[i]);
        }
        fmtStrB.append(dec);
        
        str = fmtStrB.toString();
        
        str = sign+str;
        
        if (str.equals(".00")) str = "0";
        
        return str;
    }
    private void cboShareClassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboShareClassActionPerformed
 
    }//GEN-LAST:event_cboShareClassActionPerformed
    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
          HashMap reportParamMap = new HashMap();
            com.see.truetransact.clientutil.ttrintegration.LinkReport.getReports(getScreenID(), reportParamMap);
    }//GEN-LAST:event_btnPrintActionPerformed
//added by rishad 07/04/2014
    private void btnMembershipLiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMembershipLiaActionPerformed
        // TODO add your handling code here:
        HashMap resultMap=new HashMap();
        HashMap whereMap=new HashMap();
        whereMap.put("MEMBERSHIP_NO",txtMemberNo.getText());
        List list= ClientUtil.executeQuery("getCustId",whereMap);
        if(list.size()>0&&list!=null)
        {
        resultMap=(HashMap)list.get(0);
        }
             
        if (txtMemberNo.getText().length() > 0&&resultMap.size()>0&&resultMap!=null&&resultMap.containsKey("CUST_ID")) {
            new MembershipLiabilityUI(CommonUtil.convertObjToStr(resultMap.get("CUST_ID")), CommonUtil.convertObjToStr(txtMemberNo.getText())).show();
        }
    }//GEN-LAST:event_btnMembershipLiaActionPerformed

    private void txtMemberNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMemberNoActionPerformed
 //added by rishad 07/04/2014
                btnMembershipLia.setEnabled(true);        // TODO add your handling code here:
    }//GEN-LAST:event_txtMemberNoActionPerformed
            
    /* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        //        observable.setTxtDrfTransAmount(txtDrfTransAmount.getText());
        //        observable.setCboDrfTransProdID(CommonUtil.convertObjToStr(cboDrfTransProdID.getSelectedItem()));
        observable.setScreen(this.getScreen());
        observable.setTxtTotalAmount(txtTotalAmount.getText());
    }
    private void updateDividendCalcUI(){
        txtTotalAmount.setText(observable.getTxtTotalAmount());
        lblMemberName.setText(observable.getTxtMemberName());
        txtMemberNo.setText(observable.getTxtMemberNo());
        cboShareClass.setSelectedItem(observable.getCboShareClass());
    }
    private void resetUI(){
        observable.resetDividendCalcDetails();
        observable.resetDividendCalcListTable();
    }
    private void callView(int viewType){
        observable.setStatus();
        final HashMap viewMap = new HashMap();
        //--- If Action type is EDIT or DELETE show the popup Screen
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ||  observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE){
            ArrayList lst = new ArrayList();
            lst = null;
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
            //Changed By Suresh
            //            whereMap.put("SHARE_CLASS", CommonUtil.convertObjToStr(cboShareClass.getSelectedItem()));
            whereMap.put("SHARE_CLASS", CommonUtil.convertObjToStr(observable.getCbmShareClass().getKeyForSelected()));
            if(chkClosedMem.isSelected()){
                   whereMap.put("CLOSED","CLOSED");
            }else{
                whereMap.put("NOTCLOSED","NOTCLOSED"); 
            }
            if(viewType == MEMBER_NO){
                viewMap.put(CommonConstants.MAP_NAME, "getSelectMemberUnpaidDividend");
            }
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            //            viewMap.put(CommonConstants.MAP_NAME, "getDrfTransferAuthMode");
            if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW || observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                //                viewMap.put(CommonConstants.MAP_NAME, "getDrfTransferEditMode");
            }
            new ViewAll(this,viewMap).show();
        }
    }
    
    public void fillData(Object obj){
        try{
            HashMap hashMap=(HashMap)obj;
            System.out.println("### fillData Hash : "+hashMap);
            System.out.println("### fillData viewType: "+viewType);
            HashMap returnMap = null;
            if (hashMap.containsKey("NEW_FROM_AUTHORIZE_LIST_UI")) {
                fromNewAuthorizeUI = true;
                newauthorizeListUI = (NewAuthorizeListUI) hashMap.get("PARENT");
                hashMap.remove("PARENT");
                viewType = AUTHORIZE;
                observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
                observable.setStatus();
                transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
                btnSave.setEnabled(false);
                btnCancel.setEnabled(true);
                btnAuthorize.setEnabled(true);
                btnAuthorize.requestFocusInWindow();
                btnReject.setEnabled(false);
                rejectFlag = 1;
            }
            if(hashMap.containsKey("FROM_AUTHORIZE_LIST_UI")){
                fromAuthorizeUI = true;
                authorizeListUI = (AuthorizeListUI) hashMap.get("PARENT");
                hashMap.remove("PARENT");
                viewType = AUTHORIZE;
                observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
                observable.setStatus();
                transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
                 btnSave.setEnabled(false);
                btnCancel.setEnabled(true);
                btnAuthorize.setEnabled(true);
                btnAuthorize.requestFocusInWindow(); 
                btnReject.setEnabled(false);
                rejectFlag=1;
            }
            if (hashMap.containsKey("FROM_MANAGER_AUTHORIZE_LIST_UI")) {
                fromManagerAuthorizeUI = true;
                ManagerauthorizeListUI = (AuthorizeListDebitUI) hashMap.get("PARENT");
                hashMap.remove("PARENT");
                viewType = AUTHORIZE;
                observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
                observable.setStatus();
                transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
                 btnSave.setEnabled(false);
                btnCancel.setEnabled(true);
                btnAuthorize.setEnabled(true);
                btnAuthorize.requestFocusInWindow(); 
                btnReject.setEnabled(false);
                rejectFlag=1;
            }
            if (viewType == MEMBER_NO) {
                txtMemberNo.setText(CommonUtil.convertObjToStr(hashMap.get("MEMBER_NO")));
               String shareTyp = CommonUtil.convertObjToStr(observable.getCbmShareClass().getKeyForSelected());
                if (shareTyp != null && shareTyp.startsWith("A")) {
                    HashMap wherMap = new HashMap();
                    wherMap.put("SHARE_CLASS", CommonUtil.convertObjToStr(observable.getCbmShareClass().getKeyForSelected()));
                    wherMap.put("MEMBER_NO", txtMemberNo.getText());
                    wherMap.put("CUR_DATE",(Date) curDate.clone());
                    System.out.println(" ccccccc " + CommonUtil.convertObjToStr(observable.getCbmShareClass().getKeyForSelected()));
                    List lst1 = ClientUtil.executeQuery("checkCustomerRetirementAge", wherMap);
                    if (lst1 != null && lst1.size() > 0) {
                        HashMap sing = (HashMap) lst1.get(0);
                        String retDt = CommonUtil.convertObjToStr(sing.get("RETIREMENT_DT"));
                        if (retDt != null && retDt.length() > 0) {
                            Date retDate = DateUtil.getDateMMDDYYYY(retDt);
                            System.out.println(" difff :" + DateUtil.dateDiff(curDate, retDate));
                            if (DateUtil.dateDiff(curDate, retDate) < 0) {
                                ClientUtil.showMessageWindow("Member is retired");
                                // Commented by nithya on 08-04-2016 for 0004112
                                //txtMemberNo.setText("");
                                //resetUI(); 
                                //observable.resetForm();
                                //txtTotalAmount.setText("");
                                //transactionUI.setButtonEnableDisable(true);
                                //transactionUI.cancelAction(false);
                                //transactionUI.resetObjects();
                                //transactionUI.setCallingApplicantName("");
                                //transactionUI.setCallingAmount("");
                                return;
                            }
                        }
                    }
                }
            }
            if (observable.getActionType() == (ClientConstants.ACTIONTYPE_EDIT)||observable.getActionType() ==(ClientConstants.ACTIONTYPE_DELETE)|| observable.getActionType() ==(ClientConstants.ACTIONTYPE_AUTHORIZE)
            || observable.getActionType() ==(ClientConstants.ACTIONTYPE_REJECT)){
                isFilled = true;
                btnRePrint.setEnabled(false);
                if(checkIfAuthPending(hashMap)){
                        observable.setTxtMemberName(CommonUtil.convertObjToStr(hashMap.get("NAME")));
                        ArrayList authList = new ArrayList();
                        paymentID ="";
                        paymentID = CommonUtil.convertObjToStr(hashMap.get("PAYMENT_ID"));
                        singleTransId = CommonUtil.convertObjToStr(hashMap.get("SINGLE_TRANS_ID"));
                        observable.populateDataForAuth(hashMap);
                        observable.setTxtMemberNo(CommonUtil.convertObjToStr(hashMap.get("MEMBER_NO")));
                    observable.getCbmShareClass().setKeyForSelected(CommonUtil.convertObjToStr(hashMap.get("SHARE_CLASS")));
                    observable.setCboShareClass(CommonUtil.convertObjToStr(observable.getCbmShareClass().getDataForKey(CommonUtil.convertObjToStr(hashMap.get("SHARE_CLASS")))));
                        initTableData();
                        updateDividendCalcUI();
                        btnCancel.setEnabled(true);
                        observable.ttNotifyObservers();
                        tblShareDividendCalculation.setEnabled(false);
                }else{
                    ClientUtil.showAlertWindow("Authorization Pending for this Particular Share No!!");
                    btnCancelActionPerformed();
                }
                //--- disable the customerSelection in Edit Mode
            }
            hashMap = null;
            returnMap = null;
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        //__ To Save the data in the Internal Frame...
        setModified(true);
        if(rejectFlag==1){
           btnReject.setEnabled(false);
       }
    }
    
    private boolean checkIfAuthPending(HashMap checkMap){
        boolean checkIfAuth = false;
        ArrayList getShareDetails =(ArrayList) ClientUtil.executeQuery("ShareDivCheckIfAuthPending", checkMap);
        if(getShareDetails!= null && getShareDetails.size() > 0){
            checkIfAuth = false;
        }else{
            checkIfAuth= true;
        }
        return true;
    }
    private void actionEditDelete(HashMap hash){
        //fromActionEditHash = true;
        observable.resetForm();
        observable.setStatus();
        setButtonEnableDisable();
    }       
    
    private void resetTransactionUI(){
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
    }
    
    
    
    
    private void setTableInsertData(){
        List finalList = null;
        finalList = observable.getFinalList();
        HashMap shareMap = new HashMap();
        if(finalList!= null && finalList.size()>0){
            System.out.println("#$@$#@$@$@ FinalList : "+finalList);
            for(int i=0; i<finalList.size(); i++){
                String shareDivNO="";
                shareMap = (HashMap)finalList.get(i);
                shareDivNO = CommonUtil.convertObjToStr(shareMap.get("DIVIDEND_CALC_ID"));
                System.out.println("$#@@$@$#$@$ shareDivNO : "+shareDivNO);
                for(int j=0; j<tblShareDividendCalculation.getRowCount();j++) {
                    if (CommonUtil.convertObjToStr(tblShareDividendCalculation.getValueAt(j, 1)).equals(shareDivNO) && !((Boolean)tblShareDividendCalculation.getValueAt(j, 0)).booleanValue()) {
                        finalList.remove(i--);
                    }
                }
            }
             int count=0;
            for(int x=0;x<finalList.size();x++){
                count++;
                Date to_period=(Date)(((HashMap)finalList.get(x)).get("TO_PERIOD"));
                for(int j=0;j<finalList.size();j++){
                     shareMap = (HashMap)finalList.get(j);
                     if( shareMap.containsKey("TO_PERIOD") && shareMap.get("TO_PERIOD") !=null ){
                         if(DateUtil.dateDiff(to_period,(Date)shareMap.get("TO_PERIOD"))==0){
                             shareMap.put("HIERARCHY_VALUE",String.valueOf(count));
                             finalList.set(j,shareMap);
                         }
                     }
                }
            }
            System.out.println("#$#$$# final List:"+finalList);
            observable.setFinalList(finalList);
            System.out.println("#$#$$# final List:"+observable.getFinalList());
        }
    }
    private void savePerformed(){
        try{
            updateOBFields();
            System.out.println("#@$@$#@#getShareDetails"+getShareDetails);
            setTableInsertData();
            if(getShareDetails!= null && getShareDetails.size() >0 && saveMode.equals("SAVE")){
                HashMap getShareDetailsMap = (HashMap) getShareDetails.get(0);
                observable.setGetShareDetailsMap(getShareDetailsMap);
            }
            observable.setResult(observable.getActionType());
            observable.doAction(saveMode);
            if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED) {
                if (observable.getProxyReturnMap()!=null) {
                    if (observable.getProxyReturnMap().containsKey("CASH_TRANS_LIST") || observable.getProxyReturnMap().containsKey("TRANSFER_TRANS_LIST")) {
                        displayTransDetail(observable.getProxyReturnMap());
                    }
                }
            }
            if(saveMode !="LOAD"){
                observable.makeToNull();
                btnCancelActionPerformed();
            }
            observable.ttNotifyObservers();
            observable.setResultStatus();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void displayTransDetail(HashMap proxyResultMap) {
        System.out.println("@#$@@$@@@$ proxyResultMap : " +proxyResultMap);
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
        HashMap transIdMap = new HashMap();
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
                        transId = (String)transMap.get("SINGLE_TRANS_ID");
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
                    transIdMap.put(transMap.get("SINGLE_TRANS_ID"),"CASH");
                }
                cashCount++;
                transType = "CASH";
            } else if (CommonUtil.convertObjToStr(keys[i]).indexOf("TRANSFER")!=-1) {
                for (int j=0; j<tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j==0) {
                        transId = (String)transMap.get("SINGLE_TRANS_ID");
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
                      transIdMap.put(transMap.get("SINGLE_TRANS_ID"),"TRANSFER");
                }
                transferCount++;
                transType = "TRANSFER";
            }
        }
        if(cashCount>0){
            displayStr+=cashDisplayStr;
        }
        if(transferCount>0){
            displayStr+=transferDisplayStr;
        }
        ClientUtil.showMessageWindow(""+displayStr);
        int yesNo = 0;
        String[] options = {"Yes", "No"};
        yesNo = COptionPane.showOptionDialog(null,"Do you want to print?", CommonConstants.WARNINGTITLE,
        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
        null, options, options[0]);
        System.out.println("#$#$$ yesNo : "+yesNo);
        if (yesNo==0) {
            TTIntegration ttIntgration = null;
            HashMap paramMap = new HashMap();
           Date curDate1=(Date) curDate.clone();
            paramMap.put("TransDt", curDate1);
            paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
            //Added By Suresh
            if(transType.equals("CASH")){
                paramMap.put("TransId", transId);
            ttIntgration.setParam(paramMap);
            String reportName = "";
                reportName = "DividentPayment";
            ttIntgration.integrationForPrint(reportName, false);
            }
            if(transType.equals("TRANSFER")){
                Object keys1[] = transIdMap.keySet().toArray();
                for (int i=0; i<keys1.length; i++) {
                    paramMap.put("TransId", keys1[i]);
                    ttIntgration.setParam(paramMap);
                    if (CommonUtil.convertObjToStr(transIdMap.get(keys1[i])).equals("TRANSFER")) {
                        ttIntgration.integrationForPrint("DividentPaymentTransfer");
                    }
                }
            }
        }
    }
    private void btnNewActionPerformed() {
        setModified(true);
        resetUI();               // to Reset all the Fields and Status in UI...
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panShareDividendCalculationDetails, true);
        setButtonEnableDisable();
        observable.resetForm();
        observable.setStatus();
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        viewType = ClientConstants.ACTIONTYPE_NEW;
        observable.setStatus();
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
        transactionUI.setCallingApplicantName("");
        transactionUI.setCallingAmount("");
        btnDebitGl.setEnabled(true);
        btnSave.setEnabled(true);
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnNew.setEnabled(false);
        tblShareDividendCalculation.setEnabled(true);
        btnRePrint.setEnabled(false);
    }
    private void deletescreenLock(){
        HashMap map=new HashMap();
        map.put("USER_ID",ProxyParameters.USER_ID);
        map.put("TRANS_DT",  curDate);
        map.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
        ClientUtil.execute("DELETE_SCREEN_LOCK", map);
    }
    
    private void btnCancelActionPerformed() {
        // TODO add your handling code here:
        setModified(false);
        //        deletescreenLock();
        observable.resetForm();
        observable.setAuthorizeStatus("");
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panShareDividendCalculationDetails, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_CANCEL);
        setButtonEnableDisable();
        resetUI();
        observable.resetForm();
        lblStatus.setText("             ");
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
        transactionUI.setCallingApplicantName("");
        transactionUI.setCallingAmount("");
        saveMode = "";
        isFilled = false;
        btnDebitGl.setEnabled(false);
        btnNew.setEnabled(true);
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        btnSave.setEnabled(false);
        btnCancel.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnView.setEnabled(false);
        lblMemberName.setText("");
        lblMemberArea.setText("");
        lblMemberStreet.setText("");
        btnRePrint.setEnabled(true);
        chkClosedMem.setSelected(false);
        //added by rishad 07/04/2014
        btnMembershipLia.setEnabled(false);
        if(fromAuthorizeUI){
            authorizeListUI.setFocusToTable();
            this.dispose();
            fromAuthorizeUI = false;
        }
        if(fromNewAuthorizeUI){
           newauthorizeListUI.setFocusToTable();
            this.dispose();
            fromNewAuthorizeUI = false;
        }
    }
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnCancel1;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDebitGl;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnMembershipLia;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnPrint1;
    private com.see.truetransact.uicomponent.CButton btnRePrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnReprintClose;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboShareClass;
    private com.see.truetransact.uicomponent.CCheckBox chkClosedMem;
    private com.see.truetransact.uicomponent.CCheckBox chkReprintCash;
    private com.see.truetransact.uicomponent.CCheckBox chkReprintTransfer;
    private com.see.truetransact.uicomponent.CLabel lblChkClosedMemNo;
    private com.see.truetransact.uicomponent.CLabel lblDebitGl;
    private com.see.truetransact.uicomponent.CLabel lblDebitGl1;
    private com.see.truetransact.uicomponent.CLabel lblDebitGl2;
    private com.see.truetransact.uicomponent.CLabel lblFromTransID;
    private com.see.truetransact.uicomponent.CLabel lblMemberArea;
    private com.see.truetransact.uicomponent.CLabel lblMemberName;
    private com.see.truetransact.uicomponent.CLabel lblMemberStreet;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblShareClass;
    private com.see.truetransact.uicomponent.CLabel lblShareClass1;
    private com.see.truetransact.uicomponent.CLabel lblShareClass2;
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
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblToTransID;
    private com.see.truetransact.uicomponent.CLabel lblTotalAmount;
    private com.see.truetransact.uicomponent.CLabel lblTransDate;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
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
    private com.see.truetransact.uicomponent.CPanel panDrfTransDetails2;
    private com.see.truetransact.uicomponent.CPanel panDrfTransDetails3;
    private com.see.truetransact.uicomponent.CPanel panDrfTransList;
    private com.see.truetransact.uicomponent.CPanel panProcessType1;
    private com.see.truetransact.uicomponent.CPanel panReprint;
    private com.see.truetransact.uicomponent.CPanel panReprintBtn;
    private com.see.truetransact.uicomponent.CPanel panReprintDetails;
    private com.see.truetransact.uicomponent.CPanel panReprintTransType;
    private com.see.truetransact.uicomponent.CPanel panShareDividendCalculation;
    private com.see.truetransact.uicomponent.CPanel panShareDividendCalculationDetails;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTransaction;
    private com.see.truetransact.uicomponent.CButtonGroup rdoDrfTransaction;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CScrollPane srpShareDividendCalculation;
    private com.see.truetransact.uicomponent.CTabbedPane tabShareDividendCalculation;
    private com.see.truetransact.uicomponent.CTable tblShareDividendCalculation;
    private javax.swing.JToolBar tbrShareProduct;
    private com.see.truetransact.uicomponent.CDateField tdtTransDate;
    private com.see.truetransact.uicomponent.CTextField txtDeviDate;
    private com.see.truetransact.uicomponent.CTextField txtDivAmount;
    private com.see.truetransact.uicomponent.CTextField txtFromTransID;
    private com.see.truetransact.uicomponent.CTextField txtMemberNo;
    private com.see.truetransact.uicomponent.CTextField txtToTransID;
    private com.see.truetransact.uicomponent.CTextField txtTotalAmount;
    private com.see.truetransact.uicomponent.CTextField txtTotalNoOfShare;
    private com.see.truetransact.uicomponent.CTextField txtTotalShareAmount;
    // End of variables declaration//GEN-END:variables
}
