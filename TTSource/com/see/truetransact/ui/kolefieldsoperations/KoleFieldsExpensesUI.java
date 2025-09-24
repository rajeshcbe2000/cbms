/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * EmptransferUI.java
 *
 * Created on feb 9, 2009, 10:53 AM
 */

package com.see.truetransact.ui.kolefieldsoperations;

import com.see.truetransact.ui.directorboardmeeting.*;
import java.util.HashMap;
import java.util.Observable;
import org.apache.log4j.Logger;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import java.util.ArrayList;
import java.util.List;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.ui.common.transaction.TransactionUI;
import com.see.truetransact.ui.common.authorize.AuthorizeUI ;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.customer.customeridchange.CustomerIdChangeOB;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.common.viewall.AuthorizeListUI;
import com.see.truetransact.ui.common.viewall.NewAuthorizeListUI;
import java.util.Date;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uimandatory.MandatoryDBCheck;
import com.see.truetransact.uicomponent.CTextField;
import com.see.truetransact.uivalidation.*;
import com.see.truetransact.uicomponent.CButtonGroup;
import com.see.truetransact.ui.directorboardmeeting.DirectorBoardRB;
import com.see.truetransact.uicomponent.COptionPane;
import java.util.Date;
import java.util.LinkedHashMap;
import javax.swing.table.DefaultTableModel;



/** This form is used to manipulate CustomerIdChangeUI related functionality
 * @author swaroop
 */
public class KoleFieldsExpensesUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer, UIMandatoryField{

    private String viewType = new String();
    private HashMap mandatoryMap;
    private KoleFieldsExpensesOB observable;
   // DirectorBoardOB ob;
    private final int AUTHORIZESTAT=8, CANCEL=0;
    final String AUTHORIZE="Authorize";
   private final static Logger log = Logger.getLogger(DirectorBoardUI.class);
    DirectorBoardRB DirectorBoardRB = new DirectorBoardRB();
   // private String prodType="";
    java.util.ResourceBundle resourceBundle;
    DirectorBoardMRB objMandatoryRB=new DirectorBoardMRB();
    String txtBoard_mt_id=null;
    String board_mt_id=null;
    String koleFiledExpensId = "";
    private Date currDt = null;
    private TransactionUI transactionUI = new TransactionUI(); 
    boolean fromAuthorizeUI = false;    
    AuthorizeListUI authorizeListUI = null;
    NewAuthorizeListUI newauthorizeListUI = null;
    boolean fromNewAuthorizeUI = false;
    /** Creates new form CustomerIdChangeUI */
    public KoleFieldsExpensesUI() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        initStartUp();
    }
    
    private void initStartUp(){
        setMandatoryHashMap();
        setFieldNames();
        internationalize();
        observable = new KoleFieldsExpensesOB();
        observable.addObserver(this);
        observable.setTransactionOB(transactionUI.getTransactionOB());
        initComponentData();
        enableDisable(false);
        setButtonEnableDisable();
        setMaxLength();
       //  objMandatoryRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.directorboardmeeting.DirectorBoardMRB", ProxyParameters.LANGUAGE);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panTranferDetails);
 
        tdtLastIntCalcDt.setEnabled(false);
    }
    
    private void setMaxLength() {
//           txtEmpID.setMaxLength(64);
//           txtEmpID.setValidation(new CurrencyValidation());
//         txtAccountNumber.setMaxLength(16);
//         txtAccountNumber.setAllowAll(true);
//         txtOldCustID.setAllowAll(true);
//         txtNewCustId.setAllowAll(true);
        txtIntAmount.setValidation(new CurrencyValidation(13, 2));
        txtTransAmount.setValidation(new CurrencyValidation(13, 2));
    }
    
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboBoardMember", new Boolean(true));
        mandatoryMap.put("tdtMeetingDate", new Boolean(true));
        mandatoryMap.put("txtSittingFeeAmount", new Boolean(true));
        mandatoryMap.put("tdtPaidDate", new Boolean(true));
        mandatoryMap.put("rdoYes1", new Boolean(false));
        mandatoryMap.put("rdoNo1", new Boolean(false));
        mandatoryMap.put("rdoYes2", new Boolean(false));
        mandatoryMap.put("rdoNo2", new Boolean(false));
    }
    
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    
    
    /****************** NEW METHODS *****************/
    
//    private void updateAuthorizeStatus(String authorizeStatus) {
//        if (viewType != AUTHORIZE){
//            HashMap mapParam = new HashMap();
//            HashMap whereMap = new HashMap();
//            whereMap.put("USER_ID", TrueTransactMain.USER_ID);
//            whereMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
//            whereMap.put("AUTHORIZE_MODE","AUTHORIZE_MODE");
//            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
//            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
//            mapParam.put(CommonConstants.MAP_NAME, "getEmpTransferDetailsAuthorize");
//            viewType = AUTHORIZE;
//            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
//            //            isFilled = false;
//            authorizeUI.show();
//            btnSave.setEnabled(false);
//            observable.setStatus();
//            lblStatus.setText(observable.getLblStatus());
//        } else if (viewType == AUTHORIZE){
//            ArrayList arrList = new ArrayList();
//            HashMap authorizeMap = new HashMap();
//            HashMap singleAuthorizeMap = new HashMap();
//            singleAuthorizeMap.put("STATUS", authorizeStatus);
//            singleAuthorizeMap.put("EMP_TRANSFER_ID", observable.getTxtEmpTransferID());
//            singleAuthorizeMap.put("AUTH_BY", TrueTransactMain.USER_ID);
//            singleAuthorizeMap.put("AUTH_DT",ClientUtil.getCurrentDateWithTime());
//            singleAuthorizeMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
//            String presentBranch = ((ComboBoxModel)cboTransBran.getModel()).getKeyForSelected().toString();
//            if (presentBranch.lastIndexOf("-")!=-1)
//            presentBranch = presentBranch.substring(0,presentBranch.lastIndexOf("-"));
//            presentBranch= presentBranch.trim();
//            singleAuthorizeMap.put("PRESENT_BRANCH_CODE",presentBranch);
//            singleAuthorizeMap.put("EMP_ID", observable.getTxtEmpID());
//            arrList.add(singleAuthorizeMap);         
//            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
//            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
//            authorize(authorizeMap,observable.getTxtEmpTransferID());
//            viewType = "";
//            super.setOpenForEditBy(observable.getStatusBy());
//        }
//    }
    
