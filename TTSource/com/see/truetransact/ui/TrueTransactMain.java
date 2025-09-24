/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * Use 
 *
 * TrueTransact.java
 *
 * Created on September 6, 2003, 6:08 AM
 */
package com.see.truetransact.ui;

/**
 * @author balachandar
 */
import java.io.File;
import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.sql.Timestamp;
import java.awt.Dimension;
import java.awt.Toolkit;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import com.see.truetransact.ui.transaction.agentCommisionDisbursal.AgentCommisionDisbursalUI;
import com.see.truetransact.ui.transaction.dailyDepositTrans.DailyDepositTransUI;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientUtil;

import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientproxy.ProxyParameters;

import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uicomponent.CDialog;
import com.see.truetransact.uicomponent.CFrame;

import com.see.truetransact.ui.product.operativeacct.OperativeAcctProductnewUI;
import com.see.truetransact.ui.product.loan.NewLoanProductUI;
import com.see.truetransact.ui.product.loan.agricultureCard.AgriCardUI;
import com.see.truetransact.ui.product.loan.agricultureCard.NewLoanAgriProductUI;
import com.see.truetransact.ui.product.advances.AdvancesProductUI;
import com.see.truetransact.ui.product.remittance.RemittanceProductUI;
import com.see.truetransact.ui.product.deposits.DepositsProductUI;
import com.see.truetransact.ui.product.bills.BillsUI;
import com.see.truetransact.ui.product.share.ShareProductUI;
import com.see.truetransact.ui.product.investments.InvestmentsProductUI;

//import com.see.truetransact.ui.advances.AdvancesODCC;
import com.see.truetransact.ui.transaction.auditEntry.AuditEntryUI;
import com.see.truetransact.ui.customer.SHGCustomerUI;

import com.see.truetransact.ui.customer.IndividualCustUI;
import com.see.truetransact.ui.customer.HistoryViewerUI;
import com.see.truetransact.ui.customer.CorporateCustomerUI;
import com.see.truetransact.ui.customer.deathmarking.DeathMarkingUI;
import com.see.truetransact.ui.customer.security.SecurityInsuranceUI;
import com.see.truetransact.ui.agent.AgentUI;
import com.see.truetransact.ui.customer.customeridchange.CustomerIdChangeUI;
import com.see.truetransact.ui.customer.gahan.GahanCustomerUI;

import com.see.truetransact.ui.transaction.denominationConfiguration.DenominationConfigurationUI;
import com.see.truetransact.ui.transaction.reprintScreen.ReprintUI;
import com.see.truetransact.ui.transaction.cash.CashTransactionUI;
import com.see.truetransact.ui.transaction.transfer.TransferUI;
import com.see.truetransact.ui.transaction.clearing.InwardClearingUI;
import com.see.truetransact.ui.transaction.clearing.outward.OutwardClearingUI;
import com.see.truetransact.ui.transaction.cashmanagement.CashManagementUI;

import com.see.truetransact.ui.transaction.token.tokenconfig.TokenConfigUI;
import com.see.truetransact.ui.transaction.token.tokenissue.TokenIssueUI;
import com.see.truetransact.ui.transaction.token.tokenloss.TokenLossUI;
import com.see.truetransact.ui.transaction.chargesServiceTax.ChargesServiceTaxUI;
import com.see.truetransact.ui.generalledger.HeadUI;
import com.see.truetransact.ui.generalledger.AccountCreationUI;
import com.see.truetransact.ui.generalledger.AccountMaintenanceUI;
import com.see.truetransact.ui.generalledger.GLOpeningUpdateUI;
import com.see.truetransact.ui.generalledger.branchgl.BranchGLUI;
import com.see.truetransact.ui.generalledger.gllimit.GLLimitUI;

import com.see.truetransact.ui.common.viewall.TableDataUI;
import com.see.truetransact.ui.common.denomination.DenominationUI;
import com.see.truetransact.ui.common.interestcalc.InterestCalculationUI;

import com.see.truetransact.ui.operativeaccount.AccountsUI;
import com.see.truetransact.ui.operativeaccount.AccountTransferUI;
import com.see.truetransact.ui.operativeaccount.LienMarkingUI;
import com.see.truetransact.ui.operativeaccount.FreezeUI;
import com.see.truetransact.ui.operativeaccount.ChargesUI;
import com.see.truetransact.ui.operativeaccount.InterestUI;
import com.see.truetransact.ui.operativeaccount.AccountClosingUI;
import com.see.truetransact.ui.operativeaccount.TodAllowedUI;
//import com.see.truetransact.ui.operativeaccount.deathmarking.AccountDeathMarkingUI;
import com.see.truetransact.ui.termloan.GoldLoanUI;
import com.see.truetransact.ui.termloan.TermLoanUI;
import com.see.truetransact.ui.termloan.depositLoan.DepositLoanUI;
import com.see.truetransact.ui.termloan.goldLoanConfiguration.GoldConfigurationUI;
import com.see.truetransact.ui.termloan.agritermloan.AgriTermLoanUI;
import com.see.truetransact.ui.termloan.drawingpower.DrawingPowerMaintenanceUI;
import com.see.truetransact.ui.termloan.emicalculator.TermLoanInstallmentUI;
import com.see.truetransact.ui.customer.CheckCustomerIdUI;
import com.see.truetransact.ui.termloan.NPA.NPAApplicationUI;
import com.see.truetransact.ui.termloan.guarantee.GuaranteeMasterUI;
import com.see.truetransact.ui.termloan.riskfund.RiskFundUI;

import com.see.truetransact.ui.bills.lodgement.LodgementBillsUI;
import com.see.truetransact.ui.bills.CarrierUI;
import com.see.truetransact.ui.bills.lodgement.CollectingBankDetailsEntryUI;

import com.see.truetransact.ui.supporting.inventory.InventoryMasterUI;
import com.see.truetransact.ui.supporting.inventory.InventoryDetailsUI;

import com.see.truetransact.ui.deposit.TermDepositUI;
import com.see.truetransact.ui.deposit.freeze.DepositFreezeUI;
import com.see.truetransact.ui.deposit.lien.DepositLienUI;
import com.see.truetransact.ui.deposit.tds.TdsDeductionUI;
import com.see.truetransact.ui.deposit.closing.DepositClosingUI;
import com.see.truetransact.ui.deposit.interestmaintenance.InterestMaintenanceUI;
import com.see.truetransact.ui.deposit.recalculationofdepositinterest.RecalculationOfDepositInterestUI;
//import com.see.truetransact.ui.deposit.transferringtoMatured.TransferringtoMaturedUI;

import com.see.truetransact.ui.clearing.tally.InwardClearingTallyUI;
import com.see.truetransact.ui.clearing.outwardtally.OutwardClearingTallyUI;
import com.see.truetransact.ui.clearing.bouncing.BouncingInstrumentwiseUI;
import com.see.truetransact.ui.clearing.returns.ReturnOfInstrumentsUI;
import com.see.truetransact.ui.clearing.banklevel.BankClearingParameterUI;
import com.see.truetransact.ui.clearing.ParameterUI;
import com.see.truetransact.ui.clearing.clearingData.ClearingDataImportUI;

import com.see.truetransact.ui.login.*;
import com.see.truetransact.ui.login.newpasswd.ChangePasswordUI;
import com.see.truetransact.ui.forex.*;

import com.see.truetransact.ui.sysadmin.user.UserUI;
import com.see.truetransact.ui.sysadmin.bank.BankUI;
import com.see.truetransact.ui.sysadmin.group.GroupUI;
import com.see.truetransact.ui.sysadmin.group.GroupOB;
import com.see.truetransact.ui.sysadmin.branchgroup.*;
import com.see.truetransact.ui.sysadmin.branchgroupscr.BranchGroupUI;
import com.see.truetransact.ui.sysadmin.group.ScreenModuleTreeNode;
import com.see.truetransact.ui.sysadmin.view.ViewLogUI;
import com.see.truetransact.ui.sysadmin.branch.BranchManagementUI;
import com.see.truetransact.ui.sysadmin.role.RoleUI;
import com.see.truetransact.ui.sysadmin.tools.PingAllUI;
import com.see.truetransact.ui.payroll.employee.EmployeeUI;
import com.see.truetransact.ui.payroll.salaryStructure.PayrollSalaryStructureUI;
import com.see.truetransact.ui.payroll.SalaryProcess.SalaryProcessUI;
import com.see.truetransact.ui.payroll.earningsDeductionGlobal.EarningsDeductionUI;
import com.see.truetransact.ui.payroll.pfMaster.PFMasterUI;
import com.see.truetransact.ui.payroll.leaveDetails.LeaveDetailsUI;
import com.see.truetransact.ui.payroll.leaveMaster.LeaveMasterUI;
import com.see.truetransact.ui.passbookDataEntry.PassbookDataEntryUI;
import com.see.truetransact.ui.sysadmin.terminal.TerminalUI;
import com.see.truetransact.ui.sysadmin.levelcontrol.LevelControlUI;
import com.see.truetransact.ui.sysadmin.levelcontrol.multilevel.MultiLevelControlUI;
import com.see.truetransact.ui.sysadmin.calender.CalenderHolidaysUI;
import com.see.truetransact.ui.sysadmin.branchacnomaintenance.AcNoMaintenanceUI;
import com.see.truetransact.ui.sysadmin.lookup.LookupMasterUI;
import com.see.truetransact.ui.sysadmin.createscreens.CreateScreensUI;
import com.see.truetransact.ui.sysadmin.audit.InspectionUI;
import com.see.truetransact.ui.sysadmin.otherbank.OtherBankUI;
import com.see.truetransact.ui.sysadmin.view.ViewLogImportanceUI;
import com.see.truetransact.ui.sysadmin.suspiciousconfig.SuspiciousConfigUI;
import com.see.truetransact.ui.sysadmin.denomination.ForexDenominationMasterUI;
import com.see.truetransact.ui.sysadmin.blockedlist.BlockedListUI;
import com.see.truetransact.ui.sysadmin.config.ConfigurationUI;
import com.see.truetransact.ui.sysadmin.stateTalukMaster.StateTalukUI;
import com.see.truetransact.ui.sysadmin.noticereportparameters.NoticePeriodUI;
import com.see.truetransact.commonutil.CommonUtil;
//import com.see.truetransact.ui.bills.TermLoanUI;

import com.see.truetransact.ui.generalledger.glentry.GLEntryUI;

import com.see.truetransact.ui.remittance.RemittancePaymentUI;
import com.see.truetransact.ui.remittance.RemittanceIssueUI;
import com.see.truetransact.ui.remittance.remitstoppayment.RemitStopPaymentUI;

import com.see.truetransact.ui.supporting.chequebook.ChequeBookUI;
import com.see.truetransact.ui.supporting.standinginstruction.StandingInstructionUI;
import com.see.truetransact.ui.supporting.standingInstructionDaily.StandingInstructionDailyUI;
import com.see.truetransact.ui.supporting.InventoryMovement.InventoryMovementUI;
import com.see.truetransact.ui.supporting.chequebook.ActNumEnqByChqNoUI;

import com.see.truetransact.ui.batchprocess.DayBeginProcessUI;
import com.see.truetransact.ui.batchprocess.DayEndProcessUI;
import com.see.truetransact.ui.batchprocess.DCDayEndProcessUI;
import com.see.truetransact.ui.batchprocess.DCDayBeginProcessUI;

import com.see.truetransact.ui.tds.tdsconfig.TDSConfigUI;
import com.see.truetransact.ui.tds.tdsexemption.TDSExemptionUI;

import com.see.truetransact.ui.privatebanking.orders.DepositRolloverUI;
import com.see.truetransact.ui.privatebanking.orders.details.DepositRolloverDetailsUI;
import com.see.truetransact.ui.privatebanking.actionitem.ftinternal.FTInternalUI;
import com.see.truetransact.ui.privatebanking.actionitem.externalwire.ExternalWireUI;
import com.see.truetransact.ui.privatebanking.actionitem.purchaseequities.PurchaseEquitiesUI;
import com.see.truetransact.ui.privatebanking.actionitem.newtimedeposit.NewTimeDepositUI;
import com.see.truetransact.ui.privatebanking.tammaintenance.TAMMaintenanceCreateUI;
import com.see.truetransact.ui.privatebanking.comlogs.relationships.RelationshipsUI;
import com.see.truetransact.ui.investments.InvestmentsMasterUI;
import com.see.truetransact.ui.investments.CallMoneyUI;
import com.see.truetransact.ui.investments.CallMoneyExtensionUI;
import com.see.truetransact.ui.investments.InvestmentsTransUI;
import com.see.truetransact.ui.investments.InvestmentsAmortizationUI;
import com.see.truetransact.ui.scheduler.ScheduleTaskUI;
import com.see.truetransact.ui.locker.lockerconfig.LockerConfigUI;
import com.see.truetransact.ui.locker.lockeroperation.LockerOperationUI;
import com.see.truetransact.ui.locker.lockerissue.LockerIssueUI;
import com.see.truetransact.ui.locker.lockersurrender.LockerSurrenderUI;

import com.see.truetransact.ui.share.DrfRecoveryUI;

import com.see.tools.workflow.ui.WorkflowUI;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.ui.share.NmfMaintenanceUI;
import com.see.truetransact.ui.share.ShareUI;
import com.see.truetransact.ui.share.sharetransfer.ShareTransferUI;
import com.see.truetransact.ui.share.shareresolution.ShareResolutionUI;
import com.see.truetransact.ui.remittance.printingdetails.PrintUI;
import com.see.truetransact.ui.product.loan.loaneligibilitymaintenance.LoanEligibilityMaintenanceUI;
//import com.see.truetransact.ui.termloan.appraiserCommision.AppraiserCommisionUI;
//import com.see.truetransact.ui.locker.lockerissue.LockerIssueUI;

import com.see.truetransact.transferobject.sysadmin.config.ConfigPasswordTO;
import com.see.truetransact.ui.bills.lodgement.multipleaccountlodgement.MultipleAccountLodgementUI;
import com.see.truetransact.ui.common.customer.malayalam.MalayalamDetailsUI;
import com.see.truetransact.ui.customer.SmartCustomerUI;
import com.see.truetransact.ui.deposit.CheckCustomerDepositsUI;
import com.see.truetransact.ui.deposit.multipledeposit.MultipleTermDepositUI;
import com.see.truetransact.ui.dueproducts.DisplayDueProductsUI;
import com.see.truetransact.ui.product.commission.DailyCommissionUI;
import com.see.truetransact.ui.servicetax.servicetaxSettings.ServiceTaxSettingsUI;

import com.see.truetransact.ui.trading.shopmaster.ShopMasterUI;
import com.see.truetransact.ui.trading.tradingsuppliermaster.TradingSupplierMasterUI;
import com.see.truetransact.ui.trading.tradingsales.TradingSalesUI;
import com.see.truetransact.ui.trading.damage.DamageUI;
import com.see.truetransact.ui.trading.tradingachead.TradingAcHeadUI;
import com.see.truetransact.ui.trading.tradinggroup.TradingGroupUI;
import com.see.truetransact.ui.trading.tradingproduct.TradingProductUI;
//import com.see.truetransact.ui.trading.purchase.PurchaseUI;
import com.see.truetransact.ui.trading.tradingpurchase.TradingPurchaseUI;
import com.see.truetransact.ui.trading.tradingsales.TradingSalesUI;
import com.see.truetransact.ui.trading.tradingstock.TradingStockUI;
import com.see.truetransact.ui.trading.tradingtransfer.TradingTransferUI;
import com.see.truetransact.ui.trading.purchaseentry.PurchaseEntryUI;
import com.see.truetransact.ui.common.viewall.QRCodeUI;
import com.see.truetransact.ui.indend.reservedepreciationachd.ReserveDepreciationAcHdUI;
import com.see.truetransact.ui.remittance.rtgs.upi.UPIQRCodeUI;

import com.see.truetransact.uicomponent.CComboBox;
import java.awt.Color;
import java.awt.Container;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.Timer;

/**
 * This is the Main class. It calls all other screens based on the Login.
 */
public class TrueTransactMain extends com.see.truetransact.uicomponent.CFrame {

    /**
     * User Info variable. After login authendication it will be filled
     */
    public static HashMap USERINFO;                 // Static UserInfo variable which is used in all other screens
    /**
     * BranchInfo map. After login authentication, this will be filled
     */
    public static HashMap BRANCHINFO;                // Static BranchInfo variable which is used in all other screens
    /**
     * BankInfo map. After login authentication, this will be filled
     */
    public static HashMap BANKINFO;                 // Static BranchInfo variable which is used in all other screens
    /**
     * RoleInfo map. After login authentication, this will be filled
     */
    public static HashMap ROLEINFO;                 // Static RoleInfo variable which is used in all other screens
    public static HashMap LEVEL_ID;

    public static HashMap getLEVEL_ID() {
        return LEVEL_ID;
    }

    public static void setLEVEL_ID(HashMap LEVEL_ID) {
        TrueTransactMain.LEVEL_ID = LEVEL_ID;
    }
    public static int CASH_CREDIT;
    public static int CASH_DEBIT;
    public static int TRANS_CREDIT;
    public static int TRANS_DEBIT;
    public static String USER_ID;                   // User ID
    public static String BRANCH_ID;                 // Branch ID
    public static String BANK_ID;                   // Branch ID
    //public static String HIERARCHY_ID;              // Hierarchy ID
    public static int HIERARCHY_ID;              // Hierarchy ID
    public static String FOREIGN_BRANCH_GROUP;      // Foreign Branch Group
    public static String FOREIGN_GROUP_ID;
    public static String ACT_NUM;
    public static String CASHIER_AUTH_ALLOWED;//CASHIER_AUTH_ALLOWED
    public static String GROUP_ID;
    public static String ROLE_ID;
    private static Date applicationDate;
    private static javax.swing.JMenu mnuWin;
    public static String selBranch;
    public static String SHIFT;
    public static String MULTI_SHARE_ALLOWED;
    public static String TOKEN_NO_REQ = "N";
    public static String SERVICE_TAX_REQ = "N";
    public static int GAHAN_PERIOD = 13; 
    public static int SENIOR_CITIZEN_AGE = 60; 
    public static Date amcFromDate = null;
	public static Date amcToDate = null;
    //    private String userName = null;                  // Logged in User Name
    final private HashMap screens = new HashMap();  // To Check loaded screens
    final private int frameDistance = 30;             // frame Distance in pixcels
    private JMenuItem mnuWindowItem = null;         // Dynamic menu for loaded screens
    private boolean interbranchAllowed = false;
    private boolean setRenderer = false;
    private String dayEndType = "";
    private String currLoginTime = "";
    public double panAmount;
    public static double PANAMT = 0;
    private ArrayList goldLoanList = new ArrayList();
    public static String language;
    public static String country;
    private java.awt.event.AWTEventListener globalKeyListener;
    private boolean shownAuthorizeMessage = false;
    public static boolean alreadyOpen = false;
    TermLoanInstallmentUI emicalc = null;
    private HashMap operationMap;
    private ProxyFactory proxy;
    Timer time;
    javax.swing.Timer amcTimer;
    AmcDisposer amcDisposer = new AmcDisposer();
    public static int amcAlertTime = 0;
    private boolean isCalledLoginExpiry = false;
    public static int pendingTxnAllowedDays = 0;
    TTAction ttAction;
    //merged from mandia by rishad
    javax.swing.Timer timer,timer1;
    public static javax.swing.Timer timer2;
    Disposer disposer = new Disposer();
    Disposer1 disposer1 = new Disposer1();
    public static Disposer2 disposer2 = new Disposer2();
    public static double logOutTime = 0;
    HashMap dbBackupResultMap = new HashMap();
    
    public static HashMap CBMSPARAMETERS;
    public String screenName = ""; //Added By Kannan AR
    public String moduleName = "";
    public void setCBMSPARAMETERS(HashMap CBMS_PARAMETERS) {
        CBMSPARAMETERS = CBMS_PARAMETERS;
    }
    /**
     * Creates new TrueTransact Constructor which addes all the components and
     * settingup the initial things.
     */
    public TrueTransactMain() {
        initComponents();
        mnuWin = new javax.swing.JMenu();
        mnuWin = mnuWindow;
        mitExportDBBackup.setVisible(false);
        mitDownload.setVisible(false);
        sptLogin.setVisible(false);
        mitDisconnect.setVisible(false);
        lblBranch.setVisible(false);
        lblUser.setVisible(false);
        setOperationMap();
//        setScheduledSms();   
        setAmcAlert();
        LoanMdsInstallmentRemider();
        appDateAlert();
        dbBackupResultMap = dbbackupActivity();
        //added by rishad 10-12-2015
        cboBranchList.setUI(new javax.swing.plaf.metal.MetalComboBoxUI(){
        public void layoutComboBox(Container parent, MetalComboBoxLayoutManager manager) {
        super.layoutComboBox(parent, manager);
        arrowButton.setBounds(0,0,0,0);
      }
    });
        //end
    }

