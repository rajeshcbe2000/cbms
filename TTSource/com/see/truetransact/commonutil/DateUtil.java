/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DateUtil.java
 *
 * Created on August 21, 2003, 4:18 PM
 */

package com.see.truetransact.commonutil;
import java.util.Date;
import java.util.HashMap;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 *
 * @author  administrator
 */
public class DateUtil {
    private int addDaysEach = 0;
    private int addMonthsEach = 0;
    private static int DAYS = 1;
    private static int MONTHS = 2;
    private static int YEARS = 3;
    
    /** Creates a new instance of DateUtil */
    public DateUtil() {
    }
    
    public static Date getDateMMDDYYYY(String date){
        Date returnDt = null;
        if (date != null && date.length() > 0) {
            try {
                returnDt = LocaleConstants.DATE_FORMAT.parse(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return returnDt;
    }
    
    public static Date getDate(int date, int month, int year) {
        GregorianCalendar calendar = new GregorianCalendar(year, month-1, date);
        return calendar.getTime();
    }
    
    public static String getStringDate(Date date){
        String strDt = "";
        
        if (date != null) {
            strDt = LocaleConstants.DATE_FORMAT.format(date);
        }
        return strDt;
    }
    
    /* if isAddDaysOnly is yes adds only noOfDays given.  
     * Otherwise adds month if noOfDays is 30 or its multiples. 
     * adds year if noOfDays is 365 or its multiples.
     */
    public static Date addDays(Date sourceDate, int noOfDays, boolean isAddDaysOnly) {
        Date returnDate = (Date)sourceDate.clone();
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(sourceDate);
        if (isAddDaysOnly) {
            calendar.add(calendar.DAY_OF_MONTH, noOfDays);
        } else {
            calendar.setTime(addDays(sourceDate, noOfDays));
        }
        returnDate.setDate(calendar.getTime().getDate());
        returnDate.setMonth(calendar.getTime().getMonth());
        returnDate.setYear(calendar.getTime().getYear());
//        returnDate.setHours(calendar.getTime().getHours());
//        returnDate.setMinutes(calendar.getTime().getMinutes());
//        returnDate.setSeconds(calendar.getTime().getSeconds());
        return returnDate;
    }
    
    public static Date nextCalcDate(Date dpDt,Date nxtDt, int freq){
        if(freq!=0){
            nxtDt=addDaysProperFormat(nxtDt, freq);
            Calendar dpnxtCalender=new GregorianCalendar(nxtDt.getYear()+1900,nxtDt.getMonth(),nxtDt.getDate());
            int lstDayofmonth=dpnxtCalender.getActualMaximum(dpnxtCalender.DAY_OF_MONTH);
            int dpDay = dpDt.getDate();
            if(lstDayofmonth>dpDay)
                nxtDt.setDate(dpDay);
            else
                nxtDt.setDate(lstDayofmonth);            
        }
        return nxtDt;
    }
    
    public static Date addDays(Date sourceDate, int noOfDays) {
////////////        GregorianCalendar calendar = new GregorianCalendar();
////////////        calendar.setTime(sourceDate);
////////////        
////////////        if ((noOfDays % 365) == 0) {
////////////            calendar.add(calendar.YEAR, noOfDays/365);
////////////        } else if ((noOfDays % 30) == 0) {
////////////            calendar.add(calendar.MONTH, noOfDays/30);
////////////        } else if ((noOfDays % 7) == 0) {
////////////            calendar.add(calendar.WEEK_OF_YEAR, noOfDays/7);
////////////        } else {
////////////            calendar.add(calendar.DAY_OF_MONTH, noOfDays);
////////////        }
////////////        
////////////        return calendar.getTime();
         Date returnDate = (Date)sourceDate.clone();
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(sourceDate);
        boolean lastDay = false;
        
        if ((noOfDays % 365) == 0) {
            calendar.add(calendar.YEAR, noOfDays/365);
        } else if ((noOfDays % 30) == 0) {
            if (calendar.getActualMaximum(calendar.DAY_OF_MONTH)==sourceDate.getDate() && sourceDate.getDate()!=30) {
                lastDay = true;
            }
            calendar.add(calendar.MONTH, noOfDays/30);
        } else if ((noOfDays % 7) == 0) {
            calendar.add(calendar.WEEK_OF_YEAR, noOfDays/7);
        } else {
            calendar.add(calendar.DAY_OF_MONTH, noOfDays);
        }
        if (lastDay)
            returnDate.setDate(calendar.getActualMaximum(calendar.DAY_OF_MONTH));
        else
            returnDate.setDate(calendar.getTime().getDate());
        returnDate.setMonth(calendar.getTime().getMonth());
        returnDate.setYear(calendar.getTime().getYear());
//        returnDate.setHours(calendar.getTime().getHours());
//        returnDate.setMinutes(calendar.getTime().getMinutes());
//        returnDate.setSeconds(calendar.getTime().getSeconds());
        
        return returnDate;
    }
    
    public static Date addDays(Date sourceDate, Date fromDate, int noOfDays) {
        Date returnDate = (Date)sourceDate.clone();
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(sourceDate);
        boolean lastDay = false;
        
        if ((noOfDays % 365) == 0) {
            calendar.add(calendar.YEAR, noOfDays/365);
        } else if ((noOfDays % 30) == 0) {
            if (calendar.getActualMaximum(calendar.DAY_OF_MONTH)==sourceDate.getDate()) {
                lastDay = true;
            }
            calendar.add(calendar.MONTH, noOfDays/30);
        } else if ((noOfDays % 7) == 0) {
            calendar.add(calendar.WEEK_OF_YEAR, noOfDays/7);
        } else {
            calendar.add(calendar.DAY_OF_MONTH, noOfDays);
        }
        if (lastDay)
            returnDate.setDate(calendar.getActualMaximum(calendar.DAY_OF_MONTH));
        else
            returnDate.setDate(calendar.getTime().getDate());
        returnDate.setMonth(calendar.getTime().getMonth());
        returnDate.setYear(calendar.getTime().getYear());
//        returnDate.setHours(calendar.getTime().getHours());
//        returnDate.setMinutes(calendar.getTime().getMinutes());
//        returnDate.setSeconds(calendar.getTime().getSeconds());
        if (returnDate.getDate()>fromDate.getDate())
            returnDate.setDate(fromDate.getDate());
        
        return returnDate;
    }
    
    public static Date addDaysProperFormat(Date sourceDate, int noOfDays) {
        Date returnDate = (Date)sourceDate.clone();
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(sourceDate);
        
        if ((noOfDays % 365) == 0) {
            calendar.add(calendar.YEAR, noOfDays/365);
        } else if ((noOfDays % 30) == 0) {
            calendar.add(calendar.MONTH, noOfDays/30);
        } else if ((noOfDays % 7) == 0) {
            calendar.add(calendar.WEEK_OF_YEAR, noOfDays/7);
        } else {
            calendar.add(calendar.DAY_OF_MONTH, noOfDays);
        }
        returnDate.setDate(calendar.getTime().getDate());
        returnDate.setMonth(calendar.getTime().getMonth());
        returnDate.setYear(calendar.getTime().getYear());
//        returnDate.setHours(calendar.getTime().getHours());
//        returnDate.setMinutes(calendar.getTime().getMinutes());
//        returnDate.setSeconds(calendar.getTime().getSeconds());
        
        return returnDate;
    }

    public static long dateDiff(Date sourceDate, Date targetDate) {
        GregorianCalendar source = new GregorianCalendar();
        source.setTime(sourceDate);
        
        GregorianCalendar target = new GregorianCalendar();
        target.setTime(targetDate);
        
        long days = (target.getTimeInMillis() - source.getTimeInMillis()) / (24*60*60*1000);
        return days;
    }
    
    public static void main(String args[]) throws Exception{
        
        System.out.println("getDate" + getDate(12, 1, 2004));
        /**
         *     getDateMMDDYYYY accepts wrong date...
         *
         */
        Calendar calender = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("DD/MM/YYYY");
        //System.out.println("getDateMMDDYYYY" + getDateMMDDYYYY("21/01/2001") +"dateFormat    "+dateFormat.format(calender.getTime()));
        //System.out.println("Date dateDiff : " + DateUtil.dateDiff(getDateMMDDYYYY("32/12/2002") , getDateMMDDYYYY("31/12/2002")));
        //        System.out.println("1 :" + DateUtil.getStringDate(DateUtil.getDateMMDDYYYY("8/12/2003")));
        //        System.out.println("2 :" + DateUtil.getStringDate(DateUtil.getDateMMDDYYYY("")));
    }
    
/*    public static Date getDateMMDDYYYY(String date){
        Calendar calendar;
        Date returnDt = null;
        if (date == null || date.length() == 0) {
            //calendar = new GregorianCalendar();
        } else {
            int year = Integer.parseInt(date.substring(date.lastIndexOf("/")+1));
            int month = Integer.parseInt(date.substring(0,date.indexOf("/")));
            int day = Integer.parseInt(date.substring(date.indexOf("/")+1,date.lastIndexOf("/")));
            calendar = new GregorianCalendar(year,month-1,day);
            returnDt = calendar.getTime();
        }
        return returnDt;
    }
 
    public static String getStringDate(Date date){
        StringBuffer strB = new StringBuffer("");
 
        if (date != null) {
            int year = date.getYear() + 1900;
            int month = date.getMonth() + 1;
            int day = date.getDate();
            strB.append(month);
            strB.append("/");
            strB.append(day);
            strB.append("/");
            strB.append(year);
        }
        return strB.toString();
    }
 */
    
    //Only difference between years are calculated. Remaining things will be added
    public static int getDiffYears(String date){
        int givenYear = Integer.parseInt(date.substring(date.lastIndexOf("/")+1));
        Calendar calendar = new GregorianCalendar();
        int currentYear = calendar.get(Calendar.YEAR);
        return currentYear-givenYear;
    }
    public static Date getDateWithoutMinitues(Date date ){
        
        if(date !=null ){
            
//            date.setHours(0);
//            date.setMinutes(0);
//            date.setSeconds(0);
        }
        else
            date=null;
        
        return date;
        
    }
    public static Date getDateWithoutMinitues(String date ){
        Date objdate=null;
        if(date !=null && date.length()>0){
            objdate=getDateMMDDYYYY(date);
//            objdate.setHours(0);
//            objdate.setMinutes(0);
//            objdate.setSeconds(0);
        }
 
        return objdate;
        
    }
    
    /* sourceDate should be a date. noOfDays in int type, addMethod should be 1, 2 or 3
     * 1 is for DAYS, 2 is for MONTHS, 3 is for YEARS. If 1, days will be added, so on...
     * divide should be true or false. 
     */
    public static Date addDays(Date sourceDate, int noOfDays, int addMethod, boolean divide) {
        Date returnDate = (Date)sourceDate.clone();
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(sourceDate);
        boolean lastDay = false;
        if(addMethod == DAYS){
            calendar.add(calendar.DAY_OF_MONTH, noOfDays);            
        }else if (addMethod == MONTHS) {
            if (divide) {
                noOfDays = noOfDays/30;
            }
            calendar.add(calendar.MONTH, noOfDays);            
        }else if ((noOfDays % 365) == 0) {
            if (divide) {
                noOfDays = noOfDays/365;
            }
            calendar.add(calendar.YEAR, noOfDays);
        }
        returnDate.setDate(calendar.getTime().getDate());
        returnDate.setMonth(calendar.getTime().getMonth());
        returnDate.setYear(calendar.getTime().getYear());
//        returnDate.setHours(calendar.getTime().getHours());
//        returnDate.setMinutes(calendar.getTime().getMinutes());
//        returnDate.setSeconds(calendar.getTime().getSeconds());
        
        return returnDate;
    }
}
