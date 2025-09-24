/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * StandingInstructionUI.java
 * STANDING_INSTRUCTON
 * Created on February 2, 2004, 2:38 PM
 */
package com.see.truetransact.ui.supporting.standinginstruction;

/**
 *
 * @author Hemant modified by Ashok modified by Sunil Added Multi Branch support
 * Added Edit locking
 */
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.ArrayList;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uicomponent.CDateField;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CPanel;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.uicomponent.CTable;
import com.see.truetransact.uicomponent.CTextField;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.ui.deposit.TermDepositUI;
import com.see.truetransact.serverside.batchprocess.task.supporting.standinginstruction.StandingInstructionTask;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.batchprocess.task.TaskStatus;
import com.see.truetransact.ui.batchprocess.*;
import com.see.truetransact.ui.common.transaction.TransactionUI;
import com.see.truetransact.ui.common.viewall.AuthorizeListUI;

import com.see.truetransact.ui.common.viewall.NewAuthorizeListUI;
import java.util.List;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
//import com.see.truetransact.ui.supporting.standinginstruction.stp.SendToSTPUI;

public class StandingInstructionUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer, UIMandatoryField {

    private ResourceBundle resourceBundle = ResourceBundle.getBundle("com.see.truetransact.ui.supporting.standinginstruction.StandingInstructionRB", ProxyParameters.LANGUAGE);
    private HashMap mandatoryMap;
    private StandingInstructionOB observable;
    private StandingInstructionMRB objMandatoryRB = new StandingInstructionMRB();
    
    
    private String viewType = "";
    private String accNum = "";
    private String alertMsg = "";
    private EnhancedTableModel tbmDebitSI;
    private EnhancedTableModel tbmCreditSI;
    TermDepositUI termDepositUI;//rd standingInstnpurpose
    final String AUTHORIZE = "Authorize";
    final String ID = "Admin";
    private double dbTotal = 0;
    private double crTotal = 0;
    private StringBuffer message;
    private final String DI = "DIAccountNo";
    private final String CI = "CIAccountNo";
    private final String AHD = "ActHeadDebit";
    private final String AHC = "ActHeadCredit";
    private String prodType = new String();
    private boolean authorizeClicked = false;
    boolean isFilled = false;
    private String siId = "";
    private String creditProdType = "";
    private boolean callingfromDeposit = false;
    private boolean callingfromDepositEditMode = false;
    private boolean callingfromEditMode = false;
    private boolean delNotAllowed = false;
    private String callingfromDepositNo = "";
    private String callingfromParticulars = "";
    private String custCat = "";
    private boolean isCreditClicked = false;
    private boolean isDebitClicked = false;
    private int rejectFlag=0;
    boolean fromAuthorizeUI = false;
    AuthorizeListUI authorizeListUI = null;
    TransactionUI transactionUI = new TransactionUI();
    private Date currDt = null;
    NewAuthorizeListUI newauthorizeListUI = null;
    boolean fromNewAuthorizeUI = false;
    /**
     * Creates new form StandingInstructionUI
     */
    public StandingInstructionUI() {
        currDt = ClientUtil.getCurrentDate();
        settingupUI();
    }

    /**
     * Initial settings of the UI *
     */
    private void settingupUI() {
        initComponents();
        setFieldNames();
        setMandatoryHashMap();
        setObservable();
        setHelpMessage();
        initComponentData();
        setComponentMaxLength();
//        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panSchedule);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panMainSI);
//        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panCharges);
        observable.resetForm();
        ClientUtil.enableDisable(panMainSI, false);
        setButtonEnableDisable();
        setHelpBtnEnable(false);
        setDebitBtnsEnable(false);
        setCreditBtnsEnable(false);
        tabSIDetails.resetVisits();
        dtdExecDateProc.setDateValue(CommonUtil.convertObjToStr(currDt.clone()));
        dtdExecDateProc.setEnabled(false);
        dtdSuspendDt.setVisible(false);
        lblSuspendUser.setVisible(false);
        lblCloseSI.setVisible(false);
        chkSuspendUser.setVisible(false);
        chkCloseSI.setVisible(false);
        lblMoTSI.setVisible(false);
        cboMoRSI.setVisible(false);
        lblSuspDate.setVisible(false);
        //Grace Days Visible false by Kannan AR Ref. Mr.Srinath
        txtGraceDaysSI.setVisible(false);
        lblGraceDaysSI.setVisible(false);
    }

    /**
     * Adding this ui as observer to a Observable Class - StandingInstructionOB *
     */
    private void setObservable() {
        observable = StandingInstructionOB.getInstance();
        observable.addObserver(this);
    }

    public static void main(String[] arg) {
        javax.swing.JFrame jf = new javax.swing.JFrame();
        StandingInstructionUI siUI = new StandingInstructionUI();
        jf.getContentPane().add(siUI);
        siUI.show();
        jf.setSize(750, 600);
        jf.show();
    }

    /**
     * Setting up models for ComboBoxes and Tables in the UI *
     */
    private void initComponentData() {
        cboSIType.setModel(observable.getCbmSIType());
        cboProdIDDSI.setModel(observable.getCbmProdIDDSI());
        cboProdIDCSI.setModel(observable.getCbmProdIDCSI());
        cboFrequencySI.setModel(observable.getCbmFrequencySI());
        cboMoRSI.setModel(observable.getCbmMoRSI());
        cboWeekDay.setModel(observable.getCbmWeekDay());
        cboWeek.setModel(observable.getCbmWeek());
        cboSpecificDate.setModel(observable.getCbmSpecificDate());
        cboExecConfig.setModel(observable.getCbmExecConfig());
        cboProductTypeCSI.setModel(observable.getCbmProductTypeCSI());
        cboProductTypeDSI.setModel(observable.getCbmProductTypeDSI());
        cboExecutionDay.setModel(observable.getCbmExecutionDay());

        tbmDebitSI = observable.getTbmDebitSI();
        tbmCreditSI = observable.getTbmCreditSI();

        tblDebitSI.setModel(tbmDebitSI);
        tblCreditSI.setModel(tbmCreditSI);
    }

    /**
     * Setting up the name to the components in the UI *
     */
    private void setFieldNames() {
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnDeleteCSI.setName("btnDeleteCSI");
        btnDeleteDSI.setName("btnDeleteDSI");
        btnEdit.setName("btnEdit");
        btnHelpAccNoCSI.setName("btnHelpAccNoCSI");
        btnHelpAccNoDSI.setName("btnHelpAccNoDSI");
        btnNew.setName("btnNew");
        btnNewCSI.setName("btnNewCSI");
        btnAuthorize.setName("btnAuthorize");
        btnReject.setName("btnReject");
        btnException.setName("btnException");
        btnNewDSI.setName("btnNewDSI");
        btnPrint.setName("btnPrint");
        btnSave.setName("btnSave");
        btnSaveCSI.setName("btnSaveCSI");
        btnSaveDSI.setName("btnSaveDSI");
        cboFrequencySI.setName("cboFrequencySI");
        cboMoRSI.setName("cboMoRSI");
        lblProductTypeDSI.setName("lblProductTypeDSI");
        lblProductTypeCSI.setName("lblProductTypeCSI");
        cboProductTypeDSI.setName("cboProductTypeDSI");
        cboProductTypeCSI.setName("cboProductTypeCSI");
        cboProdIDCSI.setName("cboProdIDCSI");
        cboProdIDDSI.setName("cboProdIDDSI");
        cboSIType.setName("cboSIType");
        dtdEndDtSI.setName("dtdEndDtSI");
        dtdSuspendDt.setName("dtdSuspendDt");
        dtdStartDtSI.setName("dtdStartDtSI");
        lblWeekDay.setName("lblWeekDay");
        cboWeekDay.setName("cboWeekDay");
        lblSIHoliday.setName("lblSIHoliday");
        panHolidayExecution.setName("panHolidayExecution");
        lblAutomaticPosting.setName("lblAutomaticPosting");
        lblExecutionDay.setName("lblExecutionDay");
        cboExecutionDay.setName("cboExecutionDay");
        panAutoPosting.setName("panAutoPosting");
        lblForwardCount.setName("lblSIForwardCount");
        txtForwardCount.setName("txtForwardCount");
        lblAcceptanceCharges.setName("lblAcceptanceCharges");
        txtAcceptanceCharges.setName("txtAcceptanceCharges");
        lblExecutionCharges.setName("lblExecutionCharges");
        lblFailureST.setName("lblFailureST");
        lblServiceTax.setName("lblServiceTax");
        txtExecutionCharges.setName("txtExecutionCharges");
        txtFailureST.setName("txtFailureST");
        txtServiceTax.setName("txtServiceTax");
        lblFailureCharges.setName("lblFailureCharges");
        txtFailureCharges.setName("txtFailureCharges");
        lblExecConfig.setName("lblExecConfig");
        cboExecConfig.setName("cboExecConfig");
        lblWeek.setName("lblWeek");
        cboWeek.setName("cboWeek");
        lblSpecificDate.setName("lblSpecificDate");
        cboSpecificDate.setName("cboSpecificDate");
        lblAccHeadCSI.setName("lblAccHeadCSI");
        lblAccHeadDSI.setName("lblAccHeadDSI");
        txtAccHeadValueCSI.setName("txtAccHeadValueCSI");
        txtAccHeadValueDSI.setName("txtAccHeadValueDSI");
        lblAccNoCSI.setName("lblAccNoCSI");
        lblAccNoDSI.setName("lblAccNoDSI");
        lblAmountCSI.setName("lblAmountCSI");
        lblAmountDSI.setName("lblAmountDSI");
        lblDebitTotal.setName("lblDebitTotal");
        lblCreditTotal.setName("lblCreditTOtal");
        lblDbTotalValue.setName("lblDbTotalValue");
        lblCrTotValue.setName("lblCrTotValue");
        lblBeneficiarySI.setName("lblBeneficiarySI");
        lblCommChargesSI.setName("lblCommChargesSI");
        lblCommSI.setName("lblCommSI");
        lblEndDtSI.setName("lblEndDtSI");
        lblFrequencySI.setName("lblFrequencySI");
        lblGraceDaysSI.setName("lblGraceDaysSI");
        lblMinBalSI.setName("lblMinBalSI");
        lblMoTSI.setName("lblMoTSI");
        lblMsg.setName("lblMsg");
        lblMultiplieSI.setName("lblMultiplieSI");
//        lblNameCSI.setName("lblNameCSI");
//        lblNameDSI.setName("lblNameDSI");
        lblNameValueCSI.setName("lblNameValueCSI");
        lblNameValueDSI.setName("lblNameValueDSI");
        lblParticularsCSI.setName("lblParticularsCSI");
        lblParticularsDSI.setName("lblParticularsDSI");
        lblProdIDCSI.setName("lblProdIDCSI");
        lblProdIDDSI.setName("lblProdIDDSI");
        lblRettCommChargesSI.setName("lblRettCommChargesSI");
        lblRettCommSI.setName("lblRettCommSI");
        lblSINo.setName("lblSINo");
        lblSIType.setName("lblSIType");
        lblSpace.setName("lblSpace");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblStartDtSI.setName("lblStartDtSI");
        lblStatus.setName("lblStatus");
        mbrMain.setName("mbrMain");
        panDebitActDetails.setName("panDebitActDetails");
        panDebitDetails.setName("panDebitDetails");
        panProdIdAccHeadDSI.setName("panProdIdAccHeadDSI");
        panAccNumberNameDSI.setName("panAccNumberNameDSI");
        panProdIdAccHeadCSI.setName("panProdIdAccHeadCSI");
        panAccNumberCSI.setName("panAccNumberCSI");
        panCreditActDetails.setName("panCreditActDetails");
        panCreditDetails.setName("panCreditDetails");
        panCommSI.setName("panCommSI");
        panCreditSI.setName("panCreditSI");
        panDebitSI.setName("panDebitSI");
        panCrTotal.setName("panCrTotal");
        panDbTotal.setName("panDbTotal");
        panMainSI.setName("panMainSI");
        panRettCommSI.setName("panRettCommSI");
        panSchedule.setName("panSchedule");
        panSI.setName("panSI");
        panSIID.setName("panSIId");
        panCharges.setName("panSICharges");
        panStatus.setName("panStatus");
        rdoCommSI_No.setName("rdoCommSI_No");
        rdoCommSI_Yes.setName("rdoCommSI_Yes");
        rdoRettCommSI_No.setName("rdoRettCommSI_No");
        rdoRettCommSI_Yes.setName("rdoRettCommSI_Yes");
        srpCrebitSI.setName("srpCrebitSI");
        srpDebitSI.setName("srpDebitSI");
        tblCreditSI.setName("tblCreditSI");
        tblDebitSI.setName("tblDebitSI");
        txtAccNoCSI.setName("txtAccNoCSI");
        txtAccNoDSI.setName("txtAccNoDSI");
        txtAmountCSI.setName("txtAmountCSI");
        txtAmountDSI.setName("txtAmountDSI");
        txtBeneficiarySI.setName("txtBeneficiarySI");
        txtCommChargesSI.setName("txtCommChargesSI");
        txtGraceDaysSI.setName("txtGraceDaysSI");
        txtMinBalSI.setName("txtMinBalSI");
        txtMultiplieSI.setName("txtMultiplieSI");
        txtParticularsCSI.setName("txtParticularsCSI");
        txtParticularsDSI.setName("txtParticularsDSI");
        txtRettCommChargesSI.setName("txtRettCommChargesSI");
        txtSINo.setName("txtSINo");
        tabSIDetails.setName("tabSIDetails");
        rdoHolidayExecution_Yes.setName("rdoHolidayExecution_Yes");
        rdoHolidayExecution_No.setName("rdoHolidayExecution_No");
        rdoSIAutoPosting_Yes.setName("rdoSIAutoPosting_Yes");
        rdoSIAutoPosting_No.setName("rdoSIAutoPosting_No");
        lblNextExecutionDate.setName("lblNextExecutionDate");
        lblHolidayExecutionDate.setName("lblHolidayExecutionDate");
        lblFwdExecutionDate.setName("lblFwdExecutionDate");
        lblLastExecutionDate.setName("lblLastExecutionDate");
        lbl1NextExecutionDate.setName("lbl1NextExecutionDate");
        lbl1HolidayExecutionDate.setName("lbl1HolidayExecutionDate");
        lbl1FwdExecutionDate.setName("lbl1FwdExecutionDate");
        lbl1LastExecutionDate.setName("lbl1LastExecutionDate");
        chkSuspendUser.setName("chkSuspendUser");
        chkCloseSI.setName("chkCloseSI");
        lblSuspendUser.setName("lblSuspendUser");
        lblCloseSI.setName("lblCloseSI");
    }

