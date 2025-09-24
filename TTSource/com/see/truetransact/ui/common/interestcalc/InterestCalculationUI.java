/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * InterestCalculationUI.java
 *
 * Created on March 22, 2004, 4:38 PM
 */

package com.see.truetransact.ui.common.interestcalc;
import com.see.truetransact.commonutil.InterestCalc;
import com.see.truetransact.clientutil.*;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.ToDateValidation;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.commonutil.CommonUtil;

import java.util.Date;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import com.see.truetransact.uivalidation.CurrencyValidation;

/**
 *
 * @author  Pinky
 */
public class InterestCalculationUI extends CInternalFrame implements
UIMandatoryField,Observer {
    
    private InterestCalculationRB resourceBundle;
    private HashMap mandatoryMap;
    private InterestCalculationMRB objMandatoryRB;
    private InterestCalculationOB observable;
    private String viewType = "";
    private EnhancedTableModel tbmResult;
    private Date matDt = null;
    private String behavesLike = null;
    private long period = 0;
    private Date currDt = null;
    /** Creates new form BeanForm */
    public InterestCalculationUI() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        initSetup();
    }
    /** Initial set up */
    private void initSetup(){
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setObservable();
        setHelpMessage();
        setMaxLength();
        ClientUtil.enableDisable(this, false);
        setButtonEnableDisable();
        initComponentData();
        disableComps();
    }
    
    private void disableComps() {
        txtToAccount.setVisible(false);
        lblAccountTo.setVisible(false);
        btnToAccount.setVisible(false);
        panPeriodResult.setVisible(false);
        panRateOfInterest.setVisible(false);
        panMain1.setVisible(false);
        txtTotalAmount.setEnabled(false);
        txtTotalInterest.setEnabled(false);
        lblDuration.setVisible(false);
        btnEdit.setVisible(false);
        btnDelete.setVisible(false);
        btnSave.setVisible(false);
        btnDepSubNoAccNew1.setEnabled(false);
        btnDepSubNoAccNew.setEnabled(false);        
    }
    
    /** Set observable */
    private void setObservable() {
        observable = InterestCalculationOB.getInstance();
        observable.addObserver(this);
    }
    private void setMaxLength() {
        //txtPrincipal.setMaxLength(10);
//        txtPrincipal.setValidation(new NumericValidation(14,2));
        txtPrincipal.setValidation(new CurrencyValidation());
        
        txtDays.setMaxLength(4);
        txtDays.setValidation(new NumericValidation());
        //txtMonths.setMaxLength(2);
        txtYears.setMaxLength(2);
        txtMonths.setMaxLength(3);
        txtMonths.setValidation(new NumericValidation());
        txtYears.setValidation(new NumericValidation());
        txtTotalInterest.setValidation(new CurrencyValidation());
        txtTotalAmount.setValidation(new CurrencyValidation());
        txtRateofInterest.setValidation(new NumericValidation(3,3));        
    }
   /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnInterestCreditHead.setName("btnInterestCreditHead");
        btnInterestDebitHead.setName("btnInterestDebitHead");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnSave.setName("btnSave");
        cboAccountType.setName("cboAccountType");
//        cboDepositsAccountType.setName("cboDepositsAccountType");
        
        cboCompounded.setName("cboCompounded");
        cboDepositsCompounded.setName("cboDepositsCompounded");
        
        cboFloatPrecision.setName("cboFloatPrecision");
        cboGracePeriod.setName("cboGracePeriod");
        cboMonth.setName("cboMonth");
        cboProductID.setName("cboProductID");
        cboRoundingInterest.setName("cboRoundingInterest");
        cboRoundingPrincipal.setName("cboRoundingPrincipal");
        cboYear.setName("cboYear");
        lblAccountFrom.setName("lblAccountFrom");
        lblAccountTo.setName("lblAccountTo");
        lblAccountType.setName("lblAccountType");
        //added DepositInterestInstallment
        lblDepositsInterest.setName("lblDepositInterest");
        lblDepositsInstallment.setName("lblDepositInstallment");
        
        lblCompounded.setName("lblCompounded");
        lblDays.setName("lblDays");
        lblDuration.setName("lblDuration");
        lblFloatPrecision.setName("lblFloatPrecision");
        lblFromDate.setName("lblFromDate");
        lblGracePeriod.setName("lblGracePeriod");
        lblInterestCreditHead.setName("lblInterestCreditHead");
        lblInterestDebitHead.setName("lblInterestDebitHead");
        lblInterestOption.setName("lblInterestOption");
        lblMonth.setName("lblMonth");
        lblMonths.setName("lblMonths");
        lblMsg.setName("lblMsg");
        lblPenalRate.setName("lblPenalRate");
        lblPenalRatePercentage.setName("lblPenalRatePercentage");
        lblPercentageOfRate.setName("lblPercentageOfRate");
        lblPeriodOpt.setName("lblPeriodOpt");
        lblPrincipal.setName("lblPrincipal");
        lblProductID.setName("lblProductID");
        lblRateofInterest.setName("lblRateofInterest");
        //lblReport.setName("lblReport");
        lblRoundingInterest.setName("lblRoundingInterest");
        lblRoundingPrincipal.setName("lblRoundingPrincipal");
        lblSpace.setName("lblSpace");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblStatus.setName("lblStatus");
        lblToDate.setName("lblToDate");
        lblTotalInterest.setName("lblTotalInterest");
        lblYear.setName("lblYear");
        lblYears.setName("lblYears");
        mbrMain1.setName("mbrMain1");
        panCreditHead.setName("panCreditHead");
        panDebitHead.setName("panDebitHead");
//        panDuration.setName("panDuration");
        panGracePeriod.setName("panGracePeriod");
        panInterest.setName("panInterest");
        panInterestAndPeriod.setName("panInterestAndPeriod");
        panInterestCalculation.setName("panInterestCalculation");
        panInterestHead.setName("panInterestHead");
        panInterestOption.setName("panInterestOption");
        panMain0.setName("panMain0");
        panMain1.setName("panMain1");
        panPenalRate.setName("panPenalRate");
        panPeriod.setName("panPeriod");
//        panPeriodOption.setName("panPeriodOption");
        panRateOfInterest.setName("panRateOfInterest");
        //panReport.setName("panReport");
        panResult.setName("panResult");
        panRounding.setName("panRounding");
        panStatus.setName("panStatus");
        rdoInterestOption_Compound.setName("rdoInterestOption_Compound");
        rdoInterestOption_Fixed.setName("rdoInterestOption_Fixed");
        rdoInterestOption_Floating.setName("rdoInterestOption_Floating");
        rdoInterestOption_Simple.setName("rdoInterestOption_Simple");
        rdoPeriodOption_Date.setName("rdoPeriodOption_Date");
        rdoPeriodOption_Duration.setName("rdoPeriodOption_Duration");
        // rdoReport_Details.setName("rdoReport_Details");
        //rdoReport_Summary.setName("rdoReport_Summary");
