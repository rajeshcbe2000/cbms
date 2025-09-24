/*
 ** Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 * AccountDeathMarkingUI.java
 *
 * Created on June 2, 2004, 6:51 PM
 */

package com.see.truetransact.ui.operativeaccount.deathmarking;

/**
 *
 * @author  Ashok
 */

import java.util.Observer;
import java.util.Observable;
import java.util.HashMap;
import java.util.Date;
import java.util.ArrayList;

import com.see.truetransact.ui.operativeaccount.deathmarking.AccountDeathMarkingRB;
import com.see.truetransact.ui.operativeaccount.deathmarking.AccountDeathMarkingOB;
import com.see.truetransact.ui.operativeaccount.deathmarking.AccountDeathMarkingMRB;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.ui.TrueTransactMain;

public class AccountDeathMarkingUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer, UIMandatoryField {
    
    private AccountDeathMarkingRB resourceBundle = new AccountDeathMarkingRB();
    private AccountDeathMarkingOB observable;
    private AccountDeathMarkingMRB objMandatoryRB;
    private HashMap mandatoryMap;
    private String viewType = "";
    final String AUTHORIZE="Authorize";
    private boolean isFilled = false;
    private Date currDt = null;
    /** Creates new form DeathMarkingUI */
    public AccountDeathMarkingUI() {
        initComponents();
        initGUI();
        
    }
    
    /** Initialsises the GUI componenets */
    private void initGUI(){
        setFieldNames();
        setMaxLengths();
        internationalize();
        setMandatoryHashMap();
        setHelpMessage();
        setObservable();
        initComponentData();
        observable.resetForm();
        ClientUtil.enableDisable(panDeathMark, false);
        setButtonEnableDisable();
        Date currDt = ClientUtil.getCurrentDate();
    }
    
    /* Auto Generated Method - setFieldNames()
      This method assigns name for all the components.
      Other functions are working based on this name. */
    private void setFieldNames() {
        btnAuthorize.setName("btnAuthorize");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnAccountNumber.setName("btnAccountNumber");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        cboProductId.setName("cboProductId");
        cboRelationship.setName("cboRelationship");
        lblAccountHead.setName("lblAccountHead");
        lblAvailableBalance.setName("lblAvailableBalance");
        lblClearingBalance.setName("lblClearingBalance");
        lblCustomerName.setName("lblCustomerName");
        lblDateOfDeath.setName("lblDateOfDeath");
        lblAccountNumber.setName("lblDepositNumber");
        lblHdValue.setName("lblHdValue");
        lblMsg.setName("lblMsg");
        lblNomineeAvailable.setName("lblNomineeAvailable");
        lblProductId.setName("lblProductId");
        lblReferenceNo.setName("lblReferenceNo");
        lblRelationship.setName("lblRelationship");
        lblRemarks.setName("lblRemarks");
        lblReportedBy.setName("lblReportedBy");
        lblReportedOn.setName("lblReportedOn");
        lblSettlementMode.setName("lblSettlementMode");
        lblShadowCredit.setName("lblShadowCredit");
        lblShadowDebit.setName("lblShadowDebit");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblStatus.setName("lblStatus");
        lblValueBalance.setName("lblValueBalance");
        lblValueClearing.setName("lblValueClearing");
        lblValueCredit.setName("lblValueCredit");
        lblValueCustName.setName("lblValueCustName");
        lblValueDebit.setName("lblValueDebit");
        lblValueNominee.setName("lblValueNominee");
        lblValueSettlement.setName("lblValueSettlement");
        mbrDeathMarking.setName("mbrDeathMarking");
        panDeathMark.setName("panDeathMark");
        panDepositNumber.setName("panDepositNumber");
        panStatus.setName("panStatus");
        tdtDtOfDeath.setName("tdtDtOfDeath");
        tdtReportedOn.setName("tdtReportedOn");
        txtAccountNumber.setName("txtDepositNumber");
        txtReferenceNo.setName("txtReferenceNo");
        txtRemarks.setName("txtRemarks");
        txtReportedBy.setName("txtReportedBy");
    }
    
