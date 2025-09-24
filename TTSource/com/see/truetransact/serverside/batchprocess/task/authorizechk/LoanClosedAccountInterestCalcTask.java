/*
 * InterestCalculationTask.java
 *
 * Created on April 23, 2007, 4:33 PM
 */
package com.see.truetransact.serverside.batchprocess.task.authorizechk;

import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverside.batchprocess.task.Task;
import com.see.truetransact.serverside.batchprocess.task.TaskStatus;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.batchprocess.BatchConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.serverside.common.transaction.TransactionDAO;
import com.see.truetransact.businessrule.validateexception.ValidationRuleException;
import com.see.truetransact.commonutil.exceptionconstants.termloan.TermLoanConstants;
import com.see.truetransact.commonutil.interestcalc.InterestCalculationBean;
import com.see.truetransact.commonutil.interestcalc.InterestCalculationConstants;
import com.see.truetransact.commonutil.interestcalc.LoanCalculateInterest;
import com.see.truetransact.serverside.batchprocess.task.operativeaccount.DailyBalanceUpdateTask;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.interestcalc.Rounding;
import com.see.truetransact.commonutil.interestcalc.InterestCalculationConstants;
import com.see.truetransact.transferobject.termloan.TermLoanCourtDetailsTO;
import com.see.truetransact.transferobject.termloan.TermLoanOTSInstallmentTO;
import com.see.truetransact.serverside.tds.tdscalc.TdsCalc;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.TTException;
import com.ibatis.db.sqlmap.SqlMap;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import org.apache.log4j.Logger;

/**
 *
 * @author ABI
 */
public class LoanClosedAccountInterestCalcTask extends Task {

    private static SqlMap sqlMap = null;
    private boolean nodays;
    private long checkDiffDays;
    private Date chekdateLast;
    private Date checkCurrDate;
    private Date Startdt;
    private long zeroDiff;
    private boolean exist;
    private Date checkThisCDate;
    private Date Lday;
    private String _branchID;
    private int freq;
    private final String INTEREST = "INTEREST";
    private final String PENALTY = "PENALTY";
    private final String ROI = "ROI";
    private List interestList = null;
    private String behaves_like = null;
    private String intRecivableAchd = null;
    private String loanAchd = null;
    private HashMap chargesMap = null;
    private HashMap retraspectiveMap = new HashMap();
    private TaskStatus status;
    private String ROUNDING_NEAREST = "NEAREST_VALUE";
    private String NEAREST_TENS = "NEAREST_TENS";
    private String NEAREST_HUNDREDS = "NEAREST_HUNDREDS";
    private String interestRate = "";
    private boolean prematureLTD = false;
    private Date currDt = null;
    private Date applDt = null;
    private String dayEndType;
    private List branchList;
    private String taskLable;
    private HashMap periodStringMap;
    private String user = "";
    private double total_Penal = 0.0;
    private long adv_uniqKey = 0;
    private HashMap interestsMap;
    private HashMap zeroIntMap;
    private double totalvalidateInterest = 0;

    public LoanClosedAccountInterestCalcTask() throws Exception {
    }

    /**
     * Creates a new instance of InterestCalculationTask
     */
    public LoanClosedAccountInterestCalcTask(TaskHeader header) throws Exception {
        interestsMap = new HashMap();
        periodStringMap = new HashMap();
        periodStringMap.put("DAYS", new Integer(1));
        periodStringMap.put("MONTHS", new Integer(2));
        periodStringMap.put("YEARS", new Integer(3));
        setHeader(header);
        System.out.println("header####" + header);
        _branchID = getHeader().getBranchID();
        currDt = ServerUtil.getCurrentDate(_branchID);
        applDt = (Date) currDt.clone();  // Added by Rajesh
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
        chargesMap = new HashMap();
        chargesMap = header.getTaskParam();
        if (chargesMap != null && chargesMap.containsKey("DAY_END_TYPE")) {
            dayEndType = CommonUtil.convertObjToStr(chargesMap.get("DAY_END_TYPE"));
        }
        if (CommonUtil.convertObjToStr(dayEndType).equals("BANK_LEVEL")) {
            HashMap tempMap = new HashMap();
            if (chargesMap.containsKey("BRANCH_LST")) {
                branchList = (List) chargesMap.get("BRANCH_LST");
                System.out.println("branchList*****" + branchList);
            } else {
                tempMap.put("NEXT_DATE", currDt);
                branchList = (List) sqlMap.executeQueryForList("getAllBranchesFromDayEnd", tempMap);
            }
            tempMap = null;
        } else {
            HashMap tempMap = new HashMap();
            tempMap.put(CommonConstants.BRANCH_ID, getHeader().getBranchID());
            branchList = new ArrayList();
            branchList.add(tempMap);
            tempMap = null;
        }
        if (chargesMap != null && chargesMap.containsKey("INT_CAL_TASK_LABLE")) {
            taskLable = CommonUtil.convertObjToStr(chargesMap.get("INT_CAL_TASK_LABLE"));
        }
    }

    public TaskStatus executeTask() throws Exception {
        _branchID = getHeader().getBranchID();
        status = new TaskStatus();
        status.setStatus(BatchConstants.STARTED);
        HashMap whereMap = new HashMap();
        whereMap.put("BRANCH_CODE", _branchID);
        whereMap.put("CURR_DATE", currDt);
        if (chargesMap != null) {
            whereMap.putAll(chargesMap);
            System.out.println("wheremap#####111  ###" + whereMap);
        }
        /*     THIS IS FOR TERMLOAN INTEREST CALACULATION    */
//        try{
        //            interestCalculationTL(whereMap);//comment for testing purpose more chages is their dont delete it

        //            System.out.println("chargeMap######@@@@"+chargesMap);
        chekdateLast = new Date();
        if (chargesMap != null && chargesMap.containsKey("CHARGESUI")) {
            //                System.out.println("CHARGESUI#####@@@@@@"+chargesMap);
            //for updation of dayend balance
            DailyBalanceUpdateTask dailyBalanceTask = new DailyBalanceUpdateTask(getHeader());
            dailyBalanceTask.executeTask();
            chargesMap.put("PROD_ID", chargesMap.get("PRODUCT_ID"));
            chargesMap.put("CURR_DATE", chargesMap.get("DATE_TO"));

            chekdateLast = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(chargesMap.get("DATE_TO")));
//                chekdateLast= holiydaychecking(whereMap);
            if (DateUtil.dateDiff(chekdateLast, currDt) < 0) {
                chekdateLast = currDt;
            }
            List lst = sqlMap.executeQueryForList("getLoansProduct", chargesMap);
            if (lst != null && lst.size() > 0) {
                HashMap productMap = (HashMap) lst.get(0);
                chargesMap.putAll(productMap);
            }
            chargesMap.put("BATCH_PROCESS", "BATCH_PROCESS");
            System.out.println("CHARGESUI#####@@@@@@" + chargesMap);
            List lstAppdt = sqlMap.executeQueryForList("GETLASTCALCDT", chargesMap);
            if (lstAppdt != null && lstAppdt.size() > 0) {
                HashMap lastAppDt = (HashMap) lstAppdt.get(0);
                chargesMap.putAll(lastAppDt);
                if (lastAppDt.get("AS_CUSTOMER_COMES") != null && lastAppDt.get("AS_CUSTOMER_COMES").equals("Y")) {
                    return status;
                }
            }
            HashMap allProductInt = interestCalcTermLoanAD(chargesMap);

        }

        System.out.println("yy nottt" + chargesMap);
        if (!(chargesMap != null && chargesMap.containsKey("CHARGESUI"))) {
            if (branchList != null && branchList.size() > 0) {
                for (int b = 0; b < branchList.size(); b++) {
                    HashMap branchMap = (HashMap) branchList.get(b);
                    _branchID = CommonUtil.convertObjToStr(branchMap.get(CommonConstants.BRANCH_ID));
                    currDt = ServerUtil.getCurrentDate(_branchID);
                    applDt = (Date) currDt.clone();  // Added by Rajesh
                    HashMap compMap = new HashMap();
                    compMap.put("TASK_NAME", taskLable);
                    compMap.put("DAYEND_DT", currDt);
                    compMap.put("BRANCH_ID", _branchID);
                    List compLst = (List) sqlMap.executeQueryForList("getSelectTaskLst", compMap);
                    compMap = null;
                    String compStatus = "";
                    if (compLst != null && compLst.size() > 0) {
                        compMap = (HashMap) compLst.get(0);
                        compStatus = CommonUtil.convertObjToStr(compMap.get("TASK_STATUS"));
                        compMap = null;
                    }
                    if (compLst.size() <= 0 || compStatus.equals("ERROR")) {
                        try {
                            sqlMap.startTransaction();
                            HashMap errorMap = new HashMap();
                            errorMap.put("ERROR_DATE", currDt);
                            errorMap.put("TASK_NAME", taskLable);
                            errorMap.put("BRANCH_ID", _branchID);
                            sqlMap.executeUpdate("deleteError_showing", errorMap);
                            sqlMap.commitTransaction();
                            whereMap.put(CommonConstants.BRANCH_ID, branchMap.get(CommonConstants.BRANCH_ID));
                            System.out.println("branchMap#####@@@@@@ : " + branchMap);
                            System.out.println("whereMap#####@@@@@@ : " + whereMap);
                            Date dts = holiydaychecking(whereMap);
                            //            if(!(chargesMap !=null && chargesMap.containsKey("CHARGESUI")))
                            if (checkThisCDate != null) {
                                Date curr_dt = (Date) currDt.clone();

                                curr_dt = DateUtil.getDateWithoutMinitues(curr_dt);
                                System.out.println(curr_dt + "curr_dt" + "checkthis cDate#####" + checkThisCDate);
                                if (checkThisCDate.equals(curr_dt)) {
                                    HashMap map = new HashMap();
                                    //                    map.put("PROD_ID","OD");
                                    List lastcaldt = sqlMap.executeQueryForList("getLoansProduct", map);
                                    for (int i = 0; i < lastcaldt.size(); i++) {
                                        map = (HashMap) lastcaldt.get(i);
                                        //                    String stDt=CommonUtil.convertObjToStr(map.get("LAST_INTCALC_DTDEBIT"));
                                        //                    Startdt=DateUtil.getDateMMDDYYYY(stDt);
                                        //as an when customer
                                        List lstAppdt = sqlMap.executeQueryForList("GETLASTCALCDT", map);

                                        System.out.println("welcome to checking prod_map" + map);
                                        HashMap passDate = new HashMap();
                                        passDate.put("CURR_DATE", currDt);
                                        //                    passDate.put("START_DATE",Startdt);
                                        passDate.put("BRANCH_ID", _branchID);
                                        passDate.put("FREQ", new Long(freq));
                                        passDate.put("PROD_ID", map.get("PROD_ID"));
                                        //daily balance updation
                                        HashMap balanceMap = new HashMap();

                                        balanceMap.put("PRODUCT_ID", map.get("PROD_ID"));
                                        TaskHeader dailyTaskHeader = new TaskHeader();
                                        dailyTaskHeader.setTaskParam(balanceMap);

                                        if (map.get("BEHAVES_LIKE").equals("OD")) {
                                            dailyTaskHeader.setProductType("AD");
                                        } else {
                                            dailyTaskHeader.setProductType("TL");
                                        }
                                        dailyTaskHeader.setBranchID(_branchID);
                                        System.out.println("#$#$#$ dailyTaskHeader : " + dailyTaskHeader);
                                        DailyBalanceUpdateTask dailyBalanceTask = new DailyBalanceUpdateTask(dailyTaskHeader);
                                        dailyBalanceTask.executeTask();
                                        passDate.put("BEHAVES_LIKE", map.get("BEHAVES_LIKE"));
                                        passDate.put("BATCH_PROCESS", "BATCH_PROCESS");
                                        System.out.println("getAll record###" + passDate);
                                        String actnum = null;
                                        //testing purpose;
                                        //                            if(CommonUtil.convertObjToStr(map.get("BEHAVES_LIKE")).equals("OD"))
                                        //                                actnum="LAOD300117";
                                        //                            else  TESTING ONLY
                                        //                                actnum="LAGL301039";
                                        //                            passDate.put("ACT_NUM",actnum);
                                        //new interest calculation for TermLoan and Advances (9-dec-2007 new methode)
                                        if (lstAppdt != null && lstAppdt.size() > 0) {
                                            HashMap lastAppDt = (HashMap) lstAppdt.get(0);
                                            passDate.putAll(lastAppDt);
                                            if (lastAppDt.get("AS_CUSTOMER_COMES") != null && lastAppDt.get("AS_CUSTOMER_COMES").equals("Y")) {
                                                continue;
                                            }
                                        }
                                        System.out.println("execute passDate####" + passDate);
                                        HashMap allProductInt = interestCalcTermLoanAD(passDate);
                                    }
//                                     if (status.getStatus() != BatchConstants.ERROR){
//                                            sqlMap.startTransaction();
//                                            if(compStatus.equals("ERROR")){
//                                                HashMap statusMap = new HashMap();
//                                                //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
//                                                statusMap.put("BRANCH_CODE", _branchID);
//                                                statusMap.put("TASK_NAME", taskLable);
//                                                statusMap.put("TASK_STATUS", "COMPLETED");
//                                                statusMap.put("USER_ID", getHeader().getUserID());
//                                                statusMap.put("DAYEND_DT", currDt);
//                                                sqlMap.executeUpdate("updateTskStatus", statusMap);
//                                                statusMap = null;
//                                            }else{
//                                                HashMap statusMap = new HashMap();
//                                                //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
//                                                statusMap.put("BRANCH_CODE", _branchID);
//                                                statusMap.put("TASK_NAME", taskLable);
//                                                statusMap.put("TASK_STATUS", "COMPLETED");
//                                                statusMap.put("USER_ID", getHeader().getUserID());
//                                                statusMap.put("DAYEND_DT", currDt);
//                                                sqlMap.executeUpdate("InsertDayEndStatus", statusMap);
//                                                statusMap = null;
//                                            }
//                                            sqlMap.commitTransaction();
//                                    }

                                }
                                //
                            }
                        } catch (Exception e) {
                            sqlMap.rollbackTransaction();
                            e.printStackTrace();
                            status.setStatus(BatchConstants.ERROR);
//                         sqlMap.rollbackTransaction();
//                        status.setStatus(BatchConstants.ERROR) ;
//                        sqlMap.startTransaction();
//                        if(compStatus.equals("ERROR")){
//                            HashMap statusMap = new HashMap();
//                            //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
//                            statusMap.put("BRANCH_CODE", _branchID);
//                            statusMap.put("TASK_NAME", taskLable);
//                            statusMap.put("TASK_STATUS", "ERROR");
//                            statusMap.put("USER_ID", getHeader().getUserID());
//                            statusMap.put("DAYEND_DT", currDt);
//                            sqlMap.executeUpdate("updateTskStatus", statusMap);
//                            statusMap = null;
//                        }else{
////                            isError = true;
//                            HashMap statusMap = new HashMap();
//                            //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
//                            statusMap.put("BRANCH_CODE", _branchID);
//                            statusMap.put("TASK_NAME", taskLable);
//                            statusMap.put("TASK_STATUS", "ERROR");
//                            statusMap.put("USER_ID", getHeader().getUserID());
//                            statusMap.put("DAYEND_DT", currDt);
//                            sqlMap.executeUpdate("InsertDayEndStatus", statusMap);
//                            statusMap = null;
//                            
//                        }
//                        HashMap errorMap = new HashMap();
//                        errorMap.put("ERROR_DATE",currDt);
//                        errorMap.put("TASK_NAME", taskLable);
//                        errorMap.put("ERROR_MSG",e.getMessage());
//                        System.out.println("Error ERROR_MSG " + e.getMessage());
//                        errorMap.put("ACT_NUM","asd");
//                        errorMap.put("BRANCH_ID", _branchID);
//                        System.out.println("errorMap" + errorMap);
//                        //                        sqlMap.startTransaction();
//                        sqlMap.executeUpdate("insertError_showing", errorMap);
//                        //                        sqlMap.commitTransaction();
//                        //                            System.out.println("Error thrown for Depsoit No " + dataMap.get("DEPOSITNO"));
//                        //                             sqlMap.rollbackTransaction();
//                        sqlMap.commitTransaction();
//                        
//                        e.printStackTrace();
                            //                            continue;
                        }
                    }

                    System.out.println("compStatus,,,," + compStatus);
                    if (!compStatus.equals("COMPLETED")) {
                        System.out.println("nottt doneeeee");
                        if (status.getStatus() != BatchConstants.ERROR) {
                            sqlMap.startTransaction();
                            System.out.println("compStatus..." + compStatus);
                            if (compStatus.equals("ERROR")) {
                                HashMap statusMap = new HashMap();
                                //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
                                statusMap.put("BRANCH_CODE", _branchID);
                                statusMap.put("TASK_NAME", taskLable);
                                statusMap.put("TASK_STATUS", "COMPLETED");
                                statusMap.put("USER_ID", getHeader().getUserID());
                                statusMap.put("DAYEND_DT", currDt);
                                sqlMap.executeUpdate("updateTskStatus", statusMap);
                                statusMap = null;
                            } else {
                                HashMap statusMap = new HashMap();
                                //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
                                statusMap.put("BRANCH_CODE", _branchID);
                                statusMap.put("TASK_NAME", taskLable);
                                statusMap.put("TASK_STATUS", "COMPLETED");
                                statusMap.put("USER_ID", getHeader().getUserID());
                                statusMap.put("DAYEND_DT", currDt);
                                sqlMap.executeUpdate("InsertDayEndStatus", statusMap);
                                statusMap = null;
                            }
                            sqlMap.commitTransaction();
                        } else {
                            if (compStatus.equals("ERROR")) {
                                HashMap statusMap = new HashMap();
                                //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
                                statusMap.put("BRANCH_CODE", _branchID);
                                statusMap.put("TASK_NAME", taskLable);
                                statusMap.put("TASK_STATUS", "ERROR");
                                statusMap.put("USER_ID", getHeader().getUserID());
                                statusMap.put("DAYEND_DT", currDt);
                                sqlMap.executeUpdate("updateTskStatus", statusMap);
                                statusMap = null;
                            } else {
                                //                            isError = true;
                                HashMap statusMap = new HashMap();
                                //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
                                statusMap.put("BRANCH_CODE", _branchID);
                                statusMap.put("TASK_NAME", taskLable);
                                statusMap.put("TASK_STATUS", "ERROR");
                                statusMap.put("USER_ID", getHeader().getUserID());
                                statusMap.put("DAYEND_DT", currDt);
                                sqlMap.executeUpdate("InsertDayEndStatus", statusMap);
                                statusMap = null;

                            }
                        }
                    }
//                          else
//                          {
//                                HashMap statusMap = new HashMap();
//                                //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
//                                statusMap.put("BRANCH_CODE", _branchID);
//                                statusMap.put("TASK_NAME", taskLable);
//                                statusMap.put("TASK_STATUS", "COMPLETED");
//                                statusMap.put("USER_ID", getHeader().getUserID());
//                                statusMap.put("DAYEND_DT", currDt);
//                                System.out.println("statusMap..."+statusMap);
//                                sqlMap.executeUpdate("InsertDayEndStatus", statusMap);
//                                statusMap = null;
//                          }
                }
            }
        }
//        }
//        catch(Exception e){
//            sqlMap.rollbackTransaction();
//            e.printStackTrace();
//            status.setStatus(BatchConstants.ERROR);
//        }

        status.setStatus(BatchConstants.COMPLETED);

        System.out.println("...fffinshed successfullyyy......" + status);

        checkThisCDate = null;
        return status;

    }

    //    //already intcalc TL IS THEIR NOW NEW TYPE USING TOTAL_BALANCE ONLY
    //    public HashMap interestCalcTermLoanAD(HashMap passDate, boolean forEMI)throws Exception{
    //        HashMap penalMap = interestCalcTermLoanAD(passDate);
    //        passDate.put("INSTALL_TYPE","UNIFORM_PRINCIPLE_EMI");
    //        HashMap interestMap = interestCalcTermLoanAD(passDate);
    //    }
    //already intcalc TL IS THEIR NOW NEW TYPE USING TOTAL_BALANCE ONLY
    public HashMap interestCalcTermLoanAD(HashMap passDate) throws Exception {
        prematureLTD = false;
        Double ROI = new Double(0);
        HashMap actListMap = new HashMap();
        HashMap depositMap = new HashMap();
        if (status != null) {
            status.setStatus(BatchConstants.STARTED);
        }
        Date curr_dt = null;
        Date actClosingDt = null;
        String oneMonthInterest = null;
        HashMap insertRecord = new HashMap();
        HashMap lastAppDt = new HashMap();
        String sourceScreen = "";
        Date MatDate = null;
        System.out.println("pass  ...Date####" + passDate);
        String behavelike = CommonUtil.convertObjToStr(passDate.get("BEHAVES_LIKE"));
        if (passDate.containsKey("CALC_ON_MATURITY")) {
            if (passDate.get("CALC_ON_MATURITY") != null && passDate.get("ACT_NUM") != null && passDate.containsKey("ACT_NUM") && passDate.get("CALC_ON_MATURITY").equals("Y") && passDate.containsKey("CALC_ON_MATURITY")) {
                depositMap.put("ACCT_NUM", CommonUtil.convertObjToStr(passDate.get("ACT_NUM")));
                List cumDepositlst = sqlMap.executeQueryForList("getExpiryDateForDepositLoan", depositMap);
                if (cumDepositlst != null && cumDepositlst.size() > 0) {
                    HashMap Depositlst = (HashMap) cumDepositlst.get(0);
                    if (Depositlst != null && Depositlst.containsKey("MATURITY_DT")) {
                        passDate.put("MATURITY_DATE", Depositlst.get("MATURITY_DT"));
                        interestsMap.put("MATURITY_DATE", Depositlst.get("MATURITY_DT"));
                        MatDate = (Date) Depositlst.get("MATURITY_DT");
                    }
                }
                System.out.println("cumDepositlst" + cumDepositlst + " map " + passDate);

                System.out.println("passDate.get(CALC_ON_MATURITY)" + passDate.get("CALC_ON_MATURITY"));
                interestsMap.put("CALC_ON_MATURITY", passDate.get("CALC_ON_MATURITY"));
            }
        }
        if (passDate.containsKey("USER_ID")) {
            user = CommonUtil.convertObjToStr(passDate.get("USER_ID"));
        }
        if (!(passDate.containsKey("PENAL_APP_PRINCIPAL") || passDate.containsKey("PENAL_APP_INTEREST"))) {
            List lstAppdt = sqlMap.executeQueryForList("GETLASTCALCDT", passDate);
            if (lstAppdt != null && lstAppdt.size() > 0) {
                lastAppDt = (HashMap) lstAppdt.get(0);
                passDate.putAll(lastAppDt);
            }
        }
        //        passDate.put("INTEREST_CALCULTION","INTEREST_CALCULTION");//for standard asset only
        if (passDate.containsKey("CURR_DATE") && passDate.get("CURR_DATE") != null) {
            //            String currdt=CommonUtil.convertObjToStr(passDate.get("CURR_DATE"));
            //            curr_dt=DateUtil.getDateMMDDYYYY(currdt);
            //            System.out.println("currdate### before"+curr_dt);
            curr_dt = (Date) passDate.get("CURR_DATE");
            System.out.println("currdate### after" + curr_dt);
            //            if(passDate.containsKey("LOAN_ACCOUNT_CLOSING"))
            //            chekdateLast=DateUtil.addDaysProperFormat(curr_dt,-1);
            //            else
            if (passDate.containsKey("LOAN_ACCOUNT_CLOSING") && passDate.get("LOAN_ACCOUNT_CLOSING") != null) {
                chekdateLast = (Date) DateUtil.addDaysProperFormat(curr_dt, -1);
                System.out.println("checkdatelast####" + chekdateLast);
                //                passDate.remove("INTEREST_CALCULTION");
            }
            //            if(passDate.containsKey("CHARGESUI"))
            //                chekdateLast =curr_dt;
        }

        List allRecords = null;

        HashMap penalInterest = new HashMap();
        System.out.println("##### interestCalcTermLoanAD() : " + passDate);
        if (CommonUtil.convertObjToStr(passDate.get("BEHAVES_LIKE")).equals("OD")) {
            if (passDate.containsKey("ACT_FROM") && passDate.containsKey("ACT_TO")) { //Added By Suresh   Query A/c No Between only take
                passDate.remove("ACT_NUM");
            }
            penalInterest = new HashMap();
            penalInterest.putAll(passDate);
            allRecords = sqlMap.executeQueryForList("getAllClosedLoanRecordOD", passDate);
            if (passDate.containsKey("ACT_FROM") && passDate.containsKey("ACT_TO")) { //Added By Suresh
                passDate.put("ACT_NUM", CommonUtil.convertObjToStr(passDate.get("ACT_FROM")));
            }
        } else {
            if (passDate.containsKey("ACT_FROM") && passDate.containsKey("ACT_TO")) { //Added By Suresh   Query A/c No Between only take
                passDate.remove("ACT_NUM");
            }
            penalInterest = new HashMap();
            if (passDate.containsKey("PRODUCT_RECORD_PENAL") && passDate.get("PRODUCT_RECORD_PENAL") != null) {
                allRecords = (List) passDate.get("PRODUCT_RECORD_PENAL");
            } else if (passDate.containsKey("PRODUCT_RECORD") && passDate.get("PRODUCT_RECORD") != null) {
                allRecords = (List) passDate.get("PRODUCT_RECORD");
            } else {
                allRecords = sqlMap.executeQueryForList("getAllClosedLoanRecord", passDate);
            }
            if (passDate.containsKey("ACT_FROM") && passDate.containsKey("ACT_TO")) { //Added By Suresh
                passDate.put("ACT_NUM", CommonUtil.convertObjToStr(passDate.get("ACT_FROM")));
            }
            penalInterest.putAll(passDate);
        }
        //        System.out.println("allrecordforinterest"+allRecords);
        HashMap productRecord = new HashMap();
        if (allRecords != null && allRecords.size() > 0) {
            for (int j = 0; j < allRecords.size(); j++) {
                try {

                    do {
                        if (!(passDate.containsKey("LOAN_ACCOUNT_CLOSING") && passDate.get("LOAN_ACCOUNT_CLOSING") != null)) {
                            sqlMap.startTransaction();
                        }

                        HashMap returnMap = new HashMap();
                        HashMap singleRecord = new HashMap();
                        HashMap penalInterestMap = new HashMap();
                        HashMap cumDepositMap = null;
                        String intGetFrom = null;
                        productRecord = new HashMap();
                        productRecord = (HashMap) allRecords.get(j);
                        System.out.println("productRecord Record  ### : " + productRecord);
                        if (passDate.containsKey("PRODUCT_RECORD_PENAL") && passDate.get("PRODUCT_RECORD_PENAL") != null) {
                            productRecord.put(PENALTY, PENALTY);
                        }
                        intGetFrom = CommonUtil.convertObjToStr(productRecord.get("INT_GET_FROM"));
                        singleRecord.put("ACT_NUM", productRecord.get("ACCT_NUM"));
                        if (passDate.containsKey("ACCOUNT_LAST_INT_CALC_DT") && passDate.get("ACCOUNT_LAST_INT_CALC_DT") != null) {
                            productRecord.put("LAST_INT_CALC_DT", getProperFormatDate(DateUtil.addDays((Date) productRecord.get("LAST_INT_CALC_DT"), 0)));

                        } else if (passDate.get("ACCOUNT_OPEN_DT") != null && productRecord.get("ACCT_OPEN_DT") != null) {
                            if (DateUtil.dateDiff((Date) passDate.get("ONLINE_DATE"), (Date) productRecord.get("ACCT_OPEN_DT")) > 0) {
                                productRecord.put("LAST_INT_CALC_DT", getProperFormatDate(DateUtil.addDays((Date) productRecord.get("ACCT_OPEN_DT"), -1)));
                            } else {
                                productRecord.put("LAST_INT_CALC_DT", getProperFormatDate(DateUtil.addDays((Date) passDate.get("ONLINE_DATE"), -1)));
                            }
                        } else if (passDate.containsKey("FROM_PERIOD")) {
                            productRecord.put("LAST_INT_CALC_DT", getProperFormatDate((Date) passDate.get("LAST_INT_CALC_DT")));
                        }

                        singleRecord.put("LAST_INT_CALC_DT", getProperFormatDate(productRecord.get("LAST_INT_CALC_DT")));

                        System.out.println("productRecord Record  ###" + productRecord + "singleRecord" + singleRecord);

                        if (!(passDate.containsKey("PRODUCT_RECORD") && passDate.get("PRODUCT_RECORD") != null)) {
                            sqlMap.executeUpdate("deleteLoanInterestTMP", singleRecord);
                        }
                        //  do{comment by abi
                        double totInterest = 0;
                        if (!(passDate.containsKey("LOAN_ACCOUNT_CLOSING") && passDate.get("LOAN_ACCOUNT_CLOSING") != null)) {
                            sqlMap.startTransaction();
                        }

//                    //OTS interest calculation
//                    if(productRecord.containsKey("OTS") && CommonUtil.convertObjToStr(productRecord.get("OTS")).equals("Y")){
//                        returnMap= calculateOTSInterstDetails(productRecord);
//                        return returnMap;
//                    }
                        Date actOpenDt = (Date) productRecord.get("ACCT_OPEN_DT");
//                    if (CommonUtil.convertObjToStr(productRecord.get("ACCT_CLOSE_DT")).length()>0) {
//                        chekdateLast = (Date) DateUtil.addDaysProperFormat((Date)productRecord.get("ACCT_CLOSE_DT"), 0);
//                        productRecord.put("LAST_INT_CALC_DT",DateUtil.addDays(actOpenDt, -1));
//                        curr_dt=(Date)chekdateLast.clone();
//                        currDt=(Date)chekdateLast.clone();
//                        System.out.println("ACCT_CLOSE_DT####" + chekdateLast);
//                    }
                        //for prematurecloser 30 days interest
                        if (passDate.containsKey("PREMATURE_ONEMONTH_INT")) {
                            int prematurePeriod = CommonUtil.convertObjToDouble(productRecord.get("PREMATURE_FREQ")).intValue();
//                        if(DateUtil.dateDiff((Date)productRecord.get("ACCT_OPEN_DT"),curr_dt)<=prematurePeriod) {
                            actOpenDt = (Date) productRecord.get("ACCT_OPEN_DT");
                            int addMethod = CommonUtil.convertObjToInt(periodStringMap.get(CommonUtil.convertObjToStr(productRecord.get("PREMATURE_PERIOD"))));
                            Date addedDate = DateUtil.addDays(actOpenDt, prematurePeriod, addMethod, false);
                            System.out.println("!!!!### curr_dt:" + curr_dt + " / addedDate:" + addedDate);
                            if (DateUtil.dateDiff(curr_dt, addedDate) >= 1) {
//                            if(productRecord.containsKey("LAST_INT_CALC_DT") && productRecord.get("LAST_INT_CALC_DT") !=null) {
                                int premaPeriod = (int) prematurePeriod;
//                                chekdateLast=DateUtil.addDays((Date) productRecord.get("LAST_INT_CALC_DT"),premaPeriod,true);
                                oneMonthInterest = CommonUtil.convertObjToStr(productRecord.get("PREMATURE_INT_CALC_AMT"));
//                                System.out.println("oneMonthInterest###"+oneMonthInterest+"chekdateLast"+chekdateLast);
                                System.out.println("oneMonthInterest###" + oneMonthInterest + " / curr_dt" + curr_dt);
                                interestList = getInterestDetails(productRecord, passDate);

                                if (oneMonthInterest.equals("LOANSANCTIONAMT")) {
                                    return (oneMonthInterstCalculationforSanctionAmt(productRecord, curr_dt));
                                } else if (oneMonthInterest.equals("LOANOUTSTANDINGAMT")) {
                                    return (oneMonthInterstCalculationforOutstandingAmt(productRecord, curr_dt));
                                }
//                            }
                            }

                        }

                        //for return asset status roi
                        insertRecord.put("ASSET_STATUS", productRecord.get("ASSET_STATUS"));
                        interestList = getInterestDetails(productRecord, passDate);
                        if (interestList != null && interestList.size() > 0) {
                            HashMap map = (HashMap) interestList.get(0);
                            System.out.println("map   mm :" + map);
                            insertRecord.put("ROI", map.get("INTEREST"));
                        }
                        if (productRecord.containsKey("SECRETARIAT_INT") && CommonUtil.convertObjToStr(productRecord.get("SECRETARIAT_INT")).equals("Y")) {
                            return emiCalculaterforKerla(productRecord, interestList);

                        }
                        //FOR EMI INTEREST CALCULATION
                        if (productRecord != null && productRecord.containsKey("INSTALL_TYPE") && productRecord.get("INSTALL_TYPE") != null
                                && // ((productRecord.get("INSTALL_TYPE").equals("UNIFORM_PRINCIPLE_EMI") && productRecord.get("EMI_IN_SIMPLEINTREST").equals("Y")) ) && (!productRecord.get("INSTALL_TYPE").equals("LUMP_SUM"))
                                (!(productRecord.get("INSTALL_TYPE").equals("UNIFORM_PRINCIPLE_EMI")))&&(!productRecord.get("INSTALL_TYPE").equals("USER_DEFINED")) && (!productRecord.get("INSTALL_TYPE").equals("LUMP_SUM"))
                                && productRecord.containsKey("AS_CUSTOMER_COMES") && productRecord.get("AS_CUSTOMER_COMES").equals("Y") && (!productRecord.get("INSTALL_TYPE").equals("EYI"))) {
                            productRecord.put("NO_OF_INSTALLMENT", passDate.get("NO_OF_INSTALLMENT"));
                            if (passDate.containsKey("LOAN_EMI_CLOSE") && passDate.get("LOAN_EMI_CLOSE") != null) {
                                productRecord.put("LOAN_EMI_CLOSE", passDate.get("LOAN_EMI_CLOSE"));
                            }
                            productRecord.put("NO_OF_INSTALLMENT", passDate.get("NO_OF_INSTALLMENT"));
                            return emiCalculater(productRecord, interestList, passDate);
                        }

                        //END EMI INTEREST CALCULATION
                        List cumDepositlst = sqlMap.executeQueryForList("getDepositBehavesforLoan", productRecord);
                        if (cumDepositlst != null && cumDepositlst.size() > 0) {
                            cumDepositMap = (HashMap) cumDepositlst.get(0);
                            if (passDate.containsKey("SOURCE_SCREEN") && passDate.get("SOURCE_SCREEN") != null) {
                                sourceScreen = CommonUtil.convertObjToStr(passDate.get("SOURCE_SCREEN"));
                            } else {
                                sourceScreen = "";
                            }

                            // kerla co oeprative bank theny want to collect till deposit matruiry date or upto date  for that purpose added condition
                            if (sourceScreen.equals("LOAN_CLOSING")) {
                                if (productRecord.containsKey("INT_UPTO_MAT_DT") && CommonUtil.convertObjToStr(productRecord.get("INT_UPTO_MAT_DT")).equals("Y")) {
                                    if (cumDepositMap.containsKey("MATURITY_DT") && cumDepositMap.get("MATURITY_DT") != null) {
                                        if (DateUtil.dateDiff((Date) cumDepositMap.get("MATURITY_DT"), curr_dt) > 0) {
                                            chekdateLast = (Date) DateUtil.addDaysProperFormat((Date) cumDepositMap.get("MATURITY_DT"), 0);
                                            if (behavelike != null && behavelike.equals("LOANS_AGAINST_DEPOSITS")) {
                                                chekdateLast = (Date) DateUtil.addDaysProperFormat((Date) cumDepositMap.get("MATURITY_DT"), -1);
                                            }
                                            productRecord.put("END_DT", chekdateLast);
                                        }
                                        System.out.println("Till Maturity date ######" + chekdateLast);
                                    }

                                }//kerla  validation end
                            }
                            // kerla co oeprative bank theny want to collect till deposit matruiry date or upto date  for that purpose added condition
                            if (sourceScreen.equals("DEPOSIT_CLOSING")) {
                                if (productRecord.containsKey("INT_UPTO_DEPOSIT_MAT_DT") && CommonUtil.convertObjToStr(productRecord.get("INT_UPTO_DEPOSIT_MAT_DT")).equals("Y")) {
                                    if (cumDepositMap.containsKey("MATURITY_DT") && cumDepositMap.get("MATURITY_DT") != null) {
                                        if (DateUtil.dateDiff((Date) cumDepositMap.get("MATURITY_DT"), curr_dt) > 0) {
                                            chekdateLast = (Date) DateUtil.addDaysProperFormat((Date) cumDepositMap.get("MATURITY_DT"), 0);
                                            if (behavelike != null && behavelike.equals("LOANS_AGAINST_DEPOSITS")) {
                                                chekdateLast = (Date) DateUtil.addDaysProperFormat((Date) cumDepositMap.get("MATURITY_DT"), -1);
                                            }
                                            productRecord.put("END_DT", chekdateLast);
                                        }
                                        System.out.println("Till deposit Maturity date ######" + chekdateLast);
                                    }

                                }//kerla  validation end
                            }

//edited by Nidhin
                            String deposit_behaves = CommonUtil.convertObjToStr(cumDepositMap.get("BEHAVES_LIKE"));
                            if (deposit_behaves.equals("CUMMULATIVE") || deposit_behaves.equals("RECURRING")) {
                                productRecord.put("PRINICIPAL", cumDepositMap.get("LOAN_BALANCE_PRINCIPAL"));
                                if (interestsMap.containsKey("MATURITY_DATE") && interestsMap.get("MATURITY_DATE") != null && interestsMap.containsKey("CALC_ON_MATURITY") && interestsMap.get("CALC_ON_MATURITY") != null) {
                                    if (interestsMap.get("CALC_ON_MATURITY").equals("Y")) {
                                        productRecord.put("MATURITY_DATE", interestsMap.get("MATURITY_DATE"));
                                    }
                                }
                                returnMap = cumalitiveDepositAsAnWhen(productRecord, passDate);
                                returnMap.putAll(lastAppDt);
                                return returnMap;
                            }
                        }

                        if (passDate.containsKey("DEPOSIT_PREMATURE_CLOSER")) {
                            if (productRecord.containsKey("AS_CUSTOMER_COMES")
                                    && CommonUtil.convertObjToStr(productRecord.get("AS_CUSTOMER_COMES")).equals("Y")) {
                                prematureLTD = true;
                                passDate.remove("DEPOSIT_PREMATURE_CLOSER");
                            }
                        }
                        if (passDate.containsKey("DEPOSIT_PREMATURE_CLOSER") && prematureLTD == false) {

                            returnMap = prematureDepositClosingInt(productRecord, passDate);
                            return returnMap;
                        }
                        if (productRecord.containsKey("LAST_INT_CALC_DT") && productRecord.get("LAST_INT_CALC_DT") != null && DateUtil.dateDiff((Date) productRecord.get("LAST_INT_CALC_DT"), chekdateLast) == 0) {
                            continue;
                        }
                        //for penal
                        if (CommonUtil.convertObjToStr(productRecord.get("PENAL_APPL")).equals("Y") && (!CommonUtil.convertObjToStr(passDate.get("BEHAVES_LIKE")).equals("OD"))) {
                            penalInterest.putAll(productRecord);
                            penalInterestMap = interestCalculationTL(penalInterest);
                            if (passDate.containsKey("FORPENAL_INTEREST_PURPOSE") && passDate.get("FORPENAL_INTEREST_PURPOSE") != null) {
                                System.out.println("penalinterest@@@@@@@" + penalInterestMap);
                                return penalInterestMap;
                            }

                            //                    System.out.println("totpenalint$######"+totpenalint);
                        }
                        System.out.print("singleRecordforinterestproduct" + productRecord);
                        singleRecord.put("ACT_NUM", productRecord.get("ACCT_NUM"));
                        if (passDate.containsKey("LOAN_ACCOUNT_CLOSING")) {
                            singleRecord.put("CURR_DT", currDt);
                            singleRecord.put("BRANCH_CODE", _branchID);
                            singleRecord.put("USER_ID", user);
                            //                    List lst=sqlMap.executeQueryForList("selectLoanInterestTMP",singleRecord);
                            //                    if( lst!=null && lst.size()>0)
                            //sqlMap.executeUpdate("deleteLoanInterestTMP", singleRecord);
                        }
                        // The following block added by Rajesh because after going inside interestCalculationTL() chekdateLast making -1
                        if (passDate.containsKey("LOAN_ACCOUNT_CLOSING") && passDate.get("LOAN_ACCOUNT_CLOSING") != null) {
                            chekdateLast = (Date) DateUtil.addDaysProperFormat(curr_dt, -1);
                            System.out.println("checkdatelast####" + chekdateLast);
                            if (sourceScreen.equals("LOAN_CLOSING")) {
                                if (productRecord.containsKey("INT_UPTO_MAT_DT") && CommonUtil.convertObjToStr(productRecord.get("INT_UPTO_MAT_DT")).equals("Y")) {
                                    if (cumDepositMap.containsKey("MATURITY_DT") && cumDepositMap.get("MATURITY_DT") != null) {
                                        if (DateUtil.dateDiff((Date) cumDepositMap.get("MATURITY_DT"), curr_dt) > 0) {
                                            chekdateLast = (Date) DateUtil.addDaysProperFormat((Date) cumDepositMap.get("MATURITY_DT"), 0);
                                            if (behavelike != null && behavelike.equals("LOANS_AGAINST_DEPOSITS")) {
                                                chekdateLast = (Date) DateUtil.addDaysProperFormat((Date) cumDepositMap.get("MATURITY_DT"), -1);
                                            }
                                            productRecord.put("END_DT", chekdateLast);
                                        }
                                        System.out.println("Till Maturitydate last ######" + chekdateLast);
                                    }

                                }//kerla  validation end
                            }
                            if (sourceScreen.equals("DEPOSIT_CLOSING")) {
                                if (productRecord.containsKey("INT_UPTO_DEPOSIT_MAT_DT") && CommonUtil.convertObjToStr(productRecord.get("INT_UPTO_DEPOSIT_MAT_DT")).equals("Y")) {
                                    if (cumDepositMap.containsKey("MATURITY_DT") && cumDepositMap.get("MATURITY_DT") != null) {
                                        if (DateUtil.dateDiff((Date) cumDepositMap.get("MATURITY_DT"), curr_dt) > 0) {
                                            chekdateLast = (Date) DateUtil.addDaysProperFormat((Date) cumDepositMap.get("MATURITY_DT"), 0);
                                            if (behavelike != null && behavelike.equals("LOANS_AGAINST_DEPOSITS")) {
                                                chekdateLast = (Date) DateUtil.addDaysProperFormat((Date) cumDepositMap.get("MATURITY_DT"), -1);
                                            }
                                            productRecord.put("END_DT", chekdateLast);
                                        }
                                        System.out.println("Till Maturitydate last ######" + chekdateLast);
                                    }

                                }//kerla  validation end
                            }
                        }
                        Date tempDt = (Date) currDt.clone();
                        tempDt.setDate(chekdateLast.getDate());
                        tempDt.setMonth(chekdateLast.getMonth());
                        tempDt.setYear(chekdateLast.getYear());
                        singleRecord.put("CURR_DATE", DateUtil.addDays(tempDt, 0));//chekdateLast
                        System.out.println("singleRecord@@@" + singleRecord);
                        //                else
                        //                    singleRecord.put("CURR_DATE",passDate.get("CURR_DATE"));

                        Date acOpDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(productRecord.get("ACCT_OPEN_DT")));
                        Date stDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(productRecord.get("LAST_INT_CALC_DT")));
                        //               if(!passDate.containsKey("LOAN_ACCOUNT_CLOSING"))
                        // The following if block commented because it's calculating retrospective interest also & one day interest more also
//                    if(acOpDt!=null && DateUtil.dateDiff(acOpDt,stDt)==0){
//                        tempDt=(Date)currDt.clone();
//                        tempDt.setDate(stDt.getDate());
//                        tempDt.setMonth(stDt.getMonth());
//                        tempDt.setYear(stDt.getYear());
//                        singleRecord.put("START_DATE",tempDt);//stDt
//                    }
//                    else{
                        stDt = DateUtil.addDaysProperFormat(stDt, 1);
                        tempDt = (Date) currDt.clone();
                        tempDt.setDate(stDt.getDate());
                        tempDt.setMonth(stDt.getMonth());
                        tempDt.setYear(stDt.getYear());
                        singleRecord.put("START_DATE", tempDt);//stDt
//                    }
                        if (prematureLTD) {//<--this is for as an only passDate.containsKey("DEPOSIT_PREMATURE_CLOSER")
                            tempDt = (Date) currDt.clone();
                            tempDt.setDate(acOpDt.getDate());
                            tempDt.setMonth(acOpDt.getMonth());
                            tempDt.setYear(acOpDt.getYear());
                            singleRecord.put("START_DATE", tempDt);//acOpDt
                        }
                        //                 Date acOpDt=DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(productRecord.get("ACCT_OPEN_DT")));

                        //                stDt=DateUtil.addDaysProperFormat(stDt,1);
                        //                singleRecord.put("START_DATE",stDt);
                        //                singleRecord.put("START_DATE",productRecord.get("LAST_INT_CALC_DT"));
                        interestList = null;
                        //                if(singleRecord!=null)//  only for testing purpose
                        //                return singleRecord;
                        // The following line commented because not going inside RetrasPectiveInterestCalculation() method
//                    interestList= getInterestDetails(productRecord,passDate);
                        System.out.println("singleRecord333" + singleRecord);

//                    // The following block added to avoid unnecessary Retrospective int calculation by Rajesh
//                    Date last_int_calc_dt = (Date) productRecord.get("LAST_INT_CALC_DT");
//                    tempDt=(Date)currDt.clone();
//                    tempDt.setDate(last_int_calc_dt.getDate());
//                    tempDt.setMonth(last_int_calc_dt.getMonth());
//                    tempDt.setYear(last_int_calc_dt.getYear());
//                    productRecord.put("LAST_INT_CALC_DT", DateUtil.addDays(tempDt,1));
//                    // End (further one line added below RetrasPectiveInterestCalculation call
                        //retraspective
//                    double finalRetraspectiveAmt=RetrasPectiveInterestCalculation(productRecord,passDate,singleRecord);
                        double finalRetraspectiveAmt = 0;
                        interestList = getInterestDetails(productRecord, passDate);
                        List dailyBalOD = null;
                        System.out.println("############################## singleRecord  : " + singleRecord);
                        System.out.println("############################## productRecord : " + productRecord);
                        if (CommonUtil.convertObjToStr(passDate.get("BEHAVES_LIKE")).equals("OD")) {
                            HashMap paramMap = new HashMap();
                            paramMap.put("START_DT", singleRecord.get("START_DATE"));
                            if (passDate.containsKey("FROM_ACCOUNT_CLOSING") && passDate.get("FROM_ACCOUNT_CLOSING") != null) //valuedate interest should calculate till currdate for account closing screen and charges scrren told by srinath sir on 27-1-2014
                            {
                                paramMap.put("END_DT", chekdateLast);//ServerUtil.getCurrentDate(_branchCode));
                            } else {
                                paramMap.put("END_DT", singleRecord.get("CURR_DATE"));
                            }
                            paramMap.put("ACT_NUM", singleRecord.get("ACT_NUM"));
                            if (!actListMap.containsKey(CommonUtil.convertObjToStr(productRecord.get("ACCT_NUM")))) {
                                totalvalidateInterest = valuedateInterestCalculation(productRecord, passDate, paramMap, intGetFrom);
                            } else {
                                totalvalidateInterest = 0;
                            }
                            dailyBalOD = sqlMap.executeQueryForList("dayEndBalanceForIntCalcAD", singleRecord);
                            paramMap.clear();
                            paramMap = null;
                            System.out.println(paramMap);
                        } else if (CommonUtil.convertObjToStr(productRecord.get("INTEREST")).equals("SIMPLE") && CommonUtil.convertObjToStr(productRecord.get("LOAN_BALANCE")).equals("N")
                                && CommonUtil.convertObjToStr(productRecord.get("SUBSIDY_RECEIVED_DT")).equals("N")) {
                            dailyBalOD = sqlMap.executeQueryForList("dayEndBalanceForSubsidyIntCalc_simple_TL", singleRecord);
                        } else if (CommonUtil.convertObjToStr(productRecord.get("INTEREST")).equals("COMPOUND") && CommonUtil.convertObjToStr(productRecord.get("LOAN_BALANCE")).equals("N")
                                && CommonUtil.convertObjToStr(productRecord.get("SUBSIDY_RECEIVED_DT")).equals("N")) {
                            dailyBalOD = sqlMap.executeQueryForList("dayEndBalanceForSubsidyIntCalcTL", singleRecord);
                        } else if (CommonUtil.convertObjToStr(productRecord.get("INTEREST_TYPE")).equals("FLAT_RATE") && CommonUtil.convertObjToStr(productRecord.get("INTEREST")).equals("SIMPLE") || (oneMonthInterest != null && oneMonthInterest.equals("LOANSANCTIONAMT"))) {
                            dailyBalOD = sqlMap.executeQueryForList("dayEndBalanceForIntCalcFlat_TL", singleRecord);
                        } else if (CommonUtil.convertObjToStr(productRecord.get("INTEREST")).equals("COMPOUND")) {
                            dailyBalOD = sqlMap.executeQueryForList("dayEndBalanceForIntCalcTL", singleRecord);
                        } else if (CommonUtil.convertObjToStr(productRecord.get("INTEREST")).equals("SIMPLE")) {
                            dailyBalOD = sqlMap.executeQueryForList("dayEndBalanceForIntCalc_simple_TL", singleRecord);
                        }
                        //                    System.out.println("DAILYbALANCEOD&&&&&&^^"+dailyBalOD);
                        //                dailyBalOD=null; testing penal
                        totInterest = 0;
                        HashMap hashDailyMap = new HashMap();
                        Date firstDate = new Date();
                        String firstDt = null;
                        Date dayEndDt = null;
                        if (dailyBalOD != null && dailyBalOD.size() > 0) {
                            for (int i = 0; i < dailyBalOD.size(); i++) {
                                //                        System.out.println("#$#$"+dailyBalOD);
                                hashDailyMap = (HashMap) dailyBalOD.get(i);
                                System.out.println("hashDailyMapbbbbbbb" + hashDailyMap + " i Value : " + i);
                                hashDailyMap.putAll(productRecord);
                                dayEndDt = DateUtil.getDateWithoutMinitues(CommonUtil.convertObjToStr(hashDailyMap.get("DAY_END_DT")));
                                Date nextDt = DateUtil.getDateWithoutMinitues(CommonUtil.convertObjToStr(hashDailyMap.get("NEXT_DT")));
                                double curr_amt = 0;
                                double next_amt = 0;
                                System.out.println("DateUtil.dateDiff(dayEndDt,chekdateLast)" + DateUtil.dateDiff(dayEndDt, chekdateLast) + "chekdateLast" + chekdateLast + "dayEndDt" + dayEndDt + "passDate.containsKey()" + passDate.containsKey("CHARGESUI"));
                                if (passDate.containsKey("CHARGESUI") && DateUtil.dateDiff(dayEndDt, chekdateLast) < 0) {
                                    break;
                                }

                                //                    if(CommonUtil.convertObjToStr(productRecord.get("INTEREST")).equals("COMPOUND")){
                                curr_amt = CommonUtil.convertObjToDouble(hashDailyMap.get("AMT")).doubleValue();
                                next_amt = CommonUtil.convertObjToDouble(hashDailyMap.get("NEXT_AMT")).doubleValue();
                                //                    }else{
                                //                        curr_amt=CommonUtil.convertObjToDouble(hashDailyMap.get("PRINCIPAL")).doubleValue();
                                //                        next_amt=CommonUtil.convertObjToDouble(hashDailyMap.get("NEXT_PRINCIPAL")).doubleValue();
                                //                    }
                                //                            System.out.println("productrecord####"+productRecord+"curramt&&&&$$"+curr_amt+"nextamt###"+next_amt);
                                if (curr_amt == next_amt && firstDt == null) {
                                    firstDt = CommonUtil.convertObjToStr(hashDailyMap.get("DAY_END_DT"));
                                    firstDate = DateUtil.getDateMMDDYYYY(firstDt);
//                                    if (curr_amt > 0 && CommonUtil.convertObjToStr(productRecord.get("INTEREST")).equals("COMPOUND")) {
//                                        firstDt = null;
//                                    }
                                    System.out.println("inside the firstdate##" + firstDt);

                                }
                                if ((CommonUtil.convertObjToStr(productRecord.get("INTEREST")).equals("COMPOUND")) || (CommonUtil.convertObjToStr(productRecord.get("INTEREST")).equals("SIMPLE") && curr_amt != next_amt)
                                        || i + 1 == dailyBalOD.size()) {// || (passDate.containsKey("CHARGESUI")&& DateUtil.dateDiff(nextDt,chekdateLast) >=0 && curr_amt<0)) {
                                    hashDailyMap.putAll(productRecord);
                                    if (firstDt != null && firstDt.length() > 0) {
                                        System.out.println("inside the if condition ##" + hashDailyMap);
                                        hashDailyMap.put("START_DT", firstDate);
                                        //                            hashDailyMap.put("END_DT",hashDailyMap.get("DAY_END_DT"));

                                        //                                if (i+1==dailyBalOD.size())   //now onlycommented becase last date calculation done by out of for loop
                                        //                                    hashDailyMap.put("END_DT",hashDailyMap.get("NEXT_DT"));
                                        //                                else
                                        if (DateUtil.dateDiff(dayEndDt, nextDt) == 0) {
                                            hashDailyMap.put("END_DT", hashDailyMap.get("NEXT_DT"));
                                        } else {

                                            hashDailyMap.put("END_DT", DateUtil.addDaysProperFormat(nextDt, -1));
                                            if (i + 1 == dailyBalOD.size() && passDate.containsKey("LOAN_ACCOUNT_CLOSING")) {
                                                hashDailyMap.put("END_DT", DateUtil.addDaysProperFormat(curr_dt, -1));
                                                if ((productRecord.containsKey("INT_UPTO_DEPOSIT_MAT_DT") && CommonUtil.convertObjToStr(productRecord.get("INT_UPTO_DEPOSIT_MAT_DT")).equals("N"))
                                                        || (productRecord.containsKey("INT_UPTO_MAT_DT") && CommonUtil.convertObjToStr(productRecord.get("INT_UPTO_MAT_DT")).equals("N"))) {
                                                    hashDailyMap.put("END_DT", DateUtil.addDaysProperFormat(curr_dt, -1));
                                                }
                                                if (sourceScreen.equals("LOAN_CLOSING")) {    //////////////////------------------------------
                                                    if (productRecord.containsKey("INT_UPTO_MAT_DT") && CommonUtil.convertObjToStr(productRecord.get("INT_UPTO_MAT_DT")).equals("Y")) {
                                                        if (DateUtil.dateDiff((Date) cumDepositMap.get("MATURITY_DT"), curr_dt) > 0) {
                                                            hashDailyMap.put("END_DT", DateUtil.addDaysProperFormat(chekdateLast, 0));
                                                        } else {
                                                            hashDailyMap.put("END_DT", DateUtil.addDaysProperFormat(curr_dt, -1));
                                                        }

                                                    }
                                                } else if (sourceScreen.equals("DEPOSIT_CLOSING")) {
                                                    if (productRecord.containsKey("INT_UPTO_DEPOSIT_MAT_DT") && CommonUtil.convertObjToStr(productRecord.get("INT_UPTO_DEPOSIT_MAT_DT")).equals("Y")) {
                                                        hashDailyMap.put("END_DT", DateUtil.addDaysProperFormat(chekdateLast, 0));
                                                    }
                                                }
                                            }
                                            //                                    hashDailyMap.put("NO_ROUND_OFF_INT","NO_ROUND_OFF_INT");

                                            hashDailyMap.put("NO_ROUND_OFF_INT", "NO_ROUND_OFF_INT");
                                        }

                                    } else {
                                        System.out.println("inside the else condition ##" + hashDailyMap);
                                        hashDailyMap.put("START_DT", hashDailyMap.get("DAY_END_DT"));
                                        if ((chekdateLast != null && DateUtil.dateDiff(dayEndDt, chekdateLast) == 0)
                                                || DateUtil.dateDiff(dayEndDt, nextDt) == 0)//(dayEndDt.equals(chekdateLast))
                                        {
                                            hashDailyMap.put("END_DT", hashDailyMap.get("NEXT_DT"));
                                        } else {
                                            hashDailyMap.put("END_DT", DateUtil.addDaysProperFormat(nextDt, -1));
                                        }
                                        hashDailyMap.put("NO_ROUND_OFF_INT", "NO_ROUND_OFF_INT");
                                    }
                                    if (i + 1 == dailyBalOD.size() && passDate.containsKey("LOAN_ACCOUNT_CLOSING")) {
                                        hashDailyMap.put("END_DT", DateUtil.addDaysProperFormat(curr_dt, -1));
                                        if (sourceScreen.equals("LOAN_CLOSING")) {    //////////////////------------------------------
                                            if (productRecord.containsKey("INT_UPTO_MAT_DT") && CommonUtil.convertObjToStr(productRecord.get("INT_UPTO_MAT_DT")).equals("Y")) {
                                                hashDailyMap.put("END_DT", DateUtil.addDaysProperFormat(chekdateLast, 0));
                                                if (behavelike != null && behavelike.equals("LOANS_AGAINST_DEPOSITS")) {
                                                    chekdateLast = (Date) DateUtil.addDaysProperFormat(chekdateLast, -1);
                                                }
                                            }
                                        } else if (sourceScreen.equals("DEPOSIT_CLOSING")) {
                                            if (productRecord.containsKey("INT_UPTO_DEPOSIT_MAT_DT") && CommonUtil.convertObjToStr(productRecord.get("INT_UPTO_DEPOSIT_MAT_DT")).equals("Y")) {
                                                hashDailyMap.put("END_DT", DateUtil.addDaysProperFormat(chekdateLast, 0));
                                            }
                                        }
                                    }
                                    System.out.println("hashdailymap####" + hashDailyMap);
                                    if ((CommonUtil.convertObjToStr(productRecord.get("INTEREST")).equals("COMPOUND"))
                                            || (CommonUtil.convertObjToStr(productRecord.get("INTEREST")).equals("SIMPLE")
                                            && CommonUtil.convertObjToDouble(hashDailyMap.get("AMT")).doubleValue() > 0)) {  //PRINCIPAL
                                        if (CommonUtil.convertObjToStr(productRecord.get("INTEREST")).equals("SIMPLE")) {
                                            //added by sreekrishnan for mantis 10212
                                            if (CommonUtil.convertObjToStr(productRecord.get("PROD_SUBSIDY")).equals("Y")) {
                                                if (CommonUtil.convertObjToStr(productRecord.get("LOAN_BALANCE")).equals("N")) {
                                                    if (CommonUtil.convertObjToStr(productRecord.get("SUBSIDY_ALLOWED")).equals("Y")) {
                                                        if (CommonUtil.convertObjToDouble(productRecord.get("SUBSIDY_AMT")) > 0) {
                                                            hashDailyMap.put("PRINCIPAL_BAL", (CommonUtil.convertObjToDouble(hashDailyMap.get("AMT")) - (CommonUtil.convertObjToDouble(productRecord.get("SUBSIDY_AMT"))
                                                                    - CommonUtil.convertObjToDouble(hashDailyMap.get("SUBSIDY_ADJUSTED_AMT")))));
                                                        } else {
                                                            hashDailyMap.put("PRINCIPAL_BAL", hashDailyMap.get("AMT"));
                                                        }
                                                    } else {
                                                        hashDailyMap.put("PRINCIPAL_BAL", hashDailyMap.get("AMT"));
                                                    }
                                                } else {
                                                    hashDailyMap.put("PRINCIPAL_BAL", hashDailyMap.get("AMT"));
                                                }
                                            } else {
                                                hashDailyMap.put("PRINCIPAL_BAL", hashDailyMap.get("AMT"));
                                            }
                                            System.out.println("hashDailyMap.put(PRINCIPAL_BAL)#$#$#$" + hashDailyMap.get("PRINCIPAL_BAL"));
                                        } else {
                                            hashDailyMap.put("PRINCIPAL_BAL", new Double(-CommonUtil.convertObjToDouble(hashDailyMap.get("AMT")).doubleValue()));
                                        }
                                        hashDailyMap.put("PROD_ID", passDate.get("PROD_ID"));
                                        System.out.println("HashDailyMap#####" + hashDailyMap);
                                        //                                Date nextdt=DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hashDailyMap.get("NEXT_DT")));
                                        //                                if(! (DateUtil.dateDiff(nextdt,chekdateLast)==0)){
                                        hashDailyMap.put("NO_ROUND_OFF_INT", "NO_ROUND_OFF_INT");
                                        //                                }
                                        HashMap resultInt = new HashMap();
                                        //                                    if(cumDepositMap !=null && cumDepositMap.containsKey("BEHAVES_LIKE") && (cumDepositMap.get("BEHAVES_LIKE").equals("CUMMULATIVE")|| cumDepositMap.get("BEHAVES_LIKE").equals("RECURRING"))) {
                                        //                                        resultInt = cumalitiveDepositAsAnWhen(productRecord,passDate,hashDailyMap);
                                        //                                    }else
                                        if (hashDailyMap.containsKey("INTEREST_TYPE") && hashDailyMap.get("INTEREST_TYPE").equals("FLOATING_RATE")) {
                                            HashMap interestMap = new HashMap();
                                            Date nextroidt = null;
                                            boolean dayendOver = false;
                                            interestMap.put("FROM_DATE", productRecord.get("FROM_DATE"));   //hashDailyMap.get("START_DT"));
                                            interestMap.put("TO_DATE", productRecord.get("TO_DATE"));   //hashDailyMap.get("END_DT"));
                                            interestMap.put("PROD_ID", passDate.get("PROD_ID"));
                                            interestMap.put("CATEGORY_ID", productRecord.get("CATEGORY_ID"));
                                            interestMap.put("AMOUNT", productRecord.get("AMOUNT"));
                                            interestMap.put("DAYENDSTDT", DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hashDailyMap.get("START_DT"))));
                                            interestMap.put("DAYENDEDDT", DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hashDailyMap.get("END_DT"))));
                                            interestMap.put("ACCT_NUM", hashDailyMap.get("ACCT_NUM"));
                                            Date start_dt = (Date) hashDailyMap.get("START_DT");
                                            Date dayEndBalanceDt = (Date) hashDailyMap.get("END_DT");
                                            List resultInterstRate = null;
                                            if (intGetFrom.equals("PROD")) {
                                                resultInterstRate = sqlMap.executeQueryForList("getSelectProductTermLoanFlotingInterestMap", interestMap);
                                            } else {
                                                resultInterstRate = (List) sqlMap.executeQueryForList("getSelectAccountTermLoanFlotingInterestMap", interestMap);
                                            }
                                            //                                            resultInterstRate=sqlMap.executeQueryForList("getSelectProductTermLoanFlotingInterestMap",interestMap);

                                            if (resultInterstRate == null || resultInterstRate.isEmpty()) {
                                                if (intGetFrom.equals("PROD")) {
                                                    resultInterstRate = sqlMap.executeQueryForList("getSelectProductDayEndTermLoanInterestMap", interestMap);
                                                } else {
                                                    resultInterstRate = (List) sqlMap.executeQueryForList("getSelectAccountTermLoanDayEndInterestMap", interestMap);
                                                }
                                            }
                                            if (resultInterstRate != null && resultInterstRate.size() > 0) {
                                                for (int k = 0; k < resultInterstRate.size(); k++) {
                                                    HashMap interestResultMap = (HashMap) resultInterstRate.get(k);
                                                    Date roifrom_dt = (Date) interestResultMap.get("FROM_DT");
                                                    Date roito_dt = (Date) interestResultMap.get("TO_DT");
                                                    if (DateUtil.dateDiff(start_dt, roifrom_dt) > 0) {
                                                        if (intGetFrom.equals("PROD")) {
                                                            resultInterstRate = sqlMap.executeQueryForList("getSelectProductDayEndTermLoanInterestMap", interestMap);
                                                        } else {
                                                            resultInterstRate = (List) sqlMap.executeQueryForList("getSelectAccountTermLoanDayEndInterestMap", interestMap);
                                                        }
                                                        System.out.println("resultInterstRate!!!!!!" + resultInterstRate);
                                                        if (resultInterstRate != null && resultInterstRate.size() > 0) {
                                                            interestResultMap = (HashMap) resultInterstRate.get(0);
                                                            roifrom_dt = (Date) interestResultMap.get("FROM_DT");
                                                            roito_dt = (Date) interestResultMap.get("TO_DT");
                                                        } else {
                                                            return null;
                                                        }
                                                    }
                                                    if (DateUtil.dateDiff(start_dt, roifrom_dt) <= 0 && (roito_dt == null || DateUtil.dateDiff(roito_dt, (Date) hashDailyMap.get("END_DT")) <= 0)) {
                                                        interestList.clear();
                                                        interestList.add(resultInterstRate.get(k));
                                                        resultInt = calculateInterest(hashDailyMap);
                                                        if (resultInt != null) {
                                                            hashDailyMap.put(InterestCalculationConstants.INTEREST, resultInt.get(InterestCalculationConstants.INTEREST));
                                                            if (passDate.containsKey("LOAN_ACCOUNT_CLOSING")) {
                                                                insertLoanInteresttmp(hashDailyMap);
                                                            } else {
                                                                insertLoanInterest(hashDailyMap);
                                                            }
                                                        }
                                                    } else {
                                                        do {
                                                            if (roito_dt != null && DateUtil.dateDiff(dayEndBalanceDt, roito_dt) <= 0) {
                                                                hashDailyMap.put("END_DT", roito_dt);
                                                            } else {
                                                                hashDailyMap.put("END_DT", dayEndBalanceDt);
                                                                dayendOver = true;
                                                            }
                                                            if (resultInterstRate != null && resultInterstRate.size() > 0) {
                                                                //interestList.add(resultInterstRate.get(0));
                                                                interestList.clear();
                                                                interestList.add(resultInterstRate.get(k));
                                                            }
                                                            resultInt = calculateInterest(hashDailyMap);
                                                            if (resultInt != null) {
                                                                hashDailyMap.put(InterestCalculationConstants.INTEREST, resultInt.get(InterestCalculationConstants.INTEREST));
                                                                if (passDate.containsKey("LOAN_ACCOUNT_CLOSING")) {
                                                                    insertLoanInteresttmp(hashDailyMap);
                                                                } else {
                                                                    insertLoanInterest(hashDailyMap);
                                                                }
                                                            }
                                                            if (roito_dt != null) {
                                                                roifrom_dt = (Date) DateUtil.addDays(roito_dt, 1);
                                                            }
                                                            hashDailyMap.put("START_DT", roifrom_dt);
                                                            hashDailyMap.put("END_DT", dayEndBalanceDt);
                                                            interestMap.put("DAYENDSTDT", hashDailyMap.get("START_DT"));
                                                            interestMap.put("DAYENDEDDT", hashDailyMap.get("END_DT"));
                                                            if (intGetFrom.equals("PROD")) {
                                                                resultInterstRate = sqlMap.executeQueryForList("getSelectProductDayEndTermLoanInterestMap", interestMap);
                                                            } else {
                                                                resultInterstRate = sqlMap.executeQueryForList("getSelectAccountTermLoanDayEndInterestMap", interestMap);
                                                            }
                                                            if (resultInterstRate != null && resultInterstRate.size() > 0) {
                                                                interestResultMap = (HashMap) resultInterstRate.get(0);
                                                                roifrom_dt = (Date) interestResultMap.get("FROM_DT");
                                                                if (interestResultMap.containsKey("TO_DT") && interestResultMap.get("TO_DT") != null) {
                                                                    roito_dt = (Date) interestResultMap.get("TO_DT");
                                                                } else {
                                                                    roito_dt = null;
                                                                }
                                                            } else {
                                                                roito_dt = null;
                                                            }
                                                            hashDailyMap.put("START_DT", roifrom_dt);
                                                        } while (DateUtil.dateDiff(roifrom_dt, dayEndBalanceDt) > 0 && dayendOver == false);
                                                        //                                                           Date nextroidt=(Date)DateUtil.addDays(roifrom_dt,1);
                                                        //                                                           if(DateUtil.dateDiff(nextroidt,dayEndBalanceDt)>0){
                                                        //                                                           hashDailyMap.put("START_DT",nextroidt);
                                                        //                                                           hashDailyMap.put("END_DT",dayEndBalanceDt);
                                                        //                                                            resultInt= calculateInterest(hashDailyMap);
                                                        //                                                }
                                                        //                                                    interestList
                                                        dayendOver = false;
                                                    }

                                                }
                                            }

                                        } else {
                                            System.out.print("FLOATING_RATE else part " + hashDailyMap);
                                            resultInt = calculateInterest(hashDailyMap);
                                            hashDailyMap.put(InterestCalculationConstants.INTEREST, resultInt.get(InterestCalculationConstants.INTEREST));
                                            if (passDate.containsKey("LOAN_ACCOUNT_CLOSING")) {
                                                insertLoanInteresttmp(hashDailyMap);
                                            } else {
                                                insertLoanInterest(hashDailyMap);
                                            }
                                        }
                                        totInterest += CommonUtil.convertObjToDouble(resultInt.get(InterestCalculationConstants.INTEREST)).doubleValue();
                                        ROI = CommonUtil.convertObjToDouble(resultInt.get("ROI"));
                                        firstDt = null;
                                        //                             if(CommonUtil.convertObjToStr(productRecord.get("INTEREST")).equals("COMPOUND") && next_amt>0){
                                        //                                firstDt=CommonUtil.convertObjToStr(hashDailyMap.get("NEXT_DT"));
                                        //                            }

                                    }
                                }
                                //                    if(CommonUtil.convertObjToStr(productRecord.get("INTEREST")).equals("COMPOUND") && next_amt>0){
                                //                        firstDt=CommonUtil.convertObjToStr(hashDailyMap.get("NEXT_DT"));
                                //                        firstDate=DateUtil.getDateMMDDYYYY(firstDt);
                                //                        System.out.println("nextamt###"+next_amt);
                                //                    }

                            }
                        }
                        System.out.print(chekdateLast + "totInterest" + totInterest);
                        Date dayenddt = null;
                        if (hashDailyMap.containsKey("END_DT") && hashDailyMap.get("END_DT") != null)//CHANGE DAY_END_DT
                        {
                            dayenddt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hashDailyMap.get("END_DT")));
                        }
                        System.out.println("lastdayend date  :" + dayenddt);
                        //                 if( passDate.containsKey("BATCH_PROCESS") && DateUtil.dateDiff(chekdateLast, curr_dt)<0 || passDate.containsKey("CHARGESUI")){// ||
                        //                 (CommonUtil.convertObjToDouble(hashDailyMap.get("AMT")).doubleValue() == CommonUtil.convertObjToDouble(hashDailyMap.("NEXT_AMT")).doubleValue())){
                        if (dayenddt != null) {
                            if (DateUtil.dateDiff(chekdateLast, dayenddt) < 0) {//|| passDate.containsKey("CHARGESUI")
                                System.out.println("datediff##### ttt" + DateUtil.dateDiff(chekdateLast, curr_dt));
                                hashDailyMap.putAll(productRecord);
                                if (firstDt != null) {
                                    hashDailyMap.put("START_DT", firstDate);
                                } else {
                                    hashDailyMap.put("START_DT", DateUtil.addDaysProperFormat(dayEndDt, 1));
                                }
                                //                    if(passDate.containsKey("CHARGESUI"))
                                //                         hashDailyMap.put("END_DT",passDate.get("DATE_TO"));
                                //                    else
                                hashDailyMap.put("END_DT", chekdateLast);
                                if ((CommonUtil.convertObjToDouble(hashDailyMap.get("AMT")).doubleValue() < 0 && CommonUtil.convertObjToStr(productRecord.get("INTEREST")).equals("COMPOUND"))
                                        || (CommonUtil.convertObjToStr(productRecord.get("INTEREST")).equals("SIMPLE") && CommonUtil.convertObjToDouble(hashDailyMap.get("AMT")).doubleValue() > 0) && 1 != dailyBalOD.size()) {
                                    if (CommonUtil.convertObjToStr(productRecord.get("INTEREST")).equals("SIMPLE")) {
                                        hashDailyMap.put("PRINCIPAL_BAL", new Double(CommonUtil.convertObjToDouble(hashDailyMap.get("AMT")).doubleValue()));
                                    } else {
                                        hashDailyMap.put("PRINCIPAL_BAL", new Double(-CommonUtil.convertObjToDouble(hashDailyMap.get("AMT")).doubleValue()));
                                    }
                                    //                        hashDailyMap.put("PRINCIPAL_BAL",hashDailyMap.get("AMT"));
                                    hashDailyMap.put("PROD_ID", passDate.get("PROD_ID"));
                                    HashMap resultInt = null;
                                    //                                if(cumDepositMap !=null && cumDepositMap.containsKey("BEHAVES_LIKE") && (cumDepositMap.get("BEHAVES_LIKE").equals("CUMMULATIVE")|| cumDepositMap.get("BEHAVES_LIKE").equals("RECURRING"))) {
                                    //                                          resultInt = cumalitiveDepositAsAnWhen(productRecord,passDate,hashDailyMap);
                                    //                                }else
                                    //floting rate of interest
                                    if (hashDailyMap.containsKey("INTEREST_TYPE") && hashDailyMap.get("INTEREST_TYPE").equals("FLOATING_RATE")) {
                                        HashMap interestMap = new HashMap();
                                        Date nextroidt = null;
                                        boolean dayendOver = false;
                                        interestMap.put("FROM_DATE", productRecord.get("FROM_DATE"));   //hashDailyMap.get("START_DT"));
                                        interestMap.put("TO_DATE", productRecord.get("TO_DATE"));   //hashDailyMap.get("END_DT"));
                                        interestMap.put("PROD_ID", passDate.get("PROD_ID"));
                                        interestMap.put("CATEGORY_ID", productRecord.get("CATEGORY_ID"));
                                        interestMap.put("AMOUNT", productRecord.get("AMOUNT"));
                                        interestMap.put("DAYENDSTDT", hashDailyMap.get("START_DT"));
                                        interestMap.put("DAYENDEDDT", hashDailyMap.get("END_DT"));
                                        interestMap.put("ACCT_NUM", hashDailyMap.get("ACCT_NUM"));
                                        Date start_dt = (Date) hashDailyMap.get("START_DT");
                                        Date dayEndBalanceDt = (Date) hashDailyMap.get("END_DT");
                                        List resultInterstRate = null;
                                        if (intGetFrom.equals("PROD")) {
                                            resultInterstRate = sqlMap.executeQueryForList("getSelectProductTermLoanFlotingInterestMap", interestMap);
                                        } else {
                                            resultInterstRate = (List) sqlMap.executeQueryForList("getSelectAccountTermLoanFlotingInterestMap", interestMap);
                                        }

                                        if (resultInterstRate == null || resultInterstRate.isEmpty()) {
                                            if (intGetFrom.equals("PROD")) {
                                                resultInterstRate = sqlMap.executeQueryForList("getSelectProductDayEndTermLoanInterestMap", interestMap);
                                            } else {
                                                resultInterstRate = sqlMap.executeQueryForList("getSelectAccountTermLoanDayEndInterestMap", interestMap);
                                            }
                                        }

                                        if (resultInterstRate != null && resultInterstRate.size() > 0) {
                                            for (int k = 0; k < resultInterstRate.size(); k++) {
                                                HashMap interestResultMap = (HashMap) resultInterstRate.get(k);
                                                Date roifrom_dt = (Date) interestResultMap.get("FROM_DT");
                                                Date roito_dt = (Date) interestResultMap.get("TO_DT");
                                                if (DateUtil.dateDiff(start_dt, roifrom_dt) > 0) {
                                                    resultInterstRate = sqlMap.executeQueryForList("getSelectProductDayEndTermLoanInterestMap", interestMap);
                                                    if (resultInterstRate != null && resultInterstRate.size() > 0) {
                                                        interestResultMap = (HashMap) resultInterstRate.get(0);
                                                        roifrom_dt = (Date) interestResultMap.get("FROM_DT");
                                                        roito_dt = (Date) interestResultMap.get("TO_DT");
                                                    }
                                                    if (DateUtil.dateDiff(start_dt, roifrom_dt) <= 0 && (roito_dt == null || DateUtil.dateDiff(roito_dt, (Date) hashDailyMap.get("END_DT")) <= 0)) {
                                                        interestList.clear();
                                                        interestList.add(resultInterstRate.get(k));
                                                        resultInt = calculateInterest(hashDailyMap);
                                                        if (resultInt != null) {
                                                            hashDailyMap.put(InterestCalculationConstants.INTEREST, resultInt.get(InterestCalculationConstants.INTEREST));
                                                            if (passDate.containsKey("LOAN_ACCOUNT_CLOSING")) {
                                                                insertLoanInteresttmp(hashDailyMap);
                                                            } else {
                                                                insertLoanInterest(hashDailyMap);
                                                            }
                                                        }
                                                    } else {
                                                        do {
                                                            if (roito_dt != null && DateUtil.dateDiff(dayEndBalanceDt, roito_dt) <= 0) {
                                                                hashDailyMap.put("END_DT", roito_dt);
                                                            } else {
                                                                hashDailyMap.put("END_DT", dayEndBalanceDt);
                                                                dayendOver = true;
                                                            }
                                                            if (resultInterstRate != null && resultInterstRate.size() > 0) {
                                                                //                                                            interestList=(List)resultInterstRate.get(0);
                                                                interestList.clear();
                                                                interestList.add(resultInterstRate.get(k));
                                                            }
                                                            resultInt = calculateInterest(hashDailyMap);
                                                            if (resultInt != null) {
                                                                hashDailyMap.put(InterestCalculationConstants.INTEREST, resultInt.get(InterestCalculationConstants.INTEREST));
                                                                if (passDate.containsKey("LOAN_ACCOUNT_CLOSING")) {
                                                                    insertLoanInteresttmp(hashDailyMap);
                                                                } else {
                                                                    insertLoanInterest(hashDailyMap);
                                                                }
                                                            }
                                                            if (roito_dt != null) {
                                                                roifrom_dt = (Date) DateUtil.addDays(roito_dt, 1);
                                                            }
                                                            hashDailyMap.put("START_DT", roifrom_dt);
                                                            hashDailyMap.put("END_DT", dayEndBalanceDt);
                                                            interestMap.put("DAYENDSTDT", hashDailyMap.get("START_DT"));
                                                            interestMap.put("DAYENDEDDT", hashDailyMap.get("END_DT"));
                                                            if (intGetFrom.equals("PROD")) {
                                                                resultInterstRate = sqlMap.executeQueryForList("getSelectProductDayEndTermLoanInterestMap", interestMap);
                                                            } else {
                                                                resultInterstRate = sqlMap.executeQueryForList("getSelectAccountTermLoanDayEndInterestMap", interestMap);
                                                            }
                                                            if (resultInterstRate != null && resultInterstRate.size() > 0) {
                                                                interestResultMap = (HashMap) resultInterstRate.get(0);
                                                                roifrom_dt = (Date) interestResultMap.get("FROM_DT");
                                                                if (interestResultMap.containsKey("TO_DT") && interestResultMap.get("TO_DT") != null) {
                                                                    roito_dt = (Date) interestResultMap.get("TO_DT");
                                                                }
                                                            } else {
                                                                roito_dt = null;
                                                            }
                                                            hashDailyMap.put("START_DT", roifrom_dt);
                                                        } while (DateUtil.dateDiff(roifrom_dt, dayEndBalanceDt) > 0 && dayendOver == false);
                                                        //                                                           Date nextroidt=(Date)DateUtil.addDays(roifrom_dt,1);
                                                        //                                                           if(DateUtil.dateDiff(nextroidt,dayEndBalanceDt)>0){
                                                        //                                                           hashDailyMap.put("START_DT",nextroidt);
                                                        //                                                           hashDailyMap.put("END_DT",dayEndBalanceDt);
                                                        //                                                            resultInt= calculateInterest(hashDailyMap);
                                                        //                                                }
                                                        //                                                    interestList
                                                    }

                                                }

                                            }
                                        }
                                    } else {
                                        resultInt = calculateInterest(hashDailyMap);
                                        hashDailyMap.put(InterestCalculationConstants.INTEREST, resultInt.get(InterestCalculationConstants.INTEREST));
                                        //                             hashDailyMap.put("DAY_END_DT",hashDailyMap.get("START_DT")); //for insert interestdetails only
                                        hashDailyMap.put("NEXT_DT", chekdateLast);  //for insert interestdetails only
                                        if (passDate.containsKey("LOAN_ACCOUNT_CLOSING")) {
                                            insertLoanInteresttmp(hashDailyMap);
                                        } else {
                                            insertLoanInterest(hashDailyMap);
                                        }
                                    }
                                    if (resultInt != null) {
                                        totInterest += CommonUtil.convertObjToDouble(resultInt.get(InterestCalculationConstants.INTEREST)).doubleValue();
                                        ROI = CommonUtil.convertObjToDouble(resultInt.get("ROI"));
                                    }
                                }
                            }
                        }
                        System.out.println("totInterest%$%$%$%$%$" + totInterest + "penalInterestMap.get(TOT_PENAL_INT)^$^$^$^$^" + penalInterestMap.get("TOT_PENAL_INT"));
                        //                if(totInterest >0){
                        HashMap roundeMap = new HashMap();
                        if (finalRetraspectiveAmt != 0) {
                            totInterest -= finalRetraspectiveAmt;
                        }
                        //checking purpose
                        //                    totInterest*=(-1);
                        roundeMap.put("TOT_INTEREST", new Double(totInterest));
                        roundeMap.put("PROD_ID", passDate.get("PROD_ID"));
                        totInterest = roundOffLoanInterest(roundeMap);
                        //                try{
                        //                    sqlMap.startTransaction();
                        //                    insertRecord=new HashMap();

                        //OTS interest calculation
                        if (productRecord.containsKey("OTS") && CommonUtil.convertObjToStr(productRecord.get("OTS")).equals("Y")) {
                            returnMap = calculateOTSInterstDetails(productRecord);//returnMap
                            insertRecord.putAll(returnMap);
//                        totInterest=0;//need not calculate interest
                        }
                        if (ROI.doubleValue() > 0) {
                            insertRecord.put("ROI", ROI);
                        }
                        insertRecord.put("ACT_NUM", singleRecord.get("ACT_NUM"));
                        insertRecord.put("PROD_ID", passDate.get("PROD_ID"));
                        insertRecord.put("FROM_DATE", singleRecord.get("START_DATE"));
                        insertRecord.put("TO_DATE", singleRecord.get("CURR_DATE"));
                        insertRecord.put("INTEREST", new Double(totInterest));
                        //                                    sqlMap.executeUpdate("insertMonthInterestOD", insertRecord);
                        insertRecord.put("ACCOUNTNO", insertRecord.get("ACT_NUM"));
                        insertRecord.put("AMOUNT", insertRecord.get("INTEREST"));
                        insertRecord.put("ASSET_STATUS", productRecord.get("ASSET_STATUS"));
                        double penal = 0;
                        if (productRecord.containsKey("OTS") && CommonUtil.convertObjToStr(productRecord.get("OTS")).equals("Y")) {
                        } else if (penalInterestMap != null && penalInterestMap.size() > 0) {
                            insertRecord.put("LOAN_CLOSING_PENAL_INT", penalInterestMap.get("TOT_PENAL_INT"));
                            penal = CommonUtil.convertObjToDouble(penalInterestMap.get("TOT_PENAL_INT")).doubleValue();
                        }
                        if (passDate.containsKey("BATCH_PROCESS")) {
                            insertRecord.put("LAST_CALC_DT", chekdateLast);
                        } else {
                            insertRecord.put("LAST_CALC_DT", singleRecord.get("CURR_DATE"));
                        }
                        insertRecord.put("BEHAVES_LIKE", passDate.get("BEHAVES_LIKE"));
                        if (!passDate.containsKey("LOAN_ACCOUNT_CLOSING")) {
                            if (passDate.containsKey("BATCH_PROCESS") && productRecord.get("ASSET_STATUS") != null && productRecord.get("ASSET_STATUS").equals("STANDARD_ASSETS")) {
                                insertRecord.put("END_DT", hashDailyMap.get("END_DT"));
//                            if(productRecord.containsKey("INTEREST_DUE_KEPT_RECEIVABLE") && CommonUtil.convertObjToStr(productRecord.get("INTEREST_DUE_KEPT_RECEIVABLE")).equals("N")){
//                                doTransactionBaseAVBalanceOD(insertRecord,productRecord);
//                            }
//                            else if(productRecord.containsKey("INTEREST_DUE_KEPT_RECEIVABLE") && CommonUtil.convertObjToStr(productRecord.get("INTEREST_DUE_KEPT_RECEIVABLE")).equals("Y")){
                                doTransactionOD(insertRecord, productRecord);
                                System.out.println("IF PARTTT ");
//                            }
                            } else {
                                System.out.println("ELSE PARTTT ");
                                npaInterest(insertRecord);//non standard asset keep in seperate
                            }
                        }
                        System.out.println("execution successfully");
                        if (prematureLTD/* || passDate.containsKey("NORMAL_CLOSER")*/) {
                            HashMap where = new HashMap();
                            where.put("ACT_NUM", singleRecord.get("ACT_NUM"));
                            where.put("FROM_DT", singleRecord.get("START_DATE"));
                            where.put("TO_DATE", currDt);//singleRecord.get("CURR_DATE"));
                            HashMap paidMap = (HashMap) sqlMap.executeQueryForObject("getPaidPrinciple", where);
                            double paidInt = CommonUtil.convertObjToDouble(paidMap.get("INTEREST")).doubleValue();
                            double paidPenalInt = CommonUtil.convertObjToDouble(paidMap.get("PENAL")).doubleValue();
                            totInterest = totInterest - paidInt;
                            paidPenalInt = penal - paidPenalInt;
                            insertRecord.put("INTEREST", new Double(totInterest));//INTEREST
                            insertRecord.put("LOAN_CLOSING_PENAL_INT", new Double(paidPenalInt));

                        }
                        if (!(passDate.containsKey("LOAN_ACCOUNT_CLOSING") && passDate.get("LOAN_ACCOUNT_CLOSING") != null)) {
                            sqlMap.commitTransaction();
                        }
                        penalInterestMap = new HashMap();

                        System.out.println("insert Record  ###" + insertRecord);
                    } while (calculatePenalInterestAdvancesCalenderFreq(productRecord, passDate, actListMap));
                } catch (Exception e) {
                    sqlMap.rollbackTransaction();
//                        status.setStatus(BatchConstants.ERROR) ;
//                        HashMap errorMap = new HashMap();
//                        errorMap.put("ERROR_DATE",currDt);
//                        errorMap.put("TASK_NAME", taskLable);
//                        errorMap.put("ERROR_MSG",e.getMessage());
//                        errorMap.put("ACT_NUM",productRecord.get("ACCT_NUM"));
//                        errorMap.put("BRANCH_ID", productRecord.get("BRANCH_ID"));
//                        sqlMap.startTransaction();
//                        sqlMap.executeUpdate("insertError_showing", errorMap);
//                        sqlMap.commitTransaction();
                    String errMsg = "";
                    TTException tte = null;
                    HashMap exceptionMap = null;
                    HashMap excMap = null;
                    String strExc = null;
                    String errClassName = "";
                    e.printStackTrace();
                    if (e instanceof TTException) {
                        System.out.println("#$#$ if TTException part..." + e);
                        tte = (TTException) e;
                        if (tte != null) {
                            exceptionMap = tte.getExceptionHashMap();
                            System.out.println("#$#$ if TTException part exceptionMap ..." + exceptionMap);
                            if (exceptionMap != null) {
                                ArrayList list = (ArrayList) exceptionMap.get(CommonConstants.EXCEPTION_LIST);
                                errClassName = CommonUtil.convertObjToStr(exceptionMap.get(CommonConstants.CONSTANT_CLASS));
                                System.out.println("#$#$ if TTException part EXCEPTION_LIST ..." + list);
                                if (list != null && list.size() > 0) {
                                    for (int i = 0; i < list.size(); i++) {
                                        if (list.get(i) instanceof HashMap) {
                                            excMap = (HashMap) list.get(i);
                                            System.out.println("#$#$ if TTException part excMap ..." + excMap);
                                            strExc = CommonUtil.convertObjToStr(excMap.get(CommonConstants.ERR_MESSAGE))
                                                    + " (" + CommonUtil.convertObjToStr(excMap.get(CommonConstants.ERR_DATA)) + ")";
                                        } else {
                                            strExc = (String) list.get(i);
                                            System.out.println("#$#$ if TTException part strExc ..." + strExc);
                                        }
                                        HashMap errorMap = new HashMap();
                                        errorMap.put("ERROR_DATE", currDt);
                                        errorMap.put("TASK_NAME", taskLable);
                                        errorMap.put("ERROR_MSG", strExc);
                                        errorMap.put("ERROR_CLASS", errClassName);
                                        errorMap.put("ACT_NUM", productRecord.get("ACCT_NUM"));
                                        errorMap.put("BRANCH_ID", productRecord.get("BRANCH_ID"));
                                        sqlMap.startTransaction();
                                        sqlMap.executeUpdate("insertError_showing", errorMap);
                                        sqlMap.commitTransaction();
                                        errorMap = null;
                                    }
                                }
                            } else {
                                System.out.println("#$#$ if not TTException part..." + e);
                                errMsg = e.getMessage();
                                HashMap errorMap = new HashMap();
                                errorMap.put("ERROR_DATE", currDt);
                                errorMap.put("TASK_NAME", taskLable);
                                errorMap.put("ERROR_MSG", errMsg);
                                errorMap.put("ERROR_CLASS", errClassName);
                                errorMap.put("ACT_NUM", productRecord.get("ACCT_NUM"));
                                errorMap.put("BRANCH_ID", productRecord.get("BRANCH_ID"));
                                sqlMap.startTransaction();
                                sqlMap.executeUpdate("insertError_showing", errorMap);
                                sqlMap.commitTransaction();
                                errorMap = null;
                            }
                        }
                    } else {
                        System.out.println("#$#$ if not TTException part..." + e);
                        errMsg = e.getMessage();
                        HashMap errorMap = new HashMap();
                        errorMap.put("ERROR_DATE", currDt);
                        errorMap.put("TASK_NAME", taskLable);
                        errorMap.put("ERROR_MSG", errMsg);
                        errorMap.put("ERROR_CLASS", errClassName);
                        errorMap.put("ACT_NUM", productRecord.get("ACCT_NUM"));
                        errorMap.put("BRANCH_ID", productRecord.get("BRANCH_ID"));
                        sqlMap.startTransaction();
                        sqlMap.executeUpdate("insertError_showing", errorMap);
                        sqlMap.commitTransaction();
                        errorMap = null;
                    }
                    if (status != null) {
                        status.setStatus(BatchConstants.ERROR);
                    }
                    //                        System.out.println("Error thrown for Operative A/c No " + dataMap.get("ACT_NUM"));
                    //                e.printStackTrace();
                    tte = null;
                    exceptionMap = null;
                    excMap = null;
                    e.printStackTrace();
                }
            }
        }
        insertRecord.putAll(lastAppDt);

        insertRecord.put("LAST_INT_CALC_DT", passDate.get("LAST_INT_CALC_DT"));
        insertRecord.put("INTEREST_WAIVER", productRecord.get("INTEREST_WAIVER"));
        insertRecord.put("PENAL_WAIVER", productRecord.get("PENAL_WAIVER"));
        //added b y rishad  18/03/2014
        insertRecord.put("PRINCIPAL_WAIVER", productRecord.get("PRINCIPAL_WAIVER"));
        insertRecord.put("NOTICE_WAIVER", productRecord.get("NOTICE_WAIVER"));
          if(productRecord.containsKey("REBATE_MODE")&&productRecord.get("REBATE_MODE")!=null)
        {
         insertRecord.put("REBATE_MODE", productRecord.get("REBATE_MODE"));
        }
        if (insertRecord.containsKey("LOAN_CLOSING_PENAL_INT") && CommonUtil.convertObjToDouble(insertRecord.get("LOAN_CLOSING_PENAL_INT")).doubleValue() == 0.0) {
            insertRecord.put("REBATE_INTEREST", new Double(rebateInterestCalculation(productRecord)));
        } else {
            insertRecord.put("REBATE_INTEREST", new Double(0));
        }

        System.out.println("#$#$# Final return map : " + insertRecord);
        passDate = new HashMap();
        lastAppDt = new HashMap();

        return insertRecord;
    }

    /**
     * Penal interest is calculated basing on following critiria account debit
     * interest not serving with in reapyment frequency(declared in product
     * level) then we should calculate the penal interest basing out standing
     * balance from completed repayment frequency date to till date
     *
     */
    private boolean calculatePenalInterestAdvancesCalenderFreq(HashMap productRecord, HashMap passDate, HashMap actListMap) throws Exception {
        HashMap dataMap = new HashMap();
        HashMap resultMap = new HashMap();
        double paidInterest = 0;
        double debitedInterest = 0;
        String asCustomerComes = CommonUtil.convertObjToStr(productRecord.get("AS_CUSTOMER_COMES"));
        String behavesLike = CommonUtil.convertObjToStr(productRecord.get("BEHAVES_LIKE"));
        System.out.println("interestList IN-------->" + interestList);
        double penIntRate = 0;
        if (interestList != null) {
            for (int i = 0; i < interestList.size(); i++) {
                HashMap interestMap = (HashMap) interestList.get(i);
                penIntRate = CommonUtil.convertObjToDouble(interestMap.get("PENAL_INTEREST"));
            }
        }
        System.out.println("penIntRate IN-------->" + penIntRate);
        if (asCustomerComes.equals("N") && behavesLike.equals("OD")) {
            Date lastIntCalcDt = (Date) productRecord.get("LAST_INT_CALC_DT");
            int repayFreq = CommonUtil.convertObjToInt(productRecord.get("INTEREST_REPAY_FREQ"));
            Date fromDate = DateUtil.addDays(lastIntCalcDt, repayFreq);

            dataMap.put("ACCT_NUM", productRecord.get("ACCT_NUM"));
            dataMap.put("START_DT", fromDate);
            dataMap.put("LAST_INT_CALC_DT", productRecord.get("LAST_INT_CALC_DT"));
            dataMap.put("CURR_DT", currDt);
            System.out.println("dataMap" + dataMap);
            List lst = sqlMap.executeQueryForList("getSelectDebitInterest", dataMap);
            if (lst != null && lst.size() > 0) {
                resultMap = (HashMap) lst.get(0);
                debitedInterest = CommonUtil.convertObjToDouble(resultMap.get("DEBITEDINTEREST")).doubleValue();
            }

            paidInterest = CommonUtil.convertObjToDouble(sqlMap.executeQueryForObject("getSelectPaidInterest", dataMap)).doubleValue();
            if (debitedInterest > paidInterest) {
                if (penIntRate > 0) {
                    productRecord.put("PENALTY", "PENALTY");
                }
                System.out.println("productRecord@#@#" + productRecord);

                if (!actListMap.containsKey(CommonUtil.convertObjToStr(productRecord.get("ACCT_NUM")))) {
                    actListMap.put(productRecord.get("ACCT_NUM"), productRecord.get("ACCT_NUM"));
                    return true;
                }

            }
        }
        return false;

    }
    //only for as an when customer

    private HashMap oneMonthInterstCalculationforSanctionAmt(HashMap map, Date dt) throws Exception {
//        Date acct_open_dt=DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(map.get("LAST_INT_CALC_DT")));
//        long noOfdays=DateUtil.dateDiff(acct_open_dt, dt);
//        double amount =CommonUtil.convertObjToDouble(map.get("AMOUNT")).doubleValue();
//        HashMap  interestMap=null;
//        if(interestList !=null && interestList.size()>0)
//            interestMap=(HashMap)interestList.get(0);
//        int interestPenal= CommonUtil.convertObjToInt(interestMap.get("INTEREST"));
//        int interestPenalRate= CommonUtil.convertObjToInt(interestMap.get("PENAL_INTEREST"));
//        double interest= amount*noOfdays*interestPenal/36500;
//        interestMap=new HashMap();
//        interestMap.put("TOT_INTEREST",new Double(interest));
//        interest=roundOffLoanInterest(interestMap);
//        map.put("INTEREST",new Double(interest));
//        System.out.println("map   ####"+map);

        Date actOpenDt = (Date) map.get("ACCT_OPEN_DT");
        double amount = CommonUtil.convertObjToDouble(map.get("AMOUNT")).doubleValue();
//        int prematureFreq = CommonUtil.convertObjToInt(map.get("PREMATURE_FREQ"));
//        String prematurePeriod = CommonUtil.convertObjToStr(map.get("PREMATURE_PERIOD"));
        int prematureIntFreq = CommonUtil.convertObjToInt(map.get("PREMATURE_INT_FREQ"));
        String prematureIntPeriod = CommonUtil.convertObjToStr(map.get("PREMATURE_INT_PERIOD"));
        HashMap interestMap = null;
        if (interestList != null && interestList.size() > 0) {
            interestMap = (HashMap) interestList.get(0);
            int interestPenal = CommonUtil.convertObjToInt(interestMap.get("INTEREST"));
            double interest = 0;
            int divide = 0;
            if (prematureIntPeriod.equals("DAYS")) {
                divide = 36500;
            } else if (prematureIntPeriod.equals("MONTHS")) {
                divide = 1200;
            } else if (prematureIntPeriod.equals("YEARS")) {
                prematureIntFreq = prematureIntFreq * 12;
                divide = 1200;
            }
            interest = amount * prematureIntFreq * interestPenal / divide;
            interestMap = new HashMap();
            interestMap.put("TOT_INTEREST", new Double(interest));
            interest = roundOffLoanInterest(interestMap);
            map.put("INTEREST", new Double(interest));
            map.put("PREMATURE", "PREMATURE");
        }
        System.out.println("@@@@ oneMonthInterstCalculationforSanctionAmt map   ####" + map);
        return map;
    }

    private HashMap calculateOTSInterstDetails(HashMap productRecord) throws Exception {
        HashMap returnMap = new HashMap();
        HashMap dataMap = new HashMap();
        HashMap penalMap = new HashMap();
        double paidPrinicpel = 0, paidInterest = 0, paidPenal = 0, paidCharges = 0, totPaid = 0, otsAmount = 0, calculatePenal = 0, penalRate = 0;
        double otsPrincipal = 0, otsInterest = 0, otspenal = 0, otsCharges = 0;
        TermLoanCourtDetailsTO obj = null;
        double payableAmt = 0, totPayableAmt = 0;
        Date otd_dt = null;
        List lst = sqlMap.executeQueryForList("SelectTermLoanOTSInstallmentTO", productRecord.get("ACCT_NUM"));
        List lst1 = sqlMap.executeQueryForList("selectTermLoanCourtDetailsTO", productRecord.get("ACCT_NUM"));
        List hireachyTrans = sqlMap.executeQueryForList("selectAppropriatTransaction_OTS", productRecord.get("PROD_ID"));

        dataMap.put("ACT_NUM", productRecord.get("ACCT_NUM"));
        if (lst1 != null && lst1.size() > 0) {
            obj = (TermLoanCourtDetailsTO) lst1.get(0);
            otd_dt = obj.getOTSDate();
            otsAmount = CommonUtil.convertObjToDouble(obj.getSettlementAmt()).doubleValue();
            penalRate = CommonUtil.convertObjToDouble(obj.getPenal()).doubleValue();
            otsPrincipal = CommonUtil.convertObjToDouble(obj.getPrincipalAmount()).doubleValue();
            otsInterest = CommonUtil.convertObjToDouble(obj.getInterestAmount()).doubleValue();
            otspenal = CommonUtil.convertObjToDouble(obj.getPenalInterestAmount()).doubleValue();
            otsCharges = CommonUtil.convertObjToDouble(obj.getChargeAmount()).doubleValue();

        }
        if (otd_dt != null) {
            dataMap.put("FROM_DT", otd_dt);
            dataMap.put("TO_DATE", currDt);

        }
        List lst2 = sqlMap.executeQueryForList("getPaidPrinciple", dataMap);
        if (lst2 != null && lst2.size() > 0) {
            HashMap paidMap = (HashMap) lst2.get(0);
            paidPrinicpel = CommonUtil.convertObjToDouble(paidMap.get("PRINCIPLE")).doubleValue();
//            otsPrincipal-=paidPrinicpel;
            paidInterest = CommonUtil.convertObjToDouble(paidMap.get("INTEREST")).doubleValue();
//            otsInterest-=paidInterest;
            paidPenal = CommonUtil.convertObjToDouble(paidMap.get("PENAL")).doubleValue();
//            otspenal-=paidPenal;
            paidCharges = CommonUtil.convertObjToDouble(paidMap.get("EXPENSE")).doubleValue();
//            otsCharges-=paidCharges;
            totPaid = paidPrinicpel + paidInterest;

        }

        if (lst != null && lst.size() > 0) {
            TermLoanOTSInstallmentTO objTermLoanOTSInstallmentTO = new TermLoanOTSInstallmentTO();
            for (int i = 0; i < lst.size(); i++) {
                objTermLoanOTSInstallmentTO = (TermLoanOTSInstallmentTO) lst.get(i);
                payableAmt = CommonUtil.convertObjToDouble(objTermLoanOTSInstallmentTO.getInstallmentAmt()).doubleValue();
                Date instDate = objTermLoanOTSInstallmentTO.getInstallmentDate();

                if (DateUtil.dateDiff(currDt, instDate) >= 0) {
                    //                    if(payableAmt<=totPaid){
                    //                        totPaid-=payableAmt;
                    //                        continue;
                    //
                    //                    }else{
                    //                        if(totPaid !=0)
                    //                            payableAmt-=totPaid;
                    //                       totPayableAmt+=payableAmt;
                    //                    }
                    totPayableAmt += payableAmt;
                } else if (DateUtil.dateDiff(currDt, instDate) < 0) {
                    //calculate penal interest
                    long diffDate = DateUtil.dateDiff(otd_dt, currDt);
                    int days = (int) diffDate / 24 * 60 * 60 * 1000;

                    calculatePenal = (double) otsAmount * penalRate * diffDate / 36500;
                    System.out.println("otsAmount###" + otsAmount + "penalRate###" + penalRate + "diffDate" + diffDate + "calculatePenal" + calculatePenal);
                    penalMap.put("PROD_ID", productRecord.get("PROD_ID"));
                    penalMap.put("TOT_PENAL_INT", new Double(calculatePenal));
                    calculatePenal = roundOffLoanInterest(penalMap);
                    calculatePenal -= paidPenal;
                    returnMap.put("PENAL", new Double(calculatePenal));
                    returnMap.put("LOAN_CLOSING_PENAL_INT", new Double(calculatePenal));
                    if (payableAmt <= totPaid) {
                        totPaid -= payableAmt;
                        payableAmt = 0;
                    } else if (payableAmt > totPaid) {
                        payableAmt -= totPaid;
                        totPaid = 0;
                    }
                    totPayableAmt += payableAmt;
                    returnMap.put("DAYS", new Double(days));
                }
            }
            if (hireachyTrans != null && hireachyTrans.size() > 0) {
                HashMap hirachtMap = (HashMap) hireachyTrans.get(0);
                java.util.Iterator iter = hirachtMap.keySet().iterator();

                while (iter.hasNext()) {
                    String key = CommonUtil.convertObjToStr(iter.next());
                    String val = (String) hirachtMap.get(key);
                    System.out.println("val#####" + val + "totPayableAmt###" + totPayableAmt + "otsPrincipal" + otsPrincipal + "otspenal" + otspenal);
//                   if(val.equals("PRINCIPAL")){
                    if (totPayableAmt > 0 && paidPrinicpel > 0) {//otsPrincipal
                        if (totPayableAmt >= paidPrinicpel) {
                            totPayableAmt -= paidPrinicpel;
                            returnMap.put("PRINCIPAL", new Double(paidPrinicpel));
                        } else {
                            returnMap.put("PRINCIPAL", new Double(totPayableAmt));
                            totPayableAmt = 0;
                        }

                    }
//                   }else if(val.equals("PENALINTEREST")){
//                      if(totPayableAmt>0 && otspenal>0){
//                           if(totPayableAmt>=otspenal){
//                               totPayableAmt-=otspenal;
//                               returnMap.put("LOAN_CLOSING_PENAL_INT",new Double(otspenal+calculatePenal));
//                           }else{
//                               returnMap.put("LOAN_CLOSING_PENAL_INT",new Double(totPayableAmt+calculatePenal));
//                               totPayableAmt=0;
//                           }
//                          
//                           
//                      }
//                   }else if(val.equals("INTEREST")){
//                       
//                       if(totPayableAmt>0 && otsInterest>0){
//                           if(totPayableAmt>=otsInterest){
//                               totPayableAmt-=otsInterest;
//                                returnMap.put("INTEREST",new Double(otsInterest));
//                           }else{
//                               returnMap.put("INTEREST",new Double(totPayableAmt));
//                               totPayableAmt=0;
//                           }
//                          
//                      }
//                       
//                   }else if(val.equals("CHARGES")){
//                         if(totPayableAmt>0 && otsCharges>0){
//                           if(totPayableAmt>=otsCharges){
//                               totPayableAmt-=otsCharges;
//                                returnMap.put("MISCELLANEOUS CHARGES",new Double(otsCharges));
//                           }else{
//                               returnMap.put("MISCELLANEOUS CHARGES",new Double(totPayableAmt));
//                               totPayableAmt=0;
//                           }
//                          
//                      }
//                       
//                        
//                      }
                }

            }
            returnMap.put("TOTAL_OTS", new Double(otsAmount));
//            returnMap.put("INTEREST",new Double(totPayableAmt));
            returnMap.put("OTS", String.valueOf("Y"));
        }
        System.out.println("returnMap#####" + returnMap);
        return returnMap;
    }
    //only for as an when customer

    private HashMap oneMonthInterstCalculationforOutstandingAmt(HashMap map, Date dt) throws Exception {
//        Date last_int_calc_dt=DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(map.get("LAST_INT_CALC_DT")));
//        long noOfdays=DateUtil.dateDiff(last_int_calc_dt, dt)+2;
//        double amount =CommonUtil.convertObjToDouble(map.get("CLEAR_BALANCE")).doubleValue();
//        amount*=-1;
//        HashMap  interestMap=null;
//        if(interestList !=null && interestList.size()>0)
//             interestMap=(HashMap)interestList.get(0);
//            int interestPenal= CommonUtil.convertObjToInt(interestMap.get("INTEREST"));
//            int interestPenalRate= CommonUtil.convertObjToInt(interestMap.get("PENAL_INTEREST"));
//           double interest= amount*noOfdays*interestPenal/36500;
//           interestMap=new HashMap();
//           interestMap.put("TOT_INTEREST",new Double(interest));
//            interest=roundOffLoanInterest(interestMap);
//            map.put("INTEREST",new Double(interest));
        System.out.println("map   ####" + map);
        Date actOpenDt = (Date) map.get("ACCT_OPEN_DT");
        Date lastIntCalcDt = (Date) map.get("LAST_INT_CALC_DT");
        Date last_int_calc_dt = DateUtil.addDays(lastIntCalcDt, 1);
        double amount = CommonUtil.convertObjToDouble(map.get("CLEAR_BALANCE")).doubleValue();
        amount *= -1;
        int prematurePeriod = CommonUtil.convertObjToDouble(map.get("PREMATURE_FREQ")).intValue();
        int prematureIntFreq = CommonUtil.convertObjToInt(map.get("PREMATURE_INT_FREQ"));
        String prematureIntPeriod = CommonUtil.convertObjToStr(map.get("PREMATURE_INT_PERIOD"));
        int addMethod = CommonUtil.convertObjToInt(periodStringMap.get(CommonUtil.convertObjToStr(map.get("PREMATURE_PERIOD"))));
        HashMap interestMap = null;
        if (interestList != null && interestList.size() > 0) {
            interestMap = (HashMap) interestList.get(0);
            int interestPenal = CommonUtil.convertObjToInt(interestMap.get("INTEREST"));
            double interest = 0;
            int divide = 0;
            if (DateUtil.dateDiff(actOpenDt, last_int_calc_dt) == 0) {
                if (prematureIntPeriod.equals("DAYS")) {
                    divide = 36500;
                } else if (prematureIntPeriod.equals("MONTHS")) {
                    divide = 1200;
                } else if (prematureIntPeriod.equals("YEARS")) {
                    prematureIntFreq = prematureIntFreq * 12;
                    divide = 1200;
                }
            } else {
                System.out.println("@@@@ lastIntCalcDt ####" + lastIntCalcDt);
                System.out.println("@@@@ DateUtil.addDays(actOpenDt, prematurePeriod, addMethod, false) ####" + DateUtil.addDays(actOpenDt, prematurePeriod, addMethod, false));
                prematureIntFreq = (int) DateUtil.dateDiff(lastIntCalcDt, DateUtil.addDays(actOpenDt, prematurePeriod, addMethod, false));
                divide = 36500;
            }
            interest = amount * prematureIntFreq * interestPenal / divide;
            interestMap = new HashMap();
            interestMap.put("TOT_INTEREST", new Double(interest));
            interest = roundOffLoanInterest(interestMap);
            map.put("INTEREST", new Double(interest));
            map.put("PREMATURE", "PREMATURE");
            map.put("ROI", interestPenal);
        }
        System.out.println("@@@@ oneMonthInterstCalculationforOutstandingAmt map   ####" + map);
        return map;
    }

    private HashMap emiCalculater(HashMap productRecord, List interestList, HashMap passDate) throws Exception {
        System.out.println("productRecord ####0" + productRecord + "interestList  ##" + interestList + "&&" + passDate);
        HashMap map = new HashMap();
        HashMap returnMap = new HashMap();
        HashMap interestMap = new HashMap();
        //interestdetails
        if (interestList != null && interestList.size() > 0) {
            interestMap = (HashMap) interestList.get(0);
        }
        //

        map.put("ACCT_NUM", productRecord.get("ACCT_NUM"));
        Date curr_dt = (Date) currDt.clone();
        Date expiryDt = (Date) productRecord.get("TO_DATE");
        Date last_int_calc_dt = (Date) productRecord.get("LAST_INT_CALC_DT");
        map.put("CURR_DT", curr_dt);
        int noOfInstallmentCount = 0;
        double totalDue = 0.0;
        double totPrincipal = 0.0;
        double totInterest = 0.0;
        double emiAmt = 0.0;
        double oldEmiAmt = 0.0;
        long countdays = 0;
        long lastcountdays = 0;
        long expiryDtCount = 0;
        double amt = 0;
        long noOfinstallment = 0;
        int gracePeriod = 0;
//        StringBuffer showDueInstallmentNo=new StringBuffer();
        long showDueInstallmentCount = 0;
        Date instDt = null;
        List lst = null;
        double penalInt = 0;
        int interestPenal = 0, interestPenalRate = 0;
        System.out.println("map@@@@" + map);
        returnMap.put("PROD_ID", productRecord.get("PROD_ID"));
        interestPenal = CommonUtil.convertObjToInt(interestMap.get("INTEREST"));
        interestPenalRate = CommonUtil.convertObjToInt(interestMap.get("PENAL_INTEREST"));
        gracePeriod = CommonUtil.convertObjToInt(productRecord.get("GRACE_PERIOD_PENAL"));
        String isPrincipalPenal = CommonUtil.convertObjToStr(passDate.get("PENAL_APP_PRINCIPAL"));
        String isInterestPenal = CommonUtil.convertObjToStr(passDate.get("PENAL_APP_INTEREST"));

        noOfinstallment = CommonUtil.convertObjToLong(productRecord.get("NO_OF_INSTALLMENT"));
        map.put("NO_OF_INSTALLMENT", productRecord.get("NO_OF_INSTALLMENT"));
        if (noOfinstallment == 0) {
            lst = sqlMap.executeQueryForList("getEmiDue", map);
        } else {
            lst = sqlMap.executeQueryForList("getEmiDueBasedInstallment", map);
        }
        if (lst != null && lst.size() > 0) {
            for (int i = 0; i < lst.size(); i++) {
                HashMap singleRecord = (HashMap) lst.get(i);
                noOfInstallmentCount++;
                noOfinstallment = CommonUtil.convertObjToLong(productRecord.get("NO_OF_INSTALLMENT"));
                if (noOfinstallment > lst.size() || noOfinstallment == 0) {
                    noOfinstallment = lst.size();
                } else {
                    noOfinstallment = 0;
                }
                if (productRecord.containsKey("NO_OF_INSTALLMENT") && productRecord.get("NO_OF_INSTALLMENT") != null
                        && CommonUtil.convertObjToLong(productRecord.get("NO_OF_INSTALLMENT")) > 0 && CommonUtil.convertObjToLong(productRecord.get("NO_OF_INSTALLMENT")) == i) {

                    break;
                }
                totalDue += CommonUtil.convertObjToDouble(singleRecord.get("TOTAL_AMT")).doubleValue();
                double totalemiAmt = CommonUtil.convertObjToDouble(singleRecord.get("TOTAL_AMT")).doubleValue();// regarding mahila they are change totalamt into( principal amt only overdueinterest)totalamt in penal interest
                emiAmt = CommonUtil.convertObjToDouble(singleRecord.get("PRINCIPAL_AMT")).doubleValue();
                totPrincipal += CommonUtil.convertObjToDouble(singleRecord.get("PRINCIPAL_AMT")).doubleValue();
                totInterest += CommonUtil.convertObjToDouble(singleRecord.get("INTEREST_AMT")).doubleValue();
                double emiInterest = CommonUtil.convertObjToDouble(singleRecord.get("INTEREST_AMT")).doubleValue();
                instDt = (Date) singleRecord.get("INSTALLMENT_DT");
//                showDueInstallmentNo.append(singleRecord.get("INSTALLMENT_SLNO")+ new String(","));
                if (oldEmiAmt != 0 && oldEmiAmt != emiAmt) {
                    lastcountdays = DateUtil.dateDiff(instDt, curr_dt);
                    //                    break;
                }
                if (DateUtil.dateDiff(DateUtil.addDays(instDt, gracePeriod), curr_dt) <= 0) {
                    break;
                }
                if (DateUtil.dateDiff(instDt, curr_dt) >= 0) {
                    showDueInstallmentCount++;
                }
                if (DateUtil.dateDiff(instDt, curr_dt) > 0) {
                    if (DateUtil.dateDiff(curr_dt, expiryDt) > 0) {
                        countdays += DateUtil.dateDiff(instDt, DateUtil.addDays(curr_dt, -1));
                    } else {
                        countdays += DateUtil.dateDiff(instDt, expiryDt);
                    }
                    System.out.println("countdays ####" + countdays);
                    singleRecord.put("PRINCIPAL_BAL", singleRecord.get("TOTAL_AMT"));
                    singleRecord.put("START_DT", instDt);
                    singleRecord.put("END_DT", DateUtil.addDaysProperFormat(curr_dt, -1));
                    singleRecord.put(PENALTY, PENALTY);
                    singleRecord.put("NO_ROUND_OFF_INT", "NO_ROUND_OFF_INT");
                    System.out.println("emioverduecalculation" + singleRecord);
                    singleRecord.put(ROI, ROI);
                    oldEmiAmt = emiAmt;
                    if (countdays == 0) {
                        countdays = 1;
                    }
//                    if(countdays>0){
                    if (isInterestPenal.equals("Y")) {
                        penalInt += (emiInterest * countdays * interestPenalRate) / 36500;   //totalemiamt
                        System.out.println("penalInt ##" + penalInt + "isInterestPenal" + emiInterest + "countdays " + countdays + "interestPenalRate ##" + interestPenalRate);
                    }
                    if (isPrincipalPenal.equals("Y")) {
                        penalInt += (emiAmt * countdays * interestPenalRate) / 36500;
                        System.out.println("penalInt ##" + penalInt + "isPrincipalPenal" + emiInterest + "countdays " + countdays + "interestPenalRate ##" + interestPenalRate);
                    }
                    totInterest += (emiAmt * countdays * interestPenal) / 36500;
//                    }
                }
                if (DateUtil.dateDiff(expiryDt, curr_dt) > 0) {
                    amt = CommonUtil.convertObjToDouble(productRecord.get("LOAN_BALANCE_PRINCIPAL")).doubleValue();
                    expiryDtCount = DateUtil.dateDiff(expiryDt, curr_dt);
                }
                System.out.println("countdays" + countdays + "oldEmiAmt" + oldEmiAmt + "interestPenal ##" + interestPenalRate);
                countdays = 0;
            }
            System.out.println("totInterest afer due principal 1" + totPrincipal + "totInterest afer due interest 1   :" + totInterest + "emiAmt"
                    + emiAmt);

            /**
             * * ======= penal interest
             */
            //            double penalInt=0;
            //            if(countdays>0)
            //                penalInt= (oldEmiAmt*countdays*interestPenalRate)/36500;
//            if(lastcountdays>0)
//                penalInt+= (emiAmt*lastcountdays*interestPenalRate)/36500;
            /**
             * * ======= interest
             */
            System.out.println("countdays" + countdays + "oldEmiAmt" + oldEmiAmt + "interestPenal ##" + interestPenal);
            //            totInterest+= (oldEmiAmt*countdays*interestPenal)/36500;
            System.out.println("lastcountdays" + lastcountdays + "emiAmt" + emiAmt + "interestPenal ##" + interestPenal + "penalInt###" + penalInt);
//            if(lastcountdays>0)
//                totInterest+= (emiAmt*lastcountdays*interestPenal)/36500;
            if (amt > 0) {
//                 if(isPrincipalPenal.equals("Y")){
                totInterest += (amt * expiryDtCount * CommonUtil.convertObjToInt(interestMap.get("INTEREST"))) / 36500;
//                 }
                if (isPrincipalPenal.equals("Y")) {
                    penalInt += (amt * expiryDtCount * CommonUtil.convertObjToInt(interestMap.get("PENAL_INTEREST"))) / 36500;
                }
                System.out.println("INTEREST after expiry" + totInterest + "PENAL INTEREST" + penalInt);
            }

            ///EMI INT CALC LAST INTCALC DT TO CURR DT
            if (productRecord.containsKey("LOAN_EMI_CLOSE") && productRecord.get("LOAN_EMI_CLOSE") != null) {
                if (instDt != null && DateUtil.dateDiff(instDt, curr_dt) > 0) {

                    interestPenal = CommonUtil.convertObjToInt(interestMap.get("INTEREST"));
                    interestPenalRate = CommonUtil.convertObjToInt(interestMap.get("PENAL_INTEREST"));
                    amt = CommonUtil.convertObjToDouble(productRecord.get("LOAN_BALANCE_PRINCIPAL")).doubleValue();
                    instDt = DateUtil.addDays(instDt, 1);
                    long noOfDays = DateUtil.dateDiff(instDt, curr_dt);
                    //                    double penalInt=0;
                    //                     penalInt+= (amt*noOfDays*interestPenalRate)/36500;
                    totInterest += (amt * noOfDays * interestPenal) / 36500;
                    //                    returnMap.put("TOT_INTEREST", new Double(penalInt));
                    //                    penalInt=roundOffLoanInterest(returnMap);
                    //
                }
            }
            returnMap.put("TOT_INTEREST", new Double(penalInt));
            penalInt = roundOffLoanInterest(returnMap);
            returnMap.put("LOAN_CLOSING_PENAL_INT", new Double(penalInt));
            System.out.println("calculatePenlaInt###" + penalInt);
            returnMap.put("TOT_INTEREST", new Double(totInterest));
            totInterest = roundOffLoanInterest(returnMap);
            returnMap.put("INTEREST", new Double(totInterest));
            returnMap.put("ASSET_STATUS", productRecord.get("ASSET_STATUS"));
            returnMap.put("ROI", interestMap.get("INTEREST"));
            returnMap.put("REPAYMENT_TYPE", "EMI");
            returnMap.put("PRINCIPAL_DUE", new Double(totPrincipal));
            if (noOfInstallmentCount > 0) {
                returnMap.put("NO_OF_INSTALLMENT", new Long(noOfInstallmentCount));
            }
            returnMap.put("SHOW_INSTALLMENT_NO", new Long(showDueInstallmentCount));
            System.out.println("returnMap#####" + returnMap);
        } else {
            //            map.put("ACT_NUM",map.get("ACCT_NUM"));
            //            List minInsDtlst=sqlMap.executeQueryForList("getMinimaminstallmentTL",map);
            //            HashMap lstInsMap=null;
            //            if(minInsDtlst !=null && minInsDtlst.size()>0)
            //                lstInsMap=(HashMap)minInsDtlst.get(0);
            //            Date last_inst_dt=(Date)lstInsMap.get("INSTALLMENT_DT");
            if (productRecord.containsKey("LOAN_EMI_CLOSE") && productRecord.get("LOAN_EMI_CLOSE") != null) {
                if (DateUtil.dateDiff(last_int_calc_dt, curr_dt) > 0) {
//                    int interestPenal=0;
//                    int interestPenalRate=0;
                    interestPenal = CommonUtil.convertObjToInt(interestMap.get("INTEREST"));
                    interestPenalRate = CommonUtil.convertObjToInt(interestMap.get("PENAL_INTEREST"));
                    amt = CommonUtil.convertObjToDouble(productRecord.get("LOAN_BALANCE_PRINCIPAL")).doubleValue();
                    last_int_calc_dt = DateUtil.addDays(last_int_calc_dt, 1);
                    long noOfDays = DateUtil.dateDiff(last_int_calc_dt, curr_dt);
//                    double penalInt=0;
                    //                     penalInt+= (amt*noOfDays*interestPenalRate)/36500;
                    totInterest += (amt * noOfDays * interestPenal) / 36500;
                    //                    returnMap.put("TOT_INTEREST", new Double(penalInt));
                    //                    penalInt=roundOffLoanInterest(returnMap);
                    returnMap.put("LOAN_CLOSING_PENAL_INT", new Double(penalInt));
                    System.out.println("calculatePenlaInt###" + penalInt);
                    returnMap.put("TOT_INTEREST", new Double(totInterest));
                    totInterest = roundOffLoanInterest(returnMap);
                    returnMap.put("INTEREST", new Double(totInterest));
                    returnMap.put("ASSET_STATUS", productRecord.get("ASSET_STATUS"));
                    returnMap.put("ROI", interestMap.get("INTEREST"));
                    returnMap.put("REPAYMENT_TYPE", "EMI");
                    returnMap.put("PRINCIPAL_DUE", new Double(totPrincipal));
                    System.out.println("returnMap2  #####" + returnMap);
                }
            }
            if (!productRecord.containsKey("LOAN_EMI_CLOSE")) {
                returnMap.put("PRINCIPAL_DUE", new Double(0));
                returnMap.put("REPAYMENT_TYPE", "EMI");
                returnMap.put("ASSET_STATUS", productRecord.get("ASSET_STATUS"));
                returnMap.put("ROI", interestMap.get("INTEREST"));
            }
        }

        /* The following block added by Rajesh 
         * for Calculating Holiday Period (Moratorium) Interest
         * if party is paying amount before starting the First Installment date
         */
        Date firstInstallmentDt = (Date) productRecord.get("FIRST_INSTALL_DT");
        if (firstInstallmentDt != null) {
            Date lastIntCalcDt = (Date) productRecord.get("LAST_INT_CALC_DT");
            lastIntCalcDt = DateUtil.addDays(lastIntCalcDt, 1);
            System.out.println("###firstInstallmentDt:" + firstInstallmentDt
                    + "###currDt:" + currDt
                    + "###lastIntCalcDt:" + lastIntCalcDt);
            System.out.println("DateUtil.dateDiff(currDt, firstInstallmentDt):" + DateUtil.dateDiff(currDt, firstInstallmentDt));
            System.out.println("DateUtil.dateDiff(lastIntCalcDt,curr_dt):" + DateUtil.dateDiff(lastIntCalcDt, curr_dt));
            if (DateUtil.dateDiff(currDt, firstInstallmentDt) >= 0) {
                System.out.println("###inside if 1");
                long noOfDays = DateUtil.dateDiff(lastIntCalcDt, curr_dt);
                if (noOfDays > 0) {
                    System.out.println("###inside if 2");
                    amt = CommonUtil.convertObjToDouble(productRecord.get("LOAN_BALANCE_PRINCIPAL")).doubleValue();
                    double holidayInterest = (amt * noOfDays * interestPenal) / 36500;
                    double totInt = CommonUtil.convertObjToDouble(returnMap.get("INTEREST")).doubleValue();
                    returnMap.put("TOT_INTEREST", new Double(holidayInterest));
                    holidayInterest = roundOffLoanInterest(returnMap);
                    totInt += holidayInterest;
                    returnMap.put("INTEREST", new Double(totInt));
                    returnMap.put("MORATORIUM_INT_FOR_EMI", new Double(holidayInterest));
                }
            }
        }
        System.out.println("###returnMap after calculating Holiday interest :" + returnMap);
        return returnMap;
    }

    /**
     * kerla co operative bank purpose created we need to calculated monthly and
     * semi monthy also customer paying amoun before due date we should give
     * some discuont ex monthly installment customer paying amt 16 days before
     * we should take only 15 days interest
     */
    private HashMap emiCalculaterforKerla(HashMap productRecord, List interestList) throws Exception {
        System.out.println("productRecord ####0" + productRecord + "interestList  ##" + interestList);
        HashMap map = new HashMap();
        HashMap returnMap = new HashMap();
        HashMap interestMap = new HashMap();
        //interestdetails
        if (interestList != null && interestList.size() > 0) {
            interestMap = (HashMap) interestList.get(0);
        }
        //

        map.put("ACCT_NUM", productRecord.get("ACCT_NUM"));
        Date curr_dt = (Date) currDt.clone();
        Date expiryDt = (Date) productRecord.get("TO_DATE");
        Date last_int_calc_dt = (Date) productRecord.get("LAST_INT_CALC_DT");
        if (DateUtil.dateDiff(last_int_calc_dt, currDt) <= 0) {
            returnMap.put("LAST_CALC_DT", last_int_calc_dt);
            returnMap.put("ROI", interestMap.get("INTEREST"));
            returnMap.put("INTEREST", CommonUtil.convertObjToDouble(productRecord.get("MIN_DEBITINT_AMT")));
            return returnMap;
        }
        map.put("CURR_DT", curr_dt);
        int noOfInstallmentCount = 0;
        double totalDue = 0.0;
        double totPrincipal = 0.0;
        double totInterest = 0.0;
        double emiAmt = 0.0;
        double oldEmiAmt = 0.0;
        long countdays = 0;
        long lastcountdays = 0;
        long expiryDtCount = 0;
        double amt = 0;
        long noOfinstallment = 0;
        //        StringBuffer showDueInstallmentNo=new StringBuffer();
        long showDueInstallmentCount = 0;
        Date instDt = null;
        List lst = null;
        double penalInt = 0;
        double calcOverDueInt = 0;
        int interestPenal = 0, interestPenalRate = 0;
        HashMap lastPaidDateMap = new HashMap();
        HashMap nextInstDateMap = new HashMap();
        HashMap lastInstallDateMap = new HashMap();
        System.out.println("map@@@@" + map);
        interestPenal = CommonUtil.convertObjToInt(interestMap.get("INTEREST"));
        interestPenalRate = CommonUtil.convertObjToInt(interestMap.get("PENAL_INTEREST"));
        Date actOpenDt = (Date) productRecord.get("ACCT_OPEN_DT");
        noOfinstallment = CommonUtil.convertObjToLong(productRecord.get("NO_OF_INSTALLMENT"));
        map.put("NO_OF_INSTALLMENT", productRecord.get("NO_OF_INSTALLMENT"));

        int parameter1 = CommonUtil.convertObjToInt(productRecord.get("PARAMETER_1"));  // value is 9
        int parameter2 = CommonUtil.convertObjToInt(productRecord.get("PARAMETER_2"));  // value is 10
        int parameter3 = CommonUtil.convertObjToInt(productRecord.get("PARAMETER_3"));  // value is 20

        lst = sqlMap.executeQueryForList("getLastPaidDate", productRecord);
        if (lst != null && lst.size() > 0) {
            lastPaidDateMap = (HashMap) lst.get(0);
            lst = null;
        }

        if (lastPaidDateMap != null && lastPaidDateMap.containsKey("LASTPAID_INSTALLMENT_DT") && lastPaidDateMap.get("LASTPAID_INSTALLMENT_DT") != null) {
            System.out.println("lastPaidDateMap NOT NULL ###" + lastPaidDateMap);
            productRecord.put("LASTPAID_INSTALLMENT_DT", lastPaidDateMap.get("LASTPAID_INSTALLMENT_DT"));
        } else {
            System.out.println("lastPaidDateMap IS NULL ###");
            lastPaidDateMap.put("LASTPAID_INSTALLMENT_DT", actOpenDt);
            productRecord.put("LASTPAID_INSTALLMENT_DT", actOpenDt);
        }
        System.out.println("productRecord BEFORE getNextInstallmentDueDate :" + productRecord);
        lst = sqlMap.executeQueryForList("getNextInstallmentDueDate", productRecord);
        if (lst != null && lst.size() > 0) {
            nextInstDateMap = (HashMap) lst.get(0);
            System.out.println("nextInstDateMap###" + nextInstDateMap);
            lst = null;
        }
        if (!(nextInstDateMap != null && nextInstDateMap.containsKey("NEXT_INSTALLMENT_DT") && nextInstDateMap.get("NEXT_INSTALLMENT_DT") != null)) {
            nextInstDateMap.put("NEXT_INSTALLMENT_DT", actOpenDt);
        }

        productRecord.put("CURR_DT", currDt);
        lst = sqlMap.executeQueryForList("getLastInstallmentDate", productRecord);
        if (lst != null && lst.size() > 0) {
            lastInstallDateMap = (HashMap) lst.get(0);
            lst = null;
        }
        if (!(lastInstallDateMap != null && lastInstallDateMap.containsKey("LAST_INSTALLMENT_DT") && lastInstallDateMap.get("LAST_INSTALLMENT_DT") != null)) {
            lastInstallDateMap = new HashMap();
            lastInstallDateMap.put("LAST_INSTALLMENT_DT", actOpenDt);
        }
        double totCalculatedInt = 0;
        System.out.println("nextInstDateMap###" + nextInstDateMap + "nextInstDateMap###" + nextInstDateMap + "lastInstallDateMap###" + lastInstallDateMap);
        Date lastInstal_paidDt = (Date) lastPaidDateMap.get("LASTPAID_INSTALLMENT_DT");
        Date lastInstal_Dt = (Date) lastInstallDateMap.get("LAST_INSTALLMENT_DT");
        Date nextInstal_Dt = (Date) nextInstDateMap.get("NEXT_INSTALLMENT_DT");

        int interestRate = CommonUtil.convertObjToInt(interestMap.get("INTEREST"));
        double clearBalance = CommonUtil.convertObjToDouble(productRecord.get("CLEAR_BALANCE")).doubleValue() * -1;
        //DIFF_DAYS := ASONDATE - LASTPAIDUPTO ;
        long countDays = DateUtil.dateDiff(lastInstal_paidDt, currDt);
        System.out.println("DIFF_DAYS###" + countDays);
        // NXT_DAYS := (LSTINSTDATE + 9) - LASTPAIDUPTO;

        long nextDays = DateUtil.dateDiff(lastInstal_paidDt, DateUtil.addDays(lastInstal_Dt, parameter1));
        System.out.println("NXT_DAYS###" + nextDays);
        //NOM := MONTHS_BETWEEN(ASONDATE,LASTPAIDUPTO);

        long noMonthsInDays = DateUtil.dateDiff(lastInstal_paidDt, currDt);
        int noMonth = (int) noMonthsInDays / 30;
        System.out.println("noMonth###" + noMonth);

        Date lastCalcDt = null;
        //IF  DIFF_DAYS <=20 AND DIFF_DAYS > 0 THEN
        System.out.println("countDays#####" + countDays + "nextDays" + nextDays + "noMonthsInDays###" + noMonthsInDays + "currDt" + currDt);
        if (countDays <= parameter3 && countDays > 0) {
            //calculate 15 days interest only
            // -- FROM LSTINSTDATE; HALF MONTH INTEREST
            totCalculatedInt = (clearBalance * interestRate) * ((double) 1 / 2400);
            lastCalcDt = DateUtil.addDays(lastInstal_paidDt, 15);
            System.out.println("#$#$ 15 DAYS INTEREST:" + totCalculatedInt);
        } else {
            //IF (ASONDATE >= (LASTPAIDUPTO + 20) AND ASONDATE <= (NXTINSTDT + 9)) THEN
            if ((DateUtil.dateDiff(DateUtil.addDays(lastInstal_paidDt, parameter3), currDt) >= 0) && DateUtil.dateDiff(DateUtil.addDays(nextInstal_Dt, parameter1), currDt) <= 0) {
                //calculate 1 month interest
                // -- FROM LSTINSTDATE; ONE MONTH INTEREST
                totCalculatedInt = (clearBalance * interestRate) * ((double) 1 / 1200);
                lastCalcDt = DateUtil.addDays(lastInstal_paidDt, 30);
                System.out.println("#$#$ 1 MONTH INTEREST:" + totCalculatedInt);
            } else {
                //  IF (ASONDATE >= (LSTINSTDATE + 10) AND ASONDATE <= (LSTINSTDATE + 20)  ) THEN
                if (DateUtil.dateDiff(DateUtil.addDays(lastInstal_Dt, parameter2), currDt) >= 0 && DateUtil.dateDiff(DateUtil.addDays(lastInstal_Dt, parameter3), currDt) <= 0) {
                    //    DBMS_OUTPUT.PUT_LINE(  NOM || ' AND HALF MONTH INTEREST');
                    //  -- FROM LSTINSTDATE; 'NOM' MONTHS AND HALF MONTH INTEREST
                    totCalculatedInt = (clearBalance * interestRate) * ((double) noMonth / 1200);
                    totCalculatedInt += (clearBalance * interestRate) * ((double) 1 / 2400);
                    lastCalcDt = DateUtil.addDays(lastInstal_paidDt, noMonth * 30);
                    lastCalcDt = DateUtil.addDays(lastCalcDt, 15);
                    System.out.println("#$#$ 10 TO 20 DAYS AFTER LAST_INSTALLMENT_DATE");
                    System.out.println("#$#$ " + noMonth + " MONTHS AND HALF MONTH INTEREST:" + totCalculatedInt);
                } else {
                    if (DateUtil.dateDiff(DateUtil.addDays(lastInstal_Dt, parameter2), currDt) < 0) {
                        //  DBMS_OUTPUT.PUT_LINE(NOM || ' MONTH INTEREST');
                        //-- FROM LSTINSTDATE; MONTH INTEREST
                        totCalculatedInt = (clearBalance * interestRate) * ((double) noMonth / 1200);
                        lastCalcDt = DateUtil.addDays(lastInstal_paidDt, noMonth * 30);
                        System.out.println("#$#$ LESS THAN 9 DAYS AFTER LAST_INSTALLMENT_DATE");
                        System.out.println("#$#$ " + noMonth + " MONTHS INTEREST:" + totCalculatedInt);
                    } else if (DateUtil.dateDiff(DateUtil.addDays(lastInstal_Dt, parameter3), currDt) > 0) {
                        totCalculatedInt = (clearBalance * interestRate) * ((double) noMonth / 1200);
                        totCalculatedInt += (clearBalance * interestRate) * ((double) 1 / 1200);
                        lastCalcDt = DateUtil.addDays(lastInstal_paidDt, (noMonth + 1) * 30);
                        System.out.println("#$#$ GREATER THAN 20 DAYS AFTER LAST_INSTALLMENT_DATE");
                        System.out.println("#$#$ " + (noMonth + 1) + " MONTHS INTEREST:" + totCalculatedInt);
                    }
                }
            }

        }

        returnMap.put("TOT_INTEREST", new Double(totCalculatedInt));
        totInterest = roundOffLoanInterest(returnMap);
        if (totInterest > 0) {
            returnMap.put("INTEREST", new Double(totInterest));
        } else {
            returnMap.put("INTEREST", CommonUtil.convertObjToDouble(productRecord.get("MIN_DEBITINT_AMT")));
        }
        returnMap.put("ASSET_STATUS", productRecord.get("ASSET_STATUS"));
        returnMap.put("ROI", interestMap.get("INTEREST"));
        returnMap.put("LAST_INT_CALC_DT", lastInstal_paidDt);
        Date futureLastIntCalcDt = (Date) lastInstal_paidDt.clone();
        futureLastIntCalcDt.setDate(lastCalcDt.getDate());
        futureLastIntCalcDt.setMonth(lastCalcDt.getMonth());
        futureLastIntCalcDt.setYear(lastCalcDt.getYear());
        returnMap.put("FUTURE_LAST_INT_CALC_DT", futureLastIntCalcDt);
        returnMap.put("SECRETARIAT_INT", "Y");
        returnMap.put("MIN_DEBITINT_AMT", productRecord.get("MIN_DEBITINT_AMT"));

        System.out.println("###returnMap after calculating Holiday interest :" + returnMap);
        return returnMap;
    }

    private void npaInterest(HashMap npaMap) throws Exception {
        System.out.println("npa Interest#####" + npaMap);
        if ((npaMap.containsKey("INTEREST") && CommonUtil.convertObjToDouble(npaMap.get("INTEREST")).doubleValue() > 0)
                || (npaMap.containsKey("TOT_PENAL_INT") && CommonUtil.convertObjToDouble(npaMap.get("TOT_PENAL_INT")).doubleValue() > 0)) {
            List maxLoanTrans = null;
            if (CommonUtil.convertObjToStr(npaMap.get("BEHAVES_LIKE")).equals("OD")) {
                maxLoanTrans = sqlMap.executeQueryForList("getIntDetailsAD", npaMap);
            } else {
                maxLoanTrans = sqlMap.executeQueryForList("getIntDetails", npaMap);
            }
            HashMap maxLoanTransmap = null;
            int trans_slno = 1;
            if (maxLoanTrans != null && maxLoanTrans.size() > 0) {
                maxLoanTransmap = (HashMap) maxLoanTrans.get(maxLoanTrans.size() - 1);
                trans_slno = CommonUtil.convertObjToInt(maxLoanTransmap.get("TRANS_SLNO"));
                trans_slno++;
            }
            if (maxLoanTransmap != null) {
                double amount = CommonUtil.convertObjToDouble(npaMap.get("INTEREST")).doubleValue();
                if (amount == 0) {
                    amount = CommonUtil.convertObjToDouble(npaMap.get("TOT_PENAL_INT")).doubleValue();
                }
                npaMap.put("TRN_CODE", "NPA_INT");
//                npaMap.put("PBAL", String.valueOf(new Double(CommonUtil.convertObjToDouble(maxLoanTransmap.get("PBAL")).doubleValue())));
//                npaMap.put("PRINCIPLE", String.valueOf(new Double(0)));
//                npaMap.put("IBAL", String.valueOf(new Double(CommonUtil.convertObjToDouble(maxLoanTransmap.get("IBAL")).doubleValue())));
//                npaMap.put("INTEREST", String.valueOf(new Double(0)));
//                npaMap.put("PIBAL", String.valueOf(new Double(CommonUtil.convertObjToDouble(maxLoanTransmap.get("PIBAL")).doubleValue())));
//                npaMap.put("PENAL", String.valueOf(new Double(0)));
//                npaMap.put("EBAL", String.valueOf(new Double(CommonUtil.convertObjToDouble(maxLoanTransmap.get("EBAL")).doubleValue())));
//                npaMap.put("EXPENSE", String.valueOf(new Double(0)));
//                if (npaMap.containsKey("INTEREST") && npaMap.get("INTEREST") != null && (!npaMap.containsKey("TOT_PENAL_INT"))) {
//                    npaMap.put("TRN_CODE", "NPA_INT");
//                    npaMap.put("NPA_INT_BAL", new Double(amount + CommonUtil.convertObjToDouble(maxLoanTransmap.get("NPA_INT_BAL"))));
//                    npaMap.put("DAY_END_BALANCE", String.valueOf(new Double(CommonUtil.convertObjToDouble(maxLoanTransmap.get("DAY_END_BALANCE")).doubleValue() + amount)));
//                    npaMap.put("NPA_INTEREST", new Double(amount));
//                } else {
//                    npaMap.put("NPA_INT_BAL", new Double(CommonUtil.convertObjToDouble(maxLoanTransmap.get("NPA_INTEREST"))));
//                    npaMap.put("NPA_INTEREST",new Double(0));
//                }
//                if (npaMap.containsKey("TOT_PENAL_INT") && npaMap.get("TOT_PENAL_INT") != null) {
//                    npaMap.put("TRN_CODE", "NPA_PENAL_INT");
//                    npaMap.put("NPA_PENAL_BAL", new Double(amount + CommonUtil.convertObjToDouble(maxLoanTransmap.get("NPA_PENAL_BAL"))));
//                    npaMap.put("NPA_PENAL", new Double(amount));
//                } else {
//                    npaMap.put("NPA_PENAL_BAL", new Double(CommonUtil.convertObjToDouble(maxLoanTransmap.get("NPA_PENAL_BAL"))));
//                    npaMap.put("NPA_PENAL", new Double(0));
//                }
//                npaMap.put("BRANCH_CODE", _branchCode);
//                npaMap.put("TODAY_DT", currDt);
//                npaMap.put("AUTHORIZE_STATUS", "AUTHORIZED");
//                npaMap.put("AUTHORIZE_BY", "TTSYSTEM");
//                npaMap.put("TRANS_SLNO", new Long(trans_slno));
//                adv_uniqKey = new Long(trans_slno);
//                npaMap.put("TRANS_MODE", "TRANSFER");
                npaMap.put("PBAL", CommonUtil.convertObjToDouble(maxLoanTransmap.get("PBAL")));
                npaMap.put("PRINCIPLE", new Double(0));
                npaMap.put("IBAL", CommonUtil.convertObjToDouble(maxLoanTransmap.get("IBAL")).doubleValue());
                npaMap.put("INTEREST", new Double(0));
                npaMap.put("PIBAL", CommonUtil.convertObjToDouble(maxLoanTransmap.get("PIBAL")).doubleValue());
                npaMap.put("PENAL", new Double(0));
                npaMap.put("EBAL", CommonUtil.convertObjToDouble(maxLoanTransmap.get("EBAL")).doubleValue());
                npaMap.put("EXPENSE", new Double(0));
                if (npaMap.containsKey("INTEREST") && npaMap.get("INTEREST") != null && (!npaMap.containsKey("TOT_PENAL_INT"))) {
                    npaMap.put("TRN_CODE", "NPA_INT");
                    npaMap.put("NPA_INT_BAL", new Double(amount + CommonUtil.convertObjToDouble(maxLoanTransmap.get("NPA_INT_BAL"))));
                    npaMap.put("DAY_END_BALANCE", String.valueOf(new Double(CommonUtil.convertObjToDouble(maxLoanTransmap.get("DAY_END_BALANCE")).doubleValue() + amount)));
                    npaMap.put("NPA_INTEREST", new Double(amount));
                } else {
                    npaMap.put("NPA_INT_BAL", new Double(CommonUtil.convertObjToDouble(maxLoanTransmap.get("NPA_INTEREST"))));
                    npaMap.put("NPA_INTEREST",new Double(0));
                }
                if (npaMap.containsKey("TOT_PENAL_INT") && npaMap.get("TOT_PENAL_INT") != null) {
                    npaMap.put("TRN_CODE", "NPA_PENAL_INT");
                    npaMap.put("NPA_PENAL_BAL", new Double(amount + CommonUtil.convertObjToDouble(maxLoanTransmap.get("NPA_PENAL_BAL"))));
                    npaMap.put("NPA_PENAL", new Double(amount));
                } else {
                    npaMap.put("NPA_PENAL_BAL", new Double(CommonUtil.convertObjToDouble(maxLoanTransmap.get("NPA_PENAL_BAL"))));
                    npaMap.put("NPA_PENAL", new Double(0));
                }
                npaMap.put("BRANCH_CODE", _branchCode);
                npaMap.put("TODAY_DT", currDt);
                npaMap.put("AUTHORIZE_STATUS", "AUTHORIZED");
                npaMap.put("AUTHORIZE_BY", "TTSYSTEM");
                npaMap.put("TRANS_SLNO", new Long(trans_slno));
                adv_uniqKey = new Long(trans_slno);
                npaMap.put("TRANS_MODE", "TRANSFER");
                if (CommonUtil.convertObjToStr(npaMap.get("BEHAVES_LIKE")).equals("OD")) {
                    sqlMap.executeUpdate("insertAuthorizeAdvTransDetails", npaMap);
                } else {
                    sqlMap.executeUpdate("insertintoloanTransAuthDetails", npaMap);
                }
            }
        }
        sqlMap.executeUpdate("updateclearBal", npaMap);
        npaMap.put("AMOUNT", total_Penal);
        sqlMap.executeUpdate("updateadvPbal", npaMap);
        //        return npaMap;
    }

    private void insertLoanInteresttmp(HashMap interestMap) throws Exception {
        System.out.println("insertLoanInteresttmp####" + interestMap);
        HashMap loanIntMap = new HashMap();
        int noofdays = 0;
        //         List lst=sqlMap.executeQueryForList("selectLoanInterestTMP",interestMap);
        //         if( lst!=null && lst.size()>0)
        //             sqlMap.executeUpdate("deleteLoanInterestTMP",loanIntMap);
        double amt = CommonUtil.convertObjToDouble(interestMap.get("AMT")).doubleValue();
        if (interestMap.get("INTEREST") != null) {   //This condition added by Rajesh
            noofdays = CommonUtil.convertObjToInt(DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(interestMap.get("START_DT"))),
                    DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(interestMap.get("END_DT")))));
            System.out.println("noof days####" + noofdays);
            Date dayenddt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(interestMap.get("START_DT")));
            Date nextdt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(interestMap.get("END_DT")));

            if (CommonUtil.convertObjToStr(interestMap.get("VALUEDATE")).equals("VALUEDATE_DR")) {

                loanIntMap.put("REMARKS", "1");
                loanIntMap.put("VALIDATE_TYPE", "VALUEDATE_DR");
            } else if (CommonUtil.convertObjToStr(interestMap.get("VALUEDATE")).equals("VALUEDATE_CR")) {
                loanIntMap.put("REMARKS", "2");
                loanIntMap.put("VALIDATE_TYPE", "VALUEDATE_CR");
            } else if (CommonUtil.convertObjToStr(interestMap.get("PENALTY")).equals("PENALTY")) {
                loanIntMap.put("REMARKS", "3");
                loanIntMap.put("VALIDATE_TYPE", "PENALTY");
            } else {
                loanIntMap.put("REMARKS", "0");
                loanIntMap.put("VALIDATE_TYPE", "ROI");
            }
            noofdays += 1;
//            if(noofdays==0)
//                noofdays+=1;
//            else
//                noofdays+=2;
            System.out.println("noof after  days####" + noofdays);
            if (amt < 0) {
                amt *= -1;
            }
            if (CommonUtil.convertObjToStr(interestMap.get("ACCT_NUM")).length() > 0) {
                loanIntMap.put("ACT_NUM", interestMap.get("ACCT_NUM"));
            } else {
                loanIntMap.put("ACT_NUM", interestMap.get("ACT_NUM"));
            }
            loanIntMap.put("FROM_DT", dayenddt);
            loanIntMap.put("TO_DATE", nextdt);
            loanIntMap.put("AMT", new Double(amt));
            loanIntMap.put("NO_OF_DAYS", noofdays);
            loanIntMap.put("TOT_PRODUCT", new Double(amt * noofdays));
            loanIntMap.put("INT_AMT", interestMap.get("INTEREST"));
            loanIntMap.put("PROD_ID", interestMap.get("PROD_ID"));
            loanIntMap.put("CUST_ID", interestMap.get("CUST_ID"));
            loanIntMap.put("INT_RATE", CommonUtil.convertObjToDouble(interestRate));
            loanIntMap.put("CURR_DT", applDt); // Added by Rajesh
            loanIntMap.put("BRANCH_CODE", _branchID);
            loanIntMap.put("USER_ID", user);
//            VALIDATE_TYPE
            System.out.println("loanIntMap#####@@@@" + loanIntMap);

            sqlMap.executeQueryForList("insertLoanInterestTMPPROCEDURE", loanIntMap);
            //sqlMap.executeUpdate("insertLoanInterestTMP",loanIntMap); // COMMENT BY ABI WE MADE AURONOMOUS TRANSACTION THROUGH THE ORACLE PROCEDURE
        }
    }

    private void insertLoanInterest(HashMap interestMap) throws Exception {
        System.out.println("insertLoanInterest####" + interestMap);
        System.out.println("insertLoanInteresttmp####" + interestMap);
        HashMap loanIntMap = new HashMap();
        long noofdays = 0;
        double amt = CommonUtil.convertObjToDouble(interestMap.get("AMT")).doubleValue();
        noofdays = DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(interestMap.get("START_DT"))),
                DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(interestMap.get("END_DT"))));
        System.out.println("noof after  days####" + noofdays);
        //        if(noofdays==0)
        //            noofdays=1;
        //        else
        noofdays += 1;
        System.out.println("noof after  days####" + noofdays);
        if (amt < 0) {
            amt *= -1;
        }
        String stringDayend = CommonUtil.convertObjToStr(interestMap.get("START_DT"));
        Date dayenddt = DateUtil.getDateMMDDYYYY(stringDayend);
        String enddt = CommonUtil.convertObjToStr(interestMap.get("END_DT"));
        Date nextdt = DateUtil.getDateMMDDYYYY(enddt);

        if (CommonUtil.convertObjToStr(interestMap.get("VALUEDATE")).equals("VALUEDATE_DR")) {

            loanIntMap.put("REMARKS", "1");
            loanIntMap.put("VALIDATE_TYPE", "VALUEDATE_DR");
        } else if (CommonUtil.convertObjToStr(interestMap.get("VALUEDATE")).equals("VALUEDATE_CR")) {
            loanIntMap.put("REMARKS", "2");
            loanIntMap.put("VALIDATE_TYPE", "VALUEDATE_CR");
        } else if (CommonUtil.convertObjToStr(interestMap.get("PENALTY")).equals("PENALTY")) {
            loanIntMap.put("REMARKS", "3");
            loanIntMap.put("VALIDATE_TYPE", "PENALTY");
        } else {
            loanIntMap.put("REMARKS", "0");
            loanIntMap.put("VALIDATE_TYPE", "ROI");
        }
        if (CommonUtil.convertObjToStr(interestMap.get("ACCT_NUM")).length() > 0) {
            loanIntMap.put("ACT_NUM", interestMap.get("ACCT_NUM"));
        } else {
            loanIntMap.put("ACT_NUM", interestMap.get("ACT_NUM"));
        }
        loanIntMap.put("ACT_NUM", interestMap.get("ACT_NUM"));
        loanIntMap.put("FROM_DT", dayenddt);
        loanIntMap.put("TO_DATE", nextdt);
        loanIntMap.put("AMT", new Double(amt));
        loanIntMap.put("NO_OF_DAYS", new Long(noofdays));
        loanIntMap.put("TOT_PRODUCT", new Double(amt * noofdays));
        loanIntMap.put("INT_AMT", interestMap.get("INTEREST"));
        loanIntMap.put("PROD_ID", interestMap.get("PROD_ID"));
        loanIntMap.put("CUST_ID", interestMap.get("CUST_ID"));

        loanIntMap.put("INT_RATE", CommonUtil.convertObjToDouble(interestRate));
        System.out.println("loanIntMap#####@@@@" + loanIntMap);
        sqlMap.executeUpdate("insertLoanInterest", loanIntMap);
    }

    private double amountRouding(HashMap prodLevelWhereMap) throws Exception {

        HashMap prodLevelValues = getProductLevelValues(CommonUtil.convertObjToStr(prodLevelWhereMap.get("PROD_ID")));
        String roundingValue = null; //interestBean.getRoundingFactor();
        String roundingMode = null;//interestBean.getRoundingType();
        System.out.println("prodLevelValues 111=======" + prodLevelValues);
        if (prodLevelValues.containsKey("ROUNDING_TYPE")) {
            roundingMode = (CommonUtil.convertObjToStr(prodLevelValues.get("ROUNDING_TYPE")));
        }
        double amount = CommonUtil.convertObjToDouble(prodLevelValues.get("")).doubleValue();
        if (roundingValue != null && roundingValue.length() != 0
                && roundingMode != null && roundingMode.length() != 0) {
            try {
                DecimalFormat d = new DecimalFormat();
                d.setMaximumFractionDigits(2);
                d.setDecimalSeparatorAlwaysShown(true);
                String str = d.parse(d.format(amount)).toString();
                amount = Double.parseDouble(str);
                amount = amount * 100;
                long principal = (long) amount;
                long roundingFactor = 1;
                Rounding rd = new Rounding();
                if (roundingValue.equals(InterestCalculationConstants.ROUNDING_VALUE_5_PAISE)) {
                    roundingFactor = 5;
                } else if (roundingValue.equals(InterestCalculationConstants.ROUNDING_VALUE_10_PAISE)) {
                    roundingFactor = 10;
                } else if (roundingValue.equals(InterestCalculationConstants.ROUNDING_VALUE_25_PAISE)) {
                    roundingFactor = 25;
                } else if (roundingValue.equals(InterestCalculationConstants.ROUNDING_VALUE_50_PAISE)) {
                    roundingFactor = 50;
                } else if (roundingValue.equals(InterestCalculationConstants.ROUNDING_VALUE_1_RUPEE)) {
                    roundingFactor = 100;
                } else if (roundingValue.equals(InterestCalculationConstants.ROUNDING_VALUE_5_RUPEES)) {
                    roundingFactor = 5 * 100;
                } else if (roundingValue.equals(InterestCalculationConstants.ROUNDING_VALUE_10_RUPEES)) {
                    roundingFactor = 10 * 100;
                } else if (roundingValue.equals(InterestCalculationConstants.ROUNDING_VALUE_50_RUPEES)) {
                    roundingFactor = 50 * 100;
                } else if (roundingValue.equals(InterestCalculationConstants.ROUNDING_VALUE_100_RUPEES)) {
                    roundingFactor = 100 * 100;
                }
                if (roundingMode.lastIndexOf("_") != -1) {
                    roundingMode = roundingMode.substring(0, roundingMode.lastIndexOf("_"));
                }
                if (roundingMode.equals(InterestCalculationConstants.ROUNDING_NEAREST)) {
                    principal = rd.getNearest(principal, roundingFactor);
                } else if (roundingMode.equals(InterestCalculationConstants.ROUNDING_LOWER)) {
                    principal = rd.lower(principal, roundingFactor);
                } else if (roundingMode.equals(InterestCalculationConstants.ROUNDING_HIGHER)) {
                    principal = rd.higher(principal, roundingFactor);
                }
                amount = (double) principal / 100;

                d = null;
                rd = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return amount;
    }

    /**
     * INTEREST_DUE_KEPT_RECEIVABLE column is N then proced basing on available
     * balance if available balance is more than calculated interest then debit
     * advances and cedit interest recived on loan achead .if available balance
     * is no need not debit advances just keep it in ibal caloumn in
     * adv_trans_details table if available balance is partial compare to
     * interest then upto extent debit advances credit int on loan achead
     * remaining portion keep it in ibal for that pupose we need not to create
     * TOD its applicable for calendar frequency and only for advances not loans
     * *
     */
    void doTransactionBaseAVBalanceOD(HashMap hash, HashMap productRecord) throws Exception {

        System.out.println("hash Tranaction####" + hash + "productRecord ####" + productRecord);
        hash.put("ACCT_NUM", hash.get("ACT_NUM"));
        double amount = CommonUtil.convertObjToDouble(hash.get("AMOUNT")).doubleValue();
        double availableBalance = CommonUtil.convertObjToDouble(productRecord.get("AVAILABLE_BALANCE")).doubleValue();
        if (availableBalance <= 0.0) {
            insertAuthorizeLoanDebitInterestOD(hash);
        } else if (availableBalance >= amount) {
            doDebitCreditTransaction(hash);

            HashMap updateMap = (HashMap) hash.clone();
            updateMap.put("COMMAND", "UPDATE_MINUS");
            insertAuthorizeLoanDebitInterestOD(updateMap);
            updateMap.remove("COMMAND");
        } else if (availableBalance < amount) {
            HashMap updateMap = (HashMap) hash.clone();
            hash.put("AMOUNT", new Double(availableBalance));
            hash.put("INTEREST", new Double(availableBalance));
            doDebitCreditTransaction(hash);

            updateMap.put("COMMAND", "UPDATE_PLUS");
            insertAuthorizeLoanDebitInterestOD(updateMap);
            updateMap.remove("COMMAND");
        }
    }

    /**
     * In this method doing debit advances account credit intrest on loans
     * achead
     *
     */
    private void doDebitCreditTransaction(HashMap hash) throws Exception {

        HashMap loanDetails = null;
        HashMap hashMap = null;
        HashMap paidAmtMap = null;
        System.out.println("doOd Tranactiongggg####" + hash);
        //        sqlMap.executeUpdate("updateclearBal", hash);

        hash.put("IBAL", hash.get("AMOUNT"));
        double ibal = CommonUtil.convertObjToDouble(hash.get("AMOUNT")).doubleValue();
        List getachd = sqlMap.executeQueryForList("getInterestAndPenalIntActHead", hash);
        loanDetails = (HashMap) getachd.get(0);
        //        if(CommonUtil.convertObjToStr(hash.get("BEHAVES_LIKE")).equals("LOANS_AGAINST_DEPOSITS"))
        //            intRecivableAchd=CommonUtil.convertObjToStr(loanDetails.get("AC_DEBIT_INT"));
        //        else
        String intRecivedAchd = CommonUtil.convertObjToStr(loanDetails.get("AC_DEBIT_INT"));
        loanAchd = CommonUtil.convertObjToStr(loanDetails.get("ACCT_HEAD"));
        hash.put("LOAN_ACHD", loanAchd);
        hash.put("INT_RECIVABLE", intRecivedAchd);
        List lst = null;
        if (CommonUtil.convertObjToStr(hash.get("BEHAVES_LIKE")).equals("OD")) {
            lst = sqlMap.executeQueryForList("getIntDetailsAD", hash);
            hash.put("PROD_TYPE", "ADVANCES");
            doTransaction(hash);
        }
    }

    private void insertAuthorizeLoanDebitInterestOD(HashMap authorizeMap) throws Exception {
        System.out.println("insertauthorizeloandebit####" + authorizeMap);
        HashMap inputMap = new HashMap();
        //        authorizeMap.put("ACT_NUM",authorizeMap.get("ACCOUNTNO"));
//        if(authorizeMap.containsKey("TRN_CODE") && authorizeMap.get("TRN_CODE")!=null && (!authorizeMap.get("TRN_CODE").equals("CLEARING_BOUNCED"))){
        List maxLoanTrans = sqlMap.executeQueryForList("getIntDetailsAD", authorizeMap);
        HashMap maxLoanTransmap = null;
        int trans_slno = 1;
        if (maxLoanTrans != null && maxLoanTrans.size() > 0) {
            maxLoanTransmap = (HashMap) maxLoanTrans.get(maxLoanTrans.size() - 1);
            trans_slno = CommonUtil.convertObjToInt(maxLoanTransmap.get("TRANS_SLNO"));
            adv_uniqKey = trans_slno;
            if (authorizeMap.containsKey("COMMAND") && CommonUtil.convertObjToStr(authorizeMap.get("COMMAND")).length() > 0) {
            } else {
                trans_slno++;
                adv_uniqKey = trans_slno;
            }
        }

        System.out.println("maxloantransMap##########" + maxLoanTransmap);
        if (maxLoanTransmap != null) {
            double amount = CommonUtil.convertObjToDouble(authorizeMap.get("AMOUNT")).doubleValue();
            if (amount < 0) {
                amount *= (-1);
            }
            double ibal = CommonUtil.convertObjToDouble(maxLoanTransmap.get("IBAL")).doubleValue();
            //            authorizeMap.putAll(maxLoanTransmap);
            inputMap.put("PRINCIPAL", CommonUtil.convertObjToDouble(maxLoanTransmap.get("PRINCIPLE")));//String.valueOf(new Double(principle)));
            inputMap.put("PRINCIPLE", CommonUtil.convertObjToDouble(maxLoanTransmap.get("PRINCIPLE")));
            inputMap.put("ACCOUNTNO", CommonUtil.convertObjToStr(authorizeMap.get("ACCOUNTNO")));

            inputMap.put("TODAY_DT", currDt);
            System.out.println("total_Penal...." + total_Penal);
            /// if(total_Penal>0)
            inputMap.put("PBAL", CommonUtil.convertObjToDouble(maxLoanTransmap.get("PBAL")));

            inputMap.put("INTEREST", CommonUtil.convertObjToDouble(authorizeMap.get("AMOUNT")));
            if (authorizeMap.containsKey("COMMAND") && CommonUtil.convertObjToStr(authorizeMap.get("COMMAND")).equals("UPDATE_MINUS")) {
                amount = CommonUtil.convertObjToDouble(maxLoanTransmap.get("INTEREST")).doubleValue();
                inputMap.put("IBAL", CommonUtil.convertObjToDouble(new Double(ibal - amount)));
            } else if (authorizeMap.containsKey("COMMAND") && CommonUtil.convertObjToStr(authorizeMap.get("COMMAND")).equals("UPDATE_PLUS")) {
                double debitedInterest = CommonUtil.convertObjToDouble(maxLoanTransmap.get("INTEREST")).doubleValue();
                inputMap.put("INTEREST", CommonUtil.convertObjToDouble(maxLoanTransmap.get("INTEREST")));
                amount -= debitedInterest;
                inputMap.put("IBAL", CommonUtil.convertObjToDouble(new Double(ibal + amount)));
            } else {
                inputMap.put("IBAL", CommonUtil.convertObjToDouble(new Double(ibal + amount)));
            }

            inputMap.put("PENAL", CommonUtil.convertObjToDouble(maxLoanTransmap.get("PENAL")));
            inputMap.put("PIBAL", CommonUtil.convertObjToDouble(maxLoanTransmap.get("PIBAL")));
            inputMap.put("EBAL", CommonUtil.convertObjToDouble(maxLoanTransmap.get("EBAL")));
            inputMap.put("EXPENSE", CommonUtil.convertObjToDouble(maxLoanTransmap.get("EXPENSE")));
            inputMap.put("BRANCH_CODE", _branchCode);
            inputMap.put("TRANSTYPE", "DEBIT");
            inputMap.put("PROD_ID", "");
            inputMap.put("NPA_INTEREST", new Double(0));
            inputMap.put("NPA_INT_BAL", new Double(0));
            inputMap.put("NPA_PENAL", new Double(0));
            inputMap.put("NPA_PENAL_BAL",new Double(0));
            inputMap.put("EXCESS_AMT", new Double(0));
            inputMap.put("AUTHORIZE_STATUS", "AUTHORIZED");
            inputMap.put("TRN_CODE", new String("DI"));
            inputMap.put("PRINCIPAL_AMOUNT", String.valueOf(inputMap.get("PRINCIPLE")));
            inputMap.put("INTEREST_AMOUNT", String.valueOf(inputMap.get("INTEREST")));
            inputMap.put("PENUL_INTEREST_AMOUNT", String.valueOf(inputMap.get("PENAL")));
            inputMap.put("EFFECTIVE_DT", null);
            inputMap.put("TRANS_SLNO", new Long(trans_slno));
            adv_uniqKey = new Long(trans_slno);
            inputMap.put("TRANS_MODE", "TRANSFER");
            inputMap.put("PARTICULARS", "Debit Interest Upto" + DateUtil.getStringDate((Date) authorizeMap.get("LAST_CALC_DT")));
            inputMap.put("LAST_CALC_DT",CommonUtil.getProperDate(currDt,DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(authorizeMap.get("LAST_CALC_DT")))));
            inputMap.put("TRANS_ID", "");
            inputMap.put("EXCESS_AMT", new Double(0));
            inputMap.put("POSTAGE_CHARGE", new Double(0));
            inputMap.put("POSTAGE_CHARGE_BAL", new Double(0));
            inputMap.put("ARBITARY_CHARGE", new Double(0));
            inputMap.put("ARBITARY_CHARGE_BAL", new Double(0));
            inputMap.put("LEGAL_CHARGE", new Double(0));
            inputMap.put("LEGAL_CHARGE_BAL", new Double(0));
            inputMap.put("INSURANCE_CHARGE", new Double(0));
            inputMap.put("INSURANCE_CHARGE_BAL", new Double(0));
            inputMap.put("EXE_DEGREE", new Double(0));
            inputMap.put("EXE_DEGREE_BAL", new Double(0));
            inputMap.put("MISC_CHARGES", new Double(0));
            inputMap.put("MISC_CHARGES_BAL", new Double(0));
            if (authorizeMap.containsKey("COMMAND") && CommonUtil.convertObjToStr(authorizeMap.get("COMMAND")).length() > 0) {
                sqlMap.executeUpdate("updateAuthorizeAdvTransDetails", inputMap);
            } else {
                sqlMap.executeUpdate("insertAuthorizeAdvTransDetails", inputMap);
            }
            sqlMap.executeUpdate("updateclearBal", inputMap);
            inputMap.put("AMOUNT", total_Penal);
            sqlMap.executeUpdate("updateadvPbal", inputMap);
        }
    }

    void doTransactionOD(HashMap hash, HashMap productRecord) throws Exception {
        HashMap loanDetails = null;
        HashMap hashMap = null;
        HashMap paidAmtMap = null;
        System.out.println("doOd Tranaction####" + hash);
        System.out.println("####productRecord###" + productRecord);
        //        sqlMap.executeUpdate("updateclearBal", hash);

        hash.put("IBAL", hash.get("AMOUNT"));
        double ibal = CommonUtil.convertObjToDouble(hash.get("AMOUNT")).doubleValue();
        List getachd = sqlMap.executeQueryForList("getInterestAndPenalIntActHead", hash);
        loanDetails = (HashMap) getachd.get(0);
        //        if(CommonUtil.convertObjToStr(hash.get("BEHAVES_LIKE")).equals("LOANS_AGAINST_DEPOSITS"))
        //            intRecivableAchd=CommonUtil.convertObjToStr(loanDetails.get("AC_DEBIT_INT"));
        //        else

        if (productRecord.containsKey("INTEREST_DUE_KEPT_RECEIVABLE") && CommonUtil.convertObjToStr(productRecord.get("INTEREST_DUE_KEPT_RECEIVABLE")).equals("N")) {
            intRecivableAchd = CommonUtil.convertObjToStr(loanDetails.get("AC_DEBIT_INT"));
        } else {
            intRecivableAchd = CommonUtil.convertObjToStr(loanDetails.get("INT_PAYABLE_ACHD"));
        }
        loanAchd = CommonUtil.convertObjToStr(loanDetails.get("ACCT_HEAD"));

        hash.put("PENAL_INT_OD", CommonUtil.convertObjToStr(loanDetails.get("PENAL_INT")));
        hash.put("LOAN_ACHD", loanAchd);
        hash.put("INT_RECIVABLE", intRecivableAchd);
        List lst = null;
        if (CommonUtil.convertObjToStr(hash.get("BEHAVES_LIKE")).equals("OD")) {
            lst = sqlMap.executeQueryForList("getIntDetailsAD", hash);
            hash.put("PROD_TYPE", "ADVANCES");
            if (productRecord.containsKey("PENALTY")) {
                hash.put("DEBIT_LOAN_TYPE", "DPI");
            }
            ////ADV STRATSSS 

            double intest1 = 0.0;
            double interst2 = 0.0;

            HashMap amap = new HashMap();
            amap.put("ACTNUM", hash.get("ACCOUNTNO").toString());
            // System.out.println("");
            amap.put("ASONDT", hash.get("TO_DATE"));

            ///aaaaaaaaaaaaa
            System.out.println("amap..." + amap);
            List interstList = sqlMap.executeQueryForList("getInterestAdv", amap);
            System.out.println("interstList..." + interstList);
            if (interstList != null && interstList.size() > 0) {
                HashMap intertmap = new HashMap();
                intertmap = (HashMap) interstList.get(0);
                if (intertmap != null && intertmap.containsKey("INTEREST") && intertmap.get("INTEREST") != null) {
                    hash.put("AMOUNT", intertmap.get("INTEREST").toString());
                    hash.put("INTEREST", intertmap.get("INTEREST").toString());

                    hash.put("IBAL", intertmap.get("INTEREST").toString());
                    if (!intertmap.get("INTEREST").toString().equals("")) {
                        intest1 = Double.parseDouble(intertmap.get("INTEREST").toString());
                    }
                }

            }
            System.out.println("moosiiid hashh" + hash);
            List penalIntestList = sqlMap.executeQueryForList("getPenalInterestAdv", amap);
            System.out.println("penalIntestList..." + penalIntestList);
            if (penalIntestList != null && penalIntestList.size() > 0) {
                HashMap intertmap = new HashMap();
                intertmap = (HashMap) penalIntestList.get(0);
                if (intertmap != null && intertmap.containsKey("INTEREST") && intertmap.get("INTEREST") != null) {
                    hash.put("AMOUNT", intertmap.get("INTEREST").toString());
                    hash.put("TOT_PENAL_INT", intertmap.get("INTEREST").toString());

                    // hash.put("IBAL",intertmap.get("INTEREST").toString());
                    if (!intertmap.get("INTEREST").toString().equals("")) {
                        interst2 = Double.parseDouble(intertmap.get("INTEREST").toString());
                    }

                }

            }
            hash.put("AMOUNT", interst2 + intest1);
            total_Penal = interst2 + intest1;
            HashMap inpt = new HashMap();
            inpt.put("TODAY_DT", currDt);
            inpt.put("ACCOUNTNO", hash.get("ACCOUNTNO").toString());
            inpt.put("AMOUNT", interst2 + intest1);
            inpt.put("TRANS_SLNO", adv_uniqKey);

            System.out.println("inpt....." + inpt);

            sqlMap.executeUpdate("updateadvPbal", inpt);

            System.out.println("total_Penal,,,," + total_Penal);
            System.out.println("moooddggg" + hash);

            //// ADV ENDSS
            doTransaction(hash);
            if (lst != null && lst.size() > 0 && CommonUtil.convertObjToStr(hash.get("BEHAVES_LIKE")).equals("OD")) {
                System.out.println("adv_details###" + lst);
                lst = sqlMap.executeQueryForList("getIntDetailsAD", hash);
                HashMap adv_hash = (HashMap) lst.get(0);
                paidAmtMap = insertExcesscredit(hash, adv_hash);
                System.out.println("paidamtmap######" + paidAmtMap);
                hash.putAll(adv_hash);
                System.out.println("finalmap####" + hash);
                //                sqlMap.executeUpdate("updateIntDetailsAD", hash);
            }
        } else {
            hash.put("PROD_TYPE", "LOANS");
            lst = sqlMap.executeQueryForList("getIntDetails", hash);
            hashMap = (HashMap) lst.get(0);
            hash.put("TOT_INT", hash.get("INTEREST"));
            hash.put("PREVAMT", hashMap.get("PBAL"));
            hash.put("END_DT", hash.get("END_DT"));
            batchProcessDebitInt(hash);
            lst = sqlMap.executeQueryForList("getIntDetails", hash);
            hashMap = (HashMap) lst.get(0);
            System.out.println("hashltd#####%%%" + hash);
            paidAmtMap = insertExcesscredit(hash, hashMap);
            System.out.println("paidamtmap######" + paidAmtMap);
        }
        //if loans against termdeposit only immediatly debit interest receivable to int received
        if ((hash.containsKey("BEHAVES_LIKE") && CommonUtil.convertObjToStr(hash.get("BEHAVES_LIKE")).equals("LOANS_AGAINST_DEPOSITS"))
                || (paidAmtMap != null && paidAmtMap.size() > 0)) {
            HashMap txMap = new HashMap();
            ArrayList transferList = new ArrayList();
            TxTransferTO transfer = new TxTransferTO();
            txMap.put(TransferTrans.DR_AC_HD, loanDetails.get("INT_PAYABLE_ACHD"));
            txMap.put(TransferTrans.DR_BRANCH, _branchID);
            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
            txMap.put(TransferTrans.CR_AC_HD, loanDetails.get("AC_DEBIT_INT"));
            txMap.put(TransferTrans.CR_BRANCH, _branchID);
            txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
            txMap.put(TransferTrans.CURRENCY, "INR");
            if (chargesMap != null && chargesMap.containsKey("CHARGESUI")) {
                txMap.put(TransferTrans.DR_INSTRUMENT_2, "CHARGESUI");
            }
            TransferTrans trans = new TransferTrans();
            trans.setInitiatedBranch(_branchID);
            trans.setTransMode(CommonConstants.TX_TRANSFER);

            //                double ibal=CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
            if (hash.containsKey("BEHAVES_LIKE") && CommonUtil.convertObjToStr(hash.get("BEHAVES_LIKE")).equals("LOANS_AGAINST_DEPOSITS")) {
                if (ibal > 0) {
                    txMap.put("PARTICULARS", "InterestUpto " + DateUtil.getStringDate(chekdateLast));
                    transferList.add(trans.getDebitTransferTO(txMap, ibal));
                    txMap.put("PARTICULARS", "" + hash.get("ACCOUNTNO"));
                    transferList.add(trans.getCreditTransferTO(txMap, ibal));
                    trans.setForDebitInt(true);
                    if (hash.containsKey("DEBIT_LOAN_TYPE") && hash.get("DEBIT_LOAN_TYPE") != null)//debit loan type key passed from dpi only
                    {
                        trans.setLoanDebitInt("DPI");
                    } else {
                        trans.setLoanDebitInt("DI");//default
                    }
                    trans.doDebitCredit(transferList, _branchCode, true);
                }
            } else//receivable to recied
            if ((CommonUtil.convertObjToDouble(paidAmtMap.get("INTEREST")).doubleValue()) > 0) {
                txMap.put("PARTICULARS", "InterestUpto " + DateUtil.getStringDate(chekdateLast));
                transferList.add(trans.getDebitTransferTO(txMap, CommonUtil.convertObjToDouble(paidAmtMap.get("INTEREST")).doubleValue()));
                txMap.put("PARTICULARS", "" + hash.get("ACCOUNTNO"));
                transferList.add(trans.getCreditTransferTO(txMap, CommonUtil.convertObjToDouble(paidAmtMap.get("INTEREST")).doubleValue()));
                trans.setForDebitInt(true);
                //                        if(hash.containsKey("DEBIT_LOAN_TYPE") && hash.get("DEBIT_LOAN_TYPE")!=null)//debit loan type key passed from dpi only
                //                            trans.setLoanDebitInt("DPI");
                //                        else
                //                            trans.setLoanDebitInt("DI");//default
                trans.doDebitCredit(transferList, _branchCode, true);
                transferList = new ArrayList();
                trans = new TransferTrans();
                trans.setInitiatedBranch(_branchID);
                trans.setTransMode(CommonConstants.TX_TRANSFER);
                //receivable to recied
                if ((CommonUtil.convertObjToDouble(paidAmtMap.get("PENAL")).doubleValue()) > 0) {
                    txMap.put("PARTICULARS", "InterestUpto " + DateUtil.getStringDate(chekdateLast));
                    txMap.put(TransferTrans.CR_AC_HD, loanDetails.get("PENAL_INT"));
                    transferList.add(trans.getDebitTransferTO(txMap, CommonUtil.convertObjToDouble(paidAmtMap.get("PENAL")).doubleValue()));
                    txMap.put("PARTICULARS", "" + hash.get("ACCOUNTNO"));
                    transferList.add(trans.getCreditTransferTO(txMap, CommonUtil.convertObjToDouble(paidAmtMap.get("PENAL")).doubleValue()));
                    trans.setForDebitInt(true);
                    //                        if(hash.containsKey("DEBIT_LOAN_TYPE") && hash.get("DEBIT_LOAN_TYPE")!=null)//debit loan type key passed from dpi only
                    //                            trans.setLoanDebitInt("DPI");
                    //                        else
                    //                            trans.setLoanDebitInt("DI");//default
                    trans.doDebitCredit(transferList, _branchCode, true);
                }
            }
        }
        hashMap = null;
        hash = null;

        //        if(lst !=null && lst.size()>0 && CommonUtil.convertObjToStr(hash.get("BEHAVES_LIKE")).equals("OD")){
        //            System.out.println("adv_details###"+lst);
        //            HashMap adv_hash=(HashMap)lst.get(0);
        //            hash.putAll(adv_hash);testing
        //            System.out.println("finalmap####"+hash);
        //            sqlMap.executeUpdate("updateIntDetailsAD", hash);
        //        }
    }
    //for interest  & penal interest calcaulation TL only

    private HashMap interestCalculationTL(HashMap hash) throws Exception {
        HashMap map = null;
        System.out.println("hash######" + hash);
        HashMap penalMap = new HashMap();
        //    try{
        //        List prodId_lst=sqlMap.executeQueryForList("GETPRODUCTID",hash);
        //        System.out.println("collect of all product id###"+prodId_lst);
        //        if(prodId_lst.size()>0){
        //            for(int i=0;i<prodId_lst.size();i++){
        //                map=(HashMap)prodId_lst.get(i);
        behaves_like = CommonUtil.convertObjToStr(hash.get("BEHAVES_LIKE"));
        ////                List lstAppdt=sqlMap.executeQueryForList("GETLASTCALCDT",map);
        ////                HashMap maplast=(HashMap)lstAppdt.get(0);
        ////                //                map.put("BRANCH_ID",hash.get("BRANCH_CODE"));
        ////                int frequency=CommonUtil.convertObjToInt(maplast.get("DEBITINT_CALC_FREQ"));
        ////                //                if(frequency==30){
        ////                //                    frequency=1;
        ////                //                }
        ////                String lastappdate=CommonUtil.convertObjToStr(maplast.get("LAST_INTCALC_DTDEBIT"));
        ////                Date lastApDate=DateUtil.getDateMMDDYYYY(lastappdate);
        ////                Date lastAppDate=DateUtil.getDateMMDDYYYY(lastappdate);
        ////
        ////                lastApDate= DateUtil.addDaysProperFormat(lastApDate, frequency);

        Date curr_dt = (Date) currDt.clone();

        curr_dt = DateUtil.getDateWithoutMinitues(curr_dt);

        hash.put("CURR_DATE", currDt);
        System.out.println("curr_dt####" + curr_dt);

        //                Date dt =holiydaychecking(hash);
        //                 hash.put("CURR_DATE",lastApDate);
        System.out.println(chargesMap + "checkThisCDate####after" + checkThisCDate);
        if ((chargesMap != null && chargesMap.containsKey("CHARGESUI")) || hash.containsKey("LOAN_ACCOUNT_CLOSING") || (DateUtil.dateDiff(checkThisCDate, curr_dt) == 0)) {
            //FOR GET ACHD
            HashMap loanDetails = null;
            List getachd = sqlMap.executeQueryForList("getInterestAndPenalIntActHead", hash);
            loanDetails = (HashMap) getachd.get(0);
            intRecivableAchd = CommonUtil.convertObjToStr(loanDetails.get("INT_PAYABLE_ACHD"));
            loanAchd = CommonUtil.convertObjToStr(loanDetails.get("ACCT_HEAD"));
            hash.put("FREQ", new Long(freq));// freq
            hash.put("BATCH_PROCESS", "BATCH");
            //                        map.put("START_DATE",lastAppDate);

            loanDetails = new HashMap();
            loanDetails = calculateInterestNonBatchAndbatch(hash);

            if (loanDetails != null) {
                penalMap.put("TOT_PENAL_INT", loanDetails.get("TOT_PENAL_INT"));
                System.out.println("loanInterestDetails#######" + loanDetails);
            }
        }
        //                }
        //            }
        //        }
        return penalMap;
    }

    private double roundOffLoanInterest(HashMap prodLevelWhereMap) throws Exception {
        Rounding rd = new Rounding();
        HashMap prodLevelValues = getProductLevelValues(CommonUtil.convertObjToStr(prodLevelWhereMap.get("PROD_ID")));
        String roundingType = null;
        double interest = 0;
        interest = CommonUtil.convertObjToDouble(prodLevelWhereMap.get("TOT_INTEREST")).doubleValue();
        if (interest == 0) {
            interest = CommonUtil.convertObjToDouble(prodLevelWhereMap.get("TOT_PENAL_INT")).doubleValue();
        }
        if (prodLevelValues.containsKey("DEBIT_INT_ROUNDOFF") && prodLevelValues.get("DEBIT_INT_ROUNDOFF") != null) {
            roundingType = (CommonUtil.convertObjToStr(prodLevelValues.get("DEBIT_INT_ROUNDOFF")));
        } else {
            roundingType = ROUNDING_NEAREST;//InterestCalculationConstants.ROUNDING_HIGHER;
        }
        System.out.println(interest + "prodLevelValues#####" + prodLevelValues);
        String floatPrecisionStr = "2";
        if (roundingType != null && roundingType.length() > 0
                && floatPrecisionStr != null && floatPrecisionStr.length() > 0) {
            int floatPrecision = Integer.parseInt(floatPrecisionStr);
            //            interest= interest*Math.pow(10, floatPrecision+1);
            double number = interest;
            System.out.println("!@!@ INSIDE IF ROUNDING..." + roundingType);
            //The following block changed for Polpully Bank
//            if(roundingType.equals(ROUNDING_NEAREST))
//                number =rd.getNearestHigher(number,1);
//            else if (roundingType.equals(NEAREST_TENS))
//                number =rd.getNearestHigher(number,10);
//            else if (roundingType.equals(NEAREST_HUNDREDS))
//                number =rd.getNearestHigher(number,100);
            if (roundingType.equals(ROUNDING_NEAREST)) {
                number = (long) (number + 0.5); //rd.getNearest(number,1);
            } else if (roundingType.equals(NEAREST_TENS)) {
                number = rd.getNearest(number, 10);
            } else if (roundingType.equals(NEAREST_HUNDREDS)) {
                number = rd.getNearest(number, 100);
            }
            interest = number;///Math.pow(10,floatPrecision+1);
        }
        rd = null;
        System.out.println("interest@@@####" + interest);
        return interest;
    }

    void batchProcessDebitInt(HashMap loanDetails) throws Exception {
        System.out.println("loanDetails #####" + loanDetails);
        HashMap interestMap = new HashMap();
        double prevAmt = CommonUtil.convertObjToDouble(loanDetails.get("PREVAMT")).doubleValue();
        double totInterest = CommonUtil.convertObjToDouble(loanDetails.get("TOT_INT")).doubleValue();
        if (loanDetails.containsKey("ACCT_NUM")) {
            interestMap.put("ACCOUNTNO", loanDetails.get("ACCT_NUM"));
        } else {
            interestMap.put("ACCOUNTNO", loanDetails.get("ACT_NUM"));
        }
        if (loanDetails.containsKey("DEBIT_LOAN_TYPE") && loanDetails.get("DEBIT_LOAN_TYPE") != null) {
            interestMap.put("DEBIT_LOAN_TYPE", loanDetails.get("DEBIT_LOAN_TYPE"));
        }
        interestMap.put("BRANCH_CODE", _branchID);
        if (behaves_like != null && behaves_like.equals("LOANS_AGAINST_DEPOSITS")) {
            prevAmt += totInterest;  //intAmt
        }        //        interestMap.put("PBAL",loanDetails.get("PREVAMT"));       //new Double(prevAmt));
        interestMap.put("INTEREST", loanDetails.get("INTEREST"));//new Double(0));    //new Double(totInterest));   //intAmt
        interestMap.put("IBAL", new Double(0));
        interestMap.put("PENAL", loanDetails.get("TOT_PENAL_INT"));    //new Double(totInterest));   //intAmt
        interestMap.put("PIBAL", loanDetails.get("TOT_PENAL_INT")); //new Double(totInterest));       //intAmt
        interestMap.put("TRANS_ID", "");
        interestMap.put("TRN_DATE", loanDetails.get("END_DT"));
        interestMap.put("TODAY_DT", chekdateLast); //endDate);
        interestMap.put("PRINCIPLE", new Long(0));
        interestMap.put("SEC_CODE", loanDetails.get("PROD_ID"));
        interestMap.put("PROD_ID", loanDetails.get("PROD_ID"));

        interestMap.put("AMOUNT", interestMap.get("IBAL"));
        interestMap.put("LAST_CALC_DT", chekdateLast);
        System.out.println("doTransaction####" + interestMap);//loanDetails.get("END_DT"));     //endDate);
        //        sqlMap.executeUpdate("updateclearBal", interestMap);
        //                                    sqlMap.executeUpdate("updateOtherBalancesTL", interestMap);
        interestMap.put("TOT_PENAL_INT", loanDetails.get("TOT_PENAL_INT"));
        interestMap.put("INT_RECIVABLE", intRecivableAchd);
        interestMap.put("LOAN_ACHD", loanAchd);
        interestMap.put("PROD_TYPE", "LOANS");
        System.out.println("doTransaction####" + interestMap);

        doTransaction(interestMap);
        //for testing
        //                                    break;

        //                                }
        //            }
        //            tempMap = null;
        //            getPrinInt = null;
        //            loanDetails = null;
    }

    /**
     * *** this for out database only List
     * getSingleRecord=sqlMap.executeQueryForList("GetSumProductAD",loanDetails);
     * if(getSingleRecord.size()>0){ HashMap
     * sumMap=(HashMap)getSingleRecord.get(0); String
     * product=CommonUtil.convertObjToStr(sumMap.get("PRODUCT"));
     * System.out.println("productTotal"+product); double
     * productTotal=Double.parseDouble(product); HashMap interest=new HashMap();
     * interest.put("ADVANCES","OD"); List
     * interestRate=sqlMap.executeQueryForList("getAdvancesInterestOD",interest);
     * if(interestRate.size()>0){ sqlMap.startTransaction(); HashMap
     * getIntrest=(HashMap)interestRate.get(0); long
     * intRate=CommonUtil.convertObjToLong(getIntrest.get("APPL_INTEREST"));
     * System.out.println("veryimportant INT RATE"+intRate); double
     * monthInterest=(productTotal*intRate)/36500;
     * System.out.println("veryimportant"+(monthInterest=monthInterest*(-1)));
     * HashMap insertRecord=new HashMap(); //
     * insertRecord.put("ACT_NUM",singleRecord.get("ACT_NUM")); comment by 29
     * aug // insertRecord.put("PROD_ID", interest.get("ADVANCES")); //
     * insertRecord.put("FROM_DATE",singleRecord.get("START_DATE")); //
     * insertRecord.put("TO_DATE",singleRecord.get("CURR_DATE")); //
     * insertRecord.put("INTEREST",new Double(monthInterest));
     * sqlMap.executeUpdate("insertMonthInterestOD", insertRecord);
     * System.out.println("execution successfully"); sqlMap.commitTransaction();
     * } }
     *
     */
    //        }
    //    getActNum = null;
    //                                sqlMap.executeUpdate("updatelastintcalcdate", map);
    //                    }
    //
    //
    //                }
    //
    //            }
    //        }
    //            }catch(Exception e){
    //
    //            }
    //    }
    /*
     hashAmount=(HashMap)Lst.get(1);
     String clearAmount=CommonUtil.convertObjToStr(hashAmount.get("CLEAR_BALANCE"));
     long Amt=Long.parseLong(clearAmount);
     String Amount=CommonUtil.convertObjToStr(map.get("AMOUNT"));
     long amount=Long.parseLong(Amount);
     //                if(prodType.equals("DEBIT"))
     //                {
     //                    amount+=Amt;
     //
     //                }
     //                else
     //                {
     //                    amount-=Amt;
     //                }
     }
     for(int i = 0 , j = lst.size(); i < j; i++){
     try{
     HashMap map=new HashMap();
     map = (HashMap)lst.get(i) ;
     map.put("BRANCH_CODE", getHeader().getBranchID());
     map.put("CURR_DATE",ServerUtil.getCurrentDate(getHeader().getBranchID()));
     sqlMap.startTransaction();
    
     // Day Begin Insertion
    
     sqlMap.executeUpdate("insertInterestCalculationTask", map);
    
     sqlMap.commitTransaction();
     }catch(Exception e){
     sqlMap.rollbackTransaction();
     e.printStackTrace();
     status.setStatus(BatchConstants.ERROR);*/
    //    private void getInterestApplication(HashMap hash){
    //        HashMap map=new HashMap();
    //        HashMap intApp=null;
    //         try{
    //            List prodId_lst=sqlMap.executeQueryForList("GETPRODUCTID",hash);
    //            if(prodId_lst.size()>0){
    //                for(int i=0;i<prodId_lst.size();i++){
    //                    map=(HashMap)prodId_lst.get(i);
    //                    List lstAppdt=sqlMap.executeQueryForList("GETLASTCALCDT",map);
    //                    HashMap maplast=(HashMap)lstAppdt.get(0);
    //                    int frequency=CommonUtil.convertObjToInt(maplast.get("DEBITINT_APPL_FREQ"))/30;
    //                    if(frequency==30){
    //                        frequency=1;
    //                    }
    //                    String lastappdate=CommonUtil.convertObjToStr("LAST_INTAPPL_DTDEBIT");
    //                    Date lastApDate=DateUtil.getDateMMDDYYYY(lastappdate);
    //                    int getMonth=lastApDate.getMonth();
    //                    for(int j=1;j<=frequency;j++){
    //
    //                        getMonth+=1;
    //                        getMonth+=frequency;
    //                        lastApDate.setMonth(getMonth);
    //                    }
    //                        Date curr_dt=ServerUtil.getCurrentDate(getHeader().getBranchID());
    //                        if(getMonth==(curr_dt.getMonth()+1)){
    //                            holiydaychecking(hash);
    //                            intApp=new HashMap();
    //                            intApp.put("LAST_APP_DT",lastApDate);
    //                            intApp.put("CURR_DATE",curr_dt);
    //                            if(checkThisCDate.equals(curr_dt)){
    //                                List intAppDt=sqlMap.executeQueryForList("getLoanInterstAppDate",intApp);
    //
    //                            }
    //
    //                        }
    //
    //
    //                }
    //            }
    //
    //        }catch(Exception e){
    //
    //        }
    //    }
    private HashMap calculateInterestNonBatchAndbatch(HashMap map) throws Exception {
        System.out.println("calculateinterest nonbatch####" + map);
        Date inst_dt = null;
        Date curr_dt = DateUtil.getDateWithoutMinitues(currDt);
        map.put("BRANCH_ID", _branchID);
        map.put("CURR_DT", curr_dt);
        Date penalWaiveDt = (Date) map.get("PENAL_WAIVE_DT");

        //        List getActNum=sqlMap.executeQueryForList("getAllLoanRecord",map);
        HashMap intcalcMap = new HashMap();
        //        if(getActNum.size()>0)
        //            for(int k=0;k<getActNum.size();k++){

        HashMap loanDetailsmap = new HashMap();

        //                loanDetailsmap=(HashMap)getActNum.get(k);
        System.out.println("loanDetailsmap####" + map);
        String last_int_cal_dt = CommonUtil.convertObjToStr(map.get("LAST_INT_CALC_DT"));
        Date lastIntCalDt = new Date();
        if (last_int_cal_dt.length() > 0) {
            lastIntCalDt = DateUtil.getDateMMDDYYYY(last_int_cal_dt);
            //                    lastIntCalDt=DateUtil.addDaysProperFormat(lastIntCalDt,1);
            Date tempDt = (Date) currDt.clone();
            tempDt.setDate(lastIntCalDt.getDate());
            tempDt.setMonth(lastIntCalDt.getMonth());
            tempDt.setYear(lastIntCalDt.getYear());
            map.put("START_DATE", tempDt);//lastIntCalDt);
        }
        try {
            interestList = null;
            interestList = getInterestDetails(map, map);
            map.put("ACT_NUM", map.get("ACCT_NUM"));
            map.put("START_DATE", map.get("START_DATE"));
            Date tempDt = (Date) currDt.clone();
            tempDt.setDate(chekdateLast.getDate());
            tempDt.setMonth(chekdateLast.getMonth());
            tempDt.setYear(chekdateLast.getYear());
            map.put("CURR_DATE", tempDt);//curr_dt
            map.put("PROD_ID", map.get("PROD_ID"));
            map.put("LAST_CALC_DT", tempDt);
            System.out.println("singlecustomer#####" + map);
            //FOR PENAL
            //            double paidPrinciple=0;
            //            double totPrinciple=0;
            //            List paidprincipal=sqlMap.executeQueryForList("getPrincipalforPenal",map);
            //            if(paidprincipal !=null && paidprincipal.size()>0) {
            //                HashMap paidPrincipalMap=(HashMap)paidprincipal.get(0);
            //                totPrinciple=CommonUtil.convertObjToDouble(paidPrincipalMap.get("PRINCIPLE")).doubleValue();
            //                paidPrinciple=CommonUtil.convertObjToDouble(paidPrincipalMap.get("PRINCIPLE")).doubleValue();
            //            List allInstallment=sqlMap.executeQueryForList("getAllLoanInstallment",map);

            ////////                    for(int i=0;i<allInstallment.size();i++) {
            ////////                   11     HashMap allInstallmentMap=(HashMap)allInstallment.get(i);
            ////////                        double instalAmt=CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue();
            ////////                        if(instalAmt<=totPrinciple) {
            ////////                            totPrinciple-=instalAmt;
            ////////                            //                        if(lst.size()==1){
            ////////                            inst_dt=new Date();
            ////////                            String in_date=CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
            ////////                            inst_dt=DateUtil.getDateMMDDYYYY(in_date);
            ////////                            //                        }
            ////////                        }
            ////////                        else{
            ////////                            inst_dt=new Date();
            ////////                            String in_date=CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
            ////////                            inst_dt=DateUtil.getDateMMDDYYYY(in_date);
            ////////                            totPrinciple=CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue()- totPrinciple;
            ////////                            break;
            ////////                        }
            ////////
            ////////                    }
            //            }
            //        inst_dt=DateUtil.getDateWithoutMinitues(inst_dt);
            inst_dt = lastIntCalDt;
            //        if(DateUtil.dateDiff(inst_dt,chekdateLast)>0){//curr_dt
            //END PENAL
            List getPrinInt = sqlMap.executeQueryForList("getPrincipalforCalInt", map);

            Date stDate = null;
            Date endDate = null;
            Date endDatepass = null;
            double Bal = 0;
            double amt = 0;
            double prevAmt = 0;
            double totAmt = 0;
            double product = 0;
            double totInterest = 0.0;
            double totPenalInt = 0;
            double prevBalPrinciple = 0;
            double totPayableBal = 0;
            boolean iscalculated = true;
            int t = 0;
            HashMap tempMap = null;
            HashMap totBalMap = null;
            HashMap totInterestmap = new HashMap();
            HashMap balprincipal = new HashMap();

            HashMap payblePrincipalMap = new HashMap();
            System.out.println("getprincipal int ####" + getPrinInt);
            balprincipal.putAll(map);

            //                     if(DateUtil.dateDiff(inst_dt,lastIntCalDt)>0)//want from start not paid
            //                          balprincipal.put("FROM_DT",DateUtil.addDaysProperFormat(lastIntCalDt,1));
            //                     else
            balprincipal.put("FROM_DT", inst_dt);//inst_dt map.get("ACCT_OPEN_DT")
            balprincipal.put("TO_DATE", chekdateLast);//curr_dt
            //            List payablePrincipal=sqlMap.executeQueryForList("getOverDueDetails",balprincipal);//  getPrincipalDueDetails
            List allInstallment = sqlMap.executeQueryForList("getPrincipalDueDetails", map);//getAllLoanInstallment
            List paidprincipal = sqlMap.executeQueryForList("getPrincipalforPenal", map);
            Date endDt = null;
            double remainBalance = 0;
            int gracePeriodInDays = 0;
            if (map.containsKey("GRACE_PERIOD_PENAL") && map.get("GRACE_PERIOD_PENAL") != null) {
                gracePeriodInDays = CommonUtil.convertObjToInt(map.get("GRACE_PERIOD_PENAL"));
            }
            if (map.containsKey("PENAL_APP_PRINCIPAL") && map.get("PENAL_APP_PRINCIPAL") != null && map.get("PENAL_APP_PRINCIPAL").equals("Y")) {
                if (allInstallment != null) //loan all installment
                {
                    for (int i = 0; i < allInstallment.size(); i++) {
                        HashMap allInstallmentMap = (HashMap) allInstallment.get(i);
                        System.out.println(lastIntCalDt + "getAllLoanInstallment" + i + "   " + allInstallmentMap);
                        amt = 0;

                        Date installmentDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT")));
                        System.out.println("lastIntCalDt === " + lastIntCalDt);
                        System.out.println("installmentDt === " + installmentDt);
                        System.out.println("DATEE DIFFF==== === " + DateUtil.dateDiff(lastIntCalDt, installmentDt));
                        if (DateUtil.dateDiff(lastIntCalDt, installmentDt) > 0) {
                            //babu commented on 18-07-2013 ID:8037
                           /* if(penalWaiveDt !=null && DateUtil.dateDiff(installmentDt,penalWaiveDt)>0)
                             {
                             endDt=DateUtil.addDaysProperFormat(penalWaiveDt,1);
                             }
                             else{*/

                            endDt = DateUtil.addDaysProperFormat(installmentDt, 0);
                            if (DateUtil.dateDiff(endDt, currDt) == 0) {
                                endDt = DateUtil.addDaysProperFormat(endDt, -1);
                            }
                            System.out.println("endDt -------- " + endDt);
                            // }
                        } else {
                            /* if(penalWaiveDt !=null && DateUtil.dateDiff(lastIntCalDt,penalWaiveDt)>0){
                             endDt=DateUtil.addDaysProperFormat(penalWaiveDt,1);
                             System.out.println("endDt --222------ "+endDt);
                             }
                             else{*/
                            endDt = DateUtil.addDaysProperFormat(lastIntCalDt, 1);
                            System.out.println("endDt ----2222---- " + endDt);
                            //}
                            System.out.println("endDt -----111--- " + endDt);
                        }

                        Date GraceDate = DateUtil.addDays(endDt, gracePeriodInDays);
                        if (gracePeriodInDays > 0 && DateUtil.dateDiff(currDt, GraceDate) >= 0) {
                            continue;
                        }
                        amt = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue();
                        //added by sreekrishnan for mantis 10212
                        if (CommonUtil.convertObjToStr(map.get("PROD_SUBSIDY")).equals("Y")) {
                            if (CommonUtil.convertObjToStr(map.get("LOAN_BALANCE")).equals("N")) {
                                if (CommonUtil.convertObjToStr(map.get("SUBSIDY_ALLOWED")).equals("Y")) {
                                    if (CommonUtil.convertObjToDouble(map.get("SUBSIDY_AMT")) > 0) {
                                        amt = amt - (CommonUtil.convertObjToDouble(map.get("SUBSIDY_AMT")) - CommonUtil.convertObjToDouble(map.get("SUBSIDY_ADJUSTED_AMT")));
                                    }
                                }
                            }
                        }
                        if (amt > remainBalance) {
                            amt -= remainBalance;
                        } else {
                            remainBalance -= amt;
                            amt = 0;
                        }
                        if (paidprincipal != null) {
                            for (int j = 0; j < paidprincipal.size(); j++) { //loan transdetails credit only
                                balprincipal = new HashMap();
                                balprincipal.putAll(map);
                                HashMap paidprincipalMap = (HashMap) paidprincipal.get(j);
                                Date trans_dt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paidprincipalMap.get("TRANS_DT")));
                                double paidPrincipal = CommonUtil.convertObjToDouble(paidprincipalMap.get("PRINCIPLE")).doubleValue();
                                if (DateUtil.dateDiff(installmentDt, trans_dt) > 0) {
                                    amt = amt;
                                } else {
                                    //                                amt=amt - paidPrincipal;
                                }
                                if (DateUtil.dateDiff(endDt, trans_dt) >= 0) {
                                    if (DateUtil.dateDiff(lastIntCalDt, installmentDt) > 0 && amt > 0) {
                                        //
                                        balprincipal.put("PRINCIPAL_BAL", new Double(amt));
                                        balprincipal.put("START_DT", endDt);
                                        if (DateUtil.dateDiff(endDt, DateUtil.addDaysProperFormat(trans_dt, -1)) > 0) {
                                            balprincipal.put("END_DT", DateUtil.addDaysProperFormat(trans_dt, -1));
                                        } else {
                                            balprincipal.put("END_DT", DateUtil.addDaysProperFormat(installmentDt, -1));
                                        }
                                        balprincipal.put(PENALTY, PENALTY);
                                        balprincipal.put("NO_ROUND_OFF_INT", "NO_ROUND_OFF_INT");
                                        Date st_dt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(balprincipal.get("START_DT")));
                                        if (DateUtil.dateDiff(st_dt, chekdateLast) >= 0) {
                                            totInterestmap = calculateInterest(balprincipal);
                                            totPenalInt += CommonUtil.convertObjToDouble(totInterestmap.get(INTEREST)).doubleValue();
                                            balprincipal.put(INTEREST, totInterestmap.get(INTEREST));
                                            if (balprincipal.containsKey("LOAN_ACCOUNT_CLOSING")) {
                                                System.out.println("sssssssssssRish..444444444444...");
                                                insertLoanInteresttmp(balprincipal);
                                            } else {
                                                insertLoanInterest(balprincipal);
                                            }
                                        }
                                        iscalculated = false;
                                        //

                                    }
                                }
                                if (DateUtil.dateDiff(endDt, trans_dt) >= 0 && iscalculated) {
                                    if (DateUtil.dateDiff(lastIntCalDt, trans_dt) > 0 && amt > 0) {
                                        //
                                        balprincipal.put("PRINCIPAL_BAL", new Double(amt));
                                        balprincipal.put("START_DT", endDt);
                                        balprincipal.put("END_DT", DateUtil.addDaysProperFormat(trans_dt, -1));
                                        balprincipal.put(PENALTY, PENALTY);
                                        balprincipal.put("NO_ROUND_OFF_INT", "NO_ROUND_OFF_INT");
                                        totInterestmap = calculateInterest(balprincipal);
                                        totPenalInt += CommonUtil.convertObjToDouble(totInterestmap.get(INTEREST)).doubleValue();
                                        balprincipal.put(INTEREST, totInterestmap.get(INTEREST));
                                        if (balprincipal.containsKey("LOAN_ACCOUNT_CLOSING")) {
                                            insertLoanInteresttmp(balprincipal);
                                        } else {
                                            insertLoanInterest(balprincipal);
                                        }

                                        //
                                    }
                                }
                                System.out.println("endDt:" + endDt);
                                System.out.println("trans_dt:" + trans_dt);
                                System.out.println("amt:" + amt);
                                System.out.println("paidPrincipal:" + paidPrincipal);
                                if (DateUtil.dateDiff(endDt, trans_dt) >= 0) {

                                    endDt = trans_dt;
                                }
                                amt = amt - paidPrincipal;
                                if (amt < 0) {
                                    remainBalance = (-amt);
                                }
                                System.out.println("j  " + j + "before remove paidprincipal" + paidprincipal);
                                paidprincipal.remove(j);
                                j = -1;
                                System.out.println("after remove paidprincipal" + paidprincipal);
                                iscalculated = true;
                            }
                        }

                        System.out.println("#$$@@@ endDt: " + endDt);
                        System.out.println("#$#$444 chekdateLast: " + chekdateLast);
                        System.out.println("#$#$444 amt: " + amt);
                        if (DateUtil.dateDiff(endDt, chekdateLast) >= 0 && amt > 0) {   //condition changed from if(DateUtil.dateDiff(endDt,chekdateLast)>0 && amt>0){
                            balprincipal = new HashMap();
                            remainBalance = 0;
                            balprincipal.putAll(map);
                            balprincipal.put("PRINCIPAL_BAL", new Double(amt));
                            balprincipal.put("START_DT", endDt);
                            balprincipal.put("END_DT", chekdateLast);
                            balprincipal.put(PENALTY, PENALTY);
                            balprincipal.put("NO_ROUND_OFF_INT", "NO_ROUND_OFF_INT");
                            System.out.println("calculateinterest3333" + balprincipal);
                            totInterestmap = calculateInterest(balprincipal);
                            totPenalInt += CommonUtil.convertObjToDouble(totInterestmap.get(INTEREST)).doubleValue();
                            balprincipal.put(INTEREST, totInterestmap.get(INTEREST));
                            if (balprincipal.containsKey("LOAN_ACCOUNT_CLOSING")) {
                                insertLoanInteresttmp(balprincipal);
                            } else {
                                insertLoanInterest(balprincipal);
                            }
                        }
                    }
                }
            }
            //for principal due  only  testing oubpose comment
            //////////////            if(payablePrincipal !=null && payablePrincipal.size()>0){
            //////////////                for(int j=0;j<payablePrincipal.size();j++){
            //////////////                    payblePrincipalMap=(HashMap)payablePrincipal.get(j);
            //////////////                    double payablePrincipalAmt=0;
            //////////////                    //                        payablePrincipalAmt=CommonUtil.convertObjToDouble(payblePrincipalMap.get("PRINCIPAL_AMT")).doubleValue();
            //////////////                    //                        totPayableBal= paidPrinciple - payablePrincipalAmt;
            //////////////                    //                        paidPrinciple= paidPrinciple - payablePrincipalAmt;
            //////////////                    //                        if(totPayableBal>0 )//&& paidPrinciple<0
            //////////////                    //                            break;
            //////////////                    //                        else{
            //////////////                    balprincipal=new HashMap();
            //////////////                    balprincipal.putAll(map);
            //////////////                    Date installmentDt=DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(payblePrincipalMap.get("INSTALLMENT_DT")));
            //////////////                    if(DateUtil.dateDiff(installmentDt,inst_dt)==0)
            //////////////                        balprincipal.put("PRINCIPAL_BAL",new Double(totPrinciple));
            //////////////                    else
            //////////////                        balprincipal.put("PRINCIPAL_BAL",payblePrincipalMap.get("PRINCIPAL_AMT"));
            //////////////                    if(DateUtil.dateDiff(installmentDt,lastIntCalDt)>0)
            //////////////                        balprincipal.put("START_DT",DateUtil.addDaysProperFormat(lastIntCalDt,1));
            //////////////                    else
            //////////////                        balprincipal.put("START_DT",DateUtil.addDaysProperFormat(installmentDt,1));
            //////////////                    balprincipal.put("END_DT",chekdateLast);
            //////////////                    balprincipal.put(PENALTY,PENALTY);
            //////////////                    balprincipal.put("NO_ROUND_OFF_INT","NO_ROUND_OFF_INT");
            //////////////                    totInterestmap=calculateInterest(balprincipal);
            //////////////                    totPenalInt+=CommonUtil.convertObjToDouble(totInterestmap.get(INTEREST)).doubleValue();
            //////////////                    balprincipal.put(INTEREST,totInterestmap.get(INTEREST));
            //////////////                    if(balprincipal.containsKey("LOAN_ACCOUNT_CLOSING"))
            //////////////                        insertLoanInteresttmp(balprincipal);
            //////////////                    else
            //////////////                        insertLoanInterest(balprincipal);
            //////////////
            //////////////                    //                        }
            //////////////                }
            //////////////            }
            stDate = null;
            //            inst_dt=last
            if (map.containsKey("PENAL_APP_INTEREST") && map.get("PENAL_APP_INTEREST") != null && map.get("PENAL_APP_INTEREST").equals("Y")) {
                for (int x = 0; x < getPrinInt.size(); x++) {
                    //for interest penalint only calculated
                    intcalcMap = new HashMap();

                    tempMap = (HashMap) getPrinInt.get(x);
                    String trnCode = CommonUtil.convertObjToStr(tempMap.get("TRN_CODE"));
                    if ((trnCode.equals("DI") || trnCode.equals("DPI")) && x == 0) {
                        endDate = (Date) tempMap.get("TRANS_DT");
                        if (DateUtil.dateDiff(inst_dt, endDate) > 0) {
                            endDate = DateUtil.addDaysProperFormat(endDate, 1);
                        } else {
                            endDate = DateUtil.addDaysProperFormat(inst_dt, 1);
                        }

                        tempMap.put("TRANS_DT", endDate);
                        //                        endDate=DateUtil.addDaysProperFormat(endDate,1);
                    } else {
                        endDate = (Date) tempMap.get("TRANS_DT");

                    }
                    Bal = CommonUtil.convertObjToDouble(tempMap.get("IBAL")).doubleValue();
                    //                        double loantransprincipal=0;
                    //                        if(trnCode.equals("OLG")||trnCode.equals("C*"))
                    //                         loantransprincipal=CommonUtil.convertObjToDouble(tempMap.get("PRINCIPLE")).doubleValue();
                    System.out.println("stdate##" + stDate + "enddate###" + endDate);

                    //                        totPayableBal-=loantransprincipal;
                    System.out.println("totpayablebal#####  ###" + totPayableBal);
                    if (stDate != null && DateUtil.dateDiff(stDate, endDate) > 0) {

                        map.put("START_DT", stDate);
                        if (DateUtil.dateDiff(endDate, chekdateLast) == 0) {
                            endDatepass = DateUtil.addDaysProperFormat(endDate, 0);
                        } else {
                            endDatepass = DateUtil.addDaysProperFormat(endDate, -1);
                        }
                        double payablePrincipalAmt = 0;

                        //                         endDatepass = endDate;
                        System.out.println("endDatepass" + endDatepass);
                        map.put("END_DT", endDatepass);

                        intcalcMap.putAll(map);
                        System.out.println("before  calculateInt##" + intcalcMap);

                        //                            HashMap totInterestmap=calculateInterest(intcalcMap);
                        //                            totInterest+=CommonUtil.convertObjToDouble(totInterestmap.get(INTEREST)).doubleValue();
                        if (prevAmt > 0) {  //|| totPayableBal>0&& DateUtil.dateDiff(stDate,endDate)>0 CommonUtil.convertObjToDouble(tempMap.get("IBAL")).doubleValue()>0
                            map.put("PRINCIPAL_BAL", new Double(prevAmt));//tempMap.get("IBAL"));    //prevAmt  +totPayableBal
                            intcalcMap.putAll(map);
                            intcalcMap.put(PENALTY, PENALTY);
                            System.out.println("before  calculateInt##" + intcalcMap);
                            intcalcMap.put("NO_ROUND_OFF_INT", "NO_ROUND_OFF_INT");
                            totInterestmap = calculateInterest(intcalcMap);
                            totPenalInt += CommonUtil.convertObjToDouble(totInterestmap.get(INTEREST)).doubleValue();
                            intcalcMap.put(INTEREST, totInterestmap.get(INTEREST));
                            if (balprincipal.containsKey("LOAN_ACCOUNT_CLOSING")) {
                                insertLoanInteresttmp(intcalcMap);
                            } else {
                                insertLoanInterest(intcalcMap);
                            }
                            System.out.println("totPenal" + totPenalInt + "penalInt####" + totInterestmap);
                            intcalcMap.remove(PENALTY);

                        }
                    }
                    stDate = endDate;
                    prevAmt = Bal;
                    System.out.println("#### tempMap : " + tempMap);
                    System.out.println("#### stDate : " + stDate);
                    System.out.println("#### endDate : " + endDate);
                    System.out.println("#### prevAmt : " + prevAmt);
                    System.out.println("#### product : " + product);
                    System.out.println("#### amount : " + amt);
                }
                //                    if(! map.containsKey("BATCH_PROCESS"))
                System.out.println(endDate + "endDate###" + "checkdatelast###" + chekdateLast);
                if (stDate != null && endDate != null && DateUtil.dateDiff(endDate, chekdateLast) > 0 && prevAmt != 0) {//|| totPayableBal>0)

                    map.put("START_DT", endDate);
                    //                                loanDetailsmap.put("END_DT",DateUtil.addDaysProperFormat(curr_dt,-1));
                    map.put("END_DT", chekdateLast);
                    map.put("PRINCIPAL_BAL", new Double(prevAmt));//prevAmt+totPayableBal
                    intcalcMap = new HashMap();
                    intcalcMap.putAll(map);
                    intcalcMap.put(PENALTY, PENALTY);
                    totInterestmap = new HashMap();
                    System.out.println("before batch calculateInt##" + intcalcMap);
                    intcalcMap.put("NO_ROUND_OFF_INT", "NO_ROUND_OFF_INT");
                    totInterestmap = calculateInterest(intcalcMap);
                    totPenalInt += CommonUtil.convertObjToDouble(totInterestmap.get(INTEREST)).doubleValue();
                    intcalcMap.put(INTEREST, totInterestmap.get(INTEREST));
                    if (balprincipal.containsKey("LOAN_ACCOUNT_CLOSING")) {
                        insertLoanInteresttmp(intcalcMap);
                    } else {
                        insertLoanInterest(intcalcMap);
                    }
                    //                            }
                }
                //                    if(! (chargesMap !=null && chargesMap.containsKey("CHARGESUI")))
                //                        if (tempMap!=null && map.containsKey("BATCH_PROCESS")) {
                //                            Calendar c1 = new GregorianCalendar(endDate.getYear(),endDate.getMonth(),endDate.getDate());
                //                            int lastDay = c1.getActualMaximum(Calendar.DAY_OF_MONTH);
                ////                            endDate = new Date(endDate.getYear(),endDate.getMonth(),lastDay);
                //
                //                            if(DateUtil.dateDiff(endDate,chekdateLast)>0){
                //                                map.put("LAST_CALC_DT",endDate);
                //
                //                                map.put("START_DT",endDate);   //stDate);
                //                                map.put("END_DT",chekdateLast);
                //                                double payablePrincipalAmt=0;
                //                                if(payblePrincipalMap.containsKey("PRINCIPAL_AMT"))
                //                                    payablePrincipalAmt=CommonUtil.convertObjToDouble(payblePrincipalMap.get("PRINCIPAL_AMT")).doubleValue();
                //                                map.put("PRINCIPAL_BAL",new Double(prevAmt+payablePrincipalAmt));
                //                                intcalcMap=new HashMap();
                //                                intcalcMap.putAll(map);
                //                                 totInterestmap=new HashMap();
                //                                System.out.println("totInterest gregorian"+intcalcMap);
                //                                if(CommonUtil.convertObjToDouble(tempMap.get("IBAL")).doubleValue()>0 ){//|| totPayableBal>0
                //                                    map.put("PRINCIPAL_BAL",new Double(prevAmt));    //prevAmt  tempMap.get("IBAL") +totPayableBal
                //                                    intcalcMap.putAll(map);
                //                                    intcalcMap.put(PENALTY,PENALTY);
                //                                    System.out.println("before  calculateInt##"+intcalcMap);
                //                                     intcalcMap.put("NO_ROUND_OFF_INT","NO_ROUND_OFF_INT");
                //                                    totInterestmap=calculateInterest(intcalcMap);
                //                                    totPenalInt+=CommonUtil.convertObjToDouble(totInterestmap.get(INTEREST)).doubleValue();
                //                         dont delete            intcalcMap.put(INTEREST,totInterestmap.get(INTEREST));
                //                                if(balprincipal.containsKey("LOAN_ACCOUNT_CLOSING"))
                //                                    insertLoanInteresttmp(intcalcMap);
                //                                else
                //                                    insertLoanInterest(intcalcMap);
                //                                    System.out.println("totPenal"+totPenalInt+"penalInt####"+totInterestmap);
                //                                    intcalcMap.remove(PENALTY);
                //                                }
                //                            }
                //                        }
            }
            intcalcMap.putAll(map);
            intcalcMap.put("PREVAMT", new Double(prevBalPrinciple));         //new Double(prevAmt));
            intcalcMap.put("TOT_INT", new Double(totInterest));
            intcalcMap.put("TOT_PENAL_INT", new Double(totPenalInt));
            System.out.println("intcalcMap####" + intcalcMap);
            totPenalInt = roundOffLoanInterest(intcalcMap);
            intcalcMap.put("TOT_PENAL_INT", new Double(totPenalInt));
            System.out.println("map#####       @@@@@      :" + map);
            intcalcMap.put("ACCOUNTNO", map.get("ACCT_NUM"));
            if (!map.containsKey("LOAN_ACCOUNT_CLOSING")) {
                if (map.containsKey("BATCH_PROCESS") && totPenalInt > 0 && map.containsKey("ASSET_STATUS") && map.get("ASSET_STATUS") != null && map.get("ASSET_STATUS").equals("STANDARD_ASSETS")) {
                    //                        totPenalInt=roundOffLoanInterest(intcalcMap);
                    //                        intcalcMap.put("TOT_PENAL_INT",new Double(totPenalInt));
                    intcalcMap.put("DEBIT_LOAN_TYPE", "DPI");
                    System.out.println("bartchprocessdebitint###" + intcalcMap);
                    batchProcessDebitInt(intcalcMap);
                } else if (totPenalInt > 0) {
                    npaInterest(intcalcMap);
                }
            }
            //        }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return intcalcMap;
    }

    /**
     * rebate interest calculation is reward to customer eligible for rebate is
     * paid amt should be before due date and should not get interest waive off
     *
     */
    private double rebateInterestCalculation(HashMap productRecord) throws Exception {
        Date rebateFromDt = null;
        HashMap dataMap = new HashMap();
        HashMap resultMap = new HashMap();
        double rebateCalculatedInterest = 0;
        System.out.println("productRecord for rebate" + productRecord);
        if (productRecord.get("REBATE_ALLOWED") != null && CommonUtil.convertObjToStr(productRecord.get("REBATE_ALLOWED")).equals("Y")
                && productRecord.get("REBATE_CALCULATION") != null && CommonUtil.convertObjToStr(productRecord.get("REBATE_CALCULATION")).equals("Repayment Time")
                && productRecord.get("ACCOUNT_REBATE_ALLOWED") != null && CommonUtil.convertObjToStr(productRecord.get("ACCOUNT_REBATE_ALLOWED")).equals("Y")) {

            String rebatePeriod = CommonUtil.convertObjToStr(productRecord.get("REBATE_PERIOD"));

            if (productRecord.containsKey("REBATE_DT") && productRecord.get("REBATE_DT") != null && DateUtil.dateDiff((Date) productRecord.get("REBATE_DT"), currDt) == 0) {
                return 0.0;
            }
            if (rebatePeriod.equals("Account open date")) {
                rebateFromDt = (Date) productRecord.get("ACCT_OPEN_DT");
                if (productRecord.containsKey("PENAL_WAIVE_DT") && productRecord.get("PENAL_WAIVE_DT") != null) {
                    return 0.0;
                }

            } else if (rebatePeriod.equals("Financial year")) {
                rebateFromDt = (Date) currDt.clone();
                System.out.println("rebateFromDt" + rebateFromDt + "(rebateFromDt.getMonth()+1) <=4  " + rebateFromDt.getMonth());
                if ((rebateFromDt.getMonth() + 1) > 0 && (rebateFromDt.getMonth() + 1) <= 4) {
//                     rebateFromDt = DateUtil.addDays(rebateFromDt,-365);
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.YEAR, -1);
                    rebateFromDt = new Date(cal.getTimeInMillis());
                }
                System.out.println("rebateFromDt.getMonth()+1 " + rebateFromDt.getMonth() + 1 + "rebateFromDt###" + rebateFromDt);
                rebateFromDt.setDate(CommonUtil.convertObjToInt(productRecord.get("FIN_YEAR_START_DD")));
                rebateFromDt.setMonth(CommonUtil.convertObjToInt(productRecord.get("FIN_YEAR_START_MM")) - 1);
                if (productRecord.containsKey("PENAL_WAIVE_DT") && productRecord.get("PENAL_WAIVE_DT") != null) {
                    Date waiveDate = (Date) productRecord.get("PENAL_WAIVE_DT");
                    System.out.println("waiveDate" + waiveDate + "rebateFromDt   " + rebateFromDt);
                    if (DateUtil.dateDiff(rebateFromDt, waiveDate) >= 0) {
                        return 0.0;
                    }
                }
            }

            dataMap.put("FROM_DT", rebateFromDt);
            dataMap.put("TO_DATE", currDt);
            dataMap.put("ACT_NUM", productRecord.get("ACCT_NUM"));

            //checking whether this account eligible of rebatecalculation
            List paidInterest = (List) sqlMap.executeQueryForList("getPaidPrinciple", dataMap);
            if (paidInterest != null && paidInterest.size() > 0) {
                resultMap = (HashMap) paidInterest.get(0);
                double paidPenal = CommonUtil.convertObjToDouble(resultMap.get("PENAL")).doubleValue();
                if (paidPenal > 0.0) {
                    return 0.0;
                }
            }

            //end
            if (productRecord.get("REBATE_DT") != null) {
                rebateFromDt = (Date) productRecord.get("REBATE_DT");
            } else {
                rebateFromDt = (Date) productRecord.get("ACCT_OPEN_DT");
            }
            dataMap.put("ACT_NUM", productRecord.get("ACCT_NUM"));
            dataMap.put("FROM_DT", rebateFromDt);
            dataMap.put("TO_DATE", currDt);
            double rebatePercentage = CommonUtil.convertObjToDouble(productRecord.get("REBATE_PERCENTAGE")).doubleValue();
            paidInterest = (List) sqlMap.executeQueryForList("getPaidPrinciple", dataMap);
            if (paidInterest != null && paidInterest.size() > 0) {
                resultMap = (HashMap) paidInterest.get(0);
                double paidInterestAmt = CommonUtil.convertObjToDouble(resultMap.get("INTEREST")).doubleValue();
                if (paidInterestAmt > 0) {
                    List payableInterest = (List) sqlMap.executeQueryForList("selectLoanInterest", dataMap);
                    if (payableInterest == null || payableInterest.isEmpty()) {
                        payableInterest = (List) sqlMap.executeQueryForList("selectLoanInterestTMP", dataMap);
                    }

                    if (payableInterest != null && payableInterest.size() > 0) {
                        for (int i = 0; i < payableInterest.size(); i++) {
                            HashMap interestMap = (HashMap) payableInterest.get(i);
                            double intRate = CommonUtil.convertObjToDouble(interestMap.get("INT_RATE")).doubleValue();
                            double calculatedIntAmt = CommonUtil.convertObjToDouble(interestMap.get("INT_AMT")).doubleValue();

                            if (calculatedIntAmt > 0) {
                                if (paidInterestAmt >= calculatedIntAmt) {
                                    rebateCalculatedInterest += calculatedIntAmt * rebatePercentage / intRate;
                                    paidInterestAmt -= calculatedIntAmt;
                                } else if (paidInterestAmt < calculatedIntAmt) {
                                    rebateCalculatedInterest += paidInterestAmt * rebatePercentage / intRate;
                                    paidInterestAmt = 0;
                                }
                            }

                        }

                        dataMap.put("PROD_ID", productRecord.get("PROD_ID"));
                        dataMap.put("TOT_INTEREST", new Double(rebateCalculatedInterest));
                        rebateCalculatedInterest = roundOffLoanInterest(dataMap);

                    }
                }

            }
        }

        return rebateCalculatedInterest;
    }

    private HashMap dateMinus(HashMap dateMap) {
        String day = CommonUtil.convertObjToStr(dateMap.get("NEXT_DATE"));
        //        Date lastDay=DateUtil.getDateMMDDYYYY(day);
        Date lastDay = (Date) dateMap.get("NEXT_DATE");
        int days = lastDay.getDate();
        days--;
        lastDay.setDate(days);
        dateMap.put("NEXT_DATE", lastDay);
        dateMap.put("BRANCH_CODE", _branchID);
        return dateMap;
    }

    private boolean doNoHoliday(String nonHoliday) {
        checkThisCDate = DateUtil.getDateMMDDYYYY(nonHoliday);
        System.out.println("nonHoliday" + nonHoliday);
        return false;
    }

    public Date holiydaychecking(HashMap hash) {
        HashMap MonthEnd = new HashMap(); //traansferto holidaychecking method;
        HashMap chekLastDay = new HashMap();
        chekLastDay.put("CURR_DATE", hash.get("CURR_DATE"));
        try {
            List LastDaychek = sqlMap.executeQueryForList("GetLastDateAD", chekLastDay);
            if (LastDaychek.size() > 0) {

                HashMap lastday = (HashMap) LastDaychek.get(0);
                String lastdays = CommonUtil.convertObjToStr(lastday.get("LAST_DAYS"));
                System.out.println("monthendday" + lastdays);
                chekdateLast = (Date) lastday.get("LAST_DAYS");// DateUtil.getDateMMDDYYYY(lastdays);
                checkThisCDate = DateUtil.getDateMMDDYYYY(lastdays);
                freq = 0;
                if (chekdateLast.getMonth() == 2 || chekdateLast.getMonth() == 5 || chekdateLast.getMonth() == 8 || chekdateLast.getMonth() == 11) {   // for quertrly
                    freq = 90;
                }
                if (chekdateLast.getMonth() == 8 || chekdateLast.getMonth() == 2) //for half yearly
                {
                    freq = 180;
                }
                if (chekdateLast.getMonth() == 2) {  // annual
                    freq = 365;
                }
                if (freq == 0) {
                    freq = 30;
                }
            }
            boolean checkHoliday = true;
            Date date = (Date) chekdateLast.clone();
            MonthEnd.put("NEXT_DATE", date);
            MonthEnd.put("BRANCH_CODE", hash.get("BRANCH_CODE"));
            //                try{
            //            sqlMap.startTransaction();
            Lday = chekdateLast;
            while (checkHoliday) {
                boolean tholiday = false;
                System.out.println("enterytothecheckholiday" + checkHoliday);
                List Holiday = sqlMap.executeQueryForList("checkHolidayDateOD", MonthEnd);
                List weeklyOf = sqlMap.executeQueryForList("checkWeeklyOffOD", MonthEnd);
                boolean isHoliday = Holiday.size() > 0 ? true : false;
                boolean isWeekOff = weeklyOf.size() > 0 ? true : false;
                if (isHoliday || isWeekOff) {
                    MonthEnd = dateMinus(MonthEnd);
                    checkHoliday = true;
                } else {
                    checkHoliday = doNoHoliday(CommonUtil.convertObjToStr(MonthEnd.get("NEXT_DATE")));;

                }
                //                if(! isWeekOff)
                //                    if(isHoliday) {
                //                        MonthEnd = dateMinus(MonthEnd);
                //                        checkHoliday=true;
                //                    }
                //                    else  {
                //                        checkHoliday=doNoHoliday(CommonUtil.convertObjToStr(MonthEnd.get("NEXT_DATE")));;
                //                    }
                //                if(!isHoliday)
                //                    if(isWeekOff) {
                //                        HashMap week=(HashMap)weeklyOf.get(0);
                //                        int datenum=Lday.getDay();
                //                        int ofyes=CommonUtil.convertObjToInt(week.get("WEEKLY_OFF1"));
                //                        if(datenum==ofyes){
                //                            MonthEnd = dateMinus(MonthEnd);
                //                            checkHoliday=true;
                //                        }
                //                    }
                //                    else {
                //                        checkHoliday=doNoHoliday(CommonUtil.convertObjToStr(MonthEnd.get("NEXT_DATE")));;
                //                    }

            }

            //            sqlMap.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return checkThisCDate;
    }
    //TRANSACTION

    void doTransaction(HashMap hash) throws Exception {
        System.out.println("doTRansaction ####" + hash);
        HashMap txMap = new HashMap();
        TxTransferTO transferTo = new TxTransferTO();
        ArrayList transferList = new ArrayList(); // for local transfer

        txMap.put(TransferTrans.DR_AC_HD, hash.get("LOAN_ACHD"));
        txMap.put(TransferTrans.DR_BRANCH, _branchID);
        txMap.put(TransferTrans.CURRENCY, "INR");
        txMap.put(TransferTrans.DR_ACT_NUM, hash.get("ACCOUNTNO"));
        if (CommonUtil.convertObjToStr(hash.get("PROD_TYPE")).equals("ADVANCES")) {
            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.ADVANCES);
            txMap.put("TRANS_MOD_TYPE", TransactionFactory.ADVANCES);
        } else {
            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.LOANS);
            txMap.put("TRANS_MOD_TYPE", TransactionFactory.LOANS);
        }
        if (chargesMap != null && chargesMap.containsKey("CHARGESUI")) {
            txMap.put(TransferTrans.DR_INSTRUMENT_2, "CHARGESUI");
        }
        double ibal = CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();//IBAL
        if (ibal == 0 && !CommonUtil.convertObjToStr(hash.get("PROD_TYPE")).equals("ADVANCES")) {
            ibal = CommonUtil.convertObjToDouble(hash.get("TOT_PENAL_INT")).doubleValue();
        }
        txMap.put(TransferTrans.CR_AC_HD, hash.get("INT_RECIVABLE"));

        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
        txMap.put(TransferTrans.CR_BRANCH, _branchID);
        txMap.put(TransferTrans.CURRENCY, "INR");
        System.out.print("txmap####transfer##" + txMap);
        TransferTrans trans = new TransferTrans();
        trans.setTransMode(CommonConstants.TX_TRANSFER);
        trans.setInitiatedBranch(_branchCode);
        trans.setLinkBatchId(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
        if (ibal > 0) {
            if (hash.containsKey("DEBIT_LOAN_TYPE") && hash.get("DEBIT_LOAN_TYPE") != null) {
                txMap.put("PARTICULARS", "Penal InterestUpto " + DateUtil.getStringDate(chekdateLast));
            } else {
                txMap.put("PARTICULARS", "InterestUpto " + DateUtil.getStringDate(chekdateLast));
            }

            if (CommonUtil.convertObjToStr(hash.get("PROD_TYPE")).equals("ADVANCES") && hash.containsKey("TOT_PENAL_INT") && hash.get("TOT_PENAL_INT") != null) {
                double d = ibal + CommonUtil.convertObjToDouble(hash.get("TOT_PENAL_INT")).doubleValue();
                transferList.add(trans.getDebitTransferTO(txMap, d));
            } else {
                transferList.add(trans.getDebitTransferTO(txMap, ibal));
            }

            txMap.put("PARTICULARS", "" + hash.get("ACCOUNTNO"));
            transferList.add(trans.getCreditTransferTO(txMap, ibal));
            if (CommonUtil.convertObjToStr(hash.get("PROD_TYPE")).equals("ADVANCES") && hash.containsKey("TOT_PENAL_INT") && hash.get("TOT_PENAL_INT") != null) {

                ibal = CommonUtil.convertObjToDouble(hash.get("TOT_PENAL_INT")).doubleValue();
                txMap.put(TransferTrans.CR_AC_HD, hash.get("PENAL_INT_OD"));

                txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                if (CommonUtil.convertObjToStr(hash.get("PROD_TYPE")).equals("ADVANCES")) {
                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.ADVANCES);
                } else {
                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.LOANS);
                }
                transferList.add(trans.getCreditTransferTO(txMap, ibal));
            }

            if (CommonUtil.convertObjToStr(hash.get("PROD_TYPE")).equals("ADVANCES")) {
                trans.setForDebitInt(false);
            } else {
                trans.setForDebitInt(true);
            }
            if (hash.containsKey("DEBIT_LOAN_TYPE") && hash.get("DEBIT_LOAN_TYPE") != null)//debit loan type key passed from dpi only
            {
                trans.setLoanDebitInt("DPI");
            } else {
                trans.setLoanDebitInt("DI");//default
            }
            System.out.println("transferListbbbbbb" + transferList);

            trans.doDebitCredit(transferList, _branchCode, true);

            System.out.println("bbbbblllllll");

//                 if(CommonUtil.convertObjToStr(hash.get("PROD_TYPE")).equals("ADVANCES") && hash.containsKey("TOT_PENAL_INT") && hash.get("TOT_PENAL_INT")!=null)
//            {
//                
//                  txMap=new HashMap();
//         transferTo=new TxTransferTO();
//         transferList = new ArrayList(); // for local transfer
//        
////        txMap.put(TransferTrans.DR_AC_HD, hash.get("LOAN_ACHD"));
////        txMap.put(TransferTrans.DR_BRANCH, _branchID);
////        txMap.put(TransferTrans.CURRENCY, "INR");
////        txMap.put(TransferTrans.DR_ACT_NUM, hash.get("ACCOUNTNO"));
////        if(CommonUtil.convertObjToStr(hash.get("PROD_TYPE")).equals("ADVANCES"))
////            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.ADVANCES);
////        else
////            txMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.LOANS);
//         //ibal=CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();//IBAL
//       // if(ibal==0)
//            ibal=CommonUtil.convertObjToDouble(hash.get("TOT_PENAL_INT")).doubleValue();
//        txMap.put(TransferTrans.CR_AC_HD, hash.get("PENAL_INT_OD"));
//        
//        txMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
//        txMap.put(TransferTrans.CR_BRANCH, _branchID);
//        txMap.put(TransferTrans.CURRENCY, "INR");
//        System.out.print("txmap####transfer##"+txMap);
//         trans = new TransferTrans();
//        trans.setTransMode(CommonConstants.CREDIT);
//        trans.setInitiatedBranch(_branchCode);
//        trans.setLinkBatchId(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
//             
//          
//                
//        if(ibal>0)
//        {
//        if(hash.containsKey("DEBIT_LOAN_TYPE") && hash.get("DEBIT_LOAN_TYPE")!=null)
//                txMap.put("PARTICULARS","Penal InterestUpto "+DateUtil.getStringDate(chekdateLast));
//            else
//                txMap.put("PARTICULARS","PenalInterestUpto "+DateUtil.getStringDate(chekdateLast));
////            transferList.add(trans.getDebitTransferTO(txMap,ibal));
//            txMap.put("PARTICULARS", ""+hash.get("ACCOUNTNO"));
//            transferList.add(trans.getCreditTransferTO(txMap,ibal));
////             if(CommonUtil.convertObjToStr(hash.get("PROD_TYPE")).equals("ADVANCES"))
////                trans.setForDebitInt(false);
////             else 
////            trans.setForDebitInt(true);
////            if(hash.containsKey("DEBIT_LOAN_TYPE") && hash.get("DEBIT_LOAN_TYPE")!=null)//debit loan type key passed from dpi only
////                trans.setLoanDebitInt("DPI");
////            else
////                trans.setLoanDebitInt("DI");//default
////            System.out.println("transferListfdgdfgd55555bbbbb"+transferList);
//           trans.doDebitCredit(transferList, _branchCode, true);
//           
//        //  trans.
////        
////            }
//        
//        
//            }
//            
//            }
            System.out.println("ServerUtil.getCurrentDate(_branchCode)>>" + ServerUtil.getCurrentDate(_branchCode));
            hash.put("LAST_CALC_DT", DateUtil.addDaysProperFormat(ServerUtil.getCurrentDate(_branchCode), -1));
            sqlMap.executeUpdate("updateclearBal", hash);
            if (retraspectiveMap.containsKey("UPDATE_RET_APP_DT") && retraspectiveMap.get("UPDATE_RET_APP_DT") != null) {
                hash.put("UPDATE_RET_APP_DT", retraspectiveMap.get("UPDATE_RET_APP_DT"));
                sqlMap.executeUpdate("updateRetrasPectiveDt", hash);
            }

        }
        System.out.println("TRANSFERLIST #####" + transferList);
        ibal = 0;
    }

    public Date getProperDateFormat(Object obj) {
        Date currDate = null;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            currDate = (Date) currDt.clone();
            currDate.setDate(tempDt.getDate());
            currDate.setMonth(tempDt.getMonth());
            currDate.setYear(tempDt.getYear());
        }
        return currDate;
    }

    HashMap calculateInterest(HashMap map) throws Exception {
        System.out.println("calcualteinterest ###" + map);
        HashMap hash = null;
        if (map.containsKey(PENALTY)) {
            hash = setInterestCalculationBeanValues(map, PENALTY);
        } else {
            hash = setInterestCalculationBeanValues(map, ROI);
        }
        LoanCalculateInterest interest = new LoanCalculateInterest();
        //modified by rishad 03/02/2015 exception handled properly
        try {
            Thread.sleep((long) Math.random());
        } catch (InterruptedException ex) {
            System.err.println("An InterruptedException was caught: " + ex.getMessage());
        }

        if (interestsMap != null) {
            hash.put("INTEREST_MAP", interestsMap);
            System.out.println("HHHHHASH" + hash);
        }
        HashMap getIntAmt = interest.getInterest(hash);
        return getIntAmt;
    }

    private List getInterestDetails(HashMap whereMap, HashMap passDt) throws Exception {
        List list = null;
        String intToGetFrom = CommonUtil.convertObjToStr(whereMap.get("INT_GET_FROM"));
        if (intToGetFrom.equals("PROD")) {
            whereMap.put(CommonConstants.DEBIT, CommonConstants.DEBIT);
            // Add code for making interest periodwise : Added by nithya on 30-10-2017 for 7867
            List intCalcMethodLst = (List) sqlMap.executeQueryForList("checkIfIntCalcPeriodSlabWiseOrNot", whereMap);
            if (intCalcMethodLst != null && intCalcMethodLst.size() > 0) {
                HashMap intCalcMethodMap = (HashMap) intCalcMethodLst.get(0);
                if (intCalcMethodMap.containsKey("IS_INT_PERIOD_SLAB_CALC") && intCalcMethodMap.get("IS_INT_PERIOD_SLAB_CALC") != null && !"".equals(intCalcMethodMap.get("IS_INT_PERIOD_SLAB_CALC"))) {
                    if (CommonUtil.convertObjToStr(intCalcMethodMap.get("IS_INT_PERIOD_SLAB_CALC")).equalsIgnoreCase("Y")) {
                        whereMap.put("PERIOD_WISE_INT_RATE", "PERIOD_WISE_INT_RATE");
                        whereMap.put("INT_UP_TO_DATE", currDt.clone());
                    } else {
                        whereMap.put("AMOUNT_WISE_INT_RATE", "AMOUNT_WISE_INT_RATE");
                    }
                } else {
                    whereMap.put("AMOUNT_WISE_INT_RATE", "AMOUNT_WISE_INT_RATE");
                }
            } else {
                whereMap.put("AMOUNT_WISE_INT_RATE", "AMOUNT_WISE_INT_RATE");
            }
            list = (List) sqlMap.executeQueryForList("getSelectProductTermLoanInterestMap", whereMap); //change deposit date to from date
            System.out.println("prodListrepaymentcalculator" + list);
        } else if (intToGetFrom.equals("ACT")) {
            list = (List) sqlMap.executeQueryForList("getSelectAccountTermLoanInterestMap", whereMap);
            System.out.println("actListrepaymentcalculator" + list);
        }
        if (list != null && list.size() > 0 && (passDt.containsKey("DEPOSIT_PREMATURE_CLOSER") || prematureLTD)) {
            for (int i = 0; i < list.size(); i++) {
                HashMap depositList = (HashMap) list.get(i);
                System.out.println("depositList ###" + depositList);
                depositList.put("INTEREST", passDt.get("DEPOSIT INT"));
                list.set(i, depositList);
            }
            System.out.println("list####" + list);
        }

        if (list == null || list.size() == 0) {
            throw new ValidationRuleException(TermLoanConstants.NO_INTEREST_DETAILS);
        }
        return list;
    }

    private HashMap setInterestCalculationBeanValues(HashMap prodLevelWhereMap, String interestType) throws Exception {
        HashMap map = new HashMap();
        HashMap repayMap = new HashMap();
        interestRate = new String();
        HashMap prodLevelValues = getProductLevelValues(CommonUtil.convertObjToStr(prodLevelWhereMap.get("PROD_ID")));
        InterestCalculationBean interestBean = new InterestCalculationBean();
        interestBean.setCompoundingPeriod("365");
        interestBean.setPrincipalAmt(CommonUtil.convertObjToStr(prodLevelWhereMap.get("PRINCIPAL_BAL")));
        //        if(CommonUtil.convertObjToStr(prodLevelWhereMap.get("PROD_ID")).equals("OD"))
        //            interestBean.setCompoundingType("COMPOUND");
        //        else
        interestBean.setCompoundingType("REPAYMENT");
        //        interestBean.setInterestType(CommonUtil.convertObjToStr(prodLevelWhereMap.get("INTEREST")));//COMPUND OR SIMPLE
        interestBean.setInterestType("COMPOUND");
        interestBean.setIsDuration_ddmmyy(false);
        interestBean.setDuration_FromDate(CommonUtil.convertObjToStr(prodLevelWhereMap.get("START_DT")));
        interestBean.setDuration_ToDate(CommonUtil.convertObjToStr(prodLevelWhereMap.get("END_DT")));
        interestBean.setFloatPrecision("2");
        if (prodLevelValues.containsKey("YEAR_OPTION")) {
            interestBean.setYearOption(CommonUtil.convertObjToStr(prodLevelValues.get("YEAR_OPTION")));
        } else {
            interestBean.setYearOption(InterestCalculationConstants.YEAR_OPTION_ACTUAL);
        }
        if (prodLevelValues.containsKey("MONTH_OPTION")) {
            interestBean.setMonthOption(CommonUtil.convertObjToStr(prodLevelValues.get("MONTH_OPTION")));
        } else {
            interestBean.setMonthOption(InterestCalculationConstants.MONTH_OPTION_ACTUAL);
        }
        if (prodLevelValues.containsKey("ROUNDING_TYPE")) {
            interestBean.setRoundingType(CommonUtil.convertObjToStr(prodLevelValues.get("ROUNDING_TYPE")));
        } else {
            interestBean.setRoundingType(InterestCalculationConstants.ROUNDING_HIGHER);
        }
        //Mantis 8358 - Babu
        if (prodLevelValues.containsKey("DEBIT_INT_ROUNDOFF")) {
            if (prodLevelValues.get("DEBIT_INT_ROUNDOFF") != null) {
                String intRd = CommonUtil.convertObjToStr(prodLevelValues.get("DEBIT_INT_ROUNDOFF"));
                if (intRd != null && intRd.equals(ROUNDING_NEAREST)) {
                    interestBean.setRoundingType(InterestCalculationConstants.ROUNDING_NEAREST);
                }
                if (intRd != null && intRd.equals(NEAREST_TENS)) {
                    interestBean.setRoundingType(InterestCalculationConstants.ROUNDING_VALUE_10_RUPEES);
                }
                if (intRd != null && intRd.equals(NEAREST_HUNDREDS)) {
                    interestBean.setRoundingType(InterestCalculationConstants.ROUNDING_VALUE_100_RUPEES);
                }
            }
        }
        if (prodLevelValues.containsKey("ROUNDING_FACTOR")) {
            interestBean.setRoundingFactor(CommonUtil.convertObjToStr(prodLevelValues.get("ROUNDING_FACTOR")));
        } else {
            interestBean.setRoundingFactor(InterestCalculationConstants.ROUNDING_VALUE_1_RUPEE);
        }
        if (prodLevelWhereMap.containsKey("NO_ROUND_OFF_INT")) {
            interestBean.setNoLoanRoundeInt("NO_ROUND_OFF_INT");
        }
        repayMap.put("FROM_DATE", (Date) prodLevelWhereMap.get("START_DT"));
        repayMap.put("TO_DATE", (Date) prodLevelWhereMap.get("END_DT"));
        ArrayList variousInterestList = null;
        //        if (interestType.equals(ROI) || interestType.equals(PENALTY)){ 
        zeroIntMap = new HashMap();
        zeroIntMap.putAll(prodLevelWhereMap);
        variousInterestList = getInterestList(interestList, interestType);

        //        }
        //        else
        if (interestType.equals(PENALTY)) {
            //            variousInterestList = getPenulInterestList(DateUtil.getStringDate((Date)prodLevelWhereMap.get("START_DT")), DateUtil.getStringDate((Date)prodLevelWhereMap.get("END_DT")));
        }
        if (checkZeroInterest(zeroIntMap)) {
            interestBean.setRateOfInterest("0");
        } else {
            interestBean.setRateOfInterest(CommonUtil.convertObjToStr(((HashMap) variousInterestList.get(0)).get(INTEREST)));
        }
        System.out.println("interestBean.setRateOfInterest^$^$^$^$^$^" + interestBean.getRateOfInterest());
        //interestBean.setRateOfInterest(CommonUtil.convertObjToStr(((HashMap) variousInterestList.get(0)).get(INTEREST)));
        //interestBean.setRateOfInterest(CommonUtil.convertObjToStr(((HashMap) variousInterestList.get(0)).get(INTEREST)));
        //for insert interest detais
        interestRate = CommonUtil.convertObjToStr(((HashMap) variousInterestList.get(0)).get(INTEREST));
        repayMap.put("VARIOUS_INTEREST_RATE", variousInterestList);
        repayMap.put("REPAYMENT_TYPE", "REPAYMENT");
        map.put(CommonConstants.DATA, interestBean);
        //        map.put("INTEREST_TYPE_C_S",prodLevelWhereMap.get("INTEREST"));
        map.put("REPAYMENT_DETAILS", repayMap);
        System.out.println("interest Bean  all map#####" + map);
        repayMap = null;
        interestBean = null;
        return map;
    }

    private ArrayList getInterestList(List list, String interestType) throws Exception {
        HashMap temp;
        HashMap interestMap;
        HashMap matToDate;
        System.out.println("getInterestList" + list + "interestType" + interestType);
        ArrayList various_interestList = new ArrayList();
        for (int i = list.size() - 1, j = 0; i >= 0; --i, ++j) {
            temp = (HashMap) list.get(j);
            //line added by nidhin

            if (temp.containsKey("TO_DT") && temp.get("TO_DT") != null) {
                interestsMap.put("MAT_DATE", temp.get("TO_DT"));
            }
            System.out.println("TODATEEEE" + interestsMap);
            interestMap = new HashMap();
            if (interestType.equals(ROI)) {
                if (checkZeroInterest(zeroIntMap)) {
                    interestMap.put(INTEREST, new Double(0));
                } else {
                    interestMap.put(INTEREST, temp.get(INTEREST));
                }
            } else if (interestType.equals(PENALTY)) {
                interestMap.put(INTEREST, temp.get("PENAL_INTEREST"));
            }
            interestMap.put("FROM_DATE", DateUtil.getStringDate((Date) temp.get("FROM_DT")));
            various_interestList.add(interestMap);
            interestMap = null;
            temp = null;
        }
        temp = null;
        System.out.println("varius interest list###3" + various_interestList);
        return various_interestList;
    }

    private HashMap getProductLevelValues(String strProdID) throws Exception {
        HashMap transactionMap = new HashMap();
        HashMap retrieve = new HashMap();
        transactionMap.put("PROD_ID", strProdID);
        List resultList = (List) sqlMap.executeQueryForList("getCompFreqRoundOff_LoanProd", transactionMap);
        if (resultList.size() > 0) {
            // If Product Account Head exist in Database
            retrieve = (HashMap) resultList.get(0);
        }
        transactionMap = null;
        resultList = null;
        return retrieve;
    }

    private boolean checkZeroInterest(HashMap intMap) throws Exception {
        System.out.println("intMap&%%&%&%&%&%&" + intMap);
        boolean checkFlag = false;
        Date stDt = (Date) currDt.clone();
        stDt.setDate(currDt.getDate());
        stDt.setMonth(currDt.getMonth());
        stDt.setYear(currDt.getYear());
        Date tempDt = (Date) currDt.clone();
        tempDt.setDate(currDt.getDate());
        tempDt.setMonth(currDt.getMonth());
        tempDt.setYear(currDt.getYear());
        Date fromDt = null;
        int gracePd = 0;
        if (CommonUtil.convertObjToStr(intMap.get("ZERO_INTEREST")).equals("Y")) {
            if (!CommonUtil.convertObjToStr(intMap.get("ZERO_INTEREST_PERIOD")).equals("")
                    && CommonUtil.convertObjToInt(intMap.get("ZERO_INTEREST_PERIOD")) > 0) {
                gracePd = CommonUtil.convertObjToInt(intMap.get("ZERO_INTEREST_PERIOD"));
                fromDt = DateUtil.addDays(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(intMap.get("FROM_DATE"))), gracePd * 30);
                if (fromDt.compareTo(tempDt) > 0) {
                    checkFlag = true;
                }
            }
        }
        return checkFlag;
    }

    private ArrayList getPenulInterestList(String fromDate, String toDate) throws Exception {
        ArrayList list = null;
        return list;

    }

    private HashMap prematureDepositClosingIntForAsOnWhen(HashMap productRecord, HashMap map) throws Exception {
        Date startDt = new Date();
        Date endDt = new Date();
        System.out.println("productRecord####" + productRecord + "map####" + map);
        HashMap periodMap = new HashMap();
        HashMap returnmap = new HashMap();
        startDt = (Date) productRecord.get("ACCT_OPEN_DT");
        endDt = (Date) map.get("CURR_DATE");

        periodMap.put("ACT_NUM", productRecord.get("ACCT_NUM"));
        periodMap.put("FROM_DT", startDt);
        periodMap.put("TO_DT", endDt);
        List principalTransList = (List) sqlMap.executeQueryForList("getPrincipalTransactions", periodMap);
        HashMap fromPrincipalTransMap = new HashMap();
        HashMap toPrincipalTransMap = new HashMap();
        if (principalTransList != null && principalTransList.size() > 0) {
            int transSize = principalTransList.size();
            for (int i = 0; i < transSize; i++) {

                fromPrincipalTransMap = (HashMap) principalTransList.get(i);
                periodMap.put("FROM_DT", fromPrincipalTransMap.get("TRANS_DT"));
                periodMap.put("TO_DT", endDt);
                if (transSize >= (i + 2)) {
                    toPrincipalTransMap = (HashMap) principalTransList.get(i + 1);
                    periodMap.put("TO_DT", toPrincipalTransMap.get("TRANS_DT"));
                }
            }
        }

        return null;
    }

    private HashMap prematureDepositClosingInt(HashMap productRecord, HashMap map) throws Exception {
        Date startDt = new Date();
        String stDate = null;
        System.out.println("productRecord####" + productRecord + "map####" + map);
        HashMap periodMap = new HashMap();
        HashMap returnmap = new HashMap();
        stDate = CommonUtil.convertObjToStr(productRecord.get("ACCT_OPEN_DT"));
        startDt = DateUtil.getDateMMDDYYYY(stDate);
        Date stDate1 = startDt;
        periodMap.put("ACT_NUM", productRecord.get("ACCT_NUM"));
        HashMap lastDateMap = new HashMap();

        sqlMap.executeUpdate("deleteLTDTrans", periodMap);
        int j = 1;
        do {
            periodMap.put("START_DATE", startDt);
            lastDateMap.put("CURR_DATE", stDate1);
            List monthEnddt = sqlMap.executeQueryForList("GetLastDateAD", lastDateMap);
            HashMap lastday = (HashMap) monthEnddt.get(0);
            //            String lastdays=CommonUtil.convertObjToStr(lastday.get("LAST_DAYS"));
            //            System.out.println("monthendday"+lastdays);
            Date endDt = (Date) lastday.get("LAST_DAYS");
            periodMap.put("CURR_DATE", endDt);

            List transList = sqlMap.executeQueryForList("getPrincipalforCalPrematureInt", periodMap);
            for (int i = 0; i < transList.size(); i++) {
                HashMap insertMap = (HashMap) transList.get(i);
                insertMap.put("BRANCH_ID", map.get("BRANCH_ID"));
                insertMap.put("PROD_ID", productRecord.get("PROD_ID"));
                insertMap.put("ACT_NUM", productRecord.get("ACCT_NUM"));
                String trans_strdt = CommonUtil.convertObjToStr(insertMap.get("TRANS_DT"));
                Date trans_Dt = DateUtil.getDateMMDDYYYY(trans_strdt);
                String trnCode = CommonUtil.convertObjToStr(insertMap.get("TRN_CODE"));
                if (DateUtil.dateDiff(trans_Dt, endDt) >= 0) {
                    if (trnCode.equals("OLG") || trnCode.equals("C*")) {
                        //                        double totAmt=CommonUtil.convertObjToDouble(insertMap.get("PRINCIPLE")).doubleValue();
                        //                        totAmt+=CommonUtil.convertObjToDouble(insertMap.get("INTEREST")).doubleValue();
                        //                        totAmt+=CommonUtil.convertObjToDouble(insertMap.get("PENAL")).doubleValue();
                        //                        totAmt+=CommonUtil.convertObjToDouble(insertMap.get("EXPENSE")).doubleValue();
                        //                        insertMap.put("TOT_AMT",new Double(totAmt));
                        HashMap creditMap = lastTransactionDetails(insertMap);
                        creditMap.put("TOT_AMT", insertMap.get("TOT_AMT"));
                        creditMap.put("ACT_NUM", productRecord.get("ACCT_NUM"));
                        HashMap outputMap = insertcredit(creditMap);
                    } else {

                        HashMap retunmap = insertAuthorizeLoanDebit(insertMap);
                        System.out.println("retunmap###$$$     " + retunmap);
                    }
                }
            }
            interestList = null;
            interestList = getInterestDetails(productRecord, map);
            transList = sqlMap.executeQueryForList("getPrincipalforPrematureInterestCalculation", periodMap);
            if (transList != null && transList.size() > 0) {
                //                    for(int i=0;i<transList.size();i++){
                //                    HashMap interestmap=transList.get(i);
                //                    interestmap.putAll(map);
                if ((endDt.getMonth() == chekdateLast.getMonth()) && ((endDt.getYear() + 1900) == (chekdateLast.getYear() + 1900))) {
                    map.put("END_DT", chekdateLast);
                } else {
                    map.put("END_DT", endDt);
                }
                System.out.println("checkdatelast####3333333333" + chekdateLast);
                map.putAll(productRecord);
                HashMap intMap = calculatePrematureInt(transList, map);
                intMap.put("TRN_CODE", "DI");
                intMap.put("TOT_AMT", intMap.get("TOT_PENAL_INT"));
                //                    String strEndDt = CommonUtil.convertObjToStr(endDt);
                //                    Date dtEndDt = DateUtil.getDateMMDDYYYY(strEndDt);
                java.util.Calendar c1 = new java.util.GregorianCalendar(endDt.getYear(), endDt.getMonth(), endDt.getDate());
                System.out.println("@#@#@# endDt " + endDt);
                Date trans_dt = (Date) map.get("END_DT");
                intMap.put("TRANS_DT", trans_dt);
                System.out.println("@#@#@# intMap #### " + intMap);

                HashMap interstMap = insertAuthorizeLoanDebit(intMap);
                //                    }
                //                }

            }
            startDt = endDt;
            stDate1 = DateUtil.addDaysProperFormat(endDt, 1);//for month end
        } while (DateUtil.dateDiff(startDt, chekdateLast) > 0);
        //retun premature int and prev int
        double prematureInt = 0;
        List getIntDetails = sqlMap.executeQueryForList("getIntDetailsForLTD", periodMap);
        if (getIntDetails != null && getIntDetails.size() > 0) {
            HashMap ltdInt = (HashMap) getIntDetails.get(0);
            prematureInt = CommonUtil.convertObjToDouble(ltdInt.get("IBAL")).doubleValue();
            returnmap.put("PREMATURE_INT", ltdInt.get("IBAL"));
        }
        periodMap = new HashMap();
        periodMap.put("ACT_NUM", productRecord.get("ACCT_NUM"));
        periodMap.put("START_DATE", productRecord.get("ACCT_OPEN_DT"));
        periodMap.put("CURR_DATE", chekdateLast);
        List getprevIntDetails = sqlMap.executeQueryForList("getPaidAmount", periodMap);//getIntDetails
        if (getprevIntDetails != null && getprevIntDetails.size() > 0) {
            HashMap preInt = (HashMap) getprevIntDetails.get(0);
            returnmap.put("PAID_PRINCIPLE", preInt.get("PRINCIPLE"));
            double paidInt = CommonUtil.convertObjToDouble(preInt.get("INTEREST")).doubleValue();
            double finalInt = paidInt - prematureInt;
            //              finalInt*=-1;
            //              returnmap.put("LOAN_INT",preInt.get("INTEREST"));
            returnmap.put("FINAL_INT", new Double(finalInt));
            //              returnmap.put("PAID_EXPENSE",preInt.get("EXPENSE"));
        } else {
            returnmap.put("FINAL_INT", new Double(prematureInt));
        }
        System.out.println("returnMap###" + returnmap);
        return returnmap;
    }

    private HashMap lastTransactionDetails(HashMap actMap) throws Exception {
        List getIntDetails = sqlMap.executeQueryForList("getIntDetailsForLTD", actMap);
        System.out.println("getIntDetails====================================$$$$$$$$$$$$$$$$" + getIntDetails);
        HashMap hash = null;
        long trans_slno = 0;
        HashMap insertPenal = new HashMap();
        if (getIntDetails != null) {
            for (int i = 0; i < getIntDetails.size(); i++) {
                hash = (HashMap) getIntDetails.get(i);

                insertPenal.put("CURR_MONTH_INT", hash.get("IBAL"));
                insertPenal.put("PRINCIPLE_BAL", hash.get("PBAL"));
                insertPenal.put("PENAL_INT", hash.get("PIBAL"));;
                insertPenal.put("EBAL", hash.get("EBAL"));
                trans_slno = CommonUtil.convertObjToInt(hash.get("TRANS_SLNO"));
                trans_slno++;
                insertPenal.put("TRANS_SLNO", new Long(trans_slno));
                insertPenal.put("TRN_CODE", hash.get("TRN_CODE"));
                insertPenal.put("TRANS_DT", actMap.get("TRANS_DT"));

                insertPenal.put("PROD_ID", hash.get("PROD_ID"));
                insertPenal.put("DAY_END_BALANCE", hash.get("DAY_END_BALANCE"));
            }
        }
        return insertPenal;
    }

    private HashMap insertcredit(HashMap insertmap) throws Exception {

        HashMap inputMap = new HashMap();
        System.out.println("insertmap###vvv#" + insertmap);
        System.out.println("aakkkuuu");
        inputMap.putAll(insertmap);
        double amount = CommonUtil.convertObjToDouble(insertmap.get("TOT_AMT")).doubleValue();
        if (amount < 0) {
            amount = amount * (-1);
        }
        //                if(insertmap.get("LOANPARTICULARS")!=null ){
        double pInt = CommonUtil.convertObjToDouble(insertmap.get("PENAL_INT")).doubleValue();
        double Int = CommonUtil.convertObjToDouble(insertmap.get("CURR_MONTH_INT")).doubleValue();
        double principelBal = CommonUtil.convertObjToDouble(insertmap.get("PRINCIPLE_BAL")).doubleValue();
        double ebal = CommonUtil.convertObjToDouble(insertmap.get("EBAL")).doubleValue();
        double totInt = pInt + Int + ebal;
        double principle = 0;
        double paidPenal = pInt;
        double paidInt = Int;
        double paidebal = ebal;
        if (amount >= totInt) {
            amount -= ebal;
            amount -= pInt;
            amount -= Int;
            paidPenal = 0;
            paidInt = 0;
            paidebal = 0;
            if (amount > 0.0) {
                principelBal -= amount;
                principle = amount;
            }
        } else if (amount >= pInt + ebal) {
            paidPenal = 0;
            paidebal = 0;
            amount -= pInt;

            amount -= ebal;
            if (amount != 0.0) {
                paidInt -= amount;
                Int = amount;
            }
        } else {

            Int = 0;
            pInt = 0;
            ebal = paidebal;

            if (amount > 0.0) {
                if (amount >= paidebal) {
                    paidebal = 0;
                    paidPenal -= amount;
                } else {
                    paidebal -= amount;
                    ebal = amount;

                }
            }
        }
        inputMap.put("ACCOUNTNO", insertmap.get("ACT_NUM"));
        inputMap.put("TRANS_DT", insertmap.get("TRANS_DT"));
        inputMap.put("PROD_ID", insertmap.get("PROD_ID"));
        inputMap.put("BRANCH_CODE", _branchCode);
        inputMap.put("PRINCIPLE", new Double(principle));
        inputMap.put("PBAL", new Double(principelBal));
        inputMap.put("INTEREST", new Double(Int));
        inputMap.put("IBAL", new Double(paidInt));
        inputMap.put("PENAL", new Double(pInt));
        inputMap.put("PIBAL", new Double(paidPenal));
        inputMap.put("EBAL", new Double(paidebal));
        inputMap.put("EXPENSE", new Double(ebal));
        if (CommonUtil.convertObjToStr(insertmap.get("TRN_CODE")).equals("OLG")) {
            inputMap.put("TRN_CODE", new String("OLG"));
        } else {
            inputMap.put("TRN_CODE", new String("C*"));
        }
        inputMap.put("PRINCIPAL_AMOUNT", inputMap.get("PRINCIPLE"));
        inputMap.put("INTEREST_AMOUNT", inputMap.get("INTEREST"));
        inputMap.put("PENUL_INTEREST_AMOUNT", inputMap.get("PENAL"));
        inputMap.put("EFFECTIVE_DT", insertmap.get("INSTALL_DT"));
        inputMap.put("TRANS_SLNO", new Long(CommonUtil.convertObjToInt(insertmap.get("TRANS_SLNO"))));
        inputMap.put("DAY_END_BALANCE", new Double(CommonUtil.convertObjToDouble(insertmap.get("DAY_END_BALANCE")).doubleValue()
                - CommonUtil.convertObjToDouble(insertmap.get("TOT_AMT")).doubleValue()));

        inputMap.put("TRANS_MODE", "TRANSFER");
        System.out.println("inputmap###nn##update" + inputMap);
        sqlMap.executeUpdate("insertintoloanTransAuthDetailsLTD", inputMap);
        System.out.println("getRuleMap######" + inputMap);
        return inputMap;

    }
    //note: if loan or advances excess amount is there immediatly shoude credit

    public HashMap insertExcesscredit(HashMap insertmap, HashMap detailsMap) throws Exception {
        HashMap inputMap = new HashMap();
        double excess_amt = 0;
        double intAmount = 0;
        if (insertmap.containsKey("AMOUNT")) {
            intAmount = CommonUtil.convertObjToDouble(insertmap.get("AMOUNT")).doubleValue();
        }
        if (insertmap.containsKey("PROD_TYPE") && insertmap.get("PROD_TYPE").equals("LOANS")) {
            excess_amt = CommonUtil.convertObjToDouble(detailsMap.get("EXCESS_AMT")).doubleValue();
        } else {
            excess_amt = CommonUtil.convertObjToDouble(detailsMap.get("PBAL")).doubleValue();
            if (excess_amt > 0 || intAmount == 0) {
                inputMap.put("INTEREST", new Double(0));
                inputMap.put("PENAL", new Double(0));
                return inputMap;
            }
        }
        if (excess_amt < 0) {
            excess_amt *= -1;
        }
        long slno = CommonUtil.convertObjToLong(detailsMap.get("TRANS_SLNO"));
        slno++;
        System.out.println("detailsMap####" + detailsMap);
        if (excess_amt > 0) {
            System.out.println("insertmap####" + insertmap);
            inputMap.putAll(insertmap);
            double amount = CommonUtil.convertObjToDouble(insertmap.get("TOT_AMT")).doubleValue();
            if (amount < 0) {
                amount = amount * (-1);
            }
            amount = excess_amt;
            double principelDueBal = 0;
            if (insertmap.containsKey("PROD_TYPE") && insertmap.get("PROD_TYPE").equals("LOANS")) {
                HashMap map = new HashMap();
                map.putAll(insertmap);
                map.putAll(detailsMap);
                principelDueBal = ActualprincipalDue(map);
            }
            //            else
            //                 principelBal=CommonUtil.convertObjToDouble(detailsMap.get("PRINCIPLE_BAL")).doubleValue();
            //                if(insertmap.get("LOANPARTICULARS")!=null ){
            double pInt = CommonUtil.convertObjToDouble(detailsMap.get("PIBAL")).doubleValue();
            double Int = CommonUtil.convertObjToDouble(detailsMap.get("IBAL")).doubleValue();
            double principelBal = CommonUtil.convertObjToDouble(detailsMap.get("PBAL")).doubleValue();
            double ebal = CommonUtil.convertObjToDouble(detailsMap.get("EBAL")).doubleValue();
            double npa_bal = CommonUtil.convertObjToDouble(insertmap.get("NPA_INT_BAL")).doubleValue();
            double npa_Penal_bal = CommonUtil.convertObjToDouble(insertmap.get("NPA_PENAL_BAL")).doubleValue();
            double npatotal = npa_Penal_bal + npa_bal;
            double totInt = pInt + Int + ebal;
            double principle = 0;
            double paidPenal = pInt;
            double paidInt = Int;
            double paidebal = ebal;
            double paid_npa_bal = 0;//npa_bal;
            double paid_npa_penal_bal = 0;//npa_Penal_bal;
            double remain_Excess_Amt = 0;
            if (amount >= totInt) {
                amount -= ebal;
                amount -= pInt;
                amount -= Int;
                paidPenal = 0;
                paidInt = 0;
                paidebal = 0;
                if (amount > 0.0) {
                    if (amount >= principelDueBal) {
                        principle = principelDueBal;
                        principelBal -= principelDueBal;
                        amount -= principelDueBal;
                        remain_Excess_Amt = amount;
                        amount = 0;
                    } else {
                        principle = amount;
                        principelBal -= amount;
                        amount = 0;
                    }
                    System.out.println("principelBal" + principelBal + "principle" + principle + "amount" + amount);

                }
            } else if (amount >= pInt + ebal) {
                paidPenal = 0;
                paidebal = 0;
                amount -= pInt;

                amount -= ebal;
                if (amount > 0.0) {
                    paidInt -= amount;
                    Int = amount;
                }
            } else {
                //                    pInt-=amount;
                Int = 0;
                pInt = 0;
                ebal = paidebal;
                //            amount-=paidebal;
                if (amount > 0.0) {
                    if (amount >= paidebal) {

                        amount -= ebal;
                        paidebal = 0;
                        System.out.println("ebal" + ebal + "paidebal####" + paidebal + "amount" + amount);
                        if (amount > 0) {

                            paidPenal -= amount;
                            pInt = amount;
                            System.out.println("eba123l" + ebal + "paidebal####" + paidebal + "amount" + amount + "paidpenal" + paidPenal + "pInt###" + pInt);
                        }
                    } else {
                        paidebal -= amount;
                        ebal = amount;

                    }
                }
            }
            inputMap.put("PRINCIPAL", new Double(principle));
            inputMap.put("PRINCIPLE", new Double(principle));
            inputMap.put("TODAY_DT", currDt);
            inputMap.put("PBAL", new Double(principelBal));
            inputMap.put("INTEREST", new Double(Int));
            inputMap.put("IBAL", new Double(paidInt));
            inputMap.put("PENAL", new Double(pInt));
            inputMap.put("PIBAL", new Double(paidPenal));
            inputMap.put("EBAL", new Double(paidebal));
            inputMap.put("EXPENSE", new Double(ebal));
            inputMap.put("BRANCH_CODE", _branchCode);
            inputMap.put("TRANSTYPE", "CREDIT");
            inputMap.put("PROD_ID", detailsMap.get("PROD_ID"));
            inputMap.put("NPA_INTEREST", new Double(paid_npa_bal));
            inputMap.put("NPA_INT_BAL", new Double(npa_bal));
            inputMap.put("NPA_PENAL", new Double(paid_npa_penal_bal));
            inputMap.put("NPA_PENAL_BAL", new Double(npa_Penal_bal));
            if (insertmap.get("PROD_TYPE").equals("ADVANCES")) {
                inputMap.put("PBAL", String.valueOf(new Double(remain_Excess_Amt * -1)));
                inputMap.put("EXCESS_AMT", new Double(0));
            } else {
                inputMap.put("EXCESS_AMT", new Double(remain_Excess_Amt));
            }
            inputMap.put("AUTHORIZE_STATUS", "AUTHORIZED");
            inputMap.put("TRN_CODE", new String("C*"));
            inputMap.put("PRINCIPAL_AMOUNT", CommonUtil.convertObjToDouble(inputMap.get("PRINCIPLE")));
            inputMap.put("INTEREST_AMOUNT", CommonUtil.convertObjToDouble(inputMap.get("INTEREST")));
            inputMap.put("PENUL_INTEREST_AMOUNT", CommonUtil.convertObjToDouble(inputMap.get("PENAL")));
            inputMap.put("EFFECTIVE_DT", insertmap.get("INSTALL_DT"));
            inputMap.put("TRANS_SLNO", new Long(slno));
            inputMap.put("TRANS_MODE", "TRANSFER");
            inputMap.put("PARTICULARS", "Credit Balance");
            inputMap.put("TRANS_ID", "");
            inputMap.put("EXCESS_AMT", new Double(0));
            inputMap.put("POSTAGE_CHARGE", new Double(0));
            inputMap.put("POSTAGE_CHARGE_BAL", new Double(0));
            inputMap.put("ARBITARY_CHARGE", new Double(0));
            inputMap.put("ARBITARY_CHARGE_BAL", new Double(0));
            inputMap.put("LEGAL_CHARGE", new Double(0));
            inputMap.put("LEGAL_CHARGE_BAL", new Double(0));
            inputMap.put("INSURANCE_CHARGE", new Double(0));
            inputMap.put("INSURANCE_CHARGE_BAL", new Double(0));
            inputMap.put("EXE_DEGREE", new Double(0));
            inputMap.put("EXE_DEGREE_BAL", new Double(0));
            inputMap.put("MISC_CHARGES", new Double(0));
            inputMap.put("MISC_CHARGES_BAL", new Double(0));
            if (insertmap.containsKey("PROD_TYPE") && insertmap.get("PROD_TYPE").equals("LOANS")) {
                sqlMap.executeUpdate("insertintoloanTransAuthDetails", inputMap);
            } else {
                sqlMap.executeUpdate("insertAuthorizeAdvTransDetails", inputMap);
            }
        }
        return inputMap;

    }

    private double ActualprincipalDue(HashMap loanInstall) throws Exception {
        System.out.println("loanInstall#############" + loanInstall);
        List paidAmt = sqlMap.executeQueryForList("getPaidPrinciple", loanInstall);
        HashMap allInstallmentMap = (HashMap) paidAmt.get(0);
        double totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPLE")).doubleValue();
        paidAmt = sqlMap.executeQueryForList("getIntDetails", loanInstall);
        allInstallmentMap = (HashMap) paidAmt.get(0);
        double totExcessPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("EXCESS_AMT")).doubleValue();
        //        totPrinciple+=totExcessPrinciple;
        List lst = sqlMap.executeQueryForList("getAllLoanInstallment", loanInstall);
        Date inst_dt = null;
        for (int i = 0; i < lst.size(); i++) {
            allInstallmentMap = (HashMap) lst.get(i);
            double instalAmt = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue();
            if (instalAmt <= totPrinciple) {
                totPrinciple -= instalAmt;
                //                        if(lst.size()==1){
                inst_dt = new Date();
                String in_date = CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
                inst_dt = DateUtil.getDateMMDDYYYY(in_date);
                //                        }
            } else {
                inst_dt = new Date();
                String in_date = CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
                inst_dt = DateUtil.getDateMMDDYYYY(in_date);
                totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue() - totPrinciple;
                break;
            }

        }
        Date tempDt = (Date) currDt.clone();
        Date instDt = DateUtil.addDays(inst_dt, 1);
        tempDt.setDate(instDt.getDate());
        tempDt.setMonth(instDt.getMonth());
        tempDt.setYear(instDt.getYear());
        loanInstall.put("FROM_DATE", tempDt);//DateUtil.addDays(inst_dt,1));
        loanInstall.put("TO_DATE", currDt);
        System.out.println("getTotalamount#####" + loanInstall);
        List lst1 = null;
        if (inst_dt != null && (totPrinciple > 0)) {
            lst1 = sqlMap.executeQueryForList("getTotalAmountOverDue", loanInstall);
            System.out.println("listsize####" + lst1);
        }
        double principle = 0;
        if (lst1 != null && lst1.size() > 0) {
            HashMap map = (HashMap) lst1.get(0);
            principle = CommonUtil.convertObjToDouble(map.get("PRINCIPAL_AMOUNT")).doubleValue();
        }
        totPrinciple += principle;
        System.out.println("prinicpal due####" + totPrinciple);
        return totPrinciple;
    }

    private HashMap insertAuthorizeLoanDebit(HashMap authorizeMap) throws Exception {
        System.out.println("insertauthorizeloainnnnndebit####" + authorizeMap);
        //        authorizeMap.put("ACT_NUM",authorizeMap.get("ACCOUNTNO"));
        if (authorizeMap.containsKey("TRN_CODE") && authorizeMap.get("TRN_CODE") != null && (!authorizeMap.get("TRN_CODE").equals("CLEARING_BOUNCED"))) {
            List maxLoanTrans = sqlMap.executeQueryForList("getIntDetailsForLTD", authorizeMap);
            HashMap maxLoanTransmap = null;
            int trans_slno = 1;
            if (maxLoanTrans != null && maxLoanTrans.size() > 0) {
                maxLoanTransmap = (HashMap) maxLoanTrans.get(maxLoanTrans.size() - 1);
                trans_slno = CommonUtil.convertObjToInt(maxLoanTransmap.get("TRANS_SLNO"));
                trans_slno++;
            }

            System.out.println("maxloantransMap##########" + maxLoanTransmap);
            if (maxLoanTransmap != null) {
                double amount = CommonUtil.convertObjToDouble(authorizeMap.get("TOT_AMT")).doubleValue();
                if (amount < 0) {
                    amount *= (-1);
                }
                //            authorizeMap.putAll(maxLoanTransmap);
                authorizeMap.put("TRN_CODE", authorizeMap.get("TRN_CODE"));
                if (authorizeMap.containsKey("TRN_CODE") && authorizeMap.get("TRN_CODE") != null && (authorizeMap.get("TRN_CODE").equals("DP") || authorizeMap.get("TRN_CODE").equals("CLEARING_BOUNCED"))) {
                    authorizeMap.put("PBAL", new Double(amount + CommonUtil.convertObjToDouble(maxLoanTransmap.get("PBAL")).doubleValue()));
                    authorizeMap.put("DAY_END_BALANCE", new Double(CommonUtil.convertObjToDouble(maxLoanTransmap.get("DAY_END_BALANCE")).doubleValue() + amount));
                    authorizeMap.put("PRINCIPAL", new Double(amount));

                    maxLoanTransmap.remove("PBAL");
                    maxLoanTransmap.remove("PRINCIPLE");
                } else {
                    authorizeMap.put("PBAL", new Double(CommonUtil.convertObjToDouble(maxLoanTransmap.get("PBAL")).doubleValue()));
                    authorizeMap.put("PRINCIPLE", new Double(0));
                }
                if (authorizeMap.containsKey("TRN_CODE") && authorizeMap.get("TRN_CODE") != null && authorizeMap.get("TRN_CODE").equals("DI")) {
                    authorizeMap.put("IBAL", new Double(amount + CommonUtil.convertObjToDouble(maxLoanTransmap.get("IBAL")).doubleValue()));
                    authorizeMap.put("DAY_END_BALANCE", new Double(CommonUtil.convertObjToDouble(maxLoanTransmap.get("DAY_END_BALANCE")).doubleValue() + amount));
                    authorizeMap.put("INTEREST", new Double(amount));
                    //
                } else {
                    authorizeMap.put("IBAL", new Double(CommonUtil.convertObjToDouble(maxLoanTransmap.get("IBAL")).doubleValue()));
                    authorizeMap.put("INTEREST", new Double(0));
                }
                if (authorizeMap.containsKey("TRN_CODE") && authorizeMap.get("TRN_CODE") != null && authorizeMap.get("TRN_CODE").equals("DPI")) {
                    authorizeMap.put("PIBAL", new Double(amount + CommonUtil.convertObjToDouble(maxLoanTransmap.get("PIBAL")).doubleValue()));
                    authorizeMap.put("DAY_END_BALANCE", new Double(CommonUtil.convertObjToDouble(maxLoanTransmap.get("DAY_END_BALANCE")).doubleValue() + amount));
                    authorizeMap.put("PENAL", new Double(amount));
                    //
                } else {
                    authorizeMap.put("PIBAL", new Double(CommonUtil.convertObjToDouble(maxLoanTransmap.get("PIBAL")).doubleValue()));
                    authorizeMap.put("PENAL", new Double(0));
                }
                if (authorizeMap.containsKey("TRN_CODE") && authorizeMap.get("TRN_CODE") != null && authorizeMap.get("TRN_CODE").equals("OTHERCHARGES")) {
                    authorizeMap.put("EBAL", new Double(amount + CommonUtil.convertObjToDouble(maxLoanTransmap.get("EBAL")).doubleValue()));
                    authorizeMap.put("DAY_END_BALANCE", new Double(CommonUtil.convertObjToDouble(maxLoanTransmap.get("DAY_END_BALANCE")).doubleValue() + amount));
                    authorizeMap.put("EXPENSE", new Double(amount));
                    //
                } else {
                    authorizeMap.put("EBAL", new Double(CommonUtil.convertObjToDouble(maxLoanTransmap.get("EBAL")).doubleValue()));
                    authorizeMap.put("EXPENSE", new Double(0));
                }
                //
            } else {
                authorizeMap.put("TRN_CODE", authorizeMap.get("TRN_CODE"));
                if (authorizeMap.containsKey("TRN_CODE") && authorizeMap.get("TRN_CODE") != null && (authorizeMap.get("TRN_CODE").equals("DP") || authorizeMap.get("TRN_CODE").equals("CLEARING_BOUNCED"))) {
                    authorizeMap.put("PBAL", new Double(CommonUtil.convertObjToDouble(authorizeMap.get("TOT_AMT")).doubleValue()));
                    authorizeMap.put("PRINCIPAL", new Double(CommonUtil.convertObjToDouble(authorizeMap.get("TOT_AMT")).doubleValue()));
                    authorizeMap.put("DAY_END_BALANCE", new Double(CommonUtil.convertObjToDouble(authorizeMap.get("TOT_AMT")).doubleValue()));
                    authorizeMap.put("TRN_CODE", "DP");
                } else {
                    authorizeMap.put("PBAL", new Double(0));
                    authorizeMap.put("PRINCIPAL", new Double(0));
                }

                if (authorizeMap.containsKey("TRN_CODE") && authorizeMap.get("TRN_CODE") != null && authorizeMap.get("TRN_CODE").equals("OTHERCHARGES")) {
                    authorizeMap.put("EBAL", new Double(CommonUtil.convertObjToDouble(authorizeMap.get("TOT_AMT")).doubleValue()));
                    authorizeMap.put("EXPENSE", new Double(CommonUtil.convertObjToDouble(authorizeMap.get("TOT_AMT")).doubleValue()));
                    authorizeMap.put("DAY_END_BALANCE", new Double(CommonUtil.convertObjToDouble(authorizeMap.get("TOT_AMT")).doubleValue()));
                } else {
                    authorizeMap.put("EBAL", new Double(0));
                    authorizeMap.put("EXPENSE", new Double(0));
                }

            }

            authorizeMap.put("TRANS_SLNO", new Long(trans_slno));
            System.out.println("authorizeMap####befor insert" + authorizeMap);
            sqlMap.executeUpdate("insertLoansDisbursementDetailsCumLoanLTD", authorizeMap);

            authorizeMap.put("AMOUNT", total_Penal);
            sqlMap.executeUpdate("updateadvPbal", authorizeMap);
        }
        return authorizeMap;
    }

    private HashMap calculatePrematureInt(List getPrinInt, HashMap getMap) throws Exception {
        Date stDate = null, endDatepass = null, endDate = null, MonthEndDt = null;;
        System.out.println("getMap####" + getMap);
        HashMap intcalcMap = null;
        HashMap tempMap = null;
        System.out.println("getPrinInt####" + getPrinInt);
        double Bal = 0, prevAmt = 0, totInt = 0;
        HashMap totInterestmap = null;
        //        String stringmonthEnd=CommonUtil.convertObjToStr(getMap.get("END_DT"));
        //        MonthEndDt=DateUtil.getDateMMDDYYYY(stringmonthEnd);
        MonthEndDt = (Date) getMap.get("END_DT");
        for (int x = 0; x < getPrinInt.size(); x++) {

            intcalcMap = new HashMap();

            tempMap = (HashMap) getPrinInt.get(x);
            String trnCode = CommonUtil.convertObjToStr(tempMap.get("TRN_CODE"));
            if ((trnCode.equals("DI") || trnCode.equals("DPI")) && x == 0) {
                endDate = (Date) tempMap.get("TRANS_DT");
                //                if( DateUtil.dateDiff(inst_dt,endDate)>0)
                endDate = DateUtil.addDaysProperFormat(endDate, 1);
                //                else
                //                    endDate=DateUtil.addDaysProperFormat(inst_dt,1);

                tempMap.put("TRANS_DT", endDate);

            } else {
                endDate = (Date) tempMap.get("TRANS_DT");

            }
            Bal = CommonUtil.convertObjToDouble(tempMap.get("DAY_END_BALANCE")).doubleValue();
            System.out.println("stdate##" + stDate + "enddate###" + endDate);

            System.out.println("tempMap#####  ###" + tempMap);
            if (stDate != null && DateUtil.dateDiff(stDate, endDate) > 0) {

                getMap.put("START_DT", stDate);
                if (DateUtil.dateDiff(endDate, MonthEndDt) == 0) {
                    endDatepass = DateUtil.addDaysProperFormat(endDate, 0);
                } else {
                    endDatepass = DateUtil.addDaysProperFormat(endDate, -1);
                }
                double payablePrincipalAmt = 0;

                getMap.put("END_DT", endDatepass);

                intcalcMap.putAll(getMap);
                System.out.println("before  calculateInt##" + intcalcMap);
                if (prevAmt > 0) {
                    getMap.put("PRINCIPAL_BAL", new Double(prevAmt));
                    intcalcMap.putAll(getMap);
                    intcalcMap.put(ROI, ROI);
                    System.out.println("before  calculateInt##" + intcalcMap);
                    intcalcMap.put("NO_ROUND_OFF_INT", "NO_ROUND_OFF_INT");
                    totInterestmap = calculateInterest(intcalcMap);
                    totInt += CommonUtil.convertObjToDouble(totInterestmap.get(INTEREST)).doubleValue();
                    intcalcMap.put(INTEREST, totInterestmap.get(INTEREST));
                    if (getMap.containsKey("LOAN_ACCOUNT_CLOSING")) {
                        insertLoanInteresttmp(intcalcMap);
                    } else {
                        insertLoanInterest(intcalcMap);
                    }
                    intcalcMap.remove(ROI);

                }
            }
            stDate = endDate;
            prevAmt = Bal;
            System.out.println("#### tempMap : " + tempMap);
            System.out.println("#### stDate : " + stDate);
            System.out.println("#### endDate : " + endDate);
            System.out.println("#### prevAmt : " + prevAmt);
            //            System.out.println("#### product : "+product);
            //            System.out.println("#### amount : "+amt);
        }

        System.out.println(endDate + "endDate###" + "checkdatelast###" + MonthEndDt);
        if (stDate != null && endDate != null && DateUtil.dateDiff(endDate, MonthEndDt) > 0 && prevAmt != 0) {//|| totPayableBal>0)

            getMap.put("START_DT", endDate);

            getMap.put("END_DT", MonthEndDt);
            getMap.put("PRINCIPAL_BAL", new Double(prevAmt));//prevAmt+totPayableBal
            intcalcMap = new HashMap();
            intcalcMap.putAll(getMap);
            intcalcMap.put(ROI, ROI);
            totInterestmap = new HashMap();
            System.out.println("before batch calculateInt##" + intcalcMap);
            intcalcMap.put("NO_ROUND_OFF_INT", "NO_ROUND_OFF_INT");
            totInterestmap = calculateInterest(intcalcMap);
            totInt += CommonUtil.convertObjToDouble(totInterestmap.get(INTEREST)).doubleValue();
            intcalcMap.put(INTEREST, totInterestmap.get(INTEREST));
            if (getMap.containsKey("LOAN_ACCOUNT_CLOSING")) {
                insertLoanInteresttmp(intcalcMap);
            } else {
                insertLoanInterest(intcalcMap);
            }

        }

        intcalcMap.putAll(getMap);
        //        intcalcMap.put("PREVAMT",new Double(prevBalPrinciple));         //new Double(prevAmt));
        //        intcalcMap.put("TOT_INT",new Double(totInterest));
        intcalcMap.put("TOT_PENAL_INT", new Double(totInt));
        totInt = roundOffLoanInterest(intcalcMap);
        intcalcMap.put("TOT_PENAL_INT", new Double(totInt));
        if (!getMap.containsKey("LOAN_ACCOUNT_CLOSING")) {
            if (getMap.containsKey("BATCH_PROCESS") && totInt > 0) {
                intcalcMap.put("DEBIT_LOAN_TYPE", "DPI");
                System.out.println("bartchprocessdebitint###" + intcalcMap);
                batchProcessDebitInt(intcalcMap);
            }
        }
        return intcalcMap;
    }

    private double RetrasPectiveInterestCalculation(HashMap productRecord, HashMap passDate, HashMap singleRecord) throws Exception {
        HashMap resultMap = new HashMap();
        List dailyBalOD = null;
        HashMap sumDailyBalMap = new HashMap();
        retraspectiveMap = new HashMap();
        double totProduct = 0;
        double calRetraspectiveInt = 0;
        double calRetraspectiveInactiveInt = 0;
        double remainInterest = 0;
        Date retraspectiveStDt = null;
        //account level last
        Date lastRetraspectiveAppDt = null;
        lastRetraspectiveAppDt = (Date) productRecord.get("RETRASPECTIVE_APP_DT");
        String intToGetFrom = CommonUtil.convertObjToStr(productRecord.get("INT_GET_FROM"));
        productRecord.put("CURR_DT", passDate.get("CURR_DATE"));
        if (intToGetFrom.equals("PROD")) {
            interestList = sqlMap.executeQueryForList("getSelectProductTermLoanRetraspectiveCalcInterestMap", productRecord);
            System.out.println("prodListrepaymentcalculator" + interestList);
        } else if (intToGetFrom.equals("ACT")) {
            interestList = sqlMap.executeQueryForList("getSelectAccountTermLoanRetraspectiveCalcInterestMap", productRecord); //change deposit date to from date
            System.out.println("actListrepaymentcalculator" + interestList);
        }
        //        interestList=sqlMap.executeQueryForList("getSelectProductTermLoanRetraspectiveCalcInterestMap",productRecord);
        System.out.println(passDate + "productRecord" + productRecord + "singleRecord####" + singleRecord);
        if (interestList != null) {
            for (int i = 0; i < interestList.size(); i++) {
                HashMap interestMap = (HashMap) interestList.get(i);
                if (i == 0) {
                    retraspectiveStDt = (Date) interestMap.get("ROI_DATE");
                }
            }
        }
        //actual Calculation Started
        if (retraspectiveStDt != null) {
            if (lastRetraspectiveAppDt == null || DateUtil.dateDiff(lastRetraspectiveAppDt, retraspectiveStDt) > 0) {
                if (interestList != null) {
                    calRetraspectiveInt = finalRetraspectiveCalculation(productRecord, passDate, singleRecord, interestList, retraspectiveStDt);
                    //                for(int i=0;i<interestList.size();i++){
                    //                    HashMap interestMap=(HashMap)interestList.get(i);
                    //                    if(i==0)
                    //                        retraspectiveStDt=(Date)interestMap.get("ROI_DATE");
                    //
                    //                    Date from_dt=(Date)interestMap.get("ROI_DATE");
                    //                    Date to_dt=(Date)interestMap.get("ROI_END_DATE");
                    //                    double intRate=CommonUtil.convertObjToDouble(interestMap.get("ROI")).doubleValue();
                    //                    if(to_dt ==null)
                    //                        to_dt=(Date)productRecord.get("LAST_INT_CALC_DT");
                    //                    singleRecord.put("START_DT",from_dt);
                    //                    singleRecord.put("END_DT",to_dt);
                    //                    if(CommonUtil.convertObjToStr(passDate.get("BEHAVES_LIKE")).equals("OD"))
                    //                        dailyBalOD=sqlMap.executeQueryForList("getSumDayAdvEndBalance", singleRecord);
                    //                    else
                    //                        dailyBalOD=sqlMap.executeQueryForList("getSumDayLoanEndBalance", singleRecord);
                    //                    sumDailyBalMap=new HashMap();
                    //                    if(dailyBalOD!=null)
                    //                        sumDailyBalMap=(HashMap)dailyBalOD.get(0);
                    //                    if(CommonUtil.convertObjToStr(passDate.get("BEHAVES_LIKE")).equals("OD"))
                    //                        totProduct=CommonUtil.convertObjToDouble(sumDailyBalMap.get("AMT")).doubleValue();
                    //                    else if(CommonUtil.convertObjToStr(productRecord.get("INTEREST_TYPE")).equals("FLAT_RATE") && CommonUtil.convertObjToStr(productRecord.get("INTEREST")).equals("SIMPLE"))
                    //                        totProduct=CommonUtil.convertObjToDouble(sumDailyBalMap.get("FLAT_RATE_PRINCIPAL")).doubleValue();
                    //                    else if(CommonUtil.convertObjToStr(productRecord.get("INTEREST")).equals("COMPOUND"))
                    //                        totProduct=CommonUtil.convertObjToDouble(sumDailyBalMap.get("AMT")).doubleValue();
                    //                    else if(CommonUtil.convertObjToStr(productRecord.get("INTEREST")).equals("SIMPLE"))
                    //                        totProduct=CommonUtil.convertObjToDouble(sumDailyBalMap.get("PRINCIPAL")).doubleValue();
                    //                    System.out.println("totProduct####"+totProduct+"intRate"+intRate);
                    //                    if(totProduct<0)
                    //                        totProduct=totProduct*(-1);
                    //                    if(totProduct >0){
                    //                        calRetraspectiveInt+=(double)totProduct*intRate/36500;
                    //                        System.out.println("calRetraspectiveInactiveI   nt####"+(double)totProduct*intRate/36500);
                    //                        System.out.println("calRetraspectiveInactiveI   nt####"+calRetraspectiveInactiveInt);
                    //                    }
                    //                }

                }
                System.out.println(retraspectiveStDt + "calRetraspectiveInt####" + calRetraspectiveInt);
                if (retraspectiveStDt != null) {
                    retraspectiveMap.put("UPDATE_RET_APP_DT", retraspectiveStDt);
                }

                //end
                //inactive calculation Started
                if (retraspectiveStDt != null) {
                    productRecord.put("START_DT", retraspectiveStDt);
                }
                if (intToGetFrom.equals("PROD")) {
                    interestList = sqlMap.executeQueryForList("getSelectProductTermLoanRetraspectiveInactiveCalcInterestMap", productRecord);
                    if (interestList == null || interestList.isEmpty()) {
                        interestList = sqlMap.executeQueryForList("getSelectProductTermLoanRetraspectiveInactiveCalcNullInterestMap", productRecord);
                    }

                } else if (intToGetFrom.equals("ACT")) {
                    interestList = sqlMap.executeQueryForList("getSelectAccountTermLoanRetraspectiveInactiveCalcInterestMap", productRecord);
                    if (interestList == null || interestList.isEmpty()) {
                        interestList = sqlMap.executeQueryForList("getSelectAccountTermLoanRetraspectiveInactiveNullCalcInterestMap", productRecord);
                    }

                }
                System.out.println(passDate + "productRecord" + productRecord + "singleRecord####" + singleRecord);
                //actual Calculation Started
                if (interestList != null) {
                    calRetraspectiveInactiveInt = finalRetraspectiveCalculation(productRecord, passDate, singleRecord, interestList, retraspectiveStDt);

                    //                for(int i=0;i<interestList.size();i++){
                    //                    HashMap interestMap=(HashMap)interestList.get(i);
                    //                    Date from_dt=(Date)interestMap.get("FROM_DT");
                    //                    Date to_dt=(Date)interestMap.get("TO_DT");
                    //                    double intRate=CommonUtil.convertObjToDouble(interestMap.get("INTEREST")).doubleValue();
                    //                    if(to_dt ==null || DateUtil.dateDiff(from_dt,to_dt)<0)
                    //                        to_dt=(Date)productRecord.get("LAST_INT_CALC_DT");
                    //                    singleRecord.put("START_DT",from_dt);
                    //                    singleRecord.put("END_DT",to_dt);
                    //                    if(CommonUtil.convertObjToStr(passDate.get("BEHAVES_LIKE")).equals("OD"))
                    //                        dailyBalOD=sqlMap.executeQueryForList("getSumDayAdvEndBalance", singleRecord);
                    //                    else
                    //                        dailyBalOD=sqlMap.executeQueryForList("getSumDayLoanEndBalance", singleRecord);
                    //                    sumDailyBalMap=new HashMap();
                    //                    if(dailyBalOD!=null)
                    //                        sumDailyBalMap=(HashMap)dailyBalOD.get(0);
                    //                    if(CommonUtil.convertObjToStr(passDate.get("BEHAVES_LIKE")).equals("OD"))
                    //                        totProduct=CommonUtil.convertObjToDouble(sumDailyBalMap.get("AMT")).doubleValue();
                    //                    else if(CommonUtil.convertObjToStr(productRecord.get("INTEREST_TYPE")).equals("FLAT_RATE") && CommonUtil.convertObjToStr(productRecord.get("INTEREST")).equals("SIMPLE"))
                    //                        totProduct=CommonUtil.convertObjToDouble(sumDailyBalMap.get("FLAT_RATE_PRINCIPAL")).doubleValue();
                    //                    else if(CommonUtil.convertObjToStr(productRecord.get("INTEREST")).equals("COMPOUND"))
                    //                        totProduct=CommonUtil.convertObjToDouble(sumDailyBalMap.get("AMT")).doubleValue();
                    //                    else if(CommonUtil.convertObjToStr(productRecord.get("INTEREST")).equals("SIMPLE"))
                    //                        totProduct=CommonUtil.convertObjToDouble(sumDailyBalMap.get("PRINCIPAL")).doubleValue();
                    //                    System.out.println("totProduct####2"+totProduct+"intRate"+intRate);
                    //                    if(totProduct<0)
                    //                        totProduct=totProduct*(-1);
                    //                    if(totProduct !=0){
                    //                        calRetraspectiveInactiveInt+=totProduct*intRate/36500;
                    //                        System.out.println("calRetraspectiveInactiveIntF####"+calRetraspectiveInactiveInt);
                    //                        System.out.println("calRetraspectiveInactiveIntF ####"+totProduct*intRate/36500);
                    //                    }
                    //                }
                    System.out.println("calRetraspectiveInactiveInt####" + calRetraspectiveInactiveInt);
                }
                remainInterest = calRetraspectiveInactiveInt - calRetraspectiveInt;

                //
            }
        }
        calRetraspectiveInactiveInt = 0;
        calRetraspectiveInactiveInt = 0;
        retraspectiveStDt = null;
        System.out.println("remainInterest@@@@@" + remainInterest);
        return remainInterest;
    }

    private double finalRetraspectiveCalculation(HashMap productRecord, HashMap passDate, HashMap singleRecord, List interestList, Date retraspectiveStDt) throws Exception {
        double calRetraspectiveInt = 0;
        List dailyBalOD = null;
        Date from_dt = null;
        HashMap sumDailyBalMap = new HashMap();
        double totProduct = 0;
        if (interestList != null) {
            for (int i = 0; i < interestList.size(); i++) {
                HashMap interestMap = (HashMap) interestList.get(i);
                System.out.println("singleRecordfirst" + singleRecord);
                if (i == 0) {
                    //                    retraspectiveStDt=(Date)interestMap.get("ROI_DATE");
                    from_dt = retraspectiveStDt;
                } else {
                    from_dt = (Date) interestMap.get("ROI_DATE");
                }
                if (DateUtil.dateDiff(from_dt, retraspectiveStDt) > 0) {
                    from_dt = retraspectiveStDt;
                }
                Date to_dt = (Date) interestMap.get("ROI_END_DATE");
                double intRate = CommonUtil.convertObjToDouble(interestMap.get("ROI")).doubleValue();
                if (to_dt == null || (to_dt != null && DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(productRecord.get("LAST_INT_CALC_DT"))), to_dt) > 0)) {
                    to_dt = (Date) productRecord.get("LAST_INT_CALC_DT");
                }
                if (DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(productRecord.get("LAST_INT_CALC_DT"))), from_dt) > 0) {
                    break;
                }
                if (to_dt == null || DateUtil.dateDiff(from_dt, to_dt) <= 0) {
                    to_dt = (Date) productRecord.get("LAST_INT_CALC_DT");
                }
                singleRecord.put("START_DT", from_dt);
                singleRecord.put("END_DT", to_dt);
                if (CommonUtil.convertObjToStr(passDate.get("BEHAVES_LIKE")).equals("OD")) {
                    dailyBalOD = sqlMap.executeQueryForList("getSumDayAdvEndBalance", singleRecord);
                } else {
                    dailyBalOD = sqlMap.executeQueryForList("getSumDayLoanEndBalance", singleRecord);
                }
                sumDailyBalMap = new HashMap();
                if (dailyBalOD != null) {
                    sumDailyBalMap = (HashMap) dailyBalOD.get(0);
                }
                if (CommonUtil.convertObjToStr(passDate.get("BEHAVES_LIKE")).equals("OD")) {
                    totProduct = CommonUtil.convertObjToDouble(sumDailyBalMap.get("AMT")).doubleValue();
                } else if (CommonUtil.convertObjToStr(productRecord.get("INTEREST_TYPE")).equals("FLAT_RATE") && CommonUtil.convertObjToStr(productRecord.get("INTEREST")).equals("SIMPLE")) {
                    totProduct = CommonUtil.convertObjToDouble(sumDailyBalMap.get("FLAT_RATE_PRINCIPAL")).doubleValue();
                } else if (CommonUtil.convertObjToStr(productRecord.get("INTEREST")).equals("COMPOUND")) {
                    totProduct = CommonUtil.convertObjToDouble(sumDailyBalMap.get("AMT")).doubleValue();
                } else if (CommonUtil.convertObjToStr(productRecord.get("INTEREST")).equals("SIMPLE")) {
                    totProduct = CommonUtil.convertObjToDouble(sumDailyBalMap.get("PRINCIPAL")).doubleValue();
                }
                System.out.println("totProduct####" + totProduct + "intRate" + intRate);
                if (totProduct < 0) {
                    totProduct = totProduct * (-1);
                }
                if (totProduct > 0) {
                    calRetraspectiveInt += (double) totProduct * intRate / 36500;
                    System.out.println("calRetraspectiveInactiveI   nt####" + (double) totProduct * intRate / 36500);
                    System.out.println("calRetraspectiveInactiveI   nt####" + calRetraspectiveInt);
                }
                System.out.println("singleRecordlast" + singleRecord);
            }

        }
        return calRetraspectiveInt;
    }

    //mahila nidhi deposit interest calculation
    private HashMap cumalitiveDepositAsAnWhen(HashMap productRecord, HashMap passDate) throws Exception {
        System.out.println("productRecord####  :" + productRecord + "passDate  ####" + passDate);
        HashMap returnmap = new HashMap();
        TdsCalc tdsCalc = new TdsCalc(_branchID);
        Date tdsEndDt = (Date) currDt.clone();
        HashMap passMap = new HashMap();
        Date acct_open_date = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(productRecord.get("ACCT_OPEN_DT")));
        passMap.put("ACT_NUM", productRecord.get("ACCT_NUM"));
        if (passDate != null && passDate.containsKey("DEPOSIT_PREMATURE_CLOSER") && passDate.get("DEPOSIT_PREMATURE_CLOSER") != null) {
            passMap.put("START_DATE", DateUtil.addDays((Date) productRecord.get("ACCT_OPEN_DT"), -1));
        } else {
            passMap.put("START_DATE", productRecord.get("LAST_INT_CALC_DT"));
        }
        if (productRecord.containsKey("END_DT") && productRecord.get("END_DT") != null) {
            passMap.put("CURR_DATE", (Date) productRecord.get("END_DT"));
        } else {
            passMap.put("CURR_DATE", tdsEndDt);
        }
        HashMap hashDailyMap = new HashMap();
        List lst = sqlMap.executeQueryForList("transDetailsForCumulative_TL", passMap);
        //        if(lst !=null && lst.size()>0){
        if (lst == null || lst.size() == 0) {
            hashDailyMap.put("TRANS_DT", passMap.get("START_DATE"));
            hashDailyMap.put("NEXT_DT", DateUtil.addDays(tdsEndDt, -1));
            hashDailyMap.put("AMT", productRecord.get("PRINICIPAL"));
            lst = new ArrayList();
            lst.add(hashDailyMap);
        }
        interestList = getInterestDetails(productRecord, passDate);
        HashMap interestMap = null;
        if (interestList != null && interestList.size() > 0) {
            interestMap = (HashMap) interestList.get(0);
        }
        double roi = CommonUtil.convertObjToDouble(interestMap.get("INTEREST")).doubleValue();
        returnmap.put("ROI", interestMap.get("INTEREST"));
        Date forDiffDt = null;
        double totIntToAcc = 0.0;
        double cumprinciple = 0;
        System.out.println("in side the CUMMULATIVE ");
        double interestAmt = 0.0;
        HashMap where = new HashMap();

        if (passDate.containsKey("DEPOSIT_PREMATURE_CLOSER") && passDate.get("DEPOSIT_PREMATURE_CLOSER") != null) {
            forDiffDt = (Date) productRecord.get("ACCT_OPEN_DT");
            where.put("FROM_DT", productRecord.get("ACCT_OPEN_DT"));
        } else {
            forDiffDt = (Date) productRecord.get("LAST_INT_CALC_DT");
            where.put("FROM_DT", DateUtil.addDays((Date) productRecord.get("LAST_INT_CALC_DT"), 2));
        }
        for (int i = 0; i < lst.size(); i++) {
            hashDailyMap = (HashMap) lst.get(i);
            forDiffDt = (Date) hashDailyMap.get("TRANS_DT");
            if (productRecord.containsKey("MATURITY_DATE") && productRecord.get("MATURITY_DATE") != null) {
                tdsEndDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(productRecord.get("MATURITY_DATE")));
            } else {
                tdsEndDt = (Date) hashDailyMap.get("NEXT_DT");
            }
            System.out.println("TDS End date :" + tdsEndDt);
            if (DateUtil.dateDiff(tdsEndDt, forDiffDt) == 0) {
                tdsEndDt = (Date) currDt.clone();
            }
            double priniciple = CommonUtil.convertObjToDouble(hashDailyMap.get("AMT")).doubleValue();
            cumprinciple = priniciple;
            int intFreq = 90;
            Date tdsStartDt = DateUtil.addDays(forDiffDt, intFreq);//tdsStartDt
            System.out.println("tdsStartDt befor for loop #" + tdsStartDt + "   " + tdsEndDt);
            for (Date calStartDt = tdsStartDt; DateUtil.dateDiff(calStartDt, tdsEndDt) >= 0;
                    calStartDt = DateUtil.addDays(calStartDt, intFreq)) {
                System.out.println("in side the CUMMULATIVE Completed ");
                double period = (double) intFreq / 30;
                System.out.println("priniciple    " + priniciple);
                //            cumprinciple=priniciple+totIntToAcc;
                System.out.println("cumprinciple    " + cumprinciple);
                double interest = 0.0;
                interest = (cumprinciple * period * roi) / 1200;
                interest = interest * 10000;// for four decimal rounding of
                interest = (double) tdsCalc.getNearest((long) (interest * 100), 100) / 100;
                interest = interest / 10000;
                interestAmt = interest;
                totIntToAcc = totIntToAcc + interestAmt;
                cumprinciple = priniciple + totIntToAcc;
                System.out.println("totIntToAcc$$$$$$$$$$$$$$$$$$$$$" + totIntToAcc);
                System.out.println("tdsStartDt inside for loop #" + tdsStartDt + "   " + tdsEndDt);

                forDiffDt = calStartDt;
                System.out.println("tdsStartDt inside for loop #" + forDiffDt + "   " + tdsEndDt);
                System.out.println("in side the CUMMULATIVE Differ calStartDt" + calStartDt + "diffPeriod&&&&&&&&&&&&&&&+" + totIntToAcc);
            }
            System.out.println("tdsStartDt outside for loop #" + forDiffDt + "   " + tdsEndDt);
            System.out.println("cumprinciple    " + cumprinciple);
            if (DateUtil.dateDiff(forDiffDt, tdsEndDt) > 0) {
                System.out.println("in side the CUMMULATIVE Differ ");
                //            long diffPeriod=DateUtil.dateDiff(forDiffDt,tdsEndDt)+1;
                long diffPeriod = DateUtil.dateDiff(forDiffDt, tdsEndDt);
                double diffInt = 0.0;
                priniciple = priniciple + totIntToAcc;
                diffInt = (diffPeriod * cumprinciple * roi) / 36500;
                System.out.println("in side the CUMMULATIVE Differ diffInt" + diffInt + "diffPeriod&&&&&&&&&&&&&&&+" + diffPeriod);
                totIntToAcc = totIntToAcc + diffInt;
            }
            System.out.println("finaltotIntToAcc$$$$$$$$$$$$$$$$$$$$$" + totIntToAcc);
        }
        Rounding rd = new Rounding();
        totIntToAcc = rd.getNearestHigher(totIntToAcc, 1);
        if (acct_open_date != null && DateUtil.dateDiff(acct_open_date, currDt) == 0) {
            totIntToAcc = 0;
        }
        where.put("ACT_NUM", productRecord.get("ACCT_NUM"));
        returnmap.put("INTEREST", new Double(totIntToAcc));
        where.put("TO_DATE", currDt);//singleRecord.get("CURR_DATE"));
        if (passDate.containsKey("DEPOSIT_PREMATURE_CLOSER") || passDate.containsKey("NORMAL_CLOSER")) {
            HashMap paidMap = (HashMap) sqlMap.executeQueryForObject("getPaidPrinciple", where);
            double paidInt = CommonUtil.convertObjToDouble(paidMap.get("INTEREST")).doubleValue();
            double paidPenalInt = CommonUtil.convertObjToDouble(paidMap.get("PENAL")).doubleValue();
            totIntToAcc = totIntToAcc - paidInt;
        }
        //                        paidPenalInt=penal-paidPenalInt;
        returnmap.put("FINAL_INT", new Double(totIntToAcc));
        returnmap.put("INTEREST", new Double(totIntToAcc));
        //        returnmap.put("INTEREST",new Double(totIntToAcc));//INTEREST
        returnmap.put("LOAN_CLOSING_PENAL_INT", new Double(0));
        returnmap.put("ASSET_STATUS", productRecord.get("ASSET_STATUS"));

        System.out.println("returnmap@@@@###" + returnmap);
        //        }
        return returnmap;
    }
    //    public long getNearest(long number,long roundingFactor)  {
    //        long roundingFactorOdd = roundingFactor;
    //        if ((roundingFactor%2) != 0)
    //            roundingFactorOdd +=1;
    //        long mod = number%roundingFactor;
    //        if ((mod <= (roundingFactor/2)) || (mod <= (roundingFactorOdd/2)))
    //            return lower(number,roundingFactor);
    //        else
    //            return higher(number,roundingFactor);
    //    }

    public static void main(String[] arg) {
        //
        //          try{
        //              TaskHeader hd = new TaskHeader();
        //                hd.setBranchID("Bran");
        //                hd.setUserID("sysadmin");
        //                UserCheckTask ft = new UserCheckTask();
        //   InterestCalculationTask ict=new InterestCalculationTask(hd);
        //                System.out.println (ft.executeTask().getStatus());
        //                System.out.println("BatchConstants.ERROR :" + BatchConstants.ERROR);
        //   }catch(Exception E){
        //   E.printStackTrace();
        //  }
    }

    public Date getProperFormatDate(Object obj) {
        Date returnDt = null;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = (Date) obj;
            returnDt = (Date) currDt.clone();
            returnDt.setDate(tempDt.getDate());
            returnDt.setMonth(tempDt.getMonth());
            returnDt.setYear(tempDt.getYear());
        }
        return returnDt;
    }

    double valuedateInterestCalculation(HashMap productRecord, HashMap passDate, HashMap paramMap, String intGetFrom) throws Exception {
        System.out.print("productRecord valuedateInterestCalculation" + productRecord + "passDate####" + passDate + "paramMap####" + paramMap + "intGetFrom###" + intGetFrom);
        double valuedateInterestAmt = 0, tmpInterestamt = 0;
        HashMap hashDailyMap = new HashMap();
        HashMap valudateMap = new HashMap();
        List lst = (List) sqlMap.executeQueryForList("selectTransValuedateInterest", paramMap);

        if (lst != null && lst.size() > 0) {
            for (int i = 0; i < lst.size(); i++) {
                valudateMap = (HashMap) lst.get(i);

                hashDailyMap.put("ACCT_NUM", productRecord.get("ACCT_NUM"));
                hashDailyMap.put("START_DT", valudateMap.get("VALUE_DT"));
                hashDailyMap.put("END_DT", DateUtil.addDays((Date) valudateMap.get("TRANS_DT"), -1));
                hashDailyMap.put("AMOUNT", valudateMap.get("AMOUNT"));
                hashDailyMap.put("AMT", valudateMap.get("AMOUNT"));
                hashDailyMap.put("PROD_ID", productRecord.get("PROD_ID"));
                hashDailyMap.put("CUST_ID", productRecord.get("CUST_ID"));
                hashDailyMap.put("PRINCIPAL_BAL", valudateMap.get("AMOUNT"));
                if (CommonUtil.convertObjToStr(valudateMap.get("TRANS_TYPE")).equals("DEBIT")) {
                    hashDailyMap.put("VALUEDATE", "VALUEDATE_DR");
                } else {
                    hashDailyMap.put("VALUEDATE", "VALUEDATE_CR");
                }

                tmpInterestamt = calculateFixedAndFloatingInterest(productRecord, hashDailyMap, passDate, intGetFrom);

                if (CommonUtil.convertObjToStr(valudateMap.get("TRANS_TYPE")).equals("DEBIT")) {
                    valuedateInterestAmt += tmpInterestamt;
                } else {
                    valuedateInterestAmt -= tmpInterestamt;
                }

                //END 
            }
        }
        System.out.println("valuedateInterestAmt" + valuedateInterestAmt);
        return valuedateInterestAmt;

    }

    double calculateFixedAndFloatingInterest(HashMap productRecord, HashMap hashDailyMap, HashMap passDate, String intGetFrom) throws Exception {
        HashMap resultInt = new HashMap();
        double totInterest = 0;
        if (productRecord.containsKey("INTEREST_TYPE") && productRecord.get("INTEREST_TYPE").equals("FLOATING_RATE")) {
            HashMap interestMap = new HashMap();
            Date nextroidt = null;
            boolean dayendOver = false;
            interestMap.put("FROM_DATE", productRecord.get("FROM_DATE"));   //hashDailyMap.get("START_DT"));
            interestMap.put("TO_DATE", productRecord.get("TO_DATE"));   //hashDailyMap.get("END_DT"));
            interestMap.put("PROD_ID", passDate.get("PROD_ID"));
            interestMap.put("CATEGORY_ID", productRecord.get("CATEGORY_ID"));
            interestMap.put("AMOUNT", hashDailyMap.get("AMOUNT"));
            interestMap.put("DAYENDSTDT", DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hashDailyMap.get("START_DT"))));
            interestMap.put("DAYENDEDDT", DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hashDailyMap.get("END_DT"))));
            interestMap.put("ACCT_NUM", hashDailyMap.get("ACCT_NUM"));
            System.out.println("interestMap$$$$$$" + interestMap);
            Date start_dt = (Date) hashDailyMap.get("START_DT");
            Date dayEndBalanceDt = (Date) hashDailyMap.get("END_DT");
            List resultInterstRate = null;
            if (intGetFrom.equals("PROD")) {
                resultInterstRate = sqlMap.executeQueryForList("getSelectProductTermLoanFlotingInterestMap", interestMap);
            } else {
                resultInterstRate = (List) sqlMap.executeQueryForList("getSelectAccountTermLoanFlotingInterestMap", interestMap);
            }
            System.out.println("resultInterstRate1$$$$$$" + resultInterstRate);

            if (resultInterstRate == null || resultInterstRate.isEmpty()) {
                if (intGetFrom.equals("PROD")) {
                    resultInterstRate = sqlMap.executeQueryForList("getSelectProductDayEndTermLoanInterestMap", interestMap);
                } else {
                    resultInterstRate = (List) sqlMap.executeQueryForList("getSelectAccountTermLoanDayEndInterestMap", interestMap);
                }
            }
            System.out.println("resultInterstRate@@@@!!!!!!" + resultInterstRate);
            if (resultInterstRate != null && resultInterstRate.size() > 0) {
                for (int k = 0; k < resultInterstRate.size(); k++) {
                    HashMap interestResultMap = (HashMap) resultInterstRate.get(k);
                    Date roifrom_dt = (Date) interestResultMap.get("FROM_DT");
                    Date roito_dt = (Date) interestResultMap.get("TO_DT");
                    if (DateUtil.dateDiff(start_dt, roifrom_dt) > 0) {
                        if (intGetFrom.equals("PROD")) {
                            resultInterstRate = sqlMap.executeQueryForList("getSelectProductDayEndTermLoanInterestMap", interestMap);
                        } else {
                            resultInterstRate = (List) sqlMap.executeQueryForList("getSelectAccountTermLoanDayEndInterestMap", interestMap);
                        }
                        System.out.println("resultInterstRate!!!!!!" + resultInterstRate);
                        if (resultInterstRate != null && resultInterstRate.size() > 0) {
                            interestResultMap = (HashMap) resultInterstRate.get(0);
                            roifrom_dt = (Date) interestResultMap.get("FROM_DT");
                            roito_dt = (Date) interestResultMap.get("TO_DT");
                        } else {
                            return 0;
                        }
                    }
                    if (DateUtil.dateDiff(start_dt, roifrom_dt) <= 0 && (roito_dt == null || DateUtil.dateDiff(roito_dt, (Date) hashDailyMap.get("END_DT")) <= 0)) {
                        interestList.clear();
                        interestList.add(resultInterstRate.get(k));
                        hashDailyMap.put("NO_ROUND_OFF_INT", "NO_ROUND_OFF_INT");
                        resultInt = calculateInterest(hashDailyMap);
                        if (resultInt != null) {
                            hashDailyMap.put(InterestCalculationConstants.INTEREST, resultInt.get(InterestCalculationConstants.INTEREST));
                            if (resultInt.containsKey("SUBSIDY_AMT")) {
                                hashDailyMap.put("SUBSIDY_AMT", resultInt.get("SUBSIDY_AMT"));
                                hashDailyMap.put("SUBSIDY_INT", resultInt.get("SUBSIDY_INT"));
                                hashDailyMap.put("TOT_INT_WTSUBSIDY", resultInt.get("TOT_INT_WTSUBSIDY"));
                                hashDailyMap.put("CUST_RATE_INT", resultInt.get("CUST_RATE_INT"));
                                hashDailyMap.put("CUST_INT_AMT", resultInt.get("CUST_INT_AMT"));
                                hashDailyMap.put("SUBSIDY_LIST", resultInt.get("SUBSIDY_LIST"));
                                hashDailyMap.put("ROI", resultInt.get("ROI"));
                            }
                            if (passDate.containsKey("LOAN_ACCOUNT_CLOSING")) {
                                insertLoanInteresttmp(hashDailyMap);
                            } else {
                                insertLoanInterest(hashDailyMap);
                            }
                            totInterest += CommonUtil.convertObjToDouble(resultInt.get(InterestCalculationConstants.INTEREST)).doubleValue();
                        }
                        System.out.println("resultInt1  $$$$####" + resultInt + " totInterest1####" + totInterest);
                    } else {
                        do {
                            if (roito_dt != null && DateUtil.dateDiff(dayEndBalanceDt, roito_dt) <= 0) {
                                hashDailyMap.put("END_DT", roito_dt);
                            } else {
                                hashDailyMap.put("END_DT", dayEndBalanceDt);
                                dayendOver = true;
                            }
                            if (resultInterstRate != null && resultInterstRate.size() > 0) {
                                //interestList.add(resultInterstRate.get(0));
                                interestList.clear();
                                interestList.add(resultInterstRate.get(k));
                            }
                            hashDailyMap.put("NO_ROUND_OFF_INT", "NO_ROUND_OFF_INT");
                            resultInt = calculateInterest(hashDailyMap);
                            if (resultInt != null) {
                                hashDailyMap.put(InterestCalculationConstants.INTEREST, resultInt.get(InterestCalculationConstants.INTEREST));
                                if (resultInt.containsKey("SUBSIDY_AMT")) {
                                    hashDailyMap.put("SUBSIDY_AMT", resultInt.get("SUBSIDY_AMT"));
                                    hashDailyMap.put("SUBSIDY_INT", resultInt.get("SUBSIDY_INT"));
                                    hashDailyMap.put("TOT_INT_WTSUBSIDY", resultInt.get("TOT_INT_WTSUBSIDY"));
                                    hashDailyMap.put("CUST_RATE_INT", resultInt.get("CUST_RATE_INT"));
                                    hashDailyMap.put("CUST_INT_AMT", resultInt.get("CUST_INT_AMT"));
                                    hashDailyMap.put("SUBSIDY_LIST", resultInt.get("SUBSIDY_LIST"));
                                    hashDailyMap.put("ROI", resultInt.get("ROI"));
                                }
                                if (passDate.containsKey("LOAN_ACCOUNT_CLOSING")) {

                                    insertLoanInteresttmp(hashDailyMap);
                                } else {
                                    insertLoanInterest(hashDailyMap);
                                }
                                totInterest += CommonUtil.convertObjToDouble(resultInt.get(InterestCalculationConstants.INTEREST)).doubleValue();
                            }
                            System.out.println("resultInt  $$$$####" + resultInt + " totInterest2####" + totInterest);
                            if (roito_dt != null) {
                                roifrom_dt = (Date) DateUtil.addDays(roito_dt, 1);
                            }
                            hashDailyMap.put("START_DT", roifrom_dt);
                            hashDailyMap.put("END_DT", dayEndBalanceDt);
                            interestMap.put("DAYENDSTDT", hashDailyMap.get("START_DT"));
                            interestMap.put("DAYENDEDDT", hashDailyMap.get("END_DT"));
                            System.out.println("interestMap$$$###" + interestMap);
                            if (intGetFrom.equals("PROD")) {
                                resultInterstRate = sqlMap.executeQueryForList("getSelectProductDayEndTermLoanInterestMap", interestMap);
                            } else {
                                resultInterstRate = sqlMap.executeQueryForList("getSelectAccountTermLoanDayEndInterestMap", interestMap);
                            }
                            System.out.println("resultInterstRate22@@@@" + resultInterstRate);
                            if (resultInterstRate != null && resultInterstRate.size() > 0) {
                                interestResultMap = (HashMap) resultInterstRate.get(0);
                                roifrom_dt = (Date) interestResultMap.get("FROM_DT");
                                if (interestResultMap.containsKey("TO_DT") && interestResultMap.get("TO_DT") != null) {
                                    roito_dt = (Date) interestResultMap.get("TO_DT");
                                } else {
                                    roito_dt = null;
                                }
                            } else {
                                roito_dt = null;
                            }
                            hashDailyMap.put("START_DT", roifrom_dt);
                            System.out.println("roifromdt@@@@" + roifrom_dt + "roito_dt" + roito_dt);
                        } while (DateUtil.dateDiff(roifrom_dt, dayEndBalanceDt) > 0 && dayendOver == false);
                        dayendOver = false;
                    }

                }
            }

        } else {
            hashDailyMap.put("NO_ROUND_OFF_INT", "NO_ROUND_OFF_INT");
            resultInt = calculateInterest(hashDailyMap);
            System.out.println("resultInterest####" + resultInt);
            hashDailyMap.put(InterestCalculationConstants.INTEREST, resultInt.get(InterestCalculationConstants.INTEREST));
            if (resultInt.containsKey("SUBSIDY_AMT")) {
                hashDailyMap.put("SUBSIDY_AMT", resultInt.get("SUBSIDY_AMT"));
                hashDailyMap.put("SUBSIDY_INT", resultInt.get("SUBSIDY_INT"));
                hashDailyMap.put("TOT_INT_WTSUBSIDY", resultInt.get("TOT_INT_WTSUBSIDY"));
                hashDailyMap.put("CUST_RATE_INT", resultInt.get("CUST_RATE_INT"));
                hashDailyMap.put("CUST_INT_AMT", resultInt.get("CUST_INT_AMT"));
                hashDailyMap.put("SUBSIDY_LIST", resultInt.get("SUBSIDY_LIST"));
                hashDailyMap.put("ROI", resultInt.get("ROI"));
            }
            if (passDate.containsKey("LOAN_ACCOUNT_CLOSING")) {

                insertLoanInteresttmp(hashDailyMap);
            } else {
                insertLoanInterest(hashDailyMap);
            }
            totInterest += CommonUtil.convertObjToDouble(resultInt.get(InterestCalculationConstants.INTEREST)).doubleValue();
        }

        return totInterest;
    }

    public HashMap interestCalc(HashMap passDate) throws Exception {
        HashMap returnMap = new HashMap();
        HashMap penalreturnMap = null;
        System.out.println("interestCalc" + passDate);
        double calculatePenal = 0;
        HashMap categoryMap = new HashMap();
        HashMap penalMap = new HashMap();
        Date fromDate = null, toDate = null, tempCurrDt = null;
        double paidInt = 0;
        //Added By Revathi       To Avoid Closed Loan Accounts Should not Calculate Interest and Penal ref by Abi 18-july-2014
        HashMap checkActMap = new HashMap();
        checkActMap.put("ACCOUNTNO", CommonUtil.convertObjToStr(passDate.get("ACT_NUM")));
        checkActMap.put("FROM_ENQUIRY_SCREEN", "FROM_ENQUIRY_SCREEN");
        List closedList = sqlMap.executeQueryForList("getAccountClosingStatus", checkActMap);
        if (closedList != null && closedList.size() > 0) {
            return returnMap;
        }
        returnMap = interestCalcTermLoanAD((HashMap) passDate.clone());
        if (CommonConstants.BANK_TYPE.equals("DCCB")) {
            double interest = CommonUtil.convertObjToDouble(returnMap.get("INTEREST"));
            double penalInterest = CommonUtil.convertObjToDouble(returnMap.get("LOAN_CLOSING_PENAL_INT"));
            List lst = sqlMap.executeQueryForList("getAllClosedLoanRecord", passDate);
            if (lst != null && lst.size() > 0) {
                categoryMap = (HashMap) lst.get(0);
                if (CommonUtil.convertObjToStr(categoryMap.get("AUTHORIZE_REMARK")).equals("GOLD_LOAN")) {//&& CommonUtil.convertObjToInt(categoryMap.get("NO_INSTALL"))==1 sri nath sir told all gold loan applicable for new type penal interest calculation
                    fromDate = (Date) DateUtil.addDays((Date) categoryMap.get("ACCT_OPEN_DT"), -1);
                    double gracePeriod = CommonUtil.convertObjToDouble(categoryMap.get("GRACE_PERIOD_PENAL")).doubleValue();
                    //Added By Suresh  04-Aug-2014 Gold Loan Penal Calculation Should Calculate, If Last_Int_Calc_Dt to Current_Date Diff>180 (180 is Product Level Grace Period Parameter)
                    //(GOLD_LOAN_PENAL)-START
                    Date lastIntCalcDt = null;
                    Date lastInterestCalcDt = null;
                    if (passDate.containsKey("LAST_INT_CALC_DT") && passDate.get("LAST_INT_CALC_DT") != null) {
                        categoryMap.put("LAST_INT_CALC_DT", getProperFormatDate(DateUtil.addDays((Date) passDate.get("LAST_INT_CALC_DT"), -1)));
                    } else if (passDate.get("ONLINE_DATE") != null && categoryMap.get("ACCT_OPEN_DT") != null) {
                        if (DateUtil.dateDiff((Date) passDate.get("ONLINE_DATE"), (Date) categoryMap.get("ACCT_OPEN_DT")) > 0) {
                            categoryMap.put("LAST_INT_CALC_DT", getProperFormatDate(DateUtil.addDays((Date) categoryMap.get("ACCT_OPEN_DT"), -1)));
                        } else {
                            categoryMap.put("LAST_INT_CALC_DT", getProperFormatDate(DateUtil.addDays((Date) passDate.get("ONLINE_DATE"), -1)));
                        }
                    }
                    lastIntCalcDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(categoryMap.get("LAST_INT_CALC_DT")));
                    lastInterestCalcDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(categoryMap.get("LAST_INT_CALC_DT")));
                    System.out.println("interestCalc PenalGracePeriod : " + gracePeriod);
                    System.out.println("interestCalc lastInterestCalcDt : " + lastInterestCalcDt);
                    lastInterestCalcDt = DateUtil.addDays((Date) lastInterestCalcDt, (int) gracePeriod);
                    System.out.println("interestCalc DIFFERENT DAYS (Last_Int_Calc_Dt to Current_Dt): " + ((int) DateUtil.dateDiff(lastIntCalcDt, currDt) - 1) + " (days)");
                    if (DateUtil.dateDiff(lastInterestCalcDt, currDt) > 0) {
                        System.out.println("interestCalc PENAL CALCULATION YES ");
                        passDate.put("LAST_INT_CALC_DT", (Date) lastIntCalcDt);
                        passDate.put("CURR_DATE", currDt);
                        passDate.put("PRODUCT_RECORD_PENAL", lst);
                        passDate.put("PRODUCT_RECORD", "PRODUCT_RECORD");
                        System.out.println("interestCalc BEFORE CALCULATE PENAL INTEREST : " + passDate);
                        penalMap = interestCalcTermLoanAD(passDate);
                    } else {
                        System.out.println("interestCalc PENAL CALCULATION NO ");
                        penalMap = null;
                    }
                    System.out.println("interestCalc PENAL MAP   : " + penalMap);
                    if (penalMap != null && penalMap.size() > 0) {
                        penalInterest = CommonUtil.convertObjToDouble(penalMap.get("INTEREST")).doubleValue();
                        returnMap.put("TOT_PENAL_INT", new Double(penalInterest));
                        returnMap.put("LOAN_CLOSING_PENAL_INT", new Double(penalInterest));
                    }
                }
            }
        }
        System.out.println("######### return interestCalc() returnMap : " + returnMap);
        return returnMap;
    }
}
