/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * UserUI.java
 *
 * Created on September 29, 2003, 5:04 PM
 */
package com.see.truetransact.ui.sysadmin.user;

import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uicomponent.CDateField;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.EmailValidation;
import com.see.truetransact.uivalidation.ToDateValidation;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.customer.CheckCustomerIdUI;
import com.see.truetransact.uivalidation.NumericValidation;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import java.util.Date;
/**
 *
 * @author  karthik
 * @author  Bala
 *
 * @modified Pinky
 * @modified Sunil 23 Aug 2005
 *  Added Foreign Branch Group and Foreign Group Id fields
 */
public class UserUI extends CInternalFrame implements Observer, UIMandatoryField {

    private UserRB resourceBundle;
    private HashMap mandatoryMap;
    private UserMRB objMandatoryRB;
    private UserOB observable;
    private boolean actionAuthExcepReject = false;
    private boolean _intRateNew = false;
    private int viewType = -1;
    final int EDIT = 0, DELETE = 1, AUTHORIZE = 2, EMPLOYEE_ID = 4, TERMINAL_ID = 5, VIEW = 6, CUSTOMER= 7;
    boolean isFilled = false;
    private StringBuffer strBuf;
    String statusModified = null;
    String userID = null;
    private String view = "";
    private Date currDt = null;
    /** Creates new form UserUI */
    public UserUI() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        initSetup();
    }

    /* Calling methods for the initial setup. */
    private void initSetup() {
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setObservable();
        setHelpMessage();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panUserMain);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panTerminalDetails);
        setMaxLength();
        setComponent();
        setUp(ClientConstants.ACTIONTYPE_CANCEL, false);
        enableDisableSuspendFields(false);
        btnView.setEnabled(!btnView.isEnabled());
        chkLockedStatus.setSelected(false);
        chkLockedStatus.setEnabled(false);
        statusModified=null;
        txtCustomerId.setEnabled(false);
       // pwdConfirmPassword.setEnabled(false);
       // txtCustomerName.setEnabled(false);
    }

    private void enableDisableSuspendFields(boolean flag) {
        this.txtReasonForSuspend.setEditable(flag);
        this.txtReasonForSuspend.setEnabled(flag);
        this.tdtSuspendFrom.setEnabled(flag);
        this.tdtSuspendTo.setEnabled(flag);
    }
    /* Auto Generated Method - setFieldNames()
    This method assigns name for all the components.
    Other functions are working based on this name. */

    private void setFieldNames() {
        btnAdd.setName("btnAdd");
        btnAuthorize.setName("btnAuthorize");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnCustomerID.setName("btnCustomerID");
        btnException.setName("btnException");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnRemove.setName("btnRemove");
        btnSave.setName("btnSave");
        btnTerminalID.setName("btnTerminalID");
        cboGroupID.setName("cboGroupID");
        cboRoleID.setName("cboRoleID");
        lblForeignGroupId.setName("lblForeignGroupId");
        cboForeignGroupId.setName("cboForeignGroupId");
        lblForeignBranchGroup.setName("lblForeignBranchGroup");
        cboForeignBranchGroup.setName("cboForeignBranchGroup");
        chkAppraiserAllowed.setName("chkAppraiserAllowed");
        chkSmartCard.setName("chkSmartCard");
        chkSuspend.setName("chkSuspend");
        chkUserSuspend.setName("chkUserSuspend");
        lblAccessFromDate.setName("lblAccessFromDate");
        lblAccessToDate.setName("lblAccessToDate");
        lblActivateUser.setName("lblActivateUser");
        lblBranchID.setName("lblBranchID");
        lblBranchName.setName("lblBranchName");
        lblConfirmPassword.setName("lblConfirmPassword");
        lblDisplayCustName.setName("lblDisplayCustName");
        lblDisplayLastLoginDate.setName("lblDisplayLastLoginDate");
        lblEmailId.setName("lblEmailId");
        lblCustomerId.setName("lblCustomerId");
        lblCustomerName.setName("lblCustomerName");
        lblGroupID.setName("lblGroupID");
        lblIpAddress.setName("lblIpAddress");
        lblLastLoginDate.setName("lblLastLoginDate");
        lblMachineName.setName("lblMachineName");
        lblMsg.setName("lblMsg");
        lblPassword.setName("lblPassword");
        lblReasonForSuspend.setName("lblReasonForSuspend");
        lblRemarks.setName("lblRemarks");
        lblRoleID.setName("lblRoleID");
        lblSmartCard.setName("lblSmartCard");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace5.setName("lblSpace5");
        lblStatus.setName("lblStatus");
        lblSuspend.setName("lblSuspend");
        lblSuspendFrom.setName("lblSuspendFrom");
        lblSuspendTo.setName("lblSuspendTo");
        lblTerminalDescription.setName("lblTerminalDescription");
        lblTerminalId.setName("lblTerminalId");
        lblTerminalName.setName("lblTerminalName");
        lblUserId.setName("lblUserId");
        lblUserIdExpiresOn.setName("lblUserIdExpiresOn");
        lblUserSuspend.setName("lblUserSuspend");
        mbrMain.setName("mbrMain");
        panSettings.setName("panSettings");
        panStatus.setName("panStatus");
        panTerminal.setName("panTerminal");
        panTerminalButtons.setName("panTerminalButtons");
        panTerminalDetails.setName("panTerminalDetails");
        panTerminalID.setName("panTerminalID");
        panUserInfo.setName("panUserInfo");
        panUserMain.setName("panUserMain");
        pwdConfirmPassword.setName("pwdConfirmPassword");
        pwdPassword.setName("pwdPassword");
        srpTerminal.setName("srpTerminal");
        tabUser.setName("tabUser");
        tblTerminal.setName("tblTerminal");
        tdtAccessFromDate.setName("tdtAccessFromDate");
        tdtAccessToDate.setName("tdtAccessToDate");
        tdtActivateUser.setName("tdtActivateUser");
        tdtSuspendFrom.setName("tdtSuspendFrom");
        tdtSuspendTo.setName("tdtSuspendTo");
        tdtUserIdExpiresOn.setName("tdtUserIdExpiresOn");
        txtBranchID.setName("txtBranchID");
        txtBranchName.setName("txtBranchName");
        txtEmailId.setName("txtEmailId");
        txtCustomerId.setName("txtCustomerId");
        txtIPAddress.setName("txtIPAddress");
        txtMachineName.setName("txtMachineName");
        txtReasonForSuspend.setName("txtReasonForSuspend");
        txtRemarks.setName("txtRemarks");
        txtTerminalDescription.setName("txtTerminalDescription");
        txtTerminalId.setName("txtTerminalId");
        txtTerminalName.setName("txtTerminalName");
        txtUserId.setName("txtUserId");
    }

    private boolean passWordConfirmation() {
        boolean confirmed = false;
        char[] pwd1 = pwdPassword.getPassword();
        char[] pwd2 = pwdConfirmPassword.getPassword();
        if (pwd1.length == pwd2.length) {
            int i;
            for (i = 0; i < pwd1.length; i++) {
                if (pwd1[i] != pwd2[i]) {
                    break;
                }
            }
            if (i == pwd1.length) {
                confirmed = true;
            }
        }
        //        JOptionPane.showMessageDialog(this,"Both passwords should be same");
        //        this.pwdConfirmPassword.setText("");
        return confirmed;
        //        this.pwdConfirmPassword.requestFocus();
    }

    /* Auto Generated Method - internationalize()
    This method used to assign display texts from
    the Resource Bundle File. */
    private void internationalize() {
        resourceBundle = new UserRB();
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblRoleID.setText(resourceBundle.getString("lblRoleID"));
        lblForeignGroupId.setText(resourceBundle.getString("lblForeignGroupId"));
        lblForeignBranchGroup.setText(resourceBundle.getString("lblForeignBranchGroup"));
        lblTerminalId.setText(resourceBundle.getString("lblTerminalId"));
        lblCustomerId.setText(resourceBundle.getString("lblCustomerId"));
        btnAdd.setText(resourceBundle.getString("btnAdd"));
        lblUserId.setText(resourceBundle.getString("lblUserId"));
        lblCustomerName.setText(resourceBundle.getString("lblCustomerName"));
        lblGroupID.setText(resourceBundle.getString("lblGroupID"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        lblDisplayCustName.setText(resourceBundle.getString("lblDisplayCustName"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        lblSmartCard.setText(resourceBundle.getString("lblSmartCard"));
        lblMachineName.setText(resourceBundle.getString("lblMachineName"));
        chkSmartCard.setText(resourceBundle.getString("chkSmartCard"));
        chkAppraiserAllowed.setText(resourceBundle.getString("chkAppraiserAllowed"));
        lblLastLoginDate.setText(resourceBundle.getString("lblLastLoginDate"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        btnRemove.setText(resourceBundle.getString("btnRemove"));
        lblEmailId.setText(resourceBundle.getString("lblEmailId"));
        lblSuspendFrom.setText(resourceBundle.getString("lblSuspendFrom"));
        lblDisplayLastLoginDate.setText(resourceBundle.getString("lblDisplayLastLoginDate"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblSuspendTo.setText(resourceBundle.getString("lblSuspendTo"));
        lblAccessToDate.setText(resourceBundle.getString("lblAccessToDate"));
        chkSuspend.setText(resourceBundle.getString("chkSuspend"));
        chkUserSuspend.setText(resourceBundle.getString("chkUserSuspend"));
        lblConfirmPassword.setText(resourceBundle.getString("lblConfirmPassword"));
        lblReasonForSuspend.setText(resourceBundle.getString("lblReasonForSuspend"));
        lblUserIdExpiresOn.setText(resourceBundle.getString("lblUserIdExpiresOn"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblTerminalDescription.setText(resourceBundle.getString("lblTerminalDescription"));
        lblUserSuspend.setText(resourceBundle.getString("lblUserSuspend"));
        ((javax.swing.border.TitledBorder) panSettings.getBorder()).setTitle(resourceBundle.getString("panSettings"));
        lblBranchID.setText(resourceBundle.getString("lblBranchID"));
        btnCustomerID.setText(resourceBundle.getString("btnCustomerID"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblAccessFromDate.setText(resourceBundle.getString("lblAccessFromDate"));
        btnTerminalID.setText(resourceBundle.getString("btnTerminalID"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblRemarks.setText(resourceBundle.getString("lblRemarks"));
        lblSpace5.setText(resourceBundle.getString("lblSpace5"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblIpAddress.setText(resourceBundle.getString("lblIpAddress"));
        lblSuspend.setText(resourceBundle.getString("lblSuspend"));
        lblTerminalName.setText(resourceBundle.getString("lblTerminalName"));
        lblPassword.setText(resourceBundle.getString("lblPassword"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblActivateUser.setText(resourceBundle.getString("lblActivateUser"));
        lblBranchName.setText(resourceBundle.getString("lblBranchName"));
    }

    /* Auto Generated Method - update()
    This method called by Observable. It updates the UI with
    Observable's data. If needed add/Remove RadioButtons
    method need to be added.*/
    public void update(Observable observed, Object arg) {
        txtCustomerId.setText(observable.getTxtCustomerId());
        txtUserId.setText(observable.getTxtUserId());
        txtEmailId.setText(observable.getTxtEmailId());
        tdtUserIdExpiresOn.setDateValue(observable.getTdtUserIdExpiresOn());
        txtRemarks.setText(observable.getTxtRemarks());
        pwdPassword.setText(observable.getPwdPassword());
        pwdConfirmPassword.setText(observable.getPwdConfirmPassword());
        txtBranchID.setText(observable.getTxtBranchID());
        txtBranchName.setText(observable.getTxtBranchName());
        cboGroupID.setSelectedItem(observable.getCboGroupID());
        cboRoleID.setSelectedItem(observable.getCboRoleID());
        cboForeignGroupId.setSelectedItem(observable.getCboForeignGroupId());
        cboForeignBranchGroup.setSelectedItem(observable.getCboForeignBranchGroup());
        tdtActivateUser.setDateValue(observable.getTdtActivateUser());
        chkSmartCard.setSelected(observable.getChkSmartCard());
        chkUserSuspend.setSelected(observable.getChkUserSuspend());
        chkSuspend.setSelected(observable.getChkSuspend());
         chkAppraiserAllowed.setSelected(observable.isChkAppraiserAllowed());
        txtReasonForSuspend.setText(observable.getTxtReasonForSuspend());
        tdtSuspendFrom.setDateValue(observable.getTdtSuspendFrom());
        tdtSuspendTo.setDateValue(observable.getTdtSuspendTo());
        txtTerminalId.setText(observable.getTxtTerminalId());
        txtTerminalName.setText(observable.getTxtTerminalName());
        txtIPAddress.setText(observable.getTxtIPAddress());
        txtMachineName.setText(observable.getTxtMachineName());
        txtTerminalDescription.setText(observable.getTxtTerminalDescription());
        tdtAccessToDate.setDateValue(observable.getTdtAccessToDate());
        tdtAccessFromDate.setDateValue(observable.getTdtAccessFromDate());
        lblDisplayCustName.setText(observable.getLblDisplayCustName());
        lblStatus.setText(observable.getLblStatus());
        lblDisplayLastLoginDate.setText(observable.getLblLastLoginDate());
        tblTerminal.setModel(observable.getTmlTerminals());
        cboGroupID.setModel(observable.getCbmGroupID());
        cboRoleID.setModel(observable.getCbmRoleID());
        cboForeignGroupId.setModel(observable.getCbmForeignGroupId());
        cboForeignBranchGroup.setModel(observable.getCbmForeignBranchGroup());
    }

    /* Auto Generated Method - updateOBFields()
    This method called by Save option of UI.
    It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setTxtCustomerId(txtCustomerId.getText());
        observable.setTxtUserId(txtUserId.getText());
        observable.setTxtEmailId(txtEmailId.getText());
        observable.setTdtUserIdExpiresOn(tdtUserIdExpiresOn.getDateValue());
        observable.setTxtRemarks(txtRemarks.getText());
        observable.setPwdPassword(new String(pwdPassword.getPassword()));
        observable.setPwdConfirmPassword(new String(pwdConfirmPassword.getPassword()));
        observable.setTxtBranchID(txtBranchID.getText());
        observable.setTxtBranchName(txtBranchName.getText());
        observable.setCboRoleID((String) cboRoleID.getSelectedItem());
        observable.setCboForeignGroupId((String) cboForeignGroupId.getSelectedItem());
        observable.setCboForeignBranchGroup((String) cboForeignBranchGroup.getSelectedItem());
        observable.setCboGroupID((String) cboGroupID.getSelectedItem());
        observable.setTdtActivateUser(tdtActivateUser.getDateValue());
        observable.setChkSmartCard(chkSmartCard.isSelected());
        observable.setChkAppraiserAllowed(chkAppraiserAllowed.isSelected());
        observable.setChkUserSuspend(chkUserSuspend.isSelected());
        observable.setChkSuspend(chkSuspend.isSelected());
        observable.setTxtReasonForSuspend(txtReasonForSuspend.getText());
        observable.setTdtSuspendFrom(tdtSuspendFrom.getDateValue());
        observable.setTdtSuspendTo(tdtSuspendTo.getDateValue());
        observable.setTxtTerminalId(txtTerminalId.getText());
        observable.setTxtTerminalName(txtTerminalName.getText());
        observable.setTxtIPAddress(txtIPAddress.getText());
        observable.setTxtMachineName(txtMachineName.getText());
        observable.setTxtTerminalDescription(txtTerminalDescription.getText());
        observable.setTdtAccessToDate(tdtAccessToDate.getDateValue());
        observable.setTdtAccessFromDate(tdtAccessFromDate.getDateValue());
        observable.setLblDisplayCustName(lblDisplayCustName.getText());
        observable.setCbmGroupID((ComboBoxModel) cboGroupID.getModel());
        observable.setCbmRoleID((ComboBoxModel) cboRoleID.getModel());
        observable.setTmlTerminals((TableModel) tblTerminal.getModel());
    }

    /* Auto Generated Method - setMandatoryHashMap()
    
    ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
    
    This method list out all the Input Fields available in the UI.
    It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtCustomerId", new Boolean(true));
        mandatoryMap.put("txtUserId", new Boolean(true));
        mandatoryMap.put("txtEmailId", new Boolean(true));
        mandatoryMap.put("tdtUserIdExpiresOn", new Boolean(true));
        mandatoryMap.put("txtRemarks", new Boolean(true));
        mandatoryMap.put("pwdPassword", new Boolean(true));
        mandatoryMap.put("pwdConfirmPassword", new Boolean(true));
        mandatoryMap.put("txtBranchID", new Boolean(true));
        mandatoryMap.put("txtBranchName", new Boolean(true));
        mandatoryMap.put("cboRoleID", new Boolean(true));
        mandatoryMap.put("cboForeignGroupId", new Boolean(true));
        mandatoryMap.put("cboForeignBranchGroup", new Boolean(true));
        mandatoryMap.put("cboGroupID", new Boolean(true));
        mandatoryMap.put("tdtActivateUser", new Boolean(true));
        mandatoryMap.put("chkSmartCard", new Boolean(true));
        mandatoryMap.put("chkUserSuspend", new Boolean(true));
        mandatoryMap.put("chkSuspend", new Boolean(true));
        mandatoryMap.put("txtReasonForSuspend", new Boolean(true));
        mandatoryMap.put("tdtSuspendFrom", new Boolean(true));
        mandatoryMap.put("tdtSuspendTo", new Boolean(true));
        mandatoryMap.put("txtTerminalId", new Boolean(true));
        mandatoryMap.put("txtTerminalName", new Boolean(true));
        mandatoryMap.put("txtIPAddress", new Boolean(true));
        mandatoryMap.put("txtMachineName", new Boolean(true));
        mandatoryMap.put("txtTerminalDescription", new Boolean(true));
        mandatoryMap.put("tdtAccessToDate", new Boolean(true));
        mandatoryMap.put("tdtAccessFromDate", new Boolean(true));
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
        objMandatoryRB = new UserMRB();
        txtCustomerId.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCustomerId"));
        txtUserId.setHelpMessage(lblMsg, objMandatoryRB.getString("txtUserId"));
        tdtUserIdExpiresOn.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtUserIdExpiresOn"));
        txtRemarks.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRemarks"));
        pwdPassword.setHelpMessage(lblMsg, objMandatoryRB.getString("pwdPassword"));
        pwdConfirmPassword.setHelpMessage(lblMsg, objMandatoryRB.getString("pwdConfirmPassword"));
        txtBranchID.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBranchID"));
        txtBranchName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBranchName"));
        cboRoleID.setHelpMessage(lblMsg, objMandatoryRB.getString("cboRoleID"));
        cboForeignGroupId.setHelpMessage(lblMsg, objMandatoryRB.getString("cboForeignGroupId"));
        cboForeignBranchGroup.setHelpMessage(lblMsg, objMandatoryRB.getString("cboForeignBranchGroup"));
        cboGroupID.setHelpMessage(lblMsg, objMandatoryRB.getString("cboGroupID"));
        tdtActivateUser.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtActivateUser"));
        chkSmartCard.setHelpMessage(lblMsg, objMandatoryRB.getString("chkSmartCard"));
        chkAppraiserAllowed.setHelpMessage(lblMsg, objMandatoryRB.getString("chkAppraiserAllowed"));
        chkUserSuspend.setHelpMessage(lblMsg, objMandatoryRB.getString("chkUserSuspend"));
        chkSuspend.setHelpMessage(lblMsg, objMandatoryRB.getString("chkSuspend"));
        txtReasonForSuspend.setHelpMessage(lblMsg, objMandatoryRB.getString("txtReasonForSuspend"));
        tdtSuspendFrom.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtSuspendFrom"));
        tdtSuspendTo.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtSuspendTo"));
        txtTerminalId.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTerminalId"));
        txtTerminalName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTerminalName"));
        txtIPAddress.setHelpMessage(lblMsg, objMandatoryRB.getString("txtIPAddress"));
        txtMachineName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMachineName"));
        txtTerminalDescription.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTerminalDescription"));
        tdtAccessToDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtAccessToDate"));
        tdtAccessFromDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtAccessFromDate"));
    }

    public void setMaxLength() {
//        txtUserId.setValidation(new NumericValidation());
        txtEmailId.setMaxLength(64);
        txtEmailId.setValidation(new EmailValidation());
        txtRemarks.setMaxLength(256);
        txtReasonForSuspend.setMaxLength(256);
        txtUserId.setAllowAll(true);
    }

    /** Set observable */
    private void setObservable() {
        observable = UserOB.getInstance();
        observable.addObserver(this);
    }

    private void setComponent() {
        tblTerminal.setModel(observable.getTmlTerminals());
    }
    //Enable/Disable include/exclude buttons.

    private void enableDisableButtons(boolean enableDisable) {
        btnAdd.setEnabled(enableDisable);
        btnRemove.setEnabled(false);
        btnCustomerID.setEnabled(enableDisable);
        btnTerminalID.setEnabled(enableDisable);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        tbrOperativeAcctProduct = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
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
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace22 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        tabUser = new com.see.truetransact.uicomponent.CTabbedPane();
        panUserMain = new com.see.truetransact.uicomponent.CPanel();
        panUserInfo = new com.see.truetransact.uicomponent.CPanel();
        lblEmployeeId = new com.see.truetransact.uicomponent.CLabel();
        txtEmployeeId = new com.see.truetransact.uicomponent.CTextField();
        lblUserId = new com.see.truetransact.uicomponent.CLabel();
        txtUserId = new com.see.truetransact.uicomponent.CTextField();
        lblPassword = new com.see.truetransact.uicomponent.CLabel();
        lblDisplayCustName = new com.see.truetransact.uicomponent.CLabel();
        lblEmailId = new com.see.truetransact.uicomponent.CLabel();
        txtEmailId = new com.see.truetransact.uicomponent.CTextField();
        lblUserIdExpiresOn = new com.see.truetransact.uicomponent.CLabel();
        tdtUserIdExpiresOn = new com.see.truetransact.uicomponent.CDateField();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        txtRemarks = new com.see.truetransact.uicomponent.CTextField();
        pwdPassword = new com.see.truetransact.uicomponent.CPasswordField();
        btnCustomerID = new com.see.truetransact.uicomponent.CButton();
        lblBranchID = new com.see.truetransact.uicomponent.CLabel();
        lblBranchName = new com.see.truetransact.uicomponent.CLabel();
        txtBranchID = new com.see.truetransact.uicomponent.CTextField();
        txtBranchName = new com.see.truetransact.uicomponent.CTextField();
        lblGroupID = new com.see.truetransact.uicomponent.CLabel();
        lblRoleID = new com.see.truetransact.uicomponent.CLabel();
        cboRoleID = new com.see.truetransact.uicomponent.CComboBox();
        cboGroupID = new com.see.truetransact.uicomponent.CComboBox();
        tdtActivateUser = new com.see.truetransact.uicomponent.CDateField();
        lblActivateUser = new com.see.truetransact.uicomponent.CLabel();
        lblEmployeeName = new com.see.truetransact.uicomponent.CLabel();
        lblDisplayEmpName = new com.see.truetransact.uicomponent.CLabel();
        lblLastLoginDate = new com.see.truetransact.uicomponent.CLabel();
        lblDisplayLastLoginDate = new com.see.truetransact.uicomponent.CLabel();
        lblForeignGroupId = new com.see.truetransact.uicomponent.CLabel();
        cboForeignGroupId = new com.see.truetransact.uicomponent.CComboBox();
        lblForeignBranchGroup = new com.see.truetransact.uicomponent.CLabel();
        cboForeignBranchGroup = new com.see.truetransact.uicomponent.CComboBox();
        lblAppraiserAllowed = new com.see.truetransact.uicomponent.CLabel();
        chkAppraiserAllowed = new com.see.truetransact.uicomponent.CCheckBox();
        lblCustomerId = new com.see.truetransact.uicomponent.CLabel();
        txtCustomerId = new com.see.truetransact.uicomponent.CPasswordField();
        lblCustomerName = new com.see.truetransact.uicomponent.CLabel();
        lblConfirmPassword = new com.see.truetransact.uicomponent.CLabel();
        pwdConfirmPassword = new com.see.truetransact.uicomponent.CPasswordField();
        panSettings = new com.see.truetransact.uicomponent.CPanel();
        chkSmartCard = new com.see.truetransact.uicomponent.CCheckBox();
        chkUserSuspend = new com.see.truetransact.uicomponent.CCheckBox();
        chkSuspend = new com.see.truetransact.uicomponent.CCheckBox();
        lblUserSuspend = new com.see.truetransact.uicomponent.CLabel();
        lblSmartCard = new com.see.truetransact.uicomponent.CLabel();
        lblSuspend = new com.see.truetransact.uicomponent.CLabel();
        lblSuspendFrom = new com.see.truetransact.uicomponent.CLabel();
        lblSuspendTo = new com.see.truetransact.uicomponent.CLabel();
        lblReasonForSuspend = new com.see.truetransact.uicomponent.CLabel();
        txtReasonForSuspend = new com.see.truetransact.uicomponent.CTextField();
        tdtSuspendFrom = new com.see.truetransact.uicomponent.CDateField();
        tdtSuspendTo = new com.see.truetransact.uicomponent.CDateField();
        lblLockedStatus = new com.see.truetransact.uicomponent.CLabel();
        chkLockedStatus = new com.see.truetransact.uicomponent.CCheckBox();
        panTerminal = new com.see.truetransact.uicomponent.CPanel();
        srpTerminal = new com.see.truetransact.uicomponent.CScrollPane();
        tblTerminal = new com.see.truetransact.uicomponent.CTable();
        panTerminalButtons = new com.see.truetransact.uicomponent.CPanel();
        btnAdd = new com.see.truetransact.uicomponent.CButton();
        btnRemove = new com.see.truetransact.uicomponent.CButton();
        panTerminalDetails = new com.see.truetransact.uicomponent.CPanel();
        panTerminalID = new com.see.truetransact.uicomponent.CPanel();
        txtTerminalId = new com.see.truetransact.uicomponent.CTextField();
        btnTerminalID = new com.see.truetransact.uicomponent.CButton();
        txtTerminalName = new com.see.truetransact.uicomponent.CTextField();
        lblTerminalName = new com.see.truetransact.uicomponent.CLabel();
        lblIpAddress = new com.see.truetransact.uicomponent.CLabel();
        txtIPAddress = new com.see.truetransact.uicomponent.CTextField();
        txtMachineName = new com.see.truetransact.uicomponent.CTextField();
        lblMachineName = new com.see.truetransact.uicomponent.CLabel();
        lblTerminalDescription = new com.see.truetransact.uicomponent.CLabel();
        txtTerminalDescription = new com.see.truetransact.uicomponent.CTextField();
        lblTerminalId = new com.see.truetransact.uicomponent.CLabel();
        lblAccessFromDate = new com.see.truetransact.uicomponent.CLabel();
        lblAccessToDate = new com.see.truetransact.uicomponent.CLabel();
        tdtAccessToDate = new com.see.truetransact.uicomponent.CDateField();
        tdtAccessFromDate = new com.see.truetransact.uicomponent.CDateField();
        mbrMain = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptDelete = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitAuthorize = new javax.swing.JMenuItem();
        mitException = new javax.swing.JMenuItem();
        mitReject = new javax.swing.JMenuItem();
        sptException = new javax.swing.JSeparator();
        mitPrint = new javax.swing.JMenuItem();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMinimumSize(new java.awt.Dimension(620, 700));
        setPreferredSize(new java.awt.Dimension(590, 665));

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

        lblSpace4.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace4);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnNew);

        lblSpace17.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace17.setText("     ");
        lblSpace17.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace17);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnEdit);

        lblSpace18.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace18.setText("     ");
        lblSpace18.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace18.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace18.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace18);

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

        lblSpace19.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace19.setText("     ");
        lblSpace19.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace19.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace19.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace19);

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

        lblSpace20.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace20.setText("     ");
        lblSpace20.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace20.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace20.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace20);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnException);

        lblSpace21.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace21.setText("     ");
        lblSpace21.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace21);

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
        tbrOperativeAcctProduct.add(btnReject);

        lblSpace5.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnPrint);

        lblSpace22.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace22.setText("     ");
        lblSpace22.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace22);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnClose);

        getContentPane().add(tbrOperativeAcctProduct, java.awt.BorderLayout.NORTH);

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

        tabUser.setMinimumSize(new java.awt.Dimension(590, 650));
        tabUser.setPreferredSize(new java.awt.Dimension(590, 650));

        panUserMain.setMinimumSize(new java.awt.Dimension(700, 385));
        panUserMain.setPreferredSize(new java.awt.Dimension(590, 620));
        panUserMain.setLayout(new java.awt.GridBagLayout());

        panUserInfo.setMinimumSize(new java.awt.Dimension(450, 355));
        panUserInfo.setPreferredSize(new java.awt.Dimension(590, 620));
        panUserInfo.setLayout(new java.awt.GridBagLayout());

        lblEmployeeId.setText("Employee Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panUserInfo.add(lblEmployeeId, gridBagConstraints);

        txtEmployeeId.setEditable(false);
        txtEmployeeId.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panUserInfo.add(txtEmployeeId, gridBagConstraints);

        lblUserId.setText("User Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panUserInfo.add(lblUserId, gridBagConstraints);

        txtUserId.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panUserInfo.add(txtUserId, gridBagConstraints);

        lblPassword.setText("Password");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panUserInfo.add(lblPassword, gridBagConstraints);

        lblDisplayCustName.setText("Display Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panUserInfo.add(lblDisplayCustName, gridBagConstraints);

        lblEmailId.setText("Email Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panUserInfo.add(lblEmailId, gridBagConstraints);

        txtEmailId.setMinimumSize(new java.awt.Dimension(100, 21));
        txtEmailId.setValidation(new EmailValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panUserInfo.add(txtEmailId, gridBagConstraints);

        lblUserIdExpiresOn.setText("User Id Expires on");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panUserInfo.add(lblUserIdExpiresOn, gridBagConstraints);

        tdtUserIdExpiresOn.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtUserIdExpiresOnFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panUserInfo.add(tdtUserIdExpiresOn, gridBagConstraints);

        lblRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panUserInfo.add(lblRemarks, gridBagConstraints);

        txtRemarks.setMinimumSize(new java.awt.Dimension(150, 21));
        txtRemarks.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panUserInfo.add(txtRemarks, gridBagConstraints);

        pwdPassword.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panUserInfo.add(pwdPassword, gridBagConstraints);

        btnCustomerID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnCustomerID.setPreferredSize(new java.awt.Dimension(21, 21));
        btnCustomerID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustomerIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panUserInfo.add(btnCustomerID, gridBagConstraints);

        lblBranchID.setText("Branch ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panUserInfo.add(lblBranchID, gridBagConstraints);

        lblBranchName.setText("Branch Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panUserInfo.add(lblBranchName, gridBagConstraints);

        txtBranchID.setEditable(false);
        txtBranchID.setAllowAll(true);
        txtBranchID.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panUserInfo.add(txtBranchID, gridBagConstraints);

        txtBranchName.setEditable(false);
        txtBranchName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panUserInfo.add(txtBranchName, gridBagConstraints);

        lblGroupID.setText("Group ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panUserInfo.add(lblGroupID, gridBagConstraints);

        lblRoleID.setText("Role ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panUserInfo.add(lblRoleID, gridBagConstraints);

        cboRoleID.setMinimumSize(new java.awt.Dimension(100, 21));
        cboRoleID.setPopupWidth(200);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panUserInfo.add(cboRoleID, gridBagConstraints);

        cboGroupID.setMinimumSize(new java.awt.Dimension(100, 21));
        cboGroupID.setPopupWidth(200);
        cboGroupID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboGroupIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panUserInfo.add(cboGroupID, gridBagConstraints);

        tdtActivateUser.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtActivateUserFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panUserInfo.add(tdtActivateUser, gridBagConstraints);

        lblActivateUser.setText("User account Activate on");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panUserInfo.add(lblActivateUser, gridBagConstraints);

        lblEmployeeName.setText("Employee Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panUserInfo.add(lblEmployeeName, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panUserInfo.add(lblDisplayEmpName, gridBagConstraints);

        lblLastLoginDate.setText("Last Login Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panUserInfo.add(lblLastLoginDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panUserInfo.add(lblDisplayLastLoginDate, gridBagConstraints);

        lblForeignGroupId.setText("Foreign Group ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panUserInfo.add(lblForeignGroupId, gridBagConstraints);

        cboForeignGroupId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboForeignGroupId.setPopupWidth(200);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panUserInfo.add(cboForeignGroupId, gridBagConstraints);

        lblForeignBranchGroup.setText("Foreign Branch Group");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panUserInfo.add(lblForeignBranchGroup, gridBagConstraints);

        cboForeignBranchGroup.setMinimumSize(new java.awt.Dimension(100, 21));
        cboForeignBranchGroup.setPopupWidth(200);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panUserInfo.add(cboForeignBranchGroup, gridBagConstraints);

        lblAppraiserAllowed.setText("Appraiser Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panUserInfo.add(lblAppraiserAllowed, gridBagConstraints);

        chkAppraiserAllowed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkAppraiserAllowedActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panUserInfo.add(chkAppraiserAllowed, gridBagConstraints);

        lblCustomerId.setText("Customer ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panUserInfo.add(lblCustomerId, gridBagConstraints);

        txtCustomerId.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panUserInfo.add(txtCustomerId, gridBagConstraints);

        lblCustomerName.setText("Customer Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panUserInfo.add(lblCustomerName, gridBagConstraints);

        lblConfirmPassword.setText("Confirm Password");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panUserInfo.add(lblConfirmPassword, gridBagConstraints);

        pwdConfirmPassword.setMinimumSize(new java.awt.Dimension(100, 21));
        pwdConfirmPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pwdConfirmPasswordActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panUserInfo.add(pwdConfirmPassword, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panUserMain.add(panUserInfo, gridBagConstraints);

        panSettings.setBorder(javax.swing.BorderFactory.createTitledBorder("Settings"));
        panSettings.setMinimumSize(new java.awt.Dimension(240, 140));
        panSettings.setPreferredSize(new java.awt.Dimension(590, 620));
        panSettings.setLayout(new java.awt.GridBagLayout());

        chkSmartCard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSmartCardActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(20, 8, 0, 0);
        panSettings.add(chkSmartCard, gridBagConstraints);

        chkUserSuspend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkUserSuspendActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 0);
        panSettings.add(chkUserSuspend, gridBagConstraints);

        chkSuspend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSuspendActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 0);
        panSettings.add(chkSuspend, gridBagConstraints);

        lblUserSuspend.setText("User can suspend his/her account temporarily");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 37, 0, 0);
        panSettings.add(lblUserSuspend, gridBagConstraints);

        lblSmartCard.setText("Smart Card or Bio-metric login with a pin");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(21, 69, 0, 0);
        panSettings.add(lblSmartCard, gridBagConstraints);

        lblSuspend.setText("Suspend user temporarily");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 155, 0, 0);
        panSettings.add(lblSuspend, gridBagConstraints);

        lblSuspendFrom.setText("From");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 273, 0, 0);
        panSettings.add(lblSuspendFrom, gridBagConstraints);

        lblSuspendTo.setText("To");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 8, 0, 0);
        panSettings.add(lblSuspendTo, gridBagConstraints);

        lblReasonForSuspend.setText("Reason For Suspension");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 164, 0, 0);
        panSettings.add(lblReasonForSuspend, gridBagConstraints);

        txtReasonForSuspend.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 8, 0, 0);
        panSettings.add(txtReasonForSuspend, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 9, 0);
        panSettings.add(tdtSuspendFrom, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 9, 8);
        panSettings.add(tdtSuspendTo, gridBagConstraints);

        lblLockedStatus.setText("Locked Status");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(31, 47, 0, 0);
        panSettings.add(lblLockedStatus, gridBagConstraints);

        chkLockedStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkLockedStatusActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(31, 0, 0, 0);
        panSettings.add(chkLockedStatus, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        panUserMain.add(panSettings, gridBagConstraints);

        tabUser.addTab("User Details", panUserMain);

        panTerminal.setLayout(new java.awt.GridBagLayout());

        srpTerminal.setMinimumSize(new java.awt.Dimension(241, 205));
        srpTerminal.setPreferredSize(new java.awt.Dimension(241, 205));

        tblTerminal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblTerminal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTerminalMouseClicked(evt);
            }
        });
        srpTerminal.setViewportView(tblTerminal);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panTerminal.add(srpTerminal, gridBagConstraints);

        panTerminalButtons.setPreferredSize(new java.awt.Dimension(175, 46));
        panTerminalButtons.setLayout(new java.awt.GridBagLayout());

        btnAdd.setText("Add");
        btnAdd.setMaximumSize(new java.awt.Dimension(160, 40));
        btnAdd.setMinimumSize(new java.awt.Dimension(80, 26));
        btnAdd.setPreferredSize(new java.awt.Dimension(80, 28));
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        panTerminalButtons.add(btnAdd, new java.awt.GridBagConstraints());

        btnRemove.setText("Remove");
        btnRemove.setMaximumSize(new java.awt.Dimension(160, 40));
        btnRemove.setPreferredSize(new java.awt.Dimension(80, 28));
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActionPerformed(evt);
            }
        });
        panTerminalButtons.add(btnRemove, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        panTerminal.add(panTerminalButtons, gridBagConstraints);

        panTerminalDetails.setLayout(new java.awt.GridBagLayout());

        txtTerminalId.setEditable(false);
        txtTerminalId.setMinimumSize(new java.awt.Dimension(100, 21));
        panTerminalID.add(txtTerminalId);

        btnTerminalID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnTerminalID.setMinimumSize(new java.awt.Dimension(21, 21));
        btnTerminalID.setPreferredSize(new java.awt.Dimension(21, 21));
        btnTerminalID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTerminalIDActionPerformed(evt);
            }
        });
        panTerminalID.add(btnTerminalID);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panTerminalDetails.add(panTerminalID, gridBagConstraints);

        txtTerminalName.setEditable(false);
        txtTerminalName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTerminalDetails.add(txtTerminalName, gridBagConstraints);

        lblTerminalName.setText("Terminal Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTerminalDetails.add(lblTerminalName, gridBagConstraints);

        lblIpAddress.setText("IP Address");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTerminalDetails.add(lblIpAddress, gridBagConstraints);

        txtIPAddress.setEditable(false);
        txtIPAddress.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTerminalDetails.add(txtIPAddress, gridBagConstraints);

        txtMachineName.setEditable(false);
        txtMachineName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTerminalDetails.add(txtMachineName, gridBagConstraints);

        lblMachineName.setText("Machine Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTerminalDetails.add(lblMachineName, gridBagConstraints);

        lblTerminalDescription.setText("Terminal Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTerminalDetails.add(lblTerminalDescription, gridBagConstraints);

        txtTerminalDescription.setEditable(false);
        txtTerminalDescription.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTerminalDetails.add(txtTerminalDescription, gridBagConstraints);

        lblTerminalId.setText("Terminal Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTerminalDetails.add(lblTerminalId, gridBagConstraints);

        lblAccessFromDate.setText("Access From Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTerminalDetails.add(lblAccessFromDate, gridBagConstraints);

        lblAccessToDate.setText("Access To Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTerminalDetails.add(lblAccessToDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTerminalDetails.add(tdtAccessToDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTerminalDetails.add(tdtAccessFromDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        panTerminal.add(panTerminalDetails, gridBagConstraints);

        tabUser.addTab("Terminal Info", panTerminal);

        getContentPane().add(tabUser, java.awt.BorderLayout.CENTER);

        mnuProcess.setText("Process");

        mitNew.setText("New");
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);
        mnuProcess.add(sptDelete);

        mitSave.setText("Save");
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancel");
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess.add(mitCancel);
        mnuProcess.add(sptCancel);

        mitAuthorize.setText("Authorize");
        mitAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitAuthorizeActionPerformed(evt);
            }
        });
        mnuProcess.add(mitAuthorize);

        mitException.setText("Exception");
        mitException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitExceptionActionPerformed(evt);
            }
        });
        mnuProcess.add(mitException);

        mitReject.setText("Rejection");
        mitReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitRejectActionPerformed(evt);
            }
        });
        mnuProcess.add(mitReject);
        mnuProcess.add(sptException);

        mitPrint.setText("Print");
        mitPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitPrintActionPerformed(evt);
            }
        });
        mnuProcess.add(mitPrint);

        mitClose.setText("Close");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrMain.add(mnuProcess);

        setJMenuBar(mbrMain);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        viewType = VIEW;
        popUp();
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed

    private void tdtUserIdExpiresOnFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtUserIdExpiresOnFocusLost
        // TODO add your handling code here:
        ClientUtil.validateToDate(tdtUserIdExpiresOn, tdtActivateUser.getDateValue());
    }//GEN-LAST:event_tdtUserIdExpiresOnFocusLost

    /** gets the Date-1 in DD/MM/YYYY Mode as String 
     *
     */
    private String getCurrentDateinDDMMYYYYMinusOne() {

        java.util.Date dt = new java.util.Date();
        StringBuffer sbDt = new StringBuffer();
        sbDt.append(dt.getDate() - 1);
        sbDt.append("/");
        sbDt.append((dt.getMonth() + 1));
        sbDt.append("/");
        sbDt.append((dt.getYear() + 1900));
        return sbDt.toString();
    }
    private void tdtActivateUserFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtActivateUserFocusLost
        // TODO add your handling code here:
        ClientUtil.validateToDate(tdtActivateUser, DateUtil.getStringDate(DateUtil.addDays(currDt, -1)));
    }//GEN-LAST:event_tdtActivateUserFocusLost
    private void btnCheck() {
        btnCancel.setEnabled(true);
        btnSave.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnEdit.setEnabled(false);
    }

    /**
     * To addError Message for date field validations
     */
    private void addErrorMsg(String str) {
        if (strBuf == null) {
            strBuf = new StringBuffer();
        }
        strBuf.append(CommonUtil.convertObjToStr(resourceBundle.getString(str))).append("\n");
    }

    /**
     * validate date fields
     */
    private boolean validationForDateFields() {
        boolean value = false;
        if (validateDate(tdtUserIdExpiresOn, tdtActivateUser.getDateValue())) {
            addErrorMsg("UserIdExpired");
            value = true;
        }
        if (validateDate(tdtSuspendTo, tdtSuspendFrom.getDateValue())) {
            addErrorMsg("SuspendTo");
            value = true;
        }
        if (validateDate(tdtAccessToDate, tdtAccessFromDate.getDateValue())) {
            addErrorMsg("AccessTo");
            value = true;
        }
        if (validateDate(tdtSuspendFrom, tdtActivateUser.getDateValue())) {
            addErrorMsg("SuspendFromDate");
            value = true;
        }
        if (validateDate(tdtAccessFromDate, tdtActivateUser.getDateValue())) {
            addErrorMsg("AccessFromDate");
            value = true;
        }
        if (validateDate(tdtUserIdExpiresOn, tdtAccessToDate.getDateValue())) {
            addErrorMsg("AccessToDate");
            value = true;
        }
        if (validateDate(tdtUserIdExpiresOn, tdtSuspendTo.getDateValue())) {
            addErrorMsg("SuspendToDate");
            value = true;
        }
        return value;
    }
    
    boolean chekForUnauthorizedTrans(String userId) {
        boolean unauthorizedTrans = false;
        HashMap where = new HashMap();
        where.put("TRANS_DT", currDt.clone());
        where.put("STATUS_BY", userId);
        List list = (List) ClientUtil.executeQuery("checkUnauthorizedTransByUser", where);
        if (list != null && list.size() > 0) {
            where = (HashMap) list.get(0);
            int transCount = CommonUtil.convertObjToInt(where.get("TRNS_RECORDS"));
            if (transCount > 0) {
                unauthorizedTrans = true;
            }
        }
        return unauthorizedTrans;
    }
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:       
        setModified(false);
        if(viewType == EDIT){
            String oldBranchId = observable.getTxtBranchID();
            String changedBranchId = txtBranchID.getText();
            if(!(oldBranchId.equalsIgnoreCase(changedBranchId)) && txtBranchID.getText().length() > 0){
                boolean unauthorizedTrans = chekForUnauthorizedTrans(txtUserId.getText());
                if(unauthorizedTrans){
                    displayAlert("Unauthorized transaction exists for this user.\n Cannot change branch code. \nPlease Check !");
                    return;
                }
            }
        }
        String mandatoryMessage = checkMandatory(panUserMain);
        mandatoryMessage += checkMandatory(panTerminalDetails);
        if (chkSuspend.isSelected() && (tdtSuspendFrom.getDateValue().length() <= 0 || tdtSuspendTo.getDateValue().length() <= 0)) {
            StringBuffer stbMsg = new StringBuffer("Enter Suspended From Date and To Date");
            mandatoryMessage += CommonUtil.convertObjToStr(stbMsg);
        }            
        HashMap whereBranchCode = new HashMap();
        whereBranchCode.put("BRANCHCODE", TrueTransactMain.BRANCH_ID);
        java.util.List listOpenDate = (java.util.List) ClientUtil.executeQuery("getSelectBranchMasterTO", whereBranchCode);
        com.see.truetransact.transferobject.sysadmin.branch.BranchMasterTO branchMasterToOpenDate = (com.see.truetransact.transferobject.sysadmin.branch.BranchMasterTO) listOpenDate.get(0);
        java.util.Date openingDate = branchMasterToOpenDate.getOpeningDt();
        whereBranchCode = null;
        listOpenDate = null;
        branchMasterToOpenDate = null;
        //--- check for User activation dt >= Branch activation dt
        if (openingDate.after(DateUtil.getDateMMDDYYYY(tdtActivateUser.getDateValue()))) {
            StringBuffer stbMsgOpeningDate = new StringBuffer(resourceBundle.getString("userActivationDateValidation"));
            mandatoryMessage += CommonUtil.convertObjToStr(stbMsgOpeningDate);
            stbMsgOpeningDate = null;
        }

        if (mandatoryMessage.length() > 0) {
            displayAlert(mandatoryMessage);
        } else if (!passWordConfirmation()) {
            JOptionPane.showMessageDialog(this, "Both passwords should be same");
            this.pwdConfirmPassword.setText("");
        } else if (validationForDateFields()) {
            displayAlert(CommonUtil.convertObjToStr(strBuf));
            strBuf = null;
            // Date fields are not valid
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW && observable.userIdUniqueness(txtUserId.getText())) {
            displayAlert(resourceBundle.getString("UserIdUniqueness"));
        } else {
            updateOBFields();
            observable.doAction();
            super.removeEditLock(txtUserId.getText());
            observable.resetForm();
            txtEmployeeId.setText("");
            chkLockedStatus.setEnabled(false);
            setUp(ClientConstants.ACTIONTYPE_CANCEL, false);
            enableDisableSuspendFields(false);
            txtUserId.setEditable(false);
            observable.setResultStatus();
        }
        // IF condition added by Jeffin John on 27-05-2014 for Mantis ID - 9168
        
        if (statusModified != null && statusModified.equals("MODIFIED") && chkLockedStatus.isSelected() == false) {
            HashMap updateMap = new HashMap();
            updateMap.put("USER_ID", userID);
            updateMap.put("LOGIN_STATUS", "LOGOUT");
            updateMap.put("LAST_LOGOUT_DT", currDt.clone());
            updateMap.put("SUSPEND_USER", "N");
            ClientUtil.execute("updateUserLogoutStatus", updateMap);
            ClientUtil.execute("updateSuspendUser", updateMap);
            ClientUtil.execute("updateUserLoginHistory", updateMap);
        }
        //End of updation by Jeffin John
    }//GEN-LAST:event_btnSaveActionPerformed
    private void chkSuspendAction() {
        if (chkSuspend.isSelected() == true) {
            enableDisableSuspendFields(true);
        } else {
            enableDisableSuspendFields(false);
            this.txtReasonForSuspend.setText("");
            this.tdtSuspendFrom.setDateValue("");
            this.tdtSuspendTo.setDateValue("");
        }
    }
    private void chkSuspendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSuspendActionPerformed
        // TODO add your handling code here:
        chkSuspendAction();
    }//GEN-LAST:event_chkSuspendActionPerformed

    private void mitPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitPrintActionPerformed
        // TODO add your handling code here:
        btnPrintActionPerformed(evt);
    }//GEN-LAST:event_mitPrintActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
        HashMap reportParamMap = new HashMap();
        com.see.truetransact.clientutil.ttrintegration.LinkReport.getReports(getScreenID(), reportParamMap);
    }//GEN-LAST:event_btnPrintActionPerformed

    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // TODO add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed

    private void mitRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitRejectActionPerformed
        // TODO add your handling code here:
        btnRejectActionPerformed(evt);
    }//GEN-LAST:event_mitRejectActionPerformed

    private void mitExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitExceptionActionPerformed
        // TODO add your handling code here:
        btnExceptionActionPerformed(evt);
    }//GEN-LAST:event_mitExceptionActionPerformed

    private void mitAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitAuthorizeActionPerformed
        // TODO add your handling code here:
        btnAuthorizeActionPerformed(evt);
    }//GEN-LAST:event_mitAuthorizeActionPerformed

    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // TODO add your handling code here:
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed

    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // TODO add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed

    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // TODO add your handling code here:
        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed

    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // TODO add your handling code here:
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed

    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // TODO add your handling code here:
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        //        viewType = AUTHORIZE;
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
        btnCancel.setEnabled(true);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
        //        viewType = AUTHORIZE;
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
        btnCancel.setEnabled(true);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        //        viewType = AUTHORIZE;
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnCancel.setEnabled(true);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);

    }//GEN-LAST:event_btnAuthorizeActionPerformed

    private void pwdConfirmPasswordFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pwdConfirmPasswordFocusLost
        // Add your handling code here:
        char[] pwd1 = pwdPassword.getPassword();
        char[] pwd2 = pwdConfirmPassword.getPassword();
        if (pwd1.length == pwd2.length) {
            int i;
            for (i = 0; i < pwd1.length; i++) {
                if (pwd1[i] != pwd2[i]) {
                    break;
                }
            }
            if (i == pwd1.length) {
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Both passwords should be same");
        this.pwdConfirmPassword.setText("");
        this.pwdConfirmPassword.requestFocus();
    }//GEN-LAST:event_pwdConfirmPasswordFocusLost

    private void tblTerminalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTerminalMouseClicked
        // Add your handling code here:
        if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE) {
            btnRemove.setEnabled(true);
            ArrayList arrRow = observable.populateTerminals(tblTerminal.getSelectedRow());
            txtTerminalId.setText((String) arrRow.get(1));
            tdtAccessFromDate.setDateValue((String) arrRow.get(2));
            tdtAccessToDate.setDateValue((String) arrRow.get(3));
            if ((txtTerminalId.getText().length() > 0) || (txtTerminalId.getText() != null)) {
                updateOBFields();
                observable.populateTerminalTO(txtTerminalId.getText());
            }
        }
    }//GEN-LAST:event_tblTerminalMouseClicked

    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActionPerformed
        // Add your handling code here:
        observable.deleteTerminalData(tblTerminal.getSelectedRow());
        populateTerminalTable();
        ClientUtil.clearAll(panTerminalDetails);
    }//GEN-LAST:event_btnRemoveActionPerformed
    private void populateTerminalTable() {
        tblTerminal.setModel(observable.getTmlTerminals());
        tblTerminal.revalidate();
    }
    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // Add your handling code here:
        boolean value = this.validationForDateFields();
        if (!value) {
            if (txtTerminalId.getText().length() > 0 && txtUserId.getText().length() > 0) {
                ArrayList irRow = new ArrayList();
                irRow.add(txtUserId.getText());
                irRow.add(txtTerminalId.getText());
                irRow.add(tdtAccessFromDate.getDateValue());
                irRow.add(tdtAccessToDate.getDateValue());

                observable.insertTerminalData(irRow);
                populateTerminalTable();
                ClientUtil.clearAll(panTerminalDetails);
            } else {
                if (txtTerminalId.getText().length() == 0) {
                    JOptionPane.showMessageDialog(this, "TerminalID cannot be empty");
                } else {
                    JOptionPane.showMessageDialog(this, "UserID cannot be empty");
                }
            }
        } else {
            displayAlert(CommonUtil.convertObjToStr(strBuf));
        }
        strBuf = null;
    }//GEN-LAST:event_btnAddActionPerformed
                        private void cboGroupIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboGroupIDActionPerformed
                            // Add your handling code here:
                            ComboBoxModel cboModel = (ComboBoxModel) cboGroupID.getModel();
                            if (cboModel != null) {
                                String grpID = (String) cboModel.getKeyForSelected();
                                if (grpID != null && !grpID.equals("")) {
                                    System.out.println(grpID);
                                    observable.populateRoleID(grpID);
                                    cboRoleID.setModel(observable.getCbmRoleID());
                                }
                            }
    }//GEN-LAST:event_cboGroupIDActionPerformed
                                                                                    private void btnTerminalIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTerminalIDActionPerformed
                                                                                        // Add your handling code here:
                                                                                        final HashMap testMap = new HashMap();
                                                                                        testMap.put(CommonConstants.MAP_NAME, "getAllTerminalMasterTO");
                                                                                        viewType = TERMINAL_ID;
                                                                                        ClientUtil.clearAll(panTerminalDetails);
                                                                                        new ViewAll(this, testMap).show();
    }//GEN-LAST:event_btnTerminalIDActionPerformed
                                                                                                                                                                                                                                    private void btnCustomerIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustomerIDActionPerformed
                                                                                                                                                                                                                                        viewType = EMPLOYEE_ID;
                                                                                                                                                                                                                                        new CheckCustomerIdUI(this);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                
    }                private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-LAST:event_btnCustomerIDActionPerformed
                                                            // Add your handling code here://GEN-FIRST:event_btnCloseActionPerformed


            cifClosingAlert();

            btnCancelActionPerformed(null);
