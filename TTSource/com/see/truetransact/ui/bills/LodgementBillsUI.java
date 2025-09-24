/* Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LodgementBillsUI.java
 *
 * Created on March 15, 2004, 12:56 PM
 */

package com.see.truetransact.ui.bills;

import com.see.truetransact.ui.bills.LodgementBillsRB;
import com.see.truetransact.ui.bills.LodgementBillsMRB;
import com.see.truetransact.ui.bills.LodgementBillsOB;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.PincodeValidation_IN;
import com.see.truetransact.clientexception.ClientParseException;

import java.util.Observable;
import java.util.HashMap;
import java.util.Observer;
import java.util.List;
/**
 *
 * @author  Lohith R.
 */

public class LodgementBillsUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer,UIMandatoryField{
    
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private LodgementBillsOB observable;
    HashMap mandatoryMap;
    final int EDIT=0,DELETE=1,CUSTOMERID=2;
    int ACTION=-1;
    private StringBuffer customerName;
    private final String stringSpace = " ";
    
    /** Creates new form LodgementBillsUI */
    public LodgementBillsUI() {
        initComponents();
        initStartUP();
    }
    
    /** Initialzation of UI */
    private void initStartUP(){
        setObservable();
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setMaximumLength();
        setHelpMessage();
        initComponentData();
        ClientUtil.enableDisable(this, false);
        setButtonEnableDisable();
        observable.resetStatus();
        resetFields();
        
    }
    
    private void setObservable() {
        /* Implementing Singleton pattern */
        observable = LodgementBillsOB.getInstance();
        observable.addObserver(this);
    }
    
    private void setFieldNames() {
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnCustomerID.setName("btnCustomerID");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnSave.setName("btnSave");
        cTextField3.setName("cTextField3");
        cTextField4.setName("cTextField4");
        cboBankDetails.setName("cboBankDetails");
        cboDispatchDetailsCity.setName("cboDispatchDetailsCity");
        cboDispatchDetailsCountry.setName("cboDispatchDetailsCountry");
        cboDispatchDetailsState.setName("cboDispatchDetailsState");
        cboDraweeDetailsCity.setName("cboDraweeDetailsCity");
        cboDraweeDetailsCountry.setName("cboDraweeDetailsCountry");
        cboDraweeDetailsState.setName("cboDraweeDetailsState");
        cboProductID.setName("cboProductID");
        cboTypeOfBill.setName("cboTypeOfBill");
        lblAccHead.setName("lblAccHead");
        lblAccountHead.setName("lblAccountHead");
        lblAmountOfBill.setName("lblAmountOfBill");
        lblAmtOfBill.setName("lblAmtOfBill");
        lblBankDetails.setName("lblBankDetails");
        lblBorrowerNum.setName("lblBorrowerNum");
        lblCommission.setName("lblCommission");
        lblCustomerAccNo.setName("lblCustomerAccNo");
        lblCustomerAccountNumber.setName("lblCustomerAccountNumber");
        lblCustomerID.setName("lblCustomerID");
        lblCustomerName.setName("lblCustomerName");
        lblCustomerNm.setName("lblCustomerNm");
        lblDiscount.setName("lblDiscount");
        lblDispatchDetailsArea.setName("lblDispatchDetailsArea");
        lblDispatchDetailsCity.setName("lblDispatchDetailsCity");
        lblDispatchDetailsCountry.setName("lblDispatchDetailsCountry");
        lblDispatchDetailsName.setName("lblDispatchDetailsName");
        lblDispatchDetailsOthers.setName("lblDispatchDetailsOthers");
        lblDispatchDetailsPincode.setName("lblDispatchDetailsPincode");
        lblDispatchDetailsState.setName("lblDispatchDetailsState");
        lblDispatchDetailsStreet.setName("lblDispatchDetailsStreet");
        lblDraweeDetailsArea.setName("lblDraweeDetailsArea");
        lblDraweeDetailsCity.setName("lblDraweeDetailsCity");
        lblDraweeDetailsCountry.setName("lblDraweeDetailsCountry");
        lblDraweeDetailsName.setName("lblDraweeDetailsName");
        lblDraweeDetailsOthers.setName("lblDraweeDetailsOthers");
        lblDraweeDetailsPincode.setName("lblDraweeDetailsPincode");
        lblDraweeDetailsState.setName("lblDraweeDetailsState");
        lblDraweeDetailsStreet.setName("lblDraweeDetailsStreet");
        lblInstrumentDetails.setName("lblInstrumentDetails");
        lblMargin.setName("lblMargin");
        lblMsg.setName("lblMsg");
        lblPSRBOtherBanks.setName("lblPSRBOtherBanks");
        lblPostage.setName("lblPostage");
        lblProductID.setName("lblProductID");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblStatus.setName("lblStatus");
        lblTypeOfBill.setName("lblTypeOfBill");
        mbrMain.setName("mbrMain");
        panDispatchDetails.setName("panDispatchDetails");
        panDraweeDeatils.setName("panDraweeDeatils");
        panMain.setName("panMain");
        panProductID.setName("panProductID");
        panStatus.setName("panStatus");
        txtBorrowerNum.setName("txtBorrowerNum");
        txtCommission.setName("txtCommission");
        txtCustomerID.setName("txtCustomerID");
        txtDiscount.setName("txtDiscount");
        txtDispatchDetailsArea.setName("txtDispatchDetailsArea");
        txtDispatchDetailsName.setName("txtDispatchDetailsName");
        txtDispatchDetailsOthers.setName("txtDispatchDetailsOthers");
        txtDispatchDetailsPincode.setName("txtDispatchDetailsPincode");
        txtDispatchDetailsStreet.setName("txtDispatchDetailsStreet");
        txtDraweeDetailsArea.setName("txtDraweeDetailsArea");
        txtDraweeDetailsName.setName("txtDraweeDetailsName");
        txtDraweeDetailsOthers.setName("txtDraweeDetailsOthers");
        txtDraweeDetailsPincode.setName("txtDraweeDetailsPincode");
        txtDraweeDetailsStreet.setName("txtDraweeDetailsStreet");
        txtInstrumentDetails.setName("txtInstrumentDetails");
        txtMargin.setName("txtMargin");
        txtPSBROtherBanks.setName("txtPSBROtherBanks");
        txtPostage.setName("txtPostage");
        txtAmountOfBill.setName("txtAmountOfBill");
    }
    
