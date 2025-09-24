/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * LoanDisbursementUI.java
 *
 * Created on April 28, 2009, 5:02 PM
 */

package com.see.truetransact.ui.termloan.loandisbursement;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.uivalidation.PercentageValidation;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientUtil;
//import com.see.tools.workflow.clientutils.ComboBoxModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uimandatory.MandatoryDBCheck;
import com.see.truetransact.uicomponent.CButtonGroup;
import com.see.truetransact.clientutil.ComboBoxModel;
import org.apache.log4j.Logger;
import java.util.HashMap;


/**
 *
 * @author  Administrator
 */
public class LoanDisbursementUI extends com.see.truetransact.uicomponent.CPanel  implements java.util.Observer,UIMandatoryField{
    java.util.ResourceBundle resourceBundle= java.util.ResourceBundle.getBundle("com.see.truetransact.ui.termloan.loandisbursement.LoanDisbursementRB",ProxyParameters.LANGUAGE);
    private LoanDisbursementOB agriSubLimitOB;
    private final static Logger log = Logger.getLogger(LoanDisbursementUI.class);
    private int selectedSubLimitRow=-1;
    private int selectedInspectionRow=-1;
    private boolean tblInspectionSelected=false;
    private String viewType = "";
    private HashMap mandatoryMap=null;
   
    
    /** Creates new form BeanForm */
    public LoanDisbursementUI() {
        initComponents();
        setFieldNames();
        setObservable();
        initComponentData();
        setMaxLength();
        internationalize();
        setEnableDisable(false);
        setMandatoryHashMap();
//        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panTableFields_Inspection);

        //         new MandatoryCheck().putMandatoryMarks(getClass().getName(), panSanctionDetails_Sanction);
    }
   
   
    private void setObservable(){
        agriSubLimitOB=new LoanDisbursementOB();
        agriSubLimitOB.addObserver(this);
    }
    private void initComponentData(){
        cboDisbursementStage.setModel(agriSubLimitOB.getCbmType());
//        cboType.setModel(agriSubLimitOB.getCbmType());
//        tblInspectionDetails.setModel(agriSubLimitOB.getTblInspectionDetails());
        tblSubLimit.setModel(agriSubLimitOB.getTblSubLimit());
    }
    private void  setFieldNames(){
        lblDisbursementDt.setName("lblDisbursementDt");
        lblDisbursementAmt.setName("lblDisbursementAmt");
        lblDisbursementStage.setName("lblDisbursementStage");
        lblRemarks.setName("lblRemarks");
        tdtDisbursementDt.setName("tdtDisbursementDt");
        cboDisbursementStage.setName("cboDisbursementStage");
        rdoDisbursementAmtRs.setName("rdoDisbursementAmtRs");
        rdoDisbursementAmt.setName("rdoDisbursementAmt");
        txtDisbursementAmtRs.setName("txtDisbursementAmtRs");
        txtDisbursementAmt.setName("txtDisbursementAmt");
        txtRemarks.setName("txtRemarks");
        
        btnsubLimitNew.setName("btnsubLimitNew");
        btnSubLimitSave.setName("btnSubLimitSave");
        btnSubLimitDelete.setName("btnSubLimitDelete");
        panTable2_SubLimit.setName("panTable2_SubLimit");
        panSanctionDetails_Upper.setName("panSanctionDetails_Upper");
        panTable2_SubLimit.setName("panTable2_SubLimit");
        panSanctionDetails_Sanction.setName("panSanctionDetails_Sanction");
//        panButton2_SD2.setName("panButton2_SD2");
//        panButton2.setName("panButton2");
//        scrollPaneObservation.setName("scrollPaneObservation");
    }
    private void setEnableDisable(boolean val){
        ClientUtil.enableDisable(panSanctionDetails_Upper,val);
        ClientUtil.enableDisable(panSanctionDetails_Sanction,val);
//        ClientUtil.enableDisable(panButton2_SD2,val);
//        ClientUtil.enableDisable(panTableFields_Inspection,val);
//        ClientUtil.enableDisable(panSanctionDetails_Table1,val);
        enableDisableSubLimitButton(val);
        enableDisableInspectionButton(val);
        
    }
    private void setMaxLength(){
        txtDisbursementAmtRs.setValidation(new CurrencyValidation());
        txtDisbursementAmt.setValidation(new PercentageValidation());
//        txtSurveyNo.setAllowAll(true);
        
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
//        btnInspectionNew.setEnabled(val);
//        btnInspectionSave.setEnabled(val);
//        btnInspectionDelete.setEnabled(val);
    }
    public  void enableDisableInspectionNewButton(boolean val){
//        btnInspectionNew.setEnabled(!val);
//        btnInspectionSave.setEnabled(val);
//        btnInspectionDelete.setEnabled(val);
    }
    
    
    private void internationalize(){
        lblDisbursementDt.setText(resourceBundle.getString("lblDisbursementDt"));
        lblDisbursementAmt.setText(resourceBundle.getString("lblDisbursementAmt"));
        lblDisbursementStage.setText(resourceBundle.getString("lblDisbursementStage"));
        lblRemarks.setText(resourceBundle.getString("lblRemarks"));
//        btnInspectionDelete.setText(resourceBundle.getString("btnInspectionDelete"));
        btnsubLimitNew.setText(resourceBundle.getString("btnsubLimitNew"));
        btnSubLimitSave.setText(resourceBundle.getString("btnSubLimitSave"));
        btnSubLimitDelete.setText(resourceBundle.getString("btnSubLimitDelete"));
//        lblHectare.setText(resourceBundle.getString("lblHectare"));
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        DisburseAmtGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        panSublimitDetails = new com.see.truetransact.uicomponent.CPanel();
        panSanctionDetails_Upper = new com.see.truetransact.uicomponent.CPanel();
        panButton2_SD3 = new com.see.truetransact.uicomponent.CPanel();
        btnsubLimitNew = new com.see.truetransact.uicomponent.CButton();
        btnSubLimitSave = new com.see.truetransact.uicomponent.CButton();
        btnSubLimitDelete = new com.see.truetransact.uicomponent.CButton();
        panSanctionDetails_Sanction = new com.see.truetransact.uicomponent.CPanel();
        lblDisbursementAmt = new com.see.truetransact.uicomponent.CLabel();
        tdtDisbursementDt = new com.see.truetransact.uicomponent.CDateField();
        lblDisbursementDt = new com.see.truetransact.uicomponent.CLabel();
        cPanel3 = new com.see.truetransact.uicomponent.CPanel();
        rdoDisbursementAmt = new com.see.truetransact.uicomponent.CRadioButton();
        txtDisbursementAmtRs = new com.see.truetransact.uicomponent.CTextField();
        rdoDisbursementAmtRs = new com.see.truetransact.uicomponent.CRadioButton();
        txtDisbursementAmt = new com.see.truetransact.uicomponent.CTextField();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        lblDisbursementStage = new com.see.truetransact.uicomponent.CLabel();
        cboDisbursementStage = new com.see.truetransact.uicomponent.CComboBox();
        txtRemarks = new com.see.truetransact.uicomponent.CTextField();
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

        panSanctionDetails_Upper.setMaximumSize(new java.awt.Dimension(470, 375));
        panSanctionDetails_Upper.setMinimumSize(new java.awt.Dimension(470, 375));
        panSanctionDetails_Upper.setPreferredSize(new java.awt.Dimension(470, 375));
        panButton2_SD3.setLayout(new java.awt.GridBagLayout());

        panButton2_SD3.setMinimumSize(new java.awt.Dimension(215, 33));
        panButton2_SD3.setPreferredSize(new java.awt.Dimension(215, 33));
        btnsubLimitNew.setText("New");
        btnsubLimitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsubLimitNewActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton2_SD3.add(btnsubLimitNew, gridBagConstraints);

        btnSubLimitSave.setText("Save");
        btnSubLimitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubLimitSaveActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton2_SD3.add(btnSubLimitSave, gridBagConstraints);

        btnSubLimitDelete.setText("Delete");
        btnSubLimitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubLimitDeleteActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton2_SD3.add(btnSubLimitDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSanctionDetails_Upper.add(panButton2_SD3, gridBagConstraints);

        panSanctionDetails_Sanction.setLayout(new java.awt.GridBagLayout());

        panSanctionDetails_Sanction.setMaximumSize(new java.awt.Dimension(200, 40));
        panSanctionDetails_Sanction.setMinimumSize(new java.awt.Dimension(200, 40));
        panSanctionDetails_Sanction.setPreferredSize(new java.awt.Dimension(200, 40));
        lblDisbursementAmt.setText("DisburstAmt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panSanctionDetails_Sanction.add(lblDisbursementAmt, gridBagConstraints);

        tdtDisbursementDt.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtDisbursementDt.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtDisbursementDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtDisbursementDtFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSanctionDetails_Sanction.add(tdtDisbursementDt, gridBagConstraints);

        lblDisbursementDt.setText("Disburst Dt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panSanctionDetails_Sanction.add(lblDisbursementDt, gridBagConstraints);

        cPanel3.setLayout(new java.awt.GridBagLayout());

        cPanel3.setMaximumSize(new java.awt.Dimension(350, 50));
        cPanel3.setMinimumSize(new java.awt.Dimension(350, 50));
        cPanel3.setPreferredSize(new java.awt.Dimension(350, 50));
        rdoDisbursementAmt.setText("% Loan Amt");
        DisburseAmtGroup.add(rdoDisbursementAmt);
        rdoDisbursementAmt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDisbursementAmtActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 9, 0, 0);
        cPanel3.add(rdoDisbursementAmt, gridBagConstraints);

        txtDisbursementAmtRs.setMaximumSize(new java.awt.Dimension(100, 21));
        txtDisbursementAmtRs.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDisbursementAmtRs.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDisbursementAmtRsFocusLost(evt);
            }
        });
        txtDisbursementAmtRs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDisbursementAmtRsActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 7);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        cPanel3.add(txtDisbursementAmtRs, gridBagConstraints);

        rdoDisbursementAmtRs.setText(" Amt Rs");
        DisburseAmtGroup.add(rdoDisbursementAmtRs);
        rdoDisbursementAmtRs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDisbursementAmtRsActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        cPanel3.add(rdoDisbursementAmtRs, gridBagConstraints);

        txtDisbursementAmt.setMaximumSize(new java.awt.Dimension(50, 21));
        txtDisbursementAmt.setMinimumSize(new java.awt.Dimension(50, 21));
        txtDisbursementAmt.setPreferredSize(new java.awt.Dimension(50, 21));
        txtDisbursementAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDisbursementAmtFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 2);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        cPanel3.add(txtDisbursementAmt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSanctionDetails_Sanction.add(cPanel3, gridBagConstraints);

        lblRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panSanctionDetails_Sanction.add(lblRemarks, gridBagConstraints);

        lblDisbursementStage.setText("DisburstStage");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panSanctionDetails_Sanction.add(lblDisbursementStage, gridBagConstraints);

        cboDisbursementStage.setMaximumSize(new java.awt.Dimension(100, 21));
        cboDisbursementStage.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSanctionDetails_Sanction.add(cboDisbursementStage, gridBagConstraints);

        txtRemarks.setNextFocusableComponent(tdtDisbursementDt);
        txtRemarks.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSanctionDetails_Sanction.add(txtRemarks, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panSanctionDetails_Upper.add(panSanctionDetails_Sanction, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSublimitDetails.add(panSanctionDetails_Upper, gridBagConstraints);

        panTable2_SubLimit.setLayout(new java.awt.GridBagLayout());

        panTable2_SubLimit.setMaximumSize(new java.awt.Dimension(400, 350));
        panTable2_SubLimit.setMinimumSize(new java.awt.Dimension(400, 350));
        panTable2_SubLimit.setPreferredSize(new java.awt.Dimension(400, 350));
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
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSublimitDetails.add(panTable2_SubLimit, gridBagConstraints);

        add(panSublimitDetails, new java.awt.GridBagConstraints());

    }//GEN-END:initComponents

    private void txtDisbursementAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDisbursementAmtFocusLost
        // TODO add your handling code here:
          agriSubLimitOB.validateValues(-1, true,false ,true);
    }//GEN-LAST:event_txtDisbursementAmtFocusLost

    private void txtDisbursementAmtRsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDisbursementAmtRsFocusLost
        // TODO add your handling code here:
        updateOBFields();
        agriSubLimitOB.validateValues(-1, true,true ,false);
    }//GEN-LAST:event_txtDisbursementAmtRsFocusLost

    private void txtDisbursementAmtRsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDisbursementAmtRsActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_txtDisbursementAmtRsActionPerformed

    private void rdoDisbursementAmtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDisbursementAmtActionPerformed
        // TODO add your handling code here:
        rdoDisbursementAmtActionPerformed();
    }//GEN-LAST:event_rdoDisbursementAmtActionPerformed
    private void rdoDisbursementAmtActionPerformed(){
       if(rdoDisbursementAmt.isSelected()){
           txtDisbursementAmtRs.setEnabled(false);
           txtDisbursementAmt.setEnabled(true);
       }
       else{
           txtDisbursementAmtRs.setEnabled(true);
           txtDisbursementAmt.setEnabled(false);
       }
               
    }
    private void rdoDisbursementAmtRsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDisbursementAmtRsActionPerformed
        // TODO add your handling code here:
       rdoDisbursementAmtRsActionPerformed();
        
    }//GEN-LAST:event_rdoDisbursementAmtRsActionPerformed
    private void rdoDisbursementAmtRsActionPerformed(){
        if(rdoDisbursementAmtRs.isSelected()){
            txtDisbursementAmt.setEnabled(false);
            txtDisbursementAmtRs.setEnabled(true);
        }
        else{
            txtDisbursementAmt.setEnabled(true);
            txtDisbursementAmtRs.setEnabled(false);
        }

    }
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

    private void btnSubLimitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubLimitDeleteActionPerformed
        // TODO add your handling code here:
                // TODO add your handling code here:
