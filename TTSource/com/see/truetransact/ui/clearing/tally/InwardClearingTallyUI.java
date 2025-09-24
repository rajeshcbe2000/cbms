/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * InwardClearingTallyUI.java
 *
 * Created on March 17, 2004, 4:24 PM
 */

package com.see.truetransact.ui.clearing.tally;

import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientproxy.ProxyParameters;

import com.see.truetransact.uicomponent.COptionPane;

import java.util.HashMap;
import java.util.List;
import java.util.Observer;
import java.util.Observable;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;


/**
 *
 * @author  rahul
 * modified by Annamalai
 * @modified Bala
 */
public class InwardClearingTallyUI extends CInternalFrame implements java.util.Observer, UIMandatoryField {
    private HashMap mandatoryMap;
    private InwardClearingTallyOB observable;
    private InwardClearingTallyRB resourceBundle = new InwardClearingTallyRB();
    
    final int EDIT=0, DELETE=1, VIEW = 2;
    int viewType=-1;
    final String INR = "Indian Rupees";
    
    private EnhancedTableModel tbmInstDet;
    private EnhancedTableModel tbmDifference;
    Date curDate = null;
    
    /** Creates new form InwardClearingTallyUI */
    public InwardClearingTallyUI() {
        curDate = ClientUtil.getCurrentDate();
        initComponents();
        initSetup();
    }
    
    private void initSetup(){
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setObservable();
        initComponentData();// Fill all the combo boxes...
        setTableModel();
        ClientUtil.enableDisable(this, false);// Disables all when the screen appears for the 1st time
        setButtonEnableDisable();// Enables/Disables the necessary buttons and menu items...
        observable.resetForm();// To reset all the fields in UI...
        observable.resetLable();// To reset all the Lables in UI...
        observable.resetStatus();// To reset the Satus in the UI...
        setHelpMessage();
        setMaxLenths();
    }
    
    /*
     *Setting tables' model with OB table Model.
     */
    private void setTableModel() {
        tbmInstDet = observable.getTbmInstDet();
        tbmDifference = observable.getTbmDifference();
        tblInstDet.setModel(tbmInstDet);
        tblDifference.setModel(tbmDifference);
    }
    // Creates The Instance of InwardClearingOB
    private void setObservable() {
        observable = InwardClearingTallyOB.getInstance();
        observable.addObserver(this);
    }
    
    private void setFieldNames() {
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnSave.setName("btnSave");
        cboClearingType.setName("cboClearingType");
        cboCurrencyBID.setName("cboCurrencyBID");
        cboCurrencyPC.setName("cboCurrencyPC");
        lblClearingDate.setName("lblClearingDate");
        lblClearingType.setName("lblClearingType");
        lblMsg.setName("lblMsg");
        lblPhysicalAmount.setName("lblPhysicalAmount");
        lblPhysicalInstruments.setName("lblPhysicalInstruments");
        lblScheduleNo.setName("lblScheduleNo");
        lblServiceAmount.setName("lblServiceAmount");
        lblServiceInstruments.setName("lblServiceInstruments");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblStatus.setName("lblStatus");
        mbrMain.setName("mbrMain");
        panInwardClearingTally.setName("panInwardClearingTally");
        panPhysicalCount.setName("panPhysicalCount");
        panSchedule.setName("panSchedule");
        panServiceBranch.setName("panServiceBranch");
        panStatus.setName("panStatus");
        tdtClearingDate.setName("tdtClearingDate");
        txtPhysicalAmount.setName("txtPhysicalAmount");
        txtPhysicalInstruments.setName("txtPhysicalInstruments");
        txtScheduleNo.setName("txtScheduleNo");
        txtServiceAmount.setName("txtServiceAmount");
        txtServiceInstruments.setName("txtServiceInstruments");
    }
    
