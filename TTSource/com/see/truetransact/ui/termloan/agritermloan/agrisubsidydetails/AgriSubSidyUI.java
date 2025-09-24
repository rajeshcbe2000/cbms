/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AgriSubSidyUI.java
 *
 * Created on April 28, 2009, 5:02 PM
 */

package com.see.truetransact.ui.termloan.agritermloan.agrisubsidydetails;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientUtil;
//import com.see.tools.workflow.clientutils.ComboBoxModel;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.TrueTransactMain;
import org.apache.log4j.Logger;
import java.util.HashMap;


/**
 *
 * @author  Administrator
 */
public class AgriSubSidyUI extends com.see.truetransact.uicomponent.CPanel  implements java.util.Observer,UIMandatoryField{
    java.util.ResourceBundle resourceBundle= java.util.ResourceBundle.getBundle("com.see.truetransact.ui.termloan.agritermloan.agrisubsidydetails.AgriSubSidyRB",ProxyParameters.LANGUAGE);
    private AgriSubSidyOB agriSubSidyOB;
    private final static Logger log = Logger.getLogger(AgriSubSidyUI.class);
    private int selectedSubLimitRow=-1;
    private boolean tblSubLimitSelected=false;
    private int selectedInspectionRow=-1;
    private boolean tblInspectionSelected=false;
    private String viewType = "";
    
    /** Creates new form BeanForm */
    public AgriSubSidyUI() {
        initComponents();
        setFieldNames();
        setObservable();
        initComponentData();
        internationalize();
        setMaxLength();
        setEnableDisable(false);
        lblSubsidyAcctNo.setVisible(false);
        lblSubsidyShowAcctno.setVisible(false);
        //         new MandatoryCheck().putMandatoryMarks(getClass().getName(), panValutionDetails);
    }
    private void setObservable(){
        agriSubSidyOB= new AgriSubSidyOB();
        agriSubSidyOB.addObserver(this);
    }
    private void initComponentData(){
        cboTypeOfSubSidy.setModel(agriSubSidyOB.getCbmTypeOfSubSidy());
        cboRecivedFrom.setModel(agriSubSidyOB.getCbmRecivedFrom());
        cboDepositProdId.setModel(agriSubSidyOB.getCbmDepositProdId());
        tblInspectionDetails.setModel(agriSubSidyOB.getTblSubsidyDetails());
        tblSubLimit.setModel(agriSubSidyOB.getTblValution());
    }
    private void  setFieldNames(){
        lblPropertyType.setName("lblPropertyType");
        txtPropertyType.setName("txtPropertyType");
        lblValutionDt.setName("lblValutionDt");
        tdtValutionDt.setName("tdtValutionDt");
        lblValutionAmt.setName("lblValutionAmt");
        txtValutionAmt.setName("txtValutionAmt");
        lblValuatedBy.setName("lblValuatedBy");
        txtValuatedBy.setName("txtValuatedBy");
        lblName.setName("lblName");
        lblDisPlayName.setName("lblDisPlayName");
        lblDesignation.setName("lblDesignation");
        lblDisplaylDesig.setName("lblDisplaylDesig");
        lblValutionRemarks.setName("lblValutionRemarks");
        txtValutionRemarks.setName("txtValutionRemarks");
        lblTypeOfSubSidy.setName("lblTypeOfSubSidy");
        cboTypeOfSubSidy.setName("cboTypeOfSubSidy");
        lblDepositProdId.setName("lblDepositProdId");
        cboDepositProdId.setName("cboDepositProdId");
        lblDepositNo.setName("lblDepositNo");
        txtDepositNo.setName("txtDepositNo");
        lblSubSidyDate.setName("lblSubSidyDate");
        tdlSubSidyDate.setName("tdlSubSidyDate");
        lblRecivedFrom.setName("lblRecivedFrom");
        cboRecivedFrom.setName("cboRecivedFrom");
        lblSubSidyAmt.setName("lblSubSidyAmt");
        txtSubSidyAmt.setName("txtSubSidyAmt");
        lblAmtAdjusted.setName("lblAmtAdjusted");
        txtAmtAdjusted.setName("txtAmtAdjusted");
        lblAmtRefunded.setName("lblAmtRefunded");
        txtAmtRefunded.setName("txtAmtRefunded");
        lblRefundDate.setName("lblRefundDate");
        tdtRefundDate.setName("tdtRefundDate");
        lblOutStandingAmt.setName("lblOutStandingAmt");
        txtOutStandingAmt.setName("txtOutStandingAmt");
    }
    private void setEnableDisable(boolean val){
        ClientUtil.enableDisable(panSanctionDetails_Upper,val);
        ClientUtil.enableDisable(panValutionDetails,val);
         btnInspectBy.setEnabled(val);
         btnInspectBy2.setEnabled(val);
        //        ClientUtil.enableDisable(panTable2_SubLimit,val);
        ClientUtil.enableDisable(panButton2_SD2,val);
        ClientUtil.enableDisable(panTableFields_Subsidy,val);
        //        ClientUtil.enableDisable( panTable_SubSidyDetails,val);
        ClientUtil.enableDisable( panSubsidyDetails_Table,val);
        enableDisableValutionButton(val);
        enableDisableSubsidyButton(val);
        
    }
    private void setMaxLength(){
        txtSubSidyAmt.setValidation(new CurrencyValidation());
        txtValutionAmt.setValidation(new CurrencyValidation());
        txtOutStandingAmt.setValidation(new CurrencyValidation());
        txtAmtAdjusted.setValidation(new  CurrencyValidation());
        txtAmtRefunded.setValidation(new CurrencyValidation());
    }
    public void enableDisableValutionButton(boolean val){
        btnValutiontNew.setEnabled(val);
        btnValutionSave.setEnabled(val);
        btnValutionDelete.setEnabled(val);
    }
    public void enableDisableValutionNewButton(boolean val){
        btnValutiontNew.setEnabled(!val);
        btnValutionSave.setEnabled(val);
        btnValutionDelete.setEnabled(val);
    }
    