//        if(tblInspectionDetails.getRowCount()>0){
//            ClientUtil.showMessageWindow("First Delete SubLimit Details");
//            return;
//        }
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
             // TODO add your handling code here:
//         final String mandatoryMessages = new MandatoryDBCheck().checkMandatory(getClass().getName(), panSanctionDetails_Sanction);
//        String mandatoryMessage="";
////        mandatoryMessage=CommonUtil.convertObjToStr(txtSubLimitAmt.getText());
//        if(mandatoryMessage.equals("")){
//            ClientUtil.displayAlert("Enter Sanction Amount");
//            return;
//        }
////        mandatoryMessage=CommonUtil.convertObjToStr(tdtSubLimitFromDt.getDateValue());
//        if(mandatoryMessage.equals("")){
//            ClientUtil.displayAlert("Enter Sanction From Date");
//            return;
//        }
//        
////        mandatoryMessage=CommonUtil.convertObjToStr(tdtSubLimitToDt.getDateValue());
//        if(mandatoryMessage.equals("")){
//            ClientUtil.displayAlert("Enter Sanction To Date");
//            return;
//        }
        
        updateOBFields();
        if(agriSubLimitOB.validateValues(selectedSubLimitRow, agriSubLimitOB.isTblSubLimitSelected(),false,true))
            return;
        agriSubLimitOB.addSubLimit(selectedSubLimitRow, agriSubLimitOB.isTblSubLimitSelected());
        
        subLimitEnableDisable(false);
        resetSubLimit();
