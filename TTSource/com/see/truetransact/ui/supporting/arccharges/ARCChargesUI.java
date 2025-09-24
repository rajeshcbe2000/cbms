/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ARCChargesUI.java
 *
 * Created on December 22, 2004, 5:15 PM
 */

package com.see.truetransact.ui.supporting.arccharges;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Observable;
import java.util.LinkedHashMap;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.clientexception.ClientParseException;

/**
 *
 * @author  152691
 */

public class ARCChargesUI extends com.see.truetransact.uicomponent.CInternalFrame implements com.see.truetransact.uimandatory.UIMandatoryField, java.util.Observer{
    
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.supporting.arccharges.ARCChargesRB", ProxyParameters.LANGUAGE);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private ARCChargesMRB objMandatoryRB = new ARCChargesMRB();
    private String viewType = new String();
    private boolean updateMode = false;
    final String AUTHORIZE="Authorize";
    private boolean isFilled = false;
    HashMap mandatoryMap = null;
    ARCChargesOB observable = null;
    int updateTab=-1;
    
    /** Creates new form ChargesUI */
    public ARCChargesUI() {
        initComponents();
        setFieldNames();
        internationalize();
        observable = new ARCChargesOB();
        setMaxLengths();
        initComponentData();
        setMandatoryHashMap();
        setHelpMessage();
        setButtonEnableDisable();
//        btnAccGroupId.setEnabled(false);
        panChargeTable.setVisible(false);
        panChargeBtn.setVisible(false);
        ClientUtil.enableDisable(panChargeMainDetails,false);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panChargeMainDetails, getMandatoryHashMap());
    }
    
    private void initComponentData() {
        cboRoundOffType.setModel(observable.getCbmRoundOffType());
        cboChargeBase.setModel(observable.getCbmChargeBase());
//        cboSchemeId.setModel(observable.getCbmProductId());
        cboChargeType.setModel(observable.getCbmChargeType());
        tblCharge.setModel(observable.getTblCharge());
    }
    
    
/* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
//        btnAccGroupId.setName("btnAccGroupId");
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
        cboChargeBase.setName("cboChargeBase");
        cboChargeType.setName("cboChargeType");
        cboRoundOffType.setName("cboRoundOffType");
       
        lbDivisibleBy.setName("lbDivisibleBy");
//        lblAccGroupId.setName("lblAccGroupId");
        lblChargeBase.setName("lblChargeBase");
//        lblChargeDesc.setName("lblChargeDesc");
        lblChargeId.setName("lblChargeId");
        lblChargeRate.setName("lblChargeRate");
//        lblDeductionOccu.setName("lblDeductionOccu");
        lblFlatCharges.setName("lblFlatCharges");
        lblFromSlabAmount.setName("lblFromSlabAmount");
        lblMaximumChrgAmt.setName("lblMaximumChrgAmt");
        lblMinimumChrgAmt.setName("lblMinimumChrgAmt");
        lblMsg.setName("lblMsg");
        lblRoundOffType.setName("lblRoundOffType");
        
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblSpace5.setName("lblSpace5");
        lblSpace6.setName("lblSpace6");
        lblStatus.setName("lblStatus");
        lblToSlabAmount.setName("lblToSlabAmount");
        mbrMain.setName("mbrMain");
        panAccountHead.setName("panAccountHead");
        panChargeMainDetails.setName("panChargeMainDetails");
        panStatus.setName("panStatus");
//        rdoDeductionAccClose_no.setName("rdoDeductionAccClose_no");
//        rdoDeductionAccOpen_yes.setName("rdoDeductionAccOpen_yes");
//        rdoMandatory_no.setName("rdoMandatory_no");
//        rdoMandatory_yes.setName("rdoMandatory_yes");
//        txtAccGroupId.setName("txtAccGroupId");
//        txtChargeDesc.setName("txtChargeDesc");
        txtChargeId.setName("txtChargeId");
        txtChargeRate.setName("txtChargeRate");
        txtDivisibleBy.setName("txtDivisibleBy");
        txtFlatCharges.setName("txtFlatCharges");
        txtFromSlabAmount.setName("txtFromSlabAmount");
        txtMaximumChrgAmt.setName("txtMaximumChrgAmt");
        txtMinimumChrgAmt.setName("txtMinimumChrgAmt");
        txtToSlabAmount.setName("txtToSlabAmount");
    }
    
    
    
  /* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
//        lblChargeDesc.setText(resourceBundle.getString("lblChargeDesc"));
        lblFromSlabAmount.setText(resourceBundle.getString("lblFromSlabAmount"));
        btnClose.setText(resourceBundle.getString("btnClose"));
//        rdoDeductionAccClose_no.setText(resourceBundle.getString("rdoDeductionAccClose_no"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        btnException.setText(resourceBundle.getString("btnException"));
//        lblAccGroupId.setText(resourceBundle.getString("lblAccGroupId"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblChargeBase.setText(resourceBundle.getString("lblChargeBase"));
        lblSpace5.setText(resourceBundle.getString("lblSpace5"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lbDivisibleBy.setText(resourceBundle.getString("lbDivisibleBy"));
//        rdoMandatory_yes.setText(resourceBundle.getString("rdoMandatory_yes"));
        lblMaximumChrgAmt.setText(resourceBundle.getString("lblMaximumChrgAmt"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
//        btnAccGroupId.setText(resourceBundle.getString("btnAccGroupId"));
        lblFlatCharges.setText(resourceBundle.getString("lblFlatCharges"));
        lblRoundOffType.setText(resourceBundle.getString("lblRoundOffType"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        lblChargeId.setText(resourceBundle.getString("lblChargeId"));
//        lblSchemeId.setText(resourceBundle.getString("lblSchemeId"));
        lblToSlabAmount.setText(resourceBundle.getString("lblToSlabAmount"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblSpace6.setText(resourceBundle.getString("lblSpace6"));
//        rdoMandatory_no.setText(resourceBundle.getString("rdoMandatory_no"));
        lblChargeRate.setText(resourceBundle.getString("lblChargeRate"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
//        lblDeductionOccu.setText(resourceBundle.getString("lblDeductionOccu"));
        lblMinimumChrgAmt.setText(resourceBundle.getString("lblMinimumChrgAmt"));
//        rdoDeductionAccOpen_yes.setText(resourceBundle.getString("rdoDeductionAccOpen_yes"));
    }
    
    
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
    }
    
    public void update() {
//        cboSchemeId.setSelectedItem(observable.getCboSchemeId());
//        txtAccGroupId.setText(observable.getTxtAccGroupId());
        txtDivisibleBy.setText(observable.getTxtDivisibleBy());
        cboChargeBase.setSelectedItem(observable.getCboChargeBase());
       ((ComboBoxModel) cboChargeType.getModel()).setKeyForSelected(observable.getCboChargeType());
        txtChargeId.setText(observable.getTxtChargeId());
//        txtChargeDesc.setText(observable.getTxtChargeDesc());
        txtMinimumChrgAmt.setText(observable.getTxtMinimumChrgAmt());
        txtMaximumChrgAmt.setText(observable.getTxtMaximumChrgAmt());
        cboRoundOffType.setSelectedItem(observable.getCboRoundOffType());
        String  mandatory=CommonUtil.convertObjToStr(observable.getRdoMandatory());
//        if(mandatory.length()>0 && mandatory.equals("Y")){
//            rdoMandatory_yes.setSelected(true);
//            rdoMandatory_no.setSelected(false);
//        }else{
//            rdoMandatory_yes.setSelected(false);
//            rdoMandatory_no.setSelected(true);
//        }
        String  DeductionOccu=CommonUtil.convertObjToStr(observable.getRdoDeductionAcc());
//        if(DeductionOccu.length()>0 && DeductionOccu.equals("O")){
//            rdoDeductionAccOpen_yes.setSelected(true);
//            rdoDeductionAccClose_no.setSelected(false);
//        }else{
//            rdoDeductionAccOpen_yes.setSelected(false);
//            rdoDeductionAccClose_no.setSelected(true);
//        }
        cboChargeBase.setSelectedItem(observable.getCboChargeBase());
        String chargeBase = (String) cboChargeBase.getSelectedItem();
        if(chargeBase.equals("Sanction Amount")){
            slabAmountVisible(false);
            flatChargesVisible(false);
            chargeRateVisible(true);
        }
        if(chargeBase.equals("Flat Charge")){
            slabAmountVisible(false);
            flatChargesVisible(true);
            chargeRateVisible(false);
        }
        if(chargeBase.equals("Amount Range")){
            slabAmountVisible(true);
            flatChargesVisible(false);
            chargeRateVisible(true);
        }
        txtChargeRate.setText(observable.getTxtChargeRate());
        txtFlatCharges.setText(observable.getTxtFlatCharges());
        txtFromSlabAmount.setText(observable.getTxtFromSlabAmount());
        txtToSlabAmount.setText(observable.getTxtToSlabAmount());
    }
    
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setTxtToSlabAmount(txtToSlabAmount.getText());
        observable.setTxtFlatCharges(txtFlatCharges.getText());
        observable.setTxtDivisibleBy(txtDivisibleBy.getText());
        observable.setCboChargeBase((String) cboChargeBase.getSelectedItem());
        observable.setCboChargeType((String) cboChargeType.getSelectedItem());
        observable.setTxtChargeId(txtChargeId.getText());
//        observable.setTxtChargeDesc(txtChargeDesc.getText());
        observable.setTxtFromSlabAmount(txtFromSlabAmount.getText());
        observable.setTxtMinimumChrgAmt(txtMinimumChrgAmt.getText());
        observable.setTxtMaximumChrgAmt(txtMaximumChrgAmt.getText());
        observable.setTxtChargeRate(txtChargeRate.getText());
        observable.setCboRoundOffType((String) cboRoundOffType.getSelectedItem());
//        observable.setCboSchemeId((String) cboSchemeId.getSelectedItem());
//        observable.setTxtAccGroupId(txtAccGroupId.getText());
//        if(rdoMandatory_yes.isSelected()==true)
//            observable.setRdoMandatory(CommonUtil.convertObjToStr("Y"));
//        else
//            observable.setRdoMandatory(CommonUtil.convertObjToStr("N"));
//        if(rdoDeductionAccOpen_yes.isSelected()==true)
//            observable.setRdoDeductionAcc(CommonUtil.convertObjToStr("O"));
//        else
//            observable.setRdoDeductionAcc(CommonUtil.convertObjToStr("C"));
    }
    
 /* Auto Generated Method - setMandatoryHashMap()
  
ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
  
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboSchemeId", new Boolean(true));
        mandatoryMap.put("txtAccGroupId", new Boolean(true));
        mandatoryMap.put("txtToSlabAmount", new Boolean(false));
        mandatoryMap.put("txtFlatCharges", new Boolean(false));
        mandatoryMap.put("txtDivisibleBy", new Boolean(false));
        mandatoryMap.put("cboChargeBase", new Boolean(false));
        mandatoryMap.put("txtChargeId", new Boolean(false));
        mandatoryMap.put("txtChargeDesc", new Boolean(false));
        mandatoryMap.put("txtFromSlabAmount", new Boolean(false));
        mandatoryMap.put("txtMinimumChrgAmt", new Boolean(false));
        mandatoryMap.put("txtMaximumChrgAmt", new Boolean(false));
        mandatoryMap.put("txtChargeRate", new Boolean(false));
        mandatoryMap.put("cboRoundOffType", new Boolean(false));
    }
    
/* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    /** Method used to check whether the Mandatory Fields in the Form are Filled or not */
    private String checkMandatory(javax.swing.JComponent component){
        return new MandatoryCheck().checkMandatory(getClass().getName(), component);
    }
    
    /** Method used to Give a Alert when any Mandatory Field is not filled by the user */
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    
/* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        ARCChargesMRB objMandatoryRB = new ARCChargesMRB();
        txtToSlabAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtToSlabAmount"));
        txtFlatCharges.setHelpMessage(lblMsg, objMandatoryRB.getString("txtFlatCharges"));
        txtDivisibleBy.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDivisibleBy"));
        cboChargeBase.setHelpMessage(lblMsg, objMandatoryRB.getString("cboChargeBase"));
        txtChargeId.setHelpMessage(lblMsg, objMandatoryRB.getString("txtChargeId"));
//        txtChargeDesc.setHelpMessage(lblMsg, objMandatoryRB.getString("txtChargeDesc"));
        txtFromSlabAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtFromSlabAmount"));
        txtMinimumChrgAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMinimumChrgAmt"));
        txtMaximumChrgAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMaximumChrgAmt"));
        txtChargeRate.setHelpMessage(lblMsg, objMandatoryRB.getString("txtChargeRate"));
        cboRoundOffType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboRoundOffType"));
//        cboSchemeId.setHelpMessage(lblMsg, objMandatoryRB.getString("cboSchemeId"));
//        txtAccGroupId.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAccGroupId"));
    }
    
    private void setMaxLengths(){
        txtChargeId.setMaxLength(16);
//        txtChargeDesc.setMaxLength(16);
        txtFromSlabAmount.setMaxLength(16);
        txtToSlabAmount.setMaxLength(16);
        txtFlatCharges.setMaxLength(16);
        txtFromSlabAmount.setValidation(new CurrencyValidation(14,2));
        txtToSlabAmount.setValidation(new CurrencyValidation(14,2));
        txtFlatCharges.setValidation(new CurrencyValidation(14,2));
        txtChargeRate.setValidation(new NumericValidation(3,2));
        txtDivisibleBy.setValidation(new CurrencyValidation(14,2));
        txtMinimumChrgAmt.setValidation(new CurrencyValidation(14,2));
        txtMaximumChrgAmt.setValidation(new CurrencyValidation(14,2));
        txtChargeId.setAllowAll(true);
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
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoDeductionAcc = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoMandatory = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrOperativeAcctProduct = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace6 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace56 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace57 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace58 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace59 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace60 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace61 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        panChargeMainDetailsData = new com.see.truetransact.uicomponent.CPanel();
        panChargeMainDetails = new com.see.truetransact.uicomponent.CPanel();
        lblToSlabAmount = new com.see.truetransact.uicomponent.CLabel();
        txtToSlabAmount = new com.see.truetransact.uicomponent.CTextField();
        lblFlatCharges = new com.see.truetransact.uicomponent.CLabel();
        txtFlatCharges = new com.see.truetransact.uicomponent.CTextField();
        lblChargeBase = new com.see.truetransact.uicomponent.CLabel();
        txtDivisibleBy = new com.see.truetransact.uicomponent.CTextField();
        lblFromSlabAmount = new com.see.truetransact.uicomponent.CLabel();
        lbDivisibleBy = new com.see.truetransact.uicomponent.CLabel();
        lblChargeId = new com.see.truetransact.uicomponent.CLabel();
        cboChargeBase = new com.see.truetransact.uicomponent.CComboBox();
        txtChargeId = new com.see.truetransact.uicomponent.CTextField();
        txtFromSlabAmount = new com.see.truetransact.uicomponent.CTextField();
        lblMinimumChrgAmt = new com.see.truetransact.uicomponent.CLabel();
        txtMinimumChrgAmt = new com.see.truetransact.uicomponent.CTextField();
        lblMaximumChrgAmt = new com.see.truetransact.uicomponent.CLabel();
        txtMaximumChrgAmt = new com.see.truetransact.uicomponent.CTextField();
        lblChargeRate = new com.see.truetransact.uicomponent.CLabel();
        txtChargeRate = new com.see.truetransact.uicomponent.CTextField();
        lblRoundOffType = new com.see.truetransact.uicomponent.CLabel();
        cboRoundOffType = new com.see.truetransact.uicomponent.CComboBox();
        panAccountHead = new com.see.truetransact.uicomponent.CPanel();
        panChargeBtn = new com.see.truetransact.uicomponent.CPanel();
        btnChargeNew = new com.see.truetransact.uicomponent.CButton();
        btnChargeSave = new com.see.truetransact.uicomponent.CButton();
        btnChargeDelete = new com.see.truetransact.uicomponent.CButton();
        cboChargeType = new com.see.truetransact.uicomponent.CComboBox();
        lblChargeBase1 = new com.see.truetransact.uicomponent.CLabel();
        panChargeTable = new com.see.truetransact.uicomponent.CPanel();
        panChargeDetailTable = new com.see.truetransact.uicomponent.CPanel();
        srpChargeTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblCharge = new com.see.truetransact.uicomponent.CTable();
        mbrMain = new com.see.truetransact.uicomponent.CMenuBar();
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
        setFont(new java.awt.Font("SansSerif", 0, 10)); // NOI18N
        setMinimumSize(new java.awt.Dimension(850, 660));
        setPreferredSize(new java.awt.Dimension(850, 660));

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
        tbrOperativeAcctProduct.add(btnView);

        lblSpace6.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace6);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnNew);

        lblSpace56.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace56.setText("     ");
        lblSpace56.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace56.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace56.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace56);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnEdit);

        lblSpace57.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace57.setText("     ");
        lblSpace57.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace57.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace57.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace57);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnDelete);

        lblSpace2.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnSave);

        lblSpace58.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace58.setText("     ");
        lblSpace58.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace58.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace58.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace58);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnCancel);

        lblSpace3.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnAuthorize);

        lblSpace59.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace59.setText("     ");
        lblSpace59.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace59.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace59.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace59);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnException);

        lblSpace60.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace60.setText("     ");
        lblSpace60.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace60.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace60.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace60);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnReject);

        lblSpace5.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrOperativeAcctProduct.add(btnPrint);

        lblSpace61.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace61.setText("     ");
        lblSpace61.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace61.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace61.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace61);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnClose);

        getContentPane().add(tbrOperativeAcctProduct, java.awt.BorderLayout.NORTH);

        panStatus.setMinimumSize(new java.awt.Dimension(110, 19));
        panStatus.setPreferredSize(new java.awt.Dimension(110, 19));
        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace4.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace4, gridBagConstraints);

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

        panChargeMainDetailsData.setMinimumSize(new java.awt.Dimension(850, 580));
        panChargeMainDetailsData.setPreferredSize(new java.awt.Dimension(850, 580));
        panChargeMainDetailsData.setLayout(new java.awt.GridBagLayout());

        panChargeMainDetails.setMinimumSize(new java.awt.Dimension(420, 580));
        panChargeMainDetails.setPreferredSize(new java.awt.Dimension(420, 580));
        panChargeMainDetails.setLayout(new java.awt.GridBagLayout());

        lblToSlabAmount.setText("To Slab Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChargeMainDetails.add(lblToSlabAmount, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChargeMainDetails.add(txtToSlabAmount, gridBagConstraints);

        lblFlatCharges.setText("Flat Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChargeMainDetails.add(lblFlatCharges, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChargeMainDetails.add(txtFlatCharges, gridBagConstraints);

        lblChargeBase.setText("Charge Base");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChargeMainDetails.add(lblChargeBase, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChargeMainDetails.add(txtDivisibleBy, gridBagConstraints);

        lblFromSlabAmount.setText("From Slab Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChargeMainDetails.add(lblFromSlabAmount, gridBagConstraints);

        lbDivisibleBy.setText("Divisible By");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChargeMainDetails.add(lbDivisibleBy, gridBagConstraints);

        lblChargeId.setText("Charge Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChargeMainDetails.add(lblChargeId, gridBagConstraints);

        cboChargeBase.setPreferredSize(new java.awt.Dimension(27, 22));
        cboChargeBase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboChargeBaseActionPerformed(evt);
            }
        });
        cboChargeBase.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboChargeBaseFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.ipadx = 75;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panChargeMainDetails.add(cboChargeBase, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChargeMainDetails.add(txtChargeId, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChargeMainDetails.add(txtFromSlabAmount, gridBagConstraints);

        lblMinimumChrgAmt.setText("Minimum Charge Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChargeMainDetails.add(lblMinimumChrgAmt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChargeMainDetails.add(txtMinimumChrgAmt, gridBagConstraints);

        lblMaximumChrgAmt.setText("Maximum Charge Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChargeMainDetails.add(lblMaximumChrgAmt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChargeMainDetails.add(txtMaximumChrgAmt, gridBagConstraints);

        lblChargeRate.setText("Charge Rate(%)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChargeMainDetails.add(lblChargeRate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChargeMainDetails.add(txtChargeRate, gridBagConstraints);

        lblRoundOffType.setText("Round Off Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 2, 1);
        panChargeMainDetails.add(lblRoundOffType, gridBagConstraints);

        cboRoundOffType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboRoundOffType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboRoundOffTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChargeMainDetails.add(cboRoundOffType, gridBagConstraints);

        panAccountHead.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panChargeMainDetails.add(panAccountHead, gridBagConstraints);

        panChargeBtn.setMinimumSize(new java.awt.Dimension(95, 35));
        panChargeBtn.setPreferredSize(new java.awt.Dimension(95, 35));
        panChargeBtn.setLayout(new java.awt.GridBagLayout());

        btnChargeNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnChargeNew.setToolTipText("New");
        btnChargeNew.setMaximumSize(new java.awt.Dimension(29, 27));
        btnChargeNew.setMinimumSize(new java.awt.Dimension(29, 27));
        btnChargeNew.setPreferredSize(new java.awt.Dimension(29, 27));
        btnChargeNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChargeNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChargeBtn.add(btnChargeNew, gridBagConstraints);

        btnChargeSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnChargeSave.setToolTipText("Save");
        btnChargeSave.setMaximumSize(new java.awt.Dimension(29, 27));
        btnChargeSave.setMinimumSize(new java.awt.Dimension(29, 27));
        btnChargeSave.setName("btnContactNoAdd");
        btnChargeSave.setPreferredSize(new java.awt.Dimension(29, 27));
        btnChargeSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChargeSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChargeBtn.add(btnChargeSave, gridBagConstraints);

        btnChargeDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnChargeDelete.setToolTipText("Delete");
        btnChargeDelete.setMaximumSize(new java.awt.Dimension(29, 27));
        btnChargeDelete.setMinimumSize(new java.awt.Dimension(29, 27));
        btnChargeDelete.setPreferredSize(new java.awt.Dimension(29, 27));
        btnChargeDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChargeDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChargeBtn.add(btnChargeDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.insets = new java.awt.Insets(15, 0, 0, 0);
        panChargeMainDetails.add(panChargeBtn, gridBagConstraints);

        cboChargeType.setPreferredSize(new java.awt.Dimension(27, 22));
        cboChargeType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboChargeTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipadx = 75;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panChargeMainDetails.add(cboChargeType, gridBagConstraints);

        lblChargeBase1.setText("Charge Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChargeMainDetails.add(lblChargeBase1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panChargeMainDetailsData.add(panChargeMainDetails, gridBagConstraints);

        panChargeTable.setMinimumSize(new java.awt.Dimension(360, 500));
        panChargeTable.setPreferredSize(new java.awt.Dimension(360, 500));
        panChargeTable.setLayout(new java.awt.GridBagLayout());

        panChargeDetailTable.setMinimumSize(new java.awt.Dimension(350, 450));
        panChargeDetailTable.setPreferredSize(new java.awt.Dimension(350, 450));
        panChargeDetailTable.setLayout(new java.awt.GridBagLayout());

        srpChargeTable.setMinimumSize(new java.awt.Dimension(350, 450));
        srpChargeTable.setPreferredSize(new java.awt.Dimension(350, 450));

        tblCharge.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SL No", "From Amount", "To Amount"
            }
        ));
        tblCharge.setMinimumSize(new java.awt.Dimension(350, 5000));
        tblCharge.setPreferredScrollableViewportSize(new java.awt.Dimension(250, 500));
        tblCharge.setPreferredSize(new java.awt.Dimension(350, 5000));
        tblCharge.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblChargeMousePressed(evt);
            }
        });
        srpChargeTable.setViewportView(tblCharge);

        panChargeDetailTable.add(srpChargeTable, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panChargeTable.add(panChargeDetailTable, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panChargeMainDetailsData.add(panChargeTable, gridBagConstraints);

        getContentPane().add(panChargeMainDetailsData, java.awt.BorderLayout.EAST);

        mnuProcess.setText("Process");
        mnuProcess.setToolTipText("Menu");

        mitNew.setText("New");
        mitNew.setToolTipText("");
        mitNew.setEnabled(false);
        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mitEdit.setEnabled(false);
        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mitDelete.setEnabled(false);
        mnuProcess.add(mitDelete);

        sptDelete.setEnabled(false);
        mnuProcess.add(sptDelete);

        mitSave.setText("Save");
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancle");
        mnuProcess.add(mitCancel);

        sptCancel.setEnabled(false);
        mnuProcess.add(sptCancel);

        mitClose.setText("Close");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mitClose.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mitCloseMouseClicked(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrMain.add(mnuProcess);

        setJMenuBar(mbrMain);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cboChargeTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboChargeTypeActionPerformed
        // TODO add your handling code here:
        String chargeType =CommonUtil.convertObjToStr(observable.getCbmChargeType().getKeyForSelected());

        if(chargeType.length()>0 && observable.getActionType()==ClientConstants.ACTIONTYPE_NEW){
            HashMap whereMap =new HashMap();
            whereMap.put("CHARGE_TYPE",chargeType);
            List lst=ClientUtil.executeQuery("checkARCChargeDetails", whereMap);
            if(lst !=null && lst.size()>0){
                StringBuffer buffer=new StringBuffer();
                HashMap resultMap=(HashMap)lst.get(0);
		 buffer.append("Charge Type    :"+chargeType+"Already Created" +"\n");
                 
                buffer.append("Charge Id is  :");
                buffer.append(CommonUtil.convertObjToStr(resultMap.get("CHARGE_ID")));
                ClientUtil.showMessageWindow(buffer.toString());
                cboChargeType.setSelectedItem("");
            }
        }
        
    }//GEN-LAST:event_cboChargeTypeActionPerformed

    private void cboRoundOffTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboRoundOffTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboRoundOffTypeActionPerformed
    
    private void tblChargeMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblChargeMousePressed
        // TODO add your handling code here:
        updateSlabAmountFields();
        cboChargeBase.setEnabled(false);
        txtFromSlabAmount.setEnabled(false);
        updateMode = true;
        updateTab= tblCharge.getSelectedRow();
        observable.setSlabAmountData(false);
        String st=CommonUtil.convertObjToStr(tblCharge.getValueAt(tblCharge.getSelectedRow(),0));
        observable.populateSlabDetails(st);
        populateSlabDetails();
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_AUTHORIZE||observable.getActionType()==ClientConstants.ACTIONTYPE_REJECT ||
        observable.getActionType()==ClientConstants.ACTIONTYPE_VIEW || observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE ||
        observable.getActionType()==ClientConstants.ACTIONTYPE_EXCEPTION){
            btnChargeNew.setEnabled(false);
            btnChargeSave.setEnabled(false);
            btnChargeDelete.setEnabled(false);
            disableSlabDetails(false);
        }else {
            if(tblCharge.getSelectedRow()== tblCharge.getRowCount()-1){
                btnChargeSave.setEnabled(true);
                btnChargeDelete.setEnabled(true);
                btnChargeNew.setEnabled(false);
                disableSlabDetails(true);
            }else{
                btnChargeSave.setEnabled(false);
                btnChargeDelete.setEnabled(false);
                btnChargeNew.setEnabled(false);
                disableSlabDetails(false);
            }
        }
    }//GEN-LAST:event_tblChargeMousePressed
    public void populateSlabDetails(){
        txtFromSlabAmount.setText(observable.getTxtFromSlabAmount());
        txtToSlabAmount.setText(observable.getTxtToSlabAmount());
        txtChargeRate.setText(observable.getTxtChargeRate());
        txtDivisibleBy.setText(observable.getTxtDivisibleBy());
        cboRoundOffType.setSelectedItem(observable.getCboRoundOffType());
        txtMinimumChrgAmt.setText(observable.getTxtMinimumChrgAmt());
        txtMaximumChrgAmt.setText(observable.getTxtMaximumChrgAmt());
    }
    private void btnChargeDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChargeDeleteActionPerformed
        // TODO add your handling code here:
        String s=  CommonUtil.convertObjToStr(tblCharge.getValueAt(tblCharge.getSelectedRow(),0));
        observable.deleteTableData(s,tblCharge.getSelectedRow());
        observable.resetSlabDetails();
        resetSlabDetails();
        disableSlabDetails(false);
        btnChargeNew.setEnabled(true);
        btnChargeSave.setEnabled(false);
        btnChargeDelete.setEnabled(false);
    }//GEN-LAST:event_btnChargeDeleteActionPerformed
    
    private void btnChargeSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChargeSaveActionPerformed
        // TODO add your handling code here:
        try{
            updateSlabAmountFields();
            observable.addSlabAmountTable(updateTab,updateMode);
            tblCharge.setModel(observable.getTblCharge());
            observable.resetSlabDetails();
            resetSlabDetails();
            disableSlabDetails(false);
            btnChargeNew.setEnabled(true);
            btnChargeSave.setEnabled(false);
            btnChargeDelete.setEnabled(false);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnChargeSaveActionPerformed
    public void updateSlabAmountFields(){
        observable.setTxtFromSlabAmount(txtFromSlabAmount.getText());
        observable.setTxtToSlabAmount(txtToSlabAmount.getText());
        observable.setTxtDivisibleBy(txtDivisibleBy.getText());
        observable.setTxtChargeId(txtChargeId.getText());
        observable.setTxtMinimumChrgAmt(txtMinimumChrgAmt.getText());
        observable.setTxtMaximumChrgAmt(txtMaximumChrgAmt.getText());
        observable.setTxtChargeRate(txtChargeRate.getText());
        observable.setCboRoundOffType((String) cboRoundOffType.getSelectedItem());
//        observable.setTxtAccGroupId(txtAccGroupId.getText());
    }
    
    private void resetSlabDetails(){
        txtFromSlabAmount.setText("");
        txtToSlabAmount.setText("");
        txtChargeRate.setText("");
        txtDivisibleBy.setText("");
        cboRoundOffType.setSelectedItem("");
        txtMinimumChrgAmt.setText("");
        txtMaximumChrgAmt.setText("");
    }
    
    private void disableSlabDetails(boolean flag){
        txtToSlabAmount.setEnabled(flag);
        txtChargeRate.setEnabled(flag);
        txtDivisibleBy.setEnabled(flag);
        cboRoundOffType.setEnabled(flag);
        txtMinimumChrgAmt.setEnabled(flag);
        txtMaximumChrgAmt.setEnabled(flag);
    }
    private void btnChargeNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChargeNewActionPerformed
        // TODO add your handling code here:
        updateMode = false;
        cboChargeBase.setEnabled(false);
        if(tblCharge.getRowCount() == 0){
            txtFromSlabAmount.setText(String.valueOf(1.0));
        }else if(tblCharge.getRowCount() >0){
            double fromAmt = 0.0;
            for(int i=0; i<tblCharge.getRowCount(); i++){
                fromAmt = CommonUtil.convertObjToDouble(tblCharge.getValueAt(i, 2)).doubleValue();
            }
            fromAmt += 1;
            System.out.println("@@@#%$$$% fromAmt : "+fromAmt);
            txtFromSlabAmount.setText(String.valueOf(fromAmt));
        }
        observable.setSlabAmountData(true);
        btnChargeNew.setEnabled(false);
        btnChargeSave.setEnabled(true);
        btnChargeDelete.setEnabled(false);
        disableSlabDetails(true);
    }//GEN-LAST:event_btnChargeNewActionPerformed
            
    private void cboChargeBaseFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboChargeBaseFocusLost
        // TODO add your handling code here:
        
    }//GEN-LAST:event_cboChargeBaseFocusLost
    private void slabAmountVisible(boolean val){
        lblFromSlabAmount.setVisible(val);
        txtFromSlabAmount.setVisible(val);
        lblToSlabAmount.setVisible(val);
        txtToSlabAmount.setVisible(val);
        txtFromSlabAmount.setText("");
        txtToSlabAmount.setText("");
    }
    
    private void chargeRateVisible(boolean val){
        lblChargeRate.setVisible(val);
        txtChargeRate.setVisible(val);
        txtChargeRate.setText("");
    }
    
    private void divRoundOffVisible(boolean val){
        lbDivisibleBy.setVisible(val);
        txtDivisibleBy.setVisible(val);
        //        txtDivisibleBy.setText("");
        lblRoundOffType.setVisible(val);
        cboRoundOffType.setVisible(val);
        //        cboRoundOffType.setSelectedItem("");
        lblMinimumChrgAmt.setVisible(val);
        txtMinimumChrgAmt.setVisible(val);
        //        txtMinimumChrgAmt.setText("");
        lblMaximumChrgAmt.setVisible(val);
        txtMaximumChrgAmt.setVisible(val);
        //        txtMaximumChrgAmt.setText("");
    }
    
    private void flatChargesVisible(boolean val){
        lblFlatCharges.setVisible(val);
        txtFlatCharges.setVisible(val);
        txtFlatCharges.setText("");
    }
    
    private void amtChargeEnableDisable(boolean val){
        txtFromSlabAmount.setEnabled(val);
        txtToSlabAmount.setEnabled(val);
        
        txtChargeRate.setEnabled(val);
        txtToSlabAmount.setEnabled(val);
        txtToSlabAmount.setEnabled(val);
        txtToSlabAmount.setEnabled(val);
    }    
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        btnView.setEnabled(false);
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        popUp("Enquiry");
        ClientUtil.enableDisable(panChargeMainDetails,false);
        lblStatus.setText("Enquiry");
        btnSave.setEnabled(false);
        btnView.setEnabled(false);
    }//GEN-LAST:event_btnViewActionPerformed
        
    /** To display a popUp window for viewing existing data */
    private void popUp(String currAction){
        viewType = currAction;
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        if (currAction.equalsIgnoreCase("Delete")){
            viewMap.put(CommonConstants.MAP_NAME, "getARCChargeDetailDelete");
        }else if(currAction.equalsIgnoreCase("Enquiry") || currAction.equalsIgnoreCase("Edit")){
            viewMap.put(CommonConstants.MAP_NAME, "getARCChargeDetailEditView");
        }else if(viewType.equals("ACCT_HEAD")){
            viewMap.put(CommonConstants.MAP_NAME, "TermLoan.getSelectAcctHeadTOList");
        }
        new ViewAll(this,viewMap).show();
    }
    
    public void fillData(Object obj){
        try{
            HashMap hashMap=(HashMap)obj;
            System.out.println("### fillData Hash : "+hashMap);
            isFilled = true;
            if(viewType == "ACCT_HEAD"){
//                txtAccGroupId.setText(CommonUtil.convertObjToStr(hashMap.get("AC_HD_ID")));
                return;
            }else if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
                this.setButtonEnableDisable();
                hashMap.put(CommonConstants.MAP_WHERE,hashMap.get("CHARGE_ID"));
                observable.getData(hashMap);
                update();
                tblCharge.setModel(observable.getTblCharge());
            }else if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT){
                this.setButtonEnableDisable();
                 hashMap.put(CommonConstants.MAP_WHERE,hashMap.get("CHARGE_ID"));
                observable.getData(hashMap);
                update();
                tblCharge.setModel(observable.getTblCharge());
            }
            if(viewType ==  AUTHORIZE) {
                btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                ClientUtil.enableDisable(this,false);
            }
            hashMap = null;
            btnCancel.setEnabled(true);
        }catch(Exception e){
            e.printStackTrace();
        }
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }
    
