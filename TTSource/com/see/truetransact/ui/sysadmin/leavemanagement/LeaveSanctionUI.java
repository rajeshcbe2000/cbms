/**
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LeaveSanctionUI.java
 *
 * Created on march 31, 2010, 11:00 AM
 */

package com.see.truetransact.ui.sysadmin.leavemanagement;

/**
 *
 * @author Swaroop
 */
import java.util.ArrayList;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uivalidation.PercentageValidation;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CTextField;
import com.see.truetransact.clientutil.ComboBoxModel;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.Date;
import java.util.List;
import com.see.truetransact.uicomponent.CButtonGroup;
import com.see.truetransact.uivalidation.DefaultValidation;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com .see.truetransact.uivalidation.ToDateValidation;
public class LeaveSanctionUI extends CInternalFrame implements UIMandatoryField, Observer{
    
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private HashMap mandatoryMap;
    private LeaveSanctionOB observable;
    private LeaveSanctionMRB objMandatoryRB;
    private ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.sysadmin.leavemanagement.LeaveSanctionRB", ProxyParameters.LANGUAGE);
    private String viewType = new String();
    final String AUTHORIZE="Authorize";
    private boolean isFilled = false;
    private boolean dataExists = false;
    private int tblRowCount;
    private boolean loanTypeExists = false;
    private int existingRowcount;
    final int LEAVE_REQ=0,LEAVE_SAN=1;
    int pan=-1;
    int updateTab=-1;
    private boolean updateMode = false;
    Date extDate= null;
    private Date currDt = null;
    /** Creates new form LeaveSanctionUI */
    public LeaveSanctionUI() {
        currDt = ClientUtil.getCurrentDate();
        initUIComponents();
    }
    
