/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LookupMasterUI.java
 *
 * Created on February 27, 2004, 11:23 AM
 */

package com.see.truetransact.ui.sysadmin.lookup;

import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.EnhancedTableModel;

import javax.swing.table.DefaultTableModel;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.clientproxy.ProxyParameters;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.ArrayList;
import java.util.Date;
/**
 *
 * @author  Lohith R.
 */
public class LookupMasterUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer,UIMandatoryField{
    private HashMap mandatoryMap;
    private LookupMasterOB observable;
    //    LookupMasterRB resourceBundle = new LookupMasterRB();
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.sysadmin.lookup.LookupMasterRB", ProxyParameters.LANGUAGE);
    
    int result;
    private boolean selectedTableYesNo = false;
    private int isEditable = -1;
    private final String NO = "N";
    int viewType = -1;
    final int EDIT=0, AUTHORIZE=1, VIEW =3;
    boolean isFilled = false;
    private Date currDt = null;
    /** Creates new form LookupMasterUI */
    public LookupMasterUI() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        initStartUp();
    }
    
    /** Initialzation of UI */
    private void initStartUp(){
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setHelpMessage();
        setMaximumLength();
        setObservable();
        setButtonEnableDisable();
        setButtonLookupMasterTab(false);
        setFieldInvisible();
        resetTable();
        observable.resetStatus();
        observable.setTxtLookupID("");
        observable.resetFields();
        setLookupIdDisable();
        ClientUtil.enableDisable(this, false);
    }
    
    private void setObservable() {
        /* Implementing Singleton pattern */
        observable = LookupMasterOB.getInstance();
        observable.addObserver(this);
    }
    private void setFieldNames() {
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnEdit.setName("btnEdit");
        btnLookupMasterTabAdd.setName("btnLookupMasterTabAdd");
        btnLookupMasterTabDelete.setName("btnLookupMasterTabDelete");
        btnLookupMasterTabNew.setName("btnLookupMasterTabNew");
        btnPrint.setName("btnPrint");
        btnSave.setName("btnSave");
        lblLookupDesc.setName("lblLookupDesc");
        lblLookupID.setName("lblLookupID");
        lblLookupRef.setName("lblLookupRef");
        lblMsg.setName("lblMsg");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblStatus.setName("lblStatus");
        mbrMain.setName("mbrMain");
        panButton.setName("panButton");
        panLookupMaster.setName("panLookupMaster");
        panMain.setName("panMain");
        panStatus.setName("panStatus");
        panTable.setName("panTable");
        panText.setName("panText");
        sptDescription.setName("sptDescription");
        srpTable.setName("srpTable");
        tblLookupMaster.setName("tblLookupMaster");
        txtLookupDesc.setName("txtLookupDesc");
        txtLookupID.setName("txtLookupID");
        txtLookupRef.setName("txtLookupRef");
    }
    
    private void internationalize() {
        btnClose.setText(resourceBundle.getString("btnClose"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        ((javax.swing.border.TitledBorder)panMain.getBorder()).setTitle(resourceBundle.getString("panMain"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblLookupID.setText(resourceBundle.getString("lblLookupID"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        btnLookupMasterTabDelete.setText(resourceBundle.getString("btnLookupMasterTabDelete"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        btnLookupMasterTabAdd.setText(resourceBundle.getString("btnLookupMasterTabAdd"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblLookupRef.setText(resourceBundle.getString("lblLookupRef"));
        btnLookupMasterTabNew.setText(resourceBundle.getString("btnLookupMasterTabNew"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        lblLookupDesc.setText(resourceBundle.getString("lblLookupDesc"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
    }
    
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtLookupRef", new Boolean(true));
        mandatoryMap.put("txtLookupDesc", new Boolean(true));
        mandatoryMap.put("txtLookupID", new Boolean(true));
    }
    
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    public void update(Observable observed, Object arg) {
        txtLookupRef.setText(observable.getTxtLookupRef());
        txtLookupDesc.setText(observable.getTxtLookupDesc());
        txtLookupID.setText(observable.getTxtLookupID());
        tblLookupMaster.setModel(observable.getTblLookupMaster());
        tblLookupMaster.revalidate();
        lblStatus.setText(observable.getLblStatus());
    }
    
    public void updateOBFields() {
        observable.setTxtLookupRef(txtLookupRef.getText());
        observable.setTxtLookupDesc(txtLookupDesc.getText());
        observable.setTxtLookupID(txtLookupID.getText());
        observable.setTblLookupMaster((com.see.truetransact.clientutil.EnhancedTableModel)tblLookupMaster.getModel());
    }
    
    public void setHelpMessage() {
        LookupMasterMRB objMandatoryRB = new LookupMasterMRB();
        txtLookupRef.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLookupRef"));
        txtLookupDesc.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLookupDesc"));
        txtLookupID.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLookupID"));
    }
    
    private void setMaximumLength(){
        txtLookupID.setMaxLength(64);
        txtLookupRef.setMaxLength(64);
        txtLookupDesc.setMaxLength(128);
        //Added By Suresh
        txtLookupRef.setAllowAll(true);
    }
    
    private void setFieldInvisible(){
        btnException.setVisible(false);
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        tbrMain = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace21 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace22 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace23 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace24 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panMain = new com.see.truetransact.uicomponent.CPanel();
        panTable = new com.see.truetransact.uicomponent.CPanel();
        srpTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblLookupMaster = new com.see.truetransact.uicomponent.CTable();
        panText = new com.see.truetransact.uicomponent.CPanel();
        panLookupMaster = new com.see.truetransact.uicomponent.CPanel();
        lblLookupID = new com.see.truetransact.uicomponent.CLabel();
        lblLookupRef = new com.see.truetransact.uicomponent.CLabel();
        lblLookupDesc = new com.see.truetransact.uicomponent.CLabel();
        txtLookupRef = new com.see.truetransact.uicomponent.CTextField();
        txtLookupDesc = new com.see.truetransact.uicomponent.CTextField();
        txtLookupID = new com.see.truetransact.uicomponent.CTextField();
        sptDescription = new com.see.truetransact.uicomponent.CSeparator();
        panButton = new com.see.truetransact.uicomponent.CPanel();
        btnLookupMasterTabNew = new com.see.truetransact.uicomponent.CButton();
        btnLookupMasterTabAdd = new com.see.truetransact.uicomponent.CButton();
        btnLookupMasterTabDelete = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        mbrMain = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitEdit = new javax.swing.JMenuItem();
        sptEdit = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Lookup Master");
        setMinimumSize(new java.awt.Dimension(650, 400));
        setPreferredSize(new java.awt.Dimension(650, 400));

        tbrMain.setEnabled(false);
        tbrMain.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 11)); // NOI18N
        tbrMain.setMinimumSize(new java.awt.Dimension(28, 28));
        tbrMain.setPreferredSize(new java.awt.Dimension(28, 28));

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
        tbrMain.add(btnView);

        lblSpace5.setText("     ");
        tbrMain.add(lblSpace5);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrMain.add(btnEdit);

        lblSpace1.setText("     ");
        tbrMain.add(lblSpace1);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrMain.add(btnSave);

        lblSpace21.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace21.setText("     ");
        lblSpace21.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrMain.add(lblSpace21);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrMain.add(btnCancel);

        lblSpace2.setText("     ");
        tbrMain.add(lblSpace2);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrMain.add(btnAuthorize);

        lblSpace22.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace22.setText("     ");
        lblSpace22.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrMain.add(lblSpace22);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrMain.add(btnException);

        lblSpace23.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace23.setText("     ");
        lblSpace23.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace23.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace23.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrMain.add(lblSpace23);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrMain.add(btnReject);

        lblSpace4.setText("     ");
        tbrMain.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrMain.add(btnPrint);

        lblSpace24.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace24.setText("     ");
        lblSpace24.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrMain.add(lblSpace24);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrMain.add(btnClose);

        getContentPane().add(tbrMain, java.awt.BorderLayout.NORTH);

        panMain.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panMain.setLayout(new java.awt.GridBagLayout());

        panTable.setLayout(new java.awt.GridBagLayout());

        srpTable.setMinimumSize(new java.awt.Dimension(230, 130));
        srpTable.setPreferredSize(new java.awt.Dimension(230, 130));

        tblLookupMaster.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblLookupMaster.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblLookupMasterMouseClicked(evt);
            }
        });
        srpTable.setViewportView(tblLookupMaster);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panTable.add(srpTable, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panMain.add(panTable, gridBagConstraints);

        panText.setLayout(new java.awt.GridBagLayout());

        panLookupMaster.setLayout(new java.awt.GridBagLayout());

        lblLookupID.setText("Lookup ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLookupMaster.add(lblLookupID, gridBagConstraints);

        lblLookupRef.setText("Lookup Ref ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLookupMaster.add(lblLookupRef, gridBagConstraints);

        lblLookupDesc.setText("Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLookupMaster.add(lblLookupDesc, gridBagConstraints);

        txtLookupRef.setMinimumSize(new java.awt.Dimension(150, 21));
        txtLookupRef.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLookupMaster.add(txtLookupRef, gridBagConstraints);

        txtLookupDesc.setMinimumSize(new java.awt.Dimension(150, 21));
        txtLookupDesc.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLookupMaster.add(txtLookupDesc, gridBagConstraints);

        txtLookupID.setMinimumSize(new java.awt.Dimension(150, 21));
        txtLookupID.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLookupMaster.add(txtLookupID, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLookupMaster.add(sptDescription, gridBagConstraints);

        panButton.setLayout(new java.awt.GridBagLayout());

        btnLookupMasterTabNew.setText("New");
        btnLookupMasterTabNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLookupMasterTabNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton.add(btnLookupMasterTabNew, gridBagConstraints);

        btnLookupMasterTabAdd.setText("Save");
        btnLookupMasterTabAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLookupMasterTabAddActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton.add(btnLookupMasterTabAdd, gridBagConstraints);

        btnLookupMasterTabDelete.setText("Delete");
        btnLookupMasterTabDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLookupMasterTabDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton.add(btnLookupMasterTabDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLookupMaster.add(panButton, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panText.add(panLookupMaster, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMain.add(panText, gridBagConstraints);

        getContentPane().add(panMain, java.awt.BorderLayout.CENTER);

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace3.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace3, gridBagConstraints);

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

        mitEdit.setText("Edit");
        mitEdit.setEnabled(false);
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);
        mnuProcess.add(sptEdit);

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

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        popUpItems(VIEW);
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    //    public void authorizeStatus(String authorizeStatus) {
    //        if (viewType == AUTHORIZE && isFilled){
    //            HashMap singleAuthorizeMap = new HashMap();
    //            singleAuthorizeMap.put("STATUS", authorizeStatus);
    //            singleAuthorizeMap.put("USER_ID", TrueTransactMain.USER_ID);
    //            singleAuthorizeMap.put("AUTHORIZEDT", currDt.clone());
    //            singleAuthorizeMap.put("LOOKUP ID", CommonUtil.convertObjToStr(txtLookupID.getText()));
    //            ClientUtil.execute("authorizeLookUpData", singleAuthorizeMap);
    //
    //            btnSave.setEnabled(true);
    //            btnCancelActionPerformed(null);
    //        } else{
    //            viewType = AUTHORIZE;
    //            HashMap mapParam = new HashMap();
    //
    //            //__ To Save the data in the Internal Frame...
    //            setModified(true);
    //
    //            mapParam.put(CommonConstants.MAP_NAME, "getLookUpAuthorizeList");
    //            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeLookUpData");
    //
    //            isFilled = false;
    //            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
    //            authorizeUI.show();
    //
    //            lblStatus.setText(ClientConstants.RESULT_STATUS[authorizeUI.getResultStatus()]);
    //            btnSave.setEnabled(false);
    //        }
    //    }
    
    public void authorizeStatus(String authorizeStatus) {
        if (viewType == AUTHORIZE && isFilled) {
            HashMap singleAuthorizeMap = new HashMap();
            ArrayList arrList = new ArrayList();
            HashMap authDataMap = new HashMap();
            
            if(CommonUtil.convertObjToStr(txtLookupRef.getText()).equalsIgnoreCase("")){
                displayAlert(resourceBundle.getString("REFIDWARNING"));
                
            }else{
                authDataMap.put("LOOKUP ID", CommonUtil.convertObjToStr(txtLookupID.getText()));
                authDataMap.put("LOOKUP REFERENCE ID", CommonUtil.convertObjToStr(txtLookupRef.getText()));
                authDataMap.put("USER_ID", TrueTransactMain.USER_ID);
                authDataMap.put("AUTHORIZEDT", currDt.clone());
                
                arrList.add(authDataMap);
                
                singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
                singleAuthorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
                authorize(singleAuthorizeMap);
                isFilled = false;
            }
            
        } else {
            HashMap mapParam = new HashMap();
            
            //__ To Save the data in the Internal Frame...
            setModified(true);
            
            mapParam.put(CommonConstants.MAP_NAME, "getLookUpAuthorizeList");
            viewType = AUTHORIZE;
            isFilled = false;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.setException(false);
            authorizeUI.show();
            btnSave.setEnabled(false);
        }
    }
    
    public void authorize(HashMap map) {
        System.out.println("Authorize Map : " + map);
        ///observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        observable.setAuthorizeMap(map);
        observable.doAction();
        if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED) {
            btnSave.setEnabled(true);
            btnCancelActionPerformed(null);
            observable.setResultStatus();
        }
    }
    private void btnLookupMasterTabDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLookupMasterTabDeleteActionPerformed
        // Add your handling code here:
        updateOBFields();
        observable.deleteLookupMasterTab();
        setButtonLookupMasterTab(false);
        btnLookupMasterTabNew.setEnabled(true);
        //        btnLookupMasterTabAdd.setEnabled(btnEdit.isEnabled());
        //        btnLookupMasterTabDelete.setEnabled(btnEdit.isEnabled());
        observable.resetFields();
        ClientUtil.enableDisable(this, false);
    }//GEN-LAST:event_btnLookupMasterTabDeleteActionPerformed
    
    private void tblLookupMasterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblLookupMasterMouseClicked
        // Add your handling code here:
        selectedTableYesNo = true;
        observable.resetFields();
        updateOBFields();
        observable.populateLookupMasterTab(tblLookupMaster.getSelectedRow());
        
        //__ If Delete, No Editing of the Code is Allowed...
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
        || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
        || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT
        || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION 
        || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW ){
            SetFieldsEnable(false);
            ClientUtil.enableDisable(this.panText, false);
            setButtonLookupMasterTab(false);
            
        }else{
            //if(isEditable == 1){ //Changed By Kannan AR modification allowed based on editable column in LOOKUP_MASTER_DESC table
            if(observable.isEditable()){
                SetFieldsEnable(true);
                ClientUtil.enableDisable(this.panText, true);
                setLookupIdDisable();
                txtLookupRef.setEnabled(false);                
                setButtonLookupMasterTab(true);
                btnLookupMasterTabNew.setEnabled(false);
            }else{
                SetFieldsEnable(false);
                ClientUtil.enableDisable(this.panText, false);
                setButtonLookupMasterTab(false);
            }            
            //setButtonLookupMasterTab(true); //Commented By Kannan AR
        }
    }//GEN-LAST:event_tblLookupMasterMouseClicked
    
    private void btnLookupMasterTabAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLookupMasterTabAddActionPerformed
        // Add your handling code here:
        final String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panMain);
        /* mandatoryMessage length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
        if (mandatoryMessage.length() > 0){
            displayAlert(mandatoryMessage);
        }else{
            addCTable();
        }
    }//GEN-LAST:event_btnLookupMasterTabAddActionPerformed
    
    private void displayAlert(String message){
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    private void btnCheck(){
         btnCancel.setEnabled(true);
         btnSave.setEnabled(false);
         btnAuthorize.setEnabled(false);
         btnReject.setEnabled(false);
         btnException.setEnabled(false);
         btnEdit.setEnabled(false);
     }
    
    public void addCTable(){
        if(selectedTableYesNo == true){
            /** when clicked on the row of CTable **/
            observable.setTxtLookupDesc(txtLookupDesc.getText());
            observable.setTableValueAt();
            observable.resetFields();
            ClientUtil.enableDisable(panText,false);
            setButtonLookupMasterTab(false);
            btnLookupMasterTabNew.setEnabled(true);
        }else{
            /** when clicked on the new button of CTable **/
            result=0;
            updateOBFields();
            result = observable.addLookupMasterTab();
            if(result!=1){
                ClientUtil.enableDisable(panText,false);
                setButtonLookupMasterTab(false);
                btnLookupMasterTabNew.setEnabled(true);
                


            }else if (result == 1){
                /** The action taken for the Cancel option **/
                ClientUtil.enableDisable(panText,true);
                txtLookupID.setEnabled(false);
                txtLookupID.setEditable(false);
                setButtonLookupMasterTab(true);
                btnLookupMasterTabNew.setEnabled(false);
                
            }
        }
        //        ClientUtil.enableDisable(panText,false);
        //            setButtonLookupMasterTab(false);
        //            btnLookupMasterTabAdd.setEnabled(btnEdit.isEnabled());
        
        //        btnLookupMasterTabDelete.setEnabled(btnEdit.isEnabled());
    }
    
    private void btnLookupMasterTabNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLookupMasterTabNewActionPerformed
        // Add your handling code here:
        selectedTableYesNo = false;
        updateOBFields();
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.resetFields();
        setButtonLookupMasterTab(true);
        //        btnLookupMasterTabDelete.setEnabled(btnEdit.isEnabled());
        btnLookupMasterTabNew.setEnabled(false);
        ClientUtil.enableDisable(this, true);
        //        txtLookupID.setEditable(btnEdit.isEnabled());
        //        txtLookupID.setEnabled(btnEdit.isEnabled());
        //        txtLookupRef.setEditable(!btnEdit.isEnabled());
        //        txtLookupRef.setEnabled(!btnEdit.isEnabled());
        setLookupIdDisable();
        SetFieldsEnable(true);
    }//GEN-LAST:event_btnLookupMasterTabNewActionPerformed
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        observable.resetStatus();
        ClientUtil.enableDisable(this, false);
        observable.resetFields();
//        setButtonEnableDisable();
        resetTable();
        //__ To reset the Value of the Editable Variable...
        isEditable = -1;
        cifClosingAlert();
        setlookupID();
        //        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // Add your handling code here:
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        super.removeEditLock(txtLookupID.getText());
        observable.resetStatus();
        observable.setAuthorizeMap(null);
        ClientUtil.enableDisable(this, false);
        observable.resetFields();
        observable.setTxtLookupID("");
        setButtonEnableDisable();
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        setButtonLookupMasterTab(false);
        resetTable();
        //__ To reset the Value of the Editable Variable...
        isEditable = -1;
        
        setlookupID();
        
        isFilled = false;
        setModified(false);
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // Add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        updateOBFields();
        observable.doSave();
        super.removeEditLock(txtLookupID.getText());
        observable.setResultStatus();
        ClientUtil.enableDisable(this, false);
        observable.setResult(ClientConstants.ACTIONTYPE_EDIT);
        observable.setResultStatus();
        lblStatus.setText(observable.getLblStatus());
       
        observable.resetFields();
        resetTable();
        observable.setTxtLookupID("");
        //__ To reset the Value of the Editable Variable...
        isEditable = -1;
        
        setButtonEnableDisable();
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        setButtonLookupMasterTab(false);
        setlookupID();
        
        isFilled = false;
        setModified(false);
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // Add your handling code here:
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        //__ To reset the Value of the Editable Variable...
        isEditable = -1;
        
        popUpItems(EDIT);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        observable.existingData();
        btnLookupMasterTabNew.setEnabled(true);
        
        
//        //__ To Save the data in the Internal Frame...
//        setModified(true);
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void popUpItems(int field) {
        viewType = field;
        final HashMap viewMap = new HashMap();
        ArrayList lst = new ArrayList();
        lst.add("LOOKUP ID");
        viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
        lst = null;
        viewMap.put(CommonConstants.MAP_NAME, "ViewAllLookupMasterTO");
        new ViewAll(this, viewMap).show();
    }
    
    /** Called Automatically when viewAll() is Called...
     * @param param The selected data from the viewAll() is passed as a param
     */
    public void fillData(Object param) {
        //__ To Save the data in the Internal Frame...
        setModified(true);
        
        observable.setTxtLookupID(txtLookupID.getText());
        final HashMap hash = (HashMap) param;
        HashMap hashMap = new HashMap();
        hashMap.put(CommonConstants.MAP_NAME, "ViewLookupMaster");
        hashMap.put(CommonConstants.MAP_WHERE, hash.get("LOOKUP ID"));
        System.out.println("Editable: " + CommonUtil.convertObjToStr(hash.get("IS EDITABLE")));
        
        //        if(CommonUtil.convertObjToStr(hash.get("IS EDITABLE")).equalsIgnoreCase(NO)){
        //            isEditable = -1;
        //        }else{
        //            isEditable = 1;
        //            btnLookupMasterTabNew.setEnabled(true);
        //        }
        
        observable.populateData(hashMap);
        isFilled = true;
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        setButtonEnableDisable();
        //        btnLookupMasterTabNew.setEnabled(!btnEdit.isEnabled());
        
        if(viewType == AUTHORIZE || viewType == VIEW){
            setButtonLookupMasterTab(false);
            btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
            btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
            btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
        }
//        btnCancel.setEnabled(true);
    }
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm
    
    private void resetTable() {
        observable.removeLookupMasterRow();
    }
    
    private void setlookupID(){
        txtLookupID.setText("");
        observable.setTxtLookupID("");
    }
    
    // To Enable or Disable the fields of the Table in Lookup Master...
    private void setButtonEnableDisable() {
        btnEdit.setEnabled(!btnEdit.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        btnSave.setEnabled(!btnEdit.isEnabled());
        btnCancel.setEnabled(!btnEdit.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        lblStatus.setText(observable.getLblStatus());
        btnView.setEnabled(!btnView.isEnabled());
    }
    
    private void setButtonLookupMasterTab(boolean value){
        //        btnLookupMasterTabNew.setEnabled(!btnEdit.isEnabled());
        //        btnLookupMasterTabAdd.setEnabled(!btnEdit.isEnabled());
        //        btnLookupMasterTabDelete.setEnabled(!btnEdit.isEnabled());
        
        btnLookupMasterTabNew.setEnabled(value);
        btnLookupMasterTabAdd.setEnabled(value);
        btnLookupMasterTabDelete.setEnabled(value);
        
    }
    
    private void SetFieldsEnable(boolean value){
        //        txtLookupID.setEnabled(value);
        //        txtLookupID.setEditable(value);
        txtLookupRef.setEnabled(value);
        txtLookupRef.setEditable(value);
        txtLookupDesc.setEnabled(value);
        txtLookupDesc.setEditable(value);
    }
    
    private void setLookupIdDisable(){
        txtLookupID.setEnabled(false);
        txtLookupID.setEditable(false);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnLookupMasterTabAdd;
    private com.see.truetransact.uicomponent.CButton btnLookupMasterTabDelete;
    private com.see.truetransact.uicomponent.CButton btnLookupMasterTabNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CLabel lblLookupDesc;
    private com.see.truetransact.uicomponent.CLabel lblLookupID;
    private com.see.truetransact.uicomponent.CLabel lblLookupRef;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace21;
    private com.see.truetransact.uicomponent.CLabel lblSpace22;
    private com.see.truetransact.uicomponent.CLabel lblSpace23;
    private com.see.truetransact.uicomponent.CLabel lblSpace24;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panButton;
    private com.see.truetransact.uicomponent.CPanel panLookupMaster;
    private com.see.truetransact.uicomponent.CPanel panMain;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTable;
    private com.see.truetransact.uicomponent.CPanel panText;
    private javax.swing.JSeparator sptCancel;
    private com.see.truetransact.uicomponent.CSeparator sptDescription;
    private javax.swing.JSeparator sptEdit;
    private com.see.truetransact.uicomponent.CScrollPane srpTable;
    private com.see.truetransact.uicomponent.CTable tblLookupMaster;
    private javax.swing.JToolBar tbrMain;
    private com.see.truetransact.uicomponent.CTextField txtLookupDesc;
    private com.see.truetransact.uicomponent.CTextField txtLookupID;
    private com.see.truetransact.uicomponent.CTextField txtLookupRef;
    // End of variables declaration//GEN-END:variables
}