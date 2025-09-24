/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 * DeathMarkingUI.java
 *
 * Created on May 26, 2004, 4:35 PM
 */

package com.see.truetransact.ui.deposit.deathmarking;

/**
 *
 * @author Ashok
 */

import java.util.Observer;
import java.util.Observable;
import java.util.HashMap;
import java.util.Date;
import java.util.ArrayList;

import com.see.truetransact.ui.deposit.deathmarking.DeathMarkingRB;
import com.see.truetransact.ui.deposit.deathmarking.DeathMarkingOB;
import com.see.truetransact.ui.deposit.deathmarking.DeathMarkingMRB;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.ui.TrueTransactMain;

public class DeathMarkingUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer,UIMandatoryField{
    
    private DeathMarkingRB resourceBundle = new DeathMarkingRB();
    private HashMap mandatoryMap;
    private DeathMarkingOB observable;
    private DeathMarkingMRB objMandatoryRB;
    private String viewType= "";
    final String AUTHORIZE="Authorize";
    private boolean isFilled = false;
    private Date curDate = null;
    
    /** Creates new form DeathMarkingUI */
    public DeathMarkingUI() {
        curDate = ClientUtil.getCurrentDate();
        initComponents();
        initUIComponents();
    }
    
    /** Initialise the ui components */
    private void initUIComponents(){
        setFieldNames();
        internationalize();
        setObservable();
        setMandatoryHashMap();
        setHelpMessage();
        setMaximumLength();
        initComponentData();
        observable.resetForm();
        ClientUtil.enableDisable(panMain, false);
        setButtonEnableDisable();
       }
    
    /** Sets the names to each Uicomponents */
    private void setFieldNames() {
        btnNew.setName("btnNew");
        btnDelete.setName("btnDelete");
        btnCancel.setName("btnCancel");
        btnAuthorize.setName("btnAuthorize");
        btnReject.setName("btnReject");
        btnException.setName("btnException");
        btnClose.setName("btnClose");
        btnEdit.setName("btnEdit");
        btnPrint.setName("btnPrint");
        btnSave.setName("btnSave");
        txtCustomerId.setName("txtCustomerId");
        btnCustomerId.setName("btnCustomerId");
        tdtDtOfDeath.setName("tdtDtOfDeath");
        tdtReportedOn.setName("tdtReportedOn");
        lblDateOfDeath.setName("lblDateOfDeath");
        lblCustomerId.setName("lblCustomerId");
        lblMsg.setName("lblMsg");
        lblCustomerName.setName("lblCustomerName");
        lblReferenceNo.setName("lblReferenceNo");
        lblRelationship.setName("lblRelationship");
        lblRemarks.setName("lblRemarks");
        lblReportedBy.setName("lblReportedBy");
        lblReportedOn.setName("lblReportedOn");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblStatus.setName("lblStatus");
        mbrDeathMarking.setName("mbrDeathMarking");
        panDetails.setName("panDetails");
        panId.setName("panId");
        panMain.setName("panMain");
        panStatus.setName("panStatus");
        srpDeathMarking.setName("srpDeathMarking");
        tblDeathMarking.setName("tblDeathMarking");
        txtReferenceNo.setName("txtReferenceNo");
        cboRelationship.setName("cboRelationship");
        txtRemarks.setName("txtRemarks");
        txtReportedBy.setName("txtReportedBy");
    }
    