    /* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblAccountHead.setText(resourceBundle.getString("lblAccountHead"));
        lblNomineeAvailable.setText(resourceBundle.getString("lblNomineeAvailable"));
        lblShadowCredit.setText(resourceBundle.getString("lblShadowCredit"));
        lblAvailableBalance.setText(resourceBundle.getString("lblAvailableBalance"));
        lblReportedOn.setText(resourceBundle.getString("lblReportedOn"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        lblRelationship.setText(resourceBundle.getString("lblRelationship"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        lblProductId.setText(resourceBundle.getString("lblProductId"));
        lblClearingBalance.setText(resourceBundle.getString("lblClearingBalance"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblValueCustName.setText(resourceBundle.getString("lblValueCustName"));
        lblValueBalance.setText(resourceBundle.getString("lblValueBalance"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblSettlementMode.setText(resourceBundle.getString("lblSettlementMode"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        btnAccountNumber.setText(resourceBundle.getString("btnAccountNumber"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        lblValueNominee.setText(resourceBundle.getString("lblValueNominee"));
        lblShadowDebit.setText(resourceBundle.getString("lblShadowDebit"));
        lblRemarks.setText(resourceBundle.getString("lblRemarks"));
        lblValueClearing.setText(resourceBundle.getString("lblValueClearing"));
        lblCustomerName.setText(resourceBundle.getString("lblCustomerName"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblDateOfDeath.setText(resourceBundle.getString("lblDateOfDeath"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblHdValue.setText(resourceBundle.getString("lblHdValue"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        lblValueCredit.setText(resourceBundle.getString("lblValueCredit"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblValueDebit.setText(resourceBundle.getString("lblValueDebit"));
        lblReportedBy.setText(resourceBundle.getString("lblReportedBy"));
        lblValueSettlement.setText(resourceBundle.getString("lblValueSettlement"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblAccountNumber.setText(resourceBundle.getString("lblAccountNumber"));
        lblReferenceNo.setText(resourceBundle.getString("lblReferenceNo"));
    }
    
    
/* Auto Generated Method - setMandatoryHashMap()
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboProductId", new Boolean(true));
        mandatoryMap.put("txtAccountNumber", new Boolean(true));
        mandatoryMap.put("tdtDtOfDeath", new Boolean(true));
        mandatoryMap.put("tdtReportedOn", new Boolean(true));
        mandatoryMap.put("txtReportedBy", new Boolean(true));
        mandatoryMap.put("cboRelationship", new Boolean(true));
        mandatoryMap.put("txtReferenceNo", new Boolean(true));
        mandatoryMap.put("txtRemarks", new Boolean(true));
    }
    
/* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    
    
/* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        objMandatoryRB = new AccountDeathMarkingMRB();
        cboProductId.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProductId"));
        txtAccountNumber.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAccountNumber"));
        tdtDtOfDeath.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtDtOfDeath"));
        tdtReportedOn.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtReportedOn"));
        txtReportedBy.setHelpMessage(lblMsg, objMandatoryRB.getString("txtReportedBy"));
        cboRelationship.setHelpMessage(lblMsg, objMandatoryRB.getString("cboRelationship"));
        txtReferenceNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtReferenceNo"));
        txtRemarks.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRemarks"));
    }
    
    
    
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        cboProductId.setSelectedItem(observable.getCboProductId());
        txtAccountNumber.setText(observable.getTxtAccountNumber());
        tdtDtOfDeath.setDateValue(observable.getTdtDtOfDeath());
        tdtReportedOn.setDateValue(observable.getTdtReportedOn());
        txtReportedBy.setText(observable.getTxtReportedBy());
        cboRelationship.setSelectedItem(observable.getCboRelationship());
        txtReferenceNo.setText(observable.getTxtReferenceNo());
        txtRemarks.setText(observable.getTxtRemarks());
    }
    
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setCboProductId((String) cboProductId.getSelectedItem());
        observable.setTxtAccountNumber(txtAccountNumber.getText());
        observable.setTdtDtOfDeath(tdtDtOfDeath.getDateValue());
        observable.setTdtReportedOn(tdtReportedOn.getDateValue());
        observable.setTxtReportedBy(txtReportedBy.getText());
        observable.setCboRelationship((String) cboRelationship.getSelectedItem());
        observable.setTxtReferenceNo(txtReferenceNo.getText());
        observable.setTxtRemarks(txtRemarks.getText());
        observable.setModule(getModule());
        observable.setScreen(getScreen());
    }
    
    private void setObservable() {
        observable = AccountDeathMarkingOB.getInstance();
        observable.addObserver(this);
    }
    
    private void setMaxLengths(){
        txtAccountNumber.setMaxLength(16);
        txtReportedBy.setMaxLength(64);
        txtReferenceNo.setMaxLength(32);
        txtRemarks.setMaxLength(128);
    }
    
    /** Makes the buttons either enable or disable */
    private void setButtonEnableDisable(){
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
        lblStatus.setText(observable.getLblStatus());
        if(observable.getActionType()== ClientConstants.ACTIONTYPE_DELETE){
            btnAccountNumber.setEnabled(btnNew.isEnabled());
        }else  if(observable.getActionType()== ClientConstants.ACTIONTYPE_EDIT){
            btnAccountNumber.setEnabled(!btnNew.isEnabled());
        }else {
            btnAccountNumber.setEnabled(!btnNew.isEnabled());
        }
        if(observable.getLblStatus().equals(ClientConstants.RESULT_STATUS[2]) || observable.getLblStatus().equals(ClientConstants.RESULT_STATUS[3]) || observable.getLblStatus().equals(ClientConstants.RESULT_STATUS[1])){
            btnAccountNumber.setEnabled(!btnClose.isEnabled());
        }
        
        /** Sets the datamodel for the Combobox */
    }
    
