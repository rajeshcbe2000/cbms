/**
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * Author   : Chithra
 * Location : Thrissur
 * Date of Completion : 1-08-2015
 */
package com.see.truetransact.ui.payroll.leaveSurrender;

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
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.customer.customeridchange.CustomerIdChangeOB;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.interestcalc.Rounding;
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

/**
 * This form is used to manipulate CustomerIdChangeUI related functionality
 *
 * @author swaroop
 */
public class LeaveSurrenderUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer, UIMandatoryField {

    private String viewType = new String();
    private HashMap mandatoryMap;
    Rounding rd = new Rounding();
    private LeaveSurrenderOB observable;
    // DirectorBoardOB ob;
    private final int AUTHORIZESTAT = 8, CANCEL = 0;
    final String AUTHORIZE = "Authorize";
    final String REJECT = "Reject";
    private final static Logger log = Logger.getLogger(LeaveSurrenderUI.class);
    LeaveSurrenderRB LeaveSurrenderRB = new LeaveSurrenderRB();
    // private String prodType="";
    java.util.ResourceBundle resourceBundle;
    LeaveSurrenderMRB objMandatoryRB = new LeaveSurrenderMRB();
    String txtBoard_mt_id = null;
    String leave_surrender_id = null;
    private Date currDt = null;
    private TransactionUI transactionUI = new TransactionUI();

