/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * HeadUI.java
 *
 * Created on August 7, 2003, 3:18 PM
 */

package com.see.truetransact.ui.generalledger;

import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.common.viewall.TableDialogUI;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientproxy.ProxyParameters;

import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import org.apache.log4j.Logger;
import java.util.Date;
/**
 *
 * @author  Annamalai
 */
public class HeadUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer,UIMandatoryField{
    
    private HeadOB _observable;
    private HashMap _mandatoryMap;
    private boolean _subHeadNew = false;
    private final HashMap accountType = new HashMap();
    private final String TRIPLE_ZERO = "000";
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.generalledger.HeadRB", ProxyParameters.LANGUAGE);
    private final static Logger _log = Logger.getLogger(HeadUI.class);
    
    final int EDIT=0,DELETE=1,AUTHORIZE=2, VIEW=4;
    int viewType=-1;
    boolean isFilled = false;
    boolean isAuth = false;
    private Date currDt = null;
    /** Creates new form HeadUI */
    public HeadUI() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        initStartup();
    }
    
    private void initStartup() {
        setFieldNames();
        internationalize();
        setMaxLengths();
        setObservable();
        initComponentData();
        ClientUtil.enableDisable(this, false);
        setButtonEnableDisable();
        setSubHeadButtonDefaultEnableDisable(false);
        setMandatoryHashMap();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panMajorHead);
        _observable.resetStatus();
        _observable.resetForm();
        setHelpMessage();
        accTypePut();
        this.txtMajorHeadCode1.setEnabled(false);
        this.btnView.setEnabled(false);
    }
    
    private void setMaxLengths(){
        txtMajorHeadCode1.setMaxLength(1);
        txtMajorHeadCode2.setMaxLength(3);
        txtMajorHeadCode2.setValidation(new NumericValidation());
        txtMajorHeadDesc.setMaxLength(128);
        
        txtSubHeadCode.setMaxLength(3);
        txtSubHeadCode.setValidation(new NumericValidation());
        txtSubHeadDesc.setMaxLength(128);
    }
    
    public void setMandatoryHashMap() {
        _mandatoryMap = new HashMap();
        _mandatoryMap.put("txtMajorHeadCode1", new Boolean(true));
        _mandatoryMap.put("txtMajorHeadCode2", new Boolean(true));
        _mandatoryMap.put("txtMajorHeadDesc", new Boolean(true));
        _mandatoryMap.put("txtSubHeadCode", new Boolean(true));
        _mandatoryMap.put("txtSubHeadDesc", new Boolean(true));
        _mandatoryMap.put("cboAccountType", new Boolean(true));
    }
    
    public HashMap getMandatoryHashMap() {
        return _mandatoryMap;
    }
    
    
    
    public void setHelpMessage() {
        HeadMRB objMandatoryRB = new HeadMRB();
        txtMajorHeadCode1.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMajorHeadCode1"));
        txtMajorHeadCode2.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMajorHeadCode2"));
        txtMajorHeadDesc.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMajorHeadDesc"));
        txtSubHeadCode.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSubHeadCode"));
        txtSubHeadDesc.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSubHeadDesc"));
        cboAccountType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboAccountType"));
        cboFinalActType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboFinalActType"));
        cboSubAccountType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboSubAccountType"));
        chkGLHeadConsolidated.setHelpMessage(lblMsg, objMandatoryRB.getString("chkGLHeadConsolidated"));
        
    }
    
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
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        dlgMajorHead = new com.see.truetransact.uicomponent.CDialog();
        srpMajorHead = new com.see.truetransact.uicomponent.CScrollPane();
        tblMajorHead = new com.see.truetransact.uicomponent.CTable();
        tbrHead = new javax.swing.JToolBar();
        btnView1 = new com.see.truetransact.uicomponent.CButton();
        lblSpace6 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace11 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace12 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace13 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace14 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace15 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace16 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panHead = new com.see.truetransact.uicomponent.CPanel();
        panTable = new com.see.truetransact.uicomponent.CPanel();
        srpSubHeadTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblSubHeadList = new com.see.truetransact.uicomponent.CTable();
        panSubHeadButton = new com.see.truetransact.uicomponent.CPanel();
        panSubHead = new com.see.truetransact.uicomponent.CPanel();
        lblSubHeadCode = new com.see.truetransact.uicomponent.CLabel();
        lblSubHeadDesc = new com.see.truetransact.uicomponent.CLabel();
        panButton = new com.see.truetransact.uicomponent.CPanel();
        btnSubHeadSave = new com.see.truetransact.uicomponent.CButton();
        btnSubHeadNew = new com.see.truetransact.uicomponent.CButton();
        btnSubHeadDelete = new com.see.truetransact.uicomponent.CButton();
        txtSubHeadCode = new com.see.truetransact.uicomponent.CTextField();
        txtSubHeadDesc = new com.see.truetransact.uicomponent.CTextField();
        panMajorHead = new com.see.truetransact.uicomponent.CPanel();
        lblMajorHeadCode = new com.see.truetransact.uicomponent.CLabel();
        lblMajorHeadDesc = new com.see.truetransact.uicomponent.CLabel();
        txtMajorHeadDesc = new com.see.truetransact.uicomponent.CTextField();
        lblAccountType = new com.see.truetransact.uicomponent.CLabel();
        cboAccountType = new com.see.truetransact.uicomponent.CComboBox();
        panMajorHeadCode = new com.see.truetransact.uicomponent.CPanel();
        txtMajorHeadCode1 = new com.see.truetransact.uicomponent.CTextField();
        txtMajorHeadCode2 = new com.see.truetransact.uicomponent.CTextField();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblGLHeadConsolidated = new com.see.truetransact.uicomponent.CLabel();
        chkGLHeadConsolidated = new com.see.truetransact.uicomponent.CCheckBox();
        lblFinalActType = new com.see.truetransact.uicomponent.CLabel();
        cboFinalActType = new com.see.truetransact.uicomponent.CComboBox();
        lblSubAccountType = new com.see.truetransact.uicomponent.CLabel();
        cboSubAccountType = new com.see.truetransact.uicomponent.CComboBox();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        mbrMain = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        sptEdit = new javax.swing.JSeparator();
        mitDelete = new javax.swing.JMenuItem();
        sptDelete = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitClose = new javax.swing.JMenuItem();

        dlgMajorHead.getContentPane().setLayout(new java.awt.GridBagLayout());

        tblMajorHead.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Major Head Code", "Major Head Name"
            }
        ));
        srpMajorHead.setViewportView(tblMajorHead);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        dlgMajorHead.getContentPane().add(srpMajorHead, gridBagConstraints);

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Major/Sub Head");
        setMinimumSize(new java.awt.Dimension(730, 114));

        btnView1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView1.setToolTipText("Enquiry");
        btnView1.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView1.setPreferredSize(new java.awt.Dimension(21, 21));
        btnView1.setEnabled(false);
        btnView1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnView1ActionPerformed(evt);
            }
        });
        tbrHead.add(btnView1);

        lblSpace6.setText("     ");
        tbrHead.add(lblSpace6);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrHead.add(btnNew);

        lblSpace11.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace11.setText("     ");
        lblSpace11.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace11.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace11.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace11);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrHead.add(btnEdit);

        lblSpace12.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace12.setText("     ");
        lblSpace12.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace12.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace12.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace12);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrHead.add(btnDelete);

        lblSpace4.setText("     ");
        tbrHead.add(lblSpace4);

        lblSpace2.setText("     ");
        tbrHead.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrHead.add(btnSave);

        lblSpace13.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace13.setText("     ");
        lblSpace13.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace13.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace13.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace13);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrHead.add(btnCancel);

        lblSpace3.setText("     ");
        tbrHead.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrHead.add(btnAuthorize);

        lblSpace14.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace14.setText("     ");
        lblSpace14.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace14.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace14.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace14);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrHead.add(btnException);

        lblSpace15.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace15.setText("     ");
        lblSpace15.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace15.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace15.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace15);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrHead.add(btnReject);

        lblSpace5.setText("     ");
        tbrHead.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrHead.add(btnPrint);

        lblSpace16.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace16.setText("     ");
        lblSpace16.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace16.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace16.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace16);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrHead.add(btnClose);

        getContentPane().add(tbrHead, java.awt.BorderLayout.NORTH);

        panHead.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panHead.setLayout(new java.awt.GridBagLayout());

        panTable.setLayout(new java.awt.GridBagLayout());

        srpSubHeadTable.setPreferredSize(new java.awt.Dimension(300, 350));

        tblSubHeadList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblSubHeadList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblSubHeadListMousePressed(evt);
            }
        });
        srpSubHeadTable.setViewportView(tblSubHeadList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTable.add(srpSubHeadTable, gridBagConstraints);

        panSubHeadButton.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panTable.add(panSubHeadButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panHead.add(panTable, gridBagConstraints);

        panSubHead.setBorder(javax.swing.BorderFactory.createTitledBorder("Sub Head"));
        panSubHead.setLayout(new java.awt.GridBagLayout());

        lblSubHeadCode.setText("Sub Head Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSubHead.add(lblSubHeadCode, gridBagConstraints);

        lblSubHeadDesc.setText("Sub Head Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSubHead.add(lblSubHeadDesc, gridBagConstraints);

        panButton.setLayout(new java.awt.GridBagLayout());

        btnSubHeadSave.setText("Save");
        btnSubHeadSave.setEnabled(false);
        btnSubHeadSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubHeadSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton.add(btnSubHeadSave, gridBagConstraints);

        btnSubHeadNew.setText("New");
        btnSubHeadNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubHeadNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton.add(btnSubHeadNew, gridBagConstraints);

        btnSubHeadDelete.setText("Delete");
        btnSubHeadDelete.setEnabled(false);
        btnSubHeadDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubHeadDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton.add(btnSubHeadDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panSubHead.add(panButton, gridBagConstraints);

        txtSubHeadCode.setMaxLength(4);
        txtSubHeadCode.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSubHead.add(txtSubHeadCode, gridBagConstraints);

        txtSubHeadDesc.setAllowAll(true);
        txtSubHeadDesc.setMaxLength(128);
        txtSubHeadDesc.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSubHead.add(txtSubHeadDesc, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panHead.add(panSubHead, gridBagConstraints);

        panMajorHead.setBorder(javax.swing.BorderFactory.createTitledBorder("Major Head"));
        panMajorHead.setLayout(new java.awt.GridBagLayout());

        lblMajorHeadCode.setText("Major Head Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMajorHead.add(lblMajorHeadCode, gridBagConstraints);

        lblMajorHeadDesc.setText("Major Head Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMajorHead.add(lblMajorHeadDesc, gridBagConstraints);

        txtMajorHeadDesc.setMaxLength(128);
        txtMajorHeadDesc.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMajorHead.add(txtMajorHeadDesc, gridBagConstraints);

        lblAccountType.setText("Account Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMajorHead.add(lblAccountType, gridBagConstraints);

        cboAccountType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboAccountType.setPopupWidth(150);
        cboAccountType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboAccountTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMajorHead.add(cboAccountType, gridBagConstraints);

        panMajorHeadCode.setMinimumSize(new java.awt.Dimension(102, 21));
        panMajorHeadCode.setPreferredSize(new java.awt.Dimension(102, 21));
        panMajorHeadCode.setLayout(new java.awt.GridBagLayout());

        txtMajorHeadCode1.setMaxLength(3);
        txtMajorHeadCode1.setMinimumSize(new java.awt.Dimension(17, 21));
        txtMajorHeadCode1.setPreferredSize(new java.awt.Dimension(17, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panMajorHeadCode.add(txtMajorHeadCode1, gridBagConstraints);

        txtMajorHeadCode2.setMinimumSize(new java.awt.Dimension(84, 21));
        txtMajorHeadCode2.setPreferredSize(new java.awt.Dimension(84, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panMajorHeadCode.add(txtMajorHeadCode2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMajorHead.add(panMajorHeadCode, gridBagConstraints);

        btnView.setText("View");
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        panMajorHead.add(btnView, gridBagConstraints);

        lblGLHeadConsolidated.setText("GL Heads to be Consolidated");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMajorHead.add(lblGLHeadConsolidated, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 1, 4, 4);
        panMajorHead.add(chkGLHeadConsolidated, gridBagConstraints);

        lblFinalActType.setText("Final Account Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMajorHead.add(lblFinalActType, gridBagConstraints);

        cboFinalActType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "Trading", "Profit And Loss", "Balance Sheet" }));
        cboFinalActType.setMaximumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        panMajorHead.add(cboFinalActType, gridBagConstraints);

        lblSubAccountType.setText("Sub Account Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMajorHead.add(lblSubAccountType, gridBagConstraints);

        cboSubAccountType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "Interest Payable", "Interest Recievable", "Reserves", "Net Profit", "Netloss", "Interest Paid", "Interest Recieved" }));
        cboSubAccountType.setMaximumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        panMajorHead.add(cboSubAccountType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panHead.add(panMajorHead, gridBagConstraints);

        getContentPane().add(panHead, java.awt.BorderLayout.CENTER);

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
        mnuProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuProcessActionPerformed(evt);
            }
        });

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
        mnuProcess.add(sptEdit);

        mitDelete.setText("Delete");
        mnuProcess.add(mitDelete);
        mnuProcess.add(sptDelete);

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
        mnuProcess.add(mitClose);

        mbrMain.add(mnuProcess);

        setJMenuBar(mbrMain);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnView1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnView1ActionPerformed
        // TODO add your handling code here:
        _observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        _observable.setStatus();
        lblStatus.setText(_observable.getLblStatus());
        popUp();
        btnCheck();
    }//GEN-LAST:event_btnView1ActionPerformed
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
        _observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        _observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        _observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    public void authorizeStatus(String authorizeStatus) {
        if (viewType == AUTHORIZE && isFilled){
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("USER_ID", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("AUTHORIZEDT", currDt.clone());
            final String ACCTHD = CommonUtil.convertObjToStr(txtMajorHeadCode1.getText()) + CommonUtil.convertObjToStr(txtMajorHeadCode2.getText());
            System.out.println("ACCTHD: " + ACCTHD);
            
            singleAuthorizeMap.put("MAJOR ACCOUNT HEAD", ACCTHD);
            ClientUtil.execute("authorizeHeadData", singleAuthorizeMap);
            
            btnCancelActionPerformed(null);
        } else{
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            
            //__ To Save the data in the Internal Frame...
            setModified(true);
            
            mapParam.put(CommonConstants.MAP_NAME, "getHeadAuthorizeList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeHeadData");
            
            isFilled = false;
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
        }
    }
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // Add your handling code here:
        final HashMap actType = new HashMap();
        actType.put("MJR_AC_HD_TYPE",CommonUtil.convertObjToStr(((ComboBoxModel)cboAccountType.getModel()).getKeyForSelected()));
        final TableDialogUI objTableDialogUI = new TableDialogUI("getMjrAcHdList", actType);
        objTableDialogUI.setTitle("Major Head List");
        objTableDialogUI.show();
    }//GEN-LAST:event_btnViewActionPerformed
    
    private void cboAccountTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboAccountTypeActionPerformed
        // Add your handling code here:
        if (!(cboAccountType.getSelectedIndex() == -1 || cboAccountType.getSelectedIndex() ==0)){
            txtMajorHeadCode1.setText(accTypeGet(((ComboBoxModel)cboAccountType.getModel()).getKeyForSelected()));
            this.btnView.setEnabled(true);
        }else{
            txtMajorHeadCode1.setText("");
            this.btnView.setEnabled(false);
        }
    }//GEN-LAST:event_cboAccountTypeActionPerformed
    
    private void accTypePut(){
        accountType.put(GLConstants.ASSETS,"1");
        accountType.put(GLConstants.LIABILITY, "2");
        accountType.put(GLConstants.EXPENDITURE, "5");
        accountType.put(GLConstants.INCOME, "4");
//        accountType.put(GLConstants.CONTRAASSETS, "9");
        accountType.put(GLConstants.CONTRALIABILITY, "3");
    }
    
    private String accTypeGet(Object key){
        return (String)accountType.get(CommonUtil.convertObjToStr(key));
    }
    
    private void btnSubHeadDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubHeadDeleteActionPerformed
        // Add your handling code here:
        final HashMap acHd = new HashMap();
        acHd.put("MJR_HD",txtMajorHeadCode1.getText()+txtMajorHeadCode2.getText());
        acHd.put("SUB_HD",txtSubHeadCode.getText());
        final Integer count = (Integer)ClientUtil.executeQuery("ctAcHd_SubHd", acHd).get(0);
        if (CommonUtil.convertObjToInt(count) >0){
            displayAlert(resourceBundle.getString("acHdExistance"));
        }else if (deleteAlert() == 0){
            updateOBFields();
            _observable.deleteSubHeadDesc();
            _observable.resetSubHeadDetails();
            ClientUtil.enableDisable(panSubHead,false);
            btnSubHeadNew.setEnabled(false);
            setSubHeadButtonEnableDisable();
            _observable.ttNotifyObservers();
        }
    }//GEN-LAST:event_btnSubHeadDeleteActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        if (tblSubHeadList.getRowCount() > 0){
            displayAlert(resourceBundle.getString("subAcHdExistance"));
        }else if (deleteAlert() == 0){
            _observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
            savePerformed();
        }
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnSubHeadNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubHeadNewActionPerformed
        // Add your handling code here:
        updateOBFields();
        _observable.resetSubHeadDetails();
        _observable.ttNotifyObservers();
        ClientUtil.enableDisable(panSubHead,true);
        setSubHeadButtonOnSelectionEnableDisable();
        _subHeadNew = true;
        txtSubHeadCode.requestFocus(true);
    }//GEN-LAST:event_btnSubHeadNewActionPerformed
    
    private void tblSubHeadListMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSubHeadListMousePressed
        // Add your handling code here:
        if (_observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE){
            //When the Action Type is NOT DELETE alone the following operation should be done
            caseNotDelete();
        }
    }//GEN-LAST:event_tblSubHeadListMousePressed
    
    /* To get the data in the Selected Row and display on apt places and enabling apt components*/
    private void caseNotDelete(){
        updateOBFields();
        _observable.populateSubHead(tblSubHeadList.getSelectedRow());
        if(viewType == AUTHORIZE|| _observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW){
            ClientUtil.enableDisable(panSubHead,false);
        }else{
            ClientUtil.enableDisable(panSubHead,true);
        }
        txtSubHeadCode.setEnabled(false);
        setSubHeadButtonDefaultEnableDisable(true);
        _subHeadNew = false;
    }
    
    private int deleteAlert(){
        final String[] options = {resourceBundle.getString("cDialogOK"),resourceBundle.getString("cDialogCancel")};
        final int option = COptionPane.showOptionDialog(null, resourceBundle.getString("deleteWarning"), CommonConstants.WARNINGTITLE,
        COptionPane.OK_CANCEL_OPTION, COptionPane.WARNING_MESSAGE,
        null, options, options[0]);
        return option;
    }
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        _observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        popUp();
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // Add your handling code here:
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        setModified(false);
        _observable.resetForm();
        _observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        ClientUtil.enableDisable(this, false);
        if(isAuth){
            btnDelete.setEnabled(true);
            isAuth = false;
        }
        setButtonEnableDisable();
        _observable.setStatus();
        setSubHeadButtonDefaultEnableDisable(false);
        
        isFilled = false;
        viewType = -1;
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // Add your handling code here:
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        
        final String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panMajorHead);
        if (mandatoryMessage.length() > 1){
            displayAlert(mandatoryMessage);
        }else{
            savePerformed();
        }
        
        isFilled = false;
    }//GEN-LAST:event_btnSaveActionPerformed
    
    /** Contains the operations to be done when the Save operation is performed and
     * Mandatory conditons are satisfied
     **/
    private void savePerformed(){
        boolean notExist = true;
        if (_observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
            txtMajorHeadCode2.setText(CommonUtil.lpad(CommonUtil.convertObjToStr(txtMajorHeadCode2.getText()),3,'0'));
            
            System.out.println("txtMajorHeadCode2: " + txtMajorHeadCode2.getText());
            
            if (txtMajorHeadCode2.getText().equals(TRIPLE_ZERO)){
                displayAlert(resourceBundle.getString("mjrHdZero"));
                txtMajorHeadCode2.requestFocus();
                notExist = false;
            }else{
                final HashMap mjrAcHd = new HashMap();
                mjrAcHd.put("MJR_CODE",txtMajorHeadCode1.getText()+txtMajorHeadCode2.getText());
                final Integer count = (Integer)ClientUtil.executeQuery("chkMjrAcHdExistance", mjrAcHd).get(0);
                if (CommonUtil.convertObjToInt(count) >0){
                    displayAlert(resourceBundle.getString("mjrHdExistance"));
                    notExist = false;
                }else{
                    notExist = true;
                }
            }
        }
        updateOBFields();
        if (notExist){
            _observable.doAction();
            ClientUtil.enableDisable(this, false);
            if(isAuth){
                btnDelete.setEnabled(true);
                isAuth = false;
            }
            setButtonEnableDisable();
            setSubHeadButtonDefaultEnableDisable(false);
            _observable.resetForm();
            _observable.setStatus();
            _observable.setResultStatus();
        }
        
        setModified(false);
    }
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        setModified(true);
        _observable.resetForm();
        ClientUtil.enableDisable(panMajorHead, true);
        this.txtMajorHeadCode1.setEnabled(false);
        setSubHeadButtonEnableDisable();
        setButtonEnableDisable();
        _observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        _observable.setStatus();
        cboAccountType.requestFocus(true);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnNewActionPerformed
    
    private void btnSubHeadSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubHeadSaveActionPerformed
        // Add your handling code here:
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panSubHead);
        txtSubHeadCode.setText(CommonUtil.lpad(CommonUtil.convertObjToStr(txtSubHeadCode.getText()), 3, '0'));
        
        System.out.println("txtSubHeadCode: " + txtSubHeadCode.getText());
        
        
        if (txtSubHeadCode.getText().equals(TRIPLE_ZERO)){
            mandatoryMessage = mandatoryMessage + resourceBundle.getString("subHdZero");
        }
        if (mandatoryMessage.length() > 1){
            displayAlert(mandatoryMessage);
        }else{
            subHeadSavePerformed();
        }
        mandatoryMessage = null;
    }//GEN-LAST:event_btnSubHeadSaveActionPerformed
    
    /** Contains the operations to be done when the Sub Head Save operation is performed and
     * Mandatory conditons are satisfied
     **/
    private void subHeadSavePerformed(){
        updateOBFields();
        _log.info("_subHeadNew:"+_subHeadNew);
        final int option = _observable.addSubHeadList(_subHeadNew);
        _log.info("option:"+option);
        setSubHeadButtonEnableDisable();
        if (option == 2){
            btnSubHeadSave.setEnabled(true);
        }else {
            ClientUtil.enableDisable(panSubHead,false);
        }
    }
    
    private void mnuProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuProcessActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_mnuProcessActionPerformed
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // Add your handling code here:
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // Add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed
    
    /** To display a popUp window for viewing existing data */
    private void popUp() {
        final HashMap viewMap = new HashMap();
        if ( _observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ||  _observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || _observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW){
            viewMap.put("MAPNAME", "viewAllMajorHead");
        }
        new ViewAll(this, viewMap).show();
    }
    
    private void setObservable() {
        //_observable = new HeadOB();
        /* Implementing Singleton pattern */
        _observable = HeadOB.getInstance();
        _observable.addObserver(this);
    }
    
    /*To set model for combo boxes*/
    private void initComponentData() {
        cboAccountType.setModel(_observable.getCbmAccountType());
        cboFinalActType.setModel(_observable.getCbmFinalActType());
        cboSubAccountType.setModel(_observable.getCbmSubAccountType());
    }
    
    /**
     * Called by the Popup window created thru popUp method
     **/
    public void fillData(Object param) {
        setModified(true);
        final HashMap hash = (HashMap) param;
        System.out.println("hash:" + hash);
        if (_observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT
        || _observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
        || viewType == AUTHORIZE ||
         _observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW){
            isFilled = true;
            actionEditDelete(hash);
        }
        if(_observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW)
        {
            ClientUtil.enableDisable(panHead,false);
        }
        if(viewType == AUTHORIZE){
                    btnAuthorize.setEnabled(_observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(_observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(_observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                    btnDelete.setEnabled(false);
                    ClientUtil.enableDisable(panMajorHead,false);
        }
        if (_observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
            btnAuthorize.setEnabled(false);
            btnReject.setEnabled(false);
            btnException.setEnabled(false);
        }
    }
    
    /*To get the data and populating on the screen,set the status and enabling the apt components*/
    private void actionEditDelete(HashMap hash){
        hash.put("WHERE", hash.get("MAJOR ACCOUNT HEAD"));
        _observable.setStatus();
        _observable.populateData(hash);
        setButtonEnableDisable();
        enableCompForEditDelete();
        
        if(CommonUtil.convertObjToStr(_observable.getAuthStatus()).length() > 0){
            isAuth = true;
            btnDelete.setEnabled(false);
            txtMajorHeadDesc.setEnabled(false);
        }
    }
    
    /* To enable apt components for Edit and Delete options*/
    private void enableCompForEditDelete(){
        if (_observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE){
            ClientUtil.enableDisable(this, false);
        }else{
            enableCompForEdit();
        }
    }
    
    /** To enable the components that should be enabled for Edit option.
     * The components to be enabled are txtMajorHeadDesc and btnSubHeadNew
     **/
    private void enableCompForEdit(){
        setDelBtnEnableDisable(true);
        ClientUtil.enableDisable(panMajorHead, true);
        cboAccountType.setEnabled(false);
        txtMajorHeadCode1.setEnabled(false);
        txtMajorHeadCode2.setEnabled(false);
        btnSubHeadNew.setEnabled(true);
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
        btnView1.setEnabled(!btnView1.isEnabled());
        
        setDelBtnEnableDisable(false);
    }
    
    private void setDelBtnEnableDisable(boolean enableDisable){
        btnDelete.setEnabled(enableDisable );
        mitDelete.setEnabled(enableDisable);
    }
    
    private void setSubHeadButtonDefaultEnableDisable(boolean enableDisable) {
        btnSubHeadNew.setEnabled(enableDisable);
        btnSubHeadSave.setEnabled(enableDisable);
        btnSubHeadDelete.setEnabled(enableDisable);
    }
    
    /* To enable or disable sub Head buttons when any one of them is selected*/
    private void setSubHeadButtonOnSelectionEnableDisable() {
        btnSubHeadNew.setEnabled(true);
        btnSubHeadSave.setEnabled(true);
        btnSubHeadDelete.setEnabled(false);
    }
    
    /* To enable only the Sub Head New button*/
    private void setSubHeadButtonEnableDisable() {
        btnSubHeadNew.setEnabled(true);
        btnSubHeadSave.setEnabled(false);
        btnSubHeadDelete.setEnabled(false);
    }
    
    private void setFieldNames() {
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnSave.setName("btnSave");
        btnSubHeadNew.setName("btnSubHeadNew");
        btnSubHeadSave.setName("btnSubHeadSave");
        btnSubHeadDelete.setName("btnSubHeadDelete");
        cboAccountType.setName("cboAccountType");
        dlgMajorHead.setName("dlgMajorHead");
        lblAccountType.setName("lblAccountType");
        lblMajorHeadCode.setName("lblMajorHeadCode");
        lblMajorHeadDesc.setName("lblMajorHeadDesc");
        lblGLHeadConsolidated.setName("lblGLHeadConsolidated");
        lblMsg.setName("lblMsg");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblStatus.setName("lblStatus");
        lblSubHeadCode.setName("lblSubHeadCode");
        lblSubHeadDesc.setName("lblSubHeadDesc");
        mbrMain.setName("mbrMain");
        panButton.setName("panButton");
        panHead.setName("panHead");
        panMajorHead.setName("panMajorHead");
        panStatus.setName("panStatus");
        panSubHead.setName("panSubHead");
        panSubHeadButton.setName("panSubHeadButton");
        panTable.setName("panTable");
        srpMajorHead.setName("srpMajorHead");
        srpSubHeadTable.setName("srpSubHeadTable");
        tblMajorHead.setName("tblMajorHead");
        tblSubHeadList.setName("tblSubHeadList");
        panMajorHeadCode.setName("panMajorHeadCode");
        txtMajorHeadCode1.setName("txtMajorHeadCode1");
        txtMajorHeadCode2.setName("txtMajorHeadCode2");
        txtMajorHeadDesc.setName("txtMajorHeadDesc");
        txtSubHeadCode.setName("txtSubHeadCode");
        txtSubHeadDesc.setName("txtSubHeadDesc");
    }
    
    
    private void internationalize() {
        btnSubHeadDelete.setText(resourceBundle.getString("btnSubHeadDelete"));
        btnSubHeadNew.setText(resourceBundle.getString("btnSubHeadNew"));
        btnSubHeadSave.setText(resourceBundle.getString("btnSubHeadSave"));
        lblSubHeadDesc.setText(resourceBundle.getString("lblSubHeadDesc"));
        lblMajorHeadCode.setText(resourceBundle.getString("lblMajorHeadCode"));
        lblSubHeadCode.setText(resourceBundle.getString("lblSubHeadCode"));
        lblMajorHeadDesc.setText(resourceBundle.getString("lblMajorHeadDesc"));
        lblGLHeadConsolidated.setText(resourceBundle.getString("lblGLHeadConsolidated"));
    }
    
    public void update(java.util.Observable observed, Object arg) {
        cboAccountType.setSelectedItem(_observable.getCboAccountType());
        
        if(_observable.getCboFinalActType()!=null){
        cboFinalActType.setSelectedItem(_observable.getCboFinalActType());
        }else{
            cboFinalActType.setSelectedIndex(0);
        }
        txtMajorHeadCode1.setText(_observable.getTxtMajorHeadCode1());
        txtMajorHeadCode2.setText(_observable.getTxtMajorHeadCode2());
        txtMajorHeadDesc.setText(_observable.getTxtMajorHeadDesc());
        //Added By Suresh
        if(_observable.getChkGLHeadConsolidated() == true){
            chkGLHeadConsolidated.setSelected(true);
        }else{
            chkGLHeadConsolidated.setSelected(false);
        }
        txtSubHeadCode.setText(_observable.getTxtSubHeadCode());
        txtSubHeadDesc.setText(_observable.getTxtSubHeadDesc());
        cboFinalActType.setSelectedItem(_observable.getCboFinalActType());
        cboSubAccountType.setSelectedItem(_observable.getCboSubAccountType());
        tblSubHeadList.setModel(_observable.getTblSubHeadList());
        lblStatus.setText(_observable.getLblStatus());
    }
    
    public void updateOBFields() {
        _observable.setCboAccountType((String)cboAccountType.getSelectedItem());
        
        _observable.setTxtMajorHeadCode1(txtMajorHeadCode1.getText());
        _observable.setTxtMajorHeadCode2(txtMajorHeadCode2.getText());
        _observable.setTxtMajorHeadDesc(txtMajorHeadDesc.getText());
        if(chkGLHeadConsolidated.isSelected()==true)
            _observable.setChkGLHeadConsolidated(true);
        else
            _observable.setChkGLHeadConsolidated(false);
        _observable.setTxtSubHeadCode(txtSubHeadCode.getText());
        _observable.setTxtSubHeadDesc(txtSubHeadDesc.getText());
        _observable.setCboFinalActType((String)cboFinalActType.getSelectedItem());
        _observable.setCboSubAccountType((String)cboSubAccountType.getSelectedItem());
        _observable.setTblSubHeadList((com.see.truetransact.clientutil.EnhancedTableModel)tblSubHeadList.getModel());
        _observable.setModule(getModule());
        _observable.setScreen(getScreen());
    }
    
    
    private String checkMandatory(javax.swing.JComponent component){
        return new MandatoryCheck().checkMandatory(getClass().getName(), component);
    }
    
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    public  static void main(String args[]){
        javax.swing.JFrame jf = new javax.swing.JFrame();
        
        HeadUI bui = new HeadUI();
        jf.setSize(300,525);
        jf.getContentPane().add(bui);
        jf.show();
        bui.show();
        
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
    private com.see.truetransact.uicomponent.CButton btnSubHeadDelete;
    private com.see.truetransact.uicomponent.CButton btnSubHeadNew;
    private com.see.truetransact.uicomponent.CButton btnSubHeadSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CButton btnView1;
    private com.see.truetransact.uicomponent.CComboBox cboAccountType;
    private com.see.truetransact.uicomponent.CComboBox cboFinalActType;
    private com.see.truetransact.uicomponent.CComboBox cboSubAccountType;
    private com.see.truetransact.uicomponent.CCheckBox chkGLHeadConsolidated;
    private com.see.truetransact.uicomponent.CDialog dlgMajorHead;
    private com.see.truetransact.uicomponent.CLabel lblAccountType;
    private com.see.truetransact.uicomponent.CLabel lblFinalActType;
    private com.see.truetransact.uicomponent.CLabel lblGLHeadConsolidated;
    private com.see.truetransact.uicomponent.CLabel lblMajorHeadCode;
    private com.see.truetransact.uicomponent.CLabel lblMajorHeadDesc;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace11;
    private com.see.truetransact.uicomponent.CLabel lblSpace12;
    private com.see.truetransact.uicomponent.CLabel lblSpace13;
    private com.see.truetransact.uicomponent.CLabel lblSpace14;
    private com.see.truetransact.uicomponent.CLabel lblSpace15;
    private com.see.truetransact.uicomponent.CLabel lblSpace16;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace6;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblSubAccountType;
    private com.see.truetransact.uicomponent.CLabel lblSubHeadCode;
    private com.see.truetransact.uicomponent.CLabel lblSubHeadDesc;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panButton;
    private com.see.truetransact.uicomponent.CPanel panHead;
    private com.see.truetransact.uicomponent.CPanel panMajorHead;
    private com.see.truetransact.uicomponent.CPanel panMajorHeadCode;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panSubHead;
    private com.see.truetransact.uicomponent.CPanel panSubHeadButton;
    private com.see.truetransact.uicomponent.CPanel panTable;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private javax.swing.JSeparator sptEdit;
    private com.see.truetransact.uicomponent.CScrollPane srpMajorHead;
    private com.see.truetransact.uicomponent.CScrollPane srpSubHeadTable;
    private com.see.truetransact.uicomponent.CTable tblMajorHead;
    private com.see.truetransact.uicomponent.CTable tblSubHeadList;
    private javax.swing.JToolBar tbrHead;
    private com.see.truetransact.uicomponent.CTextField txtMajorHeadCode1;
    private com.see.truetransact.uicomponent.CTextField txtMajorHeadCode2;
    private com.see.truetransact.uicomponent.CTextField txtMajorHeadDesc;
    private com.see.truetransact.uicomponent.CTextField txtSubHeadCode;
    private com.see.truetransact.uicomponent.CTextField txtSubHeadDesc;
    // End of variables declaration//GEN-END:variables
    
}
