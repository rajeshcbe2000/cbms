/*
 * PreInterestCalculator.java
 *
 * Created on January 13, 2005, 12:25 PM
 */
package com.see.truetransact.ui.common.servicetax;

import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.interestcalc.Rounding;
import java.text.DecimalFormat;
import java.util.*;

public class ServiceTaxCalculation {

    public static final String TOT_AMOUNT = "TOT_AMOUNT";
    public static final String CURR_DT = "CURR_DT";
    public static final String SERVICE_TAX = "SERVICE_TAX";
    public static final String EDUCATION_CESS = "EDUCATION_CESS";
    public static final String HIGHER_EDU_CESS = "HIGHER_EDU_CESS"; 
    public static final String SWACHH_CESS = "SWACHH_CESS";
    public static final String KRISHIKALYAN_CESS = "KRISHI_KALYAN_CESS";
    public static final String TOT_TAX_AMT = "TOT_TAX_AMT";
    public static final String TAX_HEAD_ID = "TAX_HEAD_ID";
    public static final String SWACHH_HEAD_ID = "SWACHH_HEAD_ID";
    public static final String KRISHIKALYAN_HEAD_ID = "KRISHIKALYAN_HEAD_ID";
    Date curDate = null;
    private final String NEAREST = "NEAREST_VALUE";
    private final String HIGHER = "HIGHER_VALUE";
    private final String LOWER = "LOWER_VALUE";
    private final String NO_ROUND_OFF = "NO_ROUND_OFF";
    DecimalFormat df = new DecimalFormat("##.00");

    //__ To get the Rate of Interest...
    public HashMap getServiceTaxRates(HashMap inputMap) throws Exception {
        List lst = (List) ClientUtil.executeQuery("getServiceTaxDetails", inputMap);
        HashMap dataMap = null;
        if (lst != null && lst.size() > 0) {
            dataMap = (HashMap) lst.get(0);
        }
        return dataMap;
    }

    //__ Method to Insert the record into the Table...
    private String roundOff(HashMap resultMap) {
        /**
         * Check for the Rounding Off Criteria
         */
        String roundOffVal = null;
//        if (CommonUtil.convertObjToStr(resultMap.get("ROUNF_OFF")).equalsIgnoreCase("HIGHER_VALUE")) {
//            roundOffVal = InterestCalculationConstants.ROUNDING_HIGHER;
//        } else if (CommonUtil.convertObjToStr(resultMap.get("ROUNF_OFF")).equalsIgnoreCase("LOWER_VALUE")) {
//            roundOffVal = InterestCalculationConstants.ROUNDING_LOWER;
//        } else if (CommonUtil.convertObjToStr(resultMap.get("ROUNF_OFF")).equalsIgnoreCase("NEAREST_VALUE")) {
//            roundOffVal = InterestCalculationConstants.ROUNDING_NEAREST;
//        }

        return roundOffVal;
    }