//        this.dispose();
    }                                        private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-LAST:event_btnCloseActionPerformed
                                                                                    // Add your handling code here://GEN-FIRST:event_btnCancelActionPerformed
            setModified(false);
            setUp(ClientConstants.ACTIONTYPE_CANCEL, false);
            enableDisableSuspendFields(false);
            txtUserId.setEditable(false);
            chkLockedStatus.setSelected(false);
            chkLockedStatus.setEnabled(false);
            super.removeEditLock(txtUserId.getText());
            viewType = -1;
            observable.resetForm();
    }//GEN-LAST:event_btnCancelActionPerformed
                                                                private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
                                                                    // Add your handling code here:
                                                                    //                                                setUp(ClientConstants.ACTIONTYPE_DELETE,false);
                                                                    if (viewType == DELETE && isFilled == false) {
                                                                        view = "DELETE";
                                                                        isFilled = true;
                                                                        observable.doAction();
                                                                        lblStatus.setText("Deleted");
                                                                        observable.resetForm();
                                                                    } else {
                                                                        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
                                                                        viewType = DELETE;
                                                                        popUp();
                                                                        isFilled = false;
                                                                        btnDelete.setEnabled(true);
                                                                        lblStatus.setText("Delete");
                                                                    }             
    }//GEN-LAST:event_btnDeleteActionPerformed
    private String checkMandatory(JComponent component) {
        return new MandatoryCheck().checkMandatory(getClass().getName(), component);
    }

    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        setModified(true);
        setUp(ClientConstants.ACTIONTYPE_NEW, true);
        txtUserId.setEditable(true);
        enableDisableSuspendFields(false);
        tdtActivateUser.setDateValue(DateUtil.getStringDate((Date) currDt.clone()));
        tdtUserIdExpiresOn.setEnabled(true);
         txtCustomerId.setEnabled(false);
         txtCustomerId.disable();
    }//GEN-LAST:event_btnNewActionPerformed
    private void setUp(int actionType, boolean isEnable) {
        actionAuthExcepReject = false;
        //ClientUtil.enableDisable(tabUser,isEnable);
        ClientUtil.enableDisable(this, isEnable);
        setButtonEnableDisable();
        enableDisableButtons(isEnable);
        observable.setActionType(actionType);
        observable.setStatus();
    }

    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        this.dispose();
    }//GEN-LAST:event_exitForm

