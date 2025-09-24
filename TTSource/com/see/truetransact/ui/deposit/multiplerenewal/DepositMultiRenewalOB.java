/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DepositInterestApplicationOB.java
 *
 * Created on Mon Jun 13 18:24:58 IST 2011
 */
package com.see.truetransact.ui.deposit.multiplerenewal;

import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.apache.log4j.Logger;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.commonutil.InterestCalc;
import com.see.truetransact.transferobject.deposit.AccInfoTO;
import com.see.truetransact.transferobject.deposit.DepSubNoAccInfoTO;
import com.see.truetransact.transferobject.deposit.JointAccntTO;
import com.see.truetransact.clientutil.JointAcctHolderManipulation;
import com.see.truetransact.ui.TrueTransactMain;
import java.util.GregorianCalendar;
import javax.swing.DefaultListModel;
import java.text.DecimalFormat;

/**
 *
 * @author
 */
public class DepositMultiRenewalOB extends CObservable {

    final ArrayList tableTitle = new ArrayList();
    private ArrayList IncVal = new ArrayList();
    private EnhancedTableModel tblDepositInterestApplication;
    private ProxyFactory proxy;
    private HashMap map;
    private final static Logger log = Logger.getLogger(DepositMultiRenewalOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private int result;
    private int actionType;
    private boolean rdoPrizedMember_Yes = false;
    private boolean rdoPrizedMember_No = false;
    private int yearTobeAdded = 1900;
    private List finalList = null;
    private List finalTableList = null;
    private HashMap param;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private ArrayList rdList;
    private String txtProductID = "";
    private String txtTokenNo = "";
    final String prodBehavesLikeRecurr = "RECURRING";
    final String prodBehavesLikeDaily = "DAILY";
    final String prodBehavesLikeFixed = "FIXED";
    final String prodBehavesLikeCummulative = "CUMMULATIVE";
    private List calFreqAccountList = null;
    private String cboSIProductId = "";
    private ComboBoxModel cbmSIProductId;
    private ComboBoxModel cbmOAProductID;
    private ComboBoxModel cbmCategory;
    LinkedHashMap depSubNoTOMap;
    private DefaultListModel lstSelectedTransaction = new DefaultListModel();
    private LinkedHashMap newTransactionMap = new LinkedHashMap();
    HashMap cashtoTransferMap = new HashMap();
    private ComboBoxModel cbmProdType, cboRenewalInterestTransMode;
    private ComboBoxModel cbmRenewalInterestTransMode;
    private ComboBoxModel cbmRenewalInterestTransProdId;
    private String productBehavesLike = "";
    private LinkedHashMap allowedTransactionDetailsTO = null;
    private final String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs";
    private LinkedHashMap transactionDetailsTO;
    HashMap finalMap = new HashMap();
    private final String FLD_FOR_DB_YES_NO = "DBYesOrNo";
    private String productInterestType = "";
     private EnhancedTableModel tblJointAccnt;
     public final String YES_STR = "Y";
    public final String NO_STR = "N";
    AccInfoTO objAccInfoTO;
    LinkedHashMap jntAcctTOMap;
    public LinkedHashMap jntAcctAll;
    JointAcctHolderManipulation objJointAcctHolderManipulation = new JointAcctHolderManipulation();
      private final String YES_FULL_STR = "Yes";
    private final String NO_FULL_STR = "No";
     private String cboCategory = "";
     HashMap jntAcctSingleRec;
     private Date currDt = null;
        void setCboCategory(String cboCategory) {
        this.cboCategory = cboCategory;
        setChanged();
    }

    String getCboCategory() {
        return this.cboCategory;
    }

    public void setCbmCategory(ComboBoxModel cbmCategory) {
        this.cbmCategory = cbmCategory;
        setChanged();
    }

    ComboBoxModel getCbmCategory() {
        return cbmCategory;
    }
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRenewalInterestTransMode() {
        return cbmRenewalInterestTransMode;
    }

    /**
     * Setter for property cbmRenewalInterestTransMode.
     *
     * @param cbmRenewalInterestTransMode New value of property
     * cbmRenewalInterestTransMode.
     */
    public void setCbmRenewalInterestTransMode(com.see.truetransact.clientutil.ComboBoxModel cbmRenewalInterestTransMode) {
        this.cbmRenewalInterestTransMode = cbmRenewalInterestTransMode;
    }

    public ComboBoxModel getCboRenewalInterestTransMode() {
        return cboRenewalInterestTransMode;
    }

    public void setCboRenewalInterestTransMode(ComboBoxModel cboRenewalInterestTransMode) {
        this.cboRenewalInterestTransMode = cboRenewalInterestTransMode;
    }
    private HashMap lookUpHash;

    public void setCbmRenewalDepTransProdId(com.see.truetransact.clientutil.ComboBoxModel cbmRenewalInterestTransProdId) {
        this.cbmRenewalInterestTransProdId = cbmRenewalInterestTransProdId;
    }

    /**
     * Getter for property cbmRenewalInterestTransProdId.
     *
     * @return Value of property cbmRenewalInterestTransProdId.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRenewalInterestTransProdId() {
        return cbmRenewalInterestTransProdId;
    }

    public void setCbmRenewalInterestTransProdId(com.see.truetransact.clientutil.ComboBoxModel cbmRenewalInterestTransProdId) {
        this.cbmRenewalInterestTransProdId = cbmRenewalInterestTransProdId;
    }

    public DepositMultiRenewalOB() {
        try {
            currDt = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "DepositMultiRenewalJNDI");
            map.put(CommonConstants.HOME, "deposit.multiplerenewal.DepositMultiRenewalHome");
            map.put(CommonConstants.REMOTE, "deposit.multiplerenewal.DepositMultiRenewal");
            setDepositInterestTableTitle();
            fillDropdown();
            tblDepositInterestApplication = new EnhancedTableModel(null, tableTitle);
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    public void setDepositInterestTableTitle() {
        tableTitle.add("Select");
        tableTitle.add("Account No");
        tableTitle.add("Customer");
        tableTitle.add("Deposit Amount");
        tableTitle.add("Opening Mode");
        tableTitle.add("Mat Date");
        tableTitle.add("Rate of Interest");
        tableTitle.add("Interest");
        tableTitle.add("Ren.Dep Amt");
        tableTitle.add("Ren.ROI");
        tableTitle.add("Ren.Mat Amount");
        tableTitle.add("Ren.Int Amount");
        tableTitle.add("Ren.Mat Date");
        tableTitle.add("Curr.Category");
        tableTitle.add("Curr.Period(DD-MM-YY)");
        tableTitle.add("TDS"); // Added by nithya on 06-02-2020 for KD-1090
        IncVal = new ArrayList();
    }

    private void getKeyValue(HashMap keyValue) throws Exception {
        log.info("In getKeyValue()");

        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }

    public void setCbmProdId(String prodType) {
        if (CommonUtil.convertObjToStr(prodType).length() > 1) {
            if (prodType.equals("GL")) {
                key = new ArrayList();
                value = new ArrayList();
            } else {
                try {
                    param = new HashMap();
                    param.put(CommonConstants.MAP_NAME, "Cash.getAccProduct" + prodType);
                    param.put(CommonConstants.PARAMFORQUERY, null);
                    keyValue = ClientUtil.populateLookupData(param);
                    fillData((HashMap) keyValue.get(CommonConstants.DATA));
                    cbmOAProductID = new ComboBoxModel(key, value);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            key = new ArrayList();
            value = new ArrayList();
            key.add("");
            value.add("");
        }
        //  cbmOAProductID = new ComboBoxModel(key, value);
        //   this.cbmOAProductID = cbmOAProductID;
        //   setChanged();


    }

    public void setCbmProdType(ComboBoxModel cbmProdType) {
        this.cbmProdType = cbmProdType;
        setChanged();
    }

    ComboBoxModel getCbmProdType() {
        return cbmProdType;
    }

    public void setCbmRenewalInterestTransProdId(String prodType) {
        if (prodType.equals("GL")) {
            key = new ArrayList();
            value = new ArrayList();
        } else {
            try {
                lookUpHash = new HashMap();
                lookUpHash.put(CommonConstants.MAP_NAME, "Cash.getAccProduct" + prodType);
                lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
                keyValue = ClientUtil.populateLookupData(lookUpHash);
                getKeyValue((HashMap) keyValue.get(CommonConstants.DATA));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        cbmRenewalInterestTransProdId = new ComboBoxModel(key, value);
        this.cbmRenewalInterestTransProdId = cbmRenewalInterestTransProdId;
        setChanged();
    }

    private void fillDropdown() throws Exception {
        try {
            lookUpHash = new HashMap();
            lookUpHash.put(CommonConstants.MAP_NAME, null);
            final ArrayList lookup_keys = new ArrayList();
            lookup_keys.add("PRODUCTTYPE");
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);

            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap) keyValue.get("PRODUCTTYPE"));
            cbmProdType = new ComboBoxModel(key, value);
            //cbmProdType.removeKeyAndElement("SA");
            //cbmProdType.removeKeyAndElement("AB");
            cbmProdType.removeKeyAndElement("AD");
            cbmProdType.removeKeyAndElement("TD");
            cbmProdType.removeKeyAndElement("TL");
            HashMap param = new HashMap();
            param.put(CommonConstants.MAP_NAME, "getSIProducts");
            keyValue = ClientUtil.populateLookupData(param);
            fillData((HashMap) keyValue.get(CommonConstants.DATA));
            cbmSIProductId = new ComboBoxModel(key, value);

            lookup_keys.add("REMITTANCE_PAYMENT.PAY_MODE");
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);

            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap) keyValue.get("REMITTANCE_PAYMENT.PAY_MODE"));
            cbmRenewalInterestTransMode = new ComboBoxModel(key, value);
            lstSelectedTransaction = new DefaultListModel();
            
            lookup_keys.add("CATEGORY");
            lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
            keyValue = ClientUtil.populateLookupData(lookUpHash);
            getKeyValue((HashMap) keyValue.get("CATEGORY"));
            cbmCategory = new ComboBoxModel(key, value);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fillData(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get("KEY");
        value = (ArrayList) keyValue.get("VALUE");
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

    public String getProductBehaveLike(String param) {
        final HashMap whereMap = new HashMap();
        whereMap.put("PROD_ID", param);
        final List resultList = ClientUtil.executeQuery("getProductBehavesLike", whereMap);
        HashMap resultProductBehavesLike = (HashMap) resultList.get(0);
        productBehavesLike = CommonUtil.convertObjToStr(resultProductBehavesLike.get("BEHAVES_LIKE"));
        productInterestType = CommonUtil.convertObjToStr(resultProductBehavesLike.get("INT_TYPE"));
        // setBehavesLike(productBehavesLike);
        return productBehavesLike;
    }

    public long roundOffLower(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        return number - mod;
    }
    public void insertTableData(HashMap whereMap) {
        try {
            ArrayList rowList = new ArrayList();
            ArrayList tableList = new ArrayList();
            HashMap dataMap = new HashMap();
            finalMap = new HashMap();
            List lst = ClientUtil.executeQuery("listAllMultipleRenewal", whereMap);
            DepSubNoAccInfoTO objDepSubNoAccInfoTO = null;
            if (lst != null && lst.size() > 0) {
                for (int i = 0; i < lst.size(); i++) {
                    dataMap = (HashMap) lst.get(i);
                    HashMap renewalMap = new HashMap();
                    // Changed case of Deposit No,Customer,Deposit amount,Product Id,Opening Mode to uppercase to solve java 7 issue
                    //String accNo = CommonUtil.convertObjToStr(dataMap.get("Deposit No"));
                    String accNo = CommonUtil.convertObjToStr(dataMap.get("DEPOSIT NO"));
                    //String customer = CommonUtil.convertObjToStr(dataMap.get("Customer"));
                    String customer = CommonUtil.convertObjToStr(dataMap.get("CUSTOMER"));
                    String cust_id = CommonUtil.convertObjToStr(dataMap.get("CUST_ID"));
                    //String depAmt = CommonUtil.convertObjToStr(dataMap.get("Deposit amount"));
                    String depAmt = CommonUtil.convertObjToStr(dataMap.get("DEPOSIT AMOUNT"));
                    String categ ="";
                    if (whereMap.containsKey("CATEGORY") && whereMap.get("CATEGORY") != null) {
                        categ = CommonUtil.convertObjToStr(whereMap.get("CATEGORY"));
                    } else {
                        categ = CommonUtil.convertObjToStr(dataMap.get("CATEGORY"));
                    }
                    String currCategory=CommonUtil.convertObjToStr(dataMap.get("CATEGORY"));
                    //String prodid = CommonUtil.convertObjToStr(dataMap.get("Product Id"));
                    String prodid = CommonUtil.convertObjToStr(dataMap.get("PRODUCT ID"));
                    //String opMode = CommonUtil.convertObjToStr(dataMap.get("Opening Mode"));
                    String opMode = CommonUtil.convertObjToStr(dataMap.get("OPENING MODE"));
                    String MatDate = CommonUtil.convertObjToStr(dataMap.get("MATURITY_DATE"));
                    String prodBevaves = getProductBehaveLike(prodid);
                    HashMap data = new HashMap();
                    rowList = new ArrayList();
                    rowList.add(new Boolean(false));
                    rowList.add(accNo);
                    rowList.add(customer);
                    rowList.add(depAmt);
                    rowList.add(opMode);
                    rowList.add(MatDate);
                    HashMap dMap = new HashMap();
                    dMap.put("DEPOSIT NO", accNo);
                    List list = (List) ClientUtil.executeQuery("getSelectDepSubNoAccInfoTO", dMap);
                    HashMap calcDepSubNo;
                    String oldDepDate = "";
                    double renIntAmt = 0;
                    double renewedDepAmt = 0, matAmt = 0, balIntAmt = 0, tdsAmount = 0.0;
                    for (int j = 0; j < list.size(); j++) {
                        calcDepSubNo = (HashMap) list.get(j);
                        double totAmt = CommonUtil.convertObjToDouble(calcDepSubNo.get("TOT_INT_AMT")).doubleValue();
                        double drAmt = CommonUtil.convertObjToDouble(calcDepSubNo.get("TOTAL_INT_DRAWN")).doubleValue();
                        double crAmt = CommonUtil.convertObjToDouble(calcDepSubNo.get("TOTAL_INT_CREDIT")).doubleValue();
                        oldDepDate = CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_DT"));
                        // Added by nithya on 06-11-2020 from KD-2378
                        if(calcDepSubNo.containsKey("LAST_INT_APPL_DT") && calcDepSubNo.get("LAST_INT_APPL_DT") != null){
                           oldDepDate = CommonUtil.convertObjToStr(calcDepSubNo.get("LAST_INT_APPL_DT")); 
                        }
                         // End
                        balIntAmt = totAmt - drAmt;
                        double balAmt = crAmt - drAmt;
                        balIntAmt = (double) getNearest((long) (balIntAmt * 100), 100) / 100;
                        rowList.add(CommonUtil.convertObjToStr(calcDepSubNo.get("RATE_OF_INT")));//ROI
                        if (prodBevaves != null && prodBevaves.equals("FIXED")) {
                            // rowList.add(totAmt);//int
                            rowList.add(balIntAmt);//int
                        } else {
                            rowList.add(balIntAmt);//int
                        }
                        matAmt = CommonUtil.convertObjToDouble(calcDepSubNo.get("MATURITY_AMT")).doubleValue();
                         // For TDS                        
                       // System.out.println("balance Int amount = " + balIntAmt);
                        if (balIntAmt > 0) { // Added by nithya on 06-02-2020 for KD-1090
                            double tdsAmt = balIntAmt;
                            HashMap tdsCalcMap = new HashMap();
                            tdsCalcMap.put("TDS_CALCULATION", "TDS_CALCULATION");
                            tdsCalcMap.put("CUST_ID", cust_id);
                            tdsCalcMap.put("PROD_ID", getTxtProductID());
                            tdsCalcMap.put("DEPOSIT_NO", accNo);
                            tdsCalcMap.put("RATE_OF_INT", calcDepSubNo.get("RATE_OF_INT"));
                            tdsCalcMap.put("TDS_AMOUNT", new Double(tdsAmt));
                            tdsCalcMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
                            //System.out.println("tdsCalcMap :: " + tdsCalcMap + "operationMap :: " + map);
                            try {
                                HashMap tdsData = proxy.executeQuery(tdsCalcMap, map);
                                if (tdsData != null && tdsData.size() > 0) {
                                    if (tdsData.containsKey("TDSDRAMT") && tdsData.get("TDSDRAMT") != null && CommonUtil.convertObjToDouble(tdsData.get("TDSDRAMT")) > 0) {
                                        //setLblTDSAmount(CommonUtil.convertObjToStr(CommonUtil.convertObjToDouble(tdsData.get("TDSDRAMT"))));
                                        //setTdsAcHd(CommonUtil.convertObjToStr(tdsData.get("TDSCrACHdId")));
                                        tdsAmount = CommonUtil.convertObjToDouble(tdsData.get("TDSDRAMT"));
                                        renewalMap.put("TDS_AMOUNT",tdsData.get("TDSDRAMT"));
                                        renewalMap.put("TDS_AC_HD",tdsData.get("TDSCrACHdId"));
                                    } else {
                                        tdsAmount = 0.0; 
                                    }
                                }
                                //System.out.println("tdsData :: " + tdsData);
                            } catch (Exception ex) {
                                System.out.println("Exception in TDS Calculation :: " + ex);
                            }
                        }
                        // end
                        renewedDepAmt = 0;
                        if (prodBevaves != null && prodBevaves.equals("FIXED")) {
                            if (whereMap.containsKey("INT_WITHDRAWING")) {
                                renewedDepAmt = matAmt;//-balIntAmt;
                                rowList.add(renewedDepAmt);//Ren dep amt
                            } else {
                                renewedDepAmt = matAmt + balIntAmt - tdsAmount;
                                rowList.add(renewedDepAmt);//Ren dep amt
                            }
                        } else {
                            if (whereMap.containsKey("INT_WITHDRAWING")) {
                                renewedDepAmt = matAmt - balIntAmt;
                                rowList.add(renewedDepAmt);//Ren dep amt
                            } else {
                                renewedDepAmt = matAmt - tdsAmount;
                                rowList.add(renewedDepAmt);//Ren dep amt
                                //rowList.add(matAmt);//Ren dep amt
                            }
                        }
                        //ren int calc
                        double maturityAmt = 0.0;
                        double depositAmt = 0;
                        double interestAmt = 0;
                        
                        int pYeras = 0, pMonths = 0, pDays = 0;
                        pYeras = CommonUtil.convertObjToInt(whereMap.get("PERIOD_YEARS"));
                        pMonths = CommonUtil.convertObjToInt(whereMap.get("PERIOD_MONTHS"));
                        pDays = CommonUtil.convertObjToInt(whereMap.get("PERIOD_DAYS"));
                        String rowPeriod = CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_PERIOD_DD")) + "-" + CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_PERIOD_MM")) + "-" + CommonUtil.convertObjToStr(calcDepSubNo.get("DEPOSIT_PERIOD_YY"));
                        HashMap detailsHash = new HashMap();
                        //txtRenewalRateOfInterest.setText(String.valueOf(txtRenewalRateOfInterest.getText()));
                        //added by rishad 25/11/2019 for renewal int calcaulation mistake in case of discounted rate
                        if (calcDepSubNo.containsKey("INTPAY_FREQ") && calcDepSubNo.get("INTPAY_FREQ") != null) {
                            int freq = CommonUtil.convertObjToInt(calcDepSubNo.get("INTPAY_FREQ"));
                            if (freq == 30) {
                                detailsHash.put("INTEREST_TYPE", "MONTHLY");
                            } else if (freq == 90) {
                                detailsHash.put("INTEREST_TYPE", "QUATERLY");
                            } else if (freq == 180) {
                                detailsHash.put("INTEREST_TYPE", "HALF YEARLY");
                            } else if (freq == 0) {
                                detailsHash.put("INTEREST_TYPE", "DATE OF MATURITY");
                            } else if (freq == 360) {
                                detailsHash.put("INTEREST_TYPE", "YEARLY");
                            } else if (freq == 60) {
                                detailsHash.put("INTEREST_TYPE", "2MONTHS");
                            } else if (freq == 120) {
                                detailsHash.put("INTEREST_TYPE", "4MONTHS");
                            } else if (freq == 150) {
                                detailsHash.put("INTEREST_TYPE", "5MONTHS");
                            } else if (freq == 210) {
                                detailsHash.put("INTEREST_TYPE", "7MONTHS");
                            } else if (freq == 240) {
                                detailsHash.put("INTEREST_TYPE", "8MONTHS");
                            } else if (freq == 270) {
                                detailsHash.put("INTEREST_TYPE", "9MONTHS");
                            } else if (freq == 300) {
                                detailsHash.put("INTEREST_TYPE", "10MONTHS");
                            } else if (freq == 330) {
                                detailsHash.put("INTEREST_TYPE", "11MONTHS");
                            }
                        }
                        String depMatDate = DateUtil.getStringDate((Date) calcDepSubNo.get("MATURITY_DT"));
                        detailsHash.put("AMOUNT", renewedDepAmt);
                        detailsHash.put("DEPOSIT_DT", depMatDate);
                        detailsHash.put("PERIOD_DAYS", pDays);
                        detailsHash.put("PERIOD_MONTHS", pMonths);
                        detailsHash.put("PERIOD_YEARS", pYeras);
                        String RenewMatDate = calculateRenewalMatDate(depMatDate,
                                pYeras, pMonths, pDays);
                        detailsHash.put("MATURITY_DT", RenewMatDate);
                        double ROI = setRenewalRateOfInterset(prodid, categ, renewedDepAmt,
                                depMatDate, RenewMatDate,pYeras,pMonths);
                        detailsHash.put("ROI", ROI);
                        detailsHash.put("DISCOUNTED_RATE", "");
                        detailsHash.put("BEHAVES_LIKE", prodBevaves);
                        detailsHash.put("CATEGORY_ID", categ);
                        detailsHash.put("PROD_ID", prodid);
                        detailsHash.put("BEHAVES_LIKE", prodBevaves);
                        detailsHash.put("BEHAVES_LIKE", prodBevaves);
                        detailsHash.put("DEPOSIT_DT", DateUtil.getDateMMDDYYYY(depMatDate));
                        detailsHash.put("MATURITY_DT", DateUtil.getDateMMDDYYYY(RenewMatDate));
                        detailsHash = setRenewalAmountsAccROI(detailsHash, null);
                        rowList.add(CommonUtil.convertObjToStr(ROI));//Rate of int
                        double renMatAmount = CommonUtil.convertObjToDouble(detailsHash.get("AMOUNT"));
                        renMatAmount = (double) getNearest((long) (renMatAmount * 100), 100) / 100;
                        rowList.add(CommonUtil.convertObjToStr(renMatAmount));//Ren Mat amt

                        renIntAmt = CommonUtil.convertObjToDouble(detailsHash.get("INTEREST"));
                        // renIntAmt = (double) getNearest((long) (renIntAmt * 100), 100) / 100;
                        //added by Rishad 17/06/2015  for mantis 10758
                        HashMap roundMap = new HashMap();
                        String prodId = getTxtProductID();
                        roundMap.put("PROD_ID", prodId);
                        List roundgList = ClientUtil.executeQuery("getRoungOffTypeInterest", roundMap);
                        if (!roundgList.isEmpty()) {
                            roundMap = (HashMap) roundgList.get(0);
                        }
                        if (prodBevaves.equals("FIXED")) {
                            maturityAmt = CommonUtil.convertObjToDouble(renewedDepAmt);
                            renIntAmt = CommonUtil.convertObjToDouble(detailsHash.get("INTEREST")).doubleValue();
                            if (roundMap.get("INT_ROUNDOFF_TERMS").equals("NEAREST_VALUE")) {
                                renIntAmt = (double) getNearest((long) (renIntAmt * 100), 100) / 100;
                            } else if (roundMap.get("INT_ROUNDOFF_TERMS").equals("LOWER_VALUE")) {
                                renIntAmt = (double) roundOffLower((long) (renIntAmt * 100), 100) / 100;
                            } else if (roundMap.get("INT_ROUNDOFF_TERMS").equals("HIGHER_VALUE")) {
                                renIntAmt = (double) higher((long) (renIntAmt * 100), 100) / 100;
                            } else {
                                renIntAmt = new Double(renIntAmt);
                            }
                        } else if (prodBevaves.equals("CUMMULATIVE")) {
                            maturityAmt = CommonUtil.convertObjToDouble(detailsHash.get("AMOUNT")).doubleValue();
                            if (roundMap.get("INT_ROUNDOFF_TERMS").equals("NEAREST_VALUE")) {
                                maturityAmt = (double) getNearest((long) (maturityAmt * 100), 100) / 100;
                            } else if (roundMap.get("INT_ROUNDOFF_TERMS").equals("LOWER_VALUE")) {
                                maturityAmt = (double) roundOffLower((long) (maturityAmt * 100), 100) / 100;
                            } else if (roundMap.get("INT_ROUNDOFF_TERMS").equals("HIGHER_VALUE")) {
                                maturityAmt = (double) higher((long) (maturityAmt * 100), 100) / 100;
                            } else {
                                maturityAmt = new Double(maturityAmt);
                            }
                            renIntAmt = maturityAmt - CommonUtil.convertObjToDouble(renewedDepAmt);
                        } else {
                            if (roundMap.get("INT_ROUNDOFF_TERMS").equals("NEAREST_VALUE")) {
                                maturityAmt = CommonUtil.convertObjToDouble(detailsHash.get("AMOUNT")).doubleValue();
                                maturityAmt = (double) getNearest((long) (maturityAmt * 100), 100) / 100;
                                renIntAmt = CommonUtil.convertObjToDouble(detailsHash.get("INTEREST")).doubleValue();
                                renIntAmt = (double) getNearest((long) (renIntAmt * 100), 100) / 100;
                            } else if (roundMap.get("INT_ROUNDOFF_TERMS").equals("LOWER_VALUE")) {
                                maturityAmt = CommonUtil.convertObjToDouble(detailsHash.get("AMOUNT")).doubleValue();
                                maturityAmt = (double) roundOffLower((long) (maturityAmt * 100), 100) / 100;
                                renIntAmt = CommonUtil.convertObjToDouble(detailsHash.get("INTEREST")).doubleValue();
                                renIntAmt = (double) roundOffLower((long) (renIntAmt * 100), 100) / 100;
                            } else if (roundMap.get("INT_ROUNDOFF_TERMS").equals("HIGHER_VALUE")) {
                                maturityAmt = CommonUtil.convertObjToDouble(detailsHash.get("AMOUNT")).doubleValue();
                                maturityAmt = (double) higher((long) (maturityAmt * 100), 100) / 100;
                                renIntAmt = CommonUtil.convertObjToDouble(detailsHash.get("INTEREST")).doubleValue();
                                renIntAmt = (double) higher((long) (renIntAmt * 100), 100) / 100;
                            } else {
                                maturityAmt = new Double(maturityAmt);
                                renIntAmt = new Double(renIntAmt);
                            }
                        }
                        //end 10758
                        rowList.add(CommonUtil.convertObjToStr(renIntAmt));//Ren Int amt
                        rowList.add(CommonUtil.convertObjToStr(DateUtil.getDateMMDDYYYY(RenewMatDate)));//Ren Mat date
                        rowList.add(CommonUtil.convertObjToStr(currCategory));//Curr category
                        rowList.add(CommonUtil.convertObjToStr(rowPeriod));
                        if (prodBevaves != null && prodBevaves.equals("FIXED") && whereMap.containsKey("INT_WITHDRAWING")) {
                            //renewalMap.put("RENEWAL_TOT_INTAMT",CommonUtil.convertObjToStr(balIntAmt));
                            renewalMap.put("RENEWAL_TOT_INTAMT", CommonUtil.convertObjToStr(renIntAmt));
                        } else {
                            renewalMap.put("RENEWAL_TOT_INTAMT", CommonUtil.convertObjToStr(renIntAmt));
                        }
                        renewalMap.put("RENEWAL_DEPOSIT_AMT", CommonUtil.convertObjToStr(renewedDepAmt));
                        renewalMap.put("BALANCE_INT_AMT", CommonUtil.convertObjToStr(balIntAmt));
                        renewalMap.put("RENEWAL_MATURITY_AMT", CommonUtil.convertObjToStr(renMatAmount));//detailsHash.get("AMOUNT")));
                        renewalMap.put("RENEWAL_MATURITY_DT", RenewMatDate);
                        renewalMap.put("RENEWAL_RATE_OF_INT", CommonUtil.convertObjToStr(ROI));
                        renewalMap.put("RENEWAL_INT_FREQ", CommonUtil.convertObjToStr(calcDepSubNo.get("INTPAY_FREQ")));
                        renewalMap.put("INT_PAY_PROD_TYPE", CommonUtil.convertObjToStr(calcDepSubNo.get("INT_PAY_PROD_TYPE")));
                        renewalMap.put("INT_PAY_PROD_ID", CommonUtil.convertObjToStr(calcDepSubNo.get("INT_PAY_PROD_ID")));
                        renewalMap.put("INT_PAY_ACC_NO", CommonUtil.convertObjToStr(calcDepSubNo.get("INT_PAY_ACC_NO")));
                        renewalMap.put("INTPAY_MODE", CommonUtil.convertObjToStr(calcDepSubNo.get("INTPAY_MODE")));
                        renewalMap.put("RENEWAL_PAY_MODE", CommonUtil.convertObjToStr(calcDepSubNo.get("INTPAY_MODE")));
                        String mode = CommonUtil.convertObjToStr(calcDepSubNo.get("INTPAY_MODE"));
                        renewalMap.put("POSTAGE_AMT",calcDepSubNo.get("POSTAGE_AMT"));
                        if (mode != null && mode.equals("TRANSFER")) {
                            renewalMap.put("RENEWAL_PAY_PRODTYPE", CommonUtil.convertObjToStr(calcDepSubNo.get("INT_PAY_PROD_TYPE")));
                            renewalMap.put("RENEWAL_PAY_PRODID", CommonUtil.convertObjToStr(calcDepSubNo.get("INT_PAY_PROD_ID")));
                            renewalMap.put("RENEWAL_PAY_ACCNO", CommonUtil.convertObjToStr(calcDepSubNo.get("INT_PAY_ACC_NO")));
                        }
                        //end
                    }
                    rowList.add(CommonUtil.convertObjToStr(tdsAmount)); // Added by nithya on 06-02-2020 for KD-1090
                    tableList.add(rowList);
                    //setting the finalmap
                    objAccInfoTO = setAccInfoData(whereMap, dataMap, cust_id,categ);
                    objAccInfoTO.setCommand("RENEW");
                    data.put("TERMDEPOSIT", objAccInfoTO);
                    //For joint acc
                    //Below line commented by sreekrishnan as per discussed with jithesh
                    /* if (objAccInfoTO.getConstitution().equals("JOINT_ACCOUNT")) {
                            data.put("JointAccntTO", setJointAccntData(dataMap));
                            jntAcctTOMap = new LinkedHashMap() ;
                     }*/
                   /*   if (objAccInfoTO.getAuthorizedSignatory().equals(YES_STR)) {
                        data.put(AUTH_SIGN_DAO, authorizedSignatoryOB.setAuthorizedSignatory());
                         data.put(AUTH_SIGN_INST_DAO, authorizedSignatoryInstructionOB.setAuthorizedSignatoryInstruction());
                      }*/
                    //--- Puts the data if the Poa is checked
                   /* if (objAccInfoTO.getPoa().equals(YES_STR)) {
                        data.put(POA_FOR_DAO, powerOfAttorneyOB.setTermLoanPowerAttorney());
                    }*/

                    //--- puts the data if the NomineeDetails is checked
                    if (objAccInfoTO.getNomineeDetails().equals(YES_STR)) {
                        HashMap nomineeMap =new HashMap();
                         nomineeMap.put("DEPOSIT_NO", CommonUtil.convertObjToStr(dataMap.get("DEPOSIT NO")));
                            List lst1 = (List) ClientUtil.executeQuery("getSelectRenewalNomineeTOTD", nomineeMap);
                            if (lst1 != null && lst1.size() > 0) {
                                data.put("AccountNomineeTO", lst1);
                            }
                       // data.put("AccountNomineeTO", objNomineeOB.getNomimeeList());
                        data.put("AccountNomineeDeleteTO", new ArrayList());
                    }
                    //end
                    renewalMap.put("RENEWAL_DEP_ADDING", "NO");
                    renewalMap.put("RENEW_POSTAGE_AMT", "");
                    renewalMap.put("RENEWAL_INT_PAYABLE", "Y");
                    renewalMap.put("RENEWAL_DEPOSIT_YEARS", CommonUtil.convertObjToStr(whereMap.get("PERIOD_YEARS")));
                    renewalMap.put("OLD_DEPOSIT_DATE", oldDepDate);
                    renewalMap.put("FLD_DEP_RENEWAL_SUB_PRINTINGNO", "");
                    renewalMap.put("RENEWAL_INT_PAYABLE", "Y");
                    renewalMap.put("RENEWAL_CATEGORY", CommonUtil.convertObjToStr(categ));
                    renewalMap.put("RENEWAL_DEPOSIT_DT", CommonUtil.convertObjToStr(dataMap.get("MATURITY_DATE")));
                    renewalMap.put("DEPOSIT_NO", CommonUtil.convertObjToStr(dataMap.get("DEPOSIT NO")));
                    renewalMap.put("RENEWAL_NOTICE", "N");
                    renewalMap.put("SB_PERIOD_RUN", "");//////////////
                    renewalMap.put("AUTO_RENEWAL", "N");
                    int siFreq = CommonUtil.convertObjToInt(renewalMap.get("RENEWAL_INT_FREQ"));
                    String freq = "";
                    if (siFreq == 30) {
                        freq = CommonUtil.convertObjToStr("Monthly");
                    } else if (siFreq == 90) {
                        freq = CommonUtil.convertObjToStr("Quaterly");
                    } else if (siFreq == 180) {
                        freq = CommonUtil.convertObjToStr("Half Yearly");
                    } else if (siFreq == 360) {
                        freq = CommonUtil.convertObjToStr("Yearly");
                    } else if (siFreq == 60) {
                        freq = CommonUtil.convertObjToStr("2 Months");
                    } else if (siFreq == 120) {
                        freq = CommonUtil.convertObjToStr("4 Months");
                    } else if (siFreq == 150) {
                        freq = CommonUtil.convertObjToStr("5 Months");
                    } else if (siFreq == 210) {
                        freq = CommonUtil.convertObjToStr("7 Months");
                    } else if (siFreq == 240) {
                        freq = CommonUtil.convertObjToStr("8 Months");
                    } else if (siFreq == 270) {
                        freq = CommonUtil.convertObjToStr("9 Months");
                    } else if (siFreq == 300) {
                        freq = CommonUtil.convertObjToStr("10 Months");
                    } else if (siFreq == 330) {
                        freq = CommonUtil.convertObjToStr("11 Months");
                    } else if (siFreq == 0) {
                        freq = "Date of Maturity";
                    }
                    ///periodic int clc
                    double perIntAmt = 0, yr = 0;
                    int pYeras = 0, pMonths = 0, pDays = 0;
                    pYeras = CommonUtil.convertObjToInt(whereMap.get("PERIOD_YEARS"));
                    pMonths = CommonUtil.convertObjToInt(whereMap.get("PERIOD_MONTHS"));
                    pDays = CommonUtil.convertObjToInt(whereMap.get("PERIOD_DAYS"));
                    if (pYeras > 0) {
                        yr = CommonUtil.convertObjToDouble(pYeras);
                    }

                    if (pMonths > 0) {
                        yr = yr + (CommonUtil.convertObjToDouble(pMonths) / 12);
                    }

                    if (pDays > 0) {
                        yr = yr + (CommonUtil.convertObjToDouble(pDays) / 365);
                    }
                    double totalIntAmtPerYear = (CommonUtil.convertObjToDouble(renIntAmt) / yr);
                    if (siFreq == 180) {
                        perIntAmt = totalIntAmtPerYear / 2;
                    } else if (siFreq == 30) {
                        perIntAmt = totalIntAmtPerYear / 12;
                    double depositAmt = CommonUtil.convertObjToDouble(renewedDepAmt);

                        //--- Calculation for Period as No.Of Days
                        int YrsToDay = 0;
                        int MonToDay = 0;
                        int daysEntered = 0;
                        int periodInDays = 0;
                        if (pYeras > 0) {
                            YrsToDay = (CommonUtil.convertObjToInt(pYeras)) * 365;
                        }
                        if (pMonths > 0) {
                            MonToDay = (CommonUtil.convertObjToInt(pMonths) * 30);
                        }
                        if (pDays > 0) {
                            daysEntered = (CommonUtil.convertObjToInt(pDays));
                        }
                        periodInDays = (YrsToDay + MonToDay + daysEntered);
                    }
                    if (siFreq == 360) {
                        perIntAmt = totalIntAmtPerYear;
                    } else if (siFreq == 90) {
                        perIntAmt = totalIntAmtPerYear / 4;
                    } else if (siFreq == 0) {
                        perIntAmt = 0;
                    } else if (siFreq == 60) {
                        perIntAmt = totalIntAmtPerYear / 6;
                    } else if (siFreq == 120) {
                        perIntAmt = totalIntAmtPerYear / 3;
                    } else if (siFreq == 150) {
                        perIntAmt = totalIntAmtPerYear / 2.4;
                    } else if (siFreq == 210) {
                        perIntAmt = totalIntAmtPerYear / 1.7;
                    } else if (siFreq == 240) {
                        perIntAmt = totalIntAmtPerYear / 1.5;
                    } else if (siFreq == 270) {
                        perIntAmt = totalIntAmtPerYear / 1.33;
                    } else if (siFreq == 300) {
                        perIntAmt = totalIntAmtPerYear / 1.2;
                    } else if (siFreq == 330) {
                        perIntAmt = totalIntAmtPerYear / 1.09;
                    }
                    try {
                        String depInt = "N";
                        HashMap dataMap1 = new HashMap();
                        dataMap.put("PROD_ID", CommonUtil.convertObjToStr(dataMap.get("PRODUCT ID")));
                        List periodList = ClientUtil.executeQuery("getIntRoundAtIntApplication", dataMap1);
                        if (periodList != null && periodList.size() > 0) {
                            HashMap periodMap = (HashMap) periodList.get(0);
                            if (periodMap.containsKey("INT_ROUND_AT_INTAPPL")) {
                                depInt = "Y";
                            }
                        }
                        if (depInt != null && depInt.equals("N")) {
                            perIntAmt = (double) getNearest((long) (perIntAmt * 100), 100) / 100;
                        } else {
                            DecimalFormat df = new DecimalFormat("#.##");
                            perIntAmt = CommonUtil.convertObjToDouble(df.format(perIntAmt));
                        }
                    } catch (Exception e) {
                        System.out.println("Exxxxx-->" + e);
                    }
                    //end
                    renewalMap.put("RENEWAL_PAY_FREQ", CommonUtil.convertObjToStr(freq));
                    renewalMap.put("RENEWAL_NOTICE", "N");
                    renewalMap.put("RENEWAL_DEPOSIT_DAYS", CommonUtil.convertObjToStr(whereMap.get("PERIOD_DAYS")));
                    if (whereMap.containsKey("INT_WITHDRAWING")) {
                        renewalMap.put("RENEWAL_INT_WITHDRAWING", "YES");
                        double withdrawingIntAmt = CommonUtil.convertObjToDouble(renewalMap.get("BALANCE_INT_AMT")) - tdsAmount; // 17-02-2020
                        //renewalMap.put("WITHDRAWING_INT_AMT", CommonUtil.convertObjToStr(renewalMap.get("BALANCE_INT_AMT")));
                        renewalMap.put("WITHDRAWING_INT_AMT",withdrawingIntAmt);
                        String withDType = CommonUtil.convertObjToStr(whereMap.get("INT_MODE"));
                        renewalMap.put("RENEWAL_INT_TRANS_MODE", withDType);
                        if (withDType.equals("TRANSFER")) {
                            renewalMap.put("RENEWAL_INT_TRANS_ACCNO", CommonUtil.convertObjToStr(whereMap.get("INT_AC_NO")));
                            renewalMap.put("RENEWAL_INT_TRANS_PRODID", CommonUtil.convertObjToStr(whereMap.get("INT_PROD_ID")));
                            renewalMap.put("RENEWAL_INT_TRANS_PRODTYPE", CommonUtil.convertObjToStr(whereMap.get("INT_PROD_TYPE")));

                        } else {
                            renewalMap.put("RENEWAL_INT_TOKEN_NO", "");
                        }

                        renewalMap.put("RENEWAL_INT_WITHDRAWING", "YES");
                        renewalMap.put("RENEWAL_INT_WITHDRAWING", "YES");

                    } else {
                        renewalMap.put("RENEWAL_INT_WITHDRAWING", "NO");///////////////////
                    }
                    renewalMap.put("PREV_INT_AMT", "0");
                    renewalMap.put("RENEWAL_DEPOSIT_MONTHS", CommonUtil.convertObjToStr(whereMap.get("PERIOD_MONTHS")));
                    renewalMap.put("USER_ID", TrueTransactMain.USER_ID);
                    renewalMap.put("RENEWAL_PRODID", CommonUtil.convertObjToStr(dataMap.get("PRODUCT ID")));
                    renewalMap.put("AUTO_RENEW_WITH", "N");
                    renewalMap.put("RENEWAL_CALENDER_FREQ_DAY", "");
                    renewalMap.put("RENEWAL_DEP_WITHDRAWING", "NO");
                    renewalMap.put("SB_INT_AMT", "0");
                    renewalMap.put("RENEWAL_CALENDER_FREQ", "N");
                    renewalMap.put("RENEWAL_PERIODIC_INT", CommonUtil.convertObjToStr(perIntAmt));
                    renewalMap.put("BRANCH_CODE", TrueTransactMain.selBranch);
                    renewalMap.put(CommonConstants.INITIATED_BRANCH, ProxyParameters.BRANCH_ID);
                    renewalMap.put("OLD_MATURITY_DATE", CommonUtil.convertObjToStr(dataMap.get("MATURITY_DATE")));
                    renewalMap.put("PENDING_AMT_RATE", "");
                    renewalMap.put("CUST_STATUS",CommonUtil.convertObjToStr(dataMap.get("CUST_STATUS")));
                    data.put("RENEWALMAP", renewalMap);
                    if (transactionDetailsTO == null) {
                        transactionDetailsTO = new LinkedHashMap();
                    }
                    data.put(CommonConstants.MODULE, "Time Deposits");
                    data.put(CommonConstants.SCREEN, "Deposit Accounts");
                    data.put(CommonConstants.SELECTED_BRANCH_ID, TrueTransactMain.selBranch);
                    //data.put("BRANCH_CODE", TrueTransactMain.selBranch);
                    data.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);//Added by nithya on 12-09-2022 for KD-3463
                    data.put(CommonConstants.INITIATED_BRANCH, ProxyParameters.BRANCH_ID);
                    data.put("DepSubNoAccInfoTO", setDepSubNoAccInfoData(whereMap, dataMap, renewalMap, prodBevaves));
                    transactionDetailsTO.put(NOT_DELETED_TRANS_TOs, allowedTransactionDetailsTO);
                    data.put("TransactionTO", transactionDetailsTO);
                    data.put("SAME_DEPOSIT_NO", "");
                    data.put("UI_PRODUCT_TYPE", "TD");
                    data.put("MODE", CommonConstants.TOSTATUS_INSERT);
                    HashMap intMap = new HashMap();
                    intMap.put("TRANS_MODE", "");
                    intMap.put("INT", "INT");

