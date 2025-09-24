/* 
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * GDSPrizedMoneyDetailsEntryUI.java
 *
 * Created on November 26, 2003, 11:27 AM
 */
package com.see.truetransact.ui.gdsapplication.gdsprizedmoneydetailsentry;

import com.see.truetransact.ui.mdsapplication.mdsprizedmoneydetailsentry.*;
import java.util.HashMap;
import java.util.List;
import java.util.Date;
import java.util.Observer;
import java.util.Observable;import java.util.HashMap;
import java.util.ArrayList;
import java.util.Observable;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.ui.common.transaction.TransactionUI;
import com.see.truetransact.ui.common.viewall.AuthorizeListUI;
import com.see.truetransact.ui.common.viewall.NewAuthorizeListUI;

/**
 *
 * @author Nithya
 *
 **/

public class GDSPrizedMoneyDetailsEntryUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer, UIMandatoryField{
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.gdsapplication.gdsprizedmoneydetailsentry.GDSPrizedMoneyDetailsEntryRB", ProxyParameters.LANGUAGE);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private GDSPrizedMoneyDetailsEntryMRB objMandatoryRB = new GDSPrizedMoneyDetailsEntryMRB();
    private TransactionUI transactionUI = new TransactionUI();
    private String viewType = new String();
    GDSPrizedMoneyDetailsEntryOB observable = null;
    final String AUTHORIZE="Authorize";
    private String SCHEME_NAME,CHIT_NO;
    HashMap mandatoryMap = null;
    HashMap addressMap = null;
    boolean isFilled = false;
    private HashMap productMap = new HashMap();
    private String divisionNo = "";
    boolean fromAuthorizeUI = false;
    AuthorizeListUI authorizeListUI = null;
    private int rejectFlag=0;
    private String userDefined = "N";
    private String isSpecialScheme = "";
    private String isWeeklyOrMonthlyFrequency = "";
    private Date currDt = null;
    boolean thalayal = false;
    NewAuthorizeListUI newauthorizeListUI = null;
    boolean fromNewAuthorizeUI = false;
    /** Creates new form BeanForm */
    public GDSPrizedMoneyDetailsEntryUI() {
        initComponents();
        initForm();
    }
    
    private void initForm(){
        currDt = ClientUtil.getCurrentDate();
        setFieldNames();
        internationalize();
        observable = new GDSPrizedMoneyDetailsEntryOB();
        setMaxLengths();
        initComponentData();
        setMandatoryHashMap();
        setHelpMessage();
        setButtonEnableDisable();
        ClientUtil.enableDisable(panPrizedMoneyDetails,false);
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
        btnChittalNo.setName("btnChittalNo");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        btnSchemeName.setName("btnSchemeName");
        btnView.setName("btnView");
        lblApplicationNo.setName("lblApplicationNo");
        lblCommisionAmount.setName("lblCommisionAmount");
        lblDivisionNo.setName("lblDivisionNo");
        lblDrawOrAuctionDt.setName("lblDrawOrAuctionDt");
        lblInstallmentNo.setName("lblInstallmentNo");
        lblInstallmentsAmountPaid.setName("lblInstallmentsAmountPaid");
        lblInstallmentsDue.setName("lblInstallmentsDue");
        lblInstallmentsPaid.setName("lblInstallmentsPaid");
//        lblMembName.setName("lblMembName");
        lblMemberType.setName("lblMemberType");
        lblMemberName.setName("lblMemberName");
        lblChittalNo.setName("lblChittalNo");
        lblMsg.setName("lblMsg");
        lblNetAmountPayable.setName("lblNetAmountPayable");
        lblNextBonusAmount.setName("lblNextBonusAmount");
        lblPrizedAmount.setName("lblPrizedAmount");
        lblSchemeName.setName("lblSchemeName");
        lblSpace.setName("lblSpace");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblSpace5.setName("lblSpace5");
        lblSpace6.setName("lblSpace6");
        lblStatus.setName("lblStatus");
        lblTotalBonusAmount.setName("lblTotalBonusAmount");
        lblTotalDiscount.setName("lblTotalDiscount");
        mbrMain.setName("mbrMain");
        panCustomerId4.setName("panCustomerId4");
        panCustomerId5.setName("panCustomerId5");
        panInsideMDSDetails.setName("panInsideMDSDetails");
        panInsideMemberDetails.setName("panInsideMemberDetails");
        panInsidePrizedMoneyDetails.setName("panInsidePrizedMoneyDetails");
        panPrizedMoneyDetails.setName("panPrizedMoneyDetails");
        panStatus.setName("panStatus");
        tabPrizedMoneyDetails.setName("tabPrizedMoneyDetails");
        tdtDrawOrAuctionDt.setName("tdtDrawOrAuctionDt");
        txtApplicationNo.setName("txtApplicationNo");
        txtCommisionAmount.setName("txtCommisionAmount");
        txtDivisionNo.setName("txtDivisionNo");
        txtInstallmentNo.setName("txtInstallmentNo");
        txtInstallmentsAmountPaid.setName("txtInstallmentsAmountPaid");
        txtInstallmentsDue.setName("txtInstallmentsDue");
        txtInstallmentsPaid.setName("txtInstallmentsPaid");
        txtChittalNo.setName("txtChittalNo");
        txtNetAmountPayable.setName("txtNetAmountPayable");
        txtNextBonusAmount.setName("txtNextBonusAmount");
        txtPrizedAmount.setName("txtPrizedAmount");
        txtSchemeName.setName("txtSchemeName");
        txtTotalBonusAmount.setName("txtTotalBonusAmount");
        panPrizedMoneyType.setName("panPrizedMoneyType");
        lblPrizedMoneyType.setName("lblPrizedMoneyType");
        lblDraw.setName("lblDraw");
        lblAuction.setName("lblAuction");
        chkDraw.setName("chkDraw");
        chkAuction.setName("chkAuction");
        chkUserDefined.setName("chkUserDefined");
        lblNextInstallmentDt.setName("lblNextInstallmentDt");
        tdtNextInstallmentDt.setName("tdtNextInstallmentDt");
        tdtNextInstallmentDt1.setName("tdtNextInstallmentDt1");
        lblInstallmentOverDueAmount.setName("lblInstallmentOverDueAmount");
        txtInstallmentOverDueAmount.setName("txtInstallmentOverDueAmount");
        txtSubNo.setName("txtSubNo");
        panAddressDetails.setName("panAddressDetails");
        lblStreet.setName("lblStreet");
        lblArea.setName("lblArea");
        lblCity.setName("lblCity");
        lblState.setName("lblState");
        lblPin.setName("lblPin");
        lblValStreet.setName("lblValStreet");
        lblValArea.setName("lblValArea");
        lblValCity.setName("lblValCity");
        lblValState.setName("lblValState");
        lblValPin.setName("lblValPin");
    }
    
    private void setMaxLengths(){
        txtPrizedAmount.setValidation(new CurrencyValidation(14,2));
        txtTotalBonusAmount.setValidation(new CurrencyValidation(14,2));
        txtNextBonusAmount.setValidation(new CurrencyValidation(14,2));
        txtCommisionAmount.setValidation(new CurrencyValidation(14,2));
        txtTotalDiscount.setValidation(new CurrencyValidation(14,2));
        txtNetAmountPayable.setValidation(new CurrencyValidation(14,2));
        txtInstallmentsPaid.setValidation(new NumericValidation(14,2));
        txtInstallmentsAmountPaid.setValidation(new CurrencyValidation(14,2));
        txtInstallmentsDue.setValidation(new NumericValidation(14,2));
        txtInstallmentOverDueAmount.setValidation(new CurrencyValidation(14,2));
        txtDivisionNo.setValidation(new NumericValidation());
        txtSubNo.setValidation(new NumericValidation());
    }
    
    /** Auto Generated Method - internationalize()
     * This method used to assign display texts from
     * the Resource Bundle File. */
    private void internationalize() {
        resourceBundle = new GDSPrizedMoneyDetailsEntryRB();
        lblSchemeName.setText(resourceBundle.getString("lblSchemeName"));
        lblInstallmentsPaid.setText(resourceBundle.getString("lblInstallmentsPaid"));
        lblInstallmentsAmountPaid.setText(resourceBundle.getString("lblInstallmentsAmountPaid"));
        lblInstallmentsDue.setText(resourceBundle.getString("lblInstallmentsDue"));
        lblInstallmentOverDueAmount.setText(resourceBundle.getString("lblInstallmentOverDueAmount"));
        lblApplicationNo.setText(resourceBundle.getString("lblApplicationNo"));
        lblChittalNo.setText(resourceBundle.getString("lblChittalNo"));
        lblMemberType.setText(resourceBundle.getString("lblMemberType"));
        lblPrizedAmount.setText(resourceBundle.getString("lblPrizedAmount"));
        lblTotalBonusAmount.setText(resourceBundle.getString("lblTotalBonusAmount"));
        lblNextBonusAmount.setText(resourceBundle.getString("lblNextBonusAmount"));
        lblCommisionAmount.setText(resourceBundle.getString("lblCommisionAmount"));
        lblTotalDiscount.setText(resourceBundle.getString("lblTotalDiscount"));
        lblNetAmountPayable.setText(resourceBundle.getString("lblNetAmountPayable"));
        lblNextInstallmentDt.setText(resourceBundle.getString("lblNextInstallmentDt"));
        lblDrawOrAuctionDt.setText(resourceBundle.getString("lblDrawOrAuctionDt"));
        lblInstallmentNo.setText(resourceBundle.getString("lblInstallmentNo"));
        lblDivisionNo.setText(resourceBundle.getString("lblDivisionNo"));
        lblPrizedMoneyType.setText(resourceBundle.getString("lblPrizedMoneyType"));
        lblDraw.setText(resourceBundle.getString("lblDraw"));
        lblAuction.setText(resourceBundle.getString("lblAuction"));
    }
    
    /* Auto Generated Method - setMandatoryHashMap()
     
    ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
     
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtDivisionNo", new Boolean(true));
        mandatoryMap.put("txtInstallmentNo", new Boolean(true));
        mandatoryMap.put("txtSchemeName", new Boolean(true));
        mandatoryMap.put("tdtDrawOrAuctionDt", new Boolean(true));
        mandatoryMap.put("txtPrizedAmount", new Boolean(true));
        mandatoryMap.put("txtTotalBonusAmount", new Boolean(true));
        mandatoryMap.put("txtNextBonusAmount", new Boolean(true));
        mandatoryMap.put("txtCommisionAmount", new Boolean(true));
        mandatoryMap.put("txtTotalDiscount", new Boolean(true));
        mandatoryMap.put("txtNetAmountPayable", new Boolean(true));
        mandatoryMap.put("txtApplicationNo", new Boolean(true));
        mandatoryMap.put("txtChittalNo", new Boolean(true));
        mandatoryMap.put("txtInstallmentsDue", new Boolean(true));
        mandatoryMap.put("txtInstallmentsAmountPaid", new Boolean(true));
        mandatoryMap.put("txtInstallmentsPaid", new Boolean(true));
        mandatoryMap.put("tdtInstallmentDate", new Boolean(true));
    }
    
/* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    
    private void initComponentData() {
        try{
            
        }catch(ClassCastException e){
            parseException.logException(e,true);
        }
    }
    
    private void setMaximumLength(){
        txtDivisionNo.setValidation(new NumericValidation());
    }
    
    /** Auto Generated Method - update()
     * This method called by Observable. It updates the UI with
     * Observable's data. If needed add/Remove RadioButtons
     * method need to be added.*/
    public void update(Observable observed, Object arg) {
    }
    
    
    public void update(){
        txtSchemeName.setText(observable.getTxtSchemeName());
        tdtDrawOrAuctionDt.setDateValue(observable.getTdtDrawOrAuctionDt());
        txtInstallmentNo.setText(observable.getTxtInstallmentNo());
        txtDivisionNo.setText(observable.getTxtDivisionNo());
        String  draw=CommonUtil.convertObjToStr(observable.getChkDraw());
        if(draw.length()>0 && draw.equals("Y"))
            chkDraw.setSelected(true);
        else
            chkDraw.setSelected(false);
        String  auction=CommonUtil.convertObjToStr(observable.getChkAuction());
        if(auction.length()>0 && auction.equals("Y"))
            chkAuction.setSelected(true);
        else
            chkAuction.setSelected(false);
        if(!chkAuction.isSelected() && !chkDraw.isSelected()){
            chkNoAuction.setSelected(true);
        }
        String userDefined = CommonUtil.convertObjToStr(observable.getChkUserDefined());
        if (userDefined.length() > 0 && userDefined.equals("Y")) {
            chkUserDefined.setSelected(true);
        } else {
            chkUserDefined.setSelected(false);
        }
        tdtNextInstallmentDt.setDateValue(observable.getTdtNextInstallmentDt());
        tdtNextInstallmentDt1.setDateValue(observable.getTdtPayementDate());
        txtPrizedAmount.setText(observable.getTxtPrizedAmount());
        txtTotalBonusAmount.setText(observable.getTxtTotalBonusAmount());
        txtNextBonusAmount.setText(observable.getTxtNextBonusAmount());
        txtCommisionAmount.setText(observable.getTxtCommisionAmount());
        txtTotalDiscount.setText(observable.getTxtTotalDiscount());
        txtNetAmountPayable.setText(observable.getTxtNetAmountPayable());
        txtApplicationNo.setText(observable.getTxtApplicationNo());
        txtChittalNo.setText(observable.getTxtChittalNo());
        lblMembType.setText(observable.getLblMembType());
        lblMemberName.setText(observable.getLblMemberName());
        txtInstallmentsPaid.setText(observable.getTxtInstallmentsPaid());
        txtInstallmentsAmountPaid.setText(observable.getTxtInstallmentsAmountPaid());
        txtInstallmentsDue.setText(observable.getTxtInstallmentsDue());
        txtInstallmentOverDueAmount.setText(observable.getTxtInstallmentOverDueAmount());
        txtSubNo.setText(observable.getTxtSubNo());
    }
    
