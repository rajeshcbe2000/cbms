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

package com.see.truetransact.ui.product.loan.loaneligibilitymaintenance;

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
public class LoanEligibilityMaintenanceUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer, UIMandatoryField {
    private HashMap mandatoryMap;
    private LoanEligibilityMaintenanceOB observable;
    int updateTab = -1, viewType = -1;
    String FREEZESTATUS = "";
    boolean flag = false;
    private final String TYPE = "COMPLETE";
    int rowSelected = -1;
    private String advances="";
    private Date currDt = null;
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.product.loan.loaneligibilitymaintenance.LoanEligibilityMaintenanceRB", ProxyParameters.LANGUAGE);
    
    private final static Logger log = Logger.getLogger(LoanEligibilityMaintenanceUI.class);
    /** Creates new form FreezeUI */
    public LoanEligibilityMaintenanceUI() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        initStartUp();
    }
    public LoanEligibilityMaintenanceUI(String advance) {
        this.advances=advance;
        initComponents();
        initStartUp();
    }
    private void initStartUp(){
        
        setMandatoryHashMap();
        setFieldNames();
        internationalize();
//        if(advances.equals("ADVANCES"))
//            observable = new LoanEligibilityMaintenanceOB(advances);
//        else
            observable = new LoanEligibilityMaintenanceOB();
        observable.setAdvances(advances);
        observable.addObserver(this);
        update(observable, null);
        initComponentData();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panDateEntyDetails);
//        tblEligibilityList.setDefaultRenderer(Object.class, renderer);
        formButtonsEnableDisable();
        setHelpMessage();
        
        setMaxLength();
        btnDelete.setEnabled(false);
        tdtToDate.setEnabled(false);
    }
    
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboCropType", new Boolean(true));
        mandatoryMap.put("txtEligibileAmount", new Boolean(true));
        mandatoryMap.put("tdtFromDate", new Boolean(true));
      
    }
    
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    private void setMaxLength(){
        txtEligibileAmount.setMaxLength(16);
        txtEligibileAmount.setValidation(new CurrencyValidation(14,2));
     
    }
    
    public void authorizeStatus(String actionPerformed) {
        if((!btnNew.isEnabled()) && (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION)) {
            System.out.println("btnNew is Disable...");
            HashMap singleAuthorizeMap = new HashMap();
            ArrayList arrList = new ArrayList();
            HashMap authDataMap = new HashMap();
//            LoanEligibilityTO obj =new LoanEligibilityTO();
//            int row = observable.getAuthRow();
//            if(observable.getCropList()!=null && observable.getCropList().size()>0){
//                for(int i=0;i<observable.getCropList().size();i++){
//                    obj=(LoanEligibilityTO)observable.getCropList().get(i);
//                }
//            }
//            authDataMap.put("ACT_NUM", txtAccountNumber.getText());
//            authDataMap.put("FREEZE_ID", (String)tblEligibilityList.getValueAt(row,1));
//            authDataMap.put("FREEZE_AMT", txtAmount.getText());
//            authDataMap.put("FREEZE_STATUS", FREEZESTATUS);
//            authDataMap.put(CommonConstants.AUTHORIZEDT, currDt.clone());
//            authDataMap.put("FREEZE_TYPE", (String)(((ComboBoxModel)(cboType).getModel())).getKeyForSelected());
            arrList.add(authDataMap);
            
            singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, actionPerformed);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDATA, observable.getCropList());
            super.setOpenForEditBy(observable.getStatusBy());