    /* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        btnNew.setText(resourceBundle.getString("btnNew"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        btnException.setText(resourceBundle.getString("btnException"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        btnCustomerId.setText(resourceBundle.getString("btnCustomerId"));
        lblCustomerName.setText(resourceBundle.getString("lblCustomerName"));
        lblDateOfDeath.setText(resourceBundle.getString("lblDateOfDeath"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblReportedOn.setText(resourceBundle.getString("lblReportedOn"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblRelationship.setText(resourceBundle.getString("lblRelationship"));
        lblCustomerId.setText(resourceBundle.getString("lblCustomerId"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblReportedBy.setText(resourceBundle.getString("lblReportedBy"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblReferenceNo.setText(resourceBundle.getString("lblReferenceNo"));
        lblRemarks.setText(resourceBundle.getString("lblRemarks"));
    }
    
    
    /* Auto Generated Method - setMandatoryHashMap()
      This method list out all the Input Fields available in the UI.
      It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtCustomerId", new Boolean(true));
        mandatoryMap.put("tdtDtOfDeath", new Boolean(true));
        mandatoryMap.put("tdtReportedOn", new Boolean(true));
        mandatoryMap.put("txtReportedBy", new Boolean(true));
        mandatoryMap.put("cboRelationship", new Boolean(true));
        mandatoryMap.put("txtReferenceNo", new Boolean(true));
        mandatoryMap.put("txtRemarks", new Boolean(false));
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
        objMandatoryRB = new DeathMarkingMRB();
        txtCustomerId.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCustomerId"));
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
        tdtDtOfDeath.setDateValue(observable.getDtdDtOfDeath());
        txtCustomerId.setText(observable.getTxtCustomerId());
        tdtReportedOn.setDateValue(observable.getDtdReportedOn());
        txtReportedBy.setText(observable.getTxtReportedBy());
        cboRelationship.setSelectedItem(observable.getCboRelationShip());
        txtReferenceNo.setText(observable.getTxtReferenceNo());
        txtRemarks.setText(observable.getTxtRemarks());
        tblDeathMarking.setModel(observable.getTblDeathMarkingModel());
    }
    
    
    /* Auto Generated Method - updateOBFields()
     This method called by Save option of UI.
     It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setTxtCustomerId((String) txtCustomerId.getText());
        observable.setDtdDtOfDeath(tdtDtOfDeath.getDateValue());
        observable.setDtdReportedOn(tdtReportedOn.getDateValue());
        observable.setTxtReportedBy(txtReportedBy.getText());
        observable.setCboRelationShip((String)cboRelationship.getSelectedItem());
        observable.setTxtReferenceNo(txtReferenceNo.getText());
        observable.setTxtRemarks(txtRemarks.getText());
        
        observable.setModule(getModule());
        observable.setScreen(getScreen());
    }
    
    /** Sets the textfields to a maximumlength */
    private void setMaximumLength(){
        txtReportedBy.setMaxLength(64);
        txtReferenceNo.setMaxLength(32);
        txtRemarks.setMaxLength(128);
    }
    
    /** Sets the datamodel for the Combobox */
    private void initComponentData(){
        cboRelationship.setModel(observable.getCbmRelationship());
    }
    
