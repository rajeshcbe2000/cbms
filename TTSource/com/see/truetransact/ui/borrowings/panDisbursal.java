/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * panDisbursal.java
 *
 * Created on September 12, 2011, 6:44 PM
 */

package com.see.truetransact.ui.borrowings;

import javax.swing.table.DefaultTableModel;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uicomponent.COptionPane;
import java.util.*;
import java.text.SimpleDateFormat;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.transferobject.borrowings.disbursal.BorrowingDisbursalTO;
import com.see.truetransact.transferobject.investments.InvestmentsTransTO;
import com.see.truetransact.ui.common.transaction.TransactionUI;
import com.see.truetransact.ui.common.viewall.AuthorizeListUI;
import com.see.truetransact.ui.common.viewall.NewAuthorizeListUI;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.ui.salaryrecovery.AuthorizeListDebitUI;
import com.see.truetransact.ui.salaryrecovery.AuthorizeListCreditUI;
/**
 *
 * @author  user
 */
public class panDisbursal extends CInternalFrame implements Observer,UIMandatoryField {
    
    private DisbursalOB observable;
    private DisbursalMRB objMandatoryRB = new DisbursalMRB();//Instance for the MandatoryResourceBundle
    private HashMap mandatoryMap;//Map for putting up MandatoryFields in the UI
    private String viewType = "";//Variable used to store ActionType(New,Edit,Delete)
    private final static ClientParseException parseException = ClientParseException.getInstance();//Instance of ClientParseException to log the Exceptions
    private final String AUTHORIZE="Authorize";//Variable used when btnAuthorize is clicked
    private String strBorrowingNo="";
    //On disbursal transaction
    double totalAmount = 0.0;
    double sanctionAmount = 0.0;
    double avalBalance=0.0;
    double balanceAmt=0.0;
    TransactionUI transactionUI = new TransactionUI();
    double amtBorrow=0.0;
    String multiDis="";
    private ArrayList tableList=new ArrayList();
    private HashMap finalMap=new HashMap();
    boolean fromAuthorizeUI = false;
    boolean fromCashierAuthorizeUI = false;
    boolean fromManagerAuthorizeUI = false;
    AuthorizeListUI authorizeListUI = null;
    AuthorizeListDebitUI ManagerauthorizeListUI=null;
    AuthorizeListCreditUI CashierauthorizeListUI=null;
    private int rejectFlag=0;
    private Date currDt = null;
    NewAuthorizeListUI newauthorizeListUI = null;
    boolean fromNewAuthorizeUI = false;
    /** Creates new form Repayment */
    public panDisbursal() {
        currDt =ClientUtil.getCurrentDate();
        initComponents();
        setFieldNames();
        setObservable();
        observable.resetForm();
        setMaxLengths();
        initComponentData();
        setMandatoryHashMap();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panDisbursal, getMandatoryHashMap());
        panTrans.add(transactionUI);
       
  
        transactionUI.setSourceScreen("BORROW_DISBURSE");
        observable.setTransactionOB(transactionUI.getTransactionOB());
        ClientUtil.enableDisable(panDisbursal, false);
        setButtonEnableDisable();
        //Added By Suresh
        transactionUI.setProdType();
    }
    
    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        btnDelete.setEnabled(!btnDelete.isEnabled());
        btnSave.setEnabled(!btnSave.isEnabled());
        btnCancel.setEnabled(!btnCancel.isEnabled());
        btnVwBr.setEnabled(!btnVwBr.isEnabled());
        
    }
    
    private void setMaxLengths() {
        
    }
    
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
        lblBrRefNo.setName("lblBrRefNo");
        txtBorrowingRefNo.setName("txtBorrowingRefNo");
        btnVwBr.setName("btnVwBr");
        cDetailPanel.setName("cDetailPanel");
        lblBrNo.setName("lblBrNo");
        txtBorrowingNo.setName("txtBorrowingNo");
        lblAgency.setName("lblAgency");
        txtAgency.setName("txtAgency");
        lblType.setName("lblType");
        txtType.setName("txtType");
        lblDes.setName("lblDes");
        txtaDescription.setName("txtaDescription");
        lblSanDate.setName("lblSanDate");
        tdtDateSanctioned.setName("tdtDateSanctioned");
        lblSanAmt.setName("lblSanAmt");
        txtAmtSanctioned.setName("txtAmtSanctioned");
        lblRateInt.setName("lblRateInt");
        txtRateInterest.setName("txtRateInterest");
        lblNoInstall.setName("lblNoInstall");
        txtnoofInstall.setName("txtnoofInstall");
        lblPrinRep.setName("lblPrinRep");
        txtPrinRepFrq.setName("txtPrinRepFrq");
        lblIntRep.setName("lblIntRep");
        txtIntRepFrq.setName("txtIntRepFrq");
        lblMorotorium.setName("lblMorotorium");
        txtMorotorium.setName("txtMorotorium");
        lblSanExpDate.setName("lblSanExpDate");
        tdtDateExpiry.setName("tdtDateExpiry");
        panTrans.setName("panTrans");
        lblAmtBorrowed.setName("lblAmtBorrowed");
        txtAmtBorrowed.setName("txtAmtBorrowed");
        txtAvalBal.setName("txtAvalBal");
    }
    
    private void initComponentData() {
        try{
             txtAmtSanctioned.setValidation(new CurrencyValidation());
              txtAmtBorrowed.setValidation(new CurrencyValidation());
               txtAvalBal.setValidation(new CurrencyValidation());
        }catch(ClassCastException e){
            parseException.logException(e,true);
        }
    }
    
    private void setObservable() {
        try{
            observable = DisbursalOB.getInstance();
            observable.addObserver(this);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        cPanel1 = new com.see.truetransact.uicomponent.CPanel();
        tbrTokenConfig = new com.see.truetransact.uicomponent.CToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lbSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace51 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace52 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lbSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace53 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace54 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace55 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace56 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panDisbursal = new com.see.truetransact.uicomponent.CPanel();
        lblBrRefNo = new com.see.truetransact.uicomponent.CLabel();
        txtBorrowingRefNo = new com.see.truetransact.uicomponent.CTextField();
        btnVwBr = new com.see.truetransact.uicomponent.CButton();
        cDetailPanel = new com.see.truetransact.uicomponent.CPanel();
        lblBrNo = new com.see.truetransact.uicomponent.CLabel();
        txtBorrowingNo = new com.see.truetransact.uicomponent.CTextField();
        lblAgency = new com.see.truetransact.uicomponent.CLabel();
        lblType = new com.see.truetransact.uicomponent.CLabel();
        lblDes = new com.see.truetransact.uicomponent.CLabel();
        txtaDescription = new com.see.truetransact.uicomponent.CTextArea();
        tdtDateSanctioned = new com.see.truetransact.uicomponent.CDateField();
        lblSanDate = new com.see.truetransact.uicomponent.CLabel();
        lblSanAmt = new com.see.truetransact.uicomponent.CLabel();
        txtAmtSanctioned = new com.see.truetransact.uicomponent.CTextField();
        lblSanExpDate = new com.see.truetransact.uicomponent.CLabel();
        tdtDateExpiry = new com.see.truetransact.uicomponent.CDateField();
        txtMorotorium = new com.see.truetransact.uicomponent.CTextField();
        lblMorotorium = new com.see.truetransact.uicomponent.CLabel();
        lblIntRep = new com.see.truetransact.uicomponent.CLabel();
        txtnoofInstall = new com.see.truetransact.uicomponent.CTextField();
        txtRateInterest = new com.see.truetransact.uicomponent.CTextField();
        lblRateInt = new com.see.truetransact.uicomponent.CLabel();
        lblNoInstall = new com.see.truetransact.uicomponent.CLabel();
        lblPrinRep = new com.see.truetransact.uicomponent.CLabel();
        lblDteExpiry4 = new com.see.truetransact.uicomponent.CLabel();
        txtAgency = new com.see.truetransact.uicomponent.CTextField();
        txtType = new com.see.truetransact.uicomponent.CTextField();
        txtPrinRepFrq = new com.see.truetransact.uicomponent.CTextField();
        txtIntRepFrq = new com.see.truetransact.uicomponent.CTextField();
        lblAmtBorrowed = new com.see.truetransact.uicomponent.CLabel();
        txtAmtBorrowed = new com.see.truetransact.uicomponent.CTextField();
        txtAvalBal = new com.see.truetransact.uicomponent.CTextField();
        jLabel1 = new javax.swing.JLabel();
        panTrans = new com.see.truetransact.uicomponent.CPanel();
        cPanel2 = new com.see.truetransact.uicomponent.CPanel();
        srpMultiTransTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblMultiTransTable = new com.see.truetransact.uicomponent.CTable();
        panCheckBookBtn = new com.see.truetransact.uicomponent.CPanel();
        btnaddSave = new com.see.truetransact.uicomponent.CButton();
        btnAddDelete = new com.see.truetransact.uicomponent.CButton();
        btnaddNew = new com.see.truetransact.uicomponent.CButton();
        cPanel3 = new com.see.truetransact.uicomponent.CPanel();
        lblTotAmt = new com.see.truetransact.uicomponent.CLabel();
        txtTotAmt = new com.see.truetransact.uicomponent.CTextField();
        jInternalFrame1 = new javax.swing.JInternalFrame();
        cPanel4 = new com.see.truetransact.uicomponent.CPanel();
        tbrTokenConfig1 = new com.see.truetransact.uicomponent.CToolBar();
        btnView1 = new com.see.truetransact.uicomponent.CButton();
        lbSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnNew1 = new com.see.truetransact.uicomponent.CButton();
        btnEdit1 = new com.see.truetransact.uicomponent.CButton();
        btnDelete1 = new com.see.truetransact.uicomponent.CButton();
        lbSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnSave1 = new com.see.truetransact.uicomponent.CButton();
        btnCancel1 = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize1 = new com.see.truetransact.uicomponent.CButton();
        btnReject1 = new com.see.truetransact.uicomponent.CButton();
        btnException1 = new com.see.truetransact.uicomponent.CButton();
        lblSpace6 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint1 = new com.see.truetransact.uicomponent.CButton();
        btnClose1 = new com.see.truetransact.uicomponent.CButton();
        panDisbursal1 = new com.see.truetransact.uicomponent.CPanel();
        lblBrRefNo1 = new com.see.truetransact.uicomponent.CLabel();
        txtBorrowingRefNo1 = new com.see.truetransact.uicomponent.CTextField();
        btnVwBr1 = new com.see.truetransact.uicomponent.CButton();
        cDetailPanel1 = new com.see.truetransact.uicomponent.CPanel();
        lblBrNo1 = new com.see.truetransact.uicomponent.CLabel();
        txtBorrowingNo1 = new com.see.truetransact.uicomponent.CTextField();
        lblAgency1 = new com.see.truetransact.uicomponent.CLabel();
        lblType1 = new com.see.truetransact.uicomponent.CLabel();
        lblDes1 = new com.see.truetransact.uicomponent.CLabel();
        txtaDescription1 = new com.see.truetransact.uicomponent.CTextArea();
        tdtDateSanctioned1 = new com.see.truetransact.uicomponent.CDateField();
        lblSanDate1 = new com.see.truetransact.uicomponent.CLabel();
        lblSanAmt1 = new com.see.truetransact.uicomponent.CLabel();
        txtAmtSanctioned1 = new com.see.truetransact.uicomponent.CTextField();
        lblSanExpDate1 = new com.see.truetransact.uicomponent.CLabel();
        tdtDateExpiry1 = new com.see.truetransact.uicomponent.CDateField();
        txtMorotorium1 = new com.see.truetransact.uicomponent.CTextField();
        lblMorotorium1 = new com.see.truetransact.uicomponent.CLabel();
        lblIntRep1 = new com.see.truetransact.uicomponent.CLabel();
        txtnoofInstall1 = new com.see.truetransact.uicomponent.CTextField();
        txtRateInterest1 = new com.see.truetransact.uicomponent.CTextField();
        lblRateInt1 = new com.see.truetransact.uicomponent.CLabel();
        lblNoInstall1 = new com.see.truetransact.uicomponent.CLabel();
        lblPrinRep1 = new com.see.truetransact.uicomponent.CLabel();
        lblDteExpiry5 = new com.see.truetransact.uicomponent.CLabel();
        txtAgency1 = new com.see.truetransact.uicomponent.CTextField();
        txtType1 = new com.see.truetransact.uicomponent.CTextField();
        txtPrinRepFrq1 = new com.see.truetransact.uicomponent.CTextField();
        txtIntRepFrq1 = new com.see.truetransact.uicomponent.CTextField();
        lblAmtBorrowed1 = new com.see.truetransact.uicomponent.CLabel();
        txtAmtBorrowed1 = new com.see.truetransact.uicomponent.CTextField();
        txtAvalBal1 = new com.see.truetransact.uicomponent.CTextField();
        jLabel2 = new javax.swing.JLabel();
        panTrans1 = new com.see.truetransact.uicomponent.CPanel();
        cPanel5 = new com.see.truetransact.uicomponent.CPanel();
        srpMultiTransTable1 = new com.see.truetransact.uicomponent.CScrollPane();
        tblMultiTransTable1 = new com.see.truetransact.uicomponent.CTable();
        panCheckBookBtn1 = new com.see.truetransact.uicomponent.CPanel();
        btnaddSave1 = new com.see.truetransact.uicomponent.CButton();
        btnAddDelete1 = new com.see.truetransact.uicomponent.CButton();
        btnaddNew1 = new com.see.truetransact.uicomponent.CButton();
        cPanel6 = new com.see.truetransact.uicomponent.CPanel();
        lblTotAmt1 = new com.see.truetransact.uicomponent.CLabel();
        txtTotAmt1 = new com.see.truetransact.uicomponent.CTextField();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMinimumSize(new java.awt.Dimension(1000, 600));
        setPreferredSize(new java.awt.Dimension(1000, 600));

        cPanel1.setMinimumSize(new java.awt.Dimension(950, 750));
        cPanel1.setPreferredSize(new java.awt.Dimension(950, 750));
        cPanel1.setLayout(new java.awt.GridBagLayout());

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setEnabled(false);
        btnView.setFocusable(false);
        btnView.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView.setPreferredSize(new java.awt.Dimension(21, 21));
        tbrTokenConfig.add(btnView);

        lbSpace3.setText("     ");
        tbrTokenConfig.add(lbSpace3);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.setFocusable(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnNew);

        lblSpace51.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace51.setText("     ");
        lblSpace51.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace51.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace51.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace51);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.setFocusable(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnEdit);

        lblSpace52.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace52.setText("     ");
        lblSpace52.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace52.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace52.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace52);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.setFocusable(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnDelete);

        lbSpace2.setText("     ");
        tbrTokenConfig.add(lbSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.setFocusable(false);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnSave);

        lblSpace53.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace53.setText("     ");
        lblSpace53.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace53.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace53.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace53);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.setEnabled(false);
        btnCancel.setFocusable(false);
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnCancel);

        lblSpace3.setText("     ");
        tbrTokenConfig.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnAuthorize);

        lblSpace54.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace54.setText("     ");
        lblSpace54.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace54.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace54.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace54);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.setFocusable(false);
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnException);

        lblSpace55.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace55.setText("     ");
        lblSpace55.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace55.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace55.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace55);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnReject);

        lblSpace5.setText("     ");
        tbrTokenConfig.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.setFocusable(false);
        tbrTokenConfig.add(btnPrint);

        lblSpace56.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace56.setText("     ");
        lblSpace56.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace56.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace56.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace56);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.setFocusable(false);
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnClose);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 23;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel1.add(tbrTokenConfig, gridBagConstraints);

        panDisbursal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panDisbursal.setMaximumSize(new java.awt.Dimension(830, 620));
        panDisbursal.setMinimumSize(new java.awt.Dimension(830, 620));
        panDisbursal.setLayout(null);

        lblBrRefNo.setText("Borrowing ref number ");
        panDisbursal.add(lblBrRefNo);
        lblBrRefNo.setBounds(40, 10, 160, 18);

        txtBorrowingRefNo.setEnabled(false);
        panDisbursal.add(txtBorrowingRefNo);
        txtBorrowingRefNo.setBounds(200, 10, 160, 21);

        btnVwBr.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnVwBr.setToolTipText("Search");
        btnVwBr.setNextFocusableComponent(txtAmtBorrowed);
        btnVwBr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrowingAction(evt);
            }
        });
        panDisbursal.add(btnVwBr);
        btnVwBr.setBounds(369, 10, 40, 22);

        cDetailPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Details"));
        cDetailPanel.setMinimumSize(new java.awt.Dimension(950, 240));
        cDetailPanel.setLayout(new java.awt.GridBagLayout());

        lblBrNo.setText("Borrowing number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 26;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel.add(lblBrNo, gridBagConstraints);

        txtBorrowingNo.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 189;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel.add(txtBorrowingNo, gridBagConstraints);

        lblAgency.setText("Agency ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 22;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel.add(lblAgency, gridBagConstraints);

        lblType.setText("Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.ipadx = 42;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel.add(lblType, gridBagConstraints);

        lblDes.setText("Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipadx = 45;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel.add(lblDes, gridBagConstraints);

        txtaDescription.setRows(7);
        txtaDescription.setEnabled(false);
        txtaDescription.setMaximumSize(new java.awt.Dimension(200, 40));
        txtaDescription.setMinimumSize(new java.awt.Dimension(200, 40));
        txtaDescription.setPreferredSize(new java.awt.Dimension(200, 40));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 7;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel.add(txtaDescription, gridBagConstraints);

        tdtDateSanctioned.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel.add(tdtDateSanctioned, gridBagConstraints);

        lblSanDate.setText("Sanction Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 48;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel.add(lblSanDate, gridBagConstraints);

        lblSanAmt.setText("Sanction Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 23;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 30;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel.add(lblSanAmt, gridBagConstraints);

        txtAmtSanctioned.setEnabled(false);
        txtAmtSanctioned.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAmtSanctionedActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 23;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 89;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel.add(txtAmtSanctioned, gridBagConstraints);

        lblSanExpDate.setText("Sanction expiry Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 19;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel.add(lblSanExpDate, gridBagConstraints);

        tdtDateExpiry.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel.add(tdtDateExpiry, gridBagConstraints);

        txtMorotorium.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipadx = 89;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel.add(txtMorotorium, gridBagConstraints);

        lblMorotorium.setText("Morotorium ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.ipadx = 61;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel.add(lblMorotorium, gridBagConstraints);

        lblIntRep.setText("Interest Repayment ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 23;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel.add(lblIntRep, gridBagConstraints);

        txtnoofInstall.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 189;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel.add(txtnoofInstall, gridBagConstraints);

        txtRateInterest.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 189;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel.add(txtRateInterest, gridBagConstraints);

        lblRateInt.setText("Rate of Interest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 42;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel.add(lblRateInt, gridBagConstraints);

        lblNoInstall.setText("No of installment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 33;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel.add(lblNoInstall, gridBagConstraints);

        lblPrinRep.setText("Principal Repayment ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.ipadx = 6;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel.add(lblPrinRep, gridBagConstraints);

        lblDteExpiry4.setText("(months)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 13;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.ipadx = 39;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel.add(lblDteExpiry4, gridBagConstraints);

        txtAgency.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 189;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel.add(txtAgency, gridBagConstraints);

        txtType.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 189;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel.add(txtType, gridBagConstraints);

        txtPrinRepFrq.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 189;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel.add(txtPrinRepFrq, gridBagConstraints);

        txtIntRepFrq.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipadx = 189;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel.add(txtIntRepFrq, gridBagConstraints);

        lblAmtBorrowed.setText("Transaction Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 23;
        gridBagConstraints.ipadx = 14;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel.add(lblAmtBorrowed, gridBagConstraints);

        txtAmtBorrowed.setAllowAll(true);
        txtAmtBorrowed.setEnabled(false);
        txtAmtBorrowed.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAmtBorrowedFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 23;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 79;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel.add(txtAmtBorrowed, gridBagConstraints);

        txtAvalBal.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 13;
        gridBagConstraints.gridy = 23;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 89;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel.add(txtAvalBal, gridBagConstraints);

        jLabel1.setText("Aval Bal");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 15;
        gridBagConstraints.gridy = 19;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipadx = 22;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel.add(jLabel1, gridBagConstraints);

        panDisbursal.add(cDetailPanel);
        cDetailPanel.setBounds(10, 40, 700, 230);

        panTrans.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panTrans.setMinimumSize(new java.awt.Dimension(800, 600));
        panTrans.setPreferredSize(new java.awt.Dimension(800, 600));
        panTrans.setLayout(new java.awt.GridBagLayout());
        panDisbursal.add(panTrans);
        panTrans.setBounds(10, 270, 1030, 240);

        cPanel2.setLayout(new java.awt.GridBagLayout());

        srpMultiTransTable.setMinimumSize(new java.awt.Dimension(250, 150));
        srpMultiTransTable.setPreferredSize(new java.awt.Dimension(250, 150));

        tblMultiTransTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sl No", "Investment  ID", "Int.A/C No", "Amount"
            }
        ));
        tblMultiTransTable.setMinimumSize(new java.awt.Dimension(225, 200));
        tblMultiTransTable.setPreferredScrollableViewportSize(new java.awt.Dimension(350, 250));
        tblMultiTransTable.setPreferredSize(new java.awt.Dimension(225, 200));
        tblMultiTransTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblMultiTransTableMousePressed(evt);
            }
        });
        srpMultiTransTable.setViewportView(tblMultiTransTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 20);
        cPanel2.add(srpMultiTransTable, gridBagConstraints);

        panCheckBookBtn.setMinimumSize(new java.awt.Dimension(110, 35));
        panCheckBookBtn.setPreferredSize(new java.awt.Dimension(110, 35));
        panCheckBookBtn.setLayout(new java.awt.GridBagLayout());

        btnaddSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnaddSave.setToolTipText("New");
        btnaddSave.setMaximumSize(new java.awt.Dimension(29, 27));
        btnaddSave.setMinimumSize(new java.awt.Dimension(29, 27));
        btnaddSave.setPreferredSize(new java.awt.Dimension(29, 27));
        btnaddSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnaddSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCheckBookBtn.add(btnaddSave, gridBagConstraints);

        btnAddDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnAddDelete.setToolTipText("Delete");
        btnAddDelete.setMaximumSize(new java.awt.Dimension(29, 27));
        btnAddDelete.setMinimumSize(new java.awt.Dimension(29, 27));
        btnAddDelete.setPreferredSize(new java.awt.Dimension(29, 27));
        btnAddDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCheckBookBtn.add(btnAddDelete, gridBagConstraints);

        btnaddNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnaddNew.setToolTipText("New");
        btnaddNew.setMaximumSize(new java.awt.Dimension(29, 27));
        btnaddNew.setMinimumSize(new java.awt.Dimension(29, 27));
        btnaddNew.setPreferredSize(new java.awt.Dimension(29, 27));
        btnaddNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnaddNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCheckBookBtn.add(btnaddNew, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        cPanel2.add(panCheckBookBtn, gridBagConstraints);

        cPanel3.setMinimumSize(new java.awt.Dimension(250, 30));
        cPanel3.setPreferredSize(new java.awt.Dimension(250, 30));
        cPanel3.setLayout(new java.awt.GridBagLayout());

        lblTotAmt.setText("Total Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        cPanel3.add(lblTotAmt, gridBagConstraints);

        txtTotAmt.setText("0.0");
        txtTotAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTotAmt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotAmtActionPerformed(evt);
            }
        });
        txtTotAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTotAmtFocusLost(evt);
            }
        });
        cPanel3.add(txtTotAmt, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        cPanel2.add(cPanel3, gridBagConstraints);

        panDisbursal.add(cPanel2);
        cPanel2.setBounds(710, 10, 310, 260);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel1.add(panDisbursal, gridBagConstraints);

        getContentPane().add(cPanel1, java.awt.BorderLayout.CENTER);

        jInternalFrame1.setClosable(true);
        jInternalFrame1.setIconifiable(true);
        jInternalFrame1.setMaximizable(true);
        jInternalFrame1.setResizable(true);
        jInternalFrame1.setMinimumSize(new java.awt.Dimension(960, 600));
        jInternalFrame1.setPreferredSize(new java.awt.Dimension(960, 600));

        cPanel4.setMinimumSize(new java.awt.Dimension(950, 750));
        cPanel4.setPreferredSize(new java.awt.Dimension(950, 750));
        cPanel4.setLayout(new java.awt.GridBagLayout());

        btnView1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView1.setToolTipText("Enquiry");
        btnView1.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView1.setPreferredSize(new java.awt.Dimension(21, 21));
        btnView1.setEnabled(false);
        tbrTokenConfig1.add(btnView1);

        lbSpace4.setText("     ");
        tbrTokenConfig1.add(lbSpace4);

        btnNew1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew1.setToolTipText("New");
        btnNew1.setEnabled(false);
        btnNew1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNew1ActionPerformed(evt);
            }
        });
        tbrTokenConfig1.add(btnNew1);

        btnEdit1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit1.setToolTipText("Edit");
        btnEdit1.setEnabled(false);
        btnEdit1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEdit1ActionPerformed(evt);
            }
        });
        tbrTokenConfig1.add(btnEdit1);

        btnDelete1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete1.setToolTipText("Delete");
        btnDelete1.setEnabled(false);
        btnDelete1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelete1ActionPerformed(evt);
            }
        });
        tbrTokenConfig1.add(btnDelete1);

        lbSpace5.setText("     ");
        tbrTokenConfig1.add(lbSpace5);

        btnSave1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave1.setToolTipText("Save");
        btnSave1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSave1ActionPerformed(evt);
            }
        });
        tbrTokenConfig1.add(btnSave1);

        btnCancel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel1.setToolTipText("Cancel");
        btnCancel1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancel1ActionPerformed(evt);
            }
        });
        tbrTokenConfig1.add(btnCancel1);

        lblSpace4.setText("     ");
        tbrTokenConfig1.add(lblSpace4);

        btnAuthorize1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize1.setToolTipText("Authorize");
        btnAuthorize1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorize1ActionPerformed(evt);
            }
        });
        tbrTokenConfig1.add(btnAuthorize1);

        btnReject1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject1.setToolTipText("Reject");
        btnReject1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReject1ActionPerformed(evt);
            }
        });
        tbrTokenConfig1.add(btnReject1);

        btnException1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException1.setToolTipText("Exception");
        btnException1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnException1ActionPerformed(evt);
            }
        });
        tbrTokenConfig1.add(btnException1);

        lblSpace6.setText("     ");
        tbrTokenConfig1.add(lblSpace6);

        btnPrint1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint1.setToolTipText("Print");
        tbrTokenConfig1.add(btnPrint1);

        btnClose1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose1.setToolTipText("Close");
        btnClose1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClose1ActionPerformed(evt);
            }
        });
        tbrTokenConfig1.add(btnClose1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 23;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel4.add(tbrTokenConfig1, gridBagConstraints);

        panDisbursal1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panDisbursal1.setMaximumSize(new java.awt.Dimension(830, 620));
        panDisbursal1.setMinimumSize(new java.awt.Dimension(830, 620));
        panDisbursal1.setLayout(null);

        lblBrRefNo1.setText("Borrowing ref number ");
        panDisbursal1.add(lblBrRefNo1);
        lblBrRefNo1.setBounds(40, 10, 160, 18);

        txtBorrowingRefNo1.setEnabled(false);
        panDisbursal1.add(txtBorrowingRefNo1);
        txtBorrowingRefNo1.setBounds(200, 10, 160, 21);

        btnVwBr1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnVwBr1.setToolTipText("Search");
        btnVwBr1.setNextFocusableComponent(txtAmtBorrowed);
        btnVwBr1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVwBr1btnBorrowingAction(evt);
            }
        });
        panDisbursal1.add(btnVwBr1);
        btnVwBr1.setBounds(369, 10, 40, 22);

        cDetailPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Details"));
        cDetailPanel1.setMinimumSize(new java.awt.Dimension(950, 240));
        cDetailPanel1.setLayout(new java.awt.GridBagLayout());

        lblBrNo1.setText("Borrowing number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 26;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel1.add(lblBrNo1, gridBagConstraints);

        txtBorrowingNo1.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 189;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel1.add(txtBorrowingNo1, gridBagConstraints);

        lblAgency1.setText("Agency ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 22;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel1.add(lblAgency1, gridBagConstraints);

        lblType1.setText("Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.ipadx = 42;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel1.add(lblType1, gridBagConstraints);

        lblDes1.setText("Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipadx = 45;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel1.add(lblDes1, gridBagConstraints);

        txtaDescription1.setRows(7);
        txtaDescription1.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 7;
        gridBagConstraints.ipadx = 200;
        gridBagConstraints.ipady = 36;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel1.add(txtaDescription1, gridBagConstraints);

        tdtDateSanctioned1.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel1.add(tdtDateSanctioned1, gridBagConstraints);

        lblSanDate1.setText("Sanction Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 48;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel1.add(lblSanDate1, gridBagConstraints);

        lblSanAmt1.setText("Sanction Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 23;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 30;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel1.add(lblSanAmt1, gridBagConstraints);

        txtAmtSanctioned1.setEnabled(false);
        txtAmtSanctioned1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAmtSanctioned1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 23;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 89;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel1.add(txtAmtSanctioned1, gridBagConstraints);

        lblSanExpDate1.setText("Sanction expiry Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 19;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel1.add(lblSanExpDate1, gridBagConstraints);

        tdtDateExpiry1.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel1.add(tdtDateExpiry1, gridBagConstraints);

        txtMorotorium1.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipadx = 89;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel1.add(txtMorotorium1, gridBagConstraints);

        lblMorotorium1.setText("Morotorium ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.ipadx = 61;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel1.add(lblMorotorium1, gridBagConstraints);

        lblIntRep1.setText("Interest Repayment ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 23;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel1.add(lblIntRep1, gridBagConstraints);

        txtnoofInstall1.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 189;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel1.add(txtnoofInstall1, gridBagConstraints);

        txtRateInterest1.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 189;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel1.add(txtRateInterest1, gridBagConstraints);

        lblRateInt1.setText("Rate of Interest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 42;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel1.add(lblRateInt1, gridBagConstraints);

        lblNoInstall1.setText("No of installment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 33;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel1.add(lblNoInstall1, gridBagConstraints);

        lblPrinRep1.setText("Principal Repayment ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.ipadx = 6;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel1.add(lblPrinRep1, gridBagConstraints);

        lblDteExpiry5.setText("(months)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 13;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.ipadx = 39;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel1.add(lblDteExpiry5, gridBagConstraints);

        txtAgency1.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 189;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel1.add(txtAgency1, gridBagConstraints);

        txtType1.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 189;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel1.add(txtType1, gridBagConstraints);

        txtPrinRepFrq1.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 189;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel1.add(txtPrinRepFrq1, gridBagConstraints);

        txtIntRepFrq1.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipadx = 189;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel1.add(txtIntRepFrq1, gridBagConstraints);

        lblAmtBorrowed1.setText("Transaction Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 23;
        gridBagConstraints.ipadx = 14;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel1.add(lblAmtBorrowed1, gridBagConstraints);

        txtAmtBorrowed1.setAllowAll(true);
        txtAmtBorrowed1.setEnabled(false);
        txtAmtBorrowed1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAmtBorrowed1FocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 23;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 79;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel1.add(txtAmtBorrowed1, gridBagConstraints);

        txtAvalBal1.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 13;
        gridBagConstraints.gridy = 23;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 89;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel1.add(txtAvalBal1, gridBagConstraints);

        jLabel2.setText("Aval Bal");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 15;
        gridBagConstraints.gridy = 19;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipadx = 22;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cDetailPanel1.add(jLabel2, gridBagConstraints);

        panDisbursal1.add(cDetailPanel1);
        cDetailPanel1.setBounds(10, 40, 700, 230);

        panTrans1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panTrans1.setMinimumSize(new java.awt.Dimension(800, 600));
        panTrans1.setPreferredSize(new java.awt.Dimension(800, 600));
        panTrans1.setLayout(new java.awt.GridBagLayout());
        panDisbursal1.add(panTrans1);
        panTrans1.setBounds(10, 270, 1030, 240);

        cPanel5.setLayout(new java.awt.GridBagLayout());

        srpMultiTransTable1.setMinimumSize(new java.awt.Dimension(250, 150));

        tblMultiTransTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sl No", "Investment  ID", "Int.A/C No", "Amount"
            }
        ));
        tblMultiTransTable1.setMinimumSize(new java.awt.Dimension(225, 200));
        tblMultiTransTable1.setPreferredScrollableViewportSize(new java.awt.Dimension(350, 250));
        tblMultiTransTable1.setPreferredSize(new java.awt.Dimension(225, 200));
        tblMultiTransTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblMultiTransTable1MousePressed(evt);
            }
        });
        srpMultiTransTable1.setViewportView(tblMultiTransTable1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 20);
        cPanel5.add(srpMultiTransTable1, gridBagConstraints);

        panCheckBookBtn1.setMinimumSize(new java.awt.Dimension(110, 35));
        panCheckBookBtn1.setPreferredSize(new java.awt.Dimension(110, 35));
        panCheckBookBtn1.setLayout(new java.awt.GridBagLayout());

        btnaddSave1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnaddSave1.setToolTipText("New");
        btnaddSave1.setMaximumSize(new java.awt.Dimension(29, 27));
        btnaddSave1.setMinimumSize(new java.awt.Dimension(29, 27));
        btnaddSave1.setPreferredSize(new java.awt.Dimension(29, 27));
        btnaddSave1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnaddSave1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCheckBookBtn1.add(btnaddSave1, gridBagConstraints);

        btnAddDelete1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnAddDelete1.setToolTipText("Delete");
        btnAddDelete1.setMaximumSize(new java.awt.Dimension(29, 27));
        btnAddDelete1.setMinimumSize(new java.awt.Dimension(29, 27));
        btnAddDelete1.setPreferredSize(new java.awt.Dimension(29, 27));
        btnAddDelete1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddDelete1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCheckBookBtn1.add(btnAddDelete1, gridBagConstraints);

        btnaddNew1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnaddNew1.setToolTipText("New");
        btnaddNew1.setMaximumSize(new java.awt.Dimension(29, 27));
        btnaddNew1.setMinimumSize(new java.awt.Dimension(29, 27));
        btnaddNew1.setPreferredSize(new java.awt.Dimension(29, 27));
        btnaddNew1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnaddNew1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCheckBookBtn1.add(btnaddNew1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        cPanel5.add(panCheckBookBtn1, gridBagConstraints);

        cPanel6.setMinimumSize(new java.awt.Dimension(250, 30));
        cPanel6.setPreferredSize(new java.awt.Dimension(250, 30));
        cPanel6.setLayout(new java.awt.GridBagLayout());

        lblTotAmt1.setText("Total Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        cPanel6.add(lblTotAmt1, gridBagConstraints);

        txtTotAmt1.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTotAmt1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTotAmt1FocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        cPanel6.add(txtTotAmt1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        cPanel5.add(cPanel6, gridBagConstraints);

        panDisbursal1.add(cPanel5);
        cPanel5.setBounds(710, 10, 330, 260);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel4.add(panDisbursal1, gridBagConstraints);

        jInternalFrame1.getContentPane().add(cPanel4, java.awt.BorderLayout.CENTER);

        getContentPane().add(jInternalFrame1, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        
        cifClosingAlert();
//        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void txtAmtBorrowedFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAmtBorrowedFocusLost
        // TODO add your handling code here:
//        transactionUI.setCallingAmount(txtAmtBorrowed.getText());
//        transactionUI.setCallingTransType("TRANSFER");
//        double amountBorrowed = CommonUtil.convertObjToDouble(txtAmtBorrowed.getText()).doubleValue();
//        amountBorrowed = amountBorrowed + totalAmount;
//        if(amountBorrowed > sanctionAmount){
//            ClientUtil.showAlertWindow("Amount Exceeds the Disbursment Limit!!");
//            txtAmtBorrowed.setText("");
//            amountBorrowed = 0.0;
//        }
//        //Added BY Suresh
//        transactionUI.cancelAction(false);
//        transactionUI.setButtonEnableDisable(true);
//        transactionUI.resetObjects();
//        transactionUI.setCallingAmount(txtAmtBorrowed.getText());
//        transactionUI.setCallingTransType("TRANSFER");
    }//GEN-LAST:event_txtAmtBorrowedFocusLost
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        setModified(true);
        observable.resetForm();
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
        transactionUI.cancelAction(false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.resetObjects();
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
        txtAmtBorrowed.setEnabled(true);
        txtAmtBorrowed.setText("0");
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnSave.setEnabled(true);
        btnVwBr.setEnabled(true);
        btnCancel.setEnabled(true);
    }//GEN-LAST:event_btnNewActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        callView(ClientConstants.ACTION_STATUS[2]);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_VIEW);
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        callView( ClientConstants.ACTION_STATUS[3]);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_DELETE);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        setModified(false);
        
//        final String mandatoryMessage = checkMandatory(panDisbursal);
//        StringBuffer message = new StringBuffer(mandatoryMessage);
////        if(txtBorrowingRefNo.getText().equals("")) {
////            //message.append(objMandatoryRB.getString("txtBorrowingRefNo"));
////        }
//        
//        if(message.length() > 0 ){
//            displayAlert(message.toString());
//            return;
//        }
        
        
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            int transactionSize = 0;
            System.out.println("hhhhhhhhhhhhhh" + (transactionUI.getOutputTO()).size());
            if (transactionUI.getOutputTO().size() == 0 && CommonUtil.convertObjToDouble(txtTotAmt.getText()).doubleValue() > 0) {
                System.out.println("hhhhhhhhhhhhhh" + (transactionUI.getOutputTO()).size());
                ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS));
                return;
            } else {
                if (CommonUtil.convertObjToDouble(txtAmtBorrowed.getText()).doubleValue() > 0) {
                    amtBorrow = CommonUtil.convertObjToDouble(txtAmtBorrowed.getText()).doubleValue();
                    System.out.println("txtAmtBorrowed.getText()0000000000=====" + txtAmtBorrowed.getText());
                    transactionSize = (transactionUI.getOutputTO()).size();
                    if (transactionSize != 1 && CommonUtil.convertObjToDouble(txtAmtBorrowed.getText()).doubleValue() > 0) {
                        ClientUtil.showAlertWindow("Multiple Transactions are Not allowed, Make it one Transaction");
                        return;
                    } else {
                        observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                    }
                } else if (transactionUI.getOutputTO().size() > 0) {
                    observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                }
            }
            transactionSize = (transactionUI.getOutputTO()).size();
            if (transactionSize == 0 && CommonUtil.convertObjToDouble(txtTotAmt.getText()).doubleValue() > 0) {
               
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
    }//GEN-LAST:event_btnSaveActionPerformed

    public void updateAvalBalance() {
        try {
               HashMap singleAuthorizeMap1 = new HashMap();
            singleAuthorizeMap1.put("BORROWING_NO", strBorrowingNo);
            singleAuthorizeMap1.put("AMOUNT_BORRWED",CommonUtil.convertObjToDouble(totalAmount+amtBorrow));
            ClientUtil.execute("amtborrowedupdated", singleAuthorizeMap1);
                            HashMap singleAuthorizeMap2 = new HashMap();
                            singleAuthorizeMap2.put("BORROWING_NO", strBorrowingNo);
                            singleAuthorizeMap2.put("AMOUNT_BORRWED",CommonUtil.convertObjToDouble(balanceAmt-amtBorrow));
            singleAuthorizeMap2.put("CLEAR_BALANCE", CommonUtil.convertObjToDouble(totalAmount+amtBorrow));
                            ClientUtil.execute("avalbalupdated", singleAuthorizeMap2);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    private void savePerformed(){
        // System.out.println("IN savePerformed");
        String action = "";
        System.out.println("actionsssssss"+action);
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW ){
            action=CommonConstants.TOSTATUS_INSERT;
        }else if(observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT){
            action=CommonConstants.TOSTATUS_UPDATE;
        }else if(observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE){
            action=CommonConstants.TOSTATUS_DELETE;
        }
        saveAction(action);
    }
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        setModified(false);
        observable.resetForm();
        //txtNoOfTokens.setText("");
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panDisbursal, false);
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
        tableList=null;
        observable.insertTable(tableList);
        tblMultiTransTable.setModel(observable.getTblCheckBookTable());
        finalMap=null;
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
         if (fromCashierAuthorizeUI) {
            this.dispose();
            fromCashierAuthorizeUI = false;
            CashierauthorizeListUI.setFocusToTable();
        }
        if (fromManagerAuthorizeUI) {
            this.dispose();
            fromManagerAuthorizeUI = false;
            ManagerauthorizeListUI.setFocusToTable();
        }
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
//        tableList=null;
//        observable.insertTable(tableList);
//        tblMultiTransTable.setModel(observable.getTblCheckBookTable());
//        finalMap=null;
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
    }//GEN-LAST:event_btnExceptionActionPerformed
        public void authorizeStatus(String authorizeStatus) {
            if (!viewType.equals(AUTHORIZE)){
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID,TrueTransactMain.USER_ID);
            whereMap.put("AUTHORIZED_BY",TrueTransactMain.USER_ID);
            whereMap.put("CREATED_BY", TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID,TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            whereMap.put("CASHIER_AUTH_ALLOWED", TrueTransactMain.CASHIER_AUTH_ALLOWED);
            whereMap.put("TRANS_DT", currDt.clone());
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
          
            if(TrueTransactMain.CASHIER_AUTH_ALLOWED!=null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y"))
            { 
                mapParam.put(CommonConstants.MAP_NAME, "getBorrowingDisbursalCashierAuthorizeList");
            }else{
            mapParam.put(CommonConstants.MAP_NAME, "getBorrowingDisbursalAuthorizeList");
            }
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeBorrowingDisbursal");
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            observable.setStatus();
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
        } else if (viewType.equals(AUTHORIZE)){
            
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.USER_ID,TrueTransactMain.USER_ID);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDt.clone());
            singleAuthorizeMap.put("DISBURSAL_NO", observable.getDisbursalNo());
            singleAuthorizeMap.put("BORROWING_NO", txtBorrowingNo.getText());
            singleAuthorizeMap.put(CommonConstants.BRANCH_ID,TrueTransactMain.BRANCH_ID);
            observable.setAuthMap(singleAuthorizeMap); 
            //Added By Suresh
            if(transactionUI.getOutputTO().size()>0){
                observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
            }
            observable.execute(authorizeStatus); 
                if (fromNewAuthorizeUI) {
                    newauthorizeListUI.removeSelectedRow();
                    this.dispose();
                    newauthorizeListUI.setFocusToTable();
                    newauthorizeListUI.displayDetails("Borrowing Disbursal");
                }
             if (fromAuthorizeUI) {
                    authorizeListUI.removeSelectedRow();
                    this.dispose();
                    authorizeListUI.setFocusToTable();
                    authorizeListUI.displayDetails("Borrowing Disbursal");
                }
             if (fromCashierAuthorizeUI) {
                CashierauthorizeListUI.removeSelectedRow();
                this.dispose();
                CashierauthorizeListUI.setFocusToTable();
                } 
             if (fromManagerAuthorizeUI) {
                ManagerauthorizeListUI.removeSelectedRow();
                this.dispose();
                ManagerauthorizeListUI.setFocusToTable();
                }
            viewType = "";
            btnCancelActionPerformed(null);
        }
        }
    private void btnBorrowingAction(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrowingAction
        // TODO add your handling code here:
        callView("BORROWING_DATA");
        System.out.println("22222222222222222222222222222222" + txtBorrowingRefNo.getText());
        if (txtBorrowingRefNo.getText().length() > 0) {
            sanctionAmount = CommonUtil.convertObjToDouble(txtAmtSanctioned.getText()).doubleValue();
            HashMap getDisbursmentAmtMap = new HashMap();
            getDisbursmentAmtMap.put("BORRWING_REFNO", CommonUtil.convertObjToStr(txtBorrowingRefNo.getText()));
            List getDisbursementAmtLst = null;
            if (observable.getTxtType().equals("Cash Credit")) {
                getDisbursementAmtLst = ClientUtil.executeQuery("getBorrowDisbursementTotalAmountForCC", getDisbursmentAmtMap);
            } else {
                getDisbursementAmtLst = ClientUtil.executeQuery("getBorrowDisbursementTotalAmountForLoan", getDisbursmentAmtMap);
            }
            System.out.println("#$%#$%#$getDisbursementAmtLst:" + getDisbursementAmtLst);
            if (getDisbursementAmtLst != null && getDisbursementAmtLst.size() > 0) {
                getDisbursmentAmtMap = (HashMap) getDisbursementAmtLst.get(0);
                totalAmount = CommonUtil.convertObjToDouble(getDisbursmentAmtMap.get("TOTAL_AMOUNT")).doubleValue();
                strBorrowingNo = txtBorrowingNo.getText();
                if (totalAmount >= sanctionAmount) {
                    ClientUtil.showAlertWindow("Amount Disbursement Completed ,cannot make another Disbursment");
                    //btnCancelActionPerformed(null);
                    ClientUtil.clearAll(cDetailPanel);
                } else {
                    double amtBorrowed = CommonUtil.convertObjToDouble(txtAmtBorrowed.getText()).doubleValue();
                    balanceAmt = sanctionAmount - totalAmount;
                    if (amtBorrowed > balanceAmt) {
                        txtAmtBorrowed.setText(String.valueOf(balanceAmt));
                    }
                }
            }
            System.out.println("getMultiDis : " + observable.getMultiDis());
            System.out.println("multiDis: " + multiDis);
            if (observable.getMultiDis() != null && observable.getMultiDis().equalsIgnoreCase("NO")) {
                txtAmtBorrowed.setEnabled(false);
                txtAmtBorrowed.setEditable(false);
            } else {
                txtAmtBorrowed.setEnabled(true);
                txtAmtBorrowed.setEditable(true);
            }
            transactionUI.setCallingAmount(txtAmtBorrowed.getText());
            transactionUI.setCallingTransType("TRANSFER");
        }
    }//GEN-LAST:event_btnBorrowingAction
    private void callView(String currField) {
        viewType = currField;
        HashMap viewMap = new HashMap();
        ArrayList lst = new ArrayList();
        
        if (viewType.equals(ClientConstants.ACTION_STATUS[2]) ||
        viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) ||
        viewType.equals(ClientConstants.ACTION_STATUS[17])) {
            lst.add("DISBURSAL_NO");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            viewMap.put(CommonConstants.MAP_NAME, "BorrwingDisbursal.getSelectBorrowingDisbursalList");
        }
        else {
            lst.add("BORROWING_NO");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            viewMap.put(CommonConstants.MAP_NAME, "BorrwingDisbursal.getSelectBorrowingDList");
        }
        HashMap where = new HashMap();
        where.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
        viewMap.put(CommonConstants.MAP_WHERE, where);
        where = null;
          System.out.println("viewMap--inmmmmmmmmmmmmmmmmm-----"+viewMap);
        new ViewAll(this,viewMap).show();
        // new ViewAll(this,
    }
    @Override
    public void update(Observable observed, Object arg) {
       System.out.println("observable.getTxtBorrowingNo()IN CLSSSSS===="+observable.getTxtBorrowingNo());
        txtBorrowingNo.setText(observable.getTxtBorrowingNo());
        txtAgency.setText(observable.getTxtAgency());
        txtBorrowingRefNo.setText(observable.getTxtBorrowingRefNo());
        txtType.setText(observable.getTxtType());
        txtaDescription.setText(observable.getTxtaDescription());
        txtPrinRepFrq.setText(observable.getTxtPrinRepFrq());
        txtIntRepFrq.setText(observable.getTxtIntRepFrq());
        txtMorotorium.setText(observable.getTxtMorotorium());
        txtAvalBal.setText(String.valueOf(observable.getAvalbalBorrowedMaster()));
        // System.out.println("TXTRATEOF INETRRRR===="+observable.getTxtRateInterest());
        if(observable.getTxtRateInterest()!=null) {
            txtRateInterest.setText(String.valueOf(observable.getTxtRateInterest()));
        }
        if(observable.getTxtnoofInstall()!=null) {
            txtnoofInstall.setText(String.valueOf(observable.getTxtnoofInstall()));
        }
        
        if(observable.getTxtAmtSanctioned()!=null) {
            txtAmtSanctioned.setText(String.valueOf(observable.getTxtAmtSanctioned()));
        }
      //  if(observable.getTxtAmtBorrowed()!=null) {
          if(observable.getTxtAmtSanctioned()!=null) {
           txtAmtBorrowed.setText(String.valueOf(observable.getTxtAmtBorrowed()));
            // txtAmtBorrowed.setText(String.valueOf(observable.getTxtAmtSanctioned()));
        }
        if(observable.getTdtDateSanctioned()!=null) {
            tdtDateSanctioned.setDateValue(getDtPrintValue(String.valueOf(observable.getTdtDateSanctioned())));
        }
        if(observable.getTdtDateExpiry()!=null) {
            tdtDateExpiry.setDateValue(getDtPrintValue(String.valueOf(observable.getTdtDateExpiry())));
        }
        multiDis=observable.getMultiDis();
        
    }
    public void fillData(Object  map) {
        
        //        setModified(true);
        HashMap hash = (HashMap) map;
         if (hash.containsKey("NEW_FROM_AUTHORIZE_LIST_UI")) {
            fromNewAuthorizeUI = true;
            newauthorizeListUI = (NewAuthorizeListUI) hash.get("PARENT");
            hash.remove("PARENT");
            viewType = AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setStatus();
            transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
            btnSave.setEnabled(false);
            btnCancel.setEnabled(true);
            btnAuthorize.setEnabled(true);
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
            transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
            btnSave.setEnabled(false);
            btnCancel.setEnabled(true);
            btnAuthorize.setEnabled(true);
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
                transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
                btnSave.setEnabled(false);
                btnCancel.setEnabled(true);
                btnAuthorize.setEnabled(true);
                btnReject.setEnabled(false);
                rejectFlag=1;
            }
         if (hash.containsKey("FROM_MANAGER_AUTHORIZE_LIST_UI")) {
                fromManagerAuthorizeUI = true;
                ManagerauthorizeListUI = (AuthorizeListDebitUI) hash.get("PARENT");
                hash.remove("PARENT");
                viewType = AUTHORIZE;
                observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
                observable.setStatus();
                transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
                btnSave.setEnabled(false);
                btnCancel.setEnabled(true);
                btnAuthorize.setEnabled(true);
                btnReject.setEnabled(false);
                rejectFlag=1;
            }
        if (viewType != null) {
            if (viewType.equals("BORROWING_DATA") || viewType.equals(ClientConstants.ACTION_STATUS[2]) ||
            viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) || viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                HashMap where = new HashMap();
                //   HashMap data = new HashMap();
                System.out.println("jjjjjjjjjjjjjjjjj"+hash);
                if(viewType.equals("BORROWING_DATA")) {
                    //  data.put("BORROWING_DATA", "BORROWING_DATA");
                    where.put("BORROWING_NO", hash.get("BORROWING_NO"));
                }
                else {
                    //  data.put("DISBURSAL_DATA", "DISBURSAL_DATA");
                    where.put("DISBURSAL_NO", hash.get("DISBURSAL_NO"));
                }
                //   where.put(CommonConstants.BRANCH_ID, "0001");
                //  hash.put("DATA", data);
                hash.put(CommonConstants.MAP_WHERE, where);
                observable.populateData(hash);
                 tblMultiTransTable.setModel(observable.getTblCheckBookTable());
                
                //   fillTxtNoOfTokens();
                if (observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE || observable.getActionType()==ClientConstants.ACTIONTYPE_VIEW) {
                    ClientUtil.enableDisable(panDisbursal, false);
                }else  if (observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                    ClientUtil.enableDisable(panDisbursal, false);
                }
                if(viewType.equals(AUTHORIZE)){
                    ClientUtil.enableDisable(panDisbursal, false);
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                    btnAuthorize.setEnabled(true);
                    btnAuthorize.requestFocusInWindow();
                    btnAuthorize.setFocusable(true);
                }
                //  setButtonEnableDisable();
            }
        }
        if(rejectFlag==1){
           btnReject.setEnabled(false);
       }
        if (observable.getActionType()==ClientConstants.ACTIONTYPE_AUTHORIZE){
	        btnAuthorize.setEnabled(true);
	        btnAuthorize.requestFocusInWindow();
	        btnAuthorize.setFocusable(true);
       }
    }
    private void txtAmtSanctionedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAmtSanctionedActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAmtSanctionedActionPerformed

    private void tblMultiTransTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMultiTransTableMousePressed
        // TODO add your handling code here:
        String acno="";
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT || observable.getActionType()==ClientConstants.ACTIONTYPE_AUTHORIZE){
            if(tblMultiTransTable.getRowCount()>0){
                acno=CommonUtil.convertObjToStr(tblMultiTransTable.getValueAt(tblMultiTransTable.getSelectedRow(), 2));
              //  observable.showData(acno);
            }
        }else if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW){
            acno=CommonUtil.convertObjToStr(tblMultiTransTable.getValueAt(tblMultiTransTable.getSelectedRow(), 2));
            ArrayList aList=new ArrayList();
            BorrowingDisbursalTO  obj= (BorrowingDisbursalTO)finalMap.get(acno);
            System.out.println("obj"+obj);
            aList.add(obj);
          //  observable.showDatanew(aList);
        }
        txtAmtBorrowed.setText(CommonUtil.convertObjToStr(tblMultiTransTable.getValueAt(tblMultiTransTable.getSelectedRow(), 3)));
    }//GEN-LAST:event_tblMultiTransTableMousePressed

    private void btnaddSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaddSaveActionPerformed
        // TODO add your handling code here:
        if((CommonUtil.convertObjToDouble(txtAmtBorrowed.getText()))>0.0){
            HashMap transMap=new HashMap();
            ArrayList newList=new ArrayList();
           newList.add(txtBorrowingRefNo.getText());
            newList.add(txtBorrowingNo.getText());
            newList.add(txtAmtBorrowed.getText());
            if(tableList==null){
                tableList=new ArrayList();
            }
            for(int i=0;i<tblMultiTransTable.getRowCount();i++){
                if(txtBorrowingNo.getText().equals(tblMultiTransTable.getValueAt(i, 2))){
                    tableList.remove(i);
                }
            }
            tableList.add(newList);
            observable.insertTable(tableList);
            tblMultiTransTable.setModel(observable.getTblCheckBookTable());
            updateOBFields();
            if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW ){
                String action=CommonConstants.TOSTATUS_INSERT;
                BorrowingDisbursalTO obj=observable.getBorrowingDisbursalTO(action);//BorrowingDisbursalTO getBorrowingDisbursalTO(String command)
                if(finalMap==null){
                    finalMap=new HashMap();
                }
                finalMap.put(txtBorrowingNo.getText(),obj);

            }
            System.out.println("fmapppp"+finalMap);
            calcTotal();
           clearNextadd();
        }
    }//GEN-LAST:event_btnaddSaveActionPerformed