    public void enableDisableSubsidyButton(boolean val){
        btnSubSidyNew.setEnabled(val);
        btnSubsidySave.setEnabled(val);
        btnSubsidyDelete.setEnabled(val);
    }
    public  void enableDisableSubsidyNewButton(boolean val){
        btnSubSidyNew.setEnabled(!val);
        btnSubsidySave.setEnabled(val);
        btnSubsidyDelete.setEnabled(val);
    }
    
    
    private void internationalize(){
        lblPropertyType.setText(resourceBundle.getString("lblPropertyType"));
        lblValutionDt.setText(resourceBundle.getString("lblValutionDt"));
        lblValutionAmt.setText(resourceBundle.getString("lblValutionAmt"));
        lblValuatedBy.setText(resourceBundle.getString("lblValuatedBy"));
        lblName.setText(resourceBundle.getString("lblName"));
        lblDesignation.setText(resourceBundle.getString("lblDesignation"));
        lblValutionRemarks.setText(resourceBundle.getString("lblValutionRemarks"));
        lblTypeOfSubSidy.setText(resourceBundle.getString("lblTypeOfSubSidy"));
        lblDepositProdId.setText(resourceBundle.getString("lblDepositProdId"));
        lblDepositNo.setText(resourceBundle.getString("lblDepositNo"));
        lblSubSidyDate.setText(resourceBundle.getString("lblSubSidyDate"));
        lblRecivedFrom.setText(resourceBundle.getString("lblRecivedFrom"));
        lblSubSidyAmt.setText(resourceBundle.getString("lblSubSidyAmt"));
        lblAmtAdjusted.setText(resourceBundle.getString("lblAmtAdjusted"));
        lblAmtRefunded.setText(resourceBundle.getString("lblAmtRefunded"));
        lblRefundDate.setText(resourceBundle.getString("lblRefundDate"));
        lblOutStandingAmt.setText(resourceBundle.getString("lblOutStandingAmt"));
        
        
        btnSubSidyNew.setText(resourceBundle.getString("btnSubSidyNew"));
        btnSubsidySave.setText(resourceBundle.getString("btnSubsidySave"));
        btnSubsidyDelete.setText(resourceBundle.getString("btnSubsidyDelete"));
        btnValutiontNew.setText(resourceBundle.getString("btnValutiontNew"));
        btnValutionSave.setText(resourceBundle.getString("btnValutionSave"));
        btnValutionDelete.setText(resourceBundle.getString("btnValutionDelete"));
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        panSublimitDetails = new com.see.truetransact.uicomponent.CPanel();
        panSanctionDetails_Upper = new com.see.truetransact.uicomponent.CPanel();
        panValutionDetails = new com.see.truetransact.uicomponent.CPanel();
        lblValutionAmt = new com.see.truetransact.uicomponent.CLabel();
        tdtValutionDt = new com.see.truetransact.uicomponent.CDateField();
        txtPropertyType = new com.see.truetransact.uicomponent.CTextField();
        lblPropertyType = new com.see.truetransact.uicomponent.CLabel();
        lblValutionDt = new com.see.truetransact.uicomponent.CLabel();
        lblDisplaylDesig = new com.see.truetransact.uicomponent.CLabel();
        btnInspectBy = new com.see.truetransact.uicomponent.CButton();
        txtValutionAmt = new com.see.truetransact.uicomponent.CTextField();
        lblDisPlayName = new com.see.truetransact.uicomponent.CLabel();
        lblValutionRemarks = new com.see.truetransact.uicomponent.CLabel();
        lblName = new com.see.truetransact.uicomponent.CLabel();
        txtValutionRemarks = new com.see.truetransact.uicomponent.CTextField();
        lblDesignation = new com.see.truetransact.uicomponent.CLabel();
        lblValuatedBy = new com.see.truetransact.uicomponent.CLabel();
        txtValuatedBy = new com.see.truetransact.uicomponent.CTextField();
        panButton2_SD2 = new com.see.truetransact.uicomponent.CPanel();
        btnValutiontNew = new com.see.truetransact.uicomponent.CButton();
        btnValutionSave = new com.see.truetransact.uicomponent.CButton();
        btnValutionDelete = new com.see.truetransact.uicomponent.CButton();
        panSubsidyDetails_Table = new com.see.truetransact.uicomponent.CPanel();
        panTableFields_Subsidy = new com.see.truetransact.uicomponent.CPanel();
        lblDepositProdId = new com.see.truetransact.uicomponent.CLabel();
        cboRecivedFrom = new com.see.truetransact.uicomponent.CComboBox();
        lblRecivedFrom = new com.see.truetransact.uicomponent.CLabel();
        txtSubSidyAmt = new com.see.truetransact.uicomponent.CTextField();
        lblSubSidyDate = new com.see.truetransact.uicomponent.CLabel();
        tdlSubSidyDate = new com.see.truetransact.uicomponent.CDateField();
        panButton2 = new com.see.truetransact.uicomponent.CPanel();
        btnSubSidyNew = new com.see.truetransact.uicomponent.CButton();
        btnSubsidySave = new com.see.truetransact.uicomponent.CButton();
        btnSubsidyDelete = new com.see.truetransact.uicomponent.CButton();
        lblSubSidyAmt = new com.see.truetransact.uicomponent.CLabel();
        tdtRefundDate = new com.see.truetransact.uicomponent.CDateField();
        cboDepositProdId = new com.see.truetransact.uicomponent.CComboBox();
        txtAmtAdjusted = new com.see.truetransact.uicomponent.CTextField();
        lblAmtAdjusted = new com.see.truetransact.uicomponent.CLabel();
        lblRefundDate = new com.see.truetransact.uicomponent.CLabel();
        txtAmtRefunded = new com.see.truetransact.uicomponent.CTextField();
        txtOutStandingAmt = new com.see.truetransact.uicomponent.CTextField();
        lblAmtRefunded = new com.see.truetransact.uicomponent.CLabel();
        cboTypeOfSubSidy = new com.see.truetransact.uicomponent.CComboBox();
        txtDepositNo = new com.see.truetransact.uicomponent.CTextField();
        lblDepositNo = new com.see.truetransact.uicomponent.CLabel();
        lblTypeOfSubSidy = new com.see.truetransact.uicomponent.CLabel();
        btnInspectBy2 = new com.see.truetransact.uicomponent.CButton();
        lblOutStandingAmt = new com.see.truetransact.uicomponent.CLabel();
        panSubsidyAcctNo = new com.see.truetransact.uicomponent.CPanel();
        lblSubsidyAcctNo = new com.see.truetransact.uicomponent.CLabel();
        lblSubsidyShowAcctno = new com.see.truetransact.uicomponent.CLabel();
        panTable_SubSidyDetails = new com.see.truetransact.uicomponent.CPanel();
        srpTable_InspectionDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblInspectionDetails = new com.see.truetransact.uicomponent.CTable();
        panTableValution = new com.see.truetransact.uicomponent.CPanel();
        srpTable_SubLimit = new com.see.truetransact.uicomponent.CScrollPane();
        tblSubLimit = new com.see.truetransact.uicomponent.CTable();

        setLayout(new java.awt.GridBagLayout());

        setMinimumSize(new java.awt.Dimension(820, 560));
        setPreferredSize(new java.awt.Dimension(820, 560));
        panSublimitDetails.setLayout(new java.awt.GridBagLayout());

        panSublimitDetails.setMinimumSize(new java.awt.Dimension(820, 550));
        panSublimitDetails.setPreferredSize(new java.awt.Dimension(820, 550));
        panSanctionDetails_Upper.setLayout(new java.awt.GridBagLayout());

        panSanctionDetails_Upper.setMaximumSize(new java.awt.Dimension(465, 200));
        panSanctionDetails_Upper.setMinimumSize(new java.awt.Dimension(465, 200));
        panSanctionDetails_Upper.setPreferredSize(new java.awt.Dimension(465, 200));
        panValutionDetails.setLayout(new java.awt.GridBagLayout());

        panValutionDetails.setMinimumSize(new java.awt.Dimension(179, 80));
        panValutionDetails.setPreferredSize(new java.awt.Dimension(179, 80));
        lblValutionAmt.setText("Valution Amt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panValutionDetails.add(lblValutionAmt, gridBagConstraints);

        tdtValutionDt.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtValutionDt.setNextFocusableComponent(txtValutionAmt);
        tdtValutionDt.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtValutionDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtValutionDtFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panValutionDetails.add(tdtValutionDt, gridBagConstraints);

        txtPropertyType.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPropertyType.setNextFocusableComponent(tdtValutionDt);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panValutionDetails.add(txtPropertyType, gridBagConstraints);

        lblPropertyType.setText("PropertyType");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panValutionDetails.add(lblPropertyType, gridBagConstraints);

        lblValutionDt.setText("Valution Dt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panValutionDetails.add(lblValutionDt, gridBagConstraints);

        lblDisplaylDesig.setText("Desgination");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panValutionDetails.add(lblDisplaylDesig, gridBagConstraints);

        btnInspectBy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnInspectBy.setText("cButton1");
        btnInspectBy.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnInspectBy.setMaximumSize(new java.awt.Dimension(35, 25));
        btnInspectBy.setMinimumSize(new java.awt.Dimension(35, 25));
        btnInspectBy.setNextFocusableComponent(txtValutionRemarks);
        btnInspectBy.setPreferredSize(new java.awt.Dimension(35, 25));
        btnInspectBy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInspectByActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panValutionDetails.add(btnInspectBy, gridBagConstraints);

        txtValutionAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtValutionAmt.setNextFocusableComponent(txtValuatedBy);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panValutionDetails.add(txtValutionAmt, gridBagConstraints);

        lblDisPlayName.setText("Person Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panValutionDetails.add(lblDisPlayName, gridBagConstraints);

        lblValutionRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panValutionDetails.add(lblValutionRemarks, gridBagConstraints);

        lblName.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panValutionDetails.add(lblName, gridBagConstraints);

        txtValutionRemarks.setMinimumSize(new java.awt.Dimension(100, 21));
        txtValutionRemarks.setNextFocusableComponent(btnValutionSave);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panValutionDetails.add(txtValutionRemarks, gridBagConstraints);

        lblDesignation.setText("Desgination");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panValutionDetails.add(lblDesignation, gridBagConstraints);

        lblValuatedBy.setText("Valuated By");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 0);
        panValutionDetails.add(lblValuatedBy, gridBagConstraints);

        txtValuatedBy.setMinimumSize(new java.awt.Dimension(100, 21));
        txtValuatedBy.setNextFocusableComponent(txtValutionRemarks);
        txtValuatedBy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtValuatedByActionPerformed(evt);
            }
        });
        txtValuatedBy.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtValuatedByFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panValutionDetails.add(txtValuatedBy, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panSanctionDetails_Upper.add(panValutionDetails, gridBagConstraints);

        panButton2_SD2.setLayout(new java.awt.GridBagLayout());

        panButton2_SD2.setMinimumSize(new java.awt.Dimension(215, 33));
        panButton2_SD2.setPreferredSize(new java.awt.Dimension(215, 33));
        btnValutiontNew.setText("New");
        btnValutiontNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnValutiontNewActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton2_SD2.add(btnValutiontNew, gridBagConstraints);

        btnValutionSave.setText("Save");
        btnValutionSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnValutionSaveActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton2_SD2.add(btnValutionSave, gridBagConstraints);

        btnValutionDelete.setText("Delete");
        btnValutionDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnValutionDeleteActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton2_SD2.add(btnValutionDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSanctionDetails_Upper.add(panButton2_SD2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSublimitDetails.add(panSanctionDetails_Upper, gridBagConstraints);

        panSubsidyDetails_Table.setLayout(new java.awt.GridBagLayout());

        panSubsidyDetails_Table.setBorder(new javax.swing.border.TitledBorder("Subsidy Details"));
        panSubsidyDetails_Table.setMaximumSize(new java.awt.Dimension(820, 285));
        panSubsidyDetails_Table.setMinimumSize(new java.awt.Dimension(820, 285));
        panSubsidyDetails_Table.setPreferredSize(new java.awt.Dimension(820, 285));
        panTableFields_Subsidy.setLayout(new java.awt.GridBagLayout());

        panTableFields_Subsidy.setMaximumSize(new java.awt.Dimension(465, 250));
        panTableFields_Subsidy.setMinimumSize(new java.awt.Dimension(465, 250));
        panTableFields_Subsidy.setPreferredSize(new java.awt.Dimension(465, 250));
        lblDepositProdId.setText("Deposit ProdId");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_Subsidy.add(lblDepositProdId, gridBagConstraints);

        cboRecivedFrom.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboRecivedFrom.setMinimumSize(new java.awt.Dimension(100, 21));
        cboRecivedFrom.setNextFocusableComponent(txtSubSidyAmt);
        cboRecivedFrom.setPopupWidth(150);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_Subsidy.add(cboRecivedFrom, gridBagConstraints);

        lblRecivedFrom.setText("Recived From");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_Subsidy.add(lblRecivedFrom, gridBagConstraints);

        txtSubSidyAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSubSidyAmt.setNextFocusableComponent(txtAmtAdjusted);
        txtSubSidyAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSubSidyAmtFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_Subsidy.add(txtSubSidyAmt, gridBagConstraints);

        lblSubSidyDate.setText("Subsidy Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_Subsidy.add(lblSubSidyDate, gridBagConstraints);

        tdlSubSidyDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdlSubSidyDate.setNextFocusableComponent(cboRecivedFrom);
        tdlSubSidyDate.setPreferredSize(new java.awt.Dimension(100, 21));
        tdlSubSidyDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdlSubSidyDateFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_Subsidy.add(tdlSubSidyDate, gridBagConstraints);

        panButton2.setLayout(new java.awt.GridBagLayout());

        btnSubSidyNew.setText("New");
        btnSubSidyNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubSidyNewActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton2.add(btnSubSidyNew, gridBagConstraints);

        btnSubsidySave.setText("Save");
        btnSubsidySave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubsidySaveActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton2.add(btnSubsidySave, gridBagConstraints);

        btnSubsidyDelete.setText("Delete");
        btnSubsidyDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubsidyDeleteActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton2.add(btnSubsidyDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTableFields_Subsidy.add(panButton2, gridBagConstraints);

        lblSubSidyAmt.setText("Subsidy Amt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_Subsidy.add(lblSubSidyAmt, gridBagConstraints);

        tdtRefundDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtRefundDate.setNextFocusableComponent(txtOutStandingAmt);
        tdtRefundDate.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtRefundDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtRefundDateFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_Subsidy.add(tdtRefundDate, gridBagConstraints);

        cboDepositProdId.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboDepositProdId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboDepositProdId.setNextFocusableComponent(txtDepositNo);
        cboDepositProdId.setPopupWidth(150);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_Subsidy.add(cboDepositProdId, gridBagConstraints);

        txtAmtAdjusted.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAmtAdjusted.setNextFocusableComponent(txtAmtRefunded);
        txtAmtAdjusted.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAmtAdjustedFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_Subsidy.add(txtAmtAdjusted, gridBagConstraints);

        lblAmtAdjusted.setText("Amt Adjusted ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_Subsidy.add(lblAmtAdjusted, gridBagConstraints);

        lblRefundDate.setText("Refund Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_Subsidy.add(lblRefundDate, gridBagConstraints);

        txtAmtRefunded.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAmtRefunded.setNextFocusableComponent(tdtRefundDate);
        txtAmtRefunded.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAmtRefundedFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_Subsidy.add(txtAmtRefunded, gridBagConstraints);

        txtOutStandingAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtOutStandingAmt.setNextFocusableComponent(btnSubsidySave);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_Subsidy.add(txtOutStandingAmt, gridBagConstraints);

        lblAmtRefunded.setText("Amt Refunded ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_Subsidy.add(lblAmtRefunded, gridBagConstraints);

        cboTypeOfSubSidy.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboTypeOfSubSidy.setMinimumSize(new java.awt.Dimension(100, 21));
        cboTypeOfSubSidy.setNextFocusableComponent(cboDepositProdId);
        cboTypeOfSubSidy.setPopupWidth(150);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_Subsidy.add(cboTypeOfSubSidy, gridBagConstraints);

        txtDepositNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_Subsidy.add(txtDepositNo, gridBagConstraints);

        lblDepositNo.setText("Deposit No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_Subsidy.add(lblDepositNo, gridBagConstraints);

        lblTypeOfSubSidy.setText("Type of Subsidy");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_Subsidy.add(lblTypeOfSubSidy, gridBagConstraints);

        btnInspectBy2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnInspectBy2.setText("cButton1");
        btnInspectBy2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnInspectBy2.setMaximumSize(new java.awt.Dimension(35, 25));
        btnInspectBy2.setMinimumSize(new java.awt.Dimension(35, 25));
        btnInspectBy2.setNextFocusableComponent(tdlSubSidyDate);
        btnInspectBy2.setPreferredSize(new java.awt.Dimension(35, 25));
        btnInspectBy2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInspectBy2ActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panTableFields_Subsidy.add(btnInspectBy2, gridBagConstraints);

        lblOutStandingAmt.setText(" OutStanding Amt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_Subsidy.add(lblOutStandingAmt, gridBagConstraints);

        panSubsidyAcctNo.setLayout(new java.awt.GridBagLayout());

        panSubsidyAcctNo.setMaximumSize(new java.awt.Dimension(300, 30));
        panSubsidyAcctNo.setMinimumSize(new java.awt.Dimension(300, 30));
        panSubsidyAcctNo.setPreferredSize(new java.awt.Dimension(300, 30));
        lblSubsidyAcctNo.setText(" OutStanding Amt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panSubsidyAcctNo.add(lblSubsidyAcctNo, gridBagConstraints);

        lblSubsidyShowAcctno.setText(" OutStanding Amt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panSubsidyAcctNo.add(lblSubsidyShowAcctno, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        panTableFields_Subsidy.add(panSubsidyAcctNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSubsidyDetails_Table.add(panTableFields_Subsidy, gridBagConstraints);

        panTable_SubSidyDetails.setLayout(new java.awt.GridBagLayout());

        srpTable_InspectionDetails.setMinimumSize(new java.awt.Dimension(330, 250));
        srpTable_InspectionDetails.setPreferredSize(new java.awt.Dimension(330, 250));
        tblInspectionDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblInspectionDetails.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblInspectionDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblInspectionDetailsMouseClicked(evt);
            }
        });

        srpTable_InspectionDetails.setViewportView(tblInspectionDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panTable_SubSidyDetails.add(srpTable_InspectionDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSubsidyDetails_Table.add(panTable_SubSidyDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSublimitDetails.add(panSubsidyDetails_Table, gridBagConstraints);

        panTableValution.setLayout(new java.awt.GridBagLayout());

        panTableValution.setMaximumSize(new java.awt.Dimension(150, 40));
        panTableValution.setMinimumSize(new java.awt.Dimension(150, 40));
        panTableValution.setPreferredSize(new java.awt.Dimension(150, 40));
        srpTable_SubLimit.setMinimumSize(new java.awt.Dimension(330, 140));
        srpTable_SubLimit.setPreferredSize(new java.awt.Dimension(330, 140));
        tblSubLimit.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblSubLimit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSubLimitMouseClicked(evt);
            }
        });

        srpTable_SubLimit.setViewportView(tblSubLimit);

        panTableValution.add(srpTable_SubLimit, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSublimitDetails.add(panTableValution, gridBagConstraints);

        add(panSublimitDetails, new java.awt.GridBagConstraints());

    }//GEN-END:initComponents

    private void tdtRefundDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtRefundDateFocusLost
        // TODO add your handling code here:
        ClientUtil.validateLTDate(tdtRefundDate);
    }//GEN-LAST:event_tdtRefundDateFocusLost

    private void tdlSubSidyDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdlSubSidyDateFocusLost
        // TODO add your handling code here:
          ClientUtil.validateLTDate(tdlSubSidyDate);
    }//GEN-LAST:event_tdlSubSidyDateFocusLost
    
    private void txtAmtRefundedFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAmtRefundedFocusLost
        // TODO add your handling code here:
       calculateSubsidy();
    }//GEN-LAST:event_txtAmtRefundedFocusLost
    private void calculateSubsidy(){
        String amtRefunded=CommonUtil.convertObjToStr(txtAmtRefunded.getText());
        String amtAdjusted=CommonUtil.convertObjToStr(txtAmtAdjusted.getText());
        String subsidyAmt=CommonUtil.convertObjToStr(txtSubSidyAmt.getText());
        if(subsidyAmt.equals(""))
            subsidyAmt="0";
        if(amtAdjusted.equals(""))
            amtAdjusted="0";
        if(amtRefunded.equals(""))
            amtRefunded="0";
        double subsidy=Double.parseDouble(subsidyAmt);
        double adjust=Double.parseDouble(amtAdjusted);
        double refund=Double.parseDouble(amtRefunded);
        double outStandingSubsidy=subsidy-(adjust+refund);
        if(outStandingSubsidy>=0) {
            agriSubSidyOB.setOutStandingAmt(String.valueOf(outStandingSubsidy));
            txtOutStandingAmt.setText(String.valueOf(outStandingSubsidy));
        }
    }
    private void txtAmtAdjustedFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAmtAdjustedFocusLost
        // TODO add your handling code here:
        calculateSubsidy();
        
    }//GEN-LAST:event_txtAmtAdjustedFocusLost
    
    private void txtSubSidyAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSubSidyAmtFocusLost
        // TODO add your handling code here:
        
        
    }//GEN-LAST:event_txtSubSidyAmtFocusLost
    
    private void txtValuatedByFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtValuatedByFocusLost
        // TODO add your handling code here:
        
    }//GEN-LAST:event_txtValuatedByFocusLost
    
    private void txtValuatedByActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtValuatedByActionPerformed
        // TODO add your handling code here:
        String empId=CommonUtil.convertObjToStr(txtValuatedBy.getText());
        if(empId.length()>0){
            HashMap singleMap=new HashMap();
            singleMap.put("EMP_ID",empId);
            singleMap.put("BRANCH_CODE",TrueTransactMain.selBranch);
            java.util.List lst =(java.util.List)ClientUtil.executeQuery("getEmployeeName", singleMap);
            if(lst!=null && lst.size()>0){
                HashMap resultMap=(HashMap)lst.get(0);
                lblDisPlayName.setText(CommonUtil.convertObjToStr(resultMap.get("EMP NAME")));
                lblDisplaylDesig.setText(CommonUtil.convertObjToStr(resultMap.get("DESIGNATION")));
            }else{
                lblDisPlayName.setText(txtValuatedBy.getText());
                lblDisplaylDesig.setText("");
            }
        }
    }//GEN-LAST:event_txtValuatedByActionPerformed
    
    private void btnInspectBy2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInspectBy2ActionPerformed
        // TODO add your handling code here:
        popUp("DEPOSIT_NO");
    }//GEN-LAST:event_btnInspectBy2ActionPerformed
    private void popUp(String val){
        HashMap viewMap=new HashMap();
        HashMap whereMap=new HashMap();
        viewType=val;
        viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountListTD");
        String Prod_id=((ComboBoxModel)cboDepositProdId.getModel()).getKeyForSelected().toString();
        whereMap.put("PROD_ID",Prod_id);
        whereMap.put("SELECTED_BRANCH",TrueTransactMain.selBranch);
        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        new ViewAll(this ,"",viewMap).show();
    }
    private void btnInspectByActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInspectByActionPerformed
        // TODO add your handling code here:
        viewType="EMP_ID";
        HashMap viewMap =new HashMap();
        HashMap mapwhere=new HashMap();
        viewMap.put(CommonConstants.MAP_NAME,"getEmployeeName");
        mapwhere.put(CommonConstants.BRANCH_ID,TrueTransactMain.BRANCH_ID);
        viewMap.put(CommonConstants.MAP_WHERE,mapwhere);
        new ViewAll(this,"",viewMap).show();
        mapwhere=null;
        mapwhere=null;
        
    }//GEN-LAST:event_btnInspectByActionPerformed
    
    private void tdtValutionDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtValutionDtFocusLost
        // TODO add your handling code here:
        ClientUtil.validateLTDate(tdtValutionDt);
    }//GEN-LAST:event_tdtValutionDtFocusLost
    