    private void internationalize() {
        LodgementBillsRB resourceBundle = new LodgementBillsRB();
        lblDispatchDetailsArea.setText(resourceBundle.getString("lblDispatchDetailsArea"));
        lblDispatchDetailsName.setText(resourceBundle.getString("lblDispatchDetailsName"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        btnCustomerID.setText(resourceBundle.getString("btnCustomerID"));
        lblDispatchDetailsStreet.setText(resourceBundle.getString("lblDispatchDetailsStreet"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblAccHead.setText(resourceBundle.getString("lblAccHead"));
        lblDispatchDetailsCountry.setText(resourceBundle.getString("lblDispatchDetailsCountry"));
        lblCustomerName.setText(resourceBundle.getString("lblCustomerName"));
        lblDispatchDetailsState.setText(resourceBundle.getString("lblDispatchDetailsState"));
        lblDispatchDetailsCity.setText(resourceBundle.getString("lblDispatchDetailsCity"));
        lblDispatchDetailsOthers.setText(resourceBundle.getString("lblDispatchDetailsOthers"));
        lblDraweeDetailsArea.setText(resourceBundle.getString("lblDraweeDetailsArea"));
        lblDraweeDetailsName.setText(resourceBundle.getString("lblDraweeDetailsName"));
        lblDispatchDetailsPincode.setText(resourceBundle.getString("lblDispatchDetailsPincode"));
        lblAmtOfBill.setText(resourceBundle.getString("lblAmtOfBill"));
        lblBankDetails.setText(resourceBundle.getString("lblBankDetails"));
        lblCustomerID.setText(resourceBundle.getString("lblCustomerID"));
        lblCustomerAccNo.setText(resourceBundle.getString("lblCustomerAccNo"));
        lblDraweeDetailsCountry.setText(resourceBundle.getString("lblDraweeDetailsCountry"));
        lblAmountOfBill.setText(resourceBundle.getString("lblAmountOfBill"));
        lblDraweeDetailsStreet.setText(resourceBundle.getString("lblDraweeDetailsStreet"));
        lblPostage.setText(resourceBundle.getString("lblPostage"));
        lblDraweeDetailsState.setText(resourceBundle.getString("lblDraweeDetailsState"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblInstrumentDetails.setText(resourceBundle.getString("lblInstrumentDetails"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        lblCommission.setText(resourceBundle.getString("lblCommission"));
        lblDraweeDetailsPincode.setText(resourceBundle.getString("lblDraweeDetailsPincode"));
        lblProductID.setText(resourceBundle.getString("lblProductID"));
        lblDraweeDetailsCity.setText(resourceBundle.getString("lblDraweeDetailsCity"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblBorrowerNum.setText(resourceBundle.getString("lblBorrowerNum"));
        lblTypeOfBill.setText(resourceBundle.getString("lblTypeOfBill"));
        ((javax.swing.border.TitledBorder)panProductID.getBorder()).setTitle(resourceBundle.getString("panProductID"));
        lblDraweeDetailsOthers.setText(resourceBundle.getString("lblDraweeDetailsOthers"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        ((javax.swing.border.TitledBorder)panDraweeDeatils.getBorder()).setTitle(resourceBundle.getString("panDraweeDeatils"));
        lblDiscount.setText(resourceBundle.getString("lblDiscount"));
        lblAccountHead.setText(resourceBundle.getString("lblAccountHead"));
        lblCustomerNm.setText(resourceBundle.getString("lblCustomerNm"));
        lblPSRBOtherBanks.setText(resourceBundle.getString("lblPSRBOtherBanks"));
        ((javax.swing.border.TitledBorder)panDispatchDetails.getBorder()).setTitle(resourceBundle.getString("panDispatchDetails"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblMargin.setText(resourceBundle.getString("lblMargin"));
        lblCustomerAccountNumber.setText(resourceBundle.getString("lblCustomerAccountNumber"));
    }
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboProductID", new Boolean(true));
        mandatoryMap.put("cboTypeOfBill", new Boolean(true));
        mandatoryMap.put("cboBankDetails", new Boolean(true));
        mandatoryMap.put("txtCustomerID", new Boolean(true));
        mandatoryMap.put("txtInstrumentDetails", new Boolean(true));
        mandatoryMap.put("txtDiscount", new Boolean(true));
        mandatoryMap.put("txtMargin", new Boolean(true));
        mandatoryMap.put("txtBorrowerNum", new Boolean(true));
        mandatoryMap.put("txtPSBROtherBanks", new Boolean(true));
        mandatoryMap.put("txtCommission", new Boolean(true));
        mandatoryMap.put("txtPostage", new Boolean(true));
        mandatoryMap.put("txtDraweeDetailsName", new Boolean(true));
        mandatoryMap.put("txtDraweeDetailsStreet", new Boolean(true));
        mandatoryMap.put("txtDraweeDetailsPincode", new Boolean(true));
        mandatoryMap.put("txtDraweeDetailsArea", new Boolean(true));
        mandatoryMap.put("cboDraweeDetailsCity", new Boolean(true));
        mandatoryMap.put("cboDraweeDetailsState", new Boolean(true));
        mandatoryMap.put("cboDraweeDetailsCountry", new Boolean(true));
        mandatoryMap.put("txtDraweeDetailsOthers", new Boolean(true));
        mandatoryMap.put("txtDispatchDetailsName", new Boolean(true));
        mandatoryMap.put("txtDispatchDetailsStreet", new Boolean(true));
        mandatoryMap.put("txtDispatchDetailsPincode", new Boolean(true));
        mandatoryMap.put("txtDispatchDetailsArea", new Boolean(true));
        mandatoryMap.put("cboDispatchDetailsCity", new Boolean(true));
        mandatoryMap.put("cboDispatchDetailsState", new Boolean(true));
        mandatoryMap.put("cboDispatchDetailsCountry", new Boolean(true));
        mandatoryMap.put("txtDispatchDetailsOthers", new Boolean(true));
        mandatoryMap.put("txtAmountOfBill", new Boolean(true));
    }
    
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    public void update(Observable observed, Object arg) {
        cboProductID.setSelectedItem(observable.getCboProductID());
        cboTypeOfBill.setSelectedItem(observable.getCboTypeOfBill());
        cboBankDetails.setSelectedItem(observable.getCboBankDetails());
        txtCustomerID.setText(observable.getTxtCustomerID());
        txtInstrumentDetails.setText(observable.getTxtInstrumentDetails());
        txtDiscount.setText(observable.getTxtDiscount());
        txtMargin.setText(observable.getTxtMargin());
        txtBorrowerNum.setText(observable.getTxtBorrowerNum());
        txtPSBROtherBanks.setText(observable.getTxtPSBROtherBanks());
        txtCommission.setText(observable.getTxtCommission());
        txtPostage.setText(observable.getTxtPostage());
        txtDraweeDetailsName.setText(observable.getTxtDraweeDetailsName());
        txtDraweeDetailsStreet.setText(observable.getTxtDraweeDetailsStreet());
        txtDraweeDetailsPincode.setText(observable.getTxtDraweeDetailsPincode());
        txtDraweeDetailsArea.setText(observable.getTxtDraweeDetailsArea());
        cboDraweeDetailsCity.setSelectedItem(observable.getCboDraweeDetailsCity());
        cboDraweeDetailsState.setSelectedItem(observable.getCboDraweeDetailsState());
        cboDraweeDetailsCountry.setSelectedItem(observable.getCboDraweeDetailsCountry());
        txtDraweeDetailsOthers.setText(observable.getTxtDraweeDetailsOthers());
        txtDispatchDetailsName.setText(observable.getTxtDispatchDetailsName());
        txtDispatchDetailsStreet.setText(observable.getTxtDispatchDetailsStreet());
        txtDispatchDetailsPincode.setText(observable.getTxtDispatchDetailsPincode());
        txtDispatchDetailsArea.setText(observable.getTxtDispatchDetailsArea());
        cboDispatchDetailsCity.setSelectedItem(observable.getCboDispatchDetailsCity());
        cboDispatchDetailsState.setSelectedItem(observable.getCboDispatchDetailsState());
        cboDispatchDetailsCountry.setSelectedItem(observable.getCboDispatchDetailsCountry());
        txtDispatchDetailsOthers.setText(observable.getTxtDispatchDetailsOthers());
        txtAmountOfBill.setText(observable.getTxtAmountOfBill());
        lblStatus.setText(observable.getLblStatus());
    }
    
    public void updateOBFields() {
        /* display on UI for combo box */
        observable.setModule(getModule());
        observable.setScreen(getScreen());
        observable.setCboProductID((String)cboProductID.getSelectedItem());
        observable.setCboTypeOfBill((String)cboTypeOfBill.getSelectedItem());
        observable.setCboBankDetails((String)cboBankDetails.getSelectedItem());
        observable.setCboDraweeDetailsCity((String)cboDraweeDetailsCity.getSelectedItem());
        observable.setCboDraweeDetailsState((String)cboDraweeDetailsState.getSelectedItem());
        observable.setCboDraweeDetailsCountry((String)cboDraweeDetailsCountry.getSelectedItem());
        observable.setCboDispatchDetailsCity((String)cboDispatchDetailsCity.getSelectedItem());
        observable.setCboDispatchDetailsState((String)cboDispatchDetailsState.getSelectedItem());
        observable.setCboDispatchDetailsCountry((String)cboDispatchDetailsCountry.getSelectedItem());
        
        /* display on UI  for text box */
        observable.setTxtCustomerID(txtCustomerID.getText());
        observable.setTxtInstrumentDetails(txtInstrumentDetails.getText());
        observable.setTxtDiscount(txtDiscount.getText());
        observable.setTxtMargin(txtMargin.getText());
        observable.setTxtBorrowerNum(txtBorrowerNum.getText());
        observable.setTxtPSBROtherBanks(txtPSBROtherBanks.getText());
        observable.setTxtCommission(txtCommission.getText());
        observable.setTxtPostage(txtPostage.getText());
        observable.setTxtDraweeDetailsName(txtDraweeDetailsName.getText());
        observable.setTxtDraweeDetailsStreet(txtDraweeDetailsStreet.getText());
        observable.setTxtDraweeDetailsPincode(txtDraweeDetailsPincode.getText());
        observable.setTxtDraweeDetailsArea(txtDraweeDetailsArea.getText());
        observable.setTxtDraweeDetailsOthers(txtDraweeDetailsOthers.getText());
        observable.setTxtDispatchDetailsName(txtDispatchDetailsName.getText());
        observable.setTxtDispatchDetailsStreet(txtDispatchDetailsStreet.getText());
        observable.setTxtDispatchDetailsPincode(txtDispatchDetailsPincode.getText());
        observable.setTxtDispatchDetailsArea(txtDispatchDetailsArea.getText());
        observable.setTxtDispatchDetailsOthers(txtDispatchDetailsOthers.getText());
        observable.setTxtAmountOfBill(txtAmountOfBill.getText());
    }
    
    private void setMaximumLength(){
        txtCustomerID.setMaxLength(16);
        txtBorrowerNum.setMaxLength(32);
        txtInstrumentDetails.setMaxLength(32);
        txtPSBROtherBanks.setMaxLength(32);
        txtCommission.setMaxLength(16);
        txtCommission.setValidation(new NumericValidation());
        txtDiscount.setMaxLength(16);
        txtDiscount.setValidation(new NumericValidation());
        txtPostage.setMaxLength(16);
        txtPostage.setValidation(new NumericValidation());
        txtMargin.setMaxLength(16);
        txtMargin.setValidation(new NumericValidation());
        txtDraweeDetailsOthers.setMaxLength(64);
        txtDraweeDetailsName.setMaxLength(64);
        txtDraweeDetailsStreet.setMaxLength(128);
        txtDraweeDetailsArea.setMaxLength(128);
        txtDraweeDetailsPincode.setMaxLength(128);
        txtDraweeDetailsPincode.setValidation(new PincodeValidation_IN());
        txtDispatchDetailsOthers.setMaxLength(64);
        txtDispatchDetailsName.setMaxLength(64);
        txtDispatchDetailsStreet.setMaxLength(128);
        txtDispatchDetailsArea.setMaxLength(128);
        txtDispatchDetailsPincode.setMaxLength(128);
        txtDispatchDetailsPincode.setValidation(new PincodeValidation_IN());
        txtAmountOfBill.setMaxLength(128);
        txtAmountOfBill.setValidation(new NumericValidation());
    }
    
    public void setHelpMessage() {
        LodgementBillsMRB objMandatoryRB = new LodgementBillsMRB();
        cboProductID.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProductID"));
        cboTypeOfBill.setHelpMessage(lblMsg, objMandatoryRB.getString("cboTypeOfBill"));
        cboBankDetails.setHelpMessage(lblMsg, objMandatoryRB.getString("cboBankDetails"));
        txtCustomerID.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCustomerID"));
        txtInstrumentDetails.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInstrumentDetails"));
        txtDiscount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDiscount"));
        txtMargin.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMargin"));
        txtBorrowerNum.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBorrowerNum"));
        txtPSBROtherBanks.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPSBROtherBanks"));
        txtCommission.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCommission"));
        txtPostage.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPostage"));
        txtDraweeDetailsName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDraweeDetailsName"));
        txtDraweeDetailsStreet.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDraweeDetailsStreet"));
        txtDraweeDetailsPincode.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDraweeDetailsPincode"));
        txtDraweeDetailsArea.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDraweeDetailsArea"));
        cboDraweeDetailsCity.setHelpMessage(lblMsg, objMandatoryRB.getString("cboDraweeDetailsCity"));
        cboDraweeDetailsState.setHelpMessage(lblMsg, objMandatoryRB.getString("cboDraweeDetailsState"));
        cboDraweeDetailsCountry.setHelpMessage(lblMsg, objMandatoryRB.getString("cboDraweeDetailsCountry"));
        txtDraweeDetailsOthers.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDraweeDetailsOthers"));
        txtDispatchDetailsName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDispatchDetailsName"));
        txtDispatchDetailsStreet.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDispatchDetailsStreet"));
        txtDispatchDetailsPincode.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDispatchDetailsPincode"));
        txtDispatchDetailsArea.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDispatchDetailsArea"));
        cboDispatchDetailsCity.setHelpMessage(lblMsg, objMandatoryRB.getString("cboDispatchDetailsCity"));
        cboDispatchDetailsState.setHelpMessage(lblMsg, objMandatoryRB.getString("cboDispatchDetailsState"));
        cboDispatchDetailsCountry.setHelpMessage(lblMsg, objMandatoryRB.getString("cboDispatchDetailsCountry"));
        txtDispatchDetailsOthers.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDispatchDetailsOthers"));
        txtAmountOfBill.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAmountOfBill"));
    }
    
    private void initComponentData() {
        cboProductID.setModel(observable.getCbmProductID());
        cboBankDetails.setModel(observable.getCbmBankDetails());
        cboTypeOfBill.setModel(observable.getCbmTypeOfBill());
        cboDraweeDetailsCity.setModel(observable.getCbmDraweeDetailsCity());
        cboDraweeDetailsState.setModel(observable.getCbmDraweeDetailsState());
        cboDraweeDetailsCountry.setModel(observable.getCbmDraweeDetailsCountry());
        cboDispatchDetailsCity.setModel(observable.getCbmDispatchDetailsCity());
        cboDispatchDetailsState.setModel(observable.getCbmDispatchDetailsState());
        cboDispatchDetailsCountry.setModel(observable.getCbmDispatchDetailsCountry());
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        cTextField3 = new com.see.truetransact.uicomponent.CTextField();
        cTextField4 = new com.see.truetransact.uicomponent.CTextField();
        tbrMain = new javax.swing.JToolBar();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        panMain = new com.see.truetransact.uicomponent.CPanel();
        panProductID = new com.see.truetransact.uicomponent.CPanel();
        lblProductID = new com.see.truetransact.uicomponent.CLabel();
        lblTypeOfBill = new com.see.truetransact.uicomponent.CLabel();
        lblAccHead = new com.see.truetransact.uicomponent.CLabel();
        lblBankDetails = new com.see.truetransact.uicomponent.CLabel();
        cboProductID = new com.see.truetransact.uicomponent.CComboBox();
        cboTypeOfBill = new com.see.truetransact.uicomponent.CComboBox();
        cboBankDetails = new com.see.truetransact.uicomponent.CComboBox();
        txtCustomerID = new com.see.truetransact.uicomponent.CTextField();
        lblCustomerID = new com.see.truetransact.uicomponent.CLabel();
        lblCustomerAccNo = new com.see.truetransact.uicomponent.CLabel();
        lblInstrumentDetails = new com.see.truetransact.uicomponent.CLabel();
        lblAmtOfBill = new com.see.truetransact.uicomponent.CLabel();
        lblDiscount = new com.see.truetransact.uicomponent.CLabel();
        lblMargin = new com.see.truetransact.uicomponent.CLabel();
        lblBorrowerNum = new com.see.truetransact.uicomponent.CLabel();
        lblCustomerNm = new com.see.truetransact.uicomponent.CLabel();
        lblPSRBOtherBanks = new com.see.truetransact.uicomponent.CLabel();
        lblCommission = new com.see.truetransact.uicomponent.CLabel();
        lblPostage = new com.see.truetransact.uicomponent.CLabel();
        btnCustomerID = new com.see.truetransact.uicomponent.CButton();
        txtInstrumentDetails = new com.see.truetransact.uicomponent.CTextField();
        txtDiscount = new com.see.truetransact.uicomponent.CTextField();
        txtMargin = new com.see.truetransact.uicomponent.CTextField();
        txtBorrowerNum = new com.see.truetransact.uicomponent.CTextField();
        txtPSBROtherBanks = new com.see.truetransact.uicomponent.CTextField();
        txtCommission = new com.see.truetransact.uicomponent.CTextField();
        txtPostage = new com.see.truetransact.uicomponent.CTextField();
        lblCustomerAccountNumber = new com.see.truetransact.uicomponent.CLabel();
        lblAmountOfBill = new com.see.truetransact.uicomponent.CLabel();
        lblAccountHead = new com.see.truetransact.uicomponent.CLabel();
        lblCustomerName = new com.see.truetransact.uicomponent.CLabel();
        txtAmountOfBill = new com.see.truetransact.uicomponent.CTextField();
        panDraweeDeatils = new com.see.truetransact.uicomponent.CPanel();
        txtDraweeDetailsName = new com.see.truetransact.uicomponent.CTextField();
        txtDraweeDetailsStreet = new com.see.truetransact.uicomponent.CTextField();
        txtDraweeDetailsPincode = new com.see.truetransact.uicomponent.CTextField();
        txtDraweeDetailsArea = new com.see.truetransact.uicomponent.CTextField();
        cboDraweeDetailsCity = new com.see.truetransact.uicomponent.CComboBox();
        cboDraweeDetailsState = new com.see.truetransact.uicomponent.CComboBox();
        cboDraweeDetailsCountry = new com.see.truetransact.uicomponent.CComboBox();
        lblDraweeDetailsName = new com.see.truetransact.uicomponent.CLabel();
        lblDraweeDetailsStreet = new com.see.truetransact.uicomponent.CLabel();
        lblDraweeDetailsArea = new com.see.truetransact.uicomponent.CLabel();
        lblDraweeDetailsCity = new com.see.truetransact.uicomponent.CLabel();
        lblDraweeDetailsState = new com.see.truetransact.uicomponent.CLabel();
        lblDraweeDetailsCountry = new com.see.truetransact.uicomponent.CLabel();
        lblDraweeDetailsPincode = new com.see.truetransact.uicomponent.CLabel();
        lblDraweeDetailsOthers = new com.see.truetransact.uicomponent.CLabel();
        txtDraweeDetailsOthers = new com.see.truetransact.uicomponent.CTextField();
        panDispatchDetails = new com.see.truetransact.uicomponent.CPanel();
        txtDispatchDetailsName = new com.see.truetransact.uicomponent.CTextField();
        txtDispatchDetailsStreet = new com.see.truetransact.uicomponent.CTextField();
        txtDispatchDetailsPincode = new com.see.truetransact.uicomponent.CTextField();
        txtDispatchDetailsArea = new com.see.truetransact.uicomponent.CTextField();
        cboDispatchDetailsCity = new com.see.truetransact.uicomponent.CComboBox();
        cboDispatchDetailsState = new com.see.truetransact.uicomponent.CComboBox();
        cboDispatchDetailsCountry = new com.see.truetransact.uicomponent.CComboBox();
        lblDispatchDetailsName = new com.see.truetransact.uicomponent.CLabel();
        lblDispatchDetailsStreet = new com.see.truetransact.uicomponent.CLabel();
        lblDispatchDetailsArea = new com.see.truetransact.uicomponent.CLabel();
        lblDispatchDetailsCity = new com.see.truetransact.uicomponent.CLabel();
        lblDispatchDetailsState = new com.see.truetransact.uicomponent.CLabel();
        lblDispatchDetailsCountry = new com.see.truetransact.uicomponent.CLabel();
        lblDispatchDetailsPincode = new com.see.truetransact.uicomponent.CLabel();
        lblDispatchDetailsOthers = new com.see.truetransact.uicomponent.CLabel();
        txtDispatchDetailsOthers = new com.see.truetransact.uicomponent.CTextField();
        mbrMain = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptDelete = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitClose = new javax.swing.JMenuItem();

        cTextField3.setText("cTextField3");
        cTextField4.setText("cTextField4");

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMaximumSize(new java.awt.Dimension(580, 540));
        setMinimumSize(new java.awt.Dimension(575, 545));
        setPreferredSize(new java.awt.Dimension(575, 545));
        tbrMain.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 11));
        tbrMain.setMinimumSize(new java.awt.Dimension(28, 28));
        tbrMain.setPreferredSize(new java.awt.Dimension(28, 28));
        tbrMain.setEnabled(false);
        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif")));
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });

        tbrMain.add(btnNew);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif")));
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        tbrMain.add(btnEdit);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif")));
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        tbrMain.add(btnDelete);

        lblSpace1.setText("     ");
        tbrMain.add(lblSpace1);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif")));
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        tbrMain.add(btnSave);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif")));
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        tbrMain.add(btnCancel);

        lblSpace2.setText("     ");
        tbrMain.add(lblSpace2);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif")));
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });

        tbrMain.add(btnPrint);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif")));
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        tbrMain.add(btnClose);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(tbrMain, gridBagConstraints);

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace3.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace3, gridBagConstraints);

        lblStatus.setBorder(new javax.swing.border.EtchedBorder());
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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(panStatus, gridBagConstraints);

        panMain.setLayout(new java.awt.GridBagLayout());

        panProductID.setLayout(new java.awt.GridBagLayout());

        panProductID.setBorder(new javax.swing.border.TitledBorder(""));
        lblProductID.setText("Product ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 35, 2, 4);
        panProductID.add(lblProductID, gridBagConstraints);

        lblTypeOfBill.setText("Type of Bills");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 35, 2, 4);
        panProductID.add(lblTypeOfBill, gridBagConstraints);

        lblAccHead.setText("Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 28, 2, 4);
        panProductID.add(lblAccHead, gridBagConstraints);

        lblBankDetails.setText("Bank Details");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 28, 2, 4);
        panProductID.add(lblBankDetails, gridBagConstraints);

        cboProductID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProductIDActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panProductID.add(cboProductID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panProductID.add(cboTypeOfBill, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panProductID.add(cboBankDetails, gridBagConstraints);

        txtCustomerID.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panProductID.add(txtCustomerID, gridBagConstraints);

        lblCustomerID.setText("Customer ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 35, 2, 4);
        panProductID.add(lblCustomerID, gridBagConstraints);

        lblCustomerAccNo.setText("Customer Acc. No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 35, 2, 4);
        panProductID.add(lblCustomerAccNo, gridBagConstraints);

        lblInstrumentDetails.setText("Instrument / Bill Details");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 35, 2, 4);
        panProductID.add(lblInstrumentDetails, gridBagConstraints);

        lblAmtOfBill.setText("Amount of Bill");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 35, 2, 4);
        panProductID.add(lblAmtOfBill, gridBagConstraints);

        lblDiscount.setText("Discount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 35, 2, 4);
        panProductID.add(lblDiscount, gridBagConstraints);

        lblMargin.setText("Margin");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 35, 2, 4);
        panProductID.add(lblMargin, gridBagConstraints);

        lblBorrowerNum.setText("Borrower No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 28, 2, 4);
        panProductID.add(lblBorrowerNum, gridBagConstraints);

        lblCustomerNm.setText("Customer Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 28, 2, 4);
        panProductID.add(lblCustomerNm, gridBagConstraints);

        lblPSRBOtherBanks.setText("P / SBR / Other Banks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 28, 2, 4);
        panProductID.add(lblPSRBOtherBanks, gridBagConstraints);

        lblCommission.setText("Commission");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 28, 2, 4);
        panProductID.add(lblCommission, gridBagConstraints);

        lblPostage.setText("Postage");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 28, 2, 4);
        panProductID.add(lblPostage, gridBagConstraints);

        btnCustomerID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnCustomerID.setMinimumSize(new java.awt.Dimension(28, 21));
        btnCustomerID.setPreferredSize(new java.awt.Dimension(28, 21));
        btnCustomerID.setEnabled(false);
        btnCustomerID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustomerIDActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 2);
        panProductID.add(btnCustomerID, gridBagConstraints);

        txtInstrumentDetails.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panProductID.add(txtInstrumentDetails, gridBagConstraints);

        txtDiscount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panProductID.add(txtDiscount, gridBagConstraints);

        txtMargin.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panProductID.add(txtMargin, gridBagConstraints);

        txtBorrowerNum.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panProductID.add(txtBorrowerNum, gridBagConstraints);

        txtPSBROtherBanks.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panProductID.add(txtPSBROtherBanks, gridBagConstraints);

        txtCommission.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panProductID.add(txtCommission, gridBagConstraints);

        txtPostage.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panProductID.add(txtPostage, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panProductID.add(lblCustomerAccountNumber, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panProductID.add(lblAmountOfBill, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panProductID.add(lblAccountHead, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panProductID.add(lblCustomerName, gridBagConstraints);

        txtAmountOfBill.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panProductID.add(txtAmountOfBill, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMain.add(panProductID, gridBagConstraints);

        panDraweeDeatils.setLayout(new java.awt.GridBagLayout());

        panDraweeDeatils.setBorder(new javax.swing.border.TitledBorder("Drawee Details"));
        txtDraweeDetailsName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDraweeDeatils.add(txtDraweeDetailsName, gridBagConstraints);

        txtDraweeDetailsStreet.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDraweeDeatils.add(txtDraweeDetailsStreet, gridBagConstraints);

        txtDraweeDetailsPincode.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDraweeDeatils.add(txtDraweeDetailsPincode, gridBagConstraints);

        txtDraweeDetailsArea.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDraweeDeatils.add(txtDraweeDetailsArea, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDraweeDeatils.add(cboDraweeDetailsCity, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDraweeDeatils.add(cboDraweeDetailsState, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDraweeDeatils.add(cboDraweeDetailsCountry, gridBagConstraints);

        lblDraweeDetailsName.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDraweeDeatils.add(lblDraweeDetailsName, gridBagConstraints);

        lblDraweeDetailsStreet.setText("Street");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDraweeDeatils.add(lblDraweeDetailsStreet, gridBagConstraints);

        lblDraweeDetailsArea.setText("Area");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDraweeDeatils.add(lblDraweeDetailsArea, gridBagConstraints);

        lblDraweeDetailsCity.setText("City");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDraweeDeatils.add(lblDraweeDetailsCity, gridBagConstraints);

        lblDraweeDetailsState.setText("State");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDraweeDeatils.add(lblDraweeDetailsState, gridBagConstraints);

        lblDraweeDetailsCountry.setText("Country");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDraweeDeatils.add(lblDraweeDetailsCountry, gridBagConstraints);

        lblDraweeDetailsPincode.setText("Pincode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDraweeDeatils.add(lblDraweeDetailsPincode, gridBagConstraints);

        lblDraweeDetailsOthers.setText("Drawee Bank / Branch / Others");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panDraweeDeatils.add(lblDraweeDetailsOthers, gridBagConstraints);

        txtDraweeDetailsOthers.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDraweeDeatils.add(txtDraweeDetailsOthers, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMain.add(panDraweeDeatils, gridBagConstraints);

        panDispatchDetails.setLayout(new java.awt.GridBagLayout());

        panDispatchDetails.setBorder(new javax.swing.border.TitledBorder("Forwarded / Dispatch Details"));
        txtDispatchDetailsName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDispatchDetails.add(txtDispatchDetailsName, gridBagConstraints);

        txtDispatchDetailsStreet.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDispatchDetails.add(txtDispatchDetailsStreet, gridBagConstraints);

        txtDispatchDetailsPincode.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDispatchDetails.add(txtDispatchDetailsPincode, gridBagConstraints);

        txtDispatchDetailsArea.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDispatchDetails.add(txtDispatchDetailsArea, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDispatchDetails.add(cboDispatchDetailsCity, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDispatchDetails.add(cboDispatchDetailsState, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDispatchDetails.add(cboDispatchDetailsCountry, gridBagConstraints);

        lblDispatchDetailsName.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDispatchDetails.add(lblDispatchDetailsName, gridBagConstraints);

        lblDispatchDetailsStreet.setText("Street");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDispatchDetails.add(lblDispatchDetailsStreet, gridBagConstraints);

        lblDispatchDetailsArea.setText("Area");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDispatchDetails.add(lblDispatchDetailsArea, gridBagConstraints);

        lblDispatchDetailsCity.setText("City");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDispatchDetails.add(lblDispatchDetailsCity, gridBagConstraints);

        lblDispatchDetailsState.setText("State");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDispatchDetails.add(lblDispatchDetailsState, gridBagConstraints);

        lblDispatchDetailsCountry.setText("Country");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDispatchDetails.add(lblDispatchDetailsCountry, gridBagConstraints);

        lblDispatchDetailsPincode.setText("Pincode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDispatchDetails.add(lblDispatchDetailsPincode, gridBagConstraints);

        lblDispatchDetailsOthers.setText("Drawee Bank / Branch / Others");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panDispatchDetails.add(lblDispatchDetailsOthers, gridBagConstraints);

        txtDispatchDetailsOthers.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panDispatchDetails.add(txtDispatchDetailsOthers, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMain.add(panDispatchDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 2.0;
        gridBagConstraints.weighty = 4.0;
        getContentPane().add(panMain, gridBagConstraints);

        mnuProcess.setText("Process");
        mnuProcess.setToolTipText("Menu");
        mitNew.setText("New");
        mitNew.setToolTipText("");
        mitNew.setEnabled(false);
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });

        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mitEdit.setEnabled(false);
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });

        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mitDelete.setEnabled(false);
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });

        mnuProcess.add(mitDelete);

        sptDelete.setEnabled(false);
        mnuProcess.add(sptDelete);

        mitSave.setText("Save");
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });

        mnuProcess.add(mitSave);

        mitCancel.setText("Cancle");
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });

        mnuProcess.add(mitCancel);

        sptCancel.setEnabled(false);
        mnuProcess.add(sptCancel);

        mitClose.setText("Close");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });

        mnuProcess.add(mitClose);

        mbrMain.add(mnuProcess);

        setJMenuBar(mbrMain);

        pack();
    }//GEN-END:initComponents
    
    private void cboProductIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProductIDActionPerformed
        // Add your handling code here:
        try{
            final HashMap prdouctIDdMap = new HashMap();
            HashMap hash = new HashMap();
            if(cboProductID.getSelectedItem().toString().length() > 0){
                prdouctIDdMap.put("PROD_ID", observable.getCbmProductID().getKeyForSelected().toString());
                final List resultList = ClientUtil.executeQuery("Bill_Lodgements.getSelectedProductID", prdouctIDdMap);
                final HashMap resultMap = (HashMap)resultList.get(0);
                lblAccountHead.setText(resultMap.get("GL_AC_HD").toString());
            }else{
                lblAccountHead.setText("");
            }
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }//GEN-LAST:event_cboProductIDActionPerformed
    
    private void btnCustomerIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustomerIDActionPerformed
        // Add your handling code here:
        popUpItems(CUSTOMERID);
    }//GEN-LAST:event_btnCustomerIDActionPerformed
    
    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_btnPrintActionPerformed
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // Add your handling code here:
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        observable.resetStatus();
        setButtonCustomerID();
        setButtonEnableDisable();
        ClientUtil.enableDisable(this, false);
        resetFields();
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // Add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        final String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panMain);
        /* mandatoryMessage length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
        if (mandatoryMessage.length() > 0){
            displayAlert(mandatoryMessage);
        }else{
            savePerformed();
        }
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // Add your handling code here:
        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        lblStatus.setText(observable.getLblStatus());
        popUpItems(DELETE);
        setButtonEnableDisable();
        ClientUtil.enableDisable(this,false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // Add your handling code here:
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        popUpItems(EDIT);
        cboProductID.setSelectedItem(observable.getCboProductID());
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // Add your handling code here:
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.setStatus();
        ClientUtil.enableDisable(this, true);
        setButtonCustomerID();
        setButtonEnableDisable();
    }//GEN-LAST:event_btnNewActionPerformed
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm
    
    /**
     * @param args the command line arguments
     */
 /*   public static void main(String args[]) {
        new LodgementBillsUI().show();
    }  */
    
    private void displayAlert(String message){
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    private void savePerformed(){
        updateOBFields();
        observable.doAction();
        setButtonCustomerID();
        ClientUtil.enableDisable(this, false);
        setButtonEnableDisable();
        resetFields();
        observable.setResultStatus();
        lblStatus.setText(observable.getLblStatus());
    }
    
    
    /** This method helps in popoualting the data from the data base
     * @param Action the argument is passed according to the command issued
     */
    private void popUpItems(int Action) {
        if (Action == EDIT || Action == DELETE){
            lblStatus.setText(ClientConstants.ACTION_STATUS[0]);
        }
        final HashMap viewMap = new HashMap();
        ACTION=Action;
        if ( Action == EDIT || Action == DELETE){
            viewMap.put(CommonConstants.MAP_NAME, "ViewAllLodgementBillsTO");
        }else{
            viewMap.put(CommonConstants.MAP_NAME, "Bill_Lodgements.ViewAllCustomerID");
        }
        new ViewAll(this, viewMap).show();
    }
    
    /** This method helps in filling the data frm the data base to respective txt fields
     * @param param The selected data from the viewAll() is passed as a param
     */
    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
        if (ACTION == EDIT){
            setButtonCustomerID();
            setButtonEnableDisable();
        }
        ClientUtil.enableDisable(this, true);
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE){
            actionEditDelete(hash);
        }else if(ACTION == CUSTOMERID){
            customerID(hash);
        }
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
    }
    
    private void customerID(HashMap hash){
        setFullName(hash);
        updateOBFields();
        observable.setTxtCustomerID((String)hash.get("CUSTOMER ID"));
        txtCustomerID.setText(observable.getTxtCustomerID());
        lblCustomerName.setText(customerName.toString());
        final HashMap customerID = new HashMap();
        customerID.put("CUSTOMER ID", txtCustomerID.getText());
        final List resultList = ClientUtil.executeQuery("Bill_Lodgements.getSelectedCustomerID", customerID);
        final HashMap resultMap = (HashMap)resultList.get(0);
        lblCustomerAccountNumber.setText(resultMap.get("ACCOUNT NUMBER").toString());
        customerName = null;
    }
    
    private void actionEditDelete(HashMap hash){
        if(ACTION == CUSTOMERID){
            txtCustomerID.setText((String)hash.get("CUSTOMER ID"));
            updateOBFields();
            displayCustomerDetails();
        }else{
            observable.setStatus();
            hash.put(CommonConstants.MAP_WHERE, hash.get("LODGEMENT ID"));
            observable.setLodgementId((String) hash.get("LODGEMENT ID"));
            observable.populateData(hash);
            displayCustomerDetails();
        }
    }
    
    private void displayCustomerDetails(){
        customerName = new StringBuffer();
        final HashMap customerID = new HashMap();
        customerID.put("CUSTOMER ID", txtCustomerID.getText());
        final List resultList = ClientUtil.executeQuery("Bill_Lodgements.getCustomerID", customerID);
        final HashMap resultMap = (HashMap)resultList.get(0);
        setFullName(resultMap);
        lblCustomerAccountNumber.setText(resultMap.get("ACCOUNT NUMBER").toString());
        lblCustomerName.setText(customerName.toString());
        customerName = null;
    }
    
    private void setFullName(HashMap hash){
        customerName = new StringBuffer();
        customerName.append(hash.get("FIRST NAME"));
        customerName.append(stringSpace);
        customerName.append(hash.get("LAST NAME"));
        customerName.append(stringSpace);
        customerName.append(hash.get("MIDDLE NAME"));
    }
    
    private void resetFields(){
        lblCustomerAccountNumber.setText("");
        lblAccountHead.setText("");
        lblCustomerName.setText("");
        resetCombo();
        observable.resetForm();
    }
    
    private void resetCombo(){
        cboProductID.setSelectedItem("");
        cboBankDetails.setSelectedItem("");
        cboTypeOfBill.setSelectedItem("");
        cboDraweeDetailsCity.setSelectedItem("");
        cboDraweeDetailsState.setSelectedItem("");
        cboDraweeDetailsCountry.setSelectedItem("");;
        cboDispatchDetailsCity.setSelectedItem("");
        cboDispatchDetailsState.setSelectedItem("");
        cboDispatchDetailsCountry.setSelectedItem("");
    }
    
    /*This method performs enable and the disable of the necessary buttons*/
    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(btnNew.isEnabled());
        btnDelete.setEnabled(btnNew.isEnabled());
        mitNew.setEnabled(!mitNew.isEnabled());
        mitEdit.setEnabled(mitNew.isEnabled());
        mitDelete.setEnabled(mitNew.isEnabled());
        btnSave.setEnabled(!btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(!mitNew.isEnabled());
        mitCancel.setEnabled(!mitNew.isEnabled());
        lblStatus.setText(observable.getLblStatus());
    }
    
    private void setButtonCustomerID(){
        btnCustomerID.setEnabled(btnNew.isEnabled());
        txtCustomerID.setEditable(!btnNew.isEnabled());
        txtCustomerID.setEnabled(!btnNew.isEnabled());
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnCustomerID;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CTextField cTextField3;
    private com.see.truetransact.uicomponent.CTextField cTextField4;
    private com.see.truetransact.uicomponent.CComboBox cboBankDetails;
    private com.see.truetransact.uicomponent.CComboBox cboDispatchDetailsCity;
    private com.see.truetransact.uicomponent.CComboBox cboDispatchDetailsCountry;
    private com.see.truetransact.uicomponent.CComboBox cboDispatchDetailsState;
    private com.see.truetransact.uicomponent.CComboBox cboDraweeDetailsCity;
    private com.see.truetransact.uicomponent.CComboBox cboDraweeDetailsCountry;
    private com.see.truetransact.uicomponent.CComboBox cboDraweeDetailsState;
    private com.see.truetransact.uicomponent.CComboBox cboProductID;
    private com.see.truetransact.uicomponent.CComboBox cboTypeOfBill;
    private com.see.truetransact.uicomponent.CLabel lblAccHead;
    private com.see.truetransact.uicomponent.CLabel lblAccountHead;
    private com.see.truetransact.uicomponent.CLabel lblAmountOfBill;
    private com.see.truetransact.uicomponent.CLabel lblAmtOfBill;
    private com.see.truetransact.uicomponent.CLabel lblBankDetails;
    private com.see.truetransact.uicomponent.CLabel lblBorrowerNum;
    private com.see.truetransact.uicomponent.CLabel lblCommission;
    private com.see.truetransact.uicomponent.CLabel lblCustomerAccNo;
    private com.see.truetransact.uicomponent.CLabel lblCustomerAccountNumber;
    private com.see.truetransact.uicomponent.CLabel lblCustomerID;
    private com.see.truetransact.uicomponent.CLabel lblCustomerName;
    private com.see.truetransact.uicomponent.CLabel lblCustomerNm;
    private com.see.truetransact.uicomponent.CLabel lblDiscount;
    private com.see.truetransact.uicomponent.CLabel lblDispatchDetailsArea;
    private com.see.truetransact.uicomponent.CLabel lblDispatchDetailsCity;
    private com.see.truetransact.uicomponent.CLabel lblDispatchDetailsCountry;
    private com.see.truetransact.uicomponent.CLabel lblDispatchDetailsName;
    private com.see.truetransact.uicomponent.CLabel lblDispatchDetailsOthers;
    private com.see.truetransact.uicomponent.CLabel lblDispatchDetailsPincode;
    private com.see.truetransact.uicomponent.CLabel lblDispatchDetailsState;
    private com.see.truetransact.uicomponent.CLabel lblDispatchDetailsStreet;
    private com.see.truetransact.uicomponent.CLabel lblDraweeDetailsArea;
    private com.see.truetransact.uicomponent.CLabel lblDraweeDetailsCity;
    private com.see.truetransact.uicomponent.CLabel lblDraweeDetailsCountry;
    private com.see.truetransact.uicomponent.CLabel lblDraweeDetailsName;
    private com.see.truetransact.uicomponent.CLabel lblDraweeDetailsOthers;
    private com.see.truetransact.uicomponent.CLabel lblDraweeDetailsPincode;
    private com.see.truetransact.uicomponent.CLabel lblDraweeDetailsState;
    private com.see.truetransact.uicomponent.CLabel lblDraweeDetailsStreet;
    private com.see.truetransact.uicomponent.CLabel lblInstrumentDetails;
    private com.see.truetransact.uicomponent.CLabel lblMargin;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblPSRBOtherBanks;
    private com.see.truetransact.uicomponent.CLabel lblPostage;
    private com.see.truetransact.uicomponent.CLabel lblProductID;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTypeOfBill;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panDispatchDetails;
    private com.see.truetransact.uicomponent.CPanel panDraweeDeatils;
    private com.see.truetransact.uicomponent.CPanel panMain;
    private com.see.truetransact.uicomponent.CPanel panProductID;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private javax.swing.JToolBar tbrMain;
    private com.see.truetransact.uicomponent.CTextField txtAmountOfBill;
    private com.see.truetransact.uicomponent.CTextField txtBorrowerNum;
    private com.see.truetransact.uicomponent.CTextField txtCommission;
    private com.see.truetransact.uicomponent.CTextField txtCustomerID;
    private com.see.truetransact.uicomponent.CTextField txtDiscount;
    private com.see.truetransact.uicomponent.CTextField txtDispatchDetailsArea;
    private com.see.truetransact.uicomponent.CTextField txtDispatchDetailsName;
    private com.see.truetransact.uicomponent.CTextField txtDispatchDetailsOthers;
    private com.see.truetransact.uicomponent.CTextField txtDispatchDetailsPincode;
    private com.see.truetransact.uicomponent.CTextField txtDispatchDetailsStreet;
    private com.see.truetransact.uicomponent.CTextField txtDraweeDetailsArea;
    private com.see.truetransact.uicomponent.CTextField txtDraweeDetailsName;
    private com.see.truetransact.uicomponent.CTextField txtDraweeDetailsOthers;
    private com.see.truetransact.uicomponent.CTextField txtDraweeDetailsPincode;
    private com.see.truetransact.uicomponent.CTextField txtDraweeDetailsStreet;
    private com.see.truetransact.uicomponent.CTextField txtInstrumentDetails;
    private com.see.truetransact.uicomponent.CTextField txtMargin;
    private com.see.truetransact.uicomponent.CTextField txtPSBROtherBanks;
    private com.see.truetransact.uicomponent.CTextField txtPostage;
    // End of variables declaration//GEN-END:variables
}