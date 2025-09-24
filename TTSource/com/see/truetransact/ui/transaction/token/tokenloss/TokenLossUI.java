/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * TokenLossUI.java
 *
 * Created on January 25, 2005, 4:06 PM
 */

package com.see.truetransact.ui.transaction.token.tokenloss;

/**
 *
 * @author  ashokvijayakumar
 */
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.uicomponent.COptionPane;
import java.util.ResourceBundle;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uivalidation.ToDateValidation;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.uivalidation.ToDateValidation;
import java.util.Observer;
import java.util.Observable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Date;

public class TokenLossUI extends CInternalFrame implements Observer,UIMandatoryField{
    
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("com.see.truetransact.ui.transaction.token.tokenloss.TokenLossRB", ProxyParameters.LANGUAGE);
    private HashMap mandatoryMap;
    private TokenLossMRB objMandatoryRB = new TokenLossMRB();
    private TokenLossOB observable;
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private String viewType = "";//variable used to check...whether the actionType is edit or delete and accordingly making fields enable or disable
    private final String AUTHORIZE ="Authorize";//final varaible...used to store viewtype as Authorize..when Authorize button is clicked
    
    /** Creates new form TokenLossUI */
    public TokenLossUI() {
        initForm();
    }
    
    /** Initialise the Form TokenLossUI **/
    private void initForm(){
        initComponents();
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setHelpMessage();
        setMaxLengths();
        setObservable();
        initComponentData();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panTokenLoss);
        observable.resetForm();
        ClientUtil.enableDisable(panTokenLoss, false);
        makePanelVisible(false);
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
        cboTokenType.setName("cboTokenType");
        chkTokenRecovered.setName("chkTokenRecovered");
        lbSpace2.setName("lbSpace2");
        lblDateOfLoss.setName("lblDateOfLoss");
        lblMsg.setName("lblMsg");
        lblRecoveredDate.setName("lblRecoveredDate");
        lblRemarks.setName("lblRemarks");
        lblSeriesNo.setName("lblSeriesNo");
        lblSpace1.setName("lblSpace1");
        lblSpace3.setName("lblSpace3");
        lblSpace5.setName("lblSpace5");
        lblStatus.setName("lblStatus");
        lblTokenRecovered.setName("lblTokenRecovered");
        lblTokenType.setName("lblTokenType");
        mbrTokenLoss.setName("mbrTokenLoss");
        panStatus.setName("panStatus");
        panTokenLoss.setName("panTokenLoss");
        panTokenRecovery.setName("panTokenRecovery");
        tdtDateOfLoss.setName("tdtDateOfLoss");
        tdtRecoveredDate.setName("tdtRecoveredDate");
        txtRemarks.setName("txtRemarks");
        cboSeriesNo.setName("cboSeriesNo");
        lblTokenLossId.setName("lblTokenLossId");
        txtTokenLossId.setName("txtTokenLossId");
        lblTokenNo.setName("lblTokenNo");
        txtTokenNo.setName("txtTokenNo");
    }
    
/* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        lblSpace5.setText(resourceBundle.getString("lblSpace5"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblTokenRecovered.setText(resourceBundle.getString("lblTokenRecovered"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblDateOfLoss.setText(resourceBundle.getString("lblDateOfLoss"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        lblSeriesNo.setText(resourceBundle.getString("lblSeriesNo"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        lblRecoveredDate.setText(resourceBundle.getString("lblRecoveredDate"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        chkTokenRecovered.setText(resourceBundle.getString("chkTokenRecovered"));
        lblTokenType.setText(resourceBundle.getString("lblTokenType"));
        lbSpace2.setText(resourceBundle.getString("lbSpace2"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblRemarks.setText(resourceBundle.getString("lblRemarks"));
    }
    
    /* Auto Generated Method - setMandatoryHashMap()
     
ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
     
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtTokenLossId", new Boolean(false));
        mandatoryMap.put("txtTokenNo", new Boolean(true));
        mandatoryMap.put("cboTokenType", new Boolean(true));
        mandatoryMap.put("tdtDateOfLoss", new Boolean(true));
        mandatoryMap.put("txtRemarks", new Boolean(true));
        mandatoryMap.put("cboSeriesNo", new Boolean(true));
        mandatoryMap.put("chkTokenRecovered", new Boolean(true));
        mandatoryMap.put("tdtRecoveredDate", new Boolean(true));
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
        cboTokenType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboTokenType"));
        tdtDateOfLoss.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtDateOfLoss"));
        txtRemarks.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRemarks"));
        cboSeriesNo.setHelpMessage(lblMsg, objMandatoryRB.getString("cboSeriesNo"));
        chkTokenRecovered.setHelpMessage(lblMsg, objMandatoryRB.getString("chkTokenRecovered"));
        tdtRecoveredDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtRecoveredDate"));
        txtTokenLossId.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTokenLossId"));
        txtTokenNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTokenNo"));
    }
    
    /** Sets the maximum possible length to the TextFields in the UI **/
    private void setMaxLengths(){
        txtRemarks.setMaxLength(256);
        txtTokenNo.setMaxLength(16);
        txtTokenNo.setAllowNumber(true);
    }
    
    /** Sets the SelectedItem for cboTokenType as "Metal Type" **/
    private void setDefaultTokenType(){
        cboTokenType.setSelectedItem(observable.getCbmTokenType().getDataForKey("METAL"));
        cboTokenType.setEnabled(false);
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
    /* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        cboTokenType.setSelectedItem(observable.getCboTokenType());
        tdtDateOfLoss.setDateValue(observable.getTdtDateOfLoss());
        txtRemarks.setText(observable.getTxtRemarks());
        cboSeriesNo.setSelectedItem(observable.getCboSeriesNo());
        chkTokenRecovered.setSelected(observable.getChkTokenRecovered());
        tdtRecoveredDate.setDateValue(observable.getTdtRecoveredDate());
        txtTokenNo.setText(observable.getTxtTokenNo());
        txtTokenLossId.setText(observable.getTxtTokenLostId());
        lblStatus.setText(observable.getLblStatus());
    }
    
    
    /** Adds up the Observable to this form */
    private void setObservable() {
        try{
            observable = TokenLossOB.getInstance();
            observable.addObserver(this);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /** Auto Generated Method - updateOBFields()
     * This method called by Save option of UI.
     * It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setCboTokenType((String) cboTokenType.getSelectedItem());
        observable.setTdtDateOfLoss(tdtDateOfLoss.getDateValue());
        observable.setTxtRemarks(txtRemarks.getText());
        observable.setCboSeriesNo((String)cboSeriesNo.getSelectedItem());
        observable.setChkTokenRecovered(chkTokenRecovered.isSelected());
        observable.setTxtTokenNo(txtTokenNo.getText());
        observable.setTdtRecoveredDate(tdtRecoveredDate.getDateValue());
        observable.setModule(getModule());
        observable.setScreen(getScreen());
        observable.setTokenStatus(CommonConstants.STOPPED);
    }
    
    /*Setting model to the combobox cboTokenType  */
    private void initComponentData() {
        try{
            cboTokenType.setModel(observable.getCbmTokenType());
            cboSeriesNo.setModel(observable.getCbmSeriesNo());
        }catch(ClassCastException e){
            parseException.logException(e,true);
        }
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
        final String mandatoryMessage = checkMandatory(panTokenLoss);
        StringBuffer message = new StringBuffer(mandatoryMessage);
        if(chkTokenRecovered.isSelected()){
            if(tdtRecoveredDate.getDateValue().equals("")){
                message.append(objMandatoryRB.getString("tdtRecoveredDate"));
            }
        }
        if(observable.getActionType()!= ClientConstants.ACTIONTYPE_DELETE && message.length() > 0 ){
            displayAlert(message.toString());
        }else{
            observable.execute(status);
            if(observable.getResult()!= ClientConstants.ACTIONTYPE_FAILED){
                HashMap lockMap = new HashMap();
                ArrayList lst = new ArrayList();
                lst.add("TOKEN_LOST_ID");
                lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
                if (observable.getProxyReturnMap()!=null) {
                    if (observable.getProxyReturnMap().containsKey("TOKEN_LOST_ID")) {
                        lockMap.put("TOKEN_LOST_ID", observable.getProxyReturnMap().get("TOKEN_LOST_ID"));
                    }
                }
                if (status==CommonConstants.TOSTATUS_UPDATE) {
                    lockMap.put("TOKEN_LOST_ID", observable.getTxtTokenLostId());
                }
                setEditLockMap(lockMap);
                setEditLock();

                settings();
            }
        }
    }
    
    /* set the screen after the updation,insertion, deletion */
    private void settings(){
        observable.resetForm();
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panTokenLoss, false);
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
        if (currField.equals(ClientConstants.ACTION_STATUS[2]) || currField.equals(ClientConstants.ACTION_STATUS[17])) {
            ArrayList lst = new ArrayList();
            lst.add("TOKEN_LOST_ID");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            viewMap.put(CommonConstants.MAP_NAME, "getSelectTokenLoss");
            HashMap where = new HashMap();
            where.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, where);
            where = null;
        }
        else{
            viewMap.put(CommonConstants.MAP_NAME, "getSelectTokenLossForEdit");
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
            viewType.equals(ClientConstants.ACTION_STATUS[3])|| viewType.equals(AUTHORIZE) || viewType.equals(ClientConstants.ACTION_STATUS[17]) ) {
                HashMap where = new HashMap();
                where.put("TOKEN_LOST_ID", hash.get("TOKEN_LOST_ID"));
                where.put(CommonConstants.BRANCH_ID,TrueTransactMain.BRANCH_ID);
                hash.put(CommonConstants.MAP_WHERE, where);
                where = null;
                observable.populateData(this,hash);
                if (observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE ) {
                    ClientUtil.enableDisable(panTokenLoss, false);
                }
                if(observable.getActionType()!= ClientConstants.ACTIONTYPE_DELETE && !panTokenRecovery.isVisible()){
                    ClientUtil.enableDisable(panTokenLoss, true);
                }
                if(viewType.equals(AUTHORIZE) || observable.getActionType()==ClientConstants.ACTIONTYPE_VIEW){
                ClientUtil.enableDisable(panTokenLoss, false);
                  if(hash.get("TOKEN_STATUS").equals("REVOKED")){
                      panTokenRecovery.setVisible(true);
                      chkTokenRecovered.setVisible(false);
                      lblTokenRecovered.setVisible(false);
                      tdtRecoveredDate.setDateValue(CommonUtil.convertObjToStr(hash.get("RECOVER_DT")));
                      tdtRecoveredDate.setVisible(true);
                      tdtRecoveredDate.setEnabled(false);
                  }
                }
                setButtonEnableDisable();
                if(viewType.equals(AUTHORIZE)){
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                }
            }
        }
    }
    
    /** Method used to do Required operation when user clicks btnAuthorize,btnReject or btnReject **/
    public void authorizeStatus(String authorizeStatus) {
        if (!viewType.equals(AUTHORIZE)){
            viewType = AUTHORIZE;
            setModified(true);
            HashMap mapParam = new HashMap();
            mapParam.put(CommonConstants.MAP_NAME, "getTokenLossAuthorizeList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeTokenLoss");
            HashMap where = new HashMap();
            //             where.put("TOKEN_STATUS", CommonConstants.STOPPED);
            where.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            where.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            mapParam.put(CommonConstants.UPDATE_MAP_WHERE, where);
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
            //            singleAuthorizeMap.put("TOKEN_STATUS", CommonConstants.STOPPED);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, ClientUtil.getCurrentDate());
            singleAuthorizeMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("TOKEN_LOST_ID", txtTokenLossId.getText());
            singleAuthorizeMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            ClientUtil.execute("authorizeTokenLoss", singleAuthorizeMap);
            viewType = "";
            super.setOpenForEditBy(observable.getStatusBy());
            super.removeEditLock(txtTokenLossId.getText());
            
            btnCancelActionPerformed(null);
            lblStatus.setText(authorizeStatus);
        }
    }
    
    /** Making the Fields enable or disable accoridingly whether the data viewed is authorized or not **/
    public void setEnableDisable(boolean flag){
        tdtDateOfLoss.setEnabled(flag);
        cboSeriesNo.setEditable(flag);
        txtTokenNo.setEditable(flag);
        txtRemarks.setEditable(flag);
    }
    
    /** Making the panRecovery visible or invisible. accordingly wheher the data is authorized or not **/
    public void makePanelVisible(boolean flag){
        panTokenRecovery.setVisible(flag);
        chkTokenRecovered.setEnabled(flag);
        tdtRecoveredDate.setEnabled(flag);
        lblRecoveredDate.setVisible(false);
        tdtRecoveredDate.setVisible(false);
    }
    
    /** Displays the alertwindow to the user **/
    private int showAlertWindow(String alertMsg) throws Exception {
        int optionSelected = 1;
        String[] options = {resourceBundle.getString("cDialogOK")};
        optionSelected = COptionPane.showOptionDialog(null,resourceBundle.getString(alertMsg), CommonConstants.INFORMATIONTITLE,
        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
        null, options, options[0]);
        return optionSelected;
    }
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
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panTokenLoss = new com.see.truetransact.uicomponent.CPanel();
        lblTokenType = new com.see.truetransact.uicomponent.CLabel();
        cboTokenType = new com.see.truetransact.uicomponent.CComboBox();
        lblDateOfLoss = new com.see.truetransact.uicomponent.CLabel();
        tdtDateOfLoss = new com.see.truetransact.uicomponent.CDateField();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        txtRemarks = new com.see.truetransact.uicomponent.CTextField();
        lblSeriesNo = new com.see.truetransact.uicomponent.CLabel();
        panTokenRecovery = new com.see.truetransact.uicomponent.CPanel();
        chkTokenRecovered = new com.see.truetransact.uicomponent.CCheckBox();
        lblRecoveredDate = new com.see.truetransact.uicomponent.CLabel();
        tdtRecoveredDate = new com.see.truetransact.uicomponent.CDateField();
        lblTokenRecovered = new com.see.truetransact.uicomponent.CLabel();
        cboSeriesNo = new com.see.truetransact.uicomponent.CComboBox();
        lblTokenLossId = new com.see.truetransact.uicomponent.CLabel();
        txtTokenLossId = new com.see.truetransact.uicomponent.CTextField();
        lblTokenNo = new com.see.truetransact.uicomponent.CLabel();
        txtTokenNo = new com.see.truetransact.uicomponent.CTextField();
        tbrTokenLoss = new com.see.truetransact.uicomponent.CToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace6 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace26 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace27 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lbSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace28 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace29 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace30 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace31 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        mbrTokenLoss = new com.see.truetransact.uicomponent.CMenuBar();
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
        setMinimumSize(new java.awt.Dimension(780, 520));
        setPreferredSize(new java.awt.Dimension(780, 520));

        panTokenLoss.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panTokenLoss.setLayout(new java.awt.GridBagLayout());

        lblTokenType.setText("Token Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTokenLoss.add(lblTokenType, gridBagConstraints);

        cboTokenType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboTokenType.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTokenLoss.add(cboTokenType, gridBagConstraints);

        lblDateOfLoss.setText("Date of Loss");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTokenLoss.add(lblDateOfLoss, gridBagConstraints);

        tdtDateOfLoss.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtDateOfLossFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTokenLoss.add(tdtDateOfLoss, gridBagConstraints);

        lblRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTokenLoss.add(lblRemarks, gridBagConstraints);

        txtRemarks.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTokenLoss.add(txtRemarks, gridBagConstraints);

        lblSeriesNo.setText("Series No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTokenLoss.add(lblSeriesNo, gridBagConstraints);

        panTokenRecovery.setBorder(javax.swing.BorderFactory.createTitledBorder("Token Recovery"));
        panTokenRecovery.setLayout(new java.awt.GridBagLayout());

        chkTokenRecovered.setVerifyInputWhenFocusTarget(false);
        chkTokenRecovered.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkTokenRecoveredActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panTokenRecovery.add(chkTokenRecovered, gridBagConstraints);

        lblRecoveredDate.setText("Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panTokenRecovery.add(lblRecoveredDate, gridBagConstraints);

        tdtRecoveredDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtRecoveredDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panTokenRecovery.add(tdtRecoveredDate, gridBagConstraints);

        lblTokenRecovered.setText("Recovered");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panTokenRecovery.add(lblTokenRecovered, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 16, 0, 0);
        panTokenLoss.add(panTokenRecovery, gridBagConstraints);

        cboSeriesNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTokenLoss.add(cboSeriesNo, gridBagConstraints);

        lblTokenLossId.setText("Token Loss Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTokenLoss.add(lblTokenLossId, gridBagConstraints);

        txtTokenLossId.setEditable(false);
        txtTokenLossId.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTokenLoss.add(txtTokenLossId, gridBagConstraints);

        lblTokenNo.setText("Token No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTokenLoss.add(lblTokenNo, gridBagConstraints);

        txtTokenNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTokenNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTokenNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTokenLoss.add(txtTokenNo, gridBagConstraints);

        getContentPane().add(panTokenLoss, java.awt.BorderLayout.CENTER);

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
        tbrTokenLoss.add(btnView);

        lblSpace6.setText("     ");
        tbrTokenLoss.add(lblSpace6);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrTokenLoss.add(btnNew);

        lblSpace26.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace26.setText("     ");
        lblSpace26.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenLoss.add(lblSpace26);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrTokenLoss.add(btnEdit);

        lblSpace27.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace27.setText("     ");
        lblSpace27.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenLoss.add(lblSpace27);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrTokenLoss.add(btnDelete);

        lbSpace2.setText("     ");
        tbrTokenLoss.add(lbSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrTokenLoss.add(btnSave);

        lblSpace28.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace28.setText("     ");
        lblSpace28.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenLoss.add(lblSpace28);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrTokenLoss.add(btnCancel);

        lblSpace3.setText("     ");
        tbrTokenLoss.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrTokenLoss.add(btnAuthorize);

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenLoss.add(lblSpace29);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrTokenLoss.add(btnException);

        lblSpace30.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace30.setText("     ");
        lblSpace30.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenLoss.add(lblSpace30);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrTokenLoss.add(btnReject);

        lblSpace5.setText("     ");
        tbrTokenLoss.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrTokenLoss.add(btnPrint);

        lblSpace31.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace31.setText("     ");
        lblSpace31.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenLoss.add(lblSpace31);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrTokenLoss.add(btnClose);

        getContentPane().add(tbrTokenLoss, java.awt.BorderLayout.NORTH);

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

        mbrTokenLoss.add(mnuProcess);

        setJMenuBar(mbrTokenLoss);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        callView(ClientConstants.ACTION_STATUS[17]);
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed

    private void tdtRecoveredDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtRecoveredDateFocusLost
        // TODO add your handling code here:
         boolean retDate = false;
         ClientUtil.validateLTDate(tdtRecoveredDate);
         String availDate =  tdtRecoveredDate.getDateValue();
        Date dtAv = DateUtil.getDateMMDDYYYY(availDate);
        String date = tdtDateOfLoss.getDateValue();
        Date createdDt = DateUtil.getDateMMDDYYYY(date);
        Date dt = ClientUtil.getCurrentDate();
        
        Date TodaysDt = dtAv;
  if(((TodaysDt.after(createdDt) || TodaysDt.compareTo(createdDt) == 0) && (TodaysDt.before((dt))) || TodaysDt.compareTo(dt) == 0)) { 
            retDate = true;
        } 
          if(retDate == false)
          {
            ClientUtil.showMessageWindow("Recovered Date Should Be Between Date Of Loss And Future date!!!!!!");
                tdtRecoveredDate.setDateValue("");      
          }
    }//GEN-LAST:event_tdtRecoveredDateFocusLost

    private void tdtDateOfLossFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtDateOfLossFocusLost
        // TODO add your handling code here:
        String s= tdtDateOfLoss.getDateValue();
        observable.setTdtDateOfLoss(s);
         ClientUtil.validateLTDate(tdtDateOfLoss);
    }//GEN-LAST:event_tdtDateOfLossFocusLost
    
    private void txtTokenNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTokenNoFocusLost
        // TODO add your handling code here:
        try{
            final String msg = "existingMsg";
            if( observable.getActionType() == ClientConstants.ACTIONTYPE_NEW && !(cboSeriesNo.getSelectedItem().equals("")) && !(txtTokenNo.getText().equals("")) ){
                boolean exists = observable.isTokenNoExists(CommonUtil.convertObjToStr(observable.getCbmSeriesNo().getKeyForSelected()), txtTokenNo.getText());
                if(exists){
                    showAlertWindow(msg);
                    txtTokenNo.requestFocus();
                    txtTokenNo.setText("");
                }
            }
            if(!(cboSeriesNo.getSelectedItem().equals("")) && !(txtTokenNo.getText().equals(""))){
                boolean issued = observable.isTokenIssued(CommonUtil.convertObjToStr(cboSeriesNo.getSelectedItem()), txtTokenNo.getText());
                 if(!issued){
                    ClientUtil.showAlertWindow(resourceBundle.getString("issuedMsg"));
                    txtTokenNo.requestFocus();
                    txtTokenNo.setText("");
                }
            }
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }//GEN-LAST:event_txtTokenNoFocusLost
    
    private void chkTokenRecoveredActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkTokenRecoveredActionPerformed
        // TODO add your handling code here:
        if(chkTokenRecovered.isSelected()){
            tdtRecoveredDate.setVisible(true);
            lblRecoveredDate.setVisible(true);
        }else{
            tdtRecoveredDate.setVisible(false);
            lblRecoveredDate.setVisible(false);
        }
    }//GEN-LAST:event_chkTokenRecoveredActionPerformed
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // TODO add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_mitCloseActionPerformed
    
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
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        callView(ClientConstants.ACTION_STATUS[2]);

        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
       

        
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // TODO add your handling code here:
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        panTokenRecovery.setVisible(false);
        if(observable.getAuthorizeStatus()!=null)
        super.removeEditLock(txtTokenLossId.getText());
        setModified(false);
        observable.resetForm();
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panTokenLoss, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        setButtonEnableDisable();
        viewType = "";
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);

        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        btnView.setEnabled(true);

    }//GEN-LAST:event_btnCancelActionPerformed
    
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
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        callView( ClientConstants.ACTION_STATUS[3]);

        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);

        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);

    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        setModified(false);
        savePerformed();
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        setModified(true);
        observable.resetForm();
        ClientUtil.enableDisable(panTokenLoss, true);
        txtTokenNo.setEditable(true);
        setDefaultTokenType();
        makePanelVisible(false);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();

        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);

        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
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
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboSeriesNo;
    private com.see.truetransact.uicomponent.CComboBox cboTokenType;
    private com.see.truetransact.uicomponent.CCheckBox chkTokenRecovered;
    private com.see.truetransact.uicomponent.CLabel lbSpace2;
    private com.see.truetransact.uicomponent.CLabel lblDateOfLoss;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblRecoveredDate;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
    private com.see.truetransact.uicomponent.CLabel lblSeriesNo;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace26;
    private com.see.truetransact.uicomponent.CLabel lblSpace27;
    private com.see.truetransact.uicomponent.CLabel lblSpace28;
    private com.see.truetransact.uicomponent.CLabel lblSpace29;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace30;
    private com.see.truetransact.uicomponent.CLabel lblSpace31;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace6;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTokenLossId;
    private com.see.truetransact.uicomponent.CLabel lblTokenNo;
    private com.see.truetransact.uicomponent.CLabel lblTokenRecovered;
    private com.see.truetransact.uicomponent.CLabel lblTokenType;
    private com.see.truetransact.uicomponent.CMenuBar mbrTokenLoss;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTokenLoss;
    private com.see.truetransact.uicomponent.CPanel panTokenRecovery;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptView;
    private com.see.truetransact.uicomponent.CToolBar tbrTokenLoss;
    private com.see.truetransact.uicomponent.CDateField tdtDateOfLoss;
    private com.see.truetransact.uicomponent.CDateField tdtRecoveredDate;
    private com.see.truetransact.uicomponent.CTextField txtRemarks;
    private com.see.truetransact.uicomponent.CTextField txtTokenLossId;
    private com.see.truetransact.uicomponent.CTextField txtTokenNo;
    // End of variables declaration//GEN-END:variables
    
}
