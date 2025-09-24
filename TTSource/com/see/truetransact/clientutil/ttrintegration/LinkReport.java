/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * LinkReport.java
 *
 * Created on December 3, 2005, 3:29 PM
 */

package com.see.truetransact.clientutil.ttrintegration;

import java.util.ArrayList;
import java.util.HashMap;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.ClientUtil;
/**
 *
 * @author  Bala
 */
public class LinkReport {
    
    private String actNum = "";
    /** Creates a new instance of LinkReport */
    public LinkReport() {
    }
    
    public static void getReports(String screenID, HashMap map) {
        if (map!=null) {
            map.put("BRANCH_ID", com.see.truetransact.ui.TrueTransactMain.BRANCH_ID);
        }
        ArrayList reports = new ArrayList();
//        if (screenID.equals("SCR01065")) { // Transaction -> Cash
//            reports.add("CashScrollDeleted");
//            reports.add("CashScrollIB");
//            reports.add("CashScrollRejected");
//            reports.add("cashReceipt");
//            reports.add("passbook");
//            reports.add("CTR");
//            
//        } 
//        else if (screenID.equals("SCR01048")) { // Accounts -> Accounts
//            reports.add("AccountMasterListing");
//            reports.add("AccountsCreditToDebit");
//            reports.add("AccountCreditLimit");
//            }
//        else if (screenID.equals("SCR01050")) { // Account -> Charges
////            reports.add("AccountCharges");
//             reports.add("DepositInterestAppl");
//             reports.add("DepositInterestDetails");
//             reports.add("DepositIntProvReport");
//             reports.add("OperativeInterest");
//             reports.add("OperativeInterestDetail");
//             reports.add("DepInterestEnq");
//           }
//        else if (screenID.equals("SCR01052")) { // Account -> Freeze
//            reports.add("AccountFreeze");    
//            reports.add("AccountFreezePeriodWise");
//            reports.add("AccountUnFreezePeriodWise");
//           }
//        
//         else if (screenID.equals("SCR01066")) { // Transaction -> Transfer
//            reports.add("TransferScrollIB"); 
//           }
//        else if (screenID.equals("SCR01067")) { // Transaction -> Inward
//            reports.add("Clearing_Schedule");            
//            reports.add("InwardScrollIB");       
//            reports.add("InwardReturnsReg");
//            reports.add("ChequeReturnsRegister");
//       
//            
//           }
//        
//        else if (screenID.equals("SCR01068")) { // Transaction -> Outward
//            reports.add("ClearingHouseOutward");
//            reports.add("ClearingHouseScheOutward");
//            reports.add("Clearing_Schedule");            
//            reports.add("OutwardReturnReg");
//            reports.add("OutwardClearingRegister"); 
//            reports.add("OutwardClearingScroll");             
//            reports.add("SchemewiseDisbursementProgress");
//           }
//          else if (screenID.equals("SCR01041")) { // periodic activity ->Day Begin
//            reports.add("AccStatement");
//            reports.add("BalanceSheet"); 
//            reports.add("CashScrollDeleted");
//            reports.add("CashScrollIB");   
//            reports.add("CashScrollRejected");   
////            reports.add("CashScroll");
//            reports.add("DayBook");
//            reports.add("DepositAcctMaturingList");                     
//            reports.add("GL_Abstract");          
//            reports.add("InwardScrollIB");
//            reports.add("ListAccBalAll");   
//            reports.add("MinBalance");  
//            reports.add("MinorTurningMajorActivity");
//            reports.add("OutwardClearingRegister"); 
//            reports.add("OutwardClearingScroll");             
//            reports.add("SchemewiseDisbursementProgress");            
//            reports.add("SubDay");  
//            reports.add("SystemTx");            
////            reports.add("TDResidualMatuaritywise");  
//            reports.add("TDS");
//            
//           
//            reports.add("TransferScrollIB"); 
//            reports.add("TransactionsOnDate");             
////            reports.add("TDSizewise");             
//            reports.add("tdresidual");  
//           }
//        
//        else if (screenID.equals("SCR01042")) { // periodic activity ->Day End
//            reports.add("AccStatement");
//            reports.add("BalanceSheet"); 
//            reports.add("CashScrollDeleted");
//            reports.add("CashScrollIB");   
//            reports.add("CashScrollRejected");   
////            reports.add("CashScroll");
//            reports.add("DayBook");
//            reports.add("DepositAcctMaturingList");                       
//            reports.add("GL_Abstract");          
//            reports.add("InwardScrollIB");
//            reports.add("ListAccBalAll");   
//            reports.add("MinBalance");  
//            reports.add("MinorTurningMajorActivity");
//            reports.add("OutwardClearingRegister"); 
//            reports.add("OutwardClearingScroll");             
//            reports.add("SchemewiseDisbursementProgress");
//            reports.add("StandingInstructionFailure");  
//            reports.add("SubDay");  
//            reports.add("SystemTx");
////            reports.add("TDResidualMatuaritywise"); 
//            reports.add("tdresidual");
////            reports.add("TDSizewise");
//            reports.add("TDS");
//            reports.add("TODSUnderBMDiscreation");
//            reports.add("TransferScrollIB"); 
//            reports.add("TransactionsOnDate");   
//        }
//        else if (screenID.equals("SCR01121")) { // Share ->Share Resolution
//            reports.add("AllotmentOfShares");
//            reports.add("ShareAccountDetails");
//         }
//        else if (screenID.equals("SCR01103")) { // Share ->Share Transfer
//            reports.add("ShareAccountDetails");
//         }
//         else if (screenID.equals("SCR01075")) { //Supporting ->Cheque Book Management
//            reports.add("ChequeReturnsRegister"); 
//            reports.add("ChequeUsageDetails");
//            reports.add("ChqBookRegister"); 
//            reports.add("StopPaymentRegister");
//         }
//        else if (screenID.equals("SCR01071")) { // Tally ->Inward Tally
//            reports.add("Clearing_Schedule"); 
//            
//         }
//        
//           else if (screenID.equals("SCR01072")) { // Tally ->Outward Tally
//            reports.add("Clearing_Schedule"); 
//            
//         }
//         else if (screenID.equals("SCR01054")) { // Term loan/Advance Account->TermLoan
////            reports.add("DemandPromisNote"); 
////            reports.add("DPNoteStatement");
////            reports.add("DPNoteList"); 
////            reports.add("DrawingPower");
////            reports.add("DPNote");
////            reports.add("FacilitiesUnderBMDiscreation"); 
////            reports.add("HypothecationAgreement");
//            reports.add("HypothecationRepayment");
//            reports.add("LoansRepaymentSchedule");
////            reports.add("LiabilityPortfolio");
////            reports.add("SchemewiseDisbursementProgress");
////            reports.add("TODSUnderBMDiscreation");
////            reports.add("AdvanceAgainstDeposit");
////            reports.add("StopPaymentRegister");
//            reports.add("cashcreditloan");
//             reports.add("SchemewiseDisbursementProgress");
//         }
//        else if (screenID.equals("SCR01007")) { // Asset->Portfolio
////            reports.add("AdvanceAgainstDeposit"); 
////            reports.add("AdvanceAgainstDeposit_2"); 
//         }
//        
//         else if (screenID.equals("SCR01056")) { // Deposit->Termdeposit(Deposit Account)
//            reports.add("DepositAcctDetailsList"); 
//            reports.add("DepositAcctMaturingList");
//            reports.add("DepositInterestAppl");
//            reports.add("DepositInterestDetails");
//            reports.add("DepositIntProvReport");  
//            reports.add("InstitutionalDepositStatement"); 
//            reports.add("MaturityAlert"); 
//            reports.add("DepInterestEnq");
//         }
//        
//        else if (screenID.equals("SCR01043")) { // Customer ->Individual
//            reports.add("individualcustomerdetail"); 
//            
//            
//         }
//        else if (screenID.equals("SCR01044")) { // Customer ->corporate
//            reports.add("corporatecustomerdetails"); 
//            
//         }
//        else if (screenID.equals("SCR01046")) { // Customer ->Security
////            reports.add("StatementAssets"); 
//            
//         }
//        else if (screenID.equals("SCR01062")) { // Remittance ->Issue
//            reports.add("RemitIssueListing");
//         }
//        else if (screenID.equals("SCR01063")) { // Remittance ->Payment
//            reports.add("RemitPaymentCancelListing"); 
//           
//         }
//        
//        else if (screenID.equals("SCR01064")) { // Remittance ->RemitStopPayment
//              reports.add("StopPaymentIssue");
//              reports.add("StopPaymentRegister");
//         }
//        
//        else if (screenID.equals("SCR01061")) { // Deposit ->Deposit Closing
//            reports.add("DepositPrematureClosure"); 
//         }
//        else if (screenID.equals("SCR01057")) { // Common ->InterestCalc
//
//            reports.add("TDInterestRangeClassification");
//         }
//        else if (screenID.equals("SCR01059")) { // Deposit ->Lien
//            reports.add("DepositLien"); 
//         }
//        else if (screenID.equals("SCR01035")) { // sysadmin ->calender
//            reports.add("HolidayList"); 
//         }
//        else if (screenID.equals("SCR01031")) { // sysadmin ->user
//            reports.add("SupendedUsersOnDate"); 
//            reports.add("SuspendUserPeriod"); 
//            reports.add("UserLoginStatus"); 
//         }
//        else if (screenID.equals("SCR01017")) { //Supporting ->Inventory Master
//            reports.add("InventorySecurityItems"); 
//         }
//        else if (screenID.equals("SCR01018")) { // Supporting ->Inventory Details 
//            reports.add("InventorySecurityItems"); 
//         }
//        else if (screenID.equals("SCR01076")) { // Supporting ->Standing Instruction 
//            reports.add("StandingInsRegister"); 
//            reports.add("StandingInstructionFailure"); 
//            reports.add("StandingInstructionSuccess"); 
//         }
//        else if (screenID.equals("SCR01051")) { // Operative Accounts->Lien
//            reports.add("OperativeAccountLien"); 
//         }
//        else if (screenID.equals("SCR01014")) { // Product Management -> Interest Rate Maintenance 
//            reports.add("TDInterestRangeClassification"); 
//         }
        map.put(CommonConstants.BRANCH_ID, map.get("BRANCH_ID"));
        map.put("SCREEN_ID", screenID);
        reports = (ArrayList)ClientUtil.executeQuery("getReportNamesForScreen", map);
        TTIntegration.integration(reports.toArray());
        System.out.println("#$#$ Actnum : "+TTIntegration.getActNum());
    }
    
