/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DepositClosingOB.java
 *
 * Created on May 20, 2004, 11:14 AM
 */

/*
 * TODO
 * a. In setClosureTO() method, set all values using below code as guideline
i. this.setIntDrawn(String.valueOf(CommonUtil.convertObjToDouble(intDrawnMap.get("INT_AMT")).doubleValue()));
 *
 *
 */
package com.see.truetransact.ui.deposit.closing;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.transferobject.deposit.closing.DepositWithDrawalTO;
import com.see.truetransact.transferobject.deposit.closing.SubDepositTO;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.commonutil.interestcalc.DateDifference;
import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.transferobject.product.deposits.DepositsProductIntPayTO;
import com.see.truetransact.transferobject.product.loan.LoanProductAccHeadTO;
import com.see.truetransact.transferobject.servicetax.servicetaxdetails.ServiceTaxDetailsTO;
import com.see.truetransact.ui.common.servicetax.ServiceTaxCalculation;
//import com.see.truetransact.uicomponent.COptionPane;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
//import java.util.Observable;
import java.util.GregorianCalendar;
//import java.util.Calendar;
import com.see.truetransact.uicomponent.CObservable;

import com.see.truetransact.ui.common.transaction.TransactionOB;
//import com.see.truetransact.commonutil.interestcalc.CommonCalculateInterest ;
//import com.see.truetransact.transferobject.product.deposits.DepositsProductIntPayTO ;
//import com.see.truetransact.transferobject.common.transaction.TransactionTO;
import com.see.truetransact.ui.transaction.agentCommisionDisbursal.AgentCommisionDisbursalOB;
import java.text.DecimalFormat;

/**
 *
 * @author  Pinky
 *
 */
public class DepositClosingOB extends CObservable {

    private TransactionOB transactionOB;
    private double lienAmount;
    private double freezeAmount;
    private static DepositClosingOB depositClosingOB;
    private HashMap map, lookupMap;
    private ProxyFactory proxy;
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private ArrayList key, value;
    private TableModel tbmSubDeposit, tbmPartialWithdrawal;
    private ArrayList subDepositTOs;
    private int actionType;
    private int result;
    private String lblStatus;
    private String customerID;
    private String constitution;
    private String category;
    private String modeOfSettlement;
    private String depositActName;
    private String prinicipal;
    private String period;
    private String depositDate;
    private String maturityDate;
    private String maturityValue;
    private String rateOfInterest;
    private String intPaymentFreq;
    private String balanceDeposit;
    private String withDrawn;
    private String intCr;
    private String intDrawn;
    private String tdsCollected;
    private String lastIntAppDate;
    private String lienFreezeAmt;
    private String noInstPaid;
    private String noInstDue;
    private String balance;
    private String premClos;
    private HashMap chargeMap;
    private List Chargelst;
    private HashMap bothRecPayMap = null;
    private HashMap serviceTax_Map=null;
    private String lblServiceTaxval="";
    private ServiceTaxDetailsTO objservicetaxDetTo;
    private String rec_recivable  = "";
    private String depositPeriodWK="";    
    private String groupDepositProd = "N";  // Added by nithya on 09-10-2017 for group deposit changes
    private String groupDepositRecInt = ""; // Added by nithya on 09-10-2017 for group deposit changes
    private String isSlabWiseDailyDeposit = "";
    private String txtSingleTransId="";

    public String getTxtSingleTransId() {
        return txtSingleTransId;
    }

    public void setTxtSingleTransId(String txtSingleTransId) {
        this.txtSingleTransId = txtSingleTransId;
    }

    public String getDepositPeriodWK() {
        return depositPeriodWK;
    }

    public void setDepositPeriodWK(String depositPeriodWK) {
        this.depositPeriodWK = depositPeriodWK;
    }

     public HashMap getBothRecPayMap() {
        return bothRecPayMap;
    }

    public void setBothRecPayMap(HashMap bothRecPayMap) {
        this.bothRecPayMap = bothRecPayMap;
    }

    public List getChargelst() {
        return Chargelst;
    }

    public void setChargelst(List Chargelst) {
        this.Chargelst = Chargelst;
    }
    
    public String getPremClos() {
        return premClos;
    }

    public void setPremClos(String premClos) {
        this.premClos = premClos;
        System.out.println("premClos in OB" + premClos);
    }

    public String getPrev_interest() {
        return prev_interest;
    }

    public void setPrev_interest(String prev_interest) {
        this.prev_interest = prev_interest;
    }
    private String prev_interest = "";
    private String closingIntCr = "";
    private String closingIntDb = "";
    private String payReceivable = "";
    private String permanentPayReceivable = "";
    private String closingTds = "";
    private String closingDisbursal = "";
    //2lines added 26.03.2007
    private String RateApplicable = "";
    private String PenaltyPenalRate = "";
    private double cummWithDrawnAmount = 0;
    private double cummIntCredit = 0;
    private double cummIntDebit = 0;
    private double cummTDCollected = 0;
    private double cummLienFreezeAmount = 0;
    private double cummBalance = 0;
    private double cummClosingCr = 0;
    private double cummClosingDb = 0;
    private double cummClosingTdsCollected = 0;
    private double cummClosingDisbursal = 0;
    private double cummClosingRateApplicable = 0;
    private double cummClosingPenaltyPenalRate = 0;
    private String tdsAcHd = null;
    private String cboProductId = "";
    private String lblClosingType = "";
    private String txtDepositNo = "";
    private String subDepositNo = "";
    private ComboBoxModel cbmProductId;
    private double unitAmt = 0;
    private double prematureRunPeriod = 0;
    private long periodNoOfDays = 0;
    private String partialAllowed = "";
    private String noOfUnitsWithDrawn = "";
    private String noOfUnitsAvai = "";
    private String prematureClosingRate = "";
    private String prematureClosingDate = "";
    private String txtAmtWithDrawn = "";
    private String presentUnitInt = "";
    private String settlementUnitInt = "";
    private String noOfUnits = "";
    private String depositRunPeriod = "";
    private String prodID = "";
    private String noOfWithDrawalUnits = "";
    private String withdrawalTOStatus = "";
    private String txtPWNoOfUnits = "";
    //one line added 27.03.2007
    private String setPenaltyPenalRate = "";
    private String typeOfDep = "";
    private boolean rdoPenaltyPenalRate_yes = false;
    private boolean rdoPenaltyPenalRate_no = false;
    private boolean rdoTypeOfDeposit_Yes = false;
    private boolean rdoTypeOfDeposit_No = false;
    private boolean rdoTransfer_Yes = false;
    private boolean rdoTransfer_No = false;
    private String transfer_out_mode = null;
    private String transferBranch_code = null;
    private LinkedHashMap allowedTransactionDetailsTO = null;
    private LinkedHashMap transactionDetailsTO = null;
    private LinkedHashMap deletedTransactionDetailsTO = null;
    private final String DELETED_TRANS_TOs = "DELETED_TRANS_TOs";
    private final String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs";
    private ArrayList withdrawalTOs, deletePWList;
    DepositWithDrawalTO objPartialWithDrawalTO;
    private HashMap authorizeMap;
    private HashMap behavesMap = new HashMap();
    public HashMap oldTransactionMap = new HashMap();
    private HashMap oldTransMap = new HashMap();
    private HashMap ltdClosingMap = new HashMap();
    private double disburseAmt = 0.0;
    private double balanceAmt = 0.0;
    private String lblReceive = null;
    private String lblBalanceDeposit = null;
    private boolean rateOfIntCalculated = false;
    private String transStatus = "";
    private String penaltyInt = null;
    private String transProdId = "";
    private String actualPeriodRun = "";
    private String delayedInstallments = "";
    private String chargeAmount = "";
    private String lblPayRecDet;
    private boolean rdoYesButton;
    private boolean rdoNoButton;
    private int ViewTypeDet = -1;
    private String totalBalance = "";
    private Date curDate = null;
    public String depositPenalAmt = null;
    public String depositPenalMonth = null;
    private String deathClaim = "";
    private boolean deathFlag = false;
    private String deathClaimAmt = "";
    private String agentCommisionRecoveredValue = "";
    private String lstProvDt = "";
    public boolean normalClosing = false;
    AgentCommisionDisbursalOB agentCommisionDisbursalOB = new AgentCommisionDisbursalOB();
    private boolean flPtWithoutPeriod = false;
    private HashMap serviceChargeMap = new HashMap();
    //The following variable added by Rajesh
    private String termLoanAdvanceActNum = "";
    private String ltdDeposit = "";
    private String interestRound = "";
    DecimalFormat df = new DecimalFormat("##.00");
    //Added by chithra on 22-04-14 for addittional Interest
    private String lblMaturityPeriod;
    private String lblAddIntrstRteVal;
    private String lblAddIntRtAmtVal;
    private String addIntLoanAmt;
    private String DOUBLING_SCHEME_BEHAVES_LIKE="";
    private final static double Avg_Millis_Per_Month=365.24*24*60*60*1000 / 12;
    private String chkBehavesLike ="";
    private String authrize2 ="";
    private double totalCharge = 0;
    private String specialRDCompleted = "N"; // 03-06-2020
    private String dailyDepositLoanPreClose = "N";
    private String dailyDepositLoanPreCloseROI = "";
    
    public String getInterestRound() {
        return interestRound;
    }

    public void setInterestRound(String interestRound) {
        this.interestRound = interestRound;
    }

    /** Creates a new instance of DepositClosingOB */
    public DepositClosingOB() throws Exception {
        curDate = ClientUtil.getCurrentDate();
        transactionOB = new TransactionOB();
        map = new HashMap();
        map.put(CommonConstants.JNDI, "DepositClosingJNDI");
        map.put(CommonConstants.HOME, "deposit.closing.DepositClosingHome");
        map.put(CommonConstants.REMOTE, "deposit.closing.DepositClosing");
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            System.err.println("Exception " + e + "Caught");
            e.printStackTrace();
        }
        fillDropDown();
        setTableModel();
        this.subDepositTOs = new ArrayList();
        this.withdrawalTOs = new ArrayList();
        this.deletePWList = new ArrayList();
    }

    public void setTableModel() {
        ArrayList columnHeader = new ArrayList();
        columnHeader.add("Select");
        columnHeader.add("Deposit Sub No");
        columnHeader.add("Amount");
        columnHeader.add("Maturity Date");
        ArrayList data = new ArrayList();

        tbmSubDeposit = (new TableModel(data, columnHeader) {

            public boolean isCellEditable(int rowIndex, int mColIndex) {
                if (mColIndex == 0) {
                    return true;
                }
                return false;
            }
        });

        columnHeader = new ArrayList();
        columnHeader.add("Withdrawal NO");
        columnHeader.add("Amount");
        columnHeader.add("No of Units");

        this.tbmPartialWithdrawal = new TableModel(data, columnHeader);
    }

    public static DepositClosingOB getInstance() throws Exception {
        DepositClosingOB depositClosingOB = new DepositClosingOB();
        return depositClosingOB;

    }

    private void fillDropDown() throws Exception {
        lookupMap = new HashMap();
        lookupMap.put(CommonConstants.JNDI, "LookUpJNDI");
        lookupMap.put(CommonConstants.HOME, "common.lookup.LookUpHome");
        lookupMap.put(CommonConstants.REMOTE, "common.lookup.LookUp");

        final HashMap param = new HashMap();

        param.put(CommonConstants.MAP_NAME, "getDepositProducts");
        param.put(CommonConstants.PARAMFORQUERY, null);
        //        System.out.println("**paramMap :"+param);
        HashMap lookupValues = ClientUtil.populateLookupData(param);

        fillData((HashMap) lookupValues.get(CommonConstants.DATA));
        cbmProductId = new ComboBoxModel(key, value);

    }

    private void fillData(HashMap keyValue) throws Exception {
        key = (ArrayList) keyValue.get(CommonConstants.KEY);
        value = (ArrayList) keyValue.get(CommonConstants.VALUE);
    }

    /** To get data for comboboxes */
    private HashMap populateDataLocal(HashMap obj) throws Exception {
        HashMap keyValue = proxy.executeQuery(obj, lookupMap);
        return keyValue;
    }

    // Setter method for cboProductId
    public void setCboProductId(String cboProductId) {
        this.cboProductId = cboProductId;
        setChanged();
    }
    // Getter method for cboProductId

    public String getCboProductId() {
        return this.cboProductId;
    }

    // Setter method for txtDepositNo
    public void setTxtDepositNo(String txtDepositNo) {
        this.txtDepositNo = txtDepositNo;
        setChanged();
    }
    // Getter method for txtDepositNo

    public String getTxtDepositNo() {
        return this.txtDepositNo;
    }

    /** Getter for property cbmProductId.
     * @return Value of property cbmProductId.
     *
     */
    public ComboBoxModel getCbmProductId() {
        return cbmProductId;
    }

    /** Setter for property cbmProductId.
     * @param cbmProductId New value of property cbmProductId.
     *
     */
    public void setCbmProductId(ComboBoxModel cbmProductId) {
        this.cbmProductId = cbmProductId;
    }

    public String getAccountHead(String prodId) {
        HashMap whereMap = new HashMap();
        whereMap.put("PRODID", prodId);
        List list = ClientUtil.executeQuery("Closing.getDepositAcHd", whereMap);
        if (list != null && list.size() > 0) {
            whereMap = (HashMap) list.get(0);
            prodId = (String) whereMap.get("ACHDID");
            prodId += "[" + (String) whereMap.get("ACHDDESC") + "]";
            setPartialAllowed((String) whereMap.get("PARTIAL_WITHDRAWAL"));
            setUnitAmt(CommonUtil.convertObjToDouble(whereMap.get("UNIT_AMT")).doubleValue());
            return prodId;
        }
        return "";
    }

    public boolean getSubDepositNos(HashMap paramMap) {
        ArrayList dataList = new ArrayList();
        ArrayList data;
        SubDepositTO obj;
        List list = null;
        if (getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            list = ClientUtil.executeQuery("getSelectSubDepositTO", paramMap);
        } else if (getActionType() == ClientConstants.ACTIONTYPE_EDIT
                || getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
                || getActionType() == ClientConstants.ACTIONTYPE_DELETE
                || getActionType() == ClientConstants.ACTIONTYPE_REJECT
                || getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
            list = ClientUtil.executeQuery("getClosedSubDepositTO", paramMap);
        }

        if (list != null && list.size() > 0) {
            int size = list.size();
            subDepositTOs = (ArrayList) list;
            for (int i = 0; i < size; i++) {
                obj = (SubDepositTO) subDepositTOs.get(i);
                data = new ArrayList();
                data.add(new Boolean(true));
                data.add(obj.getDepositSubNo());
                data.add(obj.getDepositAmt());
                data.add(obj.getMaturityDt());
                dataList.add(data);
                setDepositDate(DateUtil.getStringDate(obj.getDepositDt()));
                setMaturityDate(DateUtil.getStringDate(obj.getMaturityDt()));
                if (obj.getStatus() != null && obj.getStatus().equals("LIEN")) {
                    setTypeOfDep("LTD");
                } else {
                    setTypeOfDep("NOLTD");
                }
            }
            this.tbmSubDeposit.setData(dataList);
            this.tbmSubDeposit.fireTableDataChanged();
            return false;
        } else {
            ClientUtil.showMessageWindow("Invalid Account No");
            return true;
        }
    }

    public boolean getSubDepositNosLien(HashMap paramMap) {
        ArrayList dataList = new ArrayList();
        ArrayList data;
        SubDepositTO obj;
        List list = null;
        if (getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            list = ClientUtil.executeQuery("getSelectSubDepositTO", paramMap);
        } else if (getActionType() == ClientConstants.ACTIONTYPE_EDIT
                || getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
                || getActionType() == ClientConstants.ACTIONTYPE_DELETE
                || getActionType() == ClientConstants.ACTIONTYPE_REJECT
                || getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
            list = ClientUtil.executeQuery("getClosedSubDepositTO", paramMap);
        }

        if (list != null && list.size() > 0) {
            int size = list.size();
            subDepositTOs = (ArrayList) list;
            for (int i = 0; i < size; i++) {
                obj = (SubDepositTO) subDepositTOs.get(i);
                data = new ArrayList();
                data.add(new Boolean(true));
                data.add(obj.getDepositSubNo());
                data.add(obj.getDepositAmt());
                data.add(obj.getMaturityDt());
//                dataList.add(data);
                setDepositDate(DateUtil.getStringDate(obj.getDepositDt()));
                setMaturityDate(DateUtil.getStringDate(obj.getMaturityDt()));
                if (obj.getStatus() != null && obj.getStatus().equals("LIEN")) {
                    setTypeOfDep("LTD");

                } else {
                    setTypeOfDep("NOLTD");
                }
            }
//            this.tbmSubDeposit.setData(dataList);
//            this.tbmSubDeposit.fireTableDataChanged();
            return false;
        } else {
            ClientUtil.showMessageWindow("Invalid Account No");
            return true;
        }
    }

    public void getData(HashMap paramMap) throws Exception {
        System.out.println("paramMap......................."+paramMap);
        HashMap mapData = new HashMap();
        HashMap tdsData = new HashMap();
        HashMap closureData = new HashMap();

        mapData = proxy.executeQuery(paramMap, map);
        System.out.println("getDataMapData : " + mapData);
        HashMap depositMap=new HashMap();
        if (mapData != null && !mapData.equals("") && mapData.size() > 0) {
            setDepositDetails((HashMap) ((List) mapData.get("DEPOSIT_DETAILS")).get(0));
            if(mapData.containsKey("DEPOSIT_DETAILS")){
            depositMap=(HashMap) ((List) mapData.get("DEPOSIT_DETAILS")).get(0);
            }
            //            setCustomerDetails((HashMap)((List)mapData.get("DEPOSIT_DETAILS")).get(0)) ;
            if (mapData.containsKey("DEPOSIT_CLOSE_DETAILS")) {
                //added by Chithra on 09-06-14
                 Date depDt = null;
                long diffDays = 0;
                depDt = DateUtil.getDateMMDDYYYY(getDepositDate());
                diffDays = DateUtil.dateDiff(depDt, curDate);
                HashMap whrMap = new HashMap();     
                List lst = (List)mapData.get("DEPOSIT_CLOSE_DETAILS");
                if(lst!=null && lst.size()>0){
                HashMap depMap = (HashMap) lst.get(0);
                whrMap.put("PROD_ID", depMap.get("PROD_ID"));
                }else{
                  whrMap.put("PROD_ID", paramMap.get("PRODUCT_ID"));
                }
                List lstScheme = ClientUtil.executeQuery("getSchemeIntroDate", whrMap);
                long mindte = 0;
                if (lstScheme != null && lstScheme.size() > 0) {
                    HashMap schemeMap = (HashMap) lstScheme.get(0);
                    if (schemeMap!=null && schemeMap.containsKey("MIN_DEPOSIT_PERIOD")) {
                        mindte = CommonUtil.convertObjToLong(CommonUtil.convertObjToStr(schemeMap.get("MIN_DEPOSIT_PERIOD")));
                    }
                   
                }
               //Added by Chithra 
                if (getLblClosingType().equals(CommonConstants.PREMATURE_CLOSURE)) {
                    HashMap con_map = new HashMap();
                    con_map.put("PROD_ID", paramMap.get("PRODUCT_ID"));
                    lstScheme = ClientUtil.executeQuery("checkIntRateEditable", con_map);
                    if (lstScheme != null && lstScheme.size() > 0) {
                        HashMap schemeMap = (HashMap) lstScheme.get(0);
                        if (schemeMap != null && schemeMap.containsKey("DIFF_ROI_YES_NO")) {
                            String diffRoi = CommonUtil.convertObjToStr(schemeMap.get("DIFF_ROI_YES_NO"));
                            if (diffRoi != null && diffRoi.equals("Y") && schemeMap.containsKey("PREMATURE_CLOSURE_APPLY")) {
                                String preMatAppl = CommonUtil.convertObjToStr(schemeMap.get("PREMATURE_CLOSURE_APPLY"));
                                if (preMatAppl != null  && schemeMap.containsKey("PRE_MAT_INT_TYPE")) {
                                    String preMatIntType = CommonUtil.convertObjToStr(schemeMap.get("PRE_MAT_INT_TYPE"));
                                    if (preMatIntType != null && preMatIntType.equals("COMPOUND")) {
                                        DOUBLING_SCHEME_BEHAVES_LIKE = "CUMMULATIVE";
                                    } else if (preMatIntType != null && preMatIntType.equals("SIMPLE")) {
                                        DOUBLING_SCHEME_BEHAVES_LIKE = "FIXED";
                                    }
                                }
                            }
                        }
                    }

                }
                if (diffDays >= mindte) {
                    // Added by nithya on 18-09-2019 for KD 606 - RD Closure - Premature And Defaulter Payment Needs Different Settings
                    String depositBehavesLike = "";
                    String irregularRDOtherCharge = "";
                    String intApplyForIrregularRD = "Y";
                    String specialRD = "N";
                    HashMap rdPaidInst = new HashMap();
                    rdPaidInst.put("DEPOSIT_NO", paramMap.get("DEPOSITNO"));
                    List behavesList = ClientUtil.executeQuery("getCustDepositNoBehavesLike", rdPaidInst);
                    if (behavesList != null && behavesList.size() > 0) {
                        HashMap behavesMap = (HashMap) behavesList.get(0);
                        depositBehavesLike = CommonUtil.convertObjToStr(behavesMap.get("BEHAVES_LIKE"));
                        irregularRDOtherCharge = CommonUtil.convertObjToStr(behavesMap.get("RD_CLOSING_SB_DEPOSIT_ROI"));
                        intApplyForIrregularRD = CommonUtil.convertObjToStr(behavesMap.get("INT_APPLY_FOR_IRREGULAR_RD"));
                        specialRD = CommonUtil.convertObjToStr(behavesMap.get("SPECIAL_RD"));
                    }
                    if (depositBehavesLike.equalsIgnoreCase("RECURRING") && irregularRDOtherCharge.equalsIgnoreCase("Y")) {
                        int paidMonths = 0;
                        int actualMonths = 0;
                        int no_week = 0;
                        double instAmount = 0.0;                        
                        List rdPaidInstList = ClientUtil.executeQuery("getRdPaidInstllments", rdPaidInst);
                        if (rdPaidInstList != null && rdPaidInstList.size() > 0) {
                            rdPaidInst = (HashMap) rdPaidInstList.get(0);
                            actualMonths = CommonUtil.convertObjToInt(rdPaidInst.get("TOTAL_INSTALLMENTS"));
                            paidMonths = CommonUtil.convertObjToInt(rdPaidInst.get("TOTAL_INSTALL_PAID"));
                            instAmount = CommonUtil.convertObjToDouble(rdPaidInst.get("DEPOSIT_AMT")).doubleValue();
                            no_week = CommonUtil.convertObjToInt(rdPaidInst.get("DEPOSIT_PERIOD_WK"));
                        }
                        if(intApplyForIrregularRD.equalsIgnoreCase("N")){
                           paramMap.put("INT_APPLY_FOR_IRREGULAR_RD","N"); 
                        }
                        if (getLblClosingType().equals(CommonConstants.PREMATURE_CLOSURE)
                                || (getLblClosingType().equals(CommonConstants.NORMAL_CLOSURE) && actualMonths != paidMonths)) {
                            setClosingIntRateRD((List) mapData.get("DEPOSIT_CLOSE_DETAILS"), paramMap);
                        } else {
                            setClosingIntRate((List) mapData.get("DEPOSIT_CLOSE_DETAILS"));
                        }
                        if(specialRD.equalsIgnoreCase("Y")){ //Added by nithya on 28-03-2020 for KD-1535
                            HashMap roiParamMap = new HashMap();
                            roiParamMap.put("DEPOSIT_NO", this.getTxtDepositNo());
                            roiParamMap.put("PROD_ID", getProdID());
                            List specialRDCompleteLst = ClientUtil.executeQuery("getSpecialRDCompletedStatus", roiParamMap);
                            if(specialRDCompleteLst != null && specialRDCompleteLst.size() > 0){
                                HashMap specialRDMap = (HashMap)specialRDCompleteLst.get(0);
                                if(specialRDMap.containsKey("COMPLETESTATUS") && specialRDMap.get("COMPLETESTATUS") != null){
                                    if(CommonUtil.convertObjToStr(specialRDMap.get("COMPLETESTATUS")).equalsIgnoreCase("Y")){
                                        //ClientUtil.showAlertWindow("Special RD installments Completed. Cannot close the account.");
                                        //return;
                                        setSpecialRDCompleted("Y");
                                    }
                                }
                            }
                        }
                    }else{
                        setClosingIntRate((List) mapData.get("DEPOSIT_CLOSE_DETAILS"));
                    }                    
                }
            } else if (mapData.containsKey("DEPOSIT_CLOSE_DETAILS_FLOATING")) {
                setClosingFloatingDetails((HashMap) mapData.get("DEPOSIT_CLOSE_DETAILS_FLOATING"));
            }
            List list = (List) mapData.get("TransactionTO");
            if (mapData.containsKey("TRANS_DETAILS")) {
                //commented by rishad 05/12/1016
            //    oldTransactionMap.put("TRANS_DETAILS", mapData.get("TRANS_DETAILS"));
                //The following block added by Rajesh
                if (mapData.get("TRANS_DETAILS") instanceof String) {
                    termLoanAdvanceActNum = CommonUtil.convertObjToStr(mapData.get("TRANS_DETAILS"));
                }
            }
            if (mapData.containsKey("DEPOSIT_CLOSING_AMT_DETAILS_CASH"))//depositamount giving cash.
            {
                oldTransactionMap.put("DEPOSIT_CLOSING_AMT_DETAILS_CASH", mapData.get("DEPOSIT_CLOSING_AMT_DETAILS_CASH"));
            }

            if (mapData.containsKey("DEPOSIT_CLOSING_INT_DETAILS_CASH"))//interest amount paid to payable
            {
                oldTransactionMap.put("DEPOSIT_CLOSING_INT_DETAILS_CASH", mapData.get("DEPOSIT_CLOSING_INT_DETAILS_CASH"));
            }

            if (mapData.containsKey("DEPOSIT_CLOSING_PAY_INT_DETAILS_CASH"))//interest amount payable to cash
            {
                oldTransactionMap.put("DEPOSIT_CLOSING_PAY_INT_DETAILS_CASH", mapData.get("DEPOSIT_CLOSING_PAY_INT_DETAILS_CASH"));
            }

            if (mapData.containsKey("DEPOSIT_CLOSING_AMT_DETAILS_TRANSFER"))//deposit amount giving transfer
            {
                oldTransactionMap.put("DEPOSIT_CLOSING_AMT_DETAILS_TRANSFER", mapData.get("DEPOSIT_CLOSING_AMT_DETAILS_TRANSFER"));
            }

            if (mapData.containsKey("DEPOSIT_CLOSING_INT_DETAILS_TRANSFER"))//interest amount paid to payable
            {
                oldTransactionMap.put("DEPOSIT_CLOSING_INT_DETAILS_TRANSFER", mapData.get("DEPOSIT_CLOSING_INT_DETAILS_TRANSFER"));
            }

            if (mapData.containsKey("DEPOSIT_CLOSING_PAY_INT_DETAILS_TRANSFER"))//interest amount payable to transfer
            {
                oldTransactionMap.put("DEPOSIT_CLOSING_PAY_INT_DETAILS_TRANSFER", mapData.get("DEPOSIT_CLOSING_PAY_INT_DETAILS_TRANSFER"));
            }

            if (mapData.containsKey("DEPOSIT_CLOSING_DELAY_DETAILS_TRANSFER"))//recurring delayed chargeamount
            {
                oldTransactionMap.put("DEPOSIT_CLOSING_DELAY_DETAILS_TRANSFER", mapData.get("DEPOSIT_CLOSING_DELAY_DETAILS_TRANSFER"));
            }

            if (mapData.containsKey("DEPOSIT_CLOSING_TDS_DETAILS_TRANSFER"))//TDS deducted amount.
            {
                oldTransactionMap.put("DEPOSIT_CLOSING_TDS_DETAILS_TRANSFER", mapData.get("DEPOSIT_CLOSING_TDS_DETAILS_TRANSFER"));
            }

            mapData.put("DEPOSIT_NO", paramMap.get("DEPOSITNO"));
            if (!mapData.containsKey("DEPOSIT_CLOSE_DETAILS_FLOATING")) {
                setClosingDetails(mapData);
            }
            transactionOB.setDetails(list);
             //added By Chithra on 22-04-14
           
             Date currDt = null, strMatDt = null;
                            long diff = 0;
                            strMatDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("MATURITY_DT")));
                            currDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(paramMap.get("CURRENT_DT")));
                            diff = DateUtil.dateDiff(strMatDt, currDt);
            if (mapData!=null&&mapData.containsKey("RENEWAL_AND_MATURED_DETAILS")&& diff>0) {
                String renewalOpt = "";
                String matuOpt = "";
                String notRenewMatu = "";
                HashMap addIntdet = new HashMap();
                String lienPrdId="";
                String lienAmt = "";
                if (rdoTypeOfDeposit_No) {
                    HashMap whereMap = new HashMap();
                    whereMap.put("DEPOSIT_NO", this.getTxtDepositNo());
                    List list1 = ClientUtil.executeQuery("getLoanAccountDetailsForClosing", whereMap);
                    if (list1 != null && list1.size() > 0) {
                        HashMap newMap = (HashMap) list1.get(0);
                        lienPrdId = CommonUtil.convertObjToStr(newMap.get("LIEN_PROD_ID"));
                        lienAmt = CommonUtil.convertObjToStr(newMap.get("LIEN_AMOUNT"));
                    }
                }
                    addIntdet = (HashMap) mapData.get("RENEWAL_AND_MATURED_DETAILS");//addDetList.get(0);
                    renewalOpt = CommonUtil.convertObjToStr(addIntdet.get("DATE_OF_RENEWAL"));
                    matuOpt = CommonUtil.convertObjToStr(addIntdet.get("DATE_OF_MATURITY"));
                    notRenewMatu = CommonUtil.convertObjToStr(addIntdet.get("ELIGIBLE_TWO_RATE"));
                    String SBorDep =CommonUtil.convertObjToStr(addIntdet.get("INT_RATE_APPLIED_OVERDUE"));
                  String   closureYn=CommonUtil.convertObjToStr(addIntdet.get("CLOSURE_INT_YN"));
                  if(closureYn!=null&&closureYn.equalsIgnoreCase("Y"))
                  {
                    if (SBorDep != null && SBorDep.equalsIgnoreCase("Y")) {// added by chithra 17-05-14
                            setLblMaturityPeriod(CommonUtil.convertObjToStr(diff) + " " + "Days");
                            HashMap ROIDet = new HashMap();
                            if (mapData.containsKey("DEPOSIT_ADDITIONAL_INTEREST_DETAILS")) {
                                List DataList = (List) mapData.get("DEPOSIT_ADDITIONAL_INTEREST_DETAILS");
                                if(DataList!=null&&DataList.size()>0)
                                {
                                ROIDet = (HashMap) DataList.get(0);
                                if (ROIDet != null) {
                                    setLblAddIntrstRteVal(CommonUtil.convertObjToStr(ROIDet.get("ROI")));
                                }
                                    if (rdoTypeOfDeposit_No) {
                                        HashMap whereAddIntdet = new HashMap();
                                        whereAddIntdet.put("DEPOSIT_DT", paramMap.get("CURRENT_DT"));
                                        whereAddIntdet.put("PRODID", lienPrdId);
                                        whereAddIntdet.put("CUSTID", getCustomerID());
                                        whereAddIntdet.put("PERIOD", diff);
                                        whereAddIntdet.put("CATEGORY_ID", depositMap.get("CATEGORY_ID"));
                                        whereAddIntdet.put("DEPOSITNO", this.getTxtDepositNo());
                                        List addInterest = ClientUtil.executeQuery("getDepositClosingLoanInterestDetails", whereAddIntdet);
                                        if (addInterest != null && addInterest.size() > 0) {
                                            HashMap intDet = (HashMap) addInterest.get(0);
                                            if (intDet != null && intDet.containsKey("ROI")) {
                                                String ROI = CommonUtil.convertObjToStr(intDet.get("ROI"));
                                                setAddIntLoanAmt(calcAddIntAmtForLoan(strMatDt, currDt, ROI, lienAmt));
                                            }
                                        }
                                    }
                               
                                setLblAddIntRtAmtVal(calcAddIntAmt(strMatDt,currDt));
                                
                                }
                                else
                                {
                                      setLblAddIntrstRteVal("");
                                      setLblMaturityPeriod("");
                                      setLblAddIntRtAmtVal("");
                                      setAddIntLoanAmt("");
                                }
                            }
                      }
                      else  if (SBorDep != null && SBorDep.equalsIgnoreCase("N")) {
                          String DateVAl="";
                        if (renewalOpt != null && renewalOpt.length() > 0 && renewalOpt.equalsIgnoreCase("Y")) {
                             DateVAl= CommonUtil.convertObjToStr(paramMap.get("CURRENT_DT"));
                            setLblMaturityPeriod(CommonUtil.convertObjToStr(diff) + " " + "Days");

                            HashMap ROIDet = new HashMap();
                            if (mapData.containsKey("DEPOSIT_ADDITIONAL_INTEREST_DETAILS")) {
                                List DataList = (List) mapData.get("DEPOSIT_ADDITIONAL_INTEREST_DETAILS");
                                if(DataList!=null&&DataList.size()>0)
                                {
                                ROIDet = (HashMap) DataList.get(0);
                                if (ROIDet != null) {
                                    setLblAddIntrstRteVal(CommonUtil.convertObjToStr(ROIDet.get("ROI")));
                                }
                                    if (rdoTypeOfDeposit_No) {
                                        HashMap whereAddIntdet = new HashMap();
                                        whereAddIntdet.put("DEPOSIT_DT", paramMap.get("CURRENT_DT"));
                                        whereAddIntdet.put("PRODID", lienPrdId);
                                        whereAddIntdet.put("CUSTID", getCustomerID());
                                        whereAddIntdet.put("PERIOD", diff);
                                        whereAddIntdet.put("CATEGORY_ID", depositMap.get("CATEGORY_ID"));
                                        whereAddIntdet.put("DEPOSITNO", this.getTxtDepositNo());
                                        List addInterest = ClientUtil.executeQuery("getDepositClosingLoanInterestDetails", whereAddIntdet);
                                        if (addInterest != null && addInterest.size() > 0) {
                                            HashMap intDet = (HashMap) addInterest.get(0);
                                            if (intDet != null && intDet.containsKey("ROI")) {
                                                String ROI = CommonUtil.convertObjToStr(intDet.get("ROI"));
                                                setAddIntLoanAmt(calcAddIntAmtForLoan(strMatDt, currDt, ROI, lienAmt));
                                            }
                                        }
                                    }
                                
                                setLblAddIntRtAmtVal(calcAddIntAmt(strMatDt,currDt));
                                }
                                else
                                {
                                      setLblAddIntrstRteVal("");
                                      setLblMaturityPeriod("");
                                      setLblAddIntRtAmtVal("");
                                      setAddIntLoanAmt("");
                                }
                            }
                            
                        } 
                        else if (matuOpt != null && matuOpt.length() > 0 && matuOpt.equalsIgnoreCase("Y")) {
                            DateVAl= CommonUtil.convertObjToStr(paramMap.get("MATURITY_DT"));
                            setLblMaturityPeriod(CommonUtil.convertObjToStr(diff) + " " + "Days");

                            HashMap ROIDet = new HashMap();
                            if (mapData.containsKey("DEPOSIT_ADDITIONAL_MAT_INTEREST_DETAILS")) {
                                List DataList = (List) mapData.get("DEPOSIT_ADDITIONAL_MAT_INTEREST_DETAILS");
                                if(DataList!=null&&DataList.size()>0)
                                {
                                ROIDet = (HashMap) DataList.get(0);
                                if (ROIDet != null) {
                                    setLblAddIntrstRteVal(CommonUtil.convertObjToStr(ROIDet.get("ROI")));
                                }
                                    if (rdoTypeOfDeposit_No) {
                                        HashMap whereAddIntdet = new HashMap();
                                        whereAddIntdet.put("DEPOSIT_DT", paramMap.get("CURRENT_DT"));
                                        whereAddIntdet.put("PRODID", lienPrdId);
                                        whereAddIntdet.put("CUSTID", getCustomerID());
                                        whereAddIntdet.put("PERIOD", diff);
                                        whereAddIntdet.put("CATEGORY_ID", depositMap.get("CATEGORY_ID"));
                                        whereAddIntdet.put("DEPOSITNO", this.getTxtDepositNo());
                                        List addInterest = ClientUtil.executeQuery("getDepositClosingLoanInterestDetails", whereAddIntdet);
                                        if (addInterest != null && addInterest.size() > 0) {
                                            HashMap intDet = (HashMap) addInterest.get(0);
                                            if (intDet != null && intDet.containsKey("ROI")) {
                                                String ROI = CommonUtil.convertObjToStr(intDet.get("ROI"));
                                                setAddIntLoanAmt(calcAddIntAmtForLoan(strMatDt, currDt, ROI, lienAmt));
                                            }
                                        }
                                    }
                                    setLblAddIntRtAmtVal(calcAddIntAmt(strMatDt,currDt));
                                }
                                else
                                {
                                      setLblAddIntrstRteVal("");
                                      setLblMaturityPeriod("");
                                      setLblAddIntRtAmtVal("");
                                      setAddIntLoanAmt("");
                                }
                            }
                        }else if (notRenewMatu != null && notRenewMatu.trim().length() > 0) {
                           
                            setLblMaturityPeriod(CommonUtil.convertObjToStr(diff) + " " + "Days");

                            HashMap RnwDet = new HashMap();
                            double rnwInt = 0.0, matInt = 0.0;
                            if (mapData.containsKey("DEPOSIT_ADDITIONAL_INTEREST_DETAILS")) {
                                List DataList = (List) mapData.get("DEPOSIT_ADDITIONAL_INTEREST_DETAILS");
                                if(DataList!=null && DataList.size()>0)
                                {
                                RnwDet = (HashMap) DataList.get(0);
                                if (RnwDet != null) {
                                    rnwInt = CommonUtil.convertObjToDouble(RnwDet.get("ROI"));
                                }
                                }
                                else
                                {
                                   rnwInt=0.0; 
                                }
                            }
                            HashMap MatDet = new HashMap();
                            if (mapData.containsKey("DEPOSIT_ADDITIONAL_MAT_INTEREST_DETAILS")) {
                                List DataList = (List) mapData.get("DEPOSIT_ADDITIONAL_MAT_INTEREST_DETAILS");
                                 if(DataList!=null && DataList.size()>0)
                                {
                                MatDet = (HashMap) DataList.get(0);
                                matInt = CommonUtil.convertObjToDouble(MatDet.get("ROI"));
                                }
                                 else
                                {
                                   matInt=0.0; 
                                }
                            }
                            if (notRenewMatu.equalsIgnoreCase("N")) {

                                if (matInt < rnwInt) {
                                    DateVAl= CommonUtil.convertObjToStr(paramMap.get("MATURITY_DT"));
                                    setLblAddIntrstRteVal(String.valueOf(matInt));
                                } else {
                                    DateVAl= CommonUtil.convertObjToStr(paramMap.get("CURRENT_DT"));
                                    setLblAddIntrstRteVal(String.valueOf(rnwInt));
                                }
                            } else if (notRenewMatu.equalsIgnoreCase("Y")) {
                                if (matInt > rnwInt) {
                                     DateVAl= CommonUtil.convertObjToStr(paramMap.get("MATURITY_DT"));
                                    setLblAddIntrstRteVal(String.valueOf(matInt));
                                } else {
                                     DateVAl= CommonUtil.convertObjToStr(paramMap.get("CURRENT_DT"));
                                    setLblAddIntrstRteVal(String.valueOf(rnwInt));
                                }
                            }
                            if(matInt==0 && rnwInt==0)
                            {
                               setLblAddIntRtAmtVal("");
                               setLblMaturityPeriod("");
                               setLblAddIntrstRteVal(""); 
                               setAddIntLoanAmt("");
                            }
                            else{
                                if(rdoTypeOfDeposit_No){
                                 HashMap whereAddIntdet = new HashMap();
                                    whereAddIntdet.put("DEPOSIT_DT", paramMap.get("CURRENT_DT"));
                                    whereAddIntdet.put("PRODID", lienPrdId);
                                    whereAddIntdet.put("CUSTID", getCustomerID());
                                    whereAddIntdet.put("PERIOD", diff);
                                    whereAddIntdet.put("CATEGORY_ID", depositMap.get("CATEGORY_ID"));
                                    whereAddIntdet.put("DEPOSITNO", this.getTxtDepositNo());
                                    List addInterest = ClientUtil.executeQuery("getDepositClosingLoanInterestDetails", whereAddIntdet);
                                      if(addInterest!=null&&addInterest.size()>0) {
                                  HashMap intDet=(HashMap)addInterest.get(0);
                                  if(intDet!=null && intDet.containsKey("ROI")){
                                  String ROI =CommonUtil.convertObjToStr(intDet.get("ROI"));
                                 
                                  setAddIntLoanAmt(calcAddIntAmtForLoan(strMatDt,currDt,ROI,lienAmt));
                                          }
                                    }
                                
                               setLblAddIntRtAmtVal(calcAddIntAmt(strMatDt,currDt));}
							}
                        } else {
                            setLblAddIntRtAmtVal("");
                            setLblMaturityPeriod("");
                            setLblAddIntrstRteVal("");
                            setAddIntLoanAmt("");
                        }
                    }
                  }
                }
            //End on 22-04-14
            double paid = CommonUtil.convertObjToDouble(getIntDrawn()).doubleValue();
            double payable = CommonUtil.convertObjToDouble(getClosingIntDb()).doubleValue();
            String payAmt = CommonUtil.convertObjToStr(getPayReceivable());
            
//            if (payable > paid && getLastIntAppDate().length() > 0 && !getLastIntAppDate().equals("")
//                    && getLstProvDt().length() > 0 && !getLstProvDt().equals("")) {
            if (payable > paid){ // Modified condition for TDS calculation // Added by nithya on 06-02-2020 for KD-1090
                double tdsAmt = CommonUtil.convertObjToDouble(payAmt).doubleValue();
                closureData.put("TDS_CALCULATION", "TDS_CALCULATION");
                closureData.put("CUST_ID", getCustomerID());
                closureData.put("PROD_ID", getProdID());
                closureData.put("DEPOSIT_NO", getTxtDepositNo());
                closureData.put("RATE_OF_INT", getPrematureClosingRate());
                closureData.put("TDS_AMOUNT", new Double(tdsAmt));
                tdsData = proxy.executeQuery(closureData, map);
                if (tdsData != null && tdsData.size() > 0) {
                    System.out.println("#######tdsData : " + tdsData);
                    setClosingTds(CommonUtil.convertObjToStr(tdsData.get("TDSDRAMT")));
                    setTdsAcHd(CommonUtil.convertObjToStr(tdsData.get("TDSCrACHdId")));
                } else {
                    setClosingTds("0.0");
                }
            } else {
                setClosingTds("0.0");
            }
            list = null;
            mapData = null;
            tdsData = null;
            closureData = null;
        } else {
            ClientUtil.displayAlert("This Deposit Account is Already Closed");
            resetDepositDetails();
            resetCustomerDetails();
        }
    }

    private void setClosingIntRate(List lst) {
        if (lst != null && lst.size() > 0) {
            if (getLblClosingType().equals(CommonConstants.PREMATURE_CLOSURE)
                    || getLblClosingType().equals(CommonConstants.NORMAL_CLOSURE)) {
                HashMap hash = new HashMap();
                hash = (HashMap) lst.get(0);
                //            int size = lst.size();
                //            Date fromDate = DateUtil.getDateMMDDYYYY(getDepositDate());
                //            Date toDate = new Date();
                //            if(getLblClosingType().equals(CommonConstants.PREMATURE_CLOSURE))
                //                toDate = (Date)curDate.clone();
                //            else
                //                toDate = DateUtil.getDateMMDDYYYY(getMaturityDate());
                //            //        if(size>0){
                //            System.out.println("From Date : "+fromDate);
                //            System.out.println("To Date : "+toDate);
                //            long frPeriod = 0, toPeriod = 0;
                //            double roi = 0.0, penal = 0.0 ;
                //            long dateDiff = 0;
                //            boolean foundIntRate=false;
                //            for (int i=0; i<size; i++) {
                //                hash = (HashMap) lst.get(i);
                //                frPeriod = CommonUtil.convertObjToLong(hash.get("FROM_PERIOD"));
                //                toPeriod = CommonUtil.convertObjToLong(hash.get("TO_PERIOD"));
                //                roi = CommonUtil.convertObjToDouble(hash.get("ROI")).doubleValue();
                //                penal = CommonUtil.convertObjToDouble(hash.get("PENAL_INT")).doubleValue();
                //                System.out.println("From period : "+frPeriod);
                //                System.out.println("To period : "+toPeriod);
                //                dateDiff = DateUtil.dateDiff(fromDate,toDate);
                //                periodNoOfDays=dateDiff -1;
                //                System.out.println("Date diff : "+dateDiff);
                //                if(dateDiff>=frPeriod && dateDiff<=toPeriod) {
                //                    i=size;
                //                    foundIntRate=true;
                //                }
                //            }
                //            System.out.println("From roi : "+roi);
                //            String closingRoi = "";
                //            String closingPenal = "";
                //            if (foundIntRate) {
                //                closingRoi = CommonUtil.convertObjToStr(hash.get("ROI"));
                //                closingPenal = CommonUtil.convertObjToStr(hash.get("PENAL_INT"));
                //            } else {
                //                closingRoi = "0";
                //                closingPenal = "0";
                //                roi = 0;
                //                penal = 0;
                //            }
                //            if(getViewTypeDet() == 100){



                double penal = CommonUtil.convertObjToDouble(hash.get("PENAL_INT")).doubleValue();
                double roi = CommonUtil.convertObjToDouble(hash.get("ROI")).doubleValue();
                String closingPenal = CommonUtil.convertObjToStr(hash.get("PENAL_INT"));
                String closingRoi = CommonUtil.convertObjToStr(hash.get("ROI"));
                this.setRateApplicable(String.valueOf(roi));
               
                if (getLblClosingType().equals(CommonConstants.PREMATURE_CLOSURE)) {
                    //added by jjjj
                    System.out.println("jjjjj test");
                    if (getPremClos().equals("Y")) {
                        System.out.println("getPremClos" + getPremClos());
                        // this.setRateApplicable(String.valueOf(roi));
                        if (isRdoYesButton()) {
                            System.out.println("premature closs" + penal);
                            this.setPenaltyPenalRate(String.valueOf(penal));
                            setPrematureClosingRate(String.valueOf(penal));
                            //  roi = penal;
                        } else if (isRdoNoButton()) {
                            setPrematureClosingRate(String.valueOf(penal));
                            this.setPenaltyPenalRate(String.valueOf(0.0));
                        }
                    } else {

                        if (isRdoYesButton()) {
                            this.setPenaltyPenalRate(String.valueOf(penal));
                            setPrematureClosingRate(String.valueOf(roi - penal));
                        } else if (isRdoNoButton()) {
                            setPrematureClosingRate(String.valueOf(roi));
                            this.setPenaltyPenalRate(String.valueOf(0.0));
                        }
                    }
                } else if (getLblClosingType().equals(CommonConstants.NORMAL_CLOSURE)) {
                    HashMap whereAddIntdet = new HashMap();
                    whereAddIntdet.put("DEPOSIT_NO", this.getTxtDepositNo());
                    List accIntLst = ClientUtil.executeQuery("getIntCrIntDrawn", whereAddIntdet);
                    if (accIntLst != null && accIntLst.size() > 0) {
                        HashMap intDet = (HashMap) accIntLst.get(0);
                        if (intDet != null && intDet.containsKey("RATE_OF_INT")) {
                            roi = CommonUtil.convertObjToDouble(intDet.get("RATE_OF_INT")).doubleValue();
                            closingRoi = CommonUtil.convertObjToStr(intDet.get("RATE_OF_INT"));

                        }
                    }
                    this.setRateApplicable(String.valueOf(roi));
                    setPrematureClosingRate(String.valueOf(roi));
                    this.setPenaltyPenalRate(String.valueOf(0.0));
                }
                //            }
                //            setPrematureClosingRate(String.valueOf(roi-penal));
                //            HashMap editMap = new HashMap();
                //            if(getViewTypeDet() == 100 || getViewTypeDet() == 250){//editmode and new mode
                //                if(getLblClosingType().equals(CommonConstants.PREMATURE_CLOSURE)){
                //                    if(isRdoYesButton() == true){
                //                        setPrematureClosingRate(String.valueOf(roi- penal));
                //                        setPenaltyPenalRate(String.valueOf(penal));
                //                        setRateApplicable(String.valueOf(roi));
                //                    }else if(isRdoNoButton() == true){
                //                        setPrematureClosingRate(String.valueOf(roi));
                //                        setRateApplicable(String.valueOf(roi));
                //                        setPenaltyPenalRate(String.valueOf("0.0"));
                //                    }
                //                }else{
                //                    setPrematureClosingRate(String.valueOf(roi));
                //                }
                //            }
                //            if(getViewTypeDet() == 200){//editmode only...
                //                editMap.put("DEPOSIT_NO",getTxtDepositNo());
                //                lst = ClientUtil.executeQuery("getPenalYesorNoDetails", editMap);
                //                if(lst !=null && lst.size()>0){
                //                    editMap = (HashMap)lst.get(0);
                //                    setPenaltyInt(CommonUtil.convertObjToStr(editMap.get("PENAL_INT")));
                //                    setTypeOfDep(CommonUtil.convertObjToStr(editMap.get("PAYMENT_TYPE")));
                //                }
                //                setPenaltyInt(CommonUtil.convertObjToStr(editMap.get("PENAL_INT")));
                //                double calcPenalInt = 0.0;
                //                double penalAmt = CommonUtil.convertObjToDouble(editMap.get("PENAL_RATE")).doubleValue();
                //                double currInt = CommonUtil.convertObjToDouble(editMap.get("CURR_RATE_OF_INT")).doubleValue();
                //                if(isRdoYesButton() != true && isRdoNoButton() != true){
                //                    setPrematureClosingRate(String.valueOf(currInt));
                //                    setPenaltyPenalRate(String.valueOf(penalAmt));
                //                    setRateApplicable(String.valueOf(currInt + penalAmt));
                //                }
                //                setViewTypeDet(100);
                //            }
                //            if(getViewTypeDet() == 8){//authorize mode
                //                editMap = new HashMap();
                //                editMap.put("DEPOSIT_NO",getTxtDepositNo());
                //                lst = ClientUtil.executeQuery("getPenalYesorNoDetails", editMap);
                //                if(lst !=null && lst.size()>0){
                //                    editMap = (HashMap)lst.get(0);
                //                    setPenaltyInt(CommonUtil.convertObjToStr(editMap.get("PENAL_INT")));
                //                    double penalAmt = CommonUtil.convertObjToDouble(editMap.get("PENAL_RATE")).doubleValue();
                //                    double penalInt = CommonUtil.convertObjToDouble(editMap.get("CURR_RATE_OF_INT")).doubleValue();
                //                    setPrematureClosingRate(CommonUtil.convertObjToStr(editMap.get("CURR_RATE_OF_INT")));
                //                    setRateApplicable(CommonUtil.convertObjToStr(editMap.get("PENAL_RATE")));
                //                    setPenaltyPenalRate(String.valueOf(penalAmt));
                //                    if(penalAmt == 0){
                //                        setRateApplicable(CommonUtil.convertObjToStr(editMap.get("CURR_RATE_OF_INT")));
                //                        setTypeOfDep(CommonUtil.convertObjToStr(editMap.get("PAYMENT_TYPE")));
                //                    }else{
                //                        penalInt = penalInt + penalAmt;
                //                        setRateApplicable(String.valueOf(penalInt));
                //                    }
                //                }
                //            }
                //            if(DateUtil.dateDiff(fromDate,toDate )==0){
                //                setPrematureClosingRate("0.0");
                //                setRateApplicable("0.0");
                //                setPenaltyPenalRate("0.0");
                //            }
                //        }else{
                //            if(getViewTypeDet() == 100){
                //                double penal = 0.0;
                //                double roi = 0.0;
                //                this.setRateApplicable(String.valueOf(roi));
                //                if(getLblClosingType().equals(CommonConstants.PREMATURE_CLOSURE)) {
                //                    if(rdoPenaltyPenalRate_yes){
                //                        this.setPenaltyPenalRate(String.valueOf(0.0));
                //                        setPrematureClosingRate(String.valueOf(0.0));
                //                    }else if(rdoPenaltyPenalRate_no) {
                //                        setPrematureClosingRate(String.valueOf(0.0));
                //                        this.setPenaltyPenalRate(String.valueOf(0.0));
                //                    }
                //                }else if(getLblClosingType().equals(CommonConstants.NORMAL_CLOSURE)) {
                //                        setPrematureClosingRate(String.valueOf(0.0));
                //                        this.setPenaltyPenalRate(String.valueOf(0.0));
                //                }
                //            }
            } else if (getLblClosingType().equals(CommonConstants.TRANSFER_OUT_CLOSURE)) {
                double penal = 0.0;
                double roi = 0.0;
                setPrematureClosingRate(String.valueOf(penal));
                setRateApplicable(String.valueOf(roi));
                setPenaltyPenalRate(String.valueOf(roi));
            }
            lst = null;
        } else if (lst.isEmpty()) {
            double penal = 0.0;
            double roi = 0.0;
            setPrematureClosingRate(String.valueOf(penal));
            setRateApplicable(String.valueOf(roi));
            setPenaltyPenalRate(String.valueOf(roi));
        }
    }

    private void setDepositDetails(HashMap hashMap) {
        System.out.println("#########hashMap" + hashMap);
        this.setConstitution((String) hashMap.get("CONSTITUTION"));
        this.setCategory((String) hashMap.get("CATEGORY"));
        this.setModeOfSettlement((String) hashMap.get("SETTLEMENT_MODE"));
        this.setDepositActName((String) hashMap.get("CUST_NAME"));
        this.setCustomerID((String) hashMap.get("CUST_ID"));
    }

    private void setCustomerDetails(HashMap hashMap) {
    }

    public void resetDepositDetails() {
        this.setConstitution("");
        this.setCategory("");
        this.setModeOfSettlement("");
        this.setDepositActName("");
        this.setGroupDepositProd("N");// Added by nithya on 09-10-2017 for group deposit changes
        this.setGroupDepositRecInt("");// Added by nithya on 09-10-2017 for group deposit changes
        this.setIsSlabWiseDailyDeposit("");//nithya
        setSpecialRDCompleted("N"); //03-06-2020
        setDailyDepositLoanPreClose("N");
        setDailyDepositLoanPreCloseROI("");
    }

    private void resetCustomerDetails() {
        this.setCustomerID("");
    }

    public void getSubDepositDetails() {
        try {
            System.out.println("Premature Closing Rate 1111" + getPrematureClosingRate());
            ArrayList data;
            ArrayList subDepositList = this.tbmSubDeposit.getDataArrayList();
            double principal = 0, balance = 0;
            ArrayList selectedList = new ArrayList();
            int size = subDepositList.size();
            for (int i = 0; i < size; i++) {
                data = (ArrayList) subDepositList.get(i);
                if (String.valueOf(data.get(0)).equalsIgnoreCase("true")) {
                    selectedList.add(subDepositList.get(i));
                    principal += ((Double) data.get(2)).doubleValue();
                } else {
                    balance += ((Double) data.get(2)).doubleValue();
                }
            }
            if (selectedList.size() > 0) {
                if (selectedList.size() == 1) {
                    setSingleDeposit((int) ((ArrayList) selectedList.get(0)).get(1));
                } else {
                    setSubDepositDetails(selectedList);
                }
                this.setPrinicipal(String.valueOf(principal));
                this.setBalanceDeposit(String.valueOf(principal));
            }//else if( selectedList.size()==0){
            //                this.resetDetails();
            //            }
            //this.resetPartilalWithDrawal();
            this.lblClosingType = "";
            // System.out.println("");
        } catch (Exception E) {
            E.printStackTrace();
        }
    }

    public void resetDetails() {
        this.resetDepositDetailTO();
        this.resetClosureTO();
        this.resetPresentTO();
        this.resetPartilalWithDrawal();
        this.setBalanceDeposit("");
        setLtdDeposit("");
        rec_recivable="";
        setDepositPeriodWK("");
    }

    private void resetDepositDetailTO() {
        this.setDepositDate("");
        this.setIntPaymentFreq("");
        this.setRateOfInterest("");
        this.setMaturityDate("");
        this.setMaturityValue("");
        this.setPeriod("");
        this.setPrinicipal("");
    }

    private SubDepositTO populate(int i) {
        return (SubDepositTO) subDepositTOs.get(i);
    }

    private void setSingleDeposit(int subDepositNo) throws Exception {
        int i = findSubDeposit(subDepositNo);
        if (i != -1) {
            SubDepositTO obj = populate(i);
            setSubDepositTO(obj);
            setPresentTO(obj);
            setClosureTO(obj);
        } else {
            resetDepositDetailTO();
            resetPresentTO();
            resetClosureTO();
        }
    }

    private int findSubDeposit(int subDepositNo) {
        int size = subDepositTOs.size();
        SubDepositTO obj;
        for (int i = 0; i < size; i++) {
            obj = (SubDepositTO) subDepositTOs.get(i);
            if (obj.getDepositSubNo()==CommonUtil.convertObjToInt(subDepositNo)) {
                return i;
            }
        }
        return -1;
    }

    private void setSubDepositTO(SubDepositTO obj) {
        this.setSubDepositNo(CommonUtil.convertObjToStr(obj.getDepositSubNo()));
        this.setDepositDate(DateUtil.getStringDate(obj.getDepositDt()));
        double freq = CommonUtil.convertObjToDouble(obj.getIntpayFreq()).doubleValue();
        if (freq == 0) {
            this.setIntPaymentFreq(CommonUtil.convertObjToStr("Date of Maturity"));
        } else if (freq == 30) {
            this.setIntPaymentFreq(CommonUtil.convertObjToStr("Monthly"));
        } else if (freq == 90) {
            this.setIntPaymentFreq(CommonUtil.convertObjToStr("Quaterly"));
        } else if (freq == 180) {
            this.setIntPaymentFreq(CommonUtil.convertObjToStr("Half Yearly"));
        } else {
            this.setIntPaymentFreq(CommonUtil.convertObjToStr("Yearly"));
        }
        //        this.setIntPaymentFreq(CommonUtil.convertObjToStr(obj.getIntpayFreq()));
        this.setRateOfInterest(CommonUtil.convertObjToStr(obj.getRateOfInt()));
        this.setMaturityDate(DateUtil.getStringDate(obj.getMaturityDt()));
        this.setMaturityValue(CommonUtil.convertObjToStr(obj.getMaturityAmt()));
        //        setStatusBy(obj.getStatusBy());
        setAuthorizeStatus(obj.getAuthorizeStatus());
        StringBuffer period = new StringBuffer();
        String temp;

        temp = "0";
        if (obj.getDepositPeriodDd() != null) {
            temp = obj.getDepositPeriodDd().toString();
        }

        period.append(temp + " days ");

        temp = "0";
        if (obj.getDepositPeriodMm() != null) {
            temp = obj.getDepositPeriodMm().toString();
        }
        period.append(temp + " months ");

        temp = "0";
        if (obj.getDepositPeriodYy() != null) {
            temp = obj.getDepositPeriodYy().toString();
        }
        period.append(temp + " years");

        this.setPeriod(period.toString());
        //uncommentted
        this.setPrinicipal(CommonUtil.convertObjToStr(obj.getDepositAmt()));
    }

    private void setSubDepositDetails(ArrayList selectedList) throws Exception {
        resetCummValues();
        int size = selectedList.size();
        int index;
        ArrayList data;
        SubDepositTO obj;
        for (int i = 0; i < size; i++) {
            data = (ArrayList) selectedList.get(i);
            this.setSubDepositNo((String) data.get(1));
            index = findSubDeposit((int) data.get(1));
            if (index != -1) {
                obj = populate(i);
                setPresentTO(obj);
                setClosureTO(obj);
                cummulatePresentPosition();
                cummulateClosingPosition();
            }
        }
        resetDepositDetailTO();
        resetPresentTO();
        resetClosureTO();
        setCummDepositDetails();
    }

    private void resetCummValues() {
        cummWithDrawnAmount = 0;
        cummIntCredit = 0;
        cummIntDebit = 0;
        cummTDCollected = 0;
        cummLienFreezeAmount = 0;
        cummBalance = 0;
        cummClosingCr = 0;
        cummClosingDb = 0;
        cummClosingTdsCollected = 0;
        cummClosingDisbursal = 0;
    }

    private void setCummDepositDetails() {
        this.setBalance(String.valueOf(this.cummBalance));
        this.setClosingDisbursal(String.valueOf(this.cummClosingDisbursal));
        this.setClosingIntCr(String.valueOf(this.cummClosingCr));
        this.setClosingIntDb(String.valueOf(this.cummClosingDb));
        this.setClosingTds(String.valueOf(this.cummClosingTdsCollected));
        this.setIntCr(String.valueOf(this.cummIntCredit));
        this.setIntDrawn(String.valueOf(this.cummIntDebit));
        this.setLienFreezeAmt(String.valueOf(this.cummLienFreezeAmount));
        this.setTdsCollected(String.valueOf(this.cummTDCollected));
        this.setWithDrawn(String.valueOf(this.cummWithDrawnAmount));

    }

    private void cummulatePresentPosition() {
        Double value;
        value = CommonUtil.convertObjToDouble(this.getIntCr());
        if (value != null) {
            cummIntCredit += value.doubleValue();
        }

        value = CommonUtil.convertObjToDouble(this.getIntDrawn());
        if (value != null) {
            cummIntDebit += value.doubleValue();
        }

        value = CommonUtil.convertObjToDouble(this.getLienFreezeAmt());
        if (value != null) {
            cummLienFreezeAmount += value.doubleValue();
        }

        value = CommonUtil.convertObjToDouble(this.getTdsCollected());
        if (value != null) {
            cummTDCollected += value.doubleValue();
        }

        value = CommonUtil.convertObjToDouble(this.getWithDrawn());
        if (value != null) {
            cummWithDrawnAmount += value.doubleValue();
        }

        value = CommonUtil.convertObjToDouble(this.getBalance());
        if (value != null) {
            cummBalance += value.doubleValue();
        }
    }

    private void cummulateClosingPosition() {
        Double value;
        value = CommonUtil.convertObjToDouble(this.getClosingIntCr());
        if (value != null) {
            cummClosingCr += value.doubleValue();
        }

        value = CommonUtil.convertObjToDouble(this.getClosingIntDb());
        if (value != null) {
            cummClosingDb += value.doubleValue();
        }

        value = CommonUtil.convertObjToDouble(this.getClosingDisbursal());
        if (value != null) {
            cummClosingDisbursal += value.doubleValue();
        }
        //        System.out.println("cummulativeClosingPosition : "+cummClosingDisbursal);

        value = CommonUtil.convertObjToDouble(this.getClosingTds());
        if (value != null) {
            cummClosingTdsCollected += value.doubleValue();
        }
    }

    public double getRound(double amount, String mode) {  // double result=0.0;
        System.out.println("in getRounddd " + amount + "  " + mode);
        if (mode != null && !mode.equals("")) {
            if (mode.equals("NO_ROUND_OFF")) {
                System.out.println(" in no round");
                String amt = df.format(amount);
                amount = Double.parseDouble(amt);
                System.out.println("interestAmt " + amount);
            } else if (mode.equals("NEAREST_VALUE")) {
                System.out.println(" in nearest roundingg");
                amount = (double) getNearest((long) (amount * 100), 100) / 100;
            } else if (mode.equals("HIGHER_VALUE")) {
                System.out.println("in higher valueee");
                double d = amount;
                if (d % 1.0 > 0) {
                    System.out.println("mode valuee  " + d % 1.0);
                    double c = d % 1.0;
                    d = d - c;
                    d = d + 1;
                    System.out.println("Higher valuuuee " + d);
                } else {
                    System.out.println("dsf  " + d % 1.0);
                    System.out.println("ggggg " + d);
                }
                amount = d;
                System.out.println("Higher valuuuee reall " + d);

                //interestAmt = (double)getNearest((long)(interestAmt *100),100)/100;
            } else if (mode.equals("LOWER_VALUE")) {
                System.out.println("in lower round valueee");
                double d = amount;
                if (d % 1.0 > 0) {
                    System.out.println("mode valuee  " + d % 1.0);
                    double c = d % 1.0;
                    d = d - c;

                    System.out.println("Higher valuuuee " + d);
                } else {
                    System.out.println("dsf  " + d % 1.0);
                    System.out.println("ggggg " + d);
                }
                amount = d;
                System.out.println("Higher valuuuee reall " + d);

            } else {
                System.out.println(" in no round");
                amount = new Double(amount);
                System.out.println("interestAmt " + amount);
            }
        } else {
            System.out.println(" i else  in no round");
            amount = new Double(amount);
            System.out.println("interestAmt " + amount);

        }
        return amount;
    }

    private void setPresentTO(SubDepositTO obj) {
        //        System.out.println("######subDepositTO :"+obj);
        this.setIntCr(String.valueOf(CommonUtil.convertObjToDouble(obj.getTotalIntCredit()).doubleValue()));
        this.setIntDrawn(String.valueOf(CommonUtil.convertObjToDouble(obj.getTotalIntDrawn()).doubleValue()));
        double paidInst = 0, totalInst = 0;
        //        this.setPayReceivable(String.valueOf(CommonUtil.convertObjToDouble(getPayReceivable()).doubleValue()));
        totalInst = (obj.getTotalInstallments() != null) ? obj.getTotalInstallments().doubleValue() : 0;
        paidInst = (obj.getTotalInstallPaid() != null) ? obj.getTotalInstallPaid().doubleValue() : 0;

        this.setNoInstDue(String.valueOf(totalInst));
        this.setNoInstPaid(String.valueOf(paidInst));
        this.setLastIntAppDate(DateUtil.getStringDate(obj.getLastIntApplDt()));

        //        this.setBalance(CommonUtil.convertObjToStr(obj.getAvailableBalance()));
        this.setBalance(CommonUtil.convertObjToStr(obj.getTotalBalance()));
        System.out.println("setBalance 66666666666666666666666 :::::::"+getBalance());
        this.setLienFreezeAmt(getLienFreezeData());
        this.setTdsCollected(getTDSAmount());
        this.setWithDrawn(getWithDrawAmt());

    }

    private String getLienFreezeData() {
        Double amount;
        double amt = 0;
        HashMap whereMap = new HashMap();
        whereMap.put("DEPOSITNO", this.getTxtDepositNo());
        whereMap.put("SUBDEPOSIT", CommonUtil.convertObjToInt(this.getSubDepositNo()));
        List list = ClientUtil.executeQuery("getFreezeAmount", whereMap);
        List list1 = ClientUtil.executeQuery("getLienAmount", whereMap);
        if (list != null && list.size() > 0) {
            whereMap = (HashMap) list.get(0);
            amount = CommonUtil.convertObjToDouble(whereMap.get("AMOUNT"));
            if (amount != null) {
                amt += amount.doubleValue();
            }
        }
        if (list1 != null && list1.size() > 0) {
            whereMap = (HashMap) list1.get(0);
            amount = CommonUtil.convertObjToDouble(whereMap.get("AMOUNT"));
            if (amount != null) {
                amt += amount.doubleValue();
            }
        }
        list = null;
        list1 = null;
        whereMap = null;
        return String.valueOf(amt);
    }

    private String getTDSAmount() {
        Double amount;
        double amt = 0;
        HashMap whereMap = new HashMap();
        whereMap.put("DEPOSITNO", this.getTxtDepositNo() + "_1");
        //        whereMap.put("SUBDEPOSIT",this.getSubDepositNo());
        List list = ClientUtil.executeQuery("getTDSAmount", whereMap);
        if (list != null && list.size() > 0) {
            whereMap = (HashMap) list.get(0);
            amount = CommonUtil.convertObjToDouble(whereMap.get("AMOUNT"));
            if (amount != null) {
                amt += amount.doubleValue();
            }
        }
        list = null;
        whereMap = null;
        return String.valueOf(amt);
    }

    private String getWithDrawAmt() {
        Double amount;
        double amt = 0;
        HashMap whereMap = new HashMap();
        whereMap.put("DEPOSITNO", this.getTxtDepositNo());
        whereMap.put("SUBDEPOSIT", this.getSubDepositNo());
        List list = ClientUtil.executeQuery("getWithDrawAmount", whereMap);
        if (list != null && list.size() > 0) {
            whereMap = (HashMap) list.get(0);
            setNoOfUnitsWithDrawn(((BigDecimal) whereMap.get("NO_OF_UNITS")).toString());
            amount = CommonUtil.convertObjToDouble(whereMap.get("AMT"));

            if (amount != null) {
                amt += amount.doubleValue();
            }
        }
        list = null;
        whereMap = null;
        return String.valueOf(amt);
    }

    public void setPartilalWithDrawal() {
        if (getNoOfUnitsWithDrawn() == null || getNoOfUnitsWithDrawn().length() == 0) {
            Double amount = CommonUtil.convertObjToDouble(this.getWithDrawn());
            if (amount != null) {
                double amt = amount.doubleValue() / this.getUnitAmt();
                this.setNoOfUnitsWithDrawn(String.valueOf(amt));
            }
        }
        Double amount;
        double amt = 0, unitAmt = 0;
        double totalUnits = 0, drawnUnits = 0;
        amount = CommonUtil.convertObjToDouble(this.getPrinicipal());
        if (amount != null) {
            amt = amount.doubleValue();
        }
        unitAmt = this.getUnitAmt();
        totalUnits = amt / unitAmt;

        if (getNoOfUnitsWithDrawn() != null) {
            drawnUnits = CommonUtil.convertObjToDouble(getNoOfUnitsWithDrawn()).doubleValue();
        }
        this.setNoOfUnitsAvai(String.valueOf(totalUnits - drawnUnits));

        this.setPrematureClosingDate(getRunPeriod());
        this.setPresentUnitInt("0");
        this.setSettlementUnitInt("0");

        this.setPrematureClosingRate(getPreMatureRate());
        this.setDepositRunPeriod(getRunPeriod());
        this.setListTOS();
    }

    public void resetPartilalWithDrawal() {
        this.setNoOfUnitsAvai("");
        this.setNoOfUnitsWithDrawn("");
        this.setPrematureClosingRate("");
        this.setPrematureClosingDate("");
        this.setPresentUnitInt("");
        this.setSettlementUnitInt("");
        this.setDepositRunPeriod("");
        this.setPrematureClosingRate("");

        this.setTxtAmtWithDrawn("");
        this.setTxtPWNoOfUnits("");
        this.withdrawalTOs.clear();
        this.deletePWList.clear();
        this.tbmPartialWithdrawal.setData(new ArrayList());
        this.tbmPartialWithdrawal.fireTableDataChanged();
    }

    private void setListTOS() {
        HashMap whereMap = new HashMap();
        whereMap.put("DEPOSITNO", this.getTxtDepositNo());
        whereMap.put("SUBDEPOSIT", this.getSubDepositNo());
        List list = ClientUtil.executeQuery("getSelectDepositWithDrawalTO", whereMap);
        if (list != null && list.size() > 0) {
            withdrawalTOs = (ArrayList) list;
            setTableData();
        }
    }

    private void setTableData() {
        int size = withdrawalTOs.size();
        ArrayList row;
        ArrayList rows = new ArrayList();
        //DepositWithDrawalTO obj ;
        for (int i = 0; i < size; i++) {
            objPartialWithDrawalTO = (DepositWithDrawalTO) withdrawalTOs.get(i);
            row = setRow();
            rows.add(row);
        }
        tbmPartialWithdrawal.setData(rows);
        tbmPartialWithdrawal.fireTableDataChanged();
    }

    private ArrayList setRow() {
        ArrayList row = new ArrayList();
        row.add(objPartialWithDrawalTO.getWithdrawNo());
        row.add(objPartialWithDrawalTO.getWithdrawAmt());
        row.add(objPartialWithDrawalTO.getNoOfUnits());
        return row;
    }

    private String getPreMatureRate() {
        HashMap whereMap = new HashMap();
        whereMap.put("DEPOSITNO", this.getTxtDepositNo());
        whereMap.put("SUBDEPOSIT", this.getSubDepositNo());
        whereMap.put("PRODID", this.getProdID());
        whereMap.put("PERIOD", new Double(this.prematureRunPeriod));
        List list = ClientUtil.executeQuery("getPenalRate", whereMap);
        if (list != null && list.size() > 0) {
            whereMap = (HashMap) list.get(0);
            return ((BigDecimal) whereMap.get("PENAL_INT")).toString();
        }
        list = null;
        whereMap = null;
        return "";
    }

    private String getRunPeriod() {
        Date d2 = (Date) curDate.clone();
        Date d1 = DateUtil.getDateMMDDYYYY(this.getDepositDate());
        double period = DateDifference.difference(d1, d2, "ActualMonth", "365");
        period = period * 365;
        prematureRunPeriod = period;
        int years = (int) (period / 365);
        int months = (int) ((period % 365) / 30);
        int days = (int) (period - years * 365 - months * 30);
        return (String.valueOf(days) + " days " + String.valueOf(months) + " months " + String.valueOf(years) + " years");
    }

    private double CalculationForSeniorCitizenDeath(HashMap calculationMap) {
        String cid = getCustomerID();
        double interestAmount = 0.0;
        double DepAmount = CommonUtil.convertObjToDouble(calculationMap.get("DEP_AMOUNT")).doubleValue();
        HashMap hmap = new HashMap();
        hmap.put("CUST_ID", cid);

        Date deathDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(calculationMap.get("DEATH_DT")));

        Date matdt = DateUtil.getDateMMDDYYYY(getMaturityDate());
        Date maturityDt = (Date) matdt.clone();
        maturityDt.setDate(matdt.getDate());
        maturityDt.setMonth(matdt.getMonth());
        maturityDt.setYear(matdt.getYear());
        String periodRun = getActualPeriodRun();
        Date depositDt = DateUtil.getDateMMDDYYYY(getDepositDate());

        Date depdt = (Date) curDate.clone();
        Date deathDate = (Date) curDate.clone();
        depdt.setDate(depositDt.getDate());
        depdt.setMonth(depositDt.getMonth());
        depdt.setYear(depositDt.getYear());
        int days = depdt.getDate();
        int month = depdt.getMonth() + 1;
        int year = depdt.getYear() + 1900;

        deathDate.setDate(deathDt.getDate());
        deathDate.setMonth(deathDt.getMonth());
        deathDate.setYear(deathDt.getYear());
        int Deathdays = deathDate.getDate();
        int Deathmonth = deathDate.getMonth() + 1;
        int Deathyear = deathDate.getYear() + 1900;



        int diffNormalDays = Deathdays - days;
        int diffNormalMonths = Deathmonth - month;
        int diffNormalYears = Deathyear - year;


        diffNormalMonths = (diffNormalMonths * 30) + (diffNormalYears * 360);
        diffNormalDays = diffNormalMonths + diffNormalDays;

        int curdate = 0;
        int curmonth = 0;
        int curYear = 0;
        int count = 0;
        long period = 0;
        if (!calculationMap.get("NORMAL").equals("NORMAL")) {
            curdate = curDate.getDate();
            curmonth = curDate.getMonth() + 1;
            curYear = curDate.getYear() + 1900;
            period = DateUtil.dateDiff(depositDt, curDate);
            while (DateUtil.dateDiff(depositDt, curDate) > 0) {
                int mon = depositDt.getMonth();
                int startYear = depositDt.getYear() + 1900;
                if (mon == 1 && startYear % 4 == 0) {
                    count++;
                }
                depositDt = DateUtil.addDays(depositDt, 30);
            }
            period -= count;


        } else {
            curdate = maturityDt.getDate();
            curmonth = maturityDt.getMonth() + 1;
            curYear = maturityDt.getYear() + 1900;
            period = DateUtil.dateDiff(depositDt, curDate);
        }

        Date SeniorDate = (Date) curDate.clone();

        SeniorDate.setDate(deathDt.getDate());
        SeniorDate.setMonth(deathDt.getMonth());
        SeniorDate.setYear(deathDt.getYear());
        int sdate = SeniorDate.getDate();
        int smonth = SeniorDate.getMonth() + 1;
        int syear = SeniorDate.getYear() + 1900;

        int diffdays = curdate - sdate;
        int diffMon = curmonth - smonth;
        int diffYear = curYear - syear;

        diffMon = (diffMon * 30) + (diffYear * 360);
        diffdays = diffMon + diffdays;

        Date endDt = curDate;
        long diffnormaldate = DateUtil.dateDiff(depositDt, curDate);
        hmap.put("DEPOSIT_DT", DateUtil.getDateMMDDYYYY(getDepositDate()));
        hmap.put("PRODID", getProdID());
        hmap.put("CUSTID", cid);
        hmap.put("DEPOSITNO", getTxtDepositNo());
        hmap.put("PERIOD", new Long(period));


        long diffnormalmnth = diffNormalDays / 30;
        long diffnormalDays = diffNormalDays % 30;

        long difSeniormonth = diffdays / 30;
        long difSeniorDays = diffdays % 30;


        if (deathDt != null) {
            List RateList = ClientUtil.executeQuery("getDepositClosingDetailsForSeniorCitizen", hmap);
            if (RateList != null && RateList.size() > 0) {
                hmap = (HashMap) RateList.get(0);
                double roi = CommonUtil.convertObjToDouble(hmap.get("ROI")).doubleValue();
                double rateOfInterest = CommonUtil.convertObjToDouble(calculationMap.get("RATE_OF_INTEREST")).doubleValue();



                if (!getPenaltyPenalRate().equals("")) {
                    double penal = CommonUtil.convertObjToDouble(getPenaltyPenalRate()).doubleValue();
                    roi = roi - penal;
                }
                interestAmount = (DepAmount * difSeniormonth * roi) / 1200;
                interestAmount = interestAmount + (DepAmount * difSeniorDays * roi) / 36500;
                //  double namount=(double)getNearest((long)(interestAmount *100),100)/100;
                double namount = getRound(interestAmount, getInterestRound());
                interestAmount = interestAmount + (DepAmount * diffnormalmnth * rateOfInterest) / 1200;
                interestAmount = interestAmount + (DepAmount * diffnormalDays * rateOfInterest) / 36500;
                double Samount = interestAmount - namount;
                //  namount=(double)getNearest((long)(namount *100),100)/100;
                namount = getRound(namount, getInterestRound());
                // Samount=(double)getNearest((long)(Samount *100),100)/100;
                Samount = getRound(Samount, getInterestRound());
                ClientUtil.showMessageWindow("SeniorCitizen Death Claim Details....\n"
                        + "\n Matured Date : " + matdt
                        + "\n CurrentDate : " + curDate
                        + "\n Senior Citizen Rate Applicable for: " + diffnormalmnth + "months" + diffnormalDays + "days"
                        + "\n Normal Rate Applicable for: " + difSeniormonth + "months" + difSeniorDays + "days"
                        + "\n Senior Citizen rate of Interest : " + rateOfInterest
                        + "\n Senior Citizen Interest Amount : " + "Rs" + Samount
                        + "\n Normal rate of Interest : " + roi
                        + "\n Normal Interest Amount : " + "Rs" + namount);

            }
        }
        return interestAmount;
    }

    private double simpleInterestCalculation(HashMap calculationMap) {
        System.out.println("Simple Calculation : " + calculationMap);
        double rateOfInt = 0.0;
        double sbCalcAmt = 0.0;
        double principal = 0.0;
        long difference = 0;
        Date depositDate = DateUtil.getDateMMDDYYYY(getDepositDate());
        Date maturityDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getMaturityDate()));
        long dateDiffMat = DateUtil.dateDiff(depositDate, maturityDate);
        System.out.println("########dateDiffMat : " + dateDiffMat);
        long period = dateDiffMat;
        Date currentDate = (Date) curDate.clone();
        difference = DateUtil.dateDiff(maturityDate, currentDate);
        HashMap sbInterestMap = new HashMap();
        sbInterestMap.put("PRODUCT_TYPE", "OA");
        HashMap sbProdIdMap = new HashMap();
        sbProdIdMap.put("BEHAVIOR", "SB");
        List lstProd = ClientUtil.executeQuery("getProdIdForOperative", sbProdIdMap);
        if (lstProd != null && lstProd.size() > 0) {
            HashMap prodMap = new HashMap();
            prodMap = (HashMap) lstProd.get(0);
            sbInterestMap.put("PROD_ID", prodMap.get("PROD_ID"));
        }
        sbInterestMap.put("CATEGORY_ID", calculationMap.get("CATEGORY_ID"));
        sbInterestMap.put("DEPOSIT_DT", maturityDate);
        sbInterestMap.put("AMOUNT", CommonUtil.convertObjToDouble(calculationMap.get("DEP_AMOUNT")));
        sbInterestMap.put("PERIOD", new Double(difference));
        System.out.println("########lstInt : " + sbInterestMap);
        List lstInt = (List) ClientUtil.executeQuery("icm.getInterestRates", sbInterestMap);
        System.out.println("########lstInt : " + lstInt);
        if (lstInt != null && lstInt.size() > 0) {
            HashMap sbRateOfInt = (HashMap) lstInt.get(0);
            rateOfInt = CommonUtil.convertObjToDouble(sbRateOfInt.get("ROI")).doubleValue();
            System.out.println("########dateDiffMat : " + difference);
        }   //up to this
        if (calculationMap.get("BEHAVES_LIKE").equals("FIXED") || calculationMap.get("BEHAVES_LIKE").equals("CUMMULATIVE")) {
            principal = CommonUtil.convertObjToDouble(calculationMap.get("MAT_AMOUNT")).doubleValue();
        }
        if (calculationMap.get("BEHAVES_LIKE").equals("RECURRING")||calculationMap.get("BEHAVES_LIKE").equals("THRIFT")||calculationMap.get("BEHAVES_LIKE").equals("BENEVOLENT")) { //thrif part added by rishad thrift also behaves like rd
            principal = CommonUtil.convertObjToDouble(calculationMap.get("CLEAR_BALANCE")).doubleValue();
        }
        sbCalcAmt = principal + (principal * rateOfInt * difference) / (36500);
        sbCalcAmt = sbCalcAmt - principal;
        sbCalcAmt = (double) getNearest((long) (sbCalcAmt * 100), 100) / 100;
        setDeathClaimAmt(String.valueOf(sbCalcAmt));
        if (!calculationMap.get("BEHAVES_LIKE").equals("RECURRING")) {
            ClientUtil.showMessageWindow("Death Claim Interest Details....\n"
                    + "\n Matured Date is : " + maturityDate
                    + "\n CurrentDate is : " + curDate
                    + "\n Principal : " + principal
                    + "\n Period : " + difference
                    + "\n RateOf Interest : " + rateOfInt
                    + "\n Interest Amount is : " + sbCalcAmt);
        }
        lstInt = null;
        lstProd = null;
        sbInterestMap = null;
        sbProdIdMap = null;
        return sbCalcAmt;
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

    private double holidayProvision(Date matDt, double amount, double interest, double rateOfInterest, String holidayProv) {
        double holidayAmt = 0.0;
        if (holidayProv.equals("Y")) {
            double diffholiday = 0.0;
            double diffweekday = 0.0;
            double count = 0;
            Date matutiryDt = (Date) curDate.clone();
            if (matDt != null && matDt.getDate() > 0) {
                matutiryDt.setDate(matDt.getDate());
                matutiryDt.setMonth(matDt.getMonth());
                matutiryDt.setYear(matDt.getYear());
            }
            Date currDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(curDate.clone()));
            HashMap weeklyOff = new HashMap();
            HashMap holidayMap = new HashMap();
            weeklyOff.put("NEXT_DATE", setProperDtFormat(matutiryDt));
            weeklyOff.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
            weeklyOff.put("CURR_DATE", currDt.clone());
            boolean week = false;
            boolean holiday = false;
            List lst = ClientUtil.executeQuery("checkHolidayProvisionTD", weeklyOff);
            if (lst != null && lst.size() > 0) {
                for (int j = 0; j < lst.size(); j++) {
                    count = count + 1;
                }
                holidayMap.put("NEXT_DATE", setProperDtFormat(matutiryDt));
                holidayMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
                lst = ClientUtil.executeQuery("checkWeeklyOffOD", holidayMap);
                if (lst != null && lst.size() > 0) {
                    count = count + 1;
                }
            } else {
                lst = ClientUtil.executeQuery("checkWeeklyOffOD", weeklyOff);
                if (lst != null && lst.size() > 0) {
                    count = count + 1;
                }
                lst = ClientUtil.executeQuery("checkHolidayProvisionTD", weeklyOff);
                if (lst != null && lst.size() > 0) {
                    for (int j = 0; j < lst.size(); j++) {
                        count = count + 1;
                    }
                }
            }
            diffweekday = DateUtil.dateDiff(matutiryDt, currDt);
            if (count == diffweekday) {
                count = count;
                //            }else if(count>1 && count == diffweekday-1)
                //                count = count;
            } else {
                count = 0;
            }
            if (count > 0) {
                deathFlag = true;
                double diff = count + diffholiday;
                holidayAmt = amount + (amount * rateOfInterest * diff) / (36500);
                holidayAmt = holidayAmt - amount;
                //                interest = interest + holidayAmt;
            }
            weeklyOff = null;
            holidayMap = null;
        }
        return holidayAmt;
    }
    private Date setProperDtFormat(Date dt) {
        Date tempDt = (Date) curDate.clone();
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }
	public static double monthDiff(Date d1,Date d2)
    {
        return (d1.getTime()-d2.getTime())/Avg_Millis_Per_Month;
    }
    private void setClosingDetails(HashMap mapData) {
        System.out.println("setClosingDetails : " + mapData);
        if (mapData.containsKey("INTEREST_DETAILS")) {
            HashMap interestCalcMap = (HashMap) mapData.get("INTEREST_DETAILS");
            HashMap intDrawnMap = (HashMap) mapData.get("INTEREST_DRAWN");
            HashMap custMap = new HashMap();
            double recIntVal =0;
            double amount = 0.0;
            double principal = 0.0;
            double interest = 0.0;
            double interestAmt = 0.0;
            double intDrawn = 0.0;
            double intCredit = 0.0;
            double balIntAmt = 0.0;
            double totAmt = 0.0;
            double commPeriod = 0.0;
            double prematureMinPeriod = 0.0;
            int weeklycalcNo = 0;
            double interestNotPaying = 0.0;
            String interestNotPayingMode = "";
            custMap.put("DEPOSIT_NO", mapData.get("DEPOSIT_NO"));
            List list = ClientUtil.executeQuery("getCustDepositNoBehavesLike", custMap);
            if (list != null && list.size() > 0) {
                custMap = (HashMap) list.get(0);
                behavesMap.put("BEHAVES_LIKE", custMap.get("BEHAVES_LIKE"));
                chkBehavesLike = CommonUtil.convertObjToStr(custMap.get("BEHAVES_LIKE"));
                if (!DOUBLING_SCHEME_BEHAVES_LIKE.equals("") && DOUBLING_SCHEME_BEHAVES_LIKE!=null && CommonUtil.convertObjToStr(custMap.get("BEHAVES_LIKE")).equals("CUMMULATIVE")
                    && getLblClosingType().equals(CommonConstants.PREMATURE_CLOSURE)) {
                    behavesMap.put("BEHAVES_LIKE", DOUBLING_SCHEME_BEHAVES_LIKE);
                }
                String holidayProv = null;
                String commisionMode = null;
                //                HashMap holidayProvMap = new HashMap();
                //                holidayProvMap.put("PROD_ID",custMap.get("PROD_ID"));
                //                List holidayList = ClientUtil.executeQuery("getHolidayInterestYesNo", holidayProvMap);
                //                if(holidayList!=null && holidayList.size()>0){
                //                    holidayProvMap = (HashMap)holidayList.get(0);
                //                }
                setLstProvDt(CommonUtil.convertObjToStr(custMap.get("LST_PROV_DT")));
                interestNotPaying = CommonUtil.convertObjToDouble(custMap.get("INTEREST_NOT_PAYING")).doubleValue();
                interestNotPayingMode = CommonUtil.convertObjToStr(custMap.get("INTEREST_NOT_PAYING_MODE"));
                holidayProv = CommonUtil.convertObjToStr(custMap.get("PAYINT_DEP_MATURITY"));
                commisionMode = CommonUtil.convertObjToStr(custMap.get("AGENT_COMMISION_MODE"));
                setDeathClaimAmt(String.valueOf("0"));
                setTotalBalance(CommonUtil.convertObjToStr(custMap.get("TOTAL_BALANCE")));
                double recurringAmount = CommonUtil.convertObjToDouble(custMap.get("CLEAR_BALANCE")).doubleValue();
                intDrawn = CommonUtil.convertObjToDouble(custMap.get("TOTAL_INT_DRAWN")).doubleValue();
                intCredit = CommonUtil.convertObjToDouble(custMap.get("TOTAL_INT_CREDIT")).doubleValue();
                totAmt = CommonUtil.convertObjToDouble(custMap.get("TOT_INT_AMT")).doubleValue();
                System.out.println("bal innntt in ob" + balIntAmt + "  f  " + intDrawn + " rg " + intCredit);
                // balIntAmt = (double)getNearest((long)(balIntAmt *100),100)/100;
                balIntAmt = getRound(balIntAmt, getInterestRound());
                //  intDrawn = (double)getNearest((long)(intDrawn *100),100)/100;
                intDrawn = getRound(intDrawn, getInterestRound());
                // intCredit = (double)getNearest((long)(intCredit *100),100)/100;
                intCredit = getRound(intCredit, getInterestRound());
                balIntAmt = intCredit - intDrawn;
                this.setClosingIntCr(String.valueOf(balIntAmt));
                this.setIntDrawn(String.valueOf(intDrawn));
                this.setIntCr(String.valueOf(intCredit));
                this.setLastIntAppDate(CommonUtil.convertObjToStr(map.get("INT_LAST_APPL_DT")));
                this.setTdsCollected(CommonUtil.convertObjToStr(new Double(CommonUtil.convertObjToDouble(getTdsCollected()).doubleValue())));
                this.setPrematureClosingDate(DateUtil.getStringDate(curDate));
                this.setPrematureClosingDate(DateUtil.getStringDate(curDate));
                setBalanceAmt(CommonUtil.convertObjToDouble(custMap.get("CLEAR_BALANCE")).doubleValue());
                setDelayedInstallments("");
                setChargeAmount("");
                Date depDate = null;
                double totalMonths = 0.0;
                double holidayAmt = 0.0;
                //                System.out.println("######bal : "+getBalanceAmt());
                //                String behave = CommonUtil.convertObjToStr(getProdID());
                //                HashMap behavesLikeMap = new HashMap();
                //                behavesLikeMap.put("PROD_ID", behave);
                //                List lst = ClientUtil.executeQuery("getBehavesLikeForDeposit",  behavesLikeMap);
                //                if(lst != null && lst.size() > 0) {
                //                    behavesMap = new HashMap();
                //                    behavesMap = (HashMap)lst.get(0);
                //After Maturing Deposit we have to calculate Operative Account Interest Rate we have to give the customer
                double rateOfInt = 0.0;
                double difference = 0.0;
                double uncompletedInterest = 0.0;
                double completedInterest = 0.0;
                double monthPeriod = 0.0;
                double yearPeriod = 0.0;
                long diffDay = 0;
                long act_run_period = 0;
                double freq = CommonUtil.convertObjToDouble(custMap.get("INTPAY_FREQ")).doubleValue();
                HashMap objMap = new HashMap();
                objMap.put("MODE", getLblClosingType());
                objMap.put("COMMAND", getCommand());
                if (((String) objMap.get("MODE")).equals(CommonConstants.PREMATURE_CLOSURE)) {
                    depDate = DateUtil.getDateMMDDYYYY(getDepositDate());
                    Date currDate = (Date) curDate.clone();
                    Date depDt = (Date) curDate.clone();
                    if (depDate.getDate() > 0) {
                        depDt.setDate(depDate.getDate());
                        depDt.setMonth(depDate.getMonth());
                        depDt.setYear(depDate.getYear());
                    }
                    HashMap prematureDateMap = new HashMap();
                    double period = 0;
                    String temp;
                    String mthPeriod = null;
                    String yrPeriod = null;
                    String dysPeriod = null;
                    double completedQuarter = 0.0;
                    int count = 0;
                    StringBuffer periodLetters = new StringBuffer();
                    if (behavesMap.get("BEHAVES_LIKE").equals("DAILY")) {
                        periodLetters.append(String.valueOf(0) + " Years ");
                        prematureDateMap.put("FROM_DATE", depDt);
                        prematureDateMap.put("TO_DATE", currDate);
                        Date deptDate = DateUtil.getDateMMDDYYYY(getDepositDate());
                        List lst = ClientUtil.executeQuery("periodRunMap", prematureDateMap);
                        if (lst != null && lst.size() > 0) {
                            prematureDateMap = (HashMap) lst.get(0);
                            monthPeriod = CommonUtil.convertObjToDouble(prematureDateMap.get("NO_OF_MONTHS")).doubleValue();
                            totalMonths = CommonUtil.convertObjToDouble(prematureDateMap.get("NO_OF_MONTHS")).doubleValue();
                            diffDay = CommonUtil.convertObjToLong(prematureDateMap.get("DAYS"));
                            act_run_period = CommonUtil.convertObjToLong(prematureDateMap.get("TOTAL_DIFF_DAYSAS"));
                        }
                        temp = "0";
                        mthPeriod = String.valueOf(monthPeriod);
                        if (mthPeriod != null) {
                            temp = mthPeriod.toString();
                        }
                        periodLetters.append(mthPeriod + " Months ");
                        //                        diffDay = DateUtil.dateDiff(depDt,currDate);
                    } else {
                        prematureDateMap.put("FROM_DATE", depDt);
                        prematureDateMap.put("TO_DATE", currDate);
                        int j = 0;
                        Date nextDate = depDt;
                        Date k = DateUtil.nextCalcDate(depDt, depDt, 30);
                        for (; DateUtil.dateDiff(k, currDate) > 0; k = DateUtil.nextCalcDate(depDt, k, 30)) {
                            j = j + 1;
                            nextDate = k;
                        }
                        monthPeriod = j;
                        totalMonths = j;
                        diffDay = DateUtil.dateDiff(nextDate, currDate);
                        if (behavesMap.get("BEHAVES_LIKE").equals("FIXED")) {
                            Date deptDate = DateUtil.getDateMMDDYYYY(getDepositDate());
                            List lst = ClientUtil.executeQuery("periodRunMap", prematureDateMap);
                            if (lst != null && lst.size() > 0) {
                                prematureDateMap = (HashMap) lst.get(0);
                                monthPeriod = CommonUtil.convertObjToDouble(prematureDateMap.get("NO_OF_MONTHS")).doubleValue();
                                totalMonths = CommonUtil.convertObjToDouble(prematureDateMap.get("NO_OF_MONTHS")).doubleValue();
                                diffDay = CommonUtil.convertObjToLong(prematureDateMap.get("DAYS"));
                                act_run_period = CommonUtil.convertObjToLong(prematureDateMap.get("TOTAL_DIFF_DAYSAS"));
                            }
                            long totCompPeriod = 0;
                            if (freq == 30) {
                                yrPeriod = String.valueOf(completedQuarter);
                                mthPeriod = String.valueOf(monthPeriod);
                            } else if (freq == 90) {
                                completedQuarter = monthPeriod / 3;
                                completedQuarter = (long) roundOffLower((long) (completedQuarter * 100), 100) / 100;
                                yrPeriod = String.valueOf(completedQuarter);
                                monthPeriod = monthPeriod - (completedQuarter * 3);
                                mthPeriod = String.valueOf(monthPeriod);
                                totCompPeriod = (long) (completedQuarter * 3);
                                totalMonths = totCompPeriod;
                            } else if (freq == 180) {
                                completedQuarter = monthPeriod / 6;
                                completedQuarter = (long) roundOffLower((long) (completedQuarter * 100), 100) / 100;
                                yrPeriod = String.valueOf(completedQuarter);
                                monthPeriod = monthPeriod - (completedQuarter * 6);
                                mthPeriod = String.valueOf(monthPeriod);
                                totCompPeriod = (long) (completedQuarter * 6);
                                totalMonths = totCompPeriod;
                            } else if (freq == 360) {
                                completedQuarter = monthPeriod / 12;
                                completedQuarter = (long) roundOffLower((long) (completedQuarter * 100), 100) / 100;
                                yrPeriod = String.valueOf(completedQuarter);
                                monthPeriod = monthPeriod - (completedQuarter * 12);
                                mthPeriod = String.valueOf(monthPeriod);
                                totCompPeriod = (long) (completedQuarter * 12);
                                totalMonths = totCompPeriod;
                            } else {
                                yrPeriod = String.valueOf(completedQuarter);
                                mthPeriod = String.valueOf("0.0");
                                totalMonths = 0; // Changed 27-jan-2012
                                diffDay = DateUtil.dateDiff(depDt, currDate);
                            }
                            if ((monthPeriod != 0) && (freq == 90 || freq == 180 || freq == 360)) {
                                for (int i = 0; i < totCompPeriod; i++) {
                                    if (deptDate.getDate() == 28) {
                                        deptDate = DateUtil.addDays(deptDate, 30, 2, true);
                                    } else {
                                        deptDate = DateUtil.addDays(deptDate, 30);
                                    }
                                }
                                diffDay = DateUtil.dateDiff(deptDate, currDate);
                                mthPeriod = String.valueOf("0.0");
                            }
                        } else if (behavesMap.get("BEHAVES_LIKE").equals("RECURRING")
                                || behavesMap.get("BEHAVES_LIKE").equals("CUMMULATIVE")) {
                            System.out.println("CUMMULATIVE jjjj1");
                            if (monthPeriod >= 3) {
                                System.out.println("2222222222222222");
                                completedQuarter = monthPeriod / 3;
                                completedQuarter = (long) roundOffLower((long) (completedQuarter * 100), 100) / 100;
                                yrPeriod = String.valueOf(completedQuarter);
                                monthPeriod = monthPeriod - (completedQuarter * 3);
                                if (behavesMap.get("BEHAVES_LIKE").equals("CUMMULATIVE")) {
                                    completedQuarter = completedQuarter * 3;
                                    Date cummDepDate = null;
                                    cummDepDate = DateUtil.getDateMMDDYYYY(getDepositDate());
                                    for (int i = 0; i < completedQuarter; i++) {
                                        if (cummDepDate.getDate() == 28) {
                                            cummDepDate = DateUtil.addDays(cummDepDate, 30, 2, true);
                                        } else {
                                            cummDepDate = DateUtil.addDays(cummDepDate, 30);
                                        }
                                    }
                                    diffDay = DateUtil.dateDiff(cummDepDate, currDate);
                                    mthPeriod = String.valueOf("0.0");
                                }
                                if (behavesMap.get("BEHAVES_LIKE").equals("RECURRING")) {
                                    mthPeriod = String.valueOf(monthPeriod);
                                }
                            } else {
                                yrPeriod = "0.0";
                                if (behavesMap.get("BEHAVES_LIKE").equals("CUMMULATIVE")) {
                                    Date cummDepDate = DateUtil.getDateMMDDYYYY(getDepositDate());
                                    diffDay = DateUtil.dateDiff(cummDepDate, currDate);
                                    mthPeriod = String.valueOf("0.0");
                                }
                                if (behavesMap.get("BEHAVES_LIKE").equals("RECURRING")) {
                                    mthPeriod = String.valueOf(monthPeriod);
                                }
                                //                                mthPeriod = String.valueOf(monthPeriod);
                            }
                        }
                        temp = "0";
                        if (yrPeriod != null) {
                            temp = yrPeriod.toString();
                        }
                        if (behavesMap.get("BEHAVES_LIKE").equals("FIXED")) {
                            if (freq == 90) {
                                periodLetters.append(yrPeriod + " Qters ");
                            } else if (freq == 180) {
                                periodLetters.append(yrPeriod + " Half Yearly ");
                            } else if (freq == 360) {
                                periodLetters.append(yrPeriod + " Yearly ");
                            } else {
                                periodLetters.append(yrPeriod + " Qters ");
                            }
                        } else {
                            periodLetters.append(yrPeriod + " Qters ");
                        }
                        temp = "0";
                        if (mthPeriod != null) {
                            temp = mthPeriod.toString();
                        }
                        periodLetters.append(mthPeriod + " Months ");
                    }
                    dysPeriod = String.valueOf(diffDay);
                    temp = "0";
                    if (dysPeriod != null) {
                        temp = dysPeriod.toString();
                    }
                    periodLetters.append(dysPeriod + " Days ");
                   // if (behavesMap.get("BEHAVES_LIKE").equals("FIXED") || behavesMap.get("BEHAVES_LIKE").equals("DAILY")) {
                    if(chkBehavesLike!=null && (chkBehavesLike.equals("FIXED") || chkBehavesLike.equals("DAILY"))){
                        setActualPeriodRun(act_run_period + " Days");
                    } else {
                        setActualPeriodRun(periodLetters.toString());
                    }
                } else {
                    setActualPeriodRun(CommonUtil.convertObjToStr(getPeriod()));
                }
                double interestgreater = 0.0;
                if (behavesMap.get("BEHAVES_LIKE").equals("FIXED")) {
                    //babu
                    Date depositedDate = DateUtil.getDateMMDDYYYY(getDepositDate());
                    Date currentDate = (Date) curDate.clone();
                    long diffDD = DateUtil.dateDiff(depositedDate, currentDate);//bbb
                    System.out.println("diffDD IN====" + diffDD);
                    if (isRdoTransfer_Yes() == true) {
                        transfer_out_mode = "Y";
                        this.setIntDrawn(getIntDrawn());
                        System.out.println("interest in ob1" + interest);
                        // interest = (double)getNearest((long)(interest *100),100)/100;
                        interest = getRound(interest, getInterestRound());
                        //    balIntAmt = (double)getNearest((long)(balIntAmt *100),100)/100;
                        balIntAmt = getRound(balIntAmt, getInterestRound());
                        System.out.println("jjjjjjj123========");
                        setClosingIntDb(String.valueOf(interest));
                        setClosingIntCr(String.valueOf(balIntAmt));
                        setClosingDisbursal(getPrinicipal()); //Maturity Amt
                        setDisburseAmt(CommonUtil.convertObjToDouble(this.getClosingDisbursal()).doubleValue());
                        if (getDisburseAmt() > principal) {
                            setLblPayRecDet("Payable");
                            this.setPayReceivable(String.valueOf(0.0));
                            this.setPermanentPayReceivable(String.valueOf(0.0));
                        } else {
                            setLblPayRecDet("Receivable");
                            this.setPayReceivable(String.valueOf(0.0));
                            this.setPermanentPayReceivable(String.valueOf(0.0));
                        }
                        if (totAmt == intDrawn) {
                            this.setClosingDisbursal(getBalance());
                            this.setPayReceivable(String.valueOf(0.0));
                            this.setPermanentPayReceivable(String.valueOf(0.0));
                        }
                    } else {
                        System.out.println("IN FIXED DEPOSIT PREMATURE CLOSING///////");
                        this.setClosingIntDb(String.valueOf(CommonUtil.convertObjToDouble(intDrawnMap.get("INT_AMT")).doubleValue()));
                        System.out.println("getBalance-----------------  :"+getBalance());
                        System.out.println("getPrinicipal()--------------- :"+getPrinicipal());
                        System.out.println("getPrinicipal()--------------- :"+getPrematureClosingRate());
                        principal = new Double(getBalance().length() > 1 ? getPrinicipal() : "0").doubleValue();
                        double rateOfInterest = CommonUtil.convertObjToDouble(getPrematureClosingRate()).doubleValue();
                        this.setMaturityValue(getPrinicipal());
                        freq = CommonUtil.convertObjToDouble(custMap.get("INTPAY_FREQ")).doubleValue();
                        double matAmt = CommonUtil.convertObjToDouble(custMap.get("MATURITY_AMT")).doubleValue();
                        double period = 0.0;
                        String PrematureApply = "";
                        String seniorBenifitApply = "";
                        double seniorBenifitRate = 0.0;
                        double seniorBenifit =0.0;
                        System.out.println("bbxvcvxbcvb nzvxcbzx :"+ objMap.get("MODE")+" :"+getLblClosingType());
                        if (((String) objMap.get("MODE")).equals(CommonConstants.PREMATURE_CLOSURE)) {
                            transfer_out_mode = "N";
                            Date fromDate = DateUtil.getDateMMDDYYYY(getDepositDate());
                            Date currDate = (Date) curDate.clone();
                            period = DateUtil.dateDiff(fromDate, currDate);
                            Date deathDt = null;
                            String category = "";
                            String discounted = CommonUtil.convertObjToStr(custMap.get("DISCOUNTED_RATE"));
                            List prematureApplyList = ClientUtil.executeQuery("getPrematureCloserApply", custMap);
                            if (prematureApplyList != null && prematureApplyList.size() > 0) {
                                HashMap cumMap = (HashMap) prematureApplyList.get(0);
                                PrematureApply = CommonUtil.convertObjToStr(cumMap.get("PREMATURE_CLOSURE_APPLY"));
                                seniorBenifitApply = CommonUtil.convertObjToStr(cumMap.get("NORMAL_RATE_FOR_SENIOR_CITIZEN"));
                                seniorBenifitRate = CommonUtil.convertObjToDouble(cumMap.get("SENIOR_BENIFIT_RATE"));                                
                           // }
                            if (PrematureApply != null && PrematureApply.length()>0 && PrematureApply.equals("SB Rate")) {
                                depDate = DateUtil.getDateMMDDYYYY(getDepositDate());
                                Date acopdate = DateUtil.getDateMMDDYYYY(getDepositDate());
                                Date matdt = DateUtil.getDateMMDDYYYY(getMaturityDate());
                                if (DateUtil.dateDiff(curDate, matdt) > 0) {
                                    matdt = curDate;
                                }
                                Date maturityDate = (Date) curDate.clone();
                                maturityDate.setDate(matdt.getDate());
                                maturityDate.setMonth(matdt.getMonth());
                                maturityDate.setYear(matdt.getYear());

                                Date acopeningDt = (Date) curDate.clone();
                                acopeningDt.setDate(acopdate.getDate());
                                acopeningDt.setMonth(acopdate.getMonth());
                                acopeningDt.setYear(acopdate.getYear());
                                long periodSB = DateUtil.dateDiff(acopeningDt, maturityDate);
                                HashMap rdPaidInst = new HashMap();
                                rdPaidInst.put("DEPOSIT_DT", DateUtil.getDateMMDDYYYY(getDepositDate()));
                                rdPaidInst.put("PRODID", getProdID());
                                rdPaidInst.put("CUSTID", getCustomerID());
                                rdPaidInst.put("DEPOSITNO", getTxtDepositNo());
                                rdPaidInst.put("PERIOD", new Long(periodSB));
                                List SbRateList = ClientUtil.executeQuery("getSBrateDetails", rdPaidInst);
                                if (SbRateList != null && SbRateList.size() > 0) {
                                    rdPaidInst = (HashMap) SbRateList.get(0);
                                    rateOfInterest = CommonUtil.convertObjToDouble(rdPaidInst.get("ROI")).doubleValue();

                                    if (custMap.containsKey("CATEGORY") && custMap.get("CATEGORY").equals("SENIOR_CITIZENS")) {

                                        if (seniorBenifitApply != null && seniorBenifitApply.length()>0 && seniorBenifitApply.equals("N")) {
                                            rateOfInterest = rateOfInterest + seniorBenifitRate;
                                        } else {
                                            rateOfInterest = rateOfInterest;
                                        }
                                        
                                    } 
                                    setRateApplicable(String.valueOf(rateOfInterest));
                                    setPrematureClosingRate(String.valueOf(rateOfInterest));

                                }
                            }
                            }
                            HashMap hmap = new HashMap();
                            hmap.put("CUST_ID", getCustomerID());
                            hmap.put("DEPOSIT_NO", getTxtDepositNo());
                            List seniorList = ClientUtil.executeQuery("getSeniorDetails", hmap);
                            if (seniorList != null && seniorList.size() > 0) {
                                hmap = (HashMap) seniorList.get(0);
                                category = CommonUtil.convertObjToStr(hmap.get("CATEGORY"));
                                deathDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hmap.get("DEATH_DT")));
                            }
                            if (category.equals("SENIOR_CITIZENS") && custMap.get("DEATH_CLAIM") != null && custMap.get("DEATH_CLAIM").equals("Y") && deathFlag == false) {
                                //                                ClientUtil.showMessageWindow(""
                                if (DateUtil.dateDiff(deathDt, DateUtil.getDateMMDDYYYY(getMaturityDate())) > 0) {
                                    HashMap calculationMap = new HashMap();
                                    calculationMap.put("DEP_AMOUNT", custMap.get("DEPOSIT_AMT"));
                                    calculationMap.put("CLEAR_BALANCE", custMap.get("CLEAR_BALANCE"));
                                    calculationMap.put("MAT_AMOUNT", custMap.get("MATURITY_AMT"));
                                    calculationMap.put("CATEGORY_ID", custMap.get("CATEGORY"));
                                    calculationMap.put("BEHAVES_LIKE", behavesMap.get("BEHAVES_LIKE"));
                                    calculationMap.put("DEATH_DT", deathDt);
                                    calculationMap.put("RATE_OF_INTEREST", new Double(rateOfInterest));
                                    calculationMap.put("NORMAL", "PREMATURE");
                                    interest = CalculationForSeniorCitizenDeath(calculationMap);
                                }
                                System.out.println("interest senior IN====" + interest);
                            } else {
                                if (freq == 30 && discounted != null && discounted.equals("Y")) {
                                    double intAmt = 0.0;
                                    System.out.println("totalMonths IN====" + totalMonths);
                                    if (totalMonths > 0) {
                                        intAmt = rateOfInterest / 4 / (Math.pow((1 + (rateOfInterest / 1200)), 2) + (1 + (rateOfInterest / 1200)) + 1);//bb
                                        //   intAmt = rateOfInterest/4 / (Math.pow((1+(rateOfInterest/36500)),2) + (1+(rateOfInterest/36500)) +1);
                                        double calcAmt = principal / 100;
                                        intAmt = intAmt * calcAmt;
                                        // intAmt = (double)getNearest((long)(intAmt *100),100)/100;
                                        intAmt = getRound(intAmt, getInterestRound());
                                        intAmt = intAmt * totalMonths;//bb
                                        //    intAmt = intAmt * diffDD;
                                    }
                                    if (diffDay > 0) {
                                        principal = principal;// + intAmt;
                                        amount = principal + (principal * rateOfInterest * diffDay) / (36500);//bbb
                                        interestgreater = amount - principal;
                                    }
                                    interest = intAmt + interestgreater;
                                } else if (freq == 0 && diffDay > 0 && discounted != null && discounted.equals("Y")) {
                                    double lessamount = principal + (principal * rateOfInterest * diffDay) / (36500);
                                    interest = lessamount - principal;

                                } else {
                                    if (totalMonths > 0 || diffDay > 0) {
                                        // double greateramount = principal+(principal * rateOfInterest * totalMonths) /1200;
                                        System.out.println("principal IN====" + principal + " rateOfInterest==" + rateOfInterest + " diffDay==" + diffDay);
                                        double greateramount = principal + (principal * rateOfInterest * diffDD) / 36500;//bbb
                                        interestgreater = greateramount - principal;
                                    }
                                    double interestless = 0.0;
                                    /*
                                    if(diffDay>0){
                                    System.out.println("principal11 IN===="+principal+" rateOfInterest="+rateOfInterest+" diffDay="+diffDay);
                                    double lessamount = principal+(principal * rateOfInterest * diffDay) /(36500);
                                    interestless = lessamount - principal;
                                    }*/
                                    interest = interestgreater + interestless;
                                    
                                }
                            }
                        } else {
                            transfer_out_mode = "NO";
                            String category = "";// Added by nithya on 01-12-2018 for KD 281 - CANT AUTHORIZE THE FIXED DEPOSIT CLOSING.(DEATH MARKED CUSTOMER)
                            interest = CommonUtil.convertObjToDouble(custMap.get("TOT_INT_AMT")).doubleValue();
                            Date matDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(custMap.get("MATURITY_DT")));
                            amount = CommonUtil.convertObjToDouble(custMap.get("MATURITY_AMT")).doubleValue();
                            rateOfInterest = CommonUtil.convertObjToDouble(getPrematureClosingRate()).doubleValue();
                            Date matDt = (Date) curDate.clone();
                            Date deathDt = null;
                            Date dob = null;
                            if (matDate.getDate() > 0) {
                                matDt.setYear(matDate.getYear());
                                matDt.setMonth(matDate.getMonth());
                                matDt.setDate(matDate.getDate());
                            }
                            holidayAmt = holidayProvision(matDt, amount, interest, rateOfInterest, holidayProv);

                            HashMap hmap = new HashMap();
                            hmap.put("CUST_ID", getCustomerID());
                            hmap.put("DEPOSIT_NO", getTxtDepositNo());
                            List seniorList = ClientUtil.executeQuery("getSeniorDetails", hmap);
                            if (seniorList != null && seniorList.size() > 0) {
                                hmap = (HashMap) seniorList.get(0);
                                category = CommonUtil.convertObjToStr(hmap.get("CATEGORY"));
                                deathDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hmap.get("DEATH_DT")));
                            }
                            if (category.equals("SENIOR_CITIZENS") && custMap.get("DEATH_CLAIM") != null && custMap.get("DEATH_CLAIM").equals("Y") && deathFlag == false) {
                                //                                ClientUtil.showMessageWindow(""
                                if (DateUtil.dateDiff(deathDt, DateUtil.getDateMMDDYYYY(getMaturityDate())) > 0) {
                                    HashMap calculationMap = new HashMap();
                                    calculationMap.put("DEP_AMOUNT", custMap.get("DEPOSIT_AMT"));
                                    calculationMap.put("CLEAR_BALANCE", custMap.get("CLEAR_BALANCE"));
                                    calculationMap.put("MAT_AMOUNT", custMap.get("MATURITY_AMT"));
                                    calculationMap.put("CATEGORY_ID", custMap.get("CATEGORY"));
                                    calculationMap.put("BEHAVES_LIKE", behavesMap.get("BEHAVES_LIKE"));
                                    calculationMap.put("DEATH_DT", deathDt);
                                    calculationMap.put("RATE_OF_INTEREST", new Double(rateOfInterest));
                                    calculationMap.put("NORMAL", "NORMAL");

                                    interest = CalculationForSeniorCitizenDeath(calculationMap);
                                }
                            }

                            if (custMap.get("DEATH_CLAIM") != null && custMap.get("DEATH_CLAIM").equals("Y") && deathFlag == false) {
                                HashMap calculationMap = new HashMap();
                                calculationMap.put("DEP_AMOUNT", custMap.get("DEPOSIT_AMT"));
                                calculationMap.put("CLEAR_BALANCE", custMap.get("CLEAR_BALANCE"));
                                calculationMap.put("MAT_AMOUNT", custMap.get("MATURITY_AMT"));
                                calculationMap.put("CATEGORY_ID", custMap.get("CATEGORY"));
                                calculationMap.put("BEHAVES_LIKE", behavesMap.get("BEHAVES_LIKE"));
                                double deathAmt = 0.0;
                                if(custMap.containsKey("DEATH_CLAIM_INT") && custMap.get("DEATH_CLAIM_INT")!= null && CommonUtil.convertObjToStr(custMap.get("DEATH_CLAIM_INT")).equalsIgnoreCase("Y")){// Added by nithya on 13-08-2019 for KD 583
                                    deathAmt = simpleInterestCalculation(calculationMap);
                                }
                                interest = interest + deathAmt;
                                setDeathClaim("Y");
                                calculationMap = null;
                            }
                        }
                        this.setIntDrawn(getIntDrawn());
                        interest = interest + holidayAmt;
                        //  interest = (double)getNearest((long)(interest *100),100)/100;
                        interest = getRound(interest, getInterestRound());
                        if (holidayAmt > 0) {
                            ClientUtil.showMessageWindow("Holiday interest amount is :" + holidayAmt);
                        }

                        double availableBal = CommonUtil.convertObjToDouble(custMap.get("TOTAL_BALANCE")).doubleValue();
                        if (availableBal == 0) {
                            setClosingDisbursal(CommonUtil.convertObjToStr("0"));
                            this.setPayReceivable(CommonUtil.convertObjToStr("0"));
                            this.setPermanentPayReceivable(String.valueOf(0.0));
                            setPrematureClosingRate(CommonUtil.convertObjToStr("0"));
                            ClientUtil.showAlertWindow("No Balance in the account. Do you want to really close...");
                        } else {
                            //  balIntAmt = (double)getNearest((long)(balIntAmt *100),100)/100;
                            balIntAmt = getRound(balIntAmt, getInterestRound());
                            System.out.println("jjjjjj333333333333333");
                            setClosingIntDb(String.valueOf(interest));
                            setClosingIntCr(String.valueOf(balIntAmt));
                            setClosingDisbursal(
                                    CommonUtil.convertObjToStr(new Double(
                                    CommonUtil.convertObjToDouble(getBalance()).doubleValue()
                                    + CommonUtil.convertObjToDouble(getClosingIntDb()).doubleValue()
                                    - CommonUtil.convertObjToDouble(getIntDrawn()).doubleValue()))); //Maturity Amt
                            setDisburseAmt(CommonUtil.convertObjToDouble(this.getClosingDisbursal()).doubleValue());
                            if (getDisburseAmt() > principal) {
                                setLblPayRecDet("Payable");
                                this.setPayReceivable(
                                        CommonUtil.convertObjToStr(new Double(
                                        CommonUtil.convertObjToDouble(getClosingIntDb()).doubleValue()
                                        - CommonUtil.convertObjToDouble(getIntDrawn()).doubleValue())));

                                this.setPermanentPayReceivable(CommonUtil.convertObjToStr(new Double(
                                        CommonUtil.convertObjToDouble(getClosingIntDb()).doubleValue()
                                        - CommonUtil.convertObjToDouble(getIntDrawn()).doubleValue())));
                            } else {
                                setLblPayRecDet("Receivable");
                                this.setPayReceivable(
                                        CommonUtil.convertObjToStr(new Double(
                                        CommonUtil.convertObjToDouble(getIntDrawn()).doubleValue()
                                        - CommonUtil.convertObjToDouble(getClosingIntDb()).doubleValue())));

                                this.setPermanentPayReceivable(CommonUtil.convertObjToStr(new Double(
                                        CommonUtil.convertObjToDouble(getIntDrawn()).doubleValue()
                                        - CommonUtil.convertObjToDouble(getClosingIntDb()).doubleValue())));
                                //                                System.out.println("#########payRecValue else :" +this.getPayReceivable());
                            }
                            double negValue = CommonUtil.convertObjToDouble(getPayReceivable()).doubleValue();
                            if (negValue < 0) {
                                negValue = negValue * -1;
                                this.setPayReceivable(String.valueOf(negValue));
                                this.setPermanentPayReceivable(String.valueOf(negValue));
                            }
                            if (CommonUtil.convertObjToDouble(getClosingIntDb()).doubleValue() == intDrawn) {
                                this.setClosingDisbursal(getBalance());
                                this.setPayReceivable(String.valueOf(0.0));
                                this.setPermanentPayReceivable(String.valueOf(0.0));
                            }
                        }
                    }
                } else if (behavesMap.get("BEHAVES_LIKE").equals("RECURRING")) {
                    setLblBalanceDeposit("Installment Amount");
                    this.setMaturityValue(String.valueOf(CommonUtil.convertObjToDouble(custMap.get("MATURITY_AMT")).doubleValue()));
                    principal = new Double(getPrinicipal().length() > 1 ? getPrinicipal() : "0").doubleValue();
                    double rateOfInterest = CommonUtil.convertObjToDouble(getPrematureClosingRate()).doubleValue();
                    this.setBalanceAmt(recurringAmount);
                    long period = 0;
                    int count = 0;
                    long diff = 0;
                    double rdApplyRate = 0.0;
                    String rdApply = "";
                    String rdClosingOtherChargeApply = "";
                    String fullAmtToDefaulterAfterMaturity = "N";
                    Date mat = null;
                    Date frm = DateUtil.getDateMMDDYYYY(getDepositDate());
                    if (((String) objMap.get("MODE")).equals(CommonConstants.PREMATURE_CLOSURE)) {
                        mat = (Date) curDate.clone();
                    } else {
                        mat = DateUtil.getDateMMDDYYYY(getMaturityDate());
                    }

                    periodNoOfDays = DateUtil.dateDiff(frm, mat);
                    period = periodNoOfDays - diff;
                    if (isRdoTransfer_Yes() == true) {
                        transfer_out_mode = "Y";
                        principal = new Double(getBalance().length() > 1 ? getBalance() : "0").doubleValue();
                        //                            System.out.println("######interest : "+this.getClosingIntDb());
                        //                            System.out.println("######interest : "+interest);
                        this.setIntDrawn(getIntDrawn());
//                        interest = (double)getNearest((long)(interest *100),100)/100;
                        interest = getRound(interest, getInterestRound());
                        // balIntAmt = (double)getNearest((long)(balIntAmt *100),100)/100;
                        balIntAmt = getRound(balIntAmt, getInterestRound());
                        System.out.println("jjjjjj44444444444444");
                        setClosingIntDb(String.valueOf(interest));
                        setClosingIntCr(String.valueOf(balIntAmt));
                        setClosingDisbursal(getBalance()); //Maturity Amt
                        setDisburseAmt(CommonUtil.convertObjToDouble(this.getClosingDisbursal()).doubleValue());
                        //                            System.out.println("######DisburseAmt : "+getDisburseAmt());
                        //                            System.out.println("###### principal: "+principal);
                        if (getDisburseAmt() > principal) {
                            setLblPayRecDet("Payable");
                            this.setPayReceivable(String.valueOf(0.0));
                            this.setPermanentPayReceivable(String.valueOf(0.0));
                            //                                System.out.println("#########payRecValue :" +this.getPayReceivable());
                        } else {
                            setLblPayRecDet("Receivable");
                            this.setPayReceivable(String.valueOf(0.0));
                            this.setPermanentPayReceivable(String.valueOf(0.0));
                            //                                System.out.println("#########payRecValue else :" +this.getPayReceivable());
                        }
                        if (totAmt == intDrawn) {
                            this.setClosingDisbursal(getBalance());
                            this.setPayReceivable(String.valueOf(0.0));
                            this.setPermanentPayReceivable(String.valueOf(0.0));
                        }
                        //                            System.out.println("intPayFreq :"+custMap.get("INTPAY_FREQ"));
                    } else {
                        //                            System.out.println("######recurring this.setBalanceAmt : "+this.getMaturityValue());
                        //                            System.out.println("###### periodNoOfDays : "+periodNoOfDays);
                        //                        if(periodNoOfDays >=46){
                        double yrPeriod = 0.0;
                        double mnPeriod = 0.0;
                        double diffDays = 0.0;
                        double calcIntAmt = 0.0;
                        double balSimpAmt = 0.0;
                        double amount1 = 0.0;
                        double fdMonths = 0.0;
                        double greateramount = 0.0;
                        double compoundInt = 0.0;
                        double interestless = 0.0;
                        double calcFDMonthSecondIntAmt = 0.0;
                        double calcFDDaysSecondIntAmt = 0.0;
                        double calcMnIntAmt = 0.0;
                        double calcFDMonthIntAmt = 0.0;
                        double calcFDDaysIntAmt = 0.0;
                        if (((String) objMap.get("MODE")).equals(CommonConstants.PREMATURE_CLOSURE)) {//Premature closure calculation..
                            transfer_out_mode = "N";
                            long totInstallments = CommonUtil.convertObjToLong(custMap.get("TOTAL_INSTALL_PAID"));
                            if (totInstallments >= 3) { // if installment is more than three means calcualting for recurring formula...
                                if (totInstallments > totalMonths) {
                                    period = (long) totalMonths / 3;
                                } else {
                                    period = totInstallments / 3;
                                }
                                period = (long) roundOffLower((long) (period * 100), 100) / 100;
                                amount = principal * (Math.pow((1 + rateOfInterest / 400), period) - 1) / (1 - Math.pow((1 + rateOfInterest / 400), -1 / 3.0));
                                amount1 = amount;
                                if (totInstallments > totalMonths) {
                                    mnPeriod = totalMonths - (period * 3);
                                } else {
                                    mnPeriod = totInstallments - (period * 3);
                                }
                                interest = amount - (principal * period * 3);
                                Date fromDate = DateUtil.getDateMMDDYYYY(getDepositDate());
                                //                                    Date nextDate = fromDate;
                                //                                    Date k = DateUtil.nextCalcDate(fromDate,fromDate,30);
                                //                                    for(int i=0; i<period; i++)
                                //                                        fromDate = DateUtil.nextCalcDate(fromDate,k, 30);
                                //                                    diffDay = DateUtil.dateDiff(nextDate, currDate);
                                period = period * 3;
                                for (int i = 0; i < period; i++) {
                                    fromDate = DateUtil.addDays(fromDate, 30);
                                }
                                Date currDate = (Date) curDate.clone();
                                mnPeriod = totInstallments - period;
                                HashMap dayendMap = new HashMap();//balance installment calculating from trans_dt to curr_dt
                                double dayendBalance = 0.0;
                                double dayendBal = 0.0;
                                if (mnPeriod > 0 && mnPeriod <= 2) {
                                    HashMap depositRecMap = new HashMap();//balance installment calculating from trans_dt to curr_dt
                                    depositRecMap.put("DEPOSIT_NO", mapData.get("DEPOSIT_NO") + "_1");
                                    depositRecMap.put("START_DT", fromDate);
                                    depositRecMap.put("SL_NO", new Double(period + 1));
                                    List lstRecord = ClientUtil.executeQuery("getDepNotProperRecurring", depositRecMap);
                                    if (lstRecord != null && lstRecord.size() > 0) {
                                        depositRecMap = (HashMap) lstRecord.get(0);
                                        Date firstTransDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositRecMap.get("TRANS_DT")));
                                        Date dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositRecMap.get("DUE_DATE")));
                                        //                                            diffDays = DateUtil.dateDiff(dueDate,firstTransDt);
                                        //                                            if(diffDays>0){
                                        //                                                principal = new Double(getPrinicipal().length()>1 ? getPrinicipal() : "0").doubleValue();
                                        //                                                if(mnPeriod == 2)
                                        //                                                    principal = principal * (totInstallments-mnPeriod);
                                        //                                                else
                                        //                                                    principal = principal * (totInstallments - 1);
                                        //                                                greateramount = dayendBalance+(dayendBalance * rateOfInterest * diffDays) /(36500);
                                        //                                                calcMnIntAmt = greateramount - dayendBal;
                                        //                                            }
                                        if (firstTransDt == null) {
                                            firstTransDt = dueDate;
                                        }
                                        if (firstTransDt != null && (DateUtil.dateDiff(firstTransDt, dueDate) > 0)) {
                                            firstTransDt = dueDate;
                                        } else {
                                            firstTransDt = firstTransDt;
                                        }
                                        dayendMap.put("ACT_NUM", mapData.get("DEPOSIT_NO"));
                                        dayendMap.put("DAY_END_DT", firstTransDt);
                                        List lstDay = ClientUtil.executeQuery("getDayendBalanceDeposits", dayendMap);
                                        if (lstDay != null && lstDay.size() > 0) {
                                            dayendMap = (HashMap) lstDay.get(0);
                                            dayendBalance = CommonUtil.convertObjToDouble(dayendMap.get("AMT")).doubleValue();
                                            dayendBal = CommonUtil.convertObjToDouble(dayendMap.get("AMT")).doubleValue();
                                        }
                                        mnPeriod = mnPeriod - 1;
                                        if (mnPeriod > 0) {
                                            depositRecMap.put("DEPOSIT_NO", mapData.get("DEPOSIT_NO") + "_1");
                                            depositRecMap.put("START_DT", fromDate);
                                            depositRecMap.put("SL_NO", new Double(period + 2));
                                            lstRecord = ClientUtil.executeQuery("getDepNotProperRecurring", depositRecMap);
                                            if (lstRecord != null && lstRecord.size() > 0) {
                                                depositRecMap = (HashMap) lstRecord.get(0);
                                                Date secondTransDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositRecMap.get("TRANS_DT")));
                                                dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositRecMap.get("DUE_DATE")));
                                                diffDays = DateUtil.dateDiff(firstTransDt, secondTransDt);
                                                if (diffDays > 0) {
                                                    greateramount = dayendBalance + (dayendBalance * rateOfInterest * diffDays) / (36500);
                                                    calcFDDaysIntAmt = greateramount - dayendBal;
                                                }
                                                for (int i = 0; i < mnPeriod; i++) {
                                                    fromDate = DateUtil.addDays(fromDate, 30);
                                                }
                                                if (secondTransDt == null) {
                                                    secondTransDt = dueDate;
                                                }
                                                System.out.println("###### : " + DateUtil.dateDiff(secondTransDt, dueDate));
                                                if (secondTransDt != null && (DateUtil.dateDiff(secondTransDt, dueDate) > 0)) {
                                                    secondTransDt = dueDate;
                                                } else {
                                                    secondTransDt = secondTransDt;
                                                }
                                                if (DateUtil.dateDiff(firstTransDt, secondTransDt) == 0) {
                                                    firstTransDt = fromDate;
                                                }
                                                dueDate = secondTransDt;
                                                diffDays = DateUtil.dateDiff(dueDate, currDate);
                                                dayendMap = new HashMap();//balance installment calculating from trans_dt to curr_dt
                                                dayendMap.put("ACT_NUM", mapData.get("DEPOSIT_NO"));
                                                dayendMap.put("DAY_END_DT", secondTransDt);
                                                lstDay = ClientUtil.executeQuery("getDayendBalanceDeposits", dayendMap);
                                                if (lstDay != null && lstDay.size() > 0) {
                                                    dayendMap = (HashMap) lstDay.get(0);
                                                    dayendBalance = CommonUtil.convertObjToDouble(dayendMap.get("AMT")).doubleValue();
                                                    dayendBal = CommonUtil.convertObjToDouble(dayendMap.get("AMT")).doubleValue();
                                                }
                                                if (diffDays > 0) {
                                                    greateramount = dayendBalance + (dayendBalance * rateOfInterest * diffDays) / (36500);
                                                    calcFDMonthSecondIntAmt = greateramount - dayendBal;
                                                }
                                            }
                                        } else {
                                            diffDays = DateUtil.dateDiff(firstTransDt, currDate);
                                            if (diffDays > 0) {
                                                greateramount = dayendBalance + (dayendBalance * rateOfInterest * diffDays) / (36500);
                                                calcFDMonthSecondIntAmt = greateramount - dayendBal;
                                            }
                                        }
                                    }
                                    lstRecord = null;
                                    depositRecMap = null;
                                } else if (mnPeriod == 0) {//incase only 3 installments paid,from 3rd inst paid dt to till now simple interest.
                                    HashMap depositRecMap = new HashMap();//balance installment calculating from trans_dt to curr_dt
                                    depositRecMap.put("DEPOSIT_NO", mapData.get("DEPOSIT_NO") + "_1");
                                    depositRecMap.put("START_DT", fromDate);
                                    depositRecMap.put("SL_NO", new Double(period));
                                    List lstRecord = ClientUtil.executeQuery("getDepNotProperRecurring", depositRecMap);
                                    if (lstRecord != null && lstRecord.size() > 0) {
                                        depositRecMap = (HashMap) lstRecord.get(0);
                                        Date firstTransDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositRecMap.get("TRANS_DT")));
                                        Date dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositRecMap.get("DUE_DATE")));
                                        dueDate = DateUtil.nextCalcDate(dueDate, dueDate, 30);
                                        //                                            dueDate = DateUtil.addDays(dueDate, 30);
                                        if (firstTransDt == null) {
                                            firstTransDt = dueDate;
                                        }
                                        if (firstTransDt != null && (DateUtil.dateDiff(firstTransDt, dueDate) > 0)) {
                                            firstTransDt = dueDate;
                                        } else {
                                            firstTransDt = firstTransDt;
                                        }
                                        diffDays = DateUtil.dateDiff(firstTransDt, currDate);
                                        dayendMap = new HashMap();//balance installment calculating from trans_dt to curr_dt
                                        dayendMap.put("ACT_NUM", mapData.get("DEPOSIT_NO"));
                                        dayendMap.put("DAY_END_DT", firstTransDt);
                                        List lstDay = ClientUtil.executeQuery("getDayendBalanceDeposits", dayendMap);
                                        if (lstDay != null && lstDay.size() > 0) {
                                            dayendMap = (HashMap) lstDay.get(0);
                                            dayendBalance = CommonUtil.convertObjToDouble(dayendMap.get("AMT")).doubleValue();
                                            dayendBal = CommonUtil.convertObjToDouble(dayendMap.get("AMT")).doubleValue();
                                        }
                                        if (diffDays > 0) {
                                            greateramount = dayendBalance + (dayendBalance * rateOfInterest * diffDays) / (36500);
                                            calcFDDaysIntAmt = greateramount - dayendBal;
                                        }
                                    }
                                    lstRecord = null;
                                    depositRecMap = null;
                                } else if (mnPeriod >= 3) {
                                    HashMap depositRecMap = new HashMap();//balance installment calculating from trans_dt to curr_dt
                                    depositRecMap.put("DEPOSIT_NO", mapData.get("DEPOSIT_NO") + "_1");
                                    depositRecMap.put("START_DT", fromDate);
                                    depositRecMap.put("SL_NO", new Double(period + 1));
                                    List lstRecord = ClientUtil.executeQuery("getDepNotProperRecurring", depositRecMap);
                                    if (lstRecord != null && lstRecord.size() > 0) {
                                        depositRecMap = (HashMap) lstRecord.get(0);
                                        Date firstTransDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositRecMap.get("TRANS_DT")));
                                        Date dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositRecMap.get("DUE_DATE")));
                                        if (firstTransDt == null) {
                                            firstTransDt = dueDate;
                                        }
                                        if (firstTransDt != null && (DateUtil.dateDiff(firstTransDt, dueDate) > 0)) {
                                            firstTransDt = dueDate;
                                        } else {
                                            firstTransDt = firstTransDt;
                                        }
                                        dayendMap = new HashMap();
                                        dayendMap.put("ACT_NUM", mapData.get("DEPOSIT_NO"));
                                        dayendMap.put("DAY_END_DT", firstTransDt);
                                        List lstDay = ClientUtil.executeQuery("getDayendBalanceDeposits", dayendMap);
                                        if (lstDay != null && lstDay.size() > 0) {
                                            dayendMap = (HashMap) lstDay.get(0);
                                            dayendBalance = CommonUtil.convertObjToDouble(dayendMap.get("AMT")).doubleValue();
                                            dayendBal = CommonUtil.convertObjToDouble(dayendMap.get("AMT")).doubleValue();
                                        }
                                        mnPeriod = mnPeriod - 1;
                                        if (mnPeriod > 0) {
                                            depositRecMap = new HashMap();
                                            depositRecMap.put("DEPOSIT_NO", mapData.get("DEPOSIT_NO") + "_1");
                                            depositRecMap.put("START_DT", fromDate);
                                            depositRecMap.put("SL_NO", new Double(period + 2));
                                            lstRecord = ClientUtil.executeQuery("getDepNotProperRecurring", depositRecMap);
                                            if (lstRecord != null && lstRecord.size() > 0) {
                                                depositRecMap = (HashMap) lstRecord.get(0);
                                                Date secondTransDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositRecMap.get("TRANS_DT")));
                                                dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositRecMap.get("DUE_DATE")));
                                                if (secondTransDt == null) {
                                                    secondTransDt = dueDate;
                                                }
                                                if (firstTransDt != null && (DateUtil.dateDiff(firstTransDt, secondTransDt) > 0)) {
                                                    secondTransDt = dueDate;
                                                } else {
                                                    secondTransDt = secondTransDt;
                                                }
                                                diffDays = DateUtil.dateDiff(firstTransDt, secondTransDt);
                                                if (diffDays > 0) {
                                                    greateramount = dayendBalance + (dayendBalance * rateOfInterest * diffDays) / (36500);
                                                    calcFDDaysIntAmt = greateramount - dayendBal;
                                                }
                                                dayendMap = new HashMap();
                                                dayendMap.put("ACT_NUM", mapData.get("DEPOSIT_NO"));
                                                dayendMap.put("DAY_END_DT", secondTransDt);
                                                lstDay = ClientUtil.executeQuery("getDayendBalanceDeposits", dayendMap);
                                                if (lstDay != null && lstDay.size() > 0) {
                                                    dayendMap = (HashMap) lstDay.get(0);
                                                    dayendBalance = CommonUtil.convertObjToDouble(dayendMap.get("AMT")).doubleValue();
                                                    dayendBal = CommonUtil.convertObjToDouble(dayendMap.get("AMT")).doubleValue();
                                                }
                                                if (diffDays > 0) {
                                                    greateramount = dayendBalance + (dayendBalance * rateOfInterest * diffDays) / (36500);
                                                    calcFDMonthIntAmt = greateramount - dayendBal;
                                                }
                                                mnPeriod = mnPeriod - 1;
                                                if (mnPeriod > 0) {
                                                    for (int i = 0; i < mnPeriod; i++) {
                                                        fromDate = DateUtil.addDays(fromDate, 30);
                                                    }
                                                    Date thirdTransDt = null;
                                                    depositRecMap = new HashMap();
                                                    depositRecMap.put("DEPOSIT_NO", mapData.get("DEPOSIT_NO") + "_1");
                                                    depositRecMap.put("START_DT", fromDate);
                                                    depositRecMap.put("SL_NO", new Double(period + 3));
                                                    lstRecord = ClientUtil.executeQuery("getDepNotProperRecurring", depositRecMap);
                                                    if (lstRecord != null && lstRecord.size() > 0) {
                                                        depositRecMap = (HashMap) lstRecord.get(0);
                                                        dayendMap = new HashMap();
                                                        thirdTransDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositRecMap.get("TRANS_DT")));
                                                        dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositRecMap.get("DUE_DATE")));
                                                        dayendMap.put("ACT_NUM", mapData.get("DEPOSIT_NO"));
                                                        dayendMap.put("DAY_END_DT", thirdTransDt);
                                                        if (thirdTransDt == null) {
                                                            thirdTransDt = dueDate;
                                                        }
                                                        if (firstTransDt != null && (DateUtil.dateDiff(secondTransDt, thirdTransDt) > 0)) {
                                                            thirdTransDt = dueDate;
                                                        } else {
                                                            thirdTransDt = thirdTransDt;
                                                        }
                                                        lstDay = ClientUtil.executeQuery("getDayendBalanceDeposits", dayendMap);
                                                        if (lstDay != null && lstDay.size() > 0) {
                                                            dayendMap = (HashMap) lstDay.get(0);
                                                            dayendBalance = CommonUtil.convertObjToDouble(dayendMap.get("AMT")).doubleValue();
                                                            dayendBal = CommonUtil.convertObjToDouble(dayendMap.get("AMT")).doubleValue();
                                                        }
                                                        diffDays = DateUtil.dateDiff(thirdTransDt, curDate);
                                                        if (diffDays > 0) {
                                                            greateramount = dayendBalance + (dayendBalance * rateOfInterest * diffDays) / (36500);
                                                            calcFDMonthSecondIntAmt = greateramount - dayendBal;
                                                        }
                                                    }
                                                } else {
                                                    diffDays = DateUtil.dateDiff(secondTransDt, currDate);
                                                    if (diffDays > 0) {
                                                        greateramount = dayendBalance + (dayendBalance * rateOfInterest * diffDays) / (36500);
                                                        calcFDMonthSecondIntAmt = greateramount - dayendBal;
                                                    }
                                                }
                                            }
                                        } else {
                                            diffDays = DateUtil.dateDiff(firstTransDt, currDate);
                                            if (diffDays > 0) {
                                                greateramount = dayendBalance + (dayendBalance * rateOfInterest * diffDays) / (36500);
                                                calcFDMonthSecondIntAmt = greateramount - dayendBal;
                                            }
                                        }
                                    }
                                    lstRecord = null;
                                    depositRecMap = null;
                                }
                                interest += calcFDDaysIntAmt + calcFDMonthIntAmt + calcFDDaysSecondIntAmt + calcFDMonthSecondIntAmt + calcMnIntAmt;
                            } else { // if installment is less than three means calcualting for simple interest formula...
                                Date fromDate = DateUtil.getDateMMDDYYYY(getDepositDate());
                                Date currDate = (Date) curDate.clone();
                                mnPeriod = totInstallments;
                                if (mnPeriod > 0) {
                                    HashMap depositRecMap = new HashMap();//balance installment calculating from trans_dt to curr_dt
                                    depositRecMap.put("DEPOSIT_NO", mapData.get("DEPOSIT_NO") + "_1");
                                    depositRecMap.put("START_DT", fromDate);
                                    if (totInstallments == 2) {
                                        totInstallments = totInstallments - 1;
                                    }
                                    depositRecMap.put("SL_NO", new Double(totInstallments));
                                    List lstFirstRecord = ClientUtil.executeQuery("getDepNotProperRecurring", depositRecMap);
                                    if (lstFirstRecord != null && lstFirstRecord.size() > 0) {
                                        depositRecMap = (HashMap) lstFirstRecord.get(0);
                                        Date firstTransDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositRecMap.get("TRANS_DT")));
                                        Date dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositRecMap.get("DUE_DATE")));
                                        if (firstTransDt == null) {
                                            firstTransDt = dueDate;
                                        }
                                        if (firstTransDt != null && (DateUtil.dateDiff(firstTransDt, dueDate) > 0)) {
                                            firstTransDt = dueDate;
                                        } else {
                                            firstTransDt = firstTransDt;
                                        }
                                        mnPeriod = mnPeriod - 1;
                                        if (mnPeriod > 0) {
                                            depositRecMap.put("DEPOSIT_NO", mapData.get("DEPOSIT_NO") + "_1");
                                            depositRecMap.put("START_DT", fromDate);
                                            depositRecMap.put("SL_NO", new Double(totInstallments + 1));
                                            List lstSecondRecord = ClientUtil.executeQuery("getDepNotProperRecurring", depositRecMap);
                                            if (lstSecondRecord != null && lstSecondRecord.size() > 0) {
                                                depositRecMap = (HashMap) lstSecondRecord.get(0);
                                                Date secondTransDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositRecMap.get("TRANS_DT")));
                                                dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositRecMap.get("DUE_DATE")));
                                                if (secondTransDt == null) {
                                                    secondTransDt = dueDate;
                                                }
                                                diffDays = DateUtil.dateDiff(firstTransDt, secondTransDt);
                                                if (diffDays > 0) {
                                                    principal = new Double(getPrinicipal().length() > 1 ? getPrinicipal() : "0").doubleValue();
                                                    greateramount = principal + (principal * rateOfInterest * diffDays) / (36500);
                                                    calcFDDaysIntAmt = greateramount - principal;
                                                }
                                                diffDays = DateUtil.dateDiff(secondTransDt, currDate);
                                                if (diffDays > 0) {
                                                    principal = new Double(getPrinicipal().length() > 1 ? getPrinicipal() : "0").doubleValue();
                                                    principal = principal * 2;
                                                    greateramount = principal + (principal * rateOfInterest * diffDays) / (36500);
                                                    calcFDMonthIntAmt = greateramount - principal;
                                                }
                                            }
                                            lstSecondRecord = null;
                                        } else {
                                            diffDays = DateUtil.dateDiff(firstTransDt, currDate);
                                            if (diffDays > 0) {
                                                principal = new Double(getPrinicipal().length() > 1 ? getPrinicipal() : "0").doubleValue();
                                                greateramount = principal + (principal * rateOfInterest * diffDays) / (36500);
                                                calcFDDaysIntAmt = greateramount - principal;
                                            }
                                        }
                                    }
                                    depositRecMap = null;
                                    lstFirstRecord = null;
                                }
                                interest = calcFDDaysIntAmt + calcFDMonthIntAmt;
                            }
                            //                                    System.out.println("Pre Mature uncompletedInterest : "+uncompletedInterest +"balSimpAmt :"+balSimpAmt+"interest :"+interest);
                        } else {//Normal closure calculation...DOWN CODING R ALL COREECT DONT CHANGE...
                            transfer_out_mode = "NO";
                            Date fromDate = DateUtil.getDateMMDDYYYY(getDepositDate());
                            Date matDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getMaturityDate()));
                            int periodYear = CommonUtil.convertObjToInt(custMap.get("DEPOSIT_PERIOD_YY"));
                            int periodMonth = CommonUtil.convertObjToInt(custMap.get("DEPOSIT_PERIOD_MM"));
                            int totalInstall = CommonUtil.convertObjToInt(custMap.get("TOTAL_INSTALL_PAID"));
                            //                                    System.out.println("##### period year : "+periodYear);
                            if (periodYear >= 1) {
                                periodMonth = periodMonth + (periodYear * 12);
                            }
                            if (periodMonth == totalInstall) {
                                interest = CommonUtil.convertObjToDouble(custMap.get("TOT_INT_AMT")).doubleValue();
                            } else {
                                long totInstallments = CommonUtil.convertObjToLong(custMap.get("TOTAL_INSTALL_PAID"));
                                if (totInstallments >= 3) { // if installment is more than three means calcualting for recurring formula...
                                    if (totInstallments > periodMonth) {
                                        period = (long) periodMonth / 3;
                                        mnPeriod = periodMonth;
                                    } else {
                                        period = totInstallments / 3;
                                        mnPeriod = totInstallments;
                                    }
                                    period = (long) roundOffLower((long) (period * 100), 100) / 100;
                                    amount = principal * (Math.pow((1 + rateOfInterest / 400), period) - 1) / (1 - Math.pow((1 + rateOfInterest / 400), -1 / 3.0));
                                    amount1 = amount;
                                    if (totInstallments > totalMonths) {
                                        mnPeriod = totalMonths - (period * 3);
                                    } else {
                                        mnPeriod = totInstallments - (period * 3);
                                    }
                                    interest = amount - (principal * period * 3);
                                    Date nextDate = fromDate;
                                    Date k = DateUtil.nextCalcDate(fromDate, fromDate, 30);
                                    period = period * 3;
                                    for (int i = 0; i < period; i++) {
                                        fromDate = DateUtil.nextCalcDate(fromDate, k, 30);
                                    }
                                    //                                        period = period *3;
                                    //                                        for(int i=0; i<period; i++)
                                    //                                            fromDate = DateUtil.addDays(fromDate, 30);
                                    Date currDate = (Date) curDate.clone();
                                    mnPeriod = totInstallments - period;
                                    if (mnPeriod > 0 && mnPeriod <= 2) {
                                        HashMap depositRecMap = new HashMap();//balance installment calculating from trans_dt to curr_dt
                                        depositRecMap.put("DEPOSIT_NO", mapData.get("DEPOSIT_NO") + "_1");
                                        depositRecMap.put("START_DT", fromDate);
                                        depositRecMap.put("SL_NO", new Double(period + 1));
                                        List lstRecord = ClientUtil.executeQuery("getDepNotProperRecurring", depositRecMap);
                                        if (lstRecord != null && lstRecord.size() > 0) {
                                            depositRecMap = (HashMap) lstRecord.get(0);
                                            Date firstTransDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositRecMap.get("TRANS_DT")));
                                            Date dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositRecMap.get("DUE_DATE")));
                                            if (firstTransDt != null && (DateUtil.dateDiff(firstTransDt, dueDate) > 0)) {
                                                firstTransDt = dueDate;
                                            } else {
                                                firstTransDt = firstTransDt;
                                            }
                                            if (firstTransDt == null) {
                                                firstTransDt = dueDate;
                                            }
                                            mnPeriod = mnPeriod - 1;
                                            if (mnPeriod > 0) {
                                                depositRecMap.put("DEPOSIT_NO", mapData.get("DEPOSIT_NO") + "_1");
                                                depositRecMap.put("START_DT", fromDate);
                                                depositRecMap.put("SL_NO", new Double(period + 2));
                                                lstRecord = ClientUtil.executeQuery("getDepNotProperRecurring", depositRecMap);
                                                if (lstRecord != null && lstRecord.size() > 0) {
                                                    depositRecMap = (HashMap) lstRecord.get(0);
                                                    Date secondTransDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositRecMap.get("TRANS_DT")));
                                                    dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositRecMap.get("DUE_DATE")));
                                                    if (secondTransDt != null && (DateUtil.dateDiff(secondTransDt, dueDate) > 0)) {
                                                        secondTransDt = dueDate;
                                                    } else {
                                                        secondTransDt = secondTransDt;
                                                    }
                                                    if (secondTransDt == null) {
                                                        secondTransDt = dueDate;
                                                    }
                                                    diffDays = DateUtil.dateDiff(firstTransDt, secondTransDt);
                                                    if (diffDays > 0) {
                                                        principal = new Double(getPrinicipal().length() > 1 ? getPrinicipal() : "0").doubleValue();
                                                        principal = principal * (totInstallments - 2);
                                                        greateramount = principal + (principal * rateOfInterest * diffDays) / (36500);
                                                        calcFDDaysIntAmt = greateramount - principal;
                                                    }
                                                    diffDays = DateUtil.dateDiff(secondTransDt, matDate);
                                                    if (diffDays > 0) {
                                                        principal = new Double(getPrinicipal().length() > 1 ? getPrinicipal() : "0").doubleValue();
                                                        principal = principal * totInstallments;
                                                        greateramount = principal + (principal * rateOfInterest * diffDays) / (36500);
                                                        calcFDMonthSecondIntAmt = greateramount - principal;
                                                    }
                                                }
                                            } else {
                                                diffDays = DateUtil.dateDiff(firstTransDt, matDate);
                                                if (diffDays > 0) {
                                                    principal = new Double(getPrinicipal().length() > 1 ? getPrinicipal() : "0").doubleValue();
                                                    principal = principal * totInstallments;
                                                    greateramount = principal + (principal * rateOfInterest * diffDays) / (36500);
                                                    calcFDMonthSecondIntAmt = greateramount - principal;
                                                }
                                            }
                                        }
                                        lstRecord = null;
                                        depositRecMap = null;
                                    } else if (mnPeriod == 0) {
                                        HashMap depositRecMap = new HashMap();//balance installment calculating from trans_dt to curr_dt
                                        depositRecMap.put("DEPOSIT_NO", mapData.get("DEPOSIT_NO") + "_1");
                                        depositRecMap.put("START_DT", fromDate);
                                        depositRecMap.put("SL_NO", new Double(period));
                                        List lstRecord = ClientUtil.executeQuery("getDepNotProperRecurring", depositRecMap);
                                        if (lstRecord != null && lstRecord.size() > 0) {
                                            depositRecMap = (HashMap) lstRecord.get(0);
                                            Date firstTransDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositRecMap.get("TRANS_DT")));
                                            Date dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositRecMap.get("DUE_DATE")));
                                            dueDate = DateUtil.nextCalcDate(dueDate, dueDate, 30);
                                            //                                                dueDate = DateUtil.addDays(dueDate, 30);
                                            if (firstTransDt == null) {
                                                firstTransDt = dueDate;
                                            }
                                            if (firstTransDt != null && (DateUtil.dateDiff(firstTransDt, dueDate) > 0)) {
                                                firstTransDt = dueDate;
                                            } else {
                                                firstTransDt = firstTransDt;
                                            }
                                            diffDays = DateUtil.dateDiff(firstTransDt, matDate);
                                            if (diffDays > 0) {
                                                principal = new Double(getPrinicipal().length() > 1 ? getPrinicipal() : "0").doubleValue();
                                                principal = principal * period;
                                                greateramount = principal + (principal * rateOfInterest * diffDays) / (36500);
                                                calcFDDaysIntAmt = greateramount - principal;
                                            }
                                        }
                                        lstRecord = null;
                                        depositRecMap = null;
                                    }
                                    interest += calcFDDaysIntAmt + calcFDMonthIntAmt + calcFDDaysSecondIntAmt + calcFDMonthSecondIntAmt;
                                } else {
                                    mnPeriod = totInstallments;
                                    if (mnPeriod > 0) {
                                        HashMap depositRecMap = new HashMap();//balance installment calculating from trans_dt to curr_dt
                                        depositRecMap.put("DEPOSIT_NO", mapData.get("DEPOSIT_NO") + "_1");
                                        depositRecMap.put("START_DT", fromDate);
                                        if (totInstallments == 2) {
                                            totInstallments = totInstallments - 1;
                                        }
                                        depositRecMap.put("SL_NO", new Double(totInstallments));
                                        List lstFirstRecord = ClientUtil.executeQuery("getDepNotProperRecurring", depositRecMap);
                                        if (lstFirstRecord != null && lstFirstRecord.size() > 0) {
                                            depositRecMap = (HashMap) lstFirstRecord.get(0);
                                            Date firstTransDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositRecMap.get("TRANS_DT")));
                                            Date dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositRecMap.get("DUE_DATE")));
                                            if (firstTransDt == null) {
                                                firstTransDt = dueDate;
                                            }
                                            if (firstTransDt != null && (DateUtil.dateDiff(firstTransDt, dueDate) > 0)) {
                                                firstTransDt = dueDate;
                                            } else {
                                                firstTransDt = firstTransDt;
                                            }
                                            mnPeriod = mnPeriod - 1;
                                            if (mnPeriod > 0) {
                                                depositRecMap.put("DEPOSIT_NO", mapData.get("DEPOSIT_NO") + "_1");
                                                depositRecMap.put("START_DT", fromDate);
                                                depositRecMap.put("SL_NO", new Double(totInstallments + 1));
                                                List lstSecondRecord = ClientUtil.executeQuery("getDepNotProperRecurring", depositRecMap);
                                                if (lstSecondRecord != null && lstSecondRecord.size() > 0) {
                                                    depositRecMap = (HashMap) lstSecondRecord.get(0);
                                                    Date secondTransDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositRecMap.get("TRANS_DT")));
                                                    dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depositRecMap.get("DUE_DATE")));
                                                    if (secondTransDt == null) {
                                                        secondTransDt = dueDate;
                                                    }
                                                    diffDays = DateUtil.dateDiff(firstTransDt, secondTransDt);
                                                    if (diffDays > 0) {
                                                        principal = new Double(getPrinicipal().length() > 1 ? getPrinicipal() : "0").doubleValue();
                                                        greateramount = principal + (principal * rateOfInterest * diffDays) / (36500);
                                                        calcFDDaysIntAmt = greateramount - principal;
                                                    }
                                                    diffDays = DateUtil.dateDiff(secondTransDt, matDate);
                                                    if (diffDays > 0) {
                                                        principal = new Double(getPrinicipal().length() > 1 ? getPrinicipal() : "0").doubleValue();
                                                        principal = principal * 2;
                                                        greateramount = principal + (principal * rateOfInterest * diffDays) / (36500);
                                                        calcFDMonthIntAmt = greateramount - principal;
                                                    }
                                                }
                                                lstSecondRecord = null;
                                            } else {
                                                diffDays = DateUtil.dateDiff(firstTransDt, matDate);
                                                if (diffDays > 0) {
                                                    principal = new Double(getPrinicipal().length() > 1 ? getPrinicipal() : "0").doubleValue();
                                                    greateramount = principal + (principal * rateOfInterest * diffDays) / (36500);
                                                    calcFDDaysIntAmt = greateramount - principal;
                                                }
                                            }
                                        }
                                        depositRecMap = null;
                                        lstFirstRecord = null;
                                    }
                                    interest = calcFDDaysIntAmt + calcFDMonthIntAmt;
                                }
                            }
                            //                                matDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(custMap.get("MATURITY_DT")));
                            //                                amount = CommonUtil.convertObjToDouble(custMap.get("MATURITY_AMT")).doubleValue();
                            //                                rateOfInterest = CommonUtil.convertObjToDouble(getPrematureClosingRate()).doubleValue();
                            //                                Date matDt = (Date)curDate.clone();
                            //                                if(matDate.getDate()>0){
                            //                                    matDt.setYear(matDate.getYear());
                            //                                    matDt.setMonth(matDate.getMonth());
                            //                                    matDt.setDate(matDate.getDate());
                            //                                }
                            //                                interest = holidayProvision(matDt,amount,interest,rateOfInterest,holidayProv);
                            //                                if(custMap.get("DEATH_CLAIM")!=null && custMap.get("DEATH_CLAIM").equals("Y") && deathFlag == false){
                            //                                    HashMap calculationMap = new HashMap();
                            //                                    calculationMap.put("DEP_AMOUNT",custMap.get("DEPOSIT_AMT"));
                            //                                    calculationMap.put("CLEAR_BALANCE",custMap.get("CLEAR_BALANCE"));
                            //                                    calculationMap.put("MAT_AMOUNT",custMap.get("MATURITY_AMT"));
                            //                                    calculationMap.put("CATEGORY_ID",custMap.get("CATEGORY"));
                            //                                    calculationMap.put("BEHAVES_LIKE",behavesMap.get("BEHAVES_LIKE"));
                            //                                    double deathAmt = simpleInterestCalculation(calculationMap);
                            //                                    interest = interest + deathAmt;
                            //                                    setDeathClaim("Y");
                            //                                }
                        }//upcoming codings r delayed installment calculation...
                        double delayAmt = 0.0;
                        double depAmt = CommonUtil.convertObjToDouble(custMap.get("DEPOSIT_AMT")).doubleValue();
                        double chargeAmt = depAmt / 100;
                        HashMap delayMap = new HashMap();
                        delayMap.put("PROD_ID", custMap.get("PROD_ID"));
                        delayMap.put("DEPOSIT_AMT", custMap.get("DEPOSIT_AMT"));
                        List lst = ClientUtil.executeQuery("getSelectDelayedRate", delayMap);
                        if (lst != null && lst.size() > 0) {
                            delayMap = (HashMap) lst.get(0);
                            delayAmt = CommonUtil.convertObjToDouble(delayMap.get("PENAL_INT")).doubleValue();
                            delayAmt = delayAmt * chargeAmt;
                        }
                        delayMap = null;
                        lst = null;
                        long totalDelay = 0;
                        HashMap depRecMap = new HashMap();
                        depRecMap.put("DEPOSIT_NO", mapData.get("DEPOSIT_NO") + "_1");
                        List lstRec = ClientUtil.executeQuery("getDepTransactionRecurring", depRecMap);
                        if (lstRec != null && lstRec.size() > 0) {//how many unCompleted quarter is there upto (one by one) we have to calculate simple interest formula....
                            for (int i = 0; i < lstRec.size(); i++) {//when the customer is paid from that dt to today dt....
                                depRecMap = (HashMap) lstRec.get(i);
                                Date transDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depRecMap.get("TRANS_DT")));
                                Date dueDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(depRecMap.get("DUE_DATE")));
                                int transMonth = transDt.getMonth() + 1;
                                int dueMonth = dueDate.getMonth() + 1;
                                int dueYear = dueDate.getYear() + 1900;
                                int transYear = transDt.getYear() + 1900;
                                int delayeInstallment = transMonth - dueMonth;
                                if (dueYear == transYear) {
                                    delayeInstallment = transMonth - dueMonth;
                                } else {
                                    int diffYear = transYear - dueYear;
                                    delayeInstallment = (diffYear * 12 - dueMonth) + transMonth;
                                }
                                if (delayeInstallment < 0) {
                                    delayeInstallment = 0;
                                }
                                totalDelay = totalDelay + delayeInstallment;
                            }
                            delayAmt = delayAmt * totalDelay;
                            double tblDelayMonth = CommonUtil.convertObjToDouble(custMap.get("DELAYED_MONTH")).doubleValue();
                            double tblDelayAmt = CommonUtil.convertObjToDouble(custMap.get("DELAYED_AMOUNT")).doubleValue();
                            totalDelay = totalDelay - (long) tblDelayMonth;
                            if (totalDelay > 0) {
                                delayAmt = delayAmt - tblDelayAmt;
                                setDelayedInstallments(String.valueOf(totalDelay) + " Month");
                                delayAmt = (double) getNearest((long) (delayAmt * 100), 100) / 100;
                                if(custMap.containsKey("RD_CLOSING_PENAL_REQUIRED") && custMap.get("RD_CLOSING_PENAL_REQUIRED") != null && CommonUtil.convertObjToStr(custMap.get("RD_CLOSING_PENAL_REQUIRED")).equalsIgnoreCase("Y")){  // Added by nithya on 18-09-2019 for KD 570 RD Closure Needs Delayed Amount Calculation.
                                   setChargeAmount(String.valueOf(delayAmt));
                                }else{
                                   setChargeAmount(""); 
                                }   
                            }
                        }
                        depRecMap = null;
                        lstRec = null;
                        amount = CommonUtil.convertObjToDouble(custMap.get("MATURITY_AMT")).doubleValue();
                        rateOfInterest = CommonUtil.convertObjToDouble(getPrematureClosingRate()).doubleValue();
                        Date matDt = (Date) curDate.clone();
                        Date matDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getMaturityDate()));
                        if (matDate.getDate() > 0) {
                            matDt.setYear(matDate.getYear());
                            matDt.setMonth(matDate.getMonth());
                            matDt.setDate(matDate.getDate());
                        }
                        holidayAmt = holidayProvision(matDt, amount, interest, rateOfInterest, holidayProv);
                        if (custMap.get("DEATH_CLAIM") != null && custMap.get("DEATH_CLAIM").equals("Y") && deathFlag == false) {
                            HashMap calculationMap = new HashMap();
                            calculationMap.put("DEP_AMOUNT", custMap.get("DEPOSIT_AMT"));
                            calculationMap.put("CLEAR_BALANCE", custMap.get("CLEAR_BALANCE"));
                            calculationMap.put("MAT_AMOUNT", custMap.get("MATURITY_AMT"));
                            calculationMap.put("CATEGORY_ID", custMap.get("CATEGORY"));
                            calculationMap.put("BEHAVES_LIKE", behavesMap.get("BEHAVES_LIKE"));
                            double deathAmt = 0.0;
                            if(custMap.containsKey("DEATH_CLAIM_INT") && custMap.get("DEATH_CLAIM_INT")!= null && CommonUtil.convertObjToStr(custMap.get("DEATH_CLAIM_INT")).equalsIgnoreCase("Y")){// Added by nithya on 13-08-2019 for KD 583
                               deathAmt = simpleInterestCalculation(calculationMap);
                            }
                            interest = interest + deathAmt;
                            calculationMap = null;
                            setDeathClaim("Y");
                        }
                        //                        }
                        List rdApplyList = ClientUtil.executeQuery("getIrregularRDApply", custMap);
                        if (rdApplyList != null && rdApplyList.size() > 0) {
                            HashMap rdMap = (HashMap) rdApplyList.get(0);
                            rdApply = CommonUtil.convertObjToStr(rdMap.get("IRREGULAR_RD_APPLY"));
                            rdClosingOtherChargeApply = CommonUtil.convertObjToStr(rdMap.get("RD_CLOSING_SB_DEPOSIT_ROI"));// Added by nithya on 18-09-2019 for KD 606 - RD Closure - Premature And Defaulter Payment Needs Different Settings
                            fullAmtToDefaulterAfterMaturity = CommonUtil.convertObjToStr(rdMap.get("FULLAMT_ON_MATURE_CLOSURE"));
                        }
                        int paidMonths = 0;
                        int actualMonths = 0;
                        int no_week = 0;
                        double instAmount = 0.0;
                        double totalMaturityAmount = 0.0;
                        double totalPaidInstAmt = 0.0;
                        HashMap rdPaidInst = new HashMap();
                        rdPaidInst.put("DEPOSIT_NO", mapData.get("DEPOSIT_NO"));

                        List rdPaidInstList = ClientUtil.executeQuery("getRdPaidInstllments", rdPaidInst);
                        if (rdPaidInstList != null && rdPaidInstList.size() > 0) {
                            rdPaidInst = (HashMap) rdPaidInstList.get(0);
                            actualMonths = CommonUtil.convertObjToInt(rdPaidInst.get("TOTAL_INSTALLMENTS"));
                            paidMonths = CommonUtil.convertObjToInt(rdPaidInst.get("TOTAL_INSTALL_PAID"));
                            instAmount = CommonUtil.convertObjToDouble(rdPaidInst.get("DEPOSIT_AMT")).doubleValue();
                            no_week = CommonUtil.convertObjToInt(rdPaidInst.get("DEPOSIT_PERIOD_WK"));
                            totalMaturityAmount = no_week * instAmount;
                            totalPaidInstAmt = paidMonths * instAmount;
                        }
					//Added By Chithra		
                       if(no_week >0){
                           if(actualMonths == paidMonths && DateUtil.dateDiff(curDate, matDate) > 0){//nithya*******************                              
                               String weeklyRDMaturityDt  = DateUtil.getStringDate(matDate);
                               ClientUtil.showAlertWindow("Total installments paid . But maturity date is - " + weeklyRDMaturityDt);
                           }
                            setDepositPeriodWK(CommonUtil.convertObjToStr(no_week));
                            //setPrematureClosingRate("0");
                             setRateApplicable("0");
                             HashMap rdwhrMap = new HashMap();
                             rdwhrMap.put("PROD_ID", getProdID());
                             double instNo =0,instPenal =0;
                             double irregularRDInt = 0.0;
                             List slabList = ClientUtil.executeQuery("getRdWeeklyInstallmentSlabs", rdwhrMap);
                             for(int k=0;k<slabList.size();k++){
                                 HashMap each = (HashMap)slabList.get(k);
                                 if(each!=null && each.size()>0){
                                     double fromInst = CommonUtil.convertObjToDouble(each.get("FROM_INSTALL"));
                                     double toInst = CommonUtil.convertObjToDouble(each.get("TO_INSTALL"));
                                     if(paidMonths > fromInst && paidMonths <= toInst){
                                         instNo = CommonUtil.convertObjToDouble(each.get("INSTALLMENT_NO")); 
                                         instPenal = CommonUtil.convertObjToDouble(each.get("PENAL")); 
                                     }
                                 }
                             }
                           //Check for full amount payment during mature closure and full amount not remitted
                             // Done for chazhur SCB - KD-3684 WEEKLY - CLOSING REG
                             /*
                             WEEKLY NEW-PRODUCT ID 115WEEKLY CLOSING NEW REQUIREMENT IS GIVEN BELOW.
                             1) MATURED CLOSING 52 INSTALLMENTS PLUS 2 INSTALLMENT BONUS.
                             2) CLOSING AFTER ONE YEAR- PAYING FULL REMITTED  AMOUNT
                             3) FOR PREMATURE CLOSING RECOVER 1 INSTALLMENT FROM THE REMITTED AMOUNT
                             */
                             if(CommonUtil.convertObjToStr(objMap.get("MODE")).equals(CommonConstants.NORMAL_CLOSURE) && fullAmtToDefaulterAfterMaturity.equals("Y")  && totalPaidInstAmt < totalMaturityAmount){
                                 instNo = 0;
                                 instPenal = 0;
                                 if (rdClosingOtherChargeApply.equalsIgnoreCase("Y")) {
                                     System.out.println("Executing inside rdClosingOtherChargeApply ");
                                     instNo = 0;
                                     instPenal = 0;
                                     System.out.println("irregular rd other prod rate :: " + getPrematureClosingRate());
                                     HashMap intMap = new HashMap();
                                     intMap.put("DEPOSIT_NO", getTxtDepositNo());
                                     intMap.put("ROI", getPrematureClosingRate());
                                     intMap.put("BRANCHCODE", ProxyParameters.BRANCH_ID);
                                     List intLst = ClientUtil.executeQuery("getRDClosingOtherRateInt", intMap);
                                     if (intLst != null && intLst.size() > 0) {
                                         intMap = (HashMap) intLst.get(0);
                                         if (intMap.containsKey("INT_AMT") && intMap.get("INT_AMT") != null) {
                                             irregularRDInt = CommonUtil.convertObjToDouble(intMap.get("INT_AMT"));
                                         }
                                     }
                                 }
                             }
                           // End
                            if (instNo > 0) {
                               interest = instAmount * instNo;
                           } else if (instPenal > 0) {
                               interest = 0;
                               double recInt = instAmount * instPenal;
                               recIntVal = recInt;
                               this.setIntCr(CommonUtil.convertObjToStr(recInt));
                               setClosingIntDb(CommonUtil.convertObjToStr(interest - recInt));
                               rec_recivable = CommonUtil.convertObjToStr(recInt);
                           } else {
                               if(irregularRDInt > 0){
                                  interest = irregularRDInt; 
                               }else{
                               interest = 0;
                               }
                           }
                        }
                        else {
                           // Commented by nithya on 18-09-2019 for KD 606 - RD Closure - Premature And Defaulter Payment Needs Different Settings
                           /****************************** rewriting the below code lines **********************************/
//                            if (rdApply.equals("SB Rate") && (((String) objMap.get("MODE")).equals(CommonConstants.PREMATURE_CLOSURE)
//                                    || ((CommonUtil.convertObjToStr(objMap.get("MODE"))).equals(CommonConstants.NORMAL_CLOSURE)
//                                    && actualMonths != paidMonths))) {
//                                Date acopdate = DateUtil.getDateMMDDYYYY(getDepositDate());
//                                Date matdt = DateUtil.getDateMMDDYYYY(getMaturityDate());
//                               if (DateUtil.dateDiff(curDate, matdt) > 0) {
//                                    matdt = curDate;
//                                }
////                            HashMap rdPaidInst = new HashMap();
////                            rdPaidInst.put("DEPOSIT_NO", mapData.get("DEPOSIT_NO"));
////
////                            List rdPaidInstList = ClientUtil.executeQuery("getRdPaidInstllments", rdPaidInst);
////                            if (rdPaidInstList != null && rdPaidInstList.size() > 0) {
////                                rdPaidInst = (HashMap) rdPaidInstList.get(0);
////                                actualMonths = CommonUtil.convertObjToInt(rdPaidInst.get("TOTAL_INSTALLMENTS"));
////                                paidMonths = CommonUtil.convertObjToInt(rdPaidInst.get("TOTAL_INSTALL_PAID"));
////                                instAmount = CommonUtil.convertObjToDouble(rdPaidInst.get("DEPOSIT_AMT")).doubleValue();
////                            }
//                                Date maturityDate = (Date) curDate.clone();
//                                maturityDate.setDate(matdt.getDate());
//                                maturityDate.setMonth(matdt.getMonth());
//                                maturityDate.setYear(matdt.getYear());
//
//                                Date acopeningDt = (Date) curDate.clone();
//                                acopeningDt.setDate(acopdate.getDate());
//                                acopeningDt.setMonth(acopdate.getMonth());
//                                acopeningDt.setYear(acopdate.getYear());
//
//                                int acmonth = acopeningDt.getMonth() + 1;
//                                int acyear = acopeningDt.getYear() + 1900;
//
//                                int curmon = curDate.getMonth() + 1;
//                                int curYear = curDate.getYear() + 1900;
//
//                                int year = curYear - acyear;
//                                int mon = curmon - acmonth;
//
//                                mon = (year / 360) / 30 + mon + 1;
//                                paidMonths = mon - paidMonths;
//                                mon = mon * (mon + 1) / 2;
//                                paidMonths = paidMonths * (paidMonths + 1) / 2;
//
//
//                                double sbrate = 0.0;
//                                long periodSB = DateUtil.dateDiff(acopeningDt, maturityDate);
//
//                                rdPaidInst.put("DEPOSIT_DT", DateUtil.getDateMMDDYYYY(getDepositDate()));
//                                rdPaidInst.put("PRODID", getProdID());
//                                rdPaidInst.put("CUSTID", getCustomerID());
//                                rdPaidInst.put("DEPOSITNO", getTxtDepositNo());
//                                rdPaidInst.put("PERIOD", new Long(periodSB));
//                                List SbRateList = ClientUtil.executeQuery("getSBrateDetails", rdPaidInst);
//                                if (SbRateList != null && SbRateList.size() > 0) {
//                                    rdPaidInst = (HashMap) SbRateList.get(0);
//                                    sbrate = CommonUtil.convertObjToDouble(rdPaidInst.get("ROI")).doubleValue();
//                                    setRateApplicable(String.valueOf(sbrate));
//                                    setPrematureClosingRate(String.valueOf(sbrate));
//
//                                }
//                                int unPaid = mon - paidMonths;
//                                interest = instAmount * (sbrate / 100) * unPaid / 12;
//
//                            }
//
//                            if (rdApply.equals("SB Rate") && (CommonUtil.convertObjToStr(objMap.get("MODE"))).equals(CommonConstants.NORMAL_CLOSURE)
//                                    && actualMonths != paidMonths) {
//                                HashMap whrMap = new HashMap();
//                                whrMap.put("DEPOSIT_DT", CommonUtil.getProperDate(curDate, DateUtil.getDateMMDDYYYY(getDepositDate())));
//                                whrMap.put("CURR_DT", curDate);
//                                whrMap.put("DEPOSIT_NO", getTxtDepositNo());
//                                whrMap.put("DEPOSITSUB_NO", getTxtDepositNo() + "_1");
//                                List SbRateIntList = ClientUtil.executeQuery("getRDNormalClosingInterest", whrMap);
//                                if (SbRateIntList != null && SbRateIntList.size() > 0) {
//                                    HashMap sing = (HashMap) SbRateIntList.get(0);
//                                    if (sing != null && sing.containsKey("INT_AMT")) {
//                                        interest = CommonUtil.convertObjToDouble(sing.get("INT_AMT"));
//                                    }
//                                }
//                            }
                            
                            /****************************** End - rewriting the below code lines **********************************/
                           // Added by nithya on 18-09-2019 for KD 606 - RD Closure - Premature And Defaulter Payment Needs Different Settings
                           if (rdClosingOtherChargeApply.equalsIgnoreCase("Y")) {
                               System.out.println("inside SB Rate :: " + interest);
                               if (((CommonUtil.convertObjToStr(objMap.get("MODE"))).equals(CommonConstants.NORMAL_CLOSURE) && actualMonths != paidMonths) || ((String) objMap.get("MODE")).equals(CommonConstants.PREMATURE_CLOSURE)) {
                                   System.out.println("inside if " + interest);
                                   HashMap intMap = new HashMap();
                                   intMap.put("DEPOSIT_NO", getTxtDepositNo());
                                   intMap.put("ROI", getPrematureClosingRate());
                                   intMap.put("BRANCHCODE",ProxyParameters.BRANCH_ID);
                                   List intLst = ClientUtil.executeQuery("getRDClosingOtherRateInt", intMap);
                                   if(intLst != null && intLst.size() > 0){
                                       intMap = (HashMap) intLst.get(0);
                                       if(intMap.containsKey("INT_AMT") && intMap.get("INT_AMT") != null){
                                           interest = CommonUtil.convertObjToDouble(intMap.get("INT_AMT"));
                                       }
                                   }
                               }
                               System.out.println("interest :: " + interest);
                           }

                            interest = interest + holidayAmt;
                        }
                        //  interest = (double)getNearest((long)(interest *100),100)/100;
                        interest = getRound(interest, getInterestRound());
                        if (holidayAmt > 0) {
                            ClientUtil.showMessageWindow("Holiday interest amount is :" + holidayAmt);
                        }
                        double availableBal = CommonUtil.convertObjToDouble(custMap.get("TOTAL_BALANCE")).doubleValue();
                        if (availableBal == 0) {
                            setClosingDisbursal(CommonUtil.convertObjToStr("0"));
                            this.setPayReceivable(CommonUtil.convertObjToStr("0"));
                            this.setPermanentPayReceivable(CommonUtil.convertObjToStr("0"));
                            setPrematureClosingRate(CommonUtil.convertObjToStr("0"));
                            ClientUtil.showAlertWindow("No Balance in the account. Do you want to really close...");
                        } else {
                            this.setClosingIntDb(String.valueOf(interest));
                            double disburse = 0.0;
                            setPrinicipal(CommonUtil.convertObjToStr(custMap.get("TOTAL_BALANCE")));
                            this.setClosingDisbursal(
                                    CommonUtil.convertObjToStr(new Double(
                                    CommonUtil.convertObjToDouble(getPrinicipal()).doubleValue()
                                    + CommonUtil.convertObjToDouble(getClosingIntDb()).doubleValue()-CommonUtil.convertObjToDouble(getIntCr()))));
                           	//Added By Chithra
                            if (recIntVal > 0) {
                                this.setClosingDisbursal(
                                        CommonUtil.convertObjToStr(new Double(
                                        CommonUtil.convertObjToDouble(getPrinicipal()).doubleValue()
                                        + CommonUtil.convertObjToDouble(getClosingIntDb()).doubleValue()) - recIntVal));
                            }
                            setDisburseAmt(CommonUtil.convertObjToDouble(this.getClosingDisbursal()).doubleValue());

                            principal = CommonUtil.convertObjToDouble(getPrinicipal()).doubleValue();
                            disburse = CommonUtil.convertObjToDouble(getClosingDisbursal()).doubleValue();
                            if (disburse > principal) {
                                setLblPayRecDet("Payable");
                                this.setPayReceivable(
                                        CommonUtil.convertObjToStr(new Double(
                                        CommonUtil.convertObjToDouble(getClosingIntDb()).doubleValue()
                                        - CommonUtil.convertObjToDouble(getIntDrawn()).doubleValue())));
                                this.setPermanentPayReceivable(CommonUtil.convertObjToStr(new Double(
                                        CommonUtil.convertObjToDouble(getClosingIntDb()).doubleValue()
                                        - CommonUtil.convertObjToDouble(getIntDrawn()).doubleValue())));
                                //                                System.out.println("#########payRecValue :" +this.getPayReceivable());
                            } else {
                                setLblPayRecDet("Receivable");
                                this.setPayReceivable(
                                        CommonUtil.convertObjToStr(new Double(
                                        CommonUtil.convertObjToDouble(getIntCr()).doubleValue()
                                        - CommonUtil.convertObjToDouble(getClosingIntDb()).doubleValue())));
                                this.setPermanentPayReceivable(CommonUtil.convertObjToStr(new Double(
                                        CommonUtil.convertObjToDouble(getIntCr()).doubleValue()
                                        - CommonUtil.convertObjToDouble(getClosingIntDb()).doubleValue())));
                                //                                System.out.println("#########payRecValue else :" +this.getPayReceivable());
                            }
                            double negValue = CommonUtil.convertObjToDouble(getPayReceivable()).doubleValue();
                            if (negValue < 0) {
                                negValue = negValue * -1;
                                this.setPayReceivable(String.valueOf(negValue));
                                this.setPermanentPayReceivable(String.valueOf(negValue));
                            }
                        }
                    }
                }
                else if (behavesMap.get("BEHAVES_LIKE").equals("THRIFT")||behavesMap.get("BEHAVES_LIKE").equals("BENEVOLENT")) {
                    setLblBalanceDeposit("Installment Amount");
                    this.setMaturityValue(String.valueOf(CommonUtil.convertObjToDouble(custMap.get("MATURITY_AMT")).doubleValue()));
                    principal = new Double(getPrinicipal().length() > 1 ? getPrinicipal() : "0").doubleValue();
                    double rateOfInterest = CommonUtil.convertObjToDouble(getPrematureClosingRate()).doubleValue();
                    this.setBalanceAmt(recurringAmount);
                    long period = 0;
                    int count = 0;
                    long diff = 0;
                    double rdApplyRate = 0.0;
                    String rdApply = "";
                    Date mat = null;
                    Date frm = DateUtil.getDateMMDDYYYY(getDepositDate());
                    if (((String) objMap.get("MODE")).equals(CommonConstants.PREMATURE_CLOSURE)) {
                        mat = (Date) curDate.clone();
                    } else {
                        mat = DateUtil.getDateMMDDYYYY(getMaturityDate());
                    }

                    periodNoOfDays = DateUtil.dateDiff(frm, mat);
                    period = periodNoOfDays - diff;
                    if (isRdoTransfer_Yes() == true) {
                        transfer_out_mode = "Y";
                        principal = new Double(getBalance().length() > 1 ? getBalance() : "0").doubleValue();
                        //                            System.out.println("######interest : "+this.getClosingIntDb());
                        //                            System.out.println("######interest : "+interest);
                        this.setIntDrawn(getIntDrawn());
//                        interest = (double)getNearest((long)(interest *100),100)/100;
                        interest = getRound(interest, getInterestRound());
                        // balIntAmt = (double)getNearest((long)(balIntAmt *100),100)/100;
                        balIntAmt = getRound(balIntAmt, getInterestRound());
                        System.out.println("jjjjjj44444444444444");
                        setClosingIntDb(String.valueOf(interest));
                        setClosingIntCr(String.valueOf(balIntAmt));
                        setClosingDisbursal(getBalance()); //Maturity Amt
                        setDisburseAmt(CommonUtil.convertObjToDouble(this.getClosingDisbursal()).doubleValue());
                        //                            System.out.println("######DisburseAmt : "+getDisburseAmt());
                        //                            System.out.println("###### principal: "+principal);
                        if (getDisburseAmt() > principal) {
                            setLblPayRecDet("Payable");
                            this.setPayReceivable(String.valueOf(0.0));
                            this.setPermanentPayReceivable(String.valueOf(0.0));
                            //                                System.out.println("#########payRecValue :" +this.getPayReceivable());
                        } else {
                            setLblPayRecDet("Receivable");
                            this.setPayReceivable(String.valueOf(0.0));
                            this.setPermanentPayReceivable(String.valueOf(0.0));
                            //                                System.out.println("#########payRecValue else :" +this.getPayReceivable());
                        }
                        if (totAmt == intDrawn) {
                            this.setClosingDisbursal(getBalance());
                            this.setPayReceivable(String.valueOf(0.0));
                            this.setPermanentPayReceivable(String.valueOf(0.0));
                        }
                        //                            System.out.println("intPayFreq :"+custMap.get("INTPAY_FREQ"));
                    } else {
                        //                            System.out.println("######recurring this.setBalanceAmt : "+this.getMaturityValue());
                        //                            System.out.println("###### periodNoOfDays : "+periodNoOfDays);
                        //                        if(periodNoOfDays >=46){
                        double yrPeriod = 0.0;
                        double mnPeriod = 0.0;
                        double diffDays = 0.0;
                        double calcIntAmt = 0.0;
                        double balSimpAmt = 0.0;
                        double amount1 = 0.0;
                        double fdMonths = 0.0;
                        double greateramount = 0.0;
                        double compoundInt = 0.0;
                        double interestless = 0.0;
                        double calcFDMonthSecondIntAmt = 0.0;
                        double calcFDDaysSecondIntAmt = 0.0;
                        double calcMnIntAmt = 0.0;
                        double calcFDMonthIntAmt = 0.0;
                        double calcFDDaysIntAmt = 0.0;
                        if (((String) objMap.get("MODE")).equals(CommonConstants.PREMATURE_CLOSURE)) {//Premature closure calculation.
                            if (behavesMap.get("BEHAVES_LIKE").equals("THRIFT")) {
                                HashMap whereMap = new HashMap();
                                whereMap.put("Deposit_No", CommonUtil.convertObjToStr(getTxtDepositNo()));
                                whereMap.put("curr_Dt", curDate.clone());
                                List list1 = ClientUtil.executeQuery("getThriftBenovelentInterest", whereMap);
                                if (list1 != null && list1.size() > 0) {
                                    HashMap resultMap = new HashMap();
                                    resultMap = (HashMap) list1.get(0);
                                    interest = CommonUtil.convertObjToDouble(resultMap.get("INTEREST"));

                                }
                            }
                            transfer_out_mode = "N";
                        } else {//Normal closure calculation...DOWN CODING R ALL COREECT DONT CHANGE...
                            transfer_out_mode = "NO";
                            if (behavesMap.get("BEHAVES_LIKE").equals("THRIFT")) {
                                HashMap whereMap = new HashMap();
                                whereMap.put("Deposit_No", CommonUtil.convertObjToStr(getTxtDepositNo()));
                                whereMap.put("curr_Dt", curDate.clone());
                                List list1 = ClientUtil.executeQuery("getThriftBenovelentInterest", whereMap);
                                if (list1 != null && list1.size() > 0) {
                                    HashMap resultMap = new HashMap();
                                    resultMap = (HashMap) list1.get(0);
                                    interest = CommonUtil.convertObjToDouble(resultMap.get("INTEREST"));
                                }
                            }
                        }//upcoming codings r delayed installment calculation...
                        amount = CommonUtil.convertObjToDouble(custMap.get("MATURITY_AMT")).doubleValue();
                        rateOfInterest = CommonUtil.convertObjToDouble(getPrematureClosingRate()).doubleValue();
                        Date matDt = (Date) curDate.clone();
                        Date matDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(getMaturityDate()));
                        if (matDate.getDate() > 0) {
                            matDt.setYear(matDate.getYear());
                            matDt.setMonth(matDate.getMonth());
                            matDt.setDate(matDate.getDate());
                        }
                        holidayAmt = holidayProvision(matDt, amount, interest, rateOfInterest, holidayProv);
                        if (custMap.get("DEATH_CLAIM") != null && custMap.get("DEATH_CLAIM").equals("Y") && deathFlag == false) {
                            HashMap calculationMap = new HashMap();
                            calculationMap.put("DEP_AMOUNT", custMap.get("DEPOSIT_AMT"));
                            calculationMap.put("CLEAR_BALANCE", custMap.get("CLEAR_BALANCE"));
                            calculationMap.put("MAT_AMOUNT", custMap.get("MATURITY_AMT"));
                            calculationMap.put("CATEGORY_ID", custMap.get("CATEGORY"));
                            calculationMap.put("BEHAVES_LIKE", behavesMap.get("BEHAVES_LIKE"));
                            double deathAmt = 0.0;
                            if(custMap.containsKey("DEATH_CLAIM_INT") && custMap.get("DEATH_CLAIM_INT")!= null && CommonUtil.convertObjToStr(custMap.get("DEATH_CLAIM_INT")).equalsIgnoreCase("Y")){// Added by nithya on 13-08-2019 for KD 583
                               deathAmt = simpleInterestCalculation(calculationMap);
                            }
                            interest = interest + deathAmt;
                            calculationMap = null;
                            setDeathClaim("Y");
                        }
                        interest = getRound(interest, getInterestRound());
                        if (holidayAmt > 0) {
                            ClientUtil.showMessageWindow("Holiday interest amount is :" + holidayAmt);
                        }
                        double availableBal = CommonUtil.convertObjToDouble(custMap.get("TOTAL_BALANCE")).doubleValue();
                        if (availableBal == 0) {
                            setClosingDisbursal(CommonUtil.convertObjToStr("0"));
                            this.setPayReceivable(CommonUtil.convertObjToStr("0"));
                            this.setPermanentPayReceivable(CommonUtil.convertObjToStr("0"));
                            setPrematureClosingRate(CommonUtil.convertObjToStr("0"));
                            ClientUtil.showAlertWindow("No Balance in the account. Do you want to really close...");
                        } else {
                            this.setClosingIntDb(String.valueOf(interest));
                            double disburse = 0.0;
                            setPrinicipal(CommonUtil.convertObjToStr(custMap.get("TOTAL_BALANCE")));
                            this.setClosingDisbursal(
                                    CommonUtil.convertObjToStr(new Double(
                                    CommonUtil.convertObjToDouble(getPrinicipal()).doubleValue()
                                    + CommonUtil.convertObjToDouble(getClosingIntDb()).doubleValue())));
                            //Added By Chithra		
                            if (recIntVal > 0) {
                                this.setClosingDisbursal(
                                        CommonUtil.convertObjToStr(new Double(
                                        CommonUtil.convertObjToDouble(getPrinicipal()).doubleValue()
                                        + CommonUtil.convertObjToDouble(getClosingIntDb()).doubleValue()) - recIntVal));
                            }
                            setDisburseAmt(CommonUtil.convertObjToDouble(this.getClosingDisbursal()).doubleValue());

                            principal = CommonUtil.convertObjToDouble(getPrinicipal()).doubleValue();
                            disburse = CommonUtil.convertObjToDouble(getClosingDisbursal()).doubleValue();
                            if (disburse > principal) {
                                setLblPayRecDet("Payable");
                                if (behavesMap.get("BEHAVES_LIKE").equals("THRIFT")) {
                                    this.setPayReceivable(
                                            CommonUtil.convertObjToStr(CommonUtil.convertObjToDouble(getClosingIntDb()).doubleValue()));
                                    this.setPermanentPayReceivable(CommonUtil.convertObjToStr(CommonUtil.convertObjToDouble(getClosingIntDb()).doubleValue()));
                                } else {
                                    this.setPayReceivable(
                                            CommonUtil.convertObjToStr(new Double(
                                            CommonUtil.convertObjToDouble(getClosingIntDb()).doubleValue()
                                            - CommonUtil.convertObjToDouble(getIntDrawn()).doubleValue())));
                                    this.setPermanentPayReceivable(CommonUtil.convertObjToStr(new Double(
                                            CommonUtil.convertObjToDouble(getClosingIntDb()).doubleValue()
                                            - CommonUtil.convertObjToDouble(getIntDrawn()).doubleValue())));
                                }

                                //                                System.out.println("#########payRecValue :" +this.getPayReceivable());
                            } else {
                                setLblPayRecDet("Receivable");
                                if (behavesMap.get("BEHAVES_LIKE").equals("THRIFT")) {
                                    this.setPayReceivable(
                                            CommonUtil.convertObjToStr(CommonUtil.convertObjToDouble(getClosingIntDb()).doubleValue()));
                                    this.setPermanentPayReceivable(CommonUtil.convertObjToStr(CommonUtil.convertObjToDouble(getClosingIntDb()).doubleValue()));
                                } else {
                                    this.setPayReceivable(
                                            CommonUtil.convertObjToStr(new Double(
                                            CommonUtil.convertObjToDouble(getIntCr()).doubleValue()
                                            - CommonUtil.convertObjToDouble(getClosingIntDb()).doubleValue())));
//                                this.setPayReceivable(
//                                        CommonUtil.convertObjToStr(new Double(
//                                        CommonUtil.convertObjToDouble(getIntCr()).doubleValue()
//                                        - CommonUtil.convertObjToDouble(getClosingIntDb()).doubleValue())));
                                    this.setPermanentPayReceivable(CommonUtil.convertObjToStr(new Double(
                                            CommonUtil.convertObjToDouble(getIntCr()).doubleValue()
                                            - CommonUtil.convertObjToDouble(getClosingIntDb()).doubleValue())));
                                }

                                //                                System.out.println("#########payRecValue else :" +this.getPayReceivable());
                            }
                            double negValue = CommonUtil.convertObjToDouble(getPayReceivable()).doubleValue();
                            if (negValue < 0) {
                                negValue = negValue * -1;
                                this.setPayReceivable(String.valueOf(negValue));
                                this.setPermanentPayReceivable(String.valueOf(negValue));
                            }
                        }
                    }
                } 
                else if (behavesMap.get("BEHAVES_LIKE").equals("CUMMULATIVE")) {
                    System.out.println("jjjjjjjjjjjjjjjjjjjjjjjjjjjjkkkkkkkkk");
                    if (isRdoTransfer_Yes() == true) {
                        transfer_out_mode = "Y";
                        //                            System.out.println("######interest : "+this.getClosingIntDb());
                        //                            System.out.println("######interest : "+interest);
                        this.setIntDrawn(getIntDrawn());
                        //  interest = (double)getNearest((long)(interest *100),100)/100;
                        interest = getRound(interest, getInterestRound());
                        // balIntAmt = (double)getNearest((long)(balIntAmt *100),100)/100;
                        balIntAmt = getRound(balIntAmt, getInterestRound());
                        System.out.println("jjjjjj6666666666666666");
                        setClosingIntDb(String.valueOf(interest));
                        setClosingIntCr(String.valueOf(balIntAmt));
                        setClosingDisbursal(getBalance()); //Maturity Amt
                        setDisburseAmt(CommonUtil.convertObjToDouble(this.getClosingDisbursal()).doubleValue());
                        //                            System.out.println("######DisburseAmt : "+getDisburseAmt());
                        //                            System.out.println("###### principal: "+principal);
                        if (getDisburseAmt() > principal) {
                            setLblPayRecDet("Payable");
                            this.setPayReceivable(String.valueOf(0.0));
                            this.setPermanentPayReceivable(String.valueOf(0.0));
                            //                                System.out.println("#########payRecValue :" +this.getPayReceivable());
                        } else {
                            setLblPayRecDet("Receivable");
                            this.setPayReceivable(String.valueOf(0.0));
                            this.setPermanentPayReceivable(String.valueOf(0.0));
                            //                                System.out.println("#########payRecValue else :" +this.getPayReceivable());
                        }
                        if (totAmt == intDrawn) {
                            this.setClosingDisbursal(getBalance());
                            this.setPayReceivable(String.valueOf(0.0));
                            this.setPermanentPayReceivable(String.valueOf(0.0));
                        }
                    } else {
                        double period = 0;
                        double rateOfInterest = 0.0;
                        double diff = 0;
                        uncompletedInterest = 0.0;
                        String PrematureApply = "";
                        String seniorBenifitApply = "";
                        double seniorBenifitRate = 0.0;
                        //String  seniorBenifitApply="";
                        double seniorBenifit =0.0;
                        System.out.println("jjjjjj777777777777");
                        this.setClosingIntDb(String.valueOf(CommonUtil.convertObjToDouble(intDrawnMap.get("INT_AMT")).doubleValue()));
                        principal = new Double(getPrinicipal().length() > 1 ? getPrinicipal() : "0").doubleValue();
                        this.setMaturityValue(String.valueOf(CommonUtil.convertObjToDouble(custMap.get("MATURITY_AMT")).doubleValue()));
                        rateOfInterest = CommonUtil.convertObjToDouble(getPrematureClosingRate()).doubleValue();
                        if (((String) objMap.get("MODE")).equals(CommonConstants.PREMATURE_CLOSURE)) {
                            transfer_out_mode = "N";
                            List prematureApplyList = ClientUtil.executeQuery("getPrematureCloserApply", custMap);
                            if (prematureApplyList != null && prematureApplyList.size() > 0) {
                                HashMap cumMap = (HashMap) prematureApplyList.get(0);
                                PrematureApply = CommonUtil.convertObjToStr(cumMap.get("PREMATURE_CLOSURE_APPLY"));
                                seniorBenifitApply = CommonUtil.convertObjToStr(cumMap.get("NORMAL_RATE_FOR_SENIOR_CITIZEN"));
                                seniorBenifitRate = CommonUtil.convertObjToDouble(cumMap.get("SENIOR_BENIFIT_RATE"));                                
                            //}
                            if (PrematureApply != null && PrematureApply.length()>0 && PrematureApply.equals("SB Rate")) {
                                depDate = DateUtil.getDateMMDDYYYY(getDepositDate());
                                Date acopdate = DateUtil.getDateMMDDYYYY(getDepositDate());
                                Date matdt = DateUtil.getDateMMDDYYYY(getMaturityDate());
                                if (DateUtil.dateDiff(curDate, matdt) > 0) {
                                    matdt = curDate;
                                }
                                Date maturityDate = (Date) curDate.clone();
                                maturityDate.setDate(matdt.getDate());
                                maturityDate.setMonth(matdt.getMonth());
                                maturityDate.setYear(matdt.getYear());

                                Date acopeningDt = (Date) curDate.clone();
                                acopeningDt.setDate(acopdate.getDate());
                                acopeningDt.setMonth(acopdate.getMonth());
                                acopeningDt.setYear(acopdate.getYear());
                                long periodSB = DateUtil.dateDiff(acopeningDt, maturityDate);
                                HashMap rdPaidInst = new HashMap();
                                rdPaidInst.put("DEPOSIT_DT", DateUtil.getDateMMDDYYYY(getDepositDate()));
                                rdPaidInst.put("PRODID", getProdID());
                                rdPaidInst.put("CUSTID", getCustomerID());
                                rdPaidInst.put("DEPOSITNO", getTxtDepositNo());
                                rdPaidInst.put("PERIOD", new Long(periodSB));
                                List SbRateList = ClientUtil.executeQuery("getSBrateDetails", rdPaidInst);
                                if (SbRateList != null && SbRateList.size() > 0) {
                                    rdPaidInst = (HashMap) SbRateList.get(0);
                                    rateOfInterest = CommonUtil.convertObjToDouble(rdPaidInst.get("ROI")).doubleValue();

                                    if (custMap.containsKey("CATEGORY") && custMap.get("CATEGORY").equals("SENIOR_CITIZENS")) {

                                        if (seniorBenifitApply != null && seniorBenifitApply.length()>0 && seniorBenifitApply.equals("N")) {
                                            rateOfInterest = rateOfInterest + seniorBenifitRate;
                                        } else {
                                            rateOfInterest = rateOfInterest;
                                        }
                                        
                                    } 
                                    setRateApplicable(String.valueOf(rateOfInterest));
                                    setPrematureClosingRate(String.valueOf(rateOfInterest));

                                }
                            }
                        }
                            double yrPeriod = 0.0;
                            double mnPeriod = 0.0;
                            double amount1 = 0.0;
                            period = totalMonths / 3;
                            period = (long) roundOffLower((long) (period * 100), 100) / 100;
                            if (period > 0 && rateOfInterest > 0) {
                                amount = principal * (Math.pow((1 + rateOfInterest / 400), period));// Completed quarters.....
                                completedInterest = amount - principal;
                                amount1 = amount;
                                //                                Date nextDate = depDate;
                                //                                Date k = DateUtil.nextCalcDate(depDate,depDate,30);
                                //                                period = period * 3;
                                //                                for(int i=0; i<period; i++)
                                //                                    depDate = DateUtil.nextCalcDate(depDate,k, 30);
                                //                                period = period *3;
                                //                                for(int i =0; i<period;i++)
                                //                                    depDate = DateUtil.addDays(depDate,30);
                                //                                Date currDate = (Date)curDate.clone();
                                //                                diffDay = DateUtil.dateDiff(depDate,currDate);
                                //                                double leftMonth = (double)totalMonths - period * 3;
                                //                                diffDay = (long)(leftMonth * 30 + diffDay);
                                if (diffDay > 0) {
                                    amount = amount + (amount * rateOfInterest * diffDay) / (36500);// unCompleted quarters.....
                                    uncompletedInterest = amount - amount1;
                                }
                            } else if (diffDay > 0 && rateOfInterest > 0) {
                                principal = CommonUtil.convertObjToDouble(custMap.get("DEPOSIT_AMT")).doubleValue();
                                amount = principal + (principal * rateOfInterest * diffDay) / (36500);// unCompleted quarters.....
                                uncompletedInterest = amount - principal;
                            }
                            interest = completedInterest + uncompletedInterest + interestgreater;
                            System.out.println("####CUMMULATIVE interest : " + interest);
                        } else {
                            transfer_out_mode = "NO";
                            interest = CommonUtil.convertObjToDouble(custMap.get("TOT_INT_AMT")).doubleValue();
                            System.out.println("interest=======" + interest);
                            Date matDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(custMap.get("MATURITY_DT")));
                            amount = CommonUtil.convertObjToDouble(custMap.get("MATURITY_AMT")).doubleValue();
                            rateOfInterest = CommonUtil.convertObjToDouble(getPrematureClosingRate()).doubleValue();
                            Date matDt = (Date) curDate.clone();
                            if (matDate.getDate() > 0) {
                                matDt.setYear(matDate.getYear());
                                matDt.setMonth(matDate.getMonth());
                                matDt.setDate(matDate.getDate());
                            }
                            holidayAmt = holidayProvision(matDt, amount, interest, rateOfInterest, holidayProv);
                            //added by rishad for death interest calculation for SENIOR_CITIZENS  
                             Date deathDt = null;
                            HashMap hmap = new HashMap();
                            hmap.put("CUST_ID", getCustomerID());
                            hmap.put("DEPOSIT_NO", getTxtDepositNo());
                            List seniorList = ClientUtil.executeQuery("getSeniorDetails", hmap);
                            if (seniorList != null && seniorList.size() > 0) {
                                hmap = (HashMap) seniorList.get(0);
                                category = CommonUtil.convertObjToStr(hmap.get("CATEGORY"));
                                deathDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(hmap.get("DEATH_DT")));
                            }
                            if (category.equals("SENIOR_CITIZENS") && custMap.get("DEATH_CLAIM") != null && custMap.get("DEATH_CLAIM").equals("Y") && deathFlag == false) {
                                //                                ClientUtil.showMessageWindow(""
                                if (DateUtil.dateDiff(deathDt, DateUtil.getDateMMDDYYYY(getMaturityDate())) > 0) {
                                    HashMap calculationMap = new HashMap();
                                    calculationMap.put("DEP_AMOUNT", custMap.get("DEPOSIT_AMT"));
                                    calculationMap.put("CLEAR_BALANCE", custMap.get("CLEAR_BALANCE"));
                                    calculationMap.put("MAT_AMOUNT", custMap.get("MATURITY_AMT"));
                                    calculationMap.put("CATEGORY_ID", custMap.get("CATEGORY"));
                                    calculationMap.put("BEHAVES_LIKE", behavesMap.get("BEHAVES_LIKE"));
                                    calculationMap.put("DEATH_DT", deathDt);
                                    calculationMap.put("RATE_OF_INTEREST", new Double(rateOfInterest));
                                    calculationMap.put("NORMAL", "NORMAL");

                                    interest = CalculationForSeniorCitizenDeath(calculationMap);
                                }
                            }
                            if (custMap.get("DEATH_CLAIM") != null && custMap.get("DEATH_CLAIM").equals("Y") && deathFlag == false) {
                                HashMap calculationMap = new HashMap();
                                calculationMap.put("DEP_AMOUNT", custMap.get("DEPOSIT_AMT"));
                                calculationMap.put("CLEAR_BALANCE", custMap.get("CLEAR_BALANCE"));
                                calculationMap.put("MAT_AMOUNT", custMap.get("MATURITY_AMT"));
                                calculationMap.put("CATEGORY_ID", custMap.get("CATEGORY"));
                                calculationMap.put("BEHAVES_LIKE", behavesMap.get("BEHAVES_LIKE"));
                                double deathAmt = 0.0;
                                if(custMap.containsKey("DEATH_CLAIM_INT") && custMap.get("DEATH_CLAIM_INT")!= null && CommonUtil.convertObjToStr(custMap.get("DEATH_CLAIM_INT")).equalsIgnoreCase("Y")){// Added by nithya on 13-08-2019 for KD 583
                                   deathAmt = simpleInterestCalculation(calculationMap);
                                }
                                interest = interest + deathAmt;
                                calculationMap = null;
                                setDeathClaim("Y");
                            }
                        }
                        interest = interest + holidayAmt;
                        // interest = (double)getNearest((long)(interest *100),100)/100;
                        interest = getRound(interest, getInterestRound());
                        if (holidayAmt > 0) {
                            ClientUtil.showMessageWindow("Holiday interest amount is :" + holidayAmt);
                        }
                        double availableBal = CommonUtil.convertObjToDouble(custMap.get("TOTAL_BALANCE")).doubleValue();
                        if (availableBal == 0) {
                            setClosingDisbursal(CommonUtil.convertObjToStr("0"));
                            this.setPayReceivable(CommonUtil.convertObjToStr("0"));
                            this.setPermanentPayReceivable(CommonUtil.convertObjToStr("0"));
                            setPrematureClosingRate(CommonUtil.convertObjToStr("0"));
                            ClientUtil.showAlertWindow("No Balance in the account. Do you want to really close...");
                        } else {
                            System.out.println("jjjjjj8888888888888");
                            this.setClosingIntDb(String.valueOf(interest));
                            double intAmt = CommonUtil.convertObjToDouble(custMap.get("TOTAL_INT_DRAWN")).doubleValue();
                            // intAmt = (double)getNearest((long)(intAmt *100),100)/100;
                            intAmt = getRound(intAmt, getInterestRound());
                            double amt = principal + intAmt;
                            amt = (double) getNearest((long) (amt * 100), 100) / 100;
                            setBalance(String.valueOf(amt));
                            this.setIntDrawn(getIntDrawn());
                            this.setClosingDisbursal(
                                    CommonUtil.convertObjToStr(new Double(
                                    CommonUtil.convertObjToDouble(getPrinicipal()).doubleValue()
                                    + CommonUtil.convertObjToDouble(getClosingIntDb()).doubleValue())));
                            //                        + CommonUtil.convertObjToDouble(getIntDrawn()).doubleValue()))); //Maturity Amt
                            setDisburseAmt(CommonUtil.convertObjToDouble(this.getClosingDisbursal()).doubleValue());
                            //                            System.out.println("#########payRecValue behavesLikeMap else :" +this.getDisburseAmt());

                            if (getDisburseAmt() > amt) {
                                setLblPayRecDet("Payable");
                                this.setPayReceivable(
                                        CommonUtil.convertObjToStr(new Double(
                                        CommonUtil.convertObjToDouble(getClosingIntDb()).doubleValue()
                                        - CommonUtil.convertObjToDouble(getIntDrawn()).doubleValue())));
                                this.setPermanentPayReceivable(CommonUtil.convertObjToStr(new Double(
                                        CommonUtil.convertObjToDouble(getClosingIntDb()).doubleValue()
                                        - CommonUtil.convertObjToDouble(getIntDrawn()).doubleValue())));
                            } else {
                                setLblPayRecDet("Receivable");
                                this.setPayReceivable(
                                        CommonUtil.convertObjToStr(new Double(
                                        CommonUtil.convertObjToDouble(getIntCr()).doubleValue()
                                        - CommonUtil.convertObjToDouble(getClosingIntDb()).doubleValue())));
                                this.setPermanentPayReceivable(CommonUtil.convertObjToStr(new Double(
                                        CommonUtil.convertObjToDouble(getIntCr()).doubleValue()
                                        - CommonUtil.convertObjToDouble(getClosingIntDb()).doubleValue())));
                            }
                            double negValue = CommonUtil.convertObjToDouble(getPayReceivable()).doubleValue();
                            if (negValue < 0) {
                                negValue = negValue * -1;
                                this.setPayReceivable(String.valueOf(negValue));
                                this.setPermanentPayReceivable(String.valueOf(negValue));
                            }
                        }
                    }

                } else if (behavesMap.get("BEHAVES_LIKE").equals("DAILY")) {//DAILY DEPOSIT CALCULATION STARTING
                    DepositsProductIntPayTO depositsProductIntPayTO =null;
                    custMap.put("MODE", objMap.get("MODE"));
                    Date matDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(custMap.get("MATURITY_DT")));
                    amount = CommonUtil.convertObjToDouble(custMap.get("MATURITY_AMT")).doubleValue();
                    double rateOfInterest =0;
                    //Change ROI rate 
                    String rdApply="";
                    List rdApplyList = ClientUtil.executeQuery("getIrregularRDApply", custMap);
                    if (rdApplyList != null && rdApplyList.size() > 0) {
                        HashMap rdMap = (HashMap) rdApplyList.get(0);
                        rdApply = CommonUtil.convertObjToStr(rdMap.get("IRREGULAR_RD_APPLY"));
                    }
                    if ((rdApply!=null && rdApply.equals("SB Rate")) && (((String) objMap.get("MODE")).equals(CommonConstants.PREMATURE_CLOSURE)||((String) objMap.get("MODE")).equals(CommonConstants.NORMAL_CLOSURE))) {
                          Date acopdate = DateUtil.getDateMMDDYYYY(getDepositDate());
                          System.out.println("acopdate------->"+acopdate);
                          Date acopeningDt = (Date) curDate.clone();
                          acopeningDt.setDate(acopdate.getDate());
                          acopeningDt.setMonth(acopdate.getMonth());
                          acopeningDt.setYear(acopdate.getYear());
                          System.out.println("acopeningDt  77------->"+acopeningDt);   
                          Date matdt = DateUtil.getDateMMDDYYYY(getMaturityDate());
                          System.out.println("matdt  77------->"+matdt); 
                          if (DateUtil.dateDiff(curDate, matdt) > 0) {
                               matdt = curDate;
                          }
                          System.out.println("matdt  88------->"+matdt); 
                          Date maturityDate = (Date) curDate.clone();
                          maturityDate.setDate(matdt.getDate());
                          maturityDate.setMonth(matdt.getMonth());
                          maturityDate.setYear(matdt.getYear());
                          System.out.println("maturityDate  88------->"+maturityDate); 
                         double sbrate = 0.0;
                         long periodSB = DateUtil.dateDiff(acopeningDt, maturityDate);
                         HashMap rdPaidInst=new HashMap();
                         rdPaidInst.put("DEPOSIT_DT", DateUtil.getDateMMDDYYYY(getDepositDate()));
                         rdPaidInst.put("PRODID", getProdID());
                         rdPaidInst.put("CUSTID", getCustomerID());
                         rdPaidInst.put("DEPOSITNO", getTxtDepositNo());
                         rdPaidInst.put("PERIOD", new Long(periodSB));
                          System.out.println("rdPaidInst  88------->"+rdPaidInst); 
                           HashMap intCommMap = new HashMap();
                            intCommMap.put("value", getProdID());
                            List intCommList = ClientUtil.executeQuery("getSelectDepositsProductIntPayTO", intCommMap);
                            depositsProductIntPayTO = (DepositsProductIntPayTO) intCommList.get(0);
                            if (depositsProductIntPayTO!=null && depositsProductIntPayTO.getSlabWiseInterest() != null && depositsProductIntPayTO.getSlabWiseInterest().equals("Y")) {
                                HashMap dtMap=new HashMap();
                                dtMap.put("PROD_ID",getProdID());
                                double periodMonths = monthDiff(acopeningDt, maturityDate);
                                periodMonths=getRound(periodMonths,"HIGHER_VALUE");
                                if(periodMonths<0){
                                  periodMonths=-1*periodMonths;
                                }
                                long period=Math.round(periodMonths);
                                dtMap.put("PERIOD",CommonUtil.convertObjToDouble(period));
                                List roiLst=ClientUtil.executeQuery("getDailyROIRate", dtMap);
                                if(roiLst!=null && roiLst.size()>0){
                                   HashMap roiMap =(HashMap)roiLst.get(0); 
                                   if(roiMap!=null && roiMap.containsKey("ROI")){
                                       double roiDaily=CommonUtil.convertObjToDouble(roiMap.get("ROI"));
                                        setRateApplicable(String.valueOf(roiDaily));
                                        setPrematureClosingRate(String.valueOf(roiDaily));
                                        rateOfInterest=roiDaily;
                                         if (rdoTypeOfDeposit_No && ((String) objMap.get("MODE")).equals(CommonConstants.PREMATURE_CLOSURE)) { //Added by nithya for KD-3211                                            
                                           double roiDailyPrematureClose = CommonUtil.convertObjToDouble(roiMap.get("PREMATURE_ROI"));  
                                           setDailyDepositLoanPreClose("Y");
                                           setDailyDepositLoanPreCloseROI(String.valueOf(roiDailyPrematureClose));
                                         }                                        
                                   }
                                }
                            }
                            else{
                                 List SbRateList = ClientUtil.executeQuery("getSBrateDetails", rdPaidInst);
                                 if (SbRateList != null && SbRateList.size() > 0) {
                                        rdPaidInst = (HashMap) SbRateList.get(0);
                                        sbrate = CommonUtil.convertObjToDouble(rdPaidInst.get("ROI")).doubleValue();
                                         System.out.println("sbrate  88------->"+sbrate); 
                                        setRateApplicable(String.valueOf(sbrate));
                                        setPrematureClosingRate(String.valueOf(sbrate));
                                        rateOfInterest=sbrate;
                                 }
                            }
                    }
                    else{
                    //End
                    rateOfInterest = CommonUtil.convertObjToDouble(getPrematureClosingRate()).doubleValue();
                    }
                    Date matDt = (Date) curDate.clone();
                    if (matDate.getDate() > 0) {
                        matDt.setYear(matDate.getYear());
                        matDt.setMonth(matDate.getMonth());
                        matDt.setDate(matDate.getDate());
                    }
                    custMap.put("INT_AMT", intDrawnMap.get("INT_AMT"));
                    HashMap intCommMap = new HashMap();
                    intCommMap.put("value", getProdID());
                    List intCommList = ClientUtil.executeQuery("getSelectDepositsProductIntPayTO", intCommMap);
                    depositsProductIntPayTO = (DepositsProductIntPayTO) intCommList.get(0);
                    dailyDepositInterestCalc(custMap, matDt, amount, interest, rateOfInterest, holidayProv, commisionMode, totalMonths, diffDay,
                            interestNotPaying, interestNotPayingMode,depositsProductIntPayTO);
                }
                setPenaltyInt(CommonUtil.convertObjToStr(custMap.get("PENAL_INT")));
            }
            custMap = null;
            list = null;
            if (getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                notifyObservers();
            }




        }

    }

    private void setClosingFloatingDetails(HashMap mapData) {
        System.out.println("setClosingDetails : " + mapData);

        HashMap custMap = new HashMap();
        double amount = 0.0;
        double principal = 0.0;
        double interest = 0.0;
        double interestAmt = 0.0;
        double intDrawn = 0.0;
        double intCredit = 0.0;
        double balIntAmt = 0.0;
        double totAmt = 0.0;
        double commPeriod = 0.0;
        double prematureMinPeriod = 0.0;
        int weeklycalcNo = 0;
        double interestNotPaying = 0.0;
        String interestNotPayingMode = "";
        HashMap withPeriodMap = new HashMap();
        if (CommonUtil.convertObjToStr(mapData.get("FLOATING_TYPE")).equals("WITHOUT_PERIOD")) {
            withPeriodMap = (HashMap) mapData.get("DEPOSIT_CLOSE_DETAILS_MAP");
        } else {
            withPeriodMap = mapData;
        }
        custMap.put("DEPOSIT_NO", withPeriodMap.get("DEPOSITNO"));
        List list = ClientUtil.executeQuery("getCustDepositNoBehavesLike", custMap);
        if (list != null && list.size() > 0) {
            if (isRdoTransfer_Yes() == true) {
                transfer_out_mode = "Y";
            } else {
                transfer_out_mode = "N";
            }

            custMap = (HashMap) list.get(0);
            behavesMap.put("BEHAVES_LIKE", custMap.get("BEHAVES_LIKE"));
            String holidayProv = null;
            String commisionMode = null;
            setLstProvDt(CommonUtil.convertObjToStr(custMap.get("LST_PROV_DT")));
            interestNotPaying = CommonUtil.convertObjToDouble(custMap.get("INTEREST_NOT_PAYING")).doubleValue();
            interestNotPayingMode = CommonUtil.convertObjToStr(custMap.get("INTEREST_NOT_PAYING_MODE"));
            holidayProv = CommonUtil.convertObjToStr(custMap.get("PAYINT_DEP_MATURITY"));
            commisionMode = CommonUtil.convertObjToStr(custMap.get("AGENT_COMMISION_MODE"));
            setDeathClaimAmt(String.valueOf("0"));
            setTotalBalance(CommonUtil.convertObjToStr(custMap.get("TOTAL_BALANCE")));
            double recurringAmount = CommonUtil.convertObjToDouble(custMap.get("CLEAR_BALANCE")).doubleValue();
            intDrawn = CommonUtil.convertObjToDouble(custMap.get("TOTAL_INT_DRAWN")).doubleValue();
            intCredit = CommonUtil.convertObjToDouble(custMap.get("TOTAL_INT_CREDIT")).doubleValue();
            totAmt = CommonUtil.convertObjToDouble(custMap.get("TOT_INT_AMT")).doubleValue();
            // balIntAmt = (double)getNearest((long)(balIntAmt *100),100)/100;
            balIntAmt = getRound(balIntAmt, getInterestRound());
            //  intDrawn = (double)getNearest((long)(intDrawn *100),100)/100;
            intDrawn = getRound(intDrawn, getInterestRound());

            //  intCredit = (double)getNearest((long)(intCredit *100),100)/100;
            intCredit = getRound(intCredit, getInterestRound());
            balIntAmt = intCredit - intDrawn;
            this.setClosingIntCr(String.valueOf(balIntAmt));
            this.setIntDrawn(String.valueOf(intDrawn));
            this.setIntCr(String.valueOf(intCredit));
            this.setLastIntAppDate(CommonUtil.convertObjToStr(map.get("INT_LAST_APPL_DT")));
            this.setTdsCollected(CommonUtil.convertObjToStr(new Double(CommonUtil.convertObjToDouble(getTdsCollected()).doubleValue())));
            this.setPrematureClosingDate(DateUtil.getStringDate(curDate));
            this.setPrematureClosingDate(DateUtil.getStringDate(curDate));
            setBalanceAmt(CommonUtil.convertObjToDouble(custMap.get("CLEAR_BALANCE")).doubleValue());
            setBalance(CommonUtil.convertObjToStr(custMap.get("CLEAR_BALANCE")));
            setDelayedInstallments("");
            setChargeAmount("");

            Date depDate = null;
            double interestAmount = 0.0;
            if (mapData.containsKey("FLOATING_TYPE")) {
                if (CommonUtil.convertObjToStr(mapData.get("FLOATING_TYPE")).equals("WITHOUT_PERIOD")) {
                    List withoutPeriodDtList = (List) mapData.get("DEPOSIT_CLOSE_DETAILS_WITHOUT_PERIOD");
                    System.out.println("#$#%#$%withoutPeriodDtList:" + withoutPeriodDtList);
                    for (int i = 0; i < withoutPeriodDtList.size(); i++) {
                        HashMap withPeriodDt = (HashMap) withoutPeriodDtList.get(i);
                        Date nextInterestDt = null;
                        if (i + 1 < withoutPeriodDtList.size()) {
                            HashMap nextIntDtMap = (HashMap) withoutPeriodDtList.get(i + 1);
                            nextInterestDt = (Date) nextIntDtMap.get("ROI_DT");
                        }
                        Date interestDt = (Date) withPeriodDt.get("ROI_DT");
                        System.out.println("@#$%#$%interestDt:" + interestDt);
                        withPeriodMap.put("DEPOSIT_DT", interestDt);
                        withPeriodMap.put("AMOUNT", CommonUtil.convertObjToDouble(withPeriodMap.get("MATURITY_AMT")));
                        withPeriodMap.put("PROD_ID", withPeriodMap.get("PRODUCT_ID"));
                        double maturityAmount = CommonUtil.convertObjToDouble(withPeriodMap.get("AMOUNT")).doubleValue();
                        setBalanceDeposit(String.valueOf(maturityAmount));
                        System.out.println("#$#%#$%withPeriodMap:" + withPeriodMap);
                        List lstInt = (List) ClientUtil.executeQuery("icm.getInterestRates", withPeriodMap);
                        if (lstInt != null && lstInt.size() > 0) {
                            HashMap lstIntMap = (HashMap) lstInt.get(0);
                            System.out.println("#$%#$%lstIntMap: " + i + " : " + lstIntMap);
                            double roi = CommonUtil.convertObjToDouble(lstIntMap.get("ROI")).doubleValue();
                            double noOfDays = 0;
                            if (nextInterestDt != null) {
                                noOfDays = DateUtil.dateDiff(interestDt, nextInterestDt);
                            }
                            interestAmount += (roi * maturityAmount * noOfDays) / 36500;
                            System.out.println("@#$%#$%interestAmount" + interestAmount);
                            // interestAmount = (double)getNearest((long)(interestAmount *100),100)/100;
                            interestAmount = getRound(interestAmount, getInterestRound());
                            System.out.println("jjjjjj9999999999999999999");
                            setClosingIntDb(String.valueOf(interestAmount));
                            double closingDisbursal = CommonUtil.convertObjToDouble(getPrinicipal()).doubleValue() + interestAmount;
                            setClosingDisbursal(String.valueOf(closingDisbursal));
                            setLblPayRecDet("Payable");
                            this.setPayReceivable(String.valueOf(interestAmount));
                            this.setPermanentPayReceivable(String.valueOf(interestAmount));

                        }
                    }
                    System.out.println("@#$%#$%interestAmountfinal" + interestAmount);
                } else if (CommonUtil.convertObjToStr(mapData.get("FLOATING_TYPE")).equals("WITH_PERIOD")) {
                    System.out.println("$%#$%$inside with period:" + withPeriodMap);
                    Date depositDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(withPeriodMap.get("DEPOSIT_DT")));
                    Date closingDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(withPeriodMap.get("CLOSING_DT")));
                    Date maturityDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(withPeriodMap.get("MATURITY_DT")));

                    //                        for normal closure, i.e.,within the period
                    withPeriodMap.put("DEPOSIT_DT", depositDate);
                    withPeriodMap.put("AMOUNT", CommonUtil.convertObjToDouble(withPeriodMap.get("MATURITY_AMT")));
                    withPeriodMap.put("PROD_ID", withPeriodMap.get("PRODUCT_ID"));
                    withPeriodMap.put("PERIOD", new Double(DateUtil.dateDiff(depositDate, maturityDate)));
                    double maturityAmount = CommonUtil.convertObjToDouble(withPeriodMap.get("AMOUNT")).doubleValue();
                    setBalanceDeposit(String.valueOf(maturityAmount));
                    double roi = 0.0;
                    double penal = 0.0;
                    List lstInt = (List) ClientUtil.executeQuery("icm.getInterestRates", withPeriodMap);
                    if (lstInt != null && lstInt.size() > 0) {
                        HashMap lstIntMap = (HashMap) lstInt.get(0);
                        System.out.println("#$%#$%lstIntMap: " + lstIntMap);
                        roi = CommonUtil.convertObjToDouble(lstIntMap.get("ROI")).doubleValue();
                        penal = CommonUtil.convertObjToDouble(lstIntMap.get("PENAL_INT")).doubleValue();
                    }
                    int period = 0;
                    if (closingDate.after(maturityDate) || DateUtil.dateDiff(closingDate, maturityDate) == 0) {
//                        System.out.println("!!!! Closing date is after maturity date is true");
                        period = (int) DateUtil.dateDiff(depositDate, maturityDate);
                    } else if (closingDate.before(maturityDate)) {

//                        System.out.println("!!!! Closing date is before maturity date is true");
                        period = (int) DateUtil.dateDiff(depositDate, closingDate);
//                        double penal = CommonUtil.convertObjToDouble(custMap.get("PENAL_INT")).doubleValue();
                        String closingPenal = CommonUtil.convertObjToStr(custMap.get("PENAL_INT"));
                        String closingRoi = CommonUtil.convertObjToStr(custMap.get("ROI"));
                        this.setRateApplicable(String.valueOf(roi));
                        if (getLblClosingType().equals(CommonConstants.PREMATURE_CLOSURE)) {
                            withPeriodMap.put("PERIOD", new Double(period));
                            lstInt = (List) ClientUtil.executeQuery("icm.getInterestRates", withPeriodMap);
                            if (lstInt != null && lstInt.size() > 0) {
                                HashMap lstIntMap = (HashMap) lstInt.get(0);
                                System.out.println("#$%#$%lstIntMap: " + lstIntMap);
                                roi = CommonUtil.convertObjToDouble(lstIntMap.get("ROI")).doubleValue();
                                penal = CommonUtil.convertObjToDouble(lstIntMap.get("PENAL_INT")).doubleValue();
                            }



                            System.out.println("getPremClosNNNNN" + getPremClos());
                            this.setRateApplicable(String.valueOf(roi));
                            if (isRdoYesButton()) {
                                this.setPenaltyPenalRate(String.valueOf(penal));
                                setPrematureClosingRate(String.valueOf(roi - penal));
                                roi = roi - penal;
                            } else if (isRdoNoButton()) {
                                setPrematureClosingRate(String.valueOf(roi));
                                this.setPenaltyPenalRate(String.valueOf(0.0));
                            }

                        } else if (getLblClosingType().equals(CommonConstants.NORMAL_CLOSURE)) {
                            setPrematureClosingRate(closingRoi);
                            this.setPenaltyPenalRate(String.valueOf(0.0));
                            roi = new Double(closingRoi).doubleValue();
                        }
                    }
                    interestAmount += (roi * maturityAmount * period) / 36500;
                    System.out.println("interest for the 1st period:" + interestAmount);
                    if (closingDate.after(maturityDate)) {
                        Date startDt = (Date) maturityDate.clone();
                        Date endDt = (Date) maturityDate.clone();
                        int noOfDays = 0;
                        int i = 2;
                        while (DateUtil.dateDiff(endDt, closingDate) > 0) {
                            endDt = DateUtil.addDays(endDt, period, true);
                            if (DateUtil.dateDiff(endDt, closingDate) > 0) {
                                noOfDays = (int) DateUtil.dateDiff(startDt, endDt);
                            } else {
                                noOfDays = (int) DateUtil.dateDiff(startDt, closingDate);
                            }
                            withPeriodMap.put("DEPOSIT_DT", startDt);
//                                withPeriodMap.put("PERIOD", new Double(noOfDays));
                            System.out.println("#$#%#$%withPeriodMap inside while loop:" + withPeriodMap);
                            List lstInterst = (List) ClientUtil.executeQuery("icm.getInterestRates", withPeriodMap);
                            if (lstInterst != null && lstInterst.size() > 0) {
                                HashMap lstInterstMap = (HashMap) lstInterst.get(0);
                                System.out.println("#$%#$%lstInterstMap: " + i + " : " + lstInterstMap);
                                roi = CommonUtil.convertObjToDouble(lstInterstMap.get("ROI")).doubleValue();
                                interestAmount += (roi * maturityAmount * noOfDays) / 36500;
                                System.out.println("@#$%#$%interestAmount for " + i + " Period :" + roi + "*" + maturityAmount + "*"
                                        + +noOfDays + "/" + ")/36500 :" + (roi * maturityAmount * noOfDays) / 36500);
                                System.out.println("@#$%#$%tot interestAmount for " + i + " Period :" + interestAmount);
                            }
                            startDt = (Date) endDt.clone();
                            i++;
                        }
                    }
                    //interestAmount = (double)getNearest((long)(interestAmount *100),100)/100;
                    interestAmount = getRound(interestAmount, getInterestRound());
                    System.out.println("jjjjjj10000000000000000");
                    setClosingIntDb(String.valueOf(interestAmount));
                    double closingDisbursal = CommonUtil.convertObjToDouble(getPrinicipal()).doubleValue() + interestAmount;
                    setClosingDisbursal(String.valueOf(closingDisbursal));
                    setLblPayRecDet("Payable");
                    this.setPayReceivable(String.valueOf(interestAmount));
                    this.setPermanentPayReceivable(String.valueOf(interestAmount));
                }
            }

            setPenaltyInt(CommonUtil.convertObjToStr(custMap.get("PENAL_INT")));
        }
        custMap = null;
        list = null;
        if (getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            notifyObservers();
        }
    }

    public long roundOffLower(long number, long roundingFactor) {
        long mod = number % roundingFactor;
        return number - mod;
    }

    public void dailyDepositInterestCalc(HashMap custMap, Date matDt, double amount, double interest,
            double rateOfInterest, String holidayProv, String commisionMode, double totalMonths, double diffDay,
            double interestNotPaying, String interestNotPayingMode,DepositsProductIntPayTO depositsProductIntPayTO) {
        double period = 0;
        double diff = 0;
        double uncompletedInterest = 0.0;
        //        double totalMonths = 0.0;
        //        double diffDay = 0.0;
        double completedInterest = 0.0;
        double sumOfAmt = 0.0;
        double weekEndAmt = 0.0;
        double weekTransAmt = 0.0;
        int weeklycalcNo = 0;
        double commPeriod = 0.0;
        double prematureMinPeriod = 0.0;
        if (isRdoTransfer_Yes() == true) {
            transfer_out_mode = "Y";
        } else {
            transfer_out_mode = "N";
            commPeriod = CommonUtil.convertObjToDouble(custMap.get("AGENT_COMMISION_PERIOD")).doubleValue();
            prematureMinPeriod = CommonUtil.convertObjToDouble(custMap.get("PREMATURE_MIN_PERIOD")).doubleValue();
            String dailyIntCalc = CommonUtil.convertObjToStr(custMap.get("DAILY_INT_CALC"));
            double depFreq = CommonUtil.convertObjToDouble(custMap.get("DEPOSIT_FREQ"));
            String weekSpec = CommonUtil.convertObjToStr(custMap.get("WEEKLY_SPEC"));
            if (custMap.get("BEHAVES_LIKE").equals("DAILY")) {
                if (dailyIntCalc.equals("WEEKLY")) {
                    weeklycalcNo = CommonUtil.convertObjToInt(custMap.get("WEEKLY_BASIS"));
                } else {
                    weeklycalcNo = 0;
                }
            }
            System.out.println("jjjjjj1111111111122222222");
            this.setClosingIntDb(String.valueOf(CommonUtil.convertObjToDouble(custMap.get("INT_AMT")).doubleValue()));
            double principal = new Double(getPrinicipal().length() > 1 ? getPrinicipal() : "0").doubleValue();
            System.out.println("####DAILY period : " + principal);
            HashMap customerMap = agentsCommisionAmt(custMap);
            HashMap dayendBalMap = new HashMap();
            HashMap weekTransMap = new HashMap();
            this.setMaturityValue(String.valueOf(CommonUtil.convertObjToDouble(custMap.get("MATURITY_AMT")).doubleValue()));
            rateOfInterest = CommonUtil.convertObjToDouble(getPrematureClosingRate()).doubleValue();
            Date depDate = DateUtil.getDateMMDDYYYY(getDepositDate());
            int depDay = depDate.getDay() + 1;
            Date depositDate = DateUtil.nextCalcDate(depDate, depDate, Math.abs(7 - depDay + weeklycalcNo));
            //            Date depositDate = DateUtil.addDays(depDate, Math.abs(7-depDay + weeklycalcNo));
            if (((String) custMap.get("MODE")).equals(CommonConstants.PREMATURE_CLOSURE)
                    || ((String) custMap.get("MODE")).equals(CommonConstants.NORMAL_CLOSURE)) {
                double totalPeriod = 0;
                long totPeriod = 0;
                double transactionAmt = 0.0;
                if ((DateUtil.dateDiff((Date) custMap.get("DEPOSIT_DT"), (Date) curDate.clone())) >= prematureMinPeriod) {
                    if (dailyIntCalc.equals("WEEKLY")) {
                        long Period = DateUtil.dateDiff((Date) depDate, (Date) curDate.clone());
                        totPeriod = Period / 7;
                        for (int i = 0; i < totPeriod; i++) {
                            dayendBalMap.put("DAY_END_DT", depositDate);
                            dayendBalMap.put("ACT_NUM", getTxtDepositNo());
                            List lst = ClientUtil.executeQuery("getSelectDailyDepositDayEndBal", dayendBalMap);
                            if (lst != null && lst.size() > 0) {
                                dayendBalMap = (HashMap) lst.get(0);
                                sumOfAmt += CommonUtil.convertObjToDouble(dayendBalMap.get("AMT")).doubleValue();
                            }
                            depositDate = DateUtil.addDays(depositDate, 7);
                        }
                        depDate = DateUtil.getDateMMDDYYYY(getDepositDate());
                        depositDate = DateUtil.getDateMMDDYYYY(getDepositDate());
                        Period = DateUtil.dateDiff((Date) depDate, (Date) curDate.clone());
                        totalPeriod = Period / 30;
                        Date lastDay = null;
                        for (int i = 0; i < totalPeriod; i++) {
                            int noOfDays = 0;
                            weekTransMap.put("ACC_NUM", getTxtDepositNo() + "_1");
                            weekTransMap.put("TRN_DT", depositDate);
                            if (i == 0) {
                                //                            GregorianCalendar firstdaymonth = new GregorianCalendar(1,depDate.getMonth(),depDate.getYear()+1900);
                                GregorianCalendar firstdaymonth = new GregorianCalendar((depDate.getYear() + 1900), depDate.getMonth(), depDate.getDate());
                                noOfDays = firstdaymonth.getActualMaximum(firstdaymonth.DAY_OF_MONTH);
                                lastDay = depDate;
                                lastDay.setDate(noOfDays);
                                weekTransMap.put("LAST_TRN_DT", lastDay);
                            }
                            if (i != 0) {
                                //                            GregorianCalendar firstdaymonth = new GregorianCalendar(1,depositDate.getMonth(),depositDate.getYear()+1900);
                                GregorianCalendar firstdaymonth = new GregorianCalendar((depositDate.getYear() + 1900), depositDate.getMonth(), depositDate.getDate());
                                noOfDays = firstdaymonth.getActualMaximum(firstdaymonth.DAY_OF_MONTH);
                                GregorianCalendar lastdaymonth = new GregorianCalendar(depositDate.getYear() + 1900, depositDate.getMonth(), noOfDays);
                                weekTransMap.put("LAST_TRN_DT", lastdaymonth.getTime());
                                depDate = lastdaymonth.getTime();
                            }
                            List lstTrans = ClientUtil.executeQuery("getSelectDailyDepositWeekendTrans", weekTransMap);
                            if (lstTrans != null && lstTrans.size() > 0) {
                                weekTransMap = (HashMap) lstTrans.get(0);
                                weekEndAmt = CommonUtil.convertObjToDouble(weekTransMap.get("AMOUNT")).doubleValue();
                                weekTransAmt = principal * noOfDays;
                                if (weekEndAmt > weekTransAmt) {
                                    transactionAmt += weekEndAmt - weekTransAmt;
                                }
                            }
                            lstTrans = null;
                            depositDate = DateUtil.addDays(depDate, 1);
                        }
                        interest += (sumOfAmt - transactionAmt) * 7 * rateOfInterest / 36500;
                    } else if (dailyIntCalc.equals("MONTHLY")) {
                        depDate = DateUtil.getDateMMDDYYYY(getDepositDate());
                        depositDate = DateUtil.getDateMMDDYYYY(getDepositDate());
                        double Perioddays = DateUtil.dateDiff((Date) depDate, (Date) curDate.clone());
                        totalPeriod = Perioddays / 30;
                        totalPeriod = (double) getNearest((long) (totalPeriod * 100), 100) / 100;
                        int noOfDays = 0;
                        Date lastDay = null;
                        //                    GregorianCalendar firstdaymonth = new GregorianCalendar(1,depDate.getMonth()+1,depDate.getYear()+1900);
                        GregorianCalendar firstdaymonth = new GregorianCalendar((depDate.getYear() + 1900), depDate.getMonth(), depDate.getDate());
                        noOfDays = firstdaymonth.getActualMaximum(firstdaymonth.DAY_OF_MONTH);
                        lastDay = depDate;
                        if (depDate.getMonth() == 1) {
                            noOfDays = 28;
                        }
                        if (((String) custMap.get("MODE")).equals(CommonConstants.NORMAL_CLOSURE)) {
                            int periodYear = CommonUtil.convertObjToInt(custMap.get("DEPOSIT_PERIOD_YY"));
                            int periodMonth = CommonUtil.convertObjToInt(custMap.get("DEPOSIT_PERIOD_MM"));
                            if (periodYear > 0) {
                                totalPeriod = (periodYear * 12) + periodMonth;
                            } else if (periodMonth > 0) {
                                totalPeriod = periodMonth;
                            }
                        }
                        lastDay.setDate(noOfDays);
                        if (custMap.containsKey("DAILY_INT_CALC_METHOD") && CommonUtil.convertObjToStr(custMap.get("DAILY_INT_CALC_METHOD")).equals("MINIMUM_BALANCE")) {
                            //                            for minumum balance based interest calculation
                            //                            for(int i = 0;i<=totalPeriod;i++){
                            double count = 0.0;
                            HashMap weeklyOff = new HashMap();
                            HashMap holidayMap = new HashMap();
                            lastDay.setDate(noOfDays);
                            weeklyOff.put("NEXT_DATE", setProperDtFormat(lastDay));
                            weeklyOff.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
                            weeklyOff.put("CURR_DATE",setProperDtFormat(lastDay) );
                            boolean week = false;
                            boolean holiday = false;

                            //                                if(i != (totalPeriod)){
                            List lst = ClientUtil.executeQuery("checkHolidayProvisionTD", weeklyOff);
                            if (lst != null && lst.size() > 0) {
                                for (int j = 0; j < lst.size(); j++) {
                                    count = count + 1;
                                }
                                holidayMap.put("NEXT_DATE", setProperDtFormat(lastDay));
                                holidayMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
                                lst = ClientUtil.executeQuery("checkWeeklyOffOD", holidayMap);
                                if (lst != null && lst.size() > 0) {
                                    count = count + 1;
                                }
                            } else {
                                lst = ClientUtil.executeQuery("checkWeeklyOffOD", weeklyOff);
                                if (lst != null && lst.size() > 0) {
                                    count = count + 1;
                                }
                                lst = ClientUtil.executeQuery("checkHolidayProvisionTD", weeklyOff);
                                if (lst != null && lst.size() > 0) {
                                    for (int j = 0; j < lst.size(); j++) {
                                        count = count + 1;
                                    }
                                }
                            }
                            int remainDays = noOfDays - (int) count;
                            lastDay.setDate(remainDays);
                            dayendBalMap.put("TODAY_DT", curDate);
                            if (depositDate.getDate() <= 10) {
                                depositDate.setDate(1);
                                dayendBalMap.put("START_DT", depositDate);
                            } else {
                                dayendBalMap.put("START_DT", depDate);
                            }
                            dayendBalMap.put("PROD_ID", custMap.get("PROD_ID"));
                            dayendBalMap.put("ADD_MONTHS", "-1");
                            dayendBalMap.put("ACT_NUM", getTxtDepositNo());
                            System.out.println("@!#$@#$dayendBalMap:" + dayendBalMap + " :depDate " + depDate);
                            lst = ClientUtil.executeQuery("getDailyBalanceDD", dayendBalMap);
                            int lstSize = lst.size();
                            System.out.println("Amount List:" + lst);
                            for (int i = 0; i < lstSize; i++) {
                                HashMap sumOfAmtMap = new HashMap();
                                sumOfAmtMap = (HashMap) lst.get(i);
                                sumOfAmt += CommonUtil.convertObjToDouble(sumOfAmtMap.get("AMT")).doubleValue();
                                System.out.println("@#$%#$%sumOfAmt:" + sumOfAmt);
                            }

                            interest += (sumOfAmt - transactionAmt) * rateOfInterest / 1200;
                            System.out.println("@#%#$%#$%sumOfAmt:" + sumOfAmt + " :transactionAmt: " + transactionAmt + "  :rateOfInterest:" + rateOfInterest);
                            System.out.println("#@$@#$@#$interest:" + interest);
                        } else {
                            if (depFreq == 7 && weekSpec != null && weekSpec.equals("Y")) {//Added by chithra for mantis :10345: New Weekly Deposit Schemes For Pudukad SCB
                                totalPeriod = totalPeriod - 1;
                                HashMap whrMap = new HashMap();
                                whrMap.put("TRANS_DT ", curDate);
                                whrMap.put("ACT_NUM", getTxtDepositNo());
                                List rsltlst = ClientUtil.executeQuery("getCurrentDateTransactionAmt", whrMap);
                                if (rsltlst != null && rsltlst.size() > 0) {
                                    HashMap sing = (HashMap) rsltlst.get(0);
                                    if (sing != null && sing.containsKey("TOTAL_BALANCE")) {
                                        sumOfAmt += CommonUtil.convertObjToDouble(sing.get("TOTAL_BALANCE"));
                                    }
                                }
                            }
                            for (int i = 0; i <= totalPeriod; i++) {
                                double count = 0.0;
                                HashMap weeklyOff = new HashMap();
                                HashMap holidayMap = new HashMap();
                                lastDay.setDate(noOfDays);
                                weeklyOff.put("NEXT_DATE", setProperDtFormat(lastDay));
                                weeklyOff.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
                                weeklyOff.put("CURR_DATE", setProperDtFormat(lastDay));
                                boolean week = false;
                                boolean holiday = false;
                                if (i != (totalPeriod)) {
                                    List lst = ClientUtil.executeQuery("checkHolidayProvisionTD", weeklyOff);
                                    if (lst != null && lst.size() > 0) {
                                        for (int j = 0; j < lst.size(); j++) {
                                            count = count + 1;
                                        }
                                        holidayMap.put("NEXT_DATE", setProperDtFormat(lastDay));
                                        holidayMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
                                        lst = ClientUtil.executeQuery("checkWeeklyOffOD", holidayMap);
                                        if (lst != null && lst.size() > 0) {
                                            count = count + 1;
                                        }
                                    } else {
                                        lst = ClientUtil.executeQuery("checkWeeklyOffOD", weeklyOff);
                                        if (lst != null && lst.size() > 0) {
                                            count = count + 1;
                                        }
                                        lst = ClientUtil.executeQuery("checkHolidayProvisionTD", weeklyOff);
                                        if (lst != null && lst.size() > 0) {
                                            for (int j = 0; j < lst.size(); j++) {
                                                count = count + 1;
                                            }
                                        }
                                    }
                                    int remainDays = noOfDays - (int) count;
                                    lastDay.setDate(remainDays);
                                    dayendBalMap.put("DAY_END_DT", lastDay);
                                    dayendBalMap.put("ACT_NUM", getTxtDepositNo());
                                    if (depFreq == 7 && weekSpec != null && weekSpec.equals("Y")) {//Added by chithra for mantis :10345: New Weekly Deposit Schemes For Pudukad SCB
                                        lst = ClientUtil.executeQuery("getSelectWeeklyDepositDayEndBal", dayendBalMap);
                                    } else {
                                        lst = ClientUtil.executeQuery("getSelectDailyDepositDayEndBal", dayendBalMap);
                                    }
                                    if (lst != null && lst.size() > 0) {
                                        dayendBalMap = (HashMap) lst.get(0);
                                        sumOfAmt += CommonUtil.convertObjToDouble(dayendBalMap.get("AMT")).doubleValue();
                                        if (lastDay.getMonth() == 1) {
                                            noOfDays = 31;
                                        } else {
                                            if (noOfDays == 31 && (lastDay.getMonth() == 6 || lastDay.getMonth() == 11)) {
                                                noOfDays = 31;
                                            } else if (noOfDays == 31 && lastDay.getMonth() == 0) {
                                                noOfDays = 28;
                                            } else if (noOfDays == 31) {
                                                noOfDays = 30;
                                            } else if (noOfDays == 30) {
                                                noOfDays = 31;
                                            }
                                        }
                                        lastDay = DateUtil.addDays(lastDay, noOfDays);
                                    }
                                }
                                //                        }else{
                                //                            dayendBalMap.put("DAY_END_DT",lastDay);
                                //                            dayendBalMap.put("ACT_NUM",getTxtDepositNo());
                                //                            List lst = ClientUtil.executeQuery("getSelectDailyDepositDayEndBal", dayendBalMap);
                                //                            if(lst!=null && lst.size()>0){
                                //                                dayendBalMap = (HashMap)lst.get(0);
                                //                                double leftAmt = CommonUtil.convertObjToDouble(dayendBalMap.get("AMT")).doubleValue();
                                //                                long differ = DateUtil.dateDiff(lastDay, curDate);
                                //                                interest = leftAmt * differ * rateOfInterest / 36500;
                                //                            }
                                //                        }
                            }
                            //                    double leftAmt = 0.0;
                            //                    long remainingDay = DateUtil.dateDiff((Date)lastDay, (Date)curDate.clone());
                            //                    for(int j=(int)remainingDay;j<=remainingDay;j++){
                            //                        dayendBalMap.put("DAY_END_DT",lastDay);
                            //                        dayendBalMap.put("ACT_NUM",getTxtDepositNo());
                            //                        List lst = ClientUtil.executeQuery("getSelectDailyDepositDayEndBal", dayendBalMap);
                            //                        if(lst!=null && lst.size()>0){
                            //                            dayendBalMap = (HashMap)lst.get(0);
                            //                            leftAmt += CommonUtil.convertObjToDouble(dayendBalMap.get("AMT")).doubleValue();
                            ////                            lastDay = DateUtil.addDays(lastDay, 1);
                            //                        }
                            //                    }
                            //                    double leftdayinterest = leftAmt * remainingDay * rateOfInterest / 36500;
                            
                            
                            interest += (sumOfAmt - transactionAmt) * rateOfInterest / 1200;
                            //by Nidhin
                            /*HashMap intCommMap = new HashMap();
                            intCommMap.put("value", getProdID());
                            List intCommList = ClientUtil.executeQuery("getSelectDepositsProductIntPayTO", intCommMap);
                            DepositsProductIntPayTO depositsProductIntPayTO = (DepositsProductIntPayTO) intCommList.get(0);*/
                            if (behavesMap.containsKey("BEHAVES_LIKE")) {
                                if (behavesMap.get("BEHAVES_LIKE").equals("DAILY")) {
                                    if (depositsProductIntPayTO!=null && depositsProductIntPayTO.getSlabWiseInterest() != null && depositsProductIntPayTO.getSlabWiseInterest().equals("Y")) {
                                        HashMap intMap = new HashMap();
                                        intMap.put("ACNUM", getTxtDepositNo());
                                        intMap.put("ASON", curDate);
                                        List intList = ClientUtil.executeQuery("getSlabWiseInterest", intMap);
                                        System.out.println("intLisDAILYt" + intList + "  intMap  " + intMap);
                                        if (intList != null) {
                                            intMap = (HashMap) intList.get(0);
                                        }
                                        if (intMap != null && intMap.get("INTEREST") != null) {
                                            interest = CommonUtil.convertObjToDouble(intMap.get("INTEREST"));
                                        } else {
                                            interest = CommonUtil.convertObjToDouble(0.0);
                                        }
                                        System.out.println("set to slabwise Interst");
                                        setClosingIntDb(CommonUtil.convertObjToStr(interest));
                                    }
                                    System.out.println("INTERETST" + interest);
                                }
                            }
                            //                    interest += leftdayinterest;
                        }
                    }
                    if (((String) custMap.get("MODE")).equals(CommonConstants.PREMATURE_CLOSURE)) {
                        boolean commisionFlag = false;
                        System.out.println("before commPeriod :" + commPeriod + "totalMonths :" + totalMonths);
                        if (commisionMode != null && commisionMode.equals("Months")) {
                            //                            commPeriod = commPeriod * 30;
                            System.out.println("after commPeriod :" + commPeriod + "totalMonths :" + totalMonths);
                        } else {
                            ClientUtil.showAlertWindow("Product level parameter not properly set");
                            return;
                        }
                        if (commisionMode != null && commisionMode.equals("Months") && totalMonths < commPeriod) {
                            if (totalMonths == commPeriod && diffDay == 0) {
                                commisionFlag = true;
                                interest = 0.0;
                                setRateApplicable(String.valueOf("0.0"));
                                setPrematureClosingRate(String.valueOf("0.0"));
                            } else if (totalMonths == commPeriod && diffDay > 0) {
                                commisionFlag = false;
                            } else if (totalMonths <= commPeriod && diffDay > 0) {
                                commisionFlag = true;
                                interest = 0.0;
                                setRateApplicable(String.valueOf("0.0"));
                                setPrematureClosingRate(String.valueOf("0.0"));
                            }
                        } else if (commisionMode != null && commisionMode.equals("Daily")
                                && (DateUtil.dateDiff((Date) custMap.get("DEPOSIT_DT"), (Date) curDate.clone())) <= commPeriod) {
                            commisionFlag = true;
                            //                    }else if(commisionMode!=null && commisionMode.equals("Years")){
                            //                        commisionFlag = true;
                        }
                        if (interestNotPayingMode != null && interestNotPayingMode.equals("Months") && interestNotPaying < commPeriod) {
                            commisionFlag = false;
                            interest = 0.0;
                            setRateApplicable(String.valueOf("0.0"));
                            setPrematureClosingRate(String.valueOf("0.0"));
                        } else if (commisionMode != null && commisionMode.equals("Daily")
                                && (DateUtil.dateDiff((Date) custMap.get("DEPOSIT_DT"), (Date) curDate.clone())) <= commPeriod) {
                            commisionFlag = false;
                            interest = 0.0;
                            setRateApplicable(String.valueOf("0.0"));
                            setPrematureClosingRate(String.valueOf("0.0"));
                        }
                        /*
                        commented by Rishad this function alway return zero so no use by this portion else it producing nullpointer issue
                        checked by jithesh  0010778: daily deposit premature closer null pointer issue  
                         */
//                        if (commisionFlag == true) {
//                            String agentId = "";
//                            agentId = CommonUtil.convertObjToStr(custMap.get("AGENT_ID"));
//                            agentCommisionDisbursalOB.setDeposit_closing("CLOSING_SCREEN");
//                            agentCommisionDisbursalOB.setDeposit_No(getTxtDepositNo());
//                            agentCommisionDisbursalOB.setAgentId(String.valueOf(agentId));
//                            agentCommisionDisbursalOB.setDepositDate(getDepositDate());
//                            agentCommisionDisbursalOB.setPrematurePeriod(new Double(totalPeriod));
//                            double AgentCommAmt = agentCommisionDisbursalOB.commisionCalculation();
//                            AgentCommAmt = (double) getNearest((long) (AgentCommAmt * 100), 100) / 100;
//                            setAgentCommisionRecoveredValue(String.valueOf(AgentCommAmt));
//                            commisionFlag = false;
//                        }
                        //end Rishad
                    }
                } else {
                    ClientUtil.displayAlert("This Daily Deposit Not Eligible for Closing...");
                    return;
                }
                if (((String) custMap.get("MODE")).equals(CommonConstants.NORMAL_CLOSURE)) {
                    //                    double holidayAmt = 0.0;
                    double holidayAmt = holidayProvision(matDt, amount, interest, rateOfInterest, holidayProv);
                    interest = interest + holidayAmt;
                    if (custMap.get("DEATH_CLAIM") != null && custMap.get("DEATH_CLAIM").equals("Y") && deathFlag == false) {
                        HashMap calculationMap = new HashMap();
                        calculationMap.put("DEP_AMOUNT", custMap.get("DEPOSIT_AMT"));
                        calculationMap.put("CLEAR_BALANCE", custMap.get("CLEAR_BALANCE"));
                        calculationMap.put("MAT_AMOUNT", custMap.get("MATURITY_AMT"));
                        calculationMap.put("CATEGORY_ID", custMap.get("CATEGORY"));
                        calculationMap.put("BEHAVES_LIKE", behavesMap.get("BEHAVES_LIKE"));
                        double deathAmt = 0.0;
                        if(custMap.containsKey("DEATH_CLAIM_INT") && custMap.get("DEATH_CLAIM_INT")!= null && CommonUtil.convertObjToStr(custMap.get("DEATH_CLAIM_INT")).equalsIgnoreCase("Y")){// Added by nithya on 13-08-2019 for KD 583
                            deathAmt = simpleInterestCalculation(calculationMap);
                         }
                        interest = interest + deathAmt;
                        calculationMap = null;
                        setDeathClaim("Y");
                    }
                }
                //            }else{
                //                long Period = DateUtil.dateDiff((Date)depDate,(Date)matDt);
                //                long totPeriod = Period /7;
                //                for(int i = 0;i<totPeriod;i++){
                //                    dayendBalMap.put("DAY_END_DT",depositDate);
                //                    dayendBalMap.put("ACT_NUM",getTxtDepositNo());
                //                    List lst = ClientUtil.executeQuery("getSelectDailyDepositDayEndBal", dayendBalMap);
                //                    if(lst!=null && lst.size()>0){
                //                        dayendBalMap = (HashMap)lst.get(0);
                //                        sumOfAmt += CommonUtil.convertObjToDouble(dayendBalMap.get("AMT")).doubleValue();
                //                    }
                //                    depositDate = DateUtil.addDays(depositDate, 7);
                //                }
                //                depDate = DateUtil.getDateMMDDYYYY(getDepositDate());
                //                depositDate = DateUtil.getDateMMDDYYYY(getDepositDate());
                //                Period = DateUtil.dateDiff((Date)depDate,(Date)(Date)matDt);
                //                long totalPeriod = Period /30;
                //                double transactionAmt = 0.0;
                //                Date lastDay = null;
                //                for(int i = 0;i<totalPeriod;i++){
                //                    int noOfDays = 0;
                //                    weekTransMap.put("ACC_NUM",getTxtDepositNo()+"_1");
                //                    weekTransMap.put("TRN_DT",depositDate);
                //                    if(i == 0){
                //                        GregorianCalendar firstdaymonth = new GregorianCalendar(1,depDate.getMonth(),depDate.getYear()+1900);
                //                        noOfDays = firstdaymonth.getActualMaximum(firstdaymonth.DAY_OF_MONTH);
                //                        lastDay = depDate;
                //                        lastDay.setDate(noOfDays);
                //                        weekTransMap.put("LAST_TRN_DT",lastDay);
                //                    }
                //                    if(i != 0){
                //                        GregorianCalendar firstdaymonth = new GregorianCalendar(1,depositDate.getMonth(),depositDate.getYear()+1900);
                //                        noOfDays = firstdaymonth.getActualMaximum(firstdaymonth.DAY_OF_MONTH);
                //                        GregorianCalendar lastdaymonth = new GregorianCalendar(depositDate.getYear()+1900,depositDate.getMonth(),noOfDays);
                //                        weekTransMap.put("LAST_TRN_DT",lastdaymonth.getTime());
                //                        depDate = lastdaymonth.getTime();
                //                    }
                //                    List lstTrans = ClientUtil.executeQuery("getSelectDailyDepositWeekendTrans", weekTransMap);
                //                    if(lstTrans!=null && lstTrans.size()>0){
                //                        weekTransMap = (HashMap)lstTrans.get(0);
                //                        weekEndAmt = CommonUtil.convertObjToDouble(weekTransMap.get("AMOUNT")).doubleValue();
                //                        weekTransAmt = principal * noOfDays;
                //                        if(weekEndAmt>weekTransAmt){
                //                            transactionAmt += weekEndAmt - weekTransAmt;
                //                        }
                //                    }
                //                    lstTrans = null;
                //                    depositDate = DateUtil.addDays(depDate, 1);
                //                }
                //                interest += (sumOfAmt - transactionAmt) * 7 * rateOfInterest /36500;

            }
            //interest = (double)getNearest((long)(interest *100),100)/100;
            interest = getRound(interest, getInterestRound());
            System.out.println("uncompletedInterest:" + uncompletedInterest + "interest:" + interest);
            System.out.println("jjjjiiiiibbbb");
            this.setClosingIntDb(String.valueOf(interest));
            HashMap cMap = new HashMap();
            cMap.put("PROD_ID",custMap.get("PROD_ID"));
            String rdNature = "";
            List rdApplyList = ClientUtil.executeQuery("getIrregularRDApply", cMap);
            if (rdApplyList != null && rdApplyList.size() > 0) {
                HashMap rdMap = (HashMap) rdApplyList.get(0);
                rdNature = CommonUtil.convertObjToStr(rdMap.get("RD_NATURE"));
            }
            if (rdNature != null && rdNature.equalsIgnoreCase("Y")) {
                double intNtAmt = rd_NatureIntAmt(rdNature);
                interest = intNtAmt;
                if (intNtAmt > 0) {
                    setLblPayRecDet("Payable");
                } else {
                    setLblPayRecDet("Receivable");
                    this.setClosingIntDb("0");
                }
            }
            // Added by nithya on 03-02-2017 Mantis id : 0005664: Daily deposit Interest calculation
            if (dailyIntCalc.equals("INSTALLMENTS")) {// Added by nithya for int calc method installmentwise . Mantis id : 0005664: Daily deposit Interest calculation
                holidayProv = "N";//                        
                System.out.println("Executing installment wise daily acnt calculatiob");
                System.out.println("data from ui :: \n custMap " + custMap + "\nmatDt :: " + matDt + "\namount :: " + amount + "\ninterest::" + interest + "\nrateOfInterest ::" + rateOfInterest);
                String depoStatus = CommonUtil.convertObjToStr(custMap.get("MODE"));
                double dailyIntAmt = dailyInstSlabwiseIntCalc(getTxtDepositNo(), depoStatus);
                setIsSlabWiseDailyDeposit("Y");
                interest = dailyIntAmt;
                if (dailyIntAmt > 0) {
                    setLblPayRecDet("Payable");
                } else {
                    setLblPayRecDet("Receivable");
                    this.setClosingIntDb("0");
                }
            }
            // End
            // Added by nithya on 04-10-2017 for group deposit changes
            boolean isGroupDeposit = false;
            HashMap checkMap = new HashMap();
            checkMap.put("PROD_ID", custMap.get("PROD_ID"));
            List groupDepositProdList = ClientUtil.executeQuery("getIsGroupDepositProduct", checkMap);
            if (groupDepositProdList != null && groupDepositProdList.size() > 0) {
                HashMap groupDepositProdMap = (HashMap) groupDepositProdList.get(0);
                if (groupDepositProdMap != null && groupDepositProdMap.containsKey("IS_GROUP_DEPOSIT") && groupDepositProdMap.get("IS_GROUP_DEPOSIT") != null) {
                    if (CommonUtil.convertObjToStr(groupDepositProdMap.get("IS_GROUP_DEPOSIT")).equalsIgnoreCase("Y")) {
                        isGroupDeposit = true;
                        String depoStatus = CommonUtil.convertObjToStr(custMap.get("MODE"));
                        double groupDepositIntAmt = dailyGroupDepositIntCalc(getTxtDepositNo(), depoStatus);                                                
                        if (groupDepositIntAmt > 0) {
                            interest = groupDepositIntAmt;
                            setLblPayRecDet("Payable");
                        } else {
                           // setLblPayRecDet("Receivable");
                            this.setClosingIntDb("0");
                            System.out.println("setIntCr :: "+ getIntCr());
                            setIntCr("0");
                           // setAgentCommisionRecoveredValue(String.valueOf(groupDepositIntAmt));
                        }
                    }
                }
            }
            
            
            // End
            System.out.println("this.setClosingIntDb : " + this.getClosingIntDb());
            double intAmt = CommonUtil.convertObjToDouble(custMap.get("TOTAL_INT_DRAWN")).doubleValue();
            //  intAmt = (double)getNearest((long)(intAmt *100),100)/100;
            intAmt = getRound(intAmt, getInterestRound());
            double amt = CommonUtil.convertObjToDouble(getBalance()).doubleValue() + intAmt;
            amt = (double) getNearest((long) (amt * 100), 100) / 100;
            this.setIntDrawn(getIntDrawn());
            System.out.println("setBalance : " + getBalanceAmt());

            this.setClosingDisbursal(CommonUtil.convertObjToStr(new Double(
                    CommonUtil.convertObjToDouble(getBalance()).doubleValue() + interest - intAmt)));
            setDisburseAmt(CommonUtil.convertObjToDouble(this.getClosingDisbursal()).doubleValue());
            System.out.println("#########payRecValue behavesLikeMap else :" + this.getDisburseAmt());
            if (getDisburseAmt() > amt) {
                setLblPayRecDet("Payable");
                this.setPayReceivable(
                        CommonUtil.convertObjToStr(new Double(
                        CommonUtil.convertObjToDouble(getClosingIntDb()).doubleValue()
                        - CommonUtil.convertObjToDouble(getIntDrawn()).doubleValue())));
                this.setPermanentPayReceivable(
                        CommonUtil.convertObjToStr(new Double(
                        CommonUtil.convertObjToDouble(getClosingIntDb()).doubleValue()
                        - CommonUtil.convertObjToDouble(getIntDrawn()).doubleValue())));
                System.out.println("#########payRecValue :" + this.getPayReceivable());
            } else {
                setLblPayRecDet("Receivable");
                this.setPayReceivable(
                        CommonUtil.convertObjToStr(new Double(
                        CommonUtil.convertObjToDouble(getIntCr()).doubleValue()
                        - CommonUtil.convertObjToDouble(getClosingIntDb()).doubleValue())));
                this.setPermanentPayReceivable(
                        CommonUtil.convertObjToStr(new Double(
                        CommonUtil.convertObjToDouble(getIntCr()).doubleValue()
                        - CommonUtil.convertObjToDouble(getClosingIntDb()).doubleValue())));
                System.out.println("#########payRecValue else :" + this.getPayReceivable());// i have to change
            }
            double negValue = CommonUtil.convertObjToDouble(getPayReceivable()).doubleValue();
            if (negValue < 0) {
                negValue = negValue * -1;
                this.setPayReceivable(String.valueOf(negValue));
                this.setPermanentPayReceivable(String.valueOf(negValue));
            }
            //Added By Suresh
            if (custMap.get("BEHAVES_LIKE").equals("DAILY")) {
                if (serviceChargeMap == null) {
                    serviceChargeMap = new HashMap();
                }
                if (sumOfAmt > 0) {
                    serviceChargeMap.put("TOTAL_DEP_DAY_END_AMT", String.valueOf(sumOfAmt));
                    serviceChargeMap.put("DAILY_INT_CALC", dailyIntCalc);
                }
            }
           if (dailyIntCalc.equals("INSTALLMENTS")) {
                setIntCr("0");
            }
           if(isGroupDeposit){
             //   setIntCr("0");
           }
            weekTransMap = null;
            dayendBalMap = null;
            customerMap = null;
        }
    }

    protected HashMap getSlabWiseCommision() {
        System.out.println("here getSlabWiseCommision");
        HashMap commMap = null;
        HashMap behavesLikeMap = new HashMap();
        behavesLikeMap.put("PROD_ID", getProdID());
        List lst = ClientUtil.executeQuery("getBehavesLikeForDeposit", behavesLikeMap);
        if (lst != null && lst.size() > 0) {
            behavesLikeMap = (HashMap) lst.get(0);
        }
        HashMap mapIntComm = new HashMap();
        mapIntComm.put("value", getProdID());
        List intCommList = ClientUtil.executeQuery("getSelectDepositsProductIntPayTO", mapIntComm);
        DepositsProductIntPayTO depositsProductIntPayTO = (DepositsProductIntPayTO) intCommList.get(0);
        System.out.println("Here depositsProductIntPayTO" + depositsProductIntPayTO);
        if (behavesLikeMap.containsKey("BEHAVES_LIKE")) {
            if (behavesLikeMap != null && behavesLikeMap.get("BEHAVES_LIKE").equals("DAILY")) {
                if (depositsProductIntPayTO.getSlabWiseCommision() != null && depositsProductIntPayTO.getSlabWiseCommision().equals("Y")) {
                    commMap = new HashMap();
                    commMap.put("ACNUM", getTxtDepositNo());
                    commMap.put("ASON", curDate.clone());
                    List commList = ClientUtil.executeQuery("getSlabWiseCommision", commMap);
                    System.out.println("getSlabWiseCommision()" + commList);
                    if (commList != null) {
                        commMap = (HashMap) commList.get(0);
                    }
                }
            }
        }
        return commMap;
    }

    private void setClosureTO(SubDepositTO obj) throws Exception {
        HashMap whereMap = new HashMap();
        whereMap.put("DEPOSITNO", obj.getDepositNo());
        whereMap.put("DEPOSITSUBNO", obj.getDepositSubNo());
        whereMap.put("DEPOSIT_DT", obj.getDepositDt());
        whereMap.put("MATURITY_DT", obj.getMaturityDt());

//        whereMap.put(CommonConstants.PRODUCT_ID,CommonUtil.convertObjToStr(getCbmProductId().getKeyForSelected()));
        System.out.println("setClosureTo :" + whereMap);
        HashMap mapData = proxy.executeQuery(whereMap, map);
    }

    public HashMap agentsCommisionAmt(HashMap custMap) {
        return custMap;
    }

    private double getTdsPayable() {
        double tdsAmt = 0;
        //EG : List list = ClientUtil.executeQuery("getSelectDepositWithDrawalTO",whereMap);
        // Calculate TDS if applicable
        // Insert into TDS table

        return tdsAmt;
    }

    private void resetPresentTO() {
        this.setIntCr("");
        this.setIntDrawn("");
        this.setLastIntAppDate("");
        this.setBalance("");
        this.setLienFreezeAmt("");
        this.setNoInstDue("");
        this.setNoInstPaid("");
        this.setTdsCollected("");
        this.setWithDrawn("");
    }

    private void resetClosureTO() {
        this.setClosingDisbursal("");
        this.setClosingIntCr("");
        this.setClosingIntDb("");
        this.setClosingTds("");
        this.setPayReceivable("");
        this.setPermanentPayReceivable("");
        this.setPenaltyPenalRate("");
        this.setRateApplicable("");
        this.setDelayedInstallments("");
        this.setChargeAmount("");
        this.setAgentCommisionRecoveredValue("");
        this.setRdoPenaltyPenalRate_no(false);
        this.setRdoPenaltyPenalRate_yes(false);
        this.setRdoTypeOfDeposit_No(false);
        this.setRdoTypeOfDeposit_Yes(false);
        this.setRdoTransfer_No(false);
        this.setRdoTransfer_Yes(false);
		//added by chithra 22/04/2014
       this.setLblAddIntRtAmtVal("");
       this.setLblAddIntrstRteVal("");
       this.setLblMaturityPeriod("");
       this.setAddIntLoanAmt("");
       this.setServiceTax_Map(null);
       this.setLblServiceTaxval("");
       this.setLblAddIntRtAmtVal(null);
       this.setPrev_interest("");
       this.setLblPayRecDet("");
       this.setPayReceivable("");
    }

    /** Getter for property tbmSubDeposit.
     * @return Value of property tbmSubDeposit.
     *
     */
    public TableModel getTbmSubDeposit() {
        return tbmSubDeposit;
    }

    /** Setter for property tbmSubDeposit.
     * @param tbmSubDeposit New value of property tbmSubDeposit.
     *
     */
    public void setTbmSubDeposit(TableModel tbmSubDeposit) {
        this.tbmSubDeposit = tbmSubDeposit;
    }

    /** Getter for property constitution.
     * @return Value of property constitution.
     *
     */
    public java.lang.String getConstitution() {
        return constitution;
    }

    /** Setter for property constitution.
     * @param constitution New value of property constitution.
     *
     */
    public void setConstitution(java.lang.String constitution) {
        this.constitution = constitution;
    }

    /** Getter for property category.
     * @return Value of property category.
     *
     */
    public java.lang.String getCategory() {
        return category;
    }

    /** Setter for property category.
     * @param category New value of property category.
     *
     */
    public void setCategory(java.lang.String category) {
        this.category = category;
    }

    /** Getter for property maturityDate.
     * @return Value of property maturityDate.
     *
     */
    public java.lang.String getMaturityDate() {
        return maturityDate;
    }

    /** Setter for property maturityDate.
     * @param maturityDate New value of property maturityDate.
     *
     */
    public void setMaturityDate(java.lang.String maturityDate) {
        this.maturityDate = maturityDate;
    }

    /** Getter for property maturityValue.
     * @return Value of property maturityValue.
     *
     */
    public java.lang.String getMaturityValue() {
        return maturityValue;
    }

    /** Setter for property maturityValue.
     * @param maturityValue New value of property maturityValue.
     *
     */
    public void setMaturityValue(java.lang.String maturityValue) {
        this.maturityValue = maturityValue;
    }

    /** Getter for property period.
     * @return Value of property period.
     *
     */
    public java.lang.String getPeriod() {
        return period;
    }

    /** Setter for property period.
     * @param period New value of property period.
     *
     */
    public void setPeriod(java.lang.String period) {
        this.period = period;
    }

    /** Getter for property prinicipal.
     * @return Value of property prinicipal.
     *
     */
    public java.lang.String getPrinicipal() {
        return prinicipal;
    }

    /** Setter for property prinicipal.
     * @param prinicipal New value of property prinicipal.
     *
     */
    public void setPrinicipal(java.lang.String prinicipal) {
        this.prinicipal = prinicipal;
    }

    /** Getter for property rateOfInterest.
     * @return Value of property rateOfInterest.
     *
     */
    public java.lang.String getRateOfInterest() {
        return rateOfInterest;
    }

    /** Setter for property rateOfInterest.
     * @param rateOfInterest New value of property rateOfInterest.
     *
     */
    public void setRateOfInterest(java.lang.String rateOfInterest) {
        this.rateOfInterest = rateOfInterest;
        setChanged();
    }

    /** Getter for property intPaymentFreq.
     * @return Value of property intPaymentFreq.
     *
     */
    public java.lang.String getIntPaymentFreq() {
        return intPaymentFreq;
    }

    /** Setter for property intPaymentFreq.
     * @param intPaymentFreq New value of property intPaymentFreq.
     *
     *
     */
    public void setIntPaymentFreq(java.lang.String intPaymentFreq) {
        this.intPaymentFreq = intPaymentFreq;
    }

    /** Getter for property depositDate.
     * @return Value of property depositDate.
     *
     */
    public java.lang.String getDepositDate() {
        return depositDate;
    }

    /** Setter for property depositDate.
     * @param depositDate New value of property depositDate.
     *
     */
    public void setDepositDate(java.lang.String depositDate) {
        this.depositDate = depositDate;
    }

    /** Getter for property balance.
     * @return Value of property balance.
     *
     */
    public java.lang.String getBalance() {
        return balance;
    }

    /** Setter for property balance.
     * @param balance New value of property balance.
     *
     */
    public void setBalance(java.lang.String balance) {
        this.balance = balance;
    }

    /** Getter for property closingTds.
     * @return Value of property closingTds.
     *
     */
    public java.lang.String getClosingTds() {
        return closingTds;
    }

    /** Setter for property closingTds.
     * @param closingTds New value of property closingTds.
     *
     */
    public void setClosingTds(java.lang.String closingTds) {
        this.closingTds = closingTds;
    }

    /** Getter for property closingIntCr.
     * @return Value of property closingIntCr.
     *
     */
    public java.lang.String getClosingIntCr() {
        return closingIntCr;
    }

    /** Setter for property closingIntCr.
     * @param closingIntCr New value of property closingIntCr.
     *
     */
    public void setClosingIntCr(java.lang.String closingIntCr) {
        this.closingIntCr = closingIntCr;
        setChanged();
    }

    /** Getter for property balanceDeposit.
     * @return Value of property balanceDeposit.
     *
     */
    public java.lang.String getBalanceDeposit() {
        return balanceDeposit;
    }

    /** Setter for property balanceDeposit.
     * @param balanceDeposit New value of property balanceDeposit.
     *
     */
    public void setBalanceDeposit(java.lang.String balanceDeposit) {
        this.balanceDeposit = balanceDeposit;
    }

    /** Getter for property closingIntDb.
     * @return Value of property closingIntDb.
     *
     */
    public java.lang.String getClosingIntDb() {
        return closingIntDb;
    }

    /** Setter for property closingIntDb.
     * @param closingIntDb New value of property closingIntDb.
     *
     */
    public void setClosingIntDb(java.lang.String closingIntDb) {
        this.closingIntDb = closingIntDb;
    }

    /** Getter for property closingDisbursal.
     * @return Value of property closingDisbursal.
     *
     */
    public java.lang.String getClosingDisbursal() {
        return closingDisbursal;
    }

    /** Setter for property closingDisbursal.
     * @param closingDisbursal New value of property closingDisbursal.
     *
     */
    public void setClosingDisbursal(java.lang.String closingDisbursal) {
        this.closingDisbursal = closingDisbursal;
    }

    /** Getter for property intCr.
     * @return Value of property intCr.
     *
     */
    public java.lang.String getIntCr() {
        return intCr;
    }

    /** Setter for property intCr.
     * @param intCr New value of property intCr.
     *
     */
    public void setIntCr(java.lang.String intCr) {
        this.intCr = intCr;
    }

    /** Getter for property intDrawn.
     * @return Value of property intDrawn.
     *
     */
    public java.lang.String getIntDrawn() {
        return intDrawn;
    }

    /** Setter for property intDrawn.
     * @param intDrawn New value of property intDrawn.
     *
     */
    public void setIntDrawn(java.lang.String intDrawn) {
        this.intDrawn = intDrawn;
    }

    /** Getter for property lastIntAppDate.
     * @return Value of property lastIntAppDate.
     *
     */
    public java.lang.String getLastIntAppDate() {
        return lastIntAppDate;
    }

    /** Setter for property lastIntAppDate.
     * @param lastIntAppDate New value of property lastIntAppDate.
     *
     */
    public void setLastIntAppDate(java.lang.String lastIntAppDate) {
        this.lastIntAppDate = lastIntAppDate;
    }

    /** Getter for property lienFreezeAmt.
     * @return Value of property lienFreezeAmt.
     *
     */
    public java.lang.String getLienFreezeAmt() {
        return lienFreezeAmt;
    }

    /** Setter for property lienFreezeAmt.
     * @param lienFreezeAmt New value of property lienFreezeAmt.
     *
     */
    public void setLienFreezeAmt(java.lang.String lienFreezeAmt) {
        this.lienFreezeAmt = lienFreezeAmt;
    }

    /** Getter for property noInstDue.
     * @return Value of property noInstDue.
     *
     */
    public java.lang.String getNoInstDue() {
        return noInstDue;
    }

    /** Setter for property noInstDue.
     * @param noInstDue New value of property noInstDue.
     *
     */
    public void setNoInstDue(java.lang.String noInstDue) {
        this.noInstDue = noInstDue;
    }

    /** Getter for property noInstPaid.
     * @return Value of property noInstPaid.
     *
     */
    public java.lang.String getNoInstPaid() {
        return noInstPaid;
    }

    /** Setter for property noInstPaid.
     * @param noInstPaid New value of property noInstPaid.
     *
     */
    public void setNoInstPaid(java.lang.String noInstPaid) {
        this.noInstPaid = noInstPaid;
    }

    /** Getter for property payReceivable.
     * @return Value of property payReceivable.
     *
     */
    public java.lang.String getPayReceivable() {
        return payReceivable;
    }

    /** Setter for property payReceivable.
     * @param payReceivable New value of property payReceivable.
     *
     */
    public void setPayReceivable(java.lang.String payReceivable) {
        this.payReceivable = payReceivable;
    }

    /** Getter for property tdsCollected.
     * @return Value of property tdsCollected.
     *
     */
    public java.lang.String getTdsCollected() {
        return tdsCollected;
    }

    /** Setter for property tdsCollected.
     * @param tdsCollected New value of property tdsCollected.
     *
     */
    public void setTdsCollected(java.lang.String tdsCollected) {
        this.tdsCollected = tdsCollected;
    }

    /** Getter for property withDrawn.
     * @return Value of property withDrawn.
     *
     */
    public java.lang.String getWithDrawn() {
        return withDrawn;
    }

    /** Setter for property withDrawn.
     * @param withDrawn New value of property withDrawn.
     *
     */
    public void setWithDrawn(java.lang.String withDrawn) {
        this.withDrawn = withDrawn;
    }

    /** Getter for property actionType.
     * @return Value of property actionType.
     *
     */
    public int getActionType() {
        return actionType;
    }

    /** Setter for property actionType.
     * @param actionType New value of property actionType.
     *
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    /** Getter for property lblStatus.
     * @return Value of property lblStatus.
     *
     */
    public java.lang.String getLblStatus() {
        return lblStatus;
    }

    /** Setter for property lblStatus.
     * @param lblStatus New value of property lblStatus.
     *
     */
    public void setLblStatus(java.lang.String lblStatus) {
        this.lblStatus = lblStatus;
    }

    /** Getter for property result.
     * @return Value of property result.
     *
     */
    public int getResult() {
        return result;
    }

    /** Setter for property result.
     * @param result New value of property result.
     *
     */
    public void setResult(int result) {
        this.result = result;
    }

    public void setStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[this.getActionType()]);
        setChanged();
        notifyObservers();
    }

    public void setResultStatus() {
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        setChanged();
        notifyObservers();
    }

    public void resetForm() {
        this.subDepositTOs.clear();
        this.withdrawalTOs.clear();

        this.cbmProductId.setKeyForSelected("");
        this.tbmSubDeposit.setData(new ArrayList());
        this.tbmSubDeposit.fireTableDataChanged();

        this.cboProductId = "";
        lblClosingType = "";
        this.txtDepositNo = "";
        this.setPenaltyPenalRate = "";
        setActualPeriodRun("");
        resetDetails();
        resetDepositDetails();
        resetCustomerDetails();
        resetPartilalWithDrawal();
        transactionDetailsTO = null;
        deletedTransactionDetailsTO = null;
        allowedTransactionDetailsTO = null;
        oldTransactionMap=null;
        oldTransactionMap=new HashMap();
        setAuthrize2("");
        setBothRecPayMap(null);
        setTotalCharge(0);
        setChargelst(null);
        setChanged();
        notifyObservers();
    }

    /** Getter for property lblClosingType.
     * @return Value of property lblClosingType.
     *
     */
    public java.lang.String getLblClosingType() {
        return lblClosingType;
    }

    /** Setter for property lblClosingType.
     * @param lblClosingType New value of property lblClosingType.
     *
     */
    public void setLblClosingType(java.lang.String lblClosingType) {
        System.out.println("lblClosingType============= :"+lblClosingType);
        this.lblClosingType = lblClosingType;
    }

    /** Getter for property customerID.
     * @return Value of property customerID.
     *
     */
    public java.lang.String getCustomerID() {
        return customerID;
    }

    /** Setter for property customerID.
     * @param customerID New value of property customerID.
     *
     */
    public void setCustomerID(java.lang.String customerID) {
        this.customerID = customerID;
    }

    /** Getter for property unitAmt.
     * @return Value of property unitAmt.
     *
     */
    public double getUnitAmt() {
        return unitAmt;
    }

    /** Setter for property unitAmt.
     * @param unitAmt New value of property unitAmt.
     *
     */
    public void setUnitAmt(double unitAmt) {
        this.unitAmt = unitAmt;
    }

    /** Getter for property partialAllowed.
     * @return Value of property partialAllowed.
     *
     */
    public java.lang.String getPartialAllowed() {
        return partialAllowed;
    }

    /** Setter for property partialAllowed.
     * @param partialAllowed New value of property partialAllowed.
     *
     */
    public void setPartialAllowed(java.lang.String partialAllowed) {
        this.partialAllowed = partialAllowed;
    }

    /** Getter for property subDepositNo.
     * @return Value of property subDepositNo.
     *
     */
    public java.lang.String getSubDepositNo() {
        return subDepositNo;
    }

    /** Setter for property subDepositNo.
     * @param subDepositNo New value of property subDepositNo.
     *
     */
    public void setSubDepositNo(java.lang.String subDepositNo) {
        this.subDepositNo = subDepositNo;
    }

    /** Getter for property noOfUnitsWithDrawn.
     * @return Value of property noOfUnitsWithDrawn.
     *
     */
    public java.lang.String getNoOfUnitsWithDrawn() {
        return noOfUnitsWithDrawn;
    }

    /** Setter for property noOfUnitsWithDrawn.
     * @param noOfUnitsWithDrawn New value of property noOfUnitsWithDrawn.
     *
     */
    public void setNoOfUnitsWithDrawn(java.lang.String noOfUnitsWithDrawn) {
        this.noOfUnitsWithDrawn = noOfUnitsWithDrawn;
    }

    /** Getter for property depositRunPeriod.
     * @return Value of property depositRunPeriod.
     *
     */
    public java.lang.String getDepositRunPeriod() {
        return depositRunPeriod;
    }

    /** Setter for property depositRunPeriod.
     * @param depositRunPeriod New value of property depositRunPeriod.
     *
     */
    public void setDepositRunPeriod(java.lang.String depositRunPeriod) {
        this.depositRunPeriod = depositRunPeriod;
    }

    /** Getter for property noOfUnitsAvai.
     * @return Value of property noOfUnitsAvai.
     *
     */
    public java.lang.String getNoOfUnitsAvai() {
        return noOfUnitsAvai;
    }

    /** Setter for property noOfUnitsAvai.
     * @param noOfUnitsAvai New value of property noOfUnitsAvai.
     *
     */
    public void setNoOfUnitsAvai(java.lang.String noOfUnitsAvai) {
        this.noOfUnitsAvai = noOfUnitsAvai;
    }

    /** Getter for property prematureClosingDate.
     * @return Value of property prematureClosingDate.
     *
     */
    public java.lang.String getPrematureClosingDate() {
        return prematureClosingDate;
    }

    /** Setter for property prematureClosingDate.
     * @param prematureClosingDate New value of property prematureClosingDate.
     *
     */
    public void setPrematureClosingDate(java.lang.String prematureClosingDate) {
        this.prematureClosingDate = prematureClosingDate;
        setChanged();
    }

    /** Getter for property prematureClosingRate.
     * @return Value of property prematureClosingRate.
     *
     */
    public java.lang.String getPrematureClosingRate() {
        return prematureClosingRate;
    }

    /** Setter for property prematureClosingRate.
     * @param prematureClosingRate New value of property prematureClosingRate.
     *
     */
    public void setPrematureClosingRate(java.lang.String prematureClosingRate) {
        this.prematureClosingRate = prematureClosingRate;
        setChanged();
    }

    /** Getter for property txtAmtWithDrawn.
     * @return Value of property txtAmtWithDrawn.
     *
     */
    public java.lang.String getTxtAmtWithDrawn() {
        return txtAmtWithDrawn;
    }

    /** Setter for property txtAmtWithDrawn.
     * @param txtAmtWithDrawn New value of property txtAmtWithDrawn.
     *
     */
    public void setTxtAmtWithDrawn(java.lang.String txtAmtWithDrawn) {
        this.txtAmtWithDrawn = txtAmtWithDrawn;
    }

    /** Getter for property presentUnitInt.
     * @return Value of property presentUnitInt.
     *
     */
    public java.lang.String getPresentUnitInt() {
        return presentUnitInt;
    }

    /** Setter for property presentUnitInt.
     * @param presentUnitInt New value of property presentUnitInt.
     *
     */
    public void setPresentUnitInt(java.lang.String presentUnitInt) {
        this.presentUnitInt = presentUnitInt;
    }

    /** Getter for property settlementUnitInt.
     * @return Value of property settlementUnitInt.
     *
     */
    public java.lang.String getSettlementUnitInt() {
        return settlementUnitInt;
    }

    /** Setter for property settlementUnitInt.
     * @param settlementUnitInt New value of property settlementUnitInt.
     *
     */
    public void setSettlementUnitInt(java.lang.String settlementUnitInt) {
        this.settlementUnitInt = settlementUnitInt;
    }

    /** Getter for property noOfUnits.
     * @return Value of property noOfUnits.
     *
     */
    public java.lang.String getNoOfUnits() {
        return noOfUnits;
    }

    /** Setter for property noOfUnits.
     * @param noOfUnits New value of property noOfUnits.
     *
     */
    public void setNoOfUnits(java.lang.String noOfUnits) {
        this.noOfUnits = noOfUnits;
    }

    /** Getter for property prodID.
     * @return Value of property prodID.
     *
     */
    public java.lang.String getProdID() {
        return prodID;
    }

    /** Setter for property prodID.
     * @param prodID New value of property prodID.
     *
     */
    public void setProdID(java.lang.String prodID) {
        this.prodID = prodID;
    }

    /** Getter for property noOfWithDrawalUnits.
     * @return Value of property noOfWithDrawalUnits.
     *
     */
    public java.lang.String getNoOfWithDrawalUnits() {
        return noOfWithDrawalUnits;
    }

    /** Setter for property noOfWithDrawalUnits.
     * @param noOfWithDrawalUnits New value of property noOfWithDrawalUnits.
     *
     */
    public void setNoOfWithDrawalUnits(java.lang.String noOfWithDrawalUnits) {
        this.noOfWithDrawalUnits = noOfWithDrawalUnits;
    }
    
     private double getTotalClosingCharge() {// added by nithya on 29-04-2019 for KD 446 - 6086 -RD Lien LTD Closing-AUTHORIZATION ISSUE.
        double totalcharge = 0.0;
        List depositchargeLst = new ArrayList();
         System.out.println("getChargelst() :: " + getChargelst());
        if (getChargelst() != null && getChargelst().size() > 0) {
            depositchargeLst = getChargelst();
            if (depositchargeLst != null && depositchargeLst.size() > 0) {
                for (int i = 0; i < depositchargeLst.size(); i++) {
                    double depositClosingCharge = 0.0;
                    HashMap chargeMap = new HashMap();
                    String accHead = "";
                    double chargeAmt = 0;
                    String chargeType = "";
                    chargeMap = (HashMap) depositchargeLst.get(i);
                    if (chargeMap.containsKey("CHARGE_AMOUNT") && chargeMap.get("CHARGE_AMOUNT") != null) {
                        depositClosingCharge = CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMOUNT"));
                        totalcharge += depositClosingCharge;
                    }
                }
            }
        }
         System.out.println("getServiceTax_Map()... " + getServiceTax_Map());
        if(getServiceTax_Map()!=null && getServiceTax_Map().size() > 0
                    && CommonUtil.convertObjToDouble(getServiceTax_Map().get("TOT_TAX_AMT"))>0){
          System.out.println("Total Service tax :: " + CommonUtil.convertObjToDouble(getServiceTax_Map().get("TOT_TAX_AMT")));  
          totalcharge = totalcharge + CommonUtil.convertObjToDouble(getServiceTax_Map().get("TOT_TAX_AMT"));  
        }
        return totalcharge;
    }

    public void doAction() {
        try {
            if (getTxtDepositNo() != null && getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                HashMap whereMap = new HashMap();
                whereMap.put("ACCOUNTNO", getTxtDepositNo());
                List lst = ClientUtil.executeQuery("getDepositAuthorizeStatus", whereMap);
                for (int i = 0; i < lst.size(); i++) {
                    whereMap = (HashMap) lst.get(0);
                    String authStatus = CommonUtil.convertObjToStr(whereMap.get("AUTHORIZE_STATUS"));
                    String authBy = CommonUtil.convertObjToStr(whereMap.get("AUTHORIZE_USER"));
                    if (!authStatus.equals("")) {
                        actionType = ClientConstants.ACTIONTYPE_FAILED;
                        throw new TTException("This transaction already " + " " + authStatus.toLowerCase() + " " + "by" + authBy);
                    }
                    System.out.println("Authorized..." + authBy);
                }
            }
            if (actionType != ClientConstants.ACTIONTYPE_FAILED) {
                HashMap objMap = new HashMap();
                objMap.put("MODE", getLblClosingType());

                objMap.put("COMMAND", getCommand());
                System.out.println("Customer id" + getCustomerID());
                if (((String) objMap.get("MODE")).equals(CommonConstants.NORMAL_CLOSURE)
                        || ((String) objMap.get("MODE")).equals(CommonConstants.PREMATURE_CLOSURE)
                        || ((String) objMap.get("MODE")).equals(CommonConstants.TRANSFER_OUT_CLOSURE)) {
                    HashMap closureData = new HashMap();
                    closureData.put("DEPOSIT_NO", getTxtDepositNo());
                    closureData.put("DEPOSIT_SUB_NO", CommonUtil.convertObjToInt(getSubDepositNo()));
                    System.out.println("getClosingIntCr()=====" + getClosingIntCr());
                    closureData.put("CR_INTEREST", getClosingIntCr());
                    System.out.println("getClosingIntDb()======" + getClosingIntDb());
                    closureData.put("DR_INTEREST", getClosingIntDb());
                    System.out.println("getClosingIntDb()===" + getClosingIntDb());
                    closureData.put("INTEREST_DRAWN", getIntDrawn());
                    System.out.println("getIntCr()======" + getIntCr());
                    closureData.put("PAID_INTEREST", getIntCr());
                    closureData.put("TDS_SHARE", getClosingTds());
                    closureData.put("TDS_ACHD", getTdsAcHd());
                    closureData.put("PROD_ID", getProdID());
                    closureData.put("INT_DISPLAY", getLblReceive());
                    closureData.put("PAY_AMT", getPayReceivable());
                    closureData.put("LIEN", new Double(getLienAmount())); //Get this from the common screen
                    closureData.put("BEHAVES_LIKE", behavesMap.get("BEHAVES_LIKE"));
                    closureData.put("DEPOSIT_AMT", getBalanceDeposit());
                    closureData.put("ROI", getRateApplicable());
                    System.out.println("getPenaltyPenalRate()====" + getPenaltyPenalRate());
                    closureData.put("PENAL_INT", getPenaltyPenalRate());
                    closureData.put("CUST_ID", getCustomerID());
                    closureData.put("DEPOSIT_DT", getDepositDate());
                    closureData.put("MODE", getLblClosingType());
                    System.out.println("getPenaltyInt()===" + getPenaltyInt());
                    closureData.put("PENALTY_INT", getPenaltyInt());
                    closureData.put("LIEN_STATUS", getTransStatus());
                    closureData.put("TRANS_PROD_ID", getTransProdId());
                    closureData.put("CLEAR_BALANCE", getBalance());
                    closureData.put("CURR_RATE_OF_INT", getPrematureClosingRate());
                    closureData.put("PENAL_RATE", getPenaltyPenalRate());
                    System.out.println("getChargeAmount()====" + getChargeAmount());
                    closureData.put("DELAYED_AMOUNT", getChargeAmount());  // Added by nithya on 18-09-2019 for KD 570 RD Closure Needs Delayed Amount Calculation. 
                    //closureData.put("DELAYED_AMOUNT", "0");
                    System.out.println("getPrev_interest====" + getPrev_interest());
                    double d = 0.0;
                    if (!getPrev_interest().equals("")) {
                        d = Double.parseDouble(getPrev_interest());
                        System.out.println("dddd" + d);

                    }
                    closureData.put("PREV_INTEREST_AMT", d);
                    closureData.put("INTEREST_AMT", new Double(getPayReceivable()));
                    closureData.put("TOTAL_AMT", getClosingDisbursal());
                    closureData.put("LAST_INT_APPL_DT", getLastIntAppDate());
                    closureData.put("TOTAL_BALANCE", getTotalBalance());
                    closureData.put("TYPES_OF_DEPOSIT", getTypeOfDep());
                    closureData.put("AGENT_COMMISION_AMT", getAgentCommisionRecoveredValue());
                    closureData.put("TRANSFER_OUT_MODE", transfer_out_mode);
                    closureData.put("TRANSFER_OUT_BRANCH_CODE", getTransferBranch_code());
                    
                    if (oldTransactionMap != null && oldTransactionMap.size() > 0) {
                        closureData.put("OLDTRANSACTION", oldTransactionMap);
                    }
                    if (oldTransMap.containsKey("TRANS_DETAILS")) {
                        closureData.put("TRANS_DETAILS", oldTransMap);
                    }
                    if (getLtdClosingMap() != null && getLtdClosingMap().size() > 0) {
                        closureData.put("LTDCLOSINGDATA", getLtdClosingMap());
                    }
                    if (getLtdDeposit().equals("true")) {
                        objMap.put("LTD", "LTD");
                    }
                    if (getChargelst() != null) {
                        closureData.put("Charge List Data", getChargelst());
                        if(getServiceTax_Map()!=null && getServiceTax_Map().size() > 0
                    && CommonUtil.convertObjToDouble(getServiceTax_Map().get("TOT_TAX_AMT"))>0){
                            closureData.put("serviceTax_Details", getServiceTax_Map()); 
                            serviceTax_Map.put("act_num", getTxtDepositNo());
                            closureData.put("serviceTax_DetailsTo", setServiceTaxDetails(serviceTax_Map)); 
                        }
                    }else if(CommonUtil.convertObjToDouble((getAgentCommisionRecoveredValue())) > 0){// Addded by nithya on 16-06-2018 for calculating ServiceTax/GST implementation for charges and agent commission
                        if(getServiceTax_Map()!=null && getServiceTax_Map().size() > 0
                    && CommonUtil.convertObjToDouble(getServiceTax_Map().get("TOT_TAX_AMT"))>0){
                            closureData.put("serviceTax_Details", getServiceTax_Map()); 
                            serviceTax_Map.put("act_num", getTxtDepositNo());
                            closureData.put("serviceTax_DetailsTo", setServiceTaxDetails(serviceTax_Map)); 
                        }
                    }
                    if(getTransStatus().equalsIgnoreCase("LIEN") && behavesMap.get("BEHAVES_LIKE").equals("RECURRING")){// added by nithya on 29-04-2019 for KD 446 - 6086 -RD Lien LTD Closing-AUTHORIZATION ISSUE.
                        double depositClosingCharge = getTotalClosingCharge();
                        System.out.println("depositClosingCharge... " + depositClosingCharge);
                        double finalDepoAmt = CommonUtil.convertObjToDouble(getTotalBalance()) - depositClosingCharge ;
                        closureData.put("DEPOSIT_AMT", finalDepoAmt);                        
                    }
                    //Added By Suresh
                    if (serviceChargeMap != null && serviceChargeMap.size() > 0) {
                        closureData.put("SERVICE_CHARGE_DETAILS", getServiceChargeMap());
                        serviceChargeMap = null;
                    }

                    closureData.put("ADD_INT_AMOUNT", getLblAddIntRtAmtVal());//added by chithra on 16-05-14 For additional int amt

                    closureData.put("ADD_LOAN_INT_AMOUNT", getAddIntLoanAmt());//added by chithra on 16-05-14 For additional Loan int amt
                    closureData.put("ADD_INT_DAYS", getLblMaturityPeriod());
                    closureData.put("ADD_INT_RATE", getLblAddIntrstRteVal());

                    if (getLastIntAppDate() != null && !getLastIntAppDate().equals("")) {
                        closureData.put("FROM_INT_DATE", getLastIntAppDate());
                    } else {
                        closureData.put("FROM_INT_DATE", getDepositDate());
                    }
                    if (getLblClosingType().equals(CommonConstants.PREMATURE_CLOSURE)) {
                        closureData.put("TO_INT_DATE", curDate);
                    } else {
                        closureData.put("TO_INT_DATE", getMaturityDate());
                    }
                    if (getBothRecPayMap() != null && getBothRecPayMap().size() > 0) {
                        closureData.put("BOTH_PAY_REC", getBothRecPayMap());
                    }
                    if(rec_recivable!=null && CommonUtil.convertObjToDouble(rec_recivable)>0){
                       closureData.put("REC_RECIVABLE", rec_recivable);  
                    }
                    closureData.put("AUTHORIZE_BY_2", getAuthrize2());
                    //Added by sreekrishnan for premature charge transaction
                    closureData.put("TOTAL_CHARGE", getTotalCharge());
                    objMap.put("CLOSUREMAP", closureData);
                    System.out.println("#######doAction######### : " + closureData);
                }
                //============= START OF TRANSACTION BLOCK ===================
                if (CommonUtil.convertObjToDouble(getClosingDisbursal()).doubleValue() > 0) {
                    if (transactionDetailsTO == null) {
                        transactionDetailsTO = new LinkedHashMap();
                    }
                    if (deletedTransactionDetailsTO != null) {
                        transactionDetailsTO.put(DELETED_TRANS_TOs, deletedTransactionDetailsTO);
                        deletedTransactionDetailsTO = null;
                    }
                    transactionDetailsTO.put(NOT_DELETED_TRANS_TOs, allowedTransactionDetailsTO);
                    allowedTransactionDetailsTO = null;

                    objMap.put("TransactionTO", transactionDetailsTO);
                    HashMap transMap = (HashMap) transactionDetailsTO.get("NOT_DELETED_TRANS_TOs");
                    System.out.println("Transaction with TL" + transMap);
                    if (transMap.containsKey("1") && transMap.get("1") != null) {
                        TransactionTO transactionTO = (TransactionTO) transMap.get("1");
                        if (transactionTO.getProductType() != null && transactionTO.getProductType().equals("TL") && transactionTO.getProductId() != null) {
                            if (transactionTO.getDebitAcctNo() != null && transactionTO.getDebitAcctNo().length() > 0) {
                                //double taxAmt = calcServiceTaxAmount(transactionTO.getDebitAcctNo(), transactionTO.getProductId());
                                List taxSettingsList = calcServiceTaxAmount(transactionTO.getDebitAcctNo(), transactionTO.getProductId());
                                            if (taxSettingsList != null && taxSettingsList.size() > 0) {
                                HashMap ser_Tax_Val = new HashMap();
                                ser_Tax_Val.put(ServiceTaxCalculation.CURR_DT, curDate.clone());
                                //ser_Tax_Val.put(ServiceTaxCalculation.TOT_AMOUNT, taxAmt);
                                ser_Tax_Val.put("SERVICE_TAX_DATA", taxSettingsList);
                                ServiceTaxCalculation objServiceTax = new ServiceTaxCalculation();
                                serviceTax_Map = objServiceTax.calculateServiceTax(ser_Tax_Val);
                                if (serviceTax_Map != null && serviceTax_Map.containsKey(ServiceTaxCalculation.TOT_TAX_AMT)) {
                                    String amt = CommonUtil.convertObjToStr(serviceTax_Map.get(ServiceTaxCalculation.TOT_TAX_AMT));
                                    //serviceTax_Map.put(ServiceTaxCalculation.TOT_TAX_AMT, objServiceTax.roundOffAmt(amt, "NEAREST_VALUE"));                                                                     
                                    serviceTax_Map.put(ServiceTaxCalculation.TOT_TAX_AMT, amt);                                    
                                }
                                if (getServiceTax_Map() != null && getServiceTax_Map().size() > 0
                                        && CommonUtil.convertObjToDouble(getServiceTax_Map().get("TOT_TAX_AMT")) > 0) {
                                    objMap.put("serviceTaxDetails", getServiceTax_Map());
                                    serviceTax_Map.put("act_num",transactionTO.getDebitAcctNo());
                                    objMap.put("serviceTaxDetailsTO", setServiceTaxDetails(serviceTax_Map));
                                }
                            }
                            }
                        }
                    }
                }
                if (getAuthorizeMap() != null) {
                    double penalAmt = CommonUtil.convertObjToDouble(getDepositPenalAmt()).doubleValue();
                    double penalMonth = CommonUtil.convertObjToDouble(getDepositPenalMonth()).doubleValue();
                    if (penalAmt > 0) {
                        objMap.put("DEPOSIT_PENAL_AMT", getDepositPenalAmt());
                        objMap.put("DEPOSIT_PENAL_MONTH", getDepositPenalMonth());
                    }
                    objMap.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
                    if (TrueTransactMain.SERVICE_TAX_REQ.equals("Y")) {
                        objMap.put("SERVICE_TAX_AUTH", "SERVICE_TAX_AUTH");
                    }
                }
                //========= END OF TRANSACTION BLOCK ======================
//                HashMap proxyResultMap = new HashMap();
                //added by vivek
                System.out.println("payorrecjcijifw>>>" + getLblPayRecDet());
                if (getLblPayRecDet() != null) {
                    objMap.put("PAYORRECEIVABLE", getLblPayRecDet());
                }
                //Added by sreekrishnan for interbranch transaction
                objMap.put("BRANCH_ID", getSelectedBranchID());
                System.out.println("branch%#%#%%#@"+getSelectedBranchID());
                objMap.put(CommonConstants.INITIATED_BRANCH, ProxyParameters.BRANCH_ID);
                objMap.put(CommonConstants.SCREEN, getScreen()); // Added by nithya on 26-08-2016 [ inserting screen name for transactions ]
                objMap.put(CommonConstants.SCREEN, "Deposit Account Closing");
                HashMap proxyResultMap = proxy.execute(objMap, map);
                setProxyReturnMap(proxyResultMap);
                setResult(getActionType());
                objMap = null;
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            e.printStackTrace();
            parseException.logException(e, true);
        }
    }

    private void setPartialWithDrawalTO() {
        objPartialWithDrawalTO = new DepositWithDrawalTO();
        objPartialWithDrawalTO.setDepositNo(getTxtDepositNo());
        objPartialWithDrawalTO.setDepositSubNo(CommonUtil.convertObjToInt(getSubDepositNo()));
        objPartialWithDrawalTO.setNoOfUnits(CommonUtil.convertObjToDouble(this.getTxtPWNoOfUnits()));
        objPartialWithDrawalTO.setWithdrawAmt(CommonUtil.convertObjToDouble(this.getTxtAmtWithDrawn()));
        objPartialWithDrawalTO.setWithdrawDt(curDate);
        setPWTOStatus();
        // setTOStatus();
    }

    private void setPWTOStatus() {
        objPartialWithDrawalTO.setStatus(this.getWithdrawalTOStatus());
        objPartialWithDrawalTO.setStatusBy(TrueTransactMain.USER_ID);
        objPartialWithDrawalTO.setStatusDt(curDate);
    }

    public String checkAcNoWithoutProdType(String actNum) {
        HashMap mapData = new HashMap();
        boolean isExists = false;
//        String actNum="";
        try {//dont delete chck selectalldao
            mapData.put("ACT_NUM", actNum);
            List mapDataList = ClientUtil.executeQuery("getActNumFromAllProducts", mapData);
            System.out.println("#### mapDataList :" + mapDataList);
            if (mapDataList != null && mapDataList.size() > 0) {
                mapData = (HashMap) mapDataList.get(0);
                setTxtDepositNo(CommonUtil.convertObjToStr(mapData.get("ACT_NUM")));
                 if (!(CommonUtil.convertObjToStr(mapData.get("BRANCH_ID")).equals(TrueTransactMain.selBranch))) {
                    ClientUtil.displayAlert("This Deposit Account does not belong to current branch.\n Please do interbranch transaction.");
                    resetDepositDetails();
                    resetCustomerDetails();
                    actNum = "";
                }else{
                actNum = CommonUtil.convertObjToStr(mapData.get("ACT_NUM"));
                setSelectedBranchID(CommonUtil.convertObjToStr(mapData.get("BRANCH_ID")));


                HashMap paramMap = new HashMap();

                paramMap.put("DEPOSITNO", mapData.get("ACT_NUM"));
                paramMap.put(CommonConstants.PRODUCT_ID, mapData.get("PROD_ID"));
                paramMap.put("DEPOSITSUBNO", CommonUtil.convertObjToInt(mapData.get("ACT_NUM")));

                getSubDepositNosLien(paramMap);





//                cbmProductIDProdType.setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_TYPE")));
//                setProdType(CommonUtil.convertObjToStr(mapData.get("PROD_TYPE")));
//                setCbmProductID(getProdType());
//                getProducts();
                cbmProductId.setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_ID")));
                isExists = true;
                 }
            } else {
//                ArrayList key=new ArrayList();
//                ArrayList value=new ArrayList();
//                key.add("");
//                value.add("");   
//                setCbmProdId("");
//                isExists = false;
//                key = null;
//                value = null;
                isExists = false;
                actNum = "";
            }
            mapDataList = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        mapData = null;
        return actNum;
    }

    private String getCommand() {
        if (this.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            return CommonConstants.TOSTATUS_INSERT;
        } else if (this.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            return CommonConstants.TOSTATUS_UPDATE;
        } else if (this.getActionType() == ClientConstants.ACTIONTYPE_DELETE) {
            return CommonConstants.TOSTATUS_DELETE;
        }
        return "";
    }

    /** Getter for property withdrawalTOStatus.
     * @return Value of property withdrawalTOStatus.
     *
     */
    public java.lang.String getWithdrawalTOStatus() {
        return withdrawalTOStatus;
    }

    /** Setter for property withdrawalTOStatus.
     * @param withdrawalTOStatus New value of property withdrawalTOStatus.
     *
     */
    public void setWithdrawalTOStatus(java.lang.String withdrawalTOStatus) {
        this.withdrawalTOStatus = withdrawalTOStatus;
    }

    /** Getter for property tbmPartialWithdrawal.
     * @return Value of property tbmPartialWithdrawal.
     *
     */
    public TableModel getTbmPartialWithdrawal() {
        return tbmPartialWithdrawal;
    }

    /** Setter for property tbmPartialWithdrawal.
     * @param tbmPartialWithdrawal New value of property tbmPartialWithdrawal.
     *
     */
    public void setTbmPartialWithdrawal(TableModel tbmPartialWithdrawal) {
        this.tbmPartialWithdrawal = tbmPartialWithdrawal;
    }

    public void populatePWTableRow(int rowNum) {
        objPartialWithDrawalTO = (DepositWithDrawalTO) withdrawalTOs.get(rowNum);
        this.setTxtAmtWithDrawn(CommonUtil.convertObjToStr(objPartialWithDrawalTO.getWithdrawAmt()));
        this.setTxtPWNoOfUnits(CommonUtil.convertObjToStr(objPartialWithDrawalTO.getNoOfUnits()));
        //objPartialWithDrawalTO=null;
    }

    public void deletePWData(int rowNum) {
        objPartialWithDrawalTO = (DepositWithDrawalTO) withdrawalTOs.get(rowNum);
        setPWTOStatus();
        if (objPartialWithDrawalTO.getWithdrawNo().compareToIgnoreCase("-") != 0) {
            deletePWList.add(objPartialWithDrawalTO);
        }
        withdrawalTOs.remove(rowNum);

        this.tbmPartialWithdrawal.removeRow(rowNum);
        this.tbmPartialWithdrawal.fireTableDataChanged();

        //objPartialWithDrawalTO=null;
    }

    /** Getter for property txtPWNoOfUnits.
     * @return Value of property txtPWNoOfUnits.
     *
     */
    public java.lang.String getTxtPWNoOfUnits() {
        return txtPWNoOfUnits;
    }

    /** Setter for property txtPWNoOfUnits.
     * @param txtPWNoOfUnits New value of property txtPWNoOfUnits.
     *
     */
    public void setTxtPWNoOfUnits(java.lang.String txtPWNoOfUnits) {
        this.txtPWNoOfUnits = txtPWNoOfUnits;
    }

    public void insertPWData(int rowNo) {
        setPartialWithDrawalTO();
        if (rowNo == -1) {
            this.objPartialWithDrawalTO.setWithdrawNo("-");
            this.setPWTOStatus();
            this.withdrawalTOs.add(this.objPartialWithDrawalTO);
            ArrayList irRow = this.setRow();
            this.tbmPartialWithdrawal.insertRow(tbmPartialWithdrawal.getRowCount(), irRow);
        } else {
            updatePWTO((DepositWithDrawalTO) this.withdrawalTOs.get(rowNo));
            if (objPartialWithDrawalTO.getWithdrawNo().compareToIgnoreCase("-") != 0) {
                setPWTOStatus();
            }
            ArrayList irRow = setRow();
            this.withdrawalTOs.set(rowNo, objPartialWithDrawalTO);
            this.tbmPartialWithdrawal.removeRow(rowNo);
            this.tbmPartialWithdrawal.insertRow(rowNo, irRow);
        }
        this.tbmPartialWithdrawal.fireTableDataChanged();
        //objPartialWithDrawalTO=null;
        //return 0;
    }

    private void updatePWTO(DepositWithDrawalTO oldTO) {
        oldTO.setNoOfUnits(objPartialWithDrawalTO.getNoOfUnits());
        oldTO.setWithdrawAmt(objPartialWithDrawalTO.getWithdrawAmt());
        //oldTO.setWithdrawDt(objPartialWithDrawalTO.getWithdrawDt());
        objPartialWithDrawalTO = oldTO;
    }

    /** Getter for property modeOfSettlement.
     * @return Value of property modeOfSettlement.
     *
     */
    public java.lang.String getModeOfSettlement() {
        return modeOfSettlement;
    }

    /** Setter for property modeOfSettlement.
     * @param modeOfSettlement New value of property modeOfSettlement.
     *
     */
    public void setModeOfSettlement(java.lang.String modeOfSettlement) {
        this.modeOfSettlement = modeOfSettlement;
    }

    /** Getter for property depositActName.
     * @return Value of property depositActName.
     *
     */
    public java.lang.String getDepositActName() {
        return depositActName;
    }

    /** Setter for property depositActName.
     * @param depositActName New value of property depositActName.
     *
     */
    public void setDepositActName(java.lang.String depositActName) {
        this.depositActName = depositActName;
    }

    /**
     *============= CODE FOR TRANSACTION ENABLING FOLLOWS =======================
     * Added by Sunil (152691)
     * first revision on on 7 Feb 2005 @ 12.30 PM
     */
    /**
     * Setter for property transactionOB.
     * @param transactionOB New value of property transactionOB.
     */
    public void setTransactionOB(TransactionOB transactionOB) {
        this.transactionOB = transactionOB;
    }

    /**
     * Getter for property allowedTransactionDetailsTO.
     * @return Value of property allowedTransactionDetailsTO.
     */
    public java.util.LinkedHashMap getAllowedTransactionDetailsTO() {
        return allowedTransactionDetailsTO;
    }

    /**
     * Setter for property allowedTransactionDetailsTO.
     * @param allowedTransactionDetailsTO New value of property allowedTransactionDetailsTO.
     */
    public void setAllowedTransactionDetailsTO(java.util.LinkedHashMap allowedTransactionDetailsTO) {
        this.allowedTransactionDetailsTO = allowedTransactionDetailsTO;
    }

    /**
     * Getter for property authorizeMap.
     * @return Value of property authorizeMap.
     */
    public java.util.HashMap getAuthorizeMap() {
        return authorizeMap;
    }

    /**
     * Setter for property authorizeMap.
     * @param authorizeMap New value of property authorizeMap.
     */
    public void setAuthorizeMap(java.util.HashMap authorizeMap) {
        this.authorizeMap = authorizeMap;
    }

    /**
     * Getter for property lienAmount.
     * @return Value of property lienAmount.
     */
    public double getLienAmount() {
        return lienAmount;
    }

    /**
     * Setter for property lienAmount.
     * @param lienAmount New value of property lienAmount.
     */
    public void setLienAmount(double lienAmount) {
        this.lienAmount = lienAmount;
    }

    /**
     * Getter for property freezeAmount.
     * @return Value of property freezeAmount.
     */
    public double getFreezeAmount() {
        return freezeAmount;
    }

    /**
     * Setter for property freezeAmount.
     * @param freezeAmount New value of property freezeAmount.
     */
    public void setFreezeAmount(double freezeAmount) {
        this.freezeAmount = freezeAmount;
    }

    /**
     * Getter for property RateApplicable.
     * @return Value of property RateApplicable.
     */
    public java.lang.String getRateApplicable() {
        return RateApplicable;
    }

    /**
     * Setter for property RateApplicable.
     * @param RateApplicable New value of property RateApplicable.
     */
    public void setRateApplicable(java.lang.String RateApplicable) {
        this.RateApplicable = RateApplicable;
    }

    /**
     * Getter for property PenaltyPenalRate.
     * @return Value of property PenaltyPenalRate.
     */
    public java.lang.String getPenaltyPenalRate() {
        return PenaltyPenalRate;
    }

    /**
     * Setter for property PenaltyPenalRate.
     * @param PenaltyPenalRate New value of property PenaltyPenalRate.
     */
    public void setPenaltyPenalRate(java.lang.String PenaltyPenalRate) {
        this.PenaltyPenalRate = PenaltyPenalRate;
    }

    /**
     * Getter for property setPenaltyPenalRate.
     * @return Value of property setPenaltyPenalRate.
     */
    public java.lang.String getSetPenaltyPenalRate() {
        return setPenaltyPenalRate;
    }

    /**
     * Setter for property setPenaltyPenalRate.
     * @param setPenaltyPenalRate New value of property setPenaltyPenalRate.
     */
    public void setSetPenaltyPenalRate(java.lang.String setPenaltyPenalRate) {
        this.setPenaltyPenalRate = setPenaltyPenalRate;
    }

    /**
     * Getter for property rdoPenaltyPenalRate_yes.
     * @return Value of property rdoPenaltyPenalRate_yes.
     */
    public boolean isRdoPenaltyPenalRate_yes() {
        return rdoPenaltyPenalRate_yes;
    }

    /**
     * Setter for property rdoPenaltyPenalRate_yes.
     * @param rdoPenaltyPenalRate_yes New value of property rdoPenaltyPenalRate_yes.
     */
    public void setRdoPenaltyPenalRate_yes(boolean rdoPenaltyPenalRate_yes) {
        this.rdoPenaltyPenalRate_yes = rdoPenaltyPenalRate_yes;
    }

    /**
     * Getter for property rdoPenaltyPenalRate_no.
     * @return Value of property rdoPenaltyPenalRate_no.
     */
    public boolean isRdoPenaltyPenalRate_no() {
        return rdoPenaltyPenalRate_no;
    }

    /**
     * Setter for property rdoPenaltyPenalRate_no.
     * @param rdoPenaltyPenalRate_no New value of property rdoPenaltyPenalRate_no.
     */
    public void setRdoPenaltyPenalRate_no(boolean rdoPenaltyPenalRate_no) {
        this.rdoPenaltyPenalRate_no = rdoPenaltyPenalRate_no;
    }

    /**
     * Getter for property disburseAmt.
     * @return Value of property disburseAmt.
     */
    public double getDisburseAmt() {
        return disburseAmt;
    }

    /**
     * Setter for property disburseAmt.
     * @param disburseAmt New value of property disburseAmt.
     */
    public void setDisburseAmt(double disburseAmt) {
        this.disburseAmt = disburseAmt;
    }

    /**
     * Getter for property balanceAmt.
     * @return Value of property balanceAmt.
     */
    public double getBalanceAmt() {
        return balanceAmt;
    }

    /**
     * Setter for property balanceAmt.
     * @param balanceAmt New value of property balanceAmt.
     */
    public void setBalanceAmt(double balanceAmt) {
        this.balanceAmt = balanceAmt;
    }

    /**
     * Getter for property lblReceive.
     * @return Value of property lblReceive.
     */
    public java.lang.String getLblReceive() {
        return lblReceive;
    }

    /**
     * Setter for property lblReceive.
     * @param lblReceive New value of property lblReceive.
     */
    public void setLblReceive(java.lang.String lblReceive) {
        this.lblReceive = lblReceive;
    }

    /**
     * Getter for property lblBalanceDeposit.
     * @return Value of property lblBalanceDeposit.
     */
    public java.lang.String getLblBalanceDeposit() {
        return lblBalanceDeposit;
    }

    /**
     * Setter for property lblBalanceDeposit.
     * @param lblBalanceDeposit New value of property lblBalanceDeposit.
     */
    public void setLblBalanceDeposit(java.lang.String lblBalanceDeposit) {
        this.lblBalanceDeposit = lblBalanceDeposit;
    }

    /**
     * Getter for property transStatus.
     * @return Value of property transStatus.
     */
    public java.lang.String getTransStatus() {
        return transStatus;
    }

    /**
     * Setter for property transStatus.
     * @param transStatus New value of property transStatus.
     */
    public void setTransStatus(java.lang.String transStatus) {
        this.transStatus = transStatus;
    }

    /**
     * Getter for property penaltyInt.
     * @return Value of property penaltyInt.
     */
    public java.lang.String getPenaltyInt() {
        return penaltyInt;
    }

    /**
     * Setter for property penaltyInt.
     * @param penaltyInt New value of property penaltyInt.
     */
    public void setPenaltyInt(java.lang.String penaltyInt) {
        this.penaltyInt = penaltyInt;
    }

    /**
     * Getter for property transProdId.
     * @return Value of property transProdId.
     */
    public java.lang.String getTransProdId() {
        return transProdId;
    }

    /**
     * Setter for property transProdId.
     * @param transProdId New value of property transProdId.
     */
    public void setTransProdId(java.lang.String transProdId) {
        this.transProdId = transProdId;
    }

    /**
     * Getter for property actualPeriodRun.
     * @return Value of property actualPeriodRun.
     */
    public java.lang.String getActualPeriodRun() {
        return actualPeriodRun;
    }

    /**
     * Setter for property actualPeriodRun.
     * @param actualPeriodRun New value of property actualPeriodRun.
     */
    public void setActualPeriodRun(java.lang.String actualPeriodRun) {
        this.actualPeriodRun = actualPeriodRun;
    }

    /**
     * Getter for property tdsAcHd.
     * @return Value of property tdsAcHd.
     */
    public java.lang.String getTdsAcHd() {
        return tdsAcHd;
    }

    /**
     * Setter for property tdsAcHd.
     * @param tdsAcHd New value of property tdsAcHd.
     */
    public void setTdsAcHd(java.lang.String tdsAcHd) {
        this.tdsAcHd = tdsAcHd;
    }

    /**
     * Getter for property lblPayRecDet.
     * @return Value of property lblPayRecDet.
     */
    public java.lang.String getLblPayRecDet() {
        return lblPayRecDet;
    }

    /**
     * Setter for property lblPayRecDet.
     * @param lblPayRecDet New value of property lblPayRecDet.
     */
    public void setLblPayRecDet(java.lang.String lblPayRecDet) {
        this.lblPayRecDet = lblPayRecDet;
    }

    /**
     * Getter for property delayedInstallments.
     * @return Value of property delayedInstallments.
     */
    public java.lang.String getDelayedInstallments() {
        return delayedInstallments;
    }

    /**
     * Setter for property delayedInstallments.
     * @param delayedInstallments New value of property delayedInstallments.
     */
    public void setDelayedInstallments(java.lang.String delayedInstallments) {
        this.delayedInstallments = delayedInstallments;
    }

    /**
     * Getter for property chargeAmount.
     * @return Value of property chargeAmount.
     */
    public java.lang.String getChargeAmount() {
        return chargeAmount;
    }

    /**
     * Setter for property chargeAmount.
     * @param chargeAmount New value of property chargeAmount.
     */
    public void setChargeAmount(java.lang.String chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    /**
     * Getter for property rdoYesButton.
     * @return Value of property rdoYesButton.
     */
    public boolean isRdoYesButton() {
        return rdoYesButton;
    }

    /**
     * Setter for property rdoYesButton.
     * @param rdoYesButton New value of property rdoYesButton.
     */
    public void setRdoYesButton(boolean rdoYesButton) {
        this.rdoYesButton = rdoYesButton;
    }

    /**
     * Getter for property ViewTypeDet.
     * @return Value of property ViewTypeDet.
     */
    public int getViewTypeDet() {
        return ViewTypeDet;
    }

    /**
     * Setter for property ViewTypeDet.
     * @param ViewTypeDet New value of property ViewTypeDet.
     */
    public void setViewTypeDet(int ViewTypeDet) {
        this.ViewTypeDet = ViewTypeDet;
    }

    /**
     * Getter for property rdoNoButton.
     * @return Value of property rdoNoButton.
     */
    public boolean isRdoNoButton() {
        return rdoNoButton;
    }

    /**
     * Setter for property rdoNoButton.
     * @param rdoNoButton New value of property rdoNoButton.
     */
    public void setRdoNoButton(boolean rdoNoButton) {
        this.rdoNoButton = rdoNoButton;
    }

    /**
     * Getter for property totalBalance.
     * @return Value of property totalBalance.
     */
    public java.lang.String getTotalBalance() {
        return totalBalance;
    }

    /**
     * Setter for property totalBalance.
     * @param totalBalance New value of property totalBalance.
     */
    public void setTotalBalance(java.lang.String totalBalance) {
        this.totalBalance = totalBalance;
    }

    /**
     * Getter for property ltdClosingMap.
     * @return Value of property ltdClosingMap.
     */
    public java.util.HashMap getLtdClosingMap() {
        return ltdClosingMap;
    }

    /**
     * Setter for property ltdClosingMap.
     * @param ltdClosingMap New value of property ltdClosingMap.
     */
    public void setLtdClosingMap(java.util.HashMap ltdClosingMap) {
        this.ltdClosingMap = ltdClosingMap;
    }

    /**
     * Getter for property typeOfDep.
     * @return Value of property typeOfDep.
     */
    public java.lang.String getTypeOfDep() {
        return typeOfDep;
    }

    /**
     * Setter for property typeOfDep.
     * @param typeOfDep New value of property typeOfDep.
     */
    public void setTypeOfDep(java.lang.String typeOfDep) {
        this.typeOfDep = typeOfDep;
    }

    /**
     * Getter for property depositPenalAmt.
     * @return Value of property depositPenalAmt.
     */
    public java.lang.String getDepositPenalAmt() {
        return depositPenalAmt;
    }

    /**
     * Setter for property depositPenalAmt.
     * @param depositPenalAmt New value of property depositPenalAmt.
     */
    public void setDepositPenalAmt(java.lang.String depositPenalAmt) {
        this.depositPenalAmt = depositPenalAmt;
    }

    /**
     * Getter for property depositPenalMonth.
     * @return Value of property depositPenalMonth.
     */
    public java.lang.String getDepositPenalMonth() {
        return depositPenalMonth;
    }

    /**
     * Setter for property depositPenalMonth.
     * @param depositPenalMonth New value of property depositPenalMonth.
     */
    public void setDepositPenalMonth(java.lang.String depositPenalMonth) {
        this.depositPenalMonth = depositPenalMonth;
    }

    /**
     * Getter for property rdoTypeOfDeposit_Yes.
     * @return Value of property rdoTypeOfDeposit_Yes.
     */
    public boolean isRdoTypeOfDeposit_Yes() {
        return rdoTypeOfDeposit_Yes;
    }

    /**
     * Setter for property rdoTypeOfDeposit_Yes.
     * @param rdoTypeOfDeposit_Yes New value of property rdoTypeOfDeposit_Yes.
     */
    public void setRdoTypeOfDeposit_Yes(boolean rdoTypeOfDeposit_Yes) {
        this.rdoTypeOfDeposit_Yes = rdoTypeOfDeposit_Yes;
    }

    /**
     * Getter for property rdoTypeOfDeposit_No.
     * @return Value of property rdoTypeOfDeposit_No.
     */
    public boolean isRdoTypeOfDeposit_No() {
        return rdoTypeOfDeposit_No;
    }

    /**
     * Setter for property rdoTypeOfDeposit_No.
     * @param rdoTypeOfDeposit_No New value of property rdoTypeOfDeposit_No.
     */
    public void setRdoTypeOfDeposit_No(boolean rdoTypeOfDeposit_No) {
        this.rdoTypeOfDeposit_No = rdoTypeOfDeposit_No;
    }

    /**
     * Getter for property deathClaim.
     * @return Value of property deathClaim.
     */
    public java.lang.String getDeathClaim() {
        return deathClaim;
    }

    /**
     * Setter for property deathClaim.
     * @param deathClaim New value of property deathClaim.
     */
    public void setDeathClaim(java.lang.String deathClaim) {
        this.deathClaim = deathClaim;
    }

    /**
     * Getter for property deathClaimAmt.
     * @return Value of property deathClaimAmt.
     */
    public java.lang.String getDeathClaimAmt() {
        return deathClaimAmt;
    }

    /**
     * Setter for property deathClaimAmt.
     * @param deathClaimAmt New value of property deathClaimAmt.
     */
    public void setDeathClaimAmt(java.lang.String deathClaimAmt) {
        this.deathClaimAmt = deathClaimAmt;
    }

    /**
     * Getter for property agentCommisionRecoveredValue.
     * @return Value of property agentCommisionRecoveredValue.
     */
    public java.lang.String getAgentCommisionRecoveredValue() {
        return agentCommisionRecoveredValue;
    }

    /**
     * Setter for property agentCommisionRecoveredValue.
     * @param agentCommisionRecoveredValue New value of property agentCommisionRecoveredValue.
     */
    public void setAgentCommisionRecoveredValue(java.lang.String agentCommisionRecoveredValue) {
        this.agentCommisionRecoveredValue = agentCommisionRecoveredValue;
    }

    /**
     * Getter for property rdoTransfer_Yes.
     * @return Value of property rdoTransfer_Yes.
     */
    public boolean isRdoTransfer_Yes() {
        return rdoTransfer_Yes;
    }

    /**
     * Setter for property rdoTransfer_Yes.
     * @param rdoTransfer_Yes New value of property rdoTransfer_Yes.
     */
    public void setRdoTransfer_Yes(boolean rdoTransfer_Yes) {
        this.rdoTransfer_Yes = rdoTransfer_Yes;
    }

    /**
     * Getter for property rdoTransfer_No.
     * @return Value of property rdoTransfer_No.
     */
    public boolean isRdoTransfer_No() {
        return rdoTransfer_No;
    }

    /**
     * Setter for property rdoTransfer_No.
     * @param rdoTransfer_No New value of property rdoTransfer_No.
     */
    public void setRdoTransfer_No(boolean rdoTransfer_No) {
        this.rdoTransfer_No = rdoTransfer_No;
    }

    /**
     * Getter for property transferBranch_code.
     * @return Value of property transferBranch_code.
     */
    public java.lang.String getTransferBranch_code() {
        return transferBranch_code;
    }

    /**
     * Setter for property transferBranch_code.
     * @param transferBranch_code New value of property transferBranch_code.
     */
    public void setTransferBranch_code(java.lang.String transferBranch_code) {
        this.transferBranch_code = transferBranch_code;
    }

    /**
     * Getter for property lstProvDt.
     * @return Value of property lstProvDt.
     */
    public java.lang.String getLstProvDt() {
        return lstProvDt;
    }

    /**
     * Setter for property lstProvDt.
     * @param lstProvDt New value of property lstProvDt.
     */
    public void setLstProvDt(java.lang.String lstProvDt) {
        this.lstProvDt = lstProvDt;
    }

    /**
     * Getter for property flPtWithoutPeriod.
     * @return Value of property flPtWithoutPeriod.
     */
    public boolean isFlPtWithoutPeriod() {
        return flPtWithoutPeriod;
    }

    /**
     * Setter for property flPtWithoutPeriod.
     * @param flPtWithoutPeriod New value of property flPtWithoutPeriod.
     */
    public void setFlPtWithoutPeriod(boolean flPtWithoutPeriod) {
        this.flPtWithoutPeriod = flPtWithoutPeriod;
    }

    /**
     * Getter for property permanentPayReceivable.
     * @return Value of property permanentPayReceivable.
     */
    public java.lang.String getPermanentPayReceivable() {
        return permanentPayReceivable;
    }

    /**
     * Setter for property permanentPayReceivable.
     * @param permanentPayReceivable New value of property permanentPayReceivable.
     */
    public void setPermanentPayReceivable(java.lang.String permanentPayReceivable) {
        this.permanentPayReceivable = permanentPayReceivable;
    }

    /**
     * Getter for property serviceChargeMap.
     * @return Value of property serviceChargeMap.
     */
    public java.util.HashMap getServiceChargeMap() {
        return serviceChargeMap;
    }

    /**
     * Setter for property serviceChargeMap.
     * @param serviceChargeMap New value of property serviceChargeMap.
     */
    public void setServiceChargeMap(java.util.HashMap serviceChargeMap) {
        this.serviceChargeMap = serviceChargeMap;
    }

    /**
     * Getter for property termLoanAdvanceActNum.
     * @return Value of property termLoanAdvanceActNum.
     */
    public java.lang.String getTermLoanAdvanceActNum() {
        return termLoanAdvanceActNum;
    }

    /**
     * Setter for property termLoanAdvanceActNum.
     * @param termLoanAdvanceActNum New value of property termLoanAdvanceActNum.
     */
    public void setTermLoanAdvanceActNum(java.lang.String termLoanAdvanceActNum) {
        this.termLoanAdvanceActNum = termLoanAdvanceActNum;
    }

    /**
     * Getter for property ltdDeposit.
     * @return Value of property ltdDeposit.
     */
    public java.lang.String getLtdDeposit() {
        return ltdDeposit;
    }

    /**
     * Setter for property ltdDeposit.
     * @param ltdDeposit New value of property ltdDeposit.
     */
    public void setLtdDeposit(java.lang.String ltdDeposit) {
        this.ltdDeposit = ltdDeposit;
    }
//added by chithra 22/04/2014
    public String getLblAddIntRtAmtVal() {
        return lblAddIntRtAmtVal;
    }

    public void setLblAddIntRtAmtVal(String lblAddIntRtAmtVal) {
        this.lblAddIntRtAmtVal = lblAddIntRtAmtVal;
    }

    public String getLblAddIntrstRteVal() {
        return lblAddIntrstRteVal;
    }

    public void setLblAddIntrstRteVal(String lblAddIntrstRteVal) {
        this.lblAddIntrstRteVal = lblAddIntrstRteVal;
    }

    public String getLblMaturityPeriod() {
        return lblMaturityPeriod;
    }

    public void setLblMaturityPeriod(String lblMaturityPeriod) {
        this.lblMaturityPeriod = lblMaturityPeriod;
    }
    public String calcAddIntAmt(Date startDt,Date currDt) {
        double prin_Amt = CommonUtil.convertObjToDouble(this.getPrinicipal());
        double addIntrst = CommonUtil.convertObjToDouble(this.getLblAddIntrstRteVal());
        String days = this.getLblMaturityPeriod().replace("Days", "").trim();
        int rem_days = CommonUtil.convertObjToInt(days);
        double AddIntAmt = 0;
        double totalMonths = 0;
        AddIntAmt = (prin_Amt * addIntrst * rem_days) / 36500;
        return CommonUtil.convertObjToStr(getRound(AddIntAmt, getInterestRound()));
        // return CommonUtil.convertObjToStr((prin_Amt * addIntrst * rem_days) / 36500);

    }
    public String calcAddIntAmtForLoan(Date startDt,Date currDt,String lienRoi,String lienAmt) {
        double addIntrst = CommonUtil.convertObjToDouble(this.getLblAddIntrstRteVal());
        String days = this.getLblMaturityPeriod().replace("Days", "").trim();
        int rem_days = CommonUtil.convertObjToInt(days);
        double lRoi = 0, lAmt = 0;
        if (lienRoi != null && !lienRoi.equals("")) {
            lRoi = CommonUtil.convertObjToDouble(lienRoi) + addIntrst;
        }
        if (lienAmt != null && !lienAmt.equals("")) {
            lAmt = CommonUtil.convertObjToDouble(lienAmt);
        }
        double loanAmt = (lAmt * lRoi * rem_days) / 36500;

        return CommonUtil.convertObjToStr(getRound(loanAmt, getInterestRound()));
    }

    public String getAddIntLoanAmt() {
        return addIntLoanAmt;
    }

    public void setAddIntLoanAmt(String addIntLoanAmt) {
        this.addIntLoanAmt = addIntLoanAmt;
    }

    public String getLblServiceTaxval() {
        return lblServiceTaxval;
    }

    public void setLblServiceTaxval(String lblServiceTaxval) {
        this.lblServiceTaxval = lblServiceTaxval;
    }

    public HashMap getServiceTax_Map() {
        return serviceTax_Map;
    }

    public void setServiceTax_Map(HashMap serviceTax_Map) {
        this.serviceTax_Map = serviceTax_Map;
    }

    public ServiceTaxDetailsTO setServiceTaxDetails(HashMap serviceTax_Map) {
        final ServiceTaxDetailsTO objservicetaxDetTo = new ServiceTaxDetailsTO();
        try {
            objservicetaxDetTo.setCommand(getCommand());
            objservicetaxDetTo.setStatus(CommonConstants.STATUS_CREATED);
            objservicetaxDetTo.setStatusBy(TrueTransactMain.USER_ID);
            objservicetaxDetTo.setAcct_Num(CommonUtil.convertObjToStr(serviceTax_Map.get("act_num")));
            objservicetaxDetTo.setParticulars("Deposit Closing");
            objservicetaxDetTo.setBranchID(ProxyParameters.BRANCH_ID);
            objservicetaxDetTo.setTrans_type("C");
            if (serviceTax_Map != null && serviceTax_Map.containsKey("SERVICE_TAX")) {
                objservicetaxDetTo.setServiceTaxAmt(CommonUtil.convertObjToDouble(serviceTax_Map.get("SERVICE_TAX")));
            }
            if (serviceTax_Map != null && serviceTax_Map.containsKey("EDUCATION_CESS")) {
                objservicetaxDetTo.setEducationCess(CommonUtil.convertObjToDouble(serviceTax_Map.get("EDUCATION_CESS")));
            }
            if (serviceTax_Map != null && serviceTax_Map.containsKey("HIGHER_EDU_CESS")) {
                objservicetaxDetTo.setHigherCess(CommonUtil.convertObjToDouble(serviceTax_Map.get("HIGHER_EDU_CESS")));
            }
            if (serviceTax_Map != null && serviceTax_Map.containsKey("SWACHH_CESS")) {
                objservicetaxDetTo.setSwachhCess(CommonUtil.convertObjToDouble(serviceTax_Map.get("SWACHH_CESS")));
            }
            if (serviceTax_Map != null && serviceTax_Map.containsKey("KRISHI_KALYAN_CESS")) {
                objservicetaxDetTo.setKrishiKalyan(CommonUtil.convertObjToDouble(serviceTax_Map.get("KRISHI_KALYAN_CESS")));
            }
            if (serviceTax_Map != null && serviceTax_Map.containsKey("TOT_TAX_AMT")) {
                objservicetaxDetTo.setTotalTaxAmt(CommonUtil.convertObjToDouble(serviceTax_Map.get("TOT_TAX_AMT")));
            }
            double roudVal = objservicetaxDetTo.getTotalTaxAmt() - (objservicetaxDetTo.getServiceTaxAmt() + objservicetaxDetTo.getEducationCess() + objservicetaxDetTo.getHigherCess() + objservicetaxDetTo.getSwachhCess() + objservicetaxDetTo.getKrishiKalyan());
            ServiceTaxCalculation serviceTaxObj = new ServiceTaxCalculation();
            objservicetaxDetTo.setRoundVal(CommonUtil.convertObjToStr(serviceTaxObj.roundOffAmtForRoundVal(roudVal)));
            objservicetaxDetTo.setStatusDt((Date) curDate.clone());

            if (getCommand().equalsIgnoreCase("INSERT")) {
                objservicetaxDetTo.setCreatedBy(TrueTransactMain.USER_ID);
                objservicetaxDetTo.setCreatedDt((Date) curDate.clone());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return objservicetaxDetTo;

    }

    public String getAuthrize2() {
        return authrize2;
    }

    public void setAuthrize2(String authrize2) {
        this.authrize2 = authrize2;
    }
    public double rd_NatureIntAmt(String rdNature) {
        double interest = 0, recIntVal = 0;
        double recInt = 0;
        if (rdNature != null && rdNature.equalsIgnoreCase("Y")) {
            int paidMonths = 0;
            int actualMonths = 0;
            int no_week = 0;
            double instAmount = 0.0,clearBlnce =0;
            HashMap rdPaidInst = new HashMap();
            rdPaidInst.put("DEPOSIT_NO", this.getTxtDepositNo());

            List rdPaidInstList = ClientUtil.executeQuery("getRdPaidInstllments", rdPaidInst);
            if (rdPaidInstList != null && rdPaidInstList.size() > 0) {
                rdPaidInst = (HashMap) rdPaidInstList.get(0);
                actualMonths = CommonUtil.convertObjToInt(rdPaidInst.get("TOTAL_INSTALLMENTS"));
                paidMonths = CommonUtil.convertObjToInt(rdPaidInst.get("TOTAL_INSTALL_PAID"));
                instAmount = CommonUtil.convertObjToDouble(rdPaidInst.get("DEPOSIT_AMT")).doubleValue();
                no_week = CommonUtil.convertObjToInt(rdPaidInst.get("DEPOSIT_PERIOD_WK"));
                clearBlnce = CommonUtil.convertObjToDouble(rdPaidInst.get("CLEAR_BALANCE"));
                if(instAmount>0)
                paidMonths = CommonUtil.convertObjToInt(clearBlnce/instAmount);
            }
            System.out.println("no_week :" + no_week + "paidMonths : " + paidMonths);

            if (no_week > 0) {
                setDepositPeriodWK(CommonUtil.convertObjToStr(no_week));
                setPrematureClosingRate("0");
                setRateApplicable("0");
                HashMap rdwhrMap = new HashMap();
                rdwhrMap.put("PROD_ID", getProdID());
                double instNo = 0, instPenal = 0;
                List slabList = ClientUtil.executeQuery("getRdWeeklyInstallmentSlabs", rdwhrMap);
                for (int k = 0; k < slabList.size(); k++) {
                    HashMap each = (HashMap) slabList.get(k);
                    if (each != null && each.size() > 0) {
                        double fromInst = CommonUtil.convertObjToDouble(each.get("FROM_INSTALL"));
                        double toInst = CommonUtil.convertObjToDouble(each.get("TO_INSTALL"));
                        if (paidMonths > fromInst && paidMonths <= toInst) {
                            instNo = CommonUtil.convertObjToDouble(each.get("INSTALLMENT_NO"));
                            instPenal = CommonUtil.convertObjToDouble(each.get("PENAL"));
                        }
                    }
                }

                if (instNo > 0) {
                    interest = instAmount * instNo;
                } else if (instPenal > 0) {
                    interest = 0;
                    recInt = instAmount * instPenal;
                    recIntVal = recInt;

                } else {
                    interest = 0;
                }
                this.setIntCr(CommonUtil.convertObjToStr(recInt));
                setClosingIntDb(CommonUtil.convertObjToStr(interest - recInt));
                rec_recivable = CommonUtil.convertObjToStr(recInt);
                System.out.println("interest--------- :" + interest + " recIntVal: " + recIntVal);
            }
        }
        return (interest - recInt);
    }

    public double getTotalCharge() {
        return totalCharge;
    }

    public void setTotalCharge(double totalCharge) {
        this.totalCharge = totalCharge;
    }
    // Added by nithya for int calc method installmentwise . Mantis id : 0005664: Daily deposit Interest calculation
     public double dailyInstSlabwiseIntCalc(String depoNo, String depoStatus) {
        String fullAmtToDefaulterAfterMaturity = "N";
        double totalMaturityAmount = 0.0;
        double totalPaidInstAmt = 0.0;
        double interest = 0, recIntVal = 0;
        double recInt = 0;
        String slabSelectionMethod = "";
        System.out.println("deposit  matured :: ");
        HashMap actDetailMap = new HashMap();
        actDetailMap.put("DEPOSIT_NO", depoNo);
        List actDetailLst = ClientUtil.executeQuery("getActDetailsForDailyPrdct", actDetailMap);
        HashMap actDetailsMap = new HashMap();
        actDetailsMap = (HashMap) actDetailLst.get(0);
        //DSA.MATURITY_DT,DSA.CLEAR_BALANCE,DSA.DEPOSIT_AMT,DSA.DEPOSIT_PERIOD_DD FROM
         if (actDetailsMap.containsKey("SLAB_SELECTION_METHOD") && actDetailsMap.get("SLAB_SELECTION_METHOD") != null) {
             slabSelectionMethod = CommonUtil.convertObjToStr(actDetailsMap.get("SLAB_SELECTION_METHOD"));
         }
         if (actDetailsMap.containsKey("FULLAMT_ON_MATURE_CLOSURE") && actDetailsMap.get("FULLAMT_ON_MATURE_CLOSURE") != null) {
            fullAmtToDefaulterAfterMaturity = CommonUtil.convertObjToStr(actDetailsMap.get("FULLAMT_ON_MATURE_CLOSURE")); 
         }        
        double actClearBal = CommonUtil.convertObjToDouble(actDetailsMap.get("CLEAR_BALANCE"));
        double actDepositAmount = CommonUtil.convertObjToDouble(actDetailsMap.get("DEPOSIT_AMT"));
        int sctDepoPeriod = CommonUtil.convertObjToInt(actDetailsMap.get("DEPOSIT_PERIOD_DD"));
        setDepositPeriodWK(CommonUtil.convertObjToStr(sctDepoPeriod));
        int actTotInstallPaid = (int) (actClearBal / actDepositAmount);
        totalMaturityAmount = sctDepoPeriod * actDepositAmount;
        totalPaidInstAmt = actTotInstallPaid * actDepositAmount;
        System.out.println("totalMaturityAmount :: " + totalMaturityAmount +"  totalPaidInstAmt :: " + totalPaidInstAmt);
        HashMap dailyWhrMap = new HashMap();
        dailyWhrMap.put("PROD_ID", getProdID());
        double slabInstNo = 0;
        double slabPenalInstNo = 0;
        List slabList = ClientUtil.executeQuery("getRdWeeklyInstallmentSlabs", dailyWhrMap);
        for (int k = 0; k < slabList.size(); k++) {
            HashMap each = (HashMap) slabList.get(k);
            if (each != null && each.size() > 0) {
                double fromInst = CommonUtil.convertObjToDouble(each.get("FROM_INSTALL"));
                double toInst = CommonUtil.convertObjToDouble(each.get("TO_INSTALL"));
                if (actTotInstallPaid > fromInst && actTotInstallPaid <= toInst) {
                    slabInstNo = CommonUtil.convertObjToDouble(each.get("INSTALLMENT_NO"));
                    slabPenalInstNo = CommonUtil.convertObjToDouble(each.get("PENAL"));
                }
            }
        }      
        
         if (slabSelectionMethod.equalsIgnoreCase("DIRECT")) {
             slabInstNo = slabInstNo;
             slabPenalInstNo = slabPenalInstNo;
         } else {
             // Conditions to be checked        
             if (depoStatus.equals(CommonConstants.NORMAL_CLOSURE)) {
                 if (actTotInstallPaid < sctDepoPeriod) { // If complete installments not paid : no interest, no penal
                     slabInstNo = 0;
                     slabPenalInstNo = 0;
                 } else if (actTotInstallPaid >= sctDepoPeriod) { // If complete installments paid : interest given, no penal
                     slabPenalInstNo = 0;
                 }
             } else if (depoStatus.equals(CommonConstants.PREMATURE_CLOSURE)) {
                 if (actTotInstallPaid < sctDepoPeriod) {// If complete installments not paid : no interest, penal charged
                     slabInstNo = 0;
                 } else if (actTotInstallPaid >= sctDepoPeriod) {// If complete installments paid : no interest, no penal 
                     slabInstNo = 0;
                     slabPenalInstNo = 0;
                 }
             }
             // End  
         }
         //Check for full amount payment during mature closure and full amount not remitted
         // Done for chazhur SCB - KD-3684 WEEKLY - CLOSING REG
                             /*
          WEEKLY NEW-PRODUCT ID 115WEEKLY CLOSING NEW REQUIREMENT IS GIVEN BELOW.
          1) MATURED CLOSING 52 INSTALLMENTS PLUS 2 INSTALLMENT BONUS.
          2) CLOSING AFTER ONE YEAR- PAYING FULL REMITTED  AMOUNT
          3) FOR PREMATURE CLOSING RECOVER 1 INSTALLMENT FROM THE REMITTED AMOUNT
         
         Note : Doing the changes also for Daily product
          */
         if (depoStatus.equals(CommonConstants.NORMAL_CLOSURE) && fullAmtToDefaulterAfterMaturity.equals("Y") && totalPaidInstAmt < totalMaturityAmount) {
             slabInstNo = 0;
             slabPenalInstNo = 0;
         }
         // End
         if (slabInstNo > 0) {
             interest = actDepositAmount * slabInstNo;
         } else if (slabPenalInstNo > 0) {
             interest = 0;
             recInt = actDepositAmount * slabPenalInstNo;
             recIntVal = recInt;
         } else {
             interest = 0;
         }
         System.out.println("interest :: " + interest); 
         System.out.println("recInt :: " + recInt); 
         this.setIntCr(CommonUtil.convertObjToStr(recInt));
         setClosingIntDb(CommonUtil.convertObjToStr(interest - recInt));
         rec_recivable = CommonUtil.convertObjToStr(recInt);
         System.out.println("interest--------- :" + interest + " recIntVal: " + recIntVal);
         return (interest - recInt);
    }
     
     // Added by nithya on 04-10-2017 for group deposit changes
     public double dailyGroupDepositIntCalc(String depoNo, String depoStatus) {
         System.out.println("Inside DepositClosingOB :: dailyGroupDepositIntCalc ");
        double interest = 0, recIntVal = 0;
        double maturityAmount = 0.0;
        int sctDepoPeriodYr = 0;
        int sctDepoPerioddd = 0;
        int sctDepoPeriodMon = 0;
        double recInt = 0;
        int sctDepoPeriod = 0;
        String intCalcType = "";
        String intRecType = "";
        String prematureIntRecType = "";
        double intCalcRate = 0;
        double intRecRate = 0;
        double prematureIntRecRate = 0;
        String isInterestRecovery = "";
        String isIntPayIfFullAmountNotPaid = "";
        double IntRateIfFullAmountNotPaid = 0;
        System.out.println("deposit  status :: " + depoStatus);
        HashMap actDetailMap = new HashMap();
        actDetailMap.put("DEPOSIT_NO", depoNo);
        List actDetailLst = ClientUtil.executeQuery("getActDetailsForDailyPrdct", actDetailMap);
        HashMap actDetailsMap = new HashMap();
        actDetailsMap = (HashMap) actDetailLst.get(0);
        List groupDepoIntDetailsLst = ClientUtil.executeQuery("getInterestRateForDepositGroup", actDetailMap);
        HashMap groupDepoIntDetailsMap = (HashMap) groupDepoIntDetailsLst.get(0);
        intCalcType = CommonUtil.convertObjToStr(groupDepoIntDetailsMap.get("INTEREST_AMOUNT_TYPE"));
        intRecType =  CommonUtil.convertObjToStr(groupDepoIntDetailsMap.get("INTEREST_RECOVERY_TYPE"));
        prematureIntRecType = CommonUtil.convertObjToStr(groupDepoIntDetailsMap.get("PREMATURE_INT_REC_CALC_TYPE"));
        intCalcRate = CommonUtil.convertObjToDouble(groupDepoIntDetailsMap.get("INTEREST_AMOUNT"));
        intRecRate = CommonUtil.convertObjToDouble(groupDepoIntDetailsMap.get("INTEREST_RECOVERY"));
        prematureIntRecRate = CommonUtil.convertObjToDouble(groupDepoIntDetailsMap.get("PREMATURE_INT_REC_CALC_AMOUNT"));
        //DSA.MATURITY_DT,DSA.CLEAR_BALANCE,DSA.DEPOSIT_AMT,DSA.DEPOSIT_PERIOD_DD FROM
        // IS_INTEREST_RECOVERY,INT_CALC_BEFORE_FULL_PAYMENT,INT_CALC_RATE_BEFORE_PAYMENT
        isInterestRecovery = CommonUtil.convertObjToStr(groupDepoIntDetailsMap.get("IS_INTEREST_RECOVERY"));
        isIntPayIfFullAmountNotPaid = CommonUtil.convertObjToStr(groupDepoIntDetailsMap.get("INT_CALC_BEFORE_FULL_PAYMENT"));
        IntRateIfFullAmountNotPaid = CommonUtil.convertObjToDouble(groupDepoIntDetailsMap.get("INT_CALC_RATE_BEFORE_PAYMENT"));
        double actClearBal = CommonUtil.convertObjToDouble(actDetailsMap.get("CLEAR_BALANCE"));
        double actDepositAmount = CommonUtil.convertObjToDouble(actDetailsMap.get("DEPOSIT_AMT"));
        if(actDetailsMap.get("DEPOSIT_PERIOD_DD") != null){
           sctDepoPerioddd = CommonUtil.convertObjToInt(actDetailsMap.get("DEPOSIT_PERIOD_DD")); 
        }if(actDetailsMap.get("DEPOSIT_PERIOD_YY") != null){
           sctDepoPeriodYr = CommonUtil.convertObjToInt(actDetailsMap.get("DEPOSIT_PERIOD_YY"));
        }if(actDetailsMap.get("DEPOSIT_PERIOD_MM") != null){
            sctDepoPeriodMon = CommonUtil.convertObjToInt(actDetailsMap.get("DEPOSIT_PERIOD_MM")); 
        }      
        sctDepoPeriod = sctDepoPeriodMon + (sctDepoPeriodYr * 12);
        System.out.println("sctDepoPeriod :: " + sctDepoPeriod);             
      //  setDepositPeriodWK(CommonUtil.convertObjToStr(sctDepoPeriod));
        maturityAmount = actDepositAmount * sctDepoPeriod ;
         System.out.println("maturityAmount :: "+ maturityAmount +"actClearBal " + actClearBal);
        if(depoStatus.equals(CommonConstants.NORMAL_CLOSURE)){           
            if(actClearBal < maturityAmount){ // if full amount not paid
              // In this case interest recovery [ either percentage or absolute ]
                if (isInterestRecovery.equalsIgnoreCase("Y")) {
                    double commissionAmt = 0;
                    if (intRecType.equalsIgnoreCase("Absolute")) {
                        //recInt = intRecRate;
                        commissionAmt = intRecRate;
                    } else if (intRecType.equalsIgnoreCase("Percent")) {
                        actDetailMap.put("INT_RATE", intRecRate);
                        List intRecList = ClientUtil.executeQuery("getPercentWiseIntRecoveryAmtForGroupDeposit", actDetailMap);
                        HashMap intRecLstMap = (HashMap) intRecList.get(0);
                        //recInt = CommonUtil.convertObjToDouble(intRecLstMap.get("RECOVERY_AMOUNT"));
                        commissionAmt = CommonUtil.convertObjToDouble(intRecLstMap.get("RECOVERY_AMOUNT"));
                    }
                    //setGroupDepositRecInt(CommonUtil.convertObjToStr(recInt));
                    setGroupDepositRecInt(CommonUtil.convertObjToStr(commissionAmt));
                }               
                if (isIntPayIfFullAmountNotPaid.equalsIgnoreCase("Y")) {// Only percentage wise calculation
                    actDetailMap.put("INT_RATE", IntRateIfFullAmountNotPaid);
                    actDetailMap.put("CURRDT", curDate.clone()); // Added by nithya on 08-04-2020 for KD-1767
                    List intCalcist = ClientUtil.executeQuery("getPercentWiseInterestForGroupDeposit", actDetailMap);
                    HashMap intRecLstMap = (HashMap) intCalcist.get(0);
                    interest = CommonUtil.convertObjToDouble(intRecLstMap.get("INTEREST_AMOUNT"));
                }              
            }else{ //if full amount paid
               // In this case interest given [ either percentage or absolute ]   
                if(intCalcType.equalsIgnoreCase("Absolute")){
                    interest = intCalcRate ;
                }else if(intCalcType.equalsIgnoreCase("Percent")){
                    // getPercentWiseInterestForGroupDeposit
                     actDetailMap.put("INT_RATE",intCalcRate);
                     actDetailMap.put("CURRDT", curDate.clone()); // Added by nithya on 08-04-2020 for KD-1767
                     List intCalcist = ClientUtil.executeQuery("getPercentWiseInterestForGroupDeposit", actDetailMap);                  
                     HashMap intRecLstMap = (HashMap)intCalcist.get(0);
                     interest = CommonUtil.convertObjToDouble(intRecLstMap.get("INTEREST_AMOUNT"));
                }
            }
        }        
         System.out.println("interest :: " + interest); 
         System.out.println("recInt :: " + recInt); 
         this.setIntCr(CommonUtil.convertObjToStr(recInt));
         setClosingIntDb(CommonUtil.convertObjToStr(interest - recInt));
         //rec_recivable = CommonUtil.convertObjToStr(recInt);
         System.out.println("interest--------- :" + interest + " recIntVal: " + recIntVal);
         return (interest - recInt);
    }

    public String getGroupDepositProd() {
        return groupDepositProd;
    }

    public void setGroupDepositProd(String groupDepositProd) {
        this.groupDepositProd = groupDepositProd;
    }
    
    public String getGroupDepositRecInt() {
        return groupDepositRecInt;
    }

    public void setGroupDepositRecInt(String groupDepositRecInt) {
        this.groupDepositRecInt = groupDepositRecInt;
    }
    
    // End
    
   // Addded by nithya on 16-06-2018 for calculating ServiceTax/GST implementation for charges and agent commission
    public HashMap checkServiceTaxApplicable(String accheadId) {
        HashMap checkForTaxMap = new HashMap();
        if (TrueTransactMain.SERVICE_TAX_REQ.equals("Y")) {
            HashMap whereMap = new HashMap();
            whereMap.put("AC_HD_ID", accheadId);
            List accHeadList = ClientUtil.executeQuery("getCheckServiceTaxApplicableForShare", whereMap);                  
            if (accHeadList != null && accHeadList.size() > 0) {
                HashMap accHeadMap = (HashMap) accHeadList.get(0);
                if (accHeadMap != null && accHeadMap.containsKey("SERVICE_TAX_APPLICABLE")&& accHeadMap.containsKey("SERVICE_TAX_ID")) {
                    checkForTaxMap.put("SERVICE_TAX_APPLICABLE",accHeadMap.get("SERVICE_TAX_APPLICABLE"));
                    checkForTaxMap.put("SERVICE_TAX_ID",accHeadMap.get("SERVICE_TAX_ID"));
                }
            }
        }
        return checkForTaxMap;
    }
    //Added b by Rishad 20-Feb-2019 for serice and gst calculation 
   public List calcServiceTaxAmount(String accNum, String prodId) throws Exception {
        HashMap whereMap = new HashMap();
        whereMap.put("ACT_NUM", accNum);
        List chargeAmtList = ClientUtil.executeQuery("getChargeDetails", whereMap);
        double taxAmt = 0;
        HashMap taxMap;
        List taxSettingsList = new ArrayList();
        if (chargeAmtList != null && chargeAmtList.size() > 0) {
            String checkFlag = "N";
            whereMap = new HashMap();
            HashMap checkForTaxMap = new HashMap();
            whereMap.put("value", prodId);
            List accHeadList = ClientUtil.executeQuery("getSelectLoanProductAccHeadTO", whereMap);
            if (accHeadList != null && accHeadList.size() > 0) {
                for (int i = 0; i < chargeAmtList.size(); i++) {
                    HashMap chargeMap = (HashMap) chargeAmtList.get(i);
                    if (chargeMap != null && chargeMap.size() > 0) {

                        String accId = "";

                        LoanProductAccHeadTO accHeadObj = (LoanProductAccHeadTO) accHeadList.get(0);
                        String chargetype = "";
                        if (chargeMap.containsKey("CHARGE_TYPE")) {
                            chargetype = CommonUtil.convertObjToStr(chargeMap.get("CHARGE_TYPE"));
                        }
                        if (chargetype != null && chargetype.equals("EP CHARGE")) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getEpCost());
                        }
                        if (chargetype != null && chargetype.equals("ARC CHARGE")) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getArcCost());
                        }
                        if (chargetype != null && chargetype.equals("MISCELLANEOUS CHARGES")) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getMiscServChrg());
                        }
                        if (chargetype != null && chargetype.equals("POSTAGE CHARGES")) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getPostageCharges());
                        }
                        if (chargetype != null && chargetype.equals("ADVERTISE CHARGES")) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getAdvertisementHead());
                        }
                        if (chargetype != null && chargetype.equals("ARBITRARY CHARGES")) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getArbitraryCharges());
                        }
                        if (chargetype != null && chargetype.equals("LEGAL CHARGES")) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getLegalCharges());
                        }
                        if (chargetype != null && chargetype.equals("NOTICE CHARGES")) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getNoticeCharges());
                        }
                        if (chargetype != null && chargetype.equals("INSURANCE CHARGES")) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getInsuranceCharges());
                        }
                        if (chargetype != null && chargetype.equals("EXECUTION DECREE CHARGES")) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getExecutionDecreeCharges());
                        }
                        if (chargetype != null && chargetype.equals("OTHER CHARGES")) {
                            accId = CommonUtil.convertObjToStr(accHeadObj.getOthrChrgsHead());
                        }
                        checkForTaxMap = checkServiceTaxApplicable(accId);
                        if(checkForTaxMap.containsKey("SERVICE_TAX_APPLICABLE") && checkForTaxMap.get("SERVICE_TAX_APPLICABLE") != null && CommonUtil.convertObjToStr(checkForTaxMap.get("SERVICE_TAX_APPLICABLE")).equalsIgnoreCase("Y")){
                            if(checkForTaxMap.containsKey("SERVICE_TAX_ID") && checkForTaxMap.get("SERVICE_TAX_ID") != null && CommonUtil.convertObjToStr(checkForTaxMap.get("SERVICE_TAX_ID")).length() > 0){
                                if(CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_AMT")) > 0){
                                   taxMap = new HashMap();
                                   taxMap.put("SETTINGS_ID",checkForTaxMap.get("SERVICE_TAX_ID"));
                                   taxMap.put(ServiceTaxCalculation.TOT_AMOUNT,chargeMap.get("CHARGE_AMT"));
                                   taxSettingsList.add(taxMap);   
                                }                                     
                            }
                        }
                    }
                }
            }
        }
        return taxSettingsList;
    }
   
    public String getIsSlabWiseDailyDeposit() {
        return isSlabWiseDailyDeposit;
    }

    public void setIsSlabWiseDailyDeposit(String isSlabWiseDailyDeposit) {
        this.isSlabWiseDailyDeposit = isSlabWiseDailyDeposit;
    }    
    
    private void setClosingIntRateRD(List lst, HashMap paramMap) {
        //select GET_RD_CLOSING_INT_RATE('0001110000490','110','NORMAL','0001') from dual
        double rateOfInt = 0.0;
        HashMap roiParamMap = new HashMap();
        roiParamMap.put("DEPOSIT_NO", this.getTxtDepositNo());
        roiParamMap.put("PROD_ID", getProdID());
        roiParamMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
        if (getLblClosingType().equals(CommonConstants.PREMATURE_CLOSURE)) {
            roiParamMap.put("CLOSURE_TYPE", "PRECLOSE");
        } else {
            roiParamMap.put("CLOSURE_TYPE", "NORMAL");
        }
        if (roiParamMap.get("CLOSURE_TYPE").equals("NORMAL") && paramMap.containsKey("INT_APPLY_FOR_IRREGULAR_RD") && paramMap.get("INT_APPLY_FOR_IRREGULAR_RD") != null && CommonUtil.convertObjToStr(paramMap.get("INT_APPLY_FOR_IRREGULAR_RD")).equalsIgnoreCase("N")) {// Added by nithya on 27-03-2020 for KD-1535
            rateOfInt = 0.0;
            System.out.println("rate of int :: " + rateOfInt);
        } else {
            List roiLst = ClientUtil.executeQuery("getRDClosingIntRate", roiParamMap);
            if (roiLst != null && roiLst.size() > 0) {
                HashMap roiResultMap = (HashMap) roiLst.get(0);
                if (roiResultMap.containsKey("RATE_OF_INT") && roiResultMap.get("RATE_OF_INT") != null) {
                    rateOfInt = CommonUtil.convertObjToDouble(roiResultMap.get("RATE_OF_INT"));
                }
            }
        }
        setPrematureClosingRate(String.valueOf(rateOfInt));
        this.setPenaltyPenalRate(String.valueOf(0.0));
        this.setRateApplicable(String.valueOf(rateOfInt));
    }

    public String getSpecialRDCompleted() {
        return specialRDCompleted;
    }

    public void setSpecialRDCompleted(String specialRDCompleted) {
        this.specialRDCompleted = specialRDCompleted;
    }

    public String getDailyDepositLoanPreClose() {
        return dailyDepositLoanPreClose;
    }

    public void setDailyDepositLoanPreClose(String dailyDepositLoanPreClose) {
        this.dailyDepositLoanPreClose = dailyDepositLoanPreClose;
    }

    public String getDailyDepositLoanPreCloseROI() {
        return dailyDepositLoanPreCloseROI;
    }

    public void setDailyDepositLoanPreCloseROI(String dailyDepositLoanPreCloseROI) {
        this.dailyDepositLoanPreCloseROI = dailyDepositLoanPreCloseROI;
    }
   
}
