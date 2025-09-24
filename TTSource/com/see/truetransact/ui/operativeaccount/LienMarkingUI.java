/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * LienMarkingUI.java
 *
 * Created on August 6, 2003, 10:52 AM
 */

package com.see.truetransact.ui.operativeaccount;

import java.util.HashMap;
import java.util.ArrayList;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.uimandatory.MandatoryCheck;

import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uivalidation.ToDateValidation;

import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Component;
import javax.swing.JTable;
import java.awt.Color;
import org.apache.log4j.Logger;
import java.util.List;
import java.util.Date;
/**
 *
 * @author  annamalai_t1
 * modified by Karthik
 */
public class LienMarkingUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer, UIMandatoryField {
    private HashMap mandatoryMap;
    private LienMarkingOB observable;
    
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.operativeaccount.LienMarkingRB", ProxyParameters.LANGUAGE);
    
    int LIENACCOUNTNO = 0, updateTab = -1, viewType = -1;
    boolean isFilled = false;
    private boolean oldActNo = true;
    String LIENSTATUS = "";
    int rowSelected = -1;
    private Date currDt = null;
    private final static Logger log = Logger.getLogger(LienMarkingUI.class);
    /** Creates new form LienMarkingUI */
    public LienMarkingUI() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        setMandatoryHashMap();
        setFieldNames();
        internationalize();
        observable = new LienMarkingOB();
        observable.addObserver(this);
        update(observable, null);
        initComponentData();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panProductInnerMost);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panLienDetailsInner);
        btnLienAccountNumber.setEnabled(false);
        tblLienList.setDefaultRenderer(Object.class, renderer);
        formButtonsEnableDisable();
        setHelpMessage();
        setMaxLenths();
        btnDelete.setEnabled(false);
    }
    
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtLienAmount", new Boolean(true));
        mandatoryMap.put("tdtLienDate", new Boolean(true));
        mandatoryMap.put("txtRemarks", new Boolean(true));
        mandatoryMap.put("txtLienAccountNumber", new Boolean(true));
        mandatoryMap.put("cboProductID", new Boolean(true));
        mandatoryMap.put("txtAccountNumber", new Boolean(true));
    }
    
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    
    public void authorizeStatus(String actionPerformed) {
        if((!btnNew.isEnabled()) && (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION)) {
            HashMap singleAuthorizeMap = new HashMap();
            ArrayList arrList = new ArrayList();
            HashMap authDataMap = new HashMap();
            int row = observable.getAuthRow();
            
            authDataMap.put("ACT_NUM", txtAccountNumber.getText());
            authDataMap.put("LIEN_ID", (String)tblLienList.getValueAt(row,1));
            authDataMap.put("LIEN_AMT", txtLienAmount.getText());
            authDataMap.put("LIEN_STATUS", LIENSTATUS);
            arrList.add(authDataMap);
            
            singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, actionPerformed);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            super.setOpenForEditBy(observable.getStatusBy());
            super.removeEditLock((String)tblLienList.getValueAt(row,1));
            authorize(singleAuthorizeMap);
            lblStatus.setText(actionPerformed);
        }else{
            observable.resetForm();
            observable.resetLienDetails();
            
            //__ To Save the data in the Internal Frame...
            setModified(true);
            
            HashMap mapParam = new HashMap();
            
            HashMap whereMap = new HashMap();
            whereMap.put("BEHAVIOR", null);
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, actionPerformed);
            
            mapParam.put(CommonConstants.MAP_NAME, "getLienMarkingAuthorizeTOList");
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            
            AuthorizeUI authorizeUI = new AuthorizeUI(this,mapParam);
            authorizeUI.show();
            observable.setStatus();
            //__ If there's no data to be Authorized, call Cancel action...
