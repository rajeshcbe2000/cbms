/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * InterestTaskRunner.java
 *
 * Created on January 13, 2005, 12:25 PM
 */
package com.see.truetransact.serverside.common.interest;

import java.util.HashMap;
import java.util.Date;
import java.util.List;
//import java.util.GregorianCalendar ;
//import java.util.ArrayList ;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.transferobject.batchprocess.interest.InterestBatchTO;
//import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
//import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.interestcalc.InterestCalculationBean;
import com.see.truetransact.commonutil.interestcalc.InterestCalculationConstants;
import com.see.truetransact.commonutil.interestcalc.CommonCalculateInterest;
import com.see.truetransact.serverside.transaction.common.TransactionDAOConstants;

/**
 *
 * @author 152691 This class is used to find out the interest rate applicable.
 * The inputs required are productId, Category and Principle Amt Tables to be
 * queried are Deposit_Roi_Group, Deposit_Roi_Group_Cat, Deposit_Roi_Group_Prod,
 * Deposit_Roi_Group_Type_Rate
 *
 * Return value will be the rate of Interst and penal interest in a HashMap
 * (double with 8 decimal places)
 */
public class InterestTaskRunner {

    private static SqlMap sqlMap = null;
    //input keys; PARAM MAP
    public static final String CATEGORY_ID = "CATEGORY_ID";
    public static final String PRODUCT_TYPE = "PRODUCT_TYPE";
    public static final String PRODUCT_ID = "PROD_ID";
    public static final String PRINCIPLE_AMT = "AMOUNT";
    public static final String LAST_APPL_DT = "LAST_APPL_DT";
    public static final String CREDIT_INT = "CREDIT_INT";
    public static final String DEBIT_INT = "DEBIT_INT";
    public static final String AC_HD_ID = "AC_HD_ID";
    public static final String DEPOSIT_DT = "DEPOSIT_DT";
    public static final String APPL_NEW_DT = "APPL_NEW_DT";
    //output keys;
    public static final String ROI = "ROI";
    public static final String PENAL_INTEREST = "PENAL_INT";
    public static final String ADDITIONAL_INTEREST = "LIMIT_AMOUNT";
    public static final String ADDL_INTEREST = "ADDITIONAL_INTEREST";
    public static final String PROD_ID = "PROD_ID";
    public int PENAL = 0;
    private int freqRec = 0;
    private double period = 0;
    public HashMap intDataMap = new HashMap();
    public HashMap interestMap = new HashMap();
    private String _branchCode = null;
    Date curDate = null;

    /**
     * Creates a new instance of PreInterestCalculator
     */
    public InterestTaskRunner() throws Exception {
        getSqlMap();
    }

    private void getSqlMap() throws Exception {
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }

    //__ To get the Rate of Interest...
    public HashMap getInterestRates(HashMap inputMap) throws Exception {
        System.out.println("#####getInterestRatesinputMap : " + inputMap);
        final HashMap dataMap = (HashMap) sqlMap.executeQueryForObject("icm.getInterestRates", inputMap);
      //  System.out.println("#####getInterestRatesdataMap : " + dataMap);
        //Map in com.see.truetransact.transferobject.common.interestcalculation.InterestCalcultionMap.xml
        return dataMap;
    }

    /*__ To calculate Product Amount for Interest Calculation
     *   For Operative TOD, Advances & Agri OD, CC types (All running a/cs)
     */
    public HashMap getProductAmtForIntCalc(HashMap resultMap, String productType) throws Exception {
        double productAmt = 0;
        if (productType.equals("OA")) {
            resultMap.put("TABLE_NAME", "ACT_DAYEND_BALANCE");
        } else if (productType.equals("AD")) {
            resultMap.put("TABLE_NAME", "ADVANCES_DAYEND_BALANCE");
            resultMap.put("CALC_MODE", "INTEREST");

        } else if (productType.equals("AAD")) {
            resultMap.put("TABLE_NAME", "AGRI_ADV_DAYEND_BALANCE");
        }
        HashMap dataMap = null;
        resultMap.put(TransactionDAOConstants.PROD_TYPE, productType);
        List productList = (List) sqlMap.executeQueryForList("getProductAmountForInterest", resultMap);
        int lstSize = productList.size();
        if (productType.equals("OA") || productType.equals("AD")) {
            if (productList != null && lstSize > 0) {
                for (int i = 0; i < productList.size(); i++) {
                    dataMap = (HashMap) productList.get(i);
                    productAmt += CommonUtil.convertObjToDouble(dataMap.get("AMT")).doubleValue();
                }
            }
        }
        if (productAmt < 0) {
            productAmt *= -1;
        }
        HashMap productMap = new HashMap();
        productMap.put("PRODUCT_AMT", new Double(productAmt));
        productMap.put("NO_OF_DAYS", new Integer(lstSize));
        return productMap;
    }

