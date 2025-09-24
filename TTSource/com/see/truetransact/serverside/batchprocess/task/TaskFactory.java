/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * TaskFactory.java
 *
 * Created on May 17, 2004, 4:23 PM
 */
package com.see.truetransact.serverside.batchprocess.task;

import com.see.truetransact.serverside.batchprocess.task.gl.*;
import com.see.truetransact.serverside.batchprocess.task.initializeSerialNumber.InitSerialNumTask;
import com.see.truetransact.serverside.batchprocess.task.operativeaccount.*;
import com.see.truetransact.serverside.batchprocess.task.customer.*;
import com.see.truetransact.serverside.batchprocess.task.operativeaccount.interest.*;
import com.see.truetransact.serverside.batchprocess.task.supporting.standinginstruction.*;
import com.see.truetransact.serverside.batchprocess.task.authorizechk.*;
import com.see.truetransact.serverside.batchprocess.task.cashtally.CashTallyCheckTask;
import com.see.truetransact.serverside.batchprocess.task.gl.glabstract.GlAbstractUpdateTask;
import com.see.truetransact.serverside.batchprocess.task.deposit.flexi.FlexiTask;
import com.see.truetransact.serverside.batchprocess.task.deposit.DepositAutoRenewalTask;
import com.see.truetransact.serverside.batchprocess.task.deposit.DepositAutoRenewalTask;
import com.see.truetransact.serverside.batchprocess.task.datacenter.AllBranchDayEndTask;
import com.see.truetransact.serverside.batchprocess.task.datacenter.AllBranchReconcileTask;
import com.see.truetransact.serverside.charges.ExcessTransChrgesTask;
import com.see.truetransact.serverside.charges.FolioChargesTask;
import com.see.truetransact.serverside.charges.InOperativeChargesTask;
import com.see.truetransact.serverside.charges.MinBalanceChargesTask;
import com.see.truetransact.serverside.batchprocess.task.share.DividendCalcTask;
import com.see.truetransact.serverside.batchprocess.task.loan.NPATask;
import com.see.truetransact.serverside.batchprocess.task.deposit.MaturedDepositTask;
import com.see.truetransact.serverside.batchprocess.task.deposit.DepositMaturingCheckTask;
import com.see.truetransact.serverside.batchprocess.task.loan.LoanDemandCreationTask;
import com.see.truetransact.serverside.batchprocess.task.deposit.flexi.ReverseFlexiTaskonMaturity;
import com.see.truetransact.serverside.batchprocess.task.deposit.flexi.ReverseFlexiTask;
import com.see.truetransact.serverside.batchprocess.task.remittance.RemittanceLapseTransferTask;
import com.see.truetransact.serverside.batchprocess.task.maturedaccounts.MaturedAccountsCheckTask;
import com.see.truetransact.serverside.batchprocess.task.unsecuredloanaccounts.UnSecuredLoanAccountsCheckTask;
import com.see.truetransact.serverside.batchprocess.task.executeLockerRentSi.ExecuteLockerRentSiCheckTask;
import com.see.truetransact.serverside.batchprocess.task.lockerOperation.LockerOperationCheckTask;
import com.see.truetransact.serverside.batchprocess.task.interbranchchk.DifferentDateInterBranchCheck;
import com.see.truetransact.serverside.batchprocess.task.scheduleOperation.ScheduleCheckTask;
import com.see.truetransact.serverside.batchprocess.task.scheduleOperation.TransactionCheckTask;

/**
 *
 * @author bala
 */
public class TaskFactory implements java.io.Serializable {

    /**
     * Creates a new instance of TaskFactory
     */
    public TaskFactory() {
    }

