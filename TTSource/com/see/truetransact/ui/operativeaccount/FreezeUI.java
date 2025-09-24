/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * FreezeUI.java
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
 * @author  annamalai_t1
 * modified by Karthik
 */
public class FreezeUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer,UIMandatoryField {
    private HashMap mandatoryMap;
    private FreezeOB observable;
    int updateTab = -1, viewType = -1;
    String FREEZESTATUS = "";
    boolean flag = false;
    private final String TYPE = "COMPLETE";
    int rowSelected = -1;
    private String advances="";
    private Date currDt = null;
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.operativeaccount.FreezeRB", ProxyParameters.LANGUAGE);
    
    private final static Logger log = Logger.getLogger(FreezeUI.class);
    /** Creates new form FreezeUI */
    public FreezeUI() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        initStartUp();
    }
    public FreezeUI(String advance) {
        currDt = ClientUtil.getCurrentDate();
        this.advances=advance;
        initComponents();
        initStartUp();
    }
    private void initStartUp(){
        setMandatoryHashMap();
        setFieldNames();
        internationalize();
        if(advances.equals("ADVANCES"))
            observable = new FreezeOB(advances);
        else
            observable = new FreezeOB();
        observable.setAdvances(advances);
        observable.addObserver(this);
        update(observable, null);
        initComponentData();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panProductInnerMost);
        tblFreezeList.setDefaultRenderer(Object.class, renderer);
        formButtonsEnableDisable();
        setHelpMessage();
        
        setMaxLength();
        btnDelete.setEnabled(false);
        lblOD.setVisible(false); // Added by nithya 
    }
    
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtAmount", new Boolean(true));
        mandatoryMap.put("tdtDate", new Boolean(true));
        mandatoryMap.put("cboType", new Boolean(true));
        mandatoryMap.put("rdoCreditDebit_Credit", new Boolean(true));
        mandatoryMap.put("txtRemarks", new Boolean(true));
        mandatoryMap.put("cboProductID", new Boolean(true));
        mandatoryMap.put("txtAccountNumber", new Boolean(true));
    }
    
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    private void setMaxLength(){
        txtAmount.setMaxLength(16);
        txtAmount.setValidation(new CurrencyValidation(14,2));
        txtAccountNumber.setMaxLength(16);
        txtAccountNumber.setAllowAll(true);
    }
    
    public void authorizeStatus(String actionPerformed) {
        if((!btnNew.isEnabled()) && (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION)) {
            System.out.println("btnNew is Disable...");
            HashMap singleAuthorizeMap = new HashMap();
            ArrayList arrList = new ArrayList();
            HashMap authDataMap = new HashMap();
            int row = observable.getAuthRow();
            
            authDataMap.put("ACT_NUM", txtAccountNumber.getText());
            authDataMap.put("FREEZE_ID", (String)tblFreezeList.getValueAt(row,1));
            authDataMap.put("FREEZE_AMT", txtAmount.getText());
            authDataMap.put("FREEZE_STATUS", FREEZESTATUS);
            authDataMap.put(CommonConstants.AUTHORIZEDT, currDt.clone());
            authDataMap.put("FREEZE_TYPE", (String)(((ComboBoxModel)(cboType).getModel())).getKeyForSelected());
            arrList.add(authDataMap);
            
            singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, actionPerformed);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            super.setOpenForEditBy(observable.getStatusBy());
            super.removeEditLock((String)tblFreezeList.getValueAt(row,1));
            authorize(singleAuthorizeMap);
            lblStatus.setText(actionPerformed);
            
        }else{
            System.out.println("btnNew is Enable...");
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
            //            whereMap.put("BEHAVIOR", null);
            if(advances.equals("ADVANCES"))
                mapParam.put(CommonConstants.MAP_NAME, "getFreezeAccountAuthorizeTOListOD");
            else
                mapParam.put(CommonConstants.MAP_NAME, "getFreezeAccountAuthorizeTOList");
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            
            System.out.println("mapParam: "+mapParam);
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
    
    
    
    public void authorize(HashMap map) {
        System.out.println("Authorize Map : " + map);
        map.put("USER_ID", TrueTransactMain.USER_ID);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        observable.setAuthorizeMap(map);
        observable.doAction();
        
        int result = observable.getResult();
        
        //        formButtonsEnableDisable();
        //        observable.resetObjects();
        //        observable.resetForm();
        //        /*
        //         * To Reset the Screen after the Authorization...
        //         */
        //        observable.resetFreezeDetails();
        //        observable.setStatus();
        //        observable.setAuthorizeMap(null);
        //
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
        panFreeze = new com.see.truetransact.uicomponent.CPanel();
        panTable = new com.see.truetransact.uicomponent.CPanel();
        panFreezeDetails = new com.see.truetransact.uicomponent.CPanel();
        panFreezeSave = new com.see.truetransact.uicomponent.CPanel();
        btnFreezeSave = new com.see.truetransact.uicomponent.CButton();
        btnFreezeNew = new com.see.truetransact.uicomponent.CButton();
        btnFreezeDelete = new com.see.truetransact.uicomponent.CButton();
        btnUnFreeze = new com.see.truetransact.uicomponent.CButton();
        panFreezeDetailsInner = new com.see.truetransact.uicomponent.CPanel();
        lblAmount = new com.see.truetransact.uicomponent.CLabel();
        txtAmount = new com.see.truetransact.uicomponent.CTextField();
        lblType = new com.see.truetransact.uicomponent.CLabel();
        cboType = new com.see.truetransact.uicomponent.CComboBox();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        txtRemarks = new com.see.truetransact.uicomponent.CTextField();
        lblFreezeSlNo = new com.see.truetransact.uicomponent.CLabel();
        lblFreezeSlNoDesc = new com.see.truetransact.uicomponent.CLabel();
        lblDate = new com.see.truetransact.uicomponent.CLabel();
        tdtDate = new com.see.truetransact.uicomponent.CDateField();
        panFreezeList = new com.see.truetransact.uicomponent.CPanel();
        srpFreezeList = new com.see.truetransact.uicomponent.CScrollPane();
        tblFreezeList = new com.see.truetransact.uicomponent.CTable();
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
        lblCustomerNameDisplay = new com.see.truetransact.uicomponent.CLabel();
        panAccountHead1 = new com.see.truetransact.uicomponent.CPanel();
        txtAccountNumber = new com.see.truetransact.uicomponent.CTextField();
        btnAccountNumber = new com.see.truetransact.uicomponent.CButton();
        panClearBalance = new com.see.truetransact.uicomponent.CPanel();
        lblClearBalance = new com.see.truetransact.uicomponent.CLabel();
        lblClearBalanceDisplay = new com.see.truetransact.uicomponent.CLabel();
        lblExistingFreezesSum = new com.see.truetransact.uicomponent.CLabel();
        lblExistingFreezesSumDisplay = new com.see.truetransact.uicomponent.CLabel();
        lblClearBalance1 = new com.see.truetransact.uicomponent.CLabel();
        lblClearBalanceDisplay1 = new com.see.truetransact.uicomponent.CLabel();
        lblExistingLienSum = new com.see.truetransact.uicomponent.CLabel();
        lblExistingLienSumDisplay = new com.see.truetransact.uicomponent.CLabel();
        lblOD = new com.see.truetransact.uicomponent.CLabel();
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
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace20 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace21 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
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
        setTitle("Freeze/Unfreeze");
        setMinimumSize(new java.awt.Dimension(800, 440));
        setPreferredSize(new java.awt.Dimension(800, 440));

        panFreeze.setLayout(new java.awt.GridBagLayout());

        panTable.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panTable.setLayout(new java.awt.GridBagLayout());

        panFreezeDetails.setLayout(new java.awt.GridBagLayout());

        panFreezeSave.setLayout(new java.awt.GridBagLayout());

        btnFreezeSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnFreezeSave.setToolTipText("Save");
        btnFreezeSave.setMaximumSize(new java.awt.Dimension(29, 27));
        btnFreezeSave.setMinimumSize(new java.awt.Dimension(29, 27));
        btnFreezeSave.setName("btnContactNoAdd"); // NOI18N
        btnFreezeSave.setPreferredSize(new java.awt.Dimension(29, 27));
        btnFreezeSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFreezeSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFreezeSave.add(btnFreezeSave, gridBagConstraints);

        btnFreezeNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnFreezeNew.setToolTipText("New");
        btnFreezeNew.setMaximumSize(new java.awt.Dimension(29, 27));
        btnFreezeNew.setMinimumSize(new java.awt.Dimension(29, 27));
        btnFreezeNew.setPreferredSize(new java.awt.Dimension(29, 27));
        btnFreezeNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFreezeNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFreezeSave.add(btnFreezeNew, gridBagConstraints);

        btnFreezeDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnFreezeDelete.setToolTipText("Delete");
        btnFreezeDelete.setMaximumSize(new java.awt.Dimension(29, 27));
        btnFreezeDelete.setMinimumSize(new java.awt.Dimension(29, 27));
        btnFreezeDelete.setPreferredSize(new java.awt.Dimension(29, 27));
        btnFreezeDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFreezeDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFreezeSave.add(btnFreezeDelete, gridBagConstraints);

        btnUnFreeze.setText("UnFreeze");
        btnUnFreeze.setToolTipText("UnFreeze");
        btnUnFreeze.setPreferredSize(new java.awt.Dimension(100, 27));
        btnUnFreeze.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUnFreezeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFreezeSave.add(btnUnFreeze, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        panFreezeDetails.add(panFreezeSave, gridBagConstraints);

        panFreezeDetailsInner.setLayout(new java.awt.GridBagLayout());

        lblAmount.setText("Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFreezeDetailsInner.add(lblAmount, gridBagConstraints);

        txtAmount.setMaxLength(16);
        txtAmount.setValidation(new CurrencyValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFreezeDetailsInner.add(txtAmount, gridBagConstraints);

        lblType.setText("Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFreezeDetailsInner.add(lblType, gridBagConstraints);

        cboType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFreezeDetailsInner.add(cboType, gridBagConstraints);

        lblRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFreezeDetailsInner.add(lblRemarks, gridBagConstraints);

        txtRemarks.setMaxLength(256);
        txtRemarks.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFreezeDetailsInner.add(txtRemarks, gridBagConstraints);

        lblFreezeSlNo.setText(" Sl No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFreezeDetailsInner.add(lblFreezeSlNo, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFreezeDetailsInner.add(lblFreezeSlNoDesc, gridBagConstraints);

        lblDate.setText("Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFreezeDetailsInner.add(lblDate, gridBagConstraints);

        tdtDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFreezeDetailsInner.add(tdtDate, gridBagConstraints);

        panFreezeDetails.add(panFreezeDetailsInner, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(0, 60, 0, 4);
        panTable.add(panFreezeDetails, gridBagConstraints);

        panFreezeList.setLayout(new java.awt.GridBagLayout());

        srpFreezeList.setPreferredSize(new java.awt.Dimension(454, 125));

        tblFreezeList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tblFreezeList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblFreezeListMousePressed(evt);
            }
        });
        srpFreezeList.setViewportView(tblFreezeList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panFreezeList.add(srpFreezeList, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.8;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        panTable.add(panFreezeList, gridBagConstraints);

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
        panFreeze.add(panTable, gridBagConstraints);

        panProduct.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panProduct.setLayout(new java.awt.GridBagLayout());

        panProductIDInner.setMinimumSize(new java.awt.Dimension(325, 118));
        panProductIDInner.setPreferredSize(new java.awt.Dimension(325, 118));
        panProductIDInner.setLayout(new java.awt.GridBagLayout());

        panProductInnerMost.setMinimumSize(new java.awt.Dimension(375, 118));
        panProductInnerMost.setPreferredSize(new java.awt.Dimension(375, 118));
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
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductInnerMost.add(cboProductID, gridBagConstraints);

        lblAccountHead.setText("Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductInnerMost.add(lblAccountHead, gridBagConstraints);

        panAccountHead.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 13)); // NOI18N
        panAccountHead.setMinimumSize(new java.awt.Dimension(100, 18));
        panAccountHead.setPreferredSize(new java.awt.Dimension(100, 18));
        panAccountHead.setLayout(new java.awt.GridBagLayout());

        lblAccountHeadCode.setForeground(new java.awt.Color(0, 51, 204));
        lblAccountHeadCode.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblAccountHeadCode.setPreferredSize(new java.awt.Dimension(175, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panAccountHead.add(lblAccountHeadCode, gridBagConstraints);

        lblAccountHeadDesc.setForeground(new java.awt.Color(0, 51, 204));
        lblAccountHeadDesc.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblAccountHeadDesc.setPreferredSize(new java.awt.Dimension(45, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 3);
        panAccountHead.add(lblAccountHeadDesc, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 5, 4, 0);
        panProductInnerMost.add(panAccountHead, gridBagConstraints);

        lblAccountNumber.setText("Account Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductInnerMost.add(lblAccountNumber, gridBagConstraints);

        lblCustomerNameDisplay.setForeground(new java.awt.Color(0, 51, 204));
        lblCustomerNameDisplay.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblCustomerNameDisplay.setMaximumSize(new java.awt.Dimension(150, 21));
        lblCustomerNameDisplay.setMinimumSize(new java.awt.Dimension(150, 21));
        lblCustomerNameDisplay.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 40, 4, 4);
        panProductInnerMost.add(lblCustomerNameDisplay, gridBagConstraints);

        panAccountHead1.setLayout(new java.awt.GridBagLayout());

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
        panAccountHead1.add(txtAccountNumber, gridBagConstraints);

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
        panAccountHead1.add(btnAccountNumber, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panProductInnerMost.add(panAccountHead1, gridBagConstraints);

        panProductIDInner.add(panProductInnerMost, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.ipadx = 44;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 0, 8);
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

        lblExistingFreezesSum.setText("Sum of Existing Freezes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panClearBalance.add(lblExistingFreezesSum, gridBagConstraints);

        lblExistingFreezesSumDisplay.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panClearBalance.add(lblExistingFreezesSumDisplay, gridBagConstraints);

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

        lblExistingLienSum.setText("Sum of Existing Freezes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panClearBalance.add(lblExistingLienSum, gridBagConstraints);

        lblExistingLienSumDisplay.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panClearBalance.add(lblExistingLienSumDisplay, gridBagConstraints);

        lblOD.setText("OD Amount Exists");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        panClearBalance.add(lblOD, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 0.8;
        gridBagConstraints.insets = new java.awt.Insets(4, 11, 1, 11);
        panProduct.add(panClearBalance, gridBagConstraints);

        sptProduct.setOrientation(javax.swing.SwingConstants.VERTICAL);
        sptProduct.setPreferredSize(new java.awt.Dimension(5, 5));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        panProduct.add(sptProduct, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFreeze.add(panProduct, gridBagConstraints);

        getContentPane().add(panFreeze, java.awt.BorderLayout.CENTER);

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

        mbrMain.add(mnuProcess);

        setJMenuBar(mbrMain);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtAccountNumberFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccountNumberFocusLost
        // TODO add your handling code here:
        String ACCOUNTNO = (String) txtAccountNumber.getText();
        if (ACCOUNTNO.length()>0) {
            if (observable.checkAcNoWithoutProdType(ACCOUNTNO)) {
                cboProductIDActionPerformed(null);
                txtAccountNumber.setText(observable.getTxtAccountNumber());
                ACCOUNTNO = (String) txtAccountNumber.getText();
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
//                observable.resetLienListTable();
//                observable.resetObjects();
                clearBalances();
                return;
            }
        }        
//        String PRODID= ((ComboBoxModel)cboProductID.getModel()).getKeyForSelected().toString();
//        String acct_num=txtAccountNumber.getText();
//        HashMap where =new HashMap();
//        where.put("PRODID",PRODID);
//        where.put("acct_num",acct_num);
//        List lst=null;
//        if(advances.equals("ADVANCES"))
//            lst = ClientUtil.executeQuery("Freeze.getAccountDataNumberNameAD",where);
//        else
//            lst = ClientUtil.executeQuery("Freeze.getAccountDataNumberName",where);
//         if(lst!=null && lst.size()>0){
//             where=(HashMap)lst.get(0);
//             where.put("ACCOUNT NUMBER",CommonUtil.convertObjToStr(where.get("Account Number")));
//            if(CommonUtil.convertObjToDouble(where.get("CLEAR_BALANCE")).doubleValue()>0){
//             fillData(where);
//             txtAccountNumber.setText(acct_num);
//            }else
//                 ClientUtil.showMessageWindow("Debit Balance ="+CommonUtil.convertObjToDouble(where.get("CLEAR_BALANCE")).doubleValue()+
//                 "\n"+"Not Passible for Freeze");
//         }else{
//              ClientUtil.displayAlert("Invalid Number");
//              observable.resetAccountDetails();
//              observable.resetFreezeListTable();
//              observable.resetObjects();
//               clearBalances();
//         }        
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
    
    private void tdtDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtDateFocusLost
        // TODO add your handling code here:
        //__ if Some date is Selected...
        if(tdtDate.getDateValue() != null && tdtDate.getDateValue().length() > 0){
            HashMap dataMap = new HashMap();
            dataMap.put("ACT_NUM", CommonUtil.convertObjToStr(txtAccountNumber.getText()));
            dataMap.put("F_DATE", DateUtil.getDateMMDDYYYY(tdtDate.getDateValue()));
            boolean val = observable.verifyAccountDate(dataMap);
            
            //__ if selected date is before account opening date...
            if(!val){
                //__ Reset the date...
                tdtDate.setDateValue("");
                displayAlert(resourceBundle.getString("FREEZEDATEWARNING"));
            }
        }
    }//GEN-LAST:event_tdtDateFocusLost
    
    private void cboTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboTypeActionPerformed
        double clearBalance = CommonUtil.convertObjToDouble(lblClearBalanceDisplay.getText()).doubleValue();
        double clearBalance1 = CommonUtil.convertObjToDouble(lblClearBalanceDisplay1.getText()).doubleValue();
        double freezeExist = CommonUtil.convertObjToDouble(lblExistingFreezesSumDisplay.getText()).doubleValue();
        double LienExist = CommonUtil.convertObjToDouble(lblExistingLienSumDisplay.getText()).doubleValue();
        txtAmount.setEnabled(false);
        if(observable.getActionType()!=ClientConstants.ACTIONTYPE_AUTHORIZE &&
        observable.getActionType()!=ClientConstants.ACTIONTYPE_EXCEPTION &&
        observable.getActionType()!=ClientConstants.ACTIONTYPE_REJECT ){
            String TYPE =  CommonUtil.convertObjToStr(((ComboBoxModel)(cboType.getModel())).getKeyForSelected());
            if(TYPE !=null && TYPE .length()>0){
                if(TYPE.compareToIgnoreCase(this.TYPE)==0 && observable.getLblFreezeStatus()==CommonConstants.STATUS_CREATED){
                    double bal=CommonUtil.convertObjToDouble(lblClearBalanceDisplay.getText()).doubleValue();
                     if(clearBalance1<clearBalance){
                    bal=CommonUtil.convertObjToDouble(lblClearBalanceDisplay1.getText()).doubleValue();
                    if(freezeExist>0.0||LienExist>0.0)
                        bal=bal-freezeExist-LienExist;
                     }
                    txtAmount.setText(String.valueOf(bal));
                    txtAmount.setEnabled(false);
                }else if(observable.getActionType()!=ClientConstants.ACTIONTYPE_DELETE){
                    if(observable.getLblFreezeStatus()==CommonConstants.STATUS_CREATED){
                        txtAmount.setText("");
                    }
                    /*if(TYPE.equalsIgnoreCase("CREDIT_FREEZE") || TYPE.equalsIgnoreCase("DEBIT_FREEZE") || TYPE.equalsIgnoreCase("TOTAL_FREEZE")){
                        txtAmount.setEnabled(false);
                    }else if(TYPE.equalsIgnoreCase("PARTIAL")){
                        txtAmount.setEnabled(false);
                    }else{
                        txtAmount.setEnabled(true);
                    }*/
                }
            }
        }
        txtAmount.setEnabled(false);
        txtAmount.setEditable(false);       
        if (CommonUtil.convertObjToStr(((ComboBoxModel)(cboType.getModel())).getKeyForSelected()).equalsIgnoreCase("PARTIAL")) {
            txtAmount.setEnabled(true);
            txtAmount.setEditable(true);
        }

    }//GEN-LAST:event_cboTypeActionPerformed
    
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
    
    private void btnUnFreezeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUnFreezeActionPerformed
        // Add your handling code here:
        updateUIFields();
        String unFreezeRemarks = COptionPane.showInputDialog(resourceBundle.getString("optionLblUnFreeze"));
        observable.unFreeze(unFreezeRemarks, tblFreezeList.getSelectedRow());
        ClientUtil.enableDisable(panFreezeDetailsInner,false);
        setFreezeButtonEnableDisable();
        observable.resetFreezeDetails();
        observable.ttNotifyObservers();
    }//GEN-LAST:event_btnUnFreezeActionPerformed
    
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
        for(int i=0; i<tblFreezeList.getRowCount();i++) {
            String data= CommonUtil.convertObjToStr(tblFreezeList.getValueAt(i,1));
            if(observable.getActionType() ==ClientConstants.ACTIONTYPE_EDIT)
                setMode(ClientConstants.ACTIONTYPE_EDIT);
            String status=CommonUtil.convertObjToStr((tblFreezeList.getValueAt(i,4)));
            if(status.length()>0)
                super.removeEditLock(data);
        }
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        observable.resetForm();
        observable.resetFreezeDetails();
        observable.resetObjects();
        formButtonsEnableDisable();
        observable.setStatus();
        updateTab = -1;
        viewType = -1;
        //__ Make the Screen Closable..
        setModified(false);
        
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
            lst.add("FREEZE_NO");
            lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
            if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW) {
                if (observable.getProxyReturnMap()!=null) {
                    if (observable.getProxyReturnMap().containsKey("FREEZE_NO")) {
                        lst=(ArrayList)observable.getProxyReturnMap().get("FREEZE_NO");
                        for(int i=0;i<lst.size();i++) {
                            lockMap.put("FREEZE_NO",lst.get(i));
                            setEditLockMap(lockMap);
                            setEditLock();
                        }
                    }
                }
            }
            formButtonsEnableDisable();
            observable.resetObjects();
            observable.resetForm();
            observable.resetFreezeDetails();
            observable.setResultStatus();
            
            viewType = -1;
            
            //__ Make the Screen Closable..
            setModified(false);
            
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
        observable.resetFreezeDetails();
        observable.setNewObj();
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        popUp();
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        observable.resetForm();
        observable.resetFreezeDetails();
        observable.setNewObj();
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        popUp();
        
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        enableDisable(true);
        setButtonEnableDisable();
        ClientUtil.enableDisable(this.panFreezeDetailsInner, false);
        setFreezeButtonEnableDisableDefault();
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.resetForm();
        observable.resetFreezeDetails();
        observable.resetObjects();
        observable.setNewObj();
        observable.setStatus();
        
        //__ To Save the data in the Internal Frame...
        setModified(true);
        
        updateTab = -1;
    }//GEN-LAST:event_btnNewActionPerformed
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // Add your handling code here:
        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed
    
    private void btnFreezeDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFreezeDeleteActionPerformed
        // Add your handling code here:
        updateUIFields();
        observable.deleteFreezeTab(tblFreezeList.getSelectedRow());
        observable.resetFreezeDetails();
        ClientUtil.enableDisable(this.panFreezeDetailsInner, false);
        setFreezeButtonEnableDisableDefault();
        observable.ttNotifyObservers();
    }//GEN-LAST:event_btnFreezeDeleteActionPerformed
    
    private void cboProductIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProductIDActionPerformed
        // Add your handling code here:
        observable.setCboProductID((String) cboProductID.getSelectedItem());
        //To display AccountHead details based on proper ProductID selection
        if( observable.getCboProductID().length() > 0 && observable.getTxtAccountNumber().length() >= 0 ){
            observable.getAccountHeadForProduct();
            clearBalances();
        }
        
    }//GEN-LAST:event_cboProductIDActionPerformed
    
    private void tblFreezeListMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblFreezeListMousePressed
        // Add your handling code here:
        String authorizeStatus = "";
        rowSelected = tblFreezeList.getSelectedRow();
        flag = true;
        updateUIFields();
        observable.populateFreezeDetails(tblFreezeList.getSelectedRow());
        if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE){
            //              boolean UNFREEZED = observable.getLblFreezeStatus().equalsIgnoreCase("UNFREEZED");
            //           if(!UNFREEZED){
            // if(observable.getLblAuth().equalsIgnoreCase("AUTHORIZED")){
            if( (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE) || ( observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT)){
                ClientUtil.enableDisable(this.panFreezeDetailsInner, false);
                this.btnFreezeNew.setEnabled(false);
                this.btnFreezeSave.setEnabled(false);
                this.btnFreezeDelete.setEnabled(false);
                this.btnUnFreeze.setEnabled(false);
                txtAmount.setEnabled(false);
                txtAmount.setEditable(false);
            }else {
                HashMap viewMap = new HashMap();
                if(observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT) {
                    String fslNo= lblFreezeSlNoDesc.getText();
                    viewMap.put("FREEZE_NO",fslNo);
                    whenTableRowSelected(viewMap);
                }
                authorizeStatus = observable.getLblAuth();
                if(authorizeStatus!=null && authorizeStatus.length()>0){
                    if((authorizeStatus.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED))||(authorizeStatus.equalsIgnoreCase(CommonConstants.STATUS_REJECTED))){
                        ClientUtil.enableDisable(this.panFreezeDetailsInner,false);
                        this.btnUnFreeze.setEnabled(true);
                        txtAmount.setEnabled(true);
                        txtAmount.setEnabled(true);
                        
                    }
                }
                else{
                    ClientUtil.enableDisable(this.panFreezeDetailsInner, true);
                    setFreezeButtonEnableDisableSelect();
                    txtAccountNumber.setEnabled(false);
                    this.btnUnFreeze.setEnabled(false);
                    this.btnFreezeNew.setEnabled(true);
                    tdtDate.setEnabled(false);
                    //                    updateTab = 1;
                    updateTab = tblFreezeList.getSelectedRow();
                      String freezeType =  CommonUtil.convertObjToStr(((ComboBoxModel)(cboType.getModel())).getKeyForSelected());
                      if(freezeType.equalsIgnoreCase("CREDIT_FREEZE")||freezeType.equalsIgnoreCase("DEBIT_FREEZE")||freezeType.equalsIgnoreCase("TOTAL_FREEZE"))
                          txtAmount.setEnabled(false);
                      else
                          txtAmount.setEnabled(true);
                }
            }
        }
        String status =    CommonUtil.convertObjToStr(tblFreezeList.getValueAt(rowSelected, 5));
        if(status.equalsIgnoreCase("UNFREEZED")) {
            this.btnFreezeDelete.setEnabled(false);
            this.btnFreezeSave.setEnabled(false);
        }
        if(status.equalsIgnoreCase("UNFREEZED") && (authorizeStatus.equalsIgnoreCase(CommonConstants.STATUS_REJECTED))) {
            ClientUtil.enableDisable(this.panFreezeDetailsInner,false);
            this.btnFreezeDelete.setEnabled(false);
            this.btnFreezeSave.setEnabled(false);
        }

        observable.ttNotifyObservers();
    }//GEN-LAST:event_tblFreezeListMousePressed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // Add your handling code here:
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // Add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed
    
    private void btnFreezeSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFreezeSaveActionPerformed
        int result=0;
        double sum=0.0;
        updateUIFields();
        StringBuffer str = new StringBuffer();
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panFreezeDetails);
        if(mandatoryMessage.length() > 0){
            str.append(mandatoryMessage + "\n");
        }
        double clearBalance = CommonUtil.convertObjToDouble(lblClearBalanceDisplay.getText()).doubleValue();
        double clearBal = CommonUtil.convertObjToDouble(lblClearBalanceDisplay.getText()).doubleValue();
        if(advances.equals("ADVANCES") && clearBalance<1){
            ClientUtil.showMessageWindow("This Customer Not Having Credit_balance");
            return;
        }
        double clearBalance1 = CommonUtil.convertObjToDouble(lblClearBalanceDisplay1.getText()).doubleValue();
        if(clearBalance1<clearBalance)
            clearBalance = CommonUtil.convertObjToDouble(lblClearBalanceDisplay1.getText()).doubleValue();
        if(observable.isTodProd()){
            clearBalance = CommonUtil.convertObjToDouble(lblClearBalanceDisplay.getText()).doubleValue();
        }
        double FreezeAmt = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
        double freezeExist = CommonUtil.convertObjToDouble(lblExistingFreezesSumDisplay.getText()).doubleValue();
        double LienExist = CommonUtil.convertObjToDouble(lblExistingLienSumDisplay.getText()).doubleValue();
        double FreezeTabAmt = observable.FreezeTabSum();
        if(clearBalance1<clearBal){
           sum = (clearBalance-LienExist);
        }
        else {
            sum = (clearBalance+freezeExist);
        }
        
        final String TYPE = CommonUtil.convertObjToStr(((ComboBoxModel)(cboType.getModel())).getKeyForSelected());
        if(observable.isTodProd()){
           
        }else{
            if (TYPE.equalsIgnoreCase("PARTIAL")) {
                if (clearBalance == FreezeAmt) {
                    str.append(resourceBundle.getString("PARTWARNING"));
                }
            }
        }
        
        if(TYPE.equalsIgnoreCase("COMPLETE")) {
            if(clearBalance != FreezeAmt)
                str.append(resourceBundle.getString("COMPWARNING"));
        }
        //__ if a particular row is Updated...
        if(updateTab > -1){
            if(sum  < (FreezeAmt + FreezeTabAmt - CommonUtil.convertObjToDouble(tblFreezeList.getValueAt(rowSelected, 2)).doubleValue())) {
                str.append(resourceBundle.getString("AMTWARNING"));
            }
        }else{
            if(sum < (FreezeAmt + FreezeTabAmt)){
                str.append(resourceBundle.getString("AMTWARNING"));
            }
        }
        
        if(txtAmount.getText().equals("0.00")) {
            ClientUtil.displayAlert(" Amount Cannot Be Zero");
            btnCancelActionPerformed(null);
            return;
        }
        if(tblFreezeList.getRowCount()>0){
            for(int i=0;i<tblFreezeList.getRowCount();i++){
                String freezeTypeInTable=CommonUtil.convertObjToStr(tblFreezeList.getValueAt(i,0));
                if((freezeTypeInTable.equalsIgnoreCase("PARTIAL") || freezeTypeInTable.equalsIgnoreCase("COMPLETE")) &&
                (TYPE.equalsIgnoreCase("CREDIT_FREEZE") || TYPE.equalsIgnoreCase("DEBIT_FREEZE") ||TYPE.equalsIgnoreCase("TOTAL_FREEZE"))){
                    ClientUtil.showMessageWindow("Can Select Only PARTIAL/COMPLETE option");
                    return;
                }
                if((freezeTypeInTable.equalsIgnoreCase("CREDIT_FREEZE") || freezeTypeInTable.equalsIgnoreCase("DEBIT_FREEZE") ||freezeTypeInTable.equalsIgnoreCase("TOTAL_FREEZE")) &&
                (TYPE.equalsIgnoreCase("PARTIAL") || TYPE.equalsIgnoreCase("DEBIT_FREEZE") ||TYPE.equalsIgnoreCase("COMPLETE"))){
                    ClientUtil.showMessageWindow("PARTIAL/COMPLETE option Not Allowed");
                    return;
                }
                if((freezeTypeInTable.equalsIgnoreCase("CREDIT_FREEZE") || freezeTypeInTable.equalsIgnoreCase("DEBIT_FREEZE") ||freezeTypeInTable.equalsIgnoreCase("TOTAL_FREEZE")) &&
                (TYPE.equalsIgnoreCase("CREDIT_FREEZE") || TYPE.equalsIgnoreCase("DEBIT_FREEZE") ||TYPE.equalsIgnoreCase("TOTAL_FREEZE"))){
                    ClientUtil.showMessageWindow("SAME option Selected");
                    return;
                }
            }
        }
        
        //        StringBuffer str = new StringBuffer();
        
        //To display error message if all the mandatory fields are not filled in, else proceed with normal flow
        //        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panFreezeDetails);
        //        if(mandatoryMessage.length() > 0){
        //            str.append(mandatoryMessage + "\n");
        //        }
        
        //        final String freezeType = CommonUtil.convertObjToStr(((ComboBoxModel)(cboType.getModel())).getKeyForSelected());
        //        if(!freezeType.equalsIgnoreCase("COMPLETE")
        //            && (CommonUtil.convertObjToDouble(lblClearBalanceDisplay.getText()).doubleValue() >
        //            CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue())){
        //            str.append(resourceBundle.getString("AMTWARNING"));
        //        }
        
        //        System.out.println("str: " + str);
        if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && str.length() > 0 ){
            displayAlert(str.toString());
        } else {
            result = observable.addFreeze(updateTab);
            if (result == 2){
                ClientUtil.enableDisable(panFreezeDetailsInner, true);
            } else {
                observable.resetFreezeDetails();
                ClientUtil.enableDisable(panFreezeDetailsInner,false);
                setFreezeButtonEnableDisableDefault();
            }
            ArrayList lst = new ArrayList();
            lst.add("FREEZE_NO");
            HashMap lockMap = new HashMap();
            lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
            if(observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT) {
                lockMap.put("FREEZE_NO", observable.getLblFreezeId());
                
                setEditLockMap(lockMap);
                setEditLock();
            }
            updateTab = -1;
            observable.ttNotifyObservers();
        }
    }//GEN-LAST:event_btnFreezeSaveActionPerformed
            private void btnFreezeNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFreezeNewActionPerformed
                // Add your handling code here:
                
                String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panProductInnerMost);
                System.out.println("mandatoryMessage: " + mandatoryMessage);
                updateTab = -1;
                if(mandatoryMessage.length() > 0){
                    displayAlert(mandatoryMessage);
                    
                }else{
                    updateUIFields();
                    boolean compFreeze = observable.isNewAllowed();
                    if(!compFreeze){
                        observable.resetFreezeDetails();
                        ClientUtil.enableDisable(this.panFreezeDetailsInner, true);
                        // observable.resetPhoneDetailsNotify();
                        setFreezeButtonEnableDisableNew();
                        observable.ttNotifyObservers();
                    }else{
                        displayAlert(resourceBundle.getString("completeWarning"));
                    }
                }
                tdtDate.setDateValue(DateUtil.getStringDate((Date) currDt.clone()));
                tdtDate.setEnabled(false);
                observable.setLblFreezeStatus(CommonConstants.STATUS_CREATED);
    }//GEN-LAST:event_btnFreezeNewActionPerformed
            
    private void btnAccountNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccountNumberActionPerformed
        // Add your handling code here:
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
        ClientUtil.clearAll(this.panFreezeDetailsInner);
        tdtDate.setDateValue(DateUtil.getStringDate((Date) currDt.clone()));
        tdtDate.setEnabled(false);
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
        map.put("RECORD_KEY", paramMap.get("FREEZE_NO"));
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
        cboType.setModel(observable.getCbmType());
    }
    
    /** To display a popUp window for viewing existing data as well as to select new
     * customer for entry
     */
    private void popUp(){
        HashMap testMap = null;
        //To display customer info based on the selected ProductID
        if( observable.getActionType() == ClientConstants.ACTIONTYPE_NEW ){
            testMap = accountViewMap();
            new ViewAll(this, testMap, true).show();
            
        }else if(viewType != -1){
            testMap = accountViewMap();
            new ViewAll(this, testMap, true).show();
        }
        //To display the existing accounts which are set to freezed
        else if( observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ){
            testMap = freezeEditMap();
            new ViewAll(this, testMap).show();
            
        }else if(observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE){
            testMap = freezeDeletetMap();
            new ViewAll(this, testMap).show();
        }
        else if( observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW ){
            testMap = freezeEditMap();
            new ViewAll(this, testMap).show();
            
        }
        //        new ViewAll(this, testMap).show();
    }
    
    /** Called by the Popup window created thru popUp method */
    public void fillData(Object obj) {
        try{
            HashMap hash = (HashMap) obj;
            //To fillData for a new entry
            if( observable.getActionType() == ClientConstants.ACTIONTYPE_NEW ){
                fillDataNew(hash);
                if(advances.equals("ADVANCES")){
                    double clearBalance=CommonUtil.convertObjToDouble(lblClearBalanceDisplay.getText()).doubleValue();
                }
                
            }
            else if(viewType != -1){
                fillDataNew(hash);
                viewType = -1;
            }
            else if( observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW){
                //fillDataNew(hash);
                fillDataEdit(hash);
                setButtonEnableDisable();
                btnAccountNumber.setEnabled(false);
                observable.setStatus();
                checkForEdit();
            } else if( observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION){
                fillDataEdit(hash);
                setButton4Authorize();
                btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
            }
            
        }catch(Exception e){
            log.error(e);
        }
        
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }
    
    private void checkForEdit(){
        // To enable disable controls for EDIT operation
        if( observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ){
            enableDisable(true);
            cboProductID.setEnabled(false);
            txtAccountNumber.setEnabled(false);
            setFreezeButtonEnableDisableDefault();
            ClientUtil.enableDisable(this.panFreezeDetailsInner, false);
        }
    }
    
    /** To fillData for a new entry */
    private void fillDataNew(HashMap hash){
        txtAccountNumber.setText((String)hash.get("ACCOUNT NUMBER"));
        
        //__ Reset the Table with the change of Account No...
        //        observable.resetForm();
        observable.resetAccountDetails();
        observable.resetFreezeListTable();
        observable.resetObjects();
        showCustomerName();
         txtAccountNumber.setText((String)hash.get("ACCOUNT NUMBER"));
         if(observable.isTodProd()){ // Added by nithya
             lblOD.setVisible(true);
         }
    }
    
    /** To fillData for existing entry */
    private void fillDataEdit(HashMap hash){
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION) {
            hash.put("ACCTNUMBER", hash.get("ACT_NUM"));
        } else {
            hash.put("ACCTNUMBER", hash.get("ACCOUNT NUMBER"));
        }
        System.out.println("hash: " + hash);
         if(advances.equals("ADVANCES")){
             hash.put("ADVANCES","ADVANCES");
             hash.put("BRANCH_ID",getSelectedBranchID());
         }
        observable.getProductDetails(hash);
        observable.getData(hash);
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION) {
            final String FREEZEID = (String)hash.get("FREEZE_ID");
            FREEZESTATUS = (String)hash.get("FREEZE_STATUS");
            try{
                observable.setAuthRowData(FREEZEID);
            }catch(Exception e){
                System.out.println("Error in observable.setAuthRow(FREEZEID);");
            }
        }
    }
    
    /** To get popUp data for a new entry */
    private HashMap accountViewMap(){
        final HashMap testMap = new HashMap();
        if(advances.equals("ADVANCES"))
            testMap.put(CommonConstants.MAP_NAME, "Freeze.getAccountDataOD");
        else
            testMap.put(CommonConstants.MAP_NAME, "Freeze.getAccountData");
            HashMap todCheckmap = new HashMap();
            todCheckmap.put("PROD_ID",(String)(observable.getCbmProductID()).getKeyForSelected());
            List todList = ClientUtil.executeQuery("isTODSetForProduct", todCheckmap);
            if (todList != null && todList.size() > 0) {
                 HashMap todMap = (HashMap) todList.get(0);
                if (todMap.containsKey("TEMP_OD_ALLOWED")) {
                    if (CommonUtil.convertObjToStr(todMap.get("TEMP_OD_ALLOWED")).equalsIgnoreCase("Y")) {
                        testMap.put(CommonConstants.MAP_NAME, "Freeze.getAccountDataForSBOD");
                    }
                }
            }    
        final HashMap whereMap = new HashMap();
        System.out.println("Prod Id :"+(String)(observable.getCbmProductID()).getKeyForSelected());
        
        //__ Only the Authorized, Operational/New And not already Freezed records Should be Available...
        whereMap.put("PRODID", (String)(observable.getCbmProductID()).getKeyForSelected());
        whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
        testMap.put(CommonConstants.MAP_WHERE, whereMap);
        
        return testMap;
    }
    
    /** To get popUp data for already existing entries for modification */
    private HashMap freezeEditMap(){
        final HashMap testMap = new HashMap();
        //__ All but Deleted and Authorized Records Should be Available...
        
        if(advances .equals("ADVANCES"))
            testMap.put(CommonConstants.MAP_NAME, "getSelectFreezeAccountListOD");
        else
            testMap.put(CommonConstants.MAP_NAME, "getSelectFreezeAccountList");
        final HashMap whereMap = new HashMap();
        whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
        testMap.put(CommonConstants.MAP_WHERE, whereMap);
        
        return testMap;
    }
    
    /** To get popUp data for already existing entries for modification */
    private HashMap freezeDeletetMap(){
        final HashMap testMap = new HashMap();
        //__ All but Deleteda,and Authorized Records should be aba
        if(advances .equals("ADVANCES"))
            testMap.put(CommonConstants.MAP_NAME, "getSelectFreezeDeleteListOD");
        else
            testMap.put(CommonConstants.MAP_NAME, "getSelectFreezeDeleteList");
        
        final HashMap whereMap = new HashMap();
        whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
        testMap.put(CommonConstants.MAP_WHERE, whereMap);
        
        return testMap;
    }
    
    /** To display customer related details based on account number */
    private void showCustomerName(){
        updateUIFields();
        observable.getCustomerDetails();
    }
    
    public void update(java.util.Observable o, Object arg) {
        txtAmount.setText(observable.getTxtAmount());
        tdtDate.setDateValue(observable.getTdtFreezeDate());
        txtRemarks.setText(observable.getTxtRemarks());
        txtAccountNumber.setText(observable.getTxtAccountNumber());
        
        cboType.setSelectedItem(observable.getCboType());
//        cboProductID.setSelectedItem(observable.getCboProductID());
        
        tblFreezeList.setModel(observable.getTblFreezeListModel());
        lblAccountHeadCode.setText(observable.getAccountHeadCode());
        lblAccountHeadDesc.setText(observable.getAccountHeadDesc());
        lblCustomerNameDisplay.setText(observable.getCustomerName());
        lblCustomerNameDisplay.setToolTipText(lblCustomerNameDisplay.getText());
        lblClearBalanceDisplay.setText(observable.getClearBalance());
        lblExistingFreezesSumDisplay.setText(observable.getFreezeSum());
        lblExistingLienSumDisplay.setText(observable.getLienSum());
        lblStatus.setText(observable.getLblStatus());
         lblClearBalanceDisplay1.setText(observable.getClearBalance1());
        lblFreezeSlNoDesc.setText(observable.getLblFreezeId());
        if(observable.isTodProd()){ // Added by nithya
            lblOD.setText("Tod Amount exists Rs : " + observable.getTodAmountExist());
        }
    }
    
    public void updateUIFields() {
        observable.setTxtAmount(txtAmount.getText());
        observable.setTxtRemarks(txtRemarks.getText());
        observable.setTxtAccountNumber(txtAccountNumber.getText());
        observable.setTdtFreezeDate(tdtDate.getDateValue());
        observable.setTblFreezeListModel((com.see.truetransact.clientutil.EnhancedTableModel)tblFreezeList.getModel());
        
        observable.setCboProductID((String) cboProductID.getSelectedItem());
        observable.setCboType((String) cboType.getSelectedItem());
    }
    
    private void setFieldNames() {
        btnAccountNumber.setName("btnAccountNumber");
        btnFreezeDelete.setName("btnFreezeDelete");
        btnFreezeNew.setName("btnFreezeNew");
        btnFreezeSave.setName("btnFreezeSave");
        btnUnFreeze.setName("btnUnFreeze");
        cboProductID.setName("cboProductID");
        cboType.setName("cboType");
        lblAccountHead.setName("lblAccountHead");
        lblAccountHeadCode.setName("lblAccountHeadCode");
        lblAccountHeadDesc.setName("lblAccountHeadDesc");
        lblAccountNumber.setName("lblAccountNumber");
        lblClearBalance.setName("lblClearBalance");
        lblClearBalanceDisplay.setName("lblClearBalanceDisplay");
        //lblCreditDebit.setName("lblCreditDebit");
        //lblCustomerName.setName("lblCustomerName");
        lblCustomerNameDisplay.setName("lblCustomerNameDisplay");
        lblExistingFreezesSum.setName("lblExistingFreezesSum");
        lblExistingFreezesSumDisplay.setName("lblExistingFreezesSumDisplay");
        lblExistingLienSum.setName("lblExistingLienSum");
        lblExistingLienSumDisplay.setName("lblExistingLienSumDisplay");
        lblClearBalance1.setName("lblClearBalance1");
        lblClearBalanceDisplay1.setName("lblClearBalanceDisplay1");
        
        lblDate.setName("lblDate");
        tdtDate.setName("tdtDate");
        lblFreezeSlNo.setName("lblFreezeSlNo");
        lblFreezeSlNoDesc.setName("lblFreezeSlNoDesc");
        
        lblProductID.setName("lblProductID");
        lblRemarks.setName("lblRemarks");
        lblType.setName("lblType");
        mbrMain.setName("mbrMain");
        panAccountHead.setName("panAccountHead");
        lblAmount.setName("lblAmount");
        panClearBalance.setName("panClearBalance");
        // panCreditDebit.setName("panCreditDebit");
        panFreezeDetails.setName("panFreezeDetails");
        panFreezeList.setName("panFreezeList");
        panFreezeSave.setName("panFreezeSave");
        panProduct.setName("panProduct");
        panProductIDInner.setName("panProductIDInner");
        panProductInnerMost.setName("panProductInnerMost");
        panTable.setName("panTable");
        sptDetails.setName("sptDetails");
        sptProduct.setName("sptProduct");
        srpFreezeList.setName("srpFreezeList");
        tblFreezeList.setName("tblFreezeList");
        //tdtFreezeDate.setName("tdtFreezeDate");
        txtAccountNumber.setName("txtAccountNumber");
        txtAmount.setName("txtAmount");
        txtRemarks.setName("txtRemarks");
    }
    
    private void internationalize() {
        //        FreezeRB resourceBundle = new FreezeRB();
        lblClearBalanceDisplay.setText(resourceBundle.getString("lblClearBalanceDisplay"));
        lblClearBalanceDisplay1.setText(resourceBundle.getString("lblClearBalanceDisplay1"));
        btnFreezeNew.setText(resourceBundle.getString("btnFreezeNew"));
        lblExistingFreezesSumDisplay.setText(resourceBundle.getString("lblExistingFreezesSumDisplay"));
        lblExistingLienSumDisplay.setText(resourceBundle.getString("lblExistingLienSumDisplay"));
        //lblCustomerName.setText(resourceBundle.getString("lblCustomerName"));
        lblAccountNumber.setText(resourceBundle.getString("lblAccountNumber"));
        btnFreezeSave.setText(resourceBundle.getString("btnFreezeSave"));
        //rdoCreditDebit_Debit.setText(resourceBundle.getString("rdoCreditDebit_Debit"));
        lblAccountHeadDesc.setText(resourceBundle.getString("lblAccountHeadDesc"));
        lblAccountHeadCode.setText(resourceBundle.getString("lblAccountHeadCode"));
        lblAccountHead.setText(resourceBundle.getString("lblAccountHead"));
        lblType.setText(resourceBundle.getString("lblType"));
        lblClearBalance.setText(resourceBundle.getString("lblClearBalance"));
        lblClearBalance1.setText(resourceBundle.getString("lblClearBalance1"));
        lblExistingFreezesSum.setText(resourceBundle.getString("lblExistingFreezesSum"));
        lblExistingLienSum.setText(resourceBundle.getString("lblExistingLienSum"));
        btnAccountNumber.setText(resourceBundle.getString("btnAccountNumber"));
        lblAmount.setText(resourceBundle.getString("lblAmount"));
        lblProductID.setText(resourceBundle.getString("lblProductID"));
        btnFreezeDelete.setText(resourceBundle.getString("btnFreezeDelete"));
        btnUnFreeze.setText(resourceBundle.getString("btnUnFreeze"));
        lblDate.setText(resourceBundle.getString("lblDate"));
        lblFreezeSlNo.setText(resourceBundle.getString("lblFreezeSlNo"));
        lblFreezeSlNoDesc.setText(resourceBundle.getString("lblFreezeSlNoDesc"));
        lblRemarks.setText(resourceBundle.getString("lblRemarks"));
        lblCustomerNameDisplay.setText(resourceBundle.getString("lblCustomerNameDisplay"));
    }
    
    public void setHelpMessage() {
        FreezeMRB objMandatoryRB = new FreezeMRB();
        txtAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAmount"));
        tdtDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtDate"));
        cboType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboType"));
        txtRemarks.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRemarks"));
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
    
    private void setFreezeButtonEnableDisable(){
        this.btnFreezeNew.setEnabled(false);
        this.btnFreezeSave.setEnabled(false);
        this.btnFreezeDelete.setEnabled(false);
        this.btnUnFreeze.setEnabled(false);
    }
    
    private void setFreezeButtonEnableDisableDefault(){
        this.btnFreezeNew.setEnabled(true);
        this.btnFreezeSave.setEnabled(false);
        this.btnFreezeDelete.setEnabled(false);
    }
    
    private void setFreezeButtonEnableDisableNew(){
        this.btnFreezeNew.setEnabled(false);
        this.btnFreezeSave.setEnabled(true);
        this.btnFreezeDelete.setEnabled(false);
        this.btnUnFreeze.setEnabled(false);
    }
    
    private void setFreezeButtonEnableDisableSelect(){
        this.btnFreezeNew.setEnabled(false);
        this.btnFreezeSave.setEnabled(true);
        this.btnFreezeDelete.setEnabled(true);
        this.btnUnFreeze.setEnabled(false);
    }
    
    private void formButtonsEnableDisable(){
        enableDisable(false);
        setButtonEnableDisable();
        setFreezeButtonEnableDisable();
    }
    
    //To enable disable fields after a delete or UnFreeze operation
    private void onDeleteUnFreeze(){
        ClientUtil.enableDisable(this.panFreezeDetailsInner, false);
        setFreezeButtonEnableDisableDefault();
    }
    public void clearBalances(){
        lblClearBalanceDisplay.setText("");
        lblClearBalanceDisplay1.setText("");
        lblExistingFreezesSumDisplay.setText("");
        lblExistingLienSumDisplay.setText("");
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
    private com.see.truetransact.uicomponent.CButton btnFreezeDelete;
    private com.see.truetransact.uicomponent.CButton btnFreezeNew;
    private com.see.truetransact.uicomponent.CButton btnFreezeSave;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnUnFreeze;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboProductID;
    private com.see.truetransact.uicomponent.CComboBox cboType;
    private com.see.truetransact.uicomponent.CLabel lblAccountHead;
    private com.see.truetransact.uicomponent.CLabel lblAccountHeadCode;
    private com.see.truetransact.uicomponent.CLabel lblAccountHeadDesc;
    private com.see.truetransact.uicomponent.CLabel lblAccountNumber;
    private com.see.truetransact.uicomponent.CLabel lblAmount;
    private com.see.truetransact.uicomponent.CLabel lblClearBalance;
    private com.see.truetransact.uicomponent.CLabel lblClearBalance1;
    private com.see.truetransact.uicomponent.CLabel lblClearBalanceDisplay;
    private com.see.truetransact.uicomponent.CLabel lblClearBalanceDisplay1;
    private com.see.truetransact.uicomponent.CLabel lblCustomerNameDisplay;
    private com.see.truetransact.uicomponent.CLabel lblDate;
    private com.see.truetransact.uicomponent.CLabel lblExistingFreezesSum;
    private com.see.truetransact.uicomponent.CLabel lblExistingFreezesSumDisplay;
    private com.see.truetransact.uicomponent.CLabel lblExistingLienSum;
    private com.see.truetransact.uicomponent.CLabel lblExistingLienSumDisplay;
    private com.see.truetransact.uicomponent.CLabel lblFreezeSlNo;
    private com.see.truetransact.uicomponent.CLabel lblFreezeSlNoDesc;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblOD;
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
    private com.see.truetransact.uicomponent.CLabel lblType;
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
    private com.see.truetransact.uicomponent.CPanel panAccountHead1;
    private com.see.truetransact.uicomponent.CPanel panClearBalance;
    private com.see.truetransact.uicomponent.CPanel panFreeze;
    private com.see.truetransact.uicomponent.CPanel panFreezeDetails;
    private com.see.truetransact.uicomponent.CPanel panFreezeDetailsInner;
    private com.see.truetransact.uicomponent.CPanel panFreezeList;
    private com.see.truetransact.uicomponent.CPanel panFreezeSave;
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
    private com.see.truetransact.uicomponent.CScrollPane srpFreezeList;
    private com.see.truetransact.uicomponent.CTable tblFreezeList;
    private javax.swing.JToolBar tbrOperativeAcctProduct;
    private com.see.truetransact.uicomponent.CDateField tdtDate;
    private com.see.truetransact.uicomponent.CTextField txtAccountNumber;
    private com.see.truetransact.uicomponent.CTextField txtAmount;
    private com.see.truetransact.uicomponent.CTextField txtRemarks;
    // End of variables declaration//GEN-END:variables
    
    /* Set a cellrenderer to this table in order highlight the deleted rows */
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
        FreezeUI fui = new FreezeUI();
        javax.swing.JFrame j = new javax.swing.JFrame();
        j.getContentPane().add(fui);
        j.show();
        fui.show();
    }
}