//        sptInterestOption.setName("sptInterestOption");
        sptReport.setName("sptReport");
        srpResult.setName("srpResult");
        tblResult.setName("tblResult");
        tdtFromDate.setName("tdtFromDate");
        tdtToDate.setName("tdtToDate");
        txtDays.setName("txtDays");
        txtGracePeriod.setName("txtGracePeriod");
        txtInterestCreditHead.setName("txtInterestCreditHead");
        txtInterestDebitHead.setName("txtInterestDebitHead");
        txtMonths.setName("txtMonths");
        txtPenalRate.setName("txtPenalRate");
        txtPrincipal.setName("txtPrincipal");
        txtRateofInterest.setName("txtRateofInterest");
        txtTotalInterest.setName("txtTotalInterest");
        txtYears.setName("txtYears");
    }
 /* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        resourceBundle = new InterestCalculationRB();
        btnClose.setText(resourceBundle.getString("btnClose"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblTotalInterest.setText(resourceBundle.getString("lblTotalInterest"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        // rdoReport_Details.setText(resourceBundle.getString("rdoReport_Details"));
        lblAccountTo.setText(resourceBundle.getString("lblAccountTo"));
        rdoInterestOption_Fixed.setText(resourceBundle.getString("rdoInterestOption_Fixed"));
        lblAccountType.setText(resourceBundle.getString("lblAccountType"));
        //added DepositInterestInstallment
        lblDepositsInterest.setName("lblDepositsInterest");
        lblDepositsInstallment.setName("lblDepositsinstallment");
        
        lblSpace.setText(resourceBundle.getString("lblSpace"));
        lblPenalRate.setText(resourceBundle.getString("lblPenalRate"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblMonth.setText(resourceBundle.getString("lblMonth"));
        lblDuration.setText(resourceBundle.getString("lblDuration"));
        rdoInterestOption_Floating.setText(resourceBundle.getString("rdoInterestOption_Floating"));
        lblPercentageOfRate.setText(resourceBundle.getString("lblPercentageOfRate"));
        btnInterestDebitHead.setText(resourceBundle.getString("btnInterestDebitHead"));
        lblRoundingPrincipal.setText(resourceBundle.getString("lblRoundingPrincipal"));
        lblAccountFrom.setText(resourceBundle.getString("lblAccountFrom"));
        lblCompounded.setText(resourceBundle.getString("lblCompounded"));
        rdoPeriodOption_Date.setText(resourceBundle.getString("rdoPeriodOption_Date"));
        //rdoReport_Summary.setText(resourceBundle.getString("rdoReport_Summary"));
        lblFloatPrecision.setText(resourceBundle.getString("lblFloatPrecision"));
        lblRateofInterest.setText(resourceBundle.getString("lblRateofInterest"));
        rdoInterestOption_Simple.setText(resourceBundle.getString("rdoInterestOption_Simple"));
        lblFromDate.setText(resourceBundle.getString("lblFromDate"));
        lblInterestDebitHead.setText(resourceBundle.getString("lblInterestDebitHead"));
        lblMonths.setText(resourceBundle.getString("lblMonths"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        rdoPeriodOption_Duration.setText(resourceBundle.getString("rdoPeriodOption_Duration"));
        lblYears.setText(resourceBundle.getString("lblYears"));
        btnInterestCreditHead.setText(resourceBundle.getString("btnInterestCreditHead"));
        ((TitledBorder)panPeriod.getBorder()).setTitle(resourceBundle.getString("panPeriod"));
        lblRoundingInterest.setText(resourceBundle.getString("lblRoundingInterest"));
        lblProductID.setText(resourceBundle.getString("lblProductID"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblDays.setText(resourceBundle.getString("lblDays"));
        lblPenalRatePercentage.setText(resourceBundle.getString("lblPenalRatePercentage"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        lblToDate.setText(resourceBundle.getString("lblToDate"));
        ((TitledBorder)panRateOfInterest.getBorder()).setTitle(resourceBundle.getString("panRateOfInterest"));
        lblPeriodOpt.setText(resourceBundle.getString("lblPeriodOpt"));
        lblInterestCreditHead.setText(resourceBundle.getString("lblInterestCreditHead"));
        lblPrincipal.setText(resourceBundle.getString("lblPrincipal"));
        lblInterestOption.setText(resourceBundle.getString("lblInterestOption"));
        lblYear.setText(resourceBundle.getString("lblYear"));
        rdoInterestOption_Compound.setText(resourceBundle.getString("rdoInterestOption_Compound"));
        //  lblReport.setText(resourceBundle.getString("lblReport"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblGracePeriod.setText(resourceBundle.getString("lblGracePeriod"));
    }
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtPrincipal", new Boolean(true));
        mandatoryMap.put("rdoPeriodOption_Duration", new Boolean(true));
        mandatoryMap.put("tdtFromDate", new Boolean(true));
        mandatoryMap.put("tdtToDate", new Boolean(true));
        mandatoryMap.put("cboYear", new Boolean(true));
        mandatoryMap.put("cboMonth", new Boolean(true));
        mandatoryMap.put("txtGracePeriod", new Boolean(true));
        mandatoryMap.put("cboGracePeriod", new Boolean(true));
        mandatoryMap.put("txtDays", new Boolean(true));
        mandatoryMap.put("txtMonths", new Boolean(true));
        mandatoryMap.put("txtYears", new Boolean(true));
        mandatoryMap.put("rdoInterestOption_Simple", new Boolean(true));
        mandatoryMap.put("cboCompounded", new Boolean(true));
        //added Deposits
        mandatoryMap.put("cboDepositsCompounded", new Boolean(true));
        
        mandatoryMap.put("cboAccountType", new Boolean(true));
        mandatoryMap.put("txtRateofInterest", new Boolean(true));
        mandatoryMap.put("txtPenalRate", new Boolean(true));
        mandatoryMap.put("rdoReport_Summary", new Boolean(true));
        mandatoryMap.put("cboRoundingPrincipal", new Boolean(true));
        mandatoryMap.put("cboRoundingInterest", new Boolean(true));
        mandatoryMap.put("cboFloatPrecision", new Boolean(true));
        mandatoryMap.put("txtInterestCreditHead", new Boolean(true));
        mandatoryMap.put("txtInterestDebitHead", new Boolean(true));
    }
    
/* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
        /* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        
        txtPrincipal.setText(observable.getTxtPrincipal());
        rdoPeriodOption_Duration.setSelected(observable.getRdoPeriodOption_Duration());
        rdoPeriodOption_Date.setSelected(observable.getRdoPeriodOption_Date());
        tdtFromDate.setDateValue(observable.getTdtFromDate());
        tdtToDate.setDateValue(observable.getTdtToDate());
        cboYear.setSelectedItem(observable.getCboYear());
        cboMonth.setSelectedItem(observable.getCboMonth());
        txtGracePeriod.setText(observable.getTxtGracePeriod());
        cboGracePeriod.setSelectedItem(observable.getCboGracePeriod());
        txtDays.setText(observable.getTxtDays());
        txtMonths.setText(observable.getTxtMonths());
        txtYears.setText(observable.getTxtYears());
        rdoInterestOption_Simple.setSelected(observable.getRdoInterestOption_Simple());
        rdoInterestOption_Fixed.setSelected(observable.getRdoInterestOption_Fixed());
        rdoInterestOption_Compound.setSelected(observable.getRdoInterestOption_Compound());
        rdoInterestOption_Floating.setSelected(observable.getRdoInterestOption_Floating());
        //cboCompounded.setSelectedItem(observable.getCboCompounded());
        
        cboAccountType.setSelectedItem(observable.getCboAccountType());
        txtRateofInterest.setText(observable.getTxtRateofInterest());
        txtPenalRate.setText(observable.getTxtPenalRate());
        // rdoReport_Summary.setSelected(observable.getRdoReport_Summary());
        //rdoReport_Details.setSelected(observable.getRdoReport_Details());
        cboRoundingPrincipal.setSelectedItem(observable.getCboRoundingPrincipal());
        cboRoundingInterest.setSelectedItem(observable.getCboRoundingInterest());
        cboFloatPrecision.setSelectedItem(observable.getCboFloatPrecision());
        txtInterestCreditHead.setText(observable.getTxtInterestCreditHead());
        txtInterestDebitHead.setText(observable.getTxtInterestDebitHead());
        txtTotalInterest.setText(observable.getTxtTotalInterest());
        
        cboCompounded.setModel(observable.getCbmCompounded());
        //added Deposits
        cboDepositsCompounded.setModel(observable.getCbmDepositsCompounded());
        //cboProductID.setModel(observable.getCbmProductID());
        ((ComboBoxModel)cboProductID.getModel()).setKeyForSelected(observable.getCboProductID());
        ((ComboBoxModel)cboCategory.getModel()).setKeyForSelected(observable.getCboCategory());
        ((ComboBoxModel)cboInterestPaymentFrequency.getModel()).setKeyForSelected(observable.getCboInterestPaymentFrequency());
        txtFromAccount.setText(observable.getTxtFromAccount());
        cboRoundingPrincipal.setModel(observable.getCbmRoundingPrincipal());
        cboRoundingInterest.setModel(observable.getCbmRoundingInterest());
        cboPrincipalRoundingValue.setModel(observable.getCbmPrincipalRoundingValue());
        
        cboYear.setModel(observable.getCbmYear());
        cboMonth.setModel(observable.getCbmMonth());
        cboAccountType.setModel(observable.getCbmAccountType());
        cboFloatPrecision.setModel(observable.getCbmFloatPrecision());
        cboRoundingInterest.setModel(observable.getCbmRoundingInterest());
        cboGracePeriod.setModel(observable.getCbmGracePeriod());
        
        cboCompoundingType.setModel(observable.getCbmCompoundingType());
        
        txtTotalAmount.setText(observable.getTxtTotalAmount());
//        tblPeriodResult.revalidate();
//        tblPeriodResult.setModel(observable.getTbmPeriodAmount());
        
        txtFromAccount.setText(observable.getTxtFromAccount());
        
        cboRateOption.setModel(observable.getCbmRateOption());
        tblResult.setModel(observable.getTblDepSubNo());
        
    }
        /* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setTxtPrincipal(txtPrincipal.getText());
        observable.setRdoPeriodOption_Duration(rdoPeriodOption_Duration.isSelected());
        observable.setRdoPeriodOption_Date(rdoPeriodOption_Date.isSelected());
        observable.setTdtFromDate(tdtFromDate.getDateValue());
        observable.setTdtToDate(tdtToDate.getDateValue());
        observable.setCboYear((String) cboYear.getSelectedItem());
        observable.setCboMonth((String) cboMonth.getSelectedItem());
        observable.setTxtGracePeriod(txtGracePeriod.getText());
        observable.setCboGracePeriod((String) cboGracePeriod.getSelectedItem());
        observable.setTxtDays(txtDays.getText());
        observable.setTxtMonths(txtMonths.getText());
        observable.setTxtYears(txtYears.getText());
        observable.setRdoInterestOption_Simple(rdoInterestOption_Simple.isSelected());
        observable.setRdoInterestOption_Fixed(rdoInterestOption_Fixed.isSelected());
        observable.setRdoInterestOption_Compound(rdoInterestOption_Compound.isSelected());
        observable.setRdoInterestOption_Floating(rdoInterestOption_Floating.isSelected());
        //observable.setCboCompounded((String) cboCompounded.getSelectedItem());
        observable.setCboAccountType((String) cboAccountType.getSelectedItem());
        observable.setTxtRateofInterest(txtRateofInterest.getText());
        observable.setTxtPenalRate(txtPenalRate.getText());
        //  observable.setRdoReport_Summary(rdoReport_Summary.isSelected());
        //observable.setRdoReport_Details(rdoReport_Details.isSelected());
        observable.setCboRoundingPrincipal((String) cboRoundingPrincipal.getSelectedItem());
        observable.setCboRoundingInterest((String) cboRoundingInterest.getSelectedItem());
        observable.setCboFloatPrecision((String) cboFloatPrecision.getSelectedItem());
        observable.setTxtInterestCreditHead(txtInterestCreditHead.getText());
        observable.setTxtInterestDebitHead(txtInterestDebitHead.getText());
        observable.setTxtTotalAmount(txtTotalAmount.getText());
        if (cboCompounded.isEnabled())
            observable.setCbmCompounded((ComboBoxModel)cboCompounded.getModel());
        
        //added Deposits
//        observable.setCbmDepositsCompounded((ComboBoxModel)cboDepositsCompounded.getModel());
        observable.setCbmCategory((ComboBoxModel)cboCategory.getModel());
        observable.setCbmProductID((ComboBoxModel)cboProductID.getModel());
        observable.setCbmInterestPaymentFrequency((ComboBoxModel)cboInterestPaymentFrequency.getModel());
        observable.setCbmRoundingInterest((ComboBoxModel)cboRoundingInterest.getModel());
        observable.setCbmRoundingPrincipal((ComboBoxModel)cboRoundingPrincipal.getModel());
        
        observable.setCbmPrincipalRoundingValue((ComboBoxModel)cboPrincipalRoundingValue.getModel());
        
        observable.setCbmAccountType((ComboBoxModel)cboAccountType.getModel());
        observable.setCbmFloatPrecision((ComboBoxModel)cboFloatPrecision.getModel());
        observable.setCbmGracePeriod((ComboBoxModel)cboGracePeriod.getModel());
        observable.setCbmMonth((ComboBoxModel)cboMonth.getModel());
        observable.setCbmYear((ComboBoxModel)cboYear.getModel());
        if(cboCompoundingType.isEnabled())
            observable.setCbmCompoundingType((ComboBoxModel)cboCompoundingType.getModel());
        
//        observable.setTbmPeriodAmount((TableModel)tblPeriodResult.getModel());
        
        observable.setCboProductID((String)((ComboBoxModel)cboProductID.getModel()).getKeyForSelected());
        observable.setTxtFromAccount(txtFromAccount.getText());
        
        observable.setCbmRateOption((ComboBoxModel)cboRateOption.getModel());
        observable.setCboRateOption((String)((ComboBoxModel)cboRateOption.getModel()).getKeyForSelected());
        
    }
        /* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        objMandatoryRB = new InterestCalculationMRB() ;
        txtPrincipal.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPrincipal"));
        rdoPeriodOption_Duration.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoPeriodOption_Duration"));
        tdtFromDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtFromDate"));
        tdtToDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtToDate"));
        cboYear.setHelpMessage(lblMsg, objMandatoryRB.getString("cboYear"));
        cboMonth.setHelpMessage(lblMsg, objMandatoryRB.getString("cboMonth"));
        txtGracePeriod.setHelpMessage(lblMsg, objMandatoryRB.getString("txtGracePeriod"));
        cboGracePeriod.setHelpMessage(lblMsg, objMandatoryRB.getString("cboGracePeriod"));
        txtDays.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDays"));
        txtMonths.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMonths"));
        txtYears.setHelpMessage(lblMsg, objMandatoryRB.getString("txtYears"));
        rdoInterestOption_Simple.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoInterestOption_Simple"));
        cboCompounded.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCompounded"));
        //added deposits
        cboDepositsCompounded.setHelpMessage(lblMsg, objMandatoryRB.getString("cboDepositsCompounded"));
        cboAccountType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboAccountType"));
        txtRateofInterest.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRateofInterest"));
        txtPenalRate.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPenalRate"));
        //rdoReport_Summary.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoReport_Summary"));
        cboRoundingPrincipal.setHelpMessage(lblMsg, objMandatoryRB.getString("cboRoundingPrincipal"));
        cboRoundingInterest.setHelpMessage(lblMsg, objMandatoryRB.getString("cboRoundingInterest"));
        cboFloatPrecision.setHelpMessage(lblMsg, objMandatoryRB.getString("cboFloatPrecision"));
        txtInterestCreditHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInterestCreditHead"));
        txtInterestDebitHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInterestDebitHead"));
    }
    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        btnDelete.setEnabled(!btnDelete.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());
        
        btnSave.setEnabled(!btnSave.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        
        btnInterestCreditHead.setEnabled(!btnNew.isEnabled());
        btnInterestDebitHead.setEnabled(!btnNew.isEnabled());
        
        btnFromAccount.setEnabled(!btnNew.isEnabled());
        btnToAccount.setEnabled(!btnNew.isEnabled());
    }
    private void buttonEnableDisable() {
        tdtFromDate.setEnabled(false);
        tdtToDate.setEnabled(false);
        cboCompounded.setEnabled(false);
        cboAccountType.setEnabled(false);
        cboCompoundingType.setEnabled(false);
    }
    private void initComponentData() {
        cboCompounded.setModel(observable.getCbmCompounded());
        //added Deposits
        cboDepositsCompounded.setModel(observable.getCbmDepositsCompounded());
        cboProductID.setModel(observable.getCbmProductID());
        cboRoundingPrincipal.setModel(observable.getCbmRoundingPrincipal());
        cboPrincipalRoundingValue.setModel(observable.getCbmPrincipalRoundingValue());
        cboRoundingInterest.setModel(observable.getCbmRoundingInterest());
        
        cboYear.setModel(observable.getCbmYear());
        cboMonth.setModel(observable.getCbmMonth());
        cboAccountType.setModel(observable.getCbmAccountType());
        cboFloatPrecision.setModel(observable.getCbmFloatPrecision());
        cboRoundingInterest.setModel(observable.getCbmRoundingInterest());
        cboGracePeriod.setModel(observable.getCbmGracePeriod());
        cboCategory.setModel(observable.getCbmCategory());
        cboInterestPaymentFrequency.setModel(observable.getCbmInterestPaymentFrequency());
        cboCompoundingType.setModel(observable.getCbmCompoundingType());
//        tblPeriodResult.setModel(observable.getTbmPeriodAmount());
        
//        tbmResult = observable.getTbmResult();
//        tblResult.setModel(tbmResult);
        
        cboRateOption.setModel(observable.getCbmRateOption());
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoPeriodOption = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoInterestOption = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoReport = new com.see.truetransact.uicomponent.CButtonGroup();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        tbrAdvances = new javax.swing.JToolBar();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace24 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace25 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace26 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace27 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panMain0 = new com.see.truetransact.uicomponent.CPanel();
        panInterestCalculation = new com.see.truetransact.uicomponent.CPanel();
        panInterestAndPeriod = new com.see.truetransact.uicomponent.CPanel();
        panPeriod = new com.see.truetransact.uicomponent.CPanel();
        lblDuration = new com.see.truetransact.uicomponent.CLabel();
        txtPrincipal = new com.see.truetransact.uicomponent.CTextField();
        lblPrincipal = new com.see.truetransact.uicomponent.CLabel();
        txtRateofInterest = new com.see.truetransact.uicomponent.CTextField();
        lblRateofInterest = new com.see.truetransact.uicomponent.CLabel();
        cboProductID = new com.see.truetransact.uicomponent.CComboBox();
        lblProductID = new com.see.truetransact.uicomponent.CLabel();
        btnDepSubNoAccNew = new com.see.truetransact.uicomponent.CButton();
        lblTotalInterest = new com.see.truetransact.uicomponent.CLabel();
        txtTotalInterest = new com.see.truetransact.uicomponent.CTextField();
        txtTotalAmount = new com.see.truetransact.uicomponent.CTextField();
        lblTotalAmount = new com.see.truetransact.uicomponent.CLabel();
        lblDays = new com.see.truetransact.uicomponent.CLabel();
        lblMonths = new com.see.truetransact.uicomponent.CLabel();
        lblYears = new com.see.truetransact.uicomponent.CLabel();
        txtDays = new com.see.truetransact.uicomponent.CTextField();
        txtMonths = new com.see.truetransact.uicomponent.CTextField();
        txtYears = new com.see.truetransact.uicomponent.CTextField();
        panInterestOption1 = new com.see.truetransact.uicomponent.CPanel();
        rdoDiscounted_No = new com.see.truetransact.uicomponent.CRadioButton();
        rdoDiscounted_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        lblDuration1 = new com.see.truetransact.uicomponent.CLabel();
        cboInterestPaymentFrequency = new com.see.truetransact.uicomponent.CComboBox();
        lblRateofInterest1 = new com.see.truetransact.uicomponent.CLabel();
        lblCategory = new com.see.truetransact.uicomponent.CLabel();
        cboCategory = new com.see.truetransact.uicomponent.CComboBox();
        btnDepSubNoAccNew1 = new com.see.truetransact.uicomponent.CButton();
        panResult = new com.see.truetransact.uicomponent.CPanel();
        srpResult = new com.see.truetransact.uicomponent.CScrollPane();
        tblResult = new com.see.truetransact.uicomponent.CTable();
        sptOperatesLike = new com.see.truetransact.uicomponent.CSeparator();
        sptOperatesLike1 = new com.see.truetransact.uicomponent.CSeparator();
        panRateOfInterest = new com.see.truetransact.uicomponent.CPanel();
        lblInterestOption = new com.see.truetransact.uicomponent.CLabel();
        panInterestOption = new com.see.truetransact.uicomponent.CPanel();
        rdoInterestOption_Simple = new com.see.truetransact.uicomponent.CRadioButton();
        rdoInterestOption_Fixed = new com.see.truetransact.uicomponent.CRadioButton();
        rdoInterestOption_Compound = new com.see.truetransact.uicomponent.CRadioButton();
        rdoInterestOption_Floating = new com.see.truetransact.uicomponent.CRadioButton();
        lblCompounded = new com.see.truetransact.uicomponent.CLabel();
        panInterest = new com.see.truetransact.uicomponent.CPanel();
        lblPercentageOfRate = new com.see.truetransact.uicomponent.CLabel();
        cboRateOption = new com.see.truetransact.uicomponent.CComboBox();
        lblPenalRate = new com.see.truetransact.uicomponent.CLabel();
        panPenalRate = new com.see.truetransact.uicomponent.CPanel();
        txtPenalRate = new com.see.truetransact.uicomponent.CTextField();
        lblPenalRatePercentage = new com.see.truetransact.uicomponent.CLabel();
        panCompound = new com.see.truetransact.uicomponent.CPanel();
        cboCompoundingType = new com.see.truetransact.uicomponent.CComboBox();
        cboCompounded = new com.see.truetransact.uicomponent.CComboBox();
        lblDepositsInstallment = new com.see.truetransact.uicomponent.CLabel();
        cboAccountType = new com.see.truetransact.uicomponent.CComboBox();
        cboDepositsCompounded = new com.see.truetransact.uicomponent.CComboBox();
        lblAccountType = new com.see.truetransact.uicomponent.CLabel();
        lblDepositsInterest = new com.see.truetransact.uicomponent.CLabel();
        panAccount = new com.see.truetransact.uicomponent.CPanel();
        lblAccountTo = new com.see.truetransact.uicomponent.CLabel();
        panAccountFrom = new com.see.truetransact.uicomponent.CPanel();
        txtFromAccount = new com.see.truetransact.uicomponent.CTextField();
        btnFromAccount = new com.see.truetransact.uicomponent.CButton();
        panAccountTo = new com.see.truetransact.uicomponent.CPanel();
        txtToAccount = new com.see.truetransact.uicomponent.CTextField();
        btnToAccount = new com.see.truetransact.uicomponent.CButton();
        lblAccountFrom = new com.see.truetransact.uicomponent.CLabel();
        lblPeriodOpt = new com.see.truetransact.uicomponent.CLabel();
        txtGracePeriod = new com.see.truetransact.uicomponent.CTextField();
        rdoPeriodOption_Date = new com.see.truetransact.uicomponent.CRadioButton();
        cboGracePeriod = new com.see.truetransact.uicomponent.CComboBox();
        rdoPeriodOption_Duration = new com.see.truetransact.uicomponent.CRadioButton();
        lblGracePeriod = new com.see.truetransact.uicomponent.CLabel();
        lblFromDate = new com.see.truetransact.uicomponent.CLabel();
        tdtFromDate = new com.see.truetransact.uicomponent.CDateField();
        lblToDate = new com.see.truetransact.uicomponent.CLabel();
        tdtToDate = new com.see.truetransact.uicomponent.CDateField();
        cboYear = new com.see.truetransact.uicomponent.CComboBox();
        lblYear = new com.see.truetransact.uicomponent.CLabel();
        lblMonth = new com.see.truetransact.uicomponent.CLabel();
        cboMonth = new com.see.truetransact.uicomponent.CComboBox();
        panGracePeriod = new com.see.truetransact.uicomponent.CPanel();
        panMain1 = new com.see.truetransact.uicomponent.CPanel();
        panRounding = new com.see.truetransact.uicomponent.CPanel();
        lblRoundingPrincipal = new com.see.truetransact.uicomponent.CLabel();
        lblRoundingInterest = new com.see.truetransact.uicomponent.CLabel();
        cboRoundingInterest = new com.see.truetransact.uicomponent.CComboBox();
        lblFloatPrecision = new com.see.truetransact.uicomponent.CLabel();
        cboFloatPrecision = new com.see.truetransact.uicomponent.CComboBox();
        cboRoundingPrincipal = new com.see.truetransact.uicomponent.CComboBox();
        cboPrincipalRoundingValue = new com.see.truetransact.uicomponent.CComboBox();
        panInterestHead = new com.see.truetransact.uicomponent.CPanel();
        panCreditHead = new com.see.truetransact.uicomponent.CPanel();
        txtInterestCreditHead = new com.see.truetransact.uicomponent.CTextField();
        btnInterestCreditHead = new com.see.truetransact.uicomponent.CButton();
        panDebitHead = new com.see.truetransact.uicomponent.CPanel();
        txtInterestDebitHead = new com.see.truetransact.uicomponent.CTextField();
        btnInterestDebitHead = new com.see.truetransact.uicomponent.CButton();
        lblInterestCreditHead = new com.see.truetransact.uicomponent.CLabel();
        lblInterestDebitHead = new com.see.truetransact.uicomponent.CLabel();
        sptReport = new com.see.truetransact.uicomponent.CSeparator();
        panPeriodResult = new com.see.truetransact.uicomponent.CPanel();
        srpPeriodResult = new com.see.truetransact.uicomponent.CScrollPane();
        tblPeriodResult = new com.see.truetransact.uicomponent.CTable();
        mbrMain1 = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess1 = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptDelete = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Interest Calculation");
        setMinimumSize(new java.awt.Dimension(550, 550));
        setPreferredSize(new java.awt.Dimension(550, 550));

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace, gridBagConstraints);

        lblStatus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblStatus.setText("                      ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblStatus, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblMsg, gridBagConstraints);

        getContentPane().add(panStatus, java.awt.BorderLayout.SOUTH);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnNew);

        lblSpace24.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace24.setText("     ");
        lblSpace24.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace24);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnEdit);

        lblSpace25.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace25.setText("     ");
        lblSpace25.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace25.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace25.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace25);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnDelete);

        lblSpace2.setText("     ");
        tbrAdvances.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnSave);

        lblSpace26.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace26.setText("     ");
        lblSpace26.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace26);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnCancel);

        lblSpace3.setText("     ");
        tbrAdvances.add(lblSpace3);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnPrint);

        lblSpace27.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace27.setText("     ");
        lblSpace27.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace27);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnClose);

        getContentPane().add(tbrAdvances, java.awt.BorderLayout.NORTH);

        panMain0.setMinimumSize(new java.awt.Dimension(840, 650));
        panMain0.setPreferredSize(new java.awt.Dimension(840, 650));
        panMain0.setLayout(new java.awt.GridBagLayout());

        panInterestCalculation.setMinimumSize(new java.awt.Dimension(825, 430));
        panInterestCalculation.setPreferredSize(new java.awt.Dimension(825, 430));
        panInterestCalculation.setLayout(new java.awt.GridBagLayout());

        panInterestAndPeriod.setMinimumSize(new java.awt.Dimension(800, 420));
        panInterestAndPeriod.setPreferredSize(new java.awt.Dimension(800, 500));
        panInterestAndPeriod.setLayout(new java.awt.GridBagLayout());

        panPeriod.setBorder(javax.swing.BorderFactory.createTitledBorder("Period"));
        panPeriod.setMinimumSize(new java.awt.Dimension(550, 420));
        panPeriod.setPreferredSize(new java.awt.Dimension(550, 420));
        panPeriod.setLayout(new java.awt.GridBagLayout());

        lblDuration.setText("Duration");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPeriod.add(lblDuration, gridBagConstraints);

        txtPrincipal.setMinimumSize(new java.awt.Dimension(130, 21));
        txtPrincipal.setPreferredSize(new java.awt.Dimension(130, 21));
        txtPrincipal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPrincipalActionPerformed(evt);
            }
        });
        txtPrincipal.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPrincipalFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPeriod.add(txtPrincipal, gridBagConstraints);

        lblPrincipal.setText("Principal Amount");
        lblPrincipal.setMinimumSize(new java.awt.Dimension(102, 18));
        lblPrincipal.setPreferredSize(new java.awt.Dimension(102, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 4);
        panPeriod.add(lblPrincipal, gridBagConstraints);

        txtRateofInterest.setMinimumSize(new java.awt.Dimension(60, 21));
        txtRateofInterest.setPreferredSize(new java.awt.Dimension(60, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPeriod.add(txtRateofInterest, gridBagConstraints);

        lblRateofInterest.setText("Rate of Interest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 1, 4, 2);
        panPeriod.add(lblRateofInterest, gridBagConstraints);

        cboProductID.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProductID.setPopupWidth(225);
        cboProductID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProductIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPeriod.add(cboProductID, gridBagConstraints);

        lblProductID.setText("Product ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 4);
        panPeriod.add(lblProductID, gridBagConstraints);

        btnDepSubNoAccNew.setText("Calculate");
        btnDepSubNoAccNew.setMinimumSize(new java.awt.Dimension(100, 27));
        btnDepSubNoAccNew.setPreferredSize(new java.awt.Dimension(100, 27));
        btnDepSubNoAccNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDepSubNoAccNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPeriod.add(btnDepSubNoAccNew, gridBagConstraints);

        lblTotalInterest.setText("Total Interest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPeriod.add(lblTotalInterest, gridBagConstraints);

        txtTotalInterest.setMinimumSize(new java.awt.Dimension(130, 21));
        txtTotalInterest.setPreferredSize(new java.awt.Dimension(130, 21));
        txtTotalInterest.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPeriod.add(txtTotalInterest, gridBagConstraints);

        txtTotalAmount.setMinimumSize(new java.awt.Dimension(130, 21));
        txtTotalAmount.setPreferredSize(new java.awt.Dimension(130, 21));
        txtTotalAmount.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPeriod.add(txtTotalAmount, gridBagConstraints);

        lblTotalAmount.setText("Maturity Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPeriod.add(lblTotalAmount, gridBagConstraints);

        lblDays.setText("Days");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPeriod.add(lblDays, gridBagConstraints);

        lblMonths.setText("Months");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPeriod.add(lblMonths, gridBagConstraints);

        lblYears.setText("Years");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPeriod.add(lblYears, gridBagConstraints);

        txtDays.setMinimumSize(new java.awt.Dimension(50, 21));
        txtDays.setPreferredSize(new java.awt.Dimension(50, 21));
        txtDays.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDaysFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPeriod.add(txtDays, gridBagConstraints);

        txtMonths.setMinimumSize(new java.awt.Dimension(50, 21));
        txtMonths.setPreferredSize(new java.awt.Dimension(50, 21));
        txtMonths.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMonthsFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPeriod.add(txtMonths, gridBagConstraints);

        txtYears.setMinimumSize(new java.awt.Dimension(50, 21));
        txtYears.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPeriod.add(txtYears, gridBagConstraints);

        panInterestOption1.setMinimumSize(new java.awt.Dimension(90, 21));
        panInterestOption1.setPreferredSize(new java.awt.Dimension(90, 21));
        panInterestOption1.setLayout(new java.awt.GridBagLayout());

        rdoInterestOption.add(rdoDiscounted_No);
        rdoDiscounted_No.setText("No");
        rdoDiscounted_No.setMinimumSize(new java.awt.Dimension(50, 18));
        rdoDiscounted_No.setPreferredSize(new java.awt.Dimension(50, 18));
        rdoDiscounted_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDiscounted_NoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 1, 2, 0);
        panInterestOption1.add(rdoDiscounted_No, gridBagConstraints);

        rdoInterestOption.add(rdoDiscounted_Yes);
        rdoDiscounted_Yes.setText("Yes");
        rdoDiscounted_Yes.setMinimumSize(new java.awt.Dimension(50, 18));
        rdoDiscounted_Yes.setPreferredSize(new java.awt.Dimension(30, 18));
        rdoDiscounted_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDiscounted_YesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 1, 2, 1);
        panInterestOption1.add(rdoDiscounted_Yes, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPeriod.add(panInterestOption1, gridBagConstraints);

        lblDuration1.setText("Discounted Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPeriod.add(lblDuration1, gridBagConstraints);

        cboInterestPaymentFrequency.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "----Select----" }));
        cboInterestPaymentFrequency.setMinimumSize(new java.awt.Dimension(100, 21));
        cboInterestPaymentFrequency.setPopupWidth(100);
        cboInterestPaymentFrequency.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboInterestPaymentFrequencyActionPerformed(evt);
            }
        });
        cboInterestPaymentFrequency.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboInterestPaymentFrequencyFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 1);
        panPeriod.add(cboInterestPaymentFrequency, gridBagConstraints);

        lblRateofInterest1.setText("Interest Pay Mode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 1, 4, 2);
        panPeriod.add(lblRateofInterest1, gridBagConstraints);

        lblCategory.setText("Category");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panPeriod.add(lblCategory, gridBagConstraints);

        cboCategory.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "----Select----" }));
        cboCategory.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCategory.setPopupWidth(200);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 1);
        panPeriod.add(cboCategory, gridBagConstraints);

        btnDepSubNoAccNew1.setText("Clear");
        btnDepSubNoAccNew1.setMinimumSize(new java.awt.Dimension(100, 27));
        btnDepSubNoAccNew1.setPreferredSize(new java.awt.Dimension(100, 27));
        btnDepSubNoAccNew1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDepSubNoAccNew1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        panPeriod.add(btnDepSubNoAccNew1, gridBagConstraints);

        panResult.setMinimumSize(new java.awt.Dimension(300, 420));
        panResult.setPreferredSize(new java.awt.Dimension(300, 420));
        panResult.setLayout(new java.awt.GridBagLayout());

        srpResult.setPreferredSize(new java.awt.Dimension(74, 100));

        tblResult.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Value Date", "Interest Amount"
            }
        ));
        tblResult.setMinimumSize(new java.awt.Dimension(150, 0));
        tblResult.setPreferredScrollableViewportSize(new java.awt.Dimension(350, 300));
        tblResult.setPreferredSize(new java.awt.Dimension(150, 2500));
        tblResult.setOpaque(false);
        srpResult.setViewportView(tblResult);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panResult.add(srpResult, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panPeriod.add(panResult, gridBagConstraints);

        sptOperatesLike.setPreferredSize(new java.awt.Dimension(413, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPeriod.add(sptOperatesLike, gridBagConstraints);

        sptOperatesLike1.setPreferredSize(new java.awt.Dimension(413, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPeriod.add(sptOperatesLike1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panInterestAndPeriod.add(panPeriod, gridBagConstraints);

        panRateOfInterest.setBorder(javax.swing.BorderFactory.createTitledBorder("Rate of Interest"));
        panRateOfInterest.setMinimumSize(new java.awt.Dimension(420, 425));
        panRateOfInterest.setPreferredSize(new java.awt.Dimension(460, 425));
        panRateOfInterest.setLayout(new java.awt.GridBagLayout());

        lblInterestOption.setText("Interest Option");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 1, 2, 2);
        panRateOfInterest.add(lblInterestOption, gridBagConstraints);

        panInterestOption.setMinimumSize(new java.awt.Dimension(290, 31));
        panInterestOption.setLayout(new java.awt.GridBagLayout());

        rdoInterestOption.add(rdoInterestOption_Simple);
        rdoInterestOption_Simple.setSelected(true);
        rdoInterestOption_Simple.setText("Simple");
        rdoInterestOption_Simple.setPreferredSize(new java.awt.Dimension(65, 18));
        rdoInterestOption_Simple.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoInterestOption_SimpleActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 1);
        panInterestOption.add(rdoInterestOption_Simple, gridBagConstraints);

        rdoInterestOption.add(rdoInterestOption_Fixed);
        rdoInterestOption_Fixed.setText("Fixed");
        rdoInterestOption_Fixed.setPreferredSize(new java.awt.Dimension(57, 18));
        rdoInterestOption_Fixed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoInterestOption_FixedActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 1, 2, 0);
        panInterestOption.add(rdoInterestOption_Fixed, gridBagConstraints);

        rdoInterestOption.add(rdoInterestOption_Compound);
        rdoInterestOption_Compound.setText("Compound");
        rdoInterestOption_Compound.setPreferredSize(new java.awt.Dimension(87, 18));
        rdoInterestOption_Compound.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoInterestOption_CompoundActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 1, 2, 1);
        panInterestOption.add(rdoInterestOption_Compound, gridBagConstraints);

        rdoInterestOption.add(rdoInterestOption_Floating);
        rdoInterestOption_Floating.setText("Floating");
        rdoInterestOption_Floating.setPreferredSize(new java.awt.Dimension(71, 18));
        rdoInterestOption_Floating.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoInterestOption_FloatingActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 1, 2, 1);
        panInterestOption.add(rdoInterestOption_Floating, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panRateOfInterest.add(panInterestOption, gridBagConstraints);

        lblCompounded.setText("Interest Comp Freq");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 1, 2, 2);
        panRateOfInterest.add(lblCompounded, gridBagConstraints);

        panInterest.setLayout(new java.awt.GridBagLayout());

        lblPercentageOfRate.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterest.add(lblPercentageOfRate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterest.add(cboRateOption, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panRateOfInterest.add(panInterest, gridBagConstraints);

        lblPenalRate.setText("Penal Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 1, 2, 2);
        panRateOfInterest.add(lblPenalRate, gridBagConstraints);

        panPenalRate.setLayout(new java.awt.GridBagLayout());

        txtPenalRate.setMinimumSize(new java.awt.Dimension(60, 21));
        txtPenalRate.setPreferredSize(new java.awt.Dimension(60, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPenalRate.add(txtPenalRate, gridBagConstraints);

        lblPenalRatePercentage.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPenalRate.add(lblPenalRatePercentage, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panRateOfInterest.add(panPenalRate, gridBagConstraints);

        panCompound.setMinimumSize(new java.awt.Dimension(175, 22));
        panCompound.setPreferredSize(new java.awt.Dimension(170, 25));
        panCompound.setLayout(new java.awt.GridBagLayout());

        cboCompoundingType.setMinimumSize(new java.awt.Dimension(80, 21));
        cboCompoundingType.setPopupWidth(200);
        cboCompoundingType.setPreferredSize(new java.awt.Dimension(80, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 1, 4, 1);
        panCompound.add(cboCompoundingType, gridBagConstraints);

        cboCompounded.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Daily", "Weekly", "Monthly", "Fortnightly", "Bimonthly", "Quarterly", "Semiannually", "Annually" }));
        cboCompounded.setMinimumSize(new java.awt.Dimension(80, 22));
        cboCompounded.setPopupWidth(0);
        cboCompounded.setPreferredSize(new java.awt.Dimension(80, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 1, 4, 1);
        panCompound.add(cboCompounded, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panRateOfInterest.add(panCompound, gridBagConstraints);

        lblDepositsInstallment.setText("Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 1, 2, 2);
        panRateOfInterest.add(lblDepositsInstallment, gridBagConstraints);

        cboAccountType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Account-wise", "Ledger-wise" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 1, 2, 2);
        panRateOfInterest.add(cboAccountType, gridBagConstraints);

        cboDepositsCompounded.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Daily", "Weekly", "Monthly", "Fortnightly", "Bimonthly", "Quarterly", "Semiannually", "Annually" }));
        cboDepositsCompounded.setMinimumSize(new java.awt.Dimension(80, 22));
        cboDepositsCompounded.setPreferredSize(new java.awt.Dimension(80, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 1, 2, 2);
        panRateOfInterest.add(cboDepositsCompounded, gridBagConstraints);

        lblAccountType.setText("Account Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 1, 2, 2);
        panRateOfInterest.add(lblAccountType, gridBagConstraints);

        lblDepositsInterest.setText("Deposit Installment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 1, 2, 2);
        panRateOfInterest.add(lblDepositsInterest, gridBagConstraints);

        panAccount.setMinimumSize(new java.awt.Dimension(270, 105));
        panAccount.setPreferredSize(new java.awt.Dimension(270, 105));
        panAccount.setLayout(new java.awt.GridBagLayout());

        lblAccountTo.setText("To Account");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAccount.add(lblAccountTo, gridBagConstraints);

        panAccountFrom.setLayout(new java.awt.GridBagLayout());

        txtFromAccount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAccountFrom.add(txtFromAccount, gridBagConstraints);

        btnFromAccount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnFromAccount.setPreferredSize(new java.awt.Dimension(21, 20));
        btnFromAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFromAccountActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAccountFrom.add(btnFromAccount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panAccount.add(panAccountFrom, gridBagConstraints);

        panAccountTo.setLayout(new java.awt.GridBagLayout());

        txtToAccount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAccountTo.add(txtToAccount, gridBagConstraints);

        btnToAccount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnToAccount.setPreferredSize(new java.awt.Dimension(21, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAccountTo.add(btnToAccount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panAccount.add(panAccountTo, gridBagConstraints);

        lblAccountFrom.setText("Account");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAccount.add(lblAccountFrom, gridBagConstraints);

        lblPeriodOpt.setText("Duration/Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAccount.add(lblPeriodOpt, gridBagConstraints);

        txtGracePeriod.setMinimumSize(new java.awt.Dimension(60, 21));
        txtGracePeriod.setPreferredSize(new java.awt.Dimension(60, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAccount.add(txtGracePeriod, gridBagConstraints);

        rdoPeriodOption.add(rdoPeriodOption_Date);
        rdoPeriodOption_Date.setText("Date");
        rdoPeriodOption_Date.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoPeriodOption_DateActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccount.add(rdoPeriodOption_Date, gridBagConstraints);

        cboGracePeriod.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Days", "Months", "Years" }));
        cboGracePeriod.setPreferredSize(new java.awt.Dimension(80, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAccount.add(cboGracePeriod, gridBagConstraints);

        rdoPeriodOption.add(rdoPeriodOption_Duration);
        rdoPeriodOption_Duration.setSelected(true);
        rdoPeriodOption_Duration.setText("Duration");
        rdoPeriodOption_Duration.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoPeriodOption_DurationActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panAccount.add(rdoPeriodOption_Duration, gridBagConstraints);

        lblGracePeriod.setText("Grace Period");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAccount.add(lblGracePeriod, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        panRateOfInterest.add(panAccount, gridBagConstraints);

        lblFromDate.setText("From");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRateOfInterest.add(lblFromDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panRateOfInterest.add(tdtFromDate, gridBagConstraints);

        lblToDate.setText("To");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRateOfInterest.add(lblToDate, gridBagConstraints);

        tdtToDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtToDateFocusLost(evt);
            }
        });
        tdtToDate.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                tdtToDateInputMethodTextChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panRateOfInterest.add(tdtToDate, gridBagConstraints);

        cboYear.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "365", "366", "360" }));
        cboYear.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panRateOfInterest.add(cboYear, gridBagConstraints);

        lblYear.setText("Year");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRateOfInterest.add(lblYear, gridBagConstraints);

        lblMonth.setText("Month");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRateOfInterest.add(lblMonth, gridBagConstraints);

        cboMonth.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "30", "Actual Day" }));
        cboMonth.setPreferredSize(new java.awt.Dimension(80, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panRateOfInterest.add(cboMonth, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panInterestAndPeriod.add(panRateOfInterest, gridBagConstraints);

        panGracePeriod.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInterestAndPeriod.add(panGracePeriod, gridBagConstraints);

        panMain1.setMinimumSize(new java.awt.Dimension(850, 80));
        panMain1.setPreferredSize(new java.awt.Dimension(700, 80));
        panMain1.setLayout(new java.awt.GridBagLayout());

        panRounding.setMinimumSize(new java.awt.Dimension(450, 70));
        panRounding.setPreferredSize(new java.awt.Dimension(450, 70));
        panRounding.setLayout(new java.awt.GridBagLayout());

        lblRoundingPrincipal.setText("Principal Rounding off Factor");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panRounding.add(lblRoundingPrincipal, gridBagConstraints);

        lblRoundingInterest.setText("Interest Rounding off Factor");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panRounding.add(lblRoundingInterest, gridBagConstraints);

        cboRoundingInterest.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Nearest", "Lower", "Higher", "Next Digit" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panRounding.add(cboRoundingInterest, gridBagConstraints);

        lblFloatPrecision.setText("Float Precision");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panRounding.add(lblFloatPrecision, gridBagConstraints);

        cboFloatPrecision.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0", "1", "2", "3", "4" }));
        cboFloatPrecision.setMinimumSize(new java.awt.Dimension(50, 21));
        cboFloatPrecision.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panRounding.add(cboFloatPrecision, gridBagConstraints);

        cboRoundingPrincipal.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panRounding.add(cboRoundingPrincipal, gridBagConstraints);

        cboPrincipalRoundingValue.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "5 Paise", "10 Paise", "25 Paise", "50 Paise", "1 Rupee", "5 Rupees", "10 Rupees", "50 Rupees", "100 Rupees" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panRounding.add(cboPrincipalRoundingValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panMain1.add(panRounding, gridBagConstraints);

        panInterestHead.setMinimumSize(new java.awt.Dimension(350, 60));
        panInterestHead.setPreferredSize(new java.awt.Dimension(350, 60));
        panInterestHead.setLayout(new java.awt.GridBagLayout());

        panCreditHead.setLayout(new java.awt.GridBagLayout());

        txtInterestCreditHead.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCreditHead.add(txtInterestCreditHead, gridBagConstraints);

        btnInterestCreditHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnInterestCreditHead.setPreferredSize(new java.awt.Dimension(21, 20));
        btnInterestCreditHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInterestCreditHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCreditHead.add(btnInterestCreditHead, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panInterestHead.add(panCreditHead, gridBagConstraints);

        panDebitHead.setLayout(new java.awt.GridBagLayout());

        txtInterestDebitHead.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitHead.add(txtInterestDebitHead, gridBagConstraints);

        btnInterestDebitHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnInterestDebitHead.setPreferredSize(new java.awt.Dimension(21, 20));
        btnInterestDebitHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInterestDebitHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitHead.add(btnInterestDebitHead, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panInterestHead.add(panDebitHead, gridBagConstraints);

        lblInterestCreditHead.setText("Interest Credit Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panInterestHead.add(lblInterestCreditHead, gridBagConstraints);

        lblInterestDebitHead.setText("Interest Debit Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panInterestHead.add(lblInterestDebitHead, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panMain1.add(panInterestHead, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.weightx = 1.0;
        panInterestAndPeriod.add(panMain1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panInterestCalculation.add(panInterestAndPeriod, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        panInterestCalculation.add(sptReport, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panMain0.add(panInterestCalculation, gridBagConstraints);

        panPeriodResult.setMinimumSize(new java.awt.Dimension(840, 75));
        panPeriodResult.setPreferredSize(new java.awt.Dimension(840, 75));
        panPeriodResult.setLayout(new java.awt.GridBagLayout());

        srpPeriodResult.setMinimumSize(new java.awt.Dimension(200, 150));
        srpPeriodResult.setPreferredSize(new java.awt.Dimension(200, 150));
        srpPeriodResult.setOpaque(false);

        tblPeriodResult.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Value Date", "Amount"
            }
        ));
        tblPeriodResult.setPreferredSize(new java.awt.Dimension(200, 90));
        srpPeriodResult.setViewportView(tblPeriodResult);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.8;
        panPeriodResult.add(srpPeriodResult, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 0.6;
        gridBagConstraints.weighty = 1.0;
        panMain0.add(panPeriodResult, gridBagConstraints);

        getContentPane().add(panMain0, java.awt.BorderLayout.CENTER);

        mnuProcess1.setText("Process");

        mitNew.setMnemonic('N');
        mitNew.setText("New");
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess1.add(mitNew);

        mitEdit.setMnemonic('E');
        mitEdit.setText("Edit");
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess1.add(mitEdit);

        mitDelete.setMnemonic('D');
        mitDelete.setText("Delete");
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess1.add(mitDelete);
        mnuProcess1.add(sptDelete);

        mitSave.setMnemonic('S');
        mitSave.setText("Save");
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess1.add(mitSave);

        mitCancel.setMnemonic('C');
        mitCancel.setText("Cancel");
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess1.add(mitCancel);
        mnuProcess1.add(sptCancel);

        mitClose.setMnemonic('L');
        mitClose.setText("Close");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess1.add(mitClose);

        mbrMain1.add(mnuProcess1);

        setJMenuBar(mbrMain1);
    }// </editor-fold>//GEN-END:initComponents

    private void rdoDiscounted_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDiscounted_NoActionPerformed
        // TODO add your handling code here:
        cboInterestPaymentFrequency.setSelectedItem("");
        rdoDiscounted_Yes.setSelected(false);
        rdoDiscounted_No.setSelected(true);

    }//GEN-LAST:event_rdoDiscounted_NoActionPerformed

    private void rdoDiscounted_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDiscounted_YesActionPerformed
        // TODO add your handling code here:
        cboInterestPaymentFrequency.setSelectedItem("");
        rdoDiscounted_Yes.setSelected(true);
        rdoDiscounted_No.setSelected(false);

    }//GEN-LAST:event_rdoDiscounted_YesActionPerformed

    private void cboInterestPaymentFrequencyFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboInterestPaymentFrequencyFocusLost
        // TODO add your handling code here:

    }//GEN-LAST:event_cboInterestPaymentFrequencyFocusLost

    private void cboInterestPaymentFrequencyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboInterestPaymentFrequencyActionPerformed
        // TODO add your handling code here:
        String freq = CommonUtil.convertObjToStr(observable.getCbmInterestPaymentFrequency().getKeyForSelected());
        if(!behavesLike.equals("") && behavesLike.equals("FIXED")){
            if(!freq.equals("") && freq.equals("30")){
//                rdoDiscounted_Yes.setSelected(true);
            }else 
                rdoDiscounted_No.setSelected(true);
            freq = "";
        }
    }//GEN-LAST:event_cboInterestPaymentFrequencyActionPerformed

    private void cboProductIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProductIDActionPerformed
        // TODO add your handling code here:
        String prodId = ((ComboBoxModel)cboProductID.getModel()).getKeyForSelected().toString();
        HashMap prodMap = new HashMap();
        prodMap.put("PROD_ID",prodId);
        List lst = ClientUtil.executeQuery("getProductBehavesLike",prodMap);
        if(lst!=null && lst.size()>0){
            prodMap = (HashMap)lst.get(0);
            behavesLike = CommonUtil.convertObjToStr(prodMap.get("BEHAVES_LIKE"));
            if(behavesLike.equals("FIXED")){
                panInterestOption1.setVisible(true);
                lblDuration1.setVisible(true);
                cboInterestPaymentFrequency.setVisible(true);
                lblRateofInterest1.setVisible(true);
                txtDays.setVisible(true);
                lblDays.setVisible(true);
            }else {
                if(behavesLike.equals("RECURRING")){
                    txtDays.setVisible(false);
                    lblDays.setVisible(false);
                }else{
                    txtDays.setVisible(true);
                    lblDays.setVisible(true);
                }
                panInterestOption1.setVisible(false);
                lblDuration1.setVisible(false); 
                cboInterestPaymentFrequency.setVisible(false);
                lblRateofInterest1.setVisible(false);
            }
        }
    }//GEN-LAST:event_cboProductIDActionPerformed

    private void btnDepSubNoAccNew1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDepSubNoAccNew1ActionPerformed
        // TODO add your handling code here:
        ClientUtil.enableDisable(this,true);
        cboProductID.setSelectedItem("");
        txtYears.setText("");
        txtMonths.setText("");
        txtDays.setText("");
        cboCategory.setSelectedItem("");
        cboInterestPaymentFrequency.setSelectedItem("");
        txtPrincipal.setText("");
        txtRateofInterest.setText("");
        txtTotalInterest.setText("");
        txtTotalAmount.setText("");
        txtTotalInterest.setEnabled(false);
        txtTotalAmount.setEnabled(false);
        observable.resetTableModel();
    }//GEN-LAST:event_btnDepSubNoAccNew1ActionPerformed

    private void btnDepSubNoAccNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDepSubNoAccNewActionPerformed
        // TODO add your handling code here:        
        String payMode = CommonUtil.convertObjToStr(cboInterestPaymentFrequency.getSelectedItem());
        HashMap detailsHash = new HashMap();
        detailsHash.put("AMOUNT", txtPrincipal.getText());
        detailsHash.put("DEPOSIT_DT",currDt.clone());
        if(txtDays.getText().equals("") && txtDays.getText().length() == 0)
            detailsHash.put("PERIOD_DAYS", "0");
        else if(!txtDays.getText().equals("")&& txtDays.getText().length()>0)
            detailsHash.put("PERIOD_DAYS", txtDays.getText());
        if(txtMonths.getText().equals("") && txtMonths.getText().length() == 0)
            detailsHash.put("PERIOD_MONTHS", "0");
        else if(!txtMonths.getText().equals("")&& txtMonths.getText().length()>0)
            detailsHash.put("PERIOD_MONTHS", txtMonths.getText());
        if(txtYears.getText().equals("")&& txtYears.getText().length() == 0)
            detailsHash.put("PERIOD_YEARS", "0");
        else if(!txtYears.getText().equals("")&& txtYears.getText().length()>0)
            detailsHash.put("PERIOD_YEARS", txtYears.getText());
        detailsHash.put("MATURITY_DT", matDt);
        detailsHash.put("ROI", txtRateofInterest.getText());
        if(behavesLike.equals("FIXED")){
            if(rdoDiscounted_Yes.isSelected() == false && rdoDiscounted_No.isSelected() == false && 
            !payMode.equals("") && payMode.equals("Monthly")){
                ClientUtil.displayAlert("discounted Rate choose...");
                return;
            }                
            if(payMode.equals("")){
                ClientUtil.displayAlert("Interest Payment mode choose...");
                return;
            }
            if(rdoDiscounted_Yes.isSelected() == true)
                detailsHash.put("DISCOUNTED_RATE","Y");
            else if(rdoDiscounted_No.isSelected() == true)
                detailsHash.put("DISCOUNTED_RATE","N");
        if(payMode.equals("Monthly"))
            payMode = "MONTHLY";
        else if(payMode.equals("Quaterly"))
            payMode = "QUATERLY";
        else if(payMode.equals("Half Yearly"))
            payMode = "HALF YEARLY";
        else if(payMode.equals("Yearly"))
            payMode = "YEARLY";
        else
            payMode = "DATE OF MATURITY";
            
        period = (long) (CommonUtil.convertObjToDouble(txtDays.getText()).doubleValue() + 
                (CommonUtil.convertObjToDouble(txtMonths.getText()).doubleValue() * 30) +
                (CommonUtil.convertObjToDouble(txtYears.getText()).doubleValue() * 360));
        }else{
            payMode = "QUATERLY";            
            period = (long) (CommonUtil.convertObjToDouble(txtDays.getText()).doubleValue() + 
                    (CommonUtil.convertObjToDouble(txtMonths.getText()).doubleValue() * 30) +
                    (CommonUtil.convertObjToDouble(txtYears.getText()).doubleValue() * 360));
        }
        detailsHash.put("INTEREST_TYPE", payMode);
        detailsHash.put("BEHAVES_LIKE", behavesLike);
        detailsHash.put("PEROID", new Double(period));
        HashMap amtDetHash = observable.calculateIntDetails(detailsHash);
        System.out.println("$$$$$detailshash :"+detailsHash);
        double interestAmt = 0.0,depositAmt = 0.0,maturityAmt = 0.0;
        if(behavesLike.equals("FIXED")){
            txtTotalAmount.setText(txtPrincipal.getText());
            observable.setTxtTotalAmount(txtTotalAmount.getText());
            interestAmt =CommonUtil.convertObjToDouble(amtDetHash.get("INTEREST")).doubleValue();
            interestAmt = (double)getNearest((long)(interestAmt *100),100)/100;
            txtTotalInterest.setText(String.valueOf(interestAmt));
            observable.setTxtTotalInterest(String.valueOf(interestAmt));
        }else if(behavesLike.equals("CUMMULATIVE")){
            interestAmt =CommonUtil.convertObjToDouble(amtDetHash.get("INTEREST")).doubleValue();
            interestAmt = (double)getNearest((long)(interestAmt *100),100)/100;
            depositAmt = CommonUtil.convertObjToDouble(txtPrincipal.getText()).doubleValue();
            txtTotalInterest.setText(String.valueOf(interestAmt));
            observable.setTxtTotalInterest(String.valueOf(interestAmt));
            txtTotalAmount.setText(String.valueOf(depositAmt+interestAmt));
        }else if(behavesLike.equals("RECURRING")){
            maturityAmt = CommonUtil.convertObjToDouble(amtDetHash.get("AMOUNT")).doubleValue();
            maturityAmt = (double)getNearest((long)(maturityAmt *100),100)/100;
            interestAmt =CommonUtil.convertObjToDouble(amtDetHash.get("INTEREST")).doubleValue();
            interestAmt = (double)getNearest((long)(interestAmt *100),100)/100;
            txtTotalAmount.setText(String.valueOf(maturityAmt));
            txtTotalInterest.setText(String.valueOf(interestAmt));
            observable.setTxtTotalInterest(String.valueOf(interestAmt));
            observable.setTxtTotalAmount(String.valueOf(maturityAmt));
        }
    }//GEN-LAST:event_btnDepSubNoAccNewActionPerformed
    
    public long getNearest(long number,long roundingFactor)  {
        long roundingFactorOdd = roundingFactor;
        if ((roundingFactor%2) != 0)
            roundingFactorOdd +=1;
        long mod = number%roundingFactor;
        if ((mod < (roundingFactor/2)) || (mod < (roundingFactorOdd/2)))
            return lower(number,roundingFactor);
        else
            return higher(number,roundingFactor);
    }
    
    public long lower(long number,long roundingFactor) {
        long mod = number%roundingFactor;
        return number-mod;
    }
    
    public long higher(long number,long roundingFactor) {
        long mod = number%roundingFactor;
        if ( mod == 0)
            return number;
        return (number-mod) + roundingFactor ;
    }
    
    private void txtPrincipalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrincipalActionPerformed
        // TODO add your handling code here:
//        txtPrincipalFocusLost(null);
    }//GEN-LAST:event_txtPrincipalActionPerformed
    private String  calculateMatDate(){
        String maturityDate = null;
        java.util.Date depDate = (java.util.Date)currDt.clone();
        System.out.println("####calculateMatDate : "+depDate);
        if(depDate !=null){
            GregorianCalendar cal = new GregorianCalendar((depDate.getYear()+1900),depDate.getMonth(),depDate.getDate());
            if((txtYears.getText() != null) && (!txtYears.getText().equals(""))){
                cal.add(GregorianCalendar.YEAR, Integer.parseInt(txtYears.getText()));
            }else{
                txtYears.setText(String.valueOf(0));
                cal.add(GregorianCalendar.YEAR, 0);
            }
            if((txtMonths.getText() != null) && (!txtMonths.getText().equals(""))){
                cal.add(GregorianCalendar.MONTH, Integer.parseInt(txtMonths.getText()));
            }else{
                txtMonths.setText(String.valueOf(0));
                cal.add(GregorianCalendar.MONTH, 0);
            }
            if((txtDays.getText() != null) && (!txtDays.getText().equals(""))){
                cal.add(GregorianCalendar.DAY_OF_MONTH, Integer.parseInt(txtDays.getText()));
            }else{
                txtDays.setText(String.valueOf(0));
                cal.add(GregorianCalendar.DAY_OF_MONTH, 0);
            }
            maturityDate = (DateUtil.getStringDate(cal.getTime()));
        }
        return maturityDate;
    }
    private void txtPrincipalFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPrincipalFocusLost
        // TODO add your handling code here:
        HashMap rateOfIntMap = new HashMap();
        HashMap periodMap = new HashMap();
//        periodMap.put("DAYS",new Double(txtDays.getText()));
//        periodMap.put("MONTHS",new Double(txtMonths.getText()));
//        periodMap.put("YEARS",new Double(txtYears.getText()));
//        if(txtDays.getText().equals("") && txtDays.getText().length() == 0)
//            txtDays.setText(String.valueOf(0.0));
//        if(txtMonths.getText().equals("") && txtMonths.getText().length() == 0)
//            txtMonths.setText(String.valueOf(0.0));
//        if(txtYears.getText().equals("")&& txtYears.getText().length() == 0)
//            txtYears.setText(String.valueOf(0.0));
        
        Date startDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(currDt.clone()));
        matDt = DateUtil.getDateMMDDYYYY(calculateMatDate());
        period = DateUtil.dateDiff(startDt,matDt);
        long count = 0;
        while (DateUtil.dateDiff(startDt, matDt)>0) {
            int month = startDt.getMonth();
            int startYear = startDt.getYear()+1900;
            if (month==1 && startYear%4==0)
                count++;
            startDt = DateUtil.addDays(startDt, 30);
        }
        period -= count;
        String prodId = ((ComboBoxModel)cboProductID.getModel()).getKeyForSelected().toString();
        String category = ((ComboBoxModel)cboCategory.getModel()).getKeyForSelected().toString();
        HashMap prodMap = new HashMap();
        prodMap.put("PROD_ID",prodId);
        List lst = ClientUtil.executeQuery("getProductBehavesLike",prodMap);
        if(lst!=null && lst.size()>0){
            prodMap = (HashMap)lst.get(0);
            behavesLike = CommonUtil.convertObjToStr(prodMap.get("BEHAVES_LIKE"));
            rateOfIntMap.put("PRODUCT_TYPE","TD");
            rateOfIntMap.put("PROD_ID",prodId);
            rateOfIntMap.put("CATEGORY_ID",category);
            rateOfIntMap.put("AMOUNT",CommonUtil.convertObjToDouble(txtPrincipal.getText()));
            rateOfIntMap.put("DEPOSIT_DT",currDt);
            rateOfIntMap.put("PERIOD",new Double(period));
            lst = ClientUtil.executeQuery("icm.getInterestRates", rateOfIntMap);
            if(lst!=null && lst.size()>0){
                HashMap outputMap = (HashMap)lst.get(0);
                txtRateofInterest.setText(String.valueOf(outputMap.get("ROI")));
//                txtRateofInterest.setEditable(false);             
            }else{
                
            }
        }
    }//GEN-LAST:event_txtPrincipalFocusLost

    private void btnInterestDebitHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInterestDebitHeadActionPerformed
        callView("INTEREST_DEBIT_HD");
        // Add your handling code here:
    }//GEN-LAST:event_btnInterestDebitHeadActionPerformed

    private void btnInterestCreditHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInterestCreditHeadActionPerformed
        callView("INTEREST_CREDIT_HD");
        // Add your handling code here:
    }//GEN-LAST:event_btnInterestCreditHeadActionPerformed

    private void btnFromAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFromAccountActionPerformed
        callView("ACCOUNT_NO");
        // Add your handling code here:
    }//GEN-LAST:event_btnFromAccountActionPerformed
    
    private void txtMonthsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMonthsFocusLost
        // Add your handling code here:
//        if(txtMonths.getText().length()!=0){
//            if (Integer.parseInt(txtMonths.getText()) > 12) {
//                JOptionPane.showMessageDialog(this,"Month should be less then 13");
//                txtMonths.setText("");
//            }
//        }
        if(behavesLike.equals("RECURRING")){
            int recurrPeriod = (int)(CommonUtil.convertObjToInt(txtMonths.getText())) +
                               (CommonUtil.convertObjToInt(txtYears.getText()) * 12);
            int balancePeriod = recurrPeriod %3;
            if(balancePeriod!=0){
                ClientUtil.showAlertWindow("Enter Multiples of Three Months...");
                txtMonths.requestFocus();
                return;
            }
        }

    }//GEN-LAST:event_txtMonthsFocusLost
    
    private void txtDaysFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDaysFocusLost
        // Add your handling code here:
        if(behavesLike.equals("CUMMULATIVE") && txtDays.getText().length()!=0 && Integer.parseInt(txtDays.getText()) > 90){
            JOptionPane.showMessageDialog(this,"Days should be less then 90");
            txtDays.setText("");
        }
        
    }//GEN-LAST:event_txtDaysFocusLost
    
    private void rdoInterestOption_FixedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoInterestOption_FixedActionPerformed
        // Add your handling code here:
        cboCompoundingType.setSelectedItem("");
        cboAccountType.setSelectedItem("");
        cboCompounded.setSelectedItem("");
        cboCompoundingType.setSelectedItem("");
        cboCompounded.setEnabled(false);
        cboAccountType.setEnabled(false);
        cboCompoundingType.setEnabled(false);
    }//GEN-LAST:event_rdoInterestOption_FixedActionPerformed
    
    private void rdoInterestOption_SimpleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoInterestOption_SimpleActionPerformed
        // Add your handling code here:
        cboCompoundingType.setSelectedItem("");
        cboAccountType.setSelectedItem("");
        cboCompounded.setSelectedItem("");
        cboCompoundingType.setSelectedItem("");
        cboCompounded.setEnabled(false);
        cboAccountType.setEnabled(false);
        cboCompoundingType.setEnabled(false);
//        observable.setCboCompounded("");
//        cboCompounded.setSelectedItem(observable.getCboCompounded());

    }//GEN-LAST:event_rdoInterestOption_SimpleActionPerformed
    
    private void tdtToDateInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_tdtToDateInputMethodTextChanged
        // Add your handling code here:
        System.out.println("yes");
    }//GEN-LAST:event_tdtToDateInputMethodTextChanged
    
    private void tdtToDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtToDateFocusLost
        // Add your handling code here:
        System.out.println("yes");
        Date frmDate = DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue());
        Date toDate = DateUtil.getDateMMDDYYYY(tdtToDate.getDateValue());
        try {
            System.out.println("!!!"+DateUtil.dateDiff(frmDate,toDate));
        if (DateUtil.dateDiff(frmDate,toDate) < 0) {
            ClientUtil.displayAlert("To date should be greater than from date");
            tdtToDate.setDateValue("");
            tdtToDate.requestFocus();
        }
        }catch(Exception ioe){}
/*        if ( tdtToDate.getDateValue() != null) {
            ToDateValidation toDate= new ToDateValidation(DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue()));
            toDate.setComponent(tdtToDate);
            System.out.println(toDate.validate());
            if(!toDate.validate()) {
                toDate.setErrorMessage("To date should be greater than from date");
                tdtToDate.setDateValue("");
            }
        }*/
        
    }//GEN-LAST:event_tdtToDateFocusLost
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
        btnCancelActionPerformed(null);