//            super.removeEditLock((String)tblEligibilityList.getValueAt(row,1));
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
//            if(advances.equals("ADVANCES"))
//                mapParam.put(CommonConstants.MAP_NAME, "getFreezeAccountAuthorizeTOListOD");
//            else
                mapParam.put(CommonConstants.MAP_NAME, "getCropAuthorizeTOList");
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
        panEligibilityDetails = new com.see.truetransact.uicomponent.CPanel();
        panFreezeSave = new com.see.truetransact.uicomponent.CPanel();
        btnCropSave = new com.see.truetransact.uicomponent.CButton();
        btnCropNew = new com.see.truetransact.uicomponent.CButton();
        btnCropDelete = new com.see.truetransact.uicomponent.CButton();
        panDateEntyDetails = new com.see.truetransact.uicomponent.CPanel();
        lblEligibileAmount = new com.see.truetransact.uicomponent.CLabel();
        txtEligibileAmount = new com.see.truetransact.uicomponent.CTextField();
        lblCropType = new com.see.truetransact.uicomponent.CLabel();
        cboCropType = new com.see.truetransact.uicomponent.CComboBox();
        lblToDate = new com.see.truetransact.uicomponent.CLabel();
        lblFromDate = new com.see.truetransact.uicomponent.CLabel();
        tdtToDate = new com.see.truetransact.uicomponent.CDateField();
        tdtFromDate = new com.see.truetransact.uicomponent.CDateField();
        panTableList = new com.see.truetransact.uicomponent.CPanel();
        srpFreezeList = new com.see.truetransact.uicomponent.CScrollPane();
        tblEligibilityList = new com.see.truetransact.uicomponent.CTable();
        sptDetails = new com.see.truetransact.uicomponent.CSeparator();
        tbrOperativeAcctProduct = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace65 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace66 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace67 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace68 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace69 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace70 = new com.see.truetransact.uicomponent.CLabel();
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

        panEligibilityDetails.setMinimumSize(new java.awt.Dimension(270, 204));
        panEligibilityDetails.setPreferredSize(new java.awt.Dimension(270, 204));
        panEligibilityDetails.setLayout(new java.awt.GridBagLayout());

        panFreezeSave.setLayout(new java.awt.GridBagLayout());

        btnCropSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnCropSave.setToolTipText("Save");
        btnCropSave.setMaximumSize(new java.awt.Dimension(29, 27));
        btnCropSave.setMinimumSize(new java.awt.Dimension(29, 27));
        btnCropSave.setName("btnContactNoAdd");
        btnCropSave.setPreferredSize(new java.awt.Dimension(29, 27));
        btnCropSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCropSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFreezeSave.add(btnCropSave, gridBagConstraints);

        btnCropNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnCropNew.setToolTipText("New");
        btnCropNew.setMaximumSize(new java.awt.Dimension(29, 27));
        btnCropNew.setMinimumSize(new java.awt.Dimension(29, 27));
        btnCropNew.setPreferredSize(new java.awt.Dimension(29, 27));
        btnCropNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCropNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFreezeSave.add(btnCropNew, gridBagConstraints);

        btnCropDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnCropDelete.setToolTipText("Delete");
        btnCropDelete.setMaximumSize(new java.awt.Dimension(29, 27));
        btnCropDelete.setMinimumSize(new java.awt.Dimension(29, 27));
        btnCropDelete.setPreferredSize(new java.awt.Dimension(29, 27));
        btnCropDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCropDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFreezeSave.add(btnCropDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panEligibilityDetails.add(panFreezeSave, gridBagConstraints);

        panDateEntyDetails.setMinimumSize(new java.awt.Dimension(300, 170));
        panDateEntyDetails.setPreferredSize(new java.awt.Dimension(300, 170));
        panDateEntyDetails.setLayout(new java.awt.GridBagLayout());

        lblEligibileAmount.setText("Loan eligible per Acre ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDateEntyDetails.add(lblEligibileAmount, gridBagConstraints);

        txtEligibileAmount.setMaxLength(16);
        txtEligibileAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtEligibileAmount.setValidation(new CurrencyValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDateEntyDetails.add(txtEligibileAmount, gridBagConstraints);

        lblCropType.setText("Crop Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDateEntyDetails.add(lblCropType, gridBagConstraints);

        cboCropType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCropType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCropTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDateEntyDetails.add(cboCropType, gridBagConstraints);

        lblToDate.setText("To Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDateEntyDetails.add(lblToDate, gridBagConstraints);

        lblFromDate.setText("From Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDateEntyDetails.add(lblFromDate, gridBagConstraints);

        tdtToDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtToDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDateEntyDetails.add(tdtToDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDateEntyDetails.add(tdtFromDate, gridBagConstraints);

        panEligibilityDetails.add(panDateEntyDetails, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(0, 40, 0, 4);
        panTable.add(panEligibilityDetails, gridBagConstraints);

        panTableList.setMinimumSize(new java.awt.Dimension(354, 125));
        panTableList.setPreferredSize(new java.awt.Dimension(354, 125));
        panTableList.setLayout(new java.awt.GridBagLayout());

        srpFreezeList.setPreferredSize(new java.awt.Dimension(454, 125));

        tblEligibilityList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tblEligibilityList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblEligibilityListMousePressed(evt);
            }
        });
        srpFreezeList.setViewportView(tblEligibilityList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panTableList.add(srpFreezeList, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.8;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        panTable.add(panTableList, gridBagConstraints);

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

        lblSpace65.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace65.setText("     ");
        lblSpace65.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace65.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace65.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace65);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnEdit);

        lblSpace66.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace66.setText("     ");
        lblSpace66.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace66.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace66.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace66);

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

        lblSpace67.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace67.setText("     ");
        lblSpace67.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace67.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace67.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace67);

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

        lblSpace68.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace68.setText("     ");
        lblSpace68.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace68.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace68.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace68);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnException);

        lblSpace69.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace69.setText("     ");
        lblSpace69.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace69.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace69.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace69);

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

        lblSpace70.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace70.setText("     ");
        lblSpace70.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace70.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace70.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace70);

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
    
    private void tdtToDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtToDateFocusLost
        // TODO add your handling code here:
        //__ if Some date is Selected...