    /*__ To calculate the Interest and put the data in the InterestBatchTO Object...
     *   For Operative TOD, Advances & Agri OD, CC types (All running a/cs)
     */
    public InterestBatchTO getInterestAmountCommon(TaskHeader header, HashMap resultMap) throws Exception {
        System.out.println("#### interestAmount() 1 resultMap : " + resultMap);
        _branchCode = header.getBranchID();
        curDate = ServerUtil.getCurrentDate(_branchCode);

        //__ Create A new Object to Enter the Data in the DataBase...
        InterestBatchTO objInterestBatchTO = new InterestBatchTO();

        //__ if the End Date is Greater than the Start Date
        if ((DateUtil.dateDiff((Date) resultMap.get("START"), (Date) resultMap.get("END"))) >= 0) {
        //    System.out.println("DateDifference " + DateUtil.dateDiff((Date) resultMap.get("START"), (Date) resultMap.get("END")));
            double interestAmt = 0;
            HashMap paramMap = header.getTaskParam();
            HashMap productAmountMap = getProductAmtForIntCalc(resultMap, header.getProductType());
            Double productAmount = CommonUtil.convertObjToDouble(productAmountMap.get("PRODUCT_AMT"));
            resultMap.put(InterestTaskRunner.PRINCIPLE_AMT, productAmount);
            InterestCalculationBean objInterestCalculationBean = new InterestCalculationBean();
            objInterestCalculationBean.setPrincipalAmt(String.valueOf(productAmount));

            objInterestCalculationBean.setIsDuration_ddmmyy(false);
            objInterestCalculationBean.setYearOption(InterestCalculationConstants.YEAR_OPTION_360);
            objInterestCalculationBean.setMonthOption(InterestCalculationConstants.MONTH_OPTION_30);
            objInterestCalculationBean.setDuration_FromDate(DateUtil.getStringDate((Date) resultMap.get("START")));
            objInterestCalculationBean.setDuration_ToDate(DateUtil.getStringDate((Date) resultMap.get("END")));

            if (header.getProductType().equals(TransactionFactory.OPERATIVE)) {
                objInterestCalculationBean.setIsDuration_ddmmyy(true);
                objInterestCalculationBean.setYearOption(InterestCalculationConstants.YEAR_OPTION_360);
                objInterestCalculationBean.setMonthOption(InterestCalculationConstants.MONTH_OPTION_30);
                objInterestCalculationBean.setDuration_mm("1");
                objInterestCalculationBean.setInterestType(InterestCalculationConstants.SIMPLE_INTEREST);
                objInterestCalculationBean.setCompoundingPeriod("30");
            //    System.out.println("#######TransactionFactory.OPERATIVE : " + objInterestCalculationBean);
                resultMap.put("INT_TYPE", "D");  // To take Debit ROI from DEPOSIT_ROI_GROUP_TYPE_RATE
                resultMap.put("DEPOSIT_DT", resultMap.get("START"));  // To take Debit ROI from DEPOSIT_ROI_GROUP_TYPE_RATE
            }
            if (header.getProductType().equals(TransactionFactory.ADVANCES)) {
                objInterestCalculationBean.setIsDuration_ddmmyy(true);
                objInterestCalculationBean.setYearOption(InterestCalculationConstants.YEAR_OPTION_365);
                objInterestCalculationBean.setMonthOption(InterestCalculationConstants.MONTH_OPTION_30);
                objInterestCalculationBean.setDuration_mm("1");
                objInterestCalculationBean.setInterestType(InterestCalculationConstants.SIMPLE_INTEREST);
                objInterestCalculationBean.setCompoundingPeriod("30");
            //    System.out.println("#######TransactionFactory.advances : " + objInterestCalculationBean);
                resultMap.put("INT_TYPE", "C");  // To take Debit ROI from DEPOSIT_ROI_GROUP_TYPE_RATE
                resultMap.put("DEPOSIT_DT", resultMap.get("START"));  // To take Debit ROI from DEPOSIT_ROI_GROUP_TYPE_RATE
            }
            String rateMaturity = "0.0";
            double interestOnMaturity = 0;

            rateMaturity = setRateOfInterst(resultMap, header.getProductType());

          //  System.out.println("#### interestAmount() after setRateOfInterst() resultMap : " + resultMap);

            objInterestCalculationBean.setRateOfInterest(rateMaturity);
            objInterestCalculationBean.setInterestType(isCompounded(resultMap));
            //        objInteresicm.getInterestRatestCalculationBean.setFloatPrecision("8");
            objInterestCalculationBean.setFloatPrecision("2");
            resultMap.put("ROUNF_OFF", "HIGHER_VALUE");
            objInterestCalculationBean.setRoundingType(roundOff(resultMap));
            interestMap.put(CommonConstants.DATA, objInterestCalculationBean);
            interestMap.put("NO_OF_DAYS", productAmountMap.get("NO_OF_DAYS"));
       //     System.out.println("objInterestCalculationBean:" + objInterestCalculationBean);

          //  System.out.println("#### interestAmount() before if condition resultMap : " + resultMap);

            //System.out.println("before calculation interestMap:" + interestMap);

            interestMap = CommonCalculateInterest.getInterest(interestMap);

         //   System.out.println("After calculation interestMap:" + interestMap);
            interestOnMaturity = CommonUtil.convertObjToDouble(interestMap.get(InterestCalculationConstants.INTEREST)).doubleValue();

            interestAmt = interestOnMaturity;
            objInterestBatchTO.setActNum(CommonUtil.convertObjToStr(paramMap.get("ACT_NUM")));

            //__ Check the Transaction Type...
//            if(header.getTransactionType().equals(CommonConstants.PAYABLE)){
//                objInterestBatchTO.setAcHdId(CommonUtil.convertObjToStr(paramMap.get("DEBIT_INT")));
//                
//            }else{
//                objInterestBatchTO.setAcHdId(CommonUtil.convertObjToStr(paramMap.get("CREDIT_INT")));
//                
//            }
            //__ Depending on the Condition(s), Interest Amt is Sum of different Interest Amt's
            //__ at differetn Date Slabs...

            objInterestBatchTO.setIntAmt(new Double(interestAmt));

            objInterestBatchTO.setIntDt(curDate);
            objInterestBatchTO.setIntRate(CommonUtil.convertObjToDouble(objInterestCalculationBean.getRateOfInterest()));

            //__ Check for the Interest Type...

            if (resultMap.containsKey("COMP_TYPE") && resultMap.get("COMP_TYPE").equals("Y")) {
                objInterestBatchTO.setIntType(InterestCalculationConstants.COMPOUND_INTEREST);

            } else {
                objInterestBatchTO.setIntType(InterestCalculationConstants.SIMPLE_INTEREST);

            }
            objInterestBatchTO.setPrincipleAmt(CommonUtil.convertObjToDouble(objInterestCalculationBean.getPrincipalAmt()));
            objInterestBatchTO.setProductId(CommonUtil.convertObjToStr(resultMap.get("PROD_ID")));
            objInterestBatchTO.setProductType(header.getProductType());
            objInterestBatchTO.setTransLogId(null);

            objInterestBatchTO.setCustId(CommonUtil.convertObjToStr(resultMap.get("CUST_ID")));
            //__ Default value...
            objInterestBatchTO.setIsTdsApplied("N");

            if (CommonUtil.convertObjToInt(resultMap.get("FREQ")) > 0) {
                objInterestBatchTO.setApplDt(curDate);
            }

            //__Use this value to run the update on the OP_AC_INTPAY_PARAM table
            paramMap.put("APPL_NEW_DT", objInterestBatchTO.getApplDt());

            //__ Enter the Interest Calculated at the Maturity Level/Date..
            intDataMap.put(CommonConstants.AT_MATURITY_INT, String.valueOf(interestOnMaturity));
            intDataMap.put(CommonConstants.TOTAL_INTEREST_INT, String.valueOf(interestAmt));

            intDataMap.put(CommonConstants.AT_MATURITY_ROI, rateMaturity);

        } else {
            throw new TTException("Start Date is greater than the End Date...");

        }
      //  System.out.println("interestMap in Runner: " + getIntDataMap());
     //   System.out.println("interestAmountEnding " + objInterestBatchTO);
        return objInterestBatchTO;
    }
    //__ To calculate the Interest and put the data in the InterestBatchTO Object...