private void chkLockedStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkLockedStatusActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_chkLockedStatusActionPerformed

private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
    // Add your handling code here:                                                                                                                                                                                                                                                                                                   
    viewType = EDIT;                                                                                                                                                                                                                                                                                                                                        
    popUp();  
    
    //Part of code added by Jeffin John on 27-05-2014 for Mantis ID - 9168
    
    String selectedUserID = txtUserId.getText();
    userID = selectedUserID;
    HashMap suspendMap = new HashMap();
    suspendMap.put("USER_ID",selectedUserID);
    List suspendList = new ArrayList();
    String status = null;
    String logStatus = null;
    suspendList = ClientUtil.executeQuery("getSuspendStatus", suspendMap);
    if(suspendList!=null && suspendList.size() > 0){
        HashMap userMap = (HashMap)suspendList.get(0);
        if(userMap!=null && userMap.containsKey("SUSPEND_USER")){
            status = CommonUtil.convertObjToStr(userMap.get("SUSPEND_USER"));
        }
    }
    suspendList = ClientUtil.executeQuery("getLoggedinUser", suspendMap);
    if(suspendList!=null && suspendList.size() > 0){
        HashMap userMap = (HashMap)suspendList.get(0);
        if(userMap!=null && userMap.containsKey("LOGIN_STATUS")){
            logStatus = CommonUtil.convertObjToStr(userMap.get("LOGIN_STATUS"));
        }
    }
    if (CommonUtil.convertObjToStr(TrueTransactMain.USER_ID).equals(observable.getTxtUserId())) {
        lblPassword.setVisible(true);
        lblDisplayCustName.setVisible(true);
        pwdPassword.setVisible(true);
        pwdConfirmPassword.setVisible(true);
    } else {
        lblPassword.setVisible(false);
        lblDisplayCustName.setVisible(false);
        pwdPassword.setVisible(false);
        pwdConfirmPassword.setVisible(false);
    }
    statusModified="";
    if(status != null && !status.equals("")){
        if (status.equals("Y")) {
            statusModified = "MODIFIED";
            chkLockedStatus.setEnabled(true);
            chkLockedStatus.setSelected(true);
        }
    }
    if (logStatus != null && !logStatus.equals("")){
        if (logStatus.equals("LOGIN")) {
            statusModified = "MODIFIED";
            chkLockedStatus.setEnabled(true);
            chkLockedStatus.setSelected(true);
        }
    }
}//GEN-LAST:event_btnEditActionPerformed

    private void chkAppraiserAllowedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkAppraiserAllowedActionPerformed
        // TODO add your handling code here:
//        if(chkAppraiserAllowed.isSelected()){
//            observable.setChkAppraiserAllowed(true);
//        }else{
//            observable.setChkAppraiserAllowed(false);
//        }
//            
    }//GEN-LAST:event_chkAppraiserAllowedActionPerformed

    private void chkSmartCardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSmartCardActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkSmartCardActionPerformed

    private void chkUserSuspendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkUserSuspendActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkUserSuspendActionPerformed

    private void pwdConfirmPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pwdConfirmPasswordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pwdConfirmPasswordActionPerformed
    /** To display a popUp window for viewing existing data as well as to select new
     * customer for entry
     */
    private void popUp() {
        final HashMap testMap = new HashMap();
        ArrayList lst = new ArrayList();
        lst.add("USER ID");
        testMap.put(ClientConstants.RECORD_KEY_COL, lst);
        lst = null;
        testMap.put(CommonConstants.MAP_NAME, "getAllSelectUserTO");
        isFilled = false;
        new ViewAll(this, testMap).show();
    }

    /** Called by the Popup window created thru popUp method */
    public void fillData(Object obj) {
        if (viewType == EDIT) {
            setUp(ClientConstants.ACTIONTYPE_EDIT, true);
            txtUserId.setEditable(false);
            chkSuspendAction();
        } else if (viewType == DELETE || viewType == VIEW) {
            setUp(ClientConstants.ACTIONTYPE_DELETE, false);
            enableDisableSuspendFields(false);
            txtUserId.setEditable(false);
        }
        HashMap data = (HashMap) obj;
        if (viewType != -1) {
            updateOBFields();
            if (viewType == EMPLOYEE_ID) {  //added by anjuanand for Mantis ID: 9606
                try {
                        final String CUSTID = CommonUtil.convertObjToStr(data.get("CUST_ID"));
                        txtCustomerId.setText(CUSTID);
                        lblDisplayCustName.setText(CommonUtil.convertObjToStr(data.get("NAME")));
			            HashMap result = new HashMap();
                        result = observable.getBranchDet(data);
                        txtBranchID.setText(CommonUtil.convertObjToStr(result.get("branchCode")));
                        txtBranchName.setText(CommonUtil.convertObjToStr(result.get("branchName")));
                    
    //                txtEmployeeId.setText(CommonUtil.convertObjToStr(data.get("EMPLOYEECODE")));
    //                txtEmailId.setText(CommonUtil.convertObjToStr(data.get("OFFICIALEMAIL")));
    //                txtBranchID.setText(CommonUtil.convertObjToStr(data.get("BRANCHCODE")));
    //                txtBranchName.setText(CommonUtil.convertObjToStr(data.get("BRANCHNAME")));
    //                observable.setLblEmployeeName(CommonUtil.convertObjToStr(data.get("EMPLOYEENAME")));
    //                tdtUserIdExpiresOn.setDateValue(CommonUtil.convertObjToStr(data.get("DOL")));
    //                tdtUserIdExpiresOn.setDateValue(CommonUtil.convertObjToStr(data.get("DOL")));
                } catch (SQLException ex) {
                    Logger.getLogger(UserUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (viewType == TERMINAL_ID) {
                observable.populateTerminalTO((String) data.get("TERMINALID"));
            } else if (viewType == EDIT || viewType == DELETE || viewType == AUTHORIZE || viewType == VIEW) {
                isFilled = true;
                observable.getData(data);
                lblDisplayCustName.setText(observable.getCustomerName(CommonUtil.convertObjToStr(txtCustomerId.getText())));
                txtUserId.setEditable(false);
                if (viewType == EDIT) {
                    chkSuspendAction();
                }
            }
        }
        if (viewType == AUTHORIZE) {
            btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
            btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
            btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
        }
        tdtUserIdExpiresOn.setEnabled(true);
    }

    public boolean validateDate(CDateField tdtObj, String strMMDDYYY) {
        boolean valid = false;
        if (tdtObj.getDateValue() != null && !tdtObj.getDateValue().equals("") && strMMDDYYY != null && !strMMDDYYY.equals("")) {
            ToDateValidation toDate = new ToDateValidation(DateUtil.getDateMMDDYYYY(strMMDDYYY));
            toDate.setComponent(tdtObj);
            if (!toDate.validate()) {
                valid = true;
                // tdtObj.setDateValue("");

            }
        }
        return valid;
    }

    /** Method for AUTHORIZE, EXCEPTION and REJECTION */
    public void authorizeStatus(String authorizeStatus) {
        actionAuthExcepReject = true;
        if (viewType == AUTHORIZE && isFilled) {
            System.out.println(" here in the auth 1 ");
            final HashMap tamMaintenanceCreateMap = new HashMap();
            tamMaintenanceCreateMap.put("USER_ID", TrueTransactMain.USER_ID);
            tamMaintenanceCreateMap.put("STATUS", authorizeStatus);
            tamMaintenanceCreateMap.put("USER ID", observable.getTxtUserId());
            tamMaintenanceCreateMap.put("CURR_DATE", currDt.clone());
            ClientUtil.execute("authorizeUser", tamMaintenanceCreateMap);
            observable.setResult(observable.getActionType());          
             super.removeEditLock(txtUserId.getText());
            observable.resetForm();
            //            observable.resetDrawingPowerFields();
            //            observable.resetDrawingPowerDetailsFields();

            setButtonEnableDisable();
            ClientUtil.enableDisable(this, false);
            enableDisableSuspendFields(false);
            txtUserId.setEditable(false);
            observable.setResultStatus();
            viewType = -1;         
            //            observable.removeTableRow();

        } else {
            System.out.println(" here in the auth 2 ");
            final HashMap mapParam = new HashMap();
            mapParam.put(CommonConstants.MAP_NAME, "getUserAuthorizeList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeUser");
            viewType = AUTHORIZE;
            lblStatus.setText(ClientConstants.ACTION_STATUS[0]);
            isFilled = false;
            final AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            btnSaveDisable();
//            setAuthBtnEnableDisable();
        }
    }

    private void btnSaveDisable() {
        btnSave.setEnabled(false);
        mitSave.setEnabled(false);
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
        setAuthBtnEnableDisable();
    }

    /** To Enable or Disable Authorize, Rejection and Exception Button */
    private void setAuthBtnEnableDisable() {
        final boolean enableDisable = !btnSave.isEnabled();
        btnAuthorize.setEnabled(enableDisable);
        btnException.setEnabled(enableDisable);
        btnReject.setEnabled(enableDisable);
        mitAuthorize.setEnabled(enableDisable);
        mitException.setEnabled(enableDisable);
        mitReject.setEnabled(enableDisable);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAdd;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnCustomerID;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnRemove;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnTerminalID;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboForeignBranchGroup;
    private com.see.truetransact.uicomponent.CComboBox cboForeignGroupId;
    private com.see.truetransact.uicomponent.CComboBox cboGroupID;
    private com.see.truetransact.uicomponent.CComboBox cboRoleID;
    private com.see.truetransact.uicomponent.CCheckBox chkAppraiserAllowed;
    private com.see.truetransact.uicomponent.CCheckBox chkLockedStatus;
    private com.see.truetransact.uicomponent.CCheckBox chkSmartCard;
    private com.see.truetransact.uicomponent.CCheckBox chkSuspend;
    private com.see.truetransact.uicomponent.CCheckBox chkUserSuspend;
    private com.see.truetransact.uicomponent.CLabel lblAccessFromDate;
    private com.see.truetransact.uicomponent.CLabel lblAccessToDate;
    private com.see.truetransact.uicomponent.CLabel lblActivateUser;
    private com.see.truetransact.uicomponent.CLabel lblAppraiserAllowed;
    private com.see.truetransact.uicomponent.CLabel lblBranchID;
    private com.see.truetransact.uicomponent.CLabel lblBranchName;
    private com.see.truetransact.uicomponent.CLabel lblConfirmPassword;
    private com.see.truetransact.uicomponent.CLabel lblCustomerId;
    private com.see.truetransact.uicomponent.CLabel lblCustomerName;
    private com.see.truetransact.uicomponent.CLabel lblDisplayCustName;
    private com.see.truetransact.uicomponent.CLabel lblDisplayEmpName;
    private com.see.truetransact.uicomponent.CLabel lblDisplayLastLoginDate;
    private com.see.truetransact.uicomponent.CLabel lblEmailId;
    private com.see.truetransact.uicomponent.CLabel lblEmployeeId;
    private com.see.truetransact.uicomponent.CLabel lblEmployeeName;
    private com.see.truetransact.uicomponent.CLabel lblForeignBranchGroup;
    private com.see.truetransact.uicomponent.CLabel lblForeignGroupId;
    private com.see.truetransact.uicomponent.CLabel lblGroupID;
    private com.see.truetransact.uicomponent.CLabel lblIpAddress;
    private com.see.truetransact.uicomponent.CLabel lblLastLoginDate;
    private com.see.truetransact.uicomponent.CLabel lblLockedStatus;
    private com.see.truetransact.uicomponent.CLabel lblMachineName;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblPassword;
    private com.see.truetransact.uicomponent.CLabel lblReasonForSuspend;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
    private com.see.truetransact.uicomponent.CLabel lblRoleID;
    private com.see.truetransact.uicomponent.CLabel lblSmartCard;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
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
    private com.see.truetransact.uicomponent.CLabel lblSuspend;
    private com.see.truetransact.uicomponent.CLabel lblSuspendFrom;
    private com.see.truetransact.uicomponent.CLabel lblSuspendTo;
    private com.see.truetransact.uicomponent.CLabel lblTerminalDescription;
    private com.see.truetransact.uicomponent.CLabel lblTerminalId;
    private com.see.truetransact.uicomponent.CLabel lblTerminalName;
    private com.see.truetransact.uicomponent.CLabel lblUserId;
    private com.see.truetransact.uicomponent.CLabel lblUserIdExpiresOn;
    private com.see.truetransact.uicomponent.CLabel lblUserSuspend;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitAuthorize;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitException;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitReject;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panSettings;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTerminal;
    private com.see.truetransact.uicomponent.CPanel panTerminalButtons;
    private com.see.truetransact.uicomponent.CPanel panTerminalDetails;
    private com.see.truetransact.uicomponent.CPanel panTerminalID;
    private com.see.truetransact.uicomponent.CPanel panUserInfo;
    private com.see.truetransact.uicomponent.CPanel panUserMain;
    private com.see.truetransact.uicomponent.CPasswordField pwdConfirmPassword;
    private com.see.truetransact.uicomponent.CPasswordField pwdPassword;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private javax.swing.JSeparator sptException;
    private com.see.truetransact.uicomponent.CScrollPane srpTerminal;
    private com.see.truetransact.uicomponent.CTabbedPane tabUser;
    private com.see.truetransact.uicomponent.CTable tblTerminal;
    private javax.swing.JToolBar tbrOperativeAcctProduct;
    private com.see.truetransact.uicomponent.CDateField tdtAccessFromDate;
    private com.see.truetransact.uicomponent.CDateField tdtAccessToDate;
    private com.see.truetransact.uicomponent.CDateField tdtActivateUser;
    private com.see.truetransact.uicomponent.CDateField tdtSuspendFrom;
    private com.see.truetransact.uicomponent.CDateField tdtSuspendTo;
    private com.see.truetransact.uicomponent.CDateField tdtUserIdExpiresOn;
    private com.see.truetransact.uicomponent.CTextField txtBranchID;
    private com.see.truetransact.uicomponent.CTextField txtBranchName;
    private com.see.truetransact.uicomponent.CPasswordField txtCustomerId;
    private com.see.truetransact.uicomponent.CTextField txtEmailId;
    private com.see.truetransact.uicomponent.CTextField txtEmployeeId;
    private com.see.truetransact.uicomponent.CTextField txtIPAddress;
    private com.see.truetransact.uicomponent.CTextField txtMachineName;
    private com.see.truetransact.uicomponent.CTextField txtReasonForSuspend;
    private com.see.truetransact.uicomponent.CTextField txtRemarks;
    private com.see.truetransact.uicomponent.CTextField txtTerminalDescription;
    private com.see.truetransact.uicomponent.CTextField txtTerminalId;
    private com.see.truetransact.uicomponent.CTextField txtTerminalName;
    private com.see.truetransact.uicomponent.CTextField txtUserId;
    // End of variables declaration//GEN-END:variables

    public static void main(String[] arg) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Throwable th) {
            th.printStackTrace();
        }
        JFrame jf = new JFrame();
        UserUI gui = new UserUI();
        jf.getContentPane().add(gui);
        jf.setSize(550, 660);
        jf.show();
        gui.show();
    }
}