    public void startMain() {
        shownAuthorizeMessage = false;
        alreadyOpen = false;
        initStartup();
        createTree();
        setTreeRenderer();
        populateBranches();

//        globalKeyListener();
//        Toolkit.getDefaultToolkit().addAWTEventListener(
//            globalKeyListener, java.awt.AWTEvent.KEY_EVENT_MASK);

//        Toolkit.getDefaultToolkit().addAWTEventListener(new java.awt.event.AWTEventListener()
//        {
//            public void eventDispatched(java.awt.AWTEvent event)
//            {
//                String eventText = event.toString();
//                if(eventText.indexOf("PRESSED") != -1 || eventText.indexOf("RELEASED") != -1)
//                {
////                    System.out.println("#### eventText : "+eventText);
//                    showLoginExpiry();
//                }
//            }
//        }, java.awt.AWTEvent.MOUSE_EVENT_MASK + java.awt.AWTEvent.KEY_EVENT_MASK);
        
        Toolkit.getDefaultToolkit().addAWTEventListener(new java.awt.event.AWTEventListener() {
        public void eventDispatched(java.awt.AWTEvent event) {
                String eventText = event.toString();
                if (event instanceof java.awt.event.KeyEvent) {
                    java.awt.event.KeyEvent keyEvent = (java.awt.event.KeyEvent) event;
//                    if (keyEvent.isControlDown() && keyEvent.isAltDown()
//                            && keyEvent.getKeyText(keyEvent.getKeyCode()).equals("L") && !isCalledLoginExpiry) {
//                        LoginExpiry();
//                    }
                    // Commented by nithya on 15-04-2016 for 3146 : Linux issue - 
                    if((keyEvent.getKeyCode() == KeyEvent.VK_CONTROL) && (keyEvent.getKeyCode() == KeyEvent.VK_ALT) && (keyEvent.getKeyCode() == KeyEvent.VK_L) && !isCalledLoginExpiry){
                     LoginExpiry();  
                    }
                    else  if (keyEvent.isControlDown()&& keyEvent.getKeyText(keyEvent.getKeyCode()).equals("S")) {
                        txtSmartCustomer.grabFocus();
                    }
                }
            }
        }, java.awt.AWTEvent.KEY_EVENT_MASK);
         ttAction = new TTAction();
         dtpTTMain.getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(javax.swing.KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.ALT_MASK | InputEvent.CTRL_MASK, true), "UserLock");
         dtpTTMain.getInputMap(javax.swing.JComponent.WHEN_FOCUSED).put(javax.swing.KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.ALT_MASK | InputEvent.CTRL_MASK, true), "UserLock");
        dtpTTMain.getActionMap().put("UserLock", ttAction);
        //merged from mandiya by rishad
        long mask = java.awt.AWTEvent.MOUSE_EVENT_MASK | java.awt.AWTEvent.KEY_EVENT_MASK;
        Toolkit.getDefaultToolkit().addAWTEventListener(new Listener(), mask);
        startAutoLogoutTimer();
        startAutoLogoutTimer1();
    }
   
    public void setOperationMap() {
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "SmsConfigJNDI");
        operationMap.put(CommonConstants.HOME, "sms.SmsConfigHome");
        operationMap.put(CommonConstants.REMOTE, "sms.SmsConfig");
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
//    public void setScheduledSms() {
//        Timer time = new Timer();
//        if (smsAllowed != null && smsAllowed.equals("Y")) {
//            time.scheduleAtFixedRate(new smsTimer(), (1800000), (1800000));
////            time.scheduleAtFixedRate(new smsTimer(), (1000), (1000));
//        }
//    }
    
    public void setAmcAlert() {
        if (amcToDate != null && amcAlertTime >0 && DateUtil.dateDiff(amcToDate, ClientUtil.getCurrentDate())>0) {
            amcTimer = new javax.swing.Timer((int) amcAlertTime * 60000, amcDisposer);
            amcTimer.start();
        }        
    }    
    
    public void appDateAlert() {
       Date date = new Date();
//       System.out.println("date$@$@#$#@$"+date);
//       System.out.println("date$@$@#$#@$"+ClientUtil.getCurrentDate());
       if (DateUtil.dateDiff(date, ClientUtil.getCurrentDate())!=0) {
            dtpTTMain.setBackground(new Color(0, 255, 204));
            com.see.truetransact.uicomponent.COptionPane.showMessageDialog(null, "Mismatch in system date and CBMS application date,Please check!!!");
       } else {
           dtpTTMain.setBackground(new Color(51, 204, 255));
       }        
    }  
    
    class AmcDisposer implements java.awt.event.ActionListener {
        public void actionPerformed(java.awt.event.ActionEvent e) {
            com.see.truetransact.uicomponent.COptionPane.showMessageDialog(null, "Product usage period is over, Please contact Administrator");
            if (amcTimer != null) {
                amcTimer.start();
                amcTimer = null;
            }
        }
    }
    
    public void LoanMdsInstallmentRemider() {
//        if (smsAllowed != null && smsAllowed.equals("Y")) {
            HashMap instMap = new HashMap();
            instMap.put("LOAN_MDS_INSTALLMENT", "LOAN_MDS_INSTALLMENT");
            instMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            try {
                proxy.execute(instMap, operationMap);
            } catch (Exception ex) {
                Logger.getLogger(TrueTransactMain.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            }
//        } else {
//        }
    }
    
    public class smsTimer extends TimerTask {       
        public void run() {
            //dtpTTMain.setBackground(Color.red);
            HashMap smsMap = new HashMap();
            smsMap.put("SMS_SCHEDULE","SMS_SCHEDULE");
            smsMap.put(CommonConstants.BRANCH_ID,TrueTransactMain.BRANCH_ID);
            try {
                proxy.execute(smsMap, operationMap);
                //dtpTTMain.setBackground(Color.BLACK);
            } catch (Exception ex) {
                Logger.getLogger(TrueTransactMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
    }   
    
    private void globalKeyListener() {
        globalKeyListener = new java.awt.event.AWTEventListener() {

            public void eventDispatched(java.awt.AWTEvent event) {
                String eventText = event.toString();
                if (event instanceof java.awt.event.KeyEvent) {
                    java.awt.event.KeyEvent keyEvent = (java.awt.event.KeyEvent) event;
//                        System.out.println("#### keyEvent.getKeyCode() : "+keyEvent.getKeyCode());
//                        System.out.println("#### keyEvent.getKeyText(123) : "+keyEvent.getKeyText(123));
                    if (keyEvent.getKeyText(keyEvent.getKeyCode()).equals("F12") && eventText.indexOf("PRESSED") != -1) {
//                            System.out.println("#### true : ");
                        System.out.println("#### true : " + dtpTTMain.getSelectedFrame());
                        CInternalFrame frm = null;
                        if (dtpTTMain.getSelectedFrame() != null && dtpTTMain.getSelectedFrame() instanceof CInternalFrame) {
                            frm = (CInternalFrame) dtpTTMain.getSelectedFrame();
                            frm.callInternationalize();
                        }

//                            switchEnglish = !switchEnglish;
//                            internationalize();

                    }
                }

            }
        };
    }
     public void startAutoLogoutTimer() {
           timer = new javax.swing.Timer((int) logOutTime * 60000, disposer);
           timer.start();
        isCalledLoginExpiry = false;
//        System.out.println("Started timer:"+new Date());
    }
    
    public void startAutoLogoutTimer1() {
          timer1 = new javax.swing.Timer((int) 1000, disposer1);
          timer1.start();
//        System.out.println("Started timer1 :"+new Date());
    }

    public static void startAutoLogoutTimerSessionExpiry() {
        com.see.truetransact.uicomponent.COptionPane.showMessageDialog(null, "Invalid Session (or) Session Timed out. Please Login Again.");
        sessionmakeLogOut();
    }
    class TTAction implements javax.swing.Action {
        public void addPropertyChangeListener(java.beans.PropertyChangeListener arg0) {
            //System.out.println("addPropertyChangeListener: \"U\" has been typed!");
        }
        public Object getValue(String arg0) {
            //System.out.println("getValue: \"U\" has been typed!");
            return null;
        }
        public boolean isEnabled() {
//            System.out.println("isEnabled: \"U\" has been typed!");  
            LoginExpiry();
            return false;
        }
        public void putValue(String arg0, Object arg1) {
            //System.out.println("putValue: \"U\" has been typed!");
        }
        public void removePropertyChangeListener(java.beans.PropertyChangeListener arg0) {
            //System.out.println("removePropertyChangeListener: \"U\" has been typed!");
        }
        public void setEnabled(boolean arg0) {
            //System.out.println("set enabled: \"U\" has been typed!");
        }
        public void actionPerformed(java.awt.event.ActionEvent arg0) {
//            System.out.println("action performed: \"Ctrl+Alt+L\" has been typed!");  
            LoginExpiry();
        }
    }
    
    private void LoginExpiry() {
         if (timer != null) {
             timer.stop();
            timer = null;
        }
        isCalledLoginExpiry = true;
        try {
            LoginExpiryUI loginExpiryUI = new LoginExpiryUI(this);
            this.showDialog(loginExpiryUI);
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
     class Listener implements java.awt.event.AWTEventListener {

        boolean is(int id, int mask) {
            return (id & mask) == mask;
        }

        public void eventDispatched(java.awt.AWTEvent event) {
            int id = event.getID();
//            System.out.println(id+" = "+java.awt.event.MouseEvent.MOUSE_ENTERED);
            if (is(id, java.awt.event.MouseEvent.MOUSE_ENTERED) || is(id, java.awt.event.MouseEvent.MOUSE_EXITED)) {
                return;
            }
            if (timer != null) {
                  timer.restart();
//                System.out.println("restarted timer:"+new Date());
            }
        }
    }
     
       class Disposer implements java.awt.event.ActionListener {

        public void actionPerformed(java.awt.event.ActionEvent e) {
            com.see.truetransact.uicomponent.COptionPane.showMessageDialog(null, "Session Timed out. Please Login Again.");
            makeLogOut();
            if (timer != null) {
                  timer.stop();
                timer = null;
            }
//            System.out.println("timer stopped:"+new Date());
        }
    }
    
   static  class Disposer2 implements java.awt.event.ActionListener {

        public void actionPerformed(java.awt.event.ActionEvent e) {
            com.see.truetransact.uicomponent.COptionPane.showMessageDialog(null, "Invalid Session (or) Session Timed out. Please Login Again.");
            sessionmakeLogOut();
            if (timer2 != null) {
                  timer2.stop();
                timer2 = null;
            }
//            System.out.println("timer stopped:"+new Date());
        }
    }

    class Disposer1 implements java.awt.event.ActionListener {
        public void actionPerformed(java.awt.event.ActionEvent e) {
            if (ProxyParameters.BRANCH_ID == null && BRANCH_ID == null) {
                com.see.truetransact.uicomponent.COptionPane.showMessageDialog(null, "Session Timed out. Please Login Again.");
                makeLogOut();
                if (timer1 != null) {
                      timer1.stop();
                    timer1 = null;
                }
            } else {
                if (BRANCH_ID != null) {
                    ProxyParameters.USER_ID = USER_ID;
                    ProxyParameters.BRANCH_ID = BRANCH_ID;
                    ProxyParameters.BANK_ID = BANK_ID;
                    ProxyParameters.HIERARCHY_ID = HIERARCHY_ID;
                }
            }
        }
    }
    
    private void showLoginExpiry() {
        long currentTime = new Date().getTime();
        if (ProxyParameters.lastAccessTime > 0
                && (currentTime > (ProxyParameters.lastAccessTime
                + CommonUtil.convertObjToInt(CommonConstants.TIME_OUT) * 60 * 1000))) {
            System.out.println("#### not worked time if part : " + ((currentTime - ProxyParameters.lastAccessTime) / 1000) / 60);
            ProxyParameters.lastAccessTime = currentTime;
            com.see.truetransact.uicomponent.COptionPane.showMessageDialog(null, "Session Timed out. Please Login Again.");
            try {
                LoginExpiryUI loginExpiryUI = new LoginExpiryUI(this);
                this.showDialog(loginExpiryUI);
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        } else {
            ProxyParameters.lastAccessTime = currentTime;
        }

    }

    public static void populateBranches() {
        HashMap param = new HashMap();
        param.put(CommonConstants.MAP_NAME, "getOwnBranches");
        HashMap where = new HashMap();
        where.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
        where.put("BRANCH_GROUP", FOREIGN_BRANCH_GROUP);
        param.put(CommonConstants.PARAMFORQUERY, where);
        where = null;
        HashMap keyValue = (HashMap) ClientUtil.populateLookupData(param).get(CommonConstants.DATA);
        ComboBoxModel cbmBranches = new ComboBoxModel((ArrayList) keyValue.get(CommonConstants.KEY), (ArrayList) keyValue.get(CommonConstants.VALUE));
        cboBranchList.setModel(cbmBranches);

        cboBranchList.setSelectedItem(((ComboBoxModel) cboBranchList.getModel()).getDataForKey(BRANCH_ID));
    }

    /*
     * Initial Setup for tree
     */
    private void initStartup() {
        // Getting User Name from the Map
        //        userName = (String) USERINFO.get(CommonConstants.USER_ID);
        ProxyParameters.lastAccessTime = (new java.util.Date()).getTime();
        USER_ID = (String) USERINFO.get(CommonConstants.USER_ID);
        BRANCH_ID = (String) BRANCHINFO.get(CommonConstants.BRANCH_ID);
        SHIFT = (String) BRANCHINFO.get("SHIFT");
        BANK_ID = (String) BANKINFO.get("BANK_CODE");

        GROUP_ID = (String) USERINFO.get("USER_GROUP");
        ROLE_ID = (String) USERINFO.get("USER_ROLE");
        CASH_CREDIT = CommonUtil.convertObjToInt(LEVEL_ID.get("CASH_CREDIT"));
        CASH_DEBIT = CommonUtil.convertObjToInt(LEVEL_ID.get("CASH_DEBIT"));
        TRANS_CREDIT = CommonUtil.convertObjToInt(LEVEL_ID.get("TRANS_CREDIT"));
        TRANS_DEBIT = CommonUtil.convertObjToInt(LEVEL_ID.get("TRANS_DEBIT"));
        //System.out.println("CASH_CREDITCASH_CREDIT:::"+CASH_CREDIT);
        //System.out.println("CASH_DEBIT:::"+CASH_DEBIT);
        //System.out.println("TRANS_CREDIT:::::"+TRANS_CREDIT);
        //System.out.println("TRANS_DEBIT::::"+TRANS_DEBIT);

        FOREIGN_BRANCH_GROUP = CommonUtil.convertObjToStr(USERINFO.get("FOREIGN_BRANCH_GROUP"));
        FOREIGN_GROUP_ID = CommonUtil.convertObjToStr(USERINFO.get("FOREIGN_GROUP_ID"));

        //        GROUP_ID = "GRP01";
        //        ROLE_ID = "ROL02";
        //HIERARCHY_ID = String.valueOf(CommonUtil.convertObjToInt(ROLEINFO.get("HIERARCHY_ID")));

        HIERARCHY_ID = CommonUtil.convertObjToInt(ROLEINFO.get("HIERARCHY_ID"));
        System.out.println("HIERARCHY_ID:" + HIERARCHY_ID);
//        mnuBankName.setText("<html>Branch : <font color=blue><B>" + BRANCH_ID + "</B> </font>&nbsp;&nbsp;&nbsp;&nbsp;User : <B><font color=blue>" + USER_ID + "</B></font>");
        Date lastLogoutDt = (Date) USERINFO.get("LAST_LOGOUT_DT");
        Date currLoginDt = (Date) USERINFO.get("CURR_DATE");
        String lastLogoutTime = "";
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        if (lastLogoutDt != null) {
            lastLogoutTime = dateFormat.format(lastLogoutDt);
        }
        currLoginTime = dateFormat.format(currLoginDt);
        mnuBankName.setText("<html>Last logout time : " + lastLogoutTime + "&nbsp;&nbsp;"
                + "&nbsp;&nbsp;Current login time : " + currLoginTime + "&nbsp;&nbsp;"
                + "&nbsp;&nbsp;Branch : <font color=blue><B>"
                + BRANCH_ID + "</B> </font>&nbsp;&nbsp;User : <B><font color=blue>" + USER_ID + "</B></font>&nbsp;Shift :<B><font color=blue>"
                + SHIFT + /*"</B></font>&nbsp;&nbsp;Application Lock : <B><font color=blue>"
                + "Ctrl+Alt+L" + "</B></font>*/"</html>");
        mnuBankName.remove(jMenuItem1);

        ProxyParameters.USER_ID = USER_ID;
        ProxyParameters.BRANCH_ID = BRANCH_ID;
        TrueTransactMain.selBranch = BRANCH_ID;
        ProxyParameters.BANK_ID = BANK_ID;
        ProxyParameters.HIERARCHY_ID = HIERARCHY_ID;
        ProxyParameters.dbDriverName = (String) USERINFO.get("DB_DRIVER_NAME");
        ProxyParameters.SESSION_ID = (String) USERINFO.get(CommonConstants.SESSION_ID);
        ProxyParameters.HEAD_OFFICE = (String) USERINFO.get(CommonConstants.HEAD_OFFICE);
        selBranch = BRANCH_ID;

        System.out.println("GROUP " + GROUP_ID + " : ROLE_ID " + ROLE_ID);
        lblBranch.setText(" Home Branch : " + BRANCH_ID);
        lblUser.setText(" User : " + USER_ID);
        lblDate.setText("<html> Date : <font color=blue>" + DateUtil.getStringDate(ClientUtil.getCurrentDate()) + "</font></html>");
        //cLabel1.setText("");

        setTitle(getTitle() + " Ver : "+TrueTransactMain.CBMSPARAMETERS.get("RELEASE_VERSION")+" - " + (String) BANKINFO.get("BANK_NAME"));
        // loading TT Logo
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_logo_small.gif")).getImage());
        //babu
       /*
         * List list = (List)ClientUtil.executeQuery("getCashierAuthAllowed",
         * null); for(int i=0;i<list.size();i++) { HashMap
         * map=(HashMap)list.get(i);
         *
         * if(map.get("CASHIER_AUTH_ALLOWED")!=null) {
         * CASHIER_AUTH_ALLOWED=map.get("CASHIER_AUTH_ALLOWED").toString(); } }
         */
        //System.out.println("CASHIER_AUTH_ALLOWED-----===" + CASHIER_AUTH_ALLOWED);
    }

    public static javax.swing.JTree getTree() {
        return treModules;
    }

    private void createTree() {
        //       // OB
        //        if (userName.equalsIgnoreCase("sysadmin")) {
        //            createTreeManual();
        //        } else {
        HashMap map = new HashMap();
        map.put(CommonConstants.MAP_WHERE, GROUP_ID);
        GroupOB treeOB = new GroupOB(false);

        HashMap whereMap = new HashMap();
        whereMap.put("BRANCH_CODE", BRANCH_ID);
        String strStatus = CommonUtil.convertObjToStr(ClientUtil.executeQuery("chkTransactionAllowed", whereMap).get(0));
        ArrayList grantScreen = new ArrayList();
        whereMap.put("DAYBEGIN_DT", ClientUtil.getCurrentDate());
        List incompleteBran = ClientUtil.executeQuery("getDCLevelDayBeginStatus", whereMap);
        if (strStatus.equalsIgnoreCase("COMPLETED")) {
            whereMap.put("GROUP_ID", GROUP_ID);
            java.util.List lst = (java.util.List) ClientUtil.executeQuery("getGroupScreenDataForDayBegin", whereMap);
            for (int i = 0; i < lst.size(); i++) {
                whereMap = (HashMap) lst.get(i);
                //                whereMap.put("screenId","SCR01041");
                //                whereMap.put("screenClass",null);
                //                whereMap.put("screenName","Day Begin");
                //                whereMap.put("screenSlNo","1");
                //                whereMap.put("moduleId","14");
                //                whereMap.put("moduleSlNo","16");
                //                whereMap.put("moduleName","Periodic Activity");
                //                whereMap.put("newAllowed","Y");
                //                whereMap.put("editAllowed","Y");
                //                whereMap.put("deleteAllowed","Y");
                //                whereMap.put("authRejAllowed","Y");
                //                whereMap.put("exceptionAllowed","Y");
                //                whereMap.put("printAllowed","Y");
                //                whereMap.put("interbranchAllowed", null);
                grantScreen.add(whereMap);
            }
            treeOB.setGrantScreen(grantScreen);
            grantScreen = null;
        } else if (incompleteBran.size() <= 0) {
            whereMap.put("GROUP_ID", GROUP_ID);
            java.util.List lst = (java.util.List) ClientUtil.executeQuery("getGroupScreenDataForDayBegin", whereMap);
            for (int i = 0; i < lst.size(); i++) {
                whereMap = (HashMap) lst.get(i);
                grantScreen.add(whereMap);
            }
            treeOB.setGrantScreen(grantScreen);
            ClientUtil.showAlertWindow("Day Begin not Completed properly");
            grantScreen = null;
        } else {
            treeOB.populateData(map);
        }
        treModules.setModel(treeOB.getGrantScreenModel());
//        if (treeOB.getGoldLoanProdIDs()!=null && treeOB.getGoldLoanProdIDs().size()>0) {
//            goldLoanList = treeOB.getGoldLoanProdIDs();
//        }
        map = null;
        treeOB = null;
        whereMap = null;
        //        }
    }

    private void createTreeManual() {
        // Generating TT menu Tree.
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("TrueTransact");
        DefaultMutableTreeNode backOffice = new DefaultMutableTreeNode("Back Office");
        DefaultMutableTreeNode frontOffice = new DefaultMutableTreeNode("Front Office");
        root.add(backOffice);
        root.add(frontOffice);

        //if (!userName.equalsIgnoreCase("cashier"))
        backOffice.add(getGLTree());
        backOffice.add(getProductsTree());

        //uncommented
        //        backOffice.add(getCashierAdminTree());

        backOffice.add(getClearingAdminTree());
        backOffice.add(getSupportAdminTree());
        backOffice.add(getAdminTree());
        backOffice.add(getAuditTree());
        backOffice.add(getDayTree());

        frontOffice.add(getCustTree());
        frontOffice.add(getShareTree());
        frontOffice.add(getOperativeTree());
        frontOffice.add(getLoanTree());
        frontOffice.add(getDepositTree());
        frontOffice.add(getRemittanceTree());
        frontOffice.add(getTransactionTree());
        frontOffice.add(getLockerTree());
        frontOffice.add(getInvestmentsTree());

        // uncommented
        //        frontOffice.add(getCashierTree());

        frontOffice.add(getClearingTree());
        frontOffice.add(getSupportTree());
        frontOffice.add(getBillsTree());
        if (TrueTransactMain.SERVICE_TAX_REQ.equals("Y")) {
            frontOffice.add(getServiceTaxTree());
        }
        //frontOffice.add(getPrivateTree());

        treModules.setModel(new DefaultTreeModel(root));
    }

    private void setTreeRenderer() {
        //        // Creating Tree Renderer & Tree Model
        //        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        //        renderer.setLeafIcon(new ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_OPENFLD.jpg")));
        //        //renderer.setOpenIcon(new ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif")));

        // Creating Tree Renderer & Tree Model
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer() {

            javax.swing.Icon defIcon = new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_OPENFLD.gif"));
            javax.swing.Icon repIcon = new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/report.gif"));
            javax.swing.Icon icon = new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/edit_small.gif"));

            public java.awt.Component getTreeCellRendererComponent(
                    javax.swing.JTree tree,
                    Object value,
                    boolean sel,
                    boolean expanded,
                    boolean leaf,
                    int row,
                    boolean hasFocus) {

                super.getTreeCellRendererComponent(
                        tree, value, sel,
                        expanded, leaf, row,
                        hasFocus);
                if (leaf && isUnderDevelopment(value)) {
                    setIcon(icon);
                    setToolTipText("This module is Under-Development.");
                } else if (leaf && isReport(value)) {
                    setIcon(repIcon);
                    setToolTipText(null); //no tool tip
                } else if (leaf) {
                    setIcon(defIcon);
                    setToolTipText(CommonUtil.convertObjToStr(value));
                } else {
                    setToolTipText(CommonUtil.convertObjToStr(value));
                }
                return this;
            }

            protected boolean isUnderDevelopment(Object value) {
                DefaultMutableTreeNode node =
                        (DefaultMutableTreeNode) value;
                //            System.out.println("parent : " + node.getParent().toString());
                String title = node.toString();
//                if (title.equals("Bills") ||
//                title.equals("Orders") ||
//                title.equals("Order Details") ||
//                title.equals("Lodgement")) {
//                    return true;
//                }
                return false;
            }

            protected boolean isReport(Object value) {
                DefaultMutableTreeNode node =
                        (DefaultMutableTreeNode) value;
                String title = node.getParent().toString();
                if (title.endsWith("Reports") || title.endsWith("Pre-Prints")) {
                    setBackgroundSelectionColor(Color.RED);
                    return true;
                }
                return false;
            }
        };
        javax.swing.ToolTipManager.sharedInstance().registerComponent(treModules);
        treModules.setCellRenderer(renderer);
        if (!setRenderer) {
            expandAll(treModules);
            treModules.revalidate();
            setRenderer = true;
        }
        /*
         * treModules.treeDidChange(); treModules.expandRow(0);
         * treModules.updateUI();
         */
    }
    /*
     *
     *
     * class TreeRenderer extends DefaultTreeCellRenderer { javax.swing.Icon
     * defIcon; javax.swing.Icon icon;
     *
     * public TreeRenderer(javax.swing.Icon defIcon, javax.swing.Icon icon) {
     * this.defIcon = defIcon; this.icon = icon; }
     *
     * public java.awt.Component getTreeCellRendererComponent( javax.swing.JTree
     * tree, Object value, boolean sel, boolean expanded, boolean leaf, int row,
     * boolean hasFocus) {
     *
     * super.getTreeCellRendererComponent( tree, value, sel, expanded, leaf,
     * row, hasFocus); if (leaf && isUnderDevelopment(value)) { setIcon(icon);
     * setToolTipText("This module is Under-Development."); } else if (leaf) {
     * setIcon(defIcon); setToolTipText(null); //no tool tip } return this; }
     *
     * protected boolean isUnderDevelopment(Object value) {
     * DefaultMutableTreeNode node = (DefaultMutableTreeNode) value; String
     * title = node.toString(); if (title.equals("Bills") ||
     * title.equals("Orders") || title.equals("Order Details") ||
     * title.equals("Lodgement")) { return true; } return false; } }
     */
    /*
     * Day begin & End related activities Tree
     */

    private DefaultMutableTreeNode getDayTree() {
        DefaultMutableTreeNode day = new DefaultMutableTreeNode("Day Activity");
        DefaultMutableTreeNode dayBegin = new DefaultMutableTreeNode("Day Begin");
        DefaultMutableTreeNode dayEnd = new DefaultMutableTreeNode("Day End");
        //        DefaultMutableTreeNode dayBeginCash = new DefaultMutableTreeNode("Day Begin Cash");
        //        DefaultMutableTreeNode dayEndCash = new DefaultMutableTreeNode("Day End Cash");
        day.add(dayBegin);
        day.add(dayEnd);
        //        day.add(dayBeginCash);
        //        day.add(dayEndCash);
        return day;
    }

    /*
     * General Ledger Tree
     */
    private DefaultMutableTreeNode getGLTree() {
        DefaultMutableTreeNode gl = new DefaultMutableTreeNode("General Ledger");
        DefaultMutableTreeNode majorGL = new DefaultMutableTreeNode("Major/Sub Head");
        DefaultMutableTreeNode acctHdGL = new DefaultMutableTreeNode("Account Head");
        DefaultMutableTreeNode acctMainGL = new DefaultMutableTreeNode("Account Head Maintenance");
        DefaultMutableTreeNode branchGL = new DefaultMutableTreeNode("Branchwise GL");
        DefaultMutableTreeNode glEntry = new DefaultMutableTreeNode("GL Entry");
        gl.add(majorGL);
        gl.add(acctHdGL);
        gl.add(acctMainGL);
        gl.add(branchGL);
        gl.add(glEntry);
        return gl;
    }

    /*
     * Customer Information Tree
     */
    private DefaultMutableTreeNode getCustTree() {
        DefaultMutableTreeNode cust = new DefaultMutableTreeNode("Customer");
        DefaultMutableTreeNode indiCust = new DefaultMutableTreeNode("Individual");
        DefaultMutableTreeNode corpCust = new DefaultMutableTreeNode("Corporate");
        DefaultMutableTreeNode deathCust = new DefaultMutableTreeNode("Death Marking");
        DefaultMutableTreeNode securityCust = new DefaultMutableTreeNode("Security");
        DefaultMutableTreeNode agentCust = new DefaultMutableTreeNode("Agent");
        DefaultMutableTreeNode custIDChange = new DefaultMutableTreeNode("Change Customer ID");
        DefaultMutableTreeNode malaConvr = new DefaultMutableTreeNode("Member Malayalam Conversion");
        cust.add(indiCust);
        cust.add(corpCust);
        cust.add(deathCust);
        cust.add(securityCust);
        cust.add(agentCust);
        cust.add(custIDChange);
        cust.add(malaConvr);
        return cust;
    }

    /*
     * All Products Tree
     */
    private DefaultMutableTreeNode getProductsTree() {
        DefaultMutableTreeNode product = new DefaultMutableTreeNode("Product Management");
        DefaultMutableTreeNode oprAcctProduct = new DefaultMutableTreeNode("Operative Account");

        //````````````````
        DefaultMutableTreeNode termLoansProduct = new DefaultMutableTreeNode("Asset Portfolio");
        DefaultMutableTreeNode advancesProduct = new DefaultMutableTreeNode("Advances");
        DefaultMutableTreeNode depositsProduct = new DefaultMutableTreeNode("Liability Portfolio");
        DefaultMutableTreeNode remitProduct = new DefaultMutableTreeNode("Remittance");
        DefaultMutableTreeNode billsProduct = new DefaultMutableTreeNode("Bills");
        DefaultMutableTreeNode shareProduct = new DefaultMutableTreeNode("Share");
        DefaultMutableTreeNode charges = new DefaultMutableTreeNode("Charges Configuration");
        DefaultMutableTreeNode intMain = new DefaultMutableTreeNode("Interest Rate Maintenance");
        DefaultMutableTreeNode tdsMain = new DefaultMutableTreeNode("TDS Maintenance");
        DefaultMutableTreeNode dailyComm = new DefaultMutableTreeNode("Daily Commission");


        product.add(oprAcctProduct);

        //````````````````
        product.add(termLoansProduct);
        product.add(advancesProduct);
        product.add(depositsProduct);
        product.add(remitProduct);
        product.add(billsProduct);
        product.add(shareProduct);
        product.add(charges);
        product.add(intMain);

        product.add(tdsMain);
        product.add(dailyComm);

        return product;
    }

    private DefaultMutableTreeNode getShareTree() {
        DefaultMutableTreeNode shareAcct = new DefaultMutableTreeNode("Share");
        DefaultMutableTreeNode acctShare = new DefaultMutableTreeNode("Share Account");
        DefaultMutableTreeNode shareTransfer = new DefaultMutableTreeNode("Share Transfer");
        shareAcct.add(acctShare);
        shareAcct.add(shareTransfer);

        return shareAcct;
    }

    /*
     * Operative Account Tree
     */
    private DefaultMutableTreeNode getOperativeTree() {
        DefaultMutableTreeNode oprAcct = new DefaultMutableTreeNode("Operative Account");
        DefaultMutableTreeNode acctsOA = new DefaultMutableTreeNode("Accounts");
        DefaultMutableTreeNode acctTransOA = new DefaultMutableTreeNode("Account Transfer");
        DefaultMutableTreeNode chrgOA = new DefaultMutableTreeNode("Charges");
        //        DefaultMutableTreeNode interestOA = new DefaultMutableTreeNode("Interest");
        DefaultMutableTreeNode acctCloseOA = new DefaultMutableTreeNode("Account Closing");
        DefaultMutableTreeNode lienOA = new DefaultMutableTreeNode("Lien Marking");
        DefaultMutableTreeNode freezeOA = new DefaultMutableTreeNode("Freeze");
        DefaultMutableTreeNode TODOA = new DefaultMutableTreeNode("Tod Maintenance");
        //        DefaultMutableTreeNode deathOA = new DefaultMutableTreeNode("Death Marking");
        oprAcct.add(acctsOA);
        oprAcct.add(acctTransOA);
        oprAcct.add(chrgOA);
        //        oprAcct.add(interestOA);
        oprAcct.add(lienOA);
        oprAcct.add(freezeOA);
        oprAcct.add(TODOA);

        //        oprAcct.add(deathOA);

        return oprAcct;
    }

    //    /* Advances Account Tree */
    //    private DefaultMutableTreeNode getAdvancesTree() {
    //        DefaultMutableTreeNode advAcct = new DefaultMutableTreeNode("Advances");
    //        DefaultMutableTreeNode accAA = new DefaultMutableTreeNode("Advances Account");
    //        advAcct.add(accAA);
    //        return advAcct;
    //    }
    /*
     * Loan Account Tree
     */
    private DefaultMutableTreeNode getLoanTree() {
//        DefaultMutableTreeNode loanAcct = new DefaultMutableTreeNode("Term Loan & Advance");
        DefaultMutableTreeNode accLA = new DefaultMutableTreeNode("Term Loans/Advance Account");
        DefaultMutableTreeNode drawingPower = new DefaultMutableTreeNode("Drawing Power Maintenance");
        DefaultMutableTreeNode emiCalculator = new DefaultMutableTreeNode("EMI Calculator");
        DefaultMutableTreeNode agriTermLoan = new DefaultMutableTreeNode("Agri Term Loans/Agri Advance Account");
        DefaultMutableTreeNode termLoanRepaySchedule = new DefaultMutableTreeNode("Term Loan Repayment Schedule"); // Added by nithya
//        DefaultMutableTreeNode loanClosing=new DefaultMutableTreeNode("Term Loan / AdvancesClosing");
        accLA.add(accLA);//loanAcct
        accLA.add(drawingPower);
        accLA.add(emiCalculator);
        accLA.add(agriTermLoan);
        accLA.add(termLoanRepaySchedule); // Added by nithya
//        loanAcct.add(loanClosing);

        //        DefaultMutableTreeNode loanClosing=new DefaultMutableTreeNode("Term Loan / AdvancesClosing");
//        loanAcct.add(accLA);
//        loanAcct.add(drawingPower);
//        loanAcct.add(emiCalculator);
//        loanAcct.add(agriTermLoan);
        //        loanAcct.add(loanClosing);


        return accLA;
    }

    /*
     * Deposit Account Tree
     */
    private DefaultMutableTreeNode getDepositTree() {
        DefaultMutableTreeNode depAcct = new DefaultMutableTreeNode("Deposits");

        DefaultMutableTreeNode accDA = new DefaultMutableTreeNode("Deposits Account");
        DefaultMutableTreeNode intCalcAdmin = new DefaultMutableTreeNode("Interest Calculator");

//        DefaultMutableTreeNode accDA = new DefaultMutableTreeNode("Deposit Accounts");
//        DefaultMutableTreeNode intCalcAdmin = new DefaultMutableTreeNode("Interest Calculation");

        DefaultMutableTreeNode depTDSExemption = new DefaultMutableTreeNode("TDS Exemption");
        DefaultMutableTreeNode depTDS = new DefaultMutableTreeNode("TDS Deduction");
        DefaultMutableTreeNode depClose = new DefaultMutableTreeNode("Deposit Closing");
        DefaultMutableTreeNode depLien = new DefaultMutableTreeNode("Deposit Lien");
        DefaultMutableTreeNode depFreeze = new DefaultMutableTreeNode("Deposit Freeze");
        DefaultMutableTreeNode depSerach = new DefaultMutableTreeNode("Deposit Account Search"); // Added by nithya on 22.02.2016 for 3695
        depAcct.add(accDA);
        depAcct.add(intCalcAdmin);
        depAcct.add(depTDSExemption);
        depAcct.add(depTDS);
        depAcct.add(depLien);
        depAcct.add(depFreeze);
        depAcct.add(depClose);
        depAcct.add(depSerach); // Added by nithya on 22.02.2016 for 3695
        return depAcct;
    }

    /*
     * Remittance Account Tree
     */
    private DefaultMutableTreeNode getRemittanceTree() {
        DefaultMutableTreeNode remi = new DefaultMutableTreeNode("Remittance");
        DefaultMutableTreeNode remittanceIssue = new DefaultMutableTreeNode("Issue");
        DefaultMutableTreeNode remittancePayment = new DefaultMutableTreeNode("Payment");
        DefaultMutableTreeNode remittanceStopPayment = new DefaultMutableTreeNode("Stop Payment");
        DefaultMutableTreeNode print = new DefaultMutableTreeNode("Print");
        remi.add(remittanceIssue);
        remi.add(remittancePayment);
        remi.add(remittanceStopPayment);
        remi.add(print);
        return remi;
    }

    /*
     * Transaction for all kind of products Tree
     */
    private DefaultMutableTreeNode getTransactionTree() {
        DefaultMutableTreeNode trans = new DefaultMutableTreeNode("Transaction");
        DefaultMutableTreeNode cashTrans = new DefaultMutableTreeNode("Cash");
        DefaultMutableTreeNode transferTrans = new DefaultMutableTreeNode("Transfer");
        DefaultMutableTreeNode inwardClearing = new DefaultMutableTreeNode("Inward Clearing");
        DefaultMutableTreeNode outwardClearing = new DefaultMutableTreeNode("Outward Clearing");
        DefaultMutableTreeNode tokenIssue = new DefaultMutableTreeNode("Token Issue");
        DefaultMutableTreeNode tokenLoss = new DefaultMutableTreeNode("Token Loss");
        DefaultMutableTreeNode cashManagement = new DefaultMutableTreeNode("Cash Movement");
        DefaultMutableTreeNode chargesService = new DefaultMutableTreeNode("chargesServiceTax");
        DefaultMutableTreeNode settlement = new DefaultMutableTreeNode("settlement");
        DefaultMutableTreeNode balanceTrans = new DefaultMutableTreeNode("Balance");
        trans.add(cashTrans);
        trans.add(transferTrans);
        trans.add(inwardClearing);
        trans.add(outwardClearing);
        trans.add(tokenIssue);
        trans.add(tokenLoss);
        trans.add(cashManagement);
        trans.add(chargesService);
        trans.add(settlement);
        trans.add(balanceTrans);
        return trans;
    }

    private DefaultMutableTreeNode getLockerTree() {
        DefaultMutableTreeNode locker = new DefaultMutableTreeNode("Locker");
        DefaultMutableTreeNode lockerIssue = new DefaultMutableTreeNode("Locker Issue");
//        DefaultMutableTreeNode transferTrans = new DefaultMutableTreeNode("Transfer");
//        DefaultMutableTreeNode inwardClearing = new DefaultMutableTreeNode("Inward Clearing");
//        DefaultMutableTreeNode outwardClearing = new DefaultMutableTreeNode("Outward Clearing");
//        DefaultMutableTreeNode tokenIssue = new DefaultMutableTreeNode("Token Issue");
//        DefaultMutableTreeNode tokenLoss = new DefaultMutableTreeNode("Token Loss");
//        DefaultMutableTreeNode cashManagement = new DefaultMutableTreeNode("Cash Movement");
//        DefaultMutableTreeNode chargesService = new DefaultMutableTreeNode("chargesServiceTax");
//        DefaultMutableTreeNode settlement = new DefaultMutableTreeNode("settlement");

        locker.add(lockerIssue);
//        trans.add(transferTrans);
//        trans.add(inwardClearing);
//        trans.add(outwardClearing);
//        trans.add(tokenIssue);
//        trans.add(tokenLoss);
//        trans.add(cashManagement);
//        trans.add(chargesService);
//        trans.add(settlement);

        return locker;
    }
    /*
     * Cashier System Tree
     */

    /*
     * Cashier System Tree
     */
    private DefaultMutableTreeNode getServiceTaxTree() {
        DefaultMutableTreeNode serviceTax = new DefaultMutableTreeNode("Service Tax Settings");
        DefaultMutableTreeNode serviceTaxSettings = new DefaultMutableTreeNode("Service Tax Settings");
        serviceTax.add(serviceTaxSettings);
        return serviceTax;
    }

    private DefaultMutableTreeNode getCashierAdminTree() {
        DefaultMutableTreeNode forexA = new DefaultMutableTreeNode("Cashier System");
        DefaultMutableTreeNode exchRateA = new DefaultMutableTreeNode("Exchange Rate Parameter");
        forexA.add(exchRateA);
        return forexA;
    }

    /*
     * Cashier System Tree
     */
    private DefaultMutableTreeNode getCashierTree() {
        DefaultMutableTreeNode forex = new DefaultMutableTreeNode("Cashier System");
        DefaultMutableTreeNode currExch = new DefaultMutableTreeNode("Currency Exchange");

        //`````````````````
        DefaultMutableTreeNode fileSender = new DefaultMutableTreeNode("File Sender");
        DefaultMutableTreeNode cashTransfer = new DefaultMutableTreeNode("Cash Transfer");
        DefaultMutableTreeNode recCashTransfer = new DefaultMutableTreeNode("Receive Cash Transfer");
        DefaultMutableTreeNode pendingCashTransfer = new DefaultMutableTreeNode("Pending Cash Transfer");

        DefaultMutableTreeNode exchRate = new DefaultMutableTreeNode("Exchange Rate");
//        DefaultMutableTreeNode denomination = new DefaultMutableTreeNode("Cash Stock - Denominationwise");
        DefaultMutableTreeNode denomination = new DefaultMutableTreeNode("Cash Stock View- Denominationwise");
        forex.add(currExch);

        //``````````````````
        forex.add(fileSender);
        forex.add(cashTransfer);
        forex.add(recCashTransfer);
        forex.add(pendingCashTransfer);

        forex.add(exchRate);
        forex.add(denomination);
        return forex;
    }

    /*
     * Clearing Tree
     */
    private DefaultMutableTreeNode getClearingAdminTree() {
        DefaultMutableTreeNode clearingA = new DefaultMutableTreeNode("Clearing");
        DefaultMutableTreeNode clrBankParamA = new DefaultMutableTreeNode("Clearing Configuration");
        DefaultMutableTreeNode clrParamA = new DefaultMutableTreeNode("Clearing Parameters");
        clearingA.add(clrBankParamA);
        clearingA.add(clrParamA);
        return clearingA;
    }

    /*
     * Clearing Tree
     */
    private DefaultMutableTreeNode getClearingTree() {
        DefaultMutableTreeNode clearing = new DefaultMutableTreeNode("Clearing");
        DefaultMutableTreeNode clrTally = new DefaultMutableTreeNode("Inward Tally");
        DefaultMutableTreeNode clrOutwardTally = new DefaultMutableTreeNode("Outward Tally");
        DefaultMutableTreeNode clrBouncing = new DefaultMutableTreeNode("Bouncing of Instrument");
        DefaultMutableTreeNode clrReturn = new DefaultMutableTreeNode("Return of Instrument");
        DefaultMutableTreeNode clrData = new DefaultMutableTreeNode("Clearing Data");
        clearing.add(clrTally);
        clearing.add(clrOutwardTally);
        clearing.add(clrBouncing);
        clearing.add(clrReturn);
        clearing.add(clrData);
        return clearing;
    }

    /*
     * Supporting Module Tree
     */
    private DefaultMutableTreeNode getSupportAdminTree() {
        DefaultMutableTreeNode supmodA = new DefaultMutableTreeNode("Supporting Modules");
        DefaultMutableTreeNode inventoryMasSMA = new DefaultMutableTreeNode("Inventory Master");
        DefaultMutableTreeNode inventoryDetSMA = new DefaultMutableTreeNode("Inventory Details");
        supmodA.add(inventoryMasSMA);
        supmodA.add(inventoryDetSMA);
        return supmodA;
    }

    /*
     * Supporting Module Tree
     */
    private DefaultMutableTreeNode getSupportTree() {
        DefaultMutableTreeNode supmod = new DefaultMutableTreeNode("Supporting Modules");
        DefaultMutableTreeNode chkBkMgtSMA = new DefaultMutableTreeNode("Cheque Book Management");
        DefaultMutableTreeNode siSM = new DefaultMutableTreeNode("Standing Instruction");
        supmod.add(chkBkMgtSMA);
        supmod.add(siSM);
        return supmod;
    }

    /*
     * Private Banking Tree
     */
    private DefaultMutableTreeNode getPrivateTree() {
        DefaultMutableTreeNode priBank = new DefaultMutableTreeNode("Private Banking");
        DefaultMutableTreeNode relationshipPB = new DefaultMutableTreeNode("Relationship");
        DefaultMutableTreeNode ordPB = new DefaultMutableTreeNode("Orders");
        DefaultMutableTreeNode ordDetailsPB = new DefaultMutableTreeNode("Order Details");
        //        DefaultMutableTreeNode ftInternalAIPB = new DefaultMutableTreeNode("FT Internal");
        //        DefaultMutableTreeNode externalWireAIPB = new DefaultMutableTreeNode("External Wire");
        //        DefaultMutableTreeNode purequiAIPB = new DefaultMutableTreeNode("Purchase Equity");
        //        DefaultMutableTreeNode timeDepositPB = new DefaultMutableTreeNode("New Time Deposit");
        //        DefaultMutableTreeNode tamMaintenancePB = new DefaultMutableTreeNode("TAM Maintenance");
        //relationshipPB.
        priBank.add(relationshipPB);
        priBank.add(ordPB);
        priBank.add(ordDetailsPB);
        //        priBank.add(ftInternalAIPB);
        //        priBank.add(externalWireAIPB);
        //        priBank.add(purequiAIPB);
        //        priBank.add(timeDepositPB);
        //        priBank.add(tamMaintenancePB);

        //	DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        //	renderer.setLeafIcon(new ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.jpg")));
        //renderer.setOpenIcon(new ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif")));
        return priBank;
    }

    /*
     * ALM information Tree
     */
    private DefaultMutableTreeNode getAlmTree() {
        DefaultMutableTreeNode alm = new DefaultMutableTreeNode("ALM");
        DefaultMutableTreeNode repALM = new DefaultMutableTreeNode("ALM");
        alm.add(repALM);
        return alm;
    }

    /*
     * Bills Tree
     */
    private DefaultMutableTreeNode getBillsTree() {
        DefaultMutableTreeNode bills = new DefaultMutableTreeNode("Bills");
        DefaultMutableTreeNode carrier = new DefaultMutableTreeNode("Carrier");
        DefaultMutableTreeNode lodgement = new DefaultMutableTreeNode("Lodgement");
        DefaultMutableTreeNode collectingBankDetailsEntry = new DefaultMutableTreeNode("CollectingBankDetailsEntryUI");
        DefaultMutableTreeNode multipleAcctlodgement = new DefaultMutableTreeNode("Multiple Account Lodgement"); // Added by nithya
        bills.add(carrier);
        bills.add(lodgement);
        bills.add(collectingBankDetailsEntry);
        bills.add(multipleAcctlodgement); // Added by nithya
        return bills;
    }
    //investment trans and master

    private DefaultMutableTreeNode getInvestmentsTree() {
        DefaultMutableTreeNode investment = new DefaultMutableTreeNode("Investment");
        DefaultMutableTreeNode investmentMaster = new DefaultMutableTreeNode("InvestmentMaster");
        DefaultMutableTreeNode investmentTrans = new DefaultMutableTreeNode("InvestmentTrans");
        DefaultMutableTreeNode investmentAmortization = new DefaultMutableTreeNode("InvestmentsAmortization");

        investment.add(investmentMaster);
        investment.add(investmentTrans);
        return investment;
    }
    /*
     * Admin Tree
     */

    private DefaultMutableTreeNode getAdminTree() {
        DefaultMutableTreeNode admin = new DefaultMutableTreeNode("Administrator");
        DefaultMutableTreeNode screensAdmin = new DefaultMutableTreeNode("Reference Screens");
        DefaultMutableTreeNode configAdmin = new DefaultMutableTreeNode("Configuration");
        DefaultMutableTreeNode lookupAdmin = new DefaultMutableTreeNode("Lookup Master");
        DefaultMutableTreeNode empAdmin = new DefaultMutableTreeNode("Employee");
        DefaultMutableTreeNode terminalAdmin = new DefaultMutableTreeNode("Terminal Master");
        DefaultMutableTreeNode levelcontrolAdmin = new DefaultMutableTreeNode("Level Setting");
        DefaultMutableTreeNode multilevelcontrolAdmin = new DefaultMutableTreeNode("Escalation");
        DefaultMutableTreeNode calendarAdmin = new DefaultMutableTreeNode("Calendar");
        DefaultMutableTreeNode userAdmin = new DefaultMutableTreeNode("User");
        DefaultMutableTreeNode onlineUserAdmin = new DefaultMutableTreeNode("Online Users");
        DefaultMutableTreeNode grpAdmin = new DefaultMutableTreeNode("Group");
        DefaultMutableTreeNode roleAdmin = new DefaultMutableTreeNode("Role");
        DefaultMutableTreeNode branAdmin = new DefaultMutableTreeNode("Branch Setup");

        //`````````````````
        //        DefaultMutableTreeNode onlineAdmin = new DefaultMutableTreeNode("Online Branches");

        DefaultMutableTreeNode bankAdmin = new DefaultMutableTreeNode("Bank");
        DefaultMutableTreeNode customizeAdmin = new DefaultMutableTreeNode("Customize");
        DefaultMutableTreeNode scheduleAdmin = new DefaultMutableTreeNode("Scheduler");
        DefaultMutableTreeNode branGrpAdmin = new DefaultMutableTreeNode("Branch Group");
        DefaultMutableTreeNode otherBankAdmin = new DefaultMutableTreeNode("Other Bank/Branch");
        DefaultMutableTreeNode tokenConfig = new DefaultMutableTreeNode("Token Configuration");
        DefaultMutableTreeNode lockerConfig = new DefaultMutableTreeNode("Locker Configuration");

        DefaultMutableTreeNode denominationConfig = new DefaultMutableTreeNode("Denomination Config");


        //        DefaultMutableTreeNode logAdmin = new DefaultMutableTreeNode("Log");
        //        DefaultMutableTreeNode viewImpAdmin = new DefaultMutableTreeNode("Log Importance Config");
        //        DefaultMutableTreeNode susTrackAdmin = new DefaultMutableTreeNode("Suspicious Activity Tracking");
        //        DefaultMutableTreeNode blockListAdmin = new DefaultMutableTreeNode("Block List");
        //        DefaultMutableTreeNode auditAdmin = new DefaultMutableTreeNode("Audit");

        admin.add(bankAdmin);
        admin.add(configAdmin);
        admin.add(branGrpAdmin);
        admin.add(branAdmin);

        admin.add(tokenConfig);
        admin.add(lockerConfig);

        //```````````````
        //        admin.add(onlineAdmin);

        admin.add(grpAdmin);
        admin.add(levelcontrolAdmin);
        admin.add(multilevelcontrolAdmin);
        admin.add(roleAdmin);
        admin.add(empAdmin);
        admin.add(otherBankAdmin);
        admin.add(terminalAdmin);
        admin.add(userAdmin);
        admin.add(onlineUserAdmin);
        admin.add(screensAdmin);
        admin.add(lookupAdmin);
        admin.add(calendarAdmin);
        admin.add(denominationConfig);
        //        admin.add(customizeAdmin);
        //        admin.add(scheduleAdmin);

        //        admin.add(logAdmin);
        //        admin.add(viewImpAdmin);
        //        admin.add(susTrackAdmin);
        //        admin.add(blockListAdmin);
        //        admin.add(auditAdmin);

        return admin;
    }

    /*
     * Admin Tree
     */
    private DefaultMutableTreeNode getAuditTree() {
        DefaultMutableTreeNode audit = new DefaultMutableTreeNode("Audit & Inspection");
        DefaultMutableTreeNode logAdmin = new DefaultMutableTreeNode("Log");
        DefaultMutableTreeNode viewImpAdmin = new DefaultMutableTreeNode("Log Importance Config");
        DefaultMutableTreeNode susTrackAdmin = new DefaultMutableTreeNode("Suspicious Activity Tracking");
        DefaultMutableTreeNode blockListAdmin = new DefaultMutableTreeNode("Block List");
        DefaultMutableTreeNode auditAdmin = new DefaultMutableTreeNode("Audit");

        audit.add(logAdmin);
        audit.add(viewImpAdmin);
        audit.add(susTrackAdmin);
        audit.add(blockListAdmin);
        audit.add(auditAdmin);

        return audit;
    }

    /**
     * This is expanding all the nodes available in the tree
     *
     * @param tree Tree Object
     */
    public void expandAll(javax.swing.JTree tree) {
        int row = 0;
        //        int rowCnt = tree.getRowCount();   // If we want to expand all nodes we can uncomment this and comment rowcnt=4
        int rowCnt = 4;
        //System.out.println(rowCnt);
        ArrayList pathList = new ArrayList();
        //        while (row < rowCnt) {                                          //Previously written code commented by Rajesh because not expanding all
        //            System.out.println ("#$#$ inside while "+tree.getRowCount());
        //          tree.expandRow(row);
        //          row++;
        //        }
        while (row < rowCnt) {
            pathList.add(tree.getPathForRow(row));
            row++;
        }
        row = 0;
        while (row < rowCnt) {
            javax.swing.tree.TreePath treePath = (javax.swing.tree.TreePath) pathList.get(row);
            //tree.expandPath(treePath);
            row++;
        }

    }

    /**
     * It will shrink all the tree nodes
     *
     * @param tree Tree Object
     */
    public void collapseAll(javax.swing.JTree tree) {
        int row = tree.getRowCount() - 1;
        while (row >= 0) {
            tree.collapseRow(row);
            row--;
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

        tbrTTMain = new javax.swing.JToolBar();
        cButton1 = new com.see.truetransact.uicomponent.CButton();
        cButton2 = new com.see.truetransact.uicomponent.CButton();
        cButton3 = new com.see.truetransact.uicomponent.CButton();
        cButton4 = new com.see.truetransact.uicomponent.CButton();
        panTTMain = new com.see.truetransact.uicomponent.CPanel();
        spnTTMain = new javax.swing.JSplitPane();
        panTree = new com.see.truetransact.uicomponent.CPanel();
        scrTree = new com.see.truetransact.uicomponent.CScrollPane();
        treModules = new javax.swing.JTree();
        panFind = new com.see.truetransact.uicomponent.CPanel();
        txtFind = new com.see.truetransact.uicomponent.CTextField();
        btnFind = new com.see.truetransact.uicomponent.CButton();
        dtpTTMain = new com.see.truetransact.uicomponent.CDesktopPane();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblBranch = new com.see.truetransact.uicomponent.CLabel();
        lblUser = new com.see.truetransact.uicomponent.CLabel();
        lblDate = new com.see.truetransact.uicomponent.CLabel();
        cboBranchList = new com.see.truetransact.uicomponent.CComboBox();
        lblSelBranch = new com.see.truetransact.uicomponent.CLabel();
        txtSmartCustomer = new com.see.truetransact.uicomponent.CTextField();
        mbrTrueTransactMain = new com.see.truetransact.uicomponent.CMenuBar();
        mnuLogin = new javax.swing.JMenu();
        mitDownload = new javax.swing.JMenuItem();
        sptLogin = new javax.swing.JSeparator();
        mitDisconnect = new javax.swing.JMenuItem();
        mitPassword = new javax.swing.JMenuItem();
        mitApplnLock = new javax.swing.JMenuItem();
        mitExportDBBackup = new javax.swing.JMenuItem();
        sptExit = new javax.swing.JSeparator();
        mitExit = new javax.swing.JMenuItem();
        mnuWindow = new javax.swing.JMenu();
        mitCascade = new javax.swing.JMenuItem();
        mitTiled = new javax.swing.JMenuItem();
        sptWindow = new javax.swing.JSeparator();
        mitExpandAll = new javax.swing.JMenuItem();
        mitCollapse = new javax.swing.JMenuItem();
        mnuDue = new javax.swing.JMenu();
        mnuBankName = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        cButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_ClientProfile.gif"))); // NOI18N
        tbrTTMain.add(cButton1);

        cButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        tbrTTMain.add(cButton2);

        cButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_ClientView.gif"))); // NOI18N
        tbrTTMain.add(cButton3);

        cButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Schedule.gif"))); // NOI18N
        tbrTTMain.add(cButton4);

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("CBMS++");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });
        getContentPane().setLayout(new java.awt.BorderLayout(2, 2));

        panTTMain.setLayout(new java.awt.GridBagLayout());

        panTree.setPreferredSize(new java.awt.Dimension(250, 357));
        panTree.setLayout(new java.awt.GridBagLayout());

        scrTree.setPreferredSize(new java.awt.Dimension(150, 324));

        treModules.setFont(new java.awt.Font("MS Sans Serif", 0, 13)); // NOI18N
        treModules.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                treModulesMouseClicked(evt);
            }
        });
        treModules.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                treModulesValueChanged(evt);
            }
        });
        scrTree.setViewportView(treModules);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panTree.add(scrTree, gridBagConstraints);

        panFind.setLayout(new java.awt.GridBagLayout());

        txtFind.setMinimumSize(new java.awt.Dimension(6, 25));
        txtFind.setPreferredSize(new java.awt.Dimension(100, 25));
        txtFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFindActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panFind.add(txtFind, gridBagConstraints);

        btnFind.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_FIND.gif"))); // NOI18N
        btnFind.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panFind.add(btnFind, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panTree.add(panFind, gridBagConstraints);

        spnTTMain.setLeftComponent(panTree);
        spnTTMain.setRightComponent(dtpTTMain);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panTTMain.add(spnTTMain, gridBagConstraints);

        getContentPane().add(panTTMain, java.awt.BorderLayout.CENTER);

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblBranch.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblBranch.setText(" Home Branch : ");
        lblBranch.setMaximumSize(new java.awt.Dimension(200, 25));
        lblBranch.setMinimumSize(new java.awt.Dimension(200, 25));
        lblBranch.setPreferredSize(new java.awt.Dimension(200, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panStatus.add(lblBranch, gridBagConstraints);

        lblUser.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblUser.setText(" User :");
        lblUser.setMaximumSize(new java.awt.Dimension(200, 25));
        lblUser.setMinimumSize(new java.awt.Dimension(200, 25));
        lblUser.setPreferredSize(new java.awt.Dimension(200, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panStatus.add(lblUser, gridBagConstraints);

        lblDate.setText("date1");
        lblDate.setMaximumSize(new java.awt.Dimension(150, 25));
        lblDate.setMinimumSize(new java.awt.Dimension(150, 25));
        lblDate.setPreferredSize(new java.awt.Dimension(150, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panStatus.add(lblDate, gridBagConstraints);

        cboBranchList.setMaximumSize(new java.awt.Dimension(125, 21));
        cboBranchList.setMinimumSize(new java.awt.Dimension(125, 21));
        cboBranchList.setPreferredSize(new java.awt.Dimension(125, 21));
        cboBranchList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cboBranchListMouseClicked(evt);
            }
        });
        cboBranchList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboBranchListActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panStatus.add(cboBranchList, gridBagConstraints);

        lblSelBranch.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSelBranch.setText(" Selected Branch : ");
        lblSelBranch.setMaximumSize(new java.awt.Dimension(200, 25));
        lblSelBranch.setMinimumSize(new java.awt.Dimension(200, 25));
        lblSelBranch.setPreferredSize(new java.awt.Dimension(200, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        panStatus.add(lblSelBranch, gridBagConstraints);

        txtSmartCustomer.setPreferredSize(new java.awt.Dimension(110, 21));
        txtSmartCustomer.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSmartCustomerFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 27;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 9, 0, 0);
        panStatus.add(txtSmartCustomer, gridBagConstraints);

        getContentPane().add(panStatus, java.awt.BorderLayout.SOUTH);

        mnuLogin.setText("Main");
        mnuLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuLoginActionPerformed(evt);
            }
        });

        mitDownload.setText("Update Local DB");
        mitDownload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDownloadActionPerformed(evt);
            }
        });
        mnuLogin.add(mitDownload);
        mnuLogin.add(sptLogin);

        mitDisconnect.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/exit.gif"))); // NOI18N
        mitDisconnect.setText("Logout");
        mitDisconnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDisconnectActionPerformed(evt);
            }
        });
        mnuLogin.add(mitDisconnect);

        mitPassword.setText("Change Password");
        mitPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitPasswordActionPerformed(evt);
            }
        });
        mnuLogin.add(mitPassword);

        mitApplnLock.setText("Application Lock (Ctrl+Alt+L)");
        mitApplnLock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitApplnLockActionPerformed(evt);
            }
        });
        mnuLogin.add(mitApplnLock);

        mitExportDBBackup.setText("Export DB Backup");
        mitExportDBBackup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitExportDBBackupActionPerformed(evt);
            }
        });
        mnuLogin.add(mitExportDBBackup);
        mnuLogin.add(sptExit);

        mitExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/exit.gif"))); // NOI18N
        mitExit.setText("Exit");
        mitExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitExitActionPerformed(evt);
            }
        });
        mnuLogin.add(mitExit);

        mbrTrueTransactMain.add(mnuLogin);

        mnuWindow.setText("Window");

        mitCascade.setText("Cascade");
        mitCascade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCascadeActionPerformed(evt);
            }
        });
        mnuWindow.add(mitCascade);

        mitTiled.setText("Tile");
        mitTiled.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitTiledActionPerformed(evt);
            }
        });
        mnuWindow.add(mitTiled);
        mnuWindow.add(sptWindow);

        mitExpandAll.setText("Expand All");
        mitExpandAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitExpandAllActionPerformed(evt);
            }
        });
        mnuWindow.add(mitExpandAll);

        mitCollapse.setText("Collapse");
        mitCollapse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCollapseActionPerformed(evt);
            }
        });
        mnuWindow.add(mitCollapse);

        mbrTrueTransactMain.add(mnuWindow);

        mnuDue.setText("Due List");
        mnuDue.setPreferredSize(new java.awt.Dimension(100, 19));
        mnuDue.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mnuDueMouseClicked(evt);
            }
        });
        mbrTrueTransactMain.add(mnuDue);

        mnuBankName.setText("ABC Bank Ltd.");
        mnuBankName.setFocusable(false);
        mnuBankName.setFont(new java.awt.Font("Arial Unicode MS", 0, 12)); // NOI18N
        mnuBankName.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        mnuBankName.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        mnuBankName.setMaximumSize(new java.awt.Dimension(900, 32767));
        mnuBankName.setPreferredSize(new java.awt.Dimension(900, 21));

        jMenuItem1.setText("#12, M.G. Road, Bangalore - 560020");
        mnuBankName.add(jMenuItem1);

        mbrTrueTransactMain.add(mnuBankName);

        setJMenuBar(mbrTrueTransactMain);

        pack();
    }// </editor-fold>//GEN-END:initComponents
            private void cboBranchListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboBranchListActionPerformed
                //cboBranchList.setEnabled(false);
                if (cboBranchList.getSelectedIndex() > 0) {                 
                        String color = "blue";
                        if (!BRANCH_ID.equals(((ComboBoxModel) cboBranchList.getModel()).getKeyForSelected())) {
                            javax.swing.JInternalFrame jFrames[] = dtpTTMain.getAllFrames();
                            String selected = "";
                            if(treModules.getLastSelectedPathComponent() != null){
                                selected = treModules.getLastSelectedPathComponent().toString();
                            }
                            if (jFrames.length > 0 && (selected != null && selected.length()>0 && !selected.equals("Transfer"))) { 
                                ClientUtil.showAlertWindow("<html>Close all the opened windows then select Branch for InterBranch Transaction</html>");
                                cboBranchList.setSelectedItem(((ComboBoxModel) cboBranchList.getModel()).getDataForKey(BRANCH_ID));
                                return;
                            }
                            //Added for selected branch day end checking
                            Date selectedBranchDt = ClientUtil.getOtherBranchCurrentDate(CommonUtil.convertObjToStr(((ComboBoxModel) cboBranchList.getModel()).getKeyForSelected()));                            
                            //System.out.println("selectedBranchDt : "+selectedBranchDt);
//                            if(selectedBranchDt == null){
//                                ClientUtil.showAlertWindow("BOD is not completed for the selected branch " +"\n"+"Interbranch Transaction Not allowed");                                
//                                color = "blue";
//                                cboBranchList.setSelectedItem(((ComboBoxModel) cboBranchList.getModel()).getDataForKey(BRANCH_ID));
//                                lblSelBranch.setText("<html>&nbsp;Selected Branch : <font color=" + color + ">" + CommonUtil.convertObjToStr(cboBranchList.getSelectedItem()) + "</font></html>");
//                                return;
//                            }else if(DateUtil.dateDiff(ClientUtil.getCurrentDate(), selectedBranchDt)!=0){
//                                ClientUtil.showAlertWindow("Application Date is different in the Selected branch " +"\n"+"Interbranch Transaction Not allowed");                                
//                                color = "blue";
//                                cboBranchList.setSelectedItem(((ComboBoxModel) cboBranchList.getModel()).getDataForKey(BRANCH_ID));
//                                lblSelBranch.setText("<html>&nbsp;Selected Branch : <font color=" + color + ">" + CommonUtil.convertObjToStr(cboBranchList.getSelectedItem()) + "</font></html>");
//                                return;
//                            }else { 
                                int confirm = ClientUtil.confirmationAlert("Are you sure want to do interbranch transaction", 1);
                                if (confirm == 0) {
                                    color = "red";
                                    lblSelBranch.setText("<html>&nbsp;Selected Branch : <font color=" + color + ">" + CommonUtil.convertObjToStr(cboBranchList.getSelectedItem()) + "</font></html>");
                                } else {
                                    color = "blue";
                                    cboBranchList.setSelectedItem(((ComboBoxModel) cboBranchList.getModel()).getDataForKey(BRANCH_ID));
                                    lblSelBranch.setText("<html>&nbsp;Selected Branch : <font color=" + color + ">" + CommonUtil.convertObjToStr(cboBranchList.getSelectedItem()) + "</font></html>");
                                }
//                            }
                        }
                        lblSelBranch.setText("<html>&nbsp;Selected Branch : <font color=" + color + ">" + CommonUtil.convertObjToStr(cboBranchList.getSelectedItem()) + "</font></html>");
                    }
