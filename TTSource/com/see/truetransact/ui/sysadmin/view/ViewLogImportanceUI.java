/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ViewLogImportanceUI.java
 *
 * Created on January 7, 2005, 11:57 AM
 */

package com.see.truetransact.ui.sysadmin.view;

import com.see.truetransact.clientutil.*;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uimandatory.MandatoryCheck;

import java.util.Observer;
import java.util.Observable;
import java.util.HashMap;
import java.util.Date;

import java.util.ArrayList;
/**
 *
 * @author  152713
 */
public class ViewLogImportanceUI extends com.see.truetransact.uicomponent.CInternalFrame  implements Observer, UIMandatoryField {
    HashMap mandatoryMap =  new HashMap();
    private ViewLogImportanceOB observable;
    private ViewLogImportanceRB objViewLogImportanceRB =  new ViewLogImportanceRB();
    private boolean isFilled = false;
    private boolean updateModeViewLogImportance = false;
    int modeViewLogImportance = -1;
    private String viewType = "";
    /** Creates new form ViewLogImportanceUI */
    public ViewLogImportanceUI() {
        initComponents();
        initStartUP();
    }
    
    /** Initialzation of UI */
    private void initStartUP(){
        setObservable();
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        initComponentData();
        setHelpMessage();
        setButtonEnableDisable();
        enableDisableAllFields(false);
        enableOnlyNewBtn();
        tblTable_Importance.setModel(observable.getTblViewLogImportance());
        btnEditActionPerformed(null);
        lblSpace2.setVisible(false);
        mitNew.setVisible(false);
        mitEdit.setVisible(false);
        mitDelete.setVisible(false);
        mitCancel.setVisible(false);
        btnNew.setVisible(false);
        btnEdit.setVisible(false);
        btnDelete.setVisible(false);
        btnCancel.setVisible(false);
    }
    
    /** Implementing Singleton pattern */
    private void setObservable() {
        observable = ViewLogImportanceOB.getInstance();
        observable.addObserver(this);
    }
    
    /** Auto Generated Method - setFieldNames()
     * This method assigns name for all the components.
     * Other functions are working based on this name. */
    private void setFieldNames() {
        cboActivity.setName("cboActivity");
        cboModule.setName("cboModule");
        cboScreen.setName("cboScreen");
        lblActivity.setName("lblActivity");
        lblModule.setName("lblModule");
        lblScreen.setName("lblScreen");
        panViewLogAll.setName("panViewLogAll");
        panImportanceFieldsTblBtn.setName("panImportanceFieldsTblBtn");
        panViewLogFields.setName("panViewLogFields");
        panToolBtns_LogViewImportance.setName("panToolBtns_LogViewImportance");
        panTable_Importance.setName("panTable_Importance");
        srpTable_Importance.setName("srpTable_Importance");
        tblTable_Importance.setName("tblTable_Importance");
        cboImportance.setName("cboImportance");
        lblImportance.setName("lblImportance");
        btnNew_Importance.setName("btnNew_Importance");
        btnSave_Importance.setName("btnSave_Importance");
        btnDelete_Importance.setName("btnDelete_Importance");
        lblMsg.setName("lblMsg");
        lblStatus.setName("lblStatus");
        lblSpace1.setName("lblSpace1");
    }
    
