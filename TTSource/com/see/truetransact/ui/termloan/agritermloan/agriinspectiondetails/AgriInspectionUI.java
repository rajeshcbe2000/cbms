/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AgriInspectionUI.java
 *
 * Created on April 28, 2009, 5:02 PM
 */

package com.see.truetransact.ui.termloan.agritermloan.agriinspectiondetails;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.tools.workflow.clientutils.ComboBoxModel;
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
public class AgriInspectionUI extends com.see.truetransact.uicomponent.CPanel  implements java.util.Observer,UIMandatoryField{
    java.util.ResourceBundle resourceBundle= java.util.ResourceBundle.getBundle("com.see.truetransact.ui.termloan.agritermloan.agriinspectiondetails.AgriInspectionRB",ProxyParameters.LANGUAGE);
    private AgriInspectionOB agriInspectionOB;
    private final static Logger log = Logger.getLogger(AgriInspectionUI.class);
    private int selectedSubLimitRow=-1;
    private boolean tblSubLimitSelected=false;
    private int selectedInspectionRow=-1;
    private boolean tblInspectionSelected=false;
    private String viewType = "";
    
    /** Creates new form BeanForm */
    public AgriInspectionUI() {
        initComponents();
        setFieldNames();
        setObservable();
        initComponentData();
        setMaxLength();
        internationalize();
        setEnableDisable(false);
        panSanctionDetails_Upper.setVisible(false);
        panTable2_SubLimit.setVisible(false);
        //         new MandatoryCheck().putMandatoryMarks(getClass().getName(), panSanctionDetails_Sanction);
    }
    private void setObservable(){
        agriInspectionOB=new AgriInspectionOB();
        agriInspectionOB.addObserver(this);
    }
    private void initComponentData(){
        cboInspectionDetails.setModel(agriInspectionOB.getCbmInspectionDetails());
        cboTypeOfInspection.setModel(agriInspectionOB.getCbmTypeOfInspection());
        tblInspectionDetails.setModel(agriInspectionOB.getTblInspectionDetails());
        tblSubLimit.setModel(agriInspectionOB.getTblSubLimit());
    }
    private void  setFieldNames(){
        lblSubLimitAmt.setName("lblSubLimitAmt");
        txtSubLimitAmt.setName("txtSubLimitAmt");
        lblSubLimitFromDt.setName("lblSubLimitFromDt");
        tdtSubLimitFromDt.setName("tdtSubLimitFromDt");
        lblSubLimitToDt.setName("lblSubLimitToDt");
        tdtSubLimitToDt.setName("tdtSubLimitToDt");
        lblTypeOfInspection.setName("lblTypeOfInspection");
        cboTypeOfInspection.setName("cboTypeOfInspection");
        lblDateOfInspection.setName("lblDateOfInspection");
        tdtDateOfInspection.setName("tdtDateOfInspection");
        lblInspectionDetails.setName("lblInspectionDetails");
        cboInspectionDetails.setName("cboInspectionDetails");
        lblInspectPosition.setName("lblInspectPosition");
        lblInspectPositions.setName("lblInspectPositions");
        lblInspectBy.setName("lblInspectBy");
        txtInspectBy.setName("txtInspectBy");
        lblInspectName.setName("lblInspectName");
        btnInspectBy.setName("btnInspectBy");
        lblInsepectionObservation.setName("lblInsepectionObservation");
        textAreaInspectObservation.setName("textAreaInspectObservation");
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
        panTableFields_Inspection.setName("panTableFields_Inspection");
        panTable_InspectionDetails.setName("panTable_InspectionDetails");
        panSanctionDetails_Sanction.setName("panSanctionDetails_Sanction");
        panButton2_SD2.setName("panButton2_SD2");
        panButton2.setName("panButton2");
        scrollPaneObservation.setName("scrollPaneObservation");
    }
    private void setEnableDisable(boolean val){
        ClientUtil.enableDisable(panSanctionDetails_Upper,val);
        ClientUtil.enableDisable(panSanctionDetails_Sanction,val);
        //        ClientUtil.enableDisable(panTable2_SubLimit,val);
        ClientUtil.enableDisable(panButton2_SD2,val);
        ClientUtil.enableDisable(panTableFields_Inspection,val);
        //        ClientUtil.enableDisable(panTable_InspectionDetails,val);
        ClientUtil.enableDisable(panSanctionDetails_Table1,val);
        enableDisableSubLimitButton(val);
        enableDisableInspectionButton(val);
        btnInspectBy.setEnabled(val);
        
    }
    private void setMaxLength(){
        txtSubLimitAmt.setValidation(new CurrencyValidation());
        //        tdtSubLimitFromDt..setValidation(new ToDateValidation());
        
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
        //        txtSubLimitAmt.setText(resourceBundle.getString("txtSubLimitAmt"));
        lblSubLimitToDt.setText(resourceBundle.getString("lblSubLimitToDt"));
        lblTypeOfInspection.setText(resourceBundle.getString("lblTypeOfInspection"));
        lblDateOfInspection.setText(resourceBundle.getString("lblDateOfInspection"));
        lblInspectionDetails.setText(resourceBundle.getString("lblInspectionDetails"));
        lblInspectPosition.setText(resourceBundle.getString("lblInspectPosition"));
//        lblInspectPositions.setText(resourceBundle.getString("lblInspectPositions"));
        lblInspectBy.setText(resourceBundle.getString("lblInspectBy"));
        //        txtInspectBy.setText(resourceBundle.getString("txtInspectBy"));
//        btnInspectBy.setText(resourceBundle.getString("btnInspectBy"));
        lblInsepectionObservation.setText(resourceBundle.getString("lblInsepectionObservation"));
        //        textAreaInspectObservation.setText(resourceBundle.getString("textAreaInspectObservation"));
        btnInspectionNew.setText(resourceBundle.getString("btnInspectionNew"));
        btnInspectionSave.setText(resourceBundle.getString("btnInspectionSave"));
        btnInspectionDelete.setText(resourceBundle.getString("btnInspectionDelete"));
        btnsubLimitNew.setText(resourceBundle.getString("btnsubLimitNew"));
        btnSubLimitSave.setText(resourceBundle.getString("btnSubLimitSave"));
        btnSubLimitDelete.setText(resourceBundle.getString("btnSubLimitDelete"));
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
        lblTypeOfInspection = new com.see.truetransact.uicomponent.CLabel();
        cboTypeOfInspection = new com.see.truetransact.uicomponent.CComboBox();
        lblInspectBy = new com.see.truetransact.uicomponent.CLabel();
        txtInspectBy = new com.see.truetransact.uicomponent.CTextField();
        lblDateOfInspection = new com.see.truetransact.uicomponent.CLabel();
        tdtDateOfInspection = new com.see.truetransact.uicomponent.CDateField();
        panButton2 = new com.see.truetransact.uicomponent.CPanel();
        btnInspectionNew = new com.see.truetransact.uicomponent.CButton();
        btnInspectionSave = new com.see.truetransact.uicomponent.CButton();
        btnInspectionDelete = new com.see.truetransact.uicomponent.CButton();
        cboInspectionDetails = new com.see.truetransact.uicomponent.CComboBox();
        lblInspectionDetails = new com.see.truetransact.uicomponent.CLabel();
        lblInspectPosition = new com.see.truetransact.uicomponent.CLabel();
        lblInspectPositions = new com.see.truetransact.uicomponent.CLabel();
        lblInspectNames = new com.see.truetransact.uicomponent.CLabel();
        lblInspectName = new com.see.truetransact.uicomponent.CLabel();
        lblInsepectionObservation = new com.see.truetransact.uicomponent.CLabel();
        btnInspectBy = new com.see.truetransact.uicomponent.CButton();
        scrollPaneObservation = new com.see.truetransact.uicomponent.CScrollPane();
        textAreaInspectObservation = new com.see.truetransact.uicomponent.CTextArea();
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

        panSanctionDetails_Table1.setBorder(new javax.swing.border.TitledBorder("Inspection Details"));
        panSanctionDetails_Table1.setMaximumSize(new java.awt.Dimension(725, 280));
        panSanctionDetails_Table1.setMinimumSize(new java.awt.Dimension(725, 280));
        panSanctionDetails_Table1.setPreferredSize(new java.awt.Dimension(725, 280));
        panTableFields_Inspection.setLayout(new java.awt.GridBagLayout());

        panTableFields_Inspection.setMinimumSize(new java.awt.Dimension(400, 250));
        panTableFields_Inspection.setPreferredSize(new java.awt.Dimension(400, 250));
        lblTypeOfInspection.setText("Type of Inspection");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_Inspection.add(lblTypeOfInspection, gridBagConstraints);

        cboTypeOfInspection.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboTypeOfInspection.setMinimumSize(new java.awt.Dimension(100, 21));
        cboTypeOfInspection.setNextFocusableComponent(tdtDateOfInspection);
        cboTypeOfInspection.setPopupWidth(150);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_Inspection.add(cboTypeOfInspection, gridBagConstraints);

        lblInspectBy.setText("InspectBY");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_Inspection.add(lblInspectBy, gridBagConstraints);

        txtInspectBy.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_Inspection.add(txtInspectBy, gridBagConstraints);

        lblDateOfInspection.setText("DateOfInspection");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_Inspection.add(lblDateOfInspection, gridBagConstraints);

        tdtDateOfInspection.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtDateOfInspection.setNextFocusableComponent(cboInspectionDetails);
        tdtDateOfInspection.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtDateOfInspection.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtDateOfInspectionFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_Inspection.add(tdtDateOfInspection, gridBagConstraints);

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
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTableFields_Inspection.add(panButton2, gridBagConstraints);

        cboInspectionDetails.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboInspectionDetails.setMinimumSize(new java.awt.Dimension(100, 21));
        cboInspectionDetails.setNextFocusableComponent(txtInspectBy);
        cboInspectionDetails.setPopupWidth(250);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_Inspection.add(cboInspectionDetails, gridBagConstraints);

        lblInspectionDetails.setText("InspectionDetails");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_Inspection.add(lblInspectionDetails, gridBagConstraints);

        lblInspectPosition.setText("Designation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_Inspection.add(lblInspectPosition, gridBagConstraints);

        lblInspectPositions.setMaximumSize(new java.awt.Dimension(100, 15));
        lblInspectPositions.setMinimumSize(new java.awt.Dimension(100, 15));
        lblInspectPositions.setPreferredSize(new java.awt.Dimension(100, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_Inspection.add(lblInspectPositions, gridBagConstraints);

        lblInspectNames.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_Inspection.add(lblInspectNames, gridBagConstraints);

        lblInspectName.setText("LA0000000034324");
        lblInspectName.setMaximumSize(new java.awt.Dimension(100, 15));
        lblInspectName.setMinimumSize(new java.awt.Dimension(100, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_Inspection.add(lblInspectName, gridBagConstraints);

        lblInsepectionObservation.setText("InspectionObservation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTableFields_Inspection.add(lblInsepectionObservation, gridBagConstraints);

        btnInspectBy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnInspectBy.setText("cButton1");
        btnInspectBy.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnInspectBy.setMaximumSize(new java.awt.Dimension(35, 25));
        btnInspectBy.setMinimumSize(new java.awt.Dimension(35, 25));
        btnInspectBy.setNextFocusableComponent(textAreaInspectObservation);
        btnInspectBy.setPreferredSize(new java.awt.Dimension(35, 25));
        btnInspectBy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInspectByActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panTableFields_Inspection.add(btnInspectBy, gridBagConstraints);

        scrollPaneObservation.setMinimumSize(new java.awt.Dimension(254, 54));
        textAreaInspectObservation.setMinimumSize(new java.awt.Dimension(250, 50));
        textAreaInspectObservation.setNextFocusableComponent(btnInspectionSave);
        textAreaInspectObservation.setPreferredSize(new java.awt.Dimension(250, 50));
        scrollPaneObservation.setViewportView(textAreaInspectObservation);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        panTableFields_Inspection.add(scrollPaneObservation, gridBagConstraints);

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

    private void tdtDateOfInspectionFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtDateOfInspectionFocusLost
        // TODO add your handling code here:
        ClientUtil.validateLTDate(tdtDateOfInspection);
    }//GEN-LAST:event_tdtDateOfInspectionFocusLost
    
    private void tdtSubLimitFromDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtSubLimitFromDtFocusLost
        // TODO add your handling code here:
        
    }//GEN-LAST:event_tdtSubLimitFromDtFocusLost
    
    private void tblInspectionDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblInspectionDetailsMouseClicked
        // TODO add your handling code here:
        
        selectedInspectionRow=tblInspectionDetails.getSelectedRow();
        agriInspectionOB.setInspectionDetails(selectedInspectionRow,TrueTransactMain.selBranch);
        if ((agriInspectionOB.getLblStatus().equals(ClientConstants.ACTION_STATUS[3])) ||
        (agriInspectionOB.getLblStatus().equals(ClientConstants.ACTION_STATUS[8]))  ||
        (agriInspectionOB.getLblStatus().equals(ClientConstants.ACTION_STATUS[10]))){
            inspectionEnableDisable(false);
            enableDisableInspectionButton(false);
        }else{
            inspectionEnableDisable(true);
            enableDisableInspectionNewButton(true);
        }
        tblInspectionSelected=true;
//        enableDisableInspectionNewButton(true);
        
    }//GEN-LAST:event_tblInspectionDetailsMouseClicked
    
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
    public void fillData(Object obj){
        HashMap map=(HashMap)obj;
        if(viewType.equals("EMP_ID")){
            txtInspectBy.setText(CommonUtil.convertObjToStr(map.get("EMPLOYEE ID")));
            lblInspectName.setText(CommonUtil.convertObjToStr(map.get("EMP NAME")));
            lblInspectPositions.setText(CommonUtil.convertObjToStr(map.get("DESIGNATION")));
        }
    }
    private void btnInspectionDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInspectionDeleteActionPerformed
        // TODO add your handling code here:
        selectedInspectionRow=tblInspectionDetails.getSelectedRow();
        agriInspectionOB.deleteInspectionData(selectedInspectionRow);
        inspectionEnableDisable(true);
        tblInspectionSelected=true;
        resetInspection();
        resetSubLimit();
        inspectionEnableDisable(false);
        enableDisableInspectionNewButton(false);
    }//GEN-LAST:event_btnInspectionDeleteActionPerformed
    
    private void btnInspectionSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInspectionSaveActionPerformed
        // TODO add your handling code here:
        String message="";
        //        if((((ComboBoxModel)cboTypeOfInspection.getModel()).getKeyForSelected().toString().length())>0)
        //            message=CommonUtil.convertObjToStr(((com.see.tools.workflow.clientutils.ComboBoxModel)cboTypeOfInspection.getModel()).getKeyForSelected());
        if(message.length()>0){
            ClientUtil.displayAlert("Select Type Of Inspection");
            message=CommonUtil.convertObjToStr(tdtDateOfInspection.getDateValue());
            return;
        }
        message=CommonUtil.convertObjToStr(tdtDateOfInspection.getDateValue());
        if(message.length()==0){
            ClientUtil.displayAlert("Choose Inspection Date");
            return;
        }
        //        message=CommonUtil.convertObjToStr(((ComboBoxModel)cboTypeOfInspection.getModel()).getKeyForSelected());
        //        if(message.length()==0){
        //            ClientUtil.displayAlert("Choose Type of Inspection");
        //            return;
        //        }
        message=CommonUtil.convertObjToStr(txtInspectBy.getText());
        if(message.length()==0){
            ClientUtil.displayAlert("Enter Inspect By");
            return;
        }
        updateOBFields();
        agriInspectionOB.addInpection(selectedInspectionRow, tblInspectionSelected);
        resetInspection();
//        resetSubLimit();
        agriInspectionOB.resetFormComponets();
        inspectionEnableDisable(false);
        selectedInspectionRow=-1;
        enableDisableInspectionNewButton(false);
    }//GEN-LAST:event_btnInspectionSaveActionPerformed
    
    private void btnInspectionNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInspectionNewActionPerformed
        // TODO add your handling code here:
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
        tblSubLimitSelected=false;
        resetSubLimit();
//        resetInspection();
        subLimitEnableDisable(true);
        enableDisableSubLimitButton(true);
    }//GEN-LAST:event_btnsubLimitNewActionPerformed
    
    private void tblSubLimitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSubLimitMouseClicked
        // TODO add your handling code here:
        selectedSubLimitRow=tblSubLimit.getSelectedRow();
        agriInspectionOB.setSubLimitDetails(selectedSubLimitRow);
        if ((agriInspectionOB.getLblStatus().equals(ClientConstants.ACTION_STATUS[3])) ||
        (agriInspectionOB.getLblStatus().equals(ClientConstants.ACTION_STATUS[8]))  ||
        (agriInspectionOB.getLblStatus().equals(ClientConstants.ACTION_STATUS[10]))){
            subLimitEnableDisable(false);
            enableDisableSubLimitButton(false);
        }
        else{
            subLimitEnableDisable(true);
            enableDisableSubLimitNewButton(true);
        }
        tblSubLimitSelected=true;
//        enableDisableSubLimitNewButton(true);
    }//GEN-LAST:event_tblSubLimitMouseClicked
    
