/*
 * DepositIntTask.java
 *
 * Created on February 15, 2005, 12:43 PM
 */
package com.see.truetransact.serverside.batchprocess.task.operativeaccount.interest;

import com.see.truetransact.serverside.batchprocess.task.Task;
import com.see.truetransact.serverside.batchprocess.task.TaskStatus;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.batchprocess.BatchConstants;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.transferobject.investments.InvestmentsTransTO;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.commonutil.interestcalc.Rounding;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.LocaleConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.TTException;

import com.see.truetransact.transferobject.batchprocess.interest.*;

import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.serverside.transaction.transfer.*;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.common.interest.InterestTaskRunner;
import com.see.truetransact.serverside.remittance.RemittanceIssueDAO;

import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.remittance.RemittanceIssueTO;
import com.see.truetransact.serverside.tds.tdscalc.TdsCalc;
import java.sql.SQLException;
import java.util.List;
import java.util.Date;
import java.text.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import com.see.truetransact.serverside.investments.InvestmentsTransDAO;
import com.see.truetransact.serverside.batchprocess.task.authorizechk.InterestCalculationTask;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.serverside.transaction.cash.CashTransactionDAO;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import com.see.truetransact.serverside.common.viewall.SelectAllDAO;
import com.see.truetransact.transferobject.mdsapplication.mdsreceiptentry.MDSReceiptEntryTO;
import com.see.truetransact.serverside.mdsapplication.mdsreceiptentry.MDSReceiptEntryDAO;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 152721
 */
public class DepositIntTask extends Task {

    private static SqlMap sqlMap = null;
    private TransferDAO transferDAO = new TransferDAO();
    private String branch = "BRAN";
    private int taskSelected = 0;
    private final int BATCHPROCESS = 1;
    private final int OBCODE = 2;
    private final int INSERTDATA = 3;
    private final String SCREEN = "INTEREST";
    private final String MODULE = "OPERATIVE";
    private HashMap paramMap;
    private HashMap cashToTransferMap;
    private HashMap interestMap;
    private HashMap recurringMap;
    private ArrayList recdList;
    private ArrayList loanList;
    private TaskStatus status = null;
    private boolean isTransaction = true;
    private Date checkThisCDate = null;
    Date nextCalcDate = null;
    Date cummCalcApplDt = null;
    private String dayEndType;
    private String actBranch;
    private Date currentDate;
    private List branchList;
    private String taskLable;
    private MDSReceiptEntryDAO mdsReceiptEntryDAO = null;
    private MDSReceiptEntryTO mdsReceiptEntryTO = null;
    private LinkedHashMap transactionDetailsTO;
    private LinkedHashMap deletedTransactionDetailsTO;
    private LinkedHashMap allowedTransactionDetailsTO;
    private final String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs";
    private String strCashId="";
    String bhvsLike="";
    String slab="";
    private final static double Avg_Millis_Per_Month=365.24*24*60*60*1000 / 12;
    double rateOfInt=0.0;
    private String  generateSingleTransId ="";
    private boolean prodTDSDeduction = false; // Added by nithya on 06-02-2020 for KD-1090
    /** Creates a new instance of DepositIntTask */
    public DepositIntTask(TaskHeader header) throws Exception {
     
        header.setProductType(TransactionFactory.DEPOSITS); //because it is a Term Deposit
        header.setTransactionType(CommonConstants.CREDIT); //because it is a Term Deposit
        setHeader(header);
        branch = header.getBranchID();
        initializeTaskObj(header.getTaskParam());
        isTransaction = true;
    }

    /**
     * Creates a new instance of DepositIntTask
     */
    public DepositIntTask(TaskHeader header, boolean isTransactionEnabled) throws Exception {
        setHeader(header);
        branch = header.getBranchID();
        initializeTaskObj(header.getTaskParam());
        isTransaction = isTransactionEnabled;
    }

    /**
     * Creates a new instance of DepositIntTask
     */
    public DepositIntTask(HashMap paramMap) throws Exception {
        this.paramMap = paramMap;
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
        branch = CommonUtil.convertObjToStr(paramMap.get(CommonConstants.BRANCH_ID));
        isTransaction = new Boolean(CommonUtil.convertObjToStr(paramMap.get("DO_TRANSACTION"))).booleanValue();
        TaskHeader header = new TaskHeader();
        header.setTransactionType("CREDIT");
        setHeader(header);
        if (paramMap.containsKey("CASHTOTRANSFER") && paramMap.get("CASHTOTRANSFER") != null) {
            if (cashToTransferMap == null) {
                cashToTransferMap = new HashMap();
            }
            cashToTransferMap = (HashMap) paramMap.get("CASHTOTRANSFER");
        }
        List listData = ServerUtil.executeQuery("getCashierAuth", new HashMap());
        HashMap map1 = (HashMap) listData.get(0);
            strCashId = getCashUniqueId();
    }

    private String getCashUniqueId() throws Exception {
        //  final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "CASH_UNIQUE_ID");
        HashMap map = generateID();
        return (String) map.get(CommonConstants.DATA);
    }

    public HashMap generateID() {
        HashMap hash = null, result = null;
        try {
            String mapName = "getCurrentID";
            HashMap where = new HashMap();
            where.put("ID_KEY", "CASH_UNIQUE_ID"); //Here u have to pass BORROW_ID or something else
            List list = null;
            sqlMap.executeUpdate("updateIDGenerated", where);  // This update statement just updates curr_value=curr_value+1
            list = (List) sqlMap.executeQueryForList(mapName, where);  // This will get u the updated curr_value, prefix and length
            if (list.size() > 0) {
                hash = (HashMap) list.get(0);
                String strPrefix = "", strLen = "";

                // Prefix for the ID.
                if (hash.containsKey("PREFIX")) {
                    strPrefix = (String) hash.get("PREFIX");
                    if (strPrefix == null || strPrefix.trim().length() == 0) {
                        strPrefix = "";
                    }
                }

                // Maximum Length for the ID
                int len = 10;
                if (hash.containsKey("ID_LENGTH")) {
                    strLen = String.valueOf(hash.get("ID_LENGTH"));
                    if (strLen == null || strLen.trim().length() == 0) {
                        len = 10;
                    } else {
                        len = Integer.parseInt(strLen.trim());
                    }
                }

                int numFrom = strPrefix.trim().length();

                String newID = String.valueOf(hash.get("CURR_VALUE"));

                // Number Part of the String and incrementing 1 (ex. only 00085 from OGGOT00085)
                result = new HashMap();
                String genID = strPrefix.toUpperCase() + CommonUtil.lpad(newID, len - numFrom, '0');
                result.put(CommonConstants.DATA, genID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private Date setProperDtFormat(Date dt) {
        Date tempDt = (Date) currentDate.clone();
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }

    public HashMap calcMDS(String chittal_No, String schemeName, String prodType) throws Exception {
        HashMap dataMap = new HashMap();
        HashMap whereMap = new HashMap();
        HashMap productMap = new HashMap();
        HashMap installmentMap = new HashMap();
        String chittalNo = "";
        String subNo = "";
        if (chittal_No.indexOf("_") != -1) {
            chittalNo = chittal_No.substring(0, chittal_No.indexOf("_"));
            subNo = chittal_No.substring(chittal_No.indexOf("_") + 1, chittal_No.length());
        }
        dataMap.put("CHITTAL_NO", chittal_No);
        dataMap.put("SCHEME_NAME", schemeName);
        double insAmt = 0.0;
        long pendingInst = 0;
        int divNo = 0;
        long insDueAmt = 0;
        int noOfInsPaid = 0;
        int instDay = 1;
        boolean prized = false;
        int curInsNo = 0;
        Date instDate = null;
        boolean bonusAvailabe = true;
        ArrayList penalList = new ArrayList();
        ArrayList bonusList = new ArrayList();
        ArrayList discountList = new ArrayList();

        long noOfInstPay = 0;
        int totIns = 0;
        Date startDate = null;
        Date insDate = null;
        int startMonth = 0;
        int insMonth = 0;
        whereMap.put("SCHEME_NAME", dataMap.get("SCHEME_NAME"));
        whereMap.put("CHITTAL_NO", chittalNo);
        whereMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
        List lst = sqlMap.executeQueryForList("getSelectSchemeAcctHead", whereMap);
        String bonusFirstInst = "";
        if (lst != null && lst.size() > 0) {
            productMap = (HashMap) lst.get(0);
            insAmt = CommonUtil.convertObjToDouble(productMap.get("INSTALLMENT_AMOUNT")).doubleValue();
            totIns = CommonUtil.convertObjToInt(productMap.get("NO_OF_INSTALLMENTS"));
            startDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(productMap.get("SCHEME_START_DT")));
            insDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(productMap.get("SCHEME_START_DT")));
            startMonth = insDate.getMonth();
            bonusFirstInst = CommonUtil.convertObjToStr(productMap.get("ADVANCE_COLLECTION"));
        }
        Date endDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(productMap.get("SCHEME_END_DT")));
        int startNoForPenal = 0;
        int firstInst_No = -1;
        int addNo = 1;
        if (bonusFirstInst.equals("Y")) {
            startNoForPenal = 1;
            addNo = 0;
            firstInst_No = 0;
        }
        List insList = sqlMap.executeQueryForList("getNoOfInstalmentsPaid", whereMap);
        if (insList != null && insList.size() > 0) {
            whereMap = (HashMap) insList.get(0);
            noOfInsPaid = CommonUtil.convertObjToInt(whereMap.get("NO_OF_INST"));
        }
        HashMap chittalMap = new HashMap();
        chittalMap.put("CHITTAL_NO", chittalNo);
        chittalMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
        List chitLst = sqlMap.executeQueryForList("getSelctApplnReceiptDetails", chittalMap);
        if (chitLst != null && chitLst.size() > 0) {
            chittalMap = (HashMap) chitLst.get(0);
            instDay = CommonUtil.convertObjToInt(chittalMap.get("INSTALLMENT_DAY"));
            divNo = CommonUtil.convertObjToInt(chittalMap.get("DIVISION_NO"));
        }
        HashMap insDateMap = new HashMap();
        insDateMap.put("DIVISION_NO", CommonUtil.convertObjToInt(String.valueOf(divNo)));
        insDateMap.put("SCHEME_NAME", dataMap.get("SCHEME_NAME"));
        insDateMap.put("CURR_DATE", setProperDtFormat(currentDate));
        insDateMap.put("ADD_MONTHS", CommonUtil.convertObjToInt("-1"));
        List insDateLst = sqlMap.executeQueryForList("getMDSCurrentInsDate", insDateMap);
        if (insDateLst != null && insDateLst.size() > 0) {
            insDateMap = (HashMap) insDateLst.get(0);
            curInsNo = CommonUtil.convertObjToInt(insDateMap.get("INSTALLMENT_NO"));
            pendingInst = curInsNo - noOfInsPaid;
            if (pendingInst < 0) {
                pendingInst = 0;
            }
            insMonth = startMonth + curInsNo;
            insDate.setMonth(insMonth);
            if (pendingInst > 0) {
                noOfInstPay = pendingInst;
            }

        }
        if (noOfInstPay > 0) {
            HashMap prizedMap = new HashMap();
            prizedMap.put("SCHEME_NAME", dataMap.get("SCHEME_NAME"));
            prizedMap.put("DIVISION_NO", String.valueOf(divNo));
            prizedMap.put("CHITTAL_NO", dataMap.get("CHITTAL_NO"));
            lst = sqlMap.executeQueryForList("getSelectPrizedDetailsEntryRecords", prizedMap);
            if (lst != null && lst.size() > 0) {
                prizedMap = (HashMap) lst.get(0);
                if (prizedMap.get("DRAW") != null && !prizedMap.get("DRAW").equals("") && prizedMap.get("DRAW").equals("Y")) {
                    prized = true;
                }
                if (prizedMap.get("AUCTION") != null && !prizedMap.get("AUCTION").equals("") && prizedMap.get("AUCTION").equals("Y")) {
                    prized = true;
                }
            } else {
                prized = false;
            }
            long totDiscAmt = 0;
            long penalAmt = 0;
            double netAmt = 0;
            double insAmtPayable = 0;
            double totBonusAmt = 0;
            double bonusAmt = 0;
            String penalIntType = "";
            long penalValue = 0;
            String penalGraceType = "";
            long penalGraceValue = 0;
            String penalCalcBaseOn = "";
            long diffDayPending = 0;

            if (pendingInst > 0) {              //pending installment calculation starts...
                insDueAmt = (long) insAmt * pendingInst;
                double calc = 0;
                long totInst = pendingInst;
                penalCalcBaseOn = CommonUtil.convertObjToStr(productMap.get("PENAL_CALC"));
                if (prized == true) {
                    penalIntType = CommonUtil.convertObjToStr(productMap.get("PENAL_PRIZED_INT_TYPE"));
                    penalValue = CommonUtil.convertObjToLong(productMap.get("PENAL_PRIZED_INT_AMT"));
                    penalGraceType = CommonUtil.convertObjToStr(productMap.get("PENAL_PRIZED_GRACE_PERIOD_TYPE"));
                    penalGraceValue = CommonUtil.convertObjToLong(productMap.get("PENAL_PRIZED_GRACE_PERIOD"));
                } else if (prized == false) {
                    penalIntType = CommonUtil.convertObjToStr(productMap.get("PENAL_INT_TYPE"));
                    penalValue = CommonUtil.convertObjToLong(productMap.get("PENAL_INT_AMT"));
                    penalGraceType = CommonUtil.convertObjToStr(productMap.get("PENAL_GRACE_PERIOD_TYPE"));
                    penalGraceValue = CommonUtil.convertObjToLong(productMap.get("PENAL_GRACE_PERIOD"));
                }
                for (int j = startNoForPenal; j < noOfInstPay + startNoForPenal; j++) {
                    HashMap nextInstMap = new HashMap();
                    nextInstMap.put("SCHEME_NAME", dataMap.get("SCHEME_NAME"));
                    nextInstMap.put("DIVISION_NO", String.valueOf(divNo));
                    nextInstMap.put("SL_NO", new Double(j + noOfInsPaid));
                    List listRec = sqlMap.executeQueryForList("getSelectNextInstDate", nextInstMap);
                    if (listRec != null && listRec.size() > 0) {
                        double penal = 0;
                        nextInstMap = (HashMap) listRec.get(0);
                        instDate = setProperDtFormat(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextInstMap.get("NEXT_INSTALLMENT_DATE"))));
                        diffDayPending = DateUtil.dateDiff(instDate, currentDate);
                        //Holiday Checking - Added By Suresh
                        HashMap holidayMap = new HashMap();
                        boolean checkHoliday = true;
                        holidayMap.put("NEXT_DATE", instDate);
                        holidayMap.put("BRANCH_CODE", actBranch);
                        while (checkHoliday) {
                            boolean tholiday = false;
                            List Holiday = sqlMap.executeQueryForList("checkHolidayDateOD", holidayMap);
                            List weeklyOf = sqlMap.executeQueryForList("checkWeeklyOffOD", holidayMap);
                            boolean isHoliday = Holiday.size() > 0 ? true : false;
                            boolean isWeekOff = weeklyOf.size() > 0 ? true : false;
                            if (isHoliday || isWeekOff) {
                                if (CommonUtil.convertObjToStr(productMap.get("HOLIDAY_INT")).equals("any next working day")) {
                                    diffDayPending -= 1;
                                    instDate.setDate(instDate.getDate() + 1);
                                } else {
                                    diffDayPending += 1;
                                    instDate.setDate(instDate.getDate() - 1);
                                }
                                holidayMap.put("NEXT_DATE", instDate);
                                checkHoliday = true;
                            } else {
                                checkHoliday = false;
                            }
                        }
                        if (penalCalcBaseOn != null && !penalCalcBaseOn.equals("") && penalCalcBaseOn.equals("Days")) {
                            if (penalGraceType != null && !penalGraceType.equals("") && penalGraceType.equals("Days")) {
                                if (diffDayPending > penalGraceValue) {
                                    if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Percent")) {
                                        calc += (diffDayPending * penalValue * insAmt) / 36500;
                                    } else if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Absolute")) {
                                        calc += penalValue;
                                    }
                                }
                            } else if (penalGraceType != null && !penalGraceType.equals("") && penalGraceType.equals("Installments")) {
                                // To be written
                                if (diffDayPending > penalGraceValue) {
                                    if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Percent")) {
                                        //                                                calc = insAmt*(((pendingInst+1)*pendingInst/2)*penalValue)/1200;
                                        calc += (double) ((insAmt * penalValue) / 1200.0) * pendingInst--;
                                    } else if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Absolute")) {
                                        calc += penalValue;
                                    }
                                }
                            }
                        } else if (penalCalcBaseOn != null && !penalCalcBaseOn.equals("") && penalCalcBaseOn.equals("Installments")) {
                            if (penalGraceType != null && !penalGraceType.equals("") && penalGraceType.equals("Days")) {
                                if (diffDayPending > penalGraceValue) {
                                    if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Percent")) {
                                        calc += ((insAmt * penalValue) / 1200.0) * pendingInst--;
                                    } else if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Absolute") && totInst <= noOfInstPay) {
                                        calc += penalValue;
                                    }
                                }
                            } else if (penalGraceType != null && !penalGraceType.equals("") && penalGraceType.equals("Installments")) {
                                // To be written
                                if (diffDayPending > penalGraceValue) {
                                    if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Percent")) {
                                        //                                                calc = insAmt*(((pendingInst+1)*pendingInst/2)*penalValue)/1200;
                                        calc += (double) ((insAmt * penalValue) / 1200.0) * pendingInst--;
                                    } else if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Absolute")) {
                                        calc += penalValue;
                                    }
                                }
                            }
                        }
                        //After Scheme End Date Penal Calculating
                        if ((j + 1 == noOfInstPay + startNoForPenal) && !(penalCalcBaseOn.equals("Days") && penalIntType.equals("Percent")) && (DateUtil.dateDiff(endDate, currentDate) > 0)) {
                            if (penalIntType.equals("Percent")) {
                                diffDayPending = DateUtil.dateDiff(endDate, currentDate);
                                calc += (double) ((((insAmt * noOfInstPay * penalValue) / 100.0) * diffDayPending) / 365);
                            }
                            // Absolute Not Required...
                        }
                        penal = (long) (calc + 0.5) - penal;
                        penalList.add(String.valueOf(penal));
                        installmentMap.put("PENAL", penalList);
                        penal = calc + 0.5;

                    }
                }
                if (calc > 0) {
                    penalAmt = (long) (calc + 0.5);
                }
            }//pending installment calculation ends...
            //Discount calculation details Starts...
            for (int k = 0; k < noOfInstPay; k++) {
                HashMap nextInstMap = new HashMap();
                nextInstMap.put("SCHEME_NAME", dataMap.get("SCHEME_NAME"));
                nextInstMap.put("DIVISION_NO", String.valueOf(divNo));
                nextInstMap.put("SL_NO", new Double(k + noOfInsPaid));
                List listRec = sqlMap.executeQueryForList("getSelectNextInstDate", nextInstMap);
                if (listRec == null || listRec.isEmpty()) {
                    Date curDate = (Date) currentDate.clone();
                    int curMonth = curDate.getMonth();
                    curDate.setMonth(curMonth + 1);
                    curDate.setDate(instDay);
                    listRec = new ArrayList();
                    nextInstMap.put("NEXT_INSTALLMENT_DATE", curDate);
                    listRec.add(nextInstMap);
                }
                if (listRec != null && listRec.size() > 0) {
                    nextInstMap = (HashMap) listRec.get(0);
                    instDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextInstMap.get("NEXT_INSTALLMENT_DATE")));
                    long diffDay = DateUtil.dateDiff(instDate, currentDate);
                    String hoildayInt = CommonUtil.convertObjToStr(productMap.get("HOLIDAY_INT"));
                    if (productMap.get("BONUS_ALLOWED") != null && !productMap.get("BONUS_ALLOWED").equals("") && (productMap.get("BONUS_ALLOWED").equals("Y")
                            || productMap.get("BONUS_ALLOWED").equals("N"))) {


                        String discount = CommonUtil.convertObjToStr(productMap.get("DISCOUNT_ALLOWED"));
                        if (discount != null && !discount.equals("") && discount.equals("Y")) {
                            String discountType = CommonUtil.convertObjToStr(productMap.get("DISCOUNT_RATE_TYPE"));
                            long discountValue = CommonUtil.convertObjToLong(productMap.get("DISCOUNT_RATE_AMT"));
                            if (prized == true) {//discount calculation for prized prerson...
                                String discountPrizedDays = CommonUtil.convertObjToStr(productMap.get("DIS_PRIZED_GRACE_PERIOD_DAYS"));
                                String discountPrizedMonth = CommonUtil.convertObjToStr(productMap.get("DIS_PRIZED_GRACE_PERIOD_MONTHS"));
                                String discountPrizedAfter = CommonUtil.convertObjToStr(productMap.get("DIS_PRIZED_GRACE_PERIOD_AFTER"));
                                String discountPrizedEnd = CommonUtil.convertObjToStr(productMap.get("DIS_PRIZED_GRACE_PERIOD_END"));
                                long discountPrizedValue = CommonUtil.convertObjToLong(productMap.get("DIS_PRIZED_GRACE_PERIOD"));
                                if (discountPrizedDays != null && !discountPrizedDays.equals("") && discountPrizedDays.equals("D") && diffDay <= discountPrizedValue) {
                                    if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                        long calc = discountValue * (long) insAmt / 100;
                                        if (diffDay <= discountPrizedValue) {
                                            totDiscAmt = totDiscAmt + calc;
                                        }
                                    } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                        if (diffDay <= discountPrizedValue) {
                                            totDiscAmt = totDiscAmt + discountValue;
                                        }
                                    }
                                } else if (discountPrizedMonth != null && !discountPrizedMonth.equals("") && discountPrizedMonth.equals("M") && diffDay <= (discountPrizedValue * 30)) {
                                    if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                        long calc = discountValue * (long) insAmt / 100;
                                        totDiscAmt = totDiscAmt + calc;
                                    } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                        totDiscAmt = totDiscAmt + discountValue;
                                    }
                                } else if (discountPrizedAfter != null && !discountPrizedAfter.equals("") && discountPrizedAfter.equals("A") && currentDate.getDate() <= discountPrizedValue) {
                                    if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                        long calc = discountValue * (long) insAmt / 100;
                                        totDiscAmt = totDiscAmt + calc;
                                    } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                        totDiscAmt = totDiscAmt + discountValue;
                                    }
                                } else if (discountPrizedEnd != null && !discountPrizedEnd.equals("") && discountPrizedEnd.equals("E") && pendingInst < noOfInstPay) {
                                    if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                        long calc = discountValue * (long) insAmt / 100;
                                        totDiscAmt = totDiscAmt + calc;
                                    } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                        totDiscAmt = totDiscAmt + discountValue;
                                    }
                                } else {
                                    totDiscAmt = 0;
                                }
                            } else if (prized == false) {//discount calculation non prized person...
                                String discountGraceDays = CommonUtil.convertObjToStr(productMap.get("DIS_GRACE_PERIOD_DAYS"));
                                String discountGraceMonth = CommonUtil.convertObjToStr(productMap.get("DIS_GRACE_PERIOD_MONTHS"));
                                String discountGraceAfter = CommonUtil.convertObjToStr(productMap.get("DIS_GRACE_PERIOD_AFTER"));
                                String discountGraceEnd = CommonUtil.convertObjToStr(productMap.get("DIS_GRACE_PERIOD_END"));
                                long discountGraceValue = CommonUtil.convertObjToLong(productMap.get("DIS_GRACE_PERIOD"));
                                if (discountGraceDays != null && !discountGraceDays.equals("") && discountGraceDays.equals("D")) {
                                    if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                        long calc = discountValue * (long) insAmt / 100;
                                        if (diffDay <= discountGraceValue) {
                                            totDiscAmt = totDiscAmt + calc;
                                        }
                                    } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                        if (diffDay <= discountGraceValue) {
                                            totDiscAmt = totDiscAmt + discountValue;
                                        }
                                    } else {
                                        totDiscAmt = 0;
                                    }
                                } else if (discountGraceDays != null && !discountGraceDays.equals("") && discountGraceDays.equals("M") && diffDay <= discountGraceValue * 30 && pendingInst < noOfInstPay) {
                                    if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                        long calc = discountValue * (long) insAmt / 100;
                                        totDiscAmt = totDiscAmt + calc;
                                    } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                        totDiscAmt = totDiscAmt + discountValue;
                                    }
                                } else if (discountGraceDays != null && !discountGraceDays.equals("") && discountGraceDays.equals("A") && currentDate.getDate() <= discountGraceValue && pendingInst < noOfInstPay) {
                                    if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                        long calc = discountValue * (long) insAmt / 100;
                                        totDiscAmt = totDiscAmt + calc;
                                    } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                        totDiscAmt = totDiscAmt + discountValue;
                                    }
                                } else if (discountGraceDays != null && !discountGraceDays.equals("") && discountGraceDays.equals("E") && pendingInst < noOfInstPay) {
                                    if (discountType != null && !discountType.equals("") && discountType.equals("Percent")) {
                                        long calc = discountValue * (long) insAmt / 100;
                                        totDiscAmt = totDiscAmt + calc;
                                    } else if (discountType != null && !discountType.equals("") && discountType.equals("Absolute")) {
                                        totDiscAmt = totDiscAmt + discountValue;
                                    }
                                } else {
                                    totDiscAmt = 0;
                                }
                            }
                        } else if (discount != null && !discount.equals("") && discount.equals("N")) {
                            totDiscAmt = 0;
                        }
                    }

                }
            }

            //Bonus calculation details Starts...
            for (int l = startNoForPenal; l <= noOfInstPay - addNo; l++) {
                HashMap nextInstMap = new HashMap();
                nextInstMap.put("SCHEME_NAME", dataMap.get("SCHEME_NAME"));
                nextInstMap.put("DIVISION_NO", divNo);
                nextInstMap.put("SL_NO", new Double(l + noOfInsPaid + addNo + firstInst_No));
                List listRec = sqlMap.executeQueryForList("getSelectBonusAndNextInstDateWithoutDivision", nextInstMap);
                if (listRec == null || listRec.isEmpty()) {
                    Date curDate = (Date) currentDate.clone();
                    int curMonth = curDate.getMonth();
                    curDate.setMonth(curMonth + 1);
                    curDate.setDate(instDay);
                    listRec = new ArrayList();
                    nextInstMap.put("NEXT_INSTALLMENT_DATE", curDate);
                    listRec.add(nextInstMap);
                    bonusAvailabe = false;
                }
                if (listRec != null && listRec.size() > 0) {
                    nextInstMap = (HashMap) listRec.get(0);
                    instDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextInstMap.get("NEXT_INSTALLMENT_DATE")));
                    bonusAmt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(nextInstMap.get("NEXT_BONUS_AMOUNT"))).doubleValue();
                    long diffDay = DateUtil.dateDiff(instDate, currentDate);
                    if (productMap.get("BONUS_ROUNDING") != null && !productMap.get("BONUS_ROUNDING").equals("") && productMap.get("BONUS_ROUNDING").equals("Y")
                            && bonusAmt > 0) {
                        Rounding rod = new Rounding();
                        if (productMap.get("BONUS_ROUNDOFF") != null && productMap.get("BONUS_ROUNDOFF").equals("NEAREST_VALUE")) {
                            bonusAmt = (double) rod.getNearest((long) (bonusAmt * 100), 100) / 100;
                        } else {
                            bonusAmt = (double) rod.lower((long) (bonusAmt * 100), 100) / 100;
                        }
                    }
                    String hoildayInt = CommonUtil.convertObjToStr(productMap.get("HOLIDAY_INT"));
                    if (productMap.get("BONUS_ALLOWED") != null && !productMap.get("BONUS_ALLOWED").equals("") && (productMap.get("BONUS_ALLOWED").equals("Y")
                            || productMap.get("BONUS_ALLOWED").equals("N"))) {
                        if (bonusAvailabe == true) {
                            if (prized == true) {
                                String bonusPrizedDays = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_DAYS"));
                                String bonusPrizedMonth = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_MNTH"));
                                String bonusPrizedAfter = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_AFT"));
                                String bonusPrizedEnd = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_END"));
                                long bonusPrizedValue = CommonUtil.convertObjToLong(productMap.get("BONUS_PRIZED_GRACE_PERIOD"));
                                if (bonusPrizedDays != null && !bonusPrizedDays.equals("") && bonusPrizedDays.equals("D") && diffDay <= bonusPrizedValue) {
                                    totBonusAmt = totBonusAmt + bonusAmt;
                                } else if (bonusPrizedMonth != null && !bonusPrizedMonth.equals("") && bonusPrizedMonth.equals("M") && diffDay <= (bonusPrizedValue * 30)) {
                                    totBonusAmt = totBonusAmt + bonusAmt;
                                } else if (bonusPrizedAfter != null && !bonusPrizedAfter.equals("") && bonusPrizedAfter.equals("A") && currentDate.getDate() <= bonusPrizedValue) {
                                    totBonusAmt = totBonusAmt + bonusAmt;
                                } else if (bonusPrizedEnd != null && !bonusPrizedEnd.equals("") && bonusPrizedEnd.equals("E")) {
                                } else {
                                }
                            } else if (prized == false) {
                                String bonusGraceDays = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_DAYS"));
                                String bonusGraceMonth = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_MONTHS"));
                                String bonusGraceOnAfter = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_AFTER"));
                                String bonusGraceEnd = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_END"));
                                long bonusGraceValue = CommonUtil.convertObjToLong(productMap.get("BONUS_GRACE_PERIOD"));
                                if (bonusGraceDays != null && !bonusGraceDays.equals("") && bonusGraceDays.equals("D") && diffDay <= bonusGraceValue) {
                                    totBonusAmt = totBonusAmt + bonusAmt;
                                } else if (bonusGraceMonth != null && !bonusGraceMonth.equals("") && bonusGraceMonth.equals("M") && diffDay <= (bonusGraceValue * 30)) {
                                    totBonusAmt = totBonusAmt + bonusAmt;
                                } else if (bonusGraceOnAfter != null && !bonusGraceOnAfter.equals("") && bonusGraceOnAfter.equals("A") && currentDate.getDate() <= bonusGraceValue) {
                                    totBonusAmt = totBonusAmt + bonusAmt;
                                } else if (bonusGraceEnd != null && !bonusGraceEnd.equals("") && bonusGraceEnd.equals("E")) {
                                } else {
                                }
                            }
                        }
                        //Added By Suresh
                        Rounding rod = new Rounding();
                        if (productMap.get("BONUS_ROUNDING") != null && !productMap.get("BONUS_ROUNDING").equals("") && productMap.get("BONUS_ROUNDING").equals("Y")
                                && bonusAmt > 0) {
                            if (productMap.get("BONUS_ROUNDOFF") != null && productMap.get("BONUS_ROUNDOFF").equals("NEAREST_VALUE")) {
                                bonusAmt = (double) rod.getNearest((long) (bonusAmt * 100), 100) / 100;
                            } else {
                                bonusAmt = (double) rod.lower((long) (bonusAmt * 100), 100) / 100;
                            }
                        }
                        bonusList.add(String.valueOf(bonusAmt));
                        installmentMap.put("BONUS", bonusList);
                    }
                }
                bonusAmt = 0;
            }
            //Arbitration Amount
            double arbitrationAmount = 0.0;
            whereMap.put("ACT_NUM", dataMap.get("CHITTAL_NO"));
            List caseChargeList = sqlMap.executeQueryForList("getMDSCaseChargeDetails", whereMap);
            if (caseChargeList != null && caseChargeList.size() > 0) {
                for (int i = 0; i < caseChargeList.size(); i++) {
                    whereMap = (HashMap) caseChargeList.get(i);
                    arbitrationAmount += CommonUtil.convertObjToDouble(whereMap.get("CHARGE_AMT")).doubleValue();
                }
            }
            //Notice Amount
            double noticeAmount = 0.0;
            whereMap.put("ACT_NUM", dataMap.get("CHITTAL_NO"));
            List noticeList = sqlMap.executeQueryForList("getMDSNoticeChargeDetails", whereMap);
            if (noticeList != null && noticeList.size() > 0) {
                for (int i = 0; i < noticeList.size(); i++) {
                    whereMap = (HashMap) noticeList.get(i);
                    noticeAmount += CommonUtil.convertObjToDouble(whereMap.get("CHARGE_AMT")).doubleValue();
                }
            }
            //Charges
            double chargeAmt = 0.0;
            whereMap.put("ACT_NUM", dataMap.get("CHITTAL_NO"));
            chargeAmt = getChargeAmount(whereMap, prodType);
            if (chargeAmt > 0) {
                dataMap.put("CHARGES", String.valueOf(chargeAmt));
            } else {
                dataMap.put("CHARGES", "0");
            }
            if (productMap.get("BONUS_ROUNDING") != null && !productMap.get("BONUS_ROUNDING").equals("") && productMap.get("BONUS_ROUNDING").equals("Y")
                    && totBonusAmt > 0) {
                Rounding rod = new Rounding();
                if (productMap.get("BONUS_ROUNDOFF") != null && productMap.get("BONUS_ROUNDOFF").equals("NEAREST_VALUE")) {
                    totBonusAmt = (double) rod.getNearest((long) (totBonusAmt * 100), 100) / 100;
                } else {
                    totBonusAmt = (double) rod.lower((long) (totBonusAmt * 100), 100) / 100;
                }
            }
            netAmt = (insAmt * noOfInstPay) + penalAmt - (totBonusAmt + totDiscAmt) + chargeAmt;
            insAmtPayable = (insAmt * noOfInstPay) - (totBonusAmt + totDiscAmt);
            dataMap.put("DIVISION_NO", String.valueOf(divNo));
            dataMap.put("CHIT_START_DT", startDate);
            dataMap.put("INSTALLMENT_DATE", insDate);
            dataMap.put("NO_OF_INSTALLMENTS", String.valueOf(totIns));
            dataMap.put("CURR_INST", String.valueOf(curInsNo));
            dataMap.put("PENDING_INST", String.valueOf(pendingInst));
            dataMap.put("PENDING_DUE_AMT", String.valueOf(insDueAmt));
            dataMap.put("NO_OF_INST_PAY", String.valueOf(noOfInstPay));
            dataMap.put("PRINCIPAL", String.valueOf(insAmtPayable)); // Principal Amount
            dataMap.put("PAID_INST", String.valueOf(noOfInsPaid));
            dataMap.put("PAID_DATE", currentDate);
            dataMap.put("INTEREST", "0");
            if (prized == true) {
                dataMap.put("PRIZED_MEMBER", "Y");
            } else {
                dataMap.put("PRIZED_MEMBER", "N");
            }
            dataMap.put("BONUS", new Double(totBonusAmt));
            dataMap.put("DISCOUNT", new Double(totDiscAmt));
            dataMap.put("PENAL", new Double(penalAmt));                 // Penal Amount
            dataMap.put("TOTAL_DEMAND", new Double(netAmt));
            dataMap.put("ARBITRATION", new Double(arbitrationAmount));
            dataMap.put("NOTICE_AMT", new Double(noticeAmount));
        } else {
            dataMap = null;
        }
        return dataMap;
    }

    private void transactionPartMDS(HashMap chittalMap, HashMap map) throws Exception {
        try {
            HashMap MDSmap = new HashMap();
            HashMap bonusMap = new HashMap();
            HashMap whereMap = new HashMap();
            HashMap chittalDetailMap = new HashMap();
            HashMap installmentMap = new HashMap();
            String actNo = "";
            String subNo = "";
            String chittalNo = "";
            long count = 0;
            int curInsNo = 0;
            int noOfInsPaid = 0;
            long pendingInst = 0;
            long noOfInstPay = 0;
            double totBonusAmt = 0;
            double totPenalAmt = 0;
            double totNoticeAmt = 0;
            double totDiscountAmt = 0;
            double totArbitrationAmt = 0;
            actNo = CommonUtil.convertObjToStr(chittalMap.get("ACT_NUM"));
            if (actNo.indexOf("_") != -1) {
                chittalNo = actNo.substring(0, actNo.indexOf("_"));
                subNo = actNo.substring(actNo.indexOf("_") + 1, actNo.length());
            }
            whereMap.put("CHITTAL_NO", chittalNo);
            whereMap.put("SUB_NO", CommonUtil.convertObjToInt(subNo));
            List chittalList = (List) sqlMap.executeQueryForList("getMDSChittalDetails", whereMap);
            if (chittalList != null && chittalList.size() > 0) {
                chittalDetailMap = (HashMap) chittalList.get(0);
                whereMap.put("INT_CALC_UPTO_DT", setProperDtFormat(currentDate));
                HashMap instMap = new HashMap();
                totBonusAmt += CommonUtil.convertObjToDouble(chittalMap.get("BONUS_AMT")).doubleValue();
                totPenalAmt += CommonUtil.convertObjToDouble(chittalMap.get("PENAL_AMT")).doubleValue();
                totDiscountAmt += CommonUtil.convertObjToDouble(chittalMap.get("DISCOUNT_AMT")).doubleValue();
                totNoticeAmt = CommonUtil.convertObjToDouble(chittalMap.get("NOTICE_AMT")).doubleValue();
                totNoticeAmt = CommonUtil.convertObjToDouble(chittalMap.get("CHARGES")).doubleValue();
                totArbitrationAmt = CommonUtil.convertObjToDouble(chittalMap.get("ARBITRATION_AMT")).doubleValue();
                instMap.put("BONUS", chittalMap.get("BONUS_AMT"));
                instMap.put("DISCOUNT", chittalMap.get("DISCOUNT_AMT"));
                instMap.put("PENAL", chittalMap.get("PENAL_AMT"));
                instMap.put("INST_AMT", chittalDetailMap.get("INST_AMT"));
                List insList = sqlMap.executeQueryForList("getNoOfInstalmentsPaid", whereMap);
                if (insList != null && insList.size() > 0) {
                    whereMap = (HashMap) insList.get(0);
                    noOfInsPaid = CommonUtil.convertObjToInt(whereMap.get("NO_OF_INST"));
                    count = CommonUtil.convertObjToLong(whereMap.get("NO_OF_INST"));
                }
                HashMap insDateMap = new HashMap();
                insDateMap.put("DIVISION_NO", CommonUtil.convertObjToInt(chittalDetailMap.get("DIVISION_NO")));
                insDateMap.put("SCHEME_NAME", chittalDetailMap.get("SCHEME_NAME"));
                insDateMap.put("CURR_DATE", setProperDtFormat(currentDate));
                insDateMap.put("ADD_MONTHS", CommonUtil.convertObjToInt("-1"));
                List insDateLst = sqlMap.executeQueryForList("getMDSCurrentInsDate", insDateMap);
                if (insDateLst != null && insDateLst.size() > 0) {
                    insDateMap = (HashMap) insDateLst.get(0);
                    curInsNo = CommonUtil.convertObjToInt(insDateMap.get("INSTALLMENT_NO"));
                    pendingInst = curInsNo - noOfInsPaid;
                    if (pendingInst < 0) {
                        pendingInst = 0;
                    }
                    if (pendingInst > 0) {
                        noOfInstPay = pendingInst;
                    }
                }
                installmentMap.put(CommonUtil.convertObjToStr(insDateMap.get("INSTALLMENT_NO")), instMap);
                //Narration
                int insDay = 0;
                Date paidUpToDate = null;
                HashMap instDateMap = new HashMap();
                instDateMap.put("SCHEME_NAME", chittalDetailMap.get("SCHEME_NAME"));
                instDateMap.put("DIVISION_NO", CommonUtil.convertObjToInt(chittalDetailMap.get("DIVISION_NO")));
                instDateMap.put("INSTALLMENT_NO", CommonUtil.convertObjToInt(String.valueOf(count)));
                List insLst = sqlMap.executeQueryForList("getSelectInstUptoPaid", instDateMap);
                if (insLst != null && insLst.size() > 0) {
                    instDateMap = (HashMap) insLst.get(0);
                    paidUpToDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(instDateMap.get("NEXT_INSTALLMENT_DATE")));
                } else {
                    Date startedDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(chittalDetailMap.get("SCHEME_START_DT")));
                    insDay = CommonUtil.convertObjToInt(chittalDetailMap.get("INSTALLMENT_DAY"));
                    startedDate.setDate(insDay);
                    int stMonth = startedDate.getMonth();
                    startedDate.setMonth(stMonth + (int) count - 1);
                    paidUpToDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(startedDate));
                }
                String narration = "";
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM/yyyy");
                int noInstPay = CommonUtil.convertObjToInt(String.valueOf(noOfInstPay));
                if (noInstPay == 1) {
                    narration = "Inst#" + (noOfInsPaid + 1);
                    Date dt = DateUtil.addDays(paidUpToDate, 30);
                    narration += " " + sdf.format(dt);
                } else if (noInstPay > 1) {
                    narration = "Inst#" + (noOfInsPaid + 1);
                    narration += "-" + (noOfInsPaid + noInstPay);
                    Date dt = DateUtil.addDays(paidUpToDate, 30);
                    narration += " " + sdf.format(dt);
                    dt = DateUtil.addDays(paidUpToDate, 30 * noInstPay);
                    narration += " To " + sdf.format(dt);
                }
                //SET RECEIPT_ENTRY_TO
                mdsReceiptEntryTO = new MDSReceiptEntryTO();
                mdsReceiptEntryTO.setCommand(CommonConstants.TOSTATUS_INSERT);
                mdsReceiptEntryTO.setSchemeName(CommonUtil.convertObjToStr(chittalDetailMap.get("SCHEME_NAME")));
                mdsReceiptEntryTO.setChittalNo(chittalNo);
                mdsReceiptEntryTO.setSubNo(CommonUtil.convertObjToDouble(subNo));
                mdsReceiptEntryTO.setMemberName(CommonUtil.convertObjToStr(chittalDetailMap.get("MEMBER_NAME")));
                mdsReceiptEntryTO.setDivisionNo(CommonUtil.convertObjToDouble(chittalDetailMap.get("DIVISION_NO")));
                mdsReceiptEntryTO.setChitStartDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(chittalDetailMap.get("CHIT_START_DT"))));
                mdsReceiptEntryTO.setChitEndDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(chittalDetailMap.get("CHIT_END_DT"))));
                mdsReceiptEntryTO.setPaidDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(chittalMap.get("INT_CALC_UPTO_DT"))));
                mdsReceiptEntryTO.setNoOfInst(CommonUtil.convertObjToInt(chittalDetailMap.get("NO_OF_INSTALLMENTS")));
                mdsReceiptEntryTO.setCurrInst(CommonUtil.convertObjToDouble(String.valueOf(curInsNo)));
                mdsReceiptEntryTO.setPendingInst(CommonUtil.convertObjToDouble(String.valueOf(pendingInst)));
                mdsReceiptEntryTO.setInstAmt(CommonUtil.convertObjToDouble(chittalDetailMap.get("INST_AMT")));
                mdsReceiptEntryTO.setNoOfInstPay(CommonUtil.convertObjToInt(String.valueOf(noOfInstPay)));
                mdsReceiptEntryTO.setInstAmtPayable(CommonUtil.convertObjToDouble(chittalMap.get("PRINCIPAL")));
                mdsReceiptEntryTO.setPaidInst(CommonUtil.convertObjToDouble(String.valueOf(noOfInsPaid)));
                mdsReceiptEntryTO.setBonusAmtPayable(CommonUtil.convertObjToDouble(String.valueOf(totBonusAmt)));
                mdsReceiptEntryTO.setDiscountAmt(CommonUtil.convertObjToDouble(String.valueOf(totDiscountAmt)));
                mdsReceiptEntryTO.setPenalAmtPayable(CommonUtil.convertObjToDouble(String.valueOf(totPenalAmt)));
                mdsReceiptEntryTO.setNoticeAmt(CommonUtil.convertObjToDouble(String.valueOf(totNoticeAmt)));
                mdsReceiptEntryTO.setArbitrationAmt(CommonUtil.convertObjToDouble(String.valueOf(totArbitrationAmt)));
                mdsReceiptEntryTO.setNetAmt(CommonUtil.convertObjToDouble(chittalMap.get("TOTAL_DEMAND")));
                mdsReceiptEntryTO.setNarration(narration);
                mdsReceiptEntryTO.setBankPay("N");
                mdsReceiptEntryTO.setStatusBy((String) map.get(CommonConstants.USER_ID));
                mdsReceiptEntryTO.setStatus(CommonConstants.STATUS_CREATED);
                mdsReceiptEntryTO.setStatusDt(currentDate);
                mdsReceiptEntryTO.setBranchCode(CommonUtil.convertObjToStr(chittalDetailMap.get("BRANCH_CODE")));
                mdsReceiptEntryTO.setInitiatedBranch(CommonUtil.convertObjToStr(map.get("BRANCH_CODE")));
                MDSmap.put("INSTALLMENT_MAP", installmentMap);
                MDSmap.put("mdsReceiptEntryTO", mdsReceiptEntryTO);
                List closureList = (List) sqlMap.executeQueryForList("checkSchemeClosureDetails", chittalDetailMap);
                if (closureList != null && closureList.size() > 0) {
                    MDSmap.put("MDS_CLOSURE", "MDS_CLOSURE");
                } else {
                    MDSmap.remove("MDS_CLOSURE");
                }
                MDSmap.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
                MDSmap.put("BRANCH_CODE", map.get("BRANCH_CODE"));
                MDSmap.put("TransactionTO", map.get("TransactionTO"));
                MDSmap.put("INT_CALC_UPTO_DT", currentDate);
                MDSmap.put("FROM_RECOVERY_TALLY", "FROM_RECOVERY_TALLY");
                MDSmap.put("POSTAGE_AMT_FOR_INT", chittalMap.get("POSTAGE_AMT_FOR_INT"));
                MDSmap.put("RENEW_POSTAGE_AMT", chittalMap.get("RENEW_POSTAGE_AMT"));
                MDSmap.put("POSTAGE_ACHD", chittalMap.get("POSTAGE_ACHD"));
                mdsReceiptEntryDAO = new MDSReceiptEntryDAO();
                HashMap transMap = new HashMap();
                transMap = mdsReceiptEntryDAO.execute(MDSmap, false);                    // INSERT_TRANSACTIO
                //AUTHORIZE_START
                HashMap authorizeMap = new HashMap();
                authorizeMap.put("NET_TRANS_ID", transMap.get("BATCH_ID"));
                authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
                authorizeMap.put("USER_ID", map.get(CommonConstants.USER_ID));
                authorizeMap.put("BRANCH_CODE", map.get("BRANCH_CODE"));
                mdsReceiptEntryTO.setCommand("");
                MDSmap.put("AUTHORIZEMAP", authorizeMap);
                MDSmap.put("mdsReceiptEntryTO", mdsReceiptEntryTO);
                MDSmap.put("SCHEME_NAME", CommonUtil.convertObjToStr(chittalDetailMap.get("SCHEME_NAME")));
                MDSmap.put("USER_ID", map.get(CommonConstants.USER_ID));
                mdsReceiptEntryDAO = new MDSReceiptEntryDAO();
                HashMap transDetMap = mdsReceiptEntryDAO.execute(MDSmap, false);                                     // AUTHORIZE_TRANSACTION
                if (transMap != null && transMap.size() > 0) {
                    paramMap.put(paramMap.get("ACT_NUM"), transMap.get("BATCH_ID"));
                }
            }
        } catch (Exception e) {
            //sqlMap.rollbackTransaction();
            e.printStackTrace();
            throw e;
        }
    }
    
    
  private String generateLinkID() throws Exception {
        IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "GENERATE_LINK_ID");
        where.put("INIT_TRANS_ID", "INIT_TRANS_ID");//for trans_batch_id resetting, branch code
        where.put(CommonConstants.BRANCH_ID, branch);
        String batchID = (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
        dao = null;
        return batchID;
    }  
    //modified by rishad 17/042015
    public List getInterestTableDataKSSB(String fFlag) {
        List interestList = new ArrayList();
        List interestRow = new ArrayList();
        ArrayList rdList = new ArrayList();
        double limitAmount=0;
        String depInt = "N";
        HashMap errorMap = null;
        HashMap tdsMap = null; // Added by nithya on 06-02-2020 for KD-1090
        TdsCalc tds = null;
        HashMap custMap = null;
        try {
            generateSingleTransId = generateLinkID();
            HashMap nonBatch = new HashMap();
            // Added by nithya on 06-02-2020 for TDS implementation
            if (paramMap.containsKey(CommonConstants.PRODUCT_ID)) {
                nonBatch.put(InterestTaskRunner.PRODUCT_ID, paramMap.get(CommonConstants.PRODUCT_ID));
                HashMap whereMap = new HashMap();//TDS product Level Checking Calculate Yes/No
                whereMap.put("PROD_ID", paramMap.get(CommonConstants.PRODUCT_ID));
                List prodTDSList = (List) sqlMap.executeQueryForList("icm.getDepositMaturityIntRate", whereMap);
                if (prodTDSList != null && prodTDSList.size() > 0) {
                    whereMap = (HashMap) prodTDSList.get(0);
                    if (CommonUtil.convertObjToStr(whereMap.get("CALCULATE_TDS")).equals("Y")) {
                        prodTDSDeduction = true;
                    } else {
                        prodTDSDeduction = false;
                    }
                }
            }
            //System.out.println("############# prodTDSDeduction : "+prodTDSDeduction);
            // End
            //__ get ProductData...
            if (paramMap.containsKey(CommonConstants.PRODUCT_ID)) {
                nonBatch.put(InterestTaskRunner.PRODUCT_ID, paramMap.get(CommonConstants.PRODUCT_ID));
            }
            //__ getAccount Data
            if (paramMap.containsKey(CommonConstants.ACT_NUM)) {
                nonBatch.put(CommonConstants.ACT_NUM, paramMap.get(CommonConstants.ACT_NUM));
            }
            limitAmount=CommonUtil.convertObjToDouble(paramMap.get("LIMIT_AMOUNT"));
            nonBatch.put("BEHAVES_LIKE", "FIXED");
            nonBatch.put("APPLICATION", "APPLICATION");
            
            InterestBatchTO objInterestBatchTO = null;
            ArrayList productList = (ArrayList) sqlMap.executeQueryForList("Deposit.getProducts", nonBatch);
            HashMap dataMap = new HashMap();
            dataMap.put("PROD_ID", paramMap.get(CommonConstants.PRODUCT_ID));
            List periodList = sqlMap.executeQueryForList("getIntRoundAtIntApplication", dataMap);
            if (periodList != null && periodList.size() > 0) {
                HashMap periodMap = (HashMap) periodList.get(0);
                if (periodMap.containsKey("INT_ROUND_AT_INTAPPL") && !CommonUtil.convertObjToStr(periodMap.get("INT_ROUND_AT_INTAPPL")).equals("") && CommonUtil.convertObjToStr(periodMap.get("INT_ROUND_AT_INTAPPL")).equalsIgnoreCase("Y")) {
                    depInt = "Y";
                }
            }
            paramMap.put("SCREEN_NAME","DepositInterestApp");
            HashMap acctDtlMap = new HashMap();
            if (productList != null && productList.size() > 0) {
                int prodListSize = productList.size();
                for (int prod = 0; prod < prodListSize; prod++) {
                    //System.out.println("################# productList inside for : " + productList);
                    paramMap.put(InterestTaskRunner.AC_HD_ID, ((HashMap) productList.get(prod)).get("ACCT_HEAD")); //Ac Head
                    paramMap.put(InterestTaskRunner.DEBIT_INT, ((HashMap) productList.get(prod)).get("INT_PROV_ACHD")); //Debit Int Head
                    paramMap.put(InterestTaskRunner.CREDIT_INT, ((HashMap) productList.get(prod)).get("INT_PAY")); //Credit Int Head
                    paramMap.put(InterestTaskRunner.PRODUCT_ID, ((HashMap) productList.get(prod)).get("PROD_ID")); //Product Id
                    paramMap.put("ROUND_OFF", ((HashMap) productList.get(prod)).get("INT_ROUNDOFF_TERMS")); //Product Id
                    paramMap.put(InterestTaskRunner.LAST_APPL_DT, ((HashMap) productList.get(prod)).get("LAST_INT_APPL_DT")); //Last Interest Applied Dt Payale
                    paramMap.put("interestType", ((HashMap) productList.get(prod)).get("INT_TYPE"));
                    paramMap.put(InterestTaskRunner.APPL_NEW_DT, null); //Updated date based on FREQ_A from InterestTaskRunner
                    String postageAcHd = CommonUtil.convertObjToStr(((HashMap) productList.get(prod)).get("POSTAGE_ACHD"));
                    nonBatch.put(InterestTaskRunner.PRODUCT_ID, ((HashMap) productList.get(prod)).get("PROD_ID")); //Product Id)
                    paramMap.put("BATCH_PROCESS", "BATCH_PROCESS");
                    paramMap.put("PROD_TYPE", "TD");
                    paramMap.put("TASK", "APPLICATION");
                    paramMap.put("DISCOUNTED_RATE", ((HashMap) productList.get(prod)).get("DISCOUNTED_RATE"));
                    List recList = null;
                    List accountList = null;
                    List calfreqAccountList = null;
                    actBranch = CommonUtil.convertObjToStr(paramMap.get(CommonConstants.BRANCH_ID));
                    currentDate = ServerUtil.getCurrentDate(actBranch);
                    paramMap.put(CommonConstants.BRANCH_ID, actBranch);
                    nonBatch.put("PARAMMAP", paramMap);
                    if (paramMap.containsKey("DEPOSIT_MANUAL")) {
                        nonBatch.put("DEPOSIT_MANUAL", "DEPOSIT_MANUAL");
                    }
                    if (paramMap.containsKey("CUST_ID")) {
                        nonBatch.put("CUST_ID", paramMap.get("CUST_ID"));
                        nonBatch.put("DEPOSIT_MANUAL", "DEPOSIT_MANUAL");
                    }
                    if (paramMap.containsKey("INTPAY_MODE")) {
                        nonBatch.put("INTPAY_MODE", paramMap.get("INTPAY_MODE"));
                    }
                    if (paramMap.containsKey("LIEN_ACCOUNT")) {
                        nonBatch.put("LIEN_ACCOUNT", paramMap.get("LIEN_ACCOUNT"));
                    }
                    if (paramMap.containsKey("ACCOUNT_LIST") || paramMap.containsKey("CAL_FREQ_ACCOUNT_LIST")) {
                        if (paramMap.containsKey("ACCOUNT_LIST")) {
                            accountList = (ArrayList) paramMap.get("ACCOUNT_LIST");
                        } else if (paramMap.containsKey("CAL_FREQ_ACCOUNT_LIST")) {
                            accountList = (ArrayList) paramMap.get("CAL_FREQ_ACCOUNT_LIST");
                        }
                    } else {
                        nonBatch.put("FREQUENCY_BASED", "FREQUENCY_BASED");
                        accountList = getAccountList(nonBatch);
                    }
                    int accountListSize = 0;
                    if (accountList != null) {
                        accountListSize = accountList.size();
                    }
                    // System.out.println("################# accountListSize : " + accountListSize);
                    for (int acc = 0; acc < accountListSize; acc++) {
                        try {
                            paramMap.put("BEHAVES", ((HashMap) accountList.get(acc)).get("ACCT_TYPE"));
                            paramMap.put("ACT_NUM", ((HashMap) accountList.get(acc)).get("ACT_NUM"));
                            paramMap.put("CUST_ID", ((HashMap) accountList.get(acc)).get("CUST_ID"));
                            paramMap.put("AMT", ((HashMap) accountList.get(acc)).get("AMOUNT"));
                            paramMap.put("INTEREST_AMOUNT", ((HashMap) accountList.get(acc)).get("INTEREST_AMOUNT"));
                            paramMap.put("TDS_AMOUNT", ((HashMap) accountList.get(acc)).get("TDS_AMOUNT"));// Added by nithya on 06-02-2020 for KD-1090
                            paramMap.put("INT_CALC_UPTO_DT", ((HashMap) accountList.get(acc)).get("INT_CALC_UPTO_DT"));
                            paramMap.put("LAST_INT_DATE_DIS",((HashMap) accountList.get(acc)).get("FROM_DATE"));
                            paramMap.put("PROD_TYPE", "TD");
                            paramMap.put(CommonConstants.BRANCH_ID, actBranch);
                            paramMap.put("CUR_DT", currentDate.clone());
                            if(((HashMap) accountList.get(acc)).get("CHEQUENO")!=null){
                            paramMap.put("CHEQUENO",((HashMap) accountList.get(acc)).get("CHEQUENO"));}
                            double interestAmt = 0.0;
                            double tdsAmount = 0.0;
                            double intTrfAmt = 0.0;
                            double Tot_Int_AMT = 0.0;
                            double postageAmt = 0.0;
                            double postageRenewAmt = 0.0;
                            double tdsDeductedForBaseAccount = 0.0;
                            paramMap.put("CURR_DATE", currentDate);
                            //Get Deposit Account Details
                            List acctDtl = sqlMap.executeQueryForList("getDepoitDetailForIntApp", paramMap);
                            List postageList = sqlMap.executeQueryForList("getPostageAmount", paramMap);
                            List renewPostageList = sqlMap.executeQueryForList("getRenewPostageAmount", paramMap);
                            if (postageList != null && postageList.size() > 0) {
                                HashMap hamp = (HashMap) postageList.get(0);
                                postageAmt = CommonUtil.convertObjToDouble(hamp.get("POSTAGE_AMT")).doubleValue();
                            }
                            if (renewPostageList != null && renewPostageList.size() > 0) {
                                HashMap hamp = (HashMap) renewPostageList.get(0);
                                postageRenewAmt = CommonUtil.convertObjToDouble(hamp.get("RENEW_POSTAGE_AMT")).doubleValue();
                            }
                            double renewalCount = 0;
                            HashMap renewalCountMap = new HashMap();
                            String depositNo = CommonUtil.convertObjToStr(paramMap.get("ACT_NUM"));
                            depositNo = depositNo.lastIndexOf("_") != -1 ? depositNo.substring(0, depositNo.length() - 2) : depositNo;                                    
                            renewalCountMap.put("ACT_NUM", depositNo);
                            List lstCount = sqlMap.executeQueryForList("getSelectRenewalCount", renewalCountMap);
                            if (lstCount != null && lstCount.size() > 0) {
                                renewalCountMap = (HashMap) lstCount.get(0);
                                renewalCount = CommonUtil.convertObjToDouble(renewalCountMap.get("RENEWAL_COUNT"));
                            }
                            if (acctDtl != null && acctDtl.size() > 0) {
                                String behaves = CommonUtil.convertObjToStr(paramMap.get("BEHAVES"));
                                //System.out.println("#### behaves : " + behaves);
                                String rec = "RECURRING";
                                String mds = "MDS";
                                double Tot_Int_Cr = 0.0;
                                double Tot_Int_Dr = 0.0;
                                double Int_Difference=0.0;

                                acctDtlMap = (HashMap) acctDtl.get(0);
                                paramMap.put("TODAY_DT", currentDate);
                                Date lstproDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(acctDtlMap.get("LAST_PROV_DT")));
                                Date lstIntCrDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(acctDtlMap.get("LAST_INT_APPL_DT")));
                                Date actuallstIntCrDt = (Date) lstIntCrDt.clone();
                                //    System.out.println("rish##########################" + lstIntCrDt);
                                Date depDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(acctDtlMap.get("DEPOSIT_DT")));
                                Date depMatDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(acctDtlMap.get("MATURITY_DT")));
                                Date nextCalsDt = DateUtil.addDays(lstIntCrDt, CommonUtil.convertObjToInt(acctDtlMap.get("FREQ")));
                                int intPayFreq=CommonUtil.convertObjToInt(acctDtlMap.get("FREQ"));
                                //Date nextCalsDt = (Date) ((HashMap) accountList.get(acc)).get("TO_DT");
                                //Added by chithra for  calculating  full Interest of  Matured deposits 
                                if (DateUtil.dateDiff(depMatDt, currentDate) >= 0) {
                                    acctDtlMap.put("FREQ", "0");
                                }
                                Tot_Int_AMT = CommonUtil.convertObjToDouble(acctDtlMap.get("TOT_INT_AMT")).doubleValue();
                                Tot_Int_Cr = CommonUtil.convertObjToDouble(acctDtlMap.get("TOTAL_INT_CREDIT")).doubleValue();
                                Tot_Int_Dr = CommonUtil.convertObjToDouble(acctDtlMap.get("TOTAL_INT_DRAWN")).doubleValue();
                                Int_Difference=CommonUtil.convertObjToDouble(acctDtlMap.get("INT_DIFFERENCE"));
                               // System.out.println("Int Difference "+Int_Difference);
                                long period = DateUtil.dateDiff(depDt, depMatDt);
                                //   nextCalsDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(acctDtlMap.get("NEXT_INT_APPL_DT")));
                                //Added by chithra for  calculating  full Interest of  Matured deposits 
                                if (DateUtil.dateDiff(depMatDt, currentDate) >= 0) {
                                    nextCalsDt = currentDate;
                                }
                                //nextCalsDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(acctDtlMap.get("NEXT_INT_APPL_DT")));
                                //System.out.println("################ nextCalsDt : " + nextCalsDt);
                                paramMap.put("DEPOSIT_DT", depDt);
                                paramMap.put("MATURITY_DT", depMatDt);
                                paramMap.put("AMOUNT", acctDtlMap.get("DEPOSIT_AMT"));
                                paramMap.put("CATEGORY_ID", acctDtlMap.get("CATEGORY"));
                                paramMap.put("BEHAVES_LIKE", "FIXED");
                                paramMap.put("PERIOD1", new Double(period));
                                paramMap.put("END", nextCalsDt);
                                paramMap.put("FREQ", acctDtlMap.get("FREQ"));
                                if (DateUtil.dateDiff(lstIntCrDt, lstproDt) >= 0) {
                                    paramMap.put("START", lstproDt);
                                } else {
                                    paramMap.put("START", lstIntCrDt);
                                }
                                if (DateUtil.dateDiff(nextCalsDt, depMatDt) > 0) {
                                    paramMap.put("END", nextCalsDt);
                                } else {
                                    paramMap.put("END", depMatDt);
                                }
//                                if (!isTransaction) {
                                //    System.out.println("rrrrrrrrrrrrractual deposit date"+depDt);
                                lstIntCrDt = (Date) depDt.clone();
                                Date monthEndDate = new Date(2013, 02, 31);
                                int fr = 0;
                                int fre = CommonUtil.convertObjToInt(paramMap.get("INT_PROV_FREQ"));
                                Date diffDt = DateUtil.addDays(lstproDt, -fre);
                                //Added By Suresh
                                if (!isTransaction && Tot_Int_Cr < Tot_Int_AMT) {
                                    objInterestBatchTO = new InterestBatchTO();
                                    objInterestBatchTO.setIntRate(CommonUtil.convertObjToDouble(acctDtlMap.get("RATE_OF_INT")).doubleValue());
                                    objInterestBatchTO.setIntType("SIMPLE");
                                    objInterestBatchTO.setPrincipleAmt(CommonUtil.convertObjToDouble(acctDtlMap.get("DEPOSIT_AMT")));
                                    objInterestBatchTO.setProductType(CommonUtil.convertObjToStr("TD"));
                                    objInterestBatchTO.setLastTdsApplDt(currentDate);
                                    objInterestBatchTO.setTrnDt(currentDate);
                                    objInterestBatchTO.setDrCr("CREDIT");
                                    objInterestBatchTO.setBranch_code(CommonUtil.convertObjToStr(paramMap.get("BRANCH_CODE")));
                                    objInterestBatchTO.setUser_id(CommonUtil.convertObjToStr(paramMap.get("USER_ID")));
                                    objInterestBatchTO.setAcHdId(CommonUtil.convertObjToStr(paramMap.get("AC_HD_ID")));
                                    objInterestBatchTO.setActNum(CommonUtil.convertObjToStr(acctDtlMap.get("DEPOSIT_NO")));
                                    objInterestBatchTO.setIntDt(getProperFormatDate(lstIntCrDt));
                                    objInterestBatchTO.setApplDt(getProperFormatDate(nextCalsDt));
                                    objInterestBatchTO.setSlNo(renewalCount);
                                    //commented by rishad
                                    // interestApplictionDeleteInsert(objInterestBatchTO, true);
                                    long pr = DateUtil.dateDiff(lstIntCrDt, nextCalsDt) + 1;
                                    double roi = CommonUtil.convertObjToDouble(acctDtlMap.get("RATE_OF_INT")).doubleValue();
                                    double amount = CommonUtil.convertObjToDouble(acctDtlMap.get("DEPOSIT_AMT")).doubleValue();
                                    fr = CommonUtil.convertObjToInt(acctDtlMap.get("FREQ"));
                                    if (fr == 30 && CommonUtil.convertObjToStr(paramMap.get("DISCOUNTED_RATE")).equals("Y")) {
                                      //modified by rishad 04/2016(mm/yyyy) for deduction rate
                                        double period1 = 0.0;
                                        double interestAmt1 = 0.0;
                                        double interestAmt2 = 0.0;
                                        double calcAmt = 0.0;
                                        Date depDt1 = null;
                                        depDt1 = (Date) lstIntCrDt.clone();
                                        // depDt1=(Date)lstIntCrDt.clone();
                                        //  nextCalsDt = DateUtil.addDays(lstIntCrDt, fr);
                                        nextCalsDt = DateUtil.nextCalcDate(depDt1, lstIntCrDt, fr);
                                        pr = DateUtil.dateDiff(lstIntCrDt, nextCalsDt);
                                        if (lstIntCrDt.getDate() == 1) {                  //Checking for Only Deposit Date is One
                                            pr = 30;        //If Account Opened On 1st of the Month, Period Should be 30 (i.e) One Month
                                        }
                                        interestAmt1 = roi / 4 / (Math.pow((1 + (roi / 1200)), 2) + (1 + (roi / 1200)) + 1);    // This Is Calculating First Month of Interest
                                        period1 = (double) pr / 30;
                                        period1 = (double) getNearest((long) (period1 * 100), 100) / 100;
                                        interestAmt1 = interestAmt1 * period1;
                                        calcAmt = amount / 100;
                                        interestAmt1 = interestAmt1 * calcAmt;
                                        interestAmt1 = (double) getNearest((long) (interestAmt1 * 100), 100) / 100;
                                        objInterestBatchTO.setIntAmt(new Double(interestAmt1));
                                        objInterestBatchTO.setIntDt(getProperFormatDate(lstIntCrDt));
                                        objInterestBatchTO.setApplDt(getProperFormatDate(nextCalsDt));
                                        // lstIntCrDt = nextCalsDt;
                                        pr = DateUtil.dateDiff(lstIntCrDt, DateUtil.addDays(lstIntCrDt, CommonUtil.convertObjToInt(acctDtlMap.get("FREQ"))));
                                        interestApplictionDeleteInsert(objInterestBatchTO, false); //kannan
                                        //added by rishad
                                        Date lstIntCrDt1 = DateUtil.nextCalcDate(depDt1, lstIntCrDt, fr);
                                        //     if (DateUtil.dateDiff(lstIntCrDt1, currentDate) >= 0) {
                                        //                                                            lstIntCrDt=lstIntCrDt1;
                                        lstIntCrDt = lstIntCrDt1;
                                        for (lstIntCrDt = DateUtil.nextCalcDate(depDt1, lstIntCrDt, fr); DateUtil.dateDiff(lstIntCrDt, currentDate) >= 0; lstIntCrDt = DateUtil.nextCalcDate(depDt1, lstIntCrDt, fr)) {
                                            if (DateUtil.dateDiff(nextCalsDt, depMatDt) >= 0) {
                                                //end
                                                //for (lstIntCrDt = DateUtil.nextCalcDate(monthEndDate, lstIntCrDt, 30); DateUtil.dateDiff(lstIntCrDt, currentDate) >= 0; lstIntCrDt = DateUtil.nextCalcDate(monthEndDate, lstIntCrDt, 30)) {         // Calculating 2nd,3rd.. etc Month Interest
                                                //   if (DateUtil.dateDiff(nextCalsDt, depMatDt) >= 0) {
                                                period1 = (double) pr / 30;
                                                period1 = (double) getNearest((long) (period1 * 100), 100) / 100;
                                                interestAmt2 = roi / 4 / (Math.pow((1 + (roi / 1200)), 2) + (1 + (roi / 1200)) + 1);
                                                interestAmt2 = interestAmt2 * period1;
                                                //     System.out.println("calc amount inside"+calcAmt);
                                                interestAmt2 = interestAmt2 * calcAmt;
                                                //  System.out.println("interest amount inside,m,,"+interestAmt2);
                                                interestAmt2 = (double) getNearest((long) (interestAmt2 * 100), 100) / 100;
                                                interestAmt1 = interestAmt1 + interestAmt2;
                                                objInterestBatchTO.setIntAmt(new Double(interestAmt2));
                                                objInterestBatchTO.setIntDt(getProperFormatDate(DateUtil.addDays(nextCalsDt, 1)));
                                                objInterestBatchTO.setApplDt(getProperFormatDate(lstIntCrDt));
                                                //          interestApplictionDeleteInsert(objInterestBatchTO, false);
                                                nextCalsDt = lstIntCrDt;
                                            }
                                        }
                                        // System.out.println("interstamount   last:"+interestAmt1);
                                        interestAmt = interestAmt1;
                                        intTrfAmt = interestAmt;
                                        lstIntCrDt = DateUtil.addDays(lstIntCrDt, -30);
                                        java.sql.Timestamp nextDt = new java.sql.Timestamp(nextCalsDt.getYear(), nextCalsDt.getMonth(), nextCalsDt.getDate(), nextCalsDt.getHours(), nextCalsDt.getMinutes(), nextCalsDt.getSeconds(), 0);
                                        paramMap.put("END", nextDt);
                                        if (Tot_Int_AMT > 0 && DateUtil.dateDiff(depMatDt, nextCalsDt) >= 0) {
                                            interestAmt = Tot_Int_AMT - Tot_Int_Dr;
                                            intTrfAmt = interestAmt;
                                            nextCalsDt = depMatDt;
                                            paramMap.put("END", depMatDt);
                                        } else {
                                            interestAmt -= Tot_Int_Dr;
                                            intTrfAmt = interestAmt;
                                        }
                                        //  System.out.println("############ DISCOUNTED INT_TRF_AMT : " + intTrfAmt);
                                    } else if (fr == 90 || fr == 180 || fr == 360 || fr == 60 || fr == 120 || fr == 150 || fr == 210 || fr == 240 || fr == 270 || fr == 300 || fr == 330
                                            || ((CommonUtil.convertObjToStr(paramMap.get("DISCOUNTED_RATE")).equals("N")) && fr != 0)
                                            || (CommonUtil.convertObjToStr(paramMap.get("DISCOUNTED_RATE")).equals("N") && fr == 30)) {
                                        double interestAmt1 = 0.0;
                                        double interestAmt2 = 0.0;
                                        fr = CommonUtil.convertObjToInt(acctDtlMap.get("FREQ"));
                                        double period1 = 0.0;
                                        long pr1 = DateUtil.dateDiff(lstIntCrDt, nextCalsDt);
                                        pr = pr1;
                                        roi = 0.0;
                                        double calcAmt = CommonUtil.convertObjToDouble(acctDtlMap.get("DEPOSIT_AMT")).doubleValue();
                                        roi = CommonUtil.convertObjToDouble(acctDtlMap.get("RATE_OF_INT")).doubleValue();
                                        if (DateUtil.dateDiff(lstproDt, lstIntCrDt) > 0 && DateUtil.dateDiff(nextCalsDt, depMatDt) >= 0) {
                                            for (lstIntCrDt = DateUtil.addDays(lstIntCrDt, fr); DateUtil.dateDiff(lstIntCrDt, currentDate) >= 0; lstIntCrDt = DateUtil.addDays(lstIntCrDt, freq(lstIntCrDt, fr))) {
                                                if (DateUtil.dateDiff(nextCalsDt, depMatDt) >= 0) {
                                                    interestAmt2 = (fr * roi * calcAmt) / (1200 * 30);
                                                    interestAmt2 = (double) getNearest((long) (interestAmt2 * 100), 100) / 100;
                                                    interestAmt1 = interestAmt1 + interestAmt2;
                                                    objInterestBatchTO.setIntAmt(new Double(interestAmt2));
                                                    objInterestBatchTO.setIntDt(getProperFormatDate(DateUtil.addDays(nextCalsDt, 1)));
                                                    objInterestBatchTO.setApplDt(getProperFormatDate(lstIntCrDt));
                                                    //interestApplictionDeleteInsert(objInterestBatchTO, false);
                                                    nextCalsDt = lstIntCrDt;
                                                }
                                            }
                                            intTrfAmt = interestAmt;
                                            lstIntCrDt = DateUtil.addDays(lstIntCrDt, -fr);
                                            java.sql.Timestamp tsnextDate = new java.sql.Timestamp(nextCalsDt.getYear(), nextCalsDt.getMonth(), nextCalsDt.getDate(), nextCalsDt.getHours(), nextCalsDt.getMinutes(), nextCalsDt.getSeconds(), 0);
                                            paramMap.put("END", tsnextDate);
                                            if (Tot_Int_AMT > 0 && DateUtil.dateDiff(depMatDt, nextCalsDt) >= 0) {
                                                interestAmt = Tot_Int_AMT - (Tot_Int_Dr+Int_Difference);
                                            //    System.out.println("interest amount 3"+interestAmt);
                                                intTrfAmt = interestAmt;
                                                nextCalsDt = depMatDt;
                                            } else {
                                                interestAmt -= (Tot_Int_Dr+Int_Difference);
                                             //   System.out.println("interest amount 4"+interestAmt);
                                                intTrfAmt = interestAmt;
                                            }
                                        } else if (DateUtil.dateDiff(lstproDt, lstIntCrDt) <= 0 && DateUtil.dateDiff(nextCalsDt, depMatDt) >= 0) {

                                            double DiffAmt = 0.0;
                                            if (DateUtil.dateDiff(lstIntCrDt, depDt) != 0 || DateUtil.dateDiff(lstproDt, depDt) != 0) { // for the purpose of renwal after
                                                pr = DateUtil.dateDiff(lstIntCrDt, lstproDt);
                                                if (DateUtil.dateDiff(lstIntCrDt, depDt) >= 0) {
                                                }
                                                pr = pr + 1; //FRO MHCB
                                                period1 = pr * 30;
                                                period1 = period1 / 30;
                                                DiffAmt = (period1 * roi * calcAmt) / 36500;

                                                DiffAmt = (double) getNearest((long) (DiffAmt * 100), 100) / 100;
                                            }
                                            Date depDt1 = null;
                                            depDt1 = (Date) lstIntCrDt.clone();
                                            //  nextCalsDt = DateUtil.addDays(lstIntCrDt, fr);
                                            nextCalsDt = DateUtil.nextCalcDate(depDt1, lstIntCrDt, fr);
                                            if (DateUtil.dateDiff(nextCalsDt, currentDate) >= 0) {
                                                pr = DateUtil.dateDiff(lstIntCrDt, nextCalsDt);
                                                int freq = 30;
                                                freq = freq(lstIntCrDt, freq);
                                                period1 = pr / freq;
                                                period1 = (double) getNearest((long) (period1 * 100), 100) / 100;
                                                interestAmt = (period1 * roi * calcAmt) / 1200;
                                                if (depInt != null && depInt.equals("N")) {
                                                    interestAmt = (double) getNearest((long) (interestAmt * 100), 100) / 100;
                                                }
                                            }
                                            intTrfAmt = interestAmt;
                                            interestAmt = interestAmt - DiffAmt;
                                            if (Tot_Int_AMT > 0 && DateUtil.dateDiff(depMatDt, nextCalsDt) >= 0) {
                                                interestAmt = Tot_Int_AMT - Tot_Int_Cr;
                                                intTrfAmt = interestAmt + DiffAmt;
                                                nextCalsDt = depMatDt;
                                            }
                                            paramMap.put("END", nextCalsDt);
                                            paramMap.put("START", lstproDt);
                                            interestAmt1 = 0.0;
                                            interestAmt2 = 0.0;
                                            Date lstIntCrDt1 = DateUtil.nextCalcDate(depDt1,lstIntCrDt, fr);
                                            if (DateUtil.dateDiff(lstIntCrDt1, currentDate) >= 0) {
                                                //                                                            lstIntCrDt=lstIntCrDt1;
                                                lstIntCrDt = lstIntCrDt1;
                                                int i = 0;
                                               // lstIntCrDt = DateUtil.addDays(lstIntCrDt, freq(lstIntCrDt, fr))
                                                for (lstIntCrDt = DateUtil.nextCalcDate(depDt1,lstIntCrDt, fr); DateUtil.dateDiff(lstIntCrDt, currentDate) >= 0; lstIntCrDt = DateUtil.nextCalcDate(depDt1, lstIntCrDt, fr)) {
                                                    if (DateUtil.dateDiff(nextCalsDt, depMatDt) >= 0) {
                                                        i = i + 1;
                                                        interestAmt2 = (calcAmt * roi * fr) / (1200 * 30);
                                                        if (depInt != null && depInt.equals("N")) {
                                                            interestAmt2 = (double) getNearest((long) (interestAmt2 * 100), 100) / 100;
                                                        }
                                                        //         System.out.println("########## inside loop Interest amount 2nd"+interestAmt+"next CALC DATE "+nextCalsDt +"last int cal date"+lstIntCrDt);
                                                        interestAmt1 = interestAmt1 + interestAmt2;
                                                        objInterestBatchTO.setIntAmt(new Double(interestAmt2));
                                                        objInterestBatchTO.setIntDt(getProperFormatDate(DateUtil.addDays(nextCalsDt, 1)));
                                                        objInterestBatchTO.setApplDt(getProperFormatDate(lstIntCrDt));
                                                        //    interestApplictionDeleteInsert(objInterestBatchTO, false);
                                                        nextCalsDt = lstIntCrDt;
                                                    }
                                                }
                                                interestAmt = interestAmt + interestAmt1;
                                                intTrfAmt = interestAmt1 + intTrfAmt;
                                                lstIntCrDt = DateUtil.addDays(lstIntCrDt, -fr);
                                                paramMap.put("END", nextCalsDt);
                                                if (Tot_Int_AMT > 0 && DateUtil.dateDiff(depMatDt, nextCalsDt) >= 0) {
                                                    interestAmt = Tot_Int_AMT - (Tot_Int_Cr + Int_Difference);
                                                    // System.out.println("interest amount"+interestAmt);
                                                    intTrfAmt = interestAmt + DiffAmt;
                                                } else {
                                                    interestAmt -= (Tot_Int_Dr + Int_Difference);
                                                    //  System.out.println("interest amount 2"+interestAmt);
                                                    intTrfAmt = interestAmt;
                                                }
                                            }
                                            if (depInt != null && depInt.equals("Y")) {
                                                intTrfAmt = (double) getNearest((long) (intTrfAmt * 100), 100) / 100;
                                            }
                                            if (limitAmount >= intTrfAmt) {
                                                interestAmt = 0;
                                                intTrfAmt = 0;
                                            }
                                        }
                                    } else if (acctDtlMap.containsKey("FREQ") && CommonUtil.convertObjToInt(acctDtlMap.get("FREQ")) == 0) {
                                        //  System.out.println("################## In side On Maturity Fixed (Freq is Zero) ");
                                        long onMatPer = 0;
                                        onMatPer = DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("START"))), DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("END"))));
                                        //System.out.println("############## onMatPer   : " + onMatPer);
                                        if (DateUtil.dateDiff(lstIntCrDt, depDt) <= 0 && DateUtil.dateDiff(lstIntCrDt, lstproDt) < 0) {
                                            onMatPer = onMatPer + 1;
                                        }
                                        double calcAmt = CommonUtil.convertObjToDouble(acctDtlMap.get("DEPOSIT_AMT")).doubleValue();
                                        //System.out.println("############## calcAmt     : " + calcAmt);
                                        roi = CommonUtil.convertObjToDouble(acctDtlMap.get("RATE_OF_INT")).doubleValue();
                                        //System.out.println("############## roi         : " + roi);
                                        interestAmt = (onMatPer * roi * calcAmt) / 36500;
                                        //System.out.println("############## interestAmt : " + interestAmt);
                                        interestAmt = (double) getNearest((long) (interestAmt * 100), 100) / 100;
                                        objInterestBatchTO.setIntAmt(new Double(interestAmt));
                                        objInterestBatchTO.setIntDt(getProperFormatDate(lstIntCrDt));
                                        objInterestBatchTO.setApplDt(getProperFormatDate(nextCalsDt));
                                       // interestApplictionDeleteInsert(objInterestBatchTO, false);
                                        intTrfAmt = Tot_Int_AMT;
                                        if (Tot_Int_AMT > 0 && DateUtil.dateDiff(depMatDt, nextCalsDt) >= 0) {
                                            interestAmt = Tot_Int_AMT - Tot_Int_Cr;
                                            intTrfAmt = interestAmt;
                                            intTrfAmt = Tot_Int_AMT;
                                        }
                                    } else {
                                        //    System.out.println("########################## Monthly Interest with full quarter ");
                                        double period1 = 0.0;
                                        period1 = pr / 30;
                                        roi = CommonUtil.convertObjToDouble(acctDtlMap.get("RATE_OF_INT")).doubleValue();
                                        double calcAmt = CommonUtil.convertObjToDouble(acctDtlMap.get("DEPOSIT_AMT")).doubleValue();
                                        interestAmt = (period1 * roi * calcAmt) / 1200;
                                        interestAmt = (double) getNearest((long) (interestAmt * 100), 100) / 100;
                                        objInterestBatchTO.setIntAmt(new Double(interestAmt));
                                        objInterestBatchTO.setIntDt(getProperFormatDate(lstIntCrDt));
                                        objInterestBatchTO.setApplDt(getProperFormatDate(nextCalsDt));
                                       // interestApplictionDeleteInsert(objInterestBatchTO, false);
                                        intTrfAmt = interestAmt;
                                    }
                                    //System.out.println("############### Dep_Interest_Display");
                                } 
                                
                                // TDS calculation for displaying in front end
                                  if ((!isTransaction) && prodTDSDeduction && interestAmt > 0) {    //Added By Suresh
                           //         System.out.println("####### TDS YES : " + paramMap);
                                    tds = new TdsCalc(actBranch);       //Calling TDS Task
                                    String CustId = CommonUtil.convertObjToStr(paramMap.get("CUST_ID"));
                                    String Prod_type = CommonUtil.convertObjToStr(paramMap.get("PROD_TYPE"));
                                    String prod_id = CommonUtil.convertObjToStr(paramMap.get(InterestTaskRunner.PRODUCT_ID));
                                    String accnum = CommonUtil.convertObjToStr(paramMap.get("ACT_NUM"));
                                    tdsMap = new HashMap();
                                    Date endDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("END")));
                                    Date applnDt1 = (Date) currentDate.clone();
                                    custMap = new HashMap();
                                    if (endDate.getDate() > 0) {
                                        applnDt1.setDate(endDate.getDate());
                                        applnDt1.setMonth(endDate.getMonth());
                                        applnDt1.setYear(endDate.getYear());
                                    }
                                    String actno = CommonUtil.convertObjToStr(paramMap.get("ACT_NUM"));
                                    if (actno.lastIndexOf("_") != -1) {
                                        actno = actno.substring(0, actno.lastIndexOf("_"));
                                    }
                                    custMap.put("DEPOSIT_NO", actno);
                                    List depAccList = (List) sqlMap.executeQueryForList("getTDSAccountLevel", custMap);
                                    if (depAccList != null && depAccList.size() > 0) {
                                        tdsMap.put("INT_DATE", applnDt1);
                                        tdsMap.put("CUSTID", CustId);
                                        List exceptionList = (List) sqlMap.executeQueryForList("getTDSExceptionData", tdsMap);
                                        if (exceptionList == null || exceptionList.size() <= 0) {
                                            custMap.put("CUST_ID", CustId);
                                            List custList = (List) sqlMap.executeQueryForList("getCustData", custMap);
                                            if (custList != null && custList.size() > 0) {
                                                custMap = (HashMap) custList.get(0);
                                                if (CommonUtil.convertObjToStr(custMap.get("PAN_NUMBER")).length() > 0) {
                                                    tds.setPan(true);
                                                } else {
                                                    tds.setPan(false);
                                                }
                                            }
                                            tds.setIsTransaction(false);
                                            tdsMap = new HashMap();
                                            tdsMap = tds.tdsCalcforInt(CustId, interestAmt, accnum, Prod_type, prod_id, null); //Calling TDS Task
                                   //         System.out.println("############## After Calculated tdsMap : " + tdsMap);
                                            if (tdsMap != null && tdsMap.size() > 0) {
                                                tdsDeductedForBaseAccount = CommonUtil.convertObjToDouble(tdsMap.get("TDSDEDUCTEDTOBASEACCOUNT")).doubleValue();
                                            }
                                        }
                                    }
                                }
                                
                                // End
                                //Transaction Starting
                                if (isTransaction) {
//                                    //RISHAD START
                                    if (cashToTransferMap != null && cashToTransferMap.size() > 0) {
                                        acctDtlMap.put("INTPAY_MODE", "TRANSFER");
                                        if (cashToTransferMap.containsKey("PROD_TYPE") && cashToTransferMap.get("PROD_TYPE").equals(TransactionFactory.GL)) {
                                            acctDtlMap.put("INT_PAY_ACC_NO", cashToTransferMap.get("ACC_HEAD"));
                                            acctDtlMap.put("INT_PAY_PROD_TYPE", TransactionFactory.GL);
                                            acctDtlMap.put("INT_PAY_PROD_ID", null);
                                        } else {
                                            acctDtlMap.put("INT_PAY_ACC_NO", cashToTransferMap.get("OPERATIVE_NO"));
                                            //acctDtlMap.put("INT_PAY_PROD_TYPE", "OA");
                                            acctDtlMap.put("INT_PAY_PROD_TYPE", cashToTransferMap.get("PROD_TYPE"));
                                            acctDtlMap.put("INT_PAY_PROD_ID", cashToTransferMap.get("PROD_ID"));
                                        }
                                    }
                                    interestAmt = CommonUtil.convertObjToDouble(paramMap.get("INTEREST_AMOUNT")).doubleValue();
                                    intTrfAmt = CommonUtil.convertObjToDouble(paramMap.get("INTEREST_AMOUNT")).doubleValue();
                                    tdsAmount = CommonUtil.convertObjToDouble(paramMap.get("TDS_AMOUNT")).doubleValue();

                                    sqlMap.startTransaction();  // Start transaction enabled by Revathi
                                    double roi = 0.0;
                                    double tdsDeductAmt = 0.0;
                                    TransferTrans transferTrans = new TransferTrans();
                                    ArrayList batchList = new ArrayList();
                                    tds = new TdsCalc(actBranch);
                                    String CustId = CommonUtil.convertObjToStr(paramMap.get("CUST_ID"));
                                    String Prod_type = CommonUtil.convertObjToStr(paramMap.get("PROD_TYPE"));
                                    String prod_id = CommonUtil.convertObjToStr(paramMap.get(InterestTaskRunner.PRODUCT_ID));
                                    String accnum = CommonUtil.convertObjToStr(paramMap.get("ACT_NUM"));
                                    tdsMap = new HashMap();
                                    Date applDt1 = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("END")));
                                    Date applnDt1 = (Date) currentDate.clone();
                                    if (applDt1.getDate() > 0) {
                                        applnDt1.setDate(applDt1.getDate());
                                        applnDt1.setMonth(applDt1.getMonth());
                                        applnDt1.setYear(applDt1.getYear());
                                    }
                                    tdsMap.put("INT_DATE", applnDt1);
                                    tdsMap.put("CUSTID", CustId);
                                    tdsMap = tds.getTDSHead(CustId);
                                    tdsMap.put("TDSDRAMT",tdsAmount);
                                    tdsMap.put("TDSDEDUCTEDTOBASEACCOUNT", tdsAmount);
                                  //  System.out.println("tdsMap ::" + tdsMap);
                                    //comented at 06/02/2015  no need tds calculation in kerala scb banks approved by soji (with presence of jibi)
//                                    List exceptionList = (List) sqlMap.executeQueryForList("getTDSExceptionData", tdsMap);
//                                    if (exceptionList == null || exceptionList.size() <= 0) {
//                                        tdsMap = new HashMap();
//                                        tdsMap = tds.tdsCalcforInt(CustId, interestAmt, accnum, Prod_type, prod_id, null);
//                                    }
                                    //tdsMap = null;
                                    objInterestBatchTO = new InterestBatchTO();
                                    objInterestBatchTO.setActNum(CommonUtil.convertObjToStr(paramMap.get("ACT_NUM")));
                                    objInterestBatchTO.setIsTdsApplied("N");
                                    roi = CommonUtil.convertObjToDouble(acctDtlMap.get("RATE_OF_INT")).doubleValue();
                                    objInterestBatchTO.setAcHdId(CommonUtil.convertObjToStr(paramMap.get("CREDIT_INT")));
                                    objInterestBatchTO.setIntAmt(new Double(interestAmt));
                                    objInterestBatchTO.setIntRate(new Double(roi));
                                    objInterestBatchTO.setIntType("SIMPLE");
                                    objInterestBatchTO.setPrincipleAmt(CommonUtil.convertObjToDouble(acctDtlMap.get("DEPOSIT_AMT")));

                                    /*if (tdsMap != null) {
                                        tdsDeductedForBaseAccount = CommonUtil.convertObjToDouble(tdsMap.get("TDSDEDUCTEDTOBASEACCOUNT")).doubleValue();
                                        tdsDeductAmt = CommonUtil.convertObjToDouble(tdsMap.get("TDSDRAMT")).doubleValue();
                                        if (tdsDeductedForBaseAccount > 0) {
                                            objInterestBatchTO.setIsTdsApplied("Y");
                                            objInterestBatchTO.setTdsAmt(new Double(tdsDeductedForBaseAccount));
                                            //                                                        interestAmt=interestAmt-tdsDeductAmt;
                                            objInterestBatchTO.setIntAmt(new Double(interestAmt - tdsDeductAmt));
                                            objInterestBatchTO.setTdsDeductedFromAll(new Double(tdsDeductAmt));
                                            objInterestBatchTO.setTotalTdsAmt(new Double(tdsDeductedForBaseAccount));
                                            objInterestBatchTO.setLastTdsRecivedFrom(CommonUtil.convertObjToStr(tdsMap.get("DEBIT_ACT_NUM")));
                                            //                                                        objInterestBatchTO.setLastTdsRecivedFrom(CommonUtil.convertObjToStr(paramMap.get("ACT_NUM")));
                                            objInterestBatchTO.setLastTdsApplDt(currentDate);
                                        }
//                                        if (tdsDeductAmt > 0) {
//                                            HashMap txMap = new HashMap();
//                                            txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(tdsMap.get("TDSCrACHdId")));
//                                            txMap.put(TransferTrans.CR_PROD_TYPE, "GL");
//                                            txMap.put(TransferTrans.CURRENCY, "INR");
//                                            txMap.put(TransferTrans.PARTICULARS, accnum + "TdsDeducted");
//                                            txMap.put(TransferTrans.CR_BRANCH, actBranch);
//                                            txMap.put("generateSingleTransId", generateSingleTransId);
//                                            txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);
//                                            tdsDeductAmt = CommonUtil.convertObjToDouble(tdsMap.get("TDSDRAMT")).doubleValue();
//                                            batchList.add(transferTrans.getCreditTransferTO(txMap, tdsDeductAmt));
//                                            intTrfAmt = intTrfAmt - tdsDeductAmt;
//                                            objInterestBatchTO.setActNum(CommonUtil.convertObjToStr(paramMap.get("ACT_NUM")));
//                                        }
                                    }*/
                                    
                                    if (tdsMap != null && tdsMap.size() > 0) { // Added by nithya on 06-02-2020 for KD-1090
                                        tdsDeductedForBaseAccount = CommonUtil.convertObjToDouble(tdsMap.get("TDSDEDUCTEDTOBASEACCOUNT")).doubleValue();
                                        tdsDeductAmt = CommonUtil.convertObjToDouble(tdsMap.get("TDSDRAMT")).doubleValue();
                                        if (tdsDeductedForBaseAccount > 0) {
                                            objInterestBatchTO.setIsTdsApplied("Y");
                                            objInterestBatchTO.setTdsAmt(new Double(tdsDeductedForBaseAccount));
                                            objInterestBatchTO.setIntAmt(new Double(interestAmt));
                                            objInterestBatchTO.setTdsDeductedFromAll(new Double(tdsDeductAmt));
                                            objInterestBatchTO.setTotalTdsAmt(new Double(tdsDeductedForBaseAccount));
                                            objInterestBatchTO.setLastTdsRecivedFrom(CommonUtil.convertObjToStr(tdsMap.get("DEBIT_ACT_NUM")));
                                            objInterestBatchTO.setLastTdsApplDt(currentDate);
                                        }
                                    }

                                    Date applnDt = (Date) currentDate.clone();
                                    Date applDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("END")));
                                    if (applDt.getDate() > 0) {
                                        applnDt.setDate(applDt.getDate());
                                        applnDt.setMonth(applDt.getMonth());
                                        applnDt.setYear(applDt.getYear());
                                    }
                                    objInterestBatchTO.setApplDt(applnDt);
                                    //                                    }
                                    interestAmt = (double) getNearest((long) (interestAmt * 100), 100) / 100;
                                    double Tot_Int_bal = (interestAmt + Tot_Int_Cr) - Tot_Int_Dr - tdsDeductAmt;
                                    HashMap resultMap = new HashMap();
                                    Date nxtCalDt = null;
                                    if (CommonUtil.convertObjToStr(acctDtlMap.get("CALENDER_FREQ")).length() > 0 && CommonUtil.convertObjToStr(acctDtlMap.get("CALENDER_FREQ")).equals("Y")) {
                                        do {
                                            nxtCalDt = DateUtil.addDays(nextCalsDt, CommonUtil.convertObjToInt(acctDtlMap.get("FREQ")));
                                            nextCalsDt = DateUtil.addDays(nextCalsDt, CommonUtil.convertObjToInt(acctDtlMap.get("FREQ")));
                                            objInterestBatchTO.setApplDt(nxtCalDt);
                                            applnDt = nextCalsDt;
                                        } while (DateUtil.dateDiff(DateUtil.addDays(nextCalsDt, CommonUtil.convertObjToInt(acctDtlMap.get("FREQ"))), currentDate) >= 0);
                                    } else {
                                        nextCalsDt = (Date) currentDate.clone();
                                        Date uptoDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("INT_CALC_UPTO_DT")));
                                        //    System.out.println("rish.......................123"+uptoDt);
                                        if (uptoDt.getDate() > 0) {
                                            nextCalsDt.setDate(uptoDt.getDate());
                                            nextCalsDt.setMonth(uptoDt.getMonth());
                                            nextCalsDt.setYear(uptoDt.getYear());
                                            applnDt = nextCalsDt;
                                        }

                                        nxtCalDt = nextCaldate(depDt, nextCalsDt, acctDtlMap);
                                    }
                                    resultMap = prepareMap(paramMap, resultMap);
                                    Date apStDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("START")));
                                    Date apEdDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("END")));
                                    apStDt = DateUtil.addDays(apStDt, 1);
                                    if (interestAmt < 0 && DateUtil.dateDiff(apStDt, apEdDt) == 0) {
                                        resultMap.put(TransferTrans.DR_AC_HD, (String) paramMap.get("CREDIT_INT"));
                                        resultMap.put(TransferTrans.CR_AC_HD, (String) paramMap.get("DEBIT_INT"));
                                        interestAmt = interestAmt * -1;
                                    }
                                    resultMap.put("LINK_BATCH_ID", paramMap.get("ACT_NUM"));
                                    resultMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);
                                    transferTrans.setLinkBatchId(CommonUtil.convertObjToStr(paramMap.get("ACT_NUM")));
                                    batchList.add(transferTrans.getDebitTransferTO(resultMap, interestAmt));
                                    batchList.add(transferTrans.getCreditTransferTO(resultMap, interestAmt - tdsDeductAmt));
                                    transferTrans.setInitiatedBranch(actBranch);
                                    if (interestAmt != 0 || CommonUtil.convertObjToInt(acctDtlMap.get("FREQ")) == 0) {
//                                        transferTrans.doDebitCredit(batchList, branch); commented by shihad to block prov entry
                                    }
                                    Date nextDt = (Date) currentDate.clone();
                                    Date nextCalcDt = nxtCalDt;
                                    if (nextCalcDt.getDate() > 0) {
                                        nextDt.setDate(nextCalcDt.getDate());
                                        nextDt.setMonth(nextCalcDt.getMonth());
                                        nextDt.setYear(nextCalcDt.getYear());
                                    }
                                    Date currDt = (Date) currentDate.clone();
                                    Date stDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("START")));
                                    if (stDt.getDate() > 0) {
                                        currDt.setDate(stDt.getDate());
                                        currDt.setMonth(stDt.getMonth());
                                        currDt.setYear(stDt.getYear());
                                    }
                                    objInterestBatchTO.setProductId(CommonUtil.convertObjToStr(paramMap.get("PROD_ID")));
                                    objInterestBatchTO.setApplDt(applnDt);
                                    objInterestBatchTO.setIntDt(currDt);
                                    objInterestBatchTO.setTrnDt(currentDate);
                                    objInterestBatchTO.setDrCr("CREDIT");
                                    objInterestBatchTO.setTot_int_amt(new Double(Tot_Int_bal));
                                    objInterestBatchTO.setTransLogId("A");
                                    objInterestBatchTO.setCustId(CommonUtil.convertObjToStr(paramMap.get("CUST_ID")));
                                    //comented by rishad 03/07/2015
//                                    if (interestAmt != 0) {
//                                        if (tdsDeductAmt > 0.0) {
//                                            objInterestBatchTO.setIsTdsApplied("Y");
//                                            objInterestBatchTO.setTdsAmt(CommonUtil.convertObjToDouble(tdsMap.get("TDSDEDUCTEDTOBASEACCOUNT")));
//                                        }
//                                        sqlMap.executeUpdate("insertDepositInterestTO", objInterestBatchTO);
//                                    }
                                    objInterestBatchTO.setSlNo(renewalCount);
//                                    if (tdsDeductAmt > 0.0) { // Commented by nithya on 27-02-2020 for KD-1090
//                                        objInterestBatchTO.setIntAmt(new Double(tdsDeductAmt));
//                                        sqlMap.executeUpdate("updateTotIndDrawnAmount", objInterestBatchTO);
//                                        objInterestBatchTO.setIntAmt(new Double(0));
//                                    }
                                    HashMap upMapint = new HashMap();
                                    upMapint.put("actNum", paramMap.get("ACT_NUM"));
                                    //   System.out.println("rish.......................next appli date"+applnDt);
                                    upMapint.put("applDt", applnDt);
                                    //  System.out.println("rish.......................next appli date"+nextDt);
                                    upMapint.put("nextIntappldt", nextDt);
                                    upMapint.put("DEPOSIT_NO", paramMap.get("ACT_NUM"));
                                    List lst = sqlMap.executeQueryForList("getDepositAccountDetails", upMapint);
                                    if (lst != null && lst.size() > 0) {
                                        HashMap updateMap = (HashMap) lst.get(0);
                                        updateMap.put("TEMP_NEXT_INT_APPL_DT", updateMap.get("NEXT_INT_APPL_DT"));
                                        updateMap.put("TEMP_LAST_INT_APPL_DT", updateMap.get("LAST_INT_APPL_DT"));
                                        updateMap.put("INT_CREDIT", updateMap.get("TOTAL_INT_CREDIT"));
                                        updateMap.put("INT_DRAWN", updateMap.get("TOTAL_INT_DRAWN"));

                                        sqlMap.executeUpdate("updateDepositTempDate", updateMap);
                                    }
                                    //rishad doing
                                    double Tot_int = CommonUtil.convertObjToDouble(objInterestBatchTO.getIntAmt());
                                    String resultActNum = CommonUtil.convertObjToStr(paramMap.get("ACT_NUM"));
                                    resultActNum = resultActNum.lastIndexOf("_") != -1 ? resultActNum.substring(0, resultActNum.length() - 2) : resultActNum;
                                    double renewalIntPayable = getRenewalInterest(resultActNum);
                                    if (renewalIntPayable > 0) {
                                        double dedIntAmt = 0;
                                        dedIntAmt = Tot_int - renewalIntPayable;//total interest
                                        objInterestBatchTO.setIntAmt(dedIntAmt);
                                        HashMap lasteIntMap = new HashMap();
                                        lasteIntMap.put("DEPOSIT_NO", resultActNum);
                                        ArrayList reIntrstDateList = (ArrayList) sqlMap.executeQueryForList("getLastInterestDate", lasteIntMap);
                                        if (reIntrstDateList.size() > 0 && reIntrstDateList != null) {
                                            HashMap result = new HashMap();
                                            result = (HashMap) reIntrstDateList.get(0);
                                            paramMap.put("START", result.get("LAST_INT_APPL_DT"));
                                        }
                                    }
                                    paramMap.put("END",applnDt);
                                    sqlMap.executeUpdate("updateLastInterestAmount", objInterestBatchTO);
                                    objInterestBatchTO.setIntAmt(Tot_int);
                                    if (tdsDeductAmt > 0.0) {
                                        objInterestBatchTO.setIntAmt(new Double(0));
                                    }                                    
                                    sqlMap.executeUpdate("updateDepositIntLastApplDT", upMapint);
                                    sqlMap.executeUpdate("updateDepositNextIntApplDT", upMapint);

                                    intTrfAmt = (double) getNearest((long) (intTrfAmt * 100), 100) / 100;
                                    if (acctDtlMap.get("INTPAY_MODE").equals("TRANSFER")) {
                                        if (acctDtlMap.get("INT_PAY_PROD_TYPE") != null && CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_PROD_TYPE")).length() > 0) {
                                            HashMap accStatusMap = new HashMap();
                                            //Added By Suresh
                                            if (CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_PROD_TYPE")).equals("OA") || CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_PROD_TYPE")).equals("INV")
                                                    || CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_PROD_TYPE")).equals("AD") || CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_PROD_TYPE")).equals("TL")) {
                                                String ProdType = CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_PROD_TYPE"));
                                                if (ProdType.equals("TL")) {
                                                    ProdType = "AD";
                                                }
                                                accStatusMap.put("ACT_NUM", CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_ACC_NO")));
                                                List stLst = sqlMap.executeQueryForList("getAccountStatus" + ProdType, accStatusMap);
                                                accStatusMap = (HashMap) stLst.get(0);
                                            }
                                            if (!CommonUtil.convertObjToStr(accStatusMap.get("ACT_STATUS_ID")).equals("CLOSED") && !CommonUtil.convertObjToStr(accStatusMap.get("ACT_STATUS_ID")).equals(CommonConstants.COMPLETE_FREEZE)) {
                                       //         System.out.println("interest amount :: " + interestAmt +" Tot_Int_Dr :: " + Tot_Int_Dr +" tdsDeductAmt :: " + tdsDeductAmt);
                                                //intTrfAmt = (interestAmt + Tot_Int_Cr) - Tot_Int_Dr - tdsDeductAmt;
                                                intTrfAmt = (interestAmt + Tot_Int_Cr) - Tot_Int_Dr; // Added by nithya on 06-02-2020 for KD-1090
                                                if (behaves.equals(rec)) {
                                                    intTrfAmt = CommonUtil.convertObjToDouble(paramMap.get("AMT")).doubleValue();
                                                }
                                                if (behaves.equals(mds)) {
                                                    HashMap map = new HashMap();
                                                    TransactionTO transactionTO = new TransactionTO();
                                                    transactionDetailsTO = new LinkedHashMap();
                                                    LinkedHashMap transMap = new LinkedHashMap();
                                                    transactionTO.setTransType("TRANSFER");
                                                    transactionTO.setTransType("TRANSFER");
                                                    transactionTO.setTransAmt(CommonUtil.convertObjToDouble(((HashMap) accountList.get(acc)).get("TOTAL_DEMAND")));
                                                    transactionTO.setProductType("GL");
                                                    transactionTO.setDebitAcctNo(CommonUtil.convertObjToStr(paramMap.get(InterestTaskRunner.CREDIT_INT)));
                                                    transactionTO.setApplName(CommonUtil.convertObjToStr(paramMap.get(InterestTaskRunner.CREDIT_INT)));
                                                    transactionTO.setInstType("VOUCHER");
                                                    //                                                    transactionTo.setTransAmt(CommonUtil.convertObjToDouble(((HashMap)accountList.get(acc)).get("TOTAL_DEMAND")));
                                                    HashMap ChittalMap = new HashMap();
                                                    map.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(paramMap.get("USER_ID")));
                                                    map.put("BRANCH_CODE", actBranch);
                                                    transMap.put("1", transactionTO);
                                                    transactionDetailsTO.put("NOT_DELETED_TRANS_TOs", transMap);
                                                    map.put("TransactionTO", transactionDetailsTO);
                                                    ChittalMap.put("ACT_NUM", ((HashMap) accountList.get(acc)).get("ACCT_NUM"));
                                                    ChittalMap.put("PRINCIPAL", ((HashMap) accountList.get(acc)).get("PRINCIPAL"));
                                                    ChittalMap.put("TOTAL_DEMAND", ((HashMap) accountList.get(acc)).get("TOTAL_DEMAND"));
                                                    ChittalMap.put("INT_CALC_UPTO_DT", currentDate);
                                                    ChittalMap.put("PENAL_AMT", ((HashMap) accountList.get(acc)).get("PENAL_AMT"));
                                                    ChittalMap.put("BONUS_AMT", ((HashMap) accountList.get(acc)).get("BONUS_AMT"));
                                                    ChittalMap.put("DISCOUNT_AMT", ((HashMap) accountList.get(acc)).get("DISCOUNT_AMT"));
                                                    ChittalMap.put("INTEREST", ((HashMap) accountList.get(acc)).get("INTEREST"));
                                                    ChittalMap.put("CHARGES", ((HashMap) accountList.get(acc)).get("CHARGES"));
                                                    ChittalMap.put("ARBITRATION_AMT", ((HashMap) accountList.get(acc)).get("ARBITRATION_AMT"));
                                                    ChittalMap.put("NOTICE_AMT", ((HashMap) accountList.get(acc)).get("NOTICE_AMT"));
                                                    if (postageAmt > 0) {
                                                        ChittalMap.put("POSTAGE_AMT_FOR_INT", CommonUtil.convertObjToStr(new Double(postageAmt)));
                                                        ChittalMap.put("POSTAGE_ACHD", postageAcHd);
                                                    }
                                                    if (postageRenewAmt > 0) {
                                                        ChittalMap.put("POSTAGE_ACHD", postageAcHd);
                                                        ChittalMap.put("RENEW_POSTAGE_AMT", CommonUtil.convertObjToStr(new Double(postageRenewAmt)));
                                                    }
                                                    transactionPartMDS(ChittalMap, map);
                                                } else {
                                                    if (postageAmt > 0) {

                                                        acctDtlMap.put("POSTAGE_AMT", CommonUtil.convertObjToStr(new Double(postageAmt)));
                                                        acctDtlMap.put("POSTAGE_ACHD", postageAcHd);
                                                    }
                                                    if (postageRenewAmt > 0) {
                                                        acctDtlMap.put("POSTAGE_ACHD", postageAcHd);
                                                        acctDtlMap.put("RENEW_POSTAGE_AMT", CommonUtil.convertObjToStr(new Double(postageRenewAmt)));
                                                    }
                                                    acctDtlMap.put("INTEREST_APPL_TRANSFER_CUST_ID",CommonUtil.convertObjToStr(paramMap.get("CUST_ID")));
                                                    dointTransferToAccount(acctDtlMap, intTrfAmt, paramMap, tdsMap);
                                                    
                                                }
                                                 if (interestAmt != 0) {
                                                    if (tdsDeductAmt > 0.0) {
                                                        objInterestBatchTO.setIsTdsApplied("Y");
                                                        objInterestBatchTO.setTdsAmt(CommonUtil.convertObjToDouble(tdsMap.get("TDSDEDUCTEDTOBASEACCOUNT")));
                                                    }
                                                    objInterestBatchTO.setLastTdsRecivedFrom(CommonUtil.convertObjToStr(paramMap.get(paramMap.get("ACT_NUM"))));
                                                    objInterestBatchTO.setSlNo(renewalCount);
                                                    sqlMap.executeUpdate("insertDepositInterestTO", objInterestBatchTO);
                                                }
                                                if (interestList.size() == 0) {
                                                    interestList.add("");
                                                }
                                                if (acc == 0 || acc == accountListSize - 1) {
                                                    interestList.add(paramMap.get(paramMap.get("ACT_NUM")));
                                                }
                                                objInterestBatchTO.setIntAmt(new Double(intTrfAmt));
                                                objInterestBatchTO.setDrCr("DEBIT");
                                                Tot_Int_bal = Tot_Int_bal - intTrfAmt;
                                                objInterestBatchTO.setTot_int_amt(new Double(Tot_Int_bal));
                                                objInterestBatchTO.setLastTdsRecivedFrom(CommonUtil.convertObjToStr(paramMap.get(paramMap.get("ACT_NUM"))));
                                                sqlMap.executeUpdate("insertDepositInterestTO", objInterestBatchTO);
                                                Tot_int = CommonUtil.convertObjToDouble(objInterestBatchTO.getIntAmt());
                                                resultActNum = CommonUtil.convertObjToStr(paramMap.get("ACT_NUM"));
                                                resultActNum = resultActNum.lastIndexOf("_") != -1 ? resultActNum.substring(0, resultActNum.length() - 2) : resultActNum;
                                                renewalIntPayable = getRenewalInterest(resultActNum);
                                                if (renewalIntPayable > 0) {
                                                    double dedIntAmt = 0;
                                                    dedIntAmt = Tot_int - renewalIntPayable;//total interest
                                                    objInterestBatchTO.setIntAmt(dedIntAmt);
                                                }
                                                sqlMap.executeUpdate("updateTotIndDrawnAmount", objInterestBatchTO);
                                                if (renewalIntPayable > 0) {
                                                    HashMap whereMap = new HashMap();
                                                    whereMap.put("DEPOSIT_NO", resultActNum);
                                                    whereMap.put("PAID_DATE", currentDate.clone());
                                                    sqlMap.executeUpdate("upadateInterestApplicationDetails", whereMap);

                                                }
                                                HashMap multipletoSingleMap = new HashMap();
                                                multipletoSingleMap.put("CUST_ID",CommonUtil.convertObjToStr(paramMap.get("CUST_ID")));
                                                multipletoSingleMap.put("ACT_NUM",CommonUtil.convertObjToStr(paramMap.get("ACT_NUM")));
                                                multipletoSingleMap.put("STATUS_BY",CommonUtil.convertObjToStr(paramMap.get("USER_ID")));
                                                multipletoSingleMap.put("TRANS_DT",currentDate.clone());
                                                multipletoSingleMap.put("STATUS",CommonConstants.STATUS_CREATED);
                                                multipletoSingleMap.put("INT_AMT",objInterestBatchTO.getIntAmt());
                                                multipletoSingleMap.put("SCREEN_NAME","DEPOSIT_INTEREST_SCREEN");
                                                sqlMap.executeUpdate("insertMultipletoSingleMSGDeliver", multipletoSingleMap);
                                            }
                                            else {
                                                System.out.println("@#@#@#@#@##@#@# postgres changes:");
                                                //added by rishad 16-06-2015 for 0010756: Deposit intrest application -
                                                if (isTransaction) {
                                                    //sqlMap.rollbackTransaction();
                                                    if (errorMap == null) {
                                                        errorMap = new HashMap();
                                                    }
                                                    String actNum = CommonUtil.convertObjToStr(paramMap.get("ACT_NUM"));
                                                    actNum = actNum.lastIndexOf("_") != -1 ? actNum.substring(0, actNum.length() - 2) : actNum;
                                                    if (CommonUtil.convertObjToStr(accStatusMap.get("ACT_STATUS_ID")).equals(CommonConstants.COMPLETE_FREEZE)) {
                                                        errorMap.put(actNum, new TTException("This account has been freezed completely,\n cannot make transaction either debit or credit\n"));
                                                        if (interestList.size() >= 1) {
                                                            interestList.set(0, errorMap);
                                                        } else {
                                                            interestList.add(0, errorMap);
                                                        }
                                                        throw new TTException("This account has been freezed completely,\n cannot make transaction either debit or credit\n");
                                                    } else {
                                                        errorMap.put(actNum, new TTException("Account Number Closed"));
                                                        if (interestList.size() >= 1) {
                                                            interestList.set(0, errorMap);
                                                        } else {
                                                            interestList.add(0, errorMap);
                                                        }
                                                        throw new TTException("Int Pay Account is Closed");
                                                    }
                                                }
                                            }
                                        } else {
                                            throw new TTException("Interest Payment Product Type not found...");
                                        }
                                    } else {  //Cash Part
                                        List listData = ServerUtil.executeQuery("getCashierAuth", new HashMap());
                                        HashMap map1 = (HashMap) listData.get(0);
                                        if (map1.get("CASHIER_AUTH_ALLOWED") != null && map1.get("CASHIER_AUTH_ALLOWED").toString().equals("Y")) {
                                            dointCashInterest(intTrfAmt, paramMap, tdsMap); //tdsMap // Added by nithya on 06-02-2020 for KD-1090
                                            objInterestBatchTO.setLastTdsRecivedFrom("CASHIER_AUTH");
                                            sqlMap.executeUpdate("insertDepositInterestTO", objInterestBatchTO);
                                        } else {
                                            dointCash(intTrfAmt, paramMap, tdsMap); //tdsMap // Added by nithya on 06-02-2020 for KD-1090
                                            objInterestBatchTO.setLastTdsRecivedFrom(CommonUtil.convertObjToStr(paramMap.get(paramMap.get("ACT_NUM"))));
                                            sqlMap.executeUpdate("insertDepositInterestTO", objInterestBatchTO);
                                        }
                                        if (paramMap.containsKey(paramMap.get("ACT_NUM"))) {
                                            if (interestList.size() == 0) {
                                                  if (map1.get("CASHIER_AUTH_ALLOWED") != null && map1.get("CASHIER_AUTH_ALLOWED").toString().equals("N")) {
                                                       interestList.add("");
                                                  }
                                            }
                                            if (acc == 0 || acc == accountListSize - 1) {
                                                interestList.add(paramMap.get(paramMap.get("ACT_NUM")));
                                            }
                                        }
                                    }
                                    sqlMap.commitTransaction();  // End transaction enabled by Revathi
                                    //END  
                                } else {
                                    HashMap hmap = new HashMap();
                                    HashMap hshMap = null;
                                    HashMap postageMap = new HashMap();
                                    double postAmount = 0.0;
                                    double renewpostAmt = 0.0;
                                    interestRow = new ArrayList();
                                    String actNum = "";
                                    Date matDate = (Date) acctDtlMap.get("MATURITY_DT");
                                    long dateDiff;
                                    boolean flagM = Boolean.valueOf(paramMap.get("MATURITY").toString());
                                    if (flagM) {
                                        dateDiff = calculateDateDifference(currentDate, matDate);
                                    } else {
                                        dateDiff = calculateDateDifference(currentDate, matDate);
                                        if (dateDiff <= 0) {
                                            paramMap.put("END", matDate);
                                        }
                                        dateDiff = 1;
                                    }
                                    if (dateDiff >= 0) {
                                        double Tot_Int_bal = (interestAmt + Tot_Int_Cr) - Tot_Int_Dr;
                                        if (depInt != null && depInt.equals("Y")) {
                                            interestAmt = (double) getNearest((long) (interestAmt * 100), 100) / 100;
                                            Tot_Int_bal = (double) getNearest((long) (Tot_Int_bal * 100), 100) / 100;
                                        }
                                        interestRow.add(new Boolean(false));
                                        interestRow.add(paramMap.get("CUST_ID"));
                                        actNum = CommonUtil.convertObjToStr(paramMap.get("ACT_NUM"));
                                        postageMap.put("ACT_NUM", actNum);
                                        List postageList1 = sqlMap.executeQueryForList("getPostageAmount", postageMap);
                                        List renewPostageList1 = sqlMap.executeQueryForList("getRenewPostageAmount", postageMap);
                                        if (postageList1 != null && postageList1.size() > 0) {
                                            postageMap = (HashMap) postageList1.get(0);
                                            postAmount = CommonUtil.convertObjToDouble(postageMap.get("POSTAGE_AMT")).doubleValue();

                                        }
                                        if (renewPostageList1 != null && renewPostageList1.size() > 0) {
                                            postageMap = (HashMap) renewPostageList1.get(0);
                                            renewpostAmt = CommonUtil.convertObjToDouble(postageMap.get("RENEW_POSTAGE_AMT")).doubleValue();
                                        }
                                        actNum = actNum.lastIndexOf("_") != -1 ? actNum.substring(0, actNum.length() - 2) : actNum;

                                        double renewalIntPayable = getRenewalInterest(actNum);
                                        if (Tot_Int_bal > 0 && renewalIntPayable > 0) {
                                            Tot_Int_bal += renewalIntPayable;//total interest
                                        }
                                        interestRow.add(actNum);
                                        interestRow.add(acctDtlMap.get("CNAME"));
                                        interestRow.add(acctDtlMap.get("DEPOSIT_AMT"));
                                        interestRow.add(DateUtil.getStringDate((Date) acctDtlMap.get("DEPOSIT_DT")));
                                        interestRow.add(DateUtil.getStringDate((Date) acctDtlMap.get("MATURITY_DT")));
                                        if (renewalIntPayable > 0) {
                                            HashMap lasteIntMap = new HashMap();
                                            lasteIntMap.put("DEPOSIT_NO", actNum);
                                            ArrayList reIntrstDateList = (ArrayList) sqlMap.executeQueryForList("getLastInterestDate", lasteIntMap);
                                            if (reIntrstDateList.size() > 0 && reIntrstDateList != null) {
                                                HashMap resultMap = new HashMap();
                                                resultMap = (HashMap) reIntrstDateList.get(0);
                                                interestRow.add(DateUtil.getStringDate((Date) resultMap.get("LAST_INT_APPL_DT")));
                                            }
                                        } else {
                                            //interestRow.add(DateUtil.getStringDate((Date) paramMap.get("START")));
                                            interestRow.add(DateUtil.getStringDate((Date) paramMap.get("LAST_INT_DATE_DIS")));
                                           
                                        }
                                        interestRow.add(DateUtil.getStringDate((Date) paramMap.get("END")));
                                        
                                        interestRow.add(new Double(Tot_Int_bal));                                        
                                        interestRow.add(acctDtlMap.get("INT_PAY_ACC_NO"));
                                        interestRow.add("N");
                                        //babu added for colour change option 06/02/2015
                                        String colourStatus = CommonUtil.convertObjToStr(((HashMap) accountList.get(acc)).get("COLOUR_STATUS"));
                                        if (colourStatus != null && colourStatus.equals("M")) {
                                            interestRow.add("MATURED");
                                        } else if (colourStatus != null && colourStatus.equals("ML") || colourStatus.equals("L")) {
                                            HashMap whereLienMap = new HashMap();
                                            whereLienMap.put("DEPOSIT_NO", actNum);
                                            List LienmdsList = sqlMap.executeQueryForList("getDepositLien", whereLienMap);
                                            if (LienmdsList != null && LienmdsList.size() > 0) {
                                                hmap = (HashMap) LienmdsList.get(0);
                                                if (hmap.containsKey("REMARKS") && hmap.get("REMARKS") != null) {
                                                    if (CommonUtil.convertObjToStr(hmap.get("REMARKS")).equalsIgnoreCase("Lien From MDS")) {
                                                        if (colourStatus != null && colourStatus.equals("ML")) {
                                                            interestRow.add("MATURED MDS LIEN");
                                                        } else if (colourStatus != null && colourStatus.equals("L")) {
                                                            interestRow.add("MDS LIEN");
                                                        }
                                                    } else {
                                                        if (colourStatus != null && colourStatus.equals("ML")) {
                                                            interestRow.add("MATURED LIEN");
                                                        } else if (colourStatus != null && colourStatus.equals("L")) {
                                                            interestRow.add("LIEN");
                                                        }
                                                    }
                                                } else {
                                                    if (colourStatus != null && colourStatus.equals("ML")) {
                                                        interestRow.add("MATURED LIEN");
                                                    } else if (colourStatus != null && colourStatus.equals("L")) {
                                                        interestRow.add("LIEN"); } }} 
                                                   else {
                                                if (colourStatus != null && colourStatus.equals("ML")) {
                                                    interestRow.add("MATURED LIEN");
                                                } else if (colourStatus != null && colourStatus.equals("L")) {
                                                    interestRow.add("LIEN");}}
                                        } else {
                                            interestRow.add("NORMAL");
                                        }
                                        //added by rishad for validating 08/09/2016 
                                        boolean isCorrect = false;
                                        HashMap whereCondMap = new HashMap();
                                        whereCondMap.put("ACT_NUM", actNum);
                                        whereCondMap.put("INTEREST", Tot_Int_bal);
                                        whereCondMap.put("RENEWAl_INT", renewalIntPayable);
                                        whereCondMap.put("DEPOSIT_DT",DateUtil.getStringDate((Date) acctDtlMap.get("DEPOSIT_DT")));
                                        whereCondMap.put("END_DT",DateUtil.getStringDate((Date) paramMap.get("END")));
                                       // whereCondMap.put("FROM_DT",DateUtil.getStringDate((Date) paramMap.get("END")));
                                        isCorrect = validate(whereCondMap);
                                        if (isCorrect) {
                                            interestRow.add(CommonUtil.convertObjToStr("Error"));
                                        } else {
                                            interestRow.add(CommonUtil.convertObjToStr("OK"));
                                        }
                                        //     interestRow.add(CommonUtil.convertObjToStr(intPayFreq));
                                        interestRow.add(new Double(tdsDeductedForBaseAccount)); // Added by nithya on 06-02-2020 for KD-1090
                                        //end
                                        hmap.put("DEPOSIT_NO", actNum);
                                        hmap.put("LOAN_NO", CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_ACC_NO")));
                                        List LienList = new ArrayList();
                                        List LTDList = new ArrayList();
                                        if (colourStatus != null && colourStatus.equals("ML") || colourStatus.equals("L")) {
                                            LienList = sqlMap.executeQueryForList("getLienAccNo", hmap);
                                            LTDList = sqlMap.executeQueryForList("getLTDAccountDetailsForInterestPaying", hmap);
                                            if (LTDList != null & LTDList.size() > 0) {
                                                hmap = (HashMap) LTDList.get(0);
                                                String behave = CommonUtil.convertObjToStr(hmap.get("BEHAVES_LIKE"));
                                                double totLtdamt = CommonUtil.convertObjToDouble(hmap.get("TOTAL_BALANCE")).doubleValue();

                                                String acno = CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_ACC_NO"));
                                                String pid = CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_PROD_ID"));
                                                String pname = CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_PROD_TYPE"));
                                                //
                                                if (pname.equals("TL")) {
                                                    HashMap loanMap = calcLoanPayments(acno, pid, pname);
                                                    hshMap = new HashMap();
                                                    totLtdamt = CommonUtil.convertObjToDouble(loanMap.get("TOTAL_DEMAND")).doubleValue();
                                                    if (totLtdamt >= intTrfAmt) {
                                                        hshMap = new HashMap();
                                                        hshMap.put("ACCT_TYPE", "LOANS_AGAINST_DEPOSITS");
                                                        hshMap.put("AMOUNT", new Double(totLtdamt));
                                                        hshMap.put("ACT_NUM", actNum);
                                                        if (LienList.size() > 0 && LienList != null) {
                                                            hshMap.put("CHANGE_COLOR", "TRUE");
                                                        } else {
                                                            hshMap.put("CHANGE_COLOR", "");
                                                        }
                                                         if (limitAmount < Tot_Int_bal) {
                                                        rdList.add(hshMap);
                                                        setAccountsList(rdList);
                                                        interestList.add(interestRow);}
                                                    }
                                                } else {
                                                    hshMap = new HashMap();
                                                    hshMap.put("ACCT_TYPE", "");
                                                    hshMap.put("AMOUNT", "");
                                                    hshMap.put("ACT_NUM", actNum);
                                                    if (LienList.size() > 0 && LienList != null) {
                                                        hshMap.put("CHANGE_COLOR", "TRUE");
                                                    } else {
                                                        hshMap.put("CHANGE_COLOR", "");
                                                    }
                                         if (limitAmount < Tot_Int_bal) {
                                                    rdList.add(hshMap);
                                                    setAccountsList(rdList);
                                                    
                                                    interestList.add(interestRow);}
                                                }
                                            }
                                            
                                        }
                                        List RdList = sqlMap.executeQueryForList("getRDAccountDetails", hmap);
                                        if (RdList != null && RdList.size() > 0) {
                                            hmap = (HashMap) RdList.get(0);
                                            String recurring = "RECURRING";
                                            String behave = CommonUtil.convertObjToStr(hmap.get("BEHAVES_LIKE"));
                                            String amount = CommonUtil.convertObjToStr(hmap.get("DEPOSIT_AMT"));
                                            Date mdate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hmap.get("MATURITY_DT")));
                                            double actamount = intTrfAmt;
                                            double rdamount = CommonUtil.convertObjToDouble(amount).doubleValue();
                                            Date RDMatDt = (Date) currentDate.clone();
                                            RDMatDt.setDate(mdate.getDate());
                                            RDMatDt.setMonth(mdate.getMonth());
                                            RDMatDt.setYear(mdate.getYear());
                                            if (behave.equals(recurring)) {
                                                if (postAmount > 0.0) {
                                                    actamount = actamount - postAmount;
                                                } else if (renewpostAmt > 0.0) {
                                                    actamount = actamount - renewpostAmt;
                                                }
                                                if (DateUtil.dateDiff(currentDate, RDMatDt) > 0 && actamount >= rdamount) {
                                                    hshMap = new HashMap();
                                                    hshMap.put("ACCT_TYPE", "RECURRING");
                                                    hshMap.put("AMOUNT", amount);
                                                    hshMap.put("ACT_NUM", actNum);
                                                    if (LienList.size() > 0 && LienList != null) {
                                                        hshMap.put("CHANGE_COLOR", "TRUE");
                                                    } else {
                                                        hshMap.put("CHANGE_COLOR", "");
                                                    }
                                    if (limitAmount < Tot_Int_bal) {
                                                    interestList.add(interestRow);
                                                    rdList.add(hshMap);
                                                    setAccountsList(rdList);}
                                                }
                                            } else {
                                                hshMap = new HashMap();
                                                hshMap.put("ACCT_TYPE", "");
                                                hshMap.put("AMOUNT", "");
                                                hshMap.put("ACT_NUM", actNum);
                                                if (LienList.size() > 0 && LienList != null) {
                                                    hshMap.put("CHANGE_COLOR", "TRUE");
                                                } else {
                                                    hshMap.put("CHANGE_COLOR", "");
                                                }
                                              if (limitAmount < Tot_Int_bal) {
                                                rdList.add(hshMap);
                                                setAccountsList(rdList);
                                                interestList.add(interestRow);}

                                            }
                                        }
                                        if (LTDList == null & LTDList.size() > 0 && RdList != null && RdList.size() > 0) {
                                        } else {
                                            String acno = CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_ACC_NO"));
                                            String pid = CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_PROD_ID"));
                                            String pname = CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_PROD_TYPE"));
                                            if (pname.equals(mds)) {
                                                HashMap mdsMap = calcMDS(acno, pid, pname);
                                                double totalDemand = 0.0;
                                                String demand = "";
                                                String principal = "";
                                                String penal = "";
                                                String bonus = "";
                                                String discount = "";
                                                String arbitration = "";
                                                String charges = "";
                                                String interest = "";
                                                String notice = "";
                                                if (mdsMap != null) {
                                                    totalDemand = CommonUtil.convertObjToDouble(mdsMap.get("TOTAL_DEMAND")).doubleValue();
                                                    demand = CommonUtil.convertObjToStr(mdsMap.get("TOTAL_DEMAND"));
                                                    principal = CommonUtil.convertObjToStr(mdsMap.get("PRINCIPAL"));
                                                    penal = CommonUtil.convertObjToStr(mdsMap.get("PENAL"));
                                                    bonus = CommonUtil.convertObjToStr(mdsMap.get("BONUS"));
                                                    discount = CommonUtil.convertObjToStr(mdsMap.get("DISCOUNT"));
                                                    charges = CommonUtil.convertObjToStr(mdsMap.get("CHARGES"));
                                                    interest = CommonUtil.convertObjToStr(mdsMap.get("INTEREST"));
                                                    arbitration = CommonUtil.convertObjToStr(mdsMap.get("ARBITRATION"));
                                                    notice = CommonUtil.convertObjToStr(mdsMap.get("NOTICE_AMT"));
                                                }
                                                //                                            acno = acno.lastIndexOf("_")!=-1 ? acno.substring(0,acno.length()-2) : acno;
                                                HashMap mdsHashMap = new HashMap();
                                                mdsHashMap.put("CHITTALNO", acno);
                                                mdsHashMap = (HashMap) sqlMap.executeQueryForList("getMDSAccountDetailsForInterestPaying", mdsHashMap).get(0);
                                                Date chitEndDt = setProperDtFormat(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(mdsHashMap.get("CHIT_END_DT"))));
                                                if (postAmount > 0.0) {
                                                    intTrfAmt = intTrfAmt - postAmount;
                                                } else if (renewpostAmt > 0.0) {
                                                    intTrfAmt = intTrfAmt - renewpostAmt;
                                                }
                                                if (totalDemand > 0) {
                                                    if (intTrfAmt >= totalDemand && DateUtil.dateDiff(currentDate, chitEndDt) > 0) {
                                                        hshMap = new HashMap();
                                                        hshMap.put("ACCT_TYPE", "MDS");
                                                        hshMap.put("ACCT_NUM", CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_ACC_NO")));
                                                        hshMap.put("PRINCIPAL", principal);
                                                        hshMap.put("TOTAL_DEMAND", demand);
                                                        hshMap.put("PENAL", penal);
                                                        hshMap.put("BONUS", bonus);
                                                        hshMap.put("DISCOUNT", discount);
                                                        hshMap.put("CHARGES", charges);
                                                        hshMap.put("INTEREST", interest);
                                                        hshMap.put("ARBITRATION", arbitration);
                                                        hshMap.put("NOTICE_AMT", notice);

                                                        if (LienList.size() > 0 && LienList != null) {
                                                            hshMap.put("CHANGE_COLOR", "TRUE");
                                                        } else {
                                                            hshMap.put("CHANGE_COLOR", "");
                                                        }
                                                     if(limitAmount<Tot_Int_bal){
                                                        rdList.add(hshMap);
                                                        setAccountsList(rdList);
                                                        interestList.add(interestRow);}

                                                        mdsHashMap = null;
                                                    }
                                                }

                                            } else {
                                                hshMap = new HashMap();
                                                hshMap.put("ACCT_TYPE", "");
                                                hshMap.put("AMOUNT", "");
                                                hshMap.put("ACT_NUM", actNum);
                                                if (LienList.size() > 0 && LienList != null) {
                                                    hshMap.put("CHANGE_COLOR", "TRUE");
                                                } else {
                                                    hshMap.put("CHANGE_COLOR", "");
                                                }
                                                if (limitAmount < Tot_Int_bal) {
                                                    rdList.add(hshMap);
                                                    setAccountsList(rdList);
                                                    interestList.add(interestRow);
                                                }
                                            }

                                        }
                                        Tot_Int_bal = 0;
                                    }
                                }
                            }
                        } catch (Exception e) {
                            if (isTransaction) {
                                sqlMap.rollbackTransaction();
                                if (errorMap == null) {
                                    errorMap = new HashMap();
                                }
                                String actNum = CommonUtil.convertObjToStr(paramMap.get("ACT_NUM"));
                                actNum = actNum.lastIndexOf("_") != -1 ? actNum.substring(0, actNum.length() - 2) : actNum;
                                errorMap.put(actNum, new TTException(e));
                                if (interestList.size() >= 1) {
                                    interestList.set(0, errorMap);
                                } else {
                                    interestList.add(0, errorMap);
                                }
                            }
                            e.printStackTrace();
                        }
                    }
                    if (fFlag != null && fFlag.equals("SAVE")) {
                        if (acctDtlMap != null && acctDtlMap.get("INTPAY_MODE").equals("TRANSFER")) {
                        } else {
                            if (cashListInt!=null && cashListInt.size()>0) {
                            cashTransMapInt.put("DAILYDEPOSITTRANSTO", cashListInt);
                            cashTransMapInt.put(CommonConstants.BRANCH_ID, branch);
                            HashMap cashMap = cashTransactionDAO.execute(cashTransMapInt, false);
                            paramMap.put(paramMap.get("ACT_NUM"), cashMap.get(CommonConstants.TRANS_ID));
                            //added by rishad for rollback pupose 10/07/2015 mantis :0010745
                            HashMap whereMap=new HashMap();
                            whereMap.put("trans_id",CommonUtil.convertObjToStr(cashMap.get(CommonConstants.TRANS_ID)));
                            whereMap.put("vanishKey","CASHIER_AUTH");
                            whereMap.put("trnDt",currentDate);
                            sqlMap.executeUpdate("updateDepositInterest", whereMap);
                            //end
                            interestList.add(cashMap.get(CommonConstants.TRANS_ID));
                            cashTransactionDAO = null;
                            cashListInt.clear();}
                        }
                    }
                    //CALENDER_FREQ='Y'   => First Time only Calling this Method Based on "Deposit_Dt"
                    if (!isTransaction || (paramMap.containsKey("CAL_FREQ_ACCOUNT_LIST") && isTransaction)) {
                        //Added by nithya on 08-05-2020 for KD-1535 
                        if (paramMap.containsKey("SPECIAL_RD_SCHEME") && paramMap.get("SPECIAL_RD_SCHEME") != null && CommonUtil.convertObjToStr(paramMap.get("SPECIAL_RD_SCHEME")).equalsIgnoreCase("SPECIAL_RD_SCHEME")) {
                            nonBatch.put("SPECIAL_RD_SCHEME", "SPECIAL_RD_SCHEME");
                        }
                        if (paramMap.containsKey("RD_INT_APPLICATION") && paramMap.get("RD_INT_APPLICATION") != null && CommonUtil.convertObjToStr(paramMap.get("RD_INT_APPLICATION")).equalsIgnoreCase("RD_INT_APPLICATION")) {
                            nonBatch.put("RD_INT_APPLICATION", "RD_INT_APPLICATION");
                        }
                        //End
//                        calenderFreqIntCalcForTable(nonBatch, interestList);
                    }
                }
            }
            paramMap = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return interestList;
    }
    //added by rishad at 08/09/2016 for validating deposit account at the time of interest posting

    private boolean validate(HashMap condMap) {
        boolean isExist = false;
        try {

            List actualData = sqlMap.executeQueryForList("getDepositDataForValidate", condMap);
            if (actualData != null && actualData.size() > 0) {
                HashMap resultMap = (HashMap) actualData.get(0);
                 double totIntAmt=CommonUtil.convertObjToDouble(resultMap.get("TOT_INT_AMT"));
                 double renewalint=CommonUtil.convertObjToDouble(condMap.get("RENEWAl_INT"));
                 double calcint=CommonUtil.convertObjToDouble(condMap.get("INTEREST"));
                 double termInt=calcint-renewalint;
                 double totIntCredit=CommonUtil.convertObjToDouble(resultMap.get("TOTAL_INT_CREDIT"));
                  double totIntDrawn=CommonUtil.convertObjToDouble(resultMap.get("TOTAL_INT_DRAWN"));
                 double totCurrInt=totIntCredit+termInt;
               Date depo_dt=DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(resultMap.get("DEPOSIT_DT")));
               Date resultDepoDt=DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(condMap.get("DEPOSIT_DT")));
               Date endDt=DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(condMap.get("END_DT")));
            //    Date fromDt=DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(resultMap.get("FROM_DT")));
                if (depo_dt.compareTo(resultDepoDt) != 0) {
                    isExist = true;
                    return isExist;
                } else if (totCurrInt > totIntAmt) {
                    isExist = true;
                    return isExist;
                } else if (totIntCredit != totIntDrawn) {
                    isExist = true;
                    return isExist;
                }
                else if (depo_dt.compareTo(endDt) == 1) {
                    isExist = true;
                    return isExist;
                }
               
            } else {
                isExist = true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(DepositIntTask.class.getName()).log(Level.SEVERE, null, ex);
        }
        return isExist;
    }
    public List getInterestTableData(String fFlag) {
        List interestList = new ArrayList();
        List interestRow = new ArrayList();
        ArrayList rdList = new ArrayList();
        HashMap errorMap = null;
        String depInt="N";
        try {	
           generateSingleTransId = generateLinkID();
            HashMap nonBatch = new HashMap();

            //__ get ProductData...
            if (paramMap.containsKey(CommonConstants.PRODUCT_ID)) {
                nonBatch.put(InterestTaskRunner.PRODUCT_ID, paramMap.get(CommonConstants.PRODUCT_ID));
            }
            //__ getAccount Data
            if (paramMap.containsKey(CommonConstants.ACT_NUM)) {
                nonBatch.put(CommonConstants.ACT_NUM, paramMap.get(CommonConstants.ACT_NUM));
            }

            nonBatch.put("BEHAVES_LIKE", "FIXED");
            nonBatch.put("APPLICATION", "APPLICATION");
            InterestBatchTO objInterestBatchTO = null;
            ArrayList productList = (ArrayList) sqlMap.executeQueryForList("Deposit.getProducts", nonBatch);
            //                ArrayList productList = getProductList(nonBatch);
             HashMap dataMap=new HashMap();
                    dataMap.put("PROD_ID",paramMap.get(CommonConstants.PRODUCT_ID));
                    List periodList=sqlMap.executeQueryForList("getIntRoundAtIntApplication", dataMap);
                    if(periodList!=null && periodList.size()>0){
                        HashMap periodMap=(HashMap)periodList.get(0);
                        if(periodMap.containsKey("INT_ROUND_AT_INTAPPL")){
                            depInt="Y";
                        }
                    }
            if (productList != null && productList.size() > 0) {
                int prodListSize = productList.size();
                for (int prod = 0; prod < prodListSize; prod++) {
                    paramMap.put(InterestTaskRunner.AC_HD_ID, ((HashMap) productList.get(prod)).get("ACCT_HEAD")); //Ac Head

                    paramMap.put(InterestTaskRunner.DEBIT_INT, ((HashMap) productList.get(prod)).get("INT_PROV_ACHD")); //Debit Int Head
                    paramMap.put(InterestTaskRunner.CREDIT_INT, ((HashMap) productList.get(prod)).get("INT_PAY")); //Credit Int Head
                    paramMap.put(InterestTaskRunner.PRODUCT_ID, ((HashMap) productList.get(prod)).get("PROD_ID")); //Product Id
                    paramMap.put("ROUND_OFF", ((HashMap) productList.get(prod)).get("INT_ROUNDOFF_TERMS")); //Product Id
                    paramMap.put(InterestTaskRunner.LAST_APPL_DT, ((HashMap) productList.get(prod)).get("LAST_INT_APPL_DT")); //Last Interest Applied Dt Payale

                    paramMap.put("interestType", ((HashMap) productList.get(prod)).get("INT_TYPE"));

                    paramMap.put(InterestTaskRunner.APPL_NEW_DT, null); //Updated date based on FREQ_A from InterestTaskRunner
                    String postageAcHd = CommonUtil.convertObjToStr(((HashMap) productList.get(prod)).get("POSTAGE_ACHD"));

                    nonBatch.put(InterestTaskRunner.PRODUCT_ID, ((HashMap) productList.get(prod)).get("PROD_ID")); //Product Id)
                    paramMap.put("BATCH_PROCESS", "BATCH_PROCESS");
                    paramMap.put("PROD_TYPE", "TD");
                    paramMap.put("TASK", "APPLICATION");
                    paramMap.put("DISCOUNTED_RATE", ((HashMap) productList.get(prod)).get("DISCOUNTED_RATE"));
                    List recList = null;
                    List accountList = null;
                    actBranch = CommonUtil.convertObjToStr(paramMap.get(CommonConstants.BRANCH_ID));
                    currentDate = ServerUtil.getCurrentDate(actBranch);
                    paramMap.put(CommonConstants.BRANCH_ID, actBranch);
                    nonBatch.put("PARAMMAP", paramMap);
                    if (paramMap.containsKey("CUST_ID")) {
                        nonBatch.put("CUST_ID", paramMap.get("CUST_ID"));
                    }
                    if (paramMap.containsKey("INTPAY_MODE")) {
                        nonBatch.put("INTPAY_MODE", paramMap.get("INTPAY_MODE"));
                    }
                   
                    if (paramMap.containsKey("ACCOUNT_LIST")) {
                        accountList = (ArrayList) paramMap.get("ACCOUNT_LIST");
                    } else {
                        accountList = getAccountList(nonBatch);
                    }
                      
                    int accountListSize = accountList.size();
                      //System.out.println("################# accountListSize : " + accountListSize);
                    HashMap acctDtlMap = new HashMap();
                    for (int acc = 0; acc < accountListSize; acc++) {
                        try {
                            paramMap.put("BEHAVES", ((HashMap) accountList.get(acc)).get("ACCT_TYPE"));
                            paramMap.put("ACT_NUM", ((HashMap) accountList.get(acc)).get("ACT_NUM"));
                            paramMap.put("CUST_ID", ((HashMap) accountList.get(acc)).get("CUST_ID"));
                            paramMap.put("AMT", ((HashMap) accountList.get(acc)).get("AMOUNT"));
                            paramMap.put("INTEREST_AMOUNT", ((HashMap) accountList.get(acc)).get("INTEREST_AMOUNT"));
                            paramMap.put("INT_CALC_UPTO_DT", ((HashMap) accountList.get(acc)).get("INT_CALC_UPTO_DT"));
                            paramMap.put("PROD_TYPE", "TD");
                            paramMap.put(CommonConstants.BRANCH_ID, actBranch);
                            paramMap.put("CUR_DT", currentDate);
                            double interestAmt = 0.0;
                            double intTrfAmt = 0.0;
                            double Tot_Int_AMT = 0.0;
                            double postageAmt = 0.0;
                            double postageRenewAmt = 0.0;
                            paramMap.put("CURR_DATE", currentDate);
                   //         System.out.println("00001");
                            List acctDtl = sqlMap.executeQueryForList("getDepoitDetailForIntApp", paramMap);
                            List postageList = sqlMap.executeQueryForList("getPostageAmount", paramMap);
                            List renewPostageList = sqlMap.executeQueryForList("getRenewPostageAmount", paramMap);
                            if (postageList != null && postageList.size() > 0) {
                                HashMap hamp = (HashMap) postageList.get(0);
                                postageAmt = CommonUtil.convertObjToDouble(hamp.get("POSTAGE_AMT")).doubleValue();

                            }
                            if (renewPostageList != null && renewPostageList.size() > 0) {
                                HashMap hamp = (HashMap) renewPostageList.get(0);
                                postageRenewAmt = CommonUtil.convertObjToDouble(hamp.get("RENEW_POSTAGE_AMT")).doubleValue();
                            }

                            if (acctDtl != null && acctDtl.size() > 0) {
                                String behaves = CommonUtil.convertObjToStr(paramMap.get("BEHAVES"));
                                String rec = "RECURRING";
                                String mds = "MDS";
                                double Tot_Int_Cr = 0.0;
                                double Tot_Int_Dr = 0.0;

                                acctDtlMap = (HashMap) acctDtl.get(0);
                                paramMap.put("TODAY_DT", currentDate);
                                Date lstproDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(acctDtlMap.get("LAST_PROV_DT")));
                                Date lstIntCrDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(acctDtlMap.get("LAST_INT_APPL_DT")));
                               // System.out.println("rish   ###########"+lstIntCrDt);
                                Date depDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(acctDtlMap.get("DEPOSIT_DT")));
                                Date depMatDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(acctDtlMap.get("MATURITY_DT")));
                                Date nextCalsDt = DateUtil.addDays(lstIntCrDt, CommonUtil.convertObjToInt(acctDtlMap.get("FREQ")));
                             
                               // System.out.println("ris#################"+nextCalsDt);
                                //Added by chithra for  calculating  full Interest of  Matured deposits 
                                if(DateUtil.dateDiff(depMatDt, currentDate) >= 0){
                                   acctDtlMap.put("FREQ","0") ;
                                }
                                
                                Tot_Int_AMT = CommonUtil.convertObjToDouble(acctDtlMap.get("TOT_INT_AMT")).doubleValue();
                                Tot_Int_Cr = CommonUtil.convertObjToDouble(acctDtlMap.get("TOTAL_INT_CREDIT")).doubleValue();
                                Tot_Int_Dr = CommonUtil.convertObjToDouble(acctDtlMap.get("TOTAL_INT_DRAWN")).doubleValue();
                                long period = DateUtil.dateDiff(depDt, depMatDt);
                             //   nextCalsDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(acctDtlMap.get("NEXT_INT_APPL_DT")));
                                //Added by chithra for  calculating  full Interest of  Matured deposits 
                                if(DateUtil.dateDiff(depMatDt, currentDate) >= 0){
                                   nextCalsDt=currentDate;  
                                }
                                
                                paramMap.put("DEPOSIT_DT", depDt);
                                paramMap.put("MATURITY_DT", depMatDt);
                                paramMap.put("AMOUNT", acctDtlMap.get("DEPOSIT_AMT"));
                                paramMap.put("CATEGORY_ID", acctDtlMap.get("CATEGORY"));
                                paramMap.put("BEHAVES_LIKE", "FIXED");
                                paramMap.put("PERIOD1", new Double(period));
                                paramMap.put("END", nextCalsDt);
                                paramMap.put("FREQ", acctDtlMap.get("FREQ"));
                                if (DateUtil.dateDiff(lstIntCrDt, lstproDt) >= 0) {
                                    paramMap.put("START", lstproDt);
                                } else {
                                    paramMap.put("START", lstIntCrDt);
                                }
                                if (DateUtil.dateDiff(nextCalsDt, depMatDt) > 0) {
                                    paramMap.put("END", nextCalsDt);
                                } else {
                                    paramMap.put("END", depMatDt);
                                }
                                int fre = CommonUtil.convertObjToInt(paramMap.get("INT_PROV_FREQ"));
                                Date diffDt = DateUtil.addDays(lstproDt, -fre);
                                //Added By Suresh
                                if (!isTransaction && Tot_Int_Cr<Tot_Int_AMT) {
                                       objInterestBatchTO = new InterestBatchTO();
                                    objInterestBatchTO.setIntRate(CommonUtil.convertObjToDouble(acctDtlMap.get("RATE_OF_INT")).doubleValue());
                                    objInterestBatchTO.setIntType("SIMPLE");
                                    objInterestBatchTO.setPrincipleAmt(CommonUtil.convertObjToDouble(acctDtlMap.get("DEPOSIT_AMT")));
                                    objInterestBatchTO.setProductType(CommonUtil.convertObjToStr("TD"));
                                    objInterestBatchTO.setLastTdsApplDt(currentDate);
                                    objInterestBatchTO.setTrnDt(currentDate);
                                    objInterestBatchTO.setDrCr("CREDIT");
                                    objInterestBatchTO.setBranch_code(CommonUtil.convertObjToStr(paramMap.get("BRANCH_CODE")));
                                    objInterestBatchTO.setUser_id(CommonUtil.convertObjToStr(paramMap.get("USER_ID")));
                                    objInterestBatchTO.setAcHdId(CommonUtil.convertObjToStr(paramMap.get("AC_HD_ID")));
                                    objInterestBatchTO.setActNum(CommonUtil.convertObjToStr(acctDtlMap.get("DEPOSIT_NO")));
                                    objInterestBatchTO.setIntDt(getProperFormatDate(lstIntCrDt));
                                    objInterestBatchTO.setApplDt(getProperFormatDate(nextCalsDt));
                           //         System.out.println("######################ris"+objInterestBatchTO.getIntAmt());
                                    interestApplictionDeleteInsert(objInterestBatchTO, true);
                                    if (acctDtlMap.containsKey("FREQ") && CommonUtil.convertObjToInt(acctDtlMap.get("FREQ")) == 30 && CommonUtil.convertObjToStr(paramMap.get("DISCOUNTED_RATE")).equals("Y")) {
                               //         System.out.println("rish................00007");
                                        double period1 = 0.0;
                                        long pr = DateUtil.dateDiff(lstIntCrDt, nextCalsDt);
                                        double roi = 0.0;
                                        double amount = CommonUtil.convertObjToDouble(acctDtlMap.get("DEPOSIT_AMT")).doubleValue();
                                        roi = CommonUtil.convertObjToDouble(acctDtlMap.get("RATE_OF_INT")).doubleValue();
                                        if (DateUtil.dateDiff(lstproDt, lstIntCrDt) > 0 && DateUtil.dateDiff(nextCalsDt, depMatDt) >= 0) {
                                            double interestAmt1 = 0.0;
                                            double interestAmt2 = 0.0;
                                            for (lstIntCrDt = DateUtil.addDays(lstIntCrDt, 30); DateUtil.dateDiff(lstIntCrDt, currentDate) >= 0; lstIntCrDt = DateUtil.addDays(lstIntCrDt, 30)) {
                                                if (DateUtil.dateDiff(nextCalsDt, depMatDt) >= 0) {
                                                    period1 = (double) pr / 30;
                                                    period1 = (double) getNearest((long) (period1 * 100), 100) / 100;
                                                    interestAmt2 = roi / 4 / (Math.pow((1 + (roi / 1200)), 2) + (1 + (roi / 1200)) + 1);
                                                    interestAmt2 = interestAmt2 * period1;
                                                    double calcAmt = amount / 100;
                                                    interestAmt2 = interestAmt2 * calcAmt;
                                                    interestAmt2 = (double) getNearest((long) (interestAmt2 * 100), 100) / 100;
                                                    interestAmt1 = interestAmt1 + interestAmt2;
                                                    nextCalsDt = lstIntCrDt;

                                                }
                                            }
                                            interestAmt = interestAmt1;
                                            intTrfAmt = interestAmt;
                                            lstIntCrDt = DateUtil.addDays(lstIntCrDt, -30);
                                            if (Tot_Int_AMT > 0 && DateUtil.dateDiff(depMatDt, nextCalsDt) >= 0) {
                                                interestAmt = Tot_Int_AMT - Tot_Int_Cr;
                                                intTrfAmt = interestAmt;
                                                nextCalsDt = depMatDt;
                                            }
                                            paramMap.put("END", nextCalsDt);
                                        } else if (DateUtil.dateDiff(lstproDt, lstIntCrDt) <= 0 && DateUtil.dateDiff(nextCalsDt, depMatDt) >= 0) {
                                           
                                            double DiffAmt = 0.0;
                                            double calcAmt = 0.0;
                                            paramMap.put("START", lstIntCrDt);
                                            if (DateUtil.dateDiff(lstIntCrDt, depDt) != 0 || DateUtil.dateDiff(lstproDt, depDt) != 0) { // for the prpose of renwal after provison reneved
                                                pr = DateUtil.dateDiff(lstIntCrDt, lstproDt);
                                                if (DateUtil.dateDiff(lstIntCrDt, depDt) >= 0) {

                                                }
                                                pr = pr + 1;
                                                period1 = (double) pr;
                                                period1 = (double) period1 / 30;
                                               
                                                interestAmt = roi / 4 / (Math.pow((1 + (roi / 1200)), 2) + (1 + (roi / 1200)) + 1);
                    
                                                interestAmt = interestAmt * period1;
                                                calcAmt = amount / 100;
                                                DiffAmt = interestAmt * calcAmt;
                                               
                                                DiffAmt = (double) getNearest((long) (DiffAmt * 100), 100) / 100;
                                                paramMap.put("START", lstproDt);
                                            }
                                            pr = DateUtil.dateDiff(lstIntCrDt, nextCalsDt);
                                            period1 = (double) pr / 30;
                                            period1 = (double) getNearest((long) (period1 * 100), 100) / 100;
                                            interestAmt = roi / 4 / (Math.pow((1 + (roi / 1200)), 2) + (1 + (roi / 1200)) + 1);

                                            interestAmt = interestAmt * period1;
                                            calcAmt = amount / 100;
                                            interestAmt = interestAmt * calcAmt;
                                        
                                            interestAmt = (double) getNearest((long) (interestAmt * 100), 100) / 100;
                                            intTrfAmt = interestAmt;
                                            interestAmt = interestAmt - DiffAmt;

                                            if (Tot_Int_AMT > 0 && DateUtil.dateDiff(depMatDt, nextCalsDt) >= 0) {
                                                interestAmt = Tot_Int_AMT - Tot_Int_Cr;
                                                intTrfAmt = interestAmt + DiffAmt;
                                                nextCalsDt = depMatDt;
                                            }
                                            paramMap.put("START", lstproDt);

                                            paramMap.put("END", nextCalsDt);

                                            double interestAmt1 = 0.0;
                                            double interestAmt2 = 0.0;
                                            Date lstIntCrDt1 = DateUtil.addDays(lstIntCrDt, 30);
                                            if (DateUtil.dateDiff(lstIntCrDt1, currentDate) >= 0) {
                                                lstIntCrDt = lstIntCrDt1;
                                                for (lstIntCrDt = DateUtil.addDays(lstIntCrDt, 30); DateUtil.dateDiff(lstIntCrDt, currentDate) >= 0; lstIntCrDt = DateUtil.addDays(lstIntCrDt, 30)) {
                                                    if (DateUtil.dateDiff(nextCalsDt, depMatDt) >= 0) {
                                                        period1 = (double) pr / 30;
                                                        period1 = (double) getNearest((long) (period1 * 100), 100) / 100;
                                                        interestAmt2 = roi / 4 / (Math.pow((1 + (roi / 1200)), 2) + (1 + (roi / 1200)) + 1);
   
                                                        interestAmt2 = interestAmt2 * period1;
                                                        calcAmt = 0.0;
                                                        calcAmt = amount / 100;
                                                        interestAmt2 = interestAmt2 * calcAmt;
                                                        interestAmt2 = (double) getNearest((long) (interestAmt2 * 100), 100) / 100;
                                                        interestAmt1 = interestAmt1 + interestAmt2;
                                                     
                                                        nextCalsDt = lstIntCrDt;

                                                    }
                                                }
                                              
                                                interestAmt = interestAmt + interestAmt1;
                                                intTrfAmt = interestAmt1 + intTrfAmt;
                                                lstIntCrDt = DateUtil.addDays(lstIntCrDt, -30);

                                                paramMap.put("END", nextCalsDt);
                                               
                                                if (Tot_Int_AMT > 0 && DateUtil.dateDiff(depMatDt, nextCalsDt) >= 0) {
                                                    interestAmt = Tot_Int_AMT - Tot_Int_Cr;
                                                    intTrfAmt = interestAmt + DiffAmt;
                                                }
                                            }
                                        } else {
                                            period1 = DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("START"))), DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("END"))));
                                            period1 = (double) pr / 30;
                                            
                                            interestAmt = roi / 4 / (Math.pow((1 + (roi / 1200)), 2) + (1 + (roi / 1200)) + 1);

                                            interestAmt = interestAmt * period1;
                                            double calcAmt = amount / 100;
                                            interestAmt = interestAmt * calcAmt;
                                            intTrfAmt = interestAmt;
                                        }

                                        interestAmt = (double) getNearest((long) (interestAmt * 100), 100) / 100;
                                        objInterestBatchTO = new InterestBatchTO();
                                        objInterestBatchTO.setActNum(CommonUtil.convertObjToStr(paramMap.get("ACT_NUM")));
                                        objInterestBatchTO.setAcHdId(CommonUtil.convertObjToStr(paramMap.get("CREDIT_INT")));
                                        objInterestBatchTO.setIntAmt(new Double(interestAmt));
                                        objInterestBatchTO.setIntRate(new Double(roi));
                                        objInterestBatchTO.setIntType("SIMPLE");
                                        objInterestBatchTO.setPrincipleAmt(CommonUtil.convertObjToDouble(acctDtlMap.get("DEPOSIT_AMT")));
                                        objInterestBatchTO.setIsTdsApplied("N");
                                        Date applnDt = (Date) currentDate.clone();
                                        Date applDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("END")));
                                        if (applDt.getDate() > 0) {
                                            applnDt.setDate(applDt.getDate());
                                            applnDt.setMonth(applDt.getMonth());
                                            applnDt.setYear(applDt.getYear());
                                        }
                                        objInterestBatchTO.setApplDt(applnDt);
                                    } else if (acctDtlMap.containsKey("FREQ") && ((CommonUtil.convertObjToInt(acctDtlMap.get("FREQ")) == 90)
                                            || (CommonUtil.convertObjToInt(acctDtlMap.get("FREQ")) == 180) || (CommonUtil.convertObjToInt(acctDtlMap.get("FREQ")) == 360) || ((CommonUtil.convertObjToStr(paramMap.get("DISCOUNTED_RATE")).equals("N")) && CommonUtil.convertObjToInt(acctDtlMap.get("FREQ")) != 0)
                                            || (CommonUtil.convertObjToStr(paramMap.get("DISCOUNTED_RATE")).equals("N") && CommonUtil.convertObjToInt(acctDtlMap.get("FREQ")) == 30))) {
                                 //         System.out.println("000000000000008");
                                       //         System.out.println("#################### Frequency is (90/180/360/) Or (Discount Rate is N and Freq=30)  ");
                                        int fr = CommonUtil.convertObjToInt(acctDtlMap.get("FREQ"));
               //rish0007
                                        double period1 = 0.0;
                                        long pr1 = DateUtil.dateDiff(lstIntCrDt, nextCalsDt);
                                        double pr = (double) pr1;
                                        double roi = 0.0;

                                        double calcAmt = CommonUtil.convertObjToDouble(acctDtlMap.get("DEPOSIT_AMT")).doubleValue();
                                        roi = CommonUtil.convertObjToDouble(acctDtlMap.get("RATE_OF_INT")).doubleValue();
                               //         System.out.println("reached here......");
                                //      System.out.println("rishhhhhhhhhhhh((((((((((((first"+DateUtil.dateDiff(lstproDt, lstIntCrDt));
                                  //        System.out.println("rishhhhhhhhhhhh((((((((((((second"+DateUtil.dateDiff(nextCalsDt, depMatDt));
                                          System.out.println("rish.......lstproDt"+lstproDt);
                                               System.out.println("rish.......lstIntCrDt"+lstIntCrDt);
                                        if (DateUtil.dateDiff(lstproDt, lstIntCrDt) > 0 && DateUtil.dateDiff(nextCalsDt, depMatDt) >= 0) {
                                            System.out.println("enetered here........");
                                            
                                            double interestAmt1 = 0.0;
                                            double interestAmt2 = 0.0;
                                            System.out.println("rishffffff123"+lstIntCrDt);
                                            System.out.println("###################DateUtil.addDays(lstIntCrDt, fr)"+DateUtil.addDays(lstIntCrDt, fr));
                                            for (lstIntCrDt = DateUtil.addDays(lstIntCrDt, fr); DateUtil.dateDiff(lstIntCrDt, currentDate) >= 0; lstIntCrDt = DateUtil.addDays(lstIntCrDt, fr)) {
                                                if (DateUtil.dateDiff(nextCalsDt, depMatDt) >= 0) {
                                                    interestAmt2 = (fr * roi * calcAmt) / (1200 * 30);
                                                    interestAmt2 = (double) getNearest((long) (interestAmt2 * 100), 100) / 100;
                                                    interestAmt1 = interestAmt1 + interestAmt2;
                                                objInterestBatchTO.setIntAmt(new Double(interestAmt2));
                                                objInterestBatchTO.setIntDt(getProperFormatDate(DateUtil.addDays(nextCalsDt, 1)));
                                                objInterestBatchTO.setApplDt(getProperFormatDate(lstIntCrDt));
                                                interestApplictionDeleteInsert(objInterestBatchTO, false);
                                                    nextCalsDt = lstIntCrDt;
                                                  //  nextCalsDt = lstIntCrDt;
                                                }
                                            }
                                            System.out.println("rish.......................dsdd"+interestAmt1);
                                            TdsCalc tds = new TdsCalc(actBranch);
                                            String CustId = CommonUtil.convertObjToStr(paramMap.get("CUST_ID"));
                                            String Prod_type = CommonUtil.convertObjToStr(paramMap.get("PROD_TYPE"));
                                            String prod_id = CommonUtil.convertObjToStr(paramMap.get(InterestTaskRunner.PRODUCT_ID));
                                            String accnum = CommonUtil.convertObjToStr(paramMap.get("ACT_NUM"));
                                            HashMap tdsMap = new HashMap();
                                            //comented at 06/02/2015  no need tds calculation in kerala scb banks approved by soji (with presence of jibi)
                                           // tdsMap = tds.tdsCalcforInt(CustId, interestAmt, accnum, Prod_type, prod_id, null);
                                            interestAmt = interestAmt1;
                                            intTrfAmt = interestAmt;
                                            
                                            lstIntCrDt = DateUtil.addDays(lstIntCrDt, -fr);
                                            
                                            if (Tot_Int_AMT > 0 && DateUtil.dateDiff(depMatDt, nextCalsDt) >= 0) {
                                                interestAmt = Tot_Int_AMT - Tot_Int_Cr;
                                                intTrfAmt = interestAmt;
                                                nextCalsDt = depMatDt;
                                            }
                                            paramMap.put("END", nextCalsDt);
                                        } else if (DateUtil.dateDiff(lstproDt, lstIntCrDt) <= 0 && DateUtil.dateDiff(nextCalsDt, depMatDt) >= 0) {
                                            
                                            double DiffAmt = 0.0;
                                            if (DateUtil.dateDiff(lstIntCrDt, depDt) != 0 || DateUtil.dateDiff(lstproDt, depDt) != 0) { // for the prpose of renwal after
                                                pr = DateUtil.dateDiff(lstIntCrDt, lstproDt);
                                                if (DateUtil.dateDiff(lstIntCrDt, depDt) >= 0) {
                                                    
                                                }
                                                pr = pr + 1; //FRO MHCB
                                                period1 = pr * 30;
                                                period1 = period1 / 30;

                                                DiffAmt = (period1 * roi * calcAmt) / 36500;

                                                DiffAmt = (double) getNearest((long) (DiffAmt * 100), 100) / 100;
                                            }
                                            System.out.println("###############lstIntCrDt"+lstIntCrDt+"############nextCalsDt"+nextCalsDt);
                                            pr = DateUtil.dateDiff(lstIntCrDt, nextCalsDt);
                                            period1 = pr / 30;
                                            System.out.println("rrrrrrrrrrr"+period1);
                                            period1 = (double) getNearest((long) (period1 * 100), 100) / 100;    
                                            interestAmt = (period1 * roi * calcAmt) / 1200;
                                            if(depInt!=null && depInt.equals("N")){
                                            	interestAmt = (double) getNearest((long) (interestAmt * 100), 100) / 100;
                                            }
                                            intTrfAmt = interestAmt;
                                            interestAmt = interestAmt - DiffAmt;
                                            System.out.println("rish.......9898"+interestAmt);
                                            if (Tot_Int_AMT > 0 && DateUtil.dateDiff(depMatDt, nextCalsDt) >= 0) {
                                                interestAmt = Tot_Int_AMT - Tot_Int_Cr;
                                                
                                                intTrfAmt = interestAmt + DiffAmt;
                                                nextCalsDt = depMatDt;
                                            }
                                         
                                            paramMap.put("END", nextCalsDt);
                                            paramMap.put("START", lstproDt);
                                            double interestAmt1 = 0.0;
                                            double interestAmt2 = 0.0;
                                            System.out.println("rish####################"+lstIntCrDt);
                                              System.out.println("rids#############lstIntCrDt"+lstIntCrDt);
                                                System.out.println("rids#############fr"+fr);
                                            Date lstIntCrDt1 = DateUtil.addDays(lstIntCrDt, fr);
                                             System.out.println("rish####################lstIntCrDt1"+lstIntCrDt1);
                                            if (DateUtil.dateDiff(lstIntCrDt1, currentDate) >= 0) {
                                                //                                                            lstIntCrDt=lstIntCrDt1;
                                                lstIntCrDt = lstIntCrDt1;
                                                int i = 0;
                                                for (lstIntCrDt = DateUtil.addDays(lstIntCrDt, fr); DateUtil.dateDiff(lstIntCrDt, currentDate) >= 0; lstIntCrDt = DateUtil.addDays(lstIntCrDt, freq(lstIntCrDt,fr))) {
                                                    if (DateUtil.dateDiff(nextCalsDt, depMatDt) >= 0) {
                                                        i = i + 1;
                                                        interestAmt2 = (calcAmt * roi * fr) / (1200 * 30);
                                                        if(depInt!=null && depInt.equals("N")){
                                                        	interestAmt2 = (double) getNearest((long) (interestAmt2 * 100), 100) / 100;
                                                        }
                                                        interestAmt1 = interestAmt1 + interestAmt2;
                                                        objInterestBatchTO.setIntAmt(new Double(interestAmt2));
                                                objInterestBatchTO.setIntDt(getProperFormatDate(DateUtil.addDays(nextCalsDt, 1)));
                                                objInterestBatchTO.setApplDt(getProperFormatDate(lstIntCrDt));
                                                interestApplictionDeleteInsert(objInterestBatchTO, false);
                                                        nextCalsDt = lstIntCrDt;
                                                   }
                                                }
                                               System.out.println("rish##############"+interestAmt1);
                                               System.out.println("rish##############"+interestAmt);
                                                interestAmt = interestAmt + interestAmt1;
                                                intTrfAmt = interestAmt1 + intTrfAmt;
                                                lstIntCrDt = DateUtil.addDays(lstIntCrDt, -fr);
                                                paramMap.put("END", nextCalsDt);
                                                  System.out.println("rish.....10001"+interestAmt);
                                                if (Tot_Int_AMT > 0 && DateUtil.dateDiff(depMatDt, nextCalsDt) >= 0) {
                                                    interestAmt = Tot_Int_AMT - Tot_Int_Cr;
                                                    intTrfAmt = interestAmt + DiffAmt;
                                                }
                                            }
                                            System.out.println("rish....."+interestAmt);
                                            
                                             if(depInt!=null && depInt.equals("Y")){
                                                  intTrfAmt = (double) getNearest((long) (intTrfAmt * 100), 100) / 100;
                                             }
                                             
                                              System.out.println("rish##############ffff"+interestAmt1);
                                              System.out.println("rish....."+interestAmt);
                                        }
                                    } else if (acctDtlMap.containsKey("FREQ") && CommonUtil.convertObjToInt(acctDtlMap.get("FREQ")) == 0) {
                                        long onMatPer = 0;
                                        onMatPer = DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("START"))), DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("END"))));
                                        if (DateUtil.dateDiff(lstIntCrDt, depDt) <= 0 && DateUtil.dateDiff(lstIntCrDt, lstproDt) < 0) {
                                            onMatPer = onMatPer + 1;
                                        }
                                        double roi = 0.0;
                                        double calcAmt = CommonUtil.convertObjToDouble(acctDtlMap.get("DEPOSIT_AMT")).doubleValue();
                                        roi = CommonUtil.convertObjToDouble(acctDtlMap.get("RATE_OF_INT")).doubleValue();
                                        interestAmt = (onMatPer * roi * calcAmt) / 36500;
                                        interestAmt = (double) getNearest((long) (interestAmt * 100), 100) / 100;
                                        intTrfAmt = Tot_Int_AMT;
                                        if (Tot_Int_AMT > 0 && DateUtil.dateDiff(depMatDt, nextCalsDt) >= 0) {
                                            interestAmt = Tot_Int_AMT - Tot_Int_Cr;
                                            intTrfAmt = interestAmt;
                                            intTrfAmt = Tot_Int_AMT;
                                        }
                                    } else {
                                        double period1 = 0.0;
                                        double pr = 0.0;
                                        period1 = pr / 30;
                                        double roi = 0.0;
                                        roi = CommonUtil.convertObjToDouble(acctDtlMap.get("RATE_OF_INT")).doubleValue();
                                        double calcAmt = CommonUtil.convertObjToDouble(acctDtlMap.get("DEPOSIT_AMT")).doubleValue();
                                        interestAmt = (period1 * roi * calcAmt) / 1200;
                                        interestAmt = (double) getNearest((long) (interestAmt * 100), 100) / 100;
                                        intTrfAmt = interestAmt;
                                    }
                                 }

                                //Transaction Starting
                                if (isTransaction) {
                                    //Added By Suresh
                
                                    if (cashToTransferMap != null && cashToTransferMap.size() > 0) {
                                        acctDtlMap.put("INTPAY_MODE", "TRANSFER");
                                        if(cashToTransferMap.containsKey("PROD_TYPE") && cashToTransferMap.get("PROD_TYPE").equals(TransactionFactory.GL)){
                                            acctDtlMap.put("INT_PAY_ACC_NO", cashToTransferMap.get("ACC_HEAD"));
                                            acctDtlMap.put("INT_PAY_PROD_TYPE", TransactionFactory.GL);
                                            acctDtlMap.put("INT_PAY_PROD_ID", null);
                                        }
                                        else{
                                            acctDtlMap.put("INT_PAY_ACC_NO", cashToTransferMap.get("OPERATIVE_NO"));
                                            //acctDtlMap.put("INT_PAY_PROD_TYPE", "OA");
                                            acctDtlMap.put("INT_PAY_PROD_TYPE",cashToTransferMap.get("PROD_TYPE"));
                                            acctDtlMap.put("INT_PAY_PROD_ID", cashToTransferMap.get("PROD_ID"));
                                        }        
                                        
                                    }
                                    interestAmt = CommonUtil.convertObjToDouble(paramMap.get("INTEREST_AMOUNT")).doubleValue();
                                    intTrfAmt = CommonUtil.convertObjToDouble(paramMap.get("INTEREST_AMOUNT")).doubleValue();
                                    

                                    sqlMap.startTransaction();
                                    double roi = 0.0;
                                    double tdsDeductAmt = 0.0;
                                    TransferTrans transferTrans = new TransferTrans();
                                    ArrayList batchList = new ArrayList();
                                    TdsCalc tds = new TdsCalc(actBranch);
                                    String CustId = CommonUtil.convertObjToStr(paramMap.get("CUST_ID"));
                                    String Prod_type = CommonUtil.convertObjToStr(paramMap.get("PROD_TYPE"));
                                    String prod_id = CommonUtil.convertObjToStr(paramMap.get(InterestTaskRunner.PRODUCT_ID));
                                    String accnum = CommonUtil.convertObjToStr(paramMap.get("ACT_NUM"));
                                    HashMap tdsMap = new HashMap();
                                    Date applDt1 = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("END")));
                                    Date applnDt1 = (Date) currentDate.clone();
                                    if (applDt1.getDate() > 0) {
                                        applnDt1.setDate(applDt1.getDate());
                                        applnDt1.setMonth(applDt1.getMonth());
                                        applnDt1.setYear(applDt1.getYear());
                                    }
                                    tdsMap.put("INT_DATE", applnDt1);
                                    tdsMap.put("CUSTID", CustId);
                                     //comented at 06/02/2015  no need tds calculation in kerala scb banks approved by soji (with presence of jibi)
//                                    List exceptionList = (List) sqlMap.executeQueryForList("getTDSExceptionData", tdsMap);
//                                    if (exceptionList == null || exceptionList.size() <= 0) {
//                                        tdsMap = new HashMap();
//                                        tdsMap = tds.tdsCalcforInt(CustId, interestAmt, accnum, Prod_type, prod_id, null);
//                                    }
                                    tdsMap = null;
                                    objInterestBatchTO = new InterestBatchTO();
                                    objInterestBatchTO.setActNum(CommonUtil.convertObjToStr(paramMap.get("ACT_NUM")));
                                    objInterestBatchTO.setIsTdsApplied("N");
                                    roi = CommonUtil.convertObjToDouble(acctDtlMap.get("RATE_OF_INT")).doubleValue();
                                    objInterestBatchTO.setAcHdId(CommonUtil.convertObjToStr(paramMap.get("CREDIT_INT")));
                                    objInterestBatchTO.setIntAmt(new Double(interestAmt));
                                    objInterestBatchTO.setIntRate(new Double(roi));
                                    objInterestBatchTO.setIntType("SIMPLE");
                                    objInterestBatchTO.setPrincipleAmt(CommonUtil.convertObjToDouble(acctDtlMap.get("DEPOSIT_AMT")));

                                    if (tdsMap != null) {
                                        double tdsDeductedForBaseAccount = CommonUtil.convertObjToDouble(tdsMap.get("TDSDEDUCTEDTOBASEACCOUNT")).doubleValue();
                                        tdsDeductAmt = CommonUtil.convertObjToDouble(tdsMap.get("TDSDRAMT")).doubleValue();
                                        if (tdsDeductedForBaseAccount > 0) {
                                            objInterestBatchTO.setIsTdsApplied("Y");
                                            objInterestBatchTO.setTdsAmt(new Double(tdsDeductedForBaseAccount));
                                            //                                                        interestAmt=interestAmt-tdsDeductAmt;
                                            objInterestBatchTO.setIntAmt(new Double(interestAmt - tdsDeductAmt));
                                            objInterestBatchTO.setTdsDeductedFromAll(new Double(tdsDeductAmt));
                                            objInterestBatchTO.setTotalTdsAmt(new Double(tdsDeductedForBaseAccount));
                                            objInterestBatchTO.setLastTdsRecivedFrom(CommonUtil.convertObjToStr(tdsMap.get("DEBIT_ACT_NUM")));
                                            //                                                        objInterestBatchTO.setLastTdsRecivedFrom(CommonUtil.convertObjToStr(paramMap.get("ACT_NUM")));
                                            objInterestBatchTO.setLastTdsApplDt(currentDate);

                                        }

                                        if (tdsDeductAmt > 0) {
                                            HashMap txMap = new HashMap();
                                            txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(tdsMap.get("TDSCrACHdId")));
                                            txMap.put(TransferTrans.CR_PROD_TYPE, "GL");
                                            txMap.put(TransferTrans.CURRENCY, "INR");
                                            txMap.put(TransferTrans.PARTICULARS, accnum + "TdsDeducted");
                                            txMap.put(TransferTrans.CR_BRANCH, actBranch);
                                            txMap.put("generateSingleTransId",generateSingleTransId);
                                            txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);
                                            tdsDeductAmt=CommonUtil.convertObjToDouble(tdsMap.get("TDSDRAMT")).doubleValue();
                                            batchList.add(transferTrans.getCreditTransferTO(txMap,tdsDeductAmt));
                                            intTrfAmt=intTrfAmt-tdsDeductAmt;
                                            objInterestBatchTO.setActNum(CommonUtil.convertObjToStr(paramMap.get("ACT_NUM")));
                                        }
                                    }

                                    Date applnDt = (Date) currentDate.clone();
                                    Date applDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("END")));
                                    if (applDt.getDate() > 0) {
                                        applnDt.setDate(applDt.getDate());
                                        applnDt.setMonth(applDt.getMonth());
                                        applnDt.setYear(applDt.getYear());
                                    }
                                    objInterestBatchTO.setApplDt(applnDt);


                                    //                                    }
                                    interestAmt = (double) getNearest((long) (interestAmt * 100), 100) / 100;
                                    double Tot_Int_bal = (interestAmt + Tot_Int_Cr) - Tot_Int_Dr - tdsDeductAmt;
                                        System.out.println("rish..##########"+Tot_Int_bal);
                                    HashMap resultMap = new HashMap();
                                    Date nxtCalDt = null;
                                    if (CommonUtil.convertObjToStr(acctDtlMap.get("CALENDER_FREQ")).length() > 0 && CommonUtil.convertObjToStr(acctDtlMap.get("CALENDER_FREQ")).equals("Y")) {
                                        do {

                                            nxtCalDt = DateUtil.addDays(nextCalsDt, CommonUtil.convertObjToInt(acctDtlMap.get("FREQ")));
                                            nextCalsDt = DateUtil.addDays(nextCalsDt, CommonUtil.convertObjToInt(acctDtlMap.get("FREQ")));
                                            objInterestBatchTO.setApplDt(nxtCalDt);
                                            applnDt = nextCalsDt;
                                        } while (DateUtil.dateDiff(DateUtil.addDays(nextCalsDt, CommonUtil.convertObjToInt(acctDtlMap.get("FREQ"))), currentDate) >= 0);
                                    } else {
                                        nextCalsDt = (Date) currentDate.clone();
                                        Date uptoDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("INT_CALC_UPTO_DT")));
                                        if (uptoDt.getDate() > 0) {
                                            nextCalsDt.setDate(uptoDt.getDate());
                                            nextCalsDt.setMonth(uptoDt.getMonth());
                                            nextCalsDt.setYear(uptoDt.getYear());
                                            applnDt = nextCalsDt;
                                        }

                                        nxtCalDt = nextCaldate(depDt, nextCalsDt, acctDtlMap);
                                    }
                                    resultMap = prepareMap(paramMap, resultMap);
                                    Date apStDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("START")));
                                    Date apEdDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("END")));
                                    apStDt = DateUtil.addDays(apStDt, 1);
                                    if (interestAmt < 0 && DateUtil.dateDiff(apStDt, apEdDt) == 0) {
                                        resultMap.put(TransferTrans.DR_AC_HD, (String) paramMap.get("CREDIT_INT"));
                                        resultMap.put(TransferTrans.CR_AC_HD, (String) paramMap.get("DEBIT_INT"));
                                        interestAmt = interestAmt * -1;
                                    }
                                    resultMap.put("LINK_BATCH_ID", paramMap.get("ACT_NUM"));
                                    resultMap.put("TRANS_MOD_TYPE",TransactionFactory.DEPOSITS);
                                    transferTrans.setLinkBatchId(CommonUtil.convertObjToStr(paramMap.get("ACT_NUM")));
                                    batchList.add(transferTrans.getDebitTransferTO(resultMap, interestAmt));
                                    batchList.add(transferTrans.getCreditTransferTO(resultMap, interestAmt - tdsDeductAmt));
                                    transferTrans.setInitiatedBranch(actBranch);
                                    if (interestAmt != 0 || CommonUtil.convertObjToInt(acctDtlMap.get("FREQ")) == 0) {
//                                        transferTrans.doDebitCredit(batchList, branch); commented by shihad to block prov entry
                                    }

                                    Date nextDt = (Date) currentDate.clone();
                                    Date nextCalcDt = nxtCalDt;
                                    if (nextCalcDt.getDate() > 0) {
                                        nextDt.setDate(nextCalcDt.getDate());
                                        nextDt.setMonth(nextCalcDt.getMonth());
                                        nextDt.setYear(nextCalcDt.getYear());
                                    }
                                    Date currDt = (Date) currentDate.clone();
                                    Date stDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("START")));
                                    if (stDt.getDate() > 0) {
                                        currDt.setDate(stDt.getDate());
                                        currDt.setMonth(stDt.getMonth());
                                        currDt.setYear(stDt.getYear());
                                    }
                                    objInterestBatchTO.setProductId(CommonUtil.convertObjToStr(paramMap.get("PROD_ID")));
                                    objInterestBatchTO.setApplDt(applnDt);
                                    objInterestBatchTO.setIntDt(currDt);
                                    objInterestBatchTO.setTrnDt(currentDate);
                                    objInterestBatchTO.setDrCr("CREDIT");
                                    objInterestBatchTO.setTot_int_amt(new Double(Tot_Int_bal));
                                    objInterestBatchTO.setTransLogId("A");
                                    objInterestBatchTO.setCustId(CommonUtil.convertObjToStr(paramMap.get("CUST_ID")));
                                    if (interestAmt != 0) {
                                        if (tdsDeductAmt > 0.0) {
                                            objInterestBatchTO.setIsTdsApplied("Y");
                                            objInterestBatchTO.setTdsAmt(CommonUtil.convertObjToDouble(tdsMap.get("TDSDEDUCTEDTOBASEACCOUNT")));
                                        }
                                        sqlMap.executeUpdate("insertDepositInterestTO", objInterestBatchTO);
                                    }
                                    if (tdsDeductAmt > 0.0) {
                                        objInterestBatchTO.setIntAmt(new Double(tdsDeductAmt));
                                        sqlMap.executeUpdate("updateTotIndDrawnAmount", objInterestBatchTO);
                                        objInterestBatchTO.setIntAmt(new Double(0));
                                    }
                                    HashMap upMapint = new HashMap();
                                    upMapint.put("actNum", paramMap.get("ACT_NUM"));
                                    upMapint.put("applDt", applnDt);
                                    upMapint.put("nextIntappldt", nextDt);
                                    upMapint.put("DEPOSIT_NO", paramMap.get("ACT_NUM"));
                                    List lst = sqlMap.executeQueryForList("getDepositAccountDetails", upMapint);
                                    if (lst != null && lst.size() > 0) {
                                        HashMap updateMap = (HashMap) lst.get(0);
                                        updateMap.put("TEMP_NEXT_INT_APPL_DT", updateMap.get("NEXT_INT_APPL_DT"));
                                        updateMap.put("TEMP_LAST_INT_APPL_DT", updateMap.get("LAST_INT_APPL_DT"));
                                        updateMap.put("INT_CREDIT", updateMap.get("TOTAL_INT_CREDIT"));
                                        updateMap.put("INT_DRAWN", updateMap.get("TOTAL_INT_DRAWN"));

                                        sqlMap.executeUpdate("updateDepositTempDate", updateMap);
                                    }
                                    sqlMap.executeUpdate("updateLastInterestAmount", objInterestBatchTO);
                                    if (tdsDeductAmt > 0.0) {
                                        objInterestBatchTO.setIntAmt(new Double(0));
                                    }                                    
                                    sqlMap.executeUpdate("updateDepositIntLastApplDT", upMapint);
                                    sqlMap.executeUpdate("updateDepositNextIntApplDT", upMapint);

                                    intTrfAmt = (double) getNearest((long) (intTrfAmt * 100), 100) / 100;
                                    if (acctDtlMap.get("INTPAY_MODE").equals("TRANSFER")) {
                                        if (acctDtlMap.get("INT_PAY_PROD_TYPE") != null && CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_PROD_TYPE")).length() > 0) {
                                            HashMap accStatusMap = new HashMap();
                                            //Added By Suresh
                                            if (CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_PROD_TYPE")).equals("OA") || CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_PROD_TYPE")).equals("INV")
                                                    || CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_PROD_TYPE")).equals("AD") || CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_PROD_TYPE")).equals("TL")) {
                                                String ProdType = CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_PROD_TYPE"));
                                                if (ProdType.equals("TL")) {
                                                    ProdType = "AD";
                                                }
                                                accStatusMap.put("ACT_NUM", CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_ACC_NO")));
                                                List stLst = sqlMap.executeQueryForList("getAccountStatus" + ProdType, accStatusMap);
                                                accStatusMap = (HashMap) stLst.get(0);
                                            }
                                            if (!CommonUtil.convertObjToStr(accStatusMap.get("ACT_STATUS_ID")).equals("CLOSED")) {
                                                intTrfAmt = (interestAmt + Tot_Int_Cr) - Tot_Int_Dr - tdsDeductAmt;
                                                if (behaves.equals(rec)) {
                                                    intTrfAmt = CommonUtil.convertObjToDouble(paramMap.get("AMT")).doubleValue();
                                                }
                                                if (behaves.equals(mds)) {
                                                    HashMap map = new HashMap();
                                                    TransactionTO transactionTO = new TransactionTO();
                                                    transactionDetailsTO = new LinkedHashMap();
                                                    LinkedHashMap transMap = new LinkedHashMap();
                                                    transactionTO.setTransType("TRANSFER");
                                                    transactionTO.setTransType("TRANSFER");
                                                    transactionTO.setTransAmt(CommonUtil.convertObjToDouble(((HashMap) accountList.get(acc)).get("TOTAL_DEMAND")));
                                                    transactionTO.setProductType("GL");
                                                    transactionTO.setDebitAcctNo(CommonUtil.convertObjToStr(paramMap.get(InterestTaskRunner.CREDIT_INT)));
                                                    transactionTO.setApplName(CommonUtil.convertObjToStr(paramMap.get(InterestTaskRunner.CREDIT_INT)));
                                                    transactionTO.setInstType("VOUCHER");


                                                    //                                                    transactionTo.setTransAmt(CommonUtil.convertObjToDouble(((HashMap)accountList.get(acc)).get("TOTAL_DEMAND")));
                                                    HashMap ChittalMap = new HashMap();
                                                    map.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(paramMap.get("USER_ID")));
                                                    map.put("BRANCH_CODE", actBranch);

                                                    transMap.put("1", transactionTO);
                                                    transactionDetailsTO.put("NOT_DELETED_TRANS_TOs", transMap);

                                                    map.put("TransactionTO", transactionDetailsTO);
                                                    ChittalMap.put("ACT_NUM", ((HashMap) accountList.get(acc)).get("ACCT_NUM"));

                                                    ChittalMap.put("PRINCIPAL", ((HashMap) accountList.get(acc)).get("PRINCIPAL"));
                                                    ChittalMap.put("TOTAL_DEMAND", ((HashMap) accountList.get(acc)).get("TOTAL_DEMAND"));
                                                    ChittalMap.put("INT_CALC_UPTO_DT", currentDate);
                                                    ChittalMap.put("PENAL_AMT", ((HashMap) accountList.get(acc)).get("PENAL_AMT"));
                                                    ChittalMap.put("BONUS_AMT", ((HashMap) accountList.get(acc)).get("BONUS_AMT"));
                                                    ChittalMap.put("DISCOUNT_AMT", ((HashMap) accountList.get(acc)).get("DISCOUNT_AMT"));
                                                    ChittalMap.put("INTEREST", ((HashMap) accountList.get(acc)).get("INTEREST"));
                                                    ChittalMap.put("CHARGES", ((HashMap) accountList.get(acc)).get("CHARGES"));
                                                    ChittalMap.put("ARBITRATION_AMT", ((HashMap) accountList.get(acc)).get("ARBITRATION_AMT"));

                                                    ChittalMap.put("NOTICE_AMT", ((HashMap) accountList.get(acc)).get("NOTICE_AMT"));

                                                    if (postageAmt > 0) {

                                                        ChittalMap.put("POSTAGE_AMT_FOR_INT", CommonUtil.convertObjToStr(new Double(postageAmt)));
                                                        ChittalMap.put("POSTAGE_ACHD", postageAcHd);
                                                    }
                                                    if (postageRenewAmt > 0) {
                                                        ChittalMap.put("POSTAGE_ACHD", postageAcHd);
                                                        ChittalMap.put("RENEW_POSTAGE_AMT", CommonUtil.convertObjToStr(new Double(postageRenewAmt)));
                                                    }
                                                    transactionPartMDS(ChittalMap, map);
                                                } else {
                                                    if (postageAmt > 0) {

                                                        acctDtlMap.put("POSTAGE_AMT", CommonUtil.convertObjToStr(new Double(postageAmt)));
                                                        acctDtlMap.put("POSTAGE_ACHD", postageAcHd);
                                                    }
                                                    if (postageRenewAmt > 0) {
                                                        acctDtlMap.put("POSTAGE_ACHD", postageAcHd);
                                                        acctDtlMap.put("RENEW_POSTAGE_AMT", CommonUtil.convertObjToStr(new Double(postageRenewAmt)));
                                                    }
                                                    dointTransferToAccount(acctDtlMap, intTrfAmt, paramMap, null);
                                                }
                                                if (interestList.size() == 0) {
                                                    interestList.add("");
                                                }
                                                if (acc == 0 || acc == accountListSize - 1) {
                                                    interestList.add(paramMap.get(paramMap.get("ACT_NUM")));
                                                }
                                                objInterestBatchTO.setIntAmt(new Double(intTrfAmt));
                                                objInterestBatchTO.setDrCr("DEBIT");
                                                Tot_Int_bal = Tot_Int_bal - intTrfAmt;
                                                objInterestBatchTO.setTot_int_amt(new Double(Tot_Int_bal));
                                                sqlMap.executeUpdate("insertDepositInterestTO", objInterestBatchTO);
                                                sqlMap.executeUpdate("updateTotIndDrawnAmount", objInterestBatchTO);
                                            }
                                        } else {
                                            throw new TTException("Interest Payment Product Type not found...");
                                        }
                                    } else {  //Cash Part
                                        List listData = ServerUtil.executeQuery("getCashierAuth", new HashMap());
                                        HashMap map1 = (HashMap) listData.get(0);
                                        if (map1.get("CASHIER_AUTH_ALLOWED") != null && map1.get("CASHIER_AUTH_ALLOWED").toString().equals("Y")) {
                                            dointCashInterest(intTrfAmt, paramMap, null);
                                        } else {
                                            dointCash(intTrfAmt, paramMap, null);
                                        }
                                        if (paramMap.containsKey(paramMap.get("ACT_NUM"))) {
                                            if (interestList.size() == 0) {
                                                interestList.add("");
                                            }
                                            if (acc == 0 || acc == accountListSize - 1) {
                                                interestList.add(paramMap.get(paramMap.get("ACT_NUM")));
                                            }
                                        }
                                    }
                                    sqlMap.commitTransaction();
                                    //conn.commit();
                                } else {
                                    System.out.println("reached 000000001");
                                    System.out.println("################################"+interestAmt);
                                   
                                    HashMap hmap = new HashMap();
                                    HashMap hshMap = null;
                                    HashMap postageMap = new HashMap();
                                    double postAmount = 0.0;
                                    double renewpostAmt = 0.0;
                                    interestRow = new ArrayList();
                                    String actNum = "";
                                    Date matDate = (Date) acctDtlMap.get("MATURITY_DT");
                                    long dateDiff;
                                    boolean flagM = Boolean.valueOf(paramMap.get("MATURITY").toString());
                                    if (flagM) {
                                        dateDiff = calculateDateDifference(currentDate, matDate);
                                    } else {
                                        dateDiff = calculateDateDifference(currentDate, matDate);
                                        if (dateDiff <= 0) {
                                        	paramMap.put("END", matDate);
                                        }                                                  
                                        dateDiff = 1;
                                    }
                                    if (dateDiff >= 0) {
                                        double Tot_Int_bal = (interestAmt + Tot_Int_Cr) - Tot_Int_Dr;
                                        if(depInt!=null && depInt.equals("Y")){
                                             interestAmt = (double) getNearest((long) (interestAmt * 100), 100) / 100;
                                             Tot_Int_bal = (double) getNearest((long) (Tot_Int_bal * 100), 100) / 100;
                                        }
                                        interestRow.add(new Boolean(false));
                                        interestRow.add(paramMap.get("CUST_ID"));
                                        actNum = CommonUtil.convertObjToStr(paramMap.get("ACT_NUM"));
                                        postageMap.put("ACT_NUM", actNum);
                                        List postageList1 = sqlMap.executeQueryForList("getPostageAmount", postageMap);
                                        List renewPostageList1 = sqlMap.executeQueryForList("getRenewPostageAmount", postageMap);
                                        if (postageList1 != null && postageList1.size() > 0) {
                                            postageMap = (HashMap) postageList1.get(0);
                                            postAmount = CommonUtil.convertObjToDouble(postageMap.get("POSTAGE_AMT")).doubleValue();

                                        }
                                        if (renewPostageList1 != null && renewPostageList1.size() > 0) {
                                            postageMap = (HashMap) renewPostageList1.get(0);
                                            renewpostAmt = CommonUtil.convertObjToDouble(postageMap.get("RENEW_POSTAGE_AMT")).doubleValue();
                                        }
                                        actNum = actNum.lastIndexOf("_") != -1 ? actNum.substring(0, actNum.length() - 2) : actNum;

                                        double renewalIntPayable = getRenewalInterest(actNum);
                                        if(Tot_Int_bal>0&&renewalIntPayable>0){
                                        Tot_Int_bal+=renewalIntPayable;
                                        }//total interest
                                        interestRow.add(actNum);
                                        interestRow.add(acctDtlMap.get("CNAME"));
                                        interestRow.add(acctDtlMap.get("DEPOSIT_AMT"));
                                        interestRow.add(DateUtil.getStringDate((Date) acctDtlMap.get("DEPOSIT_DT")));
                                        interestRow.add(DateUtil.getStringDate((Date) acctDtlMap.get("MATURITY_DT")));
                                        interestRow.add(DateUtil.getStringDate((Date) paramMap.get("START")));
                                        interestRow.add(DateUtil.getStringDate((Date) paramMap.get("END")));
                                        interestRow.add(new Double(Tot_Int_bal));
                                        interestRow.add(acctDtlMap.get("INT_PAY_ACC_NO"));
                                        interestRow.add("N");
                                        //babu added for colour change option 06/02/2015
                                            String colourStatus=CommonUtil.convertObjToStr(((HashMap) accountList.get(acc)).get("COLOUR_STATUS"));
                                            if(colourStatus!=null && colourStatus.equals("ML")){
                                                 interestRow.add("MATURED LIEN");
                                            }
                                            else if(colourStatus!=null && colourStatus.equals("M")){
                                                interestRow.add("MATURED");
                                            }
                                            else if(colourStatus!=null && colourStatus.equals("L")){
                                                interestRow.add("LIEN");
                                            }
                                            else{
                                               interestRow.add("NORMAL"); 
                                            }
                                            //end
                                        hmap.put("DEPOSIT_NO", actNum);
                                        hmap.put("LOAN_NO", CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_ACC_NO")));
                                      
                                        List LienList = sqlMap.executeQueryForList("getLienAccNo", hmap);

                                        List RdList = sqlMap.executeQueryForList("getRDAccountDetails", hmap);
                                        List LTDList = sqlMap.executeQueryForList("getLTDAccountDetailsForInterestPaying", hmap);
                                        if (LTDList != null & LTDList.size() > 0) {
                                            hmap = (HashMap) LTDList.get(0);
                                            String behave = CommonUtil.convertObjToStr(hmap.get("BEHAVES_LIKE"));
                                            double totLtdamt = CommonUtil.convertObjToDouble(hmap.get("TOTAL_BALANCE")).doubleValue();

                                            String acno = CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_ACC_NO"));
                                            String pid = CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_PROD_ID"));
                                            String pname = CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_PROD_TYPE"));
                                            //
                                            if (pname.equals("TL")) {
                                                HashMap loanMap = calcLoanPayments(acno, pid, pname);
                                                hshMap = new HashMap();
                                                totLtdamt = CommonUtil.convertObjToDouble(loanMap.get("TOTAL_DEMAND")).doubleValue();
                                                if (totLtdamt >= intTrfAmt) {
                                                    hshMap = new HashMap();
                                                    hshMap.put("ACCT_TYPE", "LOANS_AGAINST_DEPOSITS");
                                                    hshMap.put("AMOUNT", new Double(totLtdamt));
                                                    hshMap.put("ACT_NUM", actNum);
                                                    if (LienList.size() > 0 && LienList != null) {
                                                        hshMap.put("CHANGE_COLOR", "TRUE");
                                                    } else {
                                                        hshMap.put("CHANGE_COLOR", "");
                                                    }
                                                    rdList.add(hshMap);
                                                    setAccountsList(rdList);
                                                    interestList.add(interestRow);
                                                }
                                            } else {
                                                hshMap = new HashMap();
                                                hshMap.put("ACCT_TYPE", "");
                                                hshMap.put("AMOUNT", "");
                                                hshMap.put("ACT_NUM", actNum);
                                                if (LienList.size() > 0 && LienList != null) {
                                                    hshMap.put("CHANGE_COLOR", "TRUE");
                                                } else {
                                                    hshMap.put("CHANGE_COLOR", "");
                                                }

                                                rdList.add(hshMap);
                                                setAccountsList(rdList);
                                                interestList.add(interestRow);
                                            }
                                        } else if (RdList != null && RdList.size() > 0) {
                                            hmap = (HashMap) RdList.get(0);
                                            String recurring = "RECURRING";
                                            String behave = CommonUtil.convertObjToStr(hmap.get("BEHAVES_LIKE"));
                                            String amount = CommonUtil.convertObjToStr(hmap.get("DEPOSIT_AMT"));
                                            Date mdate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hmap.get("MATURITY_DT")));
                                            double actamount = intTrfAmt;
                                            double rdamount = CommonUtil.convertObjToDouble(amount).doubleValue();
                                            Date RDMatDt = (Date) currentDate.clone();
                                            RDMatDt.setDate(mdate.getDate());
                                            RDMatDt.setMonth(mdate.getMonth());
                                            RDMatDt.setYear(mdate.getYear());
                                            if (behave.equals(recurring)) {
                                                if (postAmount > 0.0) {
                                                    actamount = actamount - postAmount;
                                                } else if (renewpostAmt > 0.0) {
                                                    actamount = actamount - renewpostAmt;
                                                }
                                                if (DateUtil.dateDiff(currentDate, RDMatDt) > 0 && actamount >= rdamount) {
                                                    hshMap = new HashMap();
                                                    hshMap.put("ACCT_TYPE", "RECURRING");
                                                    hshMap.put("AMOUNT", amount);
                                                    hshMap.put("ACT_NUM", actNum);
                                                    if (LienList.size() > 0 && LienList != null) {
                                                        hshMap.put("CHANGE_COLOR", "TRUE");
                                                    } else {
                                                        hshMap.put("CHANGE_COLOR", "");
                                                    }

                                                    interestList.add(interestRow);
                                                    rdList.add(hshMap);
                                                    setAccountsList(rdList);
                                                }
                                            } else {
                                                hshMap = new HashMap();
                                                hshMap.put("ACCT_TYPE", "");
                                                hshMap.put("AMOUNT", "");
                                                hshMap.put("ACT_NUM", actNum);
                                                if (LienList.size() > 0 && LienList != null) {
                                                    hshMap.put("CHANGE_COLOR", "TRUE");
                                                } else {
                                                    hshMap.put("CHANGE_COLOR", "");
                                                }

                                                rdList.add(hshMap);
                                                setAccountsList(rdList);
                                                interestList.add(interestRow);

                                            }
                                        } else {
                                            String acno = CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_ACC_NO"));
                                            String pid = CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_PROD_ID"));
                                            String pname = CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_PROD_TYPE"));


                                            if (pname.equals(mds)) {
                                                HashMap mdsMap = calcMDS(acno, pid, pname);
                                                double totalDemand = 0.0;
                                                String demand = "";
                                                String principal = "";
                                                String penal = "";
                                                String bonus = "";
                                                String discount = "";
                                                String arbitration = "";
                                                String charges = "";
                                                String interest = "";
                                                String notice = "";
                                                if (mdsMap != null) {
                                                    totalDemand = CommonUtil.convertObjToDouble(mdsMap.get("TOTAL_DEMAND")).doubleValue();
                                                    demand = CommonUtil.convertObjToStr(mdsMap.get("TOTAL_DEMAND"));
                                                    principal = CommonUtil.convertObjToStr(mdsMap.get("PRINCIPAL"));
                                                    penal = CommonUtil.convertObjToStr(mdsMap.get("PENAL"));
                                                    bonus = CommonUtil.convertObjToStr(mdsMap.get("BONUS"));
                                                    discount = CommonUtil.convertObjToStr(mdsMap.get("DISCOUNT"));
                                                    charges = CommonUtil.convertObjToStr(mdsMap.get("CHARGES"));
                                                    interest = CommonUtil.convertObjToStr(mdsMap.get("INTEREST"));
                                                    arbitration = CommonUtil.convertObjToStr(mdsMap.get("ARBITRATION"));
                                                    notice = CommonUtil.convertObjToStr(mdsMap.get("NOTICE_AMT"));
                                                }
                                                //                                            acno = acno.lastIndexOf("_")!=-1 ? acno.substring(0,acno.length()-2) : acno;
                                                HashMap mdsHashMap = new HashMap();
                                                mdsHashMap.put("CHITTALNO", acno);
                                                mdsHashMap = (HashMap) sqlMap.executeQueryForList("getMDSAccountDetailsForInterestPaying", mdsHashMap).get(0);
                                                Date chitEndDt = setProperDtFormat(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(mdsHashMap.get("CHIT_END_DT"))));
                                                if (postAmount > 0.0) {
                                                    intTrfAmt = intTrfAmt - postAmount;
                                                } else if (renewpostAmt > 0.0) {
                                                    intTrfAmt = intTrfAmt - renewpostAmt;
                                                }
                                                if (totalDemand > 0) {
                                                    if (intTrfAmt >= totalDemand && DateUtil.dateDiff(currentDate, chitEndDt) > 0) {
                                                        hshMap = new HashMap();
                                                        hshMap.put("ACCT_TYPE", "MDS");
                                                        hshMap.put("ACCT_NUM", CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_ACC_NO")));
                                                        hshMap.put("PRINCIPAL", principal);
                                                        hshMap.put("TOTAL_DEMAND", demand);
                                                        hshMap.put("PENAL", penal);
                                                        hshMap.put("BONUS", bonus);
                                                        hshMap.put("DISCOUNT", discount);
                                                        hshMap.put("CHARGES", charges);
                                                        hshMap.put("INTEREST", interest);
                                                        hshMap.put("ARBITRATION", arbitration);
                                                        hshMap.put("NOTICE_AMT", notice);

                                                        if (LienList.size() > 0 && LienList != null) {
                                                            hshMap.put("CHANGE_COLOR", "TRUE");
                                                        } else {
                                                            hshMap.put("CHANGE_COLOR", "");
                                                        }

                                                        rdList.add(hshMap);
                                                        setAccountsList(rdList);
                                                        interestList.add(interestRow);

                                                        mdsHashMap = null;
                                                    }
                                                }

                                            } else {
                                                hshMap = new HashMap();
                                                hshMap.put("ACCT_TYPE", "");
                                                hshMap.put("AMOUNT", "");
                                                hshMap.put("ACT_NUM", actNum);
                                                if (LienList.size() > 0 && LienList != null) {
                                                    hshMap.put("CHANGE_COLOR", "TRUE");
                                                } else {
                                                    hshMap.put("CHANGE_COLOR", "");
                                                }

                                                rdList.add(hshMap);
                                                setAccountsList(rdList);
                                                interestList.add(interestRow);
                                            }
                                            
                                        }
                                        Tot_Int_bal = 0;
                                    }
                                }
                            }
                        } catch (Exception e) {
                            if (isTransaction) {
                                sqlMap.rollbackTransaction();
                                if (errorMap == null) {
                                    errorMap = new HashMap();
                                }
                                String actNum = CommonUtil.convertObjToStr(paramMap.get("ACT_NUM"));
                                actNum = actNum.lastIndexOf("_") != -1 ? actNum.substring(0, actNum.length() - 2) : actNum;
                                errorMap.put(actNum, new TTException(e));
                                if (interestList.size() >= 1) {
                                    interestList.set(0, errorMap);
                                } else {
                                    interestList.add(0, errorMap);
                                }
                            }
                            e.printStackTrace();
                        }
                    }
                    if (fFlag != null && fFlag.equals("SAVE")) {
                        if (acctDtlMap != null && acctDtlMap.get("INTPAY_MODE").equals("TRANSFER")) {
                        } else {
                            cashTransMapInt.put("DAILYDEPOSITTRANSTO", cashListInt);
                            cashTransMapInt.put(CommonConstants.BRANCH_ID,branch);
                            HashMap cashMap = cashTransactionDAO.execute(cashTransMapInt, false);
                            paramMap.put(paramMap.get("ACT_NUM"), cashMap.get(CommonConstants.TRANS_ID));
                            interestList.add(cashMap.get(CommonConstants.TRANS_ID));
                            cashTransactionDAO = null;
                            cashListInt.clear();
                        }
                    }
                    //CALENDER_FREQ='Y'   => First Time only Calling this Method Based on "Deposit_Dt"
                    if (!isTransaction || (paramMap.containsKey("CAL_FREQ_ACCOUNT_LIST") && isTransaction)) {
                        //Added by nithya on 08-05-2020 for KD-1535 
                        if (paramMap.containsKey("SPECIAL_RD_SCHEME") && paramMap.get("SPECIAL_RD_SCHEME") != null && CommonUtil.convertObjToStr(paramMap.get("SPECIAL_RD_SCHEME")).equalsIgnoreCase("SPECIAL_RD_SCHEME")) {
                            nonBatch.put("SPECIAL_RD_SCHEME", "SPECIAL_RD_SCHEME");
                        }
                        if (paramMap.containsKey("RD_INT_APPLICATION") && paramMap.get("RD_INT_APPLICATION") != null && CommonUtil.convertObjToStr(paramMap.get("RD_INT_APPLICATION")).equalsIgnoreCase("RD_INT_APPLICATION")) {
                            nonBatch.put("RD_INT_APPLICATION", "RD_INT_APPLICATION");
                        }
                        //End
//                        calenderFreqIntCalcForTable(nonBatch, interestList);
                    }
                }
            }
            paramMap = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return interestList;
    }
    public int freq(Date d, int fr) {
        boolean flag = false;
        Date instdate = null;
        instdate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(d));
        instdate = setProperDtFormat(instdate);
        SimpleDateFormat s = new SimpleDateFormat("dd-mm-yyyy");
        String date = s.format(instdate);
        int year = CommonUtil.convertObjToInt(date.substring(6, 10));
        int month = CommonUtil.convertObjToInt(date.substring(3, 5));
        Calendar cal = Calendar.getInstance();
        cal.setTime(instdate);
        int year1 = cal.get(Calendar.YEAR);
        int month1 = cal.get(Calendar.MONTH);
        int day1 = cal.get(Calendar.DAY_OF_MONTH);
        if (month1 == 1||month1==0) {
            flag = findLeapyear(year1);
            if (flag) {
                return fr - 1;
            } else {
                return fr - 2;
            }
        } else {
            return fr;
        }
        // return 1;
    }

    public void setAccountsList(ArrayList recdList) {
        this.recdList = recdList;
    }

    public ArrayList getAccountsList() {
        return recdList;
    }

    private double getRenewalInterest(String deposit_Number) {
        double InterestAmt = 0.0;
        Iterator listIterator;
        try {
            HashMap interstMap = new HashMap();
            interstMap.put("ACT_NUM", deposit_Number);
            ArrayList reIntrstList = (ArrayList) sqlMap.executeQueryForList("Deposit.getRenewalInteres", interstMap);
            if (reIntrstList != null && reIntrstList.size() > 0) {
                interstMap = (HashMap) reIntrstList.get(0);
                listIterator = reIntrstList.listIterator();
                while (listIterator.hasNext()) {
                    interstMap = (HashMap) listIterator.next();
                    if (interstMap.get("RENEWAL_INTEREST_PAID") != null && !interstMap.get("RENEWAL_INTEREST_PAID").equals("")) {
                        InterestAmt += CommonUtil.convertObjToDouble(interstMap.get("RENEWAL_INTEREST_PAID").toString());
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return InterestAmt;
    }

    public HashMap interestCalculationTLAD1(Object accountNo, Object prod_id, String prodType) throws Exception {
        HashMap dataMap = new HashMap();
        HashMap hash = new HashMap();
        try {
            hash.put("ACT_NUM", accountNo);
            hash.put("PRODUCT_TYPE", prodType);
            hash.put("PROD_ID", prod_id);
            //            hash.put("TRANS_DT", interestUptoDt);
            hash.put("TRANS_DT", currentDate);
            hash.put("INITIATED_BRANCH", actBranch);
            String mapNameForCalcInt = "IntCalculationDetail";
            if (prodType.equals("AD")) {
                mapNameForCalcInt = "IntCalculationDetailAD";
            }
            List lst = sqlMap.executeQueryForList(mapNameForCalcInt, hash);
            if (lst != null && lst.size() > 0) {
                hash = (HashMap) lst.get(0);
                java.util.Iterator iterator = hash.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next().toString();
                    String value = hash.get(key).toString();
                  
                }
                if (hash.get("AS_CUSTOMER_COMES") != null && hash.get("AS_CUSTOMER_COMES").equals("N")) {
                    hash = new HashMap();
                    return hash;
                }
                hash.put("ACT_NUM", accountNo);
                hash.put("PRODUCT_TYPE", prodType);
                hash.put("PROD_ID", prod_id);
                hash.put("TRANS_DT", currentDate);
                hash.put("INITIATED_BRANCH", actBranch);
                hash.put("ACT_NUM", accountNo);
                hash.put("BRANCH_ID", actBranch);
                hash.put("BRANCH_CODE", actBranch);
                hash.put("LOAN_ACCOUNT_CLOSING", "LOAN_ACCOUNT_CLOSING");
                hash.put("CURR_DATE", currentDate);
                dataMap.put(CommonConstants.MAP_WHERE, hash);
                hash = new SelectAllDAO().executeQuery(dataMap);
                if (hash == null) {
                    hash = new HashMap();
                }
                if (hash.containsKey("DATA") && hash.get("DATA") != null) {
                    hash.putAll((HashMap) ((List) hash.get("DATA")).get(0));
                }
                hash.putAll((HashMap) dataMap.get(CommonConstants.MAP_WHERE));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hash;
    }

    public HashMap calcLoanPayments(String actNum, String prodId, String prodType) throws Exception {
        HashMap dataMap = new HashMap();
        HashMap whereMap = new HashMap();
        whereMap.put("ACT_NUM", actNum);
        int firstDay = 0;
        Date inst_dt = null;
        HashMap asAndWhenMap = interestCalculationTLAD1(actNum, prodId, prodType);
        HashMap insertPenal = new HashMap();
        List chargeList = null;
        HashMap loanInstall = new HashMap();
        loanInstall.put("ACT_NUM", actNum);
        loanInstall.put("BRANCH_CODE", _branchCode);
        if (prodType != null && prodType.equals("TL")) {      //Only TL
            HashMap allInstallmentMap = null;
            List paidAmt = sqlMap.executeQueryForList("getPaidPrinciple", loanInstall);
            allInstallmentMap = (HashMap) paidAmt.get(0);
            double totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPLE")).doubleValue();
            if (asAndWhenMap == null || (asAndWhenMap != null && asAndWhenMap.containsKey("AS_CUSTOMER_COMES") && (asAndWhenMap.get("AS_CUSTOMER_COMES") != null) && asAndWhenMap.get("AS_CUSTOMER_COMES").equals("N"))) {
                paidAmt = sqlMap.executeQueryForList("getIntDetails", loanInstall);
                if (paidAmt != null && paidAmt.size() > 0) {
                    allInstallmentMap = (HashMap) paidAmt.get(0);
                }
                double totExcessAmt = CommonUtil.convertObjToDouble(allInstallmentMap.get("EXCESS_AMT")).doubleValue();
                totPrinciple += totExcessAmt;
            }
            List lst = sqlMap.executeQueryForList("getAllLoanInstallment", loanInstall);
            //                    Date inst_dt=null;
            for (int i = 0; i < lst.size(); i++) {
                allInstallmentMap = (HashMap) lst.get(i);
                double instalAmt = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue();
                if (instalAmt <= totPrinciple) {
                    totPrinciple -= instalAmt;
                    inst_dt = new Date();
                    String in_date = CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
                    inst_dt = DateUtil.getDateMMDDYYYY(in_date);
                } else {
                    inst_dt = new Date();
                    String in_date = CommonUtil.convertObjToStr(allInstallmentMap.get("INSTALLMENT_DT"));
                    inst_dt = DateUtil.getDateMMDDYYYY(in_date);
                    totPrinciple = CommonUtil.convertObjToDouble(allInstallmentMap.get("PRINCIPAL_AMT")).doubleValue() - totPrinciple;
                    break;
                }
            }
            Date addDt = (Date) currentDate.clone();
            Date instDt = DateUtil.addDays(inst_dt, 1);
            addDt.setDate(instDt.getDate());
            addDt.setMonth(instDt.getMonth());
            addDt.setYear(instDt.getYear());
            loanInstall.put("FROM_DATE", addDt);//DateUtil.addDays(inst_dt,1));
            loanInstall.put("TO_DATE", currentDate);
            List lst1 = null;
            if (inst_dt != null && (totPrinciple > 0)) {
                lst1 = sqlMap.executeQueryForList("getTotalAmountOverDue", loanInstall);
            }
            double principle = 0;
            if (lst1 != null && lst1.size() > 0) {
                HashMap map = (HashMap) lst1.get(0);
                principle = CommonUtil.convertObjToDouble(map.get("PRINCIPAL_AMOUNT")).doubleValue();
            }
            totPrinciple += principle;
            insertPenal.put("CURR_MONTH_PRINCEPLE", new Double(totPrinciple));
            insertPenal.put("INSTALL_DT", inst_dt);
            if (asAndWhenMap.containsKey("MORATORIUM_INT_FOR_EMI")) {
                insertPenal.put("MORATORIUM_INT_FOR_EMI", asAndWhenMap.get("MORATORIUM_INT_FOR_EMI"));
            }
            if (asAndWhenMap != null && asAndWhenMap.containsKey("AS_CUSTOMER_COMES") && (asAndWhenMap.get("AS_CUSTOMER_COMES") != null) && asAndWhenMap.get("AS_CUSTOMER_COMES").equals("Y")) {
                double interest = CommonUtil.convertObjToDouble(asAndWhenMap.get("INTEREST")).doubleValue();
                double penal = CommonUtil.convertObjToDouble(asAndWhenMap.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
                List facilitylst = sqlMap.executeQueryForList("LoneFacilityDetailAD", loanInstall);
                if (facilitylst != null && facilitylst.size() > 0) {
                    HashMap hash = (HashMap) facilitylst.get(0);
                    hash.put("ACT_NUM", loanInstall.get("ACT_NUM"));
                    if (asAndWhenMap.containsKey("PREMATURE")) {
                        insertPenal.put("PREMATURE", asAndWhenMap.get("PREMATURE"));
                    }
                    if (asAndWhenMap.containsKey("PREMATURE") && asAndWhenMap.containsKey("PREMATURE_INT_CALC_AMT")
                            && CommonUtil.convertObjToStr(asAndWhenMap.get("PREMATURE_INT_CALC_AMT")).equals("LOANSANCTIONAMT")) {
                        hash.put("FROM_DT", hash.get("ACCT_OPEN_DT"));
                    } else {
                        hash.put("FROM_DT", hash.get("LAST_INT_CALC_DT"));
                        hash.put("FROM_DT", DateUtil.addDays(((Date) hash.get("FROM_DT")), 2));
                    }
                    //                            hash.put("TO_DATE", interestUptoDt.clone());
                    hash.put("TO_DATE", currentDate.clone());
                    if (!(asAndWhenMap != null && asAndWhenMap.containsKey("INSTALL_TYPE") && asAndWhenMap.get("INSTALL_TYPE") != null && asAndWhenMap.get("INSTALL_TYPE").equals("EMI"))) {
                        facilitylst = sqlMap.executeQueryForList("getPaidPrinciple", hash);
                    } else {
                        facilitylst = null;
                        if (asAndWhenMap.containsKey("PRINCIPAL_DUE") && asAndWhenMap.get("PRINCIPAL_DUE") != null) {
                            insertPenal.put("CURR_MONTH_PRINCEPLE", asAndWhenMap.get("PRINCIPAL_DUE"));
                        }
                    }
                    if (facilitylst != null && facilitylst.size() > 0) {
                        hash = (HashMap) facilitylst.get(0);
                        interest -= CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
                        penal -= CommonUtil.convertObjToDouble(hash.get("PENAL")).doubleValue();

                        insertPenal.put("PAID_INTEREST", hash.get("INTEREST"));
                    }
                }
                if (interest > 0) {
                    insertPenal.put("CURR_MONTH_INT", new Double(interest));
                } else {
                    insertPenal.put("CURR_MONTH_INT", new Double(0));
                }
                if (penal > 0) {
                    insertPenal.put("PENAL_INT", new Double(penal));
                } else {
                    insertPenal.put("PENAL_INT", new Double(0));
                }
                insertPenal.put("INTEREST", asAndWhenMap.get("INTEREST"));
                insertPenal.put("LOAN_CLOSING_PENAL_INT", asAndWhenMap.get("LOAN_CLOSING_PENAL_INT"));

                insertPenal.put("LAST_INT_CALC_DT", asAndWhenMap.get("LAST_INT_CALC_DT"));
                insertPenal.put("ROI", asAndWhenMap.get("ROI"));
                chargeList = sqlMap.executeQueryForList("getChargeDetails", loanInstall);
            } else {
                List getIntDetails = sqlMap.executeQueryForList("getIntDetails", loanInstall);
                HashMap hash = null;
                if (getIntDetails != null) {
                    for (int i = 0; i < getIntDetails.size(); i++) {
                        hash = (HashMap) getIntDetails.get(i);
                        String trn_mode = CommonUtil.convertObjToStr(hash.get("TRN_CODE"));
                        double pBal = CommonUtil.convertObjToDouble(hash.get("PBAL")).doubleValue();
                        double iBal = CommonUtil.convertObjToDouble(hash.get("IBAL")).doubleValue();
                        double pibal = CommonUtil.convertObjToDouble(hash.get("PIBAL")).doubleValue();
                        double excess = CommonUtil.convertObjToDouble(hash.get("EXCESS_AMT")).doubleValue();
                        pBal -= excess;
                        if (pBal < totPrinciple) {
                            insertPenal.put("CURR_MONTH_PRINCEPLE", new Double(pBal));
                        }
                        if (trn_mode.equals("C*")) {
                            insertPenal.put("CURR_MONTH_INT", String.valueOf(iBal + pibal));
                            insertPenal.put("PRINCIPLE_BAL", new Double(pBal));
                            insertPenal.put("EBAL", hash.get("EBAL"));
                            break;
                        } else {
                            if (!trn_mode.equals("DP")) {
                                insertPenal.put("CURR_MONTH_INT", String.valueOf(iBal + pibal));
                            }
                            insertPenal.put("EBAL", hash.get("EBAL"));
                            insertPenal.put("PRINCIPLE_BAL", new Double(pBal));
                        }
                     
                    }
                }
                getIntDetails = sqlMap.executeQueryForList("getPenalIntDetails", loanInstall);
                hash = (HashMap) getIntDetails.get(0);
                insertPenal.put("PENAL_INT", hash.get("PIBAL"));
            }
        }

        if (prodType != null && prodType.equals("AD")) // Only  AD
        {
            if (asAndWhenMap != null && asAndWhenMap.size() > 0) {
                if (asAndWhenMap.containsKey("AS_CUSTOMER_COMES") && asAndWhenMap.get("AS_CUSTOMER_COMES").equals("Y")) {
                    List facilitylst = sqlMap.executeQueryForList("LoneFacilityDetailAD", loanInstall);
                    double interest = CommonUtil.convertObjToDouble(asAndWhenMap.get("INTEREST")).doubleValue();
                    double penal = CommonUtil.convertObjToDouble(asAndWhenMap.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
                    if (facilitylst != null && facilitylst.size() > 0) {
                        HashMap hash = (HashMap) facilitylst.get(0);
                        hash.put("ACT_NUM", loanInstall.get("ACT_NUM"));
                        hash.put("FROM_DT", hash.get("LAST_INT_CALC_DT"));
                        hash.put("FROM_DT", DateUtil.addDays(((Date) hash.get("FROM_DT")), 2));
                        //                                hash.put("TO_DATE",DateUtil.addDaysProperFormat(interestUptoDt,-1));
                        hash.put("TO_DATE", DateUtil.addDaysProperFormat(currentDate, -1));
                        facilitylst = sqlMap.executeQueryForList("getPaidPrincipleAD", hash);
                        if (facilitylst != null && facilitylst.size() > 0) {
                            hash = (HashMap) facilitylst.get(0);
                            interest -= CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
                            penal -= CommonUtil.convertObjToDouble(hash.get("PENAL")).doubleValue();
                        }
                    }
                    if (interest > 0) {
                        insertPenal.put("CURR_MONTH_INT", new Double(interest));
                    } else {
                        insertPenal.put("CURR_MONTH_INT", new Double(0));
                    }
                    if (penal > 0) {
                        insertPenal.put("PENAL_INT", new Double(penal));
                    } else {
                        insertPenal.put("PENAL_INT", new Double(0));
                    }
                    insertPenal.put("INTEREST", asAndWhenMap.get("INTEREST"));
                    insertPenal.put("LOAN_CLOSING_PENAL_INT", asAndWhenMap.get("LOAN_CLOSING_PENAL_INT"));
                    chargeList = sqlMap.executeQueryForList("getChargeDetails", loanInstall);
                } else {
                    if (prodType != null && prodType.equals("AD")) {
                        List getIntDetails = sqlMap.executeQueryForList("getIntDetailsAD", loanInstall);
                        HashMap hash = null;

                        for (int i = 0; i < getIntDetails.size(); i++) {
                            hash = (HashMap) getIntDetails.get(i);
                            String trn_mode = CommonUtil.convertObjToStr(hash.get("TRN_CODE"));
                            double iBal = CommonUtil.convertObjToDouble(hash.get("IBAL")).doubleValue();
                            double pibal = CommonUtil.convertObjToDouble(hash.get("PIBAL")).doubleValue();
                            double excess = CommonUtil.convertObjToDouble(hash.get("EXCESS_AMT")).doubleValue();
                            if (trn_mode.equals("C*")) {
                                insertPenal.put("CURR_MONTH_INT", String.valueOf(iBal + pibal));
                                insertPenal.put("PRINCIPLE_BAL", new Double(CommonUtil.convertObjToDouble(hash.get("PBAL")).doubleValue() - excess));
                                insertPenal.put("EBAL", hash.get("EBAL"));
                                break;
                            } else {
                                insertPenal.put("CURR_MONTH_INT", String.valueOf(iBal + pibal));
                                insertPenal.put("PRINCIPLE_BAL", new Double(CommonUtil.convertObjToDouble(hash.get("PBAL")).doubleValue() - excess));
                                insertPenal.put("EBAL", hash.get("EBAL"));
                            }
                           
                        }
                        getIntDetails = sqlMap.executeQueryForList("getPenalIntDetailsAD", loanInstall);
                        if (getIntDetails.size() > 0) {
                            hash = (HashMap) getIntDetails.get(0);
                            insertPenal.put("PENAL_INT", hash.get("PIBAL"));
                        }
                        insertPenal.remove("PRINCIPLE_BAL");

                    }
                }
            }
        }
        //Added By Suresh  (Current Dt > To Date AND PBAL >0 in ADV_TRANS_DETAILS, Add Principle_Balance)
        if (prodType != null && prodType.equals("AD")) {
            double pBalance = 0.0;
            Date expDt = null;
            List expDtList = sqlMap.executeQueryForList("getLoanExpDate", loanInstall);
            if (expDtList != null && expDtList.size() > 0) {
                whereMap = new HashMap();
                whereMap = (HashMap) expDtList.get(0);
                pBalance = CommonUtil.convertObjToDouble(whereMap.get("PBAL")).doubleValue();
                expDt = (Date) whereMap.get("TO_DT");
                long diffDayPending = DateUtil.dateDiff(expDt, currentDate);
                if (diffDayPending > 0 && pBalance > 0) {
                    insertPenal.put("PRINCIPLE_BAL", new Double(pBalance));
                }
            }
        }
        //Charges
        double chargeAmt = 0.0;
        whereMap = new HashMap();
        whereMap.put("ACT_NUM", actNum);
        chargeAmt = getChargeAmount(whereMap, prodType);
        if (chargeAmt > 0) {
            dataMap.put("CHARGES", String.valueOf(chargeAmt));
        } else {
            dataMap.put("CHARGES", "0");
        }
        double totalDemand = 0.0;
        double principalAmount = 0.0;
        if (insertPenal.containsKey("CURR_MONTH_PRINCEPLE")) {
            principalAmount = CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_PRINCEPLE")).doubleValue();     // Principal Amount
            totalDemand = CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_INT")).doubleValue()
                    + CommonUtil.convertObjToDouble(insertPenal.get("PENAL_INT")).doubleValue() + chargeAmt;
        } else {
            principalAmount = CommonUtil.convertObjToDouble(insertPenal.get("PRINCIPLE_BAL")).doubleValue();     // Principal Amount
            totalDemand = CommonUtil.convertObjToDouble(insertPenal.get("CURR_MONTH_INT")).doubleValue()
                    + CommonUtil.convertObjToDouble(insertPenal.get("PENAL_INT")).doubleValue() + chargeAmt;
        }


        if (inst_dt != null && prodType.equals("TL")) {
            if (DateUtil.dateDiff(currentDate, inst_dt) <= 0) {
                dataMap.put("PRINCIPAL", String.valueOf(principalAmount));
            } else {
                dataMap.put("PRINCIPAL", "0");
                principalAmount = 0.0;
            }
        }
        if (prodType.equals("AD")) {
            if (principalAmount > 0) {
                dataMap.put("PRINCIPAL", String.valueOf(principalAmount));
            } else {
                dataMap.put("PRINCIPAL", "0");
            }
        }
        totalDemand += principalAmount;
        dataMap.put("INTEREST", insertPenal.get("CURR_MONTH_INT"));
        dataMap.put("PENAL", insertPenal.get("PENAL_INT"));
        dataMap.put("TOTAL_DEMAND", new Double(totalDemand));
        if (totalDemand <= 0) {
            dataMap = null;
        }
        return dataMap;
    }

    private double getChargeAmount(HashMap whereMap, String prodType) {   //Charges
        double chargeAmount = 0.0;
        try {
            List chargeList = null;
            String actNo = "";
            HashMap recoverChrgMap = new HashMap();
            actNo = CommonUtil.convertObjToStr(whereMap.get("ACT_NUM"));
            chargeList = sqlMap.executeQueryForList("getChargeDetails", whereMap);
            if (chargeList != null && chargeList.size() > 0) {
                for (int i = 0; i < chargeList.size(); i++) {
                    whereMap = (HashMap) chargeList.get(i);
                    chargeAmount += CommonUtil.convertObjToDouble(whereMap.get("CHARGE_AMT")).doubleValue();


                }
            }
            chargeList = null;
            recoverChrgMap = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chargeAmount;
    }

    private void initializeTaskObj(HashMap dataMap) throws Exception {
        paramMap = dataMap;
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
        if (paramMap != null && paramMap.containsKey("DAY_END_TYPE")) {
            dayEndType = CommonUtil.convertObjToStr(paramMap.get("DAY_END_TYPE"));
        }
        currentDate = ServerUtil.getCurrentDate(branch);
        if (CommonUtil.convertObjToStr(dayEndType).equals("BANK_LEVEL")) {
            HashMap tempMap = new HashMap();
            tempMap.put("NEXT_DATE", currentDate);
            if (paramMap.containsKey("PROCESS") && CommonUtil.convertObjToStr(paramMap.get("PROCESS")).equals(CommonConstants.DAY_END)) {
                if (paramMap.containsKey("BRANCH_LST")) {
                    branchList = (List) paramMap.get("BRANCH_LST");
                } else {
                    branchList = null;
                }
            } else {
                if (paramMap.containsKey("BRANCH_LST")) {
                    branchList = (List) paramMap.get("BRANCH_LST");
                } else {
                    branchList = null;
                }
            }
            tempMap = null;
        } else {
            HashMap tempMap = new HashMap();
            tempMap.put(CommonConstants.BRANCH_ID, getHeader().getBranchID());
            branchList = new ArrayList();
            branchList.add(tempMap);
            tempMap = null;
        }
        if (paramMap != null && paramMap.containsKey("DEP_INT_TASK_LABLE")) {
            taskLable = CommonUtil.convertObjToStr(paramMap.get("DEP_INT_TASK_LABLE"));
        }
        if (paramMap != null && paramMap.containsKey("DEP_INT_APPL_TASK_LABLE")) {
            taskLable = CommonUtil.convertObjToStr(paramMap.get("DEP_INT_APPL_TASK_LABLE"));
        }
    }

    private HashMap getAccountHead(HashMap paramMap, HashMap resultMap) throws Exception {
        HashMap recurringMap = (HashMap) paramMap.get("RECURRINGMAP");
        HashMap provisionMap = (HashMap) paramMap.get("PROVISIONMAP");
        String accHead = null;
        //Changed By Suresh
        String startDate = DateUtil.getStringDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("START"))));
        String endDate = DateUtil.getStringDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("END"))));
        if (getHeader().getTransactionType().equals(CommonConstants.CREDIT)) {
            if (paramMap.get("TASK").equals("PROVISIONING")) {
                Date lastApplDt = null;
                resultMap.put(TransferTrans.DR_AC_HD, (String) paramMap.get("DEBIT_INT")); //debit int paid on account head
                resultMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                resultMap.put(TransferTrans.DR_BRANCH, actBranch);
                resultMap.put("generateSingleTransId", generateSingleTransId);
                resultMap.put(TransferTrans.CR_AC_HD, (String)paramMap.get("CREDIT_INT")); //credit to int payable account head
                resultMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                resultMap.put(TransferTrans.CR_BRANCH, actBranch);
                if (paramMap.get("BEHAVES_LIKE").equals("RECURRING") || paramMap.get("BEHAVES_LIKE").equals("CUMMULATIVE")) {
                    lastApplDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(provisionMap.get("LAST_APPL_DT")));
                    int freq = CommonUtil.convertObjToInt(provisionMap.get("INT_APPL_FREQ"));
                    Date nextApplDate = DateUtil.addDays(lastApplDt, freq);
                    if (DateUtil.dateDiff((Date) nextApplDate, currentDate) == 0) {
                        resultMap.put(TransferTrans.DR_AC_HD, (String) paramMap.get("CREDIT_INT")); // debit int payable account head
                        resultMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                        resultMap.put(TransferTrans.DR_BRANCH, actBranch);
                        resultMap.put(TransferTrans.CR_PROD_ID, (String) paramMap.get("PROD_ID")); // credit to particular account head
                        resultMap.put(TransferTrans.CR_ACT_NUM, (String) paramMap.get("ACT_NUM"));
                        resultMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.DEPOSITS);
                        resultMap.put(TransferTrans.CR_BRANCH, actBranch);
                    }
                }
                resultMap.put(TransferTrans.PARTICULARS, "Provision made upto " + lastApplDt + " for " + CommonUtil.convertObjToStr(paramMap.get("PROD_ID")));
            } else {
                resultMap.put(TransferTrans.DR_AC_HD, (String) paramMap.get("DEBIT_INT")); //debit int paid on account head
                resultMap.put(TransferTrans.DR_PROD_TYPE, TransactionFactory.GL);
                resultMap.put(TransferTrans.DR_BRANCH, actBranch);

                resultMap.put(TransferTrans.CR_AC_HD, (String) paramMap.get("CREDIT_INT")); //credit to int payable account head
                resultMap.put(TransferTrans.CR_PROD_TYPE, TransactionFactory.GL);
                resultMap.put(TransferTrans.CR_BRANCH, actBranch);
                resultMap.put("generateSingleTransId", generateSingleTransId);
                //Changed By Suresh
//                resultMap.put(TransferTrans.PARTICULARS, "Fd Interest "+CommonUtil.convertObjToStr(paramMap.get("ACT_NUM")));
                resultMap.put(TransferTrans.PARTICULARS, "Fd Interest " + paramMap.get("ACT_NUM") + " From " + startDate + " To " + endDate);
               
            }

        }
        
        return resultMap;
    }

    private HashMap prepareMap(HashMap paramMap, HashMap resultMap) throws Exception {
        resultMap = getAccountHead(paramMap, resultMap);
        resultMap.put(TransferTrans.CURRENCY, LocaleConstants.DEFAULT_CURRENCY);

        return resultMap;
    }

    //__ Method to Calculate the Interest...
    private InterestBatchTO runBatch(HashMap resultMap, HashMap paramMap, LogDAO objLogDAO, LogTO objLogTO) throws Exception {
        InterestBatchTO objInterestBatchTO = null;
        double interestAmt = 0.0;
        double interest = 0.0;
        HashMap provisionMap = (HashMap) paramMap.get("PROVISIONMAP");
        HashMap recurringMap = (HashMap) paramMap.get("RECURRINGMAP");
        InterestTaskRunner interestTaskRun = new InterestTaskRunner();
        if (paramMap.get("TASK") == null) {
            resultMap.put("START", recurringMap.get("DEPOSIT_DT"));
            resultMap.put("END", currentDate);
        } else {
            if (DateUtil.dateDiff((Date) recurringMap.get("DEP_DATE"), (Date) recurringMap.get("MATURITY_DT")) > 0) {
                Date startDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(recurringMap.get("DEP_DATE")));
                Date depDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(resultMap.get("DEPOSIT_DT")));
                Date matDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(recurringMap.get("MATURITY_DT")));
                Date lastProvDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("LAST_PROV_DT")));
                Date nextProvDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("NEXTPROVDT")));
                Date lastApplDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("LAST_APPL_DT")));
                Date nextApplDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("NEXTAPPLDT")));
                long actualPeriod = DateUtil.dateDiff(startDt, matDt);//to get an actual period for each deposit holders.......
                long depositDiff = DateUtil.dateDiff(depDt, nextProvDt); //to get date difference from next provisioning to deposit date....
                long provDiff = DateUtil.dateDiff(lastProvDt, nextProvDt); //to get date difference from last provisioning to next provisioning date....
                long matDiff = DateUtil.dateDiff(nextProvDt, matDt); //to get date difference from next provisioning to maturity date....
                int accFreq = CommonUtil.convertObjToInt(resultMap.get("ACC_FREQ")); // account level freqency to getting and adding...
                Date nextFreqDt = DateUtil.addDays(depDt, accFreq); //last calc date and account level freqency to add and getting nextcalc date.....
                long befProvDiff = DateUtil.dateDiff(depDt, lastProvDt); // differenciate with depositdate(its picking from account level table) to nextcalcdate... it will get the accountlevel freq.....
                long aftProvDiff = DateUtil.dateDiff(lastProvDt, nextFreqDt);
                resultMap.put("ACTUALPERIOD", new Long(actualPeriod));

                if (paramMap.get("TASK").equals("PROVISIONING")) {
                    HashMap getInterestMap = new HashMap();
                    if (recurringMap.get("BEHAVES_LIKE").equals("RECURRING") || recurringMap.get("BEHAVES_LIKE").equals("CUMMULATIVE")) {
                        double matperiod = DateUtil.dateDiff((Date) startDt, (Date) matDt);
                        getInterestMap.put("AMOUNT", CommonUtil.convertObjToDouble(resultMap.get("AMOUNT")));
                        getInterestMap.put("CATEGORY_ID", resultMap.get("CATEGORY_ID"));
                        getInterestMap.put("DEPOSIT_DT", recurringMap.get("DEP_DATE"));
                        getInterestMap.put("PROD_ID", resultMap.get("PROD_ID"));
                        getInterestMap.put("PRODUCT_TYPE", "TD");
                        getInterestMap.put("PERIOD", new Double(matperiod));
                        final HashMap dataMap = (HashMap) sqlMap.executeQueryForObject("icm.getInterestRates", getInterestMap);
                    }
                    if (provDiff > depositDiff) {
                        if (!recurringMap.get("BEHAVES_LIKE").equals("RECURRING")) {
                            resultMap.put("START", depDt);
                            resultMap.put("END", currentDate);;
                            if (recurringMap.get("BEHAVES_LIKE").equals("CUMMULATIVE")) {
                                double principal = 0.0;
                                double rateOfInterest = 0.0;
                                final HashMap dataMap = (HashMap) sqlMap.executeQueryForObject("icm.getInterestRates", getInterestMap);
                                rateOfInterest = CommonUtil.convertObjToDouble(dataMap.get("ROI")).doubleValue();
                                principal = CommonUtil.convertObjToDouble(resultMap.get("AMOUNT")).doubleValue();
                                double period = DateUtil.dateDiff((Date) resultMap.get("START"), currentDate);
                                period = period / 30;
                                rateOfInterest = rateOfInterest / 100;
                                double amount = principal * (Math.pow((1 + rateOfInterest / 4.0), period / 12 * 4.0));
                                interest = amount - principal;
                            }
                        }
                        if (recurringMap.get("BEHAVES_LIKE").equals("RECURRING")) {
                            GregorianCalendar lastDate = new GregorianCalendar(startDt.getYear(), startDt.getMonth(), startDt.getDate());
                            startDt = new Date(startDt.getYear(), startDt.getMonth(), lastDate.getActualMinimum(Calendar.DAY_OF_MONTH));
                            double period = DateUtil.dateDiff((Date) startDt, currentDate);
                            final HashMap dataMap = (HashMap) sqlMap.executeQueryForObject("icm.getInterestRates", getInterestMap);
                            double rateOfInterest = CommonUtil.convertObjToDouble(dataMap.get("ROI")).doubleValue();
                            double principal = CommonUtil.convertObjToDouble(recurringMap.get("DEPOSIT_AMT")).doubleValue();
                            period = period / 30;
                            period = (double) getNearest((long) (period * 100), 100) / 100;
                            double amount = 2 * principal * (1.0 + 600.0 / rateOfInterest) * (Math.pow((1 + rateOfInterest / 400.0), period / 3) - 1.0);
                            interest = amount - (principal * period);
                        }
                    } else {
                        if (recurringMap.get("BEHAVES_LIKE").equals("CUMMULATIVE") || recurringMap.get("BEHAVES_LIKE").equals("RECURRING")) {
                            if (DateUtil.dateDiff((Date) lastProvDt, (Date) nextProvDt) >= 90) {
                                resultMap.put("START", lastProvDt);
                                resultMap.put("END", nextProvDt);
                                if (recurringMap.get("BEHAVES_LIKE").equals("RECURRING")) {
                                    double period = DateUtil.dateDiff((Date) lastProvDt, (Date) nextProvDt);
                                    final HashMap dataMap = (HashMap) sqlMap.executeQueryForObject("icm.getInterestRates", getInterestMap);
                                    String actNumber = CommonUtil.convertObjToStr(recurringMap.get("ACT_NUM"));
                                    actNumber = actNumber.substring(0, actNumber.lastIndexOf("_"));
                                    HashMap getLastTransMap = new HashMap();
                                    getLastTransMap.put("DEPOSIT_NO", actNumber);
                                    List lst = sqlMap.executeQueryForList("getLastTransDate", getLastTransMap);
                                    if (lst.size() > 0) {
                                        getLastTransMap = (HashMap) lst.get(0);
                                        long freq = CommonUtil.convertObjToLong(recurringMap.get("APPL_FREQ"));
                                        if (DateUtil.dateDiff((Date) getLastTransMap.get("LAST_TRANS_DT"), currentDate) < 90) {
                                            double rateOfInterest = CommonUtil.convertObjToDouble(dataMap.get("ROI")).doubleValue();
                                            double principal = CommonUtil.convertObjToDouble(recurringMap.get("DEPOSIT_AMT")).doubleValue();
                                            period = period / 30;
                                            period = (double) getNearest((long) (period * 100), 100) / 100;
                                            double amount = 2 * principal * (1.0 + 600.0 / rateOfInterest) * (Math.pow((1 + rateOfInterest / 400.0), period / 3) - 1.0);
                                            interest = amount - (principal * period);
                                        }
                                    }
                                }
                                if (recurringMap.get("BEHAVES_LIKE").equals("CUMMULATIVE")) {
                                    double principal = 0.0;
                                    double rateOfInterest = 0.0;
                                    final HashMap dataMap = (HashMap) sqlMap.executeQueryForObject("icm.getInterestRates", getInterestMap);
                                    rateOfInterest = CommonUtil.convertObjToDouble(dataMap.get("ROI")).doubleValue();
                                    principal = CommonUtil.convertObjToDouble(resultMap.get("AMOUNT")).doubleValue();
                                    double period = DateUtil.dateDiff((Date) lastProvDt, currentDate);
                                    rateOfInterest = rateOfInterest / 100;
                                    period = period / 30;
                                    double amount = principal * (Math.pow((1 + rateOfInterest / 4.0), period / 12 * 4.0));
                                    interest = amount - principal;
                                }
                            }
                        }
                    }
                    if (paramMap.get("CHARGES_PROCESS") != null) {
                        paramMap.put("BATCH_PROCESS", null);
                    }
                } else {
                    if (resultMap.get("BEHAVES_LIKE").equals("FIXED")) {
                        if (accFreq > aftProvDiff) {
                            resultMap.put("START", lastProvDt);
                            resultMap.put("END", currentDate);
                        } else {
                            resultMap.put("START", depDt);
                            resultMap.put("END", nextFreqDt);
                        }
                    }
                }
            }
        }

        objInterestBatchTO = interestTaskRun.interestAmount(getHeader(), resultMap);

        interestMap = interestTaskRun.getIntDataMap();
        //__ To reset the IntDataMap in InterestTaskRunner...
        interestTaskRun.setIntDataMap(null);

        interestAmt = objInterestBatchTO.getIntAmt().doubleValue();
        double inter = 0.0;
        if (recurringMap.get("BEHAVES_LIKE").equals("RECURRING")) {
            if (provisionMap != null) {
                interestAmt = interest;
                objInterestBatchTO.setIntAmt(new Double(interestAmt));
                inter = objInterestBatchTO.getIntAmt().doubleValue();
                Date lastApplDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(provisionMap.get("LAST_APPL_DT")));
                int freq = CommonUtil.convertObjToInt(provisionMap.get("INT_APPL_FREQ"));
                Date nextApplDate = DateUtil.addDays(lastApplDt, freq);
                if (DateUtil.dateDiff((Date) lastApplDt, (Date) nextApplDate) > 0) {
                    if (inter == 0) {
                        HashMap getInterestAmtMap = new HashMap();
                        getInterestAmtMap.put("ACT_NUM", resultMap.get("ACT_NUM"));
                        List lst = sqlMap.executeQueryForList("getIntForDeptIntTable", getInterestAmtMap);
                        double cummlativeAmt = 0.0;
                        for (int i = 0; i < lst.size(); i++) {
                            getInterestAmtMap = (HashMap) lst.get(i);
                            double intAmt = CommonUtil.convertObjToDouble(getInterestAmtMap.get("INT_AMT")).doubleValue();
                            cummlativeAmt = cummlativeAmt + intAmt;
                        }
                        interestAmt = cummlativeAmt;
                    } else {
                        interestAmt = interest;
                        objInterestBatchTO.setIntAmt(new Double(interestAmt));
                    }
                }
            }
        }
        if (recurringMap.get("BEHAVES_LIKE").equals("CUMMULATIVE")) {
            interestAmt = interest;
            objInterestBatchTO.setIntAmt(new Double(interestAmt));
        }
        if (DateUtil.dateDiff((Date) recurringMap.get("MATURITY_DT"), currentDate) == 0) {
            String depositNO = CommonUtil.convertObjToStr(recurringMap.get("ACT_NUM"));
            if (depositNO.lastIndexOf("_") != -1) {
                depositNO = depositNO.substring(0, depositNO.lastIndexOf("_"));
                HashMap getRemainAmtMap = new HashMap();
                getRemainAmtMap.put("DEPOSIT_NO", depositNO);
                List lst = sqlMap.executeQueryForList("getRemainAmtForDeposit", getRemainAmtMap);
                if (lst.size() > 0) {
                    getRemainAmtMap = (HashMap) lst.get(0);
                    double matAmt = CommonUtil.convertObjToDouble(getRemainAmtMap.get("MATURITY_AMT")).doubleValue();
                    double avlBal = CommonUtil.convertObjToDouble(getRemainAmtMap.get("AVAILABLE_BALANCE")).doubleValue();
                    interestAmt = matAmt - avlBal;
                }
            }
        }
        interestAmt = (double) getNearest((long) (interestAmt * 100), 100) / 100;
        //        if(paramMap.get("BATCH_PROCESS") != null)
        //__ To Insert the Data in the Transfer and Deposit Interest ...
        if ((taskSelected == BATCHPROCESS || taskSelected == INSERTDATA) && interestAmt > 0) {

            int penal = interestTaskRun.getPENAL();
            if (penal != 1) {
                ArrayList batchList = new ArrayList();
                TransferTrans transferTrans = new TransferTrans();
                transferTrans.setInitiatedBranch(actBranch);
                paramMap.put("BEHAVES_LIKE", recurringMap.get("BEHAVES_LIKE"));
                resultMap = prepareMap(paramMap, resultMap);
                if (paramMap.get("BEHAVES_LIKE").equals("RECURRING") || paramMap.get("BEHAVES_LIKE").equals("CUMMULATIVE")) {
                    Date lastApplDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(provisionMap.get("LAST_APPL_DT")));
                    int freq = CommonUtil.convertObjToInt(provisionMap.get("INT_APPL_FREQ"));
                    Date nextApplDate = DateUtil.addDays(lastApplDt, freq);
                    if (DateUtil.dateDiff((Date) nextApplDate, currentDate) == 0) {
                        if (inter != 0) {
                            if (DateUtil.dateDiff((Date) lastApplDt, (Date) nextApplDate) > 0) {
                                double intAmt = 0.0;
                                double cummlativeAmt = 0.0;
                                HashMap getInterestAmtMap = new HashMap();
                                getInterestAmtMap.put("ACT_NUM", resultMap.get("ACT_NUM"));
                                List lst = sqlMap.executeQueryForList("getIntForDeptIntTable", getInterestAmtMap);
                                for (int i = 0; i < lst.size(); i++) {
                                    getInterestAmtMap = (HashMap) lst.get(i);
                                    intAmt = CommonUtil.convertObjToDouble(getInterestAmtMap.get("INT_AMT")).doubleValue();
                                    cummlativeAmt = cummlativeAmt + intAmt;
                                }
                                interestAmt = cummlativeAmt;
                                HashMap yearInterestMap = new HashMap();
                                yearInterestMap.put("PAID_INT", "Y");
                                yearInterestMap.put("INT_PAID_DATE", currentDate);
                                yearInterestMap.put("ACT_NUM", resultMap.get("ACT_NUM"));
                                sqlMap.executeUpdate("updatePaidInterestAmount", yearInterestMap);
                            }
                        }
                    }
                }
                resultMap.put("LINK_BATCH_ID", recurringMap.get("ACT_NUM"));
                batchList.add(transferTrans.getDebitTransferTO(resultMap, interestAmt));
                batchList.add(transferTrans.getCreditTransferTO(resultMap, interestAmt));
                transferTrans.doDebitCredit(batchList, actBranch);

                objInterestBatchTO.setProductId(CommonUtil.convertObjToStr(resultMap.get("PROD_ID")));
                objInterestBatchTO.setApplDt(currentDate);
                Date currDt = (Date) currentDate.clone();
                Date stDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("START")));
                if (stDt.getDate() > 0) {
                    currDt.setDate(stDt.getDate());
                    currDt.setMonth(stDt.getMonth());
                    currDt.setYear(stDt.getYear());
                }
                //                objInterestBatchTO.setIntDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(resultMap.get("START"))));
                objInterestBatchTO.setIntDt(currDt);

                sqlMap.executeUpdate("insertDepositInterestTO", objInterestBatchTO);
                sqlMap.executeUpdate("updateLastInterestAmount", objInterestBatchTO);
                if (paramMap.get("TASK").equals("APPLICATION") && recurringMap.get("BEHAVES_LIKE").equals("FIXED")) {
                    sqlMap.executeUpdate("updateDepositIntLastApplDT", objInterestBatchTO);
                }

                if (DateUtil.dateDiff((Date) paramMap.get("CUMM_APPLDATE"), currentDate) == 0) {                   
                    sqlMap.executeUpdate("updateDepositIntLastApplDT", objInterestBatchTO);
                }
                objLogTO.setData(objInterestBatchTO.toString());
                objLogTO.setPrimaryKey(objInterestBatchTO.getKeyData());
                objLogDAO.addToLog(objLogTO);
            }
        }
        return objInterestBatchTO;
    }
  public void interestApplictionDeleteInsert(InterestBatchTO objInterestBatchTO, boolean flag) {
        try {
            HashMap finMap = new HashMap();
            finMap.put("DEPOSIT_NO", objInterestBatchTO.getActNum());
            if (flag) {
                sqlMap.executeUpdate("deleteDepositIntTrialTO", finMap);
            } else {
                sqlMap.executeUpdate("insertDepositInterestTrialTO", objInterestBatchTO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //__ To get the Data regarding the product...
    private ArrayList getProductList(HashMap nonBatch) throws Exception {
        ArrayList productList = new ArrayList();

        if (nonBatch.containsKey(InterestTaskRunner.PRODUCT_ID)) {
            productList = (ArrayList) sqlMap.executeQueryForList("Deposit.getProducts", nonBatch);
        } //        else if(CommonUtil.convertObjToStr(nonBatch.get("BEHAVES_LIKE")).equals("FIXED"))
        //            productList = (ArrayList)sqlMap.executeQueryForList("Deposit.getProducts", nonBatch);
        else if (nonBatch.containsKey("CUMINTAPP")) {
            productList = (ArrayList) sqlMap.executeQueryForList("Deposit.getProducts", nonBatch);
        } else {
            productList = (ArrayList) sqlMap.executeQueryForList("Deposit.getProducts", null);
        }

        return productList;
    }

    //__ To get the Data regarding the Account Number...
    private ArrayList getAccountList(HashMap nonBatch) throws Exception {
        ArrayList accountList = new ArrayList();
        HashMap batchMap = new HashMap();
        batchMap = (HashMap) nonBatch.get("PARAMMAP");
        //        if (!CommonUtil.convertObjToStr(dayEndType).equals("BANK_LEVEL"))

        if (!nonBatch.containsKey("DEPOSIT_MANUAL")) {
            nonBatch.put(CommonConstants.BRANCH_ID, actBranch);
        }
        if (paramMap.containsKey("ACT_FROM")) {
            nonBatch.put("ACT_FROM", paramMap.get("ACT_FROM"));
        }
        if (paramMap.containsKey("ACT_TO")) {
            nonBatch.put("ACT_TO", paramMap.get("ACT_TO"));
        }
        if (paramMap.containsKey("INT_PAY_ACC_NO")) {
            nonBatch.put("INT_PAY_ACC_NO", paramMap.get("INT_PAY_ACC_NO"));
        }
        if (paramMap.containsKey("INT_PAY_PROD_ID")) {
            nonBatch.put("INT_PAY_PROD_ID", paramMap.get("INT_PAY_PROD_ID"));
        }
        if (paramMap.containsKey("ACT_NO_LIST")) {
            nonBatch.put("ACT_NO_LIST", paramMap.get("ACT_NO_LIST"));
        }
        if (paramMap.containsKey("EXC_MATURED")) {
            nonBatch.put("EXC_MATURED", "EXC_MATURED");
        }
        if (paramMap.containsKey("EXC_LIEN")) {
            nonBatch.put("EXC_LIEN", "EXC_LIEN");
        }
        //For each product, get the account no
        // If running for a single Ac/No, take Ac/No from Map
        if (nonBatch.containsKey(CommonConstants.ACT_NUM)) {
            accountList.add(nonBatch);
        } else {
            nonBatch.put("CURR_DATE", currentDate);
            //COMMENTED BY RISHAD 16/07/2015 not required in kerala
//            if (batchMap.containsValue("PROVISIONING")) {
//                accountList = (ArrayList) sqlMap.executeQueryForList("Deposit.getAccounts", nonBatch);
//            }
            if (batchMap.containsValue("APPLICATION")) {
                if (paramMap.containsKey("INTPAY_MODE")) {
                    nonBatch.put("INTPAY_MODE", CommonUtil.convertObjToStr(paramMap.get("INTPAY_MODE")));
                }
                // accountList = (ArrayList) sqlMap.executeQueryForList("Deposit.getApplication", nonBatch);
                if(!paramMap.containsKey("SPECIAL_RD_SCHEME")){
                    nonBatch.put("NORMAL_DEP_INT_APPL","NORMAL_DEP_INT_APPL");
                }   
                if(ServerUtil.getCbmsParameterMap().containsKey("DEPO_INT_APPLN_ACT_CNT") && ServerUtil.getCbmsParameterMap().get("DEPO_INT_APPLN_ACT_CNT") != null && CommonUtil.convertObjToLong(ServerUtil.getCbmsParameterMap().get("DEPO_INT_APPLN_ACT_CNT")) > 0){
                   nonBatch.put("INT_APPLN_PROCESS_CNT", CommonUtil.convertObjToLong(ServerUtil.getCbmsParameterMap().get("DEPO_INT_APPLN_ACT_CNT"))); 
                }
                accountList = (ArrayList) sqlMap.executeQueryForList("Deposit.getApplicationDCCB", nonBatch);
            }
        }
        return accountList;
    }

    //__ Common Method Call...
    private HashMap implementTask(HashMap nonBatch, boolean isExceptionCatch) throws Exception {
        HashMap provisionMap = (HashMap) paramMap.get("PROVISIONMAP");
        InterestBatchTO objInterestBatchTO = null;
        HashMap dataMap = new HashMap();
        ArrayList accountList = new ArrayList();
        ArrayList dateList = new ArrayList();
        ArrayList closingDateList = new ArrayList();

        // Log DAO
        LogDAO objLogDAO = new LogDAO();

        // Log Transfer Object
        LogTO objLogTO = new LogTO();
        objLogTO.setUserId(getHeader().getUserID());
        objLogTO.setBranchId(getHeader().getBranchID());
        objLogTO.setIpAddr(getHeader().getIpAddr());
        objLogTO.setModule(MODULE);
        objLogTO.setScreen(SCREEN);

        //__ Get All the Acount Nos...
        if (taskSelected == OBCODE || taskSelected == INSERTDATA) {
            accountList = (ArrayList) nonBatch.get(CommonConstants.ACT_NUM);
            //__ if the Closing Date is Present...
            if (nonBatch.containsKey("CLOSING_DT")) {
                nonBatch.put("CLOSING_DT", nonBatch.get("CLOSING_DT"));
                //                closingDateList = (ArrayList)nonBatch.get("CLOSING_DT");
            }
        } else {
            accountList = getAccountList(nonBatch);

        }
        int accountListSize = accountList.size();
        for (int acc = 0; acc < accountListSize; acc++) {
            try {
                if (isTransaction) {
                    //sqlMap.startTransaction();
                }

                if (taskSelected == OBCODE || taskSelected == INSERTDATA) {
                    paramMap.put("ACT_NUM", accountList.get(acc));
           

                } else {
                    paramMap.put("ACT_NUM", ((HashMap) accountList.get(acc)).get("ACT_NUM")); //Interest is calculated for this Ac No
                  
                }
                paramMap.put("TODAY_DT", currentDate);
                String batchProcess = CommonUtil.convertObjToStr(paramMap.get("BATCH_PROCESS"));
                List behavesList = (List) sqlMap.executeQueryForList("getBehavesLikeForDeposit", paramMap);
                if (behavesList.size() > 0) {
                    recurringMap = new HashMap();
                    HashMap behavesMap = (HashMap) behavesList.get(0);
                    ArrayList resultList = new ArrayList();
                    String behaves = (String) behavesMap.get("BEHAVES_LIKE");
                    paramMap.put("BRANCH_ID", getHeader().getBranchID());
                    String depositNo = CommonUtil.convertObjToStr(paramMap.get("ACT_NUM"));
                    depositNo = depositNo.substring(0, depositNo.lastIndexOf("_"));
                    HashMap depositNoMap = new HashMap();
                    depositNoMap.put("DEPOSIT_NO", depositNo);

                    List lst = (List) sqlMap.executeQueryForList("getDepositDate", depositNoMap);
                    HashMap dateCalc = (HashMap) lst.get(0);
                    Date depositDate = (Date) dateCalc.get("DEPOSIT_DT");
                    Date maturityDate = (Date) dateCalc.get("MATURITY_DT");
                    long differencePeriod = DateUtil.dateDiff(depositDate, maturityDate);
                    if (paramMap.get("TASK") == null) {
                        paramMap.put("CLOSING_DT", nonBatch.get("CLOSING_DT"));
                        resultList = (ArrayList) sqlMap.executeQueryForList("Deposit.InterestDataForDeposits", paramMap);
                        if (resultList.size() > 0) {
                            recurringMap = (HashMap) resultList.get(0);
                            dataMap.put("RESULTLIST", resultList);
                            dataMap.put("NONBATCH", nonBatch);
                            dataMap.put("PARAMMAP", paramMap);
                            dataMap.put("OBJLOGDAO", objLogDAO);
                            dataMap.put("OBJLOGTO", objLogTO);
                            dataMap.put("RECURRINGMAP", recurringMap);
                            dataMap.put("ACC", new Integer(acc));
                            dataMap.put("OBJINTERESTBATCHTO", objInterestBatchTO);
                            dataMap.put("CLOSINGDATELIST", closingDateList);
                            dataMap = calculateResultList(dataMap);
                        }
                    } else {
                        if (!paramMap.get("TASK").equals("PROVISIONING")) {
                         
                            if (paramMap.get("CHARGES_PROCESS") != null) {
                                String subNo = CommonUtil.convertObjToStr(dateCalc.get("DEPOSIT_SUB_NO"));
                                String depFrom = CommonUtil.convertObjToStr(paramMap.get("ACT_FROM"));
                                String depTo = CommonUtil.convertObjToStr(paramMap.get("ACT_TO"));
                                String depNoSubNoFrom = depFrom + "_" + subNo;
                                String depNoSubNoTo = depTo + "_" + subNo;
                                paramMap.put("DIFFPERIOD", new Long(differencePeriod));
                                paramMap.put("ACT_FROM", depNoSubNoFrom);
                                paramMap.put("ACT_TO", depNoSubNoTo);
                            
                            }
                        }
                        resultList = (ArrayList) sqlMap.executeQueryForList("InterestDataForDeposits", paramMap);   
                        if (resultList.size() > 0) { // Provisioning Date functioning......
                            recurringMap = (HashMap) resultList.get(0);
                            Date depDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(recurringMap.get("DEPOSIT_DT")));
                            int accFreq = CommonUtil.convertObjToInt(recurringMap.get("ACC_FREQ"));
                            Date nextCalc = DateUtil.addDays(depDt, accFreq);
                            if (paramMap.get("TASK").equals("PROVISIONING")) {
                                if (paramMap.get("CHARGES_PROCESS") != null) {
                                    paramMap.put("BATCH_PROCESS", null);
                                }
                                if (behaves.equals("RECURRING") || behaves.equals("CUMMULATIVE")) { // cummulative deposits......
                                   
                                    if (DateUtil.dateDiff((Date) paramMap.get("NEXTPROVDT"), (Date) paramMap.get("TODAY_DT")) == 0) {
                                        dataMap.put("RESULTLIST", resultList);
                                        dataMap.put("NONBATCH", nonBatch);
                                        dataMap.put("PARAMMAP", paramMap);
                                        dataMap.put("OBJLOGDAO", objLogDAO);
                                        dataMap.put("OBJLOGTO", objLogTO);
                                        dataMap.put("ACC", new Integer(acc));
                                        dataMap.put("RECURRINGMAP", recurringMap);
                                        dataMap.put("PROVISIONMAP", provisionMap);
                                        dataMap.put("OBJINTERESTBATCHTO", objInterestBatchTO);
                                        dataMap.put("CLOSINGDATELIST", closingDateList);
                                        dataMap = calculateResultList(dataMap);
                                    }
                                }
                                if (behaves.equals("FIXED")) {
                                    recurringMap = (HashMap) resultList.get(0);
                                    if (DateUtil.dateDiff((Date) paramMap.get("NEXTPROVDT"), (Date) paramMap.get("TODAY_DT")) >= 0) {
                                        dataMap.put("RESULTLIST", resultList);
                                        dataMap.put("NONBATCH", nonBatch);
                                        dataMap.put("PARAMMAP", paramMap);
                                        dataMap.put("OBJLOGDAO", objLogDAO);
                                        dataMap.put("OBJLOGTO", objLogTO);
                                        dataMap.put("ACC", new Integer(acc));
                                        dataMap.put("RECURRINGMAP", recurringMap);
                                        dataMap.put("OBJINTERESTBATCHTO", objInterestBatchTO);
                                        dataMap.put("CLOSINGDATELIST", closingDateList);
                                        dataMap = calculateResultList(dataMap);
                                    }
                                }
                                paramMap.put("BATCH_PROCESS", batchProcess);
                            }
                            if (paramMap.get("TASK").equals("APPLICATION")) { //Application Date applied...
                                recurringMap = (HashMap) resultList.get(0);
                                if (paramMap.get("CHARGES_PROCESS") != null) {
                                    paramMap.put("BATCH_PROCESS", null);
                                }
                                if (DateUtil.dateDiff((Date) nextCalc, (Date) paramMap.get("TODAY_DT")) == 0) {
                                    if (behaves.equals("RECURRING") || behaves.equals("CUMMULATIVE")) { // cummulative deposits......
                                        dataMap.put("RESULTLIST", resultList);
                                        dataMap.put("NONBATCH", nonBatch);
                                        dataMap.put("PARAMMAP", paramMap);
                                        dataMap.put("OBJLOGDAO", objLogDAO);
                                        dataMap.put("OBJLOGTO", objLogTO);
                                        dataMap.put("ACC", new Integer(acc));
                                        dataMap.put("RECURRINGMAP", recurringMap);
                                        dataMap.put("OBJINTERESTBATCHTO", objInterestBatchTO);
                                        dataMap.put("CLOSINGDATELIST", closingDateList);
                                        dataMap = calculateResultList(dataMap);
                                    }

                                }
                                if (behaves.equals("FIXED")) { // cummulative deposits......
                                    if (DateUtil.dateDiff((Date) nextCalc, (Date) paramMap.get("TODAY_DT")) >= 0) {
                                        dataMap.put("RESULTLIST", resultList);
                                        dataMap.put("NONBATCH", nonBatch);
                                        dataMap.put("PARAMMAP", paramMap);
                                        dataMap.put("OBJLOGDAO", objLogDAO);
                                        dataMap.put("OBJLOGTO", objLogTO);
                                        dataMap.put("ACC", new Integer(acc));
                                        dataMap.put("RECURRINGMAP", recurringMap);
                                        dataMap.put("OBJINTERESTBATCHTO", objInterestBatchTO);
                                        dataMap.put("CLOSINGDATELIST", closingDateList);
                                        dataMap = calculateResultList(dataMap);
                                    }
                                }
                                paramMap.put("BATCH_PROCESS", batchProcess);
                            }
                        }
                    }
                }
                if (isTransaction) {
                    //sqlMap.commitTransaction();
                }
                //                status.setStatus(BatchConstants.COMPLETED);
            } catch (Exception e) {
                if (isTransaction) {
                    //sqlMap.rollbackTransaction();
                }

                e.printStackTrace();

                if (!isExceptionCatch) {
                    throw new TransRollbackException(e);
                } else {
                    status.setStatus(BatchConstants.ERROR);
                }
            }
        }
        if (paramMap.get("TASK") == null) {
        } else {
            if (paramMap.get("BATCH_PROCESS") != null) {
                HashMap calculationMap = new HashMap();
                calculationMap.put("PROD_TYPE", "TD");
                calculationMap.put("PROD_ID", paramMap.get("PROD_ID"));
                calculationMap.put("LAST_PROV_DT", currentDate);
                calculationMap.put("LAST_APPL_DT", currentDate);
                calculationMap.put("STATUS_BY", getHeader().getUserID());
                calculationMap.put("BRANCH_CODE", getHeader().getBranchID());
                List list = (List) sqlMap.executeQueryForList("getProvisionForDeposit", calculationMap);
                String prodId = null;
                if (paramMap.get("TASK").equals("PROVISIONING")) {
                    if (list.size() > 0) {
                        sqlMap.executeUpdate("updateDepositProvisionApplDT", calculationMap);
                    } else {
                        sqlMap.executeUpdate("insertDepositProvisionApplDT", calculationMap);
                    }
                } else {
                    if (DateUtil.dateDiff((Date) paramMap.get("CUMM_APPLDATE"), currentDate) == 0) {
                        if (list.size() > 0) {
                            sqlMap.executeUpdate("updateDepositApplicationApplDT", calculationMap);
                        } else {
                            sqlMap.executeUpdate("insertDepositProvisionApplDT", calculationMap);
                        }
                    }
                }
            }
        }
        return dataMap;
    }

    private HashMap calculateResultList(HashMap dataMap) throws Exception {
        ArrayList resultList = (ArrayList) dataMap.get("RESULTLIST");
        HashMap nonBatch = (HashMap) dataMap.get("NONBATCH");
        HashMap paramMap = (HashMap) dataMap.get("PARAMMAP");
        LogDAO objLogDAO = (LogDAO) dataMap.get("OBJLOGDAO");
        LogTO objLogTO = (LogTO) dataMap.get("OBJLOGTO");
        HashMap recurringMap = (HashMap) dataMap.get("RECURRINGMAP");
        HashMap provisionMap = (HashMap) paramMap.get("PROVISIONMAP");
        int acc = CommonUtil.convertObjToInt(dataMap.get("ACC"));
        paramMap.put("RECURRINGMAP", recurringMap);
        InterestBatchTO objInterestBatchTO = (InterestBatchTO) dataMap.get("OBJINTERESTBATCHTO");
        ArrayList closingDateList = (ArrayList) dataMap.get("CLOSINGDATELIST");
     
        for (int i = 0, size = resultList.size(); i < size; i++) {
            //run the interest calculator for each account...
            HashMap resultMap = (HashMap) resultList.get(i);

            if (nonBatch.containsKey("MODE")) {
                if (nonBatch.get("MODE").equals(CommonConstants.TOSTATUS_RENEW)) {
                    resultMap.put("START", resultMap.get("DEPOSIT_DT"));
                }
            }

            if (paramMap.get("TASK") == null) {
             
                if (taskSelected == OBCODE || taskSelected == INSERTDATA) {
                    //                }else{
                    //__ If the Maturity Date is Greater than Current Date...
                    if (DateUtil.dateDiff((Date) resultMap.get("MATURITY_DT"), currentDate) < 0) {
                        resultMap.put("END", currentDate);
                    } else {
                        resultMap.put("END", (Date) resultMap.get("MATURITY_DT"));
                    }
                    //__ If the Account is to be Closed...
                    if (paramMap.containsKey("CLOSING_DT")) {
                        resultMap.put("CLOSING_DT", paramMap.get("CLOSING_DT"));
                    }
                    if (closingDateList != null && closingDateList.size() > 0) {
                        resultMap.put("CLOSING_DT", paramMap.get("CLOSING_DT"));
                        //                        resultMap.put("CLOSING_DT", (Date)closingDateList.get(acc));
                    }
                    //                }else{    //__ Else, in case of the Execute Task, use Current Date as End Date...
                    resultMap.put("END", currentDate);
                    //                }
                }
                resultMap.put("START", recurringMap.get("DEPOSIT_DT"));
                resultMap.put("END", currentDate);
            } else {
                if (paramMap.get("BATCH_PROCESS") != null) {
                    if (paramMap.get("TASK").equals("PROVISIONING")) {
                        if (DateUtil.dateDiff((Date) resultMap.get("DEPOSIT_DT"), currentDate) > 0) {
                            resultMap.put("START", resultMap.get("DEPOSIT_DT"));
                            resultMap.put("END", currentDate);
                        }
                    } else {
                        if (DateUtil.dateDiff((Date) resultMap.get("DEPOSIT_DT"), currentDate) > 0) {
                            resultMap.put("START", resultMap.get("DEPOSIT_DT"));
                            resultMap.put("END", currentDate);
                        }
                    }
                }
            }
            objInterestBatchTO = runBatch(resultMap, paramMap, objLogDAO, objLogTO);
            dataMap = new HashMap();
            dataMap.put(CommonUtil.convertObjToStr(objInterestBatchTO.getActNum()), interestMap);
            dataMap.put("OBJINTERESTBATCHTO", objInterestBatchTO);
        }
        return dataMap;
    }
 //added by rishad 02/04/2014 for leap year calculation
    public boolean findLeapyear(int year) {
        boolean flag = false;
        if (year % 100 == 0) {
            if (year % 400 == 0) {

                flag = true;

            } else {

                flag = false;
            }

        } else {

            if (year % 4 == 0) {

                flag = true;
            } else {
                flag = false;
            }
        }
        return flag;
    }
//end function find leapyear
    //To be get the nearest interest amount.....
    public long getNearest(long number, long roundingFactor) {
        long roundingFactorOdd = roundingFactor;
        if ((roundingFactor % 2) != 0) {
            roundingFactorOdd += 1;
        }
        long mod = number % roundingFactor;
//        if ((mod <= (roundingFactor/2)) || (mod <= (roundingFactorOdd/2)))
        // The following change made by Rajesh for Kerala banks.  For Rs.1370.5 rounded amt should be 1371 not 1370
        if ((mod < (roundingFactor / 2)) || (mod < (roundingFactorOdd / 2))) {
            return lower(number, roundingFactor);
        } else {
            return higher(number, roundingFactor);
        }
    }

    public long lower(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        return number - mod;
    }

    public long higher(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        if (mod == 0) {
            return number;
        }
        return (number - mod) + roundingFactor;
    }

    //__ To be Called for the Batch Process...
    public TaskStatus executeTask() throws Exception {
        status = new TaskStatus();
        try {
         
            HashMap nonBatch = new HashMap();
            //__ The selected task is being performed as a Batch Process...
            taskSelected = BATCHPROCESS;

            //__ get ProductData...
            if (getHeader().getTaskParam().containsKey(CommonConstants.PRODUCT_ID)) {
                nonBatch.put(InterestTaskRunner.PRODUCT_ID, getHeader().getTaskParam().get(CommonConstants.PRODUCT_ID));
            }
            //__ getAccount Data
            if (getHeader().getTaskParam().containsKey(CommonConstants.ACT_NUM)) {
                nonBatch.put(CommonConstants.ACT_NUM, getHeader().getTaskParam().get(CommonConstants.ACT_NUM));
            }

            status.setStatus(BatchConstants.STARTED);
           // System.out.println("#$%#$^#$^#^#^$#^^#^ paramMap ***"+paramMap);    
            if (paramMap.containsKey("PROCESS") && CommonUtil.convertObjToStr(paramMap.get("PROCESS")).equals(CommonConstants.DAY_END)) {
                dayendprocess(nonBatch);
                creditInttonoFixed(nonBatch);
            } else if (paramMap.containsKey("PROCESS") && CommonUtil.convertObjToStr(paramMap.get("PROCESS")).equals("CUMTOTAL")) {

                performCumulativeInterestBeforeLive();
            } else if (paramMap.containsKey("PROCESS") && CommonUtil.convertObjToStr(paramMap.get("PROCESS")).equals(CommonConstants.DAY_BEGIN)) {
                ArrayList lstBhvsLike = (ArrayList) sqlMap.executeQueryForList("getBehavesLike", nonBatch);
                if (lstBhvsLike != null && lstBhvsLike.size() > 0) {
                    HashMap mapBhvsLike = (HashMap) lstBhvsLike.get(0);
                    bhvsLike = CommonUtil.convertObjToStr(mapBhvsLike.get("BEHAVES_LIKE"));
                }

                nonBatch.put("BEHAVES_LIKE", bhvsLike);
                nonBatch.put("APPLICATION", "APPLICATION");
                InterestBatchTO objInterestBatchTO = null;
                ArrayList productList = (ArrayList) sqlMap.executeQueryForList("Deposit.getProducts", nonBatch);
                if (productList != null && productList.size() > 0) {
                    int prodListSize = productList.size();
                    for (int prod = 0; prod < prodListSize; prod++) {
                        paramMap.put(InterestTaskRunner.AC_HD_ID, ((HashMap) productList.get(prod)).get("ACCT_HEAD")); //Ac Head

                        paramMap.put(InterestTaskRunner.DEBIT_INT, ((HashMap) productList.get(prod)).get("INT_PROV_ACHD")); //Debit Int Head
                        paramMap.put(InterestTaskRunner.CREDIT_INT, ((HashMap) productList.get(prod)).get("INT_PAY")); //Credit Int Head
                        paramMap.put(InterestTaskRunner.PRODUCT_ID, ((HashMap) productList.get(prod)).get("PROD_ID")); //Product Id
                        paramMap.put("ROUND_OFF", ((HashMap) productList.get(prod)).get("INT_ROUNDOFF_TERMS")); //Product Id
                        paramMap.put(InterestTaskRunner.LAST_APPL_DT, ((HashMap) productList.get(prod)).get("LAST_INT_APPL_DT")); //Last Interest Applied Dt Payale

                        paramMap.put("interestType", ((HashMap) productList.get(prod)).get("INT_TYPE"));

                        paramMap.put(InterestTaskRunner.APPL_NEW_DT, null); //Updated date based on FREQ_A from InterestTaskRunner


                        nonBatch.put(InterestTaskRunner.PRODUCT_ID, ((HashMap) productList.get(prod)).get("PROD_ID")); //Product Id)
                        paramMap.put("BATCH_PROCESS", "BATCH_PROCESS");
                        paramMap.put("PROD_TYPE", "TD");
                        paramMap.put("TASK", "APPLICATION");
                        paramMap.put("DISCOUNTED_RATE", ((HashMap) productList.get(prod)).get("DISCOUNTED_RATE"));
                        List accountList = null;
                        if (branchList != null && branchList.size() > 0) {
                            for (int b = 0; b < branchList.size(); b++) {
                                HashMap branchMap = (HashMap) branchList.get(b);
                                actBranch = CommonUtil.convertObjToStr(branchMap.get(CommonConstants.BRANCH_ID));
                                currentDate = ServerUtil.getCurrentDate(actBranch);
                                String compStatus = "";
                                HashMap compMap = new HashMap();
                                List compLst = null;
                                if (!paramMap.containsKey("CHARGES_PROCESS")) {

                                    compMap.put("TASK_NAME", taskLable);
                                    compMap.put("DAYEND_DT", currentDate);
                                    compMap.put("BRANCH_ID", actBranch);
                                    compLst = (List) sqlMap.executeQueryForList("getSelectTaskLstDayBegin", compMap);

                                    compMap = null;
                                    if (compLst != null && compLst.size() > 0) {
                                        compMap = (HashMap) compLst.get(0);
                                        compStatus = CommonUtil.convertObjToStr(compMap.get("TASK_STATUS"));
                                        compMap = null;
                                    }
                                } else {
                                    compStatus = "ERROR";
                                    compMap.put("TASK_NAME", "A");
                                    compMap.put("DAYEND_DT", currentDate);
                                    compMap.put("BRANCH_ID", actBranch);
                                    compLst = (List) sqlMap.executeQueryForList("getSelectTaskLstDayBegin", compMap);
                                }
                                if (compLst.size() <= 0 || compStatus.equals("ERROR")) {
                                    HashMap errorMap = new HashMap();
                                    if (!paramMap.containsKey("CHARGES_PROCESS")) {
                                        //sqlMap.startTransaction();
                                        errorMap.put("ERROR_DATE", currentDate);
                                        errorMap.put("TASK_NAME", taskLable);
                                        errorMap.put("BRANCH_ID", actBranch);
                                        sqlMap.executeUpdate("deleteError_showing", errorMap);
                                       // sqlMap.commitTransaction();
                                    }
                                    paramMap.put(CommonConstants.BRANCH_ID, actBranch);
                                    nonBatch.put("PARAMMAP", paramMap);

                                    accountList = getAccountList(nonBatch);
                                    int accountListSize = accountList.size();
                                    for (int acc = 0; acc < accountListSize; acc++) {
                                        try {
                                            if (isTransaction) {
                                               // sqlMap.startTransaction();
                                            }
                                            paramMap.put("ACT_NUM", ((HashMap) accountList.get(acc)).get("ACT_NUM"));
                                            paramMap.put("CUST_ID", ((HashMap) accountList.get(acc)).get("CUST_ID"));
                                            paramMap.put("PROD_TYPE", "TD");
                                            paramMap.put(CommonConstants.BRANCH_ID, actBranch);
                                            paramMap.put("CUR_DT", currentDate);
                                            double interestAmt = 0.0;
                                            double intTrfAmt = 0.0;
                                            paramMap.put("CURR_DATE", currentDate);
                
                                            List lstSlab = sqlMap.executeQueryForList("getIntAppSlab", paramMap);
                                            if (lstSlab.size() > 0 && lstSlab != null) {
                                                HashMap mapSlab = new HashMap();
                                                mapSlab = (HashMap) lstSlab.get(0);
                                                slab = CommonUtil.convertObjToStr(mapSlab.get("INT_APP_SLAB"));
                                            }
                                            System.out.println("00002");

                                            List acctDtl = sqlMap.executeQueryForList("getDepoitDetailForIntApp", paramMap);
                                            if (acctDtl != null && acctDtl.size() > 0) {
                                                HashMap acctDtlMap = new HashMap();
                                                acctDtlMap = (HashMap) acctDtl.get(0);
                                                paramMap.put("TODAY_DT", currentDate);
                                                Date lstproDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(acctDtlMap.get("LAST_PROV_DT")));
                                                Date lstIntCrDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(acctDtlMap.get("LAST_INT_APPL_DT")));
                                                Date depDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(acctDtlMap.get("DEPOSIT_DT")));
                                                Date depMatDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(acctDtlMap.get("MATURITY_DT")));
                                                Date nextCalsDt = DateUtil.addDays(lstIntCrDt, CommonUtil.convertObjToInt(acctDtlMap.get("FREQ")));
                                                double Tot_Int_Cr = CommonUtil.convertObjToDouble(acctDtlMap.get("TOTAL_INT_CREDIT")).doubleValue();
                                                double Tot_Int_Dr = CommonUtil.convertObjToDouble(acctDtlMap.get("TOTAL_INT_DRAWN")).doubleValue();
                                                double Tot_Int_AMT = CommonUtil.convertObjToDouble(acctDtlMap.get("TOT_INT_AMT")).doubleValue();
                                                long period = DateUtil.dateDiff(depDt, depMatDt);
                                                nextCalsDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(acctDtlMap.get("NEXT_INT_APPL_DT")));
               
                                                paramMap.put("DEPOSIT_DT", depDt);
                                                paramMap.put("MATURITY_DT", depMatDt);
                                                paramMap.put("AMOUNT", acctDtlMap.get("DEPOSIT_AMT"));
                                                paramMap.put("CATEGORY_ID", acctDtlMap.get("CATEGORY"));
                                                paramMap.put("BEHAVES_LIKE", bhvsLike);
                                                paramMap.put("PERIOD1", new Double(period));
                                                paramMap.put("END", nextCalsDt);
                                                paramMap.put("FREQ", acctDtlMap.get("FREQ"));
                                            
                                                if (DateUtil.dateDiff(lstIntCrDt, lstproDt) >= 0) {
                                                    paramMap.put("START", lstproDt);
                                                } else {
                                                    paramMap.put("START", lstIntCrDt);
                                                }

                                                if (DateUtil.dateDiff(nextCalsDt, depMatDt) > 0) {
                                                    paramMap.put("END", nextCalsDt);
                                                } else {
                                                    paramMap.put("END", depMatDt);
                                                }
                                                int fre = CommonUtil.convertObjToInt(paramMap.get("INT_PROV_FREQ"));
                                                Date diffDt = DateUtil.addDays(lstproDt, -fre);
                                                //                                    if(DateUtil.dateDiff(diffDt,lstIntCrDt)>=0){
                                                //aadded by jiby
                                    
                                                if (slab.equals("Y")) {
                                                    int diffMonth = (int) monthDiff(nextCalsDt, depDt);
                                                    HashMap mapROI = new HashMap();
                                                    mapROI.put("DIFF_MONTH", (diffMonth * 30));
                                                    mapROI.put("PROD_ID", nonBatch.get("PROD_ID"));
                                                    List lstROI = sqlMap.executeQueryForList("getSelROIForSlab", mapROI);
                                                    if (lstROI != null && lstROI.size() > 0) {
                                                        mapROI = new HashMap();
                                                        mapROI = (HashMap) lstROI.get(0);

                                                        rateOfInt = CommonUtil.convertObjToDouble(mapROI.get("ROI")).doubleValue();
                                                        //roi = rateOfInt;
                                                    } else {
                                                        throw new TTException("Enter Interest Maintenance Details");
                                                    }
                                                }
                                                ////
                                                if (acctDtlMap.containsKey("FREQ") && CommonUtil.convertObjToInt(acctDtlMap.get("FREQ")) == 30 && CommonUtil.convertObjToStr(paramMap.get("DISCOUNTED_RATE")).equals("Y")) {
                                                    double period1 = 0.0;
                                                    long pr = DateUtil.dateDiff(lstIntCrDt, nextCalsDt);
                                                    double roi = 0.0;
                                                    double amount = CommonUtil.convertObjToDouble(acctDtlMap.get("DEPOSIT_AMT")).doubleValue();
                                                   
                                                    //added by jiby

                                                    //
                                                    if (slab.equals("Y")) {
                                                        roi = rateOfInt;
                                                    } else {
                                                        roi = CommonUtil.convertObjToDouble(acctDtlMap.get("RATE_OF_INT")).doubleValue();
                                                    }
                                                    if (DateUtil.dateDiff(lstproDt, lstIntCrDt) > 0 && DateUtil.dateDiff(nextCalsDt, depMatDt) >= 0) {
                                                        double interestAmt1 = 0.0;
                                                        double interestAmt2 = 0.0;
                                                        for (lstIntCrDt = DateUtil.addDays(lstIntCrDt, 30); DateUtil.dateDiff(lstIntCrDt, currentDate) >= 0; lstIntCrDt = DateUtil.addDays(lstIntCrDt, 30)) {
                                                            if (DateUtil.dateDiff(nextCalsDt, depMatDt) >= 0) {
                                                                period1 = (double) pr / 30;
                                                                period1 = (double) getNearest((long) (period1 * 100), 100) / 100;
                                                                interestAmt2 = roi / 4 / (Math.pow((1 + (roi / 1200)), 2) + (1 + (roi / 1200)) + 1);
                                                                
                                                                interestAmt2 = interestAmt2 * period1;
                                                                double calcAmt = amount / 100;
                                                                interestAmt2 = interestAmt2 * calcAmt;
                                                                interestAmt2 = (double) getNearest((long) (interestAmt2 * 100), 100) / 100;
                                                                interestAmt1 = interestAmt1 + interestAmt2;
                                                              
                                                                nextCalsDt = lstIntCrDt;

                                                            }
                                                        }
                                                        
                                                        interestAmt = interestAmt1;
                                                        intTrfAmt = interestAmt;
                                                        lstIntCrDt = DateUtil.addDays(lstIntCrDt, -30);


                
                                                        if (Tot_Int_AMT > 0 && DateUtil.dateDiff(depMatDt, nextCalsDt) >= 0) {
                                                            interestAmt = Tot_Int_AMT - Tot_Int_Cr;
                                                            intTrfAmt = interestAmt;
                                                            nextCalsDt = depMatDt;
                                                        }
                                                        paramMap.put("END", nextCalsDt);
                                                       
                                                    } else if (DateUtil.dateDiff(lstproDt, lstIntCrDt) <= 0 && DateUtil.dateDiff(nextCalsDt, depMatDt) >= 0) {

                                                        double DiffAmt = 0.0;
                                                        double calcAmt = 0.0;
                                                        paramMap.put("START", lstIntCrDt);
                                                        if (DateUtil.dateDiff(lstIntCrDt, depDt) != 0 || DateUtil.dateDiff(lstproDt, depDt) != 0) { // for the prpose of renwal after provison reneved
                                                            pr = DateUtil.dateDiff(lstIntCrDt, lstproDt);
                                                            if (DateUtil.dateDiff(lstIntCrDt, depDt) >= 0) {
                                                                //                                                    pr=pr+1;
                                                            }
                                                            pr = pr + 1;// FOR MHCB
                                                            period1 = (double) pr;
                                                            period1 = (double) period1 / 30;
                                                            
                                                            interestAmt = roi / 4 / (Math.pow((1 + (roi / 1200)), 2) + (1 + (roi / 1200)) + 1);
                                                           
                                                            interestAmt = interestAmt * period1;
                                                            calcAmt = amount / 100;
                                                            DiffAmt = interestAmt * calcAmt;
                                                            
                                                            //                                             for the one frequence
                                                            DiffAmt = (double) getNearest((long) (DiffAmt * 100), 100) / 100;
                                                            paramMap.put("START", lstproDt);
                                                        }
                                                        pr = DateUtil.dateDiff(lstIntCrDt, nextCalsDt);
                                                        period1 = (double) pr / 30;
                                                        period1 = (double) getNearest((long) (period1 * 100), 100) / 100;
                                                        interestAmt = roi / 4 / (Math.pow((1 + (roi / 1200)), 2) + (1 + (roi / 1200)) + 1);
                                                       
                                                        interestAmt = interestAmt * period1;
                                                        calcAmt = amount / 100;
                                                        interestAmt = interestAmt * calcAmt;
                                                       
                                                        interestAmt = (double) getNearest((long) (interestAmt * 100), 100) / 100;
                                                        intTrfAmt = interestAmt;
                                                        interestAmt = interestAmt - DiffAmt;

                                                        if (Tot_Int_AMT > 0 && DateUtil.dateDiff(depMatDt, nextCalsDt) >= 0) {
                                                            interestAmt = Tot_Int_AMT - Tot_Int_Cr;
                                                            intTrfAmt = interestAmt + DiffAmt;
                                                            nextCalsDt = depMatDt;
                                                        }
                                                       
                                                        paramMap.put("START", lstproDt);
                                                        paramMap.put("END", nextCalsDt);



                                                        double interestAmt1 = 0.0;
                                                        double interestAmt2 = 0.0;
                                                        Date lstIntCrDt1 = DateUtil.addDays(lstIntCrDt, 30);
                                                        if (DateUtil.dateDiff(lstIntCrDt1, currentDate) >= 0) {
                                                            lstIntCrDt = lstIntCrDt1;
                                                            for (lstIntCrDt = DateUtil.addDays(lstIntCrDt, 30); DateUtil.dateDiff(lstIntCrDt, currentDate) >= 0; lstIntCrDt = DateUtil.addDays(lstIntCrDt, 30)) {
                                                                if (DateUtil.dateDiff(nextCalsDt, depMatDt) >= 0) {
                                                                    
                                                                    period1 = (double) pr / 30;
                                                                    period1 = (double) getNearest((long) (period1 * 100), 100) / 100;
                                                                    interestAmt2 = roi / 4 / (Math.pow((1 + (roi / 1200)), 2) + (1 + (roi / 1200)) + 1);
                                              
                                                                   
                                                                    interestAmt2 = interestAmt2 * period1;
                                                                    calcAmt = 0.0;
                                                                    calcAmt = amount / 100;
                                                                    interestAmt2 = interestAmt2 * calcAmt;
                                                                    interestAmt2 = (double) getNearest((long) (interestAmt2 * 100), 100) / 100;
                                                                    interestAmt1 = interestAmt1 + interestAmt2;
                                    
                                                                    nextCalsDt = lstIntCrDt;

                                                                }
                                                            }
                                                           
                                                            interestAmt = interestAmt + interestAmt1;
                                                            intTrfAmt = interestAmt1 + intTrfAmt;
                                                            lstIntCrDt = DateUtil.addDays(lstIntCrDt, -30);
                                                            
                                                            paramMap.put("END", nextCalsDt);

                                                            
                                                            if (Tot_Int_AMT > 0 && DateUtil.dateDiff(depMatDt, nextCalsDt) >= 0) {
                                                                interestAmt = Tot_Int_AMT - Tot_Int_Cr;
                                                                intTrfAmt = interestAmt + DiffAmt;
                                                            }

                                                        }
                                                        

                                                    } else {
                                                        period1 = DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("START"))), DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("END"))));
                                                        period1 = (double) pr / 30;
                                                        //                                     interestAmt = (double)getNearest((long)(period1 *100),100)/100;
                                                        interestAmt = roi / 4 / (Math.pow((1 + (roi / 1200)), 2) + (1 + (roi / 1200)) + 1);
                                                  
                                                        interestAmt = interestAmt * period1;
                                                        double calcAmt = amount / 100;
                                                        interestAmt = interestAmt * calcAmt;
                                                        intTrfAmt = interestAmt;
                                                    }

                                                    interestAmt = (double) getNearest((long) (interestAmt * 100), 100) / 100;
                                                 
                                                    objInterestBatchTO = new InterestBatchTO();
                                                    objInterestBatchTO.setActNum(CommonUtil.convertObjToStr(paramMap.get("ACT_NUM")));
                                                    //                                         objInterestBatchTO.setAcHdId(CommonUtil.convertObjToStr(paramMap.get("DEBIT_INT")));
                                                    objInterestBatchTO.setAcHdId(CommonUtil.convertObjToStr(paramMap.get("CREDIT_INT")));
                                                    objInterestBatchTO.setIntAmt(new Double(interestAmt));
                                                    objInterestBatchTO.setIntRate(new Double(roi));
                                                    objInterestBatchTO.setIntType("SIMPLE");
                                                    objInterestBatchTO.setPrincipleAmt(CommonUtil.convertObjToDouble(acctDtlMap.get("DEPOSIT_AMT")));
                                                    objInterestBatchTO.setIsTdsApplied("N");
                                                    //                                        Date applDt=ServerUtil.getCurrentDateProperFormat(_branchCode);
                                                    Date applnDt = (Date) currentDate.clone();
                                                    Date applDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("END")));
                                                    if (applDt.getDate() > 0) {
                                                        applnDt.setDate(applDt.getDate());
                                                        applnDt.setMonth(applDt.getMonth());
                                                        applnDt.setYear(applDt.getYear());
                                                    }
                                                    //                                        objInterestBatchTO.setApplDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("END"))));
                                                    objInterestBatchTO.setApplDt(applnDt);
                                                } else if (acctDtlMap.containsKey("FREQ") && ((CommonUtil.convertObjToInt(acctDtlMap.get("FREQ")) == 90)
                                                        || (CommonUtil.convertObjToInt(acctDtlMap.get("FREQ")) == 180) || (CommonUtil.convertObjToInt(acctDtlMap.get("FREQ")) == 360)
                                                        || (CommonUtil.convertObjToStr(paramMap.get("DISCOUNTED_RATE")).equals("N") && CommonUtil.convertObjToInt(acctDtlMap.get("FREQ")) == 30))) {

                                                   
                                                    int fr = CommonUtil.convertObjToInt(acctDtlMap.get("FREQ"));  
                                                    double period1 = 0.0;
                                                    long pr1 = DateUtil.dateDiff(lstIntCrDt, nextCalsDt);
                                                    double pr = (double) pr1;
                                                    double roi = 0.0;
                                                    double calcAmt = CommonUtil.convertObjToDouble(acctDtlMap.get("DEPOSIT_AMT")).doubleValue();
//                                                    System.out.println("@!$!@$!@$ lstIntCrDt #"+lstIntCrDt);
//                                                    System.out.println("@!$!@$!@$ nextCalsDt #"+nextCalsDt);
//                                                    System.out.println("@!$!@$!@$ pr1 #"+pr1);
//                                                    System.out.println("@!$!@$!@$ calcAmt #"+calcAmt);
                                                    if (slab.equals("Y")) {
                                                        roi = rateOfInt;
                                                    } else {
                                                        roi = CommonUtil.convertObjToDouble(acctDtlMap.get("RATE_OF_INT")).doubleValue();
                                                    }
//                                                    System.out.println("@!$!@$!@$ lstproDt #"+lstproDt);
//                                                    System.out.println("@!$!@$!@$ lstIntCrDt #"+lstIntCrDt);
                                                    if (DateUtil.dateDiff(lstproDt, lstIntCrDt) > 0 && DateUtil.dateDiff(nextCalsDt, depMatDt) >= 0) {
                                                        double interestAmt1 = 0.0;
                                                        double interestAmt2 = 0.0;
                                                        for (lstIntCrDt = DateUtil.addDays(lstIntCrDt, fr); DateUtil.dateDiff(lstIntCrDt, currentDate) >= 0; lstIntCrDt = DateUtil.addDays(lstIntCrDt, fr)) {                                                            
                                                            if (DateUtil.dateDiff(nextCalsDt, depMatDt) >= 0) {
                                                                interestAmt2 = (fr * roi * calcAmt) / (1200 * 30);
                                                                interestAmt2 = (double) getNearest((long) (interestAmt2 * 100), 100) / 100;
                                                                interestAmt1 = interestAmt1 + interestAmt2;
                                                                nextCalsDt = lstIntCrDt;
                                                            }
                                                        }                                                       
                                                        interestAmt = interestAmt1;
                                                        intTrfAmt = interestAmt;
                                                        lstIntCrDt = DateUtil.addDays(lstIntCrDt, -fr);
                                                       
                                                        if (Tot_Int_AMT > 0 && DateUtil.dateDiff(depMatDt, nextCalsDt) >= 0) {
                                                            interestAmt = Tot_Int_AMT - Tot_Int_Cr;
                                                            intTrfAmt = interestAmt;
                                                            nextCalsDt = depMatDt;
                                                        }
                                                        paramMap.put("END", nextCalsDt);
                                                    } else if (DateUtil.dateDiff(lstproDt, lstIntCrDt) <= 0 && DateUtil.dateDiff(nextCalsDt, depMatDt) >= 0) {
                                                        System.out.println("@!$!@$!@$ DateUtil.dateDiff(lstproDt, lstIntCrDt) #"+DateUtil.dateDiff(lstproDt, lstIntCrDt));
                                                        System.out.println("@!$!@$!@$ DateUtil.dateDiff(nextCalsDt, depMatDt) #"+DateUtil.dateDiff(nextCalsDt, depMatDt));
                                                        double DiffAmt = 0.0;
                                                        if (DateUtil.dateDiff(lstIntCrDt, depDt) != 0 || DateUtil.dateDiff(lstproDt, depDt) != 0) { // for the prpose of renwal after
                                                            pr = DateUtil.dateDiff(lstIntCrDt, lstproDt);                                                           
                                                            if (DateUtil.dateDiff(lstIntCrDt, depDt) >= 0) {
                                                                //                                                    pr=pr+1; //for one Day Minues in Provision in depositOpen Date FOR MHCB
                                                            }
                                                            pr = pr + 1; //FRO MHCB
                                                            period1 = pr * 30;
                                                            period1 = period1 / 30;
                                                            
                                                            DiffAmt = (period1 * roi * calcAmt) / 36500;
                                                          
                                                            DiffAmt = (double) getNearest((long) (DiffAmt * 100), 100) / 100;
                                                        }
                                                        pr = DateUtil.dateDiff(lstIntCrDt, nextCalsDt);
                                                        System.out.println("@!$!@$!@$ pr #"+pr);
                                                        period1 = pr / 30;
                                                        System.out.println("@!$!@$!@$ period1 #"+period1);
                                                        period1 = (double) getNearest((long) (period1 * 100), 100) / 100;
                                                        System.out.println("@!$!@$!@$ period1 2 #"+period1);
                                                        interestAmt = (period1 * roi * calcAmt) / 1200;
                                                        interestAmt = (double) getNearest((long) (interestAmt * 100), 100) / 100;
                                                        System.out.println("@!$!@$!@$ interestAmt 2 #"+interestAmt);
                                                        intTrfAmt = interestAmt;
                                                        interestAmt = interestAmt - DiffAmt;
                                                        System.out.println("@!$!@$!@$ DiffAmt  #"+DiffAmt);
                                                        System.out.println("@!$!@$!@$ interestAmt  #"+interestAmt);
                                                        if (Tot_Int_AMT > 0 && DateUtil.dateDiff(depMatDt, nextCalsDt) >= 0) {
                                                            System.out.println("@!$!@$!@$ Tot_Int_AMT  #"+Tot_Int_AMT);
                                                            System.out.println("@!$!@$!@$ Tot_Int_Cr  #"+Tot_Int_Cr);
                                                            interestAmt = Tot_Int_AMT - Tot_Int_Cr;
                                                            System.out.println("@!$!@$!@$ interestAmt  #"+interestAmt);
                                                            intTrfAmt = interestAmt + DiffAmt;
                                                            System.out.println("@!$!@$!@$ intTrfAmt  #"+intTrfAmt);
                                                            nextCalsDt = depMatDt;
                                                        }
                                                       
                                                        paramMap.put("END", nextCalsDt);
                                                        paramMap.put("START", lstproDt);
                                                       
                                                        double interestAmt1 = 0.0;
                                                        double interestAmt2 = 0.0;

                                                        Date lstIntCrDt1 = DateUtil.addDays(lstIntCrDt, fr);
                                                        

                                                        if (DateUtil.dateDiff(lstIntCrDt1, currentDate) >= 0) {
                                                            //                                                            lstIntCrDt=lstIntCrDt1;
                                                            lstIntCrDt = lstIntCrDt1;
                                                            int i = 0;
                                                            for (lstIntCrDt = DateUtil.addDays(lstIntCrDt, fr); DateUtil.dateDiff(lstIntCrDt, currentDate) >= 0; lstIntCrDt = DateUtil.addDays(lstIntCrDt, fr)) {
                                                                System.out.println("@!$!@$!@$ lstIntCrDt final #"+lstIntCrDt);
                                                                System.out.println("@!$!@$!@$ DateUtil.dateDiff(lstIntCrDt, currentDate) final #"+DateUtil.dateDiff(lstIntCrDt, currentDate));
                                                                System.out.println("@!$!@$!@$ nextCalsDt final #"+nextCalsDt);
                                                                if (DateUtil.dateDiff(nextCalsDt, depMatDt) >= 0) {
                                                                    i = i + 1;
                                                                    interestAmt2 = (calcAmt * roi * fr) / (1200 * 30);
                                                                    System.out.println("@!$!@$!@$ interestAmt2 ddadad #"+interestAmt2);
                                                                    interestAmt2 = (double) getNearest((long) (interestAmt2 * 100), 100) / 100;
                                                                    interestAmt1 = interestAmt1 + interestAmt2;
                                                                    nextCalsDt = lstIntCrDt;
                                                                   
                                                                }
                                                            }                                                            
                                                            interestAmt = interestAmt + interestAmt1;
                                                            System.out.println("@!$!@$!@$ interestAmt2 final #"+interestAmt);
                                                            intTrfAmt = interestAmt1 + intTrfAmt;
                                                            lstIntCrDt = DateUtil.addDays(lstIntCrDt, -fr);
                                                        
                                                            paramMap.put("END", nextCalsDt);

                                                           
                                                            if (Tot_Int_AMT > 0 && DateUtil.dateDiff(depMatDt, nextCalsDt) >= 0) {
                                                                interestAmt = Tot_Int_AMT - Tot_Int_Cr;
                                                                intTrfAmt = interestAmt + DiffAmt;
                                                            }

                                                        }
                                                       

                                                    }
                                                } else if (acctDtlMap.containsKey("FREQ") && CommonUtil.convertObjToInt(acctDtlMap.get("FREQ")) == 0) {
                                      
                                                    long onMatPer = 0;
                                                    onMatPer = DateUtil.dateDiff(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("START"))), DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("END"))));
                                            
                                                    if (DateUtil.dateDiff(lstIntCrDt, depDt) <= 0 && DateUtil.dateDiff(lstIntCrDt, lstproDt) < 0) {
                                                        onMatPer = onMatPer + 1;
                                                    }
                                                    double roi = 0.0;
                                                    double calcAmt = CommonUtil.convertObjToDouble(acctDtlMap.get("DEPOSIT_AMT")).doubleValue();
                                                  
                                                    if (slab.equals("Y")) {
                                                        roi = rateOfInt;
                                                    } else {
                                                        roi = CommonUtil.convertObjToDouble(acctDtlMap.get("RATE_OF_INT")).doubleValue();
                                                    }
                                                
                                                    interestAmt = (onMatPer * roi * calcAmt) / 36500;
                                                   
                                                    interestAmt = (double) getNearest((long) (interestAmt * 100), 100) / 100;
                                                    intTrfAmt = Tot_Int_AMT;
                                                    if (Tot_Int_AMT > 0 && DateUtil.dateDiff(depMatDt, nextCalsDt) >= 0) {
                                                        interestAmt = Tot_Int_AMT - Tot_Int_Cr;
                                                        intTrfAmt = interestAmt;
                                                        intTrfAmt = Tot_Int_AMT;
                                                    }

                                                } else {
                                                  
                                                    double period1 = 0.0;
                                                    double pr = 0.0;
                                                    period1 = pr / 30;
                                                    double roi = 0.0;
                                                    if (slab.equals("Y")) {
                                                        roi = rateOfInt;
                                                    } else {
                                                        roi = CommonUtil.convertObjToDouble(acctDtlMap.get("RATE_OF_INT")).doubleValue();
                                                    }
                                                    double calcAmt = CommonUtil.convertObjToDouble(acctDtlMap.get("DEPOSIT_AMT")).doubleValue();
                                                    interestAmt = (period1 * roi * calcAmt) / 1200;

                                                    interestAmt = (double) getNearest((long) (interestAmt * 100), 100) / 100;
                                                    intTrfAmt = interestAmt;

                                                }
                                                double roi = 0.0;
                                                double tdsDeductAmt = 0.0;
                                                TransferTrans transferTrans = new TransferTrans();
                                                ArrayList batchList = new ArrayList();
                                                TdsCalc tds = new TdsCalc(actBranch);
                                                String CustId = CommonUtil.convertObjToStr(paramMap.get("CUST_ID"));
                                                String Prod_type = CommonUtil.convertObjToStr(paramMap.get("PROD_TYPE"));
                                                String prod_id = CommonUtil.convertObjToStr(paramMap.get(InterestTaskRunner.PRODUCT_ID));
                                                String accnum = CommonUtil.convertObjToStr(paramMap.get("ACT_NUM"));
                                                HashMap tdsMap = new HashMap();
                                                Date applDt1 = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("END")));
                                                Date applnDt1 = (Date) currentDate.clone();

                                                if (applDt1.getDate() > 0) {
                                                    applnDt1.setDate(applDt1.getDate());
                                                    applnDt1.setMonth(applDt1.getMonth());
                                                    applnDt1.setYear(applDt1.getYear());
                                                }

                                                tdsMap.put("INT_DATE", applnDt1);
                                                tdsMap.put("CUSTID", CustId);
                                                 //comented at 06/02/2015  no need tds calculation in kerala scb banks approved by soji (with presence of jibi)
//                                                List exceptionList = (List) sqlMap.executeQueryForList("getTDSExceptionData", tdsMap);
//                                                
//                                                if (exceptionList == null || exceptionList.size() <= 0) {
//                                                    tdsMap = new HashMap();
//                                                    tdsMap = tds.tdsCalcforInt(CustId, interestAmt, accnum, Prod_type, prod_id, null);
//
//                                                }
                                           
                                                objInterestBatchTO = new InterestBatchTO();
                                                objInterestBatchTO.setActNum(CommonUtil.convertObjToStr(paramMap.get("ACT_NUM")));
                                                objInterestBatchTO.setIsTdsApplied("N");
                                                if (slab.equals("Y")) {
                                                    roi = rateOfInt;
                                                } else {
                                                    roi = CommonUtil.convertObjToDouble(acctDtlMap.get("RATE_OF_INT")).doubleValue();
                                                }

                                              
                                                objInterestBatchTO.setAcHdId(CommonUtil.convertObjToStr(paramMap.get("CREDIT_INT")));
                                                objInterestBatchTO.setIntAmt(new Double(interestAmt));
                                                objInterestBatchTO.setIntRate(new Double(roi));
                                                objInterestBatchTO.setIntType("SIMPLE");
                                                objInterestBatchTO.setPrincipleAmt(CommonUtil.convertObjToDouble(acctDtlMap.get("DEPOSIT_AMT")));

                                                if (tdsMap != null) {
                                                    double tdsDeductedForBaseAccount = CommonUtil.convertObjToDouble(tdsMap.get("TDSDEDUCTEDTOBASEACCOUNT")).doubleValue();
                                                    tdsDeductAmt = CommonUtil.convertObjToDouble(tdsMap.get("TDSDRAMT")).doubleValue();
                                                    if (tdsDeductedForBaseAccount > 0) {
                                                        objInterestBatchTO.setIsTdsApplied("Y");
                                                        objInterestBatchTO.setTdsAmt(new Double(tdsDeductedForBaseAccount));
                                                       
                                                        objInterestBatchTO.setIntAmt(new Double(interestAmt - tdsDeductAmt));
                                                        objInterestBatchTO.setTdsDeductedFromAll(new Double(tdsDeductAmt));
                                                        objInterestBatchTO.setTotalTdsAmt(new Double(tdsDeductedForBaseAccount));
                                                        objInterestBatchTO.setLastTdsRecivedFrom(CommonUtil.convertObjToStr(tdsMap.get("DEBIT_ACT_NUM")));
                                                      
                                                        objInterestBatchTO.setLastTdsApplDt(currentDate);

                                                    }




                                                    if (tdsDeductAmt > 0) {
                                                        HashMap txMap = new HashMap();
                                                        txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(tdsMap.get("TDSCrACHdId")));
                                                        txMap.put(TransferTrans.CR_PROD_TYPE, "GL");
                                                        txMap.put(TransferTrans.CURRENCY, "INR");
                                                        txMap.put(TransferTrans.PARTICULARS, accnum + "TdsDeducted");
                                                        txMap.put(TransferTrans.CR_BRANCH, actBranch);
                                                         txMap.put("generateSingleTransId",generateSingleTransId);
                                                       
                                                        tdsDeductAmt=CommonUtil.convertObjToDouble(tdsMap.get("TDSDRAMT")).doubleValue();
                                          
                                                        batchList.add(transferTrans.getCreditTransferTO(txMap, tdsDeductAmt));
                                                        intTrfAmt = intTrfAmt - tdsDeductAmt;
                                                        objInterestBatchTO.setActNum(CommonUtil.convertObjToStr(paramMap.get("ACT_NUM")));
                                                    }
                                                }


                                                Date applnDt = (Date) currentDate.clone();
                                                Date applDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("END")));
                                                if (applDt.getDate() > 0) {
                                                    applnDt.setDate(applDt.getDate());
                                                    applnDt.setMonth(applDt.getMonth());
                                                    applnDt.setYear(applDt.getYear());
                                                }
                                              
                                                objInterestBatchTO.setApplDt(applnDt);

                                                interestAmt = (double) getNearest((long) (interestAmt * 100), 100) / 100;
                                                double Tot_Int_bal = (interestAmt + Tot_Int_Cr) - Tot_Int_Dr - tdsDeductAmt;
                                               
                                                HashMap resultMap = new HashMap();
                                                Date nxtCalDt = null;
                                                if (CommonUtil.convertObjToStr(acctDtlMap.get("CALENDER_FREQ")).length() > 0 && CommonUtil.convertObjToStr(acctDtlMap.get("CALENDER_FREQ")).equals("Y")) {

                                                    nxtCalDt = DateUtil.addDays(nextCalsDt, CommonUtil.convertObjToInt(acctDtlMap.get("FREQ")));
                                                } else {

                                                    nxtCalDt = nextCaldate(depDt, nextCalsDt, acctDtlMap);
                                                }
                                               
                                               
                                                resultMap = prepareMap(paramMap, resultMap);
                                                Date apStDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("START")));
                                                Date apEdDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("END")));
                                                apStDt = DateUtil.addDays(apStDt, 1);
                                                if (interestAmt < 0 && DateUtil.dateDiff(apStDt, apEdDt) == 0) {
                                                    resultMap.put(TransferTrans.DR_AC_HD, (String) paramMap.get("CREDIT_INT"));
                                                    resultMap.put(TransferTrans.CR_AC_HD, (String) paramMap.get("DEBIT_INT"));
                                                    interestAmt = interestAmt * -1;
                                                }
                                                resultMap.put("LINK_BATCH_ID", paramMap.get("ACT_NUM"));
                                                transferTrans.setLinkBatchId(CommonUtil.convertObjToStr(paramMap.get("ACT_NUM")));
                                                System.out.println("$#@$#@$@#$#@ interestAmt "+interestAmt);
                                                if (paramMap.containsKey("CHARGES_PROCESS") && paramMap.get("BEHAVES_LIKE").equals("RECURRING")) {
                                                    HashMap rdMap = new HashMap();
                                                    interestAmt = 0;
                                                    List rdList;
                                                    String actNum = CommonUtil.convertObjToStr(paramMap.get("ACT_NUM"));
                                                    actNum = actNum.lastIndexOf("_") != -1 ? actNum.substring(0, actNum.length() - 2) : actNum;
                                                    rdMap.put("DEPOSIT_NO", actNum);
                                                    // Added by nithya on 22-09-2018 for KD 251 - 0017252: Deposit Interest Application(RD interest application) issue
                                                    if(paramMap.containsKey("PROVISION_DATE") && paramMap.get("PROVISION_DATE")!= null){
                                                       Date provisionDate = getProperFormatDate(paramMap.get("PROVISION_DATE"));
                                                       rdMap.put("CURR_DT", provisionDate);
                                                        //System.out.println("rdMap :: " + rdMap);
                                                    }else{
                                                       rdMap.put("CURR_DT", currentDate.clone());
                                                    }
                                                    if (paramMap.containsKey("SPECIAL_RD_SCHEME") && paramMap.get("SPECIAL_RD_SCHEME") != null && CommonUtil.convertObjToStr(paramMap.get("SPECIAL_RD_SCHEME")).equalsIgnoreCase("SPECIAL_RD_SCHEME")) {
                                                       rdList = sqlMap.executeQueryForList("getSpecialRdInterest", rdMap); 
                                                    }else{
                                                       rdList = sqlMap.executeQueryForList("getRdInterest", rdMap);
                                                    }
                                                    if (rdList != null && rdList.size() > 0) {
                                                        rdMap = new HashMap();
                                                        rdMap = (HashMap) rdList.get(0);
                                                        interestAmt = CommonUtil.convertObjToDouble(rdMap.get("RD_INTEREST"));
                                                        objInterestBatchTO.setIntAmt(new Double(interestAmt - tdsDeductAmt));
                                                        Tot_Int_bal = (interestAmt + Tot_Int_Cr) - Tot_Int_Dr - tdsDeductAmt;
                                                        System.out.println("$#@$#@$@#$#@ interestAmt RECURRING "+interestAmt);
                                                    }
                                                }
                                                batchList.add(transferTrans.getDebitTransferTO(resultMap, interestAmt));
                                                batchList.add(transferTrans.getCreditTransferTO(resultMap, interestAmt - tdsDeductAmt));
                                               
                                                transferTrans.setInitiatedBranch(actBranch);
                                           

                                                if (interestAmt != 0 || CommonUtil.convertObjToInt(acctDtlMap.get("FREQ")) == 0) {
                                                    transferTrans.doDebitCredit(batchList, branch);
                                                }

                                                Date nextDt = (Date) currentDate.clone();
                                                Date nextCalcDt = nxtCalDt;
                                                if (nextCalcDt.getDate() > 0) {
                                                    nextDt.setDate(nextCalcDt.getDate());
                                                    nextDt.setMonth(nextCalcDt.getMonth());
                                                    nextDt.setYear(nextCalcDt.getYear());
                                                }
                                                Date currDt = (Date) currentDate.clone();
                                                Date stDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("START")));
                                                if (stDt.getDate() > 0) {
                                                    currDt.setDate(stDt.getDate());
                                                    currDt.setMonth(stDt.getMonth());
                                                    currDt.setYear(stDt.getYear());
                                                }
                                                objInterestBatchTO.setProductId(CommonUtil.convertObjToStr(paramMap.get("PROD_ID")));
                                                
                                                objInterestBatchTO.setApplDt(applnDt);
                                                if (paramMap.containsKey("SPECIAL_RD_SCHEME") && paramMap.get("SPECIAL_RD_SCHEME") != null && CommonUtil.convertObjToStr(paramMap.get("SPECIAL_RD_SCHEME")).equalsIgnoreCase("SPECIAL_RD_SCHEME")) {
                                                  objInterestBatchTO.setApplDt(getProperFormatDate(paramMap.get("DATE_TO")));
                                                }
                                                objInterestBatchTO.setIntDt(currDt);
                                                objInterestBatchTO.setTrnDt(currentDate);
                                                objInterestBatchTO.setDrCr("CREDIT");
                                                objInterestBatchTO.setTot_int_amt(new Double(Tot_Int_bal));
                                                objInterestBatchTO.setTransLogId("A");
                                                objInterestBatchTO.setCustId(CommonUtil.convertObjToStr(paramMap.get("CUST_ID")));
                                                if (interestAmt != 0) {
                                                    if (tdsDeductAmt > 0.0) {
                                                        objInterestBatchTO.setIsTdsApplied("Y");
                                                        objInterestBatchTO.setTdsAmt(CommonUtil.convertObjToDouble(tdsMap.get("TDSDEDUCTEDTOBASEACCOUNT")));
                                                    }
                                                    sqlMap.executeUpdate("insertDepositInterestTO", objInterestBatchTO);
                                                }
                                                sqlMap.executeUpdate("updateLastInterestAmount", objInterestBatchTO);
                                                //System.out.println("specialRD :: " + paramMap);
                                                //Added by nithya on 08-05-2020 for KD-1535 
                                                if (paramMap.containsKey("SPECIAL_RD_SCHEME") && paramMap.get("SPECIAL_RD_SCHEME") != null && CommonUtil.convertObjToStr(paramMap.get("SPECIAL_RD_SCHEME")).equalsIgnoreCase("SPECIAL_RD_SCHEME")) {
                                                    HashMap intMap = new HashMap();
                                                    String depNo = CommonUtil.convertObjToStr(paramMap.get("ACT_NUM"));
                                                    depNo = depNo.lastIndexOf("_") != -1 ? depNo.substring(0, depNo.length() - 2) : depNo;
                                                    intMap.put("DEPOSIT_NO", depNo);
                                                    sqlMap.executeUpdate("updateTotalIntAmountForSpecialRD", intMap);
                                                }
                                                //End
                                                if (tdsDeductAmt > 0.0) {
                                                    objInterestBatchTO.setIntAmt(new Double(tdsDeductAmt));
                                                    sqlMap.executeUpdate("updateTotIndDrawnAmount", objInterestBatchTO);
                                                    objInterestBatchTO.setIntAmt(new Double(0));
                                                }
                                                HashMap upMapint = new HashMap();
                                                upMapint.put("actNum", paramMap.get("ACT_NUM"));
                                                upMapint.put("applDt", applnDt);
                                                upMapint.put("nextIntappldt", nextDt);
                                                upMapint.put("DEPOSIT_NO", paramMap.get("ACT_NUM"));
                                                List lst = sqlMap.executeQueryForList("getDepositAccountDetails", upMapint);
                                                if (lst != null && lst.size() > 0) {
                                                    HashMap updateMap = (HashMap) lst.get(0);
                                                    updateMap.put("TEMP_NEXT_INT_APPL_DT", updateMap.get("NEXT_INT_APPL_DT"));
                                                    updateMap.put("TEMP_LAST_INT_APPL_DT", updateMap.get("LAST_INT_APPL_DT"));
                                                    updateMap.put("INT_CREDIT", updateMap.get("TOTAL_INT_CREDIT"));
                                                    updateMap.put("INT_DRAWN", updateMap.get("TOTAL_INT_DRAWN"));
                                                    sqlMap.executeUpdate("updateDepositTempDate", updateMap);
                                                }
                                                if (tdsDeductAmt > 0.0) {
                                                    objInterestBatchTO.setIntAmt(new Double(0));
                                                }                                                
                                                if (paramMap.containsKey("SPECIAL_RD_SCHEME") && paramMap.get("SPECIAL_RD_SCHEME") != null && CommonUtil.convertObjToStr(paramMap.get("SPECIAL_RD_SCHEME")).equalsIgnoreCase("SPECIAL_RD_SCHEME")) {
                                                    upMapint.put("applDt", null);
                                                    //System.out.println("executing date here 1");
                                                }
                                                sqlMap.executeUpdate("updateDepositIntLastApplDT", upMapint);
                                                sqlMap.executeUpdate("updateDepositNextIntApplDT", upMapint);
                                                //                                }

                                                intTrfAmt = (double) getNearest((long) (intTrfAmt * 100), 100) / 100;
                                                if (acctDtlMap.get("INTPAY_MODE").equals("TRANSFER")) {
                                                    if (acctDtlMap.get("INT_PAY_PROD_TYPE") != null && CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_PROD_TYPE")).length() > 0) {
                                                       
                                                        HashMap accStatusMap = new HashMap();
                                                        if (CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_PROD_TYPE")).equals("OA") || CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_PROD_TYPE")).equals("AD") || CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_PROD_TYPE")).equals("TL")) {

                                                            String ProdType = CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_PROD_TYPE"));
                                                            if (ProdType.equals("TL")) {
                                                                ProdType = "AD";
                                                            }
                                                            accStatusMap.put("ACT_NUM", CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_ACC_NO")));
                                                            List stLst = sqlMap.executeQueryForList("getAccountStatus" + ProdType, accStatusMap);
                                                            accStatusMap = (HashMap) stLst.get(0);

                                                        }
                                                        if (!CommonUtil.convertObjToStr(accStatusMap.get("ACT_STATUS_ID")).equals("CLOSED")) {
                                                           
                                                            intTrfAmt = (interestAmt + Tot_Int_Cr) - Tot_Int_Dr - tdsDeductAmt;
                                                            
                                                            dointTransferToAccount(acctDtlMap, intTrfAmt, paramMap, null);
                                                            objInterestBatchTO.setIntAmt(new Double(intTrfAmt));
                                                            objInterestBatchTO.setDrCr("DEBIT");
                                                            Tot_Int_bal = Tot_Int_bal - intTrfAmt;
                                                            objInterestBatchTO.setTot_int_amt(new Double(Tot_Int_bal));
                                                            sqlMap.executeUpdate("insertDepositInterestTO", objInterestBatchTO);
                                                            sqlMap.executeUpdate("updateTotIndDrawnAmount", objInterestBatchTO);

                                                        }
                                                     
                                                    } else {
                                                        throw new TTException("Interest Payment Product Type not found...");
                                                    }
                                                }
                                            }
                                           // sqlMap.commitTransaction();

                                        } catch (Exception e) {
                                        
                                            if (!paramMap.containsKey("CHARGES_PROCESS")) {
                                                //sqlMap.rollbackTransaction();
                                                String errMsg = "";
                                                TTException tte = null;
                                                HashMap exceptionMap = null;
                                                HashMap excMap = null;
                                                String strExc = null;
                                                String errClassName = "";
                                                if (e instanceof TTException) {
                                                  
                                                    tte = (TTException) e;
                                                    if (tte != null) {
                                                        exceptionMap = tte.getExceptionHashMap();
                                                    
                                                        if (exceptionMap != null) {
                                                            ArrayList list = (ArrayList) exceptionMap.get(CommonConstants.EXCEPTION_LIST);
                                                            errClassName = CommonUtil.convertObjToStr(exceptionMap.get(CommonConstants.CONSTANT_CLASS));
                                                            
                                                            if (list != null && list.size() > 0) {
                                                                for (int i = 0; i < list.size(); i++) {
                                                                    if (list.get(i) instanceof HashMap) {
                                                                        excMap = (HashMap) list.get(i);
                                                                       
                                                                        strExc = CommonUtil.convertObjToStr(excMap.get(CommonConstants.ERR_MESSAGE))
                                                                                + " (" + CommonUtil.convertObjToStr(excMap.get(CommonConstants.ERR_DATA)) + ")";
                                                                    } else {
                                                                        strExc = (String) list.get(i);
                                                                       
                                                                    }
                                                                    errorMap = new HashMap();
                                                                    errorMap.put("ERROR_DATE", currentDate);
                                                                    errorMap.put("TASK_NAME", taskLable);
                                                                    errorMap.put("ERROR_MSG", strExc);
                                                                    errorMap.put("ERROR_CLASS", errClassName);
                                                                    errorMap.put("ACT_NUM", paramMap.get("ACT_NUM"));
                                                                    errorMap.put("BRANCH_ID", actBranch);
                                                                    //sqlMap.startTransaction();
                                                                    sqlMap.executeUpdate("insertError_showing", errorMap);
                                                                    //sqlMap.commitTransaction();
                                                                    errorMap = null;
                                                                }
                                                            }
                                                        } else {
                                                          
                                                            errMsg = e.getMessage();
                                                            errorMap = new HashMap();
                                                            errorMap.put("ERROR_DATE", currentDate);
                                                            errorMap.put("TASK_NAME", taskLable);
                                                            errorMap.put("ERROR_MSG", errMsg);
                                                            errorMap.put("ERROR_CLASS", errClassName);
                                                            errorMap.put("ACT_NUM", paramMap.get("ACT_NUM"));
                                                            errorMap.put("BRANCH_ID", actBranch);
                                                            //sqlMap.startTransaction();
                                                            sqlMap.executeUpdate("insertError_showing", errorMap);
                                                            //sqlMap.commitTransaction();
                                                            errorMap = null;
                                                        }
                                                    }
                                                } else {
                                                    
                                                    errMsg = e.getMessage();
                                                    errorMap = new HashMap();
                                                    errorMap.put("ERROR_DATE", currentDate);
                                                    errorMap.put("TASK_NAME", taskLable);
                                                    errorMap.put("ERROR_MSG", errMsg);
                                                    errorMap.put("ERROR_CLASS", errClassName);
                                                    errorMap.put("ACT_NUM", paramMap.get("ACT_NUM"));
                                                    errorMap.put("BRANCH_ID", actBranch);
                                                    //sqlMap.startTransaction();
                                                    sqlMap.executeUpdate("insertError_showing", errorMap);
                                                    //sqlMap.commitTransaction();
                                                    errorMap = null;
                                                }
                                                status.setStatus(BatchConstants.ERROR);
                                             
                                                tte = null;
                                                exceptionMap = null;
                                                excMap = null;
                                                e.printStackTrace();
                                            } else {
                                                sqlMap.rollbackTransaction();
                                                e.printStackTrace();
                                   
                                            }
                                        }
                                    }
                                }

                                if (!paramMap.containsKey("CHARGES_PROCESS")) {
                                   
                                    if (!compStatus.equals("COMPLETED")) {
                                        if (status.getStatus() != BatchConstants.ERROR) {
                                            //sqlMap.startTransaction();
                                            if (compStatus.equals("ERROR")) {
                                                HashMap statusMap = new HashMap();
                                               
                                                statusMap.put("BRANCH_CODE", actBranch);
                                                statusMap.put("TASK_NAME", taskLable);
                                                statusMap.put("TASK_STATUS", "COMPLETED");
                                                statusMap.put("USER_ID", getHeader().getUserID());
                                                statusMap.put("DAYEND_DT", currentDate);

                                                sqlMap.executeUpdate("updateTskStatusDayBegin", statusMap);
                                       
                                                statusMap = null;
                                            } else {
                                                HashMap statusMap = new HashMap();
                                              
                                                statusMap.put("BRANCH_CODE", actBranch);
                                                statusMap.put("TASK_NAME", taskLable);
                                                statusMap.put("TASK_STATUS", "COMPLETED");
                                                statusMap.put("USER_ID", getHeader().getUserID());
                                                statusMap.put("DAYEND_DT", currentDate);
                                              
                                                sqlMap.executeUpdate("InsertDayEndStatusDayBegin", statusMap);
                                          
                                                statusMap = null;
                                            }
                                            sqlMap.commitTransaction();
                                        } else {
                                            if (compStatus.equals("ERROR")) {
                                                HashMap statusMap = new HashMap();
                                                
                                                statusMap.put("BRANCH_CODE", actBranch);
                                                statusMap.put("TASK_NAME", taskLable);
                                                statusMap.put("TASK_STATUS", "ERROR");
                                                statusMap.put("USER_ID", getHeader().getUserID());
                                                statusMap.put("DAYEND_DT", currentDate);
                                             
                                                sqlMap.executeUpdate("updateTskStatusDayBegin", statusMap);
                                    
                                                statusMap = null;
                                            } else {
                                 
                                                HashMap statusMap = new HashMap();
                                              
                                                statusMap.put("BRANCH_CODE", actBranch);
                                                statusMap.put("TASK_NAME", taskLable);
                                                statusMap.put("TASK_STATUS", "ERROR");
                                                statusMap.put("USER_ID", getHeader().getUserID());
                                                statusMap.put("DAYEND_DT", currentDate);
                                               
                                                sqlMap.executeUpdate("InsertDayEndStatusDayBegin", statusMap);
                                                
                                                statusMap = null;

                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
                //Added by nithya on 08-05-2020 for KD-1535 
                if (paramMap.containsKey("SPECIAL_RD_SCHEME") && paramMap.get("SPECIAL_RD_SCHEME") != null && CommonUtil.convertObjToStr(paramMap.get("SPECIAL_RD_SCHEME")).equalsIgnoreCase("SPECIAL_RD_SCHEME")) {
                    nonBatch.put("SPECIAL_RD_SCHEME","SPECIAL_RD_SCHEME");
                    nonBatch.put("RD_DATE_TO",paramMap.get("DATE_TO"));
                }
                if (paramMap.containsKey("RD_INT_APPLICATION") && paramMap.get("RD_INT_APPLICATION") != null && CommonUtil.convertObjToStr(paramMap.get("RD_INT_APPLICATION")).equalsIgnoreCase("RD_INT_APPLICATION")) {
                    nonBatch.put("RD_INT_APPLICATION","RD_INT_APPLICATION");
                    nonBatch.put("RD_DATE_TO",paramMap.get("DATE_TO"));
                }
                //end
                
                paramMap = null;
                calenderFreqIntCalc(nonBatch);


            } else {
               
                //not daybegin
                ArrayList productList = getProductList(nonBatch);
                

                int prodListSize = productList.size();
                //            Date nextCalcDate = null;
                //            Date cummCalcApplDt = null;
                for (int prod = 0; prod < prodListSize; prod++) {
                    //Store all product level parameters
                    paramMap.put(InterestTaskRunner.AC_HD_ID, ((HashMap) productList.get(prod)).get("ACCT_HEAD")); //Ac Head
                    //                paramMap.put(InterestTaskRunner.DEBIT_INT, ((HashMap)productList.get(prod)).get("INT_DEBIT")) ; //Debit Int Head
                    paramMap.put(InterestTaskRunner.DEBIT_INT, ((HashMap) productList.get(prod)).get("INT_PROV_ACHD")); //Debit Int Head
                    paramMap.put(InterestTaskRunner.CREDIT_INT, ((HashMap) productList.get(prod)).get("INT_PAY")); //Credit Int Head
                    paramMap.put(InterestTaskRunner.PRODUCT_ID, ((HashMap) productList.get(prod)).get("PROD_ID")); //Product Id

                    paramMap.put(InterestTaskRunner.LAST_APPL_DT, ((HashMap) productList.get(prod)).get("LAST_INT_APPL_DT")); //Last Interest Applied Dt Payale

                    paramMap.put(InterestTaskRunner.APPL_NEW_DT, null); //Updated date based on FREQ_A from InterestTaskRunner
                    paramMap.put(InterestTaskRunner.PRODUCT_TYPE, getHeader().getProductType()); //Product Type; for Transaction update

                    nonBatch.put(InterestTaskRunner.PRODUCT_ID, ((HashMap) productList.get(prod)).get("PROD_ID")); //Product Id)
                    paramMap.put("BATCH_PROCESS", "BATCH_PROCESS");
                    paramMap.put("PROD_TYPE", "TD");
                    paramMap.put("BRANCH_CODE", getHeader().getBranchID());
                   
                    List lst = sqlMap.executeQueryForList("getProvisionForDeposit", paramMap);
                    
                    if (lst.size() > 0) {
                        HashMap nextMap = (HashMap) lst.get(0);
                        Date provDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextMap.get("LAST_PROV_DT")));
                        Date applDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextMap.get("LAST_APPL_DT")));
                       
                        if (paramMap.containsKey("BATCH_PROCESS")) {
                            int provFreq = CommonUtil.convertObjToInt(nextMap.get("INT_PROV_FREQ"));
                            int applFreq = CommonUtil.convertObjToInt(nextMap.get("INT_APPL_FREQ"));
                            nextCalcDate = DateUtil.addDays(provDt, provFreq);
                            cummCalcApplDt = DateUtil.addDays(applDt, applFreq);

                            GregorianCalendar lastDate = new GregorianCalendar(nextCalcDate.getYear(), nextCalcDate.getMonth(), nextCalcDate.getDate());
                            nextCalcDate = new Date(nextCalcDate.getYear(), nextCalcDate.getMonth(), lastDate.getActualMaximum(Calendar.DAY_OF_MONTH));
                           
                            if (paramMap.containsValue("DAY_BEGIN")) {
                                if (nextMap.containsValue("APPLICATION")) {
                                    paramMap.put("CUMM_APPLDATE", cummCalcApplDt);
                                    paramMap.put("LAST_PROV_DT", provDt);
                                    paramMap.put("LAST_APPL_DT", applDt);
                                    paramMap.put("NEXTPROVDT", nextCalcDate);
                                    paramMap.put("NEXTPROVDT", currentDate);
                                    paramMap.put("PROVISIONMAP", nextMap);
                                    paramMap.put("TASK", "APPLICATION");
                                    nonBatch.put("PARAMMAP", paramMap);
                                   
                                    implementTask(nonBatch, true);
                                    nextMap.put("REMARKS", "PROVISIONING");
                                   
                                }
                            }
                            if (DateUtil.dateDiff((Date) nextCalcDate, (Date) cummCalcApplDt) == 0) {
                                paramMap.put("TASK", "APPLICATION");
                                paramMap.put("TASK", "PROVISIONING");
                               
                            }
                            if (paramMap.containsValue("DAY_END")) {
                                if (nextMap.containsValue("PROVISIONING")) {
                                    holiydaychecking(nextCalcDate);
                                    Date dayB = (Date) currentDate.clone();
                                    if (DateUtil.dateDiff(dayB, checkThisCDate) <= 0) {
                                        if (DateUtil.dateDiff((Date) nextCalcDate, currentDate) == 0) {
                                           
                                            paramMap.put("TASK", "PROVISIONING");
                                            paramMap.put("CUMM_APPLDATE", cummCalcApplDt);
                                            paramMap.put("LAST_PROV_DT", provDt);
                                            paramMap.put("LAST_APPL_DT", applDt);
                                            paramMap.put("NEXTPROVDT", nextCalcDate);
                                            paramMap.put("NEXTPROVDT", currentDate);
                                            paramMap.put("PROVISIONMAP", nextMap);
                                            nonBatch.put("PARAMMAP", paramMap);
                                            implementTask(nonBatch, true);
                                           
                                        } else {
                                            nextMap.put("REMARKS", "APPLICATION");
                                        }
                                    }
                                   
                                }
                            }
                        }
                        if (paramMap.containsKey("CHARGES_PROCESS")) {
                            if (paramMap.containsValue("DAY_BEGIN")) {
                                if (nextMap.get("REMARKS").equals("APPLICATION")) {
                                    paramMap.put("TASK", "APPLICATION");
                                    paramMap.put("PROVISIONMAP", nextMap);
                                    paramMap.put("CUMM_APPLDATE", cummCalcApplDt);
                                    paramMap.put("LAST_PROV_DT", provDt);
                                    paramMap.put("LAST_APPL_DT", applDt);
                                    paramMap.put("NEXTPROVDT", nextCalcDate);
                                    paramMap.put("NEXTPROVDT", currentDate);
                                    paramMap.put("PROVISIONMAP", nextMap);
                                    nonBatch.put("PARAMMAP", paramMap);
                                 
                                    implementTask(nonBatch, true);
                                  
                                } else {
                                    if (paramMap.containsValue("DAY_END")) {
                                        if (DateUtil.dateDiff((Date) nextCalcDate, currentDate) == 0) {
                                            paramMap.put("TASK", "PROVISIONING");
                                            paramMap.put("PROVISIONMAP", nextMap);
                                            paramMap.put("CUMM_APPLDATE", cummCalcApplDt);
                                            paramMap.put("LAST_PROV_DT", provDt);
                                            paramMap.put("LAST_APPL_DT", applDt);
                                            paramMap.put("NEXTPROVDT", nextCalcDate);
                                            paramMap.put("NEXTPROVDT", currentDate);
                                            paramMap.put("PROVISIONMAP", nextMap);
                                            nonBatch.put("PARAMMAP", paramMap);
                                           
                                            implementTask(nonBatch, true);
                                      
                                        }
                                    }
                                }
                            }
                        }
                     
                    }
                }

                paramMap = null;
                taskSelected = 0;

            }

            //        }
            //    }
            if (status.getStatus() != BatchConstants.ERROR) {
                status.setStatus(BatchConstants.COMPLETED);
            }
        } catch (Exception e) {
            status.setStatus(BatchConstants.ERROR);
            //            sqlMap.rollbackTransaction();
            e.printStackTrace();
        }
        return status;
    }

    //__ To be called from the OB, Calculates the Interest and inserts the data into the DataBase...
    public void insertData() throws Exception {
 
        //__ The selected task is being called from OB of Some Code...
        taskSelected = INSERTDATA;
        final String prodId = CommonUtil.convertObjToStr(paramMap.get(CommonConstants.PRODUCT_ID));
        paramMap.put(InterestTaskRunner.PRODUCT_ID, prodId);

        ArrayList productList = getProductList(paramMap);
        setProdIdData(productList);

        implementTask(paramMap, false);
        taskSelected = 0;
    }

    public static double monthDiff(Date d1, Date d2) {
        return (d1.getTime() - d2.getTime()) / Avg_Millis_Per_Month;
    }
    //__ To be called from the OB to get/calculate the Interest...

    public HashMap getInterestData() throws Exception {
        HashMap dataMap;

        //__ The selected task is being called from OB of Some Code...
        taskSelected = OBCODE;
        final String prodId = CommonUtil.convertObjToStr(paramMap.get(CommonConstants.PRODUCT_ID));
        paramMap.put(InterestTaskRunner.PRODUCT_ID, prodId);
        ArrayList productList = getProductList(paramMap);
        setProdIdData(productList);
       
        dataMap = implementTask(paramMap, false);
        taskSelected = 0;

        return dataMap;
    }

    //__ to set the Data in the Param Map regarding the Product Data...
    private void setProdIdData(ArrayList productList) throws Exception {
        final String prodId = CommonUtil.convertObjToStr(paramMap.get(CommonConstants.PRODUCT_ID));
        for (int i = 0; i < productList.size(); i++) {
            String prodIdFromProdTable = CommonUtil.convertObjToStr(((HashMap) productList.get(i)).get("PROD_ID"));
            if (prodId.equals(prodIdFromProdTable)) {
                paramMap.put(InterestTaskRunner.AC_HD_ID, ((HashMap) productList.get(i)).get("ACCT_HEAD"));  //__ Ac Head
                paramMap.put(InterestTaskRunner.DEBIT_INT, ((HashMap) productList.get(i)).get("INT_DEBIT")); //__ Debit Int Head
                paramMap.put(InterestTaskRunner.CREDIT_INT, ((HashMap) productList.get(i)).get("INT_PAY"));  //__ Credit Int Head
                paramMap.put(InterestTaskRunner.PRODUCT_ID, ((HashMap) productList.get(i)).get("PROD_ID"));  //__ Product Id

                paramMap.put(InterestTaskRunner.LAST_APPL_DT, ((HashMap) productList.get(i)).get("LAST_INT_APPLDT")); //Last Interest Applied Dt Payale

                paramMap.put(InterestTaskRunner.APPL_NEW_DT, null); //__ Updated date based on FREQ_A from InterestTaskRunner
                paramMap.put(InterestTaskRunner.PRODUCT_TYPE, getHeader().getProductType()); //__ Product Type; for Transaction update
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {
            TaskHeader header = new TaskHeader();
            header.setTaskClass("IntPayableTask");
            header.setTransactionType(CommonConstants.PAYABLE);
            header.setProductType(TransactionFactory.DEPOSITS);

            header.setBranchID("Bran");
            header.setIpAddr("172.19.147.86");
            header.setUserID("sysadmin");

            //__ Account Nos are comming as a List...
            ArrayList acctList = new ArrayList();
            acctList.add("D0001113_1");

            ArrayList closingDateList = new ArrayList();
            closingDateList.add(DateUtil.getDate(1, 4, 2005));

            HashMap taskParam = new HashMap();
            taskParam.put(CommonConstants.BRANCH, "Bran");
            taskParam.put(CommonConstants.PRODUCT_ID, "FDGEN");
            taskParam.put(CommonConstants.ACT_NUM, acctList);

            header.setTaskParam(taskParam);
            DepositIntTask tsk = new DepositIntTask(header);
            
            HashMap dataMap = tsk.getInterestData();
           
          
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void holiydaychecking(Date lstintCr) {
        try {
            HashMap MonthEnd = new HashMap(); //traansferto holidaychecking method;
            boolean checkHoliday = true;
            MonthEnd.put("NEXT_DATE", lstintCr.clone());
            MonthEnd.put(CommonConstants.BRANCH_ID, actBranch);
     
            lstintCr = lstintCr;
            while (checkHoliday) {
                boolean tholiday = false;
           
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

            }
          
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private HashMap dateMinus(HashMap dateMap) {
        String day = CommonUtil.convertObjToStr(dateMap.get("NEXT_DATE"));
   
        Date lastDay = (Date) dateMap.get("NEXT_DATE");
        int days = lastDay.getDate();
        days--;
        lastDay.setDate(days);
        dateMap.put("NEXT_DATE", lastDay);
        dateMap.put(CommonConstants.BRANCH_ID, actBranch);
        return dateMap;
    }

    private boolean doNoHoliday(String nonHoliday) {
        checkThisCDate = DateUtil.getDateMMDDYYYY(nonHoliday);
    
        return false;
    }

    private void dayendprocess(HashMap nonBatch) throws Exception {
        if (paramMap.containsKey("PROCESS") && CommonUtil.convertObjToStr(paramMap.get("PROCESS")).equals(CommonConstants.DAY_END)) {
       
            nonBatch.put("PROVISIONING", "PROVISIONING");
            InterestBatchTO objInterestBatchTO = null;
 
            ArrayList productList = getProductList(nonBatch);

            if (productList != null && productList.size() > 0) {

                int prodListSize = productList.size();
                for (int prod = 0; prod < prodListSize; prod++) {
                    //                        paramMap=null;
                    double totProdSum = 0.0;
                    double totTdsDeductAmt = 0.0;
                    double tdsDeductAmt = 0.0;
                    double tdsDeductedForBaseAccount = 0.0;
                    String tdsHd = "";
                  
                    paramMap.put(InterestTaskRunner.AC_HD_ID, ((HashMap) productList.get(prod)).get("ACCT_HEAD")); //Ac Head
                    paramMap.put("INT_PROV_FREQ", ((HashMap) productList.get(prod)).get("INT_PROV_FREQ")); //Ac Head
                    paramMap.put(InterestTaskRunner.DEBIT_INT, ((HashMap) productList.get(prod)).get("INT_PROV_ACHD")); //Debit Int Head
                    paramMap.put(InterestTaskRunner.CREDIT_INT, ((HashMap) productList.get(prod)).get("INT_PAY")); //Credit Int Head
                    paramMap.put(InterestTaskRunner.PRODUCT_ID, ((HashMap) productList.get(prod)).get("PROD_ID")); //Product Id
                    paramMap.put("ROUND_OFF", ((HashMap) productList.get(prod)).get("INT_ROUNDOFF_TERMS")); //Product Id
                    paramMap.put(InterestTaskRunner.LAST_APPL_DT, ((HashMap) productList.get(prod)).get("LAST_INT_APPL_DT")); //Last Interest Applied Dt Payale

                    paramMap.put("interestType", ((HashMap) productList.get(prod)).get("INT_TYPE"));
                    paramMap.put("BEHAVES_LIKE", ((HashMap) productList.get(prod)).get("BEHAVES_LIKE")); //Product Id
                    paramMap.put(InterestTaskRunner.APPL_NEW_DT, null); //Updated date based on FREQ_A from InterestTaskRunner


                    nonBatch.put(InterestTaskRunner.PRODUCT_ID, ((HashMap) productList.get(prod)).get("PROD_ID")); //Product Id)
                    paramMap.put("BATCH_PROCESS", "BATCH_PROCESS");
                    paramMap.put("PROD_TYPE", "TD");
                    paramMap.put("TASK", "PROVISIONING");
                    paramMap.put("DISCOUNTED_RATE", ((HashMap) productList.get(prod)).get("DISCOUNTED_RATE"));
                    List ntlst = null;
                    if (branchList != null && branchList.size() > 0) {
                        for (int b = 0; b < branchList.size(); b++) {
                            HashMap branchMap = (HashMap) branchList.get(b);
                            actBranch = CommonUtil.convertObjToStr(branchMap.get(CommonConstants.BRANCH_ID));
                            currentDate = ServerUtil.getCurrentDate(actBranch);
                            HashMap compMap = new HashMap();
                            //                    compMap = null;
                            String compStatus = "";
                            List compLst = null;
                            if (!paramMap.containsKey("CHARGES_PROCESS")) {
                                compMap.put("TASK_NAME", taskLable);
                                compMap.put("DAYEND_DT", currentDate);
                                compMap.put("BRANCH_ID", actBranch);
                                compLst = (List) sqlMap.executeQueryForList("getSelectTaskLst", compMap);
                                if (compLst != null && compLst.size() > 0) {
                                    compMap = (HashMap) compLst.get(0);
                                    compStatus = CommonUtil.convertObjToStr(compMap.get("TASK_STATUS"));
                                    compMap = null;
                                }
                            } else {
                                compStatus = "ERROR";
                                compMap.put("TASK_NAME", "A");
                                compMap.put("DAYEND_DT", currentDate);
                                compMap.put("BRANCH_ID", actBranch);
                                compLst = (List) sqlMap.executeQueryForList("getSelectTaskLst", compMap);
                            }
                            if (compLst.size() <= 0 || compStatus.equals("ERROR")) {
                                HashMap errorMap = new HashMap();
                                if (!paramMap.containsKey("CHARGES_PROCESS")) {
                                    //sqlMap.startTransaction();
                                    errorMap.put("ERROR_DATE", currentDate);
                                    errorMap.put("TASK_NAME", taskLable);
                                    errorMap.put("BRANCH_ID", branch);
                                    sqlMap.executeUpdate("deleteError_showing", errorMap);
                                   // sqlMap.commitTransaction();
                                }
                                paramMap.put(CommonConstants.BRANCH_ID, actBranch);
                                ntlst = sqlMap.executeQueryForList("lastProvisionDt", paramMap);
                                nonBatch.put("PARAMMAP", paramMap);

                                if (ntlst != null && ntlst.size() > 0) {
                                    HashMap dtMap = (HashMap) ntlst.get(0);
                                    Date nxtDt = (Date) dtMap.get("LAST_PROV_DT");
                                    Date lstDt = nxtDt;
                                    Date nextCalsDt = nxtDt;
                                    int provFreq = CommonUtil.convertObjToInt(paramMap.get("INT_PROV_FREQ"));
                                    provFreq = provFreq * -1;
                              
                                    
                                    Date ltProvMdDt = (Date) dtMap.get("LSTPROVDT");
                                 
                                    holiydaychecking(nxtDt);
                                    nxtDt = lstDt;
                                 
                                    Date dbDt = (Date) currentDate.clone();
          
                                    List accountList = null;
                                    if (DateUtil.dateDiff(checkThisCDate, dbDt) >= 0) {
                      
                                        nonBatch.put("NXT_DT", nxtDt);
                                        accountList = getAccountList(nonBatch);
                                        if (accountList != null && accountList.size() > 0) {
                                            int accountListSize = accountList.size();
                                            for (int acc = 0; acc < accountListSize; acc++) {
                                                try {
                                                    if (isTransaction) {
                                                        //sqlMap.startTransaction();
                                                    }
                                                    paramMap.put("ACT_NUM", ((HashMap) accountList.get(acc)).get("ACT_NUM"));
                                                    //                                        actBranch = CommonUtil.convertObjToStr(((HashMap)accountList.get(acc)).get("BRANCH_ID"));
                                                    paramMap.put("PROD_TYPE", "TD");
                                                    paramMap.put("CUST_TYPE", ((HashMap) accountList.get(acc)).get("CUST_TYPE"));
                                                    paramMap.put("CUST_ID", ((HashMap) accountList.get(acc)).get("CUST_ID"));
                                                    paramMap.put("CUR_DT", currentDate);
                                                    paramMap.put(CommonConstants.BRANCH_ID, actBranch);
                                                    double interestAmt = 0.0;
                                                    //                                    paramMap.put("TASK1", "PROVISIONING");
                                                    System.out.println("00003");
                                                    List acctDtl = sqlMap.executeQueryForList("getDepoitDetailForIntApp", paramMap);
                                                    if (acctDtl != null && acctDtl.size() > 0) {
                                                        HashMap acctDtlMap = new HashMap();
                                                        acctDtlMap = (HashMap) acctDtl.get(0);
                        
                                                        paramMap.put("TODAY_DT", currentDate);
                                                        //                                            Date lstproDt=DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(acctDtlMap.get("LAST_PROV_DT")));
                                                        Date lstproDt = (Date) acctDtlMap.get("LAST_PROV_DT");
                                                        //                                            Date lstIntCrDt=DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(acctDtlMap.get("LAST_INT_APPL_DT")));
                                                        Date lstIntCrDt = (Date) acctDtlMap.get("LAST_INT_APPL_DT");
                                                        //                                            Date depDt=DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(acctDtlMap.get("DEPOSIT_DT")));
                                                        Date depDt = (Date) acctDtlMap.get("DEPOSIT_DT");
                                                        //                                            Date depMatDt=DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(acctDtlMap.get("MATURITY_DT")));
                                                        Date depMatDt = (Date) acctDtlMap.get("MATURITY_DT");
                                                        //                                            Date nextCalsDt=DateUtil.addDays(lstproDt, CommonUtil.convertObjToInt(paramMap.get("INT_PROV_FREQ")));
                                                        double Tot_Int_Cr = CommonUtil.convertObjToDouble(acctDtlMap.get("TOTAL_INT_CREDIT")).doubleValue();
                                                        double Tot_Int_Dr = CommonUtil.convertObjToDouble(acctDtlMap.get("TOTAL_INT_DRAWN")).doubleValue();
                                                        double Tot_Int_AMT = CommonUtil.convertObjToDouble(acctDtlMap.get("TOT_INT_AMT")).doubleValue();
                                                        long period = DateUtil.dateDiff(depDt, depMatDt);
                                                        paramMap.put("DEPOSIT_DT", depDt);
                                                        paramMap.put("MATURITY_DT", depMatDt);
                                                        paramMap.put("AMOUNT", acctDtlMap.get("DEPOSIT_AMT"));
                                                        paramMap.put("CATEGORY_ID", acctDtlMap.get("CATEGORY"));
                                                        //                                paramMap.put("BEHAVES_LIKE","FIXED");
                                                        paramMap.put("PERIOD1", new Double(period));
                                                        paramMap.put("END", nextCalsDt);
                                                        paramMap.put("FREQ", acctDtlMap.get("FREQ"));
                                                        tdsDeductAmt = 0.0;
                                                      

                                              
                                                        if (DateUtil.dateDiff(lstIntCrDt, lstproDt) >= 0) {
                                                            paramMap.put("START", lstproDt);
                                                        } else {
                                                            paramMap.put("START", lstIntCrDt);
                                                        }
                                                        if (DateUtil.dateDiff(nxtDt, depMatDt) >= 0) {
                                                            paramMap.put("END", nxtDt);
                                                        
                                                        } else {
                                                            paramMap.put("END", depMatDt);
                                                        }
                                                        lstIntCrDt = lstproDt;
                                                        HashMap ProviMap = new HashMap();
                                                        lstIntCrDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("START")));
                                                        ProviMap.put("LAST_APPL_DT", lstIntCrDt);
                                                        ProviMap.put("INT_APPL_FREQ", acctDtlMap.get("FREQ"));
                                                        paramMap.put("PROVISIONMAP", ProviMap);
                                                        ProviMap = null;
                                                        if (DateUtil.dateDiff(depDt, nxtDt) >= 0) {
                                                            if (DateUtil.dateDiff(lstproDt, nxtDt) > 0 || DateUtil.dateDiff(depDt, lstproDt) == 0) {
                                                                if (paramMap.containsKey("BEHAVES_LIKE") && paramMap.get("BEHAVES_LIKE").equals("FIXED")) {
                                                                    if (acctDtlMap.containsKey("FREQ") && CommonUtil.convertObjToInt(acctDtlMap.get("FREQ")) == 30 && CommonUtil.convertObjToStr(paramMap.get("DISCOUNTED_RATE")).equals("Y")) {
                                                                        lstIntCrDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("START")));
                                                                        nextCalsDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("END")));
                                                                        double period1 = 0.0;
                                                                        long pr = 0;
                                                                        if (DateUtil.dateDiff(lstIntCrDt, depDt) >= 0) {
                                                                            lstIntCrDt = DateUtil.addDays(lstIntCrDt, -1);
                                                                            pr = DateUtil.dateDiff(lstIntCrDt, nextCalsDt);
                                                                            lstIntCrDt = DateUtil.addDays(lstIntCrDt, 1);

                                                                        } else {
                                                                            pr = DateUtil.dateDiff(lstIntCrDt, nextCalsDt);
                                                                            pr = pr + 1; //FOR MHCB
                                                                        }
                                                                        double roi = 0.0;
                                                                        double amount = CommonUtil.convertObjToDouble(acctDtlMap.get("DEPOSIT_AMT")).doubleValue();
                                                                        roi = CommonUtil.convertObjToDouble(acctDtlMap.get("RATE_OF_INT")).doubleValue();
                                                                        period1 = (double) pr / 30;
                                                                        interestAmt = roi / 4 / (Math.pow((1 + (roi / 1200)), 2) + (1 + (roi / 1200)) + 1);
                                                                        interestAmt = interestAmt * period1;
                                                                        double calcAmt = amount / 100;
                                                                        interestAmt = interestAmt * calcAmt;
                                                                        interestAmt = (double) getNearest((long) (interestAmt * 100), 100) / 100;
                                                                        //                                                        totProdSum = totProdSum+interestAmt;
                                                                        if (Tot_Int_AMT > 0 && DateUtil.dateDiff(nextCalsDt, depMatDt) == 0) {
                                                                            interestAmt = Tot_Int_AMT - Tot_Int_Cr;
                                                                        }
                                                                        objInterestBatchTO = new InterestBatchTO();
                                                                        objInterestBatchTO.setActNum(CommonUtil.convertObjToStr(paramMap.get("ACT_NUM")));
                                                                        //                                         objInterestBatchTO.setAcHdId(CommonUtil.convertObjToStr(paramMap.get("DEBIT_INT")));
                                                                        objInterestBatchTO.setAcHdId(CommonUtil.convertObjToStr(paramMap.get("CREDIT_INT")));
                                                                        objInterestBatchTO.setIntAmt(new Double(interestAmt));
                                                                        objInterestBatchTO.setIntRate(new Double(roi));
                                                                        objInterestBatchTO.setIntType("SIMPLE");
                                                                        objInterestBatchTO.setPrincipleAmt(CommonUtil.convertObjToDouble(acctDtlMap.get("DEPOSIT_AMT")));
                                                                        objInterestBatchTO.setIsTdsApplied("N");
                                                                    } else if (acctDtlMap.containsKey("FREQ") && CommonUtil.convertObjToInt(acctDtlMap.get("FREQ")) != 30
                                                                            || (CommonUtil.convertObjToStr(paramMap.get("DISCOUNTED_RATE")).equals("N") && CommonUtil.convertObjToInt(acctDtlMap.get("FREQ")) == 30)) {
                                                                        lstIntCrDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("START")));
                                                                        nextCalsDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("END")));
                                                                        //                                                        long pr=DateUtil.dateDiff(lstIntCrDt, nextCalsDt);
                                                                        long pr = 0;
                                                                        if (DateUtil.dateDiff(lstIntCrDt, depDt) >= 0) {
                                                                            lstIntCrDt = DateUtil.addDays(lstIntCrDt, -1);
                                                                            pr = DateUtil.dateDiff(lstIntCrDt, nextCalsDt);
                                                                            lstIntCrDt = DateUtil.addDays(lstIntCrDt, 1);

                                                                        } else {
                                                                            pr = DateUtil.dateDiff(lstIntCrDt, nextCalsDt);
                                                                            pr = pr + 1; //ADD FOR MHCB
                                                                        }
                                                                        if (CommonUtil.convertObjToInt(acctDtlMap.get("FREQ")) == 0 && DateUtil.dateDiff(depDt, lstproDt) > 0) {
                                                                            pr = pr - 1;
                                                                        }
                                                                        double rateOfInterest1 = CommonUtil.convertObjToDouble(acctDtlMap.get("RATE_OF_INT")).doubleValue();
                                                                        double principal = CommonUtil.convertObjToDouble(acctDtlMap.get("DEPOSIT_AMT")).doubleValue();
                                                                        pr = pr - (((pr * 365) - ((int) (pr * 365))) / 365);
                                                                        interestAmt = (principal * rateOfInterest1 * pr) / 36500;
                                                                        interestAmt = (double) getNearest((long) (interestAmt * 100), 100) / 100;
                                                                        if (Tot_Int_AMT > 0 && DateUtil.dateDiff(nextCalsDt, depMatDt) == 0) {
                                                                            interestAmt = Tot_Int_AMT - Tot_Int_Cr;
                                                                        }
                                                                        objInterestBatchTO = new InterestBatchTO();
                                                                       

                                                                    }
                                                                } else {
                                                                    //                                       if(!paramMap.get("BEHAVES_LIKE").equals("RECURRING")){


                                                                    if (paramMap.get("BEHAVES_LIKE").equals("CUMMULATIVE")) {
                                                                        TdsCalc tds = new TdsCalc(actBranch);
                                                                        objInterestBatchTO = new InterestBatchTO();
                                                                        double principal = 0.0;
                                                                        double rateOfInterest = 0.0;
                                                                        HashMap tdsToCalMap = new HashMap();
                                                                        tdsToCalMap.put("CLOSEDACC", CommonUtil.convertObjToStr(paramMap.get("ACT_NUM")));
                                                                        tdsToCalMap.put("CUR_DT", currentDate);
                                                                        List tdsLst = sqlMap.executeQueryForList("getAccountPROvTDS1", tdsToCalMap);
                                                                        tdsToCalMap = new HashMap();
                                                                        if (tdsLst != null && tdsLst.size() > 0) {
                                                                            tdsToCalMap = (HashMap) tdsLst.get(0);
                                                                        }

                                                                        lstIntCrDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("START")));
                                                                        nextCalsDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("END")));
                                                                        rateOfInterest = CommonUtil.convertObjToDouble(acctDtlMap.get("RATE_OF_INT")).doubleValue();
                                                                        //                                        final HashMap dataMap = (HashMap)sqlMap.executeQueryForObject("icm.getInterestRates", paramMap);
                                                                        //                                        rateOfInterest = CommonUtil.convertObjToDouble(dataMap.get("ROI")).doubleValue();
                                                                        principal = CommonUtil.convertObjToDouble(acctDtlMap.get("DEPOSIT_AMT")).doubleValue();
                                                                        //                                double period = DateUtil.dateDiff((Date)paramMap.get("START"),(Date)ServerUtil.getCurrentDate(super._branchCode));
                                                                        objInterestBatchTO.setIntRate(new Double(rateOfInterest));

                                                                        //                                                     if(DateUtil.dateDiff(lstIntCrDt,lstproDt)<=0)
                                                                        if (DateUtil.dateDiff(ltProvMdDt, lstIntCrDt) >= 0 && DateUtil.dateDiff(depDt, nxtDt) >= 0) {
                                                                            if (DateUtil.dateDiff(lstIntCrDt, ltProvMdDt) == 0 && DateUtil.dateDiff(nxtDt, depMatDt) >= 0) {//for full Quarter
                                                                                long prd = DateUtil.dateDiff(lstIntCrDt, nextCalsDt);
                                                                                if (tdsToCalMap != null) {
                                                                                    tdsToCalMap.put("OTHERDAO", "OTHERDAO");
                                                                                    tdsToCalMap.put("START1", lstIntCrDt);
                                                                                    tdsToCalMap.put("LSTPROVDT", ltProvMdDt);

                                                                                    tdsToCalMap.put("END1", nextCalsDt);
                                                                                    tdsToCalMap.put("DISCOUNTED_RATE", paramMap.get("DISCOUNTED_RATE"));
                                                                                    double TdsIntAmt = tds.totalIntrestCalculationforTds(tdsToCalMap, null, null);
                                                                                }
                                                                                double pr3 = prd / 30;
                                                                                pr3 = (double) getNearest((long) (pr3 * 100), 100) / 100;
                                                                                rateOfInterest = rateOfInterest / 100;
                                                                                principal = principal + Tot_Int_Cr;
                                                  
                                                                                double amount = principal * (Math.pow((1 + rateOfInterest / 4.0), pr3 / 12 * 4.0));

                                                                                double interest = amount - principal;
                                                                                interestAmt = interest;
                                                                                interestAmt = (double) getNearest((long) (interestAmt * 100), 100) / 100;


                                                                            } else if (DateUtil.dateDiff(lstIntCrDt, ltProvMdDt) == 0 && DateUtil.dateDiff(nxtDt, depMatDt) < 0) { //maturinging before next Prov Dt
                                                                                long DiiPeriod = DateUtil.dateDiff(lstIntCrDt, depMatDt);
                                                                                int DiiPeriod1 = (int) DiiPeriod;
                                                                                principal = principal + Tot_Int_Cr;
                                                                                interestAmt = principal * rateOfInterest * DiiPeriod / 36500;
                                                                                interestAmt = (double) getNearest((long) (interestAmt * 100), 100) / 100;
                                                                                nxtDt = depMatDt;
                                                                                if (tdsToCalMap != null) {
                                                                                    tdsToCalMap.put("OTHERDAO", "OTHERDAO");
                                                                                    tdsToCalMap.put("START1", lstIntCrDt);
                                                                                    tdsToCalMap.put("END1", depMatDt);
                                                                                    tdsToCalMap.put("LSTPROVDT", ltProvMdDt);
                                                                                    tdsToCalMap.put("DISCOUNTED_RATE", paramMap.get("DISCOUNTED_RATE"));
                                                                                    double TdsIntAmt = tds.totalIntrestCalculationforTds(tdsToCalMap, null, null);
                                                                                }

                                                                            } else if (DateUtil.dateDiff(lstIntCrDt, ltProvMdDt) < 0 && DateUtil.dateDiff(nxtDt, depMatDt) >= 0) {//opeining after LastProvision Made Date
                                                                                lstIntCrDt = DateUtil.addDays(lstIntCrDt, -1);
                                                                                long DiiPeriod = DateUtil.dateDiff(lstIntCrDt, nxtDt);
                                                                                lstIntCrDt = DateUtil.addDays(lstIntCrDt, 1);
                                                                                int DiiPeriod1 = (int) DiiPeriod;
                                                                                principal = principal + Tot_Int_Cr;
                                                                                interestAmt = principal * rateOfInterest * DiiPeriod / 36500;
                                                                                interestAmt = (double) getNearest((long) (interestAmt * 100), 100) / 100;
                                                                                if (tdsToCalMap != null) {
                                                                                    tdsToCalMap.put("OTHERDAO", "OTHERDAO");
                                                                                    tdsToCalMap.put("START1", lstIntCrDt);
                                                                                    tdsToCalMap.put("END1", nxtDt);
                                                                                    tdsToCalMap.put("LSTPROVDT", ltProvMdDt);
                                                                                    tdsToCalMap.put("DISCOUNTED_RATE", paramMap.get("DISCOUNTED_RATE"));
                                                                                    double TdsIntAmt = tds.totalIntrestCalculationforTds(tdsToCalMap, null, null);

                                                                                }



                                                                            }
                                                               
                                                                        } else if (DateUtil.dateDiff(ltProvMdDt, lstIntCrDt) < 0) {
                             
                                                                            long diff = DateUtil.dateDiff(lstIntCrDt, ltProvMdDt);

                                                                            rateOfInterest = rateOfInterest / 100;

                                                                            principal = principal + Tot_Int_Cr;
                                                                            double diffint = (principal * rateOfInterest * diff) / 365;

                                                                            diffint = (double) getNearest((long) (diffint * 100), 100) / 100;

                                                                            principal = principal + diffint;
                                                 
                                                                            double quarterDi = DateUtil.dateDiff(ltProvMdDt, nextCalsDt);
                                                                            quarterDi = quarterDi / 30;

                                                                            quarterDi = (double) getNearest((long) (quarterDi * 100), 100) / 100;
                                                                            double amount = principal * (Math.pow((1 + rateOfInterest / 4.0), quarterDi / 12 * 4.0));
                                                                            interestAmt = amount - principal;
                                                                            interestAmt = interestAmt + diffint;
                                    
                                                                            interestAmt = (double) getNearest((long) (interestAmt * 100), 100) / 100;
                                                                            if (tdsToCalMap != null) {
                                                                                tdsToCalMap.put("OTHERDAO", "OTHERDAO");
                                                                                tdsToCalMap.put("START1", lstIntCrDt);
                                                                                tdsToCalMap.put("END1", nextCalsDt);
                                                                                tdsToCalMap.put("LSTPROVDT", ltProvMdDt);
                                                                                tdsToCalMap.put("DISCOUNTED_RATE", paramMap.get("DISCOUNTED_RATE"));
                                                                                double TdsIntAmt = tds.totalIntrestCalculationforTds(tdsToCalMap, null, null);
                                                                            }

                                                                        }
                                                                        if (Tot_Int_AMT > 0 && DateUtil.dateDiff(nextCalsDt, depMatDt) == 0) {
                                                                            interestAmt = Tot_Int_AMT - Tot_Int_Cr;
                                                                        }

                                                                        TransferTrans transferTrans = new TransferTrans();
                                                                        ArrayList batchList = new ArrayList();
                                                                       

                                                                    } //                        }
                                                                    else if (paramMap.get("BEHAVES_LIKE").equals("RECURRING")) {
                                                                        objInterestBatchTO = new InterestBatchTO();
                                                                   
                                                                        double rateOfInterest = CommonUtil.convertObjToDouble(acctDtlMap.get("RATE_OF_INT")).doubleValue();
                                                                        double principal = CommonUtil.convertObjToDouble(acctDtlMap.get("DEPOSIT_AMT")).doubleValue();
                                                                        double noofInstPaid = CommonUtil.convertObjToInt(acctDtlMap.get("TOTAL_INSTALL_PAID"));
                                                                        lstIntCrDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("START")));
                                                                        nextCalsDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("END")));
                                                                        double pr1 = DateUtil.dateDiff(lstIntCrDt, nextCalsDt);
                                               
                                                                        pr1 = pr1;
                                                                        double diffFo = 0;
                                                                        diffFo = pr1;
                                                                        
                                                                        double amount = principal * (Math.pow((1 + rateOfInterest / 400), noofInstPaid / 3) - 1) / (1 - Math.pow((1 + rateOfInterest / 400), -1 / 3.0));
                                                                        interestAmt = amount - (principal * noofInstPaid);
                                                                        interestAmt = interestAmt - Tot_Int_Cr;
                                                                        double totInst = CommonUtil.convertObjToInt(acctDtlMap.get("TOTAL_INSTALLMENTS"));
                                                                        if (DateUtil.dateDiff(depMatDt, nextCalsDt) >= 0 && noofInstPaid >= totInst && Tot_Int_AMT > 0) {
                                                                            interestAmt = Tot_Int_AMT - Tot_Int_Cr;
                                                                        }
                                                                        interestAmt = (double) getNearest((long) (interestAmt * 100), 100) / 100;
                                                                    }

                                                                }
                                                                if (interestAmt > 0) {
                                                                    if (paramMap.get("BEHAVES_LIKE").equals("FIXED") || paramMap.get("BEHAVES_LIKE").equals("CUMMULATIVE")) {
                                                                        TdsCalc tds = new TdsCalc(actBranch);
                                                                        String CustId = CommonUtil.convertObjToStr(paramMap.get("CUST_ID"));
                                                                        String Prod_type = CommonUtil.convertObjToStr(paramMap.get("PROD_TYPE"));
                                                                        String prod_id = CommonUtil.convertObjToStr(paramMap.get(InterestTaskRunner.PRODUCT_ID));
                                                                        String accnum = CommonUtil.convertObjToStr(paramMap.get("ACT_NUM"));
                                                                        HashMap tdsMap = new HashMap();
                                                                        Date applDt1 = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("END")));
                                                                        Date applnDt1 = (Date) currentDate.clone();

                                                                        if (applDt1.getDate() > 0) {
                                                                            applnDt1.setDate(applDt1.getDate());
                                                                            applnDt1.setMonth(applDt1.getMonth());
                                                                            applnDt1.setYear(applDt1.getYear());
                                                                        }

                                                                        tdsMap.put("INT_DATE", applnDt1);
                                                                        tdsMap.put("CUSTID", CustId);
                                                                       //comented at 06/02/2015  no need tds calculation in kerala scb banks approved by soji (with presence of jibi)
//                                                                        List exceptionList = (List) sqlMap.executeQueryForList("getTDSExceptionData", tdsMap);
//                                                                        if (exceptionList == null || exceptionList.size() <= 0) {
//                                                                            tdsMap = new HashMap();
//                                                                            tdsMap = tds.tdsCalcforInt(CustId, interestAmt, accnum, Prod_type, prod_id, null);
//
//                                                                        }
                                                                        paramMap.put("DEBIT_ACT_NUM", CommonUtil.convertObjToStr(paramMap.get("ACT_NUM")));
                                                                        if (tdsMap != null) {
                                                                            tdsDeductedForBaseAccount = CommonUtil.convertObjToDouble(tdsMap.get("TDSDEDUCTEDTOBASEACCOUNT")).doubleValue();
                                                                            if (tdsDeductedForBaseAccount > 0) {
                                                                                tdsDeductAmt = CommonUtil.convertObjToDouble(tdsMap.get("TDSDRAMT")).doubleValue();
                                                                                tdsHd = CommonUtil.convertObjToStr(tdsMap.get("TDSCrACHdId"));
                                                                                tdsDeductedForBaseAccount = CommonUtil.convertObjToDouble(tdsMap.get("TDSDEDUCTEDTOBASEACCOUNT")).doubleValue();
                                                                                paramMap.put("DEBIT_ACT_NUM", CommonUtil.convertObjToStr(tdsMap.get("DEBIT_ACT_NUM")));
                                                                            }
                                                                        }
                                                                    }

                                                                    objInterestBatchTO.setAcHdId(CommonUtil.convertObjToStr(paramMap.get("CREDIT_INT")));
                                                                    objInterestBatchTO.setIntAmt(new Double(interestAmt));
                                                                    if (paramMap.get("BEHAVES_LIKE").equals("RECURRING") || paramMap.get("BEHAVES_LIKE").equals("CUMMULATIVE")) {
                                                                        objInterestBatchTO.setIntType("CUMMULATIVE");
                                                                    } else if (paramMap.get("BEHAVES_LIKE").equals("FIXED")) {
                                                                        objInterestBatchTO.setIntType("SIMPLE");
                                                                    }
                                                                    objInterestBatchTO.setPrincipleAmt(CommonUtil.convertObjToDouble(acctDtlMap.get("DEPOSIT_AMT")));
                                                                    objInterestBatchTO.setIsTdsApplied("N");
                                                                    objInterestBatchTO.setProductType(CommonUtil.convertObjToStr(paramMap.put("PROD_TYPE", "TD")));

                                                                    if (tdsDeductedForBaseAccount > 0.0) {
                                                                        objInterestBatchTO.setIsTdsApplied("Y");
                                                                        objInterestBatchTO.setTdsAmt(new Double(tdsDeductedForBaseAccount));
                                                                        interestAmt = interestAmt - tdsDeductAmt;
                                                                        objInterestBatchTO.setIntAmt(new Double(interestAmt));
                                                                        objInterestBatchTO.setTdsDeductedFromAll(new Double(tdsDeductAmt));
                                                                        objInterestBatchTO.setTotalTdsAmt(new Double(tdsDeductedForBaseAccount));
                                                                        objInterestBatchTO.setLastTdsRecivedFrom(CommonUtil.convertObjToStr(paramMap.get("DEBIT_ACT_NUM")));
                                                                        objInterestBatchTO.setLastTdsApplDt(currentDate);

                                                                    }


                                                                    interestAmt = (double) getNearest((long) (interestAmt * 100), 100) / 100;
                                                                    double Tot_Int_bal = (interestAmt + Tot_Int_Cr) - Tot_Int_Dr;
                                                                    
                                                                    Tot_Int_bal = (double) getNearest((long) (Tot_Int_bal * 100), 100) / 100;
                                                                  
                                                                    objInterestBatchTO.setProductId(CommonUtil.convertObjToStr(paramMap.get("PROD_ID")));
                                                                  
                                                                    Date applnDt = (Date) currentDate.clone();
                                                                    Date applDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("END")));
                                                                    if (applDt.getDate() > 0) {
                                                                        applnDt.setDate(applDt.getDate());
                                                                        applnDt.setMonth(applDt.getMonth());
                                                                        applnDt.setYear(applDt.getYear());
                                                                    }
                                                                    Date currDt = (Date) currentDate.clone();
                                                                    Date stDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("START")));
                                                                    if (stDt.getDate() > 0) {
                                                                        currDt.setDate(stDt.getDate());
                                                                        currDt.setMonth(stDt.getMonth());
                                                                        currDt.setYear(stDt.getYear());
                                                                    }
                                                                  
                                                                    objInterestBatchTO.setApplDt(applnDt);
                                                                    objInterestBatchTO.setIntDt(currDt);
                                                                    objInterestBatchTO.setTrnDt(currentDate);
                                                                    objInterestBatchTO.setDrCr("CREDIT");
                                                                    objInterestBatchTO.setActNum(CommonUtil.convertObjToStr(paramMap.get("ACT_NUM")));
                                                                    objInterestBatchTO.setCustId(CommonUtil.convertObjToStr(((HashMap) accountList.get(acc)).get("CUST_ID")));
                                                                    objInterestBatchTO.setTot_int_amt(new Double(Tot_Int_bal));
                                                                    objInterestBatchTO.setTransLogId("P");
                                                                    objInterestBatchTO.setIntAmt(new Double(interestAmt + tdsDeductAmt));
                                                                    sqlMap.executeUpdate("insertDepositInterestTO", objInterestBatchTO);
                                                                  
                                                                    sqlMap.executeUpdate("updateLastInterestAmount", objInterestBatchTO);
                                                                    objInterestBatchTO.setIntAmt(new Double(interestAmt));
                                                                    HashMap updateIntDtMap = new HashMap();
                                                                    updateIntDtMap.put("applDt", nxtDt);
                                                                    updateIntDtMap.put("actNum", CommonUtil.convertObjToStr(paramMap.get("ACT_NUM")));
                                                                    sqlMap.executeUpdate("updateDepositIntLastProlDT", updateIntDtMap);
                                                                    totTdsDeductAmt = totTdsDeductAmt + tdsDeductAmt;
                                                                    totProdSum = totProdSum + interestAmt;
                                                                    if (tdsDeductAmt > 0.0) {
                                                                        objInterestBatchTO.setIntAmt(new Double(tdsDeductAmt));
                                                                        sqlMap.executeUpdate("updateTotIndDrawnAmount", objInterestBatchTO);
                                                                        objInterestBatchTO.setIntAmt(new Double(0));
                                                                    }
                                                                    updateIntDtMap = null;
                                                                }
                            
                                                            }
                                                        }
                                                        acctDtlMap = null;
                                                    }
                                                    //sqlMap.commitTransaction();

                                                } catch (Exception e) {
                                                    if (!paramMap.containsKey("CHARGES_PROCESS")) {
                                                       // sqlMap.rollbackTransaction();
                                                        //sqlMap.startTransaction();
                                                        if (taskSelected != OBCODE) {
                                                            String errMsg = "";
                                                            TTException tte = null;
                                                            HashMap exceptionMap = null;
                                                            HashMap excMap = null;
                                                            String strExc = null;
                                                            String errClassName = "";
                                                            if (e instanceof TTException) {
                                                                tte = (TTException) e;
                                                                if (tte != null) {
                                                                    exceptionMap = tte.getExceptionHashMap();
                                                                    if (exceptionMap != null) {
                                                                        ArrayList list = (ArrayList) exceptionMap.get(CommonConstants.EXCEPTION_LIST);
                                                                        errClassName = CommonUtil.convertObjToStr(exceptionMap.get(CommonConstants.CONSTANT_CLASS));
                                                                        if (list != null && list.size() > 0) {
                                                                            for (int i = 0; i < list.size(); i++) {
                                                                                if (list.get(i) instanceof HashMap) {
                                                                                    excMap = (HashMap) list.get(i);
                                                                                    strExc = CommonUtil.convertObjToStr(excMap.get(CommonConstants.ERR_MESSAGE))
                                                                                            + " (" + CommonUtil.convertObjToStr(excMap.get(CommonConstants.ERR_DATA)) + ")";
                                                                                } else {
                                                                                    strExc = (String) list.get(i);
                                                                                }
                                                                                errorMap = new HashMap();
                                                                                errorMap.put("ERROR_DATE", currentDate);
                                                                                errorMap.put("TASK_NAME", taskLable);
                                                                                errorMap.put("ERROR_MSG", strExc);
                                                                                errorMap.put("ERROR_CLASS", errClassName);
                                                                                errorMap.put("ACT_NUM", paramMap.get("ACT_NUM"));
                                                                                errorMap.put("BRANCH_ID", branch);
                                                                                //                                                                sqlMap.startTransaction();
                                                                                sqlMap.executeUpdate("insertError_showing", errorMap);
                                                                                //                                                                sqlMap.commitTransaction();
                                                                                errorMap = null;
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            } else {
                                                                e.printStackTrace();
                                                                errMsg = e.getMessage();
                                                                errorMap = new HashMap();
                                                                errorMap.put("ERROR_DATE", currentDate);
                                                                errorMap.put("TASK_NAME", taskLable);
                                                                errorMap.put("ERROR_MSG", errMsg);
                                                                errorMap.put("ERROR_CLASS", errClassName);
                                                                errorMap.put("ACT_NUM", paramMap.get("ACT_NUM"));
                                                                errorMap.put("BRANCH_ID", branch);
                                                                //                                                sqlMap.startTransaction();
                                                                sqlMap.executeUpdate("insertError_showing", errorMap);
                                                                //                                                sqlMap.commitTransaction();
                                                                errorMap = null;
                                                            }
                                                            status.setStatus(BatchConstants.ERROR);
                                                          
                                                            tte = null;
                                                            exceptionMap = null;
                                                            excMap = null;
                                                        }
                                                        e.printStackTrace();
                                                        //sqlMap.commitTransaction();
                                                    } else {
                                                        sqlMap.rollbackTransaction();
                                                        e.printStackTrace();
                                                    }
                                                }
                                                nxtDt = lstDt;
                                            }
                                        }
                                        if (totProdSum + totTdsDeductAmt > 0.0) {
                                            //sqlMap.startTransaction();
                                            HashMap proMap = new HashMap();
                                            proMap.put("lstproDt", lstDt);
                                            proMap.put(CommonConstants.BRANCH_ID, actBranch);
                                            proMap.put("PROD_ID", paramMap.get("PROD_ID"));
                                            if (!paramMap.containsKey("ACT_TO")) {
                                                sqlMap.executeUpdate("updateDepositprovision", proMap);
                                            }
                                            HashMap resultMap = new HashMap();
                                            TransferTrans transferTrans = new TransferTrans();
                                            ArrayList batchList = new ArrayList();


                                            resultMap = prepareMap(paramMap, resultMap);
                                            resultMap.put("LINK_BATCH_ID", paramMap.get("ACT_NUM"));
                                            batchList.add(transferTrans.getDebitTransferTO(resultMap, totProdSum + totTdsDeductAmt));
                                            batchList.add(transferTrans.getCreditTransferTO(resultMap, totProdSum));
                                            HashMap dataMap = new HashMap();
                                            if (totTdsDeductAmt > 0) {
                                                HashMap txMap = new HashMap();
                                                txMap.put(TransferTrans.CR_AC_HD, tdsHd);
                                                txMap.put(TransferTrans.CR_PROD_TYPE, "GL");
                                                txMap.put(TransferTrans.CURRENCY, "INR");
                                                txMap.put(TransferTrans.PARTICULARS, CommonUtil.convertObjToStr(paramMap.get("ACT_NUM")) + "TdsDeducted");
                                                txMap.put(TransferTrans.CR_BRANCH, actBranch);
                                                txMap.put("generateSingleTransId",generateSingleTransId);
                                       
                                                batchList.add(transferTrans.getCreditTransferTO(txMap, totTdsDeductAmt));
                                            }




                              
                                            transferTrans.setInitiatedBranch(actBranch);
                                            transferTrans.doDebitCredit(batchList, actBranch);
                                           // sqlMap.commitTransaction();
                                            proMap = null;
                                            resultMap = null;
                                            transferTrans = null;
                                            batchList = null;
                                        }
                                    }
                                    accountList = null;
                                }
                                if (!paramMap.containsKey("CHARGES_PROCESS")) {
                                    if (status.getStatus() != BatchConstants.ERROR) {
                                      //  sqlMap.startTransaction();
                                        if (compStatus.equals("ERROR")) {
                                            HashMap statusMap = new HashMap();
                                            //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
                                            statusMap.put("BRANCH_CODE", actBranch);
                                            statusMap.put("TASK_NAME", taskLable);
                                            statusMap.put("TASK_STATUS", "COMPLETED");
                                            statusMap.put("USER_ID", getHeader().getUserID());
                                            statusMap.put("DAYEND_DT", currentDate);
                                            sqlMap.executeUpdate("updateTskStatus", statusMap);
                                            statusMap = null;
                                        } else {
                                            HashMap statusMap = new HashMap();
                                            //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
                                            statusMap.put("BRANCH_CODE", actBranch);
                                            statusMap.put("TASK_NAME", taskLable);
                                            statusMap.put("TASK_STATUS", "COMPLETED");
                                            statusMap.put("USER_ID", getHeader().getUserID());
                                            statusMap.put("DAYEND_DT", currentDate);
                                            sqlMap.executeUpdate("InsertDayEndStatus", statusMap);
                                            statusMap = null;
                                        }
                                       // sqlMap.commitTransaction();
                                    } else {
                                       // sqlMap.startTransaction();
                                        if (compStatus.equals("ERROR")) {
                                            HashMap statusMap = new HashMap();
                                            //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
                                            statusMap.put("BRANCH_CODE", actBranch);
                                            statusMap.put("TASK_NAME", taskLable);
                                            statusMap.put("TASK_STATUS", "ERROR");
                                            statusMap.put("USER_ID", getHeader().getUserID());
                                            statusMap.put("DAYEND_DT", currentDate);
                                            sqlMap.executeUpdate("updateTskStatus", statusMap);
                                            statusMap = null;
                                        } else {
                                            //                            isError = true;
                                            HashMap statusMap = new HashMap();
                                            //                                (#BRANCH_CODE#, #TASK_NAME#, #TASK_STATUS#, #USER_ID#, #DAYEND_DT#)
                                            statusMap.put("BRANCH_CODE", actBranch);
                                            statusMap.put("TASK_NAME", taskLable);
                                            statusMap.put("TASK_STATUS", "ERROR");
                                            statusMap.put("USER_ID", getHeader().getUserID());
                                            statusMap.put("DAYEND_DT", currentDate);
                                            sqlMap.executeUpdate("InsertDayEndStatus", statusMap);
                                            statusMap = null;

                                        }
                                       // sqlMap.commitTransaction();
                                    }
                                }
                            }
                        }
                    } else {
                        System.out.println("No Records In DepositProvision Table for this Product " + paramMap.get("PROD_ID"));
                    }
                    ntlst = null;
                }
            }
            productList = null;
            //                paramMap=null;
            if (status.getStatus() != BatchConstants.ERROR) {
                status.setStatus(BatchConstants.COMPLETED);
            }

        }
        paramMap = null;
    }

    private void dointCash(double intTrfAmt, HashMap paramMap, HashMap tdsMap) throws Exception {
        System.out.println("inside dointCash :: " + intTrfAmt +"tdsMap :: " + tdsMap);
        //Changed By Suresh
        double tdsAmount = 0.0;
        String startDate = DateUtil.getStringDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("START"))));
        String endDate = CommonUtil.convertObjToStr(paramMap.get("INT_CALC_UPTO_DT"));//vivek
        ArrayList trfLst = new ArrayList();
        TxTransferTO objTxTransferTO = new TxTransferTO();
        TransferTrans trans = new TransferTrans();
        trans.setInitiatedBranch(actBranch);
        HashMap txMap = new HashMap();
        if(tdsMap != null && tdsMap.containsKey("TDSDRAMT") && CommonUtil.convertObjToDouble(tdsMap.get("TDSDRAMT")) > 0){
            tdsAmount = CommonUtil.convertObjToDouble(tdsMap.get("TDSDRAMT"));
        }
        //if (tdsMap != null) {
          if(tdsAmount > 0){
            txMap = new HashMap();
            txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(tdsMap.get("TDSCrACHdId")));
            txMap.put(TransferTrans.CR_PROD_TYPE, "GL");
            txMap.put(TransferTrans.CURRENCY, "INR");
            txMap.put(TransferTrans.PARTICULARS, CommonUtil.convertObjToStr(paramMap.get("ACT_NUM")) + "TdsDeducted");
            txMap.put(TransferTrans.CR_BRANCH, actBranch);
            txMap.put(CommonConstants.USER_ID, paramMap.get(CommonConstants.USER_ID));
            txMap.put("generateSingleTransId",generateSingleTransId);
            //double tdsDeductAmt=CommonUtil.convertObjToDouble(tdsMap.get("TDSDRAMT")).doubleValue();
            double tdsDeductAmt = tdsAmount;
            txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);
            TransferTrans transferTrans = new TransferTrans();
            trfLst.add(trans.getCreditTransferTO(txMap, tdsDeductAmt));
            intTrfAmt = intTrfAmt - tdsDeductAmt;
            txMap = new HashMap();
            txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(paramMap.get(InterestTaskRunner.CREDIT_INT)));
            txMap.put(TransferTrans.DR_BRANCH, actBranch);
            txMap.put(TransferTrans.DR_PROD_TYPE,"GL");
            txMap.put("generateSingleTransId",generateSingleTransId);
            //Changed By Suresh
//            txMap.put(TransferTrans.PARTICULARS, CommonUtil.convertObjToStr(paramMap.get("ACT_NUM"))+"intPaid");
            txMap.put(TransferTrans.PARTICULARS, "Fd Interest " + paramMap.get("ACT_NUM") + " From " + startDate + " To " + endDate);
            txMap.put(TransferTrans.CURRENCY, "INR");
            txMap.put(CommonConstants.USER_ID, paramMap.get(CommonConstants.USER_ID));
            //       txMap.put(TransferTrans.DR_BRANCH,branch);
            trans.setInitiatedBranch(actBranch);
            txMap.put("TRANS_MOD_TYPE",TransactionFactory.DEPOSITS);
            trfLst.add(trans.getDebitTransferTO(txMap, tdsDeductAmt));
            trans.doDebitCredit(trfLst, actBranch);
        }
        if (intTrfAmt > 0) {
            CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();
            HashMap cashTransMap = new HashMap();
            cashTransMap.put("ACC_HEAD", paramMap.get(InterestTaskRunner.CREDIT_INT));
            cashTransMap.put("PARTICULARS", "");
            cashTransMap.put("AMOUNT", String.valueOf(intTrfAmt));
            cashTransMap.put(CommonConstants.BRANCH_ID, actBranch);
            ArrayList cashList = new ArrayList();
            CashTransactionTO objCashTO = new CashTransactionTO();
            objCashTO.setTransId("");
            objCashTO.setProdType("GL");
            objCashTO.setTransType(CommonConstants.DEBIT);
            objCashTO.setInitTransId(CommonUtil.convertObjToStr(paramMap.get(CommonConstants.USER_ID)));
            objCashTO.setBranchId(CommonUtil.convertObjToStr(paramMap.get("ACT_NUM")).substring(0,4));
            objCashTO.setStatusBy(CommonUtil.convertObjToStr(paramMap.get(CommonConstants.USER_ID)));
            objCashTO.setStatusDt(currentDate);
          //  objCashTO.setTokenNo(CommonUtil.convertObjToStr(paramMap.get("TOKEN_NO")));
            objCashTO.setInstrumentNo1("INTEREST_AMT");
            objCashTO.setSingleTransId(generateSingleTransId);
    
            //Changed By Suresh
            objCashTO.setParticulars("Fd Interest " + paramMap.get("ACT_NUM") + " From " + startDate + " To " + endDate);
//            objCashTO.setParticulars("To,Fd Interest "+paramMap.get("ACT_NUM")+"intPaid");
            objCashTO.setInitiatedBranch(actBranch);
            objCashTO.setInitChannType(CommonConstants.CASHIER);
            objCashTO.setCommand(CommonConstants.TOSTATUS_INSERT);
            objCashTO.setAcHdId(CommonUtil.convertObjToStr(paramMap.get(InterestTaskRunner.CREDIT_INT)));
            objCashTO.setInpAmount(new Double(intTrfAmt));
            objCashTO.setAmount(new Double(intTrfAmt));
            objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(paramMap.get("ACT_NUM")));
            //added by rishad 03/07/2015
            objCashTO.setTokenNo(strCashId);
            if (paramMap.containsKey("SCREEN_NAME") && paramMap.get("SCREEN_NAME").equals("DepositInterestApp")) {
                objCashTO.setScreenName("DepositInterestApp");
            }
            if (paramMap.containsKey("CHEQUENO") && paramMap.get("CHEQUENO")!=null) {
                objCashTO.setInstrumentNo2(CommonUtil.convertObjToStr(paramMap.get("CHEQUENO")));
            }
            
          //  objCashTO.setScreenName("Deposit_Interest_Application");
            objCashTO.setTransModType(TransactionFactory.DEPOSITS);
            cashList.add(objCashTO);
            objCashTO = null;
            cashTransMap.put("DAILYDEPOSITTRANSTO", cashList);
            cashTransMap.put(CommonConstants.BRANCH_ID, branch);
            HashMap cashMap = cashTransactionDAO.execute(cashTransMap, false);
            paramMap.put(paramMap.get("ACT_NUM"), cashMap.get(CommonConstants.TRANS_ID));
            cashTransactionDAO = null;
            cashList.clear();

        }

    }
    ArrayList cashListInt = new ArrayList();
    HashMap cashTransMapInt = new HashMap();
    CashTransactionDAO cashTransactionDAO = new CashTransactionDAO();

    private void dointCashInterest(double intTrfAmt, HashMap paramMap, HashMap tdsMap) throws Exception {
        //Changed By Suresh
        System.out.println("inside dointCashInterest :: " + intTrfAmt +"tdsMap :: " + tdsMap);
        double tdsAmount = 0.0;
        String startDate = DateUtil.getStringDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("START"))));
        String endDate = CommonUtil.convertObjToStr(paramMap.get("INT_CALC_UPTO_DT"));//vivek
        ArrayList trfLst = new ArrayList();
        TxTransferTO objTxTransferTO = new TxTransferTO();
        TransferTrans trans = new TransferTrans();
        trans.setInitiatedBranch(actBranch);
        HashMap txMap = new HashMap();
        if(tdsMap != null && tdsMap.containsKey("TDSDRAMT") && CommonUtil.convertObjToDouble(tdsMap.get("TDSDRAMT")) > 0){
            tdsAmount = CommonUtil.convertObjToDouble(tdsMap.get("TDSDRAMT"));
        }
        //if (tdsMap != null) {
         if(tdsAmount > 0){
            txMap = new HashMap();
            txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(tdsMap.get("TDSCrACHdId")));
            txMap.put(TransferTrans.CR_PROD_TYPE, "GL");
            txMap.put(TransferTrans.CURRENCY, "INR");
            txMap.put(TransferTrans.PARTICULARS, CommonUtil.convertObjToStr(paramMap.get("ACT_NUM")) + "TdsDeducted");
            txMap.put(TransferTrans.CR_BRANCH, actBranch);
            txMap.put(CommonConstants.USER_ID, paramMap.get(CommonConstants.USER_ID));
            txMap.put("generateSingleTransId",generateSingleTransId);
            txMap.put("TRANS_MOD_TYPE",TransactionFactory.DEPOSITS);
            // double tdsDeductAmt=CommonUtil.convertObjToDouble(tdsMap.get("TDSDRAMT")).doubleValue();
            double tdsDeductAmt = tdsAmount;
            TransferTrans transferTrans = new TransferTrans();
            trfLst.add(trans.getCreditTransferTO(txMap, tdsDeductAmt));
            intTrfAmt = intTrfAmt - tdsDeductAmt;
            txMap = new HashMap();
            txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(paramMap.get(InterestTaskRunner.CREDIT_INT)));
            txMap.put(TransferTrans.DR_BRANCH, actBranch);
            txMap.put(TransferTrans.DR_PROD_TYPE,"GL");
            txMap.put("generateSingleTransId",generateSingleTransId);
            //Changed By Suresh
//            txMap.put(TransferTrans.PARTICULARS, CommonUtil.convertObjToStr(paramMap.get("ACT_NUM"))+"intPaid");
            txMap.put(TransferTrans.PARTICULARS, "Fd Interest " + paramMap.get("ACT_NUM") + " From " + startDate + " To " + endDate);
            txMap.put(TransferTrans.CURRENCY, "INR");
            txMap.put(CommonConstants.USER_ID, paramMap.get(CommonConstants.USER_ID));
            txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);
            trans.setInitiatedBranch(actBranch);
            trfLst.add(trans.getDebitTransferTO(txMap, tdsDeductAmt));
            trans.doDebitCredit(trfLst, actBranch);
        }
        if (intTrfAmt > 0) {
            cashTransMapInt.put("ACC_HEAD", paramMap.get(InterestTaskRunner.CREDIT_INT));
            cashTransMapInt.put("PARTICULARS", "");
            cashTransMapInt.put("AMOUNT", String.valueOf(intTrfAmt));
            cashTransMapInt.put(CommonConstants.BRANCH_ID, actBranch);

            CashTransactionTO objCashTO = new CashTransactionTO();
            objCashTO.setTransId("");
            objCashTO.setProdType("GL");
            objCashTO.setTransType(CommonConstants.DEBIT);
            objCashTO.setInitTransId(CommonUtil.convertObjToStr(paramMap.get(CommonConstants.USER_ID)));
            objCashTO.setBranchId(CommonUtil.convertObjToStr(paramMap.get("ACT_NUM")).substring(0,4));
          //  objCashTO.setBranchId(actBranch);
            objCashTO.setStatusBy(CommonUtil.convertObjToStr(paramMap.get(CommonConstants.USER_ID)));
            objCashTO.setStatusDt(currentDate);
       //     objCashTO.setTokenNo(CommonUtil.convertObjToStr(paramMap.get("TOKEN_NO")));
            objCashTO.setInstrumentNo1("INTEREST_AMT");
            //Changed By Suresh
            objCashTO.setParticulars("Fd Interest " + paramMap.get("ACT_NUM") + " From " + startDate + " To " + endDate);
            objCashTO.setInitiatedBranch(actBranch);
            objCashTO.setInitChannType(CommonConstants.CASHIER);
            objCashTO.setCommand(CommonConstants.TOSTATUS_INSERT);
            objCashTO.setAcHdId(CommonUtil.convertObjToStr(paramMap.get(InterestTaskRunner.CREDIT_INT)));
            objCashTO.setInpAmount(new Double(intTrfAmt));
            objCashTO.setAmount(new Double(intTrfAmt));
            objCashTO.setLinkBatchId(CommonUtil.convertObjToStr(paramMap.get("ACT_NUM")));
            //added by rishad for rollback purpose
            if (paramMap.containsKey("SCREEN_NAME") && paramMap.get("SCREEN_NAME").equals("DepositInterestApp")) {
                objCashTO.setScreenName("DepositInterestApp");
            }
             if (paramMap.containsKey("CHEQUENO") && paramMap.get("CHEQUENO")!=null) {
                objCashTO.setInstrumentNo2(CommonUtil.convertObjToStr(paramMap.get("CHEQUENO")));
            }
            objCashTO.setTokenNo(strCashId);
            objCashTO.setSingleTransId(generateSingleTransId);
            objCashTO.setTransModType(TransactionFactory.DEPOSITS);
            
            cashListInt.add(objCashTO);
            objCashTO = null;


        }

    }

    //Added By Suresh
    public InvestmentsTransTO getInvestmentsTransTO(String command, double intTrfAm, HashMap acctDtlMap) {
        HashMap whereMap = new HashMap();
        InvestmentsTransTO objgetInvestmentsTransTO = new InvestmentsTransTO();
        List invList = ServerUtil.executeQuery("getInvestmentDetails", acctDtlMap);
        if (invList != null && invList.size() > 0) {
            whereMap = (HashMap) invList.get(0);
            objgetInvestmentsTransTO.setCommand(command);
            objgetInvestmentsTransTO.setStatus(CommonConstants.STATUS_CREATED);
            objgetInvestmentsTransTO.setInvestmentBehaves(CommonUtil.convertObjToStr(whereMap.get("INVESTMENT_TYPE")));
            objgetInvestmentsTransTO.setInvestmentID(CommonUtil.convertObjToStr(whereMap.get("INVESTMENT_PROD_ID")));
            objgetInvestmentsTransTO.setInvestment_internal_Id(CommonUtil.convertObjToStr(whereMap.get("INVESTMENT_ID")));
            objgetInvestmentsTransTO.setInvestment_Ref_No(CommonUtil.convertObjToStr(whereMap.get("INVESTMENT_REF_NO")));
            objgetInvestmentsTransTO.setInvestmentName(CommonUtil.convertObjToStr(whereMap.get("INVESTMENT_PROD_DESC")));
            objgetInvestmentsTransTO.setTransDT(currentDate);
            objgetInvestmentsTransTO.setTransType("DEBIT");
            objgetInvestmentsTransTO.setTrnCode("Withdrawal");
            objgetInvestmentsTransTO.setAmount(new Double(0.0));
            objgetInvestmentsTransTO.setPurchaseDt(currentDate);
            objgetInvestmentsTransTO.setInvestmentAmount(new Double(intTrfAm));
            objgetInvestmentsTransTO.setStatusBy(getHeader().getUserID());
            objgetInvestmentsTransTO.setStatusDt(currentDate);
            objgetInvestmentsTransTO.setDividendAmount(new Double(0));
            objgetInvestmentsTransTO.setLastIntPaidDate(currentDate);
            objgetInvestmentsTransTO.setInitiatedBranch(actBranch);
            if (acctDtlMap.containsKey("BATCH_ID")) {
                objgetInvestmentsTransTO.setBatchID(CommonUtil.convertObjToStr(acctDtlMap.get("BATCH_ID")));
            }
        }
        return objgetInvestmentsTransTO;
    }
 public Date getProperFormatDate(Object obj) {
        Date dt = null;

        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            dt = (Date) currentDate.clone();
            dt.setDate(tempDt.getDate());
            dt.setMonth(tempDt.getMonth());
            dt.setYear(tempDt.getYear());
        }
        return dt;
    }
    private void dointTransferToAccount(HashMap acctDtlMap, double intTrfAmt, HashMap paramMap, HashMap tdsMap) throws Exception {
        //Changed By Suresh
        //System.out.println("tdsMap in dointTransferToAccount :: " + tdsMap);
        //System.out.println("intTrfAmt inside dointTransferToAccount " + intTrfAmt);
        String startDate = DateUtil.getStringDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("START"))));
        String endDate = DateUtil.getStringDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("END"))));
        double pstAmt = CommonUtil.convertObjToDouble(acctDtlMap.get("POSTAGE_AMT")).doubleValue();
        String achd = CommonUtil.convertObjToStr(acctDtlMap.get("POSTAGE_ACHD"));
        double renewPstAmt = CommonUtil.convertObjToDouble(acctDtlMap.get("RENEW_POSTAGE_AMT")).doubleValue();

        if (!acctDtlMap.get("INT_PAY_PROD_TYPE").equals("RM")) {
            //Added By Suresh
            String crBranchId="";
			HashMap interBranchCodeMap = new HashMap();
			interBranchCodeMap.put("ACT_NUM", CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_ACC_NO")));
			List interBranchCodeList = sqlMap.executeQueryForList("getSelectInterBranchCode", interBranchCodeMap);
			if (interBranchCodeList != null && interBranchCodeList.size() > 0) {
				interBranchCodeMap = (HashMap) interBranchCodeList.get(0);
				crBranchId = CommonUtil.convertObjToStr(interBranchCodeMap.get("BRANCH_CODE"));
			}
            if (acctDtlMap.get("INT_PAY_PROD_TYPE").equals("INV")) {
                if (intTrfAmt > 0) {
                    HashMap dataMap = new HashMap();
                    LinkedHashMap transMap = new LinkedHashMap();
                    LinkedHashMap notDelMap = new LinkedHashMap();
                    dataMap.put(CommonConstants.USER_ID, getHeader().getUserID());
                    dataMap.put(CommonConstants.BRANCH_ID, actBranch);
                    dataMap.put("InvestmentsTransTO", getInvestmentsTransTO(CommonConstants.TOSTATUS_INSERT, intTrfAmt, acctDtlMap));
                    TransactionTO transfer = new TransactionTO();
                    transfer.setTransType("TRANSFER");
                    if (pstAmt > 0.0 || renewPstAmt > 0.0) {
                        if (pstAmt > 0.0) {
                            dataMap.put("POSTAGE_AMT", CommonUtil.convertObjToStr(new Double(pstAmt)));
                            dataMap.put("POSTAGE_ACHD", achd);
                        }
                        if (renewPstAmt > 0.0) {
                            dataMap.put("RENEW_POSTAGE_AMT", CommonUtil.convertObjToStr(new Double(renewPstAmt)));
                            dataMap.put("POSTAGE_ACHD", achd);
                        }
                    }
                    transfer.setTransAmt(new Double(intTrfAmt));
                    transfer.setProductType("GL");
                    transfer.setDebitAcctNo(CommonUtil.convertObjToStr(paramMap.get(InterestTaskRunner.CREDIT_INT)));
                    transfer.setApplName(CommonUtil.convertObjToStr(paramMap.get(InterestTaskRunner.CREDIT_INT)));
                    transfer.setInstType("VOUCHER");
                    transfer.setCommand("INSERT");
                    notDelMap.put(String.valueOf(1), transfer);//"NOT_DELETED_TRANS_TOs"
                    transMap.put("NOT_DELETED_TRANS_TOs", notDelMap);
                    dataMap.put("TransactionTO", transMap);
                    dataMap.put("FROM_INTEREST_TASK", "FROM_INTEREST_TASK");
                    InvestmentsTransDAO investmentDAO = new InvestmentsTransDAO();
                    HashMap transDetMap = investmentDAO.execute(dataMap);
                    //Authorization
                    if (transDetMap != null && transDetMap.size() > 0 && transDetMap.containsKey("BATCH_ID")) {
                        HashMap whereMap = new HashMap();
                        dataMap = new HashMap();
                        String batchID = "";
                        whereMap.put("BATCH_ID", transDetMap.get("BATCH_ID"));
                        acctDtlMap.put("BATCH_ID", transDetMap.get("BATCH_ID"));
                        dataMap.put(CommonConstants.USER_ID, getHeader().getUserID());
                        dataMap.put(CommonConstants.BRANCH_ID, actBranch);
                        dataMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
                        transfer = new TransactionTO();
                        transfer.setTransType("TRANSFER");
                        transfer.setTransAmt(new Double(intTrfAmt));
                        transfer.setProductType("GL");
                        transfer.setDebitAcctNo(CommonUtil.convertObjToStr(paramMap.get(InterestTaskRunner.CREDIT_INT)));
                        transfer.setApplName(CommonUtil.convertObjToStr(paramMap.get(InterestTaskRunner.CREDIT_INT)));
                        transfer.setInstType("VOUCHER");
                        transfer.setBatchId(CommonUtil.convertObjToStr(transDetMap.get("BATCH_ID")));
                        transfer.setTransId(CommonUtil.convertObjToStr(transDetMap.get("BATCH_ID")));
                        batchID = CommonUtil.convertObjToStr(transDetMap.get("BATCH_ID"));
                        transfer.setBatchDt(currentDate);
                        notDelMap.put(String.valueOf(1), transfer);//"NOT_DELETED_TRANS_TOs"
                        transMap.put("NOT_DELETED_TRANS_TOs", notDelMap);
                        dataMap.put("TransactionTO", transMap);
                        dataMap.put("FROM_INTEREST_TASK", "FROM_INTEREST_TASK");
                        ArrayList arrList = new ArrayList();
                        HashMap authDataMap = new HashMap();
                        HashMap singleAuthorizeMap = new HashMap();
                        authDataMap.put(CommonConstants.USER_ID, getHeader().getUserID());
                        authDataMap.put("BATCH_ID", transDetMap.get("BATCH_ID"));
                        arrList.add(authDataMap);
                        singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
                        singleAuthorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
                        singleAuthorizeMap.put("InvestmentsTransTO", getInvestmentsTransTO(CommonConstants.STATUS_AUTHORIZED, intTrfAmt, acctDtlMap));
                        singleAuthorizeMap.put(CommonConstants.USER_ID, getHeader().getUserID());
                        dataMap.put(CommonConstants.AUTHORIZEMAP, singleAuthorizeMap);
                        investmentDAO = new InvestmentsTransDAO();
                        if(acctDtlMap.containsKey("INTEREST_APPL_TRANSFER_CUST_ID")){
                            dataMap.put("INTEREST_APPL_TRANSFER_CUST_ID",CommonUtil.convertObjToStr(acctDtlMap.get("INTEREST_APPL_TRANSFER_CUST_ID")));
                        }
                        transDetMap = investmentDAO.execute(dataMap);
                        if (transDetMap != null && transDetMap.size() > 0 && transDetMap.containsKey("FROM_INTEREST_TASK_COMPLETED")) {
                            paramMap.put(paramMap.get("ACT_NUM"), batchID);
                        }
                    }
                }
            } else {
                ArrayList trfLst = new ArrayList();
                TxTransferTO objTxTransferTO = new TxTransferTO();
                TransferTrans trans = new TransferTrans();
                trans.setInitiatedBranch(actBranch);
                HashMap txMap = new HashMap();
                txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(paramMap.get(InterestTaskRunner.CREDIT_INT)));
              //  txMap.put(TransferTrans.DR_BRANCH, actBranch);
                txMap.put(TransferTrans.DR_BRANCH, CommonUtil.convertObjToStr(paramMap.get("ACT_NUM")).substring(0,4));
                txMap.put(TransferTrans.DR_PROD_TYPE,"GL");
                txMap.put(TransferTrans.PARTICULARS, acctDtlMap.get("INT_PAY_ACC_NO")+" Fd Interest "+paramMap.get("ACT_NUM")+" From " +startDate+" To "+endDate);
                txMap.put(TransferTrans.CURRENCY,"INR");
                txMap.put("generateSingleTransId",generateSingleTransId);
                txMap.put("TRANS_MOD_TYPE",TransactionFactory.DEPOSITS);
                txMap.put(CommonConstants.USER_ID,CommonUtil.convertObjToStr(paramMap.get("USER_ID")));
                trans.setInitiatedBranch(actBranch);
                objTxTransferTO = trans.getDebitTransferTO(txMap, intTrfAmt);
                objTxTransferTO.setInstrumentNo1("INTEREST_AMT");
                //added by rishad for rollback purpose
                if (paramMap.containsKey("SCREEN_NAME") && paramMap.get("SCREEN_NAME").equals("DepositInterestApp")) {
                    objTxTransferTO.setScreenName("DepositInterestApp");
                }
                 if (paramMap.containsKey("CHEQUENO") && paramMap.get("CHEQUENO")!=null) {
                 objTxTransferTO.setInstrumentNo2(CommonUtil.convertObjToStr(paramMap.get("CHEQUENO")));
                 }
                objTxTransferTO.setSingleTransId(generateSingleTransId);
                trfLst.add(objTxTransferTO);
                if (tdsMap != null) {
                    txMap = new HashMap();
                    txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(tdsMap.get("TDSCrACHdId")));
                    txMap.put(TransferTrans.CR_PROD_TYPE, "GL");
                    txMap.put(TransferTrans.CURRENCY, "INR");
                    txMap.put(TransferTrans.PARTICULARS, CommonUtil.convertObjToStr(paramMap.get("ACT_NUM")) + "TdsDeducted");
                    txMap.put(TransferTrans.CR_BRANCH, actBranch);
                    txMap.put("generateSingleTransId",generateSingleTransId);
                    txMap.put("TRANS_MOD_TYPE", TransactionFactory.DEPOSITS);
                    txMap.put(CommonConstants.USER_ID,CommonUtil.convertObjToStr(paramMap.get("USER_ID")));
                    double tdsDeductAmt=CommonUtil.convertObjToDouble(tdsMap.get("TDSDRAMT")).doubleValue();
                    TransferTrans transferTrans = new TransferTrans();
                    trfLst.add(trans.getCreditTransferTO(txMap, tdsDeductAmt));
                    intTrfAmt = intTrfAmt - tdsDeductAmt;
                }
                txMap = new HashMap();
                if (intTrfAmt > 0) {
                    if (!acctDtlMap.get("INT_PAY_PROD_TYPE").equals("GL")) {
                        if (acctDtlMap.get("INT_PAY_PROD_TYPE").equals("TL")) {
                            HashMap loanMap = interestCalculationTLAD(CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_ACC_NO")),
                                    CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_PROD_ID")));
                            loanMap.put("LOAN_FROM_CHARGESUI", "");
                            trans.setAllAmountMap(loanMap);
                        }
                        txMap.put(TransferTrans.CR_ACT_NUM, CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_ACC_NO")));
                        txMap.put(TransferTrans.CR_PROD_ID, CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_PROD_ID")));
                        txMap.put(TransferTrans.CR_PROD_TYPE, CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_PROD_TYPE")));
                    } else {
                        txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_ACC_NO")));
                        txMap.put(TransferTrans.CR_PROD_TYPE, CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_PROD_TYPE")));
                    }
                    txMap.put(TransferTrans.CURRENCY,"INR");
                    txMap.put(TransferTrans.PARTICULARS, "Fd Interest "+paramMap.get("ACT_NUM")+" From " +startDate+" To "+endDate);
					if(!crBranchId.equals("") && crBranchId.length()>0){
						txMap.put(TransferTrans.CR_BRANCH,crBranchId);
					}else{
						txMap.put(TransferTrans.CR_BRANCH,actBranch);
					}
                    txMap.put("generateSingleTransId",generateSingleTransId);
                    txMap.put("TRANS_MOD_TYPE", CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_PROD_TYPE")));
                    if(pstAmt>0.0 || renewPstAmt>0.0){
                        if(pstAmt>0.0){
                            objTxTransferTO = trans.getCreditTransferTO(txMap,intTrfAmt-pstAmt);
                        }else if(renewPstAmt> 0.0){
                            objTxTransferTO = trans.getCreditTransferTO(txMap,intTrfAmt-renewPstAmt);
                        }
                        objTxTransferTO.setInstrumentNo1("INTEREST_AMT");
                        //added by rishad for rollback purpose
                        if (paramMap.containsKey("SCREEN_NAME") && paramMap.get("SCREEN_NAME").equals("DepositInterestApp")) {
                            objTxTransferTO.setScreenName("DepositInterestApp");
                        }
                        if (paramMap.containsKey("CHEQUENO") && paramMap.get("CHEQUENO") != null) {
                            objTxTransferTO.setInstrumentNo2(CommonUtil.convertObjToStr(paramMap.get("CHEQUENO")));
                        }
                        objTxTransferTO.setSingleTransId(generateSingleTransId);
                        trfLst.add(objTxTransferTO);
                        if(pstAmt>0.0){
                            txMap.put(TransferTrans.CR_AC_HD,achd);
                            txMap.put(TransferTrans.CR_PROD_TYPE,"GL");
                            txMap.put(TransferTrans.CURRENCY,"INR");
                            txMap.put(TransferTrans.PARTICULARS, CommonUtil.convertObjToStr(paramMap.get("ACT_NUM"))+"POSTAGE");
                            txMap.put(TransferTrans.CR_BRANCH,actBranch);
                            txMap.put("generateSingleTransId",generateSingleTransId);
                            txMap.put("TRANS_MOD_TYPE",TransactionFactory.GL);
                            txMap.put(CommonConstants.USER_ID,CommonUtil.convertObjToStr(paramMap.get("USER_ID")));
                            objTxTransferTO=trans.getCreditTransferTO(txMap,pstAmt);
                            objTxTransferTO.setSingleTransId(generateSingleTransId);
                            trfLst.add(objTxTransferTO);
                        }else if(renewPstAmt>0.0){
                            txMap.put(TransferTrans.CR_AC_HD,achd);
                            txMap.put(TransferTrans.CR_PROD_TYPE,"GL");
                            txMap.put(TransferTrans.CURRENCY,"INR");
                            txMap.put(TransferTrans.PARTICULARS, CommonUtil.convertObjToStr(paramMap.get("ACT_NUM"))+"POSTAGE");
                            txMap.put(TransferTrans.CR_BRANCH,actBranch);
                            txMap.put("generateSingleTransId",generateSingleTransId);
                            txMap.put("TRANS_MOD_TYPE",TransactionFactory.GL);
                            txMap.put(CommonConstants.USER_ID,CommonUtil.convertObjToStr(paramMap.get("USER_ID")));
                            objTxTransferTO=trans.getCreditTransferTO(txMap,renewPstAmt);
                            objTxTransferTO.setSingleTransId(generateSingleTransId);
                            trfLst.add(objTxTransferTO);

                        }

                    } else {
                        objTxTransferTO = trans.getCreditTransferTO(txMap, intTrfAmt);
                        objTxTransferTO.setInstrumentNo1("INTEREST_AMT");
                        //added by rishad for rollback purpose
                        if (paramMap.containsKey("SCREEN_NAME") && paramMap.get("SCREEN_NAME").equals("DepositInterestApp")) {
                            objTxTransferTO.setScreenName("DepositInterestApp");
                        }
                        if (paramMap.containsKey("CHEQUENO") && paramMap.get("CHEQUENO") != null) {
                            objTxTransferTO.setInstrumentNo2(CommonUtil.convertObjToStr(paramMap.get("CHEQUENO")));
                        }
                        trfLst.add(objTxTransferTO);
                    }

                    TransferDAO transferDAO = new TransferDAO();
                    HashMap data = new HashMap();
                    data.put("TxTransferTO", trfLst);
                    data.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
                    data.put("LINK_BATCH_ID", paramMap.get("ACT_NUM"));
                    data.put("INITIATED_BRANCH", actBranch);
                    data.put(CommonConstants.BRANCH_ID, actBranch);
                    data.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(paramMap.get("USER_ID")));
                    data.put(CommonConstants.MODULE, "Transaction");
                    data.put(CommonConstants.SCREEN, "");
                    data.put("MODE", "INSERT");
                    if (trans.getAllAmountMap() != null && trans.getAllAmountMap().size() > 0) {
                        data.put("ALL_AMOUNT", trans.getAllAmountMap());
                        if (trans.getAllAmountMap().containsKey("LOAN_FROM_CHARGESUI")) {
                            data.put("LOAN_FROM_CHARGESUI", "");
                        }
                        if (trans.getAllAmountMap().containsKey("CORP_LOAN_MAP")) {
                            data.put("CORP_LOAN_MAP", trans.getAllAmountMap().get("CORP_LOAN_MAP"));
                        }
                    }
                    transferDAO.setForLoanDebitInt(false);
                
                    HashMap authorizeMap = new HashMap();
                    authorizeMap.put("BATCH_ID", null);
                    authorizeMap.put(CommonConstants.AUTHORIZESTATUS, CommonConstants.STATUS_AUTHORIZED);
                    if(acctDtlMap.containsKey("INTEREST_APPL_TRANSFER_CUST_ID")){
                        authorizeMap.put("INTEREST_APPL_TRANSFER_CUST_ID",CommonUtil.convertObjToStr(acctDtlMap.get("INTEREST_APPL_TRANSFER_CUST_ID")));
                    }
                    data.put(CommonConstants.AUTHORIZEMAP, authorizeMap);
                    HashMap transDetMap = transferDAO.execute(data, false);
                    if (transDetMap != null && transDetMap.size() > 0) {
                        paramMap.put(paramMap.get("ACT_NUM"), transDetMap.get(CommonConstants.TRANS_ID));
                    }
                }
            }
        } else {
            if (tdsMap != null) {
                TxTransferTO objTxTransferTO = new TxTransferTO();
                TransferTrans trans = new TransferTrans();
                ArrayList trfLst = new ArrayList();
                HashMap txMap = new HashMap();
                trans.setInitiatedBranch(actBranch);
                double tdsDeductAmt = CommonUtil.convertObjToDouble(tdsMap.get("TDSDRAMT")).doubleValue();
                txMap.put(TransferTrans.DR_AC_HD, CommonUtil.convertObjToStr(paramMap.get(InterestTaskRunner.CREDIT_INT)));
                txMap.put(TransferTrans.DR_BRANCH,actBranch);
                txMap.put(TransferTrans.DR_PROD_TYPE,"GL");
                txMap.put(TransferTrans.PARTICULARS, CommonUtil.convertObjToStr(paramMap.get("ACT_NUM"))+"intPaid");
                txMap.put(TransferTrans.CURRENCY,"INR");
                txMap.put("generateSingleTransId",generateSingleTransId);
                txMap.put("TRANS_MOD_TYPE",TransactionFactory.GL);
                trfLst.add(trans.getDebitTransferTO(txMap,tdsDeductAmt));
                txMap.put(TransferTrans.CR_AC_HD,CommonUtil.convertObjToStr(tdsMap.get("TDSCrACHdId")));
                txMap.put(TransferTrans.CR_PROD_TYPE,"GL");
                txMap.put(TransferTrans.CURRENCY,"INR");
                txMap.put(TransferTrans.PARTICULARS, CommonUtil.convertObjToStr(paramMap.get("ACT_NUM"))+"TdsDeducted");
                txMap.put(TransferTrans.CR_BRANCH,actBranch);
                txMap.put("generateSingleTransId",generateSingleTransId);
                txMap.put("TRANS_MOD_TYPE",TransactionFactory.GL);
                txMap.put(CommonConstants.USER_ID,CommonUtil.convertObjToStr(paramMap.get("USER_ID")));
                TransferTrans transferTrans = new TransferTrans();
                trfLst.add(transferTrans.getCreditTransferTO(txMap, tdsDeductAmt));
                transferTrans.doDebitCredit(trfLst, actBranch);
                intTrfAmt = intTrfAmt - tdsDeductAmt;
            }
            if (intTrfAmt > 0) {
                Date todayDt = (Date) currentDate.clone();
                HashMap dtatMap = new HashMap();
                LinkedHashMap notDelMap = new LinkedHashMap();
                LinkedHashMap notDelRemMap = new LinkedHashMap();
                dtatMap.put("MODE", "INSERT");

                TransactionTO transfer = new TransactionTO();
                transfer.setTransType("TRANSFER");
                transfer.setTransAmt(new Double(intTrfAmt));
                transfer.setProductType("GL");
                transfer.setDebitAcctNo(CommonUtil.convertObjToStr(paramMap.get(InterestTaskRunner.CREDIT_INT)));
                transfer.setApplName(CommonUtil.convertObjToStr(paramMap.get(InterestTaskRunner.CREDIT_INT)));  // Added by Rajesh
                //                transfer.setDebitAcctNo(CommonUtil.convertObjToStr(paramMap.get("ACT_NUM")));
                transfer.setInstType("VOUCHER");
                transfer.setChequeDt(todayDt);
                LinkedHashMap transMap = new LinkedHashMap();
                LinkedHashMap remMap = new LinkedHashMap();
                transfer.setCommand("INSERT");


                notDelMap.put(String.valueOf(1), transfer);//"NOT_DELETED_TRANS_TOs"
                transMap.put("NOT_DELETED_TRANS_TOs", notDelMap);
                dtatMap.put("TransactionTO", transMap);
                dtatMap.put(CommonConstants.BRANCH_ID, actBranch);
                dtatMap.put("OPERATION_MODE", "ISSUE");
                dtatMap.put("AUTHORIZEMAP", null);
                RemittanceIssueTO remt = new RemittanceIssueTO();
                remt.setDraweeBranchCode(actBranch);
                remt.setAmount(new Double(intTrfAmt));
                remt.setCategory("GENERAL_CATEGORY");

                // The following block added by Rajesh
                HashMap behavesMap = new HashMap();
                behavesMap.put("BEHAVES_LIKE", "PO");
                List lstRemit = sqlMap.executeQueryForList("selectRemitProductId", behavesMap);
                if (lstRemit != null && lstRemit.size() > 0) {
                    behavesMap = (HashMap) lstRemit.get(0);
                    remt.setProdId(CommonUtil.convertObjToStr(behavesMap.get("PROD_ID")));
                }
                HashMap draweeMap = new HashMap();
                if (behavesMap.get("PAY_ISSUE_BRANCH").equals("ISSU_BRANCH")) {
                    lstRemit = sqlMap.executeQueryForList("getSelectBankTOList", null);
                    if (lstRemit != null && lstRemit.size() > 0) {
                        draweeMap = (HashMap) lstRemit.get(0);
                    }
                }
                //                remt.setProdId(CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_PROD_ID")));  // Commented by Rajesh
                remt.setFavouring(CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_ACC_NO")));
                //                remt.setDraweeBank(branch);   // Commented by Rajesh
                remt.setDraweeBank(CommonUtil.convertObjToStr(draweeMap.get("BANK_CODE")));
                remt.setBranchId(actBranch);
                remt.setCity("560");
                remt.setInstrumentNo1("PO");
                remt.setCommand("INSERT");
                remt.setTotalAmt(new Double(intTrfAmt));
                remt.setExchange(new Double(0.0));
                remt.setPostage(new Double(0.0));
                remt.setOtherCharges(new Double(0.0));
                remt.setStatusDt(todayDt);
                remt.setStatusBy("TTSYSTEM");
                notDelRemMap.put(String.valueOf(1), remt);
                remMap.put("NOT_DELETED_ISSUE_TOs", notDelRemMap);
                dtatMap.put("RemittanceIssueTO", remMap);
                dtatMap.put("POSTAGE_AMT", CommonUtil.convertObjToStr(new Double(pstAmt)));
                dtatMap.put("RENEW_POSTAGE_AMT", CommonUtil.convertObjToStr(new Double(renewPstAmt)));
                dtatMap.put("POSTAGE_ACHD", achd);
                RemittanceIssueDAO remDao = new RemittanceIssueDAO();
                HashMap resulMap = new HashMap();
                remDao.setFromotherDAo(false);
                resulMap = remDao.execute(dtatMap);
                behavesMap = null;
                draweeMap = null;
                lstRemit = null;
            }
        }


    }

    private HashMap interestCalculationTLAD(String accountNo, String prodID) {
        HashMap map = new HashMap();
        HashMap insertPenal = new HashMap();
        HashMap hash = null;
        try {

            map.put("ACT_NUM", accountNo);
            map.put("PROD_ID", prodID);
            List lst = sqlMap.executeQueryForList("IntCalculationDetail", map);
            if (lst != null && lst.size() > 0) {
                hash = (HashMap) lst.get(0);
                if (hash.get("AS_CUSTOMER_COMES") != null && hash.get("AS_CUSTOMER_COMES").equals("N")) {
                    hash = new HashMap();
                    return hash;
                }
                map.put("BRANCH_ID", actBranch);
                map.put(CommonConstants.BRANCH_ID, actBranch);
                map.putAll(hash);
                map.put("LOAN_ACCOUNT_CLOSING", "LOAN_ACCOUNT_CLOSING");
                map.put("CURR_DATE", paramMap.get("TODAY_DT"));
                TaskHeader header = new TaskHeader();
                header.setBranchID(actBranch);
                InterestCalculationTask interestcalTask = new InterestCalculationTask(header);
                hash = interestcalTask.interestCalcTermLoanAD(map);
                if (hash == null) {
                    hash = new HashMap();
                } else if (hash != null && hash.size() > 0) {
                    double interest = CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
                    double penal = CommonUtil.convertObjToDouble(hash.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
                    hash.put("ACT_NUM", accountNo);
                    hash.put("FROM_DT", hash.get("LAST_INT_CALC_DT"));
                    hash.put("FROM_DT", DateUtil.addDays(((Date) hash.get("FROM_DT")), 2));
                    hash.put("TO_DATE", map.get("CURR_DATE"));
                    List facilitylst = sqlMap.executeQueryForList("getPaidPrinciple", hash);
                    if (facilitylst != null && facilitylst.size() > 0) {
                        hash = (HashMap) facilitylst.get(0);
                        interest -= CommonUtil.convertObjToDouble(hash.get("INTEREST")).doubleValue();
                        penal -= CommonUtil.convertObjToDouble(hash.get("PENAL")).doubleValue();
                    }
                    if (interest > 0) {
                        insertPenal.put("CURR_MONTH_INT", new Double(interest));
                    } else {
                        insertPenal.put("CURR_MONTH_INT", new Double(0));
                    }
                    if (penal > 0) {
                        insertPenal.put("PENAL_INT", new Double(penal));
                    } else {
                        insertPenal.put("PENAL_INT", new Double(0));
                    }
                    List chargeList = sqlMap.executeQueryForList("getChargeDetails", map);
                    if (chargeList != null && chargeList.size() > 0) {
                        for (int i = 0; i < chargeList.size(); i++) {
                            HashMap chargeMap = (HashMap) chargeList.get(i);

                            double chargeAmt = CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMT")).doubleValue();
                            if (chargeMap.get("CHARGE_TYPE").equals("POSTAGE CHARGES") && chargeAmt > 0) {
                                insertPenal.put("POSTAGE CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.get("CHARGE_TYPE").equals("MISCELLANEOUS CHARGES") && chargeAmt > 0) {
                                insertPenal.put("MISCELLANEOUS CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.get("CHARGE_TYPE").equals("LEGAL CHARGES") && chargeAmt > 0) {
                                insertPenal.put("LEGAL CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.get("CHARGE_TYPE").equals("INSURANCE CHARGES") && chargeAmt > 0) {
                                insertPenal.put("INSURANCE CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.get("CHARGE_TYPE").equals("EXECUTION DECREE CHARGES") && chargeAmt > 0) {
                                insertPenal.put("EXECUTION DECREE CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.get("CHARGE_TYPE").equals("ARBITRARY CHARGES") && chargeAmt > 0) {
                                insertPenal.put("ARBITRARY CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            if (chargeMap.get("CHARGE_TYPE").equals("ADVERTISE CHARGES") && chargeAmt > 0) {
                                insertPenal.put("ADVERTISE CHARGES", chargeMap.get("CHARGE_AMT"));
                            }
                            chargeMap = null;
                        }

                    }
                    chargeList = null;
                }
                interestcalTask = null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        map = null;
        hash = null;

        return insertPenal;
    }

    public Date nextCaldate(Date dpDt, Date nxtDt, HashMap acctDtlMap) {
        Date depMatDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(acctDtlMap.get("MATURITY_DT")));
        if (CommonUtil.convertObjToInt(acctDtlMap.get("FREQ")) != 0) {
            nxtDt = DateUtil.addDaysProperFormat(nxtDt, CommonUtil.convertObjToInt(acctDtlMap.get("FREQ")));
            Calendar dpnxtCalender = new GregorianCalendar(nxtDt.getYear() + 1900, nxtDt.getMonth(), nxtDt.getDate());
            int lstDayofmonth = dpnxtCalender.getActualMaximum(dpnxtCalender.DAY_OF_MONTH);
            Calendar dpDtCalender = new GregorianCalendar(dpDt.getYear() + 1900, dpDt.getMonth() + 1, dpDt.getDate());
            int dpDay = dpDt.getDate();
            if (DateUtil.dateDiff(nxtDt, depMatDt) < 0) {
                return depMatDt;
            }
            if (lstDayofmonth > dpDay) {
                nxtDt.setDate(dpDay);
                return nxtDt;
            } else {
                nxtDt.setDate(lstDayofmonth);
                return nxtDt;
            }
        } else {
            return DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(acctDtlMap.get("MATURITY_DT")));
        }
        //        return nxtDt ;
    }

    private void creditInttonoFixed(HashMap nonBatch) throws Exception {
        nonBatch.put("APPLICATION", "APPLICATION");
        InterestBatchTO objInterestBatchTO = null;
        nonBatch.put("CUMINTAPP", "CUMINTAPP");
        List productList = (List) getProductList(nonBatch);
        if (productList != null && productList.size() > 0) {
            int prodListSize = productList.size();
            for (int prod = 0; prod < prodListSize; prod++) {
                paramMap = new HashMap();
                paramMap.put(InterestTaskRunner.AC_HD_ID, ((HashMap) productList.get(prod)).get("ACCT_HEAD")); //Ac Head
                paramMap.put("INT_PROV_FREQ", ((HashMap) productList.get(prod)).get("INT_PROV_FREQ")); //Ac Head
                paramMap.put(InterestTaskRunner.DEBIT_INT, ((HashMap) productList.get(prod)).get("INT_PROV_ACHD")); //Debit Int Head
                paramMap.put(InterestTaskRunner.CREDIT_INT, ((HashMap) productList.get(prod)).get("INT_PAY")); //Credit Int Head
                paramMap.put(InterestTaskRunner.PRODUCT_ID, ((HashMap) productList.get(prod)).get("PROD_ID")); //Product Id
                paramMap.put("ROUND_OFF", ((HashMap) productList.get(prod)).get("INT_ROUNDOFF_TERMS")); //Product Id
                paramMap.put(InterestTaskRunner.LAST_APPL_DT, ((HashMap) productList.get(prod)).get("LAST_INT_APPL_DT")); //Last Interest Applied Dt Payale
                paramMap.put("interestType", ((HashMap) productList.get(prod)).get("INT_TYPE"));
                paramMap.put("BEHAVES_LIKE", ((HashMap) productList.get(prod)).get("BEHAVES_LIKE")); //Product Id
                paramMap.put(InterestTaskRunner.APPL_NEW_DT, null); //Updated date based on FREQ_A from InterestTaskRunner
                nonBatch.put(InterestTaskRunner.PRODUCT_ID, ((HashMap) productList.get(prod)).get("PROD_ID")); //Product Id)
                paramMap.put("BATCH_PROCESS", "BATCH_PROCESS");
                paramMap.put("PROD_TYPE", "TD");
                paramMap.put("TASK", "PROVISIONING");
                List ntlst = null;
                if (branchList != null && branchList.size() > 0) {
                    for (int b = 0; b < branchList.size(); b++) {
                        HashMap branchMap = (HashMap) branchList.get(b);
                        actBranch = CommonUtil.convertObjToStr(branchMap.get(CommonConstants.BRANCH_ID));
                        currentDate = ServerUtil.getCurrentDate(actBranch);
                        paramMap.put(CommonConstants.BRANCH_ID, actBranch);
                        nonBatch.put("PARAMMAP", paramMap);
                        ntlst = sqlMap.executeQueryForList("lastIntApplnDt", paramMap);
                        if (ntlst != null && ntlst.size() > 0) {
                            Date nxtDt = (Date) ((HashMap) ntlst.get(0)).get("LAST_APPL_DT");
                            nxtDt = DateUtil.addDaysProperFormat(nxtDt, 360);//360
                            Date lstDt = nxtDt;
                            Date nextCalsDt = nxtDt;
                            holiydaychecking(nxtDt);
                            Date dbDt = (Date) currentDate.clone();
                            List accountList = null;
                            if (DateUtil.dateDiff(checkThisCDate, dbDt) >= 0) {
                                nonBatch.put(CommonConstants.BRANCH_ID, actBranch);
                                accountList = sqlMap.executeQueryForList("lastIntApplnToCum", nonBatch);
                                if (accountList != null && accountList.size() > 0) {
                                    int accountListSize = accountList.size();
                                    for (int i = 0; i < accountListSize; i++) {
                                        HashMap cumIntCrMap = new HashMap();
                                        cumIntCrMap = (HashMap) accountList.get(i);
                                        intCreditToCumDep(cumIntCrMap, nxtDt, paramMap);
                                    }
                                }
                                if (!nonBatch.containsKey("ACT_TO")) {
                                    HashMap upMap = new HashMap();
                                    upMap.put(CommonConstants.BRANCH_ID, actBranch);
                                    upMap.put("PROD_ID", paramMap.get(InterestTaskRunner.PRODUCT_ID));
                                    upMap.put("LAST_APPL_DT", nxtDt);
                                    sqlMap.executeUpdate("lastintApplDTforCumDeposits", upMap);
                                    upMap = null;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void intCreditToCumDep(HashMap cumIntCrMap, Date nxtDt, HashMap paramMap) throws Exception {
        InterestBatchTO objInterestBatchTO = new InterestBatchTO();

        TdsCalc tds = new TdsCalc(actBranch);
        HashMap tdsMap = new HashMap();
        tdsMap.put("INT_DATE", nxtDt);
        objInterestBatchTO.setIsTdsApplied("N");
        tdsMap.put("CUSTID", CommonUtil.convertObjToStr(cumIntCrMap.get("CUST_ID")));
        List exceptionList = (List) sqlMap.executeQueryForList("getTDSExceptionData", tdsMap);
        if (exceptionList == null || exceptionList.size() <= 0) {
            tdsMap = new HashMap();
            String Prod_type = "TD";
            String prod_id = CommonUtil.convertObjToStr(cumIntCrMap.get("PROD_ID"));
            String accnum = CommonUtil.convertObjToStr(cumIntCrMap.get("DEPOSIT_NO"));
            double intTrfAmt = CommonUtil.convertObjToDouble(cumIntCrMap.get("INTCRAMT")).doubleValue();
            double roi = CommonUtil.convertObjToDouble(cumIntCrMap.get("RATE_OF_INT")).doubleValue();
            String CustId = CommonUtil.convertObjToStr(cumIntCrMap.get("CUST_ID"));
            Date dpDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(cumIntCrMap.get("DEPOSIT_DT")));
            Date mtDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(cumIntCrMap.get("MATURITY_DT")));
            if (DateUtil.dateDiff(nxtDt, mtDt) > 0) {
                paramMap.put("END", nxtDt);
            } else {
                paramMap.put("END", mtDt);
            }
            Date stDt = DateUtil.addDays(nxtDt, -360);
            if (DateUtil.dateDiff(stDt, dpDt) > 0) {
                paramMap.put("START", dpDt);
            } else {
                paramMap.put("START", stDt);
            }
            tdsMap = null;
            if (tdsMap != null) {
                objInterestBatchTO.setIsTdsApplied("Y");
                objInterestBatchTO.setTdsAmt(CommonUtil.convertObjToDouble(tdsMap.get("TDSDRAMT")));
            }
            HashMap acctDtlMap = new HashMap();
            acctDtlMap.put("INT_PAY_PROD_TYPE", "TD");
            acctDtlMap.put("INT_PAY_PROD_ID", prod_id);
            acctDtlMap.put("INT_PAY_ACC_NO", accnum);
            dointTransferToAccount(acctDtlMap, intTrfAmt, paramMap, tdsMap);
            objInterestBatchTO.setIntAmt(new Double(intTrfAmt));
            objInterestBatchTO.setDrCr("DEBIT");
            double Tot_Int_bal = intTrfAmt;
            Tot_Int_bal = Tot_Int_bal - intTrfAmt;
            objInterestBatchTO.setTot_int_amt(new Double(Tot_Int_bal));
            objInterestBatchTO.setActNum(CommonUtil.convertObjToStr(accnum));
            objInterestBatchTO.setAcHdId(CommonUtil.convertObjToStr(paramMap.get("CREDIT_INT")));
            objInterestBatchTO.setIntAmt(new Double(intTrfAmt));
            objInterestBatchTO.setIntRate(new Double(roi));
            objInterestBatchTO.setIntType("CUMMULATIVE");
            objInterestBatchTO.setProductType("TD");
            objInterestBatchTO.setTransLogId("A");
            objInterestBatchTO.setDrCr("DEBIT");
            objInterestBatchTO.setCustId(CustId);
            objInterestBatchTO.setProductId(CommonUtil.convertObjToStr(paramMap.get("PROD_ID")));
            objInterestBatchTO.setPrincipleAmt(CommonUtil.convertObjToDouble(cumIntCrMap.get("DEPOSIT_AMT")));
            Date applnDt = (Date) currentDate.clone();
            Date applDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("END")));
            if (applDt.getDate() > 0) {
                applnDt.setDate(applDt.getDate());
                applnDt.setMonth(applDt.getMonth());
                applnDt.setYear(applDt.getYear());
            }
            Date currDt = (Date) currentDate.clone();
            Date startDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("START")));
            if (startDt.getDate() > 0) {
                currDt.setDate(startDt.getDate());
                currDt.setMonth(startDt.getMonth());
                currDt.setYear(startDt.getYear());
            }      
            objInterestBatchTO.setApplDt(applnDt);
            objInterestBatchTO.setIntDt(currDt);
            objInterestBatchTO.setTrnDt(currentDate);
            sqlMap.executeUpdate("insertDepositInterestTO", objInterestBatchTO);
            sqlMap.executeUpdate("updateTotIndDrawnAmount", objInterestBatchTO);
            HashMap updateIntDtMap = new HashMap();
            updateIntDtMap.put("applDt", nxtDt);
            updateIntDtMap.put("actNum", CommonUtil.convertObjToStr(paramMap.get("ACT_NUM")));            
            sqlMap.executeUpdate("updateDepositIntLastApplDT", updateIntDtMap);
            updateIntDtMap = null;
        }


    }

    public void CalculationofCumalativeMaturityValueInterest() throws Exception {
        TdsCalc tds = new TdsCalc(actBranch);
        HashMap nonBatch = new HashMap();
        nonBatch.put("CUMINTAPP", "CUMINTAPP");
        List productList = (List) getProductList(nonBatch);
        if (productList != null && productList.size() > 0) {
            int prodListSize = productList.size();
            for (int prod = 0; prod < prodListSize; prod++) {
                paramMap = new HashMap();
                paramMap.put(InterestTaskRunner.AC_HD_ID, ((HashMap) productList.get(prod)).get("ACCT_HEAD")); //Ac Head
                paramMap.put("INT_PROV_FREQ", ((HashMap) productList.get(prod)).get("INT_PROV_FREQ")); //Ac Head
                paramMap.put(InterestTaskRunner.DEBIT_INT, ((HashMap) productList.get(prod)).get("INT_PROV_ACHD")); //Debit Int Head
                paramMap.put(InterestTaskRunner.CREDIT_INT, ((HashMap) productList.get(prod)).get("INT_PAY")); //Credit Int Head
                paramMap.put(InterestTaskRunner.PRODUCT_ID, ((HashMap) productList.get(prod)).get("PROD_ID")); //Product Id
                paramMap.put("ROUND_OFF", ((HashMap) productList.get(prod)).get("INT_ROUNDOFF_TERMS")); //Product Id
                paramMap.put(InterestTaskRunner.LAST_APPL_DT, ((HashMap) productList.get(prod)).get("LAST_INT_APPL_DT")); //Last Interest Applied Dt Payale
                paramMap.put("interestType", ((HashMap) productList.get(prod)).get("INT_TYPE"));
                paramMap.put("BEHAVES_LIKE", ((HashMap) productList.get(prod)).get("BEHAVES_LIKE")); //Product Id
                paramMap.put(InterestTaskRunner.APPL_NEW_DT, null); //Updated date based on FREQ_A from InterestTaskRunner
                nonBatch.put(InterestTaskRunner.PRODUCT_ID, ((HashMap) productList.get(prod)).get("PROD_ID")); //Product Id)
                paramMap.put("BATCH_PROCESS", "BATCH_PROCESS");
                paramMap.put("PROD_TYPE", "TD");
                paramMap.put("BRANCH_CODE", getHeader().getBranchID());
                paramMap.put("TASK", "PROVISIONING");
                nonBatch.put("PARAMMAP", paramMap);
                List ntlst = null;
                List accountList = null;
                nonBatch.put("MATURITY_DT", currentDate);
                accountList = getAccountList(nonBatch);
                if (accountList != null && accountList.size() > 0) {
                    int accountListSize = accountList.size();
                    for (int acc = 0; acc < accountListSize; acc++) {
                        paramMap.put("ACT_NUM", ((HashMap) accountList.get(acc)).get("ACT_NUM"));
                        paramMap.put("PROD_TYPE", "TD");
                        paramMap.put("CUR_DT", currentDate);
                        paramMap.put("BRANCH_CODE", getHeader().getBranchID());
                        double interestAmt = 0.0;
                        List acctDtl = sqlMap.executeQueryForList("getDepoitDetailForIntApp", paramMap);
                        if (acctDtl != null && acctDtl.size() > 0) {
                            HashMap acctDtlMap = new HashMap();
                            acctDtlMap = (HashMap) acctDtl.get(0);
                            paramMap.put("TODAY_DT", currentDate);
                            Date lstproDt = (Date) acctDtlMap.get("LAST_PROV_DT");
                            Date lstIntCrDt = (Date) acctDtlMap.get("LAST_INT_APPL_DT");
                            Date depDt = (Date) acctDtlMap.get("DEPOSIT_DT");
                            Date depMatDt = (Date) acctDtlMap.get("MATURITY_DT");
                            double Tot_Int_Cr = CommonUtil.convertObjToDouble(acctDtlMap.get("TOTAL_INT_CREDIT")).doubleValue();
                            double Tot_Int_Dr = CommonUtil.convertObjToDouble(acctDtlMap.get("TOTAL_INT_DRAWN")).doubleValue();
                            double Tot_Int_AMT = CommonUtil.convertObjToDouble(acctDtlMap.get("TOT_INT_AMT")).doubleValue();
                            double Tot_TDS_AMT = CommonUtil.convertObjToDouble(acctDtlMap.get("TDS_AMT")).doubleValue();
                            double dpAmt = CommonUtil.convertObjToDouble(acctDtlMap.get("DEPOSIT_AMT")).doubleValue();
                            double matAmt = CommonUtil.convertObjToDouble(acctDtlMap.get("MATURITY_AMT")).doubleValue();
                            long period = DateUtil.dateDiff(depDt, depMatDt);
                            lstIntCrDt = lstproDt;
                            if (DateUtil.dateDiff(lstIntCrDt, lstproDt) >= 0) {
                                paramMap.put("START", lstproDt);
                            } else {
                                paramMap.put("START", lstIntCrDt);
                            }
                            paramMap.put("END", depMatDt);
                            HashMap tdsToCalMap = new HashMap();
                            tdsToCalMap.put("CLOSEDACC", CommonUtil.convertObjToStr(paramMap.get("ACT_NUM")));
                            tdsToCalMap.put("CUR_DT", currentDate);
                            List tdsLst = sqlMap.executeQueryForList("getAccountPROvTDS", tdsToCalMap);
                            tdsToCalMap = new HashMap();
                            if (tdsLst != null && tdsLst.size() > 0) {
                                tdsToCalMap = (HashMap) tdsLst.get(0);
                            }
                            if (tdsToCalMap != null) {
                                acctDtlMap.put("OTHERDAO", "OTHERDAO");
                                acctDtlMap.put("START1", paramMap.get("START"));
                                acctDtlMap.put("END1", paramMap.get("END"));
                                acctDtlMap.put("BEHAVES_LIKE", "CUMMULATIVE");
                                double TdsIntAmt = tds.totalIntrestCalculationforTds(acctDtlMap, null, null);
                                String Prod_type = "TD";
                                String prod_id = CommonUtil.convertObjToStr(paramMap.get("PROD_ID"));
                                String accnum = CommonUtil.convertObjToStr(paramMap.get("ACT_NUM"));

                                double intTrfAmt = TdsIntAmt;
                                intTrfAmt = (double) getNearest((long) (intTrfAmt * 100), 100) / 100;
                                String CustId = CommonUtil.convertObjToStr(((HashMap) accountList.get(acc)).get("CUST_ID"));
                                HashMap tdsMap = new HashMap();
                                Date applnDt = (Date) currentDate.clone();
                                Date applDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("END")));
                                if (applDt.getDate() > 0) {
                                    applnDt.setDate(applDt.getDate());
                                    applnDt.setMonth(applDt.getMonth());
                                    applnDt.setYear(applDt.getYear());
                                }
                                tdsMap.put("INT_DATE", applnDt);
                                tdsMap.put("CUSTID", CustId);
                                List exceptionList = (List) sqlMap.executeQueryForList("getTDSExceptionData", tdsMap);
                                if (exceptionList != null && exceptionList.size() > 0) {
                                    double intAmt = matAmt - dpAmt;
                                    intTrfAmt = intAmt - Tot_Int_Cr;
                                }
                                try {
                                    if (isTransaction) {
                                       // sqlMap.startTransaction();
                                    }
                                    HashMap resultMap = new HashMap();
                                    TransferTrans transferTrans = new TransferTrans();
                                    ArrayList batchList = new ArrayList();
                                    HashMap ProviMap = new HashMap();
                                    lstIntCrDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("START")));
                                    ProviMap.put("LAST_APPL_DT", lstIntCrDt);
                                    ProviMap.put("INT_APPL_FREQ", acctDtlMap.get("FREQ"));
                                    paramMap.put("PROVISIONMAP", ProviMap);
                                    ProviMap = null;
                                    resultMap = prepareMap(paramMap, resultMap);
                                    batchList.add(transferTrans.getDebitTransferTO(resultMap, intTrfAmt));
                                    batchList.add(transferTrans.getCreditTransferTO(resultMap, intTrfAmt));
                                    transferTrans.setInitiatedBranch(actBranch);
                                    transferTrans.doDebitCredit(batchList, actBranch);
                                    InterestBatchTO objInterestBatchTO = new InterestBatchTO();
                                    objInterestBatchTO.setAcHdId(CommonUtil.convertObjToStr(paramMap.get("CREDIT_INT")));
                                    objInterestBatchTO.setIntAmt(new Double(intTrfAmt));
                                    objInterestBatchTO.setIntRate(new Double(CommonUtil.convertObjToDouble(acctDtlMap.get("RATE_OF_INT")).doubleValue()));
                                    objInterestBatchTO.setIntType("CUMMULATIVE");
                                    objInterestBatchTO.setPrincipleAmt(CommonUtil.convertObjToDouble(acctDtlMap.get("DEPOSIT_AMT")));
                                    objInterestBatchTO.setIsTdsApplied("N");
                                    objInterestBatchTO.setActNum(CommonUtil.convertObjToStr(acctDtlMap.get("DEPOSIT_NO")));
                                    objInterestBatchTO.setDrCr("CREDIT");
                                    objInterestBatchTO.setTot_int_amt(new Double((Tot_Int_Cr + intTrfAmt) - Tot_Int_Dr));
                                    applDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("END")));
                                    if (applDt.getDate() > 0) {
                                        applnDt.setDate(applDt.getDate());
                                        applnDt.setMonth(applDt.getMonth());
                                        applnDt.setYear(applDt.getYear());
                                    }
                                    Date currDt = (Date) currentDate.clone();
                                    Date startDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("START")));
                                    if (startDt.getDate() > 0) {
                                        currDt.setDate(startDt.getDate());
                                        currDt.setMonth(startDt.getMonth());
                                        currDt.setYear(startDt.getYear());
                                    }
                                    objInterestBatchTO.setIntDt(currDt);
                                    objInterestBatchTO.setApplDt(applnDt);
                                    objInterestBatchTO.setCustId(CustId);
                                    objInterestBatchTO.setProductId(prod_id);
                                    objInterestBatchTO.setProductType("TD");
                                    objInterestBatchTO.setTrnDt(currentDate);
                                    sqlMap.executeUpdate("insertDepositInterestTO", objInterestBatchTO);
                                    sqlMap.executeUpdate("updateLastInterestAmount", objInterestBatchTO);
                                    HashMap updateIntDtMap = new HashMap();
                                    updateIntDtMap.put("applDt", depMatDt);
                                    updateIntDtMap.put("actNum", CommonUtil.convertObjToStr(acctDtlMap.get("DEPOSIT_NO")));
                                    sqlMap.executeUpdate("updateDepositIntLastProlDT", updateIntDtMap);
                                    paramMap.put("BRANCH_CODE", actBranch);
                                    paramMap.put("ACT_NUM", CommonUtil.convertObjToStr(acctDtlMap.get("DEPOSIT_NO")));
                                    ntlst = null;
                                    ntlst = sqlMap.executeQueryForList("lastIntApplnToCum", paramMap);
                                    if (ntlst != null && ntlst.size() > 0) {
                                        HashMap nxtMap = new HashMap();
                                        nxtMap = (HashMap) ntlst.get(0);
                                        intCreditToCumDep(nxtMap, depMatDt, paramMap);
                                    }
                                    if (isTransaction) {
                                       // sqlMap.commitTransaction();
                                    }

                                } catch (Exception e) {
                                    if (isTransaction) {
                                        sqlMap.rollbackTransaction();
                                    }
                                    e.printStackTrace();
                                }


                            }
                        }

                    }
                }
            }
        }

    }

    private void performCumulativeInterestBeforeLive() throws Exception {
        List bforlive = sqlMap.executeQueryForList("getCumDepositsForProvisionBeforeLive", null);
        if (bforlive != null && bforlive.size() > 0) {
            for (int i = 0; i < bforlive.size(); i++) {
                HashMap provMap = (HashMap) bforlive.get(i);
                Date dpDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(provMap.get("DEPOSIT_DT")));
                Date mtDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(provMap.get("MATURITY_DT")));
                Date uptoDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(provMap.get("UPTO_DT")));
                double dpAmt = CommonUtil.convertObjToDouble(provMap.get("DEPOSIT_AMT")).doubleValue();
                double roi = CommonUtil.convertObjToDouble(provMap.get("RATE_OF_INT")).doubleValue();
                double mtAmt = CommonUtil.convertObjToDouble(provMap.get("MATURITY_AMT")).doubleValue();
                HashMap intMap = new HashMap();
                intMap.put("START", dpDt);
                Date enddt = null;
                if (DateUtil.dateDiff(uptoDt, mtDt) >= 0) {
                    enddt = uptoDt;
                } else {
                    enddt = mtDt;
                }
                double interestAmt = 0.0;
                int compQuarter = 0;
                Date forDiffDt = dpDt;
                dpDt = DateUtil.addDays(dpDt, 90);
                forDiffDt = DateUtil.addDays(forDiffDt, -1);
                double dpAmt1 = dpAmt;
                double totIntToAcc = 0.0;
                for (Date calStartDt = DateUtil.addDays(dpDt, -1); DateUtil.dateDiff(calStartDt, enddt) >= 0; calStartDt = DateUtil.addDays(calStartDt, 90)) {
                    interestAmt = (dpAmt * 1 * roi) / 400;
                    forDiffDt = calStartDt;
                    dpAmt = dpAmt + interestAmt;
                    compQuarter = compQuarter + 1;

                }
                double interest = 0.0;
                if (compQuarter > 0) {
                    double amount = dpAmt1 * (Math.pow((1 + roi / 400), compQuarter));
                    double for100 = 100 * (Math.pow((1 + roi / 400), compQuarter));
                    for100 = for100 * 10000;
                    for100 = (double) getNearest((long) (for100 * 100), 100) / 100;
                    for100 = for100 / 10000;
                    for100 = for100 - 100;
                    interest = for100 * (dpAmt1 / 100);
                    dpAmt = interest + dpAmt1;
                }

                double diffInt = 0.0;
                if (DateUtil.dateDiff(forDiffDt, enddt) > 0) {
                    long diffPeriod = DateUtil.dateDiff(forDiffDt, enddt);
                    diffInt = (diffPeriod * dpAmt * roi) / 36500;
                    dpAmt = dpAmt + diffInt;     
                }
                totIntToAcc = dpAmt - dpAmt1;
                interest = interest + diffInt;
                totIntToAcc = (double) getNearest((long) (totIntToAcc * 100), 100) / 100;
                InterestBatchTO objInterestBatchTO = new InterestBatchTO();
                objInterestBatchTO.setActNum(CommonUtil.convertObjToStr(provMap.get("DEPOSIT_NO")));
                //                                         objInterestBatchTO.setAcHdId(CommonUtil.convertObjToStr(paramMap.get("DEBIT_INT")));
                //                    objInterestBatchTO.setAcHdId(CommonUtil.convertObjToStr(paramMap.get("CREDIT_INT")));
                objInterestBatchTO.setIntAmt(new Double(totIntToAcc));
                objInterestBatchTO.setIntRate(new Double(roi));
                objInterestBatchTO.setIntType("SIMPLE");
                objInterestBatchTO.setPrincipleAmt(CommonUtil.convertObjToDouble(provMap.get("DEPOSIT_AMT")));
                objInterestBatchTO.setIsTdsApplied("N");
                objInterestBatchTO.setIntDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(provMap.get("DEPOSIT_DT"))));
                objInterestBatchTO.setApplDt(enddt);
                objInterestBatchTO.setProductId("MN");
                sqlMap.executeUpdate("insertDepositInterestTO", objInterestBatchTO);

                //                }

            }
        }

    }

    private void calenderFreqIntCalc(HashMap nonBatch) throws Exception {
        paramMap = new HashMap();
        ArrayList lstBhvsLike = (ArrayList) sqlMap.executeQueryForList("getBehavesLike", nonBatch);
        if (lstBhvsLike != null && lstBhvsLike.size() > 0) {
            HashMap mapBhvsLike = (HashMap) lstBhvsLike.get(0);
            bhvsLike = CommonUtil.convertObjToStr(mapBhvsLike.get("BEHAVES_LIKE"));
        }
        nonBatch.put("BEHAVES_LIKE", bhvsLike);
        nonBatch.put("APPLICATION", "APPLICATION");
        InterestBatchTO objInterestBatchTO = null;
        ArrayList productList = (ArrayList) sqlMap.executeQueryForList("Deposit.getProducts", nonBatch);
        if (productList != null && productList.size() > 0) {
            int prodListSize = productList.size();
            for (int prod = 0; prod < prodListSize; prod++) {
                paramMap.put(InterestTaskRunner.AC_HD_ID, ((HashMap) productList.get(prod)).get("ACCT_HEAD")); //Ac Head

                paramMap.put(InterestTaskRunner.DEBIT_INT, ((HashMap) productList.get(prod)).get("INT_PROV_ACHD")); //Debit Int Head
                paramMap.put(InterestTaskRunner.CREDIT_INT, ((HashMap) productList.get(prod)).get("INT_PAY")); //Credit Int Head
                paramMap.put(InterestTaskRunner.PRODUCT_ID, ((HashMap) productList.get(prod)).get("PROD_ID")); //Product Id
                paramMap.put("ROUND_OFF", ((HashMap) productList.get(prod)).get("INT_ROUNDOFF_TERMS")); //Product Id
                paramMap.put(InterestTaskRunner.LAST_APPL_DT, ((HashMap) productList.get(prod)).get("LAST_INT_APPL_DT")); //Last Interest Applied Dt Payale

                paramMap.put("interestType", ((HashMap) productList.get(prod)).get("INT_TYPE"));

                paramMap.put(InterestTaskRunner.APPL_NEW_DT, null); //Updated date based on FREQ_A from InterestTaskRunner


                nonBatch.put(InterestTaskRunner.PRODUCT_ID, ((HashMap) productList.get(prod)).get("PROD_ID")); //Product Id)
                paramMap.put("BATCH_PROCESS", "BATCH_PROCESS");
                paramMap.put("PROD_TYPE", "TD");
                paramMap.put("TASK", "APPLICATION");
                paramMap.put("DISCOUNTED_RATE", ((HashMap) productList.get(prod)).get("DISCOUNTED_RATE"));

                //                        implementTask(nonBatch, true);
                List accountList = null;
                if (branchList != null && branchList.size() > 0) {
                    for (int b = 0; b < branchList.size(); b++) {
                        HashMap branchMap = (HashMap) branchList.get(b);
                        actBranch = CommonUtil.convertObjToStr(branchMap.get(CommonConstants.BRANCH_ID));
                        currentDate = ServerUtil.getCurrentDate(actBranch);
                        paramMap.put(CommonConstants.BRANCH_ID, actBranch);
                        nonBatch.put("PARAMMAP", paramMap);
                        nonBatch.put("CURR_DATE", currentDate);                                            
                        //Added by nithya on 08-05-2020 for KD-1535                        
                        String specialRDScheme = "N";
                        String rdIntApplication = "N";
                        if(nonBatch.containsKey("SPECIAL_RD_SCHEME") && nonBatch.get("SPECIAL_RD_SCHEME") != null && CommonUtil.convertObjToStr(nonBatch.get("SPECIAL_RD_SCHEME")).equalsIgnoreCase("SPECIAL_RD_SCHEME")){
                           specialRDScheme = "Y"; 
                        }    
                        if (nonBatch.containsKey("RD_INT_APPLICATION") && nonBatch.get("RD_INT_APPLICATION") != null && CommonUtil.convertObjToStr(nonBatch.get("RD_INT_APPLICATION")).equalsIgnoreCase("RD_INT_APPLICATION")) {
                            rdIntApplication = "Y";
                        }
                        if(rdIntApplication.equalsIgnoreCase("N")){
                            nonBatch.put("NOT_RD_INT_APPLN","NOT_RD_INT_APPLN");
                        }                        
                        //*****************************************
                        List accountListcal = sqlMap.executeQueryForList("Deposit.getApplicationforcalenderfreq", nonBatch);                        if (accountListcal != null && accountListcal.size() > 0) {
                            for (int i = 0; i < accountListcal.size(); i++) {
                                try {
                                    if (isTransaction) {
                                        //sqlMap.startTransaction();
                                    }
                                    HashMap acctDtlMap = new HashMap();
                                    acctDtlMap = (HashMap) accountListcal.get(i);
                                    Date lstproDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(acctDtlMap.get("LST_PROV_DT")));
                                    Date lstIntCrDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(acctDtlMap.get("LAST_INT_APPL_DT")));
                                    Date depDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(acctDtlMap.get("DEPOSIT_DT")));
                                    Date depMatDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(acctDtlMap.get("MATURITY_DT")));
                                    Date nextCalsDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(acctDtlMap.get("INT_UPTO_DATE")));
                                    int freq = CommonUtil.convertObjToInt(acctDtlMap.get("INTPAY_FREQ"));
                                    long period = DateUtil.dateDiff(depDt, depMatDt);
                                    nextCalsDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(acctDtlMap.get("INT_UPTO_DATE")));
                                    double tot_int_credit = CommonUtil.convertObjToDouble(acctDtlMap.get("TOTAL_INT_CREDIT")).doubleValue();
                                    double tot_int_debit = CommonUtil.convertObjToDouble(acctDtlMap.get("TOTAL_INT_DEBIT")).doubleValue();
                                    paramMap.put("ACT_NUM", acctDtlMap.get("ACT_NUM"));
                                    double roi = 0.0;
                                    double interestAmt = 0.0;
                                    double amount = CommonUtil.convertObjToDouble(acctDtlMap.get("DEPOSIT_AMT")).doubleValue();
                                    if (slab.equals("Y")) {
                                        roi = rateOfInt;
                                    } else {
                                        roi = CommonUtil.convertObjToDouble(acctDtlMap.get("RATE_OF_INT")).doubleValue();
                                    }
                                    double intTrfAmt = 0.0;
                                    if (acctDtlMap.containsKey("INTPAY_FREQ") && CommonUtil.convertObjToInt(acctDtlMap.get("INTPAY_FREQ")) == 30 && CommonUtil.convertObjToStr(paramMap.get("DISCOUNTED_RATE")).equals("Y")) {
                                        if (DateUtil.dateDiff(lstproDt, lstIntCrDt) == 0 && DateUtil.dateDiff(depDt, lstIntCrDt) == 0) {
                                            int pr = (int) DateUtil.dateDiff(depDt, nextCalsDt);
                                            paramMap.put("END", nextCalsDt);
                                            paramMap.put("START", depDt);
                                            interestAmt = (amount * roi) / (1200 + roi);
                                            interestAmt = (interestAmt * pr) / 30;
                                            interestAmt = (double) getNearest((long) (interestAmt * 100), 100) / 100;
                                            intTrfAmt = interestAmt;
                                        } else if (DateUtil.dateDiff(lstproDt, lstIntCrDt) < 0 && DateUtil.dateDiff(depDt, lstproDt) > 0) {
                                            int pr = (int) DateUtil.dateDiff(depDt, nextCalsDt);
                                            interestAmt = (amount * roi) / (1200 + roi);
                                            interestAmt = (interestAmt * pr) / 30;
                                            interestAmt = (double) getNearest((long) (interestAmt * 100), 100) / 100;
                                            int pr1 = (int) DateUtil.dateDiff(depDt, lstproDt);
                                            pr1 = pr1 + 1;
                                            double diffint = (amount * roi) / (1200 + roi);
                                            diffint = (diffint * pr1) / 30;
                                            diffint = (double) getNearest((long) (diffint * 100), 100) / 100;
                                            intTrfAmt = interestAmt;
                                            interestAmt = interestAmt - diffint;
                                            paramMap.put("END", nextCalsDt);
                                            paramMap.put("START", lstproDt);

                                        }


                                    } else if (acctDtlMap.containsKey("INTPAY_FREQ") && ((CommonUtil.convertObjToInt(acctDtlMap.get("INTPAY_FREQ")) == 90)
                                            || (CommonUtil.convertObjToInt(acctDtlMap.get("INTPAY_FREQ")) == 180) || (CommonUtil.convertObjToInt(acctDtlMap.get("INTPAY_FREQ")) == 360)
                                            || (CommonUtil.convertObjToStr(paramMap.get("DISCOUNTED_RATE")).equals("N") && CommonUtil.convertObjToInt(acctDtlMap.get("INTPAY_FREQ")) == 30))) {
                                        if (DateUtil.dateDiff(lstproDt, lstIntCrDt) == 0 && DateUtil.dateDiff(depDt, lstIntCrDt) == 0) {
                                           
                                            int pr = (int) DateUtil.dateDiff(depDt, nextCalsDt);
                                            interestAmt = pr * amount * roi / 36500;
                                            //                                            interestAmt=interestAmt*pr;
                                            interestAmt = (double) getNearest((long) (interestAmt * 100), 100) / 100;
                                            intTrfAmt = interestAmt;
                                            paramMap.put("END", nextCalsDt);
                                            paramMap.put("START", depDt);
                                            
                                        } else if (DateUtil.dateDiff(lstproDt, lstIntCrDt) < 0 && DateUtil.dateDiff(depDt, lstproDt) > 0) {
                                          
                                            int pr = (int) DateUtil.dateDiff(depDt, nextCalsDt);
                                            interestAmt = pr * amount * roi / 36500;
                                            //                                            interestAmt=interestAmt*pr;
                                            interestAmt = (double) getNearest((long) (interestAmt * 100), 100) / 100;
                                            int pr1 = (int) DateUtil.dateDiff(depDt, lstproDt);
                                            pr1 = pr1 + 1;
                                            double diffint = pr1 * amount * roi / 36500;
                                            //                                            diffint=interestAmt*pr1;
                                            diffint = (double) getNearest((long) (diffint * 100), 100) / 100;
                                            intTrfAmt = interestAmt;
                                            interestAmt = interestAmt - diffint;
                                
                                            paramMap.put("END", nextCalsDt);
                                            paramMap.put("START", lstproDt);
                                           
                                        }


                                    }
                                    if (interestAmt > 0) {
                                        objInterestBatchTO = new InterestBatchTO();
                                        objInterestBatchTO.setActNum(CommonUtil.convertObjToStr(paramMap.get("ACT_NUM")));
                                        objInterestBatchTO.setAcHdId(CommonUtil.convertObjToStr(paramMap.get("CREDIT_INT")));
                                        objInterestBatchTO.setIntAmt(new Double(interestAmt));
                                        objInterestBatchTO.setIntRate(new Double(roi));
                                        objInterestBatchTO.setIntType("SIMPLE");
                                        objInterestBatchTO.setPrincipleAmt(CommonUtil.convertObjToDouble(acctDtlMap.get("DEPOSIT_AMT")));
                                        objInterestBatchTO.setIsTdsApplied("N");
                                        Date applnDt = (Date) currentDate.clone();
                                       
                                        Date applDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("END")));
                                
                                        if (applDt.getDate() > 0) {
                                            applnDt.setDate(applDt.getDate());
                                            applnDt.setMonth(applDt.getMonth());
                                            applnDt.setYear(applDt.getYear());
                                        }
                                        objInterestBatchTO.setApplDt(applnDt);
                                        interestAmt = (double) getNearest((long) (interestAmt * 100), 100) / 100;
                                        HashMap resultMap = new HashMap();
                                        TransferTrans transferTrans = new TransferTrans();
                                        ArrayList batchList = new ArrayList();
                                        resultMap = prepareMap(paramMap, resultMap);
                                        Date apStDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("START")));
                                        Date apEdDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("END")));
                                        apStDt = DateUtil.addDays(apStDt, 1);
                                        if (interestAmt < 0 && DateUtil.dateDiff(apStDt, apEdDt) == 0) {
                                            resultMap.put(TransferTrans.DR_AC_HD, (String) paramMap.get("CREDIT_INT"));
                                            resultMap.put(TransferTrans.CR_AC_HD, (String) paramMap.get("DEBIT_INT"));
                                            interestAmt = interestAmt * -1;
                                        }
                                        resultMap.put("LINK_BATCH_ID", paramMap.get("ACT_NUM"));
                                        transferTrans.setLinkBatchId(CommonUtil.convertObjToStr(paramMap.get("ACT_NUM")));
                                        batchList.add(transferTrans.getDebitTransferTO(resultMap, interestAmt));
                                        batchList.add(transferTrans.getCreditTransferTO(resultMap, interestAmt));
                                        transferTrans.setInitiatedBranch(actBranch);
                                        transferTrans.doDebitCredit(batchList, actBranch);
                                        Date nextDt = (Date) currentDate.clone();
                                        Date nextCalcDt = DateUtil.addDays(nextCalsDt, freq);
                                        if (nextCalcDt.getDate() > 0) {
                                            nextDt.setDate(nextCalcDt.getDate());
                                            nextDt.setMonth(nextCalcDt.getMonth());
                                            nextDt.setYear(nextCalcDt.getYear());
                                        }
                                        Date currDt = (Date) currentDate.clone();
                                        Date stDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("START")));
                                        if (stDt.getDate() > 0) {
                                            currDt.setDate(stDt.getDate());
                                            currDt.setMonth(stDt.getMonth());
                                            currDt.setYear(stDt.getYear());
                                        }
                                        objInterestBatchTO.setProductId(CommonUtil.convertObjToStr(paramMap.get("PROD_ID")));
                                        objInterestBatchTO.setApplDt(applnDt);
                                        if (nonBatch.containsKey("SPECIAL_RD_SCHEME") && nonBatch.get("SPECIAL_RD_SCHEME") != null && CommonUtil.convertObjToStr(nonBatch.get("SPECIAL_RD_SCHEME")).equalsIgnoreCase("SPECIAL_RD_SCHEME")) {
                                          objInterestBatchTO.setApplDt(getProperFormatDate(nonBatch.get("RD_DATE_TO")));
                                        }  
                                        objInterestBatchTO.setIntDt(currDt);
                                        objInterestBatchTO.setTrnDt(currentDate);
                                        objInterestBatchTO.setDrCr("CREDIT");
                                        double Tot_Int_bal = tot_int_credit - tot_int_debit + interestAmt;
                                        objInterestBatchTO.setTot_int_amt(new Double(Tot_Int_bal));
                                        objInterestBatchTO.setTransLogId("A");
                                        objInterestBatchTO.setCustId(CommonUtil.convertObjToStr(paramMap.get("CUST_ID")));
                                        if (interestAmt != 0) {
                                            sqlMap.executeUpdate("insertDepositInterestTO", objInterestBatchTO);
                                        }
                                        sqlMap.executeUpdate("updateLastInterestAmount", objInterestBatchTO);
                                        HashMap upMapint = new HashMap();
                                        upMapint.put("actNum", paramMap.get("ACT_NUM"));
                                     
                                        upMapint.put("applDt", applnDt);
                                        upMapint.put("nextIntappldt", nextDt);
                                        //                                    upMapint.put("applDt",DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("END"))));
                                        //                                    upMapint.put("nextIntappldt",nxtCalDt);
                                        upMapint.put("DEPOSIT_NO", paramMap.get("ACT_NUM"));
                                        List lst = sqlMap.executeQueryForList("getDepositAccountDetails", upMapint);
                                        if (lst != null && lst.size() > 0) {
                                            HashMap updateMap = (HashMap) lst.get(0);
                                            updateMap.put("TEMP_NEXT_INT_APPL_DT", updateMap.get("NEXT_INT_APPL_DT"));
                                            updateMap.put("TEMP_LAST_INT_APPL_DT", updateMap.get("LAST_INT_APPL_DT"));
                                            updateMap.put("INT_CREDIT", updateMap.get("TOTAL_INT_CREDIT"));
                                            updateMap.put("INT_DRAWN", updateMap.get("TOTAL_INT_DRAWN"));
                                            sqlMap.executeUpdate("updateDepositTempDate", updateMap);
                                        } 
                                        
                                        if (nonBatch.containsKey("SPECIAL_RD_SCHEME") && nonBatch.get("SPECIAL_RD_SCHEME") != null && CommonUtil.convertObjToStr(nonBatch.get("SPECIAL_RD_SCHEME")).equalsIgnoreCase("SPECIAL_RD_SCHEME")) {
                                            upMapint.put("applDt", null);
                                            System.out.println("executing date here 2");
                                        }
                                        sqlMap.executeUpdate("updateDepositIntLastApplDT", upMapint);
                                        sqlMap.executeUpdate("updateDepositNextIntApplDT", upMapint);
                                        //                                }
                                        intTrfAmt = (double) getNearest((long) (intTrfAmt * 100), 100) / 100;
                                        if (acctDtlMap.get("INTPAY_MODE").equals("TRANSFER") && acctDtlMap.get("INT_PAY_PROD_TYPE") != null && CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_PROD_TYPE")).length() > 0) {
                                            HashMap accStatusMap = new HashMap();
                                            if (CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_PROD_TYPE")).equals("OA") || CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_PROD_TYPE")).equals("AD") || CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_PROD_TYPE")).equals("TL")) {
                                                String ProdType = CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_PROD_TYPE"));
                                                if (ProdType.equals("TL")) {
                                                    ProdType = "AD";
                                                }
                                                accStatusMap.put("ACT_NUM", CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_ACC_NO")));
                                                List stLst = sqlMap.executeQueryForList("getAccountStatus" + ProdType, accStatusMap);
                                                accStatusMap = (HashMap) stLst.get(0);

                                            }
                                        }
                                    }
                                    //sqlMap.commitTransaction();

                                } catch (Exception e) {
                                    sqlMap.rollbackTransaction();
                                    e.printStackTrace();
                                    status.setStatus(BatchConstants.ERROR);
                                }

                            }
                        }

                    }
                }
            }

        }
        paramMap = null;
    }

    private void calenderFreqIntCalcForTable(HashMap nonBatch, List interestList) throws Exception {
        paramMap = new HashMap();
        ArrayList rdList = new ArrayList();
        String rec = "RECURRING";
        String mds = "MDS";
        String behaves = "";
        HashMap errorMap = null;
        if (interestList != null && interestList.size() > 0 && interestList.get(0) instanceof HashMap) {
            errorMap = (HashMap) interestList.get(0);
        }
        nonBatch.put("BEHAVES_LIKE", "FIXED");
        nonBatch.put("APPLICATION", "APPLICATION");
        InterestBatchTO objInterestBatchTO = null;
        ArrayList productList = (ArrayList) sqlMap.executeQueryForList("Deposit.getProducts", nonBatch);
        //                ArrayList productList = getProductList(nonBatch);
       if (productList != null && productList.size() > 0) {
            int prodListSize = productList.size();
            for (int prod = 0; prod < prodListSize; prod++) {
                paramMap.put(InterestTaskRunner.AC_HD_ID, ((HashMap) productList.get(prod)).get("ACCT_HEAD")); //Ac Head
                paramMap.put(InterestTaskRunner.DEBIT_INT, ((HashMap) productList.get(prod)).get("INT_PROV_ACHD")); //Debit Int Head
                paramMap.put(InterestTaskRunner.CREDIT_INT, ((HashMap) productList.get(prod)).get("INT_PAY")); //Credit Int Head
                paramMap.put(InterestTaskRunner.PRODUCT_ID, ((HashMap) productList.get(prod)).get("PROD_ID")); //Product Id
                paramMap.put("ROUND_OFF", ((HashMap) productList.get(prod)).get("INT_ROUNDOFF_TERMS")); //Product Id
                paramMap.put(InterestTaskRunner.LAST_APPL_DT, ((HashMap) productList.get(prod)).get("LAST_INT_APPL_DT")); //Last Interest Applied Dt Payale
                paramMap.put("interestType", ((HashMap) productList.get(prod)).get("INT_TYPE"));
                paramMap.put(InterestTaskRunner.APPL_NEW_DT, null); //Updated date based on FREQ_A from InterestTaskRunner
                nonBatch.put(InterestTaskRunner.PRODUCT_ID, ((HashMap) productList.get(prod)).get("PROD_ID")); //Product Id)
                String postageAcHd = CommonUtil.convertObjToStr(((HashMap) productList.get(prod)).get("POSTAGE_ACHD"));
                paramMap.put("BATCH_PROCESS", "BATCH_PROCESS");
                paramMap.put("PROD_TYPE", "TD");
                paramMap.put("TASK", "APPLICATION");
                paramMap.put("DISCOUNTED_RATE", ((HashMap) productList.get(prod)).get("DISCOUNTED_RATE"));
                //                        implementTask(nonBatch, true);
                actBranch = branch;
                currentDate = ServerUtil.getCurrentDate(actBranch);
                paramMap.put(CommonConstants.BRANCH_ID, actBranch);
                nonBatch.put("PARAMMAP", paramMap);
                nonBatch.put("CURR_DATE", currentDate);

                double postageAmt = 0.0;
                double postageRenewAmt = 0.0;



                List accountList = null;
                List accountListcal = null;
                if (paramMap.containsKey("CAL_FREQ_ACCOUNT_LIST")) {
                    accountList = (ArrayList) paramMap.get("CAL_FREQ_ACCOUNT_LIST");
                } else {
                    accountList = new ArrayList();
                    accountList.add("");
                }
                HashMap tempMap = null;
                for (int j = 0; j < accountList.size(); j++) {
                    if (accountList.get(j) instanceof HashMap) {
                        tempMap = (HashMap) accountList.get(j);
                        nonBatch.put("ACT_NUM", tempMap.get("ACT_NUM"));
                        behaves = CommonUtil.convertObjToStr(tempMap.get("ACCT_TYPE"));
                    }
                    //Added by nithya on 08-05-2020 for KD-1535 
                    String specialRDScheme = "N";
                    String rdIntApplication = "N";
                    if (nonBatch.containsKey("SPECIAL_RD_SCHEME") && nonBatch.get("SPECIAL_RD_SCHEME") != null && CommonUtil.convertObjToStr(nonBatch.get("SPECIAL_RD_SCHEME")).equalsIgnoreCase("SPECIAL_RD_SCHEME")) {
                        specialRDScheme = "Y";
                    }
                    if (nonBatch.containsKey("RD_INT_APPLICATION") && nonBatch.get("RD_INT_APPLICATION") != null && CommonUtil.convertObjToStr(nonBatch.get("RD_INT_APPLICATION")).equalsIgnoreCase("RD_INT_APPLICATION")) {
                        rdIntApplication = "Y";
                    }
                    if (rdIntApplication.equalsIgnoreCase("N")) {
                        nonBatch.put("NOT_RD_INT_APPLN", "NOT_RD_INT_APPLN");
                    }
                    // End 
                    accountListcal = sqlMap.executeQueryForList("Deposit.getApplicationforcalenderfreq", nonBatch);
                    if (accountListcal != null && accountListcal.size() > 0) {
                        for (int i = 0; i < accountListcal.size(); i++) {
                            try {
                                HashMap acctDtlMap = new HashMap();
                                acctDtlMap = (HashMap) accountListcal.get(i);
                                Date lstproDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(acctDtlMap.get("LST_PROV_DT")));
                                Date lstIntCrDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(acctDtlMap.get("LAST_INT_APPL_DT")));
                                Date depDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(acctDtlMap.get("DEPOSIT_DT")));
                                Date depMatDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(acctDtlMap.get("MATURITY_DT")));
                                Date nextCalsDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(acctDtlMap.get("INT_UPTO_DATE")));
                                int freq = CommonUtil.convertObjToInt(acctDtlMap.get("INTPAY_FREQ"));
                                long period = DateUtil.dateDiff(depDt, depMatDt);
                                nextCalsDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(acctDtlMap.get("INT_UPTO_DATE")));
                                double tot_int_credit = CommonUtil.convertObjToDouble(acctDtlMap.get("TOTAL_INT_CREDIT")).doubleValue();
                                double tot_int_debit = CommonUtil.convertObjToDouble(acctDtlMap.get("TOTAL_INT_DEBIT")).doubleValue();
                                paramMap.put("ACT_NUM", acctDtlMap.get("ACT_NUM"));
                                List postageList = sqlMap.executeQueryForList("getPostageAmount", paramMap);
                                List renewPostageList = sqlMap.executeQueryForList("getRenewPostageAmount", paramMap);
                                if (postageList != null && postageList.size() > 0) {
                                    HashMap hamp = (HashMap) postageList.get(0);
                                    postageAmt = CommonUtil.convertObjToDouble(hamp.get("POSTAGE_AMT")).doubleValue();

                                }
                                if (renewPostageList != null && renewPostageList.size() > 0) {
                                    HashMap hamp = (HashMap) renewPostageList.get(0);
                                    postageRenewAmt = CommonUtil.convertObjToDouble(hamp.get("RENEW_POSTAGE_AMT")).doubleValue();
                                }
                                double roi = 0.0;
                                double interestAmt = 0.0;
                                double amount = CommonUtil.convertObjToDouble(acctDtlMap.get("DEPOSIT_AMT")).doubleValue();
                                roi = CommonUtil.convertObjToDouble(acctDtlMap.get("RATE_OF_INT")).doubleValue();
                                double intTrfAmt = 0.0;
                                //Added By Suresh
                                if (!isTransaction) {
                                    if (acctDtlMap.containsKey("INTPAY_FREQ") && CommonUtil.convertObjToInt(acctDtlMap.get("INTPAY_FREQ")) == 30 && CommonUtil.convertObjToStr(paramMap.get("DISCOUNTED_RATE")).equals("Y")) {
                                        if (DateUtil.dateDiff(lstproDt, lstIntCrDt) == 0 && DateUtil.dateDiff(depDt, lstIntCrDt) == 0) {
                                            int pr = (int) DateUtil.dateDiff(depDt, nextCalsDt);
                                            paramMap.put("END", nextCalsDt);
                                            paramMap.put("START", depDt);
                                            interestAmt = (amount * roi) / (1200 + roi);                                            interestAmt = (interestAmt * pr) / 30;
                                            interestAmt = (double) getNearest((long) (interestAmt * 100), 100) / 100;
                                            intTrfAmt = interestAmt;
                                        } else if (DateUtil.dateDiff(lstproDt, lstIntCrDt) < 0 && DateUtil.dateDiff(depDt, lstproDt) > 0) {
                                            int pr = (int) DateUtil.dateDiff(depDt, nextCalsDt);
                                            interestAmt = (amount * roi) / (1200 + roi);
                                            interestAmt = (interestAmt * pr) / 30;
                                            interestAmt = (double) getNearest((long) (interestAmt * 100), 100) / 100;
                                            int pr1 = (int) DateUtil.dateDiff(depDt, lstproDt);
                                            pr1 = pr1 + 1;
                                            double diffint = (amount * roi) / (1200 + roi);
                                            diffint = (diffint * pr1) / 30;
                                            diffint = (double) getNearest((long) (diffint * 100), 100) / 100;
                                            intTrfAmt = interestAmt;
                                            interestAmt = interestAmt - diffint;
                                            paramMap.put("END", nextCalsDt);
                                            paramMap.put("START", lstproDt);
                                        }
                                    } else if (acctDtlMap.containsKey("INTPAY_FREQ") && ((CommonUtil.convertObjToInt(acctDtlMap.get("INTPAY_FREQ")) == 90)
                                            || (CommonUtil.convertObjToInt(acctDtlMap.get("INTPAY_FREQ")) == 180) || (CommonUtil.convertObjToInt(acctDtlMap.get("INTPAY_FREQ")) == 360)
                                            || (CommonUtil.convertObjToStr(paramMap.get("DISCOUNTED_RATE")).equals("N") && CommonUtil.convertObjToInt(acctDtlMap.get("INTPAY_FREQ")) == 30))) {
                                        if (DateUtil.dateDiff(lstproDt, lstIntCrDt) == 0 && DateUtil.dateDiff(depDt, lstIntCrDt) == 0) {
                                            int pr = (int) DateUtil.dateDiff(depDt, nextCalsDt);
                                            interestAmt = pr * amount * roi / 36500;
                                            interestAmt = (double) getNearest((long) (interestAmt * 100), 100) / 100;
                                            intTrfAmt = interestAmt;
                                            paramMap.put("END", nextCalsDt);
                                            paramMap.put("START", depDt);
                                        } else if (DateUtil.dateDiff(lstproDt, lstIntCrDt) < 0 && DateUtil.dateDiff(depDt, lstproDt) > 0) {
                                            int pr = (int) DateUtil.dateDiff(depDt, nextCalsDt);
                                            interestAmt = pr * amount * roi / 36500;
                                            interestAmt = (double) getNearest((long) (interestAmt * 100), 100) / 100;
                                            int pr1 = (int) DateUtil.dateDiff(depDt, lstproDt);
                                            pr1 = pr1 + 1;
                                            double diffint = pr1 * amount * roi / 36500;
                                            diffint = (double) getNearest((long) (diffint * 100), 100) / 100;
                                            intTrfAmt = interestAmt;
                                            interestAmt = interestAmt - diffint;
                                            paramMap.put("END", nextCalsDt);
                                            paramMap.put("START", lstproDt);
                                        }
                                    }
                                }
                                if (isTransaction) {
                                    interestAmt = CommonUtil.convertObjToDouble(paramMap.get("INTEREST_AMOUNT")).doubleValue();
                                    intTrfAmt = CommonUtil.convertObjToDouble(paramMap.get("INTEREST_AMOUNT")).doubleValue();
                                    if (interestAmt > 0) {
                                        //sqlMap.startTransaction();
                                        objInterestBatchTO = new InterestBatchTO();
                                        objInterestBatchTO.setActNum(CommonUtil.convertObjToStr(paramMap.get("ACT_NUM")));
                                        objInterestBatchTO.setAcHdId(CommonUtil.convertObjToStr(paramMap.get("CREDIT_INT")));
                                        objInterestBatchTO.setIntAmt(new Double(interestAmt));
                                        objInterestBatchTO.setIntRate(new Double(roi));
                                        objInterestBatchTO.setIntType("SIMPLE");
                                        objInterestBatchTO.setPrincipleAmt(CommonUtil.convertObjToDouble(acctDtlMap.get("DEPOSIT_AMT")));
                                        objInterestBatchTO.setIsTdsApplied("N");
                                        Date applnDt = (Date) currentDate.clone();
                                        Date applDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("END")));
                                        if (applDt.getDate() > 0) {
                                            applnDt.setDate(applDt.getDate());
                                            applnDt.setMonth(applDt.getMonth());
                                            applnDt.setYear(applDt.getYear());
                                        }
                                        objInterestBatchTO.setApplDt(applnDt);
                                        interestAmt = (double) getNearest((long) (interestAmt * 100), 100) / 100;
                                        HashMap resultMap = new HashMap();
                                        TransferTrans transferTrans = new TransferTrans();
                                        ArrayList batchList = new ArrayList();
                                        resultMap = prepareMap(paramMap, resultMap);
                                        Date apStDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("START")));
                                        Date apEdDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("END")));
                                        apStDt = DateUtil.addDays(apStDt, 1);
                                        if (interestAmt < 0 && DateUtil.dateDiff(apStDt, apEdDt) == 0) {
                                            resultMap.put(TransferTrans.DR_AC_HD, (String) paramMap.get("CREDIT_INT"));
                                            resultMap.put(TransferTrans.CR_AC_HD, (String) paramMap.get("DEBIT_INT"));
                                            interestAmt = interestAmt * -1;
                                        }
                                        resultMap.put("LINK_BATCH_ID", paramMap.get("ACT_NUM"));
                                        transferTrans.setLinkBatchId(CommonUtil.convertObjToStr(paramMap.get("ACT_NUM")));
                                        batchList.add(transferTrans.getDebitTransferTO(resultMap, interestAmt));
                                        batchList.add(transferTrans.getCreditTransferTO(resultMap, interestAmt));
                                        transferTrans.setInitiatedBranch(actBranch);
                                        transferTrans.doDebitCredit(batchList, actBranch);
                                        Date nextDt = (Date) currentDate.clone();
                                        Date nextCalcDt = DateUtil.addDays(nextCalsDt, freq);
                                        if (nextCalcDt.getDate() > 0) {
                                            nextDt.setDate(nextCalcDt.getDate());
                                            nextDt.setMonth(nextCalcDt.getMonth());
                                            nextDt.setYear(nextCalcDt.getYear());
                                        }
                                        Date currDt = (Date) currentDate.clone();
                                        Date stDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("START")));
                                        if (stDt.getDate() > 0) {
                                            currDt.setDate(stDt.getDate());
                                            currDt.setMonth(stDt.getMonth());
                                            currDt.setYear(stDt.getYear());
                                        }
                                        objInterestBatchTO.setProductId(CommonUtil.convertObjToStr(paramMap.get("PROD_ID")));
                                        objInterestBatchTO.setApplDt(applnDt);
                                        objInterestBatchTO.setIntDt(currDt);
                                        objInterestBatchTO.setTrnDt(currentDate);
                                        objInterestBatchTO.setDrCr("CREDIT");
                                        double Tot_Int_bal = tot_int_credit - tot_int_debit + interestAmt;
                                        objInterestBatchTO.setTot_int_amt(new Double(Tot_Int_bal));
                                        objInterestBatchTO.setTransLogId("A");
                                        objInterestBatchTO.setCustId(CommonUtil.convertObjToStr(paramMap.get("CUST_ID")));
                                        if (interestAmt != 0) {
                                            sqlMap.executeUpdate("insertDepositInterestTO", objInterestBatchTO);
                                        }
                                        sqlMap.executeUpdate("updateLastInterestAmount", objInterestBatchTO);
                                        HashMap upMapint = new HashMap();
                                        upMapint.put("actNum", paramMap.get("ACT_NUM"));
                                        upMapint.put("applDt", applnDt);
                                        upMapint.put("nextIntappldt", nextDt);
                                        upMapint.put("DEPOSIT_NO", paramMap.get("ACT_NUM"));
                                        List lst = sqlMap.executeQueryForList("getDepositAccountDetails", upMapint);
                                        if (lst != null && lst.size() > 0) {
                                            HashMap updateMap = (HashMap) lst.get(0);
                                            updateMap.put("TEMP_NEXT_INT_APPL_DT", updateMap.get("NEXT_INT_APPL_DT"));
                                            updateMap.put("TEMP_LAST_INT_APPL_DT", updateMap.get("LAST_INT_APPL_DT"));
                                            updateMap.put("INT_CREDIT", updateMap.get("TOTAL_INT_CREDIT"));
                                            updateMap.put("INT_DRAWN", updateMap.get("TOTAL_INT_DRAWN"));
                                            sqlMap.executeUpdate("updateDepositTempDate", updateMap);
                                        }                                        
                                        sqlMap.executeUpdate("updateDepositIntLastApplDT", upMapint);
                                        sqlMap.executeUpdate("updateDepositNextIntApplDT", upMapint);
                                        intTrfAmt = (double) getNearest((long) (intTrfAmt * 100), 100) / 100;
                                        if (acctDtlMap.get("INTPAY_MODE").equals("TRANSFER")) {
                                            if (acctDtlMap.get("INT_PAY_PROD_TYPE") != null && CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_PROD_TYPE")).length() > 0) {
                                                HashMap accStatusMap = new HashMap();
                                                if (CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_PROD_TYPE")).equals("OA") || CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_PROD_TYPE")).equals("AD") || CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_PROD_TYPE")).equals("TL")) {
                                                    String ProdType = CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_PROD_TYPE"));
                                                    if (ProdType.equals("TL")) {
                                                        ProdType = "AD";
                                                    }
                                                    accStatusMap.put("ACT_NUM", CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_ACC_NO")));
                                                    List stLst = sqlMap.executeQueryForList("getAccountStatus" + ProdType, accStatusMap);
                                                    accStatusMap = (HashMap) stLst.get(0);
                                                }
                                                if (!CommonUtil.convertObjToStr(accStatusMap.get("ACT_STATUS_ID")).equals("CLOSED")) {
                                                    if (behaves.equals(rec)) {
                                                        intTrfAmt = CommonUtil.convertObjToDouble(tempMap.get("AMOUNT")).doubleValue();
                                                    }
                                                    if (behaves.equals(mds)) {
                                                        HashMap map = new HashMap();
                                                        TransactionTO transactionTO = new TransactionTO();
                                                        transactionDetailsTO = new LinkedHashMap();
                                                        LinkedHashMap transMap = new LinkedHashMap();
                                                        transactionTO.setTransType("TRANSFER");

                                                        transactionTO.setTransAmt(CommonUtil.convertObjToDouble(tempMap.get("TOTAL_DEMAND")));
                                                        transactionTO.setProductType("GL");
                                                        transactionTO.setDebitAcctNo(CommonUtil.convertObjToStr(paramMap.get(InterestTaskRunner.CREDIT_INT)));
                                                        transactionTO.setApplName(CommonUtil.convertObjToStr(paramMap.get(InterestTaskRunner.CREDIT_INT)));
                                                        transactionTO.setInstType("VOUCHER");

                                                        HashMap ChittalMap = new HashMap();
                                                        map.put(CommonConstants.USER_ID, CommonUtil.convertObjToStr(tempMap.get("USER_ID")));
                                                        map.put("BRANCH_CODE", actBranch);

                                                        transMap.put("1", transactionTO);
                                                        transactionDetailsTO.put("NOT_DELETED_TRANS_TOs", transMap);

                                                        map.put("TransactionTO", transactionDetailsTO);
                                                     
                                                        ChittalMap.put("ACT_NUM", tempMap.get("ACCT_NUM"));
                                                        ChittalMap.put("ACT_NUM", tempMap.get("ACCT_NUM"));

                                                        ChittalMap.put("PRINCIPAL", tempMap.get("PRINCIPAL"));
                                                        ChittalMap.put("TOTAL_DEMAND", tempMap.get("TOTAL_DEMAND"));
                                                        ChittalMap.put("INT_CALC_UPTO_DT", currentDate);

                                                        ChittalMap.put("PENAL_AMT", tempMap.get("PENAL_AMT"));
                                                        ChittalMap.put("BONUS_AMT", tempMap.get("BONUS_AMT"));
                                                        ChittalMap.put("DISCOUNT_AMT", tempMap.get("DISCOUNT_AMT"));
                                                        ChittalMap.put("INTEREST", tempMap.get("INTEREST"));
                                                        ChittalMap.put("CHARGES", tempMap.get("CHARGES"));
                                                        ChittalMap.put("ARBITRATION_AMT", tempMap.get("ARBITRATION_AMT"));
                                                        ChittalMap.put("NOTICE_AMT", tempMap.get("NOTICE_AMT"));
                                                       
                                                        if (postageAmt > 0) {

                                                            ChittalMap.put("POSTAGE_AMT_FOR_INT", CommonUtil.convertObjToStr(new Double(postageAmt)));
                                                            ChittalMap.put("POSTAGE_ACHD", postageAcHd);
                                                        }
                                                        if (postageRenewAmt > 0) {
                                                            ChittalMap.put("POSTAGE_ACHD", postageAcHd);
                                                            ChittalMap.put("RENEW_POSTAGE_AMT", CommonUtil.convertObjToStr(new Double(postageRenewAmt)));
                                                        }
                                                        transactionPartMDS(tempMap, map);



                                                    } else {
                                                        if (postageAmt > 0) {

                                                            acctDtlMap.put("POSTAGE_AMT", CommonUtil.convertObjToStr(new Double(postageAmt)));
                                                            acctDtlMap.put("POSTAGE_ACHD", postageAcHd);
                                                        }
                                                        if (postageRenewAmt > 0) {
                                                            acctDtlMap.put("POSTAGE_ACHD", postageAcHd);
                                                            acctDtlMap.put("RENEW_POSTAGE_AMT", CommonUtil.convertObjToStr(new Double(postageRenewAmt)));
                                                        }
                                                        dointTransferToAccount(acctDtlMap, intTrfAmt, paramMap, null);
                                                    }
                                                    if (interestList.size() == 0) {
                                                        interestList.add("");
                                                    }
                                                    if (j == 0 || j == accountListcal.size() - 1) {
                                                        if (interestList.size() != 2) {
                                                            if (interestList.size() == 2) {
                                                                interestList.set(1, paramMap.get(paramMap.get("ACT_NUM")));
                                                            } else {
                                                                interestList.add(paramMap.get(paramMap.get("ACT_NUM")));
                                                            }
                                                        }
                                                    }
                                                    objInterestBatchTO.setIntAmt(new Double(intTrfAmt));
                                                    objInterestBatchTO.setDrCr("DEBIT");
                                                    Tot_Int_bal = Tot_Int_bal - intTrfAmt;
                                                    objInterestBatchTO.setTot_int_amt(new Double(Tot_Int_bal));
                                                    sqlMap.executeUpdate("insertDepositInterestTO", objInterestBatchTO);
                                                    sqlMap.executeUpdate("updateTotIndDrawnAmount", objInterestBatchTO);

                                                }
                                            } else {
                                                throw new TTException("Interest Payment Product Type not found...");
                                            }
                                        } else {  //Cash Part
                                            dointCash(intTrfAmt, paramMap, null);
                                            if (paramMap.containsKey(paramMap.get("ACT_NUM"))) {
                                                if (interestList.size() == 0) {
                                                    interestList.add("");
                                                }
                                                if (j == 0 || j == accountListcal.size() - 1) {
                                                    if (interestList.size() == 2) {
                                                        interestList.set(1, paramMap.get(paramMap.get("ACT_NUM")));
                                                    } else {
                                                        interestList.add(paramMap.get(paramMap.get("ACT_NUM")));
                                                    }
                                                }
                                            }
                                        }

                                        //sqlMap.commitTransaction();
                                    }
                                } else {
                                    HashMap hmap = new HashMap();
                                    double postAmount = 0.0;
                                    double renewpostAmt = 0.0;
                                    HashMap postageMap = new HashMap();
                                    HashMap hshMap = null;
                                    ArrayList interestRow = new ArrayList();
                                    interestRow.add(new Boolean(false));
                                    interestRow.add(acctDtlMap.get("CUST_ID"));
                                    String actNum = CommonUtil.convertObjToStr(paramMap.get("ACT_NUM"));
                                    postageMap.put("ACT_NUM", actNum);
                                    List postageList1 = sqlMap.executeQueryForList("getPostageAmount", postageMap);
                                    List renewPostageList1 = sqlMap.executeQueryForList("getRenewPostageAmount", postageMap);
                                    if (postageList1 != null && postageList1.size() > 0) {
                                        postageMap = (HashMap) postageList1.get(0);
                                        postAmount = CommonUtil.convertObjToDouble(postageMap.get("POSTAGE_AMT")).doubleValue();

                                    }
                                    if (renewPostageList1 != null && renewPostageList1.size() > 0) {
                                        postageMap = (HashMap) renewPostageList1.get(0);
                                        renewpostAmt = CommonUtil.convertObjToDouble(postageMap.get("RENEW_POSTAGE_AMT")).doubleValue();
                                    }
                                    actNum = actNum.lastIndexOf("_") != -1 ? actNum.substring(0, actNum.length() - 2) : actNum;
                                    //                                    hmap.put("DEPOSIT_NO",actNum);
                                    interestRow.add(actNum);
                                    interestRow.add(acctDtlMap.get("CNAME"));
                                    interestRow.add(acctDtlMap.get("DEPOSIT_AMT"));
                                    interestRow.add(DateUtil.getStringDate((Date) acctDtlMap.get("DEPOSIT_DT")));
                                    interestRow.add(DateUtil.getStringDate((Date) acctDtlMap.get("MATURITY_DT")));
                                    interestRow.add(DateUtil.getStringDate((Date) paramMap.get("START")));
                                    interestRow.add(DateUtil.getStringDate((Date) paramMap.get("END")));
                                    interestRow.add(new Double(intTrfAmt));
                                    interestRow.add(acctDtlMap.get("INT_PAY_ACC_NO"));
                                    interestRow.add("Y");
                                    hmap.put("DEPOSIT_NO", actNum);
                                    hmap.put("LOAN_NO", CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_ACC_NO")));
                                    List LienList = sqlMap.executeQueryForList("getLienAccNo", hmap);
                                    List RdList = sqlMap.executeQueryForList("getRDAccountDetails", hmap);
                                    List LTDList = sqlMap.executeQueryForList("getLTDAccountDetailsForInterestPaying", hmap);
                                    if (LTDList != null & LTDList.size() > 0) {
                                        hmap = (HashMap) LTDList.get(0);
                                        String behave = CommonUtil.convertObjToStr(hmap.get("BEHAVES_LIKE"));
                                        double totLtdamt = CommonUtil.convertObjToDouble(hmap.get("TOTAL_BALANCE")).doubleValue();
                                        String ltd = "LOANS_AGAINST_DEPOSITS";
                                        String acno = CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_ACC_NO"));
                                        String pid = CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_PROD_ID"));
                                        String pname = CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_PROD_TYPE"));
                                        //
                                        if (pname.equals("TL")) {
                                            HashMap loanMap = calcLoanPayments(acno, pid, pname);
                                            totLtdamt = CommonUtil.convertObjToDouble(loanMap.get("TOTAL_DEMAND")).doubleValue();
                                            if (totLtdamt > intTrfAmt) {
                                                hshMap = new HashMap();
                                                hshMap.put("ACCT_TYPE", "LOANS_AGAINST_DEPOSITS");
                                                hshMap.put("AMOUNT", new Double(totLtdamt));
                                                if (LienList.size() > 0 && LienList != null) {
                                                    hshMap.put("CHANGE_COLOR", "TRUE");
                                                } else {
                                                    hshMap.put("CHANGE_COLOR", "");
                                                }
                                                rdList.add(hshMap);
                                                setAccountsList(rdList);
                                                interestList.add(interestRow);

                                            }
                                        } else {
                                            hshMap = new HashMap();
                                            hshMap.put("ACCT_TYPE", "");
                                            hshMap.put("AMOUNT", "");
                                            if (LienList.size() > 0 && LienList != null) {
                                                hshMap.put("CHANGE_COLOR", "TRUE");
                                            } else {
                                                hshMap.put("CHANGE_COLOR", "");
                                            }
                                            rdList.add(hshMap);
                                            setAccountsList(rdList);
                                            interestList.add(interestRow);
                                        }
                                    } else if (RdList != null && RdList.size() > 0) {
                                        hmap = (HashMap) RdList.get(0);
                                        String recurring = "RECURRING";
                                        String behave = CommonUtil.convertObjToStr(hmap.get("BEHAVES_LIKE"));
                                        String amount1 = CommonUtil.convertObjToStr(hmap.get("DEPOSIT_AMT"));
                                        Date mdate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hmap.get("MATURITY_DT")));
                                        double actamount = intTrfAmt;
                                        double rdamount = CommonUtil.convertObjToDouble(amount1).doubleValue();
                                        Date RDMatDt = (Date) currentDate.clone();
                                        RDMatDt.setDate(mdate.getDate());
                                        RDMatDt.setMonth(mdate.getMonth());
                                        RDMatDt.setYear(mdate.getYear());
                                        if (behave.equals(recurring)) {
                                            if (postAmount > 0.0) {
                                                actamount = actamount - postAmount;
                                            } else if (renewpostAmt > 0.0) {
                                                actamount = actamount - renewpostAmt;
                                            }
                                            if (DateUtil.dateDiff(currentDate, RDMatDt) > 0 && actamount >= rdamount) {
                                                hshMap = new HashMap();
                                                hshMap.put("ACCT_TYPE", "RECURRING");
                                                hshMap.put("AMOUNT", amount1);
                                                if (LienList.size() > 0 && LienList != null) {
                                                    hshMap.put("CHANGE_COLOR", "TRUE");
                                                } else {
                                                    hshMap.put("CHANGE_COLOR", "");
                                                }
                                                interestList.add(interestRow);
                                                rdList.add(hshMap);
                                                setAccountsList(rdList);
                                            }
                                        } else {
                                            hshMap = new HashMap();
                                            hshMap.put("ACCT_TYPE", "");
                                            hshMap.put("AMOUNT", "");
                                            if (LienList.size() > 0 && LienList != null) {
                                                hshMap.put("CHANGE_COLOR", "TRUE");
                                            } else {
                                                hshMap.put("CHANGE_COLOR", "");
                                            }
                                            rdList.add(hshMap);
                                            setAccountsList(rdList);
                                            interestList.add(interestRow);
                                        }
                                    } else {

                                        String acno = CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_ACC_NO"));
                                        String pid = CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_PROD_ID"));
                                        String pname = CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_PROD_TYPE"));


                                        if (pname.equals(mds)) {
                                            HashMap mdsMap = calcMDS(acno, pid, pname);
                                            double totalDemand = 0.0;
                                            String demand = "";
                                            String principal = "";
                                            String penal = "";
                                            String bonus = "";
                                            String discount = "";
                                            String arbitration = "";
                                            String charges = "";
                                            String interest = "";
                                            String notice = "";
                                            if (mdsMap != null) {
                                                totalDemand = CommonUtil.convertObjToDouble(mdsMap.get("TOTAL_DEMAND")).doubleValue();
                                                demand = CommonUtil.convertObjToStr(mdsMap.get("TOTAL_DEMAND"));
                                                principal = CommonUtil.convertObjToStr(mdsMap.get("PRINCIPAL"));
                                                penal = CommonUtil.convertObjToStr(mdsMap.get("PENAL"));
                                                bonus = CommonUtil.convertObjToStr(mdsMap.get("BONUS"));
                                                discount = CommonUtil.convertObjToStr(mdsMap.get("DISCOUNT"));
                                                charges = CommonUtil.convertObjToStr(mdsMap.get("CHARGES"));
                                                interest = CommonUtil.convertObjToStr(mdsMap.get("INTEREST"));
                                                arbitration = CommonUtil.convertObjToStr(mdsMap.get("ARBITRATION"));
                                                notice = CommonUtil.convertObjToStr(mdsMap.get("NOTICE_AMT"));
                                            }
                                            //                                            acno = acno.lastIndexOf("_")!=-1 ? acno.substring(0,acno.length()-2) : acno;
                                            HashMap mdsHashMap = new HashMap();
                                            mdsHashMap.put("CHITTALNO", acno);
                                            mdsHashMap = (HashMap) sqlMap.executeQueryForList("getMDSAccountDetailsForInterestPaying", mdsHashMap).get(0);
                                            Date chitEndDt = setProperDtFormat(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(mdsHashMap.get("CHIT_END_DT"))));
                                            if (totalDemand > 0) {
                                                if (postAmount > 0.0) {
                                                    intTrfAmt = intTrfAmt - postAmount;
                                                } else if (renewpostAmt > 0.0) {
                                                    intTrfAmt = intTrfAmt - renewpostAmt;
                                                }
                                            }
                                            if (intTrfAmt >= totalDemand && DateUtil.dateDiff(currentDate, chitEndDt) > 0) {
                                                hshMap = new HashMap();
                                                hshMap.put("ACCT_TYPE", "MDS");
                                                hshMap.put("ACCT_NUM", CommonUtil.convertObjToStr(acctDtlMap.get("INT_PAY_ACC_NO")));
                                                hshMap.put("PRINCIPAL", principal);
                                                hshMap.put("TOTAL_DEMAND", demand);
                                                hshMap.put("PENAL", penal);
                                                hshMap.put("BONUS", bonus);
                                                hshMap.put("DISCOUNT", discount);
                                                hshMap.put("CHARGES", charges);
                                                hshMap.put("INTEREST", interest);
                                                hshMap.put("ARBITRATION", arbitration);
                                                hshMap.put("NOTICE_AMT", notice);
                                                if (LienList.size() > 0 && LienList != null) {
                                                    hshMap.put("CHANGE_COLOR", "TRUE");
                                                } else {
                                                    hshMap.put("CHANGE_COLOR", "");
                                                }
                                                rdList.add(hshMap);
                                                setAccountsList(rdList);
                                                interestList.add(interestRow);

                                                mdsHashMap = null;
                                            }

                                        } else {
                                            hshMap = new HashMap();
                                            hshMap.put("ACCT_TYPE", "");
                                            hshMap.put("AMOUNT", "");
                                            rdList.add(hshMap);
                                            setAccountsList(rdList);
                                            interestList.add(interestRow);
                                        }

                                    }
                                }
                            } catch (Exception e) {
                                if (isTransaction) {
                                    //sqlMap.rollbackTransaction();
                                    if (errorMap == null) {
                                        errorMap = new HashMap();
                                    }
                                    String actNum = CommonUtil.convertObjToStr(paramMap.get("ACT_NUM"));
                                    actNum = actNum.lastIndexOf("_") != -1 ? actNum.substring(0, actNum.length() - 2) : actNum;
                                    errorMap.put(actNum, new TTException(e));
                                    if (interestList.size() >= 1) {
                                        interestList.set(0, errorMap);
                                    } else {
                                        interestList.add(0, errorMap);
                                    }
                                }
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

        }
        paramMap = null;
    }
    

    private long calculateDateDifference(Date currentDate, Date matDate) {
        long difference = DateUtil.dateDiff(currentDate, matDate);
        return difference;
    }
}