    /**
     * Internationalizing the components *
     */
    private void internationalize() {
        btnHelpAccNoCSI.setText(resourceBundle.getString("btnHelpAccNoCSI"));
        lblRettCommChargesSI.setText(resourceBundle.getString("lblRettCommChargesSI"));
        lblAccNoDSI.setText(resourceBundle.getString("lblAccNoDSI"));
        btnHelpAccNoDSI.setText(resourceBundle.getString("btnHelpAccNoDSI"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblNameValueDSI.setText(resourceBundle.getString("lblNameValueDSI"));
//        lblNameDSI.setText(resourceBundle.getString("lblNameDSI"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblDebitTotal.setText(resourceBundle.getString("lblDebitTotal"));
        lblCreditTotal.setText(resourceBundle.getString("lblCreditTotal"));
        lblDbTotalValue.setText(resourceBundle.getString("lblDbTotalValue"));
        lblCrTotValue.setText(resourceBundle.getString("lblCrTotValue"));
        btnSaveCSI.setText(resourceBundle.getString("btnSaveCSI"));
        lblEndDtSI.setText(resourceBundle.getString("lblEndDtSI"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        btnSaveDSI.setText(resourceBundle.getString("btnSaveDSI"));
        lblMultiplieSI.setText(resourceBundle.getString("lblMultiplieSI"));
        lblRettCommSI.setText(resourceBundle.getString("lblRettCommSI"));
        lblCommChargesSI.setText(resourceBundle.getString("lblCommChargesSI"));
        lblFrequencySI.setText(resourceBundle.getString("lblFrequencySI"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        btnDeleteCSI.setText(resourceBundle.getString("btnDeleteCSI"));
        lblCommSI.setText(resourceBundle.getString("lblCommSI"));
        rdoCommSI_Yes.setText(resourceBundle.getString("rdoCommSI_Yes"));
        lblAccHeadCSI.setText(resourceBundle.getString("lblAccHeadCSI"));
        btnNewCSI.setText(resourceBundle.getString("btnNewCSI"));
        ((javax.swing.border.TitledBorder) panMainSI.getBorder()).setTitle(resourceBundle.getString("panMainSI"));
        lblSINo.setText(resourceBundle.getString("lblSINo"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblNameValueCSI.setText(resourceBundle.getString("lblNameValueCSI"));
        lblParticularsCSI.setText(resourceBundle.getString("lblParticularsCSI"));
        lblAmountDSI.setText(resourceBundle.getString("lblAmountDSI"));
        lblAmountCSI.setText(resourceBundle.getString("lblAmountCSI"));
        ((javax.swing.border.TitledBorder) panDebitSI.getBorder()).setTitle(resourceBundle.getString("panDebitSI"));
        lblStartDtSI.setText(resourceBundle.getString("lblStartDtSI"));
        rdoCommSI_No.setText(resourceBundle.getString("rdoCommSI_No"));
        rdoRettCommSI_No.setText(resourceBundle.getString("rdoRettCommSI_No"));
        lblMinBalSI.setText(resourceBundle.getString("lblMinBalSI"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblParticularsDSI.setText(resourceBundle.getString("lblParticularsDSI"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblAccHeadDSI.setText(resourceBundle.getString("lblAccHeadDSI"));
//        lblNameCSI.setText(resourceBundle.getString("lblNameCSI"));
        lblBeneficiarySI.setText(resourceBundle.getString("lblBeneficiarySI"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblMoTSI.setText(resourceBundle.getString("lblMoTSI"));
        btnNewDSI.setText(resourceBundle.getString("btnNewDSI"));
        lblSpace.setText(resourceBundle.getString("lblSpace"));
        lblProdIDCSI.setText(resourceBundle.getString("lblProdIDCSI"));
        rdoRettCommSI_Yes.setText(resourceBundle.getString("rdoRettCommSI_Yes"));
        lblGraceDaysSI.setText(resourceBundle.getString("lblGraceDaysSI"));
        lblAccNoCSI.setText(resourceBundle.getString("lblAccNoCSI"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        ((javax.swing.border.TitledBorder) panCreditSI.getBorder()).setTitle(resourceBundle.getString("panCreditSI"));
        lblProdIDDSI.setText(resourceBundle.getString("lblProdIDDSI"));
        btnDeleteDSI.setText(resourceBundle.getString("btnDeleteDSI"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        lblSIType.setText(resourceBundle.getString("lblSIType"));
        lblWeekDay.setText(resourceBundle.getString("lblWeekDay"));
        lblSIHoliday.setText(resourceBundle.getString("lblSIHoliday"));
        lblAutomaticPosting.setText(resourceBundle.getString("lblAutomaticPosting"));
        lblForwardCount.setText(resourceBundle.getString("lblSIForwardCount"));
        lblAcceptanceCharges.setText(resourceBundle.getString("lblAcceptanceCharges"));
        lblExecutionCharges.setText(resourceBundle.getString("lblExecutionCharges"));
        lblFailureST.setText(resourceBundle.getString("lblFailureST"));
        lblServiceTax.setText(resourceBundle.getString("lblServiceTax"));
        lblFailureCharges.setText(resourceBundle.getString("lblFailureCharges"));
        lblExecConfig.setText(resourceBundle.getString("lblExecConfig"));
        lblWeek.setText(resourceBundle.getString("lblWeek"));
        lblSpecificDate.setText(resourceBundle.getString("lblSpecificDate"));
        lblProductTypeCSI.setText(resourceBundle.getString("lblProductTypeCSI"));
        lblProductTypeDSI.setText(resourceBundle.getString("lblProductTypeDSI"));
        lblExecutionDay.setText(resourceBundle.getString("lblExecutionDay"));
        lblLastExecutionDate.setText(resourceBundle.getString("lblLastExecutionDate"));
        lblNextExecutionDate.setText(resourceBundle.getString("lblNextExecutionDate"));
        lblHolidayExecutionDate.setText(resourceBundle.getString("lblHolidayExecutionDate"));
        lblFwdExecutionDate.setText(resourceBundle.getString("lblFwdExecutionDate"));
        lbl1LastExecutionDate.setText(resourceBundle.getString("lbl1LastExecutionDate"));
        lbl1NextExecutionDate.setText(resourceBundle.getString("lbl1NextExecutionDate"));
        lbl1HolidayExecutionDate.setText(resourceBundle.getString("lbl1HolidayExecutionDate"));
        lbl1FwdExecutionDate.setText(resourceBundle.getString("lbl1FwdExecutionDate"));
        chkSuspendUser.setText(resourceBundle.getString("chkSuspendUser"));
        chkCloseSI.setText(resourceBundle.getString("chkCloseSI"));
        lblSuspendUser.setText(resourceBundle.getString("lblSuspendUser"));
        lblCloseSI.setText(resourceBundle.getString("lblCloseSI"));
    }

    /**
     * Setting up Mandatory Hash Map *
     */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtSINo", new Boolean(true));
        mandatoryMap.put("cboSIType", new Boolean(true));
        mandatoryMap.put("txtMinBalSI", new Boolean(false));
        mandatoryMap.put("txtGraceDaysSI", new Boolean(true));
        mandatoryMap.put("txtCommChargesSI", new Boolean(false));
        mandatoryMap.put("txtRettCommChargesSI", new Boolean(false));
        mandatoryMap.put("cboFrequencySI", new Boolean(false));
        mandatoryMap.put("cboMoRSI", new Boolean(true));
        mandatoryMap.put("dtdEndDtSI", new Boolean(true));
        mandatoryMap.put("dtdStartDtSI", new Boolean(true));
        mandatoryMap.put("rdoCommSI_Yes", new Boolean(true));
        mandatoryMap.put("rdoRettCommSI_Yes", new Boolean(true));
        mandatoryMap.put("txtParticularsDSI", new Boolean(true));
        mandatoryMap.put("txtAccNoDSI", new Boolean(true));
        mandatoryMap.put("txtAmountDSI", new Boolean(true));
        mandatoryMap.put("cboProdIDDSI", new Boolean(true));
        mandatoryMap.put("txtParticularsCSI", new Boolean(true));
        mandatoryMap.put("txtAccNoCSI", new Boolean(true));
        mandatoryMap.put("txtAmountCSI", new Boolean(true));
        mandatoryMap.put("cboProdIDCSI", new Boolean(true));
        mandatoryMap.put("txtMultiplieSI", new Boolean(false));
        mandatoryMap.put("txtBeneficiarySI", new Boolean(false));
        mandatoryMap.put("cboWeekDay", new Boolean(false));
        mandatoryMap.put("cboWeek", new Boolean(false));
        mandatoryMap.put("cboSpecificDate", new Boolean(false));
        mandatoryMap.put("rdoHolidayExecution_Yes", new Boolean(true));
        mandatoryMap.put("rdoSIAutoPosting_Yes", new Boolean(true));
        mandatoryMap.put("txtForwardCount", new Boolean(true));
        mandatoryMap.put("cboExecConfig", new Boolean(true));
        mandatoryMap.put("txtAcceptanceCharges", new Boolean(true));
        mandatoryMap.put("txtFailureCharges", new Boolean(true));
        mandatoryMap.put("txtExecutionCharges", new Boolean(true));
        mandatoryMap.put("cboProductTypeDSI", new Boolean(true));
        mandatoryMap.put("cboProductTypeCSI", new Boolean(true));
        mandatoryMap.put("cboExecutionDay", new Boolean(false));
        mandatoryMap.put("txtAccHeadValueCSI", new Boolean(false));
        mandatoryMap.put("txtAccHeadValueDSI", new Boolean(false));
        mandatoryMap.put("chkSuspendUser", new Boolean(true));
        mandatoryMap.put("chkCloseSI", new Boolean(true));
    }

    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    public void update(Observable observed, Object arg) {
        cboProductTypeDSI.setSelectedItem(observable.getCboProductTypeDSI());
        cboProdIDDSI.setSelectedItem(observable.getCboProdIDDSI());
        txtAccHeadValueDSI.setText(observable.getTxtAccHeadValueDSI());
        txtAccNoDSI.setText(observable.getTxtAccNoDSI());
        lblNameValueDSI.setText(observable.getLblNameValueDSI());
        txtAmountDSI.setText(observable.getTxtAmountDSI());
        tblDebitSI.setModel(observable.getTbmDebitSI());
        txtParticularsDSI.setText(observable.getTxtParticularsDSI());
        cboProductTypeCSI.setSelectedItem(observable.getCboProductTypeCSI());
        cboProdIDCSI.setModel(observable.getCbmProdIDCSI());
        cboProdIDCSI.setSelectedItem(observable.getCboProdIDCSI());
        txtAccHeadValueCSI.setText(observable.getTxtAccHeadValueCSI());
        txtAccNoCSI.setText(observable.getTxtAccNoCSI());
        lblNameValueCSI.setText(observable.getLblNameValueCSI());
        txtAmountCSI.setText(observable.getTxtAmountCSI());
        txtParticularsCSI.setText(observable.getTxtParticularsCSI());
        tblCreditSI.setModel(observable.getTbmCreditSI());
        txtSINo.setText(observable.getTxtSINo());
        cboSIType.setSelectedItem(observable.getCboSIType());
        txtMultiplieSI.setText(observable.getTxtMultiplieSI());
        txtBeneficiarySI.setText(observable.getTxtBeneficiarySI());
        txtMinBalSI.setText(observable.getTxtMinBalSI());
        txtGraceDaysSI.setText(observable.getTxtGraceDaysSI());
        txtCommChargesSI.setText(observable.getTxtCommChargesSI());
        txtRettCommChargesSI.setText(observable.getTxtRettCommChargesSI());
        cboFrequencySI.setSelectedItem(observable.getCboFrequencySI());
        cboMoRSI.setSelectedItem(observable.getCboMoRSI());
        rdoCommSI_Yes.setSelected(observable.getRdoCommSI_Yes());
        rdoCommSI_No.setSelected(observable.getRdoCommSI_No());
        rdoRettCommSI_Yes.setSelected(observable.getRdoRettCommSI_Yes());
        rdoRettCommSI_No.setSelected(observable.getRdoRettCommSI_No());
        dtdEndDtSI.setDateValue(observable.getDtdEndDtSI());
        dtdSuspendDt.setDateValue(observable.getDtdSuspendDt());
        dtdStartDtSI.setDateValue(observable.getDtdStartDtSI());
        cboWeekDay.setSelectedItem(observable.getCboWeekDay());
        cboWeek.setSelectedItem(observable.getCboWeek());
        cboSpecificDate.setSelectedItem(observable.getCboSpecificDate());
        rdoHolidayExecution_Yes.setSelected(observable.getRdoHolidayExecution_Yes());
        rdoHolidayExecution_No.setSelected(observable.getRdoHolidayExecution_No());
        rdoSIAutoPosting_Yes.setSelected(observable.getRdoSIAutoPosting_Yes());
        rdoSIAutoPosting_No.setSelected(observable.getRdoSIAutoPosting_No());
        txtForwardCount.setText(observable.getTxtForwardCount());
        cboExecConfig.setSelectedItem(observable.getCboExecConfig());
        txtAcceptanceCharges.setText(observable.getTxtAcceptanceCharges());
        txtExecutionCharges.setText(observable.getTxtExecutionCharges());
        txtFailureST.setText(observable.getTxtFailureST());
        txtServiceTax.setText(observable.getTxtServiceTax());
        txtFailureCharges.setText(observable.getTxtFailureCharges());
        cboExecutionDay.setSelectedItem(observable.getCboExecutionDay());
        lbl1NextExecutionDate.setText(observable.getLbl1NextExecutonDate());
        lbl1HolidayExecutionDate.setText(observable.getLbl1HolidayExecutonDate());
        lbl1FwdExecutionDate.setText(observable.getLbl1FwdExecutionDate());
        lbl1LastExecutionDate.setText(observable.getLbl1LastExecutonDate());
        chkSuspendUser.setSelected(observable.isChkSuspendUser());
        chkCloseSI.setSelected(observable.isChkCloseSI());
        txtSiDt.setText(observable.getTxtSiDt());
        chkinstalments.setSelected(observable.isChkIntalmentsYN());
        txtNoofInstalments.setText(observable.getNoOfInst());
        chkPendingInstalment.setSelected(observable.isChkPendingInstalment());
    }

    public void updateOBFields() {
        observable.setTxtSINo(txtSINo.getText());
        observable.setCboSIType(CommonUtil.convertObjToStr(cboSIType.getSelectedItem()));
        observable.setTxtMultiplieSI(txtMultiplieSI.getText());
        observable.setTxtBeneficiarySI(txtBeneficiarySI.getText());
        observable.setTxtMinBalSI(txtMinBalSI.getText());
        observable.setTxtGraceDaysSI(txtGraceDaysSI.getText());
        observable.setTxtCommChargesSI(txtCommChargesSI.getText());
        observable.setTxtRettCommChargesSI(txtRettCommChargesSI.getText());
        observable.setCboFrequencySI(CommonUtil.convertObjToStr(cboFrequencySI.getSelectedItem()));
        observable.setCboMoRSI(CommonUtil.convertObjToStr(cboMoRSI.getSelectedItem()));
        observable.setRdoCommSI_Yes(rdoCommSI_Yes.isSelected());
        observable.setRdoCommSI_No(rdoCommSI_No.isSelected());
        observable.setRdoRettCommSI_Yes(rdoRettCommSI_Yes.isSelected());
        observable.setRdoRettCommSI_No(rdoRettCommSI_No.isSelected());
        observable.setTxtParticularsDSI(txtParticularsDSI.getText());
        observable.setTxtAccNoDSI(txtAccNoDSI.getText());
        observable.setTxtAmountDSI(txtAmountDSI.getText());
        observable.setCboProdIDDSI(CommonUtil.convertObjToStr(cboProdIDDSI.getSelectedItem()));
        observable.setTxtParticularsCSI(txtParticularsCSI.getText());
        observable.setTxtAccNoCSI(txtAccNoCSI.getText());
        observable.setTxtAmountCSI(txtAmountCSI.getText());
        observable.setCboProdIDCSI(CommonUtil.convertObjToStr(cboProdIDCSI.getSelectedItem()));
        observable.setDtdEndDtSI(dtdEndDtSI.getDateValue());
        observable.setDtdSuspendDt(dtdSuspendDt.getDateValue());
        observable.setDtdStartDtSI(dtdStartDtSI.getDateValue());
        observable.setModule(getModule());
        observable.setScreen(getScreen());
        observable.setCboWeekDay(CommonUtil.convertObjToStr(cboWeekDay.getSelectedItem()));
        observable.setCboWeek(CommonUtil.convertObjToStr(cboWeek.getSelectedItem()));
        observable.setCboSpecificDate(CommonUtil.convertObjToStr(cboSpecificDate.getSelectedItem()));
        observable.setRdoHolidayExecution_Yes(rdoHolidayExecution_Yes.isSelected());
        observable.setRdoHolidayExecution_No(rdoHolidayExecution_No.isSelected());
        observable.setCboExecutionDay(CommonUtil.convertObjToStr(cboExecutionDay.getSelectedItem()));
        observable.setRdoSIAutoPosting_Yes(rdoSIAutoPosting_Yes.isSelected());
        observable.setRdoSIAutoPosting_No(rdoSIAutoPosting_No.isSelected());
        observable.setTxtForwardCount(txtForwardCount.getText());
        observable.setCboExecConfig(CommonUtil.convertObjToStr(cboExecConfig.getSelectedItem()));
        observable.setTxtAcceptanceCharges(txtAcceptanceCharges.getText());
        observable.setTxtExecutionCharges(txtExecutionCharges.getText());
        observable.setTxtFailureST(txtFailureST.getText());
        observable.setTxtServiceTax(txtServiceTax.getText());
        observable.setTxtFailureCharges(txtFailureCharges.getText());
        observable.setCboProductTypeCSI(CommonUtil.convertObjToStr(cboProductTypeCSI.getSelectedItem()));
        observable.setCboProductTypeDSI(CommonUtil.convertObjToStr(cboProductTypeDSI.getSelectedItem()));
        observable.setTxtAccHeadValueCSI(txtAccHeadValueCSI.getText());
        observable.setTxtAccHeadValueDSI(txtAccHeadValueDSI.getText());
        observable.setSelectedBranchID(getSelectedBranchID());
        observable.setChkSuspendUser(chkSuspendUser.isSelected());
        observable.setChkCloseSI(chkCloseSI.isSelected());
        observable.setTxtSiDt(txtSiDt.getText());
        observable.setNoOfInst(txtNoofInstalments.getText());
        observable.setChkIntalmentsYN(chkinstalments.isSelected());
        observable.setChkPendingInstalment(chkPendingInstalment.isSelected());
    }

    public void setHelpMessage() {
        txtSINo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSINo"));
        cboSIType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboSIType"));
        txtGraceDaysSI.setHelpMessage(lblMsg, objMandatoryRB.getString("txtGraceDaysSI"));
        txtCommChargesSI.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCommChargesSI"));
        txtRettCommChargesSI.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRettCommChargesSI"));
        cboMoRSI.setHelpMessage(lblMsg, objMandatoryRB.getString("cboMoRSI"));
        dtdEndDtSI.setHelpMessage(lblMsg, objMandatoryRB.getString("dtdEndDtSI"));
        dtdSuspendDt.setHelpMessage(lblMsg, objMandatoryRB.getString("dtdSuspendDt"));
        dtdStartDtSI.setHelpMessage(lblMsg, objMandatoryRB.getString("dtdStartDtSI"));
        rdoCommSI_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoCommSI_Yes"));
        rdoRettCommSI_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoRettCommSI_Yes"));
        txtParticularsDSI.setHelpMessage(lblMsg, objMandatoryRB.getString("txtParticularsDSI"));
        txtAccNoDSI.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAccNoDSI"));
        txtAmountDSI.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAmountDSI"));
        cboProdIDDSI.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProdIDDSI"));
        txtParticularsCSI.setHelpMessage(lblMsg, objMandatoryRB.getString("txtParticularsCSI"));
        txtAccNoCSI.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAccNoCSI"));
        txtAmountCSI.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAmountCSI"));
        txtAccHeadValueCSI.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAccHeadValueCSI"));
        txtAccHeadValueDSI.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAccHeadValueDSI"));
        cboProdIDCSI.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProdIDCSI"));
        cboFrequencySI.setHelpMessage(lblMsg, objMandatoryRB.getString("cboFrequencySI"));
        txtMinBalSI.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMinBalSI"));
        txtBeneficiarySI.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBeneficiarySI"));
        cboWeekDay.setHelpMessage(lblMsg, objMandatoryRB.getString("cboWeekDay"));
        cboWeek.setHelpMessage(lblMsg, objMandatoryRB.getString("cboWeek"));
        cboSpecificDate.setHelpMessage(lblMsg, objMandatoryRB.getString("cboSpecificDate"));
        rdoHolidayExecution_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoHolidayExecution_Yes"));
        rdoSIAutoPosting_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoSIAutoPosting_Yes"));
        txtForwardCount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtForwardCount"));
        cboExecConfig.setHelpMessage(lblMsg, objMandatoryRB.getString("cboExecConfig"));
        txtAcceptanceCharges.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAcceptanceCharges"));
        txtFailureCharges.setHelpMessage(lblMsg, objMandatoryRB.getString("txtFailureCharges"));
        txtExecutionCharges.setHelpMessage(lblMsg, objMandatoryRB.getString("txtExecutionCharges"));
        cboProductTypeDSI.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProductTypeDSI"));
        cboProductTypeCSI.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProductTypeCSI"));
        cboExecutionDay.setHelpMessage(lblMsg, objMandatoryRB.getString("cboExecutionDay"));
    }

    private void setComponentMaxLength() {
        txtSINo.setMaxLength(16);
        txtMultiplieSI.setMaxLength(16);
        txtMinBalSI.setMaxLength(16);
        txtGraceDaysSI.setMaxLength(16);
        txtCommChargesSI.setMaxLength(16);
        txtRettCommChargesSI.setMaxLength(16);
        txtCommChargesSI.setMaxLength(16);
        txtCommChargesSI.setValidation(new CurrencyValidation());
        txtBeneficiarySI.setMaxLength(64);
        txtForwardCount.setMaxLength(2);
        txtExecutionCharges.setMaxLength(16);
        txtServiceTax.setValidation(new CurrencyValidation());
        txtFailureST.setMaxLength(16);
        txtFailureST.setValidation(new CurrencyValidation());
        txtServiceTax.setMaxLength(16);
        txtFailureCharges.setMaxLength(16);
        txtAcceptanceCharges.setMaxLength(16);
        txtAccNoCSI.setAllowAll(true);
        txtAccNoCSI.setMaxLength(16);
        txtAmountCSI.setMaxLength(16);
        txtParticularsCSI.setMaxLength(256);
        txtAccNoDSI.setAllowAll(true);
        txtAccNoDSI.setMaxLength(16);
        txtAmountDSI.setMaxLength(16);
        txtParticularsCSI.setMaxLength(256);
    }

    private void callView(String currField) {
        updateOBFields();
        viewType = currField;
        HashMap viewMap = new HashMap();
        if (callingfromDepositEditMode == false && currField.equals("Edit") || currField.equals("Delete") || currField.equals("Enquiry")) {
            HashMap where = new HashMap();
            where.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
            ArrayList lst = new ArrayList();
            lst.add("SI_ID");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            viewMap.put(CommonConstants.MAP_WHERE, where);
            viewMap.put(CommonConstants.MAP_NAME, "getSelectStandingInstructionTOList");
            where = null;
//            panSchedule.setEnabled(true);
            //viewMap.put(CommonConstants.MAP_WHERE, "DELETED");
        } else {
            if (currField.equals(DI)) {
                prodType = CommonUtil.convertObjToStr(observable.getCbmProductTypeDSI().getKeyForSelected());
                if (!prodType.equals("") && prodType != null) {
                    if (prodType.equals("TD")) {
                        
                        viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountListSI" + prodType);
                        
                    } else {
                       
                        viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList" + prodType);}
                    
                    viewMap.put(CommonConstants.MAP_WHERE, getWhereMap4AccQuery(currField));

                    //txtAmountDSI.setEnabled(false);
                }
            }
            if (currField.equals(CI)) {
                prodType = CommonUtil.convertObjToStr(observable.getCbmProductTypeCSI().getKeyForSelected());
                if (!prodType.equals("") && prodType != null) {
                    if (prodType.equals("TD")) {
                        HashMap lienMap = new HashMap();
                        //System.out.println("observable.getCbmProdIDCSI().getKeyForSelected()>>>" + observable.getCbmProdIDCSI().getKeyForSelected());
                        //System.out.println("o000>>>"+observable.getCbmSIType().getKeyForSelected());
                        lienMap.put("PROD_ID", observable.getCbmProdIDCSI().getKeyForSelected());
                        List lienList = ClientUtil.executeQuery("getExcludeLienFrmStndng", lienMap);
                        lienMap = new HashMap();
                        lienMap = (HashMap) lienList.get(0);
                        if (lienMap.get("EXCLUDE_LIEN_FRM_STANDING").equals("Y")) {
                            //System.out.println("fsdffsf");
                            viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountListSIForLienCheck");
                        }else{
                        viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountListSI" + prodType);
                        }
                    }
                    else  if(prodType.equals("TL"))
                        {
                            //System.out.println("entered inside tl ");
                         viewMap.put(CommonConstants.MAP_NAME, "SI.getAccountList" + prodType);
                        }
                    else if (prodType.equals("MDS")) {
                        viewMap.put(CommonConstants.MAP_NAME, "getChittalListSI" + prodType);
                        viewMap.put(CommonConstants.MAP_WHERE, getWhereMap4AccQuery(currField));
                    } else {
                        viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList" + prodType);
                    }
                    viewMap.put(CommonConstants.MAP_WHERE, getWhereMap4AccQuery(currField));
                }
            }
            if (currField.equals(AHD) || currField.equals(AHC)) {
                viewMap.put(CommonConstants.MAP_NAME, "Cash.getSelectAcctHead");
            }
        }
        if (callingfromDepositEditMode == false) {
            new ViewAll(this, viewMap).show();
        }
    }

    private HashMap getWhereMap4AccQuery(String btnKey) {
        HashMap whereMap = new HashMap();
        whereMap.put("SELECTED_BRANCH", getSelectedBranchID());
        whereMap.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
        if (btnKey.equals("DIAccountNo")) {
            whereMap.put("PROD_ID", (String) observable.getCbmProdIDDSI().getKeyForSelected());
            return whereMap;
        } else {
            whereMap.put("PROD_ID", observable.getCbmProdIDCSI().getKeyForSelected());
            return whereMap;
        }
    }

    //This method  fills data in the UI components after selecting a row in ViewAll.
    public void fillData(Object obj) {
        setModified(true);
        HashMap hash = (HashMap) obj;
         if(hash.containsKey("NEW_FROM_AUTHORIZE_LIST_UI")){
                fromNewAuthorizeUI = true;
                newauthorizeListUI = (NewAuthorizeListUI) hash.get("PARENT");
                hash.remove("PARENT");
                viewType = AUTHORIZE;
                observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
                observable.setStatus();
                transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
                 btnSave.setEnabled(false);
                btnCancel.setEnabled(true);
                btnAuthorize.setEnabled(true);
                btnReject.setEnabled(false);
                rejectFlag=1;
            }
         if(hash.containsKey("FROM_AUTHORIZE_LIST_UI")){
                fromAuthorizeUI = true;
                authorizeListUI = (AuthorizeListUI) hash.get("PARENT");
                hash.remove("PARENT");
                viewType = AUTHORIZE;
                observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
                observable.setStatus();
                transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
                 btnSave.setEnabled(false);
                btnCancel.setEnabled(true);
                btnAuthorize.setEnabled(true);
                btnReject.setEnabled(false);
                rejectFlag=1;
            }
        if (viewType != null) {
            if (viewType.equals("Edit") || viewType.equals("Delete") || viewType.equals(AUTHORIZE) || viewType.equals("Enquiry")) {
                // if(viewType.equals("Edit")){
                HashMap whereMap = new HashMap();
                HashMap where = new HashMap();
                where.put("SI_ID", hash.get("SI_ID"));
                where.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
                whereMap.put(CommonConstants.MAP_WHERE, where);
                where = null;
                siId = CommonUtil.convertObjToStr(hash.get("SI_ID"));
                observable.getDataSI(whereMap);
                cboSITypeSelected();
                fillLblTotal(tblCreditSI, lblCrTotValue);
                fillLblTotal(tblDebitSI, lblDbTotalValue);
                ClientUtil.enableDisable(tabSIDetails, true);

                //                if(!observable.getCbmProductTypeDSI().getKeyForSelected().equals("GL")){
                //                    txtAccHeadValueDSI.setText(observable.getAccHeadLabelCaption(CommonUtil.convertObjToStr(observable.getCbmProdIDCSI().getKeyForSelected())));
                //                }else if(!observable.getCbmProductTypeCSI().getKeyForSelected().equals("GL")){
                //                    txtAccHeadValueCSI.setText(observable.getAccHeadLabelCaption(CommonUtil.convertObjToStr(observable.getCbmProdIDDSI().getKeyForSelected())));
                //                }
                update(null, null);
                //                    panSchedule.setEnabled(true);
                //                    panCharges.setEnabled(true);
                //                    cboWeek.setEnabled(true);
                //                    cboWeekDay.setEnabled(true);
                //                }
                if (prodType.equals("TD")) {
                    cboWeek.setEnabled(false);
                    cboWeekDay.setEnabled(false);
                    cboSpecificDate.setEnabled(true);
                    cboFrequencySI.setEnabled(true);
                } else {
                    cboWeek.setEnabled(true);
                    cboWeekDay.setEnabled(true);
                    cboSpecificDate.setEnabled(true);
                    cboFrequencySI.setEnabled(true);
                }
                //                    if (viewType.equals("Edit")){
                dtdSuspendDt.setVisible(true);
                lblSuspendUser.setVisible(true);
                lblCloseSI.setVisible(true);
                chkSuspendUser.setVisible(true);
                chkCloseSI.setVisible(true);
                lblSuspDate.setVisible(true);
                if (dtdSuspendDt.getDateValue() != null && !dtdSuspendDt.getDateValue().equals("")) {
                    chkSuspendUser.setSelected(true);
                }
                //                    }
                if (viewType.equals(AUTHORIZE)) {
                    creditProdType = CommonUtil.convertObjToStr(hash.get("CREDIT_PROD_TYPE"));
                }
                if (viewType.equals("Delete")) {
                    if ((CommonUtil.convertObjToStr(hash.get("AUTHORIZE_STATUS")).equals("AUTHORIZED"))
                            || ((CommonUtil.convertObjToStr(hash.get("AUTHORIZE_STATUS")).equals("")) && ((CommonUtil.convertObjToStr(hash.get("STATUS")).equals("SUSPENDED"))
                            || (CommonUtil.convertObjToStr(hash.get("STATUS")).equals("CLOSED"))))) {
                        ClientUtil.showMessageWindow("Cannot Delete running SI, go for Closure");
                        btnSave.setEnabled(false);
                        delNotAllowed = true;
                    } else {
                        //do nothing
                        btnSave.setEnabled(true);
                    }

                }

            }
            if (viewType.equals(AUTHORIZE) || viewType.equals("Enquiry")) {
                isFilled = true;

                ClientUtil.enableDisable(panMainSI, false);
                setHelpBtnEnable(false);
                btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
            
             if (viewType.equals(AUTHORIZE)){
                 btnAuthorize.setEnabled(true);
                 btnAuthorize.requestFocusInWindow();
                 btnAuthorize.setFocusable(true);
               }
            }
            if (viewType.equals("DIAccountNo") || viewType.equals("CIAccountNo")) {
                if (prodType.equals("TL")) {
                    if (viewType.equals("DIAccountNo")) {
                        displayAlert("Not Allowing For sDebit Accounts To Loan ");
                        observable.setTxtAccNoDSI("");
                        cboProductTypeDSI.setSelectedItem("");
                        cboProdIDDSI.setSelectedItem("");
                        txtAccNoDSI.setText("");
                    }
                }
                if (prodType.equals("OA")) {
                    if (viewType.equals("DIAccountNo")) {
                        setCustCat(CommonUtil.convertObjToStr(hash.get("CUSTOMERCAT")));
                    }
                }
                if (prodType.equals("TD")) {
                    txtAmountCSI.setText(CommonUtil.convertObjToStr(hash.get("AMOUNT")));
                    txtAmountCSI.setEnabled(false);
                    if (cboSIType.getSelectedIndex() > 0) { //Added By Suresh R  Recurring Product Should Allow Variable Type (i.e Amount Always Empty)
                        if (!observable.getCbmSIType().getKeyForSelected().equals("FIXED")) {
                            txtAmountCSI.setText("");
                            cboSIType.setEnabled(false);
                        }
                    }
                }
                if (prodType.equals("TL")) {
                    HashMap repayMap = new HashMap();
                    repayMap.put("ACCT_NUM", hash.get("ACCOUNTNO"));
//                    List lst = ClientUtil.executeQuery("getSelectNonEMIAccounts", repayMap);
//                    if(lst != null && lst.size()>0){
//                        ClientUtil.showAlertWindow("Not Possible to make standing instruction for this emi account");
//                        return;
//                    }
                    txtAmountCSI.setText(CommonUtil.convertObjToStr(hash.get("AMOUNT")));
                    txtAmountCSI.setText(lblDbTotalValue.getText());
                    txtAmountCSI.setEnabled(false);
                }
                String accNo = "";
                if (prodType.equals("MDS")) {
                    txtAccNoCSI.setText(CommonUtil.convertObjToStr(hash.get("CHITTAL_NO")));
                    lblNameValueCSI.setText(CommonUtil.convertObjToStr(hash.get("MEMBER_NAME")));
                    observable.setTxtAccNoDSI(CommonUtil.convertObjToStr(hash.get("CHITTAL_NO")));
                    observable.setLblNameValueCSI(CommonUtil.convertObjToStr(hash.get("MEMBER_NAME")));
                    txtAmountCSI.setText(CommonUtil.convertObjToStr(hash.get("INST_AMT")));
                    observable.setTxtAmountCSI(CommonUtil.convertObjToStr(hash.get("INST_AMT")));
                    accNo = (String) hash.get("CHITTAL_NO");
                    txtAmountCSI.setEnabled(false);
                }
                updateOBFields();

                if (!prodType.equals("MDS")) {
                    accNo = (String) hash.get("ACCOUNTNO");
                    observable.getAccNoDetails(prodType, accNo, viewType);
                }
                cboProdIDCSI.setSelectedItem(observable.getCboProdIDCSI());
                int exist = 0;
                cboFrequencySI.setEnabled(true);
                //txtParticularsDSI.setEnabled(false);
                   if (viewType.equals("DIAccountNo")) {
                    observable.setDbranchid(CommonUtil.convertObjToStr(hash.get("BRANCH_ID")));
                    observable.setCbranchID(CommonUtil.convertObjToStr(hash.get("BRANCH_ID")));
                    //  if (CommonUtil.convertObjToStr(hash.get("BRANCH_ID")).equalsIgnoreCase(CommonUtil.convertObjToStr(hash.get("BRANCH_ID")))) {
                    if (CommonUtil.convertObjToStr(hash.get("BRANCH_ID")).length() == 0) {
                        observable.setDbranchid(accNo.substring(0, 4));
                    }
                }
                if (viewType.equals("CIAccountNo")) {
                    observable.setCbranchID(CommonUtil.convertObjToStr(hash.get("BRANCH_ID")));
                    //    if (CommonUtil.convertObjToStr(hash.get("BRANCH_ID")).equals(null) || "".equalsIgnoreCase(CommonUtil.convertObjToStr(hash.get("BRANCH_ID")))) {
                    if (CommonUtil.convertObjToStr(hash.get("BRANCH_ID")).length() == 0) {
                        observable.setCbranchID(accNo.substring(0, 4));
                    }
                }
                if (viewType.equals("DIAccountNo")) {
                    exist = observable.isExistSetDI(accNo);
                    if (exist == 1) {
                       
                        btnSaveDSI.setEnabled(true);
                        btnDeleteDSI.setEnabled(true);
                    } else {
                        btnSaveDSI.setEnabled(true);
                    }
                }
                if (viewType.equals("CIAccountNo")) {
                    exist = observable.isExistSetCI(accNo);
                    if (exist == 1) {
                        btnSaveCSI.setEnabled(true);
                        btnDeleteCSI.setEnabled(true);
                    } else {
                        btnSaveCSI.setEnabled(true);
                    }
                }
                if (cboSIType.getSelectedIndex() > 0) { //Added By Suresh R  Recurring Product Should Allow Variable Type (i.e Amount Always Empty)
                    if (!observable.getCbmSIType().getKeyForSelected().equals("FIXED")) {
                        txtAmountCSI.setText("");
                        txtAmountDSI.setText("");
                        cboSIType.setEnabled(false);
                        txtAmountCSI.setEnabled(false);
                        txtAmountDSI.setEnabled(false);
                    }
                }
            }
        }
        if (viewType.equals(AHD)) {
            int exist = 0;
            exist = observable.isAcHeadExistSetDI(CommonUtil.convertObjToStr(hash.get("A/C HEAD")));
            if (exist == 1) {
                btnSaveDSI.setEnabled(true);
                btnDeleteDSI.setEnabled(true);
            } else {
                btnSaveDSI.setEnabled(true);
                observable.setTxtAccHeadValueDSI(CommonUtil.convertObjToStr(hash.get("A/C HEAD")));
            }
        }
        if (viewType.equals(AHC)) {
            int exist = 0;
            exist = observable.isAcHeadExistSetCI(CommonUtil.convertObjToStr(hash.get("A/C HEAD")));
            if (exist == 1) {
                btnSaveDSI.setEnabled(true);
                btnDeleteDSI.setEnabled(true);
            } else {
                btnSaveDSI.setEnabled(true);
                observable.setTxtAccHeadValueCSI(CommonUtil.convertObjToStr(hash.get("A/C HEAD")));
            }
        }
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());

        if(rejectFlag==1){
           btnReject.setEnabled(false);
       }
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE){
	        btnAuthorize.setEnabled(true);
	        btnAuthorize.requestFocusInWindow();
	        btnAuthorize.setFocusable(true);
      }

    }

    public void disabledTextBox(com.see.truetransact.uicomponent.CTextField cTxt, com.see.truetransact.uicomponent.CPanel container, boolean param) {
        com.see.truetransact.uicomponent.CTextField txtDefault = new com.see.truetransact.uicomponent.CTextField();

        if (param == true) {
            cTxt.setBackground(container.getBackground());
        } else {
            cTxt.setBackground(txtDefault.getBackground());
        }
        cTxt.setEnabled(!param);
    }

    private void enableDebitSIComponents(boolean state) {
        ClientUtil.enableDisable(panDebitSI, state);
        btnHelpAccNoDSI.setEnabled(state);
        btnAccHeadDSI.setEnabled(state);
    }

    private void enableCreditSIComponents(boolean state) {
        ClientUtil.enableDisable(panCreditSI, state);
        btnHelpAccNoCSI.setEnabled(state);
        btnAccHeadCSI.setEnabled(state);
    }

    private void displayAlert(String message) {
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
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
        btnView.setEnabled(!btnView.isEnabled());


        btnstp.setVisible(false);

        if (rdoCommSI_No.isSelected()) {
            txtCommChargesSI.setText("");
            txtCommChargesSI.setEnabled(false);
            txtCommChargesSI.setEditable(false);
        }

        if (rdoRettCommSI_No.isSelected()) {
            txtRettCommChargesSI.setText("");
            txtRettCommChargesSI.setEnabled(false);
            txtRettCommChargesSI.setEditable(false);
        }

        if (rdoHolidayExecution_Yes.isSelected()) {
            cboExecutionDay.setSelectedItem("");
            cboExecutionDay.setEnabled(false);
        }
        lblStatus.setText(observable.getLblStatus());
        cboSpecificDate.setVisible(true);
    }

    /* private void enableDisableTxtFields(CTextField txtfield, boolean yesno)
     {
     }*/
    private void clearLblFields() {
        lblDbTotalValue.setText("");
        lblCrTotValue.setText("");
        txtAccHeadValueDSI.setText("");
        txtAccHeadValueCSI.setText("");
    }

    private void setButtonsDI() {
        btnNewDSI.setEnabled(true);
        btnSaveDSI.setEnabled(false);
        btnDeleteDSI.setEnabled(false);
        btnHelpAccNoDSI.setEnabled(false);
        btnAccHeadDSI.setEnabled(false);
    }

    private void setButtonsCI() {
        btnNewCSI.setEnabled(true);
        btnSaveCSI.setEnabled(false);
        btnDeleteCSI.setEnabled(false);
        btnHelpAccNoCSI.setEnabled(false);
        btnAccHeadCSI.setEnabled(false);
    }

    private void displaySIID() {
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            lblSINo.setVisible(false);
            txtSINo.setVisible(false);
        } else {
            lblSINo.setVisible(true);
            txtSINo.setVisible(true);
        }
    }

    private void enabledUI() {
        ClientUtil.enableDisable(panMainSI, true);
        ClientUtil.enableDisable(panCreditSI, false);
        ClientUtil.enableDisable(panDebitSI, false);
        setButtonEnableDisable();
        setHelpBtnEnable(true);
        setButtonsDI();
        setButtonsCI();
        displaySIID();
        //        cboExecConfig.setEnabled(false);
    }

    /**
     * Filling up the label lblTotal which used to show the total DebitAmount in
     * the DebitInstruction and total Credit amount in the CreditInstruction *
     */
    private void fillLblTotal(com.see.truetransact.uicomponent.CTable tblCrDb, com.see.truetransact.uicomponent.CLabel lblTotVal) {
        int rowCount = tblCrDb.getModel().getRowCount();
        String amt = new String();
        double amount = 0;
        if (rowCount > 0) {
            double total = 0;
            for (int i = 0; i < rowCount; i++) {
                if (CommonUtil.convertObjToStr(tblCrDb.getValueAt(i, 4)).equals("")) {
                    amt = "";
                } else {
                    amt = tblCrDb.getValueAt(i, 4).toString();
                }
                if (!amt.equals("")) {
                    amount = Double.parseDouble(amt);
                }
                total += amount;
            }
            lblTotVal.setText(String.valueOf(total));
        }
    }

    /**
     * Enabling and Disabling the cbooWeek, cboWeek, CbooWeekDay according
     * cboFrequency Selected
     */
    private void enableOrDisableCbo() {
        if (observable.getCbmFrequencySI().getKeyForSelected().equals("7") || observable.getCbmFrequencySI().getKeyForSelected().equals("30")) {
            cboWeekDay.setEnabled(false);
            cboWeek.setEnabled(false);
            cboSpecificDate.setEnabled(true);
        } else {
            cboWeekDay.setEnabled(false);
            cboWeek.setEnabled(false);
            cboSpecificDate.setEnabled(false);
            cboWeekDay.setSelectedItem("");
            cboWeek.setSelectedItem("");
            cboSpecificDate.setSelectedItem("");
        }
    }

    /**
     * Making up cboProdIDDSI Enabled or disabled, making txtAccHeadDSI editable
     * or uneditable and populating the CboProductIDDSI according to the
     * cboProdTypeDSI selected *
     */
    private void cboProdTypeDSISelected() {
        try {
            if (observable.getActionType() != ClientConstants.ACTIONTYPE_CANCEL) {
                //                String prodType=((ComboBoxModel)cboProductTypeDSI.getModel()).getKeyForSelected().toString();
                //                if((prodType.equals("OA")) || (prodType.equals("AD")) || (prodType.equals("GL"))){
                if (cboProductTypeDSI.getSelectedIndex() > 0 && !observable.getCbmProductTypeDSI().getKeyForSelected().equals("GL")) {
                    observable.getDebitProductIdByType(CommonUtil.convertObjToStr(observable.getCbmProductTypeDSI().getKeyForSelected()));
                    cboProdIDDSI.setEnabled(true);
                    cboProdIDDSI.setModel(observable.getCbmProdIDDSI());
                    txtAccHeadValueDSI.setEditable(false);
                    btnAccHeadDSI.setEnabled(false);
                    btnHelpAccNoDSI.setEnabled(true);
                } else if (cboProductTypeDSI.getSelectedIndex() > 0 && observable.getCbmProductTypeDSI().getKeyForSelected().equals("GL")) {
                    cboProdIDDSI.setSelectedItem("");
                    cboProdIDDSI.setEnabled(false);
                    btnAccHeadDSI.setEnabled(true);
                    btnHelpAccNoDSI.setEnabled(false);
                    txtAccNoDSI.setText("");
                }
                //                if(prodType.equals("TL")) {
                //                    displayAlert("Not Allowing to Debit Accouts from Loan");
                //
                //                }
                //            }else{
                //                ClientUtil.showAlertWindow("Select only OA,GL or AD");
                //                cboProductTypeDSI.setSelectedItem("");
                //            }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Making up cboProdIDCSI Enabled or disabled, making txtAccHeadCSI editable
     * or uneditable and populating the CboProductICSI according to the
     * cboProdTypeCSI selected *
     */
    private void cboProdTypeCSISelected() {
        try {
            if (cboProductTypeCSI.getSelectedIndex() > 0 && !observable.getCbmProductTypeCSI().getKeyForSelected().equals("GL")) {
                observable.getCreditProductIdByType(CommonUtil.convertObjToStr(observable.getCbmProductTypeCSI().getKeyForSelected()));
                cboProdIDCSI.setEnabled(true);
                cboProdIDCSI.setModel(observable.getCbmProdIDCSI());
                txtAccHeadValueCSI.setEditable(false);
                btnAccHeadCSI.setEnabled(false);
                btnHelpAccNoCSI.setEnabled(true);
            } else if (cboProductTypeCSI.getSelectedIndex() > 0 && observable.getCbmProductTypeCSI().getKeyForSelected().equals("GL")) {
                cboProdIDCSI.setSelectedItem("");
                cboProdIDCSI.setEnabled(false);
                btnAccHeadCSI.setEnabled(true);
                btnHelpAccNoCSI.setEnabled(false);
                txtAccNoCSI.setText("");
            }
        } catch (Exception e) {
        }
    }

    /**
     * Avoiding duplication of AccHead in CreditStandingInstructionDetails  *
     */
    private void accHeadCheck(CTable tblCrdb, CTextField txtAccHeadValueCSI, String alertMsg) {
        try {
            String accHead = txtAccHeadValueCSI.getText();
            int rowCount = tblCrdb.getModel().getRowCount();
            for (int i = 0; i < rowCount; i++) {
                String tblAccHead = CommonUtil.convertObjToStr(tblCrdb.getValueAt(i, 2));
                if (!tblAccHead.equals("")) {
                    if (tblAccHead.equalsIgnoreCase(accHead)) {
                        showAlertWindow(alertMsg);
                        txtAccHeadValueCSI.setText("");
                        txtAccHeadValueCSI.requestFocus();
                        return;
                    }
                }
            }
        } catch (Exception e) {
        }

    }

    /**
     * Doing Necessary operations when cboSIType is Selected *
     */
    private void cboSITypeSelected() {
        if (cboSIType.getSelectedIndex() > 0) {
            if (observable.getCbmSIType().getKeyForSelected().equals("FIXED")) {
                txtEditable(false);
            } else {
                txtEditable(true);
                //                txtAmountDSI.setEditable(false);
                txtAmountDSI.setText("");
                //                txtAmountDSI.setEnabled(false);
            }
        }
    }

    /**
     * Making txtMultiplieSI, txtMinBalSI editable or uneditable according to
     * the StandingInstructionType Selected
     */
    private void txtEditable(boolean flag) {
        txtMultiplieSI.setText("");
        txtMinBalSI.setText("");
        txtMultiplieSI.setEditable(flag);
        txtMultiplieSI.setEnabled(flag);
        txtMinBalSI.setEditable(flag);
        txtMinBalSI.setEnabled(flag);
    }

    /**
     * To enable or disable the HelpButtons in the UI *
     */
    private void setHelpBtnEnable(boolean flag) {
        btnHelpAccNoCSI.setEnabled(flag);
        btnAccHeadCSI.setEnabled(flag);
        btnHelpAccNoDSI.setEnabled(flag);
        btnAccHeadDSI.setEnabled(flag);
    }

    /**
     * To enable or disable the buttons in panDebitOperations *
     */
    private void setDebitBtnsEnable(boolean flag) {
        btnNewDSI.setEnabled(flag);
        btnSaveDSI.setEnabled(flag);
        btnDeleteDSI.setEnabled(flag);
    }

    /**
     * To enable or disable the buttons in panCreditOperations *
     */
    private void setCreditBtnsEnable(boolean flag) {
        btnNewCSI.setEnabled(flag);
        btnSaveCSI.setEnabled(flag);
        btnDeleteCSI.setEnabled(flag);
    }

    /**
     * This method is used to validate whether the amount entered is greater
     * than zero and if not gives an alert and clears that field *
     */
    private void validateAmount(CTextField txtAmount) {
        double amount = Double.parseDouble(txtAmount.getText());
        if (amount <= 0) {
            ClientUtil.showAlertWindow(resourceBundle.getString("amountMsg"));
            txtAmount.requestFocus();
            txtAmount.setText("");
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

        rdoCommSI = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoRettCommSI = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoSIHolidayExecution = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoSIAutoForward = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrAdvances = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace7 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace17 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace18 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace19 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace20 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace21 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnstp = new com.see.truetransact.uicomponent.CButton();
        lblSpace6 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace22 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        tabDCDayBegin = new com.see.truetransact.uicomponent.CTabbedPane();
        panMainSI = new com.see.truetransact.uicomponent.CPanel();
        panSI = new com.see.truetransact.uicomponent.CPanel();
        lblSIType = new com.see.truetransact.uicomponent.CLabel();
        cboSIType = new com.see.truetransact.uicomponent.CComboBox();
        panSIIDt = new com.see.truetransact.uicomponent.CPanel();
        lblSIDt = new com.see.truetransact.uicomponent.CLabel();
        txtSiDt = new com.see.truetransact.uicomponent.CTextField();
        panSIID = new com.see.truetransact.uicomponent.CPanel();
        lblSINo = new com.see.truetransact.uicomponent.CLabel();
        txtSINo = new com.see.truetransact.uicomponent.CTextField();
        panDebitSI = new com.see.truetransact.uicomponent.CPanel();
        panDebitDetails = new com.see.truetransact.uicomponent.CPanel();
        srpDebitSI = new com.see.truetransact.uicomponent.CScrollPane();
        tblDebitSI = new com.see.truetransact.uicomponent.CTable();
        panDbTotal = new com.see.truetransact.uicomponent.CPanel();
        lblDebitTotal = new com.see.truetransact.uicomponent.CLabel();
        lblDbTotalValue = new com.see.truetransact.uicomponent.CLabel();
        panDebitOperations = new com.see.truetransact.uicomponent.CPanel();
        btnNewDSI = new com.see.truetransact.uicomponent.CButton();
        btnSaveDSI = new com.see.truetransact.uicomponent.CButton();
        btnDeleteDSI = new com.see.truetransact.uicomponent.CButton();
        panDebitActDetails = new com.see.truetransact.uicomponent.CPanel();
        lblProductTypeDSI = new com.see.truetransact.uicomponent.CLabel();
        cboProductTypeDSI = new com.see.truetransact.uicomponent.CComboBox();
        lblProdIDDSI = new com.see.truetransact.uicomponent.CLabel();
        lblAccNoDSI = new com.see.truetransact.uicomponent.CLabel();
        panAccNumberNameDSI = new com.see.truetransact.uicomponent.CPanel();
        txtAccNoDSI = new com.see.truetransact.uicomponent.CTextField();
        btnHelpAccNoDSI = new com.see.truetransact.uicomponent.CButton();
        panNameDSI = new com.see.truetransact.uicomponent.CPanel();
        lblNameValueDSI = new com.see.truetransact.uicomponent.CLabel();
        lblAmountDSI = new com.see.truetransact.uicomponent.CLabel();
        txtAmountDSI = new com.see.truetransact.uicomponent.CTextField();
        lblParticularsDSI = new com.see.truetransact.uicomponent.CLabel();
        txtParticularsDSI = new com.see.truetransact.uicomponent.CTextField();
        panProdIdAccHeadDSI = new com.see.truetransact.uicomponent.CPanel();
        lblAccHeadDSI = new com.see.truetransact.uicomponent.CLabel();
        panAccHeadDSI = new com.see.truetransact.uicomponent.CPanel();
        txtAccHeadValueDSI = new com.see.truetransact.uicomponent.CTextField();
        btnAccHeadDSI = new com.see.truetransact.uicomponent.CButton();
        cboProdIDDSI = new com.see.truetransact.uicomponent.CComboBox();
        panCreditSI = new com.see.truetransact.uicomponent.CPanel();
        panCreditDetails = new com.see.truetransact.uicomponent.CPanel();
        panCrTotal = new com.see.truetransact.uicomponent.CPanel();
        lblCreditTotal = new com.see.truetransact.uicomponent.CLabel();
        lblCrTotValue = new com.see.truetransact.uicomponent.CLabel();
        srpCrebitSI = new com.see.truetransact.uicomponent.CScrollPane();
        tblCreditSI = new com.see.truetransact.uicomponent.CTable();
        panCreditOperations = new com.see.truetransact.uicomponent.CPanel();
        btnNewCSI = new com.see.truetransact.uicomponent.CButton();
        btnSaveCSI = new com.see.truetransact.uicomponent.CButton();
        btnDeleteCSI = new com.see.truetransact.uicomponent.CButton();
        panCreditActDetails = new com.see.truetransact.uicomponent.CPanel();
        lblProductTypeCSI = new com.see.truetransact.uicomponent.CLabel();
        cboProductTypeCSI = new com.see.truetransact.uicomponent.CComboBox();
        lblProdIDCSI = new com.see.truetransact.uicomponent.CLabel();
        lblAccNoCSI = new com.see.truetransact.uicomponent.CLabel();
        panAccNumberCSI = new com.see.truetransact.uicomponent.CPanel();
        txtAccNoCSI = new com.see.truetransact.uicomponent.CTextField();
        btnHelpAccNoCSI = new com.see.truetransact.uicomponent.CButton();
        panNameCSI = new com.see.truetransact.uicomponent.CPanel();
        lblNameValueCSI = new com.see.truetransact.uicomponent.CLabel();
        lblAmountCSI = new com.see.truetransact.uicomponent.CLabel();
        txtAmountCSI = new com.see.truetransact.uicomponent.CTextField();
        lblParticularsCSI = new com.see.truetransact.uicomponent.CLabel();
        txtParticularsCSI = new com.see.truetransact.uicomponent.CTextField();
        panProdIdAccHeadCSI = new com.see.truetransact.uicomponent.CPanel();
        lblAccHeadCSI = new com.see.truetransact.uicomponent.CLabel();
        panAccHead = new com.see.truetransact.uicomponent.CPanel();
        txtAccHeadValueCSI = new com.see.truetransact.uicomponent.CTextField();
        btnAccHeadCSI = new com.see.truetransact.uicomponent.CButton();
        cboProdIDCSI = new com.see.truetransact.uicomponent.CComboBox();
        tabSIDetails = new com.see.truetransact.uicomponent.CTabbedPane();
        panSchedule = new com.see.truetransact.uicomponent.CPanel();
        lblFrequencySI = new com.see.truetransact.uicomponent.CLabel();
        cboFrequencySI = new com.see.truetransact.uicomponent.CComboBox();
        lblWeekDay = new com.see.truetransact.uicomponent.CLabel();
        cboWeekDay = new com.see.truetransact.uicomponent.CComboBox();
        lblWeek = new com.see.truetransact.uicomponent.CLabel();
        cboWeek = new com.see.truetransact.uicomponent.CComboBox();
        lblSpecificDate = new com.see.truetransact.uicomponent.CLabel();
        cboSpecificDate = new com.see.truetransact.uicomponent.CComboBox();
        lblGraceDaysSI = new com.see.truetransact.uicomponent.CLabel();
        txtGraceDaysSI = new com.see.truetransact.uicomponent.CTextField();
        lblSIHoliday = new com.see.truetransact.uicomponent.CLabel();
        panHolidayExecution = new com.see.truetransact.uicomponent.CPanel();
        rdoHolidayExecution_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoHolidayExecution_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblAutomaticPosting = new com.see.truetransact.uicomponent.CLabel();
        panAutoPosting = new com.see.truetransact.uicomponent.CPanel();
        rdoSIAutoPosting_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoSIAutoPosting_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblForwardCount = new com.see.truetransact.uicomponent.CLabel();
        txtForwardCount = new com.see.truetransact.uicomponent.CTextField();
        lblMultiplieSI = new com.see.truetransact.uicomponent.CLabel();
        txtMultiplieSI = new com.see.truetransact.uicomponent.CTextField();
        lblMinBalSI = new com.see.truetransact.uicomponent.CLabel();
        txtMinBalSI = new com.see.truetransact.uicomponent.CTextField();
        lblBeneficiarySI = new com.see.truetransact.uicomponent.CLabel();
        txtBeneficiarySI = new com.see.truetransact.uicomponent.CTextField();
        lblMoTSI = new com.see.truetransact.uicomponent.CLabel();
        cboMoRSI = new com.see.truetransact.uicomponent.CComboBox();
        dtdStartDtSI = new com.see.truetransact.uicomponent.CDateField();
        lblStartDtSI = new com.see.truetransact.uicomponent.CLabel();
        lblEndDtSI = new com.see.truetransact.uicomponent.CLabel();
        dtdEndDtSI = new com.see.truetransact.uicomponent.CDateField();
        lblExecutionDay = new com.see.truetransact.uicomponent.CLabel();
        cboExecutionDay = new com.see.truetransact.uicomponent.CComboBox();
        lblLastExecutionDate = new com.see.truetransact.uicomponent.CLabel();
        lbl1LastExecutionDate = new com.see.truetransact.uicomponent.CLabel();
        lblNextExecutionDate = new com.see.truetransact.uicomponent.CLabel();
        lbl1NextExecutionDate = new com.see.truetransact.uicomponent.CLabel();
        chkSuspendUser = new com.see.truetransact.uicomponent.CCheckBox();
        lblSuspendUser = new com.see.truetransact.uicomponent.CLabel();
        lblHolidayExecutionDate = new com.see.truetransact.uicomponent.CLabel();
        lbl1HolidayExecutionDate = new com.see.truetransact.uicomponent.CLabel();
        lblFwdExecutionDate = new com.see.truetransact.uicomponent.CLabel();
        lbl1FwdExecutionDate = new com.see.truetransact.uicomponent.CLabel();
        dtdSuspendDt = new com.see.truetransact.uicomponent.CDateField();
        lblSuspDate = new com.see.truetransact.uicomponent.CLabel();
        chkCloseSI = new com.see.truetransact.uicomponent.CCheckBox();
        lblCloseSI = new com.see.truetransact.uicomponent.CLabel();
        panCharges = new com.see.truetransact.uicomponent.CPanel();
        lblCommSI = new com.see.truetransact.uicomponent.CLabel();
        panCommSI = new com.see.truetransact.uicomponent.CPanel();
        rdoCommSI_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoCommSI_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblCommChargesSI = new com.see.truetransact.uicomponent.CLabel();
        txtCommChargesSI = new com.see.truetransact.uicomponent.CTextField();
        lblRettCommSI = new com.see.truetransact.uicomponent.CLabel();
        panRettCommSI = new com.see.truetransact.uicomponent.CPanel();
        rdoRettCommSI_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoRettCommSI_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblRettCommChargesSI = new com.see.truetransact.uicomponent.CLabel();
        txtRettCommChargesSI = new com.see.truetransact.uicomponent.CTextField();
        lblAcceptanceCharges = new com.see.truetransact.uicomponent.CLabel();
        txtAcceptanceCharges = new com.see.truetransact.uicomponent.CTextField();
        lblExecutionCharges = new com.see.truetransact.uicomponent.CLabel();
        txtExecutionCharges = new com.see.truetransact.uicomponent.CTextField();
        lblFailureCharges = new com.see.truetransact.uicomponent.CLabel();
        txtFailureCharges = new com.see.truetransact.uicomponent.CTextField();
        lblExecConfig = new com.see.truetransact.uicomponent.CLabel();
        cboExecConfig = new com.see.truetransact.uicomponent.CComboBox();
        txtServiceTax = new com.see.truetransact.uicomponent.CTextField();
        lblServiceTax = new com.see.truetransact.uicomponent.CLabel();
        txtFailureST = new com.see.truetransact.uicomponent.CTextField();
        lblFailureST = new com.see.truetransact.uicomponent.CLabel();
        lblInstalments = new com.see.truetransact.uicomponent.CLabel();
        chkinstalments = new com.see.truetransact.uicomponent.CCheckBox();
        lblNoofInstalments = new com.see.truetransact.uicomponent.CLabel();
        txtNoofInstalments = new com.see.truetransact.uicomponent.CTextField();
        chkPendingInstalment = new com.see.truetransact.uicomponent.CCheckBox();
        panBatchProcess1 = new com.see.truetransact.uicomponent.CPanel();
        sptSpace2 = new com.see.truetransact.uicomponent.CSeparator();
        panCheckBoxes1 = new com.see.truetransact.uicomponent.CPanel();
        panMultiSearch = new com.see.truetransact.uicomponent.CPanel();
        btnSearchSI = new com.see.truetransact.uicomponent.CButton();
        lblExecDateProc = new com.see.truetransact.uicomponent.CLabel();
        dtdExecDateProc = new com.see.truetransact.uicomponent.CDateField();
        chkSelectAll1 = new com.see.truetransact.uicomponent.CCheckBox();
        btnSearchSI1 = new com.see.truetransact.uicomponent.CButton();
        scrOutputMsg1 = new com.see.truetransact.uicomponent.CScrollPane();
        tblData = new com.see.truetransact.uicomponent.CTable();
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

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMinimumSize(new java.awt.Dimension(860, 678));
        setPreferredSize(new java.awt.Dimension(860, 678));

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setEnabled(false);
        btnView.setFocusable(false);
        btnView.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView.setPreferredSize(new java.awt.Dimension(21, 21));
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnView);

        lblSpace7.setText("     ");
        tbrAdvances.add(lblSpace7);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.setFocusable(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnNew);

        lblSpace17.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace17.setText("     ");
        lblSpace17.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace17);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.setFocusable(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnEdit);

        lblSpace18.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace18.setText("     ");
        lblSpace18.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace18.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace18.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace18);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.setFocusable(false);
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
        btnSave.setFocusable(false);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnSave);

        lblSpace19.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace19.setText("     ");
        lblSpace19.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace19.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace19.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace19);

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

        lblSpace3.setText("     ");
        tbrAdvances.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnAuthorize);

        lblSpace20.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace20.setText("     ");
        lblSpace20.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace20.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace20.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace20);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.setFocusable(false);
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnException);

        lblSpace21.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace21.setText("     ");
        lblSpace21.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace21);

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

        btnstp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_ClientView.gif"))); // NOI18N
        btnstp.setToolTipText("Exception");
        btnstp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnstpActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnstp);

        lblSpace6.setText("     ");
        tbrAdvances.add(lblSpace6);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.setFocusable(false);
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnPrint);

        lblSpace22.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace22.setText("     ");
        lblSpace22.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace22);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.setFocusable(false);
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnClose);

        getContentPane().add(tbrAdvances, java.awt.BorderLayout.NORTH);

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

        getContentPane().add(panStatus, java.awt.BorderLayout.SOUTH);

        tabDCDayBegin.setMinimumSize(new java.awt.Dimension(890, 524));
        tabDCDayBegin.setPreferredSize(new java.awt.Dimension(890, 524));

        panMainSI.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panMainSI.setMaximumSize(new java.awt.Dimension(750, 500));
        panMainSI.setMinimumSize(new java.awt.Dimension(652, 371));
        panMainSI.setPreferredSize(new java.awt.Dimension(700, 600));
        panMainSI.setLayout(new java.awt.GridBagLayout());

        panSI.setLayout(new java.awt.GridBagLayout());

        lblSIType.setText("Standing Instruction Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSI.add(lblSIType, gridBagConstraints);

        cboSIType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboSIType.setPopupWidth(100);
        cboSIType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboSITypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSI.add(cboSIType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panMainSI.add(panSI, gridBagConstraints);

        panSIIDt.setLayout(new java.awt.GridBagLayout());

        lblSIDt.setText("Created Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSIIDt.add(lblSIDt, gridBagConstraints);

        txtSiDt.setEditable(false);
        txtSiDt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSIIDt.add(txtSiDt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 50, 0, 0);
        panMainSI.add(panSIIDt, gridBagConstraints);

        panSIID.setLayout(new java.awt.GridBagLayout());

        lblSINo.setText("Standing Instruction No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSIID.add(lblSINo, gridBagConstraints);

        txtSINo.setEditable(false);
        txtSINo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panSIID.add(txtSINo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 50, 0, 0);
        panMainSI.add(panSIID, gridBagConstraints);

        panDebitSI.setBorder(javax.swing.BorderFactory.createTitledBorder(" Debit Account Details "));
        panDebitSI.setMinimumSize(new java.awt.Dimension(816, 160));
        panDebitSI.setPreferredSize(new java.awt.Dimension(816, 160));
        panDebitSI.setLayout(new java.awt.GridBagLayout());

        panDebitDetails.setMaximumSize(new java.awt.Dimension(400, 128));
        panDebitDetails.setMinimumSize(new java.awt.Dimension(400, 125));
        panDebitDetails.setPreferredSize(new java.awt.Dimension(400, 125));
        panDebitDetails.setLayout(new java.awt.GridBagLayout());

        srpDebitSI.setMinimumSize(new java.awt.Dimension(400, 80));
        srpDebitSI.setPreferredSize(new java.awt.Dimension(400, 80));

        tblDebitSI.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Product Type", "Product ID", "Account Head", "Account  No.", "Amount"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDebitSI.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblDebitSIMousePressed(evt);
            }
        });
        srpDebitSI.setViewportView(tblDebitSI);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        panDebitDetails.add(srpDebitSI, gridBagConstraints);

        panDbTotal.setMinimumSize(new java.awt.Dimension(48, 18));
        panDbTotal.setPreferredSize(new java.awt.Dimension(48, 18));
        panDbTotal.setLayout(new java.awt.GridBagLayout());

        lblDebitTotal.setText("Total");
        lblDebitTotal.setMinimumSize(new java.awt.Dimension(45, 18));
        lblDebitTotal.setPreferredSize(new java.awt.Dimension(45, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDbTotal.add(lblDebitTotal, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 4);
        panDbTotal.add(lblDbTotalValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipadx = 85;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 260, 2, 0);
        panDebitDetails.add(panDbTotal, gridBagConstraints);

        panDebitOperations.setMinimumSize(new java.awt.Dimension(217, 29));
        panDebitOperations.setPreferredSize(new java.awt.Dimension(217, 29));
        panDebitOperations.setLayout(new java.awt.GridBagLayout());

        btnNewDSI.setText("New");
        btnNewDSI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewDSIActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitOperations.add(btnNewDSI, gridBagConstraints);

        btnSaveDSI.setText("Save");
        btnSaveDSI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveDSIActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitOperations.add(btnSaveDSI, gridBagConstraints);

        btnDeleteDSI.setText("Delete");
        btnDeleteDSI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteDSIActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitOperations.add(btnDeleteDSI, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        panDebitDetails.add(panDebitOperations, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panDebitSI.add(panDebitDetails, gridBagConstraints);

        panDebitActDetails.setMinimumSize(new java.awt.Dimension(400, 130));
        panDebitActDetails.setOpaque(false);
        panDebitActDetails.setPreferredSize(new java.awt.Dimension(400, 130));
        panDebitActDetails.setLayout(new java.awt.GridBagLayout());

        lblProductTypeDSI.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitActDetails.add(lblProductTypeDSI, gridBagConstraints);

        cboProductTypeDSI.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProductTypeDSI.setPopupWidth(150);
        cboProductTypeDSI.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboProductTypeDSIItemStateChanged(evt);
            }
        });
        cboProductTypeDSI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProductTypeDSIActionPerformed(evt);
            }
        });
        cboProductTypeDSI.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboProductTypeDSIFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 1, 4);
        panDebitActDetails.add(cboProductTypeDSI, gridBagConstraints);

        lblProdIDDSI.setText("Product ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDebitActDetails.add(lblProdIDDSI, gridBagConstraints);

        lblAccNoDSI.setText("Acc Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDebitActDetails.add(lblAccNoDSI, gridBagConstraints);

        panAccNumberNameDSI.setLayout(new java.awt.GridBagLayout());

        txtAccNoDSI.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAccNoDSI.setNextFocusableComponent(txtAmountDSI);
        txtAccNoDSI.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAccNoDSIFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 0);
        panAccNumberNameDSI.add(txtAccNoDSI, gridBagConstraints);

        btnHelpAccNoDSI.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnHelpAccNoDSI.setMinimumSize(new java.awt.Dimension(21, 21));
        btnHelpAccNoDSI.setPreferredSize(new java.awt.Dimension(21, 21));
        btnHelpAccNoDSI.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                btnHelpAccNoDSIItemStateChanged(evt);
            }
        });
        btnHelpAccNoDSI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHelpAccNoDSIActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panAccNumberNameDSI.add(btnHelpAccNoDSI, gridBagConstraints);

        panNameDSI.setLayout(new java.awt.GridBagLayout());

        lblNameValueDSI.setForeground(new java.awt.Color(0, 51, 204));
        lblNameValueDSI.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblNameValueDSI.setMaximumSize(new java.awt.Dimension(150, 16));
        lblNameValueDSI.setMinimumSize(new java.awt.Dimension(150, 16));
        lblNameValueDSI.setPreferredSize(new java.awt.Dimension(150, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panNameDSI.add(lblNameValueDSI, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccNumberNameDSI.add(panNameDSI, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panDebitActDetails.add(panAccNumberNameDSI, gridBagConstraints);

        lblAmountDSI.setText("Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDebitActDetails.add(lblAmountDSI, gridBagConstraints);

        txtAmountDSI.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAmountDSI.setNextFocusableComponent(txtParticularsDSI);
        txtAmountDSI.setValidation(new CurrencyValidation(14,2));
        txtAmountDSI.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtAmountDSIFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAmountDSIFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        panDebitActDetails.add(txtAmountDSI, gridBagConstraints);

        lblParticularsDSI.setText("Particulars");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDebitActDetails.add(lblParticularsDSI, gridBagConstraints);

        txtParticularsDSI.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        panDebitActDetails.add(txtParticularsDSI, gridBagConstraints);

        panProdIdAccHeadDSI.setLayout(new java.awt.GridBagLayout());

        lblAccHeadDSI.setText("Acc Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProdIdAccHeadDSI.add(lblAccHeadDSI, gridBagConstraints);

        panAccHeadDSI.setLayout(new java.awt.GridBagLayout());

        txtAccHeadValueDSI.setEditable(false);
        txtAccHeadValueDSI.setMinimumSize(new java.awt.Dimension(90, 21));
        txtAccHeadValueDSI.setPreferredSize(new java.awt.Dimension(90, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panAccHeadDSI.add(txtAccHeadValueDSI, gridBagConstraints);

        btnAccHeadDSI.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccHeadDSI.setMinimumSize(new java.awt.Dimension(21, 21));
        btnAccHeadDSI.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAccHeadDSI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccHeadDSIActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panAccHeadDSI.add(btnAccHeadDSI, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        panProdIdAccHeadDSI.add(panAccHeadDSI, gridBagConstraints);

        cboProdIDDSI.setEnabled(false);
        cboProdIDDSI.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdIDDSI.setPopupWidth(180);
        cboProdIDDSI.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboProdIDDSIItemStateChanged(evt);
            }
        });
        cboProdIDDSI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdIDDSIActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProdIdAccHeadDSI.add(cboProdIDDSI, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panDebitActDetails.add(panProdIdAccHeadDSI, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panDebitSI.add(panDebitActDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        panMainSI.add(panDebitSI, gridBagConstraints);

        panCreditSI.setBorder(javax.swing.BorderFactory.createTitledBorder("Credit Account Details"));
        panCreditSI.setMinimumSize(new java.awt.Dimension(816, 180));
        panCreditSI.setPreferredSize(new java.awt.Dimension(816, 180));
        panCreditSI.setLayout(new java.awt.GridBagLayout());

        panCreditDetails.setMinimumSize(new java.awt.Dimension(400, 130));
        panCreditDetails.setPreferredSize(new java.awt.Dimension(400, 130));
        panCreditDetails.setLayout(new java.awt.GridBagLayout());

        panCrTotal.setMinimumSize(new java.awt.Dimension(44, 20));
        panCrTotal.setPreferredSize(new java.awt.Dimension(44, 20));
        panCrTotal.setLayout(new java.awt.GridBagLayout());

        lblCreditTotal.setText("Total");
        lblCreditTotal.setMinimumSize(new java.awt.Dimension(45, 18));
        lblCreditTotal.setPreferredSize(new java.awt.Dimension(45, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCrTotal.add(lblCreditTotal, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCrTotal.add(lblCrTotValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.ipadx = 85;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 260, 1, 1);
        panCreditDetails.add(panCrTotal, gridBagConstraints);

        srpCrebitSI.setMinimumSize(new java.awt.Dimension(454, 80));
        srpCrebitSI.setPreferredSize(new java.awt.Dimension(454, 80));

        tblCreditSI.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Product Type", "Product ID", "Account Head", "Account No.", "Amount"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblCreditSI.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblCreditSIMousePressed(evt);
            }
        });
        srpCrebitSI.setViewportView(tblCreditSI);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        panCreditDetails.add(srpCrebitSI, gridBagConstraints);

        panCreditOperations.setMinimumSize(new java.awt.Dimension(217, 29));
        panCreditOperations.setPreferredSize(new java.awt.Dimension(217, 29));
        panCreditOperations.setLayout(new java.awt.GridBagLayout());

        btnNewCSI.setText("New");
        btnNewCSI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewCSIActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCreditOperations.add(btnNewCSI, gridBagConstraints);

        btnSaveCSI.setText("Save");
        btnSaveCSI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveCSIActionPerformed(evt);
            }
        });
        btnSaveCSI.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                btnSaveCSIFocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCreditOperations.add(btnSaveCSI, gridBagConstraints);

        btnDeleteCSI.setText("Delete");
        btnDeleteCSI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteCSIActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCreditOperations.add(btnDeleteCSI, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        panCreditDetails.add(panCreditOperations, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panCreditSI.add(panCreditDetails, gridBagConstraints);

        panCreditActDetails.setMinimumSize(new java.awt.Dimension(400, 141));
        panCreditActDetails.setPreferredSize(new java.awt.Dimension(400, 141));
        panCreditActDetails.setLayout(new java.awt.GridBagLayout());

        lblProductTypeCSI.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCreditActDetails.add(lblProductTypeCSI, gridBagConstraints);

        cboProductTypeCSI.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProductTypeCSI.setPopupWidth(150);
        cboProductTypeCSI.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboProductTypeCSIItemStateChanged(evt);
            }
        });
        cboProductTypeCSI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProductTypeCSIActionPerformed(evt);
            }
        });
        cboProductTypeCSI.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboProductTypeCSIFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCreditActDetails.add(cboProductTypeCSI, gridBagConstraints);

        lblProdIDCSI.setText("Product ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCreditActDetails.add(lblProdIDCSI, gridBagConstraints);

        lblAccNoCSI.setText("Acc Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCreditActDetails.add(lblAccNoCSI, gridBagConstraints);

        panAccNumberCSI.setLayout(new java.awt.GridBagLayout());

        txtAccNoCSI.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAccNoCSI.setNextFocusableComponent(txtAmountCSI);
        txtAccNoCSI.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAccNoCSIFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 0);
        panAccNumberCSI.add(txtAccNoCSI, gridBagConstraints);

        btnHelpAccNoCSI.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnHelpAccNoCSI.setMinimumSize(new java.awt.Dimension(21, 21));
        btnHelpAccNoCSI.setPreferredSize(new java.awt.Dimension(21, 21));
        btnHelpAccNoCSI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHelpAccNoCSIActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panAccNumberCSI.add(btnHelpAccNoCSI, gridBagConstraints);

        panNameCSI.setLayout(new java.awt.GridBagLayout());

        lblNameValueCSI.setForeground(new java.awt.Color(0, 51, 204));
        lblNameValueCSI.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblNameValueCSI.setMaximumSize(new java.awt.Dimension(150, 16));
        lblNameValueCSI.setMinimumSize(new java.awt.Dimension(150, 16));
        lblNameValueCSI.setPreferredSize(new java.awt.Dimension(150, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panNameCSI.add(lblNameValueCSI, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccNumberCSI.add(panNameCSI, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panCreditActDetails.add(panAccNumberCSI, gridBagConstraints);

        lblAmountCSI.setText("Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCreditActDetails.add(lblAmountCSI, gridBagConstraints);

        txtAmountCSI.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAmountCSI.setNextFocusableComponent(txtParticularsCSI);
        txtAmountCSI.setValidation(new CurrencyValidation(14,2));
        txtAmountCSI.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtAmountCSIFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAmountCSIFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCreditActDetails.add(txtAmountCSI, gridBagConstraints);

        lblParticularsCSI.setText("Particulars");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCreditActDetails.add(lblParticularsCSI, gridBagConstraints);

        txtParticularsCSI.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCreditActDetails.add(txtParticularsCSI, gridBagConstraints);

        panProdIdAccHeadCSI.setLayout(new java.awt.GridBagLayout());

        lblAccHeadCSI.setText("Acc Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panProdIdAccHeadCSI.add(lblAccHeadCSI, gridBagConstraints);

        panAccHead.setLayout(new java.awt.GridBagLayout());

        txtAccHeadValueCSI.setEditable(false);
        txtAccHeadValueCSI.setMinimumSize(new java.awt.Dimension(90, 21));
        txtAccHeadValueCSI.setPreferredSize(new java.awt.Dimension(90, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panAccHead.add(txtAccHeadValueCSI, gridBagConstraints);

        btnAccHeadCSI.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccHeadCSI.setMinimumSize(new java.awt.Dimension(21, 21));
        btnAccHeadCSI.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAccHeadCSI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccHeadCSIActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        panAccHead.add(btnAccHeadCSI, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        panProdIdAccHeadCSI.add(panAccHead, gridBagConstraints);

        cboProdIDCSI.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdIDCSI.setPopupWidth(180);
        cboProdIDCSI.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboProdIDCSIItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProdIdAccHeadCSI.add(cboProdIDCSI, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        panCreditActDetails.add(panProdIdAccHeadCSI, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panCreditSI.add(panCreditActDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        panMainSI.add(panCreditSI, gridBagConstraints);

        tabSIDetails.setMinimumSize(new java.awt.Dimension(814, 181));
        tabSIDetails.setPreferredSize(new java.awt.Dimension(814, 181));

        panSchedule.setMinimumSize(new java.awt.Dimension(809, 119));
        panSchedule.setPreferredSize(new java.awt.Dimension(809, 119));
        panSchedule.setLayout(new java.awt.GridBagLayout());

        lblFrequencySI.setText("Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panSchedule.add(lblFrequencySI, gridBagConstraints);

        cboFrequencySI.setMinimumSize(new java.awt.Dimension(100, 21));
        cboFrequencySI.setNextFocusableComponent(cboWeekDay);
        cboFrequencySI.setEnabled(false);
        cboFrequencySI.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboFrequencySIItemStateChanged(evt);
            }
        });
        cboFrequencySI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboFrequencySIActionPerformed(evt);
            }
        });
        cboFrequencySI.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cboFrequencySIFocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panSchedule.add(cboFrequencySI, gridBagConstraints);

        lblWeekDay.setText("WeekDay");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panSchedule.add(lblWeekDay, gridBagConstraints);

        cboWeekDay.setMinimumSize(new java.awt.Dimension(100, 21));
        cboWeekDay.setNextFocusableComponent(cboWeek);
        cboWeekDay.setEnabled(false);
        cboWeekDay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboWeekDayActionPerformed(evt);
            }
        });
        cboWeekDay.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboWeekDayItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panSchedule.add(cboWeekDay, gridBagConstraints);

        lblWeek.setText("Week");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panSchedule.add(lblWeek, gridBagConstraints);

        cboWeek.setMinimumSize(new java.awt.Dimension(100, 21));
        cboWeek.setNextFocusableComponent(cboSpecificDate);
        cboWeek.setEnabled(false);
        cboWeek.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboWeekActionPerformed(evt);
            }
        });
        cboWeek.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboWeekItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panSchedule.add(cboWeek, gridBagConstraints);

        lblSpecificDate.setText("Specific Dt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panSchedule.add(lblSpecificDate, gridBagConstraints);

        cboSpecificDate.setMinimumSize(new java.awt.Dimension(100, 21));
        cboSpecificDate.setNextFocusableComponent(txtGraceDaysSI);
        cboSpecificDate.setEnabled(false);
        cboSpecificDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboSpecificDateActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panSchedule.add(cboSpecificDate, gridBagConstraints);

        lblGraceDaysSI.setText("GraceDays");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panSchedule.add(lblGraceDaysSI, gridBagConstraints);

        txtGraceDaysSI.setMinimumSize(new java.awt.Dimension(100, 21));
        txtGraceDaysSI.setNextFocusableComponent(rdoHolidayExecution_Yes);
        txtGraceDaysSI.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panSchedule.add(txtGraceDaysSI, gridBagConstraints);

        lblSIHoliday.setText("Holiday Exec");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 2);
        panSchedule.add(lblSIHoliday, gridBagConstraints);

        panHolidayExecution.setNextFocusableComponent(panAutoPosting);
        panHolidayExecution.setLayout(new java.awt.GridBagLayout());

        rdoSIHolidayExecution.add(rdoHolidayExecution_Yes);
        rdoHolidayExecution_Yes.setText("Yes");
        rdoHolidayExecution_Yes.setMaximumSize(new java.awt.Dimension(49, 21));
        rdoHolidayExecution_Yes.setMinimumSize(new java.awt.Dimension(49, 21));
        rdoHolidayExecution_Yes.setNextFocusableComponent(rdoHolidayExecution_No);
        rdoHolidayExecution_Yes.setPreferredSize(new java.awt.Dimension(49, 21));
        rdoHolidayExecution_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoHolidayExecution_YesActionPerformed(evt);
            }
        });
        panHolidayExecution.add(rdoHolidayExecution_Yes, new java.awt.GridBagConstraints());

        rdoSIHolidayExecution.add(rdoHolidayExecution_No);
        rdoHolidayExecution_No.setText("No");
        rdoHolidayExecution_No.setMaximumSize(new java.awt.Dimension(49, 21));
        rdoHolidayExecution_No.setMinimumSize(new java.awt.Dimension(49, 21));
        rdoHolidayExecution_No.setNextFocusableComponent(rdoSIAutoPosting_Yes);
        rdoHolidayExecution_No.setPreferredSize(new java.awt.Dimension(49, 21));
        rdoHolidayExecution_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoHolidayExecution_NoActionPerformed(evt);
            }
        });
        panHolidayExecution.add(rdoHolidayExecution_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panSchedule.add(panHolidayExecution, gridBagConstraints);

        lblAutomaticPosting.setText("Auto Posting");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 2);
        panSchedule.add(lblAutomaticPosting, gridBagConstraints);

        panAutoPosting.setLayout(new java.awt.GridBagLayout());

        rdoSIAutoForward.add(rdoSIAutoPosting_Yes);
        rdoSIAutoPosting_Yes.setText("Yes");
        rdoSIAutoPosting_Yes.setMaximumSize(new java.awt.Dimension(49, 21));
        rdoSIAutoPosting_Yes.setMinimumSize(new java.awt.Dimension(49, 21));
        rdoSIAutoPosting_Yes.setNextFocusableComponent(rdoSIAutoPosting_No);
        rdoSIAutoPosting_Yes.setPreferredSize(new java.awt.Dimension(49, 21));
        panAutoPosting.add(rdoSIAutoPosting_Yes, new java.awt.GridBagConstraints());

        rdoSIAutoForward.add(rdoSIAutoPosting_No);
        rdoSIAutoPosting_No.setText("No");
        rdoSIAutoPosting_No.setMaximumSize(new java.awt.Dimension(49, 21));
        rdoSIAutoPosting_No.setMinimumSize(new java.awt.Dimension(49, 21));
        rdoSIAutoPosting_No.setNextFocusableComponent(txtForwardCount);
        rdoSIAutoPosting_No.setPreferredSize(new java.awt.Dimension(49, 21));
        panAutoPosting.add(rdoSIAutoPosting_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panSchedule.add(panAutoPosting, gridBagConstraints);

        lblForwardCount.setText("      Fwd Count");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 2);
        panSchedule.add(lblForwardCount, gridBagConstraints);

        txtForwardCount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtForwardCount.setNextFocusableComponent(txtMultiplieSI);
        txtForwardCount.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panSchedule.add(txtForwardCount, gridBagConstraints);

        lblMultiplieSI.setText("Multipiles  of");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 2);
        panSchedule.add(lblMultiplieSI, gridBagConstraints);

