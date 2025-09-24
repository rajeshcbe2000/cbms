/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * DateDifference.java
 *
 * Created on March 29, 2004, 5:34 PM
 */

package com.see.truetransact.commonutil.interestcalc;

import com.see.truetransact.commonutil.DateUtil;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author  Pinky
 */

public class DateDifference {
    
    /** Creates a new instance of DateDifference */
    public DateDifference() {
    }
    
    public static double difference(Date d1,Date d2,String monthOption,String yearOption){
        GregorianCalendar cal1 = new GregorianCalendar();
        GregorianCalendar cal2 = new GregorianCalendar();
        cal1.setTime(d1);
        cal2.setTime(d2);
        
        double days=0;
        double days1=0;
        int month =cal1.get(cal1.MONTH);
        int year =cal1.get(cal1.YEAR);
        int year1 =cal2.get(cal2.YEAR);
        int month1 =cal2.get(cal2.MONTH);           
        
        if(year==year1){
            if(month==month1)  {
                if(monthOption.equals("30") || monthOption.equals("30E")){
                    if (cal2.get(cal2.DATE) == 31)
                        cal2.set(cal2.DATE,30);
                }
                days=cal2.get(cal2.DATE)-cal1.get(cal1.DATE);
//                if(days==0)
//                    days=1;
                System.out.println("datedifferece#####"+days);
            }            
            else {
                days = monthPeriod(cal1,monthOption) - cal1.get(cal1.DATE);
                System.out.println("month 1 : " + month);
                System.out.println("days 1 : " + days);
                month+=1;
                
                cal1 = daysCheck(cal1);
                
                cal1.set(cal1.MONTH,month);
                
                while(month!=(month1)){
                    days+=monthPeriod(cal1,monthOption);
                    month=cal1.get(cal1.MONTH);
                    month+=1;
                    cal1 = daysCheck(cal1);
                    cal1.set(cal1.MONTH,month);
                    System.out.println("month 2 : " + month);
                    System.out.println("days 2 : " + days);
                }
                if(monthOption.equals("30") || monthOption.equals("30E")){
                    if(cal2.get(cal2.DATE)==31)
                        cal2.set(cal2.DATE,30);
                }
                days+=cal2.get(cal2.DATE);
                System.out.println("month 3 : " + month);
                System.out.println("days 3 : " + days);
            }
        }else {
            days = monthPeriod(cal1,monthOption) - cal1.get(cal1.DATE);                 
            System.out.println("days:" + days);
            month+=1;
            cal1 = daysCheck(cal1);
            cal1.set(cal1.MONTH,month);
            while( year != year1) {
                while(month<12){
                    days+=monthPeriod(cal1,monthOption);
                    System.out.println("days:" + days);
                    month=cal1.get(cal1.MONTH);
                    month+=1;
                    System.out.println("month : " + month);
                    cal1 = daysCheck(cal1);
                    cal1.set(cal1.MONTH,month);
                }
                if(yearOption.equals("Actual")) {
                    if(cal1.isLeapYear(year))                        
                        days=days/365;
                    else
                        days=days/365;
                    days1+=days;
                    days=0;
                }
                year +=1;
                month=0;
                cal1.set(cal1.YEAR,year);
            }
            month=0;
            cal1.set(cal1.MONTH,month);
            while(month!=(month1)) {
                days+=monthPeriod(cal1,monthOption);
                month=cal1.get(cal1.MONTH);
                month+=1;
                System.out.println("month1 : " + month);
                cal1 = daysCheck(cal1);
                cal1.set(cal1.MONTH,month);
                
            }
            if(monthOption.equals("30") || monthOption.equals("30E")){
                    if(cal2.get(cal2.DATE)==31)
                        cal2.set(cal2.DATE,30);
            }
            days+=cal2.get(cal2.DATE);            
        }        
        if(yearOption.equals("Actual")) {
            if(cal1.isLeapYear(year))                        
                   days=days/365;// days=days/366;
           else
                  days=days/365;
            days1+=days;
            days=days1;
        }        
        else if(yearOption.equals("365"))
            days=days/366;
        else if(yearOption.equals("360"))
            days=days/360;
        return days;
    }
    
    private static int monthPeriod(GregorianCalendar cal,String monthOption){
        if (monthOption.equals("ActualMonth"))
        return cal.getActualMaximum(cal.DATE);    
        if ((cal.get(cal.DATE) == 31)) 
            return 31;
            return 30;
    }
    
    private static GregorianCalendar daysCheck(GregorianCalendar cal1) {
//        if ((cal1.get(cal1.MONTH)== cal1.FEBRUARY && cal1.get(cal1.DATE)==28)){
////            if(cal1.isLeapYear(cal1.YEAR))
////                cal1.set(cal1.DATE, 29);
////            else 
//                cal1.set(cal1.DATE, 28);
//        } else if ((cal1.get(cal1.MONTH)==cal1.MARCH || 
//                    cal1.get(cal1.MONTH)==cal1.MAY || 
//                    cal1.get(cal1.MONTH)==cal1.JULY || 
//                    cal1.get(cal1.MONTH)==cal1.AUGUST || 
//                    cal1.get(cal1.MONTH)==cal1.OCTOBER || 
//                    cal1.get(cal1.MONTH)==cal1.DECEMBER ) && 
//                    cal1.get(cal1.DATE) == 31) {
//            cal1.set(cal1.DATE, 30);
//        }
        cal1.set(cal1.DATE, 1);
        return cal1;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {       
        Date d1 =DateUtil.getDateMMDDYYYY("31/03/2004");
        Date d2 =DateUtil.getDateMMDDYYYY("31/03/2005");
        System.out.println("d1 :" + d1 + " d2 : " + d2);
//        System.out.println(difference(d1,d2,"ActualMonth","Actual"));
        System.out.println(difference(d1,d2,"30","360"));
//        System.out.println(difference(d1,d2,"30E","360")*360);
    }
}