private void calcTotal(){
    double total=0.0;
    if(tblMultiTransTable.getRowCount()>0){
        for(int i=0;i<tblMultiTransTable.getRowCount();i++){
            total+=CommonUtil.convertObjToDouble(tblMultiTransTable.getValueAt(i, 3));
        }
        txtTotAmt.setText(CommonUtil.convertObjToStr(total));
        transactionUI.setCallingAmount(txtTotAmt.getText());
        transactionUI.setTotAmount(CommonUtil.convertObjToInt(tblMultiTransTable.getRowCount()));
    }
}
private void clearNextadd(){
    ClientUtil.enableDisable(cDetailPanel, false);
    ClientUtil.clearAll(cDetailPanel);
    txtBorrowingRefNo.setText("");
    
    
}
    private void btnAddDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddDeleteActionPerformed
        // TODO add your handling code here:
        int s = -1;
        s = tblMultiTransTable.getSelectedRow();
        System.out.println("sssde"+s);
        System.out.println("finalMap"+finalMap);
        if(s!=-1){
            tableList.remove(s);
            finalMap.remove(tblMultiTransTable.getValueAt(s, 2));
           observable.insertTable(tableList);
           tblMultiTransTable.setModel(observable.getTblCheckBookTable());
            calcTotal();
            clearNextadd();
            System.out.println("fmapppp"+finalMap);
        }else{
            ClientUtil.showAlertWindow("Please Select a row");
        }
    }//GEN-LAST:event_btnAddDeleteActionPerformed

    private void btnaddNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaddNewActionPerformed
        // TODO add your handling code here:
        if((CommonUtil.convertObjToDouble(txtAmtBorrowed.getText())>0)){
            ClientUtil.showAlertWindow("Please Save the Data First");
            return;
        }else{
            btnVwBr.setEnabled(true);
        }
    }//GEN-LAST:event_btnaddNewActionPerformed

    private void txtTotAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTotAmtFocusLost
        // TODO add your handling code here:
        
    }//GEN-LAST:event_txtTotAmtFocusLost

