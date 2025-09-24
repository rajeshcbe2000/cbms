/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * FreezeUI.java
 *
 * Created on August 6, 2003, 10:52 AM
 */
package com.see.truetransact.ui.product.groupmdsdeposit;

import com.see.truetransact.ui.product.loan.loaneligibilitymaintenance.*;
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
import com.see.truetransact.uivalidation.CurrencyValidation;


import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.TrueTransactMain;


import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Component;
import javax.swing.JTable;
import java.awt.Color;
import org.apache.log4j.Logger;
import java.util.List;
import java.util.Date;

/**
 *
 * @author annamalai_t1 modified by Karthik
 */
public class GroupMDSDepositUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer, UIMandatoryField {

    private HashMap mandatoryMap;
    private GroupMDSDepositOB observable;
    int updateTab = -1, viewType = -1;
    String FREEZESTATUS = "";
    boolean flag = false;
    private final String TYPE = "COMPLETE";
    int rowSelected = -1;
    private String advances = "";
    private Date currDt = null;
    private String viewType1 = new String();
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.product.groupmdsdeposit.GroupMDSDepositRB", ProxyParameters.LANGUAGE);
    private final static Logger log = Logger.getLogger(GroupMDSDepositUI.class);

    /**
     * Creates new form FreezeUI
     */
    public GroupMDSDepositUI() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        initStartUp();
    }

    public GroupMDSDepositUI(String advance) {
        this.advances = advance;
        initComponents();
        initStartUp();
    }

    private void initStartUp() {

        setMandatoryHashMap();
        setFieldNames();
//        internationalize();
        observable = new GroupMDSDepositOB();
        observable.setAdvances(advances);
        observable.addObserver(this);
        initComponentData();    
        formButtonsEnableDisable();
        setHelpMessage();
        setMaxLength();
        btnDelete.setEnabled(false);    
        observable.resetForm();  
        cboInterestRecovery.setEnabled(false);
        txtInterestRecovery.setEnabled(false);
        cboDueIntCalcType.setEnabled(false);
        txtDueIntCalcType.setEnabled(false);
    }

    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboCropType", new Boolean(true));
        mandatoryMap.put("txtEligibileAmount", new Boolean(true));
        mandatoryMap.put("tdtFromDate", new Boolean(true));
        mandatoryMap.put("txtGroupName", new Boolean(true));
        mandatoryMap.put("txtCount", new Boolean(true));
        mandatoryMap.put("cboProductType", new Boolean(true));
        mandatoryMap.put("cboInterestAmount", new Boolean(true));
        mandatoryMap.put("txtInterestAmount", new Boolean(true));
        mandatoryMap.put("cboPenalCalculation", new Boolean(true));
        mandatoryMap.put("txtPenalCalculation", new Boolean(true));
        mandatoryMap.put("txtDepositAmt", new Boolean(true));
        mandatoryMap.put("txtInterestRecovery", new Boolean(true));
        mandatoryMap.put("cboPrematureIntRecType", new Boolean(true));
        mandatoryMap.put("txtPrematureIntRecAmt", new Boolean(true));
        mandatoryMap.put("tdtStartDate", new Boolean(true));
        mandatoryMap.put("tdtEndDate", new Boolean(true));
    }

    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    private void setMaxLength() {
        txtCount.setMaxLength(16);
    }

    public void authorizeStatus(String actionPerformed) {
        if ((!btnNew.isEnabled()) && (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION)) {            
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, "AUTHORIZED");
            singleAuthorizeMap.put("GROUP_NO", observable.getGroupId());
            authorize(singleAuthorizeMap);
            lblStatus.setText(actionPerformed);
        } else {            
            observable.resetForm();
            observable.resetFreezeDetails();

            //__ To Save the data in the Internal Frame...
            setModified(true);
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, actionPerformed);

            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
 
            mapParam.put(CommonConstants.MAP_NAME, "getGroupMDSAuthorizeTOList");
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);

            System.out.println("mapParam: " + mapParam);
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            observable.setStatus();

        }
    }

    public void authorize(HashMap map) {
        System.out.println("Authorize Map : " + map);
        map.put("USER_ID", TrueTransactMain.USER_ID);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        observable.setAuthorizeMap(map);
        observable.execute("AUTHORIZE");
        int result = observable.getResult();
        btnCancelActionPerformed(null);
        lblStatus.setText(ClientConstants.RESULT_STATUS[result]);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoCreditDebit = new com.see.truetransact.uicomponent.CButtonGroup();
        panFreeze = new com.see.truetransact.uicomponent.CPanel();
        panTable = new com.see.truetransact.uicomponent.CPanel();
        panEligibilityDetails = new com.see.truetransact.uicomponent.CPanel();
        panDateEntyDetails = new com.see.truetransact.uicomponent.CPanel();
        rdoMds = new com.see.truetransact.uicomponent.CRadioButton();
        rdoDeposit = new com.see.truetransact.uicomponent.CRadioButton();
        panMDS = new com.see.truetransact.uicomponent.CPanel();
        lblGroupName = new com.see.truetransact.uicomponent.CLabel();
        txtGroupName = new com.see.truetransact.uicomponent.CTextField();
        lblCount = new com.see.truetransact.uicomponent.CLabel();
        txtCount = new com.see.truetransact.uicomponent.CTextField();
        cLabel3 = new com.see.truetransact.uicomponent.CLabel();
        txtDepositAmt = new com.see.truetransact.uicomponent.CTextField();
        panDate = new com.see.truetransact.uicomponent.CPanel();
        tdtStartDate = new com.see.truetransact.uicomponent.CDateField();
        lblStartDate = new com.see.truetransact.uicomponent.CLabel();
        lblEndDate = new com.see.truetransact.uicomponent.CLabel();
        tdtEndDate = new com.see.truetransact.uicomponent.CDateField();
        lblProductType = new com.see.truetransact.uicomponent.CLabel();
        cboProductType = new com.see.truetransact.uicomponent.CComboBox();
        panDeposit = new com.see.truetransact.uicomponent.CPanel();
        panIntDetails = new com.see.truetransact.uicomponent.CPanel();
        lblInterestAmount = new com.see.truetransact.uicomponent.CLabel();
        cboInterestAmount = new com.see.truetransact.uicomponent.CComboBox();
        txtInterestAmount = new com.see.truetransact.uicomponent.CTextField();
        lblInterestRecovery = new com.see.truetransact.uicomponent.CLabel();
        cboInterestRecovery = new com.see.truetransact.uicomponent.CComboBox();
        txtInterestRecovery = new com.see.truetransact.uicomponent.CTextField();
        chkIsIntRecovery = new com.see.truetransact.uicomponent.CCheckBox();
        chkIsIntForDue = new com.see.truetransact.uicomponent.CCheckBox();
        cLabel5 = new com.see.truetransact.uicomponent.CLabel();
        cboDueIntCalcType = new com.see.truetransact.uicomponent.CComboBox();
        txtDueIntCalcType = new com.see.truetransact.uicomponent.CTextField();
        panPenalDetails = new com.see.truetransact.uicomponent.CPanel();
        lbPenalCalculation = new com.see.truetransact.uicomponent.CLabel();
        cboPenalCalculation = new com.see.truetransact.uicomponent.CComboBox();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        txtPrizedPenal = new com.see.truetransact.uicomponent.CTextField();
        cLabel2 = new com.see.truetransact.uicomponent.CLabel();
        txtNonPrizedPenal = new com.see.truetransact.uicomponent.CTextField();
        panPrematureClosing = new com.see.truetransact.uicomponent.CPanel();
        cLabel4 = new com.see.truetransact.uicomponent.CLabel();
        cboPrematureIntRecType = new com.see.truetransact.uicomponent.CComboBox();
        txtPrematureIntRecAmt = new com.see.truetransact.uicomponent.CTextField();
        sptDetails = new com.see.truetransact.uicomponent.CSeparator();
        tbrOperativeAcctProduct = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace73 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace74 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace75 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace76 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace77 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace78 = new com.see.truetransact.uicomponent.CLabel();
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
        setTitle("Freeze/Unfreeze");
        setMinimumSize(new java.awt.Dimension(800, 440));
        setPreferredSize(new java.awt.Dimension(800, 550));

        panTable.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        panEligibilityDetails.setMinimumSize(new java.awt.Dimension(270, 204));
        panEligibilityDetails.setPreferredSize(new java.awt.Dimension(270, 204));

        panDateEntyDetails.setMinimumSize(new java.awt.Dimension(300, 170));
        panDateEntyDetails.setPreferredSize(new java.awt.Dimension(300, 170));

        rdoMds.setText("MDS");
        rdoMds.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoMdsActionPerformed(evt);
            }
        });

        rdoDeposit.setText("Deposit");
        rdoDeposit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDepositActionPerformed(evt);
            }
        });

        lblGroupName.setText("Group Name");

        txtGroupName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtGroupNameActionPerformed(evt);
            }
        });

        lblCount.setText("Count");

        txtCount.setAllowNumber(true);

        cLabel3.setText("Deposit Amount");

        txtDepositAmt.setAllowNumber(true);

        javax.swing.GroupLayout panMDSLayout = new javax.swing.GroupLayout(panMDS);
        panMDS.setLayout(panMDSLayout);
        panMDSLayout.setHorizontalGroup(
            panMDSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panMDSLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panMDSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panMDSLayout.createSequentialGroup()
                        .addGroup(panMDSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblGroupName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(54, 54, 54)
                        .addGroup(panMDSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtGroupName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panMDSLayout.createSequentialGroup()
                        .addComponent(cLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtDepositAmt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panMDSLayout.setVerticalGroup(
            panMDSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panMDSLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(panMDSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtGroupName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblGroupName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panMDSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panMDSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDepositAmt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        lblStartDate.setText("Start Date");

        lblEndDate.setText("End Date");

        lblProductType.setText("Product Type");

        cboProductType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProductTypeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panDateLayout = new javax.swing.GroupLayout(panDate);
        panDate.setLayout(panDateLayout);
        panDateLayout.setHorizontalGroup(
            panDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panDateLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panDateLayout.createSequentialGroup()
                        .addComponent(lblStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(tdtStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panDateLayout.createSequentialGroup()
                        .addGroup(panDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblProductType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tdtEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboProductType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(60, Short.MAX_VALUE))
        );
        panDateLayout.setVerticalGroup(
            panDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panDateLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tdtStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tdtEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblProductType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboProductType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout panDateEntyDetailsLayout = new javax.swing.GroupLayout(panDateEntyDetails);
        panDateEntyDetails.setLayout(panDateEntyDetailsLayout);
        panDateEntyDetailsLayout.setHorizontalGroup(
            panDateEntyDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panDateEntyDetailsLayout.createSequentialGroup()
                .addGroup(panDateEntyDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panDateEntyDetailsLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(panMDS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panDateEntyDetailsLayout.createSequentialGroup()
                        .addGap(92, 92, 92)
                        .addComponent(rdoDeposit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(rdoMds, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(58, 58, 58)
                .addComponent(panDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(169, Short.MAX_VALUE))
        );
        panDateEntyDetailsLayout.setVerticalGroup(
            panDateEntyDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panDateEntyDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panDateEntyDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panDateEntyDetailsLayout.createSequentialGroup()
                        .addGroup(panDateEntyDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rdoDeposit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rdoMds, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(panMDS, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panDateEntyDetailsLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(panDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(23, 23, 23))))
        );

        panIntDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Interest Calc Details"));

        lblInterestAmount.setText("Interest Amount");

        cboInterestAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboInterestAmountActionPerformed(evt);
            }
        });

        txtInterestAmount.setAllowNumber(true);

        lblInterestRecovery.setText("Interest Recovery");

        cboInterestRecovery.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboInterestRecoveryActionPerformed(evt);
            }
        });

        txtInterestRecovery.setAllowNumber(true);
        txtInterestRecovery.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtInterestRecoveryActionPerformed(evt);
            }
        });

        chkIsIntRecovery.setText("Is Interest Recovery");
        chkIsIntRecovery.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkIsIntRecoveryActionPerformed(evt);
            }
        });

        chkIsIntForDue.setText("Is Int Calc Before Full Payment");
        chkIsIntForDue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkIsIntForDueActionPerformed(evt);
            }
        });

        cLabel5.setText("Int Calc Ttype");

        txtDueIntCalcType.setAllowNumber(true);

        javax.swing.GroupLayout panIntDetailsLayout = new javax.swing.GroupLayout(panIntDetails);
        panIntDetails.setLayout(panIntDetailsLayout);
        panIntDetailsLayout.setHorizontalGroup(
            panIntDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(chkIsIntRecovery, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(panIntDetailsLayout.createSequentialGroup()
                .addGroup(panIntDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(panIntDetailsLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(cLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cboDueIntCalcType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panIntDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(chkIsIntForDue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(panIntDetailsLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(lblInterestRecovery, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(cboInterestRecovery, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panIntDetailsLayout.createSequentialGroup()
                        .addComponent(lblInterestAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cboInterestAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panIntDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtInterestRecovery, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                    .addComponent(txtInterestAmount, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                    .addComponent(txtDueIntCalcType, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)))
        );
        panIntDetailsLayout.setVerticalGroup(
            panIntDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panIntDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panIntDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblInterestAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboInterestAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtInterestAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addComponent(chkIsIntRecovery, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panIntDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblInterestRecovery, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboInterestRecovery, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtInterestRecovery, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(chkIsIntForDue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panIntDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboDueIntCalcType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDueIntCalcType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panPenalDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Penal Calc Details"));

        lbPenalCalculation.setText("Penal Calculation");

        cLabel1.setText("Prized");

        txtPrizedPenal.setAllowNumber(true);

        cLabel2.setText("Non Prized");

        txtNonPrizedPenal.setAllowNumber(true);

        javax.swing.GroupLayout panPenalDetailsLayout = new javax.swing.GroupLayout(panPenalDetails);
        panPenalDetails.setLayout(panPenalDetailsLayout);
        panPenalDetailsLayout.setHorizontalGroup(
            panPenalDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panPenalDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panPenalDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panPenalDetailsLayout.createSequentialGroup()
                        .addComponent(lbPenalCalculation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cboPenalCalculation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panPenalDetailsLayout.createSequentialGroup()
                        .addComponent(cLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtPrizedPenal, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtNonPrizedPenal, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(32, Short.MAX_VALUE))
        );
        panPenalDetailsLayout.setVerticalGroup(
            panPenalDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panPenalDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panPenalDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbPenalCalculation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboPenalCalculation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panPenalDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPrizedPenal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNonPrizedPenal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        panPrematureClosing.setBorder(javax.swing.BorderFactory.createTitledBorder("Premature Closing Details"));

        cLabel4.setText("Premature Int Recovery");

        txtPrematureIntRecAmt.setAllowNumber(true);
        txtPrematureIntRecAmt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPrematureIntRecAmtActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panPrematureClosingLayout = new javax.swing.GroupLayout(panPrematureClosing);
        panPrematureClosing.setLayout(panPrematureClosingLayout);
        panPrematureClosingLayout.setHorizontalGroup(
            panPrematureClosingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panPrematureClosingLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboPrematureIntRecType, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPrematureIntRecAmt, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panPrematureClosingLayout.setVerticalGroup(
            panPrematureClosingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panPrematureClosingLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panPrematureClosingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboPrematureIntRecType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPrematureIntRecAmt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panDepositLayout = new javax.swing.GroupLayout(panDeposit);
        panDeposit.setLayout(panDepositLayout);
        panDepositLayout.setHorizontalGroup(
            panDepositLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panDepositLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panDepositLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panPenalDetails, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panPrematureClosing, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panIntDetails, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(158, 158, 158))
        );
        panDepositLayout.setVerticalGroup(
            panDepositLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panDepositLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panDepositLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panDepositLayout.createSequentialGroup()
                        .addComponent(panPenalDetails, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(panPrematureClosing, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(69, 69, 69))
                    .addGroup(panDepositLayout.createSequentialGroup()
                        .addComponent(panIntDetails, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        javax.swing.GroupLayout panEligibilityDetailsLayout = new javax.swing.GroupLayout(panEligibilityDetails);
        panEligibilityDetails.setLayout(panEligibilityDetailsLayout);
        panEligibilityDetailsLayout.setHorizontalGroup(
            panEligibilityDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panEligibilityDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panEligibilityDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panDateEntyDetails, javax.swing.GroupLayout.DEFAULT_SIZE, 742, Short.MAX_VALUE)
                    .addComponent(panDeposit, javax.swing.GroupLayout.DEFAULT_SIZE, 742, Short.MAX_VALUE))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        panEligibilityDetailsLayout.setVerticalGroup(
            panEligibilityDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panEligibilityDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panDateEntyDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 162, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panDeposit, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );

        sptDetails.setOrientation(javax.swing.SwingConstants.VERTICAL);
        sptDetails.setPreferredSize(new java.awt.Dimension(5, 5));

        javax.swing.GroupLayout panTableLayout = new javax.swing.GroupLayout(panTable);
        panTable.setLayout(panTableLayout);
        panTableLayout.setHorizontalGroup(
            panTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panTableLayout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(panEligibilityDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 777, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sptDetails, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panTableLayout.setVerticalGroup(
            panTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panTableLayout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addComponent(sptDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(139, Short.MAX_VALUE))
            .addComponent(panEligibilityDetails, javax.swing.GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout panFreezeLayout = new javax.swing.GroupLayout(panFreeze);
        panFreeze.setLayout(panFreezeLayout);
        panFreezeLayout.setHorizontalGroup(
            panFreezeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panFreezeLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(panTable, javax.swing.GroupLayout.PREFERRED_SIZE, 837, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panFreezeLayout.setVerticalGroup(
            panFreezeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panFreezeLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(panTable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        getContentPane().add(panFreeze, java.awt.BorderLayout.CENTER);

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

        lblSpace73.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace73.setText("     ");
        lblSpace73.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace73);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnEdit);

        lblSpace74.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace74.setText("     ");
        lblSpace74.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace74);

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

        lblSpace75.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace75.setText("     ");
        lblSpace75.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace75);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnCancel);

        lblSpace4.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace4);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnAuthorize);

        lblSpace76.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace76.setText("     ");
        lblSpace76.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace76.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace76.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace76);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnException);

        lblSpace77.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace77.setText("     ");
        lblSpace77.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace77.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace77.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace77);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnReject);

        lblSpace3.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace3);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnPrint);

        lblSpace78.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace78.setText("     ");
        lblSpace78.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace78.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace78.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace78);

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
        panMDS.setEnabled(false);
        panDeposit.setEnabled(false);
        panDate.setEnabled(false);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);

    }//GEN-LAST:event_btnAuthorizeActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // Add your handling code here:
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
       observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        observable.resetStatus();
        observable.resetForm();
        formButtonsEnableDisable();
        observable.setStatus();
        updateTab = -1;
        viewType = -1;
        //__ Make the Screen Closable..
        setModified(false);

        observable.ttNotifyObservers();
        resetUIFields();
        btnDelete.setEnabled(true);
        ClientUtil.enableDisable(this, false);
        btnNew.setEnabled(true);
        btnSave.setEnabled(false);
        btnEdit.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        btnReject.setEnabled(true);
        rdoMds.setSelected(false);
        rdoDeposit.setSelected(false);
        //__ Make the Screen Closable..
        setModified(false);


    }//GEN-LAST:event_btnCancelActionPerformed
    private void resetUIFields() {
        rdoMds.setSelected(false);
        rdoDeposit.setSelected(false);
        txtGroupName.setText("");
        txtCount.setText("");
        cboProductType.setSelectedIndex(0);
        cboInterestAmount.setSelectedIndex(0);
        txtInterestAmount.setText("");
        cboPenalCalculation.setSelectedIndex(0);
        txtPrizedPenal.setText("");
        txtNonPrizedPenal.setText("");
//        cboItxtPrizedPenalSelectedIndex(0);
        txtInterestRecovery.setText("");
        cboPrematureIntRecType.setSelectedIndex(0);
        txtPrematureIntRecAmt.setText("");
        txtDepositAmt.setText("");
        tdtStartDate.setDateValue("");
        tdtEndDate.setDateValue("");

    }
    
      private boolean checkIdGenerationcount(){
        boolean flag = false;
        if(rdoDeposit.isSelected()){
            List idList = ClientUtil.executeQuery("getGroupDepositAutoGenerateCount", null);
            if(idList != null && idList.size() > 0){
                HashMap idMap = (HashMap)idList.get(0);
                if(idMap.containsKey("CNT") && idMap.get("CNT") != null && CommonUtil.convertObjToInt(idMap.get("CNT")) == 0){
                    HashMap groupMap =  new HashMap();
                    groupMap.put("GROUP_NO",txtGroupName.getText());
                    List groupList = ClientUtil.executeQuery("getGroupDepositNoAlreadyExists", groupMap);
                    if(groupList != null && groupList.size() > 0){
                        flag = true;
                    }                    
                }
            }
        }
        return flag;
    }
    
    
    
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        setModified(true);
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panDateEntyDetails);
        if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0) {
            displayAlert(mandatoryMessage);
        } else {
            if(rdoDeposit.isSelected() && checkIdGenerationcount()){
                ClientUtil.displayAlert("Deposit Group already exists !!!");
            }else if(txtGroupName.getText().length() == 0){
                 ClientUtil.displayAlert("Group Name should not be null !!!");
            }else{
                 savePerformed();
            }
        }
    }//GEN-LAST:event_btnSaveActionPerformed
    private void savePerformed() {
        updateOBFields();
        String action;
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            action = CommonConstants.TOSTATUS_INSERT;
            saveAction(action);
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            action = CommonConstants.TOSTATUS_UPDATE;
            saveAction(action);
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
            action = CommonConstants.TOSTATUS_DELETE;
            saveAction(action);
        }
    }

    private void saveAction(String status) {      

        try {
            String txtGroupName = CommonUtil.convertObjToStr(observable.getTxtGroupName());
            String txtCount = CommonUtil.convertObjToStr(observable.getTxtCount());
            Date tdtStartDate = DateUtil.getDateMMDDYYYY(observable.getTdtStartDate());
            Date tdtEndtDate = DateUtil.getDateMMDDYYYY(observable.getTdtEndDate());
            String cboProductType = CommonUtil.convertObjToStr(observable.getCboProductType());
            String cboInterestAmount = CommonUtil.convertObjToStr(observable.getCboInterestAmount());
            String txtInterestAmount = CommonUtil.convertObjToStr(observable.getTxtInterestAmount());
            String cboPenalCalculation = CommonUtil.convertObjToStr(observable.getCboPenalCalculation());
            String txtPrizedPenal = CommonUtil.convertObjToStr(observable.getTxtPrizedPenal());
            String txtNonPrizedPenal = CommonUtil.convertObjToStr(observable.getTxtNonPrizedPenal());
            String cboInterRecovery = CommonUtil.convertObjToStr(observable.getCboInterRecovery());
            String txtInterestRecovery = CommonUtil.convertObjToStr(observable.getTxtInterestRecovery());
            String cboPrematureIntRecType = CommonUtil.convertObjToStr(observable.getCboPrematureIntRecType());
            String txtPrematureIntRecAmt = CommonUtil.convertObjToStr(observable.getTxtPrematureIntRecAmt());
            String txtDepositAmt = CommonUtil.convertObjToStr(observable.getTxtDepositAmt());
            String groupType;

            final String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panDateEntyDetails);

            if (rdoMds.isSelected() && (txtCount == null || txtGroupName == null || tdtStartDate == null || tdtEndtDate == null)) {
                if (txtGroupName == null || txtGroupName.length() <= 0) {
                    ClientUtil.displayAlert("Enter the Group Name");
                    return;
                }
                if (txtCount == null || txtCount.length() <= 0) {
                    ClientUtil.displayAlert("Enter the Scheme Count");
                    return;
                }
                if (tdtStartDate == null || txtCount.length() <= 0) {
                    ClientUtil.displayAlert("Enter the Start Date");
                    return;
                }
                if (tdtEndDate == null || txtCount.length() <= 0) {
                    ClientUtil.displayAlert("Enter the End Date");
                    return;
                }
            }

            if (rdoDeposit.isSelected() && (txtCount == null || txtGroupName == null || tdtStartDate == null || tdtEndtDate == null)) {
                if (txtGroupName == null || txtGroupName.length() <= 0) {
                    ClientUtil.displayAlert("Enter the Group Name");
                    return;
                }
                if (txtCount == null || txtCount.length() <= 0) {
                    ClientUtil.displayAlert("Enter the Scheme Count");
                    return;
                }
                if (tdtStartDate == null) {
                    ClientUtil.displayAlert("Enter the Start Date");
                    return;
                }
                if (tdtEndDate == null) {
                    ClientUtil.displayAlert("Enter the End Date");
                    return;
                }
                if (cboProductType == null || cboProductType.length() <= 0) {
                    ClientUtil.displayAlert("Enter the End Date");
                    return;
                }                            
            } else {
                observable.execute(status);
                if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                    setButtonEnableDisable();
                    observable.resetForm();
                    btnCancelActionPerformed(null);
                }
                resetUIFields();          
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        observable.resetForm();
        observable.resetFreezeDetails();
        observable.setNewObj();
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        popUp();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        observable.resetForm();
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        popUp();
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:

        setButtonEnableDisable();
        panMDS.setEnabled(false);
        panDeposit.setEnabled(false);
        panDate.setEnabled(false);

        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.resetForm();
        observable.setNewObj();
        observable.setStatus();
        //__ To Save the data in the Internal Frame...
        setModified(true);

        rdoMds.setEnabled(true);
        rdoDeposit.setEnabled(true);
        rdoDeposit.setSelected(false);
        rdoMds.setSelected(false);

    }//GEN-LAST:event_btnNewActionPerformed

    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // Add your handling code here:
        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed

    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // Add your handling code here:
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed

    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // Add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed

    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // Add your handling code here:
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed

    private void rdoMdsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoMdsActionPerformed
        // TODO add your handling code here:

      //  rdoDeposit.setEnabled(false);
        rdoDeposit.setSelected(false);
        ClientUtil.enableDisable(panMDS, true);
        ClientUtil.enableDisable(panDate, true);
        ClientUtil.enableDisable(panDeposit, false);
        cboProductType.setEnabled(true);
        String prodId = "GDS";
        cboProductType.removeAllItems();
        boolean groupExists = observable.populateProdTypeCombo(prodId);
        if (groupExists) {
            cboProductType.setModel(observable.getCbmProductType());
        }
            
        
    }//GEN-LAST:event_rdoMdsActionPerformed

    private void rdoDepositActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDepositActionPerformed
        // TODO add your handling code here:
        //rdoMds.setEnabled(false);
        cboProductType.removeAllItems();
        rdoMds.setSelected(false);
        ClientUtil.enableDisable(panMDS, true);
        ClientUtil.enableDisable(panDeposit, true);
        ClientUtil.enableDisable(panDate, true);
        cboInterestRecovery.setEnabled(false);
        txtInterestRecovery.setEnabled(false);
        cboDueIntCalcType.setEnabled(false);
        txtDueIntCalcType.setEnabled(false);
        String prodId = "DEPOSIT";
        boolean groupExists = observable.populateProdTypeCombo(prodId);
        if (groupExists) {
            cboProductType.setModel(observable.getCbmProductType());
        }
    }//GEN-LAST:event_rdoDepositActionPerformed

    private void txtGroupNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtGroupNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGroupNameActionPerformed

    private void cboProductTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProductTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboProductTypeActionPerformed

    private void cboInterestAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboInterestAmountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboInterestAmountActionPerformed

    private void txtInterestRecoveryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtInterestRecoveryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtInterestRecoveryActionPerformed

    private void txtPrematureIntRecAmtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrematureIntRecAmtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrematureIntRecAmtActionPerformed

    private void cboInterestRecoveryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboInterestRecoveryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboInterestRecoveryActionPerformed

    private void chkIsIntRecoveryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkIsIntRecoveryActionPerformed
        // TODO add your handling code here:
        if (chkIsIntRecovery.isSelected()) {
            cboInterestRecovery.setEnabled(true);
            txtInterestRecovery.setEnabled(true);
        } else {
            cboInterestRecovery.setEnabled(false);
            txtInterestRecovery.setEnabled(false);
        }
    }//GEN-LAST:event_chkIsIntRecoveryActionPerformed

    private void chkIsIntForDueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkIsIntForDueActionPerformed
        // TODO add your handling code here:
        if (chkIsIntForDue.isSelected()) {
            cboDueIntCalcType.setEnabled(true);
            txtDueIntCalcType.setEnabled(true);
        } else {
            cboDueIntCalcType.setEnabled(false);
            txtDueIntCalcType.setEnabled(false);
        }
    }//GEN-LAST:event_chkIsIntForDueActionPerformed
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

    private void whenTableRowSelected(HashMap paramMap) {
        String lockedBy = "";
        HashMap map = new HashMap();
        map.put("SCREEN_ID", getScreenID());
        map.put("RECORD_KEY", paramMap.get("FREEZE_NO"));
        map.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
        map.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
        map.put("CUR_DATE", currDt.clone());
        System.out.println("Record Key Map : " + map);
        List lstLock = ClientUtil.executeQuery("selectEditLock", map);
        if (lstLock.size() > 0) {
            lockedBy = CommonUtil.convertObjToStr(lstLock.get(0));
            if (!lockedBy.equals(ProxyParameters.USER_ID)) {
                btnSave.setEnabled(false);
            } else {
                btnSave.setEnabled(true);
            }
        } else {
            btnSave.setEnabled(true);
        }
        setOpenForEditBy(lockedBy);
        if (lockedBy.equals("")) {
            ClientUtil.execute("insertEditLock", map);
        }
        if (lockedBy.length() > 0 && !lockedBy.equals(ProxyParameters.USER_ID)) {
            String data = getLockDetails(lockedBy, getScreenID());
            ClientUtil.showMessageWindow("Selected Record is Opened/Modified by " + lockedBy + data.toString());
            btnSave.setEnabled(false);
        }

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
       }
        lstLock = null;
        map = null;
        return data.toString();
    }

    /**
     * To populate Comboboxes
     */
    private void initComponentData() {
        //cboProductType.setModel(observable.getCbmProductType());
        cboInterestAmount.setModel(observable.getCbmInterestAmount());
        cboPenalCalculation.setModel(observable.getCbmPenalCalculation());
        cboInterestRecovery.setModel(observable.getCbmInterestRecovery());
        cboPrematureIntRecType.setModel(observable.getCbmPrematureIntRecType());
        cboDueIntCalcType.setModel(observable.getCbmDueIntCalcType());
    }

    /**
     * To display a popUp window for viewing existing data as well as to select
     * new customer for entry
     */
    private void popUp() {
        HashMap testMap = null;
        //To display customer info based on the selected ProductID
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            testMap = accountViewMap();
            new ViewAll(this, testMap, true).show();

        } else if (viewType != -1) {
            testMap = accountViewMap();
            new ViewAll(this, testMap, true).show();
        } //To display the existing accounts which are set to freezed
        else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            testMap = freezeEditMap();
            new ViewAll(this, testMap).show();

        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
            testMap = freezeDeletetMap();
            new ViewAll(this, testMap).show();
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
            testMap = freezeEditMap();
            new ViewAll(this, testMap).show();

        }
    }

    /**
     * Called by the Popup window created thru popUp method
     */
    public void fillData(Object obj) {
        try {
            HashMap hash = (HashMap) obj;
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
                fillDataEdit(hash);
                setButtonEnableDisable();
                observable.setStatus();
                checkForEdit();
            } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION) {
                fillDataEdit(hash);
                setButton4Authorize();
                btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
            }

        } catch (Exception e) {
            log.error(e);
        }

        //__ To Save the data in the Internal Frame...
        setModified(true);
    }


    private void checkForEdit() {
        // To enable disable controls for EDIT operation
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
        }
    }

    
    /**
     * To fillData for existing entry
     */
    private void fillDataEdit(HashMap hash) {

        System.out.println("Inside fill data edit :: hash :: " + hash);


        if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE) {

            observable.setGroupId(CommonUtil.convertObjToStr(hash.get("GROUP_NO")));
            observable.getData(hash);

            if (hash.get("GROUP_TYPE").equals("GDS")) {
                rdoMds.setEnabled(true);
                rdoMds.setSelected(true);
                ClientUtil.enableDisable(panMDS, true);
                ClientUtil.enableDisable(panDate, true);
                txtGroupName.setText(CommonUtil.convertObjToStr(hash.get("GROUP_NAME")));
                txtCount.setText(CommonUtil.convertObjToStr(hash.get("SCHEME_COUNT")));
                tdtStartDate.setDateValue(CommonUtil.convertObjToStr(hash.get("START_DT")));
                tdtEndDate.setDateValue(CommonUtil.convertObjToStr(hash.get("END_DT")));
                cboProductType.setSelectedItem(((ComboBoxModel) cboProductType.getModel()).getDataForKey(observable.getCbmProductType()));
            }
            if (hash.get("GROUP_TYPE").equals("DEPOSIT")) {
                ClientUtil.enableDisable(panMDS, true);
                rdoDeposit.setEnabled(true);
                rdoDeposit.setSelected(true);
                ClientUtil.enableDisable(panDate, true);
                ClientUtil.enableDisable(panDeposit, true);
                txtGroupName.setText(CommonUtil.convertObjToStr(hash.get("GROUP_NAME")));
                txtCount.setText(CommonUtil.convertObjToStr(hash.get("SCHEME_COUNT")));
                tdtStartDate.setDateValue(CommonUtil.convertObjToStr(hash.get("START_DT")));
                tdtEndDate.setDateValue(CommonUtil.convertObjToStr(hash.get("END_DT")));
                cboProductType.setSelectedItem(CommonUtil.convertObjToStr(hash.get("PROD_TYPE")));
            }

        }