    private void internationalize() {
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        ((javax.swing.border.TitledBorder)panServiceBranch.getBorder()).setTitle(resourceBundle.getString("panServiceBranch"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        lblScheduleNo.setText(resourceBundle.getString("lblScheduleNo"));
        lblPhysicalInstruments.setText(resourceBundle.getString("lblPhysicalInstruments"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblClearingType.setText(resourceBundle.getString("lblClearingType"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        lblClearingDate.setText(resourceBundle.getString("lblClearingDate"));
        ((javax.swing.border.TitledBorder)panPhysicalCount.getBorder()).setTitle(resourceBundle.getString("panPhysicalCount"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblServiceInstruments.setText(resourceBundle.getString("lblServiceInstruments"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblServiceAmount.setText(resourceBundle.getString("lblServiceAmount"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblPhysicalAmount.setText(resourceBundle.getString("lblPhysicalAmount"));
    }
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboClearingType", new Boolean(true));
        mandatoryMap.put("cboCurrencyBID", new Boolean(true));
        mandatoryMap.put("cboCurrencyPC", new Boolean(true));
        mandatoryMap.put("txtScheduleNo", new Boolean(true));
        mandatoryMap.put("tdtClearingDate", new Boolean(true));
        mandatoryMap.put("txtServiceInstruments", new Boolean(true));
        mandatoryMap.put("txtServiceAmount", new Boolean(true));
        mandatoryMap.put("txtDifferenveInstrument", new Boolean(true));
        mandatoryMap.put("txtDifferenceAmount", new Boolean(true));
        mandatoryMap.put("txtPhysicalInstruments", new Boolean(true));
        mandatoryMap.put("txtPhysicalAmount", new Boolean(true));
    }
    
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    public void update(Observable observed, Object arg) {
        cboClearingType.setSelectedItem(observable.getCboClearingType());
        txtScheduleNo.setText(observable.getTxtScheduleNo());
        tdtClearingDate.setDateValue(observable.getTdtClearingDate());
        txtServiceInstruments.setText(observable.getTxtServiceInstruments());
        txtServiceAmount.setText(observable.getTxtServiceAmount());
        txtPhysicalInstruments.setText(observable.getTxtPhysicalInstruments());
        txtPhysicalAmount.setText(observable.getTxtPhysicalAmount());
        
        cboCurrencyBID.setSelectedItem(observable.getCboCurrencyBID());
        cboCurrencyPC.setSelectedItem(observable.getCboCurrencyPC());
        // To set the System Values...
        
        tbmInstDet = observable.getTbmInstDet();
        tbmDifference = observable.getTbmDifference();
        
        //To set the Status...
        lblStatus.setText(observable.getLblStatus());
    }
    
    public void updateOBFields() {
        observable.setScreen(getScreen());
        observable.setModule(getModule());
        
        observable.setCboClearingType((String) cboClearingType.getSelectedItem());
        observable.setTxtScheduleNo(txtScheduleNo.getText());
        observable.setTdtClearingDate(tdtClearingDate.getDateValue());
        observable.setTxtServiceInstruments(txtServiceInstruments.getText());
        observable.setTxtServiceAmount(txtServiceAmount.getText());
        observable.setTxtPhysicalInstruments(txtPhysicalInstruments.getText());
        observable.setTxtPhysicalAmount(txtPhysicalAmount.getText());
        //To set the System Values...
        
    }
    
    public void setHelpMessage() {
        InwardClearingTallyMRB objMandatoryRB = new InwardClearingTallyMRB();
        cboClearingType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboClearingType"));
        txtScheduleNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtScheduleNo"));
        tdtClearingDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtClearingDate"));
        txtServiceInstruments.setHelpMessage(lblMsg, objMandatoryRB.getString("txtServiceInstruments"));
        txtServiceAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtServiceAmount"));
        txtPhysicalInstruments.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPhysicalInstruments"));
        txtPhysicalAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPhysicalAmount"));
    }
    
    // To fill the Data into the Combo Boxes...
    // it invokes the Combo Box model defined in OB class...
    private void initComponentData() {
        cboClearingType.setModel(observable.getCbmClearingType());
        cboCurrencyBID.setModel(observable.getCbmCurrencyBID());
        cboCurrencyPC.setModel(observable.getCbmCurrencyPC());
//        txtScheduleNo.setEditable(false);
    }
    
    // To set The Value of the Buttons Depending on the Value or Condition...
    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        btnDelete.setEnabled(!btnDelete.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());
        
        btnSave.setEnabled(!btnSave.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        btnNewBID.setEnabled(!btnNewBID.isEnabled());
        btnNewPC.setEnabled(!btnNewPC.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
        btnClsSchedule.setEnabled(false);
    }
    
    
    //To set the length of the Text Fields and Numeric Validation...
    private void setMaxLenths() {
        txtScheduleNo.setMaxLength(16);
        txtScheduleNo.setAllowNumber(true);
        
        txtServiceInstruments.setValidation(new NumericValidation(8, 0));
        txtServiceAmount.setValidation(new CurrencyValidation(14, 2));
        
        txtPhysicalInstruments.setValidation(new NumericValidation(8,0));
        txtPhysicalAmount.setValidation(new CurrencyValidation(14, 2));
    }
    
    //updating UI componentd inside PanBID with observable values.
    public void updateBIDComps() {
        txtServiceInstruments.setText(observable.getTxtServiceInstruments());
        txtServiceAmount.setText(observable.getTxtServiceAmount());
    }
    
    //updating UI componentd inside PanBID with observable values.
    public void updatePCComps() {
        txtPhysicalInstruments.setText(observable.getTxtPhysicalInstruments());
        txtPhysicalAmount.setText(observable.getTxtPhysicalAmount());
    }
    
    //Updating OB Fields for BID Panel.
    public void updateBIDOBFields() {
        observable.setCboCurrencyBID((String)((ComboBoxModel)cboCurrencyBID.getModel()).getKeyForSelected());
        observable.setTxtServiceInstruments(txtServiceInstruments.getText());
        observable.setTxtServiceAmount(txtServiceAmount.getText());
    }
    
    //Updating OB Fields for Physical count Panel.
    public void updatePCOBFields() {
        observable.setCboCurrencyPC((String)((ComboBoxModel)cboCurrencyPC.getModel()).getKeyForSelected());
        observable.setTxtPhysicalInstruments(txtPhysicalInstruments.getText());
        observable.setTxtPhysicalAmount(txtPhysicalAmount.getText());
    }
    
    private void enableDisableButtonsBID(boolean enableDisable) {
        btnNewBID.setEnabled(!enableDisable);
        btnSaveBID.setEnabled(enableDisable);
        btnClearBID.setEnabled(enableDisable);
    }
    
    private void enableDisableButtonsBIDDefault() {
        btnNewBID.setEnabled(false);
        btnSaveBID.setEnabled(false);
        btnClearBID.setEnabled(false);
    }
    
    private void enableDisableButtonsPC() {
        btnNewPC.setEnabled(!btnNewPC.isEnabled());
        btnSavePC.setEnabled(!btnNewPC.isEnabled());
        btnClearPC.setEnabled(!btnNewPC.isEnabled());
    }
    
    private void enableDisableButtonsPCDefault() {
        btnNewPC.setEnabled(false);
        btnSavePC.setEnabled(false);
        btnClearPC.setEnabled(false);
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panInwardClearingTally = new com.see.truetransact.uicomponent.CPanel();
        panSchedule = new com.see.truetransact.uicomponent.CPanel();
        lblClearingType = new com.see.truetransact.uicomponent.CLabel();
        cboClearingType = new com.see.truetransact.uicomponent.CComboBox();
        lblScheduleNo = new com.see.truetransact.uicomponent.CLabel();
        txtScheduleNo = new com.see.truetransact.uicomponent.CTextField();
        lblClearingDate = new com.see.truetransact.uicomponent.CLabel();
        tdtClearingDate = new com.see.truetransact.uicomponent.CDateField();
        panServiceBranch = new com.see.truetransact.uicomponent.CPanel();
        lblServiceInstruments = new com.see.truetransact.uicomponent.CLabel();
        txtServiceInstruments = new com.see.truetransact.uicomponent.CTextField();
        lblServiceAmount = new com.see.truetransact.uicomponent.CLabel();
        txtServiceAmount = new com.see.truetransact.uicomponent.CTextField();
        panBtnsBID = new com.see.truetransact.uicomponent.CPanel();
        btnNewBID = new com.see.truetransact.uicomponent.CButton();
        btnSaveBID = new com.see.truetransact.uicomponent.CButton();
        btnClearBID = new com.see.truetransact.uicomponent.CButton();
        lblCurrencyBID = new com.see.truetransact.uicomponent.CLabel();
        cboCurrencyBID = new com.see.truetransact.uicomponent.CComboBox();
        panPhysicalCount = new com.see.truetransact.uicomponent.CPanel();
        lblPhysicalInstruments = new com.see.truetransact.uicomponent.CLabel();
        txtPhysicalInstruments = new com.see.truetransact.uicomponent.CTextField();
        lblPhysicalAmount = new com.see.truetransact.uicomponent.CLabel();
        txtPhysicalAmount = new com.see.truetransact.uicomponent.CTextField();
        panBtnsPC = new com.see.truetransact.uicomponent.CPanel();
        btnNewPC = new com.see.truetransact.uicomponent.CButton();
        btnSavePC = new com.see.truetransact.uicomponent.CButton();
        btnClearPC = new com.see.truetransact.uicomponent.CButton();
        cboCurrencyPC = new com.see.truetransact.uicomponent.CComboBox();
        lblCurrencyPC = new com.see.truetransact.uicomponent.CLabel();
        srpInstDet = new com.see.truetransact.uicomponent.CScrollPane();
        tblInstDet = new com.see.truetransact.uicomponent.CTable();
        srpDifference = new com.see.truetransact.uicomponent.CScrollPane();
        tblDifference = new com.see.truetransact.uicomponent.CTable();
        btnClsSchedule = new com.see.truetransact.uicomponent.CButton();
        tbrHead = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace21 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace22 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace23 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace24 = new com.see.truetransact.uicomponent.CLabel();
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
        sptDelete = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setPreferredSize(new java.awt.Dimension(750, 580));

        panInwardClearingTally.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panInwardClearingTally.setPreferredSize(new java.awt.Dimension(668, 453));
        panInwardClearingTally.setLayout(new java.awt.GridBagLayout());

        panSchedule.setLayout(new java.awt.GridBagLayout());

        lblClearingType.setText("Clearing Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSchedule.add(lblClearingType, gridBagConstraints);

        cboClearingType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboClearingType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboClearingTypeActionPerformed(evt);
            }
        });
        panSchedule.add(cboClearingType, new java.awt.GridBagConstraints());

        lblScheduleNo.setText("Schedule Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSchedule.add(lblScheduleNo, gridBagConstraints);

        txtScheduleNo.setEditable(false);
        txtScheduleNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtScheduleNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtScheduleNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSchedule.add(txtScheduleNo, gridBagConstraints);

        lblClearingDate.setText("Clearing Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSchedule.add(lblClearingDate, gridBagConstraints);

        tdtClearingDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtClearingDate.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtClearingDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtClearingDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSchedule.add(tdtClearingDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInwardClearingTally.add(panSchedule, gridBagConstraints);

        panServiceBranch.setBorder(javax.swing.BorderFactory.createTitledBorder("Branch Instruments' Details"));
        panServiceBranch.setLayout(new java.awt.GridBagLayout());

        lblServiceInstruments.setText("Number of Instrument");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panServiceBranch.add(lblServiceInstruments, gridBagConstraints);

        txtServiceInstruments.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panServiceBranch.add(txtServiceInstruments, gridBagConstraints);

        lblServiceAmount.setText("Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panServiceBranch.add(lblServiceAmount, gridBagConstraints);

        txtServiceAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panServiceBranch.add(txtServiceAmount, gridBagConstraints);

        panBtnsBID.setLayout(new java.awt.GridBagLayout());

        btnNewBID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNewBID.setMinimumSize(new java.awt.Dimension(35, 26));
        btnNewBID.setPreferredSize(new java.awt.Dimension(35, 26));
        btnNewBID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewBIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panBtnsBID.add(btnNewBID, gridBagConstraints);

        btnSaveBID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSaveBID.setMinimumSize(new java.awt.Dimension(35, 26));
        btnSaveBID.setPreferredSize(new java.awt.Dimension(35, 26));
        btnSaveBID.setEnabled(false);
        btnSaveBID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveBIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panBtnsBID.add(btnSaveBID, gridBagConstraints);

        btnClearBID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnClearBID.setMinimumSize(new java.awt.Dimension(35, 26));
        btnClearBID.setPreferredSize(new java.awt.Dimension(35, 26));
        btnClearBID.setEnabled(false);
        btnClearBID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearBIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panBtnsBID.add(btnClearBID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        panServiceBranch.add(panBtnsBID, gridBagConstraints);

        lblCurrencyBID.setText("Currency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panServiceBranch.add(lblCurrencyBID, gridBagConstraints);

        cboCurrencyBID.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCurrencyBID.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboCurrencyBIDItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panServiceBranch.add(cboCurrencyBID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInwardClearingTally.add(panServiceBranch, gridBagConstraints);

        panPhysicalCount.setBorder(javax.swing.BorderFactory.createTitledBorder("Physical Count"));
        panPhysicalCount.setLayout(new java.awt.GridBagLayout());

        lblPhysicalInstruments.setText("Number of Instrument");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPhysicalCount.add(lblPhysicalInstruments, gridBagConstraints);

        txtPhysicalInstruments.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPhysicalCount.add(txtPhysicalInstruments, gridBagConstraints);

        lblPhysicalAmount.setText("Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPhysicalCount.add(lblPhysicalAmount, gridBagConstraints);

        txtPhysicalAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPhysicalCount.add(txtPhysicalAmount, gridBagConstraints);

        panBtnsPC.setLayout(new java.awt.GridBagLayout());

        btnNewPC.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNewPC.setMinimumSize(new java.awt.Dimension(35, 26));
        btnNewPC.setPreferredSize(new java.awt.Dimension(35, 26));
        btnNewPC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewPCActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panBtnsPC.add(btnNewPC, gridBagConstraints);

        btnSavePC.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSavePC.setMinimumSize(new java.awt.Dimension(35, 26));
        btnSavePC.setPreferredSize(new java.awt.Dimension(35, 26));
        btnSavePC.setEnabled(false);
        btnSavePC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSavePCActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panBtnsPC.add(btnSavePC, gridBagConstraints);

        btnClearPC.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnClearPC.setMinimumSize(new java.awt.Dimension(35, 26));
        btnClearPC.setPreferredSize(new java.awt.Dimension(35, 26));
        btnClearPC.setEnabled(false);
        btnClearPC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearPCActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panBtnsPC.add(btnClearPC, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        panPhysicalCount.add(panBtnsPC, gridBagConstraints);

        cboCurrencyPC.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCurrencyPC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCurrencyPCActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPhysicalCount.add(cboCurrencyPC, gridBagConstraints);

        lblCurrencyPC.setText("Currency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPhysicalCount.add(lblCurrencyPC, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInwardClearingTally.add(panPhysicalCount, gridBagConstraints);

        srpInstDet.setBorder(javax.swing.BorderFactory.createTitledBorder("Instruments' Details "));
        srpInstDet.setMinimumSize(new java.awt.Dimension(725, 100));
        srpInstDet.setPreferredSize(new java.awt.Dimension(725, 100));

        tblInstDet.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Currency", "No of Inst System ", "Insts' Amt System", "No of Inst Branch", "Insts' Amt Branch", "No of Inst PC", "Insts' Amt PC"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        srpInstDet.setViewportView(tblInstDet);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInwardClearingTally.add(srpInstDet, gridBagConstraints);

        srpDifference.setBorder(javax.swing.BorderFactory.createTitledBorder(" Difference Details"));
        srpDifference.setMinimumSize(new java.awt.Dimension(725, 100));
        srpDifference.setPreferredSize(new java.awt.Dimension(725, 100));

        tblDifference.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Currency", "No of Instruments", "Instruments' Amount "
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        srpDifference.setViewportView(tblDifference);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInwardClearingTally.add(srpDifference, gridBagConstraints);

        btnClsSchedule.setText("Close Schedule");
        btnClsSchedule.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClsScheduleActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInwardClearingTally.add(btnClsSchedule, gridBagConstraints);

        getContentPane().add(panInwardClearingTally, java.awt.BorderLayout.CENTER);

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
        tbrHead.add(btnView);

        lblSpace4.setText("     ");
        tbrHead.add(lblSpace4);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrHead.add(btnNew);

        lblSpace21.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace21.setText("     ");
        lblSpace21.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace21);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrHead.add(btnEdit);

        lblSpace22.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace22.setText("     ");
        lblSpace22.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace22);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrHead.add(btnDelete);

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

        lblSpace23.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace23.setText("     ");
        lblSpace23.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace23.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace23.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace23);

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

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrHead.add(btnPrint);

        lblSpace24.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace24.setText("     ");
        lblSpace24.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace24);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrHead.add(btnClose);

        getContentPane().add(tbrHead, java.awt.BorderLayout.NORTH);

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

        mitDelete.setText("Delete");
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

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        popUp(VIEW);
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed
    private void tdtClearingDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtClearingDateFocusLost
        ClientUtil.validateToDate(tdtClearingDate, DateUtil.getStringDate(DateUtil.addDays(curDate, -1)));
        if( observable.getCboClearingType().length() > 0){
            String strMsg = checkClearingTypeExist();
            if (strMsg.length() > 0)
                displayAlert(strMsg);
        }
    }//GEN-LAST:event_tdtClearingDateFocusLost
            private void btnClsScheduleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClsScheduleActionPerformed
                String tallyId = CommonUtil.convertObjToStr(observable.getLblTallyId());
                System.out.println("tallyId : " + tallyId);
                boolean isError=false;
                if (observable.getActionType() != ClientConstants.ACTIONTYPE_NEW && !tallyId.equals("")) {
                    HashMap map = new HashMap();
                    map.put("SCHEDULE_NO", txtScheduleNo.getText());
                    map.put("CLEARING_TYPE", cboClearingType.getSelectedItem());
                    map.put("BRANCH_ID", getSelectedBranchID());
                    ArrayList arrayList=new ArrayList();
                    arrayList= tbmDifference.getDataArrayList();
                    System.out.println("TablearrayList ###"+arrayList);
                    ArrayList lst=new ArrayList();
                    lst=(ArrayList)arrayList.get(0);
                    observable.setMode("CLOSE");
                    if(CommonUtil.convertObjToInt(lst.get(1))!=0.0 && CommonUtil.convertObjToInt(lst.get(2))!=0.0){
                        System.out.println("TablearrayList ###");
                        double shortExcessAmt=CommonUtil.convertObjToDouble(lst.get(2)).doubleValue();
                        if(shortExcessAmt>0){
                            observable.setExcessAmt(shortExcessAmt);
                        }else{
//                            shortExcessAmt=shortExcessAmt*-1;
                            observable.setShortAmt(shortExcessAmt);
                        }
                        observable.setMode("CLOSE");
                        isError=false;//true
                        ClientUtil.showMessageWindow(resourceBundle.getString("checkTally"));
                    }
                    //            int cnt=tblDifference.getRowCount();
                    //            if(cnt!=0){
                    //                isError=true;
                    //                ClientUtil.showMessageWindow(resourceBundle.getString("checkTally"));
                    //            }
                    List tallyCount = ClientUtil.executeQuery("checkInwardTallyCount", map);
                    
                    if (CommonUtil.convertObjToInt(tallyCount.get(0)) > 0) {
                        ClientUtil.showMessageWindow(resourceBundle.getString("checkPending"));
                        isError=true;
                    } else  if(!isError){
                        int yesno = COptionPane.showConfirmDialog(this, resourceBundle.getString("WarningForCloser"), "Schedule", COptionPane.YES_NO_OPTION);
                        if (yesno == COptionPane.YES_OPTION) {
                           
                            
//                            HashMap closeMap = new HashMap();
//                            closeMap.put("TALLY_ID", tallyId);
//                            closeMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
//                            ClientUtil.execute("closeInwardClearingTallyTO", closeMap);
                            observable.doAction();
                             btnCancelActionPerformed(null);
                        }
                    }
                } else {
                    ClientUtil.showMessageWindow(resourceBundle.getString("checkCreated"));
                }
    }//GEN-LAST:event_btnClsScheduleActionPerformed
                private void btnNewPCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewPCActionPerformed
                    ClientUtil.enableDisable(panPhysicalCount,true);
                    enableDisableButtonsPC();
                    cboCurrencyPC.setSelectedItem(INR);
                    cboCurrencyPC.setEnabled(false);
    }//GEN-LAST:event_btnNewPCActionPerformed
                    private void btnClearPCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearPCActionPerformed
                        observable.resetPCPanel();
                        updatePCComps();
                        cboCurrencyPC.setSelectedItem("");
                        ClientUtil.enableDisable(panPhysicalCount,false);
                        enableDisableButtonsPC();
                        // Add your handling code here:
    }//GEN-LAST:event_btnClearPCActionPerformed
                    
    private void btnSavePCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSavePCActionPerformed
        final String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panPhysicalCount);
        if(mandatoryMessage.length()>0) {
            displayAlert(mandatoryMessage);
            return;
        }
        updatePCOBFields();
        observable.savePC2TbmInstDet();
        //observable.setDifference((String)((ComboBoxModel)cboCurrencyPC.getModel()).getKeyForSelected());
        btnClearPCActionPerformed(null);
        // Add your handling code here:
    }//GEN-LAST:event_btnSavePCActionPerformed
    
    private void cboCurrencyPCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCurrencyPCActionPerformed
        if(cboCurrencyPC.getSelectedItem()==null || ((String)cboCurrencyPC.getSelectedItem()).equals("")) {
            return;
        }
        observable.populatePCData4Currency((String)((ComboBoxModel)cboCurrencyPC.getModel()).getKeyForSelected());
        updatePCComps();
        // Add your handling code here:
    }//GEN-LAST:event_cboCurrencyPCActionPerformed
    
    private void btnNewBIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewBIDActionPerformed
        ClientUtil.enableDisable(panServiceBranch,true);
        enableDisableButtonsBID(true);
        cboCurrencyBID.setSelectedItem(INR);
        cboCurrencyBID.setEnabled(false);
        // Add your handling code here:
    }//GEN-LAST:event_btnNewBIDActionPerformed
    
    private void btnClearBIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearBIDActionPerformed
        observable.resetBIDPanel();
        updateBIDComps();
        cboCurrencyBID.setSelectedItem("");
        ClientUtil.enableDisable(panServiceBranch,false);
        enableDisableButtonsBID(false);
        // Add your handling code here:
    }//GEN-LAST:event_btnClearBIDActionPerformed
    
    private void btnSaveBIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveBIDActionPerformed
        final String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panServiceBranch);
        if(mandatoryMessage.length()>0) {
            displayAlert(mandatoryMessage);
            return;
        }
        updateBIDOBFields();
        observable.save2TbmInstDet();
        observable.setDifference((String)((ComboBoxModel)cboCurrencyBID.getModel()).getKeyForSelected());
        btnClearBIDActionPerformed(null);
    }//GEN-LAST:event_btnSaveBIDActionPerformed
    
    private void cboCurrencyBIDItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboCurrencyBIDItemStateChanged
        if(cboCurrencyBID.getSelectedItem()==null || ((String)cboCurrencyBID.getSelectedItem()).equals("")) {
            return;
        }
        
        observable.populateSBData4Currency((String)((ComboBoxModel)cboCurrencyBID.getModel()).getKeyForSelected());
        updateBIDComps();
        // Add your handling code here:
    }//GEN-LAST:event_cboCurrencyBIDItemStateChanged
        
    private void btnCheck(){
         btnCancel.setEnabled(true);
         btnSave.setEnabled(false);
         btnNew.setEnabled(false);
         btnDelete.setEnabled(false);
         btnEdit.setEnabled(false);
     }
    /*    private void differenceEvent(){
        if(!observable.getTxtServiceInstruments().equalsIgnoreCase("")
        && !observable.getTxtServiceAmount().equalsIgnoreCase("")){
            try{
                double serviceInstruments = Double.parseDouble(txtServiceInstruments.getText());
                //double systemInstruments = Double.parseDouble(lblBookedInstrument.getText());
         
                double serviceAmount = Double.parseDouble(txtServiceAmount.getText());
                //double systemAmount = Double.parseDouble(lblBookedAmount.getText());
         
                double amount = serviceAmount - systemAmount;
                double instrument = serviceInstruments - systemInstruments;
         
                txtDifferenveInstrument.setText(String.valueOf(instrument));
                txtDifferenceAmount.setText(String.valueOf(amount));
            }catch(Exception e){
            }
        }
    }*/
    private void txtScheduleNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtScheduleNoFocusLost
        updateOBFields();
        boolean ScheduleNumber = false;
        observable.setTxtScheduleNo(txtScheduleNo.getText());
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW
        && !observable.getTxtScheduleNo().equalsIgnoreCase("")){
            ScheduleNumber = observable.checkScheduleNumber();
            if(!ScheduleNumber){
                txtScheduleNo.requestFocus(true);
            }
        }
    }//GEN-LAST:event_txtScheduleNoFocusLost
    private void cboClearingTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboClearingTypeActionPerformed
        observable.setCboClearingType((String) cboClearingType.getSelectedItem());

    }//GEN-LAST:event_cboClearingTypeActionPerformed
    private String checkClearingTypeExist() {
        String retStr = "";
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            HashMap whereMap = new HashMap();
            whereMap.put("CLEARING_TYPE", observable.getCboClearingType());
            whereMap.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
            Date IsDt = DateUtil.getDateMMDDYYYY(tdtClearingDate.getDateValue());
            if(IsDt != null){
            Date isDate = (Date)curDate.clone();
            isDate.setDate(IsDt.getDate());
            isDate.setMonth(IsDt.getMonth());
            isDate.setYear(IsDt.getYear());
            whereMap.put("CLEARING_DT",isDate);
            }else{
                whereMap.put("CLEARING_DT",DateUtil.getDateMMDDYYYY(tdtClearingDate.getDateValue()));
            }
//            whereMap.put("CLEARING_DT",DateUtil.getDateMMDDYYYY(tdtClearingDate.getDateValue()));
            System.out.println("whereMap"+whereMap);
            List lst=null;
            if(tdtClearingDate.getDateValue()!=null){
                  lst = ClientUtil.executeQuery("checkClearingTypeExist", whereMap);
                  if (lst != null && lst.size() > 0) {
                    retStr = resourceBundle.getString("checkClearingTypeExist");
            } 
          }
        }
    return retStr;
    }
        
    /*private void fireEvent(){
        if(!observable.getTxtScheduleNo().equalsIgnoreCase("")
        && ( observable.getCboClearingType().length() > 0)
        /*&& observable.getActionType() == ClientConstants.ACTIONTYPE_NEW* /){
            final HashMap resultMap = observable.setClearingData((String)observable.getCbmClearingType().getKeyForSelected(), observable.getTxtScheduleNo());
            lblBookedInstrument.setText(CommonUtil.convertObjToStr(resultMap.get("INSTRUMENT")));
            lblBookedAmount.setText(CommonUtil.convertObjToStr(resultMap.get("AMOUNT")));
     
            differenceEvent();
        }
    }*/
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // Add your handling code here:
        HashMap reportParamMap = new HashMap();
        com.see.truetransact.clientutil.ttrintegration.LinkReport.getReports(getScreenID(), reportParamMap);
    }//GEN-LAST:event_btnPrintActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        observable.resetLable();                // Reset the Editable Lables in the UI to null...
        observable.resetForm();                 // Reset the fields in the UI to null...
        ClientUtil.enableDisable(this, false);  // Disables the panel...
        setButtonEnableDisable();               // Enables or Disables the buttons and menu Items depending on their previous state...
        enableDisableButtonsBIDDefault();
        enableDisableButtonsPCDefault();
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);        //Sets the Action Type to be performed...
        observable.setStatus();                 // To set the Value of lblStatus...
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        updateOBFields();
        //To display an alert if the mandatory fields are not properly inputted, else proceed with normal operation
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panSchedule);
        mandatoryMessage += checkClearingTypeExist();
        if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0 ){
            displayAlert(mandatoryMessage);
        }
        else{
            observable.doAction();          // To perform the necessary operation depending on the Action type...
            observable.resetForm();         // Reset the fields in the UI to null...
            observable.resetLable();        // Reset the Editable Lables in the UI to null...
            ClientUtil.enableDisable(this, false);      // Disables the panel...
            setButtonEnableDisable();                   // Enables or Disables the buttons and menu Items depending on their previous state...
            observable.setResultStatus();               // To Reset the Value of lblStatus...
        }
    }//GEN-LAST:event_btnSaveActionPerformed
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        observable.resetForm();          // Reset the fields in the UI to null...
        observable.resetLable();        // Reset the Editable Lables in the UI to null...
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);        //Sets the Action Type to be performed...
        popUp(DELETE);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        observable.resetForm();         // Reset the fields in the UI to null...
        observable.resetLable();        // Reset the Editable Lables in the UI to null...
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);      //Sets the Action Type to be performed...
        popUp(EDIT);
    }//GEN-LAST:event_btnEditActionPerformed
    // To display the All the Product Id's which r having status as
    // created or updated, in a table...
    private void popUp(int field) {
        final HashMap viewMap = new HashMap();
        viewType = field;
        if(field==EDIT || field==DELETE || field==VIEW){//Edit=0 and Delete=1
            HashMap whereMap = new HashMap();
            whereMap.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            viewMap.put(CommonConstants.MAP_NAME, "viewInwardClearingTally");
        }
        new ViewAll(this, viewMap).show();
    }
    
    // this method is called automatically from ViewAll...
    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
        if (viewType==EDIT || viewType==DELETE || viewType==VIEW) {
            hash.put("SCHEDULE_NO", hash.get("SCHEDULE NO"));
            hash.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
            
            observable.populateData(hash);// Called to display the Data in the UI fields...
            setButtonEnableDisable();           // Enables or Disables the buttons and menu Items depending on their previous state...
            
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE ||  viewType==VIEW) {
                ClientUtil.enableDisable(this, false);      // Disables the panel...
                enableDisableButtonsBIDDefault();
                enableDisableButtonsPCDefault();
            }else{
                ClientUtil.enableDisable(panSchedule, false);       // Enables the panel...
            }
            
            if (tblInstDet.getRowCount() > 0)
                btnClsSchedule.setEnabled(true);
            
            observable.setStatus();             // To set the Value of lblStatus...
        }
    }
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        observable.resetForm();                  // Reset the fields in the UI to null...
        observable.resetLable();                 // Reset the Editable Lables in the UI to null...
        ClientUtil.enableDisable(panSchedule, true);   // Enables the panel...
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);       //Sets the Action Type to be performed...
        setButtonEnableDisable();               // Enables or Disables the buttons and menu Items depending on their previous state...
        observable.setStatus();                 // To set the Value of lblStatus...
    }//GEN-LAST:event_btnNewActionPerformed
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // Add your handling code here:
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // Add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // Add your handling code here:
        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // Add your handling code here:
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // Add your handling code here:
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        InwardClearingTallyUI ict = new InwardClearingTallyUI();
        javax.swing.JFrame frame = new javax.swing.JFrame();
        frame.getContentPane().add(ict);
        frame.setSize(600,600);
        frame.show();
        ict.show();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClearBID;
    private com.see.truetransact.uicomponent.CButton btnClearPC;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnClsSchedule;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnNewBID;
    private com.see.truetransact.uicomponent.CButton btnNewPC;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnSaveBID;
    private com.see.truetransact.uicomponent.CButton btnSavePC;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboClearingType;
    private com.see.truetransact.uicomponent.CComboBox cboCurrencyBID;
    private com.see.truetransact.uicomponent.CComboBox cboCurrencyPC;
    private com.see.truetransact.uicomponent.CLabel lblClearingDate;
    private com.see.truetransact.uicomponent.CLabel lblClearingType;
    private com.see.truetransact.uicomponent.CLabel lblCurrencyBID;
    private com.see.truetransact.uicomponent.CLabel lblCurrencyPC;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblPhysicalAmount;
    private com.see.truetransact.uicomponent.CLabel lblPhysicalInstruments;
    private com.see.truetransact.uicomponent.CLabel lblScheduleNo;
    private com.see.truetransact.uicomponent.CLabel lblServiceAmount;
    private com.see.truetransact.uicomponent.CLabel lblServiceInstruments;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace21;
    private com.see.truetransact.uicomponent.CLabel lblSpace22;
    private com.see.truetransact.uicomponent.CLabel lblSpace23;
    private com.see.truetransact.uicomponent.CLabel lblSpace24;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panBtnsBID;
    private com.see.truetransact.uicomponent.CPanel panBtnsPC;
    private com.see.truetransact.uicomponent.CPanel panInwardClearingTally;
    private com.see.truetransact.uicomponent.CPanel panPhysicalCount;
    private com.see.truetransact.uicomponent.CPanel panSchedule;
    private com.see.truetransact.uicomponent.CPanel panServiceBranch;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private com.see.truetransact.uicomponent.CScrollPane srpDifference;
    private com.see.truetransact.uicomponent.CScrollPane srpInstDet;
    private com.see.truetransact.uicomponent.CTable tblDifference;
    private com.see.truetransact.uicomponent.CTable tblInstDet;
    private javax.swing.JToolBar tbrHead;
    private com.see.truetransact.uicomponent.CDateField tdtClearingDate;
    private com.see.truetransact.uicomponent.CTextField txtPhysicalAmount;
    private com.see.truetransact.uicomponent.CTextField txtPhysicalInstruments;
    private com.see.truetransact.uicomponent.CTextField txtScheduleNo;
    private com.see.truetransact.uicomponent.CTextField txtServiceAmount;
    private com.see.truetransact.uicomponent.CTextField txtServiceInstruments;
    // End of variables declaration//GEN-END:variables
    
}
