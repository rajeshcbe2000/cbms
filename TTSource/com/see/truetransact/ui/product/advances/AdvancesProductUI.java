/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AdvancesProductUI.java
 *
 * Created on November 19, 2003, 5:05 PM
 */

package com.see.truetransact.ui.product.advances;

import java.util.Observer;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.TableModel;

import  com.see.truetransact.commonutil.CommonConstants;
import  com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientutil.ComboBoxModel;
//__ To add and Remove the Radio Buttons...
import com.see.truetransact.uicomponent.CButtonGroup;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uivalidation.PercentageValidation;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uicomponent.CTextField;
import com.see.truetransact.uicomponent.CComboBox;

import org.apache.log4j.Logger;

import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.TrueTransactMain;

/**
 *
 * @author  Hemant
 */

public class AdvancesProductUI extends CInternalFrame implements Observer, UIMandatoryField {
    
    private AdvancesProductOB observable;
    private HashMap mandatoryMap;
    //    private AdvancesProductRB resourceBundle = new AdvancesProductRB();
    
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.product.advances.AdvancesProductRB", ProxyParameters.LANGUAGE);
    private AdvancesProductMRB objMandatoryRB;
    
    //    private String viewType;
    //    private final String AUTHORIZE = "AUTHORIZE";
    private boolean isFilled = false;
    
    final int EDIT=0, DELETE=1, AUTHORIZE=2, INFO_IMAGE=1;
    private final int AccountHead=3, AcctClosingChrg=4, MiscServChrg=5, StatChrg=6,
    AcctDIHead=7, PenalIntHead=8, AcctCIHead=9, ClearingIntAcctHead=10,
    ExpIntHead=11, ExcessOLHead=12, ChqBookIssChrgHead=13, StopPaymentChrgHead=14,
    ChqRetInwardChrgHead=15, ChqRetOutwardChrgHead=16, FolioChrgHead=17, PRODID=18;
    
    int viewType=-1;
    
    private EnhancedTableModel tbmCRChargesInward ;
    private EnhancedTableModel tbmCRChargesOutward ;
    private EnhancedTableModel tbmMiscItemSI;
    
    private final static Logger log = Logger.getLogger(AdvancesProductUI.class);
    
    /** Creates new form CreateProductAdvances */
    public AdvancesProductUI(HashMap dataMap) {
        settingupUI(dataMap);
        //show();
    }
    
    private void settingupUI(HashMap dataMap){
        initComponents();
        setObservable();
        initComponentData();
        setTableModel();
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setHelpMessage();
        setMaxLenght();
        //        rdosDefaultValues();
        //__ To Set the Field as Invisible...
        setFieldsInVisible();
        setPanInvisible();
        setButtonVisible(false);
        
        setInitialize(dataMap);
        
        setProdEnable();
        tabProduct.resetVisits();
    }
    
    
    private void setButtonVisible(boolean value){
        btnNew.setVisible(value);
        mitNew.setVisible(value);
        
        btnEdit.setVisible(value);
        mitEdit.setVisible(value);
        
        btnDelete.setVisible(value);
        mitDelete.setVisible(value);
        
        btnPrint.setVisible(value);
        
        btnAuthorize.setVisible(value);
        btnReject.setVisible(value);
        btnException.setVisible(value);
        
        btnProdID.setVisible(value);
        
        lblAccountHead.setVisible(value);
        txtAccountHeadAccount.setVisible(value);
        btnAccountHeadAccountHelp.setVisible(value);
    }
    
    private void setInitialize(HashMap dataMap){
        //__ To set the Mode in the Screen...
        if(CommonUtil.convertObjToInt(dataMap.get("ACTION_TYPE")) == ClientConstants.ACTIONTYPE_NEW){ //__ New...
            btnNewActionPerformed(null);
            txtProductIdAccount.setText(CommonUtil.convertObjToStr(dataMap.get("PROD_ID")));
            txtProductDescAccount.setText(CommonUtil.convertObjToStr(dataMap.get("PROD_DESC")));
            
        }else if(CommonUtil.convertObjToInt(dataMap.get("ACTION_TYPE")) == ClientConstants.ACTIONTYPE_EDIT){ //__ Edit...
            viewType = EDIT;
            observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
            fillData(dataMap);
            
            //__ if no data Exists... Call New...
            if(!(CommonUtil.convertObjToStr(txtProductIdAccount.getText()).length() > 0)){
                displayInfo(resourceBundle.getString("EDIT_WARNING"));
                btnNewActionPerformed(null);
                
                txtProductIdAccount.setText(CommonUtil.convertObjToStr(dataMap.get("PROD_ID")));
//                observable.setTxtProductIdAccount(CommonUtil.convertObjToStr(dataMap.get("PROD_ID")));
                
                txtProductDescAccount.setText(CommonUtil.convertObjToStr(dataMap.get("PROD_DESC")));
//                observable.setTxtProductDescAccount(CommonUtil.convertObjToStr(dataMap.get("PROD_DESC")));
            }
            
        }else if(CommonUtil.convertObjToInt(dataMap.get("ACTION_TYPE")) == ClientConstants.ACTIONTYPE_DELETE){ //__ Delete...
            viewType = DELETE;
            observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
            fillData(dataMap);
            
            btnSave.setEnabled(false);
        }
        else if(CommonUtil.convertObjToInt(dataMap.get("ACTION_TYPE")) == ClientConstants.ACTIONTYPE_AUTHORIZE){ //__ Delete...
            viewType = AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            fillData(dataMap);
            
            btnSave.setEnabled(false);
        }
        else if(CommonUtil.convertObjToInt(dataMap.get("ACTION_TYPE")) == ClientConstants.ACTIONTYPE_REJECT){ //__ Delete...
            viewType = AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
            fillData(dataMap);
            
            btnSave.setEnabled(false);
        }
    }
    
    private void setTableModel(){
        tblMiscItemSI.setModel(tbmMiscItemSI);
        tblCRChargesOutward.setModel(tbmCRChargesOutward);
        tblCRChargesInward.setModel(tbmCRChargesInward);
    }
    private void setFieldNames() {
        btnACCAHHelp.setName("btnACCAHHelp");
        btnAccountHeadAccountHelp.setName("btnAccountHeadAccountHelp");
        btnAgCIAHHelp.setName("btnAgCIAHHelp");
        btnCIAHHelp.setName("btnCIAHHelp");
        btnCICAHHelp.setName("btnCICAHHelp");
        btnCRCInwardAHHelp.setName("btnCRCInwardAHHelp");
        btnCRCoutwardAHHelp.setName("btnCRCoutwardAHHelp");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDIAHHelp.setName("btnDIAHHelp");
        btnDelete.setName("btnDelete");
        btnDeleteCRIC.setName("btnDeleteCRIC");
        btnDeleteCROC.setName("btnDeleteCROC");
        btnDeleteSI.setName("btnDeleteSI");
        btnEIAHHelp.setName("btnEIAHHelp");
        btnEdit.setName("btnEdit");
        btnExOLHAHelp.setName("btnExOLHAHelp");
        btnFCAHHelp.setName("btnFCAHHelp");
        btnMSCAHHelp.setName("btnMSCAHHelp");
        btnNew.setName("btnNew");
        btnNewCRIC.setName("btnNewCRIC");
        btnNewCROC.setName("btnNewCROC");
        btnNewSI.setName("btnNewSI");
        btnPIAHHelp.setName("btnPIAHHelp");
        btnPrint.setName("btnPrint");
        btnSCAHHelp.setName("btnSCAHHelp");
        btnSPCAHHelp.setName("btnSPCAHHelp");
        btnSave.setName("btnSave");
        btnSaveCRIC.setName("btnSaveCRIC");
        btnSaveCROC.setName("btnSaveCROC");
        btnSaveSI.setName("btnSaveSI");
        cMenuBar1.setName("cMenuBar1");
        cMenuBar2.setName("cMenuBar2");
        cboAmountCRIC.setName("cboAmountCRIC");
        cboAmountCROC.setName("cboAmountCROC");
        cboAssetsSI.setName("cboAssetsSI");
        cboBehavesLike.setName("cboBehavesLike");
        cboCAFrequencyFCharges.setName("cboCAFrequencyFCharges");
        cboCIROIP.setName("cboCIROIP");
        cboCLPeriod.setName("cboCLPeriod");
        cboCPROIP.setName("cboCPROIP");
        cboCalcCtriteriaIP.setName("cboCalcCtriteriaIP");
        cboChargeOnDocFCharges.setName("cboChargeOnDocFCharges");
        cboChargeOnTransactionFCharges.setName("cboChargeOnTransactionFCharges");
        cboCollectChargeFCharges.setName("cboCollectChargeFCharges");
        cboCommitmentCharges.setName("cboCommitmentCharges");
        cboCreditInterestAFIP.setName("cboCreditInterestAFIP");
        cboCreditInterestCFIP.setName("cboCreditInterestCFIP");
        cboCreditInterestCompdFIP.setName("cboCreditInterestCompdFIP");
        cboDIApplicationFIR.setName("cboDIApplicationFIR");
        cboDICalculationFIR.setName("cboDICalculationFIR");
        cboDICompoundFIR.setName("cboDICompoundFIR");
        cboDIRoundOffIR.setName("cboDIRoundOffIR");
        cboDPRoundOffIR.setName("cboDPRoundOffIR");
        cboIRFrequencyFCharges.setName("cboIRFrequencyFCharges");
        cboProcessingCharges.setName("cboProcessingCharges");
        cboProdFrequencyIP.setName("cboProdFrequencyIP");
        cboProductFOthersIR.setName("cboProductFOthersIR");
        cboStmtFrequency.setName("cboStmtFrequency");
        tdtAccountSFPLRIR.setName("tdtAccountSFPLRIR");
        tdtAppliedFromPLRIR.setName("tdtAppliedFromPLRIR");
        tdtCLStartAccount.setName("tdtCLStartAccount");
        tdtDueDateFCharges.setName("tdtDueDateFCharges");
        tdtInterestADDebitIR.setName("tdtInterestADDebitIR");
        tdtInterestCDDebitIR.setName("tdtInterestCDDebitIR");
        tdtLastADIP.setName("tdtLastADIP");
        tdtLastAppliedFCharges.setName("tdtLastAppliedFCharges");
        tdtLastCDIP.setName("tdtLastCDIP");
        lblACA.setName("lblACA");
        lblACCAH.setName("lblACCAH");
        lblAIRStaffIP.setName("lblAIRStaffIP");
        lblAIStaffIP.setName("lblAIStaffIP");
        lblATMCardSI.setName("lblATMCardSI");
        lblAccountCLosingCharges.setName("lblAccountCLosingCharges");
        lblAccountHead.setName("lblAccountHead");
        lblAccountSFPLRIR.setName("lblAccountSFPLRIR");
        lblAddIntRateIP.setName("lblAddIntRateIP");
        lblAgCIAH.setName("lblAgCIAH");
        lblAmountCRIC.setName("lblAmountCRIC");
        lblAmountCROC.setName("lblAmountCROC");
        lblAppliedFromPLRIR.setName("lblAppliedFromPLRIR");
        lblAssetsSI.setName("lblAssetsSI");
        lblBehaveLike.setName("lblBehaveLike");
        lblBranchBankingSI.setName("lblBranchBankingSI");
        lblCAFrequencyFCharges.setName("lblCAFrequencyFCharges");
        lblCIAH.setName("lblCIAH");
        lblCICAH.setName("lblCICAH");
        lblCIROIP.setName("lblCIROIP");
        lblCLPeriod.setName("lblCLPeriod");
        lblCLStart.setName("lblCLStart");
        lblCPROIP.setName("lblCPROIP");
        lblCRCInwardAH.setName("lblCRCInwardAH");
        lblCRCOutwardAH.setName("lblCRCOutwardAH");
        lblCalcCriteriaIP.setName("lblCalcCriteriaIP");
        lblChargeOnDocFCharges.setName("lblChargeOnDocFCharges");
        lblChargeOnTransactionFCharges.setName("lblChargeOnTransactionFCharges");
        lblChargedDIIR.setName("lblChargedDIIR");
        lblChequeLeaves.setName("lblChequeLeaves");
        lblCollectChargeFCharges.setName("lblCollectChargeFCharges");
        lblCrediInterestAFIP.setName("lblCrediInterestAFIP");
        lblCreditCardSI.setName("lblCreditCardSI");
        lblCreditCompdIP.setName("lblCreditCompdIP");
        lblCreditInterestCFIP.setName("lblCreditInterestCFIP");
        lblCreditInterestCompdFIP.setName("lblCreditInterestCompdFIP");
        lblCreditInterestIP.setName("lblCreditInterestIP");
        lblCreditInterestOnUE.setName("lblCreditInterestOnUE");
        lblCreditInterestRateIIP.setName("lblCreditInterestRateIIP");
        lblDCRequiredIR.setName("lblDCRequiredIR");
        lblDIAH.setName("lblDIAH");
        lblDIApplicationFIR.setName("lblDIApplicationFIR");
        lblDICalculationFIR.setName("lblDICalculationFIR");
        lblDICompoundFIR.setName("lblDICompoundFIR");
        lblDIRoundOffIR.setName("lblDIRoundOffIR");
        lblDPRoundOffIR.setName("lblDPRoundOffIR");
        lblDebitCardSI.setName("lblDebitCardSI");
        lblDebitInterestOnDAUE.setName("lblDebitInterestOnDAUE");
        lblDueDateFCharges.setName("lblDueDateFCharges");
        lblEIAH.setName("lblEIAH");
        lblEOLOthersIR.setName("lblEOLOthersIR");
        lblExOLHAH.setName("lblExOLHAH");
        lblExistingAccountPLRIR.setName("lblExistingAccountPLRIR");
        lblFCAH.setName("lblFCAH");
        lblFolioEntriesFCharges.setName("lblFolioEntriesFCharges");
        lblIRFrequencyFCharges.setName("lblIRFrequencyFCharges");
        lblInterestADDebitIR.setName("lblInterestADDebitIR");
        lblInterestCDDebitIR.setName("lblInterestCDDebitIR");
        lblIsApplicableFCharges.setName("lblIsApplicableFCharges");
        lblIsApplicablePLRIR.setName("lblIsApplicablePLRIR");
        lblIsChequebookCharges.setName("lblIsChequebookCharges");
        lblIsCommitmentCharges.setName("lblIsCommitmentCharges");
        lblIsProcessingCharges.setName("lblIsProcessingCharges");
        lblIsStatementCharges.setName("lblIsStatementCharges");
        lblIsStopPaymentCharges.setName("lblIsStopPaymentCharges");
        lblLastADIP.setName("lblLastADIP");
        lblLastAccNo.setName("lblLastAccNo");
        lblLastAppliedFCharges.setName("lblLastAppliedFCharges");
        lblLastCDIP.setName("lblLastCDIP");
        lblLimitDefination.setName("lblLimitDefination");
        lblLimitEIOthersIR.setName("lblLimitEIOthersIR");
        lblMD4EOL.setName("lblMD4EOL");
        lblMSCAH.setName("lblMSCAH");
        lblMaxAmount4WS.setName("lblMaxAmount4WS");
        lblMaxDIAmtIR.setName("lblMaxDIAmtIR");
        lblMaxDIRateIR.setName("lblMaxDIRateIR");
        lblMicsServiceCharges.setName("lblMicsServiceCharges");
        lblMinDIAmtIR.setName("lblMinDIAmtIR");
        lblMinDIRateIR.setName("lblMinDIRateIR");
        lblMobileBankingClientSI.setName("lblMobileBankingClientSI");
        lblNewAccountPLRIR.setName("lblNewAccountPLRIR");
        lblNumberPattern.setName("lblNumberPattern");
        lblODOverAboveLimimt.setName("lblODOverAboveLimimt");
        lblPIAH.setName("lblPIAH");
        lblPSCommitmentCharges.setName("lblPSCommitmentCharges");
        lblPSProcessingCharges.setName("lblPSProcessingCharges");
        lblPenalIROthersIR.setName("lblPenalIROthersIR");
        lblPenalOthersIR.setName("lblPenalOthersIR");
        lblPercentageCreditInterestRateIP.setName("lblPercentageCreditInterestRateIP");
        lblPercentageMDAccount.setName("lblPercentageMDAccount");
        lblPercentageMaxDIRateIR.setName("lblPercentageMaxDIRateIR");
        lblPercentageMinDIRateIR.setName("lblPercentageMinDIRateIR");
        lblPercentagePenalIROthersIR.setName("lblPercentagePenalIROthersIR");
        lblPercentageRatePLRIR.setName("lblPercentageRatePLRIR");
        lblProcessingCharges.setName("lblProcessingCharges");
        lblProdDesc.setName("lblProdDesc");
        lblProdFrequencyIP.setName("lblProdFrequencyIP");
        lblProdId.setName("lblProdId");
        lblProductFOthersIR.setName("lblProductFOthersIR");
        lblRateCRIC.setName("lblRateCRIC");
        lblRateCROC.setName("lblRateCROC");
        lblRateFCharges.setName("lblRateFCharges");
        lblRatePLRIR.setName("lblRatePLRIR");
        lblRateSI.setName("lblRateSI");
        lblSCAH.setName("lblSCAH");
        lblSPCAH.setName("lblSPCAH");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblStaffAccOpened.setName("lblStaffAccOpened");
        lblStateFrequency.setName("lblStateFrequency");
        lblStatementCharges.setName("lblStatementCharges");
        lblToken.setName("lblToken");
        lblUAICOthersIR.setName("lblUAICOthersIR");
        lblWithdrawalSlip.setName("lblWithdrawalSlip");
        mbrMain.setName("mbrMain");
        panACAI2Account.setName("panACAI2Account");
        panAccount.setName("panAccount");
        panAccountHead.setName("panAccountHead");
        panAccountHeadAccount.setName("panAccountHeadAccount");
        panAddInterestIP.setName("panAddInterestIP");
        panAddInterestRateIP.setName("panAddInterestRateIP");
        panButtonSI.setName("panButtonSI");
        panButtonsCRIC.setName("panButtonsCRIC");
        panButtonsCROC.setName("panButtonsCROC");
        panCIUEI2Account.setName("panCIUEI2Account");
        panCLPI2Account.setName("panCLPI2Account");
        panCRChargesInward.setName("panCRChargesInward");
        panCRChargesOutward.setName("panCRChargesOutward");
        panChagres.setName("panChagres");
        panChargedDIIR.setName("panChargedDIIR");
        panCommitmentCharges.setName("panCommitmentCharges");
        panCreditCompdIP.setName("panCreditCompdIP");
        panCreditInterestIP.setName("panCreditInterestIP");
        panCreditInterestRateIP.setName("panCreditInterestRateIP");
        panDIUEI2Account.setName("panDIUEI2Account");
        panDebitCompoundIR.setName("panDebitCompoundIR");
        panDebitInterestIR.setName("panDebitInterestIR");
        panFolioCharges.setName("panFolioCharges");
        panInsideAccount.setName("panInsideAccount");
        panInterestPayable.setName("panInterestPayable");
        panInterestReceivable.setName("panInterestReceivable");
        panIsApplicableFCharges.setName("panIsApplicableFCharges");
        panIsApplicablePLRIR.setName("panIsApplicablePLRIR");
        panIsChequebookCharges.setName("panIsChequebookCharges");
        panIsCommitmentCharges.setName("panIsCommitmentCharges");
        panIsProcessingCharges.setName("panIsProcessingCharges");
        panIsStatementCharges.setName("panIsStatementCharges");
        panIsStopPaymentCharges.setName("panIsStopPaymentCharges");
        panLDI2Account.setName("panLDI2Account");
        panManagerDistAccount.setName("panManagerDistAccount");
        panMaxDIRateIR.setName("panMaxDIRateIR");
        panMinDIRateIR.setName("panMinDIRateIR");
        panMiscItemSI.setName("panMiscItemSI");
        panODALI2Account.setName("panODALI2Account");
        panOtherCharges.setName("panOtherCharges");
        panOthersIR.setName("panOthersIR");
        panPLRIR.setName("panPLRIR");
        panProcessingCharges.setName("panProcessingCharges");
        panProductAccount.setName("panProductAccount");
        panSAOI2Account.setName("panSAOI2Account");
        panSplItemDetails.setName("panSplItemDetails");
        panSplItems.setName("panSplItems");
        panTokenI2Account.setName("panTokenI2Account");
        panWSI2Account.setName("panWSI2Account");
        rdoACAAccount_No.setName("rdoACAAccount_No");
        rdoACAAccount_Yes.setName("rdoACAAccount_Yes");
        rdoATMCardSI_No.setName("rdoATMCardSI_No");
        rdoATMCardSI_Yes.setName("rdoATMCardSI_Yes");
        rdoAdditionalIntInterestPayable_No.setName("rdoAdditionalIntInterestPayable_No");
        rdoAdditionalIntInterestPayable_Yes.setName("rdoAdditionalIntInterestPayable_Yes");
        rdoBranchBankingSI_No.setName("rdoBranchBankingSI_No");
        rdoBranchBankingSI_Yes.setName("rdoBranchBankingSI_Yes");
        rdoCIUEAccount_No.setName("rdoCIUEAccount_No");
        rdoCIUEAccount_Yes.setName("rdoCIUEAccount_Yes");
        rdoChargedDIIR_No.setName("rdoChargedDIIR_No");
        rdoChargedDIIR_Yes.setName("rdoChargedDIIR_Yes");
        rdoCreditCardSI_No.setName("rdoCreditCardSI_No");
        rdoCreditCardSI_Yes.setName("rdoCreditCardSI_Yes");
        rdoCreditCompdInterestPayable_No.setName("rdoCreditCompdInterestPayable_No");
        rdoCreditCompdInterestPayable_Yes.setName("rdoCreditCompdInterestPayable_Yes");
        rdoCreditIntInterestPayable_No.setName("rdoCreditIntInterestPayable_No");
        rdoCreditIntInterestPayable_Yes.setName("rdoCreditIntInterestPayable_Yes");
        rdoDIAUEAccount_No.setName("rdoDIAUEAccount_No");
        rdoDIAUEAccount_Yes.setName("rdoDIAUEAccount_Yes");
        rdoDebitCardSI_No.setName("rdoDebitCardSI_No");
        rdoDebitCardSI_Yes.setName("rdoDebitCardSI_Yes");
        rdoDebitCompoundIR_No.setName("rdoDebitCompoundIR_No");
        rdoDebitCompoundIR_Yes.setName("rdoDebitCompoundIR_Yes");
        rdoEOLOthersIR_No.setName("rdoEOLOthersIR_No");
        rdoEOLOthersIR_Yes.setName("rdoEOLOthersIR_Yes");
        rdoExistingAccountPLRIR_No.setName("rdoExistingAccountPLRIR_No");
        rdoExistingAccountPLRIR_Yes.setName("rdoExistingAccountPLRIR_Yes");
        rdoIsApplicableFCharges_No.setName("rdoIsApplicableFCharges_No");
        rdoIsApplicableFCharges_Yes.setName("rdoIsApplicableFCharges_Yes");
        rdoIsApplicablePLRIR_No.setName("rdoIsApplicablePLRIR_No");
        rdoIsApplicablePLRIR_Yes.setName("rdoIsApplicablePLRIR_Yes");
        rdoIsChequebookCharges_No.setName("rdoIsChequebookCharges_No");
        rdoIsChequebookCharges_Yes.setName("rdoIsChequebookCharges_Yes");
        rdoIsCommitmentCharges_No.setName("rdoIsCommitmentCharges_No");
        rdoIsCommitmentCharges_Yes.setName("rdoIsCommitmentCharges_Yes");
        rdoIsProcessingCharges_No.setName("rdoIsProcessingCharges_No");
        rdoIsProcessingCharges_Yes.setName("rdoIsProcessingCharges_Yes");
        rdoIsStatementCharges_No.setName("rdoIsStatementCharges_No");
        rdoIsStatementCharges_Yes.setName("rdoIsStatementCharges_Yes");
        rdoIsStopPaymentCharges_No.setName("rdoIsStopPaymentCharges_No");
        rdoIsStopPaymentCharges_Yes.setName("rdoIsStopPaymentCharges_Yes");
        rdoLDAccount_No.setName("rdoLDAccount_No");
        rdoLDAccount_Yes.setName("rdoLDAccount_Yes");
        rdoLimitEIOthersIR_No.setName("rdoLimitEIOthersIR_No");
        rdoLimitEIOthersIR_Yes.setName("rdoLimitEIOthersIR_Yes");
        rdoMobileBankingClientSI_No.setName("rdoMobileBankingClientSI_No");
        rdoMobileBankingClientSI_Yes.setName("rdoMobileBankingClientSI_Yes");
        rdoNewAccountPLRIR_No.setName("rdoNewAccountPLRIR_No");
        rdoNewAccountPLRIR_Yes.setName("rdoNewAccountPLRIR_Yes");
        rdoODALAccount_No.setName("rdoODALAccount_No");
        rdoODALAccount_Yes.setName("rdoODALAccount_Yes");
        rdoPenalOthersIR_No.setName("rdoPenalOthersIR_No");
        rdoPenalOthersIR_Yes.setName("rdoPenalOthersIR_Yes");
        rdoSAOAccount_No.setName("rdoSAOAccount_No");
        rdoSAOAccount_Yes.setName("rdoSAOAccount_Yes");
        rdoTokanAccount_No.setName("rdoTokanAccount_No");
        rdoTokanAccount_Yes.setName("rdoTokanAccount_Yes");
        rdoUAICOthersIR_No.setName("rdoUAICOthersIR_No");
        rdoUAICOthersIR_Yes.setName("rdoUAICOthersIR_Yes");
        rdoWSAccount_No.setName("rdoWSAccount_No");
        rdoWSAccount_Yes.setName("rdoWSAccount_Yes");
        sptProductAccount.setName("sptProductAccount");
        srpCRChargesInward.setName("srpCRChargesInward");
        srpCRChargesOutward.setName("srpCRChargesOutward");
        srpMiscItemSI.setName("srpMiscItemSI");
        tabProduct.setName("tabProduct");
        tblCRChargesInward.setName("tblCRChargesInward");
        tblCRChargesOutward.setName("tblCRChargesOutward");
        tblMiscItemSI.setName("tblMiscItemSI");
        txtACCAH.setName("txtACCAH");
        txtAccountClosingCharges.setName("txtAccountClosingCharges");
        txtAccountHeadAccount.setName("txtAccountHeadAccount");
        txtAddIntRateIP.setName("txtAddIntRateIP");
        txtAgCIAH.setName("txtAgCIAH");
        txtCIAH.setName("txtCIAH");
        txtCICAH.setName("txtCICAH");
        txtCLPeriodAccount.setName("txtCLPeriodAccount");
        txtCRCInwardAH.setName("txtCRCInwardAH");
        txtCRCoutwardAH.setName("txtCRCoutwardAH");
        txtChequebookCharges.setName("txtChequebookCharges");
        txtCommitmentCharges.setName("txtCommitmentCharges");
        txtCreditInterestRateIP.setName("txtCreditInterestRateIP");
        txtDIAH.setName("txtDIAH");
        txtEIAH.setName("txtEIAH");
        txtExOLHAH.setName("txtExOLHAH");
        txtFCAH.setName("txtFCAH");
        txtFolioEntriesFCharges.setName("txtFolioEntriesFCharges");
        txtFreeCLAccount.setName("txtFreeCLAccount");
        txtLastAccNoAccount.setName("txtLastAccNoAccount");
        txtMSCAH.setName("txtMSCAH");
        txtManagerDistAccount.setName("txtManagerDistAccount");
        txtMaxAmountOnWS.setName("txtMaxAmountOnWS");
        txtMaxDIAmtIR.setName("txtMaxDIAmtIR");
        txtMaxDIRateIR.setName("txtMaxDIRateIR");
        txtMinDIAmtIR.setName("txtMinDIAmtIR");
        txtMinDIRateIR.setName("txtMinDIRateIR");
        txtMiscServiceCharges.setName("txtMiscServiceCharges");
        txtNumberpatternAccount.setName("txtNumberpatternAccount");
        txtPIAH.setName("txtPIAH");
        txtPenalIROthersIR.setName("txtPenalIROthersIR");
        txtProcessingCharges.setName("txtProcessingCharges");
        txtProductDescAccount.setName("txtProductDescAccount");
        txtProductIdAccount.setName("txtProductIdAccount");
        txtRateCRIC.setName("txtRateCRIC");
        txtRateCROC.setName("txtRateCROC");
        txtRateFCharges.setName("txtRateFCharges");
        txtRatePLRIR.setName("txtRatePLRIR");
        txtRateSI.setName("txtRateSI");
        txtSCAH.setName("txtSCAH");
        txtSPCAH.setName("txtSPCAH");
        txtStatementCharges.setName("txtStatementCharges");
        txtStopPaymentCharges.setName("txtStopPaymentCharges");
        cboProdCurrency.setName("cboProdCurrency");
        lblCreditInterestAmt.setName("lblCreditInterestAmt");
        txtCreditInterestAmt.setName("txtCreditInterestAmt");
        panChagres.setName("panChagres");
        panMiscdata.setName("panMiscdata");
        panSpecialItems.setName("panSpecialItems");
        panActHead.setName("panActHead");
    }
    
    
    