private void btnNew1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNew1ActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_btnNew1ActionPerformed

private void btnEdit1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEdit1ActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_btnEdit1ActionPerformed

private void btnDelete1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete1ActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_btnDelete1ActionPerformed

private void btnSave1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSave1ActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_btnSave1ActionPerformed

private void btnCancel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancel1ActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_btnCancel1ActionPerformed

private void btnAuthorize1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorize1ActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_btnAuthorize1ActionPerformed

private void btnReject1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReject1ActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_btnReject1ActionPerformed

private void btnException1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnException1ActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_btnException1ActionPerformed

private void btnClose1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClose1ActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_btnClose1ActionPerformed

private void btnVwBr1btnBorrowingAction(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVwBr1btnBorrowingAction
// TODO add your handling code here:
}//GEN-LAST:event_btnVwBr1btnBorrowingAction

private void txtAmtSanctioned1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAmtSanctioned1ActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_txtAmtSanctioned1ActionPerformed

private void txtAmtBorrowed1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAmtBorrowed1FocusLost
// TODO add your handling code here:
}//GEN-LAST:event_txtAmtBorrowed1FocusLost

private void tblMultiTransTable1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMultiTransTable1MousePressed
// TODO add your handling code here:
}//GEN-LAST:event_tblMultiTransTable1MousePressed

