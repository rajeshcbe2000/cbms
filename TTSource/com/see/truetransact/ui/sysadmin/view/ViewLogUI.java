/* Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ViewLogUI.java
 *
 * Created on April 16, 2004, 5:16 PM
 */

package com.see.truetransact.ui.sysadmin.view;

import com.see.truetransact.ui.sysadmin.view.ViewLogRB;
import com.see.truetransact.ui.sysadmin.view.ViewLogOB;
import com.see.truetransact.ui.sysadmin.view.ViewLogMRB;
import com.see.truetransact.ui.sysadmin.view.ViewLogPopUpUI;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.uicomponent.CButtonGroup;// To add and Remove the Radio Buttons...
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.ui.common.viewall.TableDialogUI;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.JTable;
import java.awt.Component;
import java.util.Observer;
import java.util.Observable;
import java.util.HashMap;
import java.awt.Color;
import java.util.Date;

import java.util.ArrayList;

/**
 *
 * @author  Lohith R.
 */

public class ViewLogUI extends com.see.truetransact.uicomponent.CInternalFrame  implements Observer, UIMandatoryField {
    
    HashMap mandatoryMap =  new HashMap();
    private ViewLogOB observable;
    private ViewLogRB objViewLogRB =  new ViewLogRB();
    private final int COLUMN_STATUS = 7;
    private boolean dateValue = false;
    private final String ROW_COUNT = "100";
    private final String MANY_IPADDR_VIEW_STATEMENT = "ViewLog.ViewLogMultipleIPADDR";
    private final String MANY_USER_VIEW_STATEMENT = "ViewLog.ViewLogMultipleUSERS";
    private final Color colorNew = new Color(226,255,225);
    private final Color colorEdit = new Color(224,224,255);
    private final Color colorDelete = new Color(255,222,222);
    private final Color colorAll = new Color(255,255,214);
    
    HashMap resultHash = new HashMap();
    
    /** Creates new form ViewLogUI */
    public ViewLogUI() {
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
        setMaximumLength();
        setHelpMessage();
        lblColorNew.setBackground(colorNew);
        lblColorEdit.setBackground(colorEdit);
        lblColorDelete.setBackground(colorDelete);
        lblColorAll.setBackground(colorAll);
        observable.setTxtLatestEntries(ROW_COUNT);
        txtLatestEntries.setText(observable.getTxtLatestEntries());
        observable.removeViewLogRow();
        observable.resetFields();
        tblViewLog.setModel(observable.getTblViewLog());
    }
    
    /** Implementing Singleton pattern */
    private void setObservable() {
        observable = ViewLogOB.getInstance();
        observable.addObserver(this);
    }
    
    /** Auto Generated Method - setFieldNames()
     * This method assigns name for all the components.
     * Other functions are working based on this name. */
    private void setFieldNames() {
        mbrViewLog.setName("mbrViewLog");
        mnuTableOutput.setName("mnuTableOutput");
        mitMultipleIP.setName("mitMultipleIP");
        mitMultipleUsers.setName("mitMultipleUsers");
        btnView.setName("btnView");
        btnClear.setName("btnClear");
        cboActivity.setName("cboActivity");
        cboBranchID.setName("cboBranchID");
        cboFindbyHistory.setName("cboFindbyHistory");
        cboIPAddress.setName("cboIPAddress");
        cboModule.setName("cboModule");
        cboScreen.setName("cboScreen");
        cboUserID.setName("cboUserID");
        dateFromDate.setName("dateFromDate");
        dateToDate.setName("dateToDate");
        lblUserID.setName("lblUserID");
        lblActivity.setName("lblActivity");
        lblBranchID.setName("lblBranchID");
        lblColorAll.setName("lblColorAll");
        lblColorDelete.setName("lblColorDelete");
        lblColorEdit.setName("lblColorEdit");
        lblColorNew.setName("lblColorNew");
        lblFindbyHistory.setName("lblFindbyHistory");
        lblFromDate.setName("lblFromDate");
        lblIPAddress.setName("lblIPAddress");
        lblLatestEntries.setName("lblLatestEntries");
        lblModule.setName("lblModule");
        lblScreen.setName("lblScreen");
        lblToDate.setName("lblToDate");
        panFields.setName("panFields");
        panMain.setName("panMain");
        panRadioButton.setName("panRadioButton");
        panViewLogTable.setName("panViewLogTable");
        rdoAll.setName("rdoAll");
        rdoDelete.setName("rdoDelete");
        rdoEdit.setName("rdoEdit");
        rdoNew.setName("rdoNew");
        sptFields.setName("sptFields");
        srpViewLog.setName("srpViewLog");
        tblViewLog.setName("tblViewLog");
        txtLatestEntries.setName("txtLatestEntries");
        lblStatus.setName("lblStatus");
        panStatus.setName("panStatus");
        lblMsg.setName("lblMsg");
    }
    
