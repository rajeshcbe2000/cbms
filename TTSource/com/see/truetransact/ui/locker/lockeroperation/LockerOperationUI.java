/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * LockerOperationUI.java
 *
 * Created on January 20, 2005, 3:03 PM
 */

package com.see.truetransact.ui.locker.lockeroperation;

/**
 *
 * @author  ashokvijayakumar
 * @modified : Sunil
 *      Added Edit Locking - 07-07-2005
 */
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.TrueTransactMain;

import java.util.Observable;
import java.util.Observer;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.List;
import com.see.truetransact.clientproxy.ProxyParameters;

import com.see.truetransact.commonutil.StringEncrypter;
import java.util.Date;
public class LockerOperationUI extends CInternalFrame implements Observer, UIMandatoryField{
    
    /** Vairable Declarations */
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("com.see.truetransact.ui.transaction.token.tokenconfig.TokenConfigRB", ProxyParameters.LANGUAGE);
    //Creating Instance For ResourceBundle-TokenConfigRB
    private LockerOperationOB observable; //Reference for the Observable Class TokenConfigOB
    private final static ClientParseException parseException = ClientParseException.getInstance();//Instance of ClientParseException to log the Exceptions
    private HashMap mandatoryMap;//Map for putting up MandatoryFields in the UI
    private String viewType = "";//Variable used to store ActionType(New,Edit,Delete)
    private final String AUTHORIZE="Authorize";//Variable used when btnAuthorize is clicked
     private int selectedRow = -1;
     private int selectedData = 0;
     private int result;
     private String prodID = "";
     private String issueId = "";
     private boolean pwdSave = false;
     
     private StringEncrypter encrypt = null;
     private Date currDt = null;
    /** Creates new form TokenConfigUI */
    public LockerOperationUI() {
        initForm();
    }
    
    /** Method which is used to initialize the form TokenConfig */
    private void initForm(){
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        dtdLastOperatedOn.setVisible(false);
        lblLastOperatedOn.setVisible(false);
        setFieldNames();
        setObservable();
        observable.resetForm();
//        txtNoOfTokens.setText("");
        initComponentData();
        setMandatoryHashMap();
        setMaxLengths();
//        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panTokenConfiguration);
//        ClientUtil.enableDisable(panTokenConfiguration, false);
        setButtonEnableDisable();
        btnLockerNo.setEnabled(false);
        panLockerOut.setVisible(false);
        ClientUtil.enableDisable(panStdInstructions, false);
        ClientUtil.enableDisable(panInstEntry, false, false, true);
        lblDate.setVisible(false);
        lblDateVal.setVisible(false);
       
        try {
            encrypt = new StringEncrypter();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
   /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        tblInstruction2.setName("tblInstruction2");
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
//        cboTokenType.setName("cboTokenType");
        lbSpace2.setName("lbSpace2");
//        lblEndingTokenNo.setName("lblEndingTokenNo");
        lblMsg.setName("lblMsg");
//        lblNoOfTokens.setName("lblNoOfTokens");
//        lblSeriesNo.setName("lblSeriesNo");
        lblSpace1.setName("lblSpace1");
        lblSpace3.setName("lblSpace3");
        lblSpace5.setName("lblSpace5");
//        lblStartingTokenNo.setName("lblStartingTokenNo");
        lblStatus.setName("lblStatus");
//        lblTokenType.setName("lblTokenCofigDate");
        mbrTokenConfig.setName("mbrTokenConfig");
        panStatus.setName("panStatus");
//        panTokenConfiguration.setName("panTokenConfiguration");
//        txtEndingTokenNo.setName("txtEndingTokenNo");
//        txtNoOfTokens.setName("txtNoOfTokens");
//        txtSeriesNo.setName("txtSeriesNo");
//        txtStartingTokenNo.setName("txtStartingTokenNo");
//        lblTokenConfigId.setName("lblTokenConfigId");
//        txtTokenConfigId.setName("txtTokenConfigId");
        panLockerOut.setName("panLockerOut");
        lblLockerOutDt.setName("lblLockerOutDt");
        lblLockerOutDtVal.setName("lblLockerOutDtVal");
        lblLockerNo.setName("lblLockerNo");
        lblCustName.setName("lblCustName");
        lblCustNameVal.setName("lblCustNameVal");
        lblCustomerId.setName("lblCustomerId");
        lblCustomerIdVal.setName("lblCustomerIdVal");
        lblDate.setName("lblDate");
        lblDateVal.setName("lblDateVal");
        lblModeOfOp.setName("lblModeOfOp");
        lblModeOfOpVal.setName("lblModeOfOpVal");
        lblCustId.setName("lblCustId");
        lblName.setName("lblName");
        lblPassword.setName("lblPassword");
        btnLockerOut.setName("btnLockerOut");
        btnLockerNo.setName("btnLockerNo");
        btnPasNew.setName("btnPasNew");
        btnPastSave.setName("btnPastSave");
        btnPasDelete.setName("btnPasDelete");
        
        txtCustId.setName("txtCustId");
        txtName.setName("txtName");
        txtPassword.setName("txtPassword");
        
        panStdInstructions.setName("panStdInstructions");
        panOperations.setName("panOperations");
    }
    
 /* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        lblSpace5.setText(resourceBundle.getString("lblSpace5"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnClose.setText(resourceBundle.getString("btnClose"));
//        lblNoOfTokens.setText(resourceBundle.getString("lblNoOfTokens"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
//        lblSeriesNo.setText(resourceBundle.getString("lblSeriesNo"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        btnNew.setText(resourceBundle.getString("btnNew"));
//        lblEndingTokenNo.setText(resourceBundle.getString("lblEndingTokenNo"));
//        lblStartingTokenNo.setText(resourceBundle.getString("lblStartingTokenNo"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
//        lblTokenType.setText(resourceBundle.getString("lblTokenType"));
        lbSpace2.setText(resourceBundle.getString("lbSpace2"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
//        lblTokenConfigId.setText(resourceBundle.getString("lblTokenConfigId"));
    }
    
    /** Adds up the Observable to this form */
    private void setObservable() {
        try{
            observable = LockerOperationOB.getInstance();
            observable.addObserver(this);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /*Setting model to the combobox cboTokenType  */
    private void initComponentData() {
        try{
//            cboTokenType.setModel(observable.getCbmTokenType());
            tblInstruction2.setModel(observable.getTbmInstructions2());
        }catch(ClassCastException e){
            parseException.logException(e,true);
        }
    }
    
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
//        txtTokenConfigId.setText(observable.getTxtTokenConfigId());
//        cboTokenType.setSelectedItem(observable.getCboTokenType());
//        txtSeriesNo.setText(observable.getTxtSeriesNo());
//        txtStartingTokenNo.setText(observable.getTxtStartingTokenNo());
//        txtEndingTokenNo.setText(observable.getTxtEndingTokenNo());
//        txtNoOfTokens.setText(observable.getTxtNoOfTokens());
        lblDateVal.setText(observable.getLblDateVal());
        lblLockerOutDtVal.setText(observable.getLblLockerOutDtVal());
        lblCustNameVal.setText(observable.getLblCustNameVal());
        lblCustomerIdVal.setText(observable.getLblCustomerIdVal());
        lblModeOfOpVal.setText(observable.getLblModeOfOpVal());
        txtLockerNo.setText(observable.getTxtLockerNo());
        txtCustId.setText(observable.getTxtCustId());
        txtName.setText(observable.getTxtName());
        txtPassword.setText(observable.getTxtPassword());
        lblStatus.setText(observable.getLblStatus());
        
//       tblInstruction2.setModel(observable.getTbmInstructions2());
    }
    
    private void setTblVal(){
        txtCustId.setText(observable.getTxtCustId());
        txtName.setText(observable.getTxtName());
        txtPassword.setText(observable.getTxtPassword());
    }
     private void clearTblVal(){
        txtCustId.setText("");
        txtName.setText("");
        txtPassword.setText("");
        observable.setTxtCustId("");
        observable.setTxtName("");
        observable.setTxtPassword("");
    }
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
//        observable.setTxtTokenConfigId(txtTokenConfigId.getText());
//        observable.setCboTokenType((String) cboTokenType.getSelectedItem());
//        observable.setTxtSeriesNo(txtSeriesNo.getText());
//        observable.setTxtStartingTokenNo(txtStartingTokenNo.getText());
//        observable.setTxtEndingTokenNo(txtEndingTokenNo.getText());
//        observable.setTxtNoOfTokens(txtNoOfTokens.getText());
        observable.setLblDateVal(lblDateVal.getText());
        observable.setLblLockerOutDtVal(lblLockerOutDtVal.getText());
        observable.setLblCustNameVal(lblCustNameVal.getText());
        observable.setLblCustomerIdVal(lblCustomerIdVal.getText());
        observable.setLblModeOfOpVal(lblModeOfOpVal.getText());
        observable.setTxtCustId(txtCustId.getText());
        observable.setTxtLockerNo(txtLockerNo.getText());
        observable.setTxtName(txtName.getText());
        observable.setTxtPassword(txtPassword.getText());
        observable.setModule(getModule());
        observable.setScreen(getScreen());
        observable.setLblConstitutionVal(lblConstitutionVal.getText());
    }
    
    
/* Auto Generated Method - setMandatoryHashMap()
 
ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
 
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboTokenType", new Boolean(true));
        mandatoryMap.put("txtSeriesNo", new Boolean(false));
        mandatoryMap.put("txtStartingTokenNo", new Boolean(true));
        mandatoryMap.put("txtEndingTokenNo", new Boolean(true));
        mandatoryMap.put("txtNoOfTokens", new Boolean(true));
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
//        cboTokenType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboTokenType"));
//        txtSeriesNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSeriesNo"));
//        txtStartingTokenNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtStartingTokenNo"));
//        txtEndingTokenNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtEndingTokenNo"));
//        txtNoOfTokens.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNoOfTokens"));
//        txtTokenConfigId.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTokenConfigId"));
    }
    
    /** Used to set Maximum possible lenghts for TextFields */
    private void setMaxLengths(){
//        txtSeriesNo.setMaxLength(16);
//        txtSeriesNo.setAllowAll(true);
////        setValidation(new com.see.truetransact.uivalidation.DefaultValidation());
//        txtStartingTokenNo.setMaxLength(8);
//        txtEndingTokenNo.setMaxLength(8);
    }
    
    /*Makes the button Enable or Disable accordingly when usier clicks new,edit or delete buttons */
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
    
    /** Method used to check whether the Mandatory Fields in the Form are Filled or not */
    private String checkMandatory(javax.swing.JComponent component){
        return new MandatoryCheck().checkMandatory(getClass().getName(), component);
    }
    
    /** Method used to Give a Alert when any Mandatory Field is not filled by the user */
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    /* Calls the execute method of TerminalOB to do insertion or updation or deletion */
    private void saveAction(String status){
        //To check mandtoryness of the Terminal panAcHdDetails,panAcHeadDetails panel and diplay appropriate
        //error message, else proceed
//        final String mandatoryMessage = checkMandatory(panTokenConfiguration);
//        StringBuffer message = new StringBuffer(mandatoryMessage);
//        if(observable.getCbmTokenType().getKeyForSelected().equals("METAL")){
//            if(txtSeriesNo.getText().equals("")){
//                message.append(objMandatoryRB.getString("txtSeriesNo"));
//            }
//        }
//         if((CommonUtil.convertObjToInt(txtStartingTokenNo.getText()))> (CommonUtil.convertObjToInt(txtEndingTokenNo.getText())))
//         {
//          message.append(objMandatoryRB.getString("txtNumber"));   
//         }
//        if(message.length() > 0 ){
//            displayAlert(message.toString());
//        }else{
//        if(observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE){
           // if (pwdSave) {
                observable.execute(status);
           // } else {
               // displayAlert("Password(s) not entered...");
               /// return;
           // }
//        }
            if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED){
                settings();
                btnLockerNo.setEnabled(false);
                 ClientUtil.enableDisable(panStdInstructions, false);
                 ClientUtil.enableDisable(panInstEntry, false, false, true);
                lblLockerTypeVal.setText("");
                lblIssueDateVal.setText("");
                lblExpiryDateVal.setText("");
                lblConstitutionVal.setText("");
            }
        }
            
//    }
    
