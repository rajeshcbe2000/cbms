/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * EarningsDeductionUI.java
 *
 * 
 */
package com.see.truetransact.ui.payroll.earningsDeductionGlobal;

import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uicomponent.CTextField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.CurrencyValidation;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author anjuanand
 *
 */
public class EarningsDeductionUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer, UIMandatoryField {

    private EarningsDeductionOB observable;
    java.util.ResourceBundle objMandatoryRB;
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.payroll.earningsDeductionGlobal.EarningsDeductionRB", ProxyParameters.LANGUAGE);
    private HashMap mandatoryMap;
    final int EDIT = 0, DELETE = 1, VIEW = 2, ACCTHDID = 5;
    int viewType = -1;
    private boolean selectedAccSingleRow = false;
    private boolean selectedCalcSingleRow = false;
    int accSlNo = 1;
    int calcSlNo = 1;
    boolean isFilled = false;
    private String view = "";
    static String modType = "";
    static String calcModCode = "";
    boolean isclicked = false;
    int count = 0;
    boolean isGl = false;

    public EarningsDeductionUI() {
        initComponents();
        initStartup();
    }

    private void initStartup() {
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setObservable();
        setHelpMessage();
        setValidations();
        initComponentData();                    // Fill all the combo boxes...
        ClientUtil.enableDisable(this, false);  // Disables all when the screen appears for the 1st time
        setButtonEnableDisable();               // Enables/Disables the necessary buttons and menu items...
        enableDisableButtons(false);                 // To disable the buttons(folder) in the Starting...
        observable.resetForm();                 // To reset all the fields in UI...
        observable.resetStatus();               // To reset the Satus in the UI...
        objMandatoryRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.payroll.earningsDeductionGlobal.EarningsDeductionMRB", ProxyParameters.LANGUAGE);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panEarningsDeductionInfo);
        this.resetUIData();
        enableCalcSettingsTxt(false);
        
    }

    private void initComponentData() {
        txtAccountHead.setAllowNumber(true);
        txtAmount.setAllowNumber(true);
        txtMinAmt.setAllowNumber(true);
        txtMaxAmt.setAllowNumber(true);
        txtPercentage.setAllowNumber(true);
    }

    private void setProductType() {
        List prodTypeList = null;
        prodTypeList = observable.setProductType();
        cboProductType.setModel(observable.getCbmProductType());
        cboProdType.setModel(observable.getCbmProductType());
    }

