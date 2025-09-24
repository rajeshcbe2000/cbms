/*
 * TdsCalc.java
 *
 * Created on February 8, 2005, 5:38 PM
 */
package com.see.truetransact.serverside.tds.tdscalc;

import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Calendar;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.TransRollbackException;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.batchprocess.task.TaskHeader;
import com.see.truetransact.serverside.common.idgenerate.IDGenerateDAO;
import com.see.truetransact.transferobject.tds.tdscalc.TdsCalcTO;
import com.see.truetransact.serverside.common.log.LogDAO;
import com.see.truetransact.transferobject.common.log.LogTO;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.serverside.batchprocess.task.operativeaccount.interest.DepositIntTask;
import com.see.truetransact.serverside.common.transaction.TransferTrans;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;

/**
 *
 * @author 152721
 */
public class TdsCalc {

    private static SqlMap sqlMap = null;
    //    private TdsCalcTO objTdsCalcTO;
    private String _branchCode = null;
    private Double tdsAmt;
    Date curDt;
    boolean pan = true;     //Added By Nithya
    boolean isTransaction = true;
    boolean fromDeductionScreen = false;
    HashMap depositTDSDetails = new HashMap();
    HashMap depositAccountDetails = new HashMap();


    /**
     * Creates a new instance of TdsCalc
     */
    public TdsCalc(String branchCode) throws ServiceLocatorException {
        _branchCode = branchCode;
        ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {
            TdsCalc objTdsCalc = new TdsCalc("Bran");
            //objTdsCalc.setInsertData("",  "OA", "ENZCHK");
            //            System.out.println(objTdsCalc.calculateTds("C0002014",null, null, DUtil.getDate(10,10,2005), 10000 ));
        } catch (Exception E) {
            E.printStackTrace();
            System.out.println("Error in main()");
        }

    }