    /** Sets the datamodel for the Combobox */
    private void initComponentData(){
        cboProductId.setModel(observable.getCbmProductId());
        cboRelationship.setModel(observable.getCbmRelationship());
    }
    
    /** This method is called up whenever editbuton,deletebutton or the helpbutton is clicked */
    private void callView(String currField) {
        viewType = currField;
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        if (currField.equals(ClientConstants.ACTION_STATUS[2])  ||
        currField.equals(ClientConstants.ACTION_STATUS[3])) {
            viewMap.put(CommonConstants.MAP_NAME, "getSelectAccountDeathMarkingMap");
        } else {
            viewMap.put(CommonConstants.MAP_NAME, "selectAccountDetails");
            ComboBoxModel cbmProductId = observable.getCbmProductId();
            whereMap.put("PROD_ID", cbmProductId.getKeyForSelected());
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        }
        new ViewAll(this,viewMap).show();
    }
    
    /* Fills up the HashMap with data when user selects the row in ViewAll screen  */
    public void fillData(Object  map) {
        try{
            HashMap hash = (HashMap) map;
            final String actNo="ACT_NUM";
            if (viewType != null) {
                if (viewType.equals(ClientConstants.ACTION_STATUS[2]) ||
                viewType.equals(ClientConstants.ACTION_STATUS[3]) ) {
                    isFilled = true;
                    hash.put(CommonConstants.MAP_WHERE, hash.get(actNo));
                    observable.populateData(hash);
                    if (viewType.equals(ClientConstants.ACTION_STATUS[3])) {
                        ClientUtil.enableDisable(panDeathMark, false);
                    } else {
                        ClientUtil.enableDisable(panDeathMark, true);
                    }
                    setButtonEnableDisable();
                }else if(viewType.equals("AccountNumber")){
                    txtAccountNumber.setText((String)hash.get(actNo));
                }
                
            }
            String accountNo = txtAccountNumber.getText();
            HashMap actMap = observable.getActInfo(accountNo);
            if(actMap != null){
                lblValueCustName.setText(CommonUtil.convertObjToStr(actMap.get("CUSTOMER NAME")));
                lblValueSettlement.setText(CommonUtil.convertObjToStr(actMap.get("SETTMT_MODE_ID")));
                int  count = CommonUtil.convertObjToInt(actMap.get("COUNT"));
                if(count > 0){
                    lblValueNominee.setText("YES");
                }else{
                    lblValueNominee.setText("NO");
                }
                lblValueBalance.setText(CommonUtil.convertObjToStr(actMap.get("AVAILABLE_BALANCE")));
                lblValueClearing.setText(CommonUtil.convertObjToStr(actMap.get("CLEAR_BALANCE")));
                lblValueCredit.setText(CommonUtil.convertObjToStr(actMap.get("SHADOW_CREDIT")));
                lblValueDebit.setText(CommonUtil.convertObjToStr(actMap.get("SHADOW_DEBIT")));
            }
        }catch(Exception e){
        }
    }
    