        txtMultiplieSI.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMultiplieSI.setNextFocusableComponent(txtMinBalSI);
        txtMultiplieSI.setValidation(new CurrencyValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panSchedule.add(txtMultiplieSI, gridBagConstraints);

        lblMinBalSI.setText("Minimum Balance");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 2);
        panSchedule.add(lblMinBalSI, gridBagConstraints);

        txtMinBalSI.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMinBalSI.setNextFocusableComponent(txtBeneficiarySI);
        txtMinBalSI.setValidation(new CurrencyValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 4);
        panSchedule.add(txtMinBalSI, gridBagConstraints);

        lblBeneficiarySI.setText("Beneficiary");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 2);
        panSchedule.add(lblBeneficiarySI, gridBagConstraints);

        txtBeneficiarySI.setMinimumSize(new java.awt.Dimension(100, 21));
        txtBeneficiarySI.setNextFocusableComponent(cboMoRSI);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 4);
        panSchedule.add(txtBeneficiarySI, gridBagConstraints);

        lblMoTSI.setText("Transmission Mode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 2);
        panSchedule.add(lblMoTSI, gridBagConstraints);

        cboMoRSI.setMinimumSize(new java.awt.Dimension(100, 21));
        cboMoRSI.setNextFocusableComponent(dtdStartDtSI);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 4);
        panSchedule.add(cboMoRSI, gridBagConstraints);

        dtdStartDtSI.setNextFocusableComponent(dtdEndDtSI);
        dtdStartDtSI.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                dtdStartDtSIFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 4);
        panSchedule.add(dtdStartDtSI, gridBagConstraints);

        lblStartDtSI.setText("Start Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 10, 1, 2);
        panSchedule.add(lblStartDtSI, gridBagConstraints);

        lblEndDtSI.setText("End Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 2);
        panSchedule.add(lblEndDtSI, gridBagConstraints);

        dtdEndDtSI.setNextFocusableComponent(cboExecConfig);
        dtdEndDtSI.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                dtdEndDtSIFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 4);
        panSchedule.add(dtdEndDtSI, gridBagConstraints);

        lblExecutionDay.setText("Execution Day");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 2);
        panSchedule.add(lblExecutionDay, gridBagConstraints);

        cboExecutionDay.setMinimumSize(new java.awt.Dimension(100, 21));
        cboExecutionDay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboExecutionDayActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panSchedule.add(cboExecutionDay, gridBagConstraints);

        lblLastExecutionDate.setText("Last Exec Dt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 2);
        panSchedule.add(lblLastExecutionDate, gridBagConstraints);

        lbl1LastExecutionDate.setMaximumSize(new java.awt.Dimension(100, 16));
        lbl1LastExecutionDate.setMinimumSize(new java.awt.Dimension(100, 16));
        lbl1LastExecutionDate.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panSchedule.add(lbl1LastExecutionDate, gridBagConstraints);

        lblNextExecutionDate.setText("Next Exec Dt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 2);
        panSchedule.add(lblNextExecutionDate, gridBagConstraints);

        lbl1NextExecutionDate.setMaximumSize(new java.awt.Dimension(100, 16));
        lbl1NextExecutionDate.setMinimumSize(new java.awt.Dimension(100, 16));
        lbl1NextExecutionDate.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panSchedule.add(lbl1NextExecutionDate, gridBagConstraints);

        chkSuspendUser.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        chkSuspendUser.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        chkSuspendUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSuspendUserActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 4);
        panSchedule.add(chkSuspendUser, gridBagConstraints);

        lblSuspendUser.setText("Suspend SI");
        lblSuspendUser.setMinimumSize(new java.awt.Dimension(88, 18));
        lblSuspendUser.setPreferredSize(new java.awt.Dimension(88, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 2);
        panSchedule.add(lblSuspendUser, gridBagConstraints);

        lblHolidayExecutionDate.setText("Hol Exec Dt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 2);
        panSchedule.add(lblHolidayExecutionDate, gridBagConstraints);

        lbl1HolidayExecutionDate.setMaximumSize(new java.awt.Dimension(100, 16));
        lbl1HolidayExecutionDate.setMinimumSize(new java.awt.Dimension(100, 16));
        lbl1HolidayExecutionDate.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panSchedule.add(lbl1HolidayExecutionDate, gridBagConstraints);

        lblFwdExecutionDate.setText("Fwd Exec Dt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 2);
        panSchedule.add(lblFwdExecutionDate, gridBagConstraints);

        lbl1FwdExecutionDate.setMaximumSize(new java.awt.Dimension(100, 16));
        lbl1FwdExecutionDate.setMinimumSize(new java.awt.Dimension(100, 16));
        lbl1FwdExecutionDate.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panSchedule.add(lbl1FwdExecutionDate, gridBagConstraints);

        dtdSuspendDt.setNextFocusableComponent(cboExecConfig);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panSchedule.add(dtdSuspendDt, gridBagConstraints);

        lblSuspDate.setText("Suspend Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 2);
        panSchedule.add(lblSuspDate, gridBagConstraints);

        chkCloseSI.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        chkCloseSI.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        chkCloseSI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkCloseSIActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 4);
        panSchedule.add(chkCloseSI, gridBagConstraints);

        lblCloseSI.setText("Close SI");
        lblCloseSI.setMinimumSize(new java.awt.Dimension(88, 18));
        lblCloseSI.setPreferredSize(new java.awt.Dimension(88, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 2);
        panSchedule.add(lblCloseSI, gridBagConstraints);

        tabSIDetails.addTab("StandingInstruction Schedule", panSchedule);

        panCharges.setMinimumSize(new java.awt.Dimension(517, 150));
        panCharges.setNextFocusableComponent(rdoCommSI_Yes);
        panCharges.setPreferredSize(new java.awt.Dimension(517, 150));
        panCharges.setLayout(new java.awt.GridBagLayout());

        lblCommSI.setText("SI Commission");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panCharges.add(lblCommSI, gridBagConstraints);

        panCommSI.setNextFocusableComponent(txtCommChargesSI);
        panCommSI.setLayout(new java.awt.GridBagLayout());

        rdoCommSI.add(rdoCommSI_Yes);
        rdoCommSI_Yes.setText("Yes");
        rdoCommSI_Yes.setMaximumSize(new java.awt.Dimension(49, 21));
        rdoCommSI_Yes.setMinimumSize(new java.awt.Dimension(49, 21));
        rdoCommSI_Yes.setNextFocusableComponent(rdoCommSI_No);
        rdoCommSI_Yes.setPreferredSize(new java.awt.Dimension(49, 21));
        rdoCommSI_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoCommSI_YesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panCommSI.add(rdoCommSI_Yes, gridBagConstraints);

        rdoCommSI.add(rdoCommSI_No);
        rdoCommSI_No.setText("No");
        rdoCommSI_No.setMaximumSize(new java.awt.Dimension(49, 21));
        rdoCommSI_No.setMinimumSize(new java.awt.Dimension(49, 21));
        rdoCommSI_No.setNextFocusableComponent(txtCommChargesSI);
        rdoCommSI_No.setPreferredSize(new java.awt.Dimension(49, 21));
        rdoCommSI_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoCommSI_NoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panCommSI.add(rdoCommSI_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panCharges.add(panCommSI, gridBagConstraints);

        lblCommChargesSI.setText("SI Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panCharges.add(lblCommChargesSI, gridBagConstraints);

        txtCommChargesSI.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCommChargesSI.setNextFocusableComponent(rdoRettCommSI_Yes);
        txtCommChargesSI.setValidation(new NumericValidation());
        txtCommChargesSI.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCommChargesSIFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panCharges.add(txtCommChargesSI, gridBagConstraints);

        lblRettCommSI.setText("Remittance Comission");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panCharges.add(lblRettCommSI, gridBagConstraints);

        panRettCommSI.setNextFocusableComponent(txtRettCommChargesSI);
        panRettCommSI.setLayout(new java.awt.GridBagLayout());

        rdoRettCommSI.add(rdoRettCommSI_Yes);
        rdoRettCommSI_Yes.setText("Yes");
        rdoRettCommSI_Yes.setMaximumSize(new java.awt.Dimension(49, 21));
        rdoRettCommSI_Yes.setMinimumSize(new java.awt.Dimension(49, 21));
        rdoRettCommSI_Yes.setNextFocusableComponent(rdoRettCommSI_No);
        rdoRettCommSI_Yes.setPreferredSize(new java.awt.Dimension(49, 21));
        rdoRettCommSI_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoRettCommSI_YesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panRettCommSI.add(rdoRettCommSI_Yes, gridBagConstraints);

        rdoRettCommSI.add(rdoRettCommSI_No);
        rdoRettCommSI_No.setText("No");
        rdoRettCommSI_No.setMaximumSize(new java.awt.Dimension(49, 21));
        rdoRettCommSI_No.setMinimumSize(new java.awt.Dimension(49, 21));
        rdoRettCommSI_No.setNextFocusableComponent(txtRettCommChargesSI);
        rdoRettCommSI_No.setPreferredSize(new java.awt.Dimension(49, 21));
        rdoRettCommSI_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoRettCommSI_NoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panRettCommSI.add(rdoRettCommSI_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panCharges.add(panRettCommSI, gridBagConstraints);

        lblRettCommChargesSI.setText("Remittance Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panCharges.add(lblRettCommChargesSI, gridBagConstraints);

        txtRettCommChargesSI.setMinimumSize(new java.awt.Dimension(100, 21));
        txtRettCommChargesSI.setNextFocusableComponent(txtAcceptanceCharges);
        txtRettCommChargesSI.setValidation(new CurrencyValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panCharges.add(txtRettCommChargesSI, gridBagConstraints);

        lblAcceptanceCharges.setText("Postage Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panCharges.add(lblAcceptanceCharges, gridBagConstraints);

        txtAcceptanceCharges.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAcceptanceCharges.setNextFocusableComponent(txtFailureCharges);
        txtAcceptanceCharges.setValidation(new CurrencyValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panCharges.add(txtAcceptanceCharges, gridBagConstraints);

        lblExecutionCharges.setText("SI Charges Service Tax");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panCharges.add(lblExecutionCharges, gridBagConstraints);

        txtExecutionCharges.setMinimumSize(new java.awt.Dimension(100, 21));
        txtExecutionCharges.setValidation(new CurrencyValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panCharges.add(txtExecutionCharges, gridBagConstraints);

        lblFailureCharges.setText("Failure Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panCharges.add(lblFailureCharges, gridBagConstraints);

        txtFailureCharges.setMinimumSize(new java.awt.Dimension(100, 21));
        txtFailureCharges.setNextFocusableComponent(txtExecutionCharges);
        txtFailureCharges.setValidation(new CurrencyValidation());
        txtFailureCharges.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFailureChargesFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panCharges.add(txtFailureCharges, gridBagConstraints);

        lblExecConfig.setText("Execution Configuration");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panCharges.add(lblExecConfig, gridBagConstraints);

        cboExecConfig.setMinimumSize(new java.awt.Dimension(100, 21));
        cboExecConfig.setNextFocusableComponent(panCharges);
        cboExecConfig.setEnabled(false);
        cboExecConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboExecConfigActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panCharges.add(cboExecConfig, gridBagConstraints);

        txtServiceTax.setMinimumSize(new java.awt.Dimension(100, 21));
        txtServiceTax.setValidation(new CurrencyValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panCharges.add(txtServiceTax, gridBagConstraints);

        lblServiceTax.setText("Remittance Service Tax");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panCharges.add(lblServiceTax, gridBagConstraints);

        txtFailureST.setMinimumSize(new java.awt.Dimension(100, 21));
        txtFailureST.setValidation(new CurrencyValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panCharges.add(txtFailureST, gridBagConstraints);

        lblFailureST.setText("Failure Charges Service Tax");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panCharges.add(lblFailureST, gridBagConstraints);

        tabSIDetails.addTab("StandingInstruction Charges", panCharges);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panMainSI.add(tabSIDetails, gridBagConstraints);

        lblInstalments.setText("Instalment Wise (Y/N)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 23, 0, 0);
        panMainSI.add(lblInstalments, gridBagConstraints);

        chkinstalments.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkinstalmentsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panMainSI.add(chkinstalments, gridBagConstraints);

        lblNoofInstalments.setText("No of Instalments");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 33, 0, 0);
        panMainSI.add(lblNoofInstalments, gridBagConstraints);

        txtNoofInstalments.setAllowNumber(true);
        txtNoofInstalments.setMinimumSize(new java.awt.Dimension(45, 24));
        txtNoofInstalments.setPreferredSize(new java.awt.Dimension(45, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panMainSI.add(txtNoofInstalments, gridBagConstraints);

        chkPendingInstalment.setText("Pending Instalment wise");
        chkPendingInstalment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkPendingInstalmentActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 11, 0, 0);
        panMainSI.add(chkPendingInstalment, gridBagConstraints);

        tabDCDayBegin.addTab("Standing Instruction Lodgement", panMainSI);

        panBatchProcess1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panBatchProcess1.setMinimumSize(new java.awt.Dimension(740, 624));
        panBatchProcess1.setName(""); // NOI18N
        panBatchProcess1.setPreferredSize(new java.awt.Dimension(740, 624));
        panBatchProcess1.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBatchProcess1.add(sptSpace2, gridBagConstraints);

        panCheckBoxes1.setMinimumSize(new java.awt.Dimension(536, 120));
        panCheckBoxes1.setPreferredSize(new java.awt.Dimension(536, 300));
        panCheckBoxes1.setLayout(new java.awt.GridBagLayout());

        panMultiSearch.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panMultiSearch.setMinimumSize(new java.awt.Dimension(700, 100));
        panMultiSearch.setPreferredSize(new java.awt.Dimension(700, 160));
        panMultiSearch.setLayout(new java.awt.GridBagLayout());

        btnSearchSI.setText("Search");
        btnSearchSI.setMaximumSize(new java.awt.Dimension(75, 23));
        btnSearchSI.setMinimumSize(new java.awt.Dimension(75, 23));
        btnSearchSI.setPreferredSize(new java.awt.Dimension(75, 23));
        btnSearchSI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchSIActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panMultiSearch.add(btnSearchSI, gridBagConstraints);

        lblExecDateProc.setText("Execution Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMultiSearch.add(lblExecDateProc, gridBagConstraints);

        dtdExecDateProc.setNextFocusableComponent(dtdEndDtSI);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 4);
        panMultiSearch.add(dtdExecDateProc, gridBagConstraints);

        chkSelectAll1.setText("Select All");
        chkSelectAll1.setMaximumSize(new java.awt.Dimension(81, 23));
        chkSelectAll1.setPreferredSize(new java.awt.Dimension(81, 23));
        chkSelectAll1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSelectAll1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panMultiSearch.add(chkSelectAll1, gridBagConstraints);

        btnSearchSI1.setText("Process");
        btnSearchSI1.setMaximumSize(new java.awt.Dimension(95, 23));
        btnSearchSI1.setMinimumSize(new java.awt.Dimension(95, 23));
        btnSearchSI1.setPreferredSize(new java.awt.Dimension(95, 23));
        btnSearchSI1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchSI1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panMultiSearch.add(btnSearchSI1, gridBagConstraints);

        panCheckBoxes1.add(panMultiSearch, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panBatchProcess1.add(panCheckBoxes1, gridBagConstraints);

        scrOutputMsg1.setMinimumSize(new java.awt.Dimension(200, 200));
        scrOutputMsg1.setPreferredSize(new java.awt.Dimension(200, 200));

        tblData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        scrOutputMsg1.setViewportView(tblData);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panBatchProcess1.add(scrOutputMsg1, gridBagConstraints);

        tabDCDayBegin.addTab("Standing Instruction Execution", panBatchProcess1);

        getContentPane().add(tabDCDayBegin, java.awt.BorderLayout.CENTER);

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
    }// </editor-fold>//GEN-END:initComponents

    private void chkCloseSIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkCloseSIActionPerformed
        // TODO add your handling code here:
        int a = ClientUtil.confirmationAlert("Do You Want To Close The Standing Instruction?");
        int b = 0;
        if (b == a) {
            observable.setClose("Y");
            chkSuspendUser.setSelected(false);
            dtdSuspendDt.setEnabled(false);
            dtdSuspendDt.setDateValue("");
        } else {
            observable.setClose("N");
            this.chkCloseSI.setSelected(false);
        }

    }//GEN-LAST:event_chkCloseSIActionPerformed

    private void cboProductTypeDSIItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboProductTypeDSIItemStateChanged
        // TODO add your handling code here:
        cboProductTypeDSIItemStateChanged();

    }//GEN-LAST:event_cboProductTypeDSIItemStateChanged
    private void cboProductTypeDSIItemStateChanged() {
        //rishad
        String prodType = ((ComboBoxModel) cboProductTypeDSI.getModel()).getKeyForSelected().toString();
        if ((!prodType.equals("")) && (prodType != null)) {
            if ((prodType.equals("OA")) || (prodType.equals("AD")) || (prodType.equals("GL")) || (prodType.equals("SA"))||(prodType.equals("TD"))) {
                //do nothing
            } else {
                ClientUtil.showAlertWindow("ProdType can only be OA,GL,SA or AD,TD");
                cboProductTypeDSI.setSelectedItem("");
                observable.setComboDBProdTypes();
            }
        }
    }
    private void cboProductTypeCSIFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboProductTypeCSIFocusLost
        // TODO add your handling code here:
//        String prodType=((ComboBoxModel)cboProductTypeCSI.getModel()).getKeyForSelected().toString();
//                if((prodType.equals("OA")) || (prodType.equals("AD")) || (prodType.equals("GL")) || (prodType.equals("RM")) || (prodType.equals("TD")) || (prodType.equals(""))){
//                    //do nothing
//                }else{
//                    ClientUtil.showAlertWindow("ProdType can only be OA,GL,RM,Deposits or AD");
//                    cboProductTypeCSI.setSelectedItem("");
//                }
    }//GEN-LAST:event_cboProductTypeCSIFocusLost

    private void cboProductTypeDSIFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboProductTypeDSIFocusLost
        // TODO add your handling code here:
//        String prodType=((ComboBoxModel)cboProductTypeDSI.getModel()).getKeyForSelected().toString();
//                if((prodType.equals("OA")) || (prodType.equals("AD")) || (prodType.equals("GL")) || (prodType.equals(""))){
//                    //do nothing
//                }else{
//                    ClientUtil.showAlertWindow("ProdType can only be OA,GL or AD");
//                    cboProductTypeDSI.setSelectedItem("");
//                }
    }//GEN-LAST:event_cboProductTypeDSIFocusLost

    private void chkSelectAll1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSelectAll1ActionPerformed
        // TODO add your handling code here:
        observable.setSelectAll(new Boolean(chkSelectAll1.isSelected()));
    }//GEN-LAST:event_chkSelectAll1ActionPerformed

    private void btnSearchSIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchSIActionPerformed
        // TODO add your handling code here:
        tblData.setEnabled(true);
        populateData();
    }//GEN-LAST:event_btnSearchSIActionPerformed

    private void btnSearchSI1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchSI1ActionPerformed
        // TODO add your handling code here:
//        EnhancedTableModel td = (EnhancedTableModel) tblData.getModel();
//        ArrayList arrlist = td.getDataArrayList();
//        if(tblData.getSelectedRow() >= 0){
//            for(int i=0; i< tblData.getRowCount(); i++) {
//                String siID = CommonUtil.convertObjToStr(tblData.getValueAt(i,1));
//            }
//        }
//         if(isCheck){
        int rowscnt = tblData.getRowCount();
        ArrayList branAryLst = new ArrayList();
//                            List branLst = null;
        HashMap newBranMap = new HashMap();
        if (rowscnt > 0) {
            for (int i = 0; i < rowscnt; i++) {
                String isSelected = CommonUtil.convertObjToStr(tblData.getValueAt(i, 0));
                if (isSelected.equalsIgnoreCase("TRUE")) {
                    newBranMap = new HashMap();
//                                    newBranMap.put("BRANCH_CODE", CommonUtil.convertObjToStr(tblData.getValueAt(i,2)));
                    newBranMap.put("SI_ID", CommonUtil.convertObjToStr(tblData.getValueAt(i, 1)));
                    branAryLst.add(newBranMap);
//                                    branMap.put("BRANCH_LST", branAryLst);
//                                    dayEndPrev = "DAY_END_PREV";
//                                    isCheck = false;
                }
            }
            observable.doExecute(branAryLst);
//                            tblData.clearSelection();
        }
        //System.out.println("643fff");
        
//        if (rowscnt > 0) {
//            for (int i = 0; i < rowscnt; i++) {
//                String isSelected = CommonUtil.convertObjToStr(tblData.getValueAt(i, 0));
//                System.out.println("isSelected??>>##>>>"+isSelected);
//                if (isSelected.equalsIgnoreCase("TRUE")) {
//                    System.out.println("nmmnmm i>>>"+i);
//                tblData.remove(i);
//              
//                }
//            }
//        }
        populateData();
//                            }
    }//GEN-LAST:event_btnSearchSI1ActionPerformed
    public void populateData() {
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        viewMap.put(CommonConstants.MAP_NAME, "getSiForReExecution");
        whereMap.put("LAST_RUN_DT", dtdExecDateProc.getDateValue());
        //System.out.println("#### where map : " + whereMap);
        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        try {
            ArrayList heading = observable.populateData(viewMap, tblData);
        } catch (Exception e) {
            //System.err.println("Exception " + e.toString() + "Caught");
            e.printStackTrace();
        }
    }
    private void txtAccNoCSIFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccNoCSIFocusLost
        // TODO add your handling code here:
        if (!(observable.getCbmProductTypeCSI().getKeyForSelected().equals("RM"))) {
            observable.setCboProductTypeCSI("");
            observable.debitAcNo = false;
            observable.creditAcNo = false;
            String ACCOUNTNO = txtAccNoCSI.getText();
            observable.debitAcNo = false;
            observable.creditAcNo = false;
            if ((!(observable.getCboProductTypeCSI().length() > 0)) && ACCOUNTNO.length() > 0) {
                observable.creditAcNo = true;
                if (observable.checkAcNoWithoutProdType(ACCOUNTNO)) {
                    txtAccNoCSI.setText(observable.getTxtAccNoCSI());
                    cboProdIDCSI.setModel(observable.getCbmProdIDCSI());
//                txtAccNoCSI.setText(String.valueOf(ACCOUNTNO));
                    String prodType = ((ComboBoxModel) cboProductTypeCSI.getModel()).getKeyForSelected().toString();
                    lblNameValueCSI.setText(observable.getLblNameValueCSI());
                } else {
                    ClientUtil.showAlertWindow("Invalid Account No.");
                    txtAccNoCSI.setText("");
                    return;
                }
            }
            if (txtAccNoCSI.getText().length() > 0) {
                viewType = "CIAccountNo";
                HashMap fillMap = new HashMap();
                prodType = CommonUtil.convertObjToStr(observable.getCbmProductTypeCSI().getKeyForSelected());
                fillMap.put("ACCOUNTNO", txtAccNoCSI.getText());
                fillData(fillMap);
                txtAccNoCSI.setEnabled(true);
                txtAccNoCSI.setEditable(true);
            }
        }
    }//GEN-LAST:event_txtAccNoCSIFocusLost

    private void txtFailureChargesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFailureChargesFocusLost
        // TODO add your handling code here:
        if (txtFailureCharges.getText().length() != 0) {
            double exgST = observable.calServiceTaxForSiCharges(txtFailureCharges.getText());
            observable.setTxtFailureST(String.valueOf(exgST));
            txtFailureST.setText(observable.getTxtFailureST());
        }
    }//GEN-LAST:event_txtFailureChargesFocusLost

    private void txtCommChargesSIFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCommChargesSIFocusLost
        // TODO add your handling code here:
        if (txtCommChargesSI.getText().length() != 0) {
            double exgST = observable.calServiceTaxForSiCharges(txtCommChargesSI.getText());
            observable.setTxtExecutionCharges(String.valueOf(exgST));
            txtExecutionCharges.setText(observable.getTxtExecutionCharges());
        }
    }//GEN-LAST:event_txtCommChargesSIFocusLost

    private void cboProductTypeCSIItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboProductTypeCSIItemStateChanged
        // TODO add your handling code here:
        txtAccHeadValueCSI.setText("");
        txtAccNoCSI.setText("");
        txtAmountCSI.setText("");
        txtParticularsCSI.setText("");
        txtRettCommChargesSI.setText("");
        txtAcceptanceCharges.setText("");
        lblNameValueCSI.setText("");
        rdoRettCommSI_No.setSelected(true);
        cboProductTypeCSIItemStateChanged();
    }//GEN-LAST:event_cboProductTypeCSIItemStateChanged
    private void cboProductTypeCSIItemStateChanged() {
        String prodType = ((ComboBoxModel) cboProductTypeCSI.getModel()).getKeyForSelected().toString();
        if ((!prodType.equals("")) && (prodType != null)) {
            if ((prodType.equals("OA")) || (prodType.equals("AD")) || (prodType.equals("GL")) || (prodType.equals("RM")) || (prodType.equals("TD")) || (prodType.equals("TL")) || (prodType.equals("MDS"))) {
                //do nothing
            } else {
                ClientUtil.showAlertWindow("ProdType can only be OA,GL,MDS,RM,TL,Deposits or AD");
                cboProductTypeCSI.setSelectedItem("");
                observable.setComboCDProdTypes();
            }
            if (prodType.equals("MDS")) {
                lblAccNoCSI.setText(resourceBundle.getString("lblAccNoCSIMDS"));
            } else {
                lblAccNoCSI.setText(resourceBundle.getString("lblAccNoCSI"));
            }
        }
    }
    private void dtdStartDtSIFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_dtdStartDtSIFocusLost
        // TODO add your handling code here:
        String curDate = ClientUtil.getCurrentDateinDDMMYYYY();
        ClientUtil.validateToDate(dtdStartDtSI, curDate);
    }//GEN-LAST:event_dtdStartDtSIFocusLost

    private void cboWeekItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboWeekItemStateChanged
        // TODO add your handling code here:
        if ((!cboWeek.getSelectedItem().equals("")) || (!cboWeekDay.getSelectedItem().equals(""))) {
            cboFrequencySI.setEnabled(false);
            cboFrequencySI.setSelectedItem("");
        } else {
            cboFrequencySI.setEnabled(true);
        }
    }//GEN-LAST:event_cboWeekItemStateChanged

    private void cboWeekDayItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboWeekDayItemStateChanged
        // TODO add your handling code here:
        if ((!cboWeekDay.getSelectedItem().equals("")) || (!cboWeek.getSelectedItem().equals(""))) {
            cboFrequencySI.setEnabled(false);
            cboFrequencySI.setSelectedItem("");
        } else {
            cboFrequencySI.setEnabled(true);
        }
    }//GEN-LAST:event_cboWeekDayItemStateChanged

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        callView("Enquiry");
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed

    private void chkSuspendUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSuspendUserActionPerformed
        // TODO add your handling code here:
        int a = ClientUtil.confirmationAlert("Do You Want To Suspend The Standing Instruction?");
        int b = 0;
        if (b == a) {
            observable.setSuspended("Y");
            chkCloseSI.setSelected(false);
            dtdSuspendDt.setEnabled(true);
        } else {
            observable.setSuspended("N");
            this.chkSuspendUser.setSelected(false);
        }

    }//GEN-LAST:event_chkSuspendUserActionPerformed

    private void cboWeekActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboWeekActionPerformed
        cboWeek.setEnabled(true);
        cboWeekDay.setEnabled(true);
        cboSpecificDate.setEnabled(false);
    }//GEN-LAST:event_cboWeekActionPerformed

    private void cboWeekDayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboWeekDayActionPerformed
        cboWeek.setEnabled(true);
        cboWeekDay.setEnabled(true);
        cboSpecificDate.setEnabled(false);
        // TODO add your handling code here:
    }//GEN-LAST:event_cboWeekDayActionPerformed

    private void cboSpecificDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboSpecificDateActionPerformed
        cboSpecificDate.setEnabled(true);
        cboWeek.setEnabled(false);
        cboWeekDay.setEnabled(false);
    }//GEN-LAST:event_cboSpecificDateActionPerformed

    private void cboExecConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboExecConfigActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboExecConfigActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
        HashMap reportParamMap = new HashMap();
        com.see.truetransact.clientutil.ttrintegration.LinkReport.getReports(getScreenID(), reportParamMap);
    }//GEN-LAST:event_btnPrintActionPerformed

    private void txtAmountCSIFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAmountCSIFocusLost
        // TODO add your handling code here:
        if (txtAmountCSI.getText().length() != 0) {
            validateAmount(txtAmountCSI);
            String prodType = CommonUtil.convertObjToStr(observable.getCbmProductTypeCSI().getKeyForSelected());
            if (prodType.equalsIgnoreCase("RM")) {
                String prodID = CommonUtil.convertObjToStr((((ComboBoxModel) (cboProdIDCSI).getModel())).getKeyForSelected());
                String amt = CommonUtil.convertObjToStr(txtAmountCSI.getText());
                rdoRettCommSI_Yes.setSelected(true);
                observable.setTxtRettCommChargesSI(observable.executeQueryForCharge(prodID, getCustCat(), amt, "EXCHANGE"));
                observable.setTxtAcceptanceCharges(observable.executeQueryForCharge(prodID, getCustCat(), amt, "POSTAL"));
                txtRettCommChargesSI.setText(observable.getTxtRettCommChargesSI());
                txtAcceptanceCharges.setText(observable.getTxtAcceptanceCharges());
//                double totCharges = (CommonUtil.convertObjToDouble(txtRettCommChargesSI.getText()).doubleValue()+
//                                    CommonUtil.convertObjToDouble(txtAcceptanceCharges.getText()).doubleValue());
                double exchange = CommonUtil.convertObjToDouble(txtRettCommChargesSI.getText()).doubleValue();
                double postal = CommonUtil.convertObjToDouble(txtAcceptanceCharges.getText()).doubleValue();
                double exgST = observable.calServiceTax(String.valueOf(exchange), prodID, getCustCat(), amt, "EXCHANGE");
                double postalST = observable.calServiceTax(String.valueOf(postal), prodID, getCustCat(), amt, "POSTAL");
                double totST = observable.roundInterest(exgST + postalST);
                observable.setTxtServiceTax(String.valueOf(totST));
                txtServiceTax.setText(observable.getTxtServiceTax());
            }
        }
        if (observable.getCbmSIType().getKeyForSelected().equals("VARIABLE")) {
            ClientUtil.showAlertWindow("Selected SI type is Variable amount need not be specified");
            txtAmountCSI.setText("");
        }
    }//GEN-LAST:event_txtAmountCSIFocusLost

    private void txtAmountDSIFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAmountDSIFocusLost
        // TODO add your handling code here:
        if (txtAmountDSI.getText().length() != 0) {
            validateAmount(txtAmountDSI);
        }