    public void setInsertData(String custID, String prodType, String prodID, double baseAmt, double interest, String Acc_num, Date lstIntpaidDt, String acc_no) throws Exception {
        double taxAmt = 0;
        //        double baseAmt = 0;

        try {
            System.out.println("lstIntpaidDt      **************" + lstIntpaidDt);
            System.out.println("lstIntpaidDt      **************" + baseAmt);
            TdsCalcTO objTdsCalcTO = new TdsCalcTO();
            objTdsCalcTO.setTdsId(getTdsID());
            objTdsCalcTO.setTdsDt(ServerUtil.getCurrentDate(_branchCode));
            objTdsCalcTO.setCustId(custID);
            objTdsCalcTO.setProdType(prodType);
            objTdsCalcTO.setProdId(prodID);
            objTdsCalcTO.setAcc_num(Acc_num);
            objTdsCalcTO.setIntPaidDt(lstIntpaidDt);
            objTdsCalcTO.setTdsRecivedAcNo(acc_no);
            //__ if the
            if (baseAmt > 0) {
                objTdsCalcTO.setTdsBaseAmt(new Double(baseAmt));
                objTdsCalcTO.setTdsAmt(new Double(interest));

            } else {
//                final HashMap resultMap = calculateTds(custID, prodType,prodID, ServerUtil.getCurrentDate(_branchCode), 0);
//                objTdsCalcTO.setTdsBaseAmt(CommonUtil.convertObjToDouble(resultMap.get("baseAmt")));
//                objTdsCalcTO.setTdsAmt(CommonUtil.convertObjToDouble(resultMap.get("tdsAmount")));
            }


//             sqlMap.executeUpdate("insertTdsCalcTO", objTdsCalcTO);

            objTdsCalcTO.setIsSubmitted("N");



            if (objTdsCalcTO.getTdsAmt() != null && objTdsCalcTO.getTdsAmt().doubleValue() > 0) {
                sqlMap.executeUpdate("insertTdsCalcTO", objTdsCalcTO);
                tdsAmt = objTdsCalcTO.getTdsAmt();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new TransRollbackException(e);
        }
    }

    //__ To Calculate the TDS-ID...
    private String getTdsID() throws Exception {
        final IDGenerateDAO dao = new IDGenerateDAO();
        final HashMap where = new HashMap();
        where.put(CommonConstants.MAP_WHERE, "TDS_COLLECT_ID");
        return (String) (dao.executeQuery(where)).get(CommonConstants.DATA);
    }

    //__ To Calculate the TDS Amount...
    public HashMap calculateTds(String custID, String prodType, String prodID, Date intDate, double baseExtraAmt) throws Exception {
        System.out.println("calculateTds()");
        HashMap amountMap = new HashMap();
        double tdsAmount = 0, baseAmt = 0;
        double cutOff = 0, tdsPercentage = 0;
        double totintAmt = 0.0;
        String isCutOff = "";
        curDt = ServerUtil.getCurrentDate(_branchCode);
        //        HashMap tdsAccMap = new HashMap();
        //        HashMap lstTdsDtMap=new HashMap();
        //
        //        lstTdsDtMap.put("REMARKS","TDS");
        //        lstTdsDtMap.put("BRANCH_CODE",_branchCode);
        //        List tdsDtlst= sqlMap.executeQueryForList("getLastTDSApplDT",lstTdsDtMap) ;
        //        if(tdsDtlst!=null && tdsDtlst.size()>0){
        //            lstTdsDtMap=(HashMap)tdsDtlst.get(0);
        //            lstTdsDtMap=null;
        //        }else{
        //            throw new TTException("Last TDS Applied Date not set in Deposit_Provision");
        //        }
        //        tdsAccMap.put("CUST_ID",custID);
        //        List tdsAcclst=sqlMap.executeQueryForList("getAccountforTDS",tdsAccMap);
        //        if(tdsAcclst!=null && tdsAcclst.size()>0){
        //            for(int i=0;i<tdsAcclst.size();i++){
        //                tdsAccMap=new HashMap();
        //                tdsAccMap=(HashMap)tdsAcclst.get(0);
        //                tdsAcclst=null;
        //                totintAmt=totalIntrestCalculationforTds(tdsAccMap,lstTdsDtMap);
        //            }
        //        }
        HashMap dataMap = new HashMap();
        dataMap.put("INT_DATE", intDate);

        List list = (List) sqlMap.executeQueryForList("getTDSConfigData", dataMap);
        if (list.size() > 0) {
            HashMap resultMap = (HashMap) list.get(0);

            cutOff = Double.parseDouble(CommonUtil.convertObjToStr(resultMap.get("CUT_OF_AMT")));
            tdsPercentage = Double.parseDouble(CommonUtil.convertObjToStr(resultMap.get("TDS_PERCENTAGE")));
            isCutOff = CommonUtil.convertObjToStr(resultMap.get("INCLUDE_CUTOF"));
        } else {
            throw new Exception("TDS Config Date is Not set...");
        }

        System.out.println("cutOff: " + cutOff);
        System.out.println("tdsPercentage: " + tdsPercentage);
        System.out.println("isCutOff: " + isCutOff);

        //__ if the Customer Id is available...
        System.out.println("CUSTID : " + custID);
        if (custID.length() > 0) {
            dataMap.put("CUSTID", custID);
            System.out.println("dataMap: " + dataMap);
            List exceptionList = (List) sqlMap.executeQueryForList("getTDSExceptionData", dataMap);
            if (exceptionList.size() > 0) {
                System.out.println("Tax Exception...");
                tdsPercentage = 0;
            }
            //__ Else use Combination of ProdId and ProdType...
        } else {
            dataMap.put("PRODID", prodID);
            dataMap.put("PRODTYPE", prodType);
        }
        System.out.println("dataMap: " + dataMap);
        //        baseAmt = getDataList(dataMap);

        baseAmt += baseExtraAmt;

        //__ if Base Amount exceedsd the CutOff Amount...
        if (baseAmt >= cutOff) {
            //__ if CutOff is to be deducted from the Base Amount...
            if (isCutOff.equalsIgnoreCase("Y")) {
                baseAmt = baseAmt - cutOff;
            }
            tdsAmount = (baseAmt * tdsPercentage) / 100;
        }

        amountMap.put("baseAmt", String.valueOf(baseAmt));
        amountMap.put("tdsAmount", String.valueOf(tdsAmount));

        //__ To Update the IsTdsApplied in Deposit Interest...
        //        sqlMap.executeUpdate("updateDepositInterestTaxApplied", dataMap);
        System.out.println("amountMap: " + amountMap);

        return amountMap;
    }

    private ArrayList getDataList(HashMap dataMap) throws Exception {
        double tdsAmount = 0;
        List amountList = null;
        System.out.println("####getDataListdataMap" + dataMap);
        if (CommonUtil.convertObjToStr(dataMap.get("SAME_NO")).equals("NO")) {
            amountList = (List) sqlMap.executeQueryForList("getDepositInterestAmount", dataMap);
        } else {
            amountList = (List) sqlMap.executeQueryForList("getDepositInterestAmountSameNo", dataMap);
        }
        ArrayList resultLas = (ArrayList) amountList;
        //        if(amountList.size() > 0){
        //            HashMap resultMap = (HashMap)amountList.get(0);
        //            tdsAmount = Double.parseDouble(CommonUtil.convertObjToStr(resultMap.get("SUM")));
        //            String taxAmount =CommonUtil.convertObjToStr(resultMap.get("SUM"));
        //            System.out.println("tdsAmount: "+tdsAmount);
        //            //            if( ! taxAmount.equalsIgnoreCase("N"))
        //            //                tdsAmount = Double.parseDouble(CommonUtil.convertObjToStr(resultMap.get("SUM")));
        //            //                System.out.println("tdsAmount: "+tdsAmount);
        //        }
        if (resultLas != null && resultLas.size() > 0) {
            return resultLas;
        }
        return null;
    }

    /**
     * Getter for property tdsAmt.
     *
     * @return Value of property tdsAmt.
     */
    public java.lang.Double getTdsAmt() {
        return tdsAmt;
    }

    /**
     * Setter for property tdsAmt.
     *
     * @param tdsAmt New value of property tdsAmt.
     */
    public void setTdsAmt(java.lang.Double tdsAmt) {
        this.tdsAmt = tdsAmt;
    }

    public double totalIntrestCalculationforTds_old(HashMap tdsAccMap, HashMap lstTdsDtMap, HashMap closeMap) throws Exception {
        double totIntToAcc = 0.0;
        Date forDiffDt = null;
        Date tdsStartDt = null;
        Date tdsEndDt = null;
        Date lstTdsCalDt = null;

        
        //        Date lstTdsCalDt=DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(lstTdsDtMap.get("LAST_APPL_DT")));
        //        Date tdsUptoCalDt=DateUtil.addDays(lstTdsCalDt, 360);
        Date dpDt = (Date) tdsAccMap.get("DEPOSIT_DT");
        Date lstProvDt = (Date) tdsAccMap.get("LST_PROV_DT");
        Date lst_int_dt = (Date) tdsAccMap.get("LAST_INT_APPL_DT");
        Date dpMatDt = (Date) tdsAccMap.get("MATURITY_DT");
        Date dpCloseDt = (Date) tdsAccMap.get("CLOSE_DT");
        //        Date dpDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdsAccMap.get("DEPOSIT_DT")));
        //        Date lstProvDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdsAccMap.get("LST_PROV_DT")));
        //        Date dpMatDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdsAccMap.get("MATURITY_DT")));
        //        Date dpCloseDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdsAccMap.get("CLOSE_DT")));
        double roi = CommonUtil.convertObjToDouble(tdsAccMap.get("RATE_OF_INT")).doubleValue();
        double dpAmt = CommonUtil.convertObjToDouble(tdsAccMap.get("DEPOSIT_AMT")).doubleValue();
        double totIntCr = CommonUtil.convertObjToDouble(tdsAccMap.get("TOTAL_INT_CREDIT")).doubleValue();
        double totTdsCollAmt = CommonUtil.convertObjToDouble(tdsAccMap.get("TDS_AMT")).doubleValue();
        int intFreq = CommonUtil.convertObjToInt(tdsAccMap.get("INTPAY_FREQ"));
        String BEHAVES_LIKE = CommonUtil.convertObjToStr(tdsAccMap.get("BEHAVES_LIKE"));
        String Status = CommonUtil.convertObjToStr(tdsAccMap.get("ACCT_STATUS"));
        //        double outStanding=CommonUtil.convertObjToDouble(tdsAccMap.get("OUT_STANDING")).doubleValue();
        if (tdsAccMap.containsKey("OTHERDAO") && CommonUtil.convertObjToStr(tdsAccMap.get("OTHERDAO")).equals("OTHERDAO")) {
            //            tdsStartDt=DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdsAccMap.get("START1")));
            //            tdsEndDt=DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdsAccMap.get("END1")));
            tdsStartDt = (Date) tdsAccMap.get("START1");
            tdsEndDt = (Date) tdsAccMap.get("END1");
            lstTdsCalDt = (Date) tdsAccMap.get("LSTPROVDT");
            //            lstTdsCalDt=tdsStartDt;
        } else {
            //            lstTdsCalDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(lstTdsDtMap.get("LAST_APPL_DT")));
            lstTdsCalDt = (Date) lstTdsDtMap.get("LAST_APPL_DT");
            Date tdsUptoCalDt = DateUtil.addDays(lstTdsCalDt, 360);
            
            if (lstProvDt != null && DateUtil.dateDiff(lst_int_dt, lstProvDt) > 0) {
                dpDt = lstProvDt;
            } else {
                dpDt = lst_int_dt;
            }
            
            if (CommonUtil.convertObjToStr(Status).equals("CLOSED") && CommonUtil.convertObjToDouble(tdsAccMap.get("CURR_RATE_OF_INT")).doubleValue() > 0.0) {
                roi = CommonUtil.convertObjToDouble(tdsAccMap.get("CURR_RATE_OF_INT")).doubleValue();
            }
            if (DateUtil.dateDiff(lstTdsCalDt, dpDt) >= 0) {
                tdsStartDt = dpDt;
            } else {
                //            tdsStartDt=DateUtil.addDays(lstTdsCalDt,0);
                tdsStartDt = lstTdsCalDt;
            }
            
            if (closeMap != null) {
                tdsEndDt = ServerUtil.getCurrentDate(_branchCode);
            } else {
                if (DateUtil.dateDiff(tdsUptoCalDt, dpMatDt) > 0 && dpCloseDt == null && DateUtil.dateDiff(lstTdsCalDt, dpMatDt) > 0) {                   
                    tdsEndDt = tdsUptoCalDt;
                } else if (DateUtil.dateDiff(tdsUptoCalDt, dpMatDt) > 0 && dpCloseDt != null && DateUtil.dateDiff(lstTdsCalDt, dpCloseDt) > 0) {                    
                    tdsEndDt = dpCloseDt;
                } else {                    
                    tdsEndDt = dpMatDt;
                }
            }
            
            


            if (lstProvDt != null && DateUtil.dateDiff(lstProvDt, lst_int_dt) >= 0) {
                if (DateUtil.dateDiff(tdsStartDt, lst_int_dt) >= 0) {
                    tdsStartDt = lst_int_dt;
                } else {
                    tdsStartDt = tdsStartDt;
                }

            } else {
                if (lstProvDt != null && DateUtil.dateDiff(tdsStartDt, lstProvDt) >= 0) {
                    tdsStartDt = lstProvDt;
                } else {
                    tdsStartDt = tdsStartDt;
                }
            }
        }
       
        forDiffDt = tdsStartDt;
        Date tdsStartDt1 = tdsStartDt;

        
        if (BEHAVES_LIKE != null && BEHAVES_LIKE.length() > 0 && !CommonUtil.convertObjToStr(Status).equals("CLOSED") && DateUtil.dateDiff(tdsStartDt, tdsEndDt) > 0) {
            
            if (BEHAVES_LIKE.equals("FIXED")) {
                tdsAccMap.put("FREQ", tdsAccMap.get("INTPAY_FREQ"));
                //System.out.println("in side the FIXED ");
                ////                ( CommonUtil.convertObjToStr(tdsAccMap.get("DISCOUNTED_RATE")).equals("N") && intFreq==30)
                if (CommonUtil.convertObjToStr(tdsAccMap.get("DISCOUNTED_RATE")).equals("Y") && intFreq == 30) {
                    //System.out.println("in side the monthaly ");
                    forDiffDt = tdsStartDt;
                    //                    tdsStartDt=DateUtil.addDays(tdsStartDt,30);
                    
                    tdsStartDt = nextCaldate(dpDt, tdsStartDt, tdsAccMap);
                    
                    for (Date calStartDt = tdsStartDt; DateUtil.dateDiff(calStartDt, tdsEndDt) >= 0; calStartDt = nextCaldate(dpDt, calStartDt, tdsAccMap)) {
                       
                        double interestAmt = 0.0;
                        interestAmt = roi / 4 / (Math.pow((1 + (roi / 1200)), 2) + (1 + (roi / 1200)) + 1);
                        double calcAmt = dpAmt / 100;
                        interestAmt = interestAmt * calcAmt;
                        interestAmt = (double) getNearest((long) (interestAmt * 100), 100) / 100;
                        //System.out.println("");
                        totIntToAcc = totIntToAcc + interestAmt;
                        if (DateUtil.dateDiff(tdsStartDt1, lstTdsCalDt) == 0) {
                            GregorianCalendar gre = new GregorianCalendar();
                            Calendar calendar = new GregorianCalendar(calStartDt.getYear(), calStartDt.getMonth(), calStartDt.getDate());
                            int lastDate = calendar.getActualMaximum(calendar.DAY_OF_MONTH);
                            calStartDt.setDate(lastDate);
                        }
                        forDiffDt = calStartDt;
                        //System.out.println("interestAmt&&&&" + interestAmt + "totIntToAcc$$$$$$" + totIntToAcc + "calStartDt$$$$" + calStartDt);

                    }
                    if (DateUtil.dateDiff(forDiffDt, tdsEndDt) > 0) {
                        //System.out.println("in side the monthaly diffPeriod ");
                        long diffPeriod = DateUtil.dateDiff(forDiffDt, tdsEndDt);
                        double diffperiod1 = (double) diffPeriod;
                        diffperiod1 = diffperiod1 / 30;
                        double diffInt = 0.0;
                        diffInt = roi / 4 / (Math.pow((1 + (roi / 1200)), 2) + (1 + (roi / 1200)) + 1);
                        double calcAmt = dpAmt / 100;
                        diffInt = diffInt * calcAmt;
                        diffInt = diffInt * diffperiod1;
                        //                        diffInt=(diffPeriod*dpAmt*roi)/36500;

                        totIntToAcc = totIntToAcc + diffInt;
                        //System.out.println("diffPeriod&&&&" + diffPeriod + "diffInt$$$$$$" + diffInt + "totIntToAcc$$$$" + totIntToAcc);
                    }


                } else if (intFreq == 90 || intFreq == 180 || intFreq == 360 || (CommonUtil.convertObjToStr(tdsAccMap.get("DISCOUNTED_RATE")).equals("N") && intFreq == 30)) {
                    //System.out.println("in side the not monthaly ");
                    //System.out.println("intFreq&&&&&&&&&&&&&&&&&" + intFreq);
                    
                    forDiffDt = tdsStartDt;
                    tdsStartDt = nextCaldate(dpDt, tdsStartDt, tdsAccMap);
                   
                    for (Date calStartDt = tdsStartDt; DateUtil.dateDiff(calStartDt, tdsEndDt) >= 0; calStartDt = nextCaldate(dpDt, calStartDt, tdsAccMap)) {
                        //System.out.println("in side theCompleted");
                       
                        double interestAmt = 0.0;
                        double period = (double) intFreq / 30;
                        interestAmt = (dpAmt * roi * period) / 1200;
                        //                     double calcAmt = dpAmt /100;
                        //                     interestAmt = interestAmt*calcAmt;
                        totIntToAcc = totIntToAcc + interestAmt;
                        if (DateUtil.dateDiff(tdsStartDt1, lstTdsCalDt) == 0) {
                            Calendar calendar = new GregorianCalendar(calStartDt.getYear(), calStartDt.getMonth(), calStartDt.getDate());
                            int lastDate = calendar.getActualMaximum(calendar.DAY_OF_MONTH);
                            calStartDt.setDate(lastDate);
                        }
                        forDiffDt = calStartDt;
                        forDiffDt = calStartDt;
                        //System.out.println("interestAmt&&&&" + interestAmt + "totIntToAcc$$$$$$" + totIntToAcc + "calStartDt$$$$" + calStartDt);

                    }
                    if (DateUtil.dateDiff(forDiffDt, tdsEndDt) > 0) {
                       // System.out.println("in not Completed  theCompleted");
                        long diffPeriod = DateUtil.dateDiff(forDiffDt, tdsEndDt);
                        double diffInt = 0.0;
                        diffInt = (diffPeriod * dpAmt * roi) / 36500;
                        totIntToAcc = totIntToAcc + diffInt;
                      //  System.out.println("diffPeriod&&&&" + diffPeriod + "diffInt$$$$$$" + diffInt + "totIntToAcc$$$$" + totIntToAcc);
                    }

                } else {
                    if (DateUtil.dateDiff(tdsStartDt, tdsEndDt) > 0) {                      
                        long diffPeriod = DateUtil.dateDiff(tdsStartDt, tdsEndDt);                        
                        double diffInt = 0.0;
                        diffInt = (diffPeriod * dpAmt * roi) / 36500;
                        totIntToAcc = totIntToAcc + diffInt;
                    }
                }
            } else if (BEHAVES_LIKE.equals("CUMMULATIVE")) {
                double dpAmt1 = 0.0;
                dpAmt = dpAmt - totTdsCollAmt;
                dpAmt1 = dpAmt;
               // System.out.println("in side the CUMMULATIVE ");
                double interestAmt = 0.0;
                forDiffDt = tdsStartDt;
                intFreq = 90;
                tdsAccMap.put("FREQ", "90");
                if (tdsAccMap.containsKey("INT_COMP_FREQ") && CommonUtil.convertObjToInt(tdsAccMap.get("INT_COMP_FREQ")) > 0) {
                    intFreq = CommonUtil.convertObjToInt(tdsAccMap.get("INT_COMP_FREQ"));
                }

                tdsStartDt = nextCaldate(dpDt, tdsStartDt, tdsAccMap);
                if (tdsAccMap.containsKey("OTHERDAO") && CommonUtil.convertObjToStr(tdsAccMap.get("OTHERDAO")).equals("OTHERDAO")) {
                    dpAmt = dpAmt + totIntCr - totTdsCollAmt;
                } else {
                    dpAmt = dpAmt + totIntCr - totTdsCollAmt;
                };
                //                tdsStartDt=DateUtil.addDays(tdsStartDt,-1);
                for (Date calStartDt = tdsStartDt; DateUtil.dateDiff(calStartDt, tdsEndDt) >= 0; calStartDt = nextCaldate(dpDt, calStartDt, tdsAccMap)) {
                    //System.out.println("in side the CUMMULATIVE Completed ");

                    double period = (double) intFreq / 30;
                   // System.out.println("dpAmt    " + dpAmt);
                   // System.out.println("totIntCr    " + totIntCr);
                   // System.out.println("totTdsCollAmt    " + totTdsCollAmt);
                    dpAmt1 = 0.0;
                    dpAmt1 = dpAmt + totIntToAcc;
                   // System.out.println("dpAmt    " + dpAmt1);
                    //                    double amount = dpAmt*(Math.pow((1+roi/4.0),intFreq/12 * 4.0));
                    //                    double interest = amount-dpAmt;
                    double interest = 0.0;
                    //                    totIntToAcc=0.0;
                    interest = (dpAmt1 * period * roi) / 1200;
                    interest = interest * 10000;// for four decimal rounding of
                    interest = (double) getNearest((long) (interest * 100), 100) / 100;
                    interest = interest / 10000;
                    interestAmt = interest;
                    totIntToAcc = totIntToAcc + interestAmt;
                  //  System.out.println("totIntToAcc$$$$$$$$$$$$$$$$$$$$$" + totIntToAcc);
                    if (DateUtil.dateDiff(tdsStartDt1, lstTdsCalDt) == 0) {
                        Calendar calendar = new GregorianCalendar(calStartDt.getYear(), calStartDt.getMonth(), calStartDt.getDate());
                        int lastDate = calendar.getActualMaximum(calendar.DAY_OF_MONTH);
                        calStartDt.setDate(lastDate);
                    }
                    forDiffDt = calStartDt;
                    forDiffDt = calStartDt;
                    dpAmt1 = dpAmt + totIntToAcc;
                   // System.out.println("in side the CUMMULATIVE Differ calStartDt" + calStartDt + "diffPeriod&&&&&&&&&&&&&&&+" + totIntToAcc + "dpAmt1---------------->" + dpAmt1);

                }
               // System.out.println("dpAmt    " + dpAmt1);
               // System.out.println("totTdsCollAmt    " + totTdsCollAmt);
                if (DateUtil.dateDiff(forDiffDt, tdsEndDt) > 0) {
                  //  System.out.println("in side the CUMMULATIVE Differ ");
                    long diffPeriod = DateUtil.dateDiff(forDiffDt, tdsEndDt);
                    double diffInt = 0.0;
                    dpAmt = dpAmt + totIntToAcc - totTdsCollAmt;
                    diffInt = (diffPeriod * dpAmt1 * roi) / 36500;
                   // System.out.println("in side the CUMMULATIVE Differ diffInt" + diffInt + "diffPeriod&&&&&&&&&&&&&&&+" + diffPeriod);
                    totIntToAcc = totIntToAcc + diffInt;
                }


            }
        }
        HashMap dataMap = new HashMap();
        if (!CommonUtil.convertObjToStr(tdsAccMap.get("OTHERDAO")).equals("OTHERDAO")) {
            //
            //        dataMap.put("ACC_NUM",CommonUtil.convertObjToStr(tdsAccMap.get("DEPOSIT_NO")));
            //        dataMap.put("LST_TDS_APPL_DT",(Date)lstTdsDtMap.get("LAST_APPL_DT"));
            ////        dataMap.put("LST_TDS_APPL_DT",DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(lstTdsDtMap.get("LAST_APPL_DT"))));
            //        dataMap.put("UPTO_TDS_APPL_DT",DateUtil.addDays((Date)lstTdsDtMap.get("LAST_APPL_DT"),360));
            ////        dataMap.put("UPTO_TDS_APPL_DT",DateUtil.addDays(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(lstTdsDtMap.get("LAST_APPL_DT"))),360));
            //        List renewDpLst=sqlMap.executeQueryForList("getDepositRenwal",dataMap);
            //        if(renewDpLst!=null && renewDpLst.size()>0){
            //            System.out.println("In side Renewal ");
            //            double renIntAmt=0.0;
            //            dataMap=new HashMap();
            //            dataMap=(HashMap)renewDpLst.get(0);
            //            long minPeriod=CommonUtil.convertObjToLong(dataMap.get("MIN_DAYS_BKDT_DEPOSITS"));
            //            long diff=DateUtil.dateDiff(dpMatDt, dpCloseDt);
            //            if(diff>minPeriod){
            //                HashMap sbInterestMap = new HashMap();
            //                sbInterestMap.put("PRODUCT_TYPE","OA");
            //                HashMap sbProdIdMap =new HashMap();
            //                sbProdIdMap.put("BEHAVIOR","SB");
            //                List lstProd = sqlMap.executeQueryForList("getProdIdForOperative", sbProdIdMap);
            //                if(lstProd.size()>0){
            //                    HashMap prodMap = new HashMap();
            //                    prodMap = (HashMap)lstProd.get(0);
            //                    sbInterestMap.put("PROD_ID",prodMap.get("PROD_ID"));
            //                }
            //                sbInterestMap.put("CATEGORY_ID",CommonUtil.convertObjToStr("CATEGORY"));
            //                sbInterestMap.put("DEPOSIT_DT", dpMatDt);
            //                sbInterestMap.put("AMOUNT", new Double(dpAmt));
            //                sbInterestMap.put("PERIOD", new Long(diff));
            //                double rateOfInt ;
            //                List lstInt = (List)sqlMap.executeQueryForList("icm.getInterestRates", sbInterestMap);
            //                if(lstInt.size()>0) {
            //                    HashMap sbRateOfInt = (HashMap)lstInt.get(0);
            //                    rateOfInt = CommonUtil.convertObjToDouble(sbRateOfInt.get("ROI")).doubleValue();
            //                    renIntAmt = (dpAmt * rateOfInt * diff) /(36500);
            //                }
            //                //                    renIntAmt = (dpAmt * rateOfInt * diff) /(36500);
            //                //                    totIntToAcc=totIntToAcc+totIntToAcc;
            //            }
            //            totIntToAcc=totIntToAcc+totIntToAcc;

            //        }
            double paidInt = 0.0;
            lstTdsCalDt = (Date) lstTdsDtMap.get("LAST_APPL_DT");
            Date tdsUptoCalDt = DateUtil.addDays(lstTdsCalDt, 360);
            dataMap.put("LST_TDS_APPL_DT", lstTdsCalDt);
            dataMap.put("UPTO_TDS_APPL_DT", tdsUptoCalDt);
            dataMap.put("DEPOSIT_NO", CommonUtil.convertObjToStr(tdsAccMap.get("ACT_NUM1")));
            dataMap.put("CURR_DT",curDt.clone());

            List list = (List) sqlMap.executeQueryForList("getTotalIntpaidToAccount", dataMap);
            if (list != null & list.size() > 0) {
                dataMap = (HashMap) list.get(0);
                paidInt = CommonUtil.convertObjToDouble(dataMap.get("INT_PAID_AMT")).doubleValue();
               // System.out.println("paidInt--------------------" + paidInt);
                totIntToAcc = totIntToAcc + paidInt;
            }
            list = null;

        }
       // System.out.println("totInt Amt for Account     " + dataMap.get("ACC_NUM") + "       INT AMT     =          " + totIntToAcc);
        dataMap = null;
        lstTdsDtMap = null;
        tdsAccMap = null;
        totIntToAcc = (double) getNearest((long) (totIntToAcc * 100), 100) / 100;
        return totIntToAcc;
    }
    
    
    public HashMap tdsCalcforInt(String custID, double intAmtForTds, String Acc_num, String prodType, String prodID, HashMap closeMap) throws Exception {
        double tdsAmt = 0.0;
        double totTdsforold = 0.0;
        double prvPreTdsAmt = 0.0;
        boolean isError = true;
        double actualIntAmt = intAmtForTds;
        curDt = ServerUtil.getCurrentDate(_branchCode); 
        HashMap amountMap = new HashMap();
        double tdsAmount = 0, baseAmt = 0;
        double cutOff = 0, tdsPercentage = 0;
        double totintAmt = 0.0;
        String isCutOff = "";
        fromDeductionScreen = false;
        depositTDSDetails = new HashMap();
        HashMap tdsAccMap = new HashMap();
        HashMap lstTdsDtMap = new HashMap();
        depositAccountDetails = new HashMap();
        HashMap singleAccountDetails = new HashMap();
        HashMap depositClosureMap = new HashMap();
        HashMap depMap = new HashMap();

        //To Check Any Provision Amount is available or Not
        if (Acc_num.length() > 0) {     //After Discussed with Raavi/Abi/Rajesh/Srinath Sir. Need to calculate TDS for Provision Amount.
            if (Acc_num.lastIndexOf("_") != -1) {
                Acc_num = Acc_num.substring(0, Acc_num.lastIndexOf("_"));
            }
            depMap.put("DEPOSIT_NO", Acc_num);
            List depProvList = sqlMap.executeQueryForList("getDepositProvisionDetails", depMap);
            if (depProvList != null && depProvList.size() > 0) {
                depMap = (HashMap) depProvList.get(0);
                if (CommonUtil.convertObjToDouble(depMap.get("PROV_AMT")) > 0) {
                    intAmtForTds = intAmtForTds - CommonUtil.convertObjToDouble(depMap.get("PROV_AMT"));                    
                    if (intAmtForTds <= 0) {
                        return amountMap;
                    }
                }
            }
        }
        lstTdsDtMap.put("REMARKS", "TDS");
        lstTdsDtMap.put("BRANCH_CODE", _branchCode);
        List tdsDtlst = sqlMap.executeQueryForList("getLastTDSApplDT", lstTdsDtMap);
        if (tdsDtlst != null && tdsDtlst.size() > 0) {
            lstTdsDtMap = (HashMap) tdsDtlst.get(0);
        } else {
            throw new TTException("Last TDS Applied Date not set in Deposit_Provision");
        }
        tdsAccMap.put("CUST_ID", custID);
        tdsAccMap.put("LST_TDS_APPL_DT", (Date) lstTdsDtMap.get("LAST_APPL_DT"));

        if (closeMap != null) {
            if (closeMap.containsKey("DEDUCTION_SCREEN")) {
                fromDeductionScreen = true;
                depositTDSDetails.put("CUST_ID", closeMap.get("CUSTID"));
                singleAccountDetails.put("DEPOSIT_NO", Acc_num);
                singleAccountDetails.put("INTEREST", intAmtForTds);
            }
            depositClosureMap = closeMap;
            closeMap = null;            
        }        
        if (closeMap == null) {
            List tdsAcclst = sqlMap.executeQueryForList("getAccountforTDS", tdsAccMap);
            if (tdsAcclst != null && tdsAcclst.size() > 0) {
                for (int i = 0; i < tdsAcclst.size(); i++) {
                    tdsAccMap = new HashMap();
                    tdsAccMap = (HashMap) tdsAcclst.get(i);                    
                    totintAmt = totintAmt + totalIntrestCalculationforTds(tdsAccMap, lstTdsDtMap, depositClosureMap);                    
                }
            }
            /*else {
             isError = false;
             }
             tdsAcclst = null;*/
        } else if (closeMap != null) {
            tdsAccMap.put("CLOSEDACC", CommonUtil.convertObjToStr(closeMap.get("DEPOSIT_NO")));
            List tdsAcclst = sqlMap.executeQueryForList("getAccountforTDS", tdsAccMap);
            if (tdsAcclst != null && tdsAcclst.size() > 0) {
                for (int i = 0; i < tdsAcclst.size(); i++) {
                    tdsAccMap = new HashMap();
                    tdsAccMap = (HashMap) tdsAcclst.get(i);                   
                    totintAmt = totintAmt + totalIntrestCalculationforTds(tdsAccMap, lstTdsDtMap, null);                    
                }
            } else {
                isError = false;
            }
            tdsAccMap = new HashMap();
            tdsAccMap.put("CUST_ID", custID);
            tdsAccMap.put("CLOSEDACC", CommonUtil.convertObjToStr(closeMap.get("DEPOSIT_NO")));
            tdsAccMap.put("LST_TDS_APPL_DT", (Date) lstTdsDtMap.get("LAST_APPL_DT"));
            List tdsCloseAcclst = sqlMap.executeQueryForList("getAccountclosedforTDS", tdsAccMap);
            if (tdsCloseAcclst != null && tdsCloseAcclst.size() > 0) {
                tdsAccMap = (HashMap) tdsCloseAcclst.get(0);
                tdsAccMap.put("CLOSE_DT", ServerUtil.getCurrentDate(_branchCode));
                tdsAccMap.put("RATE_OF_INT", CommonUtil.convertObjToDouble(closeMap.get("RATE_OF_INT")));
                totintAmt = totintAmt + totalIntrestCalculationforTds(tdsAccMap, lstTdsDtMap, closeMap);
            } else {
                isError = false;
            }
            tdsCloseAcclst = null;
            tdsAcclst = null;
        } else {
            return amountMap;
        }
        if (isError) {
            HashMap dataMap = new HashMap();
            Date intDate = ServerUtil.getCurrentDate(_branchCode);
            dataMap.put("INT_DATE", intDate);
            if (CommonUtil.convertObjToStr(tdsAccMap.get("CUST_TYPE")).equals("INDIVIDUAL")) {
                dataMap.put("CUST_TYPE", CommonUtil.convertObjToStr(tdsAccMap.get("CUST_TYPE")));
            } else {
                dataMap.put("CUST_TYPE", "CORPORATE");
            }
            List list = (List) sqlMap.executeQueryForList("getTDSConfigData", dataMap);
            if (list.size() > 0) {
                HashMap resultMap = (HashMap) list.get(0);
                cutOff = Double.parseDouble(CommonUtil.convertObjToStr(resultMap.get("CUT_OF_AMT")));
                if (pan) {      //Added By Suresh       With ot Without Pan Checking.
                    tdsPercentage = Double.parseDouble(CommonUtil.convertObjToStr(resultMap.get("TDS_PERCENTAGE")));
                } else {
                    tdsPercentage = Double.parseDouble(CommonUtil.convertObjToStr(resultMap.get("PAN_PERCENTAGE")));
                }
                isCutOff = CommonUtil.convertObjToStr(resultMap.get("INCLUDE_CUTOF"));
                String Cr_ac_hd_id = CommonUtil.convertObjToStr(resultMap.get("TDS_CR_AC_HD_ID"));
                amountMap.put("TDSCrACHdId", Cr_ac_hd_id);
            } else {
                throw new TTException("TDS Parameter is not set");
            }
            dataMap = null;
            
            // Added by nithya on 14-05-2020 for KD-1090
             // Calculating int amount for TDS for particular Account number
            
            totintAmt = totintAmt + intAmtForTds;
            String depNo = "";            
            HashMap intAmtMap = new HashMap();
            intAmtMap.put("DEPOSIT_NO", Acc_num +"_1");
            intAmtMap.put("CURR_DT",curDt.clone());            
            List intAmLlist = (List) sqlMap.executeQueryForList("getTotalIntpaidToAccount", intAmtMap);
            if (intAmLlist != null & intAmLlist.size() > 0) {
                intAmtMap = (HashMap) intAmLlist.get(0);
                intAmtForTds = intAmtForTds + CommonUtil.convertObjToDouble(intAmtMap.get("INT_PAID_AMT")).doubleValue(); 
            }
            //End
           
            
            if (totintAmt >= intAmtForTds) {
                totintAmt = totintAmt;
            } else {
                totintAmt = intAmtForTds;
            }
            if (totintAmt >= cutOff) {      //Checking Customer All Accounts Fin_Year Total_interest is exceeding TDS_CutOff Amount.
                
                dataMap = new HashMap();
                ArrayList backtdsLst = new ArrayList();
                dataMap.put("CUST_ID", custID);
                dataMap.put("CUSTID", custID);
                dataMap.put("LST_TDS_APPL_DT", (Date) lstTdsDtMap.get("LAST_APPL_DT"));
                dataMap.put("UPTO_TDS_APPL_DT", DateUtil.addDays((Date) lstTdsDtMap.get("LAST_APPL_DT"), 360));
                if (fromDeductionScreen) {
                    depositTDSDetails.put("TDS_ROI", tdsPercentage);
                    depositTDSDetails.put("TOTAL_INTEREST", totintAmt);
                    depositTDSDetails.put("CUT_OFF_AMOUNT", cutOff);
                }
                totintAmt = intAmtForTds;
                intAmtForTds = intAmtForTds * tdsPercentage / 100;
                intAmtForTds = (double) getNearest((long) (intAmtForTds * 100), 100) / 100;
                if (intAmtForTds < 1) {
                    intAmtForTds = 1;
                }
                prvPreTdsAmt = intAmtForTds + totTdsforold;
                double TDSDEDUCTEDTOBASEACCOUNT = intAmtForTds;
                prvPreTdsAmt = (double) getNearest((long) (prvPreTdsAmt * 100), 100) / 100;
                Date intPaidDate = ServerUtil.getCurrentDate(_branchCode);
                dataMap.put("ACT_NUM", Acc_num);
                dataMap.put("SAME_NO", "YES");
                if (closeMap == null) {
                    HashMap debitAcNoGiven = new HashMap();
                    //debitAcNoGiven = tdsDebitAcNumgiven(dataMap, Acc_num, lstTdsDtMap, tdsPercentage, intAmtForTds, custID, prodType, prodID, totintAmt, intPaidDate, actualIntAmt, amountMap);
                    if (debitAcNoGiven != null && debitAcNoGiven.containsKey("DEBIT_ACT_NUM") && CommonUtil.convertObjToStr(debitAcNoGiven.get("DEBIT_ACT_NUM")).length() > 0) {
                        amountMap.put("TDSDRAMT", new Double(0));
                        amountMap.put("TDSDEDUCTEDTOBASEACCOUNT", new Double(TDSDEDUCTEDTOBASEACCOUNT));
                        amountMap.put("DEBIT_ACT_NUM", CommonUtil.convertObjToStr(debitAcNoGiven.get("DEBIT_ACT_NUM")));
                    } else {
                        setInsertData(custID, prodType, prodID, totintAmt, intAmtForTds, Acc_num, intPaidDate, Acc_num);  //Insert TDS Collected  
                        if (fromDeductionScreen) {
                            singleAccountDetails.put("TDS_AMOUNT", prvPreTdsAmt);
                            depositAccountDetails.put(Acc_num, singleAccountDetails);
                        }                      
                        //prvPreTdsAmt = calculateTDSforAlreadyPaidInterest(dataMap, actualIntAmt, tdsPercentage, custID, prvPreTdsAmt, Acc_num);                        
                        
                        // Deducting TDS amount deducted during financial year  
                        // Added by nithya on 14-05-2020 for KD-1090                        
                                                                
                        HashMap tdsPaidMap = new HashMap();
                        tdsPaidMap.put("DEPOSIT_NO", Acc_num + "_1");
                        tdsPaidMap.put("CURR_DT", curDt.clone());
                        List tdsPaidList = (List) sqlMap.executeQueryForList("getTotalTDSDeductedFromAccount", tdsPaidMap);
                        if (tdsPaidList != null & tdsPaidList.size() > 0) {
                            tdsPaidMap = (HashMap) tdsPaidList.get(0);
                            prvPreTdsAmt = prvPreTdsAmt - CommonUtil.convertObjToDouble(tdsPaidMap.get("TDS_AMT")).doubleValue();                           
                        }                 
                        // End                       
                        
                        amountMap.put("TDSDRAMT", new Double(prvPreTdsAmt));
                        amountMap.put("TDSDEDUCTEDTOBASEACCOUNT", new Double(prvPreTdsAmt));
                        amountMap.put("DEBIT_ACT_NUM", Acc_num);
                        if (fromDeductionScreen) {
                            amountMap.put("TDS_DEDUCTION_DETAILS", depositTDSDetails);
                            amountMap.put("TDS_ACCOUNT_DETAILS", depositAccountDetails);
                        }
                    }
                }
                
                lstTdsDtMap = null;
                dataMap = null;
                depositClosureMap = null;
                return amountMap;
            } else {
                System.out.println("########## TDS NOT ELIGIBLE : " + Acc_num);
            }
        }
        depositClosureMap = null;
        amountMap = new HashMap();
        lstTdsDtMap = null;
        return amountMap;
    }
    
    private double calculateTDSforAlreadyPaidInterest(HashMap dataMap, double actualIntAmt, double tdsPercentage, String custID, double currentTdsAmt, String Acc_num) throws Exception {
        HashMap OldDepIntMap = new HashMap();
        double oldPaidInt = 0.0;
        double oldIntTDS = 0.0;
        double oldTotalIntTDS = 0.0;
        double paidTDSInt = 0.0;
        double actualIntAmt1 = actualIntAmt;
        HashMap singleAccountDetails = new HashMap();
        HashMap oldSingleActDetails = new HashMap();
        String oldPaidIntDeposit_Num = "";
        List OldDepIntList = (List) sqlMap.executeQueryForList("getOldDepositInterestAmount", dataMap);    //DEPOSIT_INTEREST table taking data based on Cust_ID
        if (OldDepIntList != null && OldDepIntList.size() > 0) {
            for (int i = 0; i < OldDepIntList.size(); i++) {
                OldDepIntMap = new HashMap();
                singleAccountDetails = new HashMap();
                oldIntTDS = 0.0;
                OldDepIntMap = (HashMap) OldDepIntList.get(i);
                oldPaidInt = CommonUtil.convertObjToDouble(OldDepIntMap.get("INT_AMT")).doubleValue();
                paidTDSInt = CommonUtil.convertObjToDouble(OldDepIntMap.get("TDS_AMT")).doubleValue();
                oldPaidIntDeposit_Num = CommonUtil.convertObjToStr(OldDepIntMap.get("ACT_NUM"));               
                if (oldPaidInt > 0 && actualIntAmt1 > 0) {
                    oldIntTDS = oldPaidInt * tdsPercentage / 100;
                    if (paidTDSInt > 0) {
                        oldIntTDS -= paidTDSInt;
                        if (oldIntTDS < 0) {
                            oldIntTDS = 0.0;
                        }
                    }
                    if (oldIntTDS > 0) {
                        oldIntTDS = (double) getNearest((long) (oldIntTDS * 100), 100) / 100;                        
                        if (!isTransaction) {
                            if (actualIntAmt1 > oldIntTDS + currentTdsAmt) {
                                oldTotalIntTDS += oldIntTDS;
                                actualIntAmt1 = actualIntAmt1 - oldIntTDS;
                                if (fromDeductionScreen) {       // Done By Suresh R     10-Feb-2016
                                    if (!depositAccountDetails.containsKey(oldPaidIntDeposit_Num)) {
                                        singleAccountDetails.put("DEPOSIT_NO", oldPaidIntDeposit_Num);
                                        singleAccountDetails.put("INTEREST", oldPaidInt);
                                        singleAccountDetails.put("TDS_AMOUNT", oldIntTDS);
                                        depositAccountDetails.put(oldPaidIntDeposit_Num, singleAccountDetails);
                                    } else {
                                        oldSingleActDetails = (HashMap) depositAccountDetails.get(oldPaidIntDeposit_Num);
                                        singleAccountDetails.put("DEPOSIT_NO", oldPaidIntDeposit_Num);
                                        singleAccountDetails.put("INTEREST", oldPaidInt + CommonUtil.convertObjToDouble(oldSingleActDetails.get("INTEREST")));
                                        singleAccountDetails.put("TDS_AMOUNT", oldIntTDS + CommonUtil.convertObjToDouble(oldSingleActDetails.get("TDS_AMOUNT")));
                                        depositAccountDetails.put(oldPaidIntDeposit_Num, singleAccountDetails);
                                    }
                                }
                            } else {
                                actualIntAmt1 = 0.0;
                            }
                        }
                        if (isTransaction) {        //Update TDS Applied for Old Deposit Interest.
                            dataMap = new HashMap();
                            if (actualIntAmt1 > oldIntTDS + currentTdsAmt) {
                                oldTotalIntTDS += oldIntTDS;
                                actualIntAmt1 = actualIntAmt1 - oldIntTDS;
                                dataMap.put("IS_TDS_APPLIED", "Y");
                                dataMap.put("TDS_AMT", new Double(oldIntTDS + paidTDSInt));
                            } else {
                                actualIntAmt1 = 0.0;
                                dataMap.put("IS_TDS_APPLIED", "N");
                                dataMap.put("TDS_AMT", new Double(paidTDSInt));
                            }
                            dataMap.put("CUST_ID", custID);
                            dataMap.put("TOTAL_TDS_AMOUNT", new Double(oldIntTDS + paidTDSInt));
                            dataMap.put("ACT_NUM", oldPaidIntDeposit_Num);
                            dataMap.put("INT_PAID_DATE", (Date) OldDepIntMap.get("INT_PAID_DATE"));                            
                            sqlMap.executeUpdate("updateTdSAppliedForOldPaidInterest", dataMap);          //Update TDS Applied
                        }
                    }
                }
            }            
            if (oldTotalIntTDS > 0) {
                currentTdsAmt += oldTotalIntTDS;
            }
        }
        return currentTdsAmt;
    }

    public HashMap tdsCalcforInt_1(String custID, double intAmtForTds, String Acc_num, String prodType, String prodID, HashMap closeMap) throws Exception {
        double tdsAmt = 0.0;
        double totTdsforold = 0.0;
        double prvPreTdsAmt = 0.0;
        boolean isError = true;
        double actualIntAmt = intAmtForTds;

        System.out.println("calculateTds()" + intAmtForTds);
        System.out.println("closeMap()" + closeMap);
        HashMap amountMap = new HashMap();
        double tdsAmount = 0, baseAmt = 0;
        double cutOff = 0, tdsPercentage = 0;
        double totintAmt = 0.0;
        String isCutOff = "";
        HashMap tdsAccMap = new HashMap();
        HashMap lstTdsDtMap = new HashMap();

        lstTdsDtMap.put("REMARKS", "TDS");
        lstTdsDtMap.put("BRANCH_CODE", _branchCode);
        List tdsDtlst = sqlMap.executeQueryForList("getLastTDSApplDT", lstTdsDtMap);
        if (tdsDtlst != null && tdsDtlst.size() > 0) {
            lstTdsDtMap = (HashMap) tdsDtlst.get(0);

        } else {
            throw new TTException("Last TDS Applied Date not set in Deposit_Provision");
        }
        tdsAccMap.put("CUST_ID", custID);
        //        tdsAccMap.put("LST_TDS_APPL_DT",DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(lstTdsDtMap.get("LAST_APPL_DT"))));
        tdsAccMap.put("LST_TDS_APPL_DT", (Date) lstTdsDtMap.get("LAST_APPL_DT"));
        if (closeMap == null) {
            List tdsAcclst = sqlMap.executeQueryForList("getAccountforTDS", tdsAccMap);
            if (tdsAcclst != null && tdsAcclst.size() > 0) {
                for (int i = 0; i < tdsAcclst.size(); i++) {
                    tdsAccMap = new HashMap();
                    tdsAccMap = (HashMap) tdsAcclst.get(i);
                    System.out.println("deposit_no----------------" + CommonUtil.convertObjToStr(tdsAccMap.get("DEPOSIT_NO")) + "\n"
                            + "Interest_Amount_before---------------------->" + totintAmt);
                    totintAmt = totintAmt + totalIntrestCalculationforTds(tdsAccMap, lstTdsDtMap, null);
                    System.out.println("totintAmt&&&&&&&&&&&&& after      " + totintAmt);
                }
            } else {
                isError = false;
            }

            tdsAcclst = null;
        } else if (closeMap != null) {
            tdsAccMap.put("CLOSEDACC", CommonUtil.convertObjToStr(closeMap.get("DEPOSIT_NO")));
            List tdsAcclst = sqlMap.executeQueryForList("getAccountforTDS", tdsAccMap);
            if (tdsAcclst != null && tdsAcclst.size() > 0) {
                for (int i = 0; i < tdsAcclst.size(); i++) {
                    tdsAccMap = new HashMap();
                    tdsAccMap = (HashMap) tdsAcclst.get(i);

                    totintAmt = totintAmt + totalIntrestCalculationforTds(tdsAccMap, lstTdsDtMap, null);
                    System.out.println("totintAmt&&&&&&&&&&&&&   in close other   " + totintAmt);
                }
            } else {
                isError = false;
            }
            //                tdsAcclst=null;
            tdsAccMap = new HashMap();
            tdsAccMap.put("CUST_ID", custID);
            tdsAccMap.put("CLOSEDACC", CommonUtil.convertObjToStr(closeMap.get("DEPOSIT_NO")));
            tdsAccMap.put("LST_TDS_APPL_DT", (Date) lstTdsDtMap.get("LAST_APPL_DT"));
            //            tdsAccMap.put("LST_TDS_APPL_DT",DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(lstTdsDtMap.get("LAST_APPL_DT"))));
            List tdsCloseAcclst = sqlMap.executeQueryForList("getAccountclosedforTDS", tdsAccMap);

            if (tdsCloseAcclst != null && tdsCloseAcclst.size() > 0) {
                //                    for(int i=0;i<tdsAcclst.size();i++){
                //                        tdsAccMap=new HashMap();
                tdsAccMap = (HashMap) tdsCloseAcclst.get(0);
                tdsAccMap.put("CLOSE_DT", ServerUtil.getCurrentDate(_branchCode));
                tdsAccMap.put("RATE_OF_INT", CommonUtil.convertObjToDouble(closeMap.get("RATE_OF_INT")));
                totintAmt = totintAmt + totalIntrestCalculationforTds(tdsAccMap, lstTdsDtMap, closeMap);
                System.out.println("totintAmt in close &&&&&&&&&&&&&     " + totintAmt);
                //                    }
            } else {
                isError = false;
            }
            tdsCloseAcclst = null;
            tdsAcclst = null;
        } else {
            //            amountMap=null;
            return amountMap;
        }

        System.out.println("totintAmt after every thing &&&&&&&&&&&&&     " + totintAmt);
        if (isError) {
            HashMap dataMap = new HashMap();
            Date intDate = ServerUtil.getCurrentDate(_branchCode);
            dataMap.put("INT_DATE", intDate);
            if (tdsAccMap.get("CUST_TYPE").equals("INDIVIDUAL")) {
                dataMap.put("CUST_TYPE", CommonUtil.convertObjToStr(tdsAccMap.get("CUST_TYPE")));
            } else {
                dataMap.put("CUST_TYPE", "CORPORATE");
            }

            List list = (List) sqlMap.executeQueryForList("getTDSConfigData", dataMap);
            if (list.size() > 0) {
                HashMap resultMap = (HashMap) list.get(0);

                cutOff = Double.parseDouble(CommonUtil.convertObjToStr(resultMap.get("CUT_OF_AMT")));
                tdsPercentage = Double.parseDouble(CommonUtil.convertObjToStr(resultMap.get("TDS_PERCENTAGE")));
                isCutOff = CommonUtil.convertObjToStr(resultMap.get("INCLUDE_CUTOF"));
                String Cr_ac_hd_id = CommonUtil.convertObjToStr(resultMap.get("TDS_CR_AC_HD_ID"));
                amountMap.put("TDSCrACHdId", Cr_ac_hd_id);
                //            resultMap=null;
            } else {
                throw new TTException("TDS Parameter is not set");
            }
            dataMap = null;
            if (totintAmt >= intAmtForTds) {
                totintAmt = totintAmt;
            } else {
                totintAmt = intAmtForTds;
            }
            if (totintAmt >= cutOff) {
                dataMap = new HashMap();
                ArrayList backtdsLst = new ArrayList();
                dataMap.put("CUST_ID", custID);
                dataMap.put("CUSTID", custID);
                //                dataMap.put("LST_TDS_APPL_DT",DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(lstTdsDtMap.get("LAST_APPL_DT"))));
                //                dataMap.put("UPTO_TDS_APPL_DT",DateUtil.addDays(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(lstTdsDtMap.get("LAST_APPL_DT"))),360));
                dataMap.put("LST_TDS_APPL_DT", (Date) lstTdsDtMap.get("LAST_APPL_DT"));
                dataMap.put("UPTO_TDS_APPL_DT", DateUtil.addDays((Date) lstTdsDtMap.get("LAST_APPL_DT"), 360));
                System.out.println("intAmtForTds Before Adding without Paid Tds" + intAmtForTds);


                System.out.println("intAmtForTds After Adding without Paid Tds" + intAmtForTds);
                totintAmt = intAmtForTds;
                intAmtForTds = intAmtForTds * tdsPercentage / 100;
                intAmtForTds = (double) getNearest((long) (intAmtForTds * 100), 100) / 100;
                if (intAmtForTds < 1) {
                    intAmtForTds = 1;
                }
                System.out.println("intAmtForTds After Adding without Paid Tds" + intAmtForTds);
                prvPreTdsAmt = intAmtForTds + totTdsforold;
                double TDSDEDUCTEDTOBASEACCOUNT = intAmtForTds;
                prvPreTdsAmt = (double) getNearest((long) (prvPreTdsAmt * 100), 100) / 100;
                Date intPaidDate = ServerUtil.getCurrentDate(_branchCode);
                dataMap.put("ACT_NUM", Acc_num);
                dataMap.put("SAME_NO", "YES");
                if (closeMap == null) {
                    HashMap debitAcNoGiven = new HashMap();
                    debitAcNoGiven = tdsDebitAcNumgiven(dataMap, Acc_num, lstTdsDtMap, tdsPercentage, intAmtForTds, custID, prodType, prodID, totintAmt, intPaidDate, actualIntAmt, amountMap);
                    if (debitAcNoGiven != null && debitAcNoGiven.containsKey("DEBIT_ACT_NUM") && CommonUtil.convertObjToStr(debitAcNoGiven.get("DEBIT_ACT_NUM")).length() > 0) {
                        amountMap.put("TDSDRAMT", new Double(0));
                        amountMap.put("TDSDEDUCTEDTOBASEACCOUNT", new Double(TDSDEDUCTEDTOBASEACCOUNT));
                        amountMap.put("DEBIT_ACT_NUM", CommonUtil.convertObjToStr(debitAcNoGiven.get("DEBIT_ACT_NUM")));
                    } else {
                        setInsertData(custID, prodType, prodID, totintAmt, intAmtForTds, Acc_num, intPaidDate, Acc_num);
                        backtdsLst = getDataList(dataMap);
                        totTdsforold = prvPreTdsAmt;
                        prvPreTdsAmt = forAlreadyInterestPaidTdsDe(backtdsLst, actualIntAmt, tdsPercentage, custID, prvPreTdsAmt, Acc_num);
                        dataMap.put("SAME_NO", "NO");
                        backtdsLst = getDataList(dataMap);
                        prvPreTdsAmt = forAlreadyInterestPaidTdsDe(backtdsLst, actualIntAmt, tdsPercentage, custID, prvPreTdsAmt, Acc_num);
                        //                if(backtdsLst!=null && backtdsLst.size()>0){
                        //                    for(int i=0;i<backtdsLst.size();i++){
                        //                        HashMap prvIntMap= new HashMap();
                        //
                        //                        double  totTdsforoldBfore=0.0;
                        //                        prvIntMap=(HashMap)backtdsLst.get(i);
                        //                        double prvInt=CommonUtil.convertObjToDouble(prvIntMap.get("INT_AMT")).doubleValue();
                        //                        double tdsAmtPrv=prvInt*tdsPercentage/100;
                        //                        tdsAmtPrv = (double)getNearest((long)(tdsAmtPrv *100),100)/100;
                        //                        totTdsforoldBfore=totTdsforold;
                        //                        //                        totTdsforold=totTdsforold+tdsAmtPrv;
                        //                        System.out.println("totTdsforoldBfore---------->"+totTdsforoldBfore);
                        //                        System.out.println("totTdsforold---------->"+totTdsforold);
                        //                        if(totTdsforoldBfore< actualIntAmt){
                        //                            totTdsforold=totTdsforold+tdsAmtPrv;
                        //                            if(totTdsforold<=actualIntAmt)
                        //                                prvPreTdsAmt=totTdsforold;
                        //                            else{
                        //                                tdsAmtPrv=actualIntAmt-totTdsforoldBfore;
                        //                                prvPreTdsAmt=totTdsforoldBfore+tdsAmtPrv;
                        //                                totTdsforold=prvPreTdsAmt;
                        //                            }
                        //
                        //                            System.out.println("totTdsforold---------->"+totTdsforold);
                        //                            String prvProd_type=CommonUtil.convertObjToStr(prvIntMap.get("PRODUCT_TYPE"));
                        //                            String prvProd_Id=CommonUtil.convertObjToStr(prvIntMap.get("PRODUCT_ID"));
                        //                            String prvAcc_num=CommonUtil.convertObjToStr(prvIntMap.get("ACT_NUM"));
                        //                            Date lstIntpaidDt = (Date)prvIntMap.get("INT_PAID_DATE");
                        //                            //                        Date lstIntpaidDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(prvIntMap.get("INT_PAID_DATE")));
                        //                            setInsertData( custID,  prvProd_type,  prvProd_Id,  prvInt,  tdsAmtPrv,prvAcc_num,lstIntpaidDt);
                        //                            dataMap.put("CUST_ID",custID);
                        //                            dataMap.put("TDS_AMT",new Double(tdsAmtPrv));
                        //                            //                        dataMap.put("LST_TDS_APPL_DT",DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(prvIntMap.get("INT_DT"))));
                        //                            //                        dataMap.put("LST_TDS_APPL_DT",DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(prvIntMap.get("APPL_DT"))));
                        //                            dataMap.put("INT_DT",(Date)prvIntMap.get("INT_DT"));
                        //                            dataMap.put("APPL_DT",(Date)prvIntMap.get("APPL_DT"));
                        //                            dataMap.put("INT_PAID_DATE",(Date)prvIntMap.get("INT_PAID_DATE"));
                        //                            dataMap.put("TRANS_LOG_ID",CommonUtil.convertObjToStr(prvIntMap.get("TRANS_LOG_ID")));
                        //                            dataMap.put("INT_AMT",prvIntMap.get("INT_AMT"));
                        //
                        //                            dataMap.put("ACT_NUM",prvAcc_num);
                        //                            System.out.println("dataMap---------------->"+dataMap);
                        //
                        //                            sqlMap.executeUpdate("updateTdSApplied", dataMap);
                        //                        }
                        //
                        //                    }
                        //                }

                        amountMap.put("TDSDRAMT", new Double(prvPreTdsAmt));
                        amountMap.put("TDSDEDUCTEDTOBASEACCOUNT", new Double(TDSDEDUCTEDTOBASEACCOUNT));
                        amountMap.put("DEBIT_ACT_NUM", Acc_num);
                    }
                }
                lstTdsDtMap = null;
                //            tdsAcclst=null;
                dataMap = null;
                System.out.println("amountMap&&&&&&&&&&&&&&&& in side not error " + amountMap);
                return amountMap;
            }
        }
        amountMap = null;
        lstTdsDtMap = null;
        //        tdsAcclst=null;
        System.out.println("amountMap&&&&&&&&&&&&&&&&" + amountMap);
        return amountMap;
        //        return intAmtForTds;
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

    private double forAlreadyInterestPaidTdsDe(List backtdsLst, double actualIntAmt, double tdsPercentage, String custID, double prvPreTdsAmt, String Acc_num) throws Exception {
        double totTdsforold = prvPreTdsAmt;
        HashMap dataMap = new HashMap();
        if (backtdsLst != null && backtdsLst.size() > 0) {
            for (int i = 0; i < backtdsLst.size(); i++) {
                HashMap prvIntMap = new HashMap();

                double totTdsforoldBfore = 0.0;
                prvIntMap = (HashMap) backtdsLst.get(i);
                System.out.println("prvIntMap--------------->" + prvIntMap);
                double parailyPaid = CommonUtil.convertObjToDouble(prvIntMap.get("TDS_AMT")).doubleValue();
                double parailyPaidTdsTotal = CommonUtil.convertObjToDouble(prvIntMap.get("TOTAL_TDS_AMOUNT")).doubleValue();
                double PendingTdsAmt = parailyPaidTdsTotal - parailyPaid;

                double prvInt = CommonUtil.convertObjToDouble(prvIntMap.get("INT_AMT")).doubleValue();
                double tdsAmtPrv = prvInt * tdsPercentage / 100;
                tdsAmtPrv = (double) getNearest((long) (tdsAmtPrv * 100), 100) / 100;
                if (PendingTdsAmt > 0) {
                    tdsAmtPrv = PendingTdsAmt;
                    tdsAmtPrv = (double) getNearest((long) (tdsAmtPrv * 100), 100) / 100;
                }
                if (tdsAmtPrv < 1) {
                    tdsAmtPrv = 1;
                }
                double tdsAmountAlreadyPaid = tdsAmtPrv;
                totTdsforoldBfore = totTdsforold;

                System.out.println("totTdsforoldBfore---------->" + totTdsforoldBfore);
                System.out.println("totTdsforold---------->" + totTdsforold);
                if (totTdsforoldBfore < actualIntAmt) {
                    totTdsforold = totTdsforold + tdsAmtPrv;
                    if (totTdsforold <= actualIntAmt) {
                        prvPreTdsAmt = totTdsforold;
                    } else {
                        tdsAmtPrv = actualIntAmt - totTdsforoldBfore;
                        prvPreTdsAmt = totTdsforoldBfore + tdsAmtPrv;
                        totTdsforold = prvPreTdsAmt;
                    }

                    System.out.println("totTdsforold---------->" + totTdsforold);
                    String prvProd_type = CommonUtil.convertObjToStr(prvIntMap.get("PRODUCT_TYPE"));
                    String prvProd_Id = CommonUtil.convertObjToStr(prvIntMap.get("PRODUCT_ID"));
                    String prvAcc_num = CommonUtil.convertObjToStr(prvIntMap.get("ACT_NUM"));
                    Date lstIntpaidDt = (Date) prvIntMap.get("INT_PAID_DATE");

                    setInsertData(custID, prvProd_type, prvProd_Id, prvInt, tdsAmtPrv, prvAcc_num, lstIntpaidDt, Acc_num);
                    dataMap.put("CUST_ID", custID);
                    dataMap.put("TDS_AMT", new Double(tdsAmtPrv));
                    if (PendingTdsAmt > 0) {
                        dataMap.put("TDS_AMT", new Double(tdsAmtPrv + parailyPaid));
                    }
                    dataMap.put("INT_DT", (Date) prvIntMap.get("INT_DT"));
                    dataMap.put("APPL_DT", (Date) prvIntMap.get("APPL_DT"));
                    dataMap.put("INT_PAID_DATE", (Date) prvIntMap.get("INT_PAID_DATE"));
                    dataMap.put("TRANS_LOG_ID", CommonUtil.convertObjToStr(prvIntMap.get("TRANS_LOG_ID")));
                    dataMap.put("INT_AMT", prvIntMap.get("INT_AMT"));
                    dataMap.put("TOTAL_TDS_DEDUCTED_FROM_ALL", new Double(0));

                    dataMap.put("TOTAL_TDS_AMOUNT", new Double(tdsAmountAlreadyPaid));
                    dataMap.put("LAST_TDS_DEDUCTED_DT", ServerUtil.getCurrentDate(_branchCode));
                    dataMap.put("LAST_TDS_RECIVED_FROM", Acc_num);
                    if (tdsAmountAlreadyPaid > tdsAmtPrv) {
                        dataMap.put("IS_TDS_APPLIED", "N");
                    } else {
                        dataMap.put("IS_TDS_APPLIED", "Y");
                    }




                    dataMap.put("ACT_NUM", prvAcc_num);
                    System.out.println("dataMap---------------->" + dataMap);

                    sqlMap.executeUpdate("updateTdSApplied", dataMap);
                }

            }
        }
        return prvPreTdsAmt;
    }

    public Date nextCaldate(Date dpDt, Date nxtDt, HashMap acctDtlMap) {
        System.out.println("nxtDt&&&&&&&&&&&&&&" + nxtDt);
        System.out.println("dpDt&&&&&&&&&&&&&&" + dpDt);
        if (CommonUtil.convertObjToInt(acctDtlMap.get("FREQ")) != 0) {
            //            nxtDt=DateUtil.addDays(nxtDt,CommonUtil.convertObjToInt(acctDtlMap.get("FREQ")));
            nxtDt = DateUtil.addDaysProperFormat(nxtDt, CommonUtil.convertObjToInt(acctDtlMap.get("FREQ")));
            System.out.println("After nxtDt&&&&&&&&&&&&&&" + nxtDt);
            Calendar dpnxtCalender = new GregorianCalendar(nxtDt.getYear() + 1900, nxtDt.getMonth(), nxtDt.getDate());
            System.out.println("dpnxtCalender&&&&&&&&&&&&&&" + dpnxtCalender.getTime());
            int lstDayofmonth = dpnxtCalender.getActualMaximum(dpnxtCalender.DAY_OF_MONTH);
            System.out.println("lstDayofmonth" + lstDayofmonth);
            Calendar dpDtCalender = new GregorianCalendar(dpDt.getYear() + 1900, dpDt.getMonth() + 1, dpDt.getDate());
            int dpDay = dpDt.getDate();
            System.out.println("dpDay$$$$$$$$$$$" + dpDay);
            if (lstDayofmonth > dpDay) {
                nxtDt.setDate(dpDay);
                return nxtDt;
            } else {
                nxtDt.setDate(lstDayofmonth);
                return nxtDt;
            }
        } else {
            return DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(acctDtlMap.get("MATURITY_DT")));
        }
        //        return nxtDt ;
    }

    private HashMap tdsDebitAcNumgiven(HashMap dataMap, String Acc_num, HashMap lstTdsDtMap, double tdsPercentage, double intAmtForTds, String custID, String prodType, String prodID, double totintAmt, Date intPaidDate, double actualIntAmt, HashMap amountMap) throws Exception {
        HashMap returnMap = new HashMap();
        returnMap = null;
        HashMap dbitMap = new HashMap();
        ArrayList backtdsLst = new ArrayList();
        dbitMap.put("ACT_NUM", (String) Acc_num);
        dbitMap.put("LST_TDS_APPL_DT", (Date) lstTdsDtMap.get("LAST_APPL_DT"));
        List debitAcNoList = (List) sqlMap.executeQueryForList("getTotalIntpaidToAccountAndDebitAcNo", dataMap);
        if (debitAcNoList != null && debitAcNoList.size() > 0) {
            dbitMap = new HashMap();
            dbitMap = (HashMap) debitAcNoList.get(0);
            System.out.println("dbitMap------------>" + dbitMap);
            String dbtAcnum = CommonUtil.convertObjToStr(dbitMap.get("DEBIT_ACCT_NO"));
            String dbtProdType = CommonUtil.convertObjToStr(dbitMap.get("DEBIT_PROD_TYPE"));
            String dbtProdId = CommonUtil.convertObjToStr(dbitMap.get("DEBIT_PROD_ID"));
            double avlBal = CommonUtil.convertObjToDouble(dbitMap.get("AVAILABLE_BALANCE")).doubleValue();
            backtdsLst = getDataList(dataMap);
            double totTdsforoldBfore = 0.0;
            if (backtdsLst != null && backtdsLst.size() > 0) {
                for (int i = 0; i < backtdsLst.size(); i++) {
                    HashMap prvIntMap = new HashMap();
                    prvIntMap = (HashMap) backtdsLst.get(i);
                    System.out.println("prvIntMap--------------->" + prvIntMap);
                    double parailyPaid = CommonUtil.convertObjToDouble(prvIntMap.get("TDS_AMT")).doubleValue();
                    double parailyPaidTdsTotal = CommonUtil.convertObjToDouble(prvIntMap.get("TOTAL_TDS_AMOUNT")).doubleValue();
                    double PendingTdsAmt = parailyPaidTdsTotal - parailyPaid;

                    double prvInt = CommonUtil.convertObjToDouble(prvIntMap.get("INT_AMT")).doubleValue();
                    double tdsAmtPrv = prvInt * tdsPercentage / 100;
                    tdsAmtPrv = (double) getNearest((long) (tdsAmtPrv * 100), 100) / 100;
                    if (PendingTdsAmt > 0) {
                        tdsAmtPrv = PendingTdsAmt;
                        tdsAmtPrv = (double) getNearest((long) (tdsAmtPrv * 100), 100) / 100;
                    }
                    totTdsforoldBfore = totTdsforoldBfore + tdsAmtPrv;
                }
            }
            totTdsforoldBfore = totTdsforoldBfore + intAmtForTds;
            if (avlBal >= totTdsforoldBfore) {
                setInsertData(custID, prodType, prodID, totintAmt, intAmtForTds, Acc_num, intPaidDate, dbtAcnum);
                forAlreadyInterestPaidTdsDe(backtdsLst, avlBal, tdsPercentage, custID, intAmtForTds, dbtAcnum);
                ArrayList trfLst = new ArrayList();
                TxTransferTO objTxTransferTO = new TxTransferTO();
                TransferTrans trans = new TransferTrans();
                trans.setInitiatedBranch(_branchCode);
                HashMap txMap = new HashMap();
                txMap.put(TransferTrans.DR_ACT_NUM, dbtAcnum);
                txMap.put(TransferTrans.DR_PROD_ID, dbtProdId);

                txMap.put(TransferTrans.DR_BRANCH, _branchCode);
                txMap.put(TransferTrans.DR_PROD_TYPE, dbtProdType);
                txMap.put(TransferTrans.PARTICULARS, "tdsPaid To" + Acc_num);
                txMap.put(TransferTrans.CURRENCY, "INR");
                trans.setInitiatedBranch(_branchCode);
                trfLst.add(trans.getDebitTransferTO(txMap, totTdsforoldBfore));
                txMap = new HashMap();
                txMap.put(TransferTrans.CR_AC_HD, CommonUtil.convertObjToStr(amountMap.get("TDSCrACHdId")));
                txMap.put(TransferTrans.CR_PROD_TYPE, "GL");
                txMap.put(TransferTrans.CURRENCY, "INR");
                txMap.put(TransferTrans.PARTICULARS, "TdsDeducted from " + Acc_num);
                txMap.put(TransferTrans.CR_BRANCH, _branchCode);
                TransferTrans transferTrans = new TransferTrans();
                trfLst.add(trans.getCreditTransferTO(txMap, totTdsforoldBfore));
                trans.doDebitCredit(trfLst, _branchCode);
                System.out.println("dbtAcnum------------>" + dbtAcnum);
                returnMap = new HashMap();
                returnMap.put("DEBIT_ACT_NUM", CommonUtil.convertObjToStr(dbtAcnum));
            }

        }
        return returnMap;
    }
  
    public HashMap getTDSHead(String custID) throws Exception {
        curDt = ServerUtil.getCurrentDate(_branchCode);
        HashMap amountMap = new HashMap();
        fromDeductionScreen = false;
        depositTDSDetails = new HashMap();
        HashMap tdsAccMap = new HashMap();
        HashMap lstTdsDtMap = new HashMap();
        depositAccountDetails = new HashMap();
        lstTdsDtMap.put("REMARKS", "TDS");
        lstTdsDtMap.put("BRANCH_CODE", _branchCode);
        List tdsDtlst = sqlMap.executeQueryForList("getLastTDSApplDT", lstTdsDtMap);
        if (tdsDtlst != null && tdsDtlst.size() > 0) {
            lstTdsDtMap = (HashMap) tdsDtlst.get(0);
        } else {
            throw new TTException("Last TDS Applied Date not set in Deposit_Provision");
        }
        tdsAccMap.put("CUST_ID", custID);
        tdsAccMap.put("LST_TDS_APPL_DT", (Date) lstTdsDtMap.get("LAST_APPL_DT"));
        List tdsAcclst = sqlMap.executeQueryForList("getAccountforTDS", tdsAccMap);
        if (tdsAcclst != null && tdsAcclst.size() > 0) {
            for (int i = 0; i < tdsAcclst.size(); i++) {
                tdsAccMap = new HashMap();
                tdsAccMap = (HashMap) tdsAcclst.get(i);
            }
        }
        HashMap dataMap = new HashMap();
        Date intDate = ServerUtil.getCurrentDate(_branchCode);
        dataMap.put("INT_DATE", intDate);
        if (CommonUtil.convertObjToStr(tdsAccMap.get("CUST_TYPE")).equals("INDIVIDUAL")) {
            dataMap.put("CUST_TYPE", CommonUtil.convertObjToStr(tdsAccMap.get("CUST_TYPE")));
        } else {
            dataMap.put("CUST_TYPE", "CORPORATE");
        }
        List list = (List) sqlMap.executeQueryForList("getTDSConfigData", dataMap);
        if (list.size() > 0) {
            HashMap resultMap = (HashMap) list.get(0);
            String Cr_ac_hd_id = CommonUtil.convertObjToStr(resultMap.get("TDS_CR_AC_HD_ID"));
            amountMap.put("TDSCrACHdId", Cr_ac_hd_id);
        }
        return amountMap;
    }
    

    public boolean isIsTransaction() {
        return isTransaction;
    }

    public void setIsTransaction(boolean isTransaction) {
        this.isTransaction = isTransaction;
    }

    public boolean isPan() {
        return pan;
    }

    public void setPan(boolean pan) {
        this.pan = pan;
    }
    
    
    public double totalIntrestCalculationforTds(HashMap tdsAccMap, HashMap lstTdsDtMap, HashMap closeMap) throws Exception {
        double totIntToAcc = 0.0;        
        Date lstTdsCalDt = null;
        HashMap dataMap = new HashMap();
        if (!CommonUtil.convertObjToStr(tdsAccMap.get("OTHERDAO")).equals("OTHERDAO")) {            
            double paidInt = 0.0;
            lstTdsCalDt = (Date) lstTdsDtMap.get("LAST_APPL_DT");
            Date tdsUptoCalDt = DateUtil.addDays(lstTdsCalDt, 360);
            dataMap.put("LST_TDS_APPL_DT", lstTdsCalDt);
            dataMap.put("UPTO_TDS_APPL_DT", tdsUptoCalDt);
            dataMap.put("DEPOSIT_NO", CommonUtil.convertObjToStr(tdsAccMap.get("ACT_NUM1")));
            dataMap.put("CURR_DT",curDt.clone());
            //System.out.println("inside tds calc :: totIntToAcc ::" + totIntToAcc);
            List list = (List) sqlMap.executeQueryForList("getTotalIntpaidToAccount", dataMap);
            if (list != null & list.size() > 0) {
                dataMap = (HashMap) list.get(0);
                paidInt = CommonUtil.convertObjToDouble(dataMap.get("INT_PAID_AMT")).doubleValue();              
                totIntToAcc = totIntToAcc + paidInt;
            }
            list = null;
        }      
        dataMap = null;
        lstTdsDtMap = null;
        tdsAccMap = null;
        totIntToAcc = (double) getNearest((long) (totIntToAcc * 100), 100) / 100;
        return totIntToAcc;
    }
    

    
}