    private void setObservable() {
        /*
         * Singleton pattern can't be implemented as there are two observers
         * using the same observable
         */
        observable = EarningsDeductionOB.getInstance(this);
        try {
            observable = new EarningsDeductionOB();
            observable.addObserver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetUIData() {
        this.viewType = -1;
    }

    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnSave.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        btnDelete.setEnabled(!btnDelete.isEnabled());
        btnAuthorize.setEnabled(!btnSave.isEnabled());
        btnReject.setEnabled(!btnSave.isEnabled());
        btnException.setEnabled(!btnSave.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
    }

    private void enableDisableButtons(boolean yesno) {
        btnAccountHead.setEnabled(yesno);
        lblProdType.setVisible(yesno);
        cboProdType.setVisible(yesno);
        lblAmount.setVisible(yesno);
        txtAmount.setVisible(yesno);
    }
    /*
     * Auto Generated Method - setFieldNames() This method assigns name for all
     * the components. Other functions are working based on this name.
     */

    private void setFieldNames() {
        tabEarnDedu.setName("tabEarnDedu");
        panEarnDedu.setName("panEarnDedu");
        panEarnDeduContra.setName("panEarnDeduContra");
        rdoEarnings.setName("rdoEarnings");
        rdoDeduction.setName("rdoDeduction");
        rdoContra.setName("rdoContra");
        panGlobalSettings.setName("panGlobalSettings");
        lblModuleType.setName("lblModuleType");
        cboModuleType.setName("cboModuleType");
        lblDescription.setName("lblDescription");
        txtDescription.setName("txtDescription");
        lblCalculationType.setName("lblCalculationType");
        cboCalculationType.setName("cboCalculationType");
        lblAmount.setName("lblAmount");
        txtAmount.setName("txtAmount");
        lblActive.setName("lblActive");
        chkActive.setName("chkActive");
        lblExcludeFromTax.setName("lblExcludeFromTax");
        chkExcludeFromTax.setName("chkExcludeFromTax");
        lblPaymentVoucherRequired.setName("lblPaymentVoucherRequired");
        chkPaymentVoucherRequired.setName("chkPaymentVoucherRequired");
        lblIndividualRequired.setName("lblIndividualRequired");
        chkIndividualRequired.setName("chkIndividualRequired");
        panAccountDetails.setName("panAccountDetails");
        panAccDet.setName("panAccDet");
        lblAccountNo.setName("lblAccountNo");
        panAccountNo.setName("panAccountNo");
        txtAccountHead.setName("txtAccountNo");
        btnAccountHead.setName("btnAccountNo");
        lblAccountType.setName("lblAccountType");
        cboAccountType.setName("cboAccountType");
        srpAccountDetails.setName("srpAccountDetails");
        tblAccountDetails.setName("tblAccountDetails");
        panAccSub.setName("panAccSub");
        btnAccSubNew.setName("btnAccSubNew");
        btnAccSubSave.setName("btnAccSubSave");
        btnAccSubDel.setName("btnAccSubDel");
        panCalculatedTypeSettings.setName("panCalculatedTypeSettings");
        panCalcSettings.setName("panCalcSettings");
        lblModType.setName("lblModType");
        cboCalcModType.setName("cboCalcModType");
        lblPercentage.setName("lblPercentage");
        txtPercentage.setName("txtPercentage");
        lblPercent.setName("lblPercent");
        lblMinAmt.setName("lblMinAmt");
        txtMinAmt.setName("txtMinAmt");
        lblMaxAmt.setName("lblMaxAmt");
        txtMaxAmt.setName("txtMaxAmt");
        lblIncludePersonalPay.setName("lblIncludePersonalPay");
        chkIncludePersonalPay.setName("chkIncludePersonalPay");
        srpCalcDetails.setName("srpCalcDetails");
        tblCalcDetails.setName("tblCalcDetails");
        panCalcSub.setName("panCalcSub");
        btnCalcSubNew.setName("btnCalcSubNew");
        btnCalcSubSave.setName("btnCalcSubSave");
        btnCalcSubDel.setName("btnCalcSubDel");
        panEarnDeduData.setName("panEarnDeduData");
        panEarningsDeduction.setName("panEarningsDeduction");
        lblSpace.setName("lblSpace");
        lblStatus.setName("lblStatus");
        lblMsg.setName("lblMsg");
        lblProductType.setName("lblProductType");
        cboProductType.setName("cboProductType");
        lblOnlyForContra.setName("lblOnlyForContra");
        chkOnlyForContra.setName("chkOnlyForContra");
        lblFromDate.setName("lblFromDate");
        tdtFromDate.setName("tdtFromDate");
        lblGl.setName("lblGl");
        chkGl.setName("chkGl");
        lblProdType.setName("lblProdType");
        cboProdType.setName("cboProdType");
    }

    /*
     * Auto Generated Method - internationalize() This method used to assign
     * display texts from the Resource Bundle File.
     */
    private void internationalize() {
        resourceBundle = new EarningsDeductionRB();
        rdoEarnings.setText(resourceBundle.getString("rdoEarnings"));
        rdoDeduction.setText(resourceBundle.getString("rdoDeduction"));
        rdoContra.setText(resourceBundle.getString("rdoContra"));
        lblModuleType.setText(resourceBundle.getString("lblModuleType"));
        lblDescription.setText(resourceBundle.getString("lblDescription"));
        lblCalculationType.setText(resourceBundle.getString("lblCalculationType"));
        lblAmount.setText(resourceBundle.getString("lblAmount"));
        lblActive.setText(resourceBundle.getString("lblActive"));
        lblExcludeFromTax.setText(resourceBundle.getString("lblExcludeFromTax"));
        lblPaymentVoucherRequired.setText(resourceBundle.getString("lblPaymentVoucherRequired"));
        lblIndividualRequired.setText(resourceBundle.getString("lblIndividualRequired"));
        lblAccountNo.setText(resourceBundle.getString("lblAccountNo"));
        lblAccountType.setText(resourceBundle.getString("lblAccountType"));
        lblModType.setText(resourceBundle.getString("lblModType"));
        lblPercentage.setText(resourceBundle.getString("lblPercentage"));
        lblPercent.setText(resourceBundle.getString("lblPercent"));
        lblIncludePersonalPay.setText(resourceBundle.getString("lblIncludePersonalPay"));
        lblMinAmt.setText(resourceBundle.getString("lblMinAmt"));
        lblMaxAmt.setText(resourceBundle.getString("lblMaxAmt"));
        lblProductType.setText(resourceBundle.getString("lblProductType"));
        lblOnlyForContra.setText(resourceBundle.getString("lblOnlyForContra"));
        lblFromDate.setText(resourceBundle.getString("lblFromDate"));
        lblGl.setText(resourceBundle.getString("lblGl"));
        lblProdType.setText(resourceBundle.getString("lblProdType"));
    }

    /*
     * Auto Generated Method - setMandatoryHashMap() This method list out all
     * the Input Fields available in the UI. It needs a class level HashMap
     * variable mandatoryMap.
     */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboModuleType", new Boolean(true));
        mandatoryMap.put("txtDescription", new Boolean(true));
        mandatoryMap.put("cboCalculationType", new Boolean(true));
        mandatoryMap.put("txtAmount", new Boolean(false));
        mandatoryMap.put("chkActive", new Boolean(false));
        mandatoryMap.put("chkExcludeFromTax", new Boolean(false));
        mandatoryMap.put("chkPaymentVoucherRequired", new Boolean(false));
        mandatoryMap.put("chkIndividualRequired", new Boolean(false));
        mandatoryMap.put("chkOnlyForContra", new Boolean(false));
        mandatoryMap.put("cboProductType", new Boolean(false));
        mandatoryMap.put("txtAccountHead", new Boolean(false));
        mandatoryMap.put("cboAccountType", new Boolean(false));
        mandatoryMap.put("txtMinAmt", new Boolean(false));
        mandatoryMap.put("txtMaxAmt", new Boolean(false));
        mandatoryMap.put("txtPercentage", new Boolean(false));
        mandatoryMap.put("chkIncludePersonalPay", new Boolean(false));
        mandatoryMap.put("tdtFromDate", new Boolean(false));
        mandatoryMap.put("chkGl", new Boolean(false));
        mandatoryMap.put("cboCalcModType", new Boolean(false));
        mandatoryMap.put("cboProdType", new Boolean(false));
    }

    /*
     * Auto Generated Method - getMandatoryHashMap() Getter method for
     * setMandatoryHashMap().
     */
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    /*
     * Auto Generated Method - setHelpMessage() This method shows tooltip help
     * for all the input fields available in the UI. It needs the Mandatory
     * Resource Bundle object. Help display Label name should be lblMsg.
     */
    public void setHelpMessage() {
        EarningsDeductionMRB objMandatoryRB = new EarningsDeductionMRB();
        rdoEarnings.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoEarnings"));
        rdoDeduction.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoDeduction"));
        rdoContra.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoContra"));
        cboModuleType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboModuleType"));
        txtDescription.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDescription"));
        cboCalculationType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCalculationType"));
        txtAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAmount"));
        txtAccountHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAccountNo"));
        cboAccountType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboAccountType"));
        cboProductType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProductType"));
        cboCalcModType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCalcModType"));
        txtPercentage.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPercentage"));
        txtMinAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMinAmt"));
        txtMaxAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMaxAmt"));
        tdtFromDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtFromDate"));
        chkGl.setHelpMessage(lblMsg, objMandatoryRB.getString("chkGl"));
        cboProdType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProdType"));
    }

    /*
     * Auto Generated Method - update() This method called by Observable. It
     * updates the UI with Observable's data. If needed add/Remove RadioButtons
     * method need to be added.
     */
    public void update(Observable observed, Object arg) {
        if (observable.isRdoEarnings()) {
            rdoEarnings.setSelected(true);
        } else if (observable.isRdoDeduction()) {
            rdoDeduction.setSelected(true);
        } else if (observable.isRdoContra()) {
            rdoContra.setSelected(true);
        }
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
            HashMap payMap = new HashMap();
            if (observable.isRdoEarnings()) {
                payMap.put("LOOKUP_ID", "ELIGIBLE_ALLOWANCE");
            } else {
                payMap.put("LOOKUP_ID", "DEDUCTIONS");
            }
            observable.setPayModType(payMap);
            cboModuleType.setModel(observable.getCbmModuleType());
            setProductType();
        }
        cboModuleType.setSelectedItem(observable.getCboModuleType());
        txtDescription.setText(observable.getTxtDescription());
        tdtFromDate.setDateValue(observable.getTdtFromDate());
        if (observable.isChkActive()) {
            chkActive.setSelected(true);
        } else {
            chkActive.setSelected(false);
        }
        if (observable.isChkExcludeFromTax()) {
            chkExcludeFromTax.setSelected(true);
        } else {
            chkExcludeFromTax.setSelected(false);
        }
        if (observable.isChkIndividualRequired()) {
            chkIndividualRequired.setSelected(false);
        } else {
            chkIndividualRequired.setSelected(true);
        }
        if (observable.isChkOnlyForContra()) {
            chkOnlyForContra.setSelected(true);
        } else {
            chkOnlyForContra.setSelected(false);
        }
        if (observable.isChkPaymentVoucherRequired()) {
            chkPaymentVoucherRequired.setSelected(true);
        } else {
            chkPaymentVoucherRequired.setSelected(false);
        }
        if (observable.isRdoEarnings() || observable.isRdoDeduction() || observable.isRdoContra()) {
            this.updateAccTable();
        }
        cboCalculationType.setSelectedItem(CommonUtil.convertObjToStr(observable.getCboCalculationType()));
        if (cboCalculationType.getSelectedItem().equals("Fixed")) {
            txtAmount.setText(CommonUtil.convertObjToStr(observable.getTxtAmount()));
        } else if (cboCalculationType.getSelectedItem().equals("Calculated")) {
            txtMinAmt.setText(CommonUtil.convertObjToStr(observable.getTxtMinAmt()));
            txtMaxAmt.setText(CommonUtil.convertObjToStr(observable.getTxtMaxAmt()));
            txtPercentage.setText(CommonUtil.convertObjToStr(observable.getTxtPercentage()));
            if (observable.isChkIncludePersonalPay()) {
                chkIncludePersonalPay.setSelected(true);
            }
            this.updateCalcTable();
            if (isclicked) {
                cboCalcModType.setModel(observable.getCbmCalcModType());
                cboCalcModType.setSelectedItem(CommonUtil.convertObjToStr(observable.getCboCalcModType()));
            }
        }
        lblStatus.setText(observable.getLblStatus());
        cboAccountType.setSelectedItem(CommonUtil.convertObjToStr(observable.getCboAccountType()));
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
            cboProductType.setSelectedItem(observable.getCbmProductType().getDataForKey(observable.getCboProductType()));
            cboProdType.setSelectedItem(observable.getCbmProductType().getDataForKey(observable.getCboProductType()));
            if (!observable.getCboProductType().equals(CommonConstants.GL_TRANSMODE_TYPE)) {
                chkGl.setSelected(false);
                panAccountDetails.setVisible(false);
                lblProdType.setVisible(true);
                cboProdType.setVisible(true);
                lblAmount.setVisible(false);
                txtAmount.setVisible(false);
            } else {
                chkGl.setSelected(true);
                panAccountDetails.setVisible(true);
                btnAccountHead.setEnabled(true);
                txtAccountHead.setText(observable.getTxtAccountHead());
                btnAccSubSave.setEnabled(true);
                lblProdType.setVisible(false);
                cboProdType.setVisible(false);
                cboAccountType.setEnabled(false);
            }
        }
    }

    /*
     * Auto Generated Method - updateOBFields() This method called by Save
     * option of UI. It updates the OB with UI data.
     */
    public void updateOBFields() {
        observable.setCboModuleType(CommonUtil.convertObjToStr(cboModuleType.getSelectedItem()));
        observable.setTxtDescription(txtDescription.getText());
        observable.setCboCalculationType(CommonUtil.convertObjToStr(cboCalculationType.getSelectedItem()));
        observable.setTxtAmount(txtAmount.getText());
        observable.setCboAccountType(CommonUtil.convertObjToStr(cboAccountType.getSelectedItem()));
        observable.setTxtAccountHead(txtAccountHead.getText());
        observable.setCboCalcModType(CommonUtil.convertObjToStr(cboCalcModType.getSelectedItem()));
        observable.setTxtMinAmt(txtMinAmt.getText());
        observable.setTxtMaxAmt(txtMaxAmt.getText());
        observable.setTxtPercentage(txtPercentage.getText());
        observable.setTdtFromDate(tdtFromDate.getDateValue());
        if (rdoEarnings.isSelected()) {
            observable.setRdoEarnings(true);
        } else if (rdoDeduction.isSelected()) {
            observable.setRdoDeduction(true);
        } else if (rdoContra.isSelected()) {
            observable.setRdoContra(true);
        }
        if (chkActive.isSelected()) {
            observable.setChkActive(true);
        } else {
            observable.setChkActive(false);
        }
        if (chkExcludeFromTax.isSelected()) {
            observable.setChkExcludeFromTax(true);
        } else {
            observable.setChkExcludeFromTax(false);
        }
        if (chkPaymentVoucherRequired.isSelected()) {
            observable.setChkPaymentVoucherRequired(true);
        } else {
            observable.setChkPaymentVoucherRequired(false);
        }
        if (chkIndividualRequired.isSelected()) {
            observable.setChkIndividualRequired(false);
        } else {
            observable.setChkIndividualRequired(true);
        }
        if (chkIncludePersonalPay.isSelected()) {
            observable.setChkIncludePersonalPay(true);
        } else {
            observable.setChkIncludePersonalPay(false);
        }
        if (chkOnlyForContra.isSelected()) {
            observable.setChkOnlyForContra(true);
        } else {
            observable.setChkOnlyForContra(false);
        }
    }

    private void setValidations() {
        txtAmount.setValidation(new CurrencyValidation(14, 2));
        txtMinAmt.setValidation(new CurrencyValidation(14, 2));
        txtMaxAmt.setValidation(new CurrencyValidation(14, 2));
        txtPercentage.setHorizontalAlignment(CTextField.RIGHT);
    }

    public static void main(String args[]) {
        javax.swing.JFrame frame = new javax.swing.JFrame();
        frame.setSize(1200, 630);
        frame.show();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoPayTypeGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        panEarningsDeduction = new com.see.truetransact.uicomponent.CPanel();
        tabEarnDedu = new com.see.truetransact.uicomponent.CTabbedPane();
        panEarningsDeductionInfo = new com.see.truetransact.uicomponent.CPanel();
        panEarnDedu = new com.see.truetransact.uicomponent.CPanel();
        panGlobalSettings = new com.see.truetransact.uicomponent.CPanel();
        lblExcludeFromTax = new com.see.truetransact.uicomponent.CLabel();
        lblPaymentVoucherRequired = new com.see.truetransact.uicomponent.CLabel();
        cboCalculationType = new com.see.truetransact.uicomponent.CComboBox();
        lblActive = new com.see.truetransact.uicomponent.CLabel();
        lblIndividualRequired = new com.see.truetransact.uicomponent.CLabel();
        txtDescription = new com.see.truetransact.uicomponent.CTextField();
        lblDescription = new com.see.truetransact.uicomponent.CLabel();
        chkPaymentVoucherRequired = new com.see.truetransact.uicomponent.CCheckBox();
        chkExcludeFromTax = new com.see.truetransact.uicomponent.CCheckBox();
        chkActive = new com.see.truetransact.uicomponent.CCheckBox();
        chkIndividualRequired = new com.see.truetransact.uicomponent.CCheckBox();
        lblCalculationType = new com.see.truetransact.uicomponent.CLabel();
        lblModuleType = new com.see.truetransact.uicomponent.CLabel();
        lblAmount = new com.see.truetransact.uicomponent.CLabel();
        txtAmount = new com.see.truetransact.uicomponent.CTextField();
        lblOnlyForContra = new com.see.truetransact.uicomponent.CLabel();
        chkOnlyForContra = new com.see.truetransact.uicomponent.CCheckBox();
        cboModuleType = new com.see.truetransact.uicomponent.CComboBox();
        lblGl = new com.see.truetransact.uicomponent.CLabel();
        chkGl = new com.see.truetransact.uicomponent.CCheckBox();
        lblProdType = new com.see.truetransact.uicomponent.CLabel();
        cboProdType = new com.see.truetransact.uicomponent.CComboBox();
        panEarnDeduContra = new com.see.truetransact.uicomponent.CPanel();
        rdoEarnings = new com.see.truetransact.uicomponent.CRadioButton();
        rdoDeduction = new com.see.truetransact.uicomponent.CRadioButton();
        rdoContra = new com.see.truetransact.uicomponent.CRadioButton();
        panEarnDeduData = new com.see.truetransact.uicomponent.CPanel();
        panCalculatedTypeSettings = new com.see.truetransact.uicomponent.CPanel();
        srpCalcDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblCalcDetails = new com.see.truetransact.uicomponent.CTable();
        panCalcSub = new com.see.truetransact.uicomponent.CPanel();
        btnCalcSubNew = new com.see.truetransact.uicomponent.CButton();
        btnCalcSubSave = new com.see.truetransact.uicomponent.CButton();
        btnCalcSubDel = new com.see.truetransact.uicomponent.CButton();
        panCalcSettings = new com.see.truetransact.uicomponent.CPanel();
        lblModType = new com.see.truetransact.uicomponent.CLabel();
        lblMinAmt = new com.see.truetransact.uicomponent.CLabel();
        txtMinAmt = new com.see.truetransact.uicomponent.CTextField();
        lblMaxAmt = new com.see.truetransact.uicomponent.CLabel();
        txtPercentage = new com.see.truetransact.uicomponent.CTextField();
        lblPercentage = new com.see.truetransact.uicomponent.CLabel();
        txtMaxAmt = new com.see.truetransact.uicomponent.CTextField();
        cboCalcModType = new com.see.truetransact.uicomponent.CComboBox();
        lblIncludePersonalPay = new com.see.truetransact.uicomponent.CLabel();
        chkIncludePersonalPay = new com.see.truetransact.uicomponent.CCheckBox();
        lblPercent = new com.see.truetransact.uicomponent.CLabel();
        lblFromDate = new com.see.truetransact.uicomponent.CLabel();
        tdtFromDate = new com.see.truetransact.uicomponent.CDateField();
        panAccountDetails = new com.see.truetransact.uicomponent.CPanel();
        srpAccountDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblAccountDetails = new com.see.truetransact.uicomponent.CTable();
        panAccSub = new com.see.truetransact.uicomponent.CPanel();
        btnAccSubNew = new com.see.truetransact.uicomponent.CButton();
        btnAccSubSave = new com.see.truetransact.uicomponent.CButton();
        btnAccSubDel = new com.see.truetransact.uicomponent.CButton();
        panAccDet = new com.see.truetransact.uicomponent.CPanel();
        lblAccountType = new com.see.truetransact.uicomponent.CLabel();
        lblAccountNo = new com.see.truetransact.uicomponent.CLabel();
        panAccountNo = new com.see.truetransact.uicomponent.CPanel();
        txtAccountHead = new com.see.truetransact.uicomponent.CTextField();
        btnAccountHead = new com.see.truetransact.uicomponent.CButton();
        cboAccountType = new com.see.truetransact.uicomponent.CComboBox();
        cboProductType = new com.see.truetransact.uicomponent.CComboBox();
        lblProductType = new com.see.truetransact.uicomponent.CLabel();
        tbrEarnDedu = new javax.swing.JToolBar();
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
        lblSpace = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        mbrEarnDedu = new com.see.truetransact.uicomponent.CMenuBar();
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
        setTitle("EarningsDeduction");
        setMinimumSize(new java.awt.Dimension(1100, 650));
        setPreferredSize(new java.awt.Dimension(1100, 650));

        panEarningsDeduction.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panEarningsDeduction.setMinimumSize(new java.awt.Dimension(1000, 490));
        panEarningsDeduction.setPreferredSize(new java.awt.Dimension(1000, 490));
        panEarningsDeduction.setLayout(new java.awt.GridBagLayout());

        tabEarnDedu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabEarnDeduMouseClicked(evt);
            }
        });
        tabEarnDedu.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabEarnDeduStateChanged(evt);
            }
        });
        tabEarnDedu.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tabEarnDeduFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tabEarnDeduFocusLost(evt);
            }
        });

        panEarningsDeductionInfo.setMinimumSize(new java.awt.Dimension(1000, 350));
        panEarningsDeductionInfo.setPreferredSize(new java.awt.Dimension(1000, 1000));
        panEarningsDeductionInfo.setLayout(new java.awt.GridBagLayout());

        panEarnDedu.setMaximumSize(new java.awt.Dimension(800, 170));
        panEarnDedu.setMinimumSize(new java.awt.Dimension(800, 200));
        panEarnDedu.setPreferredSize(new java.awt.Dimension(1000, 1000));
        panEarnDedu.setLayout(new java.awt.GridBagLayout());

        panGlobalSettings.setBorder(javax.swing.BorderFactory.createTitledBorder("Global Settings"));
        panGlobalSettings.setMinimumSize(new java.awt.Dimension(700, 168));
        panGlobalSettings.setPreferredSize(new java.awt.Dimension(700, 500));
        panGlobalSettings.setLayout(new java.awt.GridBagLayout());

        lblExcludeFromTax.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblExcludeFromTax.setText("Exclude From Tax");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 100, 0, 0);
        panGlobalSettings.add(lblExcludeFromTax, gridBagConstraints);

        lblPaymentVoucherRequired.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPaymentVoucherRequired.setText("Payment Voucher Required");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 100, 0, 0);
        panGlobalSettings.add(lblPaymentVoucherRequired, gridBagConstraints);

        cboCalculationType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "        ", "Fixed", "Calculated" }));
        cboCalculationType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCalculationType.setNextFocusableComponent(txtAmount);
        cboCalculationType.setPopupWidth(150);
        cboCalculationType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCalculationTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 0);
        panGlobalSettings.add(cboCalculationType, gridBagConstraints);

        lblActive.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblActive.setText("Active");
        lblActive.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 100, 0, 0);
        panGlobalSettings.add(lblActive, gridBagConstraints);

        lblIndividualRequired.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblIndividualRequired.setText("Individual Required");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 100, 0, 0);
        panGlobalSettings.add(lblIndividualRequired, gridBagConstraints);

        txtDescription.setMaximumSize(new java.awt.Dimension(100, 21));
        txtDescription.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDescription.setNextFocusableComponent(cboCalculationType);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 0);
        panGlobalSettings.add(txtDescription, gridBagConstraints);

        lblDescription.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDescription.setText("Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        panGlobalSettings.add(lblDescription, gridBagConstraints);

        chkPaymentVoucherRequired.setNextFocusableComponent(chkIndividualRequired);
        chkPaymentVoucherRequired.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                chkPaymentVoucherRequiredKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panGlobalSettings.add(chkPaymentVoucherRequired, gridBagConstraints);

        chkExcludeFromTax.setNextFocusableComponent(chkPaymentVoucherRequired);
        chkExcludeFromTax.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                chkExcludeFromTaxKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panGlobalSettings.add(chkExcludeFromTax, gridBagConstraints);

        chkActive.setNextFocusableComponent(chkExcludeFromTax);
        chkActive.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                chkActiveKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panGlobalSettings.add(chkActive, gridBagConstraints);

        chkIndividualRequired.setNextFocusableComponent(chkOnlyForContra);
        chkIndividualRequired.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                chkIndividualRequiredKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panGlobalSettings.add(chkIndividualRequired, gridBagConstraints);

        lblCalculationType.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCalculationType.setText("Calculation Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        panGlobalSettings.add(lblCalculationType, gridBagConstraints);

        lblModuleType.setText("Module Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        panGlobalSettings.add(lblModuleType, gridBagConstraints);

        lblAmount.setText("Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        panGlobalSettings.add(lblAmount, gridBagConstraints);

        txtAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAmount.setNextFocusableComponent(chkActive);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 0);
        panGlobalSettings.add(txtAmount, gridBagConstraints);

        lblOnlyForContra.setText("Only For Contra");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 100, 0, 0);
        panGlobalSettings.add(lblOnlyForContra, gridBagConstraints);

        chkOnlyForContra.setNextFocusableComponent(btnAccSubNew);
        chkOnlyForContra.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                chkOnlyForContraKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panGlobalSettings.add(chkOnlyForContra, gridBagConstraints);

        cboModuleType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboModuleType.setPopupWidth(150);
        cboModuleType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboModuleTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 0);
        panGlobalSettings.add(cboModuleType, gridBagConstraints);

        lblGl.setText("GL");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 100, 0, 0);
        panGlobalSettings.add(lblGl, gridBagConstraints);

        chkGl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkGlActionPerformed(evt);
            }
        });
        chkGl.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                chkGlKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panGlobalSettings.add(chkGl, gridBagConstraints);

        lblProdType.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        panGlobalSettings.add(lblProdType, gridBagConstraints);

        cboProdType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdType.setPopupWidth(150);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 0);
        panGlobalSettings.add(cboProdType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.ipadx = -26;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 5, 0);
        panEarnDedu.add(panGlobalSettings, gridBagConstraints);

        panEarnDeduContra.setLayout(new java.awt.GridBagLayout());

        rdoPayTypeGroup.add(rdoEarnings);
        rdoEarnings.setText("Earnings");
        rdoEarnings.setNextFocusableComponent(rdoDeduction);
        rdoEarnings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoEarningsActionPerformed(evt);
            }
        });
        rdoEarnings.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                rdoEarningsKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        panEarnDeduContra.add(rdoEarnings, gridBagConstraints);

        rdoPayTypeGroup.add(rdoDeduction);
        rdoDeduction.setText("Deduction");
        rdoDeduction.setNextFocusableComponent(rdoContra);
        rdoDeduction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDeductionActionPerformed(evt);
            }
        });
        rdoDeduction.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                rdoDeductionKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        panEarnDeduContra.add(rdoDeduction, gridBagConstraints);

        rdoPayTypeGroup.add(rdoContra);
        rdoContra.setText("Contribution");
        rdoContra.setNextFocusableComponent(cboModuleType);
        rdoContra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoContraActionPerformed(evt);
            }
        });
        rdoContra.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                rdoContraKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panEarnDeduContra.add(rdoContra, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.ipady = 3;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEarnDedu.add(panEarnDeduContra, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        panEarningsDeductionInfo.add(panEarnDedu, gridBagConstraints);

        panEarnDeduData.setMaximumSize(new java.awt.Dimension(465, 525));
        panEarnDeduData.setMinimumSize(new java.awt.Dimension(950, 300));
        panEarnDeduData.setPreferredSize(new java.awt.Dimension(1000, 1000));
        panEarnDeduData.setLayout(new java.awt.GridBagLayout());

        panCalculatedTypeSettings.setBorder(javax.swing.BorderFactory.createTitledBorder("Calculated Type Settings"));
        panCalculatedTypeSettings.setMinimumSize(new java.awt.Dimension(470, 300));
        panCalculatedTypeSettings.setPreferredSize(new java.awt.Dimension(500, 500));
        panCalculatedTypeSettings.setLayout(new java.awt.GridBagLayout());

        srpCalcDetails.setNextFocusableComponent(btnAuthorize);

        tblCalcDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sl No.", "PayCode", "Module Type"
            }
        ));
        tblCalcDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCalcDetailsMouseClicked(evt);
            }
        });
        srpCalcDetails.setViewportView(tblCalcDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 321;
        gridBagConstraints.ipady = 52;
        gridBagConstraints.insets = new java.awt.Insets(7, 6, 17, 16);
        panCalculatedTypeSettings.add(srpCalcDetails, gridBagConstraints);

        panCalcSub.setMinimumSize(new java.awt.Dimension(250, 26));
        panCalcSub.setPreferredSize(new java.awt.Dimension(250, 26));
        panCalcSub.setLayout(new java.awt.GridBagLayout());

        btnCalcSubNew.setText("New");
        btnCalcSubNew.setNextFocusableComponent(cboCalcModType);
        btnCalcSubNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalcSubNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCalcSub.add(btnCalcSubNew, gridBagConstraints);

        btnCalcSubSave.setText("Save");
        btnCalcSubSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalcSubSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCalcSub.add(btnCalcSubSave, gridBagConstraints);

        btnCalcSubDel.setText("Delete");
        btnCalcSubDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalcSubDelActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCalcSub.add(btnCalcSubDel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        panCalculatedTypeSettings.add(panCalcSub, gridBagConstraints);

        panCalcSettings.setMinimumSize(new java.awt.Dimension(440, 100));
        panCalcSettings.setPreferredSize(new java.awt.Dimension(500, 500));
        panCalcSettings.setLayout(new java.awt.GridBagLayout());

        lblModType.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblModType.setText("Module Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        panCalcSettings.add(lblModType, gridBagConstraints);

        lblMinAmt.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblMinAmt.setText("Min Amt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        panCalcSettings.add(lblMinAmt, gridBagConstraints);

        txtMinAmt.setMaximumSize(new java.awt.Dimension(100, 21));
        txtMinAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMinAmt.setNextFocusableComponent(txtPercentage);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panCalcSettings.add(txtMinAmt, gridBagConstraints);

        lblMaxAmt.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblMaxAmt.setText("Max Amt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        panCalcSettings.add(lblMaxAmt, gridBagConstraints);

        txtPercentage.setMaximumSize(new java.awt.Dimension(100, 21));
        txtPercentage.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPercentage.setNextFocusableComponent(txtMaxAmt);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 0);
        panCalcSettings.add(txtPercentage, gridBagConstraints);

        lblPercentage.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPercentage.setText("Percentage");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        panCalcSettings.add(lblPercentage, gridBagConstraints);

        txtMaxAmt.setMaximumSize(new java.awt.Dimension(100, 21));
        txtMaxAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMaxAmt.setNextFocusableComponent(chkIncludePersonalPay);
        txtMaxAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMaxAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panCalcSettings.add(txtMaxAmt, gridBagConstraints);

        cboCalcModType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCalcModType.setNextFocusableComponent(txtMinAmt);
        cboCalcModType.setPopupWidth(150);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 0);
        panCalcSettings.add(cboCalcModType, gridBagConstraints);

        lblIncludePersonalPay.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblIncludePersonalPay.setText("Include Personal Pay");
        lblIncludePersonalPay.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        panCalcSettings.add(lblIncludePersonalPay, gridBagConstraints);

        chkIncludePersonalPay.setNextFocusableComponent(btnCalcSubSave);
        chkIncludePersonalPay.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                chkIncludePersonalPayFocusLost(evt);
            }
        });
        chkIncludePersonalPay.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                chkIncludePersonalPayKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 82);
        panCalcSettings.add(chkIncludePersonalPay, gridBagConstraints);

        lblPercent.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        panCalcSettings.add(lblPercent, gridBagConstraints);

        lblFromDate.setText("From Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        panCalcSettings.add(lblFromDate, gridBagConstraints);

        tdtFromDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtFromDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panCalcSettings.add(tdtFromDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panCalculatedTypeSettings.add(panCalcSettings, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        panEarnDeduData.add(panCalculatedTypeSettings, gridBagConstraints);

        panAccountDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Account Details"));
        panAccountDetails.setMinimumSize(new java.awt.Dimension(450, 300));
        panAccountDetails.setPreferredSize(new java.awt.Dimension(500, 500));
        panAccountDetails.setLayout(new java.awt.GridBagLayout());

        srpAccountDetails.setNextFocusableComponent(btnAuthorize);

        tblAccountDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sl No.", "Account Head", "Account Type"
            }
        ));
        tblAccountDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblAccountDetailsMouseClicked(evt);
            }
        });
        srpAccountDetails.setViewportView(tblAccountDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 321;
        gridBagConstraints.ipady = 52;
        gridBagConstraints.insets = new java.awt.Insets(7, 6, 17, 16);
        panAccountDetails.add(srpAccountDetails, gridBagConstraints);

        panAccSub.setMinimumSize(new java.awt.Dimension(250, 26));
        panAccSub.setPreferredSize(new java.awt.Dimension(250, 26));
        panAccSub.setLayout(new java.awt.GridBagLayout());

        btnAccSubNew.setText("New");
        btnAccSubNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccSubNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAccSub.add(btnAccSubNew, gridBagConstraints);

        btnAccSubSave.setText("Save");
        btnAccSubSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccSubSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAccSub.add(btnAccSubSave, gridBagConstraints);

        btnAccSubDel.setText("Delete");
        btnAccSubDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccSubDelActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAccSub.add(btnAccSubDel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        panAccountDetails.add(panAccSub, gridBagConstraints);

        panAccDet.setMinimumSize(new java.awt.Dimension(430, 100));
        panAccDet.setPreferredSize(new java.awt.Dimension(500, 100));
        panAccDet.setLayout(new java.awt.GridBagLayout());

        lblAccountType.setText("Account Type");
        lblAccountType.setName("lblAccountType"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        panAccDet.add(lblAccountType, gridBagConstraints);

        lblAccountNo.setText("Account Head");
        lblAccountNo.setName("lblAccountNo"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 0, 0);
        panAccDet.add(lblAccountNo, gridBagConstraints);

        panAccountNo.setMinimumSize(new java.awt.Dimension(130, 33));
        panAccountNo.setPreferredSize(new java.awt.Dimension(130, 33));
        panAccountNo.setLayout(new java.awt.GridBagLayout());

        txtAccountHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAccountHead.setName("txtAccountHead"); // NOI18N
        txtAccountHead.setMaxLength(10);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccountNo.add(txtAccountHead, gridBagConstraints);

        btnAccountHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccountHead.setMaximumSize(new java.awt.Dimension(25, 25));
        btnAccountHead.setMinimumSize(new java.awt.Dimension(25, 25));
        btnAccountHead.setName("btnAccountHead"); // NOI18N
        btnAccountHead.setNextFocusableComponent(btnAccSubSave);
        btnAccountHead.setPreferredSize(new java.awt.Dimension(25, 25));
        btnAccountHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccountHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panAccountNo.add(btnAccountHead, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panAccDet.add(panAccountNo, gridBagConstraints);

        cboAccountType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "       ", "Debit", "Credit" }));
        cboAccountType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panAccDet.add(cboAccountType, gridBagConstraints);

        cboProductType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProductType.setPopupWidth(150);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panAccDet.add(cboProductType, gridBagConstraints);

        lblProductType.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        panAccDet.add(lblProductType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panAccountDetails.add(panAccDet, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panEarnDeduData.add(panAccountDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        panEarningsDeductionInfo.add(panEarnDeduData, gridBagConstraints);

        tabEarnDedu.addTab("Earnings/Deduction", panEarningsDeductionInfo);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panEarningsDeduction.add(tabEarnDedu, gridBagConstraints);
        tabEarnDedu.getAccessibleContext().setAccessibleName("Earnings/Deduction");

        getContentPane().add(panEarningsDeduction, java.awt.BorderLayout.CENTER);

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setEnabled(false);
        btnView.setFocusable(false);
        btnView.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView.setPreferredSize(new java.awt.Dimension(21, 21));
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });
        tbrEarnDedu.add(btnView);

        lblSpace5.setText("     ");
        tbrEarnDedu.add(lblSpace5);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.setNextFocusableComponent(rdoEarnings);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrEarnDedu.add(btnNew);

        lblSpace17.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace17.setText("     ");
        lblSpace17.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrEarnDedu.add(lblSpace17);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.setFocusable(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrEarnDedu.add(btnEdit);

        lblSpace18.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace18.setText("     ");
        lblSpace18.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace18.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace18.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrEarnDedu.add(lblSpace18);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrEarnDedu.add(btnDelete);

        lblSpace2.setText("     ");
        tbrEarnDedu.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.setFocusable(false);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrEarnDedu.add(btnSave);

        lblSpace19.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace19.setText("     ");
        lblSpace19.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace19.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace19.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrEarnDedu.add(lblSpace19);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.setEnabled(false);
        btnCancel.setFocusable(false);
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrEarnDedu.add(btnCancel);

        lblSpace3.setText("     ");
        tbrEarnDedu.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        tbrEarnDedu.add(btnAuthorize);

        lblSpace20.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace20.setText("     ");
        lblSpace20.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace20.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace20.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrEarnDedu.add(lblSpace20);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        tbrEarnDedu.add(btnException);

        lblSpace21.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace21.setText("     ");
        lblSpace21.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrEarnDedu.add(lblSpace21);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrEarnDedu.add(btnReject);

        lblSpace4.setText("     ");
        tbrEarnDedu.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrEarnDedu.add(btnPrint);

        lblSpace22.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace22.setText("     ");
        lblSpace22.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrEarnDedu.add(lblSpace22);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrEarnDedu.add(btnClose);

        getContentPane().add(tbrEarnDedu, java.awt.BorderLayout.NORTH);

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

        getContentPane().add(panStatus, java.awt.BorderLayout.SOUTH);

        mnuProcess.setText("Process");
        mnuProcess.setName("mnuProcess"); // NOI18N

        mitNew.setText("New");
        mitNew.setName("mitNew"); // NOI18N
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mitEdit.setName("mitEdit"); // NOI18N
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mitDelete.setName("mitDelete"); // NOI18N
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);

        sptNew.setName("sptNew"); // NOI18N
        mnuProcess.add(sptNew);

        mitSave.setText("Save");
        mitSave.setName("mitSave"); // NOI18N
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancel");
        mitCancel.setName("mitCancel"); // NOI18N
        mnuProcess.add(mitCancel);

        sptSave.setName("sptSave"); // NOI18N
        mnuProcess.add(sptSave);

        mitPrint.setText("Print");
        mitPrint.setName("mitPrint"); // NOI18N
        mnuProcess.add(mitPrint);

        mitClose.setText("Close");
        mitClose.setName("mitClose"); // NOI18N
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrEarnDedu.add(mnuProcess);

        setJMenuBar(mbrEarnDedu);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed

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
    public void setLables() {
    }

    private void tabEarnDeduMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabEarnDeduMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tabEarnDeduMouseClicked

    private void tabEarnDeduStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabEarnDeduStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_tabEarnDeduStateChanged

    private void tabEarnDeduFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tabEarnDeduFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tabEarnDeduFocusLost

    private void tabEarnDeduFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tabEarnDeduFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_tabEarnDeduFocusGained

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        modType = "";
        enableRadioBtns(true);
        enableAccDetTxt(false);
        enableCalcSettingsTxt(false);
        btnAccSubNew.setEnabled(false);
        observable.resetOBFields();
        setButtonEnableDisable();
        panGlobalSettings.setEnabled(false);
        chkIncludePersonalPay.setEnabled(false);
        resetTxtFields();
        resetRadioBtns();
        txtAmount.setEnabled(false);
        cboProductType.setEnabled(false);
        cboProdType.setEnabled(false);
        cboAccountType.setEnabled(false);
        lblAccountType.setVisible(true);
        cboAccountType.setVisible(true);
        enableAccTbl(true);
        txtMaxAmt.setFocusable(true);
        setModified(true);
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        observable.resetForm();// Reset the fields in the UI to null...
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);//Sets the Action Type to be performed...
        lblStatus.setText("Edit");
        panAccountDetails.setVisible(true);
        panCalculatedTypeSettings.setVisible(true);
        callView(ClientConstants.ACTIONTYPE_EDIT);
        btnSave.setEnabled(true);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
        tblCalcDetails.setEnabled(true);
        tblAccountDetails.setEnabled(true);
        txtMaxAmt.setFocusable(true);
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        if (viewType == ClientConstants.ACTIONTYPE_DELETE && isFilled == false) {
            view = "DELETE";
            isFilled = true;
            observable.doAction();
            lblStatus.setText("Deleted");
            resetTxtFields();
            resetRadioBtns();
            enableTxtFields(false);
            enableAccDetTxt(false);
            enableCalcSettingsTxt(false);
            tblAccountDetails.revalidate();
            tblCalcDetails.revalidate();
            observable.resetForm();
            observable.setAccountTable();
            observable.setCalcTable();
            tblAccountDetails.setModel(observable.getTbmAccountDetails());
            tblCalcDetails.setModel(observable.getTbmCalcDetails());
            btnCalcSubNew.setEnabled(false);
            btnAccSubNew.setEnabled(false);
            enableAccTbl(true);
            cboAccountType.setVisible(true);
            cboAccountType.setEditable(false);
        } else {
            observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
            lblStatus.setText("Delete");
            callView(ClientConstants.ACTIONTYPE_DELETE);
            panAccountDetails.setVisible(true);
            panCalculatedTypeSettings.setVisible(true);
            btnDelete.setEnabled(true);
            btnNew.setEnabled(false);
            btnEdit.setEnabled(false);
            btnCancel.setEnabled(true);
            txtAmount.setEditable(false);
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        final String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panEarningsDeductionInfo);
        if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0) {
            displayAlert(mandatoryMessage);
        } else {
            int i = tblAccountDetails.getRowCount();
            int j = tblCalcDetails.getRowCount();
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                if (!cboModuleType.getSelectedItem().equals("BASICPAY") && cboCalculationType.getSelectedItem().equals("Fixed") && txtAmount.getText().equals("") && chkGl.isSelected()) {
                    ClientUtil.showMessageWindow("Please enter Amount!!!");
                    return;
                } else if (!cboModuleType.getSelectedItem().equals("BASICPAY") && cboCalculationType.getSelectedItem().equals("Fixed") && !(txtAmount.getText().equals("")) && chkGl.isSelected()) {
                    double amount = CommonUtil.convertObjToDouble(txtAmount.getText());
                    if (amount <= 0) {
                        ClientUtil.showMessageWindow("Amount should be greater than 0!!!");
                        return;
                    } else {
                        savePerformed();
                    }
                } else {

                    if (cboCalculationType.getSelectedItem().equals("Calculated") && j <= 0) {
                        ClientUtil.showMessageWindow("There are no records to save!!!");
                        lblStatus.setText("Failed");
                        return;
                    } else if (cboCalculationType.getSelectedItem().equals("Calculated") && j >= 0) {
                        for (int k = 0; k < j; k++) {
                            String value = CommonUtil.convertObjToStr(tblCalcDetails.getValueAt(k, 1));
                            if (value != null && !value.equals("")) {
                                if (calcModCode != null && !calcModCode.equals("")) {
                                    calcModCode = calcModCode + "+" + value;
                                } else {
                                    calcModCode = value;
                                }
                                observable.setModType(calcModCode);
                            }
                        }
                    }
                    if (cboCalculationType.getSelectedItem().equals("Fixed") && chkGl.isSelected() && i <= 0) {
                        ClientUtil.showMessageWindow("There are no records to save!!!");
                        lblStatus.setText("Failed");
                        return;
                    } else {
                        savePerformed();
                    }
                }
            } else {
                String modeType = "";
                String payType = "";
                modeType = CommonUtil.convertObjToStr(cboModuleType.getSelectedItem());
                if (rdoEarnings.isSelected()) {
                    payType = "EARNINGS";
                } else if (rdoDeduction.isSelected()) {
                    payType = "DEDUCTIONS";
                } else if (rdoContra.isSelected()) {
                    payType = "CONTRA";
                }
                HashMap dataMap = new HashMap();
                dataMap.put("MODTYPE", modeType);
                dataMap.put("EARNDEDU", payType);
                List payCode;
                payCode = observable.getPayCode(dataMap);
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW && payCode != null && payCode.size() > 0) {
                    for (int k = 0; k < payCode.size(); k++) {
                        HashMap payMap = new HashMap();
                        payMap = (HashMap) payCode.get(k);
                        String module = "";
                        String earnDedu = "";
                        module = CommonUtil.convertObjToStr(payMap.get("PAY_MODULE_TYPE"));
                        earnDedu = CommonUtil.convertObjToStr(payMap.get("PAY_EARNDEDU"));
                        if (modeType.equals(module) && payType.equals(earnDedu) && observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                            ClientUtil.showMessageWindow("This Module Type already exists!!!");
                            resetTxtFields();
                            resetRadioBtns();
                            observable.resetForm();
                            observable.resetOBFields();
                            observable.resetStatus();
                            lblStatus.setText("Failed");
                            return;
                        }
                    }
                } else {
                    if (!cboModuleType.getSelectedItem().equals("BASICPAY") && cboCalculationType.getSelectedItem().equals("Fixed") && txtAmount.getText().equals("") && chkGl.isSelected()) {
                        ClientUtil.showMessageWindow("Please enter the Amount!!!");
                        lblStatus.setText("Failed");
                        return;
                    } else if (!cboModuleType.getSelectedItem().equals("BASICPAY") && cboCalculationType.getSelectedItem().equals("Fixed") && !(txtAmount.getText().equals("")) && chkGl.isSelected()) {
                        double amount = CommonUtil.convertObjToDouble(txtAmount.getText());
                        if (amount <= 0) {
                            ClientUtil.showMessageWindow("Amount should be greater than 0!!!");
                            return;
                        } else {
                            savePerformed();
                        }
                    }
                    if (rdoContra.isSelected() && i < 2) {
                        ClientUtil.showMessageWindow("Please enter both Debit and Credit!!!");
                        return;
                    }
                    if (cboCalculationType.getSelectedItem().equals("Calculated") && j <= 0) {
                        ClientUtil.showMessageWindow("There are no records to save!!!");
                        lblStatus.setText("Failed");
                        return;
                    } else {
                        if (cboCalculationType.getSelectedItem().equals("Fixed") && chkGl.isSelected() && i <= 0) {
                            ClientUtil.showMessageWindow("There are no records to save!!!");
                            lblStatus.setText("Failed");
                            return;
                        } else {
                            savePerformed();
                        }
                    }
                }
            }
        }
        calcModCode = null;
        isGl = false;
    }//GEN-LAST:event_btnSaveActionPerformed

    private void savePerformed() {
        updateOBFields();
        if (!isGl) {
            String prodctType = "";
            prodctType = CommonUtil.convertObjToStr(((ComboBoxModel) cboProdType.getModel()).getKeyForSelected());
            observable.setCboProductType(prodctType);
        }
        observable.doAction();
        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            lblStatus.setText("Success");
            resetData();
            btnCancelActionPerformed(null);
        } else if (observable.getResult() == ClientConstants.ACTIONTYPE_FAILED) {
            lblStatus.setText("Failed");
            resetData();
            btnCancelActionPerformed(null);
        }
    }

    private void checkGL() {
        if (chkGl.isSelected()) {
            enableAccTbl(true);
            cboProductType.setEnabled(false);
            txtAccountHead.setEditable(false);
            btnAccountHead.setEnabled(false);
            lblProdType.setVisible(false);
            cboProdType.setVisible(false);
            observable.setChkGl(true);
            cboAccountType.setEditable(false);
            if (cboCalculationType.getSelectedItem().equals("Fixed")) {
                lblAmount.setVisible(true);
                txtAmount.setVisible(true);
            }
        } else {
            enableAccTbl(false);
            lblProdType.setVisible(true);
            cboProdType.setVisible(true);
            cboProdType.setEnabled(true);
            lblAmount.setVisible(false);
            txtAmount.setVisible(false);
            observable.setChkGl(false);
        }
        setProductType();
    }

    private void resetData() {
        btnSave.setEnabled(false);
        btnNew.setEnabled(true);
        resetTxtFields();
        resetRadioBtns();
        enableAccDetTxt(false);
        enableCalcSettingsTxt(false);
        enableTxtFields(false);
        setModified(false);
        observable.resetForm();
        observable.setAccountTable();
        observable.setCalcTable();
        tblAccountDetails.revalidate();
        tblAccountDetails.setModel(observable.getTbmAccountDetails());
        tblCalcDetails.revalidate();
        tblCalcDetails.setModel(observable.getTbmCalcDetails());
        btnNew.setEnabled(true);
        btnEdit.setEnabled(true);
        btnClose.setEnabled(true);
        btnView.setEnabled(true);
        tdtFromDate.setEnabled(false);
        btnEdit.setEnabled(false);
        isGl = false;
        txtMaxAmt.setFocusable(false);
    }

    private void rdoEarningsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoEarningsActionPerformed
        // TODO add your handling code here:
        resetTxtFields();
        observable.resetOBFields();
        enableTxtFields(true);
        enableAccDetTxt(false);
        enableCalcSettingsTxt(false);
        HashMap earnMap = new HashMap();
        earnMap.put("LOOKUP_ID", "ELIGIBLE_ALLOWANCE");
        observable.setPayModType(earnMap);
        cboModuleType.setModel(observable.getCbmModuleType());
        cboAccountType.removeAllItems();
        cboAccountType.addItem("");
        cboAccountType.addItem("Debit");
        chkGl.setSelected(true);
        checkGL();
    }//GEN-LAST:event_rdoEarningsActionPerformed

    private void rdoDeductionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDeductionActionPerformed
        // TODO add your handling code here:
        resetTxtFields();
        observable.resetOBFields();
        enableTxtFields(true);
        enableAccDetTxt(false);
        enableCalcSettingsTxt(false);
        HashMap deduMap = new HashMap();
        deduMap.put("LOOKUP_ID", "DEDUCTIONS");
        observable.setPayModType(deduMap);
        cboModuleType.setModel(observable.getCbmModuleType());
        cboAccountType.removeAllItems();
        cboAccountType.addItem("");
        cboAccountType.addItem("Credit");
        chkGl.setSelected(true);
        checkGL();
    }//GEN-LAST:event_rdoDeductionActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        observable.resetOBFields();
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void cboCalculationTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCalculationTypeActionPerformed
        // TODO add your handling code here:
        txtAccountHead.setEditable(false);
        btnAccountHead.setEnabled(false);
        if (cboCalculationType.getSelectedItem().equals("Fixed")) {
            panAccountDetails.setVisible(true);
            panAccDet.setEnabled(false);
            panCalculatedTypeSettings.setVisible(false);
            lblAmount.setVisible(true);
            txtAmount.setVisible(true);
            btnAccSubNew.setEnabled(true);
        } else if (cboCalculationType.getSelectedItem().equals("Calculated")) {
            panAccountDetails.setVisible(true);
            btnAccSubNew.setEnabled(true);
            panAccDet.setEnabled(false);
            panCalcSettings.setEnabled(false);
            panAccountDetails.setEnabled(false);
            panAccSub.setEnabled(true);
            panCalculatedTypeSettings.setVisible(true);
            lblAmount.setVisible(false);
            txtAmount.setVisible(false);
            btnCalcSubNew.setEnabled(true);
            btnCalcSubSave.setEnabled(false);
            btnCalcSubDel.setEnabled(false);
            btnAccSubSave.setEnabled(false);
        }
        resetAccData();
        checkGL();
    }//GEN-LAST:event_cboCalculationTypeActionPerformed

    private void rdoContraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoContraActionPerformed
        // TODO add your handling code here:
        resetTxtFields();
        observable.resetOBFields();
        enableTxtFields(true);
        enableAccDetTxt(false);
        enableCalcSettingsTxt(false);
        HashMap deduMap = new HashMap();
        deduMap.put("LOOKUP_ID", "DEDUCTIONS");
        observable.setPayModType(deduMap);
        cboModuleType.setModel(observable.getCbmModuleType());
        cboAccountType.removeAllItems();
        cboAccountType.addItem("");
        cboAccountType.addItem("Debit");
        cboAccountType.addItem("Credit");
        chkGl.setSelected(true);
        checkGL();
    }//GEN-LAST:event_rdoContraActionPerformed

    private void btnAccountHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccountHeadActionPerformed
        // TODO add your handling code here:
        if (CommonUtil.convertObjToStr(cboProductType.getSelectedItem()).equals("")) {
            ClientUtil.showMessageWindow("Please select Product Type!!!");
        } else {
            viewType = ACCTHDID;
            final HashMap viewMap = new HashMap();
            viewMap.put(CommonConstants.MAP_NAME, "Cash.getSelectAcctHead");
            new ViewAll(this, viewMap).show();
        }
    }//GEN-LAST:event_btnAccountHeadActionPerformed

    private void btnAccSubNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccSubNewActionPerformed
        // TODO add your handling code here:
        enableAccDetTxt(true);
        txtAccountHead.setText("");
        cboAccountType.setSelectedIndex(0);
        updateAccTable();
        btnAccSubDel.setEnabled(false);
    }//GEN-LAST:event_btnAccSubNewActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        resetTxtFields();
        resetRadioBtns();
        enableRadioBtns(false);
        enableTxtFields(false);
        panCalculatedTypeSettings.setVisible(true);
        panAccountDetails.setVisible(true);
        setModified(false);
        observable.resetOBFields();
        observable.resetForm();
        observable.resetStatus();
        enableCalcSettingsTxt(false);
        enableAccDetTxt(false);
        this.updateCalcTable();
        btnSave.setEnabled(false);
        btnCancel.setEnabled(false);
        btnNew.setEnabled(true);
        btnEdit.setEnabled(true);
        btnView.setEnabled(true);
        btnDelete.setEnabled(true);
        lblAccountType.setVisible(true);
        cboAccountType.setVisible(true);
        enableAccTbl(true);
        txtMaxAmt.setFocusable(false);
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnAccSubSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccSubSaveActionPerformed
        // TODO add your handling code here:
        updateOBFields();
        if (cboProductType.getSelectedItem().equals("")) {
            ClientUtil.showMessageWindow("Please select Product Type!!!");
        } else if (cboAccountType.getSelectedItem().equals("")) {
            ClientUtil.showMessageWindow("Please select Account Type!!!");
        } else if (txtAccountHead.getText().equals("")) {
            ClientUtil.showMessageWindow("Please enter Account Head!!!");
        } else {
            if (!this.selectedAccSingleRow) {
                int rowSelected = this.tblAccountDetails.getSelectedRow();
                insertAccTableData("edit");
                observable.insertIntoAccTableData(rowSelected);
                this.updateAccTable();
                btnDelete.setEnabled(false);
                txtAccountHead.setText("");
                cboProductType.setSelectedItem("");
                cboAccountType.setSelectedIndex(0);
                txtAccountHead.setEnabled(false);
                cboProductType.setEnabled(false);
                cboAccountType.setEnabled(false);
                btnAccountHead.setEnabled(false);
            } else {
                insertAccTableData("");
                observable.insertIntoAccTableData(-1);
                this.updateAccTable();
            }
        }
    }//GEN-LAST:event_btnAccSubSaveActionPerformed

    private void insertAccTableData(String e) {
        int i = tblAccountDetails.getRowCount();
        int j = tblAccountDetails.getSelectedRow();
        int selectedindex = tblAccountDetails.getSelectedRow();
        int accSlNo = 1;
        String accType = CommonUtil.convertObjToStr(cboAccountType.getSelectedItem());
        if (e.equals("edit")) {
            if (i == 0 || selectedindex == 0) {
                accSlNo = accSlNo;
                observable.setCheck(true);
            } else {
                if (i >= 2) {
                    String tblAccType = CommonUtil.convertObjToStr(tblAccountDetails.getValueAt(i - 2, 2));
                    if (accType.equals(tblAccType)) {
                        ClientUtil.showMessageWindow(tblAccType + " already entered!!!");
                        observable.setCheck(false);
                    } else {
                        accSlNo = accSlNo + 1;
                        observable.setCheck(true);
                    }
                } else {
                    String tblAccType = CommonUtil.convertObjToStr(tblAccountDetails.getValueAt(i - 1, 2));
                    if (accType.equals(tblAccType)) {
                        ClientUtil.showMessageWindow(tblAccType + " already entered!!!");
                        observable.setCheck(false);
                    } else {
                        accSlNo = accSlNo + 1;
                        observable.setCheck(true);
                    }
                }
            }
        }
        observable.setAccSlNo(accSlNo);
        observable.setTxtAccountHead(txtAccountHead.getText());
        observable.setCboAccountType(CommonUtil.convertObjToStr(cboAccountType.getSelectedItem()));
        String prodtype = CommonUtil.convertObjToStr(((ComboBoxModel) cboProductType.getModel()).getKeyForSelected());
        isGl = true;
        observable.setCboProductType(prodtype);
    }

    private void updateAccTable() {
        this.tblAccountDetails.setModel(observable.getTbmAccountDetails());
        this.tblAccountDetails.revalidate();
    }

    private void btnCalcSubNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalcSubNewActionPerformed
        // TODO add your handling code here:
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            cboCalcModType.setModel(observable.getCbmCalcModType());
            tdtFromDate.setEnabled(true);
        }
        int rowCount = tblCalcDetails.getRowCount();
        if (rowCount > 0) {
            cboCalcModType.setSelectedIndex(0);
            btnCalcSubNew.setEnabled(true);
            btnCalcSubSave.setEnabled(true);
            cboCalcModType.setEnabled(true);
            txtMinAmt.setEditable(false);
            txtMaxAmt.setEditable(false);
            txtPercentage.setEditable(false);
        } else {
            enableCalcSettingsTxt(true);
            List calcModType = null;
            calcModType = observable.setCalcModType();
            cboCalcModType.setModel(observable.getCbmCalcModType());
            btnCalcSubSave.setEnabled(true);
            btnCalcSubDel.setEnabled(false);
            updateCalcTable();
            cboCalcModType.setSelectedIndex(0);
            chkIncludePersonalPay.setSelected(false);
        }
    }//GEN-LAST:event_btnCalcSubNewActionPerformed

    private void btnAccSubDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccSubDelActionPerformed
        // TODO add your handling code here:
        int rowSelected = this.tblAccountDetails.getSelectedRow();
        if (rowSelected >= 0) {
            observable.deleteAccTblData(rowSelected);
            txtAccountHead.setText("");
            cboAccountType.setSelectedIndex(0);
            cboProductType.setSelectedItem("");
            this.updateAccTable();
            accSlNo = tblAccountDetails.getRowCount();
            observable.setAccSlNo(accSlNo);
        } else {
            ClientUtil.showMessageWindow("Please select a row to delete!!!");
        }
    }//GEN-LAST:event_btnAccSubDelActionPerformed

    private void btnCalcSubSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalcSubSaveActionPerformed
        // TODO add your handling code here:
        updateOBFields();
        if (!this.selectedCalcSingleRow) {
            int rowSelected = this.tblCalcDetails.getSelectedRow();
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW && cboCalcModType.getSelectedItem().equals("") || txtPercentage.getText().equals("") || txtMinAmt.getText().equals("") || txtMaxAmt.getText().equals("")) {
                ClientUtil.showMessageWindow("Please enter all the details!!!");
            } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT && cboCalcModType.getSelectedItem().equals("")) {
                ClientUtil.showMessageWindow("Please enter valid Module Type!!!");
            } else {
                insertCalcTableData("edit");
                observable.insertIntoCalcTableData(rowSelected);
                cboCalcModType.setSelectedIndex(0);
                txtMaxAmt.setEditable(false);
                txtMinAmt.setEditable(false);
                txtPercentage.setEditable(false);
                tdtFromDate.setEnabled(false);
                chkIncludePersonalPay.setSelected(false);
                this.updateCalcTable();
                btnDelete.setEnabled(false);
                cboCalcModType.setEnabled(false);
            }
        } else {
            insertCalcTableData("");
            observable.insertIntoCalcTableData(-1);
            this.updateCalcTable();
            chkIncludePersonalPay.setSelected(false);
        }
    }//GEN-LAST:event_btnCalcSubSaveActionPerformed

    private void btnCalcSubDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalcSubDelActionPerformed
        // TODO add your handling code here:
        updateOBFields();
        int rowSelected = this.tblCalcDetails.getSelectedRow();
        int rowCnt = this.tblCalcDetails.getRowCount();
        if (rowSelected >= 0) {
            if (modType != null) {
                if (rowSelected == 0) {
                    modType = modType.replace(CommonUtil.convertObjToStr(observable.getCbmCalcModType().getKeyForSelected()) + "+", "");
                } else {
                    modType = modType.replace("+" + CommonUtil.convertObjToStr(observable.getCbmCalcModType().getKeyForSelected()), "");
                }
            }
            observable.setModType(modType);
            observable.deleteCalcTblData(rowSelected);
            cboCalcModType.setSelectedIndex(0);
            chkIncludePersonalPay.setSelected(false);
            if (modType != null) {
                modType = modType.replace(CommonUtil.convertObjToStr(observable.getCbmCalcModType().getKeyForSelected()), "");
                observable.setModType(modType);
            }
            if (rowSelected == 0 && rowCnt == 1) {
                txtMaxAmt.setEditable(true);
                txtMinAmt.setEditable(true);
                txtPercentage.setEditable(true);
                txtMaxAmt.setText("");
                txtMinAmt.setText("");
                txtPercentage.setText("");
                tdtFromDate.setDateValue(null);
            }
            enableCalcSettingsTxt(false);
            btnCalcSubSave.setEnabled(false);
            btnCalcSubNew.setEnabled(true);
        } else {
            ClientUtil.showMessageWindow("Please select a row to delete!!!");
        }
        this.updateCalcTable();
    }//GEN-LAST:event_btnCalcSubDelActionPerformed