    public HashMap calculateServiceTax_1(HashMap resultMap) throws Exception {
        System.out.println("Resultmap in setRateOfInterest : " + resultMap);
        HashMap retMap = new HashMap();
        HashMap map = new HashMap();
        map.put(ServiceTaxCalculation.CURR_DT, resultMap.get(ServiceTaxCalculation.CURR_DT));
        map.put("SERVICE_TAX_ID",resultMap.get("SERVICE_TAX_ID"));
        final HashMap dataMap = getServiceTaxRates(map);
        double tot_amt = CommonUtil.convertObjToDouble(resultMap.get(ServiceTaxCalculation.TOT_AMOUNT));
         if (tot_amt > 0 && dataMap != null) { 
            //SWACHH_HEAD_ID,KRISHI_HEAD_ID,SWACHH_CESS,KRISHI_KALYAN_CESS
            double ser_Rate = 0, edCess_rate = 0, higer_rate = 0,swachh_rate=0,krishikalyan_rate=0;
            if (dataMap.containsKey("SERVICE_TAX")) {
                ser_Rate = CommonUtil.convertObjToDouble(dataMap.get("SERVICE_TAX"));
            }
            if (dataMap.containsKey("EDUCATION_CESS")) {
                edCess_rate = CommonUtil.convertObjToDouble(dataMap.get("EDUCATION_CESS"));
            }
            if (dataMap.containsKey("HIGHER_EDU_CESS")) {
                higer_rate = CommonUtil.convertObjToDouble(dataMap.get("HIGHER_EDU_CESS"));
            }
             if (dataMap.containsKey("SWACHH_CESS")) {
               swachh_rate= CommonUtil.convertObjToDouble(dataMap.get("SWACHH_CESS"));
            }
            if (dataMap.containsKey("KRISHI_KALYAN_CESS")) {
               krishikalyan_rate = CommonUtil.convertObjToDouble(dataMap.get("KRISHI_KALYAN_CESS"));
            }
            double serviceTaxAmt = (tot_amt * (ser_Rate / 100));
            double ed_cess_Amt = (serviceTaxAmt * (edCess_rate / 100));
            double higher_cess_Amt = (serviceTaxAmt * (higer_rate / 100));
            double swachhCess = (tot_amt * (swachh_rate / 100));
            double krishiKalyanCess = (tot_amt * (krishikalyan_rate / 100));
            //Ref. Mail from mr.ajith swachhCess / krishkalyan round off needed only add both tax amount 
            //swachhCess=roundOffAmtForRoundVal(swachhCess);
            //swachhCess=CommonUtil.convertObjToDouble(roundOffAmt(CommonUtil.convertObjToStr(swachhCess),HIGHER)).doubleValue();   
            swachhCess=CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(swachhCess)).doubleValue();   
            //krishiKalyanCess=roundOffAmtForRoundVal(krishiKalyanCess);
            //krishiKalyanCess=CommonUtil.convertObjToDouble(roundOffAmt(CommonUtil.convertObjToStr(krishiKalyanCess),HIGHER)).doubleValue();            
            krishiKalyanCess=CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(krishiKalyanCess)).doubleValue();                        
            double tottax_amt =  serviceTaxAmt + ed_cess_Amt + higher_cess_Amt+swachhCess+krishiKalyanCess;
             if (resultMap.containsKey("TEXT_BOX")) {//changed by Kannan AR On 18-Sep-2017
                 Rounding rod = new Rounding();
                 tottax_amt = (double) rod.getNearest((long) (tottax_amt * 100), 100) / 100;
             }              
            retMap.put(ServiceTaxCalculation.TAX_HEAD_ID, dataMap.get("TAX_HEAD_ID"));
            retMap.put(ServiceTaxCalculation.SERVICE_TAX, serviceTaxAmt);
            retMap.put(ServiceTaxCalculation.EDUCATION_CESS, ed_cess_Amt);
            //getRoundOffAmount query added by Kannan AR bcz ajith told final amount should be round off
            //Ex: Swach cess: 152.55 = 152.50
             HashMap taxMap = new HashMap();
             if (swachhCess > 0.0) {
                 taxMap.put("AMOUNT", swachhCess);
                 List lst = ClientUtil.executeQuery("getRoundOffAmount", taxMap);
                 if (lst != null && lst.size() > 0) {
                     taxMap = (HashMap) lst.get(0);
                     swachhCess = CommonUtil.convertObjToDouble(taxMap.get("ROUND_OFF_AMOUNT"));
                 }
             }
             if (krishiKalyanCess > 0.0) {
                 taxMap = new HashMap();
                 taxMap.put("AMOUNT", krishiKalyanCess);
                 List lst = ClientUtil.executeQuery("getRoundOffAmount", taxMap);
                 if (lst != null && lst.size() > 0) {
                     taxMap = (HashMap) lst.get(0);
                     krishiKalyanCess = CommonUtil.convertObjToDouble(taxMap.get("ROUND_OFF_AMOUNT"));
                 }
             }
            retMap.put(ServiceTaxCalculation.SWACHH_CESS, swachhCess);
            retMap.put(ServiceTaxCalculation.KRISHIKALYAN_CESS, krishiKalyanCess);
            retMap.put(ServiceTaxCalculation.SWACHH_HEAD_ID, dataMap.get("SWACHH_HEAD_ID"));
            retMap.put(ServiceTaxCalculation.KRISHIKALYAN_HEAD_ID, dataMap.get("KRISHI_HEAD_ID"));
            retMap.put(ServiceTaxCalculation.HIGHER_EDU_CESS, higher_cess_Amt);
            retMap.put(ServiceTaxCalculation.TOT_TAX_AMT,tottax_amt);
            }
        return retMap;
    }
    
    public HashMap calculateServiceTax(HashMap resultMap) throws Exception {
        System.out.println("Resultmap in setRateOfInterest : " + resultMap);
        double serviceTaxAmt = 0;
        double ed_cess_Amt = 0;
        double higher_cess_Amt = 0;
        double swachhCess = 0;
        double krishiKalyanCess = 0;
        HashMap retMap = new HashMap();
        //String cessRoundOff = "NO_ROUND_OFF";
        String cessRoundOff = ""; // Added by nithya on 09-09-2020 for KD-2230
        String gstRoundOff = "";
        if(resultMap != null && resultMap.containsKey("SERVICE_TAX_DATA") && resultMap.get("SERVICE_TAX_DATA") != null){
            List taxSettingsList = (List) resultMap.get("SERVICE_TAX_DATA");
            if (taxSettingsList != null && taxSettingsList.size() > 0) {
                for (int i = 0; i < taxSettingsList.size(); i++) {
                    HashMap serviceTaxDataMap = (HashMap) taxSettingsList.get(i);
                    HashMap map = new HashMap();
                    map.put(ServiceTaxCalculation.CURR_DT, resultMap.get(ServiceTaxCalculation.CURR_DT));
                    map.put("SERVICE_TAX_ID", serviceTaxDataMap.get("SETTINGS_ID"));
                    final HashMap dataMap = getServiceTaxRates(map);
                    double tot_amt = CommonUtil.convertObjToDouble(serviceTaxDataMap.get(ServiceTaxCalculation.TOT_AMOUNT));
                    if (tot_amt > 0 && dataMap != null) {
                        //SWACHH_HEAD_ID,KRISHI_HEAD_ID,SWACHH_CESS,KRISHI_KALYAN_CESS
                        double ser_Rate = 0, edCess_rate = 0, higer_rate = 0, swachh_rate = 0, krishikalyan_rate = 0;
                        if (dataMap.containsKey("SERVICE_TAX")) {
                            ser_Rate = CommonUtil.convertObjToDouble(dataMap.get("SERVICE_TAX"));
                        }
                        if (dataMap.containsKey("EDUCATION_CESS")) {
                            edCess_rate = CommonUtil.convertObjToDouble(dataMap.get("EDUCATION_CESS"));
                        }
                        if (dataMap.containsKey("HIGHER_EDU_CESS")) {
                            higer_rate = CommonUtil.convertObjToDouble(dataMap.get("HIGHER_EDU_CESS"));
                        }
                        if (dataMap.containsKey("SWACHH_CESS")) {
                            swachh_rate = CommonUtil.convertObjToDouble(dataMap.get("SWACHH_CESS"));
                        }
                        if (dataMap.containsKey("KRISHI_KALYAN_CESS")) {
                            krishikalyan_rate = CommonUtil.convertObjToDouble(dataMap.get("KRISHI_KALYAN_CESS"));
                        }
                        serviceTaxAmt = serviceTaxAmt + (tot_amt * (ser_Rate / 100));
                        ed_cess_Amt = ed_cess_Amt + (serviceTaxAmt * (edCess_rate / 100));
                        higher_cess_Amt = higher_cess_Amt + (serviceTaxAmt * (higer_rate / 100));
                        swachhCess = swachhCess + (tot_amt * (swachh_rate / 100));
                        krishiKalyanCess = krishiKalyanCess + (tot_amt * (krishikalyan_rate / 100));
                        swachhCess = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(swachhCess)).doubleValue();
                        krishiKalyanCess = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(krishiKalyanCess)).doubleValue();
                        retMap.put(ServiceTaxCalculation.TAX_HEAD_ID, dataMap.get("TAX_HEAD_ID"));
                        retMap.put(ServiceTaxCalculation.SWACHH_HEAD_ID, dataMap.get("SWACHH_HEAD_ID"));
                        retMap.put(ServiceTaxCalculation.KRISHIKALYAN_HEAD_ID, dataMap.get("KRISHI_HEAD_ID"));
                        //cessRoundOff = CommonUtil.convertObjToStr(dataMap.get("CESS_ROUNDOFF")); // Added by nithya on 23-04-2020 for KD-1837                        
                    }
                }
            }
        }
        HashMap cbmsMap = new HashMap();
        List list;
        cbmsMap.put("CBMS_KEY", "GST_ROUNDOFF");
        list = ClientUtil.executeQuery("getSelectCbmsParameterValues", cbmsMap);
        if (list != null && list.size() > 0) {
            cbmsMap = (HashMap) list.get(0);
            gstRoundOff = CommonUtil.convertObjToStr(cbmsMap.get("CBMS_VALUE"));
        }
        System.out.println("gst round off value :: " + gstRoundOff);
        cbmsMap.put("CBMS_KEY", "CESS_ROUNDOFF");
        list = ClientUtil.executeQuery("getSelectCbmsParameterValues", cbmsMap);
        if (list != null && list.size() > 0) {
            cbmsMap = (HashMap) list.get(0);
            cessRoundOff = CommonUtil.convertObjToStr(cbmsMap.get("CBMS_VALUE"));
        }
        System.out.println("cess round off value :: " + cessRoundOff);
        HashMap taxMap = new HashMap();
        if (swachhCess > 0.0) {            
            if (gstRoundOff.equalsIgnoreCase("HIGHER_VALUE")) { // Added by nithya on 09-09-2020 for KD-2230
                swachhCess = Math.ceil(CommonUtil.convertObjToDouble(swachhCess));
            } else if (gstRoundOff.equalsIgnoreCase("LOWER_VALUE")) {
                swachhCess = Math.floor(CommonUtil.convertObjToDouble(swachhCess));
            } else if (gstRoundOff.equalsIgnoreCase("NEAREST_VALUE")) {
                swachhCess = Math.round(CommonUtil.convertObjToDouble(swachhCess));
            } else if (gstRoundOff.equalsIgnoreCase("NO_ROUND_OFF")) {
                swachhCess = CommonUtil.convertObjToDouble(swachhCess);
            } else {
                taxMap.put("AMOUNT", swachhCess);
                List lst = ClientUtil.executeQuery("getRoundOffAmount", taxMap);
                if (lst != null && lst.size() > 0) {
                    taxMap = (HashMap) lst.get(0);
                    swachhCess = CommonUtil.convertObjToDouble(taxMap.get("ROUND_OFF_AMOUNT"));
                    //swachhCess = Math.round(CommonUtil.convertObjToDouble(swachhCess));
                }
            }
        }
        if (krishiKalyanCess > 0.0) {// Added by nithya on 09-09-2020 for KD-2230
            if (gstRoundOff.equalsIgnoreCase("HIGHER_VALUE")) {
                krishiKalyanCess = Math.ceil(CommonUtil.convertObjToDouble(krishiKalyanCess));
            } else if (gstRoundOff.equalsIgnoreCase("LOWER_VALUE")) {
                krishiKalyanCess = Math.floor(CommonUtil.convertObjToDouble(krishiKalyanCess));
            } else if (gstRoundOff.equalsIgnoreCase("NEAREST_VALUE")) {
                krishiKalyanCess = Math.round(CommonUtil.convertObjToDouble(krishiKalyanCess));
            } else if (gstRoundOff.equalsIgnoreCase("NO_ROUND_OFF")) {
                krishiKalyanCess = CommonUtil.convertObjToDouble(krishiKalyanCess);
            } else {
                taxMap = new HashMap();
                taxMap.put("AMOUNT", krishiKalyanCess);
                List lst = ClientUtil.executeQuery("getRoundOffAmount", taxMap);
                if (lst != null && lst.size() > 0) {
                    taxMap = (HashMap) lst.get(0);
                    krishiKalyanCess = CommonUtil.convertObjToDouble(taxMap.get("ROUND_OFF_AMOUNT"));
                    //krishiKalyanCess = Math.round(CommonUtil.convertObjToDouble(krishiKalyanCess));
                }
            }
        }
        // Commented for the purpose of solving rounding issue - in case only service tax required
