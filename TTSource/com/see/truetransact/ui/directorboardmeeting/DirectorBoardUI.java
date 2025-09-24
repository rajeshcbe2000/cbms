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

package com.see.truetransact.ui.directorboardmeeting;

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



/** This form is used to manipulate CustomerIdChangeUI related functionality
 * @author swaroop
 */
public class DirectorBoardUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer, UIMandatoryField{

    private String viewType = new String();
    private HashMap mandatoryMap;
    private DirectorBoardOB observable;
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
    private Date currDt = null;
    private TransactionUI transactionUI = new TransactionUI(); 
    boolean fromAuthorizeUI = false;    
    AuthorizeListUI authorizeListUI = null;
    NewAuthorizeListUI newauthorizeListUI = null;
    boolean fromNewAuthorizeUI = false;
    /** Creates new form CustomerIdChangeUI */
    public DirectorBoardUI() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        initStartUp();
    }
    
    private void initStartUp(){
        setMandatoryHashMap();
        setFieldNames();
        internationalize();
        observable = new DirectorBoardOB();
        observable.addObserver(this);
        observable.setTransactionOB(transactionUI.getTransactionOB());
        initComponentData();
        enableDisable(false);
        setButtonEnableDisable();
        setMaxLength();
       //  objMandatoryRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.directorboardmeeting.DirectorBoardMRB", ProxyParameters.LANGUAGE);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panTranferDetails);
 
   //fiiiiiiiiiiiiiiiiiii drpddddown
        
      /*  List BM=ClientUtil.executeQuery("getAllBoardMem", new HashMap());
         for(int i=0;i<=BM.size();i++){
            String board=null;
            if(BM.get(i)!=null){
                 board=BM.get(i).toString();
            }
           cboBoardMember.addItem(board);
        }
       */ // setHelpMessage();
        
        txtSittingFeeAmount.setEditable(false);
        tdtPaidDate.setEnabled(false);
    }
    
    private void setMaxLength() {
//           txtEmpID.setMaxLength(64);
//           txtEmpID.setValidation(new CurrencyValidation());
//         txtAccountNumber.setMaxLength(16);
//         txtAccountNumber.setAllowAll(true);
//         txtOldCustID.setAllowAll(true);
//         txtNewCustId.setAllowAll(true);
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
        panTranferDetails = new com.see.truetransact.uicomponent.CPanel();
        panTransaction = new com.see.truetransact.uicomponent.CPanel();
        cPanel1 = new com.see.truetransact.uicomponent.CPanel();
        lblBoardMember = new com.see.truetransact.uicomponent.CLabel();
        cboBoardMember = new com.see.truetransact.uicomponent.CComboBox();
        btnMembershipNo = new com.see.truetransact.uicomponent.CButton();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        txtMemberName = new com.see.truetransact.uicomponent.CTextField();
        lblMeetingDate = new com.see.truetransact.uicomponent.CLabel();
        tdtMeetingDate = new com.see.truetransact.uicomponent.CDateField();
        lblAttended = new javax.swing.JLabel();
        panReq1 = new com.see.truetransact.uicomponent.CPanel();
        rdoYes1 = new com.see.truetransact.uicomponent.CRadioButton();
        rdoNo1 = new com.see.truetransact.uicomponent.CRadioButton();
        lblSittingFeePaid = new com.see.truetransact.uicomponent.CLabel();
        panReq2 = new com.see.truetransact.uicomponent.CPanel();
        rdoYes2 = new com.see.truetransact.uicomponent.CRadioButton();
        rdoNo2 = new com.see.truetransact.uicomponent.CRadioButton();
        lblSittingFeeAmount = new com.see.truetransact.uicomponent.CLabel();
        txtSittingFeeAmount = new javax.swing.JTextField();
        lblPaidDate = new com.see.truetransact.uicomponent.CLabel();
        tdtPaidDate = new com.see.truetransact.uicomponent.CDateField();
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
        setMaximumSize(new java.awt.Dimension(900, 520));
        setMinimumSize(new java.awt.Dimension(900, 520));
        setPreferredSize(new java.awt.Dimension(900, 520));

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

        cPanel1.setMinimumSize(new java.awt.Dimension(523, 300));
        cPanel1.setPreferredSize(new java.awt.Dimension(523, 300));
        cPanel1.setLayout(new java.awt.GridBagLayout());

        lblBoardMember.setText("Board Member");
        lblBoardMember.setMaximumSize(new java.awt.Dimension(100, 21));
        lblBoardMember.setMinimumSize(new java.awt.Dimension(100, 21));
        lblBoardMember.setPreferredSize(new java.awt.Dimension(100, 21));
        cPanel1.add(lblBoardMember, new java.awt.GridBagConstraints());

        cboBoardMember.setMaximumSize(new java.awt.Dimension(100, 21));
        cboBoardMember.setMinimumSize(new java.awt.Dimension(100, 21));
        cboBoardMember.setNextFocusableComponent(btnMembershipNo);
        cboBoardMember.setPopupWidth(225);
        cboBoardMember.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboBoardMemberFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        cPanel1.add(cboBoardMember, gridBagConstraints);

        btnMembershipNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnMembershipNo.setNextFocusableComponent(txtMemberName);
        btnMembershipNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnMembershipNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMembershipNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 32, 0, 0);
        cPanel1.add(btnMembershipNo, gridBagConstraints);

        cLabel1.setText("Member Name");
        cLabel1.setMaximumSize(new java.awt.Dimension(100, 21));
        cLabel1.setMinimumSize(new java.awt.Dimension(100, 21));
        cLabel1.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        cPanel1.add(cLabel1, gridBagConstraints);

        txtMemberName.setMinimumSize(new java.awt.Dimension(175, 21));
        txtMemberName.setNextFocusableComponent(tdtMeetingDate);
        txtMemberName.setPreferredSize(new java.awt.Dimension(175, 21));
        txtMemberName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMemberNameActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        cPanel1.add(txtMemberName, gridBagConstraints);

        lblMeetingDate.setText("Date of Meeting");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel1.add(lblMeetingDate, gridBagConstraints);

        tdtMeetingDate.setMaximumSize(new java.awt.Dimension(101, 19));
        tdtMeetingDate.setMinimumSize(new java.awt.Dimension(101, 19));
        tdtMeetingDate.setName("tdtDateOfBirth"); // NOI18N
        tdtMeetingDate.setNextFocusableComponent(rdoYes1);
        tdtMeetingDate.setPreferredSize(new java.awt.Dimension(101, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel1.add(tdtMeetingDate, gridBagConstraints);

        lblAttended.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        lblAttended.setText("Attended");
        lblAttended.setMaximumSize(new java.awt.Dimension(100, 21));
        lblAttended.setMinimumSize(new java.awt.Dimension(100, 21));
        lblAttended.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel1.add(lblAttended, gridBagConstraints);

        panReq1.setMaximumSize(new java.awt.Dimension(101, 19));
        panReq1.setMinimumSize(new java.awt.Dimension(101, 19));
        panReq1.setPreferredSize(new java.awt.Dimension(101, 19));
        panReq1.setLayout(new java.awt.GridBagLayout());

        rdoYes1.setText("Yes");
        rdoYes1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        rdoYes1.setNextFocusableComponent(rdoNo1);
        rdoYes1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoYes1ActionPerformed(evt);
            }
        });
        panReq1.add(rdoYes1, new java.awt.GridBagConstraints());

        rdoNo1.setText("No");
        rdoNo1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        rdoNo1.setNextFocusableComponent(rdoYes2);
        panReq1.add(rdoNo1, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel1.add(panReq1, gridBagConstraints);

        lblSittingFeePaid.setText("Sitting Fee Paid");
        lblSittingFeePaid.setMaximumSize(new java.awt.Dimension(100, 21));
        lblSittingFeePaid.setMinimumSize(new java.awt.Dimension(100, 21));
        lblSittingFeePaid.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel1.add(lblSittingFeePaid, gridBagConstraints);

        panReq2.setMaximumSize(new java.awt.Dimension(101, 19));
        panReq2.setMinimumSize(new java.awt.Dimension(101, 19));
        panReq2.setPreferredSize(new java.awt.Dimension(101, 19));
        panReq2.setLayout(new java.awt.GridBagLayout());

        rdoYes2.setText("Yes");
        rdoYes2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        rdoYes2.setNextFocusableComponent(rdoNo2);
        rdoYes2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoYes2ActionPerformed(evt);
            }
        });
        panReq2.add(rdoYes2, new java.awt.GridBagConstraints());

        rdoNo2.setText("No");
        rdoNo2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        rdoNo2.setNextFocusableComponent(txtSittingFeeAmount);
        rdoNo2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoNo2ActionPerformed(evt);
            }
        });
        panReq2.add(rdoNo2, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel1.add(panReq2, gridBagConstraints);

        lblSittingFeeAmount.setText("Sitting Fee Amount");
        lblSittingFeeAmount.setMaximumSize(new java.awt.Dimension(112, 21));
        lblSittingFeeAmount.setMinimumSize(new java.awt.Dimension(112, 21));
        lblSittingFeeAmount.setPreferredSize(new java.awt.Dimension(112, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel1.add(lblSittingFeeAmount, gridBagConstraints);

        txtSittingFeeAmount.setMaximumSize(new java.awt.Dimension(101, 21));
        txtSittingFeeAmount.setMinimumSize(new java.awt.Dimension(101, 21));
        txtSittingFeeAmount.setNextFocusableComponent(tdtPaidDate);
        txtSittingFeeAmount.setOpaque(false);
        txtSittingFeeAmount.setPreferredSize(new java.awt.Dimension(101, 21));
        txtSittingFeeAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSittingFeeAmountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel1.add(txtSittingFeeAmount, gridBagConstraints);

        lblPaidDate.setText("Paid Date");
        lblPaidDate.setMaximumSize(new java.awt.Dimension(100, 21));
        lblPaidDate.setMinimumSize(new java.awt.Dimension(100, 21));
        lblPaidDate.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel1.add(lblPaidDate, gridBagConstraints);

        tdtPaidDate.setMaximumSize(new java.awt.Dimension(101, 24));
        tdtPaidDate.setMinimumSize(new java.awt.Dimension(101, 24));
        tdtPaidDate.setName("tdtDateOfBirth"); // NOI18N
        tdtPaidDate.setPreferredSize(new java.awt.Dimension(101, 24));
        tdtPaidDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtPaidDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel1.add(tdtPaidDate, gridBagConstraints);

        javax.swing.GroupLayout panTranferDetailsLayout = new javax.swing.GroupLayout(panTranferDetails);
        panTranferDetails.setLayout(panTranferDetailsLayout);
        panTranferDetailsLayout.setHorizontalGroup(
            panTranferDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panTranferDetailsLayout.createSequentialGroup()
                .addComponent(cPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(315, 315, 315))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panTranferDetailsLayout.createSequentialGroup()
                .addComponent(panTransaction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panTranferDetailsLayout.setVerticalGroup(
            panTranferDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panTranferDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panTransaction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panEmpTransfer.add(panTranferDetails, new java.awt.GridBagConstraints());

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
        btnNew.setNextFocusableComponent(cboBoardMember);
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

    private void rdoNo2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoNo2ActionPerformed
        // TODO add your handling code here:
        txtSittingFeeAmount.setEditable(false);
        tdtPaidDate.setEnabled(false);
      //  txtSittingFeeAmount.setEnabled(false);
        panTransaction.remove(transactionUI);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
        transactionUI.setCallingApplicantName("");
        transactionUI.setCallingAmount("");
        transactionUI.setMainEnableDisable(false);
        panTransaction.removeAll();
    }//GEN-LAST:event_rdoNo2ActionPerformed
    
    private void rdoYes2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoYes2ActionPerformed
        // TODO add your handling code here:
        txtSittingFeeAmount.setEditable(true);
        //txtSittingFeeAmount.setEnabled(true);
        tdtPaidDate.setEnabled(true);
  		transactionUI.setButtonEnableDisable(true);
       
     
    }//GEN-LAST:event_rdoYes2ActionPerformed
    
    private void rdoYes1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoYes1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoYes1ActionPerformed

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
        transactionUI.setSourceScreen("DIRECTORBOARD");
        transactionUI.setParantUI(this);
        if (!viewType.equals(AUTHORIZE)){
            viewType = AUTHORIZE;
            //            setModified(true);
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID,TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID,TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.INITIATED_BRANCH, ProxyParameters.BRANCH_ID);
            whereMap.put("TRANS_DT", currDt);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            System.out.println("mapParam1111>>>>>>>"+mapParam);
            whereMap = null;
            mapParam.put(CommonConstants.MAP_NAME, "getDirectorBoardAuthorizeList");
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
            //   singleAuthorizeMap.put("DEPR_BATCH_ID", observable.getDeprBatchId());
            singleAuthorizeMap.put("PAN","DEPR");
            singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);//commented on 13-Feb-2012
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDt.clone());
            singleAuthorizeMap.put("BOARD_MT_ID", board_mt_id);
            singleAuthorizeMap.put(CommonConstants.BRANCH_ID,TrueTransactMain.BRANCH_ID);
            System.out.println("in singleAuthorizeMap>>>>>"+singleAuthorizeMap);
          //  ClientUtil.execute("authorizeDirectorBoard", singleAuthorizeMap);
            //                singleAuthorizeMap.put("DEPRECIATION_DATE",ClientUtil.getCurrentDateWithTime());
            //                singleAuthorizeMap.put("BRANCH_CODE", observable.getDeprBranchCode());
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
        ClientUtil.clearAll(this);
        setButtonEnableDisable();
        ClientUtil.enableDisable(this, false);
        lblStatus.setText("");
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
        transactionUI.setCallingApplicantName("");
        transactionUI.setCallingAmount("");
        
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
        
    }//GEN-LAST:event_btnCancelActionPerformed
    
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed

        setModified(false);
        System.out.println("in save");
//         savePerformed();
//       //    System.out.println("IN btnSaveActionPerformed111");
//        btnAuthorize.setEnabled(true);
//        btnReject.setEnabled(true);
//        btnException.setEnabled(true);
        
              //  updateOBFields();
             String mandatoryMessage ="";
        StringBuffer message = new StringBuffer(mandatoryMessage);
       
       
        resourceBundle = new DirectorBoardRB();
        //final String shareAcctMandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panDrfTransDetails);
       
        StringBuffer strBMandatory = new StringBuffer();
        if(cboBoardMember.getSelectedIndex()==0){
            message.append(objMandatoryRB.getString("cboBoardMember"));
        }
        if(tdtMeetingDate.getDateValue()==null || tdtMeetingDate.getDateValue().equals("") )
        {
            message.append(objMandatoryRB.getString("tdtMeetingDate"));
        }
        if(rdoYes2.isSelected() && txtSittingFeeAmount.getText().equals("")) {
            message.append(objMandatoryRB.getString("txtSittingFeeAmount"));
        }
          if(rdoYes2.isSelected() && (tdtPaidDate.getDateValue()==null || tdtPaidDate.getDateValue().equals("")) )
        {
            message.append(objMandatoryRB.getString("tdtPaidDate"));
        }

        if(message.length() > 0 ) {
            displayAlert(message.toString());
            return;
        }    
     
       /* String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panTranferDetails);
        if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0 ){
           displayAlert(mandatoryMessage);
            System.out.println("in save1");
           //  savePerformed();
        }
        */
        else{
            System.out.println("in save2");
            savePerformed();
        }
    }//GEN-LAST:event_btnSaveActionPerformed
    
