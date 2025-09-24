
/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * MyReportViewer.java
 */

package com.see.truetransact.ui.payroll.SalaryProcess;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author anjuanand
 */
public class MyReportViewer {

    SalaryProcessOB observable;
    private Date salarDate;
    public boolean isCheck = false;
    private String salaryDate;

    public String getSalaryDate() {
        return salaryDate;
    }

    public void setSalaryDate(String salaryDate) {
        this.salaryDate = salaryDate;
    }
    
    public Date getSalarDate() {
        return salarDate;
    }

    public void setSalarDate(Date salarDate) {
        this.salarDate = salarDate;
    }

    public MyReportViewer() {
        observable = new SalaryProcessOB();
    }

    public void openURL(String url) {
        String osName = System.getProperty("os.name");
        try {
            if (osName.startsWith("Windows")) {
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
            } else {
                String[] browsers = {"firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape", "chrome"};
                String browser = null;
                for (int count = 0; count < browsers.length && browser == null; count++) {
                    if (Runtime.getRuntime().exec(new String[]{"which", browsers[count]}).waitFor() == 0) {
                        browser = browsers[count];
                    }
                }
                Runtime.getRuntime().exec(new String[]{browser, url});
            }
        } catch (Exception e) {
            System.out.println("Error in opening browser" + e);
            e.printStackTrace();
        }
    }