                    intMap.put("SB_INT_AMT", String.valueOf("0"));
                    intMap.put("SB_PERIOD_RUN", String.valueOf("0"));
//                intMap.put("BAL_INT_AMT",String.valueOf(getBalanceInterestAmountValue()));

                    intMap.put("ACT_NUM", CommonUtil.convertObjToStr(dataMap.get("DEPOSIT NO")));
                    intMap.put("ROI", CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_RATE_OF_INT")));
                    intMap.put("CUST_ID", cust_id);
                    if (prodBevaves != null && prodBevaves.equals("FIXED")) {
                        // if(!whereMap.containsKey("INT_WITHDRAWING")){
                        intMap.put("DEPOSIT_AMT", CommonUtil.convertObjToStr(matAmt));
                        intMap.put("BAL_INT_AMT", CommonUtil.convertObjToStr(balIntAmt));
                        //  }
                        //  else{
                        //      intMap.put("DEPOSIT_AMT",  CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEPOSIT_AMT")));
                        //  intMap.put("BAL_INT_AMT", CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_TOT_INTAMT")));
                        //        }
                    } else {
                        intMap.put("DEPOSIT_AMT", CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_DEPOSIT_AMT")));
                        intMap.put("BAL_INT_AMT", CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_TOT_INTAMT")));
                    }
                    
                    data.put("INTMAP", intMap);
                    intMap = null;
                    finalMap.put(CommonUtil.convertObjToStr(dataMap.get("DEPOSIT NO")), data);
                    System.out.println("final################"+finalMap);
                    //endddd
                }
            }
            setFinalMap(finalMap);
            depSubNoTOMap = null;
            tblDepositInterestApplication = new EnhancedTableModel((ArrayList) tableList, tableTitle);
//            }
        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e, true);
        }
    }
     private HashMap insertJntAcctSingleRec(HashMap custMapData, String dbYesOrNo){
        jntAcctSingleRec = new HashMap();
        jntAcctSingleRec.put("CUST_ID",CommonUtil.convertObjToStr(custMapData.get("CUST_ID")));
        jntAcctSingleRec.put(FLD_FOR_DB_YES_NO,dbYesOrNo);
        jntAcctSingleRec.put("STATUS", "CREATED");
        return jntAcctSingleRec;
    }
  public HashMap setJointAccntData(HashMap dataMap) {
        HashMap singleRecordJntAcct;
        jntAcctTOMap = new LinkedHashMap();
        try {
            JointAccntTO objJointAccntTO;
            HashMap hash = new HashMap();
            hash.put("CUST_ID", CommonUtil.convertObjToStr(dataMap.get("CUST_ID")));
           // jntAcctAll = objJointAcctHolderManipulation.populateJointAccntTable(hash, jntAcctAll, tblJointAccnt);
            if(jntAcctAll==null){ //--- If jointAcctAll Hashmap is null, initialize it.
                jntAcctAll = new LinkedHashMap();
            }
             HashMap custMapData;
            List custListData = ClientUtil.executeQuery("getSelectAccInfoTblDisplay",dataMap);
            custMapData = (HashMap) custListData.get(0);
            String keyCustId = CommonUtil.convertObjToStr(custMapData.get("CUST_ID"));
            /* If there is No Customer Id, insert the all the data with dbYesOrNo having "No" value
             * else, insert the all the data with dbYesOrNo having "Yes" value */
            if(jntAcctAll.get(keyCustId) == null){
                custMapData = insertJntAcctSingleRec(custMapData, NO_FULL_STR);
            } else {
                custMapData = insertJntAcctSingleRec(custMapData, YES_FULL_STR);
            }
            jntAcctAll.put(keyCustId, custMapData);
            System.out.println("jntAcctAll%$%#%#%"+jntAcctAll);
            custListData = null;
            custMapData=null;
            int jntAcctSize = jntAcctAll.size();
            for (int i = 0; i < jntAcctSize; i++) {
                singleRecordJntAcct = (HashMap) jntAcctAll.get(CommonUtil.convertObjToStr(jntAcctAll.keySet().toArray()[i]));
                objJointAccntTO = new JointAccntTO();
                objJointAccntTO.setCustId(CommonUtil.convertObjToStr(singleRecordJntAcct.get("CUST_ID")));
                objJointAccntTO.setDepositNo(CommonUtil.convertObjToStr(dataMap.get("Deposit No")));
                objJointAccntTO.setStatus(CommonUtil.convertObjToStr(singleRecordJntAcct.get("STATUS")));
                objJointAccntTO.setCommand("RENEW");
                jntAcctTOMap.put(String.valueOf(i), objJointAccntTO);
                System.out.println("jntAcctTOMap%$%#%#%"+jntAcctTOMap);
                objJointAccntTO = null;
                singleRecordJntAcct = null;
            }
        } catch (Exception e) {
            parseException.logException(e, true);
        }
        return jntAcctTOMap;
    }
    public HashMap setDepSubNoAccInfoData(HashMap whereMap, HashMap dataMap, HashMap renewalMap, String prodBevaves) {
        HashMap depSubNoAccInfoSingleRec;
        depSubNoTOMap = new LinkedHashMap();
        try {
            DepSubNoAccInfoTO objDepSubNoAccInfoTO;
            //  int depSubNoSize = depSubNoAll.size();
            //  for (int i = 0; i < renewalMap.size(); i++) {
            // depSubNoAccInfoSingleRec = (HashMap) depSubNoAll.get(String.valueOf(i));
            objDepSubNoAccInfoTO = new DepSubNoAccInfoTO();
            objDepSubNoAccInfoTO.setCommand("RENEW");
            objDepSubNoAccInfoTO.setDepositAmt(CommonUtil.convertObjToDouble(dataMap.get("DEPOSIT AMOUNT")));
            objDepSubNoAccInfoTO.setDepositDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(dataMap.get("OLD_MATURITY_DATE"))));
            objDepSubNoAccInfoTO.setDepositNo("Renewal");
            objDepSubNoAccInfoTO.setDepositPeriodDd(CommonUtil.convertObjToDouble(whereMap.get("PERIOD_DAYS")));
            objDepSubNoAccInfoTO.setDepositPeriodMm(CommonUtil.convertObjToDouble(whereMap.get("PERIOD_MONTHS")));
            objDepSubNoAccInfoTO.setDepositPeriodYy(CommonUtil.convertObjToDouble(whereMap.get("PERIOD_YEARS")));
            //mm  
            //objDepSubNoAccInfoTO.setDepositPeriodWk("");
            objDepSubNoAccInfoTO.setDepositSubNo(CommonUtil.convertObjToInt("1"));
            objDepSubNoAccInfoTO.setIntpayFreq(CommonUtil.convertObjToDouble(renewalMap.get("RENEWAL_INT_FREQ")));
            objDepSubNoAccInfoTO.setIntpayMode(CommonUtil.convertObjToStr(renewalMap.get("INTPAY_MODE")));
            objDepSubNoAccInfoTO.setInstallType(CommonUtil.convertObjToStr(""));
            objDepSubNoAccInfoTO.setPaymentType(CommonUtil.convertObjToStr(""));
            objDepSubNoAccInfoTO.setPaymentDay(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_MATURITY_DT"))));
            if (prodBevaves != null && prodBevaves.equals("FIXED")) {
                // if(!whereMap.containsKey("INT_WITHDRAWING")){
                objDepSubNoAccInfoTO.setMaturityAmt(CommonUtil.convertObjToDouble(dataMap.get("DEPOSIT AMOUNT")));
                objDepSubNoAccInfoTO.setMaturityDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_MATURITY_DT"))));
                if (whereMap.containsKey("INT_WITHDRAWING")) {

                    objDepSubNoAccInfoTO.setTotIntAmt(CommonUtil.convertObjToDouble(renewalMap.get("RENEWAL_TOT_INTAMT")));
                } else {
                    objDepSubNoAccInfoTO.setTotIntAmt(CommonUtil.convertObjToDouble(renewalMap.get("RENEWAL_TOT_INTAMT")));
                }
                //  }
            } else {
                objDepSubNoAccInfoTO.setMaturityAmt(CommonUtil.convertObjToDouble(renewalMap.get("RENEWAL_MATURITY_AMT")));
                objDepSubNoAccInfoTO.setMaturityDt(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_MATURITY_DT"))));
                objDepSubNoAccInfoTO.setTotIntAmt(CommonUtil.convertObjToDouble(renewalMap.get("RENEWAL_TOT_INTAMT")));
            }
            objDepSubNoAccInfoTO.setPeriodicIntAmt(CommonUtil.convertObjToDouble(""));
            objDepSubNoAccInfoTO.setRateOfInt(CommonUtil.convertObjToDouble(renewalMap.get("RENEWAL_RATE_OF_INT")));
            objDepSubNoAccInfoTO.setCreateBy(TrueTransactMain.USER_ID);
            objDepSubNoAccInfoTO.setSubstatusBy(TrueTransactMain.USER_ID);
            objDepSubNoAccInfoTO.setSubstatusDt(currDt);
            objDepSubNoAccInfoTO.setStatus(CommonUtil.convertObjToStr(CommonConstants.STATUS_CREATED));
            objDepSubNoAccInfoTO.setIntPayProdId(CommonUtil.convertObjToStr(renewalMap.get("INT_PAY_PROD_ID")));
            objDepSubNoAccInfoTO.setIntPayProdType(CommonUtil.convertObjToStr(renewalMap.get("INT_PAY_PROD_TYPE")));
            objDepSubNoAccInfoTO.setIntPayAcNo(CommonUtil.convertObjToStr(renewalMap.get("INT_PAY_ACC_NO")));
            // if (depSubNoAccInfoSingleRec.get(FLD_DEP_SUB_CALENDER_FREQ) != null && depSubNoAccInfoSingleRec.get(FLD_DEP_SUB_CALENDER_FREQ).equals("Y")) {
            //      objDepSubNoAccInfoTO.setCalender_freq(CommonUtil.convertObjToStr(depSubNoAccInfoSingleRec.get(FLD_DEP_SUB_CALENDER_FREQ)));
            //      objDepSubNoAccInfoTO.setCalender_date(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depSubNoAccInfoSingleRec.get(FLD_DEP_SUB_CALENDER_DATE))));
            //      objDepSubNoAccInfoTO.setCalender_day(CommonUtil.convertObjToDouble(depSubNoAccInfoSingleRec.get(FLD_DEP_SUB_CALENDER_DAY)));
            // } else {
            objDepSubNoAccInfoTO.setCalender_freq("N");
            objDepSubNoAccInfoTO.setCalender_date(DateUtil.getDateMMDDYYYY(""));
            objDepSubNoAccInfoTO.setCalender_day(CommonUtil.convertObjToDouble(""));
            //  }
            if ("RENEW".equals("RENEW")) {
                objDepSubNoAccInfoTO.setFlexi_status("NR");
            } else {
                objDepSubNoAccInfoTO.setFlexi_status("N");
            }
            objDepSubNoAccInfoTO.setSalaryRecovery("N");

            String productId = CommonUtil.convertObjToStr(renewalMap.get("RENEWAL_PRODID"));
            objDepSubNoAccInfoTO.setPostageAmt(CommonUtil.convertObjToDouble(""));
            objDepSubNoAccInfoTO.setRenewPostageAmt(CommonUtil.convertObjToDouble(renewalMap.get("POSTAGE_AMT")));
            //if (getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            // calculateNextAppDate(CommonUtil.convertObjToInt(depSubNoAccInfoSingleRec.get(FLD_DEP_SUB_NO_INT_PAY_FREQ)));
            // objDepSubNoAccInfoTO.setNextIntAppDate(getNextInterestApplDate());
            // }
            //system.out.println("############Status****** :" +objDepSubNoAccInfoTO.getAcctStatus());
            HashMap recurringMap = new HashMap();
            recurringMap.put("PROD_ID", productId);
            List lst = ClientUtil.executeQuery("getBehavesLikeForDeposit", recurringMap);
            if (lst.size() > 0) {
                recurringMap = (HashMap) lst.get(0);
                if (recurringMap.get("BEHAVES_LIKE").equals("RECURRING")) {
                    double period = CommonUtil.convertObjToInt(objDepSubNoAccInfoTO.getDepositPeriodYy());
                    double periodMm = 0.0;
                    if (period >= 1) {
                        period = period * 12.0;
                        periodMm = CommonUtil.convertObjToInt(objDepSubNoAccInfoTO.getDepositPeriodMm());
                        period = period + periodMm;
                    } else {
                        period = CommonUtil.convertObjToInt(objDepSubNoAccInfoTO.getDepositPeriodMm());
                    }
                    objDepSubNoAccInfoTO.setTotalInstallments(new Double(period));
                }
                //  if (recurringMap.get("BEHAVES_LIKE").equals("DAILY")) {
                //   double yearPeriod = CommonUtil.convertObjToDouble(depSubNoAccInfoSingleRec.get(FLD_DEP_SUB_NO_DEP_PER_YY)).doubleValue();
                //   double monthPeriod = CommonUtil.convertObjToDouble(depSubNoAccInfoSingleRec.get(FLD_DEP_SUB_NO_DEP_PER_MM)).doubleValue();
                //   if (yearPeriod > 1) {
                //       yearPeriod = yearPeriod * 12;
                //       yearPeriod = yearPeriod + monthPeriod;
                //       objDepSubNoAccInfoTO.setDepositPeriodMm(CommonUtil.convertObjToDouble(new Double(yearPeriod)));
                //       objDepSubNoAccInfoTO.setDepositPeriodYy(CommonUtil.convertObjToDouble(new Double(0.0)));
                //   }
                //}
            }
            depSubNoTOMap.put("0", objDepSubNoAccInfoTO);
            //objDepSubNoAccInfoTO = null;
            // depSubNoAccInfoSingleRec = null;
            // }
        } catch (Exception e) {
            parseException.logException(e);
        }
        return depSubNoTOMap;
    }
    /* To set Account Transfer data in the Transfer Object*/

    public AccInfoTO setAccInfoData(HashMap whereMap, HashMap dataMap, String cust_id,String categ) {
        objAccInfoTO = new AccInfoTO();
        try {
            /* Sets the Authroized signatory to "Y" if it is checked
            else it assigns "N" */
            objAccInfoTO.setAuthorizedSignatory("N");
            objAccInfoTO.setCommAddress("");
            objAccInfoTO.setCommand("RENEW");
            objAccInfoTO.setCustId(CommonUtil.convertObjToStr(cust_id));
            objAccInfoTO.setDepositNo(CommonUtil.convertObjToStr(dataMap.get("DEPOSIT NO")));
            objAccInfoTO.setRenewalFromDeposit(CommonUtil.convertObjToStr(dataMap.get("DEPOSIT NO")));
            objAccInfoTO.setOpeningMode("Renewal");
            objAccInfoTO.setDepositNo("Renewal");//CommonUtil.convertObjToStr(dataMap.get("Deposit No")));
            objAccInfoTO.setRenewalCount(CommonUtil.convertObjToDouble("1"));
            objAccInfoTO.setFifteenhDeclare("N");
            objAccInfoTO.setNomineeDetails(CommonUtil.convertObjToStr(dataMap.get("NOMINEE_DETAILS")));
            objAccInfoTO.setOpeningMode("Renewal");
            objAccInfoTO.setTransOut("NR");
            objAccInfoTO.setDeathClaim("N");
            objAccInfoTO.setAutoRenewal("N");

            //  if (getRdowithIntRenewal_Yes() == true) {
            objAccInfoTO.setRenewWithInt("Y");
            // } else {
            objAccInfoTO.setRenewWithInt("N");
            //  }

            objAccInfoTO.setMatAlertRep("N");

            objAccInfoTO.setStandingInstruct("N");

            objAccInfoTO.setPanNumber("");

            objAccInfoTO.setPoa("N");

            objAccInfoTO.setAgentId("");
            objAccInfoTO.setProdId(CommonUtil.convertObjToStr(dataMap.get("PRODUCT ID")));
            objAccInfoTO.setRemarks(CommonUtil.convertObjToStr(dataMap.get("REMARKS")));
            objAccInfoTO.setMdsGroup(CommonUtil.convertObjToStr(dataMap.get("MDS_GROUP")));
            objAccInfoTO.setMdsRemarks(CommonUtil.convertObjToStr(dataMap.get("MDS_REMARKS")));
            objAccInfoTO.setSettlementMode(CommonUtil.convertObjToStr("0"));
            objAccInfoTO.setConstitution(CommonUtil.convertObjToStr(dataMap.get("CONSTITUTION")));
            objAccInfoTO.setAddressType(CommonUtil.convertObjToStr("Home"));
            objAccInfoTO.setCategory(CommonUtil.convertObjToStr(categ));
            //objAccInfoTO.setCustType("");
            objAccInfoTO.setBranchId(TrueTransactMain.selBranch);
            objAccInfoTO.setCreatedBy(TrueTransactMain.USER_ID);
            objAccInfoTO.setCreatedDt(currDt);
            objAccInfoTO.setStatusBy(TrueTransactMain.USER_ID);
            objAccInfoTO.setStatus(CommonConstants.STATUS_CREATED);
            objAccInfoTO.setStatusDt(currDt);
            objAccInfoTO.setInitiatedBranch(ProxyParameters.BRANCH_ID);
            objAccInfoTO.setPrintingNo(CommonUtil.convertObjToInt(0));
            objAccInfoTO.setReferenceNo("");
            objAccInfoTO.setMember("N");
            objAccInfoTO.setTaxDeductions(CommonUtil.convertObjToStr(dataMap.get("TAX_DEDUCTIONS")));//"TAX_DEDUCTIONS");
            objAccInfoTO.setAccZeroBalYN("N");
        } catch (Exception e) {
            parseException.logException(e, true);
        }
        return objAccInfoTO;
    }

    private String calculateRenewalMatDate(String depMatDate, int pYear, int pMonth, int pDays) {
        java.util.Date depDate = DateUtil.getDateWithoutMinitues(depMatDate);
        //system.out.println("####calculateMatDate : " + depDate);
        if (depDate != null) {
            GregorianCalendar cal = new GregorianCalendar((depDate.getYear() + yearTobeAdded), depDate.getMonth(), depDate.getDate());
            if (pYear > 0) {
                cal.add(GregorianCalendar.YEAR, pYear);
            } else {
                cal.add(GregorianCalendar.YEAR, 0);
            }
            if (pMonth > 0) {
                cal.add(GregorianCalendar.MONTH, pMonth);
            } else {
                cal.add(GregorianCalendar.MONTH, 0);
            }
            if (pDays > 0) {
                double txtBoxPeriod = CommonUtil.convertObjToDouble(pDays).doubleValue();
                String totMonths = String.valueOf(txtBoxPeriod / 365);
                long totyears = new Long(totMonths.substring(0, totMonths.indexOf("."))).longValue();
                double leftOverMth = new Double(totMonths.substring(totMonths.indexOf("."))).doubleValue();
                java.text.DecimalFormat df = new java.text.DecimalFormat("#####");
                leftOverMth = new Double(df.format(leftOverMth * 365)).doubleValue();
                if (totyears >= 1) {
                    cal.add(GregorianCalendar.YEAR, (int) totyears);
                    cal.add(GregorianCalendar.DAY_OF_MONTH, (int) leftOverMth);
                } else {
                    cal.add(GregorianCalendar.DAY_OF_MONTH, pDays);
                }
            } else {
                cal.add(GregorianCalendar.DAY_OF_MONTH, 0);
            }
            //  observable.setRenewaltdtMaturityDate(DateUtil.getStringDate(cal.getTime()));
            ///tdtRenewalMaturityDate.setDateValue(observable.getRenewaltdtMaturityDate());
            return DateUtil.getStringDate(cal.getTime());
        }
        return null;
    }

    public double setRenewalRateOfInterset(String prodiD, String category, double renewedDepAmt, String depdate, String renMatDate,int pYeras,int pMonths) {
        double retInt = -1;
        long period = 0;
        HashMap whereMap = new HashMap();
        whereMap.put("PRODUCT_TYPE", "TD");
        String sourceProdId = prodiD;
        String prodId = prodiD;

        whereMap.put("CATEGORY_ID", category);
        whereMap.put("PROD_ID", prodiD);
        whereMap.put("AMOUNT", renewedDepAmt);
        whereMap.put("PRODUCT_TYPE", "TD");
        whereMap.put("DEPOSIT_DT", depdate);
        Date startDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depdate));
        Date endDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(renMatDate));
        whereMap.put("DEPOSIT_DT", startDt);