private void cboChargeBaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboChargeBaseActionPerformed
    String chargeBase = (String) cboChargeBase.getSelectedItem();
    if(chargeBase.equals("Sanction Amount")){
        slabAmountVisible(false);
        flatChargesVisible(false);
        chargeRateVisible(true);
        divRoundOffVisible(true);
        panChargeTable.setVisible(false);
        panChargeBtn.setVisible(false);
        cboChargeBase.setEnabled(true);
        disableSlabDetails(true);
    }
    if(chargeBase.equals("Flat Charge")){
        slabAmountVisible(false);
        flatChargesVisible(true);
        chargeRateVisible(false);
        divRoundOffVisible(false);
        panChargeTable.setVisible(false);
        panChargeBtn.setVisible(false);
        cboChargeBase.setEnabled(true);
    }
    if(chargeBase.equals("Amount Range")){
        slabAmountVisible(true);
        flatChargesVisible(false);
        chargeRateVisible(true);
        divRoundOffVisible(true);
        panChargeTable.setVisible(true);
        panChargeBtn.setVisible(true);
        btnChargeNew.setEnabled(true);
        btnChargeSave.setEnabled(false);
        btnChargeDelete.setEnabled(false);
        panChargeMainDetails.setEnabled(false);
        disableSlabDetails(false);
        txtFromSlabAmount.setEnabled(false);
        if(observable.getActionType() != ClientConstants.ACTIONTYPE_NEW){
            btnChargeNew.setEnabled(false);
            cboChargeBase.setEnabled(false);
        }
    }
}//GEN-LAST:event_cboChargeBaseActionPerformed

    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // TODO add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void mitCloseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mitCloseMouseClicked
        // TODO add your handling code here:
    }/**///GEN-LAST:event_mitCloseMouseClicked
                    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
                        // Add your handling code here:
                        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
                        updateAuthorizeStatus(CommonConstants.STATUS_EXCEPTION);
                        btnCancel.setEnabled(true);
                        btnReject.setEnabled(false);
                        btnAuthorize.setEnabled(false);
    }//GEN-LAST:event_btnExceptionActionPerformed
                    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        updateAuthorizeStatus(CommonConstants.STATUS_REJECTED);
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        updateAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnCancel.setEnabled(true);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    
    private void updateAuthorizeStatus(String authorizeStatus) {
        
        if (viewType == AUTHORIZE && isFilled){
            ArrayList arrList = new ArrayList();
            HashMap authorizeMap = new HashMap();
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("SCHEME_ID", observable.getCboSchemeId());
            singleAuthorizeMap.put("CHARGE_ID", observable.getTxtChargeId());
            singleAuthorizeMap.put("AUTHORIZED_BY", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("AUTHORIZED_DT",ClientUtil.getCurrentDateWithTime());
            arrList.add(singleAuthorizeMap);
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorize(authorizeMap,observable.getCboSchemeId());
            viewType = "";
            ClientUtil.enableDisable(panChargeMainDetails,false);
            //            super.setOpenForEditBy(observable.getStatusBy());
            singleAuthorizeMap = null;
            arrList = null;
            authorizeMap = null;
        } else {
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put("USER_ID", TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            mapParam.put(CommonConstants.MAP_NAME, "getARCChargeDetailAuthorize");
            isFilled = false;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
            observable.setStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }
    public void authorize(HashMap map,String id) {
        System.out.println("Authorize Map : " + map);
        
        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
            observable.set_authorizeMap(map);
            observable.doAction();
            if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED){
                //                super.setOpenForEditBy(observable.getStatusBy());
                //                super.removeEditLock(id);
            }
            btnCancelActionPerformed(null);
            observable.setStatus();
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        popUp("Delete");
        lblStatus.setText("Delete");
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        cboChargeType.setEnabled(false);
        ClientUtil.enableDisable(panChargeMainDetails,false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        setModified(false);
        ClientUtil.enableDisable(panChargeMainDetails,false);
        ClientUtil.clearAll(this);
        observable.resetTableValues();
        observable.resetForm();
//        rdoDeductionAccClose_no.setSelected(false);
//        rdoDeductionAccOpen_yes.setSelected(false);
        btnNew.setEnabled(true);
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        btnSave.setEnabled(false);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
        btnView.setEnabled(true);
//        btnAccGroupId.setEnabled(false);
        lblStatus.setText("               ");
        btnChargeNew.setEnabled(false);
        btnChargeSave.setEnabled(false);
        btnChargeDelete.setEnabled(false);
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        popUp("Edit");
        lblStatus.setText("Edit");
        ClientUtil.enableDisable(panChargeMainDetails,true);
        txtChargeId.setEnabled(false);
//        cboSchemeId.setEnabled(false);
        btnView.setEnabled(false);
        cboChargeType.setEnabled(false);
//        btnAccGroupId.setEnabled(true);
    }//GEN-LAST:event_btnEditActionPerformed
    
    /** This method helps in popoualting the data from the data base
     * @param Action the argument is passed according to the command issued
     */
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        ClientUtil.clearAll(this);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        setButtonEnableDisable();
        ClientUtil.enableDisable(panChargeMainDetails,true);
//        btnAccGroupId.setEnabled(true);
        txtChargeId.setEnabled(false);
        panChargeTable.setVisible(false);
        panChargeBtn.setVisible(false);
        lblStatus.setText(observable.getLblStatus());
        cboChargeType.setEnabled(true);
    }//GEN-LAST:event_btnNewActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        savePerformed();
    }//GEN-LAST:event_btnSaveActionPerformed
    private boolean checkMaxAmountRange(){
        String chargeBase =CommonUtil.convertObjToStr(cboChargeBase.getSelectedItem());
        boolean amtRange =false;
        if(chargeBase.equals("Amount Range") && (observable.getActionType() != ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE)){
            ArrayList totList =(ArrayList)observable.getTblCharge().getDataArrayList();
            for(int i=0;i<totList.size();i++){
                ArrayList singleList = (ArrayList)totList.get(i);
                double toAmt =CommonUtil.convertObjToDouble(singleList.get(2)).doubleValue();
                if(toAmt >= 999999999){
                    amtRange=true;
                    break;
                }
            }
            if(!amtRange){
                ClientUtil.showMessageWindow("Max Amount Range should be  Rs 999999999");
                return true;
            }
        }
        
       
       return false; 
    }
    private void savePerformed(){
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panChargeMainDetails, getMandatoryHashMap());
        if(checkMaxAmountRange()){
            return;
        }
        if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0 ){
            displayAlert(mandatoryMessage);
        } else {
            updateOBFields();
            observable.doAction();
            if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED){
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
    }
    
    
    /** To perform the appropriate operation */
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnChargeDelete;
    private com.see.truetransact.uicomponent.CButton btnChargeNew;
    private com.see.truetransact.uicomponent.CButton btnChargeSave;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboChargeBase;
    private com.see.truetransact.uicomponent.CComboBox cboChargeType;
    private com.see.truetransact.uicomponent.CComboBox cboRoundOffType;
    private com.see.truetransact.uicomponent.CLabel lbDivisibleBy;
    private com.see.truetransact.uicomponent.CLabel lblChargeBase;
    private com.see.truetransact.uicomponent.CLabel lblChargeBase1;
    private com.see.truetransact.uicomponent.CLabel lblChargeId;
    private com.see.truetransact.uicomponent.CLabel lblChargeRate;
    private com.see.truetransact.uicomponent.CLabel lblFlatCharges;
    private com.see.truetransact.uicomponent.CLabel lblFromSlabAmount;
    private com.see.truetransact.uicomponent.CLabel lblMaximumChrgAmt;
    private com.see.truetransact.uicomponent.CLabel lblMinimumChrgAmt;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblRoundOffType;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace56;
    private com.see.truetransact.uicomponent.CLabel lblSpace57;
    private com.see.truetransact.uicomponent.CLabel lblSpace58;
    private com.see.truetransact.uicomponent.CLabel lblSpace59;
    private com.see.truetransact.uicomponent.CLabel lblSpace6;
    private com.see.truetransact.uicomponent.CLabel lblSpace60;
    private com.see.truetransact.uicomponent.CLabel lblSpace61;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblToSlabAmount;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAccountHead;
    private com.see.truetransact.uicomponent.CPanel panChargeBtn;
    private com.see.truetransact.uicomponent.CPanel panChargeDetailTable;
    private com.see.truetransact.uicomponent.CPanel panChargeMainDetails;
    private com.see.truetransact.uicomponent.CPanel panChargeMainDetailsData;
    private com.see.truetransact.uicomponent.CPanel panChargeTable;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CButtonGroup rdoDeductionAcc;
    private com.see.truetransact.uicomponent.CButtonGroup rdoMandatory;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private com.see.truetransact.uicomponent.CScrollPane srpChargeTable;
    private com.see.truetransact.uicomponent.CTable tblCharge;
    private javax.swing.JToolBar tbrOperativeAcctProduct;
    private com.see.truetransact.uicomponent.CTextField txtChargeId;
    private com.see.truetransact.uicomponent.CTextField txtChargeRate;
    private com.see.truetransact.uicomponent.CTextField txtDivisibleBy;
    private com.see.truetransact.uicomponent.CTextField txtFlatCharges;
    private com.see.truetransact.uicomponent.CTextField txtFromSlabAmount;
    private com.see.truetransact.uicomponent.CTextField txtMaximumChrgAmt;
    private com.see.truetransact.uicomponent.CTextField txtMinimumChrgAmt;
    private com.see.truetransact.uicomponent.CTextField txtToSlabAmount;
    // End of variables declaration//GEN-END:variables
    
    
    public static void main(String args[]){
        javax.swing.JFrame f = new javax.swing.JFrame();
//        ChargesUI ch = new ChargesUI();
//        f.getContentPane().add(ch);
//        ch.show();
//        f.show();
    }
}