    private void internationalize() {
        rdoCreditCompdInterestPayable_Yes.setText(resourceBundle.getString("rdoCreditCompdInterestPayable_Yes"));
        lblLastCDIP.setText(resourceBundle.getString("lblLastCDIP"));
        lblCreditCardSI.setText(resourceBundle.getString("lblCreditCardSI"));
        rdoDIAUEAccount_Yes.setText(resourceBundle.getString("rdoDIAUEAccount_Yes"));
        rdoTokanAccount_No.setText(resourceBundle.getString("rdoTokanAccount_No"));
        lblRateCROC.setText(resourceBundle.getString("lblRateCROC"));
        lblCalcCriteriaIP.setText(resourceBundle.getString("lblCalcCriteriaIP"));
        rdoBranchBankingSI_No.setText(resourceBundle.getString("rdoBranchBankingSI_No"));
        lblPSProcessingCharges.setText(resourceBundle.getString("lblPSProcessingCharges"));
        lblChequeLeaves.setText(resourceBundle.getString("lblChequeLeaves"));
        lblRateFCharges.setText(resourceBundle.getString("lblRateFCharges"));
        btnDeleteCRIC.setText(resourceBundle.getString("btnDeleteCRIC"));
        lblNewAccountPLRIR.setText(resourceBundle.getString("lblNewAccountPLRIR"));
        rdoSAOAccount_No.setText(resourceBundle.getString("rdoSAOAccount_No"));
        lblDIRoundOffIR.setText(resourceBundle.getString("lblDIRoundOffIR"));
        rdoDebitCompoundIR_No.setText(resourceBundle.getString("rdoDebitCompoundIR_No"));
        lblBranchBankingSI.setText(resourceBundle.getString("lblBranchBankingSI"));
        lblChargeOnDocFCharges.setText(resourceBundle.getString("lblChargeOnDocFCharges"));
        lblCreditInterestRateIIP.setText(resourceBundle.getString("lblCreditInterestRateIIP"));
        lblLastAccNo.setText(resourceBundle.getString("lblLastAccNo"));
        lblCLPeriod.setText(resourceBundle.getString("lblCLPeriod"));
        rdoChargedDIIR_Yes.setText(resourceBundle.getString("rdoChargedDIIR_Yes"));
        lblIsStopPaymentCharges.setText(resourceBundle.getString("lblIsStopPaymentCharges"));
        btnSPCAHHelp.setText(resourceBundle.getString("btnSPCAHHelp"));
        rdoBranchBankingSI_Yes.setText(resourceBundle.getString("rdoBranchBankingSI_Yes"));
        btnEIAHHelp.setText(resourceBundle.getString("btnEIAHHelp"));
        lblAIStaffIP.setText(resourceBundle.getString("lblAIStaffIP"));
        lblCPROIP.setText(resourceBundle.getString("lblCPROIP"));
        rdoTokanAccount_Yes.setText(resourceBundle.getString("rdoTokanAccount_Yes"));
        rdoCreditIntInterestPayable_No.setText(resourceBundle.getString("rdoCreditIntInterestPayable_No"));
        lblLastADIP.setText(resourceBundle.getString("lblLastADIP"));
        lblPenalOthersIR.setText(resourceBundle.getString("lblPenalOthersIR"));
        lblIsProcessingCharges.setText(resourceBundle.getString("lblIsProcessingCharges"));
        lblDICompoundFIR.setText(resourceBundle.getString("lblDICompoundFIR"));
        lblCreditCompdIP.setText(resourceBundle.getString("lblCreditCompdIP"));
        lblEIAH.setText(resourceBundle.getString("lblEIAH"));
        ((javax.swing.border.TitledBorder)panCRChargesInward.getBorder()).setTitle(resourceBundle.getString("panCRChargesInward"));
        ((javax.swing.border.TitledBorder)panSplItemDetails.getBorder()).setTitle(resourceBundle.getString("panSplItemDetails"));
        lblAppliedFromPLRIR.setText(resourceBundle.getString("lblAppliedFromPLRIR"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        btnMSCAHHelp.setText(resourceBundle.getString("btnMSCAHHelp"));
        lblMaxAmount4WS.setText(resourceBundle.getString("lblMaxAmount4WS"));
        rdoIsCommitmentCharges_Yes.setText(resourceBundle.getString("rdoIsCommitmentCharges_Yes"));
        lblCreditInterestIP.setText(resourceBundle.getString("lblCreditInterestIP"));
        rdoIsApplicablePLRIR_No.setText(resourceBundle.getString("rdoIsApplicablePLRIR_No"));
        lblMicsServiceCharges.setText(resourceBundle.getString("lblMicsServiceCharges"));
        rdoMobileBankingClientSI_Yes.setText(resourceBundle.getString("rdoMobileBankingClientSI_Yes"));
        lblFCAH.setText(resourceBundle.getString("lblFCAH"));
        rdoLimitEIOthersIR_Yes.setText(resourceBundle.getString("rdoLimitEIOthersIR_Yes"));
        lblPenalIROthersIR.setText(resourceBundle.getString("lblPenalIROthersIR"));
        rdoSAOAccount_Yes.setText(resourceBundle.getString("rdoSAOAccount_Yes"));
        lblCreditInterestCompdFIP.setText(resourceBundle.getString("lblCreditInterestCompdFIP"));
        rdoLDAccount_Yes.setText(resourceBundle.getString("rdoLDAccount_Yes"));
        lblDIAH.setText(resourceBundle.getString("lblDIAH"));
        lblDebitCardSI.setText(resourceBundle.getString("lblDebitCardSI"));
        lblExOLHAH.setText(resourceBundle.getString("lblExOLHAH"));
        rdoATMCardSI_No.setText(resourceBundle.getString("rdoATMCardSI_No"));
        rdoIsApplicablePLRIR_Yes.setText(resourceBundle.getString("rdoIsApplicablePLRIR_Yes"));
        lblIsCommitmentCharges.setText(resourceBundle.getString("lblIsCommitmentCharges"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        ((javax.swing.border.TitledBorder)panFolioCharges.getBorder()).setTitle(resourceBundle.getString("panFolioCharges"));
        rdoMobileBankingClientSI_No.setText(resourceBundle.getString("rdoMobileBankingClientSI_No"));
        lblMaxDIRateIR.setText(resourceBundle.getString("lblMaxDIRateIR"));
        ((javax.swing.border.TitledBorder)panDebitInterestIR.getBorder()).setTitle(resourceBundle.getString("panDebitInterestIR"));
        lblIRFrequencyFCharges.setText(resourceBundle.getString("lblIRFrequencyFCharges"));
        lblStatementCharges.setText(resourceBundle.getString("lblStatementCharges"));
        rdoExistingAccountPLRIR_Yes.setText(resourceBundle.getString("rdoExistingAccountPLRIR_Yes"));
        rdoCIUEAccount_Yes.setText(resourceBundle.getString("rdoCIUEAccount_Yes"));
        rdoIsChequebookCharges_Yes.setText(resourceBundle.getString("rdoIsChequebookCharges_Yes"));
        lblPercentageRatePLRIR.setText(resourceBundle.getString("lblPercentageRatePLRIR"));
        lblMaxDIAmtIR.setText(resourceBundle.getString("lblMaxDIAmtIR"));
        lblIsStatementCharges.setText(resourceBundle.getString("lblIsStatementCharges"));
        lblIsApplicablePLRIR.setText(resourceBundle.getString("lblIsApplicablePLRIR"));
        lblToken.setText(resourceBundle.getString("lblToken"));
        rdoIsStatementCharges_Yes.setText(resourceBundle.getString("rdoIsStatementCharges_Yes"));
        lblAmountCRIC.setText(resourceBundle.getString("lblAmountCRIC"));
        rdoWSAccount_No.setText(resourceBundle.getString("rdoWSAccount_No"));
        lblChargeOnTransactionFCharges.setText(resourceBundle.getString("lblChargeOnTransactionFCharges"));
        rdoExistingAccountPLRIR_No.setText(resourceBundle.getString("rdoExistingAccountPLRIR_No"));
        lblDICalculationFIR.setText(resourceBundle.getString("lblDICalculationFIR"));
        rdoPenalOthersIR_No.setText(resourceBundle.getString("rdoPenalOthersIR_No"));
        ((javax.swing.border.TitledBorder)panOtherCharges.getBorder()).setTitle(resourceBundle.getString("panOtherCharges"));
        lblUAICOthersIR.setText(resourceBundle.getString("lblUAICOthersIR"));
        lblODOverAboveLimimt.setText(resourceBundle.getString("lblODOverAboveLimimt"));
        lblAddIntRateIP.setText(resourceBundle.getString("lblAddIntRateIP"));
        rdoCreditCardSI_No.setText(resourceBundle.getString("rdoCreditCardSI_No"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblProdFrequencyIP.setText(resourceBundle.getString("lblProdFrequencyIP"));
        rdoWSAccount_Yes.setText(resourceBundle.getString("rdoWSAccount_Yes"));
        lblAmountCROC.setText(resourceBundle.getString("lblAmountCROC"));
        lblSPCAH.setText(resourceBundle.getString("lblSPCAH"));
        lblIsApplicableFCharges.setText(resourceBundle.getString("lblIsApplicableFCharges"));
        lblPIAH.setText(resourceBundle.getString("lblPIAH"));
        lblPercentageMaxDIRateIR.setText(resourceBundle.getString("lblPercentageMaxDIRateIR"));
        lblMSCAH.setText(resourceBundle.getString("lblMSCAH"));
        lblChargedDIIR.setText(resourceBundle.getString("lblChargedDIIR"));
        lblCrediInterestAFIP.setText(resourceBundle.getString("lblCrediInterestAFIP"));
        lblDCRequiredIR.setText(resourceBundle.getString("lblDCRequiredIR"));
        lblCRCInwardAH.setText(resourceBundle.getString("lblCRCInwardAH"));
        rdoUAICOthersIR_Yes.setText(resourceBundle.getString("rdoUAICOthersIR_Yes"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        rdoIsStopPaymentCharges_No.setText(resourceBundle.getString("rdoIsStopPaymentCharges_No"));
        rdoEOLOthersIR_Yes.setText(resourceBundle.getString("rdoEOLOthersIR_Yes"));
        lblLastAppliedFCharges.setText(resourceBundle.getString("lblLastAppliedFCharges"));
        rdoACAAccount_Yes.setText(resourceBundle.getString("rdoACAAccount_Yes"));
        lblMobileBankingClientSI.setText(resourceBundle.getString("lblMobileBankingClientSI"));
        lblStateFrequency.setText(resourceBundle.getString("lblStateFrequency"));
        lblCAFrequencyFCharges.setText(resourceBundle.getString("lblCAFrequencyFCharges"));
        ((javax.swing.border.TitledBorder)panOthersIR.getBorder()).setTitle(resourceBundle.getString("panOthersIR"));
        rdoIsProcessingCharges_Yes.setText(resourceBundle.getString("rdoIsProcessingCharges_Yes"));
        btnDeleteCROC.setText(resourceBundle.getString("btnDeleteCROC"));
        lblWithdrawalSlip.setText(resourceBundle.getString("lblWithdrawalSlip"));
        lblPercentagePenalIROthersIR.setText(resourceBundle.getString("lblPercentagePenalIROthersIR"));
        lblCICAH.setText(resourceBundle.getString("lblCICAH"));
        lblDueDateFCharges.setText(resourceBundle.getString("lblDueDateFCharges"));
        btnDeleteSI.setText(resourceBundle.getString("btnDeleteSI"));
        btnPIAHHelp.setText(resourceBundle.getString("btnPIAHHelp"));
        btnSaveCRIC.setText(resourceBundle.getString("btnSaveCRIC"));
        rdoDebitCardSI_No.setText(resourceBundle.getString("rdoDebitCardSI_No"));
        btnSaveSI.setText(resourceBundle.getString("btnSaveSI"));
        rdoDebitCompoundIR_Yes.setText(resourceBundle.getString("rdoDebitCompoundIR_Yes"));
        rdoCreditCompdInterestPayable_No.setText(resourceBundle.getString("rdoCreditCompdInterestPayable_No"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblInterestADDebitIR.setText(resourceBundle.getString("lblInterestADDebitIR"));
        lblACCAH.setText(resourceBundle.getString("lblACCAH"));
        lblPercentageCreditInterestRateIP.setText(resourceBundle.getString("lblPercentageCreditInterestRateIP"));
        rdoCreditIntInterestPayable_Yes.setText(resourceBundle.getString("rdoCreditIntInterestPayable_Yes"));
        btnACCAHHelp.setText(resourceBundle.getString("btnACCAHHelp"));
        rdoIsApplicableFCharges_Yes.setText(resourceBundle.getString("rdoIsApplicableFCharges_Yes"));
        rdoNewAccountPLRIR_Yes.setText(resourceBundle.getString("rdoNewAccountPLRIR_Yes"));
        lblAssetsSI.setText(resourceBundle.getString("lblAssetsSI"));
        lblExistingAccountPLRIR.setText(resourceBundle.getString("lblExistingAccountPLRIR"));
        rdoIsStatementCharges_No.setText(resourceBundle.getString("rdoIsStatementCharges_No"));
        ((javax.swing.border.TitledBorder)panMiscItemSI.getBorder()).setTitle(resourceBundle.getString("panMiscItemSI"));
        btnCRCoutwardAHHelp.setText(resourceBundle.getString("btnCRCoutwardAHHelp"));
        btnNewCRIC.setText(resourceBundle.getString("btnNewCRIC"));
        lblInterestCDDebitIR.setText(resourceBundle.getString("lblInterestCDDebitIR"));
        lblPercentageMinDIRateIR.setText(resourceBundle.getString("lblPercentageMinDIRateIR"));
        lblAIRStaffIP.setText(resourceBundle.getString("lblAIRStaffIP"));
        btnCICAHHelp.setText(resourceBundle.getString("btnCICAHHelp"));
        rdoLimitEIOthersIR_No.setText(resourceBundle.getString("rdoLimitEIOthersIR_No"));
        lblIsChequebookCharges.setText(resourceBundle.getString("lblIsChequebookCharges"));
        btnCIAHHelp.setText(resourceBundle.getString("btnCIAHHelp"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        lblProdId.setText(resourceBundle.getString("lblProdId"));
        lblLimitDefination.setText(resourceBundle.getString("lblLimitDefination"));
        lblDPRoundOffIR.setText(resourceBundle.getString("lblDPRoundOffIR"));
        rdoEOLOthersIR_No.setText(resourceBundle.getString("rdoEOLOthersIR_No"));
        btnExOLHAHelp.setText(resourceBundle.getString("btnExOLHAHelp"));
        rdoIsChequebookCharges_No.setText(resourceBundle.getString("rdoIsChequebookCharges_No"));
        rdoIsStopPaymentCharges_Yes.setText(resourceBundle.getString("rdoIsStopPaymentCharges_Yes"));
        rdoDebitCardSI_Yes.setText(resourceBundle.getString("rdoDebitCardSI_Yes"));
        btnNewSI.setText(resourceBundle.getString("btnNewSI"));
        lblProductFOthersIR.setText(resourceBundle.getString("lblProductFOthersIR"));
        rdoIsApplicableFCharges_No.setText(resourceBundle.getString("rdoIsApplicableFCharges_No"));
        btnAccountHeadAccountHelp.setText(resourceBundle.getString("btnAccountHeadAccountHelp"));
        lblAccountSFPLRIR.setText(resourceBundle.getString("lblAccountSFPLRIR"));
        lblBehaveLike.setText(resourceBundle.getString("lblBehaveLike"));
        lblMD4EOL.setText(resourceBundle.getString("lblMD4EOL"));
        rdoODALAccount_Yes.setText(resourceBundle.getString("rdoODALAccount_Yes"));
        rdoUAICOthersIR_No.setText(resourceBundle.getString("rdoUAICOthersIR_No"));
        lblFolioEntriesFCharges.setText(resourceBundle.getString("lblFolioEntriesFCharges"));
        lblProcessingCharges.setText(resourceBundle.getString("lblProcessingCharges"));
        btnFCAHHelp.setText(resourceBundle.getString("btnFCAHHelp"));
        ((javax.swing.border.TitledBorder)panPLRIR.getBorder()).setTitle(resourceBundle.getString("panPLRIR"));
        btnSCAHHelp.setText(resourceBundle.getString("btnSCAHHelp"));
        lblRateSI.setText(resourceBundle.getString("lblRateSI"));
        lblStaffAccOpened.setText(resourceBundle.getString("lblStaffAccOpened"));
        rdoAdditionalIntInterestPayable_Yes.setText(resourceBundle.getString("rdoAdditionalIntInterestPayable_Yes"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        rdoCreditCardSI_Yes.setText(resourceBundle.getString("rdoCreditCardSI_Yes"));
        lblCreditInterestCFIP.setText(resourceBundle.getString("lblCreditInterestCFIP"));
        lblAgCIAH.setText(resourceBundle.getString("lblAgCIAH"));
        lblCRCOutwardAH.setText(resourceBundle.getString("lblCRCOutwardAH"));
        btnAgCIAHHelp.setText(resourceBundle.getString("btnAgCIAHHelp"));
        btnSaveCROC.setText(resourceBundle.getString("btnSaveCROC"));
        lblCollectChargeFCharges.setText(resourceBundle.getString("lblCollectChargeFCharges"));
        rdoChargedDIIR_No.setText(resourceBundle.getString("rdoChargedDIIR_No"));
        lblPSCommitmentCharges.setText(resourceBundle.getString("lblPSCommitmentCharges"));
        rdoAdditionalIntInterestPayable_No.setText(resourceBundle.getString("rdoAdditionalIntInterestPayable_No"));
        lblDebitInterestOnDAUE.setText(resourceBundle.getString("lblDebitInterestOnDAUE"));
        lblLimitEIOthersIR.setText(resourceBundle.getString("lblLimitEIOthersIR"));
        lblAccountCLosingCharges.setText(resourceBundle.getString("lblAccountCLosingCharges"));
        lblNumberPattern.setText(resourceBundle.getString("lblNumberPattern"));
        lblAccountHead.setText(resourceBundle.getString("lblAccountHead"));
        rdoCIUEAccount_No.setText(resourceBundle.getString("rdoCIUEAccount_No"));
        rdoACAAccount_No.setText(resourceBundle.getString("rdoACAAccount_No"));
        btnNewCROC.setText(resourceBundle.getString("btnNewCROC"));
        lblRateCRIC.setText(resourceBundle.getString("lblRateCRIC"));
        rdoPenalOthersIR_Yes.setText(resourceBundle.getString("rdoPenalOthersIR_Yes"));
        lblACA.setText(resourceBundle.getString("lblACA"));
        lblCIROIP.setText(resourceBundle.getString("lblCIROIP"));
        lblMinDIRateIR.setText(resourceBundle.getString("lblMinDIRateIR"));
        rdoNewAccountPLRIR_No.setText(resourceBundle.getString("rdoNewAccountPLRIR_No"));
        rdoDIAUEAccount_No.setText(resourceBundle.getString("rdoDIAUEAccount_No"));
        lblProdDesc.setText(resourceBundle.getString("lblProdDesc"));
        rdoODALAccount_No.setText(resourceBundle.getString("rdoODALAccount_No"));
        lblCreditInterestOnUE.setText(resourceBundle.getString("lblCreditInterestOnUE"));
        lblDIApplicationFIR.setText(resourceBundle.getString("lblDIApplicationFIR"));
        lblRatePLRIR.setText(resourceBundle.getString("lblRatePLRIR"));
        btnCRCInwardAHHelp.setText(resourceBundle.getString("btnCRCInwardAHHelp"));
        rdoIsProcessingCharges_No.setText(resourceBundle.getString("rdoIsProcessingCharges_No"));
        lblSCAH.setText(resourceBundle.getString("lblSCAH"));
        lblATMCardSI.setText(resourceBundle.getString("lblATMCardSI"));
        rdoATMCardSI_Yes.setText(resourceBundle.getString("rdoATMCardSI_Yes"));
        lblCLStart.setText(resourceBundle.getString("lblCLStart"));
        btnDIAHHelp.setText(resourceBundle.getString("btnDIAHHelp"));
        lblEOLOthersIR.setText(resourceBundle.getString("lblEOLOthersIR"));
        ((javax.swing.border.TitledBorder)panCRChargesOutward.getBorder()).setTitle(resourceBundle.getString("panCRChargesOutward"));
        lblCIAH.setText(resourceBundle.getString("lblCIAH"));
        rdoLDAccount_No.setText(resourceBundle.getString("rdoLDAccount_No"));
        lblMinDIAmtIR.setText(resourceBundle.getString("lblMinDIAmtIR"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblPercentageMDAccount.setText(resourceBundle.getString("lblPercentageMDAccount"));
        rdoIsCommitmentCharges_No.setText(resourceBundle.getString("rdoIsCommitmentCharges_No"));
        lblCreditInterestAmt.setText(resourceBundle.getString("lblCreditInterestAmt"));
    }
    
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        //                mandatoryMap.put("cboProdCurrency", new Boolean(true));
        mandatoryMap.put("txtLastAccNoAccount", new Boolean(true));
        mandatoryMap.put("cboBehavesLike", new Boolean(true));
        mandatoryMap.put("tdtCLStartAccount", new Boolean(true));
        mandatoryMap.put("txtCLPeriodAccount", new Boolean(true));
        mandatoryMap.put("cboCLPeriod", new Boolean(true));
        mandatoryMap.put("txtAccountHeadAccount", new Boolean(true));
        mandatoryMap.put("txtManagerDistAccount", new Boolean(true));
        mandatoryMap.put("txtFreeCLAccount", new Boolean(true));
        mandatoryMap.put("cboStmtFrequency", new Boolean(true));
        mandatoryMap.put("txtNumberpatternAccount", new Boolean(true));
        mandatoryMap.put("rdoLDAccount_Yes", new Boolean(true));
        mandatoryMap.put("rdoACAAccount_Yes", new Boolean(true));
        mandatoryMap.put("rdoSAOAccount_Yes", new Boolean(true));
        mandatoryMap.put("rdoTokanAccount_Yes", new Boolean(true));
        mandatoryMap.put("rdoCIUEAccount_Yes", new Boolean(true));
        mandatoryMap.put("rdoODALAccount_Yes", new Boolean(true));
        mandatoryMap.put("rdoDIAUEAccount_Yes", new Boolean(true));
        mandatoryMap.put("rdoWSAccount_Yes", new Boolean(true));
        mandatoryMap.put("txtMaxAmountOnWS", new Boolean(true));
        mandatoryMap.put("txtProductIdAccount", new Boolean(true));
        mandatoryMap.put("txtProductDescAccount", new Boolean(true));
        mandatoryMap.put("rdoChargedDIIR_Yes", new Boolean(true));
        mandatoryMap.put("txtMinDIRateIR", new Boolean(true));
        mandatoryMap.put("txtMaxDIRateIR", new Boolean(true));
        mandatoryMap.put("txtMinDIAmtIR", new Boolean(true));
        mandatoryMap.put("txtMaxDIAmtIR", new Boolean(true));
        mandatoryMap.put("cboDICalculationFIR", new Boolean(true));
        mandatoryMap.put("tdtInterestCDDebitIR", new Boolean(true));
        mandatoryMap.put("cboDIApplicationFIR", new Boolean(true));
        mandatoryMap.put("tdtInterestADDebitIR", new Boolean(true));
        mandatoryMap.put("rdoDebitCompoundIR_Yes", new Boolean(true));
        mandatoryMap.put("cboDICompoundFIR", new Boolean(true));
        mandatoryMap.put("cboDPRoundOffIR", new Boolean(true));
        mandatoryMap.put("cboDIRoundOffIR", new Boolean(true));
        mandatoryMap.put("cboProductFOthersIR", new Boolean(true));
        mandatoryMap.put("rdoUAICOthersIR_Yes", new Boolean(true));
        mandatoryMap.put("rdoEOLOthersIR_Yes", new Boolean(true));
        mandatoryMap.put("rdoPenalOthersIR_Yes", new Boolean(true));
        mandatoryMap.put("rdoLimitEIOthersIR_Yes", new Boolean(true));
        mandatoryMap.put("txtPenalIROthersIR", new Boolean(true));
        mandatoryMap.put("rdoIsApplicablePLRIR_Yes", new Boolean(true));
        mandatoryMap.put("txtRatePLRIR", new Boolean(true));
        mandatoryMap.put("tdtAppliedFromPLRIR", new Boolean(true));
        mandatoryMap.put("rdoNewAccountPLRIR_Yes", new Boolean(true));
        mandatoryMap.put("rdoExistingAccountPLRIR_Yes", new Boolean(true));
        mandatoryMap.put("tdtAccountSFPLRIR", new Boolean(true));
        mandatoryMap.put("rdoCreditIntInterestPayable_Yes", new Boolean(true));
        mandatoryMap.put("rdoCreditCompdInterestPayable_Yes", new Boolean(true));
        mandatoryMap.put("rdoAdditionalIntInterestPayable_Yes", new Boolean(true));
        mandatoryMap.put("cboCreditInterestCFIP", new Boolean(true));
        mandatoryMap.put("cboCreditInterestAFIP", new Boolean(true));
        mandatoryMap.put("cboCreditInterestCompdFIP", new Boolean(true));
        mandatoryMap.put("cboCPROIP", new Boolean(true));
        mandatoryMap.put("cboCIROIP", new Boolean(true));
        mandatoryMap.put("cboCalcCtriteriaIP", new Boolean(true));
        mandatoryMap.put("cboProdFrequencyIP", new Boolean(true));
        mandatoryMap.put("txtCreditInterestRateIP", new Boolean(true));
        mandatoryMap.put("tdtLastCDIP", new Boolean(true));
        mandatoryMap.put("tdtLastADIP", new Boolean(true));
        mandatoryMap.put("txtAddIntRateIP", new Boolean(true));
        mandatoryMap.put("rdoIsApplicableFCharges_Yes", new Boolean(true));
        mandatoryMap.put("tdtLastAppliedFCharges", new Boolean(true));
        mandatoryMap.put("tdtDueDateFCharges", new Boolean(true));
        mandatoryMap.put("txtFolioEntriesFCharges", new Boolean(true));
        mandatoryMap.put("txtRateFCharges", new Boolean(true));
        mandatoryMap.put("cboChargeOnTransactionFCharges", new Boolean(true));
        mandatoryMap.put("cboCAFrequencyFCharges", new Boolean(true));
        mandatoryMap.put("cboCollectChargeFCharges", new Boolean(true));
        mandatoryMap.put("cboChargeOnDocFCharges", new Boolean(true));
        mandatoryMap.put("cboIRFrequencyFCharges", new Boolean(true));
        mandatoryMap.put("rdoIsStatementCharges_Yes", new Boolean(true));
        mandatoryMap.put("rdoIsChequebookCharges_Yes", new Boolean(true));
        mandatoryMap.put("rdoIsStopPaymentCharges_Yes", new Boolean(true));
        mandatoryMap.put("rdoIsProcessingCharges_Yes", new Boolean(true));
        mandatoryMap.put("txtProcessingCharges", new Boolean(true));
        mandatoryMap.put("cboProcessingCharges", new Boolean(true));
        mandatoryMap.put("rdoIsCommitmentCharges_Yes", new Boolean(true));
        mandatoryMap.put("txtCommitmentCharges", new Boolean(true));
        mandatoryMap.put("cboCommitmentCharges", new Boolean(true));
        mandatoryMap.put("txtAccountClosingCharges", new Boolean(true));
        mandatoryMap.put("txtStatementCharges", new Boolean(true));
        mandatoryMap.put("txtMiscServiceCharges", new Boolean(true));
        mandatoryMap.put("txtChequebookCharges", new Boolean(true));
        mandatoryMap.put("txtStopPaymentCharges", new Boolean(true));
        mandatoryMap.put("cboAmountCRIC", new Boolean(true));
        mandatoryMap.put("txtRateCRIC", new Boolean(true));
        mandatoryMap.put("cboAmountCROC", new Boolean(true));
        mandatoryMap.put("txtRateCROC", new Boolean(true));
        mandatoryMap.put("rdoATMCardSI_Yes", new Boolean(true));
        mandatoryMap.put("rdoCreditCardSI_Yes", new Boolean(true));
        mandatoryMap.put("rdoDebitCardSI_Yes", new Boolean(true));
        mandatoryMap.put("rdoMobileBankingClientSI_Yes", new Boolean(true));
        mandatoryMap.put("rdoBranchBankingSI_Yes", new Boolean(true));
        mandatoryMap.put("txtRateSI", new Boolean(true));
        mandatoryMap.put("cboAssetsSI", new Boolean(true));
        mandatoryMap.put("txtACCAH", new Boolean(true));
        mandatoryMap.put("txtMSCAH", new Boolean(true));
        mandatoryMap.put("txtSCAH", new Boolean(true));
        mandatoryMap.put("txtDIAH", new Boolean(true));
        mandatoryMap.put("txtPIAH", new Boolean(true));
        mandatoryMap.put("txtCIAH", new Boolean(true));
        mandatoryMap.put("txtAgCIAH", new Boolean(true));
        mandatoryMap.put("txtEIAH", new Boolean(true));
        mandatoryMap.put("txtExOLHAH", new Boolean(true));
        mandatoryMap.put("txtCICAH", new Boolean(true));
        mandatoryMap.put("txtSPCAH", new Boolean(true));
        mandatoryMap.put("txtCRCInwardAH", new Boolean(true));
        mandatoryMap.put("txtCRCoutwardAH", new Boolean(true));
        mandatoryMap.put("txtFCAH", new Boolean(true));
    }
    
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    private void setMaxLenght(){
        //__ Account Data...
        txtProductIdAccount.setMaxLength(8);
        txtProductDescAccount.setMaxLength(128);
        //__ Account Param...
        //        txtManagerDistAccount.setMaxLength(3);
        txtManagerDistAccount.setValidation(new PercentageValidation());
        txtCreditInterestAmt.setMaxLength(3);
        txtNumberpatternAccount.setMaxLength(16);
        txtLastAccNoAccount.setMaxLength(16);
        
        txtFreeCLAccount.setMaxLength(3);
        txtFreeCLAccount.setValidation(new NumericValidation());
        
        txtCLPeriodAccount.setMaxLength(5);// 4
        txtCLPeriodAccount.setValidation(new NumericValidation());
        
        txtMaxAmountOnWS.setValidation(new CurrencyValidation(14,2));
        //__ Interest Receivable...
        //        txtMinDIRateIR.setMaxLength(3);
        txtMinDIRateIR.setValidation(new PercentageValidation());
        
        //        txtMaxDIRateIR.setMaxLength(3);
        txtMaxDIRateIR.setValidation(new PercentageValidation());
        
        //        txtMinDIAmtIR.setMaxLength(16);
        txtMinDIAmtIR.setValidation(new CurrencyValidation(14,2));
        
        //        txtMaxDIAmtIR.setMaxLength(16);
        txtMaxDIAmtIR.setValidation(new CurrencyValidation(14,2));
        
        //        txtRatePLRIR.setMaxLength(3);
        txtRatePLRIR.setValidation(new PercentageValidation());
        
        //        txtPenalIROthersIR.setMaxLength(3);
        txtPenalIROthersIR.setValidation(new PercentageValidation());
        
        //__ Interest Payable...
        //        txtCreditInterestRateIP.setMaxLength(3);
        txtCreditInterestRateIP.setValidation(new PercentageValidation());
        
        //        txtAddIntRateIP.setMaxLength(3);
        txtAddIntRateIP.setValidation(new PercentageValidation());
        //__ Charges...
        //        txtAccountClosingCharges.setMaxLength(16);
        txtAccountClosingCharges.setValidation(new CurrencyValidation(14,2));
        txtRateCRIC.setValidation(new PercentageValidation());
        txtRateCROC.setValidation(new PercentageValidation());
        
        //        txtMiscServiceCharges.setMaxLength(16);
        txtMiscServiceCharges.setValidation(new CurrencyValidation(14,2));
        
        txtStatementCharges.setMaxLength(16);
        txtStatementCharges.setValidation(new CurrencyValidation(14,2));
        
        //        txtChequebookCharges.setMaxLength(16);
        txtChequebookCharges.setValidation(new CurrencyValidation(14,2));
        
        //        txtStopPaymentCharges.setMaxLength(16);
        txtStopPaymentCharges.setValidation(new CurrencyValidation(14,2));
        
        txtFolioEntriesFCharges.setMaxLength(5);
        txtFolioEntriesFCharges.setValidation(new NumericValidation());
        
        //        txtRateFCharges.setMaxLength(16);
        txtRateFCharges.setValidation(new CurrencyValidation(14,2));
        //__ Cheque Charges...
        //        txtchqRetChrgamt.setMaxLength(32);
        //        txtchqRetChrgrate.setMaxLength(16);
        //__ Special Assets...
        //        txtassetCategoryRateper.setMaxLength(3);
        txtRateSI.setValidation(new NumericValidation(2,1));
        //__ Account Heads...
        txtACCAH.setMaxLength(16);
        txtMSCAH.setMaxLength(16);
        txtSCAH.setMaxLength(16);
        txtDIAH.setMaxLength(16);
        txtPIAH.setMaxLength(16);
        txtCIAH.setMaxLength(16);
        txtAgCIAH.setMaxLength(16);
        txtEIAH.setMaxLength(16);
        txtExOLHAH.setMaxLength(16);
        txtCICAH.setMaxLength(16);
        txtSPCAH.setMaxLength(16);
        txtCRCInwardAH.setMaxLength(16);
        txtCRCoutwardAH.setMaxLength(16);
        txtFCAH.setMaxLength(16);
    }
    
    
    //    public static void main(String [] args){
    //        AdvancesProductUI advancesProductUI = new AdvancesProductUI();
    //        javax.swing.JFrame temp = new javax.swing.JFrame();
    //        temp.getContentPane().add(advancesProductUI);
    //        temp.setSize(800,700);
    //        temp.show();
    //        //advancesProductUI.show();
    //        return;
    //    }
    
    //__ To Remove the Radio Buttos(s) from its respective Group...
    private void removeRadioButtons(){
        rdoLDAccount.remove(rdoLDAccount_Yes);
        rdoLDAccount.remove(rdoLDAccount_No);
        
        rdoACAAcccount.remove(rdoACAAccount_Yes);
        rdoACAAcccount.remove(rdoACAAccount_No);
        
        rdoSAOAccount.remove(rdoSAOAccount_Yes);
        rdoSAOAccount.remove(rdoSAOAccount_No);
        
        rdoTokenAccount.remove(rdoTokanAccount_Yes);
        rdoTokenAccount.remove(rdoTokanAccount_No);
        
        rdoCIUEAccount.remove(rdoCIUEAccount_Yes);
        rdoCIUEAccount.remove(rdoCIUEAccount_No);
        
        rdoODALAccount.remove(rdoODALAccount_Yes);
        rdoODALAccount.remove(rdoODALAccount_No);
        
        rdoDIAUEAccount.remove(rdoDIAUEAccount_Yes);
        rdoDIAUEAccount.remove(rdoDIAUEAccount_No);
        
        rdoWSAccount.remove(rdoWSAccount_Yes);
        rdoWSAccount.remove(rdoWSAccount_No);
        
        rdoChargedDIIR.remove(rdoChargedDIIR_Yes);
        rdoChargedDIIR.remove(rdoChargedDIIR_No);
        
        rdoDebitCompoundIR.remove(rdoDebitCompoundIR_Yes);
        rdoDebitCompoundIR.remove(rdoDebitCompoundIR_No);
        
        rdoUAICOthersIR.remove(rdoUAICOthersIR_Yes);
        rdoUAICOthersIR.remove(rdoUAICOthersIR_No);
        
        rdoEOLOthersIR.remove(rdoEOLOthersIR_Yes);
        rdoEOLOthersIR.remove(rdoEOLOthersIR_No);
        
        rdoPenalOthersIR.remove(rdoPenalOthersIR_Yes);
        rdoPenalOthersIR.remove(rdoPenalOthersIR_No);
        
        rdoLimitEIOthersIR.remove(rdoLimitEIOthersIR_Yes);
        rdoLimitEIOthersIR.remove(rdoLimitEIOthersIR_No);
        
        rdoNewAccountPLRIR.remove(rdoNewAccountPLRIR_Yes);
        rdoNewAccountPLRIR.remove(rdoNewAccountPLRIR_No);
        
        rdoExistingAccountPLRIR.remove(rdoExistingAccountPLRIR_Yes);
        rdoExistingAccountPLRIR.remove(rdoExistingAccountPLRIR_No);
        
        rdoIsApplicablePLRIR.remove(rdoIsApplicablePLRIR_Yes);
        rdoIsApplicablePLRIR.remove(rdoIsApplicablePLRIR_No);
        
        rdoCreditIntInterestPayable.remove(rdoCreditIntInterestPayable_Yes);
        rdoCreditIntInterestPayable.remove(rdoCreditIntInterestPayable_No);
        
        rdoCreditCompdInterestPayable.remove(rdoCreditCompdInterestPayable_Yes);
        rdoCreditCompdInterestPayable.remove(rdoCreditCompdInterestPayable_No);
        
        rdoAdditionalIntInterestPayable.remove(rdoAdditionalIntInterestPayable_Yes);
        rdoAdditionalIntInterestPayable.remove(rdoAdditionalIntInterestPayable_No);
        
        rdoIsApplicableFCharges.remove(rdoIsApplicableFCharges_Yes);
        rdoIsApplicableFCharges.remove(rdoIsApplicableFCharges_No);
        
        rdoIsStatementCharges.remove(rdoIsStatementCharges_Yes);
        rdoIsStatementCharges.remove(rdoIsStatementCharges_No);
        
        rdoIsChequebookCharges.remove(rdoIsChequebookCharges_Yes);
        rdoIsChequebookCharges.remove(rdoIsChequebookCharges_No);
        
        rdoIsStopPaymentCharges.remove(rdoIsStopPaymentCharges_Yes);
        rdoIsStopPaymentCharges.remove(rdoIsStopPaymentCharges_No);
        
        rdoIsProcessingCharges.remove(rdoIsProcessingCharges_Yes);
        rdoIsProcessingCharges.remove(rdoIsProcessingCharges_No);
        
        rdoIsCommitmentCharges.remove(rdoIsCommitmentCharges_Yes);
        rdoIsCommitmentCharges.remove(rdoIsCommitmentCharges_No);
        
        rdoATMCardSI.remove(rdoATMCardSI_Yes);
        rdoATMCardSI.remove(rdoATMCardSI_No);
        
        rdoCreditCardSI.remove(rdoCreditCardSI_Yes);
        rdoCreditCardSI.remove(rdoCreditCardSI_No);
        
        rdoDebitCardSI.remove(rdoDebitCardSI_Yes);
        rdoDebitCardSI.remove(rdoDebitCardSI_No);
        
        rdoMobileBankingClientSI.remove(rdoMobileBankingClientSI_Yes);
        rdoMobileBankingClientSI.remove(rdoMobileBankingClientSI_No);
        
        rdoBranchBankingSI.remove(rdoBranchBankingSI_Yes);
        rdoBranchBankingSI.remove(rdoBranchBankingSI_No);
    }
    
    //__ To Create new radioButton Group and Add the Radio Buttos(s) to the respective Group...
    private void addRadioButtons() {
        
        rdoLDAccount = new CButtonGroup();
        rdoLDAccount.add(rdoLDAccount_Yes);
        rdoLDAccount.add(rdoLDAccount_No);
        
        rdoACAAcccount = new CButtonGroup();
        rdoACAAcccount.add(rdoACAAccount_Yes);
        rdoACAAcccount.add(rdoACAAccount_No);
        
        rdoSAOAccount = new CButtonGroup();
        rdoSAOAccount.add(rdoSAOAccount_Yes);
        rdoSAOAccount.add(rdoSAOAccount_No);
        
        rdoTokenAccount = new CButtonGroup();
        rdoTokenAccount.add(rdoTokanAccount_Yes);
        rdoTokenAccount.add(rdoTokanAccount_No);
        
        rdoCIUEAccount = new CButtonGroup();
        rdoCIUEAccount.add(rdoCIUEAccount_Yes);
        rdoCIUEAccount.add(rdoCIUEAccount_No);
        
        rdoODALAccount = new CButtonGroup();
        rdoODALAccount.add(rdoODALAccount_Yes);
        rdoODALAccount.add(rdoODALAccount_No);
        
        rdoDIAUEAccount = new CButtonGroup();
        rdoDIAUEAccount.add(rdoDIAUEAccount_Yes);
        rdoDIAUEAccount.add(rdoDIAUEAccount_No);
        
        rdoWSAccount = new CButtonGroup();
        rdoWSAccount.add(rdoWSAccount_Yes);
        rdoWSAccount.add(rdoWSAccount_No);
        
        rdoChargedDIIR = new CButtonGroup();
        rdoChargedDIIR.add(rdoChargedDIIR_Yes);
        rdoChargedDIIR.add(rdoChargedDIIR_No);
        
        rdoDebitCompoundIR = new CButtonGroup();
        rdoDebitCompoundIR.add(rdoDebitCompoundIR_Yes);
        rdoDebitCompoundIR.add(rdoDebitCompoundIR_No);
        
        rdoUAICOthersIR = new CButtonGroup();
        rdoUAICOthersIR.add(rdoUAICOthersIR_Yes);
        rdoUAICOthersIR.add(rdoUAICOthersIR_No);
        
        rdoEOLOthersIR = new CButtonGroup();
        rdoEOLOthersIR.add(rdoEOLOthersIR_Yes);
        rdoEOLOthersIR.add(rdoEOLOthersIR_No);
        
        rdoPenalOthersIR = new CButtonGroup();
        rdoPenalOthersIR.add(rdoPenalOthersIR_Yes);
        rdoPenalOthersIR.add(rdoPenalOthersIR_No);
        
        rdoLimitEIOthersIR = new CButtonGroup();
        rdoLimitEIOthersIR.add(rdoLimitEIOthersIR_Yes);
        rdoLimitEIOthersIR.add(rdoLimitEIOthersIR_No);
        
        rdoNewAccountPLRIR = new CButtonGroup();
        rdoNewAccountPLRIR.add(rdoNewAccountPLRIR_Yes);
        rdoNewAccountPLRIR.add(rdoNewAccountPLRIR_No);
        
        rdoExistingAccountPLRIR = new CButtonGroup();
        rdoExistingAccountPLRIR.add(rdoExistingAccountPLRIR_Yes);
        rdoExistingAccountPLRIR.add(rdoExistingAccountPLRIR_No);
        
        rdoIsApplicablePLRIR = new CButtonGroup();
        rdoIsApplicablePLRIR.add(rdoIsApplicablePLRIR_Yes);
        rdoIsApplicablePLRIR.add(rdoIsApplicablePLRIR_No);
        
        rdoCreditIntInterestPayable = new CButtonGroup();
        rdoCreditIntInterestPayable.add(rdoCreditIntInterestPayable_Yes);
        rdoCreditIntInterestPayable.add(rdoCreditIntInterestPayable_No);
        
        rdoCreditCompdInterestPayable = new CButtonGroup();
        rdoCreditCompdInterestPayable.add(rdoCreditCompdInterestPayable_Yes);
        rdoCreditCompdInterestPayable.add(rdoCreditCompdInterestPayable_No);
        
        rdoAdditionalIntInterestPayable = new CButtonGroup();
        rdoAdditionalIntInterestPayable.add(rdoAdditionalIntInterestPayable_Yes);
        rdoAdditionalIntInterestPayable.add(rdoAdditionalIntInterestPayable_No);
        
        rdoIsApplicableFCharges = new CButtonGroup();
        rdoIsApplicableFCharges.add(rdoIsApplicableFCharges_Yes);
        rdoIsApplicableFCharges.add(rdoIsApplicableFCharges_No);
        
        rdoIsStatementCharges = new CButtonGroup();
        rdoIsStatementCharges.add(rdoIsStatementCharges_Yes);
        rdoIsStatementCharges.add(rdoIsStatementCharges_No);
        
        rdoIsChequebookCharges = new CButtonGroup();
        rdoIsChequebookCharges.add(rdoIsChequebookCharges_Yes);
        rdoIsChequebookCharges.add(rdoIsChequebookCharges_No);
        
        rdoIsStopPaymentCharges = new CButtonGroup();
        rdoIsStopPaymentCharges.add(rdoIsStopPaymentCharges_Yes);
        rdoIsStopPaymentCharges.add(rdoIsStopPaymentCharges_No);
        
        rdoIsProcessingCharges = new CButtonGroup();
        rdoIsProcessingCharges.add(rdoIsProcessingCharges_Yes);
        rdoIsProcessingCharges.add(rdoIsProcessingCharges_No);
        
        rdoIsCommitmentCharges = new CButtonGroup();
        rdoIsCommitmentCharges.add(rdoIsCommitmentCharges_Yes);
        rdoIsCommitmentCharges.add(rdoIsCommitmentCharges_No);
        
        rdoATMCardSI = new CButtonGroup();
        rdoATMCardSI.add(rdoATMCardSI_Yes);
        rdoATMCardSI.add(rdoATMCardSI_No);
        
        rdoCreditCardSI = new CButtonGroup();
        rdoCreditCardSI.add(rdoCreditCardSI_Yes);
        rdoCreditCardSI.add(rdoCreditCardSI_No);
        
        rdoDebitCardSI = new CButtonGroup();
        rdoDebitCardSI.add(rdoDebitCardSI_Yes);
        rdoDebitCardSI.add(rdoDebitCardSI_No);
        
        rdoMobileBankingClientSI = new CButtonGroup();
        rdoMobileBankingClientSI.add(rdoMobileBankingClientSI_Yes);
        rdoMobileBankingClientSI.add(rdoMobileBankingClientSI_No);
        
        rdoBranchBankingSI = new CButtonGroup();
        rdoBranchBankingSI.add(rdoBranchBankingSI_Yes);
        rdoBranchBankingSI.add(rdoBranchBankingSI_No);
    }
    
    public void update(java.util.Observable observed, Object arg){
        removeRadioButtons();
        
        txtLastAccNoAccount.setText(observable.getTxtLastAccNoAccount());
        cboBehavesLike.setSelectedItem(observable.getCboBehavesLike());
        txtCLPeriodAccount.setText(observable.getTxtCLPeriodAccount());
        cboCLPeriod.setSelectedItem(observable.getCboCLPeriod());
        //                cboProdCurrency.setSelectedItem(((ComboBoxModel) cboProdCurrency.getModel()).getDataForKey(observable.getCboProdCurrency()));
        txtAccountHeadAccount.setText(observable.getTxtAccountHeadAccount());
        txtManagerDistAccount.setText(observable.getTxtManagerDistAccount());
        txtFreeCLAccount.setText(observable.getTxtFreeCLAccount());
        cboStmtFrequency.setSelectedItem(observable.getCboStmtFrequency());
        txtNumberpatternAccount.setText(observable.getTxtNumberpatternAccount());
        rdoLDAccount_Yes.setSelected(observable.getRdoLDAccount_Yes());
        rdoLDAccount_No.setSelected(observable.getRdoLDAccount_No());
        rdoACAAccount_Yes.setSelected(observable.getRdoACAAccount_Yes());
        rdoACAAccount_No.setSelected(observable.getRdoACAAccount_No());
        rdoSAOAccount_Yes.setSelected(observable.getRdoSAOAccount_Yes());
        rdoSAOAccount_No.setSelected(observable.getRdoSAOAccount_No());
        rdoTokanAccount_Yes.setSelected(observable.getRdoTokanAccount_Yes());
        rdoTokanAccount_No.setSelected(observable.getRdoTokanAccount_No());
        rdoCIUEAccount_Yes.setSelected(observable.getRdoCIUEAccount_Yes());
        rdoCIUEAccount_No.setSelected(observable.getRdoCIUEAccount_No());
        rdoODALAccount_Yes.setSelected(observable.getRdoODALAccount_Yes());
        rdoODALAccount_No.setSelected(observable.getRdoODALAccount_No());
        rdoDIAUEAccount_Yes.setSelected(observable.getRdoDIAUEAccount_Yes());
        rdoDIAUEAccount_No.setSelected(observable.getRdoDIAUEAccount_No());
        rdoWSAccount_Yes.setSelected(observable.getRdoWSAccount_Yes());
        rdoWSAccount_No.setSelected(observable.getRdoWSAccount_No());
        txtMaxAmountOnWS.setText(observable.getTxtMaxAmountOnWS());
        rdoWSAccount_NoActionPerformed(null);
        
        txtProductIdAccount.setText(observable.getTxtProductIdAccount());
        txtProductDescAccount.setText(observable.getTxtProductDescAccount());
        rdoChargedDIIR_Yes.setSelected(observable.getRdoChargedDIIR_Yes());
        
        rdoChargedDIIR_No.setSelected(observable.getRdoChargedDIIR_No());
        if(observable.getRdoChargedDIIR_No()){
            rdoChargedDIIR_NoActionPerformed(null);
        }
        
        txtMinDIRateIR.setText(observable.getTxtMinDIRateIR());
        txtMaxDIRateIR.setText(observable.getTxtMaxDIRateIR());
        txtMinDIAmtIR.setText(observable.getTxtMinDIAmtIR());
        txtMaxDIAmtIR.setText(observable.getTxtMaxDIAmtIR());
        cboDICalculationFIR.setSelectedItem(observable.getCboDICalculationFIR());
        cboDIApplicationFIR.setSelectedItem(observable.getCboDIApplicationFIR());
        rdoDebitCompoundIR_Yes.setSelected(observable.getRdoDebitCompoundIR_Yes());
        
        rdoDebitCompoundIR_No.setSelected(observable.getRdoDebitCompoundIR_No());
        if(observable.getRdoDebitCompoundIR_No()){
            rdoDebitCompoundIR_NoActionPerformed(null);
        }
        
        cboDICompoundFIR.setSelectedItem(observable.getCboDICompoundFIR());
        cboDPRoundOffIR.setSelectedItem(observable.getCboDPRoundOffIR());
        cboDIRoundOffIR.setSelectedItem(observable.getCboDIRoundOffIR());
        cboProductFOthersIR.setSelectedItem(observable.getCboProductFOthersIR());
        rdoUAICOthersIR_Yes.setSelected(observable.getRdoUAICOthersIR_Yes());
        rdoUAICOthersIR_No.setSelected(observable.getRdoUAICOthersIR_No());
        rdoEOLOthersIR_Yes.setSelected(observable.getRdoEOLOthersIR_Yes());
        rdoEOLOthersIR_No.setSelected(observable.getRdoEOLOthersIR_No());
        rdoPenalOthersIR_Yes.setSelected(observable.getRdoPenalOthersIR_Yes());
        
        rdoPenalOthersIR_No.setSelected(observable.getRdoPenalOthersIR_No());
        if(observable.getRdoPenalOthersIR_No()){
            rdoPenalOthersIR_NoActionPerformed(null);
        }
        
        rdoLimitEIOthersIR_Yes.setSelected(observable.getRdoLimitEIOthersIR_Yes());
        rdoLimitEIOthersIR_No.setSelected(observable.getRdoLimitEIOthersIR_No());
        txtPenalIROthersIR.setText(observable.getTxtPenalIROthersIR());
        rdoIsApplicablePLRIR_Yes.setSelected(observable.getRdoIsApplicablePLRIR_Yes());
        
        rdoIsApplicablePLRIR_No.setSelected(observable.getRdoIsApplicablePLRIR_No());
        if(observable.getRdoIsApplicablePLRIR_No()){
            rdoIsApplicablePLRIR_NoActionPerformed(null);
        }
        
        txtRatePLRIR.setText(observable.getTxtRatePLRIR());
        rdoNewAccountPLRIR_Yes.setSelected(observable.getRdoNewAccountPLRIR_Yes());
        rdoNewAccountPLRIR_No.setSelected(observable.getRdoNewAccountPLRIR_No());
        rdoExistingAccountPLRIR_Yes.setSelected(observable.getRdoExistingAccountPLRIR_Yes());
        rdoExistingAccountPLRIR_No.setSelected(observable.getRdoExistingAccountPLRIR_No());
        rdoCreditIntInterestPayable_Yes.setSelected(observable.getRdoCreditIntInterestPayable_Yes());
        
        rdoCreditIntInterestPayable_No.setSelected(observable.getRdoCreditIntInterestPayable_No());
        if(observable.getRdoCreditIntInterestPayable_No()){
            rdoCreditIntInterestPayable_NoActionPerformed(null);
        }
        
        rdoCreditCompdInterestPayable_Yes.setSelected(observable.getRdoCreditCompdInterestPayable_Yes());
        
        rdoCreditCompdInterestPayable_No.setSelected(observable.getRdoCreditCompdInterestPayable_No());
        if(observable.getRdoCreditCompdInterestPayable_No()){
            rdoCreditCompdInterestPayable_NoActionPerformed(null);
        }
        
        rdoAdditionalIntInterestPayable_Yes.setSelected(observable.getRdoAdditionalIntInterestPayable_Yes());
        
        rdoAdditionalIntInterestPayable_No.setSelected(observable.getRdoAdditionalIntInterestPayable_No());
        if(observable.getRdoAdditionalIntInterestPayable_No()){
            rdoAdditionalIntInterestPayable_NoActionPerformed(null);
        }
        
        cboCreditInterestCFIP.setSelectedItem(observable.getCboCreditInterestCFIP());
        cboCreditInterestAFIP.setSelectedItem(observable.getCboCreditInterestAFIP());
        cboCreditInterestCompdFIP.setSelectedItem(observable.getCboCreditInterestCompdFIP());
        cboCPROIP.setSelectedItem(observable.getCboCPROIP());
        cboCIROIP.setSelectedItem(observable.getCboCIROIP());
        cboCalcCtriteriaIP.setSelectedItem(observable.getCboCalcCtriteriaIP());
        cboProdFrequencyIP.setSelectedItem(observable.getCboProdFrequencyIP());
        txtCreditInterestRateIP.setText(observable.getTxtCreditInterestRateIP());
        txtAddIntRateIP.setText(observable.getTxtAddIntRateIP());
        rdoIsApplicableFCharges_Yes.setSelected(observable.getRdoIsApplicableFCharges_Yes());
        
        rdoIsApplicableFCharges_No.setSelected(observable.getRdoIsApplicableFCharges_No());
        if(observable.getRdoIsApplicableFCharges_No()){
            rdoIsApplicableFCharges_NoActionPerformed(null);
        }
        
        txtFolioEntriesFCharges.setText(observable.getTxtFolioEntriesFCharges());
        txtRateFCharges.setText(observable.getTxtRateFCharges());
        cboChargeOnTransactionFCharges.setSelectedItem(observable.getCboChargeOnTransactionFCharges());
        cboCAFrequencyFCharges.setSelectedItem(observable.getCboCAFrequencyFCharges());
        cboCollectChargeFCharges.setSelectedItem(observable.getCboCollectChargeFCharges());
        cboChargeOnDocFCharges.setSelectedItem(observable.getCboChargeOnDocFCharges());
        cboIRFrequencyFCharges.setSelectedItem(observable.getCboIRFrequencyFCharges());
        rdoIsStatementCharges_Yes.setSelected(observable.getRdoIsStatementCharges_Yes());
        
        rdoIsStatementCharges_No.setSelected(observable.getRdoIsStatementCharges_No());
        if(observable.getRdoIsStatementCharges_No()){
            rdoIsStatementCharges_NoActionPerformed(null);
        }
        rdoIsChequebookCharges_Yes.setSelected(observable.getRdoIsChequebookCharges_Yes());
        
        rdoIsChequebookCharges_No.setSelected(observable.getRdoIsChequebookCharges_No());
        if(observable.getRdoIsChequebookCharges_No()){
            rdoIsChequebookCharges_NoActionPerformed(null);
        }
        
        rdoIsStopPaymentCharges_Yes.setSelected(observable.getRdoIsStopPaymentCharges_Yes());
        
        rdoIsStopPaymentCharges_No.setSelected(observable.getRdoIsStopPaymentCharges_No());
        if(observable.getRdoIsStopPaymentCharges_No()){
            rdoIsStopPaymentCharges_NoActionPerformed(null);
        }
        
        rdoIsProcessingCharges_Yes.setSelected(observable.getRdoIsProcessingCharges_Yes());
        
        rdoIsProcessingCharges_No.setSelected(observable.getRdoIsProcessingCharges_No());
        if(observable.getRdoIsProcessingCharges_No()){
            rdoIsProcessingCharges_NoActionPerformed(null);
        }
        
        txtProcessingCharges.setText(observable.getTxtProcessingCharges());
        cboProcessingCharges.setSelectedItem(observable.getCboProcessingCharges());
        rdoIsCommitmentCharges_Yes.setSelected(observable.getRdoIsCommitmentCharges_Yes());
        
        rdoIsCommitmentCharges_No.setSelected(observable.getRdoIsCommitmentCharges_No());
        if(observable.getRdoIsCommitmentCharges_No()){
            rdoIsCommitmentCharges_NoActionPerformed(null);
        }
        
        txtCommitmentCharges.setText(observable.getTxtCommitmentCharges());
        cboCommitmentCharges.setSelectedItem(observable.getCboCommitmentCharges());
        txtAccountClosingCharges.setText(observable.getTxtAccountClosingCharges());
        txtStatementCharges.setText(observable.getTxtStatementCharges());
        txtMiscServiceCharges.setText(observable.getTxtMiscServiceCharges());
        txtChequebookCharges.setText(observable.getTxtChequebookCharges());
        txtStopPaymentCharges.setText(observable.getTxtStopPaymentCharges());
        cboAmountCRIC.setSelectedItem(observable.getCboAmountCRIC());
        txtRateCRIC.setText(observable.getTxtRateCRIC());
        cboAmountCROC.setSelectedItem(observable.getCboAmountCROC());
        txtRateCROC.setText(observable.getTxtRateCROC());
        rdoATMCardSI_Yes.setSelected(observable.getRdoATMCardSI_Yes());
        rdoATMCardSI_No.setSelected(observable.getRdoATMCardSI_No());
        rdoCreditCardSI_Yes.setSelected(observable.getRdoCreditCardSI_Yes());
        rdoCreditCardSI_No.setSelected(observable.getRdoCreditCardSI_No());
        rdoDebitCardSI_Yes.setSelected(observable.getRdoDebitCardSI_Yes());
        rdoDebitCardSI_No.setSelected(observable.getRdoDebitCardSI_No());
        rdoMobileBankingClientSI_Yes.setSelected(observable.getRdoMobileBankingClientSI_Yes());
        rdoMobileBankingClientSI_No.setSelected(observable.getRdoMobileBankingClientSI_No());
        rdoBranchBankingSI_Yes.setSelected(observable.getRdoBranchBankingSI_Yes());
        rdoBranchBankingSI_No.setSelected(observable.getRdoBranchBankingSI_No());
        txtRateSI.setText(observable.getTxtRateSI());
        cboAssetsSI.setSelectedItem(observable.getCboAssetsSI());
        txtACCAH.setText(observable.getTxtACCAH());
        txtMSCAH.setText(observable.getTxtMSCAH());
        txtSCAH.setText(observable.getTxtSCAH());
        txtDIAH.setText(observable.getTxtDIAH());
        txtPIAH.setText(observable.getTxtPIAH());
        txtCIAH.setText(observable.getTxtCIAH());
        txtAgCIAH.setText(observable.getTxtAgCIAH());
        txtEIAH.setText(observable.getTxtEIAH());
        txtExOLHAH.setText(observable.getTxtExOLHAH());
        txtCICAH.setText(observable.getTxtCICAH());
        txtSPCAH.setText(observable.getTxtSPCAH());
        txtCRCInwardAH.setText(observable.getTxtCRCInwardAH());
        txtCRCoutwardAH.setText(observable.getTxtCRCoutwardAH());
        txtFCAH.setText(observable.getTxtFCAH());
        tdtCLStartAccount.setDateValue(observable.getTdtCLStartAccount());
        tdtInterestCDDebitIR.setDateValue(observable.getTdtInterestCDDebitIR());
        tdtInterestADDebitIR.setDateValue(observable.getTdtInterestADDebitIR());
        tdtAppliedFromPLRIR.setDateValue(observable.getTdtAppliedFromPLRIR());
        tdtAccountSFPLRIR.setDateValue(observable.getTdtAccountSFPLRIR());
        tdtLastCDIP.setDateValue(observable.getTdtLastCDIP());
        tdtLastADIP.setDateValue(observable.getTdtLastADIP());
        tdtLastAppliedFCharges.setDateValue(observable.getTdtLastAppliedFCharges());
        tdtDueDateFCharges.setDateValue(observable.getTdtDueDateFCharges());
        txtCreditInterestAmt.setText(observable.getTxtCreditInterestAmt());
        
        //Setting the Data Model of tables used in UI.
        tbmMiscItemSI = observable.getTbmMiscItemSI();
        tbmCRChargesInward = observable.getTbmCRChargesInward();
        tbmCRChargesOutward = observable.getTbmCRChargesOutward();
        
        //To set the Status...
        lblStatus.setText(observable.getLblStatus());
        
        addRadioButtons();
    }
    
    private void initComponentData() {
        try{
            cboStmtFrequency.setModel(observable.getCbmStmtFrequency());
            cboBehavesLike.setModel(observable.getCbmBehavesLike());
            cboCLPeriod.setModel(observable.getCbmCLPeriod());
            cboDICalculationFIR.setModel(observable.getCbmDICalculationFIR());
            cboDIApplicationFIR.setModel(observable.getCbmDIApplicationFIR());
            cboDICompoundFIR.setModel(observable.getCbmDICompoundFIR());
            cboProductFOthersIR.setModel(observable.getCbmProductFOthersIR());
            cboDPRoundOffIR.setModel(observable.getCbmDPRoundOffIR());
            cboDIRoundOffIR.setModel(observable.getCbmDIRoundOffIR());
            cboCreditInterestCFIP.setModel(observable.getCbmCreditInterestCFIP());
            cboCreditInterestAFIP.setModel(observable.getCbmCreditInterestAFIP());
            cboCreditInterestCompdFIP.setModel(observable.getCbmCreditInterestCompdFIP());
            cboProdFrequencyIP.setModel(observable.getCbmProdFrequencyIP());
            cboCAFrequencyFCharges.setModel(observable.getCbmCAFrequencyFCharges());
            cboIRFrequencyFCharges.setModel(observable.getCbmIRFrequencyFCharges());
            cboCPROIP.setModel(observable.getCbmCPROIP());
            cboCIROIP.setModel(observable.getCbmCIROIP());
            cboCalcCtriteriaIP.setModel(observable.getCbmCalcCtriteriaIP());
            cboCollectChargeFCharges.setModel(observable.getCbmCollectChargeFCharges());
            cboAmountCRIC.setModel(observable.getCbmAmountCRIC());
            cboAmountCROC.setModel(observable.getCbmAmountCROC());
            cboAssetsSI.setModel(observable.getCbmAssetsSI());
            cboChargeOnTransactionFCharges.setModel(observable.getCbmChargeOnTransactionFCharges());
            cboChargeOnDocFCharges.setModel(observable.getCbmChargeOnDocFCharges());
            cboProcessingCharges.setModel(observable.getCbmProcessingCharges());
            cboCommitmentCharges.setModel(observable.getCbmCommitmentCharges());
            //            cboProdCurrency.setModel(observable.getCbmProdCurrency());
            
            tbmMiscItemSI = observable.getTbmMiscItemSI();
            tbmCRChargesInward = observable.getTbmCRChargesInward();
            tbmCRChargesOutward = observable.getTbmCRChargesOutward();
            
            /*cboAccountType.setModel(_observable.getCbmAccountType());
            cboMajorHead.setModel(_observable.getCbmMajorHead());
            cboSubHead.setModel(_observable.getCbmSubHead());*/
        }catch(ClassCastException e){
            System.out.println("Inside Intilize Data");
            e.printStackTrace();
        }
    }
    
    public void updateOBFields() {
        observable.setTxtLastAccNoAccount(txtLastAccNoAccount.getText());
        observable.setCboBehavesLike((String) cboBehavesLike.getSelectedItem());
        observable.setTxtCLPeriodAccount(txtCLPeriodAccount.getText());
        observable.setCboCLPeriod((String) cboCLPeriod.getSelectedItem());
        //                observable.setCboProdCurrency((String) ((ComboBoxModel) cboProdCurrency.getModel()).getKeyForSelected());
        observable.setTxtAccountHeadAccount(txtAccountHeadAccount.getText());
        observable.setTxtManagerDistAccount(txtManagerDistAccount.getText());
        observable.setTxtFreeCLAccount(txtFreeCLAccount.getText());
        observable.setCboStmtFrequency((String) cboStmtFrequency.getSelectedItem());
        observable.setTxtNumberpatternAccount(txtNumberpatternAccount.getText());
        observable.setRdoLDAccount_Yes(rdoLDAccount_Yes.isSelected());
        observable.setRdoLDAccount_No(rdoLDAccount_No.isSelected());
        observable.setRdoACAAccount_Yes(rdoACAAccount_Yes.isSelected());
        observable.setRdoACAAccount_No(rdoACAAccount_No.isSelected());
        observable.setRdoSAOAccount_Yes(rdoSAOAccount_Yes.isSelected());
        observable.setRdoSAOAccount_No(rdoSAOAccount_No.isSelected());
        observable.setRdoTokanAccount_Yes(rdoTokanAccount_Yes.isSelected());
        observable.setRdoTokanAccount_No(rdoTokanAccount_No.isSelected());
        observable.setRdoCIUEAccount_Yes(rdoCIUEAccount_Yes.isSelected());
        observable.setRdoCIUEAccount_No(rdoCIUEAccount_No.isSelected());
        observable.setRdoODALAccount_Yes(rdoODALAccount_Yes.isSelected());
        observable.setRdoODALAccount_No(rdoODALAccount_No.isSelected());
        observable.setRdoDIAUEAccount_Yes(rdoDIAUEAccount_Yes.isSelected());
        observable.setRdoDIAUEAccount_No(rdoDIAUEAccount_No.isSelected());
        observable.setRdoWSAccount_Yes(rdoWSAccount_Yes.isSelected());
        observable.setRdoWSAccount_No(rdoWSAccount_No.isSelected());
        observable.setTxtMaxAmountOnWS(txtMaxAmountOnWS.getText());
        observable.setTxtProductIdAccount(txtProductIdAccount.getText());
        observable.setTxtProductDescAccount(txtProductDescAccount.getText());
        observable.setRdoChargedDIIR_Yes(rdoChargedDIIR_Yes.isSelected());
        observable.setRdoChargedDIIR_No(rdoChargedDIIR_No.isSelected());
        observable.setTxtMinDIRateIR(txtMinDIRateIR.getText());
        observable.setTxtMaxDIRateIR(txtMaxDIRateIR.getText());
        observable.setTxtMinDIAmtIR(txtMinDIAmtIR.getText());
        observable.setTxtMaxDIAmtIR(txtMaxDIAmtIR.getText());
        observable.setCboDICalculationFIR((String) cboDICalculationFIR.getSelectedItem());
        observable.setCboDIApplicationFIR((String) cboDIApplicationFIR.getSelectedItem());
        observable.setRdoDebitCompoundIR_Yes(rdoDebitCompoundIR_Yes.isSelected());
        observable.setRdoDebitCompoundIR_No(rdoDebitCompoundIR_No.isSelected());
        observable.setCboDICompoundFIR((String) cboDICompoundFIR.getSelectedItem());
        observable.setCboDPRoundOffIR((String) cboDPRoundOffIR.getSelectedItem());
        observable.setCboDIRoundOffIR((String) cboDIRoundOffIR.getSelectedItem());
        observable.setCboProductFOthersIR((String) cboProductFOthersIR.getSelectedItem());
        observable.setRdoUAICOthersIR_Yes(rdoUAICOthersIR_Yes.isSelected());
        observable.setRdoUAICOthersIR_No(rdoUAICOthersIR_No.isSelected());
        observable.setRdoEOLOthersIR_Yes(rdoEOLOthersIR_Yes.isSelected());
        observable.setRdoEOLOthersIR_No(rdoEOLOthersIR_No.isSelected());
        observable.setRdoPenalOthersIR_Yes(rdoPenalOthersIR_Yes.isSelected());
        observable.setRdoPenalOthersIR_No(rdoPenalOthersIR_No.isSelected());
        observable.setRdoLimitEIOthersIR_Yes(rdoLimitEIOthersIR_Yes.isSelected());
        observable.setRdoLimitEIOthersIR_No(rdoLimitEIOthersIR_No.isSelected());
        observable.setTxtPenalIROthersIR(txtPenalIROthersIR.getText());
        observable.setRdoIsApplicablePLRIR_Yes(rdoIsApplicablePLRIR_Yes.isSelected());
        observable.setRdoIsApplicablePLRIR_No(rdoIsApplicablePLRIR_No.isSelected());
        observable.setTxtRatePLRIR(txtRatePLRIR.getText());
        observable.setRdoNewAccountPLRIR_Yes(rdoNewAccountPLRIR_Yes.isSelected());
        observable.setRdoNewAccountPLRIR_No(rdoNewAccountPLRIR_No.isSelected());
        observable.setRdoExistingAccountPLRIR_Yes(rdoExistingAccountPLRIR_Yes.isSelected());
        observable.setRdoExistingAccountPLRIR_No(rdoExistingAccountPLRIR_No.isSelected());
        observable.setRdoCreditIntInterestPayable_Yes(rdoCreditIntInterestPayable_Yes.isSelected());
        observable.setRdoCreditIntInterestPayable_No(rdoCreditIntInterestPayable_No.isSelected());
        observable.setRdoCreditCompdInterestPayable_Yes(rdoCreditCompdInterestPayable_Yes.isSelected());
        observable.setRdoCreditCompdInterestPayable_No(rdoCreditCompdInterestPayable_No.isSelected());
        observable.setRdoAdditionalIntInterestPayable_Yes(rdoAdditionalIntInterestPayable_Yes.isSelected());
        observable.setRdoAdditionalIntInterestPayable_No(rdoAdditionalIntInterestPayable_No.isSelected());
        observable.setCboCreditInterestCFIP((String) cboCreditInterestCFIP.getSelectedItem());
        observable.setCboCreditInterestAFIP((String) cboCreditInterestAFIP.getSelectedItem());
        observable.setCboCreditInterestCompdFIP((String) cboCreditInterestCompdFIP.getSelectedItem());
        observable.setCboCPROIP((String) cboCPROIP.getSelectedItem());
        observable.setCboCIROIP((String) cboCIROIP.getSelectedItem());
        observable.setCboCalcCtriteriaIP((String) cboCalcCtriteriaIP.getSelectedItem());
        observable.setCboProdFrequencyIP((String) cboProdFrequencyIP.getSelectedItem());
        observable.setTxtCreditInterestRateIP(txtCreditInterestRateIP.getText());
        observable.setTxtAddIntRateIP(txtAddIntRateIP.getText());
        observable.setRdoIsApplicableFCharges_Yes(rdoIsApplicableFCharges_Yes.isSelected());
        observable.setRdoIsApplicableFCharges_No(rdoIsApplicableFCharges_No.isSelected());
        observable.setTxtFolioEntriesFCharges(txtFolioEntriesFCharges.getText());
        observable.setTxtRateFCharges(txtRateFCharges.getText());
        observable.setCboChargeOnTransactionFCharges((String) cboChargeOnTransactionFCharges.getSelectedItem());
        observable.setCboCAFrequencyFCharges((String) cboCAFrequencyFCharges.getSelectedItem());
        observable.setCboCollectChargeFCharges((String) cboCollectChargeFCharges.getSelectedItem());
        observable.setCboChargeOnDocFCharges((String) cboChargeOnDocFCharges.getSelectedItem());
        observable.setCboIRFrequencyFCharges((String) cboIRFrequencyFCharges.getSelectedItem());
        observable.setRdoIsStatementCharges_Yes(rdoIsStatementCharges_Yes.isSelected());
        observable.setRdoIsStatementCharges_No(rdoIsStatementCharges_No.isSelected());
        observable.setRdoIsChequebookCharges_Yes(rdoIsChequebookCharges_Yes.isSelected());
        observable.setRdoIsChequebookCharges_No(rdoIsChequebookCharges_No.isSelected());
        observable.setRdoIsStopPaymentCharges_Yes(rdoIsStopPaymentCharges_Yes.isSelected());
        observable.setRdoIsStopPaymentCharges_No(rdoIsStopPaymentCharges_No.isSelected());
        observable.setRdoIsProcessingCharges_Yes(rdoIsProcessingCharges_Yes.isSelected());
        observable.setRdoIsProcessingCharges_No(rdoIsProcessingCharges_No.isSelected());
        observable.setTxtProcessingCharges(txtProcessingCharges.getText());
        observable.setCboProcessingCharges((String) cboProcessingCharges.getSelectedItem());
        observable.setRdoIsCommitmentCharges_Yes(rdoIsCommitmentCharges_Yes.isSelected());
        observable.setRdoIsCommitmentCharges_No(rdoIsCommitmentCharges_No.isSelected());
        observable.setTxtCommitmentCharges(txtCommitmentCharges.getText());
        observable.setCboCommitmentCharges((String) cboCommitmentCharges.getSelectedItem());
        observable.setTxtAccountClosingCharges(txtAccountClosingCharges.getText());
        observable.setTxtStatementCharges(txtStatementCharges.getText());
        observable.setTxtMiscServiceCharges(txtMiscServiceCharges.getText());
        observable.setTxtChequebookCharges(txtChequebookCharges.getText());
        observable.setTxtStopPaymentCharges(txtStopPaymentCharges.getText());
        observable.setCboAmountCRIC((String) cboAmountCRIC.getSelectedItem());
        observable.setTxtRateCRIC(txtRateCRIC.getText());
        observable.setCboAmountCROC((String) cboAmountCROC.getSelectedItem());
        observable.setTxtRateCROC(txtRateCROC.getText());
        observable.setRdoATMCardSI_Yes(rdoATMCardSI_Yes.isSelected());
        observable.setRdoATMCardSI_No(rdoATMCardSI_No.isSelected());
        observable.setRdoCreditCardSI_Yes(rdoCreditCardSI_Yes.isSelected());
        observable.setRdoCreditCardSI_No(rdoCreditCardSI_No.isSelected());
        observable.setRdoDebitCardSI_Yes(rdoDebitCardSI_Yes.isSelected());
        observable.setRdoDebitCardSI_No(rdoDebitCardSI_No.isSelected());
        observable.setRdoMobileBankingClientSI_Yes(rdoMobileBankingClientSI_Yes.isSelected());
        observable.setRdoMobileBankingClientSI_No(rdoMobileBankingClientSI_No.isSelected());
        observable.setRdoBranchBankingSI_Yes(rdoBranchBankingSI_Yes.isSelected());
        observable.setRdoBranchBankingSI_No(rdoBranchBankingSI_No.isSelected());
        observable.setTxtRateSI(txtRateSI.getText());
        observable.setCboAssetsSI((String) cboAssetsSI.getSelectedItem());
        observable.setTxtACCAH(txtACCAH.getText());
        observable.setTxtMSCAH(txtMSCAH.getText());
        observable.setTxtSCAH(txtSCAH.getText());
        observable.setTxtDIAH(txtDIAH.getText());
        observable.setTxtPIAH(txtPIAH.getText());
        observable.setTxtCIAH(txtCIAH.getText());
        observable.setTxtAgCIAH(txtAgCIAH.getText());
        observable.setTxtEIAH(txtEIAH.getText());
        observable.setTxtExOLHAH(txtExOLHAH.getText());
        observable.setTxtCICAH(txtCICAH.getText());
        observable.setTxtSPCAH(txtSPCAH.getText());
        observable.setTxtCRCInwardAH(txtCRCInwardAH.getText());
        observable.setTxtCRCoutwardAH(txtCRCoutwardAH.getText());
        observable.setTxtFCAH(txtFCAH.getText());
        observable.setTxtCreditInterestAmt(txtCreditInterestAmt.getText());
        
        //__ Date Fields...
        observable.setTdtCLStartAccount(tdtCLStartAccount.getDateValue());
        observable.setTdtInterestCDDebitIR(tdtInterestCDDebitIR.getDateValue());
        observable.setTdtInterestADDebitIR(tdtInterestADDebitIR.getDateValue());
        observable.setTdtAppliedFromPLRIR(tdtAppliedFromPLRIR.getDateValue());
        observable.setTdtAccountSFPLRIR(tdtAccountSFPLRIR.getDateValue());
        observable.setTdtLastCDIP(tdtLastCDIP.getDateValue());
        observable.setTdtLastADIP(tdtLastADIP.getDateValue());
        observable.setTdtLastAppliedFCharges(tdtLastAppliedFCharges.getDateValue());
        observable.setTdtDueDateFCharges(tdtDueDateFCharges.getDateValue());
        
        //setting the table Models.
        observable.setTbmMiscItemSI((EnhancedTableModel)tbmMiscItemSI);
        observable.setTbmCRChargesInward((EnhancedTableModel)tbmCRChargesInward);
        observable.setTbmCRChargesOutward((EnhancedTableModel)tbmCRChargesOutward);
    }
    
    public void setHelpMessage() {
        objMandatoryRB = new AdvancesProductMRB();
        //                cboProdCurrency.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProdCurrency"));
        txtLastAccNoAccount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLastAccNoAccount"));
        cboBehavesLike.setHelpMessage(lblMsg, objMandatoryRB.getString("cboBehavesLike"));
        tdtCLStartAccount.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtCLStartAccount"));
        txtCLPeriodAccount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCLPeriodAccount"));
        cboCLPeriod.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCLPeriod"));
        txtAccountHeadAccount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAccountHeadAccount"));
        txtManagerDistAccount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtManagerDistAccount"));
        txtFreeCLAccount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtFreeCLAccount"));
        cboStmtFrequency.setHelpMessage(lblMsg, objMandatoryRB.getString("cboStmtFrequency"));
        txtNumberpatternAccount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNumberpatternAccount"));
        rdoLDAccount_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoLDAccount_Yes"));
        rdoACAAccount_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoACAAccount_Yes"));
        rdoSAOAccount_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoSAOAccount_Yes"));
        rdoTokanAccount_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoTokanAccount_Yes"));
        rdoCIUEAccount_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoCIUEAccount_Yes"));
        rdoODALAccount_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoODALAccount_Yes"));
        rdoDIAUEAccount_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoDIAUEAccount_Yes"));
        rdoWSAccount_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoWSAccount_Yes"));
        txtMaxAmountOnWS.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMaxAmountOnWS"));
        txtProductIdAccount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtProductIdAccount"));
        txtProductDescAccount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtProductDescAccount"));
        rdoChargedDIIR_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoChargedDIIR_Yes"));
        txtMinDIRateIR.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMinDIRateIR"));
        txtMaxDIRateIR.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMaxDIRateIR"));
        txtMinDIAmtIR.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMinDIAmtIR"));
        txtMaxDIAmtIR.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMaxDIAmtIR"));
        cboDICalculationFIR.setHelpMessage(lblMsg, objMandatoryRB.getString("cboDICalculationFIR"));
        tdtInterestCDDebitIR.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtInterestCDDebitIR"));
        cboDIApplicationFIR.setHelpMessage(lblMsg, objMandatoryRB.getString("cboDIApplicationFIR"));
        tdtInterestADDebitIR.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtInterestADDebitIR"));
        rdoDebitCompoundIR_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoDebitCompoundIR_Yes"));
        cboDICompoundFIR.setHelpMessage(lblMsg, objMandatoryRB.getString("cboDICompoundFIR"));
        cboDPRoundOffIR.setHelpMessage(lblMsg, objMandatoryRB.getString("cboDPRoundOffIR"));
        cboDIRoundOffIR.setHelpMessage(lblMsg, objMandatoryRB.getString("cboDIRoundOffIR"));
        cboProductFOthersIR.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProductFOthersIR"));
        rdoUAICOthersIR_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoUAICOthersIR_Yes"));
        rdoEOLOthersIR_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoEOLOthersIR_Yes"));
        rdoPenalOthersIR_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoPenalOthersIR_Yes"));
        rdoLimitEIOthersIR_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoLimitEIOthersIR_Yes"));
        txtPenalIROthersIR.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPenalIROthersIR"));
        rdoIsApplicablePLRIR_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoIsApplicablePLRIR_Yes"));
        txtRatePLRIR.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRatePLRIR"));
        tdtAppliedFromPLRIR.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtAppliedFromPLRIR"));
        rdoNewAccountPLRIR_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoNewAccountPLRIR_Yes"));
        rdoExistingAccountPLRIR_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoExistingAccountPLRIR_Yes"));
        tdtAccountSFPLRIR.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtAccountSFPLRIR"));
        rdoCreditIntInterestPayable_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoCreditIntInterestPayable_Yes"));
        rdoCreditCompdInterestPayable_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoCreditCompdInterestPayable_Yes"));
        rdoAdditionalIntInterestPayable_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoAdditionalIntInterestPayable_Yes"));
        cboCreditInterestCFIP.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCreditInterestCFIP"));
        cboCreditInterestAFIP.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCreditInterestAFIP"));
        cboCreditInterestCompdFIP.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCreditInterestCompdFIP"));
        cboCPROIP.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCPROIP"));
        cboCIROIP.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCIROIP"));
        cboCalcCtriteriaIP.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCalcCtriteriaIP"));
        cboProdFrequencyIP.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProdFrequencyIP"));
        txtCreditInterestRateIP.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCreditInterestRateIP"));
        tdtLastCDIP.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtLastCDIP"));
        tdtLastADIP.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtLastADIP"));
        txtAddIntRateIP.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAddIntRateIP"));
        rdoIsApplicableFCharges_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoIsApplicableFCharges_Yes"));
        tdtLastAppliedFCharges.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtLastAppliedFCharges"));
        tdtDueDateFCharges.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtDueDateFCharges"));
        txtFolioEntriesFCharges.setHelpMessage(lblMsg, objMandatoryRB.getString("txtFolioEntriesFCharges"));
        txtRateFCharges.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRateFCharges"));
        cboChargeOnTransactionFCharges.setHelpMessage(lblMsg, objMandatoryRB.getString("cboChargeOnTransactionFCharges"));
        cboCAFrequencyFCharges.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCAFrequencyFCharges"));
        cboCollectChargeFCharges.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCollectChargeFCharges"));
        cboChargeOnDocFCharges.setHelpMessage(lblMsg, objMandatoryRB.getString("cboChargeOnDocFCharges"));
        cboIRFrequencyFCharges.setHelpMessage(lblMsg, objMandatoryRB.getString("cboIRFrequencyFCharges"));
        rdoIsStatementCharges_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoIsStatementCharges_Yes"));
        rdoIsChequebookCharges_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoIsChequebookCharges_Yes"));
        rdoIsStopPaymentCharges_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoIsStopPaymentCharges_Yes"));
        rdoIsProcessingCharges_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoIsProcessingCharges_Yes"));
        txtProcessingCharges.setHelpMessage(lblMsg, objMandatoryRB.getString("txtProcessingCharges"));
        cboProcessingCharges.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProcessingCharges"));
        rdoIsCommitmentCharges_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoIsCommitmentCharges_Yes"));
        txtCommitmentCharges.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCommitmentCharges"));
        cboCommitmentCharges.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCommitmentCharges"));
        txtAccountClosingCharges.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAccountClosingCharges"));
        txtStatementCharges.setHelpMessage(lblMsg, objMandatoryRB.getString("txtStatementCharges"));
        txtMiscServiceCharges.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMiscServiceCharges"));
        txtChequebookCharges.setHelpMessage(lblMsg, objMandatoryRB.getString("txtChequebookCharges"));
        txtStopPaymentCharges.setHelpMessage(lblMsg, objMandatoryRB.getString("txtStopPaymentCharges"));
        cboAmountCRIC.setHelpMessage(lblMsg, objMandatoryRB.getString("cboAmountCRIC"));
        txtRateCRIC.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRateCRIC"));
        cboAmountCROC.setHelpMessage(lblMsg, objMandatoryRB.getString("cboAmountCROC"));
        txtRateCROC.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRateCROC"));
        rdoATMCardSI_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoATMCardSI_Yes"));
        rdoCreditCardSI_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoCreditCardSI_Yes"));
        rdoDebitCardSI_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoDebitCardSI_Yes"));
        rdoMobileBankingClientSI_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoMobileBankingClientSI_Yes"));
        rdoBranchBankingSI_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoBranchBankingSI_Yes"));
        txtRateSI.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRateSI"));
        cboAssetsSI.setHelpMessage(lblMsg, objMandatoryRB.getString("cboAssetsSI"));
        txtACCAH.setHelpMessage(lblMsg, objMandatoryRB.getString("txtACCAH"));
        txtMSCAH.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMSCAH"));
        txtSCAH.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSCAH"));
        txtDIAH.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDIAH"));
        txtPIAH.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPIAH"));
        txtCIAH.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCIAH"));
        txtAgCIAH.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAgCIAH"));
        txtEIAH.setHelpMessage(lblMsg, objMandatoryRB.getString("txtEIAH"));
        txtExOLHAH.setHelpMessage(lblMsg, objMandatoryRB.getString("txtExOLHAH"));
        txtCICAH.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCICAH"));
        txtSPCAH.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSPCAH"));
        txtCRCInwardAH.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCRCInwardAH"));
        txtCRCoutwardAH.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCRCoutwardAH"));
        txtFCAH.setHelpMessage(lblMsg, objMandatoryRB.getString("txtFCAH"));
    }
    
    
    private void setObservable() {
        observable = AdvancesProductOB.getInstance();
        observable.addObserver(this);
    }
    
    /** This method 'll set the default value for all the
     * radiobuttons in the User Interface.
     */
    void rdosDefaultValues(){
        rdoLDAccount_Yes.setSelected(true);
        rdoLDAccount_No.setSelected(false);
        rdoACAAccount_Yes.setSelected(true);
        rdoACAAccount_No.setSelected(false);
        rdoSAOAccount_Yes.setSelected(true);
        rdoSAOAccount_No.setSelected(false);
        rdoTokanAccount_Yes.setSelected(true);
        rdoTokanAccount_No.setSelected(false);
        rdoCIUEAccount_Yes.setSelected(true);
        rdoCIUEAccount_No.setSelected(false);
        rdoODALAccount_Yes.setSelected(true);
        rdoODALAccount_No.setSelected(false);
        rdoDIAUEAccount_Yes.setSelected(true);
        rdoDIAUEAccount_No.setSelected(false);
        rdoWSAccount_Yes.setSelected(true);
        rdoWSAccount_No.setSelected(false);
        rdoChargedDIIR_Yes.setSelected(true);
        rdoChargedDIIR_No.setSelected(false);
        rdoDebitCompoundIR_Yes.setSelected(true);
        rdoDebitCompoundIR_No.setSelected(false);
        rdoUAICOthersIR_Yes.setSelected(true);
        rdoUAICOthersIR_No.setSelected(false);
        rdoEOLOthersIR_Yes.setSelected(true);
        rdoEOLOthersIR_No.setSelected(false);
        rdoPenalOthersIR_Yes.setSelected(true);
        rdoPenalOthersIR_No.setSelected(false);
        rdoLimitEIOthersIR_Yes.setSelected(true);
        rdoLimitEIOthersIR_No.setSelected(false);
        rdoNewAccountPLRIR_Yes.setSelected(true);
        rdoNewAccountPLRIR_No.setSelected(false);
        rdoExistingAccountPLRIR_No.setSelected(false);
        rdoExistingAccountPLRIR_Yes.setSelected(true);
        rdoIsApplicablePLRIR_Yes.setSelected(true);
        rdoIsApplicablePLRIR_No.setSelected(false);
        rdoCreditIntInterestPayable_Yes.setSelected(true);
        rdoCreditIntInterestPayable_No.setSelected(false);
        rdoCreditCompdInterestPayable_Yes.setSelected(true);
        rdoCreditCompdInterestPayable_No.setSelected(false);
        rdoAdditionalIntInterestPayable_Yes.setSelected(false);
        rdoAdditionalIntInterestPayable_No.setSelected(false);
        rdoIsApplicableFCharges_Yes.setSelected(true);
        rdoIsApplicableFCharges_No.setSelected(false);
        rdoIsStatementCharges_Yes.setSelected(true);
        rdoIsStatementCharges_No.setSelected(false);
        rdoIsChequebookCharges_Yes.setSelected(true);
        rdoIsChequebookCharges_No.setSelected(false);
        rdoIsStopPaymentCharges_Yes.setSelected(true);
        rdoIsStopPaymentCharges_No.setSelected(false);
        rdoIsProcessingCharges_Yes.setSelected(true);
        rdoIsProcessingCharges_No.setSelected(false);
        rdoIsCommitmentCharges_Yes.setSelected(true);
        rdoIsCommitmentCharges_No.setSelected(false);
        rdoATMCardSI_Yes.setSelected(true);
        rdoATMCardSI_No.setSelected(false);
        rdoCreditCardSI_Yes.setSelected(true);
        rdoCreditCardSI_No.setSelected(false);
        rdoDebitCardSI_Yes.setSelected(true);
        rdoDebitCardSI_No.setSelected(false);
        rdoMobileBankingClientSI_Yes.setSelected(true);
        rdoMobileBankingClientSI_No.setSelected(false);
        rdoBranchBankingSI_Yes.setSelected(true);
        rdoBranchBankingSI_No.setSelected(false);
    }
    
    //    void changePanelComponentState(java.awt.Container comp, com.see.truetransact.uicomponent.CButtonGroup buttonGroup, boolean newState){
    //
    //        System.out.println("Inside Disable Func New Satae"+newState);
    //        ClientUtil.enableDisable(comp, newState);
    //        java.util.Enumeration panelRdos = buttonGroup.getElements();
    //        com.see.truetransact.uicomponent.CRadioButton rdoButton;
    //        while( panelRdos.hasMoreElements()){
    //            rdoButton=(com.see.truetransact.uicomponent.CRadioButton)panelRdos.nextElement();
    //            rdoButton.setEnabled(true);
    //        }
    //
    //    }
    
    // This method'll make the Components Mandatory if the Corresponding Yes Radio Button is
    // selected.
    private void MandatoryOnRadoiButton(){
        if(rdoWSAccount_Yes.isSelected()){
            
        }
    }
    
    
    private void displayAlert(String message){
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    private void displayInfo(String message){
        final CMandatoryDialog cmd = new CMandatoryDialog(INFO_IMAGE);
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    private void setPanInvisible(){
        tabProduct.remove(1);
        tabProduct.remove(2);
        tabProduct.remove(2);
        tabProduct.remove(2);
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        rdoACAAcccount = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoLDAccount = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoODALAccount = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoSAOAccount = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoDIAUEAccount = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoCIUEAccount = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoTokenAccount = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoWSAccount = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoCreditIntInterestPayable = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoCreditCompdInterestPayable = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoAdditionalIntInterestPayable = new com.see.truetransact.uicomponent.CButtonGroup();
        cMenuBar1 = new com.see.truetransact.uicomponent.CMenuBar();
        rdoDebitCompoundIR = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoUAICOthersIR = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoEOLOthersIR = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoPenalOthersIR = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoLimitEIOthersIR = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoIsApplicablePLRIR = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoNewAccountPLRIR = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoExistingAccountPLRIR = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoChargedDIIR = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoATMCardSI = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoDebitCardSI = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoCreditCardSI = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoMobileBankingClientSI = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoBranchBankingSI = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoIsApplicableFCharges = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoIsStatementCharges = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoIsChequebookCharges = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoIsStopPaymentCharges = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoIsProcessingCharges = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoIsCommitmentCharges = new com.see.truetransact.uicomponent.CButtonGroup();
        cMenuBar2 = new com.see.truetransact.uicomponent.CMenuBar();
        jMenu1 = new javax.swing.JMenu();
        tabProduct = new com.see.truetransact.uicomponent.CTabbedPane();
        panAccount = new com.see.truetransact.uicomponent.CPanel();
        sptProductAccount = new com.see.truetransact.uicomponent.CSeparator();
        panInsideAccount = new com.see.truetransact.uicomponent.CPanel();
        lblCLPeriod = new com.see.truetransact.uicomponent.CLabel();
        txtLastAccNoAccount = new com.see.truetransact.uicomponent.CTextField();
        cboBehavesLike = new com.see.truetransact.uicomponent.CComboBox();
        tdtCLStartAccount = new com.see.truetransact.uicomponent.CDateField();
        lblBehaveLike = new com.see.truetransact.uicomponent.CLabel();
        panCLPI2Account = new com.see.truetransact.uicomponent.CPanel();
        txtCLPeriodAccount = new com.see.truetransact.uicomponent.CTextField();
        cboCLPeriod = new com.see.truetransact.uicomponent.CComboBox();
        panAccountHeadAccount = new com.see.truetransact.uicomponent.CPanel();
        txtAccountHeadAccount = new com.see.truetransact.uicomponent.CTextField();
        btnAccountHeadAccountHelp = new com.see.truetransact.uicomponent.CButton();
        lblMD4EOL = new com.see.truetransact.uicomponent.CLabel();
        lblCLStart = new com.see.truetransact.uicomponent.CLabel();
        lblLastAccNo = new com.see.truetransact.uicomponent.CLabel();
        panManagerDistAccount = new com.see.truetransact.uicomponent.CPanel();
        txtManagerDistAccount = new com.see.truetransact.uicomponent.CTextField();
        lblPercentageMDAccount = new com.see.truetransact.uicomponent.CLabel();
        txtFreeCLAccount = new com.see.truetransact.uicomponent.CTextField();
        cboStmtFrequency = new com.see.truetransact.uicomponent.CComboBox();
        lblNumberPattern = new com.see.truetransact.uicomponent.CLabel();
        txtNumberpatternAccount = new com.see.truetransact.uicomponent.CTextField();
        lblChequeLeaves = new com.see.truetransact.uicomponent.CLabel();
        lblStateFrequency = new com.see.truetransact.uicomponent.CLabel();
        lblAccountHead = new com.see.truetransact.uicomponent.CLabel();
        lblLimitDefination = new com.see.truetransact.uicomponent.CLabel();
        panLDI2Account = new com.see.truetransact.uicomponent.CPanel();
        rdoLDAccount_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoLDAccount_No = new com.see.truetransact.uicomponent.CRadioButton();
        panACAI2Account = new com.see.truetransact.uicomponent.CPanel();
        rdoACAAccount_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoACAAccount_No = new com.see.truetransact.uicomponent.CRadioButton();
        panSAOI2Account = new com.see.truetransact.uicomponent.CPanel();
        rdoSAOAccount_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoSAOAccount_No = new com.see.truetransact.uicomponent.CRadioButton();
        panTokenI2Account = new com.see.truetransact.uicomponent.CPanel();
        rdoTokanAccount_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoTokanAccount_No = new com.see.truetransact.uicomponent.CRadioButton();
        panCIUEI2Account = new com.see.truetransact.uicomponent.CPanel();
        rdoCIUEAccount_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoCIUEAccount_No = new com.see.truetransact.uicomponent.CRadioButton();
        panODALI2Account = new com.see.truetransact.uicomponent.CPanel();
        rdoODALAccount_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoODALAccount_No = new com.see.truetransact.uicomponent.CRadioButton();
        panDIUEI2Account = new com.see.truetransact.uicomponent.CPanel();
        rdoDIAUEAccount_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoDIAUEAccount_No = new com.see.truetransact.uicomponent.CRadioButton();
        panWSI2Account = new com.see.truetransact.uicomponent.CPanel();
        rdoWSAccount_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoWSAccount_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblWithdrawalSlip = new com.see.truetransact.uicomponent.CLabel();
        lblDebitInterestOnDAUE = new com.see.truetransact.uicomponent.CLabel();
        lblODOverAboveLimimt = new com.see.truetransact.uicomponent.CLabel();
        lblCreditInterestOnUE = new com.see.truetransact.uicomponent.CLabel();
        lblToken = new com.see.truetransact.uicomponent.CLabel();
        lblStaffAccOpened = new com.see.truetransact.uicomponent.CLabel();
        lblACA = new com.see.truetransact.uicomponent.CLabel();
        txtMaxAmountOnWS = new com.see.truetransact.uicomponent.CTextField();
        lblMaxAmount4WS = new com.see.truetransact.uicomponent.CLabel();
        panProductAccount = new com.see.truetransact.uicomponent.CPanel();
        lblProdId = new com.see.truetransact.uicomponent.CLabel();
        txtProductIdAccount = new com.see.truetransact.uicomponent.CTextField();
        txtProductDescAccount = new com.see.truetransact.uicomponent.CTextField();
        lblProdDesc = new com.see.truetransact.uicomponent.CLabel();
        lblProdCurrency = new com.see.truetransact.uicomponent.CLabel();
        cboProdCurrency = new com.see.truetransact.uicomponent.CComboBox();
        btnProdID = new com.see.truetransact.uicomponent.CButton();
        panInterestReceivable = new com.see.truetransact.uicomponent.CPanel();
        panDebitInterestIR = new com.see.truetransact.uicomponent.CPanel();
        panChargedDIIR = new com.see.truetransact.uicomponent.CPanel();
        rdoChargedDIIR_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoChargedDIIR_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblChargedDIIR = new com.see.truetransact.uicomponent.CLabel();
        lblMinDIRateIR = new com.see.truetransact.uicomponent.CLabel();
        panMinDIRateIR = new com.see.truetransact.uicomponent.CPanel();
        txtMinDIRateIR = new com.see.truetransact.uicomponent.CTextField();
        lblPercentageMinDIRateIR = new com.see.truetransact.uicomponent.CLabel();
        panMaxDIRateIR = new com.see.truetransact.uicomponent.CPanel();
        lblPercentageMaxDIRateIR = new com.see.truetransact.uicomponent.CLabel();
        txtMaxDIRateIR = new com.see.truetransact.uicomponent.CTextField();
        txtMinDIAmtIR = new com.see.truetransact.uicomponent.CTextField();
        txtMaxDIAmtIR = new com.see.truetransact.uicomponent.CTextField();
        cboDICalculationFIR = new com.see.truetransact.uicomponent.CComboBox();
        tdtInterestCDDebitIR = new com.see.truetransact.uicomponent.CDateField();
        cboDIApplicationFIR = new com.see.truetransact.uicomponent.CComboBox();
        tdtInterestADDebitIR = new com.see.truetransact.uicomponent.CDateField();
        panDebitCompoundIR = new com.see.truetransact.uicomponent.CPanel();
        rdoDebitCompoundIR_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoDebitCompoundIR_No = new com.see.truetransact.uicomponent.CRadioButton();
        cboDICompoundFIR = new com.see.truetransact.uicomponent.CComboBox();
        cboDPRoundOffIR = new com.see.truetransact.uicomponent.CComboBox();
        cboDIRoundOffIR = new com.see.truetransact.uicomponent.CComboBox();
        lblDIRoundOffIR = new com.see.truetransact.uicomponent.CLabel();
        lblDPRoundOffIR = new com.see.truetransact.uicomponent.CLabel();
        lblDICompoundFIR = new com.see.truetransact.uicomponent.CLabel();
        lblDCRequiredIR = new com.see.truetransact.uicomponent.CLabel();
        lblInterestADDebitIR = new com.see.truetransact.uicomponent.CLabel();
        lblDIApplicationFIR = new com.see.truetransact.uicomponent.CLabel();
        lblInterestCDDebitIR = new com.see.truetransact.uicomponent.CLabel();
        lblDICalculationFIR = new com.see.truetransact.uicomponent.CLabel();
        lblMaxDIAmtIR = new com.see.truetransact.uicomponent.CLabel();
        lblMinDIAmtIR = new com.see.truetransact.uicomponent.CLabel();
        lblMaxDIRateIR = new com.see.truetransact.uicomponent.CLabel();
        panOthersIR = new com.see.truetransact.uicomponent.CPanel();
        lblProductFOthersIR = new com.see.truetransact.uicomponent.CLabel();
        lblUAICOthersIR = new com.see.truetransact.uicomponent.CLabel();
        lblEOLOthersIR = new com.see.truetransact.uicomponent.CLabel();
        lblPenalOthersIR = new com.see.truetransact.uicomponent.CLabel();
        lblLimitEIOthersIR = new com.see.truetransact.uicomponent.CLabel();
        lblPenalIROthersIR = new com.see.truetransact.uicomponent.CLabel();
        cboProductFOthersIR = new com.see.truetransact.uicomponent.CComboBox();
        rdoUAICOthersIR_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoUAICOthersIR_No = new com.see.truetransact.uicomponent.CRadioButton();
        rdoEOLOthersIR_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoEOLOthersIR_No = new com.see.truetransact.uicomponent.CRadioButton();
        rdoPenalOthersIR_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoPenalOthersIR_No = new com.see.truetransact.uicomponent.CRadioButton();
        rdoLimitEIOthersIR_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoLimitEIOthersIR_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblPercentagePenalIROthersIR = new com.see.truetransact.uicomponent.CLabel();
        txtPenalIROthersIR = new com.see.truetransact.uicomponent.CTextField();
        panPLRIR = new com.see.truetransact.uicomponent.CPanel();
        panIsApplicablePLRIR = new com.see.truetransact.uicomponent.CPanel();
        rdoIsApplicablePLRIR_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoIsApplicablePLRIR_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblIsApplicablePLRIR = new com.see.truetransact.uicomponent.CLabel();
        lblRatePLRIR = new com.see.truetransact.uicomponent.CLabel();
        lblPercentageRatePLRIR = new com.see.truetransact.uicomponent.CLabel();
        txtRatePLRIR = new com.see.truetransact.uicomponent.CTextField();
        lblAppliedFromPLRIR = new com.see.truetransact.uicomponent.CLabel();
        tdtAppliedFromPLRIR = new com.see.truetransact.uicomponent.CDateField();
        rdoNewAccountPLRIR_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoNewAccountPLRIR_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblNewAccountPLRIR = new com.see.truetransact.uicomponent.CLabel();
        lblExistingAccountPLRIR = new com.see.truetransact.uicomponent.CLabel();
        rdoExistingAccountPLRIR_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoExistingAccountPLRIR_No = new com.see.truetransact.uicomponent.CRadioButton();
        tdtAccountSFPLRIR = new com.see.truetransact.uicomponent.CDateField();
        lblAccountSFPLRIR = new com.see.truetransact.uicomponent.CLabel();
        panInterestPayable = new com.see.truetransact.uicomponent.CPanel();
        lblCreditInterestIP = new com.see.truetransact.uicomponent.CLabel();
        lblCreditInterestRateIIP = new com.see.truetransact.uicomponent.CLabel();
        lblCreditInterestCFIP = new com.see.truetransact.uicomponent.CLabel();
        lblCrediInterestAFIP = new com.see.truetransact.uicomponent.CLabel();
        lblLastCDIP = new com.see.truetransact.uicomponent.CLabel();
        lblLastADIP = new com.see.truetransact.uicomponent.CLabel();
        lblCreditCompdIP = new com.see.truetransact.uicomponent.CLabel();
        lblCreditInterestCompdFIP = new com.see.truetransact.uicomponent.CLabel();
        lblCPROIP = new com.see.truetransact.uicomponent.CLabel();
        lblCIROIP = new com.see.truetransact.uicomponent.CLabel();
        lblCalcCriteriaIP = new com.see.truetransact.uicomponent.CLabel();
        lblProdFrequencyIP = new com.see.truetransact.uicomponent.CLabel();
        lblAIStaffIP = new com.see.truetransact.uicomponent.CLabel();
        panCreditInterestIP = new com.see.truetransact.uicomponent.CPanel();
        rdoCreditIntInterestPayable_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoCreditIntInterestPayable_No = new com.see.truetransact.uicomponent.CRadioButton();
        panCreditCompdIP = new com.see.truetransact.uicomponent.CPanel();
        rdoCreditCompdInterestPayable_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoCreditCompdInterestPayable_No = new com.see.truetransact.uicomponent.CRadioButton();
        panAddInterestIP = new com.see.truetransact.uicomponent.CPanel();
        rdoAdditionalIntInterestPayable_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoAdditionalIntInterestPayable_No = new com.see.truetransact.uicomponent.CRadioButton();
        cboCreditInterestCFIP = new com.see.truetransact.uicomponent.CComboBox();
        cboCreditInterestAFIP = new com.see.truetransact.uicomponent.CComboBox();
        cboCreditInterestCompdFIP = new com.see.truetransact.uicomponent.CComboBox();
        cboCPROIP = new com.see.truetransact.uicomponent.CComboBox();
        cboCIROIP = new com.see.truetransact.uicomponent.CComboBox();
        cboCalcCtriteriaIP = new com.see.truetransact.uicomponent.CComboBox();
        cboProdFrequencyIP = new com.see.truetransact.uicomponent.CComboBox();
        panCreditInterestRateIP = new com.see.truetransact.uicomponent.CPanel();
        txtCreditInterestRateIP = new com.see.truetransact.uicomponent.CTextField();
        lblPercentageCreditInterestRateIP = new com.see.truetransact.uicomponent.CLabel();
        tdtLastCDIP = new com.see.truetransact.uicomponent.CDateField();
        tdtLastADIP = new com.see.truetransact.uicomponent.CDateField();
        lblAIRStaffIP = new com.see.truetransact.uicomponent.CLabel();
        panAddInterestRateIP = new com.see.truetransact.uicomponent.CPanel();
        txtAddIntRateIP = new com.see.truetransact.uicomponent.CTextField();
        lblAddIntRateIP = new com.see.truetransact.uicomponent.CLabel();
        lblCreditInterestAmt = new com.see.truetransact.uicomponent.CLabel();
        txtCreditInterestAmt = new com.see.truetransact.uicomponent.CTextField();
        panChagres = new com.see.truetransact.uicomponent.CPanel();
        panFolioCharges = new com.see.truetransact.uicomponent.CPanel();
        panIsApplicableFCharges = new com.see.truetransact.uicomponent.CPanel();
        rdoIsApplicableFCharges_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoIsApplicableFCharges_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblLastAppliedFCharges = new com.see.truetransact.uicomponent.CLabel();
        tdtLastAppliedFCharges = new com.see.truetransact.uicomponent.CDateField();
        lblDueDateFCharges = new com.see.truetransact.uicomponent.CLabel();
        tdtDueDateFCharges = new com.see.truetransact.uicomponent.CDateField();
        lblFolioEntriesFCharges = new com.see.truetransact.uicomponent.CLabel();
        txtFolioEntriesFCharges = new com.see.truetransact.uicomponent.CTextField();
        txtRateFCharges = new com.see.truetransact.uicomponent.CTextField();
        lblRateFCharges = new com.see.truetransact.uicomponent.CLabel();
        lblChargeOnTransactionFCharges = new com.see.truetransact.uicomponent.CLabel();
        cboChargeOnTransactionFCharges = new com.see.truetransact.uicomponent.CComboBox();
        cboCAFrequencyFCharges = new com.see.truetransact.uicomponent.CComboBox();
        lblCAFrequencyFCharges = new com.see.truetransact.uicomponent.CLabel();
        lblCollectChargeFCharges = new com.see.truetransact.uicomponent.CLabel();
        cboCollectChargeFCharges = new com.see.truetransact.uicomponent.CComboBox();
        cboChargeOnDocFCharges = new com.see.truetransact.uicomponent.CComboBox();
        lblChargeOnDocFCharges = new com.see.truetransact.uicomponent.CLabel();
        cboIRFrequencyFCharges = new com.see.truetransact.uicomponent.CComboBox();
        lblIRFrequencyFCharges = new com.see.truetransact.uicomponent.CLabel();
        lblIsApplicableFCharges = new com.see.truetransact.uicomponent.CLabel();
        panOtherCharges = new com.see.truetransact.uicomponent.CPanel();
        panIsStatementCharges = new com.see.truetransact.uicomponent.CPanel();
        rdoIsStatementCharges_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoIsStatementCharges_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblIsStatementCharges = new com.see.truetransact.uicomponent.CLabel();
        lblStatementCharges = new com.see.truetransact.uicomponent.CLabel();
        lblIsProcessingCharges = new com.see.truetransact.uicomponent.CLabel();
        lblAccountCLosingCharges = new com.see.truetransact.uicomponent.CLabel();
        lblMicsServiceCharges = new com.see.truetransact.uicomponent.CLabel();
        lblProcessingCharges = new com.see.truetransact.uicomponent.CLabel();
        panIsProcessingCharges = new com.see.truetransact.uicomponent.CPanel();
        rdoIsProcessingCharges_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoIsProcessingCharges_No = new com.see.truetransact.uicomponent.CRadioButton();
        panProcessingCharges = new com.see.truetransact.uicomponent.CPanel();
        lblPSProcessingCharges = new com.see.truetransact.uicomponent.CLabel();
        txtProcessingCharges = new com.see.truetransact.uicomponent.CTextField();
        cboProcessingCharges = new com.see.truetransact.uicomponent.CComboBox();
        lblIsCommitmentCharges = new com.see.truetransact.uicomponent.CLabel();
        panIsCommitmentCharges = new com.see.truetransact.uicomponent.CPanel();
        rdoIsCommitmentCharges_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoIsCommitmentCharges_No = new com.see.truetransact.uicomponent.CRadioButton();
        panCommitmentCharges = new com.see.truetransact.uicomponent.CPanel();
        lblPSCommitmentCharges = new com.see.truetransact.uicomponent.CLabel();
        txtCommitmentCharges = new com.see.truetransact.uicomponent.CTextField();
        cboCommitmentCharges = new com.see.truetransact.uicomponent.CComboBox();
        txtAccountClosingCharges = new com.see.truetransact.uicomponent.CTextField();
        txtStatementCharges = new com.see.truetransact.uicomponent.CTextField();
        txtMiscServiceCharges = new com.see.truetransact.uicomponent.CTextField();
        panCRChargesInward = new com.see.truetransact.uicomponent.CPanel();
        cboAmountCRIC = new com.see.truetransact.uicomponent.CComboBox();
        txtRateCRIC = new com.see.truetransact.uicomponent.CTextField();
        panButtonsCRIC = new com.see.truetransact.uicomponent.CPanel();
        btnNewCRIC = new com.see.truetransact.uicomponent.CButton();
        btnSaveCRIC = new com.see.truetransact.uicomponent.CButton();
        btnDeleteCRIC = new com.see.truetransact.uicomponent.CButton();
        lblAmountCRIC = new com.see.truetransact.uicomponent.CLabel();
        lblRateCRIC = new com.see.truetransact.uicomponent.CLabel();
        srpCRChargesInward = new com.see.truetransact.uicomponent.CScrollPane();
        tblCRChargesInward = new com.see.truetransact.uicomponent.CTable();
        panCRChargesOutward = new com.see.truetransact.uicomponent.CPanel();
        cboAmountCROC = new com.see.truetransact.uicomponent.CComboBox();
        txtRateCROC = new com.see.truetransact.uicomponent.CTextField();
        panButtonsCROC = new com.see.truetransact.uicomponent.CPanel();
        btnNewCROC = new com.see.truetransact.uicomponent.CButton();
        btnSaveCROC = new com.see.truetransact.uicomponent.CButton();
        btnDeleteCROC = new com.see.truetransact.uicomponent.CButton();
        lblAmountCROC = new com.see.truetransact.uicomponent.CLabel();
        lblRateCROC = new com.see.truetransact.uicomponent.CLabel();
        srpCRChargesOutward = new com.see.truetransact.uicomponent.CScrollPane();
        tblCRChargesOutward = new com.see.truetransact.uicomponent.CTable();
        panSplItems = new com.see.truetransact.uicomponent.CPanel();
        panSplItemDetails = new com.see.truetransact.uicomponent.CPanel();
        lblATMCardSI = new com.see.truetransact.uicomponent.CLabel();
        lblCreditCardSI = new com.see.truetransact.uicomponent.CLabel();
        lblMobileBankingClientSI = new com.see.truetransact.uicomponent.CLabel();
        lblBranchBankingSI = new com.see.truetransact.uicomponent.CLabel();
        rdoATMCardSI_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoATMCardSI_No = new com.see.truetransact.uicomponent.CRadioButton();
        rdoCreditCardSI_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoCreditCardSI_No = new com.see.truetransact.uicomponent.CRadioButton();
        rdoMobileBankingClientSI_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoMobileBankingClientSI_No = new com.see.truetransact.uicomponent.CRadioButton();
        rdoBranchBankingSI_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoBranchBankingSI_No = new com.see.truetransact.uicomponent.CRadioButton();
        panMiscItemSI = new com.see.truetransact.uicomponent.CPanel();
        srpMiscItemSI = new com.see.truetransact.uicomponent.CScrollPane();
        tblMiscItemSI = new com.see.truetransact.uicomponent.CTable();
        txtRateSI = new com.see.truetransact.uicomponent.CTextField();
        cboAssetsSI = new com.see.truetransact.uicomponent.CComboBox();
        lblAssetsSI = new com.see.truetransact.uicomponent.CLabel();
        panButtonSI = new com.see.truetransact.uicomponent.CPanel();
        btnNewSI = new com.see.truetransact.uicomponent.CButton();
        btnSaveSI = new com.see.truetransact.uicomponent.CButton();
        btnDeleteSI = new com.see.truetransact.uicomponent.CButton();
        lblRateSI = new com.see.truetransact.uicomponent.CLabel();
        panAccountHead = new com.see.truetransact.uicomponent.CPanel();
        lblACCAH = new com.see.truetransact.uicomponent.CLabel();
        lblMSCAH = new com.see.truetransact.uicomponent.CLabel();
        lblSCAH = new com.see.truetransact.uicomponent.CLabel();
        lblDIAH = new com.see.truetransact.uicomponent.CLabel();
        lblPIAH = new com.see.truetransact.uicomponent.CLabel();
        lblCIAH = new com.see.truetransact.uicomponent.CLabel();
        lblAgCIAH = new com.see.truetransact.uicomponent.CLabel();
        lblEIAH = new com.see.truetransact.uicomponent.CLabel();
        lblCRCInwardAH = new com.see.truetransact.uicomponent.CLabel();
        lblCRCOutwardAH = new com.see.truetransact.uicomponent.CLabel();
        lblFCAH = new com.see.truetransact.uicomponent.CLabel();
        txtACCAH = new com.see.truetransact.uicomponent.CTextField();
        txtMSCAH = new com.see.truetransact.uicomponent.CTextField();
        txtSCAH = new com.see.truetransact.uicomponent.CTextField();
        txtDIAH = new com.see.truetransact.uicomponent.CTextField();
        txtPIAH = new com.see.truetransact.uicomponent.CTextField();
        txtCIAH = new com.see.truetransact.uicomponent.CTextField();
        txtAgCIAH = new com.see.truetransact.uicomponent.CTextField();
        txtEIAH = new com.see.truetransact.uicomponent.CTextField();
        txtCRCInwardAH = new com.see.truetransact.uicomponent.CTextField();
        txtCRCoutwardAH = new com.see.truetransact.uicomponent.CTextField();
        txtFCAH = new com.see.truetransact.uicomponent.CTextField();
        btnACCAHHelp = new com.see.truetransact.uicomponent.CButton();
        btnMSCAHHelp = new com.see.truetransact.uicomponent.CButton();
        btnSCAHHelp = new com.see.truetransact.uicomponent.CButton();
        btnDIAHHelp = new com.see.truetransact.uicomponent.CButton();
        btnPIAHHelp = new com.see.truetransact.uicomponent.CButton();
        btnCIAHHelp = new com.see.truetransact.uicomponent.CButton();
        btnAgCIAHHelp = new com.see.truetransact.uicomponent.CButton();
        btnEIAHHelp = new com.see.truetransact.uicomponent.CButton();
        btnCRCInwardAHHelp = new com.see.truetransact.uicomponent.CButton();
        btnCRCoutwardAHHelp = new com.see.truetransact.uicomponent.CButton();
        btnFCAHHelp = new com.see.truetransact.uicomponent.CButton();
        panMiscdata = new com.see.truetransact.uicomponent.CPanel();
        panCharges = new com.see.truetransact.uicomponent.CPanel();
        panIsChequebookCharges = new com.see.truetransact.uicomponent.CPanel();
        rdoIsChequebookCharges_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoIsChequebookCharges_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblIsChequebookCharges = new com.see.truetransact.uicomponent.CLabel();
        txtChequebookCharges = new com.see.truetransact.uicomponent.CTextField();
        txtStopPaymentCharges = new com.see.truetransact.uicomponent.CTextField();
        lblIsStopPaymentCharges = new com.see.truetransact.uicomponent.CLabel();
        panIsStopPaymentCharges = new com.see.truetransact.uicomponent.CPanel();
        rdoIsStopPaymentCharges_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoIsStopPaymentCharges_No = new com.see.truetransact.uicomponent.CRadioButton();
        panSpecialItems = new com.see.truetransact.uicomponent.CPanel();
        lblDebitCardSI = new com.see.truetransact.uicomponent.CLabel();
        rdoDebitCardSI_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoDebitCardSI_No = new com.see.truetransact.uicomponent.CRadioButton();
        panActHead = new com.see.truetransact.uicomponent.CPanel();
        lblExOLHAH = new com.see.truetransact.uicomponent.CLabel();
        txtExOLHAH = new com.see.truetransact.uicomponent.CTextField();
        btnExOLHAHelp = new com.see.truetransact.uicomponent.CButton();
        lblCICAH = new com.see.truetransact.uicomponent.CLabel();
        txtCICAH = new com.see.truetransact.uicomponent.CTextField();
        btnCICAHHelp = new com.see.truetransact.uicomponent.CButton();
        lblSPCAH = new com.see.truetransact.uicomponent.CLabel();
        txtSPCAH = new com.see.truetransact.uicomponent.CTextField();
        btnSPCAHHelp = new com.see.truetransact.uicomponent.CButton();
        tbrAdvances = new javax.swing.JToolBar();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
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
        mitClose = new javax.swing.JMenuItem();

        jMenu1.setText("Menu");
        cMenuBar2.add(jMenu1);

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Create Product - Account Details");
        setMinimumSize(new java.awt.Dimension(750, 625));
        setPreferredSize(new java.awt.Dimension(750, 625));
        tabProduct.setMinimumSize(new java.awt.Dimension(750, 600));
        tabProduct.setPreferredSize(new java.awt.Dimension(750, 600));
        panAccount.setLayout(new java.awt.GridBagLayout());

        panAccount.setPreferredSize(new java.awt.Dimension(750, 600));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panAccount.add(sptProductAccount, gridBagConstraints);

        panInsideAccount.setLayout(new java.awt.GridBagLayout());

        lblCLPeriod.setText("Free Cheque Leaves Period");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideAccount.add(lblCLPeriod, gridBagConstraints);

        txtLastAccNoAccount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideAccount.add(txtLastAccNoAccount, gridBagConstraints);

        cboBehavesLike.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideAccount.add(cboBehavesLike, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideAccount.add(tdtCLStartAccount, gridBagConstraints);

        lblBehaveLike.setText("Operates Like");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideAccount.add(lblBehaveLike, gridBagConstraints);

        panCLPI2Account.setLayout(new java.awt.GridBagLayout());

        txtCLPeriodAccount.setPreferredSize(new java.awt.Dimension(25, 21));
        txtCLPeriodAccount.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panCLPI2Account.add(txtCLPeriodAccount, gridBagConstraints);

        cboCLPeriod.setPreferredSize(new java.awt.Dimension(70, 21));
        panCLPI2Account.add(cboCLPeriod, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideAccount.add(panCLPI2Account, gridBagConstraints);

        panAccountHeadAccount.setLayout(new java.awt.GridBagLayout());

        txtAccountHeadAccount.setEditable(false);
        txtAccountHeadAccount.setMinimumSize(new java.awt.Dimension(70, 21));
        txtAccountHeadAccount.setPreferredSize(new java.awt.Dimension(70, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panAccountHeadAccount.add(txtAccountHeadAccount, gridBagConstraints);

        btnAccountHeadAccountHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnAccountHeadAccountHelp.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAccountHeadAccountHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccountHeadAccountHelpActionPerformed(evt);
            }
        });

        panAccountHeadAccount.add(btnAccountHeadAccountHelp, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideAccount.add(panAccountHeadAccount, gridBagConstraints);

        lblMD4EOL.setText("Percentage of Manager Discounts for EOL");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideAccount.add(lblMD4EOL, gridBagConstraints);

        lblCLStart.setText("Free Cheque Leaves Starting From");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideAccount.add(lblCLStart, gridBagConstraints);

        lblLastAccNo.setText("Last Account Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideAccount.add(lblLastAccNo, gridBagConstraints);

        panManagerDistAccount.setLayout(new java.awt.GridBagLayout());

        txtManagerDistAccount.setMinimumSize(new java.awt.Dimension(40, 21));
        txtManagerDistAccount.setPreferredSize(new java.awt.Dimension(40, 21));
        txtManagerDistAccount.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panManagerDistAccount.add(txtManagerDistAccount, gridBagConstraints);

        lblPercentageMDAccount.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panManagerDistAccount.add(lblPercentageMDAccount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideAccount.add(panManagerDistAccount, gridBagConstraints);

        txtFreeCLAccount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtFreeCLAccount.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideAccount.add(txtFreeCLAccount, gridBagConstraints);

        cboStmtFrequency.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideAccount.add(cboStmtFrequency, gridBagConstraints);

        lblNumberPattern.setText("Numbering Patterns to be Followed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideAccount.add(lblNumberPattern, gridBagConstraints);

        txtNumberpatternAccount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideAccount.add(txtNumberpatternAccount, gridBagConstraints);

        lblChequeLeaves.setText("Number of Free Cheque Leaves");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideAccount.add(lblChequeLeaves, gridBagConstraints);

        lblStateFrequency.setText("Statement Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideAccount.add(lblStateFrequency, gridBagConstraints);

        lblAccountHead.setText("Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideAccount.add(lblAccountHead, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 9;
        panAccount.add(panInsideAccount, gridBagConstraints);

        lblLimitDefination.setText("Is Limit Definition Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccount.add(lblLimitDefination, gridBagConstraints);

        panLDI2Account.setLayout(new java.awt.GridBagLayout());

        rdoLDAccount_Yes.setText("Yes");
        rdoLDAccount.add(rdoLDAccount_Yes);
        rdoLDAccount_Yes.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoLDAccount_Yes.setPreferredSize(new java.awt.Dimension(46, 21));
        rdoLDAccount_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoLDAccount_YesActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panLDI2Account.add(rdoLDAccount_Yes, gridBagConstraints);

        rdoLDAccount_No.setText("No");
        rdoLDAccount.add(rdoLDAccount_No);
        rdoLDAccount_No.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoLDAccount_No.setPreferredSize(new java.awt.Dimension(46, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panLDI2Account.add(rdoLDAccount_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccount.add(panLDI2Account, gridBagConstraints);

        panACAI2Account.setLayout(new java.awt.GridBagLayout());

        rdoACAAccount_Yes.setText("Yes");
        rdoACAAcccount.add(rdoACAAccount_Yes);
        rdoACAAccount_Yes.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoACAAccount_Yes.setPreferredSize(new java.awt.Dimension(46, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panACAI2Account.add(rdoACAAccount_Yes, gridBagConstraints);

        rdoACAAccount_No.setText("No");
        rdoACAAcccount.add(rdoACAAccount_No);
        rdoACAAccount_No.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoACAAccount_No.setPreferredSize(new java.awt.Dimension(46, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panACAI2Account.add(rdoACAAccount_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccount.add(panACAI2Account, gridBagConstraints);

        panSAOI2Account.setLayout(new java.awt.GridBagLayout());

        rdoSAOAccount_Yes.setText("Yes");
        rdoSAOAccount.add(rdoSAOAccount_Yes);
        rdoSAOAccount_Yes.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoSAOAccount_Yes.setPreferredSize(new java.awt.Dimension(46, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panSAOI2Account.add(rdoSAOAccount_Yes, gridBagConstraints);

        rdoSAOAccount_No.setText("No");
        rdoSAOAccount.add(rdoSAOAccount_No);
        rdoSAOAccount_No.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoSAOAccount_No.setPreferredSize(new java.awt.Dimension(46, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panSAOI2Account.add(rdoSAOAccount_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccount.add(panSAOI2Account, gridBagConstraints);

        panTokenI2Account.setLayout(new java.awt.GridBagLayout());

        rdoTokanAccount_Yes.setText("Yes");
        rdoTokenAccount.add(rdoTokanAccount_Yes);
        rdoTokanAccount_Yes.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoTokanAccount_Yes.setPreferredSize(new java.awt.Dimension(46, 21));
        rdoTokanAccount_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoTokanAccount_YesActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panTokenI2Account.add(rdoTokanAccount_Yes, gridBagConstraints);

        rdoTokanAccount_No.setText("No");
        rdoTokenAccount.add(rdoTokanAccount_No);
        rdoTokanAccount_No.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoTokanAccount_No.setPreferredSize(new java.awt.Dimension(46, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panTokenI2Account.add(rdoTokanAccount_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccount.add(panTokenI2Account, gridBagConstraints);

        panCIUEI2Account.setLayout(new java.awt.GridBagLayout());

        rdoCIUEAccount_Yes.setText("Yes");
        rdoCIUEAccount.add(rdoCIUEAccount_Yes);
        rdoCIUEAccount_Yes.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoCIUEAccount_Yes.setPreferredSize(new java.awt.Dimension(46, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panCIUEI2Account.add(rdoCIUEAccount_Yes, gridBagConstraints);

        rdoCIUEAccount_No.setText("No");
        rdoCIUEAccount.add(rdoCIUEAccount_No);
        rdoCIUEAccount_No.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoCIUEAccount_No.setPreferredSize(new java.awt.Dimension(46, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panCIUEI2Account.add(rdoCIUEAccount_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccount.add(panCIUEI2Account, gridBagConstraints);

        panODALI2Account.setLayout(new java.awt.GridBagLayout());

        rdoODALAccount_Yes.setText("Yes");
        rdoODALAccount.add(rdoODALAccount_Yes);
        rdoODALAccount_Yes.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoODALAccount_Yes.setPreferredSize(new java.awt.Dimension(46, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panODALI2Account.add(rdoODALAccount_Yes, gridBagConstraints);

        rdoODALAccount_No.setText("No");
        rdoODALAccount.add(rdoODALAccount_No);
        rdoODALAccount_No.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoODALAccount_No.setPreferredSize(new java.awt.Dimension(46, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panODALI2Account.add(rdoODALAccount_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccount.add(panODALI2Account, gridBagConstraints);

        panDIUEI2Account.setLayout(new java.awt.GridBagLayout());

        rdoDIAUEAccount_Yes.setText("Yes");
        rdoDIAUEAccount.add(rdoDIAUEAccount_Yes);
        rdoDIAUEAccount_Yes.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoDIAUEAccount_Yes.setPreferredSize(new java.awt.Dimension(46, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panDIUEI2Account.add(rdoDIAUEAccount_Yes, gridBagConstraints);

        rdoDIAUEAccount_No.setText("No");
        rdoDIAUEAccount.add(rdoDIAUEAccount_No);
        rdoDIAUEAccount_No.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoDIAUEAccount_No.setPreferredSize(new java.awt.Dimension(46, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panDIUEI2Account.add(rdoDIAUEAccount_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccount.add(panDIUEI2Account, gridBagConstraints);

        panWSI2Account.setLayout(new java.awt.GridBagLayout());

        rdoWSAccount_Yes.setText("Yes");
        rdoWSAccount.add(rdoWSAccount_Yes);
        rdoWSAccount_Yes.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoWSAccount_Yes.setPreferredSize(new java.awt.Dimension(46, 21));
        rdoWSAccount_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoWSAccount_YesActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panWSI2Account.add(rdoWSAccount_Yes, gridBagConstraints);

        rdoWSAccount_No.setText("No");
        rdoWSAccount.add(rdoWSAccount_No);
        rdoWSAccount_No.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoWSAccount_No.setPreferredSize(new java.awt.Dimension(46, 21));
        rdoWSAccount_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoWSAccount_NoActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panWSI2Account.add(rdoWSAccount_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccount.add(panWSI2Account, gridBagConstraints);

        lblWithdrawalSlip.setText("Allow Withdrawal Slip");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccount.add(lblWithdrawalSlip, gridBagConstraints);

        lblDebitInterestOnDAUE.setText("Debit Interest Against Unclear Effect");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccount.add(lblDebitInterestOnDAUE, gridBagConstraints);

        lblODOverAboveLimimt.setText(" O/D Allowed Over and Above Limit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccount.add(lblODOverAboveLimimt, gridBagConstraints);

        lblCreditInterestOnUE.setText("Credit Interest on Unclear Effect");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccount.add(lblCreditInterestOnUE, gridBagConstraints);

        lblToken.setText("Issue Token");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccount.add(lblToken, gridBagConstraints);

        lblStaffAccOpened.setText("Is Staff Account Opened");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccount.add(lblStaffAccOpened, gridBagConstraints);

        lblACA.setText("Are Chequebooks Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccount.add(lblACA, gridBagConstraints);

        txtMaxAmountOnWS.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMaxAmountOnWS.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccount.add(txtMaxAmountOnWS, gridBagConstraints);

        lblMaxAmount4WS.setText("Maximum Amount  for withdrawal Slip");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccount.add(lblMaxAmount4WS, gridBagConstraints);

        panProductAccount.setLayout(new java.awt.GridBagLayout());

        lblProdId.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductAccount.add(lblProdId, gridBagConstraints);

        txtProductIdAccount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtProductIdAccount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtProductIdAccountFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductAccount.add(txtProductIdAccount, gridBagConstraints);

        txtProductDescAccount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductAccount.add(txtProductDescAccount, gridBagConstraints);

        lblProdDesc.setText("Product Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductAccount.add(lblProdDesc, gridBagConstraints);

        lblProdCurrency.setText("Product Currency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panProductAccount.add(lblProdCurrency, gridBagConstraints);

        cboProdCurrency.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panProductAccount.add(cboProdCurrency, gridBagConstraints);

        btnProdID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnProdID.setToolTipText("Product ID");
        btnProdID.setPreferredSize(new java.awt.Dimension(21, 21));
        btnProdID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProdIDActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panProductAccount.add(btnProdID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panAccount.add(panProductAccount, gridBagConstraints);

        tabProduct.addTab("Account", panAccount);

        panInterestReceivable.setLayout(new java.awt.GridBagLayout());

        panInterestReceivable.setPreferredSize(new java.awt.Dimension(850, 550));
        panDebitInterestIR.setLayout(new java.awt.GridBagLayout());

        panDebitInterestIR.setBorder(new javax.swing.border.TitledBorder("Debit Interest Details"));
        panDebitInterestIR.setMinimumSize(new java.awt.Dimension(360, 400));
        panDebitInterestIR.setPreferredSize(new java.awt.Dimension(360, 415));
        panChargedDIIR.setLayout(new java.awt.GridBagLayout());

        rdoChargedDIIR_Yes.setText("Yes");
        rdoChargedDIIR.add(rdoChargedDIIR_Yes);
        rdoChargedDIIR_Yes.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoChargedDIIR_Yes.setPreferredSize(new java.awt.Dimension(46, 21));
        rdoChargedDIIR_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoChargedDIIR_YesActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panChargedDIIR.add(rdoChargedDIIR_Yes, gridBagConstraints);

        rdoChargedDIIR_No.setText("No");
        rdoChargedDIIR.add(rdoChargedDIIR_No);
        rdoChargedDIIR_No.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoChargedDIIR_No.setPreferredSize(new java.awt.Dimension(46, 21));
        rdoChargedDIIR_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoChargedDIIR_NoActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panChargedDIIR.add(rdoChargedDIIR_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitInterestIR.add(panChargedDIIR, gridBagConstraints);

        lblChargedDIIR.setText("Is Debit Interest Applicable");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitInterestIR.add(lblChargedDIIR, gridBagConstraints);

        lblMinDIRateIR.setText("Minimum Debit Interest Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitInterestIR.add(lblMinDIRateIR, gridBagConstraints);

        panMinDIRateIR.setLayout(new java.awt.GridBagLayout());

        txtMinDIRateIR.setMinimumSize(new java.awt.Dimension(40, 21));
        txtMinDIRateIR.setPreferredSize(new java.awt.Dimension(40, 21));
        txtMinDIRateIR.setValidation(new NumericValidation());
        txtMinDIRateIR.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMinDIRateIRFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panMinDIRateIR.add(txtMinDIRateIR, gridBagConstraints);

        lblPercentageMinDIRateIR.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panMinDIRateIR.add(lblPercentageMinDIRateIR, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitInterestIR.add(panMinDIRateIR, gridBagConstraints);

        panMaxDIRateIR.setLayout(new java.awt.GridBagLayout());

        lblPercentageMaxDIRateIR.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panMaxDIRateIR.add(lblPercentageMaxDIRateIR, gridBagConstraints);

        txtMaxDIRateIR.setMinimumSize(new java.awt.Dimension(40, 21));
        txtMaxDIRateIR.setPreferredSize(new java.awt.Dimension(40, 21));
        txtMaxDIRateIR.setValidation(new NumericValidation());
        txtMaxDIRateIR.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMaxDIRateIRFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panMaxDIRateIR.add(txtMaxDIRateIR, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitInterestIR.add(panMaxDIRateIR, gridBagConstraints);

        txtMinDIAmtIR.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMinDIAmtIR.setValidation(new NumericValidation());
        txtMinDIAmtIR.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMinDIAmtIRFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitInterestIR.add(txtMinDIAmtIR, gridBagConstraints);

        txtMaxDIAmtIR.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMaxDIAmtIR.setValidation(new NumericValidation());
        txtMaxDIAmtIR.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMaxDIAmtIRFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitInterestIR.add(txtMaxDIAmtIR, gridBagConstraints);

        cboDICalculationFIR.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitInterestIR.add(cboDICalculationFIR, gridBagConstraints);

        tdtInterestCDDebitIR.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitInterestIR.add(tdtInterestCDDebitIR, gridBagConstraints);

        cboDIApplicationFIR.setMinimumSize(new java.awt.Dimension(100, 21));
        cboDIApplicationFIR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboDIApplicationFIRActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitInterestIR.add(cboDIApplicationFIR, gridBagConstraints);

        tdtInterestADDebitIR.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitInterestIR.add(tdtInterestADDebitIR, gridBagConstraints);

        panDebitCompoundIR.setLayout(new java.awt.GridBagLayout());

        rdoDebitCompoundIR_Yes.setText("Yes");
        rdoDebitCompoundIR.add(rdoDebitCompoundIR_Yes);
        rdoDebitCompoundIR_Yes.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoDebitCompoundIR_Yes.setPreferredSize(new java.awt.Dimension(46, 21));
        rdoDebitCompoundIR_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDebitCompoundIR_YesActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panDebitCompoundIR.add(rdoDebitCompoundIR_Yes, gridBagConstraints);

        rdoDebitCompoundIR_No.setText("No");
        rdoDebitCompoundIR.add(rdoDebitCompoundIR_No);
        rdoDebitCompoundIR_No.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoDebitCompoundIR_No.setPreferredSize(new java.awt.Dimension(46, 21));
        rdoDebitCompoundIR_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDebitCompoundIR_NoActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panDebitCompoundIR.add(rdoDebitCompoundIR_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitInterestIR.add(panDebitCompoundIR, gridBagConstraints);

        cboDICompoundFIR.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitInterestIR.add(cboDICompoundFIR, gridBagConstraints);

        cboDPRoundOffIR.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitInterestIR.add(cboDPRoundOffIR, gridBagConstraints);

        cboDIRoundOffIR.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitInterestIR.add(cboDIRoundOffIR, gridBagConstraints);

        lblDIRoundOffIR.setText("Debit Interest Round Off");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitInterestIR.add(lblDIRoundOffIR, gridBagConstraints);

        lblDPRoundOffIR.setText("Debit Product Round Off");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitInterestIR.add(lblDPRoundOffIR, gridBagConstraints);

        lblDICompoundFIR.setText("Debit Interest Compound Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitInterestIR.add(lblDICompoundFIR, gridBagConstraints);

        lblDCRequiredIR.setText("Debit Compound Required");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitInterestIR.add(lblDCRequiredIR, gridBagConstraints);

        lblInterestADDebitIR.setText("Last Interest Application Date - Debit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitInterestIR.add(lblInterestADDebitIR, gridBagConstraints);

        lblDIApplicationFIR.setText("Debit Interest Appliction Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitInterestIR.add(lblDIApplicationFIR, gridBagConstraints);

        lblInterestCDDebitIR.setText("Last Interest Calculated Date- Debit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitInterestIR.add(lblInterestCDDebitIR, gridBagConstraints);

        lblDICalculationFIR.setText("Debit Interest Calculation Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitInterestIR.add(lblDICalculationFIR, gridBagConstraints);

        lblMaxDIAmtIR.setText("Maximum Debit Interest Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitInterestIR.add(lblMaxDIAmtIR, gridBagConstraints);

        lblMinDIAmtIR.setText("Minimum Debit Interest Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitInterestIR.add(lblMinDIAmtIR, gridBagConstraints);

        lblMaxDIRateIR.setText("Maximum Debit Interest Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitInterestIR.add(lblMaxDIRateIR, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panInterestReceivable.add(panDebitInterestIR, gridBagConstraints);

        panOthersIR.setLayout(new java.awt.GridBagLayout());

        panOthersIR.setBorder(new javax.swing.border.TitledBorder("Other Details "));
        panOthersIR.setMinimumSize(new java.awt.Dimension(320, 200));
        panOthersIR.setPreferredSize(new java.awt.Dimension(450, 650));
        lblProductFOthersIR.setText("Product Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOthersIR.add(lblProductFOthersIR, gridBagConstraints);

        lblUAICOthersIR.setText("Unclear Advance Interest Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOthersIR.add(lblUAICOthersIR, gridBagConstraints);

        lblEOLOthersIR.setText("EOL Applicable");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOthersIR.add(lblEOLOthersIR, gridBagConstraints);

        lblPenalOthersIR.setText("Penal Applicable");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOthersIR.add(lblPenalOthersIR, gridBagConstraints);

        lblLimitEIOthersIR.setText("Limit Expiry Interest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOthersIR.add(lblLimitEIOthersIR, gridBagConstraints);

        lblPenalIROthersIR.setText("Penal Interest Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOthersIR.add(lblPenalIROthersIR, gridBagConstraints);

        cboProductFOthersIR.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOthersIR.add(cboProductFOthersIR, gridBagConstraints);

        rdoUAICOthersIR_Yes.setText("Yes");
        rdoUAICOthersIR.add(rdoUAICOthersIR_Yes);
        rdoUAICOthersIR_Yes.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoUAICOthersIR_Yes.setPreferredSize(new java.awt.Dimension(46, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOthersIR.add(rdoUAICOthersIR_Yes, gridBagConstraints);

        rdoUAICOthersIR_No.setText("No");
        rdoUAICOthersIR.add(rdoUAICOthersIR_No);
        rdoUAICOthersIR_No.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoUAICOthersIR_No.setPreferredSize(new java.awt.Dimension(46, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panOthersIR.add(rdoUAICOthersIR_No, gridBagConstraints);

        rdoEOLOthersIR_Yes.setText("Yes");
        rdoEOLOthersIR.add(rdoEOLOthersIR_Yes);
        rdoEOLOthersIR_Yes.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoEOLOthersIR_Yes.setPreferredSize(new java.awt.Dimension(46, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOthersIR.add(rdoEOLOthersIR_Yes, gridBagConstraints);

        rdoEOLOthersIR_No.setText("No");
        rdoEOLOthersIR.add(rdoEOLOthersIR_No);
        rdoEOLOthersIR_No.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoEOLOthersIR_No.setPreferredSize(new java.awt.Dimension(46, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panOthersIR.add(rdoEOLOthersIR_No, gridBagConstraints);

        rdoPenalOthersIR_Yes.setText("Yes");
        rdoPenalOthersIR.add(rdoPenalOthersIR_Yes);
        rdoPenalOthersIR_Yes.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoPenalOthersIR_Yes.setPreferredSize(new java.awt.Dimension(46, 21));
        rdoPenalOthersIR_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoPenalOthersIR_YesActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOthersIR.add(rdoPenalOthersIR_Yes, gridBagConstraints);

        rdoPenalOthersIR_No.setText("No");
        rdoPenalOthersIR.add(rdoPenalOthersIR_No);
        rdoPenalOthersIR_No.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoPenalOthersIR_No.setPreferredSize(new java.awt.Dimension(46, 21));
        rdoPenalOthersIR_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoPenalOthersIR_NoActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panOthersIR.add(rdoPenalOthersIR_No, gridBagConstraints);

        rdoLimitEIOthersIR_Yes.setText("Yes");
        rdoLimitEIOthersIR.add(rdoLimitEIOthersIR_Yes);
        rdoLimitEIOthersIR_Yes.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoLimitEIOthersIR_Yes.setPreferredSize(new java.awt.Dimension(46, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOthersIR.add(rdoLimitEIOthersIR_Yes, gridBagConstraints);

        rdoLimitEIOthersIR_No.setText("No");
        rdoLimitEIOthersIR.add(rdoLimitEIOthersIR_No);
        rdoLimitEIOthersIR_No.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoLimitEIOthersIR_No.setPreferredSize(new java.awt.Dimension(46, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panOthersIR.add(rdoLimitEIOthersIR_No, gridBagConstraints);

        lblPercentagePenalIROthersIR.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOthersIR.add(lblPercentagePenalIROthersIR, gridBagConstraints);

        txtPenalIROthersIR.setMinimumSize(new java.awt.Dimension(40, 21));
        txtPenalIROthersIR.setPreferredSize(new java.awt.Dimension(40, 21));
        txtPenalIROthersIR.setValidation(new NumericValidation());
        txtPenalIROthersIR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPenalIROthersIRActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOthersIR.add(txtPenalIROthersIR, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panInterestReceivable.add(panOthersIR, gridBagConstraints);

        panPLRIR.setLayout(new java.awt.GridBagLayout());

        panPLRIR.setBorder(new javax.swing.border.TitledBorder("PLR Details"));
        panPLRIR.setMinimumSize(new java.awt.Dimension(318, 200));
        panPLRIR.setPreferredSize(new java.awt.Dimension(801, 169));
        panIsApplicablePLRIR.setLayout(new java.awt.GridBagLayout());

        rdoIsApplicablePLRIR_Yes.setText("Yes");
        rdoIsApplicablePLRIR.add(rdoIsApplicablePLRIR_Yes);
        rdoIsApplicablePLRIR_Yes.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoIsApplicablePLRIR_Yes.setPreferredSize(new java.awt.Dimension(46, 21));
        rdoIsApplicablePLRIR_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoIsApplicablePLRIR_YesActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panIsApplicablePLRIR.add(rdoIsApplicablePLRIR_Yes, gridBagConstraints);

        rdoIsApplicablePLRIR_No.setText("No");
        rdoIsApplicablePLRIR.add(rdoIsApplicablePLRIR_No);
        rdoIsApplicablePLRIR_No.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoIsApplicablePLRIR_No.setPreferredSize(new java.awt.Dimension(46, 21));
        rdoIsApplicablePLRIR_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoIsApplicablePLRIR_NoActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panIsApplicablePLRIR.add(rdoIsApplicablePLRIR_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPLRIR.add(panIsApplicablePLRIR, gridBagConstraints);

        lblIsApplicablePLRIR.setText("Is PLR Applicable");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPLRIR.add(lblIsApplicablePLRIR, gridBagConstraints);

        lblRatePLRIR.setText("Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPLRIR.add(lblRatePLRIR, gridBagConstraints);

        lblPercentageRatePLRIR.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPLRIR.add(lblPercentageRatePLRIR, gridBagConstraints);

        txtRatePLRIR.setMinimumSize(new java.awt.Dimension(40, 21));
        txtRatePLRIR.setPreferredSize(new java.awt.Dimension(40, 21));
        txtRatePLRIR.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPLRIR.add(txtRatePLRIR, gridBagConstraints);

        lblAppliedFromPLRIR.setText("Applicable From");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPLRIR.add(lblAppliedFromPLRIR, gridBagConstraints);

        tdtAppliedFromPLRIR.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPLRIR.add(tdtAppliedFromPLRIR, gridBagConstraints);

        rdoNewAccountPLRIR_Yes.setText("Yes");
        rdoNewAccountPLRIR.add(rdoNewAccountPLRIR_Yes);
        rdoNewAccountPLRIR_Yes.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoNewAccountPLRIR_Yes.setPreferredSize(new java.awt.Dimension(46, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPLRIR.add(rdoNewAccountPLRIR_Yes, gridBagConstraints);

        rdoNewAccountPLRIR_No.setText("No");
        rdoNewAccountPLRIR.add(rdoNewAccountPLRIR_No);
        rdoNewAccountPLRIR_No.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoNewAccountPLRIR_No.setPreferredSize(new java.awt.Dimension(46, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPLRIR.add(rdoNewAccountPLRIR_No, gridBagConstraints);

        lblNewAccountPLRIR.setText(" For New Account");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPLRIR.add(lblNewAccountPLRIR, gridBagConstraints);

        lblExistingAccountPLRIR.setText("For Existing Accounts ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPLRIR.add(lblExistingAccountPLRIR, gridBagConstraints);

        rdoExistingAccountPLRIR_Yes.setText("Yes");
        rdoExistingAccountPLRIR.add(rdoExistingAccountPLRIR_Yes);
        rdoExistingAccountPLRIR_Yes.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoExistingAccountPLRIR_Yes.setPreferredSize(new java.awt.Dimension(46, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPLRIR.add(rdoExistingAccountPLRIR_Yes, gridBagConstraints);

        rdoExistingAccountPLRIR_No.setText("No");
        rdoExistingAccountPLRIR.add(rdoExistingAccountPLRIR_No);
        rdoExistingAccountPLRIR_No.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoExistingAccountPLRIR_No.setPreferredSize(new java.awt.Dimension(46, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPLRIR.add(rdoExistingAccountPLRIR_No, gridBagConstraints);

        tdtAccountSFPLRIR.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPLRIR.add(tdtAccountSFPLRIR, gridBagConstraints);

        lblAccountSFPLRIR.setText("For Account Sactioned From");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPLRIR.add(lblAccountSFPLRIR, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        panInterestReceivable.add(panPLRIR, gridBagConstraints);

        tabProduct.addTab("Interest Receivable", panInterestReceivable);

        panInterestPayable.setLayout(new java.awt.GridBagLayout());

        panInterestPayable.setMinimumSize(new java.awt.Dimension(100, 21));
        panInterestPayable.setPreferredSize(new java.awt.Dimension(100, 21));
        lblCreditInterestIP.setText("Credit Interest to be Paid");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panInterestPayable.add(lblCreditInterestIP, gridBagConstraints);

        lblCreditInterestRateIIP.setText("Applicble Credit Interest Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panInterestPayable.add(lblCreditInterestRateIIP, gridBagConstraints);

        lblCreditInterestCFIP.setText("Credit Interest Calculation Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panInterestPayable.add(lblCreditInterestCFIP, gridBagConstraints);

        lblCrediInterestAFIP.setText("Credit Interest Application Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panInterestPayable.add(lblCrediInterestAFIP, gridBagConstraints);

        lblLastCDIP.setText("Last Interest Calculated Date - Credit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panInterestPayable.add(lblLastCDIP, gridBagConstraints);

        lblLastADIP.setText("Last Interest Application Date- Credit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panInterestPayable.add(lblLastADIP, gridBagConstraints);

        lblCreditCompdIP.setText("Credit Compound");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panInterestPayable.add(lblCreditCompdIP, gridBagConstraints);

        lblCreditInterestCompdFIP.setText("Credit Interest Compounr Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panInterestPayable.add(lblCreditInterestCompdFIP, gridBagConstraints);

        lblCPROIP.setText("Credit Product Round Off");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panInterestPayable.add(lblCPROIP, gridBagConstraints);

        lblCIROIP.setText("Credit Interest Round Off");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panInterestPayable.add(lblCIROIP, gridBagConstraints);

        lblCalcCriteriaIP.setText("Calculation Criteria");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panInterestPayable.add(lblCalcCriteriaIP, gridBagConstraints);

        lblProdFrequencyIP.setText("Product Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panInterestPayable.add(lblProdFrequencyIP, gridBagConstraints);

        lblAIStaffIP.setText("Addtional Interest for Staff");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panInterestPayable.add(lblAIStaffIP, gridBagConstraints);

        panCreditInterestIP.setLayout(new java.awt.GridBagLayout());

        rdoCreditIntInterestPayable_Yes.setText("Yes");
        rdoCreditIntInterestPayable.add(rdoCreditIntInterestPayable_Yes);
        rdoCreditIntInterestPayable_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoCreditIntInterestPayable_YesActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panCreditInterestIP.add(rdoCreditIntInterestPayable_Yes, gridBagConstraints);

        rdoCreditIntInterestPayable_No.setText("No");
        rdoCreditIntInterestPayable.add(rdoCreditIntInterestPayable_No);
        rdoCreditIntInterestPayable_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoCreditIntInterestPayable_NoActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panCreditInterestIP.add(rdoCreditIntInterestPayable_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panInterestPayable.add(panCreditInterestIP, gridBagConstraints);

        panCreditCompdIP.setLayout(new java.awt.GridBagLayout());

        rdoCreditCompdInterestPayable_Yes.setText("Yes");
        rdoCreditCompdInterestPayable.add(rdoCreditCompdInterestPayable_Yes);
        rdoCreditCompdInterestPayable_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoCreditCompdInterestPayable_YesActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panCreditCompdIP.add(rdoCreditCompdInterestPayable_Yes, gridBagConstraints);

        rdoCreditCompdInterestPayable_No.setText("No");
        rdoCreditCompdInterestPayable.add(rdoCreditCompdInterestPayable_No);
        rdoCreditCompdInterestPayable_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoCreditCompdInterestPayable_NoActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panCreditCompdIP.add(rdoCreditCompdInterestPayable_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panInterestPayable.add(panCreditCompdIP, gridBagConstraints);

        panAddInterestIP.setLayout(new java.awt.GridBagLayout());

        rdoAdditionalIntInterestPayable_Yes.setText("Yes");
        rdoAdditionalIntInterestPayable.add(rdoAdditionalIntInterestPayable_Yes);
        rdoAdditionalIntInterestPayable_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoAdditionalIntInterestPayable_YesActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panAddInterestIP.add(rdoAdditionalIntInterestPayable_Yes, gridBagConstraints);

        rdoAdditionalIntInterestPayable_No.setText("No");
        rdoAdditionalIntInterestPayable.add(rdoAdditionalIntInterestPayable_No);
        rdoAdditionalIntInterestPayable_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoAdditionalIntInterestPayable_NoActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panAddInterestIP.add(rdoAdditionalIntInterestPayable_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panInterestPayable.add(panAddInterestIP, gridBagConstraints);

        cboCreditInterestCFIP.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panInterestPayable.add(cboCreditInterestCFIP, gridBagConstraints);

        cboCreditInterestAFIP.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panInterestPayable.add(cboCreditInterestAFIP, gridBagConstraints);

        cboCreditInterestCompdFIP.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panInterestPayable.add(cboCreditInterestCompdFIP, gridBagConstraints);

        cboCPROIP.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panInterestPayable.add(cboCPROIP, gridBagConstraints);

        cboCIROIP.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panInterestPayable.add(cboCIROIP, gridBagConstraints);

        cboCalcCtriteriaIP.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panInterestPayable.add(cboCalcCtriteriaIP, gridBagConstraints);

        cboProdFrequencyIP.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panInterestPayable.add(cboProdFrequencyIP, gridBagConstraints);

        panCreditInterestRateIP.setLayout(new java.awt.GridBagLayout());

        txtCreditInterestRateIP.setMinimumSize(new java.awt.Dimension(40, 21));
        txtCreditInterestRateIP.setPreferredSize(new java.awt.Dimension(40, 21));
        txtCreditInterestRateIP.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panCreditInterestRateIP.add(txtCreditInterestRateIP, gridBagConstraints);

        lblPercentageCreditInterestRateIP.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panCreditInterestRateIP.add(lblPercentageCreditInterestRateIP, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterestPayable.add(panCreditInterestRateIP, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panInterestPayable.add(tdtLastCDIP, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panInterestPayable.add(tdtLastADIP, gridBagConstraints);

        lblAIRStaffIP.setText("Interest Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panInterestPayable.add(lblAIRStaffIP, gridBagConstraints);

        panAddInterestRateIP.setLayout(new java.awt.GridBagLayout());

        txtAddIntRateIP.setMinimumSize(new java.awt.Dimension(40, 21));
        txtAddIntRateIP.setPreferredSize(new java.awt.Dimension(40, 21));
        txtAddIntRateIP.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panAddInterestRateIP.add(txtAddIntRateIP, gridBagConstraints);

        lblAddIntRateIP.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panAddInterestRateIP.add(lblAddIntRateIP, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panInterestPayable.add(panAddInterestRateIP, gridBagConstraints);

        lblCreditInterestAmt.setText("Minimum Credit Interest Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panInterestPayable.add(lblCreditInterestAmt, gridBagConstraints);

        txtCreditInterestAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCreditInterestAmt.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panInterestPayable.add(txtCreditInterestAmt, gridBagConstraints);

        tabProduct.addTab("Interest Payable", panInterestPayable);

        panChagres.setLayout(new java.awt.GridBagLayout());

        panChagres.setPreferredSize(new java.awt.Dimension(800, 600));
        panFolioCharges.setLayout(new java.awt.GridBagLayout());

        panFolioCharges.setBorder(new javax.swing.border.TitledBorder("Folio Charges"));
        panFolioCharges.setMinimumSize(new java.awt.Dimension(360, 325));
        panFolioCharges.setPreferredSize(new java.awt.Dimension(360, 335));
        panIsApplicableFCharges.setLayout(new java.awt.GridBagLayout());

        rdoIsApplicableFCharges_Yes.setText("Yes");
        rdoIsApplicableFCharges.add(rdoIsApplicableFCharges_Yes);
        rdoIsApplicableFCharges_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoIsApplicableFCharges_YesActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panIsApplicableFCharges.add(rdoIsApplicableFCharges_Yes, gridBagConstraints);

        rdoIsApplicableFCharges_No.setText("No");
        rdoIsApplicableFCharges.add(rdoIsApplicableFCharges_No);
        rdoIsApplicableFCharges_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoIsApplicableFCharges_NoActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panIsApplicableFCharges.add(rdoIsApplicableFCharges_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFolioCharges.add(panIsApplicableFCharges, gridBagConstraints);

        lblLastAppliedFCharges.setText("Last Charges Applied On");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panFolioCharges.add(lblLastAppliedFCharges, gridBagConstraints);

        tdtLastAppliedFCharges.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panFolioCharges.add(tdtLastAppliedFCharges, gridBagConstraints);

        lblDueDateFCharges.setText("Next Due Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFolioCharges.add(lblDueDateFCharges, gridBagConstraints);

        tdtDueDateFCharges.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFolioCharges.add(tdtDueDateFCharges, gridBagConstraints);

        lblFolioEntriesFCharges.setText("No of Entries per Folio");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFolioCharges.add(lblFolioEntriesFCharges, gridBagConstraints);

        txtFolioEntriesFCharges.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFolioCharges.add(txtFolioEntriesFCharges, gridBagConstraints);

        txtRateFCharges.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFolioCharges.add(txtRateFCharges, gridBagConstraints);

        lblRateFCharges.setText("Rate per Folio");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFolioCharges.add(lblRateFCharges, gridBagConstraints);

        lblChargeOnTransactionFCharges.setText("To Charge On");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFolioCharges.add(lblChargeOnTransactionFCharges, gridBagConstraints);

        cboChargeOnTransactionFCharges.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFolioCharges.add(cboChargeOnTransactionFCharges, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFolioCharges.add(cboCAFrequencyFCharges, gridBagConstraints);

        lblCAFrequencyFCharges.setText("Charges Applicable Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFolioCharges.add(lblCAFrequencyFCharges, gridBagConstraints);

        lblCollectChargeFCharges.setText("To Collect Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFolioCharges.add(lblCollectChargeFCharges, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFolioCharges.add(cboCollectChargeFCharges, gridBagConstraints);

        cboChargeOnDocFCharges.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFolioCharges.add(cboChargeOnDocFCharges, gridBagConstraints);

        lblChargeOnDocFCharges.setText("To Charge On");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFolioCharges.add(lblChargeOnDocFCharges, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFolioCharges.add(cboIRFrequencyFCharges, gridBagConstraints);

        lblIRFrequencyFCharges.setText("Incomplete Rounding off Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFolioCharges.add(lblIRFrequencyFCharges, gridBagConstraints);

        lblIsApplicableFCharges.setText("Folio Chagres Applicable");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFolioCharges.add(lblIsApplicableFCharges, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChagres.add(panFolioCharges, gridBagConstraints);

        panOtherCharges.setLayout(new java.awt.GridBagLayout());

        panOtherCharges.setBorder(new javax.swing.border.TitledBorder("Other Charges"));
        panOtherCharges.setMinimumSize(new java.awt.Dimension(370, 325));
        panOtherCharges.setPreferredSize(new java.awt.Dimension(370, 340));
        panIsStatementCharges.setLayout(new java.awt.GridBagLayout());

        rdoIsStatementCharges_Yes.setText("Yes");
        rdoIsStatementCharges.add(rdoIsStatementCharges_Yes);
        rdoIsStatementCharges_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoIsStatementCharges_YesActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panIsStatementCharges.add(rdoIsStatementCharges_Yes, gridBagConstraints);

        rdoIsStatementCharges_No.setText("No");
        rdoIsStatementCharges.add(rdoIsStatementCharges_No);
        rdoIsStatementCharges_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoIsStatementCharges_NoActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panIsStatementCharges.add(rdoIsStatementCharges_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOtherCharges.add(panIsStatementCharges, gridBagConstraints);

        lblIsStatementCharges.setText("Statement");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOtherCharges.add(lblIsStatementCharges, gridBagConstraints);

        lblStatementCharges.setText("Value");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOtherCharges.add(lblStatementCharges, gridBagConstraints);

        lblIsProcessingCharges.setText("Processing");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOtherCharges.add(lblIsProcessingCharges, gridBagConstraints);

        lblAccountCLosingCharges.setText("Account Closing");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOtherCharges.add(lblAccountCLosingCharges, gridBagConstraints);

        lblMicsServiceCharges.setText("Misc Service");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOtherCharges.add(lblMicsServiceCharges, gridBagConstraints);

        lblProcessingCharges.setText("Applicable Charges ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOtherCharges.add(lblProcessingCharges, gridBagConstraints);

        panIsProcessingCharges.setLayout(new java.awt.GridBagLayout());

        rdoIsProcessingCharges_Yes.setText("Yes");
        rdoIsProcessingCharges.add(rdoIsProcessingCharges_Yes);
        rdoIsProcessingCharges_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoIsProcessingCharges_YesActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panIsProcessingCharges.add(rdoIsProcessingCharges_Yes, gridBagConstraints);

        rdoIsProcessingCharges_No.setText("No");
        rdoIsProcessingCharges.add(rdoIsProcessingCharges_No);
        rdoIsProcessingCharges_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoIsProcessingCharges_NoActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panIsProcessingCharges.add(rdoIsProcessingCharges_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOtherCharges.add(panIsProcessingCharges, gridBagConstraints);

        panProcessingCharges.setLayout(new java.awt.GridBagLayout());

        lblPSProcessingCharges.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panProcessingCharges.add(lblPSProcessingCharges, gridBagConstraints);

        txtProcessingCharges.setMinimumSize(new java.awt.Dimension(65, 21));
        txtProcessingCharges.setPreferredSize(new java.awt.Dimension(65, 21));
        txtProcessingCharges.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panProcessingCharges.add(txtProcessingCharges, gridBagConstraints);

        cboProcessingCharges.setMinimumSize(new java.awt.Dimension(40, 21));
        cboProcessingCharges.setPreferredSize(new java.awt.Dimension(55, 21));
        cboProcessingCharges.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboProcessingChargesItemStateChanged(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panProcessingCharges.add(cboProcessingCharges, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOtherCharges.add(panProcessingCharges, gridBagConstraints);

        lblIsCommitmentCharges.setText("Commitment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOtherCharges.add(lblIsCommitmentCharges, gridBagConstraints);

        panIsCommitmentCharges.setLayout(new java.awt.GridBagLayout());

        rdoIsCommitmentCharges_Yes.setText("Yes");
        rdoIsCommitmentCharges.add(rdoIsCommitmentCharges_Yes);
        rdoIsCommitmentCharges_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoIsCommitmentCharges_YesActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panIsCommitmentCharges.add(rdoIsCommitmentCharges_Yes, gridBagConstraints);

        rdoIsCommitmentCharges_No.setText("No");
        rdoIsCommitmentCharges.add(rdoIsCommitmentCharges_No);
        rdoIsCommitmentCharges_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoIsCommitmentCharges_NoActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panIsCommitmentCharges.add(rdoIsCommitmentCharges_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOtherCharges.add(panIsCommitmentCharges, gridBagConstraints);

        panCommitmentCharges.setLayout(new java.awt.GridBagLayout());

        lblPSCommitmentCharges.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCommitmentCharges.add(lblPSCommitmentCharges, gridBagConstraints);

        txtCommitmentCharges.setMinimumSize(new java.awt.Dimension(65, 21));
        txtCommitmentCharges.setPreferredSize(new java.awt.Dimension(65, 21));
        txtCommitmentCharges.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panCommitmentCharges.add(txtCommitmentCharges, gridBagConstraints);

        cboCommitmentCharges.setMinimumSize(new java.awt.Dimension(40, 21));
        cboCommitmentCharges.setPreferredSize(new java.awt.Dimension(55, 21));
        cboCommitmentCharges.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboCommitmentChargesItemStateChanged(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panCommitmentCharges.add(cboCommitmentCharges, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOtherCharges.add(panCommitmentCharges, gridBagConstraints);

        txtAccountClosingCharges.setMinimumSize(new java.awt.Dimension(80, 21));
        txtAccountClosingCharges.setPreferredSize(new java.awt.Dimension(80, 21));
        txtAccountClosingCharges.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOtherCharges.add(txtAccountClosingCharges, gridBagConstraints);

        txtStatementCharges.setMinimumSize(new java.awt.Dimension(80, 21));
        txtStatementCharges.setPreferredSize(new java.awt.Dimension(80, 21));
        txtStatementCharges.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOtherCharges.add(txtStatementCharges, gridBagConstraints);

        txtMiscServiceCharges.setMinimumSize(new java.awt.Dimension(80, 21));
        txtMiscServiceCharges.setPreferredSize(new java.awt.Dimension(80, 21));
        txtMiscServiceCharges.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOtherCharges.add(txtMiscServiceCharges, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panChagres.add(panOtherCharges, gridBagConstraints);

        panCRChargesInward.setLayout(new java.awt.GridBagLayout());

        panCRChargesInward.setBorder(new javax.swing.border.TitledBorder("Cheque Return Inward Charges"));
        cboAmountCRIC.setMinimumSize(new java.awt.Dimension(100, 21));
        cboAmountCRIC.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboAmountCRICItemStateChanged(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCRChargesInward.add(cboAmountCRIC, gridBagConstraints);

        txtRateCRIC.setMinimumSize(new java.awt.Dimension(100, 21));
        txtRateCRIC.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCRChargesInward.add(txtRateCRIC, gridBagConstraints);

        panButtonsCRIC.setLayout(new java.awt.GridBagLayout());

        btnNewCRIC.setText("New");
        btnNewCRIC.setMinimumSize(new java.awt.Dimension(85, 26));
        btnNewCRIC.setPreferredSize(new java.awt.Dimension(85, 26));
        btnNewCRIC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewCRICActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panButtonsCRIC.add(btnNewCRIC, gridBagConstraints);

        btnSaveCRIC.setText("Save");
        btnSaveCRIC.setMinimumSize(new java.awt.Dimension(85, 26));
        btnSaveCRIC.setPreferredSize(new java.awt.Dimension(85, 26));
        btnSaveCRIC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveCRICActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panButtonsCRIC.add(btnSaveCRIC, gridBagConstraints);

        btnDeleteCRIC.setText("Delete");
        btnDeleteCRIC.setMinimumSize(new java.awt.Dimension(85, 26));
        btnDeleteCRIC.setPreferredSize(new java.awt.Dimension(85, 26));
        btnDeleteCRIC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteCRICActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panButtonsCRIC.add(btnDeleteCRIC, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCRChargesInward.add(panButtonsCRIC, gridBagConstraints);

        lblAmountCRIC.setText("Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCRChargesInward.add(lblAmountCRIC, gridBagConstraints);

        lblRateCRIC.setText("Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCRChargesInward.add(lblRateCRIC, gridBagConstraints);

        tblCRChargesInward.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Amount", "Rate of Amount"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblCRChargesInward.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblCRChargesInwardMousePressed(evt);
            }
        });

        srpCRChargesInward.setViewportView(tblCRChargesInward);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.6;
        panCRChargesInward.add(srpCRChargesInward, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panChagres.add(panCRChargesInward, gridBagConstraints);

        panCRChargesOutward.setLayout(new java.awt.GridBagLayout());

        panCRChargesOutward.setBorder(new javax.swing.border.TitledBorder("Cheque Return Outward Charges"));
        cboAmountCROC.setMinimumSize(new java.awt.Dimension(100, 21));
        cboAmountCROC.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboAmountCROCItemStateChanged(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCRChargesOutward.add(cboAmountCROC, gridBagConstraints);

        txtRateCROC.setMinimumSize(new java.awt.Dimension(100, 21));
        txtRateCROC.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCRChargesOutward.add(txtRateCROC, gridBagConstraints);

        panButtonsCROC.setLayout(new java.awt.GridBagLayout());

        btnNewCROC.setText("New");
        btnNewCROC.setMinimumSize(new java.awt.Dimension(85, 26));
        btnNewCROC.setPreferredSize(new java.awt.Dimension(85, 26));
        btnNewCROC.setRolloverIcon(new javax.swing.ImageIcon(",21"));
        btnNewCROC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewCROCActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panButtonsCROC.add(btnNewCROC, gridBagConstraints);

        btnSaveCROC.setText("Save");
        btnSaveCROC.setMinimumSize(new java.awt.Dimension(85, 26));
        btnSaveCROC.setPreferredSize(new java.awt.Dimension(85, 26));
        btnSaveCROC.setRolloverIcon(new javax.swing.ImageIcon(",21"));
        btnSaveCROC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveCROCActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panButtonsCROC.add(btnSaveCROC, gridBagConstraints);

        btnDeleteCROC.setText("Delete");
        btnDeleteCROC.setMinimumSize(new java.awt.Dimension(85, 26));
        btnDeleteCROC.setPreferredSize(new java.awt.Dimension(85, 26));
        btnDeleteCROC.setRolloverIcon(new javax.swing.ImageIcon(",21"));
        btnDeleteCROC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteCROCActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panButtonsCROC.add(btnDeleteCROC, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCRChargesOutward.add(panButtonsCROC, gridBagConstraints);

        lblAmountCROC.setText("Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCRChargesOutward.add(lblAmountCROC, gridBagConstraints);

        lblRateCROC.setText("Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCRChargesOutward.add(lblRateCROC, gridBagConstraints);

        tblCRChargesOutward.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Amount", "Rate of Amount"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblCRChargesOutward.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblCRChargesOutwardMousePressed(evt);
            }
        });

        srpCRChargesOutward.setViewportView(tblCRChargesOutward);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.5;
        panCRChargesOutward.add(srpCRChargesOutward, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panChagres.add(panCRChargesOutward, gridBagConstraints);

        tabProduct.addTab("Charges", panChagres);

        panSplItems.setLayout(new java.awt.GridBagLayout());

        panSplItemDetails.setLayout(new java.awt.GridBagLayout());

        panSplItemDetails.setBorder(new javax.swing.border.TitledBorder("Special Items Details"));
        panSplItemDetails.setMinimumSize(new java.awt.Dimension(275, 200));
        panSplItemDetails.setPreferredSize(new java.awt.Dimension(275, 200));
        lblATMCardSI.setText("ATM Card ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSplItemDetails.add(lblATMCardSI, gridBagConstraints);

        lblCreditCardSI.setText("Credit Card ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSplItemDetails.add(lblCreditCardSI, gridBagConstraints);

        lblMobileBankingClientSI.setText("Mobile Banking Client");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSplItemDetails.add(lblMobileBankingClientSI, gridBagConstraints);

        lblBranchBankingSI.setText("Any  Branch Banking ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSplItemDetails.add(lblBranchBankingSI, gridBagConstraints);

        rdoATMCardSI_Yes.setText("Yes");
        rdoATMCardSI.add(rdoATMCardSI_Yes);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSplItemDetails.add(rdoATMCardSI_Yes, gridBagConstraints);

        rdoATMCardSI_No.setText("No");
        rdoATMCardSI.add(rdoATMCardSI_No);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSplItemDetails.add(rdoATMCardSI_No, gridBagConstraints);

        rdoCreditCardSI_Yes.setText("Yes");
        rdoCreditCardSI.add(rdoCreditCardSI_Yes);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSplItemDetails.add(rdoCreditCardSI_Yes, gridBagConstraints);

        rdoCreditCardSI_No.setText("No");
        rdoCreditCardSI.add(rdoCreditCardSI_No);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSplItemDetails.add(rdoCreditCardSI_No, gridBagConstraints);

        rdoMobileBankingClientSI_Yes.setText("Yes");
        rdoMobileBankingClientSI.add(rdoMobileBankingClientSI_Yes);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSplItemDetails.add(rdoMobileBankingClientSI_Yes, gridBagConstraints);

        rdoMobileBankingClientSI_No.setText("No");
        rdoMobileBankingClientSI.add(rdoMobileBankingClientSI_No);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSplItemDetails.add(rdoMobileBankingClientSI_No, gridBagConstraints);

        rdoBranchBankingSI_Yes.setText("Yes");
        rdoBranchBankingSI.add(rdoBranchBankingSI_Yes);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSplItemDetails.add(rdoBranchBankingSI_Yes, gridBagConstraints);

        rdoBranchBankingSI_No.setText("No");
        rdoBranchBankingSI.add(rdoBranchBankingSI_No);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSplItemDetails.add(rdoBranchBankingSI_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panSplItems.add(panSplItemDetails, gridBagConstraints);

        panMiscItemSI.setLayout(new java.awt.GridBagLayout());

        panMiscItemSI.setBorder(new javax.swing.border.TitledBorder("Miscellanous Item Details"));
        srpMiscItemSI.setBorder(null);
        tblMiscItemSI.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Assets Caterory", "Rate %"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblMiscItemSI.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblMiscItemSIMousePressed(evt);
            }
        });

        srpMiscItemSI.setViewportView(tblMiscItemSI);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panMiscItemSI.add(srpMiscItemSI, gridBagConstraints);

        txtRateSI.setMinimumSize(new java.awt.Dimension(100, 21));
        txtRateSI.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMiscItemSI.add(txtRateSI, gridBagConstraints);

        cboAssetsSI.setMinimumSize(new java.awt.Dimension(100, 21));
        cboAssetsSI.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboAssetsSIItemStateChanged(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMiscItemSI.add(cboAssetsSI, gridBagConstraints);

        lblAssetsSI.setText("Assets Category");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMiscItemSI.add(lblAssetsSI, gridBagConstraints);

        panButtonSI.setLayout(new java.awt.GridBagLayout());

        btnNewSI.setText("New");
        btnNewSI.setMinimumSize(new java.awt.Dimension(85, 26));
        btnNewSI.setPreferredSize(new java.awt.Dimension(85, 26));
        btnNewSI.setPressedIcon(new javax.swing.ImageIcon("75,21"));
        btnNewSI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewSIActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panButtonSI.add(btnNewSI, gridBagConstraints);

        btnSaveSI.setText("Save");
        btnSaveSI.setMinimumSize(new java.awt.Dimension(85, 26));
        btnSaveSI.setPreferredSize(new java.awt.Dimension(85, 26));
        btnSaveSI.setPressedIcon(new javax.swing.ImageIcon("75,21"));
        btnSaveSI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveSIActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panButtonSI.add(btnSaveSI, gridBagConstraints);

        btnDeleteSI.setText("Delete");
        btnDeleteSI.setMinimumSize(new java.awt.Dimension(85, 26));
        btnDeleteSI.setPreferredSize(new java.awt.Dimension(85, 26));
        btnDeleteSI.setPressedIcon(new javax.swing.ImageIcon("75,21"));
        btnDeleteSI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteSIActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panButtonSI.add(btnDeleteSI, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMiscItemSI.add(panButtonSI, gridBagConstraints);

        lblRateSI.setText("Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMiscItemSI.add(lblRateSI, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panSplItems.add(panMiscItemSI, gridBagConstraints);

        tabProduct.addTab("Special Items", panSplItems);

        panAccountHead.setLayout(new java.awt.GridBagLayout());

        panAccountHead.setPreferredSize(new java.awt.Dimension(21, 21));
        lblACCAH.setText("Account Closing Charge - P & L Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblACCAH, gridBagConstraints);

        lblMSCAH.setText("Misc Service Charges - P & L Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblMSCAH, gridBagConstraints);

        lblSCAH.setText("Statement Charges - P & L Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblSCAH, gridBagConstraints);

        lblDIAH.setText("Account For Debit Interest  - P & L Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblDIAH, gridBagConstraints);

        lblPIAH.setText("Penal Interest - P & L Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblPIAH, gridBagConstraints);

        lblCIAH.setText("Account for Credit Interest - P & L Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblCIAH, gridBagConstraints);

        lblAgCIAH.setText("Against Clearing Interest Account Head ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblAgCIAH, gridBagConstraints);

        lblEIAH.setText("Expiry Interest - P & L Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblEIAH, gridBagConstraints);

        lblCRCInwardAH.setText("Cheque Return Charges (Inward) - P & L Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblCRCInwardAH, gridBagConstraints);

        lblCRCOutwardAH.setText("Cheque Return Charges (Outward) - P & L Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblCRCOutwardAH, gridBagConstraints);

        lblFCAH.setText("Folio Charges Account - P & L Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblFCAH, gridBagConstraints);

        txtACCAH.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(txtACCAH, gridBagConstraints);

        txtMSCAH.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(txtMSCAH, gridBagConstraints);

        txtSCAH.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(txtSCAH, gridBagConstraints);

        txtDIAH.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(txtDIAH, gridBagConstraints);

        txtPIAH.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(txtPIAH, gridBagConstraints);

        txtCIAH.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(txtCIAH, gridBagConstraints);

        txtAgCIAH.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(txtAgCIAH, gridBagConstraints);

        txtEIAH.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(txtEIAH, gridBagConstraints);

        txtCRCInwardAH.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(txtCRCInwardAH, gridBagConstraints);

        txtCRCoutwardAH.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(txtCRCoutwardAH, gridBagConstraints);

        txtFCAH.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(txtFCAH, gridBagConstraints);

        btnACCAHHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnACCAHHelp.setPreferredSize(new java.awt.Dimension(21, 21));
        btnACCAHHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnACCAHHelpActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        panAccountHead.add(btnACCAHHelp, gridBagConstraints);

        btnMSCAHHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnMSCAHHelp.setPreferredSize(new java.awt.Dimension(21, 21));
        btnMSCAHHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMSCAHHelpActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        panAccountHead.add(btnMSCAHHelp, gridBagConstraints);

        btnSCAHHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnSCAHHelp.setPreferredSize(new java.awt.Dimension(21, 21));
        btnSCAHHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSCAHHelpActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        panAccountHead.add(btnSCAHHelp, gridBagConstraints);

        btnDIAHHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnDIAHHelp.setPreferredSize(new java.awt.Dimension(21, 21));
        btnDIAHHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDIAHHelpActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        panAccountHead.add(btnDIAHHelp, gridBagConstraints);

        btnPIAHHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnPIAHHelp.setPreferredSize(new java.awt.Dimension(21, 21));
        btnPIAHHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPIAHHelpActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        panAccountHead.add(btnPIAHHelp, gridBagConstraints);

        btnCIAHHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnCIAHHelp.setPreferredSize(new java.awt.Dimension(21, 21));
        btnCIAHHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCIAHHelpActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        panAccountHead.add(btnCIAHHelp, gridBagConstraints);

        btnAgCIAHHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnAgCIAHHelp.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAgCIAHHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgCIAHHelpActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        panAccountHead.add(btnAgCIAHHelp, gridBagConstraints);

        btnEIAHHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnEIAHHelp.setPreferredSize(new java.awt.Dimension(21, 21));
        btnEIAHHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEIAHHelpActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        panAccountHead.add(btnEIAHHelp, gridBagConstraints);

        btnCRCInwardAHHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnCRCInwardAHHelp.setPreferredSize(new java.awt.Dimension(21, 21));
        btnCRCInwardAHHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCRCInwardAHHelpActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 11;
        panAccountHead.add(btnCRCInwardAHHelp, gridBagConstraints);

        btnCRCoutwardAHHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnCRCoutwardAHHelp.setPreferredSize(new java.awt.Dimension(21, 21));
        btnCRCoutwardAHHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCRCoutwardAHHelpActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 12;
        panAccountHead.add(btnCRCoutwardAHHelp, gridBagConstraints);

        btnFCAHHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnFCAHHelp.setPreferredSize(new java.awt.Dimension(21, 21));
        btnFCAHHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFCAHHelpActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 13;
        panAccountHead.add(btnFCAHHelp, gridBagConstraints);

        tabProduct.addTab("Account Head", panAccountHead);

        panMiscdata.setLayout(new java.awt.GridBagLayout());

        panCharges.setLayout(new java.awt.GridBagLayout());

        panCharges.setBorder(new javax.swing.border.TitledBorder("Charges"));
        panIsChequebookCharges.setLayout(new java.awt.GridBagLayout());

        rdoIsChequebookCharges_Yes.setText("Yes");
        rdoIsChequebookCharges.add(rdoIsChequebookCharges_Yes);
        rdoIsChequebookCharges_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoIsChequebookCharges_YesActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panIsChequebookCharges.add(rdoIsChequebookCharges_Yes, gridBagConstraints);

        rdoIsChequebookCharges_No.setText("No");
        rdoIsChequebookCharges.add(rdoIsChequebookCharges_No);
        rdoIsChequebookCharges_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoIsChequebookCharges_NoActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panIsChequebookCharges.add(rdoIsChequebookCharges_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(panIsChequebookCharges, gridBagConstraints);

        lblIsChequebookCharges.setText("Chequebook");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(lblIsChequebookCharges, gridBagConstraints);

        txtChequebookCharges.setMinimumSize(new java.awt.Dimension(80, 21));
        txtChequebookCharges.setPreferredSize(new java.awt.Dimension(80, 21));
        txtChequebookCharges.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(txtChequebookCharges, gridBagConstraints);

        txtStopPaymentCharges.setMinimumSize(new java.awt.Dimension(80, 21));
        txtStopPaymentCharges.setPreferredSize(new java.awt.Dimension(80, 21));
        txtStopPaymentCharges.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(txtStopPaymentCharges, gridBagConstraints);

        lblIsStopPaymentCharges.setText("Stop Payment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(lblIsStopPaymentCharges, gridBagConstraints);

        panIsStopPaymentCharges.setLayout(new java.awt.GridBagLayout());

        rdoIsStopPaymentCharges_Yes.setText("Yes");
        rdoIsStopPaymentCharges.add(rdoIsStopPaymentCharges_Yes);
        rdoIsStopPaymentCharges_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoIsStopPaymentCharges_YesActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panIsStopPaymentCharges.add(rdoIsStopPaymentCharges_Yes, gridBagConstraints);

        rdoIsStopPaymentCharges_No.setText("No");
        rdoIsStopPaymentCharges.add(rdoIsStopPaymentCharges_No);
        rdoIsStopPaymentCharges_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoIsStopPaymentCharges_NoActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panIsStopPaymentCharges.add(rdoIsStopPaymentCharges_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(panIsStopPaymentCharges, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMiscdata.add(panCharges, gridBagConstraints);

        panSpecialItems.setLayout(new java.awt.GridBagLayout());

        panSpecialItems.setBorder(new javax.swing.border.TitledBorder("Special Items"));
        lblDebitCardSI.setText("Debit Card ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSpecialItems.add(lblDebitCardSI, gridBagConstraints);

        rdoDebitCardSI_Yes.setText("Yes");
        rdoDebitCardSI.add(rdoDebitCardSI_Yes);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSpecialItems.add(rdoDebitCardSI_Yes, gridBagConstraints);

        rdoDebitCardSI_No.setText("No");
        rdoDebitCardSI.add(rdoDebitCardSI_No);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSpecialItems.add(rdoDebitCardSI_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMiscdata.add(panSpecialItems, gridBagConstraints);

        panActHead.setLayout(new java.awt.GridBagLayout());

        panActHead.setBorder(new javax.swing.border.TitledBorder("Account Head"));
        lblExOLHAH.setText("Excess Over Limit Head - P & L Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panActHead.add(lblExOLHAH, gridBagConstraints);

        txtExOLHAH.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panActHead.add(txtExOLHAH, gridBagConstraints);

        btnExOLHAHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnExOLHAHelp.setPreferredSize(new java.awt.Dimension(21, 21));
        btnExOLHAHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExOLHAHelpActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        panActHead.add(btnExOLHAHelp, gridBagConstraints);

        lblCICAH.setText("Chequebook Issue Charges - Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panActHead.add(lblCICAH, gridBagConstraints);

        txtCICAH.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panActHead.add(txtCICAH, gridBagConstraints);

        btnCICAHHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnCICAHHelp.setPreferredSize(new java.awt.Dimension(21, 21));
        btnCICAHHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCICAHHelpActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        panActHead.add(btnCICAHHelp, gridBagConstraints);

        lblSPCAH.setText("Stop Payment Charges - P & L Head ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panActHead.add(lblSPCAH, gridBagConstraints);

        txtSPCAH.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panActHead.add(txtSPCAH, gridBagConstraints);

        btnSPCAHHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnSPCAHHelp.setPreferredSize(new java.awt.Dimension(21, 21));
        btnSPCAHHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSPCAHHelpActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        panActHead.add(btnSPCAHHelp, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMiscdata.add(panActHead, gridBagConstraints);

        tabProduct.addTab("Miscellaneous", panMiscdata);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(tabProduct, gridBagConstraints);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif")));
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });

        tbrAdvances.add(btnNew);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif")));
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        tbrAdvances.add(btnEdit);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif")));
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

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif")));
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        tbrAdvances.add(btnSave);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif")));
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        tbrAdvances.add(btnCancel);

        lblSpace3.setText("     ");
        tbrAdvances.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif")));
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });

        tbrAdvances.add(btnAuthorize);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif")));
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });

        tbrAdvances.add(btnReject);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif")));
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });

        tbrAdvances.add(btnException);

        lblSpace5.setText("     ");
        tbrAdvances.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif")));
        btnPrint.setToolTipText("Print");
        tbrAdvances.add(btnPrint);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif")));
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        tbrAdvances.add(btnClose);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(tbrAdvances, gridBagConstraints);

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace1.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace1, gridBagConstraints);

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
    
    private void rdoIsStopPaymentCharges_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoIsStopPaymentCharges_YesActionPerformed
        disabledTextBox(txtStopPaymentCharges,panOtherCharges,false);
        // Add your handling code here:
    }//GEN-LAST:event_rdoIsStopPaymentCharges_YesActionPerformed
    
    private void rdoIsStopPaymentCharges_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoIsStopPaymentCharges_NoActionPerformed
        txtStopPaymentCharges.setText("");
        disabledTextBox(txtStopPaymentCharges,panOtherCharges,true);
        //txtStopPaymentCharges.setEnabled(false);
        // Add your handling code here:
    }//GEN-LAST:event_rdoIsStopPaymentCharges_NoActionPerformed
    
    private void btnProdIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProdIDActionPerformed
        // TODO add your handling code here:
        callView(PRODID);
    }//GEN-LAST:event_btnProdIDActionPerformed
    
    private void txtProductIdAccountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtProductIdAccountFocusLost
        // TODO add your handling code here:
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
            boolean verify = observable.verifyProdId(txtProductIdAccount.getText());
            if(verify){
                displayAlert(resourceBundle.getString("prodIdWarning"));
            }else{ // Added by nithya on 19-05-2016
                HashMap whereMap = new HashMap();
                whereMap.put("PROD_ID", txtProductIdAccount.getText());
                List lst = ClientUtil.executeQuery("getAllProductIds", whereMap);
                System.out.println("getSBODBorrowerEligAmt : " + lst);
                if (lst != null && lst.size() > 0) {
                    HashMap existingProdIdMap = (HashMap) lst.get(0);
                    if (existingProdIdMap.containsKey("PROD_ID")) {
                        ClientUtil.showMessageWindow("Id is already exists for Product " + existingProdIdMap.get("PRODUCT") + "\n Please change");
                        txtProductIdAccount.setText("");
                    }
                }
            }
        }
    }//GEN-LAST:event_txtProductIdAccountFocusLost
    boolean  maxAmtRule = false;
    boolean  maxInteRule = false;
    private void txtMaxDIAmtIRFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMaxDIAmtIRFocusLost
        // TODO add your handling code here:
        if(!maxAmtRule){
            String message = maxAmtRule();
            if(message.length() > 0){
                displayAlert(message);
            }
        }
        maxAmtRule = false;
    }//GEN-LAST:event_txtMaxDIAmtIRFocusLost
    
    private void txtMinDIAmtIRFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMinDIAmtIRFocusLost
        // TODO add your handling code here:
        if(!maxAmtRule){
            String message = maxAmtRule();
            if(message.length() > 0){
                displayAlert(message);
            }
        }
        maxAmtRule = false;
    }//GEN-LAST:event_txtMinDIAmtIRFocusLost
    private String maxAmtRule(){
        maxAmtRule = true;
        String message = "";
        
        if(!(txtMaxDIAmtIR.getText().equalsIgnoreCase("")
        ||  txtMinDIAmtIR.getText().equalsIgnoreCase(""))){
            if(Double.parseDouble( txtMinDIAmtIR.getText()) >  Double.parseDouble(txtMaxDIAmtIR.getText()) ){
                message = resourceBundle.getString("MaxAmtRuleWarning");
            }
        }
        return message;
    }
    private void txtMaxDIRateIRFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMaxDIRateIRFocusLost
        // TODO add your handling code here:
        if(!maxInteRule){
            String message = maxInteRule();
            if(message.length() > 0){
                displayAlert(message);
            }
        }
        maxInteRule = false;
    }//GEN-LAST:event_txtMaxDIRateIRFocusLost
    
    private void txtMinDIRateIRFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMinDIRateIRFocusLost
        // TODO add your handling code here:
        if(!maxInteRule){
            String message = maxInteRule();
            if(message.length() > 0){
                displayAlert(message);
            }
        }
        maxInteRule = false;
    }//GEN-LAST:event_txtMinDIRateIRFocusLost
    private String maxInteRule(){
        maxInteRule = true;
        String message = "";
        
        if(!(txtMaxDIRateIR.getText().equalsIgnoreCase("")
        ||  txtMinDIRateIR.getText().equalsIgnoreCase(""))){
            if(Double.parseDouble( txtMinDIRateIR.getText()) >  Double.parseDouble(txtMaxDIRateIR.getText()) ){
                message = resourceBundle.getString("MaxInteRuleWarning");
            }
        }
        return message;
    }
    private void rdoWSAccount_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoWSAccount_NoActionPerformed
        // TODO add your handling code here:
        if(rdoWSAccount_No.isSelected()){
            txtMaxAmountOnWS.setText("");
            txtMaxAmountOnWS.setEditable(false);
            txtMaxAmountOnWS.setEnabled(false);
        }
    }//GEN-LAST:event_rdoWSAccount_NoActionPerformed
    
    private void rdoWSAccount_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoWSAccount_YesActionPerformed
        // TODO add your handling code here:
        txtMaxAmountOnWS.setEditable(true);
        txtMaxAmountOnWS.setEnabled(true);
    }//GEN-LAST:event_rdoWSAccount_YesActionPerformed
                                private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
                                    // Add your handling code here:
                                    observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
                                    authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed
                                                                                                                                                private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
// Add your handling code here:
observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
authorizeStatus(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnRejectActionPerformed
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
// Add your handling code here:
observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    public void authorizeStatus(String authorizeStatus) {
    if (viewType == AUTHORIZE){
    String strWarnMsg = tabProduct.isAllTabsVisited();
        if (strWarnMsg.length() > 0){
            displayAlert(strWarnMsg);
            return;
        }
        strWarnMsg = null;
        tabProduct.resetVisits();
        HashMap singleAuthorizeMap = new HashMap();
        singleAuthorizeMap.put("STATUS", authorizeStatus);
        singleAuthorizeMap.put("USER_ID", TrueTransactMain.USER_ID);
        singleAuthorizeMap.put("PROD_ID", txtProductIdAccount.getText());
        System.out.println("singleAuthorizeMap: " + singleAuthorizeMap);

        //                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        ClientUtil.execute("authorizeAdvancesProduct", singleAuthorizeMap);
        viewType = -1;
        btnSave.setEnabled(true);
        lblStatus.setText(authorizeStatus);
         btnCancelActionPerformed(null);
        } else {
        viewType = AUTHORIZE;
        //__ To Save the data in the Internal Frame...
        setModified(true);
        HashMap mapParam = new HashMap();
        mapParam.put(CommonConstants.MAP_NAME, "getAdvancesProductAuthorizeList");
        mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeAdvancesProduct");

        System.out.println("authorizeStatus Called...");
        AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
        authorizeUI.show();
        btnSave.setEnabled(false);
        observable.getLblStatus();
        lblStatus.setText(observable.getLblStatus());
        }
}
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        cifClosingAlert();
        //        this.dispose();
        // Add your handling code here:
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        btnCancelActionPerformed(evt);
        // Add your handling code here:
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        btnSaveActionPerformed(evt);
        // Add your handling code here:
    }//GEN-LAST:event_mitSaveActionPerformed
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        btnDeleteActionPerformed(evt);
        // Add your handling code here:
    }//GEN-LAST:event_mitDeleteActionPerformed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        btnEditActionPerformed(evt);
        // Add your handling code here:
    }//GEN-LAST:event_mitEditActionPerformed
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        btnNewActionPerformed(evt);
        // Add your handling code here:
    }//GEN-LAST:event_mitNewActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        cifClosingAlert();
        //        this.dispose();
        // Add your handling code here:
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void rdoPenalOthersIR_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoPenalOthersIR_YesActionPerformed
        disabledTextBox(txtPenalIROthersIR,panOthersIR,false);
        // Add your handling code here:
    }//GEN-LAST:event_rdoPenalOthersIR_YesActionPerformed
    
    private void rdoPenalOthersIR_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoPenalOthersIR_NoActionPerformed
        txtPenalIROthersIR.setText("");
        disabledTextBox(txtPenalIROthersIR,panOthersIR,true);
        // Add your handling code here:
    }//GEN-LAST:event_rdoPenalOthersIR_NoActionPerformed
    
    private void btnFCAHHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFCAHHelpActionPerformed
        callView(FolioChrgHead);
        // Add your handling code here:
    }//GEN-LAST:event_btnFCAHHelpActionPerformed
    
    private void btnCRCoutwardAHHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCRCoutwardAHHelpActionPerformed
        callView(ChqRetOutwardChrgHead);
        // Add your handling code here:
    }//GEN-LAST:event_btnCRCoutwardAHHelpActionPerformed
    
    private void btnCRCInwardAHHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCRCInwardAHHelpActionPerformed
        callView(ChqRetInwardChrgHead);
        // Add your handling code here:
    }//GEN-LAST:event_btnCRCInwardAHHelpActionPerformed
    
    private void btnSPCAHHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSPCAHHelpActionPerformed
        callView(StopPaymentChrgHead);
        // Add your handling code here:
    }//GEN-LAST:event_btnSPCAHHelpActionPerformed
    
    private void btnCICAHHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCICAHHelpActionPerformed
        callView(ChqBookIssChrgHead);
        // Add your handling code here:
    }//GEN-LAST:event_btnCICAHHelpActionPerformed
    
    private void btnExOLHAHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExOLHAHelpActionPerformed
        callView(ExcessOLHead);
        // Add your handling code here:
    }//GEN-LAST:event_btnExOLHAHelpActionPerformed
    
    private void btnEIAHHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEIAHHelpActionPerformed
        callView(ExpIntHead);
        // Add your handling code here:
    }//GEN-LAST:event_btnEIAHHelpActionPerformed
    
    private void btnAgCIAHHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgCIAHHelpActionPerformed
        callView(ClearingIntAcctHead);
        // Add your handling code here:
    }//GEN-LAST:event_btnAgCIAHHelpActionPerformed
    
    private void btnCIAHHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCIAHHelpActionPerformed
        callView(AcctCIHead);
        // Add your handling code here:
    }//GEN-LAST:event_btnCIAHHelpActionPerformed
    
    private void btnPIAHHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPIAHHelpActionPerformed
        callView(PenalIntHead);
        // Add your handling code here:
    }//GEN-LAST:event_btnPIAHHelpActionPerformed
    
    private void btnDIAHHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDIAHHelpActionPerformed
        callView(AcctDIHead);
        // Add your handling code here:
    }//GEN-LAST:event_btnDIAHHelpActionPerformed
    
    private void btnSCAHHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSCAHHelpActionPerformed
        callView(StatChrg);
        // Add your handling code here:
    }//GEN-LAST:event_btnSCAHHelpActionPerformed
    
    private void btnMSCAHHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMSCAHHelpActionPerformed
        callView(MiscServChrg);
        // Add your handling code here:
    }//GEN-LAST:event_btnMSCAHHelpActionPerformed
    
    private void btnACCAHHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnACCAHHelpActionPerformed
        callView(AcctClosingChrg);
        // Add your handling code here:
    }//GEN-LAST:event_btnACCAHHelpActionPerformed
    
    private void btnAccountHeadAccountHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccountHeadAccountHelpActionPerformed
        callView(AccountHead);
        // Add your handling code here:
    }//GEN-LAST:event_btnAccountHeadAccountHelpActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        observable.resetOBFields();
        ClientUtil.enableDisable(this, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);//Sets the Action Type to be performed...
        observable.setStatus();
        setButtonsEnable(false);
        setProdEnable();
        //        setButtonEnableDisable();
        btnProdID.setEnabled(false);
        
        btnSave.setEnabled(false);
        viewType = -1;
        //__ Make the Screen Closable..
        setModified(false);
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE); //__ Sets the Action Type to be performed...
        callView(DELETE);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        //        txtProductIdAccount.setEditable(false);
        //        lblStatus.setText("Edit");
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT); //__ Sets the Action Type to be performed...
        callView(EDIT);
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        //checkMandatory();
        updateOBFields();
        StringBuffer strBAlert = new StringBuffer();
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), tabProduct);
        
        //        if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0){
        //            strBAlert.append(mandatoryMessage+"\n");
        
        if(mandatoryMessage.length() > 0){
            strBAlert.append(mandatoryMessage+"\n");
        }
        if(maxInteRule().length() > 0){
            strBAlert.append(maxInteRule()+"\n");
        }
        if(maxAmtRule().length() > 0){
            strBAlert.append(maxAmtRule()+"\n");
        }
        //        }
        
        String str = "";
        str = periodLengthValidation(txtCLPeriodAccount, cboCLPeriod);
        if(str.length() > 0){
            strBAlert.append(str+"\n");
            str = "";
        }
        
        if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && strBAlert.toString().length() > 0 ){
            System.out.println("strBAlert.toString().length(): " + strBAlert.toString().length());
            displayAlert(strBAlert.toString());
            
        }else{
            observable.doAction();       //__ To perform the necessary operation depending on the Action type...
            
            //__ if the Action is not Falied, Reset the fields...
            if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED){
                observable.resetOBFields();
                ClientUtil.enableDisable(this, false);
                observable.setResultStatus();              //__ To Reset the Value of lblStatus...
                //                setButtonEnableDisable();
                setProdEnable();
                setButtonsEnable(false);
                btnProdID.setEnabled(false);
                
                //__ Make the Screen Closable..
                setModified(false);
                
                btnSave.setEnabled(false);
            }
        }
    }//GEN-LAST:event_btnSaveActionPerformed
    private String periodLengthValidation(CTextField txtField, CComboBox comboField){
        String message = "";
        String key = CommonUtil.convertObjToStr(((ComboBoxModel) comboField.getModel()).getKeyForSelected());
        if (!ClientUtil.validPeriodMaxLength(txtField, key)){
            message = objMandatoryRB.getString(txtField.getName());
        }
        return message;
    }
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        //        txtProductIdAccount.setEditable(true);
        ClientUtil.enableDisable(this, true);
        //__ ToSet the Default Values of the Radio Buttons...
        //        rdosDefaultValues();
        //        lblStatus.setText("New");
        //        setButtonEnableDisable();
        setButtonsEnable(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);   //Sets the Action Type to be performed...
        //        observable.setStatus();
        
        setProdEnable();
        //        btnProdID.setEnabled(true);
        
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }//GEN-LAST:event_btnNewActionPerformed
    
    private void btnDeleteCROCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteCROCActionPerformed
        observable.btnDeleteCROCActionEvent((String)cboAmountCROC.getSelectedItem());
        resetOutwardTablePanelComp();
        return;
        // Add your handling code here:
    }//GEN-LAST:event_btnDeleteCROCActionPerformed
    
    private void btnDeleteCRICActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteCRICActionPerformed
        int selectedRow = tblCRChargesInward.getSelectedRow();
        observable.btnDeleteCRICActionEvent((String)cboAmountCRIC.getSelectedItem());
        resetInwardTablePanelComp();
        return;
        // Add your handling code here:
    }//GEN-LAST:event_btnDeleteCRICActionPerformed
    
    private void resetSITablePanelComp(){
        txtRateSI.setText("");
        cboAssetsSI.setSelectedIndex(0);
        
        btnDeleteSI.setEnabled(false);
        btnSaveSI.setEnabled(false);
    }
    
    private void resetInwardTablePanelComp(){
        txtRateCRIC.setText("");
        cboAmountCRIC.setSelectedIndex(0);
        
        btnDeleteCRIC.setEnabled(false);
        btnSaveCRIC.setEnabled(false);
    }
    
    private void resetOutwardTablePanelComp(){
        txtRateCROC.setText("");
        cboAmountCROC.setSelectedIndex(0);
        
        btnDeleteCROC.setEnabled(false);
        btnSaveCROC.setEnabled(false);
    }
    private void btnSaveCROCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveCROCActionPerformed
        String selectedValue = (String)cboAmountCROC.getSelectedItem();
        String enteredValue = txtRateCROC.getText();
        if(enteredValue!=null && !enteredValue.equals("0") && !enteredValue.equals("0.0") && !enteredValue.equals("")){
            observable.btnSaveCROCActionEvent(selectedValue,enteredValue);
            resetOutwardTablePanelComp();
            return;
        }
        txtRateCROC.requestFocus();
        // Add your handling code here:
    }//GEN-LAST:event_btnSaveCROCActionPerformed
    
    private void btnSaveCRICActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveCRICActionPerformed
        String selectedValue = (String)cboAmountCRIC.getSelectedItem();
        String enteredValue = txtRateCRIC.getText();
        if(enteredValue!=null && !enteredValue.equals("0") && !enteredValue.equals("0.0") && !enteredValue.equals("")){
            observable.btnSaveCRICActionEvent(selectedValue,enteredValue);
            resetInwardTablePanelComp();
            return;
        }
        txtRateCRIC.requestFocus();
        // Add your handling code here:
    }//GEN-LAST:event_btnSaveCRICActionPerformed
    
    private void btnNewCROCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewCROCActionPerformed
        resetOutwardTablePanelComp();
        // Add your handling code here:
    }//GEN-LAST:event_btnNewCROCActionPerformed
    
    private void btnNewCRICActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewCRICActionPerformed
        resetInwardTablePanelComp();
        // Add your handling code here:
    }//GEN-LAST:event_btnNewCRICActionPerformed
    
    private void tblCRChargesOutwardMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCRChargesOutwardMousePressed
        int selectedRow = tblCRChargesOutward.getSelectedRow();
        cboAmountCROC.setSelectedItem((String)tbmCRChargesOutward.getValueAt(selectedRow,0));
        txtRateCROC.setText((String)tbmCRChargesOutward.getValueAt(selectedRow,1));
        
        btnSaveCROC.setEnabled(true);
        btnDeleteCROC.setEnabled(true);
        // Add your handling code here:
    }//GEN-LAST:event_tblCRChargesOutwardMousePressed
    
    private void tblCRChargesInwardMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCRChargesInwardMousePressed
        
        int selectedRow = tblCRChargesInward.getSelectedRow();
        cboAmountCRIC.setSelectedItem((String)tbmCRChargesInward.getValueAt(selectedRow,0));
        txtRateCRIC.setText((String)tbmCRChargesInward.getValueAt(selectedRow,1));
        
        btnSaveCRIC.setEnabled(true);
        btnDeleteCRIC.setEnabled(true);// Add your handling code here:
    }//GEN-LAST:event_tblCRChargesInwardMousePressed
    
    private void cboAmountCROCItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboAmountCROCItemStateChanged
        int rows = tbmCRChargesOutward.getRowCount();
        txtRateCROC.setText("0.0");
        String selectedValue = (String)cboAmountCROC.getSelectedItem();
        if(selectedValue !=null && !selectedValue.equals("")){
            String rateCROC = observable.cboCROCSelectionEvent(selectedValue);
            if(rateCROC != null && !rateCROC.equals("")){
                txtRateCROC.setText(rateCROC);
                btnSaveCROC.setEnabled(true);
                btnDeleteCROC.setEnabled(true);
            }else{
                btnSaveCROC.setEnabled(true);
                btnDeleteCROC.setEnabled(false);
            }
        }else{
            btnSaveCROC.setEnabled(false);
            btnDeleteCROC.setEnabled(false);
        }
        
        return;
        // Add your handling code here:
    }//GEN-LAST:event_cboAmountCROCItemStateChanged
    
    private void cboAmountCRICItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboAmountCRICItemStateChanged
        int rows = tbmCRChargesInward.getRowCount();
        txtRateCRIC.setText("");
        String selectedValue = (String)cboAmountCRIC.getSelectedItem();
        if(selectedValue !=null && !selectedValue .equals("")){
            String rateCRIC = observable.cboCRICSelectionEvent(selectedValue);
            if(rateCRIC != null && !rateCRIC.equals("")){
                txtRateCRIC.setText(rateCRIC);
                btnSaveCRIC.setEnabled(true);
                btnDeleteCRIC.setEnabled(true);
            } else{
                btnSaveCRIC.setEnabled(true);
                btnDeleteCRIC.setEnabled(false);
            }
        }else{
            btnSaveCRIC.setEnabled(false);
            btnDeleteCRIC.setEnabled(false);
        }
        return;
        // Add your handling code here:
    }//GEN-LAST:event_cboAmountCRICItemStateChanged
    
    private void tblMiscItemSIMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMiscItemSIMousePressed
        int selectedRow = tblMiscItemSI.getSelectedRow();
        cboAssetsSI.setSelectedItem((String)tbmMiscItemSI.getValueAt(selectedRow,0));
        txtRateSI.setText((String)tbmMiscItemSI.getValueAt(selectedRow,1));
        
        btnSaveSI.setEnabled(true);
        btnDeleteSI.setEnabled(true);
        // Add your handling code here:
    }//GEN-LAST:event_tblMiscItemSIMousePressed
    
    private void cboAssetsSIItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboAssetsSIItemStateChanged
        int rows = tbmMiscItemSI.getRowCount();
        txtRateSI.setText("");
        String selectedValue = (String)cboAssetsSI.getSelectedItem();
        if(selectedValue !=null && !selectedValue.equals("")){
            String rateSI = observable.cboMISISelectionEvent(selectedValue);
            if(rateSI!=null && !rateSI.equals("")){
                txtRateSI.setText(rateSI);
                btnSaveSI.setEnabled(true);
                btnDeleteSI.setEnabled(true);
            }else{
                btnSaveSI.setEnabled(true);
                btnDeleteSI.setEnabled(false);
            }
        }else{
            btnSaveSI.setEnabled(false);
            btnDeleteSI.setEnabled(false);
        }
        return;
        // Add your handling code here:
    }//GEN-LAST:event_cboAssetsSIItemStateChanged
    
    private void btnDeleteSIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteSIActionPerformed
        observable.btnDeleteSIActionEvent((String)cboAssetsSI.getSelectedItem());
        resetSITablePanelComp();
        return;
        
        // Add your handling code here:
    }//GEN-LAST:event_btnDeleteSIActionPerformed
    
    private void btnSaveSIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveSIActionPerformed
        String selectedValue = (String)cboAssetsSI.getSelectedItem();
        String enteredValue = txtRateSI.getText();
        if(enteredValue!=null && !enteredValue.equals("0") && !enteredValue.equals("0.0") && !enteredValue.equals("")){
            observable.btnSaveSIActionEvent(selectedValue,enteredValue);
            resetSITablePanelComp();
            return;
        }
        txtRateSI.requestFocus();
        // Add your handling code here:
    }//GEN-LAST:event_btnSaveSIActionPerformed
    
    private void btnNewSIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewSIActionPerformed
        resetSITablePanelComp();
        // Add your handling code here:
    }//GEN-LAST:event_btnNewSIActionPerformed
    
    private void cboCommitmentChargesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboCommitmentChargesItemStateChanged
        if(((String)cboCommitmentCharges.getSelectedItem()).equals("Absolute")){
            lblPSCommitmentCharges.setText("");
            txtCommitmentCharges.setValidation(new CurrencyValidation(14,2));
        }else if(((String)cboCommitmentCharges.getSelectedItem()).equals("Percent")){
            lblPSCommitmentCharges.setText("%");
            txtCommitmentCharges.setValidation(new PercentageValidation());
        }
        // Add your handling code here:
    }//GEN-LAST:event_cboCommitmentChargesItemStateChanged
    
    private void cboProcessingChargesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboProcessingChargesItemStateChanged
        if(((String)cboProcessingCharges.getSelectedItem()).equals("Absolute")){
            lblPSProcessingCharges.setText("");
            txtProcessingCharges.setValidation(new CurrencyValidation(14,2));
            //lblPSProcessingCharges.setVisible(false);
        }else if(((String)cboProcessingCharges.getSelectedItem()).equals("Percent")){
            lblPSProcessingCharges.setText("%");
            txtProcessingCharges.setValidation(new PercentageValidation());
            //lblPSProcessingCharges.setVisible(true);
        }
        // Add your handling code here:
    }//GEN-LAST:event_cboProcessingChargesItemStateChanged
    
    private void rdoAdditionalIntInterestPayable_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoAdditionalIntInterestPayable_YesActionPerformed
        ClientUtil.enableDisable(panAddInterestRateIP,true);
        // Add your handling code here:
    }//GEN-LAST:event_rdoAdditionalIntInterestPayable_YesActionPerformed
    
    private void rdoCreditCompdInterestPayable_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoCreditCompdInterestPayable_YesActionPerformed
        cboCreditInterestCompdFIP.setEnabled(true);
        // Add your handling code here:
    }//GEN-LAST:event_rdoCreditCompdInterestPayable_YesActionPerformed
    
    private void rdoCreditCompdInterestPayable_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoCreditCompdInterestPayable_NoActionPerformed
        cboCreditInterestCompdFIP.setSelectedItem("");
        cboCreditInterestCompdFIP.setEnabled(false);
        // Add your handling code here:
    }//GEN-LAST:event_rdoCreditCompdInterestPayable_NoActionPerformed
    
    public void disabledTextBox(com.see.truetransact.uicomponent.CTextField cTxt,com.see.truetransact.uicomponent.CPanel container,boolean param){
        com.see.truetransact.uicomponent.CTextField txtDefault = new com.see.truetransact.uicomponent.CTextField();
        
        if(param == true){
            cTxt.setBackground(container.getBackground());
        }else{
            cTxt.setBackground(txtDefault.getBackground());
        }
        cTxt.setEnabled(!param);
    }
    private void rdoDebitCompoundIR_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDebitCompoundIR_YesActionPerformed
        cboDICompoundFIR.setEnabled(true);
        // Add your handling code here:
    }//GEN-LAST:event_rdoDebitCompoundIR_YesActionPerformed
    
    private void rdoDebitCompoundIR_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDebitCompoundIR_NoActionPerformed
        cboDICompoundFIR.setSelectedItem("");
        cboDICompoundFIR.setEnabled(false);
        // Add your handling code here:
    }//GEN-LAST:event_rdoDebitCompoundIR_NoActionPerformed
    
    private void rdoIsCommitmentCharges_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoIsCommitmentCharges_YesActionPerformed
        //        cboCommitmentCharges.setSelectedItem("Percent");
        ClientUtil.enableDisable(panCommitmentCharges, true);
        // Add your handling code here:
    }//GEN-LAST:event_rdoIsCommitmentCharges_YesActionPerformed
    
    private void rdoIsProcessingCharges_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoIsProcessingCharges_YesActionPerformed
        //        cboProcessingCharges.setSelectedItem("Percent");
        ClientUtil.enableDisable(panProcessingCharges,true);
        // Add your handling code here:
    }//GEN-LAST:event_rdoIsProcessingCharges_YesActionPerformed
    
    private void rdoIsProcessingCharges_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoIsProcessingCharges_NoActionPerformed
        cboProcessingCharges.setSelectedItem("");
        txtProcessingCharges.setText("");
        
        ClientUtil.enableDisable(panProcessingCharges,false);        // Add your handling code here:
    }//GEN-LAST:event_rdoIsProcessingCharges_NoActionPerformed
    
    private void rdoIsChequebookCharges_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoIsChequebookCharges_YesActionPerformed
        disabledTextBox(txtChequebookCharges,panOtherCharges,false);
        // Add your handling code here:
    }//GEN-LAST:event_rdoIsChequebookCharges_YesActionPerformed
    
    private void rdoIsChequebookCharges_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoIsChequebookCharges_NoActionPerformed
        txtChequebookCharges.setText("");
        disabledTextBox(txtChequebookCharges,panOtherCharges,true);
        // Add your handling code here:
    }//GEN-LAST:event_rdoIsChequebookCharges_NoActionPerformed
    
    private void rdoIsStatementCharges_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoIsStatementCharges_YesActionPerformed
        disabledTextBox(txtStatementCharges,panOtherCharges,false);
        // Add your handling code here:
    }//GEN-LAST:event_rdoIsStatementCharges_YesActionPerformed
    
    private void rdoIsStatementCharges_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoIsStatementCharges_NoActionPerformed
        txtStatementCharges.setText("");
        disabledTextBox(txtStatementCharges,panOtherCharges,true);
        // Add your handling code here:
    }//GEN-LAST:event_rdoIsStatementCharges_NoActionPerformed
    
    private void rdoIsApplicableFCharges_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoIsApplicableFCharges_NoActionPerformed
        
        if(rdoIsApplicableFCharges_No.isSelected()) {
            ClientUtil.clearAll(panFolioCharges);
            ClientUtil.enableDisable(panFolioCharges, false);
            rdoIsApplicableFCharges_No.setSelected(true);
            rdoIsApplicableFCharges_No.setEnabled(true);
            rdoIsApplicableFCharges_Yes.setEnabled(true);
        }
    }//GEN-LAST:event_rdoIsApplicableFCharges_NoActionPerformed
    
    private void rdoCreditIntInterestPayable_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoCreditIntInterestPayable_NoActionPerformed
       
        if(rdoCreditIntInterestPayable_No.isSelected()) {
            ClientUtil.clearAll(panInterestPayable);
            ClientUtil.enableDisable(panInterestPayable, false);
            
            rdoCreditCompdInterestPayable_No.setSelected(true);
            rdoAdditionalIntInterestPayable_No.setSelected(true);
            
            rdoCreditIntInterestPayable_No.setSelected(true);
            rdoCreditIntInterestPayable_No.setEnabled(true);
            rdoCreditIntInterestPayable_Yes.setEnabled(true);
        }
        // Add your handling code here:
    }//GEN-LAST:event_rdoCreditIntInterestPayable_NoActionPerformed
    
    private void rdoCreditIntInterestPayable_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoCreditIntInterestPayable_YesActionPerformed
        ClientUtil.enableDisable(panInterestPayable, true);
        if(rdoCreditCompdInterestPayable_No.isSelected()){
            cboCreditInterestCompdFIP.setEnabled(false);
        }
        if(rdoAdditionalIntInterestPayable_No.isSelected()){
            ClientUtil.enableDisable(panAddInterestRateIP,false);
        }
        // Add your handling code here:
    }//GEN-LAST:event_rdoCreditIntInterestPayable_YesActionPerformed
    
    private void rdoIsApplicablePLRIR_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoIsApplicablePLRIR_NoActionPerformed
        
        if(rdoIsApplicablePLRIR_No.isSelected()) {
            ClientUtil.clearAll(panPLRIR);
            ClientUtil.enableDisable(panPLRIR, false);
            rdoIsApplicablePLRIR_No.setSelected(true);
            rdoIsApplicablePLRIR_No.setEnabled(true);
            rdoIsApplicablePLRIR_Yes.setEnabled(true);
        }
        
        // Add your handling code here:
    }//GEN-LAST:event_rdoIsApplicablePLRIR_NoActionPerformed
    
    private void rdoIsApplicablePLRIR_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoIsApplicablePLRIR_YesActionPerformed
        ClientUtil.enableDisable(panPLRIR,true);
        // Add your handling code here:
    }//GEN-LAST:event_rdoIsApplicablePLRIR_YesActionPerformed
    
    private void rdoChargedDIIR_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoChargedDIIR_NoActionPerformed
        if(rdoChargedDIIR_No.isSelected()) {
            ClientUtil.clearAll(panDebitInterestIR);
            ClientUtil.enableDisable(panDebitInterestIR, false);
            rdoChargedDIIR_No.setSelected(true);
            rdoChargedDIIR_No.setEnabled(true);
            rdoChargedDIIR_Yes.setEnabled(true);
        }
    }//GEN-LAST:event_rdoChargedDIIR_NoActionPerformed
    
    private void rdoChargedDIIR_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoChargedDIIR_YesActionPerformed
        ClientUtil.enableDisable(panDebitInterestIR, true);
        if(rdoDebitCompoundIR_No.isSelected()){
            cboDICompoundFIR.setEnabled(false);
        }
        // Add your handling code here:
    }//GEN-LAST:event_rdoChargedDIIR_YesActionPerformed
    
    private void cboDIApplicationFIRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboDIApplicationFIRActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_cboDIApplicationFIRActionPerformed
    
    private void rdoIsApplicableFCharges_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoIsApplicableFCharges_YesActionPerformed
        ClientUtil.enableDisable(panFolioCharges,true);
        // Add your handling code here:
    }//GEN-LAST:event_rdoIsApplicableFCharges_YesActionPerformed
    
    private void txtPenalIROthersIRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPenalIROthersIRActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_txtPenalIROthersIRActionPerformed
    
    private void rdoIsCommitmentCharges_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoIsCommitmentCharges_NoActionPerformed
        //        txtCommitmentCharges.setText("0.0");
        txtCommitmentCharges.setText("");
        cboCommitmentCharges.setSelectedItem("");
        
        ClientUtil.enableDisable(panCommitmentCharges,false);
        // Add your handling code here:
    }//GEN-LAST:event_rdoIsCommitmentCharges_NoActionPerformed
    
    private void rdoAdditionalIntInterestPayable_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoAdditionalIntInterestPayable_NoActionPerformed
        //        txtAddIntRateIP.setText("0.0");
        txtAddIntRateIP.setText("");
        ClientUtil.enableDisable(panAddInterestRateIP,false);
        // Add your handling code here:
    }//GEN-LAST:event_rdoAdditionalIntInterestPayable_NoActionPerformed
    
    private void rdoTokanAccount_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoTokanAccount_YesActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_rdoTokanAccount_YesActionPerformed
    
    private void rdoLDAccount_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoLDAccount_YesActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_rdoLDAccount_YesActionPerformed
    
    private void callView(int currField) {
        viewType = currField;
        HashMap viewMap = new HashMap();
//        if (currField==EDIT || currField == DELETE || currField== AUTHORIZE){
//            viewMap.put(CommonConstants.MAP_NAME, "getLoansProductTOData");
//            
//        }else if(currField==PRODID){
//            viewMap.put(CommonConstants.MAP_NAME, "Advances.getSelectProduct");
//            
//        }else{
            viewMap.put(CommonConstants.MAP_NAME, "OperativeAcctProduct.getSelectAcctHeadTOList");
//        }
        new ViewAll(this, viewMap, true).show();
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
        
        btnNewCRIC.setEnabled(!btnNew.isEnabled());
        btnNewCROC.setEnabled(!btnNew.isEnabled());
        btnNewSI.setEnabled(!btnNew.isEnabled());
        btnSaveCRIC.setEnabled(!btnNewCRIC.isEnabled());
        btnDeleteCRIC.setEnabled(!btnNewCRIC.isEnabled());
        btnSaveCROC.setEnabled(!btnNewCROC.isEnabled());
        btnDeleteCROC.setEnabled(!btnNewCROC.isEnabled());
        btnSaveSI.setEnabled(!btnNewSI.isEnabled());
        btnDeleteSI.setEnabled(!btnNewSI.isEnabled());
    }
    
    private void setFieldsInVisible(){
        lblProdCurrency.setVisible(false);
        cboProdCurrency.setVisible(false);
        
        lblBehaveLike.setVisible(false);
        cboBehavesLike.setVisible(false);
        
        lblNumberPattern.setVisible(false);
        txtNumberpatternAccount.setVisible(false);
        
        lblLastAccNo.setVisible(false);
        txtLastAccNoAccount.setVisible(false);
        
        lblLimitDefination.setVisible(false);
        panLDI2Account.setVisible(false);
        
        lblStaffAccOpened.setVisible(false);
        panSAOI2Account.setVisible(false);
        
        lblDebitInterestOnDAUE.setVisible(false);
        panDIUEI2Account.setVisible(false);
        
        btnAuthorize.setVisible(false);
        btnReject.setVisible(false);
        btnException.setVisible(false);
        lblSpace5.setVisible(false);
    }
    
    //This method  fill sthe data inthe UI components after selecting a row in ViewAll.
    public void fillData(Object obj) {
        HashMap hash = (HashMap) obj;
        System.out.println("HashMap: "  + hash);

        if (viewType== EDIT || viewType==DELETE || viewType==AUTHORIZE) {
            hash.put(CommonConstants.MAP_WHERE, hash.get("PROD_ID"));
            observable.populateData(hash);
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || viewType==AUTHORIZE ) {
                isFilled = true;
                
                ClientUtil.enableDisable(this, false);
                setButtonsEnable(false);
                if(hash.containsKey("ACTION_TYPE"))
                    observable.setActionType(CommonUtil.convertObjToInt(hash.get("ACTION_TYPE")));
                 btnAuthorize.setVisible(true);
                  btnReject.setVisible(true);
               
            }
            else {
                ClientUtil.enableDisable(this, true);
                setButtonsEnable(true);
                btnNew.setEnabled(true);
            }
            
                observable.setStatus();             // To set the Value of lblStatus...
                setButtonEnableDisable();
        }else if (viewType==AccountHead) {
            txtAccountHeadAccount.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
        }else if (viewType==AcctClosingChrg) {
            txtACCAH.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
        } else if (viewType==MiscServChrg) {
            txtMSCAH.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
        } else if (viewType==StatChrg) {
            txtSCAH.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
        } else if (viewType==AcctDIHead) {
            txtDIAH.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
        } else if (viewType==PenalIntHead) {
            txtPIAH.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
        } else if (viewType==AcctCIHead) {
            txtCIAH.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
        } else if (viewType==ClearingIntAcctHead) {
            txtAgCIAH.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
        } else if (viewType==ExpIntHead) {
            txtEIAH.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
        } else if (viewType==ExcessOLHead) {
            txtExOLHAH.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
        } else if (viewType==ChqBookIssChrgHead) {
            txtCICAH.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
        } else if (viewType==StopPaymentChrgHead) {
            txtSPCAH.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
        } else if (viewType==ChqRetInwardChrgHead) {
            txtCRCInwardAH.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
        } else if (viewType==ChqRetOutwardChrgHead) {
            txtCRCoutwardAH.setText(CommonUtil.convertObjToStr( hash.get("AC_HD_ID")));
        } else if (viewType==FolioChrgHead) {
            txtFCAH.setText(CommonUtil.convertObjToStr(hash.get("AC_HD_ID")));
        }else if(viewType==PRODID){
            txtProductIdAccount.setText(CommonUtil.convertObjToStr(hash.get("PRODUCT ID")));
            txtProductDescAccount.setText(CommonUtil.convertObjToStr(hash.get("PRODUCT DESCRIPTION")));
        }
       
         if(viewType==AUTHORIZE) {
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
             }
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }
    
    private void populateData(HashMap hash){
        hash.put(CommonConstants.MAP_WHERE, hash.get("PROD_ID"));
        observable.populateData(hash);
    }
    
    private void setProdEnable(){
        txtProductIdAccount.setEditable(false);
        txtProductDescAccount.setEditable(false);
        btnProdID.setEnabled(false);
    }
    
    private void setButtonsEnable(boolean value){
        btnAccountHeadAccountHelp.setEnabled(value);
        btnACCAHHelp.setEnabled(value);
        btnMSCAHHelp.setEnabled(value);
        btnSCAHHelp.setEnabled(value);
        btnDIAHHelp.setEnabled(value);
        btnPIAHHelp.setEnabled(value);
        btnAgCIAHHelp.setEnabled(value);
        btnCIAHHelp.setEnabled(value);
        btnEIAHHelp.setEnabled(value);
        btnExOLHAHelp.setEnabled(value);
        btnCICAHHelp.setEnabled(value);
        btnSPCAHHelp.setEnabled(value);
        btnCRCInwardAHHelp.setEnabled(value);
        btnCRCoutwardAHHelp.setEnabled(value);
        btnFCAHHelp.setEnabled(value);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnACCAHHelp;
    private com.see.truetransact.uicomponent.CButton btnAccountHeadAccountHelp;
    private com.see.truetransact.uicomponent.CButton btnAgCIAHHelp;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCIAHHelp;
    private com.see.truetransact.uicomponent.CButton btnCICAHHelp;
    private com.see.truetransact.uicomponent.CButton btnCRCInwardAHHelp;
    private com.see.truetransact.uicomponent.CButton btnCRCoutwardAHHelp;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDIAHHelp;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDeleteCRIC;
    private com.see.truetransact.uicomponent.CButton btnDeleteCROC;
    private com.see.truetransact.uicomponent.CButton btnDeleteSI;
    private com.see.truetransact.uicomponent.CButton btnEIAHHelp;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnExOLHAHelp;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnFCAHHelp;
    private com.see.truetransact.uicomponent.CButton btnMSCAHHelp;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnNewCRIC;
    private com.see.truetransact.uicomponent.CButton btnNewCROC;
    private com.see.truetransact.uicomponent.CButton btnNewSI;
    private com.see.truetransact.uicomponent.CButton btnPIAHHelp;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnProdID;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSCAHHelp;
    private com.see.truetransact.uicomponent.CButton btnSPCAHHelp;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnSaveCRIC;
    private com.see.truetransact.uicomponent.CButton btnSaveCROC;
    private com.see.truetransact.uicomponent.CButton btnSaveSI;
    private com.see.truetransact.uicomponent.CMenuBar cMenuBar1;
    private com.see.truetransact.uicomponent.CMenuBar cMenuBar2;
    private com.see.truetransact.uicomponent.CComboBox cboAmountCRIC;
    private com.see.truetransact.uicomponent.CComboBox cboAmountCROC;
    private com.see.truetransact.uicomponent.CComboBox cboAssetsSI;
    private com.see.truetransact.uicomponent.CComboBox cboBehavesLike;
    private com.see.truetransact.uicomponent.CComboBox cboCAFrequencyFCharges;
    private com.see.truetransact.uicomponent.CComboBox cboCIROIP;
    private com.see.truetransact.uicomponent.CComboBox cboCLPeriod;
    private com.see.truetransact.uicomponent.CComboBox cboCPROIP;
    private com.see.truetransact.uicomponent.CComboBox cboCalcCtriteriaIP;
    private com.see.truetransact.uicomponent.CComboBox cboChargeOnDocFCharges;
    private com.see.truetransact.uicomponent.CComboBox cboChargeOnTransactionFCharges;
    private com.see.truetransact.uicomponent.CComboBox cboCollectChargeFCharges;
    private com.see.truetransact.uicomponent.CComboBox cboCommitmentCharges;
    private com.see.truetransact.uicomponent.CComboBox cboCreditInterestAFIP;
    private com.see.truetransact.uicomponent.CComboBox cboCreditInterestCFIP;
    private com.see.truetransact.uicomponent.CComboBox cboCreditInterestCompdFIP;
    private com.see.truetransact.uicomponent.CComboBox cboDIApplicationFIR;
    private com.see.truetransact.uicomponent.CComboBox cboDICalculationFIR;
    private com.see.truetransact.uicomponent.CComboBox cboDICompoundFIR;
    private com.see.truetransact.uicomponent.CComboBox cboDIRoundOffIR;
    private com.see.truetransact.uicomponent.CComboBox cboDPRoundOffIR;
    private com.see.truetransact.uicomponent.CComboBox cboIRFrequencyFCharges;
    private com.see.truetransact.uicomponent.CComboBox cboProcessingCharges;
    private com.see.truetransact.uicomponent.CComboBox cboProdCurrency;
    private com.see.truetransact.uicomponent.CComboBox cboProdFrequencyIP;
    private com.see.truetransact.uicomponent.CComboBox cboProductFOthersIR;
    private com.see.truetransact.uicomponent.CComboBox cboStmtFrequency;
    private javax.swing.JMenu jMenu1;
    private com.see.truetransact.uicomponent.CLabel lblACA;
    private com.see.truetransact.uicomponent.CLabel lblACCAH;
    private com.see.truetransact.uicomponent.CLabel lblAIRStaffIP;
    private com.see.truetransact.uicomponent.CLabel lblAIStaffIP;
    private com.see.truetransact.uicomponent.CLabel lblATMCardSI;
    private com.see.truetransact.uicomponent.CLabel lblAccountCLosingCharges;
    private com.see.truetransact.uicomponent.CLabel lblAccountHead;
    private com.see.truetransact.uicomponent.CLabel lblAccountSFPLRIR;
    private com.see.truetransact.uicomponent.CLabel lblAddIntRateIP;
    private com.see.truetransact.uicomponent.CLabel lblAgCIAH;
    private com.see.truetransact.uicomponent.CLabel lblAmountCRIC;
    private com.see.truetransact.uicomponent.CLabel lblAmountCROC;
    private com.see.truetransact.uicomponent.CLabel lblAppliedFromPLRIR;
    private com.see.truetransact.uicomponent.CLabel lblAssetsSI;
    private com.see.truetransact.uicomponent.CLabel lblBehaveLike;
    private com.see.truetransact.uicomponent.CLabel lblBranchBankingSI;
    private com.see.truetransact.uicomponent.CLabel lblCAFrequencyFCharges;
    private com.see.truetransact.uicomponent.CLabel lblCIAH;
    private com.see.truetransact.uicomponent.CLabel lblCICAH;
    private com.see.truetransact.uicomponent.CLabel lblCIROIP;
    private com.see.truetransact.uicomponent.CLabel lblCLPeriod;
    private com.see.truetransact.uicomponent.CLabel lblCLStart;
    private com.see.truetransact.uicomponent.CLabel lblCPROIP;
    private com.see.truetransact.uicomponent.CLabel lblCRCInwardAH;
    private com.see.truetransact.uicomponent.CLabel lblCRCOutwardAH;
    private com.see.truetransact.uicomponent.CLabel lblCalcCriteriaIP;
    private com.see.truetransact.uicomponent.CLabel lblChargeOnDocFCharges;
    private com.see.truetransact.uicomponent.CLabel lblChargeOnTransactionFCharges;
    private com.see.truetransact.uicomponent.CLabel lblChargedDIIR;
    private com.see.truetransact.uicomponent.CLabel lblChequeLeaves;
    private com.see.truetransact.uicomponent.CLabel lblCollectChargeFCharges;
    private com.see.truetransact.uicomponent.CLabel lblCrediInterestAFIP;
    private com.see.truetransact.uicomponent.CLabel lblCreditCardSI;
    private com.see.truetransact.uicomponent.CLabel lblCreditCompdIP;
    private com.see.truetransact.uicomponent.CLabel lblCreditInterestAmt;
    private com.see.truetransact.uicomponent.CLabel lblCreditInterestCFIP;
    private com.see.truetransact.uicomponent.CLabel lblCreditInterestCompdFIP;
    private com.see.truetransact.uicomponent.CLabel lblCreditInterestIP;
    private com.see.truetransact.uicomponent.CLabel lblCreditInterestOnUE;
    private com.see.truetransact.uicomponent.CLabel lblCreditInterestRateIIP;
    private com.see.truetransact.uicomponent.CLabel lblDCRequiredIR;
    private com.see.truetransact.uicomponent.CLabel lblDIAH;
    private com.see.truetransact.uicomponent.CLabel lblDIApplicationFIR;
    private com.see.truetransact.uicomponent.CLabel lblDICalculationFIR;
    private com.see.truetransact.uicomponent.CLabel lblDICompoundFIR;
    private com.see.truetransact.uicomponent.CLabel lblDIRoundOffIR;
    private com.see.truetransact.uicomponent.CLabel lblDPRoundOffIR;
    private com.see.truetransact.uicomponent.CLabel lblDebitCardSI;
    private com.see.truetransact.uicomponent.CLabel lblDebitInterestOnDAUE;
    private com.see.truetransact.uicomponent.CLabel lblDueDateFCharges;
    private com.see.truetransact.uicomponent.CLabel lblEIAH;
    private com.see.truetransact.uicomponent.CLabel lblEOLOthersIR;
    private com.see.truetransact.uicomponent.CLabel lblExOLHAH;
    private com.see.truetransact.uicomponent.CLabel lblExistingAccountPLRIR;
    private com.see.truetransact.uicomponent.CLabel lblFCAH;
    private com.see.truetransact.uicomponent.CLabel lblFolioEntriesFCharges;
    private com.see.truetransact.uicomponent.CLabel lblIRFrequencyFCharges;
    private com.see.truetransact.uicomponent.CLabel lblInterestADDebitIR;
    private com.see.truetransact.uicomponent.CLabel lblInterestCDDebitIR;
    private com.see.truetransact.uicomponent.CLabel lblIsApplicableFCharges;
    private com.see.truetransact.uicomponent.CLabel lblIsApplicablePLRIR;
    private com.see.truetransact.uicomponent.CLabel lblIsChequebookCharges;
    private com.see.truetransact.uicomponent.CLabel lblIsCommitmentCharges;
    private com.see.truetransact.uicomponent.CLabel lblIsProcessingCharges;
    private com.see.truetransact.uicomponent.CLabel lblIsStatementCharges;
    private com.see.truetransact.uicomponent.CLabel lblIsStopPaymentCharges;
    private com.see.truetransact.uicomponent.CLabel lblLastADIP;
    private com.see.truetransact.uicomponent.CLabel lblLastAccNo;
    private com.see.truetransact.uicomponent.CLabel lblLastAppliedFCharges;
    private com.see.truetransact.uicomponent.CLabel lblLastCDIP;
    private com.see.truetransact.uicomponent.CLabel lblLimitDefination;
    private com.see.truetransact.uicomponent.CLabel lblLimitEIOthersIR;
    private com.see.truetransact.uicomponent.CLabel lblMD4EOL;
    private com.see.truetransact.uicomponent.CLabel lblMSCAH;
    private com.see.truetransact.uicomponent.CLabel lblMaxAmount4WS;
    private com.see.truetransact.uicomponent.CLabel lblMaxDIAmtIR;
    private com.see.truetransact.uicomponent.CLabel lblMaxDIRateIR;
    private com.see.truetransact.uicomponent.CLabel lblMicsServiceCharges;
    private com.see.truetransact.uicomponent.CLabel lblMinDIAmtIR;
    private com.see.truetransact.uicomponent.CLabel lblMinDIRateIR;
    private com.see.truetransact.uicomponent.CLabel lblMobileBankingClientSI;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNewAccountPLRIR;
    private com.see.truetransact.uicomponent.CLabel lblNumberPattern;
    private com.see.truetransact.uicomponent.CLabel lblODOverAboveLimimt;
    private com.see.truetransact.uicomponent.CLabel lblPIAH;
    private com.see.truetransact.uicomponent.CLabel lblPSCommitmentCharges;
    private com.see.truetransact.uicomponent.CLabel lblPSProcessingCharges;
    private com.see.truetransact.uicomponent.CLabel lblPenalIROthersIR;
    private com.see.truetransact.uicomponent.CLabel lblPenalOthersIR;
    private com.see.truetransact.uicomponent.CLabel lblPercentageCreditInterestRateIP;
    private com.see.truetransact.uicomponent.CLabel lblPercentageMDAccount;
    private com.see.truetransact.uicomponent.CLabel lblPercentageMaxDIRateIR;
    private com.see.truetransact.uicomponent.CLabel lblPercentageMinDIRateIR;
    private com.see.truetransact.uicomponent.CLabel lblPercentagePenalIROthersIR;
    private com.see.truetransact.uicomponent.CLabel lblPercentageRatePLRIR;
    private com.see.truetransact.uicomponent.CLabel lblProcessingCharges;
    private com.see.truetransact.uicomponent.CLabel lblProdCurrency;
    private com.see.truetransact.uicomponent.CLabel lblProdDesc;
    private com.see.truetransact.uicomponent.CLabel lblProdFrequencyIP;
    private com.see.truetransact.uicomponent.CLabel lblProdId;
    private com.see.truetransact.uicomponent.CLabel lblProductFOthersIR;
    private com.see.truetransact.uicomponent.CLabel lblRateCRIC;
    private com.see.truetransact.uicomponent.CLabel lblRateCROC;
    private com.see.truetransact.uicomponent.CLabel lblRateFCharges;
    private com.see.truetransact.uicomponent.CLabel lblRatePLRIR;
    private com.see.truetransact.uicomponent.CLabel lblRateSI;
    private com.see.truetransact.uicomponent.CLabel lblSCAH;
    private com.see.truetransact.uicomponent.CLabel lblSPCAH;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStaffAccOpened;
    private com.see.truetransact.uicomponent.CLabel lblStateFrequency;
    private com.see.truetransact.uicomponent.CLabel lblStatementCharges;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblToken;
    private com.see.truetransact.uicomponent.CLabel lblUAICOthersIR;
    private com.see.truetransact.uicomponent.CLabel lblWithdrawalSlip;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panACAI2Account;
    private com.see.truetransact.uicomponent.CPanel panAccount;
    private com.see.truetransact.uicomponent.CPanel panAccountHead;
    private com.see.truetransact.uicomponent.CPanel panAccountHeadAccount;
    private com.see.truetransact.uicomponent.CPanel panActHead;
    private com.see.truetransact.uicomponent.CPanel panAddInterestIP;
    private com.see.truetransact.uicomponent.CPanel panAddInterestRateIP;
    private com.see.truetransact.uicomponent.CPanel panButtonSI;
    private com.see.truetransact.uicomponent.CPanel panButtonsCRIC;
    private com.see.truetransact.uicomponent.CPanel panButtonsCROC;
    private com.see.truetransact.uicomponent.CPanel panCIUEI2Account;
    private com.see.truetransact.uicomponent.CPanel panCLPI2Account;
    private com.see.truetransact.uicomponent.CPanel panCRChargesInward;
    private com.see.truetransact.uicomponent.CPanel panCRChargesOutward;
    private com.see.truetransact.uicomponent.CPanel panChagres;
    private com.see.truetransact.uicomponent.CPanel panChargedDIIR;
    private com.see.truetransact.uicomponent.CPanel panCharges;
    private com.see.truetransact.uicomponent.CPanel panCommitmentCharges;
    private com.see.truetransact.uicomponent.CPanel panCreditCompdIP;
    private com.see.truetransact.uicomponent.CPanel panCreditInterestIP;
    private com.see.truetransact.uicomponent.CPanel panCreditInterestRateIP;
    private com.see.truetransact.uicomponent.CPanel panDIUEI2Account;
    private com.see.truetransact.uicomponent.CPanel panDebitCompoundIR;
    private com.see.truetransact.uicomponent.CPanel panDebitInterestIR;
    private com.see.truetransact.uicomponent.CPanel panFolioCharges;
    private com.see.truetransact.uicomponent.CPanel panInsideAccount;
    private com.see.truetransact.uicomponent.CPanel panInterestPayable;
    private com.see.truetransact.uicomponent.CPanel panInterestReceivable;
    private com.see.truetransact.uicomponent.CPanel panIsApplicableFCharges;
    private com.see.truetransact.uicomponent.CPanel panIsApplicablePLRIR;
    private com.see.truetransact.uicomponent.CPanel panIsChequebookCharges;
    private com.see.truetransact.uicomponent.CPanel panIsCommitmentCharges;
    private com.see.truetransact.uicomponent.CPanel panIsProcessingCharges;
    private com.see.truetransact.uicomponent.CPanel panIsStatementCharges;
    private com.see.truetransact.uicomponent.CPanel panIsStopPaymentCharges;
    private com.see.truetransact.uicomponent.CPanel panLDI2Account;
    private com.see.truetransact.uicomponent.CPanel panManagerDistAccount;
    private com.see.truetransact.uicomponent.CPanel panMaxDIRateIR;
    private com.see.truetransact.uicomponent.CPanel panMinDIRateIR;
    private com.see.truetransact.uicomponent.CPanel panMiscItemSI;
    private com.see.truetransact.uicomponent.CPanel panMiscdata;
    private com.see.truetransact.uicomponent.CPanel panODALI2Account;
    private com.see.truetransact.uicomponent.CPanel panOtherCharges;
    private com.see.truetransact.uicomponent.CPanel panOthersIR;
    private com.see.truetransact.uicomponent.CPanel panPLRIR;
    private com.see.truetransact.uicomponent.CPanel panProcessingCharges;
    private com.see.truetransact.uicomponent.CPanel panProductAccount;
    private com.see.truetransact.uicomponent.CPanel panSAOI2Account;
    private com.see.truetransact.uicomponent.CPanel panSpecialItems;
    private com.see.truetransact.uicomponent.CPanel panSplItemDetails;
    private com.see.truetransact.uicomponent.CPanel panSplItems;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTokenI2Account;
    private com.see.truetransact.uicomponent.CPanel panWSI2Account;
    private com.see.truetransact.uicomponent.CButtonGroup rdoACAAcccount;
    private com.see.truetransact.uicomponent.CRadioButton rdoACAAccount_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoACAAccount_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoATMCardSI;
    private com.see.truetransact.uicomponent.CRadioButton rdoATMCardSI_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoATMCardSI_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoAdditionalIntInterestPayable;
    private com.see.truetransact.uicomponent.CRadioButton rdoAdditionalIntInterestPayable_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoAdditionalIntInterestPayable_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoBranchBankingSI;
    private com.see.truetransact.uicomponent.CRadioButton rdoBranchBankingSI_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoBranchBankingSI_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoCIUEAccount;
    private com.see.truetransact.uicomponent.CRadioButton rdoCIUEAccount_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoCIUEAccount_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoChargedDIIR;
    private com.see.truetransact.uicomponent.CRadioButton rdoChargedDIIR_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoChargedDIIR_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoCreditCardSI;
    private com.see.truetransact.uicomponent.CRadioButton rdoCreditCardSI_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoCreditCardSI_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoCreditCompdInterestPayable;
    private com.see.truetransact.uicomponent.CRadioButton rdoCreditCompdInterestPayable_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoCreditCompdInterestPayable_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoCreditIntInterestPayable;
    private com.see.truetransact.uicomponent.CRadioButton rdoCreditIntInterestPayable_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoCreditIntInterestPayable_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoDIAUEAccount;
    private com.see.truetransact.uicomponent.CRadioButton rdoDIAUEAccount_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoDIAUEAccount_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoDebitCardSI;
    private com.see.truetransact.uicomponent.CRadioButton rdoDebitCardSI_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoDebitCardSI_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoDebitCompoundIR;
    private com.see.truetransact.uicomponent.CRadioButton rdoDebitCompoundIR_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoDebitCompoundIR_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoEOLOthersIR;
    private com.see.truetransact.uicomponent.CRadioButton rdoEOLOthersIR_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoEOLOthersIR_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoExistingAccountPLRIR;
    private com.see.truetransact.uicomponent.CRadioButton rdoExistingAccountPLRIR_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoExistingAccountPLRIR_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoIsApplicableFCharges;
    private com.see.truetransact.uicomponent.CRadioButton rdoIsApplicableFCharges_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoIsApplicableFCharges_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoIsApplicablePLRIR;
    private com.see.truetransact.uicomponent.CRadioButton rdoIsApplicablePLRIR_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoIsApplicablePLRIR_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoIsChequebookCharges;
    private com.see.truetransact.uicomponent.CRadioButton rdoIsChequebookCharges_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoIsChequebookCharges_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoIsCommitmentCharges;
    private com.see.truetransact.uicomponent.CRadioButton rdoIsCommitmentCharges_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoIsCommitmentCharges_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoIsProcessingCharges;
    private com.see.truetransact.uicomponent.CRadioButton rdoIsProcessingCharges_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoIsProcessingCharges_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoIsStatementCharges;
    private com.see.truetransact.uicomponent.CRadioButton rdoIsStatementCharges_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoIsStatementCharges_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoIsStopPaymentCharges;
    private com.see.truetransact.uicomponent.CRadioButton rdoIsStopPaymentCharges_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoIsStopPaymentCharges_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoLDAccount;
    private com.see.truetransact.uicomponent.CRadioButton rdoLDAccount_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoLDAccount_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoLimitEIOthersIR;
    private com.see.truetransact.uicomponent.CRadioButton rdoLimitEIOthersIR_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoLimitEIOthersIR_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoMobileBankingClientSI;
    private com.see.truetransact.uicomponent.CRadioButton rdoMobileBankingClientSI_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoMobileBankingClientSI_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoNewAccountPLRIR;
    private com.see.truetransact.uicomponent.CRadioButton rdoNewAccountPLRIR_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoNewAccountPLRIR_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoODALAccount;
    private com.see.truetransact.uicomponent.CRadioButton rdoODALAccount_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoODALAccount_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoPenalOthersIR;
    private com.see.truetransact.uicomponent.CRadioButton rdoPenalOthersIR_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoPenalOthersIR_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoSAOAccount;
    private com.see.truetransact.uicomponent.CRadioButton rdoSAOAccount_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoSAOAccount_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoTokanAccount_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoTokanAccount_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoTokenAccount;
    private com.see.truetransact.uicomponent.CButtonGroup rdoUAICOthersIR;
    private com.see.truetransact.uicomponent.CRadioButton rdoUAICOthersIR_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoUAICOthersIR_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoWSAccount;
    private com.see.truetransact.uicomponent.CRadioButton rdoWSAccount_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoWSAccount_Yes;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private com.see.truetransact.uicomponent.CSeparator sptProductAccount;
    private com.see.truetransact.uicomponent.CScrollPane srpCRChargesInward;
    private com.see.truetransact.uicomponent.CScrollPane srpCRChargesOutward;
    private com.see.truetransact.uicomponent.CScrollPane srpMiscItemSI;
    private com.see.truetransact.uicomponent.CTabbedPane tabProduct;
    private com.see.truetransact.uicomponent.CTable tblCRChargesInward;
    private com.see.truetransact.uicomponent.CTable tblCRChargesOutward;
    private com.see.truetransact.uicomponent.CTable tblMiscItemSI;
    private javax.swing.JToolBar tbrAdvances;
    private com.see.truetransact.uicomponent.CDateField tdtAccountSFPLRIR;
    private com.see.truetransact.uicomponent.CDateField tdtAppliedFromPLRIR;
    private com.see.truetransact.uicomponent.CDateField tdtCLStartAccount;
    private com.see.truetransact.uicomponent.CDateField tdtDueDateFCharges;
    private com.see.truetransact.uicomponent.CDateField tdtInterestADDebitIR;
    private com.see.truetransact.uicomponent.CDateField tdtInterestCDDebitIR;
    private com.see.truetransact.uicomponent.CDateField tdtLastADIP;
    private com.see.truetransact.uicomponent.CDateField tdtLastAppliedFCharges;
    private com.see.truetransact.uicomponent.CDateField tdtLastCDIP;
    private com.see.truetransact.uicomponent.CTextField txtACCAH;
    private com.see.truetransact.uicomponent.CTextField txtAccountClosingCharges;
    private com.see.truetransact.uicomponent.CTextField txtAccountHeadAccount;
    private com.see.truetransact.uicomponent.CTextField txtAddIntRateIP;
    private com.see.truetransact.uicomponent.CTextField txtAgCIAH;
    private com.see.truetransact.uicomponent.CTextField txtCIAH;
    private com.see.truetransact.uicomponent.CTextField txtCICAH;
    private com.see.truetransact.uicomponent.CTextField txtCLPeriodAccount;
    private com.see.truetransact.uicomponent.CTextField txtCRCInwardAH;
    private com.see.truetransact.uicomponent.CTextField txtCRCoutwardAH;
    private com.see.truetransact.uicomponent.CTextField txtChequebookCharges;
    private com.see.truetransact.uicomponent.CTextField txtCommitmentCharges;
    private com.see.truetransact.uicomponent.CTextField txtCreditInterestAmt;
    private com.see.truetransact.uicomponent.CTextField txtCreditInterestRateIP;
    private com.see.truetransact.uicomponent.CTextField txtDIAH;
    private com.see.truetransact.uicomponent.CTextField txtEIAH;
    private com.see.truetransact.uicomponent.CTextField txtExOLHAH;
    private com.see.truetransact.uicomponent.CTextField txtFCAH;
    private com.see.truetransact.uicomponent.CTextField txtFolioEntriesFCharges;
    private com.see.truetransact.uicomponent.CTextField txtFreeCLAccount;
    private com.see.truetransact.uicomponent.CTextField txtLastAccNoAccount;
    private com.see.truetransact.uicomponent.CTextField txtMSCAH;
    private com.see.truetransact.uicomponent.CTextField txtManagerDistAccount;
    private com.see.truetransact.uicomponent.CTextField txtMaxAmountOnWS;
    private com.see.truetransact.uicomponent.CTextField txtMaxDIAmtIR;
    private com.see.truetransact.uicomponent.CTextField txtMaxDIRateIR;
    private com.see.truetransact.uicomponent.CTextField txtMinDIAmtIR;
    private com.see.truetransact.uicomponent.CTextField txtMinDIRateIR;
    private com.see.truetransact.uicomponent.CTextField txtMiscServiceCharges;
    private com.see.truetransact.uicomponent.CTextField txtNumberpatternAccount;
    private com.see.truetransact.uicomponent.CTextField txtPIAH;
    private com.see.truetransact.uicomponent.CTextField txtPenalIROthersIR;
    private com.see.truetransact.uicomponent.CTextField txtProcessingCharges;
    private com.see.truetransact.uicomponent.CTextField txtProductDescAccount;
    private com.see.truetransact.uicomponent.CTextField txtProductIdAccount;
    private com.see.truetransact.uicomponent.CTextField txtRateCRIC;
    private com.see.truetransact.uicomponent.CTextField txtRateCROC;
    private com.see.truetransact.uicomponent.CTextField txtRateFCharges;
    private com.see.truetransact.uicomponent.CTextField txtRatePLRIR;
    private com.see.truetransact.uicomponent.CTextField txtRateSI;
    private com.see.truetransact.uicomponent.CTextField txtSCAH;
    private com.see.truetransact.uicomponent.CTextField txtSPCAH;
    private com.see.truetransact.uicomponent.CTextField txtStatementCharges;
    private com.see.truetransact.uicomponent.CTextField txtStopPaymentCharges;
    // End of variables declaration//GEN-END:variables
    
    
}