    /**
     * Creates new form CustomerIdChangeUI
     */
    public LeaveSurrenderUI() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        initStartUp();
    }

    private void initStartUp() {
        setMandatoryHashMap();
        setFieldNames();
        internationalize();
        observable = new LeaveSurrenderOB();
        observable.addObserver(this);
        observable.setTransactionOB(transactionUI.getTransactionOB());
        initComponentData();
        enableDisable(false);
        setButtonEnableDisable();
        setMaxLength();
        //  objMandatoryRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.directorboardmeeting.DirectorBoardMRB", ProxyParameters.LANGUAGE);
//        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panTranferDetails);

        //fiiiiiiiiiiiiiiiiiii drpddddown

        /*
         * List BM=ClientUtil.executeQuery("getAllBoardMem", new HashMap());
         * for(int i=0;i<=BM.size();i++){ String board=null;
         * if(BM.get(i)!=null){ board=BM.get(i).toString(); }
         * cboBoardMember.addItem(board); }
         */ // setHelpMessage();

        txtLeaveAmount.setEditable(false);
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

    /**
     * **************** NEW METHODS ****************
     */
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
    /**
     * ********** END OF NEW METHODS **************
     */
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panEmpTransfer = new com.see.truetransact.uicomponent.CPanel();
        panTranferDetails = new com.see.truetransact.uicomponent.CPanel();
        panTransaction = new com.see.truetransact.uicomponent.CPanel();
        cInternalFrame1 = new com.see.truetransact.uicomponent.CInternalFrame();
        panStatus1 = new com.see.truetransact.uicomponent.CPanel();
        lblSpace = new com.see.truetransact.uicomponent.CLabel();
        lblStatus1 = new com.see.truetransact.uicomponent.CLabel();
        lblMsg1 = new com.see.truetransact.uicomponent.CLabel();
        tbrPfMaster = new javax.swing.JToolBar();
        btnNew1 = new com.see.truetransact.uicomponent.CButton();
        btnEdit1 = new com.see.truetransact.uicomponent.CButton();
        btnDelete1 = new com.see.truetransact.uicomponent.CButton();
        lblSpace6 = new com.see.truetransact.uicomponent.CLabel();
        btnSave1 = new com.see.truetransact.uicomponent.CButton();
        btnCancel1 = new com.see.truetransact.uicomponent.CButton();
        lblSpace7 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize1 = new com.see.truetransact.uicomponent.CButton();
        btnReject1 = new com.see.truetransact.uicomponent.CButton();
        btnException1 = new com.see.truetransact.uicomponent.CButton();
        lblSpace8 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint1 = new com.see.truetransact.uicomponent.CButton();
        btnClose1 = new com.see.truetransact.uicomponent.CButton();
        panPfClose = new com.see.truetransact.uicomponent.CPanel();
        panPfMaster = new com.see.truetransact.uicomponent.CPanel();
        lblPfDate = new com.see.truetransact.uicomponent.CLabel();
        lblPfAccountNo = new com.see.truetransact.uicomponent.CLabel();
        txtPfAccountNo = new com.see.truetransact.uicomponent.CTextField();
        lblBatchIdValue = new com.see.truetransact.uicomponent.CLabel();
        lblOpeningBalance = new com.see.truetransact.uicomponent.CLabel();
        tdtLastInterestDate = new com.see.truetransact.uicomponent.CDateField();
        lblPfOpeningDate = new com.see.truetransact.uicomponent.CLabel();
        tdtPfDate = new com.see.truetransact.uicomponent.CDateField();
        txtOpeningBalance = new com.see.truetransact.uicomponent.CTextField();
        lblPfRateOfInterest = new com.see.truetransact.uicomponent.CLabel();
        lblLastInterestDate = new com.see.truetransact.uicomponent.CLabel();
        lblPfNomineeName = new com.see.truetransact.uicomponent.CLabel();
        lblPfNomineeRelation = new com.see.truetransact.uicomponent.CLabel();
        lblEmployerContribution = new com.see.truetransact.uicomponent.CLabel();
        txtPfRateOfInterest = new com.see.truetransact.uicomponent.CTextField();
        txtPfNomineeName = new com.see.truetransact.uicomponent.CTextField();
        txtPfNomineeRelation = new com.see.truetransact.uicomponent.CTextField();
        txtEmployerContribution = new com.see.truetransact.uicomponent.CTextField();
        tdtPfOpeningDate = new com.see.truetransact.uicomponent.CDateField();
        lblPfRate = new javax.swing.JLabel();
        panShareCloseTrans = new com.see.truetransact.uicomponent.CPanel();
        cPanel1 = new com.see.truetransact.uicomponent.CPanel();
        lblEmployeeId = new com.see.truetransact.uicomponent.CLabel();
        panEmpDetails = new com.see.truetransact.uicomponent.CPanel();
        panEmployeeId = new com.see.truetransact.uicomponent.CPanel();
        txtEmployeeId = new com.see.truetransact.uicomponent.CTextField();
        btnEmployeeId = new com.see.truetransact.uicomponent.CButton();
        mbrPfMaster = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess1 = new javax.swing.JMenu();
        mitNew1 = new javax.swing.JMenuItem();
        mitEdit1 = new javax.swing.JMenuItem();
        mitDelete1 = new javax.swing.JMenuItem();
        sptProcess = new javax.swing.JSeparator();
        mitSave1 = new javax.swing.JMenuItem();
        mitCancel1 = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitPrint1 = new javax.swing.JMenuItem();
        sptPrint = new javax.swing.JSeparator();
        mitClose1 = new javax.swing.JMenuItem();
        cPanel2 = new com.see.truetransact.uicomponent.CPanel();
        cPanel3 = new com.see.truetransact.uicomponent.CPanel();
        lblEmpId = new com.see.truetransact.uicomponent.CLabel();
        txtEmpId = new com.see.truetransact.uicomponent.CTextField();
        btnEmpId = new com.see.truetransact.uicomponent.CButton();
        lblLeaveAmount = new com.see.truetransact.uicomponent.CLabel();
        txtLeaveAmount = new javax.swing.JTextField();
        txtMemberName = new com.see.truetransact.uicomponent.CTextField();
        lblMemberName = new com.see.truetransact.uicomponent.CLabel();
        lblLeaveNo = new com.see.truetransact.uicomponent.CLabel();
        txtLeaveNo = new com.see.truetransact.uicomponent.CTextField();
        tbrOperativeAcctProduct = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace29 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace30 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace31 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace32 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace33 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace34 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        lblSpace35 = new com.see.truetransact.uicomponent.CLabel();
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
        setModified(true);
        setPreferredSize(new java.awt.Dimension(900, 600));

        panEmpTransfer.setMaximumSize(new java.awt.Dimension(650, 480));
        panEmpTransfer.setMinimumSize(new java.awt.Dimension(650, 480));
        panEmpTransfer.setPreferredSize(new java.awt.Dimension(650, 480));
        panEmpTransfer.setLayout(new java.awt.GridBagLayout());

        panTranferDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panTranferDetails.setMinimumSize(new java.awt.Dimension(850, 475));
        panTranferDetails.setName("panMaritalStatus"); // NOI18N
        panTranferDetails.setPreferredSize(new java.awt.Dimension(850, 475));
        panTranferDetails.setLayout(new java.awt.GridBagLayout());

        panTransaction.setMinimumSize(new java.awt.Dimension(830, 300));
        panTransaction.setOpaque(false);
        panTransaction.setPreferredSize(new java.awt.Dimension(830, 300));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        panTranferDetails.add(panTransaction, gridBagConstraints);

        cInternalFrame1.setClosable(true);
        cInternalFrame1.setIconifiable(true);
        cInternalFrame1.setMaximizable(true);
        cInternalFrame1.setResizable(true);
        cInternalFrame1.setMaximumSize(new java.awt.Dimension(825, 575));
        cInternalFrame1.setMinimumSize(new java.awt.Dimension(825, 575));
        cInternalFrame1.setPreferredSize(new java.awt.Dimension(825, 575));

        panStatus1.setLayout(new java.awt.GridBagLayout());

        lblSpace.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus1.add(lblSpace, gridBagConstraints);

        lblStatus1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblStatus1.setText("                      ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus1.add(lblStatus1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panStatus1.add(lblMsg1, gridBagConstraints);

        cInternalFrame1.getContentPane().add(panStatus1, java.awt.BorderLayout.SOUTH);

        btnNew1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew1.setToolTipText("New");
        btnNew1.setEnabled(false);
        btnNew1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNew1ActionPerformed(evt);
            }
        });
        tbrPfMaster.add(btnNew1);

        btnEdit1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit1.setToolTipText("Edit");
        btnEdit1.setEnabled(false);
        btnEdit1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEdit1ActionPerformed(evt);
            }
        });
        tbrPfMaster.add(btnEdit1);

        btnDelete1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete1.setToolTipText("Delete");
        btnDelete1.setEnabled(false);
        btnDelete1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelete1ActionPerformed(evt);
            }
        });
        tbrPfMaster.add(btnDelete1);

        lblSpace6.setText("     ");
        tbrPfMaster.add(lblSpace6);

        btnSave1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave1.setToolTipText("Save");
        btnSave1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSave1ActionPerformed(evt);
            }
        });
        tbrPfMaster.add(btnSave1);

        btnCancel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel1.setToolTipText("Cancel");
        btnCancel1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancel1ActionPerformed(evt);
            }
        });
        tbrPfMaster.add(btnCancel1);

        lblSpace7.setText("     ");
        tbrPfMaster.add(lblSpace7);

        btnAuthorize1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize1.setToolTipText("Authorize");
        tbrPfMaster.add(btnAuthorize1);

        btnReject1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject1.setToolTipText("Reject");
        btnReject1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReject1ActionPerformed(evt);
            }
        });
        tbrPfMaster.add(btnReject1);

        btnException1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException1.setToolTipText("Exception");
        tbrPfMaster.add(btnException1);

        lblSpace8.setText("     ");
        tbrPfMaster.add(lblSpace8);

        btnPrint1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint1.setToolTipText("Print");
        tbrPfMaster.add(btnPrint1);

        btnClose1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose1.setToolTipText("Close");
        btnClose1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClose1ActionPerformed(evt);
            }
        });
        tbrPfMaster.add(btnClose1);

        cInternalFrame1.getContentPane().add(tbrPfMaster, java.awt.BorderLayout.NORTH);

        panPfClose.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panPfClose.setMaximumSize(new java.awt.Dimension(815, 350));
        panPfClose.setMinimumSize(new java.awt.Dimension(815, 350));
        panPfClose.setPreferredSize(new java.awt.Dimension(1000, 1000));
        panPfClose.setLayout(new java.awt.GridBagLayout());

        panPfMaster.setBorder(javax.swing.BorderFactory.createTitledBorder("PFMasterDetails"));
        panPfMaster.setMaximumSize(new java.awt.Dimension(320, 207));
        panPfMaster.setMinimumSize(new java.awt.Dimension(300, 400));
        panPfMaster.setPreferredSize(new java.awt.Dimension(300, 500));
        panPfMaster.setLayout(new java.awt.GridBagLayout());

        lblPfDate.setText("PF Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        panPfMaster.add(lblPfDate, gridBagConstraints);

        lblPfAccountNo.setText("PF Account No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        panPfMaster.add(lblPfAccountNo, gridBagConstraints);

        txtPfAccountNo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtPfAccountNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPfAccountNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPfAccountNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panPfMaster.add(txtPfAccountNo, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPfMaster.add(lblBatchIdValue, gridBagConstraints);

        lblOpeningBalance.setText("Opening Balance");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        panPfMaster.add(lblOpeningBalance, gridBagConstraints);

        tdtLastInterestDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtLastInterestDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panPfMaster.add(tdtLastInterestDate, gridBagConstraints);

        lblPfOpeningDate.setText("PF Opening Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        panPfMaster.add(lblPfOpeningDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panPfMaster.add(tdtPfDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panPfMaster.add(txtOpeningBalance, gridBagConstraints);

        lblPfRateOfInterest.setText("PF Rate of Interest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        panPfMaster.add(lblPfRateOfInterest, gridBagConstraints);

        lblLastInterestDate.setText("Last Interest Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        panPfMaster.add(lblLastInterestDate, gridBagConstraints);

        lblPfNomineeName.setText("PF Nominee Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        panPfMaster.add(lblPfNomineeName, gridBagConstraints);

        lblPfNomineeRelation.setText("PF Nominee Relation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        panPfMaster.add(lblPfNomineeRelation, gridBagConstraints);

        lblEmployerContribution.setText("Employer Contribution");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        panPfMaster.add(lblEmployerContribution, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panPfMaster.add(txtPfRateOfInterest, gridBagConstraints);

        txtPfNomineeName.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panPfMaster.add(txtPfNomineeName, gridBagConstraints);

        txtPfNomineeRelation.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panPfMaster.add(txtPfNomineeRelation, gridBagConstraints);

        txtEmployerContribution.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panPfMaster.add(txtEmployerContribution, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panPfMaster.add(tdtPfOpeningDate, gridBagConstraints);

        lblPfRate.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 0);
        panPfMaster.add(lblPfRate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 96;
        gridBagConstraints.insets = new java.awt.Insets(25, 20, 0, 0);
        panPfClose.add(panPfMaster, gridBagConstraints);

        panShareCloseTrans.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panPfClose.add(panShareCloseTrans, gridBagConstraints);

        cPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("EmployeeDetails"));
        cPanel1.setMinimumSize(new java.awt.Dimension(320, 380));
        cPanel1.setLayout(new java.awt.GridBagLayout());

        lblEmployeeId.setText("Employee ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 16, 0, 0);
        cPanel1.add(lblEmployeeId, gridBagConstraints);

        panEmpDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("EmployeeData"));
        panEmpDetails.setMinimumSize(new java.awt.Dimension(230, 265));
        panEmpDetails.setPreferredSize(new java.awt.Dimension(230, 265));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(20, 0, 0, 9);
        cPanel1.add(panEmpDetails, gridBagConstraints);

        panEmployeeId.setLayout(new java.awt.GridBagLayout());

        txtEmployeeId.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtEmployeeId.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panEmployeeId.add(txtEmployeeId, gridBagConstraints);

        btnEmployeeId.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnEmployeeId.setToolTipText("Share No");
        btnEmployeeId.setEnabled(false);
        btnEmployeeId.setMinimumSize(new java.awt.Dimension(21, 21));
        btnEmployeeId.setPreferredSize(new java.awt.Dimension(21, 21));
        btnEmployeeId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmployeeIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panEmployeeId.add(btnEmployeeId, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 19, 2, 18);
        cPanel1.add(panEmployeeId, gridBagConstraints);

        panPfClose.add(cPanel1, new java.awt.GridBagConstraints());

        cInternalFrame1.getContentPane().add(panPfClose, java.awt.BorderLayout.CENTER);

        mnuProcess1.setText("Process");

        mitNew1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        mitNew1.setMnemonic('N');
        mitNew1.setText("New");
        mitNew1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNew1ActionPerformed(evt);
            }
        });
        mnuProcess1.add(mitNew1);

        mitEdit1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        mitEdit1.setMnemonic('E');
        mitEdit1.setText("Edit");
        mitEdit1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEdit1ActionPerformed(evt);
            }
        });
        mnuProcess1.add(mitEdit1);

        mitDelete1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        mitDelete1.setMnemonic('D');
        mitDelete1.setText("Delete");
        mitDelete1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDelete1ActionPerformed(evt);
            }
        });
        mnuProcess1.add(mitDelete1);
        mnuProcess1.add(sptProcess);

        mitSave1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        mitSave1.setMnemonic('S');
        mitSave1.setText("Save");
        mitSave1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSave1ActionPerformed(evt);
            }
        });
        mnuProcess1.add(mitSave1);

        mitCancel1.setMnemonic('C');
        mitCancel1.setText("Cancel");
        mitCancel1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancel1ActionPerformed(evt);
            }
        });
        mnuProcess1.add(mitCancel1);
        mnuProcess1.add(sptCancel);

        mitPrint1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        mitPrint1.setMnemonic('P');
        mitPrint1.setText("Print");
        mitPrint1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitPrint1ActionPerformed(evt);
            }
        });
        mnuProcess1.add(mitPrint1);
        mnuProcess1.add(sptPrint);

        mitClose1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        mitClose1.setMnemonic('l');
        mitClose1.setText("Close");
        mitClose1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitClose1ActionPerformed(evt);
            }
        });
        mnuProcess1.add(mitClose1);

        mbrPfMaster.add(mnuProcess1);

        cInternalFrame1.setJMenuBar(mbrPfMaster);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        panTranferDetails.add(cInternalFrame1, gridBagConstraints);

        cPanel2.setMinimumSize(new java.awt.Dimension(830, 150));
        cPanel2.setOpaque(false);
        cPanel2.setPreferredSize(new java.awt.Dimension(830, 150));

        cPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Employee Details"));
        cPanel3.setMinimumSize(new java.awt.Dimension(422, 100));
        cPanel3.setPreferredSize(new java.awt.Dimension(422, 100));
        cPanel3.setLayout(new java.awt.GridBagLayout());

        lblEmpId.setText("Employee ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 0);
        cPanel3.add(lblEmpId, gridBagConstraints);

        txtEmpId.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtEmpId.setMinimumSize(new java.awt.Dimension(100, 21));
        txtEmpId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmpIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        cPanel3.add(txtEmpId, gridBagConstraints);

        btnEmpId.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnEmpId.setToolTipText("Share No");
        btnEmpId.setMinimumSize(new java.awt.Dimension(21, 21));
        btnEmpId.setPreferredSize(new java.awt.Dimension(21, 21));
        btnEmpId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmpIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 55, 0, 0);
        cPanel3.add(btnEmpId, gridBagConstraints);

        lblLeaveAmount.setText("Amount ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel3.add(lblLeaveAmount, gridBagConstraints);

        txtLeaveAmount.setMaximumSize(new java.awt.Dimension(101, 21));
        txtLeaveAmount.setMinimumSize(new java.awt.Dimension(101, 21));
        txtLeaveAmount.setPreferredSize(new java.awt.Dimension(101, 19));
        txtLeaveAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtLeaveAmountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        cPanel3.add(txtLeaveAmount, gridBagConstraints);

        txtMemberName.setMinimumSize(new java.awt.Dimension(175, 21));
        txtMemberName.setPreferredSize(new java.awt.Dimension(175, 21));
        txtMemberName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMemberNameActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        cPanel3.add(txtMemberName, gridBagConstraints);

        lblMemberName.setText("Member Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        cPanel3.add(lblMemberName, gridBagConstraints);

        lblLeaveNo.setText("Leave No:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        cPanel3.add(lblLeaveNo, gridBagConstraints);

        txtLeaveNo.setAllowAll(true);
        txtLeaveNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtLeaveNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLeaveNoActionPerformed(evt);
            }
        });
        txtLeaveNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtLeaveNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        cPanel3.add(txtLeaveNo, gridBagConstraints);

        javax.swing.GroupLayout cPanel2Layout = new javax.swing.GroupLayout(cPanel2);
        cPanel2.setLayout(cPanel2Layout);
        cPanel2Layout.setHorizontalGroup(
            cPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(398, Short.MAX_VALUE))
        );
        cPanel2Layout.setVerticalGroup(
            cPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(39, Short.MAX_VALUE))
        );

        panTranferDetails.add(cPanel2, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panEmpTransfer.add(panTranferDetails, gridBagConstraints);

        getContentPane().add(panEmpTransfer, java.awt.BorderLayout.CENTER);

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

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace29);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnEdit);

        lblSpace30.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace30.setText("     ");
        lblSpace30.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace30);

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

        lblSpace31.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace31.setText("     ");
        lblSpace31.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace31);

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

        lblSpace32.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace32.setText("     ");
        lblSpace32.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace32.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace32.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace32);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnException);

        lblSpace33.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace33.setText("     ");
        lblSpace33.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace33.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace33.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace33);

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

        lblSpace34.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace34.setText("     ");
        lblSpace34.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace34);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnClose);

        lblSpace35.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace35.setText("     ");
        lblSpace35.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace35.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace35.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace35);

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
        // viewType = "";
    }//GEN-LAST:event_btnAuthorizeActionPerformed

    public void authorizeStatus(String authorizeStatus) {
        panTransaction.add(transactionUI);
        transactionUI.setSourceScreen("LEAVESURRENDER");
        transactionUI.setParantUI(this);
        if (!viewType.equals(AUTHORIZE) && !viewType.equals(REJECT)) {
            viewType = AUTHORIZE;
            if (authorizeStatus.equals(CommonConstants.STATUS_REJECTED)) {
                viewType = REJECT;
            }
            //            setModified(true);
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            System.out.println("mapParam1111>>>>>>>" + mapParam);
            whereMap = null;
            mapParam.put(CommonConstants.MAP_NAME, "getAuthorizeLeaveSurrenderDet");
            //   mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeDirectorBoard");
            System.out.println("mapParam22222>>>>>>>" + mapParam);
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            observable.setStatus();
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
        } else if (viewType.equals(AUTHORIZE) || authorizeStatus.equals(CommonConstants.STATUS_REJECTED)) {
            HashMap singleAuthorizeMap = new HashMap();
            ArrayList arrList = new ArrayList();
            HashMap authorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("AUTH_BY", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("AUTH_DT", ClientUtil.getCurrentDateWithTime());
            singleAuthorizeMap.put("PAN", "DEPR");
            singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);//commented on 13-Feb-2012
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDt.clone());
            singleAuthorizeMap.put("LEAVE_SURRENDER_ID", leave_surrender_id);
            singleAuthorizeMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            System.out.println("in singleAuthorizeMap>>>>>" + singleAuthorizeMap);
            arrList.add(singleAuthorizeMap);
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            observable.set_authorizeMap(authorizeMap);
            observable.doAction();
            viewType = "";
            btnCancelActionPerformed(null);
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
        viewType = "CANCEL";
        observable.resetForm();
        ClientUtil.clearAll(this);
        setButtonEnableDisable();
        ClientUtil.enableDisable(this, false);
        lblStatus.setText("");
        txtEmpId.setText("");
        txtMemberName.setText("");
        txtLeaveAmount.setText("");
        txtLeaveNo.setText("");
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
        transactionUI.setCallingApplicantName("");
        transactionUI.setCallingAmount("");

        //     transDetailsUI.setTransDetails(null, null, null);
        setModified(false);

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
        String mandatoryMessage = "";
        StringBuffer message = new StringBuffer(mandatoryMessage);


        resourceBundle = new LeaveSurrenderRB();
        //final String shareAcctMandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panDrfTransDetails);

        StringBuffer strBMandatory = new StringBuffer();

        if (txtEmpId.getText().length() <= 0) {
            ClientUtil.showMessageWindow("Please Enter Empolee Number");
            return;
        }
        if (CommonUtil.convertObjToInt(txtLeaveNo.getText()) == 0) {
            ClientUtil.showMessageWindow("Please Enter Leave Number");
            return;
        }
        if (CommonUtil.convertObjToDouble(txtLeaveAmount.getText()) == 0) {
            ClientUtil.showMessageWindow("Please Enter Leave Amount");
            return;
        }
        if (message.length() > 0) {
            displayAlert(message.toString());
            return;
        }
        if(CommonUtil.convertObjToDouble(transactionUI.getCallingAmount())<=0){
            ClientUtil.showMessageWindow("Please Enter Transaction Amount");
            return;
        }
        if(transactionUI.getOutputTO().size() == 0){
            ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS)) ;
            return;
        }
        /*
         * String mandatoryMessage = new
         * MandatoryCheck().checkMandatory(getClass().getName(),
         * panTranferDetails); if( observable.getActionType() !=
         * ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0 ){
         * displayAlert(mandatoryMessage); System.out.println("in save1"); //
         * savePerformed(); }
         */ else {
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
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.resetForm();
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        transactionUI.cancelAction(false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.resetObjects();
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
        panTransaction.add(transactionUI);
        transactionUI.setSourceScreen("LEAVESURRENDER");
        transactionUI.setParantUI(this);


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

    private void txtMemberNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMemberNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMemberNameActionPerformed

    private void txtLeaveAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtLeaveAmountFocusLost
        transactionUI.setCallingAmount(txtLeaveAmount.getText());
        transactionUI.setCallingApplicantName(txtMemberName.getText());
    }//GEN-LAST:event_txtLeaveAmountFocusLost

    private void btnNew1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNew1ActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        btnCancel.setEnabled(true);
        btnSave.setEnabled(true);
        btnNew.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        lblStatus.setText("New");
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }//GEN-LAST:event_btnNew1ActionPerformed

    private void btnEdit1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEdit1ActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT); //__ Sets the Action Type to be performed...
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
        btnSave.setEnabled(true);
        lblStatus.setText("Edit");
    }//GEN-LAST:event_btnEdit1ActionPerformed

    private void btnDelete1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete1ActionPerformed

        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE); //__ Sets the Action Type to be performed...
        //   callView(ClientConstants.ACTIONTYPE_DELETE);
        lblStatus.setText("Delete");
        btnDelete.setEnabled(true);
        btnNew.setEnabled(false);
        btnEdit.setEnabled(false);
        btnCancel.setEnabled(true);
        btnSave.setEnabled(false);
        // }
    }//GEN-LAST:event_btnDelete1ActionPerformed

    private void btnSave1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSave1ActionPerformed
        // TODO add your handling code here:
        final String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panPfClose);
        if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0) {
            displayAlert(mandatoryMessage);
        } else {
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                //  boolean chkEmpId = observable.chkEmpIdExists(txtEmployeeId.getText());
                // if (chkEmpId == true) {
                //    ClientUtil.showMessageWindow("This Employee already exists!!!");
                //    return;
                // }
            }
            updateOBFields();
            observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
            observable.doAction();
            if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                //  setTxtEnabled(false);
                lblStatus.setText("Success");
                /// resetTextFields();
                observable.resetForm();
            } else if (observable.getResult() == ClientConstants.ACTIONTYPE_FAILED) {
                lblStatus.setText("Failed");
                //    setTxtEnabled(false);
                // resetTextFields();
                observable.resetForm();
            }
        }
    }//GEN-LAST:event_btnSave1ActionPerformed

    private void btnCancel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancel1ActionPerformed
        // TODO add your handling code here:
        observable.resetForm();                 //__ Reset the fields in the UI to null...
        ClientUtil.enableDisable(this, false);  //__ Disables the panel...
        setButtonEnableDisable();               //__ Enables or Disables the buttons and menu Items depending on their previous state...
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);  //__ Sets the Action Type to be performed...
        observable.setStatus();
        lblStatus.setText("Cancel");
        btnSave.setEnabled(false);
        btnNew.setEnabled(true);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
        //__ Make the Screen Closable..
        setModified(false);
    }//GEN-LAST:event_btnCancel1ActionPerformed

    private void btnReject1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReject1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnReject1ActionPerformed

    private void btnClose1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClose1ActionPerformed
        // TODO add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnClose1ActionPerformed

    private void txtPfAccountNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPfAccountNoFocusLost
        // TODO add your handling code here:
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            //  boolean chkPfAct = observable.chkPFActNoExists(txtPfAccountNo.getText());
            //if (chkPfAct == true) {
            //    ClientUtil.showMessageWindow("This Account No already exists!!!");
            //   txtPfAccountNo.setText("");
            //}
        }
    }//GEN-LAST:event_txtPfAccountNoFocusLost

    private void tdtLastInterestDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtLastInterestDateFocusLost
        // TODO add your handling code here:
        ClientUtil.validateToDate(tdtLastInterestDate, tdtPfOpeningDate.getDateValue());
    }//GEN-LAST:event_tdtLastInterestDateFocusLost

    private void btnEmployeeIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmployeeIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEmployeeIdActionPerformed

    private void mitNew1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNew1ActionPerformed
        // TODO add your handling code here:
        btnEditActionPerformed(null);
    }//GEN-LAST:event_mitNew1ActionPerformed

    private void mitEdit1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEdit1ActionPerformed
        // TODO add your handling code here:
        btnEditActionPerformed(null);
    }//GEN-LAST:event_mitEdit1ActionPerformed

    private void mitDelete1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDelete1ActionPerformed
        // TODO add your handling code here:
        btnDeleteActionPerformed(null);
    }//GEN-LAST:event_mitDelete1ActionPerformed

    private void mitSave1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSave1ActionPerformed
        // TODO add your handling code here:
        btnSaveActionPerformed(null);
    }//GEN-LAST:event_mitSave1ActionPerformed

    private void mitCancel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancel1ActionPerformed
        // TODO add your handling code here:
        btnCancelActionPerformed(null);
    }//GEN-LAST:event_mitCancel1ActionPerformed

    private void mitPrint1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitPrint1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mitPrint1ActionPerformed

    private void mitClose1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitClose1ActionPerformed
        // TODO add your handling code here:
        btnCloseActionPerformed(null);
    }//GEN-LAST:event_mitClose1ActionPerformed

    private void btnEmpIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmpIdActionPerformed
        popUp("EMPLOYEE");
    }//GEN-LAST:event_btnEmpIdActionPerformed

    private void txtEmpIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmpIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmpIdActionPerformed

    private void txtLeaveNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLeaveNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLeaveNoActionPerformed

    private void txtLeaveNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtLeaveNoFocusLost
        HashMap map = new HashMap();
        map.put("EMP_ID", txtEmpId.getText());
        List resltList = ClientUtil.executeQuery("getLeaveSurrenderAmount", map);
        map.clear();
        if (resltList.size() > 0) {
            map = (HashMap) (resltList.get(0));
            double amt = CommonUtil.convertObjToDouble(map.get("AMOUNT"));
            int days = CommonUtil.convertObjToInt(txtLeaveNo.getText());
            double totamt = amt * days;
            long roundOffType = getRoundOffType("Nearest Value");
            if (roundOffType != 0) {
                totamt = rd.getNearest((long) (totamt * roundOffType), roundOffType) / roundOffType;
            }
            txtLeaveAmount.setText(CommonUtil.convertObjToStr(totamt));
            transactionUI.setCallingAmount(txtLeaveAmount.getText());
            transactionUI.setCallingApplicantName(txtMemberName.getText());
        }
    }//GEN-LAST:event_txtLeaveNoFocusLost

    /**
     * To populate Comboboxes
     */
    private void initComponentData() {
        //   cboBoardMember.setModel(observable.getCbmBoardMember());
        //      cboBoardMember.setModel(observable.getCboBoardMember());
        //    cboRoleTransBran.setModel(observable.getCbmRoleInTranBran());
        //  cboTransBran.setModel(observable.getCbmTransferBran());
    }

    /**
     * To display a popUp window for viewing existing data
     */
    private void popUp(String currAction) {
        viewType = currAction;
        HashMap viewMap = new HashMap();
        if (currAction.equalsIgnoreCase("Edit")) {
            System.out.println("in edit popup");
            HashMap map = new HashMap();
            map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getDirectorBoardEdit");
        } else if (currAction.equalsIgnoreCase("Delete")) {
            HashMap map = new HashMap();
            map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getDirectorBoardEdit");
        }
        if (currAction.equalsIgnoreCase("EMPLOYEE")) {
            viewMap.put(CommonConstants.MAP_NAME, "getEmployeeDet");
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
        new ViewAll(this, viewMap).show();

    }

    /**
     * Called by the Popup window created thru popUp method
     */
    public void fillData(Object map) {
        try {
//            setModified(true);
            HashMap hash = (HashMap) map;
            System.out.println("hASHHHHHH>>>" + hash);
            if (viewType != null) {
                if (viewType.equals(ClientConstants.ACTION_STATUS[2])
                        || viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE)
                        || viewType.equals(ClientConstants.ACTION_STATUS[17]) || viewType.equals(REJECT)) {
                    System.out.println("LEAVE_SURRENDER_ID>>>" + hash.get("LEAVE_SURRENDER_ID"));

                    hash.put(CommonConstants.MAP_WHERE, hash.get("LEAVE_SURRENDER_ID"));
                    leave_surrender_id = CommonUtil.convertObjToStr(hash.get("LEAVE_SURRENDER_ID"));
                    hash.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                    System.out.println("hassssh>>>" + hash);
                    ClientUtil.enableDisable(panTranferDetails, false);
                    //transactionUI.okAction(true);
                    txtMemberName.setText(CommonUtil.convertObjToStr(hash.get("EMP_NAME")));
                    observable.setTxtMemberName(txtMemberName.getText());
                    transactionUI.setCallingUiMode(AUTHORIZESTAT);
                    transactionUI.setCallingAccNo(CommonUtil.convertObjToStr(hash.get("LEAVE_SURRENDER_ID")));
                    transactionUI.setCallingAmount(txtLeaveAmount.getText());
                    transactionUI.setSourceAccountNumber(CommonUtil.convertObjToStr(hash.get("LEAVE_SURRENDER_ID")));
                    if (viewType.equals(AUTHORIZE)) {
                        hash.put("AUTHORIZE", "AUTHORIZE");
                    }
                    observable.getData(hash);
                    // update(null,null);
                    if (viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE)
                            || viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                    } else {
                        ClientUtil.enableDisable(panTranferDetails, true);
                        // btnEmp.setEnabled(true);
                    }
                    setButtonEnableDisable();

                }
                if (viewType.equalsIgnoreCase("EMPLOYEE")) {
                    if (hash != null && hash.size() > 0) {
                        txtEmpId.setText(CommonUtil.convertObjToStr(hash.get("EMPLOYEEID")));
                        //observable.setCboBoMember(observable.getCbmBoardMember().getKeyForSelected().toString());
                        String CUST_ID = CommonUtil.convertObjToStr(hash.get("CUST_ID"));
                        txtMemberName.setText(CommonUtil.convertObjToStr(hash.get("EMPLOYEE_NAME")));
                    }
                }
            }
        } catch (Exception e) {
            log.error(e);
        }

    }

    private void enableDisable(boolean yesno) {
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
        Double d = 0.0;
        txtMemberName.setText(observable.getTxtMemberName());
        txtEmpId.setText(observable.getTxtEmpId());
        lblStatus.setText(observable.getLblStatus());
        txtLeaveNo.setText(CommonUtil.convertObjToStr(observable.getTxtLeaveNo()));
        txtLeaveAmount.setText(CommonUtil.convertObjToStr(observable.getTxtLeaveAmount()));
        //lblEmpTransferID.setText(observable.getTxtEmpTransferID());
        // lblEmpName.setText(observable.getEmpName());
        // lblCurrBranValue.setText(observable.getCurrBranName());
    }

    public void updateOBFields() {
        observable.setTxtLeaveAmount(CommonUtil.convertObjToDouble(txtLeaveAmount.getText()));
        observable.setTxtLeaveNo(CommonUtil.convertObjToInt(txtLeaveNo.getText()));
        observable.setTxtEmpId(txtEmpId.getText());
        observable.setTxtMemberName(txtMemberName.getText());
        System.out.println("update ob fields.....");
        System.out.println("txtSittingFeeAmount.getText()" + txtLeaveAmount.getText());

    }

    private void savePerformed() {

        System.out.println("save aaa");
        updateOBFields();
        observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
        observable.doAction();
        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            HashMap lockMap = new HashMap();
            ArrayList lst = new ArrayList();
//                lst.add("EMP_TRANSFER_ID");
//                lockMap.put("EMP_TRANSFER_ID",CommonUtil.convertObjToStr(lblEmpTransferID.getText()));
//                lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
               // lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
                if (observable.getProxyReturnMap() != null) {
                        if (observable.getProxyReturnMap().containsKey("LEAVE_SURRENDER_ID")) {
                            lockMap.put("LEAVE_SURRENDER_ID",observable.getProxyReturnMap().get("LEAVE_SURRENDER_ID"));
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
btnCancelActionPerformed(null);
        setModified(false);
        ClientUtil.clearAll(this);
        observable.ttNotifyObservers();
    }

    private void setFieldNames() {
        txtLeaveAmount.setName("txtSittingFeeAmount");
        lblLeaveAmount.setName("lblSittingFeeAmount");
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

    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }

    private void deletescreenLock() {
        HashMap map = new HashMap();
        map.put("USER_ID", ProxyParameters.USER_ID);
        map.put("TRANS_DT", currDt.clone());
        map.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
        ClientUtil.execute("DELETE_SCREEN_LOCK", map);
    }

    private String getLockDetails(String lockedBy, String screenId) {
        HashMap map = new HashMap();
        StringBuffer data = new StringBuffer();
        map.put("LOCKED_BY", lockedBy);
        map.put("SCREEN_ID", screenId);
        java.util.List lstLock = ClientUtil.executeQuery("getLockedDetails", map);
        map.clear();
        if (lstLock.size() > 0) {
            map = (HashMap) (lstLock.get(0));
            data.append("\nLog in Time : ").append(map.get("LOCKED_TIME"));
            data.append("\nIP Address : ").append(map.get("IP_ADDR"));
            data.append("\nBranch : ").append(map.get("BRANCH_ID"));
        }
        lstLock = null;
        map = null;
        return data.toString();
    }

    private int getRoundOffType(String roundOff) {
        int returnVal = 0;
        if (roundOff.equals("Nearest Value")) {
            returnVal = 1 * 100;
        } else if (roundOff.equals("Nearest Hundreds")) {
            returnVal = 100 * 100;
        } else if (roundOff.equals("Nearest Tens")) {
            returnVal = 10 * 100;
        }
        return returnVal;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnAuthorize1;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnCancel1;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnClose1;
    private com.see.truetransact.uicomponent.CButton btnDateChange;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDelete1;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnEdit1;
    private com.see.truetransact.uicomponent.CButton btnEmpId;
    private com.see.truetransact.uicomponent.CButton btnEmployeeId;
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
    private com.see.truetransact.uicomponent.CInternalFrame cInternalFrame1;
    private com.see.truetransact.uicomponent.CPanel cPanel1;
    private com.see.truetransact.uicomponent.CPanel cPanel2;
    private com.see.truetransact.uicomponent.CPanel cPanel3;
    private com.see.truetransact.uicomponent.CLabel lblBatchIdValue;
    private com.see.truetransact.uicomponent.CLabel lblEmpId;
    private com.see.truetransact.uicomponent.CLabel lblEmployeeId;
    private com.see.truetransact.uicomponent.CLabel lblEmployerContribution;
    private com.see.truetransact.uicomponent.CLabel lblLastInterestDate;
    private com.see.truetransact.uicomponent.CLabel lblLeaveAmount;
    private com.see.truetransact.uicomponent.CLabel lblLeaveNo;
    private com.see.truetransact.uicomponent.CLabel lblMemberName;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblMsg1;
    private com.see.truetransact.uicomponent.CLabel lblOpeningBalance;
    private com.see.truetransact.uicomponent.CLabel lblPfAccountNo;
    private com.see.truetransact.uicomponent.CLabel lblPfDate;
    private com.see.truetransact.uicomponent.CLabel lblPfNomineeName;
    private com.see.truetransact.uicomponent.CLabel lblPfNomineeRelation;
    private com.see.truetransact.uicomponent.CLabel lblPfOpeningDate;
    private javax.swing.JLabel lblPfRate;
    private com.see.truetransact.uicomponent.CLabel lblPfRateOfInterest;
    private com.see.truetransact.uicomponent.CLabel lblSpace;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace29;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace30;
    private com.see.truetransact.uicomponent.CLabel lblSpace31;
    private com.see.truetransact.uicomponent.CLabel lblSpace32;
    private com.see.truetransact.uicomponent.CLabel lblSpace33;
    private com.see.truetransact.uicomponent.CLabel lblSpace34;
    private com.see.truetransact.uicomponent.CLabel lblSpace35;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace6;
    private com.see.truetransact.uicomponent.CLabel lblSpace7;
    private com.see.truetransact.uicomponent.CLabel lblSpace8;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblStatus1;
    private com.see.truetransact.uicomponent.CMenuBar mbrCustomer;
    private com.see.truetransact.uicomponent.CMenuBar mbrPfMaster;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitCancel1;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitClose1;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitDelete1;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitEdit1;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitNew1;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitPrint1;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenuItem mitSave1;
    private javax.swing.JMenu mnuProcess;
    private javax.swing.JMenu mnuProcess1;
    private com.see.truetransact.uicomponent.CPanel panEmpDetails;
    private com.see.truetransact.uicomponent.CPanel panEmpTransfer;
    private com.see.truetransact.uicomponent.CPanel panEmployeeId;
    private com.see.truetransact.uicomponent.CPanel panPfClose;
    private com.see.truetransact.uicomponent.CPanel panPfMaster;
    private com.see.truetransact.uicomponent.CPanel panShareCloseTrans;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panStatus1;
    private com.see.truetransact.uicomponent.CPanel panTranferDetails;
    private com.see.truetransact.uicomponent.CPanel panTransaction;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptPrint;
    private javax.swing.JSeparator sptProcess;
    private javax.swing.JSeparator sptSave;
    private javax.swing.JToolBar tbrOperativeAcctProduct;
    private javax.swing.JToolBar tbrPfMaster;
    private com.see.truetransact.uicomponent.CDateField tdtLastInterestDate;
    private com.see.truetransact.uicomponent.CDateField tdtPfDate;
    private com.see.truetransact.uicomponent.CDateField tdtPfOpeningDate;
    private com.see.truetransact.uicomponent.CTextField txtEmpId;
    private com.see.truetransact.uicomponent.CTextField txtEmployeeId;
    private com.see.truetransact.uicomponent.CTextField txtEmployerContribution;
    private javax.swing.JTextField txtLeaveAmount;
    private com.see.truetransact.uicomponent.CTextField txtLeaveNo;
    private com.see.truetransact.uicomponent.CTextField txtMemberName;
    private com.see.truetransact.uicomponent.CTextField txtOpeningBalance;
    private com.see.truetransact.uicomponent.CTextField txtPfAccountNo;
    private com.see.truetransact.uicomponent.CTextField txtPfNomineeName;
    private com.see.truetransact.uicomponent.CTextField txtPfNomineeRelation;
    private com.see.truetransact.uicomponent.CTextField txtPfRateOfInterest;
    // End of variables declaration//GEN-END:variables

    public static void main(String[] args) {
        LeaveSurrenderUI dirBrd = new LeaveSurrenderUI();
        javax.swing.JFrame j = new javax.swing.JFrame();
        j.getContentPane().add(dirBrd);
        j.show();
//        empTran.show();


    }
}