//        if (serviceTaxAmt > 0.0) {
//            taxMap = new HashMap();
//            taxMap.put("AMOUNT", serviceTaxAmt);
//            List lst = ClientUtil.executeQuery("getRoundOffAmount", taxMap);
//            if (lst != null && lst.size() > 0) {
//                taxMap = (HashMap) lst.get(0);
//                serviceTaxAmt = CommonUtil.convertObjToDouble(taxMap.get("ROUND_OFF_AMOUNT"));
//            }
//        }
        if (ed_cess_Amt > 0.0) {
            taxMap = new HashMap();
            taxMap.put("AMOUNT", ed_cess_Amt);
            List lst = ClientUtil.executeQuery("getRoundOffAmount", taxMap);
            if (lst != null && lst.size() > 0) {
                taxMap = (HashMap) lst.get(0);
                ed_cess_Amt = CommonUtil.convertObjToDouble(taxMap.get("ROUND_OFF_AMOUNT"));
            }
        }
        if (higher_cess_Amt > 0.0) {
            taxMap = new HashMap();
            taxMap.put("AMOUNT", higher_cess_Amt);
            List lst = ClientUtil.executeQuery("getRoundOffAmount", taxMap);
            if (lst != null && lst.size() > 0) {
                taxMap = (HashMap) lst.get(0);
                higher_cess_Amt = CommonUtil.convertObjToDouble(taxMap.get("ROUND_OFF_AMOUNT"));
            }
        }
        // serviceTaxAmt,swachhCess,krishiKalyanCess values will be greater than zero if IGST included
        if(serviceTaxAmt > 0 && swachhCess > 0 && krishiKalyanCess > 0){// In case if IGST required along with CGST & SGST in future - Rounding required
            if (serviceTaxAmt > 0.0) {
                // Added by nithya on 23-04-2020 for KD-1837
                System.out.println("serviceTaxAmt :: " + serviceTaxAmt);
                if(cessRoundOff.equalsIgnoreCase("HIGHER_VALUE")){
                    serviceTaxAmt = Math.ceil(CommonUtil.convertObjToDouble(serviceTaxAmt));                    
                }else if(cessRoundOff.equalsIgnoreCase("LOWER_VALUE")){
                    serviceTaxAmt = Math.floor(CommonUtil.convertObjToDouble(serviceTaxAmt));                    
                }else if(cessRoundOff.equalsIgnoreCase("NEAREST_VALUE")){
                    serviceTaxAmt = Math.round(CommonUtil.convertObjToDouble(serviceTaxAmt));                    
                }else if(cessRoundOff.equalsIgnoreCase("NO_ROUND_OFF")){ // Added by nithya on 09-09-2020 for KD-2230
                    serviceTaxAmt = CommonUtil.convertObjToDouble(serviceTaxAmt);                    
                }else{                
                taxMap = new HashMap();
                taxMap.put("AMOUNT", serviceTaxAmt);
                List lst = ClientUtil.executeQuery("getRoundOffAmount", taxMap);
                if (lst != null && lst.size() > 0) {
                    taxMap = (HashMap) lst.get(0);
                    serviceTaxAmt = CommonUtil.convertObjToDouble(taxMap.get("ROUND_OFF_AMOUNT"));
                    System.out.println("#$#$#$#$#$# service Tax amount 1 :: "+serviceTaxAmt);
                    serviceTaxAmt = Math.round(CommonUtil.convertObjToDouble(serviceTaxAmt));
                    System.out.println("#$#$#$#$#$# service Tax amount 1 after :: "+serviceTaxAmt);
                }
               }
            }
        }
        
        double tottax_amt = serviceTaxAmt + ed_cess_Amt + higher_cess_Amt + swachhCess + krishiKalyanCess;
        System.out.println("@#@#@#@#@# service tax amt : "+serviceTaxAmt);
        System.out.println("@#@#@#@#@# ed_cess_Amt : "+ed_cess_Amt);
        System.out.println("@#@#@#@#@# higher_cess_Amt : "+higher_cess_Amt);
        System.out.println("@#@#@#@#@# swachhCess : "+swachhCess);
        System.out.println("@#@#@#@#@# krishiKalyanCess : "+krishiKalyanCess);
        if (resultMap.containsKey("TEXT_BOX")) {
            Rounding rod = new Rounding();
            tottax_amt = (double) rod.getNearest((long) (tottax_amt * 100), 100) / 100;
        }  
        if(serviceTaxAmt > 0 && swachhCess == 0 && krishiKalyanCess == 0){
            System.out.println("tottax_amt :: "+ tottax_amt+"serviceTaxAmt :: "+ serviceTaxAmt);
            //serviceTaxAmt = CommonUtil.convertObjToDouble(roundOffAmt(CommonUtil.convertObjToStr(serviceTaxAmt),NEAREST)).doubleValue();
            //tottax_amt = CommonUtil.convertObjToDouble(roundOffAmt(CommonUtil.convertObjToStr(tottax_amt),NEAREST)).doubleValue();
            serviceTaxAmt = Math.round(serviceTaxAmt);// solving Rounding Mismatch issue
            tottax_amt = Math.round(tottax_amt);// solving Rounding Mismatch issue
        }
        retMap.put(ServiceTaxCalculation.SERVICE_TAX, serviceTaxAmt);
        retMap.put(ServiceTaxCalculation.EDUCATION_CESS, ed_cess_Amt);
        retMap.put(ServiceTaxCalculation.SWACHH_CESS, swachhCess);
        retMap.put(ServiceTaxCalculation.KRISHIKALYAN_CESS, krishiKalyanCess);       
        retMap.put(ServiceTaxCalculation.HIGHER_EDU_CESS, higher_cess_Amt);
        retMap.put(ServiceTaxCalculation.TOT_TAX_AMT, tottax_amt);
        System.out.println("@#@#@#@#@# final map : "+retMap);
        return retMap;
    }

    public double roundOffAmtForRoundVal(double roudVal) throws Exception {
       DecimalFormat newFormat = new DecimalFormat("#.##");
            double twoDecimal = CommonUtil.convertObjToDouble(newFormat.format(roudVal));
        return twoDecimal;
    }

    public String roundOffAmt(String amtStr, String method) throws Exception {
       String amt = amtStr;
        DecimalFormat d = new DecimalFormat();
        d.setMaximumFractionDigits(0);
        d.setDecimalSeparatorAlwaysShown(true);
        if (amtStr != null && !amtStr.equals("")) {
            amtStr = d.parse(d.format(new Double(amtStr).doubleValue())).toString();
        }
        Rounding rd = new Rounding();
        int pos = amtStr.indexOf(".");
        long intPart = 0;
        long decPart = 0;
        if (pos >= 0) {
            intPart = new Long(amtStr.substring(0, pos)).longValue();
            decPart = new Long(amtStr.substring(pos + 1)).longValue();
        } else {
            if (amtStr != null && !amtStr.equals("")) {
                intPart = new Long(amtStr).longValue();
            }
        }
        if (method.equals(NEAREST)) {
            decPart = rd.getNearest(decPart, 10);
            amtStr = intPart + "." + decPart;
            System.out.println("Nearestt " + amtStr);
        } else if (method.equals(LOWER)) {
            decPart = rd.lower(decPart, 10);
            amtStr = intPart + "." + decPart;
            System.out.println("Lower " + amtStr);
        } else if (method.equals(HIGHER)) {
            decPart = rd.higher(decPart, 10);
            amtStr = intPart + "." + decPart;
            System.out.println("Higher " + amtStr);
        } else if (method.equals(NO_ROUND_OFF)) {
//            decPart = rd.higher(decPart,10);
            // amtStr = intPart+"."+decPart;
            if (!amt.equals("")) {
                amtStr = df.format(Double.parseDouble(amt));
            } else {
                amtStr = amt;
            }
        }
       return amtStr;
    }
}
