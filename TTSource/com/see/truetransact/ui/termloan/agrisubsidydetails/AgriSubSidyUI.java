/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AgriSubSidyUI.java
 *
 * Created on April 28, 2009, 5:02 PM
 */

package com.see.truetransact.ui.termloan.agrisubsidydetails;
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
    java.util.ResourceBundle resourceBundle= java.util.ResourceBundle.getBundle("com.see.truetransact.ui.termloan.agrisubsidydetails.AgriSubSidyRB",ProxyParameters.LANGUAGE);
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
    
        //         new MandatoryCheck().putMandatoryMarks(getClass().getName(), panValutionDetails);
    }
    private void setObservable(){
        agriSubSidyOB= new AgriSubSidyOB();
        agriSubSidyOB.addObserver(this);
    }
    private void initComponentData(){
        cboDisbursement_mode.setModel(agriSubSidyOB.getCbmDisbursement_mode());
        cboSource_Code.setModel(agriSubSidyOB.getCbmSource_Code());
    }
    private void  setFieldNames(){
        
        lblSolicitor_Name.setName("lblSolicitor_Name");
        lblshowSolicitor_Name.setName("lblshowSolicitor_Name");
        lblValuerName.setName("lblValuerName");
        lblshowValuerName.setName("lblshowValuerName");
        lblDeveloper_Name.setName("lblDeveloper_Name");
        lblshowDeveloper_Name.setName("lblshowDeveloper_Name");
        lblProject_Name.setName("lblProject_Name");
        lblshowProject_Name.setName("lblshowProject_Name");
        lblReferalCode.setName("lblReferalCode");
        txtReferalCode.setName("txtReferalCode");
        lblDisbursement_mode.setName("lblDisbursement_mode");
        cboDisbursement_mode.setName("cboDisbursement_mode");
        lblSource_Code.setName("lblSource_Code");
        cboSource_Code.setName("cboSource_Code");
        panSublimitDetails.setName("panSublimitDetails");

    }
    private void setEnableDisable(boolean val){
        ClientUtil.enableDisable(panSublimitDetails,val);
        
    }
    private void setMaxLength(){
//        txtSubSidyAmt.setValidation(new CurrencyValidation());
//        txtValutionAmt.setValidation(new CurrencyValidation());
//        txtOutStandingAmt.setValidation(new CurrencyValidation());
//        txtAmtAdjusted.setValidation(new  CurrencyValidation());
//        txtAmtRefunded.setValidation(new CurrencyValidation());
    }
    public void enableDisableValutionButton(boolean val){
//        btnValutiontNew.setEnabled(val);
//        btnValutionSave.setEnabled(val);
//        btnValutionDelete.setEnabled(val);
    }
    public void enableDisableValutionNewButton(boolean val){
//        btnValutiontNew.setEnabled(!val);
//        btnValutionSave.setEnabled(val);
//        btnValutionDelete.setEnabled(val);
    }
    
    public void enableDisableSubsidyButton(boolean val){
//        btnSubSidyNew.setEnabled(val);
//        btnSubsidySave.setEnabled(val);
//        btnSubsidyDelete.setEnabled(val);
    }
    public  void enableDisableSubsidyNewButton(boolean val){
//        btnSubSidyNew.setEnabled(!val);
//        btnSubsidySave.setEnabled(val);
//        btnSubsidyDelete.setEnabled(val);
    }
    
    
    private void internationalize(){
        lblSolicitor_Name.setText(resourceBundle.getString("lblSolicitor_Name"));
        lblValuerName.setText(resourceBundle.getString("lblValuerName"));
        lblDeveloper_Name.setText(resourceBundle.getString("lblDeveloper_Name"));
        lblProject_Name.setText(resourceBundle.getString("lblProject_Name"));
        lblReferalCode.setText(resourceBundle.getString("lblReferalCode"));
        lblDisbursement_mode.setText(resourceBundle.getString("lblDisbursement_mode"));
        lblSource_Code.setText(resourceBundle.getString("lblSource_Code"));


    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        panSublimitDetails = new com.see.truetransact.uicomponent.CPanel();
        lblSolicitor_Name = new com.see.truetransact.uicomponent.CLabel();
        lblshowSolicitor_Name = new com.see.truetransact.uicomponent.CLabel();
        lblValuerName = new com.see.truetransact.uicomponent.CLabel();
        lblshowValuerName = new com.see.truetransact.uicomponent.CLabel();
        lblDeveloper_Name = new com.see.truetransact.uicomponent.CLabel();
        lblshowDeveloper_Name = new com.see.truetransact.uicomponent.CLabel();
        lblProject_Name = new com.see.truetransact.uicomponent.CLabel();
        lblshowProject_Name = new com.see.truetransact.uicomponent.CLabel();
        lblReferalCode = new com.see.truetransact.uicomponent.CLabel();
        txtReferalCode = new com.see.truetransact.uicomponent.CTextField();
        lblDisbursement_mode = new com.see.truetransact.uicomponent.CLabel();
        cboDisbursement_mode = new com.see.truetransact.uicomponent.CComboBox();
        lblSource_Code = new com.see.truetransact.uicomponent.CLabel();
        cboSource_Code = new com.see.truetransact.uicomponent.CComboBox();

        setLayout(new java.awt.GridBagLayout());

        setMinimumSize(new java.awt.Dimension(820, 560));
        setPreferredSize(new java.awt.Dimension(820, 560));
        panSublimitDetails.setLayout(new java.awt.GridBagLayout());

        panSublimitDetails.setBorder(new javax.swing.border.TitledBorder("Other Facility Details"));
        panSublimitDetails.setMinimumSize(new java.awt.Dimension(420, 290));
        panSublimitDetails.setPreferredSize(new java.awt.Dimension(420, 290));
        lblSolicitor_Name.setText("Solicitor Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panSublimitDetails.add(lblSolicitor_Name, gridBagConstraints);

        lblshowSolicitor_Name.setText("Mumbai");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panSublimitDetails.add(lblshowSolicitor_Name, gridBagConstraints);

        lblValuerName.setText("Valuer Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panSublimitDetails.add(lblValuerName, gridBagConstraints);

        lblshowValuerName.setText("Mumbai Indians");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panSublimitDetails.add(lblshowValuerName, gridBagConstraints);

        lblDeveloper_Name.setText("Developer Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panSublimitDetails.add(lblDeveloper_Name, gridBagConstraints);

        lblshowDeveloper_Name.setText("L & T  Pvt Ltd");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panSublimitDetails.add(lblshowDeveloper_Name, gridBagConstraints);

        lblProject_Name.setText("Project Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panSublimitDetails.add(lblProject_Name, gridBagConstraints);

        lblshowProject_Name.setText("Garuda Flats");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panSublimitDetails.add(lblshowProject_Name, gridBagConstraints);

        lblReferalCode.setText("Referal Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panSublimitDetails.add(lblReferalCode, gridBagConstraints);

        txtReferalCode.setMaximumSize(new java.awt.Dimension(100, 21));
        txtReferalCode.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panSublimitDetails.add(txtReferalCode, gridBagConstraints);

        lblDisbursement_mode.setText("Disbursement Mode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panSublimitDetails.add(lblDisbursement_mode, gridBagConstraints);

        cboDisbursement_mode.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboDisbursement_mode.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSublimitDetails.add(cboDisbursement_mode, gridBagConstraints);

        lblSource_Code.setText("Source Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panSublimitDetails.add(lblSource_Code, gridBagConstraints);

        cboSource_Code.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboSource_Code.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSublimitDetails.add(cboSource_Code, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(panSublimitDetails, gridBagConstraints);

    }//GEN-END:initComponents
    private void subSidyEnableDisable(boolean val){
        
        txtReferalCode.setEnabled(val);
        cboDisbursement_mode.setEnabled(val);
        cboSource_Code.setEnabled(val);
//        cboTypeOfSubSidy.setEnabled(val);
//        cboDepositProdId.setEnabled(val);
//        txtDepositNo.setEnabled(val);
//        tdlSubSidyDate.setEnabled(val);
//        cboRecivedFrom.setEnabled(val);
//        txtSubSidyAmt.setEnabled(val);
//        txtAmtAdjusted.setEnabled(val);
//        txtAmtRefunded.setEnabled(val);
//        tdtRefundDate.setEnabled(val);
//        txtOutStandingAmt.setEnabled(false);
//        btnInspectBy2.setEnabled(val);
        
    }
    
    private void resetSubsidy(){
        txtReferalCode.setText("");
        cboDisbursement_mode.setSelectedItem("");
        cboSource_Code.setSelectedItem("");

        //        cboTypeOfSubSidy.setSelectedItem("");
//        cboDepositProdId.setSelectedItem("");
//        txtDepositNo.setText("");
//        tdlSubSidyDate.setDateValue("");
//        cboRecivedFrom.setSelectedItem("");
//        txtSubSidyAmt.setText("");
//        txtAmtAdjusted.setText("");
//        txtAmtRefunded.setText("");
//        tdtRefundDate.setDateValue("");
//        txtOutStandingAmt.setText("");
    }
    public java.util.HashMap getMandatoryHashMap() {
        HashMap mandatoryMap=new HashMap();
        return mandatoryMap;
    }
    
    public void setMandatoryHashMap() {
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CComboBox cboDisbursement_mode;
    private com.see.truetransact.uicomponent.CComboBox cboSource_Code;
    private com.see.truetransact.uicomponent.CLabel lblDeveloper_Name;
    private com.see.truetransact.uicomponent.CLabel lblDisbursement_mode;
    private com.see.truetransact.uicomponent.CLabel lblProject_Name;
    private com.see.truetransact.uicomponent.CLabel lblReferalCode;
    private com.see.truetransact.uicomponent.CLabel lblSolicitor_Name;
    private com.see.truetransact.uicomponent.CLabel lblSource_Code;
    private com.see.truetransact.uicomponent.CLabel lblValuerName;
    private com.see.truetransact.uicomponent.CLabel lblshowDeveloper_Name;
    private com.see.truetransact.uicomponent.CLabel lblshowProject_Name;
    private com.see.truetransact.uicomponent.CLabel lblshowSolicitor_Name;
    private com.see.truetransact.uicomponent.CLabel lblshowValuerName;
    private com.see.truetransact.uicomponent.CPanel panSublimitDetails;
    private com.see.truetransact.uicomponent.CTextField txtReferalCode;
    // End of variables declaration//GEN-END:variables
    public static void main(String str[]){
        new AgriSubSidyUI().show();
    }
    
    public void update(java.util.Observable o, Object arg) {
        
//        lblshowSolicitor_Name.setText(agriSubSidyOB.getLblshowSolicitor_Name());
//        lblshowValuerName.setText(agriSubSidyOB.getLblshowValuerName());
//        lblshowDeveloper_Name.setText(agriSubSidyOB.getLblshowDeveloper_Name());
//        lblshowProject_Name.setText(agriSubSidyOB.getLblshowProject_Name());
        txtReferalCode.setText(agriSubSidyOB.getTxtReferalCode());
        ((com.see.truetransact.clientutil.ComboBoxModel)cboDisbursement_mode.getModel()).setKeyForSelected(agriSubSidyOB.getCboDisbursement_mode());
        ((com.see.truetransact.clientutil.ComboBoxModel)cboSource_Code.getModel()).setKeyForSelected(agriSubSidyOB.getCboSource_Code());

        
    }
    public void updateOBFields(){

        agriSubSidyOB.setLblshowSolicitor_Name(lblshowSolicitor_Name.getText());
        agriSubSidyOB.setLblshowValuerName(lblshowValuerName.getText());
        agriSubSidyOB.setLblshowDeveloper_Name(lblshowDeveloper_Name.getText());
        agriSubSidyOB.setLblshowProject_Name(lblshowProject_Name.getText());
        agriSubSidyOB.setTxtReferalCode(txtReferalCode.getText());
        agriSubSidyOB.setCboDisbursement_mode(CommonUtil.convertObjToStr(cboDisbursement_mode.getSelectedItem()));
        agriSubSidyOB.setCboSource_Code(CommonUtil.convertObjToStr(cboSource_Code.getSelectedItem()));
              
    }
    
    /**
     * Getter for property agriSubSidyOB.
     * @return Value of property agriSubSidyOB.
     */
    public com.see.truetransact.ui.termloan.agrisubsidydetails.AgriSubSidyOB getAgriSubSidyOB() {
        return agriSubSidyOB;
    }
    
    /**
     * Setter for property agriSubSidyOB.
     * @param agriSubSidyOB New value of property agriSubSidyOB.
     */
    public void setAgriSubSidyOB(com.see.truetransact.ui.termloan.agrisubsidydetails.AgriSubSidyOB agriSubSidyOB) {
        this.agriSubSidyOB = agriSubSidyOB;
    }
    
    
}
