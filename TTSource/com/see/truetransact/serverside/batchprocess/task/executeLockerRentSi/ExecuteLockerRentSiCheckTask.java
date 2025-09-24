/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ExecuteLockerRentSiCheckTask.java
 *
 * Created on February 28, 2005, 12:23 PM
 */
package com.see.truetransact.serverside.batchprocess.task.executeLockerRentSi;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.serverside.batchprocess.task.Task;
import com.see.truetransact.serverside.batchprocess.task.TaskStatus;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverside.batchprocess.BatchConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.serverside.locker.lockerSI.LockerRentSIApplicationDAO;
import com.see.truetransact.ui.locker.lockerSI.LockerRentSIApplicationUI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @author 152691 This class checks if cash transaction from Vault and teller
 * tally Is to be called as a part of the Day End batch process
 */
public class ExecuteLockerRentSiCheckTask extends Task {

    private static SqlMap sqlMap = null;
    private String branch = null;
    private String process = null;
    HashMap vaultMap = new HashMap();
    private Date expiryDate = null;
    private int commision;
    private int Penal_rate;
    private int no_of_days;
    private String expDt = "";
    ArrayList finalList = new ArrayList();

    public ExecuteLockerRentSiCheckTask(TaskHeader header) throws Exception {
        setHeader(header);
        process = header.getProcessType();
        branch = header.getBranchID();
        initializeTaskObj(header.getTaskParam());
    }

    private void initializeTaskObj(HashMap dataMap) throws Exception {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();

    }

    public String getDateddMMyyyy(java.util.Date strDate1) {

        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String strDate = df.format(strDate1);

        SimpleDateFormat dateFormat = null;
        java.util.Date varDate = null;
        try {
            //String strDate="23-Mar-2011";
            dateFormat = new SimpleDateFormat("MM/dd/yyyy");//

            varDate = (java.util.Date) dateFormat.parse(strDate);
            dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        } catch (Exception e) {
            e.printStackTrace();;
        }
        return dateFormat.format(varDate);
    }

