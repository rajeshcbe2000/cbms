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
import com.see.truetransact.ui.investments.InvestmentsAmortizationMRB;
import com.see.truetransact.ui.investments.InvestmentsAmortizationRB;
import com.see.truetransact.ui.common.transaction.TransactionUI;

public class InvestmentsAmortizationUI extends CInternalFrame implements UIMandatoryField, Observer{
    
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private HashMap mandatoryMap;
    private InvestmentsAmortizationOB observable;
    private InvestmentsAmortizationMRB objMandatoryRB;
    private ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.investments.InvestmentsAmortizationRB", ProxyParameters.LANGUAGE);
    private String viewType = new String();
    final String AUTHORIZE="Authorize";
    private TransactionUI transactionUI = new TransactionUI();
    private int yearTobeAdded = 1900;
    private Date currDt = null;
    /** Creates new form ShareProductUI */
    public InvestmentsAmortizationUI() {
        currDt = ClientUtil.getCurrentDate();
        initUIComponents();
        transactionUI.addToScreen(panTransaction);
        observable.setTransactionOB(transactionUI.getTransactionOB());
    }
    
    /** Initialsises the UIComponents */
    private void initUIComponents(){
        initComponents();
        setFieldNames();
        setMaxLength();
        internationalize();
        setMandatoryHashMap();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panInvestmentTrans, getMandatoryHashMap());
        setHelpMessage();
        setObservable();
        observable.resetForm();
        lblAmrtAmtValue.setText("");
        
        resetTransactionUI();
        lblPeriodValue.setText("");
        initComponentData();
        ClientUtil.enableDisable(panInvestmentTrans, false);
        setButtonEnableDisable();
        txtInvestmentName.setEnabled(false);
        txtInvestmentName.setEditable(false);
        tabClosingType.remove(panTransaction);
        tabClosingType.remove(panAmortizationDetails);
        btnViewAmrot.setVisible(false);
        
        
        
        
        
        
        //        ShareProduct.resetVisits();
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
        lblInvestmentID.setName("lblInvestmentID");
        lblInvestmentName.setName("lblInvestmentName");
        lblInvestmentBehaves.setName("lblInvestmentBehaves");
        lblIssueDate.setName("lblIssueDate");
        lblFaceValue.setName("lblFaceValue");
        lblAvailableNoOfUnits.setName("lblAvailableNoOfUnits");
        lblLastIntPaidDate.setName("lblLastIntPaidDate");
        lblInterestPaymentFrequency.setName("lblInterestPaymentFrequency");
        lblPeriod.setName("lblPeriod");
        lblCoupenRate.setName("lblCoupenRate");
        lblMaturityDate.setName("lblMaturityDate");
        lblTotalInvestmentAmount.setName("lblTotalInvestmentAmount");
        lblTotalPremiumPaid.setName("lblTotalPremiumPaid");
        lblTotalPremiumCollected.setName("lblTotalPremiumCollected");
        lblTotalInterestPaid.setName("lblTotalInterestPaid");
        lblTotalInterestCollected.setName("lblTotalInterestCollected");
        lblPurchaseDt.setName("lblPurchaseDt");
        lblInvestmentAmount.setName("lblInvestmentAmount");
        lblCoupenRate.setName("lblCoupenRate");
        cboInvestmentBehaves.setName("cboInvestmentBehaves");
        txtInvestmentID.setName("txtInvestmentID");
        txtInvestmentName.setName("txtInvestmentName");
        tdtPurchaseDt.setName("tdtPurchaseDt");
        txtInvestmentAmount.setName("txtInvestmentAmount");
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
        lblInvestmentID.setText(resourceBundle.getString("lblInvestmentID"));
        lblInvestmentName.setText(resourceBundle.getString("lblInvestmentName"));
        lblInvestmentBehaves.setText(resourceBundle.getString("lblInvestmentBehaves"));
        lblIssueDate.setText(resourceBundle.getString("lblIssueDate"));
        lblFaceValue.setText(resourceBundle.getString("lblFaceValue"));
        lblAvailableNoOfUnits.setText(resourceBundle.getString("lblAvailableNoOfUnits"));
        lblLastIntPaidDate.setText(resourceBundle.getString("lblLastIntPaidDate"));
        lblInterestPaymentFrequency.setText(resourceBundle.getString("lblInterestPaymentFrequency"));
        lblPeriod.setText(resourceBundle.getString("lblPeriod"));
        lblCoupenRate.setText(resourceBundle.getString("lblCoupenRate"));
        lblMaturityDate.setText(resourceBundle.getString("lblMaturityDate"));
        lblTotalInvestmentAmount.setText(resourceBundle.getString("lblTotalInvestmentAmount"));
        lblTotalPremiumPaid.setText(resourceBundle.getString("lblTotalPremiumPaid"));
        lblTotalPremiumCollected.setText(resourceBundle.getString("lblTotalPremiumCollected"));
        lblTotalInterestPaid.setText(resourceBundle.getString("lblTotalInterestPaid"));
        lblTotalInterestCollected.setText(resourceBundle.getString("lblTotalInterestCollected"));
        lblPurchaseDt.setText(resourceBundle.getString("lblPurchaseDt"));
        
        
        
        lblInvestmentAmount.setText(resourceBundle.getString("lblInvestmentAmount"));
        
        lblCoupenRate.setText(resourceBundle.getString("lblCoupenRate"));
    }
    
/* Auto Generated Method - setMandatoryHashMap()
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboInvestmentBehaves", new Boolean(true));
        mandatoryMap.put("txtInvestmentID", new Boolean(true));
        mandatoryMap.put("txtInvestmentName", new Boolean(true));
        mandatoryMap.put("tdtPurchaseDt", new Boolean(true));
        mandatoryMap.put("txtPurchaseRate", new Boolean(true));
        mandatoryMap.put("txtNoOfUnits", new Boolean(true));
        mandatoryMap.put("txtNoOfUnits", new Boolean(true));
        mandatoryMap.put("lblFaceValueValue", new Boolean(true));
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
            observable = InvestmentsAmortizationOB.getInstance();
            observable.addObserver(this);
            observable.setTransactionOB(transactionUI.getTransactionOB());
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    /* Sets the model for the comboboxes in the UI    */
    private void initComponentData() {
        try{
            cboInvestmentBehaves.setModel(observable.getCbmInvestmentBehaves());
            //            cboInterestPaymentFrequency.setModel(observable.getCbmIntPayFreq());
            //            addingRMDeposits();
            
        }catch(ClassCastException e){
            parseException.logException(e,true);
        }
    }
    
/* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        objMandatoryRB = new InvestmentsAmortizationMRB();
        cboInvestmentBehaves.setHelpMessage(lblMsg, objMandatoryRB.getString("cboInvestmentBehaves"));
        txtInvestmentID.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInvestmentID"));
        txtInvestmentName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInvestmentName"));
        tdtPurchaseDt.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtPurchaseDt"));
        
        
        txtInvestmentAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInvestmentAmount"));
        
    }
    
    
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        cboInvestmentBehaves.setSelectedItem(observable.getCboInvestmentBehaves());
        //        cboInterestPaymentFrequency.setSelectedItem(observable.getCboIntPayFreq());
        txtInvestmentID.setText(observable.getInvestmentID());
        txtInvestmentName.setText(observable.getInvestmentName());
        tdtPurchaseDt.setDateValue(CommonUtil.convertObjToStr(observable.getPurchas_Date()));
        txtInvestmentAmount.setText(CommonUtil.convertObjToStr(observable.getInvestment_amount()));
        lblIssueDateValue.setText(CommonUtil.convertObjToStr(DateUtil.getStringDate(observable.getIssueDt())));
        lblFaceValueValue.setText(CommonUtil.convertObjToStr(observable.getFaceValue()));
        lblAvailableNoOfUnitsValue.setText(CommonUtil.convertObjToStr(observable.getAvailableNoOfUnits()));
        lblInterestPaymentFrequencyValue.setText(CommonUtil.convertObjToStr(observable.getCboIntPayFreq()));
        lblCoupenValue.setText(CommonUtil.convertObjToStr(observable.getCouponRate()));
        lblMaturityDateValue.setText(CommonUtil.convertObjToStr(DateUtil.getStringDate(observable.getMaturityDate())));
        lblTotalInvestmentAmountValue.setText(CommonUtil.convertObjToStr(observable.getInvestment_amount()));
        lblTotalPremiumPaidValue.setText(CommonUtil.convertObjToStr(observable.getTotalPremiumPaid()));
        lblTotalPremiumCollectedValue.setText(CommonUtil.convertObjToStr(observable.getTotalPremiumCollected()));
        lblTotalInterestPaidValue.setText(CommonUtil.convertObjToStr(observable.getTotalInterestPaid()));
        lblTotalInterestCollectedValue.setText(CommonUtil.convertObjToStr(observable.getTotalInterestCollected()));
        lblLastIntPaidDateValue.setText(CommonUtil.convertObjToStr(DateUtil.getStringDate(observable.getLastIntPaidDate())));
        lblPeriodValue.setText(CommonUtil.convertObjToStr(CommonUtil.convertObjToInt(observable.getYears())+" "+"Years"+" "+CommonUtil.convertObjToInt(observable.getMonths())+" "+"Months")+
        " "+CommonUtil.convertObjToInt(observable.getDays())+" "+"Days");
        lblTotalInvestmentAmountValue.setText(CommonUtil.convertObjToStr(observable.getOutstandingAmount()));
        lblClassificationValue.setText(observable.getClassification());
        txtPurchaseRate.setText(CommonUtil.convertObjToStr(observable.getValuationRate()));
        tdtPurchaseDt.setDateValue(String.valueOf(observable.getShiftingDate()));
        if(CommonUtil.convertObjToStr(observable.getNewClassfication()).equals("HFM")){
            rdoHfm.setSelected(true);
            
        }else if(CommonUtil.convertObjToStr(observable.getNewClassfication()).equals("HFS")){
            rdoHfs.setSelected(true);
            
        }else if(CommonUtil.convertObjToStr(observable.getNewClassfication()).equals("HFT")){
            rdoHft.setSelected(true);
            
        }
        
        txtInvestmentAmount.setText(CommonUtil.convertObjToStr(observable.getPremium_Amount()));
        tdtPurchaseDt.setDateValue(CommonUtil.convertObjToStr(observable.getShiftingDate()));
        if(rdoAmortization.isSelected()==true)
            tblAmortizationDetails.setModel(observable.getTblInvestmentAmortizationDet());
        
    }
    
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setCboInvestmentBehaves(CommonUtil.convertObjToStr(cboInvestmentBehaves.getSelectedItem()));
        observable.setInvestmentID(CommonUtil.convertObjToStr(txtInvestmentID.getText()));
        observable.setInvestmentName(CommonUtil.convertObjToStr(txtInvestmentName.getText()));
        observable.setPurchas_Date(DateUtil.getDateMMDDYYYY(tdtPurchaseDt.getDateValue()));
        observable.setPremium_Amount(CommonUtil.convertObjToDouble(txtInvestmentAmount.getText()));
        observable.setValuationRate(CommonUtil.convertObjToDouble(txtPurchaseRate.getText()));
        observable.setShiftingDate(DateUtil.getDateMMDDYYYY(tdtPurchaseDt.getDateValue()));
        observable.setTrans_Dt((Date) currDt.clone());
        if(rdoHfm.isSelected()==true)
            observable.setNewClassfication("HFM");
        else if(rdoHfs.isSelected()==true)
            observable.setNewClassfication("HFS");
        else  if(rdoHft.isSelected()==true)
            observable.setNewClassfication("HFT");
        observable.setOldClassfication(lblClassificationValue.getText());
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
        txtInvestmentName.setMaxLength(50);
        txtInvestmentAmount.setValidation(new NumericValidation(16,2));
        txtPurchaseRate.setValidation(new NumericValidation(16,2));
    }
    
    /** Making the btnShareAccount enable or disable according to the actiontype **/
    private void setHelpButtonEnableDisable(boolean enable){
        cboInvestmentBehaves.setEnabled(enable);
        cboInvestmentBehaves.setEditable(enable);
    }
    
    /* Does necessary operaion when user clicks the save button */
    private void savePerformed(){
        updateOBFields();
        String action;
        if(rdoAmortization.isSelected()==false && rdoShfifting.isSelected()==false)
            ClientUtil.displayAlert("Please Slect Amortization Or Shifting");
        else if(rdoShfifting.isSelected()==true){
            if(rdoHfm.isSelected()==false && rdoHfs.isSelected()==false && rdoHft.isSelected()==false){
                ClientUtil.displayAlert("Please Selerct HfM ,HFS OR HFS");
            }else if(rdoHfm.isSelected()==true && CommonUtil.convertObjToStr(lblClassificationValue.getText()).equals("HFM")){
                ClientUtil.displayAlert("Investment In the Same Classification ");
            }else if(rdoHfs.isSelected()==true && CommonUtil.convertObjToStr(lblClassificationValue.getText()).equals("HFs")){
                ClientUtil.displayAlert("Investment In the Same Classification ");
            }else if(rdoHft.isSelected()==true && CommonUtil.convertObjToStr(lblClassificationValue.getText()).equals("HFt")){
                ClientUtil.displayAlert("Investment In the Same Classification ");
            }
            
            
            else if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW ){
                action=CommonConstants.TOSTATUS_INSERT;
                boolean exist=false;
                exist=observable.isInvsetMentMasterTypeExists(CommonUtil.convertObjToStr(txtInvestmentName.getText()));
                if(exist==true){
                    ClientUtil.displayAlert("This Investment  Shifited  Authorization Pending");
                }else{
                    saveAction(action);
                }
                
            }else if(observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT){
                action=CommonConstants.TOSTATUS_UPDATE;
                saveAction(action);
            }else if(observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE){
                action=CommonConstants.TOSTATUS_DELETE;
                saveAction(action);
            }
        }else if(rdoAmortization.isSelected()==true){
            int tabSize=tblAmortizationDetails.getRowCount();
            if(tabSize>0){
                action=CommonConstants.TOSTATUS_INSERT;
                saveAction(action);
            }else{
                ClientUtil.displayAlert("Amortization Records Are Not Their");
            }
            
        }
    }
    
    /* Calls the execute method of ShareProductOB to do insertion or updation or deletion */
    private void saveAction(String status){
        //To check mandtoryness of the Terminal panAcHdDetails,panAcHeadDetails panel and diplay appropriate
        //error message, else proceed
        try{
            final String mandatoryMessage = checkMandatory(panInvestmentTrans);
            String alertMsg ="";
            if(mandatoryMessage.length() > 0  && rdoAmortization.isSelected()==false ){
                displayAlert(mandatoryMessage);
            }else{
                
                if(rdoShfifting.isSelected()==true){
                    observable.setTran_Code("Shifting");
                }
                if(rdoAmortization.isSelected()==true){
                    observable.setTran_Code("Amortization");
                    observable.setPremium_Amount(CommonUtil.convertObjToDouble(lblAmrtAmtValue.getText()));
                }
                int option=0;
                if(rdoAmortization.isSelected()==true){
                    option =ClientUtil.confirmationAlert("Do You Want To Continue " +"\n"+
                    "if Yes it will Create The Transaction And it will Authorize");
                    System.out.println("option------>"+option);
                }
                if(option==0)
                    observable.execute(status);
                else{
                    btnCancelActionPerformed(null);
                }
                
                if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED){
                    setButtonEnableDisable();
                    observable.resetForm();
                    
                    observable.resetTable();
                    resetTransactionUI();
                    lblPeriodValue.setText("");
                    ClientUtil.clearAll(panInvestmentTrans);
                    ClientUtil.enableDisable(panInvestmentTrans,false);
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
        lblAmrtAmtValue.setText("");
        observable.resetTable();
        resetTransactionUI();
        lblPeriodValue.setText("");
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
            lst.add("INVESTMENT_NAME");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            viewMap.put(CommonConstants.MAP_NAME, "getSelectInvestmentShifting");
        }else if(viewType.equals("InvestmentProduct")){
            String bheaves=observable.callForBehaves();
            HashMap whereMap=new HashMap();
            whereMap.put("INVESTMENT_TYPE",bheaves);
            viewMap.put(CommonConstants.MAP_WHERE,whereMap);
            
            viewMap.put(CommonConstants.MAP_NAME, "getSelectInvestmentProduct");
        }else if(viewType.equals("InvestmentName")){
            observable.setCboInvestmentBehaves(CommonUtil.convertObjToStr(cboInvestmentBehaves.getSelectedItem()));
            String bheaves=observable.callForBehaves();
            HashMap whereMap=new HashMap();
            whereMap.put("TRANSACTIONMADE",  "TRANSACTIONMADE");
            if(rdoAmortization.isSelected()==true){
                whereMap.put("CLASSIFICATION","HFM");
                
            }
            
            whereMap.put("INVESTMENT_TYPE",bheaves);
            whereMap.put("INVESTMENT_PROD_ID",CommonUtil.convertObjToStr(txtInvestmentID.getText()));
            whereMap.put("AUTHORIZE_STATUS","AUTHORIZED");
            viewMap.put(CommonConstants.MAP_WHERE,whereMap);
            viewMap.put(CommonConstants.MAP_NAME, "getSelectInvestmentMaster");
            whereMap=null;
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
                hash.put(CommonConstants.MAP_WHERE, hash.get("INVESTMENT_NAME"));
                hash.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                observable.populateData(hash);
                if (viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) ||
                viewType.equals(ClientConstants.ACTION_STATUS[17])) {
                    ClientUtil.enableDisable(panInvestmentTrans, false);
                    setHelpButtonEnableDisable(false);
                    
                } else {
                    ClientUtil.enableDisable(panInvestmentTrans, true);
                    setHelpButtonEnableDisable(true);
                }
                setButtonEnableDisable();
                if(viewType ==  AUTHORIZE) {
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                }
            }else if(viewType.equals("InvestmentProduct")){
                txtInvestmentID.setText(CommonUtil.convertObjToStr(hash.get("INVESTMENT_PROD_ID")));
            }
            else if(viewType.equals("InvestmentName")){
                hash.put(CommonConstants.MAP_WHERE, hash.get("INVESTMENT_NAME"));
                hash.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                hash.put("MASTER","MASTER");
                observable.populateData(hash);
                
                resetTransactionUI();
                
            }
        }
        //        if(observable.getActionType()!=ClientConstants.ACTIONTYPE_NEW){
        //            callCulation();
        //
        //        }
        
        
        
        //        double faceValue=CommonUtil.convertObjToDouble(lblFaceValueValue.getText()).doubleValue();
        //        double noOfUnits=CommonUtil.convertObjToDouble(lblAvailableNoOfUnitsValue.getText()).doubleValue();
        //        double investMentAmount=faceValue*noOfUnits;
        ////        lblTotalInvestmentAmountValue.setText(CommonUtil.convertObjToStr(new Double(investMentAmount)));
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
            mapParam.put(CommonConstants.MAP_NAME, "getInvestmentsAmortizationAuthorizeList");
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            setModified(true);
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
        } else if (viewType.equals(AUTHORIZE)){
            String isAllTabsVisited="";
            //            String isAllTabsVisited = panInvestmentTrans.isAllTabsVisited();
            if(isAllTabsVisited.length()>0){
                //                CommonMethods.displayAlert(isAllTabsVisited);
            } else {
                HashMap singleAuthorizeMap = new HashMap();
                ArrayList arrList = new ArrayList();
                HashMap authDataMap = new HashMap();
                authDataMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
                authDataMap.put("BATCH_ID", observable.getBatch_Id());
                authDataMap.put("SHFTING_DATE",CommonUtil.convertObjToStr(observable.getShiftingDate()));
                authDataMap.put("INVESTMENT_NAME", observable.getInvestmentName());
                authDataMap.put("INVESTMENT_ID", observable.getInvestmentID());
                System.out.println("authDataMap : " + authDataMap);
                arrList.add(authDataMap);
                singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
                singleAuthorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
                authorize(singleAuthorizeMap);
                viewType = ClientConstants.VIEW_TYPE_CANCEL;
                ClientUtil.enableDisable(panTransaction,false);
            }
            
            
            viewType = "";
            btnCancelActionPerformed(null);
        }
    }
    public void authorize(HashMap map){
        map.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        map.put("InvestmentsTransTO",observable.getInvestmentsTransTO(CommonConstants.STATUS_AUTHORIZED));
        map.put("InvestmentsAmortizationTO",observable.getInvestmentsAmortizationTO(CommonConstants.STATUS_AUTHORIZED));
        observable.setAuthorizeMap(map);
        //        observable.setAuthorizeMap(map);
        try{
            observable.execute("AUTHORIZE");
            
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED) {
            //            isFilled = false;
            //            super.setOpenForEditBy(observable.getStatusBy());
            //            super.removeEditLock(txtShareAcctNo.getText());
            
            observable.setResultStatus();
            setModified(false);
            super.removeEditLock(((ComboBoxModel)cboInvestmentBehaves.getModel()).getKeyForSelected().toString());
            observable.resetForm();
            lblAmrtAmtValue.setText("");
            observable.resetTable();
            
            resetTransactionUI();
            lblPeriodValue.setText("");
            ClientUtil.clearAll(this);
            //        ClientUtil.enableDisable(panShareProduct, false);
            observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
            ClientUtil.enableDisable(panInvestmentTrans,false);
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

        panInvestmentTrans = new com.see.truetransact.uicomponent.CPanel();
        tabClosingType = new com.see.truetransact.uicomponent.CTabbedPane();
        panClosingPosition = new com.see.truetransact.uicomponent.CPanel();
        lblInvestmentAmount = new com.see.truetransact.uicomponent.CLabel();
        txtInvestmentAmount = new com.see.truetransact.uicomponent.CTextField();
        lblPurchaseDt = new com.see.truetransact.uicomponent.CLabel();
        tdtPurchaseDt = new com.see.truetransact.uicomponent.CDateField();
        panShfiting = new com.see.truetransact.uicomponent.CPanel();
        rdoHfm = new com.see.truetransact.uicomponent.CRadioButton();
        rdoHft = new com.see.truetransact.uicomponent.CRadioButton();
        lblClassifaction1 = new com.see.truetransact.uicomponent.CLabel();
        rdoHfs = new com.see.truetransact.uicomponent.CRadioButton();
        lblPurchaseRate = new com.see.truetransact.uicomponent.CLabel();
        txtPurchaseRate = new com.see.truetransact.uicomponent.CTextField();
        panTransaction = new com.see.truetransact.uicomponent.CPanel();
        panAmortizationDetails = new com.see.truetransact.uicomponent.CPanel();
        srpTblAmortizationDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblAmortizationDetails = new com.see.truetransact.uicomponent.CTable();
        panPurchaseOrSale1 = new com.see.truetransact.uicomponent.CPanel();
        lblTotAmrtAmt = new com.see.truetransact.uicomponent.CLabel();
        lblAmrtAmtValue = new com.see.truetransact.uicomponent.CLabel();
        panDepositDetails = new com.see.truetransact.uicomponent.CPanel();
        lblInterestPaymentFrequency = new com.see.truetransact.uicomponent.CLabel();
        lblInterestPaymentFrequencyValue = new com.see.truetransact.uicomponent.CLabel();
        lblPeriod = new com.see.truetransact.uicomponent.CLabel();
        lblPeriodValue = new com.see.truetransact.uicomponent.CLabel();
        lblMaturityDate = new com.see.truetransact.uicomponent.CLabel();
        lblMaturityDateValue = new com.see.truetransact.uicomponent.CLabel();
        lblCoupenRate = new com.see.truetransact.uicomponent.CLabel();
        lblCoupenValue = new com.see.truetransact.uicomponent.CLabel();
        lblTotalInvestmentAmount = new com.see.truetransact.uicomponent.CLabel();
        lblTotalInvestmentAmountValue = new com.see.truetransact.uicomponent.CLabel();
        lblTotalInterestCollected = new com.see.truetransact.uicomponent.CLabel();
        lblTotalInterestCollectedValue = new com.see.truetransact.uicomponent.CLabel();
        lblTotalPremiumPaid = new com.see.truetransact.uicomponent.CLabel();
        lblTotalPremiumPaidValue = new com.see.truetransact.uicomponent.CLabel();
        lblTotalPremiumCollected = new com.see.truetransact.uicomponent.CLabel();
        lblTotalPremiumCollectedValue = new com.see.truetransact.uicomponent.CLabel();
        lblTotalInterestPaid = new com.see.truetransact.uicomponent.CLabel();
        lblTotalInterestPaidValue = new com.see.truetransact.uicomponent.CLabel();
        panDepositDetails1 = new com.see.truetransact.uicomponent.CPanel();
        lblInvestmentBehaves = new com.see.truetransact.uicomponent.CLabel();
        cboInvestmentBehaves = new com.see.truetransact.uicomponent.CComboBox();
        lblInvestmentID = new com.see.truetransact.uicomponent.CLabel();
        panInvestmentID = new com.see.truetransact.uicomponent.CPanel();
        txtInvestmentID = new com.see.truetransact.uicomponent.CTextField();
        btnInvestmentID = new com.see.truetransact.uicomponent.CButton();
        lblInvestmentName = new com.see.truetransact.uicomponent.CLabel();
        lblLastIntPaidDate = new com.see.truetransact.uicomponent.CLabel();
        lblFaceValue = new com.see.truetransact.uicomponent.CLabel();
        panInvestmentName = new com.see.truetransact.uicomponent.CPanel();
        txtInvestmentName = new com.see.truetransact.uicomponent.CTextField();
        btnInvestmentName = new com.see.truetransact.uicomponent.CButton();
        lblFaceValueValue = new com.see.truetransact.uicomponent.CLabel();
        lblLastIntPaidDateValue = new com.see.truetransact.uicomponent.CLabel();
        lblAvailableNoOfUnits = new com.see.truetransact.uicomponent.CLabel();
        lblIssueDate = new com.see.truetransact.uicomponent.CLabel();
        lblAvailableNoOfUnitsValue = new com.see.truetransact.uicomponent.CLabel();
        lblIssueDateValue = new com.see.truetransact.uicomponent.CLabel();
        lblClassification = new com.see.truetransact.uicomponent.CLabel();
        lblClassificationValue = new com.see.truetransact.uicomponent.CLabel();
        panPurchaseOrSale = new com.see.truetransact.uicomponent.CPanel();
        rdoShfifting = new com.see.truetransact.uicomponent.CRadioButton();
        rdoAmortization = new com.see.truetransact.uicomponent.CRadioButton();
        lblTransType = new com.see.truetransact.uicomponent.CLabel();
        btnViewAmrot = new com.see.truetransact.uicomponent.CButton();
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
        setMaximumSize(new java.awt.Dimension(800, 600));
        setMinimumSize(new java.awt.Dimension(800, 600));
        setPreferredSize(new java.awt.Dimension(800, 600));

        panInvestmentTrans.setMinimumSize(new java.awt.Dimension(790, 525));
        panInvestmentTrans.setPreferredSize(new java.awt.Dimension(800, 600));
        panInvestmentTrans.setLayout(new java.awt.GridBagLayout());

        tabClosingType.setMinimumSize(new java.awt.Dimension(400, 275));
        tabClosingType.setPreferredSize(new java.awt.Dimension(400, 275));

        panClosingPosition.setMinimumSize(new java.awt.Dimension(250, 300));
        panClosingPosition.setPreferredSize(new java.awt.Dimension(250, 300));
        panClosingPosition.setLayout(new java.awt.GridBagLayout());

        lblInvestmentAmount.setText("Investment Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panClosingPosition.add(lblInvestmentAmount, gridBagConstraints);

        txtInvestmentAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtInvestmentAmount.setEnabled(false);
        txtInvestmentAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtInvestmentAmountActionPerformed(evt);
            }
        });
        txtInvestmentAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtInvestmentAmountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panClosingPosition.add(txtInvestmentAmount, gridBagConstraints);

        lblPurchaseDt.setText("Purchase Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panClosingPosition.add(lblPurchaseDt, gridBagConstraints);

        tdtPurchaseDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtPurchaseDtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panClosingPosition.add(tdtPurchaseDt, gridBagConstraints);

        panShfiting.setMinimumSize(new java.awt.Dimension(270, 25));
        panShfiting.setPreferredSize(new java.awt.Dimension(270, 25));
        panShfiting.setLayout(new java.awt.GridBagLayout());

        rdoHfm.setText("HFM");
        rdoHfm.setMaximumSize(new java.awt.Dimension(60, 18));
        rdoHfm.setMinimumSize(new java.awt.Dimension(60, 18));
        rdoHfm.setPreferredSize(new java.awt.Dimension(60, 18));
        rdoHfm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoHfmActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panShfiting.add(rdoHfm, gridBagConstraints);

        rdoHft.setText("HFT");
        rdoHft.setMaximumSize(new java.awt.Dimension(50, 18));
        rdoHft.setMinimumSize(new java.awt.Dimension(50, 18));
        rdoHft.setPreferredSize(new java.awt.Dimension(50, 18));
        rdoHft.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoHftActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panShfiting.add(rdoHft, gridBagConstraints);

        lblClassifaction1.setText("Classification");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panShfiting.add(lblClassifaction1, gridBagConstraints);

        rdoHfs.setText("HFS");
        rdoHfs.setMaximumSize(new java.awt.Dimension(77, 18));
        rdoHfs.setMinimumSize(new java.awt.Dimension(77, 18));
        rdoHfs.setPreferredSize(new java.awt.Dimension(77, 18));
        rdoHfs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoHfsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panShfiting.add(rdoHfs, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        panClosingPosition.add(panShfiting, gridBagConstraints);

        lblPurchaseRate.setText("Purchase Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panClosingPosition.add(lblPurchaseRate, gridBagConstraints);

        txtPurchaseRate.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPurchaseRate.setEnabled(false);
        txtPurchaseRate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPurchaseRateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panClosingPosition.add(txtPurchaseRate, gridBagConstraints);

        tabClosingType.addTab("Investment Transaction Details", panClosingPosition);

        panTransaction.setPreferredSize(new java.awt.Dimension(800, 380));
        panTransaction.setLayout(new java.awt.GridBagLayout());
        tabClosingType.addTab("Transaction", panTransaction);

        panAmortizationDetails.setPreferredSize(new java.awt.Dimension(800, 380));
        panAmortizationDetails.setLayout(new java.awt.GridBagLayout());

        srpTblAmortizationDetails.setMinimumSize(new java.awt.Dimension(780, 125));
        srpTblAmortizationDetails.setPreferredSize(new java.awt.Dimension(780, 125));

        tblAmortizationDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Serial No.", "Issue Date ", "No of Shares", "Share Value", "Status", "add/withdrawl"
            }
        ));
        tblAmortizationDetails.setMinimumSize(new java.awt.Dimension(225, 16));
        tblAmortizationDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblAmortizationDetailsMousePressed(evt);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblAmortizationDetailsMouseClicked(evt);
            }
        });
        srpTblAmortizationDetails.setViewportView(tblAmortizationDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 13, 0);
        panAmortizationDetails.add(srpTblAmortizationDetails, gridBagConstraints);

        panPurchaseOrSale1.setMaximumSize(new java.awt.Dimension(315, 23));
        panPurchaseOrSale1.setMinimumSize(new java.awt.Dimension(345, 23));
        panPurchaseOrSale1.setPreferredSize(new java.awt.Dimension(345, 23));
        panPurchaseOrSale1.setLayout(new java.awt.GridBagLayout());

        lblTotAmrtAmt.setText("Total Amortization Amount :");
        lblTotAmrtAmt.setMaximumSize(new java.awt.Dimension(200, 18));
        lblTotAmrtAmt.setMinimumSize(new java.awt.Dimension(200, 18));
        lblTotAmrtAmt.setPreferredSize(new java.awt.Dimension(200, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPurchaseOrSale1.add(lblTotAmrtAmt, gridBagConstraints);

        lblAmrtAmtValue.setMaximumSize(new java.awt.Dimension(120, 20));
        lblAmrtAmtValue.setMinimumSize(new java.awt.Dimension(120, 20));
        lblAmrtAmtValue.setPreferredSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panPurchaseOrSale1.add(lblAmrtAmtValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panAmortizationDetails.add(panPurchaseOrSale1, gridBagConstraints);

        tabClosingType.addTab("Amortization Details", panAmortizationDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInvestmentTrans.add(tabClosingType, gridBagConstraints);

        panDepositDetails.setMaximumSize(new java.awt.Dimension(380, 225));
        panDepositDetails.setMinimumSize(new java.awt.Dimension(380, 225));
        panDepositDetails.setPreferredSize(new java.awt.Dimension(380, 225));
        panDepositDetails.setLayout(new java.awt.GridBagLayout());

        lblInterestPaymentFrequency.setText("Interest Payment Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails.add(lblInterestPaymentFrequency, gridBagConstraints);

        lblInterestPaymentFrequencyValue.setMaximumSize(new java.awt.Dimension(100, 16));
        lblInterestPaymentFrequencyValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblInterestPaymentFrequencyValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails.add(lblInterestPaymentFrequencyValue, gridBagConstraints);

        lblPeriod.setText("Period");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails.add(lblPeriod, gridBagConstraints);

        lblPeriodValue.setMaximumSize(new java.awt.Dimension(100, 16));
        lblPeriodValue.setMinimumSize(new java.awt.Dimension(200, 16));
        lblPeriodValue.setPreferredSize(new java.awt.Dimension(200, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails.add(lblPeriodValue, gridBagConstraints);

        lblMaturityDate.setText("Maturity Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails.add(lblMaturityDate, gridBagConstraints);

        lblMaturityDateValue.setMaximumSize(new java.awt.Dimension(100, 16));
        lblMaturityDateValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblMaturityDateValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails.add(lblMaturityDateValue, gridBagConstraints);

        lblCoupenRate.setText("Coupen Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails.add(lblCoupenRate, gridBagConstraints);

        lblCoupenValue.setMaximumSize(new java.awt.Dimension(100, 16));
        lblCoupenValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblCoupenValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails.add(lblCoupenValue, gridBagConstraints);

        lblTotalInvestmentAmount.setText("Total Investment Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails.add(lblTotalInvestmentAmount, gridBagConstraints);

        lblTotalInvestmentAmountValue.setMaximumSize(new java.awt.Dimension(100, 16));
        lblTotalInvestmentAmountValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblTotalInvestmentAmountValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails.add(lblTotalInvestmentAmountValue, gridBagConstraints);

        lblTotalInterestCollected.setText("Total Interest Collected");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails.add(lblTotalInterestCollected, gridBagConstraints);

        lblTotalInterestCollectedValue.setMaximumSize(new java.awt.Dimension(100, 16));
        lblTotalInterestCollectedValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblTotalInterestCollectedValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails.add(lblTotalInterestCollectedValue, gridBagConstraints);

        lblTotalPremiumPaid.setText("Total Premium Paid");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails.add(lblTotalPremiumPaid, gridBagConstraints);

        lblTotalPremiumPaidValue.setMaximumSize(new java.awt.Dimension(100, 16));
        lblTotalPremiumPaidValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblTotalPremiumPaidValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails.add(lblTotalPremiumPaidValue, gridBagConstraints);

        lblTotalPremiumCollected.setText("Total Premium Collected");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails.add(lblTotalPremiumCollected, gridBagConstraints);

        lblTotalPremiumCollectedValue.setMaximumSize(new java.awt.Dimension(100, 16));
        lblTotalPremiumCollectedValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblTotalPremiumCollectedValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails.add(lblTotalPremiumCollectedValue, gridBagConstraints);

        lblTotalInterestPaid.setText("Total Interest Paid");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails.add(lblTotalInterestPaid, gridBagConstraints);

        lblTotalInterestPaidValue.setMaximumSize(new java.awt.Dimension(100, 16));
        lblTotalInterestPaidValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblTotalInterestPaidValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails.add(lblTotalInterestPaidValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        panInvestmentTrans.add(panDepositDetails, gridBagConstraints);

        panDepositDetails1.setMaximumSize(new java.awt.Dimension(380, 225));
        panDepositDetails1.setMinimumSize(new java.awt.Dimension(380, 225));
        panDepositDetails1.setPreferredSize(new java.awt.Dimension(380, 225));
        panDepositDetails1.setLayout(new java.awt.GridBagLayout());

        lblInvestmentBehaves.setText("Behaves Like");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails1.add(lblInvestmentBehaves, gridBagConstraints);

        cboInvestmentBehaves.setMinimumSize(new java.awt.Dimension(100, 21));
        cboInvestmentBehaves.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboInvestmentBehavesItemStateChanged(evt);
            }
        });
        cboInvestmentBehaves.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboInvestmentBehavesActionPerformed(evt);
            }
        });
        cboInvestmentBehaves.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboInvestmentBehavesFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDepositDetails1.add(cboInvestmentBehaves, gridBagConstraints);

        lblInvestmentID.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblInvestmentID.setText("Investment ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panDepositDetails1.add(lblInvestmentID, gridBagConstraints);

        panInvestmentID.setLayout(new java.awt.GridBagLayout());

        txtInvestmentID.setEditable(false);
        txtInvestmentID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtInvestmentID.setEnabled(false);
        txtInvestmentID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtInvestmentIDFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInvestmentID.add(txtInvestmentID, gridBagConstraints);

        btnInvestmentID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnInvestmentID.setMinimumSize(new java.awt.Dimension(21, 21));
        btnInvestmentID.setPreferredSize(new java.awt.Dimension(21, 21));
        btnInvestmentID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInvestmentIDActionPerformed(evt);
            }
        });
        btnInvestmentID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                btnInvestmentIDFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panInvestmentID.add(btnInvestmentID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 4);
        panDepositDetails1.add(panInvestmentID, gridBagConstraints);

        lblInvestmentName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblInvestmentName.setText("Investment Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panDepositDetails1.add(lblInvestmentName, gridBagConstraints);

        lblLastIntPaidDate.setText("Last Interest Paid Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panDepositDetails1.add(lblLastIntPaidDate, gridBagConstraints);

        lblFaceValue.setText("Face value");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panDepositDetails1.add(lblFaceValue, gridBagConstraints);

        panInvestmentName.setMinimumSize(new java.awt.Dimension(231, 29));
        panInvestmentName.setPreferredSize(new java.awt.Dimension(231, 29));
        panInvestmentName.setLayout(new java.awt.GridBagLayout());

        txtInvestmentName.setEditable(false);
        txtInvestmentName.setMinimumSize(new java.awt.Dimension(200, 21));
        txtInvestmentName.setPreferredSize(new java.awt.Dimension(200, 21));
        txtInvestmentName.setEnabled(false);
        txtInvestmentName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtInvestmentNameFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInvestmentName.add(txtInvestmentName, gridBagConstraints);

        btnInvestmentName.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnInvestmentName.setMinimumSize(new java.awt.Dimension(21, 21));
        btnInvestmentName.setPreferredSize(new java.awt.Dimension(21, 21));
        btnInvestmentName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInvestmentNameActionPerformed(evt);
            }
        });
        btnInvestmentName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                btnInvestmentNameFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panInvestmentName.add(btnInvestmentName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 4);
        panDepositDetails1.add(panInvestmentName, gridBagConstraints);

        lblFaceValueValue.setMaximumSize(new java.awt.Dimension(120, 18));
        lblFaceValueValue.setMinimumSize(new java.awt.Dimension(120, 18));
        lblFaceValueValue.setPreferredSize(new java.awt.Dimension(120, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDepositDetails1.add(lblFaceValueValue, gridBagConstraints);

        lblLastIntPaidDateValue.setMaximumSize(new java.awt.Dimension(120, 18));
        lblLastIntPaidDateValue.setMinimumSize(new java.awt.Dimension(120, 18));
        lblLastIntPaidDateValue.setPreferredSize(new java.awt.Dimension(120, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDepositDetails1.add(lblLastIntPaidDateValue, gridBagConstraints);

        lblAvailableNoOfUnits.setText("Available No. Of Units");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails1.add(lblAvailableNoOfUnits, gridBagConstraints);

        lblIssueDate.setText("Issue Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails1.add(lblIssueDate, gridBagConstraints);

        lblAvailableNoOfUnitsValue.setMaximumSize(new java.awt.Dimension(100, 16));
        lblAvailableNoOfUnitsValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblAvailableNoOfUnitsValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails1.add(lblAvailableNoOfUnitsValue, gridBagConstraints);

        lblIssueDateValue.setMaximumSize(new java.awt.Dimension(100, 16));
        lblIssueDateValue.setMinimumSize(new java.awt.Dimension(100, 16));
        lblIssueDateValue.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDepositDetails1.add(lblIssueDateValue, gridBagConstraints);

        lblClassification.setText("Classification");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panDepositDetails1.add(lblClassification, gridBagConstraints);

        lblClassificationValue.setMaximumSize(new java.awt.Dimension(120, 18));
        lblClassificationValue.setMinimumSize(new java.awt.Dimension(120, 18));
        lblClassificationValue.setPreferredSize(new java.awt.Dimension(120, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDepositDetails1.add(lblClassificationValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panInvestmentTrans.add(panDepositDetails1, gridBagConstraints);

        panPurchaseOrSale.setMaximumSize(new java.awt.Dimension(315, 23));
        panPurchaseOrSale.setMinimumSize(new java.awt.Dimension(345, 23));
        panPurchaseOrSale.setPreferredSize(new java.awt.Dimension(345, 23));
        panPurchaseOrSale.setLayout(new java.awt.GridBagLayout());

        rdoShfifting.setText("Shifting");
        rdoShfifting.setMaximumSize(new java.awt.Dimension(90, 18));
        rdoShfifting.setMinimumSize(new java.awt.Dimension(90, 18));
        rdoShfifting.setPreferredSize(new java.awt.Dimension(90, 18));
        rdoShfifting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoShfiftingActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panPurchaseOrSale.add(rdoShfifting, gridBagConstraints);

        rdoAmortization.setText("Amortization");
        rdoAmortization.setMaximumSize(new java.awt.Dimension(100, 18));
        rdoAmortization.setMinimumSize(new java.awt.Dimension(100, 18));
        rdoAmortization.setPreferredSize(new java.awt.Dimension(100, 18));
        rdoAmortization.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoAmortizationActionPerformed(evt);
            }
        });
        rdoAmortization.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                rdoAmortizationFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panPurchaseOrSale.add(rdoAmortization, gridBagConstraints);

        lblTransType.setText("TransType");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panPurchaseOrSale.add(lblTransType, gridBagConstraints);

        btnViewAmrot.setText("View");
        btnViewAmrot.setMaximumSize(new java.awt.Dimension(63, 23));
        btnViewAmrot.setMinimumSize(new java.awt.Dimension(63, 23));
        btnViewAmrot.setPreferredSize(new java.awt.Dimension(63, 23));
        btnViewAmrot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewAmrotActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPurchaseOrSale.add(btnViewAmrot, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        panInvestmentTrans.add(panPurchaseOrSale, gridBagConstraints);

        getContentPane().add(panInvestmentTrans, java.awt.BorderLayout.CENTER);

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
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
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

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
          HashMap reportParamMap = new HashMap();
          com.see.truetransact.clientutil.ttrintegration.LinkReport.getReports(getScreenID(), reportParamMap);
          System.out.println("btnPrintActionPerformed ====== "+getScreenID());
    }//GEN-LAST:event_btnPrintActionPerformed
    
    private void tblAmortizationDetailsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAmortizationDetailsMousePressed
        // TODO add your handling code here:
        String InvestmentName= CommonUtil.convertObjToStr(tblAmortizationDetails.getValueAt(tblAmortizationDetails.getSelectedRow(),2));
        HashMap whereMap=new HashMap();
        whereMap.put(CommonConstants.MAP_WHERE, InvestmentName);
        whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
        whereMap.put("MASTER","MASTER");
        observable.populateData(whereMap);
        whereMap=null;
    }//GEN-LAST:event_tblAmortizationDetailsMousePressed
    
    private void tblAmortizationDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAmortizationDetailsMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblAmortizationDetailsMouseClicked
    
    private void btnViewAmrotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewAmrotActionPerformed
        // TODO add your handling code here:
        observable.resetTable();
        HashMap whereMap=new HashMap();
        whereMap.put("AMORTIZATIONCALC","AMORTIZATIONCALC");
        whereMap.put("AUTHORIZE_STATUS","AUTHORIZED");
        String InvestmentType=CommonUtil.convertObjToStr(cboInvestmentBehaves.getSelectedItem());
        if(!InvestmentType.equals("") && InvestmentType.length()>0){
            observable.setCboInvestmentBehaves(CommonUtil.convertObjToStr(cboInvestmentBehaves.getSelectedItem()));
            InvestmentType=observable.callForBehaves();
            whereMap.put("INVESTMENT_TYPE",InvestmentType);
        }
        if(!CommonUtil.convertObjToStr(txtInvestmentID.getText()).equals("")){
            whereMap.put("INVESTMENT_PROD_ID",CommonUtil.convertObjToStr(txtInvestmentID.getText()));
        }
        if(!CommonUtil.convertObjToStr(txtInvestmentName.getText()).equals("")){
            whereMap.put("INVESTMENT_NAME",CommonUtil.convertObjToStr(txtInvestmentName.getText()));
        }
        observable.populateData(whereMap);
        lblAmrtAmtValue.setText("");
        observable.resetForm();
        ClientUtil.enableDisable(panDepositDetails1,false);
        btnInvestmentID.setEnabled(false);
        btnInvestmentName.setEnabled(false);
        lblAmrtAmtValue.setText(CommonUtil.convertObjToStr(new Double(calTotAmortizationAmount())));
        
    }//GEN-LAST:event_btnViewAmrotActionPerformed
    
    private double calTotAmortizationAmount(){
        int tabSize=tblAmortizationDetails.getRowCount();
        double totAmrAmount=0.0;
        if(tabSize>0){
            for(int i=0;i<tabSize;i++){
                totAmrAmount=totAmrAmount+CommonUtil.convertObjToDouble(tblAmortizationDetails.getValueAt(i,5)).doubleValue();
                System.out.println("i------>"+i);
            }
            
        }
        
        return totAmrAmount;
    }
    private void rdoAmortizationFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rdoAmortizationFocusLost
        // TODO add your handling code here:
        if(rdoAmortization.isSelected()==true){
            btnViewAmrot.setVisible(true);
        }
        
        
    }//GEN-LAST:event_rdoAmortizationFocusLost
    
    private void rdoAmortizationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoAmortizationActionPerformed
        // TODO add your handling code here:
        
        if(rdoAmortization.isSelected()==true){
            //            btnCancelActionPerformed(null);
            ClientUtil.enableDisable(panDepositDetails1,false);
            
            tabClosingType.remove(panClosingPosition);
            tabClosingType.add("Amortization Details",panAmortizationDetails);
            btnViewAmrot.setVisible(true);
            //            panAmortizationDetails.setName();
            rdoShfifting.setSelected(false);
            btnInvestmentID.setEnabled(true);
            btnInvestmentName.setEnabled(true);
            cboInvestmentBehaves.setEnabled(true);
            
            
        }
    }//GEN-LAST:event_rdoAmortizationActionPerformed
    
    private void rdoShfiftingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoShfiftingActionPerformed
        // TODO add your handling code here:
        if(rdoShfifting.isSelected()==true){
            //            btnCancelActionPerformed(null);
            tabClosingType.add("Investment Transaction Details",panClosingPosition);
            tabClosingType.remove(panAmortizationDetails);
            //            panClosingPosition.setName("");
            observable.resetTable();
            observable.resetForm();
            lblAmrtAmtValue.setText("");
            rdoAmortization.setSelected(false);
            btnViewAmrot.setVisible(false);
            ClientUtil.enableDisable(panDepositDetails1,false);
            btnInvestmentID.setEnabled(true);
            btnInvestmentName.setEnabled(true);
            cboInvestmentBehaves.setEnabled(true);
            
        }
    }//GEN-LAST:event_rdoShfiftingActionPerformed
    
    private void txtInvestmentNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInvestmentNameFocusLost
        // TODO add your handling code here:
        //        boolean exist=false;
        //        exist=observable.isInvsetMentMasterTypeExists(CommonUtil.convertObjToStr(txtInvestmentName.getText()));
        //       if(exist==true){
        //           ClientUtil.displayAlert("This Investment  Shifited  Authorization Pending");
        //       }
    }//GEN-LAST:event_txtInvestmentNameFocusLost
    
    private void rdoHftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoHftActionPerformed
        // TODO add your handling code here:
        if(rdoHft.isSelected()==true){
            rdoHfm.setSelected(false);
            rdoHfs.setSelected(false);
        }
    }//GEN-LAST:event_rdoHftActionPerformed
    
    private void rdoHfsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoHfsActionPerformed
        // TODO add your handling code here:
        if(rdoHfs.isSelected()==true){
            rdoHfm.setSelected(false);
            rdoHft.setSelected(false);
        }
    }//GEN-LAST:event_rdoHfsActionPerformed
    
    private void rdoHfmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoHfmActionPerformed
        // TODO add your handling code here:
        if(rdoHfm.isSelected()==true){
            rdoHfs.setSelected(false);
            rdoHft.setSelected(false);
        }
    }//GEN-LAST:event_rdoHfmActionPerformed
    
    private void txtPurchaseRateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPurchaseRateFocusLost
        // TODO add your handling code here:
        double purchasRate=CommonUtil.convertObjToDouble(txtPurchaseRate.getText()).doubleValue();
        double faceValue=observable.getFaceValue().doubleValue();
        double premiumPaid=observable.getTotalPremiumPaid().doubleValue();
        double outStandingAmount=observable.getOutstandingAmount().doubleValue();
        double premiumRecived=observable.getTotalPremiumCollected().doubleValue();
        double avlUnits=observable.getAvailableNoOfUnits().doubleValue();
        double shftingAmount=faceValue*avlUnits;
        double totInvsetOutStanding=outStandingAmount+premiumPaid-premiumRecived;
        double presentOutStandingAmount=purchasRate*observable.getAvailableNoOfUnits().doubleValue();
        double provisionAmount=0.0;
        if(totInvsetOutStanding>presentOutStandingAmount){
            provisionAmount= totInvsetOutStanding-presentOutStandingAmount;
            System.out.println("provisionAmount--->"+provisionAmount);
        }
        txtInvestmentAmount.setText(String.valueOf(provisionAmount));
        
    }//GEN-LAST:event_txtPurchaseRateFocusLost
    
    private void txtInvestmentAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtInvestmentAmountActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_txtInvestmentAmountActionPerformed
    
    private void txtInvestmentAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInvestmentAmountFocusLost
        // TODO add your handling code here:
        
    }//GEN-LAST:event_txtInvestmentAmountFocusLost
    
    private void btnNewFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnNewFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_btnNewFocusLost
    
    private void btnInvestmentNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnInvestmentNameFocusLost
        // TODO add your handling code here:
        //        boolean exist=false;
        //       exist=observable.isInvsetMentMasterTypeExists(CommonUtil.convertObjToStr(txtInvestmentName.getText()));
        //       if(exist==true){
        //           ClientUtil.displayAlert("This Investment  Shifited  Authorization Pending");
        //       }
        btnInvestmentID.setEnabled(false);
    }//GEN-LAST:event_btnInvestmentNameFocusLost
    
    private void btnInvestmentIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnInvestmentIDFocusLost
        // TODO add your handling code here:
        cboInvestmentBehaves.setEditable(false);
        cboInvestmentBehaves.setEnabled(false);
    }//GEN-LAST:event_btnInvestmentIDFocusLost
    
    private void txtInvestmentIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInvestmentIDFocusLost
        // TODO add your handling code here:
        cboInvestmentBehaves.setEditable(false);
        cboInvestmentBehaves.setEnabled(false);
    }//GEN-LAST:event_txtInvestmentIDFocusLost
    
    private void cboInvestmentBehavesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboInvestmentBehavesFocusLost
        // TODO add your handling code here:
        
    }//GEN-LAST:event_cboInvestmentBehavesFocusLost
    
    private void tdtPurchaseDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtPurchaseDtFocusLost
        // TODO add your handling code here:
        if(tdtPurchaseDt.getDateValue().length()>0 && !tdtPurchaseDt.getDateValue().equals("")){
            Date mtDat=DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(lblMaturityDateValue.getText()));
            Date issueDat=DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(lblIssueDateValue.getText()));
            Date purchaseDate=DateUtil.getDateMMDDYYYY(tdtPurchaseDt.getDateValue());
            if(DateUtil.dateDiff(issueDat,purchaseDate)<0 ){
                ClientUtil.displayAlert("Purchase Date Should be Greater Than issue Date");
                tdtPurchaseDt.setDateValue(null);
                tdtPurchaseDt.requestFocus();
            }
            if(DateUtil.dateDiff(purchaseDate,mtDat)<=0 ){
                ClientUtil.displayAlert("Purchase Date Should be less Than Maturity  Date");
                tdtPurchaseDt.setDateValue(null);
                tdtPurchaseDt.requestFocus();
            }
            
        }
        //        if(rdoPurchase.isSelected()==false &&  rdoSale.isSelected()==false){
        //            ClientUtil.displayAlert("Please Slect Purchase Or sale");
        //            tdtPurchaseDt.setDateValue(null);
        //            tdtPurchaseDt.requestFocus();
        //
        //        }
    }//GEN-LAST:event_tdtPurchaseDtFocusLost
    
    private void btnInvestmentNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInvestmentNameActionPerformed
        // TODO add your handling code here:
        callView("InvestmentName");
        if(rdoAmortization.isSelected()==true)
            btnViewAmrotActionPerformed(null);
    }//GEN-LAST:event_btnInvestmentNameActionPerformed
    
    private void btnInvestmentIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInvestmentIDActionPerformed
        // TODO add your handling code here:
        callView("InvestmentProduct");
    }//GEN-LAST:event_btnInvestmentIDActionPerformed
    
    private void cboInvestmentBehavesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboInvestmentBehavesActionPerformed
        //        // TODO add your handling code here:
        btnInvestmentID.setEnabled(true);
        
    }//GEN-LAST:event_cboInvestmentBehavesActionPerformed
    
    private void cboInvestmentBehavesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboInvestmentBehavesItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cboInvestmentBehavesItemStateChanged
    
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
        super.removeEditLock(((ComboBoxModel)cboInvestmentBehaves.getModel()).getKeyForSelected().toString());
        observable.resetForm();
        lblAmrtAmtValue.setText("");
        observable.resetTable();
        
        resetTransactionUI();
        lblPeriodValue.setText("");
        ClientUtil.clearAll(this);
        //        ClientUtil.enableDisable(panShareProduct, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        ClientUtil.enableDisable(panInvestmentTrans,false);
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
        //        tdtMaturityDate.setEnabled(false);
        txtInvestmentName.setEditable(false);
        txtInvestmentName.setEnabled(false);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        cboInvestmentBehaves.setEnabled(false);
        cboInvestmentBehaves.setEditable(false);
        transactionUI.setSourceScreen("REMITISSUE");
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_EDIT);
        
        
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        setModified(false);
        savePerformed();
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
        // TODO add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.resetForm();
        lblAmrtAmtValue.setText("");
        observable.resetTable();
        lblPeriodValue.setText("");
        ClientUtil.enableDisable(panInvestmentTrans,true);
        setButtonEnableDisable();
        setHelpButtonEnableDisable(true);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        transactionUI.cancelAction(false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.resetObjects();
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
        resetTransactionUI();
        ClientUtil.enableDisable(panDepositDetails1,false);
        btnInvestmentID.setEnabled(false);
        btnInvestmentName.setEnabled(false);
        
    }//GEN-LAST:event_btnNewActionPerformed
    private void addingRMDeposits(){
        
        ComboBoxModel objDepModel = new ComboBoxModel();
        objDepModel.addKeyAndElement(" ", observable.getCbmInvestmentBehaves().getDataForKey(" "));
        objDepModel.addKeyAndElement("BONDS", observable.getCbmInvestmentBehaves().getDataForKey("Bonds"));
        
        String prodType = CommonUtil.convertObjToStr((((ComboBoxModel)(cboInvestmentBehaves).getModel())).getKeyForSelected());
        observable.setCbmInvestmentBehaves(objDepModel);
        cboInvestmentBehaves.setModel(observable.getCbmInvestmentBehaves());
        cboInvestmentBehaves.setSelectedItem(((ComboBoxModel)cboInvestmentBehaves.getModel()).getDataForKey(prodType));
        
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
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnInvestmentID;
    private com.see.truetransact.uicomponent.CButton btnInvestmentName;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CButton btnViewAmrot;
    private com.see.truetransact.uicomponent.CComboBox cboInvestmentBehaves;
    private com.see.truetransact.uicomponent.CLabel lblAmrtAmtValue;
    private com.see.truetransact.uicomponent.CLabel lblAvailableNoOfUnits;
    private com.see.truetransact.uicomponent.CLabel lblAvailableNoOfUnitsValue;
    private com.see.truetransact.uicomponent.CLabel lblClassifaction1;
    private com.see.truetransact.uicomponent.CLabel lblClassification;
    private com.see.truetransact.uicomponent.CLabel lblClassificationValue;
    private com.see.truetransact.uicomponent.CLabel lblCoupenRate;
    private com.see.truetransact.uicomponent.CLabel lblCoupenValue;
    private com.see.truetransact.uicomponent.CLabel lblFaceValue;
    private com.see.truetransact.uicomponent.CLabel lblFaceValueValue;
    private com.see.truetransact.uicomponent.CLabel lblInterestPaymentFrequency;
    private com.see.truetransact.uicomponent.CLabel lblInterestPaymentFrequencyValue;
    private com.see.truetransact.uicomponent.CLabel lblInvestmentAmount;
    private com.see.truetransact.uicomponent.CLabel lblInvestmentBehaves;
    private com.see.truetransact.uicomponent.CLabel lblInvestmentID;
    private com.see.truetransact.uicomponent.CLabel lblInvestmentName;
    private com.see.truetransact.uicomponent.CLabel lblIssueDate;
    private com.see.truetransact.uicomponent.CLabel lblIssueDateValue;
    private com.see.truetransact.uicomponent.CLabel lblLastIntPaidDate;
    private com.see.truetransact.uicomponent.CLabel lblLastIntPaidDateValue;
    private com.see.truetransact.uicomponent.CLabel lblMaturityDate;
    private com.see.truetransact.uicomponent.CLabel lblMaturityDateValue;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblPeriod;
    private com.see.truetransact.uicomponent.CLabel lblPeriodValue;
    private com.see.truetransact.uicomponent.CLabel lblPurchaseDt;
    private com.see.truetransact.uicomponent.CLabel lblPurchaseRate;
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
    private com.see.truetransact.uicomponent.CLabel lblTotAmrtAmt;
    private com.see.truetransact.uicomponent.CLabel lblTotalInterestCollected;
    private com.see.truetransact.uicomponent.CLabel lblTotalInterestCollectedValue;
    private com.see.truetransact.uicomponent.CLabel lblTotalInterestPaid;
    private com.see.truetransact.uicomponent.CLabel lblTotalInterestPaidValue;
    private com.see.truetransact.uicomponent.CLabel lblTotalInvestmentAmount;
    private com.see.truetransact.uicomponent.CLabel lblTotalInvestmentAmountValue;
    private com.see.truetransact.uicomponent.CLabel lblTotalPremiumCollected;
    private com.see.truetransact.uicomponent.CLabel lblTotalPremiumCollectedValue;
    private com.see.truetransact.uicomponent.CLabel lblTotalPremiumPaid;
    private com.see.truetransact.uicomponent.CLabel lblTotalPremiumPaidValue;
    private com.see.truetransact.uicomponent.CLabel lblTransType;
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
    private com.see.truetransact.uicomponent.CPanel panClosingPosition;
    private com.see.truetransact.uicomponent.CPanel panDepositDetails;
    private com.see.truetransact.uicomponent.CPanel panDepositDetails1;
    private com.see.truetransact.uicomponent.CPanel panInvestmentID;
    private com.see.truetransact.uicomponent.CPanel panInvestmentName;
    private com.see.truetransact.uicomponent.CPanel panInvestmentTrans;
    private com.see.truetransact.uicomponent.CPanel panPurchaseOrSale;
    private com.see.truetransact.uicomponent.CPanel panPurchaseOrSale1;
    private com.see.truetransact.uicomponent.CPanel panShfiting;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTransaction;
    private com.see.truetransact.uicomponent.CRadioButton rdoAmortization;
    private com.see.truetransact.uicomponent.CRadioButton rdoHfm;
    private com.see.truetransact.uicomponent.CRadioButton rdoHfs;
    private com.see.truetransact.uicomponent.CRadioButton rdoHft;
    private com.see.truetransact.uicomponent.CRadioButton rdoShfifting;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CScrollPane srpTblAmortizationDetails;
    private com.see.truetransact.uicomponent.CTabbedPane tabClosingType;
    private com.see.truetransact.uicomponent.CTable tblAmortizationDetails;
    private javax.swing.JToolBar tbrShareProduct;
    private com.see.truetransact.uicomponent.CDateField tdtPurchaseDt;
    private com.see.truetransact.uicomponent.CTextField txtInvestmentAmount;
    private com.see.truetransact.uicomponent.CTextField txtInvestmentID;
    private com.see.truetransact.uicomponent.CTextField txtInvestmentName;
    private com.see.truetransact.uicomponent.CTextField txtPurchaseRate;
    // End of variables declaration//GEN-END:variables
    
}
