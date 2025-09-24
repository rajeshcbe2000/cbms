/**
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareProductUI.java
 *
 * Created on November 23, 2004, 4:00 PM
 */

package com.see.truetransact.ui.termloan.guarantee;

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
import com.see.truetransact.ui.termloan.guarantee.GuaranteeMasterMRB;
import com.see.truetransact.ui.common.transaction.TransactionUI;

public class GuaranteeMasterUI extends CInternalFrame implements UIMandatoryField, Observer{
    
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private HashMap mandatoryMap;
    private GuaranteeMasterOB observable;
    private GuaranteeMasterMRB objMandatoryRB;
    private ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.termloan.guarantee.GuaranteeMasterRB", ProxyParameters.LANGUAGE);
    private String viewType = new String();
    final String AUTHORIZE="Authorize";
    private int yearTobeAdded = 1900;
    private TransactionUI transactionUI = new TransactionUI();
    private int transactionSize=0;
    
    /** Creates new form ShareProductUI */
    public GuaranteeMasterUI() {
        initUIComponents();
    }
    
    /** Initialsises the UIComponents */
    private void initUIComponents(){
        initComponents();
        setFieldNames();
        setMaxLength();
        internationalize();
        setMandatoryHashMap();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panMain, getMandatoryHashMap());
        setHelpMessage();
        setObservable();
        observable.resetForm();
        initComponentData();
        ClientUtil.enableDisable(panShare, true);
        ClientUtil.enableDisable(panInvestmentDetails, true);
        transactionUI.addToScreen(panTransaction);
        observable.setTransactionOB(transactionUI.getTransactionOB());
        panTransaction.setName("Transaction Details");
        setButtonEnableDisable();
        panInvestmentDetails.setVisible(true);
        ClientUtil.enableDisable(panMain,false);
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
        lblPli.setName("lblPli");
        lblPliBranch.setName("lblPliBranch");
        lblCustId.setName("lblCustId");
        lblSanctionNo.setName("lblSanctionNo");
        lblSanctionDt.setName("lblSanctionDt");
        lblLoanNo.setName("lblLoanNo");
        lblLoanDt.setName("lblLoanDt");
        lblLoanAmount.setName("lblLoanAmount");
        lblHolidayPeriod.setName("lblHolidayPeriod");
        lblNoOfInstallments.setName("lblNoOfInstallments");
        lblRepaymentFrequency.setName("lblRepaymentFrequency");
        lblInterestType.setName("lblInterestType");
        lblGuaranteeNo.setName("lblGuaranteeNo");
        lblGuaranteeDt.setName("lblGuaranteeDt");
        lblGuaranteeSanctionedBy.setName("lblGuaranteeSanctionedBy");
        lblGuaranteeSanctionNo.setName("lblGuaranteeSanctionNo");
        lblGuaranteeAmount.setName("lblGuaranteeAmount");
        lblGuaranteeFeePayBy.setName("lblGuaranteeFeePayBy");
        lblGuaranteeFeePer.setName("lblGuaranteeFeePer");
        lblGuaranteeFee.setName("lblGuaranteeFee");
        cboPli.setName("cboPli");
        cboPliBranch.setName("cboPliBranch");
        txtCustId.setName("txtCustId");
        txtSanctionNo.setName("txtSanctionNo");
        tdtSanctionDt.setName("tdtSanctionDt");
        txtLoanNo.setName("txtLoanNo");
        tdtLoanDt.setName("tdtLoanDt");
        txtLoanAmount.setName("txtLoanAmount");
        txtHolidayPeriod.setName("txtHolidayPeriod");
        txtNoOfInstallments.setName("txtNoOfInstallments");
        cboRepaymentFrequency.setName("cboRepaymentFrequency");
        //        cboInterestType.setName("cboInterestType");
        //        txtGuaranteeNo.setName("txtGuaranteeNo");
        tdtGuaranteeDt.setName("tdtGuaranteeDt");
        cboGuaranteeSanctionedBy.setName("cboGuaranteeSanctionedBy");
        txtGuaranteeSanctionNo.setName("txtGuaranteeSanctionNo");
        txtGuaranteeAmount.setName("txtGuaranteeAmount");
        rdoCustomer.setName("rdoCustomer");
        rdoPLI.setName("rdoPLI");
        txtGuaranteeFeePer.setName("txtGuaranteeFeePer");
        txtGuaranteeFee.setName("txtGuaranteeFee");
        
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
        lblPli.setText(resourceBundle.getString("lblPli"));
        lblPliBranch.setText(resourceBundle.getString("lblPliBranch"));
        lblCustId.setText(resourceBundle.getString("lblCustId"));
        lblSanctionNo.setText(resourceBundle.getString("lblSanctionNo"));
        lblSanctionDt.setText(resourceBundle.getString("lblSanctionDt"));
        lblLoanNo.setText(resourceBundle.getString("lblLoanNo"));
        lblLoanDt.setText(resourceBundle.getString("lblLoanDt"));
        lblLoanAmount.setText(resourceBundle.getString("lblLoanAmount"));
        lblHolidayPeriod.setText(resourceBundle.getString("lblHolidayPeriod"));
        lblNoOfInstallments.setText(resourceBundle.getString("lblNoOfInstallments"));
        lblRepaymentFrequency.setText(resourceBundle.getString("lblRepaymentFrequency"));
        lblInterestType.setText(resourceBundle.getString("lblInterestType"));
        lblGuaranteeNo.setText(resourceBundle.getString("lblGuaranteeNo"));
        lblGuaranteeDt.setText(resourceBundle.getString("lblGuaranteeDt"));
        lblGuaranteeSanctionedBy.setText(resourceBundle.getString("lblGuaranteeSanctionedBy"));
        lblGuaranteeSanctionNo.setText(resourceBundle.getString("lblGuaranteeSanctionNo"));
        lblGuaranteeAmount.setText(resourceBundle.getString("lblGuaranteeAmount"));
        lblGuaranteeFeePayBy.setText(resourceBundle.getString("lblGuaranteeFeePayBy"));
        lblGuaranteeFeePer.setText(resourceBundle.getString("lblGuaranteeFeePer"));
        lblGuaranteeFee.setText(resourceBundle.getString("lblGuaranteeFee"));
        
        
    }
    