//        if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION) {
//            final String FREEZEID = (String) hash.get("FREEZE_ID");
//            FREEZESTATUS = (String) hash.get("FREEZE_STATUS");
//            try {
//                observable.setAuthRowData(FREEZEID);
//            } catch (Exception e) {
//                System.out.println("Error in observable.setAuthRow(FREEZEID);");
//            }
//        }

    }

    /**
     * To get popUp data for a new entry
     */
    private HashMap accountViewMap() {
        final HashMap testMap = new HashMap();
        if (advances.equals("ADVANCES")) {
            testMap.put(CommonConstants.MAP_NAME, "Freeze.getAccountDataOD");
        } else {
            testMap.put(CommonConstants.MAP_NAME, "Freeze.getAccountData");
        }
        final HashMap whereMap = new HashMap();
        whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
        testMap.put(CommonConstants.MAP_WHERE, whereMap);

        return testMap;
    }

    /**
     * To get popUp data for already existing entries for modification
     */
    private HashMap freezeEditMap() {
        final HashMap testMap = new HashMap();
        testMap.put(CommonConstants.MAP_NAME, "getSelectGroupMDSDepositTO");
        final HashMap whereMap = new HashMap();
        whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
        testMap.put(CommonConstants.MAP_WHERE, whereMap);
        return testMap;
    }

    /**
     * To get popUp data for already existing entries for modification
     */
    private HashMap freezeDeletetMap() {
        final HashMap testMap = new HashMap();
        //__ All but Deleteda,and Authorized Records should be aba
        if (advances.equals("ADVANCES")) {
            testMap.put(CommonConstants.MAP_NAME, "getSelectFreezeDeleteListOD");
        } else {
            testMap.put(CommonConstants.MAP_NAME, "getSelectFreezeDeleteList");
        }
        final HashMap whereMap = new HashMap();
        whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
        testMap.put(CommonConstants.MAP_WHERE, whereMap);

        return testMap;
    }

    /**
     * To display customer related details based on account number
     */
    private void showCustomerName() {
        observable.getCustomerDetails();
    }

    public void update(java.util.Observable o, Object arg) {
        txtGroupName.setText(observable.getTxtGroupName());
        txtCount.setText(observable.getTxtCount());
        tdtStartDate.setDateValue(observable.getTdtStartDate());
        tdtEndDate.setDateValue(observable.getTdtEndDate());       
        if (observable.getGroupType().equalsIgnoreCase("GDS")) {
            rdoMdsActionPerformed(null);
            rdoMds.setEnabled(true);
            rdoMds.setSelected(true);
            ClientUtil.enableDisable(panMDS, true);
            ClientUtil.enableDisable(panDate, true);
            txtDepositAmt.setText((observable.getTxtDepositAmt()));
            cboProductType.setSelectedItem(observable.getCbmProductType().getDataForKey(CommonUtil.convertObjToStr(observable.getCboProductType())));
        }
        if (observable.getGroupType().equalsIgnoreCase("Deposit")) {
            rdoDepositActionPerformed(null);
            ClientUtil.enableDisable(panMDS, true);
            rdoDeposit.setEnabled(true);
            rdoDeposit.setSelected(true);
            ClientUtil.enableDisable(panDate, true);
            ClientUtil.enableDisable(panDeposit, true);            
            cboProductType.setSelectedItem(observable.getCbmProductType().getDataForKey(CommonUtil.convertObjToStr(observable.getCboProductType())));
            cboInterestAmount.setSelectedItem(observable.getCbmInterestAmount().getDataForKey(CommonUtil.convertObjToStr(observable.getCboInterestAmount())));
            txtInterestAmount.setText(observable.getTxtInterestAmount());
            cboPenalCalculation.setSelectedItem(observable.getCbmPenalCalculation().getDataForKey(CommonUtil.convertObjToStr(observable.getCboInterestAmount())));
            txtPrizedPenal.setText(observable.getTxtPrizedPenal());
            txtNonPrizedPenal.setText(observable.getTxtNonPrizedPenal());
            cboInterestRecovery.setSelectedItem(observable.getCbmInterestRecovery().getDataForKey(CommonUtil.convertObjToStr(observable.getCboInterRecovery())));
            txtInterestRecovery.setText(observable.getTxtInterestRecovery());
            cboPrematureIntRecType.setSelectedItem(observable.getCbmPrematureIntRecType().getDataForKey(CommonUtil.convertObjToStr(observable.getCboPrematureIntRecType())));
            txtPrematureIntRecAmt.setText(observable.getTxtPrematureIntRecAmt());
            txtDepositAmt.setText((observable.getTxtDepositAmt()));
            cboDueIntCalcType.setSelectedItem(observable.getCbmDueIntCalcType().getDataForKey(CommonUtil.convertObjToStr(observable.getCboDueIntCalcType())));
            txtDueIntCalcType.setText(observable.getTxtDueIntCalcType());
            if(observable.getChkIsIntForDue().equalsIgnoreCase("Y")){
                chkIsIntForDue.setSelected(true);
            }else{
                chkIsIntForDue.setSelected(false);
            }
            if(observable.getChkIsIntRecovery().equalsIgnoreCase("Y")){
                chkIsIntRecovery.setSelected(true);
            }else{
                chkIsIntRecovery.setSelected(false);
            }
        }
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE) {
            rdoMds.setEnabled(false);
            ClientUtil.enableDisable(panMDS, false);
            ClientUtil.enableDisable(panDate, false);
            ClientUtil.enableDisable(panDeposit, false);
            rdoDeposit.setEnabled(false);
            rdoMds.setEnabled(false);
            txtGroupName.setEnabled(false);
            txtCount.setEnabled(false);
            tdtStartDate.setEnabled(false);
            tdtEndDate.setEnabled(false);
        }
    }

    public void updateOBFields() {

        if (rdoMds.isSelected()) {
            observable.setGroupType("GDS");
        } else {
            observable.setGroupType("Deposit");
        }
        observable.setTxtGroupName(txtGroupName.getText());
        observable.setTxtCount(txtCount.getText());
        observable.setCboProductType((String) ((ComboBoxModel) (cboProductType).getModel()).getKeyForSelected());       
        observable.setCboInterestAmount((String) ((ComboBoxModel) (cboInterestAmount).getModel()).getKeyForSelected());       
        observable.setTxtInterestAmount(txtInterestAmount.getText());
        observable.setCboPenalCalculation((String) ((ComboBoxModel) (cboPenalCalculation).getModel()).getKeyForSelected());        
        observable.setTxtPrizedPenal(txtPrizedPenal.getText());
        observable.setTxtNonPrizedPenal(txtNonPrizedPenal.getText());
        observable.setCboInterestRecovery((String) ((ComboBoxModel) (cboInterestRecovery).getModel()).getKeyForSelected());        
        observable.setTxtInterestRecovery(txtInterestRecovery.getText());
        observable.setTdtStartDate(tdtStartDate.getDateValue());
        observable.setTdtEndDate(tdtEndDate.getDateValue());
        observable.setCboPrematureIntRecType((String) ((ComboBoxModel) (cboPrematureIntRecType).getModel()).getKeyForSelected());
        observable.setTxtPrematureIntRecAmt(txtPrematureIntRecAmt.getText());
        observable.setTxtDepositAmt(txtDepositAmt.getText());
        observable.setCboDueIntCalcType((String) ((ComboBoxModel) (cboDueIntCalcType).getModel()).getKeyForSelected());
        observable.setTxtDueIntCalcType(txtDueIntCalcType.getText());
        if(chkIsIntRecovery.isSelected()){
            observable.setChkIsIntRecovery("Y");
        }else{
            observable.setChkIsIntRecovery("N");
        }
        
        if(chkIsIntForDue.isSelected()){
            observable.setChkIsIntForDue("Y");
        }else{
            observable.setChkIsIntForDue("N");
        }
        
    }

    private void setFieldNames() {
        lblGroupName.setName("txtRemarks");
        lblCount.setName("txtRemarks");
        txtCount.setName("txtRemarks");
        panDateEntyDetails.setName("txtRemarks");
        panEligibilityDetails.setName("txtRemarks");
//        panTableList.setName("txtRemarks");

    }

