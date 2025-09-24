/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AgriCardUI.java
 *
 * Created on December 30, 2004, 12:26 PM
 */

package com.see.truetransact.ui.product.loan.agricultureCard;

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
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.TrueTransactMain;
import org.apache.log4j.Logger;
import java.util.Date;
public class AgriCardUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer, UIMandatoryField  {
    // Variable Declarations
    AgriCardOB observable;
    final AgriCardRB  resourceBundle = new AgriCardRB();
    //        java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.product.loan.agricultureCard.NewLoanProductRB", ProxyParameters.LANGUAGE);
    private boolean tblAgriCardPress = false;
    private HashMap mandatoryMap;
    private int viewType = -1;
    private final int NEW=0, EDIT=1,DELETE=2,AUTHORIZE=3, VIEW =4;
    boolean isFilled = false;
    boolean bankCodeExist = false;
    private Date currDt = null;
    private final static Logger log = Logger.getLogger(AgriCardUI.class);
    /** Creates new form OtherBankUI */
    public AgriCardUI() {
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
        //        observable.resetForm();
        //        observable.resetStatus();
        currDt = ClientUtil.getCurrentDate();
    }
    /* To set Maximum length and for validation */
    private void setMaximumLength() {
        //        txtBankName.setMaxLength(128);
        //        txtBankCode.setMaxLength(16);
        //        txtOtherBranchCode.setMaxLength(32);
        //        txtOtherBranchShortName.setMaxLength(32);
        //        txtBranchName.setMaxLength(128);
        //        txtAddress.setMaxLength(64);
        //        txtPincode.setMaxLength(8);
        //        txtPincode.setValidation(new PincodeValidation_IN());
        txtNoOfYears.setValidation(new NumericValidation(ClientConstants.INSTRUMENT_NO2, 0));
        
        //        txtBankCode.setValidation(new NumericValidation(ClientConstants.INSTRUMENT_NO2, 0));
        //        txtBankCode.setAllowNumber(true);
        //        txtBankCode.setAllowAll(false);
        //        txtOtherBranchCode.setAllowAll(false);
        //        txtOtherBranchCode.setAllowNumber(true);
        //        txtPhoneNo.setValidation(new NumericValidation(ClientConstants.INSTRUMENT_NO2,0));
        //         txtPhoneNo.setAllowNumber(true);
        //        txtPhoneNo.setAllowAll(false);
    }
    