    private double setLockerCha(String prodID) {
        try {
            Date currDate = ServerUtil.getCurrentDate(super._branchCode);

            Date tempDt = (Date) currDate.clone();
            HashMap stMap = new HashMap();
            stMap.put("PROD_ID", prodID);
            stMap.put("TODAY_DT", tempDt);
            stMap.put("CHARGE_TYPE", "RENT_CHARGES");
            System.out.println("stMap====" + stMap);
            List stList = sqlMap.executeQueryForList("getSelectPenalty", stMap);
            if (stList != null && stList.size() > 0) {
                stMap = null;
                stMap = (HashMap) stList.get(0);
                int count_commi = Integer.parseInt(stMap.get("COUNT_COMM").toString());
                System.out.println("count_commi=======" + count_commi);
                java.util.Date ldate = tempDt;
                if (count_commi == 1) {
                    int lyear = expiryDate.getYear() + 1900;
                    //java.util.Date c=currDt;
                    int cyear = tempDt.getYear() + 1900;
                    int year_diff = cyear - lyear;
//                    System.out.println("year_diff===="+year_diff);

                    HashMap stMap1 = new HashMap();
                    stMap1.put("PROD_ID", prodID);
                    stMap1.put("TODAY_DT", tempDt);
                    stMap1.put("CHARGE_TYPE", "RENT_CHARGES");
                    List stList1 = sqlMap.executeQueryForList("getSelectCommission", stMap1);
                    if (stList1 != null && stList1.size() > 0) {
                        stMap1 = null;
                        stMap1 = (HashMap) stList1.get(0);
                        //commision = Integer.parseInt(stMap1.get("COMMISION").toString());
                        commision = CommonUtil.convertObjToInt(stMap1.get("COMMISION"));
                        System.out.println("commision=====" + commision);
                    }
                    HashMap stMap2 = new HashMap();
                    System.out.println("prodID===" + prodID);
                    stMap2.put("PROD_ID", prodID);
                    List stList2 = sqlMap.executeQueryForList("getSelectPenalRate", stMap2);
                    System.out.println("stList2=====" + stList2);
                    if (!stList2.isEmpty()&&stList2 != null && stList2.size() > 0) {
                        stMap2 = null;
                        stMap2 = (HashMap) stList2.get(0);
                        Penal_rate = Integer.parseInt(stMap2.get("PENAL_RATE_OF_INTEREST").toString());
                        System.out.println("Penal_rate=====" + Penal_rate);
                    }
//                        System.out.println("commision===="+commision);
//                        System.out.println("no_of_days===="+no_of_days);
//                        System.out.println("Penal_rate===="+Penal_rate);


                    if (year_diff <= 1) {
                        System.out.println("ldate===" + ldate);
                        System.out.println("expiryDate====" + expDt);
                        //int no_of_days1 = (int) ((ldate.getTime() - expDt.getTime()));
                        no_of_days = (int) ((ldate.getTime() - expiryDate.getTime()) / (1000 * 60 * 60 * 24));
                        double penal_Amt = (commision * no_of_days * Penal_rate) / 36500.0;
                        penal_Amt = (double) getNearest((long) (penal_Amt * 100), 100) / 100;
                        System.out.println("penal_Amt====" + penal_Amt);
                        //txtPenalAmt.setText(CommonUtil.convertObjToStr(penal_Amt));
                        return penal_Amt;
                    } else if (year_diff > 1) {
                        Date due_date = expiryDate;
                        Date todayDt = ldate;
                        String dDate = getDateddMMyyyy(due_date);
                        System.out.println("dDate================" + dDate);
                        int no_of_days = 0;
                        double penal_Amt = 0.0;
                        java.util.Date dt1 = null;
//                java.util.Date expirydate=DateUtil.getDateMMDDYYYY(due_date);
                        do {
                            no_of_days = (int) ((todayDt.getTime() - due_date.getTime()) / (1000 * 60 * 60 * 24));
                            penal_Amt = penal_Amt + ((commision * no_of_days * Penal_rate) / 36500.0);
                            String dt = "";  // Start date
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(sdf.parse(dDate));
                            cal.add(Calendar.YEAR, 1);  // number of days to add
                            dDate = sdf.format(cal.getTime());
                            dt1 = sdf.parse(dDate);
                            due_date = dt1;
                        } while (DateUtil.dateDiff(todayDt, dt1) < 0);
                        penal_Amt = (double) getNearest((long) (penal_Amt * 100), 100) / 100;
                        //txtPenalAmt.setText(CommonUtil.convertObjToStr(penal_Amt));
                        return penal_Amt;
                    }
                } else if (count_commi > 1) {

                    String incrdDate = null;
                    Date incrUtildt1 = null;
                    Double penal_Amt = 0.0;
                    Date due_date = expiryDate;
                    Date todayDt = ldate;
                    do {
                        String dDate = getDateddMMyyyy(due_date);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(sdf.parse(dDate));
                        cal.add(Calendar.YEAR, 1);
                        incrdDate = sdf.format(cal.getTime());
                        incrUtildt1 = sdf.parse(incrdDate);
                        HashMap stMapn = new HashMap();
                        stMapn.put("PROD_ID", prodID);
                        System.out.println("due_date===" + due_date);
                        stMapn.put("BEG_DAT", due_date);
                        System.out.println("incrUtildt1===" + incrUtildt1);
                        stMapn.put("LAST_DAT", incrUtildt1);
                        stMapn.put("CHARGE_TYPE", "RENT_CHARGES");
                        List stListn = sqlMap.executeQueryForList("getSelectCommissionForIncr", stMapn);
                        //System.out.println("incrUtildt1==="+incrUtildt1);
                        if (stListn != null && stListn.size() > 0) {
                            System.out.println("stListn===" + stListn);
                            stMapn = null;
                            stMapn = (HashMap) stListn.get(0);
                            System.out.println("stListn===" + stListn);
                            //commision = Integer.parseInt(stMapn.get("COMMISION").toString());
                            commision = CommonUtil.convertObjToInt(stMapn.get("COMMISION"));
                            System.out.println("commision=====" + commision);
                        } else {
                            HashMap stMapx = new HashMap();
                            stMapx.put("PROD_ID", prodID);
                            stMapx.put("CHARGE_TYPE", "RENT_CHARGES");
                            List stListx = sqlMap.executeQueryForList("getSelectCommForIncr", stMapx);
                            if (stListx != null && stListx.size() > 0) {
                                stMapx = null;
                                stMapx = (HashMap) stListx.get(count_commi - 1);
                                System.out.println("stMapx=====" + stMapx);
                                //commision = Integer.parseInt(stMapx.get("COMMISION").toString());
                                commision = CommonUtil.convertObjToInt(stMapn.get("COMMISION"));
                                System.out.println("commision=====" + commision);
                            }
                        }
                        System.out.println("todaydate====" + todayDt);
                        System.out.println("due_date====" + due_date);
                        no_of_days = (int) ((todayDt.getTime() - due_date.getTime()) / (1000 * 60 * 60 * 24));
                        System.out.println("no_of_days=====" + no_of_days);

                        HashMap stMap2 = new HashMap();
                        System.out.println("prodID===" + prodID);
                        stMap2.put("PROD_ID", prodID);
                        List stList2 = sqlMap.executeQueryForList("getSelectPenalRate", stMap2);
                        System.out.println("stList2=====" + stList2);
                        if (stList2 != null && stList2.size() > 0) {
                            stMap2 = null;
                            stMap2 = (HashMap) stList2.get(0);
                            Penal_rate = Integer.parseInt(stMap2.get("PENAL_RATE_OF_INTEREST").toString());
                            System.out.println("Penal_rate=====" + Penal_rate);
                        }
                        System.out.println("penal_-amt" + penal_Amt);
                        penal_Amt = penal_Amt + ((commision * no_of_days * Penal_rate) / 36500.0);
                        System.out.println("penal_Amt=====" + penal_Amt);
                        due_date = incrUtildt1;
                        System.out.println("due_date=====" + due_date);


                    } while (DateUtil.dateDiff(todayDt, incrUtildt1) < 0);
                    penal_Amt = (double) getNearest((long) (penal_Amt * 100), 100) / 100;
                    //txtPenalAmt.setText(CommonUtil.convertObjToStr(penal_Amt));
                    return penal_Amt;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
//        else
//        {
////            ClientUtil.showMessageWindow("Error In Populating Comission and Service Tax");
//        }
        return 0.0;
    }

    public void getRentCalculationProcess(int mm, int yyyy) {
//        int n=tblLockerRentSIApplication.getSelectedRow();
        HashMap data = new HashMap();
        HashMap retMap = new HashMap();

        Date currDate = ServerUtil.getCurrentDate(super._branchCode);
        String ryear = CommonUtil.convertObjToStr(yyyy);
        String rmonth = CommonUtil.convertObjToStr(mm);
        java.util.Date upexpdt = DateUtil.getDate(31, mm, yyyy);
        System.out.println("mm&&&&   " + mm);
        System.out.println("yyyy&&&&   " + yyyy);
        Date upexp = (Date) currDate.clone();
        upexp.setDate(31);
        upexp.setMonth(upexpdt.getMonth());
        upexp.setYear(upexpdt.getYear());
        String action = "INSERT";
//        String lockerType = CommonUtil.convertObjToStr(((ComboBoxModel)(cboLockerType.getModel())).getKeyForSelected());
        HashMap hmap = new HashMap();
//        hmap.put("PROD_ID",lockerType);
        hmap.put("EXP_DT", upexp);
        finalList = getrentCalculation(mm, yyyy);
        System.out.println("finalList  " + finalList);
//        ArrayList finalList = ((EnhancedTableModel)tblLockerRentSIApplication.getModel()).getDataArrayList();
        try {
            List llist = sqlMap.executeQueryForList("getLockDetailsForDayEnd", hmap);
            hmap = null;
            if (llist != null && llist.size() > 0) {
                hmap = (HashMap) llist.get(0);

                LockerRentSIApplicationDAO lockerRentSIDao = new LockerRentSIApplicationDAO();
                data.put("MODE", action);
                data.put("Finallist", finalList);
//            data.put("lockertype",lockerType);
                data.put("name", hmap.get("CUSTOMER_NAME"));
                data.put("ryear", ryear);
                data.put("rmonth", rmonth);
                data.put("DAY_END", "DAY_END");
                data.put("BRANCH_CODE", branch);
                lockerRentSIDao.doServiceTaxTrans(data, "INSERT");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ArrayList getrentCalculation(int mm, int yyyy) {
        java.util.Date upexpdt = DateUtil.getDate(31, mm, yyyy);
        Date currDate = ServerUtil.getCurrentDate(super._branchCode);
        Date upexp = (Date) currDate.clone();
        upexp.setDate(31);
        upexp.setMonth(upexpdt.getMonth());
        upexp.setYear(upexpdt.getYear());
        ArrayList rowList = new ArrayList();
        HashMap where = new HashMap();
        HashMap stMap = new HashMap();
        String lockerType = "";
        stMap.put("EXP_DT", upexp);
        System.out.println("yyyy " + yyyy);
        System.out.println("mm " + mm);
        try {
            List stList = sqlMap.executeQueryForList("getLockDetailsForDayEnd", stMap);
            if (stList != null && stList.size() > 0) {
                stMap = null;
                int s = stList.size();
                System.out.println("size" + s);
                for (int j = 0; j < s; j++) {
                    System.out.println("j is" + j);
                    System.out.println("stList is" + stList);
                    double ser = 0;
                    double dd1 = 0;
                    double val1 = 0;
                    double ser1 = 0;
                    double dd2 = 0;
                    double val2 = 0;
                    double ser2 = 0;
                    double dd3 = 0;
                    double val3 = 0;
                    double ser3 = 0;
                    double dd4 = 0;
                    double val4 = 0;
                    double ser4 = 0;
                    double val = 0;
                    double charg = 0;
                    double service = 0;
                    double totval = 0;
                    double comm;
                    double serv;
                    double dd = 0;
                    int realmont = 0;
                    Date edt = null;
                    stMap = (HashMap) stList.get(j);
                    String locNo = CommonUtil.convertObjToStr(stMap.get("LOCKER_NUM"));
                    String issueDt = CommonUtil.convertObjToStr(stMap.get("ISSUE_DT"));
                    expDt = CommonUtil.convertObjToStr(stMap.get("EXP_DT"));
                    expiryDate = DateUtil.getDateMMDDYYYY(expDt);
                    java.util.Date isdt = DateUtil.getDateMMDDYYYY(issueDt);
                    int k = 1;
                    int date = isdt.getDate();
                    int month = isdt.getMonth() + 1;
                    int year = isdt.getYear() + 1900;
                    java.util.Date exp = DateUtil.getDateMMDDYYYY(expDt);

                    int day = exp.getDate();
                    int mont = exp.getMonth() + 1;;
                    int yyy = exp.getYear() + 1900;
                    System.out.println("isdt  " + isdt);
                    System.out.println("exp  " + exp);
                    System.out.println("issueDt  " + issueDt);
                    System.out.println("expDt  " + expDt);

                    int cal = yyyy - yyy;
                    int calyy = cal * 12;
                    int calmon = mm - mont;
                    int totcal = calmon + calyy;
                    System.out.println("totcal" + totcal);
                    System.out.println("branch" + branch);
                    System.out.println("branch" + super._branchCode);
                    HashMap hash = new HashMap();
                    Date tempDt = (Date) currDate.clone();
                    tempDt.setDate(isdt.getDate());
                    tempDt.setMonth(isdt.getMonth());
                    tempDt.setYear(isdt.getYear());

                    Date tempexDt = (Date) currDate.clone();
                    tempexDt.setDate(exp.getDate());
                    tempexDt.setMonth(exp.getMonth());
                    tempexDt.setYear(exp.getYear());
//                hash.put("PROD_ID",lockerType);
                    hash.put("TODAY_DT", tempDt);
                    hash.put("EXP_DT", tempexDt);
                    hash.put("CHARGE_TYPE", "RENT_CHARGES");
                    hash.put("LOCKER_NUM", locNo);
                    hash.put("BRANCH_ID", branch);
                    if (totcal >= 0) {
                        List lis = sqlMap.executeQueryForList("getExpDetailsForDayEnd", hash);
                        if (lis != null && lis.size() > 0) {
                            hash = null;
                            hash = (HashMap) lis.get(0);
                            hash.put("BRANCH_ID", branch);
                            List LIST = sqlMap.executeQueryForList("getLockerTypeForDayEnd", hash);
                            if (LIST != null && LIST.size() > 0) {
                                HashMap HASHMAP = (HashMap) LIST.get(0);
                                lockerType = CommonUtil.convertObjToStr(HASHMAP.get("PROD_ID"));
                            }
                            String prodId = CommonUtil.convertObjToStr(hash.get("PRODUCT_ID"));
                            String prodType = CommonUtil.convertObjToStr(hash.get("PROD_TYPE"));
                            String actNo = CommonUtil.convertObjToStr(hash.get("CUSTOMER_ID_CR"));
                            String name = CommonUtil.convertObjToStr(hash.get("FNAME"));
                            String bal = CommonUtil.convertObjToStr(hash.get("AVAILABLE_BALANCE"));

                            double penal_Amt = setLockerCha(lockerType);
                            System.out.println("penal_Amt" + penal_Amt);

                            for (int i = yyy; i <= yyyy; i++) {



                                if (mm == mont && yyy == yyyy) {
                                    if (k == 1) {
                                        HashMap h2 = new HashMap();
                                        Date htempmid = (Date) currDate.clone();
                                        htempmid.setDate(exp.getDate());
                                        htempmid.setMonth(exp.getMonth());
                                        Date expdate = (Date) currDate.clone();
                                        expdate.setDate(31);
                                        expdate.setMonth(11);
                                        expdate.setYear(yyyy);
                                        htempmid.setYear(i - 1900);
                                        h2.put("PROD_ID", lockerType);
                                        h2.put("TODAY_DT", htempmid);
                                        h2.put("CHARGE_TYPE", "RENT_CHARGES");
                                        h2.put("EXP_DT", expdate);
                                        List hlit = sqlMap.executeQueryForList("getServiceDetails", h2);
                                        int diffcurmont = exp.getMonth() + 1;
                                        int q = 0;
                                        if (hlit != null && hlit.size() > 0) {
                                            h2 = null;
                                            int calmonth = 12 - diffcurmont;
                                            for (int m = 0; m < hlit.size(); m++) {

                                                h2 = (HashMap) hlit.get(m);
                                                comm = CommonUtil.convertObjToDouble(h2.get("COMMISION")).doubleValue();
                                                serv = CommonUtil.convertObjToDouble(h2.get("SERVICE_TAX")).doubleValue();
                                                String sdate = CommonUtil.convertObjToStr(h2.get("START_DT"));
                                                String eedate = CommonUtil.convertObjToStr(h2.get("END_DT"));



                                                if (eedate != "") {
                                                    edt = DateUtil.getDateMMDDYYYY(eedate);
                                                    Date comparedt = (Date) currDate.clone();
                                                    comparedt.setDate(edt.getDate());
                                                    comparedt.setMonth(edt.getMonth());
                                                    comparedt.setYear(edt.getYear());

                                                    int eeyear = edt.getYear() + 1900;
                                                    int eemonth = edt.getMonth() + 1;

                                                    Date ssdate = DateUtil.getDateMMDDYYYY(sdate);
                                                    int syear = ssdate.getYear() + 1900;
                                                    int smonth = ssdate.getMonth() + 1;


                                                    int diffmonth = (eeyear - syear) + (eemonth - smonth);

                                                    realmont = calmonth - diffmonth + 1;

                                                    if (eeyear == i && DateUtil.dateDiff(htempmid, comparedt) >= 0) {
                                                        if (calmonth != 0) {
                                                            if (q == 0) {
                                                                realmont = eemonth - (exp.getMonth() + 1);
                                                            } else {
                                                                realmont = (eemonth - smonth) + 1;
                                                            }
                                                            double d = comm / 12.0;

                                                            dd1 = dd1 + d * realmont;
                                                            dd = (double) getNearest((long) (dd1 * 100), 100) / 100;
                                                            ser1 = ser1 + ((d * realmont) / 100) * serv;
                                                            ser = (double) getNearest((long) (ser1 * 100), 100) / 100;
                                                            val1 = val1 + dd + ser;
                                                            val = (double) getNearest((long) (val1 * 100), 100) / 100;
                                                            calmonth = calmonth - realmont;
                                                            q++;
                                                        }
                                                    } else if (eeyear > i) {
                                                        if (calmonth != 0) {
                                                            realmont = calmonth;
                                                            double d = comm / 12.0;
                                                            dd1 = dd1 + d * realmont;
                                                            dd = (double) getNearest((long) (dd1 * 100), 100) / 100;
                                                            ser1 = ser1 + ((d * realmont) / 100) * serv;
                                                            ser = (double) getNearest((long) (ser1 * 100), 100) / 100;
                                                            val1 = val1 + dd + ser;
                                                            val = (double) getNearest((long) (val1 * 100), 100) / 100;
                                                            calmonth = calmonth - realmont;
                                                        }
                                                    }
                                                } else {
                                                    if (calmonth != 0) {
                                                        double d = comm / 12.0;

                                                        dd1 = dd1 + d * calmonth;

                                                        dd = (double) getNearest((long) (dd1 * 100), 100) / 100;
                                                        ser1 = ser3 + ((d * calmonth) / 100) * serv;
                                                        ser = (double) getNearest((long) (ser1 * 100), 100) / 100;
                                                        val1 = val3 + dd + ser;
                                                        val = (double) getNearest((long) (val1 * 100), 100) / 100;
                                                        calmonth = calmonth - realmont;
                                                    }

                                                }
                                            }
                                        }




                                        HashMap h3 = new HashMap();
                                        Date hltempmid = (Date) currDate.clone();
                                        hltempmid.setDate(exp.getDate());
                                        hltempmid.setMonth(exp.getMonth());
                                        hltempmid.setYear((i + 1) - 1900);
                                        Date hlexpdate = (Date) currDate.clone();
                                        hlexpdate.setDate(31);
                                        hlexpdate.setMonth(11);
                                        hlexpdate.setYear((i + 1) - 1900);
                                        h3.put("PROD_ID", lockerType);
                                        h3.put("TODAY_DT", hltempmid);
                                        h3.put("EXP_DT", hlexpdate);
                                        h3.put("CHARGE_TYPE", "RENT_CHARGES");
                                        List hllit = sqlMap.executeQueryForList("getServiceDetails", h3);
                                        if (hllit != null && hlit.size() > 0) {
                                            h3 = null;
                                            for (int m = 0; m < hlit.size(); m++) {
                                                h3 = (HashMap) hllit.get(m);
                                                comm = CommonUtil.convertObjToDouble(h3.get("COMMISION")).doubleValue();
                                                serv = CommonUtil.convertObjToDouble(h3.get("SERVICE_TAX")).doubleValue();
                                                String sdate = CommonUtil.convertObjToStr(h3.get("START_DT"));
                                                String eedate = CommonUtil.convertObjToStr(h3.get("END_DT"));
                                                int calmonth = mm;
                                                if (eedate != "") {
                                                    edt = DateUtil.getDateMMDDYYYY(eedate);
                                                    Date comparedt = (Date) currDate.clone();
                                                    comparedt.setDate(edt.getDate());
                                                    comparedt.setMonth(edt.getMonth());
                                                    comparedt.setYear(edt.getYear());
                                                    int eeyear = edt.getYear() + 1900;
                                                    int eemonth = edt.getMonth() + 1;

                                                    Date ssdate = DateUtil.getDateMMDDYYYY(sdate);
                                                    int syear = ssdate.getYear() + 1900;
                                                    int smonth = ssdate.getMonth() + 1;


                                                    int diffmonth = (eeyear - syear) + (eemonth - smonth);

                                                    realmont = calmonth - diffmonth + 1;

                                                    if (eeyear == i) {
                                                        if (calmonth != 0) {
                                                            if (calmonth <= eemonth) {
                                                                realmont = calmonth;
                                                            } else if (calmonth > eemonth) {
                                                                realmont = eemonth;
                                                            }
                                                            double d = comm / 12.0;

                                                            dd = dd1 + (d * realmont);
                                                            dd = (double) getNearest((long) (dd * 100), 100) / 100;
                                                            ser = ser1 + (((d * realmont) / 100) * serv);
                                                            ser = (double) getNearest((long) (ser * 100), 100) / 100;
                                                            val = val1 + dd + ser;
                                                            val = (double) getNearest((long) (val * 100), 100) / 100;
                                                            calmonth = calmonth - realmont;
                                                        }
                                                    } else if (eeyear > i) {
                                                        if (calmonth != 0) {
                                                            realmont = calmonth;
                                                            double d = comm / 12.0;
                                                            dd = dd1 + (d * realmont);
                                                            dd = (double) getNearest((long) (dd * 100), 100) / 100;
                                                            ser = ser1 + ((d * realmont) / 100) * serv;
                                                            ser = (double) getNearest((long) (ser * 100), 100) / 100;
                                                            val = val1 + dd + ser;
                                                            val = (double) getNearest((long) (val * 100), 100) / 100;
                                                            calmonth = calmonth - realmont;
                                                        }
                                                    }
                                                } else {
                                                    if (calmonth != 0) {
                                                        double d = comm / 12.0;

                                                        dd = dd1 + (d * calmonth);

                                                        dd = (double) getNearest((long) (dd * 100), 100) / 100;
                                                        ser = ser1 + (((d * calmonth) / 100) * serv);
                                                        ser = (double) getNearest((long) (ser * 100), 100) / 100;
                                                        val = val1 + dd + ser;
                                                        val = (double) getNearest((long) (val * 100), 100) / 100;
                                                        calmonth = calmonth - realmont;
                                                    }
                                                }
                                            }
                                        }



                                    } else {
                                        HashMap h2 = new HashMap();
                                        Date htempmid = (Date) currDate.clone();
                                        htempmid.setDate(exp.getDate());
                                        htempmid.setMonth(exp.getMonth());
                                        Date expdate = (Date) currDate.clone();
                                        expdate.setDate(31);
                                        expdate.setMonth(11);
                                        expdate.setYear(yyyy);
                                        htempmid.setYear(i - 1900);
                                        h2.put("PROD_ID", lockerType);
                                        h2.put("TODAY_DT", htempmid);
                                        h2.put("CHARGE_TYPE", "RENT_CHARGES");
                                        h2.put("EXP_DT", expdate);
                                        List hlit = sqlMap.executeQueryForList("getServiceDetails", h2);
                                        int diffcurmont = exp.getMonth() + 1;

                                        if (hlit != null && hlit.size() > 0) {
                                            h2 = null;
                                            int calmonth = mont;
                                            for (int m = 0; m < hlit.size(); m++) {
                                                h2 = (HashMap) hlit.get(k);
                                                comm = CommonUtil.convertObjToDouble(h2.get("COMMISION")).doubleValue();
                                                serv = CommonUtil.convertObjToDouble(h2.get("SERVICE_TAX")).doubleValue();
                                                String sdate = CommonUtil.convertObjToStr(h2.get("START_DT"));
                                                String eedate = CommonUtil.convertObjToStr(h2.get("END_DT"));



                                                if (eedate != "") {
                                                    edt = DateUtil.getDateMMDDYYYY(eedate);
                                                    Date comparedt = (Date) currDate.clone();
                                                    comparedt.setDate(edt.getDate());
                                                    comparedt.setMonth(edt.getMonth());
                                                    comparedt.setYear(edt.getYear());

                                                    int eeyear = edt.getYear() + 1900;
                                                    int eemonth = edt.getMonth() + 1;

                                                    Date ssdate = DateUtil.getDateMMDDYYYY(sdate);
                                                    int syear = ssdate.getYear() + 1900;
                                                    int smonth = ssdate.getMonth() + 1;


                                                    int diffmonth = (eeyear - syear) + (eemonth - smonth);

                                                    realmont = calmonth - diffmonth + 1;

                                                    if (eeyear == i) {
                                                        if (calmonth != 0) {
                                                            if (calmonth <= eemonth) {
                                                                realmont = calmonth;
                                                            } else if (calmonth > eemonth) {
                                                                realmont = eemonth;
                                                            }
                                                            double d = comm / 12.0;

                                                            dd1 = dd1 + d * realmont;
                                                            dd = (double) getNearest((long) (dd1 * 100), 100) / 100;
                                                            ser1 = ser1 + ((d * realmont) / 100) * serv;
                                                            ser = (double) getNearest((long) (ser1 * 100), 100) / 100;
                                                            val1 = val1 + dd + ser;
                                                            val = (double) getNearest((long) (val1 * 100), 100) / 100;
                                                            calmonth = calmonth - realmont;
                                                        }
                                                    } else if (eeyear > i) {
                                                        if (calmonth != 0) {
                                                            realmont = calmonth;
                                                            double d = comm / 12.0;
                                                            dd2 = dd1 + d * realmont;
                                                            dd = (double) getNearest((long) (dd2 * 100), 100) / 100;
                                                            ser2 = ser1 + ((d * realmont) / 100) * serv;
                                                            ser = (double) getNearest((long) (ser2 * 100), 100) / 100;
                                                            val2 = val1 + dd + ser;
                                                            val = (double) getNearest((long) (val2 * 100), 100) / 100;
                                                            calmonth = calmonth - realmont;
                                                        }
                                                    }
                                                } else {
                                                    if (calmonth != 0) {

                                                        double d = comm / 12.0;

                                                        dd3 = dd1 + d * calmonth;

                                                        dd = (double) getNearest((long) (dd3 * 100), 100) / 100;
                                                        ser3 = ser3 + ((d * calmonth) / 100) * serv;
                                                        ser = (double) getNearest((long) (ser3 * 100), 100) / 100;
                                                        val3 = val3 + dd + ser;
                                                        val = (double) getNearest((long) (val3 * 100), 100) / 100;
                                                        calmonth = calmonth - realmont;
                                                    }
                                                }
                                            }
                                        }
                                    }

                                } else if (i == yyyy) {
                                    if (mont < mm) {
                                        int q = 0;

                                        HashMap h1 = new HashMap();
                                        Date tempmid = (Date) currDate.clone();
                                        tempmid.setDate(exp.getDate());
                                        tempmid.setMonth(exp.getMonth());
                                        tempmid.setYear(i - 1900);
                                        Date hltempmid = (Date) currDate.clone();
                                        hltempmid.setDate(31);
                                        hltempmid.setMonth(11);
                                        hltempmid.setYear(i - 1900);
                                        h1.put("PROD_ID", lockerType);
                                        h1.put("TODAY_DT", tempmid);
                                        h1.put("EXP_DT", hltempmid);
                                        h1.put("CHARGE_TYPE", "RENT_CHARGES");
                                        List lit = sqlMap.executeQueryForList("getServiceDetails", h1);
                                        int calmonth;

                                        if (lit != null && lit.size() > 0) {
                                            if (k == 1) {
                                                calmonth = 12 - mont;
                                                h1 = null;
                                                for (int m = 0; m < lit.size(); m++) {
                                                    h1 = (HashMap) lit.get(m);
                                                    comm = CommonUtil.convertObjToDouble(h1.get("COMMISION")).doubleValue();
                                                    serv = CommonUtil.convertObjToDouble(h1.get("SERVICE_TAX")).doubleValue();
                                                    String sdate = CommonUtil.convertObjToStr(h1.get("START_DT"));
                                                    String eedate = CommonUtil.convertObjToStr(h1.get("END_DT"));
                                                    Date eedt = DateUtil.getDateMMDDYYYY(eedate);
                                                    Date ssdate = DateUtil.getDateMMDDYYYY(sdate);


                                                    if (eedate != "") {
                                                        edt = DateUtil.getDateMMDDYYYY(eedate);
                                                        Date comparedt = (Date) currDate.clone();
                                                        comparedt.setDate(edt.getDate());
                                                        comparedt.setMonth(edt.getMonth());
                                                        comparedt.setYear(edt.getYear());
                                                        int eeyear = edt.getYear() + 1900;
                                                        int eemonth = edt.getMonth() + 1;


                                                        int syear = ssdate.getYear() + 1900;
                                                        int smonth = ssdate.getMonth() + 1;


                                                        int diffmonth = (eeyear - syear) + (eemonth - smonth);

                                                        realmont = calmonth - diffmonth + 1;

                                                        if (eeyear == i && DateUtil.dateDiff(tempmid, comparedt) >= 0) {
                                                            if (calmonth != 0) {
                                                                if (q == 0) {
                                                                    realmont = eemonth - (exp.getMonth() + 1);
                                                                } else {
                                                                    realmont = (eemonth - smonth) + 1;
                                                                }
                                                                double d = comm / 12.0;

                                                                dd2 = dd2 + (d * realmont);
                                                                dd = (double) getNearest((long) (dd2 * 100), 100) / 100;
                                                                ser2 = ser2 + (((d * realmont) / 100) * serv);
                                                                ser = (double) getNearest((long) (ser2 * 100), 100) / 100;
                                                                val2 = val2 + dd + ser;
                                                                val = (double) getNearest((long) (val2 * 100), 100) / 100;
                                                                calmonth = calmonth - realmont;
                                                                q++;
                                                            }
                                                        } else if (eeyear > i) {
                                                            if (calmonth != 0) {
                                                                realmont = calmonth;
                                                                double d = comm / 12.0;
                                                                dd2 = dd2 + (d * realmont);
                                                                dd = (double) getNearest((long) (dd * 100), 100) / 100;
                                                                ser2 = ser2 + ((d * realmont) / 100) * serv;
                                                                ser = (double) getNearest((long) (ser * 100), 100) / 100;
                                                                val2 = val2 + dd + ser;
                                                                val = (double) getNearest((long) (val * 100), 100) / 100;
                                                                calmonth = calmonth - realmont;
                                                            }

                                                        }
                                                    } else {
                                                        if (calmonth != 0) {
                                                            double d = comm / 12.0;

                                                            dd2 = dd2 + (d * calmonth);

                                                            dd = (double) getNearest((long) (dd * 100), 100) / 100;
                                                            ser2 = ser2 + (((d * calmonth) / 100) * serv);
                                                            ser = (double) getNearest((long) (ser * 100), 100) / 100;
                                                            val2 = val2 + dd + ser;
                                                            val = (double) getNearest((long) (val * 100), 100) / 100;
                                                            calmonth = calmonth - realmont;
                                                        }
                                                    }
                                                }
                                            } else {
                                                calmonth = 12;


                                                h1 = null;
                                                for (int m = 0; m < lit.size(); m++) {
                                                    h1 = (HashMap) lit.get(m);
                                                    comm = CommonUtil.convertObjToDouble(h1.get("COMMISION")).doubleValue();
                                                    serv = CommonUtil.convertObjToDouble(h1.get("SERVICE_TAX")).doubleValue();
                                                    String sdate = CommonUtil.convertObjToStr(h1.get("START_DT"));
                                                    String eedate = CommonUtil.convertObjToStr(h1.get("END_DT"));
                                                    Date eedt = DateUtil.getDateMMDDYYYY(eedate);
                                                    Date ssdate = DateUtil.getDateMMDDYYYY(sdate);


                                                    if (eedate != "") {
                                                        edt = DateUtil.getDateMMDDYYYY(eedate);
                                                        Date comparedt = (Date) currDate.clone();
                                                        comparedt.setDate(edt.getDate());
                                                        comparedt.setMonth(edt.getMonth());
                                                        comparedt.setYear(edt.getYear());
                                                        int eeyear = edt.getYear() + 1900;
                                                        int eemonth = edt.getMonth() + 1;


                                                        int syear = ssdate.getYear() + 1900;
                                                        int smonth = ssdate.getMonth() + 1;


                                                        int diffmonth = (eeyear - syear) + (eemonth - smonth);

                                                        realmont = calmonth - diffmonth + 1;

                                                        if (eeyear == i) {
                                                            if (calmonth != 0) {
                                                                if (calmonth <= eemonth) {
                                                                    realmont = calmonth;
                                                                } else if (calmonth > eemonth) {
                                                                    realmont = eemonth;
                                                                }
                                                                double d = comm / 12.0;

                                                                dd2 = dd2 + (d * realmont);
                                                                dd = (double) getNearest((long) (dd2 * 100), 100) / 100;
                                                                ser2 = ser2 + (((d * realmont) / 100) * serv);
                                                                ser = (double) getNearest((long) (ser2 * 100), 100) / 100;
                                                                val2 = val2 + dd + ser;
                                                                val = (double) getNearest((long) (val2 * 100), 100) / 100;
                                                                calmonth = calmonth - realmont;

                                                            }
                                                        } else if (eeyear > i) {
                                                            if (calmonth != 0) {
                                                                realmont = calmonth;
                                                                double d = comm / 12.0;
                                                                dd2 = dd2 + (d * realmont);
                                                                dd = (double) getNearest((long) (dd * 100), 100) / 100;
                                                                ser2 = ser2 + ((d * realmont) / 100) * serv;
                                                                ser = (double) getNearest((long) (ser * 100), 100) / 100;
                                                                val2 = val2 + dd + ser;
                                                                val = (double) getNearest((long) (val * 100), 100) / 100;
                                                                calmonth = calmonth - realmont;
                                                            }

                                                        }
                                                    } else {
                                                        if (calmonth != 0) {
                                                            double d = comm / 12.0;

                                                            dd2 = dd2 + (d * calmonth);

                                                            dd = (double) getNearest((long) (dd * 100), 100) / 100;
                                                            ser2 = ser2 + (((d * calmonth) / 100) * serv);
                                                            ser = (double) getNearest((long) (ser * 100), 100) / 100;
                                                            val2 = val2 + dd + ser;
                                                            val = (double) getNearest((long) (val * 100), 100) / 100;
                                                            calmonth = calmonth - realmont;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        HashMap h3 = new HashMap();
                                        Date temp = (Date) currDate.clone();
                                        temp.setDate(exp.getDate());
                                        temp.setMonth(exp.getMonth());
                                        temp.setYear((i + 1) - 1900);
                                        Date hlexpdate = (Date) currDate.clone();
                                        hlexpdate.setDate(31);
                                        hlexpdate.setMonth(11);
                                        hlexpdate.setYear((i + 1) - 1900);
                                        h3.put("PROD_ID", lockerType);
                                        h3.put("TODAY_DT", temp);
                                        h3.put("EXP_DT", hlexpdate);
                                        h3.put("CHARGE_TYPE", "RENT_CHARGES");
                                        List hllit = sqlMap.executeQueryForList("getServiceDetails", h3);
                                        if (hllit != null && hllit.size() > 0) {
                                            h3 = null;
                                            calmonth = mont;
                                            for (int m = 0; m < hllit.size(); m++) {
                                                h3 = (HashMap) hllit.get(m);
                                                comm = CommonUtil.convertObjToDouble(h3.get("COMMISION")).doubleValue();
                                                serv = CommonUtil.convertObjToDouble(h3.get("SERVICE_TAX")).doubleValue();
                                                String sdate = CommonUtil.convertObjToStr(h3.get("START_DT"));
                                                String eedate = CommonUtil.convertObjToStr(h3.get("END_DT"));

                                                if (eedate != "") {
                                                    edt = DateUtil.getDateMMDDYYYY(eedate);
                                                    Date comparedt = (Date) currDate.clone();
                                                    comparedt.setDate(edt.getDate());
                                                    comparedt.setMonth(edt.getMonth());
                                                    comparedt.setYear(edt.getYear());
                                                    int eeyear = edt.getYear() + 1900;
                                                    int eemonth = edt.getMonth() + 1;

                                                    Date ssdate = DateUtil.getDateMMDDYYYY(sdate);
                                                    int syear = ssdate.getYear() + 1900;
                                                    int smonth = ssdate.getMonth() + 1;


                                                    int diffmonth = (eeyear - syear) + (eemonth - smonth);

                                                    realmont = calmonth - diffmonth + 1;
                                                    long r = DateUtil.dateDiff(comparedt, hlexpdate);
                                                    System.out.println("#######" + r);
                                                    long l = DateUtil.dateDiff(temp, comparedt);

                                                    if (eeyear == i + 1) {
                                                        if (calmonth != 0) {
                                                            if (calmonth <= eemonth) {
                                                                realmont = calmonth;
                                                            } else if (calmonth > eemonth) {
                                                                realmont = eemonth;
                                                            }
                                                            double d = comm / 12.0;

                                                            dd = dd2 + (d * realmont);
                                                            dd = (double) getNearest((long) (dd * 100), 100) / 100;
                                                            ser = ser2 + (((d * realmont) / 100) * serv);
                                                            ser = (double) getNearest((long) (ser * 100), 100) / 100;
                                                            val = val2 + dd + ser;
                                                            val = (double) getNearest((long) (val * 100), 100) / 100;
                                                            calmonth = calmonth - realmont;
                                                        }
                                                    } else if (eeyear > (i + 1)) {
                                                        if (calmonth != 0) {
                                                            realmont = calmonth;
                                                            double d = comm / 12.0;
                                                            dd = dd2 + (d * realmont);
                                                            dd = (double) getNearest((long) (dd * 100), 100) / 100;
                                                            ser = ser2 + ((d * realmont) / 100) * serv;
                                                            ser = (double) getNearest((long) (ser * 100), 100) / 100;
                                                            val = val2 + dd + ser;
                                                            val = (double) getNearest((long) (val * 100), 100) / 100;
                                                            calmonth = calmonth - realmont;
                                                        }
                                                    }
                                                } else {
                                                    if (calmonth != 0) {

                                                        double d = comm / 12.0;

                                                        dd = dd2 + (d * calmonth);

                                                        dd = (double) getNearest((long) (dd * 100), 100) / 100;
                                                        ser = ser2 + (((d * calmonth) / 100) * serv);
                                                        ser = (double) getNearest((long) (ser * 100), 100) / 100;
                                                        val = val2 + dd + ser;
                                                        val = (double) getNearest((long) (val * 100), 100) / 100;
                                                        calmonth = calmonth - realmont;
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        HashMap h1 = new HashMap();
                                        Date tempmid = (Date) currDate.clone();
                                        tempmid.setDate(exp.getDate());
                                        tempmid.setMonth(exp.getMonth());
                                        tempmid.setYear(i - 1900);
                                        Date hltempmid = (Date) currDate.clone();
                                        hltempmid.setDate(31);
                                        hltempmid.setMonth(11);
                                        hltempmid.setYear(i - 1900);
                                        h1.put("PROD_ID", lockerType);
                                        h1.put("TODAY_DT", tempmid);
                                        h1.put("EXP_DT", hltempmid);
                                        h1.put("CHARGE_TYPE", "RENT_CHARGES");
                                        List lit = sqlMap.executeQueryForList("getServiceDetails", h1);
                                        if (lit != null && lit.size() > 0) {
                                            h1 = null;
                                            int calmonth = mont;
                                            for (int m = 0; m < lit.size(); m++) {
                                                h1 = (HashMap) lit.get(m);
                                                comm = CommonUtil.convertObjToDouble(h1.get("COMMISION")).doubleValue();
                                                serv = CommonUtil.convertObjToDouble(h1.get("SERVICE_TAX")).doubleValue();
                                                String sdate = CommonUtil.convertObjToStr(h1.get("START_DT"));
                                                String eedate = CommonUtil.convertObjToStr(h1.get("END_DT"));
                                                Date eedt = DateUtil.getDateMMDDYYYY(eedate);
                                                Date ssdate = DateUtil.getDateMMDDYYYY(sdate);






                                                if (eedate != "") {
                                                    edt = DateUtil.getDateMMDDYYYY(eedate);
                                                    Date comparedt = (Date) currDate.clone();
                                                    comparedt.setDate(edt.getDate());
                                                    comparedt.setMonth(edt.getMonth());
                                                    comparedt.setYear(edt.getYear());
                                                    int eeyear = edt.getYear() + 1900;
                                                    int eemonth = edt.getMonth() + 1;


                                                    int syear = ssdate.getYear() + 1900;
                                                    int smonth = ssdate.getMonth() + 1;


                                                    int diffmonth = (eeyear - syear) + (eemonth - smonth);

                                                    realmont = calmonth - diffmonth + 1;

                                                    if (eeyear == i) {
                                                        if (calmonth != 0) {
                                                            if (calmonth <= eemonth) {
                                                                realmont = calmonth;
                                                            } else if (calmonth > eemonth) {
                                                                realmont = eemonth;
                                                            }
                                                            double d = comm / 12.0;

                                                            dd1 = dd1 + (d * realmont);
                                                            dd = (double) getNearest((long) (dd1 * 100), 100) / 100;
                                                            ser1 = ser1 + ((d * realmont) / 100) * serv;
                                                            ser = (double) getNearest((long) (ser1 * 100), 100) / 100;
                                                            val1 = val1 + dd + ser;
                                                            val = (double) getNearest((long) (val1 * 100), 100) / 100;
                                                            calmonth = calmonth - realmont;
                                                        }
                                                    } else if (eeyear > i) {
                                                        if (calmonth != 0) {
                                                            realmont = calmonth;
                                                            double d = comm / 12.0;
                                                            dd = dd1 + (d * realmont);
                                                            dd = (double) getNearest((long) (dd * 100), 100) / 100;
                                                            ser = ser1 + ((d * realmont) / 100) * serv;
                                                            ser = (double) getNearest((long) (ser * 100), 100) / 100;
                                                            val = val1 + ser;
                                                            val = (double) getNearest((long) (val * 100), 100) / 100;
                                                            calmonth = calmonth - realmont;
                                                        }
                                                    }
                                                } else {
                                                    if (calmonth != 0) {
                                                        double d = comm / 12.0;

                                                        dd = dd1 + (d * calmonth);

                                                        dd = (double) getNearest((long) (dd * 100), 100) / 100;
                                                        ser = ser1 + ((d * calmonth) / 100) * serv;
                                                        ser = (double) getNearest((long) (ser * 100), 100) / 100;
                                                        val = val1 + ser;
                                                        val = (double) getNearest((long) (val * 100), 100) / 100;
                                                        calmonth = calmonth - realmont;
                                                    }

                                                }
                                            }
                                        }
                                    }


                                } else if (yyy >= i) {
                                    HashMap h = new HashMap();
                                    Date hhdate = (Date) currDate.clone();
                                    hhdate.setDate(31);
                                    hhdate.setMonth(11);
                                    hhdate.setYear(i - 1900);
                                    h.put("PROD_ID", lockerType);
                                    h.put("TODAY_DT", tempexDt);
                                    h.put("CHARGE_TYPE", "RENT_CHARGES");
                                    h.put("EXP_DT", hhdate);
                                    int q = 0;
                                    List list = sqlMap.executeQueryForList("getServiceDetails", h);
                                    if (list != null && list.size() > 0) {
                                        h = null;
                                        int calmonth = 12 - mont;
                                        for (int m = 0; m < list.size(); m++) {
                                            h = (HashMap) list.get(m);
                                            comm = CommonUtil.convertObjToDouble(h.get("COMMISION")).doubleValue();
                                            serv = CommonUtil.convertObjToDouble(h.get("SERVICE_TAX")).doubleValue();
                                            String sdate = CommonUtil.convertObjToStr(h.get("START_DT"));
                                            String eedate = CommonUtil.convertObjToStr(h.get("END_DT"));



                                            if (eedate != "") {
                                                edt = DateUtil.getDateMMDDYYYY(eedate);
                                                Date comparedt = (Date) currDate.clone();
                                                comparedt.setDate(edt.getDate());
                                                comparedt.setMonth(edt.getMonth());
                                                comparedt.setYear(edt.getYear());

                                                int eeyear = edt.getYear() + 1900;
                                                int eemonth = edt.getMonth() + 1;

                                                Date ssdate = DateUtil.getDateMMDDYYYY(sdate);
                                                int syear = ssdate.getYear() + 1900;
                                                int smonth = ssdate.getMonth() + 1;


                                                int diffmonth = (eeyear - syear) + (eemonth - smonth);

                                                realmont = exp.getMonth() + 1;

                                                if (eeyear == i && DateUtil.dateDiff(tempexDt, comparedt) >= 0) {
                                                    if (calmonth != 0) {
                                                        if (q == 0) {
                                                            realmont = eemonth - (exp.getMonth() + 1);
                                                        } else {
                                                            realmont = (eemonth - smonth) + 1;
                                                        }
                                                        double d = comm / 12.0;

                                                        dd1 = dd1 + d * realmont;
                                                        dd = (double) getNearest((long) (dd1 * 100), 100) / 100;
                                                        ser1 = ser1 + ((d * realmont) / 100) * serv;
                                                        ser = (double) getNearest((long) (ser1 * 100), 100) / 100;
                                                        val1 = val1 + dd + ser;
                                                        val = (double) getNearest((long) (val1 * 100), 100) / 100;
                                                        calmonth = calmonth - realmont;
                                                        q++;
                                                    }
                                                } else if (eeyear > i) {
                                                    if (calmonth != 0) {
                                                        realmont = calmonth;
                                                        double d = comm / 12.0;
                                                        dd = dd1 + d * realmont;
                                                        dd = (double) getNearest((long) (dd * 100), 100) / 100;
                                                        ser = ser1 + ((d * realmont) / 100) * serv;
                                                        ser = (double) getNearest((long) (ser * 100), 100) / 100;
                                                        val = val1 + dd + ser;
                                                        val = (double) getNearest((long) (val * 100), 100) / 100;
                                                        calmonth = calmonth - realmont;
                                                    }
                                                }
                                            } else {
                                                if (calmonth != 0) {
                                                    double d = comm / 12.0;

                                                    dd = dd1 + d * calmonth;

                                                    dd = (double) getNearest((long) (dd * 100), 100) / 100;
                                                    ser = ser1 + ((d * calmonth) / 100) * serv;
                                                    ser = (double) getNearest((long) (ser * 100), 100) / 100;
                                                    val = val1 + dd + ser;
                                                    val = (double) getNearest((long) (val * 100), 100) / 100;
                                                    calmonth = calmonth - realmont;
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    HashMap h1 = new HashMap();
                                    Date hhdate = (Date) currDate.clone();
                                    hhdate.setDate(31);
                                    hhdate.setMonth(11);
                                    hhdate.setYear(i - 1900);
                                    Date tempmid = (Date) currDate.clone();
                                    tempmid.setDate(exp.getDate());
                                    tempmid.setMonth(exp.getMonth());
                                    tempmid.setYear(i - 1900);
                                    h1.put("PROD_ID", lockerType);
                                    h1.put("TODAY_DT", tempmid);
                                    h1.put("CHARGE_TYPE", "RENT_CHARGES");
                                    h1.put("EXP_DT", hhdate);
                                    List lit = sqlMap.executeQueryForList("getServiceDetails", h1);
                                    int calmonth = 12;
                                    if (lit != null && lit.size() > 0) {
                                        h1 = null;
                                        for (int m = 0; m < lit.size(); m++) {
                                            h1 = (HashMap) lit.get(m);
                                            comm = CommonUtil.convertObjToDouble(h1.get("COMMISION")).doubleValue();
                                            serv = CommonUtil.convertObjToDouble(h1.get("SERVICE_TAX")).doubleValue();
                                            String sdate = CommonUtil.convertObjToStr(h1.get("START_DT"));
                                            String eedate = CommonUtil.convertObjToStr(h1.get("END_DT"));

                                            if (eedate != "") {
                                                edt = DateUtil.getDateMMDDYYYY(eedate);
                                                Date comparedt = (Date) currDate.clone();
                                                comparedt.setDate(edt.getDate());
                                                comparedt.setMonth(edt.getMonth());
                                                comparedt.setYear(edt.getYear());

                                                int eeyear = edt.getYear() + 1900;
                                                int eemonth = edt.getMonth() + 1;

                                                Date ssdate = DateUtil.getDateMMDDYYYY(sdate);
                                                int syear = ssdate.getYear() + 1900;
                                                int smonth = ssdate.getMonth() + 1;


                                                int diffmonth = (eeyear - syear) + (eemonth - smonth);



                                                if (eeyear == i) {
                                                    if (calmonth != 0) {
                                                        if (calmonth <= eemonth) {
                                                            realmont = calmonth;
                                                        } else if (calmonth > eemonth) {
                                                            realmont = eemonth;
                                                        }
                                                        double d = comm / 12.0;

                                                        dd1 = dd1 + d * realmont;
                                                        dd = (double) getNearest((long) (dd1 * 100), 100) / 100;
                                                        ser1 = ser1 + ((d * realmont) / 100) * serv;
                                                        ser = (double) getNearest((long) (ser1 * 100), 100) / 100;
                                                        val1 = val1 + dd + ser;
                                                        val = (double) getNearest((long) (val1 * 100), 100) / 100;
                                                        calmonth = calmonth - realmont;
                                                    }
                                                } else if (eeyear > i) {
                                                    if (calmonth != 0) {
                                                        realmont = calmonth;
                                                        double d = comm / 12.0;
                                                        dd = dd1 + d * realmont;
                                                        dd = (double) getNearest((long) (dd * 100), 100) / 100;
                                                        ser = ser1 + ((d * realmont) / 100) * serv;
                                                        ser = (double) getNearest((long) (ser * 100), 100) / 100;
                                                        val = val1 + dd + ser;
                                                        val = (double) getNearest((long) (val * 100), 100) / 100;
                                                        calmonth = calmonth - realmont;
                                                    }
                                                }
                                            } else {
                                                if (calmonth != 0) {
                                                    double d = comm / 12.0;

                                                    dd = dd1 + d * calmonth;

                                                    dd = (double) getNearest((long) (dd * 100), 100) / 100;
                                                    ser = ser1 + ((d * calmonth) / 100) * serv;
                                                    ser = (double) getNearest((long) (ser * 100), 100) / 100;
                                                    val = val1 + dd + ser;
                                                    val = (double) getNearest((long) (val * 100), 100) / 100;
                                                    calmonth = calmonth - realmont;
                                                }
                                            }
                                        }
                                    }
                                }

                                charg = charg + dd;
                                service = service + ser;
                                totval = totval + val;
                                k++;
                                dd1 = 0;
                                dd2 = 0;
                                dd3 = 0;
                                dd4 = 0;
                                ser1 = 0;
                                ser2 = 0;
                                ser3 = 0;
                                ser4 = 0;
                                val1 = 0;
                                val2 = 0;
                                val3 = 0;
                                val4 = 0;
                            }

                            ArrayList alist = new ArrayList();
                            alist.add(new Boolean(true));
                            alist.add(locNo);
                            alist.add(name);
                            alist.add(expDt);
                            alist.add(String.valueOf(charg));
                            alist.add(String.valueOf(service));
                            alist.add(penal_Amt);
                            alist.add(prodId);
                            alist.add(prodType);
                            alist.add(bal);
                            alist.add(actNo);
                            alist.add(lockerType);
                            rowList.add(alist);

                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rowList;
    }

    public long getNearest(long number, long roundingFactor) {
        long roundingFactorOdd = roundingFactor;
        if ((roundingFactor % 2) != 0) {
            roundingFactorOdd += 1;
        }
        long mod = number % roundingFactor;
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

    public TaskStatus executeTask() throws Exception {
        /**
         * Formaula to be employed Vault sum(payment) + Cash sum(receipts) -
         * Cash sum(payments) = Vault sum(receipt)
         *
         */
//        LockerRentSIApplicationUI lockerRentUi=new LockerRentSIApplicationUI();
        TaskStatus status = new TaskStatus();
        status.setStatus(BatchConstants.STARTED);
        System.out.println(status.getStatus());
        HashMap dataMap = new HashMap();
        HashMap paramMap = new HashMap();
        paramMap.put("BRANCH_CODE", branch);
        paramMap.put("TODAY_DT", ServerUtil.getCurrentDate(super._branchCode));
        System.out.println("paramMap@@" + paramMap);
//        ArrayList finalList = new ArrayList();
        HashMap tempMap = null;
        HashMap hashData = null;
        List outputList = null;
        List lst = null;
        List lstData = null;
        HashMap data = null;
        List crDrList = null;
        double amt = 0;
        double glcurbal = 0;
        double openingCount = 0;
        System.out.println("paramMap@@" + paramMap);
        Date curdt = ServerUtil.getCurrentDate(super._branchCode);
        int mm = curdt.getMonth() + 1;
        int yyyy = curdt.getYear() + 1900;
        System.out.println("mm is" + mm);
        System.out.println("yyyy is" + yyyy);
//        finalList =getrentCalculation(mm, yyyy);
        getRentCalculationProcess(mm, yyyy);
        lst = sqlMap.executeQueryForList("getLockerOp", paramMap);
        //        System.out.println("@@@@@lst"+lst);

        status.setStatus(BatchConstants.COMPLETED);

        System.out.println("Completion Status : " + status.getStatus());
        return status;
    }

    public static void main(String arg[]) {
        try {
            TaskHeader header = new TaskHeader();
            header.setTaskClass("ExecuteLockerRentSiCheckTask");
            HashMap paramMap = new HashMap();
            header.setProcessType(CommonConstants.DAY_END);
            header.setBranchID(CommonConstants.BRANCH_ID);
            header.setTaskParam(paramMap);
            ExecuteLockerRentSiCheckTask tsk = new ExecuteLockerRentSiCheckTask(header);
            TaskStatus status = tsk.executeTask();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
