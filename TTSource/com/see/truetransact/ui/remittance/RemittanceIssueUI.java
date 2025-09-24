/*Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RemittanceIssueUI.java
 *
 * Created on December 30, 2003, 12:27 PM
 */

package com.see.truetransact.ui.remittance;

/**
 *
 * @author  Prasath.T
 * @modified Sunil
 * Changes Done : Removed Transaction part and made it to separate UI.
 * Added R/D amount chk
 * Created account head level transaction
 */
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.DefaultValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.authorize.AuthorizeUI ;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI ;
import com.see.truetransact.ui.remittance.revalidate.RemitIssueRevalidateUI;
import com.see.truetransact.ui.remittance.duplicate.RemitIssueDuplicateUI;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.uicomponent.COptionPane;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Observer;
import java.util.Observable;
import org.apache.log4j.Logger;
import com.see.truetransact.ui.common.transaction.TransactionUI;

public class RemittanceIssueUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer, UIMandatoryField {
    // Variables declaration - do not modify
    private final static Logger _log = Logger.getLogger(RemittanceIssueUI.class);
    private RemittanceIssueOB observable;
    private HashMap mandatoryMap;
    private boolean tableIssueMousePressed = false;
    private boolean tabletransactionMousePressed = false;
    private boolean btnSaveTransactionDetailsFlag = false;//flag to maintain whether btnSave Trans is pressed or not
    private boolean btnDeleteTransactionDetailsFlag = false;//flag to maintain whether btnDelete Trans is pressed or not
    private boolean btnNewIsPressed = false;
    private boolean duplicate = false;
    private boolean revalidate = false;
    final int EDIT=0, DELETE=1,PAYEE=2,DEBIT =3,LESS_THAN_AMOUNT =1,AUTHORIZE=3, TRANS_PROD = 4, ACC_NUM=5, PAYEE_ACC_NUM = 6, VIEW = 10, DUPLICATE = 11, REVALIDATE = 12;
    int viewType= -1;
    private int selectedRowValue;
    private int amtLimit =1;
    boolean isFilled = false;
    boolean isFill = false;
    boolean flag = false;
    private String strBatchID = "";
    private String variableNo = "";
    private String productId = "";
    private String bankCode = "";
    private String branchCode = "";
    private String category = "";
    private String amount = "";
    private String transactionType = "";
    private int rowCount;
    private String loanActNum;
    private String exg = "";
    private String pos = "";
    private String st = "";
    private boolean isBranchCodeNotValid = false;
    private boolean isRemitTblClicked = false;
    
    private TransactionUI transactionUI = new TransactionUI();
//    private RemitIssueDuplicateUI duplicateUI  = new RemitIssueDuplicateUI();
    final RemittanceIssueRB resourceBundle = new RemittanceIssueRB();
    // End of variables declaration
    private Date currDt = null;
    /** Creates new form RemittanceIssueUI */
    public RemittanceIssueUI() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        initStartup();
        
        transactionUI.addToScreen(panTransaction);
        