    /* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
   public void updateOBFields() {
        observable.setTxtSchemeName(txtSchemeName.getText());
        observable.setTdtDrawOrAuctionDt(tdtDrawOrAuctionDt.getDateValue());
        observable.setTxtInstallmentNo(txtInstallmentNo.getText());
        observable.setTxtDivisionNo(txtDivisionNo.getText());
        if(chkDraw.isSelected()==true)
            observable.setChkDraw(CommonUtil.convertObjToStr("Y"));
        else
            observable.setChkDraw(CommonUtil.convertObjToStr("N"));
        
        if(chkAuction.isSelected()==true)
            observable.setChkAuction(CommonUtil.convertObjToStr("Y"));
        else
            observable.setChkAuction(CommonUtil.convertObjToStr("N"));
        if(chkUserDefined.isSelected()==true)
            observable.setChkUserDefined(CommonUtil.convertObjToStr("Y"));
        else
            observable.setChkUserDefined(CommonUtil.convertObjToStr("N"));
        observable.setTdtNextInstallmentDt(tdtNextInstallmentDt.getDateValue());
        observable.setTdtPayementDate(tdtNextInstallmentDt1.getDateValue());
        observable.setTxtPrizedAmount(txtPrizedAmount.getText());
        observable.setTxtTotalBonusAmount(txtTotalBonusAmount.getText());
        observable.setTxtNextBonusAmount(txtNextBonusAmount.getText());
        observable.setTxtCommisionAmount(txtCommisionAmount.getText());
        observable.setTxtTotalDiscount(txtTotalDiscount.getText());
        observable.setTxtNetAmountPayable(txtNetAmountPayable.getText());
        observable.setTxtApplicationNo(txtApplicationNo.getText());
        observable.setTxtChittalNo(txtChittalNo.getText());
        observable.setLblMembType(lblMembType.getText());
        observable.setLblMemberName(lblMemberName.getText());
        observable.setTxtInstallmentsPaid(txtInstallmentsPaid.getText());
        observable.setTxtInstallmentsAmountPaid(txtInstallmentsAmountPaid.getText());
        observable.setTxtInstallmentsDue(txtInstallmentsDue.getText());
        observable.setTxtInstallmentOverDueAmount(txtInstallmentOverDueAmount.getText());
        observable.setTxtSubNo(txtSubNo.getText());
        //Added by sreekrishnan 
        if(chkTransaction.isSelected()==true)
            observable.setChkTransaction(CommonUtil.convertObjToStr("Y"));
        else
            observable.setChkTransaction(CommonUtil.convertObjToStr("N"));
        if(chkNoAuction.isSelected()==true)
            observable.setChkNoAuction(CommonUtil.convertObjToStr("Y"));
        else
            observable.setChkNoAuction(CommonUtil.convertObjToStr("N"));
    }
    
    
    
    /* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        MDSPrizedMoneyDetailsEntryMRB objMandatoryRB = new MDSPrizedMoneyDetailsEntryMRB();
        txtDivisionNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDivisionNo"));
        txtInstallmentNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInstallmentNo"));
        txtSchemeName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSchemeName"));
        tdtDrawOrAuctionDt.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtDrawOrAuctionDt"));
        txtPrizedAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPrizedAmount"));
        txtTotalBonusAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTotalBonusAmount"));
        txtNextBonusAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNextBonusAmount"));
        txtCommisionAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCommisionAmount"));
        txtTotalDiscount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTotalDiscount"));
        txtNetAmountPayable.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNetAmountPayable"));
        txtApplicationNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtApplicationNo"));
        //        txtChittalNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtChittalNo"));
        txtInstallmentsDue.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInstallmentsDue"));
        txtInstallmentsAmountPaid.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInstallmentsAmountPaid"));
        txtInstallmentsPaid.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInstallmentsPaid"));
        //        tdtInstallmentDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtInstallmentDate"));
    }
    
    
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
        
        btnAuthorize.setEnabled(btnNew.isEnabled());
        btnReject.setEnabled(btnNew.isEnabled());
        btnException.setEnabled(btnNew.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdgIsLapsedGR = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgEFTProductGR = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgPayableBranchGR = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgPrintServicesGR = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgSeriesGR = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrAdvances = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace6 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace73 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace74 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace75 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace76 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace77 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace78 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        tabPrizedMoneyDetails = new com.see.truetransact.uicomponent.CTabbedPane();
        panPrizedMoneyDetails = new com.see.truetransact.uicomponent.CPanel();
        panInsideMDSDetails = new com.see.truetransact.uicomponent.CPanel();
        lblSchemeName = new com.see.truetransact.uicomponent.CLabel();
        txtDivisionNo = new com.see.truetransact.uicomponent.CTextField();
        lblDivisionNo = new com.see.truetransact.uicomponent.CLabel();
        lblInstallmentNo = new com.see.truetransact.uicomponent.CLabel();
        lblDrawOrAuctionDt = new com.see.truetransact.uicomponent.CLabel();
        txtInstallmentNo = new com.see.truetransact.uicomponent.CTextField();
        panCustomerId4 = new com.see.truetransact.uicomponent.CPanel();
        txtSchemeName = new com.see.truetransact.uicomponent.CTextField();
        btnSchemeName = new com.see.truetransact.uicomponent.CButton();
        tdtDrawOrAuctionDt = new com.see.truetransact.uicomponent.CDateField();
        panPrizedMoneyType = new com.see.truetransact.uicomponent.CPanel();
        chkDraw = new com.see.truetransact.uicomponent.CCheckBox();
        chkAuction = new com.see.truetransact.uicomponent.CCheckBox();
        lblAuction = new com.see.truetransact.uicomponent.CLabel();
        lblDraw = new com.see.truetransact.uicomponent.CLabel();
        lblPrizedMoneyType = new com.see.truetransact.uicomponent.CLabel();
        chkNoAuction = new com.see.truetransact.uicomponent.CCheckBox();
        lblNextInstallmentDt = new com.see.truetransact.uicomponent.CLabel();
        tdtNextInstallmentDt = new com.see.truetransact.uicomponent.CDateField();
        tdtNextInstallmentDt1 = new com.see.truetransact.uicomponent.CDateField();
        lblNextInstallmentDt1 = new com.see.truetransact.uicomponent.CLabel();
        panInsidePrizedMoneyDetails = new com.see.truetransact.uicomponent.CPanel();
        lblPrizedAmount = new com.see.truetransact.uicomponent.CLabel();
        lblTotalBonusAmount = new com.see.truetransact.uicomponent.CLabel();
        lblNextBonusAmount = new com.see.truetransact.uicomponent.CLabel();
        lblCommisionAmount = new com.see.truetransact.uicomponent.CLabel();
        lblTotalDiscount = new com.see.truetransact.uicomponent.CLabel();
        lblNetAmountPayable = new com.see.truetransact.uicomponent.CLabel();
        txtPrizedAmount = new com.see.truetransact.uicomponent.CTextField();
        txtTotalBonusAmount = new com.see.truetransact.uicomponent.CTextField();
        txtNextBonusAmount = new com.see.truetransact.uicomponent.CTextField();
        txtCommisionAmount = new com.see.truetransact.uicomponent.CTextField();
        txtTotalDiscount = new com.see.truetransact.uicomponent.CTextField();
        txtNetAmountPayable = new com.see.truetransact.uicomponent.CTextField();
        lblNextInstallmentAmount = new com.see.truetransact.uicomponent.CLabel();
        txtNextInstallmentAmount = new com.see.truetransact.uicomponent.CTextField();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        chkUserDefined = new com.see.truetransact.uicomponent.CCheckBox();
        chkTransaction = new com.see.truetransact.uicomponent.CCheckBox();
        cLabel2 = new com.see.truetransact.uicomponent.CLabel();
        panInsideMemberDetails = new com.see.truetransact.uicomponent.CPanel();
        lblInstallmentsAmountPaid = new com.see.truetransact.uicomponent.CLabel();
        lblInstallmentsPaid = new com.see.truetransact.uicomponent.CLabel();
        lblInstallmentOverDueAmount = new com.see.truetransact.uicomponent.CLabel();
        txtApplicationNo = new com.see.truetransact.uicomponent.CTextField();
        panCustomerId5 = new com.see.truetransact.uicomponent.CPanel();
        txtChittalNo = new com.see.truetransact.uicomponent.CTextField();
        btnChittalNo = new com.see.truetransact.uicomponent.CButton();
        lblMemberType = new com.see.truetransact.uicomponent.CLabel();
        lblChittalNo = new com.see.truetransact.uicomponent.CLabel();
        lblApplicationNo = new com.see.truetransact.uicomponent.CLabel();
        lblInstallmentsDue = new com.see.truetransact.uicomponent.CLabel();
        txtInstallmentsDue = new com.see.truetransact.uicomponent.CTextField();
        txtInstallmentsAmountPaid = new com.see.truetransact.uicomponent.CTextField();
        txtInstallmentsPaid = new com.see.truetransact.uicomponent.CTextField();
        lblMembType = new com.see.truetransact.uicomponent.CLabel();
        panAddressDetails = new com.see.truetransact.uicomponent.CPanel();
        lblStreet = new com.see.truetransact.uicomponent.CLabel();
        lblValStreet = new com.see.truetransact.uicomponent.CLabel();
        lblValArea = new com.see.truetransact.uicomponent.CLabel();
        lblArea = new com.see.truetransact.uicomponent.CLabel();
        lblPin = new com.see.truetransact.uicomponent.CLabel();
        lblValPin = new com.see.truetransact.uicomponent.CLabel();
        lblState = new com.see.truetransact.uicomponent.CLabel();
        lblValState = new com.see.truetransact.uicomponent.CLabel();
        lblValCity = new com.see.truetransact.uicomponent.CLabel();
        lblCity = new com.see.truetransact.uicomponent.CLabel();
        txtInstallmentOverDueAmount = new com.see.truetransact.uicomponent.CTextField();
        lblMemberName = new com.see.truetransact.uicomponent.CLabel();
        txtSubNo = new com.see.truetransact.uicomponent.CTextField();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        mbrMain = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptDelete = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitAuthorize = new javax.swing.JMenuItem();
        mitReject = new javax.swing.JMenuItem();
        mitException = new javax.swing.JMenuItem();
        sptException = new javax.swing.JSeparator();
        mitPrint = new javax.swing.JMenuItem();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setMinimumSize(new java.awt.Dimension(500, 660));
        setPreferredSize(new java.awt.Dimension(550, 660));
        getContentPane().setLayout(new java.awt.GridBagLayout());

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
        tbrAdvances.add(btnView);

        lblSpace6.setText("     ");
        tbrAdvances.add(lblSpace6);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnNew);

        lblSpace73.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace73.setText("     ");
        lblSpace73.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace73);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnEdit);

        lblSpace74.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace74.setText("     ");
        lblSpace74.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace74);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnDelete);

        lblSpace3.setText("     ");
        tbrAdvances.add(lblSpace3);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnSave);

        lblSpace75.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace75.setText("     ");
        lblSpace75.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace75);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.setEnabled(false);
        btnCancel.setFocusable(false);
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnCancel);

        lblSpace4.setText("     ");
        tbrAdvances.add(lblSpace4);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.setFocusable(true);
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnAuthorize);

        lblSpace76.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace76.setText("     ");
        lblSpace76.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace76.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace76.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace76);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnException);

        lblSpace77.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace77.setText("     ");
        lblSpace77.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace77.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace77.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace77);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnReject);

        lblSpace5.setText("     ");
        tbrAdvances.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrAdvances.add(btnPrint);

        lblSpace78.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace78.setText("     ");
        lblSpace78.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace78.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace78.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace78);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnClose);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(tbrAdvances, gridBagConstraints);

        tabPrizedMoneyDetails.setMinimumSize(new java.awt.Dimension(550, 490));
        tabPrizedMoneyDetails.setPreferredSize(new java.awt.Dimension(550, 490));

        panPrizedMoneyDetails.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panPrizedMoneyDetails.setMinimumSize(new java.awt.Dimension(570, 450));
        panPrizedMoneyDetails.setPreferredSize(new java.awt.Dimension(570, 450));
        panPrizedMoneyDetails.setLayout(new java.awt.GridBagLayout());

        panInsideMDSDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("MDS Details"));
        panInsideMDSDetails.setMinimumSize(new java.awt.Dimension(540, 155));
        panInsideMDSDetails.setPreferredSize(new java.awt.Dimension(540, 155));
        panInsideMDSDetails.setLayout(new java.awt.GridBagLayout());

        lblSchemeName.setText("GDS Group Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInsideMDSDetails.add(lblSchemeName, gridBagConstraints);

        txtDivisionNo.setMinimumSize(new java.awt.Dimension(50, 21));
        txtDivisionNo.setPreferredSize(new java.awt.Dimension(50, 21));
        txtDivisionNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDivisionNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInsideMDSDetails.add(txtDivisionNo, gridBagConstraints);

        lblDivisionNo.setText("Division No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInsideMDSDetails.add(lblDivisionNo, gridBagConstraints);

        lblInstallmentNo.setText("Installment No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInsideMDSDetails.add(lblInstallmentNo, gridBagConstraints);

        lblDrawOrAuctionDt.setText("Draw/Auction Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInsideMDSDetails.add(lblDrawOrAuctionDt, gridBagConstraints);

        txtInstallmentNo.setMinimumSize(new java.awt.Dimension(50, 21));
        txtInstallmentNo.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInsideMDSDetails.add(txtInstallmentNo, gridBagConstraints);

        panCustomerId4.setPreferredSize(new java.awt.Dimension(122, 21));
        panCustomerId4.setLayout(new java.awt.GridBagLayout());

        txtSchemeName.setAllowAll(true);
        txtSchemeName.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSchemeName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSchemeNameActionPerformed(evt);
            }
        });
        txtSchemeName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSchemeNameFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCustomerId4.add(txtSchemeName, gridBagConstraints);

        btnSchemeName.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnSchemeName.setEnabled(false);
        btnSchemeName.setMaximumSize(new java.awt.Dimension(21, 21));
        btnSchemeName.setMinimumSize(new java.awt.Dimension(21, 21));
        btnSchemeName.setPreferredSize(new java.awt.Dimension(21, 21));
        btnSchemeName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSchemeNameActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panCustomerId4.add(btnSchemeName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInsideMDSDetails.add(panCustomerId4, gridBagConstraints);

        tdtDrawOrAuctionDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtDrawOrAuctionDtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInsideMDSDetails.add(tdtDrawOrAuctionDt, gridBagConstraints);

        panPrizedMoneyType.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panPrizedMoneyType.setMinimumSize(new java.awt.Dimension(375, 25));
        panPrizedMoneyType.setPreferredSize(new java.awt.Dimension(420, 25));
        panPrizedMoneyType.setLayout(new java.awt.GridBagLayout());

        chkDraw.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkDrawActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panPrizedMoneyType.add(chkDraw, gridBagConstraints);

        chkAuction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkAuctionActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panPrizedMoneyType.add(chkAuction, gridBagConstraints);

        lblAuction.setText("Auction");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPrizedMoneyType.add(lblAuction, gridBagConstraints);

        lblDraw.setText("Draw");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPrizedMoneyType.add(lblDraw, gridBagConstraints);

        lblPrizedMoneyType.setText("Prized Money Type               ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPrizedMoneyType.add(lblPrizedMoneyType, gridBagConstraints);

        chkNoAuction.setText("No Auction");
        chkNoAuction.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        chkNoAuction.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        chkNoAuction.setMinimumSize(new java.awt.Dimension(99, 20));
        chkNoAuction.setPreferredSize(new java.awt.Dimension(99, 25));
        chkNoAuction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkNoAuctionActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panPrizedMoneyType.add(chkNoAuction, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInsideMDSDetails.add(panPrizedMoneyType, gridBagConstraints);

        lblNextInstallmentDt.setText("Next Installment Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInsideMDSDetails.add(lblNextInstallmentDt, gridBagConstraints);

        tdtNextInstallmentDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtNextInstallmentDtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInsideMDSDetails.add(tdtNextInstallmentDt, gridBagConstraints);

        tdtNextInstallmentDt1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtNextInstallmentDt1FocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInsideMDSDetails.add(tdtNextInstallmentDt1, gridBagConstraints);

        lblNextInstallmentDt1.setText("Prized Money Payment Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInsideMDSDetails.add(lblNextInstallmentDt1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPrizedMoneyDetails.add(panInsideMDSDetails, gridBagConstraints);

        panInsidePrizedMoneyDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Prized Money Details"));
        panInsidePrizedMoneyDetails.setMinimumSize(new java.awt.Dimension(540, 135));
        panInsidePrizedMoneyDetails.setPreferredSize(new java.awt.Dimension(540, 130));
        panInsidePrizedMoneyDetails.setLayout(new java.awt.GridBagLayout());

        lblPrizedAmount.setText("Prized Amount / Net Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsidePrizedMoneyDetails.add(lblPrizedAmount, gridBagConstraints);

        lblTotalBonusAmount.setText("Total Bonus Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsidePrizedMoneyDetails.add(lblTotalBonusAmount, gridBagConstraints);

        lblNextBonusAmount.setText("Next Bonus Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsidePrizedMoneyDetails.add(lblNextBonusAmount, gridBagConstraints);

        lblCommisionAmount.setText("Commission Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsidePrizedMoneyDetails.add(lblCommisionAmount, gridBagConstraints);

        lblTotalDiscount.setText("Total Discount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsidePrizedMoneyDetails.add(lblTotalDiscount, gridBagConstraints);

        lblNetAmountPayable.setText("Gross Amount Payable");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsidePrizedMoneyDetails.add(lblNetAmountPayable, gridBagConstraints);

        txtPrizedAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPrizedAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPrizedAmountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsidePrizedMoneyDetails.add(txtPrizedAmount, gridBagConstraints);

        txtTotalBonusAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsidePrizedMoneyDetails.add(txtTotalBonusAmount, gridBagConstraints);

        txtNextBonusAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtNextBonusAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNextBonusAmountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsidePrizedMoneyDetails.add(txtNextBonusAmount, gridBagConstraints);

        txtCommisionAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsidePrizedMoneyDetails.add(txtCommisionAmount, gridBagConstraints);

        txtTotalDiscount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsidePrizedMoneyDetails.add(txtTotalDiscount, gridBagConstraints);

        txtNetAmountPayable.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsidePrizedMoneyDetails.add(txtNetAmountPayable, gridBagConstraints);

        lblNextInstallmentAmount.setText("Next Installment Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsidePrizedMoneyDetails.add(lblNextInstallmentAmount, gridBagConstraints);

        txtNextInstallmentAmount.setAllowNumber(true);
        txtNextInstallmentAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtNextInstallmentAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNextInstallmentAmountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsidePrizedMoneyDetails.add(txtNextInstallmentAmount, gridBagConstraints);

        cLabel1.setText("User Defined");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panInsidePrizedMoneyDetails.add(cLabel1, gridBagConstraints);

        chkUserDefined.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chkUserDefinedItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panInsidePrizedMoneyDetails.add(chkUserDefined, gridBagConstraints);

        chkTransaction.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chkTransactionItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panInsidePrizedMoneyDetails.add(chkTransaction, gridBagConstraints);

        cLabel2.setText("Auction Transaction");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panInsidePrizedMoneyDetails.add(cLabel2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPrizedMoneyDetails.add(panInsidePrizedMoneyDetails, gridBagConstraints);

        panInsideMemberDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Member  Details"));
        panInsideMemberDetails.setMinimumSize(new java.awt.Dimension(540, 200));
        panInsideMemberDetails.setPreferredSize(new java.awt.Dimension(540, 200));

        lblInstallmentsAmountPaid.setText("Installment Amount Paid");

        lblInstallmentsPaid.setText("Installments Paid");

        lblInstallmentOverDueAmount.setText("Installment OverDue Amt");

        txtApplicationNo.setBackground(new java.awt.Color(212, 208, 200));
        txtApplicationNo.setMinimumSize(new java.awt.Dimension(100, 21));

        txtChittalNo.setAllowAll(true);
        txtChittalNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtChittalNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtChittalNoFocusLost(evt);
            }
        });

        btnChittalNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnChittalNo.setEnabled(false);
        btnChittalNo.setMaximumSize(new java.awt.Dimension(21, 21));
        btnChittalNo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnChittalNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnChittalNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChittalNoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panCustomerId5Layout = new javax.swing.GroupLayout(panCustomerId5);
        panCustomerId5.setLayout(panCustomerId5Layout);
        panCustomerId5Layout.setHorizontalGroup(
            panCustomerId5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panCustomerId5Layout.createSequentialGroup()
                .addComponent(txtChittalNo, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnChittalNo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panCustomerId5Layout.setVerticalGroup(
            panCustomerId5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtChittalNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(btnChittalNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        lblMemberType.setText("Member Type");

        lblChittalNo.setText("Chittal No");

        lblApplicationNo.setText("Application No");

        lblInstallmentsDue.setText("Installments Due");

        txtInstallmentsDue.setMinimumSize(new java.awt.Dimension(100, 21));

        txtInstallmentsAmountPaid.setMinimumSize(new java.awt.Dimension(100, 21));

        txtInstallmentsPaid.setMinimumSize(new java.awt.Dimension(100, 21));

        lblMembType.setMaximumSize(new java.awt.Dimension(100, 18));
        lblMembType.setMinimumSize(new java.awt.Dimension(100, 18));
        lblMembType.setPreferredSize(new java.awt.Dimension(100, 18));

        panAddressDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Address Details"));
        panAddressDetails.setMinimumSize(new java.awt.Dimension(457, 70));
        panAddressDetails.setPreferredSize(new java.awt.Dimension(457, 70));
        panAddressDetails.setLayout(new java.awt.GridBagLayout());

        lblStreet.setText("Street");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAddressDetails.add(lblStreet, gridBagConstraints);

        lblValStreet.setMinimumSize(new java.awt.Dimension(70, 10));
        lblValStreet.setPreferredSize(new java.awt.Dimension(70, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 5, 1, 1);
        panAddressDetails.add(lblValStreet, gridBagConstraints);

        lblValArea.setMinimumSize(new java.awt.Dimension(100, 10));
        lblValArea.setPreferredSize(new java.awt.Dimension(100, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 5, 1, 1);
        panAddressDetails.add(lblValArea, gridBagConstraints);

        lblArea.setText("Area");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAddressDetails.add(lblArea, gridBagConstraints);

        lblPin.setText("Pin");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAddressDetails.add(lblPin, gridBagConstraints);

        lblValPin.setMinimumSize(new java.awt.Dimension(90, 10));
        lblValPin.setPreferredSize(new java.awt.Dimension(90, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 5, 1, 1);
        panAddressDetails.add(lblValPin, gridBagConstraints);

        lblState.setText("State");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAddressDetails.add(lblState, gridBagConstraints);

        lblValState.setMinimumSize(new java.awt.Dimension(100, 10));
        lblValState.setPreferredSize(new java.awt.Dimension(100, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 5, 1, 1);
        panAddressDetails.add(lblValState, gridBagConstraints);

        lblValCity.setText("                     ");
        lblValCity.setMinimumSize(new java.awt.Dimension(100, 10));
        lblValCity.setPreferredSize(new java.awt.Dimension(100, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 5, 1, 1);
        panAddressDetails.add(lblValCity, gridBagConstraints);

        lblCity.setText("City");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAddressDetails.add(lblCity, gridBagConstraints);

        txtInstallmentOverDueAmount.setMinimumSize(new java.awt.Dimension(100, 21));

        lblMemberName.setForeground(new java.awt.Color(0, 51, 204));
        lblMemberName.setMaximumSize(new java.awt.Dimension(125, 18));
        lblMemberName.setMinimumSize(new java.awt.Dimension(100, 18));
        lblMemberName.setPreferredSize(new java.awt.Dimension(100, 18));

        txtSubNo.setMinimumSize(new java.awt.Dimension(40, 21));
        txtSubNo.setPreferredSize(new java.awt.Dimension(40, 21));

        javax.swing.GroupLayout panInsideMemberDetailsLayout = new javax.swing.GroupLayout(panInsideMemberDetails);
        panInsideMemberDetails.setLayout(panInsideMemberDetailsLayout);
        panInsideMemberDetailsLayout.setHorizontalGroup(
            panInsideMemberDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panInsideMemberDetailsLayout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(lblApplicationNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(txtApplicationNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(109, 109, 109)
                .addComponent(lblInstallmentsPaid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(txtInstallmentsPaid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(panInsideMemberDetailsLayout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(lblChittalNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panCustomerId5, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtSubNo, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblInstallmentsAmountPaid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(txtInstallmentsAmountPaid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(panInsideMemberDetailsLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(lblMemberType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(lblMembType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(113, 113, 113)
                .addComponent(lblInstallmentsDue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(txtInstallmentsDue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(panInsideMemberDetailsLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(lblMemberName, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(lblInstallmentOverDueAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(txtInstallmentOverDueAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(panInsideMemberDetailsLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(panAddressDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 503, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panInsideMemberDetailsLayout.setVerticalGroup(
            panInsideMemberDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panInsideMemberDetailsLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(panInsideMemberDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtApplicationNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtInstallmentsPaid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panInsideMemberDetailsLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(panInsideMemberDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblApplicationNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblInstallmentsPaid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(4, 4, 4)
                .addGroup(panInsideMemberDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSubNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtInstallmentsAmountPaid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panInsideMemberDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(panCustomerId5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(panInsideMemberDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblChittalNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblInstallmentsAmountPaid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(4, 4, 4)
                .addGroup(panInsideMemberDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtInstallmentsDue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panInsideMemberDetailsLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(panInsideMemberDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblMemberType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblMembType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblInstallmentsDue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(4, 4, 4)
                .addGroup(panInsideMemberDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtInstallmentOverDueAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panInsideMemberDetailsLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(panInsideMemberDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblMemberName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblInstallmentOverDueAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(3, 3, 3)
                .addComponent(panAddressDetails, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPrizedMoneyDetails.add(panInsideMemberDetails, gridBagConstraints);

        tabPrizedMoneyDetails.addTab("Prized Money Details", panPrizedMoneyDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(tabPrizedMoneyDetails, gridBagConstraints);

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace, gridBagConstraints);

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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(panStatus, gridBagConstraints);

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
        mnuProcess.add(sptDelete);

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

        mitAuthorize.setText("Authorize");
        mnuProcess.add(mitAuthorize);

        mitReject.setText("Rejection");
        mnuProcess.add(mitReject);

        mitException.setText("Exception");
        mnuProcess.add(mitException);
        mnuProcess.add(sptException);

        mitPrint.setText("Print");
        mnuProcess.add(mitPrint);

        mitClose.setText("Close");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrMain.add(mnuProcess);

        setJMenuBar(mbrMain);
    }// </editor-fold>//GEN-END:initComponents

    private void txtChittalNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtChittalNoFocusLost
        // TODO add your handling code here:
        if(txtChittalNo.getText().length()>0){
            HashMap whereMap = new HashMap();
            whereMap.put("CHITTAL_NO",txtChittalNo.getText());
            List lst=ClientUtil.executeQuery("getSelectChittalNo",whereMap);
            if(lst !=null && lst.size()>0){
                viewType = "CHIT_NO";
                whereMap=(HashMap)lst.get(0);
                whereMap.put("DIVISION_NO",txtDivisionNo.getText());
                List chitLst=ClientUtil.executeQuery("getSelectRecordForNotEnteredDetails",whereMap);
                if(chitLst !=null && chitLst.size()>0){
                    whereMap = (HashMap)chitLst.get(0);
                    fillData(whereMap);
                    chitLst = null;
                    lst=null;
                    whereMap=null;
                }else{
                    ClientUtil.displayAlert("Chittal Already Prized !!! ");
                    clearChitDetails();
                }
            }else{
                ClientUtil.displayAlert("Invalid Chittal No !!! ");
                clearChitDetails();
            }
        }
    }//GEN-LAST:event_txtChittalNoFocusLost
    private void clearChitDetails(){
        txtApplicationNo.setText("");
        txtChittalNo.setText("");
        lblMembType.setText("");
        lblMemberName.setText("");
        txtInstallmentsPaid.setText("");
        txtInstallmentsAmountPaid.setText("");
        txtInstallmentsDue.setText("");
        txtInstallmentOverDueAmount.setText("");
        txtSubNo.setText("");
        lblValStreet.setText("");
        lblValArea.setText("");
        lblValCity.setText("");
        lblValState.setText("");
        lblValPin.setText("");
    }
    private void isWeeklyOrMonthlyFrequency() {
        HashMap schemeMap = new HashMap();
        System.out.println("Inside isWeeklyOrMonthlyFrequency()");
        schemeMap.put("SCHEME_NAME", observable.getCommonScheme());
        schemeMap.put("GROUP_NO", observable.getTxtSchemeName());
        List lst = ClientUtil.executeQuery("getGDSSchemeNameDetailsProductLevel", schemeMap);
        if (lst != null && lst.size() > 0) {
            schemeMap = (HashMap) lst.get(0);
            int instFreq = CommonUtil.convertObjToInt(schemeMap.get("INSTALLMENT_FREQUENCY"));
            if (instFreq == 7) {
                isWeeklyOrMonthlyFrequency = "W";
            } else if (instFreq == 30) {
                isWeeklyOrMonthlyFrequency = "M";
            } else {
                isWeeklyOrMonthlyFrequency = "";
            }
        }
    }
    private void txtSchemeNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSchemeNameFocusLost
        // TODO add your handling code here:
        System.out.println("herererereererere");
        if(txtSchemeName.getText().length()>0){
            HashMap whereMap = new HashMap();
            whereMap.put("SCHEME_NAME",txtSchemeName.getText());
            List lst=ClientUtil.executeQuery("getSelectSchemeName",whereMap);
            if(lst !=null && lst.size()>0){
                viewType = "SCHEME_NAME";
                whereMap=(HashMap)lst.get(0);
                List chitLst=ClientUtil.executeQuery("getSelectEachSchemeDetailsList",whereMap);
                if(chitLst !=null && chitLst.size()>0){
                    whereMap = (HashMap)chitLst.get(0);
                    fillData(whereMap);
                    txtInstallmentNo.setEnabled(false);
                    chitLst = null;
                    lst=null;
                    whereMap=null;
                }   
            }else{
                ClientUtil.displayAlert("Invalid Scheme Name !!! ");
                txtSchemeName.setText("");
            }
        }  
    }//GEN-LAST:event_txtSchemeNameFocusLost
    
    private void txtNextBonusAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNextBonusAmountFocusLost
        // TODO add your handling code here:
        double bonusAmt = CommonUtil.convertObjToDouble(txtTotalBonusAmount.getText()).doubleValue();
        double perDivisionNo = CommonUtil.convertObjToDouble(observable.getNoOfMemberPerDiv()).doubleValue();
        double eachPersionBonus = bonusAmt / perDivisionNo;
        if(CommonUtil.convertObjToDouble(txtNextBonusAmount.getText()).doubleValue()>eachPersionBonus){
            ClientUtil.showAlertWindow("Amount Should not be greater than Rs. " + eachPersionBonus);
            txtNextBonusAmount.setText(CommonUtil.convertObjToStr(eachPersionBonus));
        }else{
            double enteredAmt = CommonUtil.convertObjToDouble(txtNextBonusAmount.getText()).doubleValue();
            double leftOverAmt = eachPersionBonus - enteredAmt;
            txtTotalDiscount.setText(CommonUtil.convertObjToStr(leftOverAmt * CommonUtil.convertObjToDouble(observable.getNoOfMemberPerDiv()).doubleValue()));
        }
    }//GEN-LAST:event_txtNextBonusAmountFocusLost
    
    private void txtDivisionNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDivisionNoFocusLost
        // TODO add your handling code here:
        System.out.println("Inside txtDivisionNoFocusLost(java.awt.event.FocusEvent evt)");
        if(txtDivisionNo.getText().length()>0){
            if(CommonUtil.convertObjToDouble(observable.getNoOfDivision()).doubleValue()>=CommonUtil.convertObjToDouble(txtDivisionNo.getText()).doubleValue()){
                if(CommonUtil.convertObjToInt(getDivisionNo()) != CommonUtil.convertObjToInt(txtDivisionNo.getText())){
                    if(txtChittalNo.getText().length() > 0){
                        String[] obj4 = {"Yes","No"};
                        int option3 = COptionPane.showOptionDialog(null,("Member Details will be cleared ?"), ("Prized Money Details"),
                        COptionPane.YES_NO_CANCEL_OPTION,COptionPane.QUESTION_MESSAGE,null,obj4,obj4[0]);
                        if(option3 == 0){
                            btnChittalNo.setEnabled(true);
                            txtApplicationNo.setText("");
                            txtChittalNo.setText("");
                            lblMembType.setText("");
                            lblMemberName.setText("");
                            txtInstallmentsPaid.setText("");
                            txtInstallmentsAmountPaid.setText("");
                            txtInstallmentsDue.setText("");
                            txtInstallmentOverDueAmount.setText("");
                            txtSubNo.setText("");
                            lblValStreet.setText("");
                            lblValArea.setText("");
                            lblValCity.setText("");
                            lblValState.setText("");
                            lblValPin.setText("");
                            //                        txtDivisionNo.setText("");
                            observable.setTxtApplicationNo("");
                            observable.setTxtChittalNo("");
                            observable.setLblMembType("");
                            observable.setLblMemberName("");
                            observable.setTxtInstallmentsPaid("");
                            observable.setTxtInstallmentsAmountPaid("");
                            observable.setTxtInstallmentsDue("");
                            observable.setTxtInstallmentOverDueAmount("");
                            observable.setTxtSubNo("");
                            HashMap divisionMap = new HashMap();
                            divisionMap.put("SCHEME_NAME",observable.getCommonScheme());
                            //divisionMap.put("DIVISION_NO",txtDivisionNo.getText());
                            divisionMap.put("DIVISION_NO",CommonUtil.convertObjToInt(txtDivisionNo.getText()));
                            divisionMap.put("GROUP_NO", observable.getTxtSchemeName());
                            List lst = ClientUtil.executeQuery("getGDSSchemeNameDetailsProductLevel", divisionMap);
                            if(lst!=null && lst.size()>0){
                                divisionMap = (HashMap)lst.get(0);
                                txtInstallmentNo.setText(CommonUtil.convertObjToStr(divisionMap.get("INST_COUNT")));
                                System.out.println("txtInstallmentNo.getText()111"+txtInstallmentNo.getText());  
                            }
                        }else{
                            txtDivisionNo.setText(getDivisionNo());
                        }
                    }
                    
                }
                HashMap divisionMap = new HashMap();
                divisionMap.put("SCHEME_NAME",observable.getCommonScheme());
                //divisionMap.put("DIVISION_NO",txtDivisionNo.getText());
                divisionMap.put("DIVISION_NO",CommonUtil.convertObjToInt(txtDivisionNo.getText()));
                divisionMap.put("GROUP_NO",observable.getTxtSchemeName());
                List lst = null;
                if(isSpecialScheme != null && isSpecialScheme.equals("Y")){
                lst = ClientUtil.executeQuery("getSpecialSchemeNameDetailsProductLevel", divisionMap);        
                }else{
                lst = ClientUtil.executeQuery("getGDSSchemeNameDetailsProductLevel", divisionMap);
                }
                if(lst!=null && lst.size()>0){
                    divisionMap = (HashMap)lst.get(0);
                    int count = CommonUtil.convertObjToInt(divisionMap.get("INST_COUNT"));
                    int instFreq = CommonUtil.convertObjToInt(divisionMap.get("INSTALLMENT_FREQUENCY"));
                    Date schemeStartDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(divisionMap.get("SCHEME_START_DT")));
                    Date drawOrAucDt = null;
                    Date nextInstDt = null;
                    divisionMap.put("SCHEME_NAME",txtSchemeName.getText());
                    divisionMap.put("DIVISION_NO",CommonUtil.convertObjToInt(txtDivisionNo.getText()));
                    divisionMap.put("SL_NO",CommonUtil.convertObjToInt(divisionMap.get("INST_COUNT")));
                    divisionMap.put("CURR_DT",currDt.clone());
                    if(observable.getMultipleMember().equals("N")){             // FULL_CHITTAL
                        lst = ClientUtil.executeQuery("getSchemeNameDetailsAcctLevel", divisionMap);
                        if(lst!=null && lst.size()>0){
                            divisionMap = (HashMap)lst.get(0);
                            Date alreadyEnteredDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(divisionMap.get("DRAW_AUCTION_DATE")));
                            if(DateUtil.dateDiff(alreadyEnteredDt,(Date) currDt.clone())>0){
                                ClientUtil.showAlertWindow("Prized Details Already Exists !!!");
                                txtDivisionNo.setText("");
                                btnCancelActionPerformed(null);
                            }
                        }else{
                            if(isSpecialScheme != null && isSpecialScheme.equals("Y") && !CommonUtil.convertObjToStr(txtDivisionNo.getText()).equals("1")){
                            txtInstallmentNo.setText(CommonUtil.convertObjToStr(count));
                            observable.setTxtInstallmentNo(CommonUtil.convertObjToStr(count));    
                            }else{
                            txtInstallmentNo.setText(CommonUtil.convertObjToStr(count+1));
                            observable.setTxtInstallmentNo(CommonUtil.convertObjToStr(count+1));
                            }
                            System.out.println("txtInstallmentNo.getText()2222"+txtInstallmentNo.getText());  
                            //If Weekly Frequency
                            if(instFreq==7){
                                if(count==0){
                                    drawOrAucDt = schemeStartDt;
                                    tdtDrawOrAuctionDt.setDateValue(DateUtil.getStringDate(drawOrAucDt));
                                    nextInstDt = DateUtil.addDays(schemeStartDt, 7);
                                    tdtNextInstallmentDt.setDateValue(DateUtil.getStringDate(nextInstDt));
                                    observable.setTdtNextInstallmentDt(tdtNextInstallmentDt.getDateValue());
                                    observable.setTdtDrawOrAuctionDt(tdtDrawOrAuctionDt.getDateValue());
                                }else{
                                    HashMap weeklyMap = new HashMap();
                                    weeklyMap.put("SCHEME_NAME",txtSchemeName.getText());
                                    weeklyMap.put("INSTALLMENT_NO",String.valueOf(count));
                                    List weeklyLst = ClientUtil.executeQuery("getWeeklyInstDate", weeklyMap);
                                    if(weeklyLst!=null && weeklyLst.size()>0){
                                        weeklyMap = (HashMap) weeklyLst.get(0);
                                        drawOrAucDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(weeklyMap.get("NEXT_INSTALLMENT_DATE")));
                                        tdtDrawOrAuctionDt.setDateValue(CommonUtil.convertObjToStr(weeklyMap.get("NEXT_INSTALLMENT_DATE")));
                                        nextInstDt = DateUtil.addDays(drawOrAucDt, 7);
                                        tdtNextInstallmentDt.setDateValue(DateUtil.getStringDate(nextInstDt));
                                        observable.setTdtNextInstallmentDt(tdtNextInstallmentDt.getDateValue());
                                        observable.setTdtDrawOrAuctionDt(tdtDrawOrAuctionDt.getDateValue());
                                    }
                                }
                                tdtNextInstallmentDt.setEnabled(false);
                            }
                        }
                    }else if(observable.getMultipleMember().equals("Y")){       //CO_CHITTAL
                        List instCount = ClientUtil.executeQuery("getMaxInstNoCoChittal", divisionMap);
                        if(instCount!=null && instCount.size()>0){
                            HashMap whereMap = (HashMap)instCount.get(0);
                            int maxInsNo = CommonUtil.convertObjToInt(whereMap.get("INST_COUNT"));
                            lst = ClientUtil.executeQuery("getSchemeNameDetailsCoChittalAcctLevel", divisionMap);
                            if(lst!=null && lst.size()>0){
                                divisionMap = (HashMap)lst.get(0);
                                Date alreadyEnteredDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(divisionMap.get("DRAW_AUCTION_DATE")));
                                List countLst = ClientUtil.executeQuery("getCountPrizedCoChittal", divisionMap);
                                if(countLst!=null && countLst.size()>0){
                                    if(DateUtil.dateDiff(alreadyEnteredDt,(Date) currDt.clone())>0){
                                        ClientUtil.showAlertWindow("Prized Details Already Exists !!!");
                                        txtDivisionNo.setText("");
                                        btnCancelActionPerformed(null);
                                    }
                                }else{
                                    txtInstallmentNo.setText(String.valueOf(maxInsNo));
                                    System.out.println("txtInstallmentNo.getText()3333"+txtInstallmentNo.getText());  
                                    observable.setTxtInstallmentNo(String.valueOf(maxInsNo));
                                    txtChittalNo.setText(CommonUtil.convertObjToStr(divisionMap.get("CHITTAL_NO")));
                                    txtChittalNoFocusLost(null);
                                    txtChittalNo.setEnabled(false);
                                    btnChittalNo.setEnabled(false);
                                }
                            }else{
                                txtInstallmentNo.setText(String.valueOf(maxInsNo+1));
                                observable.setTxtInstallmentNo(String.valueOf(maxInsNo+1));
                            }
                        }
                    }
                }
                HashMap existingMap = new HashMap();
                existingMap.put("SCHEME_NAME",txtSchemeName.getText());
                //existingMap.put("DIVISION_NO",txtDivisionNo.getText());
                existingMap.put("DIVISION_NO",CommonUtil.convertObjToInt(txtDivisionNo.getText()));
                //existingMap.put("INSTALLMENT_NO",txtInstallmentNo.getText());
                existingMap.put("INSTALLMENT_NO",CommonUtil.convertObjToInt(txtInstallmentNo.getText()));
                if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
                    List authLst = ClientUtil.executeQuery("getSchemeNameDetailsNotAuthRec",existingMap);
                    if(authLst!= null && authLst.size() > 0){
                        ClientUtil.showAlertWindow("Record Exists,Pending For Authorization !!!");
                        btnCancelActionPerformed(null);
                    }
                }
            }else{
                ClientUtil.showAlertWindow("Invalid Division No");
                txtDivisionNo.setText("");
                observable.setTxtDivisionNo("");
                txtInstallmentNo.setText("");
                return;
            }
        }
    }//GEN-LAST:event_txtDivisionNoFocusLost
    
    private void txtPrizedAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPrizedAmountFocusLost
        // TODO add your handling code here:
        if(userDefined.equals("N")){
        if(txtChittalNo.getText().length()>0){
            if(txtPrizedAmount.getText().length()>0){
                int countCoChittal=0;
                double totalAmt = CommonUtil.convertObjToDouble(observable.getTotalAmtDivision()).doubleValue();
                System.out.println("totalAmt "+totalAmt);
                if(observable.getMultipleMember().equals("Y")){
                    HashMap whereMap = new HashMap();
                    whereMap.put("CHITTAL_NUMBER",txtChittalNo.getText());
                    whereMap.put("SCHEME_NAME",txtSchemeName.getText());
                    List applicationLst=ClientUtil.executeQuery("getSelectChitNoNotinMasterDetails", whereMap);
                    if(applicationLst!=null && applicationLst.size()>0){
                        countCoChittal = applicationLst.size();
                        totalAmt = totalAmt/countCoChittal;
                    }
                }
                double minBidAmt = CommonUtil.convertObjToDouble(observable.getAuctionMinAmt()).doubleValue();
                double maxBidAmt = CommonUtil.convertObjToDouble(observable.getAuctionMaxAmt()).doubleValue();
                double minLeaveAmt = totalAmt * minBidAmt /100;
                double maxLeaveAmt = totalAmt * maxBidAmt /100;
                double totalMax = totalAmt - maxLeaveAmt;
                double totalMin = totalAmt - minLeaveAmt;
                if(observable.getCommisionType().equals("Percent")){
                    double commisonAmt = totalAmt * CommonUtil.convertObjToDouble(observable.getCommisionRate()).doubleValue() /100;
                    txtCommisionAmount.setText(String.valueOf(commisonAmt));
                }else if(observable.getCommisionType().equals("Absolute")){
                    txtCommisionAmount.setText(String.valueOf(observable.getCommisionRate()));
                }
                totalMin = totalMin-CommonUtil.convertObjToDouble(txtCommisionAmount.getText()).doubleValue();
                System.out.println("thalayal"+thalayal);
                if(CommonUtil.convertObjToDouble(txtPrizedAmount.getText()).doubleValue()<=totalMin || thalayal ){
                    //                    if(CommonUtil.convertObjToDouble(txtPrizedAmount.getText()).doubleValue()<totalMax ||
                    //                    CommonUtil.convertObjToDouble(txtPrizedAmount.getText()).doubleValue()>totalMin){
                    //                        ClientUtil.showAlertWindow("Prized Amount entered should be between Rs. " +totalMax +" and Rs. "+totalMin);
                    //                        txtPrizedAmount.setText("");
                    //                    }else{
                    if(!thalayal){
                    if(observable.getCommisionType().equals("Percent")){
                        double commisonAmt = totalAmt * CommonUtil.convertObjToDouble(observable.getCommisionRate()).doubleValue() /100;
                        txtCommisionAmount.setText(String.valueOf(commisonAmt));
                    }else if(observable.getCommisionType().equals("Absolute")){
                        txtCommisionAmount.setText(String.valueOf(observable.getCommisionRate()));
                    }
                    if((CommonUtil.convertObjToDouble(txtPrizedAmount.getText()).doubleValue()<totalMax ||
                    CommonUtil.convertObjToDouble(txtPrizedAmount.getText()).doubleValue()>totalMin) && !thalayal){
                        ClientUtil.showAlertWindow("Prized Amount entered should be between Rs. " +totalMax +" and Rs. "+totalMin);
                        clearPrizedAmountDetails();
                        return;
                        }
                        double bonusAmt = totalAmt - (CommonUtil.convertObjToDouble(txtPrizedAmount.getText()).doubleValue()+
                        CommonUtil.convertObjToDouble(txtCommisionAmount.getText()).doubleValue());
                        double perDivisionNo = CommonUtil.convertObjToDouble(observable.getNoOfMemberPerDiv()).doubleValue();
                        double eachPersionBonus = bonusAmt / perDivisionNo;
                        txtTotalBonusAmount.setText(String.valueOf(bonusAmt));
                        txtNextBonusAmount.setText(String.valueOf(eachPersionBonus));
                        txtNetAmountPayable.setText(String.valueOf(CommonUtil.convertObjToDouble(txtPrizedAmount.getText()).doubleValue() +
                            CommonUtil.convertObjToDouble(txtCommisionAmount.getText()).doubleValue() +
                            CommonUtil.convertObjToDouble(txtTotalBonusAmount.getText()).doubleValue()));
                    }else{
                        txtCommisionAmount.setText(String.valueOf(0.0));
                    }
                }else{
                    ClientUtil.showAlertWindow("Prized Amount should be equal to or lesser than Rs. " +totalMin);
                    clearPrizedAmountDetails();
                    return;
                }
            }
        }else{
            ClientUtil.showAlertWindow("Chittal No Should Not be Empty !!!");;
            txtPrizedAmount.setText("");
            return;
        }
        HashMap instMap = new HashMap();
        instMap.put("GROUP_NO", txtSchemeName.getText());
        List instList = ClientUtil.executeQuery("getGDSNxtInstallmntAmt", instMap);
        System.out.println("instList  ::  "+instList);
        instMap = new HashMap();
        if(instList!=null && instList.size()>0){
          instMap = (HashMap)instList.get(0);
          System.out.println("instMap  ::  "+instMap);
        }
        double instAmt = 0.0;
        double bonusAmt = 0.0;
        //instAmt = CommonUtil.convertObjToDouble(instMap.get("SUM(INSTALLMENT_AMOUNT)"));
        instAmt = CommonUtil.convertObjToDouble(instMap.get("INSTALLMENT_AMOUNT"));
        System.out.println("instAmt  ::  "+instAmt);
        bonusAmt = CommonUtil.convertObjToDouble(txtNextBonusAmount.getText());
        instAmt = instAmt - bonusAmt;
        System.out.println("instAmt  ::  "+instAmt);
        txtNextInstallmentAmount.setText(CommonUtil.convertObjToStr(instAmt));
        instAmt = 0.0;
        bonusAmt = 0.0; 
        txtTotalDiscount.setText("0");
        }
        else{
            txtTotalDiscount.setText("0");
            System.out.println("userDefined"+userDefined);
        }
    }//GEN-LAST:event_txtPrizedAmountFocusLost
    private void enableFields(boolean flag){
        txtCommisionAmount.setEnabled(flag);
        txtTotalBonusAmount.setEnabled(flag);
        txtNextInstallmentAmount.setEnabled(flag);
        txtCommisionAmount.setEnabled(flag);
        txtTotalDiscount.setEnabled(flag);
        txtNetAmountPayable.setEnabled(flag);
        if (viewType != "Edit") {
            clearPrizedAmountDetails();
        }
    }
    private void clearPrizedAmountDetails(){
        txtPrizedAmount.setText("");
        txtCommisionAmount.setText("");
        txtTotalDiscount.setText("");
        txtTotalBonusAmount.setText("");
        txtNextBonusAmount.setText("");
        txtNextInstallmentAmount.setText("");
        txtNetAmountPayable.setText("");
    }
    private void tdtNextInstallmentDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtNextInstallmentDtFocusLost
//        // TODO add your handling code here:
//        if(observable.getHolidayType().equals("any next working day")){
//            Date nextInst = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtNextInstallmentDt.getDateValue()));
//            Date obnextInst = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(observable.getTdtNextInstallmentDt()));
//            if(DateUtil.dateDiff(nextInst,obnextInst)>0){
//                ClientUtil.showAlertWindow("date should greater than or equal "+DateUtil.getStringDate(obnextInst));
//                return;
//            }
//        }else if(observable.getHolidayType().equals("previous day")){
//            Date nextInst = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtNextInstallmentDt.getDateValue()));
//            Date obnextInst = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(observable.getTdtNextInstallmentDt()));
//            if(DateUtil.dateDiff(nextInst,obnextInst)<0){
//                ClientUtil.showAlertWindow("date should lesser than or equal "+DateUtil.getStringDate(obnextInst));
//                return;
//            }
//        }
    }//GEN-LAST:event_tdtNextInstallmentDtFocusLost
    
    private void chkAuctionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkAuctionActionPerformed
        // TODO add your handling code here:
        if(chkAuction.isSelected() == true){
            chkDraw.setEnabled(false);
            chkNoAuction.setEnabled(false);
        }else{
            chkDraw.setEnabled(true);
            chkNoAuction.setEnabled(true);
        }
    }//GEN-LAST:event_chkAuctionActionPerformed
    
    private void chkDrawActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkDrawActionPerformed
        // TODO add your handling code here:
        if(chkDraw.isSelected() == true){
            chkAuction.setEnabled(false);
            chkNoAuction.setEnabled(false);
        }else{
            chkAuction.setEnabled(true);
            chkNoAuction.setEnabled(true);
        }
    }//GEN-LAST:event_chkDrawActionPerformed
    
    private void tdtDrawOrAuctionDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtDrawOrAuctionDtFocusLost
        // TODO add your handling code here:
        System.out.println("Inside tdtDrawOrAuctionDtFocusLost(java.awt.event.FocusEvent evt)");
        if(tdtDrawOrAuctionDt.getDateValue().length()>0){
        if(DateUtil.dateDiff((Date) currDt.clone(),DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtDrawOrAuctionDt.getDateValue())))>0){
                ClientUtil.showAlertWindow("Entering Future Date Not Allow...!!!! ");
                tdtDrawOrAuctionDt.setDateValue("");
        }else{
            if(tdtDrawOrAuctionDt.getDateValue().length()>0){
                HashMap schemeMap = new HashMap();
                schemeMap.put("SCHEME_NAME",observable.getTxtSchemeName());
                schemeMap.put("GROUP_NO",observable.getTxtSchemeName());
                List lst = ClientUtil.executeQuery("getGDSSchemeNameDetailsProductLevel",schemeMap);
                if(lst!=null && lst.size()>0){
                    schemeMap = (HashMap)lst.get(0);
                    int inst = CommonUtil.convertObjToInt(schemeMap.get("INSTALLMENTS"));
                    int instDay = CommonUtil.convertObjToInt(schemeMap.get("INSTALLMENT_DAY"));
                    int instPayDay = CommonUtil.convertObjToInt(schemeMap.get("DAY_PAYMENT"));
                    Date day = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtDrawOrAuctionDt.getDateValue()));
                    if(instPayDay>0){
                        tdtNextInstallmentDt1.setDateValue(CommonUtil.convertObjToStr(DateUtil.addDays(day,instPayDay)));
                        }
                        int instFreq=0;
                        int count = CommonUtil.convertObjToInt(schemeMap.get("INST_COUNT"));
                        instFreq = CommonUtil.convertObjToInt(schemeMap.get("INSTALLMENT_FREQUENCY"));
                        if (instFreq == 7) {    
                        HashMap weeklyMap = new HashMap();
                        Date drawOrAucDt = null;
                        Date currentdrawOrAucDt = null;
                        long diffDayPending = 0;
                        weeklyMap.put("SCHEME_NAME", txtSchemeName.getText());
                        weeklyMap.put("INSTALLMENT_NO", String.valueOf(count));
                        List weeklyLst = ClientUtil.executeQuery("getWeeklyInstDate", weeklyMap);
                        if (weeklyLst != null && weeklyLst.size() > 0) {
                            weeklyMap = (HashMap) weeklyLst.get(0);
                            drawOrAucDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(weeklyMap.get("NEXT_INSTALLMENT_DATE")));
                            currentdrawOrAucDt = DateUtil.getDateMMDDYYYY(tdtDrawOrAuctionDt.getDateValue());
                            diffDayPending = DateUtil.dateDiff(drawOrAucDt, currentdrawOrAucDt);         
                            if (diffDayPending < 0) {
                                ClientUtil.showMessageWindow("Draw/Auction Date Should be equal or Greater than " + CommonUtil.convertObjToStr(weeklyMap.get("NEXT_INSTALLMENT_DATE")));
                                tdtDrawOrAuctionDt.setDateValue(CommonUtil.convertObjToStr(weeklyMap.get("NEXT_INSTALLMENT_DATE")));
                            }
                        }
                    } else if (instFreq == 30) { 
                        HashMap weeklyMap = new HashMap();
                        Date drawOrAucDt = null;
                        Date currentdrawOrAucDt = null;
                        long diffDayPending = 0;
                        weeklyMap.put("SCHEME_NAME", txtSchemeName.getText());
                        weeklyMap.put("INSTALLMENT_NO", String.valueOf(count));
                        List weeklyLst = ClientUtil.executeQuery("getWeeklyInstDate", weeklyMap);
                        if (weeklyLst != null && weeklyLst.size() > 0) {
                            weeklyMap = (HashMap) weeklyLst.get(0);
                            drawOrAucDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(weeklyMap.get("NEXT_INSTALLMENT_DATE")));
                            currentdrawOrAucDt = DateUtil.getDateMMDDYYYY(tdtDrawOrAuctionDt.getDateValue());
                            diffDayPending = DateUtil.dateDiff(drawOrAucDt, currentdrawOrAucDt);
                            if (diffDayPending < 0) {
                                ClientUtil.showMessageWindow("Draw/Auction Date Should be equal or Greater than " + CommonUtil.convertObjToStr(weeklyMap.get("NEXT_INSTALLMENT_DATE")));
                                tdtDrawOrAuctionDt.setDateValue(CommonUtil.convertObjToStr(weeklyMap.get("NEXT_INSTALLMENT_DATE")));
                            }
                        }
                    }
                    }
                }
            }
        }
    }//GEN-LAST:event_tdtDrawOrAuctionDtFocusLost
    
    private void tdtNextInstallmentDt1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtNextInstallmentDt1FocusLost
        // TODO add your handling code here:
//        Date paymentDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtNextInstallmentDt1.getDateValue()));
//        Date currDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtDrawOrAuctionDt.getDateValue()));
//        if(DateUtil.dateDiff(currDt,paymentDate)>0){
//            ClientUtil.showAlertWindow("Payment date should be "+ paymentDate + "on or after");
//            tdtNextInstallmentDt1.setDateValue("");
//            return;
//        }
        
    }//GEN-LAST:event_tdtNextInstallmentDt1FocusLost
    
    private void btnChittalNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChittalNoActionPerformed
        // TODO add your handling code here:
        if(txtDivisionNo.getText().length() == 0){
            ClientUtil.showAlertWindow("Division No should not be empty");
            return;
        }else{
            popUp("CHIT_NO");
            txtChittalNo.setEnabled(false);
        }
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
            btnNew.setEnabled(false);
            btnEdit.setEnabled(false);
            btnDelete.setEnabled(false);
            btnSave.setEnabled(true);
            btnView.setEnabled(false);
        }
    }//GEN-LAST:event_btnChittalNoActionPerformed
    
    private void btnSchemeNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSchemeNameActionPerformed
        // TODO add your handling code here:
        popUp("SCHEME_NAME");
        tdtDrawOrAuctionDt.setEnabled(true);
        txtInstallmentNo.setEnabled(false);
//        txtDivisionNo.setEnabled(true);
        txtSchemeName.setEnabled(false);
    }//GEN-LAST:event_btnSchemeNameActionPerformed
    
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        btnView.setEnabled(false);
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        popUp("Enquiry");
        ClientUtil.enableDisable(panPrizedMoneyDetails,false);
        lblStatus.setText("Enquiry");
        btnSave.setEnabled(false);
        btnView.setEnabled(false);
    }            //    private void enableDisableAliasBranchTable(boolean flag) {//GEN-LAST:event_btnViewActionPerformed
    
    
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        updateAuthorizeStatus(CommonConstants.STATUS_REJECTED);
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        txtDivisionNo.setEnabled(false);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        updateAuthorizeStatus(CommonConstants.STATUS_EXCEPTION);
        btnCancel.setEnabled(true);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        updateAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnCancel.setEnabled(true);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        txtDivisionNo.setEnabled(false);
        
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    private void updateAuthorizeStatus(String authorizeStatus) {        
        if (viewType == AUTHORIZE && isFilled){
            ArrayList arrList = new ArrayList();
            HashMap authorizeMap = new HashMap();
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            if(!chkNoAuction.isSelected()){
                singleAuthorizeMap.put("CHITTAL_NO", observable.getTxtChittalNo());
                singleAuthorizeMap.put("SUB_NO", CommonUtil.convertObjToInt(observable.getTxtSubNo()));
            }
            singleAuthorizeMap.put("SCHEME_NAME", observable.getTxtSchemeName());
            singleAuthorizeMap.put("AUTHORIZED_BY", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("AUTHORIZED_DT",currDt);
            singleAuthorizeMap.put("TRANS_ID",observable.getTransId());
            singleAuthorizeMap.put("USER_ID", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            arrList.add(singleAuthorizeMap);
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorize(authorizeMap,observable.getTxtChittalNo());
            viewType = "";
            ClientUtil.enableDisable(panPrizedMoneyDetails,false);
            super.setOpenForEditBy(observable.getStatusBy());
            singleAuthorizeMap = null;
            arrList = null;
            authorizeMap = null;
        } else {
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put("USER_ID", TrueTransactMain.USER_ID);
            whereMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            whereMap.put("TRANS_DT", currDt.clone());
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            mapParam.put(CommonConstants.MAP_NAME, "getGDSPrizedMoneyDetailsEntryAuthorize");
            isFilled = false;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
            observable.setStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }
    public void authorize(HashMap map,String id) {
        System.out.println("Authorize Map : " + map);
        
        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
            observable.set_authorizeMap(map);
            observable.doAction();
            if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED){
                super.setOpenForEditBy(observable.getStatusBy());
                super.removeEditLock(id);
                if (fromNewAuthorizeUI) {
                    newauthorizeListUI.removeSelectedRow();
                    this.dispose();
                    newauthorizeListUI.setFocusToTable();
                    newauthorizeListUI.displayDetails("MDS Prized Money Details");
                }
                 if (fromAuthorizeUI) {
                    authorizeListUI.removeSelectedRow();
                    this.dispose();
                    authorizeListUI.setFocusToTable();
                    authorizeListUI.displayDetails("MDS Prized Money Details");
                }
            }
            btnCancelActionPerformed(null);
            observable.setStatus();
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
        ClientUtil.enableDisable(panPrizedMoneyDetails,true);
        ClientUtil.clearAll(this);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        enableDisableButtons(true);
        txtApplicationNo.setEnabled(false);
        txtChittalNo.setEnabled(false);
        txtTotalBonusAmount.setEnabled(false);
        //        txtNextBonusAmount.setEnabled(false);
        txtCommisionAmount.setEnabled(false);
        txtCommisionAmount.setEnabled(false);
        txtTotalDiscount.setEnabled(false);
        txtNetAmountPayable.setEnabled(false);
        txtInstallmentsPaid.setEnabled(false);
        txtInstallmentsAmountPaid.setEnabled(false);
        txtInstallmentsDue.setEnabled(false);
        txtInstallmentOverDueAmount.setEnabled(false);
        txtSchemeName.setEnabled(true);
        txtSubNo.setEnabled(false);
        txtNextInstallmentAmount.setEnabled(false);
        thalayal = false;
    }//GEN-LAST:event_btnNewActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        popUp("Edit");
        lblStatus.setText("Edit");
        btnDelete.setEnabled(false);
        ClientUtil.enableDisable(panPrizedMoneyDetails,true);
        txtSchemeName.setEnabled(false);
        txtChittalNo.setEnabled(false);
        txtApplicationNo.setEnabled(false);
        txtInstallmentNo.setEnabled(false);
        txtInstallmentsPaid.setEnabled(false);
        txtInstallmentsAmountPaid.setEnabled(false);
        txtInstallmentsDue.setEnabled(false);
        txtInstallmentOverDueAmount.setEnabled(false);
        txtCommisionAmount.setEnabled(false);
        txtTotalDiscount.setEnabled(false);
        txtNetAmountPayable.setEnabled(false);
        txtTotalBonusAmount.setEnabled(false);
        btnNew.setEnabled(false);
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnSave.setEnabled(true);
        btnView.setEnabled(false);
        txtSubNo.setEnabled(false);
        btnChittalNo.setEnabled(true);
        observable.setOldChittalNo(txtChittalNo.getText());
        observable.setOldSubNo(txtSubNo.getText());
         txtNextInstallmentAmount.setEnabled(false);
        if(txtSchemeName.getText().length()<=0)
            btnCancelActionPerformed(null);
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        popUp("Delete");
        lblStatus.setText("Delete");
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        ClientUtil.enableDisable(panPrizedMoneyDetails,false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        savePerformed();
        btnCancel.setEnabled(true);
    }//GEN-LAST:event_btnSaveActionPerformed
    private void savePerformed(){
       if(txtDivisionNo.getText().length() == 0 ){
            ClientUtil.showAlertWindow("Division No should not be empty");
        }
        if(txtInstallmentNo.getText().length() == 0 ){
            ClientUtil.showAlertWindow("Installment No should not be empty");
        }else{
            if(!chkNoAuction.isSelected()){
//            HashMap existingMap = new HashMap();
//            existingMap.put("SCHEME_NAME",txtSchemeName.getText());
//            existingMap.put("DIVISION_NO",txtDivisionNo.getText());
//            existingMap.put("INSTALLMENT_NO",txtInstallmentNo.getText());
//            if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
//                List lst = ClientUtil.executeQuery("getSchemeNameDetailsNotAuthRec",existingMap);
//                if(lst!= null && lst.size() > 0){
//                    ClientUtil.showAlertWindow("Record Exists,Pending For Authorization !!!");
//                    return;
//                }
//            }
//            if(DateUtil.dateDiff(currDt.clone(),DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtDrawOrAuctionDt.getDateValue())))>0){
//                ClientUtil.showAlertWindow("Auction date should be equla to or lesser than "+DateUtil.getStringDate(currDt));
//                System.out.println("#######");
//            }else 
            System.out.println("#######getHolidayType"+observable.getHolidayType());
            if(chkDraw.isSelected() == false && chkAuction.isSelected() == false){
                ClientUtil.showAlertWindow("Select either Draw or Auction");
            }else if(observable.getHolidayType().equals("any next working day")){
                Date nextInst = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtNextInstallmentDt.getDateValue()));
                Date obnextInst = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(observable.getTdtNextInstallmentDt()));
//                if(DateUtil.dateDiff(nextInst,obnextInst)>0){
//                    ClientUtil.showAlertWindow("date should greater than or equal "+DateUtil.getStringDate(obnextInst));
//                    return;
//                }else 
                if(CommonUtil.convertObjToDouble(txtPrizedAmount.getText()).doubleValue() == 0){
                    ClientUtil.showAlertWindow("Prized Amount should not be empty");
                }else if(txtDivisionNo.getText().length() == 0){
                    ClientUtil.showAlertWindow("Division No should not empty");
                }else if(CommonUtil.convertObjToDouble(txtDivisionNo.getText()).doubleValue() == 0){
                    ClientUtil.showAlertWindow("Division no should not be Zero");
                }else if(txtChittalNo.getText().length() == 0){
                    ClientUtil.showAlertWindow("Chittal No should not empty");
                }else{
                    HashMap dataMap = new HashMap();
                    List aList = null;
                    Date dateAuction = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtDrawOrAuctionDt.getDateValue()));
                    dataMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
                    dataMap.put("SCHEME_NAME", txtSchemeName.getText());
                    dataMap.put("GROUP_NO", txtSchemeName.getText());
                    System.out.println("isWeeklyOrMonthlyFrequency%%#%"+isWeeklyOrMonthlyFrequency);
                    if(isWeeklyOrMonthlyFrequency != null && isWeeklyOrMonthlyFrequency.equals("M")){
                    	aList = ClientUtil.executeQuery("getGDSDivisionExistinPrizedMoney", dataMap);
                    }else if(isWeeklyOrMonthlyFrequency != null && isWeeklyOrMonthlyFrequency.equals("W")){
                    	aList = ClientUtil.executeQuery("getDivisionExistinPrizedMoneyWeekly", dataMap);    
                    }
                    String autDate = tdtDrawOrAuctionDt.getDateValue();
                    System.out.println("autDate -------" + autDate +""+aList +""+isWeeklyOrMonthlyFrequency);
                    int divNo = CommonUtil.convertObjToInt(txtDivisionNo.getText());
                    int month = dateAuction.getMonth() + 1;
                    int day = dateAuction.getDay();
                    String ye = CommonUtil.convertObjToStr(dateAuction.getYear());
                    ye = ye.substring(1, ye.length());
                    int year = CommonUtil.convertObjToInt(ye);
                    System.out.println("autDate ---month----" + month + "year ===" + year  +""+day);
                    if (isWeeklyOrMonthlyFrequency != null && isWeeklyOrMonthlyFrequency.equals("M")) {
                        if (aList.size() > 0 && aList != null) {
                            for (int i = 0; i < aList.size(); i++) {
                                HashMap mapop = (HashMap) aList.get(i);
                                if (mapop.containsKey("DDMM") && mapop.get("DDMM") != null) {
                                    String ddyy = CommonUtil.convertObjToStr(mapop.get("DDMM"));
                                    int divisionNo = CommonUtil.convertObjToInt(mapop.get("DIVISION_NO").toString());
                                    int months = CommonUtil.convertObjToInt(ddyy.substring(0, 2));
                                    int years = CommonUtil.convertObjToInt(ddyy.substring(3, ddyy.length()));
                                    System.out.println("ddyy ---days----" + months + "years ===" + years);
									if (month == months && year == years && divNo == divisionNo) {
                                        ClientUtil.showAlertWindow("Division No Already exists!!!");
                                        return;
                                    }
                                }
                            }
                        }
                    } else if (isWeeklyOrMonthlyFrequency != null && isWeeklyOrMonthlyFrequency.equals("W")) {
                        if (aList.size() > 0 && aList != null) {
                            for (int i = 0; i < aList.size(); i++) {
                                HashMap mapop = (HashMap) aList.get(i);
                                if (mapop.containsKey("DDMM") &&  mapop.get("DDMM") != null) {
                                    String ddyy = CommonUtil.convertObjToStr(mapop.get("DDMM"));
                                    int divisionNo = CommonUtil.convertObjToInt(mapop.get("DIVISION_NO").toString());
                                    int days = CommonUtil.convertObjToInt(ddyy.substring(0, 2));
                                    int months = CommonUtil.convertObjToInt(ddyy.substring(3, ddyy.length()));
                                    if (month == months && day == days && divNo == divisionNo) {
                                        ClientUtil.showAlertWindow("Division No Already exists!!!");
                                        return;
                                    }
                                }
                            }
                        }
                    }
                    updateOBFields();
                    observable.doAction();
                    if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED){
                        if (observable.getProxyReturnMap() != null && observable.getProxyReturnMap().size()>0) {
                            displayTransDetailNew(observable.getProxyReturnMap());
                        }
                        btnCancelActionPerformed(null);
                        btnCancel.setEnabled(true);
                        lblStatus.setText(ClientConstants.RESULT_STATUS[observable.getResult()]);
                    }
                    btnCancel.setEnabled(true);
                    btnAuthorize.setEnabled(true);
                    btnReject.setEnabled(true);
                    btnException.setEnabled(true);
                    setModified(false);
                    ClientUtil.clearAll(this);
                }
            }else if(observable.getHolidayType().equals("previous day")){
                Date nextInst = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtNextInstallmentDt.getDateValue()));
                Date obnextInst = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(observable.getTdtNextInstallmentDt()));
//                if(DateUtil.dateDiff(nextInst,obnextInst)<0){
//                    ClientUtil.showAlertWindow("date should lesser than or equal "+DateUtil.getStringDate(obnextInst));
//                    return;
//                }else 
                if(CommonUtil.convertObjToDouble(txtPrizedAmount.getText()).doubleValue() == 0){
                    ClientUtil.showAlertWindow("Prized Amount should not be empty");
                }else if(txtDivisionNo.getText().length() == 0){
                    ClientUtil.showAlertWindow("Division No should not empty");
                }else if(CommonUtil.convertObjToDouble(txtDivisionNo.getText()).doubleValue() == 0){
                    ClientUtil.showAlertWindow("Division no should not be Zero");
                }else if(txtChittalNo.getText().length() == 0){
                    ClientUtil.showAlertWindow("Chittal No should not empty");
                }else{
                    updateOBFields();
                    observable.doAction();
                    if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED){
                        if (observable.getProxyReturnMap() != null && observable.getProxyReturnMap().size()>0) {
                            displayTransDetailNew(observable.getProxyReturnMap());
                        }
                        btnCancelActionPerformed(null);
                        btnCancel.setEnabled(true);
                        lblStatus.setText(ClientConstants.RESULT_STATUS[observable.getResult()]);
                    }
                    btnCancel.setEnabled(true);
                    btnAuthorize.setEnabled(true);
                    btnReject.setEnabled(true);
                    btnException.setEnabled(true);
                    setModified(false);
                    ClientUtil.clearAll(this);
                }
            }else{
                updateOBFields();
                observable.doAction();
                if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED){
                    btnCancelActionPerformed(null);
                    btnCancel.setEnabled(true);
                    lblStatus.setText(ClientConstants.RESULT_STATUS[observable.getResult()]);
                }
                btnCancel.setEnabled(true);
                btnAuthorize.setEnabled(true);
                btnReject.setEnabled(true);
                btnException.setEnabled(true);
                setModified(false);
                ClientUtil.clearAll(this);
            }
        }else{
                updateOBFields();
                observable.doAction();
                if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED){
                    btnCancelActionPerformed(null);
                    btnCancel.setEnabled(true);
                    lblStatus.setText(ClientConstants.RESULT_STATUS[observable.getResult()]);
                }
                btnCancel.setEnabled(true);
                btnAuthorize.setEnabled(true);
                btnReject.setEnabled(true);
                btnException.setEnabled(true);
                setModified(false);
                ClientUtil.clearAll(this);
        }
        }
    }
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        deletescreenLock();
        viewType = "CANCEL" ;
        lblStatus.setText("               ");
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(this, false);
        setModified(false);
        btnNew.setEnabled(true);
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        btnSave.setEnabled(false);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
        btnView.setEnabled(true);
        isFilled = false;
        enableDisableButtons(false);
        observable.resetForm();
        lblValStreet.setText("");
        lblValArea.setText("");
        lblMembType.setText("");
        lblMemberName.setText("");
        lblValCity.setText("");
        lblValState.setText("");
        lblValPin.setText("");
        if (fromNewAuthorizeUI) {
            this.dispose();
            fromNewAuthorizeUI = false;
            newauthorizeListUI.setFocusToTable();
        }
         if (fromAuthorizeUI) {
            this.dispose();
            fromAuthorizeUI = false;
            authorizeListUI.setFocusToTable();
        }
         btnAuthorize.setEnabled(true);
         thalayal = false;
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void deletescreenLock(){
        HashMap map=new HashMap();
        map.put("USER_ID",ProxyParameters.USER_ID);
        map.put("TRANS_DT", currDt.clone());
        map.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
        ClientUtil.execute("DELETE_SCREEN_LOCK", map);
    }
    
    /** To display a popUp window for viewing existing data */
    private void popUp(String currAction){
        viewType = currAction;
        HashMap viewMap = new HashMap();
        if(currAction.equalsIgnoreCase("SCHEME_NAME")){
            HashMap map = new HashMap();
          //  viewMap.put(CommonConstants.MAP_NAME, "getSelectEachSchemeDetails");
            viewMap.put(CommonConstants.MAP_NAME, "getSelectEachGDSGroupDetails");            
        }else if(currAction.equalsIgnoreCase("CHIT_NO")){
            HashMap where = new HashMap();
            where.put("SCHEME_NAMES",txtSchemeName.getText());
            where.put("DIVISION_NO",txtDivisionNo.getText());
            viewMap.put(CommonConstants.MAP_WHERE, where);
            viewMap.put(CommonConstants.MAP_NAME, "getSelectGDSRecordForNotEnteredDetailsView");
        }else if (currAction.equalsIgnoreCase("Edit") || currAction.equalsIgnoreCase("Delete")){
            HashMap map = new HashMap();
            viewMap.put(CommonConstants.MAP_NAME, "getGDSPrizedMoneyDetailsEntryEditDelete");
        }else if(currAction.equalsIgnoreCase("Enquiry")){
            HashMap map = new HashMap();
            map.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getPrizedMoneyDetailsEntryView");
        }
        new ViewAll(this,viewMap).show();
    }
    private void isInstallmetNoExceedCurrntInstNo(int noOfInst){
        int instNo =  CommonUtil.convertObjToInt(txtInstallmentNo.getText()); 
        int currntNoOfInst =  noOfInst; 
        if(instNo > currntNoOfInst){
            ClientUtil.showAlertWindow("Scheme Installment is Over");    
            btnCancelActionPerformed(null);
        }
    }
    public void fillData(Object obj){
        try{
            HashMap hashMap=(HashMap)obj;
            System.out.println("### fillData Hash : "+hashMap+viewType);
            isFilled = true;
            if(hashMap.get("GROUP_NO")!=null){
              observable.setTxtSchemeName(CommonUtil.convertObjToStr(hashMap.get("GROUP_NO")));  
            }
            if(hashMap.get("GDS_NO")!=null){
              observable.setGds_no(CommonUtil.convertObjToStr(hashMap.get("GDS_NO")));  
            }
            if(viewType ==  "Authorize"){
                if(hashMap.containsKey("DIVISION_NO") && hashMap.get("DIVISION_NO") != null){
                    hashMap.put("DIVISION_NO",CommonUtil.convertObjToInt(hashMap.get("DIVISION_NO")));
                }
                List gdsApplicationList = ClientUtil.executeQuery("getGDSApplicationForGDSNo", hashMap);
                HashMap gdsApplicationMap=(HashMap)gdsApplicationList.get(0);
            
                hashMap.put("GROUP_NO",gdsApplicationMap.get("GROUP_NO"));
                observable.setTxtSchemeName(CommonUtil.convertObjToStr(gdsApplicationMap.get("GROUP_NO")));
            
            }
            
           
            isWeeklyOrMonthlyFrequency();
            hashMap.put("GROUP_NO",hashMap.get("GROUP_NO"));
            hashMap.put("GDS_NO",hashMap.get("GDS_NO"));
            //observable.setGds_no(CommonUtil.convertObjToStr(hashMap.get("GDS_NO")));
           
            
            if(viewType ==  "SCHEME_NAME") { 
            HashMap groupMap = new HashMap();
            groupMap.put("GROUP_NO",hashMap.get("GROUP_NO"));
            groupMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            List bankAdvanceList = ClientUtil.executeQuery("checkGDSAuctionEntryAllowed", groupMap);
            if(bankAdvanceList != null && bankAdvanceList.size() > 0){
                HashMap advMap = (HashMap)bankAdvanceList.get(0);
                if(advMap.containsKey(("AUCTION_REQUIRED")) && advMap.get("AUCTION_REQUIRED")!= null &&CommonUtil.convertObjToStr(advMap.get("AUCTION_REQUIRED")).equalsIgnoreCase("N")){
                   ClientUtil.displayAlert("Auction entry not allowed \nCheck Bank advance done for the previous installment.");
                   return;
                }
            }
            List commonSchemeDetailsLst = ClientUtil.executeQuery("getDetailsForOneSchemeInGroup",groupMap);
            HashMap commonSchemeMap = (HashMap)commonSchemeDetailsLst.get(0);
            String commonGroup = CommonUtil.convertObjToStr(commonSchemeMap.get("SCHEME_NAME"));
            observable.setCommonScheme(commonGroup);
            HashMap specialSchemeMap = new HashMap();    
            specialSchemeMap.put("PROD_ID", CommonUtil.convertObjToStr(hashMap.get("PROD_ID")));
            specialSchemeMap.put("SCHEME_NAME", commonGroup);
            List specialList = ClientUtil.executeQuery("getSelectMDSProductSchemeTO", specialSchemeMap);
            if(specialList != null && specialList.size() > 0){
            com.see.truetransact.transferobject.product.mds.MDSProductSchemeTO mdsProductSchemeTO = (com.see.truetransact.transferobject.product.mds.MDSProductSchemeTO)specialList.get(0);
            if(mdsProductSchemeTO.getIsSpecialScheme() != null && CommonUtil.convertObjToStr(mdsProductSchemeTO.getIsSpecialScheme()).equals("Y")){
                isSpecialScheme = CommonUtil.convertObjToStr(mdsProductSchemeTO.getIsSpecialScheme());
            }else{
              isSpecialScheme = "";  
            }
            }
            }
            if(hashMap.containsKey("NEW_FROM_AUTHORIZE_LIST_UI")){
                fromNewAuthorizeUI = true;
                newauthorizeListUI = (NewAuthorizeListUI) hashMap.get("PARENT");
                hashMap.remove("PARENT");
                viewType = AUTHORIZE;
                observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
                observable.setStatus();
                transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
                btnReject.setEnabled(false);
                rejectFlag=1;
            }
            if(hashMap.containsKey("FROM_AUTHORIZE_LIST_UI")){
                fromAuthorizeUI = true;
                authorizeListUI = (AuthorizeListUI) hashMap.get("PARENT");
                hashMap.remove("PARENT");
                viewType = AUTHORIZE;
                observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
                observable.setStatus();
                transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
                btnReject.setEnabled(false);
                rejectFlag=1;
            }
            if(viewType ==  "SCHEME_NAME") {
                productMap = hashMap;
               // observable.setTxtSchemeName(CommonUtil.convertObjToStr(hashMap.get("GROUP_NO")));
                txtSchemeName.setText(CommonUtil.convertObjToStr(hashMap.get("GROUP_NO")));
                txtSchemeName.setEnabled(false);
                txtChittalNo.setEnabled(true);
                if(hashMap.get("MULTIPLE_MEMBER").equals("Y") && !hashMap.get("MULTIPLE_MEMBER").equals("") ){
                    observable.setMultipleMember("Y");
                    txtDivisionNo.setText("1");
                    observable.setTxtDivisionNo("1");
                    txtDivisionNo.setEnabled(false);
                }else{
                    observable.setMultipleMember("N");
                    txtDivisionNo.setEnabled(true);
                }
                boolean flag = observable.calculationDate();
                if(flag == true){
                    btnCancelActionPerformed(null);
                }else{
                    tdtDrawOrAuctionDt.setDateValue(observable.getTdtDrawOrAuctionDt());
                    tdtNextInstallmentDt1.setDateValue(observable.getTdtPayementDate());
                    tdtNextInstallmentDt.setDateValue(observable.getTdtNextInstallmentDt());
                    txtDivisionNo.setText(String.valueOf("1"));
                    observable.setTxtDivisionNo(txtDivisionNo.getText());
                    System.out.println("### observable.getTxtInstallmentNo() : "+observable.getTxtInstallmentNo());
                    txtInstallmentNo.setText(observable.getTxtInstallmentNo());
                    if(tdtDrawOrAuctionDt.getDateValue().length()>0)
                    if(DateUtil.dateDiff((Date) currDt.clone(),DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtDrawOrAuctionDt.getDateValue())))>0){
                            ClientUtil.showAlertWindow("Auction date should be equal to or lesser than "+DateUtil.getStringDate(currDt));
                    }
                    //Default Division No setting...
                    HashMap schemeMap = new HashMap();
                    HashMap prizedMap = new HashMap();
                    hashMap.put("SCHEME_NAME",observable.getCommonScheme());
                    List schemeLst = ClientUtil.executeQuery("getMDSSchemeDetails", hashMap);
                    if(schemeLst!=null && schemeLst.size()>0){
                        schemeMap = (HashMap)schemeLst.get(0);
                        if(CommonUtil.convertObjToInt(schemeMap.get("NO_OF_DIVISIONS"))>1){
                            prizedMap.put("SCHEME_NAME",txtSchemeName.getText());
                            prizedMap.put("CURR_DT",currDt.clone());
                            List PrizedLst = ClientUtil.executeQuery("getMaxDivisionNoFromPrizedDetailsTable", prizedMap);
                            if(PrizedLst!=null && PrizedLst.size()>0){
                                prizedMap = (HashMap)PrizedLst.get(0);
                                if(CommonUtil.convertObjToInt(prizedMap.get("DIVISION_NO"))>=1 && 
                                (CommonUtil.convertObjToInt(schemeMap.get("NO_OF_DIVISIONS"))>CommonUtil.convertObjToInt(prizedMap.get("DIVISION_NO")))){
                                    txtDivisionNo.setText(String.valueOf(CommonUtil.convertObjToInt(prizedMap.get("DIVISION_NO"))+1));
                                    observable.setTxtDivisionNo(txtDivisionNo.getText());
                                }
                            }
                        }
                    txtDivisionNoFocusLost(null);
                    }
                    if (schemeMap != null && schemeMap.containsKey("NO_OF_INSTALLMENTS") && schemeMap.get("NO_OF_INSTALLMENTS") != null) {
                        int noOfInst = CommonUtil.convertObjToInt(schemeMap.get("NO_OF_INSTALLMENTS"));
                        isInstallmetNoExceedCurrntInstNo(noOfInst);
                    }
                }
            }else if(viewType ==  "CHIT_NO") {
                //Added by sreekrishnan
                if(hashMap.containsKey("THALAYAL") && hashMap.get("THALAYAL")!=null && !hashMap.get("THALAYAL").equals("")
                        && hashMap.get("THALAYAL").equals("Y")){
                    thalayal = true;
                }else{
                    thalayal = false;
                }
                observable.setTxtChittalNo(CommonUtil.convertObjToStr(hashMap.get("CHITTAL_NO")));
                txtSubNo.setText(CommonUtil.convertObjToStr(hashMap.get("SUB_NO")));
                observable.setTxtSubNo(CommonUtil.convertObjToStr(hashMap.get("SUB_NO")));
                observable.setReceiptDetails(hashMap);
                txtInstallmentsPaid.setText(observable.getTxtInstallmentsPaid());
                txtInstallmentsAmountPaid.setText(observable.getTxtInstallmentsAmountPaid());
                txtInstallmentsDue.setText(observable.getTxtInstallmentsDue());
                txtInstallmentOverDueAmount.setText(observable.getTxtInstallmentOverDueAmount());
                observable.getCustomerAddressDetails();
                txtApplicationNo.setText(CommonUtil.convertObjToStr(hashMap.get("APPLN_NO")));
                observable.setTxtChittalNo(CommonUtil.convertObjToStr(hashMap.get("GDS_NO")));
                txtChittalNo.setText(CommonUtil.convertObjToStr(hashMap.get("GDS_NO")));
                lblMembType.setText(CommonUtil.convertObjToStr(hashMap.get("MEMBER_TYPE")));
                lblMemberName.setText(CommonUtil.convertObjToStr(hashMap.get("MEMBER_NAME")));
                observable.setLblMemberName(CommonUtil.convertObjToStr(hashMap.get("MEMBER_NAME")));
                observable.setTxtDivisionNo(txtDivisionNo.getText());
                lblValStreet.setText(observable.getLblHouseStNo());
                lblValArea.setText(observable.getLblArea());
                lblValCity.setText(observable.getLblCity());
                lblValState.setText(observable.getLblState());
                lblValPin.setText(observable.getLblpin());
                if(txtPrizedAmount.getText().length()>0 && observable.getMultipleMember().equals("Y")){
                    txtPrizedAmountFocusLost(null);
                }
            }
            
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW){
                this.setButtonEnableDisable();
                //Added by sreekrishnan for 0010573                
                if(hashMap.containsKey("PREDEFINITION_INSTALLMENT") && hashMap.get("PREDEFINITION_INSTALLMENT").equals("Y")){  
                    observable.setPredefinedInstall(CommonUtil.convertObjToStr(hashMap.get("PREDEFINITION_INSTALLMENT")));
                    System.out.println("$@$@$@$@$@$@$@$@$@$"+observable.getPredefinedInstall());
                    HashMap dataMap = new HashMap();
                    dataMap.put("DRAW_AUCTION_DATE",hashMap.get("DRAW_AUCTION_DATE"));
                    dataMap.put("DIVISION_NO",hashMap.get("DIVISION_NO"));
                    dataMap.put("PREDEFINITION_INSTALLMENT",hashMap.get("PREDEFINITION_INSTALLMENT"));
                    dataMap.put("SCHEME_NAME",CommonUtil.convertObjToStr(hashMap.get("SCHEME_NAME")));
                    observable.getData(dataMap);
                }else{ 
                    observable.getData(hashMap);
                }
                update();
                hashMap.put("SUB_NO", CommonUtil.convertObjToInt(hashMap.get("SUB_NO")));
                List lst=ClientUtil.executeQuery("getGDSMemberAddressDetails", hashMap);
                if(lst!=null && lst.size()>0){
                    addressMap = (HashMap) lst.get(0);
                    populateAddressData(addressMap);
                }
                setDivisionNo(CommonUtil.convertObjToStr(hashMap.get("DIVISION_NO")));
                if(hashMap.containsKey("NO_OF_DIVISIONS")){
                    observable.setNoOfDivision(CommonUtil.convertObjToStr(hashMap.get("NO_OF_DIVISIONS")));
                }
            }
            else if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT){
                this.setButtonEnableDisable();
                //observable.setTxtChittalNo(CommonUtil.convertObjToStr(hashMap.get("CHITTAL_NO")));
               // observable.setTxtSchemeName(CommonUtil.convertObjToStr(hashMap.get("SCHEME_NAME")));
                observable.setTransId(CommonUtil.convertObjToStr(hashMap.get("BATCH_ID")));
                
                if(hashMap.containsKey("DIVISION_NO") && hashMap.get("DIVISION_NO") != null){
                    hashMap.put("DIVISION_NO",CommonUtil.convertObjToInt(hashMap.get("DIVISION_NO")));
                }
                List groupNo=ClientUtil.executeQuery("getGDSApplicationForGDSNo", hashMap);
                
                
                hashMap.put("AUTHORIZED_STATUS", "AUTHORIZED_STATUS");
                observable.getData(hashMap);
                hashMap.put("CHITTAL_NO",observable.getTxtChittalNo());
                if(groupNo!=null && groupNo.size()>0){
                    HashMap groupNoMap = (HashMap) groupNo.get(0);
                observable.setTxtSchemeName(CommonUtil.convertObjToStr(groupNoMap.get("GROUP_NO")));
                }
                observable.setTxtChittalNo(CommonUtil.convertObjToStr(hashMap.get("GDS_NO")));
                update();
                hashMap.put("SUB_NO", CommonUtil.convertObjToInt(hashMap.get("SUB_NO")));
                List lst=ClientUtil.executeQuery("getGDSMemberAddressDetails", hashMap);
                if(lst!=null && lst.size()>0){
                    addressMap = (HashMap) lst.get(0);
                    populateAddressData(addressMap);
                }
            }
            if(viewType ==  AUTHORIZE) {
                btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                txtDivisionNo.setEnabled(false);
                btnAuthorize.setEnabled(true);
                btnAuthorize.requestFocusInWindow();
                if(observable.getChkUserDefined() != null && observable.getChkUserDefined().equals("Y")){
                ClientUtil.enableDisable(panInsidePrizedMoneyDetails, false);
                }
            }
            if(hashMap.containsKey("FROM_AUTHORIZE_LIST_UI")){
                btnSave.setEnabled(false);
                btnCancel.setEnabled(true);
                btnAuthorize.setEnabled(true);
                btnReject.setEnabled(true);
            }
            hashMap = null;
            btnCancel.setEnabled(true);
        }catch(Exception e){
            e.printStackTrace();
        }
        