//GEN-FIRST:event_tblAccountDetails1MouseClicked
//GEN-LAST:event_tblAccountDetails1MouseClicked
    private void tblAccountDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAccountDetailsMouseClicked
        // TODO add your handling code here:
        this.selectedAccSingleRow = true;
        int rowCount = tblAccountDetails.getRowCount();
        if (rowCount >= 0) {
            updateAccTableData();
            this.selectedAccSingleRow = false;
            btnAccSubSave.setEnabled(true);
            btnAccSubDel.setEnabled(true);
            btnAccountHead.setEnabled(true);
        } else {
            ClientUtil.showMessageWindow("There are no records to display!!!");
        }
    }//GEN-LAST:event_tblAccountDetailsMouseClicked

    private void updateAccTableData() {
        this.selectedAccSingleRow = true;
        observable.populateAccTableData(tblAccountDetails.getSelectedRow());
        txtAccountHead.setText(CommonUtil.convertObjToStr(observable.getTxtAccountHead()));
        cboAccountType.setSelectedItem(CommonUtil.convertObjToStr(observable.getCboAccountType()));
        cboProductType.setSelectedItem(observable.getCbmProductType().getDataForKey(observable.getCboProductType()));
    }

    private void tblCalcDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCalcDetailsMouseClicked
        // TODO add your handling code here:
        isclicked = true;
        this.selectedCalcSingleRow = true;
        int rowCount = tblCalcDetails.getRowCount();
        int calcSelRow = 0;
        calcSelRow = tblCalcDetails.getSelectedRow();
        if (rowCount >= 0) {
            tblCalcDetails.setEnabled(true);
            String value = CommonUtil.convertObjToStr(tblCalcDetails.getValueAt(calcSelRow, 1));
            updateCalcTableData(value, rowCount);
            this.selectedCalcSingleRow = false;
            btnCalcSubSave.setEnabled(true);
            btnCalcSubDel.setEnabled(true);
        } else {
            ClientUtil.showMessageWindow("There are no records to display!!!");
        }
    }//GEN-LAST:event_tblCalcDetailsMouseClicked

    private void chkActiveKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chkActiveKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10) {
            chkActive.setSelected(true);
        }
    }//GEN-LAST:event_chkActiveKeyPressed

    private void rdoEarningsKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_rdoEarningsKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10) {
            rdoEarnings.setSelected(true);
            enableTxtFields(true);
            observable.resetOBFields();
            HashMap earnMap = new HashMap();
            earnMap.put("LOOKUP_ID", "ELIGIBLE_ALLOWANCE");
            observable.setPayModType(earnMap);
            cboModuleType.setModel(observable.getCbmModuleType());
            cboAccountType.removeAllItems();
            cboAccountType.addItem("");
            cboAccountType.addItem("Debit");
        }
    }//GEN-LAST:event_rdoEarningsKeyPressed

    private void rdoDeductionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_rdoDeductionKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10) {
            rdoDeduction.setSelected(true);
            enableTxtFields(true);
            observable.resetOBFields();
            HashMap deduMap = new HashMap();
            deduMap.put("LOOKUP_ID", "DEDUCTIONS");
            observable.setPayModType(deduMap);
            cboModuleType.setModel(observable.getCbmModuleType());
            cboAccountType.removeAllItems();
            cboAccountType.addItem("");
            cboAccountType.addItem("Credit");
        }
    }//GEN-LAST:event_rdoDeductionKeyPressed

    private void rdoContraKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_rdoContraKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10) {
            rdoContra.setSelected(true);
            enableTxtFields(true);
            observable.resetOBFields();
            HashMap deduMap = new HashMap();
            deduMap.put("LOOKUP_ID", "DEDUCTIONS");
            observable.setPayModType(deduMap);
            cboModuleType.setModel(observable.getCbmModuleType());
            cboAccountType.removeAllItems();
            cboAccountType.addItem("");
            cboAccountType.addItem("Debit");
            cboAccountType.addItem("Credit");
        }
    }//GEN-LAST:event_rdoContraKeyPressed

    private void chkExcludeFromTaxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chkExcludeFromTaxKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10) {
            chkExcludeFromTax.setSelected(true);
        }
    }//GEN-LAST:event_chkExcludeFromTaxKeyPressed

    private void chkPaymentVoucherRequiredKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chkPaymentVoucherRequiredKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10) {
            chkPaymentVoucherRequired.setSelected(true);
        }
    }//GEN-LAST:event_chkPaymentVoucherRequiredKeyPressed

    private void chkIndividualRequiredKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chkIndividualRequiredKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10) {
            chkIndividualRequired.setSelected(true);
        }
    }//GEN-LAST:event_chkIndividualRequiredKeyPressed

    private void chkOnlyForContraKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chkOnlyForContraKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10) {
            chkOnlyForContra.setSelected(true);
        }
    }//GEN-LAST:event_chkOnlyForContraKeyPressed

    private void cboModuleTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboModuleTypeActionPerformed
        // TODO add your handling code here:
        if (cboModuleType.getSelectedItem().equals("BASICPAY")) {
            txtAmount.setEnabled(false);
        } else {
            txtAmount.setEnabled(true);
        }
    }//GEN-LAST:event_cboModuleTypeActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        btnCancel.setEnabled(true);
        btnClose.setEnabled(true);
        callView(ClientConstants.ACTIONTYPE_VIEW);
        btnNew.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnSave.setEnabled(false);
        enableAccDetTxt(false);
        enableCalcSettingsTxt(false);
        enableTxtFields(false);
        btnAccSubNew.setEnabled(false);
    }//GEN-LAST:event_btnViewActionPerformed

    private void chkIncludePersonalPayKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chkIncludePersonalPayKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10) {
            chkIncludePersonalPay.setSelected(true);
        }
    }//GEN-LAST:event_chkIncludePersonalPayKeyPressed

    private void txtMaxAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMaxAmtFocusLost
        // TODO add your handling code here:
        double minAmt = CommonUtil.convertObjToDouble(txtMinAmt.getText());
        double maxAmt = CommonUtil.convertObjToDouble(txtMaxAmt.getText());
        if (minAmt >= maxAmt) {
            ClientUtil.showMessageWindow("Maximum Amount should not be less than or equal to Minimum Amount!!!");
            txtMaxAmt.setText("");
        }
    }//GEN-LAST:event_txtMaxAmtFocusLost

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRejectActionPerformed

    private void chkGlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkGlActionPerformed
        resetAccData();
        checkGL();
    }//GEN-LAST:event_chkGlActionPerformed

    private void resetAccData() {
        cboProductType.setSelectedItem("");
        cboAccountType.setSelectedIndex(0);
        txtAccountHead.setText("");
    }

    private void chkGlKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chkGlKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10) {
            chkGl.setSelected(true);
        }
    }//GEN-LAST:event_chkGlKeyPressed

    private void chkIncludePersonalPayFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_chkIncludePersonalPayFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_chkIncludePersonalPayFocusLost

    private void tdtFromDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtFromDateFocusLost
          //  observable.getTdtFromDate()
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            ClientUtil.validateToDate(tdtFromDate, ClientUtil.getCurrentDateinDDMMYYYY());
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            if (observable.getTdtFromDate() != null) {
                ClientUtil.validateToDate(tdtFromDate, observable.getTdtFromDate());
                if (tdtFromDate.getDateValue().equals("")) {
                    tdtFromDate.setDateValue(observable.getTdtFromDate());
                }
            }
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_tdtFromDateFocusLost

    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAccSubDel;
    private com.see.truetransact.uicomponent.CButton btnAccSubNew;
    private com.see.truetransact.uicomponent.CButton btnAccSubSave;
    private com.see.truetransact.uicomponent.CButton btnAccountHead;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCalcSubDel;
    private com.see.truetransact.uicomponent.CButton btnCalcSubNew;
    private com.see.truetransact.uicomponent.CButton btnCalcSubSave;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboAccountType;
    private com.see.truetransact.uicomponent.CComboBox cboCalcModType;
    private com.see.truetransact.uicomponent.CComboBox cboCalculationType;
    private com.see.truetransact.uicomponent.CComboBox cboModuleType;
    private com.see.truetransact.uicomponent.CComboBox cboProdType;
    private com.see.truetransact.uicomponent.CComboBox cboProductType;
    private com.see.truetransact.uicomponent.CCheckBox chkActive;
    private com.see.truetransact.uicomponent.CCheckBox chkExcludeFromTax;
    private com.see.truetransact.uicomponent.CCheckBox chkGl;
    private com.see.truetransact.uicomponent.CCheckBox chkIncludePersonalPay;
    private com.see.truetransact.uicomponent.CCheckBox chkIndividualRequired;
    private com.see.truetransact.uicomponent.CCheckBox chkOnlyForContra;
    private com.see.truetransact.uicomponent.CCheckBox chkPaymentVoucherRequired;
    private com.see.truetransact.uicomponent.CLabel lblAccountNo;
    private com.see.truetransact.uicomponent.CLabel lblAccountType;
    private com.see.truetransact.uicomponent.CLabel lblActive;
    private com.see.truetransact.uicomponent.CLabel lblAmount;
    private com.see.truetransact.uicomponent.CLabel lblCalculationType;
    private com.see.truetransact.uicomponent.CLabel lblDescription;
    private com.see.truetransact.uicomponent.CLabel lblExcludeFromTax;
    private com.see.truetransact.uicomponent.CLabel lblFromDate;
    private com.see.truetransact.uicomponent.CLabel lblGl;
    private com.see.truetransact.uicomponent.CLabel lblIncludePersonalPay;
    private com.see.truetransact.uicomponent.CLabel lblIndividualRequired;
    private com.see.truetransact.uicomponent.CLabel lblMaxAmt;
    private com.see.truetransact.uicomponent.CLabel lblMinAmt;
    private com.see.truetransact.uicomponent.CLabel lblModType;
    private com.see.truetransact.uicomponent.CLabel lblModuleType;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblOnlyForContra;
    private com.see.truetransact.uicomponent.CLabel lblPaymentVoucherRequired;
    private com.see.truetransact.uicomponent.CLabel lblPercent;
    private com.see.truetransact.uicomponent.CLabel lblPercentage;
    private com.see.truetransact.uicomponent.CLabel lblProdType;
    private com.see.truetransact.uicomponent.CLabel lblProductType;
    private com.see.truetransact.uicomponent.CLabel lblSpace;
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
    private com.see.truetransact.uicomponent.CMenuBar mbrEarnDedu;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAccDet;
    private com.see.truetransact.uicomponent.CPanel panAccSub;
    private com.see.truetransact.uicomponent.CPanel panAccountDetails;
    private com.see.truetransact.uicomponent.CPanel panAccountNo;
    private com.see.truetransact.uicomponent.CPanel panCalcSettings;
    private com.see.truetransact.uicomponent.CPanel panCalcSub;
    private com.see.truetransact.uicomponent.CPanel panCalculatedTypeSettings;
    private com.see.truetransact.uicomponent.CPanel panEarnDedu;
    private com.see.truetransact.uicomponent.CPanel panEarnDeduContra;
    private com.see.truetransact.uicomponent.CPanel panEarnDeduData;
    private com.see.truetransact.uicomponent.CPanel panEarningsDeduction;
    private com.see.truetransact.uicomponent.CPanel panEarningsDeductionInfo;
    private com.see.truetransact.uicomponent.CPanel panGlobalSettings;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CRadioButton rdoContra;
    private com.see.truetransact.uicomponent.CRadioButton rdoDeduction;
    private com.see.truetransact.uicomponent.CRadioButton rdoEarnings;
    private com.see.truetransact.uicomponent.CButtonGroup rdoPayTypeGroup;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CScrollPane srpAccountDetails;
    private com.see.truetransact.uicomponent.CScrollPane srpCalcDetails;
    private com.see.truetransact.uicomponent.CTabbedPane tabEarnDedu;
    private com.see.truetransact.uicomponent.CTable tblAccountDetails;
    private com.see.truetransact.uicomponent.CTable tblCalcDetails;
    private javax.swing.JToolBar tbrEarnDedu;
    private com.see.truetransact.uicomponent.CDateField tdtFromDate;
    private com.see.truetransact.uicomponent.CTextField txtAccountHead;
    private com.see.truetransact.uicomponent.CTextField txtAmount;
    private com.see.truetransact.uicomponent.CTextField txtDescription;
    private com.see.truetransact.uicomponent.CTextField txtMaxAmt;
    private com.see.truetransact.uicomponent.CTextField txtMinAmt;
    private com.see.truetransact.uicomponent.CTextField txtPercentage;
    // End of variables declaration//GEN-END:variables

    private void enableTxtFields(boolean flag) {
        rdoEarnings.setEnabled(flag);
        rdoDeduction.setEnabled(flag);
        rdoContra.setEnabled(flag);
        cboModuleType.setEnabled(flag);
        txtDescription.setEnabled(flag);
        cboCalculationType.setEnabled(flag);
        txtAmount.setEnabled(flag);
        cboProductType.setEnabled(flag);
        txtAccountHead.setEnabled(flag);
        cboAccountType.setEnabled(flag);
        chkActive.setEnabled(flag);
        chkExcludeFromTax.setEnabled(flag);
        chkIncludePersonalPay.setEnabled(flag);
        chkIndividualRequired.setEnabled(flag);
        chkPaymentVoucherRequired.setEnabled(flag);
        btnAccountHead.setEnabled(flag);
        chkOnlyForContra.setEnabled(flag);
        chkGl.setEnabled(flag);
        panAccountDetails.setEnabled(flag);
        cboProdType.setEnabled(flag);
    }

    private void enableAccDetTxt(boolean flag) {
        panAccountDetails.setEnabled(flag);
        btnAccountHead.setEnabled(flag);
        cboAccountType.setEnabled(flag);
        cboProductType.setEnabled(flag);
        txtAccountHead.setEnabled(flag);
        tblAccountDetails.setEnabled(flag);
        btnAccSubSave.setEnabled(flag);
        btnAccSubDel.setEnabled(flag);
    }

    private void enableCalcSettingsTxt(boolean flag) {
        panCalculatedTypeSettings.setEnabled(flag);
        chkIncludePersonalPay.setEnabled(flag);
        tblCalcDetails.setEnabled(flag);
        btnCalcSubNew.setEnabled(flag);
        btnCalcSubSave.setEnabled(flag);
        btnCalcSubDel.setEnabled(flag);
        cboCalcModType.setEnabled(flag);
        txtPercentage.setEnabled(flag);
        txtMinAmt.setEnabled(flag);
        txtMaxAmt.setEnabled(flag);
        tdtFromDate.setEnabled(flag);
    }

    private void enableAccTbl(boolean flag) {
        panAccountDetails.setVisible(flag);
        srpAccountDetails.setVisible(flag);
        panAccSub.setVisible(flag);
        lblAccountNo.setVisible(flag);
        panAccountNo.setVisible(flag);
        txtAccountHead.setEditable(flag);
        btnAccountHead.setVisible(flag);
        lblProductType.setVisible(flag);
        cboProductType.setVisible(flag);
        cboAccountType.setVisible(flag);
        lblAccountType.setVisible(flag);
        lblProdType.setVisible(false);
        cboProdType.setVisible(false);
    }

    private void resetRadioBtns() {
        rdoEarnings.setSelected(false);
        rdoDeduction.setSelected(false);
        rdoContra.setSelected(false);
        rdoPayTypeGroup.clearSelection();
    }

    private void enableRadioBtns(boolean flag) {
        rdoContra.setEnabled(flag);
        rdoDeduction.setEnabled(flag);
        rdoEarnings.setEnabled(flag);
    }

    private void resetTxtFields() {
        txtDescription.setText("");
        txtAmount.setText("");
        chkActive.setSelected(false);
        chkExcludeFromTax.setSelected(false);
        chkIndividualRequired.setSelected(false);
        chkPaymentVoucherRequired.setSelected(false);
        chkIncludePersonalPay.setSelected(false);
        chkOnlyForContra.setSelected(false);
        chkGl.setSelected(false);
        txtAccountHead.setText("");
        txtPercentage.setText("");
        txtMinAmt.setText("");
        txtMaxAmt.setText("");
        tdtFromDate.setDateValue(null);
        cboModuleType.setSelectedItem("");
        cboCalculationType.setSelectedIndex(0);
        cboProductType.setSelectedItem("");
        cboAccountType.setSelectedIndex(0);
        cboCalcModType.setSelectedItem("");
        accSlNo = 1;
        calcSlNo = 1;
    }

    public void fillData(Object param) {
        setModified(true);
        final HashMap hash = (HashMap) param;
        hash.put("WHERE", hash.get("PAYCODE_ID"));
        hash.put("PAY_PROD_TYPE", hash.get("PAY_PROD_TYPE"));
        if (viewType == ACCTHDID) {
            String acHdId = CommonUtil.convertObjToStr(hash.get("A/C HEAD"));
            txtAccountHead.setText(acHdId);
        } else if (viewType == ClientConstants.ACTIONTYPE_DELETE || viewType == ClientConstants.ACTIONTYPE_VIEW) {
            try {
                setModified(true);
                observable.populateData(hash);
                observable.ttNotifyObservers();
            } catch (Exception ex) {
                Logger.getLogger(EarningsDeductionUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (viewType == ClientConstants.ACTIONTYPE_EDIT) {
            try {
                ClientUtil.enableDisable(panEarnDeduContra, true);
                ClientUtil.enableDisable(panGlobalSettings, true);
                ClientUtil.enableDisable(panAccountDetails, true);
                ClientUtil.enableDisable(panCalculatedTypeSettings, true);
                observable.populateData(hash);
                observable.ttNotifyObservers();
                lblStatus.setText("Edit");
                cboCalcModType.setSelectedItem("");
                observable.setIsFilled(true);
            } catch (Exception ex) {
                Logger.getLogger(EarningsDeductionUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void updateCalcTable() {
        this.tblCalcDetails.setModel(observable.getTbmCalcDetails());
        this.tblCalcDetails.revalidate();
    }

    private void insertCalcTableData(String e) {
        int i = tblCalcDetails.getRowCount();
        int j = tblCalcDetails.getSelectedRow();
        boolean check=false;
        int selectedindex = tblCalcDetails.getSelectedRow();
        String calcType = CommonUtil.convertObjToStr(cboCalcModType.getSelectedItem());
        if (e.equals("edit")) {
            if (i == 0 || selectedindex == 0) {
                calcSlNo = calcSlNo;
                observable.setCheck(true);
            } else {
                for (int k = 1; k <= tblCalcDetails.getRowCount(); k++) {
                    String tblCalcType = CommonUtil.convertObjToStr(tblCalcDetails.getValueAt(k - 1, 2));
                    if (calcType.equals(tblCalcType)) {
                        check = true;
                    }
                }
                if (check) {
                    ClientUtil.showMessageWindow(" already entered!!!");
                    observable.setCheck(false);
                } else {
                    calcSlNo = i + 1;
                    observable.setCheck(true);
                }
            }
        }
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            if (modType.equals("") || modType.equals(null)) {
                modType = CommonUtil.convertObjToStr(observable.getCbmCalcModType().getKeyForSelected());
            } else {
                modType = modType + "+" + observable.getCbmCalcModType().getKeyForSelected();
            }
            observable.setModType(modType);
        }
        observable.setCalcSlNo(calcSlNo);
        observable.setTxtMinAmt(txtMinAmt.getText());
        observable.setTxtMaxAmt(txtMaxAmt.getText());
        observable.setTxtPercentage(txtPercentage.getText());
        observable.setCboCalcModType(CommonUtil.convertObjToStr(cboCalcModType.getSelectedItem()));
        if (chkIncludePersonalPay.isSelected()) {
            observable.setChkIncludePersonalPay(true);
        } else {
            observable.setChkIncludePersonalPay(false);
        }
    }

    private int updateCalcTableData(String value, int rowCount) {
        this.selectedCalcSingleRow = true;
        observable.setCalcSlNo(calcSlNo);
        HashMap payMap = new HashMap();
        payMap.put("PAYCODE", value);
        String paymod = observable.getPayModule(payMap);
        observable.populateCalcTableData(tblCalcDetails.getSelectedRow());
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            cboCalcModType.setModel(observable.getCbmCalcModType());
        }
        cboCalcModType.setSelectedItem(paymod);
        return rowCount;
    }

    private void callView(int viewType) {
        HashMap viewMap = new HashMap();
        this.viewType = viewType;
        if (viewType == ClientConstants.ACTIONTYPE_EDIT || viewType == ClientConstants.ACTIONTYPE_DELETE || viewType == ClientConstants.ACTIONTYPE_VIEW) {
            viewMap.put(CommonConstants.MAP_NAME, "viewEarnDedu");
            isFilled = false;
        }
        new ViewAll(this, viewMap).show();
    }
}
