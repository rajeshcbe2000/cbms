/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ForexDenominationMasterUI.java
 *
 * Created on January 27, 2005, 10:44 AM
 */

package com.see.truetransact.ui.sysadmin.denomination;

/**
 *
 * @author  152715
 */

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Observer;
import java.util.Observable;



import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.common.viewall.TableDialogUI;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.TrueTransactMain;


public class ForexDenominationMasterUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer , UIMandatoryField  {
    
    // Variable Declarations
    private ForexDenominationMasterOB observable;
    private final ForexDenominationMasterRB  resourceBundle = new ForexDenominationMasterRB();
    
    private HashMap mandatoryMap;
    private int viewType = -1;
    private final int NEW=0,EDIT=1,DELETE=2,AUTHORIZE=3 , VIEW=4;
    boolean isFilled = false;
    private final String currency = "Indian Rupees";
    
    /** Creates new form ForexDenominationMasterUI */
    public ForexDenominationMasterUI() {
        initComponents();
        initStartUp();
    }
    private void initStartUp() {
        setFieldNames();
        internationalize();
        setObservable();
        initComponentData();
        setMandatoryHashMap();
        setMaximumLength();
        setHelpMessage();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panForexDenominationMaster);
        setButtonEnableDisable();
        ClientUtil.enableDisable(this, false);
        setCurrencyEnableDisable(false);
        observable.resetForm();
        observable.resetStatus();
    }
    // To set The Value of the Buttons Depending on the Value or Condition...
    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        btnDelete.setEnabled(!btnDelete.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());
        
        btnSave.setEnabled(!btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
    }
     /* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        ForexDenominationMasterMRB objMandatoryRB = new ForexDenominationMasterMRB();
        txtDenominationName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDenominationName"));
        txtDenominationValue.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDenominationValue"));
        cboCurrency.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCurrency"));
        cboDenominationType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboDenominationType"));
    }
    /* To set Maximum length and for validation */
    private void setMaximumLength() {
        txtDenominationName.setMaxLength(32);
        txtDenominationValue.setMaxLength(16);
        txtDenominationValue.setValidation(new CurrencyValidation(14,2));
    }
    private void setObservable() {
        /* Implementing Singleton pattern */
        observable = ForexDenominationMasterOB.getInstance();
        observable.addObserver(this);
    }
    /*To set model for combo boxes*/
    private void initComponentData() {
        cboCurrency.setModel(observable.getCbmCurrency());
        cboDenominationType.setModel(observable.getCbmDenominationType());
    }
     /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        btnAuthorize.setName("btnAuthorize");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        //	btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        cboCurrency.setName("cboCurrency");
        cboDenominationType.setName("cboDenominationType");
        lblCurrency.setName("lblCurrency");
        lblDenominationName.setName("lblDenominationName");
        lblDenominationValue.setName("lblDenominationValue");
        lblMsg.setName("lblMsg");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace5.setName("lblSpace5");
        lblStatus.setName("lblStatus");
        lblType.setName("lblType");
        mbrForexDenominationMaster.setName("mbrForexDenominationMaster");
        panForexDenominationMaster.setName("panForexDenominationMaster");
        panStatus.setName("panStatus");
        txtDenominationName.setName("txtDenominationName");
        txtDenominationValue.setName("txtDenominationValue");
    }
    
/* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        lblSpace5.setText(resourceBundle.getString("lblSpace5"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblType.setText(resourceBundle.getString("lblType"));
        lblDenominationName.setText(resourceBundle.getString("lblDenominationName"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        //	btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblCurrency.setText(resourceBundle.getString("lblCurrency"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblDenominationValue.setText(resourceBundle.getString("lblDenominationValue"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
    }
    
    
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        cboCurrency.setSelectedItem(observable.getCboCurrency());
        txtDenominationName.setText(observable.getTxtDenominationName());
        txtDenominationValue.setText(observable.getTxtDenominationValue());
        cboDenominationType.setSelectedItem(observable.getCboDenominationType());
        lblStatus.setText(observable.getLblStatus());
    }
    
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setCboCurrency((String) cboCurrency.getSelectedItem());
        observable.setTxtDenominationName(txtDenominationName.getText());
        observable.setTxtDenominationValue(txtDenominationValue.getText());
        observable.setCboDenominationType((String) cboDenominationType.getSelectedItem());
    }
    
    
/* Auto Generated Method - setMandatoryHashMap()
 
ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
 
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboCurrency", new Boolean(true));
        mandatoryMap.put("txtDenominationName", new Boolean(true));
        mandatoryMap.put("txtDenominationValue", new Boolean(true));
        mandatoryMap.put("cboDenominationType", new Boolean(true));
    }
    
/* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    
    
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        tbrForexDenominationMaster = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
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
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace29 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace30 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace31 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panForexDenominationMaster = new com.see.truetransact.uicomponent.CPanel();
        lblCurrency = new com.see.truetransact.uicomponent.CLabel();
        lblDenominationName = new com.see.truetransact.uicomponent.CLabel();
        lblDenominationValue = new com.see.truetransact.uicomponent.CLabel();
        lblType = new com.see.truetransact.uicomponent.CLabel();
        cboCurrency = new com.see.truetransact.uicomponent.CComboBox();
        txtDenominationName = new com.see.truetransact.uicomponent.CTextField();
        txtDenominationValue = new com.see.truetransact.uicomponent.CTextField();
        cboDenominationType = new com.see.truetransact.uicomponent.CComboBox();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        mbrForexDenominationMaster = new com.see.truetransact.uicomponent.CMenuBar();
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
        setTitle("Forex Denomination Master");
        setMinimumSize(new java.awt.Dimension(780, 520));
        setPreferredSize(new java.awt.Dimension(780, 520));

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
        tbrForexDenominationMaster.add(btnView);

        lblSpace4.setText("     ");
        tbrForexDenominationMaster.add(lblSpace4);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrForexDenominationMaster.add(btnNew);

        lblSpace26.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace26.setText("     ");
        lblSpace26.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrForexDenominationMaster.add(lblSpace26);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrForexDenominationMaster.add(btnEdit);

        lblSpace27.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace27.setText("     ");
        lblSpace27.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrForexDenominationMaster.add(lblSpace27);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrForexDenominationMaster.add(btnDelete);

        lblSpace2.setText("     ");
        tbrForexDenominationMaster.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrForexDenominationMaster.add(btnSave);

        lblSpace28.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace28.setText("     ");
        lblSpace28.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrForexDenominationMaster.add(lblSpace28);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrForexDenominationMaster.add(btnCancel);

        lblSpace3.setText("     ");
        tbrForexDenominationMaster.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrForexDenominationMaster.add(btnAuthorize);

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrForexDenominationMaster.add(lblSpace29);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrForexDenominationMaster.add(btnException);

        lblSpace30.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace30.setText("     ");
        lblSpace30.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrForexDenominationMaster.add(lblSpace30);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.setMaximumSize(new java.awt.Dimension(29, 27));
        btnReject.setMinimumSize(new java.awt.Dimension(29, 27));
        btnReject.setPreferredSize(new java.awt.Dimension(29, 27));
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrForexDenominationMaster.add(btnReject);

        lblSpace5.setText("     ");
        tbrForexDenominationMaster.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrForexDenominationMaster.add(btnPrint);

        lblSpace31.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace31.setText("     ");
        lblSpace31.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrForexDenominationMaster.add(lblSpace31);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrForexDenominationMaster.add(btnClose);

        getContentPane().add(tbrForexDenominationMaster, java.awt.BorderLayout.NORTH);

        panForexDenominationMaster.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panForexDenominationMaster.setLayout(new java.awt.GridBagLayout());

        lblCurrency.setText("Currency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panForexDenominationMaster.add(lblCurrency, gridBagConstraints);

        lblDenominationName.setText("Denomination Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panForexDenominationMaster.add(lblDenominationName, gridBagConstraints);

        lblDenominationValue.setText("Denomination Value");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panForexDenominationMaster.add(lblDenominationValue, gridBagConstraints);

        lblType.setText("Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panForexDenominationMaster.add(lblType, gridBagConstraints);

        cboCurrency.setMaximumSize(new java.awt.Dimension(100, 21));
        cboCurrency.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panForexDenominationMaster.add(cboCurrency, gridBagConstraints);

        txtDenominationName.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDenominationName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDenominationNameActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panForexDenominationMaster.add(txtDenominationName, gridBagConstraints);

        txtDenominationValue.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panForexDenominationMaster.add(txtDenominationValue, gridBagConstraints);

        cboDenominationType.setMaximumSize(new java.awt.Dimension(100, 21));
        cboDenominationType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboDenominationType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboDenominationTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panForexDenominationMaster.add(cboDenominationType, gridBagConstraints);

        getContentPane().add(panForexDenominationMaster, java.awt.BorderLayout.CENTER);

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
        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mnuProcess.add(mitDelete);
        mnuProcess.add(sptDelete);

        mitSave.setText("Save");
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancel");
        mnuProcess.add(mitCancel);
        mnuProcess.add(sptCancel);

        mitClose.setText("Close");
        mnuProcess.add(mitClose);

        mbrForexDenominationMaster.add(mnuProcess);

        setJMenuBar(mbrForexDenominationMaster);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        viewType= VIEW;
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        popUp(); 
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed

    private void cboDenominationTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboDenominationTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboDenominationTypeActionPerformed

    private void txtDenominationNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDenominationNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDenominationNameActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        viewType = EDIT;
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        popUp(); 
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        cifClosingAlert();
//        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
    
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
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        setModified(false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        ClientUtil.enableDisable(this, false);
        setCurrencyEnableDisable(false);
        setButtonEnableDisable();
        viewType = -1;
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        observable.resetForm();
        observable.setStatus();
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        setModified(false);
        final String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panForexDenominationMaster);
        updateOBFields();
        boolean exist = observable.checkDuplicate();
        if ((exist) && (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE)) {
            displayAlert(resourceBundle.getString("warning"));
        } else if (mandatoryMessage.length() > 0){
            displayAlert(mandatoryMessage);
        } else {
            savePerformed();
        }
         btnAuthorize.setEnabled(true);
         btnReject.setEnabled(true);
         btnException.setEnabled(true);
    }//GEN-LAST:event_btnSaveActionPerformed
    // When Save Button is Pressed
    
    
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

    private void savePerformed(){
        updateOBFields();
        observable.doAction();
        ClientUtil.enableDisable(this, false);
        setButtonEnableDisable();
        observable.resetForm();
        observable.setResultStatus();
    }
    /**
     * To Display Warning Message
     */
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }
    private void popUp() {
        final HashMap viewMap = new HashMap();
        viewMap.put(CommonConstants.MAP_NAME, "viewForexDenominationMaster");
        new ViewAll(this, viewMap).show();
    }
    // Called Automatically when viewAll() is Called...
    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;        
        HashMap where = new HashMap();
        // Value of Currency is HardCoded
        // Value of Currency is Static
        where.put("CURRENCY", (String)hash.get("CURRENCY"));
        where.put("NAME", (String)hash.get("NAME"));
        where.put("VALUE", CommonUtil.convertObjToDouble(hash.get("VALUE")));
        where.put("TYPE", (String)hash.get("TYPE"));
        hash.put(CommonConstants.MAP_WHERE, where);
        where = null;
        observable.populateData(hash);
        if (viewType == EDIT) {
            ClientUtil.enableDisable(this, true);
            txtDenominationName.setEnabled(false);
        }
        else
            ClientUtil.enableDisable(this, false);
        setCurrencyEnableDisable(false);
        if(viewType==AUTHORIZE) {
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                    isFilled = true;
                }
        observable.setStatus();
        setButtonEnableDisable();
    }
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        viewType = DELETE;
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        popUp();
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    private void setCurrencyEnableDisable(boolean flag) {
        cboCurrency.setEditable(flag);
        cboCurrency.setEnabled(flag);
    }
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        setModified(true);
        viewType = NEW;
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        ClientUtil.enableDisable(this,true);
        setCurrencyEnableDisable(false);
        setButtonEnableDisable();
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        observable.setStatus();
        observable.resetForm();
        cboCurrency.setSelectedItem(currency);
    }//GEN-LAST:event_btnNewActionPerformed
    
    public void authorizeStatus(String authorizeStatus) {
        if (viewType != AUTHORIZE){
            viewType = AUTHORIZE;
            isFilled = false;
            HashMap mapParam = new HashMap();
            mapParam.put(CommonConstants.MAP_NAME, "viewForexDenominationMasterForAuth");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeForexDenomMaster");
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
            
        } else if (viewType == AUTHORIZE && isFilled){
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("USER_ID", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("CURRENCY", CommonUtil.convertObjToStr( observable.getCbmCurrency().getKeyForSelected()));
            //            singleAuthorizeMap.put("DENOMINATION_NAME",txtDenominationName.getText());
            //            singleAuthorizeMap.put("DENOMINATION_VALUE", txtDenominationValue.getText());
            //            singleAuthorizeMap.put("DENOMINATION_TYPE", (String)cboDenominationType.getSelectedItem());
//            singleAuthorizeMap.put("NAME",txtDenominationName.getText());
//            singleAuthorizeMap.put("VALUE", txtDenominationValue.getText());
            singleAuthorizeMap.put("NAME",observable.getTxtDenominationName());
            singleAuthorizeMap.put("VALUE", CommonUtil.convertObjToDouble(observable.getTxtDenominationValue()));
            String type=CommonUtil.convertObjToStr( observable.getCbmDenominationType().getKeyForSelected());
            singleAuthorizeMap.put("TYPE", type);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, ClientUtil.getCurrentDate());
            ClientUtil.execute("authorizeForexDenomMaster", singleAuthorizeMap);
            viewType = -1;
            btnCancelActionPerformed(null);
        }
        observable.resetStatus();
        lblStatus.setText(observable.getLblStatus());
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
    private com.see.truetransact.uicomponent.CComboBox cboCurrency;
    private com.see.truetransact.uicomponent.CComboBox cboDenominationType;
    private com.see.truetransact.uicomponent.CLabel lblCurrency;
    private com.see.truetransact.uicomponent.CLabel lblDenominationName;
    private com.see.truetransact.uicomponent.CLabel lblDenominationValue;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace26;
    private com.see.truetransact.uicomponent.CLabel lblSpace27;
    private com.see.truetransact.uicomponent.CLabel lblSpace28;
    private com.see.truetransact.uicomponent.CLabel lblSpace29;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace30;
    private com.see.truetransact.uicomponent.CLabel lblSpace31;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblType;
    private com.see.truetransact.uicomponent.CMenuBar mbrForexDenominationMaster;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panForexDenominationMaster;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private javax.swing.JToolBar tbrForexDenominationMaster;
    private com.see.truetransact.uicomponent.CTextField txtDenominationName;
    private com.see.truetransact.uicomponent.CTextField txtDenominationValue;
    // End of variables declaration//GEN-END:variables
    
}