//        txtDivisionNo.setEnabled(true);
        //__ To Save the data in the Internal Frame...
        setModified(true);
         if(rejectFlag==1){
           btnReject.setEnabled(false);
       }
    }
    
        public void  displayTransDetailNew(HashMap returnMap){
         String cashDisplayStr = "Cash Transaction Details...\n";
        String transferDisplayStr = "Transfer Transaction Details...\n";
        String displayStr = "";
        String transId = "";
        String transType = "";
      //  System.out.println("jhj>>>>>>>");
        Object keys[] = returnMap.keySet().toArray();
        System.out.println("jhj>>>>>>>adad");
        //System.out.println("keeeeeeeeeeeyyy>>>>>>>>>>>"+keys[]);
        int cashCount = 0;
        int transferCount = 0;
        List tempList = null;
        HashMap transMap = null;
        String actNum = "";
        HashMap transIdMap = new HashMap();
        HashMap transTypeMap = new HashMap();
       // System.out.println("keeeeeeeeeeeyyy>>>>>>>>>>>" + keys.length);
        for (int i = 0; i < keys.length; i++) {
            System.out.println("jhj>>>>>>>adad1211222@@@@" + (returnMap.get(keys[i]) instanceof String));
            if (returnMap.get(keys[i]) instanceof String) {
          //      System.out.println("hdfdasd");
                continue;
            }

          //  System.out.println("hdfdasd@@@@@");
            tempList = (List) returnMap.get(keys[i]);
          //  System.out.println("hdfdasd@@@@@>>>>>" + tempList);
            if (CommonUtil.convertObjToStr(keys[i]).indexOf("CASH") != -1) {
              //  System.out.println("haaaiii11....>>>");
                for (int j = 0; j < tempList.size(); j++) {
                  //  System.out.println("haaaiii11....>>>aa");
                    transMap = (HashMap) tempList.get(j);
                    if (j == 0) {
                       // System.out.println("haaaiii11....>>>bb");
                        transId = (String) transMap.get("SINGLE_TRANS_ID");
                    }
                    cashDisplayStr += "Trans Id : " + transMap.get("TRANS_ID")
                            + "   Trans Type : " + transMap.get("TRANS_TYPE");
                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if (actNum != null && !actNum.equals("")) {
                      //  System.out.println("haaaiii11....>>>cc");
                        cashDisplayStr += "   Account No : " + transMap.get("ACT_NUM")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    } else {
                      //  System.out.println("haaaiii11....>>>dd");
                        cashDisplayStr += "   Ac Hd Desc : " + transMap.get("AC_HD_ID")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                    }
                    transTypeMap.put(transMap.get("SINGLE_TRANS_ID"), transMap.get("TRANS_TYPE"));
                    transIdMap.put(transMap.get("SINGLE_TRANS_ID"), "CASH");
                }
                cashCount++;
            } else if (CommonUtil.convertObjToStr(keys[i]).indexOf("TRANSFER") != -1) {
                //System.out.println("haaaiii22....>>>");
                for (int j = 0; j < tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j == 0) {
                      //  System.out.println("haaaiii22....>>>aa");
                        transId = (String) transMap.get("SINGLE_TRANS_ID");
                    }
                    transIdMap.put(transMap.get("SINGLE_TRANS_ID"), "TRANSFER");
                    break;
//                    transferDisplayStr += "Trans Id : " + transMap.get("TRANS_ID")
//                            + "   Batch Id : " + transMap.get("BATCH_ID")
//                            + "   Trans Type : " + transMap.get("TRANS_TYPE");
//                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
//                    if (actNum != null && !actNum.equals("")) {
//                       // System.out.println("haaaiii22....>>>bb");
//                        transferDisplayStr += "   Account No : " + transMap.get("ACT_NUM")
//                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
//                    } else {
//                      //  System.out.println("haaaiii22....>>>cc");
//                        transferDisplayStr += "   Ac Hd Desc : " + transMap.get("AC_HD_ID")
//                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
//                    }                    
                }
                transferDisplayStr += "   Batch Id : " + transMap.get("BATCH_ID");
                transferCount++;
            }
        }
        if (cashCount > 0) {
            displayStr += cashDisplayStr;
        }
        if (transferCount > 0) {
            displayStr += transferDisplayStr;
        }
        if (!displayStr.equals("")) {
            ClientUtil.showMessageWindow("" + displayStr);
        }
        int yesNo = 0;
        String[] options = {"Yes", "No"};
        yesNo = COptionPane.showOptionDialog(null,"Do you want to print?", CommonConstants.WARNINGTITLE,
        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
        null, options, options[0]);
        System.out.println("#$#$$ yesNo : "+yesNo);
        if (yesNo==0) {
            TTIntegration ttIntgration = null;
            HashMap paramMap = new HashMap();
            //paramMap.put("TransId", transId);
            paramMap.put("TransDt", currDt);
            paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
            Object keys1[] = transIdMap.keySet().toArray();
             for (int i = 0; i < keys.length; i++) {
                            paramMap.put("TransId", keys1[i]);
                            ttIntgration.setParam(paramMap);
                            //                        if (((String)TrueTransactMain.BANKINFO.get("BANK_NAME")).toUpperCase().lastIndexOf("POLPULLY")!=-1) {
                            if (CommonUtil.convertObjToStr(transIdMap.get(keys1[i])).equals("TRANSFER")) {
                                ttIntgration.integrationForPrint("ReceiptPayment",false);
                            } 
                            else if (CommonUtil.convertObjToStr(transTypeMap.get(keys1[i])).equals("DEBIT")) {
                                    ttIntgration.integrationForPrint("CashPayment", false);
                            }else {
                                ttIntgration.integrationForPrint("CashReceipt", false);
                            }
                        }     
        }
        
    }
        
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void populateAddressData(HashMap addressMap){
        lblValStreet.setText(CommonUtil.convertObjToStr(addressMap.get("HOUSE_ST")));
        lblValArea.setText(CommonUtil.convertObjToStr(addressMap.get("AREA")));
        lblValCity.setText(CommonUtil.convertObjToStr(addressMap.get("CITY")));
        lblValState.setText(CommonUtil.convertObjToStr(addressMap.get("STATE")));
        lblValPin.setText(CommonUtil.convertObjToStr(addressMap.get("PIN")));
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
    
    public void enableDisableButtons(boolean flag){
        btnSchemeName.setEnabled(flag);
        btnChittalNo.setEnabled(flag);
    }
    
    public void authorizeStatus(String authorizeStatus) {
    }
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // Add your handling code here:
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // Add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // Add your handling code here:
        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // Add your handling code here:
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // Add your handling code here:
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnCloseActionPerformed(evt);
        //        this.dispose();
    }//GEN-LAST:event_mitCloseActionPerformed

    private void txtNextInstallmentAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNextInstallmentAmountFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNextInstallmentAmountFocusLost

    private void chkUserDefinedItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chkUserDefinedItemStateChanged
        // TODO add your handling code here:
        if (chkUserDefined.isSelected()) {
            userDefined = "Y";
            ClientUtil.enableDisable(panInsidePrizedMoneyDetails, true);
        } else {
            userDefined = "N";
            enableFields(false);
        }
    }//GEN-LAST:event_chkUserDefinedItemStateChanged

