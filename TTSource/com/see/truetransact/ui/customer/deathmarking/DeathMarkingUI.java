/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 * DeathMarkingUI.java
 *
 * Created on May 26, 2004, 4:35 PM
 */

package com.see.truetransact.ui.customer.deathmarking;

/**
 *
 * @author Ashok
 */

import java.util.Observer;
import java.util.Observable;
import java.util.HashMap;
import java.util.Date;
import java.util.ArrayList;

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
import java.util.ResourceBundle;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.ui.customer.CheckCustomerIdUI;

public class DeathMarkingUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer,UIMandatoryField{
    
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("com.see.truetransact.ui.customer.deathmarking.DeathMarkingRB", ProxyParameters.LANGUAGE);
    private HashMap mandatoryMap;
    private DeathMarkingOB observable;
    private DeathMarkingMRB objMandatoryRB;
    private String viewType= "";
    final String AUTHORIZE="Authorize";
    private boolean isFilled = false;
    private Date curDate = null;
    private String DRF="";
    /** Creates new form DeathMarkingUI */
    public DeathMarkingUI() {
        curDate = ClientUtil.getCurrentDate();
        initComponents();
        initUIComponents();
    }
    public DeathMarkingUI(HashMap hmap){
        curDate = ClientUtil.getCurrentDate();
        initComponents();
        initUIComponents();
        if(hmap.containsKey("DRF"))
        if(hmap.get("DRF").equals("NEW")){
        ArrayList list=(ArrayList)ClientUtil.executeQuery("getDeathMarkingDetailsForDRF",hmap); 
        if(list!=null && list.size()>0){
        HashMap hashMap=(HashMap)list.get(0);
        String custid=CommonUtil.convertObjToStr(hashMap.get("CUSTID"));
        String name=CommonUtil.convertObjToStr(hashMap.get("ACCT_NAME"));
        txtCustomerId.setText(custid);
        lblValueCustName.setText(name);
        observable.setDeathMarkingInfoList(list);
        }
        btnNew.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnSave.setEnabled(true);
        btnCancel.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnPrint.setEnabled(false);
        btnClose.setEnabled(false);
        ClientUtil.enableDisable(panDetails,true);
       observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
       DRF="NEW";
       setModified(true);
        }else if(hmap.get("DRF").equals("AUTHORIZE")){
            ArrayList list=(ArrayList)ClientUtil.executeQuery("getDeathMarkingDetailsForDRF",hmap);
            HashMap hashMap=(HashMap)list.get(0);
            String custid=CommonUtil.convertObjToStr(hashMap.get("CUSTID"));
            String name=CommonUtil.convertObjToStr(hashMap.get("ACCT_NAME"));
            txtCustomerId.setText(custid);
            lblValueCustName.setText(name);
            observable.setDeathMarkingInfoList(list);
            tdtDtOfDeath.setDateValue(CommonUtil.convertObjToStr(hmap.get("DEATH_DT")));
            txtReportedBy.setText(CommonUtil.convertObjToStr(hmap.get("REPORTED_BY")));
            txtReferenceNo.setText(CommonUtil.convertObjToStr(hmap.get("REFERENCE_NO")));
            tdtReportedOn.setDateValue(CommonUtil.convertObjToStr(hmap.get("REPORTED_ON")));
            String relationship=CommonUtil.convertObjToStr(hmap.get("RELATIONSHIP"));           
            txtRemarks.setText(CommonUtil.convertObjToStr(hmap.get("REMARKS")));
            
            btnNew.setEnabled(false);
            btnEdit.setEnabled(false);
            btnDelete.setEnabled(false);
            btnSave.setEnabled(false);
            btnCancel.setEnabled(false);
            btnAuthorize.setEnabled(true);
            btnReject.setEnabled(false);
            btnException.setEnabled(false);
            btnPrint.setEnabled(false);
            btnClose.setEnabled(false);
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            DRF="AUTHORIZE";
            isFilled=true;
            setModified(true);
        }else if(hmap.get("DRF").equals("REJECT")){
            ArrayList list=(ArrayList)ClientUtil.executeQuery("getDeathMarkingDetailsForDRF",hmap);
            HashMap hashMap=(HashMap)list.get(0);
            String custid=CommonUtil.convertObjToStr(hashMap.get("CUSTID"));
            String name=CommonUtil.convertObjToStr(hashMap.get("ACCT_NAME"));
            txtCustomerId.setText(custid);
            lblValueCustName.setText(name);
            observable.setDeathMarkingInfoList(list);
            tdtDtOfDeath.setDateValue(CommonUtil.convertObjToStr(hmap.get("DEATH_DT")));
            txtReportedBy.setText(CommonUtil.convertObjToStr(hmap.get("REPORTED_BY")));
            txtReferenceNo.setText(CommonUtil.convertObjToStr(hmap.get("REFERENCE_NO")));
            tdtReportedOn.setDateValue(CommonUtil.convertObjToStr(hmap.get("REPORTED_ON")));
            cboRelationship.setSelectedItem(CommonUtil.convertObjToStr(hmap.get("RELATIONSHIP")));
            txtRemarks.setText(CommonUtil.convertObjToStr(hmap.get("REMARKS")));
            btnNew.setEnabled(false);
            btnEdit.setEnabled(false);
            btnDelete.setEnabled(false);
            btnSave.setEnabled(false);
            btnCancel.setEnabled(false);
            btnAuthorize.setEnabled(false);
            btnReject.setEnabled(true);
            btnException.setEnabled(false);
            btnPrint.setEnabled(false);
            btnClose.setEnabled(false);
            observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
            DRF="REJECT";
            isFilled=true;
            setModified(true);
        }else if(hmap.get("DRF").equals("DELETE")){
            ArrayList list=(ArrayList)ClientUtil.executeQuery("getDeathMarkingDetailsForDRF",hmap);
            HashMap hashMap=(HashMap)list.get(0);
            String custid=CommonUtil.convertObjToStr(hashMap.get("CUSTID"));
            String name=CommonUtil.convertObjToStr(hashMap.get("ACCT_NAME"));
            txtCustomerId.setText(custid);
            lblValueCustName.setText(name);
            observable.setDeathMarkingInfoList(list);
            tdtDtOfDeath.setDateValue(CommonUtil.convertObjToStr(hmap.get("DEATH_DT")));
            txtReportedBy.setText(CommonUtil.convertObjToStr(hmap.get("REPORTED_BY")));
            txtReferenceNo.setText(CommonUtil.convertObjToStr(hmap.get("REFERENCE_NO")));
            tdtReportedOn.setDateValue(CommonUtil.convertObjToStr(hmap.get("REPORTED_ON")));
            cboRelationship.setSelectedItem(CommonUtil.convertObjToStr(hmap.get("RELATIONSHIP")));
            txtRemarks.setText(CommonUtil.convertObjToStr(hmap.get("REMARKS")));
            btnNew.setEnabled(false);
            btnEdit.setEnabled(false);
            btnDelete.setEnabled(false);
            btnSave.setEnabled(true);
            btnCancel.setEnabled(false);
            btnAuthorize.setEnabled(false);
            btnReject.setEnabled(false);
            btnException.setEnabled(false);
            btnPrint.setEnabled(false);
            btnClose.setEnabled(false);
            observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
            DRF="DELETE";
            setModified(true);
        }
    }
    