//     private void savePerformed(){
//       
//       // System.out.println("IN savePerformed");
//        String action;
//      //    System.out.println("IN observable.getActionType(): "+observable.getActionType());
//     //      System.out.println("IN ClientConstants.ACTIONTYPE_NEW: "+ClientConstants.ACTIONTYPE_NEW);
//        if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW ){
//        //     System.out.println("IN savePerformed ACTIONTYPE_NEW"); 
//                     
//            action=CommonConstants.TOSTATUS_INSERT;
//            saveAction(action);
//          
//        }else if(observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT){
//            action=CommonConstants.TOSTATUS_UPDATE;
//            saveAction(action);
//        }else if(observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE){
//            action=CommonConstants.TOSTATUS_DELETE;
//            saveAction(action);
//        }
//    }
//    
    
     
     
     
//     private void saveAction(String status){
//        //To check mandtoryness of the Terminal panAcHdDetails,panAcHeadDetails panel and diplay appropriate
//        //error message, else proceed
//      //  System.out.println("status saveAction11111: "+status);
////       txtAmtBorrowed.
//        final String mandatoryMessage = checkMandatory(panBorrowing);
//        StringBuffer message = new StringBuffer(mandatoryMessage);
//        if(cboBoardMember.getSelectedItem().equals(""))
//        {
//            message.append(objMandatoryRB.getString("cboBoardMember"));
//        }
//        if(tdtMeetingDate.getDateValue().equals(""))
//        {
//                message.append(objMandatoryRB.getString("tdtMeetingDate"));
//         }
//         
//        
//      /*  if(txtSeriesNo.getText().equals(""))
//        {
//                message.append(objMandatoryRB.getString("txtSeriesNo"));
//         }
//         if((CommonUtil.convertObjToInt(txtStartingTokenNo.getText()))> (CommonUtil.convertObjToInt(txtEndingTokenNo.getText())))
//         {
//          message.append(objMandatoryRB.getString("txtNumber"));   
//         }
//       //Portion is for calculating exp date
//      // setExpDateOnCalculation();
//      
//       
//       */
//        //setExpDateOnCalculation();
//      //  System.out.println("status saveAction: "+status);
//        if(message.length() > 0 ){
//            displayAlert(message.toString());
//       }else{
//             updateOBFields();
//               // setExpDateOnCalculation();
//            observable.execute(status);
////            if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED){
////                HashMap lockMap = new HashMap();
////                ArrayList lst = new ArrayList();
////                lst.add("BORROWING_NO");
////                lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
////             //   if (observable.getProxyReturnMap()!=null) {
////              //      if (observable.getProxyReturnMap().containsKey("CONFIG_ID")) {
////              //          lockMap.put("CONFIG_ID", observable.getProxyReturnMap().get("CONFIG_ID"));
////              //      }
////              //  }
////                if (status==CommonConstants.TOSTATUS_UPDATE) {
////                    lockMap.put("BORROWING_NO", observable.getBorrowingNo());
////                }
////          //      setEditLockMap(lockMap);
////               // setEditLock();
////                settings();
////            }
//        }
//            
//    }
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        enableDisable(true);
        setButtonEnableDisable();
        setModified(true);
        cboBoardMember.setEnabled(true);
        tdtMeetingDate.setEnabled(true);
        panReq1.setEnabled(true);
         panReq2.setEnabled(true);
        txtSittingFeeAmount.setEditable(false);
        tdtPaidDate.setEnabled(false);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.resetForm();
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        panTransaction.add(transactionUI);
        transactionUI.setSourceScreen("DIRECTORBOARD");
        transactionUI.setParantUI(this);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
        transactionUI.setMainEnableDisable(false);
        transactionUI.setCallingAccNo("");
        transactionUI.setCallingProdID("");
        transactionUI.setCallingAmount("");
        transactionUI.setCallingTransProdType("");
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

    private void cboBoardMemberFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboBoardMemberFocusLost
        // TODO add your handling code here:
          txtMemberName.setText(observable.getCbmBoardMember().getKeyForSelected().toString());
    }//GEN-LAST:event_cboBoardMemberFocusLost

    private void txtMemberNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMemberNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMemberNameActionPerformed

    private void btnMembershipNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMembershipNoActionPerformed
        // TODO add your handling code here:
        popUp("MEMBER_NO");
    }//GEN-LAST:event_btnMembershipNoActionPerformed

    private void txtSittingFeeAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSittingFeeAmountFocusLost
       
        transactionUI.setCallingAmount(txtSittingFeeAmount.getText());
       transactionUI.setCallingApplicantName(txtMemberName.getText());
    }//GEN-LAST:event_txtSittingFeeAmountFocusLost

    private void tdtPaidDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtPaidDateFocusLost
      transactionUI.requestFocus();
    }//GEN-LAST:event_tdtPaidDateFocusLost
                
    /** To populate Comboboxes */
    private void initComponentData() {
        cboBoardMember.setModel(observable.getCbmBoardMember());
        
      
  //      cboBoardMember.setModel(observable.getCboBoardMember());
    //    cboRoleTransBran.setModel(observable.getCbmRoleInTranBran());
      //  cboTransBran.setModel(observable.getCbmTransferBran());
    }
    
    /** To display a popUp window for viewing existing data */
    private void popUp(String currAction){
       viewType = currAction;
        HashMap viewMap = new HashMap();
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
        }if (currAction.equalsIgnoreCase("MEMBER_NO")) {
            viewMap.put(CommonConstants.MAP_NAME, "getAllBoardMemDet");
            viewMap.put(CommonConstants.MAP_WHERE, new HashMap());
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
                   System.out.println("board_mt_id>>>" + hash.get("BOARD_MT_ID"));
                   System.out.println("hASHHHHHH>>>" + hash);
                   hash.put(CommonConstants.MAP_WHERE, hash.get("BOARD_MT_ID"));
                   board_mt_id = CommonUtil.convertObjToStr(hash.get("BOARD_MT_ID"));
                   hash.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                   System.out.println("hassssh>>>" + hash);
                   ClientUtil.enableDisable(panTranferDetails, false);
                   //transactionUI.okAction(true);
                   transactionUI.setCallingUiMode(AUTHORIZESTAT);
                   transactionUI.setCallingAccNo(CommonUtil.convertObjToStr(hash.get("BOARD_MT_ID")));
                   transactionUI.setCallingAmount(txtSittingFeeAmount.getText());
                   transactionUI.setSourceAccountNumber(CommonUtil.convertObjToStr(hash.get("BOARD_MT_ID")));
                   if (viewType.equals(AUTHORIZE)) {
                       hash.put("AUTHORIZE", "AUTHORIZE");
                   }
                   observable.getData(hash);
               // update(null,null);
               if (viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) ||
               viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                } else {
                   ClientUtil.enableDisable(panTranferDetails, true);
                 // btnEmp.setEnabled(true);
               } 
              setButtonEnableDisable();

           }if(viewType.equalsIgnoreCase("MEMBER_NO")){
               if(hash!=null && hash.size()>0){
               cboBoardMember.setSelectedItem(CommonUtil.convertObjToStr(hash.get("MEMBERSHIP_NO")));
               observable.setCboBoMember(observable.getCbmBoardMember().getKeyForSelected().toString());
               String CUST_ID = CommonUtil.convertObjToStr(hash.get("CUST_ID"));
               txtMemberName.setText(observable.getCbmBoardMember().getKeyForSelected().toString());
               transactionUI.setCallingAccNo(CommonUtil.convertObjToStr(hash.get("MEMBERSHIP_NO")));
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
        removeRadioButtons();
        Double d=0.0;
      cboBoardMember.setSelectedItem(observable.getCboBoMember());
      txtMemberName.setText(observable.getCbmBoardMember().getKeyForSelected().toString());
        if(observable.getTxtSittingFeeAmount()!=null) {
              d=observable.getTxtSittingFeeAmount();
        
      if(d>0){
       txtSittingFeeAmount.setText(String.valueOf(d));
      }}
      else{
          txtSittingFeeAmount.setText("");
      }
     txtBoard_mt_id=observable.getTxtdirectorBoardNo();
     
       tdtMeetingDate.setDateValue(observable.getTxtMeetingDate());
       tdtPaidDate.setDateValue(observable.getTxtPaidDate());
     //  lblCurrBranValue.setText(observable.getTxtCurrBran());
  
        rdoYes1.setSelected(observable.isRdoYes1());
       rdoNo1.setSelected(observable.isRdoNo1());
       rdoYes2.setSelected(observable.isRdoYes2());
       rdoNo2.setSelected(observable.isRdoNo2());
       lblStatus.setText(observable.getLblStatus());
       //lblEmpTransferID.setText(observable.getTxtEmpTransferID());
       // lblEmpName.setText(observable.getEmpName());
      // lblCurrBranValue.setText(observable.getCurrBranName());
      addRadioButtons();
    }
    
    public void updateOBFields() {
       // observable.setCboBoardMember((String) cboBoardMember.getSelectedItem());
        System.out.println("update ob fields.....");
        observable.setScreen(this.getScreen());
       observable.setCboBoMember((String) cboBoardMember.getSelectedItem());
        System.out.println("txtSittingFeeAmount.getText()"+txtSittingFeeAmount.getText());
       if(txtSittingFeeAmount.getText()==null || txtSittingFeeAmount.getText().equals("")){
           observable.setTxtSittingFeeAmount(0.0);
       
       }else{
        observable.setTxtSittingFeeAmount(Double.parseDouble(txtSittingFeeAmount.getText()));
           
       }
        observable.setTxtMeetingDate(tdtMeetingDate.getDateValue());
        observable.setTxtPaidDate(tdtPaidDate.getDateValue());
        observable.setRdoYes1(rdoYes1.isSelected());
        observable.setRdoNo1(rdoNo1.isSelected()); 
        observable.setRdoYes2(rdoYes2.isSelected());
        observable.setRdoNo2(rdoNo2.isSelected()); 
        observable.setTxtdirectorBoardNo(txtBoard_mt_id);
       System.out.println("value in update ob ..."+observable.getTxtSittingFeeAmount());
    }
    
    private void addRadioButtons() {
        rdoApplType = new CButtonGroup();
        rdoApplType1= new CButtonGroup();
        rdoApplType.add(rdoYes1);
        rdoApplType.add(rdoNo1);
        rdoApplType1.add(rdoYes2);
        rdoApplType1.add(rdoNo2);
        
    }
    
    private void removeRadioButtons() {
        rdoApplType.remove(rdoYes1);
        rdoApplType.remove(rdoNo1);
        rdoApplType1.remove(rdoYes2);
        rdoApplType1.remove(rdoNo2);
    }
    
    private void savePerformed(){

            System.out.println("save aaa");
            updateOBFields();
            observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
            observable.doAction() ;
            if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED){
                HashMap lockMap = new HashMap();
                ArrayList lst = new ArrayList();
//                lst.add("EMP_TRANSFER_ID");
//                lockMap.put("EMP_TRANSFER_ID",CommonUtil.convertObjToStr(lblEmpTransferID.getText()));
//                lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                    lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
                    if (observable.getProxyReturnMap() != null) {
                        System.out.println("observable.getProxyReturnMap() :" + observable.getProxyReturnMap());
                        if (observable.getProxyReturnMap().containsKey("DIRECTOR_ID")) {
                            lockMap.put("DIRECTOR_ID", observable.getProxyReturnMap().get("DIRECTOR_ID"));
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
//                if(observable.getResult()==ClientConstants.ACTIONTYPE_EDIT){
//                    lockMap.put("EMP_TRANSFER_ID", observable.getTxtEmpTransferID());
//                }
//                setEditLockMap(lockMap);
//                setEditLock();
//                deletescreenLock();
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
       cboBoardMember.setName("cboBoardMember");
        tdtMeetingDate.setName("tdtMeetingDate");
       txtSittingFeeAmount.setName("txtSittingFeeAmount");
       tdtPaidDate.setName("tdtPaidDate");
      rdoYes1.setName("rdoYes1");
        rdoNo1.setName("rdoNo1");
        rdoYes2.setName("rdoYes2");
        rdoNo2.setName("rdoNo2");
        lblBoardMember.setName("lblBoardMember");
        lblMeetingDate.setName("lblMeetingDate");
        lblSittingFeeAmount.setName("lblSittingFeeAmount");
        lblPaidDate.setName("lblPaidDate");
       // rdoYes1.setName("rdoApplType");
       // rdoApplType1.setName("rdoApplType1");
        
        
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
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CPanel cPanel1;
    private com.see.truetransact.uicomponent.CComboBox cboBoardMember;
    private javax.swing.JLabel lblAttended;
    private com.see.truetransact.uicomponent.CLabel lblBoardMember;
    private com.see.truetransact.uicomponent.CLabel lblMeetingDate;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblPaidDate;
    private com.see.truetransact.uicomponent.CLabel lblSittingFeeAmount;
    private com.see.truetransact.uicomponent.CLabel lblSittingFeePaid;
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
    private com.see.truetransact.uicomponent.CPanel panReq1;
    private com.see.truetransact.uicomponent.CPanel panReq2;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTranferDetails;
    private com.see.truetransact.uicomponent.CPanel panTransaction;
    private com.see.truetransact.uicomponent.CButtonGroup rdoApplType;
    private com.see.truetransact.uicomponent.CButtonGroup rdoApplType1;
    private com.see.truetransact.uicomponent.CRadioButton rdoNo1;
    private com.see.truetransact.uicomponent.CRadioButton rdoNo2;
    private com.see.truetransact.uicomponent.CRadioButton rdoYes1;
    private com.see.truetransact.uicomponent.CRadioButton rdoYes2;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private javax.swing.JToolBar tbrOperativeAcctProduct;
    private com.see.truetransact.uicomponent.CDateField tdtMeetingDate;
    private com.see.truetransact.uicomponent.CDateField tdtPaidDate;
    private com.see.truetransact.uicomponent.CTextField txtMemberName;
    private javax.swing.JTextField txtSittingFeeAmount;
    // End of variables declaration//GEN-END:variables
    
    public static void main(String[] args) {
        DirectorBoardUI dirBrd = new DirectorBoardUI();
        javax.swing.JFrame j = new javax.swing.JFrame();
        j.getContentPane().add(dirBrd);
        j.show();
//        empTran.show();
    

}
}