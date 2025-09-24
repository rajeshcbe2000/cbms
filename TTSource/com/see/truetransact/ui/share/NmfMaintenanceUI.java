/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * NmfMaintenanceUI.java
 *
 * Created on January 20, 2005, 3:03 PM
 */

package com.see.truetransact.ui.share;


import com.see.truetransact.ui.locker.lockersurrender.*;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uicomponent.CButtonGroup;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.uimandatory.UIMandatoryField;
//import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.ui.customer.CheckCustomerIdUI;
import java.util.Observable;
import java.util.Observer;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Date;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.ui.common.transaction.TransactionUI;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import java.util.List;

import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.uivalidation.CurrencyValidation;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.text.DateFormat;

public class NmfMaintenanceUI extends CInternalFrame implements Observer, UIMandatoryField{
    
    /** Vairable Declarations */
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("com.see.truetransact.ui.locker.lockersurrender.LockerSurrenderRB", ProxyParameters.LANGUAGE);
    //Creating Instance For ResourceBundle-TokenConfigRB
    private NmfMaintenanceOB observable; //Reference for the Observable Class TokenConfigOB
    private final static ClientParseException parseException = ClientParseException.getInstance();//Instance of ClientParseException to log the Exceptions
    private HashMap mandatoryMap;//Map for putting up MandatoryFields in the UI
    private String viewType = "";//Variable used to store ActionType(New,Edit,Delete)
    private final String EDIT="Edit";
    private final String AUTHORIZE="Authorize";//Variable used when btnAuthorize is clicked
   
    private int result;
    private boolean isFilled = false;
    private TransactionUI transactionUI = new TransactionUI();
    
    private Date currDt = null;
    
   
    /** Creates new form TokenConfigUI */
    public NmfMaintenanceUI() {
        currDt = ClientUtil.getCurrentDate();
        initForm();
    }
    
    /** Method which is used to initialize the form TokenConfig */
    private void initForm(){
        initComponents();
        setFieldNames();
        setObservable();
        observable.resetForm();
//        initComponentData();
//        setMandatoryHashMap();
        setMaxLengths();
//        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panNmf);
        //        ClientUtil.enableDisable(panTokenConfiguration, false);
        setButtonEnableDisable();
        HashMap hmap=new HashMap();
        List lst=ClientUtil.executeQuery("getSelectNominalMemFee", hmap);
        if(lst!=null && lst.size()>0){
            hmap=(HashMap)lst.get(0);
           txtNominalMemFee.setText(CommonUtil.convertObjToStr(hmap.get("NOMINAL_MEM_FEE")));
        }
        transactionUI.addToScreen(panTransaction);
        transactionUI.setCallingAmount(txtNominalMemFee.getText());
        observable.setTransactionOB(transactionUI.getTransactionOB());
        dtdjoiningDt.setDateValue(ClientUtil.getCurrentDateinDDMMYYYY());
        btnCustId.setEnabled(false);
        lblNominalReq.setVisible(false);
        panOperations3.setVisible(false);
        txtNominalMemFee.setEnabled(false);
    }
    
   /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        //        tblInstruction2.setName("tblInstruction2");
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
      
       
        lblSpace1.setName("lblSpace1");
        lblSpace3.setName("lblSpace3");
        lblSpace5.setName("lblSpace5");
    
        lblStatus.setName("lblStatus");
      
        mbrTokenConfig.setName("mbrTokenConfig");
        panStatus.setName("panStatus");
      
        lblNominalMemNo.setName("lblProdID");
       
        lblCustId.setName("lblCustId");
        dtdjoiningDt.setName("dtdjoiningDt");
       lblNominalMemFee.setName("lblNominalMemFee");
       
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
            observable = NmfMaintenanceOB.getInstance();
            