//                }
                selBranch = CommonUtil.convertObjToStr(((ComboBoxModel) cboBranchList.getModel()).getKeyForSelected());
    }//GEN-LAST:event_cboBranchListActionPerformed
            private void mitPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitPasswordActionPerformed
                ConfigPasswordTO confPwdTO = (ConfigPasswordTO) ClientUtil.executeQuery("getSelectConfigPasswordTO", new HashMap()).get(0);
                if (!confPwdTO.getUserCannotChangepwd().equals("T")) {
                    ChangePasswordUI objChangePasswordUI = new ChangePasswordUI();
                    showDialog(objChangePasswordUI);
                } else {
                    ClientUtil.showMessageWindow("This option has been disabled...");
                }
    }//GEN-LAST:event_mitPasswordActionPerformed
                private void mitCollapseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCollapseActionPerformed
                    // Add your handling code here:
                    collapseAll(treModules);
    }//GEN-LAST:event_mitCollapseActionPerformed
                    private void mitExpandAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitExpandAllActionPerformed
                        // Add your handling code here:
                        expandAll(treModules);
    }//GEN-LAST:event_mitExpandAllActionPerformed
                        private void mitDisconnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDisconnectActionPerformed
                            // Add your handling code here:
                            logOut();
    }//GEN-LAST:event_mitDisconnectActionPerformed
                            private void mitDownloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDownloadActionPerformed
                                // Add your handling code here:
                                TTProgress ttProgress = new TTProgress();
                                showWindow(ttProgress);
    }//GEN-LAST:event_mitDownloadActionPerformed
                                private void mnuLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuLoginActionPerformed
                                    // Add your handling code here:
    }//GEN-LAST:event_mnuLoginActionPerformed
                                    private void treModulesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_treModulesMouseClicked
                                        // Add your handling code here:
                                        if (evt.getClickCount() == 2) {
                                            treModulesValueChanged(null);
                                        }
    }//GEN-LAST:event_treModulesMouseClicked
                                        private void mitTiledActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitTiledActionPerformed
                                            // Add your handling code here:
                                            setupTileWindows();
    }//GEN-LAST:event_mitTiledActionPerformed
                                            private void mitCascadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCascadeActionPerformed
                                                // Add your handling code here:
                                                setupCascadeWindows();
    }//GEN-LAST:event_mitCascadeActionPerformed
                                                private void txtFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFindActionPerformed
                                                    // Add your handling code here:
                                                    btnFindActionPerformed(evt);
    }//GEN-LAST:event_txtFindActionPerformed
                                                    private void btnFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindActionPerformed
                                                        // Add your handling code here:
                                                        // Search Option
                                                        if (!txtFind.getText().trim().equals("")) {
                                                            int pos = treModules.getMaxSelectionRow();
                                                            pos = (pos == -1 || pos == (treModules.getRowCount() - 1)) ? 0 : pos;
                                                            TreePath path = treModules.getNextMatch(txtFind.getText(), ++pos,
                                                                    javax.swing.text.Position.Bias.Forward);
                                                            treModules.setSelectionPath(path);
                                                        }
    }//GEN-LAST:event_btnFindActionPerformed
    private void treModulesValueChanged(javax.swing.event.TreeSelectionEvent evt) {                                        
        // Add your handling code here:
        try {
            Runtime.getRuntime().gc();
            CInternalFrame frm = null;
            HashMap nodeMap = null;
            if (treModules.getLastSelectedPathComponent() == null) {
                return;
            }

            String selected = treModules.getLastSelectedPathComponent().toString();
            TreePath path = treModules.getSelectionPath();// .getPath();
//            DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();            
            ScreenModuleTreeNode node = (ScreenModuleTreeNode) path.getLastPathComponent();
            nodeMap = (HashMap) node.getUserObject();
            //System.out.println("nodeMap:" + nodeMap);
            // Invoking already opened screen
            if (cboBranchList.getSelectedIndex() == 0) { // Added by nithya on 06-12-2019 for KD-992
               ClientUtil.showMessageWindow("<html><b>Selected branch</b> should not be empty.</html>");
               return;
            }
            if (screens.containsKey(selected)
                    && !((CInternalFrame) screens.get(selected)).isClosed()) {
                bringFront(selected);

                // Creating new instances of the screen.
            } else { //The following statements commented by Rajesh
//                HashMap whereMap = new HashMap();
//                if (!BRANCH_ID.equals(((ComboBoxModel)cboBranchList.getModel()).getKeyForSelected())) {
//                    whereMap.put("FOREIGN_GROUP_ID", FOREIGN_GROUP_ID);
//                    whereMap.put("SCREEN_ID", CommonUtil.convertObjToStr(nodeMap.get("screenId")));
//                    
//                    if (CommonUtil.convertObjToInt(ClientUtil.executeQuery("chkInterBranchAllowed", whereMap).get(0)) == 0) {
//                        COptionPane.showMessageDialog(this, "Inter Branch Transaction is not allowed for " + selected);
//                        return;
//                    }
//                }


                // Day Begin Condition.
                /*
                 * whereMap.put("BRANCH_CODE", BRANCH_ID); String strStatus =
                 * CommonUtil.convertObjToStr(ClientUtil.executeQuery("chkTransactionAllowed",
                 * whereMap).get(0));
                 *
                 * if (strStatus.equalsIgnoreCase("COMPLETED") &&
                 * !selected.equalsIgnoreCase("Day Begin")) {
                 *
                 * if (strStatus.equalsIgnoreCase("COMPLETED") &&
                 * !selected.equalsIgnoreCase("Day Begin")) {
                 *
                 *
                 * COptionPane.showMessageDialog(this, "Transaction is not
                 * allowed. Because Day Begin is not done."); return; }
                 */

                //System.out.println("===================== Selected 1: " + selected);

                // Check for Dialog Boxes (Other than CInternalFrame)
                int level = 2;
//                if (userName.equalsIgnoreCase("sysadmin")) {
//                    level = 3;
//                }

                if (node.getLevel() == level) {
//                    if (selected.equalsIgnoreCase("Day Begin Cash")) {
//                        TellerCashStockUI begin = new TellerCashStockUI("DAYBEGIN");
//                        begin.setTitle(selected);
//                        showDialog(begin);
//                    } else if (selected.equalsIgnoreCase("Day End Cash")) {
//                        TellerCashStockUI end = new TellerCashStockUI("DAYEND");
//                        end.setTitle(selected);
//                        showDialog(end);
//                    } 
//                    else
                    screenName = selected;
                    moduleName = (node.getParent().toString());
                    insertScreenAccessHistory();
                    if (selected.equalsIgnoreCase("EMI Calculator")) {
                        System.out.println("@#@#@#@#@#@#@# here :::"+emicalc);
                        if (emicalc!=null) {//Added By Revathi 08-mar-2023 reffered By Rajesh (Avoid EMI Calculator multiple time open)
                            emicalc.dispose();
                            emicalc = null;
                        } 
                        emicalc = new TermLoanInstallmentUI();
                        emicalc.setTitle(selected);
                        showDialog(emicalc);
                        emicalc.setModal(false);
                    } else if (selected.equalsIgnoreCase("Check Customer Id")) {
                        CheckCustomerIdUI chkCusdid = new CheckCustomerIdUI();
                        chkCusdid.setTitle(selected);
                        showDialog(chkCusdid);
                    } else if (selected.equalsIgnoreCase("Customize")) {
                        WorkflowUI workFlow = new WorkflowUI();
                        workFlow.show();
                    } else if (CommonUtil.convertObjToInt(nodeMap.get("moduleId")) >= 100) {
//                    else if (nodeMap.get("moduleId").toString().startsWith("100") || 
//                        nodeMap.get("moduleId").toString().startsWith("99")) {
                        ClientUtil.showReport(nodeMap.get("screenClass").toString());
                        //ClientUtil.showReport("CashScrollIB");
                    } else {

                        if (selected.equalsIgnoreCase("Deposit Closing")) {
                            selected = "Deposit Closing/Transfer to Matured Deposit";
                        }
                        frm = getSelectedScreen(selected);
                        if (frm == null) {
                            return;
                        }
                        frm.setScreenID(CommonUtil.convertObjToStr(nodeMap.get("screenId")));
                        frm.setSelectedBranchID(CommonUtil.convertObjToStr(((ComboBoxModel) cboBranchList.getModel()).getKeyForSelected()));
                        // Setting Module for the selected Screen
                        frm.setModule(node.getParent().toString());
                        frm.setScreen(selected);
                        //System.out.println(frm.getScreen());
                        //System.out.println(frm.getModule());
                        // Setting Screen configuration for the selected Screen
//                        frm.setScreenConfig(ClientUtil.getScreenConfigList(frm.getClass().getName())); //Commented by Rajesh
                        HashMap rightsMap = new HashMap();
                        rightsMap.put("newAllowed", nodeMap.get("newAllowed"));
                        rightsMap.put("editAllowed", nodeMap.get("editAllowed"));
                        rightsMap.put("deleteAllowed", nodeMap.get("deleteAllowed"));
                        rightsMap.put("authRejAllowed", nodeMap.get("authRejAllowed"));
                        rightsMap.put("exceptionAllowed", nodeMap.get("exceptionAllowed"));
                        rightsMap.put("printAllowed", nodeMap.get("printAllowed"));
                        rightsMap.put("interbranchAllowed", nodeMap.get("interbranchAllowed"));
                        frm.setScreenConfig(rightsMap);
                        interbranchAllowed = com.see.truetransact.commonutil.CheckForRights.checkForRights(frm);
                        //System.out.println("### Interbranch allowed : " + interbranchAllowed);
                    }
                }
            }

//            if (!BRANCH_ID.equals(((ComboBoxModel) cboBranchList.getModel()).getKeyForSelected())) {
//                if (!interbranchAllowed) {
//                    ClientUtil.showMessageWindow("Interbranch not allowed for this screen...");
//                    frm = null;
//                }
//            }
            if (frm != null) {
                // Selected Screens
                if (selected.equals("LTD")) {
                    selected = "Term Loans/Advance Account";
                }
                screens.put(selected, frm);
//                    System.out.println("#### Screens 2 : "+screens);
                // Menu added in Window Item List.
                mnuWindowItem = new javax.swing.JMenuItem(selected);
                // The following codes added by Rajesh.
                int avail = 0;
                for (int m = 5; m < mnuWindow.getItemCount(); m++) {
                    if (mnuWindow.getItem(m).getText().equals(mnuWindowItem.getText())) {
                        //System.out.println(mnuWindow.getItem(m).getText().equals(mnuWindowItem.getText()));
                        avail++;
                    }
                }
                //System.out.println(avail);
                if (avail == 0) {          // upto here (Rajesh.
                    mnuWindowItem.addActionListener(new java.awt.event.ActionListener() {

                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            // Bringing front whatever screen is clicked in windows list
                            bringFront(evt.getActionCommand());
                        }
                    });
                    mnuWindow.add(mnuWindowItem);
                }
                // Shows the clicked Screen
                showScreen(frm);
            }
            Runtime.getRuntime().gc();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex);
            Runtime.getRuntime().gc();
        }
    }                                       
    
    private void insertScreenAccessHistory(){
        if(CommonUtil.convertObjToStr(screenName).length() > 0 && CommonUtil.convertObjToStr(moduleName).length() > 0) {
            HashMap historyMap = new HashMap();
            historyMap.put(CommonConstants.SCREEN, screenName);
            historyMap.put(CommonConstants.MODULE, moduleName);
            historyMap.put(CommonConstants.USER_ID, USER_ID);
            historyMap.put(CommonConstants.BRANCH_ID, BRANCH_ID);
            historyMap.put(CommonConstants.IP_ADDR, ProxyParameters.IP_ADDR);
            historyMap.put("APPL_DT", applicationDate);
            ClientUtil.execute("insertScreenAccessHistory", historyMap);
        }
    }
    
    private void logOut() {
        try {
            javax.swing.JInternalFrame jFrames[] = dtpTTMain.getAllFrames();
            if (jFrames.length > 0) {
                String message = "";
                if (jFrames.length == 1) {
                    message += "<html>The following window is opened...<br><b>";
                } else {
                    message += "<html>The following windows are opened...<br><b>";
                }
                for (int i = 0; i < jFrames.length; i++) {
                    if (jFrames[i].getTitle().indexOf("[") != -1) {
                        message += jFrames[i].getTitle().substring(0, jFrames[i].getTitle().indexOf("[")) + "<br>";
                    } else {
                        message += jFrames[i].getTitle() + "<br>";
                    }
                }
                ClientUtil.showAlertWindow(message + "</b><br>Close all the opened windows.</html>");
                jFrames = null;
                return;
            }
            jFrames = null;
            int yesNo = ClientUtil.closeAlert();

            if (yesNo == COptionPane.YES_OPTION) {
                // Checks all the existing exported Files
//            if (!getExportFileExists()) {
                LoginUI.loginVisible = true;
                HashMap whereMap = new HashMap();
                whereMap.put("USERID", USER_ID);
                whereMap.put("STATUS", "LOGOUT");
                Date currLogoutDt = new Date();
                whereMap.put("CURR_DATE", currLogoutDt);
                HashMap screenLockMap = new HashMap();
                screenLockMap.put("USER_ID", USER_ID);
                screenLockMap.put("TRANS_DT", ClientUtil.getCurrentDate());
                screenLockMap.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
                ClientUtil.execute("DELETE_SCREEN_LOCK", screenLockMap);
                // Update the database login status as LOGOUT
                ClientUtil.execute("updateUserLogoutStatus", whereMap);

                whereMap = new HashMap();
                whereMap.put("USERID", USER_ID);
                whereMap.put("BRANCHCODE", BRANCH_ID);
                whereMap.put("LOGINSTATUS", "LOGOUT");
                whereMap.put("DATE_TIME", currLogoutDt);
                ClientUtil.execute("loginHistory", whereMap);

                //ClientUtil.execute("deleteAllEditLock", whereMap);
                // Closes the application
                this.dispose();
                java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                String currLogoutTime = dateFormat.format(currLogoutDt);
                ClientUtil.showMessageWindow("<html>Logged in time &nbsp;&nbsp;&nbsp;: " + currLoginTime
                        + "<br>Logged out time : " + currLogoutTime + "</html>");
                System.gc();
                System.exit(0);
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    public void makeLogOut() {
        LoginUI.loginVisible = true;
        HashMap whereMap = new HashMap();
        whereMap.put("USERID", USER_ID);
        whereMap.put("STATUS", "LOGOUT");
        Date currLogoutDt = new Date();
        whereMap.put("CURR_DATE", currLogoutDt);
        HashMap screenLockMap = new HashMap();
        screenLockMap.put("USER_ID", USER_ID);
        screenLockMap.put("TRANS_DT", ClientUtil.getCurrentDate());
        screenLockMap.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
        ClientUtil.execute("DELETE_SCREEN_LOCK", screenLockMap);
        // Update the database login status as LOGOUT
        ClientUtil.execute("updateUserLogoutStatus", whereMap);

        whereMap = new HashMap();
        whereMap.put("USERID", USER_ID);
        whereMap.put("BRANCHCODE", BRANCH_ID);
        whereMap.put("LOGINSTATUS", "LOGOUT");
        whereMap.put("DATE_TIME", currLogoutDt);
        ClientUtil.execute("loginHistory", whereMap);

        //ClientUtil.execute("deleteAllEditLock", whereMap);
        // Closes the application
//        Toolkit.getDefaultToolkit().removeAWTEventListener(globalKeyListener);
        this.dispose();
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        String currLogoutTime = dateFormat.format(currLogoutDt);
        ClientUtil.showMessageWindow("<html>Logged in time &nbsp;&nbsp;&nbsp;: " + currLoginTime
                + "<br>Logged out time : " + currLogoutTime + "</html>");
        System.gc();
        System.exit(0);
    }
       public static void sessionmakeLogOut() {
        LoginUI.loginVisible = true;
        HashMap whereMap = new HashMap();
        whereMap.put("USERID", USER_ID);
        whereMap.put("STATUS", "LOGOUT");
        Date currLogoutDt = new Date();
        whereMap.put("CURR_DATE", currLogoutDt);
        HashMap screenLockMap = new HashMap();
        screenLockMap.put("USER_ID", USER_ID);
        screenLockMap.put("TRANS_DT", getApplicationDate());//ClientUtil.getCurrentDate()
        screenLockMap.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
        //ClientUtil.execute("DELETE_SCREEN_LOCK", screenLockMap);//Commented By Revathi
        // Update the database login status as LOGOUT
        ClientUtil.execute("updateUserLogoutStatus", whereMap);

        whereMap = new HashMap();
        whereMap.put("USERID", USER_ID);
        whereMap.put("BRANCHCODE", BRANCH_ID);
        whereMap.put("LOGINSTATUS", "LOGOUT");
        whereMap.put("DATE_TIME", currLogoutDt);
        ClientUtil.execute("loginHistory", whereMap);

        //ClientUtil.execute("deleteAllEditLock", whereMap);
        // Closes the application
//        Toolkit.getDefaultToolkit().removeAWTEventListener(globalKeyListener);
      //  this.dispose();
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        String currLogoutTime = dateFormat.format(currLogoutDt);
        //ClientUtil.showMessageWindow("<html>Logged in time &nbsp;&nbsp;&nbsp;: " + currLoginTime
              //  + "<br>Logged out time : " + currLogoutTime + "</html>");
        System.gc();
        System.exit(0);
    }
    private void mitExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitExitActionPerformed
        logOut();
    }//GEN-LAST:event_mitExitActionPerformed
        private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
            logOut();
    }//GEN-LAST:event_exitForm

private void cboBranchListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cboBranchListMouseClicked
// TODO add your handling code here:
//    javax.swing.JInternalFrame jFrames[] = dtpTTMain.getAllFrames();
//    if (jFrames.length > 0) {
//        ClientUtil.showAlertWindow("<html>Close all the opened windows then select Branch for InterBranch Transaction</html>");
//        return;
//    }
}//GEN-LAST:event_cboBranchListMouseClicked

    private void mitApplnLockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitApplnLockActionPerformed
        // TODO add your handling code here:
        LoginExpiry();
    }//GEN-LAST:event_mitApplnLockActionPerformed