    public String acquitenceReport(String payrollId, String screen) {
        HashMap payrollIdMap = new HashMap();
        payrollIdMap.put("PAYROLLID", payrollId);

        String reports = "", start = "", end = "", content = "";
        String strBName = "", strEmpId = "", strEmpName = "", strDes = "", strAccNo = "", GE = "", GD = "";
        try {
            isCheck = false;
            PrintWriter pw = new PrintWriter(new FileWriter(CommonConstants.SERVER_PATH + "/report/processreport.html"));

            start = "<style type=text/css> tr.lightblue{ "
                    + "color: black;"
                    + "font-weight: bold;"
                    + "font-family: sans-serif, serif;"
                    + " font-size: 18px;"
                    + " caption-side: top;"
                    + " padding: 5px;"
                    + " border-bottom: 5px solid black;"
                    + " }"
                    + "   tr.light{ "
                    + "  background-color: #CCCCCC;"
                    + " color: black;"
                    + " font-weight: bold;"
                    + " font-family: sans-serif, serif;"
                    + "  font-size: 18px;"
                    + " caption-side: top;"
                    + "  padding: 5px;"
                    + " border-bottom: 5px solid black"
                    + "  }     "
                    + ".style1 { "
                    + "font-family: Arial, Helvetica, sans-serif;"
                    + "font-size: 12px;"
                    + "}"
                    + "   .one "
                    + " { "
                    + "border:5px solid red;"
                    + " border-width:5px; "
                    + " }"
                    + "</style>";
            start += "<HTML><HEAD><TITLE>Process Report </TITLE></HEAD><BODY>";
            start = "<style type=text/css> tr.lightblue{ "
                    + "color: black;"
                    + "font-weight: bold;"
                    + "font-family: sans-serif, serif;"
                    + " font-size: 18px;"
                    + " caption-side: top;"
                    + " padding: 5px;"
                    + " border-bottom: 5px solid black;"
                    + " }"
                    + "   tr.light{ "
                    + "  background-color: #CCCCCC;"
                    + //    "   border:thin;"+
                    " color: black;"
                    + " font-weight: bold;"
                    + " font-family: sans-serif, serif;"
                    + "  font-size: 18px;"
                    + " caption-side: top;"
                    + "  padding: 5px;"
                    + " border-bottom: 5px solid black"
                    + "  }     "
                    + ".style1 { "
                    + "font-family: Arial, Helvetica, sans-serif;"
                    + "font-size: 12px;"
                    + "}"
                    + "   .one "
                    + " { "
                    + "border:5px solid red;"
                    + " border-width:5px; "
                    + " }"
                    + "</style>";
            start += "<HTML><HEAD><TITLE>Process Report </TITLE></HEAD><BODY>";
            content += "<table width=100% border=1 cellspacing=0 cellpadding=0 >";
            content += "<tr class=lightblue>"
                    + "  "
                    + " <td colspan=4 rowspan=2><div align=center>" + observable.getBankName() + " </div>      <div align=center>"
                    + " <p>Payroll Report For the Month of " + getSalaryDate() + "</p> "
                    + " </div></td> "
                    + " </tr></table>";

            List paycodesEarnings = null;
            paycodesEarnings = observable.getPayDescEarnings();
            content += "<table border=1 cellspacing=0 cellpadding=5><tr></tr><tr><td> Sl.No</td><td>Staff Name</td>";
            ArrayList<HashMap> earnList = new ArrayList<HashMap>();
            for (int i = 0; i < paycodesEarnings.size(); i++) {
                HashMap payEarn = new HashMap();
                HashMap earnMap = new HashMap();
                payEarn = (HashMap) paycodesEarnings.get(i);
                String earnCode = "";
                String earnDesc = "";
                earnCode = CommonUtil.convertObjToStr(payEarn.get("PAY_CODE"));
                earnDesc = CommonUtil.convertObjToStr(payEarn.get("PAY_DESCRI"));
                content += "<td>" + earnDesc + "</td>";
                earnMap.put("pay_code", earnCode);
                earnMap.put("pay_descri", earnDesc);
                earnList.add(earnMap);
            }
            content += "<td>Total Earn</td>";
            List paycodesDeductions = null;
            paycodesDeductions = observable.getPayDescDeductions();
            ArrayList<HashMap> deduList = new ArrayList<HashMap>();
            for (int j = 0; j < paycodesDeductions.size(); j++) {
                HashMap payDedu = new HashMap();
                HashMap deduMap = new HashMap();
                payDedu = (HashMap) paycodesDeductions.get(j);
                String deduCode = "";
                String deduDesc = "";
                deduCode = CommonUtil.convertObjToStr(payDedu.get("PAY_CODE"));
                deduDesc = CommonUtil.convertObjToStr(payDedu.get("PAY_DESCRI"));
                content += "<td>" + deduDesc + "</td>";
                deduMap.put("pay_code", deduCode);
                deduMap.put("pay_descri", deduDesc);
                deduList.add(deduMap);
            }
            content += "<td>Total Recovery</td>";
            content += "<td>Net Salary</td>";
            content += "<td>Signature</td>";
            content += "</tr><tr></tr><tr></tr>";
            List empReportList = null;
            empReportList = observable.getEmployeeReportDetails();
            int i = 0;
            Double et = 0.0;
            Double netsal = 0.0;
            String nt = "";
            double dSum = 0.0;
            double cSum = 0.0;
            String ssss = "";
            String ttss = "";
            String ddd = "";
            Double gg = 0.0;
            String uu = "";
            String es = "";
            String ds = "";
            for (int k = 0; k < empReportList.size(); k++) {
                HashMap empReportMap = new HashMap();
                empReportMap = (HashMap) empReportList.get(k);
                isCheck = true;
                if (strBName != null) {
                    strBName = CommonUtil.convertObjToStr(empReportMap.get("BANK_NAME"));
                } else {
                    strBName = "";
                }
                if (strEmpId != null) {
                    strEmpId = CommonUtil.convertObjToStr(empReportMap.get("EMPLOYEEID"));
                } else {
                    strEmpId = "";
                }
                if (strEmpName != null) {
                    strEmpName = CommonUtil.convertObjToStr(empReportMap.get("EMPLOYEE_NAME"));
                } else {
                    strEmpName = "";
                }
                if (strDes != null) {
                    strDes = CommonUtil.convertObjToStr(empReportMap.get("designation"));
                } else {
                    strDes = "";
                }
                if (strAccNo != null) {
                    strAccNo = CommonUtil.convertObjToStr(empReportMap.get("net_salary_acc_no"));
                } else {
                    strAccNo = "";
                }
                i = i + 1;
                content += "<tr height=50>"
                        + " <td> " + i + "</td>"
                        + " <td> " + strEmpName + "</td>";
                double earnSum = 0.0;
                int count = 0;
                for (int l = 0; l < earnList.size(); l++) {
                    HashMap eMap = new HashMap();
                    eMap = earnList.get(l);
                    double b = 0.0;
                    HashMap whereMap = new HashMap();
                    whereMap.put("strEmpId", strEmpId);
                    whereMap.put("pay_code", CommonUtil.convertObjToStr(eMap.get("pay_code")));

                    if (screen.equals("Report")) {
                        whereMap.put("SalaryMonth", getSalarDate());
                        b = getAmountFromDbForAcquitance(whereMap);
                    } else {
                        whereMap.put("payrollId", payrollId);
                        b = getAmountFromDbForAcquitance(whereMap);
                    }
                    String bs = CommonUtil.convertObjToStr(b);
                    earnSum = earnSum + b;
                    content += " <td> " + formatCrore(bs) + "</td>";
                }
                List payrollDataList = null;
                payrollIdMap = new HashMap();
                if (screen.equals("Report")) {
                    Date dt = getSalarDate();
                    dt.setDate(1);
                    payrollIdMap.put("SalaryMonth", dt);
                } else {
                    payrollIdMap.put("PAYROLLID", payrollId);
                }
                payrollDataList = observable.getPayrollEarnData(payrollIdMap);
                if (payrollDataList != null && payrollDataList.size() > 0) {
                    for (int m = 0; m < payrollDataList.size(); m++) {
                        HashMap payrollDataMap = new HashMap();
                        payrollDataMap = (HashMap) payrollDataList.get(m);
                    }
                }
                String net = "";
                es = "" + earnSum;
                net = es;
                et = et + CommonUtil.convertObjToDouble(es);
                ssss = "" + et;
                content += " <td> " + formatCrore(es) + "</td>";
                double deduSum = 0.0;
                for (int n = 0; n < deduList.size(); n++) {
                    HashMap dMap = new HashMap();
                    dMap = deduList.get(n);
                    double b = 0;
                    HashMap whereMap = new HashMap();
                    whereMap.put("strEmpId", strEmpId);
                    whereMap.put("pay_code", CommonUtil.convertObjToStr(dMap.get("pay_code")));
                    if (screen.equals("Report")) {
                        whereMap.put("SalaryMonth", getSalarDate());
                        b = getAmountFromDbForAcquitance(whereMap);
                    } else {
                        whereMap.put("payrollId", payrollId);
                        b = getAmountFromDbForAcquitance(whereMap);
                    }
                    String bs = "" + b;
                    deduSum = deduSum + b;
                    content += " <td> " + formatCrore(bs) + "</td>";
                }
                ds = "" + deduSum;
                nt = ds;
                dSum = dSum + CommonUtil.convertObjToDouble(ds);
                ddd = "" + dSum;
                content += " <td> " + formatCrore(ds) + "</td>";
                Double iw = CommonUtil.convertObjToDouble(es) - CommonUtil.convertObjToDouble(ds);//-ddd;
                String ix = "" + iw;
                content += " <td> " + formatCrore(ix) + "</td>";
                content += "<td style='color: white;'>" + "." + "</td>";
            }
            content += "<tr height=50><td></td><td>Grand Total</td>";
            payrollIdMap = new HashMap();
            if (screen.equals("Report")) {
                Date dt = getSalarDate();
                dt.setDate(1);
                payrollIdMap.put("SalaryMonth", dt);
            } else {
                payrollIdMap.put("PAYROLLID", payrollId);
            }
            List payrollEarnDataList = null;

            payrollEarnDataList = observable.getPayrollEarningsData(payrollIdMap);
            if (payrollEarnDataList != null && payrollEarnDataList.size() > 0) {
                for (int p = 0; p < payrollEarnDataList.size(); p++) {
                    HashMap payrollEarnMap = new HashMap();
                    payrollEarnMap = (HashMap) payrollEarnDataList.get(p);
                    double earnAmount = 0.0;
                    earnAmount = CommonUtil.convertObjToDouble(payrollEarnMap.get("AMOUNT"));
                    content += "<td>" + formatCrore(CommonUtil.convertObjToStr(earnAmount)) + "</td>";
                }
            }
            content += " <td> " + formatCrore(ssss) + "</td>";
            List payrollDeduDataList = null;
            payrollIdMap = new HashMap();
            if (screen.equals("Report")) {
                Date dt = getSalarDate();
                dt.setDate(1);
                payrollIdMap.put("SalaryMonth", dt);
            } else {
                payrollIdMap.put("PAYROLLID", payrollId);
            }
            payrollDeduDataList = observable.getPayrollDeductionsData(payrollIdMap);
            if (payrollDeduDataList != null && payrollDeduDataList.size() > 0) {
                for (int p = 0; p < payrollDeduDataList.size(); p++) {
                    HashMap payrollDeduMap = new HashMap();
                    payrollDeduMap = (HashMap) payrollDeduDataList.get(p);
                    double deduAmount = 0.0;
                    deduAmount = CommonUtil.convertObjToDouble(payrollDeduMap.get("AMOUNT"));
                    content += "<td>" + formatCrore(CommonUtil.convertObjToStr(deduAmount)) + "</td>";
                }
            }
            content += " <td> " + formatCrore(ddd) + "</td>";
            Double w = CommonUtil.convertObjToDouble(ssss) - CommonUtil.convertObjToDouble(ddd);
            String x = "" + w;
            content += " <td> " + formatCrore(x) + "</td>";
            content += "</tr>";
            content += "</tr>";
            i = i + 1;
            content += "</TABLE>";
            if (!isCheck) {
                isCheck = false;
                return null;
            }
            end = "</BODY></HTML>";

            reports = start + content + end;

            pw.println(reports);

            pw.close();
            // return reports;
        } catch (Exception e) {

            e.printStackTrace();
        }
        return reports;
    }