private void chkTransactionItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chkTransactionItemStateChanged
// TODO add your handling code here:
}//GEN-LAST:event_chkTransactionItemStateChanged

private void chkNoAuctionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkNoAuctionActionPerformed
// TODO add your handling code here:
    if(chkNoAuction.isSelected() == true){
        chkDraw.setEnabled(false);
        chkAuction.setEnabled(false);
    }else{
        chkDraw.setEnabled(true);
        chkAuction.setEnabled(true);
    }
}//GEN-LAST:event_chkNoAuctionActionPerformed

    private void txtSchemeNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSchemeNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSchemeNameActionPerformed
    
    
    
    
    
    
    private void displayAlert(String message){
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    
    
    
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnChittalNo;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnSchemeName;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CLabel cLabel2;
    private com.see.truetransact.uicomponent.CCheckBox chkAuction;
    private com.see.truetransact.uicomponent.CCheckBox chkDraw;
    private com.see.truetransact.uicomponent.CCheckBox chkNoAuction;
    private com.see.truetransact.uicomponent.CCheckBox chkTransaction;
    private com.see.truetransact.uicomponent.CCheckBox chkUserDefined;
    private com.see.truetransact.uicomponent.CLabel lblApplicationNo;
    private com.see.truetransact.uicomponent.CLabel lblArea;
    private com.see.truetransact.uicomponent.CLabel lblAuction;
    private com.see.truetransact.uicomponent.CLabel lblChittalNo;
    private com.see.truetransact.uicomponent.CLabel lblCity;
    private com.see.truetransact.uicomponent.CLabel lblCommisionAmount;
    private com.see.truetransact.uicomponent.CLabel lblDivisionNo;
    private com.see.truetransact.uicomponent.CLabel lblDraw;
    private com.see.truetransact.uicomponent.CLabel lblDrawOrAuctionDt;
    private com.see.truetransact.uicomponent.CLabel lblInstallmentNo;
    private com.see.truetransact.uicomponent.CLabel lblInstallmentOverDueAmount;
    private com.see.truetransact.uicomponent.CLabel lblInstallmentsAmountPaid;
    private com.see.truetransact.uicomponent.CLabel lblInstallmentsDue;
    private com.see.truetransact.uicomponent.CLabel lblInstallmentsPaid;
    private com.see.truetransact.uicomponent.CLabel lblMembType;
    private com.see.truetransact.uicomponent.CLabel lblMemberName;
    private com.see.truetransact.uicomponent.CLabel lblMemberType;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNetAmountPayable;
    private com.see.truetransact.uicomponent.CLabel lblNextBonusAmount;
    private com.see.truetransact.uicomponent.CLabel lblNextInstallmentAmount;
    private com.see.truetransact.uicomponent.CLabel lblNextInstallmentDt;
    private com.see.truetransact.uicomponent.CLabel lblNextInstallmentDt1;
    private com.see.truetransact.uicomponent.CLabel lblPin;
    private com.see.truetransact.uicomponent.CLabel lblPrizedAmount;
    private com.see.truetransact.uicomponent.CLabel lblPrizedMoneyType;
    private com.see.truetransact.uicomponent.CLabel lblSchemeName;
    private com.see.truetransact.uicomponent.CLabel lblSpace;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace6;
    private com.see.truetransact.uicomponent.CLabel lblSpace73;
    private com.see.truetransact.uicomponent.CLabel lblSpace74;
    private com.see.truetransact.uicomponent.CLabel lblSpace75;
    private com.see.truetransact.uicomponent.CLabel lblSpace76;
    private com.see.truetransact.uicomponent.CLabel lblSpace77;
    private com.see.truetransact.uicomponent.CLabel lblSpace78;
    private com.see.truetransact.uicomponent.CLabel lblState;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblStreet;
    private com.see.truetransact.uicomponent.CLabel lblTotalBonusAmount;
    private com.see.truetransact.uicomponent.CLabel lblTotalDiscount;
    private com.see.truetransact.uicomponent.CLabel lblValArea;
    private com.see.truetransact.uicomponent.CLabel lblValCity;
    private com.see.truetransact.uicomponent.CLabel lblValPin;
    private com.see.truetransact.uicomponent.CLabel lblValState;
    private com.see.truetransact.uicomponent.CLabel lblValStreet;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitAuthorize;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitException;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitReject;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAddressDetails;
    private com.see.truetransact.uicomponent.CPanel panCustomerId4;
    private com.see.truetransact.uicomponent.CPanel panCustomerId5;
    private com.see.truetransact.uicomponent.CPanel panInsideMDSDetails;
    private com.see.truetransact.uicomponent.CPanel panInsideMemberDetails;
    private com.see.truetransact.uicomponent.CPanel panInsidePrizedMoneyDetails;
    private com.see.truetransact.uicomponent.CPanel panPrizedMoneyDetails;
    private com.see.truetransact.uicomponent.CPanel panPrizedMoneyType;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CButtonGroup rdgEFTProductGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgIsLapsedGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgPayableBranchGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgPrintServicesGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgSeriesGR;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private javax.swing.JSeparator sptException;
    private com.see.truetransact.uicomponent.CTabbedPane tabPrizedMoneyDetails;
    private javax.swing.JToolBar tbrAdvances;
    private com.see.truetransact.uicomponent.CDateField tdtDrawOrAuctionDt;
    private com.see.truetransact.uicomponent.CDateField tdtNextInstallmentDt;
    private com.see.truetransact.uicomponent.CDateField tdtNextInstallmentDt1;
    private com.see.truetransact.uicomponent.CTextField txtApplicationNo;
    private com.see.truetransact.uicomponent.CTextField txtChittalNo;
    private com.see.truetransact.uicomponent.CTextField txtCommisionAmount;
    private com.see.truetransact.uicomponent.CTextField txtDivisionNo;
    private com.see.truetransact.uicomponent.CTextField txtInstallmentNo;
    private com.see.truetransact.uicomponent.CTextField txtInstallmentOverDueAmount;
    private com.see.truetransact.uicomponent.CTextField txtInstallmentsAmountPaid;
    private com.see.truetransact.uicomponent.CTextField txtInstallmentsDue;
    private com.see.truetransact.uicomponent.CTextField txtInstallmentsPaid;
    private com.see.truetransact.uicomponent.CTextField txtNetAmountPayable;
    private com.see.truetransact.uicomponent.CTextField txtNextBonusAmount;
    private com.see.truetransact.uicomponent.CTextField txtNextInstallmentAmount;
    private com.see.truetransact.uicomponent.CTextField txtPrizedAmount;
    private com.see.truetransact.uicomponent.CTextField txtSchemeName;
    private com.see.truetransact.uicomponent.CTextField txtSubNo;
    private com.see.truetransact.uicomponent.CTextField txtTotalBonusAmount;
    private com.see.truetransact.uicomponent.CTextField txtTotalDiscount;
    // End of variables declaration//GEN-END:variables
    public static void main(String[] arg){
        try {
            javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Throwable th) {
            th.printStackTrace();
        }
        javax.swing.JFrame jf = new javax.swing.JFrame();
        GDSPrizedMoneyDetailsEntryUI gui = new GDSPrizedMoneyDetailsEntryUI();
        jf.getContentPane().add(gui);
        jf.setSize(536, 566);
        jf.show();
        gui.show();
    }
    
    /**
     * Getter for property divisionNo.
     * @return Value of property divisionNo.
     */
    public java.lang.String getDivisionNo() {
        return divisionNo;
    }
    
    /**
     * Setter for property divisionNo.
     * @param divisionNo New value of property divisionNo.
     */
    public void setDivisionNo(java.lang.String divisionNo) {
        this.divisionNo = divisionNo;
    }
    
}