    public InterestBatchTO interestAmount(TaskHeader header, HashMap resultMap) throws Exception {
        System.out.println("#### interestAmount() 1 resultMap : " + resultMap);
        _branchCode = header.getBranchID();
        curDate = ServerUtil.getCurrentDate(_branchCode);

        //__ Create A new Object to Enter the Data in the DataBase...
        InterestBatchTO objInterestBatchTO = new InterestBatchTO();


        //__ if the End Date is Greater than the Start Date
        if ((DateUtil.dateDiff((Date) resultMap.get("START"), (Date) resultMap.get("END"))) >= 0) {
            //__ Fields regarding the Interest Amount(s)...
       //     System.out.println("DateDifference " + DateUtil.dateDiff((Date) resultMap.get("START"), (Date) resultMap.get("END")));
            double interestAmt = 0;
            double interestOnMaturity = 0;
            double interestAfterMaturity = 0;
            double interestBeforeMaturity = 0;

            //__ Fields regarding the Rate of Interest...
            String rateBeforeMaturity = "0.0";
            String rateMaturity = "0.0";
            String rateAfterMaturity = "0.0";

            //__ Reset the Penal Interest type...
            setPENAL(0);


            HashMap paramMap = header.getTaskParam();
            InterestCalculationBean objInterestCalculationBean = new InterestCalculationBean();
            objInterestCalculationBean.setPrincipalAmt(CommonUtil.convertObjToStr(resultMap.get("AMOUNT")));

            objInterestCalculationBean.setIsDuration_ddmmyy(false);
            objInterestCalculationBean.setYearOption(InterestCalculationConstants.YEAR_OPTION_360);
            objInterestCalculationBean.setMonthOption(InterestCalculationConstants.MONTH_OPTION_30);
            objInterestCalculationBean.setDuration_FromDate(DateUtil.getStringDate((Date) resultMap.get("START")));
            objInterestCalculationBean.setDuration_ToDate(DateUtil.getStringDate((Date) resultMap.get("END")));

            if (header.getProductType().equals(TransactionFactory.OPERATIVE)) {
                objInterestCalculationBean.setIsDuration_ddmmyy(true);
                objInterestCalculationBean.setYearOption(InterestCalculationConstants.YEAR_OPTION_360);
                objInterestCalculationBean.setMonthOption(InterestCalculationConstants.MONTH_OPTION_30);
                objInterestCalculationBean.setDuration_mm("1");
                objInterestCalculationBean.setInterestType(InterestCalculationConstants.SIMPLE_INTEREST);
                objInterestCalculationBean.setCompoundingPeriod("30");
              //  System.out.println("#######TransactionFactory.OPERATIVE : " + objInterestCalculationBean);
            }

            //__ Use Period in Case of Deposit Interest to be Paid on Maturity....
            if (CommonUtil.convertObjToStr(header.getProductType()).equalsIgnoreCase(TransactionFactory.DEPOSITS)) {
                //__ Calculate the Period as the diff of DepositDT and MaturityDT
//                long period = DateUtil.dateDiff((Date)resultMap.get("DEPOSIT_DT"), (Date)resultMap.get("MATURITY_DT"));
                period = DateUtil.dateDiff((Date) resultMap.get("START"), (Date) resultMap.get("END"));
                resultMap.put("PERIOD", String.valueOf(period));
            }
//            if (resultMap.containsKey("BEHAVES_LIKE")) {
//                if(CommonUtil.convertObjToStr(resultMap.get("BEHAVES_LIKE")).equals("RECURRING")){
//                    resultMap.remove("PERIOD");
//                    resultMap.put("PERIOD",resultMap.get("DIFFPERIOD"));
//                    
//                }
//            }

            //__ Get the Rate Of Interest for Calculating the Maturity Interest...
            rateMaturity = setRateOfInterst(resultMap, header.getProductType());
       //     System.out.println("#### interestAmount() after setRateOfInterst() resultMap : " + resultMap);

            objInterestCalculationBean.setRateOfInterest(rateMaturity);
            objInterestCalculationBean.setInterestType(isCompounded(resultMap));
            //        objInteresicm.getInterestRatestCalculationBean.setFloatPrecision("8");
            objInterestCalculationBean.setFloatPrecision("2");

            if (resultMap.containsKey("BEHAVES_LIKE")) {
                if (CommonUtil.convertObjToStr(resultMap.get("BEHAVES_LIKE")).equals("RECURRING")) {
                    objInterestCalculationBean.setCompoundingType(InterestCalculationConstants.COMPTYPE_PERIODIC_DEPOSIT);
//                    objInterestCalculationBean.setCompoundingPeriod(CommonUtil.convertObjToStr(resultMap.get("COMP_FREQ")));
                    //System.out.println("######BEHAVES_LIKE RECURRING : " + objInterestCalculationBean);
                }

                //--- If it is Cumulative Deposit , set the parameter for it appropriately
                if (CommonUtil.convertObjToStr(resultMap.get("BEHAVES_LIKE")).equals("CUMMULATIVE")) {
                    objInterestCalculationBean.setRoundingType(roundOff(resultMap));
                    objInterestCalculationBean.setCompoundingType(InterestCalculationConstants.COMPTYPE_COMPOUND);
                   // System.out.println("@@@@@BEHAVES_LIKE CUMMULATIVE : " + objInterestCalculationBean);
                    objInterestCalculationBean.setCompoundingPeriod(CommonUtil.convertObjToStr(resultMap.get("COMP_FREQ")));
                }
            }

            objInterestCalculationBean.setRoundingType(roundOff(resultMap));
//            objInterestCalculationBean.setCompoundingType("COMPOUND");

            //__ Calculate the Maturity Interest...
//            HashMap interestMap = new HashMap();
            interestMap.put(CommonConstants.DATA, objInterestCalculationBean);
           // System.out.println("objInterestCalculationBean:" + objInterestCalculationBean);
      //      System.out.println("before calculation interestMap:" + interestMap);
            interestMap.put("FREQ", resultMap.get("FREQ"));
        //    System.out.println("########FREQ" + interestMap);
        //    System.out.println("########FREQinsideif" + interestMap.get("FREQ"));
            int freq = CommonUtil.convertObjToInt(resultMap.get("FREQ"));
//            int freq = CommonUtil.convertObjToInt(resultMap.get("PERIOD"));
           // System.out.println("Frequency Date" + freq);
            //incase behaveslike recurring freqRec assign to freq.
//            freqRec = 0;            
      //      System.out.println("#### interestAmount() before if condition resultMap : " + resultMap);
            if (CommonUtil.convertObjToStr(resultMap.get("BEHAVES_LIKE")).equals("CUMMULATIVE")) {
                objInterestCalculationBean.setRoundingType(roundOff(resultMap));
                objInterestCalculationBean.setPrincipalAmt(CommonUtil.convertObjToStr(resultMap.get("AMOUNT")));
                objInterestCalculationBean.setRoundingType(CommonUtil.convertObjToStr(resultMap.get("ROUND_OFF")));
                freq = CommonUtil.convertObjToInt(resultMap.get("PERIOD"));
            //    System.out.println("######## behavesLike CUMMULATIVE : " + resultMap);
                resultMap.remove("DIFFPERIOD");
            }

            if (CommonUtil.convertObjToStr(resultMap.get("BEHAVES_LIKE")).equals("FIXED")) {
                objInterestCalculationBean.setRoundingType(roundOff(resultMap));
                objInterestCalculationBean.setCompoundingType(InterestCalculationConstants.COMPTYPE_PERIODIC_DEPOSIT);
                objInterestCalculationBean.setPrincipalAmt(CommonUtil.convertObjToStr(resultMap.get("AMOUNT")));
                objInterestCalculationBean.setRoundingType(CommonUtil.convertObjToStr(resultMap.get("ROUND_OFF")));
                freq = CommonUtil.convertObjToInt(resultMap.get("PERIOD"));
            //    System.out.println("######## behavesLike FIXED : " + resultMap);
                resultMap.remove("DIFFPERIOD");
            }

            if (CommonUtil.convertObjToStr(resultMap.get("BEHAVES_LIKE")).equals("RECURRING")) {
//                  if(freq == 0) 
//                        freq = CommonUtil.convertObjToInt(resultMap.get("PERIOD"));
//                  if((DateUtil.dateDiff((Date)resultMap.get("START"), (Date)resultMap.get("END"))) >=freq)
//                    period = DateUtil.dateDiff((Date)resultMap.get("START"), (Date)resultMap.get("END"));
//                    period = period/30;
//                    resultMap.put("PERIOD",String.valueOf(period));
//                    System.out.println("########Recurring Period : " + period);
//                    interestMap.put("PERIOD",String.valueOf(period));
//                    interestMap.put("BEHAVES_LIKE",resultMap.get("BEHAVES_LIKE"));
//                System.out.println("Recurring calculation interestMap:" + interestMap);
//                  interestMap = CommonCalculateInterest.getInterest(interestMap);
//                  System.out.println("After calculation interestMap:" + interestMap);
//                  interestOnMaturity = CommonUtil.convertObjToDouble(interestMap.get(InterestCalculationConstants.INTEREST)).doubleValue();
                objInterestCalculationBean.setRoundingType(roundOff(resultMap));
                objInterestCalculationBean.setPrincipalAmt(CommonUtil.convertObjToStr(resultMap.get("AMOUNT")));
                objInterestCalculationBean.setRoundingType(CommonUtil.convertObjToStr(resultMap.get("ROUND_OFF")));
                freq = CommonUtil.convertObjToInt(resultMap.get("PERIOD"));
             ///   System.out.println("######## behavesLike RECURRING : " + resultMap);
                resultMap.remove("DIFFPERIOD");
            }
//            }
            //__ Use Period in Case of Deposit Interest to be Paid on Maturity....

            if (freq == 0) {
                freq = CommonUtil.convertObjToInt(resultMap.get("PERIOD"));
            }
//            if((DateUtil.dateDiff((Date)resultMap.get("START"), (Date)resultMap.get("END"))) >=freq || (header.getProductType().equals(TransactionFactory.OPERATIVE)) )
        //    System.out.println("before calculation interestMap:" + interestMap);
            if ((DateUtil.dateDiff((Date) resultMap.get("START"), (Date) resultMap.get("END"))) >= 0) {
                interestMap = CommonCalculateInterest.getInterest(interestMap);
            }
       //     System.out.println("After calculation interestMap:" + interestMap);
            interestOnMaturity = CommonUtil.convertObjToDouble(interestMap.get(InterestCalculationConstants.INTEREST)).doubleValue();
           // System.out.println("############## resultMap : " + resultMap);
           // System.out.println("########### Actual Closing Int Payable Amount : " + interestOnMaturity);
            //Added By Suresh
            if (resultMap.containsKey("INT_PAYABLE_AMOUNT") && (CommonUtil.convertObjToDouble(resultMap.get("INT_PAYABLE_AMOUNT")).doubleValue() > 0)) {
                interestOnMaturity = CommonUtil.convertObjToDouble(resultMap.get("INT_PAYABLE_AMOUNT")).doubleValue();
             //   System.out.println("########### Changed Closing Int Payable Amount : " + interestOnMaturity);
            }
            interestAmt = interestOnMaturity;
            //__ Perform these Calculation only inCase the ProductType is "Deposits"...
            if (CommonUtil.convertObjToStr(header.getProductType()).equalsIgnoreCase(TransactionFactory.DEPOSITS)) {
                //__ if the Closing Date is Specified...
                if (resultMap.containsKey("CLOSING_DT")) {

                    //__ Calculate the Period as the diff of ClosingDT and MaturityDT
                    long period = DateUtil.dateDiff((Date) resultMap.get("MATURITY_DT"), (Date) resultMap.get("CLOSING_DT"));

                    //__ If the Account is Closed After the Maturity Date...
                    if (period > 0) {
                       // System.out.println("period > 0  ");
                        HashMap aftermaturityDate = (HashMap) sqlMap.executeQueryForObject("icm.getDepositMaturityIntRate", paramMap);
                        if (aftermaturityDate.get("INTPAID_AFTERMATURITY").toString().equals("Y")) {
                            if (!CommonUtil.convertObjToStr(aftermaturityDate.get("INT_MATURED_DEP")).equals("0")) {
                                resultMap.put("PERIOD", String.valueOf(period));
                                objInterestCalculationBean.setRateOfInterest(CommonUtil.convertObjToStr(aftermaturityDate.get("INT_MATURED_DEP")));

                            } else {
                                if (!CommonUtil.convertObjToStr(aftermaturityDate.get("AFTERMATURITY_INTTYPE")).equals("MATURITY_RATE")) {
                                    resultMap.put("DEPOSIT_DT", (Date) resultMap.get("CLOSING_DT"));

                                    //__ Get the Rate Of Interest for Calculating the Interest After the Maturity Date...
                                    System.out.println("@#@#@#@#@#@# resultmap here : "+resultMap);
                                    rateAfterMaturity = setRateOfInterst(resultMap, header.getProductType());
                                    objInterestCalculationBean.setRateOfInterest(rateAfterMaturity);
                                }
                            }

                            //__ get the Interest for the Deposit Account, From the Maturity Date to Closing Date...
                            objInterestCalculationBean.setInterestType(CommonUtil.convertObjToStr(aftermaturityDate.get("AFTERMATURITY_INTTYPE")));
                            objInterestCalculationBean.setDuration_FromDate(DateUtil.getStringDate((Date) resultMap.get("MATURITY_DT")));
                            objInterestCalculationBean.setDuration_ToDate(DateUtil.getStringDate((Date) resultMap.get("CLOSING_DT")));

                            //__ Calculate the Interest when the ClosingDT is after the MaturityDT...
                            HashMap intMap = new HashMap();
                            intMap.put(CommonConstants.DATA, objInterestCalculationBean);
                            intMap = CommonCalculateInterest.getInterest(intMap);

                            interestAfterMaturity = CommonUtil.convertObjToDouble(intMap.get(InterestCalculationConstants.INTEREST)).doubleValue();
                            interestAmt = interestOnMaturity + interestAfterMaturity;
                       //     System.out.println("interestAmt:  " + interestAmt);
                        }

                        //__ If the Account is Closed Before the Maturity Date...
                    } else if (period < 0) {
                        System.out.println("period < 0  ");
                        //__ Interest type is Penal...
                        setPENAL(1);
                        //__ The Penal Interest Rate Should be taken into account...
                        //__ Get the Deposit date for the Particular Deposit No and Sub No...

                        //__ Calculate the Period as the diff of ClosingDT and DepositDT
                        period = DateUtil.dateDiff((Date) resultMap.get("DEPOSIT_DT"), (Date) resultMap.get("CLOSING_DT"));
                        resultMap.put("PERIOD", String.valueOf(period));

                        //__ get the Interest for the Deposit Account, From the Deposit Date to Closing Date...
                        //__ Get the Rate Of Interest for Calculating the Interest Before the Maturity Date...
                        rateBeforeMaturity = setRateOfInterst(resultMap, header.getProductType());

                        objInterestCalculationBean.setRateOfInterest(rateBeforeMaturity); //get current interest rate
                        objInterestCalculationBean.setDuration_FromDate(DateUtil.getStringDate((Date) resultMap.get("DEPOSIT_DT")));
                        objInterestCalculationBean.setDuration_ToDate(DateUtil.getStringDate((Date) resultMap.get("CLOSING_DT")));

                        //__ Calculate the Interest when the ClosingDT is before the MaturityDT...
                        HashMap intMap = new HashMap();
                        intMap.put(CommonConstants.DATA, objInterestCalculationBean);
                        intMap = CommonCalculateInterest.getInterest(intMap);

                        interestBeforeMaturity = CommonUtil.convertObjToDouble(intMap.get(InterestCalculationConstants.INTEREST)).doubleValue();
                        interestAmt = interestBeforeMaturity;
                    }
                }
            }

            objInterestBatchTO.setActNum(CommonUtil.convertObjToStr(paramMap.get("ACT_NUM")));

            //__ Check the Transaction Type...
            if (header.getTransactionType().equals(CommonConstants.PAYABLE)) {
                objInterestBatchTO.setAcHdId(CommonUtil.convertObjToStr(paramMap.get("DEBIT_INT")));

            } else {
                objInterestBatchTO.setAcHdId(CommonUtil.convertObjToStr(paramMap.get("CREDIT_INT")));

            }
            //__ Depending on the Condition(s), Interest Amt is Sum of different Interest Amt's
            //__ at differetn Date Slabs...
            objInterestBatchTO.setIntAmt(new Double(interestAmt));
            objInterestBatchTO.setIntDt(curDate);
            objInterestBatchTO.setIntRate(CommonUtil.convertObjToDouble(objInterestCalculationBean.getRateOfInterest()));

            //__ Check for the Interest Type...

            if (resultMap.containsKey("COMP_TYPE") && resultMap.get("COMP_TYPE").equals("Y")) {
                objInterestBatchTO.setIntType(InterestCalculationConstants.COMPOUND_INTEREST);

            } else {
                objInterestBatchTO.setIntType(InterestCalculationConstants.SIMPLE_INTEREST);

            }
            objInterestBatchTO.setPrincipleAmt(CommonUtil.convertObjToDouble(objInterestCalculationBean.getPrincipalAmt()));
            objInterestBatchTO.setProductId(CommonUtil.convertObjToStr(paramMap.get("PRODUCT_ID")));
            objInterestBatchTO.setProductType(header.getProductType());
            objInterestBatchTO.setTransLogId(null);

            //__ Check if the Calculation And Application Frequencies are equal or not..
            Date lastAppDt = (Date) paramMap.get(InterestTaskRunner.LAST_APPL_DT);
       //     System.out.println("interestTaskRunner: " + lastAppDt);
//            if(CommonUtil.convertObjToInt(resultMap.get("FREQ")) == CommonUtil.convertObjToInt(resultMap.get("FREQ_A"))){
//                objInterestBatchTO.setApplDt(currDt);
//            }
//            else{
//                objInterestBatchTO.setApplDt(DateUtil.addDays(lastAppDt,CommonUtil.convertObjToInt(resultMap.get("FREQ_A"))));
//            }
            objInterestBatchTO.setCustId(CommonUtil.convertObjToStr(resultMap.get("CUST_ID")));
            //__ Default value...
            objInterestBatchTO.setIsTdsApplied("N");

            if (CommonUtil.convertObjToInt(resultMap.get("FREQ")) > 0) {
                objInterestBatchTO.setApplDt(curDate);
            }

            //__Use this value to run the update on the OP_AC_INTPAY_PARAM table
            paramMap.put("APPL_NEW_DT", objInterestBatchTO.getApplDt());

            //__ Enter the Interest Calculated at the Maturity Level/Date..
            intDataMap.put(CommonConstants.AT_MATURITY_INT, String.valueOf(interestOnMaturity));
            intDataMap.put(CommonConstants.BEFORE_MATURITY_INT, String.valueOf(interestBeforeMaturity));
            intDataMap.put(CommonConstants.AFTER_MATURITY_INT, String.valueOf(interestAfterMaturity));
            intDataMap.put(CommonConstants.TOTAL_INTEREST_INT, String.valueOf(interestAmt));

            intDataMap.put(CommonConstants.AT_MATURITY_ROI, rateMaturity);
            intDataMap.put(CommonConstants.BEFORE_MATURITY_ROI, rateBeforeMaturity);
            intDataMap.put(CommonConstants.AFTER_MATURITY_ROI, rateAfterMaturity);


        } else {
            throw new TTException("Start Date is greater than the End Date...");

        }
    //    System.out.println("interestMap in Runner: " + getIntDataMap());
      //  System.out.println("interestAmountEnding " + objInterestBatchTO);
        return objInterestBatchTO;
    }