    /* Does necessary operaion when user clicks the save button */
    private void savePerformed(){
        updateOBFields();
        observable.doAction();
        ClientUtil.clearAll(this);
        isFilled = false;
        observable.resetForm();
        ClientUtil.enableDisable(panDeathMark, false);
        setButtonEnableDisable();
        clearLables();
        observable.setResultStatus();
        
    }
    
   /** This will check the mandatoriness of the fields in the UI */
    private String checkMandatory(javax.swing.JComponent component){
        return new MandatoryCheck().checkMandatory(getClass().getName(), component);
    }
    
    /** Display the alertmessage when the mandatory fields are left empty */
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    /* Clears the Labels */
    private void clearLables(){
        lblHdValue.setText("");
        lblValueCustName.setText("");
        lblValueSettlement.setText("");
        lblValueNominee.setText("");
        lblValueBalance.setText("");
        lblValueClearing.setText("");
        lblValueCredit.setText("");
        lblValueDebit.setText("");
    }
    
    /** validates the DtofDeathField */
    private void validateDtOfDeath(){
        try{
            Date dtDeath=  DateUtil.getDateMMDDYYYY(tdtDtOfDeath.getDateValue());
            Date current = (Date) currDt.clone();
            if(dtDeath.compareTo(current) > 0){
                showAlertWindow("currentDateMsg");
                tdtDtOfDeath.setDateValue("");
                tdtDtOfDeath.requestFocus();
            }
        }catch(Exception e){
        }
        return;
    }
    
    /** validates the reportedon datefield */
    private void dateValidation(){
        try{
            Date repOn = DateUtil.getDateMMDDYYYY(tdtReportedOn.getDateValue());
            Date dtDeath=  DateUtil.getDateMMDDYYYY(tdtDtOfDeath.getDateValue());
            if(repOn.compareTo(dtDeath) < 0){
                showAlertWindow("reportedMsg");
                tdtReportedOn.setDateValue("");
                tdtReportedOn.requestFocus();
            }
            Date current = (Date) currDt.clone();
            if(repOn.compareTo(current) > 0){
                showAlertWindow("currentDateMsg");
                tdtReportedOn.setDateValue("");
                tdtReportedOn.requestFocus();
            }
        }
        catch(Exception e){
        }
        return;
    }
    
    /** Displays an aler message during validation of datefields */
    private int showAlertWindow(String alertMsg) throws Exception {
        int optionSelected = 1;
        String[] options = {resourceBundle.getString("cDialogOK")};
        optionSelected = COptionPane.showOptionDialog(null,resourceBundle.getString(alertMsg), CommonConstants.INFORMATIONTITLE,
        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
        null, options, options[0]);
        return optionSelected;
    }
    
    /** Does the authorization for the row selected in the AuthorizationUI screen */
    public void authorizeStatus(String authorizeStatus) {
        if (!isFilled ){
            HashMap mapParam = new HashMap();
            mapParam.put(CommonConstants.MAP_NAME, "getAccountDeathMarkingAuthorizeList");
            mapParam.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
            btnDelete.setEnabled(false);
        } else if (isFilled){
            isFilled = false;
            HashMap singleAuthorizeMap = new HashMap();
            ArrayList arrList = new ArrayList();
            HashMap authDataMap = new HashMap();
            authDataMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            authDataMap.put("ACT_NUM", txtAccountNumber.getText());
            arrList.add(authDataMap);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorize(singleAuthorizeMap);
        }
      }
    
