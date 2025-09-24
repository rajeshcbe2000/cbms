/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * InstrumentAllotmentUI.java
 *
 * Created on January 20, 2005, 3:03 PM
 */

package com.see.truetransact.ui.remittance.printingdetails;

/**
 *
 * @author  ashokvijayakumar
 */
import com.see.truetransact.uicomponent.CInternalFrame;

import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uivalidation.NumericValidation;
import java.util.ResourceBundle;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uicomponent.CTextField;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.HashMap;
import java.util.ArrayList;

public class InstrumentAllotmentUI extends CInternalFrame implements Observer, UIMandatoryField{
    
    /** Vairable Declarations */
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("com.see.truetransact.ui.transaction.token.tokenissue.TokenIssueRB", ProxyParameters.LANGUAGE);//Creating Instance For ResourceBundle-TokenConfigRB
    private InstrumentAllotmentOB observable; //Reference for the Observable Class TokenConfigOB
//    private TokenIssueMRB objMandatoryRB = new TokenIssueMRB();//Instance for the MandatoryResourceBundle
    private final static ClientParseException parseException = ClientParseException.getInstance();//Instance of ClientParseException to log the Exceptions
    private HashMap mandatoryMap;//Map for putting up MandatoryFields in the UI
    private String viewType = "";//Variable used to store ActionType(New,Edit,Delete)
    private final String AUTHORIZE="Authorize";//Variable used when btnAuthorize is clicked
    private final String ISSUED = "ISSUED";//Varaible used to make TOKEN_STATUS AS ISSUED when authorize button is clicked
    private Date currDt = null;
    /** Creates new form TokenConfigUI */
    public InstrumentAllotmentUI() {
        currDt = ClientUtil.getCurrentDate();
        initForm();
    }
    
    /** Method which is used to initialize the form TokenConfig */
    private void initForm(){
        
        initComponents();
        setFieldNames();
        setObservable();
        observable.resetForm();
        initComponentData();
//        setMandatoryHashMap();
        setMaxLengths();
//        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panTokenIssue);
        ClientUtil.enableDisable(panTokenIssue, false);
        setButtonEnableDisable();
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
        lbSpace2.setName("lbSpace2");
        lblMsg.setName("lblMsg");
        lblNoOfTokens.setName("lblNoOfTokens");
        lblSeriesNo.setName("lblSeriesNo");
        lblSpace1.setName("lblSpace1");
        lblSpace3.setName("lblSpace3");
        lblSpace5.setName("lblSpace5");  
        lblStatus.setName("lblStatus");
        mbrTokenConfig.setName("mbrTokenConfig");
        panStatus.setName("panStatus");
        panTokenIssue.setName("panTokenIssue");
        panTokenLost.setName("panTokenLost");
        tblTokenLost.setName("tblTokenLost");
        txtNoOfInstruments.setName("txtNoOfInstruments");
        cboSeriesNo.setName("cboSeriesNo");
        lblTokenIssueId.setName("lblTokenIssueId");
        txtTokenIssueId.setName("txtTokenIssueId");
        txtReceiverId.setName("txtReceiverId");
        btnUserId.setName("btnUserId");
        lblEndingInstrumentNo.setName("lblEndingInstrumentNo");
        txtEndingInstrumentNo.setName("txtEndingInstrumentNo");
        lblStartingInstrumentNo.setName("lblStartingInstrumentNo");
        txtStartingInstrumentNo.setName("txtStartingInstrumentNo");
    }
    