private void txtSmartCustomerFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSmartCustomerFocusLost
// TODO add your handling code here:
    if(txtSmartCustomer.getText()!=null && txtSmartCustomer.getText().length()>0) {
        HashMap customerMap = new HashMap();
        customerMap.put("CUST_ID",txtSmartCustomer.getText().toUpperCase());
        customerMap.put("BRANCH_ID",TrueTransactMain.BRANCH_ID);
        List custListData = ClientUtil.executeQuery("getSelectAccInfoDisplay",customerMap);
        if(custListData !=null && custListData.size()>0) {
            customerMap = (HashMap) custListData.get(0);
            CInternalFrame frm = new  SmartCustomerUI(customerMap);
            frm.setTitle("Smart Customer");
            showScreen(frm);
            txtSmartCustomer.setText("");
            treModules.grabFocus();
        }else{
            ClientUtil.showAlertWindow("Invalid Smart Card!!");
            txtSmartCustomer.setText("");
            txtSmartCustomer.grabFocus();
            return;
        } 
    }
}//GEN-LAST:event_txtSmartCustomerFocusLost

    private void mnuDueMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mnuDueMouseClicked
        // TODO add your handling code here:
         CInternalFrame frm = new  DisplayDueProductsUI(this);
         showScreen(frm);
    }//GEN-LAST:event_mnuDueMouseClicked

    private void mitExportDBBackupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitExportDBBackupActionPerformed
        // TODO add your handling code here:
        
        //added by rishad 21/07/2015 for avoiding doubling issue
        CommonUtil comm = new CommonUtil();
        final JDialog loading = comm.addProgressBar();
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws InterruptedException {
                try {
                    if (dbBackupResultMap != null) {
                        Process proc;
                        String userName = CommonUtil.convertObjToStr(dbBackupResultMap.get("USER_NAME"));
                        String password = CommonUtil.convertObjToStr(dbBackupResultMap.get("PASSWORD"));
                        String portNo = CommonUtil.convertObjToStr(dbBackupResultMap.get("PORT_NO"));
                        String tnsName = CommonUtil.convertObjToStr(dbBackupResultMap.get("TNS_NAME"));
                        java.util.Date date= new java.util.Date();
                        //System.out.println(new Timestamp(date.getTime()));
                        //System.out.println("currDt : " + ClientUtil.getCurrentDate());
                        String cmd = "exp " + userName + "/" + password + "@" + tnsName + " FILE=C:\\Users\\admin\\Desktop\\"+new SimpleDateFormat("dd-MM-yyyy").format(new Date())+".dmp log=C:\\Users\\admin\\Desktop\\logData.log";
//                        String cmd = "exp " + userName + "/" + password + "@" + tnsName + " FILE=C:\\Users\\admin\\Desktop\\backup.dmp log=C:\\Users\\admin\\Desktop\\logData.log";
                        //System.out.println("cmd : " + cmd);
                        proc = Runtime.getRuntime().exec(cmd);
                        InputStream is = proc.getInputStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(is));
                        String aux = br.readLine();
                        while (aux != null) {
                            //System.out.println(aux);
                            aux = br.readLine();
                        }
                    }
                } catch (Exception e) {
                }
                return null;
            }
            @Override
            protected void done() {
                loading.dispose();
            }
        };
        worker.execute();
        loading.show();
        try {
            worker.get();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }//GEN-LAST:event_mitExportDBBackupActionPerformed
    /*
     * Checking from the User Directory, is the file exist or not... If exists
     * return true.
     */

    private boolean getExportFileExists() throws Exception {
        boolean success = true;

        // User's export directory
        final StringBuffer directory = new StringBuffer().append(
                System.getProperty("user.home")).append("/tt/export/");

        File exportDir = new File(directory.toString());
        String files[] = exportDir.list();
        StringBuffer msgB = new StringBuffer();

        for (int i = 0; i < files.length; i++) {
            msgB.append(files[i] + ", ");
        }

        // File available or not.
        if (files.length == 0) {
            success = false;
        } else {
            COptionPane.showMessageDialog(this, msgB.toString() + " Files are not send to the Branch. Please send those files.");
            success = true;
        }
        return success;
    }

    /*
     * Showing Dialog Opens Dialog box in center for the screen
     */
    public void showDialog(CDialog frm) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frm.pack();

        /*
         * Center frame on the screen
         */
        Dimension frameSize = frm.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        frm.setLocation((((screenSize.width - this.treModules.getWidth()) - frameSize.width) / 2) + 150,
                ((screenSize.height - frameSize.height) / 2));
        frm.setModal(true);
        frm.show();
    }

    /*
     * Showing Internal Frame in center for the screen
     */
    public static void showScreen(CInternalFrame frm) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frm.pack();

        try {
            frm.setFrameIcon(new javax.swing.ImageIcon(frm.getClass().getResource("/com/see/truetransact/ui/images/TT_LOGO.gif")));
            frm.setSelected(true);
        } catch (Exception ex) {
        }

        /*
         * Center frame on the screen
         */
        Dimension frameSize = frm.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