//            if(!isModified()){
//                setButtonEnableDisable();
//                btnCancelActionPerformed(null);
//            }
        }
    }
    
    private void setMaxLenths(){
        txtLienAmount.setValidation(new CurrencyValidation(14,2));
        txtAccountNumber.setMaxLength(16);
        txtAccountNumber.setAllowAll(true);
        txtLienAccountNumber.setAllowAll(true);
    }
    
    public void authorize(HashMap map) {
        System.out.println("Authorize Map : " + map);
        map.put("USER_ID", TrueTransactMain.USER_ID);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        observable.setAuthorizeMap(map);
        observable.doAction();
        int result = observable.getResult();
        
        //        if(!btnSave.isEnabled()){
        //            btnSave.setEnabled(true);
        //        }
        //        formButtonsEnableDisable();
        //        observable.resetObjects();
        //        observable.resetForm();
        //        /*
        //         * To Reset the Screen after the Authorization...
        //         */
        //        observable.resetLienDetails();
        //        lblLienCustName.setText("");
        //        btnLienAccountNumber.setEnabled(false);
        //        observable.setStatus();
        //        observable.setAuthorizeMap(null);
        //        observable.ttNotifyObservers();
        btnCancelActionPerformed(null);
        
        lblStatus.setText(ClientConstants.RESULT_STATUS[result]);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoCreditDebit = new com.see.truetransact.uicomponent.CButtonGroup();
        panLien = new com.see.truetransact.uicomponent.CPanel();
        panTable = new com.see.truetransact.uicomponent.CPanel();
        panLienDetails = new com.see.truetransact.uicomponent.CPanel();
        panLienSave = new com.see.truetransact.uicomponent.CPanel();
        btnLienSave = new com.see.truetransact.uicomponent.CButton();
        btnLienNew = new com.see.truetransact.uicomponent.CButton();
        btnLienDelete = new com.see.truetransact.uicomponent.CButton();
        btnUnLien = new com.see.truetransact.uicomponent.CButton();
        panLienDetailsInner = new com.see.truetransact.uicomponent.CPanel();
        lblLienAmount = new com.see.truetransact.uicomponent.CLabel();
        txtLienAmount = new com.see.truetransact.uicomponent.CTextField();
        lblLienDate = new com.see.truetransact.uicomponent.CLabel();
        tdtLienDate = new com.see.truetransact.uicomponent.CDateField();
        lblLienAccountHead = new com.see.truetransact.uicomponent.CLabel();
        lblLienAccountNumber = new com.see.truetransact.uicomponent.CLabel();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        txtRemarks = new com.see.truetransact.uicomponent.CTextField();
        lblBranchId = new com.see.truetransact.uicomponent.CLabel();
        lblLienProduct = new com.see.truetransact.uicomponent.CLabel();
        cboLienProduct = new com.see.truetransact.uicomponent.CComboBox();
        lblLienAccountHeadDesc = new com.see.truetransact.uicomponent.CLabel();
        lblLienCustName = new com.see.truetransact.uicomponent.CLabel();
        btnLienAccountNumber = new com.see.truetransact.uicomponent.CButton();
        lblLienSlNo = new com.see.truetransact.uicomponent.CLabel();
        lblLienSlNoDesc = new com.see.truetransact.uicomponent.CLabel();
        txtLienAccountNumber = new com.see.truetransact.uicomponent.CTextField();
        panLienList = new com.see.truetransact.uicomponent.CPanel();
        srpLienList = new com.see.truetransact.uicomponent.CScrollPane();
        tblLienList = new com.see.truetransact.uicomponent.CTable();
        sptDetails = new com.see.truetransact.uicomponent.CSeparator();
        panProduct = new com.see.truetransact.uicomponent.CPanel();
        panProductIDInner = new com.see.truetransact.uicomponent.CPanel();
        panProductInnerMost = new com.see.truetransact.uicomponent.CPanel();
        lblProductID = new com.see.truetransact.uicomponent.CLabel();
        cboProductID = new com.see.truetransact.uicomponent.CComboBox();
        lblAccountHead = new com.see.truetransact.uicomponent.CLabel();
        panAccountHead = new com.see.truetransact.uicomponent.CPanel();
        lblAccountHeadCode = new com.see.truetransact.uicomponent.CLabel();
        lblAccountHeadDesc = new com.see.truetransact.uicomponent.CLabel();
        lblAccountNumber = new com.see.truetransact.uicomponent.CLabel();
        txtAccountNumber = new com.see.truetransact.uicomponent.CTextField();
        btnAccountNumber = new com.see.truetransact.uicomponent.CButton();
        lblCustomerNameDisplay = new com.see.truetransact.uicomponent.CLabel();
        panClearBalance = new com.see.truetransact.uicomponent.CPanel();
        lblClearBalance = new com.see.truetransact.uicomponent.CLabel();
        lblClearBalanceDisplay = new com.see.truetransact.uicomponent.CLabel();
        lblExistingLiensSum = new com.see.truetransact.uicomponent.CLabel();
        lblExistingLiensSumDisplay = new com.see.truetransact.uicomponent.CLabel();
        lblClearBalance1 = new com.see.truetransact.uicomponent.CLabel();
        lblClearBalanceDisplay1 = new com.see.truetransact.uicomponent.CLabel();
        lblExistingFreezeSum = new com.see.truetransact.uicomponent.CLabel();
        lblExistingFreezeSumDisplay = new com.see.truetransact.uicomponent.CLabel();
        sptProduct = new com.see.truetransact.uicomponent.CSeparator();
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
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
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
        setResizable(true);
        setTitle("Lien Marking");
        setMinimumSize(new java.awt.Dimension(815, 545));
        setPreferredSize(new java.awt.Dimension(815, 545));

        panLien.setLayout(new java.awt.GridBagLayout());

        panTable.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panTable.setLayout(new java.awt.GridBagLayout());

        panLienDetails.setLayout(new java.awt.GridBagLayout());

        panLienSave.setLayout(new java.awt.GridBagLayout());

        btnLienSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnLienSave.setToolTipText("Save");
        btnLienSave.setMaximumSize(new java.awt.Dimension(29, 27));
        btnLienSave.setMinimumSize(new java.awt.Dimension(29, 27));
        btnLienSave.setName("btnContactNoAdd");
        btnLienSave.setPreferredSize(new java.awt.Dimension(29, 27));
        btnLienSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLienSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLienSave.add(btnLienSave, gridBagConstraints);

        btnLienNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnLienNew.setToolTipText("New");
        btnLienNew.setMaximumSize(new java.awt.Dimension(29, 27));
        btnLienNew.setMinimumSize(new java.awt.Dimension(29, 27));
        btnLienNew.setPreferredSize(new java.awt.Dimension(29, 27));
        btnLienNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLienNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLienSave.add(btnLienNew, gridBagConstraints);

        btnLienDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnLienDelete.setToolTipText("Delete");
        btnLienDelete.setMaximumSize(new java.awt.Dimension(29, 27));
        btnLienDelete.setMinimumSize(new java.awt.Dimension(29, 27));
        btnLienDelete.setPreferredSize(new java.awt.Dimension(29, 27));
        btnLienDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLienDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLienSave.add(btnLienDelete, gridBagConstraints);

        btnUnLien.setText("UnLien");
        btnUnLien.setToolTipText("UnLien");
        btnUnLien.setPreferredSize(new java.awt.Dimension(77, 27));
        btnUnLien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUnLienActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLienSave.add(btnUnLien, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        panLienDetails.add(panLienSave, gridBagConstraints);

        panLienDetailsInner.setLayout(new java.awt.GridBagLayout());

        lblLienAmount.setText("Lien Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLienDetailsInner.add(lblLienAmount, gridBagConstraints);

        txtLienAmount.setMaxLength(16);
        txtLienAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtLienAmount.setValidation(new CurrencyValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panLienDetailsInner.add(txtLienAmount, gridBagConstraints);

        lblLienDate.setText("Lien Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLienDetailsInner.add(lblLienDate, gridBagConstraints);

        tdtLienDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtLienDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panLienDetailsInner.add(tdtLienDate, gridBagConstraints);

        lblLienAccountHead.setText("Lien Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLienDetailsInner.add(lblLienAccountHead, gridBagConstraints);

        lblLienAccountNumber.setText("Lien Account Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLienDetailsInner.add(lblLienAccountNumber, gridBagConstraints);

        lblRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLienDetailsInner.add(lblRemarks, gridBagConstraints);

        txtRemarks.setMaxLength(256);
        txtRemarks.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panLienDetailsInner.add(txtRemarks, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLienDetailsInner.add(lblBranchId, gridBagConstraints);

        lblLienProduct.setText("Lien Product");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLienDetailsInner.add(lblLienProduct, gridBagConstraints);

        cboLienProduct.setMinimumSize(new java.awt.Dimension(100, 21));
        cboLienProduct.setPopupWidth(250);
        cboLienProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboLienProductActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panLienDetailsInner.add(cboLienProduct, gridBagConstraints);

        lblLienAccountHeadDesc.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panLienDetailsInner.add(lblLienAccountHeadDesc, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLienDetailsInner.add(lblLienCustName, gridBagConstraints);

        btnLienAccountNumber.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnLienAccountNumber.setToolTipText("Lien Account Number");
        btnLienAccountNumber.setMaximumSize(new java.awt.Dimension(21, 21));
        btnLienAccountNumber.setMinimumSize(new java.awt.Dimension(21, 21));
        btnLienAccountNumber.setPreferredSize(new java.awt.Dimension(21, 21));
        btnLienAccountNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLienAccountNumberActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panLienDetailsInner.add(btnLienAccountNumber, gridBagConstraints);

        lblLienSlNo.setText("LSl No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLienDetailsInner.add(lblLienSlNo, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLienDetailsInner.add(lblLienSlNoDesc, gridBagConstraints);

        txtLienAccountNumber.setMinimumSize(new java.awt.Dimension(100, 21));
        txtLienAccountNumber.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtLienAccountNumberFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panLienDetailsInner.add(txtLienAccountNumber, gridBagConstraints);

        panLienDetails.add(panLienDetailsInner, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panTable.add(panLienDetails, gridBagConstraints);

        panLienList.setLayout(new java.awt.GridBagLayout());

        srpLienList.setPreferredSize(new java.awt.Dimension(454, 125));

        tblLienList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tblLienList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblLienListMousePressed(evt);
            }
        });
        srpLienList.setViewportView(tblLienList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panLienList.add(srpLienList, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.8;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        panTable.add(panLienList, gridBagConstraints);

        sptDetails.setOrientation(javax.swing.SwingConstants.VERTICAL);
        sptDetails.setPreferredSize(new java.awt.Dimension(5, 5));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        panTable.add(sptDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLien.add(panTable, gridBagConstraints);

        panProduct.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panProduct.setLayout(new java.awt.GridBagLayout());

        panProductIDInner.setLayout(new java.awt.GridBagLayout());

        panProductInnerMost.setLayout(new java.awt.GridBagLayout());

        lblProductID.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductInnerMost.add(lblProductID, gridBagConstraints);

        cboProductID.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProductID.setPopupWidth(210);
        cboProductID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProductIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panProductInnerMost.add(cboProductID, gridBagConstraints);

        lblAccountHead.setText("Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductInnerMost.add(lblAccountHead, gridBagConstraints);

        panAccountHead.setLayout(new java.awt.GridBagLayout());

        lblAccountHeadCode.setForeground(new java.awt.Color(0, 51, 204));
        lblAccountHeadCode.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblAccountHeadCode.setPreferredSize(new java.awt.Dimension(75, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panAccountHead.add(lblAccountHeadCode, gridBagConstraints);

        lblAccountHeadDesc.setForeground(new java.awt.Color(0, 51, 204));
        lblAccountHeadDesc.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblAccountHeadDesc.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panAccountHead.add(lblAccountHeadDesc, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductInnerMost.add(panAccountHead, gridBagConstraints);

        lblAccountNumber.setText("Account Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductInnerMost.add(lblAccountNumber, gridBagConstraints);

        txtAccountNumber.setMaxLength(10);
        txtAccountNumber.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAccountNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAccountNumberActionPerformed(evt);
            }
        });
        txtAccountNumber.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAccountNumberFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panProductInnerMost.add(txtAccountNumber, gridBagConstraints);

        btnAccountNumber.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccountNumber.setToolTipText("Account No");
        btnAccountNumber.setMaximumSize(new java.awt.Dimension(21, 21));
        btnAccountNumber.setMinimumSize(new java.awt.Dimension(21, 21));
        btnAccountNumber.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAccountNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccountNumberActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panProductInnerMost.add(btnAccountNumber, gridBagConstraints);

        lblCustomerNameDisplay.setForeground(new java.awt.Color(0, 51, 204));
        lblCustomerNameDisplay.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblCustomerNameDisplay.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductInnerMost.add(lblCustomerNameDisplay, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panProductIDInner.add(panProductInnerMost, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 4);
        panProduct.add(panProductIDInner, gridBagConstraints);

        panClearBalance.setLayout(new java.awt.GridBagLayout());

        lblClearBalance.setText("Available Balance");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panClearBalance.add(lblClearBalance, gridBagConstraints);

        lblClearBalanceDisplay.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panClearBalance.add(lblClearBalanceDisplay, gridBagConstraints);

        lblExistingLiensSum.setText("Sum of Existing Liens");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panClearBalance.add(lblExistingLiensSum, gridBagConstraints);

        lblExistingLiensSumDisplay.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panClearBalance.add(lblExistingLiensSumDisplay, gridBagConstraints);

        lblClearBalance1.setText("Clear Balance");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panClearBalance.add(lblClearBalance1, gridBagConstraints);

        lblClearBalanceDisplay1.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panClearBalance.add(lblClearBalanceDisplay1, gridBagConstraints);

        lblExistingFreezeSum.setText("Sum of Existing Freeze");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panClearBalance.add(lblExistingFreezeSum, gridBagConstraints);

        lblExistingFreezeSumDisplay.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panClearBalance.add(lblExistingFreezeSumDisplay, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 0.8;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProduct.add(panClearBalance, gridBagConstraints);

        sptProduct.setOrientation(javax.swing.SwingConstants.VERTICAL);
        sptProduct.setPreferredSize(new java.awt.Dimension(5, 5));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panProduct.add(sptProduct, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLien.add(panProduct, gridBagConstraints);

        getContentPane().add(panLien, java.awt.BorderLayout.CENTER);

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
        btnAuthorize.setToolTipText("Close");
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
        btnException.setToolTipText("Print");
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
        btnReject.setToolTipText("Print");
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
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
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

        mbrMain.add(mnuProcess);

        setJMenuBar(mbrMain);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtLienAccountNumberFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtLienAccountNumberFocusLost
        // TODO add your handling code here:
        observable.LOANFlag = false;
        observable.OAFlag = false;
        String ACCOUNTNO = (String) txtLienAccountNumber.getText();
        if (ACCOUNTNO.length()>0) {
            observable.LOANFlag = true;
            oldActNo = false;
            if (observable.checkAcNoWithoutProdType(ACCOUNTNO,oldActNo)) {
                cboLienProductActionPerformed(null);
                txtLienAccountNumber.setText(observable.getTxtLienAccountNumber());
                ACCOUNTNO = (String) txtLienAccountNumber.getText();
//                txtLienAccountNumber.setText(ACCOUNTNO);
                viewType = 1;
                String PRODID= ((ComboBoxModel)cboLienProduct.getModel()).getKeyForSelected().toString();
                String acct_num = txtLienAccountNumber.getText();
                HashMap where =new HashMap();
                where.put("PRODID",PRODID);
                where.put("acct_num",ACCOUNTNO);
                where.put("ACCOUNT NUMBER",ACCOUNTNO);
                fillData(where);
            } else {
                ClientUtil.showAlertWindow("Invalid Account No.");
                txtLienAccountNumber.setText("");
//                lblAccountHeadCode.setText("");
//                lblAccountHeadDesc.setText("");
//                lblCustomerNameDisplay.setText("");
//                observable.resetAccountDetails();
//                observable.resetLienListTable();
//                observable.resetObjects();
//                clearBalances();
                return;
            }
        }        
        
    }//GEN-LAST:event_txtLienAccountNumberFocusLost

    private void txtAccountNumberFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccountNumberFocusLost
        // TODO add your handling code here:
        observable.LOANFlag = false;
        observable.OAFlag = false;
        String ACCOUNTNO = (String) txtAccountNumber.getText();
        if (ACCOUNTNO.length()>0) {
            observable.OAFlag = true;
            oldActNo = true;
            if (observable.checkAcNoWithoutProdType(ACCOUNTNO, oldActNo)) {
                txtAccountNumber.setText(observable.getTxtAccountNumber());
                ACCOUNTNO = (String) txtAccountNumber.getText();
                cboProductIDActionPerformed(null);
//                txtAccountNumber.setText(ACCOUNTNO);
                viewType = 0;
                String PRODID= ((ComboBoxModel)cboProductID.getModel()).getKeyForSelected().toString();
                String acct_num = txtAccountNumber.getText();
                HashMap where =new HashMap();
                where.put("PRODID",PRODID);
                where.put("acct_num",ACCOUNTNO);
                where.put("ACCOUNT NUMBER",ACCOUNTNO);
                fillData(where);
            } else {
                ClientUtil.showAlertWindow("Invalid Account No.");
                txtAccountNumber.setText("");
                lblAccountHeadCode.setText("");
                lblAccountHeadDesc.setText("");
                lblCustomerNameDisplay.setText("");
                observable.resetAccountDetails();
                observable.resetLienListTable();
                observable.resetObjects();
                clearBalances();
                return;
            }
        }   
    }//GEN-LAST:event_txtAccountNumberFocusLost

    private void txtAccountNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAccountNumberActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAccountNumberActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        popUp();
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
         HashMap reportParamMap = new HashMap();
 com.see.truetransact.clientutil.ttrintegration.LinkReport.getReports(getScreenID(), reportParamMap);
    }//GEN-LAST:event_btnPrintActionPerformed
    
    private void tdtLienDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtLienDateFocusLost
        // TODO add your handling code here:
        if(tdtLienDate.getDateValue() != null && tdtLienDate.getDateValue().length() > 0){
            HashMap dataMap = new HashMap();
            dataMap.put("ACT_NUM", CommonUtil.convertObjToStr(txtAccountNumber.getText()));
            dataMap.put("F_DATE", DateUtil.getDateMMDDYYYY(tdtLienDate.getDateValue()));
            boolean val = observable.verifyAccountDate(dataMap);
            
            //__ if selected date is before account opening date...
            if(!val){
                //__ Reset the date...
                tdtLienDate.setDateValue("");
                displayAlert(resourceBundle.getString("LIENDATEWARNING"));
            }
            ToDateValidation toDate= new ToDateValidation((Date) currDt.clone(), true);
            toDate.setComponent(this.tdtLienDate);
            if(!toDate.validate()) {
                 tdtLienDate.setDateValue("");
                displayAlert(resourceBundle.getString("WARNING"));
            }
        }
    }//GEN-LAST:event_tdtLienDateFocusLost
    
    private void btnLienAccountNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLienAccountNumberActionPerformed
        // Add your handling code here:
        LIENACCOUNTNO = 1;
        popUp();
    }//GEN-LAST:event_btnLienAccountNumberActionPerformed
    
    private void cboLienProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboLienProductActionPerformed
        // Add your handling code here:        
        txtLienAccountNumber.setText("");
        observable.setCboLienProduct(CommonUtil.convertObjToStr(cboLienProduct.getSelectedItem()));
        //        updateUIFields();
        if(observable.getCboLienProduct().length() > 0){
            final String achd = observable.setLienAcHead();
            lblLienAccountHeadDesc.setText(achd);
        }
    }//GEN-LAST:event_cboLienProductActionPerformed
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    
    private void btnUnLienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUnLienActionPerformed
        // Add your handling code here:
        updateUIFields();
        LienMarkingRB resourceBundle = new LienMarkingRB();
        String unlienRemarks = COptionPane.showInputDialog(resourceBundle.getString("optionLblUnLien"));
        observable.unLien(unlienRemarks, tblLienList.getSelectedRow());
        
        ClientUtil.enableDisable(panLienDetailsInner,false);
        lblLienCustName.setText("");
        btnLienAccountNumber.setEnabled(false);
        setLienButtonEnableDisableDefault();
        observable.resetLienDetails();
        observable.ttNotifyObservers();
    }//GEN-LAST:event_btnUnLienActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
        //        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // Add your handling code here:
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        for(int i=0; i<tblLienList.getRowCount();i++) {
            String data= CommonUtil.convertObjToStr(tblLienList.getValueAt(i,1));
            if(observable.getActionType() ==ClientConstants.ACTIONTYPE_EDIT)
                setMode(ClientConstants.ACTIONTYPE_EDIT);
            String status=CommonUtil.convertObjToStr((tblLienList.getValueAt(i,4)));
            if(status.length()>0)
            super.removeEditLock(data);
        }
         observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        if(!btnSave.isEnabled()){
            btnSave.setEnabled(true);
        }
        
        formButtonsEnableDisable();
        observable.resetObjects();
        observable.resetForm();
        observable.setNewObj();
        observable.resetLienDetails();
        lblLienCustName.setText("");
        btnLienAccountNumber.setEnabled(false);
        observable.setStatus();
        updateTab = -1;
        
        //__ Make the Screen Closable..
        setModified(false);
        viewType = -1;
        
        observable.ttNotifyObservers();
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        updateUIFields();
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panProductInnerMost);
        if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0 ){
            displayAlert(mandatoryMessage);
        }
        else{
            //__ If the New Record is added, alteast one row should be present...
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
                if(!(observable.rowCount() > 0)){
                    displayAlert(resourceBundle.getString("NOROWWARNING"));
                }else{
                    savePerformed();
                }
            }else{
                savePerformed();
            }
        }
    }//GEN-LAST:event_btnSaveActionPerformed
    private void savePerformed(){
        if (observable.doAction()) {
              HashMap lockMap = new HashMap();
        ArrayList lst = new ArrayList();
        lst.add("LIEN_NO");
        lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW) {
            if (observable.getProxyReturnMap()!=null) {
                if (observable.getProxyReturnMap().containsKey("LIEN_NO")) {
                    lst=(ArrayList)observable.getProxyReturnMap().get("LIEN_NO");
                    for(int i=0;i<lst.size();i++) {
                        lockMap.put("LIEN_NO",lst.get(i));
                        setEditLockMap(lockMap);
                        setEditLock();
                    }
                }
            }
        }
            formButtonsEnableDisable();
            observable.resetObjects();
            observable.resetForm();
            observable.setNewObj();
            observable.resetLienDetails();
            lblLienCustName.setText("");
            btnLienAccountNumber.setEnabled(false);
            observable.setResultStatus();
            
            //__ Make the Screen Closable..
            setModified(false);
            
            viewType = -1;
            
            observable.ttNotifyObservers();
        }
    }
    
    
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        observable.resetForm();
        observable.resetLienDetails();
        observable.setNewObj();
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        popUp();
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        observable.resetForm();
        observable.resetLienDetails();
        observable.setNewObj();
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        popUp();
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        enableDisable(true);
        setButtonEnableDisable();
        ClientUtil.enableDisable(this.panLienDetailsInner, false);
        setLienButtonEnableDisableDefault();
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.resetForm();
        observable.resetLienDetails();
        lblLienCustName.setText("");
        observable.resetObjects();
        observable.setNewObj();
        observable.setStatus();
        updateTab = -1;
        
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }//GEN-LAST:event_btnNewActionPerformed
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // Add your handling code here:
        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed
    
    private void btnLienDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLienDeleteActionPerformed
        // Add your handling code here:
        updateUIFields();
        observable.deleteLienTab(tblLienList.getSelectedRow());
        observable.resetLienDetails();
        btnLienAccountNumber.setEnabled(false);
        lblLienCustName.setText("");
        ClientUtil.enableDisable(this.panLienDetailsInner, false);
        setLienButtonEnableDisableDefault();
        observable.ttNotifyObservers();
        updateTab = -1;
    }//GEN-LAST:event_btnLienDeleteActionPerformed
    
    private void cboProductIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProductIDActionPerformed
        // Add your handling code here:
        //        updateUIFields();
        observable.setCboProductID((String) cboProductID.getSelectedItem());
        //To display AccountHead details based on proper ProductID selection
        if( observable.getCboProductID().length() > 0 && observable.getTxtAccountNumber().length() >= 0 ){
            observable.getAccountHeadForProduct();
             clearBalances();
        }
    }//GEN-LAST:event_cboProductIDActionPerformed
    
    private void tblLienListMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblLienListMousePressed
        // Add your handling code here:
        //To enable the lien input fields for modification if the actiontype is not delete
        //        if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE
        //        || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
        //        || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT
        //        || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION){
        //            updateUIFields();
        //            observable.populateLienDetails(tblLienList.getSelectedRow());
        //            btnLienAccountNumber.setEnabled(true);
        //            ClientUtil.enableDisable(this.panLienDetailsInner, true);
        //            txtLienAccountNumber.setEnabled(false);
        //            setLienButtonEnableDisableSelect();
        //            updateTab = 1;
        //            observable.ttNotifyObservers();
        //        }
        
        updateUIFields();
        rowSelected = tblLienList.getSelectedRow();
        observable.populateLienDetails(rowSelected);
        
       
        
        if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE){
            //            boolean UNLIENED = observable.getLblLienStatus().equalsIgnoreCase("UNLIENED");
            //            if(!UNLIENED){
            
            //                if(observable.getLblAuth().equalsIgnoreCase("AUTHORIZED"))
            if(( observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE) || (observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT)){
                ClientUtil.enableDisable(this.panLienDetailsInner, false);
                this.btnLienNew.setEnabled(false);
                this.btnLienSave.setEnabled(false);
                this.btnLienDelete.setEnabled(false);
                this.btnUnLien.setEnabled(false);
            }else {
                HashMap viewMap = new HashMap();
                if(observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT) {
                    String LslNo= lblLienSlNoDesc.getText();
                    viewMap.put("LIEN_NO",LslNo);
                    whenTableRowSelected(viewMap);
                }
                String authorizeStatus = observable.getLblAuth();
                if(authorizeStatus!=null && authorizeStatus.length()>0){
                    if((authorizeStatus.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED))||(authorizeStatus.equalsIgnoreCase(CommonConstants.STATUS_REJECTED))){
                        ClientUtil.enableDisable(this.panLienDetailsInner,false);
                        this.btnUnLien.setEnabled(true);
                        this.btnLienDelete.setEnabled(false);
                        this.btnLienSave.setEnabled(false);
                        
                        
                    }
                }
                else{
                    ClientUtil.enableDisable(this.panLienDetailsInner, true);
                    setLienButtonEnableDisableSelect();
                    txtLienAccountNumber.setEnabled(false);
                    btnLienAccountNumber.setEnabled(true);
                    this.btnUnLien.setEnabled(false);
                    this.btnLienNew.setEnabled(true);
                    //                    updateTab = 1;
                    updateTab = rowSelected;
                }
                
            }
        }
      String status =    CommonUtil.convertObjToStr(tblLienList.getValueAt(rowSelected, 5));
      String status1 =    CommonUtil.convertObjToStr(tblLienList.getValueAt(rowSelected, 4));
         if(status.equalsIgnoreCase("UNLIENED"))
         {
              this.btnLienDelete.setEnabled(false);
             this.btnLienSave.setEnabled(false);
         }
        if(status.equalsIgnoreCase("UNLIENED") && (status1.equalsIgnoreCase("REJECTED"))) {
           ClientUtil.enableDisable(this.panLienDetailsInner,false);
             this.btnLienDelete.setEnabled(false);
             this.btnLienSave.setEnabled(false);
        }
        
        
        observable.ttNotifyObservers();
    }//GEN-LAST:event_tblLienListMousePressed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // Add your handling code here:
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // Add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed
    
    private void btnLienSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLienSaveActionPerformed
        // Add your handling code here:
        int result=0;
        double sum=0.0;
        updateUIFields();
        StringBuffer str = new StringBuffer();
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panLienDetailsInner);
        
        if(mandatoryMessage.length() > 0){
            str.append(mandatoryMessage);
        }