            observable.addObserver(this);
            observable.setTransactionOB(transactionUI.getTransactionOB());
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /*Setting model to the combobox cboTokenType  */
    private void initComponentData() {
        try{
           
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
       
        txtNominalMemFee.setText(observable.getTxtNominalMemFee());
        txtcustId.setText(observable.getTxtCustId());
        txtnominalMemNo.setText(observable.getTxtNominalMemNo());
        dtdjoiningDt.setDateValue(observable.getDtdJoiningDt());
        if(observable.getRdoNomineereqNo().equals("Y")){
         rdoNominalReqNo.setSelected(true); 
         rdoNominalReqYes.setSelected(false); 
        }if(observable.getRdoNomineeReqyes().equals("Y")){
         rdoNominalReqNo.setSelected(false); 
         rdoNominalReqYes.setSelected(true); 
        }  
        
    }
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setTxtNominalMemNo(txtnominalMemNo.getText());
        observable.setTxtNominalMemFee(txtNominalMemFee.getText());
        observable.setTxtCustId(txtcustId.getText());
        observable.setDtdJoiningDt(dtdjoiningDt.getDateValue());
        if(rdoNominalReqYes.isSelected()==true){
           observable.setRdoNomineeReqyes("Y");
           observable.setRdoNomineereqNo("N"); 
        }if(rdoNominalReqNo.isSelected()==true){
            observable.setRdoNomineereqNo("Y");
            observable.setRdoNomineeReqyes("N");
        }
              observable.setCustName(lblCustomerName.getText());
    }
    
    
/* Auto Generated Method - setMandatoryHashMap()
 
ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
 
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtcustId" ,new Boolean(true));
        mandatoryMap.put("txtNominalMemFee", new Boolean(false));
        
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
       txtNominalMemFee.setValidation(new CurrencyValidation(14,2));

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
//    private String checkMandatory(javax.swing.JComponent component){
//        return new MandatoryCheck().checkMandatory(getClass().getName(), component);
//    }
    
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
       
        if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED){
            observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
            observable.doAction(status);
             if (observable.getProxyReturnMap()!=null) {
                        if (observable.getProxyReturnMap().containsKey("CASH_TRANS_LIST") || observable.getProxyReturnMap().containsKey("TRANSFER_TRANS_LIST")) {
                            displayTransDetail(observable.getProxyReturnMap());
                            observable.setProxyReturnMap(null);
                            
                        }
                       
                    }
           btnCancelActionPerformed(null);
            transactionUI.resetObjects();
           
        }
    }
    
    //    }
    
    
    private void displayTransDetail(HashMap proxyResultMap) {
        System.out.println("@#$@@$@@@$ proxyResultMap : " +proxyResultMap);
        String cashDisplayStr = "Cash Transaction Details...\n";
        String transferDisplayStr = "Transfer Transaction Details...\n";
        String displayStr = "";
        String transId = "";
        String transType = "";
        Object keys[] = proxyResultMap.keySet().toArray();
        int cashCount = 0;
        int transferCount = 0;
        List tempList = null;
        HashMap transMap = null;
        String actNum = "";
       
    
        for (int i=0; i<keys.length; i++) {
            if (proxyResultMap.get(keys[i]) instanceof String) {
                continue;
            }
            tempList = (List)proxyResultMap.get(keys[i]);
            if (CommonUtil.convertObjToStr(keys[i]).indexOf("CASH")!=-1) {
                for (int j=0; j<tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j==0) {
                        transId = (String)transMap.get("TRANS_ID");
                    }
                    cashDisplayStr += "Trans Id : "+transMap.get("TRANS_ID")+
                    "   Trans Type : "+transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if(actNum != null && !actNum.equals("")){
                        cashDisplayStr +="   Account No : "+transMap.get("ACT_NUM")+
                        "   Amount : "+transMap.get("AMOUNT")+"\n";
                    }else{
                        cashDisplayStr += "   Ac Hd Desc : "+transMap.get("AC_HD_ID")+
                        "   Amount : "+transMap.get("AMOUNT")+"\n";
                    }
                }
                cashCount++;
            } else if (CommonUtil.convertObjToStr(keys[i]).indexOf("TRANSFER")!=-1) {
                for (int j=0; j<tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j==0) {
                        transId = (String)transMap.get("BATCH_ID");
                    }
                    transferDisplayStr += "Trans Id : "+transMap.get("TRANS_ID")+
                    "   Batch Id : "+transMap.get("BATCH_ID")+
                    "   Trans Type : "+transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if(actNum != null && !actNum.equals("")){
                        transferDisplayStr +="   Account No : "+transMap.get("ACT_NUM")+
                        "   Amount : "+transMap.get("AMOUNT")+"\n";
                    }else{
                        transferDisplayStr += "   Ac Hd Desc : "+transMap.get("AC_HD_ID")+
                        "   Amount : "+transMap.get("AMOUNT")+"\n";
                    }
                }
                transferCount++;
            }
        }
        
        if(cashCount>0){
            displayStr+=cashDisplayStr;
        }
        if(transferCount>0){
            displayStr+=transferDisplayStr;
        }
        if(!displayStr.equals("")) {
            ClientUtil.showMessageWindow(""+displayStr);
        }
        //        ClientUtil.showMessageWindow(""+displayStr);
        
//        int yesNo = 0;
//        String[] options = {"Yes", "No"};
//        yesNo = COptionPane.showOptionDialog(null,"Do you want to print?", CommonConstants.WARNINGTITLE,
//        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
//        null, options, options[0]);
//        System.out.println("#$#$$ yesNo : "+yesNo);
//        if (yesNo==0) {
//            TTIntegration ttIntgration = null;
//            HashMap paramMap = new HashMap();
//            paramMap.put("TransId", transId);
//            paramMap.put("TransDt", observable.getCurDate());
//            paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
//            ttIntgration.setParam(paramMap);
//            //            if (((String)TrueTransactMain.BANKINFO.get("BANK_NAME")).toUpperCase().lastIndexOf("POLPULLY")!=-1) {
//            //                ttIntgration.integrationForPrint("ReceiptPayment");
//            //            } else {
//            String reportName = "";
//            if(transferCount>0){
//                reportName = "ReceiptPayment";
//            } else if (observable.getTxtShareDetShareNoFrom().equals("WITHDRAWAL")) {
//                reportName = "CashPayment";
//            } else {
//                reportName = "CashReceipt";
//            }
//            ttIntgration.integrationForPrint(reportName, false);
//        }
    }
    /* set the screen after the updation,insertion, deletion */
    private void settings(){
        observable.resetForm();
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panNmf, false, false, true);
        setButtonEnableDisable();
        observable.setResultStatus();
      
        
        transactionUI.setCallingAmount("0");
       
//        panTransaction.setVisible(false);
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
        //        ArrayList lst = new ArrayList();
        //        lst.add("CONFIG_ID");
        //        viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
        //        lst = null;
        if(viewType.equals("EDIT")){
            viewMap.put(CommonConstants.MAP_NAME, "getSelectNmfEdit");
            HashMap where = new HashMap();
            where.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, where);
            where = null;
        }else if(viewType.equals("DELETE")){
            viewMap.put(CommonConstants.MAP_NAME, "getSelectNmfDelete");
            HashMap where = new HashMap();
            where.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, where);
            where = null;
        }
        new ViewAll(this,viewMap).show();
       
    }
    private void removeRadioButtons() {
        
    }
    
    private void addRadioButtons() {// these r all radio button purpose adding...
      
    }
    /* Fills up the HashMap with data when user selects the row in ViewAll screen  */
    public void fillData(Object  map) {
        setModified(true);
        HashMap hash = (HashMap) map;
        System.out.println("hash#####"+hash);
        System.out.println("viewType#####"+viewType);
         if (viewType.equals("CUSTOMER_ID")) {
               txtcustId.setText(CommonUtil.convertObjToStr(hash.get("CUST_ID")));
               lblCustomerName.setText(CommonUtil.convertObjToStr(hash.get("NAME")));
               transactionUI.setCallingApplicantName(lblCustomerName.getText());
         }else if(viewType.equals("EDIT") || viewType.equals("DELETE") || viewType.equals("Authorize")) {
             txtnominalMemNo.setText(CommonUtil.convertObjToStr(hash.get("NOMINAL_MEM_NO")));
             txtNominalMemFee.setText(CommonUtil.convertObjToStr(hash.get("NOMINAL_MEM_FEE")));
             txtcustId.setText(CommonUtil.convertObjToStr(hash.get("CUST_ID")));
             dtdjoiningDt.setDateValue(CommonUtil.convertObjToStr(hash.get("OPENING_DT")));
             lblCustomerName.setText(CommonUtil.convertObjToStr(hash.get("CUST_NAME")));
             String nomineeReq=CommonUtil.convertObjToStr(hash.get("NOMINEE_REQ"));
             if(nomineeReq.equals("Y")){
                 rdoNominalReqYes.setSelected(true);
                 rdoNominalReqNo.setSelected(false);
             }else{
                  rdoNominalReqYes.setSelected(false);
                 rdoNominalReqNo.setSelected(true);
             }
             hash.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
            observable.populateData(hash);
            isFilled=true;
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
    
    public void authorizeStatus(String authorizeStatus) {
        if (!isFilled ){
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            mapParam.put(CommonConstants.MAP_NAME, "getSelectNmfAuthorize");
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            whereMap.put(CommonConstants.HIERARCHY_ID, ProxyParameters.HIERARCHY_ID);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
            btnSave.setEnabled(false);     
            }else if (isFilled){
            isFilled = false;
             HashMap authDataMap = new HashMap();
             authDataMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
             authDataMap.put("NOMINAL_MEM_NO", txtnominalMemNo.getText());
             authDataMap.put("CUST_ID",txtcustId.getText());
             ArrayList arrList = new ArrayList();
             HashMap singleAuthorizeMap = new HashMap();
             arrList.add(authDataMap);
             singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
             singleAuthorizeMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
             singleAuthorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
             authorize(singleAuthorizeMap);
          
        }
    }
    
    
    public void authorize(HashMap map) {
        map.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
        //        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        observable.setAuthorizeMap(map); 
        observable.doAction("Authorize");
        if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED) {
            
            observable.resetForm();
            btnCancelActionPerformed(null);
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
        }
        
    }
    
    
    /** Method used to do Required operation when user clicks btnAuthorize,btnReject or btnReject **/
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdgSurOrRenew = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrTokenConfig = new com.see.truetransact.uicomponent.CToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lbSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace65 = new com.see.truetransact.uicomponent.CLabel();
        lblSpace66 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lbSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace67 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace68 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace69 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace70 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        panAccountInfo = new com.see.truetransact.uicomponent.CPanel();
        panNmf = new com.see.truetransact.uicomponent.CPanel();
        lblCustId = new com.see.truetransact.uicomponent.CLabel();
        btnCustId = new com.see.truetransact.uicomponent.CButton();
        lblNominalMemNo = new com.see.truetransact.uicomponent.CLabel();
        txtnominalMemNo = new javax.swing.JTextField();
        lblNominalMemFee = new com.see.truetransact.uicomponent.CLabel();
        txtNominalMemFee = new com.see.truetransact.uicomponent.CTextField();
        dtdjoiningDt = new com.see.truetransact.uicomponent.CDateField();
        lbljoiningDt = new com.see.truetransact.uicomponent.CLabel();
        lblNominalReq = new com.see.truetransact.uicomponent.CLabel();
        panOperations3 = new com.see.truetransact.uicomponent.CPanel();
        rdoNominalReqYes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoNominalReqNo = new com.see.truetransact.uicomponent.CRadioButton();
        lblCustomerName = new com.see.truetransact.uicomponent.CLabel();
        txtcustId = new com.see.truetransact.uicomponent.CTextField();
        panTransaction = new com.see.truetransact.uicomponent.CPanel();
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
        setMinimumSize(new java.awt.Dimension(800, 550));
        setPreferredSize(new java.awt.Dimension(800, 550));

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setEnabled(false);
        btnView.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView.setPreferredSize(new java.awt.Dimension(21, 21));
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

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnEdit);

        lblSpace65.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace65.setText("     ");
        lblSpace65.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace65.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace65.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace65);

        lblSpace66.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace66.setText("     ");
        lblSpace66.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace66.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace66.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace66);

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

        lblSpace67.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace67.setText("     ");
        lblSpace67.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace67.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace67.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace67);

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

        lblSpace68.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace68.setText("     ");
        lblSpace68.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace68.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace68.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace68);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrTokenConfig.add(btnException);

        lblSpace69.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace69.setText("     ");
        lblSpace69.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace69.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace69.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace69);

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

        lblSpace70.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace70.setText("     ");
        lblSpace70.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace70.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace70.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTokenConfig.add(lblSpace70);

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

        panNmf.setMinimumSize(new java.awt.Dimension(350, 195));
        panNmf.setPreferredSize(new java.awt.Dimension(550, 558));
        panNmf.setLayout(new java.awt.GridBagLayout());

        lblCustId.setText("Customer Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panNmf.add(lblCustId, gridBagConstraints);

        btnCustId.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnCustId.setToolTipText("Customer Data");
        btnCustId.setMinimumSize(new java.awt.Dimension(25, 25));
        btnCustId.setPreferredSize(new java.awt.Dimension(25, 25));
        btnCustId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panNmf.add(btnCustId, gridBagConstraints);

        lblNominalMemNo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblNominalMemNo.setText("Nominal Membership No");
        lblNominalMemNo.setMaximumSize(new java.awt.Dimension(200, 18));
        lblNominalMemNo.setMinimumSize(new java.awt.Dimension(280, 18));
        lblNominalMemNo.setPreferredSize(new java.awt.Dimension(150, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panNmf.add(lblNominalMemNo, gridBagConstraints);

        txtnominalMemNo.setEditable(false);
        txtnominalMemNo.setEnabled(false);
        txtnominalMemNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtnominalMemNo.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        panNmf.add(txtnominalMemNo, gridBagConstraints);

        lblNominalMemFee.setText("NominalMember Fee");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panNmf.add(lblNominalMemFee, gridBagConstraints);

        txtNominalMemFee.setAllowNumber(true);
        txtNominalMemFee.setMinimumSize(new java.awt.Dimension(100, 21));
        txtNominalMemFee.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNominalMemFeeFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panNmf.add(txtNominalMemFee, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 0);
        panNmf.add(dtdjoiningDt, gridBagConstraints);

        lbljoiningDt.setText("Date Of Joining");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panNmf.add(lbljoiningDt, gridBagConstraints);

        lblNominalReq.setText("Nominal Required");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panNmf.add(lblNominalReq, gridBagConstraints);

        panOperations3.setLayout(new java.awt.GridBagLayout());

        rdoNominalReqYes.setText("Yes");
        rdoNominalReqYes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoNominalReqYesActionPerformed(evt);
            }
        });
        rdoNominalReqYes.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                rdoNominalReqYesFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 19;
        panOperations3.add(rdoNominalReqYes, gridBagConstraints);

        rdoNominalReqNo.setText("No");
        rdoNominalReqNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoNominalReqNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 19;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panOperations3.add(rdoNominalReqNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panNmf.add(panOperations3, gridBagConstraints);

        lblCustomerName.setForeground(new java.awt.Color(0, 51, 204));
        lblCustomerName.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblCustomerName.setMinimumSize(new java.awt.Dimension(95, 18));
        lblCustomerName.setPreferredSize(new java.awt.Dimension(95, 18));
        lblCustomerName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                lblCustomerNameFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 1);
        panNmf.add(lblCustomerName, gridBagConstraints);

        txtcustId.setAllowNumber(true);
        txtcustId.setMinimumSize(new java.awt.Dimension(100, 21));
        txtcustId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtcustIdFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panNmf.add(txtcustId, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panAccountInfo.add(panNmf, gridBagConstraints);

        panTransaction.setMinimumSize(new java.awt.Dimension(700, 400));
        panTransaction.setPreferredSize(new java.awt.Dimension(700, 400));
        panTransaction.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panAccountInfo.add(panTransaction, gridBagConstraints);

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
            
    
    
    
    
                    
  
    
            
 
       
    
    private void btnCustIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustIdActionPerformed
        // TODO add your handling code here:
        
         viewType = "CUSTOMER_ID";           
         new CheckCustomerIdUI(this);
    }//GEN-LAST:event_btnCustIdActionPerformed
    
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
        txtNominalMemFee.setEnabled(false);
        dtdjoiningDt.setEnabled(false);
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
                observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
                txtNominalMemFee.setEnabled(false);
                dtdjoiningDt.setEnabled(false);
                
        //        txtCharges.setEnabled(false);
        //        txtServiceTax.setEnabled(false);
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
        //        super.removeEditLock(txtTokenConfigId.getText());
        setModified(false);
       
        observable.resetForm();
        //        txtNoOfTokens.setText("");
        ClientUtil.clearAll(this);
        //        ClientUtil.enableDisable(panTokenConfiguration, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        setButtonEnableDisable();
        viewType = "";
        settings();
        setModified(false);
        transactionUI.setButtonEnableDisable(true);