//        if (observable.getCbmSIType().getKeyForSelected().equals("VARIABLE")) {
//            ClientUtil.showAlertWindow("Selected SI type is Variable amount need not be specified");
//            txtAmountDSI.setText("");
//        }
    }//GEN-LAST:event_txtAmountDSIFocusLost

    private void cboProdIDDSIItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboProdIDDSIItemStateChanged
        // TODO add your handling code here:
        txtAccHeadValueDSI.setText("");
        String prodType = CommonUtil.convertObjToStr(observable.getCbmProductTypeDSI().getKeyForSelected());
        String prodID = (String) observable.getCbmProdIDDSI().getKeyForSelected();
        if (prodID != null && !prodID.equals("") && prodType != null && !prodType.equals("")) {
            txtAccHeadValueDSI.setText(observable.getAccHeadLabelCaption(prodID, prodType));
            //observable.setCboProdIDCSI(prodID);
            return;
        }
    }//GEN-LAST:event_cboProdIDDSIItemStateChanged

    private void cboProdIDDSIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdIDDSIActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboProdIDDSIActionPerformed

    private void cboSITypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboSITypeActionPerformed
        // TODO add your handling code here:
        cboSITypeSelected();
    }//GEN-LAST:event_cboSITypeActionPerformed

    private void btnAccHeadCSIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccHeadCSIActionPerformed
        // TODO add your handling code here:
        callView("ActHeadCredit");
    }//GEN-LAST:event_btnAccHeadCSIActionPerformed

    private void btnAccHeadDSIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccHeadDSIActionPerformed
        // TODO add your handling code here:
        callView("ActHeadDebit");
    }//GEN-LAST:event_btnAccHeadDSIActionPerformed

    private void rdoHolidayExecution_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoHolidayExecution_NoActionPerformed
        // TODO add your handling code here:
        cboExecutionDay.setEnabled(true);
    }//GEN-LAST:event_rdoHolidayExecution_NoActionPerformed

    private void rdoHolidayExecution_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoHolidayExecution_YesActionPerformed
        // TODO add your handling code here:
        cboExecutionDay.setSelectedItem("");
        cboExecutionDay.setEnabled(false);

    }//GEN-LAST:event_rdoHolidayExecution_YesActionPerformed

    private void cboExecutionDayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboExecutionDayActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboExecutionDayActionPerformed

    private void cboProdIDCSIItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboProdIDCSIItemStateChanged
        // TODO add your handling code here:
        txtAccHeadValueCSI.setText("");
        String prodType = CommonUtil.convertObjToStr(observable.getCbmProductTypeCSI().getKeyForSelected());
        String prodID = (String) observable.getCbmProdIDCSI().getKeyForSelected();
        if (prodID != null && !prodID.equals("") && prodType != null && !prodType.equals("")) {
            txtAccHeadValueCSI.setText(observable.getAccHeadLabelCaption(prodID, prodType));
            if (prodType.equalsIgnoreCase("RM")) {
                lblAccNoCSI.setText("Favouring Name");
                txtAccNoCSI.setEnabled(true);
                txtAccNoCSI.setEditable(true);
                btnHelpAccNoCSI.setEnabled(false);
                txtAccNoCSI.setText("");
                txtAmountCSI.setText("");
                txtParticularsCSI.setText("");
                txtRettCommChargesSI.setText("");
                txtAcceptanceCharges.setText("");
            } else if (prodType.equals("MDS")) {
                lblAccNoCSI.setText(resourceBundle.getString("lblAccNoCSIMDS"));
                rdoSIAutoPosting_Yes.setSelected(true);
                rdoSIAutoPosting_No.setEnabled(false);
            } else {
                lblAccNoCSI.setText(resourceBundle.getString("lblAccNoCSI"));
                txtAccNoCSI.setEnabled(false);
                txtAccNoCSI.setEditable(false);
                rdoSIAutoPosting_Yes.setSelected(false);
                rdoSIAutoPosting_No.setEnabled(true);
            }
            //observable.setCboProdIDCSI(prodID);
            return;
        }
    }//GEN-LAST:event_cboProdIDCSIItemStateChanged

    private void cboProductTypeCSIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProductTypeCSIActionPerformed
        // TODO add your handling code here:
        cboProdTypeCSISelected();
    }//GEN-LAST:event_cboProductTypeCSIActionPerformed

    private void cboProductTypeDSIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProductTypeDSIActionPerformed
        // TODO add your handling code here:
        if (cboProductTypeDSI.getSelectedIndex() > 0) {
            cboProdTypeDSISelected();
        }
    }//GEN-LAST:event_cboProductTypeDSIActionPerformed

    private void txtAccNoDSIFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccNoDSIFocusLost
        // TODO add your handling code here:
        observable.setCboProductTypeDSI("");
        String ACCOUNTNO = txtAccNoDSI.getText();
        observable.debitAcNo = false;
        observable.creditAcNo = false;
        if ((!(observable.getCboProductTypeDSI().length() > 0)) && ACCOUNTNO.length() > 0) {
            observable.debitAcNo = true;
            if (observable.checkAcNoWithoutProdType(ACCOUNTNO)) {
                txtAccNoDSI.setText(observable.getTxtAccNoDSI());
                cboProdIDDSI.setModel(observable.getCbmProdIDDSI());
//                txtAccNoDSI.setText(String.valueOf(ACCOUNTNO));
                String prodType = ((ComboBoxModel) cboProductTypeDSI.getModel()).getKeyForSelected().toString();
                lblNameValueDSI.setText(observable.getLblNameValueDSI());
            } else {
                ClientUtil.showAlertWindow("Invalid Account No.");
                txtAccNoDSI.setText("");
                return;
            }
        }
        if (txtAccNoDSI.getText().length() > 0) {
            viewType = "DIAccountNo";
            HashMap fillMap = new HashMap();
            prodType = CommonUtil.convertObjToStr(observable.getCbmProductTypeDSI().getKeyForSelected());
            fillMap.put("ACCOUNTNO", txtAccNoDSI.getText());
            fillData(fillMap);
        }
    }//GEN-LAST:event_txtAccNoDSIFocusLost

    private void btnSaveCSIFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnSaveCSIFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSaveCSIFocusGained

    private void txtAmountDSIFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAmountDSIFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAmountDSIFocusGained

    private void cboFrequencySIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboFrequencySIActionPerformed
        // TODO add your handling code here:
//        enableOrDisableCbo();
    }//GEN-LAST:event_cboFrequencySIActionPerformed

    private void cboFrequencySIFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboFrequencySIFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_cboFrequencySIFocusGained

    private void cboFrequencySIItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboFrequencySIItemStateChanged
        // TODO add your handling code here:
        if (!cboFrequencySI.getSelectedItem().equals("")) {
            cboWeek.setSelectedItem("");
            cboWeekDay.setSelectedItem("");
            cboWeek.setEnabled(false);
            cboWeekDay.setEnabled(false);
        } else {
            cboWeek.setEnabled(true);
            cboWeekDay.setEnabled(true);
        }

    }//GEN-LAST:event_cboFrequencySIItemStateChanged

    private void rdoRettCommSI_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoRettCommSI_NoActionPerformed
        // TODO add your handling code here:
        disabledTextBox(txtRettCommChargesSI, panCharges, true);
        txtRettCommChargesSI.setText("");
        txtRettCommChargesSI.setEditable(false);
        txtAcceptanceCharges.setText("");
        txtAcceptanceCharges.setEditable(false);
        txtServiceTax.setText("");
        txtServiceTax.setEditable(false);
    }//GEN-LAST:event_rdoRettCommSI_NoActionPerformed

    private void rdoRettCommSI_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoRettCommSI_YesActionPerformed
        // TODO add your handling code here:
        disabledTextBox(txtRettCommChargesSI, panCharges, false);
        txtRettCommChargesSI.setEditable(true);