//            frm.setLocation((((screenSize.width - treModules.getWidth()) - frameSize.width) / 2) + 16,
//            ((screenSize.height - frameSize.height) / 2) - 50);
        frm.setLocation(((dtpTTMain.getWidth() - frameSize.width) / 2),
                ((screenSize.height - frameSize.height) / 2) - 50);
        dtpTTMain.add(frm);
        Runnable runner = new FrameShower(frm);
        java.awt.EventQueue.invokeLater(runner);
        //frm.show();
    }

    /*
     * Invokes the selected Screen
     */
    private CInternalFrame getSelectedScreen(String selected) {
        System.out.println("Selected : " + selected);

        CInternalFrame frm = null;
        String selectedBranch = CommonUtil.convertObjToStr(((ComboBoxModel) cboBranchList.getModel()).getKeyForSelected());
        if (selected.equals("Cash")) {
            frm = new CashTransactionUI();
        } else if (selected.equals("Balance")) {
            frm = new com.see.truetransact.ui.transaction.Balance.BalanceUI();
		} else if(selected.equals("Interest Processing")){
            frm = new com.see.truetransact.ui.deposit.interestprocessing.InterestProcessingUI();
        } else if (selected.equals("Denomination Configuration")) {
            frm = new com.see.truetransact.ui.transaction.denominationConfiguration.DenominationConfigurationUI();
        } else if (selected.equals("Denomination Entry")) {
            frm = new com.see.truetransact.ui.transaction.denomination.DenominationConfigurationUI();
        } else if (selected.equals("Reprint")) {
            frm = new com.see.truetransact.ui.transaction.reprintScreen.ReprintUI();
        } else if (selected.equals("Active Member List")) {
            frm = new com.see.truetransact.ui.activememberlist.ActiveMemberListUI();
        } else if (selected.equals("Major/Sub Head")) {
            frm = new HeadUI();
        } else if (selected.equals("Currency Exchange")) {
            frm = new CurrencyExchangeUI();
        } else if (selected.equals("Exchange Rate Parameter")) {
            frm = new ExchangeRateUI();
        } else if (selected.equals("Account Head")) {
            frm = new AccountCreationUI();
        } else if (selected.equals("Account Head Maintenance")) {
            frm = new AccountMaintenanceUI();
        } else if (selected.equals("GL Opening Update")) {
            frm = new GLOpeningUpdateUI();
        } //else if (selected.equals("Transaction Correction"))
        //    frm = new TransactionCorrectionUI();
        else if (selected.equals("Individual")) {
            frm = new IndividualCustUI();
        }
        else if (selected.equals("Member Malayalam Conversion")) {
            frm = new MalayalamDetailsUI();
        } else if (selected.equals("HistoryViewer")) {
            frm = new HistoryViewerUI();
        } else if (selected.equals("Operative Account")) {
            frm = new OperativeAcctProductnewUI();
        } //        else if (selected.equals("Advances"))
        //            frm = new AdvancesProductUI();
        //        else if (selected.equals("Term Loans"))
        else if (selected.equals("Asset Portfolio")) {
            frm = new NewLoanProductUI();
        } else if (selected.equals("AgriCards")) {
            frm = new AgriCardUI();
        } else if (selected.equals("Agri Asset Portfolio")) //AgriLoanProduct
        {
            frm = new NewLoanAgriProductUI();
        } else if (selected.equals("Remittance")) {
            frm = new RemittanceProductUI();
        } else if (selected.equals("Accounts")) {
            frm = new AccountsUI();
        } else if (selected.equals("Account Transfer")) {
            frm = new AccountTransferUI();
        } else if (selected.equals("Charges")) {
            frm = new ChargesUI();
        } else if (selected.equals("Interest")) {
            frm = new InterestUI();
        } else if (selected.equals("Freeze")) {
            frm = new FreezeUI();
        } else if (selected.equals("OD/CC Freeze")) {
            frm = new FreezeUI("ADVANCES");
        } else if (selected.equals("Lien Marking")) {
            frm = new LienMarkingUI();
        } else if (selected.equals("Account Closing")) {
            frm = new AccountClosingUI();
        } else if (selected.equals("Tod Maintenance")) {
            frm = new TodAllowedUI();
        } //        else if(selected.equals("Term Loan / AdvancesClosing"))
        //            frm=new AccountClosingUI();
        //else if (selected.equals("Advances Account"))
        //frm = new AdvancesODCC();
        else if (selected.equals("Branch Setup")) {
            frm = new BranchManagementUI();
        } else if (selected.equals("Transfer")) {
            frm = new TransferUI();
        } else if (selected.equals("Inward Clearing")) {
            frm = new InwardClearingUI(selectedBranch);
        } else if (selected.equals("Outward Clearing")) {
            frm = new OutwardClearingUI();
        } else if (selected.equals("User")) {
            frm = new UserUI();
        } //            else if (selected.equals("Cash Stock - Denominationwise"))
        else if (selected.equals("Cash Stock View- Denominationwise")) {
            frm = new TableDataUI("getDenominationList");
        } else if (selected.equals("Receive Cash Transfer")) {
            frm = new TellerAcceptanceUI();
        } else if (selected.equals("Pending Cash Transfer")) {
            frm = new TableDataUI("getCashTransferNotReceived");
        } else if (selected.equals("Online Users")) {
            if (CommonConstants.SAL_REC_MODULE != null && CommonConstants.SAL_REC_MODULE.equals("Y")) {
                frm = new TableDataUI("getAllTempUsersList");
            }else{
                frm = new TableDataUI("getAllUsersList");
            }
        } else if (selected.equals("Online Branches")) {
            frm = new PingAllUI();
        } else if (selected.equals("Issue")) {
            frm = new RemittanceIssueUI();
        } else if (selected.equals("Payment")) {
            frm = new RemittancePaymentUI();
        }  else if (selected.equals("RTGS/NEFT")) {
            frm = new com.see.truetransact.ui.remittance.rtgs.RtgsRemittanceUI();
        }  else if (selected.equals("RTGS Other Bank Branch Details")) {
            frm = new com.see.truetransact.ui.product.remittance.RTGSOtherBankBranchUI();
        } else if (selected.equals("RTGS Transaction Enquiry")) {
            frm = new com.see.truetransact.ui.remittance.rtgs.RtgsTransactionDetailsUI();
        } else if (selected.equals("Cheque Book Management")) {
            frm = new ChequeBookUI(selectedBranch);
        } else if (selected.equals("A/c No. Enq Based On Cheque Num")) {
            frm = new ActNumEnqByChqNoUI(selectedBranch);
        } else if (selected.equals("Standing Instruction")) {
            frm = new StandingInstructionUI();
        }         
        else if (selected.equals("Reserve Depreciation AcHd")) {
            frm = new ReserveDepreciationAcHdUI();
        } 
        else if (selected.equals("Standing Instruction Daily")) {
            frm = new com.see.truetransact.ui.supporting.standingInstructionDaily.StandingInstructionDailyUI();
        } else if (selected.equals("File Sender")) {
            frm = new SAAJSenderUI();
        } else if (selected.equals("Bank")) {
            frm = new BankUI();
        } else if (selected.equals("Cash Transfer")) {
            frm = new CashStockTransferUI();
        } else if (selected.equals("Employee")) {
            frm = new EmployeeUI();
        } else if (selected.equals("Payroll Salary Structure")) {
            frm = new PayrollSalaryStructureUI();
        } else if (selected.equals("EarningsDeduction")) {
            frm = new EarningsDeductionUI();
        } 
        else if (selected.equals("Salary Process"))
        {
        frm=new SalaryProcessUI();        
        } else if (selected.equals("Leave Details")) {
            frm = new LeaveDetailsUI();
        } else if (selected.equals("Leave Master")) {
            frm = new LeaveMasterUI();
        } else if (selected.equals("Other Bank PassBook Data Entry")) {
            frm = new PassbookDataEntryUI();
        } else if (selected.equals("Terminal Master")) {
            frm = new TerminalUI();
        } else if (selected.equals("Level Setting")) {
            frm = new LevelControlUI();
        } else if (selected.equals("Calendar")) {
            frm = new CalenderHolidaysUI();
        } else if (selected.equals("Liability Portfolio")) {
            frm = new DepositsProductUI();
        }  else if (selected.equals("Daily Commission")) {
            frm = new DailyCommissionUI();
        }else if (selected.equals("Deposit Accounts")) {
            frm = new TermDepositUI();
        }else if(selected.equals("Deposit Account Search")){ // Added by nithya on 22.02.2016 for 3695
            frm = new CheckCustomerDepositsUI();
        } else if (selected.equals("Multiple Deposit Creation")) {
            frm = new MultipleTermDepositUI();
        } else if (selected.equals("Recalculation of DepositInterest")) {
            frm = new RecalculationOfDepositInterestUI();
        } else if (selected.equals("Term Loans/Advance Account")) {
            frm = new com.see.truetransact.ui.termloan.TermLoanUI();
        } else if (selected.equals("Term Loan Repayment Schedule")) { // nithya
            frm = new com.see.truetransact.ui.termloan.repayment.RepaymentScheduleCreationUI();
        } else if (selected.equals("Loan against Term Deposit")) {
            frm = new com.see.truetransact.ui.termloan.depositLoan.DepositLoanUI();
        } else if (selected.equals("Subsidy Provision")) {
            frm = new com.see.truetransact.ui.termloan.loansubsidy.LoanSubsidyUI();
        } else if (selected.equals("User Group")) {
            frm = new GroupUI();
        } else if (selected.equals("Agri Term Loans/Advance Account")) {
            frm = new AgriTermLoanUI();
        } else if (selected.equals("Reference Screens")) {
            frm = new CreateScreensUI();
        } else if (selected.equals("Lookup Master")) {
            frm = new LookupMasterUI();
        } else if (selected.equals("Lodgement")) {
            frm = new LodgementBillsUI();
        } else if (selected.equals("Multiple Account Lodgement")) { // Added by nithya
            frm = new MultipleAccountLodgementUI();
        } else if (selected.equals("Inward Tally")) {
            frm = new InwardClearingTallyUI();
        } else if (selected.equals("Clearing Data")) {
            frm = new ClearingDataImportUI();
        } else if (selected.equals("Clearing Parameters")) {
            frm = new ParameterUI();
        } else if (selected.equals("Bills")) {
            frm = new BillsUI();
        } else if (selected.equals("TDS Deduction")) {
            frm = new TdsDeductionUI();
        } else if (selected.equals("Outward Tally")) {
            frm = new OutwardClearingTallyUI();
        } else if (selected.equals("Corporate")) {
            frm = new CorporateCustomerUI();
        }else if (selected.equals("Change Customer ID Single")) {  // Existing 'Change Customer ID' screen Name changed to 'Change Customer ID Single'
            frm = new CustomerIdChangeUI();
        }else if (selected.equals("Mutiple Customer ID Change")) {
            frm = new com.see.truetransact.ui.customer.multipleCustomerIdChange.MultipleCustomerIdChangeUI();
        } else if (selected.equals("Gahan Customer")) {
            frm = new GahanCustomerUI();
        } else if (selected.equals("Role")) {
            frm = new RoleUI();
        } else if (selected.equals("Bouncing of Instrument")) {
            frm = new BouncingInstrumentwiseUI();
        } else if (selected.equals("Return of Instrument")) {
            frm = new ReturnOfInstrumentsUI();
        } else if (selected.equals("Interest Calculation")) {
            frm = new InterestCalculationUI();
        } else if (selected.equals("Log")) {
            frm = new ViewLogUI();
        } else if (selected.equals("Exchange Rate")) {
            frm = new ForexExchangeUI();
        } else if (selected.equals("Deposit Closing/Transfer to Matured Deposit")) {
            frm = new DepositClosingUI();
        } else if (selected.equals("Interest Rate Maintenance")) {
            frm = new InterestMaintenanceUI();
        } else if (selected.equals("Death Marking")) {
            frm = new DeathMarkingUI();
        } //        else if (selected.equals("Death Marking"))
        //            frm = new DeathMarkingUI();
        //        else if (selected.equals("Death Marking"))
        //            frm = new AccountDeathMarkingUI();
        else if (selected.equals("Deposit Lien")) {
            frm = new DepositLienUI();
        } else if (selected.equals("Deposit Freeze")) {
            frm = new DepositFreezeUI();
        } else if (selected.equals("Audit")) {
            frm = new InspectionUI();
        } else if (selected.equals("Orders")) {
            frm = new DepositRolloverUI();
        } else if (selected.equals("Order Details")) {
            frm = new DepositRolloverDetailsUI();
        } else if (selected.equals("FT Internal")) {
            frm = new FTInternalUI();
        } else if (selected.equals("External Wire")) {
            frm = new ExternalWireUI();
        } else if (selected.equals("Purchase Equity")) {
            frm = new PurchaseEquitiesUI();
        } else if (selected.equals("New Time Deposit")) {
            frm = new NewTimeDepositUI();
        } else if (selected.equals("TAM Maintenance")) {
            frm = new TAMMaintenanceCreateUI();
        } else if (selected.equals("Relationship")) {
            frm = new RelationshipsUI();
        } else if (selected.equals("Drawing Power Maintenance")) {
            frm = new DrawingPowerMaintenanceUI();
        } else if (selected.equals("Day Begin")) {
            frm = new DayBeginProcessUI();
        } else if (selected.equals("Day End")) {
            frm = new DayEndProcessUI();
        } else if (selected.equals("Data Center Day End")) {
            frm = new DCDayEndProcessUI();
        } else if (selected.equals("Data Center Day Begin")) {
            frm = new DCDayBeginProcessUI();
        } else if (selected.equals("Back Dated Transaction")) {
            frm = new com.see.truetransact.ui.batchprocess.BackDatedTransaction.BackDatedTransactionUI();
        } else if (selected.equals("Inventory Master")) {
            frm = new InventoryMasterUI();
        } else if (selected.equals("Inventory Details")) {
            frm = new InventoryDetailsUI();
        } else if (selected.equals("Scheduler")) {
            frm = new ScheduleTaskUI();
        } else if (selected.equals("Escalation")) {
            frm = new MultiLevelControlUI();
        } else if (selected.equals("Branch Screen Group")) {
            frm = new com.see.truetransact.ui.sysadmin.branchgroupscr.BranchGroupUI();
        } else if (selected.equals("Share")) {
            frm = new ShareProductUI();
        } else if (selected.equals("Charges Configuration")) {
            frm = new com.see.truetransact.ui.common.charges.ChargesUI();
        } else if (selected.equals("Share Account")) {
            frm = new ShareUI();
        } else if (selected.equals("Branchwise GL")) {
            frm = new BranchGLUI();
        } else if (selected.equals("Other Bank/Branch")) {
            frm = new OtherBankUI();
        } else if (selected.equals("StateTaluk")) {
            frm = new StateTalukUI();
        } else if (selected.equals("Notice Period")) {
            frm = new NoticePeriodUI();
        } else if (selected.equals("GL Entry")) {
            frm = new GLEntryUI();
        } else if (selected.equals("Carrier")) {
            frm = new CarrierUI();
        } else if (selected.equals("Log Importance Config")) {
            frm = new ViewLogImportanceUI();
        } else if (selected.equals("Alerts configuration")) {
            frm = new SuspiciousConfigUI();
        } else if (selected.equals("Clearing Configuration")) {
            frm = new BankClearingParameterUI();
        } else if (selected.equals("Token Configuration")) {
            frm = new TokenConfigUI();
        } else if (selected.equals("Locker Configuration")) {
            frm = new LockerConfigUI();
        } else if (selected.equals("Token Issue")) {
            frm = new TokenIssueUI();
        } else if (selected.equals("Token Loss")) {
            frm = new TokenLossUI();
        } else if (selected.equals("Security")) {
            frm = new SecurityInsuranceUI();
        } else if (selected.equals("Stop Payment")) {
            frm = new RemitStopPaymentUI();
        } else if (selected.equals("Denomination Config")) {
            frm = new ForexDenominationMasterUI();
        } else if (selected.equals("TDS Maintenance")) {
            frm = new TDSConfigUI();
        } else if (selected.equals("TDS Exemption")) {
            frm = new TDSExemptionUI();
        } else if (selected.equals("Daily Deposit")) {
            frm = new DailyDepositTransUI();
        }else if (selected.equals("Daily Account Transactions")) {
            frm = new com.see.truetransact.ui.transaction.dailyDepositTrans.DailyAccountTransUI();
        } else if (selected.equals("DCB File Upload")) {
            frm = new com.see.truetransact.ui.transaction.dailyDepositTrans.DCBFileUploadUI();
        } else if (selected.equals("Cash Movement")) {
            frm = new CashManagementUI();
        } else if (selected.equals("Agent")) {
            frm = new AgentUI();
        } else if (selected.equals("Generate QRCode")) {
            frm = new QRCodeUI();
        } else if (selected.equals("Share Transfer")) {
            frm = new ShareTransferUI();
        } else if (selected.equals("Block List")) {
            frm = new BlockedListUI();
        } else if (selected.equals("Configuration")) {
            frm = new ConfigurationUI();
        } else if (selected.equals("Share Resolution")) {
            frm = new ShareResolutionUI();
        } else if (selected.equals("GL Limit")) {
            frm = new GLLimitUI();
        } else if (selected.equals("Branch Group")) {
            frm = new com.see.truetransact.ui.sysadmin.branchgroup.BranchGroupUI();
        } else if (selected.equals("View All Transactions")) {
            frm = new com.see.truetransact.ui.common.viewall.ViewAllTransactions();
        } else if (selected.equals("Back Dated Entry Deletion")) {
            frm = new com.see.truetransact.ui.batchprocess.backdatedentrydeletion.BackDatedEntryDeletion();
        } else if (selected.equals("NPA Application")) {
            frm = new NPAApplicationUI();
        } else if (selected.equals("Loan Closing")) {
            frm = new AccountClosingUI("TermLoan");
        } else if (selected.equals("TermLoan Charges")) {
            frm = new com.see.truetransact.ui.termloan.charges.TermLoanChargesUI();
        } else if (selected.equals("Agent Commision Disbursal")) {
            frm = new AgentCommisionDisbursalUI("Agent Commision Disbursal");
        } else if (selected.equals("Enquiry")) {
            frm = new com.see.truetransact.ui.common.viewall.EnquiryUI();
        } else if (selected.equals("NonFinEnquiry")) {
            frm = new com.see.truetransact.ui.common.viewall.NonFinTransactionUI();
        } else if (selected.equals("Print")) {
            frm = new com.see.truetransact.ui.remittance.printingdetails.PrintUI();
        } else if (selected.equals("InstrumentAllotment")) {
            frm = new com.see.truetransact.ui.remittance.printingdetails.InstrumentAllotmentUI();
        } else if (selected.equals("Inventory Movement")) {
            frm = new com.see.truetransact.ui.supporting.InventoryMovement.InventoryMovementUI();
        } else if (selected.equals("Acc No Maintenance")) {
            frm = new AcNoMaintenanceUI();
        } else if (selected.equals("Investment")) {
            frm = new InvestmentsProductUI();
        } else if (selected.equals("AuditEntry")) {
            frm = new AuditEntryUI();
        } else if (selected.equals("InvestmentMaster")) {
            frm = new InvestmentsMasterUI();
        } else if (selected.equals("InvestmentTrans")) {
            frm = new InvestmentsTransUI();
        } else if (selected.equals("InvestmentAmortization/Shifting")) {
            frm = new InvestmentsAmortizationUI();
        } else if (selected.equals("CallMoney")) {
            frm = new CallMoneyUI();
        } else if (selected.equals("CallMoneyExtension")) {
            frm = new CallMoneyExtensionUI();
        } else if (selected.equals("Bills/Cheque Account Maintenance")) {
            frm = new com.see.truetransact.ui.bills.TermLoanUI();
        } else if (selected.equals("ServiceTaxMaster")) {
            frm = new com.see.truetransact.ui.sysadmin.servicetax.ServiceTaxMaintenanceUI();
        } else if (selected.equals("GuaranteeMaster")) {
            frm = new com.see.truetransact.ui.termloan.guarantee.GuaranteeMasterUI();
        } else if (selected.equals("ChargesServiceTax & Divident")) {
            frm = new com.see.truetransact.ui.transaction.chargesServiceTax.ChargesServiceTaxUI();
        } else if (selected.equals("Employee")) {
            frm = new com.see.truetransact.ui.payroll.employee.EmployeeUI();
        } else if (selected.equals("Payroll Salary Structure")) {
            frm = new com.see.truetransact.ui.payroll.salaryStructure.PayrollSalaryStructureUI();
        } else if (selected.equals("EarningsDeduction")) {
            frm = new com.see.truetransact.ui.payroll.earningsDeductionGlobal.EarningsDeductionUI();
        } else if(selected.equals("PF interest Application")) {
                frm = new com.see.truetransact.ui.payroll.pfInterestApplication.PFIntersestApplicationUI();
        }else if(selected.equals("Leave Surrender")) {
                frm = new com.see.truetransact.ui.payroll.leaveSurrender.LeaveSurrenderUI();
        }
        else if(selected.equals("PF Master")) {
                frm = new com.see.truetransact.ui.payroll.pfMaster.PFMasterUI();
        } else if (selected.equals("PassBook")) {
            frm = new com.see.truetransact.ui.common.viewall.PassBookUI();        
        } else if (selected.equals("General_PassBook")) {
            frm = new com.see.truetransact.ui.common.viewall.PassBookNewUI();
        } 
        else if (selected.equals("Audit Closing")) {
            frm = new com.see.truetransact.ui.common.viewall.BalanceSheetClosingScreenUI();
        } 
        else if (selected.equals("Balancesheet Closing")) {
            frm = new com.see.truetransact.ui.common.viewall.GeneralLedgerOpeningEntryScreenUI();
        }
        else if (selected.equals("Loan Notice")) {
            frm = new com.see.truetransact.ui.termloan.notices.LoanNoticeUI();
        } else if (selected.equals("SalaryStructure")) {
            frm = new com.see.truetransact.ui.common.SalaryStructureUI();
        } else if (selected.equals("MiscellaneousDeduction")) {
            frm = new com.see.truetransact.ui.common.MisecllaniousDeductionUI();
        } else if (selected.equals("Deduction")) {
            frm = new com.see.truetransact.ui.common.DeductionUI();
        } else if (selected.equals("Leave Management")) {
            frm = new com.see.truetransact.ui.sysadmin.leavemanagement.LeaveManagementUI();
        } else if (selected.equals("Leave Sanction")) {
            frm = new com.see.truetransact.ui.sysadmin.leavemanagement.LeaveSanctionUI();
        } //             else if(selected.equals("Settlement"))
        //                frm = new com.see.truetransact.ui.common.settlement.SettlementUI();
        //            else if(selected.equals("ActTransfer"))
        //                frm = new com.see.truetransact.ui.common.accounttransfer.ActTransUI();
        //else if(selected.equals("Increments"))
        //    frm = new com.see.truetransact.ui.common.IncrementsPromotionsUI();
        else if (selected.equals("Locker Product")) {
            frm = new com.see.truetransact.ui.product.locker.LockerProdUI();
        } else if (selected.equals("Locker Issue")) {
            frm = new LockerIssueUI();
        } else if (selected.equals("Locker Operation")) {
            frm = new LockerOperationUI();
        } else if (selected.equals("Locker Renew/Surrender")) {
            frm = new LockerSurrenderUI();
        } else if (selected.equals("Service Tax Settings")) {
            if (TrueTransactMain.SERVICE_TAX_REQ.equals("Y")) {
                frm = new ServiceTaxSettingsUI();
            } else {
                ClientUtil.showMessageWindow("Service Tax parameter setting is not done!!");
                return null;
            }
        } else if (selected.equals("Employee Transfer")) {
            frm = new com.see.truetransact.ui.sysadmin.emptransfer.EmpTransferUI();
        } else if (selected.equals("Employee Training")) {
            frm = new com.see.truetransact.ui.payroll.employeeTraining.EmployeeTrainingUI();
        }
        else if (selected.equals("Appraiser Commision"))
        {
            frm = new com.see.truetransact.ui.termloan.appraiserCommision.AppraiserCommisionUI();
        } else if (selected.equals("Appraiser Rate Maintenance")) {
            frm = new com.see.truetransact.ui.termloan.appraiserRateMaintenance.AppraiserRateMaintenanceUI();
        } else if (selected.equals("Gold MarketRate Maintenance")) {
            frm = new com.see.truetransact.ui.termloan.goldLoanConfiguration.GoldConfigurationUI();
        } else if (selected.equals("Fixed Assets")) {
            frm = new com.see.truetransact.ui.sysadmin.fixedassets.FixedAssetsUI();
        } else if (selected.equals("Fixed Assets Description")) {
            frm = new com.see.truetransact.ui.sysadmin.fixedassets.FixedAssetsDescriptionUI();
        } else if (selected.equals("Fixed Assets Individual")) {
            frm = new com.see.truetransact.ui.sysadmin.fixedassets.FixedAssetsIndividualUI();
        } else if (selected.equals("Fixed Assets Transaction")) {
            frm = new com.see.truetransact.ui.sysadmin.fixedassets.FixedAssetsTransUI();
        } else if (selected.equals("Employee Master")) {
            frm = new com.see.truetransact.ui.employee.EmployeeMasterUI();
        } else if (selected.equals("Salary Calculation")) {
            frm = new com.see.truetransact.ui.common.IncrementsPromotionsUI();
        } else if (selected.equals("Supplier Master")) {
            frm = new com.see.truetransact.ui.inventory.SupplierMasterUI();
        } else if (selected.equals("Balance Update")) {
            frm = new com.see.truetransact.ui.supporting.inventory.BalanceUpdateUI();
        } //             else if(selected.equals("Transferring to Matured"))
        //                frm = new com.see.truetransact.ui.deposit.transferringtoMatured.TransferringtoMaturedUI();
        //            else if (selected.equals("Self Help Group"))
        //                frm = new SHGCustomerUI();
        else if (selected.equals("MDS Product")) {
            frm = new com.see.truetransact.ui.product.mds.MDSProductUI();
        } else if (selected.equals("MDS Scheme Creation")) {
            frm = new com.see.truetransact.ui.product.mds.MDSTypeUI();
        } else if (selected.equals("MDS Application New")) {
            frm = new com.see.truetransact.ui.mdsapplication.MDSApplicationUI();
        } else if (selected.equals("MDS Receipt Entry")) {
            frm = new com.see.truetransact.ui.mdsapplication.mdsreceiptentry.MDSReceiptEntryUI();
        } else if (selected.equals("MDS Receipt Entry After Closure")) {
            frm = new com.see.truetransact.ui.mdsapplication.mdsclosedreceipt.MDSClosedReciptUI();
        } else if (selected.equals("MDS Member Receipt Entry")) {
            frm = new com.see.truetransact.ui.mdsapplication.mdsmemberreceiptentry.MDSMemberReceiptEntryUI();
        } else if (selected.equals("MDS Commencement/Closure")) {
            frm = new com.see.truetransact.ui.mdsapplication.mdsconmmencement.MDSCommencementUI();
        } else if (selected.equals("MDS Change of Member")) {
            frm = new com.see.truetransact.ui.mdsapplication.mdschangeofmember.MDSChangeofMemberUI();
        } else if (selected.equals("MDS Prized Money Payment")) {
            frm = new com.see.truetransact.ui.mdsapplication.mdsprizedmoneypayment.MDSPrizedMoneyPaymentUI();
        } else if (selected.equals("MDS Prized Money Details Entry")) {
            frm = new com.see.truetransact.ui.mdsapplication.mdsprizedmoneydetailsentry.MDSPrizedMoneyDetailsEntryUI();
        } else if (selected.equals("MDS Master Maintenance")) {
            frm = new com.see.truetransact.ui.mdsapplication.mdsmastermaintenance.MDSMasterMaintenanceUI();
        } else if (selected.equals("MDS Standing Instruction")) {
            frm = new com.see.truetransact.ui.mdsapplication.MDSStandingInstructionUI();
        } else if (selected.equals("MDS Bank Advance")) {
            frm = new com.see.truetransact.ui.mdsapplication.mdsbankadvance.MDSBankAdvanceUI();
        } else if (selected.equals("MDS Letter Generation")) {
            frm = new com.see.truetransact.ui.mdsapplication.mdslettergeneration.MDSLetterGenerationUI();
        } else if (selected.equals("Cheque Book Maintenance")) {
            frm = new com.see.truetransact.ui.netbankingrequest.nbchequebookmaintenance.NbChequeBookMaintenanceUI();
        } else if (selected.equals("Self Help Group")) {
            frm = new com.see.truetransact.ui.termloan.SHG.SHGUI();
        } else if (selected.equals("SHG Transaction")) {
            frm = new com.see.truetransact.ui.termloan.SHG.SHGTransactionUI();
        } else if (selected.equals("Group Loan")) {
            frm = new com.see.truetransact.ui.termloan.groupLoan.GroupLoanUI();
        } else if (selected.equals("Group Loan Payment")) {
            frm = new com.see.truetransact.ui.termloan.groupLoan.GroupLoanPaymentUI();
        } else if (selected.equals("Group Loan Customer")) {
            frm = new com.see.truetransact.ui.termloan.groupLoan.GroupLoanCustomerUI();
        } else if (selected.equals("Pension Scheme")) {
            frm = new com.see.truetransact.ui.share.pensionScheme.PensionSchemeUI();
        } //             else if(selected.equals("Check Customer Id"))
        //                frm = new com.see.truetransact.ui.customer.CheckCustomerIdUI();
        else if (selected.equals("Deposit Charge Definition")) {
            frm = new com.see.truetransact.ui.common.charges.DepositChargesUI();
        } else if (selected.equals("Loan Charge Definition")) {
            frm = new com.see.truetransact.ui.common.charges.LoanChargesUI();
        } else if (selected.equals("Death Relief Fund Master")) {
            frm = new com.see.truetransact.ui.share.DeathReliefMasterUI();
        } else if (selected.equals("DRF Transaction")) {
            frm = new com.see.truetransact.ui.share.DrfTransactionUI();
        } else if (selected.equals("Share Dividend Calculation")) {
            frm = new com.see.truetransact.ui.share.ShareDividendCalculationUI();
        } else if (selected.equals("Share Dividend Payment")) {
            frm = new com.see.truetransact.ui.share.ShareDividendPaymentUI();
        } else if (selected.equals("Share Dividend Payment Transfer")) {
            frm = new com.see.truetransact.ui.share.dividendTransfer.DividendTransferUI();
        }
        else if (selected.equals("Dividend And Drf")) {
            frm = new com.see.truetransact.ui.share.dividendanddrf.DividendAndDrfUI();
        }else if (selected.equals("Share Conversion")) { // 10-06-2020
            frm = new com.see.truetransact.ui.share.shareconversion.ShareConversionUI();
        }
        else if (selected.equals("LocalityMaster")) {
            frm = new com.see.truetransact.ui.paddyprocurement.PaddyLocalityMasterUI();
        } else if (selected.equals("Paddy Item Opening Stock")) {
            frm = new com.see.truetransact.ui.paddyprocurement.PaddyItemOpeningStockUI();
        } else if (selected.equals("Paddy Purchase Master")) {
            frm = new com.see.truetransact.ui.paddyprocurement.PaddyPurchaseMasterUI();
        } else if (selected.equals("Paddy Sale Master")) {
            frm = new com.see.truetransact.ui.paddyprocurement.PaddySaleMasterUI();
        } else if (selected.equals("Suspense Account Master")) {
            frm = new com.see.truetransact.ui.suspenseaccount.SuspenseAccountMasterUI();
        } else if (selected.equals("Suspense Product")) {
            frm = new com.see.truetransact.ui.suspenseaccount.SuspenseAccountProductUI();
        } //            else if(selected.equals("Membership Liability"))
        //                frm = new com.see.truetransact.ui.common.customer.MembershipLiabilityUI();
        //            else if(selected.equals("Loan Charge Definition"))
        //                frm = new com.see.truetransact.ui.common.charges.LoanChargesUI();
        else if (selected.equals("Deposit Receipt Print")) {
            frm = new com.see.truetransact.ui.deposit.print.DepositPrintUI();
        }else if (selected.equals("Suspense Purchase Transaction")) {
            frm = new com.see.truetransact.ui.indend.suspenseIndend.suspenseIndendUI();
        }else if (selected.equals("Indend Liability")) {
            frm = new com.see.truetransact.ui.indend.indendLiability.IndendLiabilityUI();
        }else if (selected.equals("Deposit Receipt Print Between Periods")) {
            frm = new com.see.truetransact.ui.deposit.print.DepositReprintUI();
        }else if (selected.equals("Borrowing Master")) {
            frm = new com.see.truetransact.ui.borrowings.NewBorrowing();
        } else if (selected.equals("Borrowing Disbursal")) {
            frm = new com.see.truetransact.ui.borrowings.panDisbursal();
        } else if (selected.equals("Borrowing Repayment")) {
            frm = new com.see.truetransact.ui.borrowings.panRepaymentInt();
        } else if (selected.equals("Trading Data Import")) {
            frm = new com.see.truetransact.ui.trading.TradingUI();
        } else if (selected.equals("Cashier Approval")) {
            frm = new com.see.truetransact.ui.transaction.cash.CashierApprovalUI();
        } else if (selected.equals("Multiple Cash Tranasction")) {
            frm = new com.see.truetransact.ui.transaction.multipleCash.MultipleCashTransactionUI();
        } else if (selected.equals("Multiple Standing")) {
            frm = new com.see.truetransact.ui.transaction.multipleStanding.MultipleStandingUI();
        } /*else if (selected.equals("Print Setting")) {
            frm = new com.see.truetransact.ui.sysadmin.printScreen.PrintScreenUI();
        }*/ else if (selected.equals("MultipleMSGTOSingleMsgCustomerwise")) {
            frm = new com.see.truetransact.ui.deposit.multipletosinglecustomermsg.SendSMSUI();
        } else if (selected.equals("Interest Application")) {
            frm = new com.see.truetransact.ui.deposit.interestapplication.DepositInterestApplicationUI();
        }  else if (selected.equals("New Authorization List")) {
            frm = new com.see.truetransact.ui.common.viewall.NewAuthorizeListUI();
        }else if (selected.equals("Locker Rent SI Application")) {
            frm = new com.see.truetransact.ui.locker.lockerSI.LockerRentSIApplicationUI();
        } else if (selected.equals("Authorization List")) {
            frm = new com.see.truetransact.ui.common.viewall.AuthorizeListUI();            
        } else if (selected.equals("Rd Standing Instruction")) {
            frm = new com.see.truetransact.ui.deposit.rdstandinginstruction.RdStandingUI();
        } else if (selected.equals("Query Report")) {
            frm = new com.see.truetransact.ui.transaction.report.QueryReportUI();
        } else if (selected.equals("Roll Back")) {
            if (CommonUtil.convertObjToStr(CommonConstants.OPERATE_MODE).equals("IMPLEMENTATION")) {
                frm = new com.see.truetransact.ui.transaction.rollback.RollBackUI();
            } else {
                ClientUtil.showMessageWindow("Roll Back can be used only During Implementaion Period !!! ");
                return null;
            }
        } else if (selected.equals("Edit Migrated Data")) {                                                                       
            frm = new com.see.truetransact.ui.transaction.editMigratedData.EditMigratedDataUI();
        } else if (selected.equals("Year End Process")) {
            frm = new com.see.truetransact.ui.batchprocess.yearendprocess.YearEndProcessUI();
        } else if (selected.equals("Stores Master")) {
            frm = new com.see.truetransact.ui.indend.frmStore();
        } else if (selected.equals("Sales Master")) {
            frm = new com.see.truetransact.ui.indend.frmSale();
        } else if (selected.equals("Depo Master")) {
            frm = new com.see.truetransact.ui.indend.frmDepo();
        } else if (selected.equals("Indent Transaction")) {
            frm = new com.see.truetransact.ui.indend.frmIndend();
        }  else if (selected.equals("Indent Closing")) {
            frm = new com.see.truetransact.ui.indend.closing.IndendClosingUI();
        }  else if (selected.equals("Reserve/Depreciation Entry")) {
            frm = new com.see.truetransact.ui.indend.reserve.ReserveUI();
        } else if (selected.equals("Rent Parameters")) {
            frm = new com.see.truetransact.ui.roomrent.frmRent();
        } else if (selected.equals("Rent Register")) {
            frm = new com.see.truetransact.ui.roomrent.frmRentRegister();
        } else if (selected.equals("Rent Transaction")) {
            frm = new com.see.truetransact.ui.roomrent.frmRentTrans();
        } else if (selected.equals("SMS Parameters")) {
            frm = new com.see.truetransact.ui.sms.SMSParameterUI();
        } else if (selected.equals("Salary Deduction Mapping")) {
            frm = new com.see.truetransact.ui.salaryrecovery.SalaryDeductionMappingUI();
        } else if (selected.equals("Lock/UnLock")) {
            frm = new com.see.truetransact.ui.salaryrecovery.LockUI();
        } else if (selected.equals("Recovery Parameters")) {
            frm = new com.see.truetransact.ui.salaryrecovery.RecoveryParametersUI();
        } else if (selected.equals("Recovery List Generation")) {
            frm = new com.see.truetransact.ui.salaryrecovery.RecoveryListGenerationUI();
        } else if (selected.equals("Recovery List Tally")) {
            frm = new com.see.truetransact.ui.salaryrecovery.RecoveryListTallyUI();
        } //else if(selected.equals("Gold Loan Renewal"))
        // frm = new com.see.truetransact.ui.termloan.GoldLoanUI("Renewal");
        else if (selected.equals("Gold Loans")) {
            frm = new com.see.truetransact.ui.termloan.GoldLoanUI();
        } else if (selected.equals("Mark Salary Recovery")) {
            frm = new com.see.truetransact.ui.salaryrecovery.MarkSalaryRecoveryUI();
        } else if (selected.equals("Indent Register Print")) {
            frm = new com.see.truetransact.ui.indend.IndendRegisterReportUI();
        } else if (selected.equals("Loan Rebate")) {
            frm = new com.see.truetransact.ui.termloan.loanrebate.LoanRebateUI();
        } else if (selected.equals("Cash Vault Maintenance")) {
            frm = new com.see.truetransact.ui.forex.TellerCashStockUI("DAYBEGIN");
        } else if (selected.equals("General Body Details")) {
            frm = new com.see.truetransact.ui.generalbodydetails.GeneralBodyDetailsUI();
        } else if (selected.equals("Staff Work Diary")) {
            frm = new com.see.truetransact.ui.staffworkdiary.StaffWorkDiaryUI();
        } else if (selected.equals("Visitors Diary")) {
            frm = new com.see.truetransact.ui.visitorsdiary.VisitorsUI();
        } else if (selected.equals("ARC Charge Details")) {
            frm = new com.see.truetransact.ui.supporting.arccharges.ARCChargesUI();
        } else if (selected.equals("Transaction")) {
            frm = new com.see.truetransact.ui.salaryrecovery.TransAllUI();
        } else if (selected.equals("Deduction Exemption(SI)")) {
            frm = new com.see.truetransact.ui.transexception.TransExceptionUI();
        } else if (selected.equals("Loan Application New")) {
            frm = new com.see.truetransact.ui.termloan.loanapplicationregister.LoanApplicationUI();
        } else if (selected.equals("Shift")) {
            frm = new com.see.truetransact.ui.sysadmin.branch.ShiftUI();
        } else if (selected.equals("Visitors Diary")) {
            frm = new com.see.truetransact.ui.visitorsdiary.VisitorsUI();
        } else if (selected.equals("Complaint Register")) {
            frm = new com.see.truetransact.ui.complaintregister.ComplaintRegisterUI();
        } else if (selected.equals("Inward Register")) {
            frm = new com.see.truetransact.ui.inwardregister.InwardUI();            
        } else if (selected.equals("Supplier Master Details")) {
            frm = new com.see.truetransact.ui.indend.suppliermaster.SupplierUI();
        } else if (selected.equals("Director Board Meeting")) {
            frm = new com.see.truetransact.ui.directorboardmeeting.DirectorBoardUI();
        } else if (selected.equals("Directory Board Setting")) {
            frm = new com.see.truetransact.ui.directoryboardsetting.DirectoryBoardSettingUI();
        } else if (selected.equals("Report Setting")) {
            frm = new com.see.truetransact.ui.supporting.depositperiodwisesetting.DepositPeriodwiseSettingUI();
        } else if (selected.equals("Court Expense Setting")) {
            frm = new com.see.truetransact.ui.courtexpensesetting.CourtExpenseSettingUI();
        } else if (selected.equals("Outward Register")) {
            frm = new com.see.truetransact.ui.outwardregister.OutwardUI();
        } else if (selected.equals("Daily Loan Collection")) {
            frm = new com.see.truetransact.ui.termloan.dailyLoanTrans.DailyLoanTransUI();
        } else if (selected.equals("Collecting BankDetails Entry")) {
            frm = new com.see.truetransact.ui.bills.lodgement.CollectingBankDetailsEntryUI();
        } else if (selected.equals("DRF Recovery")) {
            frm = new com.see.truetransact.ui.share.DrfRecoveryUI();
        } else if (selected.equals("Loan Crop Eligibility Maintenance")) {
            frm = new com.see.truetransact.ui.product.loan.loaneligibilitymaintenance.LoanEligibilityMaintenanceUI();
        } else if (selected.equals("Deduction Exemption Mapping")) {
            frm = new com.see.truetransact.ui.salaryrecovery.DeductionExemptionMappingUI();
        } else if (selected.equals("NMF Maintenance")) {
            frm = new com.see.truetransact.ui.share.NmfMaintenanceUI();
        } else if (selected.equals("PurchaseEntry")) {
            frm = new com.see.truetransact.ui.indend.PurchaseEntryUI();
        } //            else if (selected.equals("DRF Recovery"))
        //                frm = new com.see.truetransact.ui.share.DrfRecoveryUI();
        //            else if (selected.equals("Loan Crop Eligibility Maintenance"))
        //                frm = new com.see.truetransact.ui.product.loan.loaneligibilitymaintenance.LoanEligibilityMaintenanceUI();
        //            else if (selected.equals("Deduction Exemption Mapping"))
        //                frm = new com.see.truetransact.ui.salaryrecovery.DeductionExemptionMappingUI();
        else if (selected.equals("RecoveryComparison")) {
            frm = new com.see.truetransact.ui.salaryrecovery.RecoveryComparisonUI();
        } //            else if (selected.equals("Sanction Master"))
        //                frm = new com.see.truetransact.ui.termloan.kcctopacs.SanctionMasterUI();
        else if (selected.equals("Personal Surety Configuration")) {
            frm = new com.see.truetransact.ui.termloan.personalSuretyConfiguration.PersonalSuretyConfigurationUI();
        } else if (selected.equals("Service Wise Loan Amount")) {
            frm = new com.see.truetransact.ui.termloan.personalSuretyConfiguration.ServiceWiseLoanAmountUI();
        } else if (selected.equals("Service Wise Loan Issue Configuration")) {
            frm = new com.see.truetransact.ui.termloan.personalSuretyConfiguration.ServiceWiseLoanIssueConfigurationUI();
        } else if (selected.equals("Deduction Exemption Recovery List Generation")) {
            frm = new com.see.truetransact.ui.salaryrecovery.DeductionExemptionListGenerationUI();
        } else if (selected.equals("Cashier Authorization List")) {
            frm = new com.see.truetransact.ui.salaryrecovery.AuthorizeListCreditUI();
        } else if (selected.equals("Manager Authorization List")) {
            frm = new com.see.truetransact.ui.salaryrecovery.AuthorizeListDebitUI();
        } else if (selected.equals("Other Bank Accounts Product")) {
            frm = new com.see.truetransact.ui.product.accountswithotherbank.AccountswithOtherBankProductUI();
        } else if (selected.equals("Other Bank Accounts Master")) {
            frm = new com.see.truetransact.ui.accountswithotherbank.AccountswithOtherBanksUI();
        } else if (selected.equals("PF Transfer")) {
            frm = new com.see.truetransact.ui.payroll.pftransfer.PFTransferUI();
        } else if (selected.equals("Pay Roll Individual")) {
            frm = new com.see.truetransact.ui.payroll.payMaster.PayRollIndividualUI();
        }else if(selected .equals("Arrear Processing")){
            frm=new com.see.truetransact.ui.payroll.Arrear.ArrearProcessingUI();
        }else if(selected .equals("Arrear Openig Entry")){
            frm=new com.see.truetransact.ui.payroll.Arrear.ArrearOpeningEntryUI();
        }else if (selected.equals("Report View")) {
            frm = new com.see.truetransact.ui.transaction.report.ReportViewUI();
        } else if (selected.equals("Schedule View")) {
            frm = new com.see.truetransact.ui.transaction.ScheduleView.SheduleViewUI();
        } else if (selected.equals("Loan Arbitration")) {
            frm = new com.see.truetransact.ui.termloan.arbitration.LoanArbitrationUI();
        } // KCC START
        else if (selected.equals("Sanction Master")) {
            frm = new com.see.truetransact.ui.termloan.kcctopacs.SanctionMasterUI();
        } else if (selected.equals("Loan Release Master")) {
            frm = new com.see.truetransact.ui.termloan.kcctopacs.ReleaseDetailsUI();
        } else if (selected.equals("Interest Subvention Adjustment")) {
            frm = new com.see.truetransact.ui.termloan.kcctopacs.InterestSubventionAdjustmentUI();
        } else if (selected.equals("Interest Subsidy Adjustment")) {
            frm = new com.see.truetransact.ui.termloan.kcctopacs.InterestSubsidyAdjustmentUI();
        } else if (selected.equals("Loan Repayment")) {
            frm = new com.see.truetransact.ui.termloan.kcctopacs.LoanRepaymentUI();
        } else if (selected.equals("Interest Subsidy Rate Maintenance")) {
            frm = new com.see.truetransact.ui.termloan.kcctopacs.InterestSubsidyRateMaintenanceUI();
        } else if (selected.equals("Release Enquiry")) {
            frm = new com.see.truetransact.ui.termloan.kcctopacs.ReleaseEnquiryUI();
        } else if (selected.equals("Release Closure")) {
            frm = new com.see.truetransact.ui.termloan.kcctopacs.ReleaseClosureUI();
        } // KCC END
        else if (selected.equals("Risk Fund and Loan Charges")) {
            frm = new com.see.truetransact.ui.termloan.riskfund.RiskFundUI();
        } else if (selected.equals("Board Resolution Details")) {
            frm = new com.see.truetransact.ui.boardresolutiondetails.BoardResolutionDetailsUI();
        } else if (selected.equals("Loan Award")) {
            frm = new com.see.truetransact.ui.termloan.arbitration.LoanAwardUI();
        } else if (selected.equals("EP Filing")) {
            frm = new com.see.truetransact.ui.termloan.arbitration.LoanEPFilingUI();
        } else if (selected.equals("Loan Status")) {
            frm = new com.see.truetransact.ui.termloan.loanstatus.LoanStatusUI();
        } else if (selected.equals("Deposit Multiple Renewal")) {
            frm = new com.see.truetransact.ui.deposit.multiplerenewal.DepositMultiRenewal();
        } else if (selected.equals("Increments")) {
            frm = new com.see.truetransact.ui.payroll.increment.IncrementUI();
        } else if (selected.equals("Cheque Leaf Enquiry")) {
            frm = new com.see.truetransact.ui.transaction.chequeenquiry.ChequeEnquiryUI();
        } else if (selected.equals("Deposit Multiple Closing")) {
            frm = new com.see.truetransact.ui.deposit.multipleclosing.DepositMultiClosingUI();
        } else if (selected.equals("Payment Voucher")) {
            frm = new com.see.truetransact.ui.payroll.voucherprocessing.PaymentVoucherUI();
        } else if (selected.equals("Head Consolidation")) {
            frm = new com.see.truetransact.ui.payroll.voucherprocessing.HeadConsolidationUI();
        }else if (selected.equals("Interest Reports")) {
            frm = new com.see.truetransact.ui.termloan.InterestReport.InterestReportUI();
        }else if (selected.equals("Agent Commision Product Wise")) {
            frm = new com.see.truetransact.ui.transaction.agentCommisionDisbursal.AgentCommisionForAllProductsUI();
        }else if (selected.equals("App Agent Collection Details")) {
            frm = new com.see.truetransact.ui.transaction.appagentcollectiondetails.AppAgentCollectionDetailsUI();
        }else if (selected.equals("Agent Commision Slab Rate")) {
            frm = new com.see.truetransact.ui.agent.agentcommisionslabrate.AgentCommisionSlabRateUI();
        }else if(selected.equals("Shop Master")){//Trading Module screen added by Revathi 03-Mar-2015
            frm = new ShopMasterUI();
        }else if(selected.equals("Trading Supplier Master")){
            frm = new TradingSupplierMasterUI();
        }else if(selected.equals("Trading Sales")){
            frm = new TradingSalesUI();
        }else if(selected.equals("Trading Purchase")){
            frm = new TradingPurchaseUI();
        }else if(selected.equals("Trading Transfer")){
            frm = new TradingTransferUI();
        }else if(selected.equals("Damage")){
            frm = new DamageUI();
        }else if(selected.equals("Trading Product")){
            frm = new TradingProductUI();
        }else if(selected.equals("Trading Stock")){
            frm = new TradingStockUI();
        }else if(selected.equals("Trading Group")){
            frm = new TradingGroupUI();
        }else if(selected.equals("Trading Config & A/C Head Maintenance")){
            frm = new TradingAcHeadUI();
        }else if(selected.equals("Trading Purchase Entry")){
            frm = new PurchaseEntryUI();
        }else if (selected.equals("Restore Stock")) {                  
            frm = new com.see.truetransact.ui.trading.restorestock.RestoreStockUI();
        }else if (selected.equals("Group MDS Deposit Product")) {
            frm = new com.see.truetransact.ui.product.groupmdsdeposit.GroupMDSDepositUI();
        }else if (selected.equals("GDSApplication")) {  
            frm = new com.see.truetransact.ui.gdsapplication.GDSApplicationUI();
        }else if (selected.equals("GDSReceiptEntry")) { 
            frm = new com.see.truetransact.ui.gdsapplication.gdsreceiptentry.GDSReceiptEntryUI();
        }else if (selected.equals("GDS Closed Receipt Entry")) { 
            frm = new com.see.truetransact.ui.gdsapplication.gdsclosedreceipt.GDSClosedReciptUI();
        }else if (selected.equals("GDSCommencement")) { 
            frm = new com.see.truetransact.ui.gdsapplication.gdscommencement.GDSCommencementUI();
        }else if (selected.equals("GDS Prized Money Details Entry")) { 
            frm = new com.see.truetransact.ui.gdsapplication.gdsprizedmoneydetailsentry.GDSPrizedMoneyDetailsEntryUI();
        }else if (selected.equals("GDS Prized Money Payment")) { 
            frm = new com.see.truetransact.ui.gdsapplication.gdsprizedmoneypayment.GDSPrizedMoneyPaymentUI();
        }else if (selected.equals("GDS Master Maintenance")) {
            frm = new com.see.truetransact.ui.gdsapplication.gdsmastermaintenance.GDSMasterMaintenanceUI();
        }else if (selected.equals("GDS Standing Instruction")) {
            frm = new com.see.truetransact.ui.gdsapplication.gdsStandingInstruction.GDSStandingInstructionUI();
        } else if (selected.equals("GDS Bank Advance")) {
            frm = new com.see.truetransact.ui.gdsapplication.gdsbankadvance.GDSBankAdvanceUI();
        } 
        else if (selected.equals("GDS Change of Member")) {
            frm = new com.see.truetransact.ui.gdsapplication.gdschangeofmember.GDSChangeofMemberUI();
        } 
        else if (selected.equals("KCC Renewal")) {
            frm = new com.see.truetransact.ui.termloan.kcc.KCCRenewalUI();
        }else if(selected.equals("File Management")){// Added by nithya on 20-01-2019 for KD-379 : Need new screen for file management
            frm = new com.see.truetransact.ui.filemanagement.FileManagementUI();
        }
        else if (selected.equals("Loan Recovery")) {
            frm = new com.see.truetransact.ui.termloan.recovery.LoanRecoveryUI();
        } 
        else if (selected.equals("Loan Transaction")) {
            frm = new com.see.truetransact.ui.termloan.loantransaction.LoanTransactionUI("TermLoan");
        }else if (selected.equals("Batch Process")) {// Added by nithya on 01-03-2019 for KD 437
            frm = new com.see.truetransact.ui.operativeaccount.interestapplication.SBInterestApplicationUI();
        }
        else if (selected.equals("OverDue Remainder")) {
            frm = new com.see.truetransact.ui.termloan.duereminder.OverDueReminderUI();
        }else if (selected.equals("Gold Security")) { // added by nithya on 07-04-2020 for KD 1379 - MDS Gold Security requirements
            frm = new com.see.truetransact.ui.customer.goldsecurity.CustomerGoldSecurityUI();
        } else if (selected.equals("Electronic Payment")) {
            frm = new com.see.truetransact.ui.transaction.electronicpayment.ElectronicPaymentUI();
        } else if (selected.equals("Recon Process")) {
            frm = new com.see.truetransact.ui.transaction.electronicreconciliation.ElectronicReconciliationUI();
        }else if (selected.equals("KCC Multiple Renewal")) {
            frm = new com.see.truetransact.ui.termloan.kcc.multiplerenewal.KCCMultipleRenewalUI();
        }else if (selected.equals("Account Head Ordering")) {
            frm = new com.see.truetransact.ui.generalledger.MajorAccountHeadOrderingUI();
        }else if(selected.equals("Deposit Creation From MobileApp")){
            frm = new com.see.truetransact.ui.deposit.depositfrommobileapp.DepositAutoCreationUI();
        }else if(selected.equals("Kole Field Expense")){
            frm = new com.see.truetransact.ui.kolefieldsoperations.KoleFieldsExpensesUI();
        }else if(selected.equals("KCC Charge Posting")){
            frm = new com.see.truetransact.ui.kolefieldsoperations.chargeposting.KoleFieldKCCChargePostingUI();
        }else if (selected.equals("Gold Stock Release")) {
            frm = new com.see.truetransact.ui.termloan.goldstockrelease.GoldStockReleaselUI();
        }else if (selected.equals("Unlock Accounts")) {
            frm = new com.see.truetransact.ui.supporting.unlockaccounts.UnlockAccountsUI();
        } else if (selected.equals("Subsidy")) {
            frm = new com.see.truetransact.ui.termloan.loansubsidy.LoanSubsidyProvisionUI();
        }else if (selected.equals("Data Correction")) {
             Date maxBackTransDt = null;
            HashMap cbmsMap = new HashMap();
            List list;
            list = ClientUtil.executeQuery("getTillDateForDataCorrectionScreen", null);
            if (list != null && list.size() > 0) {
                cbmsMap = (HashMap) list.get(0);
                maxBackTransDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(cbmsMap.get("CALC_DT")));
            }
            System.out.println("maxBackTransDt :: " + maxBackTransDt);           
            if ((maxBackTransDt != null && (DateUtil.dateDiff(ClientUtil.getCurrentDate(),maxBackTransDt)) < 0)) {
                ClientUtil.displayAlert("Corrections not allowed !!!\n Check AMC date !!! ");                
            }else{
             frm = new com.see.truetransact.ui.correctiontool.DataCorrectionUI();
            }
        }else if(selected.equals("Deposit Auto Renewal")) {
            frm = new com.see.truetransact.ui.deposit.autorenewal.DepositAutoRenewalUI();
        }else if(selected.equals("Loan Auction")) {
            frm = new com.see.truetransact.ui.termloan.loanauction.LoanAuctionDetailsUI();
        }else if (selected.equals("UPI QRCode Generation")) {
            frm = new UPIQRCodeUI();
        }