    public static String formatCrore(String str) {
        java.text.DecimalFormat numberFormat = new java.text.DecimalFormat();
        numberFormat.applyPattern("########################0.00");
        double currData = CommonUtil.convertObjToDouble(str.replaceAll(",", ""));
        str = numberFormat.format(currData);
        String num = str.substring(0, str.lastIndexOf(".")).replaceAll(",", "");
        String dec = str.substring(str.lastIndexOf("."));
        String sign = "";
        if (num.substring(0, 1).equals("-")) {
            sign = num.substring(0, 1);
            num = num.substring(1, num.length());
        }
        char[] chrArr = num.toCharArray();
        StringBuffer fmtStrB = new StringBuffer();
        for (int i = chrArr.length - 1, j = 0, k = 0; i >= 0; i--) {
            if ((j == 3 && k == 3) || (j == 2 && k == 5) || (j == 2 && k == 7)) {
                fmtStrB.insert(0, ",");
                if (k == 7) {
                    k = 0;
                }
                j = 0;
            }
            j++;
            k++;

            fmtStrB.insert(0, chrArr[i]);
        }
        fmtStrB.append(dec);
        str = fmtStrB.toString();
        str = sign + str;
        if (str.equals(".00")) {
            str = "0";
        }
        return str;
    }

    private Double getAmountFromDbForAcquitance(HashMap where) throws SQLException {

        String strEmpId = CommonUtil.convertObjToStr(where.get("strEmpId"));
        String paycode = CommonUtil.convertObjToStr(where.get("pay_code"));
        Double empAmount = 0.0;
        HashMap empAmtMap = new HashMap();
        empAmtMap.put("EMPID", strEmpId);
        empAmtMap.put("PAYCODE", paycode);
        if (where.containsKey("payrollId") && where.get("payrollId") != null) {
            empAmtMap.put("PAYROLLID", CommonUtil.convertObjToStr(where.get("payrollId")));

        } else {
            Date dt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(where.get("SalaryMonth")));
            dt.setDate(1);
            empAmtMap.put("SalaryMonth", dt);
        }
        List empAmtList = null;
        empAmtList = observable.getEmpPayrollAmt(empAmtMap);

        if (empAmtList != null && empAmtList.size() > 0) {
            for (int m = 0; m < empAmtList.size(); m++) {
                HashMap empAmountMap = new HashMap();
                empAmountMap = (HashMap) empAmtList.get(m);
                if (empAmountMap.containsKey("AMOUNT") && empAmountMap.get("AMOUNT") != null) {
                    empAmount = CommonUtil.convertObjToDouble(empAmountMap.get("AMOUNT"));
                    return empAmount;
                }
            }
        }
        return empAmount;
    }
}
