/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * TodAllowedUI.java
 *
 * Created on feb 9, 2009, 10:53 AM
 */

package com.see.truetransact.ui.operativeaccount;

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
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.common.transaction.TransactionUI;
import com.see.truetransact.ui.transaction.common.TransDetailsUI;
import com.see.truetransact.ui.common.authorize.AuthorizeUI ;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.transferobject.product.operativeacct.OperativeAcctProductTO;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.operativeaccount.TodAllowedOB;
import com.see.truetransact.commonutil.DateUtil;
import java.util.Date;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uimandatory.MandatoryDBCheck;




/** This form is used to manipulate TodAllowedUI related functionality
 * @author swaroop
 */
public class TodAllowedUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer, UIMandatoryField{
    final int AUTHORIZE=3, CANCEL=0 ;
    int viewType=-1;
    private HashMap mandatoryMap;
    private TodAllowedOB observable;
    private final static Logger log = Logger.getLogger(TodAllowedUI.class);
    TodAllowedRB todAllowedRB = new TodAllowedRB();
    private String prodType="";
    java.util.ResourceBundle resourceBundle, objMandatoryRB;
    private Date currDt = null;
    private TransDetailsUI transDetails = null;
    
        
    /** Creates new form TodAllowedUI */
    public TodAllowedUI() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        initStartUp();
        transDetails = new TransDetailsUI(panLableValues);
    }
    
    private void initStartUp(){
        setMandatoryHashMap();
        setFieldNames();
        internationalize();
        observable = new TodAllowedOB();
        observable.addObserver(this);
        initComponentData();
        enableDisable(false);
        setButtonEnableDisable();
        setMaxLength();
         objMandatoryRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.operativeaccount.TodAllowedMRB", ProxyParameters.LANGUAGE);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panProductID);
         setHelpMessage();
    }
    
    private void setMaxLength() {
         txtTODAllowed.setMaxLength(16);
         txtTODAllowed.setValidation(new CurrencyValidation(14,2));
         txtRemarks.setMaxLength(64);
         txtPermissionRefNo.setMaxLength(16);
         txtAccountNumber.setMaxLength(16);
         txtAccountNumber.setAllowAll(true);
    }
    
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboProductID", new Boolean(true));
        mandatoryMap.put("txtAccountNumber", new Boolean(true));
        mandatoryMap.put("txtTODAllowed", new Boolean(true));
        mandatoryMap.put("dtdToDate", new Boolean(true));
        mandatoryMap.put("dtdFromDate", new Boolean(true));
        mandatoryMap.put("cboPermitedBy", new Boolean(true));
        mandatoryMap.put("dtdPermittedDt", new Boolean(true));
        mandatoryMap.put("cboProdType", new Boolean(true));
        mandatoryMap.put("txtRepayedWithin", new Boolean(true));
        mandatoryMap.put("cboRepayedWithin", new Boolean(true));
    }
    
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    
    
    /****************** NEW METHODS *****************/
    
    private void updateAuthorizeStatus(String authorizeStatus) {
        if (viewType != AUTHORIZE){
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put("USER_ID", TrueTransactMain.USER_ID);
            whereMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            mapParam.put(CommonConstants.MAP_NAME, "getSelectTodAuthorizeTOList");
            viewType = AUTHORIZE;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            //            isFilled = false;
            authorizeUI.show();
            btnSave.setEnabled(false);
            observable.setStatus();
            lblStatus.setText(observable.getLblStatus());
        } else if (viewType == AUTHORIZE){
            ArrayList arrList = new ArrayList();
            HashMap authorizeMap = new HashMap();
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("TRANS_ID", observable.getTransaction_id());
            singleAuthorizeMap.put("TOD_AMOUNT", observable.getTxtTODAllowed());
            singleAuthorizeMap.put("ACT_NUM", observable.getTxtAccountNumber());
            singleAuthorizeMap.put("PROD_TYPE", CommonUtil.convertObjToStr(observable.getCbmProdType().getKeyForSelected()));
            arrList.add(singleAuthorizeMap);
            
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorize(authorizeMap);
            
        }
    }
    
    public void authorize(HashMap map) {
        System.out.println("Authorize Map : " + map);
        
        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
            observable.setResult(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.set_authorizeMap(map);
            observable.doAction();
            btnCancelActionPerformed(null);
            observable.setStatus();
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
            
        }
    }
    public void setHelpMessage() {
        //        cboProductId.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProductId"));
        cboProdType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProdType"));
        txtAccountNumber.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAccountNumber"));
        rdoSingleTransaction.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoSingleTransaction"));
        txtTODAllowed.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTODAllowed"));
        cboPermitedBy.setHelpMessage(lblMsg, objMandatoryRB.getString("cboPermitedBy"));
        txtPermissionRefNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPermissionRefNo"));
        txtRemarks.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRemarks"));
        dtdFromDate.setHelpMessage(lblMsg, objMandatoryRB.getString("dtdFromDate"));
        dtdPermittedDt.setHelpMessage(lblMsg, objMandatoryRB.getString("dtdPermittedDt"));
        dtdToDate.setHelpMessage(lblMsg, objMandatoryRB.getString("dtdToDate"));
    }
    
    /************ END OF NEW METHODS ***************/
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoTransactionType = new com.see.truetransact.uicomponent.CButtonGroup();
        panAccountInfo = new com.see.truetransact.uicomponent.CPanel();
        panAccountInfoInner = new com.see.truetransact.uicomponent.CPanel();
        panProductID = new com.see.truetransact.uicomponent.CPanel();
        lblAccountNumber = new com.see.truetransact.uicomponent.CLabel();
        lblTODAllowed = new com.see.truetransact.uicomponent.CLabel();
        txtTODAllowed = new com.see.truetransact.uicomponent.CTextField();
        lblFromDate = new com.see.truetransact.uicomponent.CLabel();
        lblPermissionRefNo = new com.see.truetransact.uicomponent.CLabel();
        lblAcctName = new com.see.truetransact.uicomponent.CLabel();
        lblAcctNameDisplay = new com.see.truetransact.uicomponent.CLabel();
        lblToDate = new com.see.truetransact.uicomponent.CLabel();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        panAcctNum = new com.see.truetransact.uicomponent.CPanel();
        txtAccountNumber = new com.see.truetransact.uicomponent.CTextField();
        btnAccountNumber = new com.see.truetransact.uicomponent.CButton();
        dtdFromDate = new com.see.truetransact.uicomponent.CDateField();
        dtdPermittedDt = new com.see.truetransact.uicomponent.CDateField();
        cboProductId = new com.see.truetransact.uicomponent.CComboBox();
        lblProd = new com.see.truetransact.uicomponent.CLabel();
        panTransactionType = new com.see.truetransact.uicomponent.CPanel();
        rdoSingleTransaction = new com.see.truetransact.uicomponent.CRadioButton();
        rdoRunningLimit = new com.see.truetransact.uicomponent.CRadioButton();
        lblTypeOfTOD = new com.see.truetransact.uicomponent.CLabel();
        cboPermitedBy = new com.see.truetransact.uicomponent.CComboBox();
        lblPermitedBy = new com.see.truetransact.uicomponent.CLabel();
        dtdToDate = new com.see.truetransact.uicomponent.CDateField();
        lblPermittedDt = new com.see.truetransact.uicomponent.CLabel();
        txtRemarks = new com.see.truetransact.uicomponent.CTextField();
        txtPermissionRefNo = new com.see.truetransact.uicomponent.CTextField();
        lblTransactionIDValue = new com.see.truetransact.uicomponent.CLabel();
        lblTransactionID = new com.see.truetransact.uicomponent.CLabel();
        cboProdType = new com.see.truetransact.uicomponent.CComboBox();
        lblProdType = new com.see.truetransact.uicomponent.CLabel();
        lblCreatedDt = new com.see.truetransact.uicomponent.CLabel();
        lblCreatedDateDesc = new com.see.truetransact.uicomponent.CLabel();
        lblrepayedDateValue = new com.see.truetransact.uicomponent.CLabel();
        cboRepayedWithin = new com.see.truetransact.uicomponent.CComboBox();
        txtRepayedWithin = new com.see.truetransact.uicomponent.CTextField();
        lblrepayedDate = new com.see.truetransact.uicomponent.CLabel();
        lblrepayedWithin = new com.see.truetransact.uicomponent.CLabel();
        sptAccountInfo = new com.see.truetransact.uicomponent.CSeparator();
        panAccountHead = new com.see.truetransact.uicomponent.CPanel();
        panLableValues = new com.see.truetransact.uicomponent.CPanel();
        panTransaction = new com.see.truetransact.uicomponent.CPanel();
        tbrOperativeAcctProduct = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace17 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace18 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
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
        lblSpace23 = new com.see.truetransact.uicomponent.CLabel();
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
        setMaximumSize(new java.awt.Dimension(800, 600));
        setMinimumSize(new java.awt.Dimension(800, 600));
        setPreferredSize(new java.awt.Dimension(800, 600));

        panAccountInfo.setLayout(new java.awt.GridBagLayout());

        panAccountInfoInner.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panAccountInfoInner.setMaximumSize(new java.awt.Dimension(728, 470));
        panAccountInfoInner.setMinimumSize(new java.awt.Dimension(728, 470));
        panAccountInfoInner.setPreferredSize(new java.awt.Dimension(728, 470));
        panAccountInfoInner.setLayout(new java.awt.GridBagLayout());

        panProductID.setMaximumSize(new java.awt.Dimension(450, 120));
        panProductID.setMinimumSize(new java.awt.Dimension(450, 120));
        panProductID.setPreferredSize(new java.awt.Dimension(450, 120));
        panProductID.setLayout(new java.awt.GridBagLayout());

        lblAccountNumber.setText("Account Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipady = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(lblAccountNumber, gridBagConstraints);

        lblTODAllowed.setText("TOD Amount Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.ipadx = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(lblTODAllowed, gridBagConstraints);

        txtTODAllowed.setMaxLength(4);
        txtTODAllowed.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTODAllowed.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(txtTODAllowed, gridBagConstraints);

        lblFromDate.setText("From Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(lblFromDate, gridBagConstraints);

        lblPermissionRefNo.setText("Permission Ref No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(lblPermissionRefNo, gridBagConstraints);

        lblAcctName.setText("Acct Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(lblAcctName, gridBagConstraints);

        lblAcctNameDisplay.setMinimumSize(new java.awt.Dimension(225, 21));
        lblAcctNameDisplay.setPreferredSize(new java.awt.Dimension(225, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(lblAcctNameDisplay, gridBagConstraints);

        lblToDate.setText("To Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(lblToDate, gridBagConstraints);

        lblRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(lblRemarks, gridBagConstraints);

        panAcctNum.setLayout(new java.awt.GridBagLayout());

        txtAccountNumber.setMaxLength(10);
        txtAccountNumber.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAccountNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAccountNumberActionPerformed(evt);
            }
        });
        txtAccountNumber.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtAccountNumberFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAccountNumberFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAcctNum.add(txtAccountNumber, gridBagConstraints);

        btnAccountNumber.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccountNumber.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnAccountNumber.setMinimumSize(new java.awt.Dimension(25, 25));
        btnAccountNumber.setPreferredSize(new java.awt.Dimension(25, 25));
        btnAccountNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccountNumberActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAcctNum.add(btnAccountNumber, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panProductID.add(panAcctNum, gridBagConstraints);

        dtdFromDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                dtdFromDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panProductID.add(dtdFromDate, gridBagConstraints);

        dtdPermittedDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                dtdPermittedDtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panProductID.add(dtdPermittedDt, gridBagConstraints);

        cboProductId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProductId.setPopupWidth(225);
        cboProductId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProductIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panProductID.add(cboProductId, gridBagConstraints);

        lblProd.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(lblProd, gridBagConstraints);

        panTransactionType.setMinimumSize(new java.awt.Dimension(165, 23));
        panTransactionType.setPreferredSize(new java.awt.Dimension(165, 23));
        panTransactionType.setLayout(new java.awt.GridBagLayout());

        rdoSingleTransaction.setText("Single transaction");
        rdoSingleTransaction.setMinimumSize(new java.awt.Dimension(85, 27));
        rdoSingleTransaction.setPreferredSize(new java.awt.Dimension(85, 27));
        rdoSingleTransaction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoSingleTransactionActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.ipadx = 44;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 5);
        panTransactionType.add(rdoSingleTransaction, gridBagConstraints);

        rdoRunningLimit.setText("Running Limit");
        rdoRunningLimit.setMargin(new java.awt.Insets(2, 5, 2, 2));
        rdoRunningLimit.setMaximumSize(new java.awt.Dimension(90, 27));
        rdoRunningLimit.setMinimumSize(new java.awt.Dimension(90, 27));
        rdoRunningLimit.setPreferredSize(new java.awt.Dimension(90, 27));
        rdoRunningLimit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoRunningLimitActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipadx = 58;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panTransactionType.add(rdoRunningLimit, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 105;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(panTransactionType, gridBagConstraints);

        lblTypeOfTOD.setText("Type Of TOD");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(lblTypeOfTOD, gridBagConstraints);

        cboPermitedBy.setMinimumSize(new java.awt.Dimension(100, 21));
        cboPermitedBy.setPopupWidth(225);
        cboPermitedBy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboPermitedByActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panProductID.add(cboPermitedBy, gridBagConstraints);

        lblPermitedBy.setText("Permited By");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(lblPermitedBy, gridBagConstraints);

        dtdToDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                dtdToDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panProductID.add(dtdToDate, gridBagConstraints);

        lblPermittedDt.setText("Permitted Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(lblPermittedDt, gridBagConstraints);

        txtRemarks.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panProductID.add(txtRemarks, gridBagConstraints);

        txtPermissionRefNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panProductID.add(txtPermissionRefNo, gridBagConstraints);

        lblTransactionIDValue.setText("[xxxxxxxxxx]");
        lblTransactionIDValue.setName("lblTransactionIDValue");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductID.add(lblTransactionIDValue, gridBagConstraints);

        lblTransactionID.setText("Transaction Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(lblTransactionID, gridBagConstraints);

        cboProdType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductID.add(cboProdType, gridBagConstraints);

        lblProdType.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(lblProdType, gridBagConstraints);

        lblCreatedDt.setText("Created Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panProductID.add(lblCreatedDt, gridBagConstraints);

        lblCreatedDateDesc.setMaximumSize(new java.awt.Dimension(100, 21));
        lblCreatedDateDesc.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductID.add(lblCreatedDateDesc, gridBagConstraints);

        lblrepayedDateValue.setMinimumSize(new java.awt.Dimension(150, 21));
        lblrepayedDateValue.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(lblrepayedDateValue, gridBagConstraints);

        cboRepayedWithin.setMinimumSize(new java.awt.Dimension(100, 21));
        cboRepayedWithin.setPopupWidth(100);
        cboRepayedWithin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboRepayedWithinActionPerformed(evt);
            }
        });
        cboRepayedWithin.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboRepayedWithinFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panProductID.add(cboRepayedWithin, gridBagConstraints);

        txtRepayedWithin.setMaxLength(4);
        txtRepayedWithin.setMinimumSize(new java.awt.Dimension(100, 21));
        txtRepayedWithin.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(txtRepayedWithin, gridBagConstraints);

        lblrepayedDate.setText("Repay Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(lblrepayedDate, gridBagConstraints);

        lblrepayedWithin.setText("To be Repayed Within");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProductID.add(lblrepayedWithin, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panAccountInfoInner.add(panProductID, gridBagConstraints);

        sptAccountInfo.setOrientation(javax.swing.SwingConstants.VERTICAL);
        sptAccountInfo.setPreferredSize(new java.awt.Dimension(5, 5));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weightx = 1.0;
        panAccountInfoInner.add(sptAccountInfo, gridBagConstraints);

        panAccountHead.setMaximumSize(new java.awt.Dimension(300, 100));
        panAccountHead.setMinimumSize(new java.awt.Dimension(300, 100));
        panAccountHead.setPreferredSize(new java.awt.Dimension(300, 100));
        panAccountHead.setLayout(new java.awt.GridBagLayout());

        panLableValues.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panLableValues.setMinimumSize(new java.awt.Dimension(200, 308));
        panLableValues.setPreferredSize(new java.awt.Dimension(200, 308));
        panLableValues.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 0);
        panAccountHead.add(panLableValues, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAccountInfoInner.add(panAccountHead, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountInfo.add(panAccountInfoInner, gridBagConstraints);

        panTransaction.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountInfo.add(panTransaction, gridBagConstraints);

        getContentPane().add(panAccountInfo, java.awt.BorderLayout.CENTER);

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

        lblSpace17.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace17.setText("     ");
        lblSpace17.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace17);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnEdit);

        lblSpace18.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace18.setText("     ");
        lblSpace18.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace18.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace18.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace18);

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

        lblSpace19.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace19.setText("     ");
        lblSpace19.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace19.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace19.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace19);

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

        lblSpace20.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace20.setText("     ");
        lblSpace20.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace20.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace20.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace20);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnException);

        lblSpace21.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace21.setText("     ");
        lblSpace21.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace21);

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

        lblSpace22.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace22.setText("     ");
        lblSpace22.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace22);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnClose);

        lblSpace23.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace23.setText("     ");
        lblSpace23.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace23.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace23.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace23);

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

    private void cboRepayedWithinFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboRepayedWithinFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_cboRepayedWithinFocusLost

    private void cboRepayedWithinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboRepayedWithinActionPerformed
        // TODO add your handling code here:
        String repayFormat = ((ComboBoxModel)cboRepayedWithin.getModel()).getKeyForSelected().toString();
        Date fromDt = DateUtil.getDateMMDDYYYY(dtdFromDate.getDateValue());
        Date toDt = DateUtil.getDateMMDDYYYY(dtdToDate.getDateValue());
        if(fromDt != null && toDt != null){
            long diffDay = DateUtil.dateDiff(fromDt,toDt);
            int repayDay = CommonUtil.convertObjToInt(txtRepayedWithin.getText());
            String repayDate = "";
            if(!repayFormat.equals("") && repayFormat.equals("DAYS"))
                repayDate = CommonUtil.convertObjToStr(DateUtil.addDays(toDt,repayDay));
            if(!repayFormat.equals("") && repayFormat.equals("MONTHS"))
                repayDate = CommonUtil.convertObjToStr(DateUtil.addDays(toDt,(repayDay * 30)));            
            if(!repayFormat.equals("") && repayFormat.equals("YEARS"))
                repayDate = CommonUtil.convertObjToStr(DateUtil.addDays(toDt,(repayDay * 365)));            
            if(repayFormat.equals(""))
                repayDate = "";
            lblrepayedDateValue.setText(String.valueOf(repayDate));
        }

    }//GEN-LAST:event_cboRepayedWithinActionPerformed

    private void btnDateChangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDateChangeActionPerformed
        // TODO add your handling code here:
          observable.setActionType(ClientConstants.ACTIONTYPE_EDITTI);
          if (viewType!=1){
              popUp();
        viewType = 1;
          }
          else if (viewType==1){
              HashMap where =new HashMap();
              where.put("TRANS_ID",lblTransactionIDValue.getText());
              String ent_dt=CommonUtil.convertObjToStr(dtdToDate.getDateValue());
              Date TODAY_DT=DateUtil.getDateMMDDYYYY(ent_dt);  
              Date toDt=(Date) currDt.clone();
              toDt.setDate(TODAY_DT.getDate());
              toDt.setMonth(TODAY_DT.getMonth());
              toDt.setYear(TODAY_DT.getYear());
              where.put("CHANGED_DATE",toDt);
              ClientUtil.execute("updateChangedDate", where);
              viewType = -1;
              btnCancelActionPerformed(null);
          }
       btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        lblStatus.setText(observable.getLblStatus());
    }//GEN-LAST:event_btnDateChangeActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        btnView.setEnabled(false);
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        popUp();
         btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        lblStatus.setText(observable.getLblStatus());
        btnDateChange.setEnabled(false);
        String prodType = ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString();
        if(rdoSingleTransaction.isSelected() == true && prodType.equals("OA"))
            enableDisableRepayDetails(true);
        else
           enableDisableRepayDetails(false);
    }//GEN-LAST:event_btnViewActionPerformed

    private void txtAccountNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAccountNumberActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAccountNumberActionPerformed

    private void txtAccountNumberFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccountNumberFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAccountNumberFocusGained

    private void rdoRunningLimitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoRunningLimitActionPerformed
        // TODO add your handling code here:
        rdoSingleTransaction.setSelected(false);
        if(rdoRunningLimit.isSelected() == true){
            txtRepayedWithin.setEnabled(false);
            cboRepayedWithin.setEditable(false);
            cboRepayedWithin.setEnabled(false);
            txtRepayedWithin.setText("");
            cboRepayedWithin.setSelectedItem("");
            lblrepayedDateValue.setText("");
            lblrepayedWithin.setVisible(false);
            txtRepayedWithin.setVisible(false);
            cboRepayedWithin.setVisible(false);
            lblrepayedDate.setVisible(false);
            lblrepayedDateValue.setVisible(false);
        }
    }//GEN-LAST:event_rdoRunningLimitActionPerformed

    private void cboProdTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdTypeActionPerformed
        // TODO add your handling code here:
        observable.setCboProdType((String) cboProdType.getSelectedItem());
        String prodType = ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString();
        observable.setCbmProductId(prodType);
        cboProductId.setModel(observable.getCbmProductId());
        clearProdFields();
        transDetails.setTransDetails(null, null, null);
        if(!prodType.equals("OA"))
             enableDisableRepayDetails(false);
        else
          enableDisableRepayDetails(true);
    }//GEN-LAST:event_cboProdTypeActionPerformed

    private void dtdPermittedDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_dtdPermittedDtFocusLost
        // TODO add your handling code here:
        ClientUtil.validateLTDate(dtdPermittedDt);
        
    }//GEN-LAST:event_dtdPermittedDtFocusLost

    private void dtdToDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_dtdToDateFocusLost
        // TODO add your handling code here:
          String to_dt=CommonUtil.convertObjToStr(dtdToDate.getDateValue());
          Date cur_date=(Date) currDt.clone();
          String from_dt=CommonUtil.convertObjToStr(dtdFromDate.getDateValue());
          Date dt=DateUtil.getDateMMDDYYYY(from_dt);
          ClientUtil.validateToDate(dtdToDate, DateUtil.getStringDate(DateUtil.addDays(cur_date, -1))); 
          Date from=DateUtil.getDateMMDDYYYY(from_dt);
          Date to=DateUtil.getDateMMDDYYYY(to_dt);
          if (from!=null && to!=null && DateUtil.dateDiff(from,to)<0) {
            displayAlert("To date should be greater than From Date...");
            dtdToDate.setDateValue("");
        }
    }//GEN-LAST:event_dtdToDateFocusLost

    private void dtdFromDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_dtdFromDateFocusLost
        // TODO add your handling code here:
          String from_dt=CommonUtil.convertObjToStr(dtdFromDate.getDateValue());
          String to_dt=CommonUtil.convertObjToStr(dtdToDate.getDateValue());
          Date cur_date=(Date) currDt.clone();
          ClientUtil.validateToDate(dtdFromDate, DateUtil.getStringDate(DateUtil.addDays(cur_date, -1)));
         
    }//GEN-LAST:event_dtdFromDateFocusLost

    private void cboPermitedByActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPermitedByActionPerformed
        // TODO add your handling code here:
         observable.setCboPermitedBy((String) cboPermitedBy.getSelectedItem());
    }//GEN-LAST:event_cboPermitedByActionPerformed

    private void cboProductIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProductIdActionPerformed
        // TODO add your handling code here:
        observable.setCboProductId((String) cboProductId.getSelectedItem());
        clearProdFields();
        transDetails.setTransDetails(null, null,null);
    }//GEN-LAST:event_cboProductIdActionPerformed

    private void rdoSingleTransactionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoSingleTransactionActionPerformed
        // TODO add your handling code here:
        rdoRunningLimit.setSelected(false);
         String prodType = ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString();
        if(rdoSingleTransaction.isSelected() == true && prodType.equals("OA"))
             enableDisableRepayDetails(true);
        else
            enableDisableRepayDetails(false);
    }//GEN-LAST:event_rdoSingleTransactionActionPerformed
                
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        updateAuthorizeStatus(CommonConstants.STATUS_EXCEPTION);
        btnCancel.setEnabled(true);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
       observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        updateAuthorizeStatus(CommonConstants.STATUS_REJECTED);
         btnCancel.setEnabled(true);
         btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        updateAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnCancel.setEnabled(true);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        mitCloseActionPerformed(evt);
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {
        cifClosingAlert();
    }
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
       observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        popUp();
        btnAccountNumber.setEnabled(false);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        lblStatus.setText(observable.getLblStatus());
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
    observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
     popUp();
    lblStatus.setText(observable.getLblStatus());
     String prodType = ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString();
        if(rdoSingleTransaction.isSelected() == true && prodType.equals("OA"))
            enableDisableRepayDetails(true);
        else
           enableDisableRepayDetails(false);
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
//        if (observable.getActionType()!=ClientConstants.ACTIONTYPE_AUTHORIZE) {
//            observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
//        }
        viewType = CANCEL ;