//        resetInspection();
       
//        if(agriSubLimitOB.isTblSubLimitSelected()==false)
//            ClientUtil.showMessageWindow("Atleast One Record Enter in SubLimit Details");
        agriSubLimitOB.setTblSubLimitSelected(false);
         agriSubLimitOB.resetFormComponets();
        agriSubLimitOB.resetFormComponetsSubLimit();
        selectedSubLimitRow=-1;
        agriSubLimitOB.setSelectedSubLimitRow(this.selectedSubLimitRow);
        enableDisableSubLimitNewButton(false);
        
   
    }//GEN-LAST:event_btnSubLimitSaveActionPerformed
    
    private void tdtDisbursementDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtDisbursementDtFocusLost
        // TODO add your handling code here:
        String subLimitFromDt=CommonUtil.convertObjToStr(tdtDisbursementDt.getDateValue());
        if(subLimitFromDt.length()>0)
        ClientUtil.validateToDate(tdtDisbursementDt,DateUtil.getStringDate(ClientUtil.getCurrentDate()));
    }//GEN-LAST:event_tdtDisbursementDtFocusLost
            public void fillData(Object obj){
        HashMap map=(HashMap)obj;
        if(viewType.equals("EMP_ID")){
//            txtInspectBy.setText(CommonUtil.convertObjToStr(map.get("EMPLOYEE ID")));
//            lblInspectName.setText(CommonUtil.convertObjToStr(map.get("EMP NAME")));
//            lblInspectPositions.setText(CommonUtil.convertObjToStr(map.get("DESIGNATION")));
        }
    }                
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
            rdoDisbursementAmtRsActionPerformed();
            rdoDisbursementAmtActionPerformed();
        }
        agriSubLimitOB.setTblSubLimitSelected(true);