//          txtAcceptanceCharges.setText("");
        txtAcceptanceCharges.setEditable(true);
        txtServiceTax.setEditable(true);
    }//GEN-LAST:event_rdoRettCommSI_YesActionPerformed

    private void rdoCommSI_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoCommSI_NoActionPerformed
        // TODO add your handling code here:
        disabledTextBox(txtCommChargesSI, panCharges, true);
        txtCommChargesSI.setText("");
        txtCommChargesSI.setEditable(false);
        txtExecutionCharges.setText("");
        txtExecutionCharges.setEditable(false);
    }//GEN-LAST:event_rdoCommSI_NoActionPerformed

    private void rdoCommSI_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoCommSI_YesActionPerformed
        // TODO add your handling code here:
        disabledTextBox(txtCommChargesSI, panCharges, false);
        txtCommChargesSI.setEditable(true);
        txtExecutionCharges.setText("");
        txtExecutionCharges.setEditable(true);
    }//GEN-LAST:event_rdoCommSI_YesActionPerformed

    private void dtdEndDtSIFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_dtdEndDtSIFocusLost
        // TODO add your handling code here:
        String startDate = dtdStartDtSI.getDateValue();
        ClientUtil.validateToDate(dtdEndDtSI, startDate);
    }//GEN-LAST:event_dtdEndDtSIFocusLost

    private void btnHelpAccNoDSIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHelpAccNoDSIActionPerformed
        // TODO add your handling code here:
        callView("DIAccountNo");
    }//GEN-LAST:event_btnHelpAccNoDSIActionPerformed

    private void btnDeleteCSIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteCSIActionPerformed
        // TODO add your handling code here:
        updateOBFields();
        observable.doAction("DeleteCI");
        ClientUtil.enableDisable(panCreditSI, false);
        fillLblTotal(tblCreditSI, lblCrTotValue);
        enableCreditSIComponents(false);
        btnSaveCSI.setEnabled(false);
        btnDeleteCSI.setEnabled(false);
        btnHelpAccNoCSI.setEnabled(false);
        isCreditClicked = false;
    }//GEN-LAST:event_btnDeleteCSIActionPerformed

    private void btnSaveCSIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveCSIActionPerformed
        // TODO add your handling code here:
        if ((tblDebitSI.getRowCount() > 1) && (tblCreditSI.getRowCount() != 0) && (isCreditClicked != true)) {
            ClientUtil.showAlertWindow("multiple credits not allowed");
            cboProductTypeCSI.setSelectedItem("");
            cboProdIDCSI.setSelectedItem("");
            txtAccNoCSI.setText("");
            txtAmountCSI.setText("");
            txtParticularsCSI.setText("");
            lblNameValueCSI.setText("");
        } else {
            alertMsg = "craccNoMsg";
            String prdType = CommonUtil.convertObjToStr(observable.getCbmProductTypeCSI().getKeyForSelected());
            accNumberCheck(tblDebitSI, txtAccNoCSI, alertMsg);
            String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panCreditSI);
            if (observable.getCbmProductTypeCSI().getKeyForSelected().equals("GL")) {
                if (txtAccHeadValueCSI.getText().equals("")) {
                    mandatoryMessage = mandatoryMessage + objMandatoryRB.getString("txtAccHeadValueCSI");
                }
            } else {
                if (txtAccNoCSI.getText().equals("")) {
                    mandatoryMessage = mandatoryMessage + objMandatoryRB.getString("txtAccNoCSI");
                }
                if (cboProdIDCSI.getSelectedItem().equals("")) {
                    mandatoryMessage = mandatoryMessage + objMandatoryRB.getString("cboProdIDCSI");
                }
            }
            if (cboSIType.getSelectedIndex() > 0) {
                if (observable.getCbmSIType().getKeyForSelected().equals("FIXED")
                        && (txtAmountCSI.getText().length() < 0)) {
                    mandatoryMessage = mandatoryMessage + objMandatoryRB.getString("txtAmountCSI");
                }
            }
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
                    double creditAmount = CommonUtil.convertObjToDouble(txtAmountCSI.getText());
                    if (cboSIType.getSelectedIndex() > 0 && CommonUtil.convertObjToStr(cboSIType.getSelectedItem()).equalsIgnoreCase("Fixed") && creditAmount <= 0) {
                        displayAlert(objMandatoryRB.getString("txtAmountCSI"));
                        return;
                    }
            }
            if (mandatoryMessage.length() > 0) {
                displayAlert(mandatoryMessage.toString());
                return;
            }

            updateOBFields();
            observable.doAction("SaveCI");
            ClientUtil.enableDisable(panCreditSI, false);
            enableCreditSIComponents(false);
            fillLblTotal(tblCreditSI, lblCrTotValue);
            setButtonsCI();
            if ((prdType.equals("RM"))) {
                btnNewCSI.setEnabled(false);
            }
            cboWeek.setEnabled(true);
            cboWeekDay.setEnabled(true);
            isCreditClicked = false;
        }
    }//GEN-LAST:event_btnSaveCSIActionPerformed

    private void btnNewCSIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewCSIActionPerformed
        // TODO add your handling code here:
        if (cboSIType.getSelectedIndex() > 0) {
            if ((observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) && cboSIType.getSelectedIndex() > 0) { //Added By Suresh R Ref By Mr Srinath  17-Jul-2017
                if (CommonUtil.convertObjToStr(observable.getCbmSIType().getKeyForSelected()).equals("VARIABLE")) {
                    if (tblCreditSI.getRowCount() > 0) {
                        ClientUtil.showMessageWindow("Multiple Record Not Allowed !!!");
                        return;
                    }
                }
            }
            if (tblDebitSI.getModel().getRowCount() == 0 && callingfromDeposit == false) {
                alertMsg = "tblmsg";
                try {
                    showAlertWindow(alertMsg);
                } catch (Exception e) {
                }
            } else {
                updateOBFields();
                observable.doAction("NewCI");
                ClientUtil.enableDisable(panCreditSI, true);
                enableCreditSIComponents(true);
                btnSaveCSI.setEnabled(true);
                btnHelpAccNoCSI.setEnabled(true);
            }
            cboWeek.setEnabled(true);
            cboWeekDay.setEnabled(true);
        } else {
            ClientUtil.showMessageWindow("Please Select Standing Instruction Type !!!");
            return;
        }
        
    }//GEN-LAST:event_btnNewCSIActionPerformed

    private void tblCreditSIMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCreditSIMousePressed
        // TODO add your handling code here:
        updateOBFields();
        enableCreditSIComponents(true);
        observable.getRowTableCI(tblCreditSI.getSelectedRow());
        observable.getAccNoDetails(CommonUtil.convertObjToStr(observable.getCbmProductTypeCSI().getKeyForSelected()), txtAccNoCSI.getText(), "CIAccountNo");
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT || observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            setCreditBtnsEnable(true);
            ClientUtil.enableDisable(panCreditActDetails, true);
            txtAccNoCSI.setEnabled(true);
            txtAccNoCSI.setEditable(true);
            isCreditClicked = true;
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || viewType.equals(AUTHORIZE) || viewType.equals("Enquiry")) {
            setCreditBtnsEnable(false);
            ClientUtil.enableDisable(panCreditActDetails, false);
            ClientUtil.enableDisable(panSchedule, false);
            ClientUtil.enableDisable(panCharges, false);
            setHelpBtnEnable(false);
        }
        if (callingfromDeposit == true || callingfromEditMode == true) {
            ClientUtil.enableDisable(panCreditActDetails, false);
            btnHelpAccNoCSI.setEnabled(false);
            btnNewCSI.setEnabled(false);
            btnDeleteCSI.setEnabled(false);
            cboExecConfig.setEnabled(false);
            txtGraceDaysSI.setEnabled(false);
            cboExecutionDay.setEnabled(false);
            cboExecConfig.setEditable(false);
            txtGraceDaysSI.setEditable(false);
            cboExecutionDay.setEditable(false);
            cboSpecificDate.setEnabled(false);
            cboSpecificDate.setEditable(false);
            cboWeekDay.setEditable(false);
            cboWeekDay.setEnabled(false);
            cboWeek.setEditable(false);
            cboWeek.setEnabled(false);
            rdoHolidayExecution_Yes.setEnabled(false);
            rdoHolidayExecution_No.setEnabled(false);
            cboMoRSI.setEditable(false);
            cboMoRSI.setEnabled(false);
            txtBeneficiarySI.setEditable(false);
            txtBeneficiarySI.setEnabled(false);
            cboFrequencySI.setEditable(false);
            cboFrequencySI.setEnabled(false);
            txtParticularsCSI.setText(getCallingfromParticulars());
            btnSaveCSI.setEnabled(false);
        }
        if (viewType.equals(AUTHORIZE) || viewType.equals("Enquiry")) {
            setCreditBtnsEnable(false);
            cboProdIDCSI.setEnabled(false);
            cboSpecificDate.setEnabled(false);
            cboProdIDDSI.setEnabled(false);
//            ClientUtil.enableDisable(panCreditActDetails,false);
            ClientUtil.enableDisable(panDebitSI, false);
            ClientUtil.enableDisable(panCreditSI, false);
            setHelpBtnEnable(false);
        }
        if (CommonUtil.convertObjToStr(observable.getCbmProductTypeCSI().getKeyForSelected()).equals("TD")) {
            txtAmountCSI.setEnabled(false);
        }

    }//GEN-LAST:event_tblCreditSIMousePressed

    private void txtAmountCSIFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAmountCSIFocusGained
        // TODO add your handling code here:
        if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE) {
            if (!txtAccNoCSI.getText().equals("") && txtAccNoCSI.getText().length() > 0) {
                alertMsg = "craccNoMsg";
            }
            accNumberCheck(tblDebitSI, txtAccNoCSI, alertMsg);
            alertMsg = "acHdMsg";
            if (observable.getCbmProductTypeCSI().getKeyForSelected().equals("GL")) {
                accHeadCheck(tblDebitSI, txtAccHeadValueCSI, alertMsg);
            }
        }
    }//GEN-LAST:event_txtAmountCSIFocusGained

    private void btnHelpAccNoCSIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHelpAccNoCSIActionPerformed
        // TODO add your handling code here:
        callView("CIAccountNo");
    }//GEN-LAST:event_btnHelpAccNoCSIActionPerformed

    private void btnDeleteDSIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteDSIActionPerformed
        // TODO add your handling code here:
        updateOBFields();
        observable.doAction("DeleteDI");
        ClientUtil.enableDisable(panDebitSI, false);
        fillLblTotal(tblDebitSI, lblDbTotalValue);
        enableDebitSIComponents(false);
        btnSaveDSI.setEnabled(false);
        btnDeleteDSI.setEnabled(false);
        btnHelpAccNoDSI.setEnabled(false);
        isDebitClicked = false;
    }//GEN-LAST:event_btnDeleteDSIActionPerformed

    private void btnSaveDSIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveDSIActionPerformed
        // TODO add your handling code here:
        try {
            if ((tblCreditSI.getRowCount() > 1) && (tblDebitSI.getRowCount() != 0) && (isDebitClicked != true)) {
                ClientUtil.showAlertWindow("multiple debits not allowed");
                cboProductTypeDSI.setSelectedItem("");
                cboProdIDDSI.setSelectedItem("");
                txtAccNoDSI.setText("");
                txtAmountDSI.setText("");
                txtParticularsDSI.setText("");
                txtAccHeadValueDSI.setText("");
                lblNameValueDSI.setText("");
                lblNameValueCSI.setText("");
            } else {
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
                    if (!(txtAccNoDSI.equals("")) && txtAccNoDSI.getText().length() > 0) {
                        alertMsg = "dbaccNoMsg";
                        accNumberCheck(tblCreditSI, txtAccNoDSI, alertMsg);
                    }
                }
                String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panDebitSI);
                if (observable.getCbmProductTypeDSI().getKeyForSelected().equals("GL")) {
                    if (txtAccHeadValueDSI.getText().equals("")) {
                        mandatoryMessage = mandatoryMessage + objMandatoryRB.getString("txtAccHeadValueDSI");
                    }
                } else {
                    if (txtAccNoDSI.getText().equals("")) {
                        mandatoryMessage = mandatoryMessage + objMandatoryRB.getString("txtAccNoDSI");
                    }
                    if (cboProdIDDSI.getSelectedItem().equals("")) {
                        mandatoryMessage = mandatoryMessage + "\n" + objMandatoryRB.getString("cboProdIDDSI");
                    }
                }
                if (cboSIType.getSelectedIndex() > 0) {
                    if (observable.getCbmSIType().getKeyForSelected().equals("FIXED")
                            && (txtAmountDSI.getText().length() <= 0)) {
                        mandatoryMessage = mandatoryMessage + "\n" + objMandatoryRB.getString("txtAmountDSI");
                    }
                }
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
                    double debitAmount = CommonUtil.convertObjToDouble(txtAmountDSI.getText());
                    if (cboSIType.getSelectedIndex() > 0 && CommonUtil.convertObjToStr(cboSIType.getSelectedItem()).equalsIgnoreCase("Fixed") && debitAmount <= 0) {
                        displayAlert(objMandatoryRB.getString("txtAmountDSI"));
                        return;
                    }
                }
                if (callingfromDeposit == true) {
                    txtParticularsCSI.setText("By TR " + txtAccNoDSI.getText());
                    setCallingfromParticulars("By TR " + txtAccNoDSI.getText());
                }
                if (mandatoryMessage.length() > 0) {
                    displayAlert(mandatoryMessage);
                    return;
                }
                updateOBFields();
                observable.doAction("SaveDI");
                ClientUtil.enableDisable(panDebitSI, false);
                enableDebitSIComponents(false);
                fillLblTotal(tblDebitSI, lblDbTotalValue);
                setButtonsDI();
                cboWeek.setEnabled(true);
                cboWeekDay.setEnabled(true);
                if (callingfromDeposit == true || callingfromEditMode == true) {
                    ClientUtil.enableDisable(panCreditActDetails, false);
                    btnHelpAccNoCSI.setEnabled(false);
                    btnDeleteCSI.setEnabled(false);
                    btnNewCSI.setEnabled(false);
                    cboExecConfig.setEnabled(false);
                    txtGraceDaysSI.setEnabled(false);
                    cboExecutionDay.setEnabled(false);
                    cboExecConfig.setEditable(false);
                    txtGraceDaysSI.setEditable(false);
                    cboExecutionDay.setEditable(false);
                    cboSpecificDate.setEnabled(false);
                    cboSpecificDate.setEditable(false);
                    cboWeekDay.setEditable(false);
                    cboWeekDay.setEnabled(false);
                    cboWeek.setEditable(false);
                    cboWeek.setEnabled(false);
                    rdoHolidayExecution_Yes.setEnabled(false);
                    rdoHolidayExecution_No.setEnabled(false);
                    cboMoRSI.setEditable(false);
                    cboMoRSI.setEnabled(false);
                    txtBeneficiarySI.setEditable(false);
                    txtBeneficiarySI.setEnabled(false);
                    cboFrequencySI.setEditable(false);
                    cboFrequencySI.setEnabled(false);
                    txtParticularsCSI.setText(getCallingfromParticulars());
                    btnSaveCSI.setEnabled(false);
                }
