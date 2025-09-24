/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * CarrierUI.java
 *
 * Created on January 5, 2005, 11:04 AM
 */

package com.see.truetransact.ui.bills;

/**
 *
 * @author  152715
 */

import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.uivalidation.PincodeValidation_IN;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.TrueTransactMain;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Observer;
import java.util.Observable;
import java.util.Date;
public class CarrierUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer , UIMandatoryField  {
    // Variable Declarations
    private CarrierOB observable;
    final CarrierRB  resourceBundle = new CarrierRB();
    private HashMap mandatoryMap;
    private int viewType = -1;
    private final int NEW=0, EDIT=1,DELETE=2,AUTHORIZE=3;
    boolean isFilled = false;
    boolean carrierCodeExist = false;
    private Date currDt = null;
    /** Creates new form CarrierUI */
    public CarrierUI() {
        initComponents();
        initStartUp();
    }
    /**
     * Action performed when the form loads...
     */
    private void initStartUp() {
        currDt = ClientUtil.getCurrentDate();
        setFieldNames();
        internationalize();
        setObservable();
        initComponentData();
        setMandatoryHashMap();
        setMaximumLength();
        setHelpMessage();
        setButtonEnableDisable();
        ClientUtil.enableDisable(this, false);
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
    }
    private void setObservable() {
        /* Implementing Singleton pattern */
        observable = CarrierOB.getInstance();
        observable.addObserver(this);
    }
    /*To set model for combo boxes*/
    private void initComponentData() {
        cboCarrierType.setModel(observable.getCbmCarrierType());
        cboCity.setModel(observable.getCbmCity());
        cboState.setModel(observable.getCbmState());
        cboCountry.setModel(observable.getCbmCountry());
    }
    /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        btnAuthorize.setName("btnAuthorize");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        cboCarrierType.setName("cboCarrierType");
        cboCity.setName("cboCity");
        cboCountry.setName("cboCountry");
        cboState.setName("cboState");
        chkIsApproved.setName("chkIsApproved");
        lblAddress.setName("lblAddress");
        lblCarrierCode.setName("lblCarrierCode");
        lblCarrierName.setName("lblCarrierName");
        lblCarrierType.setName("lblCarrierType");
        lblCity.setName("lblCity");
        lblCountry.setName("lblCountry");
        lblIsApproved.setName("lblIsApproved");
        lblMsg.setName("lblMsg");
        lblPincode.setName("lblPincode");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace5.setName("lblSpace5");
        lblState.setName("lblState");
        lblStatus.setName("lblStatus");
        mbrCarrier.setName("mbrCarrier");
        panCarrier.setName("panCarrier");
        panStatus.setName("panStatus");
        txtAddress.setName("txtAddress");
        txtCarrierCode.setName("txtCarrierCode");
        txtCarrierName.setName("txtCarrierName");
        txtPincode.setName("txtPincode");
    }
    
/* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblCity.setText(resourceBundle.getString("lblCity"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblIsApproved.setText(resourceBundle.getString("lblIsApproved"));
        lblPincode.setText(resourceBundle.getString("lblPincode"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        lblCarrierName.setText(resourceBundle.getString("lblCarrierName"));
        lblSpace5.setText(resourceBundle.getString("lblSpace5"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblCountry.setText(resourceBundle.getString("lblCountry"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblCarrierType.setText(resourceBundle.getString("lblCarrierType"));
        lblCarrierCode.setText(resourceBundle.getString("lblCarrierCode"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblAddress.setText(resourceBundle.getString("lblAddress"));
        lblState.setText(resourceBundle.getString("lblState"));
        chkIsApproved.setText(resourceBundle.getString("chkIsApproved"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
    }
    
    
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        txtCarrierCode.setText(observable.getTxtCarrierCode());
        txtCarrierName.setText(observable.getTxtCarrierName());
        cboCarrierType.setSelectedItem(observable.getCboCarrierType());
        txtAddress.setText(observable.getTxtAddress());
        cboCity.setSelectedItem(observable.getCboCity());
        cboState.setSelectedItem(observable.getCboState());
        cboCountry.setSelectedItem(observable.getCboCountry());
        chkIsApproved.setSelected(observable.getChkIsApproved());
        txtPincode.setText(observable.getTxtPincode());
        lblStatus.setText(observable.getLblStatus());
    }
    
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setTxtCarrierCode(txtCarrierCode.getText());
        observable.setTxtCarrierName(txtCarrierName.getText());
        observable.setCboCarrierType((String) cboCarrierType.getSelectedItem());
        observable.setTxtAddress(txtAddress.getText());
        observable.setCboCity((String) cboCity.getSelectedItem());
        observable.setCboState((String) cboState.getSelectedItem());
        observable.setCboCountry((String) cboCountry.getSelectedItem());
        observable.setChkIsApproved(chkIsApproved.isSelected());
        observable.setTxtPincode(txtPincode.getText());
    }
    
    
/* Auto Generated Method - setMandatoryHashMap()
 
ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
 
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtCarrierCode", new Boolean(true));
        mandatoryMap.put("txtCarrierName", new Boolean(true));
        mandatoryMap.put("cboCarrierType", new Boolean(true));
        mandatoryMap.put("txtAddress", new Boolean(true));
        mandatoryMap.put("cboCity", new Boolean(true));
        mandatoryMap.put("cboState", new Boolean(true));
        mandatoryMap.put("cboCountry", new Boolean(true));
        mandatoryMap.put("chkIsApproved", new Boolean(true));
        mandatoryMap.put("txtPincode", new Boolean(true));
    }
    
/* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
        /* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        CarrierMRB objMandatoryRB = new CarrierMRB();
        txtCarrierCode.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCarrierCode"));
        txtCarrierName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCarrierName"));
        cboCarrierType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCarrierType"));
        cboCountry.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCountry"));
        txtAddress.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAddress"));
        txtPincode.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPincode"));
        cboState.setHelpMessage(lblMsg, objMandatoryRB.getString("cboState"));
        cboCity.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCity"));
    }
    
    /* To set Maximum length and for validation */
    private void setMaximumLength() {
        txtCarrierName.setMaxLength(32);
        txtCarrierCode.setMaxLength(8);
        txtAddress.setMaxLength(64);
        txtPincode.setMaxLength(8);
        txtPincode.setValidation(new PincodeValidation_IN());
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        tbrCarrier = new javax.swing.JToolBar();
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
        panCarrier = new com.see.truetransact.uicomponent.CPanel();
        lblCarrierCode = new com.see.truetransact.uicomponent.CLabel();
        lblCarrierName = new com.see.truetransact.uicomponent.CLabel();
        lblCarrierType = new com.see.truetransact.uicomponent.CLabel();
        lblAddress = new com.see.truetransact.uicomponent.CLabel();
        lblCity = new com.see.truetransact.uicomponent.CLabel();
        lblState = new com.see.truetransact.uicomponent.CLabel();
        lblCountry = new com.see.truetransact.uicomponent.CLabel();
        lblIsApproved = new com.see.truetransact.uicomponent.CLabel();
        txtCarrierCode = new com.see.truetransact.uicomponent.CTextField();
        txtCarrierName = new com.see.truetransact.uicomponent.CTextField();
        cboCarrierType = new com.see.truetransact.uicomponent.CComboBox();
        txtAddress = new com.see.truetransact.uicomponent.CTextField();
        cboCity = new com.see.truetransact.uicomponent.CComboBox();
        cboState = new com.see.truetransact.uicomponent.CComboBox();
        cboCountry = new com.see.truetransact.uicomponent.CComboBox();
        chkIsApproved = new com.see.truetransact.uicomponent.CCheckBox();
        lblPincode = new com.see.truetransact.uicomponent.CLabel();
        txtPincode = new com.see.truetransact.uicomponent.CTextField();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        mbrCarrier = new com.see.truetransact.uicomponent.CMenuBar();
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
        setTitle("Carrier");

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrCarrier.add(btnNew);

        lblSpace26.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace26.setText("     ");
        lblSpace26.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrCarrier.add(lblSpace26);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrCarrier.add(btnEdit);

        lblSpace27.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace27.setText("     ");
        lblSpace27.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrCarrier.add(lblSpace27);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrCarrier.add(btnDelete);

        lblSpace2.setText("     ");
        tbrCarrier.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrCarrier.add(btnSave);

        lblSpace28.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace28.setText("     ");
        lblSpace28.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrCarrier.add(lblSpace28);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrCarrier.add(btnCancel);

        lblSpace3.setText("     ");
        tbrCarrier.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrCarrier.add(btnAuthorize);

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrCarrier.add(lblSpace29);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrCarrier.add(btnException);

        lblSpace30.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace30.setText("     ");
        lblSpace30.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrCarrier.add(lblSpace30);

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
        tbrCarrier.add(btnReject);

        lblSpace5.setText("     ");
        tbrCarrier.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrCarrier.add(btnPrint);

        lblSpace31.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace31.setText("     ");
        lblSpace31.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrCarrier.add(lblSpace31);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrCarrier.add(btnClose);

        getContentPane().add(tbrCarrier, java.awt.BorderLayout.NORTH);

        panCarrier.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panCarrier.setLayout(new java.awt.GridBagLayout());

        lblCarrierCode.setText("Carrier Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCarrier.add(lblCarrierCode, gridBagConstraints);

        lblCarrierName.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCarrier.add(lblCarrierName, gridBagConstraints);

        lblCarrierType.setText("Carrier Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCarrier.add(lblCarrierType, gridBagConstraints);

        lblAddress.setText("Address");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCarrier.add(lblAddress, gridBagConstraints);

        lblCity.setText("City");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCarrier.add(lblCity, gridBagConstraints);

        lblState.setText("State");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCarrier.add(lblState, gridBagConstraints);

        lblCountry.setText("Country");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCarrier.add(lblCountry, gridBagConstraints);

        lblIsApproved.setText("Is Approved");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCarrier.add(lblIsApproved, gridBagConstraints);

        txtCarrierCode.setMinimumSize(new java.awt.Dimension(50, 21));
        txtCarrierCode.setPreferredSize(new java.awt.Dimension(50, 21));
        txtCarrierCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCarrierCodeFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCarrier.add(txtCarrierCode, gridBagConstraints);

        txtCarrierName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCarrier.add(txtCarrierName, gridBagConstraints);

        cboCarrierType.setMaximumSize(new java.awt.Dimension(100, 21));
        cboCarrierType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCarrier.add(cboCarrierType, gridBagConstraints);

        txtAddress.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCarrier.add(txtAddress, gridBagConstraints);

        cboCity.setMaximumSize(new java.awt.Dimension(100, 21));
        cboCity.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCarrier.add(cboCity, gridBagConstraints);

        cboState.setMaximumSize(new java.awt.Dimension(100, 21));
        cboState.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCarrier.add(cboState, gridBagConstraints);

        cboCountry.setMaximumSize(new java.awt.Dimension(100, 21));
        cboCountry.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCarrier.add(cboCountry, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCarrier.add(chkIsApproved, gridBagConstraints);

        lblPincode.setText("Pincode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCarrier.add(lblPincode, gridBagConstraints);

        txtPincode.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCarrier.add(txtPincode, gridBagConstraints);

        getContentPane().add(panCarrier, java.awt.BorderLayout.CENTER);

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

        mbrCarrier.add(mnuProcess);

        setJMenuBar(mbrCarrier);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void txtCarrierCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCarrierCodeFocusLost
        // TODO add your handling code here:
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            carrierCodeExist = checkBankCode();
        }
    }//GEN-LAST:event_txtCarrierCodeFocusLost
    private boolean checkBankCode()  {
        boolean exist = false;
        if (observable.isCarrierCodeAlreadyExist(txtCarrierCode.getText())) {
            exist = true;
            displayAlert(resourceBundle.getString("CarrierCodeCount"));
        } else {
            exist = false;
        }
        return exist;
    }
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        
        cifClosingAlert();
//        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        authorizeStatus(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }
    /* action to perform when  main save button is pressed */
    private void savePerformed(){
        updateOBFields();
        observable.doAction();
        ClientUtil.enableDisable(this, false);
        setButtonEnableDisable();
        observable.resetForm();
        observable.setResultStatus();
    }
    public void authorizeStatus(String authorizeStatus) {
        if (viewType != AUTHORIZE){
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            mapParam.put(CommonConstants.MAP_NAME, "viewAuthorizeCarrier");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeCarrier");
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
            isFilled = false;
        } else if (viewType == AUTHORIZE && isFilled){
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("USER_ID", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("CARRIER_CODE", txtCarrierCode.getText());
            singleAuthorizeMap.put("AUTHORIZEDT", currDt.clone());
            ClientUtil.execute("authorizeCarrier", singleAuthorizeMap);
            viewType = -1;
            btnCancelActionPerformed(null);
        }
        observable.resetStatus();
        lblStatus.setText(observable.getLblStatus());
    }
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        setModified(false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        ClientUtil.enableDisable(this, false);
        setButtonEnableDisable();
        observable.resetForm();
        observable.setStatus();
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panCarrier);
        if (carrierCodeExist) {
            mandatoryMessage += resourceBundle.getString("CarrierCodeCount");
        }
        if (mandatoryMessage.length() > 0){
            displayAlert(mandatoryMessage);
        }
        else {
            savePerformed();
        }
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        viewType = DELETE;
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        popUp();
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        viewType = EDIT;
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        popUp();
    }//GEN-LAST:event_btnEditActionPerformed
    /**
     * Enable Disable Carrier Fields
     */
    public void enableDisablePanCarrier(boolean flag){
        ClientUtil.enableDisable(panCarrier,flag);
        
    }
    private void enableDisableCarrierCode(boolean flag) {
        txtCarrierCode.setEditable(flag);
    }
    /** To display a popUp window for viewing existing data */
    private void popUp() {
        final HashMap viewMap = new HashMap();
        if ( observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ||  observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE){
            viewMap.put(CommonConstants.MAP_NAME, "viewCarrier");
        }
        new ViewAll(this, viewMap).show();
    }
    // Called Automatically when viewAll() is Called...
    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
        hash.put("WHERE", (String)hash.get("CARRIER_CODE"));
        observable.populateData(hash);
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            enableDisablePanCarrier(true);
            enableDisableCarrierCode(false);
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
            enableDisablePanCarrier(false);
        } else {
            enableDisableCarrierCode(true);
        }
        observable.setStatus();
        setButtonEnableDisable();
    }
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        setModified(true);
        viewType = NEW;
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        enableDisablePanCarrier(true);
        enableDisableCarrierCode(true);
        setButtonEnableDisable();
        observable.setStatus();
        observable.resetForm();
    }//GEN-LAST:event_btnNewActionPerformed
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm
    
    //    /**
    //     * @param args the command line arguments
    //     */
    //    public static void main(String args[]) {
    //        new CarrierUI().show();
    //    }
    
    
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
    private com.see.truetransact.uicomponent.CComboBox cboCarrierType;
    private com.see.truetransact.uicomponent.CComboBox cboCity;
    private com.see.truetransact.uicomponent.CComboBox cboCountry;
    private com.see.truetransact.uicomponent.CComboBox cboState;
    private com.see.truetransact.uicomponent.CCheckBox chkIsApproved;
    private com.see.truetransact.uicomponent.CLabel lblAddress;
    private com.see.truetransact.uicomponent.CLabel lblCarrierCode;
    private com.see.truetransact.uicomponent.CLabel lblCarrierName;
    private com.see.truetransact.uicomponent.CLabel lblCarrierType;
    private com.see.truetransact.uicomponent.CLabel lblCity;
    private com.see.truetransact.uicomponent.CLabel lblCountry;
    private com.see.truetransact.uicomponent.CLabel lblIsApproved;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblPincode;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace26;
    private com.see.truetransact.uicomponent.CLabel lblSpace27;
    private com.see.truetransact.uicomponent.CLabel lblSpace28;
    private com.see.truetransact.uicomponent.CLabel lblSpace29;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace30;
    private com.see.truetransact.uicomponent.CLabel lblSpace31;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblState;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CMenuBar mbrCarrier;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panCarrier;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private javax.swing.JToolBar tbrCarrier;
    private com.see.truetransact.uicomponent.CTextField txtAddress;
    private com.see.truetransact.uicomponent.CTextField txtCarrierCode;
    private com.see.truetransact.uicomponent.CTextField txtCarrierName;
    private com.see.truetransact.uicomponent.CTextField txtPincode;
    // End of variables declaration//GEN-END:variables
    
}