    private void tblInspectionDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblInspectionDetailsMouseClicked
        // TODO add your handling code here:
        
        selectedInspectionRow=tblInspectionDetails.getSelectedRow();
        agriSubSidyOB.setSubSidyDetails(selectedInspectionRow);
        if ((agriSubSidyOB.getLblStatus().equals(ClientConstants.ACTION_STATUS[3])) ||
        (agriSubSidyOB.getLblStatus().equals(ClientConstants.ACTION_STATUS[8]))  ||
        (agriSubSidyOB.getLblStatus().equals(ClientConstants.ACTION_STATUS[10]))){
            subSidyEnableDisable(false);
            enableDisableSubsidyButton(false);
        }else{
            subSidyEnableDisable(true);
            enableDisableSubsidyNewButton(true);
        }
        tblInspectionSelected=true;
        //        enableDisableInspectionNewButton(true);
        
    }//GEN-LAST:event_tblInspectionDetailsMouseClicked
    public void fillData(Object obj){
        HashMap map=(HashMap)obj;
        if(viewType.equals("EMP_ID")){
            txtValuatedBy.setText(CommonUtil.convertObjToStr(map.get("EMPLOYEE ID")));
            lblDisPlayName.setText(CommonUtil.convertObjToStr(map.get("EMP NAME")));
            lblDisplaylDesig.setText(CommonUtil.convertObjToStr(map.get("DESIGNATION")));
        }else if
        (viewType.equals("DEPOSIT_NO")){
            map.put("ACCOUNTNO", map.get("ACCOUNTNO")+"_1");
            txtDepositNo.setText(CommonUtil.convertObjToStr(map.get("ACCOUNTNO")));
            //            lblDisPlayName.setText(CommonUtil.convertObjToStr(map.get("EMP NAME")));
            //            lblDisplaylDesig.setText(CommonUtil.convertObjToStr(map.get("DESIGNATION")));
        }
    }
        private void btnSubsidyDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubsidyDeleteActionPerformed
            // TODO add your handling code here:
            selectedInspectionRow=tblInspectionDetails.getSelectedRow();
            agriSubSidyOB.deleteSubSidyData(selectedInspectionRow);
            subSidyEnableDisable(true);
            tblInspectionSelected=true;
            resetSubsidy();
            resetValutionDetails();
            subSidyEnableDisable(false);
            agriSubSidyOB.resetSubsidyDetails();
            enableDisableSubsidyNewButton(false);
        }//GEN-LAST:event_btnSubsidyDeleteActionPerformed
        
    private void btnSubsidySaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubsidySaveActionPerformed
        // TODO add your handling code here:
        String message="";
        //        if((((ComboBoxModel)cboTypeOfInspection.getModel()).getKeyForSelected().toString().length())>0)
        //            message=CommonUtil.convertObjToStr(((com.see.tools.workflow.clientutils.ComboBoxModel)cboTypeOfInspection.getModel()).getKeyForSelected());
        //        if(message.length()>0){
        //            ClientUtil.displayAlert("Select Type Of Inspection");
        //            message=CommonUtil.convertObjToStr(tdtDateOfInspection.getDateValue());
        //            return;
        //        }
        //        message=CommonUtil.convertObjToStr(tdtDateOfInspection.getDateValue());
        //        if(message.length()==0){
        //            ClientUtil.displayAlert("Choose Inspection Date");
        //            return;
        //        }
        //        message=CommonUtil.convertObjToStr(((ComboBoxModel)cboTypeOfInspection.getModel()).getKeyForSelected());
        //        if(message.length()==0){
        //            ClientUtil.displayAlert("Choose Type of Inspection");
        //            return;
        //        }
        //        message=CommonUtil.convertObjToStr(txtInspectBy.getText());
        //        if(message.length()==0){
        //            ClientUtil.displayAlert("Enter Inspect By");
        //            return;
        //        }
        updateOBFields();
        agriSubSidyOB.addSubsidy(selectedInspectionRow, tblInspectionSelected);
        resetSubsidy();