        observable.setTransactionOB(transactionUI.getTransactionOB());
    }
    /* methods invoked at the time of new form */
    private void initStartup() {
        try{
            setFieldNames();
            internationalize();
            setObservable();
            initComponentData();
            setMaximumLength();
            new MandatoryCheck().putMandatoryMarks(getClass().getName(),panDetails);
            ClientUtil.enableDisable(this, false);
            setButtonEnableDisable();
            setPayeeAccHDButtonEnableDisable(false);
            btnPayeeAccNo.setEnabled(false);
            btnPayeeAccHead.setVisible(false);
            transactionUI.setSourceScreen("REMITISSUE");
            setIssuetxtDisable();
            setMandatoryHashMap();
            setHelpMessage();
            observable.resetStatus();
            observable.resetForm();
            setTableIssueEnableDisable(true);
            setRevalidationDuplicationEnableDisable(true);//changed 10/10/2008
            lblDupServTax.setVisible(true);
            txtDupServTax.setVisible(true);
            lblRevServTax.setVisible(true);
            txtRevServTax.setVisible(true);
            txtDuplicationCharge.setEditable(false);
            txtRevalidationCharge.setVisible(true);
            lblRevalidationCharge.setVisible(true);
            txtDupServTax.setEditable(false);
            txtDuplicationCharge.setEnabled(false);
            txtDupServTax.setEnabled(false);
            txtTotalamt.setEnabled(false);
            txtTotalamt.setEditable(false);
            txtVariableNo.setEnabled(false);
            txtVariableNo.setEditable(false);
            txtExchange1.setEnabled(false);
        }catch(Exception err){
            err.printStackTrace();
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

        tbrRemittanceIssue = new com.see.truetransact.uicomponent.CToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace6 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace17 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace18 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lbSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnRevalidate = new com.see.truetransact.uicomponent.CButton();
        btnDuplicate = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace19 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace20 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace21 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace22 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panDetails = new com.see.truetransact.uicomponent.CPanel();
        panIssueDetails = new com.see.truetransact.uicomponent.CPanel();
        panTableIssue = new com.see.truetransact.uicomponent.CPanel();
        srpIssue = new com.see.truetransact.uicomponent.CScrollPane();
        tblIssue = new com.see.truetransact.uicomponent.CTable();
        PanTotalInstruments = new com.see.truetransact.uicomponent.CPanel();
        panRITotal = new com.see.truetransact.uicomponent.CPanel();
        lblTotalInstruments = new com.see.truetransact.uicomponent.CLabel();
        txtTotalInstruments = new com.see.truetransact.uicomponent.CTextField();
        lblTotalamount = new com.see.truetransact.uicomponent.CLabel();
        txtTotalamt = new com.see.truetransact.uicomponent.CTextField();
        panChrg = new com.see.truetransact.uicomponent.CPanel();
        lblDuplicationCharge = new com.see.truetransact.uicomponent.CLabel();
        txtDuplicationCharge = new com.see.truetransact.uicomponent.CTextField();
        lblRevalidationCharge = new com.see.truetransact.uicomponent.CLabel();
        txtRevalidationCharge = new com.see.truetransact.uicomponent.CTextField();
        txtRevServTax = new com.see.truetransact.uicomponent.CTextField();
        lblRevServTax = new com.see.truetransact.uicomponent.CLabel();
        txtDupServTax = new com.see.truetransact.uicomponent.CTextField();
        lblDupServTax = new com.see.truetransact.uicomponent.CLabel();
        panbtnIssue = new com.see.truetransact.uicomponent.CPanel();
        btnNewIssue = new com.see.truetransact.uicomponent.CButton();
        btnSaveIssue = new com.see.truetransact.uicomponent.CButton();
        btnDeleteIssue = new com.see.truetransact.uicomponent.CButton();
        panIssue = new com.see.truetransact.uicomponent.CPanel();
        panLeftSubIssue = new com.see.truetransact.uicomponent.CPanel();
        cboProductId = new com.see.truetransact.uicomponent.CComboBox();
        lblProductId = new com.see.truetransact.uicomponent.CLabel();
        lblCity = new com.see.truetransact.uicomponent.CLabel();
        lblDraweeBank = new com.see.truetransact.uicomponent.CLabel();
        lblFavouring = new com.see.truetransact.uicomponent.CLabel();
        txtFavouring = new com.see.truetransact.uicomponent.CTextField();
        lblTransmissionType = new com.see.truetransact.uicomponent.CLabel();
        cboTransmissionType = new com.see.truetransact.uicomponent.CComboBox();
        lblPANGIRNo = new com.see.truetransact.uicomponent.CLabel();
        txtPANGIRNo = new com.see.truetransact.uicomponent.CTextField();
        lblCategory = new com.see.truetransact.uicomponent.CLabel();
        cboCategory = new com.see.truetransact.uicomponent.CComboBox();
        cboCity = new com.see.truetransact.uicomponent.CComboBox();
        cboDraweeBank = new com.see.truetransact.uicomponent.CComboBox();
        cboBranchCode = new com.see.truetransact.uicomponent.CComboBox();
        lblBranchCode = new com.see.truetransact.uicomponent.CLabel();
        lblAccHead = new com.see.truetransact.uicomponent.CLabel();
        lblAccHeadProdIdDisplay = new javax.swing.JLabel();
        lblAmt = new com.see.truetransact.uicomponent.CLabel();
        txtAmt = new com.see.truetransact.uicomponent.CTextField();
        lblExchange = new com.see.truetransact.uicomponent.CLabel();
        txtExchange = new com.see.truetransact.uicomponent.CTextField();
        lblAccHeadBal = new com.see.truetransact.uicomponent.CLabel();
        lblAccHeadBalDisplay = new javax.swing.JLabel();
        txtExchange1 = new com.see.truetransact.uicomponent.CTextField();
        lblExchange1 = new com.see.truetransact.uicomponent.CLabel();
        sptIssue = new com.see.truetransact.uicomponent.CSeparator();
        panRightSubIssue = new com.see.truetransact.uicomponent.CPanel();
        lblTotalAmt = new com.see.truetransact.uicomponent.CLabel();
        lblPayeeAccHead = new com.see.truetransact.uicomponent.CLabel();
        txtTotalAmt = new com.see.truetransact.uicomponent.CTextField();
        panPayeeAccHead = new com.see.truetransact.uicomponent.CPanel();
        txtPayeeAccHead = new com.see.truetransact.uicomponent.CTextField();
        btnPayeeAccHead = new com.see.truetransact.uicomponent.CButton();
        txtOtherCharges = new com.see.truetransact.uicomponent.CTextField();
        lblPayeeAccNo = new com.see.truetransact.uicomponent.CLabel();
        lblOtherCharges = new com.see.truetransact.uicomponent.CLabel();
        lblPostage = new com.see.truetransact.uicomponent.CLabel();
        txtPostage = new com.see.truetransact.uicomponent.CTextField();
        lblInstrumentNo = new com.see.truetransact.uicomponent.CLabel();
        panInstrumentNo = new com.see.truetransact.uicomponent.CPanel();
        txtInstrumentNo1 = new com.see.truetransact.uicomponent.CTextField();
        txtInstrumentNo2 = new com.see.truetransact.uicomponent.CTextField();
        lblInstrumentHIphen = new com.see.truetransact.uicomponent.CLabel();
        lblVariableNo = new com.see.truetransact.uicomponent.CLabel();
        txtVariableNo = new com.see.truetransact.uicomponent.CTextField();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        txtRemarks = new com.see.truetransact.uicomponent.CTextField();
        lblCrossing = new com.see.truetransact.uicomponent.CLabel();
        cboCrossing = new com.see.truetransact.uicomponent.CComboBox();
        cboPayeeProdType = new com.see.truetransact.uicomponent.CComboBox();
        lblPayeeProdType = new com.see.truetransact.uicomponent.CLabel();
        lblPayeeProdId = new com.see.truetransact.uicomponent.CLabel();
        cboPayeeProdId = new com.see.truetransact.uicomponent.CComboBox();
        panDebitAccNo = new com.see.truetransact.uicomponent.CPanel();
        txtPayeeAccNo = new com.see.truetransact.uicomponent.CTextField();
        btnPayeeAccNo = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        panBatchId = new com.see.truetransact.uicomponent.CPanel();
        lblBatchId = new com.see.truetransact.uicomponent.CLabel();
        lblDisplayBatchId = new com.see.truetransact.uicomponent.CLabel();
        panTransaction = new com.see.truetransact.uicomponent.CPanel();
        mbrMain = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptView = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Issue");
        setMinimumSize(new java.awt.Dimension(825, 656));
        setPreferredSize(new java.awt.Dimension(825, 655));
        getContentPane().setLayout(new java.awt.GridBagLayout());

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
        tbrRemittanceIssue.add(btnView);

        lblSpace6.setText("     ");
        tbrRemittanceIssue.add(lblSpace6);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrRemittanceIssue.add(btnNew);

        lblSpace17.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace17.setText("     ");
        lblSpace17.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrRemittanceIssue.add(lblSpace17);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrRemittanceIssue.add(btnEdit);

        lblSpace18.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace18.setText("     ");
        lblSpace18.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace18.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace18.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrRemittanceIssue.add(lblSpace18);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrRemittanceIssue.add(btnDelete);

        lbSpace2.setText("     ");
        tbrRemittanceIssue.add(lbSpace2);

        btnRevalidate.setText("R");
        btnRevalidate.setToolTipText("Revalidate");
        btnRevalidate.setMinimumSize(new java.awt.Dimension(29, 27));
        btnRevalidate.setPreferredSize(new java.awt.Dimension(29, 27));
        btnRevalidate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRevalidateActionPerformed(evt);
            }
        });
        tbrRemittanceIssue.add(btnRevalidate);

        btnDuplicate.setText("D");
        btnDuplicate.setToolTipText("Duplicate");
        btnDuplicate.setMinimumSize(new java.awt.Dimension(29, 27));
        btnDuplicate.setPreferredSize(new java.awt.Dimension(29, 27));
        btnDuplicate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDuplicateActionPerformed(evt);
            }
        });
        tbrRemittanceIssue.add(btnDuplicate);

        lblSpace5.setText("     ");
        tbrRemittanceIssue.add(lblSpace5);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrRemittanceIssue.add(btnSave);

        lblSpace19.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace19.setText("     ");
        lblSpace19.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace19.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace19.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrRemittanceIssue.add(lblSpace19);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrRemittanceIssue.add(btnCancel);

        lblSpace3.setText("     ");
        tbrRemittanceIssue.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrRemittanceIssue.add(btnAuthorize);

        lblSpace20.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace20.setText("     ");
        lblSpace20.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace20.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace20.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrRemittanceIssue.add(lblSpace20);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrRemittanceIssue.add(btnException);

        lblSpace21.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace21.setText("     ");
        lblSpace21.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrRemittanceIssue.add(lblSpace21);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrRemittanceIssue.add(btnReject);

        lblSpace4.setText("     ");
        tbrRemittanceIssue.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrRemittanceIssue.add(btnPrint);

        lblSpace22.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace22.setText("     ");
        lblSpace22.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrRemittanceIssue.add(lblSpace22);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrRemittanceIssue.add(btnClose);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(tbrRemittanceIssue, gridBagConstraints);

        panDetails.setMinimumSize(new java.awt.Dimension(700, 400));
        panDetails.setPreferredSize(new java.awt.Dimension(700, 400));
        panDetails.setEnabled(false);
        panDetails.setLayout(new java.awt.GridBagLayout());

        panIssueDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Issue Details"));
        panIssueDetails.setMinimumSize(new java.awt.Dimension(700, 570));
        panIssueDetails.setPreferredSize(new java.awt.Dimension(700, 570));
        panIssueDetails.setLayout(new java.awt.GridBagLayout());

        panTableIssue.setMinimumSize(new java.awt.Dimension(280, 400));
        panTableIssue.setPreferredSize(new java.awt.Dimension(180, 400));
        panTableIssue.setLayout(new java.awt.GridBagLayout());

        srpIssue.setMinimumSize(new java.awt.Dimension(280, 175));
        srpIssue.setPreferredSize(new java.awt.Dimension(280, 175));

        tblIssue.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblIssue.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblIssueMousePressed(evt);
            }
        });
        srpIssue.setViewportView(tblIssue);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTableIssue.add(srpIssue, gridBagConstraints);

        PanTotalInstruments.setMinimumSize(new java.awt.Dimension(299, 130));
        PanTotalInstruments.setPreferredSize(new java.awt.Dimension(299, 130));
        PanTotalInstruments.setLayout(new java.awt.GridBagLayout());

        panRITotal.setLayout(new java.awt.GridBagLayout());

        lblTotalInstruments.setText("Inst. Nos");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRITotal.add(lblTotalInstruments, gridBagConstraints);

        txtTotalInstruments.setMinimumSize(new java.awt.Dimension(58, 21));
        txtTotalInstruments.setPreferredSize(new java.awt.Dimension(58, 21));
        panRITotal.add(txtTotalInstruments, new java.awt.GridBagConstraints());

        lblTotalamount.setText("Ttl Amt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        panRITotal.add(lblTotalamount, gridBagConstraints);

        txtTotalamt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTotalamt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalamtActionPerformed(evt);
            }
        });
        txtTotalamt.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                txtTotalamtMouseMoved(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        panRITotal.add(txtTotalamt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        PanTotalInstruments.add(panRITotal, gridBagConstraints);

        panChrg.setLayout(new java.awt.GridBagLayout());

        lblDuplicationCharge.setText("Duplication Charge");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panChrg.add(lblDuplicationCharge, gridBagConstraints);

        txtDuplicationCharge.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDuplicationCharge.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDuplicationChargeFocusLost(evt);
            }
        });
        txtDuplicationCharge.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtDuplicationChargeMousePressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panChrg.add(txtDuplicationCharge, gridBagConstraints);

        lblRevalidationCharge.setText("Revalidation Charge");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panChrg.add(lblRevalidationCharge, gridBagConstraints);

        txtRevalidationCharge.setEditable(false);
        txtRevalidationCharge.setMinimumSize(new java.awt.Dimension(100, 21));
        txtRevalidationCharge.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRevalidationChargeFocusLost(evt);
            }
        });
        txtRevalidationCharge.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtRevalidationChargeMousePressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panChrg.add(txtRevalidationCharge, gridBagConstraints);

        txtRevServTax.setEditable(false);
        txtRevServTax.setMinimumSize(new java.awt.Dimension(100, 21));
        txtRevServTax.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRevServTaxFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panChrg.add(txtRevServTax, gridBagConstraints);

        lblRevServTax.setText("Rev ServiceTax");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panChrg.add(lblRevServTax, gridBagConstraints);

        txtDupServTax.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDupServTax.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDupServTaxFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panChrg.add(txtDupServTax, gridBagConstraints);

        lblDupServTax.setText("Dup ServiceTax");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panChrg.add(lblDupServTax, gridBagConstraints);

        PanTotalInstruments.add(panChrg, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panTableIssue.add(PanTotalInstruments, gridBagConstraints);

        panbtnIssue.setLayout(new java.awt.GridBagLayout());

        btnNewIssue.setText("New");
        btnNewIssue.setEnabled(false);
        btnNewIssue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewIssueActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panbtnIssue.add(btnNewIssue, gridBagConstraints);

        btnSaveIssue.setText("Save");
        btnSaveIssue.setEnabled(false);
        btnSaveIssue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveIssueActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panbtnIssue.add(btnSaveIssue, gridBagConstraints);

        btnDeleteIssue.setText("Delete");
        btnDeleteIssue.setEnabled(false);
        btnDeleteIssue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteIssueActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panbtnIssue.add(btnDeleteIssue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        panTableIssue.add(panbtnIssue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        panIssueDetails.add(panTableIssue, gridBagConstraints);

        panIssue.setMinimumSize(new java.awt.Dimension(500, 645));
        panIssue.setPreferredSize(new java.awt.Dimension(550, 645));
        panIssue.setLayout(new java.awt.GridBagLayout());

        panLeftSubIssue.setMinimumSize(new java.awt.Dimension(230, 430));
        panLeftSubIssue.setPreferredSize(new java.awt.Dimension(250, 430));
        panLeftSubIssue.setLayout(new java.awt.GridBagLayout());

        cboProductId.setMaximumSize(new java.awt.Dimension(100, 21));
        cboProductId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProductId.setPopupWidth(120);
        cboProductId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProductIdActionPerformed(evt);
            }
        });
        cboProductId.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboProductIdItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panLeftSubIssue.add(cboProductId, gridBagConstraints);

        lblProductId.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panLeftSubIssue.add(lblProductId, gridBagConstraints);

        lblCity.setText("City");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panLeftSubIssue.add(lblCity, gridBagConstraints);

        lblDraweeBank.setText("Drawee Bank");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panLeftSubIssue.add(lblDraweeBank, gridBagConstraints);

        lblFavouring.setText("Favouring");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panLeftSubIssue.add(lblFavouring, gridBagConstraints);

        txtFavouring.setMinimumSize(new java.awt.Dimension(100, 21));
        txtFavouring.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFavouringFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panLeftSubIssue.add(txtFavouring, gridBagConstraints);

        lblTransmissionType.setText("Transmission Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panLeftSubIssue.add(lblTransmissionType, gridBagConstraints);

        cboTransmissionType.setMaximumSize(new java.awt.Dimension(100, 21));
        cboTransmissionType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panLeftSubIssue.add(cboTransmissionType, gridBagConstraints);

        lblPANGIRNo.setText("PAN / GIR No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panLeftSubIssue.add(lblPANGIRNo, gridBagConstraints);

        txtPANGIRNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPANGIRNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPANGIRNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panLeftSubIssue.add(txtPANGIRNo, gridBagConstraints);

        lblCategory.setText("Category");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panLeftSubIssue.add(lblCategory, gridBagConstraints);

        cboCategory.setMaximumSize(new java.awt.Dimension(100, 21));
        cboCategory.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCategory.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboCategoryItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panLeftSubIssue.add(cboCategory, gridBagConstraints);

        cboCity.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCityActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panLeftSubIssue.add(cboCity, gridBagConstraints);

        cboDraweeBank.setMinimumSize(new java.awt.Dimension(100, 21));
        cboDraweeBank.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboDraweeBankActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panLeftSubIssue.add(cboDraweeBank, gridBagConstraints);

        cboBranchCode.setMinimumSize(new java.awt.Dimension(100, 21));
        cboBranchCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboBranchCodeActionPerformed(evt);
            }
        });
        cboBranchCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboBranchCodeFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panLeftSubIssue.add(cboBranchCode, gridBagConstraints);

        lblBranchCode.setText("Branch Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panLeftSubIssue.add(lblBranchCode, gridBagConstraints);

        lblAccHead.setText("Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panLeftSubIssue.add(lblAccHead, gridBagConstraints);

        lblAccHeadProdIdDisplay.setText("Test Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panLeftSubIssue.add(lblAccHeadProdIdDisplay, gridBagConstraints);

        lblAmt.setText("Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panLeftSubIssue.add(lblAmt, gridBagConstraints);

        txtAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panLeftSubIssue.add(txtAmt, gridBagConstraints);

        lblExchange.setText("Exchange Collected");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panLeftSubIssue.add(lblExchange, gridBagConstraints);

        txtExchange.setMinimumSize(new java.awt.Dimension(100, 21));
        txtExchange.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtExchangeFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panLeftSubIssue.add(txtExchange, gridBagConstraints);

        lblAccHeadBal.setText("Act. Head Balance");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panLeftSubIssue.add(lblAccHeadBal, gridBagConstraints);

        lblAccHeadBalDisplay.setFont(new java.awt.Font("MS Sans Serif", 0, 12)); // NOI18N
        lblAccHeadBalDisplay.setText("Test Head");
        lblAccHeadBalDisplay.setMaximumSize(new java.awt.Dimension(60, 16));
        lblAccHeadBalDisplay.setMinimumSize(new java.awt.Dimension(60, 16));
        lblAccHeadBalDisplay.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panLeftSubIssue.add(lblAccHeadBalDisplay, gridBagConstraints);

        txtExchange1.setEditable(false);
        txtExchange1.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panLeftSubIssue.add(txtExchange1, gridBagConstraints);

        lblExchange1.setText("Exchange Calculated");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panLeftSubIssue.add(lblExchange1, gridBagConstraints);

        panIssue.add(panLeftSubIssue, new java.awt.GridBagConstraints());

        sptIssue.setOrientation(javax.swing.SwingConstants.VERTICAL);
        sptIssue.setMaximumSize(new java.awt.Dimension(5, 5));
        sptIssue.setMinimumSize(new java.awt.Dimension(5, 5));
        sptIssue.setPreferredSize(new java.awt.Dimension(5, 5));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIssue.add(sptIssue, gridBagConstraints);

        panRightSubIssue.setMinimumSize(new java.awt.Dimension(260, 415));
        panRightSubIssue.setPreferredSize(new java.awt.Dimension(300, 415));
        panRightSubIssue.setLayout(new java.awt.GridBagLayout());

        lblTotalAmt.setText("Total Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panRightSubIssue.add(lblTotalAmt, gridBagConstraints);

        lblPayeeAccHead.setText("Payee Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panRightSubIssue.add(lblPayeeAccHead, gridBagConstraints);

        txtTotalAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panRightSubIssue.add(txtTotalAmt, gridBagConstraints);

        panPayeeAccHead.setMinimumSize(new java.awt.Dimension(129, 29));
        panPayeeAccHead.setLayout(new java.awt.GridBagLayout());

        txtPayeeAccHead.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPayeeAccHead.add(txtPayeeAccHead, gridBagConstraints);

        btnPayeeAccHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnPayeeAccHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnPayeeAccHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPayeeAccHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPayeeAccHead.add(btnPayeeAccHead, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panRightSubIssue.add(panPayeeAccHead, gridBagConstraints);

        txtOtherCharges.setMinimumSize(new java.awt.Dimension(100, 21));
        txtOtherCharges.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtOtherChargesFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panRightSubIssue.add(txtOtherCharges, gridBagConstraints);

        lblPayeeAccNo.setText("Payee Account No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panRightSubIssue.add(lblPayeeAccNo, gridBagConstraints);

        lblOtherCharges.setText("Service Tax");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panRightSubIssue.add(lblOtherCharges, gridBagConstraints);

        lblPostage.setText("Postage");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panRightSubIssue.add(lblPostage, gridBagConstraints);

        txtPostage.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPostage.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPostageFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panRightSubIssue.add(txtPostage, gridBagConstraints);

        lblInstrumentNo.setText("Instrument No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panRightSubIssue.add(lblInstrumentNo, gridBagConstraints);

        panInstrumentNo.setMinimumSize(new java.awt.Dimension(100, 21));
        panInstrumentNo.setLayout(new java.awt.GridBagLayout());

        txtInstrumentNo1.setMinimumSize(new java.awt.Dimension(3, 24));
        txtInstrumentNo1.setPreferredSize(new java.awt.Dimension(30, 21));
        txtInstrumentNo1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtInstrumentNo1FocusLost(evt);
            }
        });
        panInstrumentNo.add(txtInstrumentNo1, new java.awt.GridBagConstraints());

        txtInstrumentNo2.setMinimumSize(new java.awt.Dimension(6, 30));
        txtInstrumentNo2.setPreferredSize(new java.awt.Dimension(66, 21));
        txtInstrumentNo2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtInstrumentNo2FocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        panInstrumentNo.add(txtInstrumentNo2, gridBagConstraints);

        lblInstrumentHIphen.setText("-");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panInstrumentNo.add(lblInstrumentHIphen, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panRightSubIssue.add(panInstrumentNo, gridBagConstraints);

        lblVariableNo.setText("Variable No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panRightSubIssue.add(lblVariableNo, gridBagConstraints);

        txtVariableNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panRightSubIssue.add(txtVariableNo, gridBagConstraints);

        lblRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panRightSubIssue.add(lblRemarks, gridBagConstraints);

        txtRemarks.setMinimumSize(new java.awt.Dimension(100, 21));
        txtRemarks.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtRemarksKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtRemarksKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panRightSubIssue.add(txtRemarks, gridBagConstraints);

        lblCrossing.setText("Crossing");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panRightSubIssue.add(lblCrossing, gridBagConstraints);

        cboCrossing.setMaximumSize(new java.awt.Dimension(100, 21));
        cboCrossing.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCrossing.setPopupWidth(150);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panRightSubIssue.add(cboCrossing, gridBagConstraints);

        cboPayeeProdType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboPayeeProdType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboPayeeProdTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panRightSubIssue.add(cboPayeeProdType, gridBagConstraints);

        lblPayeeProdType.setText("Payee Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panRightSubIssue.add(lblPayeeProdType, gridBagConstraints);

        lblPayeeProdId.setText("Payee Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panRightSubIssue.add(lblPayeeProdId, gridBagConstraints);

        cboPayeeProdId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboPayeeProdId.setPopupWidth(215);
        cboPayeeProdId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboPayeeProdIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panRightSubIssue.add(cboPayeeProdId, gridBagConstraints);

        panDebitAccNo.setLayout(new java.awt.GridBagLayout());

        txtPayeeAccNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDebitAccNo.add(txtPayeeAccNo, gridBagConstraints);

        btnPayeeAccNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnPayeeAccNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnPayeeAccNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPayeeAccNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panDebitAccNo.add(btnPayeeAccNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panRightSubIssue.add(panDebitAccNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panIssue.add(panRightSubIssue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panIssueDetails.add(panIssue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panDetails.add(panIssueDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(panDetails, gridBagConstraints);

        panStatus.setMinimumSize(new java.awt.Dimension(830, 20));
        panStatus.setPreferredSize(new java.awt.Dimension(830, 20));
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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(panStatus, gridBagConstraints);

        panBatchId.setLayout(new java.awt.GridBagLayout());

        lblBatchId.setText("Batch Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBatchId.add(lblBatchId, gridBagConstraints);

        lblDisplayBatchId.setText("test");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBatchId.add(lblDisplayBatchId, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(panBatchId, gridBagConstraints);

        panTransaction.setMinimumSize(new java.awt.Dimension(700, 290));
        panTransaction.setPreferredSize(new java.awt.Dimension(700, 290));
        panTransaction.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(panTransaction, gridBagConstraints);

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
        mnuProcess.add(sptView);

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

        mitClose.setText("Close");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mitClose.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mitCloseMouseClicked(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrMain.add(mnuProcess);

        setJMenuBar(mbrMain);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtRevServTaxFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRevServTaxFocusLost
        // TODO add your handling code here:
         if(observable.isRevInEdit()){
            double revSerTax = CommonUtil.convertObjToDouble(txtRevServTax.getText()).doubleValue();
            double revAmt = CommonUtil.convertObjToDouble(txtRevalidationCharge.getText()).doubleValue();
            txtTotalamt.setText(String.valueOf(revAmt + revSerTax));
            transactionUI.setCallingAmount(String.valueOf(revAmt + revSerTax));
            transactionUI.setPanTransactionDetailsEnableDisable(true);
            transactionUI.setCallingParams();
        }else{
           ClientUtil.showAlertWindow("Cannot Edit Revalidate Service Tax Cancel and start again");
           txtRevServTax.setText(observable.getTxtServiceTax());
       }
    }//GEN-LAST:event_txtRevServTaxFocusLost

    private void txtRevalidationChargeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRevalidationChargeFocusLost
        // TODO add your handling code here:
        if(observable.isRevInEdit()) {
            observable.setTxtServiceTax(observable.calServiceTax(txtRevalidationCharge.getText(),productId,category,txtAmt.getText(), "REVALIDATE_CHARGE", "", ""));
            txtRevServTax.setText(observable.getTxtServiceTax());
            double revAmt = CommonUtil.convertObjToDouble(txtRevalidationCharge.getText()).doubleValue();
            double revSerTax = CommonUtil.convertObjToDouble(txtRevServTax.getText()).doubleValue();
            txtTotalamt.setText(String.valueOf(revAmt + revSerTax));
            transactionUI.setCallingAmount(String.valueOf(revAmt + revSerTax));
            transactionUI.setPanTransactionDetailsEnableDisable(true);
            transactionUI.setCallingParams();
       }else{
           ClientUtil.showAlertWindow("Cannot Edit Revalidation Charges Cancel and start again");
           txtRevalidationCharge.setText(observable.getTxtRevalidationCharge());
       }
    }//GEN-LAST:event_txtRevalidationChargeFocusLost

    private void cboProductIdItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboProductIdItemStateChanged
        // TODO add your handling code here:
        String prodID = CommonUtil.convertObjToStr((((ComboBoxModel)(cboProductId).getModel())).getKeyForSelected());
         if((transactionUI.getCallingStatus().equals("REMIT_DUP")) && (!isFill)){
//         if((prodID.equals("PO")) && (transactionUI.getCallingStatus().equals("REMIT_DUP")) && (!isFill)){COMMENTED DURING TNSC
//             if((transactionUI.getCallingUiMode() != ClientConstants.ACTIONTYPE_EDIT) && 
//             (transactionUI.getCallingUiMode() != ClientConstants.ACTIONTYPE_DELETE)){
              if (viewType != DUPLICATE){
            observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
//            observable.setStatus();
//            lblStatus.setText(observable.getLblStatus());
            isFill = true;
            popUp(DUPLICATE);
//            isFill = true;
            btnNew.setEnabled(false);
            btnEdit.setEnabled(false);
            btnDelete.setEnabled(false);
//            btnCancel.setEnabled(false);
             }
        }
        if((transactionUI.getCallingStatus().equals("REMIT_REV")) && (!isFill)){
//             if((prodID.equals("PO")) && (transactionUI.getCallingStatus().equals("REMIT_REV")) && (!isFill)){
//             if((transactionUI.getCallingUiMode() != ClientConstants.ACTIONTYPE_EDIT) && 
//             (transactionUI.getCallingUiMode() != ClientConstants.ACTIONTYPE_DELETE)){
              if (viewType != REVALIDATE){
            observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
//            observable.setStatus();
//            lblStatus.setText(observable.getLblStatus());
            isFill = true;
            popUp(REVALIDATE);
//            isFill = true;
            btnNew.setEnabled(false);
            btnEdit.setEnabled(false);
            btnDelete.setEnabled(false);
//            btnCancel.setEnabled(false);
             }
        }
    }//GEN-LAST:event_cboProductIdItemStateChanged

    private void txtDupServTaxFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDupServTaxFocusLost
        // TODO add your handling code here:
//         observable.setTxtServiceTax(observable.calServiceTax(txtDuplicationCharge.getText(),productId,category,txtAmt.getText(), "DUPLICATE_CHARGE"));
//        txtDupServTax.setText(observable.getTxtServiceTax());
        if(observable.isDupInEdit()){
            double dupSerTax = CommonUtil.convertObjToDouble(txtDupServTax.getText()).doubleValue();
            double dupAmt = CommonUtil.convertObjToDouble(txtDuplicationCharge.getText()).doubleValue();
            txtTotalamt.setText(String.valueOf(dupAmt + dupSerTax));
            transactionUI.setCallingAmount(String.valueOf(dupAmt + dupSerTax));
            transactionUI.setPanTransactionDetailsEnableDisable(true);
            transactionUI.setCallingParams();
        }else{
           ClientUtil.showAlertWindow("Cannot Edit Duplicate Service Tax Cancel and start again");
           txtDupServTax.setText(observable.getTxtServiceTax());
       }
    }//GEN-LAST:event_txtDupServTaxFocusLost

    private void txtDuplicationChargeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDuplicationChargeFocusLost
        // TODO add your handling code here:
//        double amt = CommonUtil.convertObjToDouble(txtAmt.getText()).doubleValue();
//        observable.setTxtDuplicationCharge(txtDuplicationCharge.getText());
       if(observable.isDupInEdit()) {
            observable.setTxtServiceTax(observable.calServiceTax(txtDuplicationCharge.getText(),productId,category,txtAmt.getText(), "DUPLICATE_CHARGE", "", ""));
            txtDupServTax.setText(observable.getTxtServiceTax());
            double dupAmt = CommonUtil.convertObjToDouble(txtDuplicationCharge.getText()).doubleValue();
            double dupSerTax = CommonUtil.convertObjToDouble(txtDupServTax.getText()).doubleValue();
            txtTotalamt.setText(String.valueOf(dupAmt + dupSerTax));
            transactionUI.setCallingAmount(String.valueOf(dupAmt + dupSerTax));
            transactionUI.setPanTransactionDetailsEnableDisable(true);
            transactionUI.setCallingParams();
       }else{
           ClientUtil.showAlertWindow("Cannot Edit Duplicate Charges Cancel and start again");
           txtDuplicationCharge.setText(observable.getTxtDuplicationCharge());
       }
    }//GEN-LAST:event_txtDuplicationChargeFocusLost

    private void txtInstrumentNo1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInstrumentNo1FocusLost
        // TODO add your handling code here:
        txtInstrumentNoFocusLost();
    }//GEN-LAST:event_txtInstrumentNo1FocusLost

    private void txtInstrumentNo2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInstrumentNo2FocusLost
        // TODO add your handling code here:
      txtInstrumentNoFocusLost();
    }//GEN-LAST:event_txtInstrumentNo2FocusLost

    private void txtInstrumentNoFocusLost(){
         HashMap dataMap = new HashMap();
        dataMap.put("INSTRUMENT_NO1", txtInstrumentNo1.getText());
        dataMap.put("INSTRUMENT_NO2", txtInstrumentNo2.getText());
        List lst = ClientUtil.executeQuery("checkValidInsNum", dataMap);
        dataMap = null;
        if(lst != null && lst.size() > 0){
            dataMap = (HashMap) lst.get(0);
            String authStatus = CommonUtil.convertObjToStr(dataMap.get("AUTHORIZE_STATUS"));
            if(authStatus.equalsIgnoreCase("")){
                ClientUtil.showAlertWindow("Instrument "+txtInstrumentNo1.getText()+"-"+txtInstrumentNo2.getText()+" already issued and pending for Authorization");
                txtInstrumentNo2.setText("");
            }else if(dataMap.get("AUTHORIZE_STATUS").equals("AUTHORIZED")){
                ClientUtil.showAlertWindow("Instrument "+txtInstrumentNo1.getText()+"-"+txtInstrumentNo2.getText()+" already issued");
                txtInstrumentNo2.setText("");
            }
        }else{
            //DO NOTHING
        }   
    }
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        popUp(VIEW);
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed

    private void txtTotalamtMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTotalamtMouseMoved
        // TODO add your handling code here:
        txtTotalamt.setToolTipText(observable.getTxtTotalAmt());
    }//GEN-LAST:event_txtTotalamtMouseMoved

    private void mitCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mitCloseMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_mitCloseMouseClicked
    
    private void btnPayeeAccNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPayeeAccNoActionPerformed
        if (cboPayeeProdType.getSelectedIndex() > 0 && CommonUtil.convertObjToStr(cboPayeeProdId.getSelectedItem()).trim().length() > 0)
            popUp(PAYEE_ACC_NUM);
    }//GEN-LAST:event_btnPayeeAccNoActionPerformed
    
    private void cboPayeeProdIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPayeeProdIdActionPerformed
        // Add your handling code here:
        //To Set the Value of Account Head in UI...
        if (cboPayeeProdId.getSelectedIndex() > 0) {
            observable.setCboPayeeProductId(CommonUtil.convertObjToStr( cboPayeeProdId.getSelectedItem()));
            if( observable.getCboPayeeProductId().length() > 0){
                //When the selected Product Id is not empty string
                observable.setAccountHead();
                txtPayeeAccHead.setText(observable.getTxtPayeeAccHead());
                enablePayeeHead(true);
            }
            else{
                enablePayeeHead(false);
            }
        }
    }//GEN-LAST:event_cboPayeeProdIdActionPerformed
    
    private void cboPayeeProdTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPayeeProdTypeActionPerformed
        //To Set the Value of Account Head in UI...
        if (cboPayeeProdType.getSelectedIndex() > 0) {
            String prodType = ((ComboBoxModel)cboPayeeProdType.getModel()).getKeyForSelected().toString();
            System.out.println("#########cboPayeeProdTypeActionPerformed"+prodType); 
            observable.setCboPayeeProductType(prodType);
            observable.setCbmPayeeProductId(prodType);
            cboPayeeProdId.setModel(observable.getCbmPayeeProductId());
            txtPayeeAccNo.setText("");
            txtPayeeAccHead.setText("");
            enablePayee(true);
        }
        else{
            txtPayeeAccNo.setText("");
            txtPayeeAccHead.setText("");
            cboPayeeProdId.setModel(new ComboBoxModel());
            enablePayee(false);
            enablePayeeHead(false);
        }
    }//GEN-LAST:event_cboPayeeProdTypeActionPerformed
     private void btnCheck(){
         btnCancel.setEnabled(true);
         btnSave.setEnabled(false);
         btnNew.setEnabled(false);
         btnDelete.setEnabled(false);
         btnAuthorize.setEnabled(false);
         btnReject.setEnabled(false);


         btnException.setEnabled(false);
         btnEdit.setEnabled(false);
     }
    /*
    private boolean validatePAN(){
        boolean valid = true ;
        if(txtPANGIRNo.getText()!=null && txtPANGIRNo.getText()!=""){
            txtPANGIRNo.setText(txtPANGIRNo.getText().toUpperCase());
            String panNum = txtPANGIRNo.getText();
     
            if(panNum.length()<10)
                valid = false;
     
            if(valid){
                for(int i = 0, j = panNum.length() ; i < j; i++){
                    if(i < 5 || i == 9){
                        if((int)panNum.charAt(i) < 65 || (int)panNum.charAt(i) > 91)
                            valid = false ;
                    }
                    if(i >= 5 && i < 9){
                        if(panNum.charAt(i) < 48 || panNum.charAt(i) > 57)
                            valid = false ;
                    }
                }
            }
            if(!valid){
                txtPANGIRNo.setText("");
            }
        }
        return valid ;
    }*/
    
    private void enablePayee(boolean flag){
        cboPayeeProdId.setEnabled(flag);
        
    }
    
    private void enablePayeeHead(boolean flag){
        txtPayeeAccHead.setEnabled(flag);
        txtPayeeAccNo.setEnabled(flag);
        btnPayeeAccHead.setEnabled(flag);
        btnPayeeAccNo.setEnabled(flag);
    }
    
    private void txtRemarksKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRemarksKeyPressed
                            }//GEN-LAST:event_txtRemarksKeyPressed
    
    private void txtRemarksKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRemarksKeyTyped
        
    }//GEN-LAST:event_txtRemarksKeyTyped
    
    private void txtFavouringFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFavouringFocusLost
        //This code allows user to enter alpha numeric combination
        //only numeric is not allowed
        //only alpha is allowed
        try{
            Double.parseDouble(txtFavouring.getText());
            
            txtFavouring.setText("");
        }catch(Exception E){
        }
    }//GEN-LAST:event_txtFavouringFocusLost
    
    private void txtPANGIRNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPANGIRNoFocusLost
                            }//GEN-LAST:event_txtPANGIRNoFocusLost
    
    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
      //  ClientUtil.showReport("DDIssueRegister");
         HashMap reportParamMap = new HashMap();
 com.see.truetransact.clientutil.ttrintegration.LinkReport.getReports(getScreenID(), reportParamMap);
    }//GEN-LAST:event_btnPrintActionPerformed
    
    private void txtDuplicationChargeMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtDuplicationChargeMousePressed
        
    }//GEN-LAST:event_txtDuplicationChargeMousePressed
    
    private void txtRevalidationChargeMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtRevalidationChargeMousePressed
        
    }//GEN-LAST:event_txtRevalidationChargeMousePressed
    
    private void txtTotalamtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalamtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalamtActionPerformed
    
    private void tblIssueMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblIssueMousePressed
        // TODO add your handling code here:
        if (tblIssue.getSelectedRow() >= 0) {
            boolean isAuthorized = false;
//            if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE){
                tableIssueMousePressed = true;
                setTableIssueEnableDisable(true);
                setSelectRow(tblIssue.getSelectedRow());
                observable.setSelectRow(tblIssue.getSelectedRow());
                flag = false;
                isAuthorized = observable.populateSelectedIssueDetails(observable.getSelectRow());
                exg = observable.getOldExg();
                pos = observable.getOldPos();
                st = observable.getOldSt();
                flag = true;
                if(observable.getActionType()== ClientConstants.ACTIONTYPE_NEW){
                    setPayeeAccHDButtonEnableDisable(true);
                    setPanIssueDetailsEnableDisable(true);
                    btnSaveIssue.setEnabled(true);
                    btnDeleteIssue.setEnabled(true);
                }else{
                    setPayeeAccHDButtonEnableDisable(false);
                }
                if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ){
                    if((isAuthorized) && (!duplicate) && (!revalidate)){
//                        setRevalidationDuplicationEnableDisable(true);
                        setPanIssueDetailsEnableDisable(false);
                        btnSaveIssue.setEnabled(true);
                        btnDeleteIssue.setEnabled(false);
                    } else {
                        setIssueButtonEnableDisable(true);
                        setPanIssueDetailsEnableDisable(true);
//                        btnRevalidate.setEnabled(true);//changed 10/10/208
//                        btnDuplicate.setEnabled(true);//changed 10/10/208
                        btnNewIssue.setEnabled(false);
                        btnDeleteIssue.setEnabled(false);
                        setTotalAmtDisable();
//                        duplicate = false;
                    }
                    checkPrintedServices();
                    if(duplicate){
//                        if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
//                            setDupChrAndTaxEnableDisable(false);
//                        }
//                        transactionUI.setCallingUiMode(0);
                        if(transactionUI.getCallingUiMode() == ClientConstants.ACTIONTYPE_DELETE){
                            setPanIssueDetailsEnableDisable(false);
                              cboPayeeProdId.setEnabled(false);
                              btnPayeeAccNo.setEnabled(false);
                              cboCity.setEnabled(false);
                              cboDraweeBank.setEnabled(false);
                              cboBranchCode.setEnabled(false);
                              btnSaveIssue.setEnabled(false);
                        }else{
                              duplicate = false;
                        ClientUtil.enableDisable(panLeftSubIssue,false);
                        txtPostage.setEnabled(false);
                        txtPostage.setEditable(false);
                        txtOtherCharges.setEnabled(false);
                        txtOtherCharges.setEditable(false);
                        txtTotalAmt.setEnabled(false);
                        txtTotalAmt.setEditable(false);
                        txtRemarks.setEnabled(false);
                        txtRemarks.setEditable(false);
                        cboPayeeProdType.setEnabled(false);
                        cboPayeeProdType.setEditable(false);
                        cboPayeeProdId.setEnabled(false);
                        cboPayeeProdId.setEditable(false);
                        panPayeeAccHead.setEnabled(false);
//                        panPayeeAccHead.setEditable(false);
                        txtPayeeAccNo.setEnabled(false);
                        txtPayeeAccNo.setEditable(false);
                        btnPayeeAccNo.setEnabled(false);
                        cboCrossing.setEnabled(true);
//                        ClientUtil.enableDisable(panRightSubIssue,false);
                        txtInstrumentNo1.setEnabled(true);
//                        txtInstrumentNo1.setEditable(true);
                        txtInstrumentNo2.setEnabled(true);
//                        txtDuplicationCharge.setEnabled(false);
//                        txtDupServTax.setEnabled(false);
//                        txtInstrumentNo2.setEditable(true);
                        if ((viewType == DUPLICATE) && (!observable.isDupInEdit()) && (!observable.isDupInDelete())){
                            updateOBFields();
                            observable.setSelectRow(tblIssue.getSelectedRow());
                            try{
                                observable.setOperationMode(CommonConstants.REMIT_DUPLICATE);
                                RemitIssueDuplicateUI objRemitIssueDuplicateUI = new RemitIssueDuplicateUI();
                                objRemitIssueDuplicateUI.setRowCnt(CommonUtil.convertObjToStr(observable.getSelectRow()));
                                objRemitIssueDuplicateUI.setTitle(resourceBundle.getString("titleForDuplication"));
                                amount = txtAmt.getText();
                                observable.setTxtDuplicationCharge(observable.executeQueryForCharge(productId, category, amount, "DUPLICATE_CHARGE"));
                                observable.setTxtServiceTax(observable.calServiceTax(observable.getTxtDuplicationCharge(),productId,category,amount, "DUPLICATE_CHARGE", "", ""));
                                objRemitIssueDuplicateUI.setCharges(observable.getTxtDuplicationCharge(),observable.getTxtServiceTax(),productId,category,amount,observable.getPayableAt(),observable.getCboDraweeBank(),observable.getCboBranchCode());
                                objRemitIssueDuplicateUI.show();
                                objRemitIssueDuplicateUI = null;
                                if(observable.getDup() == -1){
                                    lblDupServTax.setVisible(true);
                                    txtDupServTax.setVisible(true);
                                    observable.updateOBWithDuplication();
                                    double dupAmt = CommonUtil.convertObjToDouble(observable.getTxtDuplicationCharge()).doubleValue();
                                    double taxAmt = CommonUtil.convertObjToDouble(observable.getTxtServiceTax()).doubleValue();
                                    double callingAmt = CommonUtil.convertObjToDouble(observable.getTxtDuplicationCharge()).doubleValue();
                                    double setAmt = CommonUtil.convertObjToDouble(txtTotalamt.getText()).doubleValue();
                                    if((callingAmt + taxAmt) != 0){
                                        transactionUI.setCallingAmount(String.valueOf(callingAmt + taxAmt));
                                        transactionUI.setPanTransactionDetailsEnableDisable(true);
                                        txtDuplicationCharge.setText(observable.getTxtDuplicationCharge());
                                        txtDupServTax.setText(observable.getTxtServiceTax());
                                        txtTotalamt.setText(String.valueOf(callingAmt + setAmt + taxAmt));
                                        observable.setDupTotAmt(txtTotalamt.getText());
                                        transactionUI.setCallingParams();
                                    }else{
                                        btnDuplicate.setEnabled(false);
                                        transactionUI.setBtnSaveTransactionDetailsFlag(true);
                                        transactionUI.setMainEnableDisable(false);
                                        double transTotalAmt = CommonUtil.convertObjToDouble(transactionUI.getTransactionOB().getLblTotalTransactionAmtVal()).doubleValue();
                                        if(transTotalAmt == 0){
                                            transactionUI.getTransactionOB().setLblTotalTransactionAmtVal(txtTotalamt.getText());
                                        }
                                    }
                                    }else{
                                    //do nothing
                                }
                                ClientUtil.showMessageWindow("Enter the new Instrument No.");
                                    txtInstrumentNo2.setText("");
                            }catch(Exception e){
                                System.out.println("  "+e);
                            }
                            viewType = observable.getDup();
                            
                        }
                        
                        }
                    }
                    if(revalidate){
                         if(transactionUI.getCallingUiMode() == ClientConstants.ACTIONTYPE_DELETE){
                            setPanIssueDetailsEnableDisable(false);
                              cboPayeeProdId.setEnabled(false);
                              btnPayeeAccNo.setEnabled(false);
                              cboCity.setEnabled(false);
                              cboDraweeBank.setEnabled(false);
                              cboBranchCode.setEnabled(false);
                              btnSaveIssue.setEnabled(false);
                        }else{
//                              revalidate = false;
                        ClientUtil.enableDisable(panLeftSubIssue,false);
                        txtPostage.setEnabled(false);
                        txtPostage.setEditable(false);
                        txtOtherCharges.setEnabled(false);
                        txtOtherCharges.setEditable(false);
                        txtTotalAmt.setEnabled(false);
                        txtTotalAmt.setEditable(false);
                        txtRemarks.setEnabled(false);
                        txtRemarks.setEditable(false);
                        cboPayeeProdType.setEnabled(false);
                        cboPayeeProdType.setEditable(false);
                        cboPayeeProdId.setEnabled(false);
                        cboPayeeProdId.setEditable(false);
                        panPayeeAccHead.setEnabled(false);
//                        panPayeeAccHead.setEditable(false);
                        txtPayeeAccNo.setEnabled(false);
                        txtPayeeAccNo.setEditable(false);
                        btnPayeeAccNo.setEnabled(false);
                        cboCrossing.setEnabled(false);
//                        ClientUtil.enableDisable(panRightSubIssue,false);
                        txtInstrumentNo1.setEnabled(false);
//                        txtInstrumentNo1.setEditable(true);
                        txtInstrumentNo2.setEnabled(false);
//                        txtDuplicationCharge.setEnabled(false);
//                        txtDupServTax.setEnabled(false);
//                        txtInstrumentNo2.setEditable(true);
                        if ((viewType == REVALIDATE) && (!observable.isRevInEdit()) && (!observable.isRevInDelete())){
                            updateOBFields();
                            observable.setSelectRow(tblIssue.getSelectedRow());
                            try{
                                observable.setOperationMode(CommonConstants.REMIT_REVALIDATE);
                                RemitIssueRevalidateUI objRemitIssueRevalidateUI = new RemitIssueRevalidateUI();
                                objRemitIssueRevalidateUI.setRowCnt(CommonUtil.convertObjToStr(observable.getSelectRow()));
                                objRemitIssueRevalidateUI.setTitle(resourceBundle.getString("titleForRevalidation"));
                                amount = txtAmt.getText();
                                observable.setTxtRevalidationCharge(observable.executeQueryForCharge(productId, category, amount, "REVALIDATE_CHARGE"));
                                observable.setTxtServiceTax(observable.calServiceTax(observable.getTxtRevalidationCharge(),productId,category,amount,"REVALIDATE_CHARGE", "", ""));
                                objRemitIssueRevalidateUI.setCharges(observable.getTxtRevalidationCharge(),observable.getTxtServiceTax(),productId,category,amount,observable.getPayableAt(),observable.getCboDraweeBank(),observable.getCboBranchCode());
                                objRemitIssueRevalidateUI.show();
                                objRemitIssueRevalidateUI = null;
                                if(observable.getDup() == -1){
                                    lblRevServTax.setVisible(true);
                                    txtRevServTax.setVisible(true);
                                    observable.updateOBWithRevalidate();
                                    double revAmt = CommonUtil.convertObjToDouble(observable.getTxtRevalidationCharge()).doubleValue();
                                    double taxAmt = CommonUtil.convertObjToDouble(observable.getTxtServiceTax()).doubleValue();
                                    double callingAmt = CommonUtil.convertObjToDouble(observable.getTxtRevalidationCharge()).doubleValue();
                                    double setAmt = CommonUtil.convertObjToDouble(txtTotalamt.getText()).doubleValue();
                                    if((callingAmt + taxAmt) != 0){
                                        transactionUI.setCallingAmount(String.valueOf(callingAmt + taxAmt));
                                        transactionUI.setPanTransactionDetailsEnableDisable(true);
                                        txtRevalidationCharge.setText(observable.getTxtRevalidationCharge());
                                        txtRevServTax.setText(observable.getTxtServiceTax());
                                        txtTotalamt.setText(String.valueOf(callingAmt + setAmt + taxAmt));
                                        observable.setRevTotAmt(txtTotalamt.getText());
                                        transactionUI.setCallingParams();
                                        
                                    }else{
                                         btnDuplicate.setEnabled(false);
                                         transactionUI.setBtnSaveTransactionDetailsFlag(true);
                                         transactionUI.setMainEnableDisable(false);
                                         double transTotalAmt = CommonUtil.convertObjToDouble(transactionUI.getTransactionOB().getLblTotalTransactionAmtVal()).doubleValue();
                                        if(transTotalAmt == 0){
                                            transactionUI.getTransactionOB().setLblTotalTransactionAmtVal(txtTotalamt.getText());
                                        }
                                    } 
                                }else{
                                    //do nothing
                                }
//                                ClientUtil.showMessageWindow("Enter the new Instrument No.");
                                    txtInstrumentNo2.setEnabled(false);
                                    txtInstrumentNo1.setEnabled(false);
                            }catch(Exception e){
                                System.out.println("  "+e);
                            }
                            viewType = observable.getDup();
                            
                        }
                        
                        }
                    }
                    if(observable.depositFlag == true){
                        ClientUtil.enableDisable(this,false);
                        btnSave.setEnabled(false);
                    }
                    cboProductId.setEnabled(false);
                }