//         if(observable.getTxtLienAccountNumber().equalsIgnoreCase(""))
        if(txtLienAccountNumber.getText().equalsIgnoreCase(""))
        {
           str.append(resourceBundle.getString("ACCNTWARNING")) ;
           
        }
        if(txtLienAmount.getText().equals("0.00"))
            {
                 ClientUtil.displayAlert(" Amount Cannot Be Zero");
                 btnCancelActionPerformed(null);
                 return;  
            }
       
        
        double clearBalance = CommonUtil.convertObjToDouble(lblClearBalanceDisplay.getText()).doubleValue();
        double clearBal = CommonUtil.convertObjToDouble(lblClearBalanceDisplay.getText()).doubleValue();
        double clearBalance1 = CommonUtil.convertObjToDouble(lblClearBalanceDisplay1.getText()).doubleValue();
        if(clearBalance1<clearBalance)
        clearBalance = CommonUtil.convertObjToDouble(lblClearBalanceDisplay1.getText()).doubleValue();    
        double lienAmt = CommonUtil.convertObjToDouble(txtLienAmount.getText()).doubleValue();
        double lienTabAmt = observable.lienTabSum();
        double lienExist = CommonUtil.convertObjToDouble(lblExistingLiensSumDisplay.getText()).doubleValue();
        double freezeExist = CommonUtil.convertObjToDouble(lblExistingFreezeSumDisplay.getText()).doubleValue();
          if(clearBalance1<clearBal){
           sum = (clearBalance-freezeExist);
        }
        else {
            sum = (clearBalance+lienExist);
        }
        //__ if a particular row is Updated...
        if(updateTab > -1){
            if(sum  < (lienAmt + lienTabAmt  - CommonUtil.convertObjToDouble(tblLienList.getValueAt(rowSelected, 2)).doubleValue()))
            {
                str.append(resourceBundle.getString("AMTWARNING"));
            }
        }else{
            if(sum < (lienAmt + lienTabAmt)){
                str.append(resourceBundle.getString("AMTWARNING"));
            }
        }
        
        //__ if Lien Amount Exceeds the Available Balance...
        
        
        if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && str.toString().length() > 0 ){
            displayAlert(str.toString());
        }
        else{
            result = observable.addLien(updateTab, rowSelected);
            if (result == 2){
                ClientUtil.enableDisable(panLienDetailsInner, true);
            }else{
                observable.resetLienDetails();
                ClientUtil.enableDisable(panLienDetailsInner,false);
                btnLienAccountNumber.setEnabled(false);
                setLienButtonEnableDisableDefault();
                lblLienCustName.setText("");
            }
          ArrayList lst = new ArrayList();
         lst.add("LIEN_NO");
         HashMap lockMap = new HashMap();
         lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
          if(observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT) {
                    lockMap.put("LIEN_NO", observable.getLblLienId());
                
                setEditLockMap(lockMap);
                setEditLock();
          }
            updateTab = -1;
            rowSelected = -1;
            observable.ttNotifyObservers();
        }
        
        
