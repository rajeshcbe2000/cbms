/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * FixedAssetsDescriptionUI.java
 *
 * Created on January 20, 2005, 3:03 PM
 */

package com.see.truetransact.ui.sysadmin.fixedassets;

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
import com.see.truetransact.uivalidation.DefaultValidation;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.ui.common.authorize.AuthorizeUI ;

import java.util.Observable;
import java.util.Observer;
import java.util.HashMap;
import java.util.ArrayList;
import com.see.truetransact.commonutil.DateUtil;
import java.util.Date;

public class FixedAssetsDescriptionUI extends CInternalFrame implements Observer, UIMandatoryField{
    
    /** Vairable Declarations */
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("com.see.truetransact.ui.sysadmin.emptraining.EmpTrainingRB", ProxyParameters.LANGUAGE);//Creating Instance For ResourceBundle-TokenConfigRB
    private FixedAssetsDescriptionOB observable; //Reference for the Observable Class TokenConfigOB
    private FixedAssetsDescriptionMRB objMandatoryRB = new FixedAssetsDescriptionMRB();//Instance for the MandatoryResourceBundle
    private final static ClientParseException parseException = ClientParseException.getInstance();//Instance of ClientParseException to log the Exceptions
    private HashMap mandatoryMap;//Map for putting up MandatoryFields in the UI
    private String viewType = "";//Variable used to store ActionType(New,Edit,Delete)
    private final String AUTHORIZE="Authorize";//Variable used when btnAuthorize is clicked
    int updateTab=-1;
    boolean isFilled = false;
    private boolean updateMode = false;
    private Date currDt = null;
    /** Creates new form TokenConfigUI */
    public FixedAssetsDescriptionUI() {
        initForm();
    }
    
    /** Method which is used to initialize the form TokenConfig */
    private void initForm(){
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        setFieldNames();
        observable = new FixedAssetsDescriptionOB();
        observable.addObserver(this);
        observable.resetForm();
        initComponentData();
        setMandatoryHashMap();
        setHelpMessage();
        setMaxLengths();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panIndAssetDetails);
        ClientUtil.enableDisable(panIndAssetDetails, false);
        setButtonEnableDisable();
        enableDisablePanButton(false);
        enableDisablePanEmpDetails(false);
        txtAssetDescID.setEnabled(false);
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
        cboAssetType.setName("cboAssetType");
        lbSpace2.setName("lbSpace2");
        txtAssetSubType.setName("txtAssetSubType");
        lblMsg.setName("lblMsg");
        lblSpace1.setName("lblSpace1");
        lblSpace3.setName("lblSpace3");
        lblSpace5.setName("lblSpace5");
        lblStatus.setName("lblStatus");
        mbrTokenConfig.setName("mbrTokenConfig");
        panStatus.setName("panStatus");
        panEmpBtn.setName("panEmpBtn");
        btnEmpDelete.setName("btnEmpDelete");
        btnEmpNew.setName("btnEmpNew");
        btnEmpSave.setName("btnEmpSave");
        txtAssetDescID.setText("txtAssetDescID");
        lblAssetType.setName("lblAssetType");
        lblAssetSubType.setName("lblAssetSubType");
    }
    
 /* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        lblFaDescID.setText(resourceBundle.getString("lblFaDescID"));
        lblAssetType.setText(resourceBundle.getString("lblAssetType"));
        lblAssetSubType.setText(resourceBundle.getString("lblAssetSubType"));
    }
    
    
    /*Setting model to the combobox cboTokenType  */
    private void initComponentData() {
        try{
            cboAssetType.setModel(observable.getCbmAssetType());
        }catch(ClassCastException e){
            parseException.logException(e,true);
        }
    }
    
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        
        tblAssetList.setModel(observable.getTblAssetDetails());
        cboAssetType.setModel(observable.getCbmAssetType());
        txtAssetSubType.setText(observable.getAssetSubType());
        txtAssetDescID.setText(observable.getAssetDescID());
    }
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setAssetDescID(txtAssetDescID.getText());
        observable.setCboAssetType((String) cboAssetType.getSelectedItem());
        observable.setAssetType((String) cboAssetType.getSelectedItem());
        observable.setAssetSubType(txtAssetSubType.getText());
    }
    