//            }
              if (viewType== AUTHORIZE ){
                  setPanIssueDetailsEnableDisable(false);
                  cboPayeeProdId.setEnabled(false);
                  btnPayeeAccNo.setEnabled(false);
                  cboCity.setEnabled(false);
                  cboDraweeBank.setEnabled(false);
                  cboBranchCode.setEnabled(false);
                  isRemitTblClicked = true;
              }
//              txtDuplicationCharge.setEditable(false);
//                txtDupServTax.setEditable(false);
//                txtDuplicationCharge.setEnabled(false);
//                txtDupServTax.setEnabled(false);
//                txtTotalamt.setEnabled(false);
//                txtTotalamt.setEditable(false);  
                txtVariableNo.setEnabled(false);
                txtExchange1.setEnabled(false);
        }
    }//GEN-LAST:event_tblIssueMousePressed
                        private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
                            setModified(true);
                            setTableIssueEnableDisable(true);
                            observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
                            observable.setOperationMode(CommonConstants.REMIT_ISSUE);
                            observable.setDup(0);
                            transactionUI.setCallingStatus("");
                            observable.setStatus();
                            observable.setOldTransDetMap(null);
                            btnNewIssue.setEnabled(true);
                            setButtonEnableDisable();
                            transactionUI.cancelAction(false);
