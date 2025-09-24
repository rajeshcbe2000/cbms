/*Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RTGSUI.java
 *
 * Created on FEB 02, 2015, 12:27 PM
 */
package com.see.truetransact.ui.remittance.rtgs;

import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.interestcalc.Rounding;
import org.apache.log4j.Logger;
import com.see.truetransact.ui.common.transaction.TransactionUI;
import com.see.truetransact.ui.common.viewall.TableDialogUI;
import com.see.truetransact.uicomponent.COptionPane;
import java.util.*;
/**
 *
 * @author Suresh R
 */
public class RtgsRemittanceUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer, UIMandatoryField {
    // Variables declaration - do not modify

    private final static Logger _log = Logger.getLogger(RtgsRemittanceUI.class);
    private RtgsRemittanceOB observable;
    private HashMap mandatoryMap;
    private boolean tableIssueMousePressed = false;
    private boolean tabletransactionMousePressed = false;
    private boolean btnSaveTransactionDetailsFlag = false;//flag to maintain whether btnSave Trans is pressed or not
    private boolean btnDeleteTransactionDetailsFlag = false;//flag to maintain whether btnDelete Trans is pressed or not
    private boolean btnNewIsPressed = false;
    private boolean duplicate = false;
    private boolean revalidate = false;
    final int EDIT = 0, DELETE = 1, PAYEE = 2, DEBIT = 3, LESS_THAN_AMOUNT = 1, TRANS_PROD = 4, ACC_NUM = 5, PAYEE_ACC_NUM = 6, VIEW = 10, DUPLICATE = 11, REVALIDATE = 12, DRAWEE_BANK = 13, BRANCH_CODE = 14;
    String viewType = "";
    final String AUTHORIZE = "Authorize";
    private int selectedRowValue;
    private int amtLimit = 1;
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
    private Date currDate = null;
    private TransactionUI transactionUI = new TransactionUI();
    final RtgsRemittanceRB resourceBundle = new RtgsRemittanceRB();
    private String orgRespBank = "";
    ArrayList olist = new ArrayList();
    private boolean updation = false;
    private String orgOrRespAdviceNo = "";
    private String slNo = "";
    private int allowedRows = 200;//Added By Kannan Ref. Mr.Srinath
    
    private boolean updateMode = false;
    int updateTab = -1;
    // End of variables declaration

    public RtgsRemittanceUI() {
        initComponents();
        initStartup();
        currDate = ClientUtil.getCurrentDate();
        transactionUI.addToScreen(panTransaction);
        observable.setTransactionOB(transactionUI.getTransactionOB());
    }
    /* methods invoked at the time of new form */

    private void initStartup() {
        try {
            setFieldNames();
            internationalize();
            setObservable();
            initComponentData();
            setMaximumLength();
            setButtonEnableDisable();
            new MandatoryCheck().putMandatoryMarks(getClass().getName(), panDetails);
            ClientUtil.enableDisable(this, false);
            transactionUI.setSourceScreen("RTGS_REMITTANCE");
            setMandatoryHashMap();
            setHelpMessage();
            observable.resetStatus();
            observable.resetForm();
            btnEnableDisable(false);
            btnDisable(false);
            setTableIssueEnableDisable(true);
            setIFSC_Code();
            btnAuthorize.setEnabled(true);
            btnReject.setEnabled(true);
            btnException.setEnabled(true);
            lblRTGS_IDVal.setText("");
            lblCharges.setVisible(true);
            txtPostage.setVisible(true);
            lblAdviceNo.setVisible(false);
            txtAdviceNo.setVisible(false);
            lblRTGS_ID.setVisible(false);
            lblRTGS_IDVal.setVisible(false);
            btnBeneficiaryBranch.setVisible(false);
            txtExchangeCollect.setEnabled(false);
            setSizeTableData();
            txtTotalAmount.setEnabled(false);
            transactionUI.setProdType();
        } catch (Exception err) {
            err.printStackTrace();
        }
    }
    
    private void setSizeTableData() {
        tblIssue.getColumnModel().getColumn(0).setPreferredWidth(5);
        tblIssue.getColumnModel().getColumn(1).setPreferredWidth(50);
        tblIssue.getColumnModel().getColumn(2).setPreferredWidth(80);
        tblIssue.getColumnModel().getColumn(3).setPreferredWidth(35);
    }

    private void setIFSC_Code() {
        HashMap ifscMap = new HashMap();
        ifscMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
        List ifscList = ClientUtil.executeQuery("getBranchIFSCodeDetails", ifscMap);
        if (ifscList != null && ifscList.size() > 0) {
            ifscMap = (HashMap) ifscList.get(0);
            lblIFSC_CodeVal.setText(CommonUtil.convertObjToStr(ifscMap.get("IFSC_CODE")));
            if (ifscMap.get("IFSC_CODE")==null|| String.valueOf(ifscMap.get("IFSC_CODE")).length() <= 0) {
                ClientUtil.showMessageWindow("Sender IFSC Code Should not be Empty...!!!");
                lblIFSC_CodeVal.setText("");
            }
        }else{
            ClientUtil.showMessageWindow("Sender IFSC Code Should not be Empty...!!!");
            lblIFSC_CodeVal.setText("");
        }
    }

    private void btnEnableDisable(boolean flag) {
        btnBeneficiaryBank.setEnabled(flag);
        btnBeneficiaryBranch.setEnabled(flag);
    }

    private void btnDisable(boolean flag) {
        btnNewIssue.setEnabled(flag);
        btnSaveIssue.setEnabled(flag);
        btnDeleteIssue.setEnabled(flag);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
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
        panTableIssue = new com.see.truetransact.uicomponent.CPanel();
        srpIssue = new com.see.truetransact.uicomponent.CScrollPane();
        tblIssue = new com.see.truetransact.uicomponent.CTable();
        panbtnIssue = new com.see.truetransact.uicomponent.CPanel();
        btnNewIssue = new com.see.truetransact.uicomponent.CButton();
        btnSaveIssue = new com.see.truetransact.uicomponent.CButton();
        btnDeleteIssue = new com.see.truetransact.uicomponent.CButton();
        panTotalAmount = new com.see.truetransact.uicomponent.CPanel();
        lblTotalAmount = new com.see.truetransact.uicomponent.CLabel();
        txtTotalAmount = new com.see.truetransact.uicomponent.CTextField();
        panIssue = new com.see.truetransact.uicomponent.CPanel();
        panLeftSubIssue = new com.see.truetransact.uicomponent.CPanel();
        cboProductId = new com.see.truetransact.uicomponent.CComboBox();
        lblProductId = new com.see.truetransact.uicomponent.CLabel();
        lblBeneficiaryIFSC_Code = new com.see.truetransact.uicomponent.CLabel();
        lblAccountNumber = new com.see.truetransact.uicomponent.CLabel();
        txtAccountNumber = new com.see.truetransact.uicomponent.CTextField();
        lblIFSC_Code = new com.see.truetransact.uicomponent.CLabel();
        lblIFSC_CodeVal = new javax.swing.JLabel();
        lblAmount = new com.see.truetransact.uicomponent.CLabel();
        txtAmount = new com.see.truetransact.uicomponent.CTextField();
        txtBeneficiaryBranch = new com.see.truetransact.uicomponent.CTextField();
        btnBeneficiaryBranch = new com.see.truetransact.uicomponent.CButton();
        txtBeneficiaryBank = new com.see.truetransact.uicomponent.CTextField();
        btnBeneficiaryBank = new com.see.truetransact.uicomponent.CButton();
        lblBeneficiaryBranch = new com.see.truetransact.uicomponent.CLabel();
        lblBeneficiaryBank = new com.see.truetransact.uicomponent.CLabel();
        txtBeneficiaryIFSC_Code = new com.see.truetransact.uicomponent.CTextField();
        lblBeneficiaryName = new com.see.truetransact.uicomponent.CLabel();
        txtBeneficiaryName = new com.see.truetransact.uicomponent.CTextField();
        lblBeneficiaryBranchName = new com.see.truetransact.uicomponent.CLabel();
        lblBeneficiaryBankName = new com.see.truetransact.uicomponent.CLabel();
        sptIssue = new com.see.truetransact.uicomponent.CSeparator();
        panRightSubIssue = new com.see.truetransact.uicomponent.CPanel();
        lblTotalAmt = new com.see.truetransact.uicomponent.CLabel();
        txtTotalAmt = new com.see.truetransact.uicomponent.CTextField();
        txtServiceTax = new com.see.truetransact.uicomponent.CTextField();
        lblServiceTax = new com.see.truetransact.uicomponent.CLabel();
        lblCharges = new com.see.truetransact.uicomponent.CLabel();
        txtPostage = new com.see.truetransact.uicomponent.CTextField();
        lblRTGS_ID = new com.see.truetransact.uicomponent.CLabel();
        lblAdviceNo = new com.see.truetransact.uicomponent.CLabel();
        txtAdviceNo = new com.see.truetransact.uicomponent.CTextField();
        lblExchangeCalc = new com.see.truetransact.uicomponent.CLabel();
        txtExchangeCalc = new com.see.truetransact.uicomponent.CTextField();
        lblExchange = new com.see.truetransact.uicomponent.CLabel();
        txtExchangeCollect = new com.see.truetransact.uicomponent.CTextField();
        lblRTGS_IDVal = new com.see.truetransact.uicomponent.CLabel();
        lblGST = new com.see.truetransact.uicomponent.CLabel();
        txtgst = new com.see.truetransact.uicomponent.CTextField();
        panRemarks = new com.see.truetransact.uicomponent.CPanel();
        txtRemarks = new com.see.truetransact.uicomponent.CTextField();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
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
        setMinimumSize(new java.awt.Dimension(855, 660));
        setPreferredSize(new java.awt.Dimension(855, 660));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setEnabled(false);
        btnView.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView.setPreferredSize(new java.awt.Dimension(21, 21));
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
        btnAuthorize.setEnabled(false);
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
        btnException.setEnabled(false);
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
        btnReject.setEnabled(false);
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

        panDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("RTGS/NEFT Details"));
        panDetails.setEnabled(false);
        panDetails.setMinimumSize(new java.awt.Dimension(860, 390));
        panDetails.setPreferredSize(new java.awt.Dimension(860, 390));
        panDetails.setLayout(new java.awt.GridBagLayout());

        panTableIssue.setMinimumSize(new java.awt.Dimension(280, 350));
        panTableIssue.setPreferredSize(new java.awt.Dimension(280, 350));
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

        panTotalAmount.setMinimumSize(new java.awt.Dimension(278, 29));
        panTotalAmount.setPreferredSize(new java.awt.Dimension(278, 29));
        panTotalAmount.setLayout(new java.awt.GridBagLayout());