    /* set the screen after the updation,insertion, deletion */
    private void settings(){
        observable.resetForm();
        ClientUtil.clearAll(this);
        setButtonEnableDisable();
        ClientUtil.enableDisable(panAccountInfo, false, false, true);
        observable.resetInstTbl();
        observable.setResultStatus();
        pwdSave = false;
    }
    
    /* Does necessary operaion when user clicks the save button */
    private void savePerformed(){
        updateOBFields();
        String action;
        String Mode=lblModeOfOpVal.getText();
        String Constitution=lblConstitutionVal.getText();
        String prodid=lblLockerTypeVal.getText();
        String custid=lblCustomerIdVal.getText();
        String locno=txtLockerNo.getText();
        
        HashMap hash=new HashMap();
        
        hash.put("LOCKER_NUM",locno);
        hash.put("PROD_ID",prodid);
        
        List stList =ClientUtil.executeQuery("PasswordDetails",hash);
        
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW){
            
            if(stList!=null && stList.size()>0){
                HashMap stMap = (HashMap) stList.get(0);
                String pwd;
                pwd= CommonUtil.convertObjToStr(stMap.get("PASSWORD_REQUIRED"));
                System.out.println("the password required"  +pwd);
                
                String n="Y";
                if(pwd.equals(n)){
                    
                    List list=ClientUtil.executeQuery("Passwordrequired" , hash);
                    HashMap h=new HashMap();
                    h=(HashMap)list.get(0);
                    String password=  CommonUtil.convertObjToStr( h.get("PWD"));
                    String passwd ="";
                    if(password.equals(passwd)){
                        displayAlert("Please register your password then operate the locker");
                        btnCancelActionPerformed(null);
                    }
                    else  if(observable.checkPasswordRecords()==true)
                        
                    {
                        action=CommonConstants.TOSTATUS_INSERT;
                        saveAction(action);
                    }
                    
                    else  if(Mode.equals("Jointly") && Constitution.equals( "JOINT")) {
                        displayAlert("Please enter  the passwords.");
                        //dispalyAlert()
                        return;
                    }
                    
                    else
                        displayAlert(" Please enter the password .");
                    return;
                }
                
                else {
                    action=CommonConstants.TOSTATUS_INSERT;
                    saveAction(action);
                }
                
            }
        }
        
        
        
