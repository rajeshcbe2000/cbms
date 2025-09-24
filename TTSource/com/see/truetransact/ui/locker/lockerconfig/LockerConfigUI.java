/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * LockerConfigUI.java
 *
 * Created on January 20, 2005, 3:03 PM
 */
package com.see.truetransact.ui.locker.lockerconfig;

/**
 *
 * @author ashokvijayakumar @modified : Sunil Added Edit Locking - 07-07-2005
 */
import com.see.truetransact.uicomponent.CInternalFrame;
//import com.see.truetransact.ui.transaction.token.tokenconfig.TokenConfigRB;
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
import com.see.truetransact.uicomponent.CTextField;

import java.util.Observable;
import java.util.Observer;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.List;
import com.see.truetransact.clientproxy.ProxyParameters;
import java.util.Date;
public class LockerConfigUI extends CInternalFrame implements Observer, UIMandatoryField {

    /**
     * Vairable Declarations
     */
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("com.see.truetransact.ui.locker.lockerconfig.LockerConfigRB", ProxyParameters.LANGUAGE);
    //Creating Instance For ResourceBundle-TokenConfigRB
    private LockerConfigOB observable; //Reference for the Observable Class TokenConfigOB
    private LockerConfigMRB objMandatoryRB = new LockerConfigMRB();//Instance for the MandatoryResourceBundle
    private final static ClientParseException parseException = ClientParseException.getInstance();//Instance of ClientParseException to log the Exceptions
    private HashMap mandatoryMap;//Map for putting up MandatoryFields in the UI
    private String viewType = "";//Variable used to store ActionType(New,Edit,Delete)
    private final String AUTHORIZE = "Authorize";//Variable used when btnAuthorize is clicked
    int modePoA = -1;
    private boolean updateModePoA = false;
    boolean isFilled = false;
    //    String lblStatus = "";
    //    private final        String AUTHORIZE = "AUTHORIZE";
    private final String REJECT = "REJECT";
    private final String EXCEPTION = "EXCEPTION";
    private Date currDt = null;
    /**
     * Creates new form TokenConfigUI
     */
    public LockerConfigUI() {
        initForm();
    }