    private void btnSubLimitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubLimitDeleteActionPerformed
        // TODO add your handling code here:
        if(selectedSubLimitRow>=0)
            agriInspectionOB.deleteSubLimitData(selectedSubLimitRow);
        subLimitEnableDisable(false);
        resetSubLimit();
        resetInspection();
        tblSubLimitSelected=false;
        enableDisableSubLimitNewButton(false);
    }//GEN-LAST:event_btnSubLimitDeleteActionPerformed
    
    private void btnSubLimitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubLimitSaveActionPerformed
        // TODO add your handling code here:
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
        //        mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panSanctionDetails_Sanction);
        //        if(mandatoryMessage.length()>0){
        //            ClientUtil.displayAlert(String.valueOf(mandatoryMessage));
        //            return;
        //        }
        
        updateOBFields();
        if(agriInspectionOB.validateValues(selectedSubLimitRow,tblSubLimitSelected))
            return;
        agriInspectionOB.addSubLimit(selectedSubLimitRow, tblSubLimitSelected);
        
        subLimitEnableDisable(false);
        resetSubLimit();
//        resetInspection();
        agriInspectionOB.resetFormComponets();
        selectedSubLimitRow=-1;
        enableDisableSubLimitNewButton(false);
    }//GEN-LAST:event_btnSubLimitSaveActionPerformed
    private void subLimitEnableDisable(boolean val ){
        ClientUtil.enableDisable(panSanctionDetails_Sanction,val);
    }
    private void inspectionEnableDisable(boolean val){
        cboTypeOfInspection.setEnabled(val);
        tdtDateOfInspection.setEnabled(val);
        cboInspectionDetails.setEnabled(val);
        txtInspectBy.setEnabled(val);
        textAreaInspectObservation.setEnabled(val);
        btnInspectBy.setEnabled(val);
    }
    private void resetSubLimit(){
        txtSubLimitAmt.setText("");
        tdtSubLimitFromDt.setDateValue("");
        tdtSubLimitToDt.setDateValue("");
    }
    private void resetInspection(){
        cboTypeOfInspection.setSelectedItem("");
        tdtDateOfInspection.setDateValue("");
        cboInspectionDetails.setSelectedItem("");
        txtInspectBy.setText("");
        textAreaInspectObservation.setText("");
        lblInspectName.setText("");
        lblInspectPositions.setText("");
    }
    public java.util.HashMap getMandatoryHashMap() {
        HashMap mandatoryMap=new HashMap();
        return mandatoryMap;
    }
    
    public void setMandatoryHashMap() {
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnInspectBy;
    private com.see.truetransact.uicomponent.CButton btnInspectionDelete;
    private com.see.truetransact.uicomponent.CButton btnInspectionNew;
    private com.see.truetransact.uicomponent.CButton btnInspectionSave;
    private com.see.truetransact.uicomponent.CButton btnSubLimitDelete;
    private com.see.truetransact.uicomponent.CButton btnSubLimitSave;
    private com.see.truetransact.uicomponent.CButton btnsubLimitNew;
    private com.see.truetransact.uicomponent.CComboBox cboInspectionDetails;
    private com.see.truetransact.uicomponent.CComboBox cboTypeOfInspection;
    private com.see.truetransact.uicomponent.CLabel lblDateOfInspection;
    private com.see.truetransact.uicomponent.CLabel lblInsepectionObservation;
    private com.see.truetransact.uicomponent.CLabel lblInspectBy;
    private com.see.truetransact.uicomponent.CLabel lblInspectName;
    private com.see.truetransact.uicomponent.CLabel lblInspectNames;
    private com.see.truetransact.uicomponent.CLabel lblInspectPosition;
    private com.see.truetransact.uicomponent.CLabel lblInspectPositions;
    private com.see.truetransact.uicomponent.CLabel lblInspectionDetails;
    private com.see.truetransact.uicomponent.CLabel lblSubLimitAmt;
    private com.see.truetransact.uicomponent.CLabel lblSubLimitFromDt;
    private com.see.truetransact.uicomponent.CLabel lblSubLimitToDt;
    private com.see.truetransact.uicomponent.CLabel lblTypeOfInspection;
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
    private com.see.truetransact.uicomponent.CDateField tdtDateOfInspection;
    private com.see.truetransact.uicomponent.CDateField tdtSubLimitFromDt;
    private com.see.truetransact.uicomponent.CDateField tdtSubLimitToDt;
    private com.see.truetransact.uicomponent.CTextArea textAreaInspectObservation;
    private com.see.truetransact.uicomponent.CTextField txtInspectBy;
    private com.see.truetransact.uicomponent.CTextField txtSubLimitAmt;
    // End of variables declaration//GEN-END:variables
    public static void main(String str[]){
        new AgriInspectionUI().show();
    }
    
    public void update(java.util.Observable o, Object arg) {
        txtSubLimitAmt.setText(agriInspectionOB.getSubLimitAmt());
        tdtSubLimitFromDt.setDateValue(agriInspectionOB.getSubLimitFromDt());
        tdtSubLimitToDt.setDateValue(agriInspectionOB.getSubLimitToDt());
        ((com.see.truetransact.clientutil.ComboBoxModel)cboTypeOfInspection.getModel()).setKeyForSelected(agriInspectionOB.getCboTypeOfInspection());
        tdtDateOfInspection.setDateValue(agriInspectionOB.getDateOfInspection());
        ((com.see.truetransact.clientutil.ComboBoxModel)cboInspectionDetails.getModel()).setKeyForSelected(agriInspectionOB.getCboInspectionDetails());
        txtInspectBy.setText(agriInspectionOB.getInspectBy());
        textAreaInspectObservation.setText(agriInspectionOB.getInspectObservation());
    }
    public void updateOBFields(){
        agriInspectionOB.setSubLimitAmt(txtSubLimitAmt.getText());
        agriInspectionOB.setSubLimitFromDt(tdtSubLimitFromDt.getDateValue());
        agriInspectionOB.setSubLimitToDt(tdtSubLimitToDt.getDateValue());
        
        agriInspectionOB.setCboTypeOfInspection(CommonUtil.convertObjToStr(cboTypeOfInspection.getSelectedItem()));
        agriInspectionOB.setCboInspectionDetails(CommonUtil.convertObjToStr(cboInspectionDetails.getSelectedItem()));
        agriInspectionOB.setDateOfInspection(CommonUtil.convertObjToStr(tdtDateOfInspection.getDateValue()));
        agriInspectionOB.setInspectBy(CommonUtil.convertObjToStr(txtInspectBy.getText()));
        agriInspectionOB.setInspectObservation(CommonUtil.convertObjToStr(textAreaInspectObservation.getText()));
    }
    /**
     * Getter for property agriInspectionOB.
     * @return Value of property agriInspectionOB.
     */
    public com.see.truetransact.ui.termloan.agritermloan.agriinspectiondetails.AgriInspectionOB getagriInspectionOB() {
        return agriInspectionOB;
    }
    
    /**
     * Setter for property agriInspectionOB.
     * @param agriInspectionOB New value of property agriInspectionOB.
     */
    public void setagriInspectionOB(com.see.truetransact.ui.termloan.agritermloan.agriinspectiondetails.AgriInspectionOB agriInspectionOB) {
        this.agriInspectionOB = agriInspectionOB;
    }
    
}
