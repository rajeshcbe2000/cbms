/**
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LeaveManagementUI.java
 *
 * Created on November 23, 2004, 4:00 PM
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
import java.util.Date;

public class LeaveManagementUI extends CInternalFrame implements UIMandatoryField, Observer{
    
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private HashMap mandatoryMap;
    private LeaveManagementOB observable;
    private LeaveManagementMRB objMandatoryRB;
    private ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.sysadmin.leavemanagement.LeaveManagementRB", ProxyParameters.LANGUAGE);
    private String viewType = new String();
    final String AUTHORIZE="Authorize";
    private boolean dataExists = false;
    private int tblRowCount;
    private boolean loanTypeExists = false;
    private int existingRowcount;
    private Date currDt = null;
    /** Creates new form LeaveManagementUI */
    public LeaveManagementUI() {
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
        txtLeaveId.setEnabled(false);
        txtMaternityCountLimit.setText("");
        txtMaternityCountLimit.setEnabled(false);
        txtMaternityCountLimit.setVisible(false);
        lblMaternityCountLimit.setVisible(false);
//        lblMaternityCountLimit.setVisible(false);
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
        lblTypeOfLeave.setName("lblTypeOfLeave");
        txtTypeOfLeave.setName("txtTypeOfLeave");
        txtDesc.setName("txtDesc");
        lblDesc.setName("lblDesc");
        lblCreditType.setName("lblCreditType");
        cboCreditType.setName("cboCreditType");
        panLeaveLapses.setName("panLeaveLapses");
        lblLeaveLapses.setName("lblLeaveLapses");
        lblCarryOver.setName("lblCarryOver");
        lblAccAllowed.setName("lblAccAllowed");
        txtCarryOverYears.setName("txtCarryOverYears");
        panAccAllowed.setName("panAccAllowed");
        cboParForLeave.setName("cboParForLeave");
        lblParForLeave.setName("lblParForLeave");
        panFixedPar.setName("panFixedPar");
        txtFixedPar.setName("txtFixedPar");
        lblFixedLeave.setName("lblFixedLeave");
        lblProRatelbl2.setName("lblProRatelbl2");
        lblProRatelbl1.setName("lblProRatelbl1");
        lblProRatelbl3.setName("lblProRatelbl3");
        txtPro1.setName("txtPro1");
        txtPro2.setName("txtPro2");
        lblMaxLeaves.setName("lblMaxLeaves");
        txtMaxLeaves.setName("txtMaxLeaves");
        lblEncash.setName("lblEncash");
        lblMaxEncash.setName("lblMaxEncash");
        panEncash.setName("panEncash");
        txtMaxEncashment.setName("txtMaxEncashment");
        panLeave.setName("panLeave");
        panLeaveManagement.setName("panLeaveManagement");
        panLeaveLapses.setName("panLeaveLapses");
        cboParFixed.setName("cboParFixed");
        cboAcc.setName("cboAcc");
        txtLeaveId.setName("txtLeaveId");
        chkLtc.setName("chkLtc");
        panFixedPar1.setName("panFixedPar1");
        panProRatePar1.setName("panProRatePar1");
        lblPayType.setName("lblPayType");
        panPaymentType.setName("panPaymentType");
        rdoFull.setName("rdoFull");
        rdoHalf.setName("rdoHalf");
        lblCrDays.setName("lblCrDays");
        lblCrDays1.setName("lblCrDays1");
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
    }
    