    /* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        ViewLogRB resourceBundle = new ViewLogRB();
        lblModule.setText(resourceBundle.getString("lblModule"));
        mnuTableOutput.setText(resourceBundle.getString("mnuTableOutput"));
        mitMultipleIP.setText(resourceBundle.getString("mitMultipleIP"));
        mitMultipleUsers.setText(resourceBundle.getString("mitMultipleUsers"));
        rdoAll.setText(resourceBundle.getString("rdoAll"));
        lblFromDate.setText(resourceBundle.getString("lblFromDate"));
        lblLatestEntries.setText(resourceBundle.getString("lblLatestEntries"));
        lblColorAll.setText(resourceBundle.getString("lblColorAll"));
        lblUserID.setText(resourceBundle.getString("lblUserID"));
        lblIPAddress.setText(resourceBundle.getString("lblIPAddress"));
        lblToDate.setText(resourceBundle.getString("lblToDate"));
        rdoNew.setText(resourceBundle.getString("rdoNew"));
        lblFindbyHistory.setText(resourceBundle.getString("lblFindbyHistory"));
        lblBranchID.setText(resourceBundle.getString("lblBranchID"));
        lblScreen.setText(resourceBundle.getString("lblScreen"));
        lblColorNew.setText(resourceBundle.getString("lblColorNew"));
        lblActivity.setText(resourceBundle.getString("lblActivity"));
        rdoDelete.setText(resourceBundle.getString("rdoDelete"));
        lblColorDelete.setText(resourceBundle.getString("lblColorDelete"));
        lblColorEdit.setText(resourceBundle.getString("lblColorEdit"));
        rdoEdit.setText(resourceBundle.getString("rdoEdit"));
        btnView.setText(resourceBundle.getString("btnView"));
        btnClear.setText(resourceBundle.getString("btnClear"));
        ((javax.swing.border.TitledBorder)panMain.getBorder()).setTitle(resourceBundle.getString("panMain"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
    }
    
    
    /** Auto Generated Method - setMandatoryHashMap()
     * This method list out all the Input Fields available in the UI.
     * It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboFindbyHistory", new Boolean(true));
        mandatoryMap.put("cboUserID", new Boolean(true));
        mandatoryMap.put("cboBranchID", new Boolean(true));
        mandatoryMap.put("cboScreen", new Boolean(true));
        mandatoryMap.put("cboIPAddress", new Boolean(false));
        mandatoryMap.put("cboModule", new Boolean(true));
        mandatoryMap.put("cboActivity", new Boolean(true));
        mandatoryMap.put("txtLatestEntries", new Boolean(true));
        mandatoryMap.put("dateToDate", new Boolean(true));
        mandatoryMap.put("dateFromDate", new Boolean(true));
    }
    
    /** Auto Generated Method - getMandatoryHashMap()
     * Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    private void initComponentData() {
        cboFindbyHistory.setModel(observable.getCbmFindbyHistory());
        cboUserID.setModel(observable.getCbmUserID());
        cboBranchID.setModel(observable.getCbmBranchID());
        cboScreen.setModel(observable.getCbmScreen());
        cboIPAddress.setModel(observable.getCbmIPAddress());
        cboModule.setModel(observable.getCbmModule());
        cboActivity.setModel(observable.getCbmActivity());
    }
    
    /* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        removeRadioButtons();
        cboFindbyHistory.setSelectedItem(observable.getCboFindbyHistory());
        cboUserID.setSelectedItem(observable.getCboUserID());
        cboModule.setSelectedItem(observable.getCboModule());
        cboIPAddress.setSelectedItem(observable.getCboIPAddress());
        txtLatestEntries.setText(observable.getTxtLatestEntries());
        rdoNew.setSelected(observable.getRdoNew());
        rdoEdit.setSelected(observable.getRdoEdit());
        rdoDelete.setSelected(observable.getRdoDelete());
        rdoAll.setSelected(observable.getRdoAll());
        cboBranchID.setSelectedItem(observable.getCboBranchID());
        cboScreen.setSelectedItem(observable.getCboScreen());
        cboActivity.setSelectedItem(observable.getCboActivity());
        dateFromDate.setDateValue(observable.getDateFromDate());
        dateToDate.setDateValue(observable.getDateToDate());
        tblViewLog.setModel(observable.getTblViewLog());
        addRadioButtons();
    }
    
        /* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setCboFindbyHistory((String) cboFindbyHistory.getSelectedItem());
        observable.setCboUserID((String) cboUserID.getSelectedItem());
        observable.setCboModule((String) cboModule.getSelectedItem());
        observable.setCboIPAddress((String) cboIPAddress.getSelectedItem());
        observable.setTxtLatestEntries(txtLatestEntries.getText());
        observable.setRdoNew(rdoNew.isSelected());
        observable.setRdoEdit(rdoEdit.isSelected());
        observable.setRdoDelete(rdoDelete.isSelected());
        observable.setRdoAll(rdoAll.isSelected());
        observable.setCboBranchID((String) cboBranchID.getSelectedItem());
        observable.setCboScreen((String) cboScreen.getSelectedItem());
        observable.setCboActivity((String) cboActivity.getSelectedItem());
        observable.setDateFromDate(dateFromDate.getDateValue());
        observable.setDateToDate(dateToDate.getDateValue());
        observable.setTblViewLog((com.see.truetransact.clientutil.EnhancedTableModel)tblViewLog.getModel());
    }
    
    public void cifClosingAlert() {
        if (isModified()) {
            COptionPane.showMessageDialog(this, "Please press Save or Cancel before closing.",
            "Closing", COptionPane.OK_OPTION);
        } else {
            setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
            this.dispose();
        }
    }
    
    private void setMaximumLength(){
        txtLatestEntries.setMaxLength(3);
        txtLatestEntries.setValidation(new NumericValidation());
    }
    
    
    
    /** Auto Generated Method - setHelpMessage()
     * This method shows tooltip help for all the input fields
     * available in the UI. It needs the Mandatory Resource Bundle
     * object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        ViewLogMRB objMandatoryMRB = new ViewLogMRB();
        cboFindbyHistory.setHelpMessage(lblMsg, objMandatoryMRB.getString("cboFindbyHistory"));
        cboUserID.setHelpMessage(lblMsg, objMandatoryMRB.getString("cboUserID"));
        cboBranchID.setHelpMessage(lblMsg, objMandatoryMRB.getString("cboBranchID"));
        cboScreen.setHelpMessage(lblMsg, objMandatoryMRB.getString("cboScreen"));
        cboIPAddress.setHelpMessage(lblMsg, objMandatoryMRB.getString("cboIPAddress"));
        cboModule.setHelpMessage(lblMsg, objMandatoryMRB.getString("cboModule"));
        cboActivity.setHelpMessage(lblMsg, objMandatoryMRB.getString("cboActivity"));
        txtLatestEntries.setHelpMessage(lblMsg, objMandatoryMRB.getString("txtLatestEntries"));
        dateToDate.setHelpMessage(lblMsg, objMandatoryMRB.getString("dateToDate"));
        dateFromDate.setHelpMessage(lblMsg, objMandatoryMRB.getString("dateFromDate"));
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdgActivity = new com.see.truetransact.uicomponent.CButtonGroup();
        panMain = new com.see.truetransact.uicomponent.CPanel();
        panFields = new com.see.truetransact.uicomponent.CPanel();
        lblFindbyHistory = new com.see.truetransact.uicomponent.CLabel();
        lblUserID = new com.see.truetransact.uicomponent.CLabel();
        lblFromDate = new com.see.truetransact.uicomponent.CLabel();
        cboFindbyHistory = new com.see.truetransact.uicomponent.CComboBox();
        cboUserID = new com.see.truetransact.uicomponent.CComboBox();
        dateFromDate = new com.see.truetransact.uicomponent.CDateField();
        lblActivity = new com.see.truetransact.uicomponent.CLabel();
        lblIPAddress = new com.see.truetransact.uicomponent.CLabel();
        lblToDate = new com.see.truetransact.uicomponent.CLabel();
        cboModule = new com.see.truetransact.uicomponent.CComboBox();
        cboIPAddress = new com.see.truetransact.uicomponent.CComboBox();
        dateToDate = new com.see.truetransact.uicomponent.CDateField();
        lblLatestEntries = new com.see.truetransact.uicomponent.CLabel();
        txtLatestEntries = new com.see.truetransact.uicomponent.CTextField();
        panRadioButton = new com.see.truetransact.uicomponent.CPanel();
        lblColorNew = new com.see.truetransact.uicomponent.CLabel();
        rdoNew = new com.see.truetransact.uicomponent.CRadioButton();
        lblColorEdit = new com.see.truetransact.uicomponent.CLabel();
        rdoEdit = new com.see.truetransact.uicomponent.CRadioButton();
        lblColorDelete = new com.see.truetransact.uicomponent.CLabel();
        rdoDelete = new com.see.truetransact.uicomponent.CRadioButton();
        lblColorAll = new com.see.truetransact.uicomponent.CLabel();
        rdoAll = new com.see.truetransact.uicomponent.CRadioButton();
        sptFields = new com.see.truetransact.uicomponent.CSeparator();
        lblBranchID = new com.see.truetransact.uicomponent.CLabel();
        lblScreen = new com.see.truetransact.uicomponent.CLabel();
        lblModule = new com.see.truetransact.uicomponent.CLabel();
        cboBranchID = new com.see.truetransact.uicomponent.CComboBox();
        cboScreen = new com.see.truetransact.uicomponent.CComboBox();
        cboActivity = new com.see.truetransact.uicomponent.CComboBox();
        panViewLogTable = new com.see.truetransact.uicomponent.CPanel();
        srpViewLog = new com.see.truetransact.uicomponent.CScrollPane();
        tblViewLog = new com.see.truetransact.uicomponent.CTable();
        panClear = new com.see.truetransact.uicomponent.CPanel();
        btnClear = new com.see.truetransact.uicomponent.CButton();
        btnView = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        tbrConfig = new javax.swing.JToolBar();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace24 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        mbrViewLog = new com.see.truetransact.uicomponent.CMenuBar();
        mnuTableOutput = new javax.swing.JMenu();
        mitMultipleIP = new javax.swing.JMenuItem();
        mitMultipleUsers = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("View Log");
        getContentPane().setLayout(new java.awt.GridBagLayout());

        panMain.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panMain.setLayout(new java.awt.GridBagLayout());

        panFields.setLayout(new java.awt.GridBagLayout());

        lblFindbyHistory.setText("Find by History");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panFields.add(lblFindbyHistory, gridBagConstraints);

        lblUserID.setText("Logged on User ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panFields.add(lblUserID, gridBagConstraints);

        lblFromDate.setText("From Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panFields.add(lblFromDate, gridBagConstraints);

        cboFindbyHistory.setPopupWidth(175);
        cboFindbyHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboFindbyHistoryActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panFields.add(cboFindbyHistory, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panFields.add(cboUserID, gridBagConstraints);

        dateFromDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                dateFromDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panFields.add(dateFromDate, gridBagConstraints);

        lblActivity.setText("Activity");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panFields.add(lblActivity, gridBagConstraints);

        lblIPAddress.setText("IP Address");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panFields.add(lblIPAddress, gridBagConstraints);

        lblToDate.setText("To Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panFields.add(lblToDate, gridBagConstraints);

        cboModule.setPopupWidth(175);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panFields.add(cboModule, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panFields.add(cboIPAddress, gridBagConstraints);

        dateToDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                dateToDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panFields.add(dateToDate, gridBagConstraints);

        lblLatestEntries.setText("No. of Latest Entries");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panFields.add(lblLatestEntries, gridBagConstraints);

        txtLatestEntries.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panFields.add(txtLatestEntries, gridBagConstraints);

        panRadioButton.setLayout(new java.awt.GridBagLayout());

        lblColorNew.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        lblColorNew.setForeground(new java.awt.Color(204, 204, 204));
        lblColorNew.setMinimumSize(new java.awt.Dimension(20, 20));
        lblColorNew.setPreferredSize(new java.awt.Dimension(20, 20));
        lblColorNew.setOpaque(true);
        panRadioButton.add(lblColorNew, new java.awt.GridBagConstraints());

        rdgActivity.add(rdoNew);
        rdoNew.setText("New");
        rdoNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        panRadioButton.add(rdoNew, gridBagConstraints);

        lblColorEdit.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        lblColorEdit.setForeground(new java.awt.Color(204, 204, 204));
        lblColorEdit.setMinimumSize(new java.awt.Dimension(20, 20));
        lblColorEdit.setPreferredSize(new java.awt.Dimension(20, 20));
        lblColorEdit.setOpaque(true);
        panRadioButton.add(lblColorEdit, new java.awt.GridBagConstraints());

        rdgActivity.add(rdoEdit);
        rdoEdit.setText("Edit");
        rdoEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoEditActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        panRadioButton.add(rdoEdit, gridBagConstraints);

        lblColorDelete.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        lblColorDelete.setForeground(new java.awt.Color(204, 204, 204));
        lblColorDelete.setMinimumSize(new java.awt.Dimension(20, 20));
        lblColorDelete.setPreferredSize(new java.awt.Dimension(20, 20));
        lblColorDelete.setOpaque(true);
        panRadioButton.add(lblColorDelete, new java.awt.GridBagConstraints());

        rdgActivity.add(rdoDelete);
        rdoDelete.setText("Delete");
        rdoDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        panRadioButton.add(rdoDelete, gridBagConstraints);

        lblColorAll.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        lblColorAll.setForeground(new java.awt.Color(204, 204, 204));
        lblColorAll.setMinimumSize(new java.awt.Dimension(20, 20));
        lblColorAll.setPreferredSize(new java.awt.Dimension(20, 20));
        lblColorAll.setOpaque(true);
        panRadioButton.add(lblColorAll, new java.awt.GridBagConstraints());

        rdgActivity.add(rdoAll);
        rdoAll.setText("All");
        rdoAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoAllActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        panRadioButton.add(rdoAll, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panFields.add(panRadioButton, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panFields.add(sptFields, gridBagConstraints);

        lblBranchID.setText("Branch ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panFields.add(lblBranchID, gridBagConstraints);

        lblScreen.setText("Screen");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panFields.add(lblScreen, gridBagConstraints);

        lblModule.setText("Module");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panFields.add(lblModule, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panFields.add(cboBranchID, gridBagConstraints);

        cboScreen.setPopupWidth(175);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panFields.add(cboScreen, gridBagConstraints);

        cboActivity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboActivityActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panFields.add(cboActivity, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMain.add(panFields, gridBagConstraints);

        panViewLogTable.setLayout(new java.awt.GridBagLayout());

        srpViewLog.setMinimumSize(new java.awt.Dimension(560, 225));
        srpViewLog.setPreferredSize(new java.awt.Dimension(560, 225));

        tblViewLog.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblViewLog.setPreferredScrollableViewportSize(new java.awt.Dimension(0, 0));
        tblViewLog.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblViewLogMouseClicked(evt);
            }
        });
        srpViewLog.setViewportView(tblViewLog);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 4, 4, 4);
        panViewLogTable.add(srpViewLog, gridBagConstraints);

        panClear.setLayout(new java.awt.GridBagLayout());

        btnClear.setText("Clear Fields");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panClear.add(btnClear, gridBagConstraints);

        btnView.setText("View");
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panClear.add(btnView, gridBagConstraints);

        panViewLogTable.add(panClear, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMain.add(panViewLogTable, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
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
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 0);
        getContentPane().add(panStatus, gridBagConstraints);

        tbrConfig.setEnabled(false);

        lblSpace1.setText("     ");
        tbrConfig.add(lblSpace1);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrConfig.add(btnPrint);

        lblSpace24.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace24.setText("     ");
        lblSpace24.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrConfig.add(lblSpace24);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrConfig.add(btnClose);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(tbrConfig, gridBagConstraints);

        mnuTableOutput.setText("Table Output");
        mnuTableOutput.setMinimumSize(new java.awt.Dimension(73, 19));

        mitMultipleIP.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        mitMultipleIP.setMnemonic('N');
        mitMultipleIP.setText("Multiple IP Address");
        mitMultipleIP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitMultipleIPActionPerformed(evt);
            }
        });
        mnuTableOutput.add(mitMultipleIP);

        mitMultipleUsers.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        mitMultipleUsers.setMnemonic('E');
        mitMultipleUsers.setText("Multiple Users");
        mitMultipleUsers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitMultipleUsersActionPerformed(evt);
            }
        });
        mnuTableOutput.add(mitMultipleUsers);

        mbrViewLog.add(mnuTableOutput);

        setJMenuBar(mbrViewLog);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void mitMultipleUsersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitMultipleUsersActionPerformed
        // TODO add your handling code here:
        mitMultipleUsersActionPerformed();
    }//GEN-LAST:event_mitMultipleUsersActionPerformed
    private void mitMultipleUsersActionPerformed() {
        updateOBFields();
        HashMap whereMap = observable.hashMapForMultipleRecords();
        if (whereMap != null){
            TableDialogUI gui = new TableDialogUI(MANY_USER_VIEW_STATEMENT, whereMap);
            gui.setSize(536, 566);
            gui.show();
            gui = null;
        }
    }
    private void mitMultipleIPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitMultipleIPActionPerformed
        // TODO add your handling code here:
        mitMultipleIPActionPerformed();
    }//GEN-LAST:event_mitMultipleIPActionPerformed
    private void mitMultipleIPActionPerformed() {
        updateOBFields();
        HashMap whereMap = observable.hashMapForMultipleRecords();
        if (whereMap != null){
            TableDialogUI gui = new TableDialogUI(MANY_IPADDR_VIEW_STATEMENT, whereMap);
            gui.setSize(536, 566);
            gui.show();
            gui = null;
        }
    }
    private void tblViewLogMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblViewLogMouseClicked
        // Add your handling code here:
        resultHash = observable.populateViewLogDialogForOneRow(tblViewLog.getSelectedRow());
        new NewViewLogPopUpUI(this, resultHash).show();
    }//GEN-LAST:event_tblViewLogMouseClicked
    
    private void cboFindbyHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboFindbyHistoryActionPerformed
        // Add your handling code here:
        if (cboFindbyHistory.getSelectedIndex() > 0){
            dateFromDate.setDateValue("");
            dateToDate.setDateValue("");
        }
    }//GEN-LAST:event_cboFindbyHistoryActionPerformed
    
    private void dateToDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_dateToDateFocusLost
        // Add your handling code here:
        ClientUtil.validateToDate(dateToDate, dateFromDate.getDateValue());
        updateOBFields();
        observable.resetComboFindByHistory();
    }//GEN-LAST:event_dateToDateFocusLost
    
    private void dateFromDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_dateFromDateFocusLost
        // Add your handling code here:
        ClientUtil.validateFromDate(dateFromDate, dateToDate.getDateValue());
        updateOBFields();
        observable.resetComboFindByHistory();
    }//GEN-LAST:event_dateFromDateFocusLost
    
    private void rdoAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoAllActionPerformed
        // Add your handling code here:
        updateOBFields();
        if (rdoAll.isSelected()){
            observable.resetComboActivity();
        }
    }//GEN-LAST:event_rdoAllActionPerformed
    
    private void rdoDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDeleteActionPerformed
        // Add your handling code here:
        updateOBFields();
        if (rdoDelete.isSelected()){
            observable.resetComboActivity();
        }
    }//GEN-LAST:event_rdoDeleteActionPerformed
    
    private void rdoEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoEditActionPerformed
        // Add your handling code here:
        updateOBFields();
        if (rdoEdit.isSelected()){
            observable.resetComboActivity();
        }
    }//GEN-LAST:event_rdoEditActionPerformed
    
    private void rdoNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoNewActionPerformed
        // Add your handling code here:
        updateOBFields();
        if (rdoNew.isSelected()){
            observable.resetComboActivity();
        }
    }//GEN-LAST:event_rdoNewActionPerformed
    
    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // Add your handling code here:
        removeTableViewLogRow();
        observable.resetFields();
    }//GEN-LAST:event_btnClearActionPerformed
    
    private void cboActivityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboActivityActionPerformed
        // Add your handling code here:
        observable.setCboActivity((String) cboActivity.getSelectedItem());
        if ( cboActivity.getSelectedIndex() > 0){
            updateOBFields();
            observable.resetRadioButton();
            dateFromDate.setDateValue(observable.getDateFromDate());
            dateToDate.setDateValue(observable.getDateToDate());
        }
    }//GEN-LAST:event_cboActivityActionPerformed
    
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // Add your handling code here:
        updateOBFields();
        removeTableViewLogRow();
        observable.viewLogTable();
        tblViewLog.setModel(observable.getTblViewLog());
        /* Set a cellrenderer to this table in order format the date */
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected,hasFocus, row, column);
                column = table.convertColumnIndexToModel(column);
                
                Color colorInsert = lblColorNew.getBackground();
                Color colorUpdate = lblColorEdit.getBackground();
                Color colorDelete = lblColorDelete.getBackground();
                Color colorOthers = lblColorAll.getBackground();
                
                Object val = table.getValueAt(row, COLUMN_STATUS);
                String stringStatus  = CommonUtil.convertObjToStr(val);
                setForeground(table.getForeground());
                if (stringStatus.equalsIgnoreCase(CommonConstants.TOSTATUS_INSERT)){
                    setBackground(colorInsert);
                }else if (stringStatus.equalsIgnoreCase(CommonConstants.TOSTATUS_UPDATE)){
                    setBackground(colorUpdate);
                }else if (stringStatus.equalsIgnoreCase(CommonConstants.TOSTATUS_DELETE)){
                    setBackground(colorDelete);
                }else{
                    setBackground(colorOthers);
                }
                /* Set oquae */
                this.setOpaque(true);
                return this;
            }
        };
        tblViewLog.setDefaultRenderer(Object.class, renderer);
    }//GEN-LAST:event_btnViewActionPerformed
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new ViewLogUI().show();
    }
    
    // To Reset the Radio Buttons in the UI after any operation, We've to
    //1. Remove the Radio Buttons fron the Radio Groups...
    //2. Add the Radio Buttons Back in The Radio Groups...
    //a.) To Remove the Radio buttons...
    private void removeRadioButtons() {
        rdgActivity.remove(rdoNew);
        rdgActivity.remove(rdoEdit);
        rdgActivity.remove(rdoDelete);
        rdgActivity.remove(rdoAll);
    }
    
    // b.) To Add the Radio buttons...
    private void addRadioButtons() {
        rdgActivity = new CButtonGroup();
        rdgActivity.add(rdoNew);
        rdgActivity.add(rdoEdit);
        rdgActivity.add(rdoDelete);
        rdgActivity.add(rdoAll);
    }
    
    /** To remove the datas from the View Log Table if any  */
    public void removeTableViewLogRow(){
        if (tblViewLog.getRowCount() > 0){
            observable.removeViewLogRow();
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnClear;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboActivity;
    private com.see.truetransact.uicomponent.CComboBox cboBranchID;
    private com.see.truetransact.uicomponent.CComboBox cboFindbyHistory;
    private com.see.truetransact.uicomponent.CComboBox cboIPAddress;
    private com.see.truetransact.uicomponent.CComboBox cboModule;
    private com.see.truetransact.uicomponent.CComboBox cboScreen;
    private com.see.truetransact.uicomponent.CComboBox cboUserID;
    private com.see.truetransact.uicomponent.CDateField dateFromDate;
    private com.see.truetransact.uicomponent.CDateField dateToDate;
    private com.see.truetransact.uicomponent.CLabel lblActivity;
    private com.see.truetransact.uicomponent.CLabel lblBranchID;
    private com.see.truetransact.uicomponent.CLabel lblColorAll;
    private com.see.truetransact.uicomponent.CLabel lblColorDelete;
    private com.see.truetransact.uicomponent.CLabel lblColorEdit;
    private com.see.truetransact.uicomponent.CLabel lblColorNew;
    private com.see.truetransact.uicomponent.CLabel lblFindbyHistory;
    private com.see.truetransact.uicomponent.CLabel lblFromDate;
    private com.see.truetransact.uicomponent.CLabel lblIPAddress;
    private com.see.truetransact.uicomponent.CLabel lblLatestEntries;
    private com.see.truetransact.uicomponent.CLabel lblModule;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblScreen;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace24;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblToDate;
    private com.see.truetransact.uicomponent.CLabel lblUserID;
    private com.see.truetransact.uicomponent.CMenuBar mbrViewLog;
    private javax.swing.JMenuItem mitMultipleIP;
    private javax.swing.JMenuItem mitMultipleUsers;
    private javax.swing.JMenu mnuTableOutput;
    private com.see.truetransact.uicomponent.CPanel panClear;
    private com.see.truetransact.uicomponent.CPanel panFields;
    private com.see.truetransact.uicomponent.CPanel panMain;
    private com.see.truetransact.uicomponent.CPanel panRadioButton;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panViewLogTable;
    private com.see.truetransact.uicomponent.CButtonGroup rdgActivity;
    private com.see.truetransact.uicomponent.CRadioButton rdoAll;
    private com.see.truetransact.uicomponent.CRadioButton rdoDelete;
    private com.see.truetransact.uicomponent.CRadioButton rdoEdit;
    private com.see.truetransact.uicomponent.CRadioButton rdoNew;
    private com.see.truetransact.uicomponent.CSeparator sptFields;
    private com.see.truetransact.uicomponent.CScrollPane srpViewLog;
    private com.see.truetransact.uicomponent.CTable tblViewLog;
    private javax.swing.JToolBar tbrConfig;
    private com.see.truetransact.uicomponent.CTextField txtLatestEntries;
    // End of variables declaration//GEN-END:variables
    
}