//            if(tblCreditSI.getRowCount() > 1)
//                btnNewDSI.setEnabled(false);
                isDebitClicked = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnSaveDSIActionPerformed

    private void btnNewDSIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewDSIActionPerformed
        // TODO add your handling code here:
        if (cboSIType.getSelectedIndex() > 0) {
            if ((observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) && cboSIType.getSelectedIndex() > 0) { //Added By Suresh R Ref By Mr Srinath  17-Jul-2017
                if (CommonUtil.convertObjToStr(observable.getCbmSIType().getKeyForSelected()).equals("VARIABLE")) {
                    if (tblDebitSI.getRowCount() > 0) {
                        ClientUtil.showMessageWindow("Multiple Record Not Allowed !!!");
                        return;
                    }
                }
            }
            updateOBFields();
            observable.doAction("NewDI");
            ClientUtil.enableDisable(panDebitSI, true);
            enableDebitSIComponents(true);
            btnSaveDSI.setEnabled(true);
            btnHelpAccNoDSI.setEnabled(true);
            cboWeek.setEnabled(true);
            cboWeekDay.setEnabled(true);
            if (callingfromDeposit == true || callingfromEditMode == true) {
                ClientUtil.enableDisable(panCreditActDetails, false);
                btnHelpAccNoCSI.setEnabled(false);
                btnDeleteCSI.setEnabled(false);
                btnNewCSI.setEnabled(false);
                cboExecConfig.setEnabled(false);
                txtGraceDaysSI.setEnabled(false);
                cboExecutionDay.setEnabled(false);
                cboExecConfig.setEditable(false);
                txtGraceDaysSI.setEditable(false);
                cboExecutionDay.setEditable(false);
                cboSpecificDate.setEnabled(false);
                cboSpecificDate.setEditable(false);
                cboWeekDay.setEditable(false);
                cboWeekDay.setEnabled(false);
                cboWeek.setEditable(false);
                cboWeek.setEnabled(false);
                rdoHolidayExecution_Yes.setEnabled(false);
                rdoHolidayExecution_No.setEnabled(false);
                cboMoRSI.setEditable(false);
                cboMoRSI.setEnabled(false);
                txtBeneficiarySI.setEditable(false);
                txtBeneficiarySI.setEnabled(false);
                cboFrequencySI.setEditable(false);
                cboFrequencySI.setEnabled(false);
                txtParticularsCSI.setText(getCallingfromParticulars());
                btnSaveCSI.setEnabled(false);
            }
        } else {
            ClientUtil.showMessageWindow("Please Select Standing Instruction Type !!!");
            return;
        }
    }//GEN-LAST:event_btnNewDSIActionPerformed

    private void tblDebitSIMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDebitSIMousePressed
        // TODO add your handling code here:
        updateOBFields();
        enableDebitSIComponents(true);
        observable.getRowTableDI(tblDebitSI.getSelectedRow());
        observable.getAccNoDetails(CommonUtil.convertObjToStr(observable.getCbmProductTypeDSI().getKeyForSelected()), txtAccNoDSI.getText(), "DIAccountNo");
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT || observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            setDebitBtnsEnable(true);
            ClientUtil.enableDisable(panDebitActDetails, true);
            ClientUtil.enableDisable(tabSIDetails, true);
            if (observable.getCbmProductTypeDSI().getKeyForSelected().equals("TD")) {
                cboFrequencySI.setEnabled(true);
            }
            isDebitClicked = true;
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || viewType.equals(AUTHORIZE) || viewType.equals("Enquiry")) {
            setDebitBtnsEnable(false);
//              ClientUtil.enableDisable(panDebitActDetails,false);
            ClientUtil.enableDisable(panSchedule, false);
            ClientUtil.enableDisable(panCharges, false);
            setHelpBtnEnable(false);
        }
        if (callingfromDeposit == true || callingfromEditMode == true) {
            ClientUtil.enableDisable(panCreditActDetails, false);
            btnHelpAccNoCSI.setEnabled(false);
            btnDeleteCSI.setEnabled(false);
            btnNewCSI.setEnabled(false);
            cboExecConfig.setEnabled(false);
            txtGraceDaysSI.setEnabled(false);
            cboExecutionDay.setEnabled(false);
            cboExecConfig.setEditable(false);
            txtGraceDaysSI.setEditable(false);
            cboExecutionDay.setEditable(false);
            cboExecConfig.setEditable(false);
            txtGraceDaysSI.setEditable(false);
            cboExecutionDay.setEditable(false);
            cboSpecificDate.setEnabled(false);
            cboSpecificDate.setEditable(false);
            cboWeekDay.setEditable(false);
            cboWeekDay.setEnabled(false);
            cboWeek.setEditable(false);
            cboWeek.setEnabled(false);
            rdoHolidayExecution_Yes.setEnabled(false);
            rdoHolidayExecution_No.setEnabled(false);
            cboMoRSI.setEditable(false);
            cboMoRSI.setEnabled(false);
            txtBeneficiarySI.setEditable(false);
            txtBeneficiarySI.setEnabled(false);
            cboFrequencySI.setEditable(false);
            cboFrequencySI.setEnabled(false);
            txtParticularsCSI.setText(getCallingfromParticulars());
            btnSaveCSI.setEnabled(false);
        }
        if (viewType.equals(AUTHORIZE) || viewType.equals("Enquiry")) {
            setDebitBtnsEnable(false);
            cboProdIDCSI.setEnabled(false);
            cboSpecificDate.setEnabled(false);
//            ClientUtil.enableDisable(panDebitActDetails,false);
            ClientUtil.enableDisable(panDebitSI, false);
            ClientUtil.enableDisable(panCreditSI, false);
            setHelpBtnEnable(false);
        }
        cboSpecificDate.setEnabled(false);
        if (cboSIType.getSelectedIndex() > 0) { //Added By Suresh R  Recurring Product Should Allow Variable Type (i.e Amount Always Empty)
            if (!observable.getCbmSIType().getKeyForSelected().equals("FIXED")) {
                txtAmountCSI.setText("");
                txtAmountDSI.setText("");
                cboSIType.setEnabled(false);
                txtAmountCSI.setEnabled(false);
                txtAmountDSI.setEnabled(false);
            }
        }
    }//GEN-LAST:event_tblDebitSIMousePressed

    private void btnHelpAccNoDSIItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_btnHelpAccNoDSIItemStateChanged
        // TODO add your handling code here:
        callView("DIAccountNo");
    }//GEN-LAST:event_btnHelpAccNoDSIItemStateChanged

    private void btnstpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnstpActionPerformed
        // TODO add your handling code here:
        HashMap mapParam = new HashMap();
        HashMap whereMap = new HashMap();
        mapParam.put(CommonConstants.MAP_NAME, "getOther//System");
        mapParam.put(CommonConstants.MAP_WHERE, whereMap);

        // SendToSTPUI authorizeUI = new SendToSTPUI(mapParam);
        //authorizeUI.show();
    }//GEN-LAST:event_btnstpActionPerformed

    private void accNumberCheck(CTable tblCrdb, CTextField txtAccNo, String alertMsg) {
        try {
            String accNum = txtAccNo.getText();
            int rowCount = tblCrdb.getModel().getRowCount();
            for (int i = 0; i < rowCount; i++) {
                String tblAccNum = tblCrdb.getValueAt(i, 3).toString();
                if (!tblAccNum.equals("")) {
                    if (tblAccNum.equalsIgnoreCase(accNum)) {
                        showAlertWindow(alertMsg);
                        txtAccNo.setText("");
                        txtAccNo.requestFocus();
                        return;
                    }
                }
            }
        } catch (Exception e) {
        }

    }
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeClicked = true;
        observable.resetForm();
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
        btnCancel.setEnabled(true);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeClicked = true;
        observable.resetForm();
        authorizeStatus(CommonConstants.STATUS_REJECTED);
        btnCancel.setEnabled(true);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeClicked = true;
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnCancel.setEnabled(true);
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        cboWeek.setEnabled(false);
        cboWeekDay.setEnabled(false);
        cboSpecificDate.setEnabled(false);
        cboFrequencySI.setEnabled(false);

    }//GEN-LAST:event_btnAuthorizeActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        if (callingfromDeposit == true) {
            cancellingDepositRD();
        } else {
            ClientUtil.clearAll(this);
            cifClosingAlert();
        }
    }//GEN-LAST:event_btnCloseActionPerformed
    public void authorizeStatus(String authorizeStatus) {
//        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        observable.setStatus();


        if (viewType.equals(AUTHORIZE) && isFilled) {
            //Changed BY Suresh
//            String strWarnMsg = tabSIDetails.isAllTabsVisited();
//            if (strWarnMsg.length() > 0){
//                displayAlert(strWarnMsg);
//                return;
//            }
            viewType = ClientConstants.VIEW_TYPE_AUTHORIZE;
//            strWarnMsg = null;
            tabSIDetails.resetVisits();
            HashMap singleAuthorizeMap = new HashMap();
            displaySIID();
            singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
            singleAuthorizeMap.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDt);
            singleAuthorizeMap.put("SI_ID", siId);
            //System.out.println("Authorize Map " + singleAuthorizeMap);
            ClientUtil.execute("authorizeStandingInstruction", singleAuthorizeMap);
            if (authorizeStatus.equals("AUTHORIZED")) {
                if (dtdSuspendDt.getDateValue() != null && !dtdSuspendDt.getDateValue().equals("")) {
                    Date nxtDt = observable.getNxtDt();
                    Date suspDt = observable.getSuspDt();
                    Date lstDt = observable.getLstDt();
                    Date startDt = observable.getEndDt();
                    String executionDay = "NEXTDAY";
//                     long dateDiff = DateUtil.dateDiff(nxtDt, suspDt);
                    if (nxtDt != null) {
                        if ((DateUtil.dateDiff(nxtDt, suspDt)) >= 0) {
                            while ((DateUtil.dateDiff(nxtDt, suspDt)) >= 0) {
                                int freq = CommonUtil.convertObjToInt(observable.getFreq());
//                        observable.setNxtDt(DateUtil.addDaysProperFormat(nxtDt, freq));
                                nxtDt = DateUtil.addDaysProperFormat(nxtDt, freq);
                            }
                        } else {
                            if (lstDt != null) {
                                while ((DateUtil.dateDiff(lstDt, suspDt)) >= 0) {
                                    int freq = CommonUtil.convertObjToInt(observable.getFreq());
//                        observable.setNxtDt(DateUtil.addDaysProperFormat(nxtDt, freq));
                                    nxtDt = DateUtil.addDaysProperFormat(lstDt, freq);
                                    lstDt = DateUtil.addDaysProperFormat(lstDt, freq);
                                }
                            } else {
                                while ((DateUtil.dateDiff(startDt, suspDt)) >= 0) {
                                    int freq = CommonUtil.convertObjToInt(observable.getFreq());
//                        observable.setNxtDt(DateUtil.addDaysProperFormat(nxtDt, freq));
                                    nxtDt = DateUtil.addDaysProperFormat(startDt, freq);
                                    startDt = DateUtil.addDaysProperFormat(startDt, freq);
                                }
                            }
                        }
                    } else {
                        while ((DateUtil.dateDiff(startDt, suspDt)) >= 0) {
                            int freq = CommonUtil.convertObjToInt(observable.getFreq());
//                        observable.setNxtDt(DateUtil.addDaysProperFormat(nxtDt, freq));
                            nxtDt = DateUtil.addDaysProperFormat(startDt, freq);
                            startDt = DateUtil.addDaysProperFormat(startDt, freq);
                        }
                    }
                    HashMap paraMap = new HashMap();
                    paraMap.put("NEXT_DATE", nxtDt);
                    paraMap.put("SI_ID", txtSINo.getText());
                    observable.holiydaychecking(nxtDt, executionDay);
                    if (DateUtil.dateDiff(observable.getCheckThisCDate(), nxtDt) == 0) {
                        nxtDt = null;
                        //do nothing
                    } else {
                        observable.setNxtDt(nxtDt);

                    }
                    if (nxtDt != null) {
                        paraMap.put("HOL_EXEC_DATE", nxtDt);
                        ClientUtil.execute("updateSuspStatus", paraMap);
                    } else {
//                         paraMap.put("HOL_EXEC_DATE", "");
                        ClientUtil.execute("updateSuspStatusNull", paraMap);
                    }

                }
                if (creditProdType != null && creditProdType.equals("TD")) {
                    //System.out.println("DDDDDDD^^^^^^^^^^^^^^" + observable.getCrdActLst());
                    ArrayList newLst = (ArrayList) observable.getCrdActLst();
                    //System.out.println("newLst^^^^^^^^^^^^^^" + newLst);
                    for (int i = 0; i < newLst.size(); i++) {
                        //System.out.println("newLst^^^^^" + newLst.get(i));
                        HashMap newMap = new HashMap();
                        newMap.put("DEPOSIT_NO", CommonUtil.convertObjToStr(newLst.get(i)).substring(0, CommonUtil.convertObjToStr(newLst.get(i)).lastIndexOf("_")));
//                        actNum = txtAccNo.getText().substring(0, actNum.lastIndexOf("_"));
                        //System.out.println("newMap^^^^^" + newMap);
                        ClientUtil.execute("updateDepSIFlag", newMap);
                    }
                }
            }
            if (authorizeStatus.equals("REJECTED")) {
                //System.out.println("singleAuthorizeMap" + singleAuthorizeMap);
                List list = ClientUtil.executeQuery("getDeletedStatus", singleAuthorizeMap);
                //System.out.println("list@@@@@@@" + list);
                if (list.size() > 0) {
                    HashMap map = new HashMap();
                    map = (HashMap) list.get(0);
                    if (map.get("STATUS").equals("SUSPENDED")) {
                        ClientUtil.execute("setRejSuspStatus", singleAuthorizeMap);
                    }
                    if (map.get("STATUS").equals("CLOSED")) {
                        ClientUtil.execute("setDeletedStatus", singleAuthorizeMap);
                    }

                }
            }
            viewType = "";
            super.setOpenForEditBy(observable.getStatusBy());
            super.removeEditLock(siId);
            if(fromNewAuthorizeUI){
                this.dispose();
                newauthorizeListUI.setFocusToTable();
                newauthorizeListUI.removeSelectedRow();
                newauthorizeListUI.displayDetails("Standing Instruction");
            }
            if(fromAuthorizeUI){
                this.dispose();
                authorizeListUI.setFocusToTable();
                authorizeListUI.removeSelectedRow();
                authorizeListUI.displayDetails("Standing Instruction");
            }

            btnCancelActionPerformed(null);
        } //        else if (!viewType.equals(AUTHORIZE))
        else {
            setModified(true);
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            whereMap.put(CommonConstants.HIERARCHY_ID, TrueTransactMain.HIERARCHY_ID);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            //            whereMap = null;
            mapParam.put(CommonConstants.MAP_NAME, "getStandingInstructionAuthorizeList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeStandingInstruction");
            isFilled = false;
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
        }
    }

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
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        btnCloseActionPerformed(evt);
        // Add your handling code here:
    }//GEN-LAST:event_mitCloseActionPerformed

    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        btnCancelActionPerformed(evt);

    }//GEN-LAST:event_mitCancelActionPerformed

    private void cancellingDepositRD() {
        String[] obj = {"Yes", "No"};
        int option = COptionPane.showOptionDialog(null, ("Do u want to really cancel this Process..."), ("Select The Desired Option"),
                COptionPane.YES_NO_CANCEL_OPTION, COptionPane.QUESTION_MESSAGE, null, obj, obj[0]);
        if (option == 0) {
            callingfromDeposit = false;
            HashMap depositMap = new HashMap();
            String depositNo = getCallingfromDepositNo();
            if (depositNo.lastIndexOf("_") != -1) {
                depositNo = depositNo.substring(0, depositNo.lastIndexOf("_"));
            }
            depositMap.put("DEPOSIT_NO", depositNo);
            depositMap.put("STANDING_INSTRUCT", "N");
            ClientUtil.execute("updateStandingInstnNo", depositMap);
            setCallingfromDepositNo("");
            btnCancelActionPerformed(null);
        } else {
            return;
        }
    }

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        if (callingfromDeposit == true) {
            cancellingDepositRD();
        } else {
            setModified(false);
            if (observable.getAuthorizeStatus() != null) {
                super.removeEditLock(txtSINo.getText());
            }
            clearLblFields();
            observable.resetForm();
            ClientUtil.enableDisable(panMainSI, false);
            setDebitBtnsEnable(false);
            setCreditBtnsEnable(false);
            setHelpBtnEnable(false);
            setButtonEnableDisable();
            setCustCat("");
            viewType = "";
//            txtFailureCharges.setText("");
            btnAuthorize.setEnabled(true);
            btnReject.setEnabled(true);
            btnException.setEnabled(true);
            btnView.setEnabled(true);
            delNotAllowed = false;
        }
         if(fromNewAuthorizeUI){
            newauthorizeListUI.setFocusToTable();
            this.dispose();
        }
        if(fromAuthorizeUI){
            authorizeListUI.setFocusToTable();
            this.dispose();
        }

    }//GEN-LAST:event_btnCancelActionPerformed

    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        btnSaveActionPerformed(evt);
        // Add your handling code here:
    }//GEN-LAST:event_mitSaveActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        setModified(false);
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panSchedule);
        message = new StringBuffer(mandatoryMessage);
        message.append(new MandatoryCheck().checkMandatory(getClass().getName(), panSI));
        message.append(new MandatoryCheck().checkMandatory(getClass().getName(), panCharges));
        int rowCount = tblCreditSI.getModel().getRowCount();
        for (int i = 0; i < rowCount; i++) {
            String tblAccNum = tblCreditSI.getValueAt(i, 3).toString();
            String prodType = tblCreditSI.getValueAt(i, 0).toString();
            if (prodType != null && prodType.equalsIgnoreCase("TL") && !tblAccNum.equals("")) {
                HashMap whereMap = new HashMap();
                whereMap.put("ACT_NUM", tblAccNum);
                HashMap resultMap = new HashMap();
                List lst = ClientUtil.executeQuery("getInstallType", whereMap);
                if (lst != null && lst.size() > 0) {
                    resultMap = (HashMap) lst.get(0);
                    if (resultMap.containsKey("INSTALL_TYPE") && resultMap.get("INSTALL_TYPE").equals("EMI") && (!chkinstalments.isSelected() && !chkPendingInstalment.isSelected())) {
                        try {
                            displayAlert("Please Select 'Instalment Wise' or 'Pending Instalment wise' When credit is Emi Type Loan");
                        } catch (Exception ex) {
                            Logger.getLogger(StandingInstructionUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        return;
                    }
                }
            }
        }

        if (tblCreditSI.getModel().getRowCount() == 0) {
            message.append(objMandatoryRB.getString("NoSiCreditMsg"));
        } else if (tblDebitSI.getModel().getRowCount() == 0) {
            message.append(objMandatoryRB.getString("NoSiDebitMsg"));
        }


        if (CommonUtil.convertObjToStr(observable.getCbmSIType().getKeyForSelected()).equals("VARIABLE")) {
            if (txtMultiplieSI.getText().length() == 0 || txtMultiplieSI.getText().equals("")) {
                message.append(objMandatoryRB.getString("txtMultiplieSI"));
            }
            if (txtMinBalSI.getText().length() == 0 || txtMinBalSI.getText().equals("")) {
                message.append(objMandatoryRB.getString("txtMinBalSI"));
            }
        } else {
            if ((CommonUtil.convertObjToStr(cboFrequencySI.getSelectedItem()).length() == 0)
                    && (CommonUtil.convertObjToStr(cboWeek.getSelectedItem()).length() == 0)
                    && (CommonUtil.convertObjToStr(cboWeekDay.getSelectedItem()).length() == 0)) {
                message.append("\n" + "Atleast Frequency or WeekDay or Week should be selected");
            }
        }
        if (rdoCommSI_Yes.isSelected()) {
            if (txtCommChargesSI.getText().length() == 0 || txtCommChargesSI.getText().equals("")) {
                message.append(objMandatoryRB.getString("txtCommChargesSI"));
            }
        }
        if (rdoRettCommSI_Yes.isSelected()) {
            if (txtRettCommChargesSI.getText().length() == 0 || txtRettCommChargesSI.getText().equals("")) {
                message.append(objMandatoryRB.getString("txtRettCommChargesSI"));
            }
        }
        if (!(lblDbTotalValue.getText().equals("")) && !(lblCrTotValue.getText().equals(""))) {
            dbTotal = Double.parseDouble(lblDbTotalValue.getText());
            crTotal = Double.parseDouble(lblCrTotValue.getText());
        }

        if (observable.getCbmFrequencySI().getKeyForSelected().equals("WEEKLY")) {
            if (cboWeekDay.getSelectedItem().equals("")) {
                message.append(objMandatoryRB.getString("cboWeekDay"));
            }
            if (cboWeek.getSelectedItem().equals("")) {
                message.append(objMandatoryRB.getString("cboWeek"));
            }
            if (cboSpecificDate.getSelectedItem().equals("")) {
                message.append(objMandatoryRB.getString("cboSpecificDate"));
            }
        }
        if (chkSuspendUser.isSelected() && dtdSuspendDt.getDateValue().equals("")) {
            message.append("Enter the Susp date");
        }
        if (delNotAllowed) {
            message.append("Cannot Delete running SI, go for Closure");
        }
        
         if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            if (cboSIType.getSelectedIndex() > 0 && CommonUtil.convertObjToStr(cboSIType.getSelectedItem()).equalsIgnoreCase("Fixed")) {
                double debitAmt = 0.0;
                double creditAmt = 0.0;
                if (tblDebitSI.getRowCount() > 0) {
                    for (int i = 0; i < tblDebitSI.getRowCount(); i++) {
                        debitAmt += CommonUtil.convertObjToDouble(tblDebitSI.getValueAt(i, 4));
                    }
                }
                if (tblCreditSI.getRowCount() > 0) {
                    for (int i = 0; i < tblCreditSI.getRowCount(); i++) {
                        creditAmt += CommonUtil.convertObjToDouble(tblCreditSI.getValueAt(i, 4));
                    }
                }
                if (debitAmt <= 0) {
                    displayAlert("Debit Amount should not be zero");
                    return;
                }
                if (creditAmt <= 0) {
                    displayAlert("Credit Amount should not be zero");
                    return;
                }
            }
        }
        
        if (dbTotal == crTotal) {
            if (message.length() > 0) {
                displayAlert(message.toString());
                return;
            } else {
                updateOBFields();
                observable.setResult(observable.getActionType());
                observable.setResultStatus();
                if (lblStatus.getText().equals("New")) {
                    observable.doAction("InsertSI");
                } else if (lblStatus.getText().equals("Edit")) {
                    observable.doAction("UpdateSI");
                } else if (lblStatus.getText().equals("Delete")) {
                    observable.doAction("DeleteSI");
                }
                setCustCat("");
                if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
//                    super.removeEditLock(txtSINo.getText());
                    HashMap lockMap = new HashMap();
                    ArrayList lst = new ArrayList();
                    lst.add("TRANS_ID");
                    lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
                    if (observable.getProxyReturnMap() != null) {
                        if (observable.getProxyReturnMap().containsKey("TRANS_ID")) {
                            lockMap.put("TRANS_ID", observable.getProxyReturnMap().get("TRANS_ID"));
                        }
                    }
                    if (lblStatus.getText().equals("Edit")) {
                        lst = new ArrayList();
                        lst.add("SI_ID");
                        lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
                        lockMap.put("SI_ID", observable.getTxtSINo());
//                    observable.resetForm();
                    }
                    setEditLockMap(lockMap);
                    setEditLock();
                    observable.resetForm();

                    ClientUtil.enableDisable(panMainSI, false);
                    setButtonEnableDisable();
                    setHelpBtnEnable(false);
                    setDebitBtnsEnable(false);
                    setCreditBtnsEnable(false);
                    clearLblFields();
                    callingfromDeposit = false;
                    setCustCat("");
                }
            }
        } else if (dbTotal != crTotal) {
            try {
                alertMsg = "errormsg";
                showAlertWindow(alertMsg);
                // return;
            } catch (Exception e) {
            }
        }
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        this.chkSuspendUser.setSelected(false);
        // Add your handling code here:
    }//GEN-LAST:event_btnSaveActionPerformed
    private int showAlertWindow(String alertMsg) throws Exception {
        int optionSelected = 1;
        String[] options = {resourceBundle.getString("cDialogOK")};
        optionSelected = COptionPane.showOptionDialog(null, resourceBundle.getString(alertMsg), CommonConstants.INFORMATIONTITLE,
                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                null, options, options[0]);
        return optionSelected;
    }

    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        btnDeleteActionPerformed(evt);
        // Add your handling code here:
    }//GEN-LAST:event_mitDeleteActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        observable.setStatus();
        callView("Delete");
        ClientUtil.enableDisable(panMainSI, false);
        setButtonEnableDisable();
        setButtonsDI();
        btnNewDSI.setEnabled(false);
        setButtonsCI();
        btnNewCSI.setEnabled(false);
        displaySIID();
        fillLblTotal(tblDebitSI, lblDbTotalValue);
        fillLblTotal(tblCreditSI, lblCrTotValue);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        chkSuspendUser.setEnabled(false);
        // Add your handling code here:
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        btnEditActionPerformed(evt);
        // Add your handling code here:
    }//GEN-LAST:event_mitEditActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        observable.setStatus();
        callView("Edit");
        btnEdit.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnSave.setEnabled(true);
        btnCancel.setEnabled(true);
        //The code below is commented by Rajesh
        //enabledUI();
        ClientUtil.enableDisable(tabSIDetails, true);

        fillLblTotal(tblDebitSI, lblDbTotalValue);
        fillLblTotal(tblCreditSI, lblCrTotValue);
