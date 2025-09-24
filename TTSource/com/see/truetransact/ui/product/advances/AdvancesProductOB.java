/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AdvancesProductOB.java
 *
 * Created on December 1, 2003, 12:55 PM
 */

package com.see.truetransact.ui.product.advances;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.TableModel;

import com.see.truetransact.uicomponent.CTable;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.transferobject.product.advances.*;
import com.see.truetransact.transferobject.product.loan.LoanProductAccountTO;
import com.see.truetransact.transferobject.product.loan.agriculturecard.AgriLoanProductAccountTO;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientproxy.ProxyFactory;
import org.apache.log4j.Logger;
import java.util.Date;

/**
 *
 * @author
 */
public class AdvancesProductOB extends java.util.Observable {
    
    private HashMap operationMap = null;
    private ProxyFactory proxy = null;
    
    private String txtLastAccNoAccount = "";
    private String cboBehavesLike = "";
    private String tdtCLStartAccount = "";
    private String txtCLPeriodAccount = "";
    private String cboCLPeriod = "";
    private String txtAccountHeadAccount = "";
    private String txtManagerDistAccount = "";
    private String txtFreeCLAccount = "";
    private String cboStmtFrequency = "";
    private String txtNumberpatternAccount = "";
    private boolean rdoLDAccount_Yes = false;
    private boolean rdoLDAccount_No = false;
    private boolean rdoACAAccount_Yes = false;
    private boolean rdoACAAccount_No = false;
    private boolean rdoSAOAccount_Yes = false;
    private boolean rdoSAOAccount_No = false;
    private boolean rdoTokanAccount_Yes = false;
    private boolean rdoTokanAccount_No = false;
    private boolean rdoCIUEAccount_Yes = false;
    private boolean rdoCIUEAccount_No = false;
    private boolean rdoODALAccount_Yes = false;
    private boolean rdoODALAccount_No = false;
    private boolean rdoDIAUEAccount_Yes = false;
    private boolean rdoDIAUEAccount_No = false;
    private boolean rdoWSAccount_Yes = false;
    private boolean rdoWSAccount_No = false;
    private String txtMaxAmountOnWS = "";
    private String txtProductIdAccount = "";
    private String txtProductDescAccount = "";
    private boolean rdoChargedDIIR_Yes = false;
    private boolean rdoChargedDIIR_No = false;
    private String txtMinDIRateIR = "";
    private String txtMaxDIRateIR = "";
    private String txtMinDIAmtIR = "";
    private String txtMaxDIAmtIR = "";
    private String cboDICalculationFIR = "";
    private String tdtInterestCDDebitIR = "";
    private String cboDIApplicationFIR = "";
    private String tdtInterestADDebitIR = "";
    private boolean rdoDebitCompoundIR_Yes = false;
    private boolean rdoDebitCompoundIR_No = false;
    private String cboDICompoundFIR = "";
    private String cboDPRoundOffIR = "";
    private String cboDIRoundOffIR = "";
    private String cboProductFOthersIR = "";
    private boolean rdoUAICOthersIR_Yes = false;
    private boolean rdoUAICOthersIR_No = false;
    private boolean rdoEOLOthersIR_Yes = false;
    private boolean rdoEOLOthersIR_No = false;
    private boolean rdoPenalOthersIR_Yes = false;
    private boolean rdoPenalOthersIR_No = false;
    private boolean rdoLimitEIOthersIR_Yes = false;
    private boolean rdoLimitEIOthersIR_No = false;
    private String txtPenalIROthersIR = "";
    private boolean rdoIsApplicablePLRIR_Yes = false;
    private boolean rdoIsApplicablePLRIR_No = false;
    private String txtRatePLRIR = "";
    private String tdtAppliedFromPLRIR = "";
    private boolean rdoNewAccountPLRIR_Yes = false;
    private boolean rdoNewAccountPLRIR_No = false;
    private boolean rdoExistingAccountPLRIR_Yes = false;
    private boolean rdoExistingAccountPLRIR_No = false;
    private String tdtAccountSFPLRIR = "";
    private boolean rdoCreditIntInterestPayable_Yes = false;
    private boolean rdoCreditIntInterestPayable_No = false;
    private boolean rdoCreditCompdInterestPayable_Yes = false;
    private boolean rdoCreditCompdInterestPayable_No = false;
    private boolean rdoAdditionalIntInterestPayable_Yes = false;
    private boolean rdoAdditionalIntInterestPayable_No = false;
    private String cboCreditInterestCFIP = "";
    private String cboCreditInterestAFIP = "";
    private String cboCreditInterestCompdFIP = "";
    private String cboCPROIP = "";
    private String cboCIROIP = "";
    private String cboCalcCtriteriaIP = "";
    private String cboProdFrequencyIP = "";
    private String txtCreditInterestRateIP = "";
    private String tdtLastCDIP = "";
    private String tdtLastADIP = "";
    private String txtAddIntRateIP = "";
    private boolean rdoIsApplicableFCharges_Yes = false;
    private boolean rdoIsApplicableFCharges_No = false;
    private String tdtLastAppliedFCharges = "";
    private String tdtDueDateFCharges = "";
    private String txtFolioEntriesFCharges = "";
    private String txtRateFCharges = "";
    private String cboChargeOnTransactionFCharges = "";
    private String cboCAFrequencyFCharges = "";
    private String cboCollectChargeFCharges = "";
    private String cboChargeOnDocFCharges = "";
    private String cboIRFrequencyFCharges = "";
    private boolean rdoIsStatementCharges_Yes = false;
    private boolean rdoIsStatementCharges_No = false;
    private boolean rdoIsChequebookCharges_Yes = false;
    private boolean rdoIsChequebookCharges_No = false;
    private boolean rdoIsStopPaymentCharges_Yes = false;
    private boolean rdoIsStopPaymentCharges_No = false;
    private boolean rdoIsProcessingCharges_Yes = false;
    private boolean rdoIsProcessingCharges_No = false;
    private String txtProcessingCharges = "";
    private String cboProcessingCharges = "";
    private boolean rdoIsCommitmentCharges_Yes = false;
    private boolean rdoIsCommitmentCharges_No = false;
    private String txtCommitmentCharges = "";
    private String cboCommitmentCharges = "";
    private String txtAccountClosingCharges = "";
    private String txtStatementCharges = "";
    private String txtMiscServiceCharges = "";
    private String txtChequebookCharges = "";
    private String txtStopPaymentCharges = "";
    private String cboAmountCRIC = "";
    private String txtRateCRIC = "";
    private String cboAmountCROC = "";
    private String txtRateCROC = "";
    private boolean rdoATMCardSI_Yes = false;
    private boolean rdoATMCardSI_No = false;
    private boolean rdoCreditCardSI_Yes = false;
    private boolean rdoCreditCardSI_No = false;
    private boolean rdoDebitCardSI_Yes = false;
    private boolean rdoDebitCardSI_No = false;
    private boolean rdoMobileBankingClientSI_Yes = false;
    private boolean rdoMobileBankingClientSI_No = false;
    private boolean rdoBranchBankingSI_Yes = false;
    private boolean rdoBranchBankingSI_No = false;
    private String txtRateSI = "";
    private String cboAssetsSI = "";
    private String cboProdCurrency = "";
    private String txtACCAH = "";
    private String txtMSCAH = "";
    private String txtSCAH = "";
    private String txtDIAH = "";
    private String txtPIAH = "";
    private String txtCIAH = "";
    private String txtAgCIAH = "";
    private String txtEIAH = "";
    private String txtExOLHAH = "";
    private String txtCICAH = "";
    private String txtSPCAH = "";
    private String txtCRCInwardAH = "";
    private String txtCRCoutwardAH = "";
    private String txtFCAH = "";
    private String txtCreditInterestAmt="";
    private EnhancedTableModel tbmCRChargesInward;
    private EnhancedTableModel tbmCRChargesOutward;
    private EnhancedTableModel tbmMiscItemSI;
    
    private ComboBoxModel cbmStmtFrequency;
    private ComboBoxModel cbmBehavesLike;
    private ComboBoxModel cbmCLPeriod;
    private ComboBoxModel cbmDICalculationFIR;
    private ComboBoxModel cbmDIApplicationFIR;
    private ComboBoxModel cbmDICompoundFIR;
    private ComboBoxModel cbmProductFOthersIR;
    private ComboBoxModel cbmDPRoundOffIR;
    private ComboBoxModel cbmDIRoundOffIR;
    private ComboBoxModel cbmCreditInterestCFIP;
    private ComboBoxModel cbmCreditInterestAFIP;
    private ComboBoxModel cbmCreditInterestCompdFIP;
    private ComboBoxModel cbmProdFrequencyIP;
    private ComboBoxModel cbmCAFrequencyFCharges;
    private ComboBoxModel cbmIRFrequencyFCharges;
    private ComboBoxModel cbmCPROIP;
    private ComboBoxModel cbmCIROIP;
    private ComboBoxModel cbmCalcCtriteriaIP;
    private ComboBoxModel cbmCollectChargeFCharges;
    private ComboBoxModel cbmAmountCRIC;
    private ComboBoxModel cbmAmountCROC;
    private ComboBoxModel cbmAssetsSI;
    private ComboBoxModel cbmChargeOnTransactionFCharges;
    private ComboBoxModel cbmChargeOnDocFCharges;
    private ComboBoxModel cbmProcessingCharges;
    private ComboBoxModel cbmCommitmentCharges;
    private ComboBoxModel cbmProdCurrency;
    
    private HashMap lookUpHash;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private static AdvancesProductOB advancesProductOB;
    private final static com.see.truetransact.clientexception.ClientParseException parseException = com.see.truetransact.clientexception.ClientParseException.getInstance();
    
    private final static Logger log = Logger.getLogger(AdvancesProductOB.class);
    
    private final String CURRENCY = "INR";
    
    private final String YEAR ="YEARS";
    private final String MONTH ="MONTHS";
    private final String DAY ="DAYS";
    
    private final String YEAR1 ="Years";
    private final String MONTH1 ="Months";
    private final String DAY1 ="Days";
    
    private final String YES ="Y";
    private final String NO ="N";
    private String duration = "";
    
    private final int year = 365;
    private final int month = 30;
    private final int day = 1;
    
    private double periodData = 0; //__ for setting data depending on period comboboxes...
    private double resultData = 0; //__ for setting data depending on period comboboxes...
    
    private int resultValue=0; //__ for retrieving data from the period comboboxes...
    
    final String ABSOLUTE = "Absolute";
    final String PERCENT = "Percent";
    
    private int actionType;
    private int result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    Date curDate = null;
    