//        rowSelected = -1;
    }//GEN-LAST:event_btnLienSaveActionPerformed
    
    private void btnLienNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLienNewActionPerformed
        // Add your handling code here:
        
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panProductInnerMost);
        if(mandatoryMessage.length() > 0){
            displayAlert(mandatoryMessage);
            
        }else{
            updateUIFields();
            observable.resetLienDetails();
            //To enable Lien details input panel if proper account number is entered, else diplay error message
            if( 0 != observable.getTxtAccountNumber().length() ){
                observable.resetLienDetails();
                ClientUtil.enableDisable(this.panLienDetailsInner, true);
                btnLienAccountNumber.setEnabled(true);
                lblLienCustName.setText("");
                txtLienAccountNumber.setEnabled(true);
                // observable.resetPhoneDetailsNotify();
                setLienButtonEnableDisableNew();
                observable.ttNotifyObservers();
            }
            else{
                final String[] options = { CommonConstants.OK};
                final LienMarkingRB resourceBundle = new LienMarkingRB();
                COptionPane.showOptionDialog(null, resourceBundle.getString("accNumberErrMsg"), CommonConstants.WARNINGTITLE,
                COptionPane.DEFAULT_OPTION, COptionPane.WARNING_MESSAGE,
                null, options, options[0]);
            }
        }
        
    }//GEN-LAST:event_btnLienNewActionPerformed
    
    private void btnAccountNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccountNumberActionPerformed
        // Add your handling code here:
        System.out.println("btnAccountNumberActionPerformed()");
        viewType = 0;
        if(CommonUtil.convertObjToStr(txtAccountNumber.getText()).equalsIgnoreCase("")){
            popUp();
            
        }else{
            String[] options = {resourceBundle.getString("cDialogYes"),resourceBundle.getString("cDialogNo")};
            int select = COptionPane.showOptionDialog(null, resourceBundle.getString("ACCOUNTWARNING"), CommonConstants.WARNINGTITLE,
            COptionPane.YES_NO_CANCEL_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
            
            //__ If Yes is Selected...
            if (select == 0){
                popUp();
            }
        }
    }//GEN-LAST:event_btnAccountNumberActionPerformed
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // Add your handling code here:
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed
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
    private void whenTableRowSelected(HashMap paramMap) {
                String lockedBy = "";
                  HashMap map = new HashMap();
                 map.put("SCREEN_ID", getScreenID());
                 map.put("RECORD_KEY", paramMap.get("LIEN_NO"));
                 map.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
                 map.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
                 map.put("CUR_DATE", currDt.clone());
                 System.out.println("Record Key Map : " + map);
                 List lstLock = ClientUtil.executeQuery("selectEditLock", map);
                 if (lstLock.size() > 0) {
                        lockedBy = CommonUtil.convertObjToStr(lstLock.get(0));
                        if (!lockedBy.equals(ProxyParameters.USER_ID)) {
//                            setMode(ClientConstants.ACTIONTYPE_VIEW_MODE);
                            btnSave.setEnabled(false);
                        } else {
//                            setMode(ClientConstants.ACTIONTYPE_EDIT);
                              btnSave.setEnabled(true);
                        }
                    } else {
//                        setMode(ClientConstants.ACTIONTYPE_EDIT);
                          btnSave.setEnabled(true);
                    }
                    setOpenForEditBy(lockedBy);
                    if (lockedBy.equals(""))
                        ClientUtil.execute("insertEditLock", map);
                     if (lockedBy.length() > 0 && !lockedBy.equals(ProxyParameters.USER_ID)) {
                    String data = getLockDetails(lockedBy, getScreenID()) ;
                    ClientUtil.showMessageWindow("Selected Record is Opened/Modified by " + lockedBy + data.toString());
//                    setMode(ClientConstants.ACTIONTYPE_VIEW_MODE);
                      btnSave.setEnabled(false);
                }
                
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
    
    
    /** To populate Comboboxes */
    private void initComponentData() {
        cboProductID.setModel(observable.getCbmProductID());
        cboLienProduct.setModel(observable.getCbmLienProduct());
    }
    
    /** To display a popUp window for viewing existing data as well as to select new
     * customer for entry
     */
    private void popUp(){
        System.out.println("In popUp()");
        System.out.println("LIENACCOUNTNO: " + LIENACCOUNTNO) ;
        HashMap testMap = null;
        //To display customer info based on the selected ProductID
        if(LIENACCOUNTNO == 1){
            testMap = LienAcctNoMap();
            new ViewAll(this, testMap, true).show();
            
        }
        //        else if( observable.getActionType() == ClientConstants.ACTIONTYPE_NEW ){
        //            testMap = accountViewMap();
        //
        //        }
        else if(viewType != -1){
            testMap = accountViewMap();
            new ViewAll(this, testMap, true).show();
        }
        //To display the existing accounts which are set to freezed
        else if( observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
            testMap = LienEditMap();
            new ViewAll(this, testMap).show();
            
        }
         else if( observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW){
            testMap = LienEditMap();
            new ViewAll(this, testMap).show();
            
        }
        else if(observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE){
            testMap = LienDeleteMap();
            new ViewAll(this, testMap).show();
        }
        //        new ViewAll(this, testMap).show();
    }
    
    /** To get popUp data for already existing entries for modification */
    private HashMap LienAcctNoMap(){
        final HashMap testMap = new HashMap();
        HashMap hash = new HashMap();
        updateUIFields();
        testMap.put(CommonConstants.MAP_NAME, "Lein.getAccountNo");
        hash.put("PRODID", ((ComboBoxModel)cboLienProduct.getModel()).getKeyForSelected());
        hash.put(CommonConstants.BRANCH_ID,TrueTransactMain.BRANCH_ID);
        testMap.put(CommonConstants.MAP_WHERE, hash);
        return testMap;
    }
    
    /** Called by the Popup window created thru popUp method */
    public void fillData(Object obj) {
        System.out.println("in FillData");
        try{
            HashMap hash = (HashMap) obj;
            //To fillData for a new entry
            if(LIENACCOUNTNO == 1){
                final String ACCOUNTNO = (String) hash.get("ACCT_NUM");
                txtLienAccountNumber.setText(ACCOUNTNO);
                final String CUSTNAME = (String) hash.get("CUSTOMER_NAME");
                lblLienCustName.setText(CUSTNAME);
                LIENACCOUNTNO = 0;
            }
            //            else if( observable.getActionType() == ClientConstants.ACTIONTYPE_NEW ){
            //                fillDataNew(hash);
            //            }
            
            else if(viewType != -1){
                fillDataNew(hash);
                viewType = -1;
            }
            else if( observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW){
                //fillDataNew(hash);
                observable.resetObjects();
                fillDataEdit(hash);
                setButtonEnableDisable();
                btnAccountNumber.setEnabled(false);
                observable.setStatus();
                checkForEdit();
            } else if( observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION){
                observable.resetObjects();
                fillDataEdit(hash);
                setButton4Authorize();
//                 if(viewType==AUTHORIZE) {
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
//                }
            }
            
            
        }catch(Exception e){
            log.error(e);
        }
        
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }
    
    private void checkForEdit(){
        //To enable disable controls for EDIT operation
        if( observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ){
            enableDisable(true);
            cboProductID.setEnabled(false);
            txtAccountNumber.setEnabled(false);
            setLienButtonEnableDisableDefault();
            ClientUtil.enableDisable(this.panLienDetailsInner, false);
        }
        
        
    }
    /** To fillData for a new entry */
    private void fillDataNew(HashMap hash){
        txtAccountNumber.setText((String)hash.get("ACCOUNT NUMBER"));
        
        //__ Reset the Table with the change of Account No...
        //        observable.resetForm();
        observable.resetAccountDetails();
        observable.resetLienListTable();
        observable.resetObjects();
        showCustomerName();
        txtAccountNumber.setText((String)hash.get("ACCOUNT NUMBER"));
    }
    
    /** To fillData for existing entry */
    private void fillDataEdit(HashMap hash){
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION) {
            hash.put("ACCTNUMBER", hash.get("ACT_NUM"));
        } else {
            hash.put("ACCTNUMBER", hash.get("ACCOUNT NUMBER"));
        }
        observable.getProductDetails(hash);
        observable.getData(hash);
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION) {
            final String LIENID = (String)hash.get("LIEN_ID");
            LIENSTATUS = (String)hash.get("LIEN_STATUS");
            try{
                observable.setAuthRowData(LIENID);
            }catch(Exception e){
                System.out.println("Error in observable.setAuthRow(LIENID);");
            }
        }
    }
    
    /** To get popUp data for a new entry */
    private HashMap accountViewMap(){
        final HashMap testMap = new HashMap();
        testMap.put(CommonConstants.MAP_NAME, "getSelectAccountListForLien");
        final HashMap whereMap = new HashMap();
        whereMap.put("PRODID", (String)(observable.getCbmProductID()).getKeyForSelected());
        whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
        testMap.put(CommonConstants.MAP_WHERE, whereMap);
        return testMap;
    }
    
    /** To get popUp data for already existing entries for modification */
    private HashMap LienEditMap(){
        final HashMap testMap = new HashMap();
        
        final HashMap whereMap = new HashMap();
        whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
        
        testMap.put(CommonConstants.MAP_NAME, "getSelectLienAccountList");
        testMap.put(CommonConstants.MAP_WHERE, whereMap);
        return testMap;
    }
    
    /** To get popUp data for already existing entries for deletion */
    private HashMap LienDeleteMap(){
        final HashMap testMap = new HashMap();
        
        final HashMap whereMap = new HashMap();
        whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
        
        testMap.put(CommonConstants.MAP_NAME, "getSelectLienAccountDeleteList");
        testMap.put(CommonConstants.MAP_WHERE, whereMap);
        
        return testMap;
    }
    
    
    
    /** To display customer related details based on account number */
    private void showCustomerName(){
        updateUIFields();
        observable.getCustomerDetails();
    }
    
    public void update(java.util.Observable o, Object arg) {
        txtLienAmount.setText(observable.getTxtLienAmount());
        tdtLienDate.setDateValue(observable.getTdtLienDate());
        txtRemarks.setText(observable.getTxtRemarks());
        txtAccountNumber.setText(observable.getTxtAccountNumber());
        cboLienProduct.setSelectedItem(observable.getCboLienProduct());
        txtLienAccountNumber.setText(observable.getTxtLienAccountNumber());
        lblLienAccountHeadDesc.setText(observable.getLblLienAccountHeadDesc());
        lblLienAccountHeadDesc.setToolTipText(lblLienAccountHeadDesc.getText());
        lblStatus.setText(observable.getLblStatus());
        lblAccountHeadCode.setText(observable.getAccountHeadCode());
        lblAccountHeadDesc.setText(observable.getAccountHeadDesc());
        lblCustomerNameDisplay.setText(observable.getCustomerName());
        lblCustomerNameDisplay.setToolTipText(lblCustomerNameDisplay.getText());
        lblClearBalanceDisplay.setText(observable.getClearBalance());
        lblExistingLiensSumDisplay.setText(observable.getLienSum());
        lblExistingFreezeSumDisplay.setText(observable.getFreezeSum());
//        cboProductID.setSelectedItem(observable.getCboProductID());
        tblLienList.setModel(observable.getTblLienListModel());
        lblLienSlNoDesc.setText(observable.getLblLienId());
        lblLienCustName.setText(observable.getLblLienCustName());
        lblLienCustName.setToolTipText(lblLienCustName.getText());
        lblClearBalanceDisplay1.setText(observable.getClearBalance1());
    }
    
    
    
    public void updateUIFields() {
        observable.setTxtLienAmount(txtLienAmount.getText());
        observable.setTdtLienDate(tdtLienDate.getDateValue());
        observable.setTxtRemarks(txtRemarks.getText());
        observable.setTxtLienAccountNumber(txtLienAccountNumber.getText());
        observable.setTxtAccountNumber(txtAccountNumber.getText());
        observable.setTblLienListModel((com.see.truetransact.clientutil.EnhancedTableModel)tblLienList.getModel());
        observable.setCboProductID((String) cboProductID.getSelectedItem());
        observable.setLblLienAccountHeadDesc(lblLienAccountHeadDesc.getText());
        observable.setCboLienProduct((String) cboLienProduct.getSelectedItem());
        observable.setLblLienLienCustName(lblLienCustName.getText());
        
    }
    
    private void setFieldNames() {
        btnAccountNumber.setName("btnAccountNumber");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnLienDelete.setName("btnLienDelete");
        btnLienNew.setName("btnLienNew");
        btnLienSave.setName("btnLienSave");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnSave.setName("btnSave");
        btnLienAccountNumber.setName("btnLienAccountNumber");
        cboProductID.setName("cboProductID");
        cboLienProduct.setName("cboLienProduct");
        lblAccountHead.setName("lblAccountHead");
        lblAccountHeadCode.setName("lblAccountHeadCode");
        lblAccountHeadDesc.setName("lblAccountHeadDesc");
        lblAccountNumber.setName("lblAccountNumber");
        lblClearBalance.setName("lblClearBalance");
        lblClearBalanceDisplay.setName("lblClearBalanceDisplay");
        lblLienCustName.setName("lblLienCustName");
        lblLienProduct.setName("lblLienProduct");
        lblCustomerNameDisplay.setName("lblCustomerNameDisplay");
        lblExistingLiensSum.setName("lblExistingLiensSum");
        lblExistingLiensSumDisplay.setName("lblExistingLiensSumDisplay");
        lblExistingFreezeSum.setName("lblExistingFreezeSum");
        lblExistingFreezeSumDisplay.setName("lblExistingFreezeSumDisplay");
        lblLienAccountHead.setName("lblLienAccountHead");
        lblLienAccountNumber.setName("lblLienAccountNumber");
        lblLienAccountHeadDesc.setName("lblLienAccountHeadDesc");
        lblLienAmount.setName("lblLienAmount");
        lblLienDate.setName("lblLienDate");
        lblLienDate.setName("lblLienSlNo");
        lblLienDate.setName("lblLienSlNoDesc");
        lblMsg.setName("lblMsg");
        lblProductID.setName("lblProductID");
        lblRemarks.setName("lblRemarks");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblStatus.setName("lblStatus");
        mbrMain.setName("mbrMain");
        panAccountHead.setName("panAccountHead");
        panClearBalance.setName("panClearBalance");
        panLien.setName("panLien");
        panLienDetails.setName("panLienDetails");
        panLienDetailsInner.setName("panLienDetailsInner");
        panLienList.setName("panLienList");
        panLienSave.setName("panLienSave");
        panProduct.setName("panProduct");
        panProductIDInner.setName("panProductIDInner");
        panProductInnerMost.setName("panProductInnerMost");
        panStatus.setName("panStatus");
        panTable.setName("panTable");
        sptDetails.setName("sptDetails");
        sptProduct.setName("sptProduct");
        srpLienList.setName("srpLienList");
        tblLienList.setName("tblLienList");
        tdtLienDate.setName("tdtLienDate");
        txtAccountNumber.setName("txtAccountNumber");
        txtLienAccountNumber.setName("txtLienAccountNumber");
        txtLienAmount.setName("txtLienAmount");
        txtRemarks.setName("txtRemarks");
        lblBranchId.setName("lblBranchId");
        btnUnLien.setName("btnUnLien");
        lblClearBalance1.setName("lblClearBalance1");
        lblClearBalanceDisplay1.setName("lblClearBalanceDisplay1");
    }
    
    private void internationalize() {
        //        LienMarkingRB resourceBundle = new LienMarkingRB();
        btnUnLien.setText(resourceBundle.getString("btnUnLien"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblAccountNumber.setText(resourceBundle.getString("lblAccountNumber"));
        lblLienAmount.setText(resourceBundle.getString("lblLienAmount"));
        lblAccountHeadCode.setText(resourceBundle.getString("lblAccountHeadCode"));
        lblAccountHead.setText(resourceBundle.getString("lblAccountHead"));
        lblClearBalance.setText(resourceBundle.getString("lblClearBalance"));
        btnLienNew.setText(resourceBundle.getString("btnLienNew"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        btnAccountNumber.setText(resourceBundle.getString("btnAccountNumber"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        btnLienDelete.setText(resourceBundle.getString("btnLienDelete"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        lblRemarks.setText(resourceBundle.getString("lblRemarks"));
        lblClearBalanceDisplay.setText(resourceBundle.getString("lblClearBalanceDisplay"));
        lblLienCustName.setText(resourceBundle.getString("lblLienCustName"));
        lblLienProduct.setText(resourceBundle.getString("lblLienProduct"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblExistingLiensSum.setText(resourceBundle.getString("lblExistingLiensSum"));
        lblExistingFreezeSum.setText(resourceBundle.getString("lblExistingFreezeSum"));
        lblAccountHeadDesc.setText(resourceBundle.getString("lblAccountHeadDesc"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblLienDate.setText(resourceBundle.getString("lblLienDate"));
        btnLienSave.setText(resourceBundle.getString("btnLienSave"));
        lblLienAccountNumber.setText(resourceBundle.getString("lblLienAccountNumber"));
        lblExistingLiensSumDisplay.setText(resourceBundle.getString("lblExistingLiensSumDisplay"));
        lblExistingFreezeSumDisplay.setText(resourceBundle.getString("lblExistingFreezeSumDisplay"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        lblProductID.setText(resourceBundle.getString("lblProductID"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblCustomerNameDisplay.setText(resourceBundle.getString("lblCustomerNameDisplay"));
        lblLienAccountHead.setText(resourceBundle.getString("lblLienAccountHead"));
        lblLienAccountHeadDesc.setText(resourceBundle.getString("lblLienAccountHeadDesc"));
        btnLienAccountNumber.setText(resourceBundle.getString("btnLienAccountNumber"));
        lblBranchId.setText(resourceBundle.getString("lblBranchId"));
        lblLienSlNo.setText(resourceBundle.getString("lblLienSlNo"));
        lblLienSlNoDesc.setText(resourceBundle.getString("lblLienSlNoDesc"));
        lblClearBalance1.setText(resourceBundle.getString("lblClearBalance1"));
        lblClearBalanceDisplay1.setText(resourceBundle.getString("lblClearBalanceDisplay1"));
    }
    
    public void setHelpMessage() {
        LienMarkingMRB objMandatoryRB = new LienMarkingMRB();
        txtLienAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLienAmount"));
        tdtLienDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtLienDate"));
        txtRemarks.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRemarks"));
        cboLienProduct.setHelpMessage(lblMsg, objMandatoryRB.getString("cboLienProduct"));
        txtLienAccountNumber.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLienAccountNumber"));
        cboProductID.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProductID"));
        txtAccountNumber.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAccountNumber"));
    }
   
    private void enableDisable(boolean yesno){
        ClientUtil.enableDisable(this, yesno);
    }
    
    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
//        btnDelete.setEnabled(!btnDelete.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
//        mitDelete.setEnabled(btnDelete.isEnabled());
        
        btnSave.setEnabled(!btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        
        btnAuthorize.setEnabled(btnNew.isEnabled());
        btnReject.setEnabled(btnNew.isEnabled());
        btnException.setEnabled(btnNew.isEnabled());
        
        btnAccountNumber.setEnabled(!btnNew.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
    }
    
    private void setButton4Authorize() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(btnNew.isEnabled());
//        btnDelete.setEnabled(btnNew.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
//        mitDelete.setEnabled(btnDelete.isEnabled());
        
        btnSave.setEnabled(btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
    }
    
    private void setLienButtonEnableDisable(){
        this.btnLienNew.setEnabled(false);
        this.btnLienSave.setEnabled(false);
        this.btnLienDelete.setEnabled(false);
        this.btnUnLien.setEnabled(false);
    }
    
    private void setLienButtonEnableDisableDefault(){
        this.btnLienNew.setEnabled(true);
        this.btnLienSave.setEnabled(false);
        this.btnLienDelete.setEnabled(false);
        this.btnUnLien.setEnabled(false);
    }
    
    private void setLienButtonEnableDisableNew(){
        this.btnLienNew.setEnabled(false);
        this.btnLienSave.setEnabled(true);
        this.btnLienDelete.setEnabled(false);
        this.btnUnLien.setEnabled(false);
    }
    
    private void setLienButtonEnableDisableSelect(){
        this.btnLienNew.setEnabled(false);
        this.btnLienSave.setEnabled(true);
        this.btnLienDelete.setEnabled(true);
        this.btnUnLien.setEnabled(false);
    }
    
    private void formButtonsEnableDisable(){
        enableDisable(false);
        setButtonEnableDisable();
        setLienButtonEnableDisable();
    }
     public void clearBalances(){
        lblClearBalanceDisplay.setText("");
        lblClearBalanceDisplay1.setText("");
        lblExistingFreezeSumDisplay.setText("");
        lblExistingLiensSumDisplay.setText("");
        txtAccountNumber.setText("");
        lblCustomerNameDisplay.setText("");
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAccountNumber;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnLienAccountNumber;
    private com.see.truetransact.uicomponent.CButton btnLienDelete;
    private com.see.truetransact.uicomponent.CButton btnLienNew;
    private com.see.truetransact.uicomponent.CButton btnLienSave;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnUnLien;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboLienProduct;
    private com.see.truetransact.uicomponent.CComboBox cboProductID;
    private com.see.truetransact.uicomponent.CLabel lblAccountHead;
    private com.see.truetransact.uicomponent.CLabel lblAccountHeadCode;
    private com.see.truetransact.uicomponent.CLabel lblAccountHeadDesc;
    private com.see.truetransact.uicomponent.CLabel lblAccountNumber;
    private com.see.truetransact.uicomponent.CLabel lblBranchId;
    private com.see.truetransact.uicomponent.CLabel lblClearBalance;
    private com.see.truetransact.uicomponent.CLabel lblClearBalance1;
    private com.see.truetransact.uicomponent.CLabel lblClearBalanceDisplay;
    private com.see.truetransact.uicomponent.CLabel lblClearBalanceDisplay1;
    private com.see.truetransact.uicomponent.CLabel lblCustomerNameDisplay;
    private com.see.truetransact.uicomponent.CLabel lblExistingFreezeSum;
    private com.see.truetransact.uicomponent.CLabel lblExistingFreezeSumDisplay;
    private com.see.truetransact.uicomponent.CLabel lblExistingLiensSum;
    private com.see.truetransact.uicomponent.CLabel lblExistingLiensSumDisplay;
    private com.see.truetransact.uicomponent.CLabel lblLienAccountHead;
    private com.see.truetransact.uicomponent.CLabel lblLienAccountHeadDesc;
    private com.see.truetransact.uicomponent.CLabel lblLienAccountNumber;
    private com.see.truetransact.uicomponent.CLabel lblLienAmount;
    private com.see.truetransact.uicomponent.CLabel lblLienCustName;
    private com.see.truetransact.uicomponent.CLabel lblLienDate;
    private com.see.truetransact.uicomponent.CLabel lblLienProduct;
    private com.see.truetransact.uicomponent.CLabel lblLienSlNo;
    private com.see.truetransact.uicomponent.CLabel lblLienSlNoDesc;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblProductID;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace17;
    private com.see.truetransact.uicomponent.CLabel lblSpace18;
    private com.see.truetransact.uicomponent.CLabel lblSpace19;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace20;
    private com.see.truetransact.uicomponent.CLabel lblSpace21;
    private com.see.truetransact.uicomponent.CLabel lblSpace22;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAccountHead;
    private com.see.truetransact.uicomponent.CPanel panClearBalance;
    private com.see.truetransact.uicomponent.CPanel panLien;
    private com.see.truetransact.uicomponent.CPanel panLienDetails;
    private com.see.truetransact.uicomponent.CPanel panLienDetailsInner;
    private com.see.truetransact.uicomponent.CPanel panLienList;
    private com.see.truetransact.uicomponent.CPanel panLienSave;
    private com.see.truetransact.uicomponent.CPanel panProduct;
    private com.see.truetransact.uicomponent.CPanel panProductIDInner;
    private com.see.truetransact.uicomponent.CPanel panProductInnerMost;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTable;
    private com.see.truetransact.uicomponent.CButtonGroup rdoCreditDebit;
    private com.see.truetransact.uicomponent.CSeparator sptDetails;
    private javax.swing.JSeparator sptNew;
    private com.see.truetransact.uicomponent.CSeparator sptProduct;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CScrollPane srpLienList;
    private com.see.truetransact.uicomponent.CTable tblLienList;
    private javax.swing.JToolBar tbrOperativeAcctProduct;
    private com.see.truetransact.uicomponent.CDateField tdtLienDate;
    private com.see.truetransact.uicomponent.CTextField txtAccountNumber;
    private com.see.truetransact.uicomponent.CTextField txtLienAccountNumber;
    private com.see.truetransact.uicomponent.CTextField txtLienAmount;
    private com.see.truetransact.uicomponent.CTextField txtRemarks;
    // End of variables declaration//GEN-END:variables
    
    /* Set a cellrenderer to this table in order format the date time */
    DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
        public Component getTableCellRendererComponent(
        JTable table, Object value, boolean isSelected, boolean hasFocus,
        int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            //To set the row color as Red if the data for the row is set to be deleted, else set normal color
            if( observable.isDeleted(row) ){
                setForeground(table.getForeground());
                setBackground(Color.red);
            }
            else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }
            
            //To set the default selection background color if a row is selected
            if (isSelected)  {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            }
            
            /* Set oquae */
            this.setOpaque(true);
            return this;
        }
    };
    
    public static void main(String[] args) {
        LienMarkingUI lui = new LienMarkingUI();
        javax.swing.JFrame j = new javax.swing.JFrame();
        j.getContentPane().add(lui);
        j.show();
        lui.show();
    }
}
