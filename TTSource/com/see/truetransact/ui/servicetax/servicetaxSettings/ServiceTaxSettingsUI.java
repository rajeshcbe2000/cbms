/*
 * TokenConfigUI.java
 *
 * Created on January 20, 2005, 3:03 PM
 */

package com.see.truetransact.ui.servicetax.servicetaxSettings;

/**
 *
 * @author  ashokvijayakumar
 * @modified : Sunil
 *      Added Edit Locking - 07-07-2005
 */
import com.see.rep.util.DateUtil;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.ui.transaction.token.tokenconfig.TokenConfigRB;
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
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.AcctStatusConstants;
import java.util.*;

public class ServiceTaxSettingsUI extends CInternalFrame implements Observer,UIMandatoryField{
    
    /** Vairable Declarations */
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("com.see.truetransact.ui.servicetax.servicetaxSettings.ServiceTaxSettingsRB", ProxyParameters.LANGUAGE);
    //Creating Instance For ResourceBundle-TokenConfigRB
    private ServiceTaxSettingsOB observable; //Reference for the Observable Class TokenConfigOB
    private ServiceTaxSettingsMRB objMandatoryRB = new ServiceTaxSettingsMRB();//Instance for the MandatoryResourceBundle
    private final static ClientParseException parseException = ClientParseException.getInstance();//Instance of ClientParseException to log the Exceptions
    private HashMap mandatoryMap;//Map for putting up MandatoryFields in the UI
    private String viewType = "";//Variable used to store ActionType(New,Edit,Delete)
    private final String AUTHORIZE="Authorize";//Variable used when btnAuthorize is clicked
    final int FIXEDDEPOSIT = 2,SWACHHCESS=3,KRISHIKALYANCESS=4;
    private int view=0;
    /** Creates new form TokenConfigUI */
    public ServiceTaxSettingsUI() {
        initForm();
    }
    