//        enableDisableSubLimitNewButton(true);
    }//GEN-LAST:event_tblSubLimitMouseClicked
            private void subLimitEnableDisable(boolean val ){
        ClientUtil.enableDisable(panSanctionDetails_Sanction,val);
    }
    private void inspectionEnableDisable(boolean val){
//        cboType.setEnabled(val);
//        cboPurpose.setEnabled(val);
//        txtHectare.setEnabled(val);
//        txtSurveyNo.setEnabled(val);
//        textAreaRemarks.setEnabled(val);
    }
    private void resetSubLimit(){
        tdtDisbursementDt.setDateValue("");
        cboDisbursementStage.setSelectedItem("");
        rdoDisbursementAmtRs.setSelected(false);
        rdoDisbursementAmt.setSelected(false);
        txtDisbursementAmtRs.setText("");
        txtDisbursementAmt.setText("");
        txtRemarks.setText("");  
    }
    private void resetInspection(){
//        cboType.setSelectedItem("");
//        cboPurpose.setSelectedItem("");
//        txtHectare.setText("");
//        txtSurveyNo.setText("");
//        textAreaRemarks.setText("");
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
    private com.see.truetransact.uicomponent.CButtonGroup DisburseAmtGroup;
    private com.see.truetransact.uicomponent.CButton btnSubLimitDelete;
    private com.see.truetransact.uicomponent.CButton btnSubLimitSave;
    private com.see.truetransact.uicomponent.CButton btnsubLimitNew;
    private com.see.truetransact.uicomponent.CPanel cPanel3;
    private com.see.truetransact.uicomponent.CComboBox cboDisbursementStage;
    private com.see.truetransact.uicomponent.CLabel lblDisbursementAmt;
    private com.see.truetransact.uicomponent.CLabel lblDisbursementDt;
    private com.see.truetransact.uicomponent.CLabel lblDisbursementStage;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
    private com.see.truetransact.uicomponent.CPanel panButton2_SD3;
    private com.see.truetransact.uicomponent.CPanel panSanctionDetails_Sanction;
    private com.see.truetransact.uicomponent.CPanel panSanctionDetails_Upper;
    private com.see.truetransact.uicomponent.CPanel panSublimitDetails;
    private com.see.truetransact.uicomponent.CPanel panTable2_SubLimit;
    private com.see.truetransact.uicomponent.CRadioButton rdoDisbursementAmt;
    private com.see.truetransact.uicomponent.CRadioButton rdoDisbursementAmtRs;
    private com.see.truetransact.uicomponent.CScrollPane srpTable_SubLimit;
    private com.see.truetransact.uicomponent.CTable tblSubLimit;
    private com.see.truetransact.uicomponent.CDateField tdtDisbursementDt;
    private com.see.truetransact.uicomponent.CTextField txtDisbursementAmt;
    private com.see.truetransact.uicomponent.CTextField txtDisbursementAmtRs;
    private com.see.truetransact.uicomponent.CTextField txtRemarks;
    // End of variables declaration//GEN-END:variables
    public static void main(String str[]){
        new LoanDisbursementUI().show();
    }
    private void removeRadioButton(){
        DisburseAmtGroup.remove(rdoDisbursementAmtRs);
        DisburseAmtGroup.remove(rdoDisbursementAmt);
    }
      private void addRadioButton(){
          DisburseAmtGroup=new CButtonGroup();
        DisburseAmtGroup.add(rdoDisbursementAmtRs);
        DisburseAmtGroup.add(rdoDisbursementAmt);
    }
    public void update(java.util.Observable o, Object arg) {
        removeRadioButton();
        tdtDisbursementDt.setDateValue(agriSubLimitOB.getTdtDisbursementDt());
        ((com.see.truetransact.clientutil.ComboBoxModel)cboDisbursementStage.getModel()).setKeyForSelected(agriSubLimitOB.getCboDisbursementStage());

        rdoDisbursementAmtRs.setSelected(agriSubLimitOB.isRdoDisbursementAmtRs());
        rdoDisbursementAmt.setSelected(agriSubLimitOB.isRdoDisbursementAmt());
        txtDisbursementAmtRs.setText(agriSubLimitOB.getTxtDisbursementAmtRs());
        txtDisbursementAmt.setText(agriSubLimitOB.getTxtDisbursementAmt());
        txtRemarks.setText(agriSubLimitOB.getRemarks());
         addRadioButton();
//        txtSubLimitAmt.setText(agriSubLimitOB.getSubLimitAmt());
//        tdtSubLimitFromDt.setDateValue(agriSubLimitOB.getSubLimitFromDt());
//        tdtSubLimitToDt.setDateValue(agriSubLimitOB.getSubLimitToDt());
//        ((com.see.truetransact.clientutil.ComboBoxModel)cboPurpose.getModel()).setKeyForSelected(agriSubLimitOB.getCboPurpose());
//        ((com.see.truetransact.clientutil.ComboBoxModel)cboType.getModel()).setKeyForSelected(agriSubLimitOB.getCboType());
//        txtHectare.setText(agriSubLimitOB.getHectare());
//        textAreaRemarks.setText(agriSubLimitOB.getRemarks());
//        txtSurveyNo.setText(agriSubLimitOB.getSurveyno());
    }
    public void updateOBFields(){
        
        agriSubLimitOB.setTdtDisbursementDt(tdtDisbursementDt.getDateValue());
        agriSubLimitOB.setCboDisbursementStage(CommonUtil.convertObjToStr(cboDisbursementStage.getSelectedItem()));
        agriSubLimitOB.setRdoDisbursementAmtRs(rdoDisbursementAmtRs.isSelected());
        agriSubLimitOB.setRdoDisbursementAmt(rdoDisbursementAmt.isSelected());
        agriSubLimitOB.setTxtDisbursementAmtRs(txtDisbursementAmtRs.getText());
        agriSubLimitOB.setTxtDisbursementAmt(txtDisbursementAmt.getText());
        agriSubLimitOB.setRemarks(CommonUtil.convertObjToStr(txtRemarks.getText()));
        
//        agriSubLimitOB.setCboPurpose(CommonUtil.convertObjToStr(cboPurpose.getSelectedItem()));
//        agriSubLimitOB.setHectare(CommonUtil.convertObjToStr(txtHectare.getText()));
//        agriSubLimitOB.setSurveyno(CommonUtil.convertObjToStr(txtSurveyNo.getText()));
//         agriSubLimitOB.setRemarks(CommonUtil.convertObjToStr(textAreaRemarks.getText()));
    }
    /**
     * Getter for property agriSubLimitOB.
     * @return Value of property agriSubLimitOB.
     */
    public com.see.truetransact.ui.termloan.loandisbursement.LoanDisbursementOB getAgriSubLimitOB() {
        return agriSubLimitOB;
    }
    
    /**
     * Setter for property agriSubLimitOB.
     * @param agriSubLimitOB New value of property agriSubLimitOB.
     */
    public void setAgriSubLimitOB(com.see.truetransact.ui.termloan.loandisbursement.LoanDisbursementOB agriSubLimitOB) {
        this.agriSubLimitOB = agriSubLimitOB;
    }
    
   
    
}
