/**
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * InspectionUI.java
 *
 * Created on June 8, 2004, 2:49 PM
 */

package com.see.truetransact.ui.sysadmin.audit;

/**
 *
 * @author Ashok
 */

import com.see.truetransact.ui.sysadmin.audit.InspectionRB;
import com.see.truetransact.ui.sysadmin.audit.InspectionOB;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.ui.sysadmin.audit.InspectionMRB;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.ui.TrueTransactMain;

import java.util.HashMap;
import java.util.Observer;
import java.util.Observable;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import com.see.truetransact.clientproxy.ProxyParameters;

public class InspectionUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer,UIMandatoryField{
    
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("com.see.truetransact.ui.sysadmin.audit.InspectionRB", ProxyParameters.LANGUAGE);
    private InspectionOB observable;
    private HashMap mandatoryMap;
    private InspectionMRB objMandatoryRB;
    private String viewType = "";
    private int selectedData = 0;
    private int result = 0;
    private String startDate,endDate;
    final String AUTHORIZE="Authorize";
    private Date currDt = null;
    /** Creates new form InspectionUI */
    public InspectionUI() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        initGUI();
    }
    
    /** This method is used to initilize the gui components */
    private void initGUI(){
        setFieldNames();
        internationalize();
        setObservable();
        setMandatoryHashMap();
        setHelpMessage();
        setMaximumLength();
        initComponentData();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panAppraisal);
        observable.resetForm();
        ClientUtil.enableDisable(panInspection,false);
        setButtonEnableDisable();
        setHelpBtnEnable(false);
        resetTable();
        tabInspection.resetVisits();
    }
    
   /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        btnAuthorize.setName("btnAuthorize");
        btnBranchCode.setName("btnBranchCode");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        btnTabDelete.setName("btnTabDelete");
        btnTabNew.setName("btnTabNew");
        btnTabSave.setName("btnTabSave");
        cboBranchRating.setName("cboBranchRating");
        cboCategory.setName("cboCategory");
        cboClassification.setName("cboClassification");
        txaInspectingOfficials.setName("txaInspectingOfficials");
        cboJobCategory.setName("cboJobCategory");
        txtStaffPosition.setName("txtStaffPosition");
        cboWeeklyHoliday.setName("cboWeeklyHoliday");
        lblActualPosition.setName("lblActualPosition");
        lblBranchCode.setName("lblBranchCode");
        lblBranchName.setName("lblBranchName");
        lblBranchRating.setName("lblBranchRating");
        lblCategory.setName("lblCategory");
        lblClassification.setName("lblClassification");
        lblInspCommOn.setName("lblInspCommOn");
        lblInspConcludedOn.setName("lblInspConcludedOn");
        lblInspectingOfficials.setName("lblInspectingOfficials");
        lblJobCategory.setName("lblJobCategory");
        lblManDays.setName("lblManDays");
        lblMsg.setName("lblMsg");
        lblOpeningDate.setName("lblOpeningDate");
        lblOtherInfo.setName("lblOtherInfo");
        lblPositionAsOn.setName("lblPositionAsOn");
        lblRegion.setName("lblRegion");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblStaffPosition.setName("lblStaffPosition");
        lblStatus.setName("lblStatus");
        lblValueName.setName("lblValueName");
        lblValueOpeningDate.setName("lblValueOpeningDate");
        lblValueRegion.setName("lblValueRegion");
        lblValueWorkingHours.setName("lblValueWorkingHours");
        lblWeeklyHoliday.setName("lblWeeklyHoliday");
        lblWorkingHours.setName("lblWorkingHours");
        mbrInspection.setName("mbrInspection");
        panAppraisal.setName("panAppraisal");
        panBranchCode.setName("panBranchCode");
        panBranchDetails.setName("panBranchDetails");
        panInspection.setName("panInspection");
        panOperations.setName("panOperations");
        panOtherInfo.setName("panOtherInfo");
        panStatus.setName("panStatus");
        panJobPosition.setName("panJobPosition");
        srpDevelopment.setName("srpAdvanceInfo");
        srpAppraisalInfo.setName("srpAppraisalInfo");
        srpAuditJobMaster.setName("srpBranchDetail");
        srpOtherInfo.setName("srpOtherInfo");
        tabInspection.setName("tabInspection");
        tblDevelopment.setName("tblAdvancesInfo");
        tblAppraisalInfo.setName("tblAppraisalInfo");
        tblAuditJobMaster.setName("tblBranchDetail");
        tdtInspectionCommencedOn.setName("tdtInspectionCommencedOn");
        tdtInspectionConcludedOn.setName("tdtInspConcludedOn");
        tdtPositionAsOn.setName("tdtPositionAsOn");
        txaOtherInformation.setName("txaOtherInformation");
        txtActualPosition.setName("txtActualPosition");
        txtBranchCode.setName("txtBranchCode");
        txtNumberOfManDays.setName("txtNumberOfManDays");
        lblbranchCode.setName("lblbranchCode");
        lblValueBranchCode.setName("lblValueBranchCode");
        lblbranchName.setName("lblbranchName");
        lblValueBranchName.setName("lblValueBranchName");
        lblAuditId.setName("lblAuditId");
        lblValueAuditId.setName("lblValueAuditId");
        
    }
    
    /* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblValueRegion.setText(resourceBundle.getString("lblValueRegion"));
        btnTabSave.setText(resourceBundle.getString("btnTabSave"));
        lblWorkingHours.setText(resourceBundle.getString("lblWorkingHours"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        lblBranchCode.setText(resourceBundle.getString("lblBranchCode"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        lblWeeklyHoliday.setText(resourceBundle.getString("lblWeeklyHoliday"));
        lblClassification.setText(resourceBundle.getString("lblClassification"));
        lblInspCommOn.setText(resourceBundle.getString("lblInspCommOn"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        lblActualPosition.setText(resourceBundle.getString("lblActualPosition"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        btnBranchCode.setText(resourceBundle.getString("btnBranchCode"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        lblInspectingOfficials.setText(resourceBundle.getString("lblInspectingOfficials"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblOpeningDate.setText(resourceBundle.getString("lblOpeningDate"));
        btnTabDelete.setText(resourceBundle.getString("btnTabDelete"));
        lblValueName.setText(resourceBundle.getString("lblValueName"));
        lblInspConcludedOn.setText(resourceBundle.getString("lblInspConcludedOn"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblBranchRating.setText(resourceBundle.getString("lblBranchRating"));
        lblValueOpeningDate.setText(resourceBundle.getString("lblValueOpeningDate"));
        lblCategory.setText(resourceBundle.getString("lblCategory"));
        btnTabNew.setText(resourceBundle.getString("btnTabNew"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        lblPositionAsOn.setText(resourceBundle.getString("lblPositionAsOn"));
        lblManDays.setText(resourceBundle.getString("lblManDays"));
        lblRegion.setText(resourceBundle.getString("lblRegion"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblBranchName.setText(resourceBundle.getString("lblBranchName"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblValueWorkingHours.setText(resourceBundle.getString("lblValueWorkingHours"));
        lblStaffPosition.setText(resourceBundle.getString("lblStaffPosition"));
        lblOtherInfo.setText(resourceBundle.getString("lblOtherInfo"));
        lblJobCategory.setText(resourceBundle.getString("lblJobCategory"));
        lblbranchCode.setText(resourceBundle.getString("lblbranchCode"));
        lblValueBranchCode.setText(resourceBundle.getString("lblValueBranchCode"));
        lblbranchName.setText(resourceBundle.getString("lblbranchName"));
        lblValueBranchName.setText(resourceBundle.getString("lblValueBranchName"));
        lblAuditId.setText(resourceBundle.getString("lblAuditId"));
        lblValueAuditId.setText(resourceBundle.getString("lblValueAuditId"));
    }
    
    
/* Auto Generated Method - setMandatoryHashMap()
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtBranchCode", new Boolean(true));
        mandatoryMap.put("tdtInspCommOn", new Boolean(true));
        mandatoryMap.put("tdtInspConcOn", new Boolean(true));
        mandatoryMap.put("tdtPositionAsOn", new Boolean(true));
        mandatoryMap.put("txaOtherInfo", new Boolean(true));
        mandatoryMap.put("txtManDays", new Boolean(true));
        mandatoryMap.put("cboBranchRating", new Boolean(true));
        mandatoryMap.put("cboInspectingOfficials", new Boolean(true));
        mandatoryMap.put("cboClassification", new Boolean(true));
        mandatoryMap.put("cboCategory", new Boolean(true));
        mandatoryMap.put("cboWeeklyHoliday", new Boolean(true));
        mandatoryMap.put("cboStaffPosition", new Boolean(true));
        mandatoryMap.put("cboJobCategory", new Boolean(true));
        mandatoryMap.put("txtActualPosition", new Boolean(true));
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
        objMandatoryRB = new InspectionMRB();
        txtBranchCode.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBranchCode"));
        tdtInspectionCommencedOn.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtInspectionCommencedOn"));
        tdtInspectionConcludedOn.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtInspectionConcludedOn"));
        tdtPositionAsOn.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtPositionAsOn"));
        txaOtherInformation.setHelpMessage(lblMsg, objMandatoryRB.getString("txaOtherInformation"));
        txtNumberOfManDays.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNumberOfManDays"));
        cboBranchRating.setHelpMessage(lblMsg, objMandatoryRB.getString("cboBranchRating"));
        txaInspectingOfficials.setHelpMessage(lblMsg, objMandatoryRB.getString("txaInspectingOfficials"));
        cboClassification.setHelpMessage(lblMsg, objMandatoryRB.getString("cboClassification"));
        cboCategory.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCategory"));
        cboWeeklyHoliday.setHelpMessage(lblMsg, objMandatoryRB.getString("cboWeeklyHoliday"));
        txtStaffPosition.setHelpMessage(lblMsg, objMandatoryRB.getString("txtStaffPosition"));
        cboJobCategory.setHelpMessage(lblMsg, objMandatoryRB.getString("cboJobCategory"));
        txtActualPosition.setHelpMessage(lblMsg, objMandatoryRB.getString("txtActualPosition"));
    }
    
    
    
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        txtStaffPosition.setText(observable.getTxtStaffPosition());
        lblValueAuditId.setText(observable.getLblValueAuditId());
        txtBranchCode.setText(observable.getTxtBranchCode());
        tdtInspectionCommencedOn.setDateValue(observable.getTdtInspCommOn());
        tdtInspectionConcludedOn.setDateValue(observable.getTdtInspConcOn());
        tdtPositionAsOn.setDateValue(observable.getTdtPositionAsOn());
        txaOtherInformation.setText(observable.getTxaOtherInfo());
        txtNumberOfManDays.setText(observable.getTxtManDays());
        cboBranchRating.setSelectedItem(observable.getCboBranchRating());
        txaInspectingOfficials.setText(observable.getTxaInspectingOfficials());
        cboClassification.setSelectedItem(observable.getCboClassification());
        cboCategory.setSelectedItem(observable.getCboCategory());
        cboWeeklyHoliday.setSelectedItem(observable.getCboWeeklyHoliday());
        cboJobCategory.setSelectedItem(observable.getCboJobCategory());
        txtActualPosition.setText(observable.getTxtActualPosition());
        tblAuditJobMaster.setModel(observable.getTblAuditJobMasterModel());
        tblAuditJobMaster.revalidate();
        tblAppraisalInfo.setModel(observable.getTblAppraisalInfoModel());
        tblAppraisalInfo.revalidate();
        tblDevelopment.setModel(observable.getTblDevelopmentModel());
        tblAppraisalInfo.revalidate();
    }
    
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setTxtBranchCode(txtBranchCode.getText());
        observable.setTdtInspCommOn(tdtInspectionCommencedOn.getDateValue());
        observable.setTdtInspConcOn(tdtInspectionConcludedOn.getDateValue());
        observable.setTdtPositionAsOn(tdtPositionAsOn.getDateValue());
        observable.setTxaOtherInfo(txaOtherInformation.getText());
        observable.setTxtManDays(txtNumberOfManDays.getText());
        observable.setCboBranchRating((String) cboBranchRating.getSelectedItem());
        observable.setTxaInspectingOfficials((String) txaInspectingOfficials.getText());
        observable.setCboClassification((String) cboClassification.getSelectedItem());
        observable.setCboCategory((String) cboCategory.getSelectedItem());
        observable.setCboWeeklyHoliday((String) cboWeeklyHoliday.getSelectedItem());
        observable.setTxtStaffPosition((String) txtStaffPosition.getText());
        observable.setCboJobCategory((String) cboJobCategory.getSelectedItem());
        observable.setTxtActualPosition(txtActualPosition.getText());
        observable.setModule(getModule());
        observable.setScreen(getScreen());
    }
    
    /** Adds this ui as a observer to a observable class */
    private void setObservable() {
        observable = InspectionOB.getInstance();
        observable.addObserver(this);
    }
    
    /** Sets a Maximum length to the text fields */
    private void setMaximumLength(){
        txtBranchCode.setMaxLength(16);
        txtNumberOfManDays.setMaxLength(4);
        txaInspectingOfficials.setTabSize(512);
        txaOtherInformation.setTabSize(1024);
        txtStaffPosition.setMaxLength(4);
        txtActualPosition.setMaxLength(4);
    }
    
    /** Initialising the Data in the combobox in the ui  */
    private void initComponentData() {
        try{
            cboBranchRating.setModel(observable.getCbmBranchRating());
            cboJobCategory.setModel(observable.getCbmJobCategory());
            cboClassification.setModel(observable.getCbmClassification());
            cboCategory.setModel(observable.getCbmCategory());
            cboWeeklyHoliday.setModel(observable.getCbmWeeklyHoliday());
        }catch(ClassCastException e){
            e.printStackTrace();
        }
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
        btnView.setEnabled(!btnView.isEnabled());
    }
    
    /** This method just removes the row in the Table */
    private void resetTable(){
        observable.removeTblAuditJobMasterRow();
        observable.removeTblAppraisalInfoRow();
        observable.removeTblDevelopmentRow();
    }
    
    /** This method is called up whenever editbuton,deletebutton or the helpbutton is clicked */
    private void callView(String currField) {
        viewType = currField;
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        if (currField.equals(ClientConstants.ACTION_STATUS[2])  ||
        currField.equals(ClientConstants.ACTION_STATUS[3]) || currField.equals(ClientConstants.ACTION_STATUS[17])) {
            viewMap.put(CommonConstants.MAP_NAME, "getSelectAudit");
        } else {
            viewMap.put(CommonConstants.MAP_NAME, "getSelectBranchMaster");
        }
        new ViewAll(this,viewMap).show();
    }
    
    /* Fills up the HashMap with data when user selects the row in ViewAll screen  */
    public void fillData(Object  map) {
        try{
            setModified(true);
            HashMap hash = (HashMap) map;
            final String auditId="AUDIT_ID";
            final String branchCode = "BRANCH_CODE";
            if (viewType != null) {
                if (viewType.equals(ClientConstants.ACTION_STATUS[2]) ||
                viewType.equals(ClientConstants.ACTION_STATUS[3])|| viewType.equals(AUTHORIZE)  ||
                viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                    hash.put(CommonConstants.MAP_WHERE, hash.get(auditId));
                    hash.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                    observable.populateData(hash);
                    ClientUtil.clearAll(panJobPosition);
                    if(observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                        setHelpBtnEnable(true);
                        ClientUtil.enableDisable(panInspection, true);
                        btnTabNew.setEnabled(true);
                    }else{
                        setHelpBtnEnable(false);
                        ClientUtil.enableDisable(panInspection, false);
                        btnTabNew.setEnabled(false);
                    }
                    if(viewType.equals(AUTHORIZE)){
                        setHelpBtnEnable(false);
                        ClientUtil.enableDisable(panInspection, false);
                        btnTabNew.setEnabled(false);
                    }
                    setButtonEnableDisable();
                }else if(viewType.equals("BranchCode")){
                    txtBranchCode.setText(CommonUtil.convertObjToStr(hash.get(branchCode)));
                }
                updateOBFields();
                HashMap branchMap = observable.getBranchDetails(txtBranchCode.getText());
                if(branchMap != null){
                    lblValueName.setText(CommonUtil.convertObjToStr(branchMap.get("BRANCH_NAME")));
                    lblValueBranchCode.setText(CommonUtil.convertObjToStr(branchMap.get("BRANCH_CODE")));
                    lblValueBranchName.setText(CommonUtil.convertObjToStr(branchMap.get("BRANCH_NAME")));
                    lblValueRegion.setText(CommonUtil.convertObjToStr(branchMap.get("CITY")));
                    int hours = CommonUtil.convertObjToInt(branchMap.get("WORKING_HOURS"));
                    int mins  = CommonUtil.convertObjToInt(branchMap.get("WORKING_MINS"));
                    if ( mins >= 60){
                        int q = mins/60;
                        int r = mins % 60;
                        hours = hours + q;
                        mins = r;
                    }
                    String hrs = String.valueOf(hours);
                    String mns = String .valueOf(mins);
                    lblValueWorkingHours.setText(hrs+" Hours "+mns+" Minutes");
                    String openDate = DateUtil.getStringDate((Date)branchMap.get("OPENING_DT"));
                    lblValueOpeningDate.setText(openDate);
                }
                observable.removeTblAppraisalInfoRow();
                observable.setAppraisalInfoList(observable.getAuditPerformance(txtBranchCode.getText()));
                observable.fillTblDevelopmentModel();
                observable.ttNotifyObservers();
            }
            if(viewType.equals(AUTHORIZE)){
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /** This method displays the alertmessage when mandatory fields r empty */
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    /** This method either adds a row in the CTable to the TextFields or adds up a roow to the CTable */
    private void addCTable(){
        if(selectedData==1){
            observable.setCboJobCategory(CommonUtil.convertObjToStr(cboJobCategory.getSelectedItem()));
            observable.setTxtActualPosition(txtActualPosition.getText());
            observable.setTableValueAt();
            observable.setCboJobCategory("");
            observable.setTxtActualPosition("");
            btnTabSave.setEnabled(false);
            btnTabDelete.setEnabled(false);
            cboJobCategory.setEnabled(false);
            ClientUtil.enableDisable(panJobPosition,false);
            ClientUtil.clearAll(panJobPosition);
        }
        else{
            /** when clicked on the new button of CTable **/
            result=-1;
            updateOBFields();
            result = observable.addAuditJobMaster();
            if(result==0 || result==1 || result==-1){
                ClientUtil.enableDisable(panJobPosition,false);
                cboJobCategory.setSelectedItem("");
                txtActualPosition.setText("");
                btnTabSave.setEnabled(false);
                btnTabNew.setEnabled(true);
            }
            
            if (result == 2){
                /** The action taken for the Cancel option **/
                ClientUtil.enableDisable(panJobPosition,true);
                
            }
        }
    }
    
    /** This method check the mandtaoriness and return a string */
    private String checkMandatory(javax.swing.JComponent component){
        return new MandatoryCheck().checkMandatory(getClass().getName(), component);
    }
    
    /** This method does the authorization */
    public void authorizeStatus(String authorizeStatus) {
        if (!viewType.equals(AUTHORIZE)){
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            mapParam.put(CommonConstants.MAP_NAME, "getAuditPerformanceAuthorizeList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeAuditPerformance");
            
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
            btnCancel.setEnabled(true);
        } else if (viewType.equals(AUTHORIZE)){
            //Changed BY Suresh
            //            String strWarnMsg = tabInspection.isAllTabsVisited();
            //            if (strWarnMsg.length() > 0){
            //                displayAlert(strWarnMsg);
            //                return;
            //            }
            //            strWarnMsg = null;
            tabInspection.resetVisits();
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("USER_ID", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("AUDIT_ID", lblValueAuditId.getText());
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDt.clone());
            ClientUtil.execute("authorizeAuditPerformance", singleAuthorizeMap);
            viewType = "";
            btnCancelActionPerformed(null);
        }
    }
    
    /** This method is used to reset the Labels to empty */
    private void resetLabels(){
        lblValueBranchName.setText("");
        lblValueRegion.setText("");
        lblValueBranchCode.setText("");
        lblValueName.setText("");
        lblValueWorkingHours.setText("");
        lblValueOpeningDate.setText("");
    }
    
    private void setHelpBtnEnable(boolean flag){
        btnBranchCode.setEnabled(flag);
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        tbrInspection = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
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
        panInspection = new com.see.truetransact.uicomponent.CPanel();
        tabInspection = new com.see.truetransact.uicomponent.CTabbedPane();
        panAppraisal = new com.see.truetransact.uicomponent.CPanel();
        lblBranchCode = new com.see.truetransact.uicomponent.CLabel();
        lblBranchName = new com.see.truetransact.uicomponent.CLabel();
        lblValueName = new com.see.truetransact.uicomponent.CLabel();
        lblRegion = new com.see.truetransact.uicomponent.CLabel();
        lblValueRegion = new com.see.truetransact.uicomponent.CLabel();
        panBranchCode = new com.see.truetransact.uicomponent.CPanel();
        txtBranchCode = new com.see.truetransact.uicomponent.CTextField();
        btnBranchCode = new com.see.truetransact.uicomponent.CButton();
        lblOtherInfo = new com.see.truetransact.uicomponent.CLabel();
        lblInspCommOn = new com.see.truetransact.uicomponent.CLabel();
        tdtInspectionCommencedOn = new com.see.truetransact.uicomponent.CDateField();
        lblInspConcludedOn = new com.see.truetransact.uicomponent.CLabel();
        tdtInspectionConcludedOn = new com.see.truetransact.uicomponent.CDateField();
        lblPositionAsOn = new com.see.truetransact.uicomponent.CLabel();
        tdtPositionAsOn = new com.see.truetransact.uicomponent.CDateField();
        panOtherInfo = new com.see.truetransact.uicomponent.CPanel();
        srpOtherInfo = new com.see.truetransact.uicomponent.CScrollPane();
        txaOtherInformation = new com.see.truetransact.uicomponent.CTextArea();
        lblManDays = new com.see.truetransact.uicomponent.CLabel();
        txtNumberOfManDays = new com.see.truetransact.uicomponent.CTextField();
        lblBranchRating = new com.see.truetransact.uicomponent.CLabel();
        cboBranchRating = new com.see.truetransact.uicomponent.CComboBox();
        lblInspectingOfficials = new com.see.truetransact.uicomponent.CLabel();
        panInspOfficials = new com.see.truetransact.uicomponent.CPanel();
        srpInspectingOfficials = new com.see.truetransact.uicomponent.CScrollPane();
        txaInspectingOfficials = new com.see.truetransact.uicomponent.CTextArea();
        lblAuditId = new com.see.truetransact.uicomponent.CLabel();
        lblValueAuditId = new com.see.truetransact.uicomponent.CLabel();
        srpAppraisalInfo = new com.see.truetransact.uicomponent.CScrollPane();
        tblAppraisalInfo = new com.see.truetransact.uicomponent.CTable();
        panDevelopment = new com.see.truetransact.uicomponent.CPanel();
        srpDevelopment = new com.see.truetransact.uicomponent.CScrollPane();
        tblDevelopment = new com.see.truetransact.uicomponent.CTable();
        panBranchDetails = new com.see.truetransact.uicomponent.CPanel();
        panJobPosition = new com.see.truetransact.uicomponent.CPanel();
        lblJobCategory = new com.see.truetransact.uicomponent.CLabel();
        cboJobCategory = new com.see.truetransact.uicomponent.CComboBox();
        lblActualPosition = new com.see.truetransact.uicomponent.CLabel();
        txtActualPosition = new com.see.truetransact.uicomponent.CTextField();
        panOperations = new com.see.truetransact.uicomponent.CPanel();
        btnTabNew = new com.see.truetransact.uicomponent.CButton();
        btnTabSave = new com.see.truetransact.uicomponent.CButton();
        btnTabDelete = new com.see.truetransact.uicomponent.CButton();
        srpAuditJobMaster = new com.see.truetransact.uicomponent.CScrollPane();
        tblAuditJobMaster = new com.see.truetransact.uicomponent.CTable();
        panBranchInfo = new com.see.truetransact.uicomponent.CPanel();
        lblbranchCode = new com.see.truetransact.uicomponent.CLabel();
        lblbranchName = new com.see.truetransact.uicomponent.CLabel();
        lblValueBranchCode = new com.see.truetransact.uicomponent.CLabel();
        lblValueBranchName = new com.see.truetransact.uicomponent.CLabel();
        lblOpeningDate = new com.see.truetransact.uicomponent.CLabel();
        lblValueOpeningDate = new com.see.truetransact.uicomponent.CLabel();
        lblClassification = new com.see.truetransact.uicomponent.CLabel();
        cboClassification = new com.see.truetransact.uicomponent.CComboBox();
        lblCategory = new com.see.truetransact.uicomponent.CLabel();
        cboCategory = new com.see.truetransact.uicomponent.CComboBox();
        lblWorkingHours = new com.see.truetransact.uicomponent.CLabel();
        lblValueWorkingHours = new com.see.truetransact.uicomponent.CLabel();
        lblWeeklyHoliday = new com.see.truetransact.uicomponent.CLabel();
        cboWeeklyHoliday = new com.see.truetransact.uicomponent.CComboBox();
        lblStaffPosition = new com.see.truetransact.uicomponent.CLabel();
        txtStaffPosition = new com.see.truetransact.uicomponent.CTextField();
        mbrInspection = new com.see.truetransact.uicomponent.CMenuBar();
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
        setMinimumSize(new java.awt.Dimension(650, 580));
        setPreferredSize(new java.awt.Dimension(650, 580));

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
        tbrInspection.add(btnView);

        lblSpace5.setText("     ");
        tbrInspection.add(lblSpace5);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrInspection.add(btnNew);

        lblSpace24.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace24.setText("     ");
        lblSpace24.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrInspection.add(lblSpace24);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrInspection.add(btnEdit);

        lblSpace25.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace25.setText("     ");
        lblSpace25.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace25.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace25.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrInspection.add(lblSpace25);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrInspection.add(btnDelete);

        lblSpace2.setText("     ");
        tbrInspection.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrInspection.add(btnSave);

        lblSpace26.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace26.setText("     ");
        lblSpace26.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrInspection.add(lblSpace26);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrInspection.add(btnCancel);

        lblSpace3.setText("     ");
        tbrInspection.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrInspection.add(btnAuthorize);

        lblSpace27.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace27.setText("     ");
        lblSpace27.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrInspection.add(lblSpace27);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrInspection.add(btnException);

        lblSpace28.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace28.setText("     ");
        lblSpace28.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrInspection.add(lblSpace28);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrInspection.add(btnReject);

        lblSpace4.setText("     ");
        tbrInspection.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrInspection.add(btnPrint);

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrInspection.add(lblSpace29);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrInspection.add(btnClose);

        getContentPane().add(tbrInspection, java.awt.BorderLayout.NORTH);

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

        panInspection.setLayout(new java.awt.GridBagLayout());

        panAppraisal.setLayout(new java.awt.GridBagLayout());

        lblBranchCode.setText("Branch Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAppraisal.add(lblBranchCode, gridBagConstraints);

        lblBranchName.setText("Branch Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAppraisal.add(lblBranchName, gridBagConstraints);

        lblValueName.setMaximumSize(new java.awt.Dimension(100, 21));
        lblValueName.setMinimumSize(new java.awt.Dimension(100, 21));
        lblValueName.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAppraisal.add(lblValueName, gridBagConstraints);

        lblRegion.setText("Region");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAppraisal.add(lblRegion, gridBagConstraints);

        lblValueRegion.setMaximumSize(new java.awt.Dimension(100, 21));
        lblValueRegion.setMinimumSize(new java.awt.Dimension(100, 21));
        lblValueRegion.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAppraisal.add(lblValueRegion, gridBagConstraints);

        panBranchCode.setLayout(new java.awt.GridBagLayout());

        txtBranchCode.setEditable(false);
        txtBranchCode.setMaximumSize(new java.awt.Dimension(100, 21));
        txtBranchCode.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBranchCode.add(txtBranchCode, gridBagConstraints);

        btnBranchCode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnBranchCode.setMaximumSize(new java.awt.Dimension(21, 21));
        btnBranchCode.setMinimumSize(new java.awt.Dimension(21, 21));
        btnBranchCode.setPreferredSize(new java.awt.Dimension(21, 21));
        btnBranchCode.setEnabled(false);
        btnBranchCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBranchCodeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panBranchCode.add(btnBranchCode, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAppraisal.add(panBranchCode, gridBagConstraints);

        lblOtherInfo.setText("Other Information");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAppraisal.add(lblOtherInfo, gridBagConstraints);

        lblInspCommOn.setText("Inspection Commenced On");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAppraisal.add(lblInspCommOn, gridBagConstraints);

        tdtInspectionCommencedOn.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tdtInspectionCommencedOnPropertyChange(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAppraisal.add(tdtInspectionCommencedOn, gridBagConstraints);

        lblInspConcludedOn.setText("Inspection Concluded On");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAppraisal.add(lblInspConcludedOn, gridBagConstraints);

        tdtInspectionConcludedOn.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtInspectionConcludedOnFocusLost(evt);
            }
        });
        tdtInspectionConcludedOn.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tdtInspectionConcludedOnPropertyChange(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAppraisal.add(tdtInspectionConcludedOn, gridBagConstraints);

        lblPositionAsOn.setText("Position As On");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAppraisal.add(lblPositionAsOn, gridBagConstraints);

        tdtPositionAsOn.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tdtPositionAsOnFocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAppraisal.add(tdtPositionAsOn, gridBagConstraints);

        panOtherInfo.setLayout(new java.awt.GridBagLayout());

        srpOtherInfo.setMinimumSize(new java.awt.Dimension(220, 125));
        srpOtherInfo.setPreferredSize(new java.awt.Dimension(220, 125));

        txaOtherInformation.setMinimumSize(new java.awt.Dimension(100, 16));
        srpOtherInfo.setViewportView(txaOtherInformation);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOtherInfo.add(srpOtherInfo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAppraisal.add(panOtherInfo, gridBagConstraints);

        lblManDays.setText("No. Of ManDays");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAppraisal.add(lblManDays, gridBagConstraints);

        txtNumberOfManDays.setMinimumSize(new java.awt.Dimension(100, 21));
        txtNumberOfManDays.setValidation(new NumericValidation());
        txtNumberOfManDays.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumberOfManDaysActionPerformed(evt);
            }
        });
        txtNumberOfManDays.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNumberOfManDaysFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNumberOfManDaysFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAppraisal.add(txtNumberOfManDays, gridBagConstraints);

        lblBranchRating.setText("Branch Rating");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAppraisal.add(lblBranchRating, gridBagConstraints);

        cboBranchRating.setMinimumSize(new java.awt.Dimension(100, 21));
        cboBranchRating.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cboBranchRatingFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboBranchRatingFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAppraisal.add(cboBranchRating, gridBagConstraints);

        lblInspectingOfficials.setText("Inspecting Officials");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 22, 0);
        panAppraisal.add(lblInspectingOfficials, gridBagConstraints);

        panInspOfficials.setLayout(new java.awt.GridBagLayout());

        srpInspectingOfficials.setMinimumSize(new java.awt.Dimension(100, 42));
        srpInspectingOfficials.setPreferredSize(new java.awt.Dimension(100, 42));
        srpInspectingOfficials.setViewportView(txaInspectingOfficials);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 16);
        panInspOfficials.add(srpInspectingOfficials, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAppraisal.add(panInspOfficials, gridBagConstraints);

        lblAuditId.setText("Audit Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAppraisal.add(lblAuditId, gridBagConstraints);

        lblValueAuditId.setMaximumSize(new java.awt.Dimension(100, 21));
        lblValueAuditId.setMinimumSize(new java.awt.Dimension(100, 21));
        lblValueAuditId.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAppraisal.add(lblValueAuditId, gridBagConstraints);

        tblAppraisalInfo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No. Of ManDays", "Branch Rating", "Inspecting Officials"
            }
        ));
        srpAppraisalInfo.setViewportView(tblAppraisalInfo);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 4);
        panAppraisal.add(srpAppraisalInfo, gridBagConstraints);

        panDevelopment.setBorder(javax.swing.BorderFactory.createTitledBorder("Development"));
        panDevelopment.setLayout(new java.awt.GridBagLayout());

        srpDevelopment.setMinimumSize(new java.awt.Dimension(550, 100));
        srpDevelopment.setPreferredSize(new java.awt.Dimension(550, 100));

        tblDevelopment.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Inspection Period", "Total Deposits", "Total Advances", "Priority Sector Advances"
            }
        ));
        srpDevelopment.setViewportView(tblDevelopment);

        panDevelopment.add(srpDevelopment, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAppraisal.add(panDevelopment, gridBagConstraints);

        tabInspection.addTab("Performance Appraisal", panAppraisal);

        panBranchDetails.setLayout(new java.awt.GridBagLayout());

        panJobPosition.setBorder(javax.swing.BorderFactory.createTitledBorder("Job Position\n"));
        panJobPosition.setLayout(new java.awt.GridBagLayout());

        lblJobCategory.setText("Job Category");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panJobPosition.add(lblJobCategory, gridBagConstraints);

        cboJobCategory.setMinimumSize(new java.awt.Dimension(100, 21));
        cboJobCategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboJobCategoryActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 4);
        panJobPosition.add(cboJobCategory, gridBagConstraints);

        lblActualPosition.setText("Actual Position");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panJobPosition.add(lblActualPosition, gridBagConstraints);

        txtActualPosition.setMinimumSize(new java.awt.Dimension(100, 21));
        txtActualPosition.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 4);
        panJobPosition.add(txtActualPosition, gridBagConstraints);

        panOperations.setLayout(new java.awt.GridBagLayout());

        btnTabNew.setText("New");
        btnTabNew.setEnabled(false);
        btnTabNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTabNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOperations.add(btnTabNew, gridBagConstraints);

        btnTabSave.setText("Save");
        btnTabSave.setEnabled(false);
        btnTabSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTabSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOperations.add(btnTabSave, gridBagConstraints);

        btnTabDelete.setText("Delete");
        btnTabDelete.setEnabled(false);
        btnTabDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTabDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOperations.add(btnTabDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panJobPosition.add(panOperations, gridBagConstraints);

        srpAuditJobMaster.setMinimumSize(new java.awt.Dimension(300, 22));
        srpAuditJobMaster.setPreferredSize(new java.awt.Dimension(300, 22));

        tblAuditJobMaster.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Job Category", "Actual Position"
            }
        ));
        tblAuditJobMaster.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblAuditJobMasterMouseClicked(evt);
            }
        });
        srpAuditJobMaster.setViewportView(tblAuditJobMaster);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panJobPosition.add(srpAuditJobMaster, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        panBranchDetails.add(panJobPosition, gridBagConstraints);

        panBranchInfo.setLayout(new java.awt.GridBagLayout());

        lblbranchCode.setText("Branch Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBranchInfo.add(lblbranchCode, gridBagConstraints);

        lblbranchName.setText("Branch Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBranchInfo.add(lblbranchName, gridBagConstraints);

        lblValueBranchCode.setMaximumSize(new java.awt.Dimension(100, 21));
        lblValueBranchCode.setMinimumSize(new java.awt.Dimension(100, 21));
        lblValueBranchCode.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBranchInfo.add(lblValueBranchCode, gridBagConstraints);

        lblValueBranchName.setMaximumSize(new java.awt.Dimension(100, 21));
        lblValueBranchName.setMinimumSize(new java.awt.Dimension(100, 21));
        lblValueBranchName.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBranchInfo.add(lblValueBranchName, gridBagConstraints);

        lblOpeningDate.setText("Opening Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBranchInfo.add(lblOpeningDate, gridBagConstraints);

        lblValueOpeningDate.setMaximumSize(new java.awt.Dimension(100, 21));
        lblValueOpeningDate.setMinimumSize(new java.awt.Dimension(100, 21));
        lblValueOpeningDate.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBranchInfo.add(lblValueOpeningDate, gridBagConstraints);

        lblClassification.setText("Classification");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBranchInfo.add(lblClassification, gridBagConstraints);

        cboClassification.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBranchInfo.add(cboClassification, gridBagConstraints);

        lblCategory.setText("Category");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBranchInfo.add(lblCategory, gridBagConstraints);

        cboCategory.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCategory.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cboCategoryFocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBranchInfo.add(cboCategory, gridBagConstraints);

        lblWorkingHours.setText(" Working Hours");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 4);
        panBranchInfo.add(lblWorkingHours, gridBagConstraints);

        lblValueWorkingHours.setMaximumSize(new java.awt.Dimension(100, 21));
        lblValueWorkingHours.setMinimumSize(new java.awt.Dimension(100, 21));
        lblValueWorkingHours.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBranchInfo.add(lblValueWorkingHours, gridBagConstraints);

        lblWeeklyHoliday.setText("Weekly Holiday");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBranchInfo.add(lblWeeklyHoliday, gridBagConstraints);

        cboWeeklyHoliday.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBranchInfo.add(cboWeeklyHoliday, gridBagConstraints);

        lblStaffPosition.setText("Staff Position");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBranchInfo.add(lblStaffPosition, gridBagConstraints);

        txtStaffPosition.setEditable(false);
        txtStaffPosition.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBranchInfo.add(txtStaffPosition, gridBagConstraints);

        panBranchDetails.add(panBranchInfo, new java.awt.GridBagConstraints());

        tabInspection.addTab("Branch Details", panBranchDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panInspection.add(tabInspection, gridBagConstraints);

        getContentPane().add(panInspection, java.awt.BorderLayout.CENTER);

        mbrInspection.setName("mbrCustomer");

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

        mbrInspection.add(mnuProcess);

        setJMenuBar(mbrInspection);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        callView("Enquiry");
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed
    
    private void tdtInspectionCommencedOnPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tdtInspectionCommencedOnPropertyChange
        // TODO add your handling code here:
        
    }//GEN-LAST:event_tdtInspectionCommencedOnPropertyChange
    
    private void tdtInspectionConcludedOnPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tdtInspectionConcludedOnPropertyChange
        // TODO add your handling code here:
        
    }//GEN-LAST:event_tdtInspectionConcludedOnPropertyChange
    
    private void tdtInspectionConcludedOnFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtInspectionConcludedOnFocusLost
        // TODO add your handling code here:
        String startDate = tdtInspectionCommencedOn.getDateValue();
        ClientUtil.validateToDate(tdtInspectionConcludedOn, startDate);
        
        
        
    }//GEN-LAST:event_tdtInspectionConcludedOnFocusLost
    
    private void cboBranchRatingFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboBranchRatingFocusLost
        // TODO add your handling code here:
        
    }//GEN-LAST:event_cboBranchRatingFocusLost
    
    private void cboBranchRatingFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboBranchRatingFocusGained
        // TODO add your handling code here:
        
    }//GEN-LAST:event_cboBranchRatingFocusGained
    
    private void txtNumberOfManDaysFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNumberOfManDaysFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumberOfManDaysFocusLost
    
    private void txtNumberOfManDaysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumberOfManDaysActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumberOfManDaysActionPerformed
    
    private void cboCategoryFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboCategoryFocusGained
        // TODO add your handling code here:
        
    }//GEN-LAST:event_cboCategoryFocusGained
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // TODO add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // TODO add your handling code here:
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // TODO add your handling code here:
        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // TODO add your handling code here:
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // TODO add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // TODO add your handling code here:
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void txtNumberOfManDaysFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNumberOfManDaysFocusGained
        // Add your handling code here:
        
    }//GEN-LAST:event_txtNumberOfManDaysFocusGained
    
    private void tdtPositionAsOnFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtPositionAsOnFocusGained
        // Add your handling code here:
        startDate = tdtInspectionCommencedOn.getDateValue();
        endDate = tdtInspectionConcludedOn.getDateValue();
        try{
            if(!startDate.equals("") && !endDate.equals("")){
                observable. removeTblDevelopmentRow();
                observable.fillTblDevelopmentModel();
                observable.setTblModel(startDate , endDate);
                tblDevelopment.setModel(observable.getTblDevelopmentModel());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_tdtPositionAsOnFocusGained
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        setModified(true);
        updateOBFields();
        observable.removeTblAuditJobMasterRow();
        observable.notifyObservers();
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        ClientUtil.enableDisable(panInspection,false);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        setModified(false);
        observable.resetStatus();
        observable.resetForm();
        ClientUtil.clearAll(panInspection);
        ClientUtil.enableDisable(panInspection, false);
        ClientUtil.enableDisable(panOperations,false);
        viewType = "";
        lblValueName.setText("");
        lblValueRegion.setText("");
        lblValueBranchCode.setText("");
        lblValueBranchName.setText("");
        lblValueOpeningDate.setText("");
        lblValueWorkingHours.setText("");
        setButtonEnableDisable();
        setHelpBtnEnable(false);
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        resetTable();
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnTabDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTabDeleteActionPerformed
        // Add your handling code here:
        updateOBFields();
        observable.deleteAuditJobMaster();
        txtStaffPosition.setText(observable.fillTxtStaffPosition());
        btnTabNew.setEnabled(!btnEdit.isEnabled());
        btnTabSave.setEnabled(btnEdit.isEnabled());
        btnTabDelete.setEnabled(btnEdit.isEnabled());
        txtActualPosition.setEnabled(btnEdit.isEnabled());
        txtActualPosition.setEditable(btnEdit.isEnabled());
        cboJobCategory.setSelectedItem("");
        txtActualPosition.setText("");
    }//GEN-LAST:event_btnTabDeleteActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        callView("Delete");
        btnTabNew.setEnabled(false);
        observable.existingData();
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        callView("Edit");
        observable.existingData();
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        try{
            setModified(false);
            updateOBFields();
            StringBuffer mandatoryMsgs = new StringBuffer();
            String appraisalMsg = checkMandatory(panAppraisal);
            if(appraisalMsg.length() > 0){
                mandatoryMsgs.append(appraisalMsg);
            }
            String branchInfomsg = checkMandatory(panBranchInfo);
            if(branchInfomsg.length() > 0){
                mandatoryMsgs.append(branchInfomsg);
            }
            String mandatoryMsg = mandatoryMsgs.toString();
            if(mandatoryMsg.length() > 0){
                displayAlert(mandatoryMsg);
            }else if((observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) && (tblAuditJobMaster.getRowCount() == 0) ){
                displayAlert("Job Position Table should not be empty!!!");
            }else{
                observable.doActionPerform();
                if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED){
                    setHelpBtnEnable(false);
                    observable.resetForm();
                    resetLabels();
                    resetTable();
                    ClientUtil.clearAll(this);
                    ClientUtil.enableDisable(panInspection, false);
                    setButtonEnableDisable();
                    btnAuthorize.setEnabled(true);
                    btnReject.setEnabled(true);
                    btnException.setEnabled(true);
                    observable.setResultStatus();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        // setTerminalId();
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void btnTabSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTabSaveActionPerformed
        // Add your handling code here:
        final String mandatoryMessage = checkMandatory(panJobPosition);
        /* mandatoryMessage length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
        if (mandatoryMessage.length() > 0){
            displayAlert(mandatoryMessage);
            ClientUtil.enableDisable(panJobPosition, true);
            return;
        }else{
            addCTable();
            txtStaffPosition.setText(observable.fillTxtStaffPosition());
        }
    }//GEN-LAST:event_btnTabSaveActionPerformed
    
    private void tblAuditJobMasterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAuditJobMasterMouseClicked
        // Add your handling code here:
        selectedData = 1;
        updateOBFields();
        observable.populateTblAuditJobMasterModel(tblAuditJobMaster.getSelectedRow());
        if(observable.getActionType()!=ClientConstants.ACTIONTYPE_DELETE){
            ClientUtil.enableDisable(panJobPosition, true);
            btnTabNew.setEnabled(true);
            btnTabSave.setEnabled(true);
            btnTabDelete.setEnabled(true);
            cboJobCategory.setEnabled(false);
            txtActualPosition.setEnabled(true);
            txtActualPosition.setEditable(true);
        }else if(observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE ){
            ClientUtil.enableDisable(panJobPosition, false);
            btnTabNew.setEnabled(false);
            btnTabSave.setEnabled(false);
            btnTabDelete.setEnabled(false);
        }
        
        if(viewType.equals(AUTHORIZE)){
            ClientUtil.enableDisable(panJobPosition, false);
            btnTabNew.setEnabled(false);
            btnTabSave.setEnabled(false);
            btnTabDelete.setEnabled(false);
        }
         if(observable.getActionType()==ClientConstants.ACTIONTYPE_VIEW)
        {
         ClientUtil.enableDisable(panJobPosition, false);
            btnTabNew.setEnabled(false);
            btnTabSave.setEnabled(false);
            btnTabDelete.setEnabled(false);    
        }
    }//GEN-LAST:event_tblAuditJobMasterMouseClicked
    
    private void btnTabNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTabNewActionPerformed
        // Add your handling code here:
        selectedData = 0;
        btnTabSave.setEnabled(true);
        cboJobCategory.setEnabled(true);
        txtActualPosition.setEditable(true);
        txtActualPosition.setEnabled(true);
        cboJobCategory.setSelectedItem("");
        txtActualPosition.setText("");
        
    }//GEN-LAST:event_btnTabNewActionPerformed
    
    private void cboJobCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboJobCategoryActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_cboJobCategoryActionPerformed
    
    private void btnBranchCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBranchCodeActionPerformed
        // Add your handling code here:
        callView("BranchCode");
    }//GEN-LAST:event_btnBranchCodeActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        setModified(true);
        observable.resetForm();
        btnTabNew.setEnabled(true);
        setHelpBtnEnable(true);
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panInspection, true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        cboJobCategory.setEnabled(false);
        txtActualPosition.setEnabled(false);
        txtActualPosition.setEditable(false);
        setButtonEnableDisable();
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnNewActionPerformed
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm
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
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        javax.swing.JFrame frame=new javax.swing.JFrame();
        InspectionUI  tui = new InspectionUI();
        frame.getContentPane().add(tui);
        frame.setSize(650,580);
        frame.show();
        tui.show();
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnBranchCode;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnTabDelete;
    private com.see.truetransact.uicomponent.CButton btnTabNew;
    private com.see.truetransact.uicomponent.CButton btnTabSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboBranchRating;
    private com.see.truetransact.uicomponent.CComboBox cboCategory;
    private com.see.truetransact.uicomponent.CComboBox cboClassification;
    private com.see.truetransact.uicomponent.CComboBox cboJobCategory;
    private com.see.truetransact.uicomponent.CComboBox cboWeeklyHoliday;
    private com.see.truetransact.uicomponent.CLabel lblActualPosition;
    private com.see.truetransact.uicomponent.CLabel lblAuditId;
    private com.see.truetransact.uicomponent.CLabel lblBranchCode;
    private com.see.truetransact.uicomponent.CLabel lblBranchName;
    private com.see.truetransact.uicomponent.CLabel lblBranchRating;
    private com.see.truetransact.uicomponent.CLabel lblCategory;
    private com.see.truetransact.uicomponent.CLabel lblClassification;
    private com.see.truetransact.uicomponent.CLabel lblInspCommOn;
    private com.see.truetransact.uicomponent.CLabel lblInspConcludedOn;
    private com.see.truetransact.uicomponent.CLabel lblInspectingOfficials;
    private com.see.truetransact.uicomponent.CLabel lblJobCategory;
    private com.see.truetransact.uicomponent.CLabel lblManDays;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblOpeningDate;
    private com.see.truetransact.uicomponent.CLabel lblOtherInfo;
    private com.see.truetransact.uicomponent.CLabel lblPositionAsOn;
    private com.see.truetransact.uicomponent.CLabel lblRegion;
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
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStaffPosition;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblValueAuditId;
    private com.see.truetransact.uicomponent.CLabel lblValueBranchCode;
    private com.see.truetransact.uicomponent.CLabel lblValueBranchName;
    private com.see.truetransact.uicomponent.CLabel lblValueName;
    private com.see.truetransact.uicomponent.CLabel lblValueOpeningDate;
    private com.see.truetransact.uicomponent.CLabel lblValueRegion;
    private com.see.truetransact.uicomponent.CLabel lblValueWorkingHours;
    private com.see.truetransact.uicomponent.CLabel lblWeeklyHoliday;
    private com.see.truetransact.uicomponent.CLabel lblWorkingHours;
    private com.see.truetransact.uicomponent.CLabel lblbranchCode;
    private com.see.truetransact.uicomponent.CLabel lblbranchName;
    private com.see.truetransact.uicomponent.CMenuBar mbrInspection;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAppraisal;
    private com.see.truetransact.uicomponent.CPanel panBranchCode;
    private com.see.truetransact.uicomponent.CPanel panBranchDetails;
    private com.see.truetransact.uicomponent.CPanel panBranchInfo;
    private com.see.truetransact.uicomponent.CPanel panDevelopment;
    private com.see.truetransact.uicomponent.CPanel panInspOfficials;
    private com.see.truetransact.uicomponent.CPanel panInspection;
    private com.see.truetransact.uicomponent.CPanel panJobPosition;
    private com.see.truetransact.uicomponent.CPanel panOperations;
    private com.see.truetransact.uicomponent.CPanel panOtherInfo;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private javax.swing.JSeparator sptEdit;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CScrollPane srpAppraisalInfo;
    private com.see.truetransact.uicomponent.CScrollPane srpAuditJobMaster;
    private com.see.truetransact.uicomponent.CScrollPane srpDevelopment;
    private com.see.truetransact.uicomponent.CScrollPane srpInspectingOfficials;
    private com.see.truetransact.uicomponent.CScrollPane srpOtherInfo;
    private com.see.truetransact.uicomponent.CTabbedPane tabInspection;
    private com.see.truetransact.uicomponent.CTable tblAppraisalInfo;
    private com.see.truetransact.uicomponent.CTable tblAuditJobMaster;
    private com.see.truetransact.uicomponent.CTable tblDevelopment;
    private javax.swing.JToolBar tbrInspection;
    private com.see.truetransact.uicomponent.CDateField tdtInspectionCommencedOn;
    private com.see.truetransact.uicomponent.CDateField tdtInspectionConcludedOn;
    private com.see.truetransact.uicomponent.CDateField tdtPositionAsOn;
    private com.see.truetransact.uicomponent.CTextArea txaInspectingOfficials;
    private com.see.truetransact.uicomponent.CTextArea txaOtherInformation;
    private com.see.truetransact.uicomponent.CTextField txtActualPosition;
    private com.see.truetransact.uicomponent.CTextField txtBranchCode;
    private com.see.truetransact.uicomponent.CTextField txtNumberOfManDays;
    private com.see.truetransact.uicomponent.CTextField txtStaffPosition;
    // End of variables declaration//GEN-END:variables
    
}