    /** Method which is used to initialize the form TokenConfig */
    private void initForm(){
        initComponents();
        setFieldNames();
        setObservable();
        observable.resetForm();
        initComponentData();
        setMandatoryHashMap();
        setMaxLengths();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panTokenConfiguration);
        ClientUtil.enableDisable(panTokenConfiguration, false);
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
        lblHigherCess.setName("lblHigherCess");
        lblMsg.setName("lblMsg");
        lbltaxRate.setName("lbltaxRate");
        lblSpace1.setName("lblSpace1");
        lblSpace3.setName("lblSpace3");
        lblSpace5.setName("lblSpace5");
        lblEdCess.setName("lblEdCess");
        lblStatus.setName("lblStatus");
        lblFromDate.setName("lblFromDate");
        mbrTokenConfig.setName("mbrTokenConfig");
        panStatus.setName("panStatus");
        panTokenConfiguration.setName("panTokenConfiguration");
        txthigherCess.setName("txthigherCess");
        txtTaxRate.setName("txtTaxRate");
        txtEduCess.setName("txtEduCess");
        lblServiceTaxId.setName("lblServiceTaxId");
        txtServiceTaxId.setName("txtServiceTaxId");
        txtwefDate.setName("txtwefDate");
        lbltaxHead.setName("lbltaxHead");
        txttaxHeadId.setName("txttaxHeadId");
        btntaxHead.setName("btntaxHead");
        txttaxHeadDesc.setName("txttaxHeadDesc");
        
    }
    
 /* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        lblSpace5.setText(resourceBundle.getString("lblSpace5"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        lbltaxRate.setText(resourceBundle.getString("lblSeriesNo"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        lblHigherCess.setText(resourceBundle.getString("lblHigherCess"));
        lblEdCess.setText(resourceBundle.getString("lblEdCess"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblFromDate.setText(resourceBundle.getString("lblFromDate"));
        lbSpace2.setText(resourceBundle.getString("lbSpace2"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblServiceTaxId.setText(resourceBundle.getString("lblServiceTaxId"));
    }
    
    /** Adds up the Observable to this form */
    private void setObservable() {
        try{
            observable = ServiceTaxSettingsOB.getInstance();
            observable.addObserver(this);
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /*Setting model to the combobox cboTokenType  */
    private void initComponentData() {
        try{
            cboCessRoundOff.setModel(observable.getCbmCessRoundOff()); // Added by nithya on 23-04-2020 for KD-1837
        }catch(ClassCastException e){
            parseException.logException(e,true);
        }
    }
    
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        txtServiceTaxId.setText(observable.getTxtServiceTaxId());
        txtServiceTaxId.setEnabled(false);
        txtwefDate.setDateValue(CommonUtil.convertObjToStr(observable.getTxtfromDate()));
        txtTaxRate.setText(observable.getTxtTaxRate());
        txtEduCess.setText(observable.getTxtEduCess());
        txthigherCess.setText(observable.getTxthigherCess());
        txtSwachhCess.setText(observable.getTxtSwachhCess());
        txtKrishiKalyan.setText(observable.getTxtKrishiKayanCess());
        txttaxHeadId.setText(observable.getTxttaxHeadId());
        txtSwachhCessHeadId.setText(observable.getTxtSwatchhHeadId());
        txtKrishiKalyanCessHeadId.setText(observable.getTxtKrishiKalyanHeadId());
        lblStatus.setText(observable.getLblStatus());
        cboCessRoundOff.setSelectedItem(((ComboBoxModel) cboCessRoundOff.getModel()).getDataForKey(observable.getCboCessRoundOff())); // Added by nithya on 23-04-2020 for KD-1837
    }
    
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setTxtServiceTaxId(txtServiceTaxId.getText());
        observable.setTxtfromDate(com.see.truetransact.commonutil.DateUtil.getDateMMDDYYYY(txtwefDate.getDateValue()));
        observable.setTxtTaxRate(txtTaxRate.getText());
        observable.setTxtEduCess(txtEduCess.getText());
        observable.setTxthigherCess(txthigherCess.getText());
        observable.setTxtSwachhCess(txtSwachhCess.getText());
        observable.setTxtKrishiKayanCess(txtKrishiKalyan.getText());
        observable.setTxttaxHeadId(txttaxHeadId.getText());
        observable.setTxtSwatchhHeadId(txtSwachhCessHeadId.getText());
        observable.setTxtKrishiKalyanHeadId(txtKrishiKalyanCessHeadId.getText());
        observable.setScreen(getScreen());
        observable.setCboCessRoundOff((String) ((ComboBoxModel) cboCessRoundOff.getModel()).getKeyForSelected()); // Added by nithya on 23-04-2020 for KD-1837
    }
    
    
