/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AgriSubLimitUI.java
 *
 * Created on April 28, 2009, 5:02 PM
 */

package com.see.truetransact.ui.termloan.agritermloan.agrinewdetails;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientUtil;
//import com.see.tools.workflow.clientutils.ComboBoxModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uimandatory.MandatoryDBCheck;
import com.see.truetransact.clientutil.ComboBoxModel;
import org.apache.log4j.Logger;
import java.util.HashMap;


/**
 *
 * @author  Administrator
 */
public class AgriSubLimitUI extends com.see.truetransact.uicomponent.CPanel  implements java.util.Observer,UIMandatoryField{
    java.util.ResourceBundle resourceBundle= java.util.ResourceBundle.getBundle("com.see.truetransact.ui.termloan.agritermloan.agrinewdetails.AgriSubLimitRB",ProxyParameters.LANGUAGE);
    private AgriSubLimitOB agriSubLimitOB;
    private final static Logger log = Logger.getLogger(AgriSubLimitUI.class);
    private int selectedSubLimitRow=-1;
    private int selectedInspectionRow=-1;
    private boolean tblInspectionSelected=false;
    private String viewType = "";
    private HashMap mandatoryMap=null;
   
    
    /** Creates new form BeanForm */
    public AgriSubLimitUI() {
        initComponents();
        setFieldNames();
        setObservable();
        initComponentData();
        setMaxLength();
        internationalize();
        setEnableDisable(false);
        setMandatoryHashMap();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panTableFields_Inspection);