//    private void internationalize() {
//     }

    public void setHelpMessage() {
        LoanEligibilityMaintenanceMRB objMandatoryRB = new LoanEligibilityMaintenanceMRB();
    }

    private void enableDisable(boolean yesno) {
        ClientUtil.enableDisable(this, yesno);
    }

    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());

        btnSave.setEnabled(!btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());

        btnAuthorize.setEnabled(btnNew.isEnabled());
        btnReject.setEnabled(btnNew.isEnabled());
        btnException.setEnabled(btnNew.isEnabled());

        btnView.setEnabled(!btnView.isEnabled());
    }

    private void setButton4Authorize() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(btnNew.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());

        btnSave.setEnabled(btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());

        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
    }


    private void formButtonsEnableDisable() {
        enableDisable(false);
        setButtonEnableDisable();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CLabel cLabel2;
    private com.see.truetransact.uicomponent.CLabel cLabel3;
    private com.see.truetransact.uicomponent.CLabel cLabel4;
    private com.see.truetransact.uicomponent.CLabel cLabel5;
    private com.see.truetransact.uicomponent.CComboBox cboDueIntCalcType;
    private com.see.truetransact.uicomponent.CComboBox cboInterestAmount;
    private com.see.truetransact.uicomponent.CComboBox cboInterestRecovery;
    private com.see.truetransact.uicomponent.CComboBox cboPenalCalculation;
    private com.see.truetransact.uicomponent.CComboBox cboPrematureIntRecType;
    private com.see.truetransact.uicomponent.CComboBox cboProductType;
    private com.see.truetransact.uicomponent.CCheckBox chkIsIntForDue;
    private com.see.truetransact.uicomponent.CCheckBox chkIsIntRecovery;
    private com.see.truetransact.uicomponent.CLabel lbPenalCalculation;
    private com.see.truetransact.uicomponent.CLabel lblCount;
    private com.see.truetransact.uicomponent.CLabel lblEndDate;
    private com.see.truetransact.uicomponent.CLabel lblGroupName;
    private com.see.truetransact.uicomponent.CLabel lblInterestAmount;
    private com.see.truetransact.uicomponent.CLabel lblInterestRecovery;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblProductType;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace73;
    private com.see.truetransact.uicomponent.CLabel lblSpace74;
    private com.see.truetransact.uicomponent.CLabel lblSpace75;
    private com.see.truetransact.uicomponent.CLabel lblSpace76;
    private com.see.truetransact.uicomponent.CLabel lblSpace77;
    private com.see.truetransact.uicomponent.CLabel lblSpace78;
    private com.see.truetransact.uicomponent.CLabel lblStartDate;
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
    private com.see.truetransact.uicomponent.CPanel panDate;
    private com.see.truetransact.uicomponent.CPanel panDateEntyDetails;
    private com.see.truetransact.uicomponent.CPanel panDeposit;
    private com.see.truetransact.uicomponent.CPanel panEligibilityDetails;
    private com.see.truetransact.uicomponent.CPanel panFreeze;
    private com.see.truetransact.uicomponent.CPanel panIntDetails;
    private com.see.truetransact.uicomponent.CPanel panMDS;
    private com.see.truetransact.uicomponent.CPanel panPenalDetails;
    private com.see.truetransact.uicomponent.CPanel panPrematureClosing;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTable;
    private com.see.truetransact.uicomponent.CButtonGroup rdoCreditDebit;
    private com.see.truetransact.uicomponent.CRadioButton rdoDeposit;
    private com.see.truetransact.uicomponent.CRadioButton rdoMds;
    private com.see.truetransact.uicomponent.CSeparator sptDetails;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private javax.swing.JToolBar tbrOperativeAcctProduct;
    private com.see.truetransact.uicomponent.CDateField tdtEndDate;
    private com.see.truetransact.uicomponent.CDateField tdtStartDate;
    private com.see.truetransact.uicomponent.CTextField txtCount;
    private com.see.truetransact.uicomponent.CTextField txtDepositAmt;
    private com.see.truetransact.uicomponent.CTextField txtDueIntCalcType;
    private com.see.truetransact.uicomponent.CTextField txtGroupName;
    private com.see.truetransact.uicomponent.CTextField txtInterestAmount;
    private com.see.truetransact.uicomponent.CTextField txtInterestRecovery;
    private com.see.truetransact.uicomponent.CTextField txtNonPrizedPenal;
    private com.see.truetransact.uicomponent.CTextField txtPrematureIntRecAmt;
    private com.see.truetransact.uicomponent.CTextField txtPrizedPenal;
    // End of variables declaration//GEN-END:variables
   
    DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {

        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            //To set the row color as Red if the data for the row is set to be deleted, else set normal color
            if (observable.isDeleted(row)) {
                setForeground(table.getForeground());
                setBackground(Color.red);
            } else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }

            //To set the default selection background color if a row is selected
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            }

            /*
             * Set oquae
             */
            this.setOpaque(true);
            return this;
        }
    };

    public static void main(String[] args) {
        GroupMDSDepositUI fui = new GroupMDSDepositUI();
        javax.swing.JFrame j = new javax.swing.JFrame();
        j.getContentPane().add(fui);
        j.show();
        fui.show();
    }
}
