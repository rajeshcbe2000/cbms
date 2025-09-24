/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * AcNoMaintenanceUI.java
 *
 * Created on February 18, 2009, 01:40 PM
 *
 * AUTHOR : RAJESH.S
 */

package com.see.truetransact.ui.sysadmin.branchacnomaintenance;

import com.see.truetransact.ui.sysadmin.branchacnomaintenance.AcNoMaintenanceRB;
import com.see.truetransact.uicomponent.CButtonGroup;// To add and Remove the Radio Buttons...
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.commonutil.CommonConstants;
import javax.swing.table.DefaultTableModel;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.clientproxy.ProxyParameters;

import java.util.ArrayList;
import java.util.Observable;
import java.util.HashMap;
import java.util.Observer;
//import java.util.GregorianCalendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author   Lohith R.
 */

public class AcNoMaintenanceUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer, UIMandatoryField{
    private String holidayId = "" ;
    private HashMap mandatoryMap;
    private AcNoMaintenanceOB observable;
    //    private AcNoMaintenanceRB objAcNoMaintenanceRB;
    
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.sysadmin.branchacnomaintenance.AcNoMaintenanceRB", ProxyParameters.LANGUAGE);
    
    private final int EDIT=0,DELETE=1;
    final int AUTHORIZE=3, VIEW =4;
    int viewType=-1;
    private int intYear;
    private int leapYear;
    private int ACTION=-1;
    private int month = 0;
    private int tableRow = 0;
    private int enableButton = 0;
    private int getEditperformed = 0;
    private int dateValue;
    private Integer integerYear;
    private Integer integerSplit;
    private ArrayList tableData;
    private ArrayList arrayListTableDate;
    private String columnDate;
    private StringBuffer compareDate;
    private final String dateSeparator = "/";
    private boolean dataExist;
    private Date currentYear;
    public String stringMonthData;
    public String stringYearData;
    private boolean uniqueCombo = false;
    boolean isFilled = false;
    Date curDate = null;
    boolean branchDataChanged = false;
    /** Creates new form AcNoMaintenance */
    public AcNoMaintenanceUI() {
        curDate = ClientUtil.getCurrentDate();
        initComponents();
        initStartUp();
    }
    