/* Auto Generated Method - setMandatoryHashMap()
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtTypeOfLeave", new Boolean(true));
        mandatoryMap.put("txtDesc", new Boolean(true));
        mandatoryMap.put("cboCreditType", new Boolean(true));
        mandatoryMap.put("rdoIntroReq_Yes1", new Boolean(true));
        mandatoryMap.put("rdoAcc_Yes", new Boolean(true));
        mandatoryMap.put("cboParForLeave", new Boolean(true));
    }
    
/* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    
    /* Creates the insstance of LeaveManagement which acts as  Observable to
     *LeaveManagement UI */
    private void setObservable() {
        try{
            observable = LeaveManagementOB.getInstance();
            observable.addObserver(this);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /* Sets the model for the comboboxes in the UI    */
    private void initComponentData() {
        try{
            cboCreditType.setModel(observable.getCbmToBeCredited());
            cboAcc.setModel(observable.getCbmAcc());
            cboParForLeave.setModel(observable.getCbmParForLeave());
            cboParFixed.setModel(observable.getCbmParFixed());
        }catch(ClassCastException e){
            parseException.logException(e,true);
        }
    }
    
/* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        objMandatoryRB = new LeaveManagementMRB();
        
    }
    
    
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        removeRadioButtons();
        txtTypeOfLeave.setText(CommonUtil.convertObjToStr(observable.getTypeOfLeave()));
        txtCarryOverYears.setText(CommonUtil.convertObjToStr(observable.getCarryOver()));
        txtDesc.setText(CommonUtil.convertObjToStr(observable.getDesc()));
        txtFixedPar1.setText(CommonUtil.convertObjToStr(observable.getTxtFixedPar1()));
        txtFixedPar.setText(CommonUtil.convertObjToStr(observable.getTxtFixedPar()));
        txtPro1.setText(CommonUtil.convertObjToStr(observable.getTxtPro1()));
        txtPro2.setText(CommonUtil.convertObjToStr(observable.getTxtPro2()));
        txtMaxLeaves.setText(CommonUtil.convertObjToStr(observable.getTxtMaxLeaves()));
        txtMaternityCountLimit.setText(CommonUtil.convertObjToStr(observable.getTxtMaternityCountLimit()));
        txtMaxEncashment.setText(CommonUtil.convertObjToStr(observable.getTxtMaxEncashment()));
        cboAcc.setModel(observable.getCbmAcc());
        cboParFixed.setModel(observable.getCbmParFixed());
        cboParForLeave.setModel(observable.getCbmParForLeave());
        cboCreditType.setModel(observable.getCbmToBeCredited());
        rdoAcc_No.setSelected(observable.isRdoAcc_No());
        rdoAcc_Yes.setSelected(observable.isRdoAcc_Yes());
        rdoEncash_No1.setSelected(observable.isRdoEncash_No1());
        rdoEncash_Yes1.setSelected(observable.isRdoEncash_Yes1());
        rdoIntroReq_No1.setSelected(observable.isRdoIntroReq_No1());
        rdoIntroReq_Yes1.setSelected(observable.isRdoIntroReq_Yes1());
        txtAccText.setText(observable.getTxtAcc());
        txtLeaveId.setText(observable.getLeaveID());
        chkLtc.setSelected(observable.isChkLtc());
        chkMaternityLeave.setSelected(observable.isChkMaternityLeave());
        rdoFull.setSelected(observable.isRdoFull());
        rdoHalf.setSelected(observable.isRdoHalf());
        addRadioButtons();
    }
    
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setTypeOfLeave(txtTypeOfLeave.getText());
        observable.setCarryOver(txtCarryOverYears.getText());
        observable.setDesc(txtDesc.getText());
        observable.setCboAcc((String) cboAcc.getSelectedItem());
        observable.setCboParFixed((String) cboParFixed.getSelectedItem());
        observable.setCboParForLeave((String) cboParForLeave.getSelectedItem());
        observable.setCboToBeCredited((String) cboCreditType.getSelectedItem());
        observable.setTxtFixedPar1(txtFixedPar1.getText());
        observable.setTxtFixedPar(txtFixedPar.getText());
        observable.setTxtPro1(txtPro1.getText());
        observable.setTxtPro2(txtPro2.getText());
        observable.setTxtMaxLeaves(txtMaxLeaves.getText());
        observable.setTxtMaxEncashment(txtMaxEncashment.getText());
        observable.setTxtMaternityCountLimit(txtMaternityCountLimit.getText());
        observable.setRdoIntroReq_No1(rdoIntroReq_No1.isSelected());
        observable.setRdoIntroReq_Yes1(rdoIntroReq_Yes1.isSelected());
        observable.setRdoAcc_No(rdoAcc_No.isSelected());
        observable.setRdoAcc_Yes(rdoAcc_Yes.isSelected());
        observable.setRdoEncash_No1(rdoEncash_No1.isSelected());
        observable.setRdoEncash_Yes1(rdoEncash_Yes1.isSelected());
        observable.setTxtAcc(txtAccText.getText());
        observable.setLeaveID(txtLeaveId.getText());
        observable.setChkLtc(chkLtc.isSelected());
        observable.setChkMaternityLeave(chkMaternityLeave.isSelected());
        observable.setRdoFull(rdoFull.isSelected());
        observable.setRdoHalf(rdoHalf.isSelected());
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
    
    private void removeRadioButtons() {
        rdoIntroReq.remove(rdoIntroReq_No1);
        rdoIntroReq.remove(rdoIntroReq_Yes1);
        rdoAcc.remove(rdoAcc_No);
        rdoAcc.remove(rdoAcc_Yes);
        rdoEncash.remove(rdoEncash_No1);
        rdoEncash.remove(rdoEncash_Yes1);
        rdoPaymentType.remove(rdoFull);
        rdoPaymentType.remove(rdoHalf);
    }
    
    private void addRadioButtons() {
        rdoIntroReq = new CButtonGroup();
        rdoIntroReq.add(rdoIntroReq_No1);
        rdoIntroReq.add(rdoIntroReq_Yes1);
        
        rdoAcc = new CButtonGroup();
        rdoAcc.add(rdoAcc_No);
        rdoAcc.add(rdoAcc_Yes);
        
        rdoEncash = new CButtonGroup();
        rdoEncash.add(rdoEncash_No1);
        rdoEncash.add(rdoEncash_Yes1);
        
        rdoPaymentType = new CButtonGroup();
        rdoPaymentType.add(rdoFull);
        rdoPaymentType.add(rdoHalf);
    }
    
    /** Setting up Lengths for the TextFields in theu UI */
    private void setMaxLength(){
        txtTypeOfLeave.setAllowAll(true);
        txtDesc.setAllowAll(true);
        
    }
    
    /* Does necessary operaion when user clicks the save button */
    private void savePerformed(){
        updateOBFields();
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panLeave);
        if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0 ){
            displayAlert(mandatoryMessage);
        }
        else{
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
    
    /* Calls the execute method of LeaveManagementOB to do insertion or updation or deletion */
    private void saveAction(String status){
        //To check mandtoryness of the Terminal panAcHdDetails,panAcHeadDetails panel and diplay appropriate
        //error message, else proceed
        try{
            //            final String mandatoryMessage = checkMandatory(panLeave);
            //            String alertMsg ="";
            //            if(mandatoryMessage.length() > 0 ){
            //                displayAlert(mandatoryMessage);
            //            }else{
            observable.execute(status);
            //__ if the Action is not Falied, Reset the fields...
            if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED){
                HashMap lockMap = new HashMap();
                ArrayList lst = new ArrayList();
                lst.add("LEAVE_ID");
                lockMap.put("LEAVE_ID",CommonUtil.convertObjToStr(txtLeaveId.getText()));
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
                    lockMap.put("LEAVE_ID", observable.getLeaveID());
                }
                setEditLockMap(lockMap);
                setEditLock();
                deletescreenLock();
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
        if (currField.equals(ClientConstants.ACTION_STATUS[2])  ||
        currField.equals(ClientConstants.ACTION_STATUS[3]) || currField.equals(ClientConstants.ACTION_STATUS[17])) {
            viewMap.put(CommonConstants.MAP_NAME, "getSelectLeave");
        }else {
            viewMap.put(CommonConstants.MAP_NAME, "getSelectLeave");
        }
        new ViewAll(this,viewMap).show();
    }
    
    /* This method is used to fill up all tbe UIFields after the user
     *selects the desired row in the popup */
    public void fillData(Object  map) {
        setModified(true);
        HashMap hash = (HashMap) map;
        if (viewType != null) {
            if (viewType.equals(ClientConstants.ACTION_STATUS[2]) ||
            viewType.equals(ClientConstants.ACTION_STATUS[3])|| viewType.equals(AUTHORIZE) ||
            viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                hash.put(CommonConstants.MAP_WHERE, hash.get("LEAVE_ID"));
                hash.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                observable.populateData(hash);
                if (viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) ||
                viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                    ClientUtil.enableDisable(panLeaveManagement, false);
                } else {
                    ClientUtil.enableDisable(panLeaveManagement, true);
                }
                setButtonEnableDisable();
                if(viewType ==  AUTHORIZE) {
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                }
            }
            
        }
        if(viewType.equals(ClientConstants.ACTION_STATUS[2]) || viewType.equals(AUTHORIZE)) {
            HashMap screenMap = new HashMap();
            screenMap.put("TRANS_ID",hash.get("LEAVE_ID"));
            screenMap.put("TRANS_DT", currDt.clone());
            screenMap.put("USER_ID",ProxyParameters.USER_ID);
            screenMap.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
            List lst=ClientUtil.executeQuery("selectauthorizationLock", screenMap);
            if(lst !=null && lst.size()>0) {
                screenMap=null;
                StringBuffer open=new StringBuffer();
                for(int i=0;i<lst.size();i++){
                    screenMap=(HashMap)lst.get(i);
                    open.append("\n"+"User Id  :"+" ");
                    open.append(CommonUtil.convertObjToStr(screenMap.get("OPEN_BY"))+"\n");
                    open.append("Mode Of Operation  :" +" ");
                    open.append(CommonUtil.convertObjToStr(screenMap.get("MODE_OF_OPERATION"))+" ");
                    btnSave.setEnabled(false);
                }
                ClientUtil.showMessageWindow("already open by"+open);
                return;
            }
            else{
                hash.put("TRANS_ID",hash.get("LEAVE_ID"));
                if(viewType.equals(ClientConstants.ACTION_STATUS[2]))
                    hash.put("MODE_OF_OPERATION","EDIT");
                if(viewType==AUTHORIZE)
                    hash.put("MODE_OF_OPERATION","AUTHORIZE");
                hash.put("USER_ID",TrueTransactMain.USER_ID);
                hash.put("TRANS_DT", currDt);
                hash.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
                ClientUtil.execute("insertauthorizationLock", hash);
            }
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
            if (lockedBy.length() > 0 && !lockedBy.equals(ProxyParameters.USER_ID)) {
                String data = getLockDetails(lockedBy, getScreenID()) ;
                ClientUtil.showMessageWindow("Selected Record is Opened/Modified by " + lockedBy + data.toString());
                btnSave.setEnabled(false);
            }
            setFieldsEnableDisable();
        }
        
    }
    
    public void setFieldsEnableDisable(){
        if(rdoIntroReq_Yes1.isSelected()){
            txtCarryOverYears.setEnabled(false);
        }
        if(rdoAcc_No.isSelected()){
            txtAccText.setEnabled(false);
            cboAcc.setEnabled(false);
        }
        if(rdoEncash_No1.isSelected()){
            chkLtc.setEnabled(false);
            txtMaxEncashment.setEnabled(false);
        }
        String par= CommonUtil.convertObjToStr(cboParForLeave.getSelectedItem());
        if(par.equalsIgnoreCase("FIXED")){
            txtPro1.setEnabled(false);
            txtPro2.setEnabled(false);
        }
        else if(par.equalsIgnoreCase("PRORATE")){
            txtFixedPar1.setEnabled(false);
            txtFixedPar.setEnabled(false);
            cboParFixed.setEnabled(false);
        }
    }
    
    /** This will show the alertwindow when the user enters the already existing ShareType **/
    private int showAlertWindow(String alertMsg) throws Exception {
        int optionSelected = 1;
        String[] options = {resourceBundle.getString("cDialogOK")};
        optionSelected = COptionPane.showOptionDialog(null,resourceBundle.getString(alertMsg), CommonConstants.INFORMATIONTITLE,
        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
        null, options, options[0]);
        return optionSelected;
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
        if (!viewType.equals(AUTHORIZE)){
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            mapParam.put(CommonConstants.MAP_NAME, "getLeaveAuthorizeList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeLeaveManagement");
            mapParam.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            setModified(true);
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
        } else if (viewType.equals(AUTHORIZE)){
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
            singleAuthorizeMap.put("AUTH_BY", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDt.clone());
            singleAuthorizeMap.put("LEAVE_ID", txtLeaveId.getText());
            singleAuthorizeMap.put("AUTH_DT",currDt.clone()) ;
            ClientUtil.execute("authorizeLeaveManagement", singleAuthorizeMap);
            viewType = "";
            super.setOpenForEditBy(observable.getStatusBy());
            super.removeEditLock(this.txtLeaveId.getText());
            btnCancelActionPerformed(null);
            observable.setResultStatus();
        }
    }
    
    public static void main(String args[]){
        javax.swing.JFrame frame = new javax.swing.JFrame();
        LeaveManagementUI ui = new LeaveManagementUI();
        frame.getContentPane().add(ui);
        ui.setVisible(true);
        frame.setVisible(true);
        frame.setSize(600,600);
        frame.show();
        ui.show();
    }
    
    /** Method to do the validation of Subscribed Capital and paidup captial to be greater
     *than the Authorized Capital **/
    private void captitalValidation(CTextField txtAuthorizedCapital, CTextField txtCapital){
        Double  authorizedCapital = CommonUtil.convertObjToDouble(txtAuthorizedCapital.getText());
        Double capital = CommonUtil.convertObjToDouble(txtCapital.getText());
        if(capital.doubleValue()>authorizedCapital.doubleValue()){
            txtCapital.setText("");
        }
        
    }
    
    
    private void deletescreenLock(){
        HashMap map=new HashMap();
        map.put("USER_ID",ProxyParameters.USER_ID);
        map.put("TRANS_DT", currDt);
        map.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
        ClientUtil.execute("DELETE_SCREEN_LOCK", map);
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoIntroReq = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoAcc = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoEncash = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoLtcLfc = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoPaymentType = new com.see.truetransact.uicomponent.CButtonGroup();
        panLeaveManagement = new com.see.truetransact.uicomponent.CPanel();
        tabShareProduct = new com.see.truetransact.uicomponent.CTabbedPane();
        panLeave = new com.see.truetransact.uicomponent.CPanel();
        lblTypeOfLeave = new com.see.truetransact.uicomponent.CLabel();
        cboCreditType = new com.see.truetransact.uicomponent.CComboBox();
        lblDesc = new com.see.truetransact.uicomponent.CLabel();
        lblCreditType = new com.see.truetransact.uicomponent.CLabel();
        lblLeaveLapses = new com.see.truetransact.uicomponent.CLabel();
        lblCarryOver = new com.see.truetransact.uicomponent.CLabel();
        lblParForLeave = new com.see.truetransact.uicomponent.CLabel();
        lblEncash = new com.see.truetransact.uicomponent.CLabel();
        txtMaxEncashment = new com.see.truetransact.uicomponent.CTextField();
        lblMaxEncash = new com.see.truetransact.uicomponent.CLabel();
        txtMaxLeaves = new com.see.truetransact.uicomponent.CTextField();
        lblMaxLeaves = new com.see.truetransact.uicomponent.CLabel();
        panAccAllowedPeriod = new com.see.truetransact.uicomponent.CPanel();
        txtAccText = new com.see.truetransact.uicomponent.CTextField();
        cboAcc = new com.see.truetransact.uicomponent.CComboBox();
        panFixedPar = new com.see.truetransact.uicomponent.CPanel();
        txtFixedPar = new com.see.truetransact.uicomponent.CTextField();
        cboParFixed = new com.see.truetransact.uicomponent.CComboBox();
        panLeaveLapses = new com.see.truetransact.uicomponent.CPanel();
        rdoIntroReq_Yes1 = new com.see.truetransact.uicomponent.CRadioButton();
        rdoIntroReq_No1 = new com.see.truetransact.uicomponent.CRadioButton();
        lblAccAllowed = new com.see.truetransact.uicomponent.CLabel();
        panAccAllowed = new com.see.truetransact.uicomponent.CPanel();
        rdoAcc_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoAcc_No = new com.see.truetransact.uicomponent.CRadioButton();
        cboParForLeave = new com.see.truetransact.uicomponent.CComboBox();
        panProRatePar = new com.see.truetransact.uicomponent.CPanel();
        txtPro2 = new com.see.truetransact.uicomponent.CTextField();
        lblProRatelbl3 = new com.see.truetransact.uicomponent.CLabel();
        panEncash = new com.see.truetransact.uicomponent.CPanel();
        rdoEncash_Yes1 = new com.see.truetransact.uicomponent.CRadioButton();
        rdoEncash_No1 = new com.see.truetransact.uicomponent.CRadioButton();
        txtTypeOfLeave = new com.see.truetransact.uicomponent.CTextField();
        txtDesc = new com.see.truetransact.uicomponent.CTextField();
        txtCarryOverYears = new com.see.truetransact.uicomponent.CTextField();
        txtLeaveId = new com.see.truetransact.uicomponent.CTextField();
        chkLtc = new com.see.truetransact.uicomponent.CCheckBox();
        lblLtcLfc = new com.see.truetransact.uicomponent.CLabel();
        panPaymentType = new com.see.truetransact.uicomponent.CPanel();
        rdoFull = new com.see.truetransact.uicomponent.CRadioButton();
        rdoHalf = new com.see.truetransact.uicomponent.CRadioButton();
        panFixedPar1 = new com.see.truetransact.uicomponent.CPanel();
        txtFixedPar1 = new com.see.truetransact.uicomponent.CTextField();
        lblFixedLeave = new com.see.truetransact.uicomponent.CLabel();
        panProRatePar1 = new com.see.truetransact.uicomponent.CPanel();
        lblProRatelbl1 = new com.see.truetransact.uicomponent.CLabel();
        txtPro1 = new com.see.truetransact.uicomponent.CTextField();
        lblProRatelbl2 = new com.see.truetransact.uicomponent.CLabel();
        lblPayType = new com.see.truetransact.uicomponent.CLabel();
        lblCrDays = new com.see.truetransact.uicomponent.CLabel();
        lblCrDays1 = new com.see.truetransact.uicomponent.CLabel();
        chkMaternityLeave = new com.see.truetransact.uicomponent.CCheckBox();
        lblMaternityLeave = new com.see.truetransact.uicomponent.CLabel();
        txtMaternityCountLimit = new com.see.truetransact.uicomponent.CTextField();
        lblMaternityCountLimit = new com.see.truetransact.uicomponent.CLabel();
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
        setMaximumSize(new java.awt.Dimension(750, 500));
        setMinimumSize(new java.awt.Dimension(750, 500));
        setPreferredSize(new java.awt.Dimension(750, 500));

        panLeaveManagement.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panLeaveManagement.setLayout(new java.awt.GridBagLayout());

        tabShareProduct.setMinimumSize(new java.awt.Dimension(769, 321));
        tabShareProduct.setPreferredSize(new java.awt.Dimension(823, 609));

        panLeave.setLayout(new java.awt.GridBagLayout());

        lblTypeOfLeave.setText("Type Of Leave");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeave.add(lblTypeOfLeave, gridBagConstraints);

        cboCreditType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCreditType.setNextFocusableComponent(rdoIntroReq_Yes1);
        cboCreditType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboCreditTypeItemStateChanged(evt);
            }
        });
        cboCreditType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCreditTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeave.add(cboCreditType, gridBagConstraints);

        lblDesc.setText("Descriptor");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeave.add(lblDesc, gridBagConstraints);

        lblCreditType.setText("To Be Credited");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeave.add(lblCreditType, gridBagConstraints);

        lblLeaveLapses.setText("Leave Lapses");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeave.add(lblLeaveLapses, gridBagConstraints);

        lblCarryOver.setText("Carry Over For How Many Years");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeave.add(lblCarryOver, gridBagConstraints);

        lblParForLeave.setText("Parameters For Leave Crediting");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeave.add(lblParForLeave, gridBagConstraints);

        lblEncash.setText("Leave Encashment Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeave.add(lblEncash, gridBagConstraints);

        txtMaxEncashment.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMaxEncashment.setNextFocusableComponent(rdoFull);
        txtMaxEncashment.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeave.add(txtMaxEncashment, gridBagConstraints);

        lblMaxEncash.setText("Max Encashment Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeave.add(lblMaxEncash, gridBagConstraints);

        txtMaxLeaves.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMaxLeaves.setNextFocusableComponent(rdoEncash_Yes1);
        txtMaxLeaves.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeave.add(txtMaxLeaves, gridBagConstraints);

        lblMaxLeaves.setText("Max Leave to Be Credited");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panLeave.add(lblMaxLeaves, gridBagConstraints);

        panAccAllowedPeriod.setLayout(new java.awt.GridBagLayout());

        txtAccText.setMinimumSize(new java.awt.Dimension(50, 21));
        txtAccText.setNextFocusableComponent(cboAcc);
        txtAccText.setPreferredSize(new java.awt.Dimension(50, 21));
        txtAccText.setValidation(new NumericValidation());
        txtAccText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAccTextActionPerformed(evt);
            }
        });
        txtAccText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAccTextFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccAllowedPeriod.add(txtAccText, gridBagConstraints);

        cboAcc.setMinimumSize(new java.awt.Dimension(70, 21));
        cboAcc.setNextFocusableComponent(txtMaxLeaves);
        cboAcc.setPreferredSize(new java.awt.Dimension(70, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 4);
        panAccAllowedPeriod.add(cboAcc, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panLeave.add(panAccAllowedPeriod, gridBagConstraints);

        panFixedPar.setMinimumSize(new java.awt.Dimension(100, 29));
        panFixedPar.setPreferredSize(new java.awt.Dimension(100, 29));
        panFixedPar.setLayout(new java.awt.GridBagLayout());

        txtFixedPar.setMinimumSize(new java.awt.Dimension(25, 21));
        txtFixedPar.setNextFocusableComponent(cboParFixed);
        txtFixedPar.setPreferredSize(new java.awt.Dimension(25, 21));
        txtFixedPar.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 2);
        panFixedPar.add(txtFixedPar, gridBagConstraints);

        cboParFixed.setMinimumSize(new java.awt.Dimension(70, 21));
        cboParFixed.setNextFocusableComponent(txtPro1);
        cboParFixed.setPreferredSize(new java.awt.Dimension(70, 21));
        cboParFixed.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboParFixedFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 4);
        panFixedPar.add(cboParFixed, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeave.add(panFixedPar, gridBagConstraints);

        panLeaveLapses.setLayout(new java.awt.GridBagLayout());

        rdoIntroReq_Yes1.setText("Yes");
        rdoIntroReq_Yes1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        rdoIntroReq_Yes1.setNextFocusableComponent(rdoIntroReq_No1);
        rdoIntroReq_Yes1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoIntroReq_Yes1ActionPerformed(evt);
            }
        });
        panLeaveLapses.add(rdoIntroReq_Yes1, new java.awt.GridBagConstraints());

        rdoIntroReq_No1.setText("No");
        rdoIntroReq_No1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        rdoIntroReq_No1.setNextFocusableComponent(txtCarryOverYears);
        rdoIntroReq_No1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoIntroReq_No1ActionPerformed(evt);
            }
        });
        panLeaveLapses.add(rdoIntroReq_No1, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 4);
        panLeave.add(panLeaveLapses, gridBagConstraints);

        lblAccAllowed.setText("Accumalation Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeave.add(lblAccAllowed, gridBagConstraints);

        panAccAllowed.setLayout(new java.awt.GridBagLayout());

        rdoAcc_Yes.setText("Yes");
        rdoAcc_Yes.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        rdoAcc_Yes.setNextFocusableComponent(rdoAcc_No);
        rdoAcc_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoAcc_YesActionPerformed(evt);
            }
        });
        panAccAllowed.add(rdoAcc_Yes, new java.awt.GridBagConstraints());

        rdoAcc_No.setText("No");
        rdoAcc_No.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        rdoAcc_No.setNextFocusableComponent(txtAccText);
        rdoAcc_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoAcc_NoActionPerformed(evt);
            }
        });
        panAccAllowed.add(rdoAcc_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 4);
        panLeave.add(panAccAllowed, gridBagConstraints);

        cboParForLeave.setMinimumSize(new java.awt.Dimension(100, 21));
        cboParForLeave.setNextFocusableComponent(txtFixedPar1);
        cboParForLeave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboParForLeaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeave.add(cboParForLeave, gridBagConstraints);

        panProRatePar.setMinimumSize(new java.awt.Dimension(120, 29));
        panProRatePar.setPreferredSize(new java.awt.Dimension(120, 29));
        panProRatePar.setLayout(new java.awt.GridBagLayout());

        txtPro2.setMinimumSize(new java.awt.Dimension(25, 21));
        txtPro2.setNextFocusableComponent(rdoAcc_Yes);
        txtPro2.setPreferredSize(new java.awt.Dimension(25, 21));
        txtPro2.setValidation(new NumericValidation());
        txtPro2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPro2FocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 2);
        panProRatePar.add(txtPro2, gridBagConstraints);

        lblProRatelbl3.setText("day/s Credited");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProRatePar.add(lblProRatelbl3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeave.add(panProRatePar, gridBagConstraints);

        panEncash.setLayout(new java.awt.GridBagLayout());

        rdoEncash_Yes1.setText("Yes");
        rdoEncash_Yes1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        rdoEncash_Yes1.setNextFocusableComponent(rdoEncash_No1);
        rdoEncash_Yes1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoEncash_Yes1ActionPerformed(evt);
            }
        });
        panEncash.add(rdoEncash_Yes1, new java.awt.GridBagConstraints());

        rdoEncash_No1.setText("No");
        rdoEncash_No1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        rdoEncash_No1.setNextFocusableComponent(chkLtc);
        rdoEncash_No1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoEncash_No1ActionPerformed(evt);
            }
        });
        panEncash.add(rdoEncash_No1, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 4);
        panLeave.add(panEncash, gridBagConstraints);

        txtTypeOfLeave.setEditable(false);
        txtTypeOfLeave.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTypeOfLeave.setNextFocusableComponent(txtDesc);
        txtTypeOfLeave.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTypeOfLeaveFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeave.add(txtTypeOfLeave, gridBagConstraints);

        txtDesc.setEditable(false);
        txtDesc.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDesc.setNextFocusableComponent(cboCreditType);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeave.add(txtDesc, gridBagConstraints);

        txtCarryOverYears.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCarryOverYears.setNextFocusableComponent(cboParForLeave);
        txtCarryOverYears.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeave.add(txtCarryOverYears, gridBagConstraints);

        txtLeaveId.setEditable(false);
        txtLeaveId.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeave.add(txtLeaveId, gridBagConstraints);

        chkLtc.setNextFocusableComponent(txtMaxEncashment);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeave.add(chkLtc, gridBagConstraints);

        lblLtcLfc.setText("With Ltc/Lfc");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeave.add(lblLtcLfc, gridBagConstraints);

        panPaymentType.setMinimumSize(new java.awt.Dimension(180, 27));
        panPaymentType.setLayout(new java.awt.GridBagLayout());

        rdoFull.setText("Full Pay");
        rdoFull.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        rdoFull.setNextFocusableComponent(rdoHalf);
        panPaymentType.add(rdoFull, new java.awt.GridBagConstraints());

        rdoHalf.setText("Half Pay");
        rdoHalf.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        rdoHalf.setNextFocusableComponent(txtTypeOfLeave);
        panPaymentType.add(rdoHalf, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 4);
        panLeave.add(panPaymentType, gridBagConstraints);

        panFixedPar1.setLayout(new java.awt.GridBagLayout());

        txtFixedPar1.setMinimumSize(new java.awt.Dimension(25, 21));
        txtFixedPar1.setNextFocusableComponent(txtFixedPar);
        txtFixedPar1.setPreferredSize(new java.awt.Dimension(25, 21));
        txtFixedPar1.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 2);
        panFixedPar1.add(txtFixedPar1, gridBagConstraints);

        lblFixedLeave.setText("Leave For");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFixedPar1.add(lblFixedLeave, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 4);
        panLeave.add(panFixedPar1, gridBagConstraints);

        panProRatePar1.setLayout(new java.awt.GridBagLayout());

        lblProRatelbl1.setText("For Every");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProRatePar1.add(lblProRatelbl1, gridBagConstraints);

        txtPro1.setMinimumSize(new java.awt.Dimension(25, 21));
        txtPro1.setNextFocusableComponent(txtPro2);
        txtPro1.setPreferredSize(new java.awt.Dimension(25, 21));
        txtPro1.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 2);
        panProRatePar1.add(txtPro1, gridBagConstraints);

        lblProRatelbl2.setText("Days Worked");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProRatePar1.add(lblProRatelbl2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 4);
        panLeave.add(panProRatePar1, gridBagConstraints);

        lblPayType.setText("Leave On");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeave.add(lblPayType, gridBagConstraints);

        lblCrDays.setText("days");
        lblCrDays.setMaximumSize(new java.awt.Dimension(35, 18));
        lblCrDays.setMinimumSize(new java.awt.Dimension(35, 18));
        lblCrDays.setPreferredSize(new java.awt.Dimension(35, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeave.add(lblCrDays, gridBagConstraints);

        lblCrDays1.setText("days");
        lblCrDays1.setMaximumSize(new java.awt.Dimension(35, 18));
        lblCrDays1.setMinimumSize(new java.awt.Dimension(35, 18));
        lblCrDays1.setPreferredSize(new java.awt.Dimension(35, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeave.add(lblCrDays1, gridBagConstraints);

        chkMaternityLeave.setNextFocusableComponent(txtMaxEncashment);
        chkMaternityLeave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkMaternityLeaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeave.add(chkMaternityLeave, gridBagConstraints);

        lblMaternityLeave.setText("Maternity Leave");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeave.add(lblMaternityLeave, gridBagConstraints);

        txtMaternityCountLimit.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMaternityCountLimit.setNextFocusableComponent(cboParForLeave);
        txtMaternityCountLimit.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeave.add(txtMaternityCountLimit, gridBagConstraints);

        lblMaternityCountLimit.setText("ML Limit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLeave.add(lblMaternityCountLimit, gridBagConstraints);

        tabShareProduct.addTab("Parameters", panLeave);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panLeaveManagement.add(tabShareProduct, gridBagConstraints);

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

    private void txtTypeOfLeaveFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTypeOfLeaveFocusLost
        // TODO add your handling code here:
        txtTypeOfLeave.setText(CommonUtil.convertObjToStr(txtTypeOfLeave.getText()).toUpperCase());
        if(txtTypeOfLeave.getText().length() > 0){
            HashMap chkTypeOfLeave = new HashMap();
            chkTypeOfLeave.put("LEAVE_TYPE",CommonUtil.convertObjToStr(txtTypeOfLeave.getText()));
            List chkTypeOfLeaveLst = ClientUtil.executeQuery("getChkLeaveType",chkTypeOfLeave); 
            if(chkTypeOfLeaveLst!= null && chkTypeOfLeaveLst.size() > 0){
                ClientUtil.showAlertWindow("Leave Type already Exists!!");
                txtTypeOfLeave.setText("");
            }
        }
    }//GEN-LAST:event_txtTypeOfLeaveFocusLost

    private void chkMaternityLeaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkMaternityLeaveActionPerformed
        // TODO add your handling code here:
            
        if(chkMaternityLeave.isSelected()){ 
            txtMaternityCountLimit.setText("");
            txtMaternityCountLimit.setEnabled(true);
            txtMaternityCountLimit.setVisible(true);
            lblMaternityCountLimit.setVisible(true);
            rdoIntroReq_No1.setSelected(true);
            rdoIntroReq_Yes1.setSelected(false);
            cboParForLeave.setSelectedItem("OTHERS");
            txtFixedPar1.setEnabled(false);
            txtFixedPar.setEnabled(false);
            cboParFixed.setEnabled(false);
            txtPro1.setEnabled(false);
            txtPro2.setEnabled(false);
            rdoAcc_Yes.setSelected(true);
            rdoAcc_No.setSelected(false);
            txtMaxLeaves.setEnabled(true);
            rdoEncash_No1.setSelected(false);
            //            lblMaternityCountLimit.setVisible(true);
        }else{
            txtMaternityCountLimit.setText("");
            txtMaternityCountLimit.setEnabled(false);
            txtMaternityCountLimit.setVisible(false);
            lblMaternityCountLimit.setVisible(false);
            rdoIntroReq_No1.setSelected(false);
            rdoIntroReq_Yes1.setSelected(false);
            cboParForLeave.setSelectedItem("");
            txtFixedPar1.setEnabled(true);
            txtFixedPar.setEnabled(true);
            cboParFixed.setEnabled(true);
            txtPro1.setEnabled(true);
            txtPro2.setEnabled(true);
            rdoAcc_Yes.setSelected(true);
            rdoAcc_No.setSelected(false);
            txtMaxLeaves.setEnabled(true);
            //            lblMaternityCountLimit.setVisible(false);
            
        }
    }//GEN-LAST:event_chkMaternityLeaveActionPerformed

    private void txtPro2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPro2FocusLost
        if(txtPro1.getText().length() > 0 && txtPro2.getText().length() >0){
            if(CommonUtil.convertObjToInt(txtPro2.getText()) > CommonUtil.convertObjToInt(txtPro1.getText())){
                ClientUtil.showAlertWindow("Invalid Entry for Prorate Leave crediting!!!");
                txtPro1.setText("");
                txtPro2.setText("");
            }
        }
    }//GEN-LAST:event_txtPro2FocusLost

    private void cboParFixedFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboParFixedFocusLost
        // TODO add your handling code here:
        if(CommonUtil.convertObjToStr(cboParFixed.getSelectedItem()).length() >0){
            if(txtFixedPar.getText().length() > 0 && txtFixedPar1.getText().length() >0){
                String parameterFixed = CommonUtil.convertObjToStr(cboParFixed.getSelectedItem());
                System.out.println("@#$@#$@#$@#$parameterFixed:"+parameterFixed);
                int days = 0;
                int paramDays = 0;
                if(parameterFixed.equals("month/s")){
                    days = CommonUtil.convertObjToInt(txtFixedPar1.getText());
                    paramDays = CommonUtil.convertObjToInt(txtFixedPar.getText()) * 30;
                }else if(parameterFixed.equals("Year/s")){
                    days = CommonUtil.convertObjToInt(txtFixedPar1.getText());
                    paramDays = CommonUtil.convertObjToInt(txtFixedPar.getText()) * 365;
                }else{
                    days = CommonUtil.convertObjToInt(txtFixedPar1.getText());
                    paramDays = CommonUtil.convertObjToInt(txtFixedPar.getText());
                }
                System.out.println("!$@#$@#$@#$@#$days :"+days +" paramDays: " +paramDays);
                if(days > paramDays){
                    ClientUtil.showAlertWindow("Invalid Parameter for Leave Crediting!!");
                    txtFixedPar.setText("");
                    txtFixedPar1.setText("");
                }
            }
        }
    }//GEN-LAST:event_cboParFixedFocusLost

    private void txtAccTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAccTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAccTextActionPerformed

    private void rdoEncash_No1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoEncash_No1ActionPerformed
        // TODO add your handling code here:
        chkLtc.setSelected(false);
         chkLtc.setEnabled(false);
         txtMaxEncashment.setText("");
         txtMaxEncashment.setEnabled(false);
    }//GEN-LAST:event_rdoEncash_No1ActionPerformed

    private void rdoEncash_Yes1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoEncash_Yes1ActionPerformed
        // TODO add your handling code here:
         chkLtc.setEnabled(true);
         txtMaxEncashment.setEnabled(true);
    }//GEN-LAST:event_rdoEncash_Yes1ActionPerformed

    private void cboParForLeaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboParForLeaveActionPerformed
        // TODO add your handling code here:
       String par= ((ComboBoxModel)cboParForLeave.getModel()).getKeyForSelected().toString();
       if(par.equalsIgnoreCase("FIXED")){
           txtFixedPar1.setEnabled(true);
           txtFixedPar.setEnabled(true);
           cboParFixed.setEnabled(true);
           
           txtPro1.setText("");
           txtPro2.setText("");
           txtPro1.setEnabled(false);
           txtPro2.setEnabled(false);
       }
       else if(par.equalsIgnoreCase("PRORATE")){
           txtFixedPar1.setText("");
           txtFixedPar.setText("");
           cboParFixed.setSelectedItem("");
           txtFixedPar1.setEnabled(false);
           txtFixedPar.setEnabled(false);
           cboParFixed.setEnabled(false);
           txtPro1.setEnabled(true);
           txtPro2.setEnabled(true);
       }else{
           txtFixedPar1.setText("");
           txtFixedPar.setText("");
           cboParFixed.setSelectedItem("");
           txtFixedPar1.setEnabled(false);
           txtFixedPar.setEnabled(false);
           cboParFixed.setEnabled(false);
           txtPro1.setEnabled(false);
           txtPro2.setEnabled(false);
       }
    }//GEN-LAST:event_cboParForLeaveActionPerformed

    private void rdoAcc_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoAcc_NoActionPerformed
        // TODO add your handling code here:
        txtAccText.setText("");
        cboAcc.setSelectedItem("");
        txtAccText.setEnabled(false);
        cboAcc.setEnabled(false);
    }//GEN-LAST:event_rdoAcc_NoActionPerformed

    private void rdoAcc_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoAcc_YesActionPerformed
        // TODO add your handling code here:
        txtAccText.setEnabled(true);
        cboAcc.setEnabled(true);
    }//GEN-LAST:event_rdoAcc_YesActionPerformed

    private void rdoIntroReq_No1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoIntroReq_No1ActionPerformed
        // TODO add your handling code here:
        txtCarryOverYears.setEnabled(true);
        ClientUtil.clearAll(panAccAllowed);
        ClientUtil.enableDisable(panAccAllowed,true);
        ClientUtil.clearAll(panAccAllowedPeriod);
        ClientUtil.enableDisable(panAccAllowedPeriod,true);
        txtMaxLeaves.setEnabled(true);
    }//GEN-LAST:event_rdoIntroReq_No1ActionPerformed

    private void rdoIntroReq_Yes1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoIntroReq_Yes1ActionPerformed
        // TODO add your handling code here:
        txtCarryOverYears.setText("");
        txtCarryOverYears.setEnabled(false);
        ClientUtil.clearAll(panAccAllowed);
        ClientUtil.enableDisable(panAccAllowed,false);
        ClientUtil.clearAll(panAccAllowedPeriod);
        ClientUtil.enableDisable(panAccAllowedPeriod,false);
        txtMaxLeaves.setEnabled(false);
    }//GEN-LAST:event_rdoIntroReq_Yes1ActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        callView(ClientConstants.ACTION_STATUS[17]);
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed
                                                        
    private void txtAccTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccTextFocusLost
        // TODO add your handling code here:
        
    }//GEN-LAST:event_txtAccTextFocusLost
                                
    private void cboCreditTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboCreditTypeItemStateChanged
        // TODO add your handling code here:
        
        
    }//GEN-LAST:event_cboCreditTypeItemStateChanged
    
    private void cboCreditTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCreditTypeActionPerformed
        // TODO add your handling code here:
      
    }//GEN-LAST:event_cboCreditTypeActionPerformed
    
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
        // TODO add your handling code here:]
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
//        super.removeEditLock(txtLeaveId.getText());
        observable.resetForm();
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panLeaveManagement, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        setButtonEnableDisable();
        viewType = "";
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        callView("Delete");
         btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        txtLeaveId.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        callView("Edit");
         btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        txtLeaveId.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        setModified(false);
        savePerformed();
         btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        setModified(true);
        observable.resetForm();
        ClientUtil.enableDisable(panLeaveManagement, true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        txtLeaveId.setEnabled(false);
        disableFields();
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
    public void disableFields(){
        txtCarryOverYears.setText("");
        txtCarryOverYears.setEnabled(false);
        txtAccText.setText("");
        cboAcc.setSelectedItem("");
        txtAccText.setEnabled(false);
        cboAcc.setEnabled(false);
        chkLtc.setEnabled(false);
        txtMaxEncashment.setEnabled(false);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
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
    private com.see.truetransact.uicomponent.CComboBox cboAcc;
    private com.see.truetransact.uicomponent.CComboBox cboCreditType;
    private com.see.truetransact.uicomponent.CComboBox cboParFixed;
    private com.see.truetransact.uicomponent.CComboBox cboParForLeave;
    private com.see.truetransact.uicomponent.CCheckBox chkLtc;
    private com.see.truetransact.uicomponent.CCheckBox chkMaternityLeave;
    private com.see.truetransact.uicomponent.CLabel lblAccAllowed;
    private com.see.truetransact.uicomponent.CLabel lblCarryOver;
    private com.see.truetransact.uicomponent.CLabel lblCrDays;
    private com.see.truetransact.uicomponent.CLabel lblCrDays1;
    private com.see.truetransact.uicomponent.CLabel lblCreditType;
    private com.see.truetransact.uicomponent.CLabel lblDesc;
    private com.see.truetransact.uicomponent.CLabel lblEncash;
    private com.see.truetransact.uicomponent.CLabel lblFixedLeave;
    private com.see.truetransact.uicomponent.CLabel lblLeaveLapses;
    private com.see.truetransact.uicomponent.CLabel lblLtcLfc;
    private com.see.truetransact.uicomponent.CLabel lblMaternityCountLimit;
    private com.see.truetransact.uicomponent.CLabel lblMaternityLeave;
    private com.see.truetransact.uicomponent.CLabel lblMaxEncash;
    private com.see.truetransact.uicomponent.CLabel lblMaxLeaves;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblParForLeave;
    private com.see.truetransact.uicomponent.CLabel lblPayType;
    private com.see.truetransact.uicomponent.CLabel lblProRatelbl1;
    private com.see.truetransact.uicomponent.CLabel lblProRatelbl2;
    private com.see.truetransact.uicomponent.CLabel lblProRatelbl3;
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
    private com.see.truetransact.uicomponent.CLabel lblTypeOfLeave;
    private com.see.truetransact.uicomponent.CMenuBar mbrShareProduct;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAccAllowed;
    private com.see.truetransact.uicomponent.CPanel panAccAllowedPeriod;
    private com.see.truetransact.uicomponent.CPanel panEncash;
    private com.see.truetransact.uicomponent.CPanel panFixedPar;
    private com.see.truetransact.uicomponent.CPanel panFixedPar1;
    private com.see.truetransact.uicomponent.CPanel panLeave;
    private com.see.truetransact.uicomponent.CPanel panLeaveLapses;
    private com.see.truetransact.uicomponent.CPanel panLeaveManagement;
    private com.see.truetransact.uicomponent.CPanel panPaymentType;
    private com.see.truetransact.uicomponent.CPanel panProRatePar;
    private com.see.truetransact.uicomponent.CPanel panProRatePar1;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CButtonGroup rdoAcc;
    private com.see.truetransact.uicomponent.CRadioButton rdoAcc_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoAcc_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoEncash;
    private com.see.truetransact.uicomponent.CRadioButton rdoEncash_No1;
    private com.see.truetransact.uicomponent.CRadioButton rdoEncash_Yes1;
    private com.see.truetransact.uicomponent.CRadioButton rdoFull;
    private com.see.truetransact.uicomponent.CRadioButton rdoHalf;
    private com.see.truetransact.uicomponent.CButtonGroup rdoIntroReq;
    private com.see.truetransact.uicomponent.CRadioButton rdoIntroReq_No1;
    private com.see.truetransact.uicomponent.CRadioButton rdoIntroReq_Yes1;
    private com.see.truetransact.uicomponent.CButtonGroup rdoLtcLfc;
    private com.see.truetransact.uicomponent.CButtonGroup rdoPaymentType;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CTabbedPane tabShareProduct;
    private javax.swing.JToolBar tbrLeave;
    private com.see.truetransact.uicomponent.CTextField txtAccText;
    private com.see.truetransact.uicomponent.CTextField txtCarryOverYears;
    private com.see.truetransact.uicomponent.CTextField txtDesc;
    private com.see.truetransact.uicomponent.CTextField txtFixedPar;
    private com.see.truetransact.uicomponent.CTextField txtFixedPar1;
    private com.see.truetransact.uicomponent.CTextField txtLeaveId;
    private com.see.truetransact.uicomponent.CTextField txtMaternityCountLimit;
    private com.see.truetransact.uicomponent.CTextField txtMaxEncashment;
    private com.see.truetransact.uicomponent.CTextField txtMaxLeaves;
    private com.see.truetransact.uicomponent.CTextField txtPro1;
    private com.see.truetransact.uicomponent.CTextField txtPro2;
    private com.see.truetransact.uicomponent.CTextField txtTypeOfLeave;
    // End of variables declaration//GEN-END:variables
    
}