 /* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        lblSpace5.setText(resourceBundle.getString("lblSpace5"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblNoOfTokens.setText(resourceBundle.getString("lblNoOfTokens"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        btnUserId.setText(resourceBundle.getString("btnUserId"));
        lblSeriesNo.setText(resourceBundle.getString("lblSeriesNo"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lbSpace2.setText(resourceBundle.getString("lbSpace2"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblTokenIssueId.setText(resourceBundle.getString("lblTokenIssueId"));
        lblReceiverId.setText(resourceBundle.getString("lblReceiverId"));
        lblEndingInstrumentNo.setText(resourceBundle.getString("lblEndingInstrumentNo"));
        lblStartingInstrumentNo.setText(resourceBundle.getString("lblStartingInstrumentNo"));
        
    }
    
    /** Adds up the Observable to this form */
    private void setObservable() {
        try{
            observable = InstrumentAllotmentOB.getInstance();
            observable.addObserver(this);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /*Setting model to the combobox cboTokenType  */
    private void initComponentData() {
        try{
           
            cboSeriesNo.setModel(observable.getCbmSeriesNo());
        }catch(ClassCastException e){
            parseException.logException(e,true);
        }
    }
    
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
    txtTokenIssueId.setText(observable.getTxtInstrumentIssueId());
        txtNoOfInstruments.setText(observable.getTxtNoOfInstruments());
        cboSeriesNo.setSelectedItem(observable.getCboSeriesNo());
        txtReceiverId.setText(observable.getTxtReceiverId());
       txtStartingInstrumentNo.setText(observable.getTxtStartingInstrumentNo());
        txtEndingInstrumentNo.setText(observable.getTxtEndingInstrumentNo());
        lblStatus.setText(observable.getLblStatus());
    }
    
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
     observable.setTxtInstrumentIssueId(txtTokenIssueId.getText());
      observable.setTxtNoOfInstruments(txtNoOfInstruments.getText());
        observable.setCboSeriesNo(CommonUtil.convertObjToStr(cboSeriesNo.getSelectedItem()));
  
        observable.setTxtStartingInstrumentNo(txtStartingInstrumentNo.getText());
         observable.setTxtEndingInstrumentNo(txtEndingInstrumentNo.getText());
        observable.setTxtReceiverId(txtReceiverId.getText());
        observable.setModule(getModule());
        observable.setScreen(getScreen());
    }
    
    
/* Auto Generated Method - setMandatoryHashMap()
 
ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
 
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboTokenType", new Boolean(true));
        mandatoryMap.put("cboSeriesNo", new Boolean(false));
        mandatoryMap.put("txtStartingTokenNo", new Boolean(true));
        mandatoryMap.put("txtEndingTokenNo", new Boolean(true));
        mandatoryMap.put("txtNoOfTokens", new Boolean(true));
        mandatoryMap.put("txtReceiverId", new Boolean(true));
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
//        cboSeriesNo.setHelpMessage(lblMsg, objMandatoryRB.getString("cboSeriesNo"));
//        txtStartingTokenNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtStartingTokenNo"));
//        txtEndingTokenNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtEndingTokenNo"));
//        txtNoOfTokens.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNoOfTokens"));
//        txtTokenIssueId.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTokenIssueId"));
//        txtReceiverId.setHelpMessage(lblMsg, objMandatoryRB.getString("txtReceiverId"));
    }
    
    /** Used to set Maximum possible lenghts for TextFields */
    private void setMaxLengths(){
  
        
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
  
 
            observable.execute(status);
            if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED){
//                HashMap lockMap = new HashMap();
//                ArrayList lst = new ArrayList();
//                lst.add("ISSUE_ID");
//                lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
//                if (observable.getProxyReturnMap()!=null) {
//                    if (observable.getProxyReturnMap().containsKey("ISSUE_ID")) {
//                        lockMap.put("ISSUE_ID", observable.getProxyReturnMap().get("ISSUE_ID"));
//                    }
//                }
//                if (status==CommonConstants.TOSTATUS_UPDATE) {
//                    lockMap.put("ISSUE_ID", observable.getTxtTokenIssueId());
//                }
//                setEditLockMap(lockMap);
//                setEditLock();
                settings();
            }
        }
            
   
    
    /* set the screen after the updation,insertion, deletion */
    private void settings(){
        observable.resetForm();
        setHelpBtnEnableDisable(false);
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panTokenIssue, false);
        setButtonEnableDisable();
        observable.setResultStatus();
    }
    
    /* Does necessary operaion when user clicks the save button */
    private void savePerformed(){
        updateOBFields();
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
    
    /* Method used to showPopup ViewAll by Executing a Query */
    private void callView(String currField) {
        viewType = currField;
        HashMap viewMap = new HashMap();
        if (currField.equals(ClientConstants.ACTION_STATUS[2])  ||
        currField.equals(ClientConstants.ACTION_STATUS[3])) {
            ArrayList lst = new ArrayList();
            lst.add("ISSUE_ID");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            viewMap.put(CommonConstants.MAP_NAME, "getSelectInstrumentIssue");
            HashMap where = new HashMap();
            where.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, where);
           } else{
            viewMap.put(CommonConstants.MAP_NAME, "getSelectUserMaster");
            HashMap where = new HashMap();
            where.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, where);
            where = null;
     
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
            viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) ) {
                HashMap where = new HashMap();
                where.put("ISSUE_ID", hash.get("ISSUE_ID"));
                where.put(CommonConstants.BRANCH_ID,TrueTransactMain.BRANCH_ID);
                hash.put(CommonConstants.MAP_WHERE, where);
                where = null;
                observable.populateData(hash);
//                fillTblTokenLost();
//                fillTxtNoOfTokens();
                if (observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE) {
                    ClientUtil.enableDisable(panTokenIssue, false);
                }else if(observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                    ClientUtil.enableDisable(panTokenIssue, true);
                }
                if(viewType.equals(AUTHORIZE)){
                    ClientUtil.enableDisable(panTokenIssue, false);
                }
                setButtonEnableDisable();
            }else{
                txtReceiverId.setText(CommonUtil.convertObjToStr(hash.get("USER_ID")));
            }
        }
    }
    
    /** Method used to fill up the TextFiled txtNoofTokens */
    private void fillTxtNoOfInstruments(){
        if(txtStartingInstrumentNo.getText().length() != 0 && txtEndingInstrumentNo.getText().length() !=0 ){
            int start = Integer.parseInt(txtStartingInstrumentNo.getText());
            int end = Integer.parseInt(txtEndingInstrumentNo.getText());
            int lostInstruments = 0;
            int totalInstruments = 0;
            if(end>start){
                totalInstruments = ((end - start) + 1) - lostInstruments;
            }else if(start>end){
                totalInstruments = ((start - end) + 1) - lostInstruments;
            }
            txtNoOfInstruments.setText(String.valueOf(totalInstruments));
        }
    }
    
    /** This method is used to check user is entering the same starting number or endingnumber of already issued tokens */
    private void tokenIssueCheck(){
//        try{
//            boolean exists = false;
//            String msg = "existingMsg";
//            if(txtStartingTokenNo.getText().length() != 0 && txtEndingTokenNo.getText().length() != 0){
//                HashMap map = new HashMap();
//                HashMap where = new HashMap();
//                where.put("BRANCH_ID",TrueTransactMain.BRANCH_ID);
//                if( observable.getCbmTokenType().getKeyForSelected().equals("METAL")){
//                    map.put(CommonConstants.MAP_NAME, "getSelectMetalTokenIssue");
//                    where.put("SERIES_NO", observable.getCbmSeriesNo().getKeyForSelected());
//                    where.put("TOKEN_TYPE", observable.getCbmTokenType().getKeyForSelected());
//                    where.put("CURRENT_DATE", currDt);
//                    System.out.println("Current Date "+ currDt);
//                    map.put(CommonConstants.MAP_WHERE, where);
//                    ArrayList list = observable.getResultList(map);
//                    if(list != null){
//                        exists = issueExists(list);
//                    }
//                }else if(observable.getCbmTokenType().getKeyForSelected().equals("PAPER")){
//                    map.put(CommonConstants.MAP_NAME, "getSelectPaperTokenIssue");
//                    where.put("TOKEN_TYPE", observable.getCbmTokenType().getKeyForSelected());
//                    where.put("CURRENT_DATE", currDt);
//                    map.put(CommonConstants.MAP_WHERE, where);
//                    ArrayList list = observable.getResultList(map);
//                    if(list != null){
//                        exists = issueExists(list);
//                        
//                    }
//                }
//                if(exists){
//                    showAlertWindow(msg);
//                    txtStartingTokenNo.requestFocus();
//                    txtStartingTokenNo.setText("");
//                    txtEndingTokenNo.setText("");
//                    txtNoOfTokens.setText("");
//                }
//            }
//        }catch(Exception e){
//            parseException.logException(e,true);
//        }
//        return;
    }
    
    /** Method  to check whether user is issuing the token which is configured in the TokenCofig UI */
    private void instrumentConfigCheck(){
        try{
            boolean exists = false;
            String msg = "nonExistingMsg";
            if(txtStartingInstrumentNo.getText().length() != 0 && txtEndingInstrumentNo.getText().length() != 0 ){
                HashMap map = new HashMap();
                HashMap where = new HashMap();
                where.put("BRANCH_ID",TrueTransactMain.BRANCH_ID);
                
                if(!cboSeriesNo.getSelectedItem().equals("")){
                    map.put(CommonConstants.MAP_NAME, "checkInventoryDetailsForInstrumentIssue");
                    where.put("SERIES_NO", observable.getCbmSeriesNo().getKeyForSelected());
                    map.put(CommonConstants.MAP_WHERE, where);
                    ArrayList list = observable.getResultList(map);
                    if(list != null){
                        if(list.size()!=0){
                            exists = configExists(list);
                        }
                    }
                }
                
                if(exists == false){
                    showAlertWindow(msg);
                    
                    
                    txtNoOfInstruments.setText("");
                }
            }
        }catch(Exception e){
            parseException.logException(e,true);
            e.printStackTrace();
        }
        return;
    }
    
    /** Checks for the duplication if therez any it will return true **/
    private boolean issueExists(ArrayList resultList){
        boolean rangeExists = false;
        try{
            HashMap resultMap = (HashMap) resultList.get(0);
            if(resultMap.get("TOKEN_END_NO") != null){
                int dbIssueEndingNo = Integer.parseInt(CommonUtil.convertObjToStr(resultMap.get("TOKEN_END_NO")));
                
                
                
                
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return rangeExists;
    }
    
    /** Checks whether user is issuing the configured tokens if its not configured token retuns false */
    private boolean configExists(ArrayList resultList){
        boolean rangeExists = false;
        HashMap resultMap = (HashMap) resultList.get(0);
       int  configStartingNo = Integer.parseInt(CommonUtil.convertObjToStr(resultMap.get("LEAVES_SLNO_FROM")));
        int configEndingNo = Integer.parseInt(CommonUtil.convertObjToStr(resultMap.get("LEAVES_SLNO_TO")));
        int issueStartNo = Integer.parseInt(txtStartingInstrumentNo.getText());
        int issueEndingNo = Integer.parseInt(txtEndingInstrumentNo.getText());
        if( (issueStartNo >= configStartingNo) && (issueStartNo <= configEndingNo) && (issueEndingNo <= configEndingNo) && (issueEndingNo >= configStartingNo) ){
            rangeExists = true;
        }
        return rangeExists;
    }
    
    
    /** This will show the alertwindow **/
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
            mapParam.put(CommonConstants.MAP_NAME, "getInstrumentIssueAuthorizeList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeInstrumentIssue");
            HashMap where = new HashMap();
            mapParam.put(CommonConstants.UPDATE_MAP_WHERE,where);
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
            observable.setStatus();
            
        } else if (viewType.equals(AUTHORIZE)){
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDt.clone());
            singleAuthorizeMap.put("ISSUE_ID", txtTokenIssueId.getText());
            singleAuthorizeMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            ClientUtil.execute("authorizeInstrumentIssue", singleAuthorizeMap);
            viewType = "";
            super.setOpenForEditBy(observable.getStatusBy());
            super.removeEditLock(txtTokenIssueId.getText());
            btnCancelActionPerformed(null);
            lblStatus.setText(authorizeStatus);
        }
    }
    
    /** Method to make HelpButton btnUserId enable or disable..accroding to Edit,Delete,New,Save Button Clicked */
    private void setHelpBtnEnableDisable(boolean flag){
        btnUserId.setEnabled(flag);
    }
    
    /** To set cboSeriesNO enable or disable according to the cbotokentype selected */
    private void setCboSeriesNoEnableDisable(){
        
        
        cboSeriesNo.setEnabled(true);
        
    }
    
    private void checkinstrumentNo(CTextField tokenNo){
        int number = Integer.parseInt(tokenNo.getText());
        if(number<=0){
            ClientUtil.showAlertWindow(resourceBundle.getString("tokenNoMsg"));
            tokenNo.setText("");
        }
    }
    
    private void fillTblTokenLost(){
        HashMap map = new HashMap();
        map.put(CommonConstants.MAP_NAME, "getSelectLostTokens");
        HashMap where = new HashMap();
        where.put("SERIES_NO", CommonUtil.convertObjToStr(observable.getCbmSeriesNo().getKeyForSelected()));
        where.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
        map.put(CommonConstants.MAP_WHERE, where);
        where = null;
        observable.setTbmTokenLost(observable.getResultList(map));
        tblTokenLost.setModel(observable.getTbmTokenLost());
    }
    
    
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panTokenIssue = new com.see.truetransact.uicomponent.CPanel();
        panTokenLost = new com.see.truetransact.uicomponent.CPanel();
        srpTokenLost = new com.see.truetransact.uicomponent.CScrollPane();
        tblTokenLost = new com.see.truetransact.uicomponent.CTable();
        panIssueFields = new com.see.truetransact.uicomponent.CPanel();
        txtTokenIssueId = new com.see.truetransact.uicomponent.CTextField();
        lblTokenIssueId = new com.see.truetransact.uicomponent.CLabel();
        lblSeriesNo = new com.see.truetransact.uicomponent.CLabel();
        cboSeriesNo = new com.see.truetransact.uicomponent.CComboBox();
        lblEndingInstrumentNo = new com.see.truetransact.uicomponent.CLabel();
        txtEndingInstrumentNo = new com.see.truetransact.uicomponent.CTextField();
        lblNoOfTokens = new com.see.truetransact.uicomponent.CLabel();
        txtNoOfInstruments = new com.see.truetransact.uicomponent.CTextField();
        lblReceiverId = new com.see.truetransact.uicomponent.CLabel();
        panReceiver = new com.see.truetransact.uicomponent.CPanel();
        txtReceiverId = new com.see.truetransact.uicomponent.CTextField();
        btnUserId = new com.see.truetransact.uicomponent.CButton();
        txtStartingInstrumentNo = new com.see.truetransact.uicomponent.CTextField();
        lblStartingInstrumentNo = new com.see.truetransact.uicomponent.CLabel();
        tbrTokenConfig = new com.see.truetransact.uicomponent.CToolBar();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace29 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace30 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lbSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace31 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace32 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace33 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace34 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
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
        setMinimumSize(new java.awt.Dimension(100, 26));
        setPreferredSize(new java.awt.Dimension(620, 320));

        panTokenIssue.setLayout(new java.awt.GridBagLayout());

        panTokenLost.setLayout(new java.awt.GridBagLayout());

        srpTokenLost.setMinimumSize(new java.awt.Dimension(300, 200));
        srpTokenLost.setPreferredSize(new java.awt.Dimension(800, 404));

        tblTokenLost.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Series No", "Token No.", "Token Status", "Lost Date"
            }
        ));
        srpTokenLost.setViewportView(tblTokenLost);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panTokenLost.add(srpTokenLost, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panTokenIssue.add(panTokenLost, gridBagConstraints);

        panIssueFields.setLayout(new java.awt.GridBagLayout());

        txtTokenIssueId.setEditable(false);
        txtTokenIssueId.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIssueFields.add(txtTokenIssueId, gridBagConstraints);

        lblTokenIssueId.setText("Instrument Issue Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIssueFields.add(lblTokenIssueId, gridBagConstraints);

        lblSeriesNo.setText("Series No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIssueFields.add(lblSeriesNo, gridBagConstraints);

        cboSeriesNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIssueFields.add(cboSeriesNo, gridBagConstraints);

        lblEndingInstrumentNo.setText("Ending Instrument No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIssueFields.add(lblEndingInstrumentNo, gridBagConstraints);

        txtEndingInstrumentNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtEndingInstrumentNo.setValidation(new NumericValidation());
        txtEndingInstrumentNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEndingInstrumentNoActionPerformed(evt);
            }
        });
        txtEndingInstrumentNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEndingInstrumentNoFocusLost(evt);
            }
        });
        txtEndingInstrumentNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtEndingInstrumentNoKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIssueFields.add(txtEndingInstrumentNo, gridBagConstraints);

        lblNoOfTokens.setText("No. Of Instruments Issued");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIssueFields.add(lblNoOfTokens, gridBagConstraints);

        txtNoOfInstruments.setEditable(false);
        txtNoOfInstruments.setMinimumSize(new java.awt.Dimension(100, 21));
        txtNoOfInstruments.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIssueFields.add(txtNoOfInstruments, gridBagConstraints);

        lblReceiverId.setText("Receiver's Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panIssueFields.add(lblReceiverId, gridBagConstraints);

        panReceiver.setLayout(new java.awt.GridBagLayout());

        txtReceiverId.setEditable(false);
        txtReceiverId.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panReceiver.add(txtReceiverId, gridBagConstraints);

        btnUserId.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnUserId.setPreferredSize(new java.awt.Dimension(21, 21));
        btnUserId.setEnabled(false);
        btnUserId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUserIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panReceiver.add(btnUserId, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panIssueFields.add(panReceiver, gridBagConstraints);

        txtStartingInstrumentNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtStartingInstrumentNo.setValidation(new NumericValidation());
        txtStartingInstrumentNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtStartingInstrumentNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIssueFields.add(txtStartingInstrumentNo, gridBagConstraints);

        lblStartingInstrumentNo.setText("Starting Instrument No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIssueFields.add(lblStartingInstrumentNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panTokenIssue.add(panIssueFields, gridBagConstraints);

        getContentPane().add(panTokenIssue, java.awt.BorderLayout.CENTER);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnNew);

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace29);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnEdit);

        lblSpace30.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace30.setText("     ");
        lblSpace30.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace30);

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

        lblSpace31.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace31.setText("     ");
        lblSpace31.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace31);

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

        lblSpace32.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace32.setText("     ");
        lblSpace32.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace32.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace32.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace32);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnException);

        lblSpace33.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace33.setText("     ");
        lblSpace33.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace33.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace33.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace33);

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

        lblSpace34.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace34.setText("     ");
        lblSpace34.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace34);

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

    private void txtStartingInstrumentNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtStartingInstrumentNoActionPerformed
        // TODO add your handling code here:
         if(txtStartingInstrumentNo.getText().length()!=0)
            checkinstrumentNo(txtStartingInstrumentNo);
    }//GEN-LAST:event_txtStartingInstrumentNoActionPerformed
        
    private void btnUserIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUserIdActionPerformed
        // TODO add your handling code here:
        callView("UserId");
    }//GEN-LAST:event_btnUserIdActionPerformed
        
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
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
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
        super.removeEditLock(txtTokenIssueId.getText());
        setModified(false);
        observable.resetForm();
        setHelpBtnEnableDisable(false);
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panTokenIssue, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        setButtonEnableDisable();
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
    }//GEN-LAST:event_btnCancelActionPerformed
        
    private void txtEndingInstrumentNoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEndingInstrumentNoKeyReleased
        // TODO add your handling code here:
       fillTxtNoOfInstruments();
    }//GEN-LAST:event_txtEndingInstrumentNoKeyReleased
    
    private void txtEndingInstrumentNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEndingInstrumentNoFocusLost
        // TODO add your handling code here:
        instrumentConfigCheck();
      fillTxtNoOfInstruments();
    }//GEN-LAST:event_txtEndingInstrumentNoFocusLost
    
    private void txtEndingInstrumentNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEndingInstrumentNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEndingInstrumentNoActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        setHelpBtnEnableDisable(false);
        callView( ClientConstants.ACTION_STATUS[3]);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        setHelpBtnEnableDisable(true);
        callView(ClientConstants.ACTION_STATUS[2]);
        setCboSeriesNoEnableDisable();
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
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
        setModified(true);
        observable.resetForm();
        setHelpBtnEnableDisable(true);
        ClientUtil.enableDisable(panTokenIssue, true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        
    }//GEN-LAST:event_btnNewActionPerformed
    
    
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
    private com.see.truetransact.uicomponent.CButton btnUserId;
    private com.see.truetransact.uicomponent.CComboBox cboSeriesNo;
    private com.see.truetransact.uicomponent.CLabel lbSpace2;
    private com.see.truetransact.uicomponent.CLabel lblEndingInstrumentNo;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNoOfTokens;
    private com.see.truetransact.uicomponent.CLabel lblReceiverId;
    private com.see.truetransact.uicomponent.CLabel lblSeriesNo;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace29;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace30;
    private com.see.truetransact.uicomponent.CLabel lblSpace31;
    private com.see.truetransact.uicomponent.CLabel lblSpace32;
    private com.see.truetransact.uicomponent.CLabel lblSpace33;
    private com.see.truetransact.uicomponent.CLabel lblSpace34;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStartingInstrumentNo;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTokenIssueId;
    private com.see.truetransact.uicomponent.CMenuBar mbrTokenConfig;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panIssueFields;
    private com.see.truetransact.uicomponent.CPanel panReceiver;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTokenIssue;
    private com.see.truetransact.uicomponent.CPanel panTokenLost;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptView;
    private com.see.truetransact.uicomponent.CScrollPane srpTokenLost;
    private com.see.truetransact.uicomponent.CTable tblTokenLost;
    private com.see.truetransact.uicomponent.CToolBar tbrTokenConfig;
    private com.see.truetransact.uicomponent.CTextField txtEndingInstrumentNo;
    private com.see.truetransact.uicomponent.CTextField txtNoOfInstruments;
    private com.see.truetransact.uicomponent.CTextField txtReceiverId;
    private com.see.truetransact.uicomponent.CTextField txtStartingInstrumentNo;
    private com.see.truetransact.uicomponent.CTextField txtTokenIssueId;
    // End of variables declaration//GEN-END:variables
    
}