//                            transactionUI.setButtonEnableDisable(true);
                            transactionUI.resetObjects();
                            transactionUI.setMainEnableDisable(false);
                            transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
                            btnAuthorize.setEnabled(false);
                            btnReject.setEnabled(false);
                            btnException.setEnabled(false);
                            setRevalidationDuplicationEnableDisable(false);
    }//GEN-LAST:event_btnNewActionPerformed
                                                                                    private void cboBranchCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboBranchCodeFocusLost
                                                                                        // TODO add your handling code here:
//                                                                                        if((observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT )||(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW )){
//                                                                                            setValuesFor(productId,null,bankCode,branchCode,null);
//                                                                                            if (branchCode.length() > 0) {
//                                                                                                observable.setBranchIdFromTrueTransactMain(TrueTransactMain.BRANCH_ID);
//                                                                                                isBranchCodeNotValid = observable.validateBranchId(observable.getBranchIdFromTrueTransactMain() ,bankCode,branchCode, productId );
//                                                                                            }
//                                                                                        }
    }//GEN-LAST:event_cboBranchCodeFocusLost
    public void setDupChrAndTaxEnableDisable(boolean flag){
          txtDuplicationCharge.setEditable(flag);
          txtDupServTax.setEditable(flag);  
          txtDuplicationCharge.setEnabled(flag);
          txtDupServTax.setEnabled(flag);    
    }
              
     public void setRevChrAndTaxEnableDisable(boolean flag){
          txtRevalidationCharge.setEditable(flag);
          txtRevServTax.setEditable(flag);  
          txtRevalidationCharge.setEnabled(flag);
          txtRevServTax.setEnabled(flag);    
    }
     public void setDupChrAndTaxValues(){
        double dupAmt = CommonUtil.convertObjToDouble(txtDuplicationCharge.getText()).doubleValue();
        double dupSerTax = CommonUtil.convertObjToDouble(txtDupServTax.getText()).doubleValue();
        double totAmt = CommonUtil.convertObjToDouble(txtTotalamt.getText()).doubleValue();
        txtTotalamt.setText(String.valueOf(totAmt - (dupAmt + dupSerTax)));
        observable.setDupTotAmt(txtTotalamt.getText());
        txtDuplicationCharge.setText("");
        txtDupServTax.setText("");
           
    }
     public void setRevChrAndTaxValues(){
        double revAmt = CommonUtil.convertObjToDouble(txtRevalidationCharge.getText()).doubleValue();
        double revSerTax = CommonUtil.convertObjToDouble(txtRevServTax.getText()).doubleValue();
        double totAmt = CommonUtil.convertObjToDouble(txtTotalamt.getText()).doubleValue();
        txtTotalamt.setText(String.valueOf(totAmt - (revAmt + revSerTax)));
        observable.setRevTotAmt(txtTotalamt.getText());
        txtRevalidationCharge.setText("");
        txtRevServTax.setText("");
           
    }
    private void cboCityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCityActionPerformed
        // TODO add your handling code here:
        if((observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT )||(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW )){
            String city = CommonUtil.convertObjToStr((((ComboBoxModel)(cboCity).getModel())).getKeyForSelected());
            if(city.length()>0){
                setValuesFor(productId,null,null,null,null);
                if(productId.length()>0) {
                    observable.populateDraweeBrank(productId,city);
                    cboDraweeBank.setModel(observable.getCbmDraweeBank());
                    cboBranchCode.setModel(new ComboBoxModel());
                }else if(observable.getCboProductId().length() == 0){
                    cboDraweeBank.setModel(new ComboBoxModel());
                    cboBranchCode.setModel(new ComboBoxModel());
                }
            }else {
                cboDraweeBank.setModel(new ComboBoxModel());
                cboBranchCode.setModel(new ComboBoxModel());
            }
        }
    }//GEN-LAST:event_cboCityActionPerformed
    
    private void cboCategoryItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboCategoryItemStateChanged
        // Add your handling code here:
        setValuesFor(productId,category,null,null,amount);
        System.out.println(productId + " ==== " + category + "===" + amount);
        if(flag == true || btnNewIsPressed == true) {
            if (productId.length() > 0 &&  category.length() > 0 && amount.length() > 0) {
                observable.setTxtExchange(txtExchange.getText());
                observable.setTxtOtherCharges(txtOtherCharges.getText());
                observable.setSelectRow(tblIssue.getSelectedRow());
                observable.populateExchange(productId, category, amount,btnNewIsPressed);
                //                btnNewIsPressed = false;
                updateExchange();
                updateOBCharges();
                observable.addAllCharges();
                updateCharges();
                txtExchange1.setText(observable.getTxtExchange());
            }
        }
    }//GEN-LAST:event_cboCategoryItemStateChanged
    
    private void btnDuplicateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDuplicateActionPerformed
        // Add your handling code here:
        transactionUI.setCallingStatus("REMIT_DUP");
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_EDIT);
        if (viewType != DUPLICATE){
            if (cboProductId.getSelectedItem().equals("")){
                displayAlert("select the prodid");
                cboProductId.setEnabled(true);
            }else{
                observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
                observable.setStatus();
                lblStatus.setText(observable.getLblStatus());
                popUp(DUPLICATE);
                btnNew.setEnabled(false);
                btnEdit.setEnabled(false);
                btnDelete.setEnabled(false);
            }
            
        } else if (viewType == DUPLICATE){
            updateOBFields();
            observable.setSelectRow(tblIssue.getSelectedRow());
            try{
                observable.setOperationMode(CommonConstants.REMIT_DUPLICATE);
                RemitIssueDuplicateUI objRemitIssueDuplicateUI = new RemitIssueDuplicateUI();
                objRemitIssueDuplicateUI.setRowCnt(CommonUtil.convertObjToStr(observable.getSelectRow()));
                objRemitIssueDuplicateUI.setTitle(resourceBundle.getString("titleForDuplication"));
                amount = txtAmt.getText();
                observable.setTxtDuplicationCharge(observable.executeQueryForCharge(productId, category, amount, "DUPLICATE_CHARGE"));
                observable.setTxtServiceTax(observable.calServiceTax(observable.getTxtDuplicationCharge(),productId,category,amount, "DUPLICATE_CHARGE", "",""));
//                objRemitIssueDuplicateUI.setCharges(observable.getTxtDuplicationCharge(),observable.getTxtServiceTax());
                objRemitIssueDuplicateUI.show();
                objRemitIssueDuplicateUI = null;
                if(observable.getDup() == -1){
                    lblDupServTax.setVisible(true);
                    txtDupServTax.setVisible(true);
                    observable.updateOBWithDuplication();
                    double dupAmt = CommonUtil.convertObjToDouble(observable.getTxtDuplicationCharge()).doubleValue();
                    double taxAmt = CommonUtil.convertObjToDouble(observable.getTxtServiceTax()).doubleValue();
                    double callingAmt = CommonUtil.convertObjToDouble(observable.getTxtDuplicationCharge()).doubleValue();
                    double setAmt = CommonUtil.convertObjToDouble(txtTotalamt.getText()).doubleValue();
                    transactionUI.setCallingAmount(String.valueOf(callingAmt + taxAmt));
                    transactionUI.setPanTransactionDetailsEnableDisable(true);
                    txtDuplicationCharge.setText(observable.getTxtDuplicationCharge());
                    txtDupServTax.setText(observable.getTxtServiceTax());
                    txtTotalamt.setText(String.valueOf(callingAmt + setAmt + taxAmt));
                    transactionUI.setCallingParams();
                    btnDuplicate.setEnabled(false);
                }else{
                    //do nothing
                }
//             updateOBFields();
//                observable.setSelectRow(tblIssue.getSelectedRow());
//                try{
//                    RemitIssueDuplicateUI objRemitIssueDuplicateUI = new RemitIssueDuplicateUI();
////                    objRemitIssueDuplicateUI.setRowCnt(CommonUtil.convertObjToStr(observable.getSelectRow()));
//                    objRemitIssueDuplicateUI.setTitle(resourceBundle.getString("titleForDuplication"));
//                    objRemitIssueDuplicateUI.show();
//                    objRemitIssueDuplicateUI = null;
//                }catch(Exception e){
//                    System.out.println("  "+e);
//>>>>>>> 1.31
//                }
            }catch(Exception e){
                System.out.println("  "+e);
            }
            viewType = observable.getDup();
        }
    }//GEN-LAST:event_btnDuplicateActionPerformed
    
    private void btnRevalidateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRevalidateActionPerformed
        // Add your handling code here:
//        if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ){
//            observable.setOperationMode(CommonConstants.REMIT_REVALIDATE);
//            String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panIssue);
//            if (mandatoryMessage.length() > 0){
//                displayAlert(mandatoryMessage);
//            }else{
//                updateOBFields();
//                observable.setSelectRow(tblIssue.getSelectedRow());
//                try{
//                    RemitIssueRevalidateUI objRevalidateUI = new RemitIssueRevalidateUI();
//                    objRevalidateUI.setTitle(resourceBundle.getString("titleForRevalidation"));
//                    objRevalidateUI.show();
//                    objRevalidateUI = null;
//                }catch(Exception e){
//                    System.out.println("   "+e);
//                }
//            }
//        }
         transactionUI.setCallingStatus("REMIT_REV");
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_EDIT);
        if (viewType != DUPLICATE){
            if (cboProductId.getSelectedItem().equals("")){
                displayAlert("select the prodid");
                cboProductId.setEnabled(true);
            }else{
                observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
                observable.setStatus();
                lblStatus.setText(observable.getLblStatus());
                popUp(REVALIDATE);
                btnNew.setEnabled(false);
                btnEdit.setEnabled(false);
                btnDelete.setEnabled(false);
                btnCancel.setEnabled(true);
            }
            
        } else if (viewType == REVALIDATE){
            updateOBFields();
            observable.setSelectRow(tblIssue.getSelectedRow());
            try{
                observable.setOperationMode(CommonConstants.REMIT_REVALIDATE);
                RemitIssueRevalidateUI objRemitIssueRevalidateUI = new RemitIssueRevalidateUI();
//                objRemitIssueRevalidateUI.setRowCnt(CommonUtil.convertObjToStr(observable.getSelectRow()));
//                objRemitIssueRevalidateUI.setTitle(resourceBundle.getString("titleForDuplication"));
//                amount = txtAmt.getText();
//                observable.setTxtDuplicationCharge(observable.executeQueryForCharge(productId, category, amount, "DUPLICATE_CHARGE"));
//                observable.setTxtServiceTax(observable.calServiceTax(observable.getTxtDuplicationCharge(),productId,category,amount, "DUPLICATE_CHARGE"));
//                objRemitIssueRevalidateUI.setCharges(observable.getTxtDuplicationCharge(),observable.getTxtServiceTax());
                objRemitIssueRevalidateUI.show();
                objRemitIssueRevalidateUI = null;
                if(observable.getDup() == -1){
                    lblDupServTax.setVisible(true);
                    txtDupServTax.setVisible(true);
                    observable.updateOBWithDuplication();
                    double dupAmt = CommonUtil.convertObjToDouble(observable.getTxtDuplicationCharge()).doubleValue();
                    double taxAmt = CommonUtil.convertObjToDouble(observable.getTxtServiceTax()).doubleValue();
                    double callingAmt = CommonUtil.convertObjToDouble(observable.getTxtDuplicationCharge()).doubleValue();
                    double setAmt = CommonUtil.convertObjToDouble(txtTotalamt.getText()).doubleValue();
                    transactionUI.setCallingAmount(String.valueOf(callingAmt + taxAmt));
                    transactionUI.setPanTransactionDetailsEnableDisable(true);
                    txtDuplicationCharge.setText(observable.getTxtDuplicationCharge());
                    txtDupServTax.setText(observable.getTxtServiceTax());
                    txtTotalamt.setText(String.valueOf(callingAmt + setAmt + taxAmt));
                    transactionUI.setCallingParams();
                    btnDuplicate.setEnabled(false);
                }else{
                    //do nothing
                }
//             updateOBFields();
//                observable.setSelectRow(tblIssue.getSelectedRow());
//                try{
//                    RemitIssueDuplicateUI objRemitIssueDuplicateUI = new RemitIssueDuplicateUI();
////                    objRemitIssueDuplicateUI.setRowCnt(CommonUtil.convertObjToStr(observable.getSelectRow()));
//                    objRemitIssueDuplicateUI.setTitle(resourceBundle.getString("titleForDuplication"));
//                    objRemitIssueDuplicateUI.show();
//                    objRemitIssueDuplicateUI = null;
//                }catch(Exception e){
//                    System.out.println("  "+e);
//>>>>>>> 1.31
//                }
                btnCancel.setEnabled(true);
            }catch(Exception e){
                System.out.println("  "+e);
            }
            viewType = observable.getDup();
        }
    }//GEN-LAST:event_btnRevalidateActionPerformed
    
    private void updateOBCharges(){
        observable.setTxtExchange(txtExchange.getText());
//        observable.setTxtExchange1(txtExchange1.getText());
        observable.setTxtPostage(txtPostage.getText());
        observable.setTxtOtherCharges(txtOtherCharges.getText());
        observable.setTxtRevalidationCharge(txtRevalidationCharge.getText());
        observable.setTxtDuplicationCharge(txtDuplicationCharge.getText());
        observable.setTxtDupServTax(txtDupServTax.getText());
        observable.setTxtRevServTax(txtRevServTax.getText());
        observable.setTxtAmt(txtAmt.getText());
        observable.setTxtTotalAmt(txtTotalAmt.getText());
    }
    
    private void updateProductId(){
        cboProductId.setSelectedItem(observable.getCboProductId());
    }
    
    private void updateExchange(){
        txtExchange.setText(observable.getTxtExchange());
//        txtExchange1.setText(observable.getTxtExchange());
        txtPostage.setText(observable.getTxtPostage());
        txtOtherCharges.setText(observable.getTxtOtherCharges());
    }
    
    private void updateAmount(){
        txtTotalAmt.setText(observable.getTxtAmt());
    }
    
    private void updateCharges(){
        updateExchange();
        txtPostage.setText(observable.getTxtPostage());
        txtOtherCharges.setText(observable.getTxtOtherCharges());
        updateAmount();
        transactionUI.setCallingAmount(txtTotalamt.getText());
        //txtTotalAmt.setText(observable.getTxtTotalAmt());
    }
    
    private void cboBranchCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboBranchCodeActionPerformed
        // Add your handling code here:
        setValuesFor(productId,null,null,null,null);
    }//GEN-LAST:event_cboBranchCodeActionPerformed
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
        btnNew.setEnabled(false);
        btnSave.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_REJECT);
        btnNew.setEnabled(false);
        btnSave.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
//        btnReject.setEnabled(true);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
        btnNew.setEnabled(false);
        btnSave.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
  /* public void authorizeStatus(String authorizeStatus) {
        if (viewType == AUTHORIZE && isFilled){
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("TODAY_DT", currDt);
            singleAuthorizeMap.put("BATCH_ID", strBatchID);
            ClientUtil.execute("authorizeRemitIssue", singleAuthorizeMap);
            btnCancelActionPerformed(null);
            viewType = -1;
        } else{
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            mapParam.put(CommonConstants.MAP_NAME, "getRemitIssueAuthorizeTOList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeRemitIssue");
            isFilled = false;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
        }
    }*/
   public void authorizeStatus(String authorizeStatus) {

        if (viewType != AUTHORIZE){
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
//            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setStatus();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            mapParam.put(CommonConstants.MAP_NAME, "getRemitIssueAuthorizeTOList");
            viewType = AUTHORIZE;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            isFilled = false;
            authorizeUI.show();
            
        } 
        else if (viewType == AUTHORIZE){
            if(isRemitTblClicked && transactionUI.isIsTranTblClicked()){
	    if(!strBatchID.equals("")){
                ArrayList arrList = new ArrayList();
                HashMap authorizeMap = new HashMap();
                HashMap singleAuthorizeMap = new HashMap();
                singleAuthorizeMap.put("STATUS", authorizeStatus);
                singleAuthorizeMap.put("BATCH_ID", strBatchID);
                System.out.println("singleAuthorizeMap^^^^"+singleAuthorizeMap);
                arrList.add(singleAuthorizeMap);
                authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
                authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
                authorize(authorizeMap);
                isRemitTblClicked = false;
                transactionUI.setIsTranTblClicked(false);
                strBatchID = ""; 
            }
            viewType = -1 ;
            }else{
                if(isRemitTblClicked == false && transactionUI.isIsTranTblClicked() == false){
                    ClientUtil.showMessageWindow("View both Remit Issue and Transaction Details");
                }else if(isRemitTblClicked == false && transactionUI.isIsTranTblClicked()){
                    ClientUtil.showMessageWindow("View Remit Issue Details");
                }else if(isRemitTblClicked == true && transactionUI.isIsTranTblClicked() == false){
                     ClientUtil.showMessageWindow("View Transaction Details");
                }
            }
        }
    }

  
    public void authorize(HashMap map) {
        System.out.println("Authorize Map : " + map);
        
        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
//            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setAuthorizeMap(map);
            observable.doAction();
           if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED) { 
            super.setOpenForEditBy(observable.getStatusBy());
            super.removeEditLock(txtVariableNo.getText());
            btnCancelActionPerformed(null);
            observable.resetForm();
            observable.setResultStatus();
             observable.setAuthorizeMap(null);
        }