    //__ Method to Insert the record into the Table...
    private String roundOff(HashMap resultMap) {
        /**
         * Check for the Rounding Off Criteria
         */
        String roundOffVal = null;
        if (CommonUtil.convertObjToStr(resultMap.get("ROUNF_OFF")).equalsIgnoreCase("HIGHER_VALUE")) {
            roundOffVal = InterestCalculationConstants.ROUNDING_HIGHER;
        } else if (CommonUtil.convertObjToStr(resultMap.get("ROUNF_OFF")).equalsIgnoreCase("LOWER_VALUE")) {
            roundOffVal = InterestCalculationConstants.ROUNDING_LOWER;
        } else if (CommonUtil.convertObjToStr(resultMap.get("ROUNF_OFF")).equalsIgnoreCase("NEAREST_VALUE")) {
            roundOffVal = InterestCalculationConstants.ROUNDING_NEAREST;
        }

        return roundOffVal;
    }

    private String setRateOfInterst(HashMap resultMap, String productType) throws Exception {
        //System.out.println("Resultmap in setRateOfInterest : " + resultMap);
        HashMap map = new HashMap();
        map.put(InterestTaskRunner.PRODUCT_ID, resultMap.get(InterestTaskRunner.PRODUCT_ID));
        map.put(InterestTaskRunner.PRODUCT_TYPE, productType);
        map.put(InterestTaskRunner.CATEGORY_ID, resultMap.get(InterestTaskRunner.CATEGORY_ID));
        map.put(InterestTaskRunner.PRINCIPLE_AMT, CommonUtil.convertObjToDouble(resultMap.get(InterestTaskRunner.PRINCIPLE_AMT)));
        if (resultMap.containsKey("PERIOD")) {
            map.put("PERIOD", CommonUtil.convertObjToStr(resultMap.get("PERIOD")));
        }
//        if(resultMap.containsKey("BATCH_PROCESS") || resultMap.get("BATCH_PROCESS")==null){
//            if(resultMap.get("BATCH_PROCESS").equals("BATCH_PROCESS") || resultMap.get("BATCH_PROCESS").equals(null)){
//                resultMap.remove("PERIOD");
        if (resultMap.containsKey("PERIOD1")) {
            map.put("PERIOD", CommonUtil.convertObjToDouble(resultMap.get("PERIOD1")));
        } else if (CommonUtil.convertObjToDouble(resultMap.get("PERIOD")).doubleValue()>0) {
            map.put("PERIOD", CommonUtil.convertObjToDouble(resultMap.get("PERIOD")));
        }
      //  System.out.println("###### period :" + resultMap);
//            }
//        }
//        if (resultMap.containsKey("BEHAVES_LIKE")) {
//            if(CommonUtil.convertObjToStr(resultMap.get("BEHAVES_LIKE")).equals("RECURRING")){
//                resultMap.remove("PERIOD");
//                map.put("PERIOD",resultMap.get("DIFFPERIOD"));
//            }
//        }
        if (!resultMap.containsKey("DEPOSIT_DT")) {
            map.put(InterestTaskRunner.DEPOSIT_DT, curDate);
        } else {
            map.put(InterestTaskRunner.DEPOSIT_DT, (Date) resultMap.get("DEPOSIT_DT"));

        }

        String roi = "";
        if (resultMap.containsKey("INT_TYPE")) {
            map.put("INT_TYPE", resultMap.get("INT_TYPE"));
        }
        final HashMap dataMap = getInterestRates(map);
       // System.out.println("#####setRateOfInterst dataMap :" + dataMap);

//        if(resultMap.containsKey("BATCH_PROCESS") || resultMap.get("BATCH_PROCESS")== null) {
       // System.out.println("BATCH_PROCESSdataMap: " + dataMap);
        if (dataMap != null) {
            roi = CommonUtil.convertObjToStr(((HashMap) dataMap).get(InterestTaskRunner.ROI));
            if (resultMap.containsValue(InterestTaskRunner.PENAL_INTEREST)) {
                roi = CommonUtil.convertObjToStr(((HashMap) dataMap).get(InterestTaskRunner.PENAL_INTEREST));
            }
            if (resultMap.containsValue(InterestTaskRunner.ADDL_INTEREST)) {
                roi = CommonUtil.convertObjToStr(((HashMap) dataMap).get(InterestTaskRunner.ADDITIONAL_INTEREST));
            }
        } else {
            roi = "0.0";
        }
      //  System.out.println("#####setRateOfInterst ROI" + roi);
//        }
//        else {
//            if(dataMap  != null){
//                roi  = CommonUtil.convertObjToStr(((HashMap)dataMap).get(InterestTaskRunner.ROI));
//                System.out.println("dataMap: " + dataMap);
//                //__ if the Account Closure is Before the Maturity Date,
//                //__ Subtract the Penal int rom ROI...
//                if(productType.equalsIgnoreCase(TransactionFactory.DEPOSITS) && PENAL!=0){
//                    double rate = 0;
//                    double penal = 0;
//                    double result = 0;
//
//                    rate = Double.parseDouble(roi);
//                    penal = Double.parseDouble(CommonUtil.convertObjToStr(((HashMap)dataMap).get("PENAL_INT")));
//
//                    result = rate - penal;
//                    roi = String.valueOf(result);
//                }
//            }
//        }
    //    System.out.println("roi: " + roi);
        return roi;
    }

