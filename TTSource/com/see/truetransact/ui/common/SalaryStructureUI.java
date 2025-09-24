/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * SalaryStructureUI.java
 *
 * Created on May 25, 2004, 5:18 PM
 */

package com.see.truetransact.ui.common;

import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.uicomponent.COptionPane;
import java.util.Date;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JComponent;
import javax.swing.JFrame;
import java.util.List;
import java.text.DecimalFormat;
/**
 *
 * @author  Sathiya
 */

public class SalaryStructureUI extends CInternalFrame implements Observer, UIMandatoryField {
    
    private HashMap mandatoryMap;
    private SalaryStructureOB observable;
    //    private SalaryStructureOB resourceBundle;
    private SalaryStructureMRB objMandatoryRB;
    
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.common.SalaryStructureRB", ProxyParameters.LANGUAGE);
    
    private int viewType=-1;
    private boolean _intSalaryNew = false;
    private boolean _intDANew = false;
    private boolean _intCCANew = false;
    private boolean _intHRANew = false;
    private boolean _intTANew = false;
    private boolean _intMANew = false;
    private boolean _intOANew = false;
    private boolean isFilled = false;
    private boolean salaryStructureSave = false;
    private boolean DASave = false;
    private boolean CCASave = false;
    private boolean HRASave = false;
    private boolean TASave = false;
    private boolean MASave = false;
    private boolean OASave = false;
    private Date curDate = null;
    private boolean selectedSingleRow = false;
    private String fromDateAlert = "From date should not be empty";
    private String gradeAlert = "Grade should not be empty";
    private String designationAlert = "grade should not be empty";
    int rowSelected = -1;
    