    /** Adds this ui as a observer to a observable class */
    private void setObservable() {
        observable = DeathMarkingOB.getInstance();
        observable.addObserver(this);
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
            btnCustomerId.setEnabled(btnNew.isEnabled());
        }else  if(observable.getActionType()== ClientConstants.ACTIONTYPE_EDIT){
            btnCustomerId.setEnabled(!btnNew.isEnabled());
        }else {
            btnCustomerId.setEnabled(!btnNew.isEnabled());
        }
        if(observable.getLblStatus().equals(ClientConstants.RESULT_STATUS[2]) || observable.getLblStatus().equals(ClientConstants.RESULT_STATUS[3]) || observable.getLblStatus().equals(ClientConstants.RESULT_STATUS[1])){
            btnCustomerId.setEnabled(!btnClose.isEnabled());
        }
    }
    
    /* Does necessary operaion when user clicks the save button */
    private void savePerformed(){
        updateOBFields();
        observable.doAction();
        ClientUtil.clearAll(this);
        isFilled = false;
        observable.resetForm();
        ClientUtil.enableDisable(panMain, false);
        setButtonEnableDisable();
        clearLables();
        observable.setResultStatus();
    }
  
    /* set the screen after the updation,insertion, deletion */
    private void settings(){
        observable.resetForm();
        ClientUtil.clearAll(this);
        isFilled = false;
        ClientUtil.enableDisable(panMain, false);
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
    
    /** This method is called up whenever editbuton,deletebutton or the helpbutton is clicked */
    private void callView(String currField) {
        viewType = currField;
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        if (currField.equals(ClientConstants.ACTION_STATUS[2])  ||
        currField.equals(ClientConstants.ACTION_STATUS[3])) {
            viewMap.put(CommonConstants.MAP_NAME, "getSelectDeathMarkingMap");
        } else {
            viewMap.put(CommonConstants.MAP_NAME, "getSelectCustomerDetails");
        }
        new ViewAll(this,viewMap).show();
    }
    
    /* Fills up the HashMap with data when user selects the row in ViewAll screen  */
    public void fillData(Object  map) {
        try{
            HashMap hash = (HashMap) map;
            final String CUST_ID="CUST_ID";
            if (viewType != null) {
                if (viewType.equals(ClientConstants.ACTION_STATUS[2]) ||
                viewType.equals(ClientConstants.ACTION_STATUS[3]) ) {
                    isFilled = true;
                    hash.put(CommonConstants.MAP_WHERE, hash.get(CUST_ID));
                    observable.populateData(hash);
                    if (viewType.equals(ClientConstants.ACTION_STATUS[3])) {
                        ClientUtil.enableDisable(panMain, false);
                    } else {
                        ClientUtil.enableDisable(panMain, true);
                    }
                    setButtonEnableDisable();
                }else if(viewType.equals("CustomerId")){
                    txtCustomerId.setText((String)hash.get("CUST_ID"));
                }
                observable.removeTblDeathMarkingRow();
                String custId = txtCustomerId.getText();
                HashMap custMap = observable.getCustomerName(custId);
                lblValueCustName.setText(CommonUtil.convertObjToStr(custMap.get("CUSTOMER NAME")));
                observable.setTxtCustomerId(txtCustomerId.getText());
                observable.setDeathMarkingInfoList(observable.getDeathMarkingInfo(txtCustomerId.getText()));
                observable.setTxtCustomerId(txtCustomerId.getText());
                observable.notifyObservers();
                }
        }catch(Exception e){
        }
    }
    
    /** Does the authorization for the row selected in the AuthorizationUI screen */
    public void authorizeStatus(String authorizeStatus) {
        if (!isFilled ){
            HashMap mapParam = new HashMap();
            mapParam.put(CommonConstants.MAP_NAME, "getDeathMarkingAuthorizeList");
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
            authDataMap.put("CUST_ID", txtCustomerId.getText());
            arrList.add(authDataMap);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorize(singleAuthorizeMap);
        }
    }
    
    /* Does the Authorization thru DeathMarkingDA0 class **/
    public void authorize(HashMap map) {
        map.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        observable.setAuthorizeMap(map);
        observable.doAction();
        btnCancelActionPerformed(null);
    }
    
    /** This method clears the labels in the ui */
    private void clearLables(){
        lblValueCustName.setText("");
    }
    
   /* This shows the Alert when the user data entry is wrong **/
    private int showAlertWindow(String alertMsg) throws Exception {
        int optionSelected = 1;
        String[] options = {resourceBundle.getString("cDialogOK")};
        optionSelected = COptionPane.showOptionDialog(null,resourceBundle.getString(alertMsg), CommonConstants.INFORMATIONTITLE,
        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
        null, options, options[0]);
        return optionSelected;
    }
    
    /** validates the DtofDeathField */
    private void validateDtOfDeath(){
        try{
            Date dtDeath=  DateUtil.getDateMMDDYYYY(tdtDtOfDeath.getDateValue());
            Date current = (Date)curDate.clone();
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
            Date current = (Date)curDate.clone();
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
        panMain = new com.see.truetransact.uicomponent.CPanel();
        panId = new com.see.truetransact.uicomponent.CPanel();
        lblCustomerId = new com.see.truetransact.uicomponent.CLabel();
        lblCustomerName = new com.see.truetransact.uicomponent.CLabel();
        lblValueCustName = new com.see.truetransact.uicomponent.CLabel();
        panDepositNumber = new com.see.truetransact.uicomponent.CPanel();
        txtCustomerId = new com.see.truetransact.uicomponent.CTextField();
        btnCustomerId = new com.see.truetransact.uicomponent.CButton();
        panDetails = new com.see.truetransact.uicomponent.CPanel();
        lblDateOfDeath = new com.see.truetransact.uicomponent.CLabel();
        tdtDtOfDeath = new com.see.truetransact.uicomponent.CDateField();
        lblReportedOn = new com.see.truetransact.uicomponent.CLabel();
        tdtReportedOn = new com.see.truetransact.uicomponent.CDateField();
        lblReportedBy = new com.see.truetransact.uicomponent.CLabel();
        txtReportedBy = new com.see.truetransact.uicomponent.CTextField();
        lblRelationship = new com.see.truetransact.uicomponent.CLabel();
        lblReferenceNo = new com.see.truetransact.uicomponent.CLabel();
        txtReferenceNo = new com.see.truetransact.uicomponent.CTextField();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        txtRemarks = new com.see.truetransact.uicomponent.CTextField();
        cboRelationship = new com.see.truetransact.uicomponent.CComboBox();
        srpDeathMarking = new com.see.truetransact.uicomponent.CScrollPane();
        tblDeathMarking = new com.see.truetransact.uicomponent.CTable();
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
        setMinimumSize(new java.awt.Dimension(87, 26));
        setPreferredSize(new java.awt.Dimension(760, 330));
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

        panMain.setLayout(new java.awt.GridBagLayout());

        panId.setLayout(new java.awt.GridBagLayout());

        lblCustomerId.setText("Customer Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panId.add(lblCustomerId, gridBagConstraints);

        lblCustomerName.setText("CustomerName");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 32, 4, 4);
        panId.add(lblCustomerName, gridBagConstraints);

        lblValueCustName.setMaximumSize(new java.awt.Dimension(100, 16));
        lblValueCustName.setMinimumSize(new java.awt.Dimension(100, 16));
        lblValueCustName.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panId.add(lblValueCustName, gridBagConstraints);

        panDepositNumber.setLayout(new java.awt.GridBagLayout());

        txtCustomerId.setEditable(false);
        txtCustomerId.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositNumber.add(txtCustomerId, gridBagConstraints);

        btnCustomerId.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnCustomerId.setMaximumSize(new java.awt.Dimension(21, 21));
        btnCustomerId.setMinimumSize(new java.awt.Dimension(21, 21));
        btnCustomerId.setPreferredSize(new java.awt.Dimension(21, 21));
        btnCustomerId.setEnabled(false);
        btnCustomerId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustomerIdActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panDepositNumber.add(btnCustomerId, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panId.add(panDepositNumber, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.insets = new java.awt.Insets(4, 24, 4, 0);
        panMain.add(panId, gridBagConstraints);

        panDetails.setLayout(new java.awt.GridBagLayout());

        lblDateOfDeath.setText("Date of Death");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 32, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panDetails.add(lblDateOfDeath, gridBagConstraints);

        tdtDtOfDeath.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tdtDtOfDeathFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtDtOfDeathFocusLost(evt);
            }

        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDetails.add(tdtDtOfDeath, gridBagConstraints);

        lblReportedOn.setText("Reported On");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 76, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panDetails.add(lblReportedOn, gridBagConstraints);

        tdtReportedOn.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tdtReportedOnFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtReportedOnFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDetails.add(tdtReportedOn, gridBagConstraints);

        lblReportedBy.setText("Reported By");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 32, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panDetails.add(lblReportedBy, gridBagConstraints);

        txtReportedBy.setMinimumSize(new java.awt.Dimension(100, 21));
        txtReportedBy.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtReportedByFocusGained(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDetails.add(txtReportedBy, gridBagConstraints);

        lblRelationship.setText("Relationship");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 76, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panDetails.add(lblRelationship, gridBagConstraints);

        lblReferenceNo.setText("Reference No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 32, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panDetails.add(lblReferenceNo, gridBagConstraints);

        txtReferenceNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDetails.add(txtReferenceNo, gridBagConstraints);

        lblRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 76, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panDetails.add(lblRemarks, gridBagConstraints);

        txtRemarks.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDetails.add(txtRemarks, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        panDetails.add(cboRelationship, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 8, 0);
        panMain.add(panDetails, gridBagConstraints);

        srpDeathMarking.setMinimumSize(new java.awt.Dimension(750, 100));
        srpDeathMarking.setPreferredSize(new java.awt.Dimension(750, 100));
        tblDeathMarking.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Deposit No.", "Total Balance", "Create Dt.", "Maturity Dt.", "Interest", "Available Balance", "Settlement", "Nominee"
            }
        ));
        srpDeathMarking.setViewportView(tblDeathMarking);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panMain.add(srpDeathMarking, gridBagConstraints);

        getContentPane().add(panMain, java.awt.BorderLayout.CENTER);

        mbrDeathMarking.setName("mbrCustomer");
        mnuProcess.setText("Process");
        mnuProcess.setName("mnuProcess");
        mitNew.setText("New");
        mitNew.setName("mitNew");
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });

        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mitEdit.setName("mitEdit");
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });

        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mitDelete.setName("mitDelete");
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });

        mnuProcess.add(mitDelete);

        sptNew.setName("sptNew");
        mnuProcess.add(sptNew);

        sptEdit.setName("sptNew");
        mnuProcess.add(sptEdit);

        mitSave.setText("Save");
        mitSave.setName("mitSave");
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });

        mnuProcess.add(mitSave);

        mitCancel.setText("Cancel");
        mitCancel.setName("mitCancel");
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });

        mnuProcess.add(mitCancel);

        sptSave.setName("sptSave");
        mnuProcess.add(sptSave);

        mitPrint.setText("Print");
        mitPrint.setName("mitPrint");
        mnuProcess.add(mitPrint);

        mitClose.setText("Close");
        mitClose.setName("mitClose");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });

        mnuProcess.add(mitClose);

        mbrDeathMarking.add(mnuProcess);

        setJMenuBar(mbrDeathMarking);

        pack();
    }//GEN-END:initComponents
    
    private void tdtReportedOnFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtReportedOnFocusLost
        // TODO add your handling code here:
        dateValidation();
    }//GEN-LAST:event_tdtReportedOnFocusLost
    
    private void tdtReportedOnFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtReportedOnFocusGained
        // Add your handling code here:
        validateDtOfDeath();
    }//GEN-LAST:event_tdtReportedOnFocusGained
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
      btnCloseActionPerformed(evt); 
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // Add your handling code here:
       btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
    
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
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        ClientUtil.clearAll(this);
        clearLables();
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void btnCustomerIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustomerIdActionPerformed
        // Add your handling code here:
        callView("CustomerId");
    }//GEN-LAST:event_btnCustomerIdActionPerformed
    
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
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // Add your handling code here:
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // Add your handling code here:
        authorizeStatus(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // Add your handling code here:
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    
    private void tdtDtOfDeathFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtDtOfDeathFocusGained
        // Add your handling code here:
        
    }//GEN-LAST:event_tdtDtOfDeathFocusGained
    
    private void tdtDtOfDeathFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtDtOfDeathFocusLost
        // Add your handling code here:
    }//GEN-LAST:event_tdtDtOfDeathFocusLost
    
    private void txtReportedByFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtReportedByFocusGained
        // Add your handling code here:
        
        //String startDate = tdtDtOfDeath.getDateValue();
        //ClientUtil.validateToDate(tdtReportedOn, startDate);
    }//GEN-LAST:event_txtReportedByFocusGained
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        String mandatoryMessage = checkMandatory(panMain);
        if(mandatoryMessage.length() > 0){
            displayAlert(mandatoryMessage);
        }else{
            savePerformed();
        }
    }//GEN-LAST:event_btnSaveActionPerformed
    
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        observable.resetForm();
        clearLables();
        observable.removeTblDeathMarkingRow();
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panMain, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        setButtonEnableDisable();
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        observable.resetForm();
        ClientUtil.clearAll(this);
        clearLables();
        ClientUtil.enableDisable(panMain, true);
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
        DeathMarkingUI  tui = new DeathMarkingUI();
        frame.getContentPane().add(tui);
        frame.setSize(600,400);
        frame.show();
        tui.show();
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnCustomerId;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CComboBox cboRelationship;
    private com.see.truetransact.uicomponent.CLabel lblCustomerId;
    private com.see.truetransact.uicomponent.CLabel lblCustomerName;
    private com.see.truetransact.uicomponent.CLabel lblDateOfDeath;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblReferenceNo;
    private com.see.truetransact.uicomponent.CLabel lblRelationship;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
    private com.see.truetransact.uicomponent.CLabel lblReportedBy;
    private com.see.truetransact.uicomponent.CLabel lblReportedOn;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblValueCustName;
    private com.see.truetransact.uicomponent.CMenuBar mbrDeathMarking;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panDepositNumber;
    private com.see.truetransact.uicomponent.CPanel panDetails;
    private com.see.truetransact.uicomponent.CPanel panId;
    private com.see.truetransact.uicomponent.CPanel panMain;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private javax.swing.JSeparator sptEdit;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CScrollPane srpDeathMarking;
    private com.see.truetransact.uicomponent.CTable tblDeathMarking;
    private javax.swing.JToolBar tbrDeathMarking;
    private com.see.truetransact.uicomponent.CDateField tdtDtOfDeath;
    private com.see.truetransact.uicomponent.CDateField tdtReportedOn;
    private com.see.truetransact.uicomponent.CTextField txtCustomerId;
    private com.see.truetransact.uicomponent.CTextField txtReferenceNo;
    private com.see.truetransact.uicomponent.CTextField txtRemarks;
    private com.see.truetransact.uicomponent.CTextField txtReportedBy;
    // End of variables declaration//GEN-END:variables
    
}