    /**
     * Method which is used to initialize the form TokenConfig
     */
    private void initForm() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        setFieldNames();
        setObservable();
        observable.resetForm();
        //        txtNoOfTokens.setText("");
        initComponentData();
        setMandatoryHashMap();
        setMaxLengths();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panTokenConfiguration);
        ClientUtil.enableDisable(panTokenConfiguration, false);
        ClientUtil.enableDisable(panPowerAttorney_Part3, false, false, true);

        setButtonEnableDisable();


    }

    /*
     * Auto Generated Method - setFieldNames() This method assigns name for all
     * the components. Other functions are working based on this name.
     */
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
        panTokenConfiguration.setName("panTokenConfiguration");
        //        txtEndingTokenNo.setName("txtEndingTokenNo");
        //        txtNoOfTokens.setName("txtNoOfTokens");
        //        txtSeriesNo.setName("txtSeriesNo");
        //        txtStartingTokenNo.setName("txtStartingTokenNo");
        //        lblTokenConfigId.setName("lblTokenConfigId");
        //        txtTokenConfigId.setName("txtTokenConfigId");
        lblProdId.setName("lblProdId");
        btnProdId.setName("btnProdId");
        lblProdDesc.setName("lblProdDesc");
        lblProdDescVal.setName("lblProdDescVal");
        lblFromLocNo.setName("lblFromLocNo");
        txtFromLocNo.setName("txtFromLocNo");
        lblToLocNo.setName("lblToLocNo");
        txtToLocNo.setName("txtToLocNo");
        lblNoOfLockers.setName("lblNoOfLockers");
        txtNoOfLockers.setName("txtNoOfLockers");
        lblMasterKey.setName("lblMasterKey");
        txtMasterKey.setName("txtMasterKey");
        lblLockerKey.setName("lblLockerKey");
        txtLockerKey.setName("txtLockerKey");
        btnNew_Loc.setName("btnNew_Loc");
        btnSave_Loc.setName("btnSave_Loc");
        btnDelete_Loc.setName("btnDelete_Loc");
        txtProdId.setName("txtProdId");
        txtFromLocNo.setName("txtFromLocNo");
        txtToLocNo.setName("txtToLocNo");
        txtNoOfLockers.setName("txtNoOfLockers");
        txtMasterKey.setName("txtMasterKey");
        txtLockerKey.setName("txtLockerKey");
        panLockerConfig.setName("panLockerConfig");
        tblPowerAttroney.setName("tblPowerAttroney");
    }

    /*
     * Auto Generated Method - internationalize() This method used to assign
     * display texts from the Resource Bundle File.
     */
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
        lblProdId.setText(resourceBundle.getString("lblProdId"));
        btnProdId.setText(resourceBundle.getString("btnProdId"));
        lblProdDesc.setText(resourceBundle.getString("lblProdDesc"));
        lblProdDescVal.setText(resourceBundle.getString("lblProdDescVal"));
        lblFromLocNo.setText(resourceBundle.getString("lblFromLocNo"));
        txtFromLocNo.setText(resourceBundle.getString("txtFromLocNo"));
        lblToLocNo.setText(resourceBundle.getString("lblToLocNo"));
        txtToLocNo.setText(resourceBundle.getString("txtToLocNo"));
        lblNoOfLockers.setText(resourceBundle.getString("lblNoOfLockers"));
        txtNoOfLockers.setText(resourceBundle.getString("txtNoOfLockers"));
        lblMasterKey.setText(resourceBundle.getString("lblMasterKey"));
        txtMasterKey.setText(resourceBundle.getString("txtMasterKey"));
        lblLockerKey.setText(resourceBundle.getString("lblLockerKey"));
        txtLockerKey.setText(resourceBundle.getString("txtLockerKey"));
        btnNew_Loc.setText(resourceBundle.getString("btnNew_Loc"));
        btnSave_Loc.setText(resourceBundle.getString("btnSave_Loc"));
        btnDelete_Loc.setText(resourceBundle.getString("btnDelete_Loc"));
    }

    /**
     * Adds up the Observable to this form
     */
    private void setObservable() {
        try {
            observable = LockerConfigOB.getInstance();
            observable.addObserver(this);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    /*
     * Setting model to the combobox cboTokenType
     */
    private void initComponentData() {
        try {
            //            cboTokenType.setModel(observable.getCbmTokenType());
        } catch (ClassCastException e) {
            parseException.logException(e, true);
        }
    }

    /*
     * Auto Generated Method - update() This method called by Observable. It
     * updates the UI with Observable's data. If needed add/Remove RadioButtons
     * method need to be added.
     */
    public void update(Observable observed, Object arg) {
        //        txtTokenConfigId.setText(observable.getTxtTokenConfigId());
        //        cboTokenType.setSelectedItem(observable.getCboTokenType());
        //        txtSeriesNo.setText(observable.getTxtSeriesNo());
        //        txtStartingTokenNo.setText(observable.getTxtStartingTokenNo());
        //        txtEndingTokenNo.setText(observable.getTxtEndingTokenNo());
        //        txtNoOfTokens.setText(observable.getTxtNoOfTokens());
        txtProdId.setText(observable.getTxtProdId());
        txtFromLocNo.setText(observable.getTxtFromLocNo());
        txtToLocNo.setText(observable.getTxtToLocNo());
        txtNoOfLockers.setText(observable.getTxtNoOfLockers());
        txtMasterKey.setText(observable.getTxtMasterKey());
        txtLockerKey.setText(observable.getTxtLockerKey());
        lblStatus.setText(observable.getLblStatus());
        //        txtProdId.setText(observable.getLblStatus());
        //        txtFromLocNo.setText(observable.getLblStatus());
        //        txtToLocNo.setText(observable.getLblStatus());
        //        txtNoOfLockers.setText(observable.getLblStatus());
        //        txtMasterKey.setText(observable.getLblStatus());
        //        txtLockerKey.setText(observable.getLblStatus());
        //        lblProdDescVal.setText(observable.getLblStatus());
        tblPowerAttroney.setModel(observable.getTblPoA());
    }

    /*
     * Auto Generated Method - updateOBFields() This method called by Save
     * option of UI. It updates the OB with UI data.
     */
    public void updateOBFields() {
        observable.setTxtProdId(txtProdId.getText());
        observable.setTxtFromLocNo(txtFromLocNo.getText());
        observable.setTxtToLocNo(txtToLocNo.getText());
        observable.setTxtNoOfLockers(txtNoOfLockers.getText());
        observable.setTxtMasterKey(txtMasterKey.getText());
        observable.setTxtLockerKey(txtLockerKey.getText());
        //observable. setTxtFromLocNo("");
        observable.setModule(getModule());
        observable.setScreen(getScreen());
    }

    /*
     * Auto Generated Method - setMandatoryHashMap()
     *
     * ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
     *
     * This method list out all the Input Fields available in the UI. It needs a
     * class level HashMap variable mandatoryMap.
     */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboTokenType", new Boolean(true));
        mandatoryMap.put("txtSeriesNo", new Boolean(false));
        mandatoryMap.put("txtStartingTokenNo", new Boolean(true));
        mandatoryMap.put("txtEndingTokenNo", new Boolean(true));
        mandatoryMap.put("txtNoOfTokens", new Boolean(true));
    }

    /*
     * Auto Generated Method - getMandatoryHashMap() Getter method for setMandatoryHashMap().
     */
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    /*
     * Auto Generated Method - setHelpMessage() This method shows tooltip help
     * for all the input fields available in the UI. It needs the Mandatory
     * Resource Bundle object. Help display Label name should be lblMsg.
     */
    public void setHelpMessage() {
        txtProdId.setHelpMessage(lblMsg, objMandatoryRB.getString("txtProdId"));
        txtFromLocNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtFromLocNo"));
        txtToLocNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtToLocNo"));
        txtNoOfLockers.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNoOfLockers"));
        txtMasterKey.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMasterKey"));
        txtLockerKey.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLockerKey"));
    }

    /**
     * Used to set Maximum possible lenghts for TextFields
     */
    private void setMaxLengths() {
        //        txtSeriesNo.setMaxLength(16);
        //        txtSeriesNo.setAllowAll(true);
        ////        setValidation(new com.see.truetransact.uivalidation.DefaultValidation());
        //        txtStartingTokenNo.setMaxLength(8);
        //        txtEndingTokenNo.setMaxLength(8);
    }

    /*
     * Makes the button Enable or Disable accordingly when usier clicks new,edit
     * or delete buttons
     */
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

    /**
     * Method used to check whether the Mandatory Fields in the Form are Filled
     * or not
     */
    private String checkMandatory(javax.swing.JComponent component) {
        return new MandatoryCheck().checkMandatory(getClass().getName(), component);
    }

    /**
     * Method used to Give a Alert when any Mandatory Field is not filled by the
     * user
     */
    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }

    /*
     * Calls the execute method of TerminalOB to do insertion or updation or
     * deletion
     */
    private void saveAction(String status) {
        //To check mandtoryness of the Terminal panAcHdDetails,panAcHeadDetails panel and diplay appropriate
        //error message, else proceed
        final String mandatoryMessage = checkMandatory(panTokenConfiguration);
        StringBuffer message = new StringBuffer(mandatoryMessage);
        //        if(observable.getCbmTokenType().getKeyForSelected().equals("METAL")){
        //            if(txtSeriesNo.getText().equals("")){
        //                message.append(objMandatoryRB.getString("txtSeriesNo"));
        //            }
        //        }
        //         if((CommonUtil.convertObjToInt(txtStartingTokenNo.getText()))> (CommonUtil.convertObjToInt(txtEndingTokenNo.getText())))
        //         {
        //          message.append(objMandatoryRB.getString("txtNumber"));
        //         }
        if (message.length() > 0) {
            displayAlert(message.toString());
        } else if(tblPowerAttroney.getSelectedRow()<0){
            ClientUtil.showAlertWindow("Please enter atleast one record");
            return;
        }else{
            observable.execute(status);
            if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                HashMap lockMap = new HashMap();
                ArrayList lst = new ArrayList();
                lst.add("PROD_ID");
                lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
                if (observable.getProxyReturnMap() != null) {
                    if (observable.getProxyReturnMap().containsKey("PROD_ID")) {
                        lockMap.put("PROD_ID", observable.getProxyReturnMap().get("PROD_ID"));
                    }
                }
                if (status == CommonConstants.TOSTATUS_UPDATE) {
                    //                    lockMap.put("CONFIG_ID", observable.getTxtTokenConfigId());
                }
                //                setEditLockMap(lockMap);
                //                setEditLock();
                settings();
                btnAuthorize.setEnabled(true);
                btnReject.setEnabled(true);
                btnException.setEnabled(true);
                setModified(false);
            }
        }

    }

    /*
     * set the screen after the updation,insertion, deletion
     */
    private void settings() {
        lblProdDescVal.setText("");
        observable.resetTable();
        observable.resetForm();
        //        txtNoOfTokens.setText("");
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panTokenConfiguration, false);
        setButtonEnableDisable();
        observable.setResultStatus();
        observable.resetAllFieldsInPoA();
        //        tblPowerAttroney.setModel(observable.getTblPoA());
        tblPowerAttroney.setModel(observable.getTblPoA());
    }

    /*
     * Does necessary operaion when user clicks the save button
     */
    private void savePerformed() {
        updateOBFields();
        String action;
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            action = CommonConstants.TOSTATUS_INSERT;
            saveAction(action);
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            action = CommonConstants.TOSTATUS_UPDATE;
            saveAction(action);
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
            action = CommonConstants.TOSTATUS_DELETE;
            saveAction(action);
        }
    }

    /*
     * Method used to showPopup ViewAll by Executing a Query
     */
    private void callView(String currField) {
        viewType = currField;
        HashMap viewMap = new HashMap();
        //        ArrayList lst = new ArrayList();
        //        lst.add("PROD_ID");
        //        viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
        //        lst = null;
        if (currField == "SelectProdID") {
            viewMap.put(CommonConstants.MAP_NAME, "getSelectLockerProdID");
        } else {
            viewMap.put(CommonConstants.MAP_NAME, "getSelectLockerConfig");
        }
        HashMap where = new HashMap();
        where.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
        viewMap.put(CommonConstants.MAP_WHERE, where);
        where = null;
        new ViewAll(this, viewMap).show();
    }

    /*
     * Fills up the HashMap with data when user selects the row in ViewAll
     * screen
     */
    public void fillData(Object map) {
        isFilled = true;
        setModified(true);
        HashMap hash = (HashMap) map;
        if (viewType != null) {
            if (viewType.equals(ClientConstants.ACTION_STATUS[2])
                    || viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) || viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                HashMap where = new HashMap();
                where.put("PROD_ID", hash.get("PROD_ID"));
                where.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                hash.put(CommonConstants.MAP_WHERE, where);
                observable.populateData(hash);
                fillTblLockerDetails();
                fillTxtNoOfTokens();
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
                    ClientUtil.enableDisable(panTokenConfiguration, false);
                } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                    ClientUtil.enableDisable(panTokenConfiguration, true);
                }
                if (viewType.equals(AUTHORIZE)) {
                    ClientUtil.enableDisable(panTokenConfiguration, false);
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                }
                setButtonEnableDisable();
            } else if (viewType.equals("SelectProdID")) {
                txtProdId.setText((String) hash.get("PROD_ID"));
                lblProdDescVal.setText((String) hash.get("PROD_DESC"));
            }
        }
    }

    /**
     * Method used to fill up the TextFiled txtNoofTokens
     */
    private void fillTxtNoOfTokens() {
        //        if(txtStartingTokenNo.getText().length() != 0 && txtEndingTokenNo.getText().length() !=0 ){
        //            int start = Integer.parseInt(txtStartingTokenNo.getText());
        //            int end = Integer.parseInt(txtEndingTokenNo.getText());
        //            int totalTokens = 0;
        //            if(end>start){
        //                totalTokens = (end - start) + 1;
        //            }else if(start>end){
        //                totalTokens = (start - end) + 1;
        //            }
        //            txtNoOfTokens.setText(String.valueOf(totalTokens));
        //        }
    }

    private void fillTblLockerDetails() {
        HashMap map = new HashMap();
        map.put(CommonConstants.MAP_NAME, "getSelectLockerDetails");
        HashMap where = new HashMap();
        //        where.put("SERIES_NO", CommonUtil.convertObjToStr(observable.getCbmSeriesNo().getKeyForSelected()));
        where.put("PROD_ID", observable.getTxtProdId());
        where.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
        map.put(CommonConstants.MAP_WHERE, where);
        where = null;
        observable.setTbmLockerTab(observable.getResultList(map));
        tblPowerAttroney.setModel(observable.getTblPoA());
    }

    /**
     * This method is used to check for duplication of TokenType if any
     */
    private void tokenTypeCheck(String tokenType) {
        //        try{
        //            boolean exists = observable.isTokenTypeExists(tokenType);
        //            if(exists){
        //                String msg = "tokenTypeExistingMsg";
        //                showAlertWindow(msg);
        //                cboTokenType.requestFocus();
        //                cboTokenType.setSelectedItem("");
        //            }
        //        }catch(Exception e){
        //            parseException.logException(e,true);
        //        }
        return;
    }

    /**
     * Method called when txtSeriesNo focus is lost to check for duplication of
     * SeriesNo
     */
    private void seriesCheck(String tokenType, String seriesNo) {
        //        try{
        //            boolean exists = observable.isSeriesNoExists(tokenType, seriesNo);
        //            if(exists){
        //                String msg = "existingMsg";
        //                showAlertWindow(msg);
        //                txtSeriesNo.requestFocus();
        //                txtSeriesNo.setText("");
        //            }
        //        }catch(Exception e){
        //            parseException.logException(e,true);
        //        }
        return;
    }

    /**
     * This will show the alertwindow when the user enters the already existing
     * ShareType *
     */
    private int showAlertWindow(String alertMsg) throws Exception {
        int optionSelected = 1;
        String[] options = {resourceBundle.getString("cDialogOK")};
        optionSelected = COptionPane.showOptionDialog(null, resourceBundle.getString(alertMsg), CommonConstants.INFORMATIONTITLE,
                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                null, options, options[0]);
        return optionSelected;
    }

    private void checkTokenNo(CTextField tokenNo) {
        int number = Integer.parseInt(tokenNo.getText());
        if (number <= 0) {
            ClientUtil.showAlertWindow(resourceBundle.getString("tokenNoMsg"));
            tokenNo.setText("");
        }
    }

    /**
     * Method used to do Required operation when user clicks
     * btnAuthorize,btnReject or btnReject *
     */
    public void authorizeStatus(String authorizeStatus) {
        if (viewType == AUTHORIZE && isFilled) {
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDt.clone());
            //            singleAuthorizeMap.put("CONFIG_ID", txtTokenConfigId.getText());
            singleAuthorizeMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            singleAuthorizeMap.put("PROD_ID", txtProdId.getText());
            ClientUtil.execute("authorizeLockerConfig", singleAuthorizeMap);
            viewType = "";
            //            super.setOpenForEditBy(observable.getStatusBy());
            //            super.removeEditLock(txtTokenConfigId.getText());
            btnCancelActionPerformed(null);
            lblStatus.setText(authorizeStatus);
        } else {
            viewType = AUTHORIZE;
            setModified(true);
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            whereMap.put("PROD_ID", txtProdId.getText());
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            mapParam.put(CommonConstants.MAP_NAME, "getLockerConfigAuthorizeList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeLockerConfig");
            isFilled = false;
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            observable.setStatus();
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panTokenConfiguration = new com.see.truetransact.uicomponent.CPanel();
        panLockerConfig = new com.see.truetransact.uicomponent.CPanel();
        panOverdueRateBills8 = new com.see.truetransact.uicomponent.CPanel();
        panAccountNo1 = new com.see.truetransact.uicomponent.CPanel();
        lblProdDescVal = new com.see.truetransact.uicomponent.CLabel();
        lblProdDesc = new com.see.truetransact.uicomponent.CLabel();
        lblLockerKey = new com.see.truetransact.uicomponent.CLabel();
        txtLockerKey = new com.see.truetransact.uicomponent.CTextField();
        lblToLocNo = new com.see.truetransact.uicomponent.CLabel();
        txtToLocNo = new com.see.truetransact.uicomponent.CTextField();
        lblNoOfLockers = new com.see.truetransact.uicomponent.CLabel();
        txtNoOfLockers = new com.see.truetransact.uicomponent.CTextField();
        lblMasterKey = new com.see.truetransact.uicomponent.CLabel();
        txtMasterKey = new com.see.truetransact.uicomponent.CTextField();
        lblFromLocNo = new com.see.truetransact.uicomponent.CLabel();
        txtFromLocNo = new com.see.truetransact.uicomponent.CTextField();
        srpPowerAttroney = new com.see.truetransact.uicomponent.CScrollPane();
        tblPowerAttroney = new com.see.truetransact.uicomponent.CTable();
        panPowerAttorney_Part3 = new com.see.truetransact.uicomponent.CPanel();
        btnNew_Loc = new com.see.truetransact.uicomponent.CButton();
        btnSave_Loc = new com.see.truetransact.uicomponent.CButton();
        btnDelete_Loc = new com.see.truetransact.uicomponent.CButton();
        panPowerAttorney_Part4 = new com.see.truetransact.uicomponent.CPanel();
        lblProdId = new com.see.truetransact.uicomponent.CLabel();
        txtProdId = new com.see.truetransact.uicomponent.CTextField();
        btnProdId = new com.see.truetransact.uicomponent.CButton();
        tbrTokenConfig = new com.see.truetransact.uicomponent.CToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lbSpace3 = new com.see.truetransact.uicomponent.CLabel();
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

        panTokenConfiguration.setLayout(new java.awt.GridBagLayout());

        panLockerConfig.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panLockerConfig.setMaximumSize(new java.awt.Dimension(340, 300));
        panLockerConfig.setMinimumSize(new java.awt.Dimension(340, 300));
        panLockerConfig.setPreferredSize(new java.awt.Dimension(340, 287));
        panLockerConfig.setLayout(new java.awt.GridBagLayout());

        panOverdueRateBills8.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panLockerConfig.add(panOverdueRateBills8, gridBagConstraints);

        panAccountNo1.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panLockerConfig.add(panAccountNo1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLockerConfig.add(lblProdDescVal, gridBagConstraints);

        lblProdDesc.setText("Product Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLockerConfig.add(lblProdDesc, gridBagConstraints);

        lblLockerKey.setText("Locker Key No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLockerConfig.add(lblLockerKey, gridBagConstraints);

        txtLockerKey.setValidation(new NumericValidation());
        txtLockerKey.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtLockerKeyFocusLost(evt);
            }
        });
        txtLockerKey.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtLockerKeyKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLockerConfig.add(txtLockerKey, gridBagConstraints);

        lblToLocNo.setText("To Locker No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLockerConfig.add(lblToLocNo, gridBagConstraints);

        txtToLocNo.setValidation(new NumericValidation());
        txtToLocNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtToLocNoActionPerformed(evt);
            }
        });
        txtToLocNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtToLocNoFocusLost(evt);
            }
        });
        txtToLocNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtToLocNoKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLockerConfig.add(txtToLocNo, gridBagConstraints);

        lblNoOfLockers.setText("No. Of Lockers Configured");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLockerConfig.add(lblNoOfLockers, gridBagConstraints);

        txtNoOfLockers.setEditable(false);
        txtNoOfLockers.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLockerConfig.add(txtNoOfLockers, gridBagConstraints);

        lblMasterKey.setText("Master Key No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLockerConfig.add(lblMasterKey, gridBagConstraints);

        txtMasterKey.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLockerConfig.add(txtMasterKey, gridBagConstraints);

        lblFromLocNo.setText("From Locker No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLockerConfig.add(lblFromLocNo, gridBagConstraints);

        txtFromLocNo.setValidation(new NumericValidation());
        txtFromLocNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFromLocNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLockerConfig.add(txtFromLocNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panTokenConfiguration.add(panLockerConfig, gridBagConstraints);

        srpPowerAttroney.setMinimumSize(new java.awt.Dimension(360, 250));
        srpPowerAttroney.setPreferredSize(new java.awt.Dimension(360, 287));

        tblPowerAttroney.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sl No", "Locker No", "Master Key No", "Locker Key No", "Locker Status"
            }
        ));
        tblPowerAttroney.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblPowerAttroneyMousePressed(evt);
            }
        });
        srpPowerAttroney.setViewportView(tblPowerAttroney);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTokenConfiguration.add(srpPowerAttroney, gridBagConstraints);

        panPowerAttorney_Part3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panPowerAttorney_Part3.setLayout(new java.awt.GridBagLayout());

        btnNew_Loc.setText("New");
        btnNew_Loc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNew_LocActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPowerAttorney_Part3.add(btnNew_Loc, gridBagConstraints);

        btnSave_Loc.setText("Save");
        btnSave_Loc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSave_LocActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPowerAttorney_Part3.add(btnSave_Loc, gridBagConstraints);

        btnDelete_Loc.setText("Delete");
        btnDelete_Loc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelete_LocActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPowerAttorney_Part3.add(btnDelete_Loc, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 55, 4, 4);
        panTokenConfiguration.add(panPowerAttorney_Part3, gridBagConstraints);

        panPowerAttorney_Part4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panPowerAttorney_Part4.setLayout(new java.awt.GridBagLayout());

        lblProdId.setText("Product ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPowerAttorney_Part4.add(lblProdId, gridBagConstraints);

        txtProdId.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPowerAttorney_Part4.add(txtProdId, gridBagConstraints);

        btnProdId.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnProdId.setMaximumSize(new java.awt.Dimension(21, 21));
        btnProdId.setMinimumSize(new java.awt.Dimension(21, 21));
        btnProdId.setPreferredSize(new java.awt.Dimension(21, 21));
        btnProdId.setEnabled(false);
        btnProdId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProdIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPowerAttorney_Part4.add(btnProdId, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 55, 4, 4);
        panTokenConfiguration.add(panPowerAttorney_Part4, gridBagConstraints);

        getContentPane().add(panTokenConfiguration, java.awt.BorderLayout.CENTER);

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

        lblSpace26.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace26.setText("     ");
        lblSpace26.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace26);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnEdit);

        lblSpace27.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace27.setText("     ");
        lblSpace27.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace27);

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

        lblSpace28.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace28.setText("     ");
        lblSpace28.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace28);

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

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace29);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnException);

        lblSpace30.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace30.setText("     ");
        lblSpace30.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace30);

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

        lblSpace31.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace31.setText("     ");
        lblSpace31.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace31);

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

    private void tblPowerAttroneyMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPowerAttroneyMousePressed
        // TODO add your handling code here:
        powerAttroneyTableMousePressed();
    }//GEN-LAST:event_tblPowerAttroneyMousePressed
    private void powerAttroneyTableMousePressed() {
        // Actions have to be taken when a record of PoA details is selected
        if (tblPowerAttroney.getSelectedRow() >= 0) {
            txtLockerKey.setEnabled(true);
            //            if(!observableSettle.isIsEditMode()){
            observable.setIsTableClicked(true);
            updateOBFields();
            observable.populatePoATable(tblPowerAttroney.getSelectedRow());
            //            observableSettle.populateTable(tblPowerAttroney);
            String val = CommonUtil.convertObjToStr(tblPowerAttroney.getValueAt(tblPowerAttroney.getSelectedRow(), 0));
            boolean value = true;
            if (val.equalsIgnoreCase("false")) {
                value = true;
            } else {
                value = false;
            }
            //            if(observableSettle.isIsRetChq()){
            //                tblPowerAttroney.setValueAt(new Boolean(value),tblPowerAttroney.getSelectedRow(),0);
            //            }
            //            if(rdoReturnChq_Yes.isSelected()){
            //                observableSettle.setSelectAll(new Boolean(value),tblPowerAttroney.getSelectedRow());
            //            }
            if ((observable.getLblStatus().equals(ClientConstants.ACTION_STATUS[3]) || observable.getLblStatus().equals(ClientConstants.ACTION_STATUS[7]) || observable.getLblStatus().equals(ClientConstants.ACTION_STATUS[8]) || observable.getLblStatus().equals(ClientConstants.ACTION_STATUS[10])) || (viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT)) || (viewType.equals(ClientConstants.ACTION_STATUS[3]))) {
                //                setAllPoAEnableDisable(fals;
                setPoAToolBtnsEnableDisable(false);
            } else {
                updateModePoA = true;
                modePoA = tblPowerAttroney.getSelectedRow();
                setPoAToolBtnsEnableDisable(true);
                //                setAllPoAEnableDisable(tru;
            }

            //        }else{
            //            observableSettle.populatePoATable(tblPowerAttroney.getSelectedRow());
            //             updateModePoA = true;
            ////                modePoA = tblPowerAttroney.getSelectedRow();
            //                setPoAToolBtnsEnableDisable(true);
            //                setAllPoAEnableDisable(true);
            //        }
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                txtProdId.setEnabled(false);
                btnProdId.setEnabled(false);
                txtFromLocNo.setEnabled(false);
                txtToLocNo.setEnabled(false);
                txtNoOfLockers.setEnabled(false);
            }
        } else {
            //            setAllPoAEnableDisable(false);
            //            setPoAToolBtnsEnableDisable(false);
        }
    }
    // public void setLblStatus(String lblStatus){
    //        this.lblStatus = lblStatus;
    //    }
    //
    //    public String getLblStatus(){
    //        return lblStatus;
    //    }

    public void setPoAToolBtnsEnableDisable(boolean val) {
        btnNew_Loc.setEnabled(val);
        btnSave_Loc.setEnabled(val);
        btnDelete_Loc.setEnabled(val);
    }
    private void btnDelete_LocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete_LocActionPerformed
        // TODO add your handling code here:
        btnDeletePoAActionPerformed();

    }//GEN-LAST:event_btnDelete_LocActionPerformed
    private void btnDeletePoAActionPerformed() {
        observable.deletePoATable(modePoA);
        //        setPoAToolBtnsEnableDisable(false);
        //        setPoANewOnlyEnable();
        observable.resetForm();
        observable.ttNotifyObservers();
        modePoA = -1;
        updateModePoA = false;
    }

    private void savePoAactionPerformed() {
        //        String strOnBehalfOf = CommonUtil.convertObjToStr(((ComboBoxModel)cboPoACust.getModel()).getKeyForSelected());
        //        if(!updateModePoA)
        //        if (!observablePoA.isThisCustomerOnBehalfofOther(txtCustID_PoA.getText(), strOnBehalfOf)){
        //            return;
        //        }
        //        final String mandatoryMessage1 = new MandatoryCheck().checkMandatory(getClass().getName(), panPowerAttorney_Part1);
        //        /* mandatoryMessage1 length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
        //
        //        final String mandatoryMessage2 = new MandatoryCheck().checkMandatory(getClass().getName(), panPowerAttorney_Part2);
        //        /* mandatoryMessage2 length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
        //        if (mandatoryMessage1.length() > 0 || mandatoryMessage2.length() > 0){
        //            displayAlert(mandatoryMessage1+mandatoryMessage2);
        //        }else{
        //        if(!observableSettle.isIsEditMode()){
        //            setAllPoAEnableDisable(false);
        //            setPoAToolBtnsEnableDisable(false);
        btnNew_Loc.setEnabled(true);
        updateOBFields();
        //            updateOBFieldsBank();
        if (observable.addPoATab(modePoA, updateModePoA) == 1) {
            //                setAllPoAEnableDisable(true);
            btnSave_Loc.setEnabled(true);
        } else {
            btnSave_Loc.setEnabled(false);
        }
        updateModePoA = false;
        observable.ttNotifyObservers();
        //        }
        //        }else{
        //
        //        }
    }
    private void btnSave_LocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSave_LocActionPerformed
        // TODO add your handling code here:
        ClientUtil.enableDisable(panTokenConfiguration, false);
        savePoAactionPerformed();

        observable.setIsTableClicked(false);
        btnSave_Loc.setEnabled(true);
    }//GEN-LAST:event_btnSave_LocActionPerformed

    private void btnNew_LocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNew_LocActionPerformed
        // TODO add your handling code here:
        // newActionPerformed();
        ClientUtil.enableDisable(panTokenConfiguration, true, true, true);
        // btnProdId.setEnabled(true);
        setNewSaveOnlyEnable();
        txtFromLocNo.setText("");
        txtToLocNo.setText("");
        txtNoOfLockers.setText("");
        txtMasterKey.setText("");
        txtLockerKey.setText("");
        modePoA = -1;
        //updateModePoA = false;
        //        resetSetDetails();
        //        ClientUtil.enableDisable(panPowerAttorney_Part1,true);
        //        ClientUtil.enableDisable(panPowerAttorney_Part4,false);
        //        tdtClearingDt.setEnabled(false);
        ////        panPowerAttorney_Part4
        //        cboBounReason.setEnabled(false);
        //        ClientUtil.enableDisable(panPowerAttorney_Part9,true);
        //        btnSave_PoA2.setEnabled(false);
    }//GEN-LAST:event_btnNew_LocActionPerformed
    private void newActionPerformed() {
        txtProdId.setText("");
        txtFromLocNo.setText("");
        txtToLocNo.setText("");
        txtNoOfLockers.setText("");
        txtMasterKey.setText("");
        txtLockerKey.setText("");
        lblProdDescVal.setText("");
    }

    public void setToolBtnsEnableDisable(boolean val) {
        btnNew_Loc.setEnabled(val);
        btnSave_Loc.setEnabled(val);
        btnDelete_Loc.setEnabled(val);
    }

    public void setNewOnlyEnable() {
        btnNew_Loc.setEnabled(true);
        btnSave_Loc.setEnabled(false);
        btnDelete_Loc.setEnabled(false);
    }

    public void setNewSaveOnlyEnable() {
        btnNew_Loc.setEnabled(true);
        btnSave_Loc.setEnabled(true);
        btnDelete_Loc.setEnabled(false);
    }
    private void txtFromLocNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFromLocNoFocusLost
        // TODO add your handling code here:
        HashMap param = new HashMap();
        param.put("FROM_LOC_NO", CommonUtil.convertObjToInt(txtFromLocNo.getText()));
        param.put("PROD_ID", txtProdId.getText());
        param.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
        List lst = (List) ClientUtil.executeQuery("chkValidLocNum", param);
        param = null;
        if (lst != null && lst.size() > 0) {
            ClientUtil.showMessageWindow("Locker number already exists");
            txtFromLocNo.setText("");
        } else {
            if (!(CommonUtil.convertObjToStr(txtFromLocNo.getText()).equals(""))
                    && !(CommonUtil.convertObjToStr(txtToLocNo.getText()).equals(""))) {

                int frmLoc = CommonUtil.convertObjToInt(txtFromLocNo.getText());
                int toLoc = CommonUtil.convertObjToInt(txtToLocNo.getText());
                int qty = (-(frmLoc - toLoc)) + 1;
                txtNoOfLockers.setText(String.valueOf(qty));
            }
        }
        lst = null;

    }//GEN-LAST:event_txtFromLocNoFocusLost

    private void btnProdIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProdIdActionPerformed
        // TODO add your handling code here:
        callView("SelectProdID");
    }//GEN-LAST:event_btnProdIdActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        callView(ClientConstants.ACTION_STATUS[17]);
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed

    private void txtLockerKeyFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtLockerKeyFocusLost
        // TODO add your handling code here:
        //        if(txtStartingTokenNo.getText().length()!=0)
        //            checkTokenNo(txtStartingTokenNo);
    }//GEN-LAST:event_txtLockerKeyFocusLost

    private void txtLockerKeyKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLockerKeyKeyReleased
        // TODO add your handling code here:
        fillTxtNoOfTokens();
    }//GEN-LAST:event_txtLockerKeyKeyReleased

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
        //        if(observable.getAuthorizeStatus()!=null)
        //        super.removeEditLock(txtProdId.getText());
        setModified(false);
        observable.resetForm();
        //        txtNoOfTokens.setText("");
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panTokenConfiguration, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        observable.resetAllFieldsInPoA();
        tblPowerAttroney.setModel(observable.getTblPoA());
        setButtonEnableDisable();
        viewType = "";
        observable.setIsTableClicked(false);
        lblProdDescVal.setText("");
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
    }//GEN-LAST:event_btnCancelActionPerformed

    private void txtToLocNoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtToLocNoKeyReleased
        // TODO add your handling code here:
        fillTxtNoOfTokens();
    }//GEN-LAST:event_txtToLocNoKeyReleased

    private void txtToLocNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtToLocNoFocusLost
        // TODO add your handling code here:
        //        if(txtEndingTokenNo.getText().length()!=0)
        //            checkTokenNo(txtEndingTokenNo);
        int fromLocNo = CommonUtil.convertObjToInt(txtFromLocNo.getText());
        int toLocNo = CommonUtil.convertObjToInt(txtToLocNo.getText());
        if (!(CommonUtil.convertObjToStr(txtToLocNo.getText()).equals(""))) {
            if (toLocNo >= fromLocNo) {
                HashMap param = new HashMap();
                param.put("FROM_LOC_NO", CommonUtil.convertObjToInt(txtFromLocNo.getText()));
                param.put("PROD_ID", txtProdId.getText());
                param.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
                List lst = (List) ClientUtil.executeQuery("chkValidLocNum", param);
                param = null;
                if (lst != null && lst.size() > 0) {
                    ClientUtil.showMessageWindow("Locker number already exists");
                } else {
                    if (!(CommonUtil.convertObjToStr(txtFromLocNo.getText()).equals(""))
                            && !(CommonUtil.convertObjToStr(txtToLocNo.getText()).equals(""))) {

                        int frmLoc = CommonUtil.convertObjToInt(txtFromLocNo.getText());
                        int toLoc = CommonUtil.convertObjToInt(txtToLocNo.getText());
                        int qty = (-(frmLoc - toLoc)) + 1;
                        txtNoOfLockers.setText(String.valueOf(qty));
                    }
                }
                lst = null;
            } else {
                ClientUtil.showMessageWindow("ToLockerNo should be greater than FromLocNo");
                txtToLocNo.setText("");
                txtNoOfLockers.setText("");
            }
        }

    }//GEN-LAST:event_txtToLocNoFocusLost

    private void txtToLocNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtToLocNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtToLocNoActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        callView(ClientConstants.ACTION_STATUS[3]);
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
        
        savePerformed();

    }//GEN-LAST:event_btnSaveActionPerformed

    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // TODO add your handling code here:
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        setModified(true);
        lblProdDescVal.setText("");
        // observable.resetForm();

        //        txtNoOfTokens.setText("");