    /** Creates a new instance of AdvancesProductOB */
    public AdvancesProductOB() throws Exception{
        curDate = ClientUtil.getCurrentDate();
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "AdvancesProductJNDI");
        operationMap.put(CommonConstants.HOME, "com.see.truetransact.serverside.product.advances.AdvancesProductHome");
        operationMap.put(CommonConstants.REMOTE, "com.see.truetransact.serverside.product.advances.AdvancesProduct");
        
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            System.err.println( "Exception " + e + "Caught" );
            e.printStackTrace();
        }
        initUIComboBoxModel();
        initTableModels();
        fillDropdown();
    }
    
    public void initUIComboBoxModel(){
        cbmStmtFrequency = new ComboBoxModel();
        cbmBehavesLike = new ComboBoxModel();
        cbmCLPeriod = new ComboBoxModel();
        cbmDICalculationFIR = new ComboBoxModel();
        cbmDIApplicationFIR = new ComboBoxModel();
        cbmDICompoundFIR = new ComboBoxModel();
        cbmProductFOthersIR = new ComboBoxModel();
        cbmDPRoundOffIR = new ComboBoxModel();
        cbmDIRoundOffIR = new ComboBoxModel();
        cbmCreditInterestCFIP = new ComboBoxModel();
        cbmCreditInterestAFIP = new ComboBoxModel();
        cbmCreditInterestCompdFIP = new ComboBoxModel();
        cbmProdFrequencyIP = new ComboBoxModel();
        cbmCAFrequencyFCharges = new ComboBoxModel();
        cbmIRFrequencyFCharges = new ComboBoxModel();
        cbmCPROIP = new ComboBoxModel();
        cbmCIROIP = new ComboBoxModel();
        cbmCalcCtriteriaIP = new ComboBoxModel();
        cbmCollectChargeFCharges = new ComboBoxModel();
        cbmAmountCRIC = new ComboBoxModel();
        cbmAmountCROC = new ComboBoxModel();
        cbmAssetsSI = new ComboBoxModel();
        cbmChargeOnTransactionFCharges = new ComboBoxModel();
        cbmChargeOnDocFCharges = new ComboBoxModel();
        cbmProcessingCharges = new ComboBoxModel();
        cbmCommitmentCharges = new ComboBoxModel();
        
    }
    private void initTableModels(){
        tbmCRChargesInward = new EnhancedTableModel(
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
        };
        
        tbmCRChargesOutward = new EnhancedTableModel(
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
        };
        
        tbmMiscItemSI = new EnhancedTableModel(
        new Object [][] {
            
        },
        new String [] {
            "Assets", "Rate %"
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
        };
    }
    
    
    
    static {
        try {
            log.info("Creating AccountCreationOB...");
            advancesProductOB = new AdvancesProductOB();
        } catch(Exception e) {
            parseException.logException(e,true);
        }
    }
    
    
    /**
     * Returns an instance of AccountCreationOB.
     * @return  AccountCreationOB
     */
    public static AdvancesProductOB getInstance() {
        return advancesProductOB;
    }
    
    void setTxtLastAccNoAccount(String txtLastAccNoAccount){
        this.txtLastAccNoAccount = txtLastAccNoAccount;
        setChanged();
    }
    String getTxtLastAccNoAccount(){
        return this.txtLastAccNoAccount;
    }
    
    void setCboBehavesLike(String cboBehavesLike){
        this.cboBehavesLike = cboBehavesLike;
        setChanged();
    }
    String getCboBehavesLike(){
        return this.cboBehavesLike;
    }
    
    void setTdtCLStartAccount(String tdtCLStartAccount){
        this.tdtCLStartAccount = tdtCLStartAccount;
        setChanged();
    }
    String getTdtCLStartAccount(){
        return this.tdtCLStartAccount;
    }
    
    void setTxtCLPeriodAccount(String txtCLPeriodAccount){
        this.txtCLPeriodAccount = txtCLPeriodAccount;
        setChanged();
    }
    String getTxtCLPeriodAccount(){
        return this.txtCLPeriodAccount;
    }
    
    void setCboCLPeriod(String cboCLPeriod){
        this.cboCLPeriod = cboCLPeriod;
        setChanged();
    }
    String getCboCLPeriod(){
        return this.cboCLPeriod;
    }
    
    void setTxtAccountHeadAccount(String txtAccountHeadAccount){
        this.txtAccountHeadAccount = txtAccountHeadAccount;
        setChanged();
    }
    String getTxtAccountHeadAccount(){
        return this.txtAccountHeadAccount;
    }
    
    void setTxtManagerDistAccount(String txtManagerDistAccount){
        this.txtManagerDistAccount = txtManagerDistAccount;
        setChanged();
    }
    String getTxtManagerDistAccount(){
        return this.txtManagerDistAccount;
    }
    
    void setTxtFreeCLAccount(String txtFreeCLAccount){
        this.txtFreeCLAccount = txtFreeCLAccount;
        setChanged();
    }
    String getTxtFreeCLAccount(){
        return this.txtFreeCLAccount;
    }
    
    void setCboStmtFrequency(String cboStmtFrequency){
        this.cboStmtFrequency = cboStmtFrequency;
        setChanged();
    }
    String getCboStmtFrequency(){
        return this.cboStmtFrequency;
    }
    
    void setTxtNumberpatternAccount(String txtNumberpatternAccount){
        this.txtNumberpatternAccount = txtNumberpatternAccount;
        setChanged();
    }
    String getTxtNumberpatternAccount(){
        return this.txtNumberpatternAccount;
    }
    
    void setRdoLDAccount_Yes(boolean rdoLDAccount_Yes){
        this.rdoLDAccount_Yes = rdoLDAccount_Yes;
        setChanged();
    }
    boolean getRdoLDAccount_Yes(){
        return this.rdoLDAccount_Yes;
    }
    
    void setRdoLDAccount_No(boolean rdoLDAccount_No){
        this.rdoLDAccount_No = rdoLDAccount_No;
        setChanged();
    }
    boolean getRdoLDAccount_No(){
        return this.rdoLDAccount_No;
    }
    
    void setRdoACAAccount_Yes(boolean rdoACAAccount_Yes){
        this.rdoACAAccount_Yes = rdoACAAccount_Yes;
        setChanged();
    }
    boolean getRdoACAAccount_Yes(){
        return this.rdoACAAccount_Yes;
    }
    
    void setRdoACAAccount_No(boolean rdoACAAccount_No){
        this.rdoACAAccount_No = rdoACAAccount_No;
        setChanged();
    }
    boolean getRdoACAAccount_No(){
        return this.rdoACAAccount_No;
    }
    
    void setRdoSAOAccount_Yes(boolean rdoSAOAccount_Yes){
        this.rdoSAOAccount_Yes = rdoSAOAccount_Yes;
        setChanged();
    }
    boolean getRdoSAOAccount_Yes(){
        return this.rdoSAOAccount_Yes;
    }
    
    void setRdoSAOAccount_No(boolean rdoSAOAccount_No){
        this.rdoSAOAccount_No = rdoSAOAccount_No;
        setChanged();
    }
    boolean getRdoSAOAccount_No(){
        return this.rdoSAOAccount_No;
    }
    
    void setRdoTokanAccount_Yes(boolean rdoTokanAccount_Yes){
        this.rdoTokanAccount_Yes = rdoTokanAccount_Yes;
        setChanged();
    }
    boolean getRdoTokanAccount_Yes(){
        return this.rdoTokanAccount_Yes;
    }
    
    void setRdoTokanAccount_No(boolean rdoTokanAccount_No){
        this.rdoTokanAccount_No = rdoTokanAccount_No;
        setChanged();
    }
    boolean getRdoTokanAccount_No(){
        return this.rdoTokanAccount_No;
    }
    
    void setRdoCIUEAccount_Yes(boolean rdoCIUEAccount_Yes){
        this.rdoCIUEAccount_Yes = rdoCIUEAccount_Yes;
        setChanged();
    }
    boolean getRdoCIUEAccount_Yes(){
        return this.rdoCIUEAccount_Yes;
    }
    
    void setRdoCIUEAccount_No(boolean rdoCIUEAccount_No){
        this.rdoCIUEAccount_No = rdoCIUEAccount_No;
        setChanged();
    }
    boolean getRdoCIUEAccount_No(){
        return this.rdoCIUEAccount_No;
    }
    
    void setRdoODALAccount_Yes(boolean rdoODALAccount_Yes){
        this.rdoODALAccount_Yes = rdoODALAccount_Yes;
        setChanged();
    }
    boolean getRdoODALAccount_Yes(){
        return this.rdoODALAccount_Yes;
    }
    
    void setRdoODALAccount_No(boolean rdoODALAccount_No){
        this.rdoODALAccount_No = rdoODALAccount_No;
        setChanged();
    }
    boolean getRdoODALAccount_No(){
        return this.rdoODALAccount_No;
    }
    
    void setRdoDIAUEAccount_Yes(boolean rdoDIAUEAccount_Yes){
        this.rdoDIAUEAccount_Yes = rdoDIAUEAccount_Yes;
        setChanged();
    }
    boolean getRdoDIAUEAccount_Yes(){
        return this.rdoDIAUEAccount_Yes;
    }
    
    void setRdoDIAUEAccount_No(boolean rdoDIAUEAccount_No){
        this.rdoDIAUEAccount_No = rdoDIAUEAccount_No;
        setChanged();
    }
    boolean getRdoDIAUEAccount_No(){
        return this.rdoDIAUEAccount_No;
    }
    
    void setRdoWSAccount_Yes(boolean rdoWSAccount_Yes){
        this.rdoWSAccount_Yes = rdoWSAccount_Yes;
        setChanged();
    }
    boolean getRdoWSAccount_Yes(){
        return this.rdoWSAccount_Yes;
    }
    
    void setRdoWSAccount_No(boolean rdoWSAccount_No){
        this.rdoWSAccount_No = rdoWSAccount_No;
        setChanged();
    }
    boolean getRdoWSAccount_No(){
        return this.rdoWSAccount_No;
    }
    
    void setTxtMaxAmountOnWS(String txtMaxAmountOnWS){
        this.txtMaxAmountOnWS = txtMaxAmountOnWS;
        setChanged();
    }
    String getTxtMaxAmountOnWS(){
        return this.txtMaxAmountOnWS;
    }
    
    void setTxtProductIdAccount(String txtProductIdAccount){
        this.txtProductIdAccount = txtProductIdAccount;
        setChanged();
    }
    String getTxtProductIdAccount(){
        return this.txtProductIdAccount;
    }
    
    void setTxtProductDescAccount(String txtProductDescAccount){
        this.txtProductDescAccount = txtProductDescAccount;
        setChanged();
    }
    String getTxtProductDescAccount(){
        return this.txtProductDescAccount;
    }
    
    void setRdoChargedDIIR_Yes(boolean rdoChargedDIIR_Yes){
        this.rdoChargedDIIR_Yes = rdoChargedDIIR_Yes;
        setChanged();
    }
    boolean getRdoChargedDIIR_Yes(){
        return this.rdoChargedDIIR_Yes;
    }
    
    void setRdoChargedDIIR_No(boolean rdoChargedDIIR_No){
        this.rdoChargedDIIR_No = rdoChargedDIIR_No;
        setChanged();
    }
    boolean getRdoChargedDIIR_No(){
        return this.rdoChargedDIIR_No;
    }
    
    void setTxtMinDIRateIR(String txtMinDIRateIR){
        this.txtMinDIRateIR = txtMinDIRateIR;
        setChanged();
    }
    String getTxtMinDIRateIR(){
        return this.txtMinDIRateIR;
    }
    
    void setTxtMaxDIRateIR(String txtMaxDIRateIR){
        this.txtMaxDIRateIR = txtMaxDIRateIR;
        setChanged();
    }
    String getTxtMaxDIRateIR(){
        return this.txtMaxDIRateIR;
    }
    
    void setTxtMinDIAmtIR(String txtMinDIAmtIR){
        this.txtMinDIAmtIR = txtMinDIAmtIR;
        setChanged();
    }
    String getTxtMinDIAmtIR(){
        return this.txtMinDIAmtIR;
    }
    
    void setTxtMaxDIAmtIR(String txtMaxDIAmtIR){
        this.txtMaxDIAmtIR = txtMaxDIAmtIR;
        setChanged();
    }
    String getTxtMaxDIAmtIR(){
        return this.txtMaxDIAmtIR;
    }
    
    void setCboDICalculationFIR(String cboDICalculationFIR){
        this.cboDICalculationFIR = cboDICalculationFIR;
        setChanged();
    }
    String getCboDICalculationFIR(){
        return this.cboDICalculationFIR;
    }
    
    void setTdtInterestCDDebitIR(String tdtInterestCDDebitIR){
        this.tdtInterestCDDebitIR = tdtInterestCDDebitIR;
        setChanged();
    }
    String getTdtInterestCDDebitIR(){
        return this.tdtInterestCDDebitIR;
    }
    
    void setCboDIApplicationFIR(String cboDIApplicationFIR){
        this.cboDIApplicationFIR = cboDIApplicationFIR;
        setChanged();
    }
    String getCboDIApplicationFIR(){
        return this.cboDIApplicationFIR;
    }
    
    void setTdtInterestADDebitIR(String tdtInterestADDebitIR){
        this.tdtInterestADDebitIR = tdtInterestADDebitIR;
        setChanged();
    }
    String getTdtInterestADDebitIR(){
        return this.tdtInterestADDebitIR;
    }
    
    void setRdoDebitCompoundIR_Yes(boolean rdoDebitCompoundIR_Yes){
        this.rdoDebitCompoundIR_Yes = rdoDebitCompoundIR_Yes;
        setChanged();
    }
    boolean getRdoDebitCompoundIR_Yes(){
        return this.rdoDebitCompoundIR_Yes;
    }
    
    void setRdoDebitCompoundIR_No(boolean rdoDebitCompoundIR_No){
        this.rdoDebitCompoundIR_No = rdoDebitCompoundIR_No;
        setChanged();
    }
    boolean getRdoDebitCompoundIR_No(){
        return this.rdoDebitCompoundIR_No;
    }
    
    void setCboDICompoundFIR(String cboDICompoundFIR){
        this.cboDICompoundFIR = cboDICompoundFIR;
        setChanged();
    }
    String getCboDICompoundFIR(){
        return this.cboDICompoundFIR;
    }
    
    void setCboDPRoundOffIR(String cboDPRoundOffIR){
        this.cboDPRoundOffIR = cboDPRoundOffIR;
        setChanged();
    }
    String getCboDPRoundOffIR(){
        return this.cboDPRoundOffIR;
    }
    
    void setCboDIRoundOffIR(String cboDIRoundOffIR){
        this.cboDIRoundOffIR = cboDIRoundOffIR;
        setChanged();
    }
    String getCboDIRoundOffIR(){
        return this.cboDIRoundOffIR;
    }
    
    void setCboProductFOthersIR(String cboProductFOthersIR){
        this.cboProductFOthersIR = cboProductFOthersIR;
        setChanged();
    }
    String getCboProductFOthersIR(){
        return this.cboProductFOthersIR;
    }
    
    void setRdoUAICOthersIR_Yes(boolean rdoUAICOthersIR_Yes){
        this.rdoUAICOthersIR_Yes = rdoUAICOthersIR_Yes;
        setChanged();
    }
    boolean getRdoUAICOthersIR_Yes(){
        return this.rdoUAICOthersIR_Yes;
    }
    
    void setRdoUAICOthersIR_No(boolean rdoUAICOthersIR_No){
        this.rdoUAICOthersIR_No = rdoUAICOthersIR_No;
        setChanged();
    }
    boolean getRdoUAICOthersIR_No(){
        return this.rdoUAICOthersIR_No;
    }
    
    void setRdoEOLOthersIR_Yes(boolean rdoEOLOthersIR_Yes){
        this.rdoEOLOthersIR_Yes = rdoEOLOthersIR_Yes;
        setChanged();
    }
    boolean getRdoEOLOthersIR_Yes(){
        return this.rdoEOLOthersIR_Yes;
    }
    
    void setRdoEOLOthersIR_No(boolean rdoEOLOthersIR_No){
        this.rdoEOLOthersIR_No = rdoEOLOthersIR_No;
        setChanged();
    }
    boolean getRdoEOLOthersIR_No(){
        return this.rdoEOLOthersIR_No;
    }
    
    void setRdoPenalOthersIR_Yes(boolean rdoPenalOthersIR_Yes){
        this.rdoPenalOthersIR_Yes = rdoPenalOthersIR_Yes;
        setChanged();
    }
    boolean getRdoPenalOthersIR_Yes(){
        return this.rdoPenalOthersIR_Yes;
    }
    
    void setRdoPenalOthersIR_No(boolean rdoPenalOthersIR_No){
        this.rdoPenalOthersIR_No = rdoPenalOthersIR_No;
        setChanged();
    }
    boolean getRdoPenalOthersIR_No(){
        return this.rdoPenalOthersIR_No;
    }
    
    void setRdoLimitEIOthersIR_Yes(boolean rdoLimitEIOthersIR_Yes){
        this.rdoLimitEIOthersIR_Yes = rdoLimitEIOthersIR_Yes;
        setChanged();
    }
    boolean getRdoLimitEIOthersIR_Yes(){
        return this.rdoLimitEIOthersIR_Yes;
    }
    
    void setRdoLimitEIOthersIR_No(boolean rdoLimitEIOthersIR_No){
        this.rdoLimitEIOthersIR_No = rdoLimitEIOthersIR_No;
        setChanged();
    }
    boolean getRdoLimitEIOthersIR_No(){
        return this.rdoLimitEIOthersIR_No;
    }
    
    void setTxtPenalIROthersIR(String txtPenalIROthersIR){
        this.txtPenalIROthersIR = txtPenalIROthersIR;
        setChanged();
    }
    String getTxtPenalIROthersIR(){
        return this.txtPenalIROthersIR;
    }
    
    void setRdoIsApplicablePLRIR_Yes(boolean rdoIsApplicablePLRIR_Yes){
        this.rdoIsApplicablePLRIR_Yes = rdoIsApplicablePLRIR_Yes;
        setChanged();
    }
    boolean getRdoIsApplicablePLRIR_Yes(){
        return this.rdoIsApplicablePLRIR_Yes;
    }
    
    void setRdoIsApplicablePLRIR_No(boolean rdoIsApplicablePLRIR_No){
        this.rdoIsApplicablePLRIR_No = rdoIsApplicablePLRIR_No;
        setChanged();
    }
    boolean getRdoIsApplicablePLRIR_No(){
        return this.rdoIsApplicablePLRIR_No;
    }
    
    void setTxtRatePLRIR(String txtRatePLRIR){
        this.txtRatePLRIR = txtRatePLRIR;
        setChanged();
    }
    String getTxtRatePLRIR(){
        return this.txtRatePLRIR;
    }
    
    void setTdtAppliedFromPLRIR(String tdtAppliedFromPLRIR){
        this.tdtAppliedFromPLRIR = tdtAppliedFromPLRIR;
        setChanged();
    }
    String getTdtAppliedFromPLRIR(){
        return this.tdtAppliedFromPLRIR;
    }
    
    void setRdoNewAccountPLRIR_Yes(boolean rdoNewAccountPLRIR_Yes){
        this.rdoNewAccountPLRIR_Yes = rdoNewAccountPLRIR_Yes;
        setChanged();
    }
    boolean getRdoNewAccountPLRIR_Yes(){
        return this.rdoNewAccountPLRIR_Yes;
    }
    
    void setRdoNewAccountPLRIR_No(boolean rdoNewAccountPLRIR_No){
        this.rdoNewAccountPLRIR_No = rdoNewAccountPLRIR_No;
        setChanged();
    }
    boolean getRdoNewAccountPLRIR_No(){
        return this.rdoNewAccountPLRIR_No;
    }
    
    void setRdoExistingAccountPLRIR_Yes(boolean rdoExistingAccountPLRIR_Yes){
        this.rdoExistingAccountPLRIR_Yes = rdoExistingAccountPLRIR_Yes;
        setChanged();
    }
    boolean getRdoExistingAccountPLRIR_Yes(){
        return this.rdoExistingAccountPLRIR_Yes;
    }
    
    void setRdoExistingAccountPLRIR_No(boolean rdoExistingAccountPLRIR_No){
        this.rdoExistingAccountPLRIR_No = rdoExistingAccountPLRIR_No;
        setChanged();
    }
    boolean getRdoExistingAccountPLRIR_No(){
        return this.rdoExistingAccountPLRIR_No;
    }
    
    void setTdtAccountSFPLRIR(String tdtAccountSFPLRIR){
        this.tdtAccountSFPLRIR = tdtAccountSFPLRIR;
        setChanged();
    }
    String getTdtAccountSFPLRIR(){
        return this.tdtAccountSFPLRIR;
    }
    
    void setRdoCreditIntInterestPayable_Yes(boolean rdoCreditIntInterestPayable_Yes){
        this.rdoCreditIntInterestPayable_Yes = rdoCreditIntInterestPayable_Yes;
        setChanged();
    }
    boolean getRdoCreditIntInterestPayable_Yes(){
        return this.rdoCreditIntInterestPayable_Yes;
    }
    
    void setRdoCreditIntInterestPayable_No(boolean rdoCreditIntInterestPayable_No){
        this.rdoCreditIntInterestPayable_No = rdoCreditIntInterestPayable_No;
        setChanged();
    }
    boolean getRdoCreditIntInterestPayable_No(){
        return this.rdoCreditIntInterestPayable_No;
    }
    
    void setRdoCreditCompdInterestPayable_Yes(boolean rdoCreditCompdInterestPayable_Yes){
        this.rdoCreditCompdInterestPayable_Yes = rdoCreditCompdInterestPayable_Yes;
        setChanged();
    }
    boolean getRdoCreditCompdInterestPayable_Yes(){
        return this.rdoCreditCompdInterestPayable_Yes;
    }
    
    void setRdoCreditCompdInterestPayable_No(boolean rdoCreditCompdInterestPayable_No){
        this.rdoCreditCompdInterestPayable_No = rdoCreditCompdInterestPayable_No;
        setChanged();
    }
    boolean getRdoCreditCompdInterestPayable_No(){
        return this.rdoCreditCompdInterestPayable_No;
    }
    
    void setRdoAdditionalIntInterestPayable_Yes(boolean rdoAdditionalIntInterestPayable_Yes){
        this.rdoAdditionalIntInterestPayable_Yes = rdoAdditionalIntInterestPayable_Yes;
        setChanged();
    }
    boolean getRdoAdditionalIntInterestPayable_Yes(){
        return this.rdoAdditionalIntInterestPayable_Yes;
    }
    
    void setRdoAdditionalIntInterestPayable_No(boolean rdoAdditionalIntInterestPayable_No){
        this.rdoAdditionalIntInterestPayable_No = rdoAdditionalIntInterestPayable_No;
        setChanged();
    }
    boolean getRdoAdditionalIntInterestPayable_No(){
        return this.rdoAdditionalIntInterestPayable_No;
    }
    
    void setCboCreditInterestCFIP(String cboCreditInterestCFIP){
        this.cboCreditInterestCFIP = cboCreditInterestCFIP;
        setChanged();
    }
    String getCboCreditInterestCFIP(){
        return this.cboCreditInterestCFIP;
    }
    
    void setCboCreditInterestAFIP(String cboCreditInterestAFIP){
        this.cboCreditInterestAFIP = cboCreditInterestAFIP;
        setChanged();
    }
    String getCboCreditInterestAFIP(){
        return this.cboCreditInterestAFIP;
    }
    
    void setCboCreditInterestCompdFIP(String cboCreditInterestCompdFIP){
        this.cboCreditInterestCompdFIP = cboCreditInterestCompdFIP;
        setChanged();
    }
    String getCboCreditInterestCompdFIP(){
        return this.cboCreditInterestCompdFIP;
    }
    
    void setCboCPROIP(String cboCPROIP){
        this.cboCPROIP = cboCPROIP;
        setChanged();
    }
    String getCboCPROIP(){
        return this.cboCPROIP;
    }
    
    void setCboCIROIP(String cboCIROIP){
        this.cboCIROIP = cboCIROIP;
        setChanged();
    }
    String getCboCIROIP(){
        return this.cboCIROIP;
    }
    
    void setCboCalcCtriteriaIP(String cboCalcCtriteriaIP){
        this.cboCalcCtriteriaIP = cboCalcCtriteriaIP;
        setChanged();
    }
    String getCboCalcCtriteriaIP(){
        return this.cboCalcCtriteriaIP;
    }
    
    void setCboProdFrequencyIP(String cboProdFrequencyIP){
        this.cboProdFrequencyIP = cboProdFrequencyIP;
        setChanged();
    }
    String getCboProdFrequencyIP(){
        return this.cboProdFrequencyIP;
    }
    
    void setTxtCreditInterestRateIP(String txtCreditInterestRateIP){
        this.txtCreditInterestRateIP = txtCreditInterestRateIP;
        setChanged();
    }
    String getTxtCreditInterestRateIP(){
        return this.txtCreditInterestRateIP;
    }
    
    void setTdtLastCDIP(String tdtLastCDIP){
        this.tdtLastCDIP = tdtLastCDIP;
        setChanged();
    }
    String getTdtLastCDIP(){
        return this.tdtLastCDIP;
    }
    
    void setTdtLastADIP(String tdtLastADIP){
        this.tdtLastADIP = tdtLastADIP;
        setChanged();
    }
    String getTdtLastADIP(){
        return this.tdtLastADIP;
    }
    
    void setTxtAddIntRateIP(String txtAddIntRateIP){
        this.txtAddIntRateIP = txtAddIntRateIP;
        setChanged();
    }
    String getTxtAddIntRateIP(){
        return this.txtAddIntRateIP;
    }
    
    void setRdoIsApplicableFCharges_Yes(boolean rdoIsApplicableFCharges_Yes){
        this.rdoIsApplicableFCharges_Yes = rdoIsApplicableFCharges_Yes;
        setChanged();
    }
    boolean getRdoIsApplicableFCharges_Yes(){
        return this.rdoIsApplicableFCharges_Yes;
    }
    
    void setRdoIsApplicableFCharges_No(boolean rdoIsApplicableFCharges_No){
        this.rdoIsApplicableFCharges_No = rdoIsApplicableFCharges_No;
        setChanged();
    }
    boolean getRdoIsApplicableFCharges_No(){
        return this.rdoIsApplicableFCharges_No;
    }
    
    void setTdtLastAppliedFCharges(String tdtLastAppliedFCharges){
        this.tdtLastAppliedFCharges = tdtLastAppliedFCharges;
        setChanged();
    }
    String getTdtLastAppliedFCharges(){
        return this.tdtLastAppliedFCharges;
    }
    
    void setTdtDueDateFCharges(String tdtDueDateFCharges){
        this.tdtDueDateFCharges = tdtDueDateFCharges;
        setChanged();
    }
    String getTdtDueDateFCharges(){
        return this.tdtDueDateFCharges;
    }
    
    void setTxtFolioEntriesFCharges(String txtFolioEntriesFCharges){
        this.txtFolioEntriesFCharges = txtFolioEntriesFCharges;
        setChanged();
    }
    String getTxtFolioEntriesFCharges(){
        return this.txtFolioEntriesFCharges;
    }
    
    void setTxtRateFCharges(String txtRateFCharges){
        this.txtRateFCharges = txtRateFCharges;
        setChanged();
    }
    String getTxtRateFCharges(){
        return this.txtRateFCharges;
    }
    
    void setCboChargeOnTransactionFCharges(String cboChargeOnTransactionFCharges){
        this.cboChargeOnTransactionFCharges = cboChargeOnTransactionFCharges;
        setChanged();
    }
    String getCboChargeOnTransactionFCharges(){
        return this.cboChargeOnTransactionFCharges;
    }
    
    void setCboCAFrequencyFCharges(String cboCAFrequencyFCharges){
        this.cboCAFrequencyFCharges = cboCAFrequencyFCharges;
        setChanged();
    }
    String getCboCAFrequencyFCharges(){
        return this.cboCAFrequencyFCharges;
    }
    
    void setCboCollectChargeFCharges(String cboCollectChargeFCharges){
        this.cboCollectChargeFCharges = cboCollectChargeFCharges;
        setChanged();
    }
    String getCboCollectChargeFCharges(){
        return this.cboCollectChargeFCharges;
    }
    
    void setCboChargeOnDocFCharges(String cboChargeOnDocFCharges){
        this.cboChargeOnDocFCharges = cboChargeOnDocFCharges;
        setChanged();
    }
    String getCboChargeOnDocFCharges(){
        return this.cboChargeOnDocFCharges;
    }
    
    void setCboIRFrequencyFCharges(String cboIRFrequencyFCharges){
        this.cboIRFrequencyFCharges = cboIRFrequencyFCharges;
        setChanged();
    }
    String getCboIRFrequencyFCharges(){
        return this.cboIRFrequencyFCharges;
    }
    
    void setRdoIsStatementCharges_Yes(boolean rdoIsStatementCharges_Yes){
        this.rdoIsStatementCharges_Yes = rdoIsStatementCharges_Yes;
        setChanged();
    }
    boolean getRdoIsStatementCharges_Yes(){
        return this.rdoIsStatementCharges_Yes;
    }
    
    void setRdoIsStatementCharges_No(boolean rdoIsStatementCharges_No){
        this.rdoIsStatementCharges_No = rdoIsStatementCharges_No;
        setChanged();
    }
    boolean getRdoIsStatementCharges_No(){
        return this.rdoIsStatementCharges_No;
    }
    
    void setRdoIsChequebookCharges_Yes(boolean rdoIsChequebookCharges_Yes){
        this.rdoIsChequebookCharges_Yes = rdoIsChequebookCharges_Yes;
        setChanged();
    }
    boolean getRdoIsChequebookCharges_Yes(){
        return this.rdoIsChequebookCharges_Yes;
    }
    
    void setRdoIsChequebookCharges_No(boolean rdoIsChequebookCharges_No){
        this.rdoIsChequebookCharges_No = rdoIsChequebookCharges_No;
        setChanged();
    }
    boolean getRdoIsChequebookCharges_No(){
        return this.rdoIsChequebookCharges_No;
    }
    
    void setRdoIsStopPaymentCharges_Yes(boolean rdoIsStopPaymentCharges_Yes){
        this.rdoIsStopPaymentCharges_Yes = rdoIsStopPaymentCharges_Yes;
        setChanged();
    }
    boolean getRdoIsStopPaymentCharges_Yes(){
        return this.rdoIsStopPaymentCharges_Yes;
    }
    
    void setRdoIsStopPaymentCharges_No(boolean rdoIsStopPaymentCharges_No){
        this.rdoIsStopPaymentCharges_No = rdoIsStopPaymentCharges_No;
        setChanged();
    }
    boolean getRdoIsStopPaymentCharges_No(){
        return this.rdoIsStopPaymentCharges_No;
    }
    
    void setRdoIsProcessingCharges_Yes(boolean rdoIsProcessingCharges_Yes){
        this.rdoIsProcessingCharges_Yes = rdoIsProcessingCharges_Yes;
        setChanged();
    }
    boolean getRdoIsProcessingCharges_Yes(){
        return this.rdoIsProcessingCharges_Yes;
    }
    
    void setRdoIsProcessingCharges_No(boolean rdoIsProcessingCharges_No){
        this.rdoIsProcessingCharges_No = rdoIsProcessingCharges_No;
        setChanged();
    }
    boolean getRdoIsProcessingCharges_No(){
        return this.rdoIsProcessingCharges_No;
    }
    
    void setTxtProcessingCharges(String txtProcessingCharges){
        this.txtProcessingCharges = txtProcessingCharges;
        setChanged();
    }
    String getTxtProcessingCharges(){
        return this.txtProcessingCharges;
    }
    
    void setCboProcessingCharges(String cboProcessingCharges){
        this.cboProcessingCharges = cboProcessingCharges;
        setChanged();
    }
    String getCboProcessingCharges(){
        return this.cboProcessingCharges;
    }
    
    void setRdoIsCommitmentCharges_Yes(boolean rdoIsCommitmentCharges_Yes){
        this.rdoIsCommitmentCharges_Yes = rdoIsCommitmentCharges_Yes;
        setChanged();
    }
    boolean getRdoIsCommitmentCharges_Yes(){
        return this.rdoIsCommitmentCharges_Yes;
    }
    
    void setRdoIsCommitmentCharges_No(boolean rdoIsCommitmentCharges_No){
        this.rdoIsCommitmentCharges_No = rdoIsCommitmentCharges_No;
        setChanged();
    }
    boolean getRdoIsCommitmentCharges_No(){
        return this.rdoIsCommitmentCharges_No;
    }
    
    void setTxtCommitmentCharges(String txtCommitmentCharges){
        this.txtCommitmentCharges = txtCommitmentCharges;
        setChanged();
    }
    String getTxtCommitmentCharges(){
        return this.txtCommitmentCharges;
    }
    
    void setCboCommitmentCharges(String cboCommitmentCharges){
        this.cboCommitmentCharges = cboCommitmentCharges;
        setChanged();
    }
    String getCboCommitmentCharges(){
        return this.cboCommitmentCharges;
    }
    
    void setTxtAccountClosingCharges(String txtAccountClosingCharges){
        this.txtAccountClosingCharges = txtAccountClosingCharges;
        setChanged();
    }
    String getTxtAccountClosingCharges(){
        return this.txtAccountClosingCharges;
    }
    
    void setTxtStatementCharges(String txtStatementCharges){
        this.txtStatementCharges = txtStatementCharges;
        setChanged();
    }
    String getTxtStatementCharges(){
        return this.txtStatementCharges;
    }
    
    void setTxtMiscServiceCharges(String txtMiscServiceCharges){
        this.txtMiscServiceCharges = txtMiscServiceCharges;
        setChanged();
    }
    String getTxtMiscServiceCharges(){
        return this.txtMiscServiceCharges;
    }
    
    void setTxtChequebookCharges(String txtChequebookCharges){
        this.txtChequebookCharges = txtChequebookCharges;
        setChanged();
    }
    String getTxtChequebookCharges(){
        return this.txtChequebookCharges;
    }
    
    void setTxtStopPaymentCharges(String txtStopPaymentCharges){
        this.txtStopPaymentCharges = txtStopPaymentCharges;
        setChanged();
    }
    String getTxtStopPaymentCharges(){
        return this.txtStopPaymentCharges;
    }
    
    void setCboAmountCRIC(String cboAmountCRIC){
        this.cboAmountCRIC = cboAmountCRIC;
        setChanged();
    }
    String getCboAmountCRIC(){
        return this.cboAmountCRIC;
    }
    
    void setTxtRateCRIC(String txtRateCRIC){
        this.txtRateCRIC = txtRateCRIC;
        setChanged();
    }
    String getTxtRateCRIC(){
        return this.txtRateCRIC;
    }
    
    void setCboAmountCROC(String cboAmountCROC){
        this.cboAmountCROC = cboAmountCROC;
        setChanged();
    }
    String getCboAmountCROC(){
        return this.cboAmountCROC;
    }
    
    void setTxtRateCROC(String txtRateCROC){
        this.txtRateCROC = txtRateCROC;
        setChanged();
    }
    String getTxtRateCROC(){
        return this.txtRateCROC;
    }
    
    void setRdoATMCardSI_Yes(boolean rdoATMCardSI_Yes){
        this.rdoATMCardSI_Yes = rdoATMCardSI_Yes;
        setChanged();
    }
    boolean getRdoATMCardSI_Yes(){
        return this.rdoATMCardSI_Yes;
    }
    
    void setRdoATMCardSI_No(boolean rdoATMCardSI_No){
        this.rdoATMCardSI_No = rdoATMCardSI_No;
        setChanged();
    }
    boolean getRdoATMCardSI_No(){
        return this.rdoATMCardSI_No;
    }
    
    void setRdoCreditCardSI_Yes(boolean rdoCreditCardSI_Yes){
        this.rdoCreditCardSI_Yes = rdoCreditCardSI_Yes;
        setChanged();
    }
    boolean getRdoCreditCardSI_Yes(){
        return this.rdoCreditCardSI_Yes;
    }
    
    void setRdoCreditCardSI_No(boolean rdoCreditCardSI_No){
        this.rdoCreditCardSI_No = rdoCreditCardSI_No;
        setChanged();
    }
    boolean getRdoCreditCardSI_No(){
        return this.rdoCreditCardSI_No;
    }
    
    void setRdoDebitCardSI_Yes(boolean rdoDebitCardSI_Yes){
        this.rdoDebitCardSI_Yes = rdoDebitCardSI_Yes;
        setChanged();
    }
    boolean getRdoDebitCardSI_Yes(){
        return this.rdoDebitCardSI_Yes;
    }
    
    void setRdoDebitCardSI_No(boolean rdoDebitCardSI_No){
        this.rdoDebitCardSI_No = rdoDebitCardSI_No;
        setChanged();
    }
    boolean getRdoDebitCardSI_No(){
        return this.rdoDebitCardSI_No;
    }
    
    void setRdoMobileBankingClientSI_Yes(boolean rdoMobileBankingClientSI_Yes){
        this.rdoMobileBankingClientSI_Yes = rdoMobileBankingClientSI_Yes;
        setChanged();
    }
    boolean getRdoMobileBankingClientSI_Yes(){
        return this.rdoMobileBankingClientSI_Yes;
    }
    
    void setRdoMobileBankingClientSI_No(boolean rdoMobileBankingClientSI_No){
        this.rdoMobileBankingClientSI_No = rdoMobileBankingClientSI_No;
        setChanged();
    }
    boolean getRdoMobileBankingClientSI_No(){
        return this.rdoMobileBankingClientSI_No;
    }
    
    void setRdoBranchBankingSI_Yes(boolean rdoBranchBankingSI_Yes){
        this.rdoBranchBankingSI_Yes = rdoBranchBankingSI_Yes;
        setChanged();
    }
    boolean getRdoBranchBankingSI_Yes(){
        return this.rdoBranchBankingSI_Yes;
    }
    
    void setRdoBranchBankingSI_No(boolean rdoBranchBankingSI_No){
        this.rdoBranchBankingSI_No = rdoBranchBankingSI_No;
        setChanged();
    }
    boolean getRdoBranchBankingSI_No(){
        return this.rdoBranchBankingSI_No;
    }
    
    void setTxtRateSI(String txtRateSI){
        this.txtRateSI = txtRateSI;
        setChanged();
    }
    String getTxtRateSI(){
        return this.txtRateSI;
    }
    
    void setCboAssetsSI(String cboAssetsSI){
        this.cboAssetsSI = cboAssetsSI;
        setChanged();
    }
    String getCboAssetsSI(){
        return this.cboAssetsSI;
    }
    
    void setTxtACCAH(String txtACCAH){
        this.txtACCAH = txtACCAH;
        setChanged();
    }
    String getTxtACCAH(){
        return this.txtACCAH;
    }
    
    void setTxtMSCAH(String txtMSCAH){
        this.txtMSCAH = txtMSCAH;
        setChanged();
    }
    String getTxtMSCAH(){
        return this.txtMSCAH;
    }
    
    void setTxtSCAH(String txtSCAH){
        this.txtSCAH = txtSCAH;
        setChanged();
    }
    String getTxtSCAH(){
        return this.txtSCAH;
    }
    
    void setTxtDIAH(String txtDIAH){
        this.txtDIAH = txtDIAH;
        setChanged();
    }
    String getTxtDIAH(){
        return this.txtDIAH;
    }
    
    void setTxtPIAH(String txtPIAH){
        this.txtPIAH = txtPIAH;
        setChanged();
    }
    String getTxtPIAH(){
        return this.txtPIAH;
    }
    
    void setTxtCIAH(String txtCIAH){
        this.txtCIAH = txtCIAH;
        setChanged();
    }
    String getTxtCIAH(){
        return this.txtCIAH;
    }
    
    void setTxtAgCIAH(String txtAgCIAH){
        this.txtAgCIAH = txtAgCIAH;
        setChanged();
    }
    String getTxtAgCIAH(){
        return this.txtAgCIAH;
    }
    
    void setTxtEIAH(String txtEIAH){
        this.txtEIAH = txtEIAH;
        setChanged();
    }
    String getTxtEIAH(){
        return this.txtEIAH;
    }
    
    void setTxtExOLHAH(String txtExOLHAH){
        this.txtExOLHAH = txtExOLHAH;
        setChanged();
    }
    String getTxtExOLHAH(){
        return this.txtExOLHAH;
    }
    
    void setTxtCICAH(String txtCICAH){
        this.txtCICAH = txtCICAH;
        setChanged();
    }
    String getTxtCICAH(){
        return this.txtCICAH;
    }
    
    void setTxtSPCAH(String txtSPCAH){
        this.txtSPCAH = txtSPCAH;
        setChanged();
    }
    String getTxtSPCAH(){
        return this.txtSPCAH;
    }
    
    void setTxtCRCInwardAH(String txtCRCInwardAH){
        this.txtCRCInwardAH = txtCRCInwardAH;
        setChanged();
    }
    String getTxtCRCInwardAH(){
        return this.txtCRCInwardAH;
    }
    
    void setTxtCRCoutwardAH(String txtCRCoutwardAH){
        this.txtCRCoutwardAH = txtCRCoutwardAH;
        setChanged();
    }
    String getTxtCRCoutwardAH(){
        return this.txtCRCoutwardAH;
    }
    
    void setTxtFCAH(String txtFCAH){
        this.txtFCAH = txtFCAH;
        setChanged();
    }
    String getTxtFCAH(){
        return this.txtFCAH;
    }
    
    /** Getter for property tbmCRChargesInward.
     * @return Value of property tbmCRChargesInward.
     *
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTbmCRChargesInward() {
        return tbmCRChargesInward;
    }
    
    /** Setter for property tbmCRChargesInward.
     * @param tbmCRChargesInward New value of property tbmCRChargesInward.
     *
     */
    public void setTbmCRChargesInward(com.see.truetransact.clientutil.EnhancedTableModel tbmCRChargesInward) {
        this.tbmCRChargesInward = tbmCRChargesInward;
        setChanged();
    }
    
    /** Getter for property tbmCRChargesOutward.
     * @return Value of property tbmCRChargesOutward.
     *
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTbmCRChargesOutward() {
        return tbmCRChargesOutward;
    }
    
    /** Setter for property tbmCRChargesOutward.
     * @param tbmCRChargesOutward New value of property tbmCRChargesOutward.
     *
     */
    public void setTbmCRChargesOutward(com.see.truetransact.clientutil.EnhancedTableModel tbmCRChargesOutward) {
        this.tbmCRChargesOutward = tbmCRChargesOutward;
        setChanged();
    }
    
    /** Getter for property tbmMiscItemSI.
     * @return Value of property tbmMiscItemSI.
     *
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTbmMiscItemSI() {
        return tbmMiscItemSI;
    }
    
    /** Setter for property tbmMiscItemSI.
     * @param tbmMiscItemSI New value of property tbmMiscItemSI.
     *
     */
    public void setTbmMiscItemSI(com.see.truetransact.clientutil.EnhancedTableModel tbmMiscItemSI) {
        this.tbmMiscItemSI = tbmMiscItemSI;
        setChanged();
    }
    
    void setCbmStmtFrequency(com.see.truetransact.clientutil.ComboBoxModel cbmStmtFrequency){
        this.cbmStmtFrequency = cbmStmtFrequency;
        setChanged();
    }
    
    ComboBoxModel getCbmStmtFrequency(){
        return this.cbmStmtFrequency;
    }
    
    /** Getter for property cbmBehavesLike.
     * @return Value of property cbmBehavesLike.
     *
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmBehavesLike() {
        return cbmBehavesLike;
    }
    
    /** Setter for property cbmBehavesLike.
     * @param cbmBehavesLike New value of property cbmBehavesLike.
     *
     */
    public void setCbmBehavesLike(com.see.truetransact.clientutil.ComboBoxModel cbmBehavesLike) {
        this.cbmBehavesLike = cbmBehavesLike;
    }
    
    /** Getter for property cbmCLPeriod.
     * @return Value of property cbmCLPeriod.
     *
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmCLPeriod() {
        return cbmCLPeriod;
    }
    
    /** Setter for property cbmCLPeriod.
     * @param cbmCLPeriod New value of property cbmCLPeriod.
     *
     */
    public void setCbmCLPeriod(com.see.truetransact.clientutil.ComboBoxModel cbmCLPeriod) {
        this.cbmCLPeriod = cbmCLPeriod;
    }
    
    /** Getter for property cbmDIApplicationFIR.
     * @return Value of property cbmDIApplicationFIR.
     *
     */
    public ComboBoxModel getCbmDIApplicationFIR() {
        return cbmDIApplicationFIR;
    }
    
    /** Setter for property cbmDIApplicationFIR.
     * @param cbmDIApplicationFIR New value of property cbmDIApplicationFIR.
     *
     */
    public void setCbmDIApplicationFIR(ComboBoxModel cbmDIApplicationFIR) {
        this.cbmDIApplicationFIR = cbmDIApplicationFIR;
    }
    
    /** Getter for property cbmDICalculationFIR.
     * @return Value of property cbmDICalculationFIR.
     *
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmDICalculationFIR() {
        return cbmDICalculationFIR;
    }
    
    /** Setter for property cbmDICalculationFIR.
     * @param cbmDICalculationFIR New value of property cbmDICalculationFIR.
     *
     */
    public void setCbmDICalculationFIR(com.see.truetransact.clientutil.ComboBoxModel cbmDICalculationFIR) {
        this.cbmDICalculationFIR = cbmDICalculationFIR;
    }
    
    /** Getter for property cbmDICompoundFIR.
     * @return Value of property cbmDICompoundFIR.
     *
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmDICompoundFIR() {
        return cbmDICompoundFIR;
    }
    
    /** Setter for property cbmDICompoundFIR.
     * @param cbmDICompoundFIR New value of property cbmDICompoundFIR.
     *
     */
    public void setCbmDICompoundFIR(com.see.truetransact.clientutil.ComboBoxModel cbmDICompoundFIR) {
        this.cbmDICompoundFIR = cbmDICompoundFIR;
    }
    
    /** Getter for property cbmProductFOthersIR.
     * @return Value of property cbmProductFOthersIR.
     *
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProductFOthersIR() {
        return cbmProductFOthersIR;
    }
    
    /** Setter for property cbmProductFOthersIR.
     * @param cbmProductFOthersIR New value of property cbmProductFOthersIR.
     *
     */
    public void setCbmProductFOthersIR(com.see.truetransact.clientutil.ComboBoxModel cbmProductFOthersIR) {
        this.cbmProductFOthersIR = cbmProductFOthersIR;
    }
    
    /** Getter for property cbmDPRoundOffIR.
     * @return Value of property cbmDPRoundOffIR.
     *
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmDPRoundOffIR() {
        return cbmDPRoundOffIR;
    }
    
    /** Setter for property cbmDPRoundOffIR.
     * @param cbmDPRoundOffIR New value of property cbmDPRoundOffIR.
     *
     */
    public void setCbmDPRoundOffIR(com.see.truetransact.clientutil.ComboBoxModel cbmDPRoundOffIR) {
        this.cbmDPRoundOffIR = cbmDPRoundOffIR;
    }
    
    /** Getter for property cbmDIRoundOffIR.
     * @return Value of property cbmDIRoundOffIR.
     *
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmDIRoundOffIR() {
        return cbmDIRoundOffIR;
    }
    
    /** Setter for property cbmDIRoundOffIR.
     * @param cbmDIRoundOffIR New value of property cbmDIRoundOffIR.
     *
     */
    public void setCbmDIRoundOffIR(com.see.truetransact.clientutil.ComboBoxModel cbmDIRoundOffIR) {
        this.cbmDIRoundOffIR = cbmDIRoundOffIR;
    }
    
    /** Getter for property cbmCreditInterestAFIP.
     * @return Value of property cbmCreditInterestAFIP.
     *
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmCreditInterestAFIP() {
        return cbmCreditInterestAFIP;
    }
    
    /** Setter for property cbmCreditInterestAFIP.
     * @param cbmCreditInterestAFIP New value of property cbmCreditInterestAFIP.
     *
     */
    public void setCbmCreditInterestAFIP(com.see.truetransact.clientutil.ComboBoxModel cbmCreditInterestAFIP) {
        this.cbmCreditInterestAFIP = cbmCreditInterestAFIP;
    }
    
    /** Getter for property cbmCreditInterestCFIP.
     * @return Value of property cbmCreditInterestCFIP.
     *
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmCreditInterestCFIP() {
        return cbmCreditInterestCFIP;
    }
    
    /** Setter for property cbmCreditInterestCFIP.
     * @param cbmCreditInterestCFIP New value of property cbmCreditInterestCFIP.
     *
     */
    public void setCbmCreditInterestCFIP(com.see.truetransact.clientutil.ComboBoxModel cbmCreditInterestCFIP) {
        this.cbmCreditInterestCFIP = cbmCreditInterestCFIP;
    }
    
    /** Getter for property cbmCreditInterestCompdFIP.
     * @return Value of property cbmCreditInterestCompdFIP.
     *
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmCreditInterestCompdFIP() {
        return cbmCreditInterestCompdFIP;
    }
    
    /** Setter for property cbmCreditInterestCompdFIP.
     * @param cbmCreditInterestCompdFIP New value of property cbmCreditInterestCompdFIP.
     *
     */
    public void setCbmCreditInterestCompdFIP(com.see.truetransact.clientutil.ComboBoxModel cbmCreditInterestCompdFIP) {
        this.cbmCreditInterestCompdFIP = cbmCreditInterestCompdFIP;
    }
    
    /** Getter for property cbmProdFrequencyIP.
     * @return Value of property cbmProdFrequencyIP.
     *
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProdFrequencyIP() {
        return cbmProdFrequencyIP;
    }
    
    /** Setter for property cbmProdFrequencyIP.
     * @param cbmProdFrequencyIP New value of property cbmProdFrequencyIP.
     *
     */
    public void setCbmProdFrequencyIP(com.see.truetransact.clientutil.ComboBoxModel cbmProdFrequencyIP) {
        this.cbmProdFrequencyIP = cbmProdFrequencyIP;
    }
    
    /** Getter for property cbmIRFrequencyFCharges.
     * @return Value of property cbmIRFrequencyFCharges.
     *
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmIRFrequencyFCharges() {
        return cbmIRFrequencyFCharges;
    }
    
    /** Setter for property cbmIRFrequencyFCharges.
     * @param cbmIRFrequencyFCharges New value of property cbmIRFrequencyFCharges.
     *
     */
    public void setCbmIRFrequencyFCharges(com.see.truetransact.clientutil.ComboBoxModel cbmIRFrequencyFCharges) {
        this.cbmIRFrequencyFCharges = cbmIRFrequencyFCharges;
    }
    
    /** Getter for property cbmCAFrequencyFCharges.
     * @return Value of property cbmCAFrequencyFCharges.
     *
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmCAFrequencyFCharges() {
        return cbmCAFrequencyFCharges;
    }
    
    /** Setter for property cbmCAFrequencyFCharges.
     * @param cbmCAFrequencyFCharges New value of property cbmCAFrequencyFCharges.
     *
     */
    public void setCbmCAFrequencyFCharges(com.see.truetransact.clientutil.ComboBoxModel cbmCAFrequencyFCharges) {
        this.cbmCAFrequencyFCharges = cbmCAFrequencyFCharges;
    }
    
    /** Getter for property cbmCPROIP.
     * @return Value of property cbmCPROIP.
     *
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmCPROIP() {
        return cbmCPROIP;
    }
    
    /** Setter for property cbmCPROIP.
     * @param cbmCPROIP New value of property cbmCPROIP.
     *
     */
    public void setCbmCPROIP(com.see.truetransact.clientutil.ComboBoxModel cbmCPROIP) {
        this.cbmCPROIP = cbmCPROIP;
    }
    
    /** Getter for property cbmCIROIP.
     * @return Value of property cbmCIROIP.
     *
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmCIROIP() {
        return cbmCIROIP;
    }
    
    /** Setter for property cbmCIROIP.
     * @param cbmCIROIP New value of property cbmCIROIP.
     *
     */
    public void setCbmCIROIP(com.see.truetransact.clientutil.ComboBoxModel cbmCIROIP) {
        this.cbmCIROIP = cbmCIROIP;
    }
    
    /** Getter for property cbmCalcCtriteriaIP.
     * @return Value of property cbmCalcCtriteriaIP.
     *
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmCalcCtriteriaIP() {
        return cbmCalcCtriteriaIP;
    }
    
    /** Setter for property cbmCalcCtriteriaIP.
     * @param cbmCalcCtriteriaIP New value of property cbmCalcCtriteriaIP.
     *
     */
    public void setCbmCalcCtriteriaIP(com.see.truetransact.clientutil.ComboBoxModel cbmCalcCtriteriaIP) {
        this.cbmCalcCtriteriaIP = cbmCalcCtriteriaIP;
    }
    
    /** Getter for property cbmCollectChargeFCharges.
     * @return Value of property cbmCollectChargeFCharges.
     *
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmCollectChargeFCharges() {
        return cbmCollectChargeFCharges;
    }
    
    /** Setter for property cbmCollectChargeFCharges.
     * @param cbmCollectChargeFCharges New value of property cbmCollectChargeFCharges.
     *
     */
    public void setCbmCollectChargeFCharges(com.see.truetransact.clientutil.ComboBoxModel cbmCollectChargeFCharges) {
        this.cbmCollectChargeFCharges = cbmCollectChargeFCharges;
    }
    
    /** Getter for property cbmAmountCRIC.
     * @return Value of property cbmAmountCRIC.
     *
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmAmountCRIC() {
        return cbmAmountCRIC;
    }
    
    /** Setter for property cbmAmountCRIC.
     * @param cbmAmountCRIC New value of property cbmAmountCRIC.
     *
     */
    public void setCbmAmountCRIC(com.see.truetransact.clientutil.ComboBoxModel cbmAmountCRIC) {
        this.cbmAmountCRIC = cbmAmountCRIC;
    }
    
    /** Getter for property cbmAmountCROC.
     * @return Value of property cbmAmountCROC.
     *
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmAmountCROC() {
        return cbmAmountCROC;
    }
    
    /** Setter for property cbmAmountCROC.
     * @param cbmAmountCROC New value of property cbmAmountCROC.
     *
     */
    public void setCbmAmountCROC(com.see.truetransact.clientutil.ComboBoxModel cbmAmountCROC) {
        this.cbmAmountCROC = cbmAmountCROC;
    }
    
    /** Getter for property cbmAssetsSI.
     * @return Value of property cbmAssetsSI.
     *
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmAssetsSI() {
        return cbmAssetsSI;
    }
    
    /** Setter for property cbmAssetsSI.
     * @param cbmAssetsSI New value of property cbmAssetsSI.
     *
     */
    public void setCbmAssetsSI(com.see.truetransact.clientutil.ComboBoxModel cbmAssetsSI) {
        this.cbmAssetsSI = cbmAssetsSI;
    }
    
    /** Getter for property cbmChargeOnTransactionFCharges.
     * @return Value of property cbmChargeOnTransactionFCharges.
     *
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmChargeOnTransactionFCharges() {
        return cbmChargeOnTransactionFCharges;
    }
    
    /** Setter for property cbmChargeOnTransactionFCharges.
     * @param cbmChargeOnTransactionFCharges New value of property cbmChargeOnTransactionFCharges.
     *
     */
    public void setCbmChargeOnTransactionFCharges(com.see.truetransact.clientutil.ComboBoxModel cbmChargeOnTransactionFCharges) {
        this.cbmChargeOnTransactionFCharges = cbmChargeOnTransactionFCharges;
    }
    
    /** Getter for property cbmChargeOnDocFCharges.
     * @return Value of property cbmChargeOnDocFCharges.
     *
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmChargeOnDocFCharges() {
        return cbmChargeOnDocFCharges;
    }
    
    /** Setter for property cbmChargeOnDocFCharges.
     * @param cbmChargeOnDocFCharges New value of property cbmChargeOnDocFCharges.
     *
     */
    public void setCbmChargeOnDocFCharges(com.see.truetransact.clientutil.ComboBoxModel cbmChargeOnDocFCharges) {
        this.cbmChargeOnDocFCharges = cbmChargeOnDocFCharges;
    }
    
    /** Getter for property cbmProcessingCharges.
     * @return Value of property cbmProcessingCharges.
     *
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProcessingCharges() {
        return cbmProcessingCharges;
    }
    
    /** Setter for property cbmProcessingCharges.
     * @param cbmProcessingCharges New value of property cbmProcessingCharges.
     *
     */
    public void setCbmProcessingCharges(com.see.truetransact.clientutil.ComboBoxModel cbmProcessingCharges) {
        this.cbmProcessingCharges = cbmProcessingCharges;
    }
    
    /** Getter for property cbmCommitmentCharges.
     * @return Value of property cbmCommitmentCharges.
     *
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmCommitmentCharges() {
        return cbmCommitmentCharges;
    }
    
    /** Setter for property cbmCommitmentCharges.
     * @param cbmCommitmentCharges New value of property cbmCommitmentCharges.
     *
     */
    public void setCbmCommitmentCharges(com.see.truetransact.clientutil.ComboBoxModel cbmCommitmentCharges) {
        this.cbmCommitmentCharges = cbmCommitmentCharges;
    }
    
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    private void fillDropdown() throws Exception{
        try{
            //System.out.println("Inside FillDropDown");
            //log.info("Inside FillDropDown");
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME,null);
            final ArrayList lookup_keys = new ArrayList();
            lookup_keys.add("FREQUENCY");
            lookup_keys.add("ADVANCESPRODUCT.OPERATESLIKE");
            lookup_keys.add("PERIOD");
            lookup_keys.add("OPERATIVEACCTPRODUCT.INTROUNDOFF");
            lookup_keys.add("OPERATIVEACCTPRODUCT.ROUNDOFF");
            lookup_keys.add("OPERATIVEACCTPRODUCT.CALCCRITERIA");
            lookup_keys.add("OPERATIVEACCTPRODUCT.FOLIOCHRG");
            lookup_keys.add("LOANPRODUCT.AMOUNT");
            lookup_keys.add("ADVANCESPRODUCT.ASSETCAT");
            lookup_keys.add("ADVANCESPRODUCT.CHARGEONTR");
            lookup_keys.add("ADVANCESPRODUCT.CHARGEONDOC");
            lookup_keys.add("ADVANCESPRODUCT.CHARGETYPE");
            lookup_keys.add("FOREX.CURRENCY");
            
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            keyValue = com.see.truetransact.clientutil.ClientUtil.populateLookupData(lookUpHash);
            
            getKeyValue((HashMap)keyValue.get("FREQUENCY"));
            System.out.println((HashMap)keyValue.get("FREQUENCY"));
            cbmStmtFrequency = new ComboBoxModel(key,value);
            cbmDICalculationFIR = new ComboBoxModel(key,value);
            cbmDIApplicationFIR= new ComboBoxModel(key,value);
            cbmDICompoundFIR= new ComboBoxModel(key,value);
            cbmProductFOthersIR = new ComboBoxModel(key,value);
            cbmCreditInterestCFIP= new ComboBoxModel(key,value);
            cbmCreditInterestAFIP= new ComboBoxModel(key,value);
            cbmCreditInterestCompdFIP= new ComboBoxModel(key,value);
            cbmProdFrequencyIP= new ComboBoxModel(key,value);
            cbmCAFrequencyFCharges= new ComboBoxModel(key,value);
            cbmIRFrequencyFCharges= new ComboBoxModel(key,value);
            
            getKeyValue((HashMap)keyValue.get("PERIOD"));
            cbmCLPeriod = new ComboBoxModel(key,value);
            
            System.out.println((HashMap)keyValue.get("ADVANCESPRODUCT.OPERATESLIKE"));
            getKeyValue((HashMap)keyValue.get("ADVANCESPRODUCT.OPERATESLIKE"));
            cbmBehavesLike =new ComboBoxModel(key,value);
            
            getKeyValue((HashMap)keyValue.get("OPERATIVEACCTPRODUCT.ROUNDOFF"));
            cbmDPRoundOffIR = new ComboBoxModel(key,value);
            cbmCPROIP = new ComboBoxModel(key,value);
            
            getKeyValue((HashMap)keyValue.get("OPERATIVEACCTPRODUCT.INTROUNDOFF"));
            cbmDIRoundOffIR = new ComboBoxModel(key,value);
            cbmCIROIP = new ComboBoxModel(key,value);
            
            getKeyValue((HashMap)keyValue.get("OPERATIVEACCTPRODUCT.CALCCRITERIA"));
            cbmCalcCtriteriaIP = new ComboBoxModel(key,value);
            
            getKeyValue((HashMap)keyValue.get("OPERATIVEACCTPRODUCT.FOLIOCHRG"));
            cbmCollectChargeFCharges = new ComboBoxModel(key,value);
            
            getKeyValue((HashMap)keyValue.get("LOANPRODUCT.AMOUNT"));
            cbmAmountCRIC = new ComboBoxModel(key, value);
            cbmAmountCROC = new ComboBoxModel(key, value);
            
            getKeyValue((HashMap)keyValue.get("ADVANCESPRODUCT.ASSETCAT"));
            cbmAssetsSI = new ComboBoxModel(key,value);
            
            getKeyValue((HashMap)keyValue.get("ADVANCESPRODUCT.CHARGEONTR"));
            cbmChargeOnTransactionFCharges = new ComboBoxModel(key,value);
            
            getKeyValue((HashMap)keyValue.get("ADVANCESPRODUCT.CHARGEONDOC"));
            cbmChargeOnDocFCharges = new ComboBoxModel(key,value);
            
            getKeyValue((HashMap)keyValue.get("ADVANCESPRODUCT.CHARGETYPE"));
            cbmProcessingCharges = new ComboBoxModel(key,value);
            cbmCommitmentCharges = new ComboBoxModel(key,value);
            
            //        getKeyValue((HashMap)keyValue.get("FOREX.CURRENCY"));
            //        cbmProdCurrency = new ComboBoxModel(key,value);
        }catch(NullPointerException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public ComboBoxModel getCbmProdCurrency(){
        return this.cbmProdCurrency;
    }
    
    //Setting UI components with the AdvancesProduct Transfer Object.
    //        private void setAdvancesProductTO(AdvancesProductTO apTO){
    //            setTxtProductIdAccount(apTO.getProdId());
    //            setTxtProductDescAccount(apTO.getProdDesc());
    //            setTxtAccountHeadAccount(apTO.getAcctHead());
    //            setCboBehavesLike(apTO.getBehavesLike());
    ////            setCboProdCurrency(apTO.getBaseCurrency());
    //        }
    //__ Changed to LoanProductAccountTO...
    private void setAdvancesProductTO(LoanProductAccountTO objLoanProductAccountTO) {
        log.info("In setAdvancesProductTO...");
        
        setTxtProductIdAccount(CommonUtil.convertObjToStr(objLoanProductAccountTO.getProdId()));
        setTxtProductDescAccount(CommonUtil.convertObjToStr(objLoanProductAccountTO.getProdDesc()));
        setCboBehavesLike((String) getCbmBehavesLike().getDataForKey(CommonUtil.convertObjToStr(objLoanProductAccountTO.getBehavesLike())));
        //        setCboProdCurrency((String) getCbmProdCurrency().getDataForKey(CommonUtil.convertObjToStr(objLoanProductAccountTO.getBaseCurrency())));
        setTxtAccountHeadAccount(CommonUtil.convertObjToStr(objLoanProductAccountTO.getAcctHead()));
    }
    
    private void setAgriAdvancesProductTO(AgriLoanProductAccountTO objLoanProductAccountTO) {
        log.info("In setAdvancesProductTO...");
        
        setTxtProductIdAccount(CommonUtil.convertObjToStr(objLoanProductAccountTO.getProdId()));
        setTxtProductDescAccount(CommonUtil.convertObjToStr(objLoanProductAccountTO.getProdDesc()));
        setCboBehavesLike((String) getCbmBehavesLike().getDataForKey(CommonUtil.convertObjToStr(objLoanProductAccountTO.getBehavesLike())));
        //        setCboProdCurrency((String) getCbmProdCurrency().getDataForKey(CommonUtil.convertObjToStr(objLoanProductAccountTO.getBaseCurrency())));
        setTxtAccountHeadAccount(CommonUtil.convertObjToStr(objLoanProductAccountTO.getAcctHead()));
    }
    //        private AdvancesProductTO getAdvancesProductTO(String command){
    //          AdvancesProductTO apTO = new AdvancesProductTO();
    //          try{
    //            apTO.setCommand(command);
    //            apTO.setProdId(getTxtProductIdAccount());
    //            apTO.setProdDesc(getTxtProductDescAccount());
    //            apTO.setAcctHead(getTxtAccountHeadAccount());
    //            apTO.setBehavesLike(getCboBehavesLike());
    ////            apTO.setBaseCurrency(getCboProdCurrency());
    //            apTO.setBaseCurrency(CURRENCY);
    //          }catch(Exception e){
    //               e.printStackTrace();
    //           }
    //
    //            return apTO;
    //
    //        }
    
    public LoanProductAccountTO getAdvancesProductTO() {
        log.info("In getAdvancesProductTO...");
        
        final LoanProductAccountTO objLoanProductAccountTO = new LoanProductAccountTO();
        try{
            objLoanProductAccountTO.setProdId(CommonUtil.convertObjToStr(getTxtProductIdAccount()));
            objLoanProductAccountTO.setProdDesc(CommonUtil.convertObjToStr(getTxtProductDescAccount()));
            objLoanProductAccountTO.setAcctHead(CommonUtil.convertObjToStr(getTxtAccountHeadAccount()));
            objLoanProductAccountTO.setBehavesLike(CommonUtil.convertObjToStr(cbmBehavesLike.getKeyForSelected()));
            objLoanProductAccountTO.setBaseCurrency(CURRENCY);
            objLoanProductAccountTO.setAuthorizeStatus("");
        }catch(Exception e){
            log.info("Error in getAdvancesProductTO()");
            parseException.logException(e,true);
            //e.printStackTrace();
        }
        return objLoanProductAccountTO;
    }
    
    private void setAdvancesProductAccHeadTO(AdvancesProductAccHeadTO apAHTO){
        try{
            setTxtACCAH(CommonUtil.convertObjToStr(apAHTO.getAcClosingChrg()));
            setTxtMSCAH(CommonUtil.convertObjToStr(apAHTO.getMiscServChrg()));
            setTxtSCAH(CommonUtil.convertObjToStr(apAHTO.getStatChrg()));
            setTxtDIAH(CommonUtil.convertObjToStr(apAHTO.getAcDebitInt()));
            
            setTxtPIAH(CommonUtil.convertObjToStr(apAHTO.getPenalInt()));
            setTxtCIAH(CommonUtil.convertObjToStr(apAHTO.getAcCreditInt()));
            
            setTxtAgCIAH(CommonUtil.convertObjToStr(apAHTO.getClearingInt()));
            setTxtEIAH(CommonUtil.convertObjToStr(apAHTO.getExpiryInt()));
            setTxtExOLHAH(CommonUtil.convertObjToStr(apAHTO.getExcessLimit()));
            setTxtCICAH(CommonUtil.convertObjToStr(apAHTO.getChqbkIssueChrg()));
            setTxtSPCAH(CommonUtil.convertObjToStr(apAHTO.getStopPayChrg()));
            setTxtCRCoutwardAH(CommonUtil.convertObjToStr(apAHTO.getChqRetChrgOutward()));
            setTxtCRCInwardAH(CommonUtil.convertObjToStr(apAHTO.getChqRetChrgInward()));
            setTxtFCAH(CommonUtil.convertObjToStr(apAHTO.getFolioChrgAc()));
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    private AdvancesProductAccHeadTO getAdvancesProductAccHeadTO(){
        AdvancesProductAccHeadTO apAHTO = new AdvancesProductAccHeadTO();
        try{
            apAHTO.setProdId(getTxtProductIdAccount());
            apAHTO.setAcClosingChrg(getTxtACCAH());
            apAHTO.setMiscServChrg(getTxtMSCAH());
            apAHTO.setStatChrg(getTxtSCAH());
            apAHTO.setAcDebitInt(getTxtDIAH());
            
            apAHTO.setPenalInt(getTxtPIAH());
            apAHTO.setAcCreditInt(getTxtCIAH());
            
            apAHTO.setClearingInt(getTxtAgCIAH());
            apAHTO.setExpiryInt(getTxtEIAH());
            apAHTO.setExcessLimit(getTxtExOLHAH());
            apAHTO.setChqbkIssueChrg(getTxtCICAH());
            apAHTO.setStopPayChrg(getTxtSPCAH());
            apAHTO.setChqRetChrgOutward(getTxtCRCoutwardAH());
            apAHTO.setChqRetChrgInward(getTxtCRCInwardAH());
            apAHTO.setFolioChrgAc(getTxtFCAH());
        }catch(Exception e){
            e.printStackTrace();
        }
        return apAHTO;
        
    }
    
    private void setAdvancesProductAccParameterTO(AdvancesProductAccParameterTO apAPTO){
        try{
            if (CommonUtil.convertObjToStr(apAPTO.getChkAllowed()).equals(YES)){
                setRdoACAAccount_Yes(true);
            }else {
                setRdoACAAccount_No(true);
            }
            setTxtManagerDistAccount(CommonUtil.convertObjToStr(apAPTO.getDiscEolPerc()));
            setTxtNumberpatternAccount(CommonUtil.convertObjToStr(apAPTO.getNumberPattern()));
            setTxtLastAccNoAccount(CommonUtil.convertObjToStr(apAPTO.getLastAcNo()));
            //            setCboStmtFrequency(getFrequencyLabel(apAPTO.getStatFreq()));
            setCboStmtFrequency((String) getCbmStmtFrequency().getDataForKey(CommonUtil.convertObjToStr(apAPTO.getStatFreq())));
            setTxtFreeCLAccount(CommonUtil.convertObjToStr(apAPTO.getNoFreeChqLeaves()));
            
            //            int value = CommonUtil.convertObjToInt(apAPTO.getFreeChqPeriod());
            //
            //            if((value/365 > 0 ) && (value%365 == 0)){
            //                setTxtCLPeriodAccount(new Integer(value/365).toString());
            //                setCboCLPeriod("Years");
            //            }else if((value/30 > 0 ) && (value%30 == 0)){
            //                setTxtCLPeriodAccount(new Integer(value/30).toString());
            //                setCboCLPeriod("Months");
            //            }else{
            //                setTxtCLPeriodAccount(new Integer(value).toString());
            //                setCboCLPeriod("Days");
            //            }
            
            resultValue= CommonUtil.convertObjToInt(apAPTO.getFreeChqPeriod());
            String period = setPeriod(resultValue);
            setCboCLPeriod(period);
            setTxtCLPeriodAccount(String.valueOf(resultValue));
            resetPeriod();
            
            setTdtCLStartAccount(DateUtil.getStringDate(apAPTO.getFreeChqStart()));
            
            if(CommonUtil.convertObjToStr(apAPTO.getLimitDefAllowed()).equals(YES)){
                setRdoLDAccount_Yes(true);
            }else{
                setRdoLDAccount_No(true);
            }
            
            if(CommonUtil.convertObjToStr(apAPTO.getTempOdAllowed()).equals(YES)){
                setRdoODALAccount_Yes(true);
            }else{
                setRdoODALAccount_No(true);
            }
            
            if(CommonUtil.convertObjToStr(apAPTO.getStaffAcOpened()).equals(YES)){
                setRdoSAOAccount_Yes(true);
            }else{
                setRdoSAOAccount_No(true);
            }
            
            if(CommonUtil.convertObjToStr(apAPTO.getDebitUnclearAppl()).equals(YES)){
                setRdoDIAUEAccount_Yes(true);
            }else{
                setRdoDIAUEAccount_No(true);
            }
            
            if(CommonUtil.convertObjToStr(apAPTO.getCreditUnclearAppl()).equals(YES)){
                setRdoCIUEAccount_Yes(true);
            }else{
                setRdoCIUEAccount_No(true);
            }
            
            if(CommonUtil.convertObjToStr(apAPTO.getIssueToken()).equals(YES)){
                setRdoTokanAccount_Yes(true);
            }else{
                setRdoTokanAccount_No(true);
            }
            
            if(CommonUtil.convertObjToStr(apAPTO.getAllowWdSlip()).equals(YES)){
                setRdoWSAccount_Yes(true);
            }else{
                setRdoWSAccount_No(true);
            }
            setTxtMaxAmountOnWS(CommonUtil.convertObjToStr(apAPTO.getMaxAmtAllowed()));
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private AdvancesProductAccParameterTO getAdvancesProductAccParameterTO(){
        AdvancesProductAccParameterTO apAPTO = new AdvancesProductAccParameterTO();
        try{
            apAPTO.setProdId(getTxtProductIdAccount());
            
            if (getRdoACAAccount_Yes() == true){
                apAPTO.setChkAllowed(YES);
                
            }else {
                apAPTO.setChkAllowed(NO);
                //setRdoACAAccount_No(true);
            }
            
            apAPTO.setDiscEolPerc(CommonUtil.convertObjToDouble(getTxtManagerDistAccount()));
            apAPTO.setNumberPattern(getTxtNumberpatternAccount());
            apAPTO.setLastAcNo(getTxtLastAccNoAccount());
            //            apAPTO.setStatFreq(getFrequencyValue(getCboStmtFrequency()));
            apAPTO.setStatFreq(CommonUtil.convertObjToDouble(cbmStmtFrequency.getKeyForSelected()));
            apAPTO.setNoFreeChqLeaves(CommonUtil.convertObjToDouble(getTxtFreeCLAccount()));
            
            //            int time = (Double.valueOf(getTxtCLPeriodAccount())).intValue();
            //            String timeUnit = getCboCLPeriod();
            //            if(timeUnit.equals("Years")){
            //                apAPTO.setFreeChqPeriod(new Double(time * 365));
            //            }else if(timeUnit.equals("Months")){
            //                apAPTO.setFreeChqPeriod(new Double(time * 30));
            //            }else{
            //                apAPTO.setFreeChqPeriod(new Double(time * 1));
            //            }
            if(cboCLPeriod.length() > 0){
                duration = ((String)cbmCLPeriod.getKeyForSelected());
                periodData = setCombo(duration);
                resultData = periodData * (Double.parseDouble(txtCLPeriodAccount));
                apAPTO.setFreeChqPeriod(CommonUtil.convertObjToDouble(String.valueOf(resultData)));
            }
            
            Date TdDt = DateUtil.getDateMMDDYYYY(getTdtCLStartAccount());
            if(TdDt != null){
                Date tdDate = (Date) curDate.clone();
                tdDate.setDate(TdDt.getDate());
                tdDate.setMonth(TdDt.getMonth());
                tdDate.setYear(TdDt.getYear());
                //            apAPTO.setFreeChqStart(DateUtil.getDateMMDDYYYY(getTdtCLStartAccount()));
                apAPTO.setFreeChqStart(tdDate);
            }else{
                apAPTO.setFreeChqStart(DateUtil.getDateMMDDYYYY(getTdtCLStartAccount()));
            }
            
            if(getRdoLDAccount_Yes() == true){
                apAPTO.setLimitDefAllowed(YES);
            }else{
                apAPTO.setLimitDefAllowed(NO);
            }
            
            if(getRdoODALAccount_Yes()==true){
                apAPTO.setTempOdAllowed(YES);
            }else{
                apAPTO.setTempOdAllowed(NO);
            }
            
            if(getRdoSAOAccount_Yes()==true){
                apAPTO.setStaffAcOpened(YES);
            }else{
                apAPTO.setStaffAcOpened(NO);
            }
            
            if(getRdoDIAUEAccount_Yes()==true){
                apAPTO.setDebitUnclearAppl(YES);
            }else{
                apAPTO.setDebitUnclearAppl(NO);
            }
            
            if(getRdoCIUEAccount_Yes()==true){
                apAPTO.setCreditUnclearAppl(YES);
            }else{
                apAPTO.setCreditUnclearAppl(NO);;
            }
            
            if(getRdoTokanAccount_Yes()==true){
                apAPTO.setIssueToken(YES);
            }else{
                apAPTO.setIssueToken(NO);
            }
            
            if(getRdoWSAccount_Yes() == true){
                apAPTO.setAllowWdSlip(YES);
            }else{
                apAPTO.setAllowWdSlip(NO);
            }
            apAPTO.setMaxAmtAllowed(CommonUtil.convertObjToDouble(getTxtMaxAmountOnWS()));
            
        }catch(Exception e){
            e.printStackTrace();
        }
        return apAPTO;
    }
    
    private void setAdvancesProductChargesTO(AdvancesProductChargesTO apCTO){
        
        try{
            setTxtAccountClosingCharges(CommonUtil.convertObjToStr(apCTO.getAcClosingChrg()));
            setTxtMiscServiceCharges(CommonUtil.convertObjToStr(apCTO.getMiscServChrg()));
            
            if(CommonUtil.convertObjToStr(apCTO.getStatChrg()).equals(YES)){
                setRdoIsStatementCharges_Yes(true);
            }else{
                setRdoIsStatementCharges_No(true);
            }
            setTxtStatementCharges(CommonUtil.convertObjToStr(apCTO.getStatChrgRate()));
            
            if(CommonUtil.convertObjToStr(apCTO.getChqbkIssuedChrg()).equals(YES)){
                setRdoIsChequebookCharges_Yes(true);
            }else{
                setRdoIsChequebookCharges_No(true);
            }
            setTxtChequebookCharges(CommonUtil.convertObjToStr(apCTO.getChqbkIssuedChrgPer()));
            
            if(CommonUtil.convertObjToStr(apCTO.getStopPayChrg()).equals(YES)){
                setRdoIsStopPaymentCharges_Yes(true);
            }else{
                setRdoIsStopPaymentCharges_No(true);
            }
            setTxtStopPaymentCharges(CommonUtil.convertObjToStr(apCTO.getStopPayChrgRate()));
            
            if(CommonUtil.convertObjToStr(apCTO.getFolioChrgAppl()).equals(YES)){
                setRdoIsApplicableFCharges_Yes(true);
            }else{
                setRdoIsApplicableFCharges_No(true);
            }
            
            setTdtLastAppliedFCharges(DateUtil.getStringDate(apCTO.getLastFolioChrgon()));
            setTdtDueDateFCharges(DateUtil.getStringDate(apCTO.getNextFolioDuedate()));
            
            setTxtFolioEntriesFCharges(CommonUtil.convertObjToStr(apCTO.getNoEntriesPerFolio()));
            setTxtRateFCharges(CommonUtil.convertObjToStr(apCTO.getRatePerFolio()));
            
            //            setCboChargeOnTransactionFCharges(CommonUtil.convertObjToStr(apCTO.getToChrgOn()));
            //            setCboCAFrequencyFCharges(getFrequencyLabel(apCTO.getFolioChrgApplfreq()));
            //            setCboCollectChargeFCharges(apCTO.getToCollectFoliochrg());
            //            setCboChargeOnDocFCharges(apCTO.getToCollectChrgOn());
            //            setCboIRFrequencyFCharges(getFrequencyLabel(apCTO.getIncompFolioRoundoff()));
            
            setCboChargeOnTransactionFCharges((String) getCbmChargeOnTransactionFCharges().getDataForKey(CommonUtil.convertObjToStr(apCTO.getToChrgOn())));
            setCboCAFrequencyFCharges((String) getCbmCAFrequencyFCharges().getDataForKey(CommonUtil.convertObjToStr(apCTO.getFolioChrgApplfreq())));
            setCboCollectChargeFCharges((String) getCbmCollectChargeFCharges().getDataForKey(CommonUtil.convertObjToStr(apCTO.getToCollectFoliochrg())));
            setCboChargeOnDocFCharges((String) getCbmChargeOnDocFCharges().getDataForKey(CommonUtil.convertObjToStr(apCTO.getToCollectChrgOn())));
            setCboIRFrequencyFCharges((String) getCbmIRFrequencyFCharges().getDataForKey(CommonUtil.convertObjToStr(apCTO.getIncompFolioRoundoff())));
            
            //            if(CommonUtil.convertObjToStr(apCTO.getProcChrg()).equals("Y")){
            //                setRdoIsProcessingCharges_Yes(true);
            //
            //                Double percentValue = apCTO.getProcChrgPer();
            //                Double absoluteValue = apCTO.getProcChrgAmt();
            //                if(percentValue.doubleValue() > 0){
            //                    setCboProcessingCharges("Percent");
            //                    setTxtProcessingCharges(percentValue.toString());
            //                }
            //
            //                if(absoluteValue.doubleValue() > 0){
            //                    setCboProcessingCharges("Absolute");
            //                    setTxtProcessingCharges(absoluteValue.toString());
            //                }
            //            }else{
            //                setRdoIsProcessingCharges_No(true);
            //                setCboProcessingCharges("");
            //                setTxtProcessingCharges("0.0");
            //            }
            
            if(CommonUtil.convertObjToStr(apCTO.getProcChrg()).equals(YES)){
                setRdoIsProcessingCharges_Yes(true);
                
                if(CommonUtil.convertObjToInt(apCTO.getProcChrgPer())!=0){
                    setTxtProcessingCharges(CommonUtil.convertObjToStr(apCTO.getProcChrgPer()));
                    setCboProcessingCharges(PERCENT);
                    
                }else if(CommonUtil.convertObjToInt(apCTO.getProcChrgAmt())!= 0){
                    setTxtProcessingCharges(CommonUtil.convertObjToStr(apCTO.getProcChrgAmt()));
                    setCboProcessingCharges(ABSOLUTE);
                }
                
            }else{
                setRdoIsProcessingCharges_No(true);
                setCboProcessingCharges("");
                setTxtProcessingCharges("");
            }
            
            if(CommonUtil.convertObjToStr(apCTO.getCommitChrg()).equals(YES)){
                setRdoIsProcessingCharges_Yes(true);
                
                if(CommonUtil.convertObjToInt(apCTO.getCommitChrgPer())!=0){
                    setTxtCommitmentCharges(CommonUtil.convertObjToStr(apCTO.getCommitChrgPer()));
                    setCboCommitmentCharges(PERCENT);
                    
                }else if(CommonUtil.convertObjToInt(apCTO.getCommitChrgAmt())!= 0){
                    setTxtCommitmentCharges(CommonUtil.convertObjToStr(apCTO.getCommitChrgAmt()));
                    setCboCommitmentCharges(ABSOLUTE);
                }
                
            }else{
                setRdoIsProcessingCharges_No(true);
                setCboCommitmentCharges("");
                setTxtCommitmentCharges("");
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private AdvancesProductChargesTO getAdvancesProductChargesTO(){
        AdvancesProductChargesTO apCTO = new AdvancesProductChargesTO();
        try{
            apCTO.setProdId(getTxtProductIdAccount());
            apCTO.setAcClosingChrg(CommonUtil.convertObjToDouble(getTxtAccountClosingCharges()));
            apCTO.setMiscServChrg(CommonUtil.convertObjToDouble(getTxtMiscServiceCharges()));
            
            if(getRdoIsStatementCharges_Yes()==true){
                apCTO.setStatChrg(YES);
            }else{
                apCTO.setStatChrg(NO);
            }
            apCTO.setStatChrgRate(CommonUtil.convertObjToDouble(getTxtStatementCharges()));
            
            if(getRdoIsChequebookCharges_Yes()==true){
                apCTO.setChqbkIssuedChrg(YES);
            }else{
                apCTO.setChqbkIssuedChrg(NO);
            }
            apCTO.setChqbkIssuedChrgPer(CommonUtil.convertObjToDouble(getTxtChequebookCharges()));
            
            if(getRdoIsStopPaymentCharges_Yes()==true){
                apCTO.setStopPayChrg(YES);
            }else{
                apCTO.setStopPayChrg(NO);
            }
            apCTO.setStopPayChrgRate(CommonUtil.convertObjToDouble(getTxtStopPaymentCharges()));
            
            if(getRdoIsApplicableFCharges_Yes()==true){
                apCTO.setFolioChrgAppl(YES);
            }else{
                apCTO.setFolioChrgAppl(NO);
            }
            
            Date FcDt = DateUtil.getDateMMDDYYYY(getTdtLastAppliedFCharges());
            if(FcDt != null){
                Date fcDate = (Date) curDate.clone();
                fcDate.setDate(FcDt.getDate());
                fcDate.setMonth(FcDt.getMonth());
                fcDate.setYear(FcDt.getYear());
                //            apCTO.setLastFolioChrgon(DateUtil.getDateMMDDYYYY(getTdtLastAppliedFCharges()));
                apCTO.setLastFolioChrgon(fcDate);
            }else{
                apCTO.setLastFolioChrgon(DateUtil.getDateMMDDYYYY(getTdtLastAppliedFCharges()));
            }
            
            
            Date DueDt = DateUtil.getDateMMDDYYYY(getTdtDueDateFCharges());
            if(DueDt != null){
                Date dueDate = (Date) curDate.clone();
                dueDate.setDate(DueDt.getDate());
                dueDate.setMonth(DueDt.getMonth());
                dueDate.setYear(DueDt.getYear());
                //            apCTO.setNextFolioDuedate(DateUtil.getDateMMDDYYYY(getTdtDueDateFCharges()));
                apCTO.setNextFolioDuedate(dueDate);
            }else{
                apCTO.setNextFolioDuedate(DateUtil.getDateMMDDYYYY(getTdtDueDateFCharges()));
            }
            
            apCTO.setNoEntriesPerFolio(CommonUtil.convertObjToDouble(getTxtFolioEntriesFCharges()));
            apCTO.setRatePerFolio(CommonUtil.convertObjToDouble(getTxtRateFCharges()));
            
            apCTO.setToChrgOn(CommonUtil.convertObjToStr(cbmChargeOnTransactionFCharges.getKeyForSelected()));
            apCTO.setFolioChrgApplfreq(CommonUtil.convertObjToDouble(cbmCAFrequencyFCharges.getKeyForSelected()));
            apCTO.setToCollectFoliochrg(CommonUtil.convertObjToStr(cbmCollectChargeFCharges.getKeyForSelected()));
            apCTO.setToCollectChrgOn(CommonUtil.convertObjToStr(cbmChargeOnDocFCharges.getKeyForSelected()));
            apCTO.setIncompFolioRoundoff(CommonUtil.convertObjToDouble(cbmIRFrequencyFCharges.getKeyForSelected()));
            
            if(getRdoIsProcessingCharges_Yes()==true){
                apCTO.setProcChrg(YES);
                
                if(getCboProcessingCharges().equals(ABSOLUTE)){
                    apCTO.setProcChrgAmt(CommonUtil.convertObjToDouble(txtProcessingCharges));
                    
                }else if(getCboProcessingCharges().equals(PERCENT)){
                    apCTO.setProcChrgPer(CommonUtil.convertObjToDouble(txtProcessingCharges));
                    
                }
            }else{
                apCTO.setProcChrg(NO);
            }
            
            if(getRdoIsCommitmentCharges_Yes() == true){
                apCTO.setCommitChrg(YES);
                
                if(getCboProcessingCharges().equals(ABSOLUTE)){
                    apCTO.setCommitChrgAmt(CommonUtil.convertObjToDouble(txtCommitmentCharges));
                    
                }else if(getCboProcessingCharges().equals(PERCENT)){
                    apCTO.setCommitChrgPer(CommonUtil.convertObjToDouble(txtCommitmentCharges));
                    
                }
            }else{
                apCTO.setCommitChrg(NO);
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        return apCTO;
    }
    
    private void setAdvancesProductChqChargesTO(List list){
        try{
            AdvancesProductChqChargesTO apCCTO;
            List objList;
            int entries = list.size();
            
            EnhancedTableModel inwardCharges = getTbmCRChargesInward();
            EnhancedTableModel outwardCharges = getTbmCRChargesOutward();
            System.out.println("Inside Set method: " + entries);
            
            String chqRetType = "";
            
            for(int i=0; i < entries ; i++){
                apCCTO = (AdvancesProductChqChargesTO) list.get(i);
                chqRetType = apCCTO.getChqRetChrgtype();
                if(chqRetType.equals("Inward")){
                    objList = new ArrayList();
                    objList.add(apCCTO.getChqRetChrgamt());
                    objList.add(CommonUtil.convertObjToStr(apCCTO.getChqRetChrgrate()));
                    inwardCharges.addRow((ArrayList)objList);
                }
                if(chqRetType.equals("Outward")){
                    objList = new ArrayList();
                    objList.add(apCCTO.getChqRetChrgamt());
                    objList.add(CommonUtil.convertObjToStr(apCCTO.getChqRetChrgrate()));
                    outwardCharges.addRow((ArrayList)objList);
                }
            }
            
            setTbmCRChargesInward(inwardCharges);
            setTbmCRChargesOutward(outwardCharges);
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    private List getAdvancesProductChqChargesTO(){
        List list = new ArrayList();
        try{
            AdvancesProductChqChargesTO apCCTO;
            EnhancedTableModel tableDetails = getTbmCRChargesInward();
            int rows = tableDetails.getRowCount();
            for(int i=0; i < rows;i++){
                apCCTO = new AdvancesProductChqChargesTO();
                apCCTO.setProdId(getTxtProductIdAccount());
                apCCTO.setChqRetChrgtype("Inward");
                apCCTO.setChqRetChrgamt((String)tableDetails.getValueAt(i,0));
                apCCTO.setChqRetChrgrate(Double.valueOf((String)tableDetails.getValueAt(i,1)));
                list.add(apCCTO);
            }
            
            tableDetails = (EnhancedTableModel)getTbmCRChargesOutward();
            rows = tableDetails.getRowCount();
            for(int i=0; i < rows;i++){
                apCCTO = new AdvancesProductChqChargesTO();
                apCCTO.setProdId(getTxtProductIdAccount());
                apCCTO.setChqRetChrgtype("Outward");
                apCCTO.setChqRetChrgamt((String)tableDetails.getValueAt(i,0));
                apCCTO.setChqRetChrgrate(Double.valueOf((String)tableDetails.getValueAt(i,1)));
                list.add(apCCTO);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }
    
    private void setAdvancesProductIPTO(AdvancesProductIPTO apIPTO){
        
        if(CommonUtil.convertObjToStr(apIPTO.getCrIntPaid()).equals(YES)){
            setRdoCreditIntInterestPayable_Yes(true);
        }else{
            setRdoCreditIntInterestPayable_No(true);
        }
        setTxtCreditInterestRateIP(CommonUtil.convertObjToStr(apIPTO.getApplCrintRate()));
        
        //        setCboCreditInterestCFIP(getFrequencyLabel(apIPTO.getCrIntCalcfreq()));
        //        setCboCreditInterestAFIP(getFrequencyLabel(apIPTO.getCrIntApplfreq()));
        
        setCboCreditInterestCFIP((String) getCbmCreditInterestCFIP().getDataForKey(CommonUtil.convertObjToStr(apIPTO.getCrIntCalcfreq())));
        setCboCreditInterestAFIP((String) getCbmCreditInterestAFIP().getDataForKey(CommonUtil.convertObjToStr(apIPTO.getCrIntApplfreq())));
        setTxtCreditInterestAmt(CommonUtil.convertObjToStr(apIPTO.getCreditMinInterestAmt()));
        setTdtLastCDIP(DateUtil.getStringDate(apIPTO.getLastIntcalcDtcr()));
        setTdtLastADIP(DateUtil.getStringDate(apIPTO.getLastIntapplDtcr()));
        if(CommonUtil.convertObjToStr(apIPTO.getCrComp()).equals(YES)){
            setRdoCreditCompdInterestPayable_Yes(true);
        }else{
            setRdoCreditCompdInterestPayable_No(true);
        }
        //        setCboCreditInterestCompdFIP(getFrequencyLabel(apIPTO.getCrIntCompfreq()));
        //        setCboCPROIP(apIPTO.getCrProdRoundoff());
        //        setCboCIROIP(apIPTO.getCrIntRoundoff());
        //        setCboCalcCtriteriaIP(apIPTO.getCalcCriteria());
        //        setCboProdFrequencyIP(getFrequencyLabel(apIPTO.getProdFreq()));
        
        setCboCreditInterestCompdFIP((String) getCbmCreditInterestCompdFIP().getDataForKey(CommonUtil.convertObjToStr(apIPTO.getCrIntCompfreq())));
        setCboCPROIP((String) getCbmCPROIP().getDataForKey(CommonUtil.convertObjToStr(apIPTO.getCrProdRoundoff())));
        setCboCIROIP((String) getCbmCIROIP().getDataForKey(CommonUtil.convertObjToStr(apIPTO.getCrIntRoundoff())));
        setCboCalcCtriteriaIP((String) getCbmCalcCtriteriaIP().getDataForKey(CommonUtil.convertObjToStr(apIPTO.getCalcCriteria())));
        setCboProdFrequencyIP((String) getCbmProdFrequencyIP().getDataForKey(CommonUtil.convertObjToStr(apIPTO.getProdFreq())));
        
        if(CommonUtil.convertObjToStr(apIPTO.getAddiIntStaff()).equals(YES)){
            setRdoAdditionalIntInterestPayable_Yes(true);
        }else{
            setRdoAdditionalIntInterestPayable_No(true);
        }
        setTxtAddIntRateIP(CommonUtil.convertObjToStr(apIPTO.getAddiIntStaffPer()));
    }
    
    private AdvancesProductIPTO getAdvancesProductIPTO(){
        AdvancesProductIPTO apIPTO = new AdvancesProductIPTO();
        try{
            apIPTO.setProdId(getTxtProductIdAccount());
            if(getRdoCreditIntInterestPayable_Yes() == true){
                apIPTO.setCrIntPaid(YES);
            }else{
                apIPTO.setCrIntPaid(NO);
            }
            apIPTO.setApplCrintRate(CommonUtil.convertObjToDouble(getTxtCreditInterestRateIP()));
            
            //            apIPTO.setCrIntCalcfreq(getFrequencyValue(getCboCreditInterestCFIP()));
            //            apIPTO.setCrIntApplfreq(getFrequencyValue(getCboCreditInterestAFIP()));
            apIPTO.setCrIntCalcfreq(CommonUtil.convertObjToDouble(cbmCreditInterestCFIP.getKeyForSelected()));
            apIPTO.setCrIntApplfreq(CommonUtil.convertObjToDouble(cbmCreditInterestAFIP.getKeyForSelected()));
            
            Date LstCdDt = DateUtil.getDateMMDDYYYY(getTdtLastCDIP());
            if(LstCdDt != null){
                Date lstcdDate = (Date) curDate.clone();
                lstcdDate.setDate(LstCdDt.getDate());
                lstcdDate.setMonth(LstCdDt.getMonth());
                lstcdDate.setYear(LstCdDt.getYear());
                //            apIPTO.setLastIntcalcDtcr(DateUtil.getDateMMDDYYYY(getTdtLastCDIP()));
                apIPTO.setLastIntcalcDtcr(lstcdDate);
            }else{
                apIPTO.setLastIntcalcDtcr(DateUtil.getDateMMDDYYYY(getTdtLastCDIP()));
            }
            
            Date LstDt = DateUtil.getDateMMDDYYYY(getTdtLastADIP());
            if(LstDt != null){
                Date lstDate = (Date) curDate.clone();
                lstDate.setDate(LstDt.getDate());
                lstDate.setMonth(LstDt.getMonth());
                lstDate.setYear(LstDt.getYear());
                //            apIPTO.setLastIntapplDtcr(DateUtil.getDateMMDDYYYY(getTdtLastADIP()));
                apIPTO.setLastIntapplDtcr(lstDate);
            }else{
                apIPTO.setLastIntapplDtcr(DateUtil.getDateMMDDYYYY(getTdtLastADIP()));
            }
            
            if(getRdoCreditCompdInterestPayable_Yes()==true){
                apIPTO.setCrComp(YES);
            }else{
                apIPTO.setCrComp(NO);
            }
            
            //            apIPTO.setCrIntCompfreq(getFrequencyValue(getCboCreditInterestCompdFIP()));
            //            apIPTO.setCrProdRoundoff(getCboCPROIP());
            //            apIPTO.setCrIntRoundoff(getCboCIROIP());
            //            apIPTO.setCalcCriteria(getCboCalcCtriteriaIP());
            //            apIPTO.setProdFreq(getFrequencyValue(getCboProdFrequencyIP()));
            apIPTO.setCreditMinInterestAmt(CommonUtil.convertObjToDouble(txtCreditInterestAmt));
            apIPTO.setCrIntCompfreq(CommonUtil.convertObjToDouble(cbmCreditInterestCompdFIP.getKeyForSelected()));
            apIPTO.setCrProdRoundoff(CommonUtil.convertObjToStr(cbmCPROIP.getKeyForSelected()));
            apIPTO.setCrIntRoundoff(CommonUtil.convertObjToStr(cbmCIROIP.getKeyForSelected()));
            apIPTO.setCalcCriteria(CommonUtil.convertObjToStr(cbmCalcCtriteriaIP.getKeyForSelected()));
            apIPTO.setProdFreq(CommonUtil.convertObjToDouble(cbmProdFrequencyIP.getKeyForSelected()));
            
            if(getRdoAdditionalIntInterestPayable_Yes()==true){
                apIPTO.setAddiIntStaff(YES);
            }else{
                apIPTO.setAddiIntStaff(NO);
            }
            
            apIPTO.setAddiIntStaffPer(CommonUtil.convertObjToDouble(getTxtAddIntRateIP()));
            
        }catch(Exception e){
            e.printStackTrace();
        }
        return apIPTO;
    }
    
    private void setAdvancesProductIRTO(AdvancesProductIRTO apIRTO){
        
        if(CommonUtil.convertObjToStr(apIRTO.getDebitIntCharged()).equals(YES)){
            setRdoChargedDIIR_Yes(true);
        }else{
            setRdoChargedDIIR_No(true);
        }
        setTxtMinDIRateIR(CommonUtil.convertObjToStr(apIRTO.getMinDebitintRate()));
        setTxtMaxDIRateIR(CommonUtil.convertObjToStr(apIRTO.getMaxDebitintRate()));
        setTxtMinDIAmtIR(CommonUtil.convertObjToStr(apIRTO.getMinDebitintAmt()));
        setTxtMaxDIAmtIR(CommonUtil.convertObjToStr(apIRTO.getMaxDebitintAmt()));
        //        setCboDICalculationFIR(getFrequencyLabel(apIRTO.getDebitintCalcFreq()));
        //        setCboDIApplicationFIR(getFrequencyLabel(apIRTO.getDebitintApplFreq()));
        
        setCboDICalculationFIR((String) getCbmDICalculationFIR().getDataForKey(CommonUtil.convertObjToStr(apIRTO.getDebitintCalcFreq())));
        setCboDIApplicationFIR((String) getCbmDIApplicationFIR().getDataForKey(CommonUtil.convertObjToStr(apIRTO.getDebitintApplFreq())));
        
        
        if(CommonUtil.convertObjToStr(apIRTO.getDebitCompReq()).equals(YES)){
            setRdoDebitCompoundIR_Yes(true);
        }else{
            setRdoDebitCompoundIR_No(true);
        }
        
        //        setCboDICompoundFIR(getFrequencyLabel(apIRTO.getDebitintCompFreq()));
        setCboDICompoundFIR((String) getCbmDICompoundFIR().getDataForKey(CommonUtil.convertObjToStr(apIRTO.getDebitintCompFreq())));
        
        //        setCboDPRoundOffIR(apIRTO.getDebitProdRoundoff());
        //        setCboDIRoundOffIR(apIRTO.getDebitIntRoundoff());
        
        setCboDPRoundOffIR((String) getCbmDPRoundOffIR().getDataForKey(CommonUtil.convertObjToStr(apIRTO.getDebitProdRoundoff())));
        setCboDIRoundOffIR((String) getCbmDIRoundOffIR().getDataForKey(CommonUtil.convertObjToStr(apIRTO.getDebitIntRoundoff())));
        
        
        setTdtInterestCDDebitIR(DateUtil.getStringDate(apIRTO.getLastIntapplDtdebit()));
        setTdtInterestADDebitIR(DateUtil.getStringDate(apIRTO.getLastIntapplDtdebit()));
        
        //        setCboProductFOthersIR(getFrequencyLabel(apIRTO.getProdFreq()));
        setCboProductFOthersIR((String) getCbmProductFOthersIR().getDataForKey(CommonUtil.convertObjToStr(apIRTO.getProdFreq())));
        
        if(CommonUtil.convertObjToStr(apIRTO.getUnclearAdvIntcharg()).equals(YES)){
            setRdoUAICOthersIR_Yes(true);
        }else{
            setRdoUAICOthersIR_No(true);
        }
        
        if(CommonUtil.convertObjToStr(apIRTO.getPlrRateAppl()).equals(YES)){
            setRdoIsApplicablePLRIR_Yes(true);
        }else{
            setRdoIsApplicablePLRIR_No(true);
        }
        setTxtRatePLRIR(CommonUtil.convertObjToStr(apIRTO.getPlrRate()));
        setTdtAppliedFromPLRIR(DateUtil.getStringDate(apIRTO.getPlrApplFrom()));
        
        if(CommonUtil.convertObjToStr(apIRTO.getPlrApplNewac()).equals(YES)){
            setRdoNewAccountPLRIR_Yes(true);
        }else{
            setRdoNewAccountPLRIR_No(true);
        }
        
        if(CommonUtil.convertObjToStr(apIRTO.getPlrApplExistac()).equals(YES)){
            setRdoExistingAccountPLRIR_Yes(true);
        }else{
            setRdoExistingAccountPLRIR_No(true);
        }
        
        setTdtAccountSFPLRIR(DateUtil.getStringDate(apIRTO.getPlrApplSancfrom()));
        
        if(CommonUtil.convertObjToStr(apIRTO.getEolAppl()).equals(YES)){
            setRdoEOLOthersIR_Yes(true);
        }else{
            setRdoEOLOthersIR_No(true);
        }
        
        if(CommonUtil.convertObjToStr(apIRTO.getPenalAppl()).equals(YES)){
            setRdoPenalOthersIR_Yes(true);
        }else{
            setRdoPenalOthersIR_No(true);
        }
        
        if(CommonUtil.convertObjToStr(apIRTO.getLimitExpiryInt()).equals(YES)){
            setRdoLimitEIOthersIR_Yes(true);
        }else{
            setRdoLimitEIOthersIR_No(true);
        }
        
        setTxtPenalIROthersIR(CommonUtil.convertObjToStr(apIRTO.getPenalIntRate()));
        
    }
    
    private AdvancesProductIRTO getAdvancesProductIRTO(){
        AdvancesProductIRTO apIRTO = new AdvancesProductIRTO();
        try{
            apIRTO.setProdId(getTxtProductIdAccount());
            if(getRdoChargedDIIR_Yes() == true){
                apIRTO.setDebitIntCharged(YES);
            }else{
                apIRTO.setDebitIntCharged(NO);
            }
            apIRTO.setMinDebitintRate(CommonUtil.convertObjToDouble(getTxtMinDIRateIR()));
            apIRTO.setMaxDebitintRate(CommonUtil.convertObjToDouble(getTxtMaxDIRateIR()));
            apIRTO.setMinDebitintAmt(CommonUtil.convertObjToDouble(getTxtMinDIAmtIR()));
            apIRTO.setMaxDebitintAmt(CommonUtil.convertObjToDouble(getTxtMaxDIAmtIR()));
            
            //            apIRTO.setDebitintCalcFreq(getFrequencyValue(getCboDICalculationFIR()));
            //            apIRTO.setDebitintApplFreq(getFrequencyValue(getCboDIApplicationFIR()));
            
            apIRTO.setDebitintCalcFreq(CommonUtil.convertObjToDouble(cbmDICalculationFIR.getKeyForSelected()));
            apIRTO.setDebitintApplFreq(CommonUtil.convertObjToDouble(cbmDIApplicationFIR.getKeyForSelected()));
            
            if(getRdoDebitCompoundIR_Yes()==true){
                apIRTO.setDebitCompReq(YES);
            }else{
                apIRTO.setDebitCompReq(NO);
            }
            
            //            apIRTO.setDebitintCompFreq(getFrequencyValue(getCboDICompoundFIR()));
            apIRTO.setDebitintCompFreq(CommonUtil.convertObjToDouble(cbmDICompoundFIR.getKeyForSelected()));
            
            //            apIRTO.setDebitProdRoundoff(getCboDPRoundOffIR());
            //            apIRTO.setDebitIntRoundoff(getCboDIRoundOffIR());
            
            apIRTO.setDebitProdRoundoff(CommonUtil.convertObjToStr(cbmDPRoundOffIR.getKeyForSelected()));
            apIRTO.setDebitIntRoundoff(CommonUtil.convertObjToStr(cbmDIRoundOffIR.getKeyForSelected()));
            
            Date IntDt = DateUtil.getDateMMDDYYYY(getTdtInterestCDDebitIR());
            if(IntDt != null){
                Date intDate = (Date) curDate.clone();
                intDate.setDate(IntDt.getDate());
                intDate.setMonth(IntDt.getMonth());
                intDate.setYear(IntDt.getYear());
                //            apIRTO.setLastIntcalcDtdebit(DateUtil.getDateMMDDYYYY(getTdtInterestCDDebitIR()));
                apIRTO.setLastIntcalcDtdebit(intDate);
            }else{
                apIRTO.setLastIntcalcDtdebit(DateUtil.getDateMMDDYYYY(getTdtInterestCDDebitIR()));
            }
            
            Date IntAdDt = DateUtil.getDateMMDDYYYY(getTdtInterestADDebitIR());
            if(IntAdDt != null){
                Date intadDate = (Date) curDate.clone();
                intadDate.setDate(IntAdDt.getDate());
                intadDate.setMonth(IntAdDt.getMonth());
                intadDate.setYear(IntAdDt.getYear());
                //            apIRTO.setLastIntapplDtdebit(DateUtil.getDateMMDDYYYY(getTdtInterestADDebitIR()));
                apIRTO.setLastIntapplDtdebit(intadDate);
            }else{
                apIRTO.setLastIntapplDtdebit(DateUtil.getDateMMDDYYYY(getTdtInterestADDebitIR()));
            }
            
            //            apIRTO.setProdFreq(getFrequencyValue(getCboProductFOthersIR()));
            apIRTO.setProdFreq(CommonUtil.convertObjToDouble(cbmProductFOthersIR.getKeyForSelected()));
            
            if(getRdoUAICOthersIR_Yes()==true){
                apIRTO.setUnclearAdvIntcharg(YES);
            }else{
                apIRTO.setUnclearAdvIntcharg(NO);
            }
            
            if(getRdoIsApplicablePLRIR_Yes()==true){
                apIRTO.setPlrRateAppl(YES);
            }else{
                apIRTO.setPlrRateAppl(NO);
            }
            apIRTO.setPlrRate(CommonUtil.convertObjToDouble(getTxtRatePLRIR()));
            
            Date TdtApDt = DateUtil.getDateMMDDYYYY(getTdtAppliedFromPLRIR());
            if(TdtApDt != null){
                Date tdtAdDate = (Date) curDate.clone();
                tdtAdDate.setDate(TdtApDt.getDate());
                tdtAdDate.setMonth(TdtApDt.getMonth());
                tdtAdDate.setYear(TdtApDt.getYear());
                //            apIRTO.setPlrApplFrom(DateUtil.getDateMMDDYYYY(getTdtAppliedFromPLRIR()));
                apIRTO.setPlrApplFrom(tdtAdDate);
            }else{
                apIRTO.setPlrApplFrom(DateUtil.getDateMMDDYYYY(getTdtAppliedFromPLRIR()));
            }
            
            if(getRdoNewAccountPLRIR_Yes()==true){
                apIRTO.setPlrApplNewac(YES);
            }else{
                apIRTO.setPlrApplNewac(NO);
            }
            
            if(getRdoExistingAccountPLRIR_Yes()==true){
                apIRTO.setPlrApplExistac(YES);
            }else{
                apIRTO.setPlrApplExistac(NO);
            }
            
            Date TdtAmDt = DateUtil.getDateMMDDYYYY(getTdtAccountSFPLRIR());
            if(TdtAmDt != null){
                Date tdtAmDate = (Date) curDate.clone();
                tdtAmDate.setDate(TdtAmDt.getDate());
                tdtAmDate.setMonth(TdtAmDt.getMonth());
                tdtAmDate.setYear(TdtAmDt.getYear());
                //            apIRTO.setPlrApplSancfrom(DateUtil.getDateMMDDYYYY(getTdtAccountSFPLRIR()));
                apIRTO.setPlrApplSancfrom(tdtAmDate);
            }else{
                apIRTO.setPlrApplSancfrom(DateUtil.getDateMMDDYYYY(getTdtAccountSFPLRIR()));
            }
            
            if(getRdoEOLOthersIR_Yes()==true){
                apIRTO.setEolAppl(YES);
            }else{
                apIRTO.setEolAppl(NO);
            }
            
            if(getRdoPenalOthersIR_Yes()==true){
                apIRTO.setPenalAppl(YES);
            }else{
                apIRTO.setPenalAppl(NO);
            }
            
            if(getRdoLimitEIOthersIR_Yes()==true){
                apIRTO.setLimitExpiryInt(YES);
            }else{
                apIRTO.setLimitExpiryInt(NO);
            }
            
            apIRTO.setPenalIntRate(CommonUtil.convertObjToDouble(getTxtPenalIROthersIR()));
        }catch(Exception e){
            e.printStackTrace();
        }
        return apIRTO;
    }
    
    private void setAdvancesProductSITO(AdvancesProductSITO apSITO){
        
        if(CommonUtil.convertObjToStr(apSITO.getAtmCardIssued()).equals(YES)){
            setRdoATMCardSI_Yes(true);
        }else{
            setRdoATMCardSI_No(true);
        }
        
        if(CommonUtil.convertObjToStr(apSITO.getCrCardIssued()).equals(YES)){
            setRdoCreditCardSI_Yes(true);
        }else{
            setRdoCreditCardSI_No(true);
        }
        
        if(CommonUtil.convertObjToStr(apSITO.getDebitCardIssued()).equals(YES)){
            setRdoDebitCardSI_Yes(true);
        }else{
            setRdoDebitCardSI_No(true);
        }
        
        if(CommonUtil.convertObjToStr(apSITO.getMobileBankClient()).equals(YES)){
            setRdoMobileBankingClientSI_Yes(true);
        }else{
            setRdoMobileBankingClientSI_No(true);
        }
        if(CommonUtil.convertObjToStr(apSITO.getBranchBankingAllowed()).equals(YES)){
            setRdoBranchBankingSI_Yes(true);
        }else{
            setRdoBranchBankingSI_No(true);
        }
        
    }
    private AdvancesProductSITO getAdvancesProductSITO(){
        AdvancesProductSITO apSITO = new AdvancesProductSITO();
        try{
            apSITO.setProdId(getTxtProductIdAccount());
            if(getRdoATMCardSI_Yes() == true){
                apSITO.setAtmCardIssued(YES);
            }else{
                apSITO.setAtmCardIssued(NO);
            }
            
            if(getRdoCreditCardSI_Yes()==true){
                apSITO.setCrCardIssued(YES);
            }else{
                apSITO.setCrCardIssued(NO);
            }
            
            if(getRdoDebitCardSI_Yes()==true){
                apSITO.setDebitCardIssued(YES);
            }else{
                apSITO.setDebitCardIssued(NO);
            }
            
            if(getRdoMobileBankingClientSI_Yes()==true){
                apSITO.setMobileBankClient(YES);
            }else{
                apSITO.setMobileBankClient(NO);
            }
            if(getRdoBranchBankingSI_Yes()==true){
                apSITO.setBranchBankingAllowed(YES);
            }else{
                apSITO.setBranchBankingAllowed(NO);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return apSITO;
    }
    
    private void setAdvancesProductSplAssetsTO(List list){
        int entries = list.size();
        
        EnhancedTableModel saDetails = getTbmMiscItemSI();
        AdvancesProductSplAssetsTO apSplAssetsTO;
        for(int i=0; i < entries ; i++){
            
            apSplAssetsTO = (AdvancesProductSplAssetsTO) list.get(i);
            
            List objList = new ArrayList();
            objList.add(apSplAssetsTO.getAssetCategory());
            objList.add(CommonUtil.convertObjToStr(apSplAssetsTO.getAssetCategoryRateper()));
            saDetails.addRow((ArrayList)objList);
        }
        
        setTbmMiscItemSI(saDetails);
        
    }
    
    private List getAdvancesProductSplAssetsTO(){
        AdvancesProductSplAssetsTO apSplAssetsTO;
        List list = new ArrayList();
        try{
            TableModel tableDetails = (EnhancedTableModel)getTbmMiscItemSI();
            int rows = tableDetails.getRowCount();
            for(int i=0; i < rows;i++){
                apSplAssetsTO = new AdvancesProductSplAssetsTO();
                apSplAssetsTO.setProdId(getTxtProductIdAccount());
                apSplAssetsTO.setAssetCategory((String)tableDetails.getValueAt(i,0));
                apSplAssetsTO.setAssetCategoryRateper(Double.valueOf((String)tableDetails.getValueAt(i,1)));
                list.add(apSplAssetsTO);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }
    
    // to find the value of multiplier on the basis of period...
    public double setCombo(String duration) throws Exception{
        periodData=0;
        resultData=0;
        int period=0;
        if(!duration.equalsIgnoreCase("")){
            if( duration.equals(DAY))
                period = day;
            else if(duration.equals(MONTH))
                period = month;
            else if(duration.equals(YEAR))
                period = year;
        }
        
        duration = "";
        return period;
    }
    
    private String setPeriod(int rV) throws Exception{
        String periodValue;
        if ((rV >= year) && (rV%year == 0)) {
            periodValue = YEAR1;
            rV = rV/year;
        } else if ((rV >= month) && (rV % month == 0)) {
            periodValue=MONTH1;
            rV = rV/month;
        } else if ((rV >= day) && (rV % day == 0)) {
            periodValue=DAY1;
            rV = rV;
        } else {
            periodValue="";
            rV = 0;
        }
        resultValue = rV;
        return periodValue;
    }
    
    private void resetPeriod(){
        resultValue = 0;
    }
    
    // Returns the Current Value of Action type...
    public int getActionType(){
        return actionType;
    }
    
    // Sets the value of the Action Type to the poperation we want to execute...
    public void setActionType(int actionType) {
        this.actionType = actionType;
        setChanged();
    }
    
    // To set and change the Status of the lable STATUS
    public void setResult(int result) {
        this.result = result;
        setChanged();
    }
    public int getResult(){
        return this.result;
    }
    
    // To set the Value of the lblStatus...
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }
    public String getLblStatus(){
        return lblStatus;
    }
    
    //To reset the Value of lblStatus after each save action...
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }
    
    /** To set the status based on ActionType, either New, Edit, etc., */
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
        ttNotifyObservers();
    }
    
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        ttNotifyObservers();
    }
    
    public void ttNotifyObservers(){
        setChanged();
        notifyObservers();
    }
    
    //__ To Verify the Product Id...
    public boolean verifyProdId(String prodId) {
        boolean verify = false;
        try{
            final HashMap data = new HashMap();
            data.put("PROD_ID",prodId);
            final List resultList = ClientUtil.executeQuery("TermLoan.getProdId", data);
            if(resultList.size() > 0){
                verify = true;
            }
            else{
                verify = false;
            }
        }catch(Exception e){
            parseException.logException(e,true);
        }
        return verify;
    }
    
    public void doAction() {
        log.info("In doAction()");
        try {
            //If actionType such as NEW, EDIT, DELETE, then proceed
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                //If actionType has got propervalue then doActionPerform, else throw error
                if( getCommand() != null){
                    doActionPerform();
                }
            }
            else
                log.info("Action Type Not Defined In setChequeBookTO()");
        } catch (Exception e) {
            log.info("Error In doAction()");
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        log.info("In doActionPerform()");
        final HashMap data = new HashMap();
        final LoanProductAccountTO objLoanProductAccountTO = getAdvancesProductTO();
        objLoanProductAccountTO.setCommand(getCommand());
        
        data.put("LoanProductAccountTO", objLoanProductAccountTO);
        data.put("AdvancesProductAccHeadTO", getAdvancesProductAccHeadTO());
        data.put("AdvancesProductAccParameterTO", getAdvancesProductAccParameterTO());
        data.put("AdvancesProductChargesTO", getAdvancesProductChargesTO());
        data.put("AdvancesProductChqChargesTO", getAdvancesProductChqChargesTO());
        data.put("AdvancesProductIPTO", getAdvancesProductIPTO());
        data.put("AdvancesProductIRTO", getAdvancesProductIRTO());
        data.put("AdvancesProductSITO", getAdvancesProductSITO());
        data.put("AdvancesProductSplAssetsTO", getAdvancesProductSplAssetsTO());
        
        HashMap proxyResultMap = proxy.execute(data,operationMap);
        setResult(actionType);
        resetOBFields();
    }
    
    // to decide which action Should be performed...
    private String getCommand() throws Exception{
        log.info("In getCommand()");
        
        String command = null;
        switch (actionType) {
            case ClientConstants.ACTIONTYPE_NEW:
                command = CommonConstants.TOSTATUS_INSERT;
                break;
            case ClientConstants.ACTIONTYPE_EDIT:
                command = CommonConstants.TOSTATUS_UPDATE;
                break;
            case ClientConstants.ACTIONTYPE_DELETE:
                command = CommonConstants.TOSTATUS_DELETE;
                break;
            default:
        }
        return command;
    }
    
    public void populateData(HashMap whereMap) {
        HashMap mapData = null;
        try {
            mapData = proxy.executeQuery(whereMap, operationMap);
            
            populateOB(mapData);
        } catch( Exception e ) {
            System.err.println( "Exception " + e.toString() + "Caught" );
            e.printStackTrace();
        }
    }
    
    private void populateOB(HashMap mapData) {
        //resetOBFields();
        
        AdvancesProductTO advancesProductTO = null;
        AdvancesProductAccHeadTO advancesProductAccHeadTO = null;
        AdvancesProductAccParameterTO advancesProductAccParameterTO = null;
        AdvancesProductChargesTO advancesProductChargesTO = null;
        List advancesProductChqChargesTO = null;
        AdvancesProductIPTO advancesProductIPTO = null;
        AdvancesProductIRTO advancesProductIRTO = null;
        AdvancesProductSITO advancesProductSITO = null;
        AgriLoanProductAccountTO  agriLoanProductAccountTO=null;
        List advancesProductSplAssetsTO = null;
        
        LoanProductAccountTO loanProductAccountTO = null;
        
        //        advancesProductTO = (AdvancesProductTO) ((List) mapData.get("AdvancesProductTO")).get(0);
        //        setAdvancesProductTO (advancesProductTO);
        
        List list = (List) mapData.get("AdvancesProductAccHeadTO");
        if(list.size() > 0){
            if(mapData.containsKey("AGRI")){
                agriLoanProductAccountTO = (AgriLoanProductAccountTO) ((List) mapData.get("LoanProductAccountTO")).get(0);
                setAgriAdvancesProductTO(agriLoanProductAccountTO);
            }
            else{
                loanProductAccountTO = (LoanProductAccountTO) ((List) mapData.get("LoanProductAccountTO")).get(0);
                setAdvancesProductTO(loanProductAccountTO);
            }
            advancesProductAccHeadTO = (AdvancesProductAccHeadTO) ((List) mapData.get("AdvancesProductAccHeadTO")).get(0);
            setAdvancesProductAccHeadTO(advancesProductAccHeadTO);
            
            advancesProductAccParameterTO = (AdvancesProductAccParameterTO) ((List) mapData.get("AdvancesProductAccParameterTO")).get(0);
            setAdvancesProductAccParameterTO(advancesProductAccParameterTO);
            
            advancesProductChargesTO = (AdvancesProductChargesTO) ((List) mapData.get("AdvancesProductChargesTO")).get(0);
            setAdvancesProductChargesTO(advancesProductChargesTO);
            
            advancesProductChqChargesTO = (List) mapData.get("AdvancesProductChqChargesTO");
            System.out.println("Inside Populate OB : " + advancesProductChqChargesTO.size());
            setAdvancesProductChqChargesTO(advancesProductChqChargesTO);
            
            advancesProductIPTO = (AdvancesProductIPTO) ((List) mapData.get("AdvancesProductIPTO")).get(0);
            setAdvancesProductIPTO(advancesProductIPTO);
            
            advancesProductIRTO = (AdvancesProductIRTO) ((List) mapData.get("AdvancesProductIRTO")).get(0);
            setAdvancesProductIRTO(advancesProductIRTO);
            
            advancesProductSITO = (AdvancesProductSITO) ((List) mapData.get("AdvancesProductSITO")).get(0);
            setAdvancesProductSITO(advancesProductSITO);
            
            advancesProductSplAssetsTO =  (List) mapData.get("AdvancesProductSplAssetsTO");
            System.out.println("Inside Populate OB : " + advancesProductSplAssetsTO.size());
            setAdvancesProductSplAssetsTO(advancesProductSplAssetsTO);
            
        }
    }
    
    void setCboProdCurrency(String cboProdCurrency){
        this.cboProdCurrency = cboProdCurrency;
        setChanged();
        
    }
    String getCboProdCurrency(){
        return this.cboProdCurrency;
    }
    
    public void resetOBFields(){
        setCboProdCurrency("");
        setCboAmountCRIC("");
        setTxtRateCRIC("");
        setTxtLastAccNoAccount("");
        setCboBehavesLike("");
        setTdtCLStartAccount("");
        setTxtCLPeriodAccount("");
        setCboCLPeriod("");
        setTxtAccountHeadAccount("");
        setTxtManagerDistAccount("");
        setTxtFreeCLAccount("");
        setCboStmtFrequency("");
        setTxtNumberpatternAccount("");
        setRdoLDAccount_Yes(false);
        setRdoLDAccount_No(false);
        setRdoACAAccount_Yes(false);
        setRdoACAAccount_No(false);
        setRdoSAOAccount_Yes(false);
        setRdoSAOAccount_No(false);
        setRdoTokanAccount_Yes(false);
        setRdoTokanAccount_No(false);
        setRdoCIUEAccount_Yes(false);
        setRdoCIUEAccount_No(false);
        setRdoODALAccount_Yes(false);
        setRdoODALAccount_No(false);
        setRdoDIAUEAccount_Yes(false);
        setRdoDIAUEAccount_No(false);
        setRdoWSAccount_Yes(false);
        setRdoWSAccount_No(false);
        setTxtMaxAmountOnWS("");
        setTxtProductIdAccount("");
        setTxtProductDescAccount("");
        setRdoChargedDIIR_Yes(false);
        setRdoChargedDIIR_No(false);
        setTxtMinDIRateIR("");
        setTxtMaxDIRateIR("");
        setTxtMinDIAmtIR("");
        setTxtMaxDIAmtIR("");
        setCboDICalculationFIR("");
        setTdtInterestCDDebitIR("");
        setCboDIApplicationFIR("");
        setTdtInterestADDebitIR("");
        setRdoDebitCompoundIR_Yes(false);
        setRdoDebitCompoundIR_No(false);
        setCboDICompoundFIR("");
        setCboDPRoundOffIR("");
        setCboDIRoundOffIR("");
        setCboProductFOthersIR("");
        setRdoUAICOthersIR_Yes(false);
        setRdoUAICOthersIR_No(false);
        setRdoEOLOthersIR_Yes(false);
        setRdoEOLOthersIR_No(false);
        setRdoPenalOthersIR_Yes(false);
        setRdoPenalOthersIR_No(false);
        setRdoLimitEIOthersIR_Yes(false);
        setRdoLimitEIOthersIR_No(false);
        setTxtPenalIROthersIR("");
        setRdoIsApplicablePLRIR_Yes(false);
        setRdoIsApplicablePLRIR_No(false);
        setTxtRatePLRIR("");
        setTdtAppliedFromPLRIR("");
        setRdoNewAccountPLRIR_Yes(false);
        setRdoNewAccountPLRIR_No(false);
        setRdoExistingAccountPLRIR_Yes(false);
        setRdoExistingAccountPLRIR_No(false);
        setTdtAccountSFPLRIR("");
        setRdoCreditIntInterestPayable_Yes(false);
        setRdoCreditIntInterestPayable_No(false);
        setRdoCreditCompdInterestPayable_Yes(false);
        setRdoCreditCompdInterestPayable_No(false);
        setRdoAdditionalIntInterestPayable_Yes(false);
        setRdoAdditionalIntInterestPayable_No(false);
        setCboCreditInterestCFIP("");
        setCboCreditInterestAFIP("");
        setCboCreditInterestCompdFIP("");
        setCboCPROIP("");
        setCboCIROIP("");
        setCboCalcCtriteriaIP("");
        setCboProdFrequencyIP("");
        setTxtCreditInterestRateIP("");
        setTdtLastCDIP("");
        setTdtLastADIP("");
        setTxtAddIntRateIP("");
        setRdoIsApplicableFCharges_Yes(true);
        setRdoIsApplicableFCharges_No(false);
        setTdtLastAppliedFCharges("");
        setTdtDueDateFCharges("");
        setTxtFolioEntriesFCharges("");
        setTxtRateFCharges("");
        setCboChargeOnTransactionFCharges("");
        setCboCAFrequencyFCharges("");
        setCboCollectChargeFCharges("");
        setCboChargeOnDocFCharges("");
        setCboIRFrequencyFCharges("");
        setRdoIsStatementCharges_Yes(false);
        setRdoIsStatementCharges_No(false);
        setRdoIsChequebookCharges_Yes(false);
        setRdoIsChequebookCharges_No(false);
        setRdoIsStopPaymentCharges_Yes(false);
        setRdoIsStopPaymentCharges_No(false);
        setRdoIsProcessingCharges_Yes(false);
        setRdoIsProcessingCharges_No(false);
        setTxtProcessingCharges("");
        setCboProcessingCharges("");
        setRdoIsCommitmentCharges_Yes(false);
        setRdoIsCommitmentCharges_No(false);
        setTxtCommitmentCharges("");
        setCboCommitmentCharges("");
        setTxtAccountClosingCharges("");
        setTxtStatementCharges("");
        setTxtMiscServiceCharges("");
        setTxtChequebookCharges("");
        setTxtStopPaymentCharges("");
        setCboAmountCRIC("");
        setTxtRateCRIC("");
        setCboAmountCROC("");
        setTxtRateCROC("");
        setRdoATMCardSI_Yes(false);
        setRdoATMCardSI_No(false);
        setRdoCreditCardSI_Yes(false);
        setRdoCreditCardSI_No(false);
        setRdoDebitCardSI_Yes(false);
        setRdoDebitCardSI_No(false);
        setRdoMobileBankingClientSI_Yes(false);
        setRdoMobileBankingClientSI_No(false);
        setRdoBranchBankingSI_Yes(false);
        setRdoBranchBankingSI_No(false);
        setTxtRateSI("");
        setCboAssetsSI("");
        setTxtACCAH("");
        setTxtMSCAH("");
        setTxtSCAH("");
        setTxtDIAH("");
        setTxtPIAH("");
        setTxtCIAH("");
        setTxtAgCIAH("");
        setTxtEIAH("");
        setTxtExOLHAH("");
        setTxtCICAH("");
        setTxtSPCAH("");
        setTxtCRCInwardAH("");
        setTxtCRCoutwardAH("");
        setTxtFCAH("");
        resetTableModel(tbmCRChargesInward);
        resetTableModel(tbmCRChargesOutward);
        resetTableModel(tbmMiscItemSI);
        //        notifyObservers();
    }
    
    private void resetTableModel(EnhancedTableModel etm){
        int rows = etm.getRowCount();
        for(int i=0 ;i< rows;i++){
            etm.removeRow(0);
        }
    }
    
    //This method corresponds to the ComboBoxItemStateChange event for Chech Return Inward Charges Combobox.
    public String cboCRICSelectionEvent(String selectedValue){
        String returnValue = "";
        try{
            int rows = tbmCRChargesInward.getRowCount();
            if(rows > 0){
                for(int i = 0; i<rows;i++)
                    if(((String)tbmCRChargesInward.getValueAt(i,0)).equals(selectedValue))
                        returnValue = (String)tbmCRChargesInward.getValueAt(i,1);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return returnValue;
    }
    
    //This method corresponds to the ComboBoxItemStateChange event for Chech Return Outward Charges Combobox.
    public String cboCROCSelectionEvent(String selectedValue){
        String returnValue = "";
        try{
            int rows = tbmCRChargesOutward.getRowCount();
            if(rows > 0){
                for(int i = 0; i<rows;i++)
                    if(((String)tbmCRChargesOutward.getValueAt(i,0)).equals(selectedValue))
                        returnValue = (String)tbmCRChargesOutward.getValueAt(i,1);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return returnValue;
    }
    
    //This method corresponds to the ComboBoxItemStateChange event for Misc Item Combobox.
    public String cboMISISelectionEvent(String selectedValue){
        String returnValue = "";
        try{
            int rows = tbmMiscItemSI.getRowCount();
            if(rows > 0){
                for(int i = 0; i<rows;i++)
                    if(((String)tbmMiscItemSI.getValueAt(i,0)).equals(selectedValue))
                        returnValue = (String)tbmMiscItemSI.getValueAt(i,1);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return returnValue;
    }
    
    //This is the corresponding mathod to the Button SaveCRIC Click Action event.
    public void btnSaveCRICActionEvent(String selectedValue,String enteredValue){
        try{
            
            int rows = tbmCRChargesInward.getRowCount();
            if(rows > 0){
                for(int i = 0; i<rows;i++){
                    if(((String)tbmCRChargesInward.getValueAt(i,0)).equals(selectedValue)){
                        tbmCRChargesInward.setValueAt(enteredValue,i,1);
                        return;
                    }
                }
            }
            List objList = new ArrayList();
            
            objList.add(selectedValue);
            objList.add(enteredValue);
            
            tbmCRChargesInward.addRow((ArrayList)objList);
        }catch(Exception e){
            e.printStackTrace();
        }
        return;
    }
    
    //This is the corresponding mathod to the Button SaveCROC Click Action event.
    public void btnSaveCROCActionEvent(String selectedValue, String enteredValue){
        try{
            int rows = tbmCRChargesOutward.getRowCount();
            if(rows > 0){
                for(int i = 0; i<rows;i++){
                    if(((String)tbmCRChargesOutward.getValueAt(i,0)).equals(selectedValue)){
                        tbmCRChargesOutward.setValueAt(enteredValue,i,1);
                        return;
                    }
                }
            }
            List objList = new ArrayList();
            
            objList.add(selectedValue);
            objList.add(enteredValue);
            
            tbmCRChargesOutward.addRow((ArrayList)objList);
        }catch(Exception e){
            e.printStackTrace();
        }
        return;
    }
    
    //This is the corresponding mathod to the Button SaveMISI Click Action event.
    public void btnSaveSIActionEvent(String selectedValue,String enteredValue){
        try{
            int rows = tbmMiscItemSI.getRowCount();
            if(rows > 0){
                for(int i = 0; i<rows;i++){
                    
                    if(((String)tbmMiscItemSI.getValueAt(i,0)).equals(selectedValue)){
                        tbmMiscItemSI.setValueAt(enteredValue,i,1);
                        return;
                    }
                }
            }
            List objList = new ArrayList();
            
            objList.add(selectedValue);
            objList.add(enteredValue);
            
            tbmMiscItemSI.addRow((ArrayList)objList);
        }catch(Exception e){
            e.printStackTrace();
        }
        return;
    }
    
    //This is the corresponding mathod to the Button DeleteCRIC Click Action event.
    void btnDeleteCRICActionEvent(String selectedValue) {
        try{
            int rows = tbmCRChargesInward.getRowCount();
            if(rows > 0){
                for(int i = 0; i<rows;i++){
                    if(((String)tbmCRChargesInward.getValueAt(i,0)).equals(selectedValue)){
                        tbmCRChargesInward.removeRow(i);
                        return;
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    //This is the corresponding mathod to the Button DeleteCROC Click Action event.
    void btnDeleteCROCActionEvent(String selectedValue) {
        try{
            int rows = tbmCRChargesOutward.getRowCount();
            if(rows > 0){
                for(int i = 0; i<rows;i++){
                    if(((String)tbmCRChargesOutward.getValueAt(i,0)).equals(selectedValue)){
                        tbmCRChargesOutward.removeRow(i);
                        return;
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    //This is the corresponding mathod to the Button DeleteMISI Click Action event.
    void btnDeleteSIActionEvent(String selectedValue) {
        try{
            int rows = tbmMiscItemSI.getRowCount();
            if(rows > 0){
                for(int i = 0; i<rows;i++){
                    if(((String)tbmMiscItemSI.getValueAt(i,0)).equals(selectedValue)){
                        tbmMiscItemSI.removeRow(i);
                        return;
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /**
     * Getter for property txtCreditInterestAmt.
     * @return Value of property txtCreditInterestAmt.
     */
    public java.lang.String getTxtCreditInterestAmt() {
        return txtCreditInterestAmt;
    }
    
    /**
     * Setter for property txtCreditInterestAmt.
     * @param txtCreditInterestAmt New value of property txtCreditInterestAmt.
     */
    public void setTxtCreditInterestAmt(java.lang.String txtCreditInterestAmt) {
        this.txtCreditInterestAmt = txtCreditInterestAmt;
    }
    
}
