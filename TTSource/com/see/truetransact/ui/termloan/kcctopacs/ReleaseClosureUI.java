/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TermLoanUI.java
 *
 * Created on November 28, 2003, 3:55 PM
 */
package com.see.truetransact.ui.termloan.kcctopacs;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.util.Map;
import java.util.Date;
import java.util.List;
import javax.swing.table.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import org.apache.log4j.Logger;
import java.util.LinkedHashMap;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.uicomponent.CButtonGroup;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.ToDateValidation;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;


/*
 *
 * @author shanmugavel Created on November 28, 2003, 3:55 PM
 *
 */
public class ReleaseClosureUI extends CInternalFrame implements Observer, UIMandatoryField {

    HashMap mandatoryMap;
    ReleaseClosureOB observable;
    private boolean btnNewPressed;
    private final int KCC_ACC_NUM = 1;
    private final int FROM_REL_NUM = 2;
    private final int TO_REL_NUM = 3;
    private int ViewType;
    private final String PROD_ID = "PROD_ID";
    Date curDate;
    private boolean selectMode = false;
    private List finalList = null;
    private String view = new String();
    final String AUTHORIZE = "Authorize";
    private String AuthType = new String();
    private boolean isFilled = false;

    /**
     * Creates new form ReleaseClosureUI
     */
    public ReleaseClosureUI() {
        setObservable();
        initComponents();
        initComponentData();
        setMandatoryHashMap();
        setButtonEnableDisable();
        curDate = ClientUtil.getCurrentDate();
        ClientUtil.enableDisable(panRelSelectDet, false);
        tblReleaseDetails.setEnabled(false);
        update(observable, null);
        setMaxLength();
        btnDelete.setVisible(false);
    }

    private void setObservable() {
        try {
            observable = ReleaseClosureOB.getInstance();
            observable.addObserver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initComponentData() {
        cboKCCProdId.setModel(observable.getCbmKCCProdId());
        tblReleaseDetails.setModel(observable.getTblReleaseDet());
    }

    private void setMaxLength() {
        txtAccNo.setMaxLength(20);
        txtFromReleaseNo.setMaxLength(16);
        txtToReleaseNo.setMaxLength(16);
        txtAccNo.setAllowAll(true);
        txtFromReleaseNo.setAllowAll(true);
        txtToReleaseNo.setAllowAll(true);
    }

    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtAccNo", new Boolean(true));
        mandatoryMap.put("txtFromReleaseNo", new Boolean(true));
        mandatoryMap.put("txtToReleaseNo", new Boolean(true));
        mandatoryMap.put("cboKCCProdId", new Boolean(true));
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
        setAuthBtnEnableDisable();
    }
        
    private void setAuthBtnEnableDisable() {
        final boolean enableDisable = !btnSave.isEnabled();
        btnAuthorize.setEnabled(enableDisable);
        btnException.setEnabled(enableDisable);
        btnReject.setEnabled(enableDisable);
    }

    private void btnCustomerID_GDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustomerID_GDActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_btnCustomerID_GDActionPerformed

    private void cboProdTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboProdTypeActionPerformed
    private void cboPLINameActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }
    private void cboProdIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboProdIdActionPerformed

    private void btnFromRelNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFromRelNoActionPerformed
        // TODO add your handling code here:
        ViewType = FROM_REL_NUM;
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        whereMap.put("ACT_NUM", txtAccNo.getText());        
        viewMap.put(CommonConstants.MAP_NAME, "getSelectReleaseNoFromActNum");
        whereMap.put("SELECTED_BRANCH", ProxyParameters.BRANCH_ID);
        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        new ViewAll(this, viewMap).show();
    }//GEN-LAST:event_btnFromRelNoActionPerformed

    private void tdtAsOn_GDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtAsOn_GDFocusLost
        // Add your handling code here:
    }//GEN-LAST:event_tdtAsOn_GDFocusLost

    /**
     * This method is called from within the constructor to initialize the form.
     *
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoStatus = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoSecurityDetails = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoAccType = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoAccLimit = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoNatureInterest = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoRiskWeight = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoMultiDisburseAllow = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoSubsidy = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoInterest = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoSecurityType = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoDoAddSIs = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoPostDatedCheque = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoIsSubmitted_DocumentDetails = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoStatus_Repayment = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoExecuted_DOC = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoMandatory_DOC = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoGuarnConstution = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoSHG = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoRenewalGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoEnhanceGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoSalaryRecovery = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoRebateInterestGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoSubsidyAddMinusGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoDirectRepaymentGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoCourtOrderGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrTermLoan = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace70 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace71 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace72 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace73 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace74 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblspace3 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace75 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        panTermLoan = new com.see.truetransact.uicomponent.CPanel();
        panRelSelectDet = new com.see.truetransact.uicomponent.CPanel();
        lblFromReleaseNo = new com.see.truetransact.uicomponent.CLabel();
        txtToReleaseNo = new com.see.truetransact.uicomponent.CTextField();
        lblKCCProdId = new com.see.truetransact.uicomponent.CLabel();
        cboKCCProdId = new com.see.truetransact.uicomponent.CComboBox();
        lblToReleaseNo = new com.see.truetransact.uicomponent.CLabel();
        txtFromReleaseNo = new com.see.truetransact.uicomponent.CTextField();
        lblAccNo = new com.see.truetransact.uicomponent.CLabel();
        txtAccNo = new com.see.truetransact.uicomponent.CTextField();
        btnFromRelNo = new com.see.truetransact.uicomponent.CButton();
        lblAccNameValue = new com.see.truetransact.uicomponent.CLabel();
        btnSearch = new com.see.truetransact.uicomponent.CButton();
        btnToRelNo = new com.see.truetransact.uicomponent.CButton();
        btnkccAccNo = new com.see.truetransact.uicomponent.CButton();
        panReleaseDet = new com.see.truetransact.uicomponent.CPanel();
        srpTable_ReleaseDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblReleaseDetails = new com.see.truetransact.uicomponent.CTable();
        mbrTermLoan = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptProcess = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitAuthorize = new javax.swing.JMenuItem();
        mitException = new javax.swing.JMenuItem();
        mitReject = new javax.swing.JMenuItem();
        sptException = new javax.swing.JSeparator();
        mitPrint = new javax.swing.JMenuItem();
        sptPrint = new javax.swing.JSeparator();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Release");
        setMinimumSize(new java.awt.Dimension(650, 590));
        setPreferredSize(new java.awt.Dimension(650, 590));

        tbrTermLoan.setMinimumSize(new java.awt.Dimension(345, 29));

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setEnabled(false);
        btnView.setMinimumSize(new java.awt.Dimension(29, 27));
        btnView.setPreferredSize(new java.awt.Dimension(29, 27));
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnView);

        lblSpace4.setText("     ");
        tbrTermLoan.add(lblSpace4);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnNew);

        lblSpace70.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace70.setText("     ");
        lblSpace70.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace70.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace70.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTermLoan.add(lblSpace70);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnEdit);

        lblSpace71.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace71.setText("     ");
        lblSpace71.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace71.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace71.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTermLoan.add(lblSpace71);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnDelete);

        lblSpace2.setText("     ");
        tbrTermLoan.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnSave);

        lblSpace72.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace72.setText("     ");
        lblSpace72.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace72.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace72.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTermLoan.add(lblSpace72);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnCancel);

        lblSpace3.setText("     ");
        tbrTermLoan.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setEnabled(false);
        btnAuthorize.setMaximumSize(new java.awt.Dimension(29, 27));
        btnAuthorize.setMinimumSize(new java.awt.Dimension(29, 27));
        btnAuthorize.setPreferredSize(new java.awt.Dimension(29, 27));
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnAuthorize);

        lblSpace73.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace73.setText("     ");
        lblSpace73.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTermLoan.add(lblSpace73);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setEnabled(false);
        btnException.setMaximumSize(new java.awt.Dimension(29, 27));
        btnException.setMinimumSize(new java.awt.Dimension(29, 27));
        btnException.setPreferredSize(new java.awt.Dimension(29, 27));
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnException);

        lblSpace74.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace74.setText("     ");
        lblSpace74.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTermLoan.add(lblSpace74);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setEnabled(false);
        btnReject.setMaximumSize(new java.awt.Dimension(29, 27));
        btnReject.setMinimumSize(new java.awt.Dimension(29, 27));
        btnReject.setPreferredSize(new java.awt.Dimension(29, 27));
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnReject);

        lblspace3.setMaximumSize(new java.awt.Dimension(15, 15));
        lblspace3.setMinimumSize(new java.awt.Dimension(15, 15));
        lblspace3.setPreferredSize(new java.awt.Dimension(15, 15));
        tbrTermLoan.add(lblspace3);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnPrint);

        lblSpace75.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace75.setText("     ");
        lblSpace75.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTermLoan.add(lblSpace75);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnClose);

        getContentPane().add(tbrTermLoan, java.awt.BorderLayout.NORTH);

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

        panTermLoan.setMinimumSize(new java.awt.Dimension(500, 650));
        panTermLoan.setPreferredSize(new java.awt.Dimension(500, 650));
        panTermLoan.setLayout(new java.awt.GridBagLayout());

        panRelSelectDet.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panRelSelectDet.setMinimumSize(new java.awt.Dimension(600, 150));
        panRelSelectDet.setPreferredSize(new java.awt.Dimension(600, 150));
        panRelSelectDet.setLayout(new java.awt.GridBagLayout());

        lblFromReleaseNo.setText("From Release No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRelSelectDet.add(lblFromReleaseNo, gridBagConstraints);

        txtToReleaseNo.setMaximumSize(new java.awt.Dimension(100, 21));
        txtToReleaseNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtToReleaseNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtToReleaseNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRelSelectDet.add(txtToReleaseNo, gridBagConstraints);

        lblKCCProdId.setText("Product ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRelSelectDet.add(lblKCCProdId, gridBagConstraints);

        cboKCCProdId.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboKCCProdId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboKCCProdId.setPopupWidth(250);
        cboKCCProdId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboKCCProdIdActionPerformed(evt);
            }
        });
        cboKCCProdId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboKCCProdIdFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRelSelectDet.add(cboKCCProdId, gridBagConstraints);

        lblToReleaseNo.setText("To Release No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRelSelectDet.add(lblToReleaseNo, gridBagConstraints);

        txtFromReleaseNo.setMaximumSize(new java.awt.Dimension(100, 21));
        txtFromReleaseNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtFromReleaseNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFromReleaseNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRelSelectDet.add(txtFromReleaseNo, gridBagConstraints);

        lblAccNo.setText("Account No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRelSelectDet.add(lblAccNo, gridBagConstraints);

        txtAccNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAccNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAccNoActionPerformed(evt);
            }
        });
        txtAccNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAccNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRelSelectDet.add(txtAccNo, gridBagConstraints);

        btnFromRelNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnFromRelNo.setToolTipText("Select Customer");
        btnFromRelNo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnFromRelNo.setMaximumSize(new java.awt.Dimension(22, 21));
        btnFromRelNo.setMinimumSize(new java.awt.Dimension(22, 21));
        btnFromRelNo.setPreferredSize(new java.awt.Dimension(22, 21));
        btnFromRelNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFromRelNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panRelSelectDet.add(btnFromRelNo, gridBagConstraints);

        lblAccNameValue.setForeground(new java.awt.Color(0, 51, 204));
        lblAccNameValue.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAccNameValue.setText("Account Name");
        lblAccNameValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblAccNameValue.setMaximumSize(new java.awt.Dimension(300, 18));
        lblAccNameValue.setMinimumSize(new java.awt.Dimension(300, 18));
        lblAccNameValue.setPreferredSize(new java.awt.Dimension(300, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRelSelectDet.add(lblAccNameValue, gridBagConstraints);

        btnSearch.setForeground(new java.awt.Color(0, 51, 51));
        btnSearch.setText("Search");
        btnSearch.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(10, 4, 4, 4);
        panRelSelectDet.add(btnSearch, gridBagConstraints);

        btnToRelNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnToRelNo.setToolTipText("Select Customer");
        btnToRelNo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnToRelNo.setMaximumSize(new java.awt.Dimension(22, 21));
        btnToRelNo.setMinimumSize(new java.awt.Dimension(22, 21));
        btnToRelNo.setPreferredSize(new java.awt.Dimension(22, 21));
        btnToRelNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnToRelNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panRelSelectDet.add(btnToRelNo, gridBagConstraints);

        btnkccAccNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnkccAccNo.setToolTipText("Select Customer");
        btnkccAccNo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnkccAccNo.setMaximumSize(new java.awt.Dimension(22, 21));
        btnkccAccNo.setMinimumSize(new java.awt.Dimension(22, 21));
        btnkccAccNo.setPreferredSize(new java.awt.Dimension(22, 21));
        btnkccAccNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnkccAccNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panRelSelectDet.add(btnkccAccNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panTermLoan.add(panRelSelectDet, gridBagConstraints);

        panReleaseDet.setMinimumSize(new java.awt.Dimension(650, 320));
        panReleaseDet.setPreferredSize(new java.awt.Dimension(650, 320));
        panReleaseDet.setLayout(new java.awt.GridBagLayout());

        srpTable_ReleaseDetails.setMinimumSize(new java.awt.Dimension(600, 310));
        srpTable_ReleaseDetails.setPreferredSize(new java.awt.Dimension(600, 310));

        tblReleaseDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Select", "Release No", "Release Date", "Release Amount"
            }
        ));
        tblReleaseDetails.setPreferredScrollableViewportSize(new java.awt.Dimension(450, 300));
        tblReleaseDetails.setSelectionBackground(new java.awt.Color(204, 204, 255));
        tblReleaseDetails.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tblReleaseDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblReleaseDetailsMouseClicked(evt);
            }
        });
        srpTable_ReleaseDetails.setViewportView(tblReleaseDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panReleaseDet.add(srpTable_ReleaseDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panTermLoan.add(panReleaseDet, gridBagConstraints);

        getContentPane().add(panTermLoan, java.awt.BorderLayout.CENTER);

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
        mitEdit.setEnabled(false);
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        mitDelete.setMnemonic('D');
        mitDelete.setText("Delete");
        mitDelete.setEnabled(false);
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
        mitSave.setEnabled(false);
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setMnemonic('C');
        mitCancel.setText("Cancel");
        mitCancel.setEnabled(false);
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess.add(mitCancel);
        mnuProcess.add(sptCancel);

        mitAuthorize.setText("Authorize");
        mitAuthorize.setEnabled(false);
        mitAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitAuthorizeActionPerformed(evt);
            }
        });
        mnuProcess.add(mitAuthorize);

        mitException.setText("Exception");
        mitException.setEnabled(false);
        mitException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitExceptionActionPerformed(evt);
            }
        });
        mnuProcess.add(mitException);

        mitReject.setText("Rejection");
        mitReject.setEnabled(false);
        mitReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitRejectActionPerformed(evt);
            }
        });
        mnuProcess.add(mitReject);
        mnuProcess.add(sptException);

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

        mbrTermLoan.add(mnuProcess);

        setJMenuBar(mbrTermLoan);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        callView("Enquiry");
        btnSave.setEnabled(false);
    }//GEN-LAST:event_btnViewActionPerformed

    private void mitRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitRejectActionPerformed
        // TODO add your handling code here:
        //        btnRejectActionPerformed(evt);
    }//GEN-LAST:event_mitRejectActionPerformed

    private void mitExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitExceptionActionPerformed
        // TODO add your handling code here:
        //        btnExceptionActionPerformed(evt);
    }//GEN-LAST:event_mitExceptionActionPerformed

    private void mitAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitAuthorizeActionPerformed
        // TODO add your handling code here:
        //        btnAuthorizeActionPerformed(evt);
    }//GEN-LAST:event_mitAuthorizeActionPerformed
            private void btnDelete_BorrowerMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDelete_BorrowerMousePressed
                // Add your handling code here:
    }//GEN-LAST:event_btnDelete_BorrowerMousePressed
//GEN-FIRST:event_btnDelete_BorrowerActionPerformed
//GEN-LAST:event_btnDelete_BorrowerActionPerformed
                                                private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
                                                    // Add your handling code here:                                                    
                                                    authEnableDisable();
                                                    observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
                                                    authorizeActionPerformed(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // Add your handling code here:
        authEnableDisable();
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeActionPerformed(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // Add your handling code here:
        setModified(true);
        authEnableDisable();
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeActionPerformed(CommonConstants.STATUS_AUTHORIZED);
    }//GEN-LAST:event_btnAuthorizeActionPerformed

    private void authEnableDisable() {
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }

    private void authorizeActionPerformed(String authorizeStatus) {
        if (AuthType == AUTHORIZE && isFilled) {
            ArrayList arrList = new ArrayList();
            HashMap authorizeMap = new HashMap();
            HashMap singleAuthorizeMap = new HashMap();
            HashMap whereMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("AUTHORIZE_BY", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("REL_CLOSED_DT", curDate);
            singleAuthorizeMap.put("REL_CLOSE_NO", observable.getCloseNumber());
            arrList.add(singleAuthorizeMap);
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorize(authorizeMap, observable.getCloseNumber());
            AuthType = "";
            super.setOpenForEditBy(observable.getStatusBy());
            singleAuthorizeMap = null;
            arrList = null;
            authorizeMap = null;
        } else {
            AuthType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put("USER_ID", TrueTransactMain.USER_ID);
            whereMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            mapParam.put(CommonConstants.MAP_NAME, "getNclReleasedClosedDetailsAuthorize");
            isFilled = false;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
            observable.setStatus();
            lblStatus.setText(observable.getLblStatus());
            btnAuthorize.setEnabled(true);
        }
    }

    public void authorize(HashMap map, String id) {
        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
            finalList = observable.getFinalList();
            observable.set_authorizeMap(map);
            observable.doAction(finalList);
            if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                super.setOpenForEditBy(observable.getStatusBy());
                super.removeEditLock(id);
            }
            btnCancelActionPerformed(null);
            observable.setStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
//        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // Add your handling code here:
        //        HashMap reportParamMap = new HashMap();
        //        com.see.truetransact.clientutil.ttrintegration.LinkReport.getReports(getScreenID(), reportParamMap);
    }//GEN-LAST:event_btnPrintActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        btncancelActionPerformed();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btncancelActionPerformed() {
        ClientUtil.clearAll(this);
        observable.resetForm();
        setModified(false);
        lblStatus.setText("       ");
        ClientUtil.enableDisable(panRelSelectDet, false);
        tblReleaseDetails.setEnabled(false);
        setEnableDisable(false);                 
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        btnSave.setEnabled(false);                
        btnView.setEnabled(true);                
        btnNew.setEnabled(true);
        btnCancel.setEnabled(false);
        btnEdit.setEnabled(true);                
        lblAccNameValue.setText("");
    }
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        //        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed

    private void mitPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitPrintActionPerformed
        // Add your handling code here:
        //        btnPrintActionPerformed(evt);
    }//GEN-LAST:event_mitPrintActionPerformed

    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // Add your handling code here:
        //        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed

    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // Add your handling code here:
        //        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed

    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // Add your handling code here:
        //        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed

    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // Add your handling code here:
        //        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed

    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // Add your handling code here:
        //        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        //        btndeleteActionPerformed();
    }//GEN-LAST:event_btnDeleteActionPerformed
            private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
                // Add your handling code here:
                observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
                callView("Edit");
                lblStatus.setText("Edit");
    }//GEN-LAST:event_btnEditActionPerformed
                        private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
                            // Add your handling code here:
                            int yesNo = 0;
                            String[] options = {"Yes", "No"};
                            yesNo = COptionPane.showOptionDialog(null, "All the Selected Releases will be closed. Do you want to continue ?", CommonConstants.WARNINGTITLE,
                                    COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                                    null, options, options[0]);
                            System.out.println("#$#$$ yesNo : " + yesNo);
                            if (yesNo == 0) {
                                finalList = observable.getFinalList();
                                System.out.println("#$@$#@$@$@ FINAL : " + finalList);
                                btnSave.setEnabled(false);
                                HashMap releaseMap = new HashMap();
                                if (finalList != null && finalList.size() > 0) {
                                    for (int i = 0; i < finalList.size(); i++) {
                                        String releaseNo = "";
                                        releaseMap = (HashMap) finalList.get(i);
                                        releaseNo = CommonUtil.convertObjToStr(releaseMap.get("RELEASE_NO"));
                                        for (int j = 0; j < tblReleaseDetails.getRowCount(); j++) {
                                            if (CommonUtil.convertObjToStr(tblReleaseDetails.getValueAt(j, 1)).equals(releaseNo) && !((Boolean) tblReleaseDetails.getValueAt(j, 0)).booleanValue()) {
                                                finalList.remove(i--);
                                            }
                                        }
                                    }
                                    System.out.println("Final List" + finalList);
                                    if (finalList != null && finalList.size() > 0) {
                                        observable.doAction(finalList);
                                        ClientUtil.clearAll(this);
                                        setModified(false);
                                        observable.resetReleaseTableValues();
                                        lblAccNameValue.setText("");
                                    } else {
                                        ClientUtil.showMessageWindow(" NO Rows Selected !!! ");
                                        btnSave.setEnabled(true);
                                    }
                                }
                            }

    }//GEN-LAST:event_btnSaveActionPerformed
                                        private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
                                            // Add your handling code here:
                                            btnnewActionPerformed();
    }//GEN-LAST:event_btnNewActionPerformed

    private void callView(String currAction) {
        view = currAction;
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        if (currAction.equalsIgnoreCase("Edit")) {
            HashMap map = new HashMap();
            map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getNclClosedReleaseDetailsEdit");
        } else if (currAction.equalsIgnoreCase("Enquiry")) {
            HashMap map = new HashMap();
            map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getNclClosedReleaseDetailsView");
        }
        new ViewAll(this, viewMap).show();
    }

    private void txtAccNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAccNoActionPerformed
        // TODO add your handling code here:        
    }//GEN-LAST:event_txtAccNoActionPerformed

    private void txtAccNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccNoFocusLost
        // TODO add your handling code here:    
        if (txtAccNo.getText().length() > 0) {
            observable.resetAccNum();
            resetAccNum();
            if (cboKCCProdId.getSelectedIndex() > 0) {
                if (txtAccNo.getText().length() > 0) {
                    HashMap whereMap = new HashMap();
                    whereMap.put("ACT_NUM", txtAccNo.getText());
                    List mapDataList = ClientUtil.executeQuery("getActNumFromAllProducts", whereMap);
                    if (mapDataList != null && mapDataList.size() > 0) {
                        whereMap = (HashMap) mapDataList.get(0);
                        txtAccNo.setText(CommonUtil.convertObjToStr(whereMap.get("ACT_NUM")));
                    } else {
                        ClientUtil.displayAlert("Invalid Account No.");
                        lblAccNameValue.setText("");
                        txtAccNo.setText("");
                        return;
                    }
                }
            } else {
                ClientUtil.displayAlert("Select Product Id");
                txtAccNo.setText("");
                return;
            }
        }
    }//GEN-LAST:event_txtAccNoFocusLost

    private void btnAccNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustID2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCustID2ActionPerformed

    private void resetAccNum() {
        txtToReleaseNo.setText("");
        txtFromReleaseNo.setText("");
    }

    public void fillData(Object obj) {
        isFilled = true;
        HashMap hash = (HashMap) obj;
        System.out.println("#$#$ Hash : " + hash);
        if (ViewType == KCC_ACC_NUM) {
            String accountNum = null;
            String msg = "";
            accountNum = CommonUtil.convertObjToStr(hash.get("ACCOUNTNO"));
            txtAccNo.setText(accountNum);
            displayAccName();
        }
        if (ViewType == FROM_REL_NUM) {
            txtFromReleaseNo.setText(CommonUtil.convertObjToStr(hash.get("RELEASE_NO")));
        }
        if (ViewType == TO_REL_NUM) {
            txtToReleaseNo.setText(CommonUtil.convertObjToStr(hash.get("RELEASE_NO")));
        }
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT
                || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
            this.setButtonEnableDisable();
            System.out.println("%%%%%hash :" + hash);
            observable.setCloseNumber(CommonUtil.convertObjToStr(hash.get("REL_CLOSE_NO")));
            observable.getData(hash);
            tblReleaseDetails.setModel(observable.getTblReleaseDet());
            setEnableDisable(false);
            DisplayOtherDetails();
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
                || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION
                || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
            this.setButtonEnableDisable();
            btnReject.setEnabled(true);
            btnException.setEnabled(true);
            observable.setCloseNumber(CommonUtil.convertObjToStr(hash.get("REL_CLOSE_NO")));
            observable.getData(hash);
            tblReleaseDetails.setModel(observable.getTblReleaseDet());
            DisplayOtherDetails();
            setEnableDisable(false);
            tblReleaseDetails.setEnabled(false);            
        }
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
            tblReleaseDetails.setEnabled(false);                       
        }
         setModified(true);
    }

    private void setEnableDisable(Boolean flag) {
        ClientUtil.enableDisable(panRelSelectDet, flag);
        tblReleaseDetails.setEnabled(true);
        btnkccAccNo.setEnabled(flag);
        btnFromRelNo.setEnabled(flag);
        btnToRelNo.setEnabled(flag);
        btnSearch.setEnabled(flag);
    }

    private void DisplayOtherDetails() {
        txtFromReleaseNo.setText(observable.getTxtFromReleaseNo());
        txtToReleaseNo.setText(observable.getTxtToReleaseNo());
        HashMap whereMap = new HashMap();
        whereMap.put("RELEASE_NUMBER", txtFromReleaseNo.getText());
        observable.displayAccNumberProdId(whereMap);
        txtAccNo.setText(observable.getTxtAccNo());
        cboKCCProdId.setSelectedItem(observable.getCboKCCProdId());
        displayAccName();
    }

    private void displayAccName() {
        if (txtAccNo.getText().length() > 0) {
            HashMap whereMap = new HashMap();
            whereMap.put("ACT_NUM", txtAccNo.getText());
            List accountLst = (List) ClientUtil.executeQuery("getAllCustomerName", whereMap);
            if (accountLst != null && accountLst.size() > 0) {
                whereMap = (HashMap) accountLst.get(0);
                lblAccNameValue.setText(CommonUtil.convertObjToStr(whereMap.get("CUST_NAME")));
            }
        }
    }

    private void btnToRelNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnToRelNoActionPerformed
        // TODO add your handling code here:
        ViewType = TO_REL_NUM;
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        whereMap.put("ACT_NUM", txtAccNo.getText());         
        viewMap.put(CommonConstants.MAP_NAME, "getSelectReleaseNoFromActNum");
        whereMap.put("SELECTED_BRANCH", ProxyParameters.BRANCH_ID);
        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        new ViewAll(this, viewMap).show();
    }//GEN-LAST:event_btnToRelNoActionPerformed

    private void btnkccAccNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnkccAccNoActionPerformed
        // TODO add your handling code here:
        if (cboKCCProdId.getSelectedIndex() > 0) {
            HashMap viewMap = new HashMap();
            HashMap whereMap = new HashMap();
            ViewType = KCC_ACC_NUM;
            viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountListAD");
            whereMap.put(PROD_ID, observable.getCbmKCCProdId().getKeyForSelected());
            whereMap.put("SELECTED_BRANCH", ProxyParameters.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            new ViewAll(this, "KCC A/c", viewMap).show();
        } else {
            ClientUtil.showAlertWindow("Product Id should not be empty!!!");
            return;
        }
    }//GEN-LAST:event_btnkccAccNoActionPerformed

    private void cboKCCProdIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboKCCProdIdActionPerformed
        // TODO add your handling code here:
        System.out.println("#$#$ prodId : " + ((ComboBoxModel) cboKCCProdId.getModel()).getKeyForSelected());
        observable.setCboKCCProdId(CommonUtil.convertObjToStr(((ComboBoxModel) cboKCCProdId.getModel()).getKeyForSelected()));
    }//GEN-LAST:event_cboKCCProdIdActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        // TODO add your handling code here:  
        if (cboKCCProdId.getSelectedIndex() <= 0) {
            ClientUtil.showAlertWindow("Select Product Id!!!");
            return;
        }
        if (txtAccNo.getText().length() <= 0) {
            ClientUtil.showAlertWindow("Select Account Number!!!");
            return;
        }
        HashMap whereMap = new HashMap();
        whereMap.put("ACT_NUM", txtAccNo.getText());
        if (txtFromReleaseNo.getText().length() > 0) {
            whereMap.put("FROM_RELEASE_NO", txtFromReleaseNo.getText());
        }
        if (txtToReleaseNo.getText().length() > 0) {
            whereMap.put("TO_RELEASE_NO", txtToReleaseNo.getText());
        }
        observable.insertTableData(whereMap);
        tblReleaseDetails.setModel(observable.getTblReleaseDet());
        setSizeTableData();
        if (tblReleaseDetails.getRowCount() == 0) {
            ClientUtil.showMessageWindow(" No Data !!! ");
        }
    }//GEN-LAST:event_btnSearchActionPerformed

    private void setSizeTableData() {
        tblReleaseDetails.getColumnModel().getColumn(0).setPreferredWidth(15);
        tblReleaseDetails.getColumnModel().getColumn(1).setPreferredWidth(90);
        tblReleaseDetails.getColumnModel().getColumn(2).setPreferredWidth(90);
        tblReleaseDetails.getColumnModel().getColumn(3).setPreferredWidth(90);
    }

    private void tblReleaseDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblReleaseDetailsMouseClicked
        // TODO add your handling code here:
        if(tblReleaseDetails.isEnabled()){
        String st = CommonUtil.convertObjToStr(tblReleaseDetails.getValueAt(tblReleaseDetails.getSelectedRow(), 0));
        if (st.equals("true")) {
            tblReleaseDetails.setValueAt(new Boolean(false), tblReleaseDetails.getSelectedRow(), 0);
        } else {
            tblReleaseDetails.setValueAt(new Boolean(true), tblReleaseDetails.getSelectedRow(), 0);
        }
        }
    }//GEN-LAST:event_tblReleaseDetailsMouseClicked

    private void cboKCCProdIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboKCCProdIdFocusLost
        // TODO add your handling code here:
        observable.resetProdid();
        resetProdid();
    }//GEN-LAST:event_cboKCCProdIdFocusLost

    private void txtFromReleaseNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFromReleaseNoFocusLost
        // TODO add your handling code here:  
        if (txtFromReleaseNo.getText().length() > 0) {
            observable.resetFromRelNum();
            txtToReleaseNo.setText("");
            HashMap whereMap = new HashMap();
            whereMap.put("ACT_NUM", txtAccNo.getText());
            whereMap.put("RELEASE_NO", txtFromReleaseNo.getText());
            List releaseNoList = ClientUtil.executeQuery("getSelectReleaseNoFromActNum", whereMap);
            if (releaseNoList != null && releaseNoList.size() > 0) {
                whereMap=(HashMap) releaseNoList.get(0);
                ViewType = FROM_REL_NUM;
                fillData(whereMap);
                whereMap=null;
                releaseNoList=null;
            } else {
                ClientUtil.showMessageWindow("Invalid Release Number !!! ");
                txtFromReleaseNo.setText("");
                return;
            }
        }
    }//GEN-LAST:event_txtFromReleaseNoFocusLost

    private void resetProdid() {
        txtAccNo.setText("");
        txtToReleaseNo.setText("");
        txtFromReleaseNo.setText("");
        lblAccNameValue.setText("");
    }
    private void txtToReleaseNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtToReleaseNoFocusLost
        // TODO add your handling code here:
        if (txtToReleaseNo.getText().length() > 0) {
            observable.resetToRelNum();
            HashMap whereMap = new HashMap();
            whereMap.put("ACT_NUM", txtAccNo.getText());
            whereMap.put("RELEASE_NO", txtToReleaseNo.getText());
            List releaseNoList = ClientUtil.executeQuery("getSelectReleaseNoFromActNum", whereMap);
            if (releaseNoList != null && releaseNoList.size() > 0) {
                whereMap=(HashMap) releaseNoList.get(0);
                ViewType = TO_REL_NUM;
                fillData(whereMap);
                whereMap=null;
                releaseNoList=null;
            }else {
                ClientUtil.showMessageWindow("Invalid Release Number !!! ");
                txtToReleaseNo.setText("");
                return;
            }
        }
    }//GEN-LAST:event_txtToReleaseNoFocusLost

    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }

    public void update(Observable observed, Object arg) {
        ((ComboBoxModel) cboKCCProdId.getModel()).setKeyForSelected(observable.getCboKCCProdId());
        txtAccNo.setText(observable.getTxtAccNo());
        txtFromReleaseNo.setText(observable.getTxtFromReleaseNo());
        txtToReleaseNo.setText(observable.getTxtToReleaseNo());
    }

    private void updateOBFields() {
        observable.setCboKCCProdId(CommonUtil.convertObjToStr(((ComboBoxModel) cboKCCProdId.getModel()).getKeyForSelected()));
        observable.setTxtAccNo(txtAccNo.getText());
        observable.setTxtFromReleaseNo(txtFromReleaseNo.getText());
        observable.setTxtToReleaseNo(txtToReleaseNo.getText());
    }

    private void btnnewActionPerformed() {
        btnNewPressed = true;
        observable.resetForm();
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        ClientUtil.enableDisable(this, true);// Enables the panel...
        setEnableDisable(true);
        ClientUtil.enableDisable(panRelSelectDet, true);
        tblReleaseDetails.setEnabled(true);
        setButtonEnableDisable();
        observable.setStatus();
        setModified(true);
        lblStatus.setText(observable.getLblStatus());
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnFromRelNo;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnSearch;
    private com.see.truetransact.uicomponent.CButton btnToRelNo;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CButton btnkccAccNo;
    private com.see.truetransact.uicomponent.CComboBox cboKCCProdId;
    private com.see.truetransact.uicomponent.CLabel lblAccNameValue;
    private com.see.truetransact.uicomponent.CLabel lblAccNo;
    private com.see.truetransact.uicomponent.CLabel lblFromReleaseNo;
    private com.see.truetransact.uicomponent.CLabel lblKCCProdId;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace70;
    private com.see.truetransact.uicomponent.CLabel lblSpace71;
    private com.see.truetransact.uicomponent.CLabel lblSpace72;
    private com.see.truetransact.uicomponent.CLabel lblSpace73;
    private com.see.truetransact.uicomponent.CLabel lblSpace74;
    private com.see.truetransact.uicomponent.CLabel lblSpace75;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblToReleaseNo;
    private com.see.truetransact.uicomponent.CLabel lblspace3;
    private com.see.truetransact.uicomponent.CMenuBar mbrTermLoan;
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
    private com.see.truetransact.uicomponent.CPanel panRelSelectDet;
    private com.see.truetransact.uicomponent.CPanel panReleaseDet;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTermLoan;
    private com.see.truetransact.uicomponent.CButtonGroup rdoAccLimit;
    private com.see.truetransact.uicomponent.CButtonGroup rdoAccType;
    private com.see.truetransact.uicomponent.CButtonGroup rdoCourtOrderGroup;
    private com.see.truetransact.uicomponent.CButtonGroup rdoDirectRepaymentGroup;
    private com.see.truetransact.uicomponent.CButtonGroup rdoDoAddSIs;
    private com.see.truetransact.uicomponent.CButtonGroup rdoEnhanceGroup;
    private com.see.truetransact.uicomponent.CButtonGroup rdoExecuted_DOC;
    private com.see.truetransact.uicomponent.CButtonGroup rdoGuarnConstution;
    private com.see.truetransact.uicomponent.CButtonGroup rdoInterest;
    private com.see.truetransact.uicomponent.CButtonGroup rdoIsSubmitted_DocumentDetails;
    private com.see.truetransact.uicomponent.CButtonGroup rdoMandatory_DOC;
    private com.see.truetransact.uicomponent.CButtonGroup rdoMultiDisburseAllow;
    private com.see.truetransact.uicomponent.CButtonGroup rdoNatureInterest;
    private com.see.truetransact.uicomponent.CButtonGroup rdoPostDatedCheque;
    private com.see.truetransact.uicomponent.CButtonGroup rdoRebateInterestGroup;
    private com.see.truetransact.uicomponent.CButtonGroup rdoRenewalGroup;
    private com.see.truetransact.uicomponent.CButtonGroup rdoRiskWeight;
    private com.see.truetransact.uicomponent.CButtonGroup rdoSHG;
    private com.see.truetransact.uicomponent.CButtonGroup rdoSalaryRecovery;
    private com.see.truetransact.uicomponent.CButtonGroup rdoSecurityDetails;
    private com.see.truetransact.uicomponent.CButtonGroup rdoSecurityType;
    private com.see.truetransact.uicomponent.CButtonGroup rdoStatus;
    private com.see.truetransact.uicomponent.CButtonGroup rdoStatus_Repayment;
    private com.see.truetransact.uicomponent.CButtonGroup rdoSubsidy;
    private com.see.truetransact.uicomponent.CButtonGroup rdoSubsidyAddMinusGroup;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptException;
    private javax.swing.JSeparator sptPrint;
    private javax.swing.JSeparator sptProcess;
    private com.see.truetransact.uicomponent.CScrollPane srpTable_ReleaseDetails;
    private com.see.truetransact.uicomponent.CTable tblReleaseDetails;
    private javax.swing.JToolBar tbrTermLoan;
    private com.see.truetransact.uicomponent.CTextField txtAccNo;
    private com.see.truetransact.uicomponent.CTextField txtFromReleaseNo;
    private com.see.truetransact.uicomponent.CTextField txtToReleaseNo;
    // End of variables declaration//GEN-END:variables
}