//        if(tdtDate.getDateValue() != null && tdtDate.getDateValue().length() > 0){
//            HashMap dataMap = new HashMap();
////            dataMap.put("ACT_NUM", CommonUtil.convertObjToStr(txtAccountNumber.getText()));
//            dataMap.put("F_DATE", DateUtil.getDateMMDDYYYY(tdtDate.getDateValue()));
//            boolean val = observable.verifyAccountDate(dataMap);
//            
//            //__ if selected date is before account opening date...
//            if(!val){
//                //__ Reset the date...
//                tdtDate.setDateValue("");
//                displayAlert(resourceBundle.getString("FREEZEDATEWARNING"));
//            }
//        }
    }//GEN-LAST:event_tdtToDateFocusLost
    
    private void cboCropTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCropTypeActionPerformed
//        double clearBalance = CommonUtil.convertObjToDouble(lblClearBalanceDisplay.getText()).doubleValue();
//        double clearBalance1 = CommonUtil.convertObjToDouble(lblClearBalanceDisplay1.getText()).doubleValue();
//        double freezeExist = CommonUtil.convertObjToDouble(lblExistingFreezesSumDisplay.getText()).doubleValue();
//        double LienExist = CommonUtil.convertObjToDouble(lblExistingLienSumDisplay.getText()).doubleValue();
//        
//        if(observable.getActionType()!=ClientConstants.ACTIONTYPE_AUTHORIZE &&
//        observable.getActionType()!=ClientConstants.ACTIONTYPE_EXCEPTION &&
//        observable.getActionType()!=ClientConstants.ACTIONTYPE_REJECT ){
//            String TYPE =  CommonUtil.convertObjToStr(((ComboBoxModel)(cboType.getModel())).getKeyForSelected());
//            if(TYPE !=null && TYPE .length()>0){
//                if(TYPE.compareToIgnoreCase(this.TYPE)==0 && observable.getLblFreezeStatus()==CommonConstants.STATUS_CREATED){
//                    double bal=CommonUtil.convertObjToDouble(lblClearBalanceDisplay.getText()).doubleValue();
//                     if(clearBalance1<clearBalance){
//                    bal=CommonUtil.convertObjToDouble(lblClearBalanceDisplay1.getText()).doubleValue();
//                    if(freezeExist>0.0||LienExist>0.0)
//                        bal=bal-freezeExist-LienExist;
//                     }
//                    txtAmount.setText(String.valueOf(bal));
//                    txtAmount.setEnabled(false);
//                }else if(observable.getActionType()!=ClientConstants.ACTIONTYPE_DELETE){
//                    if(observable.getLblFreezeStatus()==CommonConstants.STATUS_CREATED){
//                        txtAmount.setText("");
//                    }
//                    if(TYPE.equalsIgnoreCase("CREDIT_FREEZE") || TYPE.equalsIgnoreCase("DEBIT_FREEZE") || TYPE.equalsIgnoreCase("TOTAL_FREEZE"))
//                        txtAmount.setEnabled(false);
//                    else
//                    txtAmount.setEnabled(true);
//                }
//            }
//        }
    }//GEN-LAST:event_cboCropTypeActionPerformed
    
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
//        for(int i=0; i<tblEligibilityList.getRowCount();i++) {
//            String data= CommonUtil.convertObjToStr(tblEligibilityList.getValueAt(i,1));
//            if(observable.getActionType() ==ClientConstants.ACTIONTYPE_EDIT)
//                setMode(ClientConstants.ACTIONTYPE_EDIT);
//            String status=CommonUtil.convertObjToStr((tblEligibilityList.getValueAt(i,4)));
//            if(status.length()>0)
//                super.removeEditLock(data);
//        }
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
        updateOBFields();
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panDateEntyDetails);
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
        ClientUtil.enableDisable(this.panDateEntyDetails, false);
        setFreezeButtonEnableDisableDefault(true);
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
    
    private void btnCropDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCropDeleteActionPerformed
        // Add your handling code here:
//        updateUIFields();
        observable.deleteFreezeTab(tblEligibilityList.getSelectedRow());
//        observable.resetFreezeDetails();
//        ClientUtil.enableDisable(this.panEligibilityDetails, false);
        setFreezeButtonEnableDisableDefault(true);
          tdtToDate.setEnabled(false);
        observable.ttNotifyObservers();
    }//GEN-LAST:event_btnCropDeleteActionPerformed
        
    private void tblEligibilityListMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblEligibilityListMousePressed
        // Add your handling code here:
        String authorizeStatus = "";
        rowSelected = tblEligibilityList.getSelectedRow();
        flag = true;
        //        updateUIFields();
        observable.populateFreezeDetails(tblEligibilityList.getSelectedRow());
        if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE){
            //              boolean UNFREEZED = observable.getLblFreezeStatus().equalsIgnoreCase("UNFREEZED");
            //           if(!UNFREEZED){
            // if(observable.getLblAuth().equalsIgnoreCase("AUTHORIZED")){
            if( (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE) || ( observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT)){
                ClientUtil.enableDisable(this.panEligibilityDetails, false);
                this.btnCropNew.setEnabled(false);
                this.btnCropSave.setEnabled(false);
                this.btnCropDelete.setEnabled(false);
            }else {
                HashMap viewMap = new HashMap();
                
                authorizeStatus = observable.getLblAuth();
                if(authorizeStatus!=null && authorizeStatus.length()>0){
                    if((authorizeStatus.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED))||(authorizeStatus.equalsIgnoreCase(CommonConstants.STATUS_REJECTED))){
                        ClientUtil.enableDisable(this.panEligibilityDetails,false);
                        
                    }
                }
                else{
                    ClientUtil.enableDisable(this.panEligibilityDetails, true);
                    setFreezeButtonEnableDisableDefault(false);
                    this.btnCropNew.setEnabled(true);
                    //                    updateTab = 1;
                    updateTab = tblEligibilityList.getSelectedRow();
                }
            }
        }
        updateTab = tblEligibilityList.getSelectedRow();
        String status =    CommonUtil.convertObjToStr(tblEligibilityList.getValueAt(rowSelected, 4));
       
        
        //
        observable.ttNotifyObservers();
    }//GEN-LAST:event_tblEligibilityListMousePressed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // Add your handling code here:
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // Add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed
    
    private void btnCropSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCropSaveActionPerformed
        int result=0;
        updateOBFields();
       
