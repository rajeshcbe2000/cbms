/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * Rounding.java
 *
 * Created on March 26, 2004, 2:12 PM
 */

package com.see.truetransact.commonutil.interestcalc;

/**
 *
 * @author  Pinky
 */

import java.text.DecimalFormat;

public class Rounding {
    
    /** Creates a new instance of Rounding */
    public Rounding() {
    }
    
    public long getNearest(long number,long roundingFactor)  {
        long roundingFactorOdd = roundingFactor;
        if ((roundingFactor%2) != 0)
            roundingFactorOdd +=1;
        long mod = number%roundingFactor;
        if ((mod < (roundingFactor/2)) || (mod < (roundingFactorOdd/2)))
            return lower(number,roundingFactor);
        else
            return higher(number,roundingFactor);
    }
    
    // The following methods added for Polpully Bank
    public double getNearest(double number,long roundingFactor)  {
        long roundingFactorOdd = roundingFactor;
        if ((roundingFactor%2) != 0)
            roundingFactorOdd +=1;
        double mod = number%roundingFactor;
        if (mod < (roundingFactor/2))
            return lower(number,roundingFactor);
        else
            return higher(number,roundingFactor);
    }
    
    public double lower(double number,long roundingFactor) {
        double mod = number%roundingFactor;
        return number-mod;
    }
    public double higher(double number,long roundingFactor) {
        double mod = number%roundingFactor;
        if ( mod == 0)
            return number;
        return (number-mod) + roundingFactor ;
    }    
    // End for Polpully Bank
    
    public double getNearestHigher(double number,long roundingFactor)  {
        System.out.println("number"+number+"rounding factor"+roundingFactor);
        double mod = number%roundingFactor;
        if ( mod == 0)
            return number;
        return (number-mod) + roundingFactor ;
    }
    public long lower(long number,long roundingFactor) {
        long mod = number%roundingFactor;
        return number-mod;
    }
    public long higher(long number,long roundingFactor) {
        long mod = number%roundingFactor;
        if ( mod == 0)
            return number;
        return (number-mod) + roundingFactor ;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Rounding rd = new Rounding();
            double test = 1234.58788;
            DecimalFormat d = new DecimalFormat();
            d.setMaximumFractionDigits(2);
            d.setDecimalSeparatorAlwaysShown(true);
            double dbl = d.parse(d.format(test)).doubleValue();
            System.out.println(dbl);
            String str = d.parse(d.format(test)).toString();
            int i = str.indexOf(".");
            String str1 = str.substring(0,i);
            String str2 = str.substring(i+1);
            System.out.println(str1 + "     " +str2);
            long number = new Long(str1).longValue();
            long fraction = new Long(str2).longValue();
            System.out.println(rd.getNearest(fraction,1));
            System.out.println(rd.lower(fraction,5));
            System.out.println(rd.higher(fraction,1));
            
        }catch (Exception e) {
            e.printStackTrace();
        }
    }    
}