//            lblStatus.setText(ClientConstants.ACTION_STATUS[observable.getActionType()]);            
        }
    }
    
    private void cboDraweeBankActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboDraweeBankActionPerformed
        // Add your handling code here:
        setValuesFor(productId,null,bankCode,null,null);
        if(productId.length()>0 && bankCode.length()>0){
            observable.populateBranchCode(productId,bankCode);
            cboBranchCode.setModel(observable.getCbmBranchCode());
        }else if(productId.length()==0 || bankCode.length()==0){
            cboBranchCode.setModel(new ComboBoxModel());
        }
    }//GEN-LAST:event_cboDraweeBankActionPerformed
    
    private void txtPostageFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPostageFocusLost
        // Add your handling code here:
        updateOBCharges();
        observable.addAllCharges();
        updateCharges();
         if((transactionUI.getCallingUiMode() == ClientConstants.ACTIONTYPE_EDIT) && (observable.getOperationMode().equals(CommonConstants.REMIT_ISSUE))){
            if(CommonUtil.convertObjToDouble(pos).doubleValue() > 0){
            double postg = CommonUtil.convertObjToDouble(txtPostage.getText()).doubleValue();
            if(postg == 0){
                ClientUtil.showAlertWindow("Postage cannot be Zero");
//                txtExchange.setText(exg);
                txtPostage.setText(pos);
//                txtOtherCharges.setText(st);
                updateOBCharges();
                observable.addAllCharges();
                updateCharges();
            }
            }
        }
    }//GEN-LAST:event_txtPostageFocusLost
    
    private void txtOtherChargesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtOtherChargesFocusLost
        // Add your handling code here:
        txtOtherChargesFocusLost();
//        updateOBCharges();
//        observable.addAllCharges();
//        updateCharges();
//        if((transactionUI.getCallingUiMode() == ClientConstants.ACTIONTYPE_EDIT) && (observable.getOperationMode().equals(CommonConstants.REMIT_ISSUE))){
//            if(CommonUtil.convertObjToDouble(st).doubleValue() > 0){
//            double stx = CommonUtil.convertObjToDouble(txtOtherCharges.getText()).doubleValue();
//            if(stx == 0){
//                ClientUtil.showAlertWindow("Service Tax cannot be Zero");
////                txtExchange.setText(exg);
////                txtPostage.setText(pos);
//                txtOtherCharges.setText(st);
//                updateOBCharges();
//                observable.addAllCharges();
//                updateCharges();
//            }
//            }
//        }
    }//GEN-LAST:event_txtOtherChargesFocusLost
    private void txtOtherChargesFocusLost(){
           updateOBCharges();
        observable.addAllCharges();
        updateCharges();
        if((transactionUI.getCallingUiMode() == ClientConstants.ACTIONTYPE_EDIT) && (observable.getOperationMode().equals(CommonConstants.REMIT_ISSUE))){
            if(CommonUtil.convertObjToDouble(st).doubleValue() > 0){
            double stx = CommonUtil.convertObjToDouble(txtOtherCharges.getText()).doubleValue();
            if(stx == 0){
                ClientUtil.showAlertWindow("Service Tax cannot be Zero");
//                txtExchange.setText(exg);
//                txtPostage.setText(pos);
                txtOtherCharges.setText(st);
                updateOBCharges();
                observable.addAllCharges();
                updateCharges();
            }
            }
        }
    }
    private void txtAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAmtFocusLost
        // Add your handling code here:
        if (CommonUtil.convertObjToDouble(this.txtAmt.getText()).doubleValue() <= 0){
            ClientUtil.displayAlert("amount should not be zero or empty");
        }
        cboCategoryItemStateChanged(null);
        setValuesFor(productId,category,bankCode,branchCode,amount);
        amtLimit = observable.checkAmountLimit(productId,bankCode,branchCode,amount);
        updateOBCharges();
        observable.addAllCharges();
        updateCharges();
        if (flag == true ||btnNewIsPressed == true) {
            if(productId.length() > 0 && category.length() > 0 && amount.length() > 0) {
                observable.setTxtExchange(txtExchange.getText());
                observable.setSelectRow(tblIssue.getSelectedRow());
                observable.populateExchange(productId,category,amount,btnNewIsPressed);
                //                btnNewIsPressed = false;
                updateExchange();
            }
        }
        
    }//GEN-LAST:event_txtAmtFocusLost
    
    private void txtExchangeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtExchangeFocusLost
        // Add your handling code here:
//        updateOBCharges();
//        observable.addAllCharges();
//        updateCharges();
        setValuesFor(productId,category,null,null,amount);
        observable.setTxtOtherCharges(observable.calServiceTax(txtExchange.getText(),productId,category,amount, "EXCHANGE", "", ""));
        txtOtherCharges.setText(observable.getTxtOtherCharges());
        updateOBCharges();
        observable.addAllCharges();
        updateCharges();
        if((transactionUI.getCallingUiMode() == ClientConstants.ACTIONTYPE_EDIT) && (observable.getOperationMode().equals(CommonConstants.REMIT_ISSUE))){
            if(CommonUtil.convertObjToDouble(exg).doubleValue() > 0){
            double exchange = CommonUtil.convertObjToDouble(txtExchange.getText()).doubleValue();
            if(exchange == 0){
                ClientUtil.showAlertWindow("Exchange cannot be Zero");
                txtExchange.setText(exg);
//                txtPostage.setText(pos);
                txtOtherCharges.setText(st);
                updateOBCharges();
                observable.addAllCharges();
                updateCharges();
            }
            }
            txtOtherChargesFocusLost();
        }
    }//GEN-LAST:event_txtExchangeFocusLost
    
    /* If PRINT_SERVICES is No in the TABLE REMITTANCE_PRODUCT
     * for the selected productId then disable crossing field
     */
    private void checkPrintedServices() {
        String printServices = observable.getPrintServices(productId);
        if(printServices.equals("N")){
            cboCrossing.setEnabled(false);
        }
    }
    /* To set the selected productId */
    private void updateProdIdAndAcctHead(){
        updateProductId();
        lblAccHeadProdIdDisplay.setText(observable.getLblAccountHeadDisplay());
        lblAccHeadBalDisplay.setText(observable.getLblAccHeadBalDisplay());
    }
    private void cboProductIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProductIdActionPerformed
        // Add your handling code here:
        String city="", drawBank="", bankCode="";  //This line added by Rajesh
        observable.setCboProductId(CommonUtil.convertObjToStr(cboProductId.getSelectedItem()));
        productId = observable.getCboProductId();
        System.out.println("##### Product ID : " + productId);
        setValuesFor(productId,null,null,null,null);
        if( productId.length() > 0){
            //  When the selected ProductId is not empty string
            observable.getAccountHeadForProduct(productId);
            updateProdIdAndAcctHead();
            checkPrintedServices();// Disable CboCrossing if PrintedService is No
            observable.populateCity();
            cboCity.setModel(observable.getCbmCity());
            observable.populateCategory(productId);
            cboCategory.setModel(observable.getCbmCategory());
            //The following if condition given by Rajesh
            if(productId.equals("PO")) {
//                cboCity.setEnabled(false);
//                cboCity.setEditable(false);
//                cboDraweeBank.setEnabled(false);
//                cboDraweeBank.setEditable(false);
//                cboBranchCode.setEnabled(false);
//                cboBranchCode.setEditable(false);
                if(cboCity.getItemCount()>=1){
                    
                    cboCity.setSelectedItem(cboCity.getItemAt(1));
//                    observable.getCbmCity().getKey(1);
                    city=CommonUtil.convertObjToStr(observable.getCbmCity().getKey(1));
                }
                observable.populateDraweeBrank(productId,city);
                cboDraweeBank.setModel(observable.getCbmDraweeBank());
                if(cboDraweeBank.getItemCount()>=1) {
                    cboDraweeBank.setSelectedItem(cboDraweeBank.getItemAt(1));
//                    drawBank=CommonUtil.convertObjToStr(cboDraweeBank.getSelectedItem());
                    drawBank=CommonUtil.convertObjToStr(observable.getCbmDraweeBank().getKey(1));
                }
                observable.populateBranchCode(productId, drawBank);
                cboBranchCode.setModel(observable.getCbmBranchCode());
                if(cboBranchCode.getItemCount()>=1) {
                    cboBranchCode.setSelectedItem(cboBranchCode.getItemAt(1));
                    bankCode=CommonUtil.convertObjToStr(cboBranchCode.getSelectedItem());
                    
                }
                System.out.println("City : " + city + "  Drawee Bank : " + drawBank + "  Branch Code : " + bankCode);
                //txtFavouring.setFocusable(true);
            }
            else {
                cboCity.setEnabled(true);
                cboCity.setEditable(true);
                cboDraweeBank.setEnabled(true);
                cboDraweeBank.setEditable(true);
                cboBranchCode.setEnabled(true);
                cboBranchCode.setEditable(true);
            }
            txtInstrumentNo1.setText(productId);
        }else if(observable.getCboProductId().length() == 0){
            observable.setLblAccountHeadDisplay("");
            cboCity.setModel(new ComboBoxModel());
            cboCategory.setModel(new ComboBoxModel());
            updateProdIdAndAcctHead();
        }
        
        if (flag == true || btnNewIsPressed == true) {
            setValuesFor(productId,category,null,null,amount);
            if(productId.length()>0 && category.length()>0 && amount.length()>0){
                observable.setTxtExchange(txtExchange.getText());
                observable.setSelectRow(tblIssue.getSelectedRow());
                observable.populateExchange(productId,category,amount,btnNewIsPressed);
                //                btnNewIsPressed = false;
                updateExchange();
            }
        }
       
    }//GEN-LAST:event_cboProductIdActionPerformed
    /* To enable or disable the main New Save Delete buttons in the pane */
    private void  setIssueButtonEnableDisable(boolean flag){
        btnNewIssue.setEnabled(flag);
        btnSaveIssue.setEnabled(flag);
        btnDeleteIssue.setEnabled(flag);
    }
    //   To set selected row from IssueDetails Table
    private void setSelectRow(int SelectedRow){
        selectedRowValue = SelectedRow;
    }
    //    To get selected row from IssueDetails Table
    private int  getSelectRow(){
        return selectedRowValue;
    }
    // To set the Revalidation Duplication enable disable
    public void setRevalidationDuplicationEnableDisable(boolean flag){
        //        btnCancelation.setEnabled(flag);
        btnRevalidate.setEnabled(flag);
        btnDuplicate.setEnabled(flag);
    }
    public void setDuplicationEnableDisable(boolean flag){
        //        btnCancelation.setEnabled(flag);
        btnRevalidate.setEnabled(flag);
        btnDuplicate.setEnabled(flag);
    }
    private void btnDeleteIssueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteIssueActionPerformed
        // Add your handling code here:
        if (tblIssue.getSelectedRow() >= 0) {
            setNewDeleteIssueButtons();
            btnSaveIssue.setEnabled(false);
            rowCount = observable.deleteRecord(getSelectRow());
            if (rowCount == 0){
                // Entire rows are deleted in the table issue then save action is called
                rowCount = -1;
                observable.setTxtTotalInstruments("");
                observable.setTxtTotalIssueAmt("");
                btnSaveActionPerformed(null);
            }
            setPanIssueDetailsEnableDisable(false);
            setRevalidationDuplicationEnableDisable(true);//changed 10/10/2008
            observable.resetIssueDetails();
            tableIssueMousePressed = false;
        }
    }//GEN-LAST:event_btnDeleteIssueActionPerformed
    private void setNewDeleteIssueButtons() {
        btnNewIssue.setEnabled(!btnNewIssue.isEnabled()); // I've added !
        btnDeleteIssue.setEnabled(!btnNewIssue.isEnabled());
    }
    
    private void btnNewIssueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewIssueActionPerformed
        // Add your handling code here:
        btnNewIsPressed = true;
        tableIssueMousePressed = false;
        setNewDeleteIssueButtons();
        btnSaveIssue.setEnabled(true);
        observable.resetIssueDetails();
        setPanIssueDetailsEnableDisable(true);
        setTotalAmtDisable();
        setPayeeAccHDButtonEnableDisable(true);
        setRevalidationDuplicationEnableDisable(false);//changed 10/10/2008
