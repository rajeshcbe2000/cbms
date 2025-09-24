 /*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * MDSLetterGenerationUI.java
 *
 *
 */
package com.see.truetransact.ui.mdsapplication.mdslettergeneration;

import java.util.*;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.ui.customer.CheckCustomerIdUI;
import javax.swing.*;

/**
 *
 * @author Suresh
 *
 *
 */
public class MDSLetterGenerationUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer, UIMandatoryField {

    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.mdsapplication.mdslettergeneration.MDSLetterGenerationRB", ProxyParameters.LANGUAGE);
    MDSLetterGenerationOB observable = null;
    private HashMap mandatoryMap;
    private String viewType = "";
    private boolean isFilled = false;
    private Date curr_dt = null;

    /** Creates new form BeanForm */
    public MDSLetterGenerationUI() {
        initComponents();
        settingupUI();
    }

    private void settingupUI() {
        setFieldNames();
        internationalize();
        observable = new MDSLetterGenerationOB();
        setMandatoryHashMap();
        setMaximumLength();
        setHelpMessage();
        setButtonEnableDisable();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panMDSLettorGeneration);
        ClientUtil.enableDisable(this, false);
        curr_dt = ClientUtil.getCurrentDate();
        lblLettorNo.setVisible(false);
        lblLettorNoVal.setVisible(false);
        btnAuthorize.setVisible(false);
        btnException.setVisible(false);
        btnReject.setVisible(false);
        lblCancelDt.setEnabled(false);
        tdtCancelDt.setEnabled(false);
        chkCancel.setEnabled(false);
        tdtFromDt.setEnabled(false);
        tdtValidUpto.setEnabled(false);
        btnCustomerId.setEnabled(false);
        tdtCancelDt.setDateValue(CommonUtil.convertObjToStr(curr_dt.clone()));
    }

    /* Auto Generated Method - setFieldNames()
    This method assigns name for all the components.
    Other functions are working based on this name. */
    private void setFieldNames() {
        btnAuthorize.setName("btnAuthorize");
        btnCancel.setName("btnCancel");
        btnChittalNo.setName("btnChittalNo");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        btnSchemeName.setName("btnSchemeName");
        btnView.setName("btnView");
        lblAuctionAmount.setName("lblAuctionAmount");
        lblChittalNo.setName("lblChittalNo");
        lblFromDt.setName("lblFromDt");
        lblLettorNo.setName("lblLettorNo");
        lblLettorNoVal.setName("lblLettorNoVal");
        lblMsg.setName("lblMsg");
        lblSchemeName.setName("lblSchemeName");
        lblSpace.setName("lblSpace");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblSpace5.setName("lblSpace5");
        lblSpace6.setName("lblSpace6");
        lblStatus.setName("lblStatus");
//        lblToDt.setName("lblToDt");
        mbrMain.setName("mbrMain");
        panChittalNo.setName("panChittalNo");
        panMDSLettorGeneration.setName("panMDSLettorGeneration");
        panMDSSchemeName.setName("panMDSSchemeName");
        panStatus.setName("panStatus");
        tabMDSLettorGeneration.setName("tabMDSLettorGeneration");
        tdtFromDt.setName("tdtFromDt");
        //  tdtToDt.setName("tdtToDt");
        txtAuctionAmount.setName("txtAuctionAmount");
        txtChittalNo.setName("txtChittalNo");
        txtSchemeName.setName("txtSchemeName");
    }

    private void internationalize() {
        resourceBundle = new MDSLetterGenerationRB();
        lblSchemeName.setText(resourceBundle.getString("lblSchemeName"));
        lblLettorNo.setText(resourceBundle.getString("lblLettorNo"));
        lblChittalNo.setText(resourceBundle.getString("lblChittalNo"));
        lblAuctionAmount.setText(resourceBundle.getString("lblAuctionAmount"));
        lblFromDt.setText(resourceBundle.getString("lblFromDt"));
//        lblToDt.setText(resourceBundle.getString("lblToDt"));
    }

    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtSchemeName", new Boolean(true));
        mandatoryMap.put("txtChittalNo", new Boolean(true));
        mandatoryMap.put("txtAuctionAmount", new Boolean(true));
        mandatoryMap.put("tdtFromDt", new Boolean(true));
        mandatoryMap.put("tdtToDt", new Boolean(true));
    }

    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    private void setMaximumLength() {
        txtSchemeName.setAllowAll(true);
        txtChittalNo.setAllowAll(true);
        txtNoOfConsecMonth.setAllowNumber(true);
    }

    public void update(Observable observed, Object arg) {
    }

    public void updateOBFields() {
        observable.setTxtSchemeName(txtSchemeName.getText());
        observable.setTxtChittalNo(txtChittalNo.getText());
        observable.setTxtAuctionAmount(txtAuctionAmount.getText());
        observable.setTdtFromDt(tdtFromDt.getDateValue());
        observable.setTdtToDt(tdtValidUpto.getDateValue());
        //  if(CommonConstants.SAL_REC_MODULE=="y"||CommonConstants.SAL_REC_MODULE=="Y"){
        // observable.setLblLettorNoVal1(lblLettorNoVal1.getText());
        //observable.setTdtConductedOnDt(DateUtil.getDateMMDDYYYY(tdtConductedOnDt.getDateValue()));
        observable.setTdtValidUpto(DateUtil.getDateMMDDYYYY(tdtValidUpto.getDateValue()));
        // observable.setTxtChittalNo1(txtChittalNo1.getText());
        observable.setTxtDiscountUpto(txtDiscountUpto.getText());
        observable.setTxtNoOfConsecMonth(txtNoOfConsecMonth.getText());
        //observable.setTxtSchemeName1(txtSchemeName1.getText());
        if (chkCancel.isSelected() == true) {
            observable.setChkCancel("true");
            observable.setTdtCancelDt(DateUtil.getDateMMDDYYYY(tdtCancelDt.getDateValue()));
        } else {
            observable.setChkCancel("false");
            observable.setTdtCancelDt(null);
        }
        // }
    }

    public void update() {
        // if(panMDSLettorGeneration.isShowing()==true){
        txtSchemeName.setText(observable.getTxtSchemeName());
        lblLettorNoVal.setText(observable.getLblLettorNoVal());
        txtChittalNo.setText(observable.getTxtChittalNo());
        txtAuctionAmount.setText(observable.getTxtAuctionAmount());
        tdtFromDt.setDateValue(observable.getTdtFromDt());
        //tdtToDt.setDateValue(observable.getTdtToDt());
        // }else if(panMDSLetter.isShowing()==true){
        // txtChittalNo1.setText(observable.getTxtChittalNo1());
        txtDiscountUpto.setText(observable.getTxtDiscountUpto());
        txtNoOfConsecMonth.setText(observable.getTxtNoOfConsecMonth());
        // txtSchemeName1.setText(observable.getTxtSchemeName1());
        // tdtConductedOnDt.setDateValue(CommonUtil.convertObjToStr(observable.getTdtConductedOnDt()));
        tdtValidUpto.setDateValue(CommonUtil.convertObjToStr(observable.getTdtValidUpto()));
        tdtCancelDt.setDateValue(CommonUtil.convertObjToStr(observable.getTdtCancelDt()));
        if (observable.getChkCancel() == "true") {
            chkCancel.setSelected(true);
        } else {
            chkCancel.setSelected(false);
        }
        // lblLettorNoVal1.setText(observable.getLblLettorNoVal1());
        // }


    }

    public void setHelpMessage() {
        MDSLetterGenerationMRB objMandatoryRB = new MDSLetterGenerationMRB();
        txtSchemeName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSchemeName"));
        txtChittalNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtChittalNo"));
        txtAuctionAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAuctionAmount"));
        tdtFromDt.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtFromDt"));
        //tdtToDt.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtToDt"));
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
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdgIsLapsedGR = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgEFTProductGR = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgPayableBranchGR = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgPrintServicesGR = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgSeriesGR = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrAdvances = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace6 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace46 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace47 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace48 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace49 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace50 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace51 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        tabMDSLettorGeneration = new com.see.truetransact.uicomponent.CTabbedPane();
        panMDSLettorGeneration = new com.see.truetransact.uicomponent.CPanel();
        panMDSSchemeName = new com.see.truetransact.uicomponent.CPanel();
        txtSchemeName = new com.see.truetransact.uicomponent.CTextField();
        btnSchemeName = new com.see.truetransact.uicomponent.CButton();
        lblSchemeName = new com.see.truetransact.uicomponent.CLabel();
        panChittalNo = new com.see.truetransact.uicomponent.CPanel();
        txtChittalNo = new com.see.truetransact.uicomponent.CTextField();
        btnChittalNo = new com.see.truetransact.uicomponent.CButton();
        lblChittalNo = new com.see.truetransact.uicomponent.CLabel();
        lblAuctionAmount = new com.see.truetransact.uicomponent.CLabel();
        txtAuctionAmount = new com.see.truetransact.uicomponent.CTextField();
        lblFromDt = new com.see.truetransact.uicomponent.CLabel();
        tdtFromDt = new com.see.truetransact.uicomponent.CDateField();
        lblLettorNo = new com.see.truetransact.uicomponent.CLabel();
        lblLettorNoVal = new com.see.truetransact.uicomponent.CLabel();
        lblMemberNameVal = new com.see.truetransact.uicomponent.CLabel();
        lblCancelDt = new com.see.truetransact.uicomponent.CLabel();
        tdtCancelDt = new com.see.truetransact.uicomponent.CDateField();
        lblNoOfConsecMonth = new com.see.truetransact.uicomponent.CLabel();
        txtNoOfConsecMonth = new com.see.truetransact.uicomponent.CTextField();
        lblDiscountUpto = new com.see.truetransact.uicomponent.CLabel();
        txtDiscountUpto = new com.see.truetransact.uicomponent.CTextField();
        chkCancel = new com.see.truetransact.uicomponent.CCheckBox();
        tdtValidUpto = new com.see.truetransact.uicomponent.CDateField();
        lblValidUpto = new com.see.truetransact.uicomponent.CLabel();
        lblCustomerId = new com.see.truetransact.uicomponent.CLabel();
        panMDSSchemeName1 = new com.see.truetransact.uicomponent.CPanel();
        txtCustomerId = new com.see.truetransact.uicomponent.CTextField();
        btnCustomerId = new com.see.truetransact.uicomponent.CButton();
        lblSchemeDetails = new com.see.truetransact.uicomponent.CLabel();
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
        setMinimumSize(new java.awt.Dimension(500, 510));
        setPreferredSize(new java.awt.Dimension(500, 510));
        getContentPane().setLayout(new java.awt.GridBagLayout());

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

        lblSpace46.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace46.setText("     ");
        lblSpace46.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace46.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace46.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace46);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnEdit);

        lblSpace47.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace47.setText("     ");
        lblSpace47.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace47.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace47.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace47);

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

        lblSpace48.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace48.setText("     ");
        lblSpace48.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace48.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace48.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace48);

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

        lblSpace49.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace49.setText("     ");
        lblSpace49.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace49.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace49.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace49);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnException);

        lblSpace50.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace50.setText("     ");
        lblSpace50.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace50.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace50.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace50);

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
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnPrint);

        lblSpace51.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace51.setText("     ");
        lblSpace51.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace51.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace51.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace51);

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

        tabMDSLettorGeneration.setMinimumSize(new java.awt.Dimension(550, 490));
        tabMDSLettorGeneration.setPreferredSize(new java.awt.Dimension(550, 490));

        panMDSLettorGeneration.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panMDSLettorGeneration.setMinimumSize(new java.awt.Dimension(800, 450));
        panMDSLettorGeneration.setPreferredSize(new java.awt.Dimension(800, 450));
        panMDSLettorGeneration.setLayout(new java.awt.GridBagLayout());

        panMDSSchemeName.setPreferredSize(new java.awt.Dimension(122, 21));
        panMDSSchemeName.setLayout(new java.awt.GridBagLayout());

        txtSchemeName.setAllowAll(true);
        txtSchemeName.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSchemeName.setNextFocusableComponent(txtChittalNo);
        txtSchemeName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSchemeNameFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panMDSSchemeName.add(txtSchemeName, gridBagConstraints);

        btnSchemeName.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnSchemeName.setEnabled(false);
        btnSchemeName.setMaximumSize(new java.awt.Dimension(21, 21));
        btnSchemeName.setMinimumSize(new java.awt.Dimension(21, 21));
        btnSchemeName.setPreferredSize(new java.awt.Dimension(21, 21));
        btnSchemeName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSchemeNameActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panMDSSchemeName.add(btnSchemeName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 0, 0);
        panMDSLettorGeneration.add(panMDSSchemeName, gridBagConstraints);

        lblSchemeName.setText("MDS Scheme Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 51, 0, 0);
        panMDSLettorGeneration.add(lblSchemeName, gridBagConstraints);

        panChittalNo.setLayout(new java.awt.GridBagLayout());

        txtChittalNo.setAllowAll(true);
        txtChittalNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtChittalNo.setNextFocusableComponent(tdtFromDt);
        txtChittalNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtChittalNoActionPerformed(evt);
            }
        });
        txtChittalNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtChittalNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panChittalNo.add(txtChittalNo, gridBagConstraints);

        btnChittalNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnChittalNo.setEnabled(false);
        btnChittalNo.setMaximumSize(new java.awt.Dimension(21, 21));
        btnChittalNo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnChittalNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnChittalNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChittalNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panChittalNo.add(btnChittalNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 0, 0);
        panMDSLettorGeneration.add(panChittalNo, gridBagConstraints);

        lblChittalNo.setText("Chittal No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 113, 0, 0);
        panMDSLettorGeneration.add(lblChittalNo, gridBagConstraints);

        lblAuctionAmount.setText("Auction Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 77, 0, 0);
        panMDSLettorGeneration.add(lblAuctionAmount, gridBagConstraints);

        txtAuctionAmount.setAllowNumber(true);
        txtAuctionAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAuctionAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAuctionAmountActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 0, 0);
        panMDSLettorGeneration.add(txtAuctionAmount, gridBagConstraints);

        lblFromDt.setText("From Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 109, 0, 0);
        panMDSLettorGeneration.add(lblFromDt, gridBagConstraints);

        tdtFromDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtFromDtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 0, 0);
        panMDSLettorGeneration.add(tdtFromDt, gridBagConstraints);

        lblLettorNo.setText("Letter No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(53, 117, 0, 0);
        panMDSLettorGeneration.add(lblLettorNo, gridBagConstraints);

        lblLettorNoVal.setForeground(new java.awt.Color(0, 51, 204));
        lblLettorNoVal.setMaximumSize(new java.awt.Dimension(125, 18));
        lblLettorNoVal.setMinimumSize(new java.awt.Dimension(100, 18));
        lblLettorNoVal.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.ipadx = 22;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(53, 8, 0, 0);
        panMDSLettorGeneration.add(lblLettorNoVal, gridBagConstraints);

        lblMemberNameVal.setForeground(new java.awt.Color(0, 51, 204));
        lblMemberNameVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblMemberNameVal.setMaximumSize(new java.awt.Dimension(150, 18));
        lblMemberNameVal.setMinimumSize(new java.awt.Dimension(150, 18));
        lblMemberNameVal.setPreferredSize(new java.awt.Dimension(150, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 10, 0, 0);
        panMDSLettorGeneration.add(lblMemberNameVal, gridBagConstraints);

        lblCancelDt.setText("Cancel Date");
        lblCancelDt.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 99, 0, 0);
        panMDSLettorGeneration.add(lblCancelDt, gridBagConstraints);

        tdtCancelDt.setEnabled(false);
        tdtCancelDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtCancelDtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 20;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 53, 0);
        panMDSLettorGeneration.add(tdtCancelDt, gridBagConstraints);

        lblNoOfConsecMonth.setText("No Of Consecutive Month");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 23, 0, 0);
        panMDSLettorGeneration.add(lblNoOfConsecMonth, gridBagConstraints);

        txtNoOfConsecMonth.setMinimumSize(new java.awt.Dimension(100, 21));
        txtNoOfConsecMonth.setNextFocusableComponent(tdtValidUpto);
        txtNoOfConsecMonth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNoOfConsecMonthActionPerformed(evt);
            }
        });
        txtNoOfConsecMonth.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNoOfConsecMonthFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 0, 0);
        panMDSLettorGeneration.add(txtNoOfConsecMonth, gridBagConstraints);

        lblDiscountUpto.setText("Discount Upto");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 88, 0, 0);
        panMDSLettorGeneration.add(lblDiscountUpto, gridBagConstraints);

        txtDiscountUpto.setAllowNumber(true);
        txtDiscountUpto.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDiscountUpto.setNextFocusableComponent(chkCancel);
        txtDiscountUpto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDiscountUptoActionPerformed(evt);
            }
        });
        txtDiscountUpto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDiscountUptoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 0, 0);
        panMDSLettorGeneration.add(txtDiscountUpto, gridBagConstraints);

        chkCancel.setText("Cancel");
        chkCancel.setEnabled(false);
        chkCancel.setNextFocusableComponent(tdtCancelDt);
        chkCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkCancelActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 19;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 36, 0, 0);
        panMDSLettorGeneration.add(chkCancel, gridBagConstraints);

        tdtValidUpto.setEnabled(false);
        tdtValidUpto.setNextFocusableComponent(txtDiscountUpto);
        tdtValidUpto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtValidUptoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 0, 0);
        panMDSLettorGeneration.add(tdtValidUpto, gridBagConstraints);

        lblValidUpto.setText("Valid Upto");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 110, 0, 0);
        panMDSLettorGeneration.add(lblValidUpto, gridBagConstraints);

        lblCustomerId.setText("Customer ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 98, 0, 0);
        panMDSLettorGeneration.add(lblCustomerId, gridBagConstraints);

        panMDSSchemeName1.setPreferredSize(new java.awt.Dimension(122, 21));
        panMDSSchemeName1.setLayout(new java.awt.GridBagLayout());

        txtCustomerId.setAllowAll(true);
        txtCustomerId.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCustomerId.setNextFocusableComponent(txtChittalNo);
        txtCustomerId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCustomerIdActionPerformed(evt);
            }
        });
        txtCustomerId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCustomerIdFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panMDSSchemeName1.add(txtCustomerId, gridBagConstraints);

        btnCustomerId.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnCustomerId.setEnabled(false);
        btnCustomerId.setMaximumSize(new java.awt.Dimension(21, 21));
        btnCustomerId.setMinimumSize(new java.awt.Dimension(21, 21));
        btnCustomerId.setPreferredSize(new java.awt.Dimension(21, 21));
        btnCustomerId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustomerIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panMDSSchemeName1.add(btnCustomerId, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 0, 0);
        panMDSLettorGeneration.add(panMDSSchemeName1, gridBagConstraints);

        lblSchemeDetails.setForeground(new java.awt.Color(0, 51, 204));
        lblSchemeDetails.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblSchemeDetails.setMaximumSize(new java.awt.Dimension(150, 18));
        lblSchemeDetails.setMinimumSize(new java.awt.Dimension(150, 18));
        lblSchemeDetails.setPreferredSize(new java.awt.Dimension(150, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 18, 0, 76);
        panMDSLettorGeneration.add(lblSchemeDetails, gridBagConstraints);

        tabMDSLettorGeneration.addTab("MDS Letter Generation", panMDSLettorGeneration);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(tabMDSLettorGeneration, gridBagConstraints);

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

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
        if (chkCancel.isSelected() == true) {
            int yesNo = 0;
            String[] options = {"Yes", "No"};
            yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                    COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                    null, options, options[0]);
            System.out.println("#$#$$ yesNo : " + yesNo);
            if (yesNo == 0) {
                TTIntegration ttIntgration = null;
                HashMap paramMap = new HashMap();
                paramMap.put("ChittalNo", txtChittalNo.getText());
                paramMap.put("Amount", txtAuctionAmount.getText());
                //  paramMap.put("FromDt", getProperDate(DateUtil.getDateMMDDYYYY(tdtFromDt.getDateValue())));
                //  paramMap.put("ToDt", getProperDate(DateUtil.getDateMMDDYYYY(tdtToDt.getDateValue())));
                ttIntgration.setParam(paramMap);
                ttIntgration.integrationForPrint("proxyCancellation", false);
            }
        } else {
            generateReport();
        }
    }//GEN-LAST:event_btnPrintActionPerformed

    private void tdtFromDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtFromDtFocusLost
        // TODO add your handling code here:
        if (tdtFromDt.getDateValue().length() > 0) {
            HashMap whereMap = new HashMap();
            whereMap.put("CHITTAL_NO", txtChittalNo.getText());
            whereMap.put("FROM_DT", getProperDate(DateUtil.getDateMMDDYYYY(tdtFromDt.getDateValue())));
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                whereMap.put("LETTER_NO", lblLettorNoVal.getText());
            }
            List recordList = ClientUtil.executeQuery("checkingPeriodForMDSAuthLettor", whereMap);
            if (recordList != null && recordList.size() > 0) {
                ClientUtil.showMessageWindow("Authorization Letter Exists For This Period !!! ");
                tdtFromDt.setDateValue("");
                return;
            }
        }
        FromAndToDateChecking();
    }//GEN-LAST:event_tdtFromDtFocusLost
    private Date getProperDate(Date sourceDate) {
        Date targetDate = (Date) curr_dt.clone();
        targetDate.setDate(sourceDate.getDate());
        targetDate.setMonth(sourceDate.getMonth());
        targetDate.setYear(sourceDate.getYear());
        return targetDate;
    }

    private void FromAndToDateChecking() {
//        if(tdtToDt.getDateValue().length()>0 && tdtFromDt.getDateValue().length()>0){
//            Date toDt = DateUtil.getDateMMDDYYYY(tdtToDt.getDateValue());
//            Date fromDt= DateUtil.getDateMMDDYYYY(tdtFromDt.getDateValue());
//            if(toDt.before(fromDt)){
//                ClientUtil.displayAlert("To Date Should Be Greater Than From Date !!! ");
//                tdtToDt.setDateValue("");
//                return;
//            }
//        }
    }
    private void txtChittalNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtChittalNoFocusLost
        // TODO add your handling code here:

        if (txtChittalNo.getText().length() > 0) {
            HashMap whereMap = new HashMap();
            whereMap.put("CHITTAL_NO", txtChittalNo.getText());
            List lst = ClientUtil.executeQuery("getSelectChittalNo", whereMap);
            if (lst != null && lst.size() > 0) {
                viewType = "CHITTTAL_NO";
                whereMap = (HashMap) lst.get(0);
                List chitLst = ClientUtil.executeQuery("getSelectRecordForNotEnteredDetails", whereMap);
                if (chitLst != null && chitLst.size() > 0) {
                    whereMap = (HashMap) chitLst.get(0);
                    fillData(whereMap);
                    chitLst = null;
                    lst = null;
                    whereMap = null;
                } else {
                    ClientUtil.displayAlert("Chittal Already Prized !!! ");
                    txtChittalNo.setText("");
                    lblMemberNameVal.setText("");
                }
            } else {
                ClientUtil.displayAlert("Invalid Chittal No !!! ");
                txtChittalNo.setText("");
                lblMemberNameVal.setText("");
            }
        }
    }//GEN-LAST:event_txtChittalNoFocusLost

    private void txtSchemeNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSchemeNameFocusLost
        // TODO add your handling code here:
        if (txtSchemeName.getText().length() > 0) {
            HashMap whereMap = new HashMap();
            whereMap.put("SCHEME_NAME", txtSchemeName.getText());
            List lst = ClientUtil.executeQuery("getSelectSchemeName", whereMap);
            if (lst != null && lst.size() > 0) {
                viewType = "SCHEME_NAME";
                whereMap = (HashMap) lst.get(0);
                List chitLst = ClientUtil.executeQuery("getSelectEachSchemeDetailsList", whereMap);
                if (chitLst != null && chitLst.size() > 0) {
                    whereMap = (HashMap) chitLst.get(0);
                    fillData(whereMap);
                    chitLst = null;
                    lst = null;
                    whereMap = null;
                }
            } else {
                ClientUtil.displayAlert("Invalid Scheme Name !!! ");
                txtSchemeName.setText("");
            }
        }
    }//GEN-LAST:event_txtSchemeNameFocusLost

    private void btnChittalNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChittalNoActionPerformed
        // TODO add your handling code here:

        popUp("CHITTTAL_NO");
        HashMap whereMap = new HashMap();
        whereMap.put("SCHEME_NAME", txtSchemeName.getText());
        List lst = ClientUtil.executeQuery("getFromDateForLetterGeneration", whereMap);
        System.out.println("lst//////////" + lst + whereMap);
    }//GEN-LAST:event_btnChittalNoActionPerformed

    private void btnSchemeNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSchemeNameActionPerformed
        // TODO add your handling code here:
        popUp("SCHEME_NAME");
    }//GEN-LAST:event_btnSchemeNameActionPerformed

    /** To display a popUp window for viewing existing data */
    private void popUp(String currAction) {
        viewType = currAction;
        HashMap viewMap = new HashMap();
        if (currAction.equalsIgnoreCase("SCHEME_NAME")) {
            HashMap newwhere = new HashMap();
            newwhere.put("CUSTOMER_ID", txtCustomerId.getText());
            //   HashMap map = new HashMap();
            viewMap.put(CommonConstants.MAP_WHERE, newwhere);

            viewMap.put(CommonConstants.MAP_NAME, "getSelectEachSchemeDetailsNew");
            System.out.println(" viewMap=========" + viewMap);
        } else if (currAction.equalsIgnoreCase("CHITTTAL_NO")) {
            HashMap where = new HashMap();
            where.put("SCHEME_NAMES", txtSchemeName.getText());
            where.put("CUST_ID", txtCustomerId.getText());
            viewMap.put(CommonConstants.MAP_WHERE, where);
            viewMap.put(CommonConstants.MAP_NAME, "getSelectRecordForNotEnteredDetailsNew");
        } else if (currAction.equalsIgnoreCase("Edit") || currAction.equalsIgnoreCase("Delete") || currAction.equalsIgnoreCase("Enquiry")) {
            HashMap map = new HashMap();
            viewMap.put(CommonConstants.MAP_NAME, "getMDSLettorGenerationEditDelete");
        }
        //else if (currAction.equalsIgnoreCase("Edit1") || currAction.equalsIgnoreCase("Delete1")){
        // HashMap map = new HashMap();
        // viewMap.put(CommonConstants.MAP_NAME, "getMDSLetterEditDeleteData");   
        // }
        new ViewAll(this, viewMap).show();
    }

    public void fillData(Object obj) {
        try {
            HashMap hashMap = (HashMap) obj;
            System.out.println("### fillData Hash : " + hashMap);
            isFilled = true;
            if (viewType == "Customer") {
                txtCustomerId.setText(CommonUtil.convertObjToStr(hashMap.get("CUST_ID")));
            }
            if (viewType == "SCHEME_NAME") {
                clearChitDetails();
                observable.setTxtSchemeName(CommonUtil.convertObjToStr(hashMap.get("SCHEME_NAME")));
                txtSchemeName.setText(CommonUtil.convertObjToStr(hashMap.get("SCHEME_NAME")));
                btnChittalNo.setEnabled(true);
            } else if (viewType == "CHITTTAL_NO") {
                observable.setTxtChittalNo(CommonUtil.convertObjToStr(hashMap.get("CHITTAL_NO")) + '_' + CommonUtil.convertObjToStr(hashMap.get("SUB_NO")));
                HashMap whereMap5 = new HashMap();
                whereMap5.put("CHITAL_NO", hashMap.get("CHITTAL_NO"));
                String chittalNo = CommonUtil.convertObjToStr(observable.getTxtChittalNo());
                List lst5 = ClientUtil.executeQuery("getChitalNoFrmdsLetter", whereMap5);
                if (lst5 != null && lst5.size() > 0) {
                    HashMap Map = new HashMap();
                    for (int i = 0; i < lst5.size(); i++) {
                        Map = (HashMap) lst5.get(i);
                        if (Map != null && Map.size() > 0) {
                            String gotChittalNo = CommonUtil.convertObjToStr(Map.get("CHITTAL_NO"));
                            if (gotChittalNo != null && !gotChittalNo.equals("") && gotChittalNo.equals(chittalNo)) {
                                ClientUtil.showMessageWindow(" This chittal number is already Registered ");
                                txtChittalNo.setText("");
                                observable.setTxtChittalNo("");
                                return;
                            }else{
                                txtChittalNo.setText(CommonUtil.convertObjToStr(hashMap.get("CHITTAL_NO")) + '_' + CommonUtil.convertObjToStr(hashMap.get("SUB_NO")));
                                lblMemberNameVal.setText(CommonUtil.convertObjToStr(hashMap.get("MEMBER_NAME")));
                                lblSchemeDetails.setText(CommonUtil.convertObjToStr(hashMap.get("SCHEME_DESC")));
                            }
                        }
                    }
                } else {
                    txtChittalNo.setText(CommonUtil.convertObjToStr(hashMap.get("CHITTAL_NO")) + '_' + CommonUtil.convertObjToStr(hashMap.get("SUB_NO")));
                    lblMemberNameVal.setText(CommonUtil.convertObjToStr(hashMap.get("MEMBER_NAME")));
                    lblSchemeDetails.setText(CommonUtil.convertObjToStr(hashMap.get("SCHEME_DESC")));
                }
            }
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
                lblLettorNo.setVisible(true);
                lblLettorNoVal.setVisible(true);
                // lblLettorNo1.setVisible(true);
                // lblLettorNoVal1.setVisible(true);
                this.setButtonEnableDisable();
                observable.getData(hashMap);
                // if(panMDSLettorGeneration.isShowing()==true){
                displayMemberName();
                //  }
                update();
                //if(panMDSLettorGeneration.isShowing()==true){
                generateReport();
                //  }//else if(panMDSLetter.isShowing()==true){
                // generateReport1();   
                // }
            }
          //Added by Anju
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                lblLettorNo.setVisible(true);
                lblLettorNoVal.setVisible(true);
                this.setButtonEnableDisable();
                observable.getData(hashMap);
                displayMemberName();
                update();
                if (chkCancel.isSelected() == true) {
                    int yesNo = 0;
                    String[] options = {"Yes", "No"};
                    yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                            COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                            null, options, options[0]);
                    System.out.println("#$#$$ yesNo : " + yesNo);
                    if (yesNo == 0) {
                        TTIntegration ttIntgration = null;
                        HashMap paramMap = new HashMap();
                        paramMap.put("ChittalNo", txtChittalNo.getText());
                        paramMap.put("Amount", txtAuctionAmount.getText());
                        //  paramMap.put("FromDt", getProperDate(DateUtil.getDateMMDDYYYY(tdtFromDt.getDateValue())));
                        //  paramMap.put("ToDt", getProperDate(DateUtil.getDateMMDDYYYY(tdtToDt.getDateValue())));
                        ttIntgration.setParam(paramMap);
                        ttIntgration.integrationForPrint("proxyCancellation", false);
                    }
                }
            }
            hashMap = null;
            btnCancel.setEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }

    private void clearChitDetails() {
        observable.resetForm();
        txtSchemeName.setText("");
        txtChittalNo.setText("");
        txtAuctionAmount.setText("");
        tdtFromDt.setDateValue("");
        //  tdtToDt.setDateValue("");
        lblMemberNameVal.setText("");
    }

    private void displayMemberName() {
        HashMap whereMap = new HashMap();
        String chittalNo = CommonUtil.convertObjToStr(observable.getTxtChittalNo());
        whereMap.put("SCHEME_NAME", CommonUtil.convertObjToStr(observable.getTxtSchemeName()));
        if (chittalNo.indexOf("_") != -1) {
            whereMap.put("CHITTAL_NO", CommonUtil.convertObjToStr(chittalNo.substring(0, chittalNo.indexOf("_"))));
            whereMap.put("SUB_NO", CommonUtil.convertObjToInt(chittalNo.substring(chittalNo.indexOf("_") + 1, chittalNo.length())));
        }
        List listRec = ClientUtil.executeQuery("getApplDetails", whereMap);
        if (listRec != null && listRec.size() > 0) {
            double penal = 0;
            whereMap = (HashMap) listRec.get(0);
            lblMemberNameVal.setText(CommonUtil.convertObjToStr(whereMap.get("MEMBER_NAME")));
            txtCustomerId.setText(CommonUtil.convertObjToStr(whereMap.get("CUST_ID")));
        }
    }

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        btnView.setEnabled(false);
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        popUp("Enquiry");
        ClientUtil.enableDisable(panMDSLettorGeneration, false);
        lblStatus.setText("Enquiry");
        btnSave.setEnabled(false);
        btnView.setEnabled(false);
        btnSchemeName.setEnabled(false);
        btnChittalNo.setEnabled(false);
    }            //    private void enableDisableAliasBranchTable(boolean flag) {//GEN-LAST:event_btnViewActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
    }//GEN-LAST:event_btnAuthorizeActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        setModified(true);
        btnCustomerId.setEnabled(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
        ClientUtil.enableDisable(panMDSLettorGeneration, true);
        // ClientUtil.enableDisable(panMDSLetter,true);
        ClientUtil.clearAll(this);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        btnSchemeName.setEnabled(true);
        lblLettorNo.setVisible(false);
        lblLettorNoVal.setVisible(false);
        lblCancelDt.setEnabled(false);
        tdtCancelDt.setEnabled(false);
        chkCancel.setEnabled(false);
        //lblLettorNo1.setVisible(false);
        // lblLettorNoVal1.setVisible(false);
        lblMemberNameVal.setText("");
        btnCustomerId.setEnabled(true);
        tdtFromDt.setEnabled(false);
        tdtValidUpto.setEnabled(false);

    }//GEN-LAST:event_btnNewActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        if (panMDSLettorGeneration.isShowing() == true) {
            popUp("Edit");
        }
        lblStatus.setText("Edit");
        btnDelete.setEnabled(false);
        ClientUtil.enableDisable(panMDSLettorGeneration, true);
        // ClientUtil.enableDisable(panMDSLetter,true);
        btnSchemeName.setEnabled(false);
        btnChittalNo.setEnabled(false);
        txtSchemeName.setEnabled(false);
        txtChittalNo.setEnabled(false);
        // btnSchemeName1.setEnabled(false);
        // btnChittalNo1.setEnabled(false);
        // txtSchemeName1.setEnabled(false);
        //  txtChittalNo1.setEnabled(false);
        btnNew.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnSave.setEnabled(true);
        btnView.setEnabled(false);
        lblCancelDt.setEnabled(true);
        tdtCancelDt.setEnabled(true);
        chkCancel.setEnabled(true);
        chkCancel.setSelected(true);
        tdtFromDt.setEnabled(false);
        //Added by Anju
        if (chkCancel.isSelected() == true) {
            int yesNo = 0;
            String[] options = {"Yes", "No"};
            yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                    COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                    null, options, options[0]);
            System.out.println("#$#$$ yesNo : " + yesNo);
            if (yesNo == 0) {
                TTIntegration ttIntgration = null;
                HashMap paramMap = new HashMap();
                paramMap.put("ChittalNo", txtChittalNo.getText());
                paramMap.put("Amount", txtAuctionAmount.getText());
                paramMap.put("FromDt", getProperDate(DateUtil.getDateMMDDYYYY(tdtFromDt.getDateValue())));
                //  paramMap.put("FromDt", getProperDate(DateUtil.getDateMMDDYYYY(tdtFromDt.getDateValue())));
                //  paramMap.put("ToDt", getProperDate(DateUtil.getDateMMDDYYYY(tdtToDt.getDateValue())));
                ttIntgration.setParam(paramMap);
                ttIntgration.integrationForPrint("MDSAuthorizationLetter", false);
                
               // ttIntgration.integrationForPrint("proxyCancellation", false);
            }
        }
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        if (panMDSLettorGeneration.isShowing() == true) {
            popUp("Delete");
        }
        lblStatus.setText("Delete");
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        ClientUtil.enableDisable(panMDSLettorGeneration, false);
        btnSchemeName.setEnabled(false);
        btnChittalNo.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        btnSave.setEnabled(false);
        String mandatoryMessage = "";
        // if(panMDSLettorGeneration.isShowing()==true){
        mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panMDSLettorGeneration);
        // }

        if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0) {
            displayAlert(mandatoryMessage);
        } else {
            savePerformed();
            btnCancel.setEnabled(true);
        }
    }//GEN-LAST:event_btnSaveActionPerformed
    private void savePerformed() {
        updateOBFields();
         //added by rishad for avoiding doubling issue at 05/08/2015
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
        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) 