//        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void rdoInterestOption_CompoundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoInterestOption_CompoundActionPerformed
        // Add your handling code here:
        cboProductID.setSelectedItem("");
        observable.setCboProductID("");
        cboCategory.setSelectedItem("");
        observable.setCboInterestPaymentFrequency("");
        cboInterestPaymentFrequency.setSelectedItem("");
        cboCompoundingType.setSelectedItem("");
        cboAccountType.setSelectedItem("");
        cboCompounded.setSelectedItem("");
        cboCompoundingType.setSelectedItem("");
        cboCompounded.setEnabled(true);
        cboAccountType.setEnabled(false);
        cboCompoundingType.setEnabled(true);
        cboDepositsCompounded.setEnabled(true);
    }//GEN-LAST:event_rdoInterestOption_CompoundActionPerformed
    
    private void rdoInterestOption_FloatingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoInterestOption_FloatingActionPerformed
        // Add your handling code here:
        cboCompoundingType.setSelectedItem("");
        cboAccountType.setSelectedItem("");
        cboCompounded.setSelectedItem("");
        cboCompoundingType.setSelectedItem("");
        cboAccountType.setEnabled(true);
        cboCompounded.setEnabled(false);
        cboCompoundingType.setEnabled(false);
    }//GEN-LAST:event_rdoInterestOption_FloatingActionPerformed
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed
    
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
    
    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // Add your handling code here:
         HashMap reportParamMap = new HashMap();
 com.see.truetransact.clientutil.ttrintegration.LinkReport.getReports(getScreenID(), reportParamMap);
    }//GEN-LAST:event_btnPrintActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        observable.resetOBFields();
        ClientUtil.clearAll(panPeriodResult);
        ClientUtil.clearAll(panResult);
        ClientUtil.enableDisable(this, false);
        setButtonEnableDisable();
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        lblStatus.setText(observable.getLblStatus());
        txtTotalAmount.setText("");
        txtTotalInterest.setText("");
        btnDepSubNoAccNew1.setEnabled(false);
        btnDepSubNoAccNew.setEnabled(false);   
        setModified(false);
        
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        
        setModified(false);
        /*
        final String mandatoryMessage = checkMandatory(panInterestCalculation);
        if(mandatoryMessage.length() > 0 ){
            displayAlert(mandatoryMessage);
        }
        else{*/
        if(validateInputs()) {
            updateOBFields();
            observable.doAction();
        }
        
        
        
    }//GEN-LAST:event_btnSaveActionPerformed
    private String checkMandatory(JComponent component){
        return new MandatoryCheck().checkMandatory(getClass().getName(),component);
    }
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        setModified(true);
        ClientUtil.enableDisable(this,true);
        setButtonEnableDisable();
        buttonEnableDisable();
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        lblStatus.setText(observable.getLblStatus());
        observable.resetOBFields();
        txtTotalInterest.setText("");
        txtTotalAmount.setText("");
        txtTotalInterest.setEnabled(false);
        txtTotalAmount.setEnabled(false);
        btnDepSubNoAccNew1.setEnabled(true);
        btnDepSubNoAccNew.setEnabled(true);
    }//GEN-LAST:event_btnNewActionPerformed
    
    private void rdoPeriodOption_DateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoPeriodOption_DateActionPerformed
        // Add your handling code here:
        tdtFromDate.setEnabled(true);
        tdtToDate.setEnabled(true);