/* Auto Generated Method - setMandatoryHashMap()
 
ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
 
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtwefDate", new Boolean(true));
        mandatoryMap.put("txtTaxRate", new Boolean(false));
        mandatoryMap.put("txtEduCess", new Boolean(true));
        mandatoryMap.put("txthigherCess", new Boolean(true));
        mandatoryMap.put("txttaxHeadId", new Boolean(true));
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
        txtwefDate.setHelpMessage(lblMsg, objMandatoryRB.getString("txtwefDate"));
        txtTaxRate.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTaxRate"));
        txtEduCess.setHelpMessage(lblMsg, objMandatoryRB.getString("txtEduCess"));
        txthigherCess.setHelpMessage(lblMsg, objMandatoryRB.getString("txthigherCess"));
        txtServiceTaxId.setHelpMessage(lblMsg, objMandatoryRB.getString("txtServiceTaxId"));
    }
    
    /** Used to set Maximum possible lenghts for TextFields */
    private void setMaxLengths(){
        txtTaxRate.setMaxLength(8);
        txtTaxRate.setAllowAll(true);
//        setValidation(new com.see.truetransact.uivalidation.DefaultValidation());
        txtEduCess.setMaxLength(8);
        txthigherCess.setMaxLength(8);
        txtSwachhCess.setMaxLength(8);
        txtKrishiKalyan.setMaxLength(8);
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
        txtServiceTaxId.setEnabled(false);
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
        final String mandatoryMessage = checkMandatory(panTokenConfiguration);
        StringBuffer message = new StringBuffer(mandatoryMessage);

         
        if(message.length() > 0 ){
            displayAlert(message.toString());
        }else{
            System.out.println("status============"+status);
            observable.execute(status);
            if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED){
                HashMap lockMap = new HashMap();
                ArrayList lst = new ArrayList();
                lst.add("SERVICETAX_GEN_ID");
                lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
                if (observable.getProxyReturnMap()!=null) {
                    if (observable.getProxyReturnMap().containsKey("SERVICETAX_GEN_ID")) {
                        lockMap.put("SERVICETAX_GEN_ID", observable.getProxyReturnMap().get("SERVICETAX_GEN_ID"));
                    }
                }
                if (status==CommonConstants.TOSTATUS_UPDATE) {
                    lockMap.put("CONFIG_ID", observable.getTxtServiceTaxId());
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
        ClientUtil.enableDisable(panTokenConfiguration, false);
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
        ArrayList lst = new ArrayList();
        lst.add("SERVICETAX_GEN_ID");
        viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
        lst = null;
        viewMap.put(CommonConstants.MAP_NAME, "getSelectServiceTaxSettings");
        HashMap where = new HashMap();
        where.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
        int yesNo = 0;
            String[] voucherOptions = {"New Settings", "Edit wrong data"};
            yesNo = COptionPane.showOptionDialog(null, "Select the edit options.", CommonConstants.WARNINGTITLE,
                    COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                    null, voucherOptions, voucherOptions[0]);
            if (yesNo == 0) {
                observable.setIsNewSettings(true);
                where.put("NEW_TAX_SETTINGS","NEW_TAX_SETTINGS");
            }else{
               observable.setIsNewSettings(false);
               where.put("EDIT_TAX_SETTINGS","EDIT_TAX_SETTINGS"); 
            }
        viewMap.put(CommonConstants.MAP_WHERE, where);
        where = null;
        new ViewAll(this,viewMap).show();
    }
    
    /* Fills up the HashMap with data when user selects the row in ViewAll screen  */
    public void fillData(Object  map) {
        setModified(true);
        HashMap hash = (HashMap) map;
        System.out.println("fillData  : " + hash);
        if (view == FIXEDDEPOSIT || view == SWACHHCESS || view == KRISHIKALYANCESS) {
            final String ACCOUNTHEAD = (String) hash.get("ACCOUNT HEAD");
            final String ACCOUNTHEADDESC = (String) hash.get("ACCOUNT HEAD DESCRIPTION");
            if (view == FIXEDDEPOSIT) {
                txttaxHeadId.setText(ACCOUNTHEAD);
                txttaxHeadDesc.setText(ACCOUNTHEADDESC);
            } else if (view == SWACHHCESS) {
                txtSwachhCessHeadId.setText(ACCOUNTHEAD);
                txtSwachhDescription.setText(ACCOUNTHEADDESC);
            } else {
                txtKrishiKalyanCessHeadId.setText(ACCOUNTHEAD);
                txtKrishiKalyanCessDescription.setText(ACCOUNTHEADDESC);
            }
        }
        if (viewType != null) {
            if (viewType.equals(ClientConstants.ACTION_STATUS[2]) ||
            viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) || viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                HashMap where = new HashMap();
                where.put("SERVICETAX_GEN_ID", hash.get("SERVICETAX_GEN_ID"));
                where.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                hash.put(CommonConstants.MAP_WHERE, where);
                observable.getData(hash);
                if (observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE || observable.getActionType()==ClientConstants.ACTIONTYPE_VIEW) {
                    ClientUtil.enableDisable(panTokenConfiguration, false);
                }else  if (observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                    ClientUtil.enableDisable(panTokenConfiguration, true);
                }
                if(viewType.equals(AUTHORIZE)){
                    ClientUtil.enableDisable(panTokenConfiguration, false);
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                }
                setButtonEnableDisable();
            }
        }
    }
    
    /** Method used to fill up the TextFiled txtNoofTokens */
   
    
   
    /** Method called when txtSeriesNo focus is lost to check for duplication of SeriesNo */
    private void seriesCheck(String tokenType, String seriesNo){
        try{
            boolean exists = observable.isSeriesNoExists(tokenType, seriesNo);
            if(exists){
                String msg = "existingMsg";
                showAlertWindow(msg);
                txtTaxRate.requestFocus();
                txtTaxRate.setText("");
            }
        }catch(Exception e){
            parseException.logException(e,true);
        }
        return;
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
    
    private void checkTokenNo(CTextField tokenNo){
        int number = Integer.parseInt(tokenNo.getText());
        if(number<=0){
            ClientUtil.showAlertWindow(resourceBundle.getString("tokenNoMsg"));
            tokenNo.setText("");
        }
    }
    private void popUp(int field) {
        final ViewAll objViewAll;
        final HashMap viewMap = new HashMap();
      //  viewType = field;
        
       //     updateOBFields();
            HashMap whereMap = new HashMap();
            view=field;
            if (field == FIXEDDEPOSIT ||field == SWACHHCESS||field == KRISHIKALYANCESS) {
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            viewMap.put(CommonConstants.MAP_NAME, "product.deposits.getAcctHeadList");}
            objViewAll = new ViewAll(this, viewMap);
          //  objViewAll.setTitle(resourceBundle.getString("acHdTitle"));
        objViewAll.show();
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
            mapParam.put(CommonConstants.MAP_NAME, "getServiceTaxSettingsAuthorizeList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeServiceTaxSettings");
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            observable.setStatus();
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
        } else if (viewType.equals(AUTHORIZE)){
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, ClientUtil.getCurrentDate());
            singleAuthorizeMap.put("SERVICETAX_GEN_ID", txtServiceTaxId.getText());
            singleAuthorizeMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            HashMap updatemap= new HashMap();
            updatemap.put("END_DATE", observable.getTxtfromDate());
            updatemap.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
            updatemap.put("SERVICETAX_GEN_ID", txtServiceTaxId.getText());
            //ClientUtil.execute("updateEndDate", updatemap);
            ClientUtil.execute("closeCurrentTaxSettings", updatemap);
            ClientUtil.execute("authorizeServiceTaxSettings", singleAuthorizeMap);
            viewType = "";
            super.setOpenForEditBy(observable.getStatusBy());
            super.removeEditLock(txtServiceTaxId.getText());
            btnCancelActionPerformed(null);
            lblStatus.setText(authorizeStatus);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panTokenConfiguration = new com.see.truetransact.uicomponent.CPanel();
        lblFromDate = new com.see.truetransact.uicomponent.CLabel();
        lbltaxRate = new com.see.truetransact.uicomponent.CLabel();
        lblEdCess = new com.see.truetransact.uicomponent.CLabel();
        lblHigherCess = new com.see.truetransact.uicomponent.CLabel();
        txtTaxRate = new com.see.truetransact.uicomponent.CTextField();
        txtEduCess = new com.see.truetransact.uicomponent.CTextField();
        txthigherCess = new com.see.truetransact.uicomponent.CTextField();
        lblServiceTaxId = new com.see.truetransact.uicomponent.CLabel();
        txtServiceTaxId = new com.see.truetransact.uicomponent.CTextField();
        txtwefDate = new com.see.truetransact.uicomponent.CDateField();
        lbltaxHead = new com.see.truetransact.uicomponent.CLabel();
        txttaxHeadId = new com.see.truetransact.uicomponent.CTextField();
        btntaxHead = new com.see.truetransact.uicomponent.CButton();
        txttaxHeadDesc = new com.see.truetransact.uicomponent.CTextField();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        cLabel2 = new com.see.truetransact.uicomponent.CLabel();
        txtSwachhCessHeadId = new com.see.truetransact.uicomponent.CTextField();
        txtKrishiKalyanCessHeadId = new com.see.truetransact.uicomponent.CTextField();
        btnSwachhCessHead = new com.see.truetransact.uicomponent.CButton();
        btnSecondCessHead = new com.see.truetransact.uicomponent.CButton();
        txtSwachhDescription = new com.see.truetransact.uicomponent.CTextField();
        txtKrishiKalyanCessDescription = new com.see.truetransact.uicomponent.CTextField();
        cLabel3 = new com.see.truetransact.uicomponent.CLabel();
        cLabel4 = new com.see.truetransact.uicomponent.CLabel();
        txtSwachhCess = new com.see.truetransact.uicomponent.CTextField();
        txtKrishiKalyan = new com.see.truetransact.uicomponent.CTextField();
        cLabel5 = new com.see.truetransact.uicomponent.CLabel();
        cboCessRoundOff = new com.see.truetransact.uicomponent.CComboBox();
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
        setMinimumSize(new java.awt.Dimension(529, 251));

        panTokenConfiguration.setLayout(new java.awt.GridBagLayout());

        lblFromDate.setText("With Effect Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 215, 0, 0);
        panTokenConfiguration.add(lblFromDate, gridBagConstraints);

        lbltaxRate.setText("Tax Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 258, 0, 0);
        panTokenConfiguration.add(lbltaxRate, gridBagConstraints);

        lblEdCess.setText("Education cess ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 216, 0, 0);
        panTokenConfiguration.add(lblEdCess, gridBagConstraints);

        lblHigherCess.setText("Secondary and  higher education cess");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 88, 0, 0);
        panTokenConfiguration.add(lblHigherCess, gridBagConstraints);

        txtTaxRate.setAllowNumber(true);
        txtTaxRate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTaxRateFocusLost(evt);
            }
        });
        txtTaxRate.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTaxRateKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTaxRateKeyTyped(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 94;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 7, 0, 0);
        panTokenConfiguration.add(txtTaxRate, gridBagConstraints);

        txtEduCess.setAllowNumber(true);
        txtEduCess.setValidation(new NumericValidation());
        txtEduCess.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEduCessFocusLost(evt);
            }
        });
        txtEduCess.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtEduCessKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 94;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 7, 0, 0);
        panTokenConfiguration.add(txtEduCess, gridBagConstraints);

        txthigherCess.setAllowNumber(true);
        txthigherCess.setValidation(new NumericValidation());
        txthigherCess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txthigherCessActionPerformed(evt);
            }
        });
        txthigherCess.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txthigherCessFocusLost(evt);
            }
        });
        txthigherCess.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txthigherCessKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 94;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 7, 0, 0);
        panTokenConfiguration.add(txthigherCess, gridBagConstraints);

        lblServiceTaxId.setText("Service Tax Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(47, 229, 0, 0);
        panTokenConfiguration.add(lblServiceTaxId, gridBagConstraints);

        txtServiceTaxId.setEditable(false);
        txtServiceTaxId.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 94;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(46, 7, 0, 0);
        panTokenConfiguration.add(txtServiceTaxId, gridBagConstraints);

        txtwefDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtwefDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 0, 0);
        panTokenConfiguration.add(txtwefDate, gridBagConstraints);

        lbltaxHead.setText("Service Tax Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 209, 0, 0);
        panTokenConfiguration.add(lbltaxHead, gridBagConstraints);

        txttaxHeadId.setAllowAll(true);
        txttaxHeadId.setMinimumSize(new java.awt.Dimension(100, 21));
        txttaxHeadId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txttaxHeadIdFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 7, 0, 0);
        panTokenConfiguration.add(txttaxHeadId, gridBagConstraints);

        btntaxHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btntaxHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btntaxHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btntaxHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btntaxHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 3, 0, 0);
        panTokenConfiguration.add(btntaxHead, gridBagConstraints);

        txttaxHeadDesc.setMinimumSize(new java.awt.Dimension(150, 21));
        txttaxHeadDesc.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 0, 84);
        panTokenConfiguration.add(txttaxHeadDesc, gridBagConstraints);

        cLabel1.setText("CGST Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 229, 0, 0);
        panTokenConfiguration.add(cLabel1, gridBagConstraints);

        cLabel2.setText("SGST Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 241, 0, 0);
        panTokenConfiguration.add(cLabel2, gridBagConstraints);

        txtSwachhCessHeadId.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 0, 0);
        panTokenConfiguration.add(txtSwachhCessHeadId, gridBagConstraints);

        txtKrishiKalyanCessHeadId.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 0, 0);
        panTokenConfiguration.add(txtKrishiKalyanCessHeadId, gridBagConstraints);

        btnSwachhCessHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnSwachhCessHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnSwachhCessHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnSwachhCessHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSwachhCessHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 3, 0, 0);
        panTokenConfiguration.add(btnSwachhCessHead, gridBagConstraints);

        btnSecondCessHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnSecondCessHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnSecondCessHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnSecondCessHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSecondCessHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 3, 0, 0);
        panTokenConfiguration.add(btnSecondCessHead, gridBagConstraints);

        txtSwachhDescription.setMinimumSize(new java.awt.Dimension(150, 21));
        txtSwachhDescription.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 0, 84);
        panTokenConfiguration.add(txtSwachhDescription, gridBagConstraints);

        txtKrishiKalyanCessDescription.setMinimumSize(new java.awt.Dimension(150, 21));
        txtKrishiKalyanCessDescription.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 0, 84);
        panTokenConfiguration.add(txtKrishiKalyanCessDescription, gridBagConstraints);

        cLabel3.setText("CGST Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 244, 0, 0);
        panTokenConfiguration.add(cLabel3, gridBagConstraints);

        cLabel4.setText("SGST Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 245, 0, 0);
        panTokenConfiguration.add(cLabel4, gridBagConstraints);

        txtSwachhCess.setAllowNumber(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 94;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 7, 0, 0);
        panTokenConfiguration.add(txtSwachhCess, gridBagConstraints);

        txtKrishiKalyan.setAllowNumber(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 94;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 7, 0, 0);
        panTokenConfiguration.add(txtKrishiKalyan, gridBagConstraints);

        cLabel5.setText("CESS Round Off");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 20;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 241, 0, 0);
        panTokenConfiguration.add(cLabel5, gridBagConstraints);

        cboCessRoundOff.setMaximumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 20;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 8, 15, 0);
        panTokenConfiguration.add(cboCessRoundOff, gridBagConstraints);

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

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        callView(ClientConstants.ACTION_STATUS[17]);
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed

    private void txtTaxRateKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTaxRateKeyTyped
        // TODO add your handling code here:

    }//GEN-LAST:event_txtTaxRateKeyTyped

    private void txtTaxRateKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTaxRateKeyPressed
        // TODO add your handling code here:

    }//GEN-LAST:event_txtTaxRateKeyPressed
    
    private void txtEduCessFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEduCessFocusLost
        // TODO add your handling code here:
        if(txtEduCess.getText().length()!=0)
            checkTokenNo(txtEduCess);
    }//GEN-LAST:event_txtEduCessFocusLost
    
    private void txtEduCessKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEduCessKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEduCessKeyReleased
    
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
    
    private void txtTaxRateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTaxRateFocusLost
        // TODO add your handling code here:
        
    }//GEN-LAST:event_txtTaxRateFocusLost
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        if(observable.getAuthorizeStatus()!=null)
        super.removeEditLock(txtServiceTaxId.getText());
        setModified(false); 
        observable.resetForm();
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panTokenConfiguration, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        setButtonEnableDisable();
        viewType = "";
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        observable.setIsNewSettings(false);
    }//GEN-LAST:event_btnCancelActionPerformed
        
    private void txthigherCessKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txthigherCessKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txthigherCessKeyReleased
    
    private void txthigherCessFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txthigherCessFocusLost
        // TODO add your handling code here:
        if(txthigherCess.getText().length()!=0)
            checkTokenNo(txthigherCess);
    }//GEN-LAST:event_txthigherCessFocusLost
    
    private void txthigherCessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txthigherCessActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txthigherCessActionPerformed
    
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
        setModified(false);
         if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW ){
           if(dateCheck(com.see.truetransact.commonutil.DateUtil.getDateMMDDYYYY(txtwefDate.getDateValue()))){
               return;
           }
        }
        savePerformed();
         
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        observable.setIsNewSettings(false);
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // TODO add your handling code here:
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        setModified(true);
        observable.resetForm();
        ClientUtil.enableDisable(panTokenConfiguration, true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        
    }//GEN-LAST:event_btnNewActionPerformed
 private boolean dateCheck(Date frmDt){
       boolean exists = false;
        try{
           String chkVal= observable.isTokenTypeExists(frmDt);
           if(chkVal!=null&&chkVal.length()>0){
               exists=true;
            if(exists){
                String msg = "This Period is Already Exists!!!";
                if(chkVal.equals("CREATED") ||chkVal.equals("MODIFIED")){
                    msg = "Entry is pending for Authorization";
                }
                ClientUtil.showAlertWindow(msg);
                txtwefDate.requestFocus();
            }
           }
        }catch(Exception e){
            e.printStackTrace();
            parseException.logException(e,true);
        }
        return exists;
    }
  public String getAccHeadDesc(String accHeadID) {
        HashMap map1 = new HashMap();
        map1.put("ACCHD_ID", accHeadID);
        List list1 = ClientUtil.executeQuery("getSelectAcchdDesc", map1);
        if (!list1.isEmpty()) {
            HashMap map2 = new HashMap();
            map2 = (HashMap) list1.get(0);
            String accHeadDesc = map2.get("AC_HD_DESC").toString();
            return accHeadDesc;
        } else {
            return "";
        }
    }
    private void txtwefDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtwefDateFocusLost
        // TODO add your handling code here:
         if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW ){
        //   dateCheck(com.see.truetransact.commonutil.DateUtil.getDateMMDDYYYY(txtwefDate.getDateValue()));
        }
    }//GEN-LAST:event_txtwefDateFocusLost

    private void txttaxHeadIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txttaxHeadIdFocusLost
        if (!(txttaxHeadId.getText().equalsIgnoreCase(""))) {
            observable.verifyAcctHead(txttaxHeadId, "product.deposits.getAcctHeadList");
            txttaxHeadDesc.setText(getAccHeadDesc(txttaxHeadId.getText()));
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_txttaxHeadIdFocusLost

    private void btntaxHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btntaxHeadActionPerformed
        popUp(FIXEDDEPOSIT);
        // TODO add your handling code here:
        // TODO add your handling code here:
    }//GEN-LAST:event_btntaxHeadActionPerformed

    private void btnSwachhCessHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSwachhCessHeadActionPerformed
        popUp(SWACHHCESS);
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSwachhCessHeadActionPerformed

    private void btnSecondCessHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSecondCessHeadActionPerformed
       popUp(KRISHIKALYANCESS);
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSecondCessHeadActionPerformed
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
    private com.see.truetransact.uicomponent.CButton btnSecondCessHead;
    private com.see.truetransact.uicomponent.CButton btnSwachhCessHead;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CButton btntaxHead;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CLabel cLabel2;
    private com.see.truetransact.uicomponent.CLabel cLabel3;
    private com.see.truetransact.uicomponent.CLabel cLabel4;
    private com.see.truetransact.uicomponent.CLabel cLabel5;
    private com.see.truetransact.uicomponent.CComboBox cboCessRoundOff;
    private com.see.truetransact.uicomponent.CLabel lbSpace2;
    private com.see.truetransact.uicomponent.CLabel lbSpace3;
    private com.see.truetransact.uicomponent.CLabel lblEdCess;
    private com.see.truetransact.uicomponent.CLabel lblFromDate;
    private com.see.truetransact.uicomponent.CLabel lblHigherCess;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblServiceTaxId;
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
    private com.see.truetransact.uicomponent.CLabel lbltaxHead;
    private com.see.truetransact.uicomponent.CLabel lbltaxRate;
    private com.see.truetransact.uicomponent.CMenuBar mbrTokenConfig;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTokenConfiguration;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptView;
    private com.see.truetransact.uicomponent.CToolBar tbrTokenConfig;
    private com.see.truetransact.uicomponent.CTextField txtEduCess;
    private com.see.truetransact.uicomponent.CTextField txtKrishiKalyan;
    private com.see.truetransact.uicomponent.CTextField txtKrishiKalyanCessDescription;
    private com.see.truetransact.uicomponent.CTextField txtKrishiKalyanCessHeadId;
    private com.see.truetransact.uicomponent.CTextField txtServiceTaxId;
    private com.see.truetransact.uicomponent.CTextField txtSwachhCess;
    private com.see.truetransact.uicomponent.CTextField txtSwachhCessHeadId;
    private com.see.truetransact.uicomponent.CTextField txtSwachhDescription;
    private com.see.truetransact.uicomponent.CTextField txtTaxRate;
    private com.see.truetransact.uicomponent.CTextField txthigherCess;
    private com.see.truetransact.uicomponent.CTextField txttaxHeadDesc;
    private com.see.truetransact.uicomponent.CTextField txttaxHeadId;
    private com.see.truetransact.uicomponent.CDateField txtwefDate;
    // End of variables declaration//GEN-END:variables
    
}