//   if(panMDSLettorGeneration.isShowing()==true){
            {
                generateReport();
            }
            //  }
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
        clearChitDetails();
    }
    private void generateReport() {
        try{
     
        int yesNo = 0;
        String[] options = {"Yes", "No"};
        yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                null, options, options[0]);
        System.out.println("#$#$$ yesNo : " + yesNo);
        if (yesNo == 0) {
            TTIntegration ttIntgration = null;
            HashMap paramMap = new HashMap();
            paramMap.put("ChittalNo", txtChittalNo.getText());
            paramMap.put("Amount", txtAuctionAmount.getText());
            paramMap.put("FromDt", getProperDate(DateUtil.getDateMMDDYYYY(tdtFromDt.getDateValue())));
//            paramMap.put("ToDt", getProperDate(DateUtil.getDateMMDDYYYY(tdtToDt.getDateValue())));
            ttIntgration.setParam(paramMap);
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT && chkCancel.isSelected()) {
                 ttIntgration.integrationForPrint("proxyCancellation", false);
            }
            ttIntgration.integrationForPrint("MDSAuthorizationLetter", false);
        }
        } catch(Exception e){
        	e.printStackTrace();   
        }         
    }
    private void displayAlert(String message) {
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
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
        btnSchemeName.setEnabled(false);
        btnChittalNo.setEnabled(false);
        observable.resetForm();
        lblLettorNoVal.setText("");
        lblMemberNameVal.setText("");
    }//GEN-LAST:event_btnCancelActionPerformed

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
        //        this.dispose();
    }//GEN-LAST:event_mitCloseActionPerformed

    private void tdtCancelDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtCancelDtFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tdtCancelDtFocusLost

    private void txtNoOfConsecMonthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNoOfConsecMonthActionPerformed
        // TODO add your handling code here:
//          if(txtNoOfConsecMonth.getText()!=null && !txtNoOfConsecMonth.getText().equals("")){
//         Mnthchk();
//          }
//         else{
//            ClientUtil.showMessageWindow("No of Consecutive Month Cannot be Empty");
//            return;
//        }
    }//GEN-LAST:event_txtNoOfConsecMonthActionPerformed

    private void txtNoOfConsecMonthFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNoOfConsecMonthFocusLost
        HashMap whereMap = new HashMap();
        whereMap.put("SCHEME_NAME", CommonUtil.convertObjToStr(observable.getTxtSchemeName()));
        List lst1 = ClientUtil.executeQuery("getFromDateFrmdsLetter", whereMap);
        HashMap Map = new HashMap();
        Map = (HashMap) lst1.get(0);
        System.out.println("MapMapMapMapMap" + Map);
        tdtFromDt.setDateValue(CommonUtil.convertObjToStr(Map.get("A_COUNT")));
        HashMap whereMap1 = new HashMap();
        whereMap1.put("NO_OF_MONTHS", txtNoOfConsecMonth.getText());
        whereMap1.put("SCHEME_NAME", CommonUtil.convertObjToStr(observable.getTxtSchemeName()));
        System.out.println("whereMap1====" + whereMap1);
        List lst2 = ClientUtil.executeQuery("getToDateFrmdsLetter", whereMap1);
        HashMap Map1 = new HashMap();
        Map1 = (HashMap) lst2.get(0);
        System.out.println("MapMapMapMapMap" + Map1);
        tdtValidUpto.setDateValue(CommonUtil.convertObjToStr(Map1.get("A_COUNT")));
    }//GEN-LAST:event_txtNoOfConsecMonthFocusLost

    private void chkCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkCancelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkCancelActionPerformed

    private void tdtValidUptoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtValidUptoFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tdtValidUptoFocusLost