        lblTotalAmount.setText("Total Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 69, 4, 4);
        panTotalAmount.add(lblTotalAmount, gridBagConstraints);

        txtTotalAmount.setMinimumSize(new java.awt.Dimension(120, 21));
        txtTotalAmount.setPreferredSize(new java.awt.Dimension(120, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panTotalAmount.add(txtTotalAmount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panTableIssue.add(panTotalAmount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weighty = 1.0;
        panDetails.add(panTableIssue, gridBagConstraints);

        panIssue.setMinimumSize(new java.awt.Dimension(560, 350));
        panIssue.setPreferredSize(new java.awt.Dimension(560, 350));
        panIssue.setLayout(new java.awt.GridBagLayout());

        panLeftSubIssue.setMinimumSize(new java.awt.Dimension(310, 330));
        panLeftSubIssue.setPreferredSize(new java.awt.Dimension(310, 330));
        panLeftSubIssue.setLayout(new java.awt.GridBagLayout());

        cboProductId.setMaximumSize(new java.awt.Dimension(100, 21));
        cboProductId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProductId.setPopupWidth(120);
        cboProductId.setPreferredSize(new java.awt.Dimension(120, 21));
        cboProductId.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboProductIdItemStateChanged(evt);
            }
        });
        cboProductId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProductIdActionPerformed(evt);
            }
        });
        cboProductId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboProductIdFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panLeftSubIssue.add(cboProductId, gridBagConstraints);

        lblProductId.setText("Product Id *");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeftSubIssue.add(lblProductId, gridBagConstraints);

        lblBeneficiaryIFSC_Code.setText("Beneficiary IFSC Code *");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeftSubIssue.add(lblBeneficiaryIFSC_Code, gridBagConstraints);

        lblAccountNumber.setText("Account Number *");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeftSubIssue.add(lblAccountNumber, gridBagConstraints);

        txtAccountNumber.setMinimumSize(new java.awt.Dimension(150, 21));
        txtAccountNumber.setPreferredSize(new java.awt.Dimension(150, 21));
        txtAccountNumber.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAccountNumberFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panLeftSubIssue.add(txtAccountNumber, gridBagConstraints);

        lblIFSC_Code.setText("Sender IFSC Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeftSubIssue.add(lblIFSC_Code, gridBagConstraints);

        lblIFSC_CodeVal.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        lblIFSC_CodeVal.setForeground(new java.awt.Color(0, 0, 255));
        lblIFSC_CodeVal.setMaximumSize(new java.awt.Dimension(120, 18));
        lblIFSC_CodeVal.setMinimumSize(new java.awt.Dimension(120, 18));
        lblIFSC_CodeVal.setPreferredSize(new java.awt.Dimension(120, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panLeftSubIssue.add(lblIFSC_CodeVal, gridBagConstraints);

        lblAmount.setText("Amount *");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeftSubIssue.add(lblAmount, gridBagConstraints);

        txtAmount.setMinimumSize(new java.awt.Dimension(120, 21));
        txtAmount.setPreferredSize(new java.awt.Dimension(120, 21));
        txtAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAmountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panLeftSubIssue.add(txtAmount, gridBagConstraints);

        txtBeneficiaryBranch.setMinimumSize(new java.awt.Dimension(120, 21));
        txtBeneficiaryBranch.setPreferredSize(new java.awt.Dimension(120, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panLeftSubIssue.add(txtBeneficiaryBranch, gridBagConstraints);

        btnBeneficiaryBranch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnBeneficiaryBranch.setMaximumSize(new java.awt.Dimension(25, 25));
        btnBeneficiaryBranch.setMinimumSize(new java.awt.Dimension(25, 25));
        btnBeneficiaryBranch.setPreferredSize(new java.awt.Dimension(25, 25));
        btnBeneficiaryBranch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBeneficiaryBranchActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panLeftSubIssue.add(btnBeneficiaryBranch, gridBagConstraints);

        txtBeneficiaryBank.setMinimumSize(new java.awt.Dimension(120, 21));
        txtBeneficiaryBank.setPreferredSize(new java.awt.Dimension(120, 21));
        txtBeneficiaryBank.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBeneficiaryBankFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panLeftSubIssue.add(txtBeneficiaryBank, gridBagConstraints);

        btnBeneficiaryBank.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnBeneficiaryBank.setMaximumSize(new java.awt.Dimension(25, 25));
        btnBeneficiaryBank.setMinimumSize(new java.awt.Dimension(25, 25));
        btnBeneficiaryBank.setPreferredSize(new java.awt.Dimension(25, 25));
        btnBeneficiaryBank.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBeneficiaryBankActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panLeftSubIssue.add(btnBeneficiaryBank, gridBagConstraints);

        lblBeneficiaryBranch.setText("Beneficiary Branch *");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeftSubIssue.add(lblBeneficiaryBranch, gridBagConstraints);

        lblBeneficiaryBank.setText("Beneficiary Bank *");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeftSubIssue.add(lblBeneficiaryBank, gridBagConstraints);

        txtBeneficiaryIFSC_Code.setMinimumSize(new java.awt.Dimension(120, 21));
        txtBeneficiaryIFSC_Code.setPreferredSize(new java.awt.Dimension(120, 21));
        txtBeneficiaryIFSC_Code.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBeneficiaryIFSC_CodeFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panLeftSubIssue.add(txtBeneficiaryIFSC_Code, gridBagConstraints);

        lblBeneficiaryName.setText("Beneficiary Name *");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeftSubIssue.add(lblBeneficiaryName, gridBagConstraints);

        txtBeneficiaryName.setMinimumSize(new java.awt.Dimension(120, 21));
        txtBeneficiaryName.setPreferredSize(new java.awt.Dimension(120, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panLeftSubIssue.add(txtBeneficiaryName, gridBagConstraints);

        lblBeneficiaryBranchName.setForeground(new java.awt.Color(0, 0, 255));
        lblBeneficiaryBranchName.setText("Branch Name");
        lblBeneficiaryBranchName.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblBeneficiaryBranchName.setMaximumSize(new java.awt.Dimension(240, 21));
        lblBeneficiaryBranchName.setMinimumSize(new java.awt.Dimension(240, 21));
        lblBeneficiaryBranchName.setPreferredSize(new java.awt.Dimension(240, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panLeftSubIssue.add(lblBeneficiaryBranchName, gridBagConstraints);

        lblBeneficiaryBankName.setForeground(new java.awt.Color(0, 0, 255));
        lblBeneficiaryBankName.setText("Bank Name");
        lblBeneficiaryBankName.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblBeneficiaryBankName.setMaximumSize(new java.awt.Dimension(240, 21));
        lblBeneficiaryBankName.setMinimumSize(new java.awt.Dimension(240, 21));
        lblBeneficiaryBankName.setPreferredSize(new java.awt.Dimension(240, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panLeftSubIssue.add(lblBeneficiaryBankName, gridBagConstraints);

        panIssue.add(panLeftSubIssue, new java.awt.GridBagConstraints());

        sptIssue.setOrientation(javax.swing.SwingConstants.VERTICAL);
        sptIssue.setMaximumSize(new java.awt.Dimension(5, 250));
        sptIssue.setMinimumSize(new java.awt.Dimension(5, 250));
        sptIssue.setPreferredSize(new java.awt.Dimension(5, 250));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panIssue.add(sptIssue, gridBagConstraints);

        panRightSubIssue.setMinimumSize(new java.awt.Dimension(260, 330));
        panRightSubIssue.setPreferredSize(new java.awt.Dimension(260, 330));
        panRightSubIssue.setLayout(new java.awt.GridBagLayout());

        lblTotalAmt.setText("Total Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRightSubIssue.add(lblTotalAmt, gridBagConstraints);

        txtTotalAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRightSubIssue.add(txtTotalAmt, gridBagConstraints);

        txtServiceTax.setMinimumSize(new java.awt.Dimension(100, 21));
        txtServiceTax.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtServiceTaxFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRightSubIssue.add(txtServiceTax, gridBagConstraints);

        lblServiceTax.setText("Service Tax");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRightSubIssue.add(lblServiceTax, gridBagConstraints);

        lblCharges.setText("Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRightSubIssue.add(lblCharges, gridBagConstraints);

        txtPostage.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPostage.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPostageFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRightSubIssue.add(txtPostage, gridBagConstraints);

        lblRTGS_ID.setText("RTGS ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRightSubIssue.add(lblRTGS_ID, gridBagConstraints);

        lblAdviceNo.setText("Advice No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRightSubIssue.add(lblAdviceNo, gridBagConstraints);

        txtAdviceNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRightSubIssue.add(txtAdviceNo, gridBagConstraints);

        lblExchangeCalc.setText("Exc Calc");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRightSubIssue.add(lblExchangeCalc, gridBagConstraints);

        txtExchangeCalc.setEditable(false);
        txtExchangeCalc.setMinimumSize(new java.awt.Dimension(100, 21));
        txtExchangeCalc.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtExchangeCalcFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRightSubIssue.add(txtExchangeCalc, gridBagConstraints);

        lblExchange.setText("Exc Collected");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRightSubIssue.add(lblExchange, gridBagConstraints);

        txtExchangeCollect.setMinimumSize(new java.awt.Dimension(100, 21));
        txtExchangeCollect.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtExchangeCollectFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRightSubIssue.add(txtExchangeCollect, gridBagConstraints);

        lblRTGS_IDVal.setForeground(new java.awt.Color(0, 0, 255));
        lblRTGS_IDVal.setText("RTGS ID");
        lblRTGS_IDVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblRTGS_IDVal.setMaximumSize(new java.awt.Dimension(135, 18));
        lblRTGS_IDVal.setMinimumSize(new java.awt.Dimension(135, 18));
        lblRTGS_IDVal.setPreferredSize(new java.awt.Dimension(135, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRightSubIssue.add(lblRTGS_IDVal, gridBagConstraints);

        lblGST.setText("GST");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panRightSubIssue.add(lblGST, gridBagConstraints);

        txtgst.setAllowNumber(true);
        txtgst.setMinimumSize(new java.awt.Dimension(100, 21));
        txtgst.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtgstFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        panRightSubIssue.add(txtgst, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panIssue.add(panRightSubIssue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panDetails.add(panIssue, gridBagConstraints);

        panRemarks.setMinimumSize(new java.awt.Dimension(540, 27));
        panRemarks.setPreferredSize(new java.awt.Dimension(540, 27));
        panRemarks.setLayout(new java.awt.GridBagLayout());

        txtRemarks.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtRemarks.setMinimumSize(new java.awt.Dimension(370, 21));
        txtRemarks.setPreferredSize(new java.awt.Dimension(370, 21));
        txtRemarks.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRemarksFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 0, 4);
        panRemarks.add(txtRemarks, gridBagConstraints);

        lblRemarks.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblRemarks.setText("Favouring Details *");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 17, 0, 0);
        panRemarks.add(lblRemarks, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        panDetails.add(panRemarks, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
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

        panTransaction.setMinimumSize(new java.awt.Dimension(700, 280));
        panTransaction.setPreferredSize(new java.awt.Dimension(700, 280));
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

    private void cboProductIdItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboProductIdItemStateChanged
    }//GEN-LAST:event_cboProductIdItemStateChanged

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_VIEW);
        popUp("Enquiry");
        lblStatus.setText("Enquiry");
        btnSave.setEnabled(false);
        btnView.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnViewActionPerformed

    private void mitCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mitCloseMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_mitCloseMouseClicked

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

    private void txtAccountNumberFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccountNumberFocusLost
                            }//GEN-LAST:event_txtAccountNumberFocusLost

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        //  ClientUtil.showReport("DDIssueRegister");
        HashMap reportParamMap = new HashMap();
        com.see.truetransact.clientutil.ttrintegration.LinkReport.getReports(getScreenID(), reportParamMap);
    }//GEN-LAST:event_btnPrintActionPerformed
    
    private void updateTableData() {
        cboProductId.setSelectedItem(CommonUtil.convertObjToStr(observable.getCboProductId()));
        txtBeneficiaryBank.setText(observable.getTxtBeneficiaryBank());
        //txtBeneficiaryBankName1.setText(observable.getTxtBeneficiaryBankName());
        lblBeneficiaryBankName.setText(observable.getTxtBeneficiaryBankName());
        txtBeneficiaryBranch.setText(observable.getTxtBeneficiaryBranch());
        //txtBeneficiaryBranchName.setText(observable.getTxtBeneficiaryBranchName());
        lblBeneficiaryBranchName.setText(observable.getTxtBeneficiaryBranchName());
        txtBeneficiaryIFSC_Code.setText(observable.getTxtBeneficiaryIFSC_Code());
        txtBeneficiaryName.setText(observable.getTxtBeneficiaryName());
        txtAmount.setText(observable.getTxtAmt());
        txtAccountNumber.setText(observable.getTxtAccountNumber());
        txtRemarks.setText(observable.getTxtRemarks());
        txtExchangeCalc.setText(observable.getTxtExchangeCalc());
        txtExchangeCollect.setText(observable.getTxtExchangeCollect());
        txtServiceTax.setText(observable.getTxtServiceTax());
        txtgst.setText(observable.getTxtgst());
        txtPostage.setText(observable.getTxtCharges());
        txtTotalAmt.setText(observable.getTxtTotalAmt());
        lblRTGS_IDVal.setText(observable.getRTGS_ID());
    }
    
    private void tblIssueMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblIssueMousePressed
        // TODO add your handling code here:
        if (tblIssue.getRowCount() > 0) {
            updateMode = true;
            updateTab = tblIssue.getSelectedRow();
            observable.setNewData(false);
            int st = CommonUtil.convertObjToInt(tblIssue.getValueAt(tblIssue.getSelectedRow(), 0));
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                ClientUtil.enableDisable(panDetails, true);
            } else {
                ClientUtil.enableDisable(panDetails, false);
            }
            observable.populateTableDetails(st);
            updateTableData();
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION) {
                btnEnableDisable(false);
                btnDisable(false);
                ClientUtil.enableDisable(panDetails, false);
            } else {
                ClientUtil.enableDisable(panDetails, true);
                btnEnableDisable(true);
                btnDisable(true);
                btnNewIssue.setEnabled(false);
            }
            txtServiceTax.setEnabled(false);
            if (ProxyParameters.HEAD_OFFICE.equals(ProxyParameters.BRANCH_ID)) {
                txtPostage.setEnabled(true);
            } else {
                txtPostage.setEnabled(false);
            }
            //txtPostage.setEnabled(false);
            txtExchangeCollect.setEnabled(false);
            txtBeneficiaryBank.setEnabled(false);
            txtBeneficiaryBranch.setEnabled(false);
            txtTotalAmt.setEnabled(false);
            txtExchangeCalc.setEnabled(false);
            cboProductId.setEnabled(false);
            txtTotalAmount.setEnabled(false);
        }
    }//GEN-LAST:event_tblIssueMousePressed
                        private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
                            newActionperformed();
    }//GEN-LAST:event_btnNewActionPerformed
    private void newActionperformed() {
        setModified(true);
        ClientUtil.enableDisable(this, false);// Enables the panel...
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        txtTotalAmount.setText("");
        txtTotalAmount.setEnabled(false);
        setButtonEnableDisable();
        ClientUtil.clearAll(this);
        lblStatus.setText("New");
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        transactionUI.cancelAction(false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.resetObjects();
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
        btnEnableDisable(false);
        btnDisable(false);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnNewIssue.setEnabled(true);
        lblRTGS_IDVal.setText("");
        lblRTGS_ID.setVisible(false);
        lblRTGS_IDVal.setVisible(false);
        txtTotalAmount.setEnabled(false);
    }

    private void updateProductId() {
        cboProductId.setSelectedItem(observable.getCboProductId());
    }

    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        updateAuthorizeStatus(CommonConstants.STATUS_EXCEPTION);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_EXCEPTION);
        btnCancel.setEnabled(true);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        updateAuthorizeStatus(CommonConstants.STATUS_REJECTED);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_REJECT);
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // Add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        updateAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
        btnCancel.setEnabled(true);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    private void updateAuthorizeStatus(String authorizeStatus) {
        if (viewType == AUTHORIZE && isFilled) {
            //Set screen & Module added by Kannan
            observable.setModule(getModule());
            observable.setScreen(getScreen());
            ArrayList arrList = new ArrayList();
            HashMap authorizeMap = new HashMap();
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("RTGS_ID", lblRTGS_IDVal.getText());
            singleAuthorizeMap.put("AUTHORIZED_BY", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("AUTHORIZED_DT", ClientUtil.getCurrentDateWithTime());
            //Added By Revathi 25/08/2016 reff by Abi
            boolean exceedTime = false;
            if (!CommonUtil.convertObjToStr(cboProductId.getSelectedItem()).equals("")) {//if condition Added By Revathi 29/08/2016
                if (CommonUtil.convertObjToStr(cboProductId.getSelectedItem()).equals("RTGS")
                        && authorizeStatus.equals("AUTHORIZED")) {
                    exceedTime = checkServerTime();
                    if (exceedTime == true) {
                        ClientUtil.showMessageWindow("RTGS Cannot be Authorized after 4 PM.Reject the record");
                        btnCancelActionPerformed(null);
                        return;
                    }
                }
            } else {
                ClientUtil.showMessageWindow("Please select RTGS/Transaction Record");
                return;
            }
            arrList.add(singleAuthorizeMap);
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorizeMap.put("FILTERED_LIST", "FILTERED_LIST" + "_" + ProxyParameters.dbDriverName);
            authorize(authorizeMap);
            viewType = "";
            super.setOpenForEditBy(observable.getStatusBy());
            singleAuthorizeMap = null;
            arrList = null;
            authorizeMap = null;
        } else {
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put("USER_ID", TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            mapParam.put(CommonConstants.MAP_NAME, "getRTGSAuthorize");
            isFilled = false;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
            observable.setStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }
    
    public void authorize(HashMap map) {
        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
            observable.set_authorizeMap(map);
            if (transactionUI.getOutputTO().size() > 0) {
                observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
            }
            observable.doAction();

            if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                //System.out.println("observable.getProxyReturnMap()" + observable.getProxyReturnMap() + "observable.getActionType()" + observable.getActionType());
                if (observable.getProxyReturnMap() != null && observable.getProxyReturnMap().size() > 0) {
                    if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE) {
                        if(observable.getProxyReturnMap().containsKey("RTGS_ID")) {
                            displayTransDetails(CommonUtil.convertObjToStr(observable.getProxyReturnMap().get("RTGS_ID")));
                        }
                        if (observable.getProxyReturnMap().containsKey("UTR_NUMBER")) {
                            //ClientUtil.showMessageWindow("RTGS/NEFT UTR NUMBER : " + observable.getProxyReturnMap().get("UTR_NUMBER"));
                            TableDialogUI tableData = new TableDialogUI(observable.getProxyReturnMap(), "UTR_NUMBER");
                            tableData.setTitle("RTGS/NEFT UTR NUMBER");
                            tableData.show();
                        }
                        // Added by nithya on 13-07-2018 for KD 153 - Need transfer print at neft/rtgs
                         if(observable.getProxyReturnMap().containsKey("RTGS_ID")){
                            int yesNo = 0;
                            String[] voucherOptions = {"Yes", "No"};
                            yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                                    COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                                    null, voucherOptions, voucherOptions[0]);
                            if (yesNo == 0) {
                                HashMap transMap = new HashMap();
                                TTIntegration ttIntgration = null;
                                HashMap printParamMap = new HashMap();
                                transMap.put("BATCH_ID", observable.getProxyReturnMap().get("RTGS_ID"));
                                transMap.put("TRANS_DT", currDate);
                                transMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                                List lst = ClientUtil.executeQuery("getTransferTransRTGSNEFTDetails", transMap);
                                if (lst != null && lst.size() > 0) {
                                    HashMap transLstMap = (HashMap) lst.get(0);
                                    printParamMap.put("TransDt", currDate);
                                    printParamMap.put("BranchId", ProxyParameters.BRANCH_ID);
                                    printParamMap.put("TransId", transLstMap.get("SINGLE_TRANS_ID"));
                                    ttIntgration.setParam(printParamMap);
                                    ttIntgration.integrationForPrint("ReceiptPayment");
                                }
                            }
                        }
                         // End
                        observable.setProxyReturnMap(null);
                    }
                }
                setMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
                super.setOpenForEditBy(observable.getStatusBy());
                super.removeEditLock(lblRTGS_IDVal.getText());
                deleteScreenLock(lblRTGS_IDVal.getText(), "AUTHORIZE");
                removeEditLockCancelTrans(lblRTGS_IDVal.getText(), getScreenID());
                btnCancelActionPerformed(null);
                observable.setStatus();
                observable.setResultStatus();
                lblStatus.setText(ClientConstants.RESULT_STATUS[observable.getResult()]);
            }
        }
    }
    
    private void txtPostageFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPostageFocusLost
        // Add your handling code here:
        CalcTotalAmount();
    }//GEN-LAST:event_txtPostageFocusLost

    private void txtServiceTaxFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtServiceTaxFocusLost
        CalcTotalAmount();
    }//GEN-LAST:event_txtServiceTaxFocusLost

    private void txtAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAmountFocusLost
        if (cboProductId.getSelectedIndex() > 0) {
            if (txtAmount.getText().length() > 0) {
                HashMap prodMap = new HashMap();
                prodMap.put("PROD_ID", ((ComboBoxModel) cboProductId.getModel()).getKeyForSelected());
                if (CommonUtil.convertObjToStr(prodMap.get("PROD_ID")).equals("RTGS")) {//Changed By Revathi 25/08/2016 reff by Srinath Sir
                    List prodList = ClientUtil.executeQuery("getRTGSAccountHeads", prodMap);
                    if (prodList != null && prodList.size() > 0) {
                        prodMap = (HashMap) prodList.get(0);
                        if (CommonUtil.convertObjToDouble(prodMap.get("MAX_AMOUNT")) < CommonUtil.convertObjToDouble(txtAmount.getText())) {
                            ClientUtil.showMessageWindow("Maximum Amount Should be Rs. " + prodMap.get("MAX_AMOUNT"));
                            txtAmount.setText("0");
                            return;
                        } else if (CommonUtil.convertObjToDouble(prodMap.get("MIN_AMOUNT")) > CommonUtil.convertObjToDouble(txtAmount.getText())) {
                            ClientUtil.showMessageWindow("Minimum Amount Should be Rs. " + prodMap.get("MIN_AMOUNT"));
                            txtAmount.setText("0");
                            return;
                        }
                        txtServiceTax.setEnabled(false);
                        if (ProxyParameters.HEAD_OFFICE.equals(ProxyParameters.BRANCH_ID)) // head office can change charges amount 
                        {
                            txtPostage.setEnabled(true);
                        } else {
                            txtPostage.setEnabled(false);
                        }
                        txtPostage.setEnabled(true);
                        txtExchangeCollect.setEnabled(false);
                        if (CommonUtil.convertObjToDouble(txtAmount.getText()) > 0) {
                            prodMap.put("PROD_ID", ((ComboBoxModel) cboProductId.getModel()).getKeyForSelected());
                            prodMap.put("AMOUNT", txtAmount.getText());
                            List chargeList = ClientUtil.executeQuery("getRTGSCharges", prodMap);
                            if (chargeList != null && chargeList.size() > 0) {
                                prodMap = (HashMap) chargeList.get(0);
                                txtPostage.setText(String.valueOf(CommonUtil.convertObjToDouble(prodMap.get("CHARGE"))));
                                double serviceTax = 0.0;
                                serviceTax = CommonUtil.convertObjToDouble(prodMap.get("SERVICE_TAX"))
                                        * CommonUtil.convertObjToDouble(txtAmount.getText()) / 100;
                                Rounding rod = new Rounding();
                                serviceTax = (double) rod.getNearest((long) (serviceTax * 100), 100) / 100;
                                txtServiceTax.setText(String.valueOf(serviceTax));
                            }
                        }
                        CalcTotalAmount();
                    }
                }else if (CommonUtil.convertObjToStr(prodMap.get("PROD_ID")).equals("NEFT")) {
                    boolean exceedTime = false;
                    boolean allowRTGSAmount = true;
                    if(CommonUtil.convertObjToStr(lblBeneficiaryBankName.getText().toUpperCase()).equals("ICICI BANK")) {
                        allowRTGSAmount=false;
                    }
                    List prodList = ClientUtil.executeQuery("getRTGSAccountHeads", prodMap);
                    if (prodList != null && prodList.size() > 0) {
                        prodMap = (HashMap) prodList.get(0);
                        if (CommonUtil.convertObjToDouble(prodMap.get("MAX_AMOUNT")) < CommonUtil.convertObjToDouble(txtAmount.getText())) {
                            exceedTime = checkServerTime();
                            if (exceedTime == true) {
                                HashMap productMap = new HashMap();
                                productMap.put("PROD_ID", "RTGS");
                                List productList = ClientUtil.executeQuery("getRTGSAccountHeads", productMap);
                                if (productList != null && productList.size() > 0) {
                                    productMap = (HashMap) productList.get(0);
                                    if (CommonUtil.convertObjToDouble(productMap.get("MAX_AMOUNT")) < CommonUtil.convertObjToDouble(txtAmount.getText())) {
                                        if (allowRTGSAmount) {
                                            ClientUtil.showMessageWindow("Maximum Amount Should be Rs. " + productMap.get("MAX_AMOUNT"));
                                            txtAmount.setText("0");
                                            return;
                                        }
                                    } else if (CommonUtil.convertObjToDouble(prodMap.get("MIN_AMOUNT")) > CommonUtil.convertObjToDouble(txtAmount.getText())) {
                                        ClientUtil.showMessageWindow("Minimum Amount Should be Rs. " + productMap.get("MIN_AMOUNT"));
                                        txtAmount.setText("0");
                                        return;
                                    }
                                    txtServiceTax.setEnabled(false);
                                    if (ProxyParameters.HEAD_OFFICE.equals(ProxyParameters.BRANCH_ID)) // head office can change charges amount 
                                    {
                                        txtPostage.setEnabled(true);
                                    } else {
                                        txtPostage.setEnabled(false);
                                    }
                                    txtPostage.setEnabled(true);
                                    txtExchangeCollect.setEnabled(false);
                                    if (CommonUtil.convertObjToDouble(txtAmount.getText()) > 0) {
                                        prodMap.put("PROD_ID", "RTGS");
                                        prodMap.put("AMOUNT", txtAmount.getText());
                                        List chargeList = ClientUtil.executeQuery("getRTGSCharges", prodMap);
                                        if (chargeList != null && chargeList.size() > 0) {
                                            prodMap = (HashMap) chargeList.get(0);
                                            txtPostage.setText(String.valueOf(CommonUtil.convertObjToDouble(prodMap.get("CHARGE"))));
                                            double serviceTax = 0.0;
                                            serviceTax = CommonUtil.convertObjToDouble(prodMap.get("SERVICE_TAX"))
                                                    * CommonUtil.convertObjToDouble(txtAmount.getText()) / 100;
                                            Rounding rod = new Rounding();
                                            serviceTax = (double) rod.getNearest((long) (serviceTax * 100), 100) / 100;
                                            txtServiceTax.setText(String.valueOf(serviceTax));
                                        }
                                    }
                                    CalcTotalAmount();
                                }
                            } else {
                                if (CommonUtil.convertObjToDouble(prodMap.get("MAX_AMOUNT")) < CommonUtil.convertObjToDouble(txtAmount.getText())) {
                                    if (allowRTGSAmount) {
                                        ClientUtil.showMessageWindow("Maximum Amount Should be Rs. " + prodMap.get("MAX_AMOUNT"));
                                        txtAmount.setText("0");
                                        return;
                                    }
                                } else if (CommonUtil.convertObjToDouble(prodMap.get("MIN_AMOUNT")) > CommonUtil.convertObjToDouble(txtAmount.getText())) {
                                    ClientUtil.showMessageWindow("Minimum Amount Should be Rs. " + prodMap.get("MIN_AMOUNT"));
                                    txtAmount.setText("0");
                                    return;
                                }
                                txtServiceTax.setEnabled(false);
                                if (ProxyParameters.HEAD_OFFICE.equals(ProxyParameters.BRANCH_ID)) // head office can change charges amount 
                                {
                                    txtPostage.setEnabled(true);
                                } else {
                                    txtPostage.setEnabled(false);
                                }
                                txtPostage.setEnabled(true);
                                txtExchangeCollect.setEnabled(false);
                                if (CommonUtil.convertObjToDouble(txtAmount.getText()) > 0) {
                                    prodMap.put("PROD_ID", ((ComboBoxModel) cboProductId.getModel()).getKeyForSelected());
                                    prodMap.put("AMOUNT", txtAmount.getText());
                                    List chargeList = ClientUtil.executeQuery("getRTGSCharges", prodMap);
                                    if (chargeList != null && chargeList.size() > 0) {
                                        prodMap = (HashMap) chargeList.get(0);
                                        txtPostage.setText(String.valueOf(CommonUtil.convertObjToDouble(prodMap.get("CHARGE"))));
                                        double serviceTax = 0.0;
                                        serviceTax = CommonUtil.convertObjToDouble(prodMap.get("SERVICE_TAX"))
                                                * CommonUtil.convertObjToDouble(txtAmount.getText()) / 100;
                                        Rounding rod = new Rounding();
                                        serviceTax = (double) rod.getNearest((long) (serviceTax * 100), 100) / 100;
                                        txtServiceTax.setText(String.valueOf(serviceTax));
                                    }
                                }
                                CalcTotalAmount();
                            }
                        } else if (exceedTime == false) {
                            if (CommonUtil.convertObjToDouble(prodMap.get("MAX_AMOUNT")) < CommonUtil.convertObjToDouble(txtAmount.getText())) {
                                if (allowRTGSAmount) {
                                    ClientUtil.showMessageWindow("Maximum Amount Should be Rs. " + prodMap.get("MAX_AMOUNT"));
                                    txtAmount.setText("0");
                                    return;
                                }
                            } else if (CommonUtil.convertObjToDouble(prodMap.get("MIN_AMOUNT")) > CommonUtil.convertObjToDouble(txtAmount.getText())) {
                                ClientUtil.showMessageWindow("Minimum Amount Should be Rs. " + prodMap.get("MIN_AMOUNT"));
                                txtAmount.setText("0");
                                return;
                            }
                            txtServiceTax.setEnabled(false);
                            if (ProxyParameters.HEAD_OFFICE.equals(ProxyParameters.BRANCH_ID)) // head office can change charges amount 
                            {
                                txtPostage.setEnabled(true);
                            } else {
                                txtPostage.setEnabled(false);
                            }
                            txtPostage.setEnabled(true);
                            txtExchangeCollect.setEnabled(false);
                            if (CommonUtil.convertObjToDouble(txtAmount.getText()) > 0) {
                                prodMap.put("PROD_ID", ((ComboBoxModel) cboProductId.getModel()).getKeyForSelected());
                                prodMap.put("AMOUNT", txtAmount.getText());
                                List chargeList = ClientUtil.executeQuery("getRTGSCharges", prodMap);
                                if (chargeList != null && chargeList.size() > 0) {
                                    prodMap = (HashMap) chargeList.get(0);
                                    txtPostage.setText(String.valueOf(CommonUtil.convertObjToDouble(prodMap.get("CHARGE"))));
                                    double serviceTax = 0.0;
                                    serviceTax = CommonUtil.convertObjToDouble(prodMap.get("SERVICE_TAX"))
                                            * CommonUtil.convertObjToDouble(txtAmount.getText()) / 100;
                                    Rounding rod = new Rounding();
                                    serviceTax = (double) rod.getNearest((long) (serviceTax * 100), 100) / 100;
                                    txtServiceTax.setText(String.valueOf(serviceTax));
                                }
                            }
                            CalcTotalAmount();
                        }
                    }
                }
            }
        } else {
            ClientUtil.showMessageWindow("Please Select Product ID ... !!!");
            txtAmount.setText("0");
            return;
        }
    }//GEN-LAST:event_txtAmountFocusLost
    private void CalcTotalAmount() {
        txtTotalAmt.setText(String.valueOf(CommonUtil.convertObjToDouble(txtAmount.getText())
                + CommonUtil.convertObjToDouble(txtExchangeCollect.getText()) + CommonUtil.convertObjToDouble(txtPostage.getText())
                + CommonUtil.convertObjToDouble(txtServiceTax.getText()) + /*CommonUtil.convertObjToDouble(txtgst.getText())+*/
                  CommonUtil.convertObjToDouble(txtExchangeCalc.getText())));
        /*transactionUI.cancelAction(false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.resetObjects();
        transactionUI.setCallingTransType("TRANSFER");
        transactionUI.setCallingAmount(txtTotalAmt.getText());*/
        if(txtExchangeCollect.getText().length()==0){
            txtExchangeCollect.setText("0");
        }
        if(txtPostage.getText().length()==0){
            txtPostage.setText("0");
        }
        if(txtServiceTax.getText().length()==0){
            txtServiceTax.setText("0");
        }
        if(txtExchangeCalc.getText().length()==0){
            txtExchangeCalc.setText("0");
        }
        
         if(txtgst.getText().length()==0){
            txtgst.setText("0");
        }
        
        updateOBFields();
    }
    private void cboProductIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProductIdActionPerformed
        // Add your handling code here:
        if ((observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT)) {//Added By Revathi 25/08/2016 reff by Abi
            if (CommonUtil.convertObjToStr(cboProductId.getSelectedItem()).equals("RTGS")) {
                boolean ExceedTime = false;
                ExceedTime = checkServerTime();
                if (ExceedTime == true) {
                    ClientUtil.showMessageWindow("RTGS Not allowed after 4 PM,Make a NEFT transaction");
                    cboProductId.setSelectedItem("");
                    return;
                }
            }
        }
    }//GEN-LAST:event_cboProductIdActionPerformed

    //   To set selected row from IssueDetails Table
    private void setSelectRow(int SelectedRow) {
        selectedRowValue = SelectedRow;
    }
    //    To get selected row from IssueDetails Table

    private int getSelectRow() {
        return selectedRowValue;
    }

    private void btnDeleteIssueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteIssueActionPerformed
        // Add your handling code here:
        int st = CommonUtil.convertObjToInt(tblIssue.getValueAt(tblIssue.getSelectedRow(), 0));
        observable.deleteTableData(st, tblIssue.getSelectedRow());
        tblIssue.setModel(observable.getTblIssueDetails());
        setTotalAmount();
        setSizeTableData();
        ClientUtil.enableDisable(panDetails, false);
        observable.resetRTGSDetails();
        resetRTGSDetails();
        btnEnableDisable(false);
        btnDisable(false);
        btnNewIssue.setEnabled(true);
        CalcTotalAmount();
    }//GEN-LAST:event_btnDeleteIssueActionPerformed

    private void btnNewIssueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewIssueActionPerformed
        // Add your handling code here:
        if (tblIssue.getRowCount() >= allowedRows) {//Added By Kannan
            ClientUtil.displayAlert("Number Of Entries Can not be More Than 200!!!");
            return;
        }
        updateMode = false;
        observable.setNewData(true);        
        ClientUtil.enableDisable(panDetails, true);
        btnEnableDisable(true);
        btnDisable(false);
        btnNewIsPressed = true;
        tableIssueMousePressed = false;
        btnSaveIssue.setEnabled(true);
        observable.resetRTGSDetails();
        txtTotalAmt.setEnabled(false);
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            lblRTGS_IDVal.setText("");
            observable.resetRTGS_ID();
        }
        txtExchangeCalc.setEnabled(false);
        txtExchangeCollect.setEnabled(false);
        updation=false;
        txtAdviceNo.setEnabled(false);
        txtBeneficiaryBank.setEnabled(false);
        txtBeneficiaryBranch.setEnabled(false);
        if(tblIssue.getRowCount()>0){
            cboProductId.setEnabled(false);
        }else{
            cboProductId.setEnabled(true);
        }
        txtTotalAmount.setEnabled(false);
        txtBeneficiaryBank.setEnabled(true);
    }//GEN-LAST:event_btnNewIssueActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed

    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // Add your handling code here:
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed

    private void enableAuthorizeButton(boolean flag) {
        btnAuthorize.setEnabled(flag);
        btnReject.setEnabled(flag);
        btnException.setEnabled(flag);
    }

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        viewType = "CANCEL";
        deleteScreenLock(lblRTGS_IDVal.getText(), "CANCEL");
        lblStatus.setText("               ");
        observable.resetForm();
        observable.resetTableValues();
        tblIssue.setModel(observable.getTblIssueDetails());
        setSizeTableData();
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(this, false);
        setModified(false);
        btnNew.setEnabled(true);
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        btnSave.setEnabled(false);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
        btnView.setEnabled(true);
        isFilled = false;
        btnEnableDisable(false);
        btnDisable(false);
        transactionUI.setCallingApplicantName("");
        transactionUI.setCallingAmount("");
        transactionUI.setCallingTransAcctNo("");
        transactionUI.setCallingProdID("");
        lblRTGS_IDVal.setText("");
        lblBeneficiaryBranchName.setText("");
        lblBeneficiaryBankName.setText("");
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
        transactionUI.setMainEnableDisable(false);
        txtTotalAmount.setText("");
        txtTotalAmount.setEnabled(false);
    }//GEN-LAST:event_btnCancelActionPerformed

    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // Add your handling code here:
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed

    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // Add your handling code here:
        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_VIEW);
        popUp("Delete");
        lblStatus.setText("Delete");
        btnSave.setEnabled(false);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_VIEW);
        popUp("Edit");
        lblStatus.setText("Edit");
        //btnSave.setEnabled(false);
        btnNewIssue.setEnabled(true);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed
    /**
     * To display a popUp window for viewing existing data
     */
    private void setEnable() {
        btnNew.setEnabled(true);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
    }
     public boolean checkInterbranchAllowed() {
         setSelectedBranchID(ProxyParameters.HEAD_OFFICE);
         System.out.println("getSelectedBranchID" +getSelectedBranchID());
        if (getSelectedBranchID() != null && !ProxyParameters.BRANCH_ID.equals(getSelectedBranchID())) {
            Date selectedBranchDt = ClientUtil.getOtherBranchCurrentDate(getSelectedBranchID());
//Date currentDate = ClientUtil.getCurrentDate();   //Commented By Suresh
            Date currentDate = currDate;     //Added By Suresh
            if (selectedBranchDt == null) {
                ClientUtil.displayAlert("BOD is not completed for the Head Office branch " + "\n" + "Interbranch Transaction Not allowed");
                return true;
            } else if (DateUtil.dateDiff(currentDate, selectedBranchDt) != 0) {
                ClientUtil.displayAlert("Application Date is different in the Head Office " + "\n" + "Interbranch Transaction Not allowed");
                return true;
            } else {
                System.out.println("Continue for interbranch trasactions ...");
            }
        }
         return false;
    }
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        if ((observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT
                || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE) && tblIssue.getRowCount() > 0) {
            if((cboProductId.getSelectedIndex() > 0) && (txtAmount.getText().length() > 0 || txtAccountNumber.getText().length() > 0)) {
                ClientUtil.showMessageWindow("Please Save RTGS/NEFT Details");
                return;
            }
            if(checkInterbranchAllowed()){
                return;
            }
            int transactionSize = 0;
            if (transactionUI.getOutputTO().size() == 0) {
                ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS));
                return;
            } else {
                transactionSize = (transactionUI.getOutputTO()).size();
            }
            if (transactionSize != 0) {
                if (!transactionUI.isBtnSaveTransactionDetailsFlag() && observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                    ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.SAVE_TX_DETAILS));
                    return;
                }
                if (transactionUI.getOutputTO().size() > 0) {
                    observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                    //Added by kannan
                    if (transactionUI.getOutputTO() != null && transactionUI.getOutputTO().size() > 0) {
//                        List orgORrespList = transactionUI.getOrgRespList();                        
//                        if (orgORrespList != null && orgORrespList.size() > 0) {
//                            observable.setOrgORrespDetailsList((ArrayList) orgORrespList);
//                        }
                    }
                    savePerformed();
                }
            }
            setModified(false);
        } else {
            ClientUtil.showMessageWindow("Please Enter RTGS/NEFT Details");
            return;
        }
    }//GEN-LAST:event_btnSaveActionPerformed
    private void savePerformed() {
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT && lblRTGS_IDVal.getText().length()>0){
            observable.setRTGS_ID(lblRTGS_IDVal.getText());
        }
        
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || 
                observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){ //Added By Suresh R   Ref By Mr Abi and Mr Srinath  06-Jul-2017
            boolean isError = observable.checkAmount();
            if(isError){
                return;
            }
        }
        
        observable.doAction();
        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            System.out.println("observable.getProxyReturnMap()"+observable.getProxyReturnMap()+"observable.getActionType()"+observable.getActionType());
            if (observable.getProxyReturnMap() != null && observable.getProxyReturnMap().size() > 0) {
                if ((observable.getActionType() == ClientConstants.ACTIONTYPE_NEW)
                        && observable.getProxyReturnMap().containsKey("RTGS_ID")) {
                    ClientUtil.showMessageWindow("RTGS/NEFT ID : " + observable.getProxyReturnMap().get("RTGS_ID"));
                    editLockValidation(CommonUtil.convertObjToStr(observable.getProxyReturnMap().get("RTGS_ID")), getScreenID());
                }else if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
                         && observable.getProxyReturnMap().containsKey("UTR_NUMBER")){
                    ClientUtil.showMessageWindow("RTGS/NEFT UTR NUMBER : " + observable.getProxyReturnMap().get("UTR_NUMBER"));
                }
                if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT && lblRTGS_IDVal.getText().length()>0){
                    editLockValidation(lblRTGS_IDVal.getText(), getScreenID());
                    deleteScreenLock(lblRTGS_IDVal.getText(), "CANCEL");
                }
                /*if ((observable.getActionType() == ClientConstants.ACTIONTYPE_NEW)
                        && observable.getProxyReturnMap().containsKey("RTGS_ID")) {
                    displayTransDetails(CommonUtil.convertObjToStr(observable.getProxyReturnMap().get("RTGS_ID")));
                    observable.setProxyReturnMap(null);
                }*/
            }
            btnCancelActionPerformed(null);
            lblStatus.setText(ClientConstants.RESULT_STATUS[observable.getResult()]);
        }
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        setModified(false);
        ClientUtil.clearAll(this);
    }
    
    private boolean checkServerTime() {//Added By Revathi 25/08/2016 reff by Abi
        HashMap serverMap = new HashMap();
        boolean value = false;
        String currTime = "";
        String serverTime = "";
        double serTime = 0.0;
        double curTime = 0.0;
        List sysLst = ClientUtil.executeQuery("checkSysTime", serverMap);
        List checkLst = ClientUtil.executeQuery("getParameterDetails", serverMap);
        HashMap checkMap = new HashMap();
        if (sysLst != null && sysLst.size() > 0) {
            serverMap = (HashMap) sysLst.get(0);
            serverTime = CommonUtil.convertObjToStr(serverMap.get("SYS_TIME"));
            serTime = CommonUtil.convertObjToDouble(serverTime);
            if (checkLst != null && checkLst.size() > 0) {
                checkMap = (HashMap) checkLst.get(0);
                currTime = CommonUtil.convertObjToStr(checkMap.get("RTGS_CHECK_TIME"));
                curTime = CommonUtil.convertObjToDouble(currTime);
                if (currTime.length() > 0 && serverTime.length() > 0) {
                    if (curTime < serTime) {
                        value= true;
                    }
                }
            }
        }
        return value;
    }
    
    private void displayTransDetail(HashMap proxyResultMap) {
        String transferDisplayStr = "Transfer Transaction Details...\n";
        String transId = "";
        String transMode = "";
        Object keys[] = proxyResultMap.keySet().toArray();
        int cashCount = 0;
        int transferCount = 0;
        List tempList = null;
        HashMap transMap = null;
        String actNum = "";
        List transIdList = new ArrayList();
        for (int i = 0; i < keys.length; i++) {
            if (proxyResultMap.get(keys[i]) instanceof String) {
                continue;
            }
            tempList = (List) proxyResultMap.get(keys[i]);
            if (CommonUtil.convertObjToStr(keys[i]).indexOf("TRANSFER") != -1) {
                for (int j = 0; j < tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j == 0) {
                        transId = (String) transMap.get("BATCH_ID");
                        transIdList.add(transId);
                        transMode = "TRANSFER";
                    }
                    transferDisplayStr += "Trans Id : " + transMap.get("TRANS_ID")
                            + "   Batch Id : " + transMap.get("BATCH_ID")
                            + "   Trans Type : " + transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if (actNum != null && !actNum.equals("")) {
                        transferDisplayStr += "   Account No : " + transMap.get("ACT_NUM")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    } else {
                        transferDisplayStr += "   Account Head : " + transMap.get("AC_HD_ID")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    }
                }
                transferCount++;
            }
        }
        if (transferCount > 0) {
            ClientUtil.showMessageWindow("" + transferDisplayStr);
        }
    }
    
    private void displayTransDetails(String  linkBatchID) {    //Added By Suresh R 08-Dec-2016
        HashMap transMap = new HashMap();
        transMap.put("BATCH_ID", linkBatchID);
        transMap.put("TRANS_DT", currDate);
        transMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        transMap.put("SHARE_DIVIDEND", "SHARE_DIVIDEND");
        TableDialogUI tableDialogUI = null;
        tableDialogUI = new TableDialogUI("getTransferDetailsWithAccHeadDesc", transMap, "");
        tableDialogUI.setTitle("RTGS/NEFT Transaction Details");
        tableDialogUI.show();
    }
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
    private void btnSaveIssueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveIssueActionPerformed
        // Add your handling code here:
        if (cboProductId.getSelectedIndex() <= 0) {
            ClientUtil.showMessageWindow("Product ID Should not be Empty...!!!");
            return;
        } else if (lblIFSC_CodeVal.getText().length() <= 0) {
            ClientUtil.showMessageWindow("IFSC Code Should not be Empty...!!!");
            return;
        } /*else if (txtBeneficiaryBankName1.getText().length() <= 0) {
            ClientUtil.showMessageWindow("Beneficiary Bank Name Should not be Empty...!!!");
            return;
        }*/ /*else if (txtBeneficiaryBranchName.getText().length() <= 0) {
            ClientUtil.showMessageWindow("Beneficiary Branch Name Should not be Empty...!!!");
            return;
        }*/  else if (txtBeneficiaryIFSC_Code.getText().length() <= 0) {
            ClientUtil.showMessageWindow("Beneficiary IFSC Code Should not be Empty...!!!");
            return;
        } else if (txtBeneficiaryName.getText().length() <= 0) {
            ClientUtil.showMessageWindow("Beneficiary Name Should not be Empty...!!!");
            return;
        } else if (CommonUtil.convertObjToInt(txtAmount.getText()) <= 0) {
            ClientUtil.showMessageWindow("Amount Should not be Empty/Zero  !!!");
            return;
        } else if (txtAccountNumber.getText().length() <= 0) {
            ClientUtil.showMessageWindow("Account Number Should not be Empty...!!!");
            return;
        }else if (txtRemarks.getText().length() <= 0) {
            ClientUtil.showMessageWindow("Favouring Details Should not be Empty...!!!");
            return;
        } else {
            String prodID = "";
            prodID = CommonUtil.convertObjToStr(cboProductId.getSelectedItem());
            //System.out.println("############ prodID : "+prodID);
            if(prodID.equals("ICICI_FT")){
                if(txtAccountNumber.getText().length()>12 || txtAccountNumber.getText().length()<12){
                    ClientUtil.showMessageWindow("Please Select the Product ID as NEFT ...!!!");//(Account number length is not 12 digits)
                    return;
                }
                if(txtBeneficiaryIFSC_Code.getText().equals("ICIC0000103") || txtBeneficiaryIFSC_Code.getText().equals("ICIC0000104")
                        || txtBeneficiaryIFSC_Code.getText().equals("ICIC0000106")){
                    ClientUtil.showMessageWindow("Please Select the Product ID as NEFT...!!!");// (IFSC Code was belongs to NEFT)
                    return;
                }
            }else if(prodID.equals("NEFT")){
                if (CommonUtil.convertObjToStr(lblBeneficiaryBankName.getText().toUpperCase()).equals("ICICI BANK")) {
                    if (!(txtBeneficiaryIFSC_Code.getText().equals("ICIC0000103") || txtBeneficiaryIFSC_Code.getText().equals("ICIC0000104")
                            || txtBeneficiaryIFSC_Code.getText().equals("ICIC0000106"))) {
                        ClientUtil.showMessageWindow("Please Select the IFSC Code ICIC0000103/ICIC0000104/ICIC0000106 !!!");
                        return;
                    }
                }
            }else if(prodID.equals("RTGS")){
                if(CommonUtil.convertObjToStr(lblBeneficiaryBankName.getText().toUpperCase()).equals("ICICI BANK")) {
                    ClientUtil.showMessageWindow("Please Select the Product ID as NEFT !!!");
                    return;
                }
            }else if(prodID.equals("IMPS")){
                //NO CODE VALIDATION
            }
            updateOBFields();
            double allAmount = 0.0; //Added By Suresh R   Ref By Mr Abi and Mr Srinath  06-Jul-2017
            double totalAmount = 0.0;
            allAmount = CommonUtil.convertObjToDouble(txtAmount.getText()) + CommonUtil.convertObjToDouble(txtExchangeCalc.getText())
                    + CommonUtil.convertObjToDouble(txtExchangeCollect.getText()) + CommonUtil.convertObjToDouble(txtServiceTax.getText())
                    + CommonUtil.convertObjToDouble(txtPostage.getText());
                    //+ CommonUtil.convertObjToDouble(txtgst.getText());
            totalAmount = CommonUtil.convertObjToDouble(txtTotalAmt.getText());
            if (allAmount > totalAmount || allAmount < totalAmount) {
                System.out.println("########### allAmount   : " + allAmount);
                System.out.println("########### totalAmount : " + totalAmount);
                ClientUtil.showMessageWindow("Total Amount is Wrong, Please Re-Enter the Amount !!!");
                return;
            }
            observable.addRTGSDetailsTable(updateTab, updateMode);
            tblIssue.setModel(observable.getTblIssueDetails());
            setTotalAmount();
            setSizeTableData();
            observable.resetRTGSDetails();
            resetRTGSDetails();
            ClientUtil.enableDisable(panDetails, false);
            btnEnableDisable(false);
            btnDisable(false);
            btnNewIssue.setEnabled(true);
            btnNewIssue.requestFocus();
        }
    }//GEN-LAST:event_btnSaveIssueActionPerformed
    private void setTotalAmount(){
        double totalAmount = 0.0;
        if(tblIssue.getRowCount()>0){
            for(int i=0; i<tblIssue.getRowCount(); i++){
                totalAmount+=CommonUtil.convertObjToDouble(tblIssue.getValueAt(i, 3));
            }
        }
        txtTotalAmount.setText(String.valueOf(totalAmount));
        transactionUI.cancelAction(false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.resetObjects();
        transactionUI.setCallingTransType("TRANSFER");
        transactionUI.setCallingAmount(txtTotalAmount.getText());
    }
    
    private void setTotalAmountEdit(){
        double totalAmount = 0.0;
        if(tblIssue.getRowCount()>0){
            for(int i=0; i<tblIssue.getRowCount(); i++){
                totalAmount+=CommonUtil.convertObjToDouble(tblIssue.getValueAt(i, 3));
            }
        }
        txtTotalAmount.setText(String.valueOf(totalAmount));
    }
    
    private void resetRTGSDetails(){
        //cboProductId.setSelectedItem("");
        //txtBeneficiaryBank.setText("");
        //txtBeneficiaryBankName1.setText("");
        //lblBeneficiaryBankName.setText("");
        txtBeneficiaryBranch.setText("");
        //txtBeneficiaryBranchName.setText("");
        lblBeneficiaryBranchName.setText("");
        txtBeneficiaryIFSC_Code.setText("");
        txtBeneficiaryName.setText("");
        txtAmount.setText("");
        txtAccountNumber.setText("");
        txtRemarks.setText("");
        txtExchangeCalc.setText("");
        txtExchangeCollect.setText("");
        txtServiceTax.setText("");
        txtgst.setText("");
        txtPostage.setText("");
        txtTotalAmt.setText("");
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            lblRTGS_IDVal.setText("");
        }
    }
            
    private void txtRemarksFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRemarksFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRemarksFocusLost

    private void btnBeneficiaryBranchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBeneficiaryBranchActionPerformed
        // TODO add your handling code here:
        popUp("BENEFICIARY_BRANCH");
    }//GEN-LAST:event_btnBeneficiaryBranchActionPerformed

    private void btnBeneficiaryBankActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBeneficiaryBankActionPerformed
        // TODO add your handling code here:
        popUp("BENEFICIARY_BANK");
    }//GEN-LAST:event_btnBeneficiaryBankActionPerformed

    private void txtExchangeCollectFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtExchangeCollectFocusLost
        // TODO add your handling code here:
        CalcTotalAmount();
    }//GEN-LAST:event_txtExchangeCollectFocusLost

    private void cboProductIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboProductIdFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_cboProductIdFocusLost

    private void txtExchangeCalcFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtExchangeCalcFocusLost
        // TODO add your handling code here:
        CalcTotalAmount();
    }//GEN-LAST:event_txtExchangeCalcFocusLost

    private void txtBeneficiaryIFSC_CodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBeneficiaryIFSC_CodeFocusLost
        // TODO add your handling code here:
        if (txtBeneficiaryIFSC_Code.getText().length() > 0) {
            String ifsc = "";//Added By Revathi.L
            ifsc = txtBeneficiaryIFSC_Code.getText();
            String ifscCode = ifsc.replaceAll("\\s+","");
            txtBeneficiaryIFSC_Code.setText(ifscCode);
            if (txtBeneficiaryBank.getText().length() > 0) {
                HashMap ifscMap = new HashMap();
                ifscMap.put("BANK_CODE", CommonUtil.convertObjToInt(txtBeneficiaryBank.getText()));
                ifscMap.put("IFSC_CODE", CommonUtil.convertObjToStr(txtBeneficiaryIFSC_Code.getText()));
                List lst = ClientUtil.executeQuery("getBranchDetailsForRTGS", ifscMap);
                if (lst != null && lst.size() > 0) {
                    ifscMap = (HashMap) lst.get(0);
                    txtBeneficiaryBranch.setText(CommonUtil.convertObjToStr(ifscMap.get("BRANCH_CODE")));
                    lblBeneficiaryBranchName.setText(CommonUtil.convertObjToStr(ifscMap.get("BRANCH_NAME")));
                } else {
                    txtBeneficiaryBranch.setText("");
                    lblBeneficiaryBranchName.setText("");
                    ClientUtil.displayAlert("Invalid Bank or Branch Details");
                    txtBeneficiaryIFSC_Code.setText("");
                    
                }
            } else {
                ClientUtil.displayAlert("Please Enter Beneficiary Bank Code !!! ");
                txtBeneficiaryIFSC_Code.setText("");
            }
        }
    }//GEN-LAST:event_txtBeneficiaryIFSC_CodeFocusLost

    private void txtBeneficiaryBankFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBeneficiaryBankFocusLost
        // TODO add your handling code here:
        if (txtBeneficiaryBank.getText().length() > 0) {
            HashMap bankMap = new HashMap();
            bankMap.put("BANK_CODE", CommonUtil.convertObjToStr(txtBeneficiaryBank.getText()));
            if (cboProductId.getSelectedIndex() > 0) {
                String prodID = CommonUtil.convertObjToStr(cboProductId.getSelectedItem());
                if (prodID.equals("ICICI_FT")) {
                    bankMap.put("ICICI_FT","ICICI_FT");
                }
            }
            List lst = ClientUtil.executeQuery("getRTGSBankDetails", bankMap);
            if (lst != null && lst.size() > 0) {
                bankMap = (HashMap) lst.get(0);
                lblBeneficiaryBankName.setText(CommonUtil.convertObjToStr(bankMap.get("LOOKUP_DESC")));
                txtBeneficiaryIFSC_Code.setText("");
                txtBeneficiaryBranch.setText("");
                lblBeneficiaryBranchName.setText("");
            } else {
                ClientUtil.displayAlert("Invalid Beneficiary Bank Code");
                txtBeneficiaryBank.setText("");
                lblBeneficiaryBankName.setText("");
                txtBeneficiaryIFSC_Code.setText("");
                txtBeneficiaryBranch.setText("");
                lblBeneficiaryBranchName.setText("");
            }
        }
    }//GEN-LAST:event_txtBeneficiaryBankFocusLost

    private void txtgstFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtgstFocusLost
        // TODO add your handling code here:
      //  CalcTotalAmount();
    }//GEN-LAST:event_txtgstFocusLost

    private void popUp(String currAction) {
        viewType = currAction;
        HashMap viewMap = new HashMap();
        if (currAction.equalsIgnoreCase("Edit") || currAction.equalsIgnoreCase("Enquiry") || currAction.equalsIgnoreCase("Delete")) {
            HashMap map = new HashMap();
            map.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getRTGSEnquiry");
        } else if (currAction.equals("BENEFICIARY_BANK")) {
            HashMap map = new HashMap();
            map.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            if (cboProductId.getSelectedIndex() > 0) {
                String prodID = CommonUtil.convertObjToStr(cboProductId.getSelectedItem());
                if (prodID.equals("ICICI_FT")) {
                    map.put("ICICI_FT","ICICI_FT");
                }
            }
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getRTGSBankDetails");
        } else if (currAction.equals("BENEFICIARY_BRANCH")) {
            HashMap where = new HashMap();
            where.put("BANK_CODE", txtBeneficiaryBank.getText());
            viewMap.put(CommonConstants.MAP_NAME, "getSelectOther_Bank_Branch");
            viewMap.put(CommonConstants.MAP_WHERE, where);
            where = null;
        }
        new ViewAll(this, viewMap).show();
    }

    public void fillData(Object param) {
        isFilled = true;
        final HashMap hash = (HashMap) param;
        System.out.println("calling filldata#####" + hash);
        if (hash.containsKey("CUST_ID")) {
            hash.put("CUSTOMER ID", hash.get("CUST_ID"));
        }
        if (viewType == "BENEFICIARY_BANK") {
            txtBeneficiaryBank.setText(CommonUtil.convertObjToStr(hash.get("LOOKUP_REF_ID")));
            //txtBeneficiaryBankName1.setText(CommonUtil.convertObjToStr(hash.get("BANK_NAME")));
            lblBeneficiaryBankName.setText(CommonUtil.convertObjToStr(hash.get("LOOKUP_DESC")));
            txtBeneficiaryIFSC_Code.setText("");
            txtBeneficiaryBranch.setText("");
            lblBeneficiaryBranchName.setText("");
        } else if (viewType == "BENEFICIARY_BRANCH") {
            txtBeneficiaryBranch.setText(CommonUtil.convertObjToStr(hash.get("BRANCH_CODE")));
            //txtBeneficiaryBranchName.setText(CommonUtil.convertObjToStr(hash.get("NAME")));
            lblBeneficiaryBranchName.setText(CommonUtil.convertObjToStr(hash.get("NAME")));
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT
                || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
            this.setButtonEnableDisable();
            lblRTGS_ID.setVisible(true);
            lblRTGS_IDVal.setVisible(true);
            lblRTGS_IDVal.setText(CommonUtil.convertObjToStr(hash.get("RTGS_ID")));
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                cboProductId.setSelectedItem(CommonUtil.convertObjToStr(hash.get("PROD_ID")));
            }
            observable.getData(hash);
            tblIssue.setModel(observable.getTblIssueDetails());
            setSizeTableData();
            
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                insertScreenLock(lblRTGS_IDVal.getText(), "EDIT");
            }
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
                insertScreenLock(lblRTGS_IDVal.getText(), "DELETE");
            }
            if (validateScreenLock(lblRTGS_IDVal.getText())) {
                btnSave.setEnabled(false);
                btnAuthorize.setEnabled(false);
                btnReject.setEnabled(false);
                btnCancelActionPerformed(null);
            }
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT && lblRTGS_IDVal.getText().length()>0) {
                if (editLockValidationMessage(lblRTGS_IDVal.getText(), getScreenID())) {
                    btnAuthorize.setEnabled(false);
                    btnSave.setEnabled(false);
                    btnCancelActionPerformed(null);
                }
            }
            /*if (observable.getProxyReturnMap() != null && observable.getProxyReturnMap().size() > 0) {
                if (observable.getProxyReturnMap().containsKey("TRANSFER_TRANS_LIST")) {
                    displayTransDetails(CommonUtil.convertObjToStr(hash.get("RTGS_ID")));
                    observable.setProxyReturnMap(null);
                }
            }*/
            setTotalAmountEdit();
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
                || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION
                || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
            this.setButtonEnableDisable();
            lblRTGS_ID.setVisible(true);
            lblRTGS_IDVal.setVisible(true);
            lblRTGS_IDVal.setText(CommonUtil.convertObjToStr(hash.get("RTGS_ID")));
            observable.getData(hash);
            tblIssue.setModel(observable.getTblIssueDetails());
            setSizeTableData();
            /*if (observable.getProxyReturnMap() != null && observable.getProxyReturnMap().size() > 0) {
                if (observable.getProxyReturnMap().containsKey("TRANSFER_TRANS_LIST")) {
                    displayTransDetails(CommonUtil.convertObjToStr(hash.get("RTGS_ID")));
                    observable.setProxyReturnMap(null);
                }
            }*/
            setTotalAmountEdit();
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE) {
                insertScreenLock(lblRTGS_IDVal.getText(), "AUTHORIZE");
            }
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
                insertScreenLock(lblRTGS_IDVal.getText(), "REJECT");
            }
            if (validateScreenLock(lblRTGS_IDVal.getText())) {
                btnSave.setEnabled(false);
                btnAuthorize.setEnabled(false);
                btnReject.setEnabled(false);
                btnCancelActionPerformed(null);
            }
        }
    }

    private void setObservable() throws Exception {
        observable = new RtgsRemittanceOB();
        observable.addObserver(this);
    }

    public RtgsRemittanceOB getRemittanceIssueOB() {
        return observable;
    }

    private void initComponentData() {
        cboProductId.setModel(observable.getCbmProductId());
//        cboTransmissionType.setModel(observable.getCbmTransmissionType());
        tblIssue.setModel(observable.getTblIssueDetails());
    }

    /* Checking for mandatory fields */
    private String checkMandatory(javax.swing.JComponent component) {
        return new MandatoryCheck().checkMandatory(getClass().getName(), component);
    }

    /* To display an alert message if any of the mandatory fields is not inputed */
    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    /* To enable or disable the main New Save Delete buttons  */

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

        btnAuthorize.setEnabled(!btnNew.isEnabled());
        btnReject.setEnabled(!btnNew.isEnabled());
        btnException.setEnabled(!btnNew.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
        lblStatus.setText(observable.getLblStatus());
    }

    /* Auto Generated Method - setFieldNames()
     This method assigns name for all the components.
     Other functions are working based on this name. */
    private void setFieldNames() {
    }

    /* Auto Generated Method - internationalize()
     This method used to assign display texts from
     the Resource Bundle File. */
    private void internationalize() {
    }
    /* Auto Generated Method - setMandatoryHashMap()
     This method list out all the Input Fields available in the UI.
     It needs a class level HashMap variable mandatoryMap. */

    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();

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
        tblIssue.setModel(observable.getTblIssueDetails());
    }

    /* Auto Generated Method - updateOBFields()
     This method called by Save option of UI.
     It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setCboProductId((String) cboProductId.getSelectedItem());
        observable.setIFSC_Code(lblIFSC_CodeVal.getText());
//        observable.setCboTransmissionType((String) cboTransmissionType.getSelectedItem());
        observable.setTxtBeneficiaryBank(txtBeneficiaryBank.getText());
        //observable.setTxtBeneficiaryBankName(txtBeneficiaryBankName1.getText());
        observable.setTxtBeneficiaryBankName(lblBeneficiaryBankName.getText());        
        observable.setTxtBeneficiaryBranch(txtBeneficiaryBranch.getText());
        //observable.setTxtBeneficiaryBranchName(txtBeneficiaryBranchName.getText());
        observable.setTxtBeneficiaryBranchName(lblBeneficiaryBranchName.getText());
        observable.setTxtBeneficiaryIFSC_Code(txtBeneficiaryIFSC_Code.getText());
        observable.setTxtBeneficiaryName(txtBeneficiaryName.getText());
        observable.setTxtAmt(txtAmount.getText());
        observable.setTxtAccountNumber(txtAccountNumber.getText());
        observable.setTxtRemarks(txtRemarks.getText());
        observable.setTxtExchangeCalc(txtExchangeCalc.getText());
        observable.setTxtExchangeCollect(txtExchangeCollect.getText());
        observable.setTxtServiceTax(txtServiceTax.getText());
        observable.setTxtgst(txtgst.getText());
        observable.setTxtCharges(txtPostage.getText());
        observable.setTxtTotalAmt(txtTotalAmt.getText());
        observable.setRTGS_ID(lblRTGS_IDVal.getText());
        observable.setModule(getModule());
        observable.setScreen(getScreen());
    }

    /**
     * To enable disable table Issue details
     */
    private void setTableIssueEnableDisable(boolean flag) {
        tblIssue.setEnabled(flag);
    }

    private String isPanMandatory() {
        MandatoryCheck objMandatory = new MandatoryCheck();
        RtgsRemittanceHashMap objMap = new RtgsRemittanceHashMap();
        HashMap mandatoryMap = objMap.getMandatoryHashMap();
        mandatoryMap.put("txtPANGIRNo", new Boolean(false));
        HashMap tempMap = new HashMap();
//        tempMap.put(CommonConstants.PRODUCT_ID, CommonUtil.convertObjToStr(cboProductId.getSelectedItem()));
        tempMap.put(CommonConstants.PRODUCT_ID, (String) ((ComboBoxModel) cboProductId.getModel()).getKeyForSelected());
        List outList = ClientUtil.executeQuery("getPanValidationAmt", tempMap);
        tempMap = null;
        if (outList != null && outList.size() > 0) {
            if (((Double) outList.get(0)).doubleValue() < CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue()) {
                mandatoryMap.put("txtPANGIRNo", new Boolean(true));
            }
        }
        outList = null;
        String mandatoryMessage = objMandatory.checkMandatory(getClass().getName(), panLeftSubIssue, mandatoryMap);
        return mandatoryMessage;
    }

    /* To set Maximum length and for validation */
    private void setMaximumLength() {
        txtRemarks.setMaxLength(128);
        txtRemarks.setAllowAll(true);
        txtAdviceNo.setAllowAll(true);
        txtBeneficiaryBank.setAllowAll(true);
        txtAccountNumber.setMaxLength(25);
       // txtAccountNumber.setAllowNumber(true);
        txtBeneficiaryName.setAllowAll(true);
        txtBeneficiaryName.setMaxLength(50);
        //txtBeneficiaryBankName1.setAllowAll(true);
        txtBeneficiaryIFSC_Code.setAllowAll(true);
        //txtBeneficiaryBranchName.setAllowAll(true);
        txtAmount.setValidation(new CurrencyValidation(14, 2));
        txtPostage.setValidation(new CurrencyValidation(14, 0));
        txtTotalAmt.setValidation(new CurrencyValidation(14, 0));
        txtServiceTax.setValidation(new CurrencyValidation(14, 0));
        txtgst.setValidation(new CurrencyValidation(14, 0));
        txtAccountNumber.setAllowAll(true);
        //txtAccountNumber.setValidation(new NumericValidation(25,0));
        txtExchangeCalc.setValidation(new CurrencyValidation(14, 0));
        txtExchangeCollect.setValidation(new CurrencyValidation(14, 0));
        txtTotalAmount.setValidation(new CurrencyValidation(14, 2));
        txtBeneficiaryIFSC_Code.setMaxLength(11);//Added By Revathi.L
    }

    /* Auto Generated Method - setHelpMessage()
     This method shows tooltip help for all the input fields
     available in the UI. It needs the Mandatory Resource Bundle
     object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        RtgsRemittanceMRB objMandatoryRB = new RtgsRemittanceMRB();

    }

    /**
     * If Transaction Type is transfer give alert
     */
    private void transferValidation() {
        RtgsRemittanceMRB objMRB = new RtgsRemittanceMRB();
        StringBuffer strB = new StringBuffer();
        strB = null;
        objMRB = null;
    }

    private void setValuesFor(String productId, String category, String bankCode, String branchCode, String amount) {
        if (productId != null) {
            this.productId = CommonUtil.convertObjToStr((((ComboBoxModel) (cboProductId).getModel())).getKeyForSelected());
        }

        if (bankCode != null) {
            this.bankCode = txtBeneficiaryBank.getText();
        }
        if (branchCode != null) {
            this.branchCode = txtBeneficiaryBranch.getText();
        }
        if (amount != null) {
            this.amount = CommonUtil.convertObjToStr(txtAmount.getText());
        }
    }

    public static void main(String args[]) {
        javax.swing.JFrame f = new javax.swing.JFrame();
        RtgsRemittanceUI ch = new RtgsRemittanceUI();
        f.getContentPane().add(ch);
        ch.show();
        ch.setSize(700, 500);
        f.show();
        f.setSize(700, 500);
        //        System.out.println("char  " + (int)'z');
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnBeneficiaryBank;
    private com.see.truetransact.uicomponent.CButton btnBeneficiaryBranch;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDeleteIssue;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnNewIssue;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnSaveIssue;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboProductId;
    private com.see.truetransact.uicomponent.CLabel lbSpace2;
    private com.see.truetransact.uicomponent.CLabel lblAccountNumber;
    private com.see.truetransact.uicomponent.CLabel lblAdviceNo;
    private com.see.truetransact.uicomponent.CLabel lblAmount;
    private com.see.truetransact.uicomponent.CLabel lblBeneficiaryBank;
    private com.see.truetransact.uicomponent.CLabel lblBeneficiaryBankName;
    private com.see.truetransact.uicomponent.CLabel lblBeneficiaryBranch;
    private com.see.truetransact.uicomponent.CLabel lblBeneficiaryBranchName;
    private com.see.truetransact.uicomponent.CLabel lblBeneficiaryIFSC_Code;
    private com.see.truetransact.uicomponent.CLabel lblBeneficiaryName;
    private com.see.truetransact.uicomponent.CLabel lblCharges;
    private com.see.truetransact.uicomponent.CLabel lblExchange;
    private com.see.truetransact.uicomponent.CLabel lblExchangeCalc;
    private com.see.truetransact.uicomponent.CLabel lblGST;
    private com.see.truetransact.uicomponent.CLabel lblIFSC_Code;
    private javax.swing.JLabel lblIFSC_CodeVal;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblProductId;
    private com.see.truetransact.uicomponent.CLabel lblRTGS_ID;
    private com.see.truetransact.uicomponent.CLabel lblRTGS_IDVal;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
    private com.see.truetransact.uicomponent.CLabel lblServiceTax;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace17;
    private com.see.truetransact.uicomponent.CLabel lblSpace18;
    private com.see.truetransact.uicomponent.CLabel lblSpace19;
    private com.see.truetransact.uicomponent.CLabel lblSpace20;
    private com.see.truetransact.uicomponent.CLabel lblSpace21;
    private com.see.truetransact.uicomponent.CLabel lblSpace22;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace6;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTotalAmount;
    private com.see.truetransact.uicomponent.CLabel lblTotalAmt;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panDetails;
    private com.see.truetransact.uicomponent.CPanel panIssue;
    private com.see.truetransact.uicomponent.CPanel panLeftSubIssue;
    private com.see.truetransact.uicomponent.CPanel panRemarks;
    private com.see.truetransact.uicomponent.CPanel panRightSubIssue;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTableIssue;
    private com.see.truetransact.uicomponent.CPanel panTotalAmount;
    private com.see.truetransact.uicomponent.CPanel panTransaction;
    private com.see.truetransact.uicomponent.CPanel panbtnIssue;
    private javax.swing.JSeparator sptCancel;
    private com.see.truetransact.uicomponent.CSeparator sptIssue;
    private javax.swing.JSeparator sptView;
    private com.see.truetransact.uicomponent.CScrollPane srpIssue;
    private com.see.truetransact.uicomponent.CTable tblIssue;
    private com.see.truetransact.uicomponent.CToolBar tbrRemittanceIssue;
    private com.see.truetransact.uicomponent.CTextField txtAccountNumber;
    private com.see.truetransact.uicomponent.CTextField txtAdviceNo;
    private com.see.truetransact.uicomponent.CTextField txtAmount;
    private com.see.truetransact.uicomponent.CTextField txtBeneficiaryBank;
    private com.see.truetransact.uicomponent.CTextField txtBeneficiaryBranch;
    private com.see.truetransact.uicomponent.CTextField txtBeneficiaryIFSC_Code;
    private com.see.truetransact.uicomponent.CTextField txtBeneficiaryName;
    private com.see.truetransact.uicomponent.CTextField txtExchangeCalc;
    private com.see.truetransact.uicomponent.CTextField txtExchangeCollect;
    private com.see.truetransact.uicomponent.CTextField txtPostage;
    private com.see.truetransact.uicomponent.CTextField txtRemarks;
    private com.see.truetransact.uicomponent.CTextField txtServiceTax;
    private com.see.truetransact.uicomponent.CTextField txtTotalAmount;
    private com.see.truetransact.uicomponent.CTextField txtTotalAmt;
    private com.see.truetransact.uicomponent.CTextField txtgst;
    // End of variables declaration//GEN-END:variables
}