        //         new MandatoryCheck().putMandatoryMarks(getClass().getName(), panSanctionDetails_Sanction);
    }
   
   
    private void setObservable(){
        agriSubLimitOB=new AgriSubLimitOB();
        agriSubLimitOB.addObserver(this);
    }
    private void initComponentData(){
        cboPurpose.setModel(agriSubLimitOB.getCbmPurpose());
        cboType.setModel(agriSubLimitOB.getCbmType());
        tblInspectionDetails.setModel(agriSubLimitOB.getTblInspectionDetails());
        tblSubLimit.setModel(agriSubLimitOB.getTblSubLimit());
    }
    private void  setFieldNames(){
        lblSubLimitAmt.setName("lblSubLimitAmt");
        txtSubLimitAmt.setName("txtSubLimitAmt");
        lblSubLimitFromDt.setName("lblSubLimitFromDt");
        tdtSubLimitFromDt.setName("tdtSubLimitFromDt");
        lblSubLimitToDt.setName("lblSubLimitToDt");
        tdtSubLimitToDt.setName("tdtSubLimitToDt");
        lblPurpose.setName("lblPurpose");
        cboPurpose.setName("cboPurpose");
        lblType.setName("lblType");
        cboType.setName("cboType");
        txtHectare.setName("txtHectare");
        lblHectare.setName("lblHectare");
        lblSurveryNo.setName("lblSurveryNo");
        txtSurveyNo.setName("txtSurveyNo");
        lblRemarks.setName("lblRemarks");
        textAreaRemarks.setName("textAreaRemarks");
        btnInspectionNew.setName("btnInspectionNew");
        btnInspectionSave.setName("btnInspectionSave");
        btnInspectionDelete.setName("btnInspectionDelete");
        panTableFields_Inspection.setName("panTableFields_Inspection");
        panTable_InspectionDetails.setName("panTable_InspectionDetails");
        btnsubLimitNew.setName("btnsubLimitNew");
        btnSubLimitSave.setName("btnSubLimitSave");
        btnSubLimitDelete.setName("btnSubLimitDelete");
        panTable2_SubLimit.setName("panTable2_SubLimit");
        panSanctionDetails_Upper.setName("panSanctionDetails_Upper");
        panTable2_SubLimit.setName("panTable2_SubLimit");
        panSanctionDetails_Sanction.setName("panSanctionDetails_Sanction");
        panButton2_SD2.setName("panButton2_SD2");
        panButton2.setName("panButton2");
        scrollPaneObservation.setName("scrollPaneObservation");
    }
    private void setEnableDisable(boolean val){
        ClientUtil.enableDisable(panSanctionDetails_Upper,val);
        ClientUtil.enableDisable(panSanctionDetails_Sanction,val);
        ClientUtil.enableDisable(panButton2_SD2,val);
        ClientUtil.enableDisable(panTableFields_Inspection,val);
        ClientUtil.enableDisable(panSanctionDetails_Table1,val);
        enableDisableSubLimitButton(val);
        enableDisableInspectionButton(val);
        
    }
    private void setMaxLength(){
        txtSubLimitAmt.setValidation(new CurrencyValidation());
        txtHectare.setAllowNumber(true);
        txtSurveyNo.setAllowAll(true);
        
    }
    public void enableDisableSubLimitButton(boolean val){
        btnsubLimitNew.setEnabled(val);
        btnSubLimitSave.setEnabled(val);
        btnSubLimitDelete.setEnabled(val);
    }
    public void enableDisableSubLimitNewButton(boolean val){
        btnsubLimitNew.setEnabled(!val);
        btnSubLimitSave.setEnabled(val);
        btnSubLimitDelete.setEnabled(val);
    }
    
    public void enableDisableInspectionButton(boolean val){
        btnInspectionNew.setEnabled(val);
        btnInspectionSave.setEnabled(val);
        btnInspectionDelete.setEnabled(val);
    }
    public  void enableDisableInspectionNewButton(boolean val){
        btnInspectionNew.setEnabled(!val);
        btnInspectionSave.setEnabled(val);
        btnInspectionDelete.setEnabled(val);
    }
    
    
    private void internationalize(){
        lblSubLimitAmt.setText(resourceBundle.getString("lblSubLimitAmt"));
        lblSubLimitToDt.setText(resourceBundle.getString("lblSubLimitToDt"));
        btnInspectionNew.setText(resourceBundle.getString("btnInspectionNew"));
        btnInspectionSave.setText(resourceBundle.getString("btnInspectionSave"));
        btnInspectionDelete.setText(resourceBundle.getString("btnInspectionDelete"));
        btnsubLimitNew.setText(resourceBundle.getString("btnsubLimitNew"));
        btnSubLimitSave.setText(resourceBundle.getString("btnSubLimitSave"));
        btnSubLimitDelete.setText(resourceBundle.getString("btnSubLimitDelete"));
        lblHectare.setText(resourceBundle.getString("lblHectare"));
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
        panSanctionDetails_Sanction = new com.see.truetransact.uicomponent.CPanel();
        lblSubLimitToDt = new com.see.truetransact.uicomponent.CLabel();
        tdtSubLimitFromDt = new com.see.truetransact.uicomponent.CDateField();
        txtSubLimitAmt = new com.see.truetransact.uicomponent.CTextField();
        lblSubLimitAmt = new com.see.truetransact.uicomponent.CLabel();
        lblSubLimitFromDt = new com.see.truetransact.uicomponent.CLabel();
        tdtSubLimitToDt = new com.see.truetransact.uicomponent.CDateField();
        panButton2_SD2 = new com.see.truetransact.uicomponent.CPanel();
        btnsubLimitNew = new com.see.truetransact.uicomponent.CButton();
        btnSubLimitSave = new com.see.truetransact.uicomponent.CButton();
        btnSubLimitDelete = new com.see.truetransact.uicomponent.CButton();
        panSanctionDetails_Table1 = new com.see.truetransact.uicomponent.CPanel();
        panTableFields_Inspection = new com.see.truetransact.uicomponent.CPanel();
        lblPurpose = new com.see.truetransact.uicomponent.CLabel();
        cboPurpose = new com.see.truetransact.uicomponent.CComboBox();
        lblHectare = new com.see.truetransact.uicomponent.CLabel();
        txtHectare = new com.see.truetransact.uicomponent.CTextField();
        panButton2 = new com.see.truetransact.uicomponent.CPanel();
        btnInspectionNew = new com.see.truetransact.uicomponent.CButton();
        btnInspectionSave = new com.see.truetransact.uicomponent.CButton();
        btnInspectionDelete = new com.see.truetransact.uicomponent.CButton();
        cboType = new com.see.truetransact.uicomponent.CComboBox();
        lblType = new com.see.truetransact.uicomponent.CLabel();
        lblSurveryNo = new com.see.truetransact.uicomponent.CLabel();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        scrollPaneObservation = new com.see.truetransact.uicomponent.CScrollPane();
        textAreaRemarks = new com.see.truetransact.uicomponent.CTextArea();
        txtSurveyNo = new com.see.truetransact.uicomponent.CTextField();
        panTable_InspectionDetails = new com.see.truetransact.uicomponent.CPanel();
        srpTable_InspectionDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblInspectionDetails = new com.see.truetransact.uicomponent.CTable();
        panTable2_SubLimit = new com.see.truetransact.uicomponent.CPanel();
        srpTable_SubLimit = new com.see.truetransact.uicomponent.CScrollPane();
        tblSubLimit = new com.see.truetransact.uicomponent.CTable();

        setLayout(new java.awt.GridBagLayout());

        setMinimumSize(new java.awt.Dimension(820, 550));
        setPreferredSize(new java.awt.Dimension(820, 550));
        panSublimitDetails.setLayout(new java.awt.GridBagLayout());

        panSublimitDetails.setMinimumSize(new java.awt.Dimension(820, 550));
        panSublimitDetails.setPreferredSize(new java.awt.Dimension(820, 550));
        panSanctionDetails_Upper.setLayout(new java.awt.GridBagLayout());

        panSanctionDetails_Upper.setMaximumSize(new java.awt.Dimension(435, 40));
        panSanctionDetails_Upper.setMinimumSize(new java.awt.Dimension(435, 40));
        panSanctionDetails_Upper.setPreferredSize(new java.awt.Dimension(435, 40));
        panSanctionDetails_Sanction.setLayout(new java.awt.GridBagLayout());

        panSanctionDetails_Sanction.setMinimumSize(new java.awt.Dimension(179, 80));
        panSanctionDetails_Sanction.setPreferredSize(new java.awt.Dimension(179, 80));
        lblSubLimitToDt.setText("SubLimitToDt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panSanctionDetails_Sanction.add(lblSubLimitToDt, gridBagConstraints);

        tdtSubLimitFromDt.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtSubLimitFromDt.setNextFocusableComponent(tdtSubLimitToDt);
        tdtSubLimitFromDt.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtSubLimitFromDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtSubLimitFromDtFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSanctionDetails_Sanction.add(tdtSubLimitFromDt, gridBagConstraints);

        txtSubLimitAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSubLimitAmt.setNextFocusableComponent(tdtSubLimitFromDt);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSanctionDetails_Sanction.add(txtSubLimitAmt, gridBagConstraints);

        lblSubLimitAmt.setText("SubLimitAmt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panSanctionDetails_Sanction.add(lblSubLimitAmt, gridBagConstraints);

        lblSubLimitFromDt.setText("SubLimitFrom Dt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panSanctionDetails_Sanction.add(lblSubLimitFromDt, gridBagConstraints);

        tdtSubLimitToDt.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtSubLimitToDt.setNextFocusableComponent(btnSubLimitSave);
        tdtSubLimitToDt.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtSubLimitToDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtSubLimitToDtFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSanctionDetails_Sanction.add(tdtSubLimitToDt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panSanctionDetails_Upper.add(panSanctionDetails_Sanction, gridBagConstraints);

        panButton2_SD2.setLayout(new java.awt.GridBagLayout());

        panButton2_SD2.setMinimumSize(new java.awt.Dimension(215, 33));
        panButton2_SD2.setPreferredSize(new java.awt.Dimension(215, 33));
        btnsubLimitNew.setText("New");
        btnsubLimitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsubLimitNewActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton2_SD2.add(btnsubLimitNew, gridBagConstraints);

        btnSubLimitSave.setText("Save");
        btnSubLimitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubLimitSaveActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton2_SD2.add(btnSubLimitSave, gridBagConstraints);

        btnSubLimitDelete.setText("Delete");
        btnSubLimitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubLimitDeleteActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton2_SD2.add(btnSubLimitDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSanctionDetails_Upper.add(panButton2_SD2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSublimitDetails.add(panSanctionDetails_Upper, gridBagConstraints);

        panSanctionDetails_Table1.setLayout(new java.awt.GridBagLayout());

        panSanctionDetails_Table1.setBorder(new javax.swing.border.TitledBorder("SubLimit Details"));
        panSanctionDetails_Table1.setMaximumSize(new java.awt.Dimension(725, 280));
        panSanctionDetails_Table1.setMinimumSize(new java.awt.Dimension(725, 280));
        panSanctionDetails_Table1.setPreferredSize(new java.awt.Dimension(725, 280));
        panTableFields_Inspection.setLayout(new java.awt.GridBagLayout());

        panTableFields_Inspection.setMinimumSize(new java.awt.Dimension(400, 250));
        panTableFields_Inspection.setPreferredSize(new java.awt.Dimension(400, 250));
        lblPurpose.setText("Purpose");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_Inspection.add(lblPurpose, gridBagConstraints);

        cboPurpose.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboPurpose.setMinimumSize(new java.awt.Dimension(100, 21));
        cboPurpose.setPopupWidth(150);
        cboPurpose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboPurposeActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_Inspection.add(cboPurpose, gridBagConstraints);

        lblHectare.setText("Hectare");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_Inspection.add(lblHectare, gridBagConstraints);

        txtHectare.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_Inspection.add(txtHectare, gridBagConstraints);

        panButton2.setLayout(new java.awt.GridBagLayout());

        btnInspectionNew.setText("New");
        btnInspectionNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInspectionNewActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton2.add(btnInspectionNew, gridBagConstraints);

        btnInspectionSave.setText("Save");
        btnInspectionSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInspectionSaveActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton2.add(btnInspectionSave, gridBagConstraints);

        btnInspectionDelete.setText("Delete");
        btnInspectionDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInspectionDeleteActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton2.add(btnInspectionDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTableFields_Inspection.add(panButton2, gridBagConstraints);

        cboType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboType.setNextFocusableComponent(txtHectare);
        cboType.setPopupWidth(250);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_Inspection.add(cboType, gridBagConstraints);

        lblType.setText("Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_Inspection.add(lblType, gridBagConstraints);

        lblSurveryNo.setText("Survey No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_Inspection.add(lblSurveryNo, gridBagConstraints);

        lblRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_Inspection.add(lblRemarks, gridBagConstraints);

        scrollPaneObservation.setMinimumSize(new java.awt.Dimension(254, 54));
        textAreaRemarks.setMinimumSize(new java.awt.Dimension(250, 50));
        textAreaRemarks.setNextFocusableComponent(btnInspectionSave);
        textAreaRemarks.setPreferredSize(new java.awt.Dimension(250, 50));
        scrollPaneObservation.setViewportView(textAreaRemarks);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        panTableFields_Inspection.add(scrollPaneObservation, gridBagConstraints);

        txtSurveyNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_Inspection.add(txtSurveyNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSanctionDetails_Table1.add(panTableFields_Inspection, gridBagConstraints);

        panTable_InspectionDetails.setLayout(new java.awt.GridBagLayout());

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
        panTable_InspectionDetails.add(srpTable_InspectionDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSanctionDetails_Table1.add(panTable_InspectionDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSublimitDetails.add(panSanctionDetails_Table1, gridBagConstraints);

        panTable2_SubLimit.setLayout(new java.awt.GridBagLayout());

        panTable2_SubLimit.setMaximumSize(new java.awt.Dimension(330, 40));
        panTable2_SubLimit.setMinimumSize(new java.awt.Dimension(330, 40));
        panTable2_SubLimit.setPreferredSize(new java.awt.Dimension(330, 40));
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

        panTable2_SubLimit.add(srpTable_SubLimit, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSublimitDetails.add(panTable2_SubLimit, gridBagConstraints);

        add(panSublimitDetails, new java.awt.GridBagConstraints());

    }//GEN-END:initComponents

    private void cboPurposeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPurposeActionPerformed
        // TODO add your handling code here:
        String purpose=CommonUtil.convertObjToStr(
         ((ComboBoxModel)cboPurpose.getModel()).getKeyForSelected());
        if(purpose.length()>0){
            agriSubLimitOB.setCbmProdId(purpose);
            cboType.setModel(agriSubLimitOB.getCbmType());
            if(purpose.equals("CROPLOAN"))
            lblHectare.setText(resourceBundle.getString("lblHectare"));
            if(purpose.equals("DAIRYLOAN"))
            lblHectare.setText(resourceBundle.getString("lblNumberCount"));
            
        }
    }//GEN-LAST:event_cboPurposeActionPerformed

    private void tdtSubLimitToDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtSubLimitToDtFocusLost
        // TODO add your handling code here:
        String fromDt=CommonUtil.convertObjToStr(tdtSubLimitFromDt.getDateValue());
        if(fromDt.length()==0){
              ClientUtil.displayAlert("Enter From Date");
              return;
    }
        ClientUtil.validateToDate(tdtSubLimitToDt,tdtSubLimitFromDt.getDateValue(),true);
            
        ClientUtil.validateLessDate(tdtSubLimitToDt,agriSubLimitOB.getLoanExpiryDate());
       
    }//GEN-LAST:event_tdtSubLimitToDtFocusLost
    
    private void tdtSubLimitFromDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtSubLimitFromDtFocusLost
        // TODO add your handling code here:
        String subLimitFromDt=CommonUtil.convertObjToStr(tdtSubLimitFromDt.getDateValue());
        if(subLimitFromDt.length()>0)
        ClientUtil.validateToDate(tdtSubLimitFromDt, agriSubLimitOB.getLoanOpenDt());
    }//GEN-LAST:event_tdtSubLimitFromDtFocusLost
    
    private void tblInspectionDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblInspectionDetailsMouseClicked
        // TODO add your handling code here:
        
        selectedInspectionRow=tblInspectionDetails.getSelectedRow();
        agriSubLimitOB.setInspectionDetails(selectedInspectionRow);
        cboType.setModel(agriSubLimitOB.getCbmType());
        if ((agriSubLimitOB.getLblStatus().equals(ClientConstants.ACTION_STATUS[3])) ||
        (agriSubLimitOB.getLblStatus().equals(ClientConstants.ACTION_STATUS[8]))  ||
        (agriSubLimitOB.getLblStatus().equals(ClientConstants.ACTION_STATUS[10]))){
            inspectionEnableDisable(false);
            enableDisableInspectionButton(false);
        }else{
            inspectionEnableDisable(true);
            enableDisableInspectionNewButton(true);
        }
        tblInspectionSelected=true;
//        enableDisableInspectionNewButton(true);
        
    }//GEN-LAST:event_tblInspectionDetailsMouseClicked
        public void fillData(Object obj){
        HashMap map=(HashMap)obj;
        if(viewType.equals("EMP_ID")){
//            txtInspectBy.setText(CommonUtil.convertObjToStr(map.get("EMPLOYEE ID")));
//            lblInspectName.setText(CommonUtil.convertObjToStr(map.get("EMP NAME")));
//            lblInspectPositions.setText(CommonUtil.convertObjToStr(map.get("DESIGNATION")));
        }
    }
    private void btnInspectionDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInspectionDeleteActionPerformed
        // TODO add your handling code here:
        selectedInspectionRow=tblInspectionDetails.getSelectedRow();
        agriSubLimitOB.deleteInspectionData(selectedInspectionRow,agriSubLimitOB.isTblSubLimitSelected());
        inspectionEnableDisable(true);
        tblInspectionSelected=true;
        resetInspection();
        agriSubLimitOB.resetFormComponets();
//        resetSubLimit();
        inspectionEnableDisable(false);
        enableDisableInspectionNewButton(false);
    }//GEN-LAST:event_btnInspectionDeleteActionPerformed
    
    private void btnInspectionSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInspectionSaveActionPerformed
        // TODO add your handling code here:
        String message="";
         final String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panTableFields_Inspection);
        //        if((((ComboBoxModel)cboTypeOfInspection.getModel()).getKeyForSelected().toString().length())>0)
        //            message=CommonUtil.convertObjToStr(((com.see.tools.workflow.clientutils.ComboBoxModel)cboTypeOfInspection.getModel()).getKeyForSelected());
        if(mandatoryMessage.length()>0){
            ClientUtil.displayAlert(mandatoryMessage);
//            message=CommonUtil.convertObjToStr(tdtDateOfInspection.getDateValue());
            return;
        }
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
        agriSubLimitOB.addInpection(selectedInspectionRow, tblInspectionSelected,agriSubLimitOB.isTblSubLimitSelected());
        resetInspection();
//        resetSubLimit();
        agriSubLimitOB.resetFormComponets();
        inspectionEnableDisable(false);
        selectedInspectionRow=-1;
        enableDisableInspectionNewButton(false);
    }//GEN-LAST:event_btnInspectionSaveActionPerformed
    
    private void btnInspectionNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInspectionNewActionPerformed
        // TODO add your handling code here:
        if(agriSubLimitOB.isTblSubLimitSelected()==false)
        {
            ClientUtil.showMessageWindow("Choose Any One SubLimit From SubLimit Table");
            return;
        }
        selectedInspectionRow=-1;
        tblInspectionSelected=false;
        resetInspection();
//        resetSubLimit();
        inspectionEnableDisable(true);
        enableDisableInspectionNewButton(true);
    }//GEN-LAST:event_btnInspectionNewActionPerformed
    
    private void btnsubLimitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsubLimitNewActionPerformed
        // TODO add your handling code here:
        selectedSubLimitRow=-1;
        agriSubLimitOB.setSelectedSubLimitRow(this.selectedSubLimitRow);
        agriSubLimitOB.setTblSubLimitSelected(false);
        agriSubLimitOB.setMainSlno(String.valueOf(-1));
        resetSubLimit();
//        resetInspection();
        subLimitEnableDisable(true);
        enableDisableSubLimitButton(true);
    }//GEN-LAST:event_btnsubLimitNewActionPerformed
    
    private void tblSubLimitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSubLimitMouseClicked
        // TODO add your handling code here:
        selectedSubLimitRow=tblSubLimit.getSelectedRow();
        agriSubLimitOB.setSelectedSubLimitRow(this.selectedSubLimitRow);
//        if(tblSubLimitSelected==true){
//            ClientUtil.showMessageWindow("First Save SubLimit");
//            return;
//        }
        agriSubLimitOB.setSubLimitDetails(selectedSubLimitRow);
        if ((agriSubLimitOB.getLblStatus().equals(ClientConstants.ACTION_STATUS[3])) ||
        (agriSubLimitOB.getLblStatus().equals(ClientConstants.ACTION_STATUS[8]))  ||
        (agriSubLimitOB.getLblStatus().equals(ClientConstants.ACTION_STATUS[10]))){
            subLimitEnableDisable(false);
            enableDisableSubLimitButton(false);
        }
        else{
            subLimitEnableDisable(true);
            enableDisableSubLimitNewButton(true);
        }
        agriSubLimitOB.setTblSubLimitSelected(true);
//        enableDisableSubLimitNewButton(true);
    }//GEN-LAST:event_tblSubLimitMouseClicked
    
    private void btnSubLimitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubLimitDeleteActionPerformed
        // TODO add your handling code here:
        if(tblInspectionDetails.getRowCount()>0){
            ClientUtil.showMessageWindow("First Delete SubLimit Details");
            return;
        }
        if(selectedSubLimitRow>=0)
            agriSubLimitOB.deleteSubLimitData(selectedSubLimitRow);
        subLimitEnableDisable(false);
        resetSubLimit();
        resetInspection();
        agriSubLimitOB.resetFormComponetsSubLimit();
        agriSubLimitOB.setTblSubLimitSelected(false);

        enableDisableSubLimitNewButton(false);
    }//GEN-LAST:event_btnSubLimitDeleteActionPerformed
    
    private void btnSubLimitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubLimitSaveActionPerformed
        // TODO add your handling code here:
         final String mandatoryMessages = new MandatoryDBCheck().checkMandatory(getClass().getName(), panSanctionDetails_Sanction);
        String mandatoryMessage="";
        mandatoryMessage=CommonUtil.convertObjToStr(txtSubLimitAmt.getText());
        if(mandatoryMessage.equals("")){
            ClientUtil.displayAlert("Enter Sanction Amount");
            return;
        }
        mandatoryMessage=CommonUtil.convertObjToStr(tdtSubLimitFromDt.getDateValue());
        if(mandatoryMessage.equals("")){
            ClientUtil.displayAlert("Enter Sanction From Date");
            return;
        }
        
        mandatoryMessage=CommonUtil.convertObjToStr(tdtSubLimitToDt.getDateValue());
        if(mandatoryMessage.equals("")){
            ClientUtil.displayAlert("Enter Sanction To Date");
            return;
        }
        
        updateOBFields();
        if(agriSubLimitOB.validateValues(selectedSubLimitRow, agriSubLimitOB.isTblSubLimitSelected()))
            return;
        agriSubLimitOB.addSubLimit(selectedSubLimitRow, agriSubLimitOB.isTblSubLimitSelected());
        
        subLimitEnableDisable(false);
        resetSubLimit();
//        resetInspection();
       
        if(agriSubLimitOB.isTblSubLimitSelected()==false)
            ClientUtil.showMessageWindow("Atleast One Record Enter in SubLimit Details");
        agriSubLimitOB.setTblSubLimitSelected(false);
         agriSubLimitOB.resetFormComponets();
        agriSubLimitOB.resetFormComponetsSubLimit();
        selectedSubLimitRow=-1;
        agriSubLimitOB.setSelectedSubLimitRow(this.selectedSubLimitRow);
        enableDisableSubLimitNewButton(false);
        
        
    }//GEN-LAST:event_btnSubLimitSaveActionPerformed
    private void subLimitEnableDisable(boolean val ){
        ClientUtil.enableDisable(panSanctionDetails_Sanction,val);
    }
    private void inspectionEnableDisable(boolean val){
        cboType.setEnabled(val);
        cboPurpose.setEnabled(val);
        txtHectare.setEnabled(val);
        txtSurveyNo.setEnabled(val);
        textAreaRemarks.setEnabled(val);
    }
    private void resetSubLimit(){
        txtSubLimitAmt.setText("");
        tdtSubLimitFromDt.setDateValue("");
        tdtSubLimitToDt.setDateValue("");
    }
    private void resetInspection(){
        cboType.setSelectedItem("");
        cboPurpose.setSelectedItem("");
        txtHectare.setText("");
        txtSurveyNo.setText("");
        textAreaRemarks.setText("");
    }
    public java.util.HashMap getMandatoryHashMap() {
//        HashMap mandatoryMap=new HashMap();
        return mandatoryMap;
    }
    
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtSubLimitAmt", new Boolean(true));
        mandatoryMap.put("tdtSubLimitFromDt", new Boolean(true));
        mandatoryMap.put("tdtSubLimitToDt", new Boolean(true));
        mandatoryMap.put("cboPurpose", new Boolean(true));
        mandatoryMap.put("cboType", new Boolean(true));
        mandatoryMap.put("txtHectare", new Boolean(true));
        mandatoryMap.put("txtSurveyNo", new Boolean(true));
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnInspectionDelete;
    private com.see.truetransact.uicomponent.CButton btnInspectionNew;
    private com.see.truetransact.uicomponent.CButton btnInspectionSave;
    private com.see.truetransact.uicomponent.CButton btnSubLimitDelete;
    private com.see.truetransact.uicomponent.CButton btnSubLimitSave;
    private com.see.truetransact.uicomponent.CButton btnsubLimitNew;
    private com.see.truetransact.uicomponent.CComboBox cboPurpose;
    private com.see.truetransact.uicomponent.CComboBox cboType;
    private com.see.truetransact.uicomponent.CLabel lblHectare;
    private com.see.truetransact.uicomponent.CLabel lblPurpose;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
    private com.see.truetransact.uicomponent.CLabel lblSubLimitAmt;
    private com.see.truetransact.uicomponent.CLabel lblSubLimitFromDt;
    private com.see.truetransact.uicomponent.CLabel lblSubLimitToDt;
    private com.see.truetransact.uicomponent.CLabel lblSurveryNo;
    private com.see.truetransact.uicomponent.CLabel lblType;
    private com.see.truetransact.uicomponent.CPanel panButton2;
    private com.see.truetransact.uicomponent.CPanel panButton2_SD2;
    private com.see.truetransact.uicomponent.CPanel panSanctionDetails_Sanction;
    private com.see.truetransact.uicomponent.CPanel panSanctionDetails_Table1;
    private com.see.truetransact.uicomponent.CPanel panSanctionDetails_Upper;
    private com.see.truetransact.uicomponent.CPanel panSublimitDetails;
    private com.see.truetransact.uicomponent.CPanel panTable2_SubLimit;
    private com.see.truetransact.uicomponent.CPanel panTableFields_Inspection;
    private com.see.truetransact.uicomponent.CPanel panTable_InspectionDetails;
    private com.see.truetransact.uicomponent.CScrollPane scrollPaneObservation;
    private com.see.truetransact.uicomponent.CScrollPane srpTable_InspectionDetails;
    private com.see.truetransact.uicomponent.CScrollPane srpTable_SubLimit;
    private com.see.truetransact.uicomponent.CTable tblInspectionDetails;
    private com.see.truetransact.uicomponent.CTable tblSubLimit;
    private com.see.truetransact.uicomponent.CDateField tdtSubLimitFromDt;
    private com.see.truetransact.uicomponent.CDateField tdtSubLimitToDt;
    private com.see.truetransact.uicomponent.CTextArea textAreaRemarks;
    private com.see.truetransact.uicomponent.CTextField txtHectare;
    private com.see.truetransact.uicomponent.CTextField txtSubLimitAmt;
    private com.see.truetransact.uicomponent.CTextField txtSurveyNo;
    // End of variables declaration//GEN-END:variables
    public static void main(String str[]){
        new AgriSubLimitUI().show();
    }
    
    public void update(java.util.Observable o, Object arg) {
        txtSubLimitAmt.setText(agriSubLimitOB.getSubLimitAmt());
        tdtSubLimitFromDt.setDateValue(agriSubLimitOB.getSubLimitFromDt());
        tdtSubLimitToDt.setDateValue(agriSubLimitOB.getSubLimitToDt());
        ((com.see.truetransact.clientutil.ComboBoxModel)cboPurpose.getModel()).setKeyForSelected(agriSubLimitOB.getCboPurpose());
        ((com.see.truetransact.clientutil.ComboBoxModel)cboType.getModel()).setKeyForSelected(agriSubLimitOB.getCboType());
        txtHectare.setText(agriSubLimitOB.getHectare());
        textAreaRemarks.setText(agriSubLimitOB.getRemarks());
        txtSurveyNo.setText(agriSubLimitOB.getSurveyno());
    }
    public void updateOBFields(){
        agriSubLimitOB.setSubLimitAmt(txtSubLimitAmt.getText());
        agriSubLimitOB.setSubLimitFromDt(tdtSubLimitFromDt.getDateValue());
        agriSubLimitOB.setSubLimitToDt(tdtSubLimitToDt.getDateValue());
        
        agriSubLimitOB.setCboType(CommonUtil.convertObjToStr(cboType.getSelectedItem()));
        agriSubLimitOB.setCboPurpose(CommonUtil.convertObjToStr(cboPurpose.getSelectedItem()));
        agriSubLimitOB.setHectare(CommonUtil.convertObjToStr(txtHectare.getText()));
        agriSubLimitOB.setSurveyno(CommonUtil.convertObjToStr(txtSurveyNo.getText()));
         agriSubLimitOB.setRemarks(CommonUtil.convertObjToStr(textAreaRemarks.getText()));
    }
    /**
     * Getter for property agriSubLimitOB.
     * @return Value of property agriSubLimitOB.
     */
    public com.see.truetransact.ui.termloan.agritermloan.agrinewdetails.AgriSubLimitOB getAgriSubLimitOB() {
        return agriSubLimitOB;
    }
    
    /**
     * Setter for property agriSubLimitOB.
     * @param agriSubLimitOB New value of property agriSubLimitOB.
     */
    public void setAgriSubLimitOB(com.see.truetransact.ui.termloan.agritermloan.agrinewdetails.AgriSubLimitOB agriSubLimitOB) {
        this.agriSubLimitOB = agriSubLimitOB;
    }
    
   
    
}