private void txtCustomerIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCustomerIdFocusLost
}//GEN-LAST:event_txtCustomerIdFocusLost

private void btnCustomerIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustomerIdActionPerformed
    viewType = "Customer";
    new CheckCustomerIdUI(this);// TODO add your handling code here:
}//GEN-LAST:event_btnCustomerIdActionPerformed

private void txtAuctionAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAuctionAmountActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_txtAuctionAmountActionPerformed

private void txtDiscountUptoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDiscountUptoActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_txtDiscountUptoActionPerformed

private void txtDiscountUptoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDiscountUptoFocusLost
    HashMap whereMap = new HashMap();
    whereMap.put("SCHEME_NAME", CommonUtil.convertObjToStr(observable.getTxtSchemeName()));
    List lst = ClientUtil.executeQuery("getMinDiscValueFrMdsletter", whereMap);

    HashMap Map = new HashMap();
    if (lst.size() > 0) {
        Map = (HashMap) lst.get(0);
    }
    HashMap whereMap1 = new HashMap();
    whereMap.put("SCHEME_NAME", CommonUtil.convertObjToStr(observable.getTxtSchemeName()));
    List lst1 = ClientUtil.executeQuery("getMaxDiscValueFrMdsletter", whereMap);
    HashMap Map1 = new HashMap();
    Map1 = (HashMap) lst1.get(0);
    System.out.println("Map1Map1Map1Map1" + Map1);
    if ((CommonUtil.convertObjToDouble(Map.get("COMMISSION")).doubleValue() > CommonUtil.convertObjToDouble(txtDiscountUpto.getText()).doubleValue() || CommonUtil.convertObjToDouble(txtDiscountUpto.getText()).doubleValue() > CommonUtil.convertObjToDouble(Map1.get("MAX_AMT")).doubleValue())) {
        ClientUtil.showMessageWindow(" Amount should be greater than " + CommonUtil.convertObjToDouble(Map.get("COMMISSION")).doubleValue() + " and less than " + CommonUtil.convertObjToDouble(Map1.get("MAX_AMT")).doubleValue());
        txtDiscountUpto.setText(" ");
    } else {
        HashMap whereMap2 = new HashMap();
        whereMap2.put("SCHEME_NAME", CommonUtil.convertObjToStr(observable.getTxtSchemeName()));
        List lst2 = ClientUtil.executeQuery("getAuctionAmtFrMdsletter", whereMap2);
        HashMap Map2 = new HashMap();
        Map2 = (HashMap) lst2.get(0);
        System.out.println("Map2Map2Map2Map2" + Map2);
        txtAuctionAmount.setText(CommonUtil.convertObjToStr(CommonUtil.convertObjToDouble(Map2.get("TOTAL_AMOUNT_SCHEME")).doubleValue() - CommonUtil.convertObjToDouble(txtDiscountUpto.getText()).doubleValue()));
    }
}//GEN-LAST:event_txtDiscountUptoFocusLost