//       super.removeEditLock(txtAccountNumber.getText());
         transDetails.setTransDetails(null,null,null);
         observable.resetForm();  
         setButtonEnableDisable();
         btnView.setEnabled(true);
         btnAccountNumber.setEnabled(false);
         ClientUtil.enableDisable(this, false);
         lblStatus.setText("");
         rdoRunningLimit.setSelected(false);
         rdoSingleTransaction.setSelected(false);
         lblCreatedDateDesc.setText("");
         setModified(false);
         lblrepayedDateValue.setText("");
         cboRepayedWithin.setEnabled(false);
    }//GEN-LAST:event_btnCancelActionPerformed
    
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
         updateOBFields();
          String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panProductID);
        if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0 ){
            displayAlert(mandatoryMessage);
        }
        else{ 
            HashMap where = new HashMap();
            String ACT_NUM=txtAccountNumber.getText();
            Date TODAY_DT=(Date) currDt.clone();
            Date toDt=(Date) currDt.clone();
            toDt.setDate(TODAY_DT.getDate());
            toDt.setMonth(TODAY_DT.getMonth());
            toDt.setYear(TODAY_DT.getYear());
            where.put("TODAY_DT",toDt);
            where.put("ACT_NUM",ACT_NUM);
            List list = ClientUtil.executeQuery("getTypeOfTod",where);
            HashMap hash = new HashMap();
            if(list!=null && list.size()>0){
            hash=(HashMap)list.get(0);
            String type_of_tod = CommonUtil.convertObjToStr(hash.get("TYPE_OF_TOD"));
            boolean tod=observable.isRdoSingleTransaction();      
            if((tod==true && type_of_tod.equals("RUNNING"))||(tod==false && type_of_tod.equals("SINGLE")))
            {
             ClientUtil.displayAlert(type_of_tod+" "+"limit already exists for this account for the current period..Change the tod_type to"+" "+type_of_tod);
            }
            else{
                 savePerformed(); 
            }
            }
            else{
                   savePerformed();  
            }
        }
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        enableDisable(true);
        setButtonEnableDisable();
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.resetForm();
        btnAccountNumber.setEnabled(true);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        rdoSingleTransaction.setSelected(true);
        
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
    
    private void txtAccountNumberFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccountNumberFocusLost
        String ACCOUNTNO = (String) txtAccountNumber.getText();
        if (ACCOUNTNO.length()>0) {
            if (observable.checkAcNoWithoutProdType(ACCOUNTNO)) {
                cboProductIdActionPerformed(null);
                txtAccountNumber.setText(observable.getTxtAccountNumber());
                ACCOUNTNO = (String) txtAccountNumber.getText();
//                txtAccountNumber.setText(ACCOUNTNO);
                viewType = 1;
                HashMap where = new HashMap();
                where.put("ACC_NUM",ACCOUNTNO);
                List lst = ClientUtil.executeQuery("getAccountNumberName"+((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString(),where);
                 if(lst!=null && lst.size()>0){
                     where=(HashMap)lst.get(0);
                     where.put("ACCOUNTNO",ACCOUNTNO);
                     fillData(where);
                     txtAccountNumber.setText(ACCOUNTNO);
                     lblAcctNameDisplay.setText(CommonUtil.convertObjToStr(where.get("CUSTOMER_NAME")));
                 }else{
                    ClientUtil.displayAlert("Invalid Number");
                    txtAccountNumber.setText("");
                    lblAcctNameDisplay.setText("");
                    transDetails.setTransDetails(null, null, null);
                }       
            } else {
                ClientUtil.showAlertWindow("Invalid Account No.");
                txtAccountNumber.setText("");
                return;
            }
        }
//        String ACC_NUM= txtAccountNumber.getText();
//        HashMap where =new HashMap();
//        where.put("ACC_NUM",ACC_NUM);
//        List lst = ClientUtil.executeQuery("getAccountNumberName"+((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString(),where);
//         if(lst!=null && lst.size()>0){
//             where=(HashMap)lst.get(0);
//             where.put("ACCOUNTNO",ACC_NUM);
//             fillData(where);
//             txtAccountNumber.setText(ACC_NUM);
//             lblAcctNameDisplay.setText(CommonUtil.convertObjToStr(where.get("CUSTOMER_NAME")));
//         }else{
//            ClientUtil.displayAlert("Invalid Number");
//            txtAccountNumber.setText("");
//            lblAcctNameDisplay.setText("");
//            transDetails.setTransDetails(null, null, null);
//        }       
    }//GEN-LAST:event_txtAccountNumberFocusLost
    
    private void btnAccountNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccountNumberActionPerformed
        viewType =1;
        popUp();
        btnSave.setEnabled(true);
        btnCancel.setEnabled(true);
    }//GEN-LAST:event_btnAccountNumberActionPerformed
        
    /** To populate Comboboxes */
    private void initComponentData() {
        cboProdType.setModel(observable.getCbmProdType());
        cboPermitedBy.setModel(observable.getCbmPermitedBy());
        cboRepayedWithin.setModel(observable.getCbmRepayPeriod());
    }
    
    /** To display a popUp window for viewing existing data */
    private void popUp(){
        HashMap testMap = null;
        //To display customer info based on the selected ProductID
        
        if( observable.getActionType() == ClientConstants.ACTIONTYPE_NEW ||viewType==1){
            testMap = accountViewMap();
            new ViewAll(this, testMap, true).show();
            viewType =-1;
        }
        else if( observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ||observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE){
            testMap= new HashMap();
            if (viewType==1) {
                testMap = accountViewMap();
                new ViewAll(this, testMap, true).show();
            }
            testMap.put("MAPNAME", "getSelectAccountListForTODForEdit");
            final HashMap whereMap = new HashMap();
            whereMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            testMap.put(CommonConstants.MAP_WHERE, whereMap);
            new ViewAll(this, testMap, true).show();
        }else if(observable.getActionType()==ClientConstants.ACTIONTYPE_VIEW){
             testMap= new HashMap();
            testMap.put("MAPNAME", "getSelectAccountListForTODForView");
            final HashMap whereMap = new HashMap();
            whereMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            testMap.put(CommonConstants.MAP_WHERE, whereMap);
            new ViewAll(this, testMap, true).show();
        }
        else if(observable.getActionType()==ClientConstants.ACTIONTYPE_EDITTI){
             testMap= new HashMap();
            testMap.put("MAPNAME", "getSelectListForDtChange");
            final HashMap whereMap = new HashMap();
            whereMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            Date TODAY_DT=(Date) currDt.clone();
            Date toDt=(Date) currDt.clone();
            toDt.setDate(TODAY_DT.getDate());
            toDt.setMonth(TODAY_DT.getMonth());
            toDt.setYear(TODAY_DT.getYear());
            whereMap.put("TODAY_DT",toDt);
            testMap.put(CommonConstants.MAP_WHERE, whereMap);
            new ViewAll(this, testMap, true).show();
        }
    }
    private void clearPreviousAccountDetails(){
        
    }
    
    /** Called by the Popup window created thru popUp method */
    public void fillData(Object obj) {
        try{
            final HashMap hash = (HashMap) obj;
            System.out.println("filldata####"+hash);
            if( observable.getActionType() == ClientConstants.ACTIONTYPE_NEW ){
                fillDataNew(hash);
                
            }
            else if( observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE){
                fillDataEdit(hash);
                setButtonEnableDisable();
                observable.setStatus();
                checkForEdit();
            }  else if( observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT 
              || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION || observable.getActionType()==ClientConstants.ACTIONTYPE_VIEW){
                fillDataEdit(hash);
                setButton4Authorize();
            }
            else if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDITTI){
                fillDataEdit(hash);
                setButton4Authorize();
                btnView.setEnabled(false);
                dtdToDate.setEnabled(true);
            }
            String ACT_NUM=(String)hash.get("ACCOUNTNO");
            if(ACT_NUM.equals(""))
                ACT_NUM=(String)hash.get("ACT_NUM");
            String prod_type= (String)((ComboBoxModel)cboProdType.getModel()).getKeyForSelected();
            if(prod_type.equals(""))
                prod_type =(String)hash.get("PROD_TYPE");
            transDetails.setTransDetails(null, null, null);
            transDetails.setTransDetails(prod_type, ProxyParameters.BRANCH_ID, ACT_NUM);
        }catch(Exception e){
            log.error(e);
        }
    }
    
    private void checkForEdit(){
        // To enable disable controls for EDIT operation
        if( observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
            enableDisable(true);
            btnAccountNumber.setEnabled(true);
        }
        else{
            enableDisable(false);
            btnAccountNumber.setEnabled(false);
        }
    }
    
    
    
    /** To fillData for a new entry */
    private void fillDataNew(HashMap hash){
        txtAccountNumber.setText((String)hash.get("ACCOUNTNO"));
        lblAcctNameDisplay.setText((String)hash.get("CUSTOMERNAME"));
        String ACCOUNTNO=(String)hash.get("ACCOUNTNO");
    }
    
    /** To fillData for existing entry */
    private void fillDataEdit(HashMap hash){
        final HashMap where = (HashMap) hash;
        where.put("USER_ID", ProxyParameters.USER_ID);
        where.put("WHERE", hash.get("TRANS_ID"));
        observable.getData(where);
    }
    
    /** To get popUp data for a new entry */
    private HashMap accountViewMap(){
        final HashMap testMap = new HashMap();
        testMap.put("MAPNAME", "Cash.getAccountList"
        + ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString());
        final HashMap whereMap = new HashMap();
        whereMap.put("SELECTED_BRANCH", TrueTransactMain.BRANCH_ID);
        whereMap.put("PROD_ID",(String)(observable.getCbmProductId()).getKeyForSelected());
        testMap.put(CommonConstants.MAP_WHERE, whereMap);
        return testMap;
    }
    
    
    private void enableDisable(boolean yesno){
        ClientUtil.enableDisable(this, yesno);
    }
    
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
        btnDateChange.setEnabled(btnNew.isEnabled());
    }
    
    private void setButton4Authorize() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(btnNew.isEnabled());
        btnDelete.setEnabled(btnNew.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());
        
        btnSave.setEnabled(btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        btnAccountNumber.setEnabled(false);
    }
    
    public void update(Observable observed, Object arg) {
        //       cboProductId.setSelectedItem( (observable.getCbmProductId()).getDataForKey(observable.getCboProductId()));
        txtAccountNumber.setText(observable.getTxtAccountNumber());
        txtPermissionRefNo.setText(observable.getTxtPermissionRefNo());
        txtRemarks.setText(observable.getTxtRemarks());
        txtTODAllowed.setText(observable.getTxtTODAllowed());
        rdoSingleTransaction.setSelected(observable.isRdoSingleTransaction());
        rdoRunningLimit.setSelected(observable.isRdoRunningLimit());
        dtdFromDate.setDateValue(observable.getDtdFromDate());
        dtdToDate.setDateValue(observable.getDtdToDate());
        dtdPermittedDt.setDateValue(observable.getDtdPermittedDt());
        cboPermitedBy.setSelectedItem( (observable.getCbmPermitedBy()).getDataForKey(observable.getCboPermitedBy()));
        lblAcctNameDisplay.setText(observable.getTxtAcctName());
        lblTransactionIDValue.setText(observable.getTransaction_id());
        txtRepayedWithin.setText(observable.getTxtRepayPeriod());
        cboRepayedWithin.setSelectedItem((observable.getCbmRepayPeriod()).getDataForKey(observable.getCboRepayPeriod()));
        //       cboProdType.setSelectedItem( (observable.getCbmProdType()).getDataForKey(observable.getCboProdType()));
        if (observable.getCbmProductId()!=null)
            cboProductId.setModel(observable.getCbmProductId());
        lblStatus.setText(observable.getLblStatus());
        lblCreatedDateDesc.setText(observable.getCreatedDt());
    }
    
    public void updateOBFields() {
        observable.setCboProductId((String)(((ComboBoxModel)(cboProductId).getModel())).getKeyForSelected());
        observable.setTxtAccountNumber(txtAccountNumber.getText());
        observable.setTxtPermissionRefNo(txtPermissionRefNo.getText());
        observable.setTxtRemarks(txtRemarks.getText());
        observable.setTxtTODAllowed(txtTODAllowed.getText());
        observable.setRdoSingleTransaction(rdoSingleTransaction.isSelected());
        observable.setRdoRunningLimit(rdoRunningLimit.isSelected());
        observable.setDtdFromDate(dtdFromDate.getDateValue());
        observable.setDtdToDate(dtdToDate.getDateValue());
        observable.setDtdPermittedDt(dtdPermittedDt.getDateValue());
        observable.setCboPermitedBy((String)(((ComboBoxModel)(cboPermitedBy).getModel())).getKeyForSelected());
        observable.setTxtAcctName(lblAcctNameDisplay.getText());
        observable.setTxtRepayPeriod(CommonUtil.convertObjToStr(txtRepayedWithin.getText()));
        observable.setCboRepayPeriod((String)((ComboBoxModel)(cboRepayedWithin.getModel())).getKeyForSelected());
        //      observable.setCboProdType((String)(((ComboBoxModel)(cboProdType).getModel())).getKeyForSelected());
        observable.setCboProdType((String) cboProdType.getSelectedItem());
        if(lblrepayedDateValue.getText().equals(""))
        observable.setLblrepayedDateValue(DateUtil.getDateMMDDYYYY(dtdToDate.getDateValue()));
        else
        observable.setLblrepayedDateValue(DateUtil.getDateMMDDYYYY(lblrepayedDateValue.getText())); 
    }
    
    private void savePerformed(){
            observable.doAction() ;
            HashMap lockMap = new HashMap();
            ArrayList lst = new ArrayList();
            lst.add("TRANS_ID");
            lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
            if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW) {
                if (observable.getProxyReturnMap()!=null) {
                    if (observable.getProxyReturnMap().containsKey("TRANS_ID")) {
                        lst=(ArrayList)observable.getProxyReturnMap().get("TRANS_ID");
                        for(int i=0;i<lst.size();i++) {
                            lockMap.put("TRANS_ID",lst.get(i));
                            setEditLockMap(lockMap);
                            setEditLock();
                        }
                    }
                }
            }
            observable.resetForm();
            enableDisable(false);
            setButtonEnableDisable();
            btnAccountNumber.setEnabled(false);
            lblStatus.setText(observable.getLblStatus());
            cboRepayedWithin.setEnabled(false);

            viewType = -1;
            ClientUtil.enableDisable(this, false);
            transDetails.setTransDetails(null,null,null);
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
            //__ Make the Screen Closable..
            setModified(false);
            
            observable.ttNotifyObservers();
    }
    private void setFieldNames() {
        cboProductId.setName("cboProductId");
        btnAccountNumber.setName("btnAccountNumber");
        lblAccountNumber.setName("lblAccountNumber");
        lblProd.setName("lblProductId");
        rdoSingleTransaction.setName("rdoSingleTransaction");
        rdoRunningLimit.setName("rdoRunningLimit");
        txtTODAllowed.setName("txtTODAllowed");
        lblTODAllowed.setName("lblTODAllowed");
        dtdFromDate.setName("dtdFromDate");
        dtdToDate.setName("dtdToDate");
        lblFromDate.setName("lblFromDate");
        lblToDate.setName("lblToDate");
        lblPermitedBy.setName("lblToDate");
        cboPermitedBy.setName("cboPermitedBy");
        lblPermissionRefNo.setName("lblPermissionRefNo");
        txtPermissionRefNo.setName("txtPermissionRefNo");
        dtdPermittedDt.setName("dtdPermittedDt");
        lblPermittedDt.setName("lblPermittedDt");
        lblTypeOfTOD.setName("lblTypeOfTOD");
        lblTransactionID.setName("lblTransactionID");
        lblProdType.setName("lblProdType");
        cboProdType.setName("cboProdType");
        txtAccountNumber.setName("txtAccountNumber");
        txtRepayedWithin.setName("txtRepayedWithin");
        cboRepayedWithin.setName("cboRepayedWithin");
    }
    
    private void internationalize() {
        java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.operativeaccount.TodAllowedRB", ProxyParameters.LANGUAGE);
        lblAccountNumber.setText(resourceBundle.getString("lblAccountNumber"));
        lblProd.setText(resourceBundle.getString("lblProd"));
        rdoSingleTransaction.setText(resourceBundle.getString("rdoSingleTransaction"));
        rdoRunningLimit.setText(resourceBundle.getString("rdoRunningLimit"));
        lblTODAllowed.setText(resourceBundle.getString("lblTODAllowed"));
        lblFromDate.setText(resourceBundle.getString("lblFromDate"));
        lblToDate.setText(resourceBundle.getString("lblToDate"));
        lblPermitedBy.setText(resourceBundle.getString("lblPermitedBy"));
        lblPermissionRefNo.setText(resourceBundle.getString("lblPermissionRefNo"));
        lblPermittedDt.setText(resourceBundle.getString("lblPermittedDt"));
        lblTypeOfTOD.setText(resourceBundle.getString("lblTypeOfTOD"));
        lblTransactionID.setText(resourceBundle.getString("lblTransactionID"));
        lblProdType.setText(resourceBundle.getString("lblProdType"));
        lblrepayedDate.setText(resourceBundle.getString("lblrepayedDate"));
        lblrepayedWithin.setText(resourceBundle.getString("lblrepayedWithin"));
    }
    
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }
    public void clearProdFields(){
        txtAccountNumber.setText("");
        lblAcctNameDisplay.setText("");
    }
    public void enableDisableRepayDetails(boolean val){
        txtRepayedWithin.setEnabled(val);
            cboRepayedWithin.setEditable(val);
            cboRepayedWithin.setEnabled(val);
            lblrepayedWithin.setVisible(val);
            txtRepayedWithin.setVisible(val);
            cboRepayedWithin.setVisible(val);
            lblrepayedDate.setVisible(val);
            lblrepayedDateValue.setVisible(val); 
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAccountNumber;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDateChange;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboPermitedBy;
    private com.see.truetransact.uicomponent.CComboBox cboProdType;
    private com.see.truetransact.uicomponent.CComboBox cboProductId;
    private com.see.truetransact.uicomponent.CComboBox cboRepayedWithin;
    private com.see.truetransact.uicomponent.CDateField dtdFromDate;
    private com.see.truetransact.uicomponent.CDateField dtdPermittedDt;
    private com.see.truetransact.uicomponent.CDateField dtdToDate;
    private com.see.truetransact.uicomponent.CLabel lblAccountNumber;
    private com.see.truetransact.uicomponent.CLabel lblAcctName;
    private com.see.truetransact.uicomponent.CLabel lblAcctNameDisplay;
    private com.see.truetransact.uicomponent.CLabel lblCreatedDateDesc;
    private com.see.truetransact.uicomponent.CLabel lblCreatedDt;
    private com.see.truetransact.uicomponent.CLabel lblFromDate;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblPermissionRefNo;
    private com.see.truetransact.uicomponent.CLabel lblPermitedBy;
    private com.see.truetransact.uicomponent.CLabel lblPermittedDt;
    private com.see.truetransact.uicomponent.CLabel lblProd;
    private com.see.truetransact.uicomponent.CLabel lblProdType;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace17;
    private com.see.truetransact.uicomponent.CLabel lblSpace18;
    private com.see.truetransact.uicomponent.CLabel lblSpace19;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace20;
    private com.see.truetransact.uicomponent.CLabel lblSpace21;
    private com.see.truetransact.uicomponent.CLabel lblSpace22;
    private com.see.truetransact.uicomponent.CLabel lblSpace23;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTODAllowed;
    private com.see.truetransact.uicomponent.CLabel lblToDate;
    private com.see.truetransact.uicomponent.CLabel lblTransactionID;
    private com.see.truetransact.uicomponent.CLabel lblTransactionIDValue;
    private com.see.truetransact.uicomponent.CLabel lblTypeOfTOD;
    private com.see.truetransact.uicomponent.CLabel lblrepayedDate;
    private com.see.truetransact.uicomponent.CLabel lblrepayedDateValue;
    private com.see.truetransact.uicomponent.CLabel lblrepayedWithin;
    private com.see.truetransact.uicomponent.CMenuBar mbrCustomer;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAccountHead;
    private com.see.truetransact.uicomponent.CPanel panAccountInfo;
    private com.see.truetransact.uicomponent.CPanel panAccountInfoInner;
    private com.see.truetransact.uicomponent.CPanel panAcctNum;
    private com.see.truetransact.uicomponent.CPanel panLableValues;
    private com.see.truetransact.uicomponent.CPanel panProductID;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTransaction;
    private com.see.truetransact.uicomponent.CPanel panTransactionType;
    private com.see.truetransact.uicomponent.CRadioButton rdoRunningLimit;
    private com.see.truetransact.uicomponent.CRadioButton rdoSingleTransaction;
    private com.see.truetransact.uicomponent.CButtonGroup rdoTransactionType;
    private com.see.truetransact.uicomponent.CSeparator sptAccountInfo;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private javax.swing.JToolBar tbrOperativeAcctProduct;
    private com.see.truetransact.uicomponent.CTextField txtAccountNumber;
    private com.see.truetransact.uicomponent.CTextField txtPermissionRefNo;
    private com.see.truetransact.uicomponent.CTextField txtRemarks;
    private com.see.truetransact.uicomponent.CTextField txtRepayedWithin;
    private com.see.truetransact.uicomponent.CTextField txtTODAllowed;
    // End of variables declaration//GEN-END:variables
    
    public static void main(String[] args) {
        TodAllowedUI tod = new TodAllowedUI();
        javax.swing.JFrame j = new javax.swing.JFrame();
        j.getContentPane().add(tod);
        j.show();
        tod.show();
    }
}
