/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ChargesCalculation.java
 *
 * Created on December 27, 2004, 2:50 PM
 */

/*
 * @author  152691
 */
package com.see.truetransact.serverside.charges;

import java.util.HashMap;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;

public class ChargesCalculation {
    //CHARGES INPUT MAP KEYS

    public static final String ACT_AMT = "ACT_AMT";
    public static final String ACT_COUNT = "ACT_COUNT";
    public static final String ACT_NUM = "ACT_NUM";
    public static final String CHRG_TYPE = "CHRG_TYPE";
    public static final String CHRG_AMT = "CHRG_AMT";
    public static final String AC_HD_ID = "AC_HD_ID";
    public static final String PRODUCT_ID = "PRODUCT_ID";
    public static final String PRODUCT_TYPE = "PRODUCT_TYPE";
    private static SqlMap sqlMap = null;

    /**
     * Creates a new instance of ChargesCalculation
     */
    public ChargesCalculation() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    public double calculateAndInsertCharge(HashMap chargeMap) throws Exception {
        /**
         * This method inserts charges into table ACT_CHARGES Input as HashMap
         * HashMap keys same as Table Column Names
         */
        double inputAmt = Double.parseDouble((String) chargeMap.get(CommonConstants.ACT_AMT));
        double inputCount = CommonUtil.convertObjToDouble((String) chargeMap.get(CommonConstants.ACT_COUNT)).doubleValue();
        double calculatedCharge = 0;

        HashMap outputMap = null;
        outputMap = (HashMap) sqlMap.executeQueryForObject("Charges.getChargesAmount", chargeMap);
        //foliocharge not need outputmap
        if (outputMap != null && (!CommonUtil.convertObjToStr(chargeMap.get("CHRG_TYPE")).equals("FOLIOCHG"))) {
            double fromAmt = CommonUtil.convertObjToDouble(outputMap.get("FROM_AMT")).doubleValue();
            double fixedRate = CommonUtil.convertObjToDouble(outputMap.get("FIXED_RATE")).doubleValue();
            double percentage = CommonUtil.convertObjToDouble(outputMap.get("PERCENTAGE")).doubleValue();
            double forEveryAmt = CommonUtil.convertObjToDouble(outputMap.get("FOR_EVERY_AMT")).doubleValue();
            double forEveryRate = CommonUtil.convertObjToDouble(outputMap.get("FOR_EVERY_RATE")).doubleValue();
            String forEveryType = CommonUtil.convertObjToStr(outputMap.get("FOR_EVERY_TYPE"));

            if (fixedRate != 0) {
                calculatedCharge += fixedRate;
            }
            if (percentage != 0) {
                calculatedCharge += (fromAmt * percentage) / 100;
            }

            if (forEveryAmt != 0) {
                double remainder = inputAmt - fromAmt;
                if (forEveryType.toUpperCase().equals("AMOUNT")) //Value from Lookup Table
                {
                    calculatedCharge += (remainder / forEveryAmt) * forEveryRate;
                } else if (forEveryType.toUpperCase().equals("PERCENTAGE"))//Value from Lookup Table
                {
                    calculatedCharge += ((remainder / forEveryAmt) * percentage) / 100;
                }
            }
            //Handle count based scenario. Eg: Folio
            if (inputCount > 0) {
                calculatedCharge *= inputCount;
            }

            //Put value back in Map
            chargeMap.put(CommonConstants.CHRG_AMT, String.valueOf(calculatedCharge));

            insertCharge(chargeMap);
            return calculatedCharge;
        } else if (chargeMap != null) {
            System.out.println("chargemap####" + chargeMap);
            double folioCount = CommonUtil.convertObjToDouble(chargeMap.get("ACT_COUNT")).doubleValue();
            double perFolioAmt = CommonUtil.convertObjToDouble(chargeMap.get("PER_FOLIO_AMT")).doubleValue();
            double availableBalance = CommonUtil.convertObjToDouble(chargeMap.get("AVAILABLE_BALANCE")).doubleValue();
            String uptobalanceType = CommonUtil.convertObjToStr(chargeMap.get("CHCK_UPTO_BALANCE"));
            chargeMap.put(CommonConstants.CHRG_AMT, String.valueOf(folioCount * perFolioAmt));
            calculatedCharge = folioCount * perFolioAmt;
            double remainAmt = 0;
            if (uptobalanceType.equals("UPTO BALANCE")) {
                remainAmt = availableBalance - calculatedCharge;
                if (remainAmt < 0) {
                    chargeMap.put("REMAIN_AMT", new Double(remainAmt));
                }

            } else if (uptobalanceType.equals("UPTO OVER DRAWING BALANCE")) {
                remainAmt = 0;
                chargeMap.put("REMAIN_AMT", new Double(remainAmt));
            } else {
                remainAmt = calculatedCharge;
                calculatedCharge = 0;
                chargeMap.put("REMAIN_AMT", new Double(remainAmt));
            }

            insertCharge(chargeMap);
            return calculatedCharge;
        } else {
            throw new TTException("Rule not defined for input amount : " + inputAmt);
        }
    }

    public void insertCharge(HashMap chargeMap) throws Exception {
        System.out.println("insertChargeData" + chargeMap);

        sqlMap.executeUpdate("Charges.insertCalcCharges", chargeMap);
    }

    public static void main(String args[]) {
        HashMap m = new HashMap();
        m.put("CHRG_TYPE", "FOLIOCHG");
        m.put("PRODUCT_TYPE", "OA");
        m.put("PRODUCT_ID", "CAGen");
        m.put("ACT_AMT", "900");
        try {
            ChargesCalculation cc = new ChargesCalculation();
            cc.calculateAndInsertCharge(m);
        } catch (Exception E) {
            E.printStackTrace();
        }
    }
}