private void txtChittalNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtChittalNoActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_txtChittalNoActionPerformed

private void txtCustomerIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCustomerIdActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_txtCustomerIdActionPerformed

    private void Mnthchk() {
        try {
            int no_mnths = Integer.parseInt(txtNoOfConsecMonth.getText());
            java.util.GregorianCalendar gCalendar = new java.util.GregorianCalendar();
            //  gCalendar.setGregorianChange(DateUtil.getDateMMDDYYYY(tdtConductedOnDt.getDateValue()));
            //gCalendar.setTime(DateUtil.getDateMMDDYYYY(tdtConductedOnDt.getDateValue()));
            gCalendar.add(gCalendar.MONTH, no_mnths);
            tdtValidUpto.setDateValue(DateUtil.getStringDate(gCalendar.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnChittalNo;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnCustomerId;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnSchemeName;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CCheckBox chkCancel;
    private com.see.truetransact.uicomponent.CLabel lblAuctionAmount;
    private com.see.truetransact.uicomponent.CLabel lblCancelDt;
    private com.see.truetransact.uicomponent.CLabel lblChittalNo;
    private com.see.truetransact.uicomponent.CLabel lblCustomerId;
    private com.see.truetransact.uicomponent.CLabel lblDiscountUpto;
    private com.see.truetransact.uicomponent.CLabel lblFromDt;
    private com.see.truetransact.uicomponent.CLabel lblLettorNo;
    private com.see.truetransact.uicomponent.CLabel lblLettorNoVal;
    private com.see.truetransact.uicomponent.CLabel lblMemberNameVal;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNoOfConsecMonth;
    private com.see.truetransact.uicomponent.CLabel lblSchemeDetails;
    private com.see.truetransact.uicomponent.CLabel lblSchemeName;
    private com.see.truetransact.uicomponent.CLabel lblSpace;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace46;
    private com.see.truetransact.uicomponent.CLabel lblSpace47;
    private com.see.truetransact.uicomponent.CLabel lblSpace48;
    private com.see.truetransact.uicomponent.CLabel lblSpace49;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace50;
    private com.see.truetransact.uicomponent.CLabel lblSpace51;
    private com.see.truetransact.uicomponent.CLabel lblSpace6;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblValidUpto;
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
    private com.see.truetransact.uicomponent.CPanel panChittalNo;
    private com.see.truetransact.uicomponent.CPanel panMDSLettorGeneration;
    private com.see.truetransact.uicomponent.CPanel panMDSSchemeName;
    private com.see.truetransact.uicomponent.CPanel panMDSSchemeName1;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CButtonGroup rdgEFTProductGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgIsLapsedGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgPayableBranchGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgPrintServicesGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgSeriesGR;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private javax.swing.JSeparator sptException;
    private com.see.truetransact.uicomponent.CTabbedPane tabMDSLettorGeneration;
    private javax.swing.JToolBar tbrAdvances;
    private com.see.truetransact.uicomponent.CDateField tdtCancelDt;
    private com.see.truetransact.uicomponent.CDateField tdtFromDt;
    private com.see.truetransact.uicomponent.CDateField tdtValidUpto;
    private com.see.truetransact.uicomponent.CTextField txtAuctionAmount;
    private com.see.truetransact.uicomponent.CTextField txtChittalNo;
    private com.see.truetransact.uicomponent.CTextField txtCustomerId;
    private com.see.truetransact.uicomponent.CTextField txtDiscountUpto;
    private com.see.truetransact.uicomponent.CTextField txtNoOfConsecMonth;
    private com.see.truetransact.uicomponent.CTextField txtSchemeName;
    // End of variables declaration//GEN-END:variables

    public static void main(String[] arg) {
        try {
            javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Throwable th) {
            th.printStackTrace();
        }
        javax.swing.JFrame jf = new javax.swing.JFrame();
        MDSLetterGenerationUI gui = new MDSLetterGenerationUI();
        jf.getContentPane().add(gui);
        jf.setSize(536, 566);
        jf.show();
        gui.show();
    }
}