//        else if (selected.equals("Activity Log")) {
//            frm = new com.see.truetransact.ui.common.viewall.ViewAllActivity();
//        }
        //            ArrayList list = treeOB.getGoldLoanProdIDs();
        GroupOB treeOB = new GroupOB(false);
        if (goldLoanList != null && goldLoanList.size() > 0) {
            for (int i = 0; i < goldLoanList.size(); i++) {
                HashMap resultMap = (HashMap) goldLoanList.get(i);
                if (resultMap.containsKey("TERMLOAN") && resultMap.get("TERMLOAN").equals("Term Loan") && resultMap.get("AUTHORIZE_REMARK").equals("GOLD_LOAN")) {
                    if (resultMap.get("AUTHORIZE_STATUS") != null) {
                        if (resultMap.get("PROD_DESC").equals(selected)) {
                            frm = new com.see.truetransact.ui.termloan.GoldLoanUI("OTHERS", CommonUtil.convertObjToStr(resultMap.get("PROD_ID")));
                        }
                    } else {
                        if (!shownAuthorizeMessage) {
                            ClientUtil.showAlertWindow(resultMap.get("PROD_ID") + " - (" + resultMap.get("PROD_DESC") + ") product not authorized");
                            shownAuthorizeMessage = true;
                            return null;
                        }
                    }
                }
            }
        }

        frm.setTitle(selected + " [" + (String) ((ComboBoxModel) cboBranchList.getModel()).getKeyForSelected() + "]");
        //System.out.println(frm.getTitle());
        return frm;
    }

    private void bringFront(final String sel) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            // need to do this just to be able to display a wait cursor

            public void run() {
                try {
                    ((CInternalFrame) screens.get(sel)).setSelected(true);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * This will display the opened screens in Cascade mode
     */
    public void setupCascadeWindows() {
        javax.swing.JInternalFrame[] frames = dtpTTMain.getAllFrames();
        int x = 0;
        int y = 0;
        int width = dtpTTMain.getWidth() / 2;
        int height = dtpTTMain.getHeight() / 2;
        for (int i = 0; i < frames.length; i++) {
            if (!frames[i].isIcon()) {
                try {
                    /*
                     * try to make maximized frames resizable this might be
                     * vetoed
                     */
                    frames[i].setMaximum(false);
                    frames[i].setLocation(x, y);
                    //frames[i].reshape(x, y, width, height);
                    frames[i].setSelected(true);
                    x += frameDistance;
                    y += frameDistance;

                    // wrap around at the desktop edge
                    if (x + width > dtpTTMain.getWidth()) {
                        x = 0;
                    }
                    if (y + height > dtpTTMain.getHeight()) {
                        y = 0;
                    }
                } catch (java.beans.PropertyVetoException e) {
                }
            }
        }
    }

    // Display all the opened screens in Tiled way
    /**
     * It will set the opened screens in Tile Mode
     */
    public void setupTileWindows() {
        javax.swing.JInternalFrame[] frames = dtpTTMain.getAllFrames();
        // count frames that aren't iconized
        int frameCount = 0;
        for (int i = 0; i < frames.length; i++) {
            if (!frames[i].isIcon()) {
                frameCount++;
            }
        }
        int rows = (int) Math.sqrt(frameCount);
        int cols = frameCount / rows;
        int extra = frameCount % rows;
        // number of columns with an extra row
        int width = dtpTTMain.getWidth() / cols;
        int height = dtpTTMain.getHeight() / rows;
        int r = 0;
        int c = 0;
        for (int i = 0; i < frames.length; i++) {
            if (!frames[i].isIcon()) {
                try {
                    frames[i].setMaximum(false);
                    frames[i].reshape(c * width, r * height, width, height);
                    r++;
                    if (r == rows) {
                        r = 0;
                        c++;
                        if (c == cols - extra) {
                            // start adding an extra row
                            rows++;
                            height = dtpTTMain.getHeight() / rows;
                        }
                    }
                } catch (java.beans.PropertyVetoException e) {
                }
            }
        }
    }

    /**setlogOutTime
     * Show window will center the screen
     *
     * @param objTTSplashScreen Window Object
     */
    public static void showWindow(javax.swing.JWindow objTTSplashScreen) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        /*
         * Center splash screen
         */
        Dimension splashScreenSize = objTTSplashScreen.getSize();
        if (objTTSplashScreen.getHeight() > screenSize.height) {
            splashScreenSize.height = screenSize.height - (screenSize.height * 5 / 100);
        }
        if (objTTSplashScreen.getWidth() > screenSize.width) {
            splashScreenSize.width = screenSize.width - (screenSize.width * 5 / 100);
        }
        objTTSplashScreen.setLocation((screenSize.width - splashScreenSize.width) / 2, (screenSize.height - splashScreenSize.height) / 2);
        objTTSplashScreen.setVisible(true);
    }

    /**
     * TT's main method
     *
     * @param args the command line arguments
     */
    public static void main(String args[]) throws Exception {
        Dimension frameSize;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        /*
         * Create the splash screen
         */
        TTSplashScreen objTTSplashScreen = new TTSplashScreen();
        objTTSplashScreen.pack();
        showWindow(objTTSplashScreen);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ie) {
        };
        objTTSplashScreen.dispose();

        LoginUI loginUI = new LoginUI();

        frameSize = loginUI.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        loginUI.setLocation((screenSize.width - frameSize.width) / 2,
                (screenSize.height - frameSize.height) / 2 - 60);
        loginUI.show();

        USERINFO = loginUI.getAuthentication();
//        System.out.println("User info " + USERINFO);
        if (USERINFO != null) {
            String loginStatus = (String) USERINFO.get("LOGIN_STATUS");
            if (loginStatus != null && loginStatus.equalsIgnoreCase("CANCEL")) {
                System.exit(0);
                /*
                 * } else if (loginStatus != null &&
                 * loginStatus.equalsIgnoreCase("LOGIN")) {
                 * com.see.truetransact.uicomponent.COptionPane.showMessageDialog(null,
                 * "User already logged in"); System.exit(0); } else if (
                 * loginStatus != null && loginStatus.equalsIgnoreCase("LOGOUT")
                 * && DateUtil.getStringDate( (Date)
                 * USERINFO.get("LAST_LOGOUT_DT")).equals(
                 * DateUtil.getStringDate(new Date()))) {
                 * com.see.truetransact.uicomponent.COptionPane.showMessageDialog(null,
                 * "Transaction is not allowed."); System.exit(0);
                 */
            } else {
                HashMap whereMap = new HashMap();
                whereMap.put("USERID", USERINFO.get(CommonConstants.USER_ID).toString());
                whereMap.put("CURR_DATE", ClientUtil.getCurrentDate());
                ClientUtil.execute("updateUserLoginStatus", whereMap);
                BRANCHINFO = (HashMap) ClientUtil.executeQuery("getBranchUserInfo", whereMap).get(0);
                BANKINFO = (HashMap) ClientUtil.executeQuery("getSelectBankTOList", null).get(0);
                TrueTransactMain frm = new TrueTransactMain();
                frameSize = frm.getSize();
                frm.setSize(screenSize.width, screenSize.height - 30);
                frm.show();
            }
        } else {
            com.see.truetransact.uicomponent.COptionPane.showMessageDialog(null, "Please check UserName and Password");
            System.exit(0);
        }
    }

    public void setUserInfo(HashMap userMap) {
        USERINFO = userMap;
    }

    public void setBranchInfo(HashMap branchMap) {
        BRANCHINFO = branchMap;
    }

    public void setBankInfo(HashMap bankInfo) {
        BANKINFO = bankInfo;
    }

    public void setRoleInfo(HashMap roleInfo) {
        ROLEINFO = roleInfo;
    }

    /**
     * Getter for property applicationDate.
     *
     * @return Value of property applicationDate.
     */
    public static java.util.Date getApplicationDate() {
        return applicationDate;
    }

    /**
     * Setter for property applicationDate.
     *
     * @param applicationDate New value of property applicationDate.
     */
    public static void setApplicationDate(java.util.Date applicationDt) {
        applicationDate = applicationDt;
    }
    // The following method added by Rajesh.

    public static javax.swing.JMenu getMenu() {
        return mnuWin;
    }

    /**
     * Getter for property dayEndType.
     *
     * @return Value of property dayEndType.
     */
    public java.lang.String getDayEndType() {
        return dayEndType;
    }

    /**
     * Setter for property dayEndType.
     *
     * @param dayEndType New value of property dayEndType.
     */
    public void setDayEndType(java.lang.String dayEndType) {
        this.dayEndType = dayEndType;
    }
        /**
     * Getter for property logOutTime.
     *
     * @return Value of property logOutTime.
     */
    public Double getlogOutTime() {
        return logOutTime;
    }

    /**
     * Setter for property logOutTime.
     *
     * @param logOutTime New value of property logOutTime.
     */
    public void setlogOutTime(Double logOutTime) {
        this.logOutTime = logOutTime;
    }

    public static boolean isAlreadyOpen() {
        return alreadyOpen;
    }

    public static void setAlreadyOpen(boolean alreadyOpen) {
        TrueTransactMain.alreadyOpen = alreadyOpen;
    }

    
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnFind;
    private com.see.truetransact.uicomponent.CButton cButton1;
    private com.see.truetransact.uicomponent.CButton cButton2;
    private com.see.truetransact.uicomponent.CButton cButton3;
    private com.see.truetransact.uicomponent.CButton cButton4;
    public static com.see.truetransact.uicomponent.CComboBox cboBranchList;
    private static com.see.truetransact.uicomponent.CDesktopPane dtpTTMain;
    private javax.swing.JMenuItem jMenuItem1;
    private com.see.truetransact.uicomponent.CLabel lblBranch;
    public static com.see.truetransact.uicomponent.CLabel lblDate;
    private com.see.truetransact.uicomponent.CLabel lblSelBranch;
    private com.see.truetransact.uicomponent.CLabel lblUser;
    private com.see.truetransact.uicomponent.CMenuBar mbrTrueTransactMain;
    private javax.swing.JMenuItem mitApplnLock;
    private javax.swing.JMenuItem mitCascade;
    private javax.swing.JMenuItem mitCollapse;
    private javax.swing.JMenuItem mitDisconnect;
    private javax.swing.JMenuItem mitDownload;
    private javax.swing.JMenuItem mitExit;
    private javax.swing.JMenuItem mitExpandAll;
    private javax.swing.JMenuItem mitExportDBBackup;
    private javax.swing.JMenuItem mitPassword;
    private javax.swing.JMenuItem mitTiled;
    private javax.swing.JMenu mnuBankName;
    private javax.swing.JMenu mnuDue;
    private javax.swing.JMenu mnuLogin;
    private javax.swing.JMenu mnuWindow;
    private com.see.truetransact.uicomponent.CPanel panFind;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTTMain;
    private com.see.truetransact.uicomponent.CPanel panTree;
    private com.see.truetransact.uicomponent.CScrollPane scrTree;
    private javax.swing.JSplitPane spnTTMain;
    private javax.swing.JSeparator sptExit;
    private javax.swing.JSeparator sptLogin;
    private javax.swing.JSeparator sptWindow;
    private javax.swing.JToolBar tbrTTMain;
    private static javax.swing.JTree treModules;
    private com.see.truetransact.uicomponent.CTextField txtFind;
    private com.see.truetransact.uicomponent.CTextField txtSmartCustomer;
    // End of variables declaration//GEN-END:variables

    

    private static class FrameShower implements Runnable {

        final CInternalFrame frame;

        public FrameShower(CInternalFrame frame) {
            this.frame = frame;
        }

        public void run() {
            frame.show();
        }
    }
    
    private HashMap dbbackupActivity() {      
        HashMap dbBackupResultMap = new HashMap();
        try{
            HashMap dbBackupMap = new HashMap();
            dbBackupMap.put("TRUE_TRANSACT_MAIN_DBBACKUP","TRUE_TRANSACT_MAIN_DBBACKUP");
            dbBackupMap = proxy.executeQuery(dbBackupMap, operationMap);
//            System.out.println("dbBackupMap : "+dbBackupMap);
            HashMap dataBaseMap = (HashMap) dbBackupMap.get("dataBaseMap");
            String urlAddress = CommonUtil.convertObjToStr(dataBaseMap.get("URL_ADDRESS"));
            String userName = CommonUtil.convertObjToStr(dataBaseMap.get("USER_NAME"));
            String password = CommonUtil.convertObjToStr(dataBaseMap.get("PASSWORD"));
            java.net.InetAddress localMachine = java.net.InetAddress.getLocalHost();
//            System.out.println("localMachineName : " + localMachine.getHostName() + "localMachineAddress : " + localMachine.getHostAddress());
//            String[] items= urlAddress.split("\\\\");
            String[] items = urlAddress.split(":");
//            System.out.println(Arrays.toString(items));
            String ipaddress = (String) items[3];
            String portNo = (String) items[4];
            String tnsName = (String) items[5];
            ipaddress = ipaddress.replace("@", "");
//            portNo = portNo.replace(":", "");
//            tnsName = tnsName.replace(":", "");
//            System.out.println("ipaddress : " + ipaddress +" portNo : " + portNo+" tnsName : " + tnsName);
            if(ipaddress.equals(localMachine.getHostName()) || ipaddress.equals(localMachine.getHostAddress())){
                mitExportDBBackup.setVisible(true);
                dbBackupResultMap.put("USER_NAME",userName);
                dbBackupResultMap.put("PASSWORD",password);
                dbBackupResultMap.put("PORT_NO",portNo);
                dbBackupResultMap.put("TNS_NAME",tnsName);
            }
        }catch(Exception e){
            
        }
        return dbBackupResultMap;
    }                             
}
/*
 * class TreeRenderer extends DefaultTreeCellRenderer { javax.swing.Icon
 * defIcon; javax.swing.Icon icon;
 *
 * public TreeRenderer(javax.swing.Icon defIcon, javax.swing.Icon icon) {
 * this.defIcon = defIcon; this.icon = icon; }
 *
 * public java.awt.Component getTreeCellRendererComponent( javax.swing.JTree
 * tree, Object value, boolean sel, boolean expanded, boolean leaf, int row,
 * boolean hasFocus) {
 *
 * super.getTreeCellRendererComponent( tree, value, sel, expanded, leaf, row,
 * hasFocus); if (leaf && isUnderDevelopment(value)) { setIcon(icon);
 * setToolTipText("This module is Under-Development."); } else if (leaf) {
 * setIcon(defIcon); setToolTipText(null); //no tool tip } return this; }
 *
 * protected boolean isUnderDevelopment(Object value) { DefaultMutableTreeNode
 * node = (DefaultMutableTreeNode) value; String title = node.toString(); if
 * (title.equals("Bills") || title.equals("Orders") || title.equals("Order
 * Details") || title.equals("Lodgement")) { return true; } return false; } }
 */