    /** Initialzation of UI */
    private void initStartUp(){
        setFieldNames();
        internationalize();
//        setMandatoryHashMap();
        setObservable();
        initComponentData();
        setMaximumLength();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panLeft);
        setHelpMessage();
        observable.resetStatus();
        observable.resetForm();
        ClientUtil.enableDisable(this, false);
        setButtonEnableDisable();
        btnDetNew.setEnabled(true);
        cboBranches.setEnabled(true);
        btnNew.setVisible(false);
        btnEdit.setVisible(false);
        btnView.setVisible(false);
        btnAuthorize.setVisible(false);
        btnReject.setVisible(false);
        btnException.setVisible(false);
        btnDelete.setVisible(false);
        lblGroupNo.setVisible(false);
        cboGroupNo.setVisible(false);// Added by nithya on 28-10-2017 for group deposit act no creation
    }
    
    private void setObservable() {
        /* Implementing Singleton pattern */
        observable = AcNoMaintenanceOB.getInstance();
        observable.addObserver(this);
    }
    
    /** Auto Generated Method - setFieldNames()
     * This method assigns name for all the components.
     * Other functions are working based on this name. */
    private void setFieldNames() {
        lblBranches.setName("lblBranches");
        lblProdId.setName("lblProdId");
        lblLastAcNo.setName("lblLastAcNo");
        lblNextAcNo.setName("lblNextAcNo");
        cboBranches.setName("cboBranches");
        cboProdId.setName("cboProdId");
        txtLastAcNo.setName("txtLastAcNo");
        lblLastAcNoDisplay.setName("lblLastAcNoDisplay");
        txtNextAcNo.setName("txtNextAcNo");
        lblNextAcNoDisplay.setName("lblNextAcNoDisplay");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnSave.setName("btnSave");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblStatus.setName("lblStatus");
        mbrMain.setName("mbrMain");
        panLeft.setName("panLeft");
        panMain.setName("panMain");
        panStatus.setName("panStatus");
        tblAcNoMaintenance.setName("tblAcNoMaintenance");
    }
    
    
    /** Auto Generated Method - internationalize()
     * This method used to assign display texts from
     * the Resource Bundle File. */
    private void internationalize() {
        //        AcNoMaintenanceRB resourceBundle = new AcNoMaintenanceRB();
        lblBranches.setText(resourceBundle.getString("lblBranches"));
        lblProdId.setText(resourceBundle.getString("lblProdId"));
        lblLastAcNo.setText(resourceBundle.getString("lblLastAcNo"));
        lblNextAcNo.setText(resourceBundle.getString("lblNextAcNo"));
        lblLastAcNoDisplay.setText(resourceBundle.getString("lblLastAcNoDisplay"));
        lblNextAcNoDisplay.setText(resourceBundle.getString("lblNextAcNoDisplay"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        ((javax.swing.border.TitledBorder)panLeft.getBorder()).setTitle(resourceBundle.getString("panLeft"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
    }
    
    /** Auto Generated Method - setMandatoryHashMap()
     * This method list out all the Input Fields available in the UI.
     * It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("rdoWeeklyOff_Yes", new Boolean(true));
        mandatoryMap.put("cboWeeklyOff1", new Boolean(false));
        mandatoryMap.put("cboWeeklyOff2", new Boolean(false));
        mandatoryMap.put("cboHalfDay1", new Boolean(false));
        mandatoryMap.put("cboHalfDay2", new Boolean(false));
        mandatoryMap.put("cboMonth", new Boolean(true));
        mandatoryMap.put("cboDate", new Boolean(true));
        mandatoryMap.put("txtHolidayName", new Boolean(true));
        mandatoryMap.put("txtRemarks", new Boolean(true));
        mandatoryMap.put("txtYear", new Boolean(true));
    }
    
    /** Auto Generated Method - getMandatoryHashMap()
     * Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    /** Auto Generated Method - update()
     * This method called by Observable. It updates the UI with
     * Observable's data. If needed add/Remove RadioButtons
     * method need to be added.*/
    public void update(Observable observed, Object arg) {
//        cboBranches.setSelectedItem(observable.getCboBranches());
//        cboProdId.setSelectedItem(observable.getCboProdId());
        txtLastAcNo.setText(observable.getTxtLastAcNo());
//        lblLastAcNoDisplay.setName("lblLastAcNoDisplay");
        txtNextAcNo.setText(observable.getTxtNextAcNo());
//        lblNextAcNoDisplay.setName("lblNextAcNoDisplay");
        tblAcNoMaintenance.setModel(observable.getTblAcNoMaintenanceTab());
        lblStatus.setText(observable.getLblStatus());
    }
    
    
    private void initComponentData() {
        cboBranches.setModel(observable.getCbmBranches());
        cboProdId.setModel(observable.getCbmProdId());        
        tblAcNoMaintenance.setModel(observable.getTblAcNoMaintenanceTab());
    }
    
    private void setMaximumLength(){
        txtLastAcNo.setMaxLength(10);
        txtLastAcNo.setAllowAll(true);
        txtNextAcNo.setMaxLength(10);
        txtNextAcNo.setAllowAll(true);
    }
    
    /** Auto Generated Method - setHelpMessage()
     * This method shows tooltip help for all the input fields
     * available in the UI. It needs the Mandatory Resource Bundle
     * object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        AcNoMaintenanceMRB objMandatoryRB = new AcNoMaintenanceMRB();
        cboBranches.setHelpMessage(lblMsg, objMandatoryRB.getString("cboBranches"));
        cboProdId.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProdId"));
        txtLastAcNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLastAcNo"));
//        txtNextAcNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNextAcNo"));
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
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace29 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace30 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace31 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace32 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace33 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace34 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panMain = new com.see.truetransact.uicomponent.CPanel();
        panLeft = new com.see.truetransact.uicomponent.CPanel();
        txtNextAcNo = new com.see.truetransact.uicomponent.CTextField();
        lblNextAcNo = new com.see.truetransact.uicomponent.CLabel();
        txtLastAcNo = new com.see.truetransact.uicomponent.CTextField();
        lblLastAcNo = new com.see.truetransact.uicomponent.CLabel();
        lblProdId = new com.see.truetransact.uicomponent.CLabel();
        cboProdId = new com.see.truetransact.uicomponent.CComboBox();
        lblLastAcNoDisplay = new com.see.truetransact.uicomponent.CLabel();
        lblNextAcNoDisplay = new com.see.truetransact.uicomponent.CLabel();
        lblGroupNo = new com.see.truetransact.uicomponent.CLabel();
        cboGroupNo = new com.see.truetransact.uicomponent.CComboBox();
        panGroupData = new com.see.truetransact.uicomponent.CPanel();
        srpAcNoMaintenance = new com.see.truetransact.uicomponent.CScrollPane();
        tblAcNoMaintenance = new com.see.truetransact.uicomponent.CTable();
        panButton = new com.see.truetransact.uicomponent.CPanel();
        btnDetSave = new com.see.truetransact.uicomponent.CButton();
        btnDetNew = new com.see.truetransact.uicomponent.CButton();
        panButton1 = new com.see.truetransact.uicomponent.CPanel();
        lblBranches = new com.see.truetransact.uicomponent.CLabel();
        cboBranches = new com.see.truetransact.uicomponent.CComboBox();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        mbrMain = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptDelete = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Holidays");
        setPreferredSize(new java.awt.Dimension(424, 545));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        tbrMain.setEnabled(false);
        tbrMain.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 11)); // NOI18N
        tbrMain.setMinimumSize(new java.awt.Dimension(28, 28));
        tbrMain.setPreferredSize(new java.awt.Dimension(28, 28));

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
        tbrMain.add(btnView);

        lblSpace4.setText("     ");
        tbrMain.add(lblSpace4);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrMain.add(btnNew);

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrMain.add(lblSpace29);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrMain.add(btnEdit);

        lblSpace30.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace30.setText("     ");
        lblSpace30.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrMain.add(lblSpace30);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrMain.add(btnDelete);

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

        lblSpace31.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace31.setText("     ");
        lblSpace31.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrMain.add(lblSpace31);

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

        lblSpace32.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace32.setText("     ");
        lblSpace32.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace32.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace32.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrMain.add(lblSpace32);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrMain.add(btnException);

        lblSpace33.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace33.setText("     ");
        lblSpace33.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace33.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace33.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrMain.add(lblSpace33);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrMain.add(btnReject);

        lblSpace5.setText("     ");
        tbrMain.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrMain.add(btnPrint);

        lblSpace34.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace34.setText("     ");
        lblSpace34.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrMain.add(lblSpace34);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrMain.add(btnClose);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(tbrMain, gridBagConstraints);

        panMain.setLayout(new java.awt.GridBagLayout());

        panLeft.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panLeft.setLayout(new java.awt.GridBagLayout());

        txtNextAcNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeft.add(txtNextAcNo, gridBagConstraints);

        lblNextAcNo.setText("Next Account No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeft.add(lblNextAcNo, gridBagConstraints);

        txtLastAcNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeft.add(txtLastAcNo, gridBagConstraints);

        lblLastAcNo.setText("Last Account No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeft.add(lblLastAcNo, gridBagConstraints);

        lblProdId.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeft.add(lblProdId, gridBagConstraints);

        cboProdId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdId.setPopupWidth(250);
        cboProdId.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboProdIdItemStateChanged(evt);
            }
        });
        cboProdId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeft.add(cboProdId, gridBagConstraints);

        lblLastAcNoDisplay.setText("Last Account No.");
        lblLastAcNoDisplay.setMaximumSize(new java.awt.Dimension(101, 20));
        lblLastAcNoDisplay.setMinimumSize(new java.awt.Dimension(101, 20));
        lblLastAcNoDisplay.setPreferredSize(new java.awt.Dimension(101, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeft.add(lblLastAcNoDisplay, gridBagConstraints);

        lblNextAcNoDisplay.setText("Next Account No.");
        lblNextAcNoDisplay.setMaximumSize(new java.awt.Dimension(103, 20));
        lblNextAcNoDisplay.setMinimumSize(new java.awt.Dimension(103, 20));
        lblNextAcNoDisplay.setPreferredSize(new java.awt.Dimension(103, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeft.add(lblNextAcNoDisplay, gridBagConstraints);

        lblGroupNo.setText("Group No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        panLeft.add(lblGroupNo, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        panLeft.add(cboGroupNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMain.add(panLeft, gridBagConstraints);

        panGroupData.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panGroupData.setMinimumSize(new java.awt.Dimension(400, 185));
        panGroupData.setPreferredSize(new java.awt.Dimension(400, 185));
        panGroupData.setLayout(new java.awt.GridBagLayout());

        srpAcNoMaintenance.setMinimumSize(new java.awt.Dimension(380, 150));
        srpAcNoMaintenance.setPreferredSize(new java.awt.Dimension(380, 150));

        tblAcNoMaintenance.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Product ID", "Product Description", "Last Ac No", "Next Ac No", "Group No"
            }
        ));
        tblAcNoMaintenance.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblAcNoMaintenanceMouseClicked(evt);
            }
        });
        srpAcNoMaintenance.setViewportView(tblAcNoMaintenance);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGroupData.add(srpAcNoMaintenance, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMain.add(panGroupData, gridBagConstraints);

        panButton.setLayout(new java.awt.GridBagLayout());

        btnDetSave.setText("Save");
        btnDetSave.setEnabled(false);
        btnDetSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton.add(btnDetSave, gridBagConstraints);

        btnDetNew.setText("New");
        btnDetNew.setEnabled(false);
        btnDetNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton.add(btnDetNew, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        panMain.add(panButton, gridBagConstraints);

        panButton1.setLayout(new java.awt.GridBagLayout());

        lblBranches.setText("Branch Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 30, 4, 4);
        panButton1.add(lblBranches, gridBagConstraints);

        cboBranches.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboBranchesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton1.add(cboBranches, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        panMain.add(panButton1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(panMain, gridBagConstraints);

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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        getContentPane().add(panStatus, gridBagConstraints);

        mnuProcess.setText("Process");
        mnuProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuProcessActionPerformed(evt);
            }
        });

        mitNew.setText("New");
        mitNew.setEnabled(false);
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mitEdit.setEnabled(false);
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mitDelete.setEnabled(false);
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
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

    private void btnDetNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetNewActionPerformed
        // TODO add your handling code here:
        btnDetSave.setEnabled(true);
        btnNewActionPerformed();
        tableRow = -1;
    }//GEN-LAST:event_btnDetNewActionPerformed

    private void tblAcNoMaintenanceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAcNoMaintenanceMouseClicked
        // TODO add your handling code here:
        cboProdIdActionPerformed(null);
        tableRow = tblAcNoMaintenance.getSelectedRow();
        observable.populateTableDetails(tableRow);
        if(!observable.getIsGroupDeposit().equalsIgnoreCase("Y")){
            lblGroupNo.setVisible(false);
            cboGroupNo.setVisible(false);
        }
        btnDetSave.setEnabled(true);
        btnDetNew.setEnabled(false);
        ClientUtil.enableDisable(panLeft, true);
    }//GEN-LAST:event_tblAcNoMaintenanceMouseClicked

    private void btnDetSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetSaveActionPerformed
        // TODO add your handling code here:
        String mandatoryAcNoMaintenanceMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panLeft);
        
        if (mandatoryAcNoMaintenanceMessage.length() > 0){
            displayAlert(mandatoryAcNoMaintenanceMessage);
        }else {
            updateOBFields();
            observable.tableInsertUpdate(tableRow);
            btnDetSave.setEnabled(false);
            btnDetNew.setEnabled(true);
            ClientUtil.enableDisable(panLeft, false);
            observable.resetFields();
            branchDataChanged = true;
            btnSave.setEnabled(true);
            btnCancel.setEnabled(true);
        }
    }//GEN-LAST:event_btnDetSaveActionPerformed

    private void cboBranchesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboBranchesActionPerformed
        // TODO add your handling code here:
        if (!branchDataChanged){
            observable.populateOB();
        } else {
            int result = ClientUtil.confirmationAlert("Changes will be lost. Do you want to continue?");
            if (result==0) {
                branchDataChanged = false;
                observable.populateOB();
            }
        }
    }//GEN-LAST:event_cboBranchesActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        popUpItems(VIEW);
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
         HashMap reportParamMap = new HashMap();
 com.see.truetransact.clientutil.ttrintegration.LinkReport.getReports(getScreenID(), reportParamMap);
    }//GEN-LAST:event_btnPrintActionPerformed

    private void mnuProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuProcessActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mnuProcessActionPerformed
    
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
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    public void authorizeStatus(String authorizeStatus) {
        if (viewType == AUTHORIZE && isFilled){
            viewType = -1;
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
            singleAuthorizeMap.put("PROD_ID", observable.getCbmProdId().getKeyForSelected());
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, curDate);
            singleAuthorizeMap.put("BRANCH_ID", ProxyParameters.USER_ID);
            ClientUtil.execute("authorizeBranchAcNoMaintenance", singleAuthorizeMap);
            btnCancelActionPerformed(null);
        } else{
            viewType = AUTHORIZE;
            final HashMap mapParam = new HashMap();
            mapParam.put(CommonConstants.MAP_NAME, "getSelectBranchAcNoMaintenanceAuthorizeTOList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeBranchAcNoMaintenance");
            mapParam.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
            isFilled = false;
            setModified(true);
            final AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
        }
    }                                        
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        observable.resetStatus();
        cifClosingAlert();
        //        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // Add your handling code here:
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        super.removeEditLock(holidayId);
        observable.resetForm();
        setButtonEnableDisable();
        
        ClientUtil.enableDisable(this, false);
        viewType = -1;
        //__ Make the Screen Closable..
        setModified(false);
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        cboBranches.setEnabled(true);
        btnDetSave.setEnabled(false);
        btnCancel.setEnabled(true);
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // Add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        //        objAcNoMaintenanceRB = new AcNoMaintenanceRB();
        
        if (!branchDataChanged){
            displayAlert("Nothing to save...");
        }else{
            saveAction();
        }
        super.removeEditLock(holidayId);
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // Add your handling code here:
        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        //        objAcNoMaintenanceRB = new AcNoMaintenanceRB();
        final String[] options = {resourceBundle.getString("cDialogOk"),resourceBundle.getString("cDialogCancel")};
        final int option = COptionPane.showOptionDialog(null, resourceBundle.getString("deleteWarning"), CommonConstants.WARNINGTITLE,
        COptionPane.OK_CANCEL_OPTION, COptionPane.WARNING_MESSAGE,
        null, options, options[0]);
        if (option == 0){
            observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
            observable.doAction();
            setButtonEnableDisable();
            
            observable.resetForm();
            observable.setResultStatus();
            ClientUtil.enableDisable(panLeft, false);
    
        }
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // Add your handling code here:
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        viewType = EDIT;
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        observable.setStatus();
        popUpItems(EDIT);
//        tblAcNoMaintenance.setModel(observable.getTblHolidays());
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // Add your handling code here:
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed
    
    private void btnNewActionPerformed() {
        // Add your handling code here:
 
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.setStatus();
        ClientUtil.enableDisable(panLeft, true);
        ClientUtil.enableDisable(panGroupData, true);
        setButtonEnableDisable();
  
        //__ To Save the data in the Internal Frame...
        setModified(true);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }
        
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
 
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.setStatus();
        ClientUtil.enableDisable(panLeft, true);
        ClientUtil.enableDisable(panGroupData, true);
        setButtonEnableDisable();
  
        //__ To Save the data in the Internal Frame...
        setModified(true);
        btnDetNew.setEnabled(true);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnNewActionPerformed
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm

    private void cboProdIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdIdActionPerformed
        // TODO add your handling code here:
        if (CommonUtil.convertObjToStr(((ComboBoxModel) cboProdId.getModel()).getKeyForSelected()).length() > 0) {
            HashMap checkMap = new HashMap();
            checkMap.put("PROD_ID", (String) ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
            List groupDepositProdList = ClientUtil.executeQuery("getIsGroupDepositProduct", checkMap);
            if (groupDepositProdList != null && groupDepositProdList.size() > 0) {
                HashMap groupDepositProdMap = (HashMap) groupDepositProdList.get(0);
                if (groupDepositProdMap != null && groupDepositProdMap.containsKey("IS_GROUP_DEPOSIT") && groupDepositProdMap.get("IS_GROUP_DEPOSIT") != null) {
                    if (CommonUtil.convertObjToStr(groupDepositProdMap.get("IS_GROUP_DEPOSIT")).equalsIgnoreCase("Y")) {
                        observable.setIsGroupDeposit("Y");
                        cboGroupNo.setVisible(true);
                        lblGroupNo.setVisible(true);
                        boolean groupExists = observable.populateGroupDepositCombo((String) ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
                        if(groupExists){
                            cboGroupNo.setModel(observable.getCbmGroupNo());  
                        }                          
                    }else{
                        lblGroupNo.setVisible(false);
                        cboGroupNo.setVisible(false);
                    }
                }
            }else{
                lblGroupNo.setVisible(false);
                cboGroupNo.setVisible(false);
            }
        }
    }//GEN-LAST:event_cboProdIdActionPerformed

    private void cboProdIdItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboProdIdItemStateChanged
        // TODO add your handling code here:
        cboProdIdActionPerformed(null);
    }//GEN-LAST:event_cboProdIdItemStateChanged
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
    /* This method helps in popoualting the data from the data base  */
    private void popUpItems(int Action) {
        final HashMap viewMap = new HashMap();
        if (Action == EDIT || Action == DELETE || Action == VIEW){
            ArrayList lst = new ArrayList();
            lst.add("HOLIDAY_ID");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            lblStatus.setText(ClientConstants.ACTION_STATUS[0]);
        }
        ACTION=Action;
        HashMap whereMap = new HashMap();
        whereMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
        
        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        viewMap.put(CommonConstants.MAP_NAME, "ViewAllAcNoMaintenanceTO");
        
        whereMap = null;
        
        new ViewAll(this, viewMap).show();
    }
    
    /* This method gets the CommonConstants.MAP_WHERE condition from the row clicked on the View All UI */
    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
        holidayId = "" ;
        boolean WeeklyDataExists = false;
//        WeeklyDataExists = observable.checkWeeklyOff();
//        if(WeeklyDataExists){
//            observable.populateWeeklyOff();
//            if(observable.getRdoWeeklyOff_Yes() == true){
//                ClientUtil.enableDisable(panWeek, true);
//            }
//        }
//        ClientUtil.enableDisable(panHoliday, true);
//        ClientUtil.enableDisable(panWeeklyOff, true);
//        //        hash.put(CommonConstants.MAP_WHERE, hash.get("HOLIDAY_ID"));
//        observable.setHolidayID(CommonUtil.convertObjToStr(hash.get("HOLIDAY_ID")));
        holidayId = CommonUtil.convertObjToStr(hash.get("HOLIDAY_ID"));
        isFilled = true;
        observable.setStatus();
        observable.populateData(hash);
        setButtonEnableDisable();
        setDelBtnEnableDisable(true);
//        setYearEnableDisable();
        
    	Date currDt = (Date)curDate.clone();;
        Date preDt=DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hash.get("HOLIDAY DATE")));
        long dateDiff=DateUtil.dateDiff(preDt,currDt);
        System.out.println("###diffnoofdays"+dateDiff);
        if(dateDiff>0)
            ClientUtil.enableDisable(panLeft,false);

        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        
        //__ To Save the data in the Internal Frame...
        setModified(true);
        
        //--- disable the fields if it is Authorize
        if(viewType == AUTHORIZE || viewType == VIEW){
            ClientUtil.enableDisable(panLeft, false);
            btnDelete.setEnabled(false);
            btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                 btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                 btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
            
        } else if(dateDiff>0){
            ClientUtil.enableDisable(panLeft, false);

        }
        else{
        ClientUtil.enableDisable(panLeft, true);
            btnDelete.setEnabled(true);
        }
    }
    

    public void saveAction(){
        tableRow = tblAcNoMaintenance.getRowCount();
        arrayListTableDate = new ArrayList();
        updateOBFields();
        if(tableRow != 0 && (ACTION == EDIT || observable.getActionType() == ClientConstants.ACTIONTYPE_NEW )){
            /** If there are datas in Table i.e table row != null **/
            enableButton = 2;
//            updateOBFields();
            observable.doAction();
            branchDataChanged = false;
            if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED){
                observable.resetForm();
                observable.setResultStatus();
                setButtonEnableDisable();
                ClientUtil.enableDisable(this, false);
            }
        }
        lblStatus.setText(observable.getLblStatus());
        cboBranches.setEnabled(true);
        btnNew.setEnabled(true);
        //__ Make the Screen Closable..
        setModified(false);
    }
    
    
    /* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setTxtLastAcNo(txtLastAcNo.getText());
        observable.setTxtNextAcNo(txtNextAcNo.getText());
        observable.setModule(getModule());
        observable.setScreen(getScreen());
    }
    
    private void displayAlert(String message){
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    

    /** To Enable or Disable New, Edit, Save and Cancel Button */
    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        btnSave.setEnabled(btnCancel.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
        setDelBtnEnableDisable(false);
    }
    
    /** To Enable or Disable Delete Button */
    private void setDelBtnEnableDisable(boolean enableDisable){
        btnDelete.setEnabled(enableDisable );
        mitDelete.setEnabled(enableDisable);
    }
    
    
    /**
     * @param args the command line arguments
     */
   /* public static void main(String args[]) {
        new AcNoMaintenanceUI().show();
    }
    */
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDetNew;
    private com.see.truetransact.uicomponent.CButton btnDetSave;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboBranches;
    private com.see.truetransact.uicomponent.CComboBox cboGroupNo;
    private com.see.truetransact.uicomponent.CComboBox cboProdId;
    private com.see.truetransact.uicomponent.CLabel lblBranches;
    private com.see.truetransact.uicomponent.CLabel lblGroupNo;
    private com.see.truetransact.uicomponent.CLabel lblLastAcNo;
    private com.see.truetransact.uicomponent.CLabel lblLastAcNoDisplay;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNextAcNo;
    private com.see.truetransact.uicomponent.CLabel lblNextAcNoDisplay;
    private com.see.truetransact.uicomponent.CLabel lblProdId;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace29;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace30;
    private com.see.truetransact.uicomponent.CLabel lblSpace31;
    private com.see.truetransact.uicomponent.CLabel lblSpace32;
    private com.see.truetransact.uicomponent.CLabel lblSpace33;
    private com.see.truetransact.uicomponent.CLabel lblSpace34;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panButton;
    private com.see.truetransact.uicomponent.CPanel panButton1;
    private com.see.truetransact.uicomponent.CPanel panGroupData;
    private com.see.truetransact.uicomponent.CPanel panLeft;
    private com.see.truetransact.uicomponent.CPanel panMain;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private com.see.truetransact.uicomponent.CScrollPane srpAcNoMaintenance;
    private com.see.truetransact.uicomponent.CTable tblAcNoMaintenance;
    private javax.swing.JToolBar tbrMain;
    private com.see.truetransact.uicomponent.CTextField txtLastAcNo;
    private com.see.truetransact.uicomponent.CTextField txtNextAcNo;
    // End of variables declaration//GEN-END:variables
}