    /** Initialise the ui components */
    private void initUIComponents(){
        setFieldNames();
        internationalize();
        setHelpBtnEnable(false);
        setObservable();
        setMandatoryHashMap();
        setHelpMessage();
        setMaximumLength();
        initComponentData();
        observable.resetForm();
        ClientUtil.enableDisable(panMain, false);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panMain);
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
        lblDtOfDeath.setName("lblDtOfDeath");
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
        lblDtOfDeath.setText(resourceBundle.getString("lblDtOfDeath"));
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
        observable.setSelectedBranchID(getSelectedBranchID());
        observable.setModule(getModule());
        observable.setScreen(getScreen());
    }
    
    /** Sets the textfields to a maximumlength */
    private void setMaximumLength(){
        txtCustomerId.setMaxLength(16);
        txtReportedBy.setMaxLength(64);
        txtReferenceNo.setMaxLength(32);
        txtReferenceNo.setAllowNumber(true);
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
    }
    
    /* Does necessary operaion when user clicks the save button */
    private void savePerformed(){
        updateOBFields();
        observable.doAction();
        ClientUtil.clearAll(this);
        isFilled = false;
        if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED){
            settings();
        }
    }
    
    /* set the screen after the updation,insertion, deletion */
    private void settings(){
        observable.resetForm();
        setHelpBtnEnable(false);
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
        whereMap.put("BRANCH_ID", getSelectedBranchID());
        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
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
            setModified(true);
            HashMap hash = (HashMap) map;
            final String CUST_ID="CUST_ID";
            final String custId = CommonUtil.convertObjToStr(hash.get(CUST_ID));
            if (viewType != null) {
                if (viewType.equals(ClientConstants.ACTION_STATUS[2]) ||
                viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) ) {
                    isFilled = true;
                    hash.put(CommonConstants.MAP_WHERE, hash.get(CUST_ID));
                    hash.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
                    observable.populateData(hash);
                    if (observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE) {
                        ClientUtil.enableDisable(panMain, false);
                        setHelpBtnEnable(false);
                    }
                    if(observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT) {
                        ClientUtil.enableDisable(panMain, true);
                        setHelpBtnEnable(true);
                    }
                    if(viewType.equals(AUTHORIZE)){
                        ClientUtil.enableDisable(panMain, false);
                        setHelpBtnEnable(false);
                    }
                    setButtonEnableDisable();
                }
                if(viewType.equals("CustomerId")){
                    txtCustomerId.setText(CommonUtil.convertObjToStr(hash.get(CUST_ID)));
                }
                
                updateOBFields();
                observable.removeTblDeathMarkingRow();
                HashMap custMap = observable.getCustomerName(custId);
                lblValueCustName.setText(CommonUtil.convertObjToStr(custMap.get("CUSTOMER NAME")));
                observable.setTxtCustomerId(CommonUtil.convertObjToStr(hash.get(CUST_ID)));
                observable.setDeathMarkingInfoList(observable.getDeathMarkingInfo(CommonUtil.convertObjToStr(hash.get(CUST_ID))));
                observable.notifyObservers();
            }
        }catch(Exception e){
        }
    }
    
    /** Does the authorization for the row selected in the AuthorizationUI screen */
    public void authorizeStatus(String authorizeStatus) {
        viewType = AUTHORIZE;
        if (!isFilled ){
            setModified(true);
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            mapParam.put(CommonConstants.MAP_NAME, "getDeathMarkingAuthorizeList");
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
        } else if (isFilled){
            isFilled = false;
            HashMap singleAuthorizeMap = new HashMap();
            ArrayList arrList = new ArrayList();
            HashMap authDataMap = new HashMap();
            authDataMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            authDataMap.put(CommonConstants.AUTHORIZEDT, curDate);
            authDataMap.put("CUST_ID", txtCustomerId.getText());
            arrList.add(authDataMap);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorize(singleAuthorizeMap);
            
        }
    }
    
    /* Does the Authorization thru DeathMarkingDA0 class **/
    public void authorize(HashMap map) {
        String alertMsg = "confirmMsg";
        int option=0;
        if(!DRF.equals("AUTHORIZE") && !DRF.equals("REJECT")){
            if(observable.getActionType()==ClientConstants.ACTIONTYPE_REJECT){
                option=ClientUtil.confirmationAlert("Are you sure to Delete/Reject this record" );
            }else{
                option= ClientUtil.confirmationAlert(resourceBundle.getString(alertMsg));
                
            }
        }
        if(option==0){
            map.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setAuthorizeMap(map);
            observable.doAction();
            btnCancelActionPerformed(null);
        }else if(option==1){
            observable.resetForm();
            clearLables();
            setHelpBtnEnable(false);
            observable.removeTblDeathMarkingRow();
            ClientUtil.clearAll(this);
            ClientUtil.enableDisable(panMain, false);
            setButtonEnableDisable();
        }
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
            Date current = observable.getCreatedDate(txtCustomerId.getText());
            if(current!=null){
                if(!current.equals("")){
                    if(dtDeath.compareTo(current) < 0){
                        String stringDate = DateUtil.getStringDate(current);
                        ClientUtil.showAlertWindow(resourceBundle.getString("createdDateMsg") + stringDate);
                        tdtDtOfDeath.setDateValue("");
                        tdtDtOfDeath.requestFocus();
                    }
                }
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
            if(repOn.compareTo(current) < 0){
                showAlertWindow("currentDateMsg");
                tdtReportedOn.setDateValue("");
                tdtReportedOn.requestFocus();
            }
        }
        catch(Exception e){
        }
        return;
    }
    
    private void setHelpBtnEnable(boolean flag){
        btnCustomerId.setEnabled(flag);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        tbrDeathMarking = new javax.swing.JToolBar();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace24 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace25 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace26 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace27 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace28 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace29 = new com.see.truetransact.uicomponent.CLabel();
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
        lblDtOfDeath = new com.see.truetransact.uicomponent.CLabel();
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
        setPreferredSize(new java.awt.Dimension(800, 450));

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrDeathMarking.add(btnNew);

        lblSpace24.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace24.setText("     ");
        lblSpace24.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrDeathMarking.add(lblSpace24);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrDeathMarking.add(btnEdit);

        lblSpace25.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace25.setText("     ");
        lblSpace25.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace25.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace25.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrDeathMarking.add(lblSpace25);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
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

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrDeathMarking.add(btnSave);

        lblSpace26.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace26.setText("     ");
        lblSpace26.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrDeathMarking.add(lblSpace26);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrDeathMarking.add(btnCancel);

        lblSpace3.setText("     ");
        tbrDeathMarking.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrDeathMarking.add(btnAuthorize);

        lblSpace27.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace27.setText("     ");
        lblSpace27.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrDeathMarking.add(lblSpace27);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrDeathMarking.add(btnException);

        lblSpace28.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace28.setText("     ");
        lblSpace28.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrDeathMarking.add(lblSpace28);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrDeathMarking.add(btnReject);

        lblSpace4.setText("     ");
        tbrDeathMarking.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrDeathMarking.add(btnPrint);

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrDeathMarking.add(lblSpace29);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
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

        panMain.setLayout(new java.awt.GridBagLayout());

        panId.setMinimumSize(new java.awt.Dimension(750, 29));
        panId.setPreferredSize(new java.awt.Dimension(750, 29));
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

        lblValueCustName.setForeground(new java.awt.Color(0, 51, 204));
        lblValueCustName.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblValueCustName.setMaximumSize(new java.awt.Dimension(100, 16));
        lblValueCustName.setMinimumSize(new java.awt.Dimension(100, 16));
        lblValueCustName.setPreferredSize(new java.awt.Dimension(300, 16));
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
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panDepositNumber.add(txtCustomerId, gridBagConstraints);

        btnCustomerId.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
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

        lblDtOfDeath.setText("Date of Death");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 32, 4, 4);
        panDetails.add(lblDtOfDeath, gridBagConstraints);

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
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 76, 4, 4);
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
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 32, 4, 4);
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
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 76, 4, 4);
        panDetails.add(lblRelationship, gridBagConstraints);

        lblReferenceNo.setText("Reference No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 32, 4, 4);
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
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 76, 4, 4);
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

        srpDeathMarking.setMinimumSize(new java.awt.Dimension(780, 200));
        srpDeathMarking.setPreferredSize(new java.awt.Dimension(780, 200));

        tblDeathMarking.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Product Type", "Account No.", "Create Dt.", "Maturity Dt.", "Interest", "Available Balance", "Settlement", "Nominee"
            }
        ));
        srpDeathMarking.setViewportView(tblDeathMarking);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 4);
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
    }// </editor-fold>//GEN-END:initComponents
    
    private void tdtReportedOnFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtReportedOnFocusLost
        // TODO add your handling code here:
        
    }//GEN-LAST:event_tdtReportedOnFocusLost
    
    private void tdtReportedOnFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtReportedOnFocusGained
        // Add your handling code here:
        
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
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void btnCustomerIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustomerIdActionPerformed
        // Add your handling code here:
//        callView("CustomerId");
        viewType = "CustomerId";
        HashMap sourceMap = new HashMap();
        sourceMap.put("DEATH_MARKING","DEATH_MARKING");
        new CheckCustomerIdUI(this,sourceMap);
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
        btnAuthorize.setEnabled(false);
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // Add your handling code here:
          btnReject.setEnabled(false);
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
        setModified(false);
        dateValidation();
        validateDtOfDeath();
        String mandatoryMessage = checkMandatory(panMain);
        if(mandatoryMessage.length() > 0){
            displayAlert(mandatoryMessage);
        }else{
            savePerformed();
        }
        
    }//GEN-LAST:event_btnSaveActionPerformed
    
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        setModified(false);
        observable.resetForm();
        clearLables();
        setHelpBtnEnable(false);
        observable.removeTblDeathMarkingRow();
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panMain, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        setButtonEnableDisable();
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        DRF="";
        isFilled = false;
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        setModified(true);
        observable.resetForm();
        setHelpBtnEnable(true);
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
    private com.see.truetransact.uicomponent.CLabel lblDtOfDeath;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblReferenceNo;
    private com.see.truetransact.uicomponent.CLabel lblRelationship;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
    private com.see.truetransact.uicomponent.CLabel lblReportedBy;
    private com.see.truetransact.uicomponent.CLabel lblReportedOn;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace24;
    private com.see.truetransact.uicomponent.CLabel lblSpace25;
    private com.see.truetransact.uicomponent.CLabel lblSpace26;
    private com.see.truetransact.uicomponent.CLabel lblSpace27;
    private com.see.truetransact.uicomponent.CLabel lblSpace28;
    private com.see.truetransact.uicomponent.CLabel lblSpace29;
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
