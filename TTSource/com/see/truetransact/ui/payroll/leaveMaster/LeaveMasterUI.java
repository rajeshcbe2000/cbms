/**
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * .
 *
 * LeaveMasterUI.java
 *
 *
 */
package com.see.truetransact.ui.payroll.leaveMaster;

/**
 *
 * @author anjuanand
 */
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uimandatory.UIMandatoryField;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LeaveMasterUI extends CInternalFrame implements UIMandatoryField, Observer {

    private final static ClientParseException parseException = ClientParseException.getInstance();
    private HashMap mandatoryMap;
    private LeaveMasterOB observable;
    private LeaveMasterMRB objMandatoryRB;
    private ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.payroll.leaveMaster.LeaveMasterRB", ProxyParameters.LANGUAGE);
    private String viewType = new String();
    boolean isFilled = false;
    private String view = "";

    /**
     * Creates new form LeaveMasterUI
     */
    public LeaveMasterUI() {
        initUIComponents();
    }

    /**
     * Initialises the UIComponents
     */
    private void initUIComponents() {
        initComponents();
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setHelpMessage();
        setObservable();
        observable.resetForm();
        initComponentData();
        ClientUtil.enableDisable(panLeaveManagement, false);
        setButtonEnableDisable();
        txtLeaveId.setEnabled(false);
    }

    /*
     * Auto Generated Method - setFieldNames() This method assigns name for all
     * the components. Other functions are working based on this name.
     */
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
        lblLeaveDescription.setName("lblLeaveDescription");
        cboLeaveDescription.setName("cboLeaveDescription");
        panLeaveLapses.setName("panLeaveLapses");
        panAccAllowed.setName("panAccAllowed");
        panEncash.setName("panEncash");
        panLeaveMangmt.setName("panLeaveMangmt");
        panLeaveManagement.setName("panLeaveManagement");
        panLeaveLapses.setName("panLeaveLapses");
        txtLeaveId.setName("txtLeaveId");
        lblDeduction.setName("lblLeaveDedPerDay");
        rdoDeduction_Yes.setName("rdoDeduction_Yes");
        panLeaveId.setName("panLeaveId");
        panLeave.setName("panLeave");
        rdoDeduction_No.setName("rdoDeduction_No");
        rdoHalfPay.setName("rdoHalfPay");
        rdoFullPay.setName("rdoFullPay");
        panPayType.setName("panPayType");
        btnView.setName("btnView");
    }

    /*
     * Auto Generated Method - internationalize() This method used to assign
     * display texts from the Resource Bundle File.
     */
    private void internationalize() {
        resourceBundle = new LeaveMasterRB();
        btnClose.setText(resourceBundle.getString("btnClose"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblLeaveId.setText(resourceBundle.getString("lblLeaveId"));
        lblLeaveDescription.setText(resourceBundle.getString("lblLeaveDescription"));
        lblDeduction.setText(resourceBundle.getString("lblLeaveDedPerDay"));
        rdoDeduction_Yes.setText(resourceBundle.getString("rdoDeduction_Yes"));
        rdoDeduction_No.setText(resourceBundle.getString("rdoDeduction_No"));
        rdoHalfPay.setText(resourceBundle.getString("rdoHalfPay"));
        rdoFullPay.setText(resourceBundle.getString("rdoFullPay"));
    }

    /*
     * Auto Generated Method - setMandatoryHashMap() This method list out all
     * the Input Fields available in the UI. It needs a class level HashMap
     * variable mandatoryMap.
     */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtLeaveDedPerDay", new Boolean(true));
        mandatoryMap.put("cboLeaveDescription", new Boolean(true));
    }

    /*
     * Auto Generated Method - getMandatoryHashMap() Getter method for
     * setMandatoryHashMap().
     */
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    /*
     * Creates the insstance of LeaveManagement which acts as Observable to
     * LeaveManagement UI
     */
    private void setObservable() {
        try {
            observable = LeaveMasterOB.getInstance();
            observable.addObserver(this);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    /*
     * Sets the model for the comboboxes in the UI
     */
    private void initComponentData() {
        try {
            cboLeaveDescription.setModel(observable.getCbmLeaveDescription());
        } catch (ClassCastException e) {
            parseException.logException(e, true);
        }
    }

    /*
     * Auto Generated Method - setHelpMessage() This method shows tooltip help
     * for all the input fields available in the UI. It needs the Mandatory
     * Resource Bundle object. Help display Label name should be lblMsg.
     */
    public void setHelpMessage() {
        objMandatoryRB = new LeaveMasterMRB();
        cboLeaveDescription.setHelpMessage(lblMsg, objMandatoryRB.getString("cboLeaveDescription"));
        rdoDeduction_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoDeduction_Yes"));
        rdoDeduction_No.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoDeduction_No"));
        rdoHalfPay.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoHalfPay"));
        rdoFullPay.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoFullPay"));
    }

    /*
     * Auto Generated Method - update() This method called by Observable. It
     * updates the UI with Observable's data. If needed add/Remove RadioButtons
     * method need to be added.
     */
    public void update(Observable observed, Object arg) {
        txtLeaveId.setText(observable.getTxtLeaveId());
        cboLeaveDescription.setSelectedItem(observable.getCboLeaveDescription());
        if (observable.isIsDeduction() == false) {
            rdoDeduction_No.setSelected(true);
            rdoDeduction_Yes.setSelected(false);
            rdoHalfPay.setSelected(false);
            rdoFullPay.setSelected(false);
        } else if (observable.isIsDeduction() == true && observable.isIsHalfPay() == true) {
            rdoDeduction_No.setSelected(false);
            rdoDeduction_Yes.setSelected(true);
            rdoHalfPay.setSelected(true);
            rdoFullPay.setSelected(false);
            rdoHalfPay.setEnabled(true);
            rdoFullPay.setEnabled(true);
        } else if (observable.isIsDeduction() == true && observable.isIsFullPay() == true) {
            rdoDeduction_No.setSelected(false);
            rdoDeduction_Yes.setSelected(true);
            rdoHalfPay.setSelected(false);
            rdoFullPay.setSelected(true);
            rdoHalfPay.setEnabled(true);
            rdoFullPay.setEnabled(true);
        }
    }

    /*
     * Auto Generated Method - updateOBFields() This method called by Save
     * option of UI. It updates the OB with UI data.
     */
    public void updateOBFields() {
        observable.setTxtLeaveId(txtLeaveId.getText());
        observable.setCboLeaveDescription(CommonUtil.convertObjToStr(cboLeaveDescription.getSelectedItem()));
    }

    /**
     * Enabling and Disabling of Buttons after Save,Edit,Delete operations
     */
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
        lblStatus.setText(observable.getLblStatus());
        btnView.setEnabled(!btnView.isEnabled());
    }

    private void enableText(boolean flag) {
        txtLeaveId.setEnabled(false);
        cboLeaveDescription.setEnabled(flag);
        rdoDeduction_Yes.setEnabled(flag);
        rdoDeduction_No.setEnabled(flag);
        rdoHalfPay.setEnabled(flag);
        rdoFullPay.setEnabled(flag);
        rdoHalfPay.setSelected(flag);
        rdoFullPay.setSelected(flag);
        rdoDeduction_Yes.setSelected(flag);
    }

    /*
     * Does necessary operaion when user clicks the save button
     */
    private void savePerformed() {
        updateOBFields();
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panLeaveMangmt);
        if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0) {
            displayAlert(mandatoryMessage);
        } else {
            observable.doAction();
            observable.resetStatus();
            if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                ClientUtil.showMessageWindow("Sucessfully Completed!!!");
                resetTxtFields();
                enableText(false);
                observable.resetForm();
                btnCancelActionPerformed(null);
                txtLeaveId.setEnabled(false);
                lblStatus.setText("Success");
            } else if (observable.getResult() == ClientConstants.ACTIONTYPE_FAILED) {
                lblStatus.setText("Failed");
            }
        }
    }

    /**
     * This will Display the Mandatory Message in a Dialog Box
     */
    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }

    /**
     * This will show a popup screen which shows all tbe Rows.of the table
     */
    private void callView(String currAction) {

        viewType = currAction;
        HashMap viewMap = new HashMap();
        if (currAction.equalsIgnoreCase("Enquiry") || currAction.equalsIgnoreCase("Edit") || currAction.equalsIgnoreCase("Delete")) {
            isFilled = false;
            HashMap map = new HashMap();
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getAllLeaves");
        }
        new ViewAll(this, viewMap).show();
    }

    /*
     * This method is used to fill up all tbe UIFields after the user selects
     * the desired row in the popup
     */
    public void fillData(Object map) {
        setModified(true);
        HashMap hash = (HashMap) map;
        if (viewType.equals("Edit") || viewType.equals("Delete") || viewType.equals("Enquiry")) {
            try {
                observable.populateOB(hash);
                isFilled = true;
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                    cboLeaveDescription.setEnabled(false);
                    rdoDeduction_Yes.setEnabled(true);
                    rdoDeduction_No.setEnabled(true);
                } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
                    enableText(false);
                }
            } catch (Exception ex) {
                Logger.getLogger(LeaveMasterUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            observable.ttNotifyObservers();
        }

    }

    public static void main(String args[]) {
        javax.swing.JFrame frame = new javax.swing.JFrame();
        LeaveMasterUI ui = new LeaveMasterUI();
        frame.getContentPane().add(ui);
        ui.setVisible(true);
        frame.setVisible(true);
        frame.setSize(600, 600);
        frame.show();
        ui.show();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        btnPayType = new com.see.truetransact.uicomponent.CButtonGroup();
        btnDeduction = new com.see.truetransact.uicomponent.CButtonGroup();
        panLeaveManagement = new com.see.truetransact.uicomponent.CPanel();
        tabLeaveManagement = new com.see.truetransact.uicomponent.CTabbedPane();
        panLeaveMangmt = new com.see.truetransact.uicomponent.CPanel();
        panAccAllowedPeriod = new com.see.truetransact.uicomponent.CPanel();
        panLeaveLapses = new com.see.truetransact.uicomponent.CPanel();
        panAccAllowed = new com.see.truetransact.uicomponent.CPanel();
        panEncash = new com.see.truetransact.uicomponent.CPanel();
        panLeaveDetails = new com.see.truetransact.uicomponent.CPanel();
        panLeaveId = new com.see.truetransact.uicomponent.CPanel();
        lblLeaveId = new com.see.truetransact.uicomponent.CLabel();
        txtLeaveId = new com.see.truetransact.uicomponent.CTextField();
        panLeave = new com.see.truetransact.uicomponent.CPanel();
        cboLeaveDescription = new com.see.truetransact.uicomponent.CComboBox();
        lblLeaveDescription = new com.see.truetransact.uicomponent.CLabel();
        lblDeduction = new com.see.truetransact.uicomponent.CLabel();
        rdoDeduction_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoDeduction_No = new com.see.truetransact.uicomponent.CRadioButton();
        panPayType = new com.see.truetransact.uicomponent.CPanel();
        rdoFullPay = new com.see.truetransact.uicomponent.CRadioButton();
        rdoHalfPay = new com.see.truetransact.uicomponent.CRadioButton();
        tbrLeaveManagement = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace17 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace18 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace19 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace20 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace21 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace22 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        mbrLeaveManagement = new com.see.truetransact.uicomponent.CMenuBar();
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
        setMaximumSize(new java.awt.Dimension(950, 650));
        setMinimumSize(new java.awt.Dimension(500, 500));
        setPreferredSize(new java.awt.Dimension(500, 500));

        panLeaveManagement.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panLeaveManagement.setLayout(new java.awt.GridBagLayout());

        tabLeaveManagement.setMinimumSize(new java.awt.Dimension(769, 321));
        tabLeaveManagement.setPreferredSize(new java.awt.Dimension(823, 609));

        panLeaveMangmt.setLayout(new java.awt.GridBagLayout());

        panAccAllowedPeriod.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panLeaveMangmt.add(panAccAllowedPeriod, gridBagConstraints);

        panLeaveLapses.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 4);
        panLeaveMangmt.add(panLeaveLapses, gridBagConstraints);

        panAccAllowed.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 4);
        panLeaveMangmt.add(panAccAllowed, gridBagConstraints);

        panEncash.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 4);
        panLeaveMangmt.add(panEncash, gridBagConstraints);

        panLeaveDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Leave Settings"));
        panLeaveDetails.setMinimumSize(new java.awt.Dimension(400, 350));
        panLeaveDetails.setPreferredSize(new java.awt.Dimension(400, 350));
        panLeaveDetails.setLayout(new java.awt.GridBagLayout());

        panLeaveId.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panLeaveId.setMinimumSize(new java.awt.Dimension(340, 80));
        panLeaveId.setPreferredSize(new java.awt.Dimension(340, 80));
        panLeaveId.setLayout(new java.awt.GridBagLayout());

        lblLeaveId.setText("Leave Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeaveId.add(lblLeaveId, gridBagConstraints);

        txtLeaveId.setEditable(false);
        txtLeaveId.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 0);
        panLeaveId.add(txtLeaveId, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panLeaveDetails.add(panLeaveId, gridBagConstraints);

        panLeave.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panLeave.setMinimumSize(new java.awt.Dimension(340, 150));
        panLeave.setPreferredSize(new java.awt.Dimension(340, 150));
        panLeave.setLayout(new java.awt.GridBagLayout());

        cboLeaveDescription.setPopupWidth(150);
        cboLeaveDescription.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboLeaveDescriptionActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 19, 4, 0);
        panLeave.add(cboLeaveDescription, gridBagConstraints);

        lblLeaveDescription.setText("Leave Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panLeave.add(lblLeaveDescription, gridBagConstraints);

        lblDeduction.setText("Deduction");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 4, 0);
        panLeave.add(lblDeduction, gridBagConstraints);

        btnDeduction.add(rdoDeduction_Yes);
        rdoDeduction_Yes.setText("Yes");
        rdoDeduction_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDeduction_YesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 17, 4, 0);
        panLeave.add(rdoDeduction_Yes, gridBagConstraints);

        btnDeduction.add(rdoDeduction_No);
        rdoDeduction_No.setText("No");
        rdoDeduction_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDeduction_NoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 72, 4, 0);
        panLeave.add(rdoDeduction_No, gridBagConstraints);

        panPayType.setMinimumSize(new java.awt.Dimension(180, 30));
        panPayType.setPreferredSize(new java.awt.Dimension(180, 30));
        panPayType.setLayout(new java.awt.GridBagLayout());

        btnPayType.add(rdoFullPay);
        rdoFullPay.setText("Full-Pay");
        rdoFullPay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoFullPayActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        panPayType.add(rdoFullPay, gridBagConstraints);

        btnPayType.add(rdoHalfPay);
        rdoHalfPay.setText("Half-Pay");
        rdoHalfPay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoHalfPayActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        panPayType.add(rdoHalfPay, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panLeave.add(panPayType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(20, 0, 0, 0);
        panLeaveDetails.add(panLeave, gridBagConstraints);

        panLeaveMangmt.add(panLeaveDetails, new java.awt.GridBagConstraints());

        tabLeaveManagement.addTab("Leave Master", panLeaveMangmt);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panLeaveManagement.add(tabLeaveManagement, gridBagConstraints);
        tabLeaveManagement.getAccessibleContext().setAccessibleName("");

        getContentPane().add(panLeaveManagement, java.awt.BorderLayout.CENTER);

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setEnabled(false);
        btnView.setFocusable(false);
        btnView.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnView.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView.setPreferredSize(new java.awt.Dimension(21, 21));
        btnView.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });
        tbrLeaveManagement.add(btnView);

        lblSpace5.setText("     ");
        tbrLeaveManagement.add(lblSpace5);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrLeaveManagement.add(btnNew);

        lblSpace17.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace17.setText("     ");
        lblSpace17.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLeaveManagement.add(lblSpace17);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrLeaveManagement.add(btnEdit);

        lblSpace18.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace18.setText("     ");
        lblSpace18.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace18.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace18.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLeaveManagement.add(lblSpace18);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrLeaveManagement.add(btnDelete);

        lblSpace2.setText("     ");
        tbrLeaveManagement.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrLeaveManagement.add(btnSave);

        lblSpace19.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace19.setText("     ");
        lblSpace19.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace19.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace19.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLeaveManagement.add(lblSpace19);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrLeaveManagement.add(btnCancel);

        lblSpace3.setText("     ");
        tbrLeaveManagement.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        tbrLeaveManagement.add(btnAuthorize);

        lblSpace20.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace20.setText("     ");
        lblSpace20.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace20.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace20.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLeaveManagement.add(lblSpace20);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        tbrLeaveManagement.add(btnException);

        lblSpace21.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace21.setText("     ");
        lblSpace21.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLeaveManagement.add(lblSpace21);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        tbrLeaveManagement.add(btnReject);

        lblSpace4.setText("     ");
        tbrLeaveManagement.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrLeaveManagement.add(btnPrint);

        lblSpace22.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace22.setText("     ");
        lblSpace22.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLeaveManagement.add(lblSpace22);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrLeaveManagement.add(btnClose);

        getContentPane().add(tbrLeaveManagement, java.awt.BorderLayout.NORTH);

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

        mbrLeaveManagement.setName("mbrCustomer");

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
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrLeaveManagement.add(mnuProcess);

        setJMenuBar(mbrLeaveManagement);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // TODO add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // TODO add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed

    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // TODO add your handling code here:
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed

    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mitDeleteActionPerformed

    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mitEditActionPerformed

    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // TODO add your handling code here:
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        setModified(false);
        observable.resetForm();
        resetTxtFields();
        enableText(false);
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panLeaveManagement, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        setButtonEnableDisable();
        viewType = "";
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        btnView.setEnabled(true);
        txtLeaveId.setEnabled(false);
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        setModified(false);
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        boolean isChkLeaveExists = false;
        HashMap dataMap = new HashMap();
        dataMap.put("LEAVEDESC", cboLeaveDescription.getSelectedItem());
        isChkLeaveExists = observable.checkLeaveExists(dataMap);
        if (isChkLeaveExists == true && observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            ClientUtil.showMessageWindow("This Leave already entered!!!");
            lblStatus.setText("Failed");
            return;
        }
        if (rdoDeduction_Yes.isSelected() && !(rdoHalfPay.isSelected() || rdoFullPay.isSelected())) {
            ClientUtil.showMessageWindow("Please select Pay Type!!!");
            return;
        } else {
            savePerformed();
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.setStatus();
        setModified(true);
        observable.resetForm();
        resetTxtFields();
        ClientUtil.enableDisable(panLeaveManagement, true);
        ClientUtil.enableDisable(panLeaveDetails, true);
        setButtonEnableDisable();
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        txtLeaveId.setEnabled(false);
        panPayType.setEnabled(false);
        lblStatus.setText("New");
        rdoHalfPay.setEnabled(false);
        rdoFullPay.setEnabled(false);
        rdoHalfPay.setSelected(false);
        rdoFullPay.setSelected(false);
        rdoDeduction_No.setSelected(true);
        rdoDeduction_Yes.setSelected(false);
    }//GEN-LAST:event_btnNewActionPerformed

    private void cboLeaveDescriptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboLeaveDescriptionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboLeaveDescriptionActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT); //__ Sets the Action Type to be performed...
        callView("Edit");
        txtLeaveId.setEnabled(false);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
        btnSave.setEnabled(true);
        lblStatus.setText("Edit");
    }//GEN-LAST:event_btnEditActionPerformed

    private void rdoDeduction_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDeduction_YesActionPerformed
        // TODO add your handling code here:
        panPayType.setEnabled(true);
        rdoHalfPay.setEnabled(true);
        rdoFullPay.setEnabled(true);
        observable.setIsDeduction(true);
        rdoHalfPay.setSelected(false);
        rdoFullPay.setSelected(false);
    }//GEN-LAST:event_rdoDeduction_YesActionPerformed

    private void rdoDeduction_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDeduction_NoActionPerformed
        // TODO add your handling code here:
        panPayType.setEnabled(false);
        rdoHalfPay.setEnabled(false);
        rdoFullPay.setEnabled(false);
        observable.setIsDeduction(false);
    }//GEN-LAST:event_rdoDeduction_NoActionPerformed

    private void rdoHalfPayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoHalfPayActionPerformed
        // TODO add your handling code here:
        observable.setIsHalfPay(true);
        observable.setIsFullPay(false);
    }//GEN-LAST:event_rdoHalfPayActionPerformed

    private void rdoFullPayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoFullPayActionPerformed
        // TODO add your handling code here:
        observable.setIsHalfPay(false);
        observable.setIsFullPay(true);
    }//GEN-LAST:event_rdoFullPayActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        callView("Enquiry");
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        btnCheck();
        txtLeaveId.setEnabled(false);
        enableText(false);
    }//GEN-LAST:event_btnViewActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE && isFilled == true) {
            view = "DELETE";
            observable.doAction();
            isFilled = false;
            if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                resetTxtFields();
                observable.resetForm();
                ClientUtil.clearAll(this);
                btnCancelActionPerformed(null);
                lblStatus.setText("Deleted");
            } else if (observable.getResult() == ClientConstants.ACTIONTYPE_FAILED) {
                resetTxtFields();
                observable.resetForm();
                lblStatus.setText("Failed");
            }
        } else {
            observable.setActionType(ClientConstants.ACTIONTYPE_DELETE); //__ Sets the Action Type to be performed...
            callView("Delete");
            btnDelete.setEnabled(true);
            btnNew.setEnabled(false);
            btnEdit.setEnabled(false);
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
            btnView.setEnabled(false);
            isFilled = true;
            lblStatus.setText("Delete");
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnCheck() {
        btnCancel.setEnabled(true);
        btnSave.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnEdit.setEnabled(false);
    }

    public void resetTxtFields() {
        txtLeaveId.setText("");
        cboLeaveDescription.setSelectedItem("");
        rdoHalfPay.setSelected(false);
        rdoFullPay.setSelected(false);
        rdoHalfPay.setEnabled(false);
        btnPayType.setSelected(null, false);
        rdoFullPay.setEnabled(false);
        rdoDeduction_No.setSelected(true);
        rdoDeduction_Yes.setSelected(false);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButtonGroup btnDeduction;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButtonGroup btnPayType;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboLeaveDescription;
    private com.see.truetransact.uicomponent.CLabel lblDeduction;
    private com.see.truetransact.uicomponent.CLabel lblLeaveDescription;
    private com.see.truetransact.uicomponent.CLabel lblLeaveId;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace17;
    private com.see.truetransact.uicomponent.CLabel lblSpace18;
    private com.see.truetransact.uicomponent.CLabel lblSpace19;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace20;
    private com.see.truetransact.uicomponent.CLabel lblSpace21;
    private com.see.truetransact.uicomponent.CLabel lblSpace22;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CMenuBar mbrLeaveManagement;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAccAllowed;
    private com.see.truetransact.uicomponent.CPanel panAccAllowedPeriod;
    private com.see.truetransact.uicomponent.CPanel panEncash;
    private com.see.truetransact.uicomponent.CPanel panLeave;
    private com.see.truetransact.uicomponent.CPanel panLeaveDetails;
    private com.see.truetransact.uicomponent.CPanel panLeaveId;
    private com.see.truetransact.uicomponent.CPanel panLeaveLapses;
    private com.see.truetransact.uicomponent.CPanel panLeaveManagement;
    private com.see.truetransact.uicomponent.CPanel panLeaveMangmt;
    private com.see.truetransact.uicomponent.CPanel panPayType;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CRadioButton rdoDeduction_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoDeduction_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoFullPay;
    private com.see.truetransact.uicomponent.CRadioButton rdoHalfPay;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CTabbedPane tabLeaveManagement;
    private javax.swing.JToolBar tbrLeaveManagement;
    private com.see.truetransact.uicomponent.CTextField txtLeaveId;
    // End of variables declaration//GEN-END:variables
}