//        resetValutionDetails();
        agriSubSidyOB.resetFormComponets();
        subSidyEnableDisable(false);
        selectedInspectionRow=-1;
        enableDisableSubsidyNewButton(false);
    }//GEN-LAST:event_btnSubsidySaveActionPerformed
    
    private void btnSubSidyNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubSidyNewActionPerformed
        // TODO add your handling code here:
        selectedInspectionRow=-1;
        tblInspectionSelected=false;
        resetSubsidy();
//        resetValutionDetails();
        subSidyEnableDisable(true);
        enableDisableSubsidyNewButton(true);
    }//GEN-LAST:event_btnSubSidyNewActionPerformed
    
    private void btnValutiontNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnValutiontNewActionPerformed
        // TODO add your handling code here:
        selectedSubLimitRow=-1;
        tblSubLimitSelected=false;
        resetValutionDetails();
//        resetSubsidy();
        subLimitEnableDisable(true);
        btnInspectBy.setEnabled(true);
        enableDisableValutionButton(true);
    }//GEN-LAST:event_btnValutiontNewActionPerformed
    
    private void tblSubLimitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSubLimitMouseClicked
        // TODO add your handling code here:
        selectedSubLimitRow=tblSubLimit.getSelectedRow();
        agriSubSidyOB.setValutionDetails(selectedSubLimitRow,TrueTransactMain.selBranch);
        if ((agriSubSidyOB.getLblStatus().equals(ClientConstants.ACTION_STATUS[3])) ||
        (agriSubSidyOB.getLblStatus().equals(ClientConstants.ACTION_STATUS[8]))  ||
        (agriSubSidyOB.getLblStatus().equals(ClientConstants.ACTION_STATUS[10]))){
            subLimitEnableDisable(false);
            enableDisableValutionButton(false);
        }
        else{
            subLimitEnableDisable(true);
            enableDisableValutionNewButton(true);
        }
        tblSubLimitSelected=true;
        //        enableDisableSubLimitNewButton(true);
    }//GEN-LAST:event_tblSubLimitMouseClicked
    
    private void btnValutionDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnValutionDeleteActionPerformed
        // TODO add your handling code here:
        if(selectedSubLimitRow>=0)
            agriSubSidyOB.deleteSubLimitData(selectedSubLimitRow);
        subLimitEnableDisable(false);
        resetValutionDetails();
        resetSubsidy();
        agriSubSidyOB.resetFormComponets();
        tblSubLimitSelected=false;
        enableDisableValutionNewButton(false);
    }//GEN-LAST:event_btnValutionDeleteActionPerformed
    
    private void btnValutionSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnValutionSaveActionPerformed
        // TODO add your handling code here:
        String mandatoryMessage="";
        mandatoryMessage=CommonUtil.convertObjToStr(txtValutionAmt.getText());
        if(mandatoryMessage.equals("")){
            ClientUtil.displayAlert("Enter Valution Amount");
            return;
        }
        mandatoryMessage=CommonUtil.convertObjToStr(tdtValutionDt.getDateValue());
        if(mandatoryMessage.equals("")){
            ClientUtil.displayAlert("Enter Valuted Date");
            return;
        }
        
        mandatoryMessage=CommonUtil.convertObjToStr(txtPropertyType.getText());
        if(mandatoryMessage.equals("")){
            ClientUtil.displayAlert("Enter Property Details");
            return;
        }
        //        mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panValutionDetails);
        //        if(mandatoryMessage.length()>0){
        //            ClientUtil.displayAlert(String.valueOf(mandatoryMessage));
        //            return;
        //        }
        
        updateOBFields();
        //        if(agriSubSidyOB.validateValues(selectedSubLimitRow,tblSubLimitSelected))
        //            return;
        agriSubSidyOB.addValution(selectedSubLimitRow, tblSubLimitSelected);
        
        subLimitEnableDisable(false);
        resetValutionDetails();