    public static void main (String arg[]) {
        LinkReport.getReports("SCR01065", null);
        LinkReport.getReports("SCR01048", null);
        LinkReport.getReports("SCR01050", null);
        LinkReport.getReports("SCR01052", null);
        LinkReport.getReports("SCR01067", null);
        LinkReport.getReports("SCR01066", null);
        LinkReport.getReports("SCR01068", null);
        LinkReport.getReports("SCR01041", null);
        LinkReport.getReports("SCR01042", null);
        LinkReport.getReports("SCR01121", null);
        LinkReport.getReports("SCR01103", null);
        LinkReport.getReports("SCR01075", null);
        LinkReport.getReports("SCR01071", null);
        LinkReport.getReports("SCR01072", null);
        LinkReport.getReports("SCR01054", null);
        LinkReport.getReports("SCR01056", null);
        LinkReport.getReports("SCR01043", null);
        LinkReport.getReports("SCR01046", null);
        LinkReport.getReports("SCR01044", null);
        LinkReport.getReports("SCR01062", null);
        LinkReport.getReports("SCR01063", null);
        LinkReport.getReports("SCR01064", null);
        LinkReport.getReports("SCR01061", null);
        LinkReport.getReports("SCR01057", null);
        LinkReport.getReports("SCR01159", null);
        LinkReport.getReports("SCR01135", null);
        LinkReport.getReports("SCR01031", null);
        LinkReport.getReports("SCR01017", null);
        LinkReport.getReports("SCR01018", null);
        LinkReport.getReports("SCR01076", null);
        LinkReport.getReports("SCR01014", null);
        LinkReport.getReports("SCR01051", null);
    }
    
    /**
     * Getter for property actNum.
     * @return Value of property actNum.
     */
    public java.lang.String getActNum() {
        return actNum;
    }
    
    /**
     * Setter for property actNum.
     * @param actNum New value of property actNum.
     */
    public void setActNum(java.lang.String actNum) {
        this.actNum = actNum;
    }
    
}
