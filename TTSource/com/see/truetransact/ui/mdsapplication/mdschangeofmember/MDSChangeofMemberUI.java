/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * MDSChangeofMemberUI.java
 *
 * Created on May 29, 2011, 11:27 AM
 */
package com.see.truetransact.ui.mdsapplication.mdschangeofmember;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Observable;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.ui.customer.CheckCustomerIdUI;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.clientexception.ClientParseException;
import java.util.Date;
import javax.swing.*;
/**
 *
 * @author
 *
 *
 */
public class MDSChangeofMemberUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer, UIMandatoryField {

    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.mdsapplication.mdschangeofmember.MDSChangeofMemberRB", ProxyParameters.LANGUAGE);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private MDSChangeofMemberMRB objMandatoryRB = new MDSChangeofMemberMRB();
    private String viewType = new String();
    MDSChangeofMemberOB observable = null;
    final String AUTHORIZE = "Authorize";
    HashMap mandatoryMap = null;
    boolean isFilled = false;
    private Date currDt = null;
    /**
     * Creates new form BeanForm
     */
    public MDSChangeofMemberUI() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        initForm();

    }

    private void initForm() {
        setFieldNames();
        internationalize();
        observable = new MDSChangeofMemberOB();
        setMaxLengths();
        initComponentData();
        setMandatoryHashMap();
        setHelpMessage();
        setButtonEnableDisable();
        ClientUtil.enableDisable(panChangeOfMemberDetails, false);
        //new MandatoryCheck().putMandatoryMarks(getClass().getName(), panChangeOfMemberDetails);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panChangeOfMemberDetails, getMandatoryHashMap());
        setVisibleEnableDisable(false);
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
        btnNewMemberNo.setName("btnNewMemberNo");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        btnSchemeNameOpen.setName("btnSchemeNameOpen");
        btnView.setName("btnView");
        lblDivisionNo.setName("lblDivisionNo");
        lblEffetiveDt.setName("lblEffetiveDt");
        lblExistingMemName.setName("lblExistingMemName");
        lblExistingMemberName.setName("lblExistingMemberName");
        //   lblChittalNo.setName("lblExistingNo");
        lblInstallmentNo.setName("lblInstallmentNo");
        lblMunnal.setName("lblMunnal");
        lblMsg.setName("lblMsg");
        lblNewMemberName.setName("lblNewMemberName");
        lblNewMemberNo.setName("lblNewMemberNo");
        lblRemarks.setName("lblRemarks");
        lblChittalNo.setName("lblChittalNo");
        lblSchemeName.setName("lblSchemeName");
        lblSpace.setName("lblSpace");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblSpace5.setName("lblSpace5");
        lblSpace6.setName("lblSpace6");
        lblStatus.setName("lblStatus");
        lblTotalAmount.setName("lblTotalAmount");
        mbrMain.setName("mbrMain");
        panChangeOfMemberDetails.setName("panChangeOfMemberDetails");
        panSchemeName.setName("panSchemeName");
        panNewMemberNo.setName("panNewMemberNo");
        panInsideChangeOfMemberDetails.setName("panInsideChangeOfMemberDetails");
        panManual.setName("panManual");
        panStatus.setName("panStatus");
        rdoManualAllowed_no.setName("rdoManualAllowed_no");
        rdoManualAllowed_yes.setName("rdoManualAllowed_yes");
        tabChangeOfMemberDetails.setName("tabChangeOfMemberDetails");
        tdtEffetiveDt.setName("tdtEffetiveDt");
        txtChittalNo.setName("txtChittalNo");
        txtInstallmentNo.setName("txtInstallmentNo");
        lblNewMembName.setName("lblNewMembName");
        txtNewMemberNo.setName("txtNewMemberNo");
        txtRemarks.setName("txtRemarks");
        txtSchemeName.setName("txtSchemeName");
        txtTotalAmount.setName("txtTotalAmount");
        txtDivisionNo.setName("txtDivisionNo");
        txtSubNo.setName("txtSubNo");
    }

    /* Auto Generated Method - internationalize()
     This method used to assign display texts from
     the Resource Bundle File. */
    private void internationalize() {
    }

    private void setMaxLengths() {
        txtChittalNo.setAllowAll(true);
        txtNewMemberNo.setAllowAll(true);
        txtDivisionNo.setValidation(new NumericValidation());
        txtSubNo.setValidation(new NumericValidation());
        txtSchemeName.setAllowAll(true);
        //        txtChitNo.setValidation(new NumericValidation());
        txtInstallmentNo.setValidation(new NumericValidation());
        txtTotalAmount.setValidation(new CurrencyValidation(14, 2));
        txtRemarks.setAllowAll(true);
    }

    private void initComponentData() {
        try {
            //            cboDivisionNo.setModel(observable.getCbmDivisionNo());
        } catch (ClassCastException e) {
            parseException.logException(e, true);
        }
    }

    /* Auto Generated Method - setMandatoryHashMap()
     
     ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
     
     This method list out all the Input Fields available in the UI.
     It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtInstallmentNo", new Boolean(true));
        mandatoryMap.put("txtRemarks", new Boolean(false));
        mandatoryMap.put("txtTotalAmount", new Boolean(true));
        mandatoryMap.put("tdtEffetiveDt", new Boolean(true));
        mandatoryMap.put("txtSchemeName", new Boolean(true));
        mandatoryMap.put("txtNewMemberNo", new Boolean(true));
        mandatoryMap.put("txtChittalNo", new Boolean(true));
        mandatoryMap.put("txtDivisionNo", new Boolean(true));
    }

    /**
     * Auto Generated Method - getMandatoryHashMap() Getter method for setMandatoryHashMap().
     */
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    /* Auto Generated Method - update()
     This method called by Observable. It updates the UI with
     Observable's data. If needed add/Remove RadioButtons
     method need to be added.*/
    public void update(Observable observed, Object arg) {
    }

    public void update() {
        txtInstallmentNo.setText(CommonUtil.convertObjToStr(observable.getTxtInstallmentNo())); //AJITH
        txtRemarks.setText(observable.getTxtRemarks());
        txtTotalAmount.setText(CommonUtil.convertObjToStr(observable.getTxtTotalAmount())); //AJITH
        tdtEffetiveDt.setDateValue(observable.getTdtEffetiveDt());
        txtSchemeName.setText(observable.getTxtSchemeName());
        txtChittalNo.setText(observable.getTxtExistingNo());
        txtDivisionNo.setText(CommonUtil.convertObjToStr(observable.getTxtDivisionNo()));   //AJITH
        txtSubNo.setText(CommonUtil.convertObjToStr(observable.getTxtSubNo())); //AJITH
        String munnal = CommonUtil.convertObjToStr(observable.getRdoMunnal());
        if (munnal.length() > 0 && munnal.equals("N")) {
            setVisibleEnableDisable(true);
            txtNewMemberNo.setText(observable.getTxtNewMemberNo());
            rdoManualAllowed_yes.setSelected(false);
            rdoManualAllowed_no.setSelected(true);
        } else {
            setVisibleEnableDisable(false);
            rdoManualAllowed_yes.setSelected(true);
            rdoManualAllowed_no.setSelected(false);
        }
        lblNewMembName.setText(observable.getTxtNewMemberName());
        lblExistingMemName.setText(observable.getTxtOldMemberName());
        lblExistingMemberNumber.setText(observable.getTxtChitNo());
    }

    /* Auto Generated Method - updateOBFields()
     This method called by Save option of UI.
     It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setTxtInstallmentNo(CommonUtil.convertObjToInt(txtInstallmentNo.getText())); //AJITH
        observable.setTxtRemarks(txtRemarks.getText());
        observable.setTxtTotalAmount(CommonUtil.convertObjToDouble(txtTotalAmount.getText()));  //AJITH
        observable.setTdtEffetiveDt(tdtEffetiveDt.getDateValue());
        observable.setTxtSchemeName(txtSchemeName.getText());
        observable.setTxtNewMemberNo(txtNewMemberNo.getText());
        observable.setTxtExistingNo(txtChittalNo.getText());
        observable.setTxtDivisionNo(CommonUtil.convertObjToInt(txtDivisionNo.getText()));   //AJITH
        observable.setTxtSubNo(CommonUtil.convertObjToInt(txtSubNo.getText()));   //AJITH
        observable.setTxtOldMemberName(lblExistingMemName.getText());
        observable.setTxtNewMemberName(lblNewMembName.getText());
        if (rdoManualAllowed_yes.isSelected() == true) {
            observable.setRdoMunnal(CommonUtil.convertObjToStr("Y"));
        } else {
            observable.setRdoMunnal(CommonUtil.convertObjToStr("N"));
        }
        observable.setTxtChitNo(lblExistingMemberNumber.getText());
    }

    /* Auto Generated Method - setHelpMessage()
     This method shows tooltip help for all the input fields
     available in the UI. It needs the Mandatory Resource Bundle
     object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        MDSChangeofMemberMRB objMandatoryRB = new MDSChangeofMemberMRB();
        txtInstallmentNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInstallmentNo"));
        txtRemarks.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRemarks"));
        txtTotalAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTotalAmount"));
        tdtEffetiveDt.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtEffetiveDt"));
        btnSchemeNameOpen.setHelpMessage(lblMsg, objMandatoryRB.getString("btnSchemeNameOpen"));
        btnNewMemberNo.setHelpMessage(lblMsg, objMandatoryRB.getString("btnNewMemberNo"));
        txtChittalNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtChittalNo"));
        txtDivisionNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDivisionNo"));
        rdoManualAllowed_yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoManualAllowed_yes"));
        rdoManualAllowed_no.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoManualAllowed_no"));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoManualAllowed = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrAdvances = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace6 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace40 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace41 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace42 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace43 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace44 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace45 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        tabChangeOfMemberDetails = new com.see.truetransact.uicomponent.CTabbedPane();
        panChangeOfMemberDetails = new com.see.truetransact.uicomponent.CPanel();
        panInsideChangeOfMemberDetails = new com.see.truetransact.uicomponent.CPanel();
        lblNewMemberName = new com.see.truetransact.uicomponent.CLabel();
        lblSchemeName = new com.see.truetransact.uicomponent.CLabel();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        lblMunnal = new com.see.truetransact.uicomponent.CLabel();
        lblEffetiveDt = new com.see.truetransact.uicomponent.CLabel();
        lblExistingMemName = new com.see.truetransact.uicomponent.CLabel();
        lblChittalNo = new com.see.truetransact.uicomponent.CLabel();
        panManual = new com.see.truetransact.uicomponent.CPanel();
        rdoManualAllowed_yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoManualAllowed_no = new com.see.truetransact.uicomponent.CRadioButton();
        txtInstallmentNo = new com.see.truetransact.uicomponent.CTextField();
        txtRemarks = new com.see.truetransact.uicomponent.CTextField();
        lblNewMemberNo = new com.see.truetransact.uicomponent.CLabel();
        lblTotalAmount = new com.see.truetransact.uicomponent.CLabel();
        lblInstallmentNo = new com.see.truetransact.uicomponent.CLabel();
        lblDivisionNo = new com.see.truetransact.uicomponent.CLabel();
        txtTotalAmount = new com.see.truetransact.uicomponent.CTextField();
        tdtEffetiveDt = new com.see.truetransact.uicomponent.CDateField();
        panSchemeName = new com.see.truetransact.uicomponent.CPanel();
        txtSchemeName = new com.see.truetransact.uicomponent.CTextField();
        btnSchemeNameOpen = new com.see.truetransact.uicomponent.CButton();
        panNewMemberNo = new com.see.truetransact.uicomponent.CPanel();
        txtNewMemberNo = new com.see.truetransact.uicomponent.CTextField();
        btnNewMemberNo = new com.see.truetransact.uicomponent.CButton();
        lblExistingMemberName = new com.see.truetransact.uicomponent.CLabel();
        lblNewMembName = new com.see.truetransact.uicomponent.CLabel();
        txtDivisionNo = new com.see.truetransact.uicomponent.CTextField();
        panChittalNo = new com.see.truetransact.uicomponent.CPanel();
        txtChittalNo = new com.see.truetransact.uicomponent.CTextField();
        btnChittalNo = new com.see.truetransact.uicomponent.CButton();
        txtSubNo = new com.see.truetransact.uicomponent.CTextField();
        lblExistingMemberNo = new com.see.truetransact.uicomponent.CLabel();
        lblExistingMemberNumber = new com.see.truetransact.uicomponent.CLabel();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace = new com.see.truetransact.uicomponent.CLabel();
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
        mitAuthorize = new javax.swing.JMenuItem();
        mitReject = new javax.swing.JMenuItem();
        mitException = new javax.swing.JMenuItem();
        sptException = new javax.swing.JSeparator();
        mitPrint = new javax.swing.JMenuItem();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setMinimumSize(new java.awt.Dimension(825, 650));
        setPreferredSize(new java.awt.Dimension(825, 650));
        getContentPane().setLayout(new java.awt.GridBagLayout());

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
        tbrAdvances.add(btnView);

        lblSpace6.setText("     ");
        tbrAdvances.add(lblSpace6);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnNew);

        lblSpace40.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace40.setText("     ");
        lblSpace40.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace40.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace40.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace40);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnEdit);

        lblSpace41.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace41.setText("     ");
        lblSpace41.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace41.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace41.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace41);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnDelete);

        lblSpace3.setText("     ");
        tbrAdvances.add(lblSpace3);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnSave);

        lblSpace42.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace42.setText("     ");
        lblSpace42.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace42.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace42.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace42);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnCancel);

        lblSpace4.setText("     ");
        tbrAdvances.add(lblSpace4);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnAuthorize);

        lblSpace43.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace43.setText("     ");
        lblSpace43.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace43.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace43.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace43);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnException);

        lblSpace44.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace44.setText("     ");
        lblSpace44.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace44.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace44.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace44);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnReject);

        lblSpace5.setText("     ");
        tbrAdvances.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrAdvances.add(btnPrint);

        lblSpace45.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace45.setText("     ");
        lblSpace45.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace45.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace45.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace45);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnClose);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(tbrAdvances, gridBagConstraints);

        tabChangeOfMemberDetails.setMinimumSize(new java.awt.Dimension(680, 480));
        tabChangeOfMemberDetails.setPreferredSize(new java.awt.Dimension(680, 480));

        panChangeOfMemberDetails.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panChangeOfMemberDetails.setMinimumSize(new java.awt.Dimension(570, 450));
        panChangeOfMemberDetails.setPreferredSize(new java.awt.Dimension(570, 450));
        panChangeOfMemberDetails.setLayout(new java.awt.GridBagLayout());

        panInsideChangeOfMemberDetails.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panInsideChangeOfMemberDetails.setMinimumSize(new java.awt.Dimension(800, 375));
        panInsideChangeOfMemberDetails.setPreferredSize(new java.awt.Dimension(835, 375));
        panInsideChangeOfMemberDetails.setLayout(new java.awt.GridBagLayout());

        lblNewMemberName.setText("New Member Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsideChangeOfMemberDetails.add(lblNewMemberName, gridBagConstraints);

        lblSchemeName.setText("MDS Scheme Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInsideChangeOfMemberDetails.add(lblSchemeName, gridBagConstraints);

        lblRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInsideChangeOfMemberDetails.add(lblRemarks, gridBagConstraints);

        lblMunnal.setText("Whether New Member a Munnal");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsideChangeOfMemberDetails.add(lblMunnal, gridBagConstraints);

        lblEffetiveDt.setText("Change Effective Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsideChangeOfMemberDetails.add(lblEffetiveDt, gridBagConstraints);

        lblExistingMemName.setForeground(new java.awt.Color(0, 51, 204));
        lblExistingMemName.setText("                                          ");
        lblExistingMemName.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsideChangeOfMemberDetails.add(lblExistingMemName, gridBagConstraints);

        lblChittalNo.setText("Chittal No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsideChangeOfMemberDetails.add(lblChittalNo, gridBagConstraints);

        panManual.setLayout(new java.awt.GridBagLayout());

        rdoManualAllowed.add(rdoManualAllowed_yes);
        rdoManualAllowed_yes.setText("Yes");
        rdoManualAllowed_yes.setMaximumSize(new java.awt.Dimension(48, 21));
        rdoManualAllowed_yes.setMinimumSize(new java.awt.Dimension(48, 18));
        rdoManualAllowed_yes.setPreferredSize(new java.awt.Dimension(48, 18));
        rdoManualAllowed_yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoManualAllowed_yesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panManual.add(rdoManualAllowed_yes, gridBagConstraints);

        rdoManualAllowed.add(rdoManualAllowed_no);
        rdoManualAllowed_no.setText("No");
        rdoManualAllowed_no.setMinimumSize(new java.awt.Dimension(46, 18));
        rdoManualAllowed_no.setPreferredSize(new java.awt.Dimension(46, 18));
        rdoManualAllowed_no.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoManualAllowed_noActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panManual.add(rdoManualAllowed_no, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 5, 1, 1);
        panInsideChangeOfMemberDetails.add(panManual, gridBagConstraints);

        txtInstallmentNo.setMinimumSize(new java.awt.Dimension(50, 21));
        txtInstallmentNo.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 2);
        panInsideChangeOfMemberDetails.add(txtInstallmentNo, gridBagConstraints);

        txtRemarks.setMinimumSize(new java.awt.Dimension(200, 21));
        txtRemarks.setPreferredSize(new java.awt.Dimension(200, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 2);
        panInsideChangeOfMemberDetails.add(txtRemarks, gridBagConstraints);

        lblNewMemberNo.setText("New Member No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsideChangeOfMemberDetails.add(lblNewMemberNo, gridBagConstraints);

        lblTotalAmount.setText("Total Amount paid till date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsideChangeOfMemberDetails.add(lblTotalAmount, gridBagConstraints);

        lblInstallmentNo.setText("Installment No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsideChangeOfMemberDetails.add(lblInstallmentNo, gridBagConstraints);

        lblDivisionNo.setText("Division No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInsideChangeOfMemberDetails.add(lblDivisionNo, gridBagConstraints);

        txtTotalAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 2);
        panInsideChangeOfMemberDetails.add(txtTotalAmount, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 2);
        panInsideChangeOfMemberDetails.add(tdtEffetiveDt, gridBagConstraints);

        panSchemeName.setLayout(new java.awt.GridBagLayout());

        txtSchemeName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSchemeName.add(txtSchemeName, gridBagConstraints);

        btnSchemeNameOpen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnSchemeNameOpen.setPreferredSize(new java.awt.Dimension(19, 21));
        btnSchemeNameOpen.setEnabled(false);
        btnSchemeNameOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSchemeNameOpenActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSchemeName.add(btnSchemeNameOpen, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 5, 1, 1);
        panInsideChangeOfMemberDetails.add(panSchemeName, gridBagConstraints);

        panNewMemberNo.setMinimumSize(new java.awt.Dimension(122, 22));
        panNewMemberNo.setPreferredSize(new java.awt.Dimension(123, 21));
        panNewMemberNo.setLayout(new java.awt.GridBagLayout());

        txtNewMemberNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panNewMemberNo.add(txtNewMemberNo, gridBagConstraints);

        btnNewMemberNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnNewMemberNo.setPreferredSize(new java.awt.Dimension(19, 21));
        btnNewMemberNo.setEnabled(false);
        btnNewMemberNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewMemberNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panNewMemberNo.add(btnNewMemberNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 5, 1, 1);
        panInsideChangeOfMemberDetails.add(panNewMemberNo, gridBagConstraints);

        lblExistingMemberName.setText("Existing Member Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsideChangeOfMemberDetails.add(lblExistingMemberName, gridBagConstraints);

        lblNewMembName.setForeground(new java.awt.Color(0, 51, 204));
        lblNewMembName.setText("                                          ");
        lblNewMembName.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsideChangeOfMemberDetails.add(lblNewMembName, gridBagConstraints);

        txtDivisionNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDivisionNo.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 2);
        panInsideChangeOfMemberDetails.add(txtDivisionNo, gridBagConstraints);

        panChittalNo.setPreferredSize(new java.awt.Dimension(169, 30));
        panChittalNo.setLayout(new java.awt.GridBagLayout());

        txtChittalNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panChittalNo.add(txtChittalNo, gridBagConstraints);

        btnChittalNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnChittalNo.setEnabled(false);
        btnChittalNo.setPreferredSize(new java.awt.Dimension(19, 21));
        btnChittalNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChittalNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panChittalNo.add(btnChittalNo, gridBagConstraints);

        txtSubNo.setMinimumSize(new java.awt.Dimension(40, 21));
        txtSubNo.setPreferredSize(new java.awt.Dimension(40, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 0);
        panChittalNo.add(txtSubNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 1);
        panInsideChangeOfMemberDetails.add(panChittalNo, gridBagConstraints);

        lblExistingMemberNo.setText("Existing Member No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsideChangeOfMemberDetails.add(lblExistingMemberNo, gridBagConstraints);

        lblExistingMemberNumber.setForeground(new java.awt.Color(0, 51, 204));
        lblExistingMemberNumber.setText("                                          ");
        lblExistingMemberNumber.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsideChangeOfMemberDetails.add(lblExistingMemberNumber, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChangeOfMemberDetails.add(panInsideChangeOfMemberDetails, gridBagConstraints);

        tabChangeOfMemberDetails.addTab("Change of Member Details", panChangeOfMemberDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(tabChangeOfMemberDetails, gridBagConstraints);

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace, gridBagConstraints);

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
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(panStatus, gridBagConstraints);

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

        mitAuthorize.setText("Authorize");
        mnuProcess.add(mitAuthorize);

        mitReject.setText("Rejection");
        mnuProcess.add(mitReject);

        mitException.setText("Exception");
        mnuProcess.add(mitException);
        mnuProcess.add(sptException);

        mitPrint.setText("Print");
        mnuProcess.add(mitPrint);

        mitClose.setText("Close");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrMain.add(mnuProcess);

        setJMenuBar(mbrMain);
    }// </editor-fold>//GEN-END:initComponents

    private void txtNewMemberNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNewMemberNoFocusLost
        // TODO add your handling code here:
        setMemberNo("EXISTING_CUSTOMER");
    }//GEN-LAST:event_txtNewMemberNoFocusLost

    private void btnNewMemberNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewMemberNoActionPerformed
        // TODO add your handling code here:

        String goldLoan = "";
        new CheckCustomerIdUI(this);
//        if(txtSchemeName.getText().length()>0 && txtChittalNo.getText().length()>0){
//            popUp("MEMBER_NO");
//        }
    }//GEN-LAST:event_btnNewMemberNoActionPerformed

    private void btnChittalNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChittalNoActionPerformed
        // TODO add your handling code here:
        popUp("CHITTAL_NO");
    }//GEN-LAST:event_btnChittalNoActionPerformed

    private void btnSchemeNameOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSchemeNameOpenActionPerformed
        // TODO add your handling code here:
        popUp("SCHEME_DETAILS");
    }//GEN-LAST:event_btnSchemeNameOpenActionPerformed

    private void rdoManualAllowed_noActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoManualAllowed_noActionPerformed
        // TODO add your handling code here:
        setVisibleEnableDisable(true);
        btnNewMemberNo.setEnabled(true);
    }//GEN-LAST:event_rdoManualAllowed_noActionPerformed

    private void rdoManualAllowed_yesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoManualAllowed_yesActionPerformed
        // TODO add your handling code here:
        setVisibleEnableDisable(false);
        txtNewMemberNo.setText("");
    }//GEN-LAST:event_rdoManualAllowed_yesActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        btnView.setEnabled(false);
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        popUp("Enquiry");
        ClientUtil.enableDisable(panChangeOfMemberDetails, false);
        lblStatus.setText("Enquiry");
        btnSave.setEnabled(false);
        btnView.setEnabled(false);
    }            //    private void enableDisableAliasBranchTable(boolean flag) {//GEN-LAST:event_btnViewActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        updateAuthorizeStatus(CommonConstants.STATUS_REJECTED);
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        updateAuthorizeStatus(CommonConstants.STATUS_EXCEPTION);
        btnCancel.setEnabled(true);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        updateAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnCancel.setEnabled(true);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    private void updateAuthorizeStatus(String authorizeStatus) {

        if (viewType == AUTHORIZE && isFilled) {
            ArrayList arrList = new ArrayList();
            HashMap authorizeMap = new HashMap();
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("CHITTAL_NO", observable.getTxtExistingNo());
            singleAuthorizeMap.put("SUB_NO", CommonUtil.convertObjToInt(observable.getTxtSubNo())); //AJITH
            singleAuthorizeMap.put("SCHEME_NAME", observable.getTxtSchemeName());
            singleAuthorizeMap.put("AUTHORIZED_BY", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("AUTHORIZED_DT", ClientUtil.getCurrentDateWithTime());
            if(rdoManualAllowed_yes.isSelected()){ // Added by nithya on 23 Jun 2025 for KD-4275
               singleAuthorizeMap.put("MUNNAL_CHITTAL", "Y"); 
            }
            arrList.add(singleAuthorizeMap);
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorize(authorizeMap, observable.getTxtExistingNo());
            viewType = "";
            super.setOpenForEditBy(observable.getStatusBy());
            singleAuthorizeMap = null;
            arrList = null;
            authorizeMap = null;
        } else {
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put("USER_ID", TrueTransactMain.USER_ID);
            whereMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            mapParam.put(CommonConstants.MAP_NAME, "getChangeOfMemberAuthorize");
            isFilled = false;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
            observable.setStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }

    public void authorize(HashMap map, String id) {
        System.out.println("Authorize Map : " + map);

        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
            observable.set_authorizeMap(map);
            observable.doAction();
            if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                super.setOpenForEditBy(observable.getStatusBy());
                super.removeEditLock(id);
            }
            btnCancelActionPerformed(null);
            observable.setStatus();
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
        ClientUtil.enableDisable(panChangeOfMemberDetails, true);
        ClientUtil.clearAll(this);
        lblStatus.setText("New");
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        enableDisableButtons(true);
        setVisibleEnableDisable(false);
        rdoManualAllowed_yes.setSelected(true);
        txtSchemeName.setEnabled(false);
        txtChittalNo.setEnabled(false);
        lblExistingMemName.setText("");
        txtSubNo.setEnabled(false);
    }//GEN-LAST:event_btnNewActionPerformed
    public void setVisibleEnableDisable(boolean flag) {
        lblNewMemberNo.setVisible(flag);
        panNewMemberNo.setVisible(flag);
        lblNewMemberName.setVisible(flag);
        lblNewMembName.setVisible(flag);
    }
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        popUp("Edit");
        lblStatus.setText("Edit");
        btnDelete.setEnabled(false);
        ClientUtil.enableDisable(panChangeOfMemberDetails, true);
        txtChittalNo.setEnabled(false);
        txtSchemeName.setEnabled(false);
        btnSchemeNameOpen.setEnabled(false);
        btnNewMemberNo.setEnabled(true);
        txtDivisionNo.setEnabled(false);
        txtInstallmentNo.setEnabled(false);
        txtTotalAmount.setEnabled(false);
        txtSubNo.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        popUp("Delete");
        lblStatus.setText("Delete");
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        ClientUtil.enableDisable(panChangeOfMemberDetails, false);
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        btnSave.setEnabled(false);
        updateOBFields();
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panChangeOfMemberDetails);

        StringBuffer message = new StringBuffer("");
        if (txtSchemeName.getText().equals("")) {
            message.append(objMandatoryRB.getString("txtSchemeName"));
            message.append("\n");
        }
        if (txtChittalNo.getText().toString().equals("")) {
            message.append(objMandatoryRB.getString("txtChittalNo"));
            message.append("\n");
        }
        if (rdoManualAllowed_no.isSelected()) {
            if (txtNewMemberNo.getText().equals("")) {
                message.append(objMandatoryRB.getString("btnNewMemberNo"));
                message.append("\n");
            }
        }

        if (tdtEffetiveDt.getDateValue().equals("")) {
            message.append(objMandatoryRB.getString("tdtEffetiveDt"));
            message.append("\n");
        }



        //Portion is for calculating exp date
        // setExpDateOnCalculation();


        // */
        //setExpDateOnCalculation();
        //  System.out.println("status saveAction: "+status);
        if (message.length() > 0) {
            displayAlert(message.toString());
        } else {
            System.out.println("STRINGGG" + mandatoryMessage);
            // ClientUtil.showMessageWindow(mandatoryMessage);
            if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0) {
                displayAlert(mandatoryMessage);
            } else {
                if (rdoManualAllowed_no.isSelected()) {
                    if (txtNewMemberNo.getText().length() > 0) {
                        savePerformed();
                        btnCancel.setEnabled(true);
                    } else {
                        ClientUtil.showMessageWindow("NewMemberNo should not be empty!!!");
                    }
                } else {
                    savePerformed();
                    btnCancel.setEnabled(true);
                }
            }
        }
    }//GEN-LAST:event_btnSaveActionPerformed
    public void enableDisableButtons(boolean flag) {
        btnSchemeNameOpen.setEnabled(flag);
        btnNewMemberNo.setEnabled(flag);
    }
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        deletescreenLock();
        viewType = "CANCEL";
        lblStatus.setText("               ");
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(this, false);
        setModified(false);
        btnNew.setEnabled(true);
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        btnSave.setEnabled(false);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
        btnView.setEnabled(true);
        isFilled = false;
        setVisibleEnableDisable(false);
        enableDisableButtons(false);
        btnChittalNo.setEnabled(false);
        lblExistingMemName.setText("");
        lblNewMembName.setText("");
        lblExistingMemberNumber.setText("");
    }//GEN-LAST:event_btnCancelActionPerformed
    private void deletescreenLock() {
        HashMap map = new HashMap();
        map.put("USER_ID", ProxyParameters.USER_ID);
        map.put("TRANS_DT", currDt.clone());
        map.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
        ClientUtil.execute("DELETE_SCREEN_LOCK", map);
    }

    public void insertCustTableRecords(HashMap hash) {
        txtNewMemberNo.setText(CommonUtil.convertObjToStr(hash.get("MEMBER NO")));
        lblNewMembName.setText(CommonUtil.convertObjToStr(hash.get("FNAME")));
    }

    /**
     * To display a popUp window for viewing existing data
     */
    private void popUp(String currAction) {
        viewType = currAction;
        HashMap viewMap = new HashMap();
        if (currAction.equalsIgnoreCase("Edit") || currAction.equalsIgnoreCase("Delete")) {
            HashMap map = new HashMap();
            map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getChangeOfMemberEditDelete");
        } else if (currAction.equalsIgnoreCase("Enquiry")) {
            HashMap map = new HashMap();
            map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getChangeOfMemberView");
        } else if (currAction.equals("SCHEME_DETAILS")) {
            HashMap map = new HashMap();
            map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getSelectEachSchemeDetails");
        } else if (currAction.equals("CHITTAL_NO")) {
            HashMap map = new HashMap();
            map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            map.put("SCHEME_NAMES", txtSchemeName.getText());
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getSelectRecordForEnquiryDetails");
        }
//        }else if (currAction.equalsIgnoreCase("MEMBER_NO")) {
//            HashMap map = new HashMap();
//            map.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
//            map.put("SCHEME_NAME",txtSchemeName.getText());
//            viewMap.put(CommonConstants.MAP_WHERE, map);
//            
//            //            getMemeberShipDetails
//            viewMap.put(CommonConstants.MAP_NAME, "getMemberDetailsFromMDSApplication");
//        }
        new ViewAll(this, viewMap).show();
    }

    private void setMemberNo(String currAction) {
        if (currAction.equals("EXISTING_CUSTOMER") && txtNewMemberNo.getText().length() > 0) {
            HashMap existingMap = new HashMap();
            existingMap.put("MEMBERSHIP_NO", txtNewMemberNo.getText());
            List mapDataList = ClientUtil.executeQuery("getSelectExistingMemberName", existingMap);
            if (mapDataList != null && mapDataList.size() > 0) {
                existingMap = (HashMap) mapDataList.get(0);
                lblNewMembName.setText(CommonUtil.convertObjToStr(existingMap.get("CUSTOMER NAME")));
            } else {
                ClientUtil.showAlertWindow("Invalid Member No ");
                txtNewMemberNo.setText("");
                lblNewMembName.setText("");
                return;
            }
        }
    }

    public void fillData(Object obj) {
        try {
            HashMap hashMap = (HashMap) obj;
            System.out.println("### fillData Hash : " + hashMap);
            isFilled = true;
//            if (viewType.equalsIgnoreCase("MEMBER_NO")) {
//                txtNewMemberNo.setText(CommonUtil.convertObjToStr(hashMap.get("MEMBER_NO")));
//                lblNewMembName.setText(CommonUtil.convertObjToStr(hashMap.get("CUSTOMER")));
//                observable.setTxtNewMemberNo(CommonUtil.convertObjToStr(hashMap.get("MEMBER_NO")));
//            }else
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
                this.setButtonEnableDisable();
                observable.getData(hashMap);
                update();
            } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
                this.setButtonEnableDisable();
                observable.setTxtExistingNo(CommonUtil.convertObjToStr(hashMap.get("CHITTAL_NO")));
                observable.setTxtSchemeName(CommonUtil.convertObjToStr(hashMap.get("SCHEME_NAME")));
                observable.getData(hashMap);
                update();
            } else if (viewType.equalsIgnoreCase("SCHEME_DETAILS")) {
                List schemeLst = (List) ClientUtil.executeQuery("getSchemeDetailsList", hashMap);
                if (schemeLst != null && schemeLst.size() > 0) {
                    HashMap productMap = new HashMap();
                    productMap = (HashMap) schemeLst.get(0);
                    if (productMap.get("NON_PRIZED_CHANGE") != null && !productMap.get("NON_PRIZED_CHANGE").equals("") && (productMap.get("NON_PRIZED_CHANGE").equals("Y"))) {
                        HashMap recordMap = new HashMap();
                        recordMap.put("SCHEME_NAME", hashMap.get("SCHEME_NAME"));
                        java.util.List lst = ClientUtil.executeQuery("getSchemeDivisionetailsList", recordMap);
                        if (lst != null && lst.size() > 0) {
                            recordMap = (HashMap) lst.get(0);
                            int noOfDiv = CommonUtil.convertObjToInt(recordMap.get("NO_OF_DIVISIONS"));
                            int totMember = CommonUtil.convertObjToInt(recordMap.get("TOTAL_NO_OF_MEMBERS"));
                            int totChitNo = CommonUtil.convertObjToInt(recordMap.get("NEXT_CHITTAL_NO"));
                            if (CommonUtil.convertObjToStr(recordMap.get("COMMENCEMENT_AUTHORIZE_STATUS")).equals("")) {
                                ClientUtil.showAlertWindow("Commencement is done pending for authorization");
                                btnCancelActionPerformed(null);
                                return;
                            } else if (CommonUtil.convertObjToStr(recordMap.get("MULTIPLE_MEMBER")).equals("N") && CommonUtil.convertObjToInt(recordMap.get("TOTAL_CHIT_NO")) != CommonUtil.convertObjToInt(recordMap.get("TOTAL_NO_OF_MEMBERS"))) {
                                ClientUtil.showAlertWindow("All the application should be entered or authorized");
                                btnCancelActionPerformed(null);
                                return;
                            }
                        }
                        observable.setTxtSchemeName(CommonUtil.convertObjToStr(hashMap.get("SCHEME_NAME")));
                        txtSchemeName.setText(CommonUtil.convertObjToStr(hashMap.get("SCHEME_NAME")));
                        txtChittalNo.setEnabled(false);
                        btnChittalNo.setEnabled(true);
                    } else {
                        ClientUtil.showMessageWindow("Changing Of Member Not Allowed...!!! ");
                        ClientUtil.enableDisable(panInsideChangeOfMemberDetails, false);
                    }
                }
            } else if (viewType.equalsIgnoreCase("CHITTAL_NO")) {
                hashMap.put("SUB_NO", CommonUtil.convertObjToInt(hashMap.get("SUB_NO")));   //AJITH
                //List prizedLst = (List) ClientUtil.executeQuery("getSelectRecordDetails", hashMap);
                List prizedLst = (List) ClientUtil.executeQuery("getSelectMDSPaymentDetails", hashMap);
                if (prizedLst != null && prizedLst.size() > 0) {
                    ClientUtil.showMessageWindow("This Chittal No Has Already Prized  Money Payment done So Can't be Changed...!!! ");
                    ClientUtil.clearAll(this);
                    lblExistingMemName.setText("");
                    lblExistingMemberNumber.setText("");
                    txtSchemeName.setText(CommonUtil.convertObjToStr(hashMap.get("SCHEME_NAME")));
                } else {
                    observable.setTxtChitNo(CommonUtil.convertObjToStr(hashMap.get("CHITTAL_NO")));
                    observable.setTxtSubNo(CommonUtil.convertObjToInt(hashMap.get("SUB_NO")));  //AJITH
                    txtSubNo.setText(CommonUtil.convertObjToStr(observable.getTxtSubNo())); //AJITH
                    observable.setReceiptDetails(hashMap);
                    txtChittalNo.setText(observable.getTxtExistingNo());
                    txtDivisionNo.setText(CommonUtil.convertObjToStr(observable.getTxtDivisionNo()));   //AJITH
                    lblExistingMemName.setText(observable.getTxtOldMemberName());
                    lblExistingMemberNumber.setText(observable.getTxtChitNo());
                    txtDivisionNo.setEnabled(false);
                    HashMap whereMap = new HashMap();
                    whereMap.put("CHITTAL_NO", observable.getTxtExistingNo());
                    whereMap.put("SUB_NO", CommonUtil.convertObjToInt(observable.getTxtSubNo()));   //AJITH
                    List list = ClientUtil.executeQuery("getTotalInstAmount", whereMap);
                    if (list != null && list.size() > 0) {
                        whereMap = (HashMap) list.get(0);
                        txtInstallmentNo.setText(CommonUtil.convertObjToStr(whereMap.get("NO_INST_PAID")));
                        txtTotalAmount.setText(CommonUtil.convertObjToStr(whereMap.get("PAID_AMT")));
                        txtInstallmentNo.setEnabled(false);
                    }
                }
            }
            if (viewType == AUTHORIZE) {
                btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
            }
            hashMap = null;
            btnCancel.setEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed

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

    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed

    /**
     * This method helps in popoualting the data from the data base
     *
     * @param currField Action the argument is passed according to the command
     * issued
     */
    private void callView(String currField) {
    }

    private void displayAlert(String message) {
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }

    private void savePerformed() {
        observable.set_authorizeMap(null); 
        //added by rishad for avoiding doubling issue 05/08/2015
        CommonUtil comm = new CommonUtil();
        final JDialog loading = comm.addProgressBar();
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws InterruptedException /** Execute some operation */
            {
                try {
                    observable.doAction();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void done() {
                loading.dispose();
            }
        };
        worker.execute();
        loading.show();
        try {
            worker.get();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        if (rdoManualAllowed_yes.isSelected() == false && rdoManualAllowed_no.isSelected() == false) {
            ClientUtil.displayAlert("Please Select New Member Munnal Yes Or No");
        }
        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            btnCancelActionPerformed(null);
            btnCancel.setEnabled(true);
            lblStatus.setText(ClientConstants.RESULT_STATUS[observable.getResult()]);
        }
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        setModified(false);
        ClientUtil.clearAll(this);
    }

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

        btnAuthorize.setEnabled(btnNew.isEnabled());
        btnReject.setEnabled(btnNew.isEnabled());
        btnException.setEnabled(btnNew.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
        lblStatus.setText(observable.getLblStatus());
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnChittalNo;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnNewMemberNo;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnSchemeNameOpen;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CLabel lblChittalNo;
    private com.see.truetransact.uicomponent.CLabel lblDivisionNo;
    private com.see.truetransact.uicomponent.CLabel lblEffetiveDt;
    private com.see.truetransact.uicomponent.CLabel lblExistingMemName;
    private com.see.truetransact.uicomponent.CLabel lblExistingMemberName;
    private com.see.truetransact.uicomponent.CLabel lblExistingMemberNo;
    private com.see.truetransact.uicomponent.CLabel lblExistingMemberNumber;
    private com.see.truetransact.uicomponent.CLabel lblInstallmentNo;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblMunnal;
    private com.see.truetransact.uicomponent.CLabel lblNewMembName;
    private com.see.truetransact.uicomponent.CLabel lblNewMemberName;
    private com.see.truetransact.uicomponent.CLabel lblNewMemberNo;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
    private com.see.truetransact.uicomponent.CLabel lblSchemeName;
    private com.see.truetransact.uicomponent.CLabel lblSpace;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace40;
    private com.see.truetransact.uicomponent.CLabel lblSpace41;
    private com.see.truetransact.uicomponent.CLabel lblSpace42;
    private com.see.truetransact.uicomponent.CLabel lblSpace43;
    private com.see.truetransact.uicomponent.CLabel lblSpace44;
    private com.see.truetransact.uicomponent.CLabel lblSpace45;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace6;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTotalAmount;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitAuthorize;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitException;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitReject;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panChangeOfMemberDetails;
    private com.see.truetransact.uicomponent.CPanel panChittalNo;
    private com.see.truetransact.uicomponent.CPanel panInsideChangeOfMemberDetails;
    private com.see.truetransact.uicomponent.CPanel panManual;
    private com.see.truetransact.uicomponent.CPanel panNewMemberNo;
    private com.see.truetransact.uicomponent.CPanel panSchemeName;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CButtonGroup rdoManualAllowed;
    private com.see.truetransact.uicomponent.CRadioButton rdoManualAllowed_no;
    private com.see.truetransact.uicomponent.CRadioButton rdoManualAllowed_yes;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private javax.swing.JSeparator sptException;
    private com.see.truetransact.uicomponent.CTabbedPane tabChangeOfMemberDetails;
    private javax.swing.JToolBar tbrAdvances;
    private com.see.truetransact.uicomponent.CDateField tdtEffetiveDt;
    private com.see.truetransact.uicomponent.CTextField txtChittalNo;
    private com.see.truetransact.uicomponent.CTextField txtDivisionNo;
    private com.see.truetransact.uicomponent.CTextField txtInstallmentNo;
    private com.see.truetransact.uicomponent.CTextField txtNewMemberNo;
    private com.see.truetransact.uicomponent.CTextField txtRemarks;
    private com.see.truetransact.uicomponent.CTextField txtSchemeName;
    private com.see.truetransact.uicomponent.CTextField txtSubNo;
    private com.see.truetransact.uicomponent.CTextField txtTotalAmount;
    // End of variables declaration//GEN-END:variables

    public static void main(String[] arg) {
        try {
            javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Throwable th) {
            th.printStackTrace();
        }
        javax.swing.JFrame jf = new javax.swing.JFrame();
        MDSChangeofMemberUI gui = new MDSChangeofMemberUI();
        jf.getContentPane().add(gui);
        jf.setSize(536, 566);
        jf.show();
        gui.show();
    }
}