/**
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * ShareProductUI.java
 *
 * Created on November 23, 2004, 4:00 PM
 */

package com.see.truetransact.ui.investments;

/**
 *
 * @author Ashok
 *  @modified : Sunil
 *      Added Edit Locking - 08-07-2005
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
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.Date;
import com.see.truetransact.ui.investments.CallMoneyExtensionMRB;
import com.see.truetransact.ui.common.transaction.TransactionUI;

public class CallMoneyExtensionUI extends CInternalFrame implements UIMandatoryField, Observer{
    
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private HashMap mandatoryMap;
    private CallMoneyExtensionOB observable;
    private CallMoneyMRB objMandatoryRB;
    
    private ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.investments.CallMoneyRB", ProxyParameters.LANGUAGE);
    private String viewType = new String();
    final String AUTHORIZE="Authorize";
    private TransactionUI transactionUI = new TransactionUI();
    private int yearTobeAdded = 1900;
    boolean isFilled = false;
    private Date currDt = null;
    /** Creates new form ShareProductUI */
    public CallMoneyExtensionUI() {
        initUIComponents();
        //        transactionUI.addToScreen(panTransaction);
        //        observable.setTransactionOB(transactionUI.getTransactionOB());
        //        panAmortizationDetails.setName("Transaction Details");
    }
    
    /** Initialsises the UIComponents */
    private void initUIComponents(){
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        setFieldNames();
        setMaxLength();
        internationalize();
        setMandatoryHashMap();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panCallMoneyDetails, getMandatoryHashMap());
        setHelpMessage();
        setObservable();
        observable.resetForm();
        initComponentData();
        setButtonEnableDisable();
        ClientUtil.enableDisable(panCallMoneyDetails,false);
        btnBatchID.setEnabled(false);
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
        lblCallMoneyInstituation.setName("lblCallMoneyInstituation");
        lblCallMoneyDate.setName("lblCallMoneyDate");
        lblCommunication.setName("lblCommunication");
        lblNoOfDays.setName("lblNoOfDays");
        lblInterestRate.setName("lblInterestRate");
        lblCallMoneyAmount.setName("lblCallMoneyAmount");
        lblParticulars.setName("lblParticulars");
        lblInterestAmount.setName("lblInterestAmount");
        tdtCallMoneyDate.setName("tdtCallMoneyDate");
        cboCallMoneyInstituation.setName("cboCallMoneyInstituation");
        cboCommunication.setName("cboCommunication");
        txtNoOfDays.setName("txtNoOfDays");
        txtInterestRate.setName("txtInterestRate");
        txtCallMoneyAmount.setName("txtCallMoneyAmount");
        txtInterestAmount.setName("txtInterestAmount");
        txtParticulars.setName("txtParticulars");
        txtNoOfDaysExtension.setName("txtNoOfDaysExtension");
        txtExtensionInterestRate.setName("txtExtensionInterestRate");
        lblNoOfDaysExtension.setName("lblNoOfDaysExtension");
        lblExtensionInterestRate.setName("lblExtensionInterestRate");
        
    }
    
    
    
    
    /* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        btnClose.setText(resourceBundle.getString("btnClose"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblCallMoneyInstituation.setText(resourceBundle.getString("lblCallMoneyInstituation"));
        lblCallMoneyDate.setText(resourceBundle.getString("lblCallMoneyDate"));
        lblCommunication.setText(resourceBundle.getString("lblCommunication"));
        lblNoOfDays.setText(resourceBundle.getString("lblNoOfDays"));
        lblInterestRate.setText(resourceBundle.getString("lblInterestRate"));
        lblCallMoneyAmount.setText(resourceBundle.getString("lblCallMoneyAmount"));
        lblParticulars.setText(resourceBundle.getString("lblParticulars"));
        lblInterestAmount.setText(resourceBundle.getString("lblInterestAmount"));
        lblNoOfDaysExtension.setText(resourceBundle.getString("lblNoOfDaysExtension"));
        lblExtensionInterestRate.setText(resourceBundle.getString("lblExtensionInterestRate"));
    }
    
/* Auto Generated Method - setMandatoryHashMap()
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboCallMoneyInstituation", new Boolean(true));
        mandatoryMap.put("cboCommunication", new Boolean(true));
        mandatoryMap.put("tdtTransDate", new Boolean(true));
        mandatoryMap.put("txtNoOfDays", new Boolean(true));
        mandatoryMap.put("txtInterestRate", new Boolean(true));
        mandatoryMap.put("txtCallMoneyAmount", new Boolean(true));
        mandatoryMap.put("txtParticulars", new Boolean(true));
        mandatoryMap.put("txtNoOfDaysExtension", new Boolean(true));
        mandatoryMap.put("txtExtensionInterestRate", new Boolean(true));
        
    }
    
/* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    
    /* Creates the insstance of ShareProduct which acts as  Observable to
     *ShareProduct UI */
    private void setObservable() {
        try{
            observable = CallMoneyExtensionOB.getInstance();
            observable.addObserver(this);
            observable.setTransactionOB(transactionUI.getTransactionOB());
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /* Sets the model for the comboboxes in the UI    */
    private void initComponentData() {
        try{
            cboCallMoneyInstituation.setModel(observable.getCbmCallMoneyInstituation());
            cboCommunication.setModel(observable.getCbmCallMoneyCommunication());
            
            
        }catch(ClassCastException e){
            parseException.logException(e,true);
        }
    }
    
/* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        objMandatoryRB = new CallMoneyMRB();
        cboCallMoneyInstituation.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCallMoneyInstituation"));
        cboCommunication.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCommunication"));
        tdtCallMoneyDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtCallMoneyDate"));
        txtNoOfDays.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNoOfDays"));
        txtInterestRate.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInterestRate"));
        txtCallMoneyAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCallMoneyAmount"));
        txtParticulars.setHelpMessage(lblMsg, objMandatoryRB.getString("txtParticulars"));
        txtInterestAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInterestAmount"));
        txtExtensionInterestRate.setHelpMessage(lblMsg, objMandatoryRB.getString("txtExtensionInterestRate"));
        txtNoOfDaysExtension.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNoOfDaysExtension"));
    }
    
    
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        tdtCallMoneyDate.setDateValue(CommonUtil.convertObjToStr(observable.getCallMoneydate()));
        cboCallMoneyInstituation.setSelectedItem(CommonUtil.convertObjToStr(observable.getCallMoneyInstituation()));
        cboCommunication.setSelectedItem(CommonUtil.convertObjToStr(observable.getCallMoneyCommunication()));
        txtNoOfDays.setText(CommonUtil.convertObjToStr(observable.getNoOfDays()));
        txtInterestRate.setText(CommonUtil.convertObjToStr(observable.getInterestRate()));
        txtCallMoneyAmount.setText(CommonUtil.convertObjToStr(observable.getCallMoneyAmount()));
        txtInterestAmount.setText(CommonUtil.convertObjToStr(observable.getInterestAmt()));
        txtParticulars.setText(CommonUtil.convertObjToStr(observable.getParticulars()));
        txtBatchId.setText(CommonUtil.convertObjToStr(observable.getBatchID()));
        lblTransDtValue.setText(CommonUtil.convertObjToStr(DateUtil.getDateMMDDYYYY(DateUtil.getStringDate(observable.getTransDate()))));
        txtNoOfDaysExtension.setText(CommonUtil.convertObjToStr(observable.getNoOfDayExtension()));
        txtExtensionInterestRate.setText(CommonUtil.convertObjToStr(observable.getExtensioninterestRate()));
    }
    
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setCallMoneydate(DateUtil.getDateMMDDYYYY(tdtCallMoneyDate.getDateValue()));
        observable.setCallMoneyInstituation(CommonUtil.convertObjToStr(cboCallMoneyInstituation.getSelectedItem()));
        observable.setCallMoneyCommunication(CommonUtil.convertObjToStr(cboCommunication.getSelectedItem()));
        observable.setNoOfDays(CommonUtil.convertObjToDouble(txtNoOfDays.getText()));
        observable.setInterestRate(CommonUtil.convertObjToDouble(txtInterestRate.getText()));
        observable.setInterestAmt(CommonUtil.convertObjToDouble(txtInterestAmount.getText()));
        observable.setCallMoneyAmount(CommonUtil.convertObjToDouble(txtCallMoneyAmount.getText()));
        observable.setInterestAmt(CommonUtil.convertObjToDouble(txtInterestAmount.getText()));
        observable.setParticulars(CommonUtil.convertObjToStr(txtParticulars.getText()));
        observable.setTransDate(DateUtil.getDateMMDDYYYY(lblTransDtValue.getText()));
        observable.setBatchID(CommonUtil.convertObjToStr(txtBatchId.getText()));
        observable.setExtensioninterestRate(CommonUtil.convertObjToDouble(txtExtensionInterestRate.getText()));
        observable.setNoOfDayExtension(CommonUtil.convertObjToDouble(txtNoOfDaysExtension.getText()));
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
    
    /** Setting up Lengths for the TextFields in theu UI */
    private void setMaxLength(){
        txtParticulars.setMaxLength(50);
        txtNoOfDays.setValidation(new NumericValidation(16,0));
        txtInterestRate.setValidation(new NumericValidation(16,2));
        txtCallMoneyAmount.setValidation(new NumericValidation(16,2));
        txtInterestAmount.setValidation(new NumericValidation(16,2));
        txtExtensionInterestRate.setValidation(new NumericValidation(16,2));
        txtNoOfDaysExtension.setValidation(new NumericValidation(16,0));
    }
    
    /** Making the btnShareAccount enable or disable according to the actiontype **/
    private void setHelpButtonEnableDisable(boolean enable){
        cboCallMoneyInstituation.setEnabled(enable);
        cboCommunication.setEnabled(enable);
        tdtCallMoneyDate.setEnabled(enable);
        txtNoOfDays.setEnabled(enable);
        txtInterestRate.setEnabled(enable);
        txtCallMoneyAmount.setEnabled(enable);
        txtParticulars.setEnabled(enable);
        txtInterestAmount.setEnabled(enable);
    }
    
    /* Does necessary operaion when user clicks the save button */
    private void savePerformed(){
        updateOBFields();
        double transTotalAmt = CommonUtil.convertObjToDouble(transactionUI.getTransactionOB().getLblTotalTransactionAmtVal()).doubleValue();
        
        observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
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
    
    /* Calls the execute method of ShareProductOB to do insertion or updation or deletion */
    private void saveAction(String status){
        //To check mandtoryness of the Terminal panAcHdDetails,panAcHeadDetails panel and diplay appropriate
        //error message, else proceed
        try{
            final String mandatoryMessage  = checkMandatory(panCallMoneyDetails);
            if(mandatoryMessage.length() > 0 ){
                displayAlert(mandatoryMessage);
            }else{
                observable.execute(status);
                if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED){
                    setButtonEnableDisable();
                    observable.resetForm();
                    resetTransactionUI();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    /** This will checks whether the Mandatory fields in the UI are filled up, If not filled up
     *it will retun an MandatoryMessage*/
    private String checkMandatory(javax.swing.JComponent component){
        return new MandatoryCheck().checkMandatory(getClass().getName(), component, getMandatoryHashMap());
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
        resetTransactionUI();
        ClientUtil.clearAll(this);
        setButtonEnableDisable();
        setHelpButtonEnableDisable(false);
        observable.setResultStatus();
    }
    
    /** This will show a popup screen which shows all tbe Rows.of the table */
    private void callView(String currField) {
        viewType = currField;
        HashMap viewMap = new HashMap();
        if (currField.equals(ClientConstants.ACTION_STATUS[2])  ||
        currField.equals(ClientConstants.ACTION_STATUS[3]) || currField.equals(ClientConstants.ACTION_STATUS[17])) {
            ArrayList lst = new ArrayList();
            //            lst.add("INVESTMENT_NAME");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            viewMap.put(CommonConstants.MAP_NAME, "getSelectCallMoneyExtensionOtherThanInsertlIST");
        }else if(viewType.equals("BATCHID")){
            viewMap.put(CommonConstants.MAP_NAME, "getSelectCallMoneyExtensionList");
        }
        
        new ViewAll(this,viewMap).show();
    }
    
    /* This method is used to fill up all tbe UIFields after the user
     *selects the desired row in the popup */
    public void fillData(Object  map) {
        isFilled=true;
        setModified(true);
        HashMap hash = (HashMap) map;
        if (viewType != null) {
            if (viewType.equals(ClientConstants.ACTION_STATUS[2]) ||
            viewType.equals(ClientConstants.ACTION_STATUS[3])|| viewType.equals(AUTHORIZE) ||
            viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                hash.put(CommonConstants.MAP_WHERE, hash.get("BATCH_ID"));
                hash.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                observable.populateData(hash);
                if (viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) ||
                viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                    
                    setHelpButtonEnableDisable(false);
                    
                } else {
                    
                    setHelpButtonEnableDisable(true);
                }
                setButtonEnableDisable();
                if(viewType ==  AUTHORIZE) {
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                }
            }else if(viewType.equals("InvestmentProduct")){
                
            }
            else if(viewType.equals("BATCHID")){
                //                hash.put(CommonConstants.MAP_WHERE, hash.get("BATCH_ID"));
                hash.put("value", hash.get("BATCH_ID"));
                hash.put("BATCH_ID", hash.get("BATCH_ID"));
                hash.put("CALLMONEYEXTENSION", "CALLMONEYEXTENSION");
                hash.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                hash.put("INIT_BRANCH", ProxyParameters.BRANCH_ID);
                hash.put("TRANS_DATE",currDt.clone());
                ArrayList lst=(ArrayList)ClientUtil.executeQuery("getSelectCallMoneyExtensionotherThanInsertTO", hash);
                if(lst!=null && lst.size()>0){
                    ClientUtil.displayAlert("Authorize Pending For This Batch Id");
                }else{
                    
                    observable.populateData(hash);
                }
                
            }
            if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW || observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                txtNoOfDaysExtension.setEnabled(true);
                txtExtensionInterestRate.setEnabled(true);
                txtNoOfDaysExtension.setEditable(true);
                txtExtensionInterestRate.setEditable(true);
            }
        }
        
    }
    
    private int showAlertWindow(String alertMsg) throws Exception {
        int optionSelected = 1;
        String[] options = {resourceBundle.getString("cDialogOK")};
        optionSelected = COptionPane.showOptionDialog(null,resourceBundle.getString(alertMsg), CommonConstants.INFORMATIONTITLE,
        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
        null, options, options[0]);
        return optionSelected;
    }
    /** This will do necessary operation for authorization **/
    public void authorizeStatus(String authorizeStatus) {
        if (viewType == AUTHORIZE && isFilled){
            HashMap singleAuthorizeMap = new HashMap();
            HashMap authDataMap = new HashMap();
            authDataMap.put("BATCH_ID", observable.getBatchID());
            authDataMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            authDataMap.put("NOOFDAYS", observable.getNoOfDayExtension());
            singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
            singleAuthorizeMap.put("NOOFDAYS", observable.getNoOfDayExtension());
            ArrayList arrList=new ArrayList();
            arrList.add(authDataMap);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            singleAuthorizeMap.put("CALLMONEYEXTENSION","CALLMONEYEXTENSION");
            authorize(singleAuthorizeMap);
            btnCancelActionPerformed(null);
        }else {
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            mapParam.put(CommonConstants.MAP_NAME, "getCallMoneyExtensionAuthorizeList");
            isFilled=false;
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            setModified(true);
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
        }
    }
    public void authorize(HashMap map){
        map.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        try{
            observable.setAuthorizeMap(map);
            observable.execute("AUTHORIZE");
            
        } catch(Exception e) {
            e.printStackTrace();
        }
        if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED) {
            observable.setResultStatus();
            setModified(false);
            observable.resetForm();
            resetTransactionUI();
            ClientUtil.clearAll(this);
            observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
            setButtonEnableDisable();
            setHelpButtonEnableDisable(false);
            viewType = "";
            btnReject.setEnabled(true);
            btnAuthorize.setEnabled(true);
            btnException.setEnabled(true);
        }
    }
    
    public static void main(String args[]){
        javax.swing.JFrame frame = new javax.swing.JFrame();
        InvestmentsMasterUI ui = new InvestmentsMasterUI();
        frame.getContentPane().add(ui);
        ui.setVisible(true);
        frame.setVisible(true);
        frame.setSize(600,600);
        frame.show();
        ui.show();
        
        
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        tbrShareProduct = new javax.swing.JToolBar();
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
        panCallMoneyDetails = new com.see.truetransact.uicomponent.CPanel();
        lblInterestRate = new com.see.truetransact.uicomponent.CLabel();
        txtInterestRate = new com.see.truetransact.uicomponent.CTextField();
        lblCallMoneyAmount = new com.see.truetransact.uicomponent.CLabel();
        txtCallMoneyAmount = new com.see.truetransact.uicomponent.CTextField();
        lblInterestAmount = new com.see.truetransact.uicomponent.CLabel();
        txtInterestAmount = new com.see.truetransact.uicomponent.CTextField();
        lblCallMoneyDate = new com.see.truetransact.uicomponent.CLabel();
        tdtCallMoneyDate = new com.see.truetransact.uicomponent.CDateField();
        lblNoOfDays = new com.see.truetransact.uicomponent.CLabel();
        txtNoOfDays = new com.see.truetransact.uicomponent.CTextField();
        lblCallMoneyInstituation = new com.see.truetransact.uicomponent.CLabel();
        cboCallMoneyInstituation = new com.see.truetransact.uicomponent.CComboBox();
        lblParticulars = new com.see.truetransact.uicomponent.CLabel();
        txtParticulars = new com.see.truetransact.uicomponent.CTextField();
        lblCommunication = new com.see.truetransact.uicomponent.CLabel();
        cboCommunication = new com.see.truetransact.uicomponent.CComboBox();
        lblTransDt = new com.see.truetransact.uicomponent.CLabel();
        lblTransDtValue = new com.see.truetransact.uicomponent.CLabel();
        lblBatchID = new com.see.truetransact.uicomponent.CLabel();
        panBatchID = new com.see.truetransact.uicomponent.CPanel();
        txtBatchId = new com.see.truetransact.uicomponent.CTextField();
        btnBatchID = new com.see.truetransact.uicomponent.CButton();
        lblNoOfDaysExtension = new com.see.truetransact.uicomponent.CLabel();
        txtNoOfDaysExtension = new com.see.truetransact.uicomponent.CTextField();
        lblExtensionInterestRate = new com.see.truetransact.uicomponent.CLabel();
        txtExtensionInterestRate = new com.see.truetransact.uicomponent.CTextField();
        lblExpiryDateValue = new com.see.truetransact.uicomponent.CLabel();
        lblExpiryDate = new com.see.truetransact.uicomponent.CLabel();
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
        setMaximumSize(new java.awt.Dimension(800, 400));
        setMinimumSize(new java.awt.Dimension(800, 400));
        setPreferredSize(new java.awt.Dimension(800, 400));

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
        tbrShareProduct.add(btnView);

        lblSpace5.setText("     ");
        tbrShareProduct.add(lblSpace5);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        btnNew.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                btnNewFocusLost(evt);
            }
        });
        tbrShareProduct.add(btnNew);

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareProduct.add(lblSpace29);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnEdit);

        lblSpace30.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace30.setText("     ");
        lblSpace30.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareProduct.add(lblSpace30);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnDelete);

        lblSpace2.setText("     ");
        tbrShareProduct.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnSave);

        lblSpace31.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace31.setText("     ");
        lblSpace31.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareProduct.add(lblSpace31);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnCancel);

        lblSpace3.setText("     ");
        tbrShareProduct.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnAuthorize);

        lblSpace32.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace32.setText("     ");
        lblSpace32.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace32.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace32.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareProduct.add(lblSpace32);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnException);

        lblSpace33.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace33.setText("     ");
        lblSpace33.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace33.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace33.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareProduct.add(lblSpace33);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnReject);

        lblSpace4.setText("     ");
        tbrShareProduct.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrShareProduct.add(btnPrint);

        lblSpace34.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace34.setText("     ");
        lblSpace34.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrShareProduct.add(lblSpace34);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrShareProduct.add(btnClose);

        getContentPane().add(tbrShareProduct, java.awt.BorderLayout.NORTH);

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

        panCallMoneyDetails.setMaximumSize(new java.awt.Dimension(800, 350));
        panCallMoneyDetails.setMinimumSize(new java.awt.Dimension(800, 350));
        panCallMoneyDetails.setPreferredSize(new java.awt.Dimension(800, 350));
        panCallMoneyDetails.setLayout(new java.awt.GridBagLayout());

        lblInterestRate.setText("InterestRate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panCallMoneyDetails.add(lblInterestRate, gridBagConstraints);

        txtInterestRate.setMinimumSize(new java.awt.Dimension(100, 21));
        txtInterestRate.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panCallMoneyDetails.add(txtInterestRate, gridBagConstraints);

        lblCallMoneyAmount.setText("CallMoneyAmount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panCallMoneyDetails.add(lblCallMoneyAmount, gridBagConstraints);

        txtCallMoneyAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCallMoneyAmount.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panCallMoneyDetails.add(txtCallMoneyAmount, gridBagConstraints);

        lblInterestAmount.setText("Interest Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panCallMoneyDetails.add(lblInterestAmount, gridBagConstraints);

        txtInterestAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtInterestAmount.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panCallMoneyDetails.add(txtInterestAmount, gridBagConstraints);

        lblCallMoneyDate.setText("CallMoneyDate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panCallMoneyDetails.add(lblCallMoneyDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panCallMoneyDetails.add(tdtCallMoneyDate, gridBagConstraints);

        lblNoOfDays.setText(" No Of Days");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panCallMoneyDetails.add(lblNoOfDays, gridBagConstraints);

        txtNoOfDays.setMinimumSize(new java.awt.Dimension(100, 21));
        txtNoOfDays.setEnabled(false);
        txtNoOfDays.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNoOfDaysFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panCallMoneyDetails.add(txtNoOfDays, gridBagConstraints);

        lblCallMoneyInstituation.setText("Instituation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCallMoneyDetails.add(lblCallMoneyInstituation, gridBagConstraints);

        cboCallMoneyInstituation.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panCallMoneyDetails.add(cboCallMoneyInstituation, gridBagConstraints);

        lblParticulars.setText("Particulars");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panCallMoneyDetails.add(lblParticulars, gridBagConstraints);

        txtParticulars.setMaximumSize(new java.awt.Dimension(300, 21));
        txtParticulars.setMinimumSize(new java.awt.Dimension(300, 21));
        txtParticulars.setPreferredSize(new java.awt.Dimension(300, 21));
        txtParticulars.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panCallMoneyDetails.add(txtParticulars, gridBagConstraints);

        lblCommunication.setText("Communication Mode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCallMoneyDetails.add(lblCommunication, gridBagConstraints);

        cboCommunication.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panCallMoneyDetails.add(cboCommunication, gridBagConstraints);

        lblTransDt.setText(" Trans Date :");
        lblTransDt.setMaximumSize(new java.awt.Dimension(75, 21));
        lblTransDt.setMinimumSize(new java.awt.Dimension(75, 21));
        lblTransDt.setPreferredSize(new java.awt.Dimension(75, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panCallMoneyDetails.add(lblTransDt, gridBagConstraints);

        lblTransDtValue.setMaximumSize(new java.awt.Dimension(105, 21));
        lblTransDtValue.setMinimumSize(new java.awt.Dimension(105, 21));
        lblTransDtValue.setPreferredSize(new java.awt.Dimension(105, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panCallMoneyDetails.add(lblTransDtValue, gridBagConstraints);

        lblBatchID.setText("Batch ID");
        lblBatchID.setMaximumSize(new java.awt.Dimension(75, 21));
        lblBatchID.setMinimumSize(new java.awt.Dimension(75, 21));
        lblBatchID.setPreferredSize(new java.awt.Dimension(75, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panCallMoneyDetails.add(lblBatchID, gridBagConstraints);

        panBatchID.setLayout(new java.awt.GridBagLayout());

        txtBatchId.setEditable(false);
        txtBatchId.setMinimumSize(new java.awt.Dimension(100, 21));
        txtBatchId.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBatchID.add(txtBatchId, gridBagConstraints);

        btnBatchID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnBatchID.setMinimumSize(new java.awt.Dimension(21, 21));
        btnBatchID.setPreferredSize(new java.awt.Dimension(21, 21));
        btnBatchID.setEnabled(false);
        btnBatchID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatchIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panBatchID.add(btnBatchID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 4);
        panCallMoneyDetails.add(panBatchID, gridBagConstraints);

        lblNoOfDaysExtension.setText("No Of Days Extension");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panCallMoneyDetails.add(lblNoOfDaysExtension, gridBagConstraints);

        txtNoOfDaysExtension.setMinimumSize(new java.awt.Dimension(100, 21));
        txtNoOfDaysExtension.setEnabled(false);
        txtNoOfDaysExtension.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNoOfDaysExtensionFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panCallMoneyDetails.add(txtNoOfDaysExtension, gridBagConstraints);

        lblExtensionInterestRate.setText("Extension InterestRate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panCallMoneyDetails.add(lblExtensionInterestRate, gridBagConstraints);

        txtExtensionInterestRate.setMinimumSize(new java.awt.Dimension(100, 21));
        txtExtensionInterestRate.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panCallMoneyDetails.add(txtExtensionInterestRate, gridBagConstraints);

        lblExpiryDateValue.setMaximumSize(new java.awt.Dimension(150, 18));
        lblExpiryDateValue.setMinimumSize(new java.awt.Dimension(150, 18));
        lblExpiryDateValue.setPreferredSize(new java.awt.Dimension(150, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panCallMoneyDetails.add(lblExpiryDateValue, gridBagConstraints);

        lblExpiryDate.setText("Expiry Date :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panCallMoneyDetails.add(lblExpiryDate, gridBagConstraints);

        getContentPane().add(panCallMoneyDetails, java.awt.BorderLayout.CENTER);

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
    
    private void txtNoOfDaysFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNoOfDaysFocusLost
        // TODO add your handling code here:
        calculateMatDate();
    }//GEN-LAST:event_txtNoOfDaysFocusLost
    
    private void txtNoOfDaysExtensionFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNoOfDaysExtensionFocusLost
        // TODO add your handling code here:
        calculateMatDate();
    }//GEN-LAST:event_txtNoOfDaysExtensionFocusLost
    
    private void  calculateMatDate(){
        java.util.Date depDate = (java.util.Date)DateUtil.getDateMMDDYYYY(tdtCallMoneyDate.getDateValue());
        System.out.println("####calculateMatDate : "+depDate);
        if(depDate !=null){
            GregorianCalendar cal = new GregorianCalendar((depDate.getYear()+yearTobeAdded),depDate.getMonth(),depDate.getDate());
            
            if((txtNoOfDays.getText() != null) && (!txtNoOfDays.getText().equals(""))){
                cal.add(GregorianCalendar.DAY_OF_MONTH, Integer.parseInt(txtNoOfDays.getText()));
            }else{
                cal.add(GregorianCalendar.DAY_OF_MONTH, 0);
            }
            
            if((txtNoOfDaysExtension.getText() != null) && (!txtNoOfDaysExtension.getText().equals(""))){
                cal.add(GregorianCalendar.DAY_OF_MONTH, Integer.parseInt(txtNoOfDaysExtension.getText()));
            }else{
                cal.add(GregorianCalendar.DAY_OF_MONTH, 0);
            }
            lblExpiryDateValue.setText(DateUtil.getStringDate(cal.getTime()));
            
        }
    }
    private void btnBatchIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatchIDActionPerformed
        // TODO add your handling code here:
        callView("BATCHID");
    }//GEN-LAST:event_btnBatchIDActionPerformed
    
    private void btnNewFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnNewFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_btnNewFocusLost
    private void  interestMethod(boolean value){
        
        
    }
    
    
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
        //        super.removeEditLock(((ComboBoxModel)cboInvestmentBehaves.getModel()).getKeyForSelected().toString());
        observable.resetForm();
        resetTransactionUI();
        ClientUtil.clearAll(this);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        setButtonEnableDisable();
        setHelpButtonEnableDisable(false);
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
        
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        callView("Edit");
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        transactionUI.setSourceScreen("REMITISSUE");
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_EDIT);
        ClientUtil.enableDisable(panCallMoneyDetails,false);
        txtNoOfDaysExtension.setEnabled(true);
        txtExtensionInterestRate.setEnabled(true);
        txtNoOfDaysExtension.setEditable(true);
        txtExtensionInterestRate.setEditable(true);
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        setModified(false);
        savePerformed();
        observable.setTransDate((Date) currDt.clone());
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        
    }//GEN-LAST:event_btnSaveActionPerformed
    private void resetTransactionUI(){
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
    }
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.resetForm();
        setButtonEnableDisable();
        setHelpButtonEnableDisable(true);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        ClientUtil.enableDisable(panCallMoneyDetails,false);
        btnBatchID.setEnabled(true);
        lblTransDtValue.setText(CommonUtil.convertObjToStr(currDt.clone()));
        
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
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnBatchID;
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
    private com.see.truetransact.uicomponent.CComboBox cboCallMoneyInstituation;
    private com.see.truetransact.uicomponent.CComboBox cboCommunication;
    private com.see.truetransact.uicomponent.CLabel lblBatchID;
    private com.see.truetransact.uicomponent.CLabel lblCallMoneyAmount;
    private com.see.truetransact.uicomponent.CLabel lblCallMoneyDate;
    private com.see.truetransact.uicomponent.CLabel lblCallMoneyInstituation;
    private com.see.truetransact.uicomponent.CLabel lblCommunication;
    private com.see.truetransact.uicomponent.CLabel lblExpiryDate;
    private com.see.truetransact.uicomponent.CLabel lblExpiryDateValue;
    private com.see.truetransact.uicomponent.CLabel lblExtensionInterestRate;
    private com.see.truetransact.uicomponent.CLabel lblInterestAmount;
    private com.see.truetransact.uicomponent.CLabel lblInterestRate;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNoOfDays;
    private com.see.truetransact.uicomponent.CLabel lblNoOfDaysExtension;
    private com.see.truetransact.uicomponent.CLabel lblParticulars;
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
    private com.see.truetransact.uicomponent.CLabel lblTransDt;
    private com.see.truetransact.uicomponent.CLabel lblTransDtValue;
    private com.see.truetransact.uicomponent.CMenuBar mbrShareProduct;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panBatchID;
    private com.see.truetransact.uicomponent.CPanel panCallMoneyDetails;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private javax.swing.JToolBar tbrShareProduct;
    private com.see.truetransact.uicomponent.CDateField tdtCallMoneyDate;
    private com.see.truetransact.uicomponent.CTextField txtBatchId;
    private com.see.truetransact.uicomponent.CTextField txtCallMoneyAmount;
    private com.see.truetransact.uicomponent.CTextField txtExtensionInterestRate;
    private com.see.truetransact.uicomponent.CTextField txtInterestAmount;
    private com.see.truetransact.uicomponent.CTextField txtInterestRate;
    private com.see.truetransact.uicomponent.CTextField txtNoOfDays;
    private com.see.truetransact.uicomponent.CTextField txtNoOfDaysExtension;
    private com.see.truetransact.uicomponent.CTextField txtParticulars;
    // End of variables declaration//GEN-END:variables
    
}