    public void authorize(HashMap map) {
        map.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        observable.setAuthorizeMap(map);
        observable.doAction();
         btnCancelActionPerformed(null);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        tbrDeathMarking = new javax.swing.JToolBar();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        panDeathMark = new com.see.truetransact.uicomponent.CPanel();
        lblProductId = new com.see.truetransact.uicomponent.CLabel();
        cboProductId = new com.see.truetransact.uicomponent.CComboBox();
        lblAccountHead = new com.see.truetransact.uicomponent.CLabel();
        lblHdValue = new com.see.truetransact.uicomponent.CLabel();
        lblAccountNumber = new com.see.truetransact.uicomponent.CLabel();
        lblCustomerName = new com.see.truetransact.uicomponent.CLabel();
        lblValueCustName = new com.see.truetransact.uicomponent.CLabel();
        lblSettlementMode = new com.see.truetransact.uicomponent.CLabel();
        lblValueSettlement = new com.see.truetransact.uicomponent.CLabel();
        lblNomineeAvailable = new com.see.truetransact.uicomponent.CLabel();
        lblValueNominee = new com.see.truetransact.uicomponent.CLabel();
        lblAvailableBalance = new com.see.truetransact.uicomponent.CLabel();
        lblValueBalance = new com.see.truetransact.uicomponent.CLabel();
        lblClearingBalance = new com.see.truetransact.uicomponent.CLabel();
        lblValueClearing = new com.see.truetransact.uicomponent.CLabel();
        lblShadowCredit = new com.see.truetransact.uicomponent.CLabel();
        lblValueCredit = new com.see.truetransact.uicomponent.CLabel();
        lblShadowDebit = new com.see.truetransact.uicomponent.CLabel();
        lblValueDebit = new com.see.truetransact.uicomponent.CLabel();
        lblDateOfDeath = new com.see.truetransact.uicomponent.CLabel();
        tdtDtOfDeath = new com.see.truetransact.uicomponent.CDateField();
        lblReportedOn = new com.see.truetransact.uicomponent.CLabel();
        tdtReportedOn = new com.see.truetransact.uicomponent.CDateField();
        lblReportedBy = new com.see.truetransact.uicomponent.CLabel();
        txtReportedBy = new com.see.truetransact.uicomponent.CTextField();
        lblRelationship = new com.see.truetransact.uicomponent.CLabel();
        cboRelationship = new com.see.truetransact.uicomponent.CComboBox();
        lblReferenceNo = new com.see.truetransact.uicomponent.CLabel();
        txtReferenceNo = new com.see.truetransact.uicomponent.CTextField();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        txtRemarks = new com.see.truetransact.uicomponent.CTextField();
        panDepositNumber = new com.see.truetransact.uicomponent.CPanel();
        txtAccountNumber = new com.see.truetransact.uicomponent.CTextField();
        btnAccountNumber = new com.see.truetransact.uicomponent.CButton();
        mbrDeathMarking = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptNew = new javax.swing.JSeparator();
        sptEdit = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptSave = new javax.swing.JSeparator();
        mitPrint = new javax.swing.JMenuItem();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif")));
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });

        tbrDeathMarking.add(btnNew);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif")));
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        tbrDeathMarking.add(btnEdit);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif")));
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        tbrDeathMarking.add(btnDelete);

        lblSpace2.setText("     ");
        tbrDeathMarking.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif")));
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        tbrDeathMarking.add(btnSave);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif")));
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        tbrDeathMarking.add(btnCancel);

        lblSpace3.setText("     ");
        tbrDeathMarking.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif")));
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });

        tbrDeathMarking.add(btnAuthorize);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif")));
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });

        tbrDeathMarking.add(btnReject);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif")));
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });

        tbrDeathMarking.add(btnException);

        lblSpace4.setText("     ");
        tbrDeathMarking.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif")));
        btnPrint.setToolTipText("Print");
        tbrDeathMarking.add(btnPrint);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif")));
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        tbrDeathMarking.add(btnClose);

        getContentPane().add(tbrDeathMarking, java.awt.BorderLayout.NORTH);

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace1.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace1, gridBagConstraints);

        lblStatus.setBorder(new javax.swing.border.EtchedBorder());
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

        panDeathMark.setLayout(new java.awt.GridBagLayout());

        lblProductId.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDeathMark.add(lblProductId, gridBagConstraints);

        cboProductId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProductId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProductIdActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDeathMark.add(cboProductId, gridBagConstraints);

        lblAccountHead.setText("AccountHead");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDeathMark.add(lblAccountHead, gridBagConstraints);

        lblHdValue.setMaximumSize(new java.awt.Dimension(32, 16));
        lblHdValue.setMinimumSize(new java.awt.Dimension(32, 16));
        lblHdValue.setPreferredSize(new java.awt.Dimension(32, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDeathMark.add(lblHdValue, gridBagConstraints);

        lblAccountNumber.setText("Account Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDeathMark.add(lblAccountNumber, gridBagConstraints);

        lblCustomerName.setText("CustomerName");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 32, 4, 4);
        panDeathMark.add(lblCustomerName, gridBagConstraints);

        lblValueCustName.setMaximumSize(new java.awt.Dimension(100, 16));
        lblValueCustName.setMinimumSize(new java.awt.Dimension(100, 16));
        lblValueCustName.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDeathMark.add(lblValueCustName, gridBagConstraints);

        lblSettlementMode.setText("Settlement Mode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDeathMark.add(lblSettlementMode, gridBagConstraints);

        lblValueSettlement.setMaximumSize(new java.awt.Dimension(100, 21));
        lblValueSettlement.setMinimumSize(new java.awt.Dimension(100, 21));
        lblValueSettlement.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDeathMark.add(lblValueSettlement, gridBagConstraints);

        lblNomineeAvailable.setText("Nominee Available");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDeathMark.add(lblNomineeAvailable, gridBagConstraints);

        lblValueNominee.setMaximumSize(new java.awt.Dimension(100, 21));
        lblValueNominee.setMinimumSize(new java.awt.Dimension(100, 21));
        lblValueNominee.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDeathMark.add(lblValueNominee, gridBagConstraints);

        lblAvailableBalance.setText("Available Balance");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDeathMark.add(lblAvailableBalance, gridBagConstraints);

        lblValueBalance.setMaximumSize(new java.awt.Dimension(100, 21));
        lblValueBalance.setMinimumSize(new java.awt.Dimension(100, 21));
        lblValueBalance.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDeathMark.add(lblValueBalance, gridBagConstraints);

        lblClearingBalance.setText("Clearing Balance");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDeathMark.add(lblClearingBalance, gridBagConstraints);

        lblValueClearing.setMaximumSize(new java.awt.Dimension(100, 21));
        lblValueClearing.setMinimumSize(new java.awt.Dimension(100, 21));
        lblValueClearing.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDeathMark.add(lblValueClearing, gridBagConstraints);

        lblShadowCredit.setText("Shadow Credit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDeathMark.add(lblShadowCredit, gridBagConstraints);

        lblValueCredit.setMaximumSize(new java.awt.Dimension(100, 21));
        lblValueCredit.setMinimumSize(new java.awt.Dimension(100, 21));
        lblValueCredit.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panDeathMark.add(lblValueCredit, gridBagConstraints);

        lblShadowDebit.setText("Shadow Credit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDeathMark.add(lblShadowDebit, gridBagConstraints);

        lblValueDebit.setMaximumSize(new java.awt.Dimension(100, 21));
        lblValueDebit.setMinimumSize(new java.awt.Dimension(100, 21));
        lblValueDebit.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDeathMark.add(lblValueDebit, gridBagConstraints);

        lblDateOfDeath.setText("Date of Death");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDeathMark.add(lblDateOfDeath, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDeathMark.add(tdtDtOfDeath, gridBagConstraints);

        lblReportedOn.setText("Reported On");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 32, 4, 4);
        panDeathMark.add(lblReportedOn, gridBagConstraints);

        tdtReportedOn.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tdtReportedOnFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtReportedOnFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDeathMark.add(tdtReportedOn, gridBagConstraints);

        lblReportedBy.setText("Reported By");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDeathMark.add(lblReportedBy, gridBagConstraints);

        txtReportedBy.setMinimumSize(new java.awt.Dimension(100, 21));
        txtReportedBy.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtReportedByFocusGained(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDeathMark.add(txtReportedBy, gridBagConstraints);

        lblRelationship.setText("Relationship");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 32, 4, 4);
        panDeathMark.add(lblRelationship, gridBagConstraints);

        cboRelationship.setMaximumSize(new java.awt.Dimension(100, 21));
        cboRelationship.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDeathMark.add(cboRelationship, gridBagConstraints);

        lblReferenceNo.setText("Reference No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDeathMark.add(lblReferenceNo, gridBagConstraints);

        txtReferenceNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDeathMark.add(txtReferenceNo, gridBagConstraints);

        lblRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDeathMark.add(lblRemarks, gridBagConstraints);

        txtRemarks.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDeathMark.add(txtRemarks, gridBagConstraints);

        panDepositNumber.setLayout(new java.awt.GridBagLayout());

        txtAccountNumber.setEditable(false);
        txtAccountNumber.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAccountNumber.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAccountNumberFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositNumber.add(txtAccountNumber, gridBagConstraints);

        btnAccountNumber.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnAccountNumber.setMaximumSize(new java.awt.Dimension(21, 21));
        btnAccountNumber.setMinimumSize(new java.awt.Dimension(21, 21));
        btnAccountNumber.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAccountNumber.setEnabled(false);
        btnAccountNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccountNumberActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panDepositNumber.add(btnAccountNumber, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        panDeathMark.add(panDepositNumber, gridBagConstraints);

        getContentPane().add(panDeathMark, java.awt.BorderLayout.WEST);

        mbrDeathMarking.setName("mbrCustomer");
        mnuProcess.setText("Process");
        mnuProcess.setName("mnuProcess");
        mitNew.setText("New");
        mitNew.setName("mitNew");
        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mitEdit.setName("mitEdit");
        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mitDelete.setName("mitDelete");
        mnuProcess.add(mitDelete);

        sptNew.setName("sptNew");
        mnuProcess.add(sptNew);

        sptEdit.setName("sptNew");
        mnuProcess.add(sptEdit);

        mitSave.setText("Save");
        mitSave.setName("mitSave");
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancel");
        mitCancel.setName("mitCancel");
        mnuProcess.add(mitCancel);

        sptSave.setName("sptSave");
        mnuProcess.add(sptSave);

        mitPrint.setText("Print");
        mitPrint.setName("mitPrint");
        mnuProcess.add(mitPrint);

        mitClose.setText("Close");
        mitClose.setName("mitClose");
        mnuProcess.add(mitClose);

        mbrDeathMarking.add(mnuProcess);

        setJMenuBar(mbrDeathMarking);

        pack();
    }//GEN-END:initComponents
    
    private void txtAccountNumberFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccountNumberFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAccountNumberFocusLost
    
    private void btnAccountNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccountNumberActionPerformed
        // TODO add your handling code here:
        callView("AccountNumber");
    }//GEN-LAST:event_btnAccountNumberActionPerformed
    
    private void tdtReportedOnFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtReportedOnFocusLost
        // TODO add your handling code here:
        dateValidation();
    }//GEN-LAST:event_tdtReportedOnFocusLost
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // Add your handling code here:
        authorizeStatus(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // Add your handling code here:
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // Add your handling code here:
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        ClientUtil.clearAll(this);
        clearLables();
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void txtReportedByFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtReportedByFocusGained
        // Add your handling code here:
        
    }//GEN-LAST:event_txtReportedByFocusGained
    
    private void tdtReportedOnFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtReportedOnFocusGained
        // Add your handling code here:
        validateDtOfDeath();
    }//GEN-LAST:event_tdtReportedOnFocusGained
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        observable.resetForm();
        ClientUtil.clearAll(this);
        clearLables();
        ClientUtil.enableDisable(panDeathMark, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        setButtonEnableDisable();
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        String mandatoryMessage = checkMandatory(panDeathMark);
        if(mandatoryMessage.length() > 0){
            displayAlert(mandatoryMessage);
        }else{
            savePerformed();
        }
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        callView("Delete");
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        callView("Edit");
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void cboProductIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProductIdActionPerformed
        // Add your handling code here:
        try{
            observable.setCboProductId((String) cboProductId.getSelectedItem());
            HashMap resultMap = observable.getAcctHeadForProd();
            lblHdValue.setText(CommonUtil.convertObjToStr(resultMap.get("AC_HD_ID")));
        }catch(Exception e){
        }
    }//GEN-LAST:event_cboProductIdActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        observable.resetForm();
        ClientUtil.enableDisable(panDeathMark, true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
    }//GEN-LAST:event_btnNewActionPerformed
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        javax.swing.JFrame frame=new javax.swing.JFrame();
        AccountDeathMarkingUI  tui = new AccountDeathMarkingUI();
        frame.getContentPane().add(tui);
        frame.setSize(600,400);
        frame.show();
        tui.show();
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAccountNumber;
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
    private com.see.truetransact.uicomponent.CComboBox cboProductId;
    private com.see.truetransact.uicomponent.CComboBox cboRelationship;
    private com.see.truetransact.uicomponent.CLabel lblAccountHead;
    private com.see.truetransact.uicomponent.CLabel lblAccountNumber;
    private com.see.truetransact.uicomponent.CLabel lblAvailableBalance;
    private com.see.truetransact.uicomponent.CLabel lblClearingBalance;
    private com.see.truetransact.uicomponent.CLabel lblCustomerName;
    private com.see.truetransact.uicomponent.CLabel lblDateOfDeath;
    private com.see.truetransact.uicomponent.CLabel lblHdValue;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNomineeAvailable;
    private com.see.truetransact.uicomponent.CLabel lblProductId;
    private com.see.truetransact.uicomponent.CLabel lblReferenceNo;
    private com.see.truetransact.uicomponent.CLabel lblRelationship;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
    private com.see.truetransact.uicomponent.CLabel lblReportedBy;
    private com.see.truetransact.uicomponent.CLabel lblReportedOn;
    private com.see.truetransact.uicomponent.CLabel lblSettlementMode;
    private com.see.truetransact.uicomponent.CLabel lblShadowCredit;
    private com.see.truetransact.uicomponent.CLabel lblShadowDebit;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblValueBalance;
    private com.see.truetransact.uicomponent.CLabel lblValueClearing;
    private com.see.truetransact.uicomponent.CLabel lblValueCredit;
    private com.see.truetransact.uicomponent.CLabel lblValueCustName;
    private com.see.truetransact.uicomponent.CLabel lblValueDebit;
    private com.see.truetransact.uicomponent.CLabel lblValueNominee;
    private com.see.truetransact.uicomponent.CLabel lblValueSettlement;
    private com.see.truetransact.uicomponent.CMenuBar mbrDeathMarking;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panDeathMark;
    private com.see.truetransact.uicomponent.CPanel panDepositNumber;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private javax.swing.JSeparator sptEdit;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private javax.swing.JToolBar tbrDeathMarking;
    private com.see.truetransact.uicomponent.CDateField tdtDtOfDeath;
    private com.see.truetransact.uicomponent.CDateField tdtReportedOn;
    private com.see.truetransact.uicomponent.CTextField txtAccountNumber;
    private com.see.truetransact.uicomponent.CTextField txtReferenceNo;
    private com.see.truetransact.uicomponent.CTextField txtRemarks;
    private com.see.truetransact.uicomponent.CTextField txtReportedBy;
    // End of variables declaration//GEN-END:variables
    
}