    /**
     * Returns a Task based on configuration.
     *
     * @throws Exception Throws Exception based on the error
     * @return Returns Task based on Configuration
     */
    public static Task createTask(TaskHeader header) throws Exception {
        if (header.getTaskClass().equals("GlAbstractUpdateTask")) {
            return new GlAbstractUpdateTask(header); // Day Begin/End Task
        } else if (header.getTaskClass().equals("NewToOperativeTask")) {
            return new NewToOperativeTask(header);
        } else if (header.getTaskClass().equals("DormantToInOperativeTask")) {
            return new DormantToInOperativeTask(header);
        } else if (header.getTaskClass().equals("OperativeToDormantTask")) {
            return new OperativeToDormantTask(header);
        } else if (header.getTaskClass().equals("MinorToMajorTask")) {
            return new MinorToMajorTask(header);
        } else if (header.getTaskClass().equals("InitSerialNumTask")) {
            return new InitSerialNumTask(header);
        } else if (header.getTaskClass().equals("StandingInstructionTask")) {
            return new StandingInstructionTask(header);
        } else if (header.getTaskClass().equals("InOperativeChargesTask")) {
            return new InOperativeChargesTask(header);
        } else if (header.getTaskClass().startsWith("Select")) {
            return new SelectTask(header);
        } else if (header.getTaskClass().equals("ExcessTransChrgesTask")) {
            return new ExcessTransChrgesTask(header);
        } else if (header.getTaskClass().equals("MinBalanceChargesTask")) {
            return new MinBalanceChargesTask(header);
        } else if (header.getTaskClass().equals("FolioChargesTask")) {
            return new FolioChargesTask(header);
        } else if (header.getTaskClass().equals("AuthorizationCheckTask")) {
            return new AuthorizationCheckTask(header);
        } else if (header.getTaskClass().equals("ZeroBalanceCheckTask")) {
            return new ZeroBalanceCheckTask(header);
        } else if (header.getTaskClass().equals("InwardTallyOutClrgCheckTask")) {
            return new InwardTallyOutClrgCheckTask(header);
        } else if (header.getTaskClass().equals("OutwdClrgPaySlipTallyCheckTask")) {
            return new OutwdClrgPaySlipTallyCheckTask(header);
        } else if (header.getTaskClass().equals("TransferTallyCheckTask")) {
            return new TransferTallyCheckTask(header);
        } else if (header.getTaskClass().equals("CashTallyCheckTask")) {
            return new CashTallyCheckTask(header);
        } else if (header.getTaskClass().equals("BalanceCheckTask")) {
            return new BalanceCheckTask(header);
        } else if (header.getTaskClass().equals("ContraHeadCheckTask")) {
            return new ContraHeadCheckTask(header);
        } else if (header.getTaskClass().equals("OABalanceCheckTask")) {
            return new OABalanceCheckTask(header);
        } else if (header.getTaskClass().equals("CashInHandCheckTask")) {
            return new CashInHandCheckTask(header);
        } else if (header.getTaskClass().equals("UnclearedScheduleCheckTask")) {
            return new UnclearedScheduleCheckTask(header);
        } else if (header.getTaskClass().equals("OAInoperativeBalanceCheckTask")) {
            return new OAInoperativeBalanceCheckTask(header);
        } else if (header.getTaskClass().equals("FlexiTask")) {
            return new FlexiTask(header);
        } else if (header.getTaskClass().equals("DepositAutoRenewalTask")) {
            return new DepositAutoRenewalTask(header);
        } else if (header.getTaskClass().equals("DepositIntTask")) {
            return new DepositIntTask(header);
        } else if (header.getTaskClass().equals("InterestTask")) {
            return new InterestTask(header);
        } else if (header.getTaskClass().equals("DebitIntTask")) {
//            return new InterestTask (header);
            return new DebitIntTask(header);
        } else if (header.getTaskClass().equals("DailyBalanceUpdateTask")) {
            return new DailyBalanceUpdateTask(header);
        } else if (header.getTaskClass().equals("UserCheckTask")) {
            return new UserCheckTask(header);
        } else if (header.getTaskClass().equals("DividendCalcTask")) {
            return new DividendCalcTask(header);
        } else if (header.getTaskClass().equals("NPATask")) {
            return new NPATask(header);
        } else if (header.getTaskClass().equals("PreviousDayEndTask")) {
            return new PreviousDayEndTask(header);
        } else if (header.getTaskClass().equals("StartDayBeginTask")) {
            return new StartDayBeginTask(header);
        } else if (header.getTaskClass().equals("MaturedDepositTask")) {
            return new MaturedDepositTask(header);
        } else if (header.getTaskClass().equals("InterBranchAuthorizationCheck")) {
            return new InterBranchAuthorizationCheck(header);
        } else if (header.getTaskClass().equals("ZeroBalanceAccountCheckTask")) {
            return new ZeroBalanceAccountCheckTask(header);
        } else if (header.getTaskClass().equals("AllBranchDayEndTask")) {
            return new AllBranchDayEndTask(header);
        } else if (header.getTaskClass().equals("AllBranchReconcileTask")) {
            return new AllBranchReconcileTask(header);
        } else if (header.getTaskClass().equals("InterestCalculationTask")) {
            return new InterestCalculationTask(header);
        } else if (header.getTaskClass().equals("LoanDemandCreationTask")) {
            return new LoanDemandCreationTask(header);
        } else if (header.getTaskClass().equals("CashAndTransferAuthCheckTask")) {
            return new CashAndTransferAuthCheckTask(header);
        } else if (header.getTaskClass().equals("ReverseFlexiTaskonMaturity")) {
            return new ReverseFlexiTaskonMaturity(header);
        } else if (header.getTaskClass().equals("ReverseFlexiTask")) {
            return new ReverseFlexiTask(header);
        } else if (header.getTaskClass().equals("RemittanceLapseTransferTask")) {
            return new RemittanceLapseTransferTask(header);
        } else if (header.getTaskClass().equals("TodTask")) {
            return new TodTask(header);
        } else if (header.getTaskClass().equals("DebitToCreditCheckTask")) {
            return new DebitToCreditCheckTask(header);
        } else if (header.getTaskClass().equals("CorpLoanInterestCalculationTask")) {
//            return new CorpLoanInterestCalculationTask(header); Dont delete this line, For LnT
        } else if (header.getTaskClass().equals("DepositMaturingCheckTask")) {
            return new DepositMaturingCheckTask(header);
        } else if (header.getTaskClass().equals("MaturedAccountsCheckTask")) {
            return new MaturedAccountsCheckTask(header);
        } else if (header.getTaskClass().equals("UnSecuredLoanAccountsCheckTask")) {
            return new UnSecuredLoanAccountsCheckTask(header);
        } else if (header.getTaskClass().equals("LockerOperationCheckTask")) {
            return new LockerOperationCheckTask(header);
        } else if (header.getTaskClass().equals("ExecuteLockerRentSiCheckTask")) {
            return new ExecuteLockerRentSiCheckTask(header);
        }else if (header.getTaskClass().equals("DifferentDateInterBranchCheck")) {
            return new DifferentDateInterBranchCheck(header);
        } else if (header.getTaskClass().equals("ScheduleCheck")) {  //added by rishad 21/07/2016
            return new ScheduleCheckTask(header);
        } else if (header.getTaskClass().equals("TransactionDataCheck")) {  //added by rishad 21/07/2016
            return new TransactionCheckTask(header);
        } else if(header.getTaskClass().equals("AgeUpdationTask")){
            return new AgeUpdationTask(header);
        }

        System.out.println("Task Not Found : " + header.getTaskClass());
        throw new TaskNotFoundException("Task Not Found " + header.getTaskClass());
    }

    public static void main(String arg[]) {
        try {
            TaskHeader header = new TaskHeader();
            header.setTaskClass("NewToOperativeTask");
            Task tsk = TaskFactory.createTask(header);
            TaskStatus status = tsk.executeTask();
            System.out.println(status.getStatus());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
//header.getTaskClass().equals("GLTask")) {
//            return new GLTask(header);
//        } else if (        
    }
}