    /** Initialsises the UIComponents */
    private void initUIComponents(){
        initComponents();
        setFieldNames();
        setMaxLength();
        internationalize();
        setMandatoryHashMap();
        setHelpMessage();
        setObservable();
        observable.resetForm();
        initComponentData();
        ClientUtil.enableDisable(panLeaveManagement, false);
        setButtonEnableDisable();
        setButtonReset(false);
        txtTabDays.setEnabled(false);
        cbotblLeaveType.setEnabled(false);
        tabShareProduct.remove(panApplication);
        tabShareProduct.add(panApplSan,"Leave Appl/San",0);
        tabShareProduct.add(panLeaveReq,"Leave Process",1);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panSanEmp);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panEnq);
    }
    private void setButtonReset(boolean enableValue){
        btnEnq.setEnabled(enableValue);
        btnCalculate.setEnabled(enableValue);
        btnLeaveSanNew.setEnabled(enableValue);
        btnLeaveSanSave.setEnabled(enableValue);
        btnLeaveSanDelete.setEnabled(enableValue);
        btnRejAppl.setEnabled(enableValue);
        btnEmp.setEnabled(enableValue);
        btnProcess.setEnabled(enableValue);
        btnEmpProcess.setEnabled(enableValue);
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
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        panLeaveReq.setName("panLeave");
        panLeaveManagement.setName("panLeaveManagement");
        cboLeaveType.setName("cboLeaveType");
        txtEmpID.setName("txtEmpID");
        btnProcess.setName("btnProcess");
        cboProcessType.setName("cboProcessType");
        tdtLvAplDt.setName("tdtLvAplDt");
        tdtReqFrom.setName("tdtReqFrom");
        tdtReqTo.setName("tdtReqTo");
        txtNoOfDays.setName("txtNoOfDays");
        txtLeavePurpose.setName("txtLeavePurpose");
        txtSanRefNo.setName("txtSanRefNo");
        tdtSanDate.setName("tdtSanDate");
        panApplSan.setName("panApplSan");
        panAppSan.setName("panAppSan");
        panBtnLeaveSan.setName("panBtnLeaveSan");
        btnLeaveSanNew.setName("btnLeaveSanNew");
        btnLeaveSanSave.setName("btnLeaveSanSave");
        btnLeaveSanDelete.setName("btnLeaveSanDelete");
        panBtnLeaveSan.setName("panBtnLeaveSan");
        cbotblLeaveType.setName("cbotblLeaveType");
        tblSan.setName("tblSan");
        panSanTable.setName("panSanTable");
        txtSanEmpID.setName("txtSanEmpID");
        btnEmp.setName("btnEmp");
        btnEnq.setName("btnEnq");
        lblLeaveNextCrDt.setName("lblLeaveNextCrDt");
        lblLeaveNextCrDt1.setName("lblLeaveNextCrDt1");
        lblLeaveBalanceAsOn.setName("lblLeaveBalanceAsOn");
        cboLeaveType1.setName("cboLeaveType1");
        lblLeaveLastCrDt.setName("lblLeaveLastCrDt");
        lblLeaveBalanceAsOnVal.setName("lblLeaveBalanceAsOnVal");
        txtOldSanRefNo.setName("txtOldSanRefNo");
        tdtTblReqTo.setName("tdtTblReqTo");
        tdtTblReqFrom.setName("tdtTblReqFrom");
        lblTblReqFrom.setName("lblTblReqFrom");
        lblTblReqTo.setName("lblTblReqTo");
        chkLtc.setName("chkLtc");
        chkLeaveEncash.setName("chkLeaveEncash");
        lblWithLtc.setName("lblWithLtc");
        lblEnCashDays.setName("lblEnCashDays");
        lblLeavEncash.setName("lblLeavEncash");
        txtLeavEncah.setName("txtLeavEncah");
        panBlock.setName("panBlock");
        lblTypeOfBlk.setName("lblTypeOfBlk");
        lblWithLtc.setName("lblWithLtc");
        panApplication.setName("panApplication");
        cboEncashMentData.setName("cboEncashMentData");
        lblLeaveID1.setName("lblLeaveID1");
        lblLeaveID.setName("lblLeaveID");
        chkLeaveCancel.setName("chkLeaveCancel");
        chkLeaveCancel1.setName("chkLeaveCancel1");
        lblLeaveCancel.setName("lblLeaveCancel");
        lblLeaveCancel.setName("lblLeaveCancel1");
        txtRemarks.setName("txtRemarks");
        txtRemarks1.setName("txtRemarks1");
        lblRemarks.setName("lblRemarks");
        lblRemarks1.setName("lblRemarks1");
        lblSanAuth1.setName("lblSanAuth1");
        lblSanAuth.setName("lblSanAuth");
        cboSanAuth.setName("cboSanAuth");
        cboSanAuth1.setName("cboSanAuth1");
    }
    
    
    /* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        btnClose.setText(resourceBundle.getString("btnClose"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        
        lblSanEmpID.setText(resourceBundle.getString("lblSanEmpID"));
        lblLeaveBalanceAsOn.setText(resourceBundle.getString("lblLeaveBalanceAsOn"));
        lblLeaveLastCrDt.setText(resourceBundle.getString("lblLeaveLastCrDt"));
        lblLeaveNextCrDt1.setText(resourceBundle.getString("lblLeaveNextCrDt1"));
        lblLeaveType9.setText(resourceBundle.getString("lblLeaveType9"));
        lblPFNumber7.setText(resourceBundle.getString("lblPFNumber7"));
        lblLoanSanctionDate9.setText(resourceBundle.getString("lblLoanSanctionDate9"));
        lblDateOfJoin10.setText(resourceBundle.getString("lblDateOfJoin10"));
        lblDateOfJoin9.setText(resourceBundle.getString("lblDateOfJoin9"));
        lblPFNumber6.setText(resourceBundle.getString("lblPFNumber6"));
        lblLoanRateofInterest23.setText(resourceBundle.getString("lblLoanRateofInterest23"));
        lblPFNumber5.setText(resourceBundle.getString("lblPFNumber5"));
        lblReportedOn3.setText(resourceBundle.getString("lblReportedOn3"));
        lblEnqLeaveType.setText(resourceBundle.getString("lblEnqLeaveType"));
        btnRejAppl.setText(resourceBundle.getString("btnRejAppl"));
        lblTabDays.setText(resourceBundle.getString("lblTabDays"));
        lblTabLeaveType.setText(resourceBundle.getString("lblTabLeaveType"));
        btnEnq.setText(resourceBundle.getString("btnEnq"));
    }
    
/* Auto Generated Method - setMandatoryHashMap()
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtSanEmpID", new Boolean(true));
        mandatoryMap.put("cboProcessType", new Boolean(true));
        mandatoryMap.put("tdtLvAplDt", new Boolean(true));
        mandatoryMap.put("tdtReqFrom", new Boolean(true));
        mandatoryMap.put("tdtReqTo", new Boolean(true));
        mandatoryMap.put("txtNoOfDays", new Boolean(true));
        mandatoryMap.put("txtLeavePurpose", new Boolean(false));
        mandatoryMap.put("txtSanRefNo", new Boolean(false));
        mandatoryMap.put("tdtSanDate", new Boolean(false));
    }
    
/* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    
    /* Creates the insstance of LeaveSanction which acts as  Observable to
     *LeaveSanction UI */
    private void setObservable() {
        try{
            observable = LeaveSanctionOB.getInstance();
            observable.addObserver(this);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /* Sets the model for the comboboxes in the UI    */
    private void initComponentData() {
        try{
            cbotblLeaveType.setModel(observable.getCbmtblLeaveType());
            cbotblLeaveType1.setModel(observable.getCbmtblLeaveType1());
            cboProcessType.setModel(observable.getCbmProcessType());
            cboProcessType1.setModel(observable.getCbmProcessType());
            cboLeaveType.setModel(observable.getCbmLeaveTypeProcess());
            cboLeaveType1.setModel(observable.getCbmLeaveTypeEnquiry());
            cboEncashMentData.setModel(observable.getCbmLeaveTypeEnquiry());
            cboEncashMentData1.setModel(observable.getCbmLeaveTypeEnquiry());
            cboPayType.setModel(observable.getCbmPayType());
            cboSanAuth.setModel(observable.getCbmSanAuth());
            cboSanAuth1.setModel(observable.getCbmSanAuth());
        }catch(ClassCastException e){
            parseException.logException(e,true);
        }
    }
    
/* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        objMandatoryRB = new LeaveSanctionMRB();
        
    }
    
    
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        removeRadioButtons();
        cboProcessType.setModel(observable.getCbmProcessType());
        tdtLvAplDt.setDateValue(observable.getAppl_dt());
        tdtReqFrom.setDateValue(observable.getReq_from());
        tdtReqTo.setDateValue(observable.getReq_to());
        txtNoOfDays.setText(observable.getNoOfdays());
        txtLeavePurpose.setText(observable.getLeavePurpose());
        tdtSanDate.setDateValue(observable.getSanDate());
        txtSanRefNo.setText(observable.getSanNo());
        rdoApp_Yes.setSelected(rdoApp_Yes.isSelected());
        rdoSan_Yes.setSelected(rdoSan_Yes.isSelected());
        txtSanEmpID.setText(observable.getEmpID());
        txtTabDays.setText(observable.getTabNoOfDays());
        cbotblLeaveType.setModel(observable.getCbmtblLeaveType());
        tblSan.setModel(observable.getTblLeaveDetails());
        txtOldSanRefNo.setText(observable.getOldSanNo());
        tdtTblReqFrom.setDateValue(observable.getTblreq_from());
        tdtTblReqTo.setDateValue(observable.getTblreq_to());
        rdoTwoYr.setSelected(observable.isRdoTwoYr());
        rdoFourYr.setSelected(observable.isRdoFourYr());
        chkLeaveEncash.setSelected(observable.isWithLeaveEncashment());
        chkLtc.setSelected(observable.isWithLtc());
        txtLeavEncah.setText(observable.getLeaveEncashmentDays());
        cboPayType.setModel(observable.getCbmPayType());
        cboEncashMentData.setModel(observable.getCbmEncashmentData());
        lblLeaveID.setText(observable.getLeaveID());
        chkLeaveCancel.setSelected(observable.isLeaveCancel());
        txtRemarks.setText(observable.getRemarks());
        cboSanAuth.setModel(observable.getCbmSanAuth());
        
        cboProcessType1.setModel(observable.getCbmProcessType1());
        tdtLvAplDt1.setDateValue(observable.getAppl_dt1());
        tdtReqFrom1.setDateValue(observable.getReq_from1());
        tdtReqTo1.setDateValue(observable.getReq_to1());
        txtNoOfDays1.setText(observable.getNoOfdays1());
        txtLeavePurpose1.setText(observable.getLeavePurpose1());
        tdtSanDate1.setDateValue(observable.getSanDate1());
        txtSanRefNo1.setText(observable.getSanNo1());
        //        rdoApp_Yes.setSelected(rdoApp_Yes.isSelected());
        //        rdoSan_Yes.setSelected(rdoSan_Yes.isSelected());
        txtSanEmpID1.setText(observable.getEmpID1());
        txtTabDays1.setText(observable.getTabNoOfDays1());
        cbotblLeaveType1.setModel(observable.getCbmtblLeaveType1());
        tblSan1.setModel(observable.getTblLeaveDetails1());
        txtOldSanRefNo1.setText(observable.getOldSanNo1());
        tdtTblReqFrom1.setDateValue(observable.getTblreq_from1());
        tdtTblReqTo1.setDateValue(observable.getTblreq_to1());
        rdoTwoYr1.setSelected(observable.isRdoTwoYr1());
        rdoFourYr1.setSelected(observable.isRdoFourYr1());
        chkLeaveEncash1.setSelected(observable.isWithLeaveEncashment1());
        chkLtc1.setSelected(observable.isWithLtc1());
        txtLeavEncah1.setText(observable.getLeaveEncashmentDays1());
        cboPayType1.setModel(observable.getCbmPayType1());
        cboEncashMentData1.setModel(observable.getCbmEncashmentData1());
        lblLeaveID1.setText(observable.getLeaveID());
        chkLeaveCancel1.setSelected(observable.isLeaveCancel1());
        txtRemarks1.setText(observable.getRemarks1());
        cboSanAuth1.setModel(observable.getCbmSanAuth1());
        addRadioButtons();
    }
    
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setCboProcessType((String) cboProcessType.getSelectedItem());
        observable.setAppl_dt(tdtLvAplDt.getDateValue());
        observable.setReq_from(tdtReqFrom.getDateValue());
        observable.setReq_to(tdtReqTo.getDateValue());
        observable.setNoOfdays(txtNoOfDays.getText());
        observable.setLeavePurpose(txtLeavePurpose.getText());
        observable.setSanNo(txtSanRefNo.getText());
        observable.setSanDate(tdtSanDate.getDateValue());
        observable.setRdoApp_Yes(rdoApp_Yes.isSelected());
        observable.setRdoSan_Yes(rdoSan_Yes.isSelected());
        observable.setEmpID(txtSanEmpID.getText());
        observable.setTabNoOfDays(txtTabDays.getText());
        observable.setCbotblLeaveType((String) cbotblLeaveType.getSelectedItem());
        observable.setOldSanNo(txtOldSanRefNo.getText());
        observable.setTblreq_from(tdtTblReqFrom.getDateValue());
        observable.setTblreq_to(tdtTblReqTo.getDateValue());
        observable.setRdoTwoYr(rdoTwoYr.isSelected());
        observable.setRdoFourYr(rdoFourYr.isSelected());
        observable.setWithLtc(chkLtc.isSelected());
        observable.setWithLeaveEncashment(chkLeaveEncash.isSelected());
        observable.setLeaveEncashmentDays(txtLeavEncah.getText());
        observable.setCboPayType((String) cboPayType.getSelectedItem());
        observable.setCboEncashmentData((String) cboEncashMentData.getSelectedItem());
        observable.setLeaveCancel(chkLeaveCancel.isSelected());
        observable.setRemarks(txtRemarks.getText());
        observable.setCboSanAuth((String) cboSanAuth.getSelectedItem());
    }
    
    /** Enabling and Disabling of Buttons after Save,Edit,Delete operations */
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
        lblStatus.setText(observable.getLblStatus());
        btnView.setEnabled(!btnView.isEnabled());
        
    }
    
    private void addRadioButtons() {
        rdoApplSan = new CButtonGroup();
        rdoApplSan.add(rdoApp_Yes);
        rdoApplSan.add(rdoSan_Yes);
        
        rdoBlock =new CButtonGroup();
        rdoBlock.add(rdoTwoYr);
        rdoBlock.add(rdoFourYr);
    }
    
    private void removeRadioButtons() {
        rdoApplSan.remove(rdoApp_Yes);
        rdoApplSan.remove(rdoSan_Yes);
        rdoBlock.remove(rdoTwoYr);
        rdoBlock.remove(rdoFourYr);
    }
    /** Setting up Lengths for the TextFields in theu UI */
    private void setMaxLength(){
        txtTabDays.setValidation(new NumericValidation());
        txtSanEmpID.setAllowAll(true);
    }
    
    /* Does necessary operaion when user clicks the save button */
    private void savePerformed(String str){
        updateOBFields();
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panApplSan);
        if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0 ){
            displayAlert(mandatoryMessage);
        }
        else{
            int noOfRows=tblSan.getRowCount();
            String proType=(((ComboBoxModel)cboProcessType.getModel()).getKeyForSelected().toString());
            if(rdoSan_Yes.isSelected() && !proType.equalsIgnoreCase("MODIFICATION")){
                for(int i=0;i<noOfRows;i++){
                    String leaveType =CommonUtil.convertObjToStr(tblSan.getValueAt(i,1));
                    if(!leaveType.equalsIgnoreCase("LOSS OF PAY")){
                        int noOfday =CommonUtil.convertObjToInt(tblSan.getValueAt(i,2));
                        HashMap where1 = new HashMap();
                        where1.put("LEAVE_TYPE",leaveType);
                        where1.put("EMP_ID",txtSanEmpID.getText());
                        List remainLeaves=ClientUtil.executeQuery("getRemainingLeaves",where1);
                        if(remainLeaves!=null && remainLeaves.size()>0){
                            int a=CommonUtil.convertObjToInt(remainLeaves.get(0));
                            if(a<noOfday){
                                ClientUtil.displayAlert(leaveType+" "+"Request Cannot Be Greater Than"+"  "+a+" "+"Days"+ '\n'+ "Since The Balance of CL is Just"+" "+a);
                                return;
                            }
                        }
                        else{
                            ClientUtil.displayAlert("No leaves For This Leave Type");
                            return;
                        }
                    }
                }
            }
            if(rdoApp_Yes.isSelected()){
                if(!chkLeaveCancel.isSelected()){
                    if(txtLeavePurpose.getText().length()<=0){
                        ClientUtil.displayAlert("Enter Purpose Of Leave");
                        return;
                    }
                }
                int totNoOfdays=0;
                for(int i=0;i<noOfRows;i++){
                    totNoOfdays=totNoOfdays+CommonUtil.convertObjToInt(tblSan.getValueAt(i,2));
                }
                if(totNoOfdays!= CommonUtil.convertObjToInt(txtNoOfDays.getText())){
                    ClientUtil.displayAlert("No Of Days Should Be Equal To The Total No oF Days In The Table");
                    return;
                }
            }
            else if(str.equalsIgnoreCase("SAVE") && observable.getActionType()!=ClientConstants.ACTIONTYPE_DELETE){
                if(txtSanRefNo.getText().length()<=0){
                    ClientUtil.displayAlert("Enter Sanction Number");
                    return;
                }
                if(tdtSanDate.getDateValue().length()<=0){
                    ClientUtil.displayAlert("Enter Sanction Date");
                    return;
                }
                String sanAuth=(((ComboBoxModel)cboSanAuth.getModel()).getKeyForSelected().toString());
                if(sanAuth.length()<=0){
                    ClientUtil.displayAlert("Enter Sanctioning Authority");
                    return;
                }
                
            }
            if(chkLeaveEncash.isSelected() && ((txtLeavEncah.getText().length()<=0) || cboEncashMentData.getSelectedIndex()<=0)){
                ClientUtil.displayAlert("Number Of days And The Leave Type Should Be Entered For Leave Encashment");
                return;
            }
            String action;
            if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW ){
                action=CommonConstants.TOSTATUS_INSERT;
                saveAction(action);
            }else if(observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                action=CommonConstants.TOSTATUS_UPDATE;
                saveAction(action);
            }else if(observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE){
                action=CommonConstants.TOSTATUS_DELETE;
                saveAction(action);
            }
        }
    }
    
    /* Calls the execute method of LeaveSanctionOB to do insertion or updation or deletion */
    private void saveAction(String status){
        //To check mandtoryness of the Terminal panAcHdDetails,panAcHeadDetails panel and diplay appropriate
        //error message, else proceed
        try{
            //            final String mandatoryMessage = checkMandatory(panLeave);
            //            String alertMsg ="";
            //            if(mandatoryMessage.length() > 0 ){
            //                displayAlert(mandatoryMessage);
            //            }else{
            observable.execute(status,"SAVE");
            //__ if the Action is not Falied, Reset the fields...
            if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED){
                HashMap lockMap = new HashMap();
                ArrayList lst = new ArrayList();
                lst.add("LEAVE_ID");
                lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
                if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW) {
                    lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
                    if (observable.getProxyReturnMap()!=null) {
                        if (observable.getProxyReturnMap().containsKey("LEAVE_ID")) {
                            lockMap.put("LEAVE_ID",observable.getProxyReturnMap().get("LEAVE_ID"));
                        }
                    }
                }
                if(observable.getResult()==ClientConstants.ACTIONTYPE_EDIT){
                    //                    lockMap.put("LEAVE_ID", observable.getLeaveID());
                }
                setEditLockMap(lockMap);
                setEditLock();
                settings();
                
            }
            //            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    /** This will checks whether the Mandatory fields in the UI are filled up, If not filled up
     *it will retun an MandatoryMessage*/
    private String checkMandatory(javax.swing.JComponent component){
        return new MandatoryCheck().checkMandatory(getClass().getName(), component);
    }
    
    /** This will Display the Mandatory Message in a Dialog Box */
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    /* set the screen after the updation,insertion, deletion */
    private void settings(){
        observable.resetForm();
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panLeaveManagement, false);
        setButtonEnableDisable();
        observable.setResultStatus();
    }
    
    /** This will show a popup screen which shows all tbe Rows.of the table */
    private void callView(String currField) {
        viewType = currField;
        HashMap viewMap = new HashMap();
        if (currField.equalsIgnoreCase("APPL")) {
            HashMap map = new HashMap();
            map.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getSelectApplLeave");
        }else if (currField.equalsIgnoreCase("SAN")) {
            HashMap map = new HashMap();
            map.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getSelectSanLeaveEdit");
        }else if (currField.equalsIgnoreCase("NEW_SAN")) {
            HashMap map = new HashMap();
            map.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getSelectSanLeave");
        } else if (currField.equalsIgnoreCase("VIEW")) {
            HashMap map = new HashMap();
            map.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getDataForEnq");
        } else if(currField.equalsIgnoreCase("EMP")||currField.equalsIgnoreCase("EMP_PROCESS")){
            HashMap map = new HashMap();
            map.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "setEmpDetails");
        } else if (currField.equalsIgnoreCase("Delete")) {
            HashMap map = new HashMap();
            map.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getLeaveSanDeleteList");
        }
        new ViewAll(this,viewMap).show();
    }
    
    /* This method is used to fill up all tbe UIFields after the user
     *selects the desired row in the popup */
    public void fillData(Object  map) {
        setModified(true);
        isFilled = true;
        HashMap hash = (HashMap) map;
        if(viewType.equals("VIEW")){
            removeRadioButtons();
            rdoApp_Yes.setSelected(false);
            rdoSan_Yes.setSelected(false);
            addRadioButtons();
        }
        if(viewType.equals("NEW_SAN")||viewType.equals("APPL") || rdoApp_Yes.isSelected()){
            hash.put("APPLICATION","APPLICATION");
        }else if(viewType.equals("SAN")|| rdoSan_Yes.isSelected()){
            hash.put("SANCTION","SANCTION");
        } else if((viewType.equals(AUTHORIZE)) || viewType.equals("VIEW") || viewType.equals("Delete")){
            hash.put("APPLICATION","APPLICATION");
            hash.put("SANCTION","SANCTION");
        }
        if (viewType != null) {
            if (viewType.equals("APPL") ||
            viewType.equals("SAN")|| viewType.equals("NEW_SAN") || viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) ||
            viewType.equals("VIEW")) {
                hash.put(CommonConstants.MAP_WHERE, hash.get("LEAVE_APPL_ID"));
                hash.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                observable.populateData(hash);
                if (viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) ||
                viewType.equals("VIEW") || viewType.equals("Delete")) {
                    ClientUtil.enableDisable(panLeaveManagement, false);
                    ClientUtil.enableDisable(panBtnEmployeeLoan, false);
                    ClientUtil.enableDisable(panSanEmp, false);
                    ClientUtil.enableDisable(panApplication, false);
                    ClientUtil.enableDisable(panAppSan, false);
                    panAppSan.setVisible(false);
                    ClientUtil.enableDisable(panApplSan, false);
                    ClientUtil.enableDisable(panBtnLeaveSan, false);
                    btnEmp.setEnabled(false);
                    btnEmp1.setEnabled(false);
                    btnRejAppl.setEnabled(false);
                    btnLeaveSanNew.setEnabled(false);
                    btnLeaveSanSave.setEnabled(false);
                    btnLeaveSanDelete.setEnabled(false);
                    btnLeaveSanNew1.setEnabled(false);
                    btnLeaveSanSave1.setEnabled(false);
                    btnLeaveSanDelete1.setEnabled(false);
                } else {
                    ClientUtil.enableDisable(panLeaveManagement, true);
                    ClientUtil.enableDisable(panBtnLeaveSan, false);
                    btnLeaveSanNew.setEnabled(true);
                    btnLeaveSanSave.setEnabled(true);
                    btnLeaveSanDelete.setEnabled(true);
                    panAppSan.setVisible(true);
                    if(rdoSan_Yes.isSelected()){
                        ClientUtil.enableDisable(panLeaveSan, false);
                        txtSanRefNo.setEnabled(true);
                        tdtSanDate.setEnabled(true);
                        cboSanAuth.setEnabled(true);
                        txtRemarks.setEnabled(true);
                    }
                }
                setButtonEnableDisable();
                btnSave.setEnabled(true);
                btnCancel.setEnabled(true);
                if(viewType.equals("VIEW")){
                    btnCheck();
                }
                if( viewType.equals("NEW_SAN") || viewType.equals("SAN")){
                    rdoApp_Yes.setEnabled(false);
                    if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW) {
                        btnNew.setEnabled(false);
                        btnEdit.setEnabled(false);
                        btnDelete.setEnabled(false);
                    }
                }
                if(viewType ==  AUTHORIZE) {
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                }
            }
            
        }
        if(viewType.equalsIgnoreCase("EMP")){
            txtSanEmpID.setText(CommonUtil.convertObjToStr(hash.get("EMPLOYEE_CODE")));
            lblEmpName.setText(CommonUtil.convertObjToStr(hash.get("EMP_NAME")));
        }
        if(viewType.equalsIgnoreCase("EMP_PROCESS")){
            txtEmpID.setText(CommonUtil.convertObjToStr(hash.get("EMPLOYEE_CODE")));
        }
        
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
            String lockedBy = "";
            HashMap Lockmap = new HashMap();
            Lockmap.put("SCREEN_ID", getScreenID());
            Lockmap.put("RECORD_KEY", CommonUtil.convertObjToStr(hash.get("LEAVE_ID")));
            Lockmap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
            System.out.println("Record Key Map : " + Lockmap);
            List lstLock = ClientUtil.executeQuery("selectEditLock", Lockmap);
            if (lstLock.size() > 0) {
                lockedBy = CommonUtil.convertObjToStr(lstLock.get(0));
                if (!lockedBy.equals(ProxyParameters.USER_ID)) {
                    btnSave.setEnabled(false);
                } else {
                    btnSave.setEnabled(true);
                }
            } else {
                btnSave.setEnabled(true);
            }
            setOpenForEditBy(lockedBy);
            if (lockedBy.equals(""))
                ClientUtil.execute("insertEditLock", Lockmap);
            if (lockedBy.length() > 0 && !lockedBy.equals(ProxyParameters.USER_ID)) {
                String data = getLockDetails(lockedBy, getScreenID()) ;
                ClientUtil.showMessageWindow("Selected Record is Opened/Modified by " + lockedBy + data.toString());
                btnSave.setEnabled(false);
            }
            
        }
        if( observable.getSanStatus()!=null && observable.getSanStatus().length()>0){
            ClientUtil.displayAlert("Leave Not Sanctioned");
            ClientUtil.enableDisable(panLeaveManagement, false);
            ClientUtil.enableDisable(panBtnEmployeeLoan, false);
            ClientUtil.enableDisable(panSanEmp, false);
            ClientUtil.enableDisable(panApplication, false);
            ClientUtil.enableDisable(panAppSan, false);
            ClientUtil.enableDisable(panApplSan, false);
            ClientUtil.enableDisable(panBtnLeaveSan, false);
            btnEmp.setEnabled(false);
            btnEmp1.setEnabled(false);
            btnRejAppl.setEnabled(false);
            btnSave.setEnabled(false);
        }
        
    }
    
    private String getLockDetails(String lockedBy, String screenId){
        HashMap map = new HashMap();
        StringBuffer data = new StringBuffer() ;
        map.put("LOCKED_BY", lockedBy) ;
        map.put("SCREEN_ID", screenId) ;
        java.util.List lstLock = ClientUtil.executeQuery("getLockedDetails", map);
        map.clear();
        if(lstLock.size() > 0){
            map = (HashMap)(lstLock.get(0));
            data.append("\nLog in Time : ").append(map.get("LOCKED_TIME")) ;
            data.append("\nIP Address : ").append(map.get("IP_ADDR")) ;
            data.append("\nBranch : ").append(map.get("BRANCH_ID"));
        }
        lstLock = null ;
        map = null ;
        return data.toString();
    }
    
    /** This will do necessary operation for authorization **/
    public void authorizeStatus(String authorizeStatus) {
        if (!viewType.equals(AUTHORIZE) && !isFilled){
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            mapParam.put(CommonConstants.MAP_NAME, "getLeaveSanAuthorizeList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeSanAppl");
            mapParam.put(CommonConstants.UPDATE_MAP_WHERE, "authorizeLeaveSanTab");
            mapParam.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            setModified(true);
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
//            btnCancelActionPerformed(null);
            observable.setResultStatus();
            
        } else if (viewType.equals(AUTHORIZE) && isFilled){
            ArrayList arrList = new ArrayList();
            HashMap authorizeMap = new HashMap();
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
            singleAuthorizeMap.put("AUTH_BY", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDt.clone());
            singleAuthorizeMap.put("AUTH_DT",currDt.clone()) ;
            singleAuthorizeMap.put("LEAVE_APPL_ID", observable.getLeaveID());
            singleAuthorizeMap.put("SANCTION_NO", observable.getSanNo());
            singleAuthorizeMap.put("OLD_SANCTION_NO", observable.getOldSanNo());
            singleAuthorizeMap.put("SANCTION", "SANCTION");
            if(observable.isLeaveCancel()){
                singleAuthorizeMap.put("LEAVE_CANCEL", "LEAVE_CANCEL");
            }
            if( observable.getSanStatus()!=null && observable.getSanStatus().length()>0){
                singleAuthorizeMap.put("SAN_STATUS_REJECT", "SAN_STATUS_REJECT");
            }
            arrList.add(singleAuthorizeMap);
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorize(authorizeMap);
            viewType = "";
            super.setOpenForEditBy(observable.getStatusBy());
            btnCancelActionPerformed(null);
            isFilled = false;
        }
    }
    
    public void authorize(HashMap map) {
        System.out.println("Authorize Map : " + map);
        
        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
            observable.setResult(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setEncashLeaveType((((ComboBoxModel)cboEncashMentData.getModel()).getKeyForSelected().toString()));
            observable.set_authorizeMap(map);
            observable.execute("AUTHORIZE","AUTHORIZE");
            btnCancelActionPerformed(null);
            observable.setStatus();
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
            
        }
    }
    
    public static void main(String args[]){
        javax.swing.JFrame frame = new javax.swing.JFrame();
        LeaveSanctionUI ui = new LeaveSanctionUI();
        frame.getContentPane().add(ui);
        ui.setVisible(true);
        frame.setVisible(true);
        frame.setSize(450,600);
        frame.show();
        ui.show();
        
    }
    
    
    private void deletescreenLock(){
        HashMap map=new HashMap();
        map.put("USER_ID",ProxyParameters.USER_ID);
        map.put("TRANS_DT", currDt.clone());
        map.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
        ClientUtil.execute("DELETE_SCREEN_LOCK", map);
    }
    public void enableDisablePanSan(boolean flag){
        cbotblLeaveType.setEnabled(flag);
        txtTabDays.setEnabled(flag);
        btnLeaveSanSave.setEnabled(flag);
        btnLeaveSanDelete.setEnabled(flag);
        cboPayType.setEnabled(flag);
        tdtTblReqFrom.setEnabled(flag);
        tdtTblReqTo.setEnabled(flag);
    }
    public void enableDisablePanSanAppl(boolean flag){
        cbotblLeaveType1.setEnabled(flag);
        txtTabDays1.setEnabled(flag);
        btnLeaveSanSave1.setEnabled(flag);
        btnLeaveSanDelete1.setEnabled(flag);
        cboPayType1.setEnabled(flag);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoApplSan = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoBlock = new com.see.truetransact.uicomponent.CButtonGroup();
        panLeaveManagement = new com.see.truetransact.uicomponent.CPanel();
        tabShareProduct = new com.see.truetransact.uicomponent.CTabbedPane();
        panLeaveReq = new com.see.truetransact.uicomponent.CPanel();
        cboLeaveType = new com.see.truetransact.uicomponent.CComboBox();
        lblLeaveType = new com.see.truetransact.uicomponent.CLabel();
        lblEmpID = new com.see.truetransact.uicomponent.CLabel();
        panAccAllowedPeriod = new com.see.truetransact.uicomponent.CPanel();
        txtEmpID = new com.see.truetransact.uicomponent.CTextField();
        btnProcess = new com.see.truetransact.uicomponent.CButton();
        btnEmpProcess = new com.see.truetransact.uicomponent.CButton();
        panApplSan = new com.see.truetransact.uicomponent.CPanel();
        panSanTable = new com.see.truetransact.uicomponent.CPanel();
        srpFamily6 = new com.see.truetransact.uicomponent.CScrollPane();
        tblSan = new com.see.truetransact.uicomponent.CTable();
        btnRejAppl = new com.see.truetransact.uicomponent.CButton();
        panLeaveSan = new com.see.truetransact.uicomponent.CPanel();
        cboProcessType = new com.see.truetransact.uicomponent.CComboBox();
        txtOldSanRefNo = new com.see.truetransact.uicomponent.CTextField();
        tdtLvAplDt = new com.see.truetransact.uicomponent.CDateField();
        lblLoanSanctionDate9 = new com.see.truetransact.uicomponent.CLabel();
        lblDateOfJoin10 = new com.see.truetransact.uicomponent.CLabel();
        tdtReqFrom = new com.see.truetransact.uicomponent.CDateField();
        lblDateOfJoin9 = new com.see.truetransact.uicomponent.CLabel();
        tdtReqTo = new com.see.truetransact.uicomponent.CDateField();
        txtNoOfDays = new com.see.truetransact.uicomponent.CTextField();
        txtLeavePurpose = new com.see.truetransact.uicomponent.CTextField();
        lblLoanRateofInterest23 = new com.see.truetransact.uicomponent.CLabel();
        lblLeaveType9 = new com.see.truetransact.uicomponent.CLabel();
        tdtSanDate = new com.see.truetransact.uicomponent.CDateField();
        lblReportedOn3 = new com.see.truetransact.uicomponent.CLabel();
        txtSanRefNo = new com.see.truetransact.uicomponent.CTextField();
        lblPFNumber5 = new com.see.truetransact.uicomponent.CLabel();
        lblPFNumber6 = new com.see.truetransact.uicomponent.CLabel();
        lblPFNumber7 = new com.see.truetransact.uicomponent.CLabel();
        lblWithLtc = new com.see.truetransact.uicomponent.CLabel();
        chkLeaveCancel = new com.see.truetransact.uicomponent.CCheckBox();
        panBlock = new com.see.truetransact.uicomponent.CPanel();
        rdoTwoYr = new com.see.truetransact.uicomponent.CRadioButton();
        rdoFourYr = new com.see.truetransact.uicomponent.CRadioButton();
        chkLeaveEncash = new com.see.truetransact.uicomponent.CCheckBox();
        lblLeavEncash = new com.see.truetransact.uicomponent.CLabel();
        txtLeavEncah = new com.see.truetransact.uicomponent.CTextField();
        lblTypeOfBlk = new com.see.truetransact.uicomponent.CLabel();
        lblEnCashDays = new com.see.truetransact.uicomponent.CLabel();
        cboEncashMentData = new com.see.truetransact.uicomponent.CComboBox();
        panAppSan = new com.see.truetransact.uicomponent.CPanel();
        rdoApp_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoSan_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        chkLtc = new com.see.truetransact.uicomponent.CCheckBox();
        lblLeaveCancel = new com.see.truetransact.uicomponent.CLabel();
        txtRemarks = new com.see.truetransact.uicomponent.CTextField();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        lblSanAuth = new com.see.truetransact.uicomponent.CLabel();
        cboSanAuth = new com.see.truetransact.uicomponent.CComboBox();
        panEnq = new com.see.truetransact.uicomponent.CPanel();
        lblLeaveNextCrDt1 = new com.see.truetransact.uicomponent.CLabel();
        lblLeaveLastCrDt = new com.see.truetransact.uicomponent.CLabel();
        lblLeaveBalanceAsOn = new com.see.truetransact.uicomponent.CLabel();
        lblEnqLeaveType = new com.see.truetransact.uicomponent.CLabel();
        cboLeaveType1 = new com.see.truetransact.uicomponent.CComboBox();
        btnEnq = new com.see.truetransact.uicomponent.CButton();
        lblLeaveNextCrDt = new com.see.truetransact.uicomponent.CLabel();
        lblLeaveBalanceAsOnVal = new com.see.truetransact.uicomponent.CLabel();
        btnCalculate = new com.see.truetransact.uicomponent.CButton();
        lblLeaveLastCrDtVal = new com.see.truetransact.uicomponent.CLabel();
        panBtnLeaveSan = new com.see.truetransact.uicomponent.CPanel();
        lblTabLeaveType = new com.see.truetransact.uicomponent.CLabel();
        lblTabDays = new com.see.truetransact.uicomponent.CLabel();
        txtTabDays = new com.see.truetransact.uicomponent.CTextField();
        panBtnEmployeeLoan = new com.see.truetransact.uicomponent.CPanel();
        btnLeaveSanNew = new com.see.truetransact.uicomponent.CButton();
        btnLeaveSanSave = new com.see.truetransact.uicomponent.CButton();
        btnLeaveSanDelete = new com.see.truetransact.uicomponent.CButton();
        cbotblLeaveType = new com.see.truetransact.uicomponent.CComboBox();
        tdtTblReqFrom = new com.see.truetransact.uicomponent.CDateField();
        tdtTblReqTo = new com.see.truetransact.uicomponent.CDateField();
        lblTblReqFrom = new com.see.truetransact.uicomponent.CLabel();
        lblTblReqTo = new com.see.truetransact.uicomponent.CLabel();
        cboPayType = new com.see.truetransact.uicomponent.CComboBox();
        lblPayType = new com.see.truetransact.uicomponent.CLabel();
        panSanEmp = new com.see.truetransact.uicomponent.CPanel();
        txtSanEmpID = new com.see.truetransact.uicomponent.CTextField();
        lblSanEmpID = new com.see.truetransact.uicomponent.CLabel();
        btnEmp = new com.see.truetransact.uicomponent.CButton();
        lblEmpName = new com.see.truetransact.uicomponent.CLabel();
        lblLeaveID = new com.see.truetransact.uicomponent.CLabel();
        panApplication = new com.see.truetransact.uicomponent.CPanel();
        panSanTable1 = new com.see.truetransact.uicomponent.CPanel();
        srpFamily7 = new com.see.truetransact.uicomponent.CScrollPane();
        tblSan1 = new com.see.truetransact.uicomponent.CTable();
        panBtnLeaveSan1 = new com.see.truetransact.uicomponent.CPanel();
        lblTabLeaveType1 = new com.see.truetransact.uicomponent.CLabel();
        lblTabDays1 = new com.see.truetransact.uicomponent.CLabel();
        txtTabDays1 = new com.see.truetransact.uicomponent.CTextField();
        panBtnEmployeeLoan1 = new com.see.truetransact.uicomponent.CPanel();
        btnLeaveSanNew1 = new com.see.truetransact.uicomponent.CButton();
        btnLeaveSanSave1 = new com.see.truetransact.uicomponent.CButton();
        btnLeaveSanDelete1 = new com.see.truetransact.uicomponent.CButton();
        cbotblLeaveType1 = new com.see.truetransact.uicomponent.CComboBox();
        tdtTblReqFrom1 = new com.see.truetransact.uicomponent.CDateField();
        tdtTblReqTo1 = new com.see.truetransact.uicomponent.CDateField();
        lblTblReqFrom1 = new com.see.truetransact.uicomponent.CLabel();
        lblTblReqTo1 = new com.see.truetransact.uicomponent.CLabel();
        cboPayType1 = new com.see.truetransact.uicomponent.CComboBox();
        lblPayType1 = new com.see.truetransact.uicomponent.CLabel();
        panSanEmp1 = new com.see.truetransact.uicomponent.CPanel();
        txtSanEmpID1 = new com.see.truetransact.uicomponent.CTextField();
        lblSanEmpID1 = new com.see.truetransact.uicomponent.CLabel();
        btnEmp1 = new com.see.truetransact.uicomponent.CButton();
        lblEmpName1 = new com.see.truetransact.uicomponent.CLabel();
        lblLeaveID1 = new com.see.truetransact.uicomponent.CLabel();
        panLeaveSan1 = new com.see.truetransact.uicomponent.CPanel();
        tdtLvAplDt1 = new com.see.truetransact.uicomponent.CDateField();
        lblLoanSanctionDate10 = new com.see.truetransact.uicomponent.CLabel();
        tdtReqTo1 = new com.see.truetransact.uicomponent.CDateField();
        lblDateOfJoin11 = new com.see.truetransact.uicomponent.CLabel();
        tdtReqFrom1 = new com.see.truetransact.uicomponent.CDateField();
        lblDateOfJoin12 = new com.see.truetransact.uicomponent.CLabel();
        lblLoanRateofInterest24 = new com.see.truetransact.uicomponent.CLabel();
        cboProcessType1 = new com.see.truetransact.uicomponent.CComboBox();
        lblLeaveType10 = new com.see.truetransact.uicomponent.CLabel();
        tdtSanDate1 = new com.see.truetransact.uicomponent.CDateField();
        lblReportedOn4 = new com.see.truetransact.uicomponent.CLabel();
        txtSanRefNo1 = new com.see.truetransact.uicomponent.CTextField();
        lblPFNumber8 = new com.see.truetransact.uicomponent.CLabel();
        lblPFNumber9 = new com.see.truetransact.uicomponent.CLabel();
        txtNoOfDays1 = new com.see.truetransact.uicomponent.CTextField();
        txtOldSanRefNo1 = new com.see.truetransact.uicomponent.CTextField();
        lblPFNumber10 = new com.see.truetransact.uicomponent.CLabel();
        lblWithLtc1 = new com.see.truetransact.uicomponent.CLabel();
        chkLtc1 = new com.see.truetransact.uicomponent.CCheckBox();
        panBlock1 = new com.see.truetransact.uicomponent.CPanel();
        rdoTwoYr1 = new com.see.truetransact.uicomponent.CRadioButton();
        rdoFourYr1 = new com.see.truetransact.uicomponent.CRadioButton();
        chkLeaveEncash1 = new com.see.truetransact.uicomponent.CCheckBox();
        lblLeavEncash1 = new com.see.truetransact.uicomponent.CLabel();
        txtLeavEncah1 = new com.see.truetransact.uicomponent.CTextField();
        lblTypeOfBlk1 = new com.see.truetransact.uicomponent.CLabel();
        lblEnCashDays1 = new com.see.truetransact.uicomponent.CLabel();
        cboEncashMentData1 = new com.see.truetransact.uicomponent.CComboBox();
        chkLeaveCancel1 = new com.see.truetransact.uicomponent.CCheckBox();
        lblLeaveCancel1 = new com.see.truetransact.uicomponent.CLabel();
        txtLeavePurpose1 = new com.see.truetransact.uicomponent.CTextField();
        txtRemarks1 = new com.see.truetransact.uicomponent.CTextField();
        lblRemarks1 = new com.see.truetransact.uicomponent.CLabel();
        lblSanAuth1 = new com.see.truetransact.uicomponent.CLabel();
        cboSanAuth1 = new com.see.truetransact.uicomponent.CComboBox();
        tbrLeave = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace29 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace30 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace31 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace32 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace33 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace34 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        mbrShareProduct = new com.see.truetransact.uicomponent.CMenuBar();
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
        setMaximumSize(new java.awt.Dimension(770, 640));
        setMinimumSize(new java.awt.Dimension(770, 640));
        setPreferredSize(new java.awt.Dimension(770, 640));

        panLeaveManagement.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panLeaveManagement.setLayout(new java.awt.GridBagLayout());

        tabShareProduct.setMinimumSize(new java.awt.Dimension(769, 321));
        tabShareProduct.setPreferredSize(new java.awt.Dimension(823, 609));

        panLeaveReq.setLayout(new java.awt.GridBagLayout());

        cboLeaveType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboLeaveType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboLeaveTypeItemStateChanged(evt);
            }
        });
        cboLeaveType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboLeaveTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeaveReq.add(cboLeaveType, gridBagConstraints);

        lblLeaveType.setText("Leave Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeaveReq.add(lblLeaveType, gridBagConstraints);

        lblEmpID.setText("Emp ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeaveReq.add(lblEmpID, gridBagConstraints);

        panAccAllowedPeriod.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panLeaveReq.add(panAccAllowedPeriod, gridBagConstraints);

        txtEmpID.setMaxLength(128);
        txtEmpID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtEmpID.setName("txtCompany");
        txtEmpID.setValidation(new DefaultValidation());
        txtEmpID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmpIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLeaveReq.add(txtEmpID, gridBagConstraints);

        btnProcess.setText("Process");
        btnProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        panLeaveReq.add(btnProcess, gridBagConstraints);

        btnEmpProcess.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnEmpProcess.setToolTipText("Account Head");
        btnEmpProcess.setPreferredSize(new java.awt.Dimension(21, 21));
        btnEmpProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmpProcessActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panLeaveReq.add(btnEmpProcess, gridBagConstraints);

        tabShareProduct.addTab("Leave Process", panLeaveReq);

        panApplSan.setBorder(javax.swing.BorderFactory.createTitledBorder("Leave Credit Details"));
        panApplSan.setMinimumSize(new java.awt.Dimension(840, 200));
        panApplSan.setPreferredSize(new java.awt.Dimension(840, 200));
        panApplSan.setLayout(new java.awt.GridBagLayout());

        panSanTable.setMaximumSize(new java.awt.Dimension(380, 152));
        panSanTable.setMinimumSize(new java.awt.Dimension(380, 152));
        panSanTable.setName("panContacts");
        panSanTable.setPreferredSize(new java.awt.Dimension(380, 152));
        panSanTable.setLayout(new java.awt.GridBagLayout());

        srpFamily6.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(""));
        srpFamily6.setMaximumSize(new java.awt.Dimension(370, 120));
        srpFamily6.setMinimumSize(new java.awt.Dimension(370, 120));
        srpFamily6.setName("srpContactList");
        srpFamily6.setPreferredSize(new java.awt.Dimension(370, 120));

        tblSan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Sl No", "Leave Type", "Days", "Req From ", "Req To", "Pay Type"
            }
        ));
        tblSan.setMinimumSize(new java.awt.Dimension(360, 150));
        tblSan.setName("tblSan");
        tblSan.setPreferredScrollableViewportSize(new java.awt.Dimension(360, 150));
        tblSan.setPreferredSize(new java.awt.Dimension(360, 150));
        tblSan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblSanMousePressed(evt);
            }
        });
        srpFamily6.setViewportView(tblSan);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panSanTable.add(srpFamily6, gridBagConstraints);

        btnRejAppl.setText("Reject Application");
        btnRejAppl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejApplActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        panSanTable.add(btnRejAppl, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 0, 2);
        panApplSan.add(panSanTable, gridBagConstraints);

        panLeaveSan.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panLeaveSan.setMinimumSize(new java.awt.Dimension(825, 180));
        panLeaveSan.setName("panMaritalStatus");
        panLeaveSan.setPreferredSize(new java.awt.Dimension(825, 180));
        panLeaveSan.setLayout(new java.awt.GridBagLayout());

        cboProcessType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProcessType.setName("cboProfession");
        cboProcessType.setNextFocusableComponent(txtOldSanRefNo);
        cboProcessType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProcessTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panLeaveSan.add(cboProcessType, gridBagConstraints);

        txtOldSanRefNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtOldSanRefNo.setNextFocusableComponent(tdtLvAplDt);
        txtOldSanRefNo.setValidation(new DefaultValidation());
        txtOldSanRefNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtOldSanRefNoActionPerformed(evt);
            }
        });
        txtOldSanRefNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtOldSanRefNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLeaveSan.add(txtOldSanRefNo, gridBagConstraints);

        tdtLvAplDt.setMinimumSize(new java.awt.Dimension(101, 19));
        tdtLvAplDt.setName("tdtDateOfBirth");
        tdtLvAplDt.setNextFocusableComponent(tdtReqFrom);
        tdtLvAplDt.setPreferredSize(new java.awt.Dimension(101, 19));
        tdtLvAplDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtLvAplDtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panLeaveSan.add(tdtLvAplDt, gridBagConstraints);

        lblLoanSanctionDate9.setText("Leave Appln Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLeaveSan.add(lblLoanSanctionDate9, gridBagConstraints);

        lblDateOfJoin10.setText("Leave Request From");
        lblDateOfJoin10.setName("lblDateOfBirth");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 20, 1, 0);
        panLeaveSan.add(lblDateOfJoin10, gridBagConstraints);

        tdtReqFrom.setMinimumSize(new java.awt.Dimension(101, 19));
        tdtReqFrom.setName("tdtDateOfBirth");
        tdtReqFrom.setNextFocusableComponent(tdtReqTo);
        tdtReqFrom.setPreferredSize(new java.awt.Dimension(101, 19));
        tdtReqFrom.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtReqFromFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLeaveSan.add(tdtReqFrom, gridBagConstraints);

        lblDateOfJoin9.setText("Leave Request To");
        lblDateOfJoin9.setName("lblDateOfBirth");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 20, 1, 0);
        panLeaveSan.add(lblDateOfJoin9, gridBagConstraints);

        tdtReqTo.setMinimumSize(new java.awt.Dimension(101, 19));
        tdtReqTo.setName("tdtDateOfBirth");
        tdtReqTo.setNextFocusableComponent(txtLeavePurpose);
        tdtReqTo.setPreferredSize(new java.awt.Dimension(101, 19));
        tdtReqTo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtReqToFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLeaveSan.add(tdtReqTo, gridBagConstraints);

        txtNoOfDays.setMinimumSize(new java.awt.Dimension(100, 21));
        txtNoOfDays.setNextFocusableComponent(txtLeavePurpose);
        txtNoOfDays.setValidation(new NumericValidation());
        txtNoOfDays.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNoOfDaysFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLeaveSan.add(txtNoOfDays, gridBagConstraints);

        txtLeavePurpose.setMaxLength(128);
        txtLeavePurpose.setMinimumSize(new java.awt.Dimension(100, 21));
        txtLeavePurpose.setName("txtCompany");
        txtLeavePurpose.setNextFocusableComponent(txtSanRefNo);
        txtLeavePurpose.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLeaveSan.add(txtLeavePurpose, gridBagConstraints);

        lblLoanRateofInterest23.setText("Purpose Of Leave");
        lblLoanRateofInterest23.setName("lblEducationalLevel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLeaveSan.add(lblLoanRateofInterest23, gridBagConstraints);

        lblLeaveType9.setText("Process Type");
        lblLeaveType9.setName("lblProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLeaveSan.add(lblLeaveType9, gridBagConstraints);

        tdtSanDate.setMinimumSize(new java.awt.Dimension(101, 19));
        tdtSanDate.setName("");
        tdtSanDate.setNextFocusableComponent(chkLeaveCancel);
        tdtSanDate.setPreferredSize(new java.awt.Dimension(101, 19));
        tdtSanDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtSanDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLeaveSan.add(tdtSanDate, gridBagConstraints);

        lblReportedOn3.setText("Sanction Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLeaveSan.add(lblReportedOn3, gridBagConstraints);

        txtSanRefNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSanRefNo.setNextFocusableComponent(tdtSanDate);
        txtSanRefNo.setValidation(new DefaultValidation());
        txtSanRefNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSanRefNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLeaveSan.add(txtSanRefNo, gridBagConstraints);

        lblPFNumber5.setText("Sanction Ref No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLeaveSan.add(lblPFNumber5, gridBagConstraints);

        lblPFNumber6.setText("No of Days");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLeaveSan.add(lblPFNumber6, gridBagConstraints);

        lblPFNumber7.setText(" Old Sanction No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLeaveSan.add(lblPFNumber7, gridBagConstraints);

        lblWithLtc.setText("With Ltc");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLeaveSan.add(lblWithLtc, gridBagConstraints);

        chkLeaveCancel.setNextFocusableComponent(rdoTwoYr);
        chkLeaveCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkLeaveCancelActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLeaveSan.add(chkLeaveCancel, gridBagConstraints);

        panBlock.setMinimumSize(new java.awt.Dimension(100, 40));
        panBlock.setPreferredSize(new java.awt.Dimension(100, 40));
        panBlock.setLayout(new java.awt.GridBagLayout());

        rdoTwoYr.setText("2 Yr Block");
        rdoTwoYr.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        rdoTwoYr.setMaximumSize(new java.awt.Dimension(87, 22));
        rdoTwoYr.setMinimumSize(new java.awt.Dimension(87, 22));
        rdoTwoYr.setNextFocusableComponent(rdoFourYr);
        rdoTwoYr.setPreferredSize(new java.awt.Dimension(87, 22));
        panBlock.add(rdoTwoYr, new java.awt.GridBagConstraints());

        rdoFourYr.setText("4 Yr Block");
        rdoFourYr.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        rdoFourYr.setMaximumSize(new java.awt.Dimension(87, 22));
        rdoFourYr.setMinimumSize(new java.awt.Dimension(87, 22));
        rdoFourYr.setNextFocusableComponent(chkLeaveEncash);
        rdoFourYr.setPreferredSize(new java.awt.Dimension(87, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panBlock.add(rdoFourYr, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panLeaveSan.add(panBlock, gridBagConstraints);

        chkLeaveEncash.setNextFocusableComponent(txtLeavEncah);
        chkLeaveEncash.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkLeaveEncashActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLeaveSan.add(chkLeaveEncash, gridBagConstraints);

        lblLeavEncash.setText("With Leave Encashment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLeaveSan.add(lblLeavEncash, gridBagConstraints);

        txtLeavEncah.setMinimumSize(new java.awt.Dimension(100, 21));
        txtLeavEncah.setNextFocusableComponent(cboEncashMentData);
        txtLeavEncah.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLeaveSan.add(txtLeavEncah, gridBagConstraints);

        lblTypeOfBlk.setText("Type Of Block");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLeaveSan.add(lblTypeOfBlk, gridBagConstraints);

        lblEnCashDays.setText("No of Days");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLeaveSan.add(lblEnCashDays, gridBagConstraints);

        cboEncashMentData.setMinimumSize(new java.awt.Dimension(100, 21));
        cboEncashMentData.setName("cboProfession");
        cboEncashMentData.setNextFocusableComponent(btnLeaveSanNew);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLeaveSan.add(cboEncashMentData, gridBagConstraints);

        panAppSan.setMinimumSize(new java.awt.Dimension(200, 21));
        panAppSan.setPreferredSize(new java.awt.Dimension(200, 27));
        panAppSan.setLayout(new java.awt.GridBagLayout());

        rdoApp_Yes.setText("Application");
        rdoApp_Yes.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        rdoApp_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoApp_YesActionPerformed(evt);
            }
        });
        panAppSan.add(rdoApp_Yes, new java.awt.GridBagConstraints());

        rdoSan_Yes.setText("Sanction");
        rdoSan_Yes.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        rdoSan_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoSan_YesActionPerformed(evt);
            }
        });
        panAppSan.add(rdoSan_Yes, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        panLeaveSan.add(panAppSan, gridBagConstraints);

        chkLtc.setNextFocusableComponent(rdoTwoYr);
        chkLtc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkLtcActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panLeaveSan.add(chkLtc, gridBagConstraints);

        lblLeaveCancel.setText("Leave Cancellation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLeaveSan.add(lblLeaveCancel, gridBagConstraints);

        txtRemarks.setMaxLength(128);
        txtRemarks.setMinimumSize(new java.awt.Dimension(100, 21));
        txtRemarks.setName("txtCompany");
        txtRemarks.setNextFocusableComponent(txtSanRefNo);
        txtRemarks.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLeaveSan.add(txtRemarks, gridBagConstraints);

        lblRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLeaveSan.add(lblRemarks, gridBagConstraints);

        lblSanAuth.setText("Sanction Authority");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLeaveSan.add(lblSanAuth, gridBagConstraints);

        cboSanAuth.setMinimumSize(new java.awt.Dimension(100, 21));
        cboSanAuth.setName("cboProfession");
        cboSanAuth.setNextFocusableComponent(txtOldSanRefNo);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panLeaveSan.add(cboSanAuth, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panApplSan.add(panLeaveSan, gridBagConstraints);

        panEnq.setBorder(javax.swing.BorderFactory.createTitledBorder("Leave Enquiry"));
        panEnq.setMinimumSize(new java.awt.Dimension(825, 85));
        panEnq.setName("panMaritalStatus");
        panEnq.setPreferredSize(new java.awt.Dimension(825, 85));
        panEnq.setLayout(new java.awt.GridBagLayout());

        lblLeaveNextCrDt1.setText("Next Credit Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panEnq.add(lblLeaveNextCrDt1, gridBagConstraints);

        lblLeaveLastCrDt.setText("Last Credit Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panEnq.add(lblLeaveLastCrDt, gridBagConstraints);

        lblLeaveBalanceAsOn.setText("Balance As on Date");
        lblLeaveBalanceAsOn.setName("lblProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panEnq.add(lblLeaveBalanceAsOn, gridBagConstraints);

        lblEnqLeaveType.setText("Leave Type");
        lblEnqLeaveType.setName("lblProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panEnq.add(lblEnqLeaveType, gridBagConstraints);

        cboLeaveType1.setMinimumSize(new java.awt.Dimension(100, 21));
        cboLeaveType1.setName("cboProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panEnq.add(cboLeaveType1, gridBagConstraints);

        btnEnq.setText("Enquiry");
        btnEnq.setMaximumSize(new java.awt.Dimension(90, 20));
        btnEnq.setMinimumSize(new java.awt.Dimension(90, 20));
        btnEnq.setPreferredSize(new java.awt.Dimension(90, 20));
        btnEnq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnqActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        panEnq.add(btnEnq, gridBagConstraints);

        lblLeaveNextCrDt.setMaximumSize(new java.awt.Dimension(80, 20));
        lblLeaveNextCrDt.setMinimumSize(new java.awt.Dimension(80, 20));
        lblLeaveNextCrDt.setName("lblRelationManager");
        lblLeaveNextCrDt.setPreferredSize(new java.awt.Dimension(80, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panEnq.add(lblLeaveNextCrDt, gridBagConstraints);

        lblLeaveBalanceAsOnVal.setMaximumSize(new java.awt.Dimension(50, 20));
        lblLeaveBalanceAsOnVal.setMinimumSize(new java.awt.Dimension(50, 20));
        lblLeaveBalanceAsOnVal.setName("lblRelationManager");
        lblLeaveBalanceAsOnVal.setPreferredSize(new java.awt.Dimension(50, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panEnq.add(lblLeaveBalanceAsOnVal, gridBagConstraints);

        btnCalculate.setText("Calculate");
        btnCalculate.setMaximumSize(new java.awt.Dimension(90, 20));
        btnCalculate.setMinimumSize(new java.awt.Dimension(90, 20));
        btnCalculate.setPreferredSize(new java.awt.Dimension(90, 20));
        btnCalculate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalculateActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        panEnq.add(btnCalculate, gridBagConstraints);

        lblLeaveLastCrDtVal.setMaximumSize(new java.awt.Dimension(80, 20));
        lblLeaveLastCrDtVal.setMinimumSize(new java.awt.Dimension(80, 20));
        lblLeaveLastCrDtVal.setName("lblRelationManager");
        lblLeaveLastCrDtVal.setPreferredSize(new java.awt.Dimension(80, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panEnq.add(lblLeaveLastCrDtVal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panApplSan.add(panEnq, gridBagConstraints);

        panBtnLeaveSan.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panBtnLeaveSan.setMinimumSize(new java.awt.Dimension(250, 180));
        panBtnLeaveSan.setPreferredSize(new java.awt.Dimension(825, 180));
        panBtnLeaveSan.setLayout(new java.awt.GridBagLayout());

        lblTabLeaveType.setText("Leave Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panBtnLeaveSan.add(lblTabLeaveType, gridBagConstraints);

        lblTabDays.setText("No Of Days");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panBtnLeaveSan.add(lblTabDays, gridBagConstraints);

        txtTabDays.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panBtnLeaveSan.add(txtTabDays, gridBagConstraints);

        panBtnEmployeeLoan.setLayout(new java.awt.GridBagLayout());

        btnLeaveSanNew.setText("New");
        btnLeaveSanNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLeaveSanNewActionPerformed(evt);
            }
        });
        panBtnEmployeeLoan.add(btnLeaveSanNew, new java.awt.GridBagConstraints());

        btnLeaveSanSave.setText("Save");
        btnLeaveSanSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLeaveSanSaveActionPerformed(evt);
            }
        });
        panBtnEmployeeLoan.add(btnLeaveSanSave, new java.awt.GridBagConstraints());

        btnLeaveSanDelete.setText("Delete");
        btnLeaveSanDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLeaveSanDeleteActionPerformed(evt);
            }
        });
        panBtnEmployeeLoan.add(btnLeaveSanDelete, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panBtnLeaveSan.add(panBtnEmployeeLoan, gridBagConstraints);

        cbotblLeaveType.setMinimumSize(new java.awt.Dimension(100, 21));
        cbotblLeaveType.setName("cboProfession");
        cbotblLeaveType.setNextFocusableComponent(cboPayType);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panBtnLeaveSan.add(cbotblLeaveType, gridBagConstraints);

        tdtTblReqFrom.setMinimumSize(new java.awt.Dimension(101, 19));
        tdtTblReqFrom.setName("");
        tdtTblReqFrom.setNextFocusableComponent(tdtTblReqTo);
        tdtTblReqFrom.setPreferredSize(new java.awt.Dimension(101, 19));
        tdtTblReqFrom.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtTblReqFromFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panBtnLeaveSan.add(tdtTblReqFrom, gridBagConstraints);

        tdtTblReqTo.setMinimumSize(new java.awt.Dimension(101, 19));
        tdtTblReqTo.setName("tdtDateOfBirth");
        tdtTblReqTo.setNextFocusableComponent(txtTabDays);
        tdtTblReqTo.setPreferredSize(new java.awt.Dimension(101, 19));
        tdtTblReqTo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtTblReqToFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panBtnLeaveSan.add(tdtTblReqTo, gridBagConstraints);

        lblTblReqFrom.setText("Request From");
        lblTblReqFrom.setName("lblDateOfBirth");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panBtnLeaveSan.add(lblTblReqFrom, gridBagConstraints);

        lblTblReqTo.setText("Request To");
        lblTblReqTo.setName("lblDateOfBirth");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panBtnLeaveSan.add(lblTblReqTo, gridBagConstraints);

        cboPayType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboPayType.setName("cboProfession");
        cboPayType.setNextFocusableComponent(tdtTblReqFrom);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panBtnLeaveSan.add(cboPayType, gridBagConstraints);

        lblPayType.setText("Pay Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panBtnLeaveSan.add(lblPayType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panApplSan.add(panBtnLeaveSan, gridBagConstraints);

        panSanEmp.setMinimumSize(new java.awt.Dimension(825, 27));
        panSanEmp.setPreferredSize(new java.awt.Dimension(825, 27));
        panSanEmp.setLayout(new java.awt.GridBagLayout());

        txtSanEmpID.setMaxLength(128);
        txtSanEmpID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSanEmpID.setName("txtCompany");
        txtSanEmpID.setValidation(new DefaultValidation());
        txtSanEmpID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSanEmpIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panSanEmp.add(txtSanEmpID, gridBagConstraints);

        lblSanEmpID.setText("Emp ");
        lblSanEmpID.setName("lblProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panSanEmp.add(lblSanEmpID, gridBagConstraints);

        btnEmp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnEmp.setToolTipText("Account Head");
        btnEmp.setPreferredSize(new java.awt.Dimension(21, 21));
        btnEmp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmpActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSanEmp.add(btnEmp, gridBagConstraints);

        lblEmpName.setMaximumSize(new java.awt.Dimension(230, 20));
        lblEmpName.setMinimumSize(new java.awt.Dimension(230, 20));
        lblEmpName.setName("lblRelationManager");
        lblEmpName.setPreferredSize(new java.awt.Dimension(230, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 21, 1, 4);
        panSanEmp.add(lblEmpName, gridBagConstraints);

        lblLeaveID.setMaximumSize(new java.awt.Dimension(80, 20));
        lblLeaveID.setMinimumSize(new java.awt.Dimension(80, 20));
        lblLeaveID.setName("lblRelationManager");
        lblLeaveID.setPreferredSize(new java.awt.Dimension(80, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panSanEmp.add(lblLeaveID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 4);
        panApplSan.add(panSanEmp, gridBagConstraints);

        tabShareProduct.addTab("Leave Appl/San", panApplSan);

        panApplication.setBorder(javax.swing.BorderFactory.createTitledBorder("Leave Credit Details"));
        panApplication.setMinimumSize(new java.awt.Dimension(840, 200));
        panApplication.setPreferredSize(new java.awt.Dimension(840, 200));
        panApplication.setLayout(new java.awt.GridBagLayout());

        panSanTable1.setMaximumSize(new java.awt.Dimension(380, 122));
        panSanTable1.setMinimumSize(new java.awt.Dimension(380, 122));
        panSanTable1.setName("panContacts");
        panSanTable1.setPreferredSize(new java.awt.Dimension(380, 122));
        panSanTable1.setLayout(new java.awt.GridBagLayout());

        srpFamily7.setViewportBorder(javax.swing.BorderFactory.createTitledBorder(""));
        srpFamily7.setMaximumSize(new java.awt.Dimension(370, 120));
        srpFamily7.setMinimumSize(new java.awt.Dimension(370, 120));
        srpFamily7.setName("srpContactList");
        srpFamily7.setPreferredSize(new java.awt.Dimension(370, 120));

        tblSan1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Sl No", "Leave Type", "Days", "Req From ", "Req To", "Pay Type"
            }
        ));
        tblSan1.setMinimumSize(new java.awt.Dimension(360, 150));
        tblSan1.setName("tblSan");
        tblSan1.setPreferredScrollableViewportSize(new java.awt.Dimension(360, 150));
        tblSan1.setPreferredSize(new java.awt.Dimension(360, 150));
        tblSan1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblSan1MousePressed(evt);
            }
        });
        srpFamily7.setViewportView(tblSan1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panSanTable1.add(srpFamily7, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 0, 2);
        panApplication.add(panSanTable1, gridBagConstraints);

        panBtnLeaveSan1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panBtnLeaveSan1.setMinimumSize(new java.awt.Dimension(250, 180));
        panBtnLeaveSan1.setPreferredSize(new java.awt.Dimension(250, 180));
        panBtnLeaveSan1.setLayout(new java.awt.GridBagLayout());

        lblTabLeaveType1.setText("Leave Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBtnLeaveSan1.add(lblTabLeaveType1, gridBagConstraints);

        lblTabDays1.setText("No Of Days");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBtnLeaveSan1.add(lblTabDays1, gridBagConstraints);

        txtTabDays1.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBtnLeaveSan1.add(txtTabDays1, gridBagConstraints);

        panBtnEmployeeLoan1.setLayout(new java.awt.GridBagLayout());

        btnLeaveSanNew1.setText("New");
        panBtnEmployeeLoan1.add(btnLeaveSanNew1, new java.awt.GridBagConstraints());

        btnLeaveSanSave1.setText("Save");
        panBtnEmployeeLoan1.add(btnLeaveSanSave1, new java.awt.GridBagConstraints());

        btnLeaveSanDelete1.setText("Delete");
        panBtnEmployeeLoan1.add(btnLeaveSanDelete1, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.insets = new java.awt.Insets(6, 3, 6, 3);
        panBtnLeaveSan1.add(panBtnEmployeeLoan1, gridBagConstraints);

        cbotblLeaveType1.setMinimumSize(new java.awt.Dimension(100, 21));
        cbotblLeaveType1.setName("cboProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panBtnLeaveSan1.add(cbotblLeaveType1, gridBagConstraints);

        tdtTblReqFrom1.setMinimumSize(new java.awt.Dimension(101, 19));
        tdtTblReqFrom1.setName("tdtDateOfBirth");
        tdtTblReqFrom1.setPreferredSize(new java.awt.Dimension(101, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panBtnLeaveSan1.add(tdtTblReqFrom1, gridBagConstraints);

        tdtTblReqTo1.setMinimumSize(new java.awt.Dimension(101, 19));
        tdtTblReqTo1.setName("tdtDateOfBirth");
        tdtTblReqTo1.setPreferredSize(new java.awt.Dimension(101, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panBtnLeaveSan1.add(tdtTblReqTo1, gridBagConstraints);

        lblTblReqFrom1.setText("Request From");
        lblTblReqFrom1.setName("lblDateOfBirth");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 20, 1, 0);
        panBtnLeaveSan1.add(lblTblReqFrom1, gridBagConstraints);

        lblTblReqTo1.setText("Request To");
        lblTblReqTo1.setName("lblDateOfBirth");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 20, 1, 0);
        panBtnLeaveSan1.add(lblTblReqTo1, gridBagConstraints);

        cboPayType1.setMinimumSize(new java.awt.Dimension(100, 21));
        cboPayType1.setName("cboProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panBtnLeaveSan1.add(cboPayType1, gridBagConstraints);

        lblPayType1.setText("Pay Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBtnLeaveSan1.add(lblPayType1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panApplication.add(panBtnLeaveSan1, gridBagConstraints);

        panSanEmp1.setMinimumSize(new java.awt.Dimension(825, 27));
        panSanEmp1.setPreferredSize(new java.awt.Dimension(825, 27));
        panSanEmp1.setLayout(new java.awt.GridBagLayout());

        txtSanEmpID1.setMaxLength(128);
        txtSanEmpID1.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSanEmpID1.setName("txtCompany");
        txtSanEmpID1.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panSanEmp1.add(txtSanEmpID1, gridBagConstraints);

        lblSanEmpID1.setText("Emp ID");
        lblSanEmpID1.setName("lblProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panSanEmp1.add(lblSanEmpID1, gridBagConstraints);

        btnEmp1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnEmp1.setToolTipText("Account Head");
        btnEmp1.setPreferredSize(new java.awt.Dimension(21, 21));
        btnEmp1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmp1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panSanEmp1.add(btnEmp1, gridBagConstraints);

        lblEmpName1.setMaximumSize(new java.awt.Dimension(230, 20));
        lblEmpName1.setMinimumSize(new java.awt.Dimension(230, 20));
        lblEmpName1.setName("lblRelationManager");
        lblEmpName1.setPreferredSize(new java.awt.Dimension(230, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panSanEmp1.add(lblEmpName1, gridBagConstraints);

        lblLeaveID1.setMaximumSize(new java.awt.Dimension(80, 20));
        lblLeaveID1.setMinimumSize(new java.awt.Dimension(80, 20));
        lblLeaveID1.setName("lblRelationManager");
        lblLeaveID1.setPreferredSize(new java.awt.Dimension(80, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panSanEmp1.add(lblLeaveID1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 4);
        panApplication.add(panSanEmp1, gridBagConstraints);

        panLeaveSan1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panLeaveSan1.setMinimumSize(new java.awt.Dimension(825, 180));
        panLeaveSan1.setName("panMaritalStatus");
        panLeaveSan1.setPreferredSize(new java.awt.Dimension(825, 180));
        panLeaveSan1.setLayout(new java.awt.GridBagLayout());

        tdtLvAplDt1.setMinimumSize(new java.awt.Dimension(101, 19));
        tdtLvAplDt1.setName("tdtDateOfBirth");
        tdtLvAplDt1.setPreferredSize(new java.awt.Dimension(101, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLeaveSan1.add(tdtLvAplDt1, gridBagConstraints);

        lblLoanSanctionDate10.setText("Leave Appl Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLeaveSan1.add(lblLoanSanctionDate10, gridBagConstraints);

        tdtReqTo1.setMinimumSize(new java.awt.Dimension(101, 19));
        tdtReqTo1.setName("tdtDateOfBirth");
        tdtReqTo1.setPreferredSize(new java.awt.Dimension(101, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLeaveSan1.add(tdtReqTo1, gridBagConstraints);

        lblDateOfJoin11.setText("Leave Req To");
        lblDateOfJoin11.setName("lblDateOfBirth");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 20, 1, 0);
        panLeaveSan1.add(lblDateOfJoin11, gridBagConstraints);

        tdtReqFrom1.setMinimumSize(new java.awt.Dimension(101, 19));
        tdtReqFrom1.setName("tdtDateOfBirth");
        tdtReqFrom1.setPreferredSize(new java.awt.Dimension(101, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLeaveSan1.add(tdtReqFrom1, gridBagConstraints);

        lblDateOfJoin12.setText("Leave Req From");
        lblDateOfJoin12.setName("lblDateOfBirth");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 20, 1, 0);
        panLeaveSan1.add(lblDateOfJoin12, gridBagConstraints);

        lblLoanRateofInterest24.setText("Purpose Of Leave");
        lblLoanRateofInterest24.setName("lblEducationalLevel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLeaveSan1.add(lblLoanRateofInterest24, gridBagConstraints);

        cboProcessType1.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProcessType1.setName("cboProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLeaveSan1.add(cboProcessType1, gridBagConstraints);

        lblLeaveType10.setText("Process Type");
        lblLeaveType10.setName("lblProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLeaveSan1.add(lblLeaveType10, gridBagConstraints);

        tdtSanDate1.setMinimumSize(new java.awt.Dimension(101, 19));
        tdtSanDate1.setName("tdtDateOfBirth");
        tdtSanDate1.setPreferredSize(new java.awt.Dimension(101, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLeaveSan1.add(tdtSanDate1, gridBagConstraints);

        lblReportedOn4.setText("Sanction Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLeaveSan1.add(lblReportedOn4, gridBagConstraints);

        txtSanRefNo1.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSanRefNo1.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLeaveSan1.add(txtSanRefNo1, gridBagConstraints);

        lblPFNumber8.setText("Sanction Ref No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLeaveSan1.add(lblPFNumber8, gridBagConstraints);

        lblPFNumber9.setText("No of Days");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLeaveSan1.add(lblPFNumber9, gridBagConstraints);

        txtNoOfDays1.setMinimumSize(new java.awt.Dimension(100, 21));
        txtNoOfDays1.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeaveSan1.add(txtNoOfDays1, gridBagConstraints);

        txtOldSanRefNo1.setMinimumSize(new java.awt.Dimension(100, 21));
        txtOldSanRefNo1.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLeaveSan1.add(txtOldSanRefNo1, gridBagConstraints);

        lblPFNumber10.setText("Old Sanction No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLeaveSan1.add(lblPFNumber10, gridBagConstraints);

        lblWithLtc1.setText("With Ltc");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLeaveSan1.add(lblWithLtc1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panLeaveSan1.add(chkLtc1, gridBagConstraints);

        panBlock1.setMinimumSize(new java.awt.Dimension(100, 40));
        panBlock1.setPreferredSize(new java.awt.Dimension(100, 40));
        panBlock1.setLayout(new java.awt.GridBagLayout());

        rdoTwoYr1.setText("2 Yr Block");
        rdoTwoYr1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        rdoTwoYr1.setMaximumSize(new java.awt.Dimension(87, 22));
        rdoTwoYr1.setMinimumSize(new java.awt.Dimension(87, 22));
        rdoTwoYr1.setPreferredSize(new java.awt.Dimension(87, 22));
        panBlock1.add(rdoTwoYr1, new java.awt.GridBagConstraints());

        rdoFourYr1.setText("4 Yr Block");
        rdoFourYr1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        rdoFourYr1.setMaximumSize(new java.awt.Dimension(87, 22));
        rdoFourYr1.setMinimumSize(new java.awt.Dimension(87, 22));
        rdoFourYr1.setPreferredSize(new java.awt.Dimension(87, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panBlock1.add(rdoFourYr1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 4);
        panLeaveSan1.add(panBlock1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeaveSan1.add(chkLeaveEncash1, gridBagConstraints);

        lblLeavEncash1.setText("With Leave Encashment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLeaveSan1.add(lblLeavEncash1, gridBagConstraints);

        txtLeavEncah1.setMinimumSize(new java.awt.Dimension(100, 21));
        txtLeavEncah1.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeaveSan1.add(txtLeavEncah1, gridBagConstraints);

        lblTypeOfBlk1.setText("Type Of Block");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLeaveSan1.add(lblTypeOfBlk1, gridBagConstraints);

        lblEnCashDays1.setText("No of Days");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLeaveSan1.add(lblEnCashDays1, gridBagConstraints);

        cboEncashMentData1.setMinimumSize(new java.awt.Dimension(100, 21));
        cboEncashMentData1.setName("cboProfession");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLeaveSan1.add(cboEncashMentData1, gridBagConstraints);

        chkLeaveCancel1.setNextFocusableComponent(rdoTwoYr);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLeaveSan1.add(chkLeaveCancel1, gridBagConstraints);

        lblLeaveCancel1.setText("Leave Cancellation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLeaveSan1.add(lblLeaveCancel1, gridBagConstraints);

        txtLeavePurpose1.setMaxLength(128);
        txtLeavePurpose1.setMinimumSize(new java.awt.Dimension(100, 21));
        txtLeavePurpose1.setName("txtCompany");
        txtLeavePurpose1.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLeaveSan1.add(txtLeavePurpose1, gridBagConstraints);

        txtRemarks1.setMaxLength(128);
        txtRemarks1.setMinimumSize(new java.awt.Dimension(100, 21));
        txtRemarks1.setName("txtCompany");
        txtRemarks1.setNextFocusableComponent(txtSanRefNo);
        txtRemarks1.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLeaveSan1.add(txtRemarks1, gridBagConstraints);

        lblRemarks1.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLeaveSan1.add(lblRemarks1, gridBagConstraints);

        lblSanAuth1.setText("Sanction Authority");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panLeaveSan1.add(lblSanAuth1, gridBagConstraints);

        cboSanAuth1.setMinimumSize(new java.awt.Dimension(100, 21));
        cboSanAuth1.setName("cboProfession");
        cboSanAuth1.setNextFocusableComponent(txtOldSanRefNo);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panLeaveSan1.add(cboSanAuth1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panApplication.add(panLeaveSan1, gridBagConstraints);

        tabShareProduct.addTab("Leave Application", panApplication);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panLeaveManagement.add(tabShareProduct, gridBagConstraints);
        tabShareProduct.getAccessibleContext().setAccessibleName("pan Application");

        getContentPane().add(panLeaveManagement, java.awt.BorderLayout.CENTER);

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
        tbrLeave.add(btnView);

        lblSpace5.setText("     ");
        tbrLeave.add(lblSpace5);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrLeave.add(btnNew);

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLeave.add(lblSpace29);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrLeave.add(btnEdit);

        lblSpace30.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace30.setText("     ");
        lblSpace30.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLeave.add(lblSpace30);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrLeave.add(btnDelete);

        lblSpace2.setText("     ");
        tbrLeave.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrLeave.add(btnSave);

        lblSpace31.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace31.setText("     ");
        lblSpace31.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLeave.add(lblSpace31);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrLeave.add(btnCancel);

        lblSpace3.setText("     ");
        tbrLeave.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrLeave.add(btnAuthorize);

        lblSpace32.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace32.setText("     ");
        lblSpace32.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace32.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace32.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLeave.add(lblSpace32);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrLeave.add(btnException);

        lblSpace33.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace33.setText("     ");
        lblSpace33.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace33.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace33.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLeave.add(lblSpace33);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrLeave.add(btnReject);

        lblSpace4.setText("     ");
        tbrLeave.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrLeave.add(btnPrint);

        lblSpace34.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace34.setText("     ");
        lblSpace34.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLeave.add(lblSpace34);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrLeave.add(btnClose);

        getContentPane().add(tbrLeave, java.awt.BorderLayout.NORTH);

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

        mbrShareProduct.setName("mbrCustomer");

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

        mbrShareProduct.add(mnuProcess);

        setJMenuBar(mbrShareProduct);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void txtSanRefNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSanRefNoFocusLost
        // TODO add your handling code here:
        if(txtSanRefNo.getText().length()>0){
            HashMap sanMap = new HashMap();
            sanMap.put("SANCTION_REF_NO",txtSanRefNo.getText());
            List sanList = ClientUtil.executeQuery("countSanctionNumber",sanMap);
            if(sanList!=null && sanList.size()>0){
                int cnt=CommonUtil.convertObjToInt(sanList.get(0));
                if(cnt>0){
                    ClientUtil.displayAlert("This sanction Number Already Exists");
                    txtSanRefNo.setText("");
                    return;
                }
            }
        }
    }//GEN-LAST:event_txtSanRefNoFocusLost
    
    private void btnLeaveSanDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLeaveSanDeleteActionPerformed
        // TODO add your handling code here:
        String s=  CommonUtil.convertObjToStr(tblSan.getValueAt(tblSan.getSelectedRow(),0));
        observable.deleteTableData(s,tblSan.getSelectedRow());
        observable.resetPanSan();
        cbotblLeaveType.setSelectedItem("");
        enableDisablePanSan(false);
        resetFields();
    }//GEN-LAST:event_btnLeaveSanDeleteActionPerformed
    
    private void btnEmp1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmp1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEmp1ActionPerformed
    
    private void chkLtcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkLtcActionPerformed
        // TODO add your handling code here:
        if(chkLtc.isSelected())
            ClientUtil.enableDisable(panBlock, true);
        else
            ClientUtil.enableDisable(panBlock, false);
    }//GEN-LAST:event_chkLtcActionPerformed
    
    private void txtOldSanRefNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtOldSanRefNoActionPerformed
        // TODO add your handling code here:
        //         if(txtOldSanRefNo.getText().length()>0){
        //            HashMap refMap = new HashMap();
        //            refMap.put("OLD_REF_NO",txtOldSanRefNo.getText());
        //            refMap.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
        //            List refList= ClientUtil.executeQuery("checkForValidRefNo",refMap);
        //            if(refList!=null && refList.size()>0){
        //                refMap = null;
        //                refMap=(HashMap)refList.get(0);
        //                Date reqTo = (Date)refMap.get("LEAVE_REQ_TO");
        //                reqTo= DateUtil.addDays(reqTo,1);
        //                tdtReqFrom.setDateValue(CommonUtil.convertObjToStr(reqTo));
        //            }
        //            else{
        //                ClientUtil.displayAlert("Old Sanction Number DoesNot Exist");
        //                txtOldSanRefNo.setText("");
        //            }
        //
        //        }
    }//GEN-LAST:event_txtOldSanRefNoActionPerformed
    
    private void txtOldSanRefNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtOldSanRefNoFocusLost
        // TODO add your handling code here:
        String proType=(((ComboBoxModel)cboProcessType.getModel()).getKeyForSelected().toString());
        if(txtOldSanRefNo.getText().length()>0){
            HashMap refMap = new HashMap();
            refMap.put("OLD_REF_NO",txtOldSanRefNo.getText());
            refMap.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
            List refList= ClientUtil.executeQuery("checkForValidRefNo",refMap);
            if(refList!=null && refList.size()>0){
                if(proType.equalsIgnoreCase("EXTENSION")){
                    refMap = null;
                    refMap=(HashMap)refList.get(0);
                    Date reqTo = (Date)refMap.get("LEAVE_REQ_TO");
                    reqTo= DateUtil.addDays(reqTo,1);
                    tdtReqFrom.setDateValue(CommonUtil.convertObjToStr(reqTo));
                    extDate=reqTo;
                    tdtReqTo.setDateValue("");
                    txtNoOfDays.setText("");
                }
            }
            else{
                ClientUtil.displayAlert("Old Sanction Number DoesNot Exist");
                txtOldSanRefNo.setText("");
            }
            
        }
    }//GEN-LAST:event_txtOldSanRefNoFocusLost
    
    private void btnCalculateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalculateActionPerformed
        // TODO add your handling code here:
        String leaveType=(((ComboBoxModel)cboLeaveType1.getModel()).getKeyForSelected().toString());
        observable.execute(leaveType, txtSanEmpID.getText());
    }//GEN-LAST:event_btnCalculateActionPerformed
    
    private void txtNoOfDaysFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNoOfDaysFocusLost
        // TODO add your handling code here:
        if(tdtReqFrom.getDateValue().length()<=0 || tdtReqTo.getDateValue().length()<=0){
            ClientUtil.displayAlert("Please Enter Leave Request Dates");
        }
        else{
            long days= CommonUtil.convertObjToLong(txtNoOfDays.getText());
            long dateDiff=DateUtil.dateDiff(DateUtil.getDateWithoutMinitues(tdtReqFrom.getDateValue()),DateUtil.getDateWithoutMinitues(tdtReqTo.getDateValue()));
            dateDiff=dateDiff+1;
            if(days!=dateDiff){
                ClientUtil.displayAlert("Leave Request Dates Is Not Equal To The Number of Days Entered");
                txtNoOfDays.setText("");
                return;
            }
        }
    }//GEN-LAST:event_txtNoOfDaysFocusLost
    
    private void chkLeaveCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkLeaveCancelActionPerformed
        // TODO add your handling code here:
        if(chkLeaveCancel.isSelected()){
            if(txtOldSanRefNo.getText().length()<=0){
                ClientUtil.displayAlert("Enter Old Sanction Reference Number");
                chkLeaveCancel.setSelected(false);
                return;
            }else{
                HashMap refMap = new HashMap();
                refMap.put("OLD_REF_NO",txtOldSanRefNo.getText());
                refMap.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
                List refList= ClientUtil.executeQuery("checkForValidRefNo",refMap);
                if(refList!=null && refList.size()>0){
                    refMap = null;
                    refMap=(HashMap)refList.get(0);
                    Date from = (Date)refMap.get("LEAVE_REQ_FROM");
                    Date to = (Date)refMap.get("LEAVE_REQ_TO");
                    tdtReqFrom.setDateValue(CommonUtil.convertObjToStr(refMap.get("LEAVE_REQ_FROM")));
                    tdtReqTo.setDateValue(CommonUtil.convertObjToStr(refMap.get("LEAVE_REQ_TO")));
                    tdtReqFrom.setEnabled(false);
                    tdtReqTo.setEnabled(false);
                    txtNoOfDays.setText(String.valueOf((DateUtil.dateDiff(from,to))+1));
                    txtNoOfDays.setEnabled(false);
                    if(CommonUtil.convertObjToStr(refMap.get("WITH_LEAVE_ENCASHMENT")).equalsIgnoreCase("Y")){
                        chkLeaveEncash.setSelected(true);
                        chkLeaveEncash.setEnabled(false);
                        cboEncashMentData.setSelectedItem(refMap.get("ENCASHMENT_LEAVE_TYPE"));
                        cboEncashMentData.setEnabled(false);
                        txtLeavEncah.setText(CommonUtil.convertObjToStr(refMap.get("ENCASHMENT_DAYS")));
                        txtLeavEncah.setEnabled(false);
                    }
                    refMap.put("MODIFICATION_EDIT","MODIFICATION_EDIT");
                    observable.populateData(refMap);
                    btnLeaveSanNew.setEnabled(false);
                    btnLeaveSanDelete.setEnabled(false);
                    btnLeaveSanSave.setEnabled(false);
                }
            }
        }  else{
            tdtReqFrom.setDateValue("");
            tdtReqTo.setDateValue("");
            txtNoOfDays.setText("");
            tdtReqFrom.setEnabled(true);
            tdtReqTo.setEnabled(true);
            txtNoOfDays.setEnabled(true);
            observable.resetTableValues();
            btnLeaveSanNew.setEnabled(true);
            btnLeaveSanDelete.setEnabled(true);
            btnLeaveSanSave.setEnabled(true);
            chkLeaveEncash.setEnabled(true);
            chkLeaveEncash.setSelected(false);
            txtLeavEncah.setText("");
            cboEncashMentData.setSelectedItem("");
        }
        
    }//GEN-LAST:event_chkLeaveCancelActionPerformed
    
    private void chkLeaveEncashActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkLeaveEncashActionPerformed
        // TODO add your handling code here:
        if(chkLeaveEncash.isSelected()){
            txtLeavEncah.setEnabled(true);
            cboEncashMentData.setEnabled(true);
        }
        else{
            cboEncashMentData.setEnabled(false);
            txtLeavEncah.setEnabled(false);
            txtLeavEncah.setText("");
            cboEncashMentData.setSelectedItem("");
        }
    }//GEN-LAST:event_chkLeaveEncashActionPerformed
    
    private void tblSan1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSan1MousePressed
        // TODO add your handling code here:
        btnLeaveSanNew1.setEnabled(true);
        updateMode = true;
        updateTab= tblSan1.getSelectedRow();
        observable.setNewData(false);
        String st=CommonUtil.convertObjToStr(tblSan1.getValueAt(tblSan1.getSelectedRow(),0));
        observable.populateLeaveDetails(st,"APPL_TAB");
        enableDisablePanSanAppl(true);
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_AUTHORIZE||observable.getActionType()==ClientConstants.ACTIONTYPE_REJECT ||
        observable.getActionType()==ClientConstants.ACTIONTYPE_VIEW || observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE || (observable.getSanStatus().length()>0) || chkLeaveCancel.isSelected()){
            enableDisablePanSanAppl(false);
            btnLeaveSanNew1.setEnabled(false);
        }
        observable.notifyObservers();
    }//GEN-LAST:event_tblSan1MousePressed
    
    private void tdtTblReqToFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtTblReqToFocusLost
        // TODO add your handling code here:
        Date tblReqTo= DateUtil.getDateMMDDYYYY(tdtTblReqTo.getDateValue());
        Date mainReqfrom = DateUtil.getDateMMDDYYYY(tdtReqFrom.getDateValue());
        Date mainReqTo = DateUtil.getDateMMDDYYYY(tdtReqTo.getDateValue());
        if(tblReqTo!=null){
            Date fromDt= DateUtil.getDateMMDDYYYY(tdtTblReqFrom.getDateValue());
            long a=DateUtil.dateDiff(fromDt,tblReqTo);
            if(!((tblReqTo.after(mainReqfrom) || tblReqTo.compareTo(mainReqfrom) == 0) &&
            tblReqTo.before( mainReqTo) || tblReqTo.compareTo(mainReqTo) == 0)){
                ClientUtil.displayAlert("Requested From Date Should Be between :"+'\n'+" "+ mainReqfrom+" "+"and"+" "+ mainReqTo);
                tdtTblReqTo.setDateValue("");
            }
            else if(a<0){
                ClientUtil.displayAlert("To Date Should Be Greater Than From Date") ;
                tdtTblReqTo.setDateValue("");
            }
            else{
                txtTabDays.setText(String.valueOf(a+1));
            }
            
        }
    }//GEN-LAST:event_tdtTblReqToFocusLost
    
    private void tdtTblReqFromFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtTblReqFromFocusLost
        // TODO add your handling code here:
        Date tableReqFrom = DateUtil.getDateMMDDYYYY(tdtTblReqFrom.getDateValue());
        if(tableReqFrom!=null){
            Date mainReqfrom = DateUtil.getDateMMDDYYYY(tdtReqFrom.getDateValue());
            Date mainReqTo = DateUtil.getDateMMDDYYYY(tdtReqTo.getDateValue());
            if(!((tableReqFrom.after(mainReqfrom) || tableReqFrom.compareTo(mainReqfrom) == 0) &&
            tableReqFrom.before( mainReqTo) || tableReqFrom.compareTo(mainReqTo) == 0)) {
                ClientUtil.displayAlert("Requested From Date Should Be between :" +'\n'+" "+mainReqfrom+" "+"and"+" "+ mainReqTo);
                tdtTblReqFrom.setDateValue("");
            }
            
        }
    }//GEN-LAST:event_tdtTblReqFromFocusLost
    
    private void tdtSanDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtSanDateFocusLost
        // TODO add your handling code here:
        Date sanDate= DateUtil.getDateMMDDYYYY(tdtSanDate.getDateValue());
        if(sanDate!=null){
            if(!sanDate.equals(currDt.clone())){
                ClientUtil.displayAlert("Sanction Date Should Be Equal To Current Date");
                tdtSanDate.setDateValue("");
            }
        }
    }//GEN-LAST:event_tdtSanDateFocusLost
    
    private void tdtLvAplDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtLvAplDtFocusLost
        // TODO add your handling code here:
        ClientUtil.validateLTDate(tdtLvAplDt);
    }//GEN-LAST:event_tdtLvAplDtFocusLost
    
    private void tdtReqToFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtReqToFocusLost
        // TODO add your handling code here:
        String proType=(((ComboBoxModel)cboProcessType.getModel()).getKeyForSelected().toString());
        Date to=DateUtil.getDateWithoutMinitues(tdtReqTo.getDateValue());
        if(to!=null){
            java.util.Date toComp = new java.sql.Timestamp(to.getTime());
            ClientUtil.validateToDate(tdtReqTo, DateUtil.getStringDate(DateUtil.addDays((Date) currDt.clone(), -1)));
            Date from=DateUtil.getDateWithoutMinitues(tdtReqFrom.getDateValue());
            java.util.Date fromComp = new java.sql.Timestamp(from.getTime());
            
            if (from!=null && to!=null && DateUtil.dateDiff(from,to)<0) {
                displayAlert("To date should be greater than From Date...");
                tdtReqTo.setDateValue("");
            }
            HashMap map = new HashMap();
            map.put("EMP_ID",txtSanEmpID.getText());
            List dateList= ClientUtil.executeQuery("getLeaveDates",map);
            if(dateList!=null && dateList.size()>0){
                for(int i=0;i<dateList.size();i++){
                    map=(HashMap)dateList.get(i);
                    Date data_from_date= (Date) map.get("LEAVE_REQ_FROM");
                    Date data_to_date= (Date) map.get("LEAVE_REQ_TO");
                    if(!proType.equalsIgnoreCase("MODIFICATION"))
                        if(((data_from_date.after(from) || data_from_date.compareTo(fromComp) == 0) && (data_from_date.before((to))) || data_from_date.compareTo(toComp) == 0) ||
                        ((data_to_date.after(from) || data_to_date.compareTo(fromComp) == 0) && (data_to_date.before((to))) || data_to_date.compareTo(toComp) == 0)) {
                            ClientUtil.displayAlert("Leave Request Already Exists Between These Dates");
                            tdtReqFrom.setDateValue("");
                            tdtReqTo.setDateValue("");
                            return;
                        }
                }
            }
            
            txtNoOfDays.setText(String.valueOf((DateUtil.dateDiff(from,to))+1));
            System.out.println("aaa");
        }
    }//GEN-LAST:event_tdtReqToFocusLost
    
    private void tdtReqFromFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtReqFromFocusLost
        String from_dt=CommonUtil.convertObjToStr(tdtReqFrom.getDateValue());
        if(from_dt.length()>0){
            ClientUtil.validateToDate(tdtReqFrom, DateUtil.getStringDate(DateUtil.addDays((Date) currDt.clone(), -1)));
            String ProcessType=((ComboBoxModel)cboProcessType.getModel()).getKeyForSelected().toString();
            if(ProcessType.equalsIgnoreCase("EXTENSION")){
                if(!from_dt.equals(CommonUtil.convertObjToStr(extDate))){
                    ClientUtil.displayAlert("From Date Should Be"+" "+extDate);
                    tdtReqFrom.setDateValue("");
                }
            }
        }
    }//GEN-LAST:event_tdtReqFromFocusLost
    
    private void cboProcessTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProcessTypeActionPerformed
        // TODO add your handling code here:
        String proType=(((ComboBoxModel)cboProcessType.getModel()).getKeyForSelected().toString());
        if(proType.equalsIgnoreCase("SANCTION")){
            txtOldSanRefNo.setEnabled(false);
            txtOldSanRefNo.setText("");
            chkLeaveCancel.setEnabled(false);
        }
        else{
            txtOldSanRefNo.setEnabled(true);
            chkLeaveCancel.setEnabled(false);
            if(proType.equalsIgnoreCase("MODIFICATION"))
                chkLeaveCancel.setEnabled(true);
            
        }
    }//GEN-LAST:event_cboProcessTypeActionPerformed
    
    private void btnEnqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnqActionPerformed
        // TODO add your handling code here:
        if(txtSanEmpID.getText().length()>0){
            clearEnqData();
            HashMap enqMap = new HashMap();
            enqMap.put("EMP_ID",txtSanEmpID.getText());
            enqMap.put("LEAVE_TYPE",((ComboBoxModel)cboLeaveType1.getModel()).getKeyForSelected().toString());
            List enqList = ClientUtil.executeQuery("EmpLeaveEnq",enqMap);
            if(enqList!=null && enqList.size()>0){
                enqMap=null;
                enqMap=(HashMap)enqList.get(0);
                lblLeaveBalanceAsOnVal.setText(CommonUtil.convertObjToStr(enqMap.get("NO_OF_DAYS")));
                lblLeaveLastCrDtVal.setText(CommonUtil.convertObjToStr(enqMap.get("LAST_CREDIT_DATE")));
                lblLeaveNextCrDt.setText(CommonUtil.convertObjToStr(enqMap.get("NEXT_CREDIT_DATE")));
            }
            else{
                ClientUtil.displayAlert("No Data Found");
            }
            
        }
        else{
            ClientUtil.displayAlert("Please Enter Employee ID");
        }
    }//GEN-LAST:event_btnEnqActionPerformed
    
    private void btnRejApplActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejApplActionPerformed
        // TODO add your handling code here:
        HashMap rejMap = new HashMap();
        rejMap.put("LEAVE_ID", observable.getLeaveID());
        rejMap.put("SANCTION_STATUS", "REJECTED");
        savePerformed("REJAPPL");
        ClientUtil.execute("rejectSan",rejMap);
        ClientUtil.execute("rejectAppl",rejMap);
    }//GEN-LAST:event_btnRejApplActionPerformed
    
    private void btnEmpProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmpProcessActionPerformed
        // TODO add your handling code here:
        callView("EMP_PROCESS");
    }//GEN-LAST:event_btnEmpProcessActionPerformed
    
    private void txtEmpIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmpIDActionPerformed
        // TODO add your handling code here:
        if(txtEmpID.getText().length()>0){
            HashMap empMap = new HashMap();
            empMap.put("EMP_CODE",txtEmpID.getText());
            empMap.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
            List empList = ClientUtil.executeQuery("getEmpDetails", empMap);
            if(empList!=null && empList.size()>0){
                empMap = null;
                empMap=(HashMap) empList.get(0);
                txtEmpID.setText(CommonUtil.convertObjToStr(empMap.get("EMPLOYEE_CODE")));
            }
            else{
                ClientUtil.displayAlert("Invalid Employee ID");
                txtEmpID.setText("");
            }
        }
    }//GEN-LAST:event_txtEmpIDActionPerformed
    
    private void btnProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessActionPerformed
        // TODO add your handling code here:
        String leaveType=(((ComboBoxModel)cboLeaveType.getModel()).getKeyForSelected().toString());
        observable.execute(leaveType, txtEmpID.getText());
        cboLeaveType.setSelectedItem("");
        txtEmpID.setText("");
        cboLeaveType.setEnabled(false);
        txtEmpID.setEnabled(false);
        btnEmpProcess.setEnabled(false);
        //        List noOfEmp = new ArrayList();
        //        HashMap parMap = new HashMap();
        //        parMap.put("LEAVE_TYPE",leaveType);
        //        List parList=ClientUtil.executeQuery("getLeaveParameters",parMap);
        //        if(parList!=null && parList.size()>0){
        //            parMap =null;
        //            parMap= (HashMap)parList.get(0);
        //            Date creditingDate= null;
        //            if((parMap.get("CREDIT_TYPE").equals("FINANCIAL_YR_END"))){
        //                creditingDate=  creditingDate;
        //            }
        //            else if((parMap.get("CREDIT_TYPE").equals("CALENDAR_YR"))){
        //                Date calDate=currDt.clone();
        //                calDate.setMonth(1);
        //                creditingDate=calDate;
        //            }
        //            else if((parMap.get("CREDIT_TYPE").equals("DATE_OF_JOINING"))){
        //                Date calDate=currDt.clone();
        //                calDate.setMonth(1);
        //                creditingDate=calDate;
        //            }
        //            if(txtEmpID.getText().length()>0){
        //                noOfEmp.add(txtEmpID.getText());
        //                parMap.put("EMP_ID",txtEmpID.getText());
        //            }
        //            else{
        //                HashMap noOfEmpMap= new HashMap();
        //                noOfEmpMap.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
        //                noOfEmp= ClientUtil.executeQuery("getAllEmp", noOfEmpMap);
        //            }
        //            for(int i=0;i<noOfEmp.size();i++){
        //                if(txtEmpID.getText().length()<=0){
        //                    HashMap balList=((HashMap)noOfEmp.get(i));
        //                    parMap.put("EMP_ID",balList.get("EMPLOYEE_CODE"));
        //                }
        //                if(parMap.get("LEAVE_LAPSES").equals("N")){
        //                    List lapsList = ClientUtil.executeQuery("getRemainingLeaves", parMap);
        //                    if(lapsList!=null && lapsList.size()>0){
        //                        int a=CommonUtil.convertObjToInt(lapsList.get(0))+CommonUtil.convertObjToInt(parMap.get("MAX_LEAVES_CREDITED"));
        //                        parMap.put("NO_OF_DAYS_ADDITION",Integer.valueOf(a));
        //                    }
        //                    else{
        //                        parMap.put("NO_OF_DAYS_ADDITION",new Integer (CommonUtil.convertObjToInt(parMap.get("MAX_LEAVES_CREDITED"))));
        //                    }
        //                }
        //                else{
        //                    parMap.put("NO_OF_DAYS_ADDITION", new Integer (CommonUtil.convertObjToInt(parMap.get("MAX_LEAVES_CREDITED"))));
        //                }
        //                parMap.put("TRANS_TYPE","CREDIT");
        //                parMap.put("TRANS_DATE",currDt.clone());
        //                parMap.put("LAST_CREDITED_DATE",currDt.clone());
        //                parMap.put("SANCTION_NO","");
        //                List count=ClientUtil.executeQuery("countEmpID", parMap);
        //                int a=CommonUtil.convertObjToInt(count.get(0));
        //                if(a<=0){
        //                    ClientUtil.execute("insertEmpLeave", parMap);
        //                }
        //                else{
        //                    ClientUtil.execute("updateEmpLeave", parMap);
        //                }
        //                System.out.println("parMap@@@@"+parMap);
        //                 parMap.put("NO_OF_DAYS_ADDITION", new Integer (CommonUtil.convertObjToInt(parMap.get("MAX_LEAVES_CREDITED"))));
        //                ClientUtil.execute("insertEmpLeaveTranfer", parMap);
        //            }
        //        }
    }//GEN-LAST:event_btnProcessActionPerformed
    
    private void txtSanEmpIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSanEmpIDActionPerformed
        // TODO add your handling code here:
        if(txtSanEmpID.getText().length()>0){
            HashMap empMap = new HashMap();
            empMap.put("EMP_CODE",txtSanEmpID.getText());
            empMap.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
            List empList = ClientUtil.executeQuery("getEmpDetails", empMap);
            if(empList!=null && empList.size()>0){
                empMap = null;
                empMap=(HashMap) empList.get(0);
                txtSanEmpID.setText(CommonUtil.convertObjToStr(empMap.get("EMPLOYEE_CODE")));
                lblEmpName.setText(CommonUtil.convertObjToStr(empMap.get("FNAME")));
            }
            else{
                ClientUtil.displayAlert("Invalid Employee ID");
                txtSanEmpID.setText("");
            }
        }
    }//GEN-LAST:event_txtSanEmpIDActionPerformed
    
    private void btnEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmpActionPerformed
        // TODO add your handling code here:
        callView("EMP");
    }//GEN-LAST:event_btnEmpActionPerformed
    
    private void rdoSan_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoSan_YesActionPerformed
        // TODO add your handling code here:
        tabShareProduct.setTitleAt(0, "Leave Sanction");
        observable.resetTableValues();
        if(observable.getActionType()==1){
            btnCancel.setEnabled(true);
            btnRejAppl.setEnabled(true);
            callView("NEW_SAN");
        }
        else if((observable.getActionType()==2) ||(observable.getActionType()==ClientConstants.ACTIONTYPE_VIEW)){
            btnRejAppl.setEnabled(true);
            callView("SAN");
        }
        //         else if(observable.getActionType()==8) {
        //             btnRejAppl.setEnabled(false);
        //            if (authOrRejStatus.equalsIgnoreCase("AUTHORIZE")){
        //                authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        //            }
        //            else{
        //                authorizeStatus(CommonConstants.STATUS_REJECTED);
        //            }
        //
        //        }
        
    }//GEN-LAST:event_rdoSan_YesActionPerformed
    
    private void rdoApp_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoApp_YesActionPerformed
        // TODO add your handling code here:
        tabShareProduct.setTitleAt(0, "Leave Application");
        observable.resetTableValues();
        if(observable.getActionType()==1){
            ClientUtil.enableDisable(panLeaveManagement, true);
            ClientUtil.enableDisable(panBtnLeaveSan, true);
            btnCancel.setEnabled(true);
            btnRejAppl.setEnabled(true);
            //            setButtonEnableDisable();
        }
        else if((observable.getActionType()==2)||(observable.getActionType()==ClientConstants.ACTIONTYPE_VIEW)) {
            callView("APPL");
        }
        //        else if(observable.getActionType()==8) {
        //            if (authOrRejStatus.equalsIgnoreCase("AUTHORIZE")){
        //                authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        //            }
        //            else{
        //                authorizeStatus(CommonConstants.STATUS_REJECTED);
        //            }
        //            rdoApp_Yes.setEnabled(false);
        //
        //        }
        btnRejAppl.setEnabled(false);
        txtLeavEncah.setEnabled(false);
        cboEncashMentData.setEnabled(false);
        ClientUtil.enableDisable(panBlock, false);
        rdoSan_Yes.setEnabled(false);
        ClientUtil.enableDisable(panBtnLeaveSan,false);
        ClientUtil.enableDisable(panBtnEmployeeLoan,true);
        txtSanRefNo.setEnabled(false);
        tdtSanDate.setEnabled(false);
        cboSanAuth.setEnabled(false);
    }//GEN-LAST:event_rdoApp_YesActionPerformed
    
    private void tblSanMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSanMousePressed
        // TODO add your handling code here:
        updateOBFields();
        btnLeaveSanNew.setEnabled(true);
        updateMode = true;
        updateTab= tblSan.getSelectedRow();
        observable.setNewData(false);
        String st=CommonUtil.convertObjToStr(tblSan.getValueAt(tblSan.getSelectedRow(),0));
        observable.populateLeaveDetails(st,"SAN_TAB");
        enableDisablePanSan(true);
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_AUTHORIZE||observable.getActionType()==ClientConstants.ACTIONTYPE_REJECT ||
        observable.getActionType()==ClientConstants.ACTIONTYPE_VIEW || observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE || (observable.getSanStatus().length()>0) || chkLeaveCancel.isSelected()){
            enableDisablePanSan(false);
            btnLeaveSanNew.setEnabled(false);
        }
        observable.notifyObservers();
        
    }//GEN-LAST:event_tblSanMousePressed
    
    private void btnLeaveSanNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLeaveSanNewActionPerformed
        // TODO add your handling code here:
        observable.resetPanSan();
        resetFields();
        txtTabDays.setEnabled(true);
        txtTabDays.setText("");
        cbotblLeaveType.setSelectedItem("");
        cbotblLeaveType.setEnabled(true);
        updateMode = false;
        observable.setNewData(true);
        if(tblSan.getRowCount()>0){
            for(int k=0; k<tblSan.getRowCount();k++){
                while (k==(tblSan.getRowCount()-1)) {
                    String tblToDate=CommonUtil.convertObjToStr(tblSan.getValueAt(k,4));
                    Date tabToDate = DateUtil.getDateWithoutMinitues(tblToDate);
                    tdtTblReqFrom.setDateValue(DateUtil.getStringDate((DateUtil.addDays(tabToDate, 1))));
                    k++;
                }
            }
        }
        enableDisablePanSan(true);
        btnLeaveSanDelete.setEnabled(false);
        btnLeaveSanNew.setEnabled(false);
    }//GEN-LAST:event_btnLeaveSanNewActionPerformed
    
    private void btnLeaveSanSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLeaveSanSaveActionPerformed
        // TODO add your handling code here:
        try{
            long days= CommonUtil.convertObjToLong(txtTabDays.getText());
            long dateDiff=DateUtil.dateDiff(DateUtil.getDateWithoutMinitues(tdtTblReqFrom.getDateValue()),DateUtil.getDateWithoutMinitues(tdtTblReqTo.getDateValue()));
            dateDiff=dateDiff+1;
            if(days!=dateDiff){
                ClientUtil.displayAlert("Leave Request Dates Is Not Equal To The Number of Days Entered");
                txtTabDays.setText("");
                return;
            }
            if(tblSan.getRowCount()>0){
                for(int k=0; k<tblSan.getRowCount();k++){
                    String leavType=CommonUtil.convertObjToStr(tblSan.getValueAt(k,1));
                    if(leavType.equalsIgnoreCase(((ComboBoxModel)cbotblLeaveType.getModel()).getKeyForSelected().toString()) && !updateMode) {
                        ClientUtil.displayAlert("Leave Type Already Exists in Table");
                        return;
                    }
                    String tblFromDate=CommonUtil.convertObjToStr(tblSan.getValueAt(k,3));
                    Date tabFromDate = DateUtil.getDateWithoutMinitues(tblFromDate);
                    String tblToDate=CommonUtil.convertObjToStr(tblSan.getValueAt(k,4));
                    Date tabToDate = DateUtil.getDateWithoutMinitues(tblToDate);
                    Date enteredFromDate = DateUtil.getDateWithoutMinitues(tdtTblReqFrom.getDateValue());
                    Date enteredToDate = DateUtil.getDateWithoutMinitues(tdtTblReqTo.getDateValue());
                    if(!updateMode){
                        if(((enteredFromDate.after(tabFromDate) || enteredFromDate.compareTo(tabFromDate) == 0) && (enteredFromDate.before((tabToDate))) || enteredFromDate.compareTo(tabToDate) == 0)) {
                            ClientUtil.displayAlert("From Date Should Be"+" "+DateUtil.addDaysProperFormat(tabToDate,1));
                            return;
                        }
                        if(((enteredToDate.after(tabFromDate) || enteredToDate.compareTo(tabFromDate) == 0) && (enteredToDate.before((tabToDate))) || enteredToDate.compareTo(tabToDate) == 0)) {
                            ClientUtil.displayAlert("Check For To Date");
                            return;
                        }
                    }
                }
            }
            
            HashMap payMap = new HashMap();
            payMap.put("LEAVE_TYPE",((ComboBoxModel)cbotblLeaveType.getModel()).getKeyForSelected().toString());
            List payList = ClientUtil.executeQuery("getPaymentType", payMap);
            if(payList!=null && payList.size()>0){
                payMap=(HashMap)payList.get(0);
                if(payMap.get("PAYMENT_TYPE").equals("FULL_PAY") &&((ComboBoxModel)cboPayType.getModel()).getKeyForSelected().toString().equals("HALF_PAY")){
                    ClientUtil.displayAlert("The Payment Type For This Leave Is Declared As FULL_PAYMENT");
                    return;
                }
            }
            updateOBFields();
            observable.addToTable(updateTab,updateMode);
            observable.resetPanSan();
            cbotblLeaveType.setSelectedItem("");
            enableDisablePanSan(false);
            resetFields();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnLeaveSanSaveActionPerformed
    
    private void cboLeaveTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboLeaveTypeItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cboLeaveTypeItemStateChanged
    
    private void cboLeaveTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboLeaveTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboLeaveTypeActionPerformed
    
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        tabShareProduct.remove(panLeaveReq);
        panEnq.setVisible(false);
        tabShareProduct.add(panApplication,"Leave Application",0);
        tabShareProduct.add(panApplSan,"Leave Sanction",1);
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        btnCheck();
        callView("VIEW");
    }//GEN-LAST:event_btnViewActionPerformed
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        tabShareProduct.remove(panLeaveReq);
        tabShareProduct.add(panApplication,"Leave Application",0);
        tabShareProduct.add(panApplSan,"Leave Sanction",1);
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:]
        tabShareProduct.remove(panLeaveReq);
        panEnq.setVisible(false);
        tabShareProduct.add(panApplication,"Leave Application",0);
        tabShareProduct.add(panApplSan,"Leave Sanction",1);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // TODO add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // TODO add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed
    
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
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // TODO add your handling code here:
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        setModified(false);
        deletescreenLock();
        //        super.removeEditLock(((ComboBoxModel)cboShareType.getModel()).getKeyForSelected().toString());
        observable.resetForm();
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panLeaveManagement, false);
        ClientUtil.enableDisable(panSanEmp,false);
        ClientUtil.enableDisable(panEnq,false);
        ClientUtil.enableDisable(panBtnLeaveSan,false);
        ClientUtil.enableDisable(panSanTable,false);
        ClientUtil.enableDisable(panApplSan,false);
        ClientUtil.enableDisable(panApplication,false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        setButtonEnableDisable();
        viewType = "";
        removeRadioButtons();
        rdoApp_Yes.setSelected(false);
        rdoSan_Yes.setSelected(false);
        addRadioButtons();
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        btnView.setEnabled(true);
        lblEmpName.setText("");
        extDate=null;
        isFilled = false;
        clearEnqData();
        setButtonReset(false);
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        tabShareProduct.remove(panLeaveReq);
        panEnq.setVisible(false);
        tabShareProduct.add(panApplication,"Leave Application",0);
        tabShareProduct.add(panApplSan,"Leave Sanction",1);
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        callView("Delete");
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        ClientUtil.clearAll(this);
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        ClientUtil.enableDisable(panBtnLeaveSan, false);
        ClientUtil.enableDisable(panAppSan, true);
        ClientUtil.enableDisable(panSanEmp,true);
        ClientUtil.enableDisable(panEnq,true);
        ClientUtil.enableDisable(panBtnEmployeeLoan,true);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        tabShareProduct.remove(panApplication);
        tabShareProduct.remove(panApplSan);
        tabShareProduct.remove(panLeaveReq);
        panEnq.setVisible(true);
        panAppSan.setVisible(true);
        tabShareProduct.add(panApplSan,"Leave Appl/San",0);
        tabShareProduct.add(panLeaveReq,"Leave Process",1);
        btnEnq.setEnabled(true);
        btnLeaveSanNew.setEnabled(true);
        btnCalculate.setEnabled(true);
        btnEmp.setEnabled(true);
        btnNew.setEnabled(false);
        btnEdit.setEnabled(false);
        btnSave.setEnabled(true);
        btnCancel.setEnabled(true);
        btnDelete.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        setModified(false);
        savePerformed("SAVE");
        ClientUtil.clearAll(this);
        btnSave.setEnabled(false);
        //        btnCancelActionPerformed(null);
        ClientUtil.enableDisable(panLeaveManagement, false);
        ClientUtil.enableDisable(panSanEmp,false);
        ClientUtil.enableDisable(panEnq,false);
        ClientUtil.enableDisable(panBtnLeaveSan,false);
        ClientUtil.enableDisable(panSanTable,false);
        ClientUtil.enableDisable(panApplSan,false);
        ClientUtil.enableDisable(panApplication,false);
        clearEnqData();
        setButtonReset(false);
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        setModified(true);
        observable.resetForm();
        ClientUtil.clearAll(this);
        if(panLeaveReq.isShowing()){
            ClientUtil.enableDisable(panLeaveReq, true);
            cboLeaveType.setEnabled(true);
            txtEmpID.setEnabled(true);
            btnEmpProcess.setEnabled(true);
            btnProcess.setEnabled(true);
        }
        else{
            ClientUtil.enableDisable(panAppSan, false);
            ClientUtil.enableDisable(panBtnLeaveSan, false);
            ClientUtil.enableDisable(panAppSan, true);
            ClientUtil.enableDisable(panSanEmp,true);
            ClientUtil.enableDisable(panEnq,true);
            ClientUtil.enableDisable(panBtnEmployeeLoan,true);
            tabShareProduct.remove(panApplication);
            tabShareProduct.remove(panApplSan);
            tabShareProduct.remove(panLeaveReq);
            panEnq.setVisible(true);
            tabShareProduct.add(panApplSan,"Leave Appl/San",0);
            tabShareProduct.add(panLeaveReq,"Leave Process",1);
            panAppSan.setVisible(true);
            btnEmp.setEnabled(true);
            btnEnq.setEnabled(true);
            btnLeaveSanNew.setEnabled(true);
            btnCalculate.setEnabled(true);
            btnEmp.setEnabled(true);
        }
        setButtonEnableDisable();
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        btnCancel.setEnabled(true);
    }//GEN-LAST:event_btnNewActionPerformed
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
    private void clearEnqData(){
        lblLeaveBalanceAsOnVal.setText(" ");
        lblLeaveLastCrDtVal.setText("");
        lblLeaveNextCrDt.setText("");
        lblEmpName.setText("");
    }
    
    private void resetFields(){
        cbotblLeaveType.setSelectedItem("");
        cboPayType.setSelectedItem("");
        txtTabDays.setText("");
        tdtTblReqFrom.setDateValue("");
        tdtTblReqTo.setDateValue("");
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCalculate;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnEmp;
    private com.see.truetransact.uicomponent.CButton btnEmp1;
    private com.see.truetransact.uicomponent.CButton btnEmpProcess;
    private com.see.truetransact.uicomponent.CButton btnEnq;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnLeaveSanDelete;
    private com.see.truetransact.uicomponent.CButton btnLeaveSanDelete1;
    private com.see.truetransact.uicomponent.CButton btnLeaveSanNew;
    private com.see.truetransact.uicomponent.CButton btnLeaveSanNew1;
    private com.see.truetransact.uicomponent.CButton btnLeaveSanSave;
    private com.see.truetransact.uicomponent.CButton btnLeaveSanSave1;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnProcess;
    private com.see.truetransact.uicomponent.CButton btnRejAppl;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboEncashMentData;
    private com.see.truetransact.uicomponent.CComboBox cboEncashMentData1;
    private com.see.truetransact.uicomponent.CComboBox cboLeaveType;
    private com.see.truetransact.uicomponent.CComboBox cboLeaveType1;
    private com.see.truetransact.uicomponent.CComboBox cboPayType;
    private com.see.truetransact.uicomponent.CComboBox cboPayType1;
    private com.see.truetransact.uicomponent.CComboBox cboProcessType;
    private com.see.truetransact.uicomponent.CComboBox cboProcessType1;
    private com.see.truetransact.uicomponent.CComboBox cboSanAuth;
    private com.see.truetransact.uicomponent.CComboBox cboSanAuth1;
    private com.see.truetransact.uicomponent.CComboBox cbotblLeaveType;
    private com.see.truetransact.uicomponent.CComboBox cbotblLeaveType1;
    private com.see.truetransact.uicomponent.CCheckBox chkLeaveCancel;
    private com.see.truetransact.uicomponent.CCheckBox chkLeaveCancel1;
    private com.see.truetransact.uicomponent.CCheckBox chkLeaveEncash;
    private com.see.truetransact.uicomponent.CCheckBox chkLeaveEncash1;
    private com.see.truetransact.uicomponent.CCheckBox chkLtc;
    private com.see.truetransact.uicomponent.CCheckBox chkLtc1;
    private com.see.truetransact.uicomponent.CLabel lblDateOfJoin10;
    private com.see.truetransact.uicomponent.CLabel lblDateOfJoin11;
    private com.see.truetransact.uicomponent.CLabel lblDateOfJoin12;
    private com.see.truetransact.uicomponent.CLabel lblDateOfJoin9;
    private com.see.truetransact.uicomponent.CLabel lblEmpID;
    private com.see.truetransact.uicomponent.CLabel lblEmpName;
    private com.see.truetransact.uicomponent.CLabel lblEmpName1;
    private com.see.truetransact.uicomponent.CLabel lblEnCashDays;
    private com.see.truetransact.uicomponent.CLabel lblEnCashDays1;
    private com.see.truetransact.uicomponent.CLabel lblEnqLeaveType;
    private com.see.truetransact.uicomponent.CLabel lblLeavEncash;
    private com.see.truetransact.uicomponent.CLabel lblLeavEncash1;
    private com.see.truetransact.uicomponent.CLabel lblLeaveBalanceAsOn;
    private com.see.truetransact.uicomponent.CLabel lblLeaveBalanceAsOnVal;
    private com.see.truetransact.uicomponent.CLabel lblLeaveCancel;
    private com.see.truetransact.uicomponent.CLabel lblLeaveCancel1;
    private com.see.truetransact.uicomponent.CLabel lblLeaveID;
    private com.see.truetransact.uicomponent.CLabel lblLeaveID1;
    private com.see.truetransact.uicomponent.CLabel lblLeaveLastCrDt;
    private com.see.truetransact.uicomponent.CLabel lblLeaveLastCrDtVal;
    private com.see.truetransact.uicomponent.CLabel lblLeaveNextCrDt;
    private com.see.truetransact.uicomponent.CLabel lblLeaveNextCrDt1;
    private com.see.truetransact.uicomponent.CLabel lblLeaveType;
    private com.see.truetransact.uicomponent.CLabel lblLeaveType10;
    private com.see.truetransact.uicomponent.CLabel lblLeaveType9;
    private com.see.truetransact.uicomponent.CLabel lblLoanRateofInterest23;
    private com.see.truetransact.uicomponent.CLabel lblLoanRateofInterest24;
    private com.see.truetransact.uicomponent.CLabel lblLoanSanctionDate10;
    private com.see.truetransact.uicomponent.CLabel lblLoanSanctionDate9;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblPFNumber10;
    private com.see.truetransact.uicomponent.CLabel lblPFNumber5;
    private com.see.truetransact.uicomponent.CLabel lblPFNumber6;
    private com.see.truetransact.uicomponent.CLabel lblPFNumber7;
    private com.see.truetransact.uicomponent.CLabel lblPFNumber8;
    private com.see.truetransact.uicomponent.CLabel lblPFNumber9;
    private com.see.truetransact.uicomponent.CLabel lblPayType;
    private com.see.truetransact.uicomponent.CLabel lblPayType1;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
    private com.see.truetransact.uicomponent.CLabel lblRemarks1;
    private com.see.truetransact.uicomponent.CLabel lblReportedOn3;
    private com.see.truetransact.uicomponent.CLabel lblReportedOn4;
    private com.see.truetransact.uicomponent.CLabel lblSanAuth;
    private com.see.truetransact.uicomponent.CLabel lblSanAuth1;
    private com.see.truetransact.uicomponent.CLabel lblSanEmpID;
    private com.see.truetransact.uicomponent.CLabel lblSanEmpID1;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace29;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace30;
    private com.see.truetransact.uicomponent.CLabel lblSpace31;
    private com.see.truetransact.uicomponent.CLabel lblSpace32;
    private com.see.truetransact.uicomponent.CLabel lblSpace33;
    private com.see.truetransact.uicomponent.CLabel lblSpace34;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTabDays;
    private com.see.truetransact.uicomponent.CLabel lblTabDays1;
    private com.see.truetransact.uicomponent.CLabel lblTabLeaveType;
    private com.see.truetransact.uicomponent.CLabel lblTabLeaveType1;
    private com.see.truetransact.uicomponent.CLabel lblTblReqFrom;
    private com.see.truetransact.uicomponent.CLabel lblTblReqFrom1;
    private com.see.truetransact.uicomponent.CLabel lblTblReqTo;
    private com.see.truetransact.uicomponent.CLabel lblTblReqTo1;
    private com.see.truetransact.uicomponent.CLabel lblTypeOfBlk;
    private com.see.truetransact.uicomponent.CLabel lblTypeOfBlk1;
    private com.see.truetransact.uicomponent.CLabel lblWithLtc;
    private com.see.truetransact.uicomponent.CLabel lblWithLtc1;
    private com.see.truetransact.uicomponent.CMenuBar mbrShareProduct;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAccAllowedPeriod;
    private com.see.truetransact.uicomponent.CPanel panAppSan;
    private com.see.truetransact.uicomponent.CPanel panApplSan;
    private com.see.truetransact.uicomponent.CPanel panApplication;
    private com.see.truetransact.uicomponent.CPanel panBlock;
    private com.see.truetransact.uicomponent.CPanel panBlock1;
    private com.see.truetransact.uicomponent.CPanel panBtnEmployeeLoan;
    private com.see.truetransact.uicomponent.CPanel panBtnEmployeeLoan1;
    private com.see.truetransact.uicomponent.CPanel panBtnLeaveSan;
    private com.see.truetransact.uicomponent.CPanel panBtnLeaveSan1;
    private com.see.truetransact.uicomponent.CPanel panEnq;
    private com.see.truetransact.uicomponent.CPanel panLeaveManagement;
    private com.see.truetransact.uicomponent.CPanel panLeaveReq;
    private com.see.truetransact.uicomponent.CPanel panLeaveSan;
    private com.see.truetransact.uicomponent.CPanel panLeaveSan1;
    private com.see.truetransact.uicomponent.CPanel panSanEmp;
    private com.see.truetransact.uicomponent.CPanel panSanEmp1;
    private com.see.truetransact.uicomponent.CPanel panSanTable;
    private com.see.truetransact.uicomponent.CPanel panSanTable1;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CRadioButton rdoApp_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoApplSan;
    private com.see.truetransact.uicomponent.CButtonGroup rdoBlock;
    private com.see.truetransact.uicomponent.CRadioButton rdoFourYr;
    private com.see.truetransact.uicomponent.CRadioButton rdoFourYr1;
    private com.see.truetransact.uicomponent.CRadioButton rdoSan_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoTwoYr;
    private com.see.truetransact.uicomponent.CRadioButton rdoTwoYr1;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CScrollPane srpFamily6;
    private com.see.truetransact.uicomponent.CScrollPane srpFamily7;
    private com.see.truetransact.uicomponent.CTabbedPane tabShareProduct;
    private com.see.truetransact.uicomponent.CTable tblSan;
    private com.see.truetransact.uicomponent.CTable tblSan1;
    private javax.swing.JToolBar tbrLeave;
    private com.see.truetransact.uicomponent.CDateField tdtLvAplDt;
    private com.see.truetransact.uicomponent.CDateField tdtLvAplDt1;
    private com.see.truetransact.uicomponent.CDateField tdtReqFrom;
    private com.see.truetransact.uicomponent.CDateField tdtReqFrom1;
    private com.see.truetransact.uicomponent.CDateField tdtReqTo;
    private com.see.truetransact.uicomponent.CDateField tdtReqTo1;
    private com.see.truetransact.uicomponent.CDateField tdtSanDate;
    private com.see.truetransact.uicomponent.CDateField tdtSanDate1;
    private com.see.truetransact.uicomponent.CDateField tdtTblReqFrom;
    private com.see.truetransact.uicomponent.CDateField tdtTblReqFrom1;
    private com.see.truetransact.uicomponent.CDateField tdtTblReqTo;
    private com.see.truetransact.uicomponent.CDateField tdtTblReqTo1;
    private com.see.truetransact.uicomponent.CTextField txtEmpID;
    private com.see.truetransact.uicomponent.CTextField txtLeavEncah;
    private com.see.truetransact.uicomponent.CTextField txtLeavEncah1;
    private com.see.truetransact.uicomponent.CTextField txtLeavePurpose;
    private com.see.truetransact.uicomponent.CTextField txtLeavePurpose1;
    private com.see.truetransact.uicomponent.CTextField txtNoOfDays;
    private com.see.truetransact.uicomponent.CTextField txtNoOfDays1;
    private com.see.truetransact.uicomponent.CTextField txtOldSanRefNo;
    private com.see.truetransact.uicomponent.CTextField txtOldSanRefNo1;
    private com.see.truetransact.uicomponent.CTextField txtRemarks;
    private com.see.truetransact.uicomponent.CTextField txtRemarks1;
    private com.see.truetransact.uicomponent.CTextField txtSanEmpID;
    private com.see.truetransact.uicomponent.CTextField txtSanEmpID1;
    private com.see.truetransact.uicomponent.CTextField txtSanRefNo;
    private com.see.truetransact.uicomponent.CTextField txtSanRefNo1;
    private com.see.truetransact.uicomponent.CTextField txtTabDays;
    private com.see.truetransact.uicomponent.CTextField txtTabDays1;
    // End of variables declaration//GEN-END:variables
    
}