    /* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        ViewLogImportanceRB resourceBundle = new ViewLogImportanceRB();
        lblModule.setText(resourceBundle.getString("lblModule"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblScreen.setText(resourceBundle.getString("lblScreen"));
        lblActivity.setText(resourceBundle.getString("lblActivity"));
        lblImportance.setText(resourceBundle.getString("lblImportance"));
        btnNew_Importance.setText(resourceBundle.getString("btnNew_Importance"));
        btnSave_Importance.setText(resourceBundle.getString("btnSave_Importance"));
        btnDelete_Importance.setText(resourceBundle.getString("btnDelete_Importance"));
    }
    
    
    /** Auto Generated Method - setMandatoryHashMap()
     * This method list out all the Input Fields available in the UI.
     * It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboImportance", new Boolean(true));
        mandatoryMap.put("cboScreen", new Boolean(true));
        mandatoryMap.put("cboModule", new Boolean(true));
        mandatoryMap.put("cboActivity", new Boolean(true));
    }
    
    /** Auto Generated Method - getMandatoryHashMap()
     * Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    private void initComponentData() {
        cboImportance.setModel(observable.getCbmImportance());
        cboScreen.setModel(observable.getCbmScreen());
        cboModule.setModel(observable.getCbmModule());
        cboActivity.setModel(observable.getCbmActivity());
    }
    
    /* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        cboModule.setSelectedItem(observable.getCboModule());
        cboScreen.setSelectedItem(observable.getCboScreen());
        cboActivity.setSelectedItem(observable.getCboActivity());
        cboImportance.setSelectedItem(observable.getCboImportance());
        tblTable_Importance.setModel(observable.getTblViewLogImportance());
        lblStatus.setText(observable.getLblStatus());
    }
    
        /* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setModule(getModule());
        observable.setScreen(getScreen());
        observable.setCboModule(CommonUtil.convertObjToStr(cboModule.getSelectedItem()));
        observable.setCboScreen(CommonUtil.convertObjToStr(cboScreen.getSelectedItem()));
        observable.setCboActivity(CommonUtil.convertObjToStr(cboActivity.getSelectedItem()));
        observable.setCboImportance(CommonUtil.convertObjToStr(cboImportance.getSelectedItem()));
    }
    
    /** Auto Generated Method - setHelpMessage()
     * This method shows tooltip help for all the input fields
     * available in the UI. It needs the Mandatory Resource Bundle
     * object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        ViewLogImportanceMRB objMandatoryMRB = new ViewLogImportanceMRB();
        cboScreen.setHelpMessage(lblMsg, objMandatoryMRB.getString("cboScreen"));
        cboModule.setHelpMessage(lblMsg, objMandatoryMRB.getString("cboModule"));
        cboActivity.setHelpMessage(lblMsg, objMandatoryMRB.getString("cboActivity"));
        cboImportance.setHelpMessage(lblMsg, objMandatoryMRB.getString("cboImportance"));
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        tbrViewLogImportance = new javax.swing.JToolBar();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace26 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace27 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace28 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace29 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panViewLogAll = new com.see.truetransact.uicomponent.CPanel();
        panImportanceFieldsTblBtn = new com.see.truetransact.uicomponent.CPanel();
        panViewLogFields = new com.see.truetransact.uicomponent.CPanel();
        lblModule = new com.see.truetransact.uicomponent.CLabel();
        cboModule = new com.see.truetransact.uicomponent.CComboBox();
        cboScreen = new com.see.truetransact.uicomponent.CComboBox();
        lblScreen = new com.see.truetransact.uicomponent.CLabel();
        cboActivity = new com.see.truetransact.uicomponent.CComboBox();
        lblActivity = new com.see.truetransact.uicomponent.CLabel();
        lblImportance = new com.see.truetransact.uicomponent.CLabel();
        cboImportance = new com.see.truetransact.uicomponent.CComboBox();
        panToolBtns_LogViewImportance = new com.see.truetransact.uicomponent.CPanel();
        btnNew_Importance = new com.see.truetransact.uicomponent.CButton();
        btnSave_Importance = new com.see.truetransact.uicomponent.CButton();
        btnDelete_Importance = new com.see.truetransact.uicomponent.CButton();
        panTable_Importance = new com.see.truetransact.uicomponent.CPanel();
        srpTable_Importance = new com.see.truetransact.uicomponent.CScrollPane();
        tblTable_Importance = new com.see.truetransact.uicomponent.CTable();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        mbrViewLog_Importance = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptProcess = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitPrint = new javax.swing.JMenuItem();
        sptPrint = new javax.swing.JSeparator();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrViewLogImportance.add(btnNew);

        lblSpace26.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace26.setText("     ");
        lblSpace26.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrViewLogImportance.add(lblSpace26);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrViewLogImportance.add(btnEdit);

        lblSpace27.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace27.setText("     ");
        lblSpace27.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrViewLogImportance.add(lblSpace27);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrViewLogImportance.add(btnDelete);

        lblSpace2.setText("     ");
        tbrViewLogImportance.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrViewLogImportance.add(btnSave);

        lblSpace28.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace28.setText("     ");
        lblSpace28.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrViewLogImportance.add(lblSpace28);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrViewLogImportance.add(btnCancel);

        lblSpace3.setText("     ");
        tbrViewLogImportance.add(lblSpace3);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrViewLogImportance.add(btnPrint);

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrViewLogImportance.add(lblSpace29);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrViewLogImportance.add(btnClose);

        getContentPane().add(tbrViewLogImportance, java.awt.BorderLayout.NORTH);

        panViewLogAll.setMinimumSize(new java.awt.Dimension(650, 250));
        panViewLogAll.setPreferredSize(new java.awt.Dimension(650, 250));
        panViewLogAll.setLayout(new java.awt.GridBagLayout());

        panImportanceFieldsTblBtn.setMinimumSize(new java.awt.Dimension(571, 184));
        panImportanceFieldsTblBtn.setLayout(new java.awt.GridBagLayout());

        panViewLogFields.setMinimumSize(new java.awt.Dimension(190, 135));
        panViewLogFields.setPreferredSize(new java.awt.Dimension(190, 135));
        panViewLogFields.setLayout(new java.awt.GridBagLayout());

        lblModule.setText("Module");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panViewLogFields.add(lblModule, gridBagConstraints);

        cboModule.setPopupWidth(200);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panViewLogFields.add(cboModule, gridBagConstraints);

        cboScreen.setPopupWidth(200);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panViewLogFields.add(cboScreen, gridBagConstraints);

        lblScreen.setText("Screen");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panViewLogFields.add(lblScreen, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panViewLogFields.add(cboActivity, gridBagConstraints);

        lblActivity.setText("Activity");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panViewLogFields.add(lblActivity, gridBagConstraints);

        lblImportance.setText("Importance");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panViewLogFields.add(lblImportance, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panViewLogFields.add(cboImportance, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panImportanceFieldsTblBtn.add(panViewLogFields, gridBagConstraints);

        panToolBtns_LogViewImportance.setLayout(new java.awt.GridBagLayout());

        btnNew_Importance.setText("New");
        btnNew_Importance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNew_ImportanceActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panToolBtns_LogViewImportance.add(btnNew_Importance, gridBagConstraints);

        btnSave_Importance.setText("Save");
        btnSave_Importance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSave_ImportanceActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panToolBtns_LogViewImportance.add(btnSave_Importance, gridBagConstraints);

        btnDelete_Importance.setText("Delete");
        btnDelete_Importance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelete_ImportanceActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panToolBtns_LogViewImportance.add(btnDelete_Importance, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panImportanceFieldsTblBtn.add(panToolBtns_LogViewImportance, gridBagConstraints);

        panTable_Importance.setMinimumSize(new java.awt.Dimension(350, 175));
        panTable_Importance.setPreferredSize(new java.awt.Dimension(350, 175));
        panTable_Importance.setLayout(new java.awt.GridBagLayout());

        srpTable_Importance.setMinimumSize(new java.awt.Dimension(350, 175));
        srpTable_Importance.setPreferredSize(new java.awt.Dimension(350, 175));

        tblTable_Importance.setModel(new javax.swing.table.DefaultTableModel(
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
        tblTable_Importance.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblTable_ImportanceMousePressed(evt);
            }
        });
        srpTable_Importance.setViewportView(tblTable_Importance);

        panTable_Importance.add(srpTable_Importance, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panImportanceFieldsTblBtn.add(panTable_Importance, gridBagConstraints);

        panViewLogAll.add(panImportanceFieldsTblBtn, new java.awt.GridBagConstraints());

        getContentPane().add(panViewLogAll, java.awt.BorderLayout.CENTER);

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
        mnuProcess.setMinimumSize(new java.awt.Dimension(73, 19));

        mitNew.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        mitNew.setMnemonic('N');
        mitNew.setText("New");
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

        mitEdit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        mitEdit.setMnemonic('E');
        mitEdit.setText("Edit");
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        mitDelete.setMnemonic('D');
        mitDelete.setText("Delete");
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);
        mnuProcess.add(sptProcess);

        mitSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        mitSave.setMnemonic('S');
        mitSave.setText("Save");
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setMnemonic('C');
        mitCancel.setText("Cancel");
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess.add(mitCancel);
        mnuProcess.add(sptCancel);

        mitPrint.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        mitPrint.setMnemonic('P');
        mitPrint.setText("Print");
        mitPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitPrintActionPerformed(evt);
            }
        });
        mnuProcess.add(mitPrint);
        mnuProcess.add(sptPrint);

        mitClose.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        mitClose.setMnemonic('l');
        mitClose.setText("Close");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrViewLog_Importance.add(mnuProcess);

        setJMenuBar(mbrViewLog_Importance);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // TODO add your handling code here:
        btnCloseActionPerformed(null);
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void mitPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitPrintActionPerformed
        // TODO add your handling code here:
        btnPrintActionPerformed(null);
    }//GEN-LAST:event_mitPrintActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // TODO add your handling code here:
        btnCancelActionPerformed(null);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // TODO add your handling code here:
        btnSaveActionPerformed(null);
    }//GEN-LAST:event_mitSaveActionPerformed
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // TODO add your handling code here:
        btnDeleteActionPerformed(null);
    }//GEN-LAST:event_mitDeleteActionPerformed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // TODO add your handling code here:
        btnEditActionPerformed(null);
    }//GEN-LAST:event_mitEditActionPerformed
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // TODO add your handling code here:
        btnNewActionPerformed(null);
    }//GEN-LAST:event_mitNewActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        btnCancelActionPerformed(null);
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPrintActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        observable.resetForm();
        ClientUtil.enableDisable(this, false);// Disables the panel...
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        observable.setStatus();
//        setButtonEnableDisable();
        enableOnlyNewBtn();
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        updateOBFields();
        observable.doAction();
        observable.resetFields();
        ClientUtil.enableDisable(this, false);
//        setButtonEnableDisable();
        enableOnlyNewBtn();
        observable.setResultStatus();
//        observable.destroyObjects();
//        observable.createObject();
        observable.ttNotifyObservers();
        modeViewLogImportance = -1;
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        observable.createObject();
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        populate("Edit");
        enableDisableAllFields(false);
        enableOnlyNewBtn();
        modeViewLogImportance = -1;
    }//GEN-LAST:event_btnEditActionPerformed
    private void populate(String viewType){
        final HashMap hash = new HashMap();
        
        if (viewType != null) {
            if (viewType.equals("Edit") || viewType.equals("Delete")) {
                isFilled = true;
                hash.put("WHERE", "");
                observable.populateData(hash);
            }
            observable.ttNotifyObservers();
            if ((observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE)){
                ClientUtil.enableDisable(this, false);
            }else if (viewType.equals("Edit")){
                enableOnlyNewBtn();
                ClientUtil.enableDisable(this, true);
            }
            observable.setStatus();
        }
    }
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        ClientUtil.enableDisable(this, true);// Enables the panel...
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.setStatus();
//        setButtonEnableDisable();
        enableOnlyNewBtn();
        enableDisableAllFields(false);
    }//GEN-LAST:event_btnNewActionPerformed
    
    private void tblTable_ImportanceMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTable_ImportanceMousePressed
        // TODO add your handling code here:
        if (tblTable_Importance.getSelectedRow() >= 0){
            updateOBFields();
            observable.populateViewLogImportanceDetails(tblTable_Importance.getSelectedRow());
            if ((observable.getLblStatus().equals(ClientConstants.ACTION_STATUS[3]))){
                enableDisableAllFields(false);
                enableDisableToolBtns(false);
            }else{
                enableDisableAllFields(true);
                enableDisableToolBtns(true);
                updateModeViewLogImportance = true;
                modeViewLogImportance = tblTable_Importance.getSelectedRow();
            }
        }
        observable.ttNotifyObservers();
    }//GEN-LAST:event_tblTable_ImportanceMousePressed
    
    private void btnDelete_ImportanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete_ImportanceActionPerformed
        // TODO add your handling code here:
        updateOBFields();
        observable.deleteViewLogImportanceTabRecord(modeViewLogImportance);
        observable.resetFields();
        enableDisableAllFields(false);
        enableOnlyNewBtn();
        observable.ttNotifyObservers();
        modeViewLogImportance = -1;
        updateModeViewLogImportance = false;
    }//GEN-LAST:event_btnDelete_ImportanceActionPerformed
    
    private void btnSave_ImportanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSave_ImportanceActionPerformed
        // TODO add your handling code here:
        final String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panViewLogFields);
        /* mandatoryMessage length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
        if (mandatoryMessage.length() > 0){
            displayAlert(mandatoryMessage);
        }else{
            updateOBFields();
            if (observable.addViewLogImportanceDetails(modeViewLogImportance, updateModeViewLogImportance) == 1){
                enableDisableAllFields(true);
            }else{
                observable.resetFields();
                enableDisableAllFields(false);
                enableOnlyNewBtn();
            }
            observable.ttNotifyObservers();
        }
        
    }//GEN-LAST:event_btnSave_ImportanceActionPerformed
    
    private void btnNew_ImportanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNew_ImportanceActionPerformed
        // TODO add your handling code here:
        updateOBFields();
        enableDisableAllFields(true);
        enableOnlyNewSaveBtns();
        observable.resetFields();
        observable.ttNotifyObservers();
        modeViewLogImportance = -1;
        updateModeViewLogImportance = false;
    }//GEN-LAST:event_btnNew_ImportanceActionPerformed
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm
    private void popUp(String field) {
        final HashMap viewMap = new HashMap();
        viewType = field;
        lblStatus.setText(ClientConstants.ACTION_STATUS[0]);
        //if ( observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ||  observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE){
        if(field.equals("Edit") || field.equals("Delete")) {
            viewMap.put("MAPNAME", "");
        }
        new ViewAll(this, viewMap).show();
    }
    
    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
        
        if (viewType != null) {
            if (viewType.equals("Edit") || viewType.equals("Delete")) {
                isFilled = true;
                hash.put("WHERE", "");
                observable.populateData(hash);
            }
            observable.ttNotifyObservers();
            if ((observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE)){
                ClientUtil.enableDisable(this, false);
            }else if (viewType.equals("Edit")){
                enableOnlyNewBtn();
                ClientUtil.enableDisable(this, true);
            }
//            setButtonEnableDisable();
            observable.setStatus();
        }
    }
    
    private void displayAlert(String message){
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    private void enableDisableAllFields(boolean val){
        cboActivity.setEnabled(val);
        cboImportance.setEnabled(val);
        cboModule.setEnabled(val);
        cboScreen.setEnabled(val);
    }
    
    private void enableDisableToolBtns(boolean val){
        btnNew_Importance.setEnabled(val);
        btnSave_Importance.setEnabled(val);
        btnDelete_Importance.setEnabled(val);
    }
    
    private void enableOnlyNewBtn(){
        btnNew_Importance.setEnabled(true);
        btnSave_Importance.setEnabled(false);
        btnDelete_Importance.setEnabled(false);
    }
    
    private void enableOnlyNewSaveBtns(){
        btnNew_Importance.setEnabled(true);
        btnSave_Importance.setEnabled(true);
        btnDelete_Importance.setEnabled(false);
    }
    
    private void setButtonEnableDisable() {
        btnNew.setEnabled(false);
        btnEdit.setEnabled(!btnEdit.isEnabled());
        btnDelete.setEnabled(false);
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());
        
        btnSave.setEnabled(!btnNew.isEnabled());
        btnCancel.setEnabled(false);
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new ViewLogImportanceUI().show();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDelete_Importance;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnNew_Importance;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnSave_Importance;
    private com.see.truetransact.uicomponent.CComboBox cboActivity;
    private com.see.truetransact.uicomponent.CComboBox cboImportance;
    private com.see.truetransact.uicomponent.CComboBox cboModule;
    private com.see.truetransact.uicomponent.CComboBox cboScreen;
    private com.see.truetransact.uicomponent.CLabel lblActivity;
    private com.see.truetransact.uicomponent.CLabel lblImportance;
    private com.see.truetransact.uicomponent.CLabel lblModule;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblScreen;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace26;
    private com.see.truetransact.uicomponent.CLabel lblSpace27;
    private com.see.truetransact.uicomponent.CLabel lblSpace28;
    private com.see.truetransact.uicomponent.CLabel lblSpace29;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CMenuBar mbrViewLog_Importance;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panImportanceFieldsTblBtn;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTable_Importance;
    private com.see.truetransact.uicomponent.CPanel panToolBtns_LogViewImportance;
    private com.see.truetransact.uicomponent.CPanel panViewLogAll;
    private com.see.truetransact.uicomponent.CPanel panViewLogFields;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptPrint;
    private javax.swing.JSeparator sptProcess;
    private com.see.truetransact.uicomponent.CScrollPane srpTable_Importance;
    private com.see.truetransact.uicomponent.CTable tblTable_Importance;
    private javax.swing.JToolBar tbrViewLogImportance;
    // End of variables declaration//GEN-END:variables
    
}