private void btnaddSave1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaddSave1ActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_btnaddSave1ActionPerformed

private void btnAddDelete1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddDelete1ActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_btnAddDelete1ActionPerformed

private void btnaddNew1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnaddNew1ActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_btnaddNew1ActionPerformed

private void txtTotAmt1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTotAmt1FocusLost
// TODO add your handling code here:
}//GEN-LAST:event_txtTotAmt1FocusLost

private void txtTotAmtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotAmtActionPerformed
// TODO add your handling code here:
        transactionUI.cancelAction(false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.resetObjects();
        transactionUI.setCallingAmount(txtTotAmt.getText());
        transactionUI.setCallingTransType("TRANSFER");
}//GEN-LAST:event_txtTotAmtActionPerformed
    
    public java.util.HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
      //  mandatoryMap.put("txtBorrowingRefNo", new Boolean(true));
        
    }
    public String getDtPrintValue(String strDate) {
        try {
            //create SimpleDateFormat object with source string date format
            java.text.SimpleDateFormat sdfSource = new java.text.SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
            //parse the string into Date object
            Date date = sdfSource.parse(strDate);
            //create SimpleDateFormat object with desired date format
            java.text.SimpleDateFormat sdfDestination = new java.text.SimpleDateFormat("dd/MM/yyyy");
            //parse the date into another format
            strDate = sdfDestination.format(date);
            //System.out.println("Date is converted from dd/MM/yy format to MM-dd-yyyy hh:mm:ss");
            //System.out.println("Converted date is : " + strDate);
        }
        catch(Exception e) {
            // e.printStackTrace();
        }
        return strDate;
    }
    private void saveAction(String status){
        //To check mandtoryness of the Terminal panAcHdDetails,panAcHeadDetails panel and diplay appropriate
        //error message, else proceed
        final String mandatoryMessage = checkMandatory(panDisbursal);
        StringBuffer message = new StringBuffer(mandatoryMessage);
//        if(txtBorrowingRefNo.getText().equals("")) {
//            message.append(objMandatoryRB.getString("txtBorrowingRefNo"));
//        }
        //setExpDateOnCalculation();
        if(message.length() > 0 ){
            displayAlert(message.toString());
        }else{
             System.out.println("hhhhhhhhhhhhhh");
           // updateOBFields();
            System.out.println("finalMap finalMap"+finalMap);
              updateAvalBalance();//to do here
              observable.setFinalMap(finalMap);
            observable.execute(status);
            if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED){
                HashMap lockMap = new HashMap();
                ArrayList lst = new ArrayList();
                lst.add("DISBURSAL_NO");
                lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
                if (observable.getProxyReturnMap()!=null) {
                    if (observable.getProxyReturnMap().containsKey("CONFIG_ID")) {
                        lockMap.put("CONFIG_ID", observable.getProxyReturnMap().get("CONFIG_ID"));
                    }
                    displayTransDetail(observable.getProxyReturnMap());
                }
                if (status==CommonConstants.TOSTATUS_UPDATE) {
                    lockMap.put("DISBURSAL_NO", observable.getDisbursalNo());
                }
                setEditLockMap(lockMap);
                setEditLock();
                settings();
            }
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
        String actNum = "";
        HashMap transIdMap = new HashMap();
        HashMap transTypeMap = new HashMap();
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
                    transTypeMap.put(transMap.get("TRANS_ID"),transMap.get("TRANS_TYPE"));
                    transIdMap.put(transMap.get("TRANS_ID"),"CASH");
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
                    transIdMap.put(transMap.get("BATCH_ID"),"TRANSFER");
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
        ClientUtil.showMessageWindow(""+displayStr);
        
        int yesNo = 0;
        String[] options = {"Yes", "No"};
        yesNo = COptionPane.showOptionDialog(null,"Do you want to print?", CommonConstants.WARNINGTITLE,
        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
        null, options, options[0]);
        System.out.println("#$#$$ yesNo : "+yesNo);
        if (yesNo==0) {
            TTIntegration ttIntgration = null;
                HashMap printParamMap = new HashMap();
                printParamMap.put("TransDt", observable.getCurrDt());
                printParamMap.put("BranchId", ProxyParameters.BRANCH_ID);
                Object keys1[] = transIdMap.keySet().toArray();
                for (int i=0; i<keys1.length; i++) {
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
            
    /** Method used to check whether the Mandatory Fields in the Form are Filled or not */
    private String checkMandatory(javax.swing.JComponent component){
        // return new MandatoryCheck().checkMandatory(getClass().getName(), component, getMandatoryHashMap());
        return "";
        //validation error
    }
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    private void settings(){
        btnCancelActionPerformed(null);
        observable.setResultStatus();
    }
    public Date getDateValue(String date1) {
        java.text.DateFormat formatter ;
        Date date=null ;
        try {
          /*  String str_date=date1;
            formatter = new java.text.SimpleDateFormat("MM/dd/yyyy");
            date = (Date)formatter.parse(str_date);*/
                SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy");
               
                  date=new Date(sdf2.format(sdf1.parse(date1)));
                
        }
        catch (java.text.ParseException e) {
            System.out.println("Error in getDateValue():"+e);
        }
        return date;
    }
    public void updateOBFields() {
        
        observable.setTxtBorrowingNo(txtBorrowingNo.getText());
        observable.setTxtAgency(txtAgency.getText());
        observable.setTxtBorrowingRefNo(txtBorrowingRefNo.getText());
        observable.setTxtType(txtType.getText());
        observable.setTxtaDescription(txtaDescription.getText());
        observable.setTxtRateInterest(Double.valueOf(txtRateInterest.getText()));
        observable.setTxtnoofInstall(Double.valueOf(txtnoofInstall.getText()));
        observable.setTxtPrinRepFrq(txtPrinRepFrq.getText() );
        observable.setTxtIntRepFrq(txtIntRepFrq.getText());
        observable.setTxtMorotorium(txtMorotorium.getText());
        
        observable.setTdtDateSanctioned(getDateValue(tdtDateSanctioned.getDateValue()));
        observable.setTdtDateExpiry(getDateValue(tdtDateExpiry.getDateValue()));
        // observable.setDateExpiry(getDateValue(tdtDateExpiry.getDateValue()));
        observable.setTxtAmtSanctioned(Double.valueOf(txtAmtSanctioned.getText()));
        observable.setTxtAmtBorrowed(Double.valueOf(txtAmtBorrowed.getText()));
        observable.setAmtBorrowedMaster(totalAmount);
        observable.setAvalbalBorrowedMaster(balanceAmt);
        observable.setModule(getModule());
        observable.setScreen(getScreen());
        observable.setModule(getModule());
        observable.setScreen(getScreen());
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAddDelete;
    private com.see.truetransact.uicomponent.CButton btnAddDelete1;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnAuthorize1;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnCancel1;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnClose1;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDelete1;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnEdit1;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnException1;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnNew1;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnPrint1;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnReject1;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnSave1;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CButton btnView1;
    private com.see.truetransact.uicomponent.CButton btnVwBr;
    private com.see.truetransact.uicomponent.CButton btnVwBr1;
    private com.see.truetransact.uicomponent.CButton btnaddNew;
    private com.see.truetransact.uicomponent.CButton btnaddNew1;
    private com.see.truetransact.uicomponent.CButton btnaddSave;
    private com.see.truetransact.uicomponent.CButton btnaddSave1;
    private com.see.truetransact.uicomponent.CPanel cDetailPanel;
    private com.see.truetransact.uicomponent.CPanel cDetailPanel1;
    private com.see.truetransact.uicomponent.CPanel cPanel1;
    private com.see.truetransact.uicomponent.CPanel cPanel2;
    private com.see.truetransact.uicomponent.CPanel cPanel3;
    private com.see.truetransact.uicomponent.CPanel cPanel4;
    private com.see.truetransact.uicomponent.CPanel cPanel5;
    private com.see.truetransact.uicomponent.CPanel cPanel6;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private com.see.truetransact.uicomponent.CLabel lbSpace2;
    private com.see.truetransact.uicomponent.CLabel lbSpace3;
    private com.see.truetransact.uicomponent.CLabel lbSpace4;
    private com.see.truetransact.uicomponent.CLabel lbSpace5;
    private com.see.truetransact.uicomponent.CLabel lblAgency;
    private com.see.truetransact.uicomponent.CLabel lblAgency1;
    private com.see.truetransact.uicomponent.CLabel lblAmtBorrowed;
    private com.see.truetransact.uicomponent.CLabel lblAmtBorrowed1;
    private com.see.truetransact.uicomponent.CLabel lblBrNo;
    private com.see.truetransact.uicomponent.CLabel lblBrNo1;
    private com.see.truetransact.uicomponent.CLabel lblBrRefNo;
    private com.see.truetransact.uicomponent.CLabel lblBrRefNo1;
    private com.see.truetransact.uicomponent.CLabel lblDes;
    private com.see.truetransact.uicomponent.CLabel lblDes1;
    private com.see.truetransact.uicomponent.CLabel lblDteExpiry4;
    private com.see.truetransact.uicomponent.CLabel lblDteExpiry5;
    private com.see.truetransact.uicomponent.CLabel lblIntRep;
    private com.see.truetransact.uicomponent.CLabel lblIntRep1;
    private com.see.truetransact.uicomponent.CLabel lblMorotorium;
    private com.see.truetransact.uicomponent.CLabel lblMorotorium1;
    private com.see.truetransact.uicomponent.CLabel lblNoInstall;
    private com.see.truetransact.uicomponent.CLabel lblNoInstall1;
    private com.see.truetransact.uicomponent.CLabel lblPrinRep;
    private com.see.truetransact.uicomponent.CLabel lblPrinRep1;
    private com.see.truetransact.uicomponent.CLabel lblRateInt;
    private com.see.truetransact.uicomponent.CLabel lblRateInt1;
    private com.see.truetransact.uicomponent.CLabel lblSanAmt;
    private com.see.truetransact.uicomponent.CLabel lblSanAmt1;
    private com.see.truetransact.uicomponent.CLabel lblSanDate;
    private com.see.truetransact.uicomponent.CLabel lblSanDate1;
    private com.see.truetransact.uicomponent.CLabel lblSanExpDate;
    private com.see.truetransact.uicomponent.CLabel lblSanExpDate1;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace51;
    private com.see.truetransact.uicomponent.CLabel lblSpace52;
    private com.see.truetransact.uicomponent.CLabel lblSpace53;
    private com.see.truetransact.uicomponent.CLabel lblSpace54;
    private com.see.truetransact.uicomponent.CLabel lblSpace55;
    private com.see.truetransact.uicomponent.CLabel lblSpace56;
    private com.see.truetransact.uicomponent.CLabel lblSpace6;
    private com.see.truetransact.uicomponent.CLabel lblTotAmt;
    private com.see.truetransact.uicomponent.CLabel lblTotAmt1;
    private com.see.truetransact.uicomponent.CLabel lblType;
    private com.see.truetransact.uicomponent.CLabel lblType1;
    private com.see.truetransact.uicomponent.CPanel panCheckBookBtn;
    private com.see.truetransact.uicomponent.CPanel panCheckBookBtn1;
    private com.see.truetransact.uicomponent.CPanel panDisbursal;
    private com.see.truetransact.uicomponent.CPanel panDisbursal1;
    private com.see.truetransact.uicomponent.CPanel panTrans;
    private com.see.truetransact.uicomponent.CPanel panTrans1;
    private com.see.truetransact.uicomponent.CScrollPane srpMultiTransTable;
    private com.see.truetransact.uicomponent.CScrollPane srpMultiTransTable1;
    private com.see.truetransact.uicomponent.CTable tblMultiTransTable;
    private com.see.truetransact.uicomponent.CTable tblMultiTransTable1;
    private com.see.truetransact.uicomponent.CToolBar tbrTokenConfig;
    private com.see.truetransact.uicomponent.CToolBar tbrTokenConfig1;
    private com.see.truetransact.uicomponent.CDateField tdtDateExpiry;
    private com.see.truetransact.uicomponent.CDateField tdtDateExpiry1;
    private com.see.truetransact.uicomponent.CDateField tdtDateSanctioned;
    private com.see.truetransact.uicomponent.CDateField tdtDateSanctioned1;
    private com.see.truetransact.uicomponent.CTextField txtAgency;
    private com.see.truetransact.uicomponent.CTextField txtAgency1;
    private com.see.truetransact.uicomponent.CTextField txtAmtBorrowed;
    private com.see.truetransact.uicomponent.CTextField txtAmtBorrowed1;
    private com.see.truetransact.uicomponent.CTextField txtAmtSanctioned;
    private com.see.truetransact.uicomponent.CTextField txtAmtSanctioned1;
    private com.see.truetransact.uicomponent.CTextField txtAvalBal;
    private com.see.truetransact.uicomponent.CTextField txtAvalBal1;
    private com.see.truetransact.uicomponent.CTextField txtBorrowingNo;
    private com.see.truetransact.uicomponent.CTextField txtBorrowingNo1;
    private com.see.truetransact.uicomponent.CTextField txtBorrowingRefNo;
    private com.see.truetransact.uicomponent.CTextField txtBorrowingRefNo1;
    private com.see.truetransact.uicomponent.CTextField txtIntRepFrq;
    private com.see.truetransact.uicomponent.CTextField txtIntRepFrq1;
    private com.see.truetransact.uicomponent.CTextField txtMorotorium;
    private com.see.truetransact.uicomponent.CTextField txtMorotorium1;
    private com.see.truetransact.uicomponent.CTextField txtPrinRepFrq;
    private com.see.truetransact.uicomponent.CTextField txtPrinRepFrq1;
    private com.see.truetransact.uicomponent.CTextField txtRateInterest;
    private com.see.truetransact.uicomponent.CTextField txtRateInterest1;
    private com.see.truetransact.uicomponent.CTextField txtTotAmt;
    private com.see.truetransact.uicomponent.CTextField txtTotAmt1;
    private com.see.truetransact.uicomponent.CTextField txtType;
    private com.see.truetransact.uicomponent.CTextField txtType1;
    private com.see.truetransact.uicomponent.CTextArea txtaDescription;
    private com.see.truetransact.uicomponent.CTextArea txtaDescription1;
    private com.see.truetransact.uicomponent.CTextField txtnoofInstall;
    private com.see.truetransact.uicomponent.CTextField txtnoofInstall1;
    // End of variables declaration//GEN-END:variables
    //    private com.see.truetransact.clientutil.TableModel tbModel;
}