    private void setObservable() {
        /* Implementing Singleton pattern */
        //        try{
        //         observable = new NewLoanProductCardOB();
        //        }catch(Exception e){
        //            System.out.println("exception"+e);
        //        }
        observable = AgriCardOB.getInstance();
        observable.addObserver(this);
    }
    /**
     * EnableDisable New Save Delete Buttons of OtherBankBranch Table
     * When New is pressed
     */
    private void enableDisableOtherBankBranch_NewSaveDelete(boolean flag) {
        btnAgriNew.setEnabled(flag);
        btnAgriSave.setEnabled(flag);
        btnAgriDelete.setEnabled(flag);
    }
    /**
     * Enable Disable New Save Delete Buttons of OtherBankBranch Table
     * When Save or Delete is invoked
     */
    private void enableDisableOtherBankBranch_SaveDelete() {
        btnAgriNew.setEnabled(true);
        btnAgriSave.setEnabled(false);
        btnAgriDelete.setEnabled(false);
    }
    /** To display a popUp window for viewing existing data */
    private void popUp() {
        final HashMap viewMap = new HashMap();
        if ( observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ||  observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW){
            //            ArrayList lst = new ArrayList();
            //            lst.add("BANK_CODE");
            //            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            //            lst = null;
            viewMap.put(CommonConstants.MAP_NAME, "getSelectCardDetails");
        }
        new ViewAll(this, viewMap).show();
    }
    // Called Automatically when viewAll() is Called...
    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
        //        hash.put("WHERE", (String)hash.get("BANK_CODE"));
        observable.populateData(hash);
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ) {
            enableDisablePanOtherBank(false);
            enableDisablepanButton(false);
            setTableOtherBankBranchEnableDisable(true);
            //            if (tblAgriCard.getRowCount() == 0) {
            // At the edit mode if all the rows are deleted
            // enable New Button in Other Bank Branch
            enableDisableOtherBankBranch_SaveDelete();
            
            //            }
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT || observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE||observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW ) {
            enableDisablePanOtherBank(true);
            enableDisablePanOtherBankBranch(false);
            setTableOtherBankBranchEnableDisable(false);
            enableDisablepanButton(false);
        } else {
            enableDisablepanButton(true);
        }
        if(viewType == AUTHORIZE ){
            btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
            btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
            btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
        }
        observable.setStatus();
        setButtonEnableDisable();
    }
    /**
     * To enable disable Bank Code and Branch Code
     * At the time of edit mode these fields are disable
     * Since these are the primary keys for the tables OTHER_BANK and OTHER_BANK_BRANCH
     */
    private void enableDisablepanButton(boolean flag) {
        btnAgriNew.setEnabled(flag);
        btnAgriSave.setEnabled(flag);
        btnAgriDelete.setEnabled(flag);
    }
    private void enableDisableBranchCode(boolean flag) {
        //        txtOtherBranchCode.setEditable(flag);
    }
    /**
     * Enable Disable OtherBankBranch Fields
     */
    public void enableDisablePanOtherBankBranch(boolean flag){
        ClientUtil.enableDisable(panAgriCard,flag);
        
    }
    /**
     * Enable Disable OtherBank Fields
     */
    public void enableDisablePanOtherBank(boolean flag){
        ClientUtil.enableDisable(panAgriCard,flag);
        
    }
    /**
     * update the OtherBankBranch details
     */
    private void updateOtherBankBranch() {
        removeRadioButtons();
        cboAgriCardType.setSelectedItem(observable.getCboAgriCardType());
        cboProdId.setSelectedItem(observable.getCboProdId());
        cboProdType.setSelectedItem(observable.getCboProdType());
        txtNoOfYears.setText(observable.getTxtNoOfYears());
        //        txtOtherBranchCode.setText(observable.getTxtOtherBranchCode());
        //        txtOtherBranchShortName.setText(observable.getTxtOtherBranchShortName());
        //        txtPincode.setText(observable.getTxtPincode());
        //        txtBranchName.setText(observable.getTxtBranchName());
        //        txtPhoneNo.setText(observable.getPhoneNo());
        //        txtBankType.setText(observable.getTxtBankType());
        
        //        cRadio_SB_Yes.setSelected(observable.isCRadio_SB_Yes());
        //        cRadio_SB_No.setSelected(observable.isCRadio_SB_No());
        addRadioButtons();
    }
    
    /*To set model for combo boxes*/
    private void initComponentData() {
        cboAgriCardType.setModel(observable.getCbmAgriCardType());
        //        cboProdId.setModel(observable.getCbmProdType());//observable.getCbmProdId()
        cboProdType.setModel(observable.getCbmProdType());
        cboAgriCardValidity.setModel(observable.getCbmAgriCardValidity());
        tblAgriCard.setModel(observable.getTblAgriCard());
        
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
        btnAgriDelete.setName("btnOtherBankDelete");
        btnAgriNew.setName("btnOtherBankNew");
        btnAgriSave.setName("btnOtherBankSave");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        lblAgriCardType.setName("lblAgriCardType");
        cboAgriCardType.setName("cboAgriCardType");
        lblAgriCardValidity.setName("lblAgriCardValidity");
        txtNoOfYears.setName("txtNoOfYears");
        cboAgriCardValidity.setName("cboAgriCardValidity");
        lblProdType.setName("lblProdType");
        cboProdType.setName("cboProdType");
        lblProdId.setName("lblProdId");
        cboProdId.setName("cboProdId");
        lblSBInterest.setName("lblSBInterest");
        cRadio_SB_Yes.setName("cRadio_SB_Yes");
        cRadio_SB_No.setName("cRadio_SB_No");
        panSBInterest.setName("panSBInterest");
        btnAgriNew.setName("btnAgriNew");
        btnAgriSave.setName("btnAgriSave");
        btnAgriDelete.setName("btnAgriDelete");
        panButton.setName("panButton");
        panAgriCard.setName("panAgriCard");
        panAgriCardTable.setName("panAgriCardTable");
        //        lblAgriCardType.setName("cboCity");
        //        lblAgriCardValidity.setName("cboCountry");
        //        cboAgriCardValidity.setName("cboState");
        //        cboAgriCardType.setName("");
        //        lblMsg.setName("lblMsg");
        //        panButton.setName("panButton");
        //        panMain.setName("panMain");
        //        panAgriCard.setName("panOtherBank");
        //        panOtherBankBranch.setName("panOtherBankBranch");
        //        panOtherBankBranchTable.setName("panOtherBankBranchTable");
        //        panStatus.setName("panStatus");
        //        panDesgBranch.setName("panDesgBranch");
        //        panHVC.setName("panHVC");
        //        srpOtherBankBranch.setName("srpOtherBankBranch");
        //        tblOtherBankBranch.setName("tblOtherBankBranch");
        //        txtAddress.setName("txtAddress");
        //        txtBankCode.setName("txtBankCode");
        //        txtBankName.setName("txtBankName");
        //        txtBankShortName.setName("txtBankShortName");
        ////        txtBranchName.setName("txtBranchName");
        ////        txtOtherBranchCode.setName("txtOtherBranchCode");
        ////        txtOtherBranchShortName.setName("txtOtherBranchShortName");
        //        txtPincode.setName("txtPincode");
        ////        txtBankType.setName("txtBankType");
        //        txtPhoneNo.setName("txtPhoneNo");
        
    }
    
/* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        lblAgriCardType.setText(resourceBundle.getString("lblAgriCardType"));
        lblAgriCardValidity.setText(resourceBundle.getString("lblAgriCardValidity"));
        lblProdType.setText(resourceBundle.getString("lblProdType"));
        lblProdId.setText(resourceBundle.getString("lblProdId"));
        lblSBInterest.setText(resourceBundle.getString("lblSBInterest"));
        cRadio_SB_Yes.setText(resourceBundle.getString("cRadio_SB_Yes"));
        cRadio_SB_No.setText(resourceBundle.getString("cRadio_SB_No"));
        //        panSBInterest.setText(resourceBundle.getString("lblBankName"));
        btnAgriNew.setText(resourceBundle.getString("btnAgriNew"));
        btnAgriSave.setText(resourceBundle.getString("btnAgriSave"));
        btnAgriDelete.setText(resourceBundle.getString("btnAgriDelete"));
        //        panAgriCard.setText(resourceBundle.getString("panAgriCard"));
        //        panAgriCard.setText(resourceBundle.getString("lblBankName"));
        //        panAgriCardTable.setText(resourceBundle.getString("lblBankName"));
        //        lblBankName.setText(resourceBundle.getString("lblBankName"));
        //        lblHVCClearing.setText(resourceBundle.getString("lblHVCClearing"));
        //        btnClose.setText(resourceBundle.getString("btnClose"));
        //        lblCity.setText(resourceBundle.getString("lblCity"));
        //        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        //        lblMsg.setText(resourceBundle.getString("lblMsg"));
        //        btnException.setText(resourceBundle.getString("btnException"));
        //        lblPincode.setText(resourceBundle.getString("lblPincode"));
        //        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        //        btnSave.setText(resourceBundle.getString("btnSave"));
        //        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        //        lblStatus.setText(resourceBundle.getString("lblStatus"));
        //        btnOtherBankSave.setText(resourceBundle.getString("btnOtherBankSave"));
        //        lblOtherBranchShortName.setText(resourceBundle.getString("lblOtherBranchShortName"));
        //        btnOtherBankNew.setText(resourceBundle.getString("btnOtherBankNew"));
        //        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        //        lblSpace5.setText(resourceBundle.getString("lblSpace5"));
        //        btnDelete.setText(resourceBundle.getString("btnDelete"));
        //        btnOtherBankDelete.setText(resourceBundle.getString("btnOtherBankDelete"));
        //        lblBranchName.setText(resourceBundle.getString("lblBranchName"));
        //        lblOtherBranchCode.setText(resourceBundle.getString("lblOtherBranchCode"));
        //        lblCountry.setText(resourceBundle.getString("lblCountry"));
        //        btnReject.setText(resourceBundle.getString("btnReject"));
        //        ((javax.swing.border.TitledBorder)panOtherBank.getBorder()).setTitle(resourceBundle.getString("panOtherBank"));
        //        btnEdit.setText(resourceBundle.getString("btnEdit"));
        //        lblBankShortName.setText(resourceBundle.getString("lblBankShortName"));
        //        btnNew.setText(resourceBundle.getString("btnNew"));
        //        lblBankCode.setText(resourceBundle.getString("lblBankCode"));
        //        btnCancel.setText(resourceBundle.getString("btnCancel"));
        //        lblState.setText(resourceBundle.getString("lblState"));
        //        lblAddress.setText(resourceBundle.getString("lblAddress"));
        //        ((javax.swing.border.TitledBorder)panOtherBankBranch.getBorder()).setTitle(resourceBundle.getString("panOtherBankBranch"));
        //        btnPrint.setText(resourceBundle.getString("btnPrint"));
        
    }
    
    
/* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        AgriCardMRB objMandatoryRB = new AgriCardMRB();
        //            lblAgriCardType.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBranchName"));
        cboAgriCardType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboAgriCardType"));
        //            lblAgriCardValidity.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBranchName"));
        txtNoOfYears.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNoOfYears"));
        cboAgriCardValidity.setHelpMessage(lblMsg, objMandatoryRB.getString("cboAgriCardValidity"));
        //            lblProdType.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBranchName"));
        cboProdType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProdType"));
        //            lblProdId.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBranchName"));
        cboProdId.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProdId"));
        //            lblSBInterest.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBranchName"));
        cRadio_SB_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("cRadio_SB_Yes"));
        cRadio_SB_No.setHelpMessage(lblMsg, objMandatoryRB.getString("cRadio_SB_No"));
        //            panSBInterest.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBranchName"));
        //            btnAgriNew.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBranchName"));
        //            btnAgriSave.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBranchName"));
        //            btnAgriDelete.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBranchName"));
    }
    
    
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(java.util.Observable observed, Object arg) {
        removeRadioButtons();
        cboAgriCardType.setSelectedItem((observable.getCbmAgriCardType()).getDataForKey(observable.getCboAgriCardType()));
        
        //            cboAgriCardType.setSelectedItem(observable.getCboAgriCardType());
        txtNoOfYears.setText(observable.getTxtNoOfYears());
        cboAgriCardValidity.setSelectedItem(observable.getCboAgriCardValidity());
        cboProdType.setSelectedItem((observable.getCbmProdType()).getDataForKey(observable.getCboProdType()));
        cboProdId.setSelectedItem((observable.getCbmProdId()).getDataForKey(observable.getCboProdId()));
        cRadio_SB_Yes.setSelected(observable.isCRadio_SB_Yes());
        cRadio_SB_No.setSelected(observable.isCRadio_SB_No());
        //            panSBInterest
        //            btnAgriNew
        //            btnAgriSave
        //            btnAgriDelete
        //            panButton
        //            panAgriCard
        //            panAgriCardTable
        ////        txtOtherBranchCode.setText(observable.getTxtOtherBranchCode());
        //        txtOtherBranchShortName.setText(observable.getTxtOtherBranchShortName());
        //        txtBranchName.setText(observable.getTxtBranchName());
        //        txtAddress.setText(observable.getTxtAddress());
        //        txtPincode.setText(observable.getTxtPincode());
        //        cboCountry.setSelectedItem(observable.getCboCountry());
        //        cboState.setSelectedItem(observable.getCboState());
        //        cboCity.setSelectedItem(observable.getCboCity());
        //        txtBankName.setText(observable.getTxtBankName());
        //        txtBankCode.setText(observable.getTxtBankCode());
        //        txtBankShortName.setText(observable.getTxtBankShortName());
        //        tblOtherBankBranch.setModel(observable.getTblOtherBankBranch());
        //        txtPhoneNo.setText(observable.getPhoneNo());
        ////        txtBankType.setText(observable.getTxtBankType());
        //        cRadio_HVC_No.setSelected(observable.isCRadio_HVC_No());
        //        cRadio_HVC_Yes.setSelected(observable.isCRadio_HVC_Yes());
        //        cRadio_DB_Yes.setSelected(observable.isCRadio_DB_Yes());
        //        cRadio_DB_No.setSelected(observable.isCRadio_DB_No());
        //        lblStatus.setText(observable.getLblStatus());
        addRadioButtons();
        
    }
    // To Reset the Radio Buttons in the UI after any operation, We've to
    //1. Remove the Radio Buttons fron the Radio Groups...
    //2. Add the Radio Buttons Back in The Radio Groups...
    //a.) To Remove the Radio buttons...
    private void removeRadioButtons() {
        //        cButtonGroup1.remove(cRadio_HVC_Yes);
        //        cButtonGroup1.remove(cRadio_HVC_No);
        cButtonGroup1.remove(cRadio_SB_Yes);
        cButtonGroup1.remove(cRadio_SB_No);
    }
    
    // b.) To Add the Radio buttons...
    private void addRadioButtons() {
        cButtonGroup1 = new CButtonGroup();
        //        cButtonGroup1.add(cRadio_HVC_Yes);
        //        cButtonGroup1.add(cRadio_HVC_No);
        cButtonGroup1.add(cRadio_SB_Yes);
        cButtonGroup1.add(cRadio_SB_No);
    }
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setCboAgriCardType((String) cboAgriCardType.getSelectedItem());
        observable.setCboAgriCardValidity((String) cboAgriCardValidity.getSelectedItem());
        observable.setTxtNoOfYears(txtNoOfYears.getText());
        observable.setCboProdType((String) cboProdType.getSelectedItem());
        observable.setCboProdId((String) cboProdId.getSelectedItem());
        observable.setCRadio_SB_No(cRadio_SB_No.isSelected());
        observable.setCRadio_SB_Yes(cRadio_SB_Yes.isSelected());
        
    }
    
    
/* Auto Generated Method - setMandatoryHashMap()
 
ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
 
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("lblAgriCardType", new Boolean(true));
        mandatoryMap.put("cboAgriCardType", new Boolean(true));
        mandatoryMap.put("lblAgriCardValidity", new Boolean(true));
        mandatoryMap.put("txtNoOfYears", new Boolean(true));
        mandatoryMap.put("cboAgriCardValidity", new Boolean(true));
        
        //        mandatoryMap.put("lblProdType", new Boolean(true));
        //        mandatoryMap.put("cboProdType", new Boolean(true));
        //        mandatoryMap.put("cboProdId", new Boolean(true));
        //        mandatoryMap.put("txtBankName", new Boolean(true));
        //        mandatoryMap.put("txtBankCode", new Boolean(true));
        //        mandatoryMap.put("txtBankShortName", new Boolean(true));
        
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
        lblSpace11 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace12 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace13 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace14 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace15 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace16 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panMain = new com.see.truetransact.uicomponent.CPanel();
        panAgriCardTable = new com.see.truetransact.uicomponent.CPanel();
        srpOtherBankBranch = new com.see.truetransact.uicomponent.CScrollPane();
        tblAgriCard = new com.see.truetransact.uicomponent.CTable();
        panAgriCard = new com.see.truetransact.uicomponent.CPanel();
        lblAgriCardType = new com.see.truetransact.uicomponent.CLabel();
        cboAgriCardType = new com.see.truetransact.uicomponent.CComboBox();
        lblAgriCardValidity = new com.see.truetransact.uicomponent.CLabel();
        txtNoOfYears = new com.see.truetransact.uicomponent.CTextField();
        panButton = new com.see.truetransact.uicomponent.CPanel();
        btnAgriSave = new com.see.truetransact.uicomponent.CButton();
        btnAgriNew = new com.see.truetransact.uicomponent.CButton();
        btnAgriDelete = new com.see.truetransact.uicomponent.CButton();
        lblProdType = new com.see.truetransact.uicomponent.CLabel();
        cboProdType = new com.see.truetransact.uicomponent.CComboBox();
        lblProdId = new com.see.truetransact.uicomponent.CLabel();
        lblSBInterest = new com.see.truetransact.uicomponent.CLabel();
        cboProdId = new com.see.truetransact.uicomponent.CComboBox();
        cboAgriCardValidity = new com.see.truetransact.uicomponent.CComboBox();
        panSBInterest = new com.see.truetransact.uicomponent.CPanel();
        cRadio_SB_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        cRadio_SB_No = new com.see.truetransact.uicomponent.CRadioButton();
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

        lblSpace11.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace11.setText("     ");
        lblSpace11.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace11.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace11.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOtherBank.add(lblSpace11);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrOtherBank.add(btnEdit);

        lblSpace12.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace12.setText("     ");
        lblSpace12.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace12.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace12.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOtherBank.add(lblSpace12);

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

        lblSpace13.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace13.setText("     ");
        lblSpace13.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace13.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace13.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOtherBank.add(lblSpace13);

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

        lblSpace14.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace14.setText("     ");
        lblSpace14.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace14.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace14.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOtherBank.add(lblSpace14);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrOtherBank.add(btnException);

        lblSpace15.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace15.setText("     ");
        lblSpace15.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace15.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace15.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOtherBank.add(lblSpace15);

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

        lblSpace16.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace16.setText("     ");
        lblSpace16.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace16.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace16.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOtherBank.add(lblSpace16);

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
        panMain.setLayout(new java.awt.GridBagLayout());

        panAgriCardTable.setLayout(new java.awt.GridBagLayout());

        srpOtherBankBranch.setPreferredSize(new java.awt.Dimension(315, 350));

        tblAgriCard.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblAgriCard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblAgriCardMousePressed(evt);
            }
        });
        srpOtherBankBranch.setViewportView(tblAgriCard);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAgriCardTable.add(srpOtherBankBranch, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMain.add(panAgriCardTable, gridBagConstraints);

        panAgriCard.setBorder(javax.swing.BorderFactory.createTitledBorder("Other Bank Branch"));
        panAgriCard.setLayout(new java.awt.GridBagLayout());

        lblAgriCardType.setText("Card Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAgriCard.add(lblAgriCardType, gridBagConstraints);

        cboAgriCardType.setMaximumSize(new java.awt.Dimension(100, 21));
        cboAgriCardType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboAgriCardType.setPopupWidth(200);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAgriCard.add(cboAgriCardType, gridBagConstraints);

        lblAgriCardValidity.setText("Card Validity");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAgriCard.add(lblAgriCardValidity, gridBagConstraints);

        txtNoOfYears.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAgriCard.add(txtNoOfYears, gridBagConstraints);

        panButton.setLayout(new java.awt.GridBagLayout());

        btnAgriSave.setText("Save");
        btnAgriSave.setEnabled(false);
        btnAgriSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgriSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton.add(btnAgriSave, gridBagConstraints);

        btnAgriNew.setText("New");
        btnAgriNew.setEnabled(false);
        btnAgriNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgriNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton.add(btnAgriNew, gridBagConstraints);

        btnAgriDelete.setText("Delete");
        btnAgriDelete.setEnabled(false);
        btnAgriDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgriDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton.add(btnAgriDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        panAgriCard.add(panButton, gridBagConstraints);

        lblProdType.setText("Prod Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAgriCard.add(lblProdType, gridBagConstraints);

        cboProdType.setMaximumSize(new java.awt.Dimension(100, 21));
        cboProdType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAgriCard.add(cboProdType, gridBagConstraints);

        lblProdId.setText("prod Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAgriCard.add(lblProdId, gridBagConstraints);

        lblSBInterest.setText("SB Interest Given");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAgriCard.add(lblSBInterest, gridBagConstraints);

        cboProdId.setMaximumSize(new java.awt.Dimension(100, 21));
        cboProdId.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAgriCard.add(cboProdId, gridBagConstraints);

        cboAgriCardValidity.setMaximumSize(new java.awt.Dimension(100, 21));
        cboAgriCardValidity.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAgriCard.add(cboAgriCardValidity, gridBagConstraints);

        panSBInterest.setLayout(new java.awt.GridBagLayout());

        cButtonGroup1.add(cRadio_SB_Yes);
        cRadio_SB_Yes.setText("Yes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        panSBInterest.add(cRadio_SB_Yes, gridBagConstraints);

        cButtonGroup1.add(cRadio_SB_No);
        cRadio_SB_No.setText("NO");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 0, 4);
        panSBInterest.add(cRadio_SB_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        panAgriCard.add(panSBInterest, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMain.add(panAgriCard, gridBagConstraints);

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
        if (cboProdType.getSelectedIndex() > 0) {
            String prodType = ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString();
            observable.setCbmProdIds(prodType);
            cboProdId.setModel(observable.getCbmProdId());
        }
    }//GEN-LAST:event_cboProdTypeActionPerformed
    
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
        //        if (observable.isBankCodeAlreadyExist(txtBankCode.getText())) {
        //            exist = true;
        //            displayAlert(resourceBundle.getString("BankCodeCount"));
        //        } else {
        //            exist = false;
        //        }
        return exist;
    }
    private void tblAgriCardMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAgriCardMousePressed
        // TODO add your handling code here:
        if (tblAgriCard.getSelectedRow() >= 0 && (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE)) {
            setTableOtherBankBranchEnableDisable(true);
            enableDisableOtherBankBranch_NewSaveDelete(true);// enable New Save Delete buttons
            enableDisablePanOtherBankBranch(true);
            tblAgriCardPress=true;
            //            observable.saveOtherBankBranch(true,tblAgriCard.getSelectedRow());
            
            observable.populateOtherBankBranch(tblAgriCard.getSelectedRow());
            observable.ttNotifyObservers();
            //            updateOtherBankBranch();
        }
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
        || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT){
            enableDisablePanOtherBankBranch(false);
            enableDisablepanButton(false);
        }
        //        if( observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW)
        //        {
        //            setTableOtherBankBranchEnableDisable(true);
        //            enableDisableOtherBankBranch_NewSaveDelete(false);// enable New Save Delete buttons
        //            enableDisablePanOtherBankBranch(true);
        //            ClientUtil.enableDisable(panOtherBankBranch,false);
        //        }
    }//GEN-LAST:event_tblAgriCardMousePressed
    
    private void btnAgriDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgriDeleteActionPerformed
        // TODO add your handling code here:
        if (tblAgriCard.getSelectedRow() >= 0) {
            observable.deleteOtherBankBranch(tblAgriCard.getSelectedRow());
            enableDisableOtherBankBranch_SaveDelete();
            tblAgriCardPress = false;
            observable.resetOtherBankBranch();
            enableDisablePanOtherBankBranch(false);
            updateOtherBankBranch();
        }
    }//GEN-LAST:event_btnAgriDeleteActionPerformed
    
    private void btnAgriSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgriSaveActionPerformed
        // TODO add your handling code here:
        //        final String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panOtherBankBranch);
        //        if (mandatoryMessage.length() > 0) {
        //            displayAlert(mandatoryMessage);
        //       } else {
        updateOBFields();
        observable.saveOtherBankBranch(tblAgriCardPress,tblAgriCard.getSelectedRow());
        enableDisablePanOtherBankBranch(false);
        enableDisableOtherBankBranch_SaveDelete();
        observable.resetOtherBankBranch();
        //            updateOtherBankBranch();
        //        }
        
    }//GEN-LAST:event_btnAgriSaveActionPerformed
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
    private void btnAgriNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgriNewActionPerformed
        // TODO add your handling code here:
        observable.setProdNo("0.0");
        observable.resetOtherBankBranch();
        updateOtherBankBranch();
        tblAgriCardPress = false;
        enableDisableOtherBankBranch_NewSaveDelete(true);
        enableDisablePanOtherBankBranch(true);
        enableDisableBranchCode(true);
    }//GEN-LAST:event_btnAgriNewActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        this.dispose();
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
            mapParam.put(CommonConstants.MAP_NAME, "viewAuthorizeCardDetails");
            //            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeOtherBank");
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
            isFilled = false;
        } else if (viewType == AUTHORIZE ){
            HashMap singleAuthorizeMap = new HashMap();
            HashMap authorizeMap=new HashMap();
            singleAuthorizeMap.put("AUTHORIZE_STATUS", authorizeStatus);
            singleAuthorizeMap.put("USER_ID", TrueTransactMain.USER_ID);
            //            singleAuthorizeMap.put("BANK_CODE", txtBankCode.getText());
            singleAuthorizeMap.put("CURR_DATE", currDt.clone());
            singleAuthorizeMap.put("AGRI_CARD_TYPE",CommonUtil.convertObjToStr(((ComboBoxModel)cboAgriCardType.getModel()).getKeyForSelected()));
            singleAuthorizeMap.put("PROD_ID",CommonUtil.convertObjToStr(((ComboBoxModel)cboProdId.getModel()).getKeyForSelected()));
            authorizeMap.put(CommonConstants.AUTHORIZEDATA,singleAuthorizeMap);
            authorize(authorizeMap);
            //            ClientUtil.execute("authorizeOtherBank", singleAuthorizeMap);
            viewType = -1;
            btnCancelActionPerformed(null);
        }
        observable.resetStatus();
        lblStatus.setText(observable.getLblStatus());
    }
    public void authorize(HashMap authMap){
        System.out.println("authMap$$$$"+authMap);
        if(authMap.get(CommonConstants.AUTHORIZEDATA)!=null) {
            observable .setAuthorizeMap(authMap);
            observable.doAction();
            btnCancelActionPerformed(null);
        }
    }
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        //        super.removeEditLock(txtBankCode.getText());
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        ClientUtil.enableDisable(this, false);
        setButtonEnableDisable();
        enableDisableOtherBankBranch_NewSaveDelete(false);
        viewType = -1;
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        observable.resetForm();
        observable.setStatus();
        
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        String mandatoryMessage="";
        //        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panOtherBank);
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
        //        super.removeEditLock(txtBankCode.getText());
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
        tblAgriCard.setEnabled(flag);
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
        viewType = NEW;
        setTableOtherBankBranchEnableDisable(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        enableDisablePanOtherBank(true);
        enableDisablepanButton(true);
        setButtonEnableDisable();
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        observable.setStatus();
        observable.resetForm();
        
    }//GEN-LAST:event_btnNewActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAgriDelete;
    private com.see.truetransact.uicomponent.CButton btnAgriNew;
    private com.see.truetransact.uicomponent.CButton btnAgriSave;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
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
    private com.see.truetransact.uicomponent.CButtonGroup cButtonGroup1;
    private com.see.truetransact.uicomponent.CButtonGroup cButtonGroup2;
    private com.see.truetransact.uicomponent.CRadioButton cRadio_SB_No;
    private com.see.truetransact.uicomponent.CRadioButton cRadio_SB_Yes;
    private com.see.truetransact.uicomponent.CComboBox cboAgriCardType;
    private com.see.truetransact.uicomponent.CComboBox cboAgriCardValidity;
    private com.see.truetransact.uicomponent.CComboBox cboProdId;
    private com.see.truetransact.uicomponent.CComboBox cboProdType;
    private com.see.truetransact.uicomponent.CLabel lblAgriCardType;
    private com.see.truetransact.uicomponent.CLabel lblAgriCardValidity;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblProdId;
    private com.see.truetransact.uicomponent.CLabel lblProdType;
    private com.see.truetransact.uicomponent.CLabel lblSBInterest;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace11;
    private com.see.truetransact.uicomponent.CLabel lblSpace12;
    private com.see.truetransact.uicomponent.CLabel lblSpace13;
    private com.see.truetransact.uicomponent.CLabel lblSpace14;
    private com.see.truetransact.uicomponent.CLabel lblSpace15;
    private com.see.truetransact.uicomponent.CLabel lblSpace16;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CMenuBar mbrOtherBank;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAgriCard;
    private com.see.truetransact.uicomponent.CPanel panAgriCardTable;
    private com.see.truetransact.uicomponent.CPanel panButton;
    private com.see.truetransact.uicomponent.CPanel panMain;
    private com.see.truetransact.uicomponent.CPanel panSBInterest;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private com.see.truetransact.uicomponent.CScrollPane srpOtherBankBranch;
    private com.see.truetransact.uicomponent.CTable tblAgriCard;
    private javax.swing.JToolBar tbrOtherBank;
    private com.see.truetransact.uicomponent.CTextField txtNoOfYears;
    // End of variables declaration//GEN-END:variables
    
}