//        double sum=0.0;
//        updateUIFields();
        StringBuffer str = new StringBuffer();
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panDateEntyDetails);
        if(mandatoryMessage.length() > 0){
            str.append(mandatoryMessage + "\n");
        }
//        double clearBalance = CommonUtil.convertObjToDouble(lblClearBalanceDisplay.getText()).doubleValue();
//        double clearBal = CommonUtil.convertObjToDouble(lblClearBalanceDisplay.getText()).doubleValue();
//        if(advances.equals("ADVANCES") && clearBalance<1){
//            ClientUtil.showMessageWindow("This Customer Not Having Credit_balance");
//            return;
//        }
//        double clearBalance1 = CommonUtil.convertObjToDouble(lblClearBalanceDisplay1.getText()).doubleValue();
//        if(clearBalance1<clearBalance)
//            clearBalance = CommonUtil.convertObjToDouble(lblClearBalanceDisplay1.getText()).doubleValue();
//        double FreezeAmt = CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue();
//        double freezeExist = CommonUtil.convertObjToDouble(lblExistingFreezesSumDisplay.getText()).doubleValue();
//        double LienExist = CommonUtil.convertObjToDouble(lblExistingLienSumDisplay.getText()).doubleValue();
//        double FreezeTabAmt = observable.FreezeTabSum();
//        if(clearBalance1<clearBal){
//           sum = (clearBalance-LienExist);
//        }
//        else {
//            sum = (clearBalance+freezeExist);
//        }
//        
//        final String TYPE = CommonUtil.convertObjToStr(((ComboBoxModel)(cboType.getModel())).getKeyForSelected());
//        if(TYPE.equalsIgnoreCase("PARTIAL")) {
//            if(clearBalance == FreezeAmt)
//                str.append(resourceBundle.getString("PARTWARNING"));
//        }
//        if(TYPE.equalsIgnoreCase("COMPLETE")) {
//            if(clearBalance != FreezeAmt)
//                str.append(resourceBundle.getString("COMPWARNING"));
//        }
//        //__ if a particular row is Updated...
//        if(updateTab > -1){
//            if(sum  < (FreezeAmt + FreezeTabAmt - CommonUtil.convertObjToDouble(tblEligibilityList.getValueAt(rowSelected, 2)).doubleValue())) {
//                str.append(resourceBundle.getString("AMTWARNING"));
//            }
//        }else{
//            if(sum < (FreezeAmt + FreezeTabAmt)){
//                str.append(resourceBundle.getString("AMTWARNING"));
//            }
//        }
//        
////        if(txtAmount.getText().equals("0.00")) {
////            ClientUtil.displayAlert(" Amount Cannot Be Zero");
////            btnCancelActionPerformed(null);
////            return;
////        }
//        if(tblEligibilityList.getRowCount()>0){
//            for(int i=0;i<tblEligibilityList.getRowCount();i++){
//                String freezeTypeInTable=CommonUtil.convertObjToStr(tblEligibilityList.getValueAt(i,0));
//                if((freezeTypeInTable.equalsIgnoreCase("PARTIAL") || freezeTypeInTable.equalsIgnoreCase("COMPLETE")) &&
//                (TYPE.equalsIgnoreCase("CREDIT_FREEZE") || TYPE.equalsIgnoreCase("DEBIT_FREEZE") ||TYPE.equalsIgnoreCase("TOTAL_FREEZE"))){
//                    ClientUtil.showMessageWindow("Can Select Only PARTIAL/COMPLETE option");
//                    return;
//                }
//                if((freezeTypeInTable.equalsIgnoreCase("CREDIT_FREEZE") || freezeTypeInTable.equalsIgnoreCase("DEBIT_FREEZE") ||freezeTypeInTable.equalsIgnoreCase("TOTAL_FREEZE")) &&
//                (TYPE.equalsIgnoreCase("PARTIAL") || TYPE.equalsIgnoreCase("DEBIT_FREEZE") ||TYPE.equalsIgnoreCase("COMPLETE"))){
//                    ClientUtil.showMessageWindow("PARTIAL/COMPLETE option Not Allowed");
//                    return;
//                }
//                if((freezeTypeInTable.equalsIgnoreCase("CREDIT_FREEZE") || freezeTypeInTable.equalsIgnoreCase("DEBIT_FREEZE") ||freezeTypeInTable.equalsIgnoreCase("TOTAL_FREEZE")) &&
//                (TYPE.equalsIgnoreCase("CREDIT_FREEZE") || TYPE.equalsIgnoreCase("DEBIT_FREEZE") ||TYPE.equalsIgnoreCase("TOTAL_FREEZE"))){
//                    ClientUtil.showMessageWindow("SAME option Selected");
//                    return;
//                }
//            }
//        }
//        
//        //        StringBuffer str = new StringBuffer();
//        
//        //To display error message if all the mandatory fields are not filled in, else proceed with normal flow
//        //        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panFreezeDetails);
//        //        if(mandatoryMessage.length() > 0){
//        //            str.append(mandatoryMessage + "\n");
//        //        }
//        
//        //        final String freezeType = CommonUtil.convertObjToStr(((ComboBoxModel)(cboType.getModel())).getKeyForSelected());
//        //        if(!freezeType.equalsIgnoreCase("COMPLETE")
//        //            && (CommonUtil.convertObjToDouble(lblClearBalanceDisplay.getText()).doubleValue() >
//        //            CommonUtil.convertObjToDouble(txtAmount.getText()).doubleValue())){
//        //            str.append(resourceBundle.getString("AMTWARNING"));
//        //        }
//        
//        //        System.out.println("str: " + str);
        if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && str.length() > 0 ){
            displayAlert(str.toString());
        } else {
          
            result = observable.addFreeze(updateTab);
            if (result == 2){
                ClientUtil.enableDisable(panEligibilityDetails, true);
            } else {
                observable.resetFreezeDetails();
                ClientUtil.enableDisable(panEligibilityDetails,false);
                setFreezeButtonEnableDisableDefault(true);
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
              tdtToDate.setEnabled(false);
              observable.resetData();
            observable.ttNotifyObservers();
        }
    }//GEN-LAST:event_btnCropSaveActionPerformed
    private void btnCropNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCropNewActionPerformed
                // Add your handling code here:
                
                String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panDateEntyDetails);
                System.out.println("mandatoryMessage: " + mandatoryMessage);
                if(mandatoryMessage.length() > 0){
                    displayAlert(mandatoryMessage);
                    
                }else{
                     ClientUtil.enableDisable(this.panDateEntyDetails, true);
                    if(observable.rowCount()>0){
                       String cropType= CommonUtil.convertObjToStr(tblEligibilityList.getValueAt(0,0));
                       observable.resetFreezeDetails();
                       observable.getCbmCropType().setKeyForSelected(cropType);
                       
                       cboCropType.setEnabled(false);
                    }else{
                       
                         cboCropType.setEnabled(true);
                    }
//                    boolean compFreeze = observable.isNewAllowed();
                     tdtToDate.setEnabled(false);
                    setFreezeButtonEnableDisableDefault(false);
//                    if(!compFreeze){
                        
                        
                        setFreezeButtonEnableDisableNew();
                        observable.ttNotifyObservers();
                }
                observable.setLblFreezeStatus(CommonConstants.STATUS_CREATED);
    }//GEN-LAST:event_btnCropNewActionPerformed
                
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
//            data.append("\nLog in Time : ").append(map.get("LOCKED_TIME")) ;
//            data.append("\nIP Address : ").append(map.get("IP_ADDR")) ;
//            data.append("\nBranch : ").append(map.get("BRANCH_ID"));
        }
        lstLock = null ;
        map = null ;
        return data.toString();
    }
    
    
    
    
    /** To populate Comboboxes */
    private void initComponentData() {
        cboCropType.setModel(observable.getCbmCropType());
        tblEligibilityList.setModel(observable.getTblEligibilityList());
//        cboType.setModel(observable.getCbmType());
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
//                if(advances.equals("ADVANCES")){
//                    double clearBalance=CommonUtil.convertObjToDouble(lblClearBalanceDisplay.getText()).doubleValue();
//                }
                
            }
            else if(viewType != -1){
                fillDataNew(hash);
                viewType = -1;
            }
            else if( observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW){
                //fillDataNew(hash);
                fillDataEdit(hash);
                setButtonEnableDisable();
//                btnAccountNumber.setEnabled(false);
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
//            enableDisable(true);
//            cboProductID.setEnabled(false);
//            txtAccountNumber.setEnabled(false);
//            setFreezeButtonEnableDisableDefault();
//            ClientUtil.enableDisable(this.panEligibilityDetails, false);
        }
    }
    
    /** To fillData for a new entry */
    private void fillDataNew(HashMap hash){
//        txtAccountNumber.setText((String)hash.get("ACCOUNT NUMBER"));
        
        //__ Reset the Table with the change of Account No...
        //        observable.resetForm();
        observable.resetAccountDetails();
        observable.resetFreezeListTable();
        observable.resetObjects();
        showCustomerName();
//         txtAccountNumber.setText((String)hash.get("ACCOUNT NUMBER"));
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
//        observable.getProductDetails(hash);
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
        final HashMap whereMap = new HashMap();
//        System.out.println("Prod Id :"+(String)(observable.getCbmProductID()).getKeyForSelected());
        
        //__ Only the Authorized, Operational/New And not already Freezed records Should be Available...
//        whereMap.put("PRODID", (String)(observable.getCbmProductID()).getKeyForSelected());
        whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
        testMap.put(CommonConstants.MAP_WHERE, whereMap);
        
        return testMap;
    }
    
    /** To get popUp data for already existing entries for modification */
    private HashMap freezeEditMap(){
        final HashMap testMap = new HashMap();
        //__ All but Deleted and Authorized Records Should be Available...
        
     
         testMap.put(CommonConstants.MAP_NAME, "editLoanEligibilityTO");
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
//        updateUIFields();
        observable.getCustomerDetails();
    }
    
    public void update(java.util.Observable o, Object arg) {
        
        txtEligibileAmount.setText(observable.getTxtEligibileAmount());
        tdtFromDate.setDateValue(observable.getTdtFromDate());
        tdtToDate.setDateValue(observable.getTdtToDate());
    }
    
     public void updateOBFields() {
        observable.setTxtEligibileAmount(txtEligibileAmount.getText());
        observable.setTdtFromDate(tdtFromDate.getDateValue());
        observable.setTdtToDate(tdtToDate.getDateValue());
    }
    private void setFieldNames() {
        lblCropType.setName("txtRemarks");
        cboCropType.setName("txtRemarks");
        lblEligibileAmount.setName("txtRemarks");
        txtEligibileAmount.setName("txtRemarks");
        lblFromDate.setName("txtRemarks");
        tdtFromDate.setName("txtRemarks");
        lblToDate.setName("txtRemarks");
        tdtToDate.setName("txtRemarks");
        panDateEntyDetails.setName("txtRemarks");
        panEligibilityDetails.setName("txtRemarks");
        panTableList.setName("txtRemarks");
        tblEligibilityList.setName("txtRemarks");
       
    }
    
    private void internationalize() {
        //        FreezeRB resourceBundle = new FreezeRB();
        lblCropType.setText(resourceBundle.getString("lblCropType"));
        lblEligibileAmount.setText(resourceBundle.getString("lblEligibileAmount"));
        lblFromDate.setText(resourceBundle.getString("lblFromDate"));
        lblToDate.setText(resourceBundle.getString("lblToDate"));
      
    }
    
    public void setHelpMessage() {
        LoanEligibilityMaintenanceMRB objMandatoryRB = new LoanEligibilityMaintenanceMRB();
        cboCropType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCropType"));
        txtEligibileAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtEligibileAmount"));
        tdtFromDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtFromDate"));
        tdtToDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtToDate"));
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
        
//        btnAccountNumber.setEnabled(!btnNew.isEnabled());
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
        this.btnCropNew.setEnabled(false);
        this.btnCropSave.setEnabled(false);
        this.btnCropDelete.setEnabled(false);
//        this.btnUnFreeze.setEnabled(false);
    }
    
    private void setFreezeButtonEnableDisableDefault(boolean flag){
        this.btnCropNew.setEnabled(flag);
        this.btnCropSave.setEnabled(!flag);
        this.btnCropDelete.setEnabled(!flag);
    }
    
    private void setFreezeButtonEnableDisableNew(){
//        this.btnCropNew.setEnabled(false);
//        this.btnCropSave.setEnabled(true);
//        this.btnCropDelete.setEnabled(false);
//        this.btnUnFreeze.setEnabled(false);
    }
    
    private void setFreezeButtonEnableDisableSelect(){
//        this.btnCropNew.setEnabled(false);
//        this.btnCropSave.setEnabled(true);
//        this.btnCropDelete.setEnabled(true);
//        this.btnUnFreeze.setEnabled(false);
    }
    
    private void formButtonsEnableDisable(){
        enableDisable(false);
        setButtonEnableDisable();
        setFreezeButtonEnableDisable();
    }
    
    //To enable disable fields after a delete or UnFreeze operation
    private void onDeleteUnFreeze(){
//        ClientUtil.enableDisable(this.panEligibilityDetails, false);
//        setFreezeButtonEnableDisableDefault();
    }
    public void clearBalances(){
//        lblClearBalanceDisplay.setText("");
//        lblClearBalanceDisplay1.setText("");
//        lblExistingFreezesSumDisplay.setText("");
//        lblExistingLienSumDisplay.setText("");
//        txtAccountNumber.setText("");
//        lblCustomerNameDisplay.setText("");
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnCropDelete;
    private com.see.truetransact.uicomponent.CButton btnCropNew;
    private com.see.truetransact.uicomponent.CButton btnCropSave;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboCropType;
    private com.see.truetransact.uicomponent.CLabel lblCropType;
    private com.see.truetransact.uicomponent.CLabel lblEligibileAmount;
    private com.see.truetransact.uicomponent.CLabel lblFromDate;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace65;
    private com.see.truetransact.uicomponent.CLabel lblSpace66;
    private com.see.truetransact.uicomponent.CLabel lblSpace67;
    private com.see.truetransact.uicomponent.CLabel lblSpace68;
    private com.see.truetransact.uicomponent.CLabel lblSpace69;
    private com.see.truetransact.uicomponent.CLabel lblSpace70;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblToDate;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panDateEntyDetails;
    private com.see.truetransact.uicomponent.CPanel panEligibilityDetails;
    private com.see.truetransact.uicomponent.CPanel panFreeze;
    private com.see.truetransact.uicomponent.CPanel panFreezeSave;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTable;
    private com.see.truetransact.uicomponent.CPanel panTableList;
    private com.see.truetransact.uicomponent.CButtonGroup rdoCreditDebit;
    private com.see.truetransact.uicomponent.CSeparator sptDetails;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CScrollPane srpFreezeList;
    private com.see.truetransact.uicomponent.CTable tblEligibilityList;
    private javax.swing.JToolBar tbrOperativeAcctProduct;
    private com.see.truetransact.uicomponent.CDateField tdtFromDate;
    private com.see.truetransact.uicomponent.CDateField tdtToDate;
    private com.see.truetransact.uicomponent.CTextField txtEligibileAmount;
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
        LoanEligibilityMaintenanceUI fui = new LoanEligibilityMaintenanceUI();
        javax.swing.JFrame j = new javax.swing.JFrame();
        j.getContentPane().add(fui);
        j.show();
        fui.show();
    }
    
   
    
}
