/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * OtherBankUI.java
 *
 * Created on December 30, 2004, 12:26 PM
 */

package com.see.truetransact.ui.sysadmin.otherbank;

/**
 *
 * @author  152715
 *  @modified : Sunil
 *      Added Edit Locking - 07-07-2005
 */
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Observer;



import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.common.viewall.TableDialogUI;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.uicomponent.CButtonGroup;// To add and Remove the Radio Buttons...
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.PincodeValidation_IN;
import com.see.truetransact.uivalidation.DefaultValidation;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.TrueTransactMain;

public class OtherBankUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer , UIMandatoryField  {
    // Variable Declarations
    private OtherBankOB observable;
    final OtherBankRB  resourceBundle = new OtherBankRB();
    private boolean tblOtherBankBranchPress = false;
    private HashMap mandatoryMap;
    private int viewType = -1;
    private String accountHead = "ACCOUNT HEAD";
    private final int NEW=0, EDIT=1,DELETE=2,AUTHORIZE=3, VIEW =4;
    private int ACCOUNT_HEAD = 7;
    boolean isFilled = false;
    boolean bankCodeExist = false;
    /** Creates new form OtherBankUI */
    public OtherBankUI() {
        initComponents();
        initStartUp();
    }
    private void initStartUp() {
        setFieldNames();
        internationalize();
        setObservable();
        initComponentData();
        setMandatoryHashMap();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panMain);
        setMaximumLength();
        setHelpMessage();
//        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panOtherBank);
        setButtonEnableDisable();
        ClientUtil.enableDisable(this, false);
        enableDisableOtherBankBranch_NewSaveDelete(false);
        observable.resetForm();
        observable.resetStatus();
        btnAccountHead.setEnabled(false);
    }
    /* To set Maximum length and for validation */
    private void setMaximumLength() {
        txtBankName.setMaxLength(128);
        txtBankCode.setMaxLength(16);
        txtOtherBranchCode.setMaxLength(32);
        txtOtherBranchShortName.setMaxLength(32);
        txtBranchName.setMaxLength(128);
        txtAddress.setMaxLength(64);
        txtMICR.setMaxLength(16);
        txtPincode.setMaxLength(8);
        txtPincode.setValidation(new PincodeValidation_IN());
        txtOtherBranchCode.setValidation(new NumericValidation(ClientConstants.INSTRUMENT_NO2, 0));
        txtBankCode.setValidation(new NumericValidation(ClientConstants.INSTRUMENT_NO2, 0));
        txtBankCode.setAllowNumber(true);
        txtBankCode.setAllowAll(false);
        txtOtherBranchCode.setAllowAll(false);
        txtOtherBranchCode.setAllowNumber(true);
        txtPhoneNo.setValidation(new NumericValidation(ClientConstants.INSTRUMENT_NO2,0));
         txtPhoneNo.setAllowNumber(true);
        txtPhoneNo.setAllowAll(false);
    }
    
    private void setObservable() {
        /* Implementing Singleton pattern */
        observable = OtherBankOB.getInstance();
        observable.addObserver(this);
    }
    /**
     * EnableDisable New Save Delete Buttons of OtherBankBranch Table
     * When New is pressed
     */
    private void enableDisableOtherBankBranch_NewSaveDelete(boolean flag) {
        btnOtherBankNew.setEnabled(flag);
        btnOtherBankSave.setEnabled(flag);
        btnOtherBankDelete.setEnabled(flag);
    }
    /**
     * Enable Disable New Save Delete Buttons of OtherBankBranch Table
     * When Save or Delete is invoked
     */
    private void enableDisableOtherBankBranch_SaveDelete() {
        btnOtherBankNew.setEnabled(true);
        btnOtherBankSave.setEnabled(false);
        btnOtherBankDelete.setEnabled(false);
    }
    /** To display a popUp window for viewing existing data */
    private void popUp() {
        final HashMap viewMap = new HashMap();
        if ( observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ||  observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW){
            ArrayList lst = new ArrayList();
            lst.add("BANK_CODE");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            viewMap.put(CommonConstants.MAP_NAME, "viewOtherBank");
        }
        new ViewAll(this, viewMap).show();
    }
    // Called Automatically when viewAll() is Called...
    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
        if(viewType == ACCOUNT_HEAD){
            //Added By Suresh
            String prodType = ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString();
            if( prodType != null && prodType.equals("GL")){
                txtAccountHeadValue.setText(CommonUtil.convertObjToStr(hash.get(accountHead)));
            }else{
                txtAccountHeadValue.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
            }
        }else{
            hash.put("WHERE", (String)hash.get("BANK_CODE"));
            observable.populateData(hash);
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ) {
                enableDisablePanOtherBank(true);
                enableDisableBankCode(false);
                setTableOtherBankBranchEnableDisable(true);
                if (tblOtherBankBranch.getRowCount() == 0) {
                    // At the edit mode if all the rows are deleted
                    // enable New Button in Other Bank Branch
                    enableDisableOtherBankBranch_SaveDelete();
                }
            } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW ) {
                enableDisablePanOtherBank(false);
                enableDisablePanOtherBankBranch(false);
                setTableOtherBankBranchEnableDisable(false);
            } else {
                enableDisableBankCode(true);
            }
            if(viewType == AUTHORIZE ){
                btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
            }
            observable.setStatus();
            setButtonEnableDisable();
        }
    }
    /**
     * To enable disable Bank Code and Branch Code
     * At the time of edit mode these fields are disable
     * Since these are the primary keys for the tables OTHER_BANK and OTHER_BANK_BRANCH
     */
    private void enableDisableBankCode(boolean flag) {
        txtBankCode.setEditable(flag);
    }
    private void enableDisableBranchCode(boolean flag) {
        txtOtherBranchCode.setEditable(flag);
    }
    /**
     * Enable Disable OtherBankBranch Fields
     */
    public void enableDisablePanOtherBankBranch(boolean flag){
        ClientUtil.enableDisable(panOtherBankBranch,flag);
        
    }
    /**
     * Enable Disable OtherBank Fields
     */
    public void enableDisablePanOtherBank(boolean flag){
        ClientUtil.enableDisable(panOtherBank,flag);
        
    }
    /**
     * update the OtherBankBranch details
     */
    private void updateOtherBankBranch() {
        removeRadioButtons();
        cboCity.setSelectedItem(observable.getCboCity());
        cboState.setSelectedItem(observable.getCboState());
        cboCountry.setSelectedItem(observable.getCboCountry());
        txtAddress.setText(observable.getTxtAddress());
        txtMICR.setText(observable.getTxtMICR());
        txtAccountHeadValue.setText(observable.getTxtAccountHeadValue());
        txtOtherBranchCode.setText(observable.getTxtOtherBranchCode());
        txtOtherBranchShortName.setText(observable.getTxtOtherBranchShortName());
        txtPincode.setText(observable.getTxtPincode());
        txtBranchName.setText(observable.getTxtBranchName());
        txtPhoneNo.setText(observable.getPhoneNo());
        cboProdType.setSelectedItem(observable.getCboProductType());
        cboProdId.setSelectedItem(observable.getCboProdID());
        cRadio_HVC_Yes.setSelected(observable.isCRadio_HVC_Yes());
        cRadio_HVC_No.setSelected(observable.isCRadio_HVC_No());
        cRadio_DB_Yes.setSelected(observable.isCRadio_DB_Yes());
        cRadio_DB_No.setSelected(observable.isCRadio_DB_No());
        addRadioButtons();
    }
    
    /*To set model for combo boxes*/
    private void initComponentData() {
        cboCity.setModel(observable.getCbmCity());
        cboState.setModel(observable.getCbmState());
        cboCountry.setModel(observable.getCbmCountry());
        cboProdType.setModel(observable.getCbmProdType());
        cboProdId.setModel(observable.getCbmProdID());
    }
    
     /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        btnAuthorize.setName("btnAuthorize");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnNew.setName("btnNew");
        btnOtherBankDelete.setName("btnOtherBankDelete");
        btnOtherBankNew.setName("btnOtherBankNew");
        btnOtherBankSave.setName("btnOtherBankSave");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        cboCity.setName("cboCity");
        cboCountry.setName("cboCountry");
        cboState.setName("cboState");
        lblAddress.setName("lblAddress");
        lblBankCode.setName("lblBankCode");
        lblBankName.setName("lblBankName");
        lblBankShortName.setName("lblBankShortName");
        lblCity.setName("lblCity");
        lblCountry.setName("lblCountry");
        lblHVCClearing.setName("lblHVCClearing");
        lblMsg.setName("lblMsg");
        lblBranchName.setName("lblBranchName");
        lblOtherBranchCode.setName("lblOtherBranchCode");
        lblOtherBranchShortName.setName("lblOtherBranchShortName");
        lblProductType.setName("lblProductType");
        lblProdId.setName("lblProdId");
        lblPincode.setName("lblPincode");
        lblBankType.setName("lblBankType");
        lblPoneNo.setName("lblPhoneNo");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace5.setName("lblSpace5");
        lblState.setName("lblState");
        lblStatus.setName("lblStatus");
        mbrOtherBank.setName("mbrOtherBank");
        panButton.setName("panButton");
        panMain.setName("panMain");
        panOtherBank.setName("panOtherBank");
        panOtherBankBranch.setName("panOtherBankBranch");
        panOtherBankBranchTable.setName("panOtherBankBranchTable");
        panStatus.setName("panStatus");
        panDesgBranch.setName("panDesgBranch");
        panHVC.setName("panHVC");
        srpOtherBankBranch.setName("srpOtherBankBranch");
        tblOtherBankBranch.setName("tblOtherBankBranch");
        txtAddress.setName("txtAddress");
        txtMICR.setName("txtMICR");
        txtAccountHeadValue.setName("txtAccountHeadValue");
        txtBankCode.setName("txtBankCode");
        txtBankName.setName("txtBankName");
        txtBankShortName.setName("txtBankShortName");
        txtBranchName.setName("txtBranchName");
        txtOtherBranchCode.setName("txtOtherBranchCode");
        txtOtherBranchShortName.setName("txtOtherBranchShortName");
        txtPincode.setName("txtPincode");
        //        txtBankType.setName("txtBankType");
        txtPhoneNo.setName("txtPhoneNo");
        
    }
    
/* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        lblBankName.setText(resourceBundle.getString("lblBankName"));
        lblHVCClearing.setText(resourceBundle.getString("lblHVCClearing"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblCity.setText(resourceBundle.getString("lblCity"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblPincode.setText(resourceBundle.getString("lblPincode"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        btnOtherBankSave.setText(resourceBundle.getString("btnOtherBankSave"));
        lblOtherBranchShortName.setText(resourceBundle.getString("lblOtherBranchShortName"));
        lblProductType.setText(resourceBundle.getString("lblProductType"));
        lblProdId.setText(resourceBundle.getString("lblProdId"));
        btnOtherBankNew.setText(resourceBundle.getString("btnOtherBankNew"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        lblSpace5.setText(resourceBundle.getString("lblSpace5"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnOtherBankDelete.setText(resourceBundle.getString("btnOtherBankDelete"));
        lblBranchName.setText(resourceBundle.getString("lblBranchName"));
        lblOtherBranchCode.setText(resourceBundle.getString("lblOtherBranchCode"));
        lblCountry.setText(resourceBundle.getString("lblCountry"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        ((javax.swing.border.TitledBorder)panOtherBank.getBorder()).setTitle(resourceBundle.getString("panOtherBank"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblBankShortName.setText(resourceBundle.getString("lblBankShortName"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        lblBankCode.setText(resourceBundle.getString("lblBankCode"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblState.setText(resourceBundle.getString("lblState"));
        lblAddress.setText(resourceBundle.getString("lblAddress"));
        ((javax.swing.border.TitledBorder)panOtherBankBranch.getBorder()).setTitle(resourceBundle.getString("panOtherBankBranch"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
    }
    
    
/* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        OtherBankMRB objMandatoryRB = new OtherBankMRB();
        txtBranchName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBranchName"));
        txtOtherBranchCode.setHelpMessage(lblMsg, objMandatoryRB.getString("txtOtherBranchCode"));
        txtBankShortName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBankShortName"));
        txtBankName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBankName"));
        txtAddress.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAddress"));
        txtMICR.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMICR"));
        cboCountry.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCountry"));
        txtBankCode.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBankCode"));
        txtPincode.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPincode"));
        cboState.setHelpMessage(lblMsg, objMandatoryRB.getString("cboState"));
        txtOtherBranchShortName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtOtherBranchShortName"));
        cboCity.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCity"));
    }
    
    
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(java.util.Observable observed, Object arg) {
        removeRadioButtons();
        txtOtherBranchCode.setText(observable.getTxtOtherBranchCode());
        txtOtherBranchShortName.setText(observable.getTxtOtherBranchShortName());
        txtBranchName.setText(observable.getTxtBranchName());
        txtAddress.setText(observable.getTxtAddress());
        txtMICR.setText(observable.getTxtMICR());
        txtAccountHeadValue.setText(observable.getTxtAccountHeadValue());
        txtPincode.setText(observable.getTxtPincode());
        cboCountry.setSelectedItem(observable.getCboCountry());
        cboState.setSelectedItem(observable.getCboState());
        cboCity.setSelectedItem(observable.getCboCity());
        txtBankName.setText(observable.getTxtBankName());
        txtBankCode.setText(observable.getTxtBankCode());
        txtBankShortName.setText(observable.getTxtBankShortName());
        tblOtherBankBranch.setModel(observable.getTblOtherBankBranch());
        txtPhoneNo.setText(observable.getPhoneNo());
        cboProdType.setSelectedItem(observable.getCboProductType());
        cboProdId.setSelectedItem(observable.getCboProdID());
        cRadio_HVC_No.setSelected(observable.isCRadio_HVC_No());
        cRadio_HVC_Yes.setSelected(observable.isCRadio_HVC_Yes());
        cRadio_DB_Yes.setSelected(observable.isCRadio_DB_Yes());
        cRadio_DB_No.setSelected(observable.isCRadio_DB_No());
        lblStatus.setText(observable.getLblStatus());
        addRadioButtons();
        
    }
    // To Reset the Radio Buttons in the UI after any operation, We've to
    //1. Remove the Radio Buttons fron the Radio Groups...
    //2. Add the Radio Buttons Back in The Radio Groups...
    //a.) To Remove the Radio buttons...
    private void removeRadioButtons() {
        cButtonGroup1.remove(cRadio_HVC_Yes);
        cButtonGroup1.remove(cRadio_HVC_No);
        cButtonGroup2.remove(cRadio_DB_Yes);
        cButtonGroup2.remove(cRadio_DB_No);
    }
    
    // b.) To Add the Radio buttons...
    private void addRadioButtons() {
        cButtonGroup1 = new CButtonGroup();
        cButtonGroup1.add(cRadio_HVC_Yes);
        cButtonGroup1.add(cRadio_HVC_No);
        cButtonGroup2.add(cRadio_DB_Yes);
        cButtonGroup2.add(cRadio_DB_No);
    }
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setTxtOtherBranchCode(txtOtherBranchCode.getText());
        observable.setTxtOtherBranchShortName(txtOtherBranchShortName.getText());
        observable.setTxtBranchName(txtBranchName.getText());
        observable.setTxtAddress(txtAddress.getText());
        observable.setTxtMICR(txtMICR.getText());
        observable.setTxtAccountHeadValue(txtAccountHeadValue.getText());
        observable.setTxtPincode(txtPincode.getText());
        observable.setCboCountry((String) cboCountry.getSelectedItem());
        observable.setCboState((String) cboState.getSelectedItem());
        observable.setCboCity((String) cboCity.getSelectedItem());
        observable.setTxtBankName(txtBankName.getText());
        observable.setTxtBankCode(txtBankCode.getText());
        observable.setTxtBankShortName(txtBankShortName.getText());
        observable.setCboProductType((String) cboProdType.getSelectedItem());
        observable.setCboProdID((String) cboProdType.getSelectedItem());
        observable.setPhoneNo(txtPhoneNo.getText());
        observable.setCRadio_HVC_No(cRadio_HVC_No.isSelected());
        observable.setCRadio_HVC_Yes(cRadio_HVC_Yes.isSelected());
        observable.setCRadio_DB_No(cRadio_DB_No.isSelected());
        observable.setCRadio_DB_Yes(cRadio_DB_Yes.isSelected());
        
    }
    
    
/* Auto Generated Method - setMandatoryHashMap()
 
ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
 
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtOtherBranchCode", new Boolean(true));
        mandatoryMap.put("txtOtherBranchShortName", new Boolean(true));
        mandatoryMap.put("txtBranchName", new Boolean(true));
        mandatoryMap.put("txtAddress", new Boolean(true));
        mandatoryMap.put("txtPincode", new Boolean(true));
        mandatoryMap.put("cboCountry", new Boolean(true));
        mandatoryMap.put("cboState", new Boolean(true));
        mandatoryMap.put("cboCity", new Boolean(true));
        mandatoryMap.put("txtBankName", new Boolean(true));
        mandatoryMap.put("txtBankCode", new Boolean(true));
        mandatoryMap.put("txtBankShortName", new Boolean(true));
    }
    
/* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    
    
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        cButtonGroup1 = new com.see.truetransact.uicomponent.CButtonGroup();
        cButtonGroup2 = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrOtherBank = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
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
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace31 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panMain = new com.see.truetransact.uicomponent.CPanel();
        panOtherBankBranchTable = new com.see.truetransact.uicomponent.CPanel();
        srpOtherBankBranch = new com.see.truetransact.uicomponent.CScrollPane();
        tblOtherBankBranch = new com.see.truetransact.uicomponent.CTable();
        panOtherBankBranch = new com.see.truetransact.uicomponent.CPanel();
        lblOtherBranchCode = new com.see.truetransact.uicomponent.CLabel();
        lblOtherBranchShortName = new com.see.truetransact.uicomponent.CLabel();
        panButton = new com.see.truetransact.uicomponent.CPanel();
        btnOtherBankSave = new com.see.truetransact.uicomponent.CButton();
        btnOtherBankNew = new com.see.truetransact.uicomponent.CButton();
        btnOtherBankDelete = new com.see.truetransact.uicomponent.CButton();
        txtOtherBranchCode = new com.see.truetransact.uicomponent.CTextField();
        txtOtherBranchShortName = new com.see.truetransact.uicomponent.CTextField();
        lblBranchName = new com.see.truetransact.uicomponent.CLabel();
        lblAddress = new com.see.truetransact.uicomponent.CLabel();
        lblCity = new com.see.truetransact.uicomponent.CLabel();
        lblState = new com.see.truetransact.uicomponent.CLabel();
        lblBankType = new com.see.truetransact.uicomponent.CLabel();
        txtBranchName = new com.see.truetransact.uicomponent.CTextField();
        txtAddress = new com.see.truetransact.uicomponent.CTextField();
        lblCountry = new com.see.truetransact.uicomponent.CLabel();
        cboCountry = new com.see.truetransact.uicomponent.CComboBox();
        cboState = new com.see.truetransact.uicomponent.CComboBox();
        cboCity = new com.see.truetransact.uicomponent.CComboBox();
        txtPincode = new com.see.truetransact.uicomponent.CTextField();
        lblPincode = new com.see.truetransact.uicomponent.CLabel();
        txtPhoneNo = new com.see.truetransact.uicomponent.CTextField();
        lblPoneNo = new com.see.truetransact.uicomponent.CLabel();
        panHVC = new com.see.truetransact.uicomponent.CPanel();
        cRadio_HVC_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        cRadio_HVC_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblHVCClearing = new com.see.truetransact.uicomponent.CLabel();
        panDesgBranch = new com.see.truetransact.uicomponent.CPanel();
        cRadio_DB_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        cRadio_DB_No = new com.see.truetransact.uicomponent.CRadioButton();
        txtMICR = new com.see.truetransact.uicomponent.CTextField();
        lblMICR = new com.see.truetransact.uicomponent.CLabel();
        lblAccountHead = new com.see.truetransact.uicomponent.CLabel();
        panAccountHeadValue = new com.see.truetransact.uicomponent.CPanel();
        txtAccountHeadValue = new com.see.truetransact.uicomponent.CTextField();
        btnAccountHead = new com.see.truetransact.uicomponent.CButton();
        lblProductType = new com.see.truetransact.uicomponent.CLabel();
        cboProdType = new com.see.truetransact.uicomponent.CComboBox();
        lblProdId = new com.see.truetransact.uicomponent.CLabel();
        cboProdId = new com.see.truetransact.uicomponent.CComboBox();
        panOtherBank = new com.see.truetransact.uicomponent.CPanel();
        lblBankShortName = new com.see.truetransact.uicomponent.CLabel();
        lblBankName = new com.see.truetransact.uicomponent.CLabel();
        txtBankName = new com.see.truetransact.uicomponent.CTextField();
        lblBankCode = new com.see.truetransact.uicomponent.CLabel();
        txtBankCode = new com.see.truetransact.uicomponent.CTextField();
        txtBankShortName = new com.see.truetransact.uicomponent.CTextField();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        mbrOtherBank = new com.see.truetransact.uicomponent.CMenuBar();
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
        setMinimumSize(new java.awt.Dimension(620, 665));
        setPreferredSize(new java.awt.Dimension(620, 665));

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
        tbrOtherBank.add(btnView);

        lblSpace4.setText("     ");
        tbrOtherBank.add(lblSpace4);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrOtherBank.add(btnNew);

        lblSpace26.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace26.setText("     ");
        lblSpace26.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOtherBank.add(lblSpace26);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrOtherBank.add(btnEdit);

        lblSpace27.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace27.setText("     ");
        lblSpace27.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOtherBank.add(lblSpace27);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrOtherBank.add(btnDelete);

        lblSpace2.setText("     ");
        tbrOtherBank.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrOtherBank.add(btnSave);

        lblSpace28.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace28.setText("     ");
        lblSpace28.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOtherBank.add(lblSpace28);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrOtherBank.add(btnCancel);

        lblSpace3.setText("     ");
        tbrOtherBank.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrOtherBank.add(btnAuthorize);

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOtherBank.add(lblSpace29);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrOtherBank.add(btnException);

        lblSpace30.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace30.setText("     ");
        lblSpace30.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOtherBank.add(lblSpace30);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.setMaximumSize(new java.awt.Dimension(29, 27));
        btnReject.setMinimumSize(new java.awt.Dimension(29, 27));
        btnReject.setPreferredSize(new java.awt.Dimension(29, 27));
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrOtherBank.add(btnReject);

        lblSpace5.setText("     ");
        tbrOtherBank.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrOtherBank.add(btnPrint);

        lblSpace31.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace31.setText("     ");
        lblSpace31.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOtherBank.add(lblSpace31);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrOtherBank.add(btnClose);

        getContentPane().add(tbrOtherBank, java.awt.BorderLayout.NORTH);

        panMain.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panMain.setMinimumSize(new java.awt.Dimension(600, 625));
        panMain.setPreferredSize(new java.awt.Dimension(600, 625));
        panMain.setLayout(new java.awt.GridBagLayout());

        panOtherBankBranchTable.setMinimumSize(new java.awt.Dimension(310, 468));
        panOtherBankBranchTable.setPreferredSize(new java.awt.Dimension(310, 468));
        panOtherBankBranchTable.setLayout(new java.awt.GridBagLayout());

        srpOtherBankBranch.setMinimumSize(new java.awt.Dimension(250, 460));
        srpOtherBankBranch.setPreferredSize(new java.awt.Dimension(250, 460));

        tblOtherBankBranch.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblOtherBankBranch.setMinimumSize(new java.awt.Dimension(235, 400));
        tblOtherBankBranch.setPreferredScrollableViewportSize(new java.awt.Dimension(235, 400));
        tblOtherBankBranch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblOtherBankBranchMousePressed(evt);
            }
        });
        srpOtherBankBranch.setViewportView(tblOtherBankBranch);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOtherBankBranchTable.add(srpOtherBankBranch, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMain.add(panOtherBankBranchTable, gridBagConstraints);

        panOtherBankBranch.setBorder(javax.swing.BorderFactory.createTitledBorder("Other Bank Branch"));
        panOtherBankBranch.setMinimumSize(new java.awt.Dimension(250, 467));
        panOtherBankBranch.setPreferredSize(new java.awt.Dimension(250, 467));
        panOtherBankBranch.setLayout(new java.awt.GridBagLayout());

        lblOtherBranchCode.setText("Branch Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 2, 4);
        panOtherBankBranch.add(lblOtherBranchCode, gridBagConstraints);

        lblOtherBranchShortName.setText("Branch Short Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panOtherBankBranch.add(lblOtherBranchShortName, gridBagConstraints);

        panButton.setLayout(new java.awt.GridBagLayout());

        btnOtherBankSave.setText("Save");
        btnOtherBankSave.setEnabled(false);
        btnOtherBankSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOtherBankSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton.add(btnOtherBankSave, gridBagConstraints);

        btnOtherBankNew.setText("New");
        btnOtherBankNew.setEnabled(false);
        btnOtherBankNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOtherBankNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton.add(btnOtherBankNew, gridBagConstraints);

        btnOtherBankDelete.setText("Delete");
        btnOtherBankDelete.setEnabled(false);
        btnOtherBankDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOtherBankDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton.add(btnOtherBankDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.insets = new java.awt.Insets(8, 16, 4, 0);
        panOtherBankBranch.add(panButton, gridBagConstraints);

        txtOtherBranchCode.setMaxLength(4);
        txtOtherBranchCode.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 2, 4);
        panOtherBankBranch.add(txtOtherBranchCode, gridBagConstraints);

        txtOtherBranchShortName.setMaxLength(128);
        txtOtherBranchShortName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panOtherBankBranch.add(txtOtherBranchShortName, gridBagConstraints);

        lblBranchName.setText("Branch Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panOtherBankBranch.add(lblBranchName, gridBagConstraints);

        lblAddress.setText("Address");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panOtherBankBranch.add(lblAddress, gridBagConstraints);

        lblCity.setText("City");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panOtherBankBranch.add(lblCity, gridBagConstraints);

        lblState.setText("State");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panOtherBankBranch.add(lblState, gridBagConstraints);

        lblBankType.setText("Designated Branch");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panOtherBankBranch.add(lblBankType, gridBagConstraints);

        txtBranchName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panOtherBankBranch.add(txtBranchName, gridBagConstraints);

        txtAddress.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panOtherBankBranch.add(txtAddress, gridBagConstraints);

        lblCountry.setText("Country");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panOtherBankBranch.add(lblCountry, gridBagConstraints);

        cboCountry.setMaximumSize(new java.awt.Dimension(100, 21));
        cboCountry.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panOtherBankBranch.add(cboCountry, gridBagConstraints);

        cboState.setMaximumSize(new java.awt.Dimension(100, 21));
        cboState.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panOtherBankBranch.add(cboState, gridBagConstraints);

        cboCity.setMaximumSize(new java.awt.Dimension(100, 21));
        cboCity.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panOtherBankBranch.add(cboCity, gridBagConstraints);

        txtPincode.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panOtherBankBranch.add(txtPincode, gridBagConstraints);

        lblPincode.setText("Pincode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panOtherBankBranch.add(lblPincode, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panOtherBankBranch.add(txtPhoneNo, gridBagConstraints);

        lblPoneNo.setText("PhoneNo");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panOtherBankBranch.add(lblPoneNo, gridBagConstraints);

        panHVC.setLayout(new java.awt.GridBagLayout());

        cButtonGroup1.add(cRadio_HVC_Yes);
        cRadio_HVC_Yes.setText("Yes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        panHVC.add(cRadio_HVC_Yes, gridBagConstraints);

        cButtonGroup1.add(cRadio_HVC_No);
        cRadio_HVC_No.setText("NO");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 0, 4);
        panHVC.add(cRadio_HVC_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        panOtherBankBranch.add(panHVC, gridBagConstraints);

        lblHVCClearing.setText("HighValueClearing");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panOtherBankBranch.add(lblHVCClearing, gridBagConstraints);

        panDesgBranch.setLayout(new java.awt.GridBagLayout());

        cButtonGroup2.add(cRadio_DB_Yes);
        cRadio_DB_Yes.setText("Yes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        panDesgBranch.add(cRadio_DB_Yes, gridBagConstraints);

        cButtonGroup2.add(cRadio_DB_No);
        cRadio_DB_No.setText("NO");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 0, 4);
        panDesgBranch.add(cRadio_DB_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        panOtherBankBranch.add(panDesgBranch, gridBagConstraints);

        txtMICR.setAllowNumber(true);
        txtMICR.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panOtherBankBranch.add(txtMICR, gridBagConstraints);

        lblMICR.setText("MICR");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panOtherBankBranch.add(lblMICR, gridBagConstraints);

        lblAccountHead.setText("SB/CA GL A/c Head");
        lblAccountHead.setName("lblAccountHead"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panOtherBankBranch.add(lblAccountHead, gridBagConstraints);

        panAccountHeadValue.setLayout(new java.awt.GridBagLayout());

        txtAccountHeadValue.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccountHeadValue.add(txtAccountHeadValue, gridBagConstraints);

        btnAccountHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccountHead.setMinimumSize(new java.awt.Dimension(25, 25));
        btnAccountHead.setPreferredSize(new java.awt.Dimension(25, 25));
        btnAccountHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccountHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccountHeadValue.add(btnAccountHead, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panOtherBankBranch.add(panAccountHeadValue, gridBagConstraints);

        lblProductType.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panOtherBankBranch.add(lblProductType, gridBagConstraints);

        cboProdType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "----Select----" }));
        cboProdType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdType.setPopupWidth(125);
        cboProdType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panOtherBankBranch.add(cboProdType, gridBagConstraints);

        lblProdId.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panOtherBankBranch.add(lblProdId, gridBagConstraints);

        cboProdId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdId.setPopupWidth(200);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panOtherBankBranch.add(cboProdId, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 4, 4);
        panMain.add(panOtherBankBranch, gridBagConstraints);

        panOtherBank.setBorder(javax.swing.BorderFactory.createTitledBorder("Other Bank"));
        panOtherBank.setMinimumSize(new java.awt.Dimension(250, 110));
        panOtherBank.setPreferredSize(new java.awt.Dimension(250, 110));
        panOtherBank.setLayout(new java.awt.GridBagLayout());

        lblBankShortName.setText("Bank Short Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 4, 4);
        panOtherBank.add(lblBankShortName, gridBagConstraints);

        lblBankName.setText("Bank Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 4, 4);
        panOtherBank.add(lblBankName, gridBagConstraints);

        txtBankName.setMaxLength(128);
        txtBankName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 4, 4);
        panOtherBank.add(txtBankName, gridBagConstraints);

        lblBankCode.setText("Bank Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panOtherBank.add(lblBankCode, gridBagConstraints);

        txtBankCode.setMinimumSize(new java.awt.Dimension(100, 21));
        txtBankCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBankCodeFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panOtherBank.add(txtBankCode, gridBagConstraints);

        txtBankShortName.setMinimumSize(new java.awt.Dimension(100, 21));
        txtBankShortName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBankShortNameFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 4, 4);
        panOtherBank.add(txtBankShortName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panMain.add(panOtherBank, gridBagConstraints);

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

        mbrOtherBank.add(mnuProcess);

        setJMenuBar(mbrOtherBank);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cboProdTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdTypeActionPerformed
        // TODO add your handling code here:
                // TODO add your handling code here:
        if (cboProdType.getSelectedIndex() > 0) {
            String prodType = ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString();
            observable.setCbmProdType(prodType);
            if(prodType.equals("GL")){
                cboProdId.setSelectedItem("");
                cboProdId.setEnabled(false);
                lblAccountHead.setText("SB/CA GL A/c Head");
            }else{
                cboProdId.setEnabled(true);
                lblAccountHead.setText("Account No");
            }
            cboProdId.setModel(observable.getCbmProdID());
        }
    }//GEN-LAST:event_cboProdTypeActionPerformed

    private void btnAccountHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccountHeadActionPerformed
        // TODO add your handling code here:
        callView(ACCOUNT_HEAD);
        txtAccountHeadValue.setEnabled(false);
    }//GEN-LAST:event_btnAccountHeadActionPerformed
    private void callView(int viewType){
        HashMap viewMap = new HashMap();
        HashMap where = new HashMap();
        this.viewType = viewType;
        System.out.println("viewTypeviewType"+viewType);
        if(viewType == ACCOUNT_HEAD){
            
            String prodType = ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString();
            System.out.println("prodTypeprodType"+prodType);
            System.out.println("((ComboBoxModel) cboProdId.getModel()).getKeyForSelected()"+((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
            
            if(!prodType.equals("GL")){
                System.out.println("((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString()"+((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString());
                viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList"
                + ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString());
                where.put("PROD_ID", ((ComboBoxModel) cboProdId.getModel()).getKeyForSelected());
            }else{
                viewMap.put(CommonConstants.MAP_NAME,"MDS.getSelectAcctHeadTOList");
            }
            where.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, where);
        }
        new ViewAll(this,viewMap).show();
    }
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        viewType = VIEW;
        popUp();
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed
    private boolean checkBankCode()  {
        boolean exist = false;
        if (observable.isBankCodeAlreadyExist(txtBankCode.getText())) {
            exist = true;
            displayAlert(resourceBundle.getString("BankCodeCount"));
        } else {
            exist = false;
        }
        return exist;
    }
    private void txtBankShortNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBankShortNameFocusLost
        // TODO add your handling code here:
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            if (observable.enableNew(txtBankCode.getText(),txtBankShortName.getText())) {
                enableDisableOtherBankBranch_SaveDelete();
                enableDisablePanOtherBankBranch(false);
                observable.resetOtherBankBranch();
                updateOtherBankBranch();
            } else {
                enableDisableOtherBankBranch_NewSaveDelete(false);
            }
        }
    }//GEN-LAST:event_txtBankShortNameFocusLost
    
    private void txtBankCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBankCodeFocusLost
        // TODO add your handling code here:
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            bankCodeExist = checkBankCode();
            if (observable.enableNew(txtBankCode.getText(),txtBankShortName.getText())) {
                enableDisableOtherBankBranch_SaveDelete();
                enableDisablePanOtherBankBranch(false);
                observable.resetOtherBankBranch();
                updateOtherBankBranch();
            } else {
                enableDisableOtherBankBranch_NewSaveDelete(false);
            }
        }
    }//GEN-LAST:event_txtBankCodeFocusLost
    
    private void tblOtherBankBranchMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblOtherBankBranchMousePressed
        // TODO add your handling code here:
        if (tblOtherBankBranch.getSelectedRow() >= 0 && observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE) {
            tblOtherBankBranchPress = true;// table row is clicked
            setTableOtherBankBranchEnableDisable(true);
            enableDisableOtherBankBranch_NewSaveDelete(true);// enable New Save Delete buttons
            enableDisablePanOtherBankBranch(true);
            String prodType = CommonUtil.convertObjToStr(tblOtherBankBranch.getValueAt(tblOtherBankBranch.getSelectedRow(),1));
            observable.setCbmProdType(prodType);
            cboProdId.setModel(observable.getCbmProdID());
            if(observable.populateOtherBankBranch(tblOtherBankBranch.getSelectedRow())) {
                enableDisableBranchCode(true);
            } else {
                enableDisableBranchCode(false);
            }
            updateOtherBankBranch();
            btnAccountHead.setEnabled(true);
            txtAccountHeadValue.setEnabled(false);
        }
        if( observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
            setTableOtherBankBranchEnableDisable(true);
            enableDisableOtherBankBranch_NewSaveDelete(false);// enable New Save Delete buttons
            enableDisablePanOtherBankBranch(true);
            ClientUtil.enableDisable(panOtherBankBranch,false);
            btnAccountHead.setEnabled(false);
        }
    }//GEN-LAST:event_tblOtherBankBranchMousePressed
    
    private void btnOtherBankDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOtherBankDeleteActionPerformed
        // TODO add your handling code here:
        if (tblOtherBankBranch.getSelectedRow() >= 0) {
            observable.deleteOtherBankBranch(tblOtherBankBranch.getSelectedRow());
            enableDisableOtherBankBranch_SaveDelete();
            tblOtherBankBranchPress = false;
            observable.resetOtherBankBranch();
            enableDisablePanOtherBankBranch(false);
            updateOtherBankBranch();
            btnAccountHead.setEnabled(false);
        }
    }//GEN-LAST:event_btnOtherBankDeleteActionPerformed
    
    private void btnOtherBankSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOtherBankSaveActionPerformed
        // TODO add your handling code here:
        final String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panOtherBankBranch);
        if (mandatoryMessage.length() > 0) {
            displayAlert(mandatoryMessage);
       } else {
            updateOBFields();
            observable.saveOtherBankBranch(tblOtherBankBranchPress,tblOtherBankBranch.getSelectedRow());
            enableDisablePanOtherBankBranch(false);
            enableDisableOtherBankBranch_SaveDelete();
            observable.resetOtherBankBranch();
            updateOtherBankBranch();
            btnAccountHead.setEnabled(false);
        }
        
    }//GEN-LAST:event_btnOtherBankSaveActionPerformed
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
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }
    private void btnOtherBankNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOtherBankNewActionPerformed
        // TODO add your handling code here:
        observable.resetOtherBankBranch();
        updateOtherBankBranch();
        tblOtherBankBranchPress = false;
        enableDisableOtherBankBranch_NewSaveDelete(true);
        enableDisablePanOtherBankBranch(true);
        enableDisableBranchCode(true);
        btnAccountHead.setEnabled(true);
        txtAccountHeadValue.setEnabled(false);
    }//GEN-LAST:event_btnOtherBankNewActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
       cifClosingAlert();
//        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPrintActionPerformed
    
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
            mapParam.put(CommonConstants.MAP_NAME, "viewAuthorizeOtherBank");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeOtherBank");
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
            isFilled = true;
        } else if (viewType == AUTHORIZE && isFilled){
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("USER_ID", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("BANK_CODE", txtBankCode.getText());
            singleAuthorizeMap.put("CURR_DATE", ClientUtil.getCurrentDate());
            ClientUtil.execute("authorizeOtherBank", singleAuthorizeMap);
            viewType = -1;
            btnCancelActionPerformed(null);
        }
        observable.resetStatus();
        lblStatus.setText(observable.getLblStatus());
    }
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        setModified(false);
        super.removeEditLock(txtBankCode.getText());
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        ClientUtil.enableDisable(this, false);
        setButtonEnableDisable();
        enableDisableOtherBankBranch_NewSaveDelete(false);
        viewType = -1;
        btnAccountHead.setEnabled(false);
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        observable.resetForm();
        observable.setStatus();
        
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        setModified(false);
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panOtherBank);
        if (bankCodeExist) {
            mandatoryMessage += resourceBundle.getString("BankCodeCount");
        }
        if (mandatoryMessage.length() > 0){
            displayAlert(mandatoryMessage);
        }
        else {
            savePerformed();
        }
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
    }//GEN-LAST:event_btnSaveActionPerformed
    /* action to perform when  main save button is pressed */
    private void savePerformed(){
        updateOBFields();
        observable.doAction();
        ClientUtil.enableDisable(this, false);
        enableDisableOtherBankBranch_NewSaveDelete(false);
        setButtonEnableDisable();
        super.removeEditLock(txtBankCode.getText());
        observable.resetForm();
        observable.setResultStatus();
        
        
    }
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        viewType = DELETE;
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        popUp();
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        viewType = EDIT;
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        popUp();
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed
    /**
     * To enable disable table Issue details
     */
    private void setTableOtherBankBranchEnableDisable(boolean flag){
        tblOtherBankBranch.setEnabled(flag);
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
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        setModified(true);
        viewType = NEW;
        setTableOtherBankBranchEnableDisable(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        enableDisablePanOtherBank(true);
        enableDisableBankCode(true);
        setButtonEnableDisable();
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        observable.setStatus();
        observable.resetForm();
        
    }//GEN-LAST:event_btnNewActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAccountHead;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnOtherBankDelete;
    private com.see.truetransact.uicomponent.CButton btnOtherBankNew;
    private com.see.truetransact.uicomponent.CButton btnOtherBankSave;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CButtonGroup cButtonGroup1;
    private com.see.truetransact.uicomponent.CButtonGroup cButtonGroup2;
    private com.see.truetransact.uicomponent.CRadioButton cRadio_DB_No;
    private com.see.truetransact.uicomponent.CRadioButton cRadio_DB_Yes;
    private com.see.truetransact.uicomponent.CRadioButton cRadio_HVC_No;
    private com.see.truetransact.uicomponent.CRadioButton cRadio_HVC_Yes;
    private com.see.truetransact.uicomponent.CComboBox cboCity;
    private com.see.truetransact.uicomponent.CComboBox cboCountry;
    private com.see.truetransact.uicomponent.CComboBox cboProdId;
    private com.see.truetransact.uicomponent.CComboBox cboProdType;
    private com.see.truetransact.uicomponent.CComboBox cboState;
    private com.see.truetransact.uicomponent.CLabel lblAccountHead;
    private com.see.truetransact.uicomponent.CLabel lblAddress;
    private com.see.truetransact.uicomponent.CLabel lblBankCode;
    private com.see.truetransact.uicomponent.CLabel lblBankName;
    private com.see.truetransact.uicomponent.CLabel lblBankShortName;
    private com.see.truetransact.uicomponent.CLabel lblBankType;
    private com.see.truetransact.uicomponent.CLabel lblBranchName;
    private com.see.truetransact.uicomponent.CLabel lblCity;
    private com.see.truetransact.uicomponent.CLabel lblCountry;
    private com.see.truetransact.uicomponent.CLabel lblHVCClearing;
    private com.see.truetransact.uicomponent.CLabel lblMICR;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblOtherBranchCode;
    private com.see.truetransact.uicomponent.CLabel lblOtherBranchShortName;
    private com.see.truetransact.uicomponent.CLabel lblPincode;
    private com.see.truetransact.uicomponent.CLabel lblPoneNo;
    private com.see.truetransact.uicomponent.CLabel lblProdId;
    private com.see.truetransact.uicomponent.CLabel lblProductType;
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
    private com.see.truetransact.uicomponent.CLabel lblState;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CMenuBar mbrOtherBank;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAccountHeadValue;
    private com.see.truetransact.uicomponent.CPanel panButton;
    private com.see.truetransact.uicomponent.CPanel panDesgBranch;
    private com.see.truetransact.uicomponent.CPanel panHVC;
    private com.see.truetransact.uicomponent.CPanel panMain;
    private com.see.truetransact.uicomponent.CPanel panOtherBank;
    private com.see.truetransact.uicomponent.CPanel panOtherBankBranch;
    private com.see.truetransact.uicomponent.CPanel panOtherBankBranchTable;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private com.see.truetransact.uicomponent.CScrollPane srpOtherBankBranch;
    private com.see.truetransact.uicomponent.CTable tblOtherBankBranch;
    private javax.swing.JToolBar tbrOtherBank;
    private com.see.truetransact.uicomponent.CTextField txtAccountHeadValue;
    private com.see.truetransact.uicomponent.CTextField txtAddress;
    private com.see.truetransact.uicomponent.CTextField txtBankCode;
    private com.see.truetransact.uicomponent.CTextField txtBankName;
    private com.see.truetransact.uicomponent.CTextField txtBankShortName;
    private com.see.truetransact.uicomponent.CTextField txtBranchName;
    private com.see.truetransact.uicomponent.CTextField txtMICR;
    private com.see.truetransact.uicomponent.CTextField txtOtherBranchCode;
    private com.see.truetransact.uicomponent.CTextField txtOtherBranchShortName;
    private com.see.truetransact.uicomponent.CTextField txtPhoneNo;
    private com.see.truetransact.uicomponent.CTextField txtPincode;
    // End of variables declaration//GEN-END:variables
    
}