//        ClientUtil.enableDisable(panDuration, false);
        cboMonth.setEnabled(true);
    }//GEN-LAST:event_rdoPeriodOption_DateActionPerformed
    
    private void rdoPeriodOption_DurationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoPeriodOption_DurationActionPerformed
        // Add your handling code here:
        tdtFromDate.setEnabled(false);
        tdtToDate.setEnabled(false);
//        ClientUtil.enableDisable(panDuration, true);
        cboMonth.setEnabled(false);
    }//GEN-LAST:event_rdoPeriodOption_DurationActionPerformed
    
    private boolean validateInputs() {
        boolean val=true;
        String message="";
        if(rdoPeriodOption_Duration.isSelected()) {
            if ( txtDays.getText().length()==0 && txtMonths.getText().length()==0 &&
            txtYears.getText().length()==0) {
                message="Duration is not given";
                val=false;
            }
        }
        else if (rdoPeriodOption_Date.isSelected()){
            if ( tdtFromDate.getDateValue().length()==0 || tdtToDate.getDateValue().length()==0){
                message="From and To Date must be filled";
                val=false;
            }
            
            if ( tdtToDate.getDateValue().length()!=0) {
                ToDateValidation toDate= new ToDateValidation(DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue()));
                toDate.setComponent(tdtToDate);
                System.out.println(toDate.validate());
                if(!toDate.validate()){
                    toDate.setErrorMessage("To date should be greater than from date");
                    tdtToDate.setDateValue("");
                    message=toDate.getErrorMessage();
                    val= false;
                }
            }
        }
        if(!val)
            JOptionPane.showMessageDialog(this,message);
        return val;
    }
    
    private void callView(String currField) {
        viewType = currField;
        HashMap viewMap = new HashMap();
        if (currField.equals("Edit") || currField.equals("Delete")){
            viewMap.put("MAPNAME", "getSelectOutwardClearingTOList");
            //viewMap.put(CommonConstants.MAP_WHERE, "DELETED");
        }else if(currField.equals("ACCOUNT_NO")){
            viewMap.put("MAPNAME", "getSelectDepositAccounts4ProdID");
            HashMap whereMap = new HashMap();
            whereMap.put("PROD_ID",(String)observable.getCbmProductID().getKeyForSelected());
            viewMap.put(com.see.truetransact.commonutil.CommonConstants.MAP_WHERE, whereMap);
        } else {
            viewMap.put("MAPNAME", "getSelectAcctHeadTOList");
        }   

        new ViewAll(this, viewMap).show();
    }
    
    public void fillData(Object obj) {
        HashMap hash = (HashMap) obj;
        if (viewType != null) {
            if (viewType.equals("Edit") || viewType.equals("Delete")) {
                /* Write the code if Edit or delete is pressed*/    
                //update(null, null);
            }else if (viewType.equals("ACCOUNT_NO")) {
                updateOBFields();
                observable.getAccNoDetails((String)hash.get("ACC_NO")); 
            } else if(viewType.equals("INTEREST_CREDIT_HD")) {
                txtInterestCreditHead.setText((String)hash.get("AC_HD_ID"));
            } else if(viewType.equals("INTEREST_DEBIT_HD")) {
                txtInterestDebitHead.setText((String)hash.get("AC_HD_ID"));
            }    
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDepSubNoAccNew;
    private com.see.truetransact.uicomponent.CButton btnDepSubNoAccNew1;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnFromAccount;
    private com.see.truetransact.uicomponent.CButton btnInterestCreditHead;
    private com.see.truetransact.uicomponent.CButton btnInterestDebitHead;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnToAccount;
    private com.see.truetransact.uicomponent.CComboBox cboAccountType;
    private com.see.truetransact.uicomponent.CComboBox cboCategory;
    private com.see.truetransact.uicomponent.CComboBox cboCompounded;
    private com.see.truetransact.uicomponent.CComboBox cboCompoundingType;
    private com.see.truetransact.uicomponent.CComboBox cboDepositsCompounded;
    private com.see.truetransact.uicomponent.CComboBox cboFloatPrecision;
    private com.see.truetransact.uicomponent.CComboBox cboGracePeriod;
    private com.see.truetransact.uicomponent.CComboBox cboInterestPaymentFrequency;
    private com.see.truetransact.uicomponent.CComboBox cboMonth;
    private com.see.truetransact.uicomponent.CComboBox cboPrincipalRoundingValue;
    private com.see.truetransact.uicomponent.CComboBox cboProductID;
    private com.see.truetransact.uicomponent.CComboBox cboRateOption;
    private com.see.truetransact.uicomponent.CComboBox cboRoundingInterest;
    private com.see.truetransact.uicomponent.CComboBox cboRoundingPrincipal;
    private com.see.truetransact.uicomponent.CComboBox cboYear;
    private com.see.truetransact.uicomponent.CLabel lblAccountFrom;
    private com.see.truetransact.uicomponent.CLabel lblAccountTo;
    private com.see.truetransact.uicomponent.CLabel lblAccountType;
    private com.see.truetransact.uicomponent.CLabel lblCategory;
    private com.see.truetransact.uicomponent.CLabel lblCompounded;
    private com.see.truetransact.uicomponent.CLabel lblDays;
    private com.see.truetransact.uicomponent.CLabel lblDepositsInstallment;
    private com.see.truetransact.uicomponent.CLabel lblDepositsInterest;
    private com.see.truetransact.uicomponent.CLabel lblDuration;
    private com.see.truetransact.uicomponent.CLabel lblDuration1;
    private com.see.truetransact.uicomponent.CLabel lblFloatPrecision;
    private com.see.truetransact.uicomponent.CLabel lblFromDate;
    private com.see.truetransact.uicomponent.CLabel lblGracePeriod;
    private com.see.truetransact.uicomponent.CLabel lblInterestCreditHead;
    private com.see.truetransact.uicomponent.CLabel lblInterestDebitHead;
    private com.see.truetransact.uicomponent.CLabel lblInterestOption;
    private com.see.truetransact.uicomponent.CLabel lblMonth;
    private com.see.truetransact.uicomponent.CLabel lblMonths;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblPenalRate;
    private com.see.truetransact.uicomponent.CLabel lblPenalRatePercentage;
    private com.see.truetransact.uicomponent.CLabel lblPercentageOfRate;
    private com.see.truetransact.uicomponent.CLabel lblPeriodOpt;
    private com.see.truetransact.uicomponent.CLabel lblPrincipal;
    private com.see.truetransact.uicomponent.CLabel lblProductID;
    private com.see.truetransact.uicomponent.CLabel lblRateofInterest;
    private com.see.truetransact.uicomponent.CLabel lblRateofInterest1;
    private com.see.truetransact.uicomponent.CLabel lblRoundingInterest;
    private com.see.truetransact.uicomponent.CLabel lblRoundingPrincipal;
    private com.see.truetransact.uicomponent.CLabel lblSpace;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace24;
    private com.see.truetransact.uicomponent.CLabel lblSpace25;
    private com.see.truetransact.uicomponent.CLabel lblSpace26;
    private com.see.truetransact.uicomponent.CLabel lblSpace27;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblToDate;
    private com.see.truetransact.uicomponent.CLabel lblTotalAmount;
    private com.see.truetransact.uicomponent.CLabel lblTotalInterest;
    private com.see.truetransact.uicomponent.CLabel lblYear;
    private com.see.truetransact.uicomponent.CLabel lblYears;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain1;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess1;
    private com.see.truetransact.uicomponent.CPanel panAccount;
    private com.see.truetransact.uicomponent.CPanel panAccountFrom;
    private com.see.truetransact.uicomponent.CPanel panAccountTo;
    private com.see.truetransact.uicomponent.CPanel panCompound;
    private com.see.truetransact.uicomponent.CPanel panCreditHead;
    private com.see.truetransact.uicomponent.CPanel panDebitHead;
    private com.see.truetransact.uicomponent.CPanel panGracePeriod;
    private com.see.truetransact.uicomponent.CPanel panInterest;
    private com.see.truetransact.uicomponent.CPanel panInterestAndPeriod;
    private com.see.truetransact.uicomponent.CPanel panInterestCalculation;
    private com.see.truetransact.uicomponent.CPanel panInterestHead;
    private com.see.truetransact.uicomponent.CPanel panInterestOption;
    private com.see.truetransact.uicomponent.CPanel panInterestOption1;
    private com.see.truetransact.uicomponent.CPanel panMain0;
    private com.see.truetransact.uicomponent.CPanel panMain1;
    private com.see.truetransact.uicomponent.CPanel panPenalRate;
    private com.see.truetransact.uicomponent.CPanel panPeriod;
    private com.see.truetransact.uicomponent.CPanel panPeriodResult;
    private com.see.truetransact.uicomponent.CPanel panRateOfInterest;
    private com.see.truetransact.uicomponent.CPanel panResult;
    private com.see.truetransact.uicomponent.CPanel panRounding;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CRadioButton rdoDiscounted_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoDiscounted_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoInterestOption;
    private com.see.truetransact.uicomponent.CRadioButton rdoInterestOption_Compound;
    private com.see.truetransact.uicomponent.CRadioButton rdoInterestOption_Fixed;
    private com.see.truetransact.uicomponent.CRadioButton rdoInterestOption_Floating;
    private com.see.truetransact.uicomponent.CRadioButton rdoInterestOption_Simple;
    private com.see.truetransact.uicomponent.CButtonGroup rdoPeriodOption;
    private com.see.truetransact.uicomponent.CRadioButton rdoPeriodOption_Date;
    private com.see.truetransact.uicomponent.CRadioButton rdoPeriodOption_Duration;
    private com.see.truetransact.uicomponent.CButtonGroup rdoReport;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private com.see.truetransact.uicomponent.CSeparator sptOperatesLike;
    private com.see.truetransact.uicomponent.CSeparator sptOperatesLike1;
    private com.see.truetransact.uicomponent.CSeparator sptReport;
    private com.see.truetransact.uicomponent.CScrollPane srpPeriodResult;
    private com.see.truetransact.uicomponent.CScrollPane srpResult;
    private com.see.truetransact.uicomponent.CTable tblPeriodResult;
    private com.see.truetransact.uicomponent.CTable tblResult;
    private javax.swing.JToolBar tbrAdvances;
    private com.see.truetransact.uicomponent.CDateField tdtFromDate;
    private com.see.truetransact.uicomponent.CDateField tdtToDate;
    private com.see.truetransact.uicomponent.CTextField txtDays;
    private com.see.truetransact.uicomponent.CTextField txtFromAccount;
    private com.see.truetransact.uicomponent.CTextField txtGracePeriod;
    private com.see.truetransact.uicomponent.CTextField txtInterestCreditHead;
    private com.see.truetransact.uicomponent.CTextField txtInterestDebitHead;
    private com.see.truetransact.uicomponent.CTextField txtMonths;
    private com.see.truetransact.uicomponent.CTextField txtPenalRate;
    private com.see.truetransact.uicomponent.CTextField txtPrincipal;
    private com.see.truetransact.uicomponent.CTextField txtRateofInterest;
    private com.see.truetransact.uicomponent.CTextField txtToAccount;
    private com.see.truetransact.uicomponent.CTextField txtTotalAmount;
    private com.see.truetransact.uicomponent.CTextField txtTotalInterest;
    private com.see.truetransact.uicomponent.CTextField txtYears;
    // End of variables declaration//GEN-END:variables
    
    
    public static void main(String[] arg){
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Throwable th) {
            th.printStackTrace();
        }
        JFrame jf = new JFrame();
        InterestCalculationUI gui = new InterestCalculationUI();
        jf.getContentPane().add(gui);
        jf.setSize(536, 566);
        jf.show();
        gui.show();
    }
}
