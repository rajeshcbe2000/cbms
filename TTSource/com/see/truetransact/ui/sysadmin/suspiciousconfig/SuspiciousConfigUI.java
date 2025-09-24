/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * SuspiciousConfigUI.java
 *
 * Created on January 8, 2005, 12:17 PM
 */

package com.see.truetransact.ui.sysadmin.suspiciousconfig;

/**
 *
 * @author  152715
 */

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Observer;
import java.util.Observable;



import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.common.viewall.TableDialogUI;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.TrueTransactMain;

public class SuspiciousConfigUI extends CInternalFrame implements Observer , UIMandatoryField  {
    // Variables Declarations
    private SuspiciousConfigOB observable;
    //    final SuspiciousConfigRB  resourceBundle = new SuspiciousConfigRB();
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.sysadmin.suspiciousconfig.SuspiciousConfigRB", ProxyParameters.LANGUAGE);
    
    private HashMap mandatoryMap;
    private boolean tblPress = false;
    private final int NEW=0,EDIT =1,DELETE=2,ACCOUNT_NO=3,CUSTOMER_NO=4,AUTHORIZE=5, VIEW=6;
    private int viewType = -1;
    private String configurationFor = null;
    final SuspiciousConfigMRB objMandatoryRB = new SuspiciousConfigMRB();
    private int GENERAL =0,ACCOUNT=1,CUSTOMER=2,LEVEL=-1;
    boolean isFilled = false;
    private String CONF_KEY = "";
    
    /** Creates new form SuspiciousConfigUI */
    public SuspiciousConfigUI() {
        initComponents();
        initStartUp();
    }
    private void initStartUp() {
        setFieldNames();
        internationalize();
        setObservable();
        initComponentData();
        setMandatoryHashMap();
        setHelpMessage();
        setMaximumLength();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panMain);
        setButtonEnableDisable();
        ClientUtil.enableDisable(this, false);
        enableDisable_NewSaveDelete(false);
        enableDisableHelpButtons(false);
        txtAccNo.setEditable(false);
        txtCustNo.setEditable(false);
        observable.resetForm();
        observable.resetStatus();
    }
    /*To set model for combo boxes*/
    private void initComponentData() {
        cboConfigurationFor.setModel(observable.getCbmConfigFor());
        //        cboTransactionType.setModel(observable.getCbmTransType());
        cboProdType.setModel(observable.getCbmProdType());
        cboPeriod.setModel(observable.getCbmPeriod());
        cboProdId.setModel(observable.getCbmProdId());
    }
    /**
     * enable disable Acc No Cust No help button
     */
    private void enableDisableHelpButtons(boolean flag) {
        btnAccNo.setEnabled(flag);
        btnCustNo.setEnabled(flag);
    }
    private void setObservable() {
        /* Implementing Singleton pattern */
        observable = SuspiciousConfigOB.getInstance();
        observable.addObserver(this);
    }
    /**
     * EnableDisable New Save Delete Buttons of OtherBankBranch Table
     * When New is pressed
     */
    private void enableDisable_NewSaveDelete(boolean flag) {
        btnSuspiciousNew.setEnabled(flag);
        btnSuspiciousSave.setEnabled(flag);
        btnSuspiciousDelete.setEnabled(flag);
    }
    /**
     * enable disable transaction fields
     */
    private void enableDisablePanTransaction(boolean flag) {
        ClientUtil.enableDisable(panTransaction,flag);
        enableDisableCustLevel(!flag);
        enableDisableAccLevel(flag);
        btnAccNo.setEnabled(!flag);
    }
    private void enableDisableConfigLevel(boolean flag) {
        enableDisablePanTransaction(true);
        enableDisableCustLevel(!flag);
        enableDisableAccLevel(flag);
        
    }
    private void enableDisableAccLevel(boolean flag) {
        btnAccNo.setEnabled(flag);
        cboProdId.setEditable(flag);
        cboProdType.setEditable(flag);
        cboProdId.setEnabled(flag);
        cboProdType.setEnabled(flag);
    }
    private void enableDisableCustLevel(boolean flag) {
        btnCustNo.setEnabled(flag);
    }
    /**
     * Enable Disable New Save Delete Buttons of OtherBankBranch Table
     * When Save or Delete is invoked
     */
    private void enableDisable_SaveDelete() {
        btnSuspiciousNew.setEnabled(true);
        btnSuspiciousSave.setEnabled(false);
        btnSuspiciousDelete.setEnabled(false);
    }
    private void enableDisableTable(boolean flag) {
        tblSuspiciousConfig.setEnabled(flag);
    }
    // To set The Value of the Buttons Depending on the Value or Condition...
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
        btnView.setEnabled(!btnView.isEnabled());
    }
    
   /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        btnAccNo.setName("btnAccNo");
        btnAuthorize.setName("btnAuthorize");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnCustNo.setName("btnCustNo");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        btnSuspiciousDelete.setName("btnSuspiciousDelete");
        btnSuspiciousNew.setName("btnSuspiciousNew");
        btnSuspiciousSave.setName("btnSuspiciousSave");
        cboConfigurationFor.setName("cboConfigurationFor");
        cboPeriod.setName("cboPeriod");
        cboProdId.setName("cboProdId");
        cboProdType.setName("cboProdType");
        chkCreditCash.setName("chkCreditCash");
        chkCreditClearing.setName("chkCreditClearing");
        chkCreditTransfer.setName("chkCreditTransfer");
        chkDebitCash.setName("chkDebitCash");
        chkDebitClearing.setName("chkDebitClearing");
        chkDebitTransfer.setName("chkDebitTransfer");
        lblAccNo.setName("lblAccNo");
        lblCash.setName("lblCash");
        lblClearing.setName("lblClearing");
        lblConfigurationFor.setName("lblConfigurationFor");
        lblCountExceeds.setName("lblCountExceeds");
        lblCredit.setName("lblCredit");
        lblCustNo.setName("lblCustNo");
        lblDebit.setName("lblDebit");
        lblMsg.setName("lblMsg");
        lblPeriod.setName("lblPeriod");
        lblProdId.setName("lblProdId");
        lblProdType.setName("lblProdType");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblStatus.setName("lblStatus");
        lblTransfer.setName("lblTransfer");
        lblWorthExceeds.setName("lblWorthExceeds");
        mbrSuspiciousConfig.setName("mbrSuspiciousConfig");
        panButtons.setName("panButtons");
        panConfigurationFor.setName("panConfigurationFor");
        panCreditDebit.setName("panCreditDebit");
        panMain.setName("panMain");
        panPeriod.setName("panPeriod");
        panStatus.setName("panStatus");
        panTable.setName("panTable");
        panTransaction.setName("panTransaction");
        spt1.setName("spt1");
        spt2.setName("spt2");
        spt3.setName("spt3");
        spt4.setName("spt4");
        spt5.setName("spt5");
        spt6.setName("spt6");
        spt7.setName("spt7");
        srpSuspiciousConfig.setName("srpSuspiciousConfig");
        tblSuspiciousConfig.setName("tblSuspiciousConfig");
        txtAccNo.setName("txtAccNo");
        txtCountExceeds.setName("txtCountExceeds");
        txtCustNo.setName("txtCustNo");
        txtPeriod.setName("txtPeriod");
        txtWorthExceeds.setName("txtWorthExceeds");
    }
    
    
    
    
    
    
    
/* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        chkCreditClearing.setText(resourceBundle.getString("chkCreditClearing"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblCredit.setText(resourceBundle.getString("lblCredit"));
        btnCustNo.setText(resourceBundle.getString("btnCustNo"));
        lblWorthExceeds.setText(resourceBundle.getString("lblWorthExceeds"));
        chkDebitCash.setText(resourceBundle.getString("chkDebitCash"));
        chkCreditTransfer.setText(resourceBundle.getString("chkCreditTransfer"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        ((javax.swing.border.TitledBorder)panTransaction.getBorder()).setTitle(resourceBundle.getString("panTransaction"));
        chkDebitClearing.setText(resourceBundle.getString("chkDebitClearing"));
        lblCountExceeds.setText(resourceBundle.getString("lblCountExceeds"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        btnSuspiciousNew.setText(resourceBundle.getString("btnSuspiciousNew"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblProdType.setText(resourceBundle.getString("lblProdType"));
        btnSuspiciousSave.setText(resourceBundle.getString("btnSuspiciousSave"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        chkCreditCash.setText(resourceBundle.getString("chkCreditCash"));
        btnAccNo.setText(resourceBundle.getString("btnAccNo"));
        lblPeriod.setText(resourceBundle.getString("lblPeriod"));
        lblClearing.setText(resourceBundle.getString("lblClearing"));
        lblCash.setText(resourceBundle.getString("lblCash"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        lblCustNo.setText(resourceBundle.getString("lblCustNo"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        ((javax.swing.border.TitledBorder)panConfigurationFor.getBorder()).setTitle(resourceBundle.getString("panConfigurationFor"));
        lblConfigurationFor.setText(resourceBundle.getString("lblConfigurationFor"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        lblDebit.setText(resourceBundle.getString("lblDebit"));
        lblProdId.setText(resourceBundle.getString("lblProdId"));
        lblTransfer.setText(resourceBundle.getString("lblTransfer"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblAccNo.setText(resourceBundle.getString("lblAccNo"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        chkDebitTransfer.setText(resourceBundle.getString("chkDebitTransfer"));
        btnSuspiciousDelete.setText(resourceBundle.getString("btnSuspiciousDelete"));
    }
    
    
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        updateFields();
        tblSuspiciousConfig.setModel(observable.getTblSuspiciousConfig());
        updateLblStatus();
    }
    private void updateLblStatus() {
        lblStatus.setText(observable.getLblStatus());
    }
    /* To set Maximum length and for validation */
    private void setMaximumLength() {
        txtAccNo.setMaxLength(16);
        txtCountExceeds.setMaxLength(8);
        txtCountExceeds.setValidation(new NumericValidation());
        txtWorthExceeds.setMaxLength(16);
        txtWorthExceeds.setValidation(new CurrencyValidation());
        txtPeriod.setMaxLength(8);
        txtPeriod.setValidation(new NumericValidation());
    }
    /* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        cboConfigurationFor.setHelpMessage(lblMsg, objMandatoryRB.getString("cboConfigurationFor"));
        cboProdType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProdType"));
        cboProdId.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProdId"));
        txtAccNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAccNo"));
        txtCustNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCustNo"));
        txtCountExceeds.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCountExceeds"));
        txtWorthExceeds.setHelpMessage(lblMsg, objMandatoryRB.getString("txtWorthExceeds"));
        txtPeriod.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPeriod"));
        cboPeriod.setHelpMessage(lblMsg, objMandatoryRB.getString("cboPeriod"));
        chkDebitClearing.setHelpMessage(lblMsg, objMandatoryRB.getString("chkDebitClearing"));
        chkDebitTransfer.setHelpMessage(lblMsg, objMandatoryRB.getString("chkDebitTransfer"));
        chkDebitCash.setHelpMessage(lblMsg, objMandatoryRB.getString("chkDebitCash"));
        chkCreditClearing.setHelpMessage(lblMsg, objMandatoryRB.getString("chkCreditClearing"));
        chkCreditTransfer.setHelpMessage(lblMsg, objMandatoryRB.getString("chkCreditTransfer"));
        chkCreditCash.setHelpMessage(lblMsg, objMandatoryRB.getString("chkCreditCash"));
    }
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setCboConfigurationFor((String) cboConfigurationFor.getSelectedItem());
        observable.setCboProdType((String) cboProdType.getSelectedItem());
        observable.setCboProdId((String) cboProdId.getSelectedItem());
        observable.setTxtAccNo(txtAccNo.getText());
        observable.setTxtCustNo(txtCustNo.getText());
        observable.setTxtCountExceeds(txtCountExceeds.getText());
        observable.setTxtWorthExceeds(txtWorthExceeds.getText());
        observable.setTxtPeriod(txtPeriod.getText());
        observable.setCboPeriod((String) cboPeriod.getSelectedItem());
        observable.setChkDebitClearing(chkDebitClearing.isSelected());
        observable.setChkDebitTransfer(chkDebitTransfer.isSelected());
        observable.setChkDebitCash(chkDebitCash.isSelected());
        observable.setChkCreditClearing(chkCreditClearing.isSelected());
        observable.setChkCreditTransfer(chkCreditTransfer.isSelected());
        observable.setChkCreditCash(chkCreditCash.isSelected());
    }
    
    
/* Auto Generated Method - setMandatoryHashMap()
 
ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
 
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboConfigurationFor", new Boolean(true));
        mandatoryMap.put("cboProdType", new Boolean(true));
        mandatoryMap.put("cboProdId", new Boolean(true));
        mandatoryMap.put("txtAccNo", new Boolean(true));
        mandatoryMap.put("txtCustNo", new Boolean(true));
        mandatoryMap.put("txtCountExceeds", new Boolean(true));
        mandatoryMap.put("txtWorthExceeds", new Boolean(true));
        mandatoryMap.put("txtPeriod", new Boolean(true));
        mandatoryMap.put("cboPeriod", new Boolean(true));
        mandatoryMap.put("chkDebitClearing", new Boolean(true));
        mandatoryMap.put("chkDebitTransfer", new Boolean(true));
        mandatoryMap.put("chkDebitCash", new Boolean(true));
        mandatoryMap.put("chkCreditClearing", new Boolean(true));
        mandatoryMap.put("chkCreditTransfer", new Boolean(true));
        mandatoryMap.put("chkCreditCash", new Boolean(true));
    }
    
/* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    
    
    /** To display a popUp window for viewing existing data */
    private void popUp(int mode) {
        viewType = mode;
        int prodID = 1,NULL =2;
        final HashMap viewMap = new HashMap();
        if ( viewType == EDIT ||  viewType == DELETE ||  viewType == VIEW){
            viewMap.put(CommonConstants.MAP_NAME, "viewSuspiciousConfig");
            new ViewAll(this, viewMap).show();
            
        } else if (mode == ACCOUNT_NO) {
            if (CommonUtil.convertObjToStr(cboProdId.getSelectedItem()).length()>0) {
                HashMap where = new HashMap();
                viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList"
                + CommonUtil.convertObjToStr(((ComboBoxModel)cboProdType.getModel()).getKeyForSelected()));
                where.put("PROD_ID", ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
                where.put("SELECTED_BRANCH", TrueTransactMain.BRANCH_ID);
                viewMap.put(CommonConstants.MAP_WHERE, where);
                where = null;
                new ViewAll(this, viewMap, true).show();
                
            } else {
                prodID = NULL;
                displayAlert(objMandatoryRB.getString("cboProdId"));
            }
        } else if (mode == CUSTOMER_NO) {
            viewMap.put(CommonConstants.MAP_NAME, "getCustomerList");
            new ViewAll(this, viewMap, true).show();
        }
        //        if (viewType == EDIT || viewType == DELETE || viewType == CUSTOMER_NO || viewType == ACCOUNT_NO && prodID != NULL) {
        //            new ViewAll(this, viewMap).show();
        //        }
    }
    
    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
        if (viewType != -1) {
            if (viewType == EDIT || viewType == DELETE || viewType == AUTHORIZE || viewType == VIEW) {
                isFilled = true;
                CONF_KEY = CommonUtil.convertObjToStr(hash.get("CONF_KEY"));
                hash.put(CommonConstants.MAP_WHERE, CONF_KEY);
                observable.populateData(hash);
                setButtonEnableDisable();
                prodIdTypeEditableTrueFalse(false);
                
                if (viewType == EDIT) {
                    enableDisable_SaveDelete();
                }else{
                    ClientUtil.enableDisable(this, false);
                    enableDisable_NewSaveDelete(false);
                    enableDisableHelpButtons(false);
                    txtAccNo.setEditable(false);
                    txtCustNo.setEditable(false);
                }
            }else if (viewType==ACCOUNT_NO) {
                txtAccNo.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
            }else if (viewType==CUSTOMER_NO) {
                txtCustNo.setText(CommonUtil.convertObjToStr(hash.get("CUSTOMERID")));
            }
            if(viewType==AUTHORIZE){
                btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
            }
            observable.setStatus();
            updateLblStatus();
        }
        
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        tbrSuspiciousConfig = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace26 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace27 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace28 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace29 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace30 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace31 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panMain = new com.see.truetransact.uicomponent.CPanel();
        panConfigurationFor = new com.see.truetransact.uicomponent.CPanel();
        lblConfigurationFor = new com.see.truetransact.uicomponent.CLabel();
        lblProdType = new com.see.truetransact.uicomponent.CLabel();
        lblProdId = new com.see.truetransact.uicomponent.CLabel();
        lblAccNo = new com.see.truetransact.uicomponent.CLabel();
        lblCustNo = new com.see.truetransact.uicomponent.CLabel();
        cboConfigurationFor = new com.see.truetransact.uicomponent.CComboBox();
        cboProdType = new com.see.truetransact.uicomponent.CComboBox();
        cboProdId = new com.see.truetransact.uicomponent.CComboBox();
        txtAccNo = new com.see.truetransact.uicomponent.CTextField();
        txtCustNo = new com.see.truetransact.uicomponent.CTextField();
        btnAccNo = new com.see.truetransact.uicomponent.CButton();
        btnCustNo = new com.see.truetransact.uicomponent.CButton();
        panTransaction = new com.see.truetransact.uicomponent.CPanel();
        lblCountExceeds = new com.see.truetransact.uicomponent.CLabel();
        lblWorthExceeds = new com.see.truetransact.uicomponent.CLabel();
        lblPeriod = new com.see.truetransact.uicomponent.CLabel();
        txtCountExceeds = new com.see.truetransact.uicomponent.CTextField();
        txtWorthExceeds = new com.see.truetransact.uicomponent.CTextField();
        panPeriod = new com.see.truetransact.uicomponent.CPanel();
        txtPeriod = new com.see.truetransact.uicomponent.CTextField();
        cboPeriod = new com.see.truetransact.uicomponent.CComboBox();
        panCreditDebit = new com.see.truetransact.uicomponent.CPanel();
        lblClearing = new com.see.truetransact.uicomponent.CLabel();
        lblTransfer = new com.see.truetransact.uicomponent.CLabel();
        lblCash = new com.see.truetransact.uicomponent.CLabel();
        chkDebitClearing = new com.see.truetransact.uicomponent.CCheckBox();
        chkDebitTransfer = new com.see.truetransact.uicomponent.CCheckBox();
        chkDebitCash = new com.see.truetransact.uicomponent.CCheckBox();
        chkCreditClearing = new com.see.truetransact.uicomponent.CCheckBox();
        chkCreditTransfer = new com.see.truetransact.uicomponent.CCheckBox();
        chkCreditCash = new com.see.truetransact.uicomponent.CCheckBox();
        spt1 = new com.see.truetransact.uicomponent.CSeparator();
        spt3 = new com.see.truetransact.uicomponent.CSeparator();
        spt2 = new com.see.truetransact.uicomponent.CSeparator();
        spt4 = new com.see.truetransact.uicomponent.CSeparator();
        spt5 = new com.see.truetransact.uicomponent.CSeparator();
        spt6 = new com.see.truetransact.uicomponent.CSeparator();
        spt7 = new com.see.truetransact.uicomponent.CSeparator();
        lblCredit = new com.see.truetransact.uicomponent.CLabel();
        lblDebit = new com.see.truetransact.uicomponent.CLabel();
        panTable = new com.see.truetransact.uicomponent.CPanel();
        srpSuspiciousConfig = new com.see.truetransact.uicomponent.CScrollPane();
        tblSuspiciousConfig = new com.see.truetransact.uicomponent.CTable();
        panButtons = new com.see.truetransact.uicomponent.CPanel();
        btnSuspiciousNew = new com.see.truetransact.uicomponent.CButton();
        btnSuspiciousSave = new com.see.truetransact.uicomponent.CButton();
        btnSuspiciousDelete = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        mbrSuspiciousConfig = new com.see.truetransact.uicomponent.CMenuBar();
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
        setTitle("Suspicious Config");
        setMinimumSize(new java.awt.Dimension(600, 500));
        setPreferredSize(new java.awt.Dimension(600, 500));

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
        tbrSuspiciousConfig.add(btnView);

        lblSpace5.setText("     ");
        tbrSuspiciousConfig.add(lblSpace5);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrSuspiciousConfig.add(btnNew);

        lblSpace26.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace26.setText("     ");
        lblSpace26.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrSuspiciousConfig.add(lblSpace26);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrSuspiciousConfig.add(btnEdit);

        lblSpace27.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace27.setText("     ");
        lblSpace27.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrSuspiciousConfig.add(lblSpace27);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrSuspiciousConfig.add(btnDelete);

        lblSpace2.setText("     ");
        tbrSuspiciousConfig.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrSuspiciousConfig.add(btnSave);

        lblSpace28.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace28.setText("     ");
        lblSpace28.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrSuspiciousConfig.add(lblSpace28);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrSuspiciousConfig.add(btnCancel);

        lblSpace3.setText("     ");
        tbrSuspiciousConfig.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrSuspiciousConfig.add(btnAuthorize);

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrSuspiciousConfig.add(lblSpace29);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrSuspiciousConfig.add(btnException);

        lblSpace30.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace30.setText("     ");
        lblSpace30.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrSuspiciousConfig.add(lblSpace30);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrSuspiciousConfig.add(btnReject);

        lblSpace4.setText("     ");
        tbrSuspiciousConfig.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrSuspiciousConfig.add(btnPrint);

        lblSpace31.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace31.setText("     ");
        lblSpace31.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrSuspiciousConfig.add(lblSpace31);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrSuspiciousConfig.add(btnClose);

        getContentPane().add(tbrSuspiciousConfig, java.awt.BorderLayout.NORTH);

        panMain.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panMain.setMinimumSize(new java.awt.Dimension(460, 350));
        panMain.setPreferredSize(new java.awt.Dimension(460, 350));
        panMain.setLayout(new java.awt.GridBagLayout());

        panConfigurationFor.setBorder(javax.swing.BorderFactory.createTitledBorder("Configuration For"));
        panConfigurationFor.setLayout(new java.awt.GridBagLayout());

        lblConfigurationFor.setText("Configuration For");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panConfigurationFor.add(lblConfigurationFor, gridBagConstraints);

        lblProdType.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panConfigurationFor.add(lblProdType, gridBagConstraints);

        lblProdId.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panConfigurationFor.add(lblProdId, gridBagConstraints);

        lblAccNo.setText("Account No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panConfigurationFor.add(lblAccNo, gridBagConstraints);

        lblCustNo.setText("Customer No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panConfigurationFor.add(lblCustNo, gridBagConstraints);

        cboConfigurationFor.setMaximumSize(new java.awt.Dimension(100, 21));
        cboConfigurationFor.setMinimumSize(new java.awt.Dimension(100, 21));
        cboConfigurationFor.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboConfigurationForItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panConfigurationFor.add(cboConfigurationFor, gridBagConstraints);

        cboProdType.setMaximumSize(new java.awt.Dimension(100, 21));
        cboProdType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panConfigurationFor.add(cboProdType, gridBagConstraints);

        cboProdId.setMaximumSize(new java.awt.Dimension(100, 21));
        cboProdId.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panConfigurationFor.add(cboProdId, gridBagConstraints);

        txtAccNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panConfigurationFor.add(txtAccNo, gridBagConstraints);

        txtCustNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panConfigurationFor.add(txtCustNo, gridBagConstraints);

        btnAccNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAccNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        panConfigurationFor.add(btnAccNo, gridBagConstraints);

        btnCustNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnCustNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnCustNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        panConfigurationFor.add(btnCustNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        panMain.add(panConfigurationFor, gridBagConstraints);

        panTransaction.setBorder(javax.swing.BorderFactory.createTitledBorder("Transaction"));
        panTransaction.setLayout(new java.awt.GridBagLayout());

        lblCountExceeds.setText("Count Exceeds");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransaction.add(lblCountExceeds, gridBagConstraints);

        lblWorthExceeds.setText("Worth Exceeds");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransaction.add(lblWorthExceeds, gridBagConstraints);

        lblPeriod.setText("Period");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransaction.add(lblPeriod, gridBagConstraints);

        txtCountExceeds.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransaction.add(txtCountExceeds, gridBagConstraints);

        txtWorthExceeds.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransaction.add(txtWorthExceeds, gridBagConstraints);

        panPeriod.setLayout(new java.awt.GridBagLayout());

        txtPeriod.setMinimumSize(new java.awt.Dimension(50, 21));
        txtPeriod.setPreferredSize(new java.awt.Dimension(50, 21));
        txtPeriod.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPeriodFocusLost(evt);
            }
        });
        panPeriod.add(txtPeriod, new java.awt.GridBagConstraints());

        cboPeriod.setMinimumSize(new java.awt.Dimension(88, 21));
        cboPeriod.setPreferredSize(new java.awt.Dimension(88, 21));
        panPeriod.add(cboPeriod, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTransaction.add(panPeriod, gridBagConstraints);

        panCreditDebit.setName("cPanel_Five");
        panCreditDebit.setLayout(new java.awt.GridBagLayout());

        lblClearing.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblClearing.setText("Clearing");
        lblClearing.setMaximumSize(new java.awt.Dimension(42, 15));
        lblClearing.setMinimumSize(new java.awt.Dimension(42, 15));
        lblClearing.setName("lblClearing");
        lblClearing.setPreferredSize(new java.awt.Dimension(42, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCreditDebit.add(lblClearing, gridBagConstraints);

        lblTransfer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTransfer.setText("Transfer");
        lblTransfer.setName("lblTransfer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCreditDebit.add(lblTransfer, gridBagConstraints);

        lblCash.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCash.setText("Cash");
        lblCash.setMaximumSize(new java.awt.Dimension(42, 15));
        lblCash.setMinimumSize(new java.awt.Dimension(42, 15));
        lblCash.setName("lblCash");
        lblCash.setPreferredSize(new java.awt.Dimension(42, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCreditDebit.add(lblCash, gridBagConstraints);

        chkDebitClearing.setName("chkDebitClearing");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCreditDebit.add(chkDebitClearing, gridBagConstraints);

        chkDebitTransfer.setName("chkCreditTransfer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCreditDebit.add(chkDebitTransfer, gridBagConstraints);

        chkDebitCash.setName("chkCreditCash");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCreditDebit.add(chkDebitCash, gridBagConstraints);

        chkCreditClearing.setName("chkDebitClearing");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCreditDebit.add(chkCreditClearing, gridBagConstraints);

        chkCreditTransfer.setName("chkDebitTransfer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCreditDebit.add(chkCreditTransfer, gridBagConstraints);

        chkCreditCash.setName("chkDebitCash");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCreditDebit.add(chkCreditCash, gridBagConstraints);

        spt1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        panCreditDebit.add(spt1, gridBagConstraints);

        spt3.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        panCreditDebit.add(spt3, gridBagConstraints);

        spt2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        panCreditDebit.add(spt2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panCreditDebit.add(spt4, gridBagConstraints);

        spt5.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        panCreditDebit.add(spt5, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panCreditDebit.add(spt6, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panCreditDebit.add(spt7, gridBagConstraints);

        lblCredit.setText("Credit");
        lblCredit.setName("lblCredit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 50, 4, 8);
        panCreditDebit.add(lblCredit, gridBagConstraints);

        lblDebit.setText("Debit");
        lblDebit.setName("lblDebit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 50, 4, 8);
        panCreditDebit.add(lblDebit, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransaction.add(panCreditDebit, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panMain.add(panTransaction, gridBagConstraints);

        panTable.setMinimumSize(new java.awt.Dimension(300, 299));
        panTable.setPreferredSize(new java.awt.Dimension(300, 299));
        panTable.setLayout(new java.awt.GridBagLayout());

        srpSuspiciousConfig.setMinimumSize(new java.awt.Dimension(320, 299));
        srpSuspiciousConfig.setPreferredSize(new java.awt.Dimension(320, 299));

        tblSuspiciousConfig.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SI No", "Configuration", "Count", "Worth"
            }
        ));
        tblSuspiciousConfig.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblSuspiciousConfigMousePressed(evt);
            }
        });
        srpSuspiciousConfig.setViewportView(tblSuspiciousConfig);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panTable.add(srpSuspiciousConfig, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panMain.add(panTable, gridBagConstraints);

        panButtons.setLayout(new java.awt.GridBagLayout());

        btnSuspiciousNew.setText("New");
        btnSuspiciousNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuspiciousNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButtons.add(btnSuspiciousNew, gridBagConstraints);

        btnSuspiciousSave.setText("Save");
        btnSuspiciousSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuspiciousSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButtons.add(btnSuspiciousSave, gridBagConstraints);

        btnSuspiciousDelete.setText("Delete");
        btnSuspiciousDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuspiciousDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButtons.add(btnSuspiciousDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        panMain.add(panButtons, gridBagConstraints);

        getContentPane().add(panMain, java.awt.BorderLayout.CENTER);

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

        mnuProcess.setText("Process");

        mitNew.setText("New");
        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mnuProcess.add(mitDelete);
        mnuProcess.add(sptDelete);

        mitSave.setText("Save");
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancel");
        mnuProcess.add(mitCancel);
        mnuProcess.add(sptCancel);

        mitClose.setText("Close");
        mnuProcess.add(mitClose);

        mbrSuspiciousConfig.add(mnuProcess);

        setJMenuBar(mbrSuspiciousConfig);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtPeriodFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPeriodFocusLost
        // TODO add your handling code here:
        if (CommonUtil.convertObjToDouble(txtPeriod.getText()).doubleValue() <= 0){
            ClientUtil.displayAlert("period should not be zero or empty");
            txtPeriod.setText("");
        }
      
    }//GEN-LAST:event_txtPeriodFocusLost

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        viewType = VIEW;
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        popUp(VIEW);
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    public void authorizeStatus(String authorizeStatus) {
        if (viewType != AUTHORIZE){
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            //__ To Save the data in the Internal Frame...
            setModified(true);
            
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            mapParam.put(CommonConstants.MAP_NAME, "getSelectSuspiciousConfigList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authSuspiciousConfig");
            
            isFilled = false;
            
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            
//            lblStatus.setText(ClientConstants.RESULT_STATUS[authorizeUI.getResultStatus()]);
            lblStatus.setText(ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_AUTHORIZE]);
            btnSave.setEnabled(false);
            
            //__ If there's no data to be Authorized, call Cancel action...  
            if(!isModified()){
                setButtonEnableDisable();
                btnCancelActionPerformed(null);
            }
        } else if (viewType == AUTHORIZE  && isFilled){
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT,ClientUtil.getCurrentDate());
            singleAuthorizeMap.put("CONF_KEY",CONF_KEY);
            
            boolean result = ClientUtil.executeWithResult("authSuspiciousConfig", singleAuthorizeMap);
            viewType = -1;
            btnSave.setEnabled(false);
            btnCancelActionPerformed(null);
            if (result)  lblStatus.setText(ClientConstants.RESULT_STATUS[ClientConstants.ACTIONTYPE_AUTHORIZE]);
        }
    }
    private void cboConfigurationForItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboConfigurationForItemStateChanged
        // TODO add your handling code here:
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            accessLevel();
        }
    }//GEN-LAST:event_cboConfigurationForItemStateChanged
    
    private void cboProdTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdTypeActionPerformed
        // TODO add your handling code here:
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            if (cboProdType.getSelectedIndex() > 0) {
                final String prodType = CommonUtil.convertObjToStr(((ComboBoxModel)cboProdType.getModel()).getKeyForSelected());
                if(prodType.length() > 0){
                    observable.setCboProdType(prodType);
                    observable.setCbmProdId(prodType);
                    cboProdId.setModel(observable.getCbmProdId());
                    if (tblPress != true){
                        observable.setTxtAccNo("");
                        txtAccNo.setText(observable.getTxtAccNo());
                    }
                }
            }
        }
    }//GEN-LAST:event_cboProdTypeActionPerformed
   private void btnCheck(){
         btnCancel.setEnabled(true);
         btnSave.setEnabled(false);
         btnNew.setEnabled(false);
         btnDelete.setEnabled(false);
         btnAuthorize.setEnabled(false);
         btnReject.setEnabled(false);
         btnException.setEnabled(false);
         btnEdit.setEnabled(false);
     }
    
    private void accessLevel() {
        if (((String)cboConfigurationFor.getSelectedItem()).length()>0){
            configurationFor = CommonUtil.convertObjToStr((((ComboBoxModel)(cboConfigurationFor).getModel())).getKeyForSelected());
            LEVEL = observable.getLevel(configurationFor);
            configurationFor = null;
            if (LEVEL == GENERAL) {
                enableDisablePanTransaction(true);
                
            } else if (LEVEL == ACCOUNT) {
                enableDisableConfigLevel(true);
                
            } else if (LEVEL == CUSTOMER) {
                enableDisableConfigLevel(false);
                
            }
            
            if (tblPress != true) {
                observable.resetFields();
                updateCustLevel();
                updateTrans();
                updateAccLevel();
            }
        }
    }
    private void tblSuspiciousConfigMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSuspiciousConfigMousePressed
        // TODO add your handling code here:
        if (tblSuspiciousConfig.getSelectedRow() >= 0){
            if(observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && observable.getActionType() != ClientConstants.ACTIONTYPE_VIEW
               && viewType != AUTHORIZE) {
                enableDisableTable(true);
                enableDisable_NewSaveDelete(true);// enable New Save Delete buttons
            }
            tblPress = true;// table row is clicked
            
            configurationFor = observable.populateSuspiciousConfig(tblSuspiciousConfig.getSelectedRow());
            if (configurationFor != null) {
                accessLevel();
            }
            cboConfigurationFor.setEnabled(true);
            //            cboConfigurationFor.setEditable(true);
            //updateFields();
            observable.ttNotifyObservers();
        }
    }//GEN-LAST:event_tblSuspiciousConfigMousePressed
     private String validateSelection(int level){
        MandatoryCheck objMandatory = new MandatoryCheck();
        SuspiciousConfigHashMap objMap = new SuspiciousConfigHashMap();
        HashMap mandatoryMap = objMap.getMandatoryHashMap();
//        String prodType=((ComboBoxModel)cboProductType.getModel()).getKeyForSelected().toString();
//        String transType = CommonUtil.convertObjToStr((((ComboBoxModel)(cboTransType).getModel())).getKeyForSelected());
//        System.out.println("transType : " + transType + " ......... getTransactionMode = " + getTransactionMode());
//        if (getTransactionMode().equals(CommonConstants.CREDIT)) {
        if(level == GENERAL){
            mandatoryMap.put("cboProdType", new Boolean(true));
            mandatoryMap.put("cboProdId", new Boolean(true));
        }
        if(level == CUSTOMER){
            mandatoryMap.put("txtCustNo", new Boolean(true));
//            mandatoryMap.put("cboProdId", new Boolean(true));
        }
        if(level == ACCOUNT){
            mandatoryMap.put("cboProdType", new Boolean(true));
            mandatoryMap.put("cboProdId", new Boolean(true));
            mandatoryMap.put("txtAccNo", new Boolean(true));
        }
//            mandatoryMap.put("txtChequeNo", new Boolean(false));
//            mandatoryMap.put("txtChequeNo2", new Boolean(false));
//        }else if (getTransactionMode().equals(CommonConstants.DEBIT) && transType.equals("CASH")) {
//            mandatoryMap.put("cboInstrumentType", new Boolean(false));
//            mandatoryMap.put("tdtChequeDate", new Boolean(false));
//            mandatoryMap.put("txtChequeNo", new Boolean(false));
//            mandatoryMap.put("txtChequeNo2", new Boolean(false));
//            if(getSourceScreen().equals("DEPOSITS") || getSourceScreen().equals("ACT_CLOSING") || getSourceScreen().equals("REMITPAYMENT"))
//                mandatoryMap.put("txtTokenNo",new Boolean(true));
//        }else{
//            mandatoryMap.put("cboInstrumentType", new Boolean(true));
//            mandatoryMap.put("tdtChequeDate", new Boolean(true));
//            mandatoryMap.put("txtTransProductId", new Boolean(true));
//            mandatoryMap.put("cboProductType", new Boolean(true));
//            mandatoryMap.put("txtDebitAccNo", new Boolean(true));
//            if(prodType.equals("GL"))
//                mandatoryMap.put("txtTransProductId", new Boolean(false));
//            if(((getSourceScreen().equals("DEPOSITS")) || (getSourceScreen().equals("ACT_CLOSING"))) && prodType.equals("RM")){
//                mandatoryMap.put("cboInstrumentType", new Boolean(true));
//                mandatoryMap.put("txtChequeNo", new Boolean(true));
//                mandatoryMap.put("txtChequeNo2", new Boolean(true));
//            }
//        }
//        if(getSourceScreen() == "REMITPAYMENT"){
//            mandatoryMap.put("cboInstrumentType", new Boolean(false));
//            mandatoryMap.put("tdtChequeDate", new Boolean(false));
//        }
//        if (getSourceScreen().equals("DEPOSITS") && prodType.equals("RM")){
//            mandatoryMap.remove("txtTransProductId");
//            mandatoryMap.put("cboInstrumentType", new Boolean(true));
//            mandatoryMap.put("txtChequeNo", new Boolean(true));
//            mandatoryMap.put("txtChequeNo2", new Boolean(true));            
//        }else if(getSourceScreen().equals("DEPOSITS") || observable.isDepFlag() && !prodType.equals("RM")){
//            mandatoryMap.remove("cboInstrumentType");
//            mandatoryMap.remove("tdtChequeDate");
//            mandatoryMap.remove("txtTransProductId");
//        }
//         if ((getSourceScreen().equals("ACT_CLOSING")) && (prodType.equals("RM"))){
//            mandatoryMap.remove("cboInstrumentType");
//            mandatoryMap.remove("tdtChequeDate");
//            mandatoryMap.remove("txtTransProductId");
//         }
        String mandatoryMessage = objMandatory.checkMandatory(getClass().getName(),panConfigurationFor, mandatoryMap);
        System.out.println("mandatoryMessage : " + mandatoryMessage);
        return mandatoryMessage;
    }
    private void btnSuspiciousDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuspiciousDeleteActionPerformed
        // TODO add your handling code here:
        if (tblSuspiciousConfig.getSelectedRow() >= 0) {
            observable.deleteSuspiciousConfig(tblSuspiciousConfig.getSelectedRow());
            enableDisable_SaveDelete();
            enableDisableHelpButtons(false);
            tblPress = false;
            observable.resetFields();
            //            enableDisableCustLevel(false);
            //            enableDisableAccLevel(false);
            enableDisablePanMain(false);
            prodIdTypeEditableTrueFalse(false);
            //            enableDisablePanTransaction(false);
            updateFields();
        }
    }//GEN-LAST:event_btnSuspiciousDeleteActionPerformed
    
    private void btnSuspiciousSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuspiciousSaveActionPerformed
        // TODO add your handling code here:
        if(cboConfigurationFor.getSelectedItem().equals("")){
            displayAlert("Select Configuration For");
        }else{
        String mandatoryMessage = "";
        if (LEVEL == GENERAL) {
            mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panTransaction);
            mandatoryMessage+=validateSelection(LEVEL);
        } else if (LEVEL == ACCOUNT) {
            mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panTransaction);
            mandatoryMessage+=validateSelection(LEVEL);
            if (!(txtAccNo.getText().length()>0)){
                mandatoryMessage += objMandatoryRB.getString("txtAccNo");
            }
        } else if (LEVEL == CUSTOMER) {
            mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panTransaction);
            mandatoryMessage+=validateSelection(LEVEL);
            if (!(txtCustNo.getText().length()>0))
                mandatoryMessage += objMandatoryRB.getString("txtCustNo");
        }
        
        if (mandatoryMessage.length() > 0) {
            displayAlert(mandatoryMessage);
        } else {
            updateOBFields();
            observable.saveSuspiciousConfig(tblPress,tblSuspiciousConfig.getSelectedRow());
            enableDisablePanMain(false);
            enableDisable_SaveDelete();
            enableDisableHelpButtons(false);
            prodIdTypeEditableTrueFalse(false);
            observable.resetFields();
            updateFields();
        }
        }
    }//GEN-LAST:event_btnSuspiciousSaveActionPerformed
    private void enableDisablePanMain(boolean flag) {
        ClientUtil.enableDisable(panMain,flag);
    }
    private void prodIdTypeEditableTrueFalse(boolean flag) {
        cboProdId.setEditable(flag);
        cboProdType.setEditable(flag);
    }
    private void btnSuspiciousNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuspiciousNewActionPerformed
        // TODO add your handling code here:
        observable.resetFields();
        updateFields();
        tblPress = false;
        enableDisable_NewSaveDelete(true);
        cboConfigurationFor.setEnabled(true);
        
    }//GEN-LAST:event_btnSuspiciousNewActionPerformed
    
    private void btnCustNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustNoActionPerformed
        // TODO add your handling code here:
        popUp(CUSTOMER_NO);
    }//GEN-LAST:event_btnCustNoActionPerformed
    
    private void btnAccNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccNoActionPerformed
        // TODO add your handling code here:
        popUp(ACCOUNT_NO);
    }//GEN-LAST:event_btnAccNoActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        cifClosingAlert();
        //        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        ClientUtil.enableDisable(this, false);
        setButtonEnableDisable();
        enableDisable_NewSaveDelete(false);
        enableDisableHelpButtons(false);
        prodIdTypeEditableTrueFalse(false);
        observable.resetForm();
        observable.setStatus();
        observable.ttNotifyObservers();
        updateLblStatus();
        
        //__ Make the Screen Closable..
        setModified(false);
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true); 
        isFilled = false;
        viewType = -1;
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        final String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panMain);
        if (tblSuspiciousConfig.getRowCount() == 0){
            displayAlert(mandatoryMessage);
        }
        else {
            savePerformed();
        }
       btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true); 
    }//GEN-LAST:event_btnSaveActionPerformed
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }
    /* action to perform when  main save button is pressed */
    private void savePerformed(){
        updateOBFields();
        observable.doAction();
        ClientUtil.enableDisable(this, false);
        enableDisable_NewSaveDelete(false);
        setButtonEnableDisable();
        enableDisableHelpButtons(false);
        prodIdTypeEditableTrueFalse(false);
        observable.resetForm();
        observable.setResultStatus();
        
        //__ Make the Screen Closable..
        setModified(false);
    }
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        viewType = EDIT;
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        popUp(DELETE);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        viewType = EDIT;
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        popUp(EDIT);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        viewType = NEW;
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        enableDisable_SaveDelete();
        //        cboConfigurationFor.setEnabled(true);
        setButtonEnableDisable();
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        observable.setStatus();
        updateLblStatus();
        observable.resetForm();
        
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }//GEN-LAST:event_btnNewActionPerformed
    private void updateFields() {
        cboConfigurationFor.setSelectedItem(observable.getCboConfigurationFor());
        updateAccLevel();
        updateCustLevel();
        updateTrans();
    }
    private void updateAccLevel() {
        txtAccNo.setText(observable.getTxtAccNo());
        cboProdType.setSelectedItem(observable.getCboProdType());
        
        if (observable.getCboProdId()!=null) {
            cboProdId.setModel(observable.getCbmProdId());
            cboProdId.setSelectedItem(observable.getCboProdId());
        }
    }
    private void updateCustLevel() {
        txtCustNo.setText(observable.getTxtCustNo());
    }
    private void updateTrans() {
        txtCountExceeds.setText(observable.getTxtCountExceeds());
        chkDebitClearing.setSelected(observable.getChkDebitClearing());
        chkDebitTransfer.setSelected(observable.getChkDebitTransfer());
        chkDebitCash.setSelected(observable.getChkDebitCash());
        chkCreditClearing.setSelected(observable.getChkCreditClearing());
        chkCreditTransfer.setSelected(observable.getChkCreditTransfer());
        chkCreditCash.setSelected(observable.getChkCreditCash());
        cboPeriod.setSelectedItem(observable.getCboPeriod());
        txtWorthExceeds.setText(observable.getTxtWorthExceeds());
        txtPeriod.setText(observable.getTxtPeriod());
    }
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAccNo;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnCustNo;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnSuspiciousDelete;
    private com.see.truetransact.uicomponent.CButton btnSuspiciousNew;
    private com.see.truetransact.uicomponent.CButton btnSuspiciousSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboConfigurationFor;
    private com.see.truetransact.uicomponent.CComboBox cboPeriod;
    private com.see.truetransact.uicomponent.CComboBox cboProdId;
    private com.see.truetransact.uicomponent.CComboBox cboProdType;
    private com.see.truetransact.uicomponent.CCheckBox chkCreditCash;
    private com.see.truetransact.uicomponent.CCheckBox chkCreditClearing;
    private com.see.truetransact.uicomponent.CCheckBox chkCreditTransfer;
    private com.see.truetransact.uicomponent.CCheckBox chkDebitCash;
    private com.see.truetransact.uicomponent.CCheckBox chkDebitClearing;
    private com.see.truetransact.uicomponent.CCheckBox chkDebitTransfer;
    private com.see.truetransact.uicomponent.CLabel lblAccNo;
    private com.see.truetransact.uicomponent.CLabel lblCash;
    private com.see.truetransact.uicomponent.CLabel lblClearing;
    private com.see.truetransact.uicomponent.CLabel lblConfigurationFor;
    private com.see.truetransact.uicomponent.CLabel lblCountExceeds;
    private com.see.truetransact.uicomponent.CLabel lblCredit;
    private com.see.truetransact.uicomponent.CLabel lblCustNo;
    private com.see.truetransact.uicomponent.CLabel lblDebit;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblPeriod;
    private com.see.truetransact.uicomponent.CLabel lblProdId;
    private com.see.truetransact.uicomponent.CLabel lblProdType;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace26;
    private com.see.truetransact.uicomponent.CLabel lblSpace27;
    private com.see.truetransact.uicomponent.CLabel lblSpace28;
    private com.see.truetransact.uicomponent.CLabel lblSpace29;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace30;
    private com.see.truetransact.uicomponent.CLabel lblSpace31;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTransfer;
    private com.see.truetransact.uicomponent.CLabel lblWorthExceeds;
    private com.see.truetransact.uicomponent.CMenuBar mbrSuspiciousConfig;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panButtons;
    private com.see.truetransact.uicomponent.CPanel panConfigurationFor;
    private com.see.truetransact.uicomponent.CPanel panCreditDebit;
    private com.see.truetransact.uicomponent.CPanel panMain;
    private com.see.truetransact.uicomponent.CPanel panPeriod;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTable;
    private com.see.truetransact.uicomponent.CPanel panTransaction;
    private com.see.truetransact.uicomponent.CSeparator spt1;
    private com.see.truetransact.uicomponent.CSeparator spt2;
    private com.see.truetransact.uicomponent.CSeparator spt3;
    private com.see.truetransact.uicomponent.CSeparator spt4;
    private com.see.truetransact.uicomponent.CSeparator spt5;
    private com.see.truetransact.uicomponent.CSeparator spt6;
    private com.see.truetransact.uicomponent.CSeparator spt7;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private com.see.truetransact.uicomponent.CScrollPane srpSuspiciousConfig;
    private com.see.truetransact.uicomponent.CTable tblSuspiciousConfig;
    private javax.swing.JToolBar tbrSuspiciousConfig;
    private com.see.truetransact.uicomponent.CTextField txtAccNo;
    private com.see.truetransact.uicomponent.CTextField txtCountExceeds;
    private com.see.truetransact.uicomponent.CTextField txtCustNo;
    private com.see.truetransact.uicomponent.CTextField txtPeriod;
    private com.see.truetransact.uicomponent.CTextField txtWorthExceeds;
    // End of variables declaration//GEN-END:variables
    public static void main(String st[]){
        javax.swing.JFrame frm = new javax.swing.JFrame();
        SuspiciousConfigUI obj = new SuspiciousConfigUI();
        frm.getContentPane().add(obj);
        obj.show();
        frm.setSize(700, 700);
        frm.show();
        
    }
    
}