//        if (startDt != null && endDt != null) {
//            period = DateUtil.dateDiff(startDt, endDt);
//        }
        //added by rishad at 15/03/2019  
        if (startDt != null && endDt != null) {
            int year = CommonUtil.convertObjToInt(pYeras);
            int month = CommonUtil.convertObjToInt(pMonths);
            
            if (year != 0 || month != 0) {
                HashMap whereDateMap = new HashMap();
                whereDateMap.put("START_DT", startDt);
                whereDateMap.put("END_DT", endDt);
                List monthList = (List) ClientUtil.executeQuery("getMonthInBetweenDayes", whereDateMap);
                if (monthList != null && monthList.size() > 0) {
                    HashMap resultMap = (HashMap) monthList.get(0);
                    if (resultMap.containsKey("COUNT")) {
                        period = CommonUtil.convertObjToLong(resultMap.get("COUNT"));
                    }
                }
            }
            period += DateUtil.dateDiff(startDt, endDt);
        }
        whereMap.put("PERIOD", period);
        List dataList = (List) ClientUtil.executeQuery("icm.getInterestRates", whereMap);
        HashMap roiHash = new HashMap();
        if (dataList != null && dataList.size() > 0) {
            roiHash = (HashMap) dataList.get(0);
            retInt = CommonUtil.convertObjToDouble(roiHash.get("ROI")).doubleValue();
        } else {
            retInt = 0;
        }
        return retInt;
    }

    public String getDepositCategoryForCustomer(String fromAcNo, String toAcNo) {
        String Category = "";
        HashMap whereMap = new HashMap();
        whereMap.put("FROM_AC_NO", fromAcNo);
        whereMap.put("TO_AC_NO", toAcNo);
        List dataList = (List) ClientUtil.executeQuery("getDepositCategoryForCustomer", whereMap);        
        if (dataList != null && dataList.size() > 0) {
            if(dataList.size()==1){
                whereMap = (HashMap) dataList.get(0);
                Category = CommonUtil.convertObjToStr(whereMap.get("CATEGORY"));
            }else {
                return null;
            }
        } else {
             return null;
        }
        return Category;
    }
        
    public HashMap setRenewalAmountsAccROI(HashMap detailsHash, HashMap param) {
       // System.out.println("########setAmountsAccROI : "+detailsHash );
        HashMap amtDetHash = new HashMap();
        long period = 0;
        long cummPeriod = 0;
        long cummMonth = 0;
        double totalAmt = 0.0;
        double depAmt =CommonUtil.convertObjToDouble(detailsHash.get("AMOUNT"));
        if (param == null) {
            if (detailsHash.get("BEHAVES_LIKE").equals("FIXED")) {
                if (detailsHash.containsKey("INTEREST_TYPE") && detailsHash.get("INTEREST_TYPE") != null && detailsHash.get("INTEREST_TYPE").equals("MONTHLY")) {
                    period = CommonUtil.convertObjToInt(detailsHash.get("PERIOD_MONTHS")) * 30;
                    period = period + CommonUtil.convertObjToInt(detailsHash.get("PERIOD_YEARS")) * 360;
                } else {
                    Date startDt = null;
                    Date endDt = null;
                    if (detailsHash.containsKey("DEPOSIT_DT") && detailsHash.containsKey("MATURITY_DT")) {
                        startDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(detailsHash.get("DEPOSIT_DT")));
                        endDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(detailsHash.get("MATURITY_DT")));
                    } else {
                        startDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(detailsHash.get("DEPOSIT_DT")));
                        endDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(detailsHash.get("MATURITY_DT")));
                    }
                    period = DateUtil.dateDiff(startDt, endDt);
                    int count = 0;
                    while (DateUtil.dateDiff(startDt, endDt) > 0) {
                        int month = startDt.getMonth();
                        int startYear = startDt.getYear() + 1900;
                        if (month == 1 && startYear % 4 == 0) {
                            count++;
                        }
                        startDt = DateUtil.addDays(startDt, 30);
                    }
                    period -= count;
                }
            } else if (detailsHash.get("BEHAVES_LIKE").equals("CUMMULATIVE")) {
                if (detailsHash.containsKey("PERIOD_DAYS") && detailsHash.get("PERIOD_DAYS") != null) {
                    period = period + CommonUtil.convertObjToInt(detailsHash.get("PERIOD_DAYS"));
                    cummPeriod = period;
                }
                if (detailsHash.containsKey("PERIOD_MONTHS") && detailsHash.get("PERIOD_MONTHS") != null) {
                    period = period + CommonUtil.convertObjToInt(detailsHash.get("PERIOD_MONTHS")) * 30;
                    cummMonth = CommonUtil.convertObjToInt(detailsHash.get("PERIOD_MONTHS"));
                }
                if (detailsHash.containsKey("PERIOD_YEARS") && detailsHash.get("PERIOD_YEARS") != null) {
                    period = period + CommonUtil.convertObjToInt(detailsHash.get("PERIOD_YEARS")) * 360;
                }
                long fullPeriod = 0;
                fullPeriod = period;
                double simpleAmt = 0.0;
                double completeAmt = 0.0;
                cummPeriod = cummPeriod % 30;
                cummMonth = cummMonth % 3;
                if (cummMonth == 0) {
                    //system.out.println("******** cummPeriod == 0: "+cummPeriod);
                }
                if (cummPeriod > 0 || cummMonth > 0) {
                    cummMonth = cummMonth * 30;
                    cummPeriod = cummPeriod + cummMonth;
                    //system.out.println("******** cummPeriod != 0: "+cummPeriod);
                }
                if (fullPeriod > 0) {
                    period = fullPeriod - cummPeriod;
                    detailsHash.put("BEHAVES_LIKE", "CUMMULATIVE");
                    detailsHash.put("PEROID", String.valueOf(period));
                    detailsHash.put("CATEGORY_ID", CommonUtil.convertObjToStr(detailsHash.get("CATEGORY_ID")));
                    detailsHash.put("PROD_ID", CommonUtil.convertObjToStr(detailsHash.get("PROD_ID")));
                    List list = (List) ClientUtil.executeQuery("getDepProdIntPay", detailsHash);
                    if (list != null && list.size() > 0) {
                        detailsHash.putAll((HashMap) list.get(0));
                        InterestCalc interestCalc = new InterestCalc();
                        amtDetHash = interestCalc.calcAmtDetails(detailsHash);
                        completeAmt = CommonUtil.convertObjToDouble(amtDetHash.get("INTEREST")).doubleValue();
                    }
                    detailsHash.put("AMOUNT", amtDetHash.get("AMOUNT"));
                    detailsHash.remove("PEROID");
                    int yearPer = CommonUtil.convertObjToInt(detailsHash.get("PERIOD_YEARS"));
                    yearPer = yearPer * 12;
                    int monthPer = CommonUtil.convertObjToInt(detailsHash.get("PERIOD_MONTHS"));
                    monthPer = (monthPer + yearPer) / 3;
                    int totMonth = monthPer * 3;
                    int tot = 0;
                    Date endDt = null;
                }
                if (cummPeriod > 0) {
                    detailsHash.put("BEHAVES_LIKE", "FIXED");
                    detailsHash.put("PEROID", String.valueOf(cummPeriod));
                    detailsHash.put("CATEGORY_ID", detailsHash.get("CATEGORY_ID"));
                    detailsHash.put("PROD_ID", detailsHash.get("PROD_ID"));
                    List list = (List) ClientUtil.executeQuery("getDepProdIntPay", detailsHash);
                    if (list != null && list.size() > 0) {
                        detailsHash.putAll((HashMap) list.get(0));
                        InterestCalc interestCalc = new InterestCalc();
                        detailsHash.put("INTEREST_TYPE", "YEARLY");
                        detailsHash.put("CALC_OPENING_MODE", "CALC_OPENING_MODE");
                        amtDetHash = interestCalc.calcAmtDetails(detailsHash);
                        simpleAmt = CommonUtil.convertObjToDouble(amtDetHash.get("INTEREST")).doubleValue();
                    }
                }
                detailsHash.put("BEHAVES_LIKE", "CUMMULATIVE");
                double interest = simpleAmt + completeAmt;
                amtDetHash.put("INTEREST", new Double(interest));
                HashMap schemeMap = new HashMap();
                schemeMap.put("PROD_ID", detailsHash.get("PROD_ID"));
                List cummulativeRateLst = ClientUtil.executeQuery("getDepProdDetails", schemeMap);
                boolean doublingFlag = false;
                int doublingCount = 0;
                if (cummulativeRateLst != null && cummulativeRateLst.size() > 0) {
                    HashMap CummRtMap = new HashMap();
                    CummRtMap = (HashMap) cummulativeRateLst.get(0);
                    System.out.println("#$%#$%$#5CummRtMap:" + CummRtMap);

                    if (CummRtMap.containsKey("DOUBLING_SCHEME")) {
                        if (CommonUtil.convertObjToStr(CummRtMap.get("DOUBLING_SCHEME")).equals("Y")) {
                            doublingFlag = true;
                            if (CummRtMap.containsKey("DOUBLING_COUNT")) {
                                doublingCount = CommonUtil.convertObjToInt(CummRtMap.get("DOUBLING_COUNT"));
                            }
                        }
                    }
                }
                if (doublingFlag && doublingCount > 0) {
                    double dep_Amt = depAmt;
                    double interestAmt = dep_Amt * doublingCount;
                    amtDetHash.put("INTEREST", interestAmt);
                    double maturityAmt = interestAmt + dep_Amt;
                    amtDetHash.put("AMOUNT", maturityAmt);
                }
                //system.out.println("******** : "+detailsHash);
            } else {
                detailsHash.put("PEROID", String.valueOf(period));
            }
            //system.out.println(" set Amount ROI rate of interest param"+detailsHash);
        }
        if (!detailsHash.get("BEHAVES_LIKE").equals("CUMMULATIVE")) {
            //--- If the param is NUll then put category and Prod ,else put all the param value to the detailHash
            detailsHash.put("PEROID", String.valueOf(period));
            if (param == null) {
                detailsHash.put("CATEGORY_ID", CommonUtil.convertObjToStr(detailsHash.get("CATEGORY_ID")));
                detailsHash.put("PROD_ID", CommonUtil.convertObjToStr(detailsHash.get("PROD_ID")));
            } else {
                detailsHash.putAll(param);
            }
            HashMap discountedMap = new HashMap();
            discountedMap.put("PROD_ID", CommonUtil.convertObjToStr(detailsHash.get("PROD_ID")));
            List lstDiscounted = ClientUtil.executeQuery("getDepProdDetails", discountedMap);
            if (lstDiscounted != null && lstDiscounted.size() > 0) {
                discountedMap = (HashMap) lstDiscounted.get(0);
                detailsHash.put("DISCOUNTED_RATE", discountedMap.get("DISCOUNTED_RATE"));
            }
            List list = (List) ClientUtil.executeQuery("getDepProdIntPay", detailsHash);
            if (list != null && list.size() > 0) {
                detailsHash.putAll((HashMap) list.get(0));
                InterestCalc interestCalc = new InterestCalc();
                amtDetHash = interestCalc.calcAmtDetails(detailsHash);
            }
        }
        return amtDetHash;
    }

    public void updateInterestData() {
        tblDepositInterestApplication = new EnhancedTableModel((ArrayList) finalList, tableTitle);
    }

    public void setAccountsList(ArrayList rdList) {
        this.rdList = rdList;
    }

    public ArrayList getAccountsList() {
        return rdList;
    }

    /** To perform the necessary operation */
    public void doAction(HashMap finalMapUI) {
        TTException exception = null;
        log.info("In doAction()" + finalMapUI);
        try {
            doActionPerform(finalMapUI);
        } catch (Exception e) {
            System.out.println("##$$$##$#$#$#$# Exception e : " + e);
            log.info("Error In doAction()");
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            e.printStackTrace();
            if (e instanceof TTException) {
                exception = (TTException) e;
            }
        }
        if (exception != null) {
            parseException.logException(exception, true);
            setResult(actionType);
        }
    }

    public void addTargetTransactionList(String selectTransaction) {
//        String selectTransaction=CommonUtil.convertObjToStr(lstAvailableTransaction.getElementAt(selected));
        lstSelectedTransaction.addElement(selectTransaction);
//        lstAvailableTransaction.removeElementAt(selected);
        HashMap singleMap = new HashMap();
        singleMap.put("SELECTED_DEPOSITS", selectTransaction);
        newTransactionMap.put(selectTransaction, singleMap);
    }

    public void removeTargetALLTransactionList() {
        lstSelectedTransaction.removeAllElements();
    }

    public void removeTargetTransactionList(int selectTransaction) {
        //        String selectTransaction=CommonUtil.convertObjToStr(lstAvailableTransaction.getElementAt(selected));
        lstSelectedTransaction.removeElementAt(selectTransaction);
        //        lstAvailableTransaction.removeElementAt(selected);
        HashMap singleMap = new HashMap();
        singleMap.put("SELECTED_DEPOSITS", selectTransaction);
        newTransactionMap.put(selectTransaction, singleMap);
    }

    public String getListDeposits() {
        StringBuffer buffer = new StringBuffer();
        if (lstSelectedTransaction != null && lstSelectedTransaction.size() > 0) {
            for (int i = 0; i < lstSelectedTransaction.size(); i++) {
                buffer.append("'" + CommonUtil.convertObjToStr(lstSelectedTransaction.get(i)) + "'");
                if (i != lstSelectedTransaction.size() - 1) {
                    buffer.append(",");
                }
            }
        }
        return buffer.toString();
    }

    /** To perform the necessary action */
    private void doActionPerform(HashMap finalTableList) throws Exception {
        HashMap proxyResultMap = proxy.execute(finalTableList, map);
        setProxyReturnMap(proxyResultMap);
        setResult(getActionType());
    }

    public void resetForm() {
        resetTableValues();
        setChanged();
    }

    public void resetTableValues() {
        tblDepositInterestApplication.setDataArrayList(null, tableTitle);
    }

    /**
     * Getter for property tblDepositInterestApplication.
     * @return Value of property tblDepositInterestApplication.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblDepositInterestApplication() {
        return tblDepositInterestApplication;
    }

    /**
     * Setter for property tblDepositInterestApplication.
     * @param tblDepositInterestApplication New value of property tblDepositInterestApplication.
     */
    public void setTblDepositInterestApplication(com.see.truetransact.clientutil.EnhancedTableModel tblDepositInterestApplication) {
        this.tblDepositInterestApplication = tblDepositInterestApplication;
    }

    /**
     * Getter for property rdoPrizedMember_Yes.
     * @return Value of property rdoPrizedMember_Yes.
     */
    public boolean getRdoPrizedMember_Yes() {
        return rdoPrizedMember_Yes;
    }

    /**
     * Setter for property rdoPrizedMember_Yes.
     * @param rdoPrizedMember_Yes New value of property rdoPrizedMember_Yes.
     */
    public void setRdoPrizedMember_Yes(boolean rdoPrizedMember_Yes) {
        this.rdoPrizedMember_Yes = rdoPrizedMember_Yes;
    }

    /**
     * Getter for property rdoPrizedMember_No.
     * @return Value of property rdoPrizedMember_No.
     */
    public boolean getRdoPrizedMember_No() {
        return rdoPrizedMember_No;
    }

    /**
     * Setter for property rdoPrizedMember_No.
     * @param rdoPrizedMember_No New value of property rdoPrizedMember_No.
     */
    public void setRdoPrizedMember_No(boolean rdoPrizedMember_No) {
        this.rdoPrizedMember_No = rdoPrizedMember_No;
    }

    /**
     * Getter for property finalList.
     * @return Value of property finalList.
     */
    public java.util.List getFinalList() {
        return finalList;
    }

    /**
     * Setter for property finalList.
     * @param finalList New value of property finalList.
     */
    public void setFinalMap(HashMap finalList) {
        this.finalMap = finalList;
    }

    public HashMap getFinalMap() {
        return finalMap;
    }

    /**
     * Setter for property finalList.
     * @param finalList New value of property finalList.
     */
    public void setFinalList(java.util.List finalList) {
        this.finalList = finalList;
    }

    /**
     * Getter for property txtProductID.
     * @return Value of property txtProductID.
     */
    public java.lang.String getTxtProductID() {
        return txtProductID;
    }

    /**
     * Setter for property txtProductID.
     * @param txtProductID New value of property txtProductID.
     */
    public void setTxtProductID(java.lang.String txtProductID) {
        this.txtProductID = txtProductID;
    }

    /**
     * Getter for property result.
     * @return Value of property result.
     */
    public int getResult() {
        return result;
    }

    /**
     * Setter for property result.
     * @param result New value of property result.
     */
    public void setResult(int result) {
        this.result = result;
    }

    /**
     * Getter for property actionType.
     * @return Value of property actionType.
     */
    public int getActionType() {
        return actionType;
    }

    /**
     * Setter for property actionType.
     * @param actionType New value of property actionType.
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    /**
     * Getter for property tableTitle.
     * @return Value of property tableTitle.
     */
    public java.util.ArrayList getTableTitle() {
        return tableTitle;
    }

    /**
     * Getter for property txtTokenNo.
     * @return Value of property txtTokenNo.
     */
    public java.lang.String getTxtTokenNo() {
        return txtTokenNo;
    }

    /**
     * Setter for property txtTokenNo.
     * @param txtTokenNo New value of property txtTokenNo.
     */
    public void setTxtTokenNo(java.lang.String txtTokenNo) {
        this.txtTokenNo = txtTokenNo;
    }

    /**
     * Getter for property calFreqAccountList.
     * @return Value of property calFreqAccountList.
     */
    public java.util.List getCalFreqAccountList() {
        return calFreqAccountList;
    }

    /**
     * Setter for property calFreqAccountList.
     * @param calFreqAccountList New value of property calFreqAccountList.
     */
    public void setCalFreqAccountList(java.util.List calFreqAccountList) {
        this.calFreqAccountList = calFreqAccountList;
    }

    /**
     * Getter for property cboSIProductId.
     * @return Value of property cboSIProductId.
     */
    public java.lang.String getCboSIProductId() {
        return cboSIProductId;
    }

    /**
     * Setter for property cboSIProductId.
     * @param cboSIProductId New value of property cboSIProductId.
     */
    public void setCboSIProductId(java.lang.String cboSIProductId) {
        this.cboSIProductId = cboSIProductId;
    }

    /**
     * Getter for property cbmSIProductId.
     * @return Value of property cbmSIProductId.
     */
    public ComboBoxModel getCbmSIProductId() {
        return cbmSIProductId;
    }

    /**
     * Getter for property lstSelectedTransaction.
     * @return Value of property lstSelectedTransaction.
     */
    public javax.swing.DefaultListModel getLstSelectedTransaction() {
        return lstSelectedTransaction;
    }

    /**
     * Setter for property lstSelectedTransaction.
     * @param lstSelectedTransaction New value of property lstSelectedTransaction.
     */
    public void setLstSelectedTransaction(javax.swing.DefaultListModel lstSelectedTransaction) {
        this.lstSelectedTransaction = lstSelectedTransaction;
    }

    /**
     * Getter for property cashtoTransferMap.
     * @return Value of property cashtoTransferMap.
     */
    public HashMap getCashtoTransferMap() {
        return cashtoTransferMap;
    }

    /**
     * Setter for property cashtoTransferMap.
     * @param cashtoTransferMap New value of property cashtoTransferMap.
     */
    public void setCashtoTransferMap(HashMap cashtoTransferMap) {
        this.cashtoTransferMap = cashtoTransferMap;
    }

    /**
     * Getter for property cbmOAProductID.
     * @return Value of property cbmOAProductID.
     */
    public ComboBoxModel getCbmOAProductID() {
        return cbmOAProductID;
    }

    /**
     * Setter for property cbmOAProductID.
     * @param cbmOAProductID New value of property cbmOAProductID.
     */
    public void setCbmOAProductID(ComboBoxModel cbmOAProductID) {
        this.cbmOAProductID = cbmOAProductID;
    }
}