//        enableOrDisableCbo();
//        ClientUtil.enableDisable(panCharges, true);
//        ClientUtil.enableDisable(panSchedule,true);
//        if (prodType.equals("TD")){        
//            cboWeek.setEnabled(false);
//            cboWeekDay.setEnabled(false);
//            cboSpecificDate.setEnabled(true);
//            cboFrequencySI.setEnabled(false); 
//        }
//        else{
//            cboWeek.setEnabled(true);
//            cboWeekDay.setEnabled(true);
//            cboSpecificDate.setEnabled(false);
//            cboFrequencySI.setEnabled(true); 
//        }

        // Add your handling code here:
        displaySIID();
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        chkinstalments.setEnabled(true);
        chkPendingInstalment.setEnabled(true);
        cboSIType.setEnabled(true);
        //txtNoofInstalments.setEnabled(true);
    }//GEN-LAST:event_btnEditActionPerformed
    public HashMap standingInstnCallingFromDepositsEditMode(HashMap standingInstnMap, TermDepositUI termDepositUI) {
        standingInstnMap.put("ACT_NUM", standingInstnMap.get("DEPOSIT_NO"));
        HashMap amountMap = new HashMap();
        amountMap.put("DEPOSIT_NO", standingInstnMap.get("DEPOSIT_NO"));
        List lst = ClientUtil.executeQuery("getSelectCreditAccNo", standingInstnMap);
        if (lst != null && lst.size() > 0) {
            standingInstnMap = (HashMap) lst.get(0);
            callingfromDepositEditMode = true;
            callingfromEditMode = true;
            standingInstnMap.put("SI_ID", standingInstnMap.get("SI_ID"));
            setSelectedBranchID(ProxyParameters.BRANCH_ID);
            String depositNo = CommonUtil.convertObjToStr(amountMap.get("DEPOSIT_NO"));
            if (depositNo.lastIndexOf("_") != -1) {
                depositNo = depositNo.substring(0, depositNo.lastIndexOf("_"));
            }
            amountMap.put("DEPOSIT_NO", depositNo);
            lst = ClientUtil.executeQuery("getauthorizeByDeposit", amountMap);
            if (lst != null && lst.size() > 0) {
                amountMap = (HashMap) lst.get(0);
                txtAccNoCSI.setText(CommonUtil.convertObjToStr(amountMap.get("DEPOSIT_NO")) + "_1");
                txtAmountCSI.setText(CommonUtil.convertObjToStr(amountMap.get("DEPOSIT_AMT")));
                txtAmountDSI.setText(CommonUtil.convertObjToStr(amountMap.get("DEPOSIT_AMT")));
                txtParticularsDSI.setText("To TR " + CommonUtil.convertObjToStr(standingInstnMap.get("DEPOSIT_NO")) + "_1");
                Date depositDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(amountMap.get("DEPOSIT_DT")));
                depositDate = DateUtil.addDays(depositDate, 30);
                Date matDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(amountMap.get("MATURITY_DT")));
                matDate = DateUtil.addDays(matDate, -30);
                Date currDepDt = (Date) currDt.clone();
                Date currMatDt = (Date) currDt.clone();
                if (depositDate.getDate() > 0) {
                    currDepDt.setDate(depositDate.getDate());
                    currDepDt.setMonth(depositDate.getMonth());
                    currDepDt.setYear(depositDate.getYear());
                }
                if (matDate.getDate() > 0) {
                    currMatDt.setDate(matDate.getDate());
                    currMatDt.setMonth(matDate.getMonth());
                    currMatDt.setYear(matDate.getYear());
                }
                btnEditActionPerformed(null);
                fillData(standingInstnMap);
                cboProductTypeCSI.setSelectedItem("Deposits");
                cboProdIDCSI.setSelectedItem("Recurring Deposits");
                observable.setDtdStartDtSI(DateUtil.getStringDate(currDepDt));
                dtdStartDtSI.setDateValue(DateUtil.getStringDate(currDepDt));
                observable.setDtdEndDtSI(DateUtil.getStringDate(currMatDt));
                dtdEndDtSI.setDateValue(DateUtil.getStringDate(currMatDt));
                txtAccNoCSI.setText(CommonUtil.convertObjToStr(amountMap.get("DEPOSIT_NO")) + "_1");
                txtAmountCSI.setText(CommonUtil.convertObjToStr(amountMap.get("DEPOSIT_AMT")));
                txtParticularsCSI.setText(getCallingfromParticulars());
                btnSaveCSIActionPerformed(null);
                btnSaveCSI.setEnabled(false);
                btnNewCSI.setEnabled(false);
                btnNewDSI.setEnabled(true);
                btnSave.setEnabled(true);
                btnCancel.setEnabled(true);
                cboExecConfig.setEnabled(false);
                txtGraceDaysSI.setEnabled(false);
                cboExecutionDay.setEnabled(false);
                cboExecConfig.setEditable(false);
                txtGraceDaysSI.setEditable(false);
                cboExecutionDay.setEditable(false);
                cboSpecificDate.setEnabled(false);
                cboSpecificDate.setEditable(false);
                cboWeekDay.setEditable(false);
                cboWeekDay.setEnabled(false);
                cboWeek.setEditable(false);
                cboWeek.setEnabled(false);
                rdoHolidayExecution_Yes.setEnabled(false);
                rdoHolidayExecution_No.setEnabled(false);
                callingfromDepositEditMode = false;
            }
        }
        return standingInstnMap;
    }
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        btnNewActionPerformed(evt);
        // Add your handling code here:
    }//GEN-LAST:event_mitNewActionPerformed
    public HashMap standingInstnCallingFromDeposits(HashMap standingInstnMap, TermDepositUI termDepositUI) {
        callingfromDeposit = true;
        btnNewActionPerformed(null);
        cboSIType.setSelectedItem("Fixed");
        cboSIType.setEnabled(false);
        observable.setModule("Supporting Modules");
        observable.setScreen("Standing Instruction");
        setSelectedBranchID(ProxyParameters.BRANCH_ID);
        btnNewCSIActionPerformed(null);
        setCallingfromDepositNo(CommonUtil.convertObjToStr(standingInstnMap.get("DEPOSIT_NO")));
        cboProductTypeCSI.setSelectedItem("Deposits");
        cboProdIDCSI.setSelectedItem("Recurring Deposits");
        HashMap amountMap = new HashMap();
        amountMap.put("DEPOSIT_NO", standingInstnMap.get("DEPOSIT_NO"));
        List lst = ClientUtil.executeQuery("getauthorizeByDeposit", amountMap);
        if (lst != null && lst.size() > 0) {
            amountMap = (HashMap) lst.get(0);
            txtAccNoCSI.setText(CommonUtil.convertObjToStr(standingInstnMap.get("DEPOSIT_NO")) + "_1");
            txtAmountCSI.setText(CommonUtil.convertObjToStr(amountMap.get("DEPOSIT_AMT")));
            btnSaveCSIActionPerformed(null);
            btnNewDSIActionPerformed(null);
            btnNewDSI.setEnabled(false);
            btnNewCSI.setEnabled(false);
            btnNewDSI.setEnabled(true);
            btnSaveCSI.setEnabled(false);
            cboFrequencySI.setSelectedItem("Monthly");
            rdoHolidayExecution_No.setSelected(true);
            rdoSIAutoPosting_No.setSelected(true);
            cboExecConfig.setSelectedItem("Day Begin");
            cboExecutionDay.setSelectedItem("Next Day");
            cboExecConfig.setEnabled(false);
            txtGraceDaysSI.setEnabled(false);
            cboExecutionDay.setEnabled(false);
            rdoHolidayExecution_Yes.setEnabled(false);
            rdoHolidayExecution_No.setEnabled(false);
            txtAmountDSI.setText(CommonUtil.convertObjToStr(amountMap.get("DEPOSIT_AMT")));
            txtParticularsDSI.setText("To TR " + CommonUtil.convertObjToStr(standingInstnMap.get("DEPOSIT_NO")) + "_1");
            txtParticularsCSI.setText(getCallingfromParticulars());
            txtAmountDSI.setEditable(false);
            txtAmountDSI.setEnabled(false);
            txtParticularsDSI.setEditable(false);
            txtParticularsDSI.setEnabled(false);
            cboMoRSI.setEditable(false);
            cboMoRSI.setEnabled(false);
            txtBeneficiarySI.setEditable(false);
            txtBeneficiarySI.setEnabled(false);
            cboFrequencySI.setEditable(false);
            cboFrequencySI.setEnabled(false);
            txtParticularsCSI.setText(getCallingfromParticulars());
            Date depositDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(amountMap.get("DEPOSIT_DT")));
            depositDate = DateUtil.addDays(depositDate, 30);
            Date matDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(amountMap.get("MATURITY_DT")));
            matDate = DateUtil.addDays(matDate, -30);
            Date currDepDt = (Date) currDt.clone();
            Date currMatDt = (Date) currDt.clone();
            if (depositDate.getDate() > 0) {
                currDepDt.setDate(depositDate.getDate());
                currDepDt.setMonth(depositDate.getMonth());
                currDepDt.setYear(depositDate.getYear());
            }
            if (matDate.getDate() > 0) {
                currMatDt.setDate(matDate.getDate());
                currMatDt.setMonth(matDate.getMonth());
                currMatDt.setYear(matDate.getYear());
            }
            observable.setDtdStartDtSI(DateUtil.getStringDate(currDepDt));
            dtdStartDtSI.setDateValue(DateUtil.getStringDate(currDepDt));
            observable.setDtdEndDtSI(DateUtil.getStringDate(currMatDt));
            dtdEndDtSI.setDateValue(DateUtil.getStringDate(currMatDt));
        }
        return standingInstnMap;
    }
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.setStatus();
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        enabledUI();
        chkSuspendUser.setEnabled(false);
        txtSiDt.setText(DateUtil.getStringDate(currDt));
        txtSiDt.setEnabled(false);
        rdoCommSI_No.setSelected(true);
        rdoCommSI_NoActionPerformed(null);
        // Add your handling code here:
    }//GEN-LAST:event_btnNewActionPerformed

private void chkinstalmentsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkinstalmentsActionPerformed
if (chkinstalments.isSelected()) {
        chkPendingInstalment.setSelected(false);
        chkPendingInstalment.setEnabled(false);
        txtNoofInstalments.setEnabled(true);
    } else {
        txtNoofInstalments.setText("");
        chkPendingInstalment.setSelected(false);
        chkPendingInstalment.setEnabled(true);
        txtNoofInstalments.setEnabled(false);
    }
}//GEN-LAST:event_chkinstalmentsActionPerformed

private void chkPendingInstalmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkPendingInstalmentActionPerformed
if(chkPendingInstalment.isSelected()){
    chkinstalments.setSelected(false);
    chkinstalments.setEnabled(false);
    txtNoofInstalments.setEnabled(false);
}else{
    chkinstalments.setSelected(false);
    chkinstalments.setEnabled(true);
    txtNoofInstalments.setEnabled(false);
}
}//GEN-LAST:event_chkPendingInstalmentActionPerformed

    /**
     * Getter for property callingfromDepositNo.
     *
     * @return Value of property callingfromDepositNo.
     */
    public java.lang.String getCallingfromDepositNo() {
        return callingfromDepositNo;
    }

    /**
     * Setter for property callingfromDepositNo.
     *
     * @param callingfromDepositNo New value of property callingfromDepositNo.
     */
    public void setCallingfromDepositNo(java.lang.String callingfromDepositNo) {
        this.callingfromDepositNo = callingfromDepositNo;
    }

    /**
     * Getter for property callingfromParticulars.
     *
     * @return Value of property callingfromParticulars.
     */
    public java.lang.String getCallingfromParticulars() {
        return callingfromParticulars;
    }

    /**
     * Setter for property callingfromParticulars.
     *
     * @param callingfromParticulars New value of property
     * callingfromParticulars.
     */
    public void setCallingfromParticulars(java.lang.String callingfromParticulars) {
        this.callingfromParticulars = callingfromParticulars;
    }

    /**
     * Getter for property custCat.
     *
     * @return Value of property custCat.
     */
    public java.lang.String getCustCat() {
        return custCat;
    }

    /**
     * Setter for property custCat.
     *
     * @param custCat New value of property custCat.
     */
    public void setCustCat(java.lang.String custCat) {
        this.custCat = custCat;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAccHeadCSI;
    private com.see.truetransact.uicomponent.CButton btnAccHeadDSI;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDeleteCSI;
    private com.see.truetransact.uicomponent.CButton btnDeleteDSI;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnHelpAccNoCSI;
    private com.see.truetransact.uicomponent.CButton btnHelpAccNoDSI;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnNewCSI;
    private com.see.truetransact.uicomponent.CButton btnNewDSI;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnSaveCSI;
    private com.see.truetransact.uicomponent.CButton btnSaveDSI;
    private com.see.truetransact.uicomponent.CButton btnSearchSI;
    private com.see.truetransact.uicomponent.CButton btnSearchSI1;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CButton btnstp;
    private com.see.truetransact.uicomponent.CComboBox cboExecConfig;
    private com.see.truetransact.uicomponent.CComboBox cboExecutionDay;
    private com.see.truetransact.uicomponent.CComboBox cboFrequencySI;
    private com.see.truetransact.uicomponent.CComboBox cboMoRSI;
    private com.see.truetransact.uicomponent.CComboBox cboProdIDCSI;
    private com.see.truetransact.uicomponent.CComboBox cboProdIDDSI;
    private com.see.truetransact.uicomponent.CComboBox cboProductTypeCSI;
    private com.see.truetransact.uicomponent.CComboBox cboProductTypeDSI;
    private com.see.truetransact.uicomponent.CComboBox cboSIType;
    private com.see.truetransact.uicomponent.CComboBox cboSpecificDate;
    private com.see.truetransact.uicomponent.CComboBox cboWeek;
    private com.see.truetransact.uicomponent.CComboBox cboWeekDay;
    private com.see.truetransact.uicomponent.CCheckBox chkCloseSI;
    private com.see.truetransact.uicomponent.CCheckBox chkPendingInstalment;
    private com.see.truetransact.uicomponent.CCheckBox chkSelectAll1;
    private com.see.truetransact.uicomponent.CCheckBox chkSuspendUser;
    private com.see.truetransact.uicomponent.CCheckBox chkinstalments;
    private com.see.truetransact.uicomponent.CDateField dtdEndDtSI;
    private com.see.truetransact.uicomponent.CDateField dtdExecDateProc;
    private com.see.truetransact.uicomponent.CDateField dtdStartDtSI;
    private com.see.truetransact.uicomponent.CDateField dtdSuspendDt;
    private com.see.truetransact.uicomponent.CLabel lbl1FwdExecutionDate;
    private com.see.truetransact.uicomponent.CLabel lbl1HolidayExecutionDate;
    private com.see.truetransact.uicomponent.CLabel lbl1LastExecutionDate;
    private com.see.truetransact.uicomponent.CLabel lbl1NextExecutionDate;
    private com.see.truetransact.uicomponent.CLabel lblAccHeadCSI;
    private com.see.truetransact.uicomponent.CLabel lblAccHeadDSI;
    private com.see.truetransact.uicomponent.CLabel lblAccNoCSI;
    private com.see.truetransact.uicomponent.CLabel lblAccNoDSI;
    private com.see.truetransact.uicomponent.CLabel lblAcceptanceCharges;
    private com.see.truetransact.uicomponent.CLabel lblAmountCSI;
    private com.see.truetransact.uicomponent.CLabel lblAmountDSI;
    private com.see.truetransact.uicomponent.CLabel lblAutomaticPosting;
    private com.see.truetransact.uicomponent.CLabel lblBeneficiarySI;
    private com.see.truetransact.uicomponent.CLabel lblCloseSI;
    private com.see.truetransact.uicomponent.CLabel lblCommChargesSI;
    private com.see.truetransact.uicomponent.CLabel lblCommSI;
    private com.see.truetransact.uicomponent.CLabel lblCrTotValue;
    private com.see.truetransact.uicomponent.CLabel lblCreditTotal;
    private com.see.truetransact.uicomponent.CLabel lblDbTotalValue;
    private com.see.truetransact.uicomponent.CLabel lblDebitTotal;
    private com.see.truetransact.uicomponent.CLabel lblEndDtSI;
    private com.see.truetransact.uicomponent.CLabel lblExecConfig;
    private com.see.truetransact.uicomponent.CLabel lblExecDateProc;
    private com.see.truetransact.uicomponent.CLabel lblExecutionCharges;
    private com.see.truetransact.uicomponent.CLabel lblExecutionDay;
    private com.see.truetransact.uicomponent.CLabel lblFailureCharges;
    private com.see.truetransact.uicomponent.CLabel lblFailureST;
    private com.see.truetransact.uicomponent.CLabel lblForwardCount;
    private com.see.truetransact.uicomponent.CLabel lblFrequencySI;
    private com.see.truetransact.uicomponent.CLabel lblFwdExecutionDate;
    private com.see.truetransact.uicomponent.CLabel lblGraceDaysSI;
    private com.see.truetransact.uicomponent.CLabel lblHolidayExecutionDate;
    private com.see.truetransact.uicomponent.CLabel lblInstalments;
    private com.see.truetransact.uicomponent.CLabel lblLastExecutionDate;
    private com.see.truetransact.uicomponent.CLabel lblMinBalSI;
    private com.see.truetransact.uicomponent.CLabel lblMoTSI;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblMultiplieSI;
    private com.see.truetransact.uicomponent.CLabel lblNameValueCSI;
    private com.see.truetransact.uicomponent.CLabel lblNameValueDSI;
    private com.see.truetransact.uicomponent.CLabel lblNextExecutionDate;
    private com.see.truetransact.uicomponent.CLabel lblNoofInstalments;
    private com.see.truetransact.uicomponent.CLabel lblParticularsCSI;
    private com.see.truetransact.uicomponent.CLabel lblParticularsDSI;
    private com.see.truetransact.uicomponent.CLabel lblProdIDCSI;
    private com.see.truetransact.uicomponent.CLabel lblProdIDDSI;
    private com.see.truetransact.uicomponent.CLabel lblProductTypeCSI;
    private com.see.truetransact.uicomponent.CLabel lblProductTypeDSI;
    private com.see.truetransact.uicomponent.CLabel lblRettCommChargesSI;
    private com.see.truetransact.uicomponent.CLabel lblRettCommSI;
    private com.see.truetransact.uicomponent.CLabel lblSIDt;
    private com.see.truetransact.uicomponent.CLabel lblSIHoliday;
    private com.see.truetransact.uicomponent.CLabel lblSINo;
    private com.see.truetransact.uicomponent.CLabel lblSIType;
    private com.see.truetransact.uicomponent.CLabel lblServiceTax;
    private com.see.truetransact.uicomponent.CLabel lblSpace;
    private com.see.truetransact.uicomponent.CLabel lblSpace17;
    private com.see.truetransact.uicomponent.CLabel lblSpace18;
    private com.see.truetransact.uicomponent.CLabel lblSpace19;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace20;
    private com.see.truetransact.uicomponent.CLabel lblSpace21;
    private com.see.truetransact.uicomponent.CLabel lblSpace22;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace6;
    private com.see.truetransact.uicomponent.CLabel lblSpace7;
    private com.see.truetransact.uicomponent.CLabel lblSpecificDate;
    private com.see.truetransact.uicomponent.CLabel lblStartDtSI;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblSuspDate;
    private com.see.truetransact.uicomponent.CLabel lblSuspendUser;
    private com.see.truetransact.uicomponent.CLabel lblWeek;
    private com.see.truetransact.uicomponent.CLabel lblWeekDay;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAccHead;
    private com.see.truetransact.uicomponent.CPanel panAccHeadDSI;
    private com.see.truetransact.uicomponent.CPanel panAccNumberCSI;
    private com.see.truetransact.uicomponent.CPanel panAccNumberNameDSI;
    private com.see.truetransact.uicomponent.CPanel panAutoPosting;
    private com.see.truetransact.uicomponent.CPanel panBatchProcess1;
    private com.see.truetransact.uicomponent.CPanel panCharges;
    private com.see.truetransact.uicomponent.CPanel panCheckBoxes1;
    private com.see.truetransact.uicomponent.CPanel panCommSI;
    private com.see.truetransact.uicomponent.CPanel panCrTotal;
    private com.see.truetransact.uicomponent.CPanel panCreditActDetails;
    private com.see.truetransact.uicomponent.CPanel panCreditDetails;
    private com.see.truetransact.uicomponent.CPanel panCreditOperations;
    private com.see.truetransact.uicomponent.CPanel panCreditSI;
    private com.see.truetransact.uicomponent.CPanel panDbTotal;
    private com.see.truetransact.uicomponent.CPanel panDebitActDetails;
    private com.see.truetransact.uicomponent.CPanel panDebitDetails;
    private com.see.truetransact.uicomponent.CPanel panDebitOperations;
    private com.see.truetransact.uicomponent.CPanel panDebitSI;
    private com.see.truetransact.uicomponent.CPanel panHolidayExecution;
    private com.see.truetransact.uicomponent.CPanel panMainSI;
    private com.see.truetransact.uicomponent.CPanel panMultiSearch;
    private com.see.truetransact.uicomponent.CPanel panNameCSI;
    private com.see.truetransact.uicomponent.CPanel panNameDSI;
    private com.see.truetransact.uicomponent.CPanel panProdIdAccHeadCSI;
    private com.see.truetransact.uicomponent.CPanel panProdIdAccHeadDSI;
    private com.see.truetransact.uicomponent.CPanel panRettCommSI;
    private com.see.truetransact.uicomponent.CPanel panSI;
    private com.see.truetransact.uicomponent.CPanel panSIID;
    private com.see.truetransact.uicomponent.CPanel panSIIDt;
    private com.see.truetransact.uicomponent.CPanel panSchedule;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CButtonGroup rdoCommSI;
    private com.see.truetransact.uicomponent.CRadioButton rdoCommSI_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoCommSI_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoHolidayExecution_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoHolidayExecution_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoRettCommSI;
    private com.see.truetransact.uicomponent.CRadioButton rdoRettCommSI_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoRettCommSI_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoSIAutoForward;
    private com.see.truetransact.uicomponent.CRadioButton rdoSIAutoPosting_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoSIAutoPosting_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoSIHolidayExecution;
    private com.see.truetransact.uicomponent.CScrollPane scrOutputMsg1;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private com.see.truetransact.uicomponent.CSeparator sptSpace2;
    private com.see.truetransact.uicomponent.CScrollPane srpCrebitSI;
    private com.see.truetransact.uicomponent.CScrollPane srpDebitSI;
    private com.see.truetransact.uicomponent.CTabbedPane tabDCDayBegin;
    private com.see.truetransact.uicomponent.CTabbedPane tabSIDetails;
    private com.see.truetransact.uicomponent.CTable tblCreditSI;
    private com.see.truetransact.uicomponent.CTable tblData;
    private com.see.truetransact.uicomponent.CTable tblDebitSI;
    private javax.swing.JToolBar tbrAdvances;
    private com.see.truetransact.uicomponent.CTextField txtAccHeadValueCSI;
    private com.see.truetransact.uicomponent.CTextField txtAccHeadValueDSI;
    private com.see.truetransact.uicomponent.CTextField txtAccNoCSI;
    private com.see.truetransact.uicomponent.CTextField txtAccNoDSI;
    private com.see.truetransact.uicomponent.CTextField txtAcceptanceCharges;
    private com.see.truetransact.uicomponent.CTextField txtAmountCSI;
    private com.see.truetransact.uicomponent.CTextField txtAmountDSI;
    private com.see.truetransact.uicomponent.CTextField txtBeneficiarySI;
    private com.see.truetransact.uicomponent.CTextField txtCommChargesSI;
    private com.see.truetransact.uicomponent.CTextField txtExecutionCharges;
    private com.see.truetransact.uicomponent.CTextField txtFailureCharges;
    private com.see.truetransact.uicomponent.CTextField txtFailureST;
    private com.see.truetransact.uicomponent.CTextField txtForwardCount;
    private com.see.truetransact.uicomponent.CTextField txtGraceDaysSI;
    private com.see.truetransact.uicomponent.CTextField txtMinBalSI;
    private com.see.truetransact.uicomponent.CTextField txtMultiplieSI;
    private com.see.truetransact.uicomponent.CTextField txtNoofInstalments;
    private com.see.truetransact.uicomponent.CTextField txtParticularsCSI;
    private com.see.truetransact.uicomponent.CTextField txtParticularsDSI;
    private com.see.truetransact.uicomponent.CTextField txtRettCommChargesSI;
    private com.see.truetransact.uicomponent.CTextField txtSINo;
    private com.see.truetransact.uicomponent.CTextField txtServiceTax;
    private com.see.truetransact.uicomponent.CTextField txtSiDt;
    // End of variables declaration//GEN-END:variables
}