//    public void authorize(HashMap map,String id) {
//        System.out.println("Authorize Map : " + map);
//        
//        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
//            observable.set_authorizeMap(map);
//            observable.doAction();
//            if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED){
//                   super.setOpenForEditBy(observable.getStatusBy());
//                   super.removeEditLock(id);
//             }
//            btnCancelActionPerformed(null);
//            observable.setStatus();
//            observable.setResultStatus();
//            lblStatus.setText(observable.getLblStatus());
//            
//        }
//    }
//    public void authorize(HashMap map) {
//        System.out.println("Authorize Map : " + map);
//        
//        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
//            observable.setResult(ClientConstants.ACTIONTYPE_AUTHORIZE);
//            observable.set_authorizeMap(map);
//            observable.doAction();
//            btnCancelActionPerformed(null);
//            observable.setStatus();
//            observable.setResultStatus();
//            lblStatus.setText(observable.getLblStatus());
//            
//        }
//    }
//    public void setHelpMessage() {
//    }
    
    /************ END OF NEW METHODS ***************/
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoApplType = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoApplType1 = new com.see.truetransact.uicomponent.CButtonGroup();
        panEmpTransfer = new com.see.truetransact.uicomponent.CPanel();
        koleFieldExpenses = new com.see.truetransact.uicomponent.CTabbedPane();
        panTranferDetails = new com.see.truetransact.uicomponent.CPanel();
        panTransaction = new com.see.truetransact.uicomponent.CPanel();
        lblAccountDetails = new com.see.truetransact.uicomponent.CPanel();
        lblBoardMember = new com.see.truetransact.uicomponent.CLabel();
        cboSuspenseProduct = new com.see.truetransact.uicomponent.CComboBox();
        btnMembershipNo = new com.see.truetransact.uicomponent.CButton();
        lblAccountNo = new com.see.truetransact.uicomponent.CLabel();
        txtAccountNo = new com.see.truetransact.uicomponent.CTextField();
        lblCustomerName = new com.see.truetransact.uicomponent.CLabel();
        lblTransType = new javax.swing.JLabel();
        lblIntAmount = new com.see.truetransact.uicomponent.CLabel();
        lblLastIntCalcDt = new com.see.truetransact.uicomponent.CLabel();
        tdtLastIntCalcDt = new com.see.truetransact.uicomponent.CDateField();
        txtIntAmount = new com.see.truetransact.uicomponent.CTextField();
        lblTransAmount = new com.see.truetransact.uicomponent.CLabel();
        txtTransAmount = new com.see.truetransact.uicomponent.CTextField();
        srpTransDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblTransDetails = new com.see.truetransact.uicomponent.CTable();
        lblBalance = new com.see.truetransact.uicomponent.CLabel();
        txtAccountBalance = new com.see.truetransact.uicomponent.CTextField();
        rdoReceipt = new com.see.truetransact.uicomponent.CRadioButton();
        rdoPayment = new com.see.truetransact.uicomponent.CRadioButton();
        tbrOperativeAcctProduct = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace62 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace63 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace64 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace65 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace66 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace67 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        lblSpace68 = new com.see.truetransact.uicomponent.CLabel();
        btnDateChange = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
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
        setMaximumSize(new java.awt.Dimension(900, 600));
        setMinimumSize(new java.awt.Dimension(900, 600));
        setPreferredSize(new java.awt.Dimension(900, 600));

        panEmpTransfer.setMaximumSize(new java.awt.Dimension(650, 455));
        panEmpTransfer.setMinimumSize(new java.awt.Dimension(650, 455));
        panEmpTransfer.setPreferredSize(new java.awt.Dimension(650, 455));
        panEmpTransfer.setLayout(new java.awt.GridBagLayout());

        panTranferDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panTranferDetails.setMinimumSize(new java.awt.Dimension(850, 445));
        panTranferDetails.setName("panMaritalStatus"); // NOI18N
        panTranferDetails.setOpaque(false);
        panTranferDetails.setPreferredSize(new java.awt.Dimension(850, 445));

        panTransaction.setMinimumSize(new java.awt.Dimension(830, 250));
        panTransaction.setPreferredSize(new java.awt.Dimension(830, 250));

        lblAccountDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Account Details"));
        lblAccountDetails.setMinimumSize(new java.awt.Dimension(523, 300));
        lblAccountDetails.setPreferredSize(new java.awt.Dimension(523, 300));

        lblBoardMember.setText("Product Name");
        lblBoardMember.setMaximumSize(new java.awt.Dimension(100, 21));
        lblBoardMember.setMinimumSize(new java.awt.Dimension(100, 21));
        lblBoardMember.setPreferredSize(new java.awt.Dimension(100, 21));

        cboSuspenseProduct.setMaximumSize(new java.awt.Dimension(100, 21));
        cboSuspenseProduct.setMinimumSize(new java.awt.Dimension(100, 21));
        cboSuspenseProduct.setNextFocusableComponent(btnMembershipNo);
        cboSuspenseProduct.setPopupWidth(225);
        cboSuspenseProduct.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboSuspenseProductFocusLost(evt);
            }
        });

        btnMembershipNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnMembershipNo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnMembershipNo.setNextFocusableComponent(txtAccountNo);
        btnMembershipNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnMembershipNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMembershipNoActionPerformed(evt);
            }
        });

        lblAccountNo.setText("Account No.");
        lblAccountNo.setMaximumSize(new java.awt.Dimension(100, 21));
        lblAccountNo.setMinimumSize(new java.awt.Dimension(100, 21));
        lblAccountNo.setPreferredSize(new java.awt.Dimension(100, 21));

        txtAccountNo.setMinimumSize(new java.awt.Dimension(175, 21));
        txtAccountNo.setPreferredSize(new java.awt.Dimension(175, 21));
        txtAccountNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAccountNoActionPerformed(evt);
            }
        });

        lblCustomerName.setForeground(new java.awt.Color(51, 0, 255));
        lblCustomerName.setText("Customer Name");

        lblTransType.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        lblTransType.setText("Transaction Type");
        lblTransType.setMaximumSize(new java.awt.Dimension(100, 21));
        lblTransType.setMinimumSize(new java.awt.Dimension(100, 21));
        lblTransType.setPreferredSize(new java.awt.Dimension(100, 21));

        lblIntAmount.setText("Interest");
        lblIntAmount.setMaximumSize(new java.awt.Dimension(112, 21));
        lblIntAmount.setMinimumSize(new java.awt.Dimension(112, 21));
        lblIntAmount.setPreferredSize(new java.awt.Dimension(112, 21));

        lblLastIntCalcDt.setText("Last Int Calc Dt");
        lblLastIntCalcDt.setMaximumSize(new java.awt.Dimension(100, 21));
        lblLastIntCalcDt.setMinimumSize(new java.awt.Dimension(100, 21));
        lblLastIntCalcDt.setPreferredSize(new java.awt.Dimension(100, 21));

        tdtLastIntCalcDt.setMaximumSize(new java.awt.Dimension(101, 24));
        tdtLastIntCalcDt.setMinimumSize(new java.awt.Dimension(101, 24));
        tdtLastIntCalcDt.setName("tdtDateOfBirth"); // NOI18N
        tdtLastIntCalcDt.setPreferredSize(new java.awt.Dimension(101, 24));
        tdtLastIntCalcDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtLastIntCalcDtFocusLost(evt);
            }
        });

        txtIntAmount.setAllowAll(true);
        txtIntAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtIntAmountFocusLost(evt);
            }
        });

        lblTransAmount.setText("Amount");

        txtTransAmount.setAllowAll(true);
        txtTransAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTransAmountFocusLost(evt);
            }
        });

        tblTransDetails.setModel(new javax.swing.table.DefaultTableModel(
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
        srpTransDetails.setViewportView(tblTransDetails);

        lblBalance.setText("Account Balance");

        txtAccountBalance.setEditable(false);
        txtAccountBalance.setAllowAll(true);

        rdoReceipt.setText("Receipt");
        rdoReceipt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoReceiptActionPerformed(evt);
            }
        });

        rdoPayment.setText("Payment");
        rdoPayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoPaymentActionPerformed(evt);
            }
        });
        rdoPayment.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                rdoPaymentFocusLost(evt);
            }
        });

        javax.swing.GroupLayout lblAccountDetailsLayout = new javax.swing.GroupLayout(lblAccountDetails);
        lblAccountDetails.setLayout(lblAccountDetailsLayout);
        lblAccountDetailsLayout.setHorizontalGroup(
            lblAccountDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lblAccountDetailsLayout.createSequentialGroup()
                .addGap(68, 68, 68)
                .addGroup(lblAccountDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(lblAccountDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, lblAccountDetailsLayout.createSequentialGroup()
                            .addComponent(lblTransAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(246, 246, 246))
                        .addGroup(lblAccountDetailsLayout.createSequentialGroup()
                            .addComponent(lblIntAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(237, 237, 237))
                        .addGroup(lblAccountDetailsLayout.createSequentialGroup()
                            .addComponent(lblTransType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)))
                    .addGroup(lblAccountDetailsLayout.createSequentialGroup()
                        .addGroup(lblAccountDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lblCustomerName, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(lblAccountDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(lblAccountDetailsLayout.createSequentialGroup()
                                    .addGroup(lblAccountDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblAccountNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblBoardMember, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(lblAccountDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txtAccountNo, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                                        .addComponent(cboSuspenseProduct, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGroup(lblAccountDetailsLayout.createSequentialGroup()
                                    .addComponent(lblLastIntCalcDt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(23, 23, 23)
                                    .addGroup(lblAccountDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtIntAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(tdtLastIntCalcDt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtTransAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(lblAccountDetailsLayout.createSequentialGroup()
                                            .addComponent(rdoReceipt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(8, 8, 8)
                                            .addComponent(rdoPayment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnMembershipNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(lblAccountDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(srpTransDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(lblAccountDetailsLayout.createSequentialGroup()
                        .addComponent(lblBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtAccountBalance, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        lblAccountDetailsLayout.setVerticalGroup(
            lblAccountDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lblAccountDetailsLayout.createSequentialGroup()
                .addGroup(lblAccountDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(lblAccountDetailsLayout.createSequentialGroup()
                        .addGroup(lblAccountDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblBoardMember, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboSuspenseProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(6, 6, 6)
                        .addGroup(lblAccountDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(lblAccountDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblAccountNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtAccountNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnMembershipNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblCustomerName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(lblAccountDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblLastIntCalcDt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tdtLastIntCalcDt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(3, 3, 3)
                        .addGroup(lblAccountDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(lblAccountDetailsLayout.createSequentialGroup()
                                .addGap(61, 61, 61)
                                .addComponent(lblTransAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(lblAccountDetailsLayout.createSequentialGroup()
                                .addGroup(lblAccountDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblTransType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(rdoReceipt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(rdoPayment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(2, 2, 2)
                                .addGroup(lblAccountDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtIntAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblIntAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTransAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(lblAccountDetailsLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(srpTransDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(lblAccountDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblBalance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtAccountBalance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panTranferDetailsLayout = new javax.swing.GroupLayout(panTranferDetails);
        panTranferDetails.setLayout(panTranferDetailsLayout);
        panTranferDetailsLayout.setHorizontalGroup(
            panTranferDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panTranferDetailsLayout.createSequentialGroup()
                .addGroup(panTranferDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(lblAccountDetails, javax.swing.GroupLayout.DEFAULT_SIZE, 830, Short.MAX_VALUE)
                    .addComponent(panTransaction, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panTranferDetailsLayout.setVerticalGroup(
            panTranferDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panTranferDetailsLayout.createSequentialGroup()
                .addComponent(lblAccountDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panTransaction, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        koleFieldExpenses.addTab("Kole Field Expenses", panTranferDetails);

        panEmpTransfer.add(koleFieldExpenses, new java.awt.GridBagConstraints());
        koleFieldExpenses.getAccessibleContext().setAccessibleName("Kole Fields Expenses");

        getContentPane().add(panEmpTransfer, java.awt.BorderLayout.CENTER);

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
        btnNew.setNextFocusableComponent(cboSuspenseProduct);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnNew);

        lblSpace62.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace62.setText("     ");
        lblSpace62.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace62.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace62.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace62);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnEdit);

        lblSpace63.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace63.setText("     ");
        lblSpace63.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace63.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace63.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace63);

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

        lblSpace64.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace64.setText("     ");
        lblSpace64.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace64.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace64.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace64);

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

        lblSpace65.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace65.setText("     ");
        lblSpace65.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace65.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace65.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace65);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnException);

        lblSpace66.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace66.setText("     ");
        lblSpace66.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace66.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace66.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace66);

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

        lblSpace67.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace67.setText("     ");
        lblSpace67.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace67.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace67.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace67);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnClose);

        lblSpace68.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace68.setText("     ");
        lblSpace68.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace68.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace68.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace68);

        btnDateChange.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/denomination.gif"))); // NOI18N
        btnDateChange.setToolTipText("Exception");
        btnDateChange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDateChangeActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnDateChange);

        getContentPane().add(tbrOperativeAcctProduct, java.awt.BorderLayout.NORTH);

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
        
    private void btnDateChangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDateChangeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDateChangeActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        btnView.setEnabled(false);
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
    //    popUp("Enquiry");
    }//GEN-LAST:event_btnViewActionPerformed
                
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
      //  updateAuthorizeStatus(CommonConstants.STATUS_EXCEPTION);
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
        btnCancel.setEnabled(true);
        btnException.setEnabled(true);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
       observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
  //      updateAuthorizeStatus(CommonConstants.STATUS_REJECTED);
         btnCancel.setEnabled(true);
        btnReject.setEnabled(true);
         btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
   //     updateAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    
    public void authorizeStatus(String authorizeStatus) {
        panTransaction.add(transactionUI);
        transactionUI.setSourceScreen("KOLEFIELDEXPENSE");
        transactionUI.setParantUI(this);
        if (!viewType.equals(AUTHORIZE)){
            viewType = AUTHORIZE;
            //            setModified(true);
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID,TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID,TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            whereMap.put("TRANS_DT",currDt.clone());
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            System.out.println("mapParam1111>>>>>>>"+mapParam);
            whereMap = null;
            mapParam.put(CommonConstants.MAP_NAME, "getKoleFieldExpensTransAuthorizeList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeDirectorBoard");
            System.out.println("mapParam22222>>>>>>>"+mapParam);
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            observable.setStatus();
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
        } else if (viewType.equals(AUTHORIZE)){
            HashMap singleAuthorizeMap = new HashMap();
            ArrayList arrList = new ArrayList();
            HashMap authorizeMap = new HashMap();
            //    HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("AUTH_BY", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("AUTH_DT",ClientUtil.getCurrentDateWithTime());
            singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);//commented on 13-Feb-2012
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDt.clone());
            singleAuthorizeMap.put("KOLE_FIELD_EXPENSE_ID", koleFiledExpensId);
            if(rdoReceipt.isSelected() && txtIntAmount.getText().length() > 0){
                singleAuthorizeMap.put("INT_CALC_DT_UPDATE","INT_CALC_DT_UPDATE");
            }
            singleAuthorizeMap.put("ACCT_NUM", txtAccountNo.getText());
            singleAuthorizeMap.put(CommonConstants.BRANCH_ID,TrueTransactMain.BRANCH_ID);
            System.out.println("in singleAuthorizeMap>>>>>"+singleAuthorizeMap);
            arrList.add(singleAuthorizeMap);
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            observable.set_authorizeMap(authorizeMap);
            observable.doAction();
            if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED){
                 if (fromNewAuthorizeUI) {
                    newauthorizeListUI.removeSelectedRow();
                    this.dispose();
                    newauthorizeListUI.setFocusToTable();
                    newauthorizeListUI.displayDetails("Director Board Meeting");
                }
                if (fromAuthorizeUI) {
                    authorizeListUI.removeSelectedRow();
                    this.dispose();
                    authorizeListUI.setFocusToTable();
                    authorizeListUI.displayDetails("Director Board Meeting");
                }
            }
            //  observable.execute(authorizeStatus);
            // ClientUtil.ex
            viewType = "";
            //            super.setOpenForEditBy(observable.getStatusBy());
            //            super.removeEditLock(txtTokenConfigId.getText());
            btnCancelActionPerformed(null);
            //  lblStatus.setText(authorizeStatus);
        }
    }
    
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        mitCloseActionPerformed(evt);
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {
        cifClosingAlert();
    }
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
       observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        observable.setStatus();
        popUp("Delete");
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
    observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
    observable.setStatus();
    System.out.println("in edit btn");
     popUp("Edit");
    lblStatus.setText(observable.getLblStatus());
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
//        deletescreenLock();
        viewType = "CANCEL" ;
        observable.resetForm();
        lblCustomerName.setText("");
        ClientUtil.clearAll(this);
        setButtonEnableDisable();
        ClientUtil.enableDisable(this, false);
        lblStatus.setText("");
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
        transactionUI.setCallingApplicantName("");
        transactionUI.setCallingAmount("");
        ((DefaultTableModel) tblTransDetails.getModel()).setRowCount(0);
        
   //     transDetailsUI.setTransDetails(null, null, null);
        setModified(false);
         if (fromNewAuthorizeUI) {
            this.dispose();
            fromNewAuthorizeUI = false;
            newauthorizeListUI.setFocusToTable();
        }
        if (fromAuthorizeUI) {
            this.dispose();
            fromAuthorizeUI = false;
            authorizeListUI.setFocusToTable();
        }
        rdoReceipt.setSelected(false);
        rdoPayment.setSelected(false);
        
    }//GEN-LAST:event_btnCancelActionPerformed
    
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed

        setModified(false);
        System.out.println("in save");
        savePerformed();
       
    }//GEN-LAST:event_btnSaveActionPerformed
 
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        enableDisable(true);
        setButtonEnableDisable();
        setModified(true);
        cboSuspenseProduct.setEnabled(true);      
        tdtLastIntCalcDt.setEnabled(false);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.resetForm();
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        panTransaction.add(transactionUI);
        transactionUI.setSourceScreen("KOLEFIELDEXPENSE");
        transactionUI.setParantUI(this);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
        transactionUI.setMainEnableDisable(true);
        transactionUI.setCallingAccNo("");
        transactionUI.setCallingProdID("");
        transactionUI.setCallingAmount("");
        transactionUI.setCallingTransProdType("");
        transactionUI.setButtonEnableDisable(true);
    }//GEN-LAST:event_btnNewActionPerformed
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed

    private void cboSuspenseProductFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboSuspenseProductFocusLost
        // TODO add your handling code here:
          
    }//GEN-LAST:event_cboSuspenseProductFocusLost

    private void txtAccountNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAccountNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAccountNoActionPerformed

    private void btnMembershipNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMembershipNoActionPerformed
        // TODO add your handling code here:
        popUp("ACCT_NO");
    }//GEN-LAST:event_btnMembershipNoActionPerformed

    private void tdtLastIntCalcDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtLastIntCalcDtFocusLost
      transactionUI.requestFocus();
    }//GEN-LAST:event_tdtLastIntCalcDtFocusLost

    private void txtTransAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTransAmountFocusLost
        // TODO add your handling code here:
        double intAmount = CommonUtil.convertObjToDouble(txtIntAmount.getText());
        double principal = CommonUtil.convertObjToDouble(txtTransAmount.getText());
        double totalAmount = intAmount + principal;
        setTransactionUI();
        transactionUI.setCallingAmount(String.valueOf(totalAmount));
    }//GEN-LAST:event_txtTransAmountFocusLost

    private void txtIntAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIntAmountFocusLost
        // TODO add your handling code here:
        double intAmount = CommonUtil.convertObjToDouble(txtIntAmount.getText());
        double principal = CommonUtil.convertObjToDouble(txtTransAmount.getText());
        double totalAmount = intAmount + principal;
        setTransactionUI();
        transactionUI.setCallingAmount(String.valueOf(totalAmount));
    }//GEN-LAST:event_txtIntAmountFocusLost

    private void rdoReceiptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoReceiptActionPerformed
        // TODO add your handling code here:
          if (rdoReceipt.isSelected()) {
            rdoPayment.setSelected(false);  
            txtIntAmount.setEnabled(true);
            double intAmount = 0.0;
            HashMap dataMap = new HashMap();
            dataMap.put("PROD_ID", observable.getCbmSuspenseProduct().getKeyForSelected());
            dataMap.put("ACCT_NUM", txtAccountNo.getText());
            List intList = ClientUtil.executeQuery("getSelectKoleFieldExpenseInterest", dataMap);
            if (intList != null && intList.size() > 0) {
                dataMap = (HashMap) intList.get(0);
                intAmount = CommonUtil.convertObjToDouble(dataMap.get("INT_AMOUNT"));
                txtIntAmount.setText(String.valueOf(intAmount));
            }
        }else{
            rdoPayment.setSelected(true);
            txtIntAmount.setText("");
            txtIntAmount.setEnabled(false);
          }
    }//GEN-LAST:event_rdoReceiptActionPerformed

    private void rdoPaymentFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rdoPaymentFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoPaymentFocusLost

    private void rdoPaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoPaymentActionPerformed
        // TODO add your handling code here:
        if (rdoPayment.isSelected()) {
            rdoReceipt.setSelected(false);
            System.out.println("payment selected true");
            txtIntAmount.setText("");
            txtIntAmount.setEnabled(false);
        } else {
            System.out.println("payment selected false");
            rdoReceipt.setSelected(true);
        }
    }//GEN-LAST:event_rdoPaymentActionPerformed

    
    private void setTransactionUI(){
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
        transactionUI.setMainEnableDisable(true);
        transactionUI.setCallingAccNo("");
        transactionUI.setCallingProdID("");
        transactionUI.setCallingAmount("");
        transactionUI.setCallingTransProdType("");
        transactionUI.setCallingAmount("");
        transactionUI.setCallingApplicantName(lblCustomerName.getText());
    }
                    
    /** To populate Comboboxes */
    private void initComponentData() {
        cboSuspenseProduct.setModel(observable.getCbmSuspenseProduct());
        
      
  //      cboBoardMember.setModel(observable.getCboBoardMember());
    //    cboRoleTransBran.setModel(observable.getCbmRoleInTranBran());
      //  cboTransBran.setModel(observable.getCbmTransferBran());
    }
    
    /** To display a popUp window for viewing existing data */
    private void popUp(String currAction){
       viewType = currAction;
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        if (currAction.equalsIgnoreCase("Edit")){
            System.out.println("in edit popup");
            HashMap map = new HashMap();
            map.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getDirectorBoardEdit"); 
        }else if(currAction.equalsIgnoreCase("Delete")){
            HashMap map = new HashMap();
            map.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getDirectorBoardEdit");
        }if (currAction.equalsIgnoreCase("ACCT_NO")) {
            viewMap.put(CommonConstants.MAP_NAME, "getLoanBehavesSuspenseAccounts");
            whereMap.put("PROD_ID",((ComboBoxModel) cboSuspenseProduct.getModel()).getKeyForSelected().toString());
            whereMap.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        }// else if(currAction.equalsIgnoreCase("EMP")){
//            HashMap map = new HashMap();
//            map.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
//            viewMap.put(CommonConstants.MAP_WHERE, map);
//            viewMap.put(CommonConstants.MAP_NAME, "setEmpDetailsForTransfer");
//        }
//        else if(currAction.equalsIgnoreCase("Enquiry")){
//            HashMap map = new HashMap();
//            map.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
//            viewMap.put(CommonConstants.MAP_WHERE, map);
//            viewMap.put(CommonConstants.MAP_NAME, "getEmpTransferDetailsView");
//        }
        new ViewAll(this,viewMap).show();
       
    }

    
     public boolean isAccountNumberExsistInAuthList(String actNumber) {
        boolean flag = false;
        if(actNumber != null && !actNumber.equals("")){
            HashMap whereMap = new HashMap();
            whereMap.put("ACCT_NUM",actNumber);
            whereMap.put("CURR_DT",ClientUtil.getCurrentDate());
            List unAuthList = ClientUtil.executeQuery("getNoOfUnauthorizedTransaction", whereMap);
            if(unAuthList != null && unAuthList.size() >0){
                ClientUtil.showMessageWindow("Pending For Authorization ....!");
                flag = true;
            }
        }
        return flag;
    }
    
    
    /** Called by the Popup window created thru popUp method */
   public void fillData(Object map) {
       try{
           setModified(true);
       HashMap hash = (HashMap) map;
           if (hash.containsKey("NEW_FROM_AUTHORIZE_LIST_UI")) {
               fromNewAuthorizeUI = true;
               newauthorizeListUI = (NewAuthorizeListUI) hash.get("PARENT");
               hash.remove("PARENT");
               viewType = AUTHORIZE;
               observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
               observable.setStatus();
               //transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
               btnSave.setEnabled(false);
               btnCancel.setEnabled(true);
               btnAuthorize.setEnabled(true);
               btnReject.setEnabled(true);
               panTransaction.add(transactionUI);
           }
           if (hash.containsKey("FROM_AUTHORIZE_LIST_UI")) {
               fromAuthorizeUI = true;
               authorizeListUI = (AuthorizeListUI) hash.get("PARENT");
               hash.remove("PARENT");
               viewType = AUTHORIZE;
               observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
               observable.setStatus();
               //transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
               btnSave.setEnabled(false);
               btnCancel.setEnabled(true);
               //btnAuthorize.setEnabled(true);
               btnReject.setEnabled(true);
               panTransaction.add(transactionUI);
           }
           if (viewType != null) {
               if (viewType.equals(ClientConstants.ACTION_STATUS[2])
                       || viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE)
                       || viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                   System.out.println("hASHHHHHH>>>" + hash);
                   hash.put(CommonConstants.MAP_WHERE, hash.get("KOLE_FIELD_EXPENSE_ID"));
                   koleFiledExpensId = CommonUtil.convertObjToStr(hash.get("KOLE_FIELD_EXPENSE_ID"));
                   hash.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                   ClientUtil.enableDisable(panTranferDetails, false);
                   transactionUI.setCallingUiMode(AUTHORIZESTAT);
                   transactionUI.setCallingAccNo(CommonUtil.convertObjToStr(hash.get("KOLE_FIELD_EXPENSE_ID")));
                   transactionUI.setSourceAccountNumber(CommonUtil.convertObjToStr(hash.get("KOLE_FIELD_EXPENSE_ID")));
                   if (viewType.equals(AUTHORIZE)) {
                       hash.put("AUTHORIZE", "AUTHORIZE");
                   }
                   observable.getData(hash);
                   if(rdoReceipt.isSelected()){
                       double intAmount = 0.0;
                       HashMap dataMap =  new HashMap();
                       dataMap.put("PROD_ID",observable.getCbmSuspenseProduct().getKeyForSelected());
                       dataMap.put("ACCT_NUM", txtAccountNo.getText());        
                       List intList = ClientUtil.executeQuery("getSelectKoleFieldExpenseInterest", dataMap);
                       if(intList != null && intList.size() > 0){
                           dataMap = (HashMap)intList.get(0);
                           intAmount = CommonUtil.convertObjToDouble(dataMap.get("INT_AMOUNT"));
                           txtIntAmount.setText(String.valueOf(intAmount));
                       }
                       
                   }
               // update(null,null);
               if (viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) ||
               viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                } else {
                   ClientUtil.enableDisable(panTranferDetails, true);
                 // btnEmp.setEnabled(true);
               } 
              setButtonEnableDisable();

           }if(viewType.equalsIgnoreCase("ACCT_NO")){
               if (hash != null && hash.size() > 0) {
                   txtAccountNo.setText(CommonUtil.convertObjToStr(hash.get("SUSPENSE_ACCT_NUM")));
                   boolean flag = isAccountNumberExsistInAuthList(CommonUtil.convertObjToStr(txtAccountNo.getText()));
                   if (flag) {
                       txtAccountNo.setText("");
                   } else {
                       lblCustomerName.setText(CommonUtil.convertObjToStr(hash.get("SUSPENSE_NAME")));
                       txtAccountBalance.setText(CommonUtil.convertObjToStr(hash.get("TOTAL_BALANCE")));
                       transactionUI.setCallingApplicantName(lblCustomerName.getText());
                       tdtLastIntCalcDt.setDateValue(DateUtil.getStringDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hash.get("INT_CALC_UPTO_DT")))));
                       populateTransDetails();
                   }
               }
           }
           if(viewType ==  AUTHORIZE) {
                btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                ClientUtil.enableDisable(this,false);
            }
       }
       }catch(Exception e){
          log.error(e);
        }
       
   }
    
   
   public void populateTransDetails() {
        tblTransDetails.setModel(new javax.swing.table.DefaultTableModel(
                setTableData(),
                //TRN_DT,TRANS_TYPE,AMOUNT,INT_AMOUNT
                new String[]{
                    "Trans Dt","Trans Type", "Principal","Interest"}) {

            Class[] types = new Class[]{
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                false,false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        });   
    }
   
    private Object[][] setTableData() {
        LinkedHashMap whereMap = new LinkedHashMap();
        whereMap.put("ACT_NUM", txtAccountNo.getText());        
        List applnList = ClientUtil.executeQuery("getSelectSuspenseAcctTransDetails", whereMap);
        //TRN_DT,TRANS_TYPE,AMOUNT,INT_AMOUNT 
        System.out.println("list1... :: " + applnList);
        HashMap processMap = new HashMap();
        if (applnList != null && applnList.size() > 0) {
            Object totalList[][] = new Object[applnList.size()][17];
            for (int j = 0; j < applnList.size(); j++) {
                processMap = (HashMap) applnList.get(j);
                totalList[j][0] = CommonUtil.convertObjToStr(processMap.get("TRN_DT"));
                totalList[j][1] = CommonUtil.convertObjToStr(processMap.get("TRANS_TYPE"));
                totalList[j][2] = CommonUtil.convertObjToStr(processMap.get("AMOUNT"));
                totalList[j][3] = CommonUtil.convertObjToStr(processMap.get("INT_AMOUNT"));                
            }
            System.out.println("Populate complete");
            return totalList;
        } 
        return null;
    }
   
   
    
  private void enableDisable(boolean yesno){
      ClientUtil.enableDisable(this, yesno);
  }
//    }
//    
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
    
    public void update(Observable observed, Object arg) {
        //removeRadioButtons();
        Double d = 0.0;
        cboSuspenseProduct.setSelectedItem(observable.getCbmSuspenseProduct().getDataForKey(observable.getCboSuspenseProduct()));
        txtAccountNo.setText(observable.getTxtAccountNo());
        txtTransAmount.setText(observable.getTxtTransAmount());
        txtIntAmount.setText(observable.getTxtIntAmount());
        if (observable.getTransType().equals("CREDIT")) {
            rdoReceipt.setSelected(true);
        } else if (observable.getTransType().equals("DEBIT")) {
            rdoPayment.setSelected(true);
        }
        tdtLastIntCalcDt.setDateValue(observable.getTdtLastIntCalcDt());
        lblStatus.setText(observable.getLblStatus());
        //addRadioButtons();
    }
    
    public void updateOBFields() {
       // observable.setCboBoardMember((String) cboBoardMember.getSelectedItem());
        System.out.println("update ob fields.....");
        observable.setScreen(this.getScreen());
        observable.setCboSuspenseProduct(((ComboBoxModel) cboSuspenseProduct.getModel()).getKeyForSelected().toString());
        observable.setTxtAccountNo(txtAccountNo.getText());
        if(rdoReceipt.isSelected()){
            observable.setTransType("CREDIT");
        }else if(rdoPayment.isSelected()){
            observable.setTransType("DEBIT");
        }
        observable.setTxtIntAmount(txtIntAmount.getText());
        observable.setTxtTransAmount(txtTransAmount.getText());
        observable.setTdtLastIntCalcDt(tdtLastIntCalcDt.getDateValue());
        System.out.println("value in update ob ..."+observable.getTxtSittingFeeAmount());
    }
    
   
    
    private void savePerformed(){
            updateOBFields();
            observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
            observable.doAction() ;
            if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED){
                HashMap lockMap = new HashMap();
                ArrayList lst = new ArrayList();
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                    lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
                    if (observable.getProxyReturnMap() != null) {
                        System.out.println("observable.getProxyReturnMap() :" + observable.getProxyReturnMap());
                        if (observable.getProxyReturnMap().containsKey("KOLE_FIELD_EXPENSE_ID")) {
                            lockMap.put("KOLE_FIELD_EXPENSE_ID", observable.getProxyReturnMap().get("KOLE_FIELD_EXPENSE_ID"));
                            getTransDetails(CommonUtil.convertObjToStr(observable.getProxyReturnMap().get("KOLE_FIELD_EXPENSE_ID")));
                        }
                        if (observable.getProxyReturnMap().containsKey("SINGLE_TRANS_ID")) {
                            lockMap.put("SINGLE_TRANS_ID", observable.getProxyReturnMap().get("SINGLE_TRANS_ID"));
                        }
                        if (lockMap.containsKey("SINGLE_TRANS_ID") && observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                            int yesNo = 0;
                            String[] options = {"Yes", "No"};
                            yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                                    COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                                    null, options, options[0]);
                            //system.out.println("#$#$$ yesNo : " + yesNo);
                            if (yesNo == 0) {
                                TTIntegration ttIntgration = null;
                                if (observable.getProxyReturnMap().containsKey("TRANS_TYPE") && observable.getProxyReturnMap().get("TRANS_TYPE").equals("CASH")) {
                                    lockMap.put("TransId", lockMap.get("SINGLE_TRANS_ID"));
                                    lockMap.put("TransDt", currDt);
                                    lockMap.put("BranchId", ProxyParameters.BRANCH_ID);
                                    ttIntgration.setParam(lockMap);
                                    ttIntgration.integrationForPrint("CashPayment");
                                } else if (observable.getProxyReturnMap().containsKey("TRANS_TYPE") && observable.getProxyReturnMap().get("TRANS_TYPE").equals("TRANSFER")) {
                                    ttIntgration = null;
                                    HashMap reportTransIdMap = new HashMap();
                                    reportTransIdMap.put("TransId", lockMap.get("SINGLE_TRANS_ID"));
                                    reportTransIdMap.put("TransDt", currDt);
                                    reportTransIdMap.put("BranchId", ProxyParameters.BRANCH_ID);
                                    ttIntgration.setParam(reportTransIdMap);
                                    ttIntgration.integrationForPrint("ReceiptPayment", false);
                                }
                            }
                        }
                    }
                }
            }
            
            observable.resetForm();
       enableDisable(false);
       setButtonEnableDisable();
            lblStatus.setText(observable.getLblStatus());
            ClientUtil.enableDisable(this, false);
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
            //__ Make the Screen Closable..
           
            setModified(false);
            ClientUtil.clearAll(this);
            observable.ttNotifyObservers();
            btnCancelActionPerformed(null);
        }
    
   private void setFieldNames() {
       cboSuspenseProduct.setName("cboBoardMember");
       tdtLastIntCalcDt.setName("tdtPaidDate");
        lblBoardMember.setName("lblBoardMember");
        lblCustomerName.setName("lblMeetingDate");
        lblIntAmount.setName("lblSittingFeeAmount");
        lblLastIntCalcDt.setName("lblPaidDate");
       // rdoYes1.setName("rdoApplType");
       // rdoApplType1.setName("rdoApplType1");
        
        
    }
   
   
     private void getTransDetails(String actNum) {
        String displayStr = "";
        HashMap transTypeMap = new HashMap();
        HashMap transMap = new HashMap();
        HashMap transCashMap = new HashMap();
        transCashMap.put("BATCH_ID", actNum);
        transCashMap.put("TRANS_DT", currDt);
        transCashMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        HashMap transIdMap = new HashMap();
        List list = ClientUtil.executeQuery("getTransferDetails", transCashMap);
        if (list != null && list.size() > 0) {
            displayStr += "Transfer Transaction Details...\n";
            for (int i = 0; i < list.size(); i++) {
                transMap = (HashMap) list.get(i);
                displayStr += "Trans Id : " + transMap.get("TRANS_ID")
                        + "   Batch Id : " + transMap.get("BATCH_ID")
                        + "   Trans Type : " + transMap.get("TRANS_TYPE");
                actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                if (actNum != null && !actNum.equals("")) {
                    displayStr += "   Account No : " + transMap.get("ACT_NUM")
                            + "   Deposit Amount : " + transMap.get("AMOUNT") + "\n";
                } else {
                    displayStr += "   Ac Hd Desc : " + transMap.get("AC_HD_ID")
                            + "   Interest Amount : " + transMap.get("AMOUNT") + "\n";
                }
                transIdMap.put(transMap.get("BATCH_ID"), "TRANSFER");
                //System.out.println("#### :" + transMap);
//                                 oldBatchId = newBatchId;
            }
        }

        list = ClientUtil.executeQuery("getCashDetails", transCashMap);
        if (list != null && list.size() > 0) {
            displayStr += "Cash Transaction Details...\n";
            for (int i = 0; i < list.size(); i++) {
                transMap = (HashMap) list.get(i);
                displayStr += "Trans Id : " + transMap.get("TRANS_ID")
                        + "   Trans Type : " + transMap.get("TRANS_TYPE");
                actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                if (actNum != null && !actNum.equals("")) {
                    displayStr += "   Account No :  " + transMap.get("ACT_NUM")
                            + "   Deposit Amount :  " + transMap.get("AMOUNT") + "\n";
                } else {
                    displayStr += "   Ac Hd Desc :  " + transMap.get("AC_HD_ID")
                            + "   Interest Amount :  " + transMap.get("AMOUNT") + "\n";
                }
                transIdMap.put(transMap.get("TRANS_ID"), "CASH");
                transTypeMap.put(transMap.get("TRANS_ID"), transMap.get("TRANS_TYPE"));
            }
        }
        if (!displayStr.equals("")) {
            ClientUtil.showMessageWindow("" + displayStr);
        }
    }
   
    
    private void internationalize() {
              //  java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.directorboardmeeting.DirectorBoardRB", ProxyParameters.LANGUAGE);
        //        lblAccountNumber.setText(resourceBundle.getString("lblAccountNumber"));
        //        lblProd.setText(resourceBundle.getString("lblProd"));
        //        lblOldCustNum.setText(resourceBundle.getString("lblOldCustNum"));
        //        lblNewCustNum.setText(resourceBundle.getString("lblNewCustNum"));
        //        lblProdType.setText(resourceBundle.getString("lblProdType"));
    }
    
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }
    
    private void deletescreenLock(){
        HashMap map=new HashMap();
        map.put("USER_ID",ProxyParameters.USER_ID);
        map.put("TRANS_DT", currDt.clone());
        map.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
        ClientUtil.execute("DELETE_SCREEN_LOCK", map);
    }
    private String getLockDetails(String lockedBy, String screenId){
        HashMap map = new HashMap();
        StringBuffer data = new StringBuffer() ;
        map.put("LOCKED_BY", lockedBy) ;
        map.put("SCREEN_ID", screenId) ;
        java.util.List lstLock = ClientUtil.executeQuery("getLockedDetails", map);
        map.clear();
        if(lstLock.size() > 0){
            map = (HashMap)(lstLock.get(0));
            data.append("\nLog in Time : ").append(map.get("LOCKED_TIME")) ;
            data.append("\nIP Address : ").append(map.get("IP_ADDR")) ;
            data.append("\nBranch : ").append(map.get("BRANCH_ID"));
        }
        lstLock = null ;
        map = null ;
        return data.toString();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDateChange;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnMembershipNo;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    public com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboSuspenseProduct;
    private com.see.truetransact.uicomponent.CTabbedPane koleFieldExpenses;
    private com.see.truetransact.uicomponent.CPanel lblAccountDetails;
    private com.see.truetransact.uicomponent.CLabel lblAccountNo;
    private com.see.truetransact.uicomponent.CLabel lblBalance;
    private com.see.truetransact.uicomponent.CLabel lblBoardMember;
    private com.see.truetransact.uicomponent.CLabel lblCustomerName;
    private com.see.truetransact.uicomponent.CLabel lblIntAmount;
    private com.see.truetransact.uicomponent.CLabel lblLastIntCalcDt;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace62;
    private com.see.truetransact.uicomponent.CLabel lblSpace63;
    private com.see.truetransact.uicomponent.CLabel lblSpace64;
    private com.see.truetransact.uicomponent.CLabel lblSpace65;
    private com.see.truetransact.uicomponent.CLabel lblSpace66;
    private com.see.truetransact.uicomponent.CLabel lblSpace67;
    private com.see.truetransact.uicomponent.CLabel lblSpace68;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTransAmount;
    private javax.swing.JLabel lblTransType;
    private com.see.truetransact.uicomponent.CMenuBar mbrCustomer;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panEmpTransfer;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTranferDetails;
    private com.see.truetransact.uicomponent.CPanel panTransaction;
    private com.see.truetransact.uicomponent.CButtonGroup rdoApplType;
    private com.see.truetransact.uicomponent.CButtonGroup rdoApplType1;
    private com.see.truetransact.uicomponent.CRadioButton rdoPayment;
    private com.see.truetransact.uicomponent.CRadioButton rdoReceipt;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CScrollPane srpTransDetails;
    private com.see.truetransact.uicomponent.CTable tblTransDetails;
    private javax.swing.JToolBar tbrOperativeAcctProduct;
    private com.see.truetransact.uicomponent.CDateField tdtLastIntCalcDt;
    private com.see.truetransact.uicomponent.CTextField txtAccountBalance;
    private com.see.truetransact.uicomponent.CTextField txtAccountNo;
    private com.see.truetransact.uicomponent.CTextField txtIntAmount;
    private com.see.truetransact.uicomponent.CTextField txtTransAmount;
    // End of variables declaration//GEN-END:variables
    
    public static void main(String[] args) {
        DirectorBoardUI dirBrd = new DirectorBoardUI();
        javax.swing.JFrame j = new javax.swing.JFrame();
        j.getContentPane().add(dirBrd);
        j.show();
//        empTran.show();
    

}
}