/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * CommonMethod.java
 *
 * Created on April 12, 2012, 2:27 PM
 */

package com.see.truetransact.ui.roomrent;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.StringTokenizer;
import java.util.regex.*;
/**
 *
 * @author  user
 */
public class CommonMethod {
    
    /** Creates a new instance of CommonMethod */
    public CommonMethod() {
    }
   public static String  getCurrentDate()
   {
        Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat formatter= new SimpleDateFormat("dd/MM/yyyy");
        String dateNow = formatter.format(currentDate.getTime());
        return dateNow;
   }
   public static boolean getSplCharsCheck(String var)
   {
       //String patt="!@#$%^&*()+=-[]\\\';,./{}|\":<>?~";
        Pattern p = Pattern.compile("[^a-z0-9,/]", Pattern.CASE_INSENSITIVE);//
        Matcher m = p.matcher(var);
        boolean b = m.find();
        return b;
   }
      public static boolean getSplCharsWithoutCheck(String var)
   {
       //String patt="!@#$%^&*()+=-[]\\\';,./{}|\":<>?~";
        Pattern p = Pattern.compile("[^a-z0-9,/,(,),-, ]", Pattern.CASE_INSENSITIVE);//
        Matcher m = p.matcher(var);
        boolean b = m.find();
        return b;
   }
   public static String getAddMonth(String day)
   {
         //create Calendar instance
    Calendar now = Calendar.getInstance();
   
    System.out.println("Current date : " + (now.get(Calendar.MONTH) + 1)
                        + "-"
                        + now.get(Calendar.DATE)
                        + "-"
                        + now.get(Calendar.YEAR));
   
    //add months to current date using Calendar.add method
    now.add(Calendar.MONTH,1);
 
    System.out.println("date after 1 months : " + (now.get(Calendar.MONTH) + 1)
                        + "-"
                        + now.get(Calendar.DATE)
                        + "-"
                        + now.get(Calendar.YEAR));
 
   
    //substract months from current date using Calendar.add method
   // now = Calendar.getInstance();
   // now.add(Calendar.MONTH, -5);
 
    //System.out.println("date before 5 months : " + (now.get(Calendar.MONTH) + 1)
    //                    + "-"
        //                + now.get(Calendar.DATE)
        //                + "-"
       //                 + now.get(Calendar.YEAR));
   
    return day+"/"+(now.get(Calendar.MONTH) + 1)+"/"+now.get(Calendar.YEAR);
   }
   
   
   public static boolean ValidEmailAddress(String emailAddress)
  {
      try
      {
        String email=emailAddress;
        Pattern p=Pattern.compile("[a-zA-Z]*[0-9]*@[a-zA-Z]*.[a-zA-Z]*");
        Matcher m=p.matcher(email);
        boolean b=m.matches();
        if(b==true)
        {
           return true;
        }
        else
        {
          return false;
        } 
      }
      catch(Exception e)
      {
          e.printStackTrace();
      }
        return false;
  }
}