//        txtInstrumentNo1.setEnabled(false);
//        txtInstrumentNo2.setEnabled(false);
//        txtInstrumentNo1.setEditable(false);
//        txtInstrumentNo2.setEditable(false);
//        txtInstrumentNo1.setText("PO");
        enablePayee(false);
        enablePayeeHead(false);
        txtPayeeAccHead.setEnabled(false);
        transactionUI.setButtonEnableDisable(true);
        ClientUtil.enableDisable(panRightSubIssue,true);
        txtPostage.setEditable(true);
        txtOtherCharges.setEditable(true);
        txtPostage.setEnabled(true);
        txtOtherCharges.setEnabled(true);
        txtRemarks.setEditable(true);
        txtRemarks.setEnabled(true);
        txtVariableNo.setEnabled(false);
        txtExchange1.setEnabled(false);
    }//GEN-LAST:event_btnNewIssueActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed
    
    //enabling input  fields issue details
    public void setPanIssueDetailsEnableDisable(boolean flag){
        ClientUtil.enableDisable(panLeftSubIssue,flag);
        ClientUtil.enableDisable(panRightSubIssue,flag);
    }
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // Add your handling code here:
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void enableAuthorizeButton(boolean flag){
        btnAuthorize.setEnabled(flag);
        btnReject.setEnabled(flag);
        btnException.setEnabled(flag);
    }
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        setIssueButtonEnableDisable(false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        if(observable.getAuthorizeStatus()!=null)
            super.removeEditLock(txtVariableNo.getText());   
        ClientUtil.enableDisable(this, false);
        setButtonEnableDisable();
        setPayeeAccHDButtonEnableDisable(false);
        btnPayeeAccNo.setEnabled(false);
        setRevalidationDuplicationEnableDisable(true);//changed 10/10/2008
        enableAuthorizeButton(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
        observable.resetForm();
        observable.resetIssueDetails();
        observable.setStatus();
        observable.destroyAllowedIssue();
        observable.destroyDupIssue();
        observable.setDupTotAmt("");
        observable.setRevTotAmt("");
        observable.setDup(0);
        observable.setDupInEdit(false);
        observable.setDupInDelete(false);
        observable.setRevInDelete(false);
        observable.setRevInDelete(false);
        transactionUI.setCallingStatus("");
        transactionUI.setMainEnableDisable(false);  //setButtonEnableDisable(true) 
        setEnable();
        setModified(false);
        viewType = -1;
        txtTotalamt.setText("");
        observable.setOperationMode("");
        observable.setTranCnt(0);
        isFill = false;
        revalidate = false;
        observable.setAuthRemarks("");
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        isRemitTblClicked = false;
        transactionUI.setIsTranTblClicked(false);
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnPayeeAccHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPayeeAccHeadActionPerformed
        // Add your handling code here:
        popUp(PAYEE);
    }//GEN-LAST:event_btnPayeeAccHeadActionPerformed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // Add your handling code here:
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // Add your handling code here:
        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        setModified(true);
        popUp(DELETE);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_DELETE);
        if(observable.getOperationMode().equals(CommonConstants.REMIT_DUPLICATE)){
            observable.setDupInDelete(true);
            observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
            observable.setStatus();
        }else if(observable.getOperationMode().equals(CommonConstants.REMIT_REVALIDATE)){
            observable.setRevInDelete(true);
            observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
            observable.setStatus();
        }else{
            observable.setOperationMode(CommonConstants.REMIT_ISSUE); 
        }
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnSave.setEnabled(true);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        setModified(true);
        popUp(EDIT);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_EDIT);
        if(observable.getOperationMode().equals(CommonConstants.REMIT_DUPLICATE)){
            observable.setDupInEdit(true);
            observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
            observable.setStatus();
        }
        else if(observable.getOperationMode().equals(CommonConstants.REMIT_REVALIDATE)){
            observable.setRevInEdit(true);
            observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
            observable.setStatus();
        }
        else
           observable.setOperationMode(CommonConstants.REMIT_ISSUE); 
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed
    /** To display a popUp window for viewing existing data */
    private void popUp(int field) {
        if (field == PAYEE || field == DEBIT) {
            lblStatus.setText(ClientConstants.ACTION_STATUS[observable.getActionType()]);
        } else {
            lblStatus.setText(ClientConstants.ACTION_STATUS[0]);
        }
        final HashMap viewMap = new HashMap();
        viewType = field;
        if(field==EDIT || field==DELETE || field ==VIEW){//Edit=0 and Delete=1
            ArrayList lst = new ArrayList();
            lst.add("VARIABLE_NO");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
             HashMap where = new HashMap();
//            where.put("PROD_ID", ((ComboBoxModel)cboProductId.getModel()).getKeyForSelected());
            where.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
//            where.put("BANK_CODE", TrueTransactMain.BANK_ID);
            viewMap.put(CommonConstants.MAP_WHERE, where);
            where = null ;
            viewMap.put(CommonConstants.MAP_NAME, "viewRemitIssue");
        }else if(field==TRANS_PROD){
            viewMap.put(CommonConstants.MAP_NAME, "InterMaintenance.getProductData" + observable.getCbmProductType().getKeyForSelected().toString());
        }else if(field==ACC_NUM){
            viewMap.put(CommonConstants.MAP_NAME, "Remittance.getAccountData" + observable.getCbmProductType().getKeyForSelected().toString());
            HashMap where_map = new HashMap();
            ////            where_map.put(CommonConstants.PRODUCT_ID, txtTransProductId.getText());
            viewMap.put(CommonConstants.MAP_WHERE, where_map);
        }else if(field==PAYEE_ACC_NUM){
            String prodType = ((ComboBoxModel)cboPayeeProdType.getModel()).getKeyForSelected().toString();
            String prodId = ((ComboBoxModel)cboPayeeProdId.getModel()).getKeyForSelected().toString();
            if(prodType != null && prodId != null){
                System.out.println("prodType : " + prodType);
                viewMap.put(CommonConstants.MAP_NAME, "Remittance.getAccountData" + prodType);
                HashMap where_map = new HashMap();
                where_map.put(CommonConstants.PRODUCT_ID, prodId);
                viewMap.put(CommonConstants.MAP_WHERE, where_map);
            }
        }else if(field == DUPLICATE){
            HashMap where = new HashMap();
            where.put("PROD_ID", ((ComboBoxModel)cboProductId.getModel()).getKeyForSelected());
            where.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            where.put("BANK_CODE", TrueTransactMain.BANK_ID);
            viewMap.put(CommonConstants.MAP_WHERE, where);
            where = null ;
            viewMap.put(CommonConstants.MAP_NAME, "getCancelIssues");
            observable.setDup(DUPLICATE);
            viewType = -1;
        }else if(field == REVALIDATE){
            HashMap where = new HashMap();
            where.put("PROD_ID", ((ComboBoxModel)cboProductId.getModel()).getKeyForSelected());
            where.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            where.put("BANK_CODE", TrueTransactMain.BANK_ID);
            where.put("CUR_DT", currDt.clone());
            viewMap.put(CommonConstants.MAP_WHERE, where);
            where = null ;
            viewMap.put(CommonConstants.MAP_NAME, "getRevalidateList");
            observable.setDup(REVALIDATE);
            viewType = -1;
        }else {
            viewMap.put(CommonConstants.MAP_NAME, "OperativeAcctProduct.getSelectAcctHeadTOList");
        }
        new ViewAll(this, viewMap).show();
    }
    
    private boolean inFillData(HashMap hashMap) {
        hashMap.put(CommonConstants.MAP_WHERE, hashMap.get("BATCH_ID"));
        strBatchID = CommonUtil.convertObjToStr(hashMap.get("BATCH_ID"));
        variableNo = CommonUtil.convertObjToStr(hashMap.get("VARIABLE_NO"));
        observable.setBatchId(strBatchID);
        observable.setTxtVariableNo(variableNo);
        boolean authorize =  observable.populateData(hashMap);
        hashMap = null;
        if(viewType!= AUTHORIZE)
            setButtonEnableDisable();
        ClientUtil.enableDisable(this, false);
        setPayeeAccHDButtonEnableDisable(false);
       
        return authorize;
    }
    
    /** Called by the Popup window created thru popUp method
     * @param param
     */
    public void fillData(Object param) {
        isFilled = true;
        final HashMap hash = (HashMap) param;
        if((!(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE)) &&
        (!(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT)))
            //        &&
            //        ((observable.isDupInEdit() == true) || (observable.isDupInDelete() == true)))
        {
            String authStatus = CommonUtil.convertObjToStr(hash.get("AUTHORIZE_STATUS"));
//            if(authStatus.equals("")){
                String varNum = CommonUtil.convertObjToStr(hash.get("VARIABLE_NO"));
                HashMap newDupMap = new HashMap();
                newDupMap.put("NEW_VARIABLE_NO", varNum);
                List dupList =  ClientUtil.executeQuery("getDupOldVariableNo", newDupMap);
                List revList =  ClientUtil.executeQuery("getRevOldVariableNo", newDupMap);
                newDupMap = null;
                if(dupList != null && dupList.size() > 0 && authStatus.equals("")){
                    transactionUI.setCallingStatus("REMIT_DUP");
                    viewType = DUPLICATE;
                    observable.setOperationMode(CommonConstants.REMIT_DUPLICATE);
                    transactionUI.setRemittanceIssue(this);
                    varNum = "";
                }
                 if(revList != null && revList.size() > 0 && authStatus.equals("")){
                    transactionUI.setCallingStatus("REMIT_REV");
                    viewType = REVALIDATE;
                    observable.setOperationMode(CommonConstants.REMIT_REVALIDATE);
                    transactionUI.setRemittanceIssue(this);
                    varNum = "";
                }
                setRevalidationDuplicationEnableDisable(false);
//            }
        }
        //        double dupCharge = CommonUtil.convertObjToDouble(hash.get("DUPLICATE_CHARGE")).doubleValue();
        //        if(dupCharge > 0){
        //            transactionUI.setCallingStatus("REMIT_DUP");
        //            viewType = DUPLICATE;
        //            observable.setOperationMode(CommonConstants.REMIT_DUPLICATE);
        //            transactionUI.setRemittanceIssue(this);
        //        }
        if(observable.getDup() == DUPLICATE){
            viewType = DUPLICATE;
            observable.setOperationMode(CommonConstants.REMIT_DUPLICATE);
            transactionUI.setRemittanceIssue(this);
            setRevalidationDuplicationEnableDisable(false);
        }
        
        if(observable.getDup() == REVALIDATE){
            viewType = REVALIDATE;
            observable.setOperationMode(CommonConstants.REMIT_REVALIDATE);
            transactionUI.setRemittanceIssue(this);
            setRevalidationDuplicationEnableDisable(false);
        }
        if (viewType != -1) {
            if (viewType==PAYEE) {
                final String accountHead=CommonUtil.convertObjToStr(hash.get("AC_HD_ID"));
                txtPayeeAccHead.setText(accountHead);
            }else if (viewType == TRANS_PROD) {
                final String productId=CommonUtil.convertObjToStr(hash.get("PROD_ID"));
                
            }else if (viewType == ACC_NUM) {
                final String accountNum=CommonUtil.convertObjToStr(hash.get("ACT_NUM"));
            }else if (viewType == PAYEE_ACC_NUM) {
                txtPayeeAccNo.setText(CommonUtil.convertObjToStr(hash.get("ACT_NUM")));
                transactionUI.setSourceAccountNumber(txtPayeeAccNo.getText());
            }else if ( viewType==EDIT || viewType==DUPLICATE || viewType==REVALIDATE) {
                System.out.println("Hash = " + hash);
                btnNewIssue.setEnabled(true);
                setTableIssueEnableDisable(true);
                
                if (inFillData(hash)){
//                    setRevalidationDuplicationEnableDisable(true);
                    setPanIssueDetailsEnableDisable(false);
                } else {
//                    setRevalidationDuplicationEnableDisable(true);//changed 10/10/2008
                    setTotalAmtDisable();
                }
                btnDeleteIssue.setEnabled(false);
                observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
                observable.setStatus();
                transactionUI.setButtonEnableDisable(false);
                btnNewIssue.setEnabled(false);
                if(viewType==DUPLICATE){
                    duplicate = true;
                    //                    setPanIssueDetailsEnableDisable(true);
                    btnSave.setEnabled(false);
                    btnCancel.setEnabled(true);
//                    setRevalidationDuplicationEnableDisable(true);
                    if(transactionUI.getCallingUiMode() == ClientConstants.ACTIONTYPE_DELETE){
                        btnSave.setEnabled(true);
                    }
                   observable.setActionType(ClientConstants.ACTIONTYPE_DUPLICATE);
                    observable.setStatus();
                    observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
                }
                 if(viewType==REVALIDATE){
                    revalidate = true;
                    //                    setPanIssueDetailsEnableDisable(true);
                    btnSave.setEnabled(false);
                    btnCancel.setEnabled(true);
//                    setRevalidationDuplicationEnableDisable(true);
                    if(transactionUI.getCallingUiMode() == ClientConstants.ACTIONTYPE_DELETE){
                        btnSave.setEnabled(true);
                    }
                    //                    double transTotalAmt = CommonUtil.convertObjToDouble(transactionUI.getTransactionOB().getLblTotalTransactionAmtVal()).doubleValue();
                    //                   observable.setTotAmt(String.valueOf(transTotalAmt));
                   observable.setActionType(ClientConstants.ACTIONTYPE_REVALIDATE);
                    observable.setStatus();
                    observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
                }
                //btnSave.setEnabled(false);
            } else if (viewType == VIEW) {
                System.out.println("Hash = " + hash);
                btnNewIssue.setEnabled(true);
                setTableIssueEnableDisable(true);
                if (inFillData(hash)){
//                    setRevalidationDuplicationEnableDisable(true);
                    setPanIssueDetailsEnableDisable(false);
                } else {
//                    setRevalidationDuplicationEnableDisable(true);//changed 10/10/2008
                    setTotalAmtDisable();
                }
                btnDeleteIssue.setEnabled(false);
                observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
                observable.setStatus();
                transactionUI.setButtonEnableDisable(false);
                btnNewIssue.setEnabled(false);
               
                //btnSave.setEnabled(false);
            }else if ( viewType==DELETE ) {
                observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
                setTableIssueEnableDisable(true);
                inFillData(hash);
//                setRevalidationDuplicationEnableDisable(true);//changed 10/10/2008
                observable.setStatus();
            }else if (viewType== AUTHORIZE ) {
                inFillData(hash);
                btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
            }
        }
        if (viewType== DUPLICATE ) {
                HashMap parMap = new HashMap();
            parMap.put("PROD_ID", hash.get("PROD_ID"));
            Date issueDate = (Date)hash.get("BATCH_DT");
            Date curDt = (Date) currDt.clone();
            List lapseLst = (List) ClientUtil.executeQuery("getLapsePrd", parMap);
            parMap = null;
            if(lapseLst != null && lapseLst.size() > 0){
                parMap = (HashMap)lapseLst.get(0);
                double period = CommonUtil.convertObjToDouble(parMap.get("LAPSE_PERIOD")).doubleValue();
                double days = (curDt.getTime() - issueDate.getTime())/1000/60/60/24;
                if(days > period){
                    ClientUtil.showAlertWindow(resourceBundle.getString("warningMsg"));
                   btnSave.setEnabled(false); 
                   tblIssue.setEnabled(false);
//                   cboPayStatus.setEnabled(false);
//                   txtRemarks.setEditable(false);
//                   txtRemarks.setEnabled(false);
                }else{
                    tblIssue.setEnabled(true);
                     btnSave.setEnabled(true);
//                     cboPayStatus.setEnabled(true);
//                     txtRemarks.setEditable(true);
//                     txtRemarks.setEnabled(true);
                }
            }
            }
        if((observable.getTranCnt() > 1) && (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE) && (!hash.get("REMARKS").equals("DUPLICATE")) &&
        (!hash.get("REMARKS").equals("REVALIDATED"))){
            ClientUtil.showAlertWindow("Cannot Edit/Delete, Reject the Issue");
            btnSave.setEnabled(false);
        }
        
    }
     /** Display message  */
//    private void ShowDialogue(String warningMessage){
//        String[] options = {resourceBundle.getString("cDialogOK")};
//        if(resourceBundle.getString(warningMessage).equals(resourceBundle.getString("printedNumberMsg"))){
//            COptionPane.showOptionDialog(null,resourceBundle.getString(warningMessage)+ observable.getDuplicateDt(), CommonConstants.WARNINGTITLE,
//            COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
//            null, options, options[0]);
//        }else{
//            COptionPane.showOptionDialog(null,resourceBundle.getString(warningMessage), CommonConstants.WARNINGTITLE,
//            COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
//            null, options, options[0]);
//        }
//        return;
//    }
    private void setEnable() {
        btnNew.setEnabled(true);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
    }
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        //transactionUI.isBtnSaveTransactionDetailsFlag() returns true if save is clicked
        //btnSaveIssue.isEnabled() returns false if save is clicked
        int transactionSize = 0 ;
        if(transactionUI.getOutputTO() == null){
            observable.showAlertWindow(resourceBundle.getString("NoRecords"));
        }
        else{
            transactionSize = (transactionUI.getOutputTO()).size();
            observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
            observable.setProcChargeMap(transactionUI.getProcOutTo());
//            if(observable.getOperationMode().equals(CommonConstants.REMIT_DUPLICATE)){
//                observable.setDupTransactionDetailsTO(transactionUI.getDupOutputTO());
//            }
                
        }
        
        if(transactionSize != 0){
            //If transactions exist, proceed to save them
            if (tblIssue.getRowCount() == 0 &&  transactionSize  == 0) {
                if (btnSaveIssue.isEnabled() && !transactionUI.isBtnSaveTransactionDetailsFlag()) {
                    observable.showAlertWindow(resourceBundle.getString("saveBoth"));
                } else {
                    observable.showAlertWindow(resourceBundle.getString("NoRecords"));
                }
            } else if (tblIssue.getRowCount()>0 &&  transactionSize  > 0) {
                if (btnSaveIssue.isEnabled() && !transactionUI.isBtnSaveTransactionDetailsFlag()) {
                    observable.showAlertWindow(resourceBundle.getString("saveBoth"));
                } else if (btnSaveIssue.isEnabled()) {
                    observable.showAlertWindow(resourceBundle.getString("saveInIssueTable"));
                } else if(!transactionUI.isBtnSaveTransactionDetailsFlag() && observable.getActionType()!=ClientConstants.ACTIONTYPE_DELETE) {
                        observable.showAlertWindow(resourceBundle.getString("saveInTxDetailsTable"));
                } else if ((!btnSaveIssue.isEnabled()) && (transactionUI.isBtnSaveTransactionDetailsFlag())){ 
//                          || (observable.getActionType() == (ClientConstants.ACTIONTYPE_DELETE))) {
                    double transTotalAmt = CommonUtil.convertObjToDouble(transactionUI.getTransactionOB().getLblTotalTransactionAmtVal()).doubleValue();
                    double remittanceTotalAmt = CommonUtil.convertObjToDouble(txtTotalamt.getText()).doubleValue();
                    if (ClientUtil.checkTotalAmountTallied(remittanceTotalAmt, transTotalAmt) == false)
                        ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NOT_TALLY));
                    else {
                        setIssueButtonEnableDisable(false);
                        setRevalidationDuplicationEnableDisable(false);//changed 10/10/2008
                        savePerformed();
//                        super.removeEditLock(txtVariableNo.getText());
                        setModified(false);
                    }
                }
            } else if ( transactionSize  == 0 && tblIssue.getRowCount() > 0) {
                if (btnSaveIssue.isEnabled()) {
                    StringBuffer strbMessage = new StringBuffer();
                    strbMessage.append(resourceBundle.getString("EnterTxDetails"));
                    strbMessage.append("\n");
                    strbMessage.append(resourceBundle.getString("saveInIssueTable"));
                    displayAlert(CommonUtil.convertObjToStr(strbMessage));
                    strbMessage = null;
                } else {
                    observable.showAlertWindow(resourceBundle.getString("EnterTxDetails"));
                }
            } else if (tblIssue.getRowCount() == 0 &&  transactionSize  > 0) {
                if (transactionUI.isBtnSaveTransactionDetailsFlag()) {
                    StringBuffer strbMessage = new StringBuffer();
                    strbMessage.append(resourceBundle.getString("EnterIssueDetails"));
                    strbMessage.append("\n");
                    strbMessage.append(resourceBundle.getString("saveInTxDetailsTable"));
                    displayAlert(CommonUtil.convertObjToStr(strbMessage));
                    strbMessage = null;
                } else {
                    observable.showAlertWindow(resourceBundle.getString("EnterIssueDetails"));
                }
            } else if (tblIssue.getRowCount()>0 || rowCount == -1) {
                if( btnSaveIssue.isEnabled() ) {
                    String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panIssue);
                    mandatoryMessage += resourceBundle.getString("saveInIssueTable");
                    if (mandatoryMessage.length() > 0){
                        displayAlert(mandatoryMessage);
                    }
                } else {
                    setIssueButtonEnableDisable(false);
                    setRevalidationDuplicationEnableDisable(false);//changed 10/10/2008
                    savePerformed();
                    setEnable();
                }
            }
        }
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // Add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed
    //
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // Add your handling code here:
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed
    /**
     * To disable the text fields in the issue details which is always disabled
     */
    private void  setIssuetxtDisable() {
        txtPayeeAccHead.setEditable(false);
        txtTotalamt.setEnabled(false);
        txtTotalInstruments.setEnabled(false);
        txtVariableNo.setEditable(false);
        txtTotalAmt.setEditable(false);
    }
    
    
    private void btnSaveIssueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveIssueActionPerformed
        // Add your handling code here:
        if((observable.getOperationMode().equals(CommonConstants.REMIT_DUPLICATE)) || (observable.getOperationMode().equals(CommonConstants.REMIT_REVALIDATE))){
            observable.setTotAmt(txtTotalamt.getText());
            
        }
        final int  YES = 0,NO_DUPLICATION = 2,UNIQUE =1;
        int RESULT = -1;
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panIssue);
        if(amtLimit == YES) {
            // If the cash limit is less than the amount give warning message
            mandatoryMessage += observable.getWarningMessageForAmount();
        }
        if ((amtLimit == NO_DUPLICATION) && !(txtPANGIRNo.getText().length() > 0)) {
            mandatoryMessage += CommonUtil.convertObjToStr(resourceBundle.getString("panGirNo"));
        }
        if (isBranchCodeNotValid) {
            // If the branch code for the selected instrument type  is not valid
            mandatoryMessage += observable.getWarningMessage();
        }
        
        if(isPanMandatory().length() > 0){
            if(!ClientUtil.validatePAN(txtPANGIRNo))
                mandatoryMessage += CommonUtil.convertObjToStr(resourceBundle.getString("InvalidPan"));
        }
        
        if (mandatoryMessage.length() > 0){
            displayAlert(mandatoryMessage);
        }else if (amtLimit == LESS_THAN_AMOUNT  || amtLimit == NO_DUPLICATION){
            observable.setStatusBy1(TrueTransactMain.USER_ID);
            observable.setBranchIdFromTrueTransactMain(TrueTransactMain.BRANCH_ID);
            observable.setStatusDate(DateUtil.getStringDate((Date) currDt.clone()));
            updateOBFields();
            transactionUI.setCallingProdID(CommonUtil.convertObjToStr((((ComboBoxModel)(cboProductId).getModel())).getKeyForSelected()));
            RESULT = observable.saveInIssueDetails(tableIssueMousePressed,getSelectRow());
            
            if((RESULT == NO_DUPLICATION) || (RESULT == YES) ){
                transactionUI.setCallingAmount(txtTotalamt.getText());
                setNewDeleteIssueButtons();
                btnSaveIssue.setEnabled(false);
                btnNewIsPressed = false;
                setRevalidationDuplicationEnableDisable(false);//changed 10/10/2008
                setPanIssueDetailsEnableDisable(false);
                setPayeeAccHDButtonEnableDisable(false);
                observable.resetIssueDetails();
            }
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
                btnNewIssue.setEnabled(false);
            }
        }
        if((observable.getOperationMode().equals(CommonConstants.REMIT_DUPLICATE)) || 
        (observable.getOperationMode().equals(CommonConstants.REMIT_REVALIDATE))){
//            txtTotalamt.setText(observable.getTotAmt());
            btnSave.setEnabled(true);
            txtDupServTax.setEnabled(false);
            txtDuplicationCharge.setEnabled(false);
            txtDupServTax.setEditable(false);
            txtDuplicationCharge.setEditable(false);
//            if(observable.getDupTotAmt().equals("")){
//                txtTotalamt.setText(observable.getDupTotAmt());
//            }
        }
        
    }//GEN-LAST:event_btnSaveIssueActionPerformed
    /* action to perform when  main save button is pressed */
    private void savePerformed(){
        updateOBFields();
        
        observable.doAction();
        ClientUtil.enableDisable(this, false);
        setButtonEnableDisable();
        setPayeeAccHDButtonEnableDisable(false);
        
        if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED){
                HashMap lockMap = new HashMap();
                ArrayList lst = new ArrayList();
                lst.add("VARIABLE_NO");
                lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
                if (observable.getProxyReturnMap()!=null) {
                    if (observable.getProxyReturnMap().containsKey("VARIABLE_NO")) {
                        lockMap.put("VARIABLE_NO", observable.getProxyReturnMap().get("VARIABLE_NO"));
                    }
                }
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                    lockMap.put("VARIABLE_NO", observable.getTxtVariableNo());
                }
            setEditLockMap(lockMap);
            setEditLock();
            observable.resetForm();
            transactionUI.resetObjects();
            btnCancelActionPerformed(null);
            revalidate = false;
            btnNew.setEnabled(true);
            btnEdit.setEnabled(true);
            btnSave.setEnabled(false);
            btnCancel.setEnabled(false);
            lblStatus.setText(ClientConstants.RESULT_STATUS[3]);
        }
        else{
            btnNew.setEnabled(false);
            btnEdit.setEnabled(false);
            btnSave.setEnabled(true);
            btnCancel.setEnabled(true);
            lblStatus.setText(ClientConstants.RESULT_STATUS[0]);
        }
        observable.setResultStatus();
        
        //}
    }
    
    private void setObservable() throws Exception {
        observable = new RemittanceIssueOB();
        observable.addObserver(this);
    }
     public RemittanceIssueOB getRemittanceIssueOB(){
        return observable ;
    }
    private void initComponentData() {
        cboProductId.setModel(observable.getCbmProductId());
        cboCity.setModel(observable.getCbmCity());
        cboDraweeBank.setModel(observable.getCbmDraweeBank());
        cboBranchCode.setModel(observable.getCbmBranchCode());
        cboCategory.setModel(observable.getCbmCategory());
        cboCrossing.setModel(observable.getCbmCrossing());
        cboTransmissionType.setModel(observable.getCbmTransmissionType());
    }
    
    /* Checking for mandatory fields */
    private String checkMandatory(javax.swing.JComponent component){
        return new MandatoryCheck().checkMandatory(getClass().getName(), component);
    }
    
    /* To display an alert message if any of the mandatory fields is not inputed */
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    /* To enable or disable the main New Save Delete buttons  */
    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnSave.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        btnDelete.setEnabled(!btnDelete.isEnabled());
        btnAuthorize.setEnabled(!btnSave.isEnabled());
        btnReject.setEnabled(!btnSave.isEnabled());
        btnException.setEnabled(!btnSave.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
    }
    
    /* To Enable or Disable the button payeeAcc head */
    private void setPayeeAccHDButtonEnableDisable(boolean flag) {
        btnPayeeAccHead.setEnabled(flag);
    }
    
    
    
/* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        PanTotalInstruments.setName("PanTotalInstruments");
        btnAuthorize.setName("btnAuthorize");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnDeleteIssue.setName("btnDeleteIssue");
        btnDuplicate.setName("btnDuplicate");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnNew.setName("btnNew");
        btnNewIssue.setName("btnNewIssue");
        btnPayeeAccHead.setName("btnPayeeAccHead");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnRevalidate.setName("btnRevalidate");
        btnSave.setName("btnSave");
        btnSaveIssue.setName("btnSaveIssue");
        cboBranchCode.setName("cboBranchCode");
        cboCategory.setName("cboCategory");
        cboCity.setName("cboCity");
        cboCrossing.setName("cboCrossing");
        cboDraweeBank.setName("cboDraweeBank");
        cboProductId.setName("cboProductId");
        cboTransmissionType.setName("cboTransmissionType");
        lbSpace2.setName("lbSpace2");
        lblAccHead.setName("lblAccHead");
        lblAccHeadBal.setName("lblAccHeadBal");
        lblAmt.setName("lblAmt");
        lblBatchId.setName("lblBatchId");
        lblBranchCode.setName("lblBranchCode");
        lblCategory.setName("lblCategory");
        lblCity.setName("lblCity");
        lblCrossing.setName("lblCrossing");
        lblDisplayBatchId.setName("lblDisplayBatchId");
        lblDraweeBank.setName("lblDraweeBank");
        lblExchange.setName("lblExchange");
        lblExchange1.setName("lblExchange1");
        lblFavouring.setName("lblFavouring");
        lblInstrumentHIphen.setName("lblInstrumentHIphen");
        lblInstrumentNo.setName("lblInstrumentNo");
        lblMsg.setName("lblMsg");
        lblOtherCharges.setName("lblOtherCharges");
        lblPANGIRNo.setName("lblPANGIRNo");
        lblPayeeAccHead.setName("lblPayeeAccHead");
        lblPayeeProdType.setName("lblPayeeProdType");
        lblPayeeProdId.setName("lblPayeeProdId");
        lblPayeeAccNo.setName("lblPayeeAccNo");
        lblPostage.setName("lblPostage");
        lblProductId.setName("lblProductId");
        lblRemarks.setName("lblRemarks");
        lblSpace1.setName("lblSpace1");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblSpace5.setName("lblSpace5");
        lblStatus.setName("lblStatus");
        lblTotalAmt.setName("lblTotalAmt");
        lblTotalInstruments.setName("lblTotalInstruments");
        lblTotalamount.setName("lblTotalamount");
        lblTransmissionType.setName("lblTransmissionType");
        lblVariableNo.setName("lblVariableNo");
        mbrMain.setName("mbrMain");
        panBatchId.setName("panBatchId");
//        panButton.setName("panButton");
        panDetails.setName("panDetails");
        panInstrumentNo.setName("panInstrumentNo");
        panIssue.setName("panIssue");
        panIssueDetails.setName("panIssueDetails");
        panLeftSubIssue.setName("panLeftSubIssue");
        panPayeeAccHead.setName("panPayeeAccHead");
        panRightSubIssue.setName("panRightSubIssue");
        panStatus.setName("panStatus");
        panTableIssue.setName("panTableIssue");
        panbtnIssue.setName("panbtnIssue");
        sptIssue.setName("sptIssue");
        srpIssue.setName("srpIssue");
        tblIssue.setName("tblIssue");
        txtAmt.setName("txtAmt");
        txtExchange.setName("txtExchange");
        txtExchange1.setName("txtExchange1");
        txtFavouring.setName("txtFavouring");
        txtInstrumentNo1.setName("txtInstrumentNo1");
        txtInstrumentNo2.setName("txtInstrumentNo2");
        txtOtherCharges.setName("txtOtherCharges");
        txtPANGIRNo.setName("txtPANGIRNo");
        txtPayeeAccHead.setName("txtPayeeAccHead");
        txtPayeeAccNo.setName("txtPayeeAccNo");
        cboPayeeProdType.setName("cboPayeeProdType");
        cboPayeeProdId.setName("cboPayeeProdId");
        txtPostage.setName("txtPostage");
        txtRemarks.setName("txtRemarks");
        txtTotalAmt.setName("txtTotalAmt");
        txtTotalInstruments.setName("txtTotalInstruments");
        txtTotalamt.setName("txtTotalamt");
        txtVariableNo.setName("txtVariableNo");
        
        txtRevalidationCharge.setName("txtRevalidationCharge");
        txtDuplicationCharge.setName("txtDuplicationCharge");
        lblRevalidationCharge.setName("lblRevalidationCharge");
        lblDuplicationCharge.setName("lblDuplicationCharge");
        txtDupServTax.setName("txtDupServTax");
        txtRevServTax.setName("txtRevServTax");
        lblDupServTax.setName("lblDupServTax");
        lblRevServTax.setName("lblRevServTax");
        
    }
    
    
    
/* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        btnClose.setText(resourceBundle.getString("btnClose"));
        btnSaveIssue.setText(resourceBundle.getString("btnSaveIssue"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        lblOtherCharges.setText(resourceBundle.getString("lblOtherCharges"));
        ((javax.swing.border.TitledBorder)panIssueDetails.getBorder()).setTitle(resourceBundle.getString("panIssueDetails"));
        btnDeleteIssue.setText(resourceBundle.getString("btnDeleteIssue"));
        lblPayeeAccNo.setText(resourceBundle.getString("lblPayeeAccNo"));
        lblPayeeProdType.setText(resourceBundle.getString("lblPayeeProdType"));
        lblPayeeProdId.setText(resourceBundle.getString("lblPayeeProdId"));
        lblInstrumentHIphen.setText(resourceBundle.getString("lblInstrumentHIphen"));
        lblPayeeAccHead.setText(resourceBundle.getString("lblPayeeAccHead"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        btnRevalidate.setText(resourceBundle.getString("btnRevalidate"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        lblExchange.setText(resourceBundle.getString("lblExchange"));
        lblExchange1.setText(resourceBundle.getString("lblExchange1"));
        btnPayeeAccHead.setText(resourceBundle.getString("btnPayeeAccHead"));
        lblTotalamount.setText(resourceBundle.getString("lblTotalamount"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblCategory.setText(resourceBundle.getString("lblCategory"));
        lblVariableNo.setText(resourceBundle.getString("lblVariableNo"));
        lblCrossing.setText(resourceBundle.getString("lblCrossing"));
        lblTransmissionType.setText(resourceBundle.getString("lblTransmissionType"));
        lblAmt.setText(resourceBundle.getString("lblAmt"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblAccHead.setText(resourceBundle.getString("lblAccHead"));
        lblAccHeadBal.setText(resourceBundle.getString("lblAccHeadBal"));
        lblTotalAmt.setText(resourceBundle.getString("lblTotalAmt"));
        lblCity.setText(resourceBundle.getString("lblCity"));
        lblFavouring.setText(resourceBundle.getString("lblFavouring"));
        lblBranchCode.setText(resourceBundle.getString("lblBranchCode"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblProductId.setText(resourceBundle.getString("lblProductId"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lbSpace2.setText(resourceBundle.getString("lbSpace2"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblPostage.setText(resourceBundle.getString("lblPostage"));
        btnDuplicate.setText(resourceBundle.getString("btnDuplicate"));
        lblInstrumentNo.setText(resourceBundle.getString("lblInstrumentNo"));
        lblRemarks.setText(resourceBundle.getString("lblRemarks"));
        lblSpace5.setText(resourceBundle.getString("lblSpace5"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblPANGIRNo.setText(resourceBundle.getString("lblPANGIRNo"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        lblDraweeBank.setText(resourceBundle.getString("lblDraweeBank"));
        btnNewIssue.setText(resourceBundle.getString("btnNewIssue"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblTotalInstruments.setText(resourceBundle.getString("lblTotalInstruments"));
        lblRevalidationCharge.setText(resourceBundle.getString("lblRevalidationCharge"));
        lblDuplicationCharge.setText(resourceBundle.getString("lblDuplicationCharge"));
        lblDupServTax.setText(resourceBundle.getString("lblDupServTax"));
        lblRevServTax.setText(resourceBundle.getString("lblRevServTax"));
    }
/* Auto Generated Method - setMandatoryHashMap()
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtApplicantsName", new Boolean(true));
        mandatoryMap.put("cboTransType", new Boolean(true));
        mandatoryMap.put("txtTransactionAmt", new Boolean(true));
        mandatoryMap.put("txtDebitAccHead", new Boolean(true));
        mandatoryMap.put("txtDebitAccNo", new Boolean(true));
        mandatoryMap.put("txtChequeNo", new Boolean(true));
        mandatoryMap.put("txtTotalTransfer", new Boolean(true));
        mandatoryMap.put("txtTotalCash", new Boolean(true));
        mandatoryMap.put("tdtChequeDate", new Boolean(true));
        mandatoryMap.put("txtTotalTransactionAmt", new Boolean(true));
        mandatoryMap.put("txtTotalamt", new Boolean(true));
        mandatoryMap.put("txtTotalInstruments", new Boolean(true));
        mandatoryMap.put("cboProductId", new Boolean(true));
        mandatoryMap.put("txtFavouring", new Boolean(true));
        mandatoryMap.put("cboTransmissionType", new Boolean(true));
        mandatoryMap.put("txtPANGIRNo", new Boolean(true));
        mandatoryMap.put("cboCategory", new Boolean(true));
        mandatoryMap.put("cboCity", new Boolean(true));
        mandatoryMap.put("cboDraweeBank", new Boolean(true));
        mandatoryMap.put("cboBranchCode", new Boolean(true));
        mandatoryMap.put("txtAmt", new Boolean(true));
        mandatoryMap.put("txtExchange", new Boolean(true));
        mandatoryMap.put("txtExchange1", new Boolean(true));
        mandatoryMap.put("txtTotalAmt", new Boolean(true));
        mandatoryMap.put("txtPayeeAccHead", new Boolean(true));
        mandatoryMap.put("cboPayeeProdType", new Boolean(true));
        mandatoryMap.put("cboPayeeProdId", new Boolean(true));
        mandatoryMap.put("txtOtherCharges", new Boolean(true));
        mandatoryMap.put("txtPayeeAccNo", new Boolean(true));
        mandatoryMap.put("txtPostage", new Boolean(true));
        mandatoryMap.put("txtInstrumentNo1", new Boolean(true));
        mandatoryMap.put("txtInstrumentNo2", new Boolean(true));
        mandatoryMap.put("txtVariableNo", new Boolean(true));
        mandatoryMap.put("txtRemarks", new Boolean(true));
        mandatoryMap.put("cboCrossing", new Boolean(true));
        mandatoryMap.put("txtRevalidationCharge", new Boolean(false));
        mandatoryMap.put("txtDuplicationCharge", new Boolean(false));
        mandatoryMap.put("txtDupServTax", new Boolean(false));
        mandatoryMap.put("txtRevServTax", new Boolean(false));
    }
    
/* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
   /* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        
        txtVariableNo.setText(observable.getTxtVariableNo());
//        txtTotalamt.setText(observable.calculateTotalAmount());
        
        if(observable.getTotAmt().equals(""))
            txtTotalamt.setText(observable.calculateTotalAmount());
        else
            txtTotalamt.setText(observable.getTotAmt());
        txtTotalInstruments.setText(observable.getTxtTotalInstruments());
        cboProductId.setSelectedItem(observable.getCboProductId());
        txtFavouring.setText(observable.getTxtFavouring());
        cboTransmissionType.setSelectedItem(observable.getCboTransmissionType());
        txtPANGIRNo.setText(observable.getTxtPANGIRNo());
        cboCategory.setSelectedItem(observable.getCboCategory());
        txtRemarks.setText(observable.getTxtRemarks());
        cboCity.setModel(observable.getCbmCity());
        cboCity.setSelectedItem(observable.getCboCity());
        cboCrossing.setSelectedItem(observable.getCboCrossing());
        cboDraweeBank.setModel(observable.getCbmDraweeBank());
        cboDraweeBank.setSelectedItem(observable.getCboDraweeBank());
        cboBranchCode.setModel(observable.getCbmBranchCode());
        cboBranchCode.setSelectedItem(observable.getCboBranchCode());
        txtTotalAmt.setText(observable.getTxtTotalAmt());
        if((observable.getOperationMode().equals(CommonConstants.REMIT_DUPLICATE)) || 
        (observable.getOperationMode().equals(CommonConstants.REMIT_REVALIDATE))){
            txtTotalAmt.setText(observable.calculateTotalAmount());
            if(observable.getTotAmt().equals(""))
                txtTotalamt.setText(observable.calculateTotalAmount());
            else
                txtTotalamt.setText(observable.getTotAmt());
        }
        if(!observable.getDupTotAmt().equals(""))
            txtTotalamt.setText(observable.getDupTotAmt());
        if(!observable.getRevTotAmt().equals(""))
            txtTotalamt.setText(observable.getRevTotAmt());
        txtPayeeAccHead.setText(observable.getTxtPayeeAccHead());
        cboPayeeProdType.setModel(observable.getCbmPayeeProductType());
        cboPayeeProdType.setSelectedItem(observable.getCboPayeeProductType());
        cboPayeeProdId.setModel(observable.getCbmPayeeProductId());
        cboPayeeProdId.setSelectedItem(observable.getCboPayeeProductId());
        txtOtherCharges.setText(observable.getTxtOtherCharges());
        txtPayeeAccNo.setText(observable.getTxtPayeeAccNo());
        txtPostage.setText(observable.getTxtPostage());
        txtExchange.setText(observable.getTxtExchange());
        txtExchange1.setText(observable.getTxtExchange1());
        txtAmt.setText(observable.getTxtAmt());
        txtInstrumentNo1.setText(observable.getTxtInstrumentNo1());
        txtInstrumentNo2.setText(observable.getTxtInstrumentNo2());
        lblStatus.setText(observable.getLblStatus());
        lblDisplayBatchId.setText(observable.getBatchId());
        tblIssue.setModel(observable.getTblIssueDetails());
        txtRevalidationCharge.setText(observable.getTxtRevalidationCharge());
        txtDuplicationCharge.setText(observable.getTxtDuplicationCharge());
        txtDupServTax.setText(observable.getTxtDupServTax());
        txtRevServTax.setText(observable.getTxtRevServTax());
        
        
    }
    
  /* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setScreen(getScreen());
        observable.setModule(getModule());
//        observable.setTxtTotalIssueAmt(CommonUtil.convertObjToStr(txtTotalamt.getText()));
        observable.setTxtTotalIssueAmt(CommonUtil.convertObjToStr(txtTotalAmt.getText()));
        observable.setTxtTotalInstruments(CommonUtil.convertObjToStr(txtTotalInstruments.getText()));
        observable.setCboProductId(CommonUtil.convertObjToStr(cboProductId.getSelectedItem()));
        observable.setCboPayeeProductType(CommonUtil.convertObjToStr(cboPayeeProdType.getSelectedItem()));
        observable.setCboPayeeProductId(CommonUtil.convertObjToStr(cboPayeeProdId.getSelectedItem()));
        observable.setTxtFavouring(CommonUtil.convertObjToStr(txtFavouring.getText()));
        observable.setCboTransmissionType(CommonUtil.convertObjToStr(cboTransmissionType.getSelectedItem()));
        observable.setTxtPANGIRNo(CommonUtil.convertObjToStr(txtPANGIRNo.getText()));
        observable.setCboCategory(CommonUtil.convertObjToStr(cboCategory.getSelectedItem()));
        observable.setCboCity(CommonUtil.convertObjToStr(cboCity.getSelectedItem()));
        observable.setCboDraweeBank(CommonUtil.convertObjToStr(cboDraweeBank.getSelectedItem()));
        observable.setCboBranchCode(CommonUtil.convertObjToStr(cboBranchCode.getSelectedItem()));
        observable.setTxtAmt(CommonUtil.convertObjToStr(txtAmt.getText()));
        observable.setTxtExchange(CommonUtil.convertObjToStr(txtExchange.getText()));
        observable.setTxtExchange1(CommonUtil.convertObjToStr(txtExchange1.getText()));
        observable.setTxtTotalAmt(CommonUtil.convertObjToStr(txtTotalAmt.getText()));
        observable.setTxtPayeeAccHead(CommonUtil.convertObjToStr(txtPayeeAccHead.getText()));
        observable.setTxtOtherCharges(CommonUtil.convertObjToStr(txtOtherCharges.getText()));
        observable.setTxtPayeeAccNo(CommonUtil.convertObjToStr(txtPayeeAccNo.getText()));
        observable.setTxtPostage(CommonUtil.convertObjToStr(txtPostage.getText()));
        observable.setTxtInstrumentNo1(CommonUtil.convertObjToStr(txtInstrumentNo1.getText()));
        observable.setTxtInstrumentNo2(CommonUtil.convertObjToStr(txtInstrumentNo2.getText()));
        observable.setTxtVariableNo(CommonUtil.convertObjToStr(txtVariableNo.getText()));
        observable.setTxtRemarks(CommonUtil.convertObjToStr(txtRemarks.getText()));
        observable.setCboCrossing(CommonUtil.convertObjToStr(cboCrossing.getSelectedItem()));
        observable.setBatchId(CommonUtil.convertObjToStr(lblDisplayBatchId.getText()));
        observable.setTxtRevalidationCharge(CommonUtil.convertObjToStr(txtRevalidationCharge.getText()));
        observable.setTxtDuplicationCharge(CommonUtil.convertObjToStr(txtDuplicationCharge.getText()));
        observable.setTxtDupServTax(CommonUtil.convertObjToStr(txtDupServTax.getText()));
        observable.setTxtRevServTax(CommonUtil.convertObjToStr(txtRevServTax.getText()));
        System.out.println("setCboPayeeProductId : " + observable.getCboPayeeProductId());
        System.out.println("getCboPayeeProductType : " + observable.getCboPayeeProductType());
    }
    
    /**
     * To enable disable table Issue details
     */
    private void setTableIssueEnableDisable(boolean flag){
        tblIssue.setEnabled(flag);
    }
    
    private void setTotalAmtDisable(){
        txtTotalAmt.setEnabled(false);
    }
    
    private String isPanMandatory(){
        MandatoryCheck objMandatory = new MandatoryCheck();
        RemittanceIssueHashMap objMap = new RemittanceIssueHashMap();
        HashMap mandatoryMap = objMap.getMandatoryHashMap();
        mandatoryMap.put("txtPANGIRNo", new Boolean(false));
        HashMap tempMap = new HashMap();
//        tempMap.put(CommonConstants.PRODUCT_ID, CommonUtil.convertObjToStr(cboProductId.getSelectedItem()));
        tempMap.put(CommonConstants.PRODUCT_ID, (String)((ComboBoxModel)cboProductId.getModel()).getKeyForSelected());
        List outList = ClientUtil.executeQuery("getPanValidationAmt", tempMap);
        tempMap = null ;
        if(outList != null && outList.size() > 0){
            if(((Double)outList.get(0)).doubleValue() < CommonUtil.convertObjToDouble(txtAmt.getText()).doubleValue()){
                mandatoryMap.put("txtPANGIRNo", new Boolean(true));
            }
        }
        outList = null ;
        String mandatoryMessage = objMandatory.checkMandatory(getClass().getName(),panLeftSubIssue, mandatoryMap);
        return mandatoryMessage;
    }
    
    /* To set Maximum length and for validation */
    private void setMaximumLength() {
        /**
         * Issue Details
         */
        txtFavouring.setMaxLength(128);
        txtFavouring.setValidation(new DefaultValidation());
        txtAmt.setMaxLength(16);
        txtAmt.setValidation(new CurrencyValidation());
        txtPANGIRNo.setMaxLength(32);
        txtExchange.setMaxLength(16);
        txtExchange.setValidation(new CurrencyValidation(14,2));
        txtExchange1.setMaxLength(16);
        txtExchange1.setValidation(new CurrencyValidation(14,2));
        txtPostage.setMaxLength(16);
        txtPostage.setValidation(new CurrencyValidation(14,2));
        txtOtherCharges.setMaxLength(16);
        txtOtherCharges.setValidation(new CurrencyValidation(14,2));
        txtTotalAmt.setMaxLength(16);
        txtTotalAmt.setValidation(new CurrencyValidation(14,2));
        txtDuplicationCharge.setValidation(new CurrencyValidation(14,2));
        txtRevalidationCharge.setValidation(new CurrencyValidation(14,2));
        txtDupServTax.setValidation(new CurrencyValidation(14,2));
        txtRevServTax.setValidation(new CurrencyValidation(14,2));
        
        txtRemarks.setMaxLength(256);
        txtRemarks.setValidation(new DefaultValidation());
        txtPayeeAccHead.setMaxLength(32);
        txtPayeeAccNo.setMaxLength(32);
        txtInstrumentNo1.setMaxLength(32);
        txtInstrumentNo2.setMaxLength(32);
        txtInstrumentNo2.setValidation(new NumericValidation());
        txtVariableNo.setMaxLength(16);
    }
    
/* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        RemittanceIssueMRB objMandatoryRB = new RemittanceIssueMRB();
        txtTotalamt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTotalamt"));
        txtTotalInstruments.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTotalInstruments"));
        cboProductId.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProductId"));
        cboPayeeProdId.setHelpMessage(lblMsg, objMandatoryRB.getString("cboPayeeProductId"));
        cboPayeeProdType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboPayeeProdType"));
        txtFavouring.setHelpMessage(lblMsg, objMandatoryRB.getString("txtFavouring"));
        cboTransmissionType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboTransmissionType"));
        txtPANGIRNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPANGIRNo"));
        cboCategory.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCategory"));
        cboCity.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCity"));
        cboDraweeBank.setHelpMessage(lblMsg, objMandatoryRB.getString("cboDraweeBank"));
        cboBranchCode.setHelpMessage(lblMsg, objMandatoryRB.getString("cboBranchCode"));
        txtAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAmt"));
        txtExchange.setHelpMessage(lblMsg, objMandatoryRB.getString("txtExchange"));
        txtExchange1.setHelpMessage(lblMsg, objMandatoryRB.getString("txtExchange1"));
        txtTotalAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTotalAmt"));
        txtPayeeAccHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPayeeAccHead"));
        txtOtherCharges.setHelpMessage(lblMsg, objMandatoryRB.getString("txtOtherCharges"));
        txtPayeeAccNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPayeeAccNo"));
        txtPostage.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPostage"));
        txtInstrumentNo1.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInstrumentNo1"));
        txtInstrumentNo2.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInstrumentNo2"));
        txtVariableNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtVariableNo"));
        txtRemarks.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRemarks"));
        cboCrossing.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCrossing"));
        txtRevalidationCharge.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRevalidationCharge"));
        txtDuplicationCharge.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDuplicationCharge"));
        txtDupServTax.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDupServTax"));
        txtRevServTax.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRevServTax"));
    }
    /**
     * If Transaction Type is transfer give alert
     */
    private void transferValidation() {
        RemittanceIssueMRB objMRB = new RemittanceIssueMRB();
        StringBuffer strB = new StringBuffer();
        strB = null;
        objMRB = null;
    }
    
    private void setValuesFor(String productId, String category, String bankCode, String branchCode, String amount) {
        if( productId != null ){
            this.productId = CommonUtil.convertObjToStr((((ComboBoxModel)(cboProductId).getModel())).getKeyForSelected());
        }
        if( category != null ){
            this.category = CommonUtil.convertObjToStr((((ComboBoxModel)(cboCategory).getModel())).getKeyForSelected());
        }
        if( bankCode != null ){
            this.bankCode = CommonUtil.convertObjToStr((((ComboBoxModel)(cboDraweeBank).getModel())).getKeyForSelected());
        }
        if( branchCode != null ){
            this.branchCode = CommonUtil.convertObjToStr((((ComboBoxModel)(cboBranchCode).getModel())).getKeyForSelected());
        }
        if( amount != null ){
            this.amount = CommonUtil.convertObjToStr(txtAmt.getText());
        }
    }
    
    public static void main(String args[]){
        javax.swing.JFrame f = new javax.swing.JFrame();
        RemittanceIssueUI ch = new RemittanceIssueUI();
        f.getContentPane().add(ch);
        ch.show();
        ch.setSize(700, 500);
        f.show();
        f.setSize(700, 500);
        //        System.out.println("char  " + (int)'z');
    }
    
    /**
     * Getter for property loanActNum.
     * @return Value of property loanActNum.
     */
    public java.lang.String getLoanActNum() {
        return transactionUI.getLoanActnum();
    }    
    
    /**
     * Setter for property loanActNum.
     * @param loanActNum New value of property loanActNum.
     */
    public void setLoanActNum(java.lang.String loanActNum) {
        this.loanActNum = loanActNum;
    }
    
 
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CPanel PanTotalInstruments;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDeleteIssue;
    private com.see.truetransact.uicomponent.CButton btnDuplicate;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnNewIssue;
    private com.see.truetransact.uicomponent.CButton btnPayeeAccHead;
    private com.see.truetransact.uicomponent.CButton btnPayeeAccNo;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnRevalidate;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnSaveIssue;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboBranchCode;
    private com.see.truetransact.uicomponent.CComboBox cboCategory;
    private com.see.truetransact.uicomponent.CComboBox cboCity;
    private com.see.truetransact.uicomponent.CComboBox cboCrossing;
    private com.see.truetransact.uicomponent.CComboBox cboDraweeBank;
    private com.see.truetransact.uicomponent.CComboBox cboPayeeProdId;
    private com.see.truetransact.uicomponent.CComboBox cboPayeeProdType;
    private com.see.truetransact.uicomponent.CComboBox cboProductId;
    private com.see.truetransact.uicomponent.CComboBox cboTransmissionType;
    private com.see.truetransact.uicomponent.CLabel lbSpace2;
    private com.see.truetransact.uicomponent.CLabel lblAccHead;
    private com.see.truetransact.uicomponent.CLabel lblAccHeadBal;
    private javax.swing.JLabel lblAccHeadBalDisplay;
    private javax.swing.JLabel lblAccHeadProdIdDisplay;
    private com.see.truetransact.uicomponent.CLabel lblAmt;
    private com.see.truetransact.uicomponent.CLabel lblBatchId;
    private com.see.truetransact.uicomponent.CLabel lblBranchCode;
    private com.see.truetransact.uicomponent.CLabel lblCategory;
    private com.see.truetransact.uicomponent.CLabel lblCity;
    private com.see.truetransact.uicomponent.CLabel lblCrossing;
    private com.see.truetransact.uicomponent.CLabel lblDisplayBatchId;
    private com.see.truetransact.uicomponent.CLabel lblDraweeBank;
    private com.see.truetransact.uicomponent.CLabel lblDupServTax;
    private com.see.truetransact.uicomponent.CLabel lblDuplicationCharge;
    private com.see.truetransact.uicomponent.CLabel lblExchange;
    private com.see.truetransact.uicomponent.CLabel lblExchange1;
    private com.see.truetransact.uicomponent.CLabel lblFavouring;
    private com.see.truetransact.uicomponent.CLabel lblInstrumentHIphen;
    private com.see.truetransact.uicomponent.CLabel lblInstrumentNo;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblOtherCharges;
    private com.see.truetransact.uicomponent.CLabel lblPANGIRNo;
    private com.see.truetransact.uicomponent.CLabel lblPayeeAccHead;
    private com.see.truetransact.uicomponent.CLabel lblPayeeAccNo;
    private com.see.truetransact.uicomponent.CLabel lblPayeeProdId;
    private com.see.truetransact.uicomponent.CLabel lblPayeeProdType;
    private com.see.truetransact.uicomponent.CLabel lblPostage;
    private com.see.truetransact.uicomponent.CLabel lblProductId;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
    private com.see.truetransact.uicomponent.CLabel lblRevServTax;
    private com.see.truetransact.uicomponent.CLabel lblRevalidationCharge;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace17;
    private com.see.truetransact.uicomponent.CLabel lblSpace18;
    private com.see.truetransact.uicomponent.CLabel lblSpace19;
    private com.see.truetransact.uicomponent.CLabel lblSpace20;
    private com.see.truetransact.uicomponent.CLabel lblSpace21;
    private com.see.truetransact.uicomponent.CLabel lblSpace22;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace6;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTotalAmt;
    private com.see.truetransact.uicomponent.CLabel lblTotalInstruments;
    private com.see.truetransact.uicomponent.CLabel lblTotalamount;
    private com.see.truetransact.uicomponent.CLabel lblTransmissionType;
    private com.see.truetransact.uicomponent.CLabel lblVariableNo;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panBatchId;
    private com.see.truetransact.uicomponent.CPanel panChrg;
    private com.see.truetransact.uicomponent.CPanel panDebitAccNo;
    private com.see.truetransact.uicomponent.CPanel panDetails;
    private com.see.truetransact.uicomponent.CPanel panInstrumentNo;
    private com.see.truetransact.uicomponent.CPanel panIssue;
    private com.see.truetransact.uicomponent.CPanel panIssueDetails;
    private com.see.truetransact.uicomponent.CPanel panLeftSubIssue;
    private com.see.truetransact.uicomponent.CPanel panPayeeAccHead;
    private com.see.truetransact.uicomponent.CPanel panRITotal;
    private com.see.truetransact.uicomponent.CPanel panRightSubIssue;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTableIssue;
    private com.see.truetransact.uicomponent.CPanel panTransaction;
    private com.see.truetransact.uicomponent.CPanel panbtnIssue;
    private javax.swing.JSeparator sptCancel;
    private com.see.truetransact.uicomponent.CSeparator sptIssue;
    private javax.swing.JSeparator sptView;
    private com.see.truetransact.uicomponent.CScrollPane srpIssue;
    private com.see.truetransact.uicomponent.CTable tblIssue;
    private com.see.truetransact.uicomponent.CToolBar tbrRemittanceIssue;
    private com.see.truetransact.uicomponent.CTextField txtAmt;
    private com.see.truetransact.uicomponent.CTextField txtDupServTax;
    private com.see.truetransact.uicomponent.CTextField txtDuplicationCharge;
    private com.see.truetransact.uicomponent.CTextField txtExchange;
    private com.see.truetransact.uicomponent.CTextField txtExchange1;
    private com.see.truetransact.uicomponent.CTextField txtFavouring;
    private com.see.truetransact.uicomponent.CTextField txtInstrumentNo1;
    private com.see.truetransact.uicomponent.CTextField txtInstrumentNo2;
    private com.see.truetransact.uicomponent.CTextField txtOtherCharges;
    private com.see.truetransact.uicomponent.CTextField txtPANGIRNo;
    private com.see.truetransact.uicomponent.CTextField txtPayeeAccHead;
    private com.see.truetransact.uicomponent.CTextField txtPayeeAccNo;
    private com.see.truetransact.uicomponent.CTextField txtPostage;
    private com.see.truetransact.uicomponent.CTextField txtRemarks;
    private com.see.truetransact.uicomponent.CTextField txtRevServTax;
    private com.see.truetransact.uicomponent.CTextField txtRevalidationCharge;
    private com.see.truetransact.uicomponent.CTextField txtTotalAmt;
    private com.see.truetransact.uicomponent.CTextField txtTotalInstruments;
    private com.see.truetransact.uicomponent.CTextField txtTotalamt;
    private com.see.truetransact.uicomponent.CTextField txtVariableNo;
    // End of variables declaration//GEN-END:variables
}

