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
package com.see.truetransact.serverside.common.charges.chargescalc;

import java.util.HashMap;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;

public class ChargesCalculation {

    private static SqlMap sqlMap = null;

    /**
     * Creates a new instance of ChargesCalculation
     */
    public ChargesCalculation() throws ServiceLocatorException {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    public double calculateCharge(HashMap chargeMap) throws Exception {
        /**
         * This method inserts charges into table ACT_CHARGES Input as HashMap
         * HashMap keys same as Table Column Names
         */
        double inputAmt = Double.parseDouble((String) chargeMap.get(CommonConstants.ACT_AMT));
        double inputCount = CommonUtil.convertObjToDouble((String) chargeMap.get(CommonConstants.ACT_COUNT)).doubleValue();
        double calculatedCharge = 0;

        HashMap outputMap = null;
        outputMap = (HashMap) sqlMap.executeQueryForObject("Charges.getChargesAmount", chargeMap);

        if (outputMap != null) {
            double toAmt = CommonUtil.convertObjToDouble(outputMap.get("TO_AMT")).doubleValue();
            double fixedRate = CommonUtil.convertObjToDouble(outputMap.get("FIXED_RATE")).doubleValue();
            double percentage = CommonUtil.convertObjToDouble(outputMap.get("PERCENTAGE")).doubleValue();
            double forEveryAmt = CommonUtil.convertObjToDouble(outputMap.get("FOR_EVERY_AMT")).doubleValue();
            double forEveryRate = CommonUtil.convertObjToDouble(outputMap.get("FOR_EVERY_RATE")).doubleValue();
            String forEveryType = CommonUtil.convertObjToStr(outputMap.get("FOR_EVERY_TYPE"));

            if (fixedRate != 0) {
                calculatedCharge += fixedRate;
            }
            if (percentage != 0) {
                calculatedCharge += (inputAmt * percentage) / 100;
            }

            if (inputAmt > toAmt) {
                if (forEveryAmt != 0) {
                    double remainder = inputAmt - toAmt;
                    if (forEveryType.toUpperCase().equals("AMOUNT")) //Value from Lookup Table
                    {
                        calculatedCharge += (remainder / forEveryAmt) * forEveryRate;
                    } else if (forEveryType.toUpperCase().equals("PERCENTAGE"))//Value from Lookup Table
                    {
                        calculatedCharge += ((remainder / forEveryAmt) * percentage) / 100;
                    }
                }
            }
            //Handle count based scenario. Eg: Folio
            if (inputCount > 0) {
                calculatedCharge *= inputCount;
            }

            //Put value back in Map
            chargeMap.put(CommonConstants.CHRG_AMT, String.valueOf(calculatedCharge));
            insertData(chargeMap);
            return calculatedCharge;
        } else {
            throw new TTException("Rule not defined for input amount : " + inputAmt);
        }
    }

    private void insertData(HashMap chargeMap) throws Exception {
        sqlMap.executeUpdate("Charges.insertCalcCharges", chargeMap);
    }

    public static void main(String args[]) {
        HashMap m = new HashMap();
        m.put("CHRG_TYPE", "GENCHG");
        m.put("PRODUCT_TYPE", "OA");
        m.put("PRODUCT_ID", "CAGen");
        m.put("ACT_AMT", "9005");
        try {
            ChargesCalculation cc = new ChargesCalculation();
            cc.calculateCharge(m);
        } catch (Exception E) {
            E.printStackTrace();
        }
    }
}