/* Auto Generated Method - setMandatoryHashMap()
 
ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
 
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboAssetType", new Boolean(true));
        mandatoryMap.put("txtAssetSubType", new Boolean(true));
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
        cboAssetType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboAssetType"));
        txtAssetSubType.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAssetSubType"));
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
    private void saveAction(){
        //To check mandtoryness of the Terminal panAcHdDetails,panAcHeadDetails panel and diplay appropriate
        //error message, else proceed
        final String mandatoryMessage = checkMandatory(panAssetDescription);
        StringBuffer message = new StringBuffer(mandatoryMessage);
        if(message.length() > 0 ){
            displayAlert(message.toString());
        }else{
            observable.doAction();
            if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED){
                HashMap lockMap = new HashMap();
                ArrayList lst = new ArrayList();
                lst.add("EMP_TRAINING_ID"); 
                lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
                if (observable.getProxyReturnMap()!=null) {
                    if (observable.getProxyReturnMap().containsKey("EMP_TRAINING_ID")) {
                        lockMap.put("EMP_TRAINING_ID", observable.getProxyReturnMap().get("EMP_TRAINING_ID"));
                    }
                }
                if(observable.getResult()==ClientConstants.ACTIONTYPE_EDIT){
                    lockMap.put("EMP_TRAINING_ID", observable.getAssetType());
                }
                setEditLockMap(lockMap);
                setEditLock();
                deletescreenLock();
                settings();
            }
        }
    }
    
    private void deletescreenLock(){
        HashMap map=new HashMap();
        map.put("USER_ID",ProxyParameters.USER_ID);
        map.put("TRANS_DT", currDt.clone());
        map.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
        ClientUtil.execute("DELETE_SCREEN_LOCK", map);
    }
    
    /* set the screen after the updation,insertion, deletion */
    private void settings(){
        observable.resetForm();
        setHelpBtnEnableDisable(false);
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panAssetDescription, false);
        setButtonEnableDisable();
        observable.setResultStatus();
        lblStatus.setText(observable.getLblStatus());
    }
    
    /* Method used to showPopup ViewAll by Executing a Query */
    private void callView(String currAction) {
        viewType = currAction;
        HashMap viewMap = new HashMap();
        if (currAction.equalsIgnoreCase("Edit")){
            HashMap map = new HashMap();
            map.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
            map.put("CURR_DT",currDt.clone());
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getFixedAssetsDescDetailsEdit");
        }else if(currAction.equalsIgnoreCase("Delete")){
            HashMap map = new HashMap();
            map.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getFixedAssetsDescDetailsDelete");
        }
        else if(currAction.equalsIgnoreCase("Enquiry")){
            HashMap map = new HashMap();
            map.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getFixedAssetsDescDetailsView");
        }
        new ViewAll(this,viewMap).show();
    }
    
    /* Fills up the HashMap with data when user selects the row in ViewAll screen  */
    public void fillData(Object  map) {
        try {
            isFilled = true;
            setModified(true);
            HashMap hash = (HashMap) map;
            if (viewType != null) {
                if (viewType.equals(ClientConstants.ACTION_STATUS[2]) ||
                viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) ||  viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                    HashMap where = new HashMap();
                    where.put("FA_DESC_ID", hash.get("FA_DESC_ID"));
                    where.put(CommonConstants.BRANCH_ID,TrueTransactMain.BRANCH_ID);
                    hash.put("VIEW_TYPE",viewType);
                    hash.put(CommonConstants.MAP_WHERE, where);
                    where = null;
                    observable.getData(hash);
                    if (observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE || viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                        ClientUtil.enableDisable(panAssetDescription, false);
                        enableDisablePanButton(false);
                        enableDisablePanEmpDetails(false);
                    }else if(observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                        ClientUtil.enableDisable(panAssetDescription, true);
                        enableDisablePanButton(true);
                        enableDisablePanEmpDetails(true);
                    }
                    setButtonEnableDisable();
                    if(viewType.equals(AUTHORIZE) ){
                        ClientUtil.enableDisable(panAssetDescription, false);
                        btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                        btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                        btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                    }
                }
            }
            if(viewType.equals(ClientConstants.ACTION_STATUS[2]) || viewType.equals(AUTHORIZE)) {
                HashMap screenMap = new HashMap();
                screenMap.put("TRANS_ID",hash.get("FA_DESC_ID"));
                screenMap.put("USER_ID",ProxyParameters.USER_ID);
                screenMap.put("TRANS_DT", currDt.clone());
                screenMap.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
                java.util.List lst=ClientUtil.executeQuery("selectauthorizationLock", screenMap);
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
                        ClientUtil.enableDisable(panAssetDescription,false);
                        enableDisablePanButton(false);
                        enableDisablePanEmpDetails(false);
                    }
                    ClientUtil.showMessageWindow("already open by"+open);
                    return;
                }
                else{
                    hash.put("TRANS_ID",hash.get("FA_DESC_ID"));
                    if(viewType.equals(ClientConstants.ACTION_STATUS[2]))
                        hash.put("MODE_OF_OPERATION","EDIT");
                    if(viewType==AUTHORIZE)
                        hash.put("MODE_OF_OPERATION","AUTHORIZE");
                    hash.put("TRANS_ID",CommonUtil.convertObjToStr(hash.get("ACT_NUM")));
                    hash.put("USER_ID",TrueTransactMain.USER_ID);
                    hash.put("TRANS_DT", currDt.clone());
                    hash.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
                    ClientUtil.execute("insertauthorizationLock", hash);
                }
            }
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
                String lockedBy = "";
                HashMap Lockmap = new HashMap();
                Lockmap.put("SCREEN_ID", getScreenID());
                Lockmap.put("RECORD_KEY", CommonUtil.convertObjToStr(hash.get("FA_DESC_ID")));
                Lockmap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
                System.out.println("Record Key Map : " + Lockmap);
                java.util.List lstLock = ClientUtil.executeQuery("selectEditLock", Lockmap);
                if (lstLock.size() > 0) {
                    lockedBy = CommonUtil.convertObjToStr(lstLock.get(0));
                    if (!lockedBy.equals(ProxyParameters.USER_ID)) {
                        btnSave.setEnabled(false);
                        ClientUtil.enableDisable(panAssetDescription,false);
                        enableDisablePanButton(false);
                        enableDisablePanEmpDetails(false);
                    } else {
                        btnSave.setEnabled(true);
                        ClientUtil.enableDisable(panAssetDescription,true);
                        enableDisablePanButton(true);
                        enableDisablePanEmpDetails(true);
                    }
                } else {
                    ClientUtil.enableDisable(panAssetDescription,true);
                    enableDisablePanButton(true);
                    enableDisablePanEmpDetails(true);
                    btnSave.setEnabled(true);
                }
                setOpenForEditBy(lockedBy);
                if (lockedBy.length() > 0 && !lockedBy.equals(ProxyParameters.USER_ID)) {
                    String data = getLockDetails(lockedBy, getScreenID()) ;
                    ClientUtil.showMessageWindow("Selected Record is Opened/Modified by " + lockedBy + data.toString());
                    btnSave.setEnabled(false);
                    ClientUtil.enableDisable(panAssetDescription,false);
                }
            }
        } catch(Exception e){
            e.printStackTrace();
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
        if (viewType == AUTHORIZE && isFilled){
            ArrayList arrList = new ArrayList();
            HashMap authorizeMap = new HashMap();
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("FA_DESC_ID", observable.getAssetDescID());
            singleAuthorizeMap.put("AUTH_BY", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("AUTH_DT",ClientUtil.getCurrentDateWithTime());
            singleAuthorizeMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            arrList.add(singleAuthorizeMap);
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorize(authorizeMap,observable.getAssetDescID());
            viewType = "";
            super.setOpenForEditBy(observable.getStatusBy());
            singleAuthorizeMap = null;
            arrList = null;
            authorizeMap = null;
        }else{
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put("USER_ID", TrueTransactMain.USER_ID);
            whereMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            mapParam.put(CommonConstants.MAP_NAME, "getFixedAssetsDescDetailsAuthorize");
            isFilled = false;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
            observable.setStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }
    
    /** Method to make HelpButton btnUserId enable or disable..accroding to Edit,Delete,New,Save Button Clicked */
    private void setHelpBtnEnableDisable(boolean flag){
        //                btnE.setEnabled(flag);
    }
    
    public void authorize(HashMap map,String id) {
        System.out.println("Authorize Map : " + map);
        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
            observable.set_authorizeMap(map);
            observable.doAction();
            if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED){
                super.setOpenForEditBy(observable.getStatusBy());
                super.removeEditLock(id);
            }
            btnCancelActionPerformed(null);
            observable.setStatus();
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
        }
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
    
    private void enableDisablePanButton(boolean flag){
        btnEmpDelete.setEnabled(flag);
        btnEmpSave.setEnabled(flag);
        btnEmpNew.setEnabled(flag);
    }
    private void enableDisablePanEmpDetails(boolean flag){
        txtAssetSubType.setEnabled(flag);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panAssetDescription = new com.see.truetransact.uicomponent.CPanel();
        panAssetList = new com.see.truetransact.uicomponent.CPanel();
        srpTokenLost = new com.see.truetransact.uicomponent.CScrollPane();
        tblAssetList = new com.see.truetransact.uicomponent.CTable();
        panDescDetails = new com.see.truetransact.uicomponent.CPanel();
        panIndAssetDetails = new com.see.truetransact.uicomponent.CPanel();
        lblAssetType = new com.see.truetransact.uicomponent.CLabel();
        panEmpBtn = new com.see.truetransact.uicomponent.CPanel();
        btnEmpNew = new com.see.truetransact.uicomponent.CButton();
        btnEmpSave = new com.see.truetransact.uicomponent.CButton();
        btnEmpDelete = new com.see.truetransact.uicomponent.CButton();
        cboAssetType = new com.see.truetransact.uicomponent.CComboBox();
        txtAssetSubType = new com.see.truetransact.uicomponent.CTextField();
        txtAssetDescID = new com.see.truetransact.uicomponent.CTextField();
        lblAssetSubType = new com.see.truetransact.uicomponent.CLabel();
        lblFaDescID = new com.see.truetransact.uicomponent.CLabel();
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
        setMaximumSize(new java.awt.Dimension(750, 550));
        setMinimumSize(new java.awt.Dimension(750, 550));
        setPreferredSize(new java.awt.Dimension(750, 550));

        panAssetDescription.setMaximumSize(new java.awt.Dimension(600, 450));
        panAssetDescription.setMinimumSize(new java.awt.Dimension(600, 450));
        panAssetDescription.setPreferredSize(new java.awt.Dimension(600, 450));
        panAssetDescription.setLayout(new java.awt.GridBagLayout());

        panAssetList.setMaximumSize(new java.awt.Dimension(250, 300));
        panAssetList.setMinimumSize(new java.awt.Dimension(280, 300));
        panAssetList.setPreferredSize(new java.awt.Dimension(280, 300));
        panAssetList.setLayout(new java.awt.GridBagLayout());

        srpTokenLost.setMaximumSize(new java.awt.Dimension(240, 280));
        srpTokenLost.setMinimumSize(new java.awt.Dimension(280, 290));
        srpTokenLost.setPreferredSize(new java.awt.Dimension(280, 290));

        tblAssetList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "SL No", "Emp Name", "Emp ID", "AuthStatus"
            }
        ));
        tblAssetList.setMaximumSize(new java.awt.Dimension(2147483647, 1000));
        tblAssetList.setMinimumSize(new java.awt.Dimension(280, 500));
        tblAssetList.setPreferredScrollableViewportSize(new java.awt.Dimension(250, 550));
        tblAssetList.setPreferredSize(new java.awt.Dimension(280, 500));
        tblAssetList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblAssetListMousePressed(evt);
            }
        });
        srpTokenLost.setViewportView(tblAssetList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panAssetList.add(srpTokenLost, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAssetDescription.add(panAssetList, gridBagConstraints);

        panDescDetails.setMaximumSize(new java.awt.Dimension(360, 300));
        panDescDetails.setMinimumSize(new java.awt.Dimension(330, 300));
        panDescDetails.setPreferredSize(new java.awt.Dimension(330, 300));
        panDescDetails.setLayout(new java.awt.GridBagLayout());

        panIndAssetDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Asset Details"));
        panIndAssetDetails.setMaximumSize(new java.awt.Dimension(350, 180));
        panIndAssetDetails.setMinimumSize(new java.awt.Dimension(300, 180));
        panIndAssetDetails.setPreferredSize(new java.awt.Dimension(300, 180));
        panIndAssetDetails.setLayout(new java.awt.GridBagLayout());

        lblAssetType.setText("Asset Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panIndAssetDetails.add(lblAssetType, gridBagConstraints);

        panEmpBtn.setLayout(new java.awt.GridBagLayout());

        btnEmpNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnEmpNew.setToolTipText("New");
        btnEmpNew.setMaximumSize(new java.awt.Dimension(29, 27));
        btnEmpNew.setMinimumSize(new java.awt.Dimension(29, 27));
        btnEmpNew.setPreferredSize(new java.awt.Dimension(29, 27));
        btnEmpNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmpNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmpBtn.add(btnEmpNew, gridBagConstraints);

        btnEmpSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnEmpSave.setToolTipText("Save");
        btnEmpSave.setMaximumSize(new java.awt.Dimension(29, 27));
        btnEmpSave.setMinimumSize(new java.awt.Dimension(29, 27));
        btnEmpSave.setName("btnContactNoAdd");
        btnEmpSave.setPreferredSize(new java.awt.Dimension(29, 27));
        btnEmpSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmpSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmpBtn.add(btnEmpSave, gridBagConstraints);

        btnEmpDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnEmpDelete.setToolTipText("Delete");
        btnEmpDelete.setMaximumSize(new java.awt.Dimension(29, 27));
        btnEmpDelete.setMinimumSize(new java.awt.Dimension(29, 27));
        btnEmpDelete.setPreferredSize(new java.awt.Dimension(29, 27));
        btnEmpDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmpDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panEmpBtn.add(btnEmpDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 0, 0, 0);
        panIndAssetDetails.add(panEmpBtn, gridBagConstraints);

        cboAssetType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboAssetType.setPopupWidth(225);
        cboAssetType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboAssetTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panIndAssetDetails.add(cboAssetType, gridBagConstraints);

        txtAssetSubType.setMaxLength(128);
        txtAssetSubType.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAssetSubType.setName("txtCompany");
        txtAssetSubType.setValidation(new DefaultValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panIndAssetDetails.add(txtAssetSubType, gridBagConstraints);

        txtAssetDescID.setEditable(false);
        txtAssetDescID.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panIndAssetDetails.add(txtAssetDescID, gridBagConstraints);

        lblAssetSubType.setText("Sub Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panIndAssetDetails.add(lblAssetSubType, gridBagConstraints);

        lblFaDescID.setText("FA Desc ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panIndAssetDetails.add(lblFaDescID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDescDetails.add(panIndAssetDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 20);
        panAssetDescription.add(panDescDetails, gridBagConstraints);

        getContentPane().add(panAssetDescription, java.awt.BorderLayout.CENTER);

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
    
    private void cboAssetTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboAssetTypeActionPerformed
        // TODO add your handling code here:
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW){
            String assetTypeExists =  CommonUtil.convertObjToStr(((ComboBoxModel)(cboAssetType.getModel())).getKeyForSelected());
            HashMap map = new HashMap();
            map.put("ASSET_TYPE",assetTypeExists);
            java.util.List lst= ClientUtil.executeQuery("countAssetsExists",map);
            int a=CommonUtil.convertObjToInt(lst.get(0));
            if(a>0){
                ClientUtil.showMessageWindow("This Asset Type Already Exists...Only Edit Allowed Of This Asset Type");
                cboAssetType.setSelectedItem("");
                return;
            }
        }
    }//GEN-LAST:event_cboAssetTypeActionPerformed
    
    private void btnEmpDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmpDeleteActionPerformed
        // TODO add your handling code here:
        String authStatus=CommonUtil.convertObjToStr(tblAssetList.getValueAt(tblAssetList.getSelectedRow(),3));
        if(authStatus.length()>0){
            ClientUtil.showAlertWindow("Row Is Already AUTHORIZED/REJECTED..Cant be Deleted");
            btnSave.setEnabled(false);
            ClientUtil.enableDisable(panAssetDescription,false);
            btnEmpDelete.setEnabled(false);
            btnEmpSave.setEnabled(false);
            return;
        }
        String s=  CommonUtil.convertObjToStr(tblAssetList.getValueAt(tblAssetList.getSelectedRow(),0));
        observable.deleteTableData(s,tblAssetList.getSelectedRow());
        observable.resetEmpDetails();
        enableDisablePanEmpDetails(false);
        btnEmpNew.setEnabled(true);
        btnEmpSave.setEnabled(false);
        btnEmpDelete.setEnabled(false);
    }//GEN-LAST:event_btnEmpDeleteActionPerformed
    
    private void tblAssetListMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAssetListMousePressed
        // TODO add your handling code here:
        updateOBFields();
        updateMode = true;
        updateTab= tblAssetList.getSelectedRow();
        observable.setNewData(false);
        String st=CommonUtil.convertObjToStr(tblAssetList.getValueAt(tblAssetList.getSelectedRow(),0));
        observable.populateLeaveDetails(st);
        enableDisablePanButton(true);
        txtAssetSubType.setEnabled(true);
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_AUTHORIZE||observable.getActionType()==ClientConstants.ACTIONTYPE_REJECT ||
        observable.getActionType()==ClientConstants.ACTIONTYPE_VIEW || observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE ||
        observable.getActionType()==ClientConstants.ACTIONTYPE_EXCEPTION){
            txtAssetSubType.setEnabled(false);
            enableDisablePanButton(false);
        }
        else{
            if(updateMode){
                String authStatus=CommonUtil.convertObjToStr(tblAssetList.getValueAt(tblAssetList.getSelectedRow(),3));
                if(authStatus.length()>0 && updateMode){
                    ClientUtil.showAlertWindow("Row Is Already AUTHORIZED/REJECTED...");
                    btnSave.setEnabled(false);
                    ClientUtil.enableDisable(panAssetDescription,false);
                    btnEmpDelete.setEnabled(false);
                    btnEmpSave.setEnabled(false);
                    return;
                }
            }
            txtAssetSubType.setEnabled(true);
            enableDisablePanButton(true);
            btnEmpNew.setEnabled(false);
        }
        observable.notifyObservers();
    }//GEN-LAST:event_tblAssetListMousePressed
    
    private void btnEmpSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmpSaveActionPerformed
        // TODO add your handling code here:
        try{
            if(txtAssetSubType.getText().length()>0){
                String assetSubType = txtAssetSubType.getText();
                String assetTypeExists =  CommonUtil.convertObjToStr(((ComboBoxModel)(cboAssetType.getModel())).getKeyForSelected());
                int tableCount = tblAssetList.getRowCount();
                if(tblAssetList.getRowCount()>0){
                    for(int i=0;i<tblAssetList.getRowCount();i++){
                        String subType=CommonUtil.convertObjToStr(tblAssetList.getValueAt(i,2));
                        String assetType=CommonUtil.convertObjToStr(tblAssetList.getValueAt(i,1));
                        
                        if(subType.equalsIgnoreCase(assetSubType) && !updateMode) {
                            ClientUtil.displayAlert("Asset Sub Type Already Exists in this Table");
                            txtAssetSubType.setText("");
                            return;
                        }
                        if(!assetTypeExists.equalsIgnoreCase(assetType)) {
                            ClientUtil.displayAlert("Asset Type Should Be Unique");
                            return;
                        }
                    }
                }
                updateOBFields();
                observable.addToTable(updateTab,updateMode);
                observable.resetEmpDetails();
                ClientUtil.enableDisable(panAssetDescription,false);
                enableDisablePanEmpDetails(false);
                btnSave.setEnabled(true);
                enableDisablePanButton(false);
                btnEmpNew.setEnabled(true);
            }
            else{
                ClientUtil.displayAlert("Asset Sub Type Should Not Be Empty...");
                return;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnEmpSaveActionPerformed
    
    private void btnEmpNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmpNewActionPerformed
        // TODO add your handling code here:
        enableDisablePanEmpDetails(true);
        updateMode = false;
        cboAssetType.setEnabled(false);
        txtAssetSubType.setText("");
        observable.setNewData(true);
        btnEmpSave.setEnabled(true);
        btnEmpDelete.setEnabled(false);
        btnEmpNew.setEnabled(false);
    }//GEN-LAST:event_btnEmpNewActionPerformed
    
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        callView("Enquiry");
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnCancel.setEnabled(true);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
        btnCancel.setEnabled(true);
        btnException.setEnabled(false);
        btnAuthorize.setEnabled(false);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnCancel.setEnabled(true);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
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
        setModified(false);
        deletescreenLock();
        observable.resetForm();
        setHelpBtnEnableDisable(false);
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panIndAssetDetails, false);
        ClientUtil.enableDisable(panEmpBtn, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        setButtonEnableDisable();
        enableDisablePanButton(false);
        viewType = "";
        updateTab = -1;
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        btnNew.setEnabled(true);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
        btnSave.setEnabled(false);
        lblStatus.setText("               ");
        isFilled = false;
        updateMode = false;
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        // setHelpBtnEnableDisable(false);
        callView("Delete");
        lblStatus.setText("Delete");
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        btnSave.setEnabled(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        // setHelpBtnEnableDisable(true);
        callView("Edit");
        lblStatus.setText("Edit");
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        txtAssetDescID.setEnabled(false);
        btnEmpSave.setEnabled(true);
        btnEmpDelete.setEnabled(true);
        btnEmpNew.setEnabled(false);
        btnSave.setEnabled(true);
        btnDelete.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panIndAssetDetails);
        if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0 ){
            displayAlert(mandatoryMessage);
        }else{
            if(txtAssetSubType.getText().length()>0 && observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE){
                ClientUtil.displayAlert("Asset Details Not Saved ! ! !");
                return;
            }
            setModified(false);
            updateOBFields();
            saveAction();
            btnCancel.setEnabled(true);
            btnAuthorize.setEnabled(true);
            btnReject.setEnabled(true);
            btnException.setEnabled(true);
            ClientUtil.enableDisable(tbrTokenConfig,false);
        }
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // TODO add your handling code here:
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        setModified(true);
        setHelpBtnEnableDisable(true);
        enableDisablePanButton(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.resetForm();
        observable.setStatus();
        setButtonEnableDisable();
        ClientUtil.enableDisable(panAssetDescription,true);
        txtAssetSubType.setEnabled(true);
        txtAssetDescID.setEnabled(false);
        btnEmpSave.setEnabled(true);
        btnEmpDelete.setEnabled(false);
        btnEmpNew.setEnabled(false);
        btnSave.setEnabled(true);
        ClientUtil.enableDisable(tbrTokenConfig,true);
        btnEmpNew.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnPrint.setEnabled(false);
    }//GEN-LAST:event_btnNewActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnEmpDelete;
    private com.see.truetransact.uicomponent.CButton btnEmpNew;
    private com.see.truetransact.uicomponent.CButton btnEmpSave;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboAssetType;
    private com.see.truetransact.uicomponent.CLabel lbSpace2;
    private com.see.truetransact.uicomponent.CLabel lbSpace3;
    private com.see.truetransact.uicomponent.CLabel lblAssetSubType;
    private com.see.truetransact.uicomponent.CLabel lblAssetType;
    private com.see.truetransact.uicomponent.CLabel lblFaDescID;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
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
    private com.see.truetransact.uicomponent.CPanel panAssetDescription;
    private com.see.truetransact.uicomponent.CPanel panAssetList;
    private com.see.truetransact.uicomponent.CPanel panDescDetails;
    private com.see.truetransact.uicomponent.CPanel panEmpBtn;
    private com.see.truetransact.uicomponent.CPanel panIndAssetDetails;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptView;
    private com.see.truetransact.uicomponent.CScrollPane srpTokenLost;
    private com.see.truetransact.uicomponent.CTable tblAssetList;
    private com.see.truetransact.uicomponent.CToolBar tbrTokenConfig;
    private com.see.truetransact.uicomponent.CTextField txtAssetDescID;
    private com.see.truetransact.uicomponent.CTextField txtAssetSubType;
    // End of variables declaration//GEN-END:variables
    public static void main(String[] args) {
        FixedAssetsDescriptionUI fad = new FixedAssetsDescriptionUI();
        javax.swing.JFrame j = new javax.swing.JFrame();
        j.getContentPane().add(fad);
        j.show();
        fad.show();
    }
}
