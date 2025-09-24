/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ForexExchangeUI.java
 *
 * Created on May 4, 2004, 4:34 PM
 */

package com.see.truetransact.ui.forex;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uivalidation.NumericValidation;

import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.uicomponent.CButtonGroup;

import java.util.HashMap;
import java.util.Observer;
import java.util.Observable;
import java.util.Date;
/**
 *
 * @author  rahul
 */
public class ForexExchangeUI extends CInternalFrame implements java.util.Observer , UIMandatoryField{
    HashMap mandatoryMap;
    ForexExchangeOB observable;
    private Date currDt = null;
    final int EDIT=0,DELETE=1;
    int viewType=-1;
    /** Creates new form ForexExchangeUI */
    public ForexExchangeUI() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        initSetup();
    }
    
    private void initSetup(){
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setObservable();
        initComponentData();
        setMaxLenths();                        // To set the Numeric Validation and the Maximum length of the Text fields...
        ClientUtil.enableDisable(this, false); // Disables all when the screen appears for the 1st time
        setButtonEnableDisable();              // Enables/Disables the necessary buttons and menu items...
        observable.resetForm();                // To reset all the fields in UI...
        observable.resetLable();               // To reset all the Lables in UI...
        observable.resetTable();               // Reset the table in UI...
        observable.resetStatus();              // To reset the Satus in the UI...        
        setHelpMessage();
    }
    
    private void setObservable() {
        observable = ForexExchangeOB.getInstance();
        observable.addObserver(this);
    }
    
   /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnSave.setName("btnSave");
        cboHours.setName("cboHours");
        cboMinutes.setName("cboMinutes");
        cboTransCurrency.setName("cboTransCurrency");
        lblBaseCurrency.setName("lblBaseCurrency");
        lblBaseCurrencyDesc.setName("lblBaseCurrencyDesc");
        lblExchangeID.setName("lblExchangeID");
        lblExchangeIDDesc.setName("lblExchangeIDDesc");
        lblHours.setName("lblHours");
        lblMiddleRate.setName("lblMiddleRate");
        lblMinutes.setName("lblMinutes");
        lblMsg.setName("lblMsg");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblStatus1.setName("lblStatus1");
        lblTransCurrency.setName("lblTransCurrency");
        lblValidDate.setName("lblValidDate");
        lblValueTime.setName("lblValueTime");
        mbrLoanProduct.setName("mbrLoanProduct");
        panData.setName("panData");
        panForexExchange.setName("panForexExchange");
        panHours.setName("panHours");
        panMinutes.setName("panMinutes");
        panStatus.setName("panStatus");
        panTableData.setName("panTableData");
        panValueTime.setName("panValueTime");
        srpTableData.setName("srpTableData");
        tblTableData.setName("tblTableData");
        tdtValidDate.setName("tdtValidDate");
        txtMiddleRate.setName("txtMiddleRate");
    }
    
    
    
        /* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        ForexExchangeRB resourceBundle = new ForexExchangeRB();
        lblMinutes.setText(resourceBundle.getString("lblMinutes"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblHours.setText(resourceBundle.getString("lblHours"));
        lblExchangeIDDesc.setText(resourceBundle.getString("lblExchangeIDDesc"));
        lblBaseCurrencyDesc.setText(resourceBundle.getString("lblBaseCurrencyDesc"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblStatus1.setText(resourceBundle.getString("lblStatus1"));
        lblMiddleRate.setText(resourceBundle.getString("lblMiddleRate"));
        lblTransCurrency.setText(resourceBundle.getString("lblTransCurrency"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        lblValueTime.setText(resourceBundle.getString("lblValueTime"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblValidDate.setText(resourceBundle.getString("lblValidDate"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        lblBaseCurrency.setText(resourceBundle.getString("lblBaseCurrency"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblExchangeID.setText(resourceBundle.getString("lblExchangeID"));
    }
    
    
    /* Auto Generated Method - setMandatoryHashMap()
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("tdtValidDate", new Boolean(true));
        mandatoryMap.put("cboTransCurrency", new Boolean(true));
        mandatoryMap.put("txtMiddleRate", new Boolean(true));
        mandatoryMap.put("cboHours", new Boolean(true));
        mandatoryMap.put("cboMinutes", new Boolean(true));
    }
    
/* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    
    
        /* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        tdtValidDate.setDateValue(observable.getTdtValidDate());
        cboTransCurrency.setSelectedItem(observable.getCboTransCurrency());
        txtMiddleRate.setText(observable.getTxtMiddleRate());
        cboHours.setSelectedItem(observable.getCboHours());
        cboMinutes.setSelectedItem(observable.getCboMinutes());
        // to set the values in table...
        tblTableData.setModel(observable.getTblForex());
        // To set the Value of Exchange Id and Base Currency...
        lblExchangeIDDesc.setText(observable.getLblExchangeId());
        lblBaseCurrencyDesc.setText(observable.getLblBaseCurrency());
        //To set the Status...
        lblStatus1.setText(observable.getLblStatus());
    }
    
        /* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setTdtValidDate(tdtValidDate.getDateValue());
        observable.setCboTransCurrency((String) cboTransCurrency.getSelectedItem());
        observable.setTxtMiddleRate(txtMiddleRate.getText());
        observable.setCboHours((String) cboHours.getSelectedItem());
        observable.setCboMinutes((String) cboMinutes.getSelectedItem());
        
        // To set the Value of Exchange Id and Base Currency..
        observable.setLblExchangeId(lblExchangeIDDesc.getText());
        observable.setLblBaseCurrency(lblBaseCurrencyDesc.getText());
        observable.setTblForex((com.see.truetransact.clientutil.EnhancedTableModel)tblTableData.getModel());
    }
    
        /* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        ForexExchangeMRB objMandatoryRB = new ForexExchangeMRB();
        tdtValidDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtValidDate"));
        cboTransCurrency.setHelpMessage(lblMsg, objMandatoryRB.getString("cboTransCurrency"));
        txtMiddleRate.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMiddleRate"));
        cboHours.setHelpMessage(lblMsg, objMandatoryRB.getString("cboHours"));
        cboMinutes.setHelpMessage(lblMsg, objMandatoryRB.getString("cboMinutes"));
    }
    
    // To fill the Data into the Combo Boxes...
    // it invokes the Combo Box model defined in OB class...
    private void initComponentData() {
        cboTransCurrency.setModel(observable.getCbmTransCurrency());
        cboHours.setModel(observable.getCbmHours());
        cboMinutes.setModel(observable.getCbmMinutes());
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
    }
    
    //To set the length of the Text Fields and Numeric Validation...
    private void setMaxLenths() {
        txtMiddleRate.setMaxLength(16);
        txtMiddleRate.setValidation(new NumericValidation());
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panForexExchange = new com.see.truetransact.uicomponent.CPanel();
        panData = new com.see.truetransact.uicomponent.CPanel();
        lblValidDate = new com.see.truetransact.uicomponent.CLabel();
        tdtValidDate = new com.see.truetransact.uicomponent.CDateField();
        lblBaseCurrency = new com.see.truetransact.uicomponent.CLabel();
        lblBaseCurrencyDesc = new com.see.truetransact.uicomponent.CLabel();
        lblTransCurrency = new com.see.truetransact.uicomponent.CLabel();
        cboTransCurrency = new com.see.truetransact.uicomponent.CComboBox();
        lblMiddleRate = new com.see.truetransact.uicomponent.CLabel();
        txtMiddleRate = new com.see.truetransact.uicomponent.CTextField();
        lblExchangeID = new com.see.truetransact.uicomponent.CLabel();
        lblExchangeIDDesc = new com.see.truetransact.uicomponent.CLabel();
        lblValueTime = new com.see.truetransact.uicomponent.CLabel();
        panValueTime = new com.see.truetransact.uicomponent.CPanel();
        panHours = new com.see.truetransact.uicomponent.CPanel();
        cboHours = new com.see.truetransact.uicomponent.CComboBox();
        lblHours = new com.see.truetransact.uicomponent.CLabel();
        panMinutes = new com.see.truetransact.uicomponent.CPanel();
        cboMinutes = new com.see.truetransact.uicomponent.CComboBox();
        lblMinutes = new com.see.truetransact.uicomponent.CLabel();
        panTableData = new com.see.truetransact.uicomponent.CPanel();
        srpTableData = new com.see.truetransact.uicomponent.CScrollPane();
        tblTableData = new com.see.truetransact.uicomponent.CTable();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus1 = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        tbrLoantProduct = new javax.swing.JToolBar();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace24 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace25 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace26 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace27 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        mbrLoanProduct = new com.see.truetransact.uicomponent.CMenuBar();
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

        panForexExchange.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panForexExchange.setLayout(new java.awt.GridBagLayout());

        panData.setLayout(new java.awt.GridBagLayout());

        lblValidDate.setText("Value Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(lblValidDate, gridBagConstraints);

        tdtValidDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtValidDate.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtValidDate.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                tdtValidDateAncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(tdtValidDate, gridBagConstraints);

        lblBaseCurrency.setText("Base Currency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(lblBaseCurrency, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(lblBaseCurrencyDesc, gridBagConstraints);

        lblTransCurrency.setText("Transaction Currency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(lblTransCurrency, gridBagConstraints);

        cboTransCurrency.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(cboTransCurrency, gridBagConstraints);

        lblMiddleRate.setText("Middle Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(lblMiddleRate, gridBagConstraints);

        txtMiddleRate.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(txtMiddleRate, gridBagConstraints);

        lblExchangeID.setText("Exchange ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(lblExchangeID, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(lblExchangeIDDesc, gridBagConstraints);

        lblValueTime.setText("Value Time");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(lblValueTime, gridBagConstraints);

        panValueTime.setLayout(new java.awt.GridBagLayout());

        panHours.setLayout(new java.awt.GridBagLayout());

        cboHours.setMinimumSize(new java.awt.Dimension(50, 21));
        cboHours.setPreferredSize(new java.awt.Dimension(50, 21));
        cboHours.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboHoursActionPerformed(evt);
            }
        });
        panHours.add(cboHours, new java.awt.GridBagConstraints());

        lblHours.setText("hrs");
        panHours.add(lblHours, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panValueTime.add(panHours, gridBagConstraints);

        panMinutes.setLayout(new java.awt.GridBagLayout());

        cboMinutes.setMinimumSize(new java.awt.Dimension(50, 21));
        cboMinutes.setPreferredSize(new java.awt.Dimension(50, 21));
        panMinutes.add(cboMinutes, new java.awt.GridBagConstraints());

        lblMinutes.setText("mins");
        panMinutes.add(lblMinutes, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panValueTime.add(panMinutes, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(panValueTime, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panForexExchange.add(panData, gridBagConstraints);

        srpTableData.setMinimumSize(new java.awt.Dimension(350, 152));
        srpTableData.setPreferredSize(new java.awt.Dimension(350, 152));
        srpTableData.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                srpTableDataAncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        tblTableData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Value Date", "Base Currency", "Transaction Date", "Middle Rate"
            }
        ));
        srpTableData.setViewportView(tblTableData);

        panTableData.add(srpTableData);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panForexExchange.add(panTableData, gridBagConstraints);

        getContentPane().add(panForexExchange, java.awt.BorderLayout.CENTER);

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace1.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace1, gridBagConstraints);

        lblStatus1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblStatus1.setText("                      ");
        lblStatus1.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                lblStatus1AncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblStatus1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblMsg, gridBagConstraints);

        getContentPane().add(panStatus, java.awt.BorderLayout.SOUTH);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnNew);

        lblSpace24.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace24.setText("     ");
        lblSpace24.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace24);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnEdit);

        lblSpace25.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace25.setText("     ");
        lblSpace25.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace25.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace25.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace25);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnDelete);

        lblSpace2.setText("     ");
        tbrLoantProduct.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnSave);

        lblSpace26.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace26.setText("     ");
        lblSpace26.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace26);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnCancel);

        lblSpace3.setText("     ");
        tbrLoantProduct.add(lblSpace3);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnPrint);

        lblSpace27.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace27.setText("     ");
        lblSpace27.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace27);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnClose);

        getContentPane().add(tbrLoantProduct, java.awt.BorderLayout.NORTH);

        mnuProcess.setText("Process");

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

        mbrLoanProduct.add(mnuProcess);

        setJMenuBar(mbrLoanProduct);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lblStatus1AncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_lblStatus1AncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_lblStatus1AncestorAdded

    private void tdtValidDateAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_tdtValidDateAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_tdtValidDateAncestorAdded

    private void srpTableDataAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_srpTableDataAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_srpTableDataAncestorAdded

    private void cboHoursActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboHoursActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboHoursActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_btnPrintActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        observable.resetForm();         // Reset the fields in the UI to null...
        observable.resetLable();        // Reset the Editable Lables in the UI to null...
        observable.resetTable();       // Reset the table in UI...
        ClientUtil.enableDisable(this, false);// Disables the panel...
        setButtonEnableDisable();       // Enables or Disables the buttons and menu Items depending on their previous state...
        observable.setResultStatus();   // To Reset the Value of lblStatus...
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        updateOBFields();
        //To display an alert if the mandatory fields are not properly inputted, else proceed with normal operation
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panForexExchange);
        if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0 ){
            displayAlert(mandatoryMessage);
        }else{
            observable.doAction();          // To perform the necessary operation depending on the Action type...
            observable.resetForm();         // Reset the fields in the UI to null...
            observable.resetLable();        // Reset the Editable Lables in the UI to null...
            observable.resetTable();       // Reset the table in UI...
            ClientUtil.enableDisable(this, false);// Disables the panel...
            setButtonEnableDisable();       // Enables or Disables the buttons and menu Items depending on their previous state...
            observable.setResultStatus();   // To Reset the Value of lblStatus...
        }
    }//GEN-LAST:event_btnSaveActionPerformed
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        observable.resetForm();   // Reset the fields in the UI to null...
        observable.resetLable();  // Reset the Editable Lables in the UI to null...
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);//Sets the Action Type to be performed...
        popUp(DELETE);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed

        // Add your handling code here:
        observable.resetForm();    // Reset the fields in the UI to null...
        observable.resetLable();   // Reset the Editable Lables in the UI to null...
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);//Sets the Action Type to be performed...
        popUp(EDIT);
    }//GEN-LAST:event_btnEditActionPerformed
    private void popUp(int field) {
        final HashMap viewMap = new HashMap();
        viewType = field;
        if(field==EDIT || field==DELETE){                //Edit=0 and Delete=1
            viewMap.put("MAPNAME", "viewForexExchange");
        }
        new ViewAll(this, viewMap).show();
    }
    
    // this method is called automatically from ViewAll...
    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
        if (viewType==EDIT || viewType==DELETE) {
            System.out.println("hash: "+hash);
            hash.put("WHERE", hash.get("EXCHANGE ID"));
            observable.populateData(hash);    // Called to display the Data in the UI fields...
            // To set the Value of Exchange Id Id...
            final String EXCHANGEID = (String) hash.get("EXCHANGE ID");
            observable.setLblExchangeId(EXCHANGEID);
            
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
                ClientUtil.enableDisable(this, false);     // Disables the panel...
            }else{
                ClientUtil.enableDisable(this, true);     // Enables the panel...
            }
            final String VALUEDATE = ((String)hash.get("VALUE DATE"));
            System.out.println("VALUEDATE: "+VALUEDATE);
            observable.fillForexTab(VALUEDATE);          // To Set the Value in Table...
            setButtonEnableDisable();                     // Enables or Disables the buttons and menu Items depending on their previous state...
            observable.setStatus();                       // To set the Value of lblStatus...
        }
    }
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        observable.resetForm();        // To reset all the fields in UI...
        observable.resetTable();       // Reset the table in UI...
        observable.resetLable();       // Reset the Editable Lables in the UI to null...
        ClientUtil.enableDisable(this, true);  // Enables the panel...
        setButtonEnableDisable();      // Enables/Disables the necessary buttons and menu items...
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);//Sets the Action Type to be performed...
        observable.setStatus();       // To set the Value of lblStatus...
        tdtValidDate.setDateValue(DateUtil.getStringDate((Date) currDt.clone()));
        observable.setBaseCurrency();
    }//GEN-LAST:event_btnNewActionPerformed
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void mitPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitPrintActionPerformed
        // Add your handling code here:
        btnPrintActionPerformed(evt);
    }//GEN-LAST:event_mitPrintActionPerformed
    
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
        new ForexExchangeUI().show();
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CComboBox cboHours;
    private com.see.truetransact.uicomponent.CComboBox cboMinutes;
    private com.see.truetransact.uicomponent.CComboBox cboTransCurrency;
    private com.see.truetransact.uicomponent.CLabel lblBaseCurrency;
    private com.see.truetransact.uicomponent.CLabel lblBaseCurrencyDesc;
    private com.see.truetransact.uicomponent.CLabel lblExchangeID;
    private com.see.truetransact.uicomponent.CLabel lblExchangeIDDesc;
    private com.see.truetransact.uicomponent.CLabel lblHours;
    private com.see.truetransact.uicomponent.CLabel lblMiddleRate;
    private com.see.truetransact.uicomponent.CLabel lblMinutes;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace24;
    private com.see.truetransact.uicomponent.CLabel lblSpace25;
    private com.see.truetransact.uicomponent.CLabel lblSpace26;
    private com.see.truetransact.uicomponent.CLabel lblSpace27;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblStatus1;
    private com.see.truetransact.uicomponent.CLabel lblTransCurrency;
    private com.see.truetransact.uicomponent.CLabel lblValidDate;
    private com.see.truetransact.uicomponent.CLabel lblValueTime;
    private com.see.truetransact.uicomponent.CMenuBar mbrLoanProduct;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panData;
    private com.see.truetransact.uicomponent.CPanel panForexExchange;
    private com.see.truetransact.uicomponent.CPanel panHours;
    private com.see.truetransact.uicomponent.CPanel panMinutes;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTableData;
    private com.see.truetransact.uicomponent.CPanel panValueTime;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptPrint;
    private javax.swing.JSeparator sptProcess;
    private com.see.truetransact.uicomponent.CScrollPane srpTableData;
    private com.see.truetransact.uicomponent.CTable tblTableData;
    private javax.swing.JToolBar tbrLoantProduct;
    private com.see.truetransact.uicomponent.CDateField tdtValidDate;
    private com.see.truetransact.uicomponent.CTextField txtMiddleRate;
    // End of variables declaration//GEN-END:variables
    
}