        else if(observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT){
            if (lblLockerOutDtVal.getText().length()>0) {
                action=CommonConstants.TOSTATUS_UPDATE;
                saveAction(action);
            } else {
                ClientUtil.showAlertWindow("Click Locker Out Button");
            }
        }else if(observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE){
            action=CommonConstants.TOSTATUS_DELETE;
            saveAction(action);
        }
    }
    
    /* Method used to showPopup ViewAll by Executing a Query */
    private void callView(String currField) {
        viewType = currField;
        HashMap viewMap = new HashMap();
        //        ArrayList lst = new ArrayList();
        //        lst.add("CONFIG_ID");
        //        viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
        //        lst = null;
        if(currField.equals("LockerNo")){
            HashMap where = new HashMap();
            where.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
            //                 String PRODUCT_ID = CommonUtil.convertObjToStr(((ComboBoxModel) cboProductIdAI.getModel()).getKeyForSelected());
            //                 where.put("PRODUCT_ID", PRODUCT_ID);
            viewMap.put(CommonConstants.MAP_NAME, "getLockOptList");
            viewMap.put(CommonConstants.MAP_WHERE, where);
            where = null;
            //                 new ViewAll(this, viewMap).show();
        }else{
            viewMap.put(CommonConstants.MAP_NAME, "getSelectLockerOptEdit");
            HashMap where = new HashMap();
            where.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, where);
            where = null;
        }
        new ViewAll(this,viewMap).show();
    }
    
    /* Fills up the HashMap with data when user selects the row in ViewAll screen  */
    public void fillData(Object  map) {
        setModified(true);
        HashMap hash = (HashMap) map;
        if (viewType != null) {
            if (viewType.equals(ClientConstants.ACTION_STATUS[2]) ||
            viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) || viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                HashMap where = new HashMap();
                where.put("OPERATION_ID", hash.get("OPERATION_ID"));
                observable.setOptID(CommonUtil.convertObjToStr(hash.get("OPERATION_ID")));
                where.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                hash.put(CommonConstants.MAP_WHERE, where);
                observable.populateData(hash);
                setPasswordTab(hash);
                panLockerOut.setEnabled(true);
                btnLockerOut.setEnabled(true);
                //                fillTxtNoOfTokens();
                if (observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                    //                    ClientUtil.enableDisable(panTokenConfiguration, true);
                    panLockerOut.setVisible(true);
                }
                if(viewType.equals(AUTHORIZE)){
                    //                    ClientUtil.enableDisable(panTokenConfiguration, false);
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                }
                setButtonEnableDisable();
                //                setProdID(CommonUtil.convertObjToStr(hash.get("PROD_ID")));
                com.see.truetransact.transferobject.locker.lockerissue.LockerIssueTO objLockerIssueTO =
                (com.see.truetransact.transferobject.locker.lockerissue.LockerIssueTO) hash.get("LockerTO");
                setProdID(objLockerIssueTO.getProdId());
                setIssueId(objLockerIssueTO.getRemarks());
                lblLockerTypeVal.setText(getProdID());
                lblIssueDateVal.setText(DateUtil.getStringDate(objLockerIssueTO.getCreateDt()));
                lblExpiryDateVal.setText(DateUtil.getStringDate(objLockerIssueTO.getExpDt()));
                lblConstitutionVal.setText(objLockerIssueTO.getLocCatId());
                
            }else if(viewType.equals("LockerNo")){
                //             txtLockerNo.setText(CommonUtil.convertObjToStr(hash.get("FROM_LOC_NO")));
                //             lblLockerKeyNoVal.setText(CommonUtil.convertObjToStr(hash.get("LOCKER_KEY_NO")));
                HashMap HMAP=new HashMap();
                HMAP.put("LOCKER_NO",CommonUtil.convertObjToStr(hash.get("LOCKER_NUM")));
                HMAP.put("ISSUE_ID",CommonUtil.convertObjToStr(hash.get("ISSUE_ID")));
                List freezeList=ClientUtil.executeQuery("getFrezeLockerDetails", HMAP);
                if(freezeList!=null && freezeList.size()>0){
                    ClientUtil.displayAlert("Locker Frozen Operation not allowed");
                    btnCancelActionPerformed(null);
                    return;
                }
               hash.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
                List lst=ClientUtil.executeQuery("getLastOperatedDt", hash);
                if(lst!=null && lst.size()>0){
                    HashMap hashmap=(HashMap)lst.get(0);
                   String date= CommonUtil.convertObjToStr(hashmap.get("OPERATION_DT"));
                    dtdLastOperatedOn.setText("<HTML><b><font color=Blue>"+
                    date+
                    "</font></b></html>");                 
                   dtdLastOperatedOn.setVisible(true);
                   lblLastOperatedOn.setVisible(true);
                }
                txtLockerNo.setText(CommonUtil.convertObjToStr(hash.get("LOCKER_NUM")));
                lblCustNameVal.setText(CommonUtil.convertObjToStr(hash.get("NAME")));
                lblCustomerIdVal.setText(CommonUtil.convertObjToStr(hash.get("CUST_ID")));
                System.out.println("@@@@@ hash:"+hash);
                lblDateVal.setText(ClientUtil.getCurrentDateWithTime().toString());
                lblModeOfOpVal.setText(CommonUtil.convertObjToStr(hash.get("MODE_OF_OPERATION")));
                setPasswordTab(hash);
                btnLockerNo.setEnabled(false);
                setProdID(CommonUtil.convertObjToStr(hash.get("PROD_ID")));
                setIssueId(CommonUtil.convertObjToStr(hash.get("ISSUE_ID")));
                lblLockerTypeVal.setText(getProdID());
                lblIssueDateVal.setText(CommonUtil.convertObjToStr(hash.get("ISSUE_DT")));
                lblExpiryDateVal.setText(CommonUtil.convertObjToStr(hash.get("EXP_DT")));
                lblConstitutionVal.setText(CommonUtil.convertObjToStr(hash.get("LOCKER_CAT_ID")));
                txtCustId.setText(CommonUtil.convertObjToStr(hash.get("CUST_ID")));
                txtName.setText(CommonUtil.convertObjToStr(hash.get("NAME")));
            }
        }
    }
    
    private void setPasswordTab(HashMap hash){
        HashMap mapData = new HashMap();
        mapData.put("LOCKER_NUM", hash.get("LOCKER_NUM"));
        mapData.put("ISSUE_ID", hash.get("ISSUE_ID"));
        mapData.put("PWD",hash.get("PASSWORD"));
        mapData.put("PWD_REQUIRED",hash.get("PASSWORD_REQUIRED"));
        mapData.put("BRANCH_CODE",hash.get("BRANCH_CODE"));
        //                    setIssueId(CommonUtil.convertObjToStr(hash.get("ISSUE_ID")));
        observable.setIssueId(CommonUtil.convertObjToStr(hash.get("ISSUE_ID")));
        mapData.put("CUST_ID", hash.get("CUST_ID"));
        observable.populateTblActData(mapData);
        //                    tblInstruction2.setModel(observable.getTbmInstructions2());
        mapData = null;
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
    
    
    /** Method used to do Required operation when user clicks btnAuthorize,btnReject or btnReject **/
    public void authorizeStatus(String authorizeStatus) {
        if (!viewType.equals(AUTHORIZE)){
            viewType = AUTHORIZE;
            setModified(true);
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            mapParam.put(CommonConstants.MAP_NAME, "getTokenConfigAuthorizeList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeTokenConfig");
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            observable.setStatus();
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
        } else if (viewType.equals(AUTHORIZE)){
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDt.clone());
            //            singleAuthorizeMap.put("CONFIG_ID", txtTokenConfigId.getText());
            singleAuthorizeMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            ClientUtil.execute("authorizeTokenConfig", singleAuthorizeMap);
            viewType = "";
            //            super.setOpenForEditBy(observable.getStatusBy());
            //            super.removeEditLock(txtTokenConfigId.getText());
            btnCancelActionPerformed(null);
            lblStatus.setText(authorizeStatus);
        }
    }
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        tbrTokenConfig = new com.see.truetransact.uicomponent.CToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lbSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace34 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace35 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lbSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace36 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace37 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace38 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace39 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        panAccountInfo = new com.see.truetransact.uicomponent.CPanel();
        panInstEntry = new com.see.truetransact.uicomponent.CPanel();
        panOperations = new com.see.truetransact.uicomponent.CPanel();
        btnPasNew = new com.see.truetransact.uicomponent.CButton();
        btnPastSave = new com.see.truetransact.uicomponent.CButton();
        btnPasDelete = new com.see.truetransact.uicomponent.CButton();
        panStdInstructions = new com.see.truetransact.uicomponent.CPanel();
        lblCustId = new com.see.truetransact.uicomponent.CLabel();
        txtCustId = new com.see.truetransact.uicomponent.CTextField();
        lblPassword = new com.see.truetransact.uicomponent.CLabel();
        lblName = new com.see.truetransact.uicomponent.CLabel();
        txtName = new com.see.truetransact.uicomponent.CTextField();
        txtPassword = new com.see.truetransact.uicomponent.CPasswordField();
        srpInstructions = new com.see.truetransact.uicomponent.CScrollPane();
        tblInstruction2 = new com.see.truetransact.uicomponent.CTable();
        panStdInstructions1 = new com.see.truetransact.uicomponent.CPanel();
        lblLockerNo = new com.see.truetransact.uicomponent.CLabel();
        txtLockerNo = new com.see.truetransact.uicomponent.CTextField();
        btnLockerNo = new com.see.truetransact.uicomponent.CButton();
        lblCustName = new com.see.truetransact.uicomponent.CLabel();
        lblModeOfOp = new com.see.truetransact.uicomponent.CLabel();
        lblCustNameVal = new com.see.truetransact.uicomponent.CLabel();
        lblDate = new com.see.truetransact.uicomponent.CLabel();
        lblDateVal = new com.see.truetransact.uicomponent.CLabel();
        lblModeOfOpVal = new com.see.truetransact.uicomponent.CLabel();
        lblCustomerId = new com.see.truetransact.uicomponent.CLabel();
        lblCustomerIdVal = new com.see.truetransact.uicomponent.CLabel();
        lblLockerType = new com.see.truetransact.uicomponent.CLabel();
        lblLockerTypeVal = new com.see.truetransact.uicomponent.CLabel();
        lblIssueDate = new com.see.truetransact.uicomponent.CLabel();
        lblIssueDateVal = new com.see.truetransact.uicomponent.CLabel();
        lblExpiryDate = new com.see.truetransact.uicomponent.CLabel();
        lblExpiryDateVal = new com.see.truetransact.uicomponent.CLabel();
        lblConstitution = new com.see.truetransact.uicomponent.CLabel();
        lblConstitutionVal = new com.see.truetransact.uicomponent.CLabel();
        dtdLastOperatedOn = new com.see.truetransact.uicomponent.CLabel();
        lblLastOperatedOn = new com.see.truetransact.uicomponent.CLabel();
        panLockerOut = new com.see.truetransact.uicomponent.CPanel();
        btnLockerOut = new com.see.truetransact.uicomponent.CButton();
        lblLockerOutDt = new com.see.truetransact.uicomponent.CLabel();
        lblLockerOutDtVal = new com.see.truetransact.uicomponent.CLabel();
        btnPhotoSign = new com.see.truetransact.uicomponent.CButton();
        panStdInstructions3 = new com.see.truetransact.uicomponent.CPanel();
        lblLockerNo2 = new com.see.truetransact.uicomponent.CLabel();
        txtLockerNo2 = new com.see.truetransact.uicomponent.CTextField();
        btnLockerNo2 = new com.see.truetransact.uicomponent.CButton();
        lblCustName2 = new com.see.truetransact.uicomponent.CLabel();
        lblModeOfOp2 = new com.see.truetransact.uicomponent.CLabel();
        lblCustNameVal2 = new com.see.truetransact.uicomponent.CLabel();
        lblDate2 = new com.see.truetransact.uicomponent.CLabel();
        lblDateVal2 = new com.see.truetransact.uicomponent.CLabel();
        lblModeOfOpVal2 = new com.see.truetransact.uicomponent.CLabel();
        lblCustomerId2 = new com.see.truetransact.uicomponent.CLabel();
        lblCustomerIdVal2 = new com.see.truetransact.uicomponent.CLabel();
        lblLockerType2 = new com.see.truetransact.uicomponent.CLabel();
        lblLockerTypeVal2 = new com.see.truetransact.uicomponent.CLabel();
        lblIssueDate2 = new com.see.truetransact.uicomponent.CLabel();
        lblIssueDateVal2 = new com.see.truetransact.uicomponent.CLabel();
        lblExpiryDate2 = new com.see.truetransact.uicomponent.CLabel();
        lblExpiryDateVal2 = new com.see.truetransact.uicomponent.CLabel();
        lblConstitution2 = new com.see.truetransact.uicomponent.CLabel();
        lblConstitutionVal2 = new com.see.truetransact.uicomponent.CLabel();
        panStdInstructions4 = new com.see.truetransact.uicomponent.CPanel();
        lblLockerNo3 = new com.see.truetransact.uicomponent.CLabel();
        txtLockerNo3 = new com.see.truetransact.uicomponent.CTextField();
        btnLockerNo3 = new com.see.truetransact.uicomponent.CButton();
        lblCustName3 = new com.see.truetransact.uicomponent.CLabel();
        lblModeOfOp3 = new com.see.truetransact.uicomponent.CLabel();
        lblCustNameVal3 = new com.see.truetransact.uicomponent.CLabel();
        lblDate3 = new com.see.truetransact.uicomponent.CLabel();
        lblDateVal3 = new com.see.truetransact.uicomponent.CLabel();
        lblModeOfOpVal3 = new com.see.truetransact.uicomponent.CLabel();
        lblCustomerId3 = new com.see.truetransact.uicomponent.CLabel();
        lblCustomerIdVal3 = new com.see.truetransact.uicomponent.CLabel();
        lblLockerType3 = new com.see.truetransact.uicomponent.CLabel();
        lblLockerTypeVal3 = new com.see.truetransact.uicomponent.CLabel();
        lblIssueDate3 = new com.see.truetransact.uicomponent.CLabel();
        lblIssueDateVal3 = new com.see.truetransact.uicomponent.CLabel();
        lblExpiryDate3 = new com.see.truetransact.uicomponent.CLabel();
        lblExpiryDateVal3 = new com.see.truetransact.uicomponent.CLabel();
        lblConstitution3 = new com.see.truetransact.uicomponent.CLabel();
        lblConstitutionVal3 = new com.see.truetransact.uicomponent.CLabel();
        mbrTokenConfig = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptView = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMinimumSize(new java.awt.Dimension(714, 550));
        setPreferredSize(new java.awt.Dimension(714, 550));

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
        tbrTokenConfig.add(btnView);

        lbSpace3.setText("     ");
        tbrTokenConfig.add(lbSpace3);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnNew);

        lblSpace34.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace34.setText("     ");
        lblSpace34.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace34);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnEdit);

        lblSpace35.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace35.setText("     ");
        lblSpace35.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace35.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace35.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace35);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnDelete);

        lbSpace2.setText("     ");
        tbrTokenConfig.add(lbSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnSave);

        lblSpace36.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace36.setText("     ");
        lblSpace36.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace36.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace36.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace36);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnCancel);

        lblSpace3.setText("     ");
        tbrTokenConfig.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnAuthorize);

        lblSpace37.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace37.setText("     ");
        lblSpace37.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace37.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace37.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace37);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnException);

        lblSpace38.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace38.setText("     ");
        lblSpace38.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace38.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace38.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace38);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnReject);

        lblSpace5.setText("     ");
        tbrTokenConfig.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrTokenConfig.add(btnPrint);

        lblSpace39.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace39.setText("     ");
        lblSpace39.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace39.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace39.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace39);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnClose);

        getContentPane().add(tbrTokenConfig, java.awt.BorderLayout.NORTH);

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

        panAccountInfo.setBorder(javax.swing.BorderFactory.createTitledBorder("Account Info."));
        panAccountInfo.setMinimumSize(new java.awt.Dimension(239, 100));
        panAccountInfo.setName("panAccountInfo");
        panAccountInfo.setPreferredSize(new java.awt.Dimension(239, 100));
        panAccountInfo.setLayout(new java.awt.GridBagLayout());

        panInstEntry.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panInstEntry.setLayout(new java.awt.GridBagLayout());

        panOperations.setLayout(new java.awt.GridBagLayout());

        btnPasNew.setText("New");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOperations.add(btnPasNew, gridBagConstraints);

        btnPastSave.setText("Save");
        btnPastSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPastSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOperations.add(btnPastSave, gridBagConstraints);

        btnPasDelete.setText("Delete");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOperations.add(btnPasDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 282);
        panInstEntry.add(panOperations, gridBagConstraints);

        panStdInstructions.setLayout(new java.awt.GridBagLayout());

        lblCustId.setText("Customer ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panStdInstructions.add(lblCustId, gridBagConstraints);

        txtCustId.setMaxLength(10);
        txtCustId.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panStdInstructions.add(txtCustId, gridBagConstraints);

        lblPassword.setText("Password");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panStdInstructions.add(lblPassword, gridBagConstraints);

        lblName.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panStdInstructions.add(lblName, gridBagConstraints);

        txtName.setMaxLength(30);
        txtName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panStdInstructions.add(txtName, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panStdInstructions.add(txtPassword, gridBagConstraints);

        panInstEntry.add(panStdInstructions, new java.awt.GridBagConstraints());

        srpInstructions.setMinimumSize(new java.awt.Dimension(250, 250));
        srpInstructions.setPreferredSize(new java.awt.Dimension(300, 100));

        tblInstruction2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Cust ID", "Name", "Password"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblInstruction2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblInstruction2MousePressed(evt);
            }
        });
        srpInstructions.setViewportView(tblInstruction2);

        panInstEntry.add(srpInstructions, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 33, 0, 0);
        panAccountInfo.add(panInstEntry, gridBagConstraints);

        panStdInstructions1.setPreferredSize(new java.awt.Dimension(405, 143));
        panStdInstructions1.setLayout(new java.awt.GridBagLayout());

        lblLockerNo.setText("Locker No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panStdInstructions1.add(lblLockerNo, gridBagConstraints);

        txtLockerNo.setEditable(false);
        txtLockerNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 0);
        panStdInstructions1.add(txtLockerNo, gridBagConstraints);

        btnLockerNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnLockerNo.setToolTipText("Customer Data");
        btnLockerNo.setMaximumSize(new java.awt.Dimension(21, 21));
        btnLockerNo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnLockerNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnLockerNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLockerNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panStdInstructions1.add(btnLockerNo, gridBagConstraints);

        lblCustName.setText("Customer Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panStdInstructions1.add(lblCustName, gridBagConstraints);

        lblModeOfOp.setText("Mode of Operation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panStdInstructions1.add(lblModeOfOp, gridBagConstraints);

        lblCustNameVal.setForeground(new java.awt.Color(0, 51, 204));
        lblCustNameVal.setMaximumSize(new java.awt.Dimension(200, 18));
        lblCustNameVal.setMinimumSize(new java.awt.Dimension(100, 18));
        lblCustNameVal.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 0);
        panStdInstructions1.add(lblCustNameVal, gridBagConstraints);

        lblDate.setText("Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panStdInstructions1.add(lblDate, gridBagConstraints);

        lblDateVal.setMaximumSize(new java.awt.Dimension(100, 18));
        lblDateVal.setMinimumSize(new java.awt.Dimension(100, 18));
        lblDateVal.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 0);
        panStdInstructions1.add(lblDateVal, gridBagConstraints);

        lblModeOfOpVal.setMaximumSize(new java.awt.Dimension(100, 18));
        lblModeOfOpVal.setMinimumSize(new java.awt.Dimension(100, 18));
        lblModeOfOpVal.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 0);
        panStdInstructions1.add(lblModeOfOpVal, gridBagConstraints);

        lblCustomerId.setText("Customer ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panStdInstructions1.add(lblCustomerId, gridBagConstraints);

        lblCustomerIdVal.setMaximumSize(new java.awt.Dimension(100, 18));
        lblCustomerIdVal.setMinimumSize(new java.awt.Dimension(100, 18));
        lblCustomerIdVal.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 0);
        panStdInstructions1.add(lblCustomerIdVal, gridBagConstraints);

        lblLockerType.setText("Locker Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panStdInstructions1.add(lblLockerType, gridBagConstraints);

        lblLockerTypeVal.setMaximumSize(new java.awt.Dimension(200, 18));
        lblLockerTypeVal.setMinimumSize(new java.awt.Dimension(100, 18));
        lblLockerTypeVal.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panStdInstructions1.add(lblLockerTypeVal, gridBagConstraints);

        lblIssueDate.setText("Issue Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panStdInstructions1.add(lblIssueDate, gridBagConstraints);

        lblIssueDateVal.setMaximumSize(new java.awt.Dimension(200, 18));
        lblIssueDateVal.setMinimumSize(new java.awt.Dimension(100, 18));
        lblIssueDateVal.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panStdInstructions1.add(lblIssueDateVal, gridBagConstraints);

        lblExpiryDate.setText("Expiry Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panStdInstructions1.add(lblExpiryDate, gridBagConstraints);

        lblExpiryDateVal.setMaximumSize(new java.awt.Dimension(200, 18));
        lblExpiryDateVal.setMinimumSize(new java.awt.Dimension(100, 18));
        lblExpiryDateVal.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panStdInstructions1.add(lblExpiryDateVal, gridBagConstraints);

        lblConstitution.setText("Constitution");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panStdInstructions1.add(lblConstitution, gridBagConstraints);

        lblConstitutionVal.setMaximumSize(new java.awt.Dimension(200, 18));
        lblConstitutionVal.setMinimumSize(new java.awt.Dimension(100, 18));
        lblConstitutionVal.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panStdInstructions1.add(lblConstitutionVal, gridBagConstraints);

        dtdLastOperatedOn.setMaximumSize(new java.awt.Dimension(100, 18));
        dtdLastOperatedOn.setMinimumSize(new java.awt.Dimension(100, 18));
        dtdLastOperatedOn.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panStdInstructions1.add(dtdLastOperatedOn, gridBagConstraints);

        lblLastOperatedOn.setText("Last Operated On");
        lblLastOperatedOn.setMaximumSize(new java.awt.Dimension(105, 18));
        lblLastOperatedOn.setMinimumSize(new java.awt.Dimension(105, 18));
        lblLastOperatedOn.setPreferredSize(new java.awt.Dimension(104, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panStdInstructions1.add(lblLastOperatedOn, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccountInfo.add(panStdInstructions1, gridBagConstraints);

        panLockerOut.setMinimumSize(new java.awt.Dimension(253, 57));
        panLockerOut.setPreferredSize(new java.awt.Dimension(253, 57));
        panLockerOut.setLayout(new java.awt.GridBagLayout());

        btnLockerOut.setText("Locker Out");
        btnLockerOut.setMaximumSize(new java.awt.Dimension(100, 27));
        btnLockerOut.setMinimumSize(new java.awt.Dimension(100, 27));
        btnLockerOut.setPreferredSize(new java.awt.Dimension(100, 27));
        btnLockerOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLockerOutActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLockerOut.add(btnLockerOut, gridBagConstraints);

        lblLockerOutDt.setText("Locker Out Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLockerOut.add(lblLockerOutDt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panLockerOut.add(lblLockerOutDtVal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 9, 0);
        panAccountInfo.add(panLockerOut, gridBagConstraints);

        btnPhotoSign.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PHOTO_SIGN.gif"))); // NOI18N
        btnPhotoSign.setMaximumSize(new java.awt.Dimension(70, 27));
        btnPhotoSign.setMinimumSize(new java.awt.Dimension(70, 27));
        btnPhotoSign.setPreferredSize(new java.awt.Dimension(70, 27));
        btnPhotoSign.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPhotoSignActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        panAccountInfo.add(btnPhotoSign, gridBagConstraints);

        panStdInstructions3.setLayout(new java.awt.GridBagLayout());

        lblLockerNo2.setText("Locker No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panStdInstructions3.add(lblLockerNo2, gridBagConstraints);

        txtLockerNo2.setEditable(false);
        txtLockerNo2.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 0);
        panStdInstructions3.add(txtLockerNo2, gridBagConstraints);

        btnLockerNo2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnLockerNo2.setToolTipText("Customer Data");
        btnLockerNo2.setMaximumSize(new java.awt.Dimension(21, 21));
        btnLockerNo2.setMinimumSize(new java.awt.Dimension(21, 21));
        btnLockerNo2.setPreferredSize(new java.awt.Dimension(21, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panStdInstructions3.add(btnLockerNo2, gridBagConstraints);

        lblCustName2.setText("Customer Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panStdInstructions3.add(lblCustName2, gridBagConstraints);

        lblModeOfOp2.setText("Mode of Operation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panStdInstructions3.add(lblModeOfOp2, gridBagConstraints);

        lblCustNameVal2.setForeground(new java.awt.Color(0, 51, 204));
        lblCustNameVal2.setMaximumSize(new java.awt.Dimension(200, 18));
        lblCustNameVal2.setMinimumSize(new java.awt.Dimension(100, 18));
        lblCustNameVal2.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 0);
        panStdInstructions3.add(lblCustNameVal2, gridBagConstraints);

        lblDate2.setText("Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panStdInstructions3.add(lblDate2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 0);
        panStdInstructions3.add(lblDateVal2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 0);
        panStdInstructions3.add(lblModeOfOpVal2, gridBagConstraints);

        lblCustomerId2.setText("Customer ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panStdInstructions3.add(lblCustomerId2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 0);
        panStdInstructions3.add(lblCustomerIdVal2, gridBagConstraints);

        lblLockerType2.setText("Locker Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panStdInstructions3.add(lblLockerType2, gridBagConstraints);

        lblLockerTypeVal2.setMaximumSize(new java.awt.Dimension(200, 18));
        lblLockerTypeVal2.setMinimumSize(new java.awt.Dimension(100, 18));
        lblLockerTypeVal2.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panStdInstructions3.add(lblLockerTypeVal2, gridBagConstraints);

        lblIssueDate2.setText("Issue Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panStdInstructions3.add(lblIssueDate2, gridBagConstraints);

        lblIssueDateVal2.setMaximumSize(new java.awt.Dimension(200, 18));
        lblIssueDateVal2.setMinimumSize(new java.awt.Dimension(100, 18));
        lblIssueDateVal2.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panStdInstructions3.add(lblIssueDateVal2, gridBagConstraints);

        lblExpiryDate2.setText("Expiry Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panStdInstructions3.add(lblExpiryDate2, gridBagConstraints);

        lblExpiryDateVal2.setMaximumSize(new java.awt.Dimension(200, 18));
        lblExpiryDateVal2.setMinimumSize(new java.awt.Dimension(100, 18));
        lblExpiryDateVal2.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panStdInstructions3.add(lblExpiryDateVal2, gridBagConstraints);

        lblConstitution2.setText("Constitution");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panStdInstructions3.add(lblConstitution2, gridBagConstraints);

        lblConstitutionVal2.setMaximumSize(new java.awt.Dimension(200, 18));
        lblConstitutionVal2.setMinimumSize(new java.awt.Dimension(100, 18));
        lblConstitutionVal2.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panStdInstructions3.add(lblConstitutionVal2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccountInfo.add(panStdInstructions3, gridBagConstraints);

        panStdInstructions4.setLayout(new java.awt.GridBagLayout());

        lblLockerNo3.setText("Locker No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panStdInstructions4.add(lblLockerNo3, gridBagConstraints);

        txtLockerNo3.setEditable(false);
        txtLockerNo3.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 0);
        panStdInstructions4.add(txtLockerNo3, gridBagConstraints);

        btnLockerNo3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnLockerNo3.setToolTipText("Customer Data");
        btnLockerNo3.setMaximumSize(new java.awt.Dimension(21, 21));
        btnLockerNo3.setMinimumSize(new java.awt.Dimension(21, 21));
        btnLockerNo3.setPreferredSize(new java.awt.Dimension(21, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panStdInstructions4.add(btnLockerNo3, gridBagConstraints);

        lblCustName3.setText("Customer Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panStdInstructions4.add(lblCustName3, gridBagConstraints);

        lblModeOfOp3.setText("Mode of Operation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panStdInstructions4.add(lblModeOfOp3, gridBagConstraints);

        lblCustNameVal3.setForeground(new java.awt.Color(0, 51, 204));
        lblCustNameVal3.setMaximumSize(new java.awt.Dimension(200, 18));
        lblCustNameVal3.setMinimumSize(new java.awt.Dimension(100, 18));
        lblCustNameVal3.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 0);
        panStdInstructions4.add(lblCustNameVal3, gridBagConstraints);

        lblDate3.setText("Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panStdInstructions4.add(lblDate3, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 0);
        panStdInstructions4.add(lblDateVal3, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 0);
        panStdInstructions4.add(lblModeOfOpVal3, gridBagConstraints);

        lblCustomerId3.setText("Customer ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panStdInstructions4.add(lblCustomerId3, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 0);
        panStdInstructions4.add(lblCustomerIdVal3, gridBagConstraints);

        lblLockerType3.setText("Locker Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panStdInstructions4.add(lblLockerType3, gridBagConstraints);

        lblLockerTypeVal3.setMaximumSize(new java.awt.Dimension(200, 18));
        lblLockerTypeVal3.setMinimumSize(new java.awt.Dimension(100, 18));
        lblLockerTypeVal3.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panStdInstructions4.add(lblLockerTypeVal3, gridBagConstraints);

        lblIssueDate3.setText("Issue Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panStdInstructions4.add(lblIssueDate3, gridBagConstraints);

        lblIssueDateVal3.setMaximumSize(new java.awt.Dimension(200, 18));
        lblIssueDateVal3.setMinimumSize(new java.awt.Dimension(100, 18));
        lblIssueDateVal3.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panStdInstructions4.add(lblIssueDateVal3, gridBagConstraints);

        lblExpiryDate3.setText("Expiry Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panStdInstructions4.add(lblExpiryDate3, gridBagConstraints);

        lblExpiryDateVal3.setMaximumSize(new java.awt.Dimension(200, 18));
        lblExpiryDateVal3.setMinimumSize(new java.awt.Dimension(100, 18));
        lblExpiryDateVal3.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panStdInstructions4.add(lblExpiryDateVal3, gridBagConstraints);

        lblConstitution3.setText("Constitution");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panStdInstructions4.add(lblConstitution3, gridBagConstraints);

        lblConstitutionVal3.setMaximumSize(new java.awt.Dimension(200, 18));
        lblConstitutionVal3.setMinimumSize(new java.awt.Dimension(100, 18));
        lblConstitutionVal3.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panStdInstructions4.add(lblConstitutionVal3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccountInfo.add(panStdInstructions4, gridBagConstraints);

        getContentPane().add(panAccountInfo, java.awt.BorderLayout.CENTER);

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
        mnuProcess.add(sptView);

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

        mitClose.setText("Close");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrTokenConfig.add(mnuProcess);

        setJMenuBar(mbrTokenConfig);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLockerNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLockerNoActionPerformed
        // TODO add your handling code here:
        callView("LockerNo");
        //btnPasNew.setEnabled(true);
        
        
        
    }//GEN-LAST:event_btnLockerNoActionPerformed

    private void btnPhotoSignActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPhotoSignActionPerformed
        // TODO add your handling code here:
        if(issueId!=null && !issueId.equalsIgnoreCase("")) {
            new com.see.truetransact.ui.common.viewphotosign.ViewPhotoSignUI(issueId, "LOCKER").show();
        }
    }//GEN-LAST:event_btnPhotoSignActionPerformed

    private void btnLockerOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLockerOutActionPerformed
        // TODO add your handling code here:
        lblLockerOutDtVal.setText(CommonUtil.convertObjToStr(ClientUtil.getCurrentDateWithTime()));
        btnLockerOut.setEnabled(false);
    }//GEN-LAST:event_btnLockerOutActionPerformed

    private void btnPastSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPastSaveActionPerformed
        // TODO add your handling code here:
        updateOBFields();
        observable.setOperatingCustomer(txtCustId.getText()); // Added by nithya on 11-12-2018 for KD -289 Locker operation for joint operators are not marking properly.
        if (txtPassword.getText().length()==0) {
            ClientUtil.showMessageWindow("Password field is empty...");
        }
        addRow();
        clearTblVal();
        ClientUtil.enableDisable(panInstEntry, false, false, true);
    
     
    }//GEN-LAST:event_btnPastSaveActionPerformed
private void addRow(){
        if(selectedData==1){
            HashMap param = new HashMap();
            param.put("CUST_ID", txtCustId.getText());
            param.put("LOCKER_NUM", txtLockerNo.getText());
            param.put("PROD_ID", getProdID());
            List lstData = (List)ClientUtil.executeQuery("getLockerPwd", param);
            param = null;
            String pwd = "";
            String status = "";
            String custId = "";
            if(lstData!=null && lstData.size() > 0){
                param = (HashMap)lstData.get(0);
                
                if(param.equals(null)){
                    ClientUtil.showMessageWindow("password not required");
                    
                    observable.setTableValueAt(selectedRow);
                    pwdSave=true;
                }
                else {
                    try {
                        pwd = CommonUtil.convertObjToStr(param.get("PWD"));
                        pwd = pwd.length() == 0 ? pwd : encrypt.decrypt(pwd); //CommonUtil.convertObjToStr(param.get("PWD"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    status = CommonUtil.convertObjToStr(param.get("STATUS"));
                    custId = CommonUtil.convertObjToStr(param.get("CUST_ID"));
                }
                if(txtPassword.getText().equals(CommonUtil.convertObjToStr(pwd))){
                  
                    observable.setTableValueAt(selectedRow);
                    pwdSave = true;
                    ClientUtil.showMessageWindow("User is authenticated");
                }else{
                    ClientUtil.showMessageWindow("Pwd doesnt match");
                   
                }
            }
           
        }
        else{
            /** when clicked on the new button related to tblInstruction **/
            result=-1;
            result = observable.addTblInstructionData();
            if(result==0){
                ClientUtil.enableDisable(panStdInstructions,true);
              
            }
            if(result==1){
                ClientUtil.enableDisable(panStdInstructions,false);
            }
            if (result == 2){
                
                ClientUtil.enableDisable(panStdInstructions,true);
                
            }
            if(result==-1){
                
                ClientUtil.enableDisable(panInstEntry,false);
                
            }
            
        }
}
    private void tblInstruction2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblInstruction2MousePressed
        // TODO add your handling code here:
          selectedRow = -1;
          if(observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE){
              selectedData = 1;
              selectedRow = tblInstruction2.getSelectedRow();
              updateOBFields();
              observable.populateSelectedRowAct(tblInstruction2.getSelectedRow());
              setTblVal();
              ClientUtil.enableDisable(panInstEntry, true, false, true);
          }
    }//GEN-LAST:event_tblInstruction2MousePressed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        callView(ClientConstants.ACTION_STATUS[17]);
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed
            
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // TODO add your handling code here:
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // TODO add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // TODO add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // TODO add your handling code here:
        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // TODO add your handling code here:
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed
        
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        if(observable.getAuthorizeStatus()!=null)

        setModified(false); 
        observable.resetForm();

        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);

        viewType = "";
        settings();
        clearTblVal();
        lblLockerTypeVal.setText("");
        lblIssueDateVal.setText("");
        lblExpiryDateVal.setText("");
        lblConstitutionVal.setText("");
        
         setModified(false);
         panLockerOut.setVisible(false);
         btnAuthorize.setEnabled(false);
         btnReject.setEnabled(false);
         btnException.setEnabled(false);
         btnNew.setEnabled(true);
       
         btnEdit.setEnabled(true);
         btnDelete.setEnabled(true);
         btnCancel.setEnabled(false);
         dtdLastOperatedOn.setVisible(false);
         lblLastOperatedOn.setVisible(false);
      
    }//GEN-LAST:event_btnCancelActionPerformed
                    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        callView( ClientConstants.ACTION_STATUS[3]);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        callView(ClientConstants.ACTION_STATUS[2]);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW){
            HashMap hashmap=new HashMap();
            String custid=lblCustomerIdVal.getText();
            hashmap.put("CUST_ID",custid);
            hashmap.put("MEMBER_NO",custid);
            List lst1=ClientUtil.executeQuery("getDeathDetailsForAcsOpening", hashmap);
            if(lst1!=null && lst1.size()>0){
                ClientUtil.displayAlert("Customer is death marked please select another customerId");
                return;
            }
        }
        setModified(false);
        savePerformed();
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // TODO add your handling code here:
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        HashMap hmap=new HashMap();
        hmap.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
        java.util.List lst=ClientUtil.executeQuery("getLockerOperationDetails", hmap);
        if(lst!=null && lst.size()>0){
            hmap=(HashMap)lst.get(0);
            String lno=CommonUtil.convertObjToStr(hmap.get("LOCKER_NUM"));
            String NAME=CommonUtil.convertObjToStr(hmap.get("ACCT_NAME"));
            ClientUtil.displayAlert("Locker Operation Not yet Completed Locker No:"+lno+" Customer Name:"+NAME);
             btnCancelActionPerformed(null);
             return;
        }
        setModified(true);
        settings();
        btnLockerNo.setEnabled(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        //btnLockerNo.setEnabled(true);
        ClientUtil.enableDisable(panStdInstructions, true);
        ClientUtil.enableDisable(panInstEntry, false, false, true);
      // btnLockerNo.setEnabled(true);
        panLockerOut.setVisible(false);
        btnPhotoSign.setEnabled(true);
       
    }//GEN-LAST:event_btnNewActionPerformed

private void btnPasNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPasNewActionPerformed
// TODO add your handling code here:
    //panStdInstructions.setEnabled(true);
    //txtCustId.setEnabled(true);
    //txtName.setEnabled(true);
    txtPassword.setEnabled(true);
    btnPastSave.setEnabled(true);
    btnPasDelete.setEnabled(true);    
}//GEN-LAST:event_btnPasNewActionPerformed
    private void btnCheck()
    {
         btnCancel.setEnabled(true);
         btnNew.setEnabled(false);
         btnDelete.setEnabled(false);
         btnAuthorize.setEnabled(false);
         btnReject.setEnabled(false);
         btnException.setEnabled(false);
         btnEdit.setEnabled(false);
     }

    /**
     * Getter for property prodID.
     * @return Value of property prodID.
     */
    public java.lang.String getProdID() {
        return prodID;
    }    
    
    /**
     * Setter for property prodID.
     * @param prodID New value of property prodID.
     */
    public void setProdID(java.lang.String prodID) {
        this.prodID = prodID;
    }
    
    /**
     * Getter for property issueId.
     * @return Value of property issueId.
     */
    public java.lang.String getIssueId() {
        return issueId;
    }
    
    /**
     * Setter for property issueId.
     * @param issueId New value of property issueId.
     */
    public void setIssueId(java.lang.String issueId) {
        this.issueId = issueId;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnLockerNo;
    private com.see.truetransact.uicomponent.CButton btnLockerNo2;
    private com.see.truetransact.uicomponent.CButton btnLockerNo3;
    private com.see.truetransact.uicomponent.CButton btnLockerOut;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPasDelete;
    private com.see.truetransact.uicomponent.CButton btnPasNew;
    private com.see.truetransact.uicomponent.CButton btnPastSave;
    private com.see.truetransact.uicomponent.CButton btnPhotoSign;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CLabel dtdLastOperatedOn;
    private com.see.truetransact.uicomponent.CLabel lbSpace2;
    private com.see.truetransact.uicomponent.CLabel lbSpace3;
    private com.see.truetransact.uicomponent.CLabel lblConstitution;
    private com.see.truetransact.uicomponent.CLabel lblConstitution2;
    private com.see.truetransact.uicomponent.CLabel lblConstitution3;
    private com.see.truetransact.uicomponent.CLabel lblConstitutionVal;
    private com.see.truetransact.uicomponent.CLabel lblConstitutionVal2;
    private com.see.truetransact.uicomponent.CLabel lblConstitutionVal3;
    private com.see.truetransact.uicomponent.CLabel lblCustId;
    private com.see.truetransact.uicomponent.CLabel lblCustName;
    private com.see.truetransact.uicomponent.CLabel lblCustName2;
    private com.see.truetransact.uicomponent.CLabel lblCustName3;
    private com.see.truetransact.uicomponent.CLabel lblCustNameVal;
    private com.see.truetransact.uicomponent.CLabel lblCustNameVal2;
    private com.see.truetransact.uicomponent.CLabel lblCustNameVal3;
    private com.see.truetransact.uicomponent.CLabel lblCustomerId;
    private com.see.truetransact.uicomponent.CLabel lblCustomerId2;
    private com.see.truetransact.uicomponent.CLabel lblCustomerId3;
    private com.see.truetransact.uicomponent.CLabel lblCustomerIdVal;
    private com.see.truetransact.uicomponent.CLabel lblCustomerIdVal2;
    private com.see.truetransact.uicomponent.CLabel lblCustomerIdVal3;
    private com.see.truetransact.uicomponent.CLabel lblDate;
    private com.see.truetransact.uicomponent.CLabel lblDate2;
    private com.see.truetransact.uicomponent.CLabel lblDate3;
    private com.see.truetransact.uicomponent.CLabel lblDateVal;
    private com.see.truetransact.uicomponent.CLabel lblDateVal2;
    private com.see.truetransact.uicomponent.CLabel lblDateVal3;
    private com.see.truetransact.uicomponent.CLabel lblExpiryDate;
    private com.see.truetransact.uicomponent.CLabel lblExpiryDate2;
    private com.see.truetransact.uicomponent.CLabel lblExpiryDate3;
    private com.see.truetransact.uicomponent.CLabel lblExpiryDateVal;
    private com.see.truetransact.uicomponent.CLabel lblExpiryDateVal2;
    private com.see.truetransact.uicomponent.CLabel lblExpiryDateVal3;
    private com.see.truetransact.uicomponent.CLabel lblIssueDate;
    private com.see.truetransact.uicomponent.CLabel lblIssueDate2;
    private com.see.truetransact.uicomponent.CLabel lblIssueDate3;
    private com.see.truetransact.uicomponent.CLabel lblIssueDateVal;
    private com.see.truetransact.uicomponent.CLabel lblIssueDateVal2;
    private com.see.truetransact.uicomponent.CLabel lblIssueDateVal3;
    private com.see.truetransact.uicomponent.CLabel lblLastOperatedOn;
    private com.see.truetransact.uicomponent.CLabel lblLockerNo;
    private com.see.truetransact.uicomponent.CLabel lblLockerNo2;
    private com.see.truetransact.uicomponent.CLabel lblLockerNo3;
    private com.see.truetransact.uicomponent.CLabel lblLockerOutDt;
    private com.see.truetransact.uicomponent.CLabel lblLockerOutDtVal;
    private com.see.truetransact.uicomponent.CLabel lblLockerType;
    private com.see.truetransact.uicomponent.CLabel lblLockerType2;
    private com.see.truetransact.uicomponent.CLabel lblLockerType3;
    private com.see.truetransact.uicomponent.CLabel lblLockerTypeVal;
    private com.see.truetransact.uicomponent.CLabel lblLockerTypeVal2;
    private com.see.truetransact.uicomponent.CLabel lblLockerTypeVal3;
    private com.see.truetransact.uicomponent.CLabel lblModeOfOp;
    private com.see.truetransact.uicomponent.CLabel lblModeOfOp2;
    private com.see.truetransact.uicomponent.CLabel lblModeOfOp3;
    private com.see.truetransact.uicomponent.CLabel lblModeOfOpVal;
    private com.see.truetransact.uicomponent.CLabel lblModeOfOpVal2;
    private com.see.truetransact.uicomponent.CLabel lblModeOfOpVal3;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblName;
    private com.see.truetransact.uicomponent.CLabel lblPassword;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace34;
    private com.see.truetransact.uicomponent.CLabel lblSpace35;
    private com.see.truetransact.uicomponent.CLabel lblSpace36;
    private com.see.truetransact.uicomponent.CLabel lblSpace37;
    private com.see.truetransact.uicomponent.CLabel lblSpace38;
    private com.see.truetransact.uicomponent.CLabel lblSpace39;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CMenuBar mbrTokenConfig;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAccountInfo;
    private com.see.truetransact.uicomponent.CPanel panInstEntry;
    private com.see.truetransact.uicomponent.CPanel panLockerOut;
    private com.see.truetransact.uicomponent.CPanel panOperations;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panStdInstructions;
    private com.see.truetransact.uicomponent.CPanel panStdInstructions1;
    private com.see.truetransact.uicomponent.CPanel panStdInstructions3;
    private com.see.truetransact.uicomponent.CPanel panStdInstructions4;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptView;
    private com.see.truetransact.uicomponent.CScrollPane srpInstructions;
    private com.see.truetransact.uicomponent.CTable tblInstruction2;
    private com.see.truetransact.uicomponent.CToolBar tbrTokenConfig;
    private com.see.truetransact.uicomponent.CTextField txtCustId;
    private com.see.truetransact.uicomponent.CTextField txtLockerNo;
    private com.see.truetransact.uicomponent.CTextField txtLockerNo2;
    private com.see.truetransact.uicomponent.CTextField txtLockerNo3;
    private com.see.truetransact.uicomponent.CTextField txtName;
    private com.see.truetransact.uicomponent.CPasswordField txtPassword;
    // End of variables declaration//GEN-END:variables
    
}