//        resetSubsidy();
        agriSubSidyOB.resetFormComponets();
        selectedSubLimitRow=-1;
        enableDisableValutionNewButton(false);
    }//GEN-LAST:event_btnValutionSaveActionPerformed
    private void subLimitEnableDisable(boolean val ){
        ClientUtil.enableDisable(panValutionDetails,val);
    }
    private void subSidyEnableDisable(boolean val){
        
        cboTypeOfSubSidy.setEnabled(val);
        cboDepositProdId.setEnabled(val);
        txtDepositNo.setEnabled(val);
        tdlSubSidyDate.setEnabled(val);
        cboRecivedFrom.setEnabled(val);
        txtSubSidyAmt.setEnabled(val);
        txtAmtAdjusted.setEnabled(val);
        txtAmtRefunded.setEnabled(val);
        tdtRefundDate.setEnabled(val);
        txtOutStandingAmt.setEnabled(false);
        btnInspectBy2.setEnabled(val);
        
    }
    private void resetValutionDetails(){
        txtPropertyType.setText("");
        tdtValutionDt.setDateValue("");
        txtValutionAmt.setText("");
        txtValuatedBy.setText("");
        lblDisPlayName.setText("");
        lblDisplaylDesig.setText("");
        txtValutionRemarks .setText("");
    }
    
    private void resetSubsidy(){
        cboTypeOfSubSidy.setSelectedItem("");
        cboDepositProdId.setSelectedItem("");
        txtDepositNo.setText("");
        tdlSubSidyDate.setDateValue("");
        cboRecivedFrom.setSelectedItem("");
        txtSubSidyAmt.setText("");
        txtAmtAdjusted.setText("");
        txtAmtRefunded.setText("");
        tdtRefundDate.setDateValue("");
        txtOutStandingAmt.setText("");
    }
    public java.util.HashMap getMandatoryHashMap() {
        HashMap mandatoryMap=new HashMap();
        return mandatoryMap;
    }
    
    public void setMandatoryHashMap() {
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnInspectBy;
    private com.see.truetransact.uicomponent.CButton btnInspectBy2;
    private com.see.truetransact.uicomponent.CButton btnSubSidyNew;
    private com.see.truetransact.uicomponent.CButton btnSubsidyDelete;
    private com.see.truetransact.uicomponent.CButton btnSubsidySave;
    private com.see.truetransact.uicomponent.CButton btnValutionDelete;
    private com.see.truetransact.uicomponent.CButton btnValutionSave;
    private com.see.truetransact.uicomponent.CButton btnValutiontNew;
    private com.see.truetransact.uicomponent.CComboBox cboDepositProdId;
    private com.see.truetransact.uicomponent.CComboBox cboRecivedFrom;
    private com.see.truetransact.uicomponent.CComboBox cboTypeOfSubSidy;
    private com.see.truetransact.uicomponent.CLabel lblAmtAdjusted;
    private com.see.truetransact.uicomponent.CLabel lblAmtRefunded;
    private com.see.truetransact.uicomponent.CLabel lblDepositNo;
    private com.see.truetransact.uicomponent.CLabel lblDepositProdId;
    private com.see.truetransact.uicomponent.CLabel lblDesignation;
    private com.see.truetransact.uicomponent.CLabel lblDisPlayName;
    private com.see.truetransact.uicomponent.CLabel lblDisplaylDesig;
    private com.see.truetransact.uicomponent.CLabel lblName;
    private com.see.truetransact.uicomponent.CLabel lblOutStandingAmt;
    private com.see.truetransact.uicomponent.CLabel lblPropertyType;
    private com.see.truetransact.uicomponent.CLabel lblRecivedFrom;
    private com.see.truetransact.uicomponent.CLabel lblRefundDate;
    private com.see.truetransact.uicomponent.CLabel lblSubSidyAmt;
    private com.see.truetransact.uicomponent.CLabel lblSubSidyDate;
    private com.see.truetransact.uicomponent.CLabel lblSubsidyAcctNo;
    private com.see.truetransact.uicomponent.CLabel lblSubsidyShowAcctno;
    private com.see.truetransact.uicomponent.CLabel lblTypeOfSubSidy;
    private com.see.truetransact.uicomponent.CLabel lblValuatedBy;
    private com.see.truetransact.uicomponent.CLabel lblValutionAmt;
    private com.see.truetransact.uicomponent.CLabel lblValutionDt;
    private com.see.truetransact.uicomponent.CLabel lblValutionRemarks;
    private com.see.truetransact.uicomponent.CPanel panButton2;
    private com.see.truetransact.uicomponent.CPanel panButton2_SD2;
    private com.see.truetransact.uicomponent.CPanel panSanctionDetails_Upper;
    private com.see.truetransact.uicomponent.CPanel panSublimitDetails;
    private com.see.truetransact.uicomponent.CPanel panSubsidyAcctNo;
    private com.see.truetransact.uicomponent.CPanel panSubsidyDetails_Table;
    private com.see.truetransact.uicomponent.CPanel panTableFields_Subsidy;
    private com.see.truetransact.uicomponent.CPanel panTableValution;
    private com.see.truetransact.uicomponent.CPanel panTable_SubSidyDetails;
    private com.see.truetransact.uicomponent.CPanel panValutionDetails;
    private com.see.truetransact.uicomponent.CScrollPane srpTable_InspectionDetails;
    private com.see.truetransact.uicomponent.CScrollPane srpTable_SubLimit;
    private com.see.truetransact.uicomponent.CTable tblInspectionDetails;
    private com.see.truetransact.uicomponent.CTable tblSubLimit;
    private com.see.truetransact.uicomponent.CDateField tdlSubSidyDate;
    private com.see.truetransact.uicomponent.CDateField tdtRefundDate;
    private com.see.truetransact.uicomponent.CDateField tdtValutionDt;
    private com.see.truetransact.uicomponent.CTextField txtAmtAdjusted;
    private com.see.truetransact.uicomponent.CTextField txtAmtRefunded;
    private com.see.truetransact.uicomponent.CTextField txtDepositNo;
    private com.see.truetransact.uicomponent.CTextField txtOutStandingAmt;
    private com.see.truetransact.uicomponent.CTextField txtPropertyType;
    private com.see.truetransact.uicomponent.CTextField txtSubSidyAmt;
    private com.see.truetransact.uicomponent.CTextField txtValuatedBy;
    private com.see.truetransact.uicomponent.CTextField txtValutionAmt;
    private com.see.truetransact.uicomponent.CTextField txtValutionRemarks;
    // End of variables declaration//GEN-END:variables
    public static void main(String str[]){
        new AgriSubSidyUI().show();
    }
    
    public void update(java.util.Observable o, Object arg) {
        txtPropertyType.setText(agriSubSidyOB.getPropertyType());
        tdtValutionDt.setDateValue( agriSubSidyOB.getTdtValutionDt());
        txtValutionAmt.setText( agriSubSidyOB.getTxtValutionAmt());
        txtValuatedBy.setText(agriSubSidyOB.getTxtValuatedBy());
        lblDisPlayName.setText(agriSubSidyOB.getLblDisPlayName());
        lblDisplaylDesig.setText(agriSubSidyOB.getLbDisplaylDesig());
        txtValutionRemarks.setText(agriSubSidyOB.getValutionRemarks());
        ((com.see.truetransact.clientutil.ComboBoxModel)cboTypeOfSubSidy.getModel()).setKeyForSelected(agriSubSidyOB.getCboTypeOfSubSidy());
        
        ((com.see.truetransact.clientutil.ComboBoxModel)cboDepositProdId.getModel()).setKeyForSelected(agriSubSidyOB.getCboDepositProdId());
        txtDepositNo.setText(agriSubSidyOB.getDepositNo());
        tdlSubSidyDate.setDateValue(agriSubSidyOB.getSubSidyDate());
        ((com.see.truetransact.clientutil.ComboBoxModel)cboRecivedFrom.getModel()).setKeyForSelected(agriSubSidyOB.getCboRecivedFrom());
        txtDepositNo.setText(agriSubSidyOB.getDepositNo());
        txtSubSidyAmt.setText(agriSubSidyOB.getSubSidyAmt());
        txtAmtAdjusted.setText(agriSubSidyOB.getAmtAdjusted());
        txtAmtRefunded.setText(agriSubSidyOB.getAmtRefunded());
        tdtRefundDate.setDateValue(agriSubSidyOB.getRefundDate());
        txtOutStandingAmt.setText(agriSubSidyOB.getOutStandingAmt());
        
        //        txtSubLimitAmt.setText(agriSubSidyOB.getSubLimitAmt());
        //        tdtSubLimitFromDt.setDateValue(agriSubSidyOB.getSubLimitFromDt());
        //        tdtSubLimitToDt.setDateValue(agriSubSidyOB.getSubLimitToDt());
        //        ((com.see.truetransact.clientutil.ComboBoxModel)cboTypeOfInspection.getModel()).setKeyForSelected(agriSubSidyOB.getCboTypeOfInspection());
        //        tdtDateOfInspection.setDateValue(agriSubSidyOB.getDateOfInspection());
        //        ((com.see.truetransact.clientutil.ComboBoxModel)cboInspectionDetails.getModel()).setKeyForSelected(agriSubSidyOB.getCboInspectionDetails());
        //        txtInspectBy.setText(agriSubSidyOB.getInspectBy());
        //        textAreaInspectObservation.setText(agriSubSidyOB.getInspectObservation());
    }
    public void updateOBFields(){
        agriSubSidyOB.setPropertyType(txtPropertyType.getText());
        agriSubSidyOB.setTdtValutionDt(tdtValutionDt.getDateValue());
        agriSubSidyOB.setTxtValutionAmt(txtValutionAmt.getText());
        agriSubSidyOB.setTxtValuatedBy(txtValuatedBy.getText());
        agriSubSidyOB.setLblDisPlayName(lblDisPlayName.getText());
        agriSubSidyOB.setLbDisplaylDesig(lblDisplaylDesig.getText());
        agriSubSidyOB.setValutionRemarks(txtValutionRemarks.getText());
        agriSubSidyOB.setCboTypeOfSubSidy(CommonUtil.convertObjToStr(cboTypeOfSubSidy.getSelectedItem()));
        agriSubSidyOB.setCboDepositProdId(CommonUtil.convertObjToStr(cboDepositProdId.getSelectedItem()));
        agriSubSidyOB. setDepositNo(txtDepositNo.getText());
        agriSubSidyOB. setSubSidyDate(tdlSubSidyDate.getDateValue());
        agriSubSidyOB. setCboRecivedFrom(CommonUtil.convertObjToStr(cboRecivedFrom.getSelectedItem()));
        agriSubSidyOB. setSubSidyAmt(txtSubSidyAmt.getText());
        agriSubSidyOB. setAmtAdjusted(txtAmtAdjusted.getText());
        agriSubSidyOB. setAmtRefunded(txtAmtRefunded.getText());
        agriSubSidyOB.  setRefundDate(tdtRefundDate.getDateValue());
        agriSubSidyOB.  setOutStandingAmt(txtOutStandingAmt.getText());
    }
    
    /**
     * Getter for property agriSubSidyOB.
     * @return Value of property agriSubSidyOB.
     */
    public com.see.truetransact.ui.termloan.agritermloan.agrisubsidydetails.AgriSubSidyOB getAgriSubSidyOB() {
        return agriSubSidyOB;
    }
    
    /**
     * Setter for property agriSubSidyOB.
     * @param agriSubSidyOB New value of property agriSubSidyOB.
     */
    public void setAgriSubSidyOB(com.see.truetransact.ui.termloan.agritermloan.agrisubsidydetails.AgriSubSidyOB agriSubSidyOB) {
        this.agriSubSidyOB = agriSubSidyOB;
    }
    
    
}