//        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
        transactionUI.setMainEnableDisable(false);
        //         panLockerOut.setVisible(false);
      
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        btnNew.setEnabled(true);
        btnSave.setEnabled(true);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
        btnCancel.setEnabled(false);
        btnSave.setEnabled(false);
        lblCustomerName.setText("");
        isFilled = false;
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        callView("DELETE");
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnNew.setEnabled(false);
        btnSave.setEnabled(true);
        btnCancel.setEnabled(true);
        btnEdit.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        callView("EDIT");
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        ClientUtil.enableDisable(panNmf, false);
        rdoNominalReqYes.setEnabled(true);
        rdoNominalReqNo.setEnabled(true);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnSave.setEnabled(true);
        btnCancel.setEnabled(true);
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        setModified(false);
        String mandatoryMessage="";
//        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(),panAccountInfo);
        if (txtcustId.getText().length()==0) {
            displayAlert("Please Enter The Customer Id");
            return;
        }
      
            // To check the transaction amounts tallied...
            java.util.LinkedHashMap transMap = transactionUI.getOutputTO();
            if (transMap!=null && transMap.size()>0) {
                Object[] objKeys = transMap.keySet().toArray();
                TransactionTO objTransactionTO = null;
                double transAmt = 0;
                for (int i=0; i<objKeys.length; i++) {
                    objTransactionTO = (TransactionTO) transMap.get(objKeys[i]);
                    transAmt = transAmt+objTransactionTO.getTransAmt().doubleValue();
                }
                System.out.println("#$#$ TotalAmount : "+txtNominalMemFee.getText()+" / TransAmt : "+transAmt);
                if (CommonUtil.convertObjToDouble(txtNominalMemFee.getText()).doubleValue() != transAmt) {
                    mandatoryMessage+=resourceBundle.getString("TRANSAMTWARNING")+"\n";
                }
            } else {
                mandatoryMessage+=resourceBundle.getString("NOTRANSWARNING")+"\n";
            }
        
        if (mandatoryMessage.length()>0) {
            displayAlert(mandatoryMessage);
        } else {
            int transactionSize = 0 ;
            if(transactionUI.getOutputTO() == null){           
                    ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS)) ;
                    return;               
            }
            else{
                transactionSize = (transactionUI.getOutputTO()).size();
                observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
            }
            savePerformed();
            isFilled=false;
            btnAuthorize.setEnabled(true);
            btnReject.setEnabled(true);
            btnException.setEnabled(true);
        }
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // TODO add your handling code here:
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        setModified(true);
        observable.resetForm();
        //        txtNoOfTokens.setText("");
        //        ClientUtil.enableDisable(panTokenConfiguration, true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnCustId.setEnabled(true);
        //        ClientUtil.enableDisable(panStdInstructions, true);
        //        ClientUtil.enableDisable(panOperations, false);
        btnCustId.setEnabled(true);
        transactionUI.cancelAction(false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.resetObjects();
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
        ClientUtil.enableDisable(panNmf, true, false, true);
        
        txtnominalMemNo.setEnabled(false);
        txtnominalMemNo.setEditable(false);
        HashMap hmap=new HashMap();
        List lst=ClientUtil.executeQuery("getSelectNominalMemFee", hmap);
        if(lst!=null && lst.size()>0){
            hmap=(HashMap)lst.get(0);
           txtNominalMemFee.setText(CommonUtil.convertObjToStr(hmap.get("NOMINAL_MEM_FEE")));
        }        
        dtdjoiningDt.setDateValue(ClientUtil.getCurrentDateinDDMMYYYY());
        transactionUI.setCallingAmount(txtNominalMemFee.getText());
        txtNominalMemFee.setEnabled(false);
        
    }//GEN-LAST:event_btnNewActionPerformed

    private void txtNominalMemFeeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNominalMemFeeFocusLost
        // TODO add your handling code here:
        transactionUI.setCallingApplicantName(lblCustomerName.getText());
        transactionUI.setCallingAmount(txtNominalMemFee.getText());
    }//GEN-LAST:event_txtNominalMemFeeFocusLost

    private void rdoNominalReqYesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoNominalReqYesActionPerformed
        if (rdoNominalReqYes.isSelected() == true) {         
            rdoNominalReqYes.setSelected(true);           
            rdoNominalReqNo.setSelected(false);
           
            //            tabTermDeposit.add(nomineeUi);
//            tabTermDeposit.add(nomineeUi,"Nominee");
//            tabTermDeposit.resetVisits();
//            ClientUtil.enableDisable(nomineeUi, true);
//            nomineeUi.resetNomineeTab();
//            nomineeUi.enableDisableNominee_SaveDelete();
////            nomineeUi.setStatusAsMajor();
//            nomineeUi.setMainCustomerId(txtCustomerId.getText());
//            nomineeUi.setCustomerList(getCustomerList());
        
        }
        }//GEN-LAST:event_rdoNominalReqYesActionPerformed

        private void rdoNominalReqYesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rdoNominalReqYesFocusLost

    }//GEN-LAST:event_rdoNominalReqYesFocusLost

    private void rdoNominalReqNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoNominalReqNoActionPerformed
        // TODO add your handling code here:
        if (rdoNominalReqNo.isSelected() == true) {
            rdoNominalReqNo.setSelected(true);
            rdoNominalReqYes.setSelected(false);
        }
        }//GEN-LAST:event_rdoNominalReqNoActionPerformed

        private void lblCustomerNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_lblCustomerNameFocusLost
        
    }//GEN-LAST:event_lblCustomerNameFocusLost

    private void txtcustIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtcustIdFocusLost
        // TODO add your handling code here:
        String custid= txtcustId.getText();
        HashMap hmap=new HashMap();
        hmap.put("CUST_ID",custid);
        List lst=ClientUtil.executeQuery("getCustName", hmap);
        if(lst!=null && lst.size()>0){
        hmap=null;
        hmap=(HashMap)lst.get(0);
        String name=CommonUtil.convertObjToStr(hmap.get("NAME"));
        lblCustomerName.setText(name);
        }else{
            ClientUtil.displayAlert("Invalid Customer No");
            txtcustId.setText("");
        }
    }//GEN-LAST:event_txtcustIdFocusLost
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
    private com.see.truetransact.uicomponent.CButton btnCustId;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CDateField dtdjoiningDt;
    private com.see.truetransact.uicomponent.CLabel lbSpace2;
    private com.see.truetransact.uicomponent.CLabel lbSpace3;
    private com.see.truetransact.uicomponent.CLabel lblCustId;
    private com.see.truetransact.uicomponent.CLabel lblCustomerName;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNominalMemFee;
    private com.see.truetransact.uicomponent.CLabel lblNominalMemNo;
    private com.see.truetransact.uicomponent.CLabel lblNominalReq;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace65;
    private com.see.truetransact.uicomponent.CLabel lblSpace66;
    private com.see.truetransact.uicomponent.CLabel lblSpace67;
    private com.see.truetransact.uicomponent.CLabel lblSpace68;
    private com.see.truetransact.uicomponent.CLabel lblSpace69;
    private com.see.truetransact.uicomponent.CLabel lblSpace70;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lbljoiningDt;
    private com.see.truetransact.uicomponent.CMenuBar mbrTokenConfig;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAccountInfo;
    private com.see.truetransact.uicomponent.CPanel panNmf;
    private com.see.truetransact.uicomponent.CPanel panOperations3;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTransaction;
    private com.see.truetransact.uicomponent.CButtonGroup rdgSurOrRenew;
    private com.see.truetransact.uicomponent.CRadioButton rdoNominalReqNo;
    private com.see.truetransact.uicomponent.CRadioButton rdoNominalReqYes;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptView;
    private com.see.truetransact.uicomponent.CToolBar tbrTokenConfig;
    private com.see.truetransact.uicomponent.CTextField txtNominalMemFee;
    private com.see.truetransact.uicomponent.CTextField txtcustId;
    private javax.swing.JTextField txtnominalMemNo;
    // End of variables declaration//GEN-END:variables
    public long getNearest(long number,long roundingFactor)  {
        long roundingFactorOdd = roundingFactor;
        if ((roundingFactor%2) != 0) {
            roundingFactorOdd +=1;
        }
        long mod = number%roundingFactor;
        if ((mod < (roundingFactor/2)) || (mod < (roundingFactorOdd/2))) {
            return lower(number,roundingFactor);
        } else {
            return higher(number,roundingFactor);
        }
    }
    
    public long lower(long number,long roundingFactor) {
        long mod = number%roundingFactor;
        return number-mod;
    }
    
    public long higher(long number,long roundingFactor) {
        long mod = number%roundingFactor;
        if ( mod == 0) {
            return number;
        }
        return (number-mod) + roundingFactor ;
    }
    
//    /**
//     * Getter for property surrenderID.
//     * @return Value of property surrenderID.
//     */
//    public java.lang.String getSurrenderID() {
//        return surrenderID;
//    }
//    
//    /**
//     * Setter for property surrenderID.
//     * @param surrenderID New value of property surrenderID.
//     */
//    public void setSurrenderID(java.lang.String surrenderID) {
//        this.surrenderID = surrenderID;
//    }
    
}