    /** Creates new form BeanForm */
    public SalaryStructureUI() {
        initComponents();
        initSetUp();
    }
    private void initSetUp(){
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setHelpMessage();
        setObservable();
        setMaxLength();
        //        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panSalaryStructureInfo);
        allScreensDisable(false);
        ClientUtil.enableDisable(panSalaryStructureInfo,false);
        enableDisableAllscreens(false);
        stagnationEnableDisable(false);
        chkFixedConveyanceVisible(false);
        chkPetrolAllowanceVisible(false);
        chkOAllowanceFixed(false);
        chkOAllowancePercentage(false);
        rdoStagnationIncrement_Yes.setEnabled(false);
        rdoStagnationIncrement_No.setEnabled(false);
        setUp(ClientConstants.ACTIONTYPE_CANCEL,false);
        setButtonEnableDisable();
        initComponentData();
        tabSalaryStructure.resetVisits();
        btnDelete.setEnabled(true);
        
        setOAVisible(false);
        visibleDA(false);
        curDate = ClientUtil.getCurrentDate();
        //        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panLienInfo);
    }
    private void setOAVisible(boolean oAVisible){
        //        cboOAllowanceTypeValue.setVisible(oAVisible);
        //        lblOAllowanceType.setVisible(oAVisible);
        cboOAParameterBasedOnValue.setVisible(oAVisible);
        lblOAParameterBasedon.setVisible(oAVisible);
        cboOASubParameterValue.setVisible(oAVisible);
        lblOASubParameter.setVisible(oAVisible);
        //        panAutoRenewal2.setVisible(oAVisible);
        lblOAFixedAmt.setVisible(oAVisible);
        txtOAFixedAmtValue.setVisible(oAVisible);
        lblOAFixedAmt.setVisible(oAVisible);
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
        lblSalaryStructureSLNO.setName("lblSalaryStructureSLNO");
        lblSalaryStructureSLNOValue.setName("lblSalaryStructureSLNOValue");
        lblSalaryStructureProdId.setName("lblSalaryStructureProdId");
        cboSalaryStructureProdId.setName("cboSalaryStructureProdId");
        lblSalaryStructureFromDate.setName("lblSalaryStructureFromDate");
        lblSalaryStructureFromDateValue.setName("lblSalaryStructureFromDateValue");
        lblSalaryStructureToDate.setName("lblSalaryStructureToDate");
        lblSalaryStructureToDateValue.setName("lblSalaryStructureToDateValue");
        lblSalaryStructureStartingAmt.setName("lblSalaryStructureStartingAmt");
        lblSalaryStructureAmt.setName("lblSalaryStructureAmt");
        lblSalaryStructureIncYear.setName("lblSalaryStructureIncYear");
        btnSalaryStructureNew.setName("btnSalaryStructureNew");
        btnSalaryStructureSave.setName("btnSalaryStructureSave");
        btnSalaryStructureDelete.setName("btnSalaryStructureDelete");
        panSalaryStructureButtons.setName("panSalaryStructureButtons");
        
        lblCCAllowanceSLNO.setName("lblCCAllowanceSLNO");
        lblCCAllowanceSLNOValue.setName("lblCCAllowanceSLNOValue");
        lblCCAllowanceCityType.setName("lblCCAllowanceCityType");
        cboCCAllowanceCityType.setName("cboCCAllowanceCityType");
        lblCCAllowance.setName("lblCCAllowance");
        cboCCAllowance.setName("cboCCAllowance");
        lblCCAllowanceFromDate.setName("lblCCAllowanceFromDate");
        tdtCCAllowanceFromDateValue.setName("tdtCCAllowanceFromDateValue");
        lblCCAllowanceToDate.setName("lblCCAllowanceToDate");
        tdtCCAllowanceToDateValue.setName("tdtCCAllowanceToDateValue");
        lblCCAllowanceStartingAmt.setName("lblCCAllowanceStartingAmt");
        txtCCAllowanceStartingAmtValue.setName("txtCCAllowanceStartingAmtValue");
        txtFromAmount.setName("txtFromAmount");
        txtToAmount.setName("txtToAmount");
        lblFromAmount.setName("lblFromAmount");
        lblToAmount.setName("lblToAmount");
        lblPercentOrFixed.setName("lblPercentOrFixed");
        lblCCAllowanceEndingAmt.setName("lblCCAllowanceEndingAmt");
        txtCCAllowanceEndingAmtValue.setName("txtCCAllowanceEndingAmtValue");
        btnCCAllowanceNew.setName("btnSalaryStructureSave");
        btnCCAllowanceSave.setName("btnSalaryStructureDelete");
        btnCCAllowanceDelete.setName("panSalaryStructureButtons");
        
        lblHRAllowanceSLNO.setName("lblHRAllowanceSLNO");
        lblHRAllowanceSLNOValue.setName("lblHRAllowanceSLNOValue");
        lblHRAllowanceCityType.setName("lblHRAllowanceCityType");
        cboHRAllowanceCityType.setName("cboHRAllowanceCityType");
        lblHRAllowanceDesignation.setName("lblHRAllowanceDesignation");
        cboHRAllowanceDesignation.setName("cboHRAllowanceDesignation");
        lblHRAllowanceFromDate.setName("lblHRAllowanceFromDate");
        tdtHRAllowanceFromDateValue.setName("tdtHRAllowanceFromDateValue");
        lblHRAllowanceToDate.setName("lblHRAllowanceToDate");
        tdtHRAllowanceToDateValue.setName("tdtHRAllowanceToDateValue");
        lblHRAllowanceStartingAmt.setName("lblHRAllowanceStartingAmt");
        txtHRAllowanceStartingAmtValue.setName("txtHRAllowanceStartingAmtValue");
        lblHRAllowanceEndingAmt.setName("lblHRAllowanceEndingAmt");
        txtHRAllowanceEndingAmtValue.setName("txtHRAllowanceEndingAmtValue");
        lblHRAPayable.setName("lblHRAPayable");
        btnHRAllowanceNew.setName("btnHRAllowanceNew");
        btnHRAllowanceSave.setName("btnHRAllowanceSave");
        btnHRAllowanceDelete.setName("btnHRAllowanceDelete");
        
        lblDASLNO.setName("lblDASLNO");
        lblDASLNOValue.setName("lblDASLNOValue");
        lblDADesignation.setName("lblDADesignation");
        cboDADesignationValue.setName("cboDADesignationValue");
        lblDAFromDate.setName("lblDAFromDate");
        tdtDAFromDateValue.setName("tdtDAFromDateValue");
        lblDAToDate.setName("lblDAToDate");
        tdtDAToDateValue.setName("tdtDAToDateValue");
        lblDANoOfPointsPerSlab.setName("lblDANoOfPointsPerSlab");
        txtDANoOfPointsPerSlabValue.setName("txtDANoOfPointsPerSlabValue");
        lblDAIndex.setName("lblDAIndex");
        txtDAIndexValue.setName("txtDAIndexValue");
        lblDATotalNoofSlab.setName("lblDATotalNoofSlab");
        txtDAPercentagePerSlabValue.setName("txtDAPercentagePerSlabValue");
        lblTotalDaPer.setName("lblTotalDaPer");
        txtTotalNoofSlabValue.setName("txtTotalNoofSlabValue");
        lblDAPercentagePerSlab.setName("lblDAPercentagePerSlab");
        txtDATotalDAPercentageValue.setName("txtDATotalDAPercentageValue");
        btnDANew.setName("btnDANew");
        btnDASave.setName("btnDASave");
        btnDADelete.setName("btnDADelete");
        
        lblTravellingAllowanceSLNO.setName("lblTravellingAllowanceSLNO");
        lblTravellingAllowanceSLNOValue.setName("lblTravellingAllowanceSLNOValue");
        lblTravellingAllowanceDesg.setName("lblTravellingAllowanceDesg");
        cboTravellingAllowance.setName("cboTravellingAllowance");
        lblTAFromDate.setName("lblTAFromDate");
        tdtTAFromDateValue.setName("tdtTAFromDateValue");
        lblTAToDate.setName("lblTAToDate");
        tdtTAToDateValue.setName("tdtTAToDateValue");
        lblFixedConveyance.setName("lblFixedConveyance");
        chkFixedConveyance.setName("chkFixedConveyance");
        lblPetrolAllowance.setName("lblPetrolAllowance");
        chkPetrolAllowance.setName("chkPetrolAllowance");
        lblBasicAmtUpto.setName("lblBasicAmtUpto");
        txtBasicAmtUptoValue.setName("txtBasicAmtUptoValue");
        lblConveyancePerMonth.setName("lblConveyancePerMonth");
        txtConveyancePerMonthValue.setName("txtConveyancePerMonthValue");
        lblBasicAmtBeyond.setName("lblBasicAmtBeyond");
        txtBasicAmtBeyondValue.setName("txtBasicAmtBeyondValue");
        lblConveyanceAmt.setName("lblConveyanceAmt");
        txtConveyanceAmtValue.setName("txtConveyanceAmtValue");
        lblNoOflitres.setName("lblNoOflitres");
        txtNooflitresValue.setName("txtNooflitresValue");
        lblPricePerlitre.setName("lblPricePerlitre");
        txtPricePerlitreValue.setName("txtPricePerlitreValue");
        lblTotalConveyanceAmt.setName("lblTotalConveyanceAmt");
        txtTotalConveyanceAmtValue.setName("txtTotalConveyanceAmtValue");
        btnTANew.setName("btnTANew");
        btnTASave.setName("btnTASave");
        btnTADelete.setName("btnTADelete");
        
        lblMASLNO.setName("lblMASLNO");
        lblMASLNOValue.setName("lblMASLNOValue");
        lblMAidDesg.setName("lblMAidDesg");
        cboMAidDesg.setName("cboMAidDesg");
        lblMAFromDate.setName("lblMAFromDate");
        tdtMAidFromDateValue.setName("tdtMAidFromDateValue");
        lblMAToDate.setName("lblMAToDate");
        tdtMAidToDateValue.setName("tdtMAidToDateValue");
        lblMAAmt.setName("lblMAAmt");
        btnMAidNew.setName("btnMAidNew");
        btnMAidSave.setName("btnMAidSave");
        btnMAidDelete.setName("btnMAidDelete");
        
        lblOASLNO.setName("lblOASLNO");
        lblOASLNOValue.setName("lblOASLNOValue");
        lblOADesignation.setName("lblOADesignation");
        cboOADesignationValue.setName("cboOADesignationValue");
        lblOAFromDate.setName("lblOAFromDate");
        tdtOAFromDateValue.setName("tdtOAFromDateValue");
        lblOAToDate.setName("lblOAToDate");
        tdtOAToDateValue.setName("tdtOAToDateValue");
        lblOAllowanceType.setName("lblOAllowanceType");
        cboOAllowanceTypeValue.setName("cboOAllowanceTypeValue");
        lblOAParameterBasedon.setName("lblOAParameterBasedon");
        cboOAParameterBasedOnValue.setName("cboOAParameterBasedOnValue");
        lblOASubParameter.setName("lblOASubParameter");
        cboOASubParameterValue.setName("cboOASubParameterValue");
        lblOAFixed.setName("lblOAFixed");
        chkOAFixedValue.setName("chkOAFixedValue");
        lblOAPecentage.setName("lblOAPecentage");
        chkOAPercentageValue.setName("chkOAPercentageValue");
        lblOAFixedAmt.setName("lblOAFixedAmt");
        txtOAFixedAmtValue.setName("txtOAFixedAmtValue");
        lblOAPercentage.setName("lblOAPercentage");
        txtOAMaximumOfValue.setName("txtOAMaximumOfValue");
        lblOAMaximumOf.setName("lblOAMaximumOf");
        txtOAPercentageValue.setName("txtOAPercentageValue");
        btnMAidNew.setName("btnMAidNew");
        btnMAidSave.setName("btnMAidSave");
        btnMAidDelete.setName("btnMAidDelete");
        
    }
/* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        resourceBundle = new SalaryStructureRB();
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        btnException.setText(resourceBundle.getString("btnException"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblSalaryStructureSLNO.setText(resourceBundle.getString("lblSalaryStructureSLNO"));
        lblSalaryStructureProdId.setText(resourceBundle.getString("lblSalaryStructureProdId"));
        lblSalaryStructureFromDate.setText(resourceBundle.getString("lblSalaryStructureFromDate"));
        lblSalaryStructureStartingAmt.setText(resourceBundle.getString("lblSalaryStructureStartingAmt"));
        lblSalaryStructureToDate.setText(resourceBundle.getString("lblSalaryStructureToDate"));
        lblSalaryStructureAmt.setText(resourceBundle.getString("lblSalaryStructureAmt"));
        lblSalaryStructureIncYear.setText(resourceBundle.getString("lblSalaryStructureIncYear"));
        lblStagnationIncrement.setText(resourceBundle.getString("lblStagnationIncrement"));
        lblSalaryStructureTotNoInc.setText(resourceBundle.getString("lblSalaryStructureTotNoInc"));
        lblSalaryStructureStagnationAmt.setText(resourceBundle.getString("lblSalaryStructureStagnationAmt"));
        lblSalaryStructureNoOfStagnation.setText(resourceBundle.getString("lblSalaryStructureNoOfStagnation"));
        lblSalaryStructureStagnationOnceIn.setText(resourceBundle.getString("lblSalaryStructureStagnationOnceIn"));
        
        lblCCAllowanceSLNO.setText(resourceBundle.getString("lblCCAllowanceSLNO"));
        lblCCAllowanceCityType.setText(resourceBundle.getString("lblCCAllowanceCityType"));
        lblCCAllowance.setText(resourceBundle.getString("lblCCAllowance"));
        lblCCAllowanceFromDate.setText(resourceBundle.getString("lblCCAllowanceFromDate"));
        lblCCAllowanceToDate.setText(resourceBundle.getString("lblCCAllowanceToDate"));
        lblCCAllowanceStartingAmt.setText(resourceBundle.getString("lblCCAllowanceStartingAmt"));
        lblCCAllowanceEndingAmt.setText(resourceBundle.getString("lblCCAllowanceEndingAmt"));
        
        lblHRAllowanceSLNO.setText(resourceBundle.getString("lblHRAllowanceSLNO"));
        lblHRAllowanceCityType.setText(resourceBundle.getString("lblHRAllowanceCityType"));
        lblHRAllowanceDesignation.setText(resourceBundle.getString("lblHRAllowanceDesignation"));
        lblHRAllowanceFromDate.setText(resourceBundle.getString("lblHRAllowanceFromDate"));
        lblHRAllowanceToDate.setText(resourceBundle.getString("lblHRAllowanceToDate"));
        lblHRAllowanceStartingAmt.setText(resourceBundle.getString("lblHRAllowanceStartingAmt"));
        lblHRAllowanceEndingAmt.setText(resourceBundle.getString("lblHRAllowanceEndingAmt"));
        lblHRAPayable.setText(resourceBundle.getString("lblHRAPayable"));
        
        lblDASLNO.setText(resourceBundle.getString("lblDASLNO"));
        lblDADesignation.setText(resourceBundle.getString("lblDADesignation"));
        lblDAFromDate.setText(resourceBundle.getString("lblDAFromDate"));
        lblDAToDate.setText(resourceBundle.getString("lblDAToDate"));
        lblDANoOfPointsPerSlab.setText(resourceBundle.getString("lblDANoOfPointsPerSlab"));
        lblDAIndex.setText(resourceBundle.getString("lblDAIndex"));
        lblDATotalNoofSlab.setText(resourceBundle.getString("lblDATotalNoofSlab"));
        lblTotalDaPer.setText(resourceBundle.getString("lblTotalDaPer"));
        lblDAPercentagePerSlab.setText(resourceBundle.getString("lblDAPercentagePerSlab"));
        
        lblTravellingAllowanceSLNO.setText(resourceBundle.getString("lblTravellingAllowanceSLNO"));
        lblTravellingAllowanceDesg.setText(resourceBundle.getString("lblTravellingAllowanceDesg"));
        lblTAFromDate.setText(resourceBundle.getString("lblTAFromDate"));
        lblTAToDate.setText(resourceBundle.getString("lblTAToDate"));
        lblFixedConveyance.setText(resourceBundle.getString("lblFixedConveyance"));
        lblPetrolAllowance.setText(resourceBundle.getString("lblPetrolAllowance"));
        lblBasicAmtUpto.setText(resourceBundle.getString("lblBasicAmtUpto"));
        lblConveyancePerMonth.setText(resourceBundle.getString("lblConveyancePerMonth"));
        lblBasicAmtBeyond.setText(resourceBundle.getString("lblBasicAmtBeyond"));
        lblConveyanceAmt.setText(resourceBundle.getString("lblConveyanceAmt"));
        lblNoOflitres.setText(resourceBundle.getString("lblNoOflitres"));
        lblPricePerlitre.setText(resourceBundle.getString("lblPricePerlitre"));
        lblTotalConveyanceAmt.setText(resourceBundle.getString("lblTotalConveyanceAmt"));
        
        lblMASLNO.setText(resourceBundle.getString("lblMASLNO"));
        lblMAidDesg.setText(resourceBundle.getString("lblMAidDesg"));
        lblMAFromDate.setText(resourceBundle.getString("lblMAFromDate"));
        lblMAToDate.setText(resourceBundle.getString("lblMAToDate"));
        lblMAAmt.setText(resourceBundle.getString("lblMAAmt"));
        
        lblOASLNO.setText(resourceBundle.getString("lblOASLNO"));
        lblOADesignation.setText(resourceBundle.getString("lblOADesignation"));
        lblOAFromDate.setText(resourceBundle.getString("lblOAFromDate"));
        lblOAToDate.setText(resourceBundle.getString("lblOAToDate"));
        lblOAllowanceType.setText(resourceBundle.getString("lblOAllowanceType"));
        lblOAParameterBasedon.setText(resourceBundle.getString("lblOAParameterBasedon"));
        lblOASubParameter.setText(resourceBundle.getString("lblOASubParameter"));
        lblOAFixed.setText(resourceBundle.getString("lblOAFixed"));
        lblOAPecentage.setText(resourceBundle.getString("lblOAPecentage"));
        lblOAFixedAmt.setText(resourceBundle.getString("lblOAFixedAmt"));
        lblOAPercentage.setText(resourceBundle.getString("lblOAPercentage"));
        lblOAMaximumOf.setText(resourceBundle.getString("lblOAMaximumOf"));
        
    }
/* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        objMandatoryRB = new SalaryStructureMRB();
        cboSalaryStructureProdId.setHelpMessage(lblMsg, objMandatoryRB.getString("cboSalaryStructureProdId"));
        lblSalaryStructureFromDateValue.setHelpMessage(lblMsg, objMandatoryRB.getString("lblSalaryStructureFromDateValue"));
        lblSalaryStructureToDateValue.setHelpMessage(lblMsg, objMandatoryRB.getString("lblSalaryStructureToDateValue"));
        txtSalaryStructureStartingAmtValue.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSalaryStructureStartingAmtValue"));
        txtSalaryStructureEndingAmtValue.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSalaryStructureEndingAmtValue"));
        txtSalaryStructureAmtValue.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSalaryStructureAmtValue"));
        txtSalaryStructureIncYearValue.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSalaryStructureIncYearValue"));
        txtSalaryStructureTotNoIncValue.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSalaryStructureTotNoIncValue"));
        txtSalaryStructureStagnationAmtValue.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSalaryStructureStagnationAmtValue"));
        txtSalaryStructureNoOfStagnationValue.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSalaryStructureNoOfStagnationValue"));
        txtSalaryStructureStagnationOnceInValue.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSalaryStructureStagnationOnceInValue"));
        cboSalaryStructureStagnationOnceIn.setHelpMessage(lblMsg, objMandatoryRB.getString("cboSalaryStructureStagnationOnceIn"));
        
        cboCCAllowanceCityType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCCAllowanceCityType"));
        cboCCAllowance.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCCAllowance"));
        tdtCCAllowanceFromDateValue.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtCCAllowanceFromDateValue"));
        tdtCCAllowanceToDateValue.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtCCAllowanceToDateValue"));
        txtCCAllowanceStartingAmtValue.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCCAllowanceStartingAmtValue"));
        txtFromAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtFromAmount"));
        txtToAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtToAmount"));
        txtCCAllowanceEndingAmtValue.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCCAllowanceEndingAmtValue"));
        
        cboHRAllowanceCityType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboHRAllowanceCityType"));
        cboHRAllowanceDesignation.setHelpMessage(lblMsg, objMandatoryRB.getString("cboHRAllowanceDesignation"));
        tdtHRAllowanceFromDateValue.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtHRAllowanceFromDateValue"));
        tdtHRAllowanceToDateValue.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtHRAllowanceToDateValue"));
        txtHRAllowanceStartingAmtValue.setHelpMessage(lblMsg, objMandatoryRB.getString("txtHRAllowanceStartingAmtValue"));
        txtHRAllowanceEndingAmtValue.setHelpMessage(lblMsg, objMandatoryRB.getString("txtHRAllowanceEndingAmtValue"));
        
        cboDADesignationValue.setHelpMessage(lblMsg, objMandatoryRB.getString("cboDADesignationValue"));
        tdtDAFromDateValue.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtDAFromDateValue"));
        tdtDAToDateValue.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtDAToDateValue"));
        txtDANoOfPointsPerSlabValue.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDANoOfPointsPerSlabValue"));
        txtDAIndexValue.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDAIndexValue"));
        txtDAPercentagePerSlabValue.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDAPercentagePerSlabValue"));
        txtTotalNoofSlabValue.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTotalNoofSlabValue"));
        txtDATotalDAPercentageValue.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDATotalDAPercentageValue"));
        
        cboTravellingAllowance.setHelpMessage(lblMsg, objMandatoryRB.getString("cboTravellingAllowance"));
        tdtTAFromDateValue.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtTAFromDateValue"));
        tdtTAToDateValue.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtTAToDateValue"));
        //        chkFixedConveyance.setHelpMessage(lblMsg, objMandatoryRB.getString("chkFixedConveyance"));
        //        chkPetrolAllowance.setHelpMessage(lblMsg, objMandatoryRB.getString("chkPetrolAllowance"));
        txtBasicAmtUptoValue.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBasicAmtUptoValue"));
        txtConveyancePerMonthValue.setHelpMessage(lblMsg, objMandatoryRB.getString("txtConveyancePerMonthValue"));
        txtBasicAmtBeyondValue.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBasicAmtBeyondValue"));
        txtConveyanceAmtValue.setHelpMessage(lblMsg, objMandatoryRB.getString("txtConveyanceAmtValue"));
        txtNooflitresValue.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNooflitresValue"));
        txtPricePerlitreValue.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPricePerlitreValue"));
        txtTotalConveyanceAmtValue.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTotalConveyanceAmtValue"));
        
        cboMAidDesg.setHelpMessage(lblMsg, objMandatoryRB.getString("cboMAidDesg"));
        tdtMAidFromDateValue.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtMAidFromDateValue"));
        tdtMAidToDateValue.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtMAidToDateValue"));
        txtMAidAmtValue.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMAidAmtValue"));
        
        cboOADesignationValue.setHelpMessage(lblMsg, objMandatoryRB.getString("cboOADesignationValue"));
        tdtOAFromDateValue.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtOAFromDateValue"));
        tdtOAToDateValue.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtOAToDateValue"));
        cboOAllowanceTypeValue.setHelpMessage(lblMsg, objMandatoryRB.getString("cboOAllowanceTypeValue"));
        cboOAParameterBasedOnValue.setHelpMessage(lblMsg, objMandatoryRB.getString("cboOAParameterBasedOnValue"));
        cboOASubParameterValue.setHelpMessage(lblMsg, objMandatoryRB.getString("cboOASubParameterValue"));
        chkOAFixedValue.setHelpMessage(lblMsg, objMandatoryRB.getString("chkOAFixedValue"));
        chkOAPercentageValue.setHelpMessage(lblMsg, objMandatoryRB.getString("chkOAPercentageValue"));
        txtOAFixedAmtValue.setHelpMessage(lblMsg, objMandatoryRB.getString("txtOAFixedAmtValue"));
        txtOAMaximumOfValue.setHelpMessage(lblMsg, objMandatoryRB.getString("txtOAMaximumOfValue"));
        txtOAPercentageValue.setHelpMessage(lblMsg, objMandatoryRB.getString("txtOAPercentageValue"));
        
    }
    private void initComponentData(){
        this.cboSalaryStructureProdId.setModel(observable.getCbmSalaryStructureProdId());
        this.cboSalaryStructureStagnationOnceIn.setModel(observable.getCbmSalaryStructureStagnationOnceIn());
        this.cboDADesignationValue.setModel(observable.getCbmDAValue());
        this.cboCCAllowanceCityType.setModel(observable.getCbmCCAllowanceCityType());
        this.cboCCAllowance.setModel(observable.getCbmCCAllowance());
        this.cboHRAllowanceCityType.setModel(observable.getCbmHRAllowanceCityType());
        this.cboHRAllowanceDesignation.setModel(observable.getCbmHRAllowanceDesignation());
        this.cboTravellingAllowance.setModel(observable.getCbmTAllowance());
        this.cboMAidDesg.setModel(observable.getCbmMAidDesg());
        
        this.cboOADesignationValue.setModel(observable.getCbmOADesignationValue());
        this.cboOAllowanceTypeValue.setModel(observable.getCbmOAllowanceTypeValue());
        this.cboOAParameterBasedOnValue.setModel(observable.getCbmOAParameterBasedOnValue());
        this.cboOASubParameterValue.setModel(observable.getCbmOASubParameterValue());
        
        //        this.cboTravellingAllowanceDesg.setModel(observable.getCbmTAllowanceCityType());
    }
    private void setObservable() {
        observable = SalaryStructureOB.getInstance();
        observable.addObserver(this);
    }
    public void setUp(int actionType,boolean isEnable) {
        ClientUtil.enableDisable(this, isEnable);
        
        observable.setActionType(actionType);
        observable.setStatus();
    }
    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(btnNew.isEnabled());
        //        btnDelete.setEnabled(btnNew.isEnabled());
        
        btnAuthorize.setEnabled(btnNew.isEnabled());
        btnReject.setEnabled(btnNew.isEnabled());
        btnException.setEnabled(btnNew.isEnabled());
        
        btnSave.setEnabled(!btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        //        mitDelete.setEnabled(btnDelete.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
    }
    private void setMaxLength(){
        //salary structure
        txtSalaryStructureEndingAmtValue.setValidation(new CurrencyValidation(14,2));
        txtSalaryStructureStartingAmtValue.setValidation(new CurrencyValidation(14,2));
        txtSalaryStructureAmtValue.setValidation(new CurrencyValidation(14,2));
        txtSalaryStructureIncYearValue.setValidation(new NumericValidation(3,3));
        txtSalaryStructureTotNoIncValue.setValidation(new NumericValidation(3,3));
        txtSalaryStructureStagnationAmtValue.setValidation(new CurrencyValidation(14,2));
        txtSalaryStructureNoOfStagnationValue.setValidation(new NumericValidation(3,3));
        txtSalaryStructureStagnationOnceInValue.setValidation(new NumericValidation(3,3));
        //dearness allowances
        txtDANoOfPointsPerSlabValue.setValidation(new NumericValidation(3,3));
        txtDAIndexValue.setValidation(new NumericValidation(14,2));
        txtDAPercentagePerSlabValue.setValidation(new NumericValidation(3,3));
        txtTotalNoofSlabValue.setValidation(new NumericValidation(14,2));
        txtDATotalDAPercentageValue.setValidation(new NumericValidation(14,2));
        //ccallowances
        txtCCAllowanceStartingAmtValue.setValidation(new NumericValidation(14,2));
        txtFromAmount.setValidation(new CurrencyValidation(14,2));
        txtToAmount.setValidation(new CurrencyValidation(14,2));
        txtCCAllowanceEndingAmtValue.setValidation(new CurrencyValidation(14,2));
        //House Rent allowances
        txtHRAllowanceStartingAmtValue.setValidation(new NumericValidation(14,2));
        txtHRAllowanceEndingAmtValue.setValidation(new CurrencyValidation(14,2));
        //TravellingAllowances
        txtBasicAmtUptoValue.setValidation(new NumericValidation(14,2));
        txtConveyancePerMonthValue.setValidation(new NumericValidation(14,2));
        txtBasicAmtBeyondValue.setValidation(new NumericValidation(14,2));
        txtConveyanceAmtValue.setValidation(new NumericValidation(14,2));
        txtNooflitresValue.setValidation(new NumericValidation(14,2));
        txtPricePerlitreValue.setValidation(new NumericValidation(14,2));
        txtTotalConveyanceAmtValue.setValidation(new NumericValidation(14,2));
        //Medical Allowances
        txtMAidAmtValue.setValidation(new CurrencyValidation(14,2));
        
        //other allowances
        txtOAFixedAmtValue.setValidation(new CurrencyValidation(14,2));
        txtOAPercentageValue.setValidation(new CurrencyValidation(14,2));
        txtOAMaximumOfValue.setValidation(new NumericValidation(14,2));
        
    }
    
    private void addRadioButton(){
        rdgStagnationIncrement.add(rdoStagnationIncrement_Yes);
        rdgStagnationIncrement.add(rdoStagnationIncrement_No);
        rdgHRAPayable.add(rdoHRAPayable_Yes);
        rdgHRAPayable.add(rdoHRAPayable_No);
    }
    
    private void removeRadioButton(){
        rdgStagnationIncrement.remove(rdoStagnationIncrement_No);
        rdgStagnationIncrement.remove(rdoStagnationIncrement_No);
        rdgHRAPayable.remove(rdoHRAPayable_Yes);
        rdgHRAPayable.remove(rdoHRAPayable_No);
    }
    /** This method is called from within the constructor to
     * initialize the form.
     *
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdgStagnationIncrement = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgHRAPayable = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgPercentOrFixed = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgBasedOnParameter = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgIndexOrPercent = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrSalaryStructure = new javax.swing.JToolBar();
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
        panSalaryStructure = new com.see.truetransact.uicomponent.CPanel();
        tabSalaryStructure = new com.see.truetransact.uicomponent.CTabbedPane();
        panSalaryDetails = new com.see.truetransact.uicomponent.CPanel();
        panSalaryStructureInfo = new com.see.truetransact.uicomponent.CPanel();
        lblSalaryStructureSLNO = new com.see.truetransact.uicomponent.CLabel();
        lblSalaryStructureSLNOValue = new com.see.truetransact.uicomponent.CLabel();
        lblSalaryStructureProdId = new com.see.truetransact.uicomponent.CLabel();
        cboSalaryStructureProdId = new com.see.truetransact.uicomponent.CComboBox();
        lblSalaryStructureFromDate = new com.see.truetransact.uicomponent.CLabel();
        lblSalaryStructureFromDateValue = new com.see.truetransact.uicomponent.CDateField();
        lblSalaryStructureToDate = new com.see.truetransact.uicomponent.CLabel();
        lblSalaryStructureToDateValue = new com.see.truetransact.uicomponent.CDateField();
        lblSalaryStructureStartingAmt = new com.see.truetransact.uicomponent.CLabel();
        txtSalaryStructureStartingAmtValue = new com.see.truetransact.uicomponent.CTextField();
        lblSalaryStructureEndingAmt = new com.see.truetransact.uicomponent.CLabel();
        txtSalaryStructureEndingAmtValue = new com.see.truetransact.uicomponent.CTextField();
        lblSalaryStructureAmt = new com.see.truetransact.uicomponent.CLabel();
        txtSalaryStructureAmtValue = new com.see.truetransact.uicomponent.CTextField();
        lblSalaryStructureIncYear = new com.see.truetransact.uicomponent.CLabel();
        txtSalaryStructureIncYearValue = new com.see.truetransact.uicomponent.CTextField();
        lblStagnationIncrement = new com.see.truetransact.uicomponent.CLabel();
        panAutoRenewal = new com.see.truetransact.uicomponent.CPanel();
        rdoStagnationIncrement_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoStagnationIncrement_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblSalaryStructureTotNoInc = new com.see.truetransact.uicomponent.CLabel();
        txtSalaryStructureTotNoIncValue = new com.see.truetransact.uicomponent.CTextField();
        lblSalaryStructureStagnationAmt = new com.see.truetransact.uicomponent.CLabel();
        txtSalaryStructureStagnationAmtValue = new com.see.truetransact.uicomponent.CTextField();
        lblSalaryStructureNoOfStagnation = new com.see.truetransact.uicomponent.CLabel();
        txtSalaryStructureNoOfStagnationValue = new com.see.truetransact.uicomponent.CTextField();
        lblSalaryStructureStagnationOnceIn = new com.see.truetransact.uicomponent.CLabel();
        panSalaryStructureButtons = new com.see.truetransact.uicomponent.CPanel();
        btnSalaryStructureNew = new com.see.truetransact.uicomponent.CButton();
        btnSalaryStructureSave = new com.see.truetransact.uicomponent.CButton();
        btnSalaryStructureDelete = new com.see.truetransact.uicomponent.CButton();
        lblSalaryStructureStagnationAmt1 = new com.see.truetransact.uicomponent.CLabel();
        panCreditingACNo = new com.see.truetransact.uicomponent.CPanel();
        txtSalaryStructureStagnationOnceInValue = new com.see.truetransact.uicomponent.CTextField();
        cboSalaryStructureStagnationOnceIn = new com.see.truetransact.uicomponent.CComboBox();
        panSalaryStructureTable = new com.see.truetransact.uicomponent.CPanel();
        srpSalaryStructure = new com.see.truetransact.uicomponent.CScrollPane();
        tblSalaryStructure = new com.see.truetransact.uicomponent.CTable();
        panCCAllowance = new com.see.truetransact.uicomponent.CPanel();
        panCCAllowanceTable = new com.see.truetransact.uicomponent.CPanel();
        srpCCAllowance = new com.see.truetransact.uicomponent.CScrollPane();
        tblCCAllowance = new com.see.truetransact.uicomponent.CTable();
        panCCAllowanceInfo = new com.see.truetransact.uicomponent.CPanel();
        lblCCAllowanceSLNO = new com.see.truetransact.uicomponent.CLabel();
        lblCCAllowanceSLNOValue = new com.see.truetransact.uicomponent.CLabel();
        lblCCAllowanceCityType = new com.see.truetransact.uicomponent.CLabel();
        cboCCAllowanceCityType = new com.see.truetransact.uicomponent.CComboBox();
        lblCCAllowanceFromDate = new com.see.truetransact.uicomponent.CLabel();
        tdtCCAllowanceFromDateValue = new com.see.truetransact.uicomponent.CDateField();
        lblCCAllowanceToDate = new com.see.truetransact.uicomponent.CLabel();
        tdtCCAllowanceToDateValue = new com.see.truetransact.uicomponent.CDateField();
        lblCCAllowanceStartingAmt = new com.see.truetransact.uicomponent.CLabel();
        txtCCAllowanceStartingAmtValue = new com.see.truetransact.uicomponent.CTextField();
        lblCCAllowanceEndingAmt = new com.see.truetransact.uicomponent.CLabel();
        txtCCAllowanceEndingAmtValue = new com.see.truetransact.uicomponent.CTextField();
        lblCCAllowance = new com.see.truetransact.uicomponent.CLabel();
        cboCCAllowance = new com.see.truetransact.uicomponent.CComboBox();
        panCCAllowanceButtons = new com.see.truetransact.uicomponent.CPanel();
        btnCCAllowanceNew = new com.see.truetransact.uicomponent.CButton();
        btnCCAllowanceSave = new com.see.truetransact.uicomponent.CButton();
        btnCCAllowanceDelete = new com.see.truetransact.uicomponent.CButton();
        panPercentOrFixed = new com.see.truetransact.uicomponent.CPanel();
        rdoPercentOrFixed_Percent = new com.see.truetransact.uicomponent.CRadioButton();
        rdoPercentOrFixed_Fixed = new com.see.truetransact.uicomponent.CRadioButton();
        lblPercentOrFixed = new com.see.truetransact.uicomponent.CLabel();
        txtFromAmount = new com.see.truetransact.uicomponent.CTextField();
        txtToAmount = new com.see.truetransact.uicomponent.CTextField();
        lblToAmount = new com.see.truetransact.uicomponent.CLabel();
        lblFromAmount = new com.see.truetransact.uicomponent.CLabel();
        panHRAllowance = new com.see.truetransact.uicomponent.CPanel();
        panHRAllowanceTable = new com.see.truetransact.uicomponent.CPanel();
        srpHRAllowance = new com.see.truetransact.uicomponent.CScrollPane();
        tblHRAllowance = new com.see.truetransact.uicomponent.CTable();
        panHRAllowanceInfo = new com.see.truetransact.uicomponent.CPanel();
        lblHRAllowanceSLNO = new com.see.truetransact.uicomponent.CLabel();
        lblHRAllowanceSLNOValue = new com.see.truetransact.uicomponent.CLabel();
        lblHRAllowanceCityType = new com.see.truetransact.uicomponent.CLabel();
        cboHRAllowanceCityType = new com.see.truetransact.uicomponent.CComboBox();
        lblHRAllowanceFromDate = new com.see.truetransact.uicomponent.CLabel();
        tdtHRAllowanceFromDateValue = new com.see.truetransact.uicomponent.CDateField();
        lblHRAllowanceToDate = new com.see.truetransact.uicomponent.CLabel();
        tdtHRAllowanceToDateValue = new com.see.truetransact.uicomponent.CDateField();
        lblHRAllowanceStartingAmt = new com.see.truetransact.uicomponent.CLabel();
        txtHRAllowanceStartingAmtValue = new com.see.truetransact.uicomponent.CTextField();
        lblHRAllowanceEndingAmt = new com.see.truetransact.uicomponent.CLabel();
        txtHRAllowanceEndingAmtValue = new com.see.truetransact.uicomponent.CTextField();
        lblHRAllowanceDesignation = new com.see.truetransact.uicomponent.CLabel();
        cboHRAllowanceDesignation = new com.see.truetransact.uicomponent.CComboBox();
        panHRAllowanceButtons = new com.see.truetransact.uicomponent.CPanel();
        btnHRAllowanceNew = new com.see.truetransact.uicomponent.CButton();
        btnHRAllowanceSave = new com.see.truetransact.uicomponent.CButton();
        btnHRAllowanceDelete = new com.see.truetransact.uicomponent.CButton();
        lblHRAPayable = new com.see.truetransact.uicomponent.CLabel();
        panAutoRenewal1 = new com.see.truetransact.uicomponent.CPanel();
        rdoHRAPayable_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoHRAPayable_No = new com.see.truetransact.uicomponent.CRadioButton();
        panDAllowances = new com.see.truetransact.uicomponent.CPanel();
        panDAInfo = new com.see.truetransact.uicomponent.CPanel();
        lblDASLNO = new com.see.truetransact.uicomponent.CLabel();
        lblDASLNOValue = new com.see.truetransact.uicomponent.CLabel();
        lblDAFromDate = new com.see.truetransact.uicomponent.CLabel();
        tdtDAFromDateValue = new com.see.truetransact.uicomponent.CDateField();
        lblDAToDate = new com.see.truetransact.uicomponent.CLabel();
        tdtDAToDateValue = new com.see.truetransact.uicomponent.CDateField();
        lblDANoOfPointsPerSlab = new com.see.truetransact.uicomponent.CLabel();
        txtDANoOfPointsPerSlabValue = new com.see.truetransact.uicomponent.CTextField();
        lblDAIndex = new com.see.truetransact.uicomponent.CLabel();
        txtDAIndexValue = new com.see.truetransact.uicomponent.CTextField();
        lblDADesignation = new com.see.truetransact.uicomponent.CLabel();
        cboDADesignationValue = new com.see.truetransact.uicomponent.CComboBox();
        panDAButtons = new com.see.truetransact.uicomponent.CPanel();
        btnDANew = new com.see.truetransact.uicomponent.CButton();
        btnDASave = new com.see.truetransact.uicomponent.CButton();
        btnDADelete = new com.see.truetransact.uicomponent.CButton();
        lblDAPercentagePerSlab = new com.see.truetransact.uicomponent.CLabel();
        lblTotalDaPer = new com.see.truetransact.uicomponent.CLabel();
        txtDATotalDAPercentageValue = new com.see.truetransact.uicomponent.CTextField();
        txtTotalNoofSlabValue = new com.see.truetransact.uicomponent.CTextField();
        lblDATotalNoofSlab = new com.see.truetransact.uicomponent.CLabel();
        txtDAPercentagePerSlabValue = new com.see.truetransact.uicomponent.CTextField();
        panIndexOrPercent = new com.see.truetransact.uicomponent.CPanel();
        rdoIndexOrPercent_Percent = new com.see.truetransact.uicomponent.CRadioButton();
        rdoIndexOrPercent_Index = new com.see.truetransact.uicomponent.CRadioButton();
        lblPercentOrFixed1 = new com.see.truetransact.uicomponent.CLabel();
        panDATable = new com.see.truetransact.uicomponent.CPanel();
        srpDA = new com.see.truetransact.uicomponent.CScrollPane();
        tblDA = new com.see.truetransact.uicomponent.CTable();
        panTravellingAllowance = new com.see.truetransact.uicomponent.CPanel();
        panTravellingAllowanceInfo = new com.see.truetransact.uicomponent.CPanel();
        lblTravellingAllowanceSLNO = new com.see.truetransact.uicomponent.CLabel();
        lblTravellingAllowanceSLNOValue = new com.see.truetransact.uicomponent.CLabel();
        lblTravellingAllowanceDesg = new com.see.truetransact.uicomponent.CLabel();
        cboTravellingAllowance = new com.see.truetransact.uicomponent.CComboBox();
        lblTAFromDate = new com.see.truetransact.uicomponent.CLabel();
        tdtTAFromDateValue = new com.see.truetransact.uicomponent.CDateField();
        lblTAToDate = new com.see.truetransact.uicomponent.CLabel();
        tdtTAToDateValue = new com.see.truetransact.uicomponent.CDateField();
        lblPricePerlitre = new com.see.truetransact.uicomponent.CLabel();
        txtPricePerlitreValue = new com.see.truetransact.uicomponent.CTextField();
        lblTotalConveyanceAmt = new com.see.truetransact.uicomponent.CLabel();
        txtTotalConveyanceAmtValue = new com.see.truetransact.uicomponent.CTextField();
        panTravellingAllowanceButtons = new com.see.truetransact.uicomponent.CPanel();
        btnTANew = new com.see.truetransact.uicomponent.CButton();
        btnTASave = new com.see.truetransact.uicomponent.CButton();
        btnTADelete = new com.see.truetransact.uicomponent.CButton();
        txtNooflitresValue = new com.see.truetransact.uicomponent.CTextField();
        lblNoOflitres = new com.see.truetransact.uicomponent.CLabel();
        txtConveyancePerMonthValue = new com.see.truetransact.uicomponent.CTextField();
        lblConveyancePerMonth = new com.see.truetransact.uicomponent.CLabel();
        lblBasicAmtUpto = new com.see.truetransact.uicomponent.CLabel();
        txtBasicAmtUptoValue = new com.see.truetransact.uicomponent.CTextField();
        lblBasicAmtBeyond = new com.see.truetransact.uicomponent.CLabel();
        txtBasicAmtBeyondValue = new com.see.truetransact.uicomponent.CTextField();
        lblConveyanceAmt = new com.see.truetransact.uicomponent.CLabel();
        txtConveyanceAmtValue = new com.see.truetransact.uicomponent.CTextField();
        panAutoRenewal3 = new com.see.truetransact.uicomponent.CPanel();
        chkPetrolAllowance = new com.see.truetransact.uicomponent.CCheckBox();
        chkFixedConveyance = new com.see.truetransact.uicomponent.CCheckBox();
        lblFixedConveyance = new com.see.truetransact.uicomponent.CLabel();
        lblPetrolAllowance = new com.see.truetransact.uicomponent.CLabel();
        panTravellingAllowanceTable = new com.see.truetransact.uicomponent.CPanel();
        srpTravellingAllowance = new com.see.truetransact.uicomponent.CScrollPane();
        tblTravellingAllowance = new com.see.truetransact.uicomponent.CTable();
        panMedicalAllowance = new com.see.truetransact.uicomponent.CPanel();
        panMedicalAllowanceInfo = new com.see.truetransact.uicomponent.CPanel();
        lblMASLNO = new com.see.truetransact.uicomponent.CLabel();
        lblMASLNOValue = new com.see.truetransact.uicomponent.CLabel();
        lblMAidDesg = new com.see.truetransact.uicomponent.CLabel();
        cboMAidDesg = new com.see.truetransact.uicomponent.CComboBox();
        lblMAFromDate = new com.see.truetransact.uicomponent.CLabel();
        tdtMAidFromDateValue = new com.see.truetransact.uicomponent.CDateField();
        lblMAToDate = new com.see.truetransact.uicomponent.CLabel();
        tdtMAidToDateValue = new com.see.truetransact.uicomponent.CDateField();
        lblMAAmt = new com.see.truetransact.uicomponent.CLabel();
        txtMAidAmtValue = new com.see.truetransact.uicomponent.CTextField();
        lblTravellingAllowanceEndingAmt2 = new com.see.truetransact.uicomponent.CLabel();
        txtTravellingAllowanceEndingAmtValue2 = new com.see.truetransact.uicomponent.CTextField();
        panMAidButtons = new com.see.truetransact.uicomponent.CPanel();
        btnMAidNew = new com.see.truetransact.uicomponent.CButton();
        btnMAidSave = new com.see.truetransact.uicomponent.CButton();
        btnMAidDelete = new com.see.truetransact.uicomponent.CButton();
        panMAid = new com.see.truetransact.uicomponent.CPanel();
        srpMAid = new com.see.truetransact.uicomponent.CScrollPane();
        tblMaid = new com.see.truetransact.uicomponent.CTable();
        panOtherAllowances = new com.see.truetransact.uicomponent.CPanel();
        panOAllowanceInfo = new com.see.truetransact.uicomponent.CPanel();
        lblOASLNO = new com.see.truetransact.uicomponent.CLabel();
        lblOASLNOValue = new com.see.truetransact.uicomponent.CLabel();
        lblOADesignation = new com.see.truetransact.uicomponent.CLabel();
        cboOADesignationValue = new com.see.truetransact.uicomponent.CComboBox();
        lblOAFromDate = new com.see.truetransact.uicomponent.CLabel();
        tdtOAFromDateValue = new com.see.truetransact.uicomponent.CDateField();
        lblOAToDate = new com.see.truetransact.uicomponent.CLabel();
        tdtOAToDateValue = new com.see.truetransact.uicomponent.CDateField();
        panOAButtons = new com.see.truetransact.uicomponent.CPanel();
        btnOANew = new com.see.truetransact.uicomponent.CButton();
        btnOASave = new com.see.truetransact.uicomponent.CButton();
        btnOADelete = new com.see.truetransact.uicomponent.CButton();
        txtOAPercentageValue = new com.see.truetransact.uicomponent.CTextField();
        lblOAPercentage = new com.see.truetransact.uicomponent.CLabel();
        lblOAFixedAmt = new com.see.truetransact.uicomponent.CLabel();
        txtOAFixedAmtValue = new com.see.truetransact.uicomponent.CTextField();
        lblOAParameterBasedon = new com.see.truetransact.uicomponent.CLabel();
        lblOAMaximumOf = new com.see.truetransact.uicomponent.CLabel();
        txtOAMaximumOfValue = new com.see.truetransact.uicomponent.CTextField();
        cboOAParameterBasedOnValue = new com.see.truetransact.uicomponent.CComboBox();
        lblOASubParameter = new com.see.truetransact.uicomponent.CLabel();
        cboOAllowanceTypeValue = new com.see.truetransact.uicomponent.CComboBox();
        lblOAllowanceType = new com.see.truetransact.uicomponent.CLabel();
        cboOASubParameterValue = new com.see.truetransact.uicomponent.CComboBox();
        panAutoRenewal2 = new com.see.truetransact.uicomponent.CPanel();
        lblOAFixed = new com.see.truetransact.uicomponent.CLabel();
        chkOAFixedValue = new com.see.truetransact.uicomponent.CCheckBox();
        lblOAPecentage = new com.see.truetransact.uicomponent.CLabel();
        chkOAPercentageValue = new com.see.truetransact.uicomponent.CCheckBox();
        lblBasedOnParameter = new com.see.truetransact.uicomponent.CLabel();
        panRdoBasedOnParameter = new com.see.truetransact.uicomponent.CPanel();
        rdoBasedOnParameter_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoBasedOnParameter_No = new com.see.truetransact.uicomponent.CRadioButton();
        panOAllowances = new com.see.truetransact.uicomponent.CPanel();
        srpOAllowances = new com.see.truetransact.uicomponent.CScrollPane();
        tblOAllowances = new com.see.truetransact.uicomponent.CTable();
        mbrMain = new com.see.truetransact.uicomponent.CMenuBar();
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
        setMinimumSize(new java.awt.Dimension(850, 525));
        setPreferredSize(new java.awt.Dimension(850, 525));

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
        tbrSalaryStructure.add(btnView);

        lblSpace5.setText("     ");
        tbrSalaryStructure.add(lblSpace5);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrSalaryStructure.add(btnNew);

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrSalaryStructure.add(lblSpace29);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrSalaryStructure.add(btnEdit);

        lblSpace30.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace30.setText("     ");
        lblSpace30.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace30.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrSalaryStructure.add(lblSpace30);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrSalaryStructure.add(btnDelete);

        lblSpace2.setText("     ");
        tbrSalaryStructure.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrSalaryStructure.add(btnSave);

        lblSpace31.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace31.setText("     ");
        lblSpace31.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace31.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrSalaryStructure.add(lblSpace31);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrSalaryStructure.add(btnCancel);

        lblSpace3.setText("     ");
        tbrSalaryStructure.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrSalaryStructure.add(btnAuthorize);

        lblSpace32.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace32.setText("     ");
        lblSpace32.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace32.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace32.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrSalaryStructure.add(lblSpace32);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrSalaryStructure.add(btnException);

        lblSpace33.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace33.setText("     ");
        lblSpace33.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace33.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace33.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrSalaryStructure.add(lblSpace33);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject\n");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrSalaryStructure.add(btnReject);

        lblSpace4.setText("     ");
        tbrSalaryStructure.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrSalaryStructure.add(btnPrint);

        lblSpace34.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace34.setText("     ");
        lblSpace34.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrSalaryStructure.add(lblSpace34);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrSalaryStructure.add(btnClose);

        getContentPane().add(tbrSalaryStructure, java.awt.BorderLayout.NORTH);

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

        panSalaryStructure.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panSalaryStructure.setMinimumSize(new java.awt.Dimension(860, 400));
        panSalaryStructure.setPreferredSize(new java.awt.Dimension(860, 400));
        panSalaryStructure.setLayout(new java.awt.GridBagLayout());

        tabSalaryStructure.setMinimumSize(new java.awt.Dimension(840, 650));
        tabSalaryStructure.setName("");
        tabSalaryStructure.setPreferredSize(new java.awt.Dimension(840, 650));

        panSalaryDetails.setMinimumSize(new java.awt.Dimension(750, 230));
        panSalaryDetails.setPreferredSize(new java.awt.Dimension(750, 230));
        panSalaryDetails.setLayout(new java.awt.GridBagLayout());

        panSalaryStructureInfo.setMinimumSize(new java.awt.Dimension(450, 400));
        panSalaryStructureInfo.setPreferredSize(new java.awt.Dimension(450, 400));
        panSalaryStructureInfo.setLayout(new java.awt.GridBagLayout());

        lblSalaryStructureSLNO.setText("SL No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panSalaryStructureInfo.add(lblSalaryStructureSLNO, gridBagConstraints);

        lblSalaryStructureSLNOValue.setMaximumSize(new java.awt.Dimension(150, 16));
        lblSalaryStructureSLNOValue.setMinimumSize(new java.awt.Dimension(150, 16));
        lblSalaryStructureSLNOValue.setPreferredSize(new java.awt.Dimension(150, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panSalaryStructureInfo.add(lblSalaryStructureSLNOValue, gridBagConstraints);

        lblSalaryStructureProdId.setText("Designation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panSalaryStructureInfo.add(lblSalaryStructureProdId, gridBagConstraints);

        cboSalaryStructureProdId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboSalaryStructureProdId.setPopupWidth(250);
        cboSalaryStructureProdId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboSalaryStructureProdIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panSalaryStructureInfo.add(cboSalaryStructureProdId, gridBagConstraints);

        lblSalaryStructureFromDate.setText("From Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panSalaryStructureInfo.add(lblSalaryStructureFromDate, gridBagConstraints);

        lblSalaryStructureFromDateValue.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                lblSalaryStructureFromDateValueFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panSalaryStructureInfo.add(lblSalaryStructureFromDateValue, gridBagConstraints);

        lblSalaryStructureToDate.setText("To Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panSalaryStructureInfo.add(lblSalaryStructureToDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panSalaryStructureInfo.add(lblSalaryStructureToDateValue, gridBagConstraints);

        lblSalaryStructureStartingAmt.setText("Scale Starting Basic Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panSalaryStructureInfo.add(lblSalaryStructureStartingAmt, gridBagConstraints);

        txtSalaryStructureStartingAmtValue.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSalaryStructureStartingAmtValue.setValidation(new CurrencyValidation(14,2));
        txtSalaryStructureStartingAmtValue.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSalaryStructureStartingAmtValueFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panSalaryStructureInfo.add(txtSalaryStructureStartingAmtValue, gridBagConstraints);

        lblSalaryStructureEndingAmt.setText("Scale Ending Basic Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panSalaryStructureInfo.add(lblSalaryStructureEndingAmt, gridBagConstraints);

        txtSalaryStructureEndingAmtValue.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSalaryStructureEndingAmtValue.setValidation(new CurrencyValidation(14,2));
        txtSalaryStructureEndingAmtValue.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSalaryStructureEndingAmtValueFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panSalaryStructureInfo.add(txtSalaryStructureEndingAmtValue, gridBagConstraints);

        lblSalaryStructureAmt.setText("Increment Amount(NI)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panSalaryStructureInfo.add(lblSalaryStructureAmt, gridBagConstraints);

        txtSalaryStructureAmtValue.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSalaryStructureAmtValue.setValidation(new CurrencyValidation(14,2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panSalaryStructureInfo.add(txtSalaryStructureAmtValue, gridBagConstraints);

        lblSalaryStructureIncYear.setText("No.of Increments");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panSalaryStructureInfo.add(lblSalaryStructureIncYear, gridBagConstraints);

        txtSalaryStructureIncYearValue.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panSalaryStructureInfo.add(txtSalaryStructureIncYearValue, gridBagConstraints);

        lblStagnationIncrement.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblStagnationIncrement.setText("Stagnation Increment");
        lblStagnationIncrement.setMaximumSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panSalaryStructureInfo.add(lblStagnationIncrement, gridBagConstraints);

        panAutoRenewal.setMinimumSize(new java.awt.Dimension(100, 16));
        panAutoRenewal.setPreferredSize(new java.awt.Dimension(100, 18));
        panAutoRenewal.setLayout(new java.awt.GridBagLayout());

        rdoStagnationIncrement_Yes.setText("Yes");
        rdoStagnationIncrement_Yes.setMaximumSize(new java.awt.Dimension(50, 18));
        rdoStagnationIncrement_Yes.setMinimumSize(new java.awt.Dimension(50, 18));
        rdoStagnationIncrement_Yes.setPreferredSize(new java.awt.Dimension(50, 18));
        rdoStagnationIncrement_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoStagnationIncrement_YesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panAutoRenewal.add(rdoStagnationIncrement_Yes, gridBagConstraints);

        rdoStagnationIncrement_No.setText("No");
        rdoStagnationIncrement_No.setMaximumSize(new java.awt.Dimension(45, 18));
        rdoStagnationIncrement_No.setMinimumSize(new java.awt.Dimension(45, 18));
        rdoStagnationIncrement_No.setPreferredSize(new java.awt.Dimension(45, 18));
        rdoStagnationIncrement_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoStagnationIncrement_NoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panAutoRenewal.add(rdoStagnationIncrement_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panSalaryStructureInfo.add(panAutoRenewal, gridBagConstraints);

        lblSalaryStructureTotNoInc.setText("Total No of Stagnation Increments");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panSalaryStructureInfo.add(lblSalaryStructureTotNoInc, gridBagConstraints);

        txtSalaryStructureTotNoIncValue.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSalaryStructureTotNoIncValue.setValidation(new CurrencyValidation(14,2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 1);
        panSalaryStructureInfo.add(txtSalaryStructureTotNoIncValue, gridBagConstraints);

        lblSalaryStructureStagnationAmt.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSalaryStructureStagnationAmt.setText("Increment Amount(SI)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panSalaryStructureInfo.add(lblSalaryStructureStagnationAmt, gridBagConstraints);

        txtSalaryStructureStagnationAmtValue.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSalaryStructureStagnationAmtValue.setValidation(new CurrencyValidation(14,2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 1);
        panSalaryStructureInfo.add(txtSalaryStructureStagnationAmtValue, gridBagConstraints);

        lblSalaryStructureNoOfStagnation.setText("No.of Increments");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panSalaryStructureInfo.add(lblSalaryStructureNoOfStagnation, gridBagConstraints);

        txtSalaryStructureNoOfStagnationValue.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panSalaryStructureInfo.add(txtSalaryStructureNoOfStagnationValue, gridBagConstraints);

        lblSalaryStructureStagnationOnceIn.setText("Once in");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panSalaryStructureInfo.add(lblSalaryStructureStagnationOnceIn, gridBagConstraints);

        btnSalaryStructureNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnSalaryStructureNew.setPreferredSize(new java.awt.Dimension(30, 30));
        btnSalaryStructureNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalaryStructureNewActionPerformed(evt);
            }
        });
        panSalaryStructureButtons.add(btnSalaryStructureNew);

        btnSalaryStructureSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSalaryStructureSave.setPreferredSize(new java.awt.Dimension(30, 30));
        btnSalaryStructureSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalaryStructureSaveActionPerformed(evt);
            }
        });
        panSalaryStructureButtons.add(btnSalaryStructureSave);

        btnSalaryStructureDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnSalaryStructureDelete.setPreferredSize(new java.awt.Dimension(30, 30));
        btnSalaryStructureDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalaryStructureDeleteActionPerformed(evt);
            }
        });
        panSalaryStructureButtons.add(btnSalaryStructureDelete);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSalaryStructureInfo.add(panSalaryStructureButtons, gridBagConstraints);

        lblSalaryStructureStagnationAmt1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSalaryStructureStagnationAmt1.setText("Stagnation Increment");
        lblSalaryStructureStagnationAmt1.setFont(new java.awt.Font("MS Sans Serif", 1, 14)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panSalaryStructureInfo.add(lblSalaryStructureStagnationAmt1, gridBagConstraints);

        panCreditingACNo.setMinimumSize(new java.awt.Dimension(200, 25));
        panCreditingACNo.setPreferredSize(new java.awt.Dimension(200, 25));
        panCreditingACNo.setLayout(new java.awt.GridBagLayout());

        txtSalaryStructureStagnationOnceInValue.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panCreditingACNo.add(txtSalaryStructureStagnationOnceInValue, gridBagConstraints);

        cboSalaryStructureStagnationOnceIn.setMinimumSize(new java.awt.Dimension(100, 21));
        cboSalaryStructureStagnationOnceIn.setPopupWidth(250);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 1);
        panCreditingACNo.add(cboSalaryStructureStagnationOnceIn, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panSalaryStructureInfo.add(panCreditingACNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panSalaryDetails.add(panSalaryStructureInfo, gridBagConstraints);

        panSalaryStructureTable.setMinimumSize(new java.awt.Dimension(400, 400));
        panSalaryStructureTable.setPreferredSize(new java.awt.Dimension(400, 400));
        panSalaryStructureTable.setLayout(new java.awt.GridBagLayout());

        srpSalaryStructure.setMinimumSize(new java.awt.Dimension(395, 375));
        srpSalaryStructure.setPreferredSize(new java.awt.Dimension(395, 375));
        srpSalaryStructure.setAutoscrolls(true);

        tblSalaryStructure.setModel(new javax.swing.table.DefaultTableModel(
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
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblSalaryStructure.setMinimumSize(new java.awt.Dimension(270, 1000));
        tblSalaryStructure.setPreferredSize(new java.awt.Dimension(270, 1000));
        tblSalaryStructure.setReorderingAllowed(false);
        tblSalaryStructure.setOpaque(false);
        tblSalaryStructure.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSalaryStructureMouseClicked(evt);
            }
        });
        srpSalaryStructure.setViewportView(tblSalaryStructure);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.8;
        panSalaryStructureTable.add(srpSalaryStructure, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panSalaryDetails.add(panSalaryStructureTable, gridBagConstraints);

        tabSalaryStructure.addTab("Salary Structure Details", panSalaryDetails);

        panCCAllowance.setMinimumSize(new java.awt.Dimension(750, 230));
        panCCAllowance.setPreferredSize(new java.awt.Dimension(750, 230));
        panCCAllowance.setLayout(new java.awt.GridBagLayout());

        panCCAllowanceTable.setMinimumSize(new java.awt.Dimension(500, 403));
        panCCAllowanceTable.setPreferredSize(new java.awt.Dimension(500, 403));
        panCCAllowanceTable.setLayout(new java.awt.GridBagLayout());

        srpCCAllowance.setMinimumSize(new java.awt.Dimension(200, 404));
        srpCCAllowance.setPreferredSize(new java.awt.Dimension(200, 404));

        tblCCAllowance.setModel(new javax.swing.table.DefaultTableModel(
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
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblCCAllowance.setMinimumSize(new java.awt.Dimension(60, 64));
        tblCCAllowance.setPreferredSize(new java.awt.Dimension(60, 10000));
        tblCCAllowance.setOpaque(false);
        tblCCAllowance.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCCAllowanceMouseClicked(evt);
            }
        });
        srpCCAllowance.setViewportView(tblCCAllowance);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.8;
        panCCAllowanceTable.add(srpCCAllowance, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panCCAllowance.add(panCCAllowanceTable, gridBagConstraints);

        panCCAllowanceInfo.setMinimumSize(new java.awt.Dimension(350, 400));
        panCCAllowanceInfo.setPreferredSize(new java.awt.Dimension(350, 400));
        panCCAllowanceInfo.setLayout(new java.awt.GridBagLayout());

        lblCCAllowanceSLNO.setText("SL No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panCCAllowanceInfo.add(lblCCAllowanceSLNO, gridBagConstraints);

        lblCCAllowanceSLNOValue.setMaximumSize(new java.awt.Dimension(150, 16));
        lblCCAllowanceSLNOValue.setMinimumSize(new java.awt.Dimension(150, 16));
        lblCCAllowanceSLNOValue.setPreferredSize(new java.awt.Dimension(150, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panCCAllowanceInfo.add(lblCCAllowanceSLNOValue, gridBagConstraints);

        lblCCAllowanceCityType.setText("City Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panCCAllowanceInfo.add(lblCCAllowanceCityType, gridBagConstraints);

        cboCCAllowanceCityType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCCAllowanceCityType.setPopupWidth(250);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panCCAllowanceInfo.add(cboCCAllowanceCityType, gridBagConstraints);

        lblCCAllowanceFromDate.setText("From Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panCCAllowanceInfo.add(lblCCAllowanceFromDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panCCAllowanceInfo.add(tdtCCAllowanceFromDateValue, gridBagConstraints);

        lblCCAllowanceToDate.setText("To Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panCCAllowanceInfo.add(lblCCAllowanceToDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panCCAllowanceInfo.add(tdtCCAllowanceToDateValue, gridBagConstraints);

        lblCCAllowanceStartingAmt.setText("CCA %");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panCCAllowanceInfo.add(lblCCAllowanceStartingAmt, gridBagConstraints);

        txtCCAllowanceStartingAmtValue.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCCAllowanceStartingAmtValue.setValidation(new CurrencyValidation(14,2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panCCAllowanceInfo.add(txtCCAllowanceStartingAmtValue, gridBagConstraints);

        lblCCAllowanceEndingAmt.setText("Max CCA");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panCCAllowanceInfo.add(lblCCAllowanceEndingAmt, gridBagConstraints);

        txtCCAllowanceEndingAmtValue.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCCAllowanceEndingAmtValue.setValidation(new CurrencyValidation(14,2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panCCAllowanceInfo.add(txtCCAllowanceEndingAmtValue, gridBagConstraints);

        lblCCAllowance.setText("Designation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panCCAllowanceInfo.add(lblCCAllowance, gridBagConstraints);

        cboCCAllowance.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCCAllowance.setPopupWidth(250);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panCCAllowanceInfo.add(cboCCAllowance, gridBagConstraints);

        btnCCAllowanceNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnCCAllowanceNew.setPreferredSize(new java.awt.Dimension(30, 30));
        btnCCAllowanceNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCCAllowanceNewActionPerformed(evt);
            }
        });
        panCCAllowanceButtons.add(btnCCAllowanceNew);

        btnCCAllowanceSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnCCAllowanceSave.setPreferredSize(new java.awt.Dimension(30, 30));
        btnCCAllowanceSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCCAllowanceSaveActionPerformed(evt);
            }
        });
        panCCAllowanceButtons.add(btnCCAllowanceSave);

        btnCCAllowanceDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnCCAllowanceDelete.setPreferredSize(new java.awt.Dimension(30, 30));
        btnCCAllowanceDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCCAllowanceDeleteActionPerformed(evt);
            }
        });
        panCCAllowanceButtons.add(btnCCAllowanceDelete);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCCAllowanceInfo.add(panCCAllowanceButtons, gridBagConstraints);

        panPercentOrFixed.setMinimumSize(new java.awt.Dimension(190, 27));
        panPercentOrFixed.setName("panMaritalStatus");
        panPercentOrFixed.setPreferredSize(new java.awt.Dimension(190, 27));
        panPercentOrFixed.setLayout(new java.awt.GridBagLayout());

        rdgPercentOrFixed.add(rdoPercentOrFixed_Percent);
        rdoPercentOrFixed_Percent.setText("PERCENT");
        rdoPercentOrFixed_Percent.setMaximumSize(new java.awt.Dimension(90, 21));
        rdoPercentOrFixed_Percent.setMinimumSize(new java.awt.Dimension(90, 21));
        rdoPercentOrFixed_Percent.setName("rdoMaritalStatus_Single");
        rdoPercentOrFixed_Percent.setPreferredSize(new java.awt.Dimension(90, 21));
        rdoPercentOrFixed_Percent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoPercentOrFixed_PercentActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPercentOrFixed.add(rdoPercentOrFixed_Percent, gridBagConstraints);

        rdgPercentOrFixed.add(rdoPercentOrFixed_Fixed);
        rdoPercentOrFixed_Fixed.setText("FIXED");
        rdoPercentOrFixed_Fixed.setMaximumSize(new java.awt.Dimension(80, 27));
        rdoPercentOrFixed_Fixed.setMinimumSize(new java.awt.Dimension(80, 27));
        rdoPercentOrFixed_Fixed.setName("rdoMaritalStatus_Married");
        rdoPercentOrFixed_Fixed.setPreferredSize(new java.awt.Dimension(69, 21));
        rdoPercentOrFixed_Fixed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoPercentOrFixed_FixedActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPercentOrFixed.add(rdoPercentOrFixed_Fixed, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panCCAllowanceInfo.add(panPercentOrFixed, gridBagConstraints);

        lblPercentOrFixed.setText("Allowance In");
        lblPercentOrFixed.setName("lblCustomerID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panCCAllowanceInfo.add(lblPercentOrFixed, gridBagConstraints);

        txtFromAmount.setMaxLength(128);
        txtFromAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtFromAmount.setName("txtCompany");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panCCAllowanceInfo.add(txtFromAmount, gridBagConstraints);

        txtToAmount.setMaxLength(128);
        txtToAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtToAmount.setName("txtCompany");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 51);
        panCCAllowanceInfo.add(txtToAmount, gridBagConstraints);

        lblToAmount.setText(" To Amount");
        lblToAmount.setName("lblCustomerID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.insets = new java.awt.Insets(1, 15, 1, 1);
        panCCAllowanceInfo.add(lblToAmount, gridBagConstraints);

        lblFromAmount.setText("From Amount");
        lblFromAmount.setName("lblCustomerID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 3);
        panCCAllowanceInfo.add(lblFromAmount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panCCAllowance.add(panCCAllowanceInfo, gridBagConstraints);

        tabSalaryStructure.addTab("CCAllowances", panCCAllowance);

        panHRAllowance.setMinimumSize(new java.awt.Dimension(750, 230));
        panHRAllowance.setPreferredSize(new java.awt.Dimension(750, 230));
        panHRAllowance.setLayout(new java.awt.GridBagLayout());

        panHRAllowanceTable.setMinimumSize(new java.awt.Dimension(500, 403));
        panHRAllowanceTable.setPreferredSize(new java.awt.Dimension(500, 403));
        panHRAllowanceTable.setLayout(new java.awt.GridBagLayout());

        srpHRAllowance.setMinimumSize(new java.awt.Dimension(200, 404));
        srpHRAllowance.setPreferredSize(new java.awt.Dimension(200, 404));

        tblHRAllowance.setModel(new javax.swing.table.DefaultTableModel(
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
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblHRAllowance.setMinimumSize(new java.awt.Dimension(60, 64));
        tblHRAllowance.setPreferredSize(new java.awt.Dimension(60, 10000));
        tblHRAllowance.setOpaque(false);
        tblHRAllowance.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblHRAllowanceMouseClicked(evt);
            }
        });
        srpHRAllowance.setViewportView(tblHRAllowance);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.8;
        panHRAllowanceTable.add(srpHRAllowance, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panHRAllowance.add(panHRAllowanceTable, gridBagConstraints);

        panHRAllowanceInfo.setMinimumSize(new java.awt.Dimension(350, 400));
        panHRAllowanceInfo.setPreferredSize(new java.awt.Dimension(350, 400));
        panHRAllowanceInfo.setLayout(new java.awt.GridBagLayout());

        lblHRAllowanceSLNO.setText("SL No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panHRAllowanceInfo.add(lblHRAllowanceSLNO, gridBagConstraints);

        lblHRAllowanceSLNOValue.setMaximumSize(new java.awt.Dimension(150, 16));
        lblHRAllowanceSLNOValue.setMinimumSize(new java.awt.Dimension(150, 16));
        lblHRAllowanceSLNOValue.setPreferredSize(new java.awt.Dimension(150, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panHRAllowanceInfo.add(lblHRAllowanceSLNOValue, gridBagConstraints);

        lblHRAllowanceCityType.setText("City Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panHRAllowanceInfo.add(lblHRAllowanceCityType, gridBagConstraints);

        cboHRAllowanceCityType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboHRAllowanceCityType.setPopupWidth(250);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panHRAllowanceInfo.add(cboHRAllowanceCityType, gridBagConstraints);

        lblHRAllowanceFromDate.setText("From Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panHRAllowanceInfo.add(lblHRAllowanceFromDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panHRAllowanceInfo.add(tdtHRAllowanceFromDateValue, gridBagConstraints);

        lblHRAllowanceToDate.setText("To Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panHRAllowanceInfo.add(lblHRAllowanceToDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panHRAllowanceInfo.add(tdtHRAllowanceToDateValue, gridBagConstraints);

        lblHRAllowanceStartingAmt.setText("CCA %");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panHRAllowanceInfo.add(lblHRAllowanceStartingAmt, gridBagConstraints);

        txtHRAllowanceStartingAmtValue.setMinimumSize(new java.awt.Dimension(100, 21));
        txtHRAllowanceStartingAmtValue.setValidation(new CurrencyValidation(14,2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panHRAllowanceInfo.add(txtHRAllowanceStartingAmtValue, gridBagConstraints);

        lblHRAllowanceEndingAmt.setText("Max CCA");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panHRAllowanceInfo.add(lblHRAllowanceEndingAmt, gridBagConstraints);

        txtHRAllowanceEndingAmtValue.setMinimumSize(new java.awt.Dimension(100, 21));
        txtHRAllowanceEndingAmtValue.setValidation(new CurrencyValidation(14,2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panHRAllowanceInfo.add(txtHRAllowanceEndingAmtValue, gridBagConstraints);

        lblHRAllowanceDesignation.setText("Designation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panHRAllowanceInfo.add(lblHRAllowanceDesignation, gridBagConstraints);

        cboHRAllowanceDesignation.setMinimumSize(new java.awt.Dimension(100, 21));
        cboHRAllowanceDesignation.setPopupWidth(250);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panHRAllowanceInfo.add(cboHRAllowanceDesignation, gridBagConstraints);

        btnHRAllowanceNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnHRAllowanceNew.setPreferredSize(new java.awt.Dimension(30, 30));
        btnHRAllowanceNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHRAllowanceNewActionPerformed(evt);
            }
        });
        panHRAllowanceButtons.add(btnHRAllowanceNew);

        btnHRAllowanceSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnHRAllowanceSave.setPreferredSize(new java.awt.Dimension(30, 30));
        btnHRAllowanceSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHRAllowanceSaveActionPerformed(evt);
            }
        });
        panHRAllowanceButtons.add(btnHRAllowanceSave);

        btnHRAllowanceDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnHRAllowanceDelete.setPreferredSize(new java.awt.Dimension(30, 30));
        btnHRAllowanceDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHRAllowanceDeleteActionPerformed(evt);
            }
        });
        panHRAllowanceButtons.add(btnHRAllowanceDelete);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panHRAllowanceInfo.add(panHRAllowanceButtons, gridBagConstraints);

        lblHRAPayable.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblHRAPayable.setText("Quarters Provided whether HRA Payable");
        lblHRAPayable.setMaximumSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panHRAllowanceInfo.add(lblHRAPayable, gridBagConstraints);

        panAutoRenewal1.setMinimumSize(new java.awt.Dimension(100, 16));
        panAutoRenewal1.setPreferredSize(new java.awt.Dimension(100, 18));
        panAutoRenewal1.setLayout(new java.awt.GridBagLayout());

        rdoHRAPayable_Yes.setText("Yes");
        rdoHRAPayable_Yes.setMaximumSize(new java.awt.Dimension(50, 18));
        rdoHRAPayable_Yes.setMinimumSize(new java.awt.Dimension(50, 18));
        rdoHRAPayable_Yes.setPreferredSize(new java.awt.Dimension(50, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panAutoRenewal1.add(rdoHRAPayable_Yes, gridBagConstraints);

        rdoHRAPayable_No.setText("No");
        rdoHRAPayable_No.setMaximumSize(new java.awt.Dimension(45, 18));
        rdoHRAPayable_No.setMinimumSize(new java.awt.Dimension(45, 18));
        rdoHRAPayable_No.setPreferredSize(new java.awt.Dimension(45, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panAutoRenewal1.add(rdoHRAPayable_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panHRAllowanceInfo.add(panAutoRenewal1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panHRAllowance.add(panHRAllowanceInfo, gridBagConstraints);

        tabSalaryStructure.addTab("House Rent Allowance", panHRAllowance);

        panDAllowances.setMinimumSize(new java.awt.Dimension(750, 230));
        panDAllowances.setPreferredSize(new java.awt.Dimension(750, 230));
        panDAllowances.setLayout(new java.awt.GridBagLayout());

        panDAInfo.setMinimumSize(new java.awt.Dimension(350, 400));
        panDAInfo.setPreferredSize(new java.awt.Dimension(350, 400));
        panDAInfo.setLayout(new java.awt.GridBagLayout());

        lblDASLNO.setText("SL No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panDAInfo.add(lblDASLNO, gridBagConstraints);

        lblDASLNOValue.setMaximumSize(new java.awt.Dimension(150, 16));
        lblDASLNOValue.setMinimumSize(new java.awt.Dimension(150, 16));
        lblDASLNOValue.setPreferredSize(new java.awt.Dimension(150, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panDAInfo.add(lblDASLNOValue, gridBagConstraints);

        lblDAFromDate.setText("From Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panDAInfo.add(lblDAFromDate, gridBagConstraints);

        tdtDAFromDateValue.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtDAFromDateValueFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panDAInfo.add(tdtDAFromDateValue, gridBagConstraints);

        lblDAToDate.setText("To Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panDAInfo.add(lblDAToDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panDAInfo.add(tdtDAToDateValue, gridBagConstraints);

        lblDANoOfPointsPerSlab.setText("No. of Points Per Slab");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panDAInfo.add(lblDANoOfPointsPerSlab, gridBagConstraints);

        txtDANoOfPointsPerSlabValue.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDANoOfPointsPerSlabValue.setValidation(new CurrencyValidation(14,2));
        txtDANoOfPointsPerSlabValue.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDANoOfPointsPerSlabValueFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panDAInfo.add(txtDANoOfPointsPerSlabValue, gridBagConstraints);

        lblDAIndex.setText("Index");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panDAInfo.add(lblDAIndex, gridBagConstraints);

        txtDAIndexValue.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDAIndexValue.setValidation(new CurrencyValidation(14,2));
        txtDAIndexValue.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDAIndexValueFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panDAInfo.add(txtDAIndexValue, gridBagConstraints);

        lblDADesignation.setText("Designation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panDAInfo.add(lblDADesignation, gridBagConstraints);

        cboDADesignationValue.setMinimumSize(new java.awt.Dimension(100, 21));
        cboDADesignationValue.setPopupWidth(250);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panDAInfo.add(cboDADesignationValue, gridBagConstraints);

        btnDANew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnDANew.setPreferredSize(new java.awt.Dimension(30, 30));
        btnDANew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDANewActionPerformed(evt);
            }
        });
        panDAButtons.add(btnDANew);

        btnDASave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnDASave.setPreferredSize(new java.awt.Dimension(30, 30));
        btnDASave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDASaveActionPerformed(evt);
            }
        });
        panDAButtons.add(btnDASave);

        btnDADelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDADelete.setPreferredSize(new java.awt.Dimension(30, 30));
        btnDADelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDADeleteActionPerformed(evt);
            }
        });
        panDAButtons.add(btnDADelete);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDAInfo.add(panDAButtons, gridBagConstraints);

        lblDAPercentagePerSlab.setText("Total DA Percentage");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panDAInfo.add(lblDAPercentagePerSlab, gridBagConstraints);

        lblTotalDaPer.setText("Total No.of Slab");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panDAInfo.add(lblTotalDaPer, gridBagConstraints);

        txtDATotalDAPercentageValue.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDATotalDAPercentageValue.setValidation(new CurrencyValidation(14,2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panDAInfo.add(txtDATotalDAPercentageValue, gridBagConstraints);

        txtTotalNoofSlabValue.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTotalNoofSlabValue.setValidation(new CurrencyValidation(14,2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panDAInfo.add(txtTotalNoofSlabValue, gridBagConstraints);

        lblDATotalNoofSlab.setText("Percentage Per Slab");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panDAInfo.add(lblDATotalNoofSlab, gridBagConstraints);

        txtDAPercentagePerSlabValue.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDAPercentagePerSlabValue.setValidation(new CurrencyValidation(14,2));
        txtDAPercentagePerSlabValue.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDAPercentagePerSlabValueFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panDAInfo.add(txtDAPercentagePerSlabValue, gridBagConstraints);

        panIndexOrPercent.setMinimumSize(new java.awt.Dimension(190, 27));
        panIndexOrPercent.setName("panMaritalStatus");
        panIndexOrPercent.setPreferredSize(new java.awt.Dimension(190, 27));
        panIndexOrPercent.setLayout(new java.awt.GridBagLayout());

        rdgIndexOrPercent.add(rdoIndexOrPercent_Percent);
        rdoIndexOrPercent_Percent.setText("PERCENT");
        rdoIndexOrPercent_Percent.setMaximumSize(new java.awt.Dimension(90, 21));
        rdoIndexOrPercent_Percent.setMinimumSize(new java.awt.Dimension(90, 21));
        rdoIndexOrPercent_Percent.setName("rdoMaritalStatus_Single");
        rdoIndexOrPercent_Percent.setPreferredSize(new java.awt.Dimension(90, 21));
        rdoIndexOrPercent_Percent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoIndexOrPercent_PercentActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panIndexOrPercent.add(rdoIndexOrPercent_Percent, gridBagConstraints);

        rdgIndexOrPercent.add(rdoIndexOrPercent_Index);
        rdoIndexOrPercent_Index.setText("INDEX");
        rdoIndexOrPercent_Index.setMaximumSize(new java.awt.Dimension(80, 27));
        rdoIndexOrPercent_Index.setMinimumSize(new java.awt.Dimension(80, 27));
        rdoIndexOrPercent_Index.setName("rdoMaritalStatus_Married");
        rdoIndexOrPercent_Index.setPreferredSize(new java.awt.Dimension(69, 21));
        rdoIndexOrPercent_Index.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoIndexOrPercent_IndexActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panIndexOrPercent.add(rdoIndexOrPercent_Index, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panDAInfo.add(panIndexOrPercent, gridBagConstraints);

        lblPercentOrFixed1.setText("Based On");
        lblPercentOrFixed1.setName("lblCustomerID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panDAInfo.add(lblPercentOrFixed1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panDAllowances.add(panDAInfo, gridBagConstraints);

        panDATable.setMinimumSize(new java.awt.Dimension(500, 403));
        panDATable.setPreferredSize(new java.awt.Dimension(500, 403));
        panDATable.setLayout(new java.awt.GridBagLayout());

        srpDA.setMinimumSize(new java.awt.Dimension(200, 404));
        srpDA.setPreferredSize(new java.awt.Dimension(200, 404));

        tblDA.setModel(new javax.swing.table.DefaultTableModel(
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
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblDA.setMinimumSize(new java.awt.Dimension(60, 64));
        tblDA.setPreferredSize(new java.awt.Dimension(60, 10000));
        tblDA.setOpaque(false);
        tblDA.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDAMouseClicked(evt);
            }
        });
        srpDA.setViewportView(tblDA);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.8;
        panDATable.add(srpDA, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panDAllowances.add(panDATable, gridBagConstraints);

        tabSalaryStructure.addTab("DAllowances", panDAllowances);

        panTravellingAllowance.setMinimumSize(new java.awt.Dimension(750, 230));
        panTravellingAllowance.setPreferredSize(new java.awt.Dimension(750, 230));
        panTravellingAllowance.setLayout(new java.awt.GridBagLayout());

        panTravellingAllowanceInfo.setMinimumSize(new java.awt.Dimension(350, 400));
        panTravellingAllowanceInfo.setPreferredSize(new java.awt.Dimension(350, 400));
        panTravellingAllowanceInfo.setLayout(new java.awt.GridBagLayout());

        lblTravellingAllowanceSLNO.setText("SL No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panTravellingAllowanceInfo.add(lblTravellingAllowanceSLNO, gridBagConstraints);

        lblTravellingAllowanceSLNOValue.setMaximumSize(new java.awt.Dimension(150, 16));
        lblTravellingAllowanceSLNOValue.setMinimumSize(new java.awt.Dimension(75, 16));
        lblTravellingAllowanceSLNOValue.setPreferredSize(new java.awt.Dimension(75, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panTravellingAllowanceInfo.add(lblTravellingAllowanceSLNOValue, gridBagConstraints);

        lblTravellingAllowanceDesg.setText("Designation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panTravellingAllowanceInfo.add(lblTravellingAllowanceDesg, gridBagConstraints);

        cboTravellingAllowance.setMinimumSize(new java.awt.Dimension(100, 21));
        cboTravellingAllowance.setPopupWidth(250);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panTravellingAllowanceInfo.add(cboTravellingAllowance, gridBagConstraints);

        lblTAFromDate.setText("From Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panTravellingAllowanceInfo.add(lblTAFromDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panTravellingAllowanceInfo.add(tdtTAFromDateValue, gridBagConstraints);

        lblTAToDate.setText("To Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panTravellingAllowanceInfo.add(lblTAToDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panTravellingAllowanceInfo.add(tdtTAToDateValue, gridBagConstraints);

        lblPricePerlitre.setText("Price Per litre");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panTravellingAllowanceInfo.add(lblPricePerlitre, gridBagConstraints);

        txtPricePerlitreValue.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPricePerlitreValue.setValidation(new CurrencyValidation(14,2));
        txtPricePerlitreValue.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPricePerlitreValueFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panTravellingAllowanceInfo.add(txtPricePerlitreValue, gridBagConstraints);

        lblTotalConveyanceAmt.setText("Total Conveyance Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panTravellingAllowanceInfo.add(lblTotalConveyanceAmt, gridBagConstraints);

        txtTotalConveyanceAmtValue.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTotalConveyanceAmtValue.setValidation(new CurrencyValidation(14,2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panTravellingAllowanceInfo.add(txtTotalConveyanceAmtValue, gridBagConstraints);

        btnTANew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnTANew.setPreferredSize(new java.awt.Dimension(30, 30));
        btnTANew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTANewActionPerformed(evt);
            }
        });
        panTravellingAllowanceButtons.add(btnTANew);

        btnTASave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnTASave.setPreferredSize(new java.awt.Dimension(30, 30));
        btnTASave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTASaveActionPerformed(evt);
            }
        });
        panTravellingAllowanceButtons.add(btnTASave);

        btnTADelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnTADelete.setPreferredSize(new java.awt.Dimension(30, 30));
        btnTADelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTADeleteActionPerformed(evt);
            }
        });
        panTravellingAllowanceButtons.add(btnTADelete);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTravellingAllowanceInfo.add(panTravellingAllowanceButtons, gridBagConstraints);

        txtNooflitresValue.setMinimumSize(new java.awt.Dimension(100, 21));
        txtNooflitresValue.setValidation(new CurrencyValidation(14,2));
        txtNooflitresValue.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNooflitresValueFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panTravellingAllowanceInfo.add(txtNooflitresValue, gridBagConstraints);

        lblNoOflitres.setText("No of litres of petrol per month");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panTravellingAllowanceInfo.add(lblNoOflitres, gridBagConstraints);

        txtConveyancePerMonthValue.setMinimumSize(new java.awt.Dimension(100, 21));
        txtConveyancePerMonthValue.setValidation(new CurrencyValidation(14,2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panTravellingAllowanceInfo.add(txtConveyancePerMonthValue, gridBagConstraints);

        lblConveyancePerMonth.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblConveyancePerMonth.setText("Conveyance Per Month");
        lblConveyancePerMonth.setMaximumSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panTravellingAllowanceInfo.add(lblConveyancePerMonth, gridBagConstraints);

        lblBasicAmtUpto.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBasicAmtUpto.setText("Basic Amount Up to");
        lblBasicAmtUpto.setMaximumSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panTravellingAllowanceInfo.add(lblBasicAmtUpto, gridBagConstraints);

        txtBasicAmtUptoValue.setMinimumSize(new java.awt.Dimension(100, 21));
        txtBasicAmtUptoValue.setValidation(new CurrencyValidation(14,2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panTravellingAllowanceInfo.add(txtBasicAmtUptoValue, gridBagConstraints);

        lblBasicAmtBeyond.setText("Basic Amount Beyond");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panTravellingAllowanceInfo.add(lblBasicAmtBeyond, gridBagConstraints);

        txtBasicAmtBeyondValue.setMinimumSize(new java.awt.Dimension(100, 21));
        txtBasicAmtBeyondValue.setValidation(new CurrencyValidation(14,2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panTravellingAllowanceInfo.add(txtBasicAmtBeyondValue, gridBagConstraints);

        lblConveyanceAmt.setText("Conveyance Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panTravellingAllowanceInfo.add(lblConveyanceAmt, gridBagConstraints);

        txtConveyanceAmtValue.setMinimumSize(new java.awt.Dimension(100, 21));
        txtConveyanceAmtValue.setValidation(new CurrencyValidation(14,2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panTravellingAllowanceInfo.add(txtConveyanceAmtValue, gridBagConstraints);

        panAutoRenewal3.setMinimumSize(new java.awt.Dimension(270, 18));
        panAutoRenewal3.setPreferredSize(new java.awt.Dimension(270, 18));
        panAutoRenewal3.setLayout(new java.awt.GridBagLayout());

        chkPetrolAllowance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkPetrolAllowanceActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panAutoRenewal3.add(chkPetrolAllowance, gridBagConstraints);

        chkFixedConveyance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkFixedConveyanceActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panAutoRenewal3.add(chkFixedConveyance, gridBagConstraints);

        lblFixedConveyance.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblFixedConveyance.setText("Fixed Conveyance");
        lblFixedConveyance.setMaximumSize(new java.awt.Dimension(120, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panAutoRenewal3.add(lblFixedConveyance, gridBagConstraints);

        lblPetrolAllowance.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPetrolAllowance.setText("Petrol Allowance");
        lblPetrolAllowance.setMaximumSize(new java.awt.Dimension(120, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAutoRenewal3.add(lblPetrolAllowance, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 40, 1, 1);
        panTravellingAllowanceInfo.add(panAutoRenewal3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panTravellingAllowance.add(panTravellingAllowanceInfo, gridBagConstraints);

        panTravellingAllowanceTable.setMinimumSize(new java.awt.Dimension(500, 403));
        panTravellingAllowanceTable.setPreferredSize(new java.awt.Dimension(500, 403));
        panTravellingAllowanceTable.setLayout(new java.awt.GridBagLayout());

        srpTravellingAllowance.setMinimumSize(new java.awt.Dimension(200, 404));
        srpTravellingAllowance.setPreferredSize(new java.awt.Dimension(200, 404));

        tblTravellingAllowance.setModel(new javax.swing.table.DefaultTableModel(
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
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblTravellingAllowance.setMinimumSize(new java.awt.Dimension(60, 64));
        tblTravellingAllowance.setPreferredSize(new java.awt.Dimension(60, 10000));
        tblTravellingAllowance.setOpaque(false);
        tblTravellingAllowance.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTravellingAllowanceMouseClicked(evt);
            }
        });
        srpTravellingAllowance.setViewportView(tblTravellingAllowance);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.8;
        panTravellingAllowanceTable.add(srpTravellingAllowance, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panTravellingAllowance.add(panTravellingAllowanceTable, gridBagConstraints);

        tabSalaryStructure.addTab("Travelling Allowances", panTravellingAllowance);

        panMedicalAllowance.setMinimumSize(new java.awt.Dimension(750, 230));
        panMedicalAllowance.setPreferredSize(new java.awt.Dimension(750, 230));
        panMedicalAllowance.setLayout(new java.awt.GridBagLayout());

        panMedicalAllowanceInfo.setMinimumSize(new java.awt.Dimension(350, 400));
        panMedicalAllowanceInfo.setPreferredSize(new java.awt.Dimension(350, 400));
        panMedicalAllowanceInfo.setLayout(new java.awt.GridBagLayout());

        lblMASLNO.setText("SL No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panMedicalAllowanceInfo.add(lblMASLNO, gridBagConstraints);

        lblMASLNOValue.setMaximumSize(new java.awt.Dimension(150, 16));
        lblMASLNOValue.setMinimumSize(new java.awt.Dimension(150, 16));
        lblMASLNOValue.setPreferredSize(new java.awt.Dimension(150, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panMedicalAllowanceInfo.add(lblMASLNOValue, gridBagConstraints);

        lblMAidDesg.setText("Designation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panMedicalAllowanceInfo.add(lblMAidDesg, gridBagConstraints);

        cboMAidDesg.setMinimumSize(new java.awt.Dimension(100, 21));
        cboMAidDesg.setPopupWidth(250);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panMedicalAllowanceInfo.add(cboMAidDesg, gridBagConstraints);

        lblMAFromDate.setText("From Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panMedicalAllowanceInfo.add(lblMAFromDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panMedicalAllowanceInfo.add(tdtMAidFromDateValue, gridBagConstraints);

        lblMAToDate.setText("To Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panMedicalAllowanceInfo.add(lblMAToDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panMedicalAllowanceInfo.add(tdtMAidToDateValue, gridBagConstraints);

        lblMAAmt.setText("Medical Aid per annum");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panMedicalAllowanceInfo.add(lblMAAmt, gridBagConstraints);

        txtMAidAmtValue.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMAidAmtValue.setValidation(new CurrencyValidation(14,2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panMedicalAllowanceInfo.add(txtMAidAmtValue, gridBagConstraints);

        lblTravellingAllowanceEndingAmt2.setText("Total Conveyance Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panMedicalAllowanceInfo.add(lblTravellingAllowanceEndingAmt2, gridBagConstraints);

        txtTravellingAllowanceEndingAmtValue2.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTravellingAllowanceEndingAmtValue2.setValidation(new CurrencyValidation(14,2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panMedicalAllowanceInfo.add(txtTravellingAllowanceEndingAmtValue2, gridBagConstraints);

        btnMAidNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnMAidNew.setPreferredSize(new java.awt.Dimension(30, 30));
        btnMAidNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMAidNewActionPerformed(evt);
            }
        });
        panMAidButtons.add(btnMAidNew);

        btnMAidSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnMAidSave.setPreferredSize(new java.awt.Dimension(30, 30));
        btnMAidSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMAidSaveActionPerformed(evt);
            }
        });
        panMAidButtons.add(btnMAidSave);

        btnMAidDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnMAidDelete.setPreferredSize(new java.awt.Dimension(30, 30));
        btnMAidDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMAidDeleteActionPerformed(evt);
            }
        });
        panMAidButtons.add(btnMAidDelete);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMedicalAllowanceInfo.add(panMAidButtons, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panMedicalAllowance.add(panMedicalAllowanceInfo, gridBagConstraints);

        panMAid.setMinimumSize(new java.awt.Dimension(500, 403));
        panMAid.setPreferredSize(new java.awt.Dimension(500, 403));
        panMAid.setLayout(new java.awt.GridBagLayout());

        srpMAid.setMinimumSize(new java.awt.Dimension(200, 404));
        srpMAid.setPreferredSize(new java.awt.Dimension(200, 404));

        tblMaid.setModel(new javax.swing.table.DefaultTableModel(
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
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblMaid.setMinimumSize(new java.awt.Dimension(60, 64));
        tblMaid.setPreferredSize(new java.awt.Dimension(60, 10000));
        tblMaid.setOpaque(false);
        tblMaid.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblMaidMouseClicked(evt);
            }
        });
        srpMAid.setViewportView(tblMaid);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.8;
        panMAid.add(srpMAid, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panMedicalAllowance.add(panMAid, gridBagConstraints);

        tabSalaryStructure.addTab("Medical Aid", panMedicalAllowance);

        panOtherAllowances.setMinimumSize(new java.awt.Dimension(750, 230));
        panOtherAllowances.setPreferredSize(new java.awt.Dimension(750, 230));
        panOtherAllowances.setLayout(new java.awt.GridBagLayout());

        panOAllowanceInfo.setMinimumSize(new java.awt.Dimension(350, 400));
        panOAllowanceInfo.setPreferredSize(new java.awt.Dimension(350, 400));
        panOAllowanceInfo.setLayout(new java.awt.GridBagLayout());

        lblOASLNO.setText("SL No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panOAllowanceInfo.add(lblOASLNO, gridBagConstraints);

        lblOASLNOValue.setMaximumSize(new java.awt.Dimension(150, 16));
        lblOASLNOValue.setMinimumSize(new java.awt.Dimension(75, 16));
        lblOASLNOValue.setPreferredSize(new java.awt.Dimension(75, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panOAllowanceInfo.add(lblOASLNOValue, gridBagConstraints);

        lblOADesignation.setText("Designation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panOAllowanceInfo.add(lblOADesignation, gridBagConstraints);

        cboOADesignationValue.setMinimumSize(new java.awt.Dimension(100, 21));
        cboOADesignationValue.setPopupWidth(250);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panOAllowanceInfo.add(cboOADesignationValue, gridBagConstraints);

        lblOAFromDate.setText("From Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panOAllowanceInfo.add(lblOAFromDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panOAllowanceInfo.add(tdtOAFromDateValue, gridBagConstraints);

        lblOAToDate.setText("To Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panOAllowanceInfo.add(lblOAToDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panOAllowanceInfo.add(tdtOAToDateValue, gridBagConstraints);

        btnOANew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnOANew.setPreferredSize(new java.awt.Dimension(30, 30));
        btnOANew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOANewActionPerformed(evt);
            }
        });
        panOAButtons.add(btnOANew);

        btnOASave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnOASave.setPreferredSize(new java.awt.Dimension(30, 30));
        btnOASave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOASaveActionPerformed(evt);
            }
        });
        panOAButtons.add(btnOASave);

        btnOADelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnOADelete.setPreferredSize(new java.awt.Dimension(30, 30));
        btnOADelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOADeleteActionPerformed(evt);
            }
        });
        panOAButtons.add(btnOADelete);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 19;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOAllowanceInfo.add(panOAButtons, gridBagConstraints);

        txtOAPercentageValue.setMinimumSize(new java.awt.Dimension(100, 21));
        txtOAPercentageValue.setValidation(new CurrencyValidation(14,2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panOAllowanceInfo.add(txtOAPercentageValue, gridBagConstraints);

        lblOAPercentage.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblOAPercentage.setText("Percentage");
        lblOAPercentage.setMaximumSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panOAllowanceInfo.add(lblOAPercentage, gridBagConstraints);

        lblOAFixedAmt.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblOAFixedAmt.setText("Fixed Amount");
        lblOAFixedAmt.setMaximumSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panOAllowanceInfo.add(lblOAFixedAmt, gridBagConstraints);

        txtOAFixedAmtValue.setMinimumSize(new java.awt.Dimension(100, 21));
        txtOAFixedAmtValue.setValidation(new CurrencyValidation(14,2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panOAllowanceInfo.add(txtOAFixedAmtValue, gridBagConstraints);

        lblOAParameterBasedon.setText("Parameter Based on");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panOAllowanceInfo.add(lblOAParameterBasedon, gridBagConstraints);

        lblOAMaximumOf.setText("Maximum of");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panOAllowanceInfo.add(lblOAMaximumOf, gridBagConstraints);

        txtOAMaximumOfValue.setMinimumSize(new java.awt.Dimension(100, 21));
        txtOAMaximumOfValue.setValidation(new CurrencyValidation(14,2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panOAllowanceInfo.add(txtOAMaximumOfValue, gridBagConstraints);

        cboOAParameterBasedOnValue.setMinimumSize(new java.awt.Dimension(100, 21));
        cboOAParameterBasedOnValue.setPopupWidth(250);
        cboOAParameterBasedOnValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboOAParameterBasedOnValueActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panOAllowanceInfo.add(cboOAParameterBasedOnValue, gridBagConstraints);

        lblOASubParameter.setText("Sub Parameter");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panOAllowanceInfo.add(lblOASubParameter, gridBagConstraints);

        cboOAllowanceTypeValue.setMinimumSize(new java.awt.Dimension(100, 21));
        cboOAllowanceTypeValue.setPopupWidth(250);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panOAllowanceInfo.add(cboOAllowanceTypeValue, gridBagConstraints);

        lblOAllowanceType.setText("Allowance Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 4);
        panOAllowanceInfo.add(lblOAllowanceType, gridBagConstraints);

        cboOASubParameterValue.setMinimumSize(new java.awt.Dimension(100, 21));
        cboOASubParameterValue.setPopupWidth(250);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panOAllowanceInfo.add(cboOASubParameterValue, gridBagConstraints);

        panAutoRenewal2.setMinimumSize(new java.awt.Dimension(160, 18));
        panAutoRenewal2.setPreferredSize(new java.awt.Dimension(160, 18));
        panAutoRenewal2.setLayout(new java.awt.GridBagLayout());

        lblOAFixed.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblOAFixed.setText("Fixed");
        lblOAFixed.setMaximumSize(new java.awt.Dimension(120, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panAutoRenewal2.add(lblOAFixed, gridBagConstraints);

        chkOAFixedValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkOAFixedValueActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panAutoRenewal2.add(chkOAFixedValue, gridBagConstraints);

        lblOAPecentage.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblOAPecentage.setText("Percentage");
        lblOAPecentage.setMaximumSize(new java.awt.Dimension(120, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 4);
        panAutoRenewal2.add(lblOAPecentage, gridBagConstraints);

        chkOAPercentageValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkOAPercentageValueActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panAutoRenewal2.add(chkOAPercentageValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 6, 1);
        panOAllowanceInfo.add(panAutoRenewal2, gridBagConstraints);

        lblBasedOnParameter.setText("Based on Parameter");
        lblBasedOnParameter.setName("lblCustomerID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panOAllowanceInfo.add(lblBasedOnParameter, gridBagConstraints);

        panRdoBasedOnParameter.setMinimumSize(new java.awt.Dimension(150, 27));
        panRdoBasedOnParameter.setName("panMaritalStatus");
        panRdoBasedOnParameter.setPreferredSize(new java.awt.Dimension(150, 27));
        panRdoBasedOnParameter.setLayout(new java.awt.GridBagLayout());

        rdgBasedOnParameter.add(rdoBasedOnParameter_Yes);
        rdoBasedOnParameter_Yes.setText("Yes");
        rdoBasedOnParameter_Yes.setMaximumSize(new java.awt.Dimension(60, 21));
        rdoBasedOnParameter_Yes.setMinimumSize(new java.awt.Dimension(60, 21));
        rdoBasedOnParameter_Yes.setName("rdoMaritalStatus_Single");
        rdoBasedOnParameter_Yes.setPreferredSize(new java.awt.Dimension(60, 21));
        rdoBasedOnParameter_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoBasedOnParameter_YesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panRdoBasedOnParameter.add(rdoBasedOnParameter_Yes, gridBagConstraints);

        rdgBasedOnParameter.add(rdoBasedOnParameter_No);
        rdoBasedOnParameter_No.setText("No");
        rdoBasedOnParameter_No.setMaximumSize(new java.awt.Dimension(60, 21));
        rdoBasedOnParameter_No.setMinimumSize(new java.awt.Dimension(60, 21));
        rdoBasedOnParameter_No.setName("rdoMaritalStatus_Married");
        rdoBasedOnParameter_No.setPreferredSize(new java.awt.Dimension(60, 21));
        rdoBasedOnParameter_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoBasedOnParameter_NoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panRdoBasedOnParameter.add(rdoBasedOnParameter_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        panOAllowanceInfo.add(panRdoBasedOnParameter, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panOtherAllowances.add(panOAllowanceInfo, gridBagConstraints);

        panOAllowances.setMinimumSize(new java.awt.Dimension(500, 403));
        panOAllowances.setPreferredSize(new java.awt.Dimension(500, 403));
        panOAllowances.setLayout(new java.awt.GridBagLayout());

        srpOAllowances.setMinimumSize(new java.awt.Dimension(200, 404));
        srpOAllowances.setPreferredSize(new java.awt.Dimension(200, 404));

        tblOAllowances.setModel(new javax.swing.table.DefaultTableModel(
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
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblOAllowances.setMinimumSize(new java.awt.Dimension(60, 64));
        tblOAllowances.setPreferredSize(new java.awt.Dimension(60, 10000));
        tblOAllowances.setOpaque(false);
        tblOAllowances.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblOAllowancesMouseClicked(evt);
            }
        });
        srpOAllowances.setViewportView(tblOAllowances);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.8;
        panOAllowances.add(srpOAllowances, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panOtherAllowances.add(panOAllowances, gridBagConstraints);

        tabSalaryStructure.addTab("Other Allowances", panOtherAllowances);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panSalaryStructure.add(tabSalaryStructure, gridBagConstraints);

        getContentPane().add(panSalaryStructure, java.awt.BorderLayout.CENTER);

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
        mitPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitPrintActionPerformed(evt);
            }
        });
        mnuProcess.add(mitPrint);

        mitClose.setText("Close");
        mitClose.setName("mitClose");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrMain.add(mnuProcess);

        setJMenuBar(mbrMain);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void rdoBasedOnParameter_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoBasedOnParameter_NoActionPerformed
        // TODO add your handling code here:
        cboOAParameterBasedOnValue.setVisible(false);
        lblOAParameterBasedon.setVisible(false);
        lblOASubParameter.setVisible(false);
        cboOASubParameterValue.setVisible(false);
        cboOAParameterBasedOnValue.setSelectedItem("");
        cboOASubParameterValue.setSelectedItem("");
    }//GEN-LAST:event_rdoBasedOnParameter_NoActionPerformed
    
    private void rdoBasedOnParameter_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoBasedOnParameter_YesActionPerformed
        // TODO add your handling code here:
        cboOAParameterBasedOnValue.setVisible(true);
        lblOAParameterBasedon.setVisible(true);
        lblOASubParameter.setVisible(true);
        cboOASubParameterValue.setVisible(true);
        cboOAParameterBasedOnValue.setSelectedItem("");
        cboOASubParameterValue.setSelectedItem("");
    }//GEN-LAST:event_rdoBasedOnParameter_YesActionPerformed
    
    private void tdtDAFromDateValueFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtDAFromDateValueFocusLost
        
    }//GEN-LAST:event_tdtDAFromDateValueFocusLost
    
    private void rdoIndexOrPercent_IndexActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoIndexOrPercent_IndexActionPerformed
        // TODO add your handling code here:
        visibleDA(true);
        rdoIndexOrPercent_Percent.setSelected(false);
        txtDATotalDAPercentageValue.setEnabled(false);
        txtDANoOfPointsPerSlabValue.setText("");
        txtDAIndexValue.setText("");
        txtDAPercentagePerSlabValue.setText("");
        txtTotalNoofSlabValue.setText("");
        txtDATotalDAPercentageValue.setText("");
    }//GEN-LAST:event_rdoIndexOrPercent_IndexActionPerformed
    
    private void rdoIndexOrPercent_PercentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoIndexOrPercent_PercentActionPerformed
        // TODO add your handling code here:
        visibleDA(false);
        rdoIndexOrPercent_Index.setSelected(false);
        txtDATotalDAPercentageValue.setEnabled(true);
        txtDANoOfPointsPerSlabValue.setText("");
        txtDAIndexValue.setText("");
        txtDAPercentagePerSlabValue.setText("");
        txtTotalNoofSlabValue.setText("");
        txtDATotalDAPercentageValue.setText("");
    }//GEN-LAST:event_rdoIndexOrPercent_PercentActionPerformed
    private void visibleDA(boolean DAVisible){
        txtDANoOfPointsPerSlabValue.setVisible(DAVisible);
        lblDANoOfPointsPerSlab.setVisible(DAVisible);
        txtDAIndexValue.setVisible(DAVisible);
        lblDAIndex.setVisible(DAVisible);
        lblDATotalNoofSlab.setVisible(DAVisible);
        txtDAPercentagePerSlabValue.setVisible(DAVisible);
        lblTotalDaPer.setVisible(DAVisible);
        txtTotalNoofSlabValue.setVisible(DAVisible);
    }
    private void rdoPercentOrFixed_FixedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoPercentOrFixed_FixedActionPerformed
        // TODO add your handling code here:
        txtCCAllowanceStartingAmtValue.setValidation(new CurrencyValidation(14,2));
        lblCCAllowanceStartingAmt.setText(resourceBundle.getString("lblCCAllowanceStartingAmt2"));
        txtCCAllowanceStartingAmtValue.setText("");
    }//GEN-LAST:event_rdoPercentOrFixed_FixedActionPerformed
    
    private void rdoPercentOrFixed_PercentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoPercentOrFixed_PercentActionPerformed
        // TODO add your handling code here:
        txtCCAllowanceStartingAmtValue.setValidation(new NumericValidation(2,2));
        lblCCAllowanceStartingAmt.setText(resourceBundle.getString("lblCCAllowanceStartingAmt"));
        txtCCAllowanceStartingAmtValue.setText("");
    }//GEN-LAST:event_rdoPercentOrFixed_PercentActionPerformed
    
    private void txtNooflitresValueFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNooflitresValueFocusLost
        // TODO add your handling code here:
        if(CommonUtil.convertObjToDouble(txtPricePerlitreValue.getText()).doubleValue()>0 &&
        CommonUtil.convertObjToDouble(txtNooflitresValue.getText()).doubleValue()>0){
            double noOfLitre = CommonUtil.convertObjToDouble(txtNooflitresValue.getText()).doubleValue();
            double perlitreAmt = CommonUtil.convertObjToDouble(txtPricePerlitreValue.getText()).doubleValue();
            double totalAmt = noOfLitre * perlitreAmt;
            txtTotalConveyanceAmtValue.setText(String.valueOf(totalAmt));
        }
    }//GEN-LAST:event_txtNooflitresValueFocusLost
    
    private void txtPricePerlitreValueFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPricePerlitreValueFocusLost
        // TODO add your handling code here:
        if(CommonUtil.convertObjToDouble(txtPricePerlitreValue.getText()).doubleValue()>0 &&
        CommonUtil.convertObjToDouble(txtNooflitresValue.getText()).doubleValue()>0){
            double noOfLitre = CommonUtil.convertObjToDouble(txtNooflitresValue.getText()).doubleValue();
            double perlitreAmt = CommonUtil.convertObjToDouble(txtPricePerlitreValue.getText()).doubleValue();
            double totalAmt = noOfLitre * perlitreAmt;
            txtTotalConveyanceAmtValue.setText(String.valueOf(totalAmt));
        }
    }//GEN-LAST:event_txtPricePerlitreValueFocusLost
    
    private void btnOADeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOADeleteActionPerformed
        // TODO add your handling code here:
        int rowCount = tblOAllowances.getRowCount();
        int rowSelected = tblOAllowances.getSelectedRow();
        if(observable.getOAAuthorizeStatus()!=null && (observable.getOAAuthorizeStatus().equals("AUTHORIZED") ||
        observable.getOAAuthorizeStatus().equals("INACTIVE"))){
            ClientUtil.showAlertWindow("Can not delete this record.Already authorized");
            return;
        }else{
            if((rowCount-1) == rowSelected){
                observable.deleteOAData(this.tblOAllowances.getSelectedRow());
                this.updateTable();
                resetOAForm();
                observable.resetOAValues();
                enableDisableOA(false);
                MASave = false;
            }else{
                ClientUtil.showAlertWindow("Can not delete this record.Delete from the last record");
                return;
            }
        }
    }//GEN-LAST:event_btnOADeleteActionPerformed
    
    private void tblOAllowancesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblOAllowancesMouseClicked
        // TODO add your handling code here:
        selectedSingleRow = true;
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE){
            updationOA(tblOAllowances.getSelectedRow());
            enableDisableOA(false);
        }else if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
            if(tblOAllowances.getSelectedRow()>=0){
                updationOA(tblOAllowances.getSelectedRow());
                txtMAidAmtValue.setEnabled(true);
                if(observable.getChkOAFixedValue() == true){
                    lblOAFixedAmt.setVisible(true);
                    txtOAFixedAmtValue.setVisible(true);
                    lblOAPercentage.setVisible(false);
                    lblOAMaximumOf.setVisible(false);
                    txtOAMaximumOfValue.setVisible(false);
                    txtOAPercentageValue.setVisible(false);
                }
                if(observable.getChkOAPercentageValue() == true){
                    lblOAFixedAmt.setVisible(false);
                    txtOAFixedAmtValue.setVisible(false);
                    lblOAPercentage.setVisible(true);
                    lblOAMaximumOf.setVisible(true);
                    txtOAMaximumOfValue.setVisible(true);
                    txtOAPercentageValue.setVisible(true);
                }
                if(observable.getOAbasedOnParameter().equals("Y")){
                    rdoBasedOnParameter_Yes.setSelected(true);
                    rdoBasedOnParameter_No.setSelected(false);
                    rdoBasedOnParameter_YesActionPerformed(null);
                }else if(observable.getOAbasedOnParameter().equals("N")){
                    rdoBasedOnParameter_Yes.setSelected(false);
                    rdoBasedOnParameter_No.setSelected(true);
                    rdoBasedOnParameter_NoActionPerformed(null);
                }
                if(observable.getOAAuthorizeStatus()!=null && (observable.getOAAuthorizeStatus().equals("AUTHORIZED") ||
                observable.getOAAuthorizeStatus().equals("INACTIVE"))){
                    ClientUtil.enableDisable(panOAllowanceInfo,false);
                    btnOANew.setEnabled(true);
                    btnOASave.setEnabled(false);
                    btnOADelete.setEnabled(false);
                    OASave = false;
                }else{
                    btnOANew.setEnabled(false);
                    btnOASave.setEnabled(true);
                    btnOADelete.setEnabled(true);
                    txtOAFixedAmtValue.setEnabled(true);
                    txtOAMaximumOfValue.setEnabled(true);
                    txtOAPercentageValue.setEnabled(true);
                    
                    
                    cboOASubParameterValue.setEnabled(true);
                    OASave = true;
                    _intOANew = false;
                }
                if(cboOAParameterBasedOnValue.getSelectedItem().equals("")){
                    cboOASubParameterValue.setEnabled(false);
                }else{
                    cboOASubParameterValue.setEnabled(true);
                }
            }
        }else{
            ClientUtil.enableDisable(this,false);
        }
        String parameters = "";
        parameters = CommonUtil.convertObjToStr(tblOAllowances.getValueAt(tblOAllowances.getSelectedRow(), 1));
        if(parameters.equals("")){
            //            cboOAParameterBasedOnValue.setVisible(false);
            rdoBasedOnParameter_No.setSelected(true);
            cboOAParameterBasedOnValue.setVisible(false);
            lblOAParameterBasedon.setVisible(false);
            lblOASubParameter.setVisible(false);
            cboOASubParameterValue.setVisible(false);
            
        }else{
            rdoBasedOnParameter_Yes.setSelected(true);
            cboOAParameterBasedOnValue.setVisible(true);
            lblOAParameterBasedon.setVisible(true);
            lblOASubParameter.setVisible(true);
            cboOASubParameterValue.setVisible(true);
        }
    }//GEN-LAST:event_tblOAllowancesMouseClicked
    
    private void btnOASaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOASaveActionPerformed
        // TODO add your handling code here:
        
        //        if(rdoOtherAllowance_Yes.isSelected()){
        if(cboOADesignationValue.getSelectedIndex()==0 && cboOADesignationValue.getSelectedIndex()<=0){
            ClientUtil.showAlertWindow(designationAlert);
        }else if(tdtOAFromDateValue.getDateValue().length() == 0){
            ClientUtil.showAlertWindow(fromDateAlert);
            //        }else if(cboOAllowanceTypeValue.getSelectedIndex()==0 && cboOAllowanceTypeValue.getSelectedIndex()<=0){
            //            ClientUtil.showAlertWindow("Allowance Type Should not be empty");
        }else if(chkOAFixedValue.isSelected() == false && chkOAPercentageValue.isSelected() == false){
            ClientUtil.showAlertWindow("Select either Fixed or Percentage");
        }else if(chkOAFixedValue.isSelected() == true && txtOAFixedAmtValue.getText().length() == 0){
            ClientUtil.showAlertWindow("Fixed Amount should not be empty");
        }else if(chkOAPercentageValue.isSelected() == true && txtOAMaximumOfValue.getText().length() == 0){
            ClientUtil.showAlertWindow("Percentage should not be empty");
        }else if(chkOAPercentageValue.isSelected() == true && txtOAPercentageValue.getText().length() == 0){
            ClientUtil.showAlertWindow("Maximum Amount should not be empty");
        }else{
            long OACboSize = 1;
            if(this._intOANew && tblOAllowances.getRowCount()>0){
                for (int i = 0;i<tblOAllowances.getRowCount();i++){
                    if(cboOASubParameterValue.getSelectedIndex()>0 && cboOASubParameterValue.getSelectedItem().toString().toUpperCase().equals(tblOAllowances.getValueAt(i, 3)) && cboOAllowanceTypeValue.getSelectedItem().toString().toUpperCase().equals(tblOAllowances.getValueAt(i, 1)) ){
                        ClientUtil.showAlertWindow("This value already exists please choose another value");
                        return;
                    }else{
                        OACboSize = OACboSize +1;
                    }
                }
            }
        }
        //        }else if(rdoOtherAllowance_No.isSelected()){
        //            System.out.println("@##@$@#$inside here !!!!");
        //        }
        btnSave.setEnabled(true);
        updateOBFields();
        if(!this._intOANew){
            observable.insertOAData(this.tblOAllowances.getSelectedRow());
        }else{
            observable.insertOAData(-1);
        }
        resetOAForm();
        enableDisableOA(false);
        this.updateTable();
        btnOANew.setEnabled(true);
        btnOASave.setEnabled(false);
        btnOADelete.setEnabled(false);
        this._intOANew = false;
        selectedSingleRow = false;
        MASave = false;
    }//GEN-LAST:event_btnOASaveActionPerformed
    private void enableDisableOA(boolean MAVal){
        cboOADesignationValue.setEnabled(MAVal);
        tdtOAFromDateValue.setEnabled(MAVal);
        tdtOAToDateValue.setEnabled(MAVal);
        cboOAllowanceTypeValue.setEnabled(MAVal);
        cboOAParameterBasedOnValue.setEnabled(MAVal);
        cboOASubParameterValue.setEnabled(MAVal);
        chkOAFixedValue.setEnabled(MAVal);
        chkOAPercentageValue.setEnabled(MAVal);
        txtOAFixedAmtValue.setEnabled(MAVal);
        txtOAMaximumOfValue.setEnabled(MAVal);
        txtOAPercentageValue.setEnabled(MAVal);
        
        
    }
    
    private void btnOANewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOANewActionPerformed
        
        // TODO add your handling code here:
        
        _intOANew = true;
        ClientUtil.enableDisable(panOAllowanceInfo,true);
        btnOASave.setEnabled(true);
        cboOADesignationValue.setEnabled(false);
        tdtOAFromDateValue.setEnabled(false);
        if(tblOAllowances.getRowCount() == 0){
            enableDisableOA(true);
            btnOASave.setEnabled(true);
        }else if(tblOAllowances.getRowCount() > 0){
            updationOA(tblOAllowances.getRowCount()-1);
            //            if(tblHaltingAllowances.getRowCount()>0){
            String haltingParameter = cboOAParameterBasedOnValue.getSelectedItem().toString().toUpperCase();
            HashMap haltingMap = new HashMap();
            haltingMap.put("LOOKUP_ID",haltingParameter);
            System.out.println("@!#$@#$haltingMap:"+haltingMap);
            List countList = ClientUtil.executeQuery("getCountOfHaltingParameter",haltingMap);
            haltingMap = (HashMap)countList.get(0);
            int count = 0;
            int countParameter = CommonUtil.convertObjToInt(haltingMap.get("CNT"));
            for (int i = 0;i<tblOAllowances.getRowCount();i++){
                System.out.println("@#$@#$@#$countParameter:"+countParameter);
                if(cboOAParameterBasedOnValue.getSelectedItem().toString().toUpperCase().equals(tblOAllowances.getValueAt(i, 2)) &&
                cboOAllowanceTypeValue.getSelectedItem().toString().toUpperCase().equals(tblOAllowances.getValueAt(i, 1))){
                    count +=1;
                }
            }
            if(countParameter == count ){
                cboOAParameterBasedOnValue.setSelectedItem("");
                cboOAParameterBasedOnValue.setEnabled(true);
            }
            else{
                cboOAParameterBasedOnValue.setEnabled(false);
            }
            //            }
            resetOAForm();
            if(cboOAParameterBasedOnValue.getSelectedItem().equals("none")){
                cboOASubParameterValue.setEnabled(false);
            }else{
                cboOASubParameterValue.setEnabled(true);
            }
            //            if(cboOAParameterBasedOnValue.getSelectedIndex()>0){
            //                cboOAParameterBasedOnValue.setEnabled(false);
            //            }else{
            //                cboOAParameterBasedOnValue.setEnabled(true);
            //            }
            //            cboOAllowanceTypeValue.setEnabled(false);
            chkOAFixedValue.setEnabled(true);
            chkOAPercentageValue.setEnabled(true);
            btnOASave.setEnabled(true);
        }
        btnOASave.setEnabled(true);
        btnOANew.setEnabled(false);
    }//GEN-LAST:event_btnOANewActionPerformed
    
    private void cboOAParameterBasedOnValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboOAParameterBasedOnValueActionPerformed
        // TODO add your handling code here:
        if(cboOAParameterBasedOnValue.getSelectedIndex()>0){
            if(cboOAParameterBasedOnValue.getSelectedItem().equals("none")){
                cboOASubParameterValue.setEnabled(false);
            }else{
                cboOASubParameterValue.setEnabled(true);
                observable.setCbmProd(CommonUtil.convertObjToStr(cboOAParameterBasedOnValue.getSelectedItem()));
                cboOASubParameterValue.setModel(observable.getCbmOASubParameterValue());
            }
        }
    }//GEN-LAST:event_cboOAParameterBasedOnValueActionPerformed
    
    private void chkOAPercentageValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkOAPercentageValueActionPerformed
        // TODO add your handling code here:
        if(chkOAPercentageValue.isSelected() == true){
            chkOAFixedValue.setEnabled(true);
            chkOAFixedValue.setSelected(false);
            txtOAFixedAmtValue.setVisible(false);
            lblOAFixedAmt.setVisible(false);
            lblOAPercentage.setVisible(true);
            lblOAMaximumOf.setVisible(true);
            txtOAPercentageValue.setVisible(true);
            txtOAMaximumOfValue.setVisible(true);
        }else if(chkOAFixedValue.isSelected() == true){
            chkOAPercentageValue.setEnabled(true);
            chkOAFixedValue.setSelected(false);
            lblOAPercentage.setVisible(true);
            lblOAMaximumOf.setVisible(true);
            txtOAPercentageValue.setVisible(false);
            txtOAMaximumOfValue.setVisible(false);
            txtOAFixedAmtValue.setVisible(false);
            lblOAFixedAmt.setVisible(false);
        }else{
            chkOAFixedValue.setSelected(false);
            chkOAFixedValue.setEnabled(true);
            txtOAFixedAmtValue.setVisible(false);
            lblOAFixedAmt.setVisible(false);
            lblOAPercentage.setVisible(false);
            lblOAMaximumOf.setVisible(false);
            txtOAPercentageValue.setVisible(false);
            txtOAMaximumOfValue.setVisible(false);
        }
        txtOAFixedAmtValue.setText("");
        txtOAPercentageValue.setText("");
        txtOAMaximumOfValue.setText("");
    }//GEN-LAST:event_chkOAPercentageValueActionPerformed
    
    private void chkOAFixedValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkOAFixedValueActionPerformed
        // TODO add your handling code here:
        if(chkOAFixedValue.isSelected() == true){
            chkOAPercentageValue.setEnabled(true);
            chkOAPercentageValue.setSelected(false);
            txtOAFixedAmtValue.setVisible(true);
            lblOAFixedAmt.setVisible(true);
            lblOAPercentage.setVisible(false);
            lblOAMaximumOf.setVisible(false);
            txtOAPercentageValue.setVisible(false);
            txtOAMaximumOfValue.setVisible(false);
        }else if(chkOAPercentageValue.isSelected() == true){
            chkOAFixedValue.setEnabled(true);
            chkOAFixedValue.setSelected(false);
            chkOAPercentageValue.setSelected(false);
            lblOAPercentage.setVisible(false);
            lblOAMaximumOf.setVisible(false);
            txtOAPercentageValue.setVisible(true);
            txtOAMaximumOfValue.setVisible(true);
            txtOAFixedAmtValue.setVisible(true);
            lblOAFixedAmt.setVisible(true);
        }else{
            chkOAPercentageValue.setSelected(false);
            chkOAPercentageValue.setEnabled(true);
            txtOAFixedAmtValue.setVisible(false);
            lblOAFixedAmt.setVisible(false);
            lblOAPercentage.setVisible(false);
            lblOAMaximumOf.setVisible(false);
            txtOAPercentageValue.setVisible(false);
            txtOAMaximumOfValue.setVisible(false);
        }
        txtOAFixedAmtValue.setText("");
        txtOAPercentageValue.setText("");
        txtOAMaximumOfValue.setText("");
        
    }//GEN-LAST:event_chkOAFixedValueActionPerformed
    
    private void txtSalaryStructureEndingAmtValueFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSalaryStructureEndingAmtValueFocusLost
        // TODO add your handling code here:
        double starting = CommonUtil.convertObjToDouble(txtSalaryStructureStartingAmtValue.getText()).doubleValue();
        double ending = CommonUtil.convertObjToDouble(txtSalaryStructureEndingAmtValue.getText()).doubleValue();
        if(starting>ending){
            ClientUtil.showAlertWindow("End amount should be greater than starting basic amount");
            txtSalaryStructureEndingAmtValue.setText("");
        }
    }//GEN-LAST:event_txtSalaryStructureEndingAmtValueFocusLost
    private void updationOA(int selectDARow) {
        observable.populateOA(selectDARow);
        populateDetail();
        _intMANew = false;
    }
    private void btnMAidDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMAidDeleteActionPerformed
        // TODO add your handling code here:
        int rowCount = tblMaid.getRowCount();
        int rowSelected = tblMaid.getSelectedRow();
        if(observable.getMAAuthorizeStatus()!=null && (observable.getMAAuthorizeStatus().equals("AUTHORIZED") ||
        observable.getMAAuthorizeStatus().equals("INACTIVE"))){
            ClientUtil.showAlertWindow("Can not delete this record.Already authorized");
            return;
        }else{
            if((rowCount-1) == rowSelected){
                observable.deleteMAData(this.tblMaid.getSelectedRow());
                this.updateTable();
                resetMAForm();
                observable.resetMAValues();
                enableDisableMA(false);
                MASave = false;
            }else{
                ClientUtil.showAlertWindow("Can not delete this record.Delete from the last record");
                return;
            }
        }
    }//GEN-LAST:event_btnMAidDeleteActionPerformed
    private void chkOAllowancePercentage(boolean OAValue){
        lblOAPercentage.setVisible(OAValue);
        lblOAMaximumOf.setVisible(OAValue);
        txtOAPercentageValue.setVisible(OAValue);
        txtOAMaximumOfValue.setVisible(OAValue);
    }
    private void btnTADeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTADeleteActionPerformed
        // TODO add your handling code here:
        int rowCount = tblTravellingAllowance.getRowCount();
        int rowSelected = tblTravellingAllowance.getSelectedRow();
        if(observable.getTAAuthorizeStatus()!=null && (observable.getTAAuthorizeStatus().equals("AUTHORIZED") ||
        observable.getTAAuthorizeStatus().equals("INACTIVE"))){
            ClientUtil.showAlertWindow("Can not delete this record.Already authorized");
            return;
        }else{
            if((rowCount-1) == rowSelected){
                observable.deleteTAData(this.tblTravellingAllowance.getSelectedRow());
                this.updateTable();
                resetTAForm();
                observable.resetTAValues();
                enableDisableTA(false);
                TASave = false;
            }else{
                ClientUtil.showAlertWindow("Can not delete this record.Delete from the last record");
                return;
            }
        }
        
    }//GEN-LAST:event_btnTADeleteActionPerformed
    private void chkOAllowanceFixed(boolean OAValue){
        txtOAFixedAmtValue.setVisible(OAValue);
        lblOAFixedAmt.setVisible(OAValue);
    }
    private void tblMaidMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMaidMouseClicked
        // TODO add your handling code here:
        selectedSingleRow = true;
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE){
            updationMA(tblMaid.getSelectedRow());
            enableDisableMA(false);
        }else if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
            if(tblMaid.getSelectedRow()>=0){
                updationMA(tblMaid.getSelectedRow());
                txtMAidAmtValue.setEnabled(true);
                if(observable.getMAAuthorizeStatus()!=null && (observable.getMAAuthorizeStatus().equals("AUTHORIZED") ||
                observable.getMAAuthorizeStatus().equals("INACTIVE"))){
                    ClientUtil.enableDisable(panMedicalAllowanceInfo,false);
                    btnMAidNew.setEnabled(true);
                    btnMAidSave.setEnabled(false);
                    btnMAidDelete.setEnabled(false);
                    MASave = false;
                }else{
                    btnMAidNew.setEnabled(false);
                    btnMAidSave.setEnabled(true);
                    btnMAidDelete.setEnabled(true);
                    MASave = true;
                    _intMANew = false;
                }
            }
        }else{
            ClientUtil.enableDisable(this,false);
        }
    }//GEN-LAST:event_tblMaidMouseClicked
    private void updationMA(int selectDARow) {
        observable.populateMA(selectDARow);
        populateDetail();
        _intMANew = false;
    }
    private void tblTravellingAllowanceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTravellingAllowanceMouseClicked
        // TODO add your handling code here:
        selectedSingleRow = true;
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE){
            updationTA(tblTravellingAllowance.getSelectedRow());
            enableDisableTA(false);
        }else if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
            if(tblTravellingAllowance.getSelectedRow()>=0){
                updationTA(tblTravellingAllowance.getSelectedRow());
                enableDisableTA(true);
                tdtTAToDateValue.setEnabled(false);
                chkFixedConveyance.setEnabled(false);
                chkPetrolAllowance.setEnabled(false);
                cboTravellingAllowance.setEnabled(false);
                if(observable.getTAAuthorizeStatus()!=null && (observable.getTAAuthorizeStatus().equals("AUTHORIZED") ||
                observable.getTAAuthorizeStatus().equals("INACTIVE"))){
                    ClientUtil.enableDisable(panTravellingAllowanceInfo,false);
                    btnTANew.setEnabled(true);
                    btnTASave.setEnabled(false);
                    btnTADelete.setEnabled(false);
                    TASave = false;
                }else{
                    btnTANew.setEnabled(false);
                    btnTASave.setEnabled(true);
                    btnTADelete.setEnabled(true);
                    TASave = true;
                    _intTANew = false;
                }
            }
        }else{
            ClientUtil.enableDisable(this,false);
        }
        if(observable.getChkFixedConveyance() == true){
            chkFixedConveyanceVisible(true);
            chkPetrolAllowanceVisible(false);
            //                lblBasicAmtUpto.setEnabled(false);
            //                lblConveyancePerMonth.setEnabled(false);
            //                lblBasicAmtBeyond.setEnabled(false);
            //                lblConveyanceAmt.setEnabled(false);
        }else if(observable.getChkPetrolAllowance() == true){
            chkPetrolAllowanceVisible(true);
            chkFixedConveyanceVisible(false);
            //                lblNoOflitres.setEnabled(false);
            //                lblPricePerlitre.setEnabled(false);
            //                lblTotalConveyanceAmt.setEnabled(false);
        }
        
    }//GEN-LAST:event_tblTravellingAllowanceMouseClicked
    private void resetOAForm(){
        //        cboOADesignationValue.setSelectedItem("");
        //        tdtOAFromDateValue.setDateValue("");
        //        tdtOAToDateValue.setDateValue("");
        //        cboOAllowanceTypeValue.setSelectedItem("");
        //        cboOAParameterBasedOnValue.setSelectedItem("");
        cboOASubParameterValue.setSelectedItem("");
        txtOAFixedAmtValue.setText("");
        txtOAMaximumOfValue.setText("");
        txtOAPercentageValue.setText("");
        
    }
    
    private void btnMAidSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMAidSaveActionPerformed
        // TODO add your handling code here:
        if(cboMAidDesg.getSelectedIndex()==0 && cboMAidDesg.getSelectedIndex()<=0){
            ClientUtil.showAlertWindow(designationAlert);
        }else if(tdtMAidFromDateValue.getDateValue().length() == 0){
            ClientUtil.showAlertWindow(fromDateAlert);
        }else if(txtMAidAmtValue.getText().length() == 0){
            ClientUtil.showAlertWindow("Medical allowance should not be empty");
        }else{
            btnSave.setEnabled(true);
            //            enableDisableButtons(false);
            updateOBFields();
            if(!this._intMANew){
                observable.insertMAData(this.tblMaid.getSelectedRow());
            }else{
                observable.insertMAData(-1);
            }
            resetMAForm();
            enableDisableMA(false);
            this.updateTable();
            btnMAidNew.setEnabled(true);
            btnMAidSave.setEnabled(false);
            btnMAidDelete.setEnabled(false);
            this._intMANew = false;
            selectedSingleRow = false;
            MASave = false;
        }
    }//GEN-LAST:event_btnMAidSaveActionPerformed
    private void resetMAForm(){
        //        cboMAidDesg.setSelectedItem("");
        //        tdtMAidFromDateValue.setDateValue("");
        tdtMAidToDateValue.setDateValue("");
        txtMAidAmtValue.setText("");
    }
    private void btnMAidNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMAidNewActionPerformed
        // TODO add your handling code here:
        if(tblMaid.getRowCount() == 0){
            enableDisableMA(false);
            //            tdtMAidToDateValue.setEnabled(true);
        }else if(tblMaid.getRowCount() >0){
            updationMA(tblMaid.getRowCount()-1);
            enableDisableMA(false);
            txtMAidAmtValue.setText("");
        }
        txtMAidAmtValue.setEnabled(true);
        _intMANew = true;
        btnMAidNew.setEnabled(false);
        btnMAidSave.setEnabled(true);
        btnMAidDelete.setEnabled(false);
    }//GEN-LAST:event_btnMAidNewActionPerformed
    private void enableDisableMA(boolean MAVal){
        cboMAidDesg.setEnabled(MAVal);
        tdtMAidFromDateValue.setEnabled(MAVal);
        //        tdtMAidToDateValue.setEnabled(MAVal);
        txtMAidAmtValue.setEnabled(MAVal);
    }
    private void lblSalaryStructureFromDateValueFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_lblSalaryStructureFromDateValueFocusLost
        // TODO add your handling code here:
        tdtCCAllowanceFromDateValue.setDateValue(lblSalaryStructureFromDateValue.getDateValue());
        tdtHRAllowanceFromDateValue.setDateValue(lblSalaryStructureFromDateValue.getDateValue());
        tdtMAidFromDateValue.setDateValue(lblSalaryStructureFromDateValue.getDateValue());
        tdtOAFromDateValue.setDateValue(lblSalaryStructureFromDateValue.getDateValue());
    }//GEN-LAST:event_lblSalaryStructureFromDateValueFocusLost
    
    private void chkPetrolAllowanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkPetrolAllowanceActionPerformed
        // TODO add your handling code here:
        if(chkPetrolAllowance.isSelected() == true){
            chkFixedConveyance.setSelected(false);
            chkFixedConveyance.setEnabled(false);
            chkFixedConveyanceVisible(false);
            chkFixedConveyanceEnableDisable(false);
            chkPetrolAllowanceVisible(true);
            chkPetrolAllowanceEnableDisable(true);
        }else if(chkFixedConveyance.isSelected() == true){
            chkFixedConveyanceVisible(true);
            chkFixedConveyanceEnableDisable(true);
            chkPetrolAllowanceVisible(false);
            chkPetrolAllowanceEnableDisable(false);
        }else if(chkPetrolAllowance.isSelected() == false){
            chkFixedConveyance.setEnabled(true);
            chkPetrolAllowanceVisible(false);
            chkPetrolAllowanceEnableDisable(false);
            //        }else{
            //            chkFixedConveyanceVisible(false);
            //            chkFixedConveyanceEnableDisable(false);
        }
    }//GEN-LAST:event_chkPetrolAllowanceActionPerformed
    private void chkPetrolAllowanceVisible(boolean chkPetrol){
        lblNoOflitres.setVisible(chkPetrol);
        txtNooflitresValue.setVisible(chkPetrol);
        lblPricePerlitre.setVisible(chkPetrol);
        txtPricePerlitreValue.setVisible(chkPetrol);
        lblTotalConveyanceAmt.setVisible(chkPetrol);
        txtTotalConveyanceAmtValue.setVisible(chkPetrol);
    }
    private void chkPetrolAllowanceEnableDisable(boolean chkPetrol){
        txtNooflitresValue.setEnabled(chkPetrol);
        txtPricePerlitreValue.setEnabled(chkPetrol);
        
        txtNooflitresValue.setText("");
        txtPricePerlitreValue.setText("");
        txtTotalConveyanceAmtValue.setText("");
    }
    private void chkFixedConveyanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkFixedConveyanceActionPerformed
        if(chkFixedConveyance.isSelected() == true){
            chkPetrolAllowance.setSelected(false);
            chkPetrolAllowance.setEnabled(false);
            chkFixedConveyanceVisible(true);
            chkFixedConveyanceEnableDisable(true);
            chkPetrolAllowanceVisible(false);
            chkPetrolAllowanceEnableDisable(false);
        }else if(chkPetrolAllowance.isSelected() == true){
            chkPetrolAllowanceVisible(true);
            chkPetrolAllowanceEnableDisable(true);
            chkFixedConveyanceVisible(false);
            chkFixedConveyanceEnableDisable(false);
        }else if(chkFixedConveyance.isSelected() == false){
            chkPetrolAllowance.setEnabled(true);
            chkFixedConveyanceVisible(false);
            chkFixedConveyanceEnableDisable(false);
        }
    }//GEN-LAST:event_chkFixedConveyanceActionPerformed
    private void chkFixedConveyanceVisible(boolean chkFixed){
        lblBasicAmtUpto.setVisible(chkFixed);
        txtBasicAmtUptoValue.setVisible(chkFixed);
        lblConveyancePerMonth.setVisible(chkFixed);
        txtConveyancePerMonthValue.setVisible(chkFixed);
        lblBasicAmtBeyond.setVisible(chkFixed);
        txtBasicAmtBeyondValue.setVisible(chkFixed);
        lblConveyanceAmt.setVisible(chkFixed);
        txtConveyanceAmtValue.setVisible(chkFixed);
    }
    private void btnHRAllowanceDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHRAllowanceDeleteActionPerformed
       int rowCount = tblHRAllowance.getRowCount();
        int rowSelected = tblHRAllowance.getSelectedRow();
        if(observable.getHRAAuthorizeStatus()!=null && (observable.getHRAAuthorizeStatus().equals("AUTHORIZED") ||
        observable.getHRAAuthorizeStatus().equals("INACTIVE"))){
            ClientUtil.showAlertWindow("Can not delete this record.Already authorized");
            return;
        }else{
            if((rowCount-1) == rowSelected){
                observable.deleteHRAData(this.tblHRAllowance.getSelectedRow());
                this.updateTable();
                resetHRAForm();
                observable.resetHRAValues();
                enableDisableHRA(false);
                HRASave = false;
            }else{
                ClientUtil.showAlertWindow("Can not delete this record.Delete from the last record");
                return;
            }
        }
    }//GEN-LAST:event_btnHRAllowanceDeleteActionPerformed
    private void chkFixedConveyanceEnableDisable(boolean chkFixed){
        txtBasicAmtUptoValue.setEnabled(chkFixed);
        txtConveyancePerMonthValue.setEnabled(chkFixed);
        txtBasicAmtBeyondValue.setEnabled(chkFixed);
        txtConveyanceAmtValue.setEnabled(chkFixed);
        
        txtBasicAmtUptoValue.setText("");
        txtConveyancePerMonthValue.setText("");
        txtBasicAmtBeyondValue.setText("");
        txtConveyanceAmtValue.setText("");
    }
    private void btnCCAllowanceDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCCAllowanceDeleteActionPerformed
        int rowCount = tblCCAllowance.getRowCount();
        int rowSelected = tblCCAllowance.getSelectedRow();
        if(observable.getCCAAuthorizeStatus()!=null && (observable.getCCAAuthorizeStatus().equals("AUTHORIZED") ||
        observable.getCCAAuthorizeStatus().equals("INACTIVE"))){
            ClientUtil.showAlertWindow("Can not delete this record.Already authorized");
            return;
        }else{
            if((rowCount-1) == rowSelected){
                observable.deleteCCAData(this.tblCCAllowance.getSelectedRow());
                this.updateTable();
                resetCCAForm();
                observable.resetCCAValues();
                enableDisableCCA(false);
                CCASave = false;
            }else{
                ClientUtil.showAlertWindow("Can not delete this record.Delete from the last record");
                return;
            }
        }
    }//GEN-LAST:event_btnCCAllowanceDeleteActionPerformed
    
    private void btnDADeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDADeleteActionPerformed
        int rowCount = tblDA.getRowCount();
        int rowSelected = tblDA.getSelectedRow();
        if(observable.getDAAuthorizeStatus()!=null && (observable.getDAAuthorizeStatus().equals("AUTHORIZED") ||
        observable.getDAAuthorizeStatus().equals("INACTIVE"))){
            ClientUtil.showAlertWindow("Can not delete this record.Already authorized");
            return;
        }else{
            if((rowCount-1) == rowSelected){
                observable.deleteDAData(this.tblDA.getSelectedRow());
                this.updateTable();
                resetDAForm();
                observable.resetDAValues();
                enableDisableDA(false);
                DASave = false;
            }else{
                ClientUtil.showAlertWindow("Can not delete this record.Delete from the last record");
                return;
            }
        }
    }//GEN-LAST:event_btnDADeleteActionPerformed
    
    private void txtDAIndexValueFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDAIndexValueFocusLost
        if(CommonUtil.convertObjToDouble(txtDANoOfPointsPerSlabValue.getText()).doubleValue() >0 &&
        CommonUtil.convertObjToDouble(txtDAIndexValue.getText()).doubleValue() >0 &&
        CommonUtil.convertObjToDouble(txtDAPercentagePerSlabValue.getText()).doubleValue() >0){
            double points = CommonUtil.convertObjToDouble(txtDANoOfPointsPerSlabValue.getText()).doubleValue();
            double index = CommonUtil.convertObjToDouble(txtDAIndexValue.getText()).doubleValue();
            double daPer = CommonUtil.convertObjToDouble(txtDAPercentagePerSlabValue.getText()).doubleValue();
            double TotNoOfSlab = index/points;
            double daPerSlab = TotNoOfSlab * daPer;
            txtTotalNoofSlabValue.setText(String.valueOf(TotNoOfSlab));
            txtDATotalDAPercentageValue.setText(String.valueOf(daPerSlab));
        }
    }//GEN-LAST:event_txtDAIndexValueFocusLost
    
    private void txtDANoOfPointsPerSlabValueFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDANoOfPointsPerSlabValueFocusLost
       if(CommonUtil.convertObjToDouble(txtDANoOfPointsPerSlabValue.getText()).doubleValue() >0 &&
        CommonUtil.convertObjToDouble(txtDAIndexValue.getText()).doubleValue() >0 &&
        CommonUtil.convertObjToDouble(txtDAPercentagePerSlabValue.getText()).doubleValue() >0){
            double points = CommonUtil.convertObjToDouble(txtDANoOfPointsPerSlabValue.getText()).doubleValue();
            double index = CommonUtil.convertObjToDouble(txtDAIndexValue.getText()).doubleValue();
            double daPer = CommonUtil.convertObjToDouble(txtDAPercentagePerSlabValue.getText()).doubleValue();
            double TotNoOfSlab = index/points;
            double daPerSlab = TotNoOfSlab * daPer;
            txtTotalNoofSlabValue.setText(String.valueOf(TotNoOfSlab));
            txtDATotalDAPercentageValue.setText(String.valueOf(daPerSlab));
        }
    }//GEN-LAST:event_txtDANoOfPointsPerSlabValueFocusLost
    
    private void txtDAPercentagePerSlabValueFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDAPercentagePerSlabValueFocusLost
        DecimalFormat df=new DecimalFormat("0.000");
        if(CommonUtil.convertObjToDouble(txtDANoOfPointsPerSlabValue.getText()).doubleValue() >0 &&
        CommonUtil.convertObjToDouble(txtDAIndexValue.getText()).doubleValue() >0 &&
        CommonUtil.convertObjToDouble(txtDAPercentagePerSlabValue.getText()).doubleValue() >0){
            double points = CommonUtil.convertObjToDouble(txtDANoOfPointsPerSlabValue.getText()).doubleValue();
            double index = CommonUtil.convertObjToDouble(txtDAIndexValue.getText()).doubleValue();
            double daPer = CommonUtil.convertObjToDouble(txtDAPercentagePerSlabValue.getText()).doubleValue();
            double TotNoOfSlab = index/points;
            TotNoOfSlab = (double)getNearest((long)(TotNoOfSlab *100),100)/100;
            double daPerSlab = TotNoOfSlab * daPer;
            daPerSlab = Double.parseDouble(df.format(daPerSlab));
            TotNoOfSlab = Double.parseDouble(df.format(TotNoOfSlab));
            //            daPerSlab = (double)getNearest((long)(daPerSlab *100),100)/100;
            txtTotalNoofSlabValue.setText(String.valueOf(TotNoOfSlab));
            txtDATotalDAPercentageValue.setText(String.valueOf(daPerSlab));
        }
    }//GEN-LAST:event_txtDAPercentagePerSlabValueFocusLost
    public long roundOffLower(long number,long roundingFactor) {
        long mod = number%roundingFactor;
        return number-mod;
    }
    
    public long getNearest(long number,long roundingFactor)  {
        long roundingFactorOdd = roundingFactor;
        if ((roundingFactor%2) != 0){
            roundingFactorOdd +=1;
        }
        long mod = number%roundingFactor;
        if ((mod < (roundingFactor/2)) || (mod < (roundingFactorOdd/2))){
            return lower(number,roundingFactor);
        }else{
            return higher(number,roundingFactor);
        }
    }
    
    public long lower(long number,long roundingFactor) {
        long mod = number%roundingFactor;
        return number-mod;
    }
    
    public long higher(long number,long roundingFactor) {
        long mod = number%roundingFactor;
        if ( mod == 0){
            return number;
        }
        return (number-mod) + roundingFactor ;
    }
    private void tblHRAllowanceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHRAllowanceMouseClicked
        // TODO add your handling code here:
        selectedSingleRow = true;
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE){
            updationHRA(tblHRAllowance.getSelectedRow());
            enableDisableHRA(false);
        }else if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
            if(tblHRAllowance.getSelectedRow()>=0){
                updationHRA(tblHRAllowance.getSelectedRow());
                enableDisableHRA(false);
                txtHRAllowanceStartingAmtValue.setEnabled(true);
                txtHRAllowanceEndingAmtValue.setEnabled(true);
                if(observable.getHRAAuthorizeStatus()!=null && (observable.getHRAAuthorizeStatus().equals("AUTHORIZED") ||
                observable.getHRAAuthorizeStatus().equals("INACTIVE"))){
                    ClientUtil.enableDisable(panHRAllowanceInfo,false);
                    btnHRAllowanceNew.setEnabled(true);
                    btnHRAllowanceSave.setEnabled(false);
                    btnHRAllowanceDelete.setEnabled(false);
                    HRASave = false;
                }else{
                    btnHRAllowanceNew.setEnabled(false);
                    btnHRAllowanceSave.setEnabled(true);
                    btnHRAllowanceDelete.setEnabled(true);
                    HRASave = true;
                    _intHRANew = false;
                }
            }
        }else{
            ClientUtil.enableDisable(this,false);
        }
    }//GEN-LAST:event_tblHRAllowanceMouseClicked
    
    private void tblCCAllowanceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCCAllowanceMouseClicked
        // TODO add your handling code here:
        selectedSingleRow = true;
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE){
            updationCCA(tblCCAllowance.getSelectedRow());
            enableDisableCCA(false);
        }else if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
            if(tblCCAllowance.getSelectedRow()>=0){
                updationCCA(tblCCAllowance.getSelectedRow());
                enableDisableCCA(false);
                txtCCAllowanceStartingAmtValue.setEnabled(true);
                txtCCAllowanceEndingAmtValue.setEnabled(true);
                if(observable.getCCAAuthorizeStatus()!=null && (observable.getCCAAuthorizeStatus().equals("AUTHORIZED") ||
                observable.getCCAAuthorizeStatus().equals("INACTIVE"))){
                    ClientUtil.enableDisable(panCCAllowanceInfo,false);
                    btnCCAllowanceNew.setEnabled(true);
                    btnCCAllowanceSave.setEnabled(false);
                    btnCCAllowanceDelete.setEnabled(false);
                    CCASave = false;
                }else{
                    btnCCAllowanceNew.setEnabled(false);
                    btnCCAllowanceSave.setEnabled(true);
                    btnCCAllowanceDelete.setEnabled(true);
                    CCASave = true;
                    _intCCANew = false;
                }
            }
        }else{
            ClientUtil.enableDisable(this,false);
        }
    }//GEN-LAST:event_tblCCAllowanceMouseClicked
    private void enableDisableAllscreens(boolean allScreen){
        btnSalaryStructureNew.setEnabled(allScreen);
        btnSalaryStructureSave.setEnabled(allScreen);
        btnSalaryStructureDelete.setEnabled(allScreen);
        btnDANew.setEnabled(allScreen);
        btnDASave.setEnabled(allScreen);
        btnDADelete.setEnabled(allScreen);
        btnCCAllowanceNew.setEnabled(allScreen);
        btnCCAllowanceSave.setEnabled(allScreen);
        btnCCAllowanceDelete.setEnabled(allScreen);
        btnHRAllowanceNew.setEnabled(allScreen);
        btnHRAllowanceSave.setEnabled(allScreen);
        btnHRAllowanceDelete.setEnabled(allScreen);
        btnTANew.setEnabled(allScreen);
        btnTASave.setEnabled(allScreen);
        btnTADelete.setEnabled(allScreen);
        cboMAidDesg.setEnabled(allScreen);
        tdtMAidFromDateValue.setEnabled(allScreen);
        tdtMAidToDateValue.setEnabled(allScreen);
        txtMAidAmtValue.setEnabled(allScreen);
        btnMAidNew.setEnabled(allScreen);
        btnMAidSave.setEnabled(allScreen);
        btnMAidDelete.setEnabled(allScreen);
        btnOANew.setEnabled(allScreen);
        btnOASave.setEnabled(allScreen);
        btnOADelete.setEnabled(allScreen);
        
        lblSalaryStructureSLNO.setVisible(allScreen);
        lblSalaryStructureSLNOValue.setVisible(allScreen);
        lblDASLNO.setVisible(allScreen);
        lblDASLNOValue.setVisible(allScreen);
        lblCCAllowanceSLNO.setVisible(allScreen);
        lblCCAllowanceSLNOValue.setVisible(allScreen);
        lblHRAllowanceSLNO.setVisible(allScreen);
        lblHRAllowanceSLNOValue.setVisible(allScreen);
        lblTravellingAllowanceSLNOValue.setVisible(allScreen);
        lblTravellingAllowanceSLNO.setVisible(allScreen);
        txtTravellingAllowanceEndingAmtValue2.setVisible(allScreen);
        lblTravellingAllowanceEndingAmt2.setVisible(allScreen);
        lblMASLNO.setVisible(allScreen);
        lblMASLNOValue.setVisible(allScreen);
        lblOASLNO.setVisible(allScreen);
        lblOASLNOValue.setVisible(allScreen);
    }
    private void tblDAMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDAMouseClicked
        // TODO add your handling code here:
        selectedSingleRow = true;
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE){
            updationDA(tblDA.getSelectedRow());
            enableDisableDA(false);
        }else if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
            ClientUtil.enableDisable(panIndexOrPercent,true);
            if(tblDA.getSelectedRow()>=0){
                updationDA(tblDA.getSelectedRow());
                enableDisableDA(false);
                txtDAIndexValue.setEnabled(true);
                txtDAPercentagePerSlabValue.setEnabled(true);
                txtDANoOfPointsPerSlabValue.setEnabled(true);
                tdtDAFromDateValue.setEnabled(true);
                if(observable.getDAAuthorizeStatus()!=null && (observable.getDAAuthorizeStatus().equals("AUTHORIZED") ||
                observable.getDAAuthorizeStatus().equals("INACTIVE"))){
                    ClientUtil.enableDisable(panDAInfo,false);
                    btnDANew.setEnabled(true);
                    btnDASave.setEnabled(false);
                    btnDADelete.setEnabled(false);
                    DASave = false;
                }else{
                    ClientUtil.enableDisable(panDAInfo,true);
                    btnDANew.setEnabled(false);
                    btnDASave.setEnabled(true);
                    btnDADelete.setEnabled(true);
                    txtDATotalDAPercentageValue.setEnabled(true);
                    DASave = true;
                    _intDANew = false;
                }
            }
        }
        if(rdoIndexOrPercent_Index.isSelected()){
            visibleDA(true);
            //        rdoIndexOrPercent_Percent.setSelected(false);
            //        txtDATotalDAPercentageValue.setEnabled(false);
        }
        else if(rdoIndexOrPercent_Percent.isSelected()){
            visibleDA(false);
            //        rdoIndexOrPercent_Index.setSelected(false);
            //        txtDATotalDAPercentageValue.setEnabled(true);
        }
    }//GEN-LAST:event_tblDAMouseClicked
    private void updationDA(int selectDARow) {
        observable.populateDA(selectDARow);
        populateDetail();
        //        _intDANew = false;
    }
    private void btnTASaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTASaveActionPerformed
        // TODO add your handling code here:
        if(cboTravellingAllowance.getSelectedIndex() == 0 || cboTravellingAllowance.getSelectedIndex()<=0){
            ClientUtil.showAlertWindow(designationAlert);
        }else if(CommonUtil.convertObjToStr(tdtTAFromDateValue.getDateValue()).length() == 0){
            ClientUtil.showAlertWindow(fromDateAlert);
        }else if(chkFixedConveyance.isSelected() == false && chkPetrolAllowance.isSelected() == false){
            ClientUtil.showAlertWindow("Select either FixedConveyance or PetrolAllowance should not be empty");
        }else if(chkFixedConveyance.isSelected() == true && CommonUtil.convertObjToDouble(txtBasicAmtUptoValue.getText()).doubleValue() == 0){
            ClientUtil.showAlertWindow("Basic amount upto should not be empty");
        }else if(chkFixedConveyance.isSelected() == true && CommonUtil.convertObjToDouble(txtConveyancePerMonthValue.getText()).doubleValue() == 0){
            ClientUtil.showAlertWindow("Conveyance per month amount should not be empty");
        }else if(chkFixedConveyance.isSelected() == true && CommonUtil.convertObjToDouble(txtBasicAmtBeyondValue.getText()).doubleValue() == 0){
            ClientUtil.showAlertWindow("Basic amount beyond should not be empty");
        }else if(chkFixedConveyance.isSelected() == true && CommonUtil.convertObjToDouble(txtConveyanceAmtValue.getText()).doubleValue() == 0){
            ClientUtil.showAlertWindow("Conveyance amount should not be empty");
        }else if(chkPetrolAllowance.isSelected() == true && CommonUtil.convertObjToDouble(txtNooflitresValue.getText()).doubleValue() == 0){
            ClientUtil.showAlertWindow("No of litre should not be empty");
        }else if(chkPetrolAllowance.isSelected() == true && CommonUtil.convertObjToDouble(txtPricePerlitreValue.getText()).doubleValue() == 0){
            ClientUtil.showAlertWindow("Price per litre amount should not be empty");
        }else if(chkPetrolAllowance.isSelected() == true && CommonUtil.convertObjToDouble(txtTotalConveyanceAmtValue.getText()).doubleValue() == 0){
            ClientUtil.showAlertWindow("Total Conveyance amount should not be empty");
        }else{
            btnSave.setEnabled(true);
            enableDisableButtons(false);
            updateOBFields();
            if(!this._intTANew){
                observable.insertTAData(this.tblTravellingAllowance.getSelectedRow());
            }else{
                observable.insertTAData(-1);
            }
            resetTAForm();
            enableDisableTA(false);
            this.updateTable();
            btnTANew.setEnabled(true);
            btnTASave.setEnabled(false);
            btnTADelete.setEnabled(false);
            this._intTANew = false;
            selectedSingleRow = false;
            TASave = false;
        }
    }//GEN-LAST:event_btnTASaveActionPerformed
    private void enableDisableTA(boolean TAVal){
        tdtTAFromDateValue.setEnabled(TAVal);
        //        tdtTAToDateValue.setEnabled(TAVal);
        chkFixedConveyance.setEnabled(TAVal);
        chkPetrolAllowance.setEnabled(TAVal);
        txtBasicAmtUptoValue.setEnabled(TAVal);
        txtConveyancePerMonthValue.setEnabled(TAVal);
        txtBasicAmtBeyondValue.setEnabled(TAVal);
        txtConveyanceAmtValue.setEnabled(TAVal);
        txtNooflitresValue.setEnabled(TAVal);
        txtPricePerlitreValue.setEnabled(TAVal);
        //        txtTotalConveyanceAmtValue.setEnabled(TAVal);
    }
    private void btnCCAllowanceSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCCAllowanceSaveActionPerformed
        // TODO add your handling code here:
        if(cboCCAllowanceCityType.getSelectedIndex() == 0 || cboCCAllowanceCityType.getSelectedIndex()<=0){
            ClientUtil.showAlertWindow("City Type should not be empty");
        }else if(cboCCAllowance.getSelectedIndex() == 0 || cboCCAllowance.getSelectedIndex()<=0){
            ClientUtil.showAlertWindow(gradeAlert);
        }else if(CommonUtil.convertObjToStr(tdtCCAllowanceFromDateValue.getDateValue()).length() == 0){
            ClientUtil.showAlertWindow(fromDateAlert);
        }else if(CommonUtil.convertObjToDouble(txtCCAllowanceStartingAmtValue.getText()).doubleValue() == 0){
            ClientUtil.showAlertWindow("Scale Ending amount should not be empty");
        }else if(CommonUtil.convertObjToDouble(txtCCAllowanceEndingAmtValue.getText()).doubleValue() == 0){
            ClientUtil.showAlertWindow("Increments amount should not be empty");
            //        }else if(this._intCCANew && (cboCCAllowanceCityType.getSelectedItem().toString().toUpperCase().equals(observable.getCboCCAllowanceCityType()))){
            //            ClientUtil.showAlertWindow("This value already exists please choose another value");
            //            return;
        }else{
            long CCACboSize = 1;
            if(this._intCCANew && tblCCAllowance.getRowCount()>0){
                for (int i = 0;i<tblCCAllowance.getRowCount();i++){
                    if(cboCCAllowanceCityType.getSelectedIndex()>0 && cboCCAllowanceCityType.getSelectedItem().toString().toUpperCase().equals(tblCCAllowance.getValueAt(i, 4))){
                        ClientUtil.showAlertWindow("This value already exists please choose another value");
                        return;
                    }else{
                        CCACboSize = CCACboSize + 1;
                    }
                }
            }
            if(this._intCCANew && CCACboSize>((long)cboCCAllowanceCityType.getItemCount()-1)){
                ClientUtil.showAlertWindow("you cannot enter more than this");
                return;
            }else{
                updateOBFields();
                if(!this._intCCANew){
                    observable.insertCCAData(this.tblCCAllowance.getSelectedRow());
                }else{
                    observable.insertCCAData(-1);
                }
                resetCCAForm();
                enableDisableCCA(false);
                btnCCAllowanceNew.setEnabled(true);
                btnCCAllowanceSave.setEnabled(false);
                btnCCAllowanceDelete.setEnabled(false);
                System.out.print("else condition:");
                this.updateTable();
                this._intCCANew = false;
                CCASave = false;
            }
        }
    }//GEN-LAST:event_btnCCAllowanceSaveActionPerformed
    private void updationTA(int selectDARow) {
        observable.populateTA(selectDARow);
        populateDetail();
        _intTANew = false;
    }
    private void btnHRAllowanceSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHRAllowanceSaveActionPerformed
        // TODO add your handling code here:
        if(cboHRAllowanceCityType.getSelectedIndex() == 0 || cboHRAllowanceCityType.getSelectedIndex()<=0){
            ClientUtil.showAlertWindow("City Type should not be empty");
        }else if(cboHRAllowanceDesignation.getSelectedIndex() == 0 || cboHRAllowanceDesignation.getSelectedIndex()<=0){
            ClientUtil.showAlertWindow(gradeAlert);
        }else if(CommonUtil.convertObjToStr(tdtHRAllowanceFromDateValue.getDateValue()).length() == 0){
            ClientUtil.showAlertWindow(fromDateAlert);
        }else if(CommonUtil.convertObjToDouble(txtHRAllowanceStartingAmtValue.getText()).doubleValue() == 0){
            ClientUtil.showAlertWindow("Scale Ending amount should not be empty");
        }else if(CommonUtil.convertObjToDouble(txtHRAllowanceEndingAmtValue.getText()).doubleValue() == 0){
            ClientUtil.showAlertWindow("Increments amount should not be empty");
        }else if(rdoHRAPayable_Yes.isSelected() == false && rdoHRAPayable_No.isSelected() == false){
            ClientUtil.showAlertWindow("Quarters Provided whether HRA Payable Should be select either yes or no");
            //        }else if(this._intHRANew && (cboHRAllowanceCityType.getSelectedItem().toString().toUpperCase().equals(observable.getCboHRAllowanceCityType()))){
            //            ClientUtil.showAlertWindow("This value already exists please choose another value");
            //            return;
        }else{
            long HRACboSize = 1;
            if(this._intHRANew && tblHRAllowance.getRowCount()>0){
                for (int i = 0;i<tblHRAllowance.getRowCount();i++){
                    if(cboHRAllowanceCityType.getSelectedIndex()>0 && cboHRAllowanceCityType.getSelectedItem().toString().toUpperCase().equals(tblHRAllowance.getValueAt(i, 4))){
                        ClientUtil.showAlertWindow("This value already exists please choose another value");
                        return;
                    }else{
                        HRACboSize = HRACboSize + 1;
                    }
                }
            }
            if(this._intHRANew && HRACboSize>((long)cboHRAllowanceCityType.getItemCount()-1)){
                ClientUtil.showAlertWindow("you cannot enter more than this");
                return;
            }else{
                updateOBFields();
                //            int selectedRow = tblHRAllowance.getSelectedRow();
                if(!this._intHRANew){
                    observable.insertHRAAData(this.tblHRAllowance.getSelectedRow());
                }else{
                    observable.insertHRAAData(-1);
                }
                resetHRAForm();
                btnHRAllowanceNew.setEnabled(true);
                btnHRAllowanceSave.setEnabled(false);
                btnHRAllowanceDelete.setEnabled(false);
                System.out.print("else condition:");
                this.updateTable();
                enableDisableHRA(false);
                this._intHRANew = false;
                HRASave = false;
            }
        }
    }//GEN-LAST:event_btnHRAllowanceSaveActionPerformed
    private void enableDisableHRA(boolean HRAVal){
        cboHRAllowanceCityType.setEnabled(HRAVal);
        //        tdtHRAllowanceToDateValue.setEnabled(HRAVal);
        txtHRAllowanceStartingAmtValue.setEnabled(HRAVal);
        txtHRAllowanceEndingAmtValue.setEnabled(HRAVal);
        rdoHRAPayable_Yes.setEnabled(HRAVal);
        rdoHRAPayable_No.setEnabled(HRAVal);
    }
    private void btnTANewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTANewActionPerformed
        // TODO add your handling code here:
        if(tblTravellingAllowance.getRowCount() == 0){
            enableDisableTA(true);
        }else if(tblTravellingAllowance.getRowCount() >0){
            updationTA(tblTravellingAllowance.getRowCount()-1);
            enableDisableTA(false);
            if(chkFixedConveyance.isSelected() == true){
                //                chkFixedConveyanceVisible(true);
                chkFixedConveyanceEnableDisable(true);
                //                chkPetrolAllowanceVisible(false);
                //                chkPetrolAllowanceEnableDisable(false);
            }else if(chkPetrolAllowance.isSelected() == true){
                //                chkPetrolAllowanceVisible(true);
                chkPetrolAllowanceEnableDisable(true);
                //                chkFixedConveyanceVisible(false);
                //                chkFixedConveyanceEnableDisable(false);
            }
            if(observable.getTAAuthorizeStatus()!=null && (observable.getTAAuthorizeStatus().equals("AUTHORIZED") ||
            observable.getTAAuthorizeStatus().equals("INACTIVE"))){
                ClientUtil.enableDisable(panTravellingAllowanceInfo,false);
                btnTANew.setEnabled(true);
                btnTASave.setEnabled(false);
                btnTADelete.setEnabled(false);
                chkFixedConveyance.setEnabled(true);
                chkPetrolAllowance.setEnabled(true);
            }else{
                btnTANew.setEnabled(false);
                btnTASave.setEnabled(true);
                btnTADelete.setEnabled(true);
                TASave = true;
            }
        }
        _intTANew = true;
        btnTADelete.setEnabled(false);
        btnTASave.setEnabled(true);
        btnTANew.setEnabled(false);
    }//GEN-LAST:event_btnTANewActionPerformed
    private void updationHRA(int selectDARow) {
        observable.populateHRA(selectDARow);
        populateDetail();
        _intHRANew = false;
    }
    private void btnDASaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDASaveActionPerformed
        // TODO add your handling code here:
        // Add your handling code here:
        boolean DABoolVal = false;
        if(tblDA.getRowCount() > 0){
            int i = tblDA.getRowCount()-1;
            Date fromDt = DateUtil.getDateMMDDYYYY(tdtDAFromDateValue.getDateValue());
            if(fromDt != null){
                Date dJoin = (Date)curDate.clone();
                dJoin.setDate(fromDt.getDate());
                dJoin.setMonth(fromDt.getMonth());
                dJoin.setYear(fromDt.getYear());
                fromDt = dJoin;
            }
            Date oldFrmDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tblDA.getValueAt(i, 1)));
            if(oldFrmDt != null){
                Date dJoin = (Date)curDate.clone();
                dJoin.setDate(oldFrmDt.getDate());
                dJoin.setMonth(oldFrmDt.getMonth());
                dJoin.setYear(oldFrmDt.getYear());
                oldFrmDt = dJoin;
            }
            if(fromDt != null ){
                if(_intDANew == true && (oldFrmDt.after(fromDt) || oldFrmDt.compareTo(fromDt)== 0)){
                    ClientUtil.showAlertWindow("DA for this period already exist please choose another value");
                    return;
                }else{
                    DABoolVal = true;
                }
            }
        }
        if(rdoIndexOrPercent_Index.isSelected()){
            if(cboDADesignationValue.getSelectedIndex() == 0 || cboDADesignationValue.getSelectedIndex()<=0){
                ClientUtil.showAlertWindow(gradeAlert);
                DABoolVal = false;
            }else if(CommonUtil.convertObjToStr(tdtDAFromDateValue.getDateValue()).length() == 0){
                ClientUtil.showAlertWindow(fromDateAlert);
                DABoolVal = false;
            }else if(CommonUtil.convertObjToDouble(txtDANoOfPointsPerSlabValue.getText()).doubleValue() == 0){
                ClientUtil.showAlertWindow("Scale DA points should not be empty");
                DABoolVal = false;
            }else if(CommonUtil.convertObjToDouble(txtDAIndexValue.getText()).doubleValue() == 0){
                ClientUtil.showAlertWindow("Index value should not be empty");
                DABoolVal = false;
            }else if(CommonUtil.convertObjToDouble(txtDAPercentagePerSlabValue.getText()).doubleValue() == 0){
                ClientUtil.showAlertWindow("Percentage should not be empty");
                DABoolVal = false;
            }
        }
        else if(rdoIndexOrPercent_Percent.isSelected()){
            System.out.println("@#$@#$@#$in here with DA :P ");
            DABoolVal = true;
        }
        if(DABoolVal == true){
            btnSave.setEnabled(true);
            System.out.print("else condition:");
            updateOBFields();
            if(!this._intDANew){
                observable.insertDAData(this.tblDA.getSelectedRow());
            }else{
                observable.insertDAData(-1);
            }
            resetDAForm();
            enableDisableDA(false);
            btnDANew.setEnabled(true);
            btnDASave.setEnabled(false);
            btnDADelete.setEnabled(false);
            this.updateTable();
            this._intDANew = false;
            btnDASave.setEnabled(false);
            btnDADelete.setEnabled(false);
            btnDANew.setEnabled(true);
            DASave = false;
        }else{
            
        }
        DABoolVal = true;
        
        
        
    }//GEN-LAST:event_btnDASaveActionPerformed
    private void enableDisableDA(boolean DAVal){
        //        cboDADesignationValue.setEnabled(DAVal);
        tdtDAFromDateValue.setEnabled(DAVal);
        //        tdtDAToDateValue.setEnabled(DAVal);
        txtDANoOfPointsPerSlabValue.setEnabled(DAVal);
        txtDAIndexValue.setEnabled(DAVal);
        txtDAPercentagePerSlabValue.setEnabled(DAVal);
        //        txtTotalNoofSlabValue.setEnabled(DAVal);
        //        txtDATotalDAPercentageValue.setEnabled(DAVal);
    }
    private void btnHRAllowanceNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHRAllowanceNewActionPerformed
        // TODO add your handling code here:
        _intHRANew = true;
        if(tblHRAllowance.getRowCount() == 0){
            enableDisableHRA(true);
        }else if(tblHRAllowance.getRowCount() >0){
            updationHRA(tblHRAllowance.getRowCount()-1);
            enableDisableHRA(false);
            //            tdtHRAllowanceToDateValue.setEnabled(true);
            txtHRAllowanceStartingAmtValue.setText("");
            txtHRAllowanceEndingAmtValue.setText("");
            txtHRAllowanceStartingAmtValue.setEnabled(true);
            txtHRAllowanceEndingAmtValue.setEnabled(true);
            //            cboHRAllowanceCityType.setSelectedItem("");
            cboHRAllowanceCityType.setEnabled(true);
        }
        btnHRAllowanceDelete.setEnabled(false);
        btnHRAllowanceSave.setEnabled(true);
        btnHRAllowanceNew.setEnabled(false);
    }//GEN-LAST:event_btnHRAllowanceNewActionPerformed
    private void updationCCA(int selectDARow) {
        observable.populateCCA(selectDARow);
        populateDetail();
        _intDANew = false;
    }
    private void btnCCAllowanceNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCCAllowanceNewActionPerformed
        // TODO add your handling code here:
        _intCCANew = true;
        if(tblCCAllowance.getRowCount() == 0){
            enableDisableCCA(true);
            txtFromAmount.setText(String.valueOf(1.0));
            txtFromAmount.setEnabled(false);
            txtToAmount.setEnabled(true);
        }else if(tblCCAllowance.getRowCount() >0){
            updationCCA(tblCCAllowance.getRowCount()-1);
            enableDisableCCA(false);
            double fromAmt = 0.0;
            for(int i=0;i< tblCCAllowance.getRowCount() ; i++){
                fromAmt = CommonUtil.convertObjToDouble(tblCCAllowance.getValueAt(i, 2)).doubleValue();
            }
            System.out.println("@#$@#$#@$ from AMount:"+fromAmt);
            fromAmt += 1;
            txtFromAmount.setText(String.valueOf(fromAmt));
            txtFromAmount.setEnabled(false);
            txtToAmount.setEnabled(true);
            txtToAmount.setText("");
            //            tdtCCAllowanceToDateValue.setEnabled(true);
            txtCCAllowanceStartingAmtValue.setText("");
            txtCCAllowanceEndingAmtValue.setText("");
            txtCCAllowanceStartingAmtValue.setEnabled(true);
            txtCCAllowanceEndingAmtValue.setEnabled(true);
            cboCCAllowanceCityType.setEnabled(true);
            ClientUtil.enableDisable(panPercentOrFixed,true);
            txtToAmount.setEnabled(true);
            
        }
        btnCCAllowanceDelete.setEnabled(false);
        btnCCAllowanceSave.setEnabled(true);
        btnCCAllowanceNew.setEnabled(false);
        
    }//GEN-LAST:event_btnCCAllowanceNewActionPerformed
    private void enableDisableCCA(boolean CCAVal){
        cboCCAllowanceCityType.setEnabled(CCAVal);
        //        cboCCAllowance.setEnabled(CCAVal);
        //        tdtCCAllowanceFromDateValue.setEnabled(CCAVal);
        //        tdtCCAllowanceToDateValue.setEnabled(CCAVal);
        txtCCAllowanceStartingAmtValue.setEnabled(CCAVal);
        txtCCAllowanceEndingAmtValue.setEnabled(CCAVal);
        ClientUtil.enableDisable(panPercentOrFixed,CCAVal);
        txtFromAmount.setEnabled(CCAVal);
        txtToAmount.setEnabled(CCAVal);
    }
    private void btnDANewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDANewActionPerformed
        // TODO add your handling code here:
        _intDANew = true;
        if(tblDA.getRowCount() == 0){
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT &&
            cboDADesignationValue.getSelectedIndex() == 0 || cboDADesignationValue.getSelectedIndex()<=0){
                cboDADesignationValue.setSelectedItem(cboSalaryStructureProdId.getSelectedItem());
            }
            enableDisableDA(true);
        }else if(tblDA.getRowCount() >0){
            updationDA(tblDA.getRowCount()-1);
            enableDisableDA(false);
            txtDANoOfPointsPerSlabValue.setText("");
            txtDAIndexValue.setText("");
            txtDAPercentagePerSlabValue.setText("");
            txtTotalNoofSlabValue.setText("");
            txtDATotalDAPercentageValue.setText("");
            //            tdtDAToDateValue.setEnabled(true);
            txtDANoOfPointsPerSlabValue.setEnabled(true);
            txtDAIndexValue.setEnabled(true);
            txtDAPercentagePerSlabValue.setEnabled(true);
        }
        tdtDAFromDateValue.setEnabled(true);
        ClientUtil.enableDisable(panIndexOrPercent,true);
        btnDANew.setEnabled(false);
        btnDASave.setEnabled(true);
        btnDADelete.setEnabled(false);
    }//GEN-LAST:event_btnDANewActionPerformed
    
    private void rdoStagnationIncrement_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoStagnationIncrement_NoActionPerformed
        // TODO add your handling code here:
        observable.setRdoStagnationIncrement_No(true);
        enableDisableButtons(false);
        
    }//GEN-LAST:event_rdoStagnationIncrement_NoActionPerformed
    
    private void rdoStagnationIncrement_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoStagnationIncrement_YesActionPerformed
        // TODO add your handling code here:
        if(tblSalaryStructure.getRowCount()>0){
            //            double endingAmt = CommonUtil.convertObjToDouble(txtSalaryStructureEndingAmtValue.getText()).doubleValue();
            //            double tableValue = CommonUtil.convertObjToDouble(tblSalaryStructure.getValueAt((tblSalaryStructure.getRowCount()-1),3)).doubleValue();
            btnSalaryStructureNew.setEnabled(true);
            txtSalaryStructureAmtValue.setEnabled(false);
            txtSalaryStructureIncYearValue.setEnabled(false);
            stagnationEnableDisable(true);
        }
        
    }//GEN-LAST:event_rdoStagnationIncrement_YesActionPerformed
    
    private void txtSalaryStructureStartingAmtValueFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSalaryStructureStartingAmtValueFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSalaryStructureStartingAmtValueFocusLost
    
    private void btnSalaryStructureNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalaryStructureNewActionPerformed
        // Add your handling code here:
        this._intSalaryNew = true;
        btnSalaryStructureNew.setEnabled(false);
        btnSalaryStructureSave.setEnabled(true);
        enableDisableButtons(true);
        if(tblSalaryStructure.getRowCount() == 0){
            txtSalaryStructureStartingAmtValue.setEnabled(true);
            txtSalaryStructureEndingAmtValue.setEnabled(true);
        }else if(tblSalaryStructure.getRowCount()>0){
            updationNewButton(tblSalaryStructure.getRowCount()-1);
            String Type = CommonUtil.convertObjToStr(tblSalaryStructure.getValueAt((tblSalaryStructure.getRowCount()-1),4));
            if(Type!= null && Type.equals("NI")){
                cboSalaryStructureProdId.setEnabled(false);
                lblSalaryStructureFromDateValue.setEnabled(false);
                txtSalaryStructureStartingAmtValue.setEnabled(false);
                txtSalaryStructureEndingAmtValue.setEnabled(false);
                txtSalaryStructureAmtValue.setText("");
                txtSalaryStructureIncYearValue.setText("");
            }else{
                rdoStagnationIncrement_Yes.setEnabled(false);
                rdoStagnationIncrement_No.setEnabled(false);
                enableDisableButtons(false);
                txtSalaryStructureAmtValue.setText("");
                txtSalaryStructureIncYearValue.setText("");
                txtSalaryStructureStagnationAmtValue.setText("");
                txtSalaryStructureNoOfStagnationValue.setText("");
                txtSalaryStructureStagnationOnceInValue.setText("");
                cboSalaryStructureStagnationOnceIn.setSelectedItem("");
                if(txtSalaryStructureTotNoIncValue.getText().length()>0){
                    txtSalaryStructureTotNoIncValue.setEnabled(false);
                }else{
                    txtSalaryStructureTotNoIncValue.setEnabled(true);
                }
                txtSalaryStructureStagnationAmtValue.setEnabled(true);
                txtSalaryStructureNoOfStagnationValue.setEnabled(true);
                txtSalaryStructureStagnationOnceInValue.setEnabled(true);
                cboSalaryStructureStagnationOnceIn.setEnabled(true);
            }
        }
        if(tblSalaryStructure.getRowCount()>0){
            String Type = CommonUtil.convertObjToStr(tblSalaryStructure.getValueAt((tblSalaryStructure.getRowCount()-1),4));
            if(Type!= null && Type.equals("NI")){
                double endingAmt = CommonUtil.convertObjToDouble(txtSalaryStructureEndingAmtValue.getText()).doubleValue();
                double tableValue = CommonUtil.convertObjToDouble(tblSalaryStructure.getValueAt((tblSalaryStructure.getRowCount()-1),3)).doubleValue();
                if(endingAmt == tableValue){
                    rdoStagnationIncrement_Yes.setSelected(true);
                    txtSalaryStructureAmtValue.setEnabled(false);
                    txtSalaryStructureIncYearValue.setEnabled(false);
                    stagnationEnableDisable(true);
                    rdoStagnationIncrement_Yes.setEnabled(false);
                    rdoStagnationIncrement_No.setEnabled(false);
                }else{
                    txtSalaryStructureAmtValue.setEnabled(true);
                    txtSalaryStructureIncYearValue.setEnabled(true);
                    stagnationEnableDisable(false);
                }
            }
        }
    }//GEN-LAST:event_btnSalaryStructureNewActionPerformed
    private void enableDisableButtons(boolean value){
        cboSalaryStructureProdId.setEnabled(value);
        lblSalaryStructureFromDateValue.setEnabled(value);
        lblSalaryStructureToDateValue.setEnabled(value);
        txtSalaryStructureAmtValue.setEnabled(value);
        txtSalaryStructureIncYearValue.setEnabled(value);
        txtSalaryStructureStartingAmtValue.setEnabled(value);
        txtSalaryStructureEndingAmtValue.setEnabled(value);
        txtSalaryStructureTotNoIncValue.setEnabled(value);
        txtSalaryStructureStagnationAmtValue.setEnabled(value);
        txtSalaryStructureNoOfStagnationValue.setEnabled(value);
        txtSalaryStructureStagnationOnceInValue.setEnabled(value);
        cboSalaryStructureStagnationOnceIn.setEnabled(value);
    }
    private void btnSalaryStructureSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalaryStructureSaveActionPerformed
        // Add your handling code here:
        //        boolean enable = false;
        String mandatoryMessage;
        mandatoryMessage = checkMandatory(this.panSalaryStructureInfo);
        double endingAmt = 0.0;
        double tableValue = 0.0;
        double totalTableAmt = 0.0;
        double noOfInc = 0.0;
        int selectedRow = tblSalaryStructure.getSelectedRow();
        if(tblSalaryStructure.getRowCount()>0){
            endingAmt = CommonUtil.convertObjToDouble(txtSalaryStructureEndingAmtValue.getText()).doubleValue();
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
                for (int i=0;i<tblSalaryStructure.getRowCount();i++){
                    if(selectedSingleRow == true){
                        if(i != selectedRow){
                            double tableIncAmt = CommonUtil.convertObjToDouble(tblSalaryStructure.getValueAt(i,1)).doubleValue();
                            double tableNoofInc = CommonUtil.convertObjToDouble(tblSalaryStructure.getValueAt(i,2)).doubleValue();
                            String type = CommonUtil.convertObjToStr(tblSalaryStructure.getValueAt(i,4));
                            String authStatus = CommonUtil.convertObjToStr(tblSalaryStructure.getValueAt(i,6));
                            if(type!= null && type.equals("NI") && authStatus.equals("")){
                                totalTableAmt += tableIncAmt * tableNoofInc;
                            }else if(type!= null && type.equals("SI") && authStatus.equals("")){
                                noOfInc = CommonUtil.convertObjToDouble(tblSalaryStructure.getValueAt(i, 2)).doubleValue();
                            }
                        }
                    }else{
                        String type = CommonUtil.convertObjToStr(tblSalaryStructure.getValueAt(i,4));
                        String authStatus = CommonUtil.convertObjToStr(tblSalaryStructure.getValueAt(i,6));
                        if(type!= null && type.equals("NI") && authStatus.equals("")){
                            double starting = 0.0;
                            if(i == 0){
                                starting = CommonUtil.convertObjToDouble(tblSalaryStructure.getValueAt(i,0)).doubleValue();
                            }
                            double tableIncAmt = CommonUtil.convertObjToDouble(tblSalaryStructure.getValueAt(i,1)).doubleValue();
                            double tableNoofInc = CommonUtil.convertObjToDouble(tblSalaryStructure.getValueAt(i,2)).doubleValue();
                            tableValue += tableIncAmt * tableNoofInc + starting;
                            if(i == 0){
                                tableValue += CommonUtil.convertObjToDouble(txtSalaryStructureAmtValue.getText()).doubleValue() *
                                CommonUtil.convertObjToDouble(txtSalaryStructureIncYearValue.getText()).doubleValue();
                            }
                        }else if(type!= null && type.equals("SI") && authStatus.equals("")){
                            tableValue += CommonUtil.convertObjToDouble(txtSalaryStructureStagnationAmtValue.getText()).doubleValue() *
                            CommonUtil.convertObjToDouble(txtSalaryStructureNoOfStagnationValue.getText()).doubleValue();
                            noOfInc += CommonUtil.convertObjToDouble(tblSalaryStructure.getValueAt(i, 2)).doubleValue();
                        }
                    }
                }
                if(tableValue == CommonUtil.convertObjToDouble(txtSalaryStructureEndingAmtValue.getText()).doubleValue() &&
                CommonUtil.convertObjToDouble(txtSalaryStructureStagnationAmtValue.getText()).doubleValue()>0){
                    tableValue += CommonUtil.convertObjToDouble(txtSalaryStructureStagnationAmtValue.getText()).doubleValue() *
                    CommonUtil.convertObjToDouble(txtSalaryStructureNoOfStagnationValue.getText()).doubleValue();
                }
                double lastRowAmt = 0.0;
                double selectedRowAmt = 0.0;
                double selectedRowStatAmt = 0.0;
                if(selectedSingleRow == true){
                    selectedRowStatAmt = CommonUtil.convertObjToDouble(tblSalaryStructure.getValueAt((tblSalaryStructure.getSelectedRow()),0)).doubleValue();
                    String type = CommonUtil.convertObjToStr(tblSalaryStructure.getValueAt(selectedRow,4));
                    String authStatus = CommonUtil.convertObjToStr(tblSalaryStructure.getValueAt(selectedRow,6));
                    if(type!= null && type.equals("NI") && authStatus.equals("")){
                        double incAmt = CommonUtil.convertObjToDouble(txtSalaryStructureAmtValue.getText()).doubleValue();
                        double incCount = CommonUtil.convertObjToDouble(txtSalaryStructureIncYearValue.getText()).doubleValue();
                        selectedRowAmt = incAmt * incCount;
                    }
                    tableValue = selectedRowStatAmt + totalTableAmt + selectedRowAmt;
                }
                //            }else if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
                //                tableValue = CommonUtil.convertObjToDouble(tblLien.getValueAt((tblLien.getRowCount()-1),3)).doubleValue();
            }
        }else{
            double starting = CommonUtil.convertObjToDouble(txtSalaryStructureStartingAmtValue.getText()).doubleValue();
            double incAmt = CommonUtil.convertObjToDouble(txtSalaryStructureAmtValue.getText()).doubleValue();
            double incCount = CommonUtil.convertObjToDouble(txtSalaryStructureIncYearValue.getText()).doubleValue();
            tableValue = incAmt * incCount + starting;
        }
        //        if(mandatoryMessage.length() > 0 ){
        //            displayAlert(mandatoryMessage);
        //            return;
        //        }else
        if(rdoStagnationIncrement_Yes.isSelected() == false && rdoStagnationIncrement_No.isSelected() == true &&
        (cboSalaryStructureProdId.getSelectedIndex() == 0 || cboSalaryStructureProdId.getSelectedIndex() <=0)){
            ClientUtil.showAlertWindow(gradeAlert);
        }else if(rdoStagnationIncrement_Yes.isSelected() == false && rdoStagnationIncrement_No.isSelected() == true &&
        lblSalaryStructureFromDateValue.getDateValue().length() == 0){
            ClientUtil.showAlertWindow("From Date should not be empty");
        }else if(rdoStagnationIncrement_Yes.isSelected() == false && rdoStagnationIncrement_No.isSelected() == true &&
        CommonUtil.convertObjToDouble(txtSalaryStructureStartingAmtValue.getText()).doubleValue() == 0){
            ClientUtil.showAlertWindow("Scale Starting amount should not be empty");
        }else if(rdoStagnationIncrement_Yes.isSelected() == false && rdoStagnationIncrement_No.isSelected() == true &&
        CommonUtil.convertObjToDouble(txtSalaryStructureEndingAmtValue.getText()).doubleValue() == 0){
            ClientUtil.showAlertWindow("Scale Ending amount should not be empty");
        }else if(rdoStagnationIncrement_Yes.isSelected() == false && rdoStagnationIncrement_No.isSelected() == true &&
        CommonUtil.convertObjToDouble(txtSalaryStructureAmtValue.getText()).doubleValue() == 0){
            ClientUtil.showAlertWindow("Increments amount should not be empty");
        }else if(rdoStagnationIncrement_Yes.isSelected() == false && rdoStagnationIncrement_No.isSelected() == true &&
        CommonUtil.convertObjToDouble(txtSalaryStructureIncYearValue.getText()).doubleValue() == 0){
            ClientUtil.showAlertWindow("No. of Increments should not be empty");
            //        }else if(tblLien.getRowCount()>0 && endingAmt == tableValue && rdoStagnationIncrement_Yes.isSelected() == false && rdoStagnationIncrement_No.isSelected() == false){
            //            ClientUtil.showAlertWindow("Select Sagnation Increment Yes or No");
        }else if(rdoStagnationIncrement_Yes.isSelected() == true && CommonUtil.convertObjToDouble(txtSalaryStructureTotNoIncValue.getText()).doubleValue()==0){
            ClientUtil.showAlertWindow("No. of Sagnation Increments should not be empty");
        }else  if(rdoStagnationIncrement_Yes.isSelected() == true && CommonUtil.convertObjToDouble(txtSalaryStructureStagnationAmtValue.getText()).doubleValue()==0){
            ClientUtil.showAlertWindow("Sagnation Increments Amount should not be empty");
        }else  if(rdoStagnationIncrement_Yes.isSelected() == true && CommonUtil.convertObjToDouble(txtSalaryStructureNoOfStagnationValue.getText()).doubleValue()==0){
            ClientUtil.showAlertWindow("No. of Sagnation Increments should not be empty");
        }else  if(rdoStagnationIncrement_Yes.isSelected() == true && CommonUtil.convertObjToDouble(txtSalaryStructureStagnationOnceInValue.getText()).doubleValue()==0){
            ClientUtil.showAlertWindow("No. of Increments should not be empty");
        }else if(rdoStagnationIncrement_Yes.isSelected() == true && (cboSalaryStructureStagnationOnceIn.getSelectedIndex() == 0 ||
        cboSalaryStructureStagnationOnceIn.getSelectedIndex() <=0)){
            ClientUtil.showAlertWindow("should not be empty");
        }else if(rdoStagnationIncrement_Yes.isSelected() == true && (noOfInc + CommonUtil.convertObjToDouble(txtSalaryStructureNoOfStagnationValue).doubleValue())<
        CommonUtil.convertObjToDouble(txtSalaryStructureTotNoIncValue).doubleValue()){
            ClientUtil.showAlertWindow("should not be empty");
        }else{
            double totAmt = 0.0;
            double calcAmt = 0.0;
            double finalAmt = 0.0;
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
                if(this._intSalaryNew == true){
                    if(tblSalaryStructure.getRowCount() == 0){
                        finalAmt += CommonUtil.convertObjToDouble(txtSalaryStructureStartingAmtValue.getText()).doubleValue();
                        finalAmt += CommonUtil.convertObjToDouble(txtSalaryStructureAmtValue.getText()).doubleValue() *
                        CommonUtil.convertObjToDouble(txtSalaryStructureIncYearValue.getText()).doubleValue();
                        totAmt = finalAmt;
                    }else{
                        finalAmt += CommonUtil.convertObjToDouble(tblSalaryStructure.getValueAt((tblSalaryStructure.getRowCount()-1),3)).doubleValue();
                        if(rdoStagnationIncrement_No.isSelected() == true && endingAmt != tableValue){
                            calcAmt = CommonUtil.convertObjToDouble(txtSalaryStructureAmtValue.getText()).doubleValue() *
                            CommonUtil.convertObjToDouble(txtSalaryStructureIncYearValue.getText()).doubleValue();
                        }else{
                            calcAmt = CommonUtil.convertObjToDouble(txtSalaryStructureStagnationAmtValue.getText()).doubleValue() *
                            CommonUtil.convertObjToDouble(txtSalaryStructureNoOfStagnationValue.getText()).doubleValue();
                        }
                        finalAmt += calcAmt;
                        totAmt = finalAmt;
                    }
                }else if(this._intSalaryNew == false){
                    //                    int selectedRow = tblLien.getSelectedRow();
                    double lastRowAmt = 0.0;
                    double incAmt = CommonUtil.convertObjToDouble(tblSalaryStructure.getValueAt((tblSalaryStructure.getSelectedRow()),1)).doubleValue();
                    double incCount = CommonUtil.convertObjToDouble(tblSalaryStructure.getValueAt((tblSalaryStructure.getSelectedRow()),2)).doubleValue();
                    lastRowAmt = incAmt * incCount;
                    totAmt = CommonUtil.convertObjToDouble(tblSalaryStructure.getValueAt((tblSalaryStructure.getRowCount()-1),3)).doubleValue();
                    totAmt = totAmt - lastRowAmt;
                    calcAmt = CommonUtil.convertObjToDouble(txtSalaryStructureAmtValue.getText()).doubleValue() *
                    CommonUtil.convertObjToDouble(txtSalaryStructureIncYearValue.getText()).doubleValue();
                    totAmt = totAmt + calcAmt;
                    finalAmt = totAmt;
                }
                if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ||
                observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
                    totAmt = tableValue;
                    finalAmt = tableValue;
                }
            }
            if(rdoStagnationIncrement_No.isSelected() == true && totAmt>CommonUtil.convertObjToDouble(txtSalaryStructureEndingAmtValue.getText()).doubleValue()){
                System.out.print("if condition:");
                ClientUtil.showAlertWindow("Exceeding Scale Maximum Amount");
                return;
            }else{
                HashMap existingDateMap = new HashMap();
                Date existingDate = DateUtil.getDateMMDDYYYY(lblSalaryStructureFromDateValue.getDateValue());
                Date currDt = (Date) curDate.clone();
                if(existingDate!=null && existingDate.getDate()>0){
                    currDt.setDate(existingDate.getDate());
                    currDt.setMonth(existingDate.getMonth());
                    currDt.setYear(existingDate.getYear());
                }
                if(tblSalaryStructure.getRowCount() == 0){
                    existingDateMap.put("GRADE",CommonUtil.convertObjToStr(cboSalaryStructureProdId.getSelectedItem()).toUpperCase());
                    existingDateMap.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
                    existingDateMap.put("FROM_DATE", currDt);
                    List lst = ClientUtil.executeQuery("getSelectExistingRecords", existingDateMap);
                    if(lst!=null && lst.size()>0){
                        existingDateMap = (HashMap)lst.get(0);
                        existingDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(existingDateMap.get("FROM_DATE")));
                        Date newRecDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(lblSalaryStructureFromDateValue.getDateValue()));
                        if(DateUtil.dateDiff(existingDate,newRecDate) == 0){
                            String[] obj4 = {"Yes","No"};
                            int option3 = COptionPane.showOptionDialog(null,("This date already exist do u want to continue?"), ("Salary Structure"),
                            COptionPane.YES_NO_CANCEL_OPTION,COptionPane.QUESTION_MESSAGE,null,obj4,obj4[0]);
                            if(option3 == 1){
                                return;
                            }
                        }
                    }
                }
                observable.setTxtSalaryStructureSingleRowAmt(String.valueOf(finalAmt));
                btnSave.setEnabled(true);
                enableDisableButtons(false);
                btnSalaryStructureNew.setEnabled(true);
                btnSalaryStructureSave.setEnabled(false);
                btnSalaryStructureDelete.setEnabled(false);
                System.out.print("else condition:");
                updateOBFields();
                if(!this._intSalaryNew){
                    observable.insertLienData(this.tblSalaryStructure.getSelectedRow());
                }else{
                    observable.insertLienData(-1);
                    
                }
                if(tblSalaryStructure.getRowCount()>0){
                    endingAmt = CommonUtil.convertObjToDouble(txtSalaryStructureEndingAmtValue.getText()).doubleValue();
                    tableValue = CommonUtil.convertObjToDouble(tblSalaryStructure.getValueAt((tblSalaryStructure.getRowCount()-1),3)).doubleValue();
                    String type = CommonUtil.convertObjToStr(tblSalaryStructure.getValueAt((tblSalaryStructure.getRowCount()-1),4));
                    String authStatus = CommonUtil.convertObjToStr(tblSalaryStructure.getValueAt((tblSalaryStructure.getRowCount()-1),6));
                    if(type!= null && type.equals("NI") && endingAmt == tableValue && authStatus.equals("")){
                        txtSalaryStructureAmtValue.setEnabled(false);
                        txtSalaryStructureIncYearValue.setEnabled(false);
                        rdoStagnationIncrement_Yes.setEnabled(true);
                        rdoStagnationIncrement_No.setEnabled(true);
                        btnSalaryStructureNew.setEnabled(false);
                        btnDANew.setEnabled(true);
                        btnCCAllowanceNew.setEnabled(true);
                        btnHRAllowanceNew.setEnabled(true);
                        btnTANew.setEnabled(true);
                        btnMAidNew.setEnabled(true);
                        btnOANew.setEnabled(true);
                    }else{
                        rdoStagnationIncrement_Yes.setEnabled(false);
                        rdoStagnationIncrement_No.setEnabled(false);
                        btnSalaryStructureNew.setEnabled(true);
                    }
                }
                resetSalaryStructureForm();
                this.updateTable();
                this._intSalaryNew = false;
                selectedSingleRow = false;
                salaryStructureSave = false;
            }
        }
    }//GEN-LAST:event_btnSalaryStructureSaveActionPerformed
    private void resetSalaryStructureForm(){
        cboSalaryStructureProdId.setSelectedItem("");
        lblSalaryStructureFromDateValue.setDateValue("");
        lblSalaryStructureToDateValue.setDateValue("");
        txtSalaryStructureStartingAmtValue.setText("");
        txtSalaryStructureAmtValue.setText("");
        txtSalaryStructureIncYearValue.setText("");
        txtSalaryStructureEndingAmtValue.setText("");
        txtSalaryStructureTotNoIncValue.setText("");
        txtSalaryStructureStagnationAmtValue.setText("");
        txtSalaryStructureNoOfStagnationValue.setText("");
        txtSalaryStructureStagnationOnceInValue.setText("");
        cboSalaryStructureStagnationOnceIn.setSelectedItem("");
    }
    private void btnSalaryStructureDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalaryStructureDeleteActionPerformed
        // Add your handling code here:
        int rowCount = tblSalaryStructure.getRowCount();
        int rowSelected = tblSalaryStructure.getSelectedRow();
        if(observable.getSSAuthorizeStatus()!=null && (observable.getSSAuthorizeStatus().equals("AUTHORIZED") ||
        observable.getSSAuthorizeStatus().equals("INACTIVE"))){
            ClientUtil.showAlertWindow("Can not delete this record Already Authorized");
            return;
        }else {
            if((rowCount-1) == rowSelected){
                observable.deleteLienData(this.tblSalaryStructure.getSelectedRow());
                this.updateTable();
                resetSalaryStructureForm();
                txtSalaryStructureAmtValue.setEnabled(false);
                txtSalaryStructureIncYearValue.setEnabled(false);
                if(tblSalaryStructure.getRowCount() == 0){
                    btnSalaryStructureNew.setEnabled(true);
                    btnSalaryStructureSave.setEnabled(false);
                    btnSalaryStructureDelete.setEnabled(false);
                }
            }else{
                ClientUtil.showAlertWindow("Can not delete this record.Delete from the last record");
                return;
            }
        }
        
    }//GEN-LAST:event_btnSalaryStructureDeleteActionPerformed
    private void resetDAForm(){
        //        cboDADesignationValue.setSelectedItem("");
        //        tdtDAFromDateValue.setDateValue("");
        tdtDAToDateValue.setDateValue("");
        txtDANoOfPointsPerSlabValue.setText("");
        txtDAIndexValue.setText("");
        txtDAPercentagePerSlabValue.setText("");
        txtTotalNoofSlabValue.setText("");
        txtDATotalDAPercentageValue.setText("");
    }
    private void cboSalaryStructureProdIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboSalaryStructureProdIdActionPerformed
        // Add your handling code here:
        if(cboSalaryStructureProdId.getSelectedIndex()>0){
            //            observable.setCbmProdId(CommonUtil.convertObjToStr(cboSalaryStructureProdId.getSelectedItem()));
            //            observable.setCbmProdId(CommonUtil.convertObjToStr(observable.getCbmSalaryStructureProdId()));
            //            cboOAllowanceTypeValue.setModel(observable.getCbmOAllowanceTypeValue());
            cboOAllowanceTypeValue.setModel(observable.getCbmOAllowanceTypeValue());
            cboDADesignationValue.setSelectedItem(cboSalaryStructureProdId.getSelectedItem());
            cboCCAllowance.setSelectedItem(cboSalaryStructureProdId.getSelectedItem());
            cboHRAllowanceDesignation.setSelectedItem(cboSalaryStructureProdId.getSelectedItem());
            cboTravellingAllowance.setSelectedItem(cboSalaryStructureProdId.getSelectedItem());
            cboMAidDesg.setSelectedItem(cboSalaryStructureProdId.getSelectedItem());
            cboOADesignationValue.setSelectedItem(cboSalaryStructureProdId.getSelectedItem());
        }
    }//GEN-LAST:event_cboSalaryStructureProdIdActionPerformed
    private void stagnationEnableDisable(boolean val){
        lblSalaryStructureTotNoInc.setVisible(val);
        txtSalaryStructureTotNoIncValue.setVisible(val);
        lblSalaryStructureStagnationAmt.setVisible(val);
        txtSalaryStructureStagnationAmtValue.setVisible(val);
        lblSalaryStructureNoOfStagnation.setVisible(val);
        txtSalaryStructureNoOfStagnationValue.setVisible(val);
        lblSalaryStructureStagnationOnceIn.setVisible(val);
        txtSalaryStructureStagnationOnceInValue.setVisible(val);
        cboSalaryStructureStagnationOnceIn.setVisible(val);
    }
    private void tblSalaryStructureMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSalaryStructureMouseClicked
        // Add your handling code here:
        selectedSingleRow = true;
        rdoStagnationIncrement_Yes.setEnabled(false);
        rdoStagnationIncrement_No.setEnabled(false);
        rowSelected = tblSalaryStructure.getSelectedRow();
        String Type = CommonUtil.convertObjToStr(tblSalaryStructure.getValueAt(rowSelected, 4));
        
        if(Type!=null && Type.equals("NI")){
            stagnationEnableDisable(false);
        }else if(Type!=null && Type.equals("SI")){
            stagnationEnableDisable(true);
        }
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE){
            enableDisableButtons(false);
            updationLien();
            if(observable.getRdoStagnationIncrement_Yes() == true){
                rdoStagnationIncrement_Yes.setSelected(true);
            }else if(observable.getRdoStagnationIncrement_No() == true){
                rdoStagnationIncrement_No.setSelected(true);
            }
        }else if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
            btnSalaryStructureNew.setEnabled(false);
            btnSalaryStructureSave.setEnabled(true);
            btnSalaryStructureDelete.setEnabled(true);
            cboSalaryStructureProdId.setEnabled(false);
            txtSalaryStructureEndingAmtValue.setEnabled(false);
            txtSalaryStructureStartingAmtValue.setEnabled(false);
            lblSalaryStructureFromDateValue.setEnabled(false);
            enableDisableButtons(false);
            updationLien();
            if(Type!=null && Type.equals("NI")){
                txtSalaryStructureAmtValue.setEnabled(true);
                txtSalaryStructureIncYearValue.setEnabled(true);
                txtSalaryStructureTotNoIncValue.setEnabled(false);
                txtSalaryStructureStagnationAmtValue.setEnabled(false);
                txtSalaryStructureNoOfStagnationValue.setEnabled(false);
                txtSalaryStructureStagnationOnceInValue.setEnabled(false);
                cboSalaryStructureStagnationOnceIn.setEnabled(false);
            }else if(Type!=null && Type.equals("SI")){
                txtSalaryStructureAmtValue.setEnabled(false);
                txtSalaryStructureIncYearValue.setEnabled(false);
                txtSalaryStructureTotNoIncValue.setEnabled(false);
                txtSalaryStructureStagnationAmtValue.setEnabled(true);
                txtSalaryStructureNoOfStagnationValue.setEnabled(true);
                txtSalaryStructureStagnationOnceInValue.setEnabled(true);
                cboSalaryStructureStagnationOnceIn.setEnabled(true);
                
            }
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
                if(DASave == false){
                    cboDADesignationValue.setSelectedItem(cboSalaryStructureProdId.getSelectedItem());
                    tdtDAFromDateValue.setDateValue(CommonUtil.convertObjToStr(lblSalaryStructureFromDateValue.getDateValue()));
                    btnDANew.setEnabled(true);
                }
                if(CCASave == false){
                    cboCCAllowance.setSelectedItem(cboSalaryStructureProdId.getSelectedItem());
                    tdtCCAllowanceFromDateValue.setDateValue(CommonUtil.convertObjToStr(lblSalaryStructureFromDateValue.getDateValue()));
                    btnCCAllowanceNew.setEnabled(true);
                }
                if(HRASave == false){
                    cboHRAllowanceDesignation.setSelectedItem(cboSalaryStructureProdId.getSelectedItem());
                    tdtHRAllowanceFromDateValue.setDateValue(CommonUtil.convertObjToStr(lblSalaryStructureFromDateValue.getDateValue()));
                    btnHRAllowanceNew.setEnabled(true);
                }
                if(TASave == false){
                    cboTravellingAllowance.setSelectedItem(cboSalaryStructureProdId.getSelectedItem());
                    tdtTAFromDateValue.setDateValue(CommonUtil.convertObjToStr(lblSalaryStructureFromDateValue.getDateValue()));
                    btnTANew.setEnabled(true);
                }
                if(MASave  == false){
                    cboMAidDesg.setSelectedItem(cboSalaryStructureProdId.getSelectedItem());
                    tdtMAidFromDateValue.setDateValue(CommonUtil.convertObjToStr(lblSalaryStructureFromDateValue.getDateValue()));
                    btnMAidNew.setEnabled(true);
                }
                if(OASave  == false){
                    cboOADesignationValue.setSelectedItem(cboSalaryStructureProdId.getSelectedItem());
                    tdtOAFromDateValue.setDateValue(CommonUtil.convertObjToStr(lblSalaryStructureFromDateValue.getDateValue()));
                    btnOASave.setEnabled(true);
                }
            }
            if(observable.getRdoStagnationIncrement_Yes() == true){
                rdoStagnationIncrement_Yes.setSelected(true);
            }else if(observable.getRdoStagnationIncrement_No() == true){
                rdoStagnationIncrement_No.setSelected(true);
            }
            if(observable.getSSAuthorizeStatus()!=null && (observable.getSSAuthorizeStatus().equals("AUTHORIZED") ||
            observable.getSSAuthorizeStatus().equals("INACTIVE"))){
                ClientUtil.enableDisable(panSalaryStructureInfo,false);
                btnSalaryStructureNew.setEnabled(true);
                btnSalaryStructureSave.setEnabled(false);
                btnSalaryStructureDelete.setEnabled(false);
                salaryStructureSave = false;
            }else{
                btnSalaryStructureNew.setEnabled(false);
                btnSalaryStructureSave.setEnabled(true);
                btnSalaryStructureDelete.setEnabled(true);
                salaryStructureSave = true;
            }
            btnDANew.setEnabled(true);
            btnCCAllowanceNew.setEnabled(true);
            btnHRAllowanceNew.setEnabled(true);
            btnTANew.setEnabled(true);
            btnMAidNew.setEnabled(true);
            btnOANew.setEnabled(true);
        }else{
            ClientUtil.enableDisable(this,false);
        }
    }//GEN-LAST:event_tblSalaryStructureMouseClicked
    
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        callView(ClientConstants.ACTIONTYPE_VIEW);
        btnCheck();
        btnPrint.setEnabled(false);
    }//GEN-LAST:event_btnViewActionPerformed
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
    
    private String checkMandatory(JComponent component){
        return new MandatoryCheck().checkMandatory(getClass().getName(),component);
    }
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    private void updateOBFields(){
        observable.setCboSalaryStructureProdId((String)((ComboBoxModel)this.cboSalaryStructureProdId.getModel()).getKeyForSelected());
        observable.setLblSalaryStructureSLNOValue(this.lblSalaryStructureSLNOValue.getText());
        observable.setLblSalaryStructureFromDateValue(this.lblSalaryStructureFromDateValue.getDateValue());
        observable.setLblSalaryStructureToDateValue(this.lblSalaryStructureToDateValue.getDateValue());
        observable.setTxtSalaryStructureStartingAmtValue(this.txtSalaryStructureStartingAmtValue.getText());
        observable.setTxtSalaryStructureAmtValue(this.txtSalaryStructureAmtValue.getText());
        observable.setTxtSalaryStructureIncYearValue(this.txtSalaryStructureIncYearValue.getText());
        observable.setTxtSalaryStructureEndingAmtValue(txtSalaryStructureEndingAmtValue.getText());
        observable.setRdoStagnationIncrement_Yes(rdoStagnationIncrement_Yes.isSelected());
        observable.setRdoStagnationIncrement_No(rdoStagnationIncrement_No.isSelected());
        observable.setTxtSalaryStructureTotNoIncValue(txtSalaryStructureTotNoIncValue.getText());
        observable.setTxtSalaryStructureStagnationAmtValue(txtSalaryStructureStagnationAmtValue.getText());
        observable.setTxtSalaryStructureNoOfStagnationValue(txtSalaryStructureNoOfStagnationValue.getText());
        observable.setTxtSalaryStructureStagnationOnceInValue(txtSalaryStructureStagnationOnceInValue.getText());
        observable.setCboSalaryStructureStagnationOnceIn((String)((ComboBoxModel)this.cboSalaryStructureStagnationOnceIn.getModel()).getKeyForSelected());
        
        observable.setTdtDAFromDateValue(tdtDAFromDateValue.getDateValue());
        observable.setTdtDAToDateValue(tdtDAToDateValue.getDateValue());
        observable.setTxtDANoOfPointsPerSlabValue(txtDANoOfPointsPerSlabValue.getText());
        observable.setTxtDAIndexValue(txtDAIndexValue.getText());
        observable.setTxtDAPercentagePerSlabValue(txtDAPercentagePerSlabValue.getText());
        observable.setTxtTotalNoofSlabValue(txtTotalNoofSlabValue.getText());
        observable.setTxtDATotalDAPercentageValue(txtDATotalDAPercentageValue.getText());
        if(rdoIndexOrPercent_Percent.isSelected()){
            observable.setRdoIndexOrPercent("PERCENT");
        }else if(rdoIndexOrPercent_Index.isSelected()){
            observable.setRdoIndexOrPercent("INDEX");
        }
        
        observable.setCboDADesignationValue((String)((ComboBoxModel)this.cboDADesignationValue.getModel()).getKeyForSelected());
        
        observable.setTdtCCAllowanceFromDateValue(tdtCCAllowanceFromDateValue.getDateValue());
        observable.setTdtCCAllowanceToDateValue(tdtCCAllowanceToDateValue.getDateValue());
        observable.setTxtCCAllowanceStartingAmtValue(txtCCAllowanceStartingAmtValue.getText());
        observable.setTxtFromAmount(txtFromAmount.getText());
        observable.setTxtToAmount(txtToAmount.getText());
        if(rdoPercentOrFixed_Fixed.isSelected()){
            observable.setRdoPercentOrFixed("FIXED");
            
        }
        else if(rdoPercentOrFixed_Percent.isSelected()){
            observable.setRdoPercentOrFixed("PERCENT");
        }
        observable.setTxtCCAllowanceEndingAmtValue(txtCCAllowanceEndingAmtValue.getText());
        observable.setCboCCAllowanceCityType((String)((ComboBoxModel)this.cboCCAllowanceCityType.getModel()).getKeyForSelected());
        observable.setCboCCAllowance((String)((ComboBoxModel)this.cboCCAllowance.getModel()).getKeyForSelected());
        
        observable.setTdtHRAllowanceFromDateValue(tdtHRAllowanceFromDateValue.getDateValue());
        observable.setTdtHRAllowanceToDateValue(tdtHRAllowanceToDateValue.getDateValue());
        observable.setRdoHRAPayable_Yes(rdoHRAPayable_Yes.isSelected());
        observable.setRdoHRAPayable_No(rdoHRAPayable_No.isSelected());
        observable.setTxtHRAllowanceStartingAmtValue(txtHRAllowanceStartingAmtValue.getText());
        observable.setTxtHRAllowanceEndingAmtValue(txtHRAllowanceEndingAmtValue.getText());
        observable.setCboHRAllowanceCityType((String)((ComboBoxModel)this.cboHRAllowanceCityType.getModel()).getKeyForSelected());
        observable.setCboHRAllowanceDesignation((String)((ComboBoxModel)this.cboHRAllowanceDesignation.getModel()).getKeyForSelected());
        
        observable.setCboTAllowanceDesgination((String)((ComboBoxModel)this.cboTravellingAllowance.getModel()).getKeyForSelected());
        observable.setTdtTAFromDateValue(tdtTAFromDateValue.getDateValue());
        observable.setTdtTAToDateValue(tdtTAToDateValue.getDateValue());
        observable.setChkFixedConveyance(chkFixedConveyance.isSelected());
        observable.setChkPetrolAllowance(chkPetrolAllowance.isSelected());
        observable.setTxtBasicAmtUptoValue(txtBasicAmtUptoValue.getText());
        observable.setTxtConveyancePerMonthValue(txtConveyancePerMonthValue.getText());
        observable.setTxtBasicAmtBeyondValue(txtBasicAmtBeyondValue.getText());
        observable.setTxtConveyanceAmtValue(txtConveyanceAmtValue.getText());
        observable.setTxtNooflitresValue(txtNooflitresValue.getText());
        observable.setTxtPricePerlitreValue(txtPricePerlitreValue.getText());
        observable.setTxtTotalConveyanceAmtValue(txtTotalConveyanceAmtValue.getText());
        
        observable.setCboMAidDesg((String)((ComboBoxModel)this.cboMAidDesg.getModel()).getKeyForSelected());
        observable.setTdtMAidFromDateValue(tdtMAidFromDateValue.getDateValue());
        observable.setTdtMAidToDateValue(tdtMAidToDateValue.getDateValue());
        observable.setTxtMAidAmtValue(txtMAidAmtValue.getText());
        
        if(rdoBasedOnParameter_Yes.isSelected()){
            observable.setOAbasedOnParameter("Y");
            
        }
        else if(rdoBasedOnParameter_No.isSelected()){
            observable.setOAbasedOnParameter("N");
        }
        observable.setCboOADesignationValue((String)((ComboBoxModel)this.cboOADesignationValue.getModel()).getKeyForSelected());
        observable.setCboOAllowanceTypeValue((String)((ComboBoxModel)this.cboOAllowanceTypeValue.getModel()).getKeyForSelected());
        observable.setCboOAParameterBasedOnValue((String)((ComboBoxModel)this.cboOAParameterBasedOnValue.getModel()).getKeyForSelected());
        observable.setCboOASubParameterValue((String)((ComboBoxModel)this.cboOASubParameterValue.getModel()).getKeyForSelected());
        observable.setTdtOAFromDateValue(tdtOAFromDateValue.getDateValue());
        observable.setTdtOAToDateValue(tdtOAToDateValue.getDateValue());
        observable.setChkOAFixedValue(chkOAFixedValue.isSelected());
        observable.setChkOAPercentageValue(chkOAPercentageValue.isSelected());
        observable.setTxtOAFixedAmtValue(txtOAFixedAmtValue.getText());
        observable.setTxtOAPercentageValue(txtOAMaximumOfValue.getText());
        observable.setTxtOAMaximumOfValue(txtOAPercentageValue.getText());
        
    }
    private void whenTableRowSelected(HashMap paramMap) {
        String lockedBy = "";
        HashMap map = new HashMap();
        map.put("SCREEN_ID", getScreenID());
        map.put("RECORD_KEY", paramMap.get("LIENNO"));
        map.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
        System.out.println("Record Key Map : " + map);
        List lstLock = ClientUtil.executeQuery("selectEditLock", map);
        if (lstLock.size() > 0) {
            lockedBy = CommonUtil.convertObjToStr(lstLock.get(0));
            if (!lockedBy.equals(ProxyParameters.USER_ID)) {
                //                            setMode(ClientConstants.ACTIONTYPE_VIEW_MODE);
                btnSave.setEnabled(false);
            } else {
                //                            setMode(ClientConstants.ACTIONTYPE_EDIT);
                btnSave.setEnabled(true);
            }
        } else {
            //                        setMode(ClientConstants.ACTIONTYPE_EDIT);
            btnSave.setEnabled(true);
        }
        
        setOpenForEditBy(lockedBy);
        if (lockedBy.length() > 0 && !lockedBy.equals(ProxyParameters.USER_ID)) {
            String data = getLockDetails(lockedBy, getScreenID()) ;
            ClientUtil.showMessageWindow("Selected Record is Opened/Modified by " + lockedBy + data.toString());
            btnSave.setEnabled(false);
            //                    setMode(ClientConstants.ACTIONTYPE_VIEW_MODE);
            
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
    
    private void updationLien() {
        int selectRow = -1;
        selectRow = tblSalaryStructure.getSelectedRow();
        observable.populateSalaryStructure(selectRow);
        populateDetail();
        _intSalaryNew = false;
    }
    
    private void populateDetail(){
        //        cboSalaryStructureProdId.setSelectedItem(observable.getCbmSalaryStructureProdId().getKeyForSelected());
        ((ComboBoxModel)this.cboSalaryStructureStagnationOnceIn.getModel()).setKeyForSelected(observable.getCboSalaryStructureStagnationOnceIn());
        ((ComboBoxModel)this.cboSalaryStructureProdId.getModel()).setKeyForSelected(observable.getCboSalaryStructureProdId());
        lblSalaryStructureFromDateValue.setDateValue(observable.getLblSalaryStructureFromDateValue());
        lblSalaryStructureToDateValue.setDateValue(observable.getLblSalaryStructureToDateValue());
        txtSalaryStructureStartingAmtValue.setText(observable.getTxtSalaryStructureStartingAmtValue());
        txtSalaryStructureEndingAmtValue.setText(observable.getTxtSalaryStructureEndingAmtValue());
        txtSalaryStructureAmtValue.setText(observable.getTxtSalaryStructureAmtValue());
        txtSalaryStructureIncYearValue.setText(observable.getTxtSalaryStructureIncYearValue());
        rdoStagnationIncrement_Yes.setSelected(observable.getRdoStagnationIncrement_Yes());
        rdoStagnationIncrement_No.setSelected(observable.getRdoStagnationIncrement_No());
        txtSalaryStructureTotNoIncValue.setText(observable.getTxtSalaryStructureTotNoIncValue());
        txtSalaryStructureStagnationAmtValue.setText(observable.getTxtSalaryStructureStagnationAmtValue());
        txtSalaryStructureNoOfStagnationValue.setText(observable.getTxtSalaryStructureNoOfStagnationValue());
        txtSalaryStructureStagnationOnceInValue.setText(observable.getTxtSalaryStructureStagnationOnceInValue());
        
        ((ComboBoxModel)this.cboDADesignationValue.getModel()).setKeyForSelected(observable.getCboDADesignationValue());
        tdtDAFromDateValue.setDateValue(observable.getTdtDAFromDateValue());
        tdtDAToDateValue.setDateValue(observable.getTdtDAToDateValue());
        txtDANoOfPointsPerSlabValue.setText(observable.getTxtDANoOfPointsPerSlabValue());
        txtDAIndexValue.setText(observable.getTxtDAIndexValue());
        txtDAPercentagePerSlabValue.setText(observable.getTxtDAPercentagePerSlabValue());
        txtTotalNoofSlabValue.setText(observable.getTxtTotalNoofSlabValue());
        txtDATotalDAPercentageValue.setText(observable.getTxtDATotalDAPercentageValue());
        if(CommonUtil.convertObjToStr(observable.getRdoIndexOrPercent()).equals("INDEX")){
            rdoIndexOrPercent_Index.setSelected(true);
            rdoIndexOrPercent_Percent.setSelected(false);
        }else if(CommonUtil.convertObjToStr(observable.getRdoIndexOrPercent()).equals("PERCENT")){
            rdoIndexOrPercent_Index.setSelected(false);
            rdoIndexOrPercent_Percent.setSelected(true);
        }
        
        rdoHRAPayable_Yes.setSelected(observable.getRdoHRAPayable_Yes());
        rdoHRAPayable_No.setSelected(observable.getRdoHRAPayable_No());
        tdtHRAllowanceFromDateValue.setDateValue(observable.getTdtHRAllowanceFromDateValue());
        tdtHRAllowanceToDateValue.setDateValue(observable.getTdtHRAllowanceToDateValue());
        txtHRAllowanceStartingAmtValue.setText(observable.getTxtHRAllowanceStartingAmtValue());
        txtHRAllowanceEndingAmtValue.setText(observable.getTxtHRAllowanceEndingAmtValue());
        ((ComboBoxModel)this.cboHRAllowanceCityType.getModel()).setKeyForSelected(observable.getCboHRAllowanceCityType());
        ((ComboBoxModel)this.cboHRAllowanceDesignation.getModel()).setKeyForSelected(observable.getCboHRAllowanceDesignation());
        
        ((ComboBoxModel)this.cboCCAllowanceCityType.getModel()).setKeyForSelected(observable.getCboCCAllowanceCityType());
        ((ComboBoxModel)this.cboCCAllowance.getModel()).setKeyForSelected(observable.getCboCCAllowance());
        tdtCCAllowanceFromDateValue.setDateValue(observable.getTdtCCAllowanceFromDateValue());
        tdtCCAllowanceToDateValue.setDateValue(observable.getTdtCCAllowanceToDateValue());
        txtCCAllowanceStartingAmtValue.setText(observable.getTxtCCAllowanceStartingAmtValue());
        txtFromAmount.setText(observable.getTxtFromAmount());
        txtToAmount.setText(observable.getTxtToAmount());
        if(CommonUtil.convertObjToStr(observable.getRdoPercentOrFixed()).equals("FIXED")){
            rdoPercentOrFixed_Fixed.setSelected(true);
            rdoPercentOrFixed_Percent.setSelected(false);
        }else if(CommonUtil.convertObjToStr(observable.getRdoPercentOrFixed()).equals("PERCENT")){
            rdoPercentOrFixed_Percent.setSelected(true);
            rdoPercentOrFixed_Fixed.setSelected(false);
        }
        txtCCAllowanceEndingAmtValue.setText(observable.getTxtCCAllowanceEndingAmtValue());
        
        ((ComboBoxModel)this.cboTravellingAllowance.getModel()).setKeyForSelected(observable.getCboTAllowanceDesgination());
        tdtTAFromDateValue.setDateValue(observable.getTdtTAFromDateValue());
        tdtTAToDateValue.setDateValue(observable.getTdtTAToDateValue());
        chkFixedConveyance.setSelected(observable.getChkFixedConveyance());
        chkPetrolAllowance.setSelected(observable.getChkPetrolAllowance());
        txtBasicAmtUptoValue.setText(observable.getTxtBasicAmtUptoValue());
        txtConveyancePerMonthValue.setText(observable.getTxtConveyancePerMonthValue());
        txtBasicAmtBeyondValue.setText(observable.getTxtBasicAmtBeyondValue());
        txtConveyanceAmtValue.setText(observable.getTxtConveyanceAmtValue());
        txtNooflitresValue.setText(observable.getTxtNooflitresValue());
        txtPricePerlitreValue.setText(observable.getTxtPricePerlitreValue());
        txtTotalConveyanceAmtValue.setText(observable.getTxtTotalConveyanceAmtValue());
        
        ((ComboBoxModel)this.cboMAidDesg.getModel()).setKeyForSelected(observable.getCboMAidDesg());
        tdtMAidFromDateValue.setDateValue(observable.getTdtMAidFromDateValue());
        tdtMAidToDateValue.setDateValue(observable.getTdtMAidToDateValue());
        txtMAidAmtValue.setText(observable.getTxtMAidAmtValue());
        
        ((ComboBoxModel)this.cboOADesignationValue.getModel()).setKeyForSelected(observable.getCboOADesignationValue());
        ((ComboBoxModel)this.cboOAllowanceTypeValue.getModel()).setKeyForSelected(observable.getCboOAllowanceTypeValue());
        ((ComboBoxModel)this.cboOAParameterBasedOnValue.getModel()).setKeyForSelected(observable.getCboOAParameterBasedOnValue());
        ((ComboBoxModel)this.cboOASubParameterValue.getModel()).setKeyForSelected(observable.getCboOASubParameterValue());
        tdtOAFromDateValue.setDateValue(observable.getTdtOAFromDateValue());
        tdtOAToDateValue.setDateValue(observable.getTdtOAToDateValue());
        chkOAFixedValue.setSelected(observable.getChkOAFixedValue());
        chkOAPercentageValue.setSelected(observable.getChkOAPercentageValue());
        txtOAFixedAmtValue.setText(observable.getTxtOAFixedAmtValue());
        txtOAMaximumOfValue.setText(observable.getTxtOAPercentageValue());
        txtOAPercentageValue.setText(observable.getTxtOAMaximumOfValue());
        if(CommonUtil.convertObjToStr(observable.getOAbasedOnParameter()).equals("Y")){
            rdoBasedOnParameter_Yes.setSelected(true);
            rdoBasedOnParameter_No.setSelected(false);
        }else if(CommonUtil.convertObjToStr(observable.getOAbasedOnParameter()).equals("N")){
            rdoBasedOnParameter_Yes.setSelected(false);
            rdoBasedOnParameter_No.setSelected(true);
        }
        
    }
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
    private void resetTAForm(){
        //        cboTravellingAllowance.setSelectedItem("");
        //        cboCCAllowance.setSelectedItem("");
        //        tdtTAFromDateValue.setDateValue("");
        tdtTAToDateValue.setDateValue("");
        txtBasicAmtUptoValue.setText("");
        txtConveyancePerMonthValue.setText("");
        txtBasicAmtBeyondValue.setText("");
        txtConveyanceAmtValue.setText("");
        txtNooflitresValue.setText("");
        txtPricePerlitreValue.setText("");
        txtTotalConveyanceAmtValue.setText("");
    }
    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // Add your handling code here:
        HashMap reportParamMap = new HashMap();
        com.see.truetransact.clientutil.ttrintegration.LinkReport.getReports(getScreenID(), reportParamMap);
    }//GEN-LAST:event_btnPrintActionPerformed
    private void resetHRAForm(){
        cboHRAllowanceCityType.setSelectedItem("");
        //        cboHRAllowanceDesignation.setSelectedItem("");
        //        tdtHRAllowanceFromDateValue.setDateValue("");
        tdtHRAllowanceToDateValue.setDateValue("");
        txtHRAllowanceStartingAmtValue.setText("");
        txtHRAllowanceEndingAmtValue.setText("");
    }
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // Add your handling code here:
        //        updateAuthorizeStatus(ClientConstants.ACTIONTYPE_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed
    private void resetCCAForm(){
        //        cboCCAllowanceCityType.setSelectedItem("");
        //        cboCCAllowance.setSelectedItem("");
        //        tdtCCAllowanceFromDateValue.setDateValue("");
        tdtCCAllowanceToDateValue.setDateValue("");
        txtCCAllowanceStartingAmtValue.setText("");
        txtFromAmount.setText("");
        txtToAmount.setText("");
        ClientUtil.clearAll(panPercentOrFixed);
        txtCCAllowanceEndingAmtValue.setText("");
    }
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        observable.setStatus();
        updateAuthorizeStatus(ClientConstants.ACTIONTYPE_REJECT);
        btnSave.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(true);
        btnPrint.setEnabled(false);
        btnDelete.setEnabled(false);
    }//GEN-LAST:event_btnRejectActionPerformed
    private void updationNewButton(int selectRow) {
        //        int selectRow = -1;
        //        selectRow = tblLien.getSelectedRow();
        observable.populateSalaryStructure(selectRow);
        populateDetail();
        //        enableDisableLienInfo();
        //        _intSalaryNew = false;
        //        observable.setLienStatus(CommonConstants.STATUS_MODIFIED);
    }
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        observable.setStatus();
        updateAuthorizeStatus(ClientConstants.ACTIONTYPE_AUTHORIZE);
        btnSave.setEnabled(false);
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(false);
        btnPrint.setEnabled(false);
        btnDelete.setEnabled(false);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    private void updateAuthorizeStatus(int actionType){
        observable.setActionType(actionType);
        observable.setStatus();
        viewType = actionType;
        String authorizeStatus = "";
        String status = "";
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE){
            authorizeStatus = CommonConstants.STATUS_AUTHORIZED;
            status = CommonConstants.STATUS_CREATED;
        }else if(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT){
            authorizeStatus = CommonConstants.STATUS_REJECTED;
            status = CommonConstants.STATUS_CREATED;
        }else if(observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE){
            authorizeStatus = "";
            status = CommonConstants.STATUS_DELETED;
        }
        //__ To Save the data in the Internal Frame...
        setModified(true);
        HashMap mapParam = new HashMap();
        if((observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE) && isFilled){
            //Changed By Suresh
//            String isAllTabsVisited = tabSalaryStructure.isAllTabsVisited();
            
            //--- If all the tabs are not visited, then show the Message
//            if(isAllTabsVisited.length()>0){
//                ClientUtil.displayAlert(isAllTabsVisited);
//                return;
//            }else{
                tabSalaryStructure.resetVisits();
                HashMap authorizeMap = new HashMap();
                authorizeMap.put("AUTHORIZE_STATUS",authorizeStatus);
                authorizeMap.put("STATUS",status);
                authorizeMap.put("AUTHORIZE_BY",TrueTransactMain.USER_ID);
                authorizeMap.put("AUTHORIZE_DATE",curDate.clone());
                authorizeMap.put("GRADE",observable.getCboSalaryStructureProdId());
                authorizeMap.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
                authorizeMap.put("FROM_DATE",observable.getLblSalaryStructureFromDateValue());
                authorizeMap.put("TEMP_SL_NO",observable.getTempSlNo());
                ClientUtil.execute("updateAuthorizeStatusSalary",authorizeMap);
                List getDASlList = ClientUtil.executeQuery("getDASlForAuth",authorizeMap);
                if(getDASlList!= null && getDASlList.size() > 0){
                    HashMap getDASlMap = (HashMap) getDASlList.get(0);
                    System.out.println("#@$@#$@#$getDASlMap:"+getDASlMap);
                    Date fromDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getDASlMap.get("DAFROM_DATE")));
                    int daSlNo = CommonUtil.convertObjToInt(getDASlMap.get("DASL_NO"));
                    System.out.println("@#$%#$%fromDt:"+fromDt+ " @#$@#$daSlNo:"+daSlNo);
                    if(daSlNo >1){
                        int days = 1;
                        daSlNo = daSlNo -1;
                        fromDt.setDate(fromDt.getDate() - days);
                        String confimDate = CommonUtil.convertObjToStr(fromDt);
                        System.out.println("!@##!change in days:"+confimDate);
                        String DaslNoStr = String.valueOf(daSlNo);
                        authorizeMap.put("DATO_DATE",fromDt);
                        authorizeMap.put("DASL_NO",DaslNoStr);
                        System.out.println("#@$@#$@#$authorizeMap:"+authorizeMap);
                        ClientUtil.execute("updatePrevDADate",authorizeMap);
                    }
                }
                ClientUtil.execute("updateAuthorizeDearnessAllowance",authorizeMap);
                ClientUtil.execute("updateAuthorizeCAllowance",authorizeMap);
                ClientUtil.execute("updateAuthorizeHRAllowance",authorizeMap);
                ClientUtil.execute("updateAuthorizeTAllowance",authorizeMap);
                ClientUtil.execute("updateAuthorizeMAllowance",authorizeMap);
                ClientUtil.execute("updateAuthorizeOAllowance",authorizeMap);
                btnCancelActionPerformed(null);
                observable.setResultStatus();
                lblStatus.setText(authorizeStatus);
                setModified(false);
                isFilled = false;
//            }
        }else{
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            mapParam.put(CommonConstants.MAP_NAME, "getSelectSalaryAuthMode");
            mapParam.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            lblStatus.setText(ClientConstants.ACTION_STATUS[observable.getResult()]);
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            //            isFilled = true;
        }
        //        HashMap mapParam = new HashMap();
        //        if(isFilled == true){
        //            HashMap authorizeMap = new HashMap();
        //            authorizeMap.put("AUTHORIZE_STATUS",authorizeStatus);
        //            authorizeMap.put("STATUS",status);
        //            authorizeMap.put("AUTHORIZE_BY",TrueTransactMain.USER_ID);
        //            authorizeMap.put("AUTHORIZE_DATE",curDate.clone());
        //            authorizeMap.put("GRADE",observable.getCboSalaryStructureProdId());
        //            authorizeMap.put("BRANCH_CODE",TrueTransactMain.BRANCH_ID);
        //            ClientUtil.execute("updateAuthorizeStatusSalary",authorizeMap);
        //            ClientUtil.execute("updateAuthorizeDearnessAllowance",authorizeMap);
        //            ClientUtil.execute("updateAuthorizeCAllowance",authorizeMap);
        //            ClientUtil.execute("updateAuthorizeHRAllowance",authorizeMap);
        //            ClientUtil.execute("updateAuthorizeTAllowance",authorizeMap);
        //            ClientUtil.execute("updateAuthorizeMAllowance",authorizeMap);
        //            btnCancelActionPerformed(null);
        //            observable.setResultStatus();
        //            isFilled = false;
        //        }else{
        //            HashMap whereMap = new HashMap();
        //            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
        //            whereMap.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
        //            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
        //            whereMap = null;
        //            mapParam.put(CommonConstants.MAP_NAME, "getSelectSalaryEditMode");
        //            mapParam.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
        //            lblStatus.setText(ClientConstants.ACTION_STATUS[observable.getResult()]);
        //            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
        //            authorizeUI.show();
        ////            isFilled = true;
        //        }
    }
    //    public void authorize(HashMap screenMap){
    //        System.out.println("screenmap#####"+screenMap);
    //        ArrayList selectedList = (ArrayList)screenMap.get(CommonConstants.AUTHORIZEDATA);
    //        String authorizeStatus = (String)screenMap.get(CommonConstants.AUTHORIZESTATUS);
    //        HashMap dataMap;
    //        String status;
    //        double amount=0,shadowLienAmt=0,bal=0;
    //        for (int i=0, j=selectedList.size(); i < j; i++) {
    //            dataMap = (HashMap) selectedList.get(i);
    //            dataMap.put("SELECTED_AUTHORIZE_STATUS", authorizeStatus);
    //            System.out.println("authorizedatamap#####"+dataMap);
    //            observable.setAuthorizeMap(dataMap);
    //            if(!observable.authorizeStatus()){
    //                COptionPane.showMessageDialog(this,resourceBundle.getString("AUTHORIZE_WARNING"));
    //                return;
    //            }
    //        }
    //        dataMap= null;
    //    }
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        double endingAmt = 0.0;
        //        double tableValue = 0.0;
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ||
        observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
            double totalTableAmt = 0.0;
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
                if(salaryStructureSave == true){
                    ClientUtil.showAlertWindow("SalaryStructure Details not saved");
                    return;
                }else if(DASave == true){
                    ClientUtil.showAlertWindow("DA Details not saved");
                    return;
                }else if(CCASave == true){
                    ClientUtil.showAlertWindow("CCA Details not saved");
                    return;
                }else if(HRASave == true){
                    ClientUtil.showAlertWindow("HRA Details not saved");
                    return;
                }else if(TASave == true){
                    ClientUtil.showAlertWindow("TA Details not saved");
                    return;
                }else if(MASave == true){
                    ClientUtil.showAlertWindow("MA Details not saved");
                    return;
                }
            }else if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
               
            }
            if(tblSalaryStructure.getRowCount()>0){
                //                endingAmt = CommonUtil.convertObjToDouble(observable.getTxtSalaryStructureEndingAmtValue()).doubleValue();
                for (int i=0;i<tblSalaryStructure.getRowCount();i++){
                    String type = CommonUtil.convertObjToStr(tblSalaryStructure.getValueAt(i,4));
                    if(type!= null && type.equals("NI")){
                        double tableIncAmt = CommonUtil.convertObjToDouble(tblSalaryStructure.getValueAt(i,1)).doubleValue();
                        double tableNoofInc = CommonUtil.convertObjToDouble(tblSalaryStructure.getValueAt(i,2)).doubleValue();
                        totalTableAmt += tableIncAmt * tableNoofInc;
                        double startingAmt = CommonUtil.convertObjToDouble(tblSalaryStructure.getValueAt(i,0)).doubleValue();
                        endingAmt = startingAmt + totalTableAmt;
                        //                        observable.setTxtSalaryStructureEndingAmtValue(String.valueOf(startingAmt));
                    }
                    observable.setTxtSalaryStructureStartingAmtValue(CommonUtil.convertObjToStr(tblSalaryStructure.getValueAt(i,0)));
                }
                double startingScaleAmt = CommonUtil.convertObjToDouble(observable.getTxtSalaryStructureStartingAmtValue()).doubleValue();
                totalTableAmt += startingScaleAmt;
                if(endingAmt == totalTableAmt){
                    observable.doAction();
                    btnCancelActionPerformed(null);
                    observable.setResultStatus();
                    //__ Make the Screen Closable..
                    setModified(false);
                }else{
                    System.out.print("else condition:");
                    ClientUtil.showAlertWindow("Entered Amount is not Matching");
                    return;
                }
            }
        }
    }//GEN-LAST:event_btnSaveActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        observable.resetForm();
        resetSalaryStructureForm();
        resetDAForm();
        resetCCAForm();
        resetHRAForm();
        resetTAForm();
        enableDisableButtons(false);
        enableDisableDA(false);
        enableDisableCCA(false);
        enableDisableHRA(false);
        setUp(ClientConstants.ACTIONTYPE_CANCEL,false);
        setButtonEnableDisable();
        btnSalaryStructureNew.setEnabled(false);
        btnSalaryStructureSave.setEnabled(false);
        btnSalaryStructureDelete.setEnabled(false);
        btnDelete.setEnabled(true);
        btnDANew.setEnabled(false);
        btnDASave.setEnabled(false);
        btnDADelete.setEnabled(false);
        btnPrint.setEnabled(true);
        enableDisableAllscreens(false);
        isFilled = false;
        DASave = false;
        CCASave = false;
        HRASave = false;
        TASave = false;
        MASave = false;
        _intSalaryNew = false;
        _intDANew = false;
        _intCCANew = false;
        _intHRANew = false;
        _intTANew = false;
        _intMANew = false;
        tabSalaryStructure.resetVisits();
        //__ Make the Screen Closable..
        setModified(false);
        
    }//GEN-LAST:event_btnCancelActionPerformed
    private void updateTable(){
        this.tblSalaryStructure.setModel(observable.getTbmLien());
        this.tblSalaryStructure.revalidate();
        
        this.tblDA.setModel(observable.getTbmDAllowance());
        this.tblDA.revalidate();
        
        this.tblCCAllowance.setModel(observable.getTbmCCAllowance());
        this.tblCCAllowance.revalidate();
        
        this.tblHRAllowance.setModel(observable.getTbmHRAllowance());
        this.tblHRAllowance.revalidate();
        
        this.tblTravellingAllowance.setModel(observable.getTbmTAllowance());
        this.tblTravellingAllowance.revalidate();
        
        this.tblMaid.setModel(observable.getTbmMAllowance());
        this.tblMaid.revalidate();
        
        this.tblOAllowances.setModel(observable.getTbmOAllowance());
        this.tblOAllowances.revalidate();
    }
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        setUp(ClientConstants.ACTIONTYPE_DELETE, false);
        updateAuthorizeStatus(ClientConstants.ACTIONTYPE_DELETE);
        btnSave.setEnabled(false);
        btnPrint.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        observable.setStatus();
        callView(ClientConstants.ACTIONTYPE_EDIT);
        btnDelete.setEnabled(false);
        btnPrint.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        this._intSalaryNew = true;
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setUp(ClientConstants.ACTIONTYPE_NEW, true);
        btnSalaryStructureSave.setEnabled(true);
        btnView.setEnabled(false);
        btnEdit.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnPrint.setEnabled(false);
        btnNew.setEnabled(false);
        btnCancel.setEnabled(true);
        btnSave.setEnabled(true);
        btnDelete.setEnabled(false);
        enableDisableButtons(false);
        stagnationEnableDisable(false);
        txtSalaryStructureEndingAmtValue.setEnabled(false);
        rdoStagnationIncrement_Yes.setEnabled(false);
        rdoStagnationIncrement_No.setEnabled(false);
        rdoStagnationIncrement_No.setSelected(true);
        allScreensDisable(false);
        enableDisableButtons(true);
        btnDANew.setEnabled(true);
        btnCCAllowanceNew.setEnabled(true);
        btnHRAllowanceNew.setEnabled(true);
        btnTANew.setEnabled(true);
        btnMAidNew.setEnabled(true);
        btnOANew.setEnabled(true);
        tabSalaryStructure.setSelectedComponent(panSalaryDetails);
        //__ To Save the data in the Internal Frame...
        setModified(true);
        
    }//GEN-LAST:event_btnNewActionPerformed
    private void allScreensDisable(boolean allDisable){
        ClientUtil.enableDisable(panDAInfo,allDisable);
        ClientUtil.enableDisable(panCCAllowanceInfo,allDisable);
        ClientUtil.enableDisable(panHRAllowanceInfo,allDisable);
        ClientUtil.enableDisable(panTravellingAllowanceInfo,allDisable);
        ClientUtil.enableDisable(panMedicalAllowanceInfo,allDisable);
        ClientUtil.enableDisable(panOAllowanceInfo,allDisable);
    }
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void mitPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitPrintActionPerformed
        // Add your handling code here:
        this.btnPrintActionPerformed(evt);
    }//GEN-LAST:event_mitPrintActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // Add your handling code here:
        this.btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // Add your handling code here:
        this.btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // Add your handling code here:
        this.btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // Add your handling code here:
        this.btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // Add your handling code here:
        this.btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed
    private void callView(int viewType){
        HashMap viewMap = new HashMap();
        HashMap where = new HashMap();
        this.viewType = viewType;
        if (viewType==ClientConstants.ACTIONTYPE_EDIT || viewType==ClientConstants.ACTIONTYPE_VIEW ||
        viewType==ClientConstants.ACTIONTYPE_DELETE || viewType==ClientConstants.ACTIONTYPE_VIEW){
            viewMap.put(CommonConstants.MAP_NAME,"getSelectSalaryEditMode");
            where.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, where);
        }
        new ViewAll(this,viewMap).show();
    }
    
    public void setViewType(int vuType) {
        viewType = vuType;
        observable.setActionType(viewType);
        observable.setStatus();
    }
    
    public void fillData(Object obj){
        try{
            HashMap hashMap=(HashMap)obj;
            System.out.println("### fillData Hash : "+hashMap);
            HashMap returnMap = null;
            isFilled = true;
            tabSalaryStructure.setSelectedComponent(panSalaryDetails);
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW){
                ClientUtil.enableDisable(this,true);
                this.setButtonEnableDisable();
                observable.getSalaryStructureData(String.valueOf(hashMap.get("GRADE")));
                ClientUtil.enableDisable(this.panSalaryStructureInfo,false);
                ClientUtil.enableDisable(this.panDAInfo,false);
                ClientUtil.enableDisable(this.panCCAllowanceInfo,false);
                ClientUtil.enableDisable(this.panHRAllowanceInfo,false);
                ClientUtil.enableDisable(this.panTravellingAllowanceInfo,false);
                ClientUtil.enableDisable(this.panMedicalAllowanceInfo,false);
                ClientUtil.enableDisable(this.panOAllowanceInfo,false);
            }else if(viewType==ClientConstants.ACTIONTYPE_DELETE){
                this.setButtonEnableDisable();
            }else if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ||
            observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT){
                ClientUtil.enableDisable(this,false);
                this.setButtonEnableDisable();
                observable.getSalaryStructureData(String.valueOf(hashMap.get("GRADE")));
                observable.setTempSlNo(CommonUtil.convertObjToStr(hashMap.get("TEMP_SL_NO")));
            }
            hashMap = null;
            returnMap = null;
        }catch(Exception e){
            e.printStackTrace();
        }
        
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }
    
    /* Auto Generated Method - setMandatoryHashMap()
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboProductID", new Boolean(true));
        mandatoryMap.put("txtDepositNo", new Boolean(true));
        mandatoryMap.put("cboSubDepositNo", new Boolean(true));
        mandatoryMap.put("tdtLienDate", new Boolean(true));
        mandatoryMap.put("txtLienAmount", new Boolean(true));
        mandatoryMap.put("txtRemark", new Boolean(true));
        mandatoryMap.put("cboLienProductID", new Boolean(true));
        mandatoryMap.put("txtLienActNum", new Boolean(true));
        mandatoryMap.put("cboCreditType", new Boolean(true));
    }
/* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    public void update(Observable o, Object arg) {
        removeRadioButton();
        this.lblStatus.setText(observable.getLblStatus());
        this.cboSalaryStructureProdId.setModel(observable.getCbmSalaryStructureProdId());
        this.cboSalaryStructureStagnationOnceIn.setModel(observable.getCbmSalaryStructureStagnationOnceIn());
        this.cboDADesignationValue.setModel(observable.getCbmDAValue());
        this.cboCCAllowanceCityType.setModel(observable.getCbmCCAllowanceCityType());
        this.cboCCAllowance.setModel(observable.getCbmCCAllowance());
        this.cboHRAllowanceCityType.setModel(observable.getCbmHRAllowanceCityType());
        this.cboHRAllowanceDesignation.setModel(observable.getCbmHRAllowanceDesignation());
        this.cboTravellingAllowance.setModel(observable.getCbmTAllowance());
        this.cboMAidDesg.setModel(observable.getCbmMAidDesg());
        
        this.cboOADesignationValue.setModel(observable.getCbmOADesignationValue());
        this.cboOAllowanceTypeValue.setModel(observable.getCbmOAllowanceTypeValue());
        this.cboOAParameterBasedOnValue.setModel(observable.getCbmOAParameterBasedOnValue());
        this.cboOASubParameterValue.setModel(observable.getCbmOASubParameterValue());
        //        this.cboTravellingAllowanceDesg.setModel(observable.getCbmTAllowanceCityType());
        this.updateTable();
        this.populateDetail();
        addRadioButton();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCCAllowanceDelete;
    private com.see.truetransact.uicomponent.CButton btnCCAllowanceNew;
    private com.see.truetransact.uicomponent.CButton btnCCAllowanceSave;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDADelete;
    private com.see.truetransact.uicomponent.CButton btnDANew;
    private com.see.truetransact.uicomponent.CButton btnDASave;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnHRAllowanceDelete;
    private com.see.truetransact.uicomponent.CButton btnHRAllowanceNew;
    private com.see.truetransact.uicomponent.CButton btnHRAllowanceSave;
    private com.see.truetransact.uicomponent.CButton btnMAidDelete;
    private com.see.truetransact.uicomponent.CButton btnMAidNew;
    private com.see.truetransact.uicomponent.CButton btnMAidSave;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnOADelete;
    private com.see.truetransact.uicomponent.CButton btnOANew;
    private com.see.truetransact.uicomponent.CButton btnOASave;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSalaryStructureDelete;
    private com.see.truetransact.uicomponent.CButton btnSalaryStructureNew;
    private com.see.truetransact.uicomponent.CButton btnSalaryStructureSave;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnTADelete;
    private com.see.truetransact.uicomponent.CButton btnTANew;
    private com.see.truetransact.uicomponent.CButton btnTASave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboCCAllowance;
    private com.see.truetransact.uicomponent.CComboBox cboCCAllowanceCityType;
    private com.see.truetransact.uicomponent.CComboBox cboDADesignationValue;
    private com.see.truetransact.uicomponent.CComboBox cboHRAllowanceCityType;
    private com.see.truetransact.uicomponent.CComboBox cboHRAllowanceDesignation;
    private com.see.truetransact.uicomponent.CComboBox cboMAidDesg;
    private com.see.truetransact.uicomponent.CComboBox cboOADesignationValue;
    private com.see.truetransact.uicomponent.CComboBox cboOAParameterBasedOnValue;
    private com.see.truetransact.uicomponent.CComboBox cboOASubParameterValue;
    private com.see.truetransact.uicomponent.CComboBox cboOAllowanceTypeValue;
    private com.see.truetransact.uicomponent.CComboBox cboSalaryStructureProdId;
    private com.see.truetransact.uicomponent.CComboBox cboSalaryStructureStagnationOnceIn;
    private com.see.truetransact.uicomponent.CComboBox cboTravellingAllowance;
    private com.see.truetransact.uicomponent.CCheckBox chkFixedConveyance;
    private com.see.truetransact.uicomponent.CCheckBox chkOAFixedValue;
    private com.see.truetransact.uicomponent.CCheckBox chkOAPercentageValue;
    private com.see.truetransact.uicomponent.CCheckBox chkPetrolAllowance;
    private com.see.truetransact.uicomponent.CLabel lblBasedOnParameter;
    private com.see.truetransact.uicomponent.CLabel lblBasicAmtBeyond;
    private com.see.truetransact.uicomponent.CLabel lblBasicAmtUpto;
    private com.see.truetransact.uicomponent.CLabel lblCCAllowance;
    private com.see.truetransact.uicomponent.CLabel lblCCAllowanceCityType;
    private com.see.truetransact.uicomponent.CLabel lblCCAllowanceEndingAmt;
    private com.see.truetransact.uicomponent.CLabel lblCCAllowanceFromDate;
    private com.see.truetransact.uicomponent.CLabel lblCCAllowanceSLNO;
    private com.see.truetransact.uicomponent.CLabel lblCCAllowanceSLNOValue;
    private com.see.truetransact.uicomponent.CLabel lblCCAllowanceStartingAmt;
    private com.see.truetransact.uicomponent.CLabel lblCCAllowanceToDate;
    private com.see.truetransact.uicomponent.CLabel lblConveyanceAmt;
    private com.see.truetransact.uicomponent.CLabel lblConveyancePerMonth;
    private com.see.truetransact.uicomponent.CLabel lblDADesignation;
    private com.see.truetransact.uicomponent.CLabel lblDAFromDate;
    private com.see.truetransact.uicomponent.CLabel lblDAIndex;
    private com.see.truetransact.uicomponent.CLabel lblDANoOfPointsPerSlab;
    private com.see.truetransact.uicomponent.CLabel lblDAPercentagePerSlab;
    private com.see.truetransact.uicomponent.CLabel lblDASLNO;
    private com.see.truetransact.uicomponent.CLabel lblDASLNOValue;
    private com.see.truetransact.uicomponent.CLabel lblDAToDate;
    private com.see.truetransact.uicomponent.CLabel lblDATotalNoofSlab;
    private com.see.truetransact.uicomponent.CLabel lblFixedConveyance;
    private com.see.truetransact.uicomponent.CLabel lblFromAmount;
    private com.see.truetransact.uicomponent.CLabel lblHRAPayable;
    private com.see.truetransact.uicomponent.CLabel lblHRAllowanceCityType;
    private com.see.truetransact.uicomponent.CLabel lblHRAllowanceDesignation;
    private com.see.truetransact.uicomponent.CLabel lblHRAllowanceEndingAmt;
    private com.see.truetransact.uicomponent.CLabel lblHRAllowanceFromDate;
    private com.see.truetransact.uicomponent.CLabel lblHRAllowanceSLNO;
    private com.see.truetransact.uicomponent.CLabel lblHRAllowanceSLNOValue;
    private com.see.truetransact.uicomponent.CLabel lblHRAllowanceStartingAmt;
    private com.see.truetransact.uicomponent.CLabel lblHRAllowanceToDate;
    private com.see.truetransact.uicomponent.CLabel lblMAAmt;
    private com.see.truetransact.uicomponent.CLabel lblMAFromDate;
    private com.see.truetransact.uicomponent.CLabel lblMASLNO;
    private com.see.truetransact.uicomponent.CLabel lblMASLNOValue;
    private com.see.truetransact.uicomponent.CLabel lblMAToDate;
    private com.see.truetransact.uicomponent.CLabel lblMAidDesg;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNoOflitres;
    private com.see.truetransact.uicomponent.CLabel lblOADesignation;
    private com.see.truetransact.uicomponent.CLabel lblOAFixed;
    private com.see.truetransact.uicomponent.CLabel lblOAFixedAmt;
    private com.see.truetransact.uicomponent.CLabel lblOAFromDate;
    private com.see.truetransact.uicomponent.CLabel lblOAMaximumOf;
    private com.see.truetransact.uicomponent.CLabel lblOAParameterBasedon;
    private com.see.truetransact.uicomponent.CLabel lblOAPecentage;
    private com.see.truetransact.uicomponent.CLabel lblOAPercentage;
    private com.see.truetransact.uicomponent.CLabel lblOASLNO;
    private com.see.truetransact.uicomponent.CLabel lblOASLNOValue;
    private com.see.truetransact.uicomponent.CLabel lblOASubParameter;
    private com.see.truetransact.uicomponent.CLabel lblOAToDate;
    private com.see.truetransact.uicomponent.CLabel lblOAllowanceType;
    private com.see.truetransact.uicomponent.CLabel lblPercentOrFixed;
    private com.see.truetransact.uicomponent.CLabel lblPercentOrFixed1;
    private com.see.truetransact.uicomponent.CLabel lblPetrolAllowance;
    private com.see.truetransact.uicomponent.CLabel lblPricePerlitre;
    private com.see.truetransact.uicomponent.CLabel lblSalaryStructureAmt;
    private com.see.truetransact.uicomponent.CLabel lblSalaryStructureEndingAmt;
    private com.see.truetransact.uicomponent.CLabel lblSalaryStructureFromDate;
    private com.see.truetransact.uicomponent.CDateField lblSalaryStructureFromDateValue;
    private com.see.truetransact.uicomponent.CLabel lblSalaryStructureIncYear;
    private com.see.truetransact.uicomponent.CLabel lblSalaryStructureNoOfStagnation;
    private com.see.truetransact.uicomponent.CLabel lblSalaryStructureProdId;
    private com.see.truetransact.uicomponent.CLabel lblSalaryStructureSLNO;
    private com.see.truetransact.uicomponent.CLabel lblSalaryStructureSLNOValue;
    private com.see.truetransact.uicomponent.CLabel lblSalaryStructureStagnationAmt;
    private com.see.truetransact.uicomponent.CLabel lblSalaryStructureStagnationAmt1;
    private com.see.truetransact.uicomponent.CLabel lblSalaryStructureStagnationOnceIn;
    private com.see.truetransact.uicomponent.CLabel lblSalaryStructureStartingAmt;
    private com.see.truetransact.uicomponent.CLabel lblSalaryStructureToDate;
    private com.see.truetransact.uicomponent.CDateField lblSalaryStructureToDateValue;
    private com.see.truetransact.uicomponent.CLabel lblSalaryStructureTotNoInc;
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
    private com.see.truetransact.uicomponent.CLabel lblStagnationIncrement;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTAFromDate;
    private com.see.truetransact.uicomponent.CLabel lblTAToDate;
    private com.see.truetransact.uicomponent.CLabel lblToAmount;
    private com.see.truetransact.uicomponent.CLabel lblTotalConveyanceAmt;
    private com.see.truetransact.uicomponent.CLabel lblTotalDaPer;
    private com.see.truetransact.uicomponent.CLabel lblTravellingAllowanceDesg;
    private com.see.truetransact.uicomponent.CLabel lblTravellingAllowanceEndingAmt2;
    private com.see.truetransact.uicomponent.CLabel lblTravellingAllowanceSLNO;
    private com.see.truetransact.uicomponent.CLabel lblTravellingAllowanceSLNOValue;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAutoRenewal;
    private com.see.truetransact.uicomponent.CPanel panAutoRenewal1;
    private com.see.truetransact.uicomponent.CPanel panAutoRenewal2;
    private com.see.truetransact.uicomponent.CPanel panAutoRenewal3;
    private com.see.truetransact.uicomponent.CPanel panCCAllowance;
    private com.see.truetransact.uicomponent.CPanel panCCAllowanceButtons;
    private com.see.truetransact.uicomponent.CPanel panCCAllowanceInfo;
    private com.see.truetransact.uicomponent.CPanel panCCAllowanceTable;
    private com.see.truetransact.uicomponent.CPanel panCreditingACNo;
    private com.see.truetransact.uicomponent.CPanel panDAButtons;
    private com.see.truetransact.uicomponent.CPanel panDAInfo;
    private com.see.truetransact.uicomponent.CPanel panDATable;
    private com.see.truetransact.uicomponent.CPanel panDAllowances;
    private com.see.truetransact.uicomponent.CPanel panHRAllowance;
    private com.see.truetransact.uicomponent.CPanel panHRAllowanceButtons;
    private com.see.truetransact.uicomponent.CPanel panHRAllowanceInfo;
    private com.see.truetransact.uicomponent.CPanel panHRAllowanceTable;
    private com.see.truetransact.uicomponent.CPanel panIndexOrPercent;
    private com.see.truetransact.uicomponent.CPanel panMAid;
    private com.see.truetransact.uicomponent.CPanel panMAidButtons;
    private com.see.truetransact.uicomponent.CPanel panMedicalAllowance;
    private com.see.truetransact.uicomponent.CPanel panMedicalAllowanceInfo;
    private com.see.truetransact.uicomponent.CPanel panOAButtons;
    private com.see.truetransact.uicomponent.CPanel panOAllowanceInfo;
    private com.see.truetransact.uicomponent.CPanel panOAllowances;
    private com.see.truetransact.uicomponent.CPanel panOtherAllowances;
    private com.see.truetransact.uicomponent.CPanel panPercentOrFixed;
    private com.see.truetransact.uicomponent.CPanel panRdoBasedOnParameter;
    private com.see.truetransact.uicomponent.CPanel panSalaryDetails;
    private com.see.truetransact.uicomponent.CPanel panSalaryStructure;
    private com.see.truetransact.uicomponent.CPanel panSalaryStructureButtons;
    private com.see.truetransact.uicomponent.CPanel panSalaryStructureInfo;
    private com.see.truetransact.uicomponent.CPanel panSalaryStructureTable;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTravellingAllowance;
    private com.see.truetransact.uicomponent.CPanel panTravellingAllowanceButtons;
    private com.see.truetransact.uicomponent.CPanel panTravellingAllowanceInfo;
    private com.see.truetransact.uicomponent.CPanel panTravellingAllowanceTable;
    private com.see.truetransact.uicomponent.CButtonGroup rdgBasedOnParameter;
    private com.see.truetransact.uicomponent.CButtonGroup rdgHRAPayable;
    private com.see.truetransact.uicomponent.CButtonGroup rdgIndexOrPercent;
    private com.see.truetransact.uicomponent.CButtonGroup rdgPercentOrFixed;
    private com.see.truetransact.uicomponent.CButtonGroup rdgStagnationIncrement;
    private com.see.truetransact.uicomponent.CRadioButton rdoBasedOnParameter_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoBasedOnParameter_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoHRAPayable_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoHRAPayable_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoIndexOrPercent_Index;
    private com.see.truetransact.uicomponent.CRadioButton rdoIndexOrPercent_Percent;
    private com.see.truetransact.uicomponent.CRadioButton rdoPercentOrFixed_Fixed;
    private com.see.truetransact.uicomponent.CRadioButton rdoPercentOrFixed_Percent;
    private com.see.truetransact.uicomponent.CRadioButton rdoStagnationIncrement_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoStagnationIncrement_Yes;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CScrollPane srpCCAllowance;
    private com.see.truetransact.uicomponent.CScrollPane srpDA;
    private com.see.truetransact.uicomponent.CScrollPane srpHRAllowance;
    private com.see.truetransact.uicomponent.CScrollPane srpMAid;
    private com.see.truetransact.uicomponent.CScrollPane srpOAllowances;
    private com.see.truetransact.uicomponent.CScrollPane srpSalaryStructure;
    private com.see.truetransact.uicomponent.CScrollPane srpTravellingAllowance;
    private com.see.truetransact.uicomponent.CTabbedPane tabSalaryStructure;
    private com.see.truetransact.uicomponent.CTable tblCCAllowance;
    private com.see.truetransact.uicomponent.CTable tblDA;
    private com.see.truetransact.uicomponent.CTable tblHRAllowance;
    private com.see.truetransact.uicomponent.CTable tblMaid;
    private com.see.truetransact.uicomponent.CTable tblOAllowances;
    private com.see.truetransact.uicomponent.CTable tblSalaryStructure;
    private com.see.truetransact.uicomponent.CTable tblTravellingAllowance;
    private javax.swing.JToolBar tbrSalaryStructure;
    private com.see.truetransact.uicomponent.CDateField tdtCCAllowanceFromDateValue;
    private com.see.truetransact.uicomponent.CDateField tdtCCAllowanceToDateValue;
    private com.see.truetransact.uicomponent.CDateField tdtDAFromDateValue;
    private com.see.truetransact.uicomponent.CDateField tdtDAToDateValue;
    private com.see.truetransact.uicomponent.CDateField tdtHRAllowanceFromDateValue;
    private com.see.truetransact.uicomponent.CDateField tdtHRAllowanceToDateValue;
    private com.see.truetransact.uicomponent.CDateField tdtMAidFromDateValue;
    private com.see.truetransact.uicomponent.CDateField tdtMAidToDateValue;
    private com.see.truetransact.uicomponent.CDateField tdtOAFromDateValue;
    private com.see.truetransact.uicomponent.CDateField tdtOAToDateValue;
    private com.see.truetransact.uicomponent.CDateField tdtTAFromDateValue;
    private com.see.truetransact.uicomponent.CDateField tdtTAToDateValue;
    private com.see.truetransact.uicomponent.CTextField txtBasicAmtBeyondValue;
    private com.see.truetransact.uicomponent.CTextField txtBasicAmtUptoValue;
    private com.see.truetransact.uicomponent.CTextField txtCCAllowanceEndingAmtValue;
    private com.see.truetransact.uicomponent.CTextField txtCCAllowanceStartingAmtValue;
    private com.see.truetransact.uicomponent.CTextField txtConveyanceAmtValue;
    private com.see.truetransact.uicomponent.CTextField txtConveyancePerMonthValue;
    private com.see.truetransact.uicomponent.CTextField txtDAIndexValue;
    private com.see.truetransact.uicomponent.CTextField txtDANoOfPointsPerSlabValue;
    private com.see.truetransact.uicomponent.CTextField txtDAPercentagePerSlabValue;
    private com.see.truetransact.uicomponent.CTextField txtDATotalDAPercentageValue;
    private com.see.truetransact.uicomponent.CTextField txtFromAmount;
    private com.see.truetransact.uicomponent.CTextField txtHRAllowanceEndingAmtValue;
    private com.see.truetransact.uicomponent.CTextField txtHRAllowanceStartingAmtValue;
    private com.see.truetransact.uicomponent.CTextField txtMAidAmtValue;
    private com.see.truetransact.uicomponent.CTextField txtNooflitresValue;
    private com.see.truetransact.uicomponent.CTextField txtOAFixedAmtValue;
    private com.see.truetransact.uicomponent.CTextField txtOAMaximumOfValue;
    private com.see.truetransact.uicomponent.CTextField txtOAPercentageValue;
    private com.see.truetransact.uicomponent.CTextField txtPricePerlitreValue;
    private com.see.truetransact.uicomponent.CTextField txtSalaryStructureAmtValue;
    private com.see.truetransact.uicomponent.CTextField txtSalaryStructureEndingAmtValue;
    private com.see.truetransact.uicomponent.CTextField txtSalaryStructureIncYearValue;
    private com.see.truetransact.uicomponent.CTextField txtSalaryStructureNoOfStagnationValue;
    private com.see.truetransact.uicomponent.CTextField txtSalaryStructureStagnationAmtValue;
    private com.see.truetransact.uicomponent.CTextField txtSalaryStructureStagnationOnceInValue;
    private com.see.truetransact.uicomponent.CTextField txtSalaryStructureStartingAmtValue;
    private com.see.truetransact.uicomponent.CTextField txtSalaryStructureTotNoIncValue;
    private com.see.truetransact.uicomponent.CTextField txtToAmount;
    private com.see.truetransact.uicomponent.CTextField txtTotalConveyanceAmtValue;
    private com.see.truetransact.uicomponent.CTextField txtTotalNoofSlabValue;
    private com.see.truetransact.uicomponent.CTextField txtTravellingAllowanceEndingAmtValue2;
    // End of variables declaration//GEN-END:variables
    public static void main(String[] args) {
        //        SalaryStructureUI lui = new SalaryStructureUI();
        JFrame j = new JFrame();
        //        j.getContentPane().add(lui);
        j.setSize(615,600);
        j.show();
        //        lui.show();
    }
    
}