    //__ To get the Compound Freq Period, if the rate Type is Compounded...
    private String isCompounded(HashMap resultMap) {
        /**
         * If the Interest Calculation is Compounded...
         */
        String compoundingPeriod = null;
        if (CommonUtil.convertObjToStr(resultMap.get("COMP_TYPE")).equalsIgnoreCase("Y")) {
            compoundingPeriod = InterestCalculationConstants.COMPOUND_INTEREST;
            if (CommonUtil.convertObjToInt(resultMap.get("COMP_FREQ")) == 365) {
                compoundingPeriod = InterestCalculationConstants.COMP_ANNUALLY;
            } else if (CommonUtil.convertObjToInt(resultMap.get("COMP_FREQ")) == 180) {
                compoundingPeriod = InterestCalculationConstants.COMP_SEMIANNUALLY;
            } else if (CommonUtil.convertObjToInt(resultMap.get("COMP_FREQ")) == 90) {
                compoundingPeriod = InterestCalculationConstants.COMP_QUARTERLY;
            } else if (CommonUtil.convertObjToInt(resultMap.get("COMP_FREQ")) == 30) {
                compoundingPeriod = InterestCalculationConstants.COMP_MONTHLY;
            } else if (CommonUtil.convertObjToInt(resultMap.get("COMP_FREQ")) == 7) {
                compoundingPeriod = InterestCalculationConstants.COMP_WEEKLY;
            }
            compoundingPeriod = InterestCalculationConstants.COMPTYPE_COMPOUND;

        } else { //__ Default interest type is Simple Interest...
            compoundingPeriod = InterestCalculationConstants.SIMPLE_INTEREST;

        }
        return compoundingPeriod;
    }

    //__ To Get the Interest of each Type present in the Collection Object...
    public HashMap getIntDataMap() {
        return intDataMap;
    }

    //__ To Set the Interest of each Type in the Collection Object...
    public void setIntDataMap(HashMap intDataMap) {
        this.intDataMap = intDataMap;
    }

    public int getPENAL() {
        return PENAL;
    }

    public void setPENAL(int PENAL) {
        this.PENAL = PENAL;
    }
}