//        btnProdId.setEnabled(false);
//        txtProdId.setEnabled(false);
//        txtFromLocNo.setEnabled(false);
//        txtToLocNo.setEnabled(false);
//        txtMasterKey.setEnabled(false);
//        txtLockerKey.setEnabled(false);

        // observable.resetAllFieldsInPoA();

        ClientUtil.enableDisable(panTokenConfiguration, false);
        ClientUtil.enableDisable(panPowerAttorney_Part3, true, false, true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        txtNoOfLockers.setEnabled(false);
        txtLockerKey.setEnabled(false);

    }//GEN-LAST:event_btnNewActionPerformed
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
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDelete_Loc;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnNew_Loc;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnProdId;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnSave_Loc;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CLabel lbSpace2;
    private com.see.truetransact.uicomponent.CLabel lbSpace3;
    private com.see.truetransact.uicomponent.CLabel lblFromLocNo;
    private com.see.truetransact.uicomponent.CLabel lblLockerKey;
    private com.see.truetransact.uicomponent.CLabel lblMasterKey;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNoOfLockers;
    private com.see.truetransact.uicomponent.CLabel lblProdDesc;
    private com.see.truetransact.uicomponent.CLabel lblProdDescVal;
    private com.see.truetransact.uicomponent.CLabel lblProdId;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace26;
    private com.see.truetransact.uicomponent.CLabel lblSpace27;
    private com.see.truetransact.uicomponent.CLabel lblSpace28;
    private com.see.truetransact.uicomponent.CLabel lblSpace29;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace30;
    private com.see.truetransact.uicomponent.CLabel lblSpace31;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblToLocNo;
    private com.see.truetransact.uicomponent.CMenuBar mbrTokenConfig;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAccountNo1;
    private com.see.truetransact.uicomponent.CPanel panLockerConfig;
    private com.see.truetransact.uicomponent.CPanel panOverdueRateBills8;
    private com.see.truetransact.uicomponent.CPanel panPowerAttorney_Part3;
    private com.see.truetransact.uicomponent.CPanel panPowerAttorney_Part4;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTokenConfiguration;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptView;
    private com.see.truetransact.uicomponent.CScrollPane srpPowerAttroney;
    private com.see.truetransact.uicomponent.CTable tblPowerAttroney;
    private com.see.truetransact.uicomponent.CToolBar tbrTokenConfig;
    private com.see.truetransact.uicomponent.CTextField txtFromLocNo;
    private com.see.truetransact.uicomponent.CTextField txtLockerKey;
    private com.see.truetransact.uicomponent.CTextField txtMasterKey;
    private com.see.truetransact.uicomponent.CTextField txtNoOfLockers;
    private com.see.truetransact.uicomponent.CTextField txtProdId;
    private com.see.truetransact.uicomponent.CTextField txtToLocNo;
    // End of variables declaration//GEN-END:variables
}