/* Auto Generated Method - setMandatoryHashMap()
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboPli", new Boolean(true));
        mandatoryMap.put("txtCustId", new Boolean(true));
        mandatoryMap.put("txtSanctionNo", new Boolean(true));
        mandatoryMap.put("tdtSanctionDt", new Boolean(true));
        mandatoryMap.put("txtLoanNo", new Boolean(true));
        mandatoryMap.put("tdtLoanDt", new Boolean(true));
        mandatoryMap.put("txtLoanAmount", new Boolean(true));
        mandatoryMap.put("txtNoOfInstallments", new Boolean(true));
        mandatoryMap.put("cboRepaymentFrequency", new Boolean(true));
        mandatoryMap.put("txtInterestRate", new Boolean(true));
        mandatoryMap.put("tdtGuaranteeDt", new Boolean(true));
        mandatoryMap.put("txtGuaranteeSanctionNo", new Boolean(true));
        mandatoryMap.put("txtGuaranteeAmount", new Boolean(true));
        mandatoryMap.put("txtGuaranteeFeePer", new Boolean(true));
        mandatoryMap.put("txtGuaranteeFee", new Boolean(true));
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
            observable = GuaranteeMasterOB.getInstance();
            observable.addObserver(this);
            observable.setTransactionOB(transactionUI.getTransactionOB());
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /* Sets the model for the comboboxes in the UI    */
    private void initComponentData() {
        try{
            //            cboPli.setModel(observable.getCbmInvestmentBehaves());
            //            cboPliBranch.setModel(observable.getCbmIntPayFreq());
            cboRepaymentFrequency.setModel(observable.getCbmRepaymentfrequency());
            cboGuaranteeSanctionedBy.setModel(observable.getCbmGuranteeSanctionBy());
            cboPli.setModel(observable.getCbmPli());
            
        }catch(ClassCastException e){
            parseException.logException(e,true);
        }
    }
    
/* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        objMandatoryRB = new GuaranteeMasterMRB();
        cboPli.setHelpMessage(lblMsg, objMandatoryRB.getString("cboPli"));
        txtCustId.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCustId"));
        txtSanctionNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSanctionNo"));
        tdtSanctionDt.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtSanctionDt"));
        txtLoanNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLoanNo"));
        tdtLoanDt.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtLoanDt"));
        txtLoanAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLoanAmount"));
        txtNoOfInstallments.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNoOfInstallments"));
        cboRepaymentFrequency.setHelpMessage(lblMsg, objMandatoryRB.getString("cboRepaymentFrequency"));
        txtInterestRate.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInterestRate"));
        tdtGuaranteeDt.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtGuaranteeDt"));
        txtGuaranteeSanctionNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtGuaranteeSanctionNo"));
        txtGuaranteeAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtGuaranteeAmount"));
        txtGuaranteeFeePer.setHelpMessage(lblMsg, objMandatoryRB.getString("txtGuaranteeFeePer"));
        txtGuaranteeFee.setHelpMessage(lblMsg, objMandatoryRB.getString("txtGuaranteeFee"));
        
    }
    
    
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        cboPli.setSelectedItem(observable.getCboPli());
        //        ((ComboBoxModel)cboPli.getModel()).setKeyForSelected(observable.getCbmPli().getKeyForSelected());
        //        ((ComboBoxModel)cboPliBranch.getModel()).setKeyForSelected(observable.getCbopliBranch());
        cboPliBranch.setSelectedItem(observable.getCbopliBranch());
        txtCustId.setText(observable.getCustId());
        txtSanctionNo.setText(observable.getSanctionNo());
        tdtSanctionDt.setDateValue(DateUtil.getStringDate(observable.getSanctionDt()));
        txtLoanNo.setText(observable.getLoanNo());
        tdtLoanDt.setDateValue(DateUtil.getStringDate(observable.getLoanDt()));
        txtLoanAmount.setText(observable.getSanctionAmt());
        txtHolidayPeriod.setText(observable.getHolidayPeriod());
        txtNoOfInstallments.setText(observable.getNoOfInst());
        cboRepaymentFrequency.setSelectedItem(observable.getCboRepaymentFrequency());
        txtInterestRate.setText(observable.getIntRate());
        lblGuranteNoValue.setText(observable.getGuaranteeNo());
        tdtGuaranteeDt.setDateValue(DateUtil.getStringDate(observable.getGuaranteeDt()));
        cboGuaranteeSanctionedBy.setSelectedItem(observable.getCboGuranteeSanctionBy());
        txtGuaranteeSanctionNo.setText(observable.getGuaranteeSanctionNo());
        if(CommonUtil.convertObjToStr(observable.getGuranteFeepayBy()).equals("CUSTOMER"))
            rdoCustomer.setSelected(true);
        else
            rdoPLI.setSelected(true);
        txtGuaranteeFeePer.setText(observable.getGuranteFeePer());
        txtGuaranteeFee.setText(observable.getGuranteFee());
        txtGuaranteeAmount.setText(observable.getGuranteAmt());
         tblAmortizationDetails.setModel(observable.getTblInvestmentTransDet());
    }
    
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setCboPli(((ComboBoxModel)cboPli.getModel()).getKeyForSelected().toString());
//        observable.setCboPli(((ComboBoxModel)cboPli.getModel()).getKeyForSelected().toString());
         observable.setCbopliBranch(CommonUtil.convertObjToStr(((ComboBoxModel)cboPliBranch.getModel()).getKeyForSelected().toString()));
        observable.setCustId(txtCustId.getText());
        observable.setSanctionNo(txtSanctionNo.getText());
        observable.setSanctionDt(DateUtil.getDateMMDDYYYY(tdtSanctionDt.getDateValue()));
        observable.setLoanNo(txtLoanNo.getText());
        observable.setLoanDt(DateUtil.getDateMMDDYYYY(tdtLoanDt.getDateValue()));
        observable.setSanctionAmt(txtLoanAmount.getText());
        observable.setHolidayPeriod(txtHolidayPeriod.getText());
        observable.setNoOfInst(txtNoOfInstallments.getText());
        observable.setCboRepaymentFrequency(((ComboBoxModel)cboRepaymentFrequency.getModel()).getKeyForSelected().toString());
        observable.setIntRate(txtInterestRate.getText());
        observable.setGuaranteeNo(lblGuranteNoValue.getText());
        observable.setGuaranteeDt(DateUtil.getDateMMDDYYYY(tdtGuaranteeDt.getDateValue()));
        observable.setCboGuranteeSanctionBy(((ComboBoxModel)cboGuaranteeSanctionedBy.getModel()).getKeyForSelected().toString());
        observable.setGuaranteeSanctionNo(txtGuaranteeSanctionNo.getText());
        observable.setGuranteAmt(txtGuaranteeAmount.getText());
        if(rdoCustomer.isSelected()==true){
            observable.setGuranteFeepayBy("CUSTOMER");
            rdoPLI.setSelected(false);
        }else{
            observable.setGuranteFeepayBy("PLI");
            rdoCustomer.setSelected(false);
        }
        observable.setGuranteFeePer(txtGuaranteeFeePer.getText());
        observable.setGuranteFee(txtGuaranteeFee.getText());
        
        
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
        //        callEnabledisableTxt();
        
    }
    
    /** Setting up Lengths for the TextFields in theu UI */
    private void setMaxLength(){
        txtLoanAmount.setValidation(new NumericValidation(16,2));
        txtSanctionNo.setMaxLength(50);
        txtHolidayPeriod.setValidation(new NumericValidation(2,0));
        txtNoOfInstallments.setValidation(new NumericValidation(4,0));
        txtInterestRate.setValidation(new NumericValidation(4,2));
        txtGuaranteeSanctionNo.setMaxLength(50);
        txtGuaranteeAmount.setValidation(new NumericValidation(16,2));
        txtGuaranteeFeePer.setValidation(new NumericValidation(4,2));
        txtGuaranteeFee.setValidation(new NumericValidation(16,2));
        
        
    }
    
    /** Making the btnShareAccount enable or disable according to the actiontype **/
    private void setHelpButtonEnableDisable(boolean enable){
        //        cboInvestmentBehaves.setEditable(enable);
        ClientUtil.enableDisable(panInvestmentDetails,enable);
    }
    
    /* Does necessary operaion when user clicks the save button */
    private void savePerformed(){
        updateOBFields();
        String action;
        
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW ){
            action=CommonConstants.TOSTATUS_INSERT;
            transactionSize = (transactionUI.getOutputTO()).size();
            observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
            saveAction(action);
        }else if(observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT){
            action=CommonConstants.TOSTATUS_UPDATE;
            transactionSize = (transactionUI.getOutputTO()).size();
            observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
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
            final String mandatoryMessage = checkMandatory(panMain);
            String alertMsg ="";
            if(mandatoryMessage.length() > 0 ){
                displayAlert(mandatoryMessage);
            }else{
                
                observable.execute(status);
                if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED){
                    setButtonEnableDisable();
                    observable.resetForm();
                    ClientUtil.clearAll(panShare);
                    ClientUtil.enableDisable(panShare,false);
                    panInvestmentDetails.setVisible(true);
                    transactionUI.cancelAction(true);
                    transactionUI.setButtonEnableDisable(true);
                    transactionUI.resetObjects();
                }
                
                //__ if the Action is not Falied, Reset the fields...
                
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
            lst.add("GUARANTEE_NO");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            if(viewType.equals("Edit"))
                viewMap.put(CommonConstants.MAP_NAME, "getSelectGaranteeMaster");
            if(viewType.equals("Delete"))
                viewMap.put(CommonConstants.MAP_NAME, "getSelectGaranteeMaster");
            
        }else {
            viewMap.put(CommonConstants.MAP_NAME, "getSelectInvestmentProduct");
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
                setButtonEnableDisable();
                hash.put(CommonConstants.MAP_WHERE, hash.get("INVESTMENT_NAME"));
                hash.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                observable.populateData(hash);
                if (viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) ||
                viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                    ClientUtil.enableDisable(panShare, false);
                    setHelpButtonEnableDisable(false);
                    
                } else {
                    ClientUtil.enableDisable(panShare, true);
                    setHelpButtonEnableDisable(true);
                }
                
                if(viewType ==  AUTHORIZE) {
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                }
            }else if(viewType.equals("InvestmentProduct")){
                //                txtInvestmentID.setText(CommonUtil.convertObjToStr(hash.get("INVESTMENT_PROD_ID")));
            }
            
            
            
            else if(viewType.equals("CUSTOMER")){
                txtCustId.setText(CommonUtil.convertObjToStr(hash.get("CUSTOMER ID")));
            }
            if(observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                //                transactionSize = (transactionUI.getOutputTO()).size();
                if((transactionUI.getOutputTO())==null ){
                    transactionUI.cancelAction(false);
                    transactionUI.setButtonEnableDisable(true);
                    transactionUI.resetObjects();
                    transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
                    transactionUI.setCallingApplicantName("");
                    transactionUI.setCallingAmount("");
                }
                else{
                    transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_EDIT);
                    
                }
            }
            
        }
        //        if(observable.getActionType()!=ClientConstants.ACTIONTYPE_NEW && observable.getAvailableNoOfUnits()>0){
        //            ClientUtil.enableDisable(panShare,false);
        //            ClientUtil.enableDisable(panInvestmentDetails,true);
        //            //            ClientUtil.enableDisable(panPurchaseOrSale,true);
        //            //            ClientUtil.enableDisable(panSlrNonSlr,true);
        ////            cboInterestPaymentFrequency.setEnabled(true);
        
        //        }
        
        
        
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
    
    /** This will do necessary operation for authorization **/
    public void authorizeStatus(String authorizeStatus) {
        if (!viewType.equals(AUTHORIZE)){
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            mapParam.put(CommonConstants.MAP_NAME, "getGuaranteeMasterAuthorizeList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeInvestmentMaster");
            mapParam.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            setModified(true);
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
        } else if (viewType.equals(AUTHORIZE)){
            String action="AUTHORIZE";
            
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            //               authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
            HashMap singleAuthorizeMap = new HashMap();
            ArrayList arrList = new ArrayList();
            HashMap authDataMap = new HashMap();
            authDataMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            System.out.println("authDataMap : " + authDataMap);
            arrList.add(authDataMap);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            singleAuthorizeMap.put("GUARANTEE_NO",CommonUtil.convertObjToStr(lblGuranteNoValue.getText()));
            singleAuthorizeMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            observable.set_authorizeMap(singleAuthorizeMap);
            //                authorize(singleAuthorizeMap);
            viewType = ClientConstants.VIEW_TYPE_CANCEL;
            ClientUtil.enableDisable(panTransaction,false);
            observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
            updateOBFields();
            observable.execute(action);
            
            btnCancelActionPerformed(null);
        }
    }
    
    public static void main(String args[]){
        javax.swing.JFrame frame = new javax.swing.JFrame();
        GuaranteeMasterUI ui = new GuaranteeMasterUI();
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
        cTabbedPane1 = new com.see.truetransact.uicomponent.CTabbedPane();
        panMain = new com.see.truetransact.uicomponent.CPanel();
        panShare = new com.see.truetransact.uicomponent.CPanel();
        cboPliBranch = new com.see.truetransact.uicomponent.CComboBox();
        lblCustId = new com.see.truetransact.uicomponent.CLabel();
        panInvestmentID = new com.see.truetransact.uicomponent.CPanel();
        txtCustId = new com.see.truetransact.uicomponent.CTextField();
        btnCustId = new com.see.truetransact.uicomponent.CButton();
        lblSanctionNo = new com.see.truetransact.uicomponent.CLabel();
        txtSanctionNo = new com.see.truetransact.uicomponent.CTextField();
        lblSanctionDt = new com.see.truetransact.uicomponent.CLabel();
        tdtSanctionDt = new com.see.truetransact.uicomponent.CDateField();
        lblPliBranch = new com.see.truetransact.uicomponent.CLabel();
        cboPli = new com.see.truetransact.uicomponent.CComboBox();
        lblPli = new com.see.truetransact.uicomponent.CLabel();
        lblLoanNo = new com.see.truetransact.uicomponent.CLabel();
        txtLoanNo = new com.see.truetransact.uicomponent.CTextField();
        lblLoanDt = new com.see.truetransact.uicomponent.CLabel();
        tdtLoanDt = new com.see.truetransact.uicomponent.CDateField();
        lblLoanAmount = new com.see.truetransact.uicomponent.CLabel();
        txtLoanAmount = new com.see.truetransact.uicomponent.CTextField();
        lblRepaymentFrequency = new com.see.truetransact.uicomponent.CLabel();
        cboRepaymentFrequency = new com.see.truetransact.uicomponent.CComboBox();
        lblNoOfInstallments = new com.see.truetransact.uicomponent.CLabel();
        txtNoOfInstallments = new com.see.truetransact.uicomponent.CTextField();
        lblHolidayPeriod = new com.see.truetransact.uicomponent.CLabel();
        txtHolidayPeriod = new com.see.truetransact.uicomponent.CTextField();
        lblInterestType = new com.see.truetransact.uicomponent.CLabel();
        txtInterestRate = new com.see.truetransact.uicomponent.CTextField();
        panInvestmentDetails = new com.see.truetransact.uicomponent.CPanel();
        lblGuaranteeNo = new com.see.truetransact.uicomponent.CLabel();
        panGuaranteeFee = new com.see.truetransact.uicomponent.CPanel();
        rdoPLI = new com.see.truetransact.uicomponent.CRadioButton();
        rdoCustomer = new com.see.truetransact.uicomponent.CRadioButton();
        lblGuaranteeDt = new com.see.truetransact.uicomponent.CLabel();
        tdtGuaranteeDt = new com.see.truetransact.uicomponent.CDateField();
        lblGuaranteeFeePayBy = new com.see.truetransact.uicomponent.CLabel();
        lblGuaranteeSanctionedBy = new com.see.truetransact.uicomponent.CLabel();
        cboGuaranteeSanctionedBy = new com.see.truetransact.uicomponent.CComboBox();
        lblGuaranteeSanctionNo = new com.see.truetransact.uicomponent.CLabel();
        txtGuaranteeSanctionNo = new com.see.truetransact.uicomponent.CTextField();
        lblGuaranteeFeePer = new com.see.truetransact.uicomponent.CLabel();
        txtGuaranteeFeePer = new com.see.truetransact.uicomponent.CTextField();
        lblGuaranteeFee = new com.see.truetransact.uicomponent.CLabel();
        txtGuaranteeFee = new com.see.truetransact.uicomponent.CTextField();
        lblGuaranteeAmount = new com.see.truetransact.uicomponent.CLabel();
        txtGuaranteeAmount = new com.see.truetransact.uicomponent.CTextField();
        lblGuranteNoValue = new com.see.truetransact.uicomponent.CLabel();
        panTransaction = new com.see.truetransact.uicomponent.CPanel();
        panAmortizationDetails = new com.see.truetransact.uicomponent.CPanel();
        srpTblAmortizationDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblAmortizationDetails = new com.see.truetransact.uicomponent.CTable();
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
        setPreferredSize(new java.awt.Dimension(900, 650));

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
        btnSave.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                btnSaveItemStateChanged(evt);
            }
        });
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

        panMain.setMaximumSize(new java.awt.Dimension(900, 650));
        panMain.setMinimumSize(new java.awt.Dimension(900, 650));
        panMain.setPreferredSize(new java.awt.Dimension(900, 650));
        panMain.setLayout(new java.awt.GridBagLayout());

        panShare.setBorder(javax.swing.BorderFactory.createTitledBorder("Loan Details"));
        panShare.setMaximumSize(new java.awt.Dimension(435, 345));
        panShare.setMinimumSize(new java.awt.Dimension(435, 345));
        panShare.setName("Loan Details");
        panShare.setPreferredSize(new java.awt.Dimension(435, 345));
        panShare.setLayout(new java.awt.GridBagLayout());

        cboPliBranch.setMinimumSize(new java.awt.Dimension(100, 21));
        cboPliBranch.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboPliBranchItemStateChanged(evt);
            }
        });
        cboPliBranch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboPliBranchActionPerformed(evt);
            }
        });
        cboPliBranch.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboPliBranchFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panShare.add(cboPliBranch, gridBagConstraints);

        lblCustId.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCustId.setText("Customer ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panShare.add(lblCustId, gridBagConstraints);

        panInvestmentID.setLayout(new java.awt.GridBagLayout());

        txtCustId.setEditable(false);
        txtCustId.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCustId.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInvestmentID.add(txtCustId, gridBagConstraints);

        btnCustId.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnCustId.setMinimumSize(new java.awt.Dimension(21, 21));
        btnCustId.setPreferredSize(new java.awt.Dimension(21, 21));
        btnCustId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustIdActionPerformed(evt);
            }
        });
        btnCustId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                btnCustIdFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panInvestmentID.add(btnCustId, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panShare.add(panInvestmentID, gridBagConstraints);

        lblSanctionNo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSanctionNo.setText("Sanction No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panShare.add(lblSanctionNo, gridBagConstraints);

        txtSanctionNo.setMinimumSize(new java.awt.Dimension(150, 21));
        txtSanctionNo.setPreferredSize(new java.awt.Dimension(125, 21));
        txtSanctionNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSanctionNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panShare.add(txtSanctionNo, gridBagConstraints);

        lblSanctionDt.setText("Sanction Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panShare.add(lblSanctionDt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panShare.add(tdtSanctionDt, gridBagConstraints);

        lblPliBranch.setText("P.L.I Branch");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panShare.add(lblPliBranch, gridBagConstraints);

        cboPli.setMinimumSize(new java.awt.Dimension(100, 21));
        cboPli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboPliActionPerformed(evt);
            }
        });
        cboPli.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboPliFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panShare.add(cboPli, gridBagConstraints);

        lblPli.setText("P.L.I");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panShare.add(lblPli, gridBagConstraints);

        lblLoanNo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblLoanNo.setText("Loan No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panShare.add(lblLoanNo, gridBagConstraints);

        txtLoanNo.setMinimumSize(new java.awt.Dimension(150, 21));
        txtLoanNo.setPreferredSize(new java.awt.Dimension(125, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panShare.add(txtLoanNo, gridBagConstraints);

        lblLoanDt.setText("Loan Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panShare.add(lblLoanDt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panShare.add(tdtLoanDt, gridBagConstraints);

        lblLoanAmount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblLoanAmount.setText("Loan Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panShare.add(lblLoanAmount, gridBagConstraints);

        txtLoanAmount.setMinimumSize(new java.awt.Dimension(150, 21));
        txtLoanAmount.setPreferredSize(new java.awt.Dimension(125, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panShare.add(txtLoanAmount, gridBagConstraints);

        lblRepaymentFrequency.setText("RepaymentFrequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panShare.add(lblRepaymentFrequency, gridBagConstraints);

        cboRepaymentFrequency.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panShare.add(cboRepaymentFrequency, gridBagConstraints);

        lblNoOfInstallments.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblNoOfInstallments.setText("No Of Installments");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panShare.add(lblNoOfInstallments, gridBagConstraints);

        txtNoOfInstallments.setMinimumSize(new java.awt.Dimension(150, 21));
        txtNoOfInstallments.setPreferredSize(new java.awt.Dimension(125, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panShare.add(txtNoOfInstallments, gridBagConstraints);

        lblHolidayPeriod.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblHolidayPeriod.setText("Holiday Period");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panShare.add(lblHolidayPeriod, gridBagConstraints);

        txtHolidayPeriod.setMinimumSize(new java.awt.Dimension(150, 21));
        txtHolidayPeriod.setPreferredSize(new java.awt.Dimension(125, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panShare.add(txtHolidayPeriod, gridBagConstraints);

        lblInterestType.setText("Interest Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panShare.add(lblInterestType, gridBagConstraints);

        txtInterestRate.setMaximumSize(new java.awt.Dimension(150, 21));
        txtInterestRate.setMinimumSize(new java.awt.Dimension(150, 21));
        txtInterestRate.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        panShare.add(txtInterestRate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panMain.add(panShare, gridBagConstraints);

        panInvestmentDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Guarantee Details"));
        panInvestmentDetails.setMaximumSize(new java.awt.Dimension(435, 345));
        panInvestmentDetails.setMinimumSize(new java.awt.Dimension(435, 345));
        panInvestmentDetails.setPreferredSize(new java.awt.Dimension(435, 345));
        panInvestmentDetails.setLayout(new java.awt.GridBagLayout());

        lblGuaranteeNo.setText("Guarantee No  ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInvestmentDetails.add(lblGuaranteeNo, gridBagConstraints);

        panGuaranteeFee.setLayout(new java.awt.GridBagLayout());

        rdoPLI.setText("PLI");
        rdoPLI.setMaximumSize(new java.awt.Dimension(50, 18));
        rdoPLI.setMinimumSize(new java.awt.Dimension(50, 18));
        rdoPLI.setPreferredSize(new java.awt.Dimension(50, 18));
        rdoPLI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoPLIActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panGuaranteeFee.add(rdoPLI, gridBagConstraints);

        rdoCustomer.setText("Customer");
        rdoCustomer.setMaximumSize(new java.awt.Dimension(45, 18));
        rdoCustomer.setMinimumSize(new java.awt.Dimension(77, 18));
        rdoCustomer.setPreferredSize(new java.awt.Dimension(77, 18));
        rdoCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoCustomerActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panGuaranteeFee.add(rdoCustomer, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panInvestmentDetails.add(panGuaranteeFee, gridBagConstraints);

        lblGuaranteeDt.setText("Guarantee Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panInvestmentDetails.add(lblGuaranteeDt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 1);
        panInvestmentDetails.add(tdtGuaranteeDt, gridBagConstraints);

        lblGuaranteeFeePayBy.setText("Guarantee Fee Recived By");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInvestmentDetails.add(lblGuaranteeFeePayBy, gridBagConstraints);

        lblGuaranteeSanctionedBy.setText("Guarantee Sanctioned By");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInvestmentDetails.add(lblGuaranteeSanctionedBy, gridBagConstraints);

        cboGuaranteeSanctionedBy.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInvestmentDetails.add(cboGuaranteeSanctionedBy, gridBagConstraints);

        lblGuaranteeSanctionNo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblGuaranteeSanctionNo.setText("Guarantee Sanction No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panInvestmentDetails.add(lblGuaranteeSanctionNo, gridBagConstraints);

        txtGuaranteeSanctionNo.setMinimumSize(new java.awt.Dimension(150, 21));
        txtGuaranteeSanctionNo.setPreferredSize(new java.awt.Dimension(125, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 5, 4);
        panInvestmentDetails.add(txtGuaranteeSanctionNo, gridBagConstraints);

        lblGuaranteeFeePer.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblGuaranteeFeePer.setText("Guarantee Fee %");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panInvestmentDetails.add(lblGuaranteeFeePer, gridBagConstraints);

        txtGuaranteeFeePer.setMinimumSize(new java.awt.Dimension(150, 21));
        txtGuaranteeFeePer.setPreferredSize(new java.awt.Dimension(125, 21));
        txtGuaranteeFeePer.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtGuaranteeFeePerFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 5, 4);
        panInvestmentDetails.add(txtGuaranteeFeePer, gridBagConstraints);

        lblGuaranteeFee.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblGuaranteeFee.setText("Guarantee Fee ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panInvestmentDetails.add(lblGuaranteeFee, gridBagConstraints);

        txtGuaranteeFee.setMinimumSize(new java.awt.Dimension(150, 21));
        txtGuaranteeFee.setPreferredSize(new java.awt.Dimension(125, 21));
        txtGuaranteeFee.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 5, 4);
        panInvestmentDetails.add(txtGuaranteeFee, gridBagConstraints);

        lblGuaranteeAmount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblGuaranteeAmount.setText("Guarantee Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panInvestmentDetails.add(lblGuaranteeAmount, gridBagConstraints);

        txtGuaranteeAmount.setMinimumSize(new java.awt.Dimension(150, 21));
        txtGuaranteeAmount.setPreferredSize(new java.awt.Dimension(125, 21));
        txtGuaranteeAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtGuaranteeAmountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 5, 4);
        panInvestmentDetails.add(txtGuaranteeAmount, gridBagConstraints);

        lblGuranteNoValue.setText("Gurant No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panInvestmentDetails.add(lblGuranteNoValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panMain.add(panInvestmentDetails, gridBagConstraints);

        panTransaction.setMinimumSize(new java.awt.Dimension(800, 250));
        panTransaction.setPreferredSize(new java.awt.Dimension(900, 250));
        panTransaction.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panMain.add(panTransaction, gridBagConstraints);

        cTabbedPane1.addTab("Guarantee Details", panMain);

        panAmortizationDetails.setPreferredSize(new java.awt.Dimension(800, 380));
        panAmortizationDetails.setLayout(new java.awt.GridBagLayout());

        srpTblAmortizationDetails.setMinimumSize(new java.awt.Dimension(780, 125));
        srpTblAmortizationDetails.setPreferredSize(new java.awt.Dimension(780, 125));

        tblAmortizationDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Trans Date", "Guarantee No", "PLI", "Amount"
            }
        ));
        tblAmortizationDetails.setAutoCreateColumnsFromModel(false);
        tblAmortizationDetails.setCellSelectionEnabled(true);
        tblAmortizationDetails.setMinimumSize(new java.awt.Dimension(225, 16));
        srpTblAmortizationDetails.setViewportView(tblAmortizationDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 13, 0);
        panAmortizationDetails.add(srpTblAmortizationDetails, gridBagConstraints);

        cTabbedPane1.addTab("Transaction  Details Query", panAmortizationDetails);

        getContentPane().add(cTabbedPane1, java.awt.BorderLayout.CENTER);

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

    private void txtGuaranteeAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtGuaranteeAmountFocusLost
        // TODO add your handling code here:
        double guarntee=CommonUtil.convertObjToDouble(txtGuaranteeAmount.getText()).doubleValue();
        if(guarntee>0){
             double loanAmt=CommonUtil.convertObjToDouble(txtLoanAmount.getText()).doubleValue();
             if(loanAmt<guarntee){
                 ClientUtil.displayAlert("Guarntee Amount Should not Cross Loan Sanction Amt");
                 txtGuaranteeAmount.setText("");
                 return;
             }
        }
    }//GEN-LAST:event_txtGuaranteeAmountFocusLost

    private void cboPliBranchItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboPliBranchItemStateChanged
        
    }//GEN-LAST:event_cboPliBranchItemStateChanged

    private void cboPliBranchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPliBranchActionPerformed
        
    }//GEN-LAST:event_cboPliBranchActionPerformed

    private void cboPliBranchFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboPliBranchFocusLost
        // TODO add your handling code here:
        System.out.println("Inside cbo Focus Lost");
        
        //        String behaves=observable.callForBehaves();
        //        if(behaves.equals("") && behaves.length()<=0){
        //            ClientUtil.displayAlert("Please Select The Investment Type");
        ////            cboInvestmentBehaves.requestFocus();
        //        }
    }//GEN-LAST:event_cboPliBranchFocusLost

    private void btnCustIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustIdActionPerformed
        // TODO add your handling code here:
        //        callView("InvestmentProduct");
        viewType ="CUSTOMER";
        HashMap viewMap=new HashMap();
        HashMap whereMap=new HashMap();
        viewMap.put(CommonConstants.MAP_NAME, "OperativeAcct.getCustData");
        whereMap.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        new ViewAll(this, viewMap).show();
        viewMap=null;
        whereMap =null;
    }//GEN-LAST:event_btnCustIdActionPerformed

    private void btnCustIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnCustIdFocusLost
        //        // TODO add your handling code here:
        //        cboInvestmentBehaves.setEditable(false);
        //        cboInvestmentBehaves.setEnabled(false);
    }//GEN-LAST:event_btnCustIdFocusLost

    private void txtSanctionNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSanctionNoFocusLost
        // TODO add your handling code here:
        //        if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW ){
        //            boolean exists=false;
        ////            exists=observable.isInvsetMentMasterTypeExists(CommonUtil.convertObjToStr(txtInvestmentName.getText()));
        //            if(exists==true){
        //                displayAlert("Already This Prod Id Exists");
        //                txtInvestmentName.setText("");
        //                txtInvestmentName.requestFocus();
        //                btnInvestmentID.setEnabled(true);
        //            }else{
        //                btnInvestmentID.setEnabled(false);
        //            }
        //        }
    }//GEN-LAST:event_txtSanctionNoFocusLost

    private void cboPliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPliActionPerformed
        // TODO add your handling code here:
        if (cboPli.getSelectedIndex() > 0) {
            String pliCode = ((ComboBoxModel)cboPli.getModel()).getKeyForSelected().toString();
            observable.getPliBranch(pliCode);
            cboPliBranch.setModel(observable.getCbmPliBranch());
        }
    }//GEN-LAST:event_cboPliActionPerformed

    private void cboPliFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboPliFocusLost
        // TODO add your handling code here:
        //        if(cboPli.get)
    }//GEN-LAST:event_cboPliFocusLost

    private void rdoPLIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoPLIActionPerformed
        // TODO add your handling code here:
        if(rdoPLI.isSelected()==true)
            rdoCustomer.setSelected(false);
    }//GEN-LAST:event_rdoPLIActionPerformed

    private void rdoCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoCustomerActionPerformed
        // TODO add your handling code here:
        
        
        if(rdoCustomer.isSelected()==true)
            rdoPLI.setSelected(false);
        //                    ClientUtil.displayAlert("Select Guarantee Fee Pay By");
    }//GEN-LAST:event_rdoCustomerActionPerformed

    private void txtGuaranteeFeePerFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtGuaranteeFeePerFocusLost
        // TODO add your handling code here:
        double per=CommonUtil.convertObjToDouble(txtGuaranteeFeePer.getText()).doubleValue();
        double amount=CommonUtil.convertObjToDouble(txtGuaranteeAmount.getText()).doubleValue();
        if(per>0 && amount >0){
            
            double fee= (amount*per)/100;
            txtGuaranteeFee.setText(CommonUtil.convertObjToStr(new Double(fee)));
        }else
            ClientUtil.displayAlert("Guarantee Fee %  or Guarantee Amount  Not Given");
    }//GEN-LAST:event_txtGuaranteeFeePerFocusLost
    
    private void btnSaveItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_btnSaveItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSaveItemStateChanged
                                                
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
        panInvestmentDetails.setVisible(true);
        ClientUtil.clearAll(this);
        //        ClientUtil.enableDisable(panShareProduct, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        ClientUtil.enableDisable(panShare,false);
        setButtonEnableDisable();
        setHelpButtonEnableDisable(false);
        viewType = "";
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        transactionUI.cancelAction(true);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_CANCEL);
        ClientUtil.enableDisable(panMain,false);
        transactionUI.cancelAction(true);
        //        transactionUI.setButtonEnableDisable(true);
        transactionUI.resetObjects();
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        callView("Delete");
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        ClientUtil.enableDisable(panShare,false);
        ClientUtil.enableDisable(panInvestmentDetails,true);
        
        
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        callView("Edit");
        
        
        //        tdtMaturityDate.setEnabled(false);
        //        txtInvestmentName.setEditable(false);
        //        txtInvestmentName.setEnabled(false);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        //        cboInvestmentBehaves.setEnabled(false);
        //        cboInvestmentBehaves.setEditable(false);
        //        txtProductID.setEnabled(false);
        //        txtProductID.setEditable(false);
        
        //        transactionSize = (transactionUI.getOutputTO()).size();
        //        if(transactionSize<=0){
        //        transactionUI.cancelAction(false);
        //        transactionUI.setButtonEnableDisable(true);
        //        transactionUI.resetObjects();
        //        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
        //        transactionUI.setCallingApplicantName("");
        //        transactionUI.setCallingAmount("");
        //        } else {
        //             transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_EDIT);
        //        }
        //
        
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
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panMain,true);
        //        tdtMaturityDate.setEnabled(false);
        //        ClientUtil.enableDisable(panLoanType,false);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
        setHelpButtonEnableDisable(true);
        //        btnTabNew.setEnabled(true);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        transactionUI.cancelAction(false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.resetObjects();
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
        transactionUI.setCallingApplicantName("");
        transactionUI.setCallingAmount("");
        //        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_EDIT);
        ////        transactionUI.setSaveDeleteTransDisable(true);
        
        
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
    private com.see.truetransact.uicomponent.CTabbedPane cTabbedPane1;
    private com.see.truetransact.uicomponent.CComboBox cboGuaranteeSanctionedBy;
    private com.see.truetransact.uicomponent.CComboBox cboPli;
    private com.see.truetransact.uicomponent.CComboBox cboPliBranch;
    private com.see.truetransact.uicomponent.CComboBox cboRepaymentFrequency;
    private com.see.truetransact.uicomponent.CLabel lblCustId;
    private com.see.truetransact.uicomponent.CLabel lblGuaranteeAmount;
    private com.see.truetransact.uicomponent.CLabel lblGuaranteeDt;
    private com.see.truetransact.uicomponent.CLabel lblGuaranteeFee;
    private com.see.truetransact.uicomponent.CLabel lblGuaranteeFeePayBy;
    private com.see.truetransact.uicomponent.CLabel lblGuaranteeFeePer;
    private com.see.truetransact.uicomponent.CLabel lblGuaranteeNo;
    private com.see.truetransact.uicomponent.CLabel lblGuaranteeSanctionNo;
    private com.see.truetransact.uicomponent.CLabel lblGuaranteeSanctionedBy;
    private com.see.truetransact.uicomponent.CLabel lblGuranteNoValue;
    private com.see.truetransact.uicomponent.CLabel lblHolidayPeriod;
    private com.see.truetransact.uicomponent.CLabel lblInterestType;
    private com.see.truetransact.uicomponent.CLabel lblLoanAmount;
    private com.see.truetransact.uicomponent.CLabel lblLoanDt;
    private com.see.truetransact.uicomponent.CLabel lblLoanNo;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNoOfInstallments;
    private com.see.truetransact.uicomponent.CLabel lblPli;
    private com.see.truetransact.uicomponent.CLabel lblPliBranch;
    private com.see.truetransact.uicomponent.CLabel lblRepaymentFrequency;
    private com.see.truetransact.uicomponent.CLabel lblSanctionDt;
    private com.see.truetransact.uicomponent.CLabel lblSanctionNo;
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
    private com.see.truetransact.uicomponent.CMenuBar mbrShareProduct;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAmortizationDetails;
    private com.see.truetransact.uicomponent.CPanel panGuaranteeFee;
    private com.see.truetransact.uicomponent.CPanel panInvestmentDetails;
    private com.see.truetransact.uicomponent.CPanel panInvestmentID;
    private com.see.truetransact.uicomponent.CPanel panMain;
    private com.see.truetransact.uicomponent.CPanel panShare;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTransaction;
    private com.see.truetransact.uicomponent.CRadioButton rdoCustomer;
    private com.see.truetransact.uicomponent.CRadioButton rdoPLI;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CScrollPane srpTblAmortizationDetails;
    private com.see.truetransact.uicomponent.CTable tblAmortizationDetails;
    private javax.swing.JToolBar tbrShareProduct;
    private com.see.truetransact.uicomponent.CDateField tdtGuaranteeDt;
    private com.see.truetransact.uicomponent.CDateField tdtLoanDt;
    private com.see.truetransact.uicomponent.CDateField tdtSanctionDt;
    private com.see.truetransact.uicomponent.CTextField txtCustId;
    private com.see.truetransact.uicomponent.CTextField txtGuaranteeAmount;
    private com.see.truetransact.uicomponent.CTextField txtGuaranteeFee;
    private com.see.truetransact.uicomponent.CTextField txtGuaranteeFeePer;
    private com.see.truetransact.uicomponent.CTextField txtGuaranteeSanctionNo;
    private com.see.truetransact.uicomponent.CTextField txtHolidayPeriod;
    private com.see.truetransact.uicomponent.CTextField txtInterestRate;
    private com.see.truetransact.uicomponent.CTextField txtLoanAmount;
    private com.see.truetransact.uicomponent.CTextField txtLoanNo;
    private com.see.truetransact.uicomponent.CTextField txtNoOfInstallments;
    private com.see.truetransact.uicomponent.CTextField txtSanctionNo;
    // End of variables declaration//GEN-END:variables
    
}
