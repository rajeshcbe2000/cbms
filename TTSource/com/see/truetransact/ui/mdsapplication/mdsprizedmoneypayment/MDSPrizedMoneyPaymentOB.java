/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * MDSPrizedMoneyPaymentOB.java
 *
 * Created on Wed Jun 22 13:15:16 IST 2011
 */
package com.see.truetransact.ui.mdsapplication.mdsprizedmoneypayment;

import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.apache.log4j.Logger;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientproxy.ProxyFactory;
//import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.ui.common.transaction.TransactionOB;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.transferobject.mdsapplication.mdsprizedmoneypayment.MDSPrizedMoneyPaymentTO;
import com.see.truetransact.transferobject.servicetax.servicetaxdetails.ServiceTaxDetailsTO;
import com.see.truetransact.ui.common.servicetax.ServiceTaxCalculation;

/**
 *
 * @author
 */
public class MDSPrizedMoneyPaymentOB extends CObservable {

    private String txtSchemeName = "";
    private Integer txtDivisionNo = 0;  //AJITH
    private Integer txtSubNo = 0;  //AJITH
    private Integer txtTotalInst = 0;  //AJITH
    private Integer txtNoOfInstPaid = 0;  //AJITH
    private Double txtCommisionAmt = 0.0; //AJITH
    private Double txtBonusAmt = 0.0; //AJITH
    private Double txtPenalAmt = 0.0; //AJITH
    private Double txtBonusRecovered = 0.0; //AJITH
    private Integer txtPrizedInstNo = 0;  //AJITH
    private Double txtOverDueAmt = 0.0; //AJITH
    private Double txtNetAmt = 0.0; //AJITH
    private Double txtAribitrationAmt = 0.0; //AJITH
    private Double txtDiscountAmt = 0.0; //AJITH
    private Double txtPrizedAmt = 0.0; //AJITH
    private Double txtNoticeAmt = 0.0; //AJITH
    private Double txtChargeAmount = 0.0; //AJITH
    private Double txtAmountRefunded = 0.0; //AJITH
    private String txtTotalAmtPaid = "";
    private Integer txtOverdueInst = 0; //AJITH
    private String tdtDrawDate = "";
    private String txtChittalNo = "";
    private String txtMemberNo = "";
    private String txtApplicantsName = "";
    private String cboTransType = "";
    private String txtTransactionAmt = "";
    private String txtTransProductId = "";
    private String cboProductType = "";
    private String txtDebitAccNo = "";
    private String tdtChequeDate = "";
    private String txtChequeNo = "";
    private String txtChequeNo2 = "";
    private String cboInstrumentType = "";
    private String txtTokenNo = "";
    private boolean chkThalayal = false;
    private boolean chkMunnal = false;
    private boolean chkChangedBetween = false;
    private String lblMemberNameVal = "";
    private String transId = "";
    private String cashId = "";
    private String lblHouseStNo = "";
    private String lblArea = "";
    private String lblCity = "";
    private String lblState = "";
    private String lblpin = "";
    private String rdoDefaulters = "";
    private String instAmount = "";
    private String defaulter = "";
    private HashMap serviceTax_Map=null;
    private String lblServiceTaxval="";
    private Date currDt = null;
    private String auctionTrans = "";
    private double pendingBonus = 0.0;
    private double pendingInstallment = 0.0;
    private boolean bounsCommisionTransfer = false;
    private boolean partPay = false;
    private int installmentDay ;
    private Double txtChitalBonus = 0.0;   //AJITH
    private String forfietDefaulterBonus = "";
    private boolean rdoPrizedMember_Yes = false;
    private boolean rdoPrizedMember_No = false;
    private double defaulterReceiptTaxAmt = 0.0; // Added by nithya on 22-11-2019 for KD-910
    private double paymentTaxAmt = 0.0;
    private Double defaulter_Interest = 0.0;   //AJITH
    private Double defaulter_Commission = 0.0;   //AJITH
    private Double defaulter_Bonus_recoverd = 0.0;   //AJITH    
    private double auctionTransCommissionTaxAmount = 0.0;

    public boolean getRdoPrizedMember_No() {
        return rdoPrizedMember_No;
    }

    public void setRdoPrizedMember_No(boolean rdoPrizedMember_No) {
        this.rdoPrizedMember_No = rdoPrizedMember_No;
    }

    public boolean getRdoPrizedMember_Yes() {
        return rdoPrizedMember_Yes;
    }

    public void setRdoPrizedMember_Yes(boolean rdoPrizedMember_Yes) {
        this.rdoPrizedMember_Yes = rdoPrizedMember_Yes;
    }

    public String getForfietDefaulterBonus() {
        return forfietDefaulterBonus;
    }

    public void setForfietDefaulterBonus(String forfietDefaulterBonus) {
        this.forfietDefaulterBonus = forfietDefaulterBonus;
    }

    public Double getTxtChitalBonus() {
        return txtChitalBonus;
    }

    public void setTxtChitalBonus(Double txtChitalBonus) {
        this.txtChitalBonus = txtChitalBonus;
    }

    public int getInstallmentDay() {
        return installmentDay;
    }

    public void setInstallmentDay(int installmentDay) {
        this.installmentDay = installmentDay;
    }

    public boolean isPartPay() {
        return partPay;
    }

    public void setPartPay(boolean partPay) {
        this.partPay = partPay;
    }
    

    public boolean getBounsCommisionTransfer() {
        return bounsCommisionTransfer;
    }

    public void setBounsCommisionTransfer(boolean bounsCommisionTransfer) {
        this.bounsCommisionTransfer = bounsCommisionTransfer;
    }

    public double getPendingInstallment() {
        return pendingInstallment;
    }

    public void setPendingInstallment(double pendingInstallment) {
        this.pendingInstallment = pendingInstallment;
    }

    public double getPendingBonus() {
        return pendingBonus;
    }

    public void setPendingBonus(double pendingBonus) {
        this.pendingBonus = pendingBonus;
    }

    public String getAuctionTrans() {
        return auctionTrans;
    }

    public void setAuctionTrans(String auctionTrans) {
        this.auctionTrans = auctionTrans;
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
    public String getDefaulter() {
        return defaulter;
    }

    public void setDefaulter(String defaulter) {
        this.defaulter = defaulter;
    }

    public Double getDefaulter_Interest() {
        return defaulter_Interest;
    }

    public void setDefaulter_Interest(Double defaulter_Interest) {
        this.defaulter_Interest = defaulter_Interest;
    }

    public Double getDefaulter_Commission() {
        return defaulter_Commission;
    }

    public void setDefaulter_Commission(Double defaulter_Commission) {
        this.defaulter_Commission = defaulter_Commission;
    }

    public Double getDefaulter_Bonus_recoverd() {
        return defaulter_Bonus_recoverd;
    }

    public void setDefaulter_Bonus_recoverd(Double defaulter_Bonus_recoverd) {
        this.defaulter_Bonus_recoverd = defaulter_Bonus_recoverd;
    }


    private HashMap oldTransDetMap = new HashMap();
    private ProxyFactory proxy;
    private HashMap map;
    private final static Logger log = Logger.getLogger(MDSPrizedMoneyPaymentOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private HashMap _authorizeMap;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private int result;
    private HashMap lookupMap;
    private HashMap param;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private int actionType;
    final ArrayList tableTitle = new ArrayList();
    private ArrayList IncVal = new ArrayList();
    private EnhancedTableModel tblChangedDetails;
    private MDSPrizedMoneyPaymentTO objMoneyPaymentTO;
    private TransactionOB transactionOB;
    private LinkedHashMap allowedTransactionDetailsTO = null;
    private LinkedHashMap transactionDetailsTO = null;
    private final String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs";
    private boolean closureFlag = false;

    public MDSPrizedMoneyPaymentOB() {
        try {
            currDt = ClientUtil.getCurrentDate();
            proxy = ProxyFactory.createProxy();
            map = new HashMap();
            map.put(CommonConstants.JNDI, "MDSPrizedMoneyPaymentJNDI");
            map.put(CommonConstants.HOME, "MDSPrizedMoneyPaymentHome");
            map.put(CommonConstants.REMOTE, "MDSPrizedMoneyPayment");
            setTableTile();
            tblChangedDetails = new EnhancedTableModel(null, tableTitle);
            System.out.println("getSelectedBranchID()Y$$&&$&$11111"+getSelectedBranchID());
            //            fillDropdown();
        } catch (Exception e) {
            parseException.logException(e, true);
        }
    }

    private void setTableTile() throws Exception {
        tableTitle.add("Earlier Member No");
        tableTitle.add("Earlier Member Name");
        tableTitle.add("Changed From InstNo");
        tableTitle.add("Changed Date");
        IncVal = new ArrayList();
    }

    public void setChangedMemberTableData() {
        HashMap whereMap = new HashMap();
        ArrayList rowList = new ArrayList();
        ArrayList singleList = new ArrayList();
        whereMap.put("CHITTAL_NO", getTxtChittalNo());
        List list = ClientUtil.executeQuery("getSelectChangedMemberDetails", whereMap);
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                whereMap = (HashMap) list.get(i);
                rowList = new ArrayList();
                rowList.add(whereMap.get("OLD_MEMBER_NO"));
                rowList.add(whereMap.get("OLD_MEMBER_NAME"));
                rowList.add(whereMap.get("INSTALLMENT_NO"));
                rowList.add(whereMap.get("CHANGE_EFFECTIVE_DATE"));
                singleList.add(rowList);
            }
            tblChangedDetails = new EnhancedTableModel((ArrayList) singleList, tableTitle);
        }
    }

    public void resetTableValues() {
        tblChangedDetails.setDataArrayList(null, tableTitle);
        setRdoDefaulters("");
        setInstAmount("");
        setBounsCommisionTransfer(false);
    }

    /**
     * To retrieve a particular customer's accountclosing record
     */
    public void getData(HashMap whereMap) {
        try {
            final HashMap data = (HashMap) proxy.executeQuery(whereMap, map);
            System.out.println("#@@%@@#%@#data" + data);
            objMoneyPaymentTO = (MDSPrizedMoneyPaymentTO) ((List) data.get("MDSPrizedMoneyPaymentTO")).get(0);
            populateMoneyPaymentData(objMoneyPaymentTO);
            if (data.containsKey("TransactionTO")) {
                List lst = (List) data.get("TransactionTO");
                System.out.println("########## lst : " + lst);
                transactionOB.setDetails(lst);
                //                setAllowedTransactionDetailsTO(transactionOB.getAllowedTransactionDetailsTO());
            }
            if (data != null && data.size() > 0) {
                if (data.containsKey("BONUS_AMT_TRANSACTION_TRANSFER")) {
                    oldTransDetMap.put("BONUS_AMT_TRANSACTION_TRANSFER", data.get("BONUS_AMT_TRANSACTION_TRANSFER"));
                }
                if (data.containsKey("BONUS_AMT_TRANSACTION_CASH")) {
                    oldTransDetMap.put("BONUS_AMT_TRANSACTION_CASH", data.get("BONUS_AMT_TRANSACTION_CASH"));
                }
//                if (data.containsKey("NOTICE_AMT_TRANSACTION"))
//                    oldTransDetMap.put("NOTICE_AMT_TRANSACTION", data.get("NOTICE_AMT_TRANSACTION"));
//                if (data.containsKey("ARBITRATION_AMT_TRANSACTION"))
//                    oldTransDetMap.put("ARBITRATION_AMT_TRANSACTION", data.get("ARBITRATION_AMT_TRANSACTION"));
//                if (data.containsKey("DISCOUNT_AMT_TRANSACTION"))
//                    oldTransDetMap.put("DISCOUNT_AMT_TRANSACTION", data.get("DISCOUNT_AMT_TRANSACTION"));
            }
            //lst = null;
        } catch (Exception e) {
            parseException.logException(e, true);
            e.printStackTrace();
        }
    }

    private void populateMoneyPaymentData(MDSPrizedMoneyPaymentTO objPaymentTO) throws Exception {
        this.setTxtSchemeName(CommonUtil.convertObjToStr(objPaymentTO.getSchemeName()));
        this.setTxtChittalNo(CommonUtil.convertObjToStr(objPaymentTO.getChittalNo()));
        this.setTxtDivisionNo(objPaymentTO.getDivisionNo());    //AJITH
        this.setTxtSubNo(objPaymentTO.getSubNo());  //AJITH
        this.setTdtDrawDate(CommonUtil.convertObjToStr(objPaymentTO.getDrawAuctionDate()));
        this.setTxtTotalInst(objPaymentTO.getTotalInstallments());  //AJITH
        this.setTxtNoOfInstPaid(objPaymentTO.getNoOfInstPaid());    //AJITH
        this.setTxtOverdueInst(objPaymentTO.getNoOfOverdueInst());  //AJITH
        this.setTxtMemberNo(CommonUtil.convertObjToStr(objPaymentTO.getMemberNo()));
        this.setLblMemberNameVal(CommonUtil.convertObjToStr(objPaymentTO.getMemberName()));
        this.setTxtOverDueAmt(objPaymentTO.getOverdueAmount()); //AJITH
        this.setTxtPrizedInstNo(objPaymentTO.getPrizedInstNo());    //AJITH
        this.setTxtBonusAmt(objPaymentTO.getBonusAmount()); //AJITH
        this.setTxtBonusRecovered(objPaymentTO.getBonusRecovered());    //AJITH
        this.setTxtPenalAmt(objPaymentTO.getPenalAmount()); //AJITH
        this.setTxtCommisionAmt(objPaymentTO.getCommisionAmount()); //AJITH
        this.setTxtAmountRefunded(objPaymentTO.getRefundAmount());  //AJITH
        this.setTxtDiscountAmt(objPaymentTO.getDiscountAmount());   //AJITH
        this.setTxtNoticeAmt(objPaymentTO.getNoticeAmount());   //AJITH
        this.setTxtChargeAmount(objPaymentTO.getChargeAmount());    //AJITH
        this.setTxtAribitrationAmt(objPaymentTO.getAribitrationAmount());   //AJITH
        this.setTxtPrizedAmt(objPaymentTO.getPrizedAmount());   //AJITH
        this.setTxtNetAmt(objPaymentTO.getNetAmount()); //AJITH
        this.setRdoDefaulters(CommonUtil.convertObjToStr(objPaymentTO.getDefaulters()));
        this.setTxtChitalBonus(objPaymentTO.getChitalBonusAmt());   //AJITH
        this.setPaymentTaxAmt(objPaymentTO.getPaymentTaxAmt()); // Added by nithya on 22-11-2019 for KD-910
        this.setDefaulterReceiptTaxAmt(objPaymentTO.getDefaulterReceiptTaxAmt());
        setChanged();
        notifyObservers();
    }

    public void setReceiptDetails(HashMap map) {
        HashMap chittalMap = new HashMap();
        chittalMap.put("CHITTAL_NO", getTxtChittalNo());
        chittalMap.put("SUB_NO", CommonUtil.convertObjToInt(getTxtSubNo()));    //AJITH
        List lst = ClientUtil.executeQuery("getSelctApplnReceiptDetails", chittalMap);
        if (lst != null && lst.size() > 0) {
            chittalMap = (HashMap) lst.get(0);
            int instFreq = 0;
            setTxtDivisionNo(CommonUtil.convertObjToInt(chittalMap.get("DIVISION_NO")));    //AJITH
            setTxtChittalNo(CommonUtil.convertObjToStr(chittalMap.get("CHITTAL_NO")));
            setTxtTotalInst(CommonUtil.convertObjToInt(chittalMap.get("NO_OF_INSTALLMENTS")));  //AJITH
            instFreq = CommonUtil.convertObjToInt(chittalMap.get("INSTALLMENT_FREQUENCY"));
            setInstAmount(CommonUtil.convertObjToStr(chittalMap.get("INST_AMT")));
            //            setTxtNoOfInstPaid(CommonUtil.convertObjToStr(chittalMap.get("INST_COUNT")));
            //            double  overDueAmt = (CommonUtil.convertObjToDouble(chittalMap.get("NO_OF_INSTALLMENTS")).doubleValue() -
            //            CommonUtil.convertObjToDouble(chittalMap.get("INST_COUNT")).doubleValue()) * CommonUtil.convertObjToDouble(chittalMap.get("INST_AMT")).doubleValue();
            //            setTxtOverDueAmt(String.valueOf(overDueAmt));
            //            setTxtOverdueInst(String.valueOf(CommonUtil.convertObjToLong(chittalMap.get("NO_OF_INSTALLMENTS")) - CommonUtil.convertObjToLong(chittalMap.get("INST_COUNT"))));
            int instDay = CommonUtil.convertObjToInt(chittalMap.get("INSTALLMENT_DAY"));
            setInstallmentDay(instDay);
            Date startDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(chittalMap.get("SCHEME_START_DT")));
            Date endDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(chittalMap.get("CHIT_END_DT")));
            System.out.println("###### endDate : " + endDate);
            Date currDate = (Date) currDt.clone();
            //Added By Suresh
            if (DateUtil.dateDiff(endDate, currDate) > 0) {
                currDate = endDate;
            }
            int stYear = startDate.getYear() + 1900;
            int currYear = currDate.getYear() + 1900;
            int stMonth = startDate.getMonth();
            int currMonth = currDate.getMonth();
            int value = 0;
            int pending = 0;
            long count = 0;
            int totInst = CommonUtil.convertObjToInt(chittalMap.get("NO_OF_INSTALLMENTS"));
            int pendingInst = 0;
            HashMap instMap = new HashMap();
            instMap.put("CHITTAL_NO", getTxtChittalNo());
            instMap.put("SUB_NO", CommonUtil.convertObjToInt(getTxtSubNo()));   //AJITH
            List instLst = ClientUtil.executeQuery("getNoOfInstalmentsPaid", instMap);
            if (instLst != null && instLst.size() > 0) {
                instMap = (HashMap) instLst.get(0);
                setTxtNoOfInstPaid(CommonUtil.convertObjToInt(instMap.get("NO_OF_INST")));  //AJITH
                count = CommonUtil.convertObjToInt(instMap.get("NO_OF_INST"));
                pendingInst = (int) (totInst-count);
                instMap.put("PENDING_INST", pendingInst);
                instMap.put("SCHEME_NAME", getTxtSchemeName());
               // System.out.println("scccccccccccccc"+getTxtSchemeName());
                List pendList = ClientUtil.executeQuery("getChittalPendingInstAmount", instMap);
                if (pendList != null && pendList.size() > 0) {
                    setPendingBonus(CommonUtil.convertObjToDouble(((HashMap)pendList.get(0)).get("TOTAL_BONUS")));
                    setPendingInstallment(CommonUtil.convertObjToDouble(pendingInst));
                   // System.out.println("getinsttttt"+getInstAmount());
                }
            }
            if (instFreq != 7) {
                if (stYear == currYear && stMonth == currMonth) {
                    pending = 0;
                    setTxtOverdueInst(0);   //AJITH
                } else if (stYear == currYear && stMonth != currMonth) {
                    //Added By Suresh
                    int diffMonth = currMonth - stMonth - 1;
                    //                int diffMonth = currMonth - stMonth;
                    int pendingVal = diffMonth - (int) count;
                    //Changed By Suresh
                    if (instDay < currDate.getDate()) {
//                        pending = pendingVal + 1;
                        pending = pendingVal;
                    } else {
                        pending = pendingVal;
                    }
                    if (pending > 0) {
                        setTxtOverdueInst(pending); //AJITH
                    } else {
                        setTxtOverdueInst(0);   //AJITH
                    }
                } else {
                    int year = currYear - stYear;
                    value = (year * 12) + currMonth - stMonth;
                    //Changed By Suresh
                    int pendingVal = value - (int) count;
                    if (instDay < currDate.getDate()) {
//                        pending = pendingVal + 1;
                        pending = pendingVal;
                    } else {
                        pending = pendingVal;
                    }
                    if (pending > 0) {
                        setTxtOverdueInst(pending); //AJITH
                    } else {
                        setTxtOverdueInst(0);   //AJITH
                    }
                }
                // Added by nithya on 21-03-2019 for KD-453 Mds payment time ,penal amount issue (defaulter )
                HashMap insDateMap = new HashMap();
                insDateMap.put("DIVISION_NO", CommonUtil.convertObjToInt(getTxtDivisionNo()));  //AJITH
                insDateMap.put("SCHEME_NAME", getTxtSchemeName());
                insDateMap.put("CURR_DATE", currDate.clone());
                insDateMap.put("CURR_DATE", currDt.clone());//KD-3602
                insDateMap.put("ADD_MONTHS", CommonUtil.convertObjToInt("-1"));
                List insDateLst = ClientUtil.executeQuery("getMDSCurrentInsDate", insDateMap);                
                if (insDateLst != null && insDateLst.size() > 0) {
                    insDateMap = (HashMap) insDateLst.get(0);
                    int pendngInstallment = 0;
                    String advance_Collection = "";
                    HashMap prodMap = new HashMap();
                    prodMap.put("SCHEME_NAME", getTxtSchemeName());
                    List prodLst = ClientUtil.executeQuery("getSelectSchemeAcctHead", prodMap);
                    if (prodLst != null && prodLst.size() > 0) {
                        prodMap = (HashMap) prodLst.get(0);
                        advance_Collection = CommonUtil.convertObjToStr(prodMap.get("ADVANCE_COLLECTION"));
                    }
                    if (advance_Collection.equals("Y") /*
                             * || CommonUtil.convertObjToStr(prodMap.get("PREDEFINITION_INSTALLMENT")).equals("Y")
                             */) {
                        pendngInstallment = CommonUtil.convertObjToInt(insDateMap.get("INSTALLMENT_NO")) - (int) count - 1;
                        System.out.println("pendingInst-0----" + pendngInstallment);
                    } else {
                        pendngInstallment = CommonUtil.convertObjToInt(insDateMap.get("INSTALLMENT_NO")) - (int) count;
                    }
                    //System.out.println("count-----" + count);
                    //System.out.println("pendingInst-----" + pendngInstallment);
                    if (pendngInstallment > 0) {
                        setTxtOverdueInst(pendngInstallment);   //AJITH
                    } else {
                        setTxtOverdueInst(0);   //AJITH
                    }
                } 
                // End of KD 453
            } else {
                //Weekly Frequency
                long diffDays = 0;
                int weekly = 0;
                diffDays = DateUtil.dateDiff(startDate, currDate);
                System.out.println("######## diffDays: " + diffDays);
                if (diffDays > 0) {
                    weekly = (int) diffDays / 7;
                    int pendingVal = weekly - (int) count;
                    if (instDay < currDate.getDate()) {
                        pending = pendingVal + 1;
                    } else {
                        pending = pendingVal;
                    }
                }
                if (pending > 0) {
                    setTxtOverdueInst(pending); //AJITH
                    } else {
                        setTxtOverdueInst(0);   //AJITH
                }
            }
            
            setTxtOverDueAmt(CommonUtil.convertObjToDouble(getTxtOverdueInst()) * CommonUtil.convertObjToDouble(chittalMap.get("INST_AMT")));   //AJITH
            setOtherDetails();
        }
    }

    public void setOtherDetails() {
        HashMap MdsDetailsMap = new HashMap();
        MdsDetailsMap.put("CHITTAL_NO", getTxtChittalNo());
        MdsDetailsMap.put("SCHEME_NAME", getTxtSchemeName());
        List lst = ClientUtil.executeQuery("getSelectChangedRecordDetails", MdsDetailsMap);
        if (lst != null && lst.size() > 0) {
            MdsDetailsMap = (HashMap) lst.get(0);
            if (MdsDetailsMap.get("THALAYAL") != null && MdsDetailsMap.get("THALAYAL").equals("Y")) {
                setChkThalayal(true);
            } else {
                setChkThalayal(false);
            }
            if (MdsDetailsMap.get("MUNNAL") != null && MdsDetailsMap.get("MUNNAL").equals("Y")) {
                setChkMunnal(true);
            } else {
                setChkMunnal(false);
            }
            setChkChangedBetween(true);
        } else {
            setChkMunnal(false);
            setChkThalayal(false);
            setChkChangedBetween(false);
        }
    }

    public void setClosureDetails(boolean flag) {
        closureFlag = flag;
    }

    /**
     * To perform the necessary operation
     */
    public void doAction() {
        try {
            if (actionType != ClientConstants.ACTIONTYPE_CANCEL || get_authorizeMap() != null) {
                doActionPerform();
            }
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e, true);
        }
    }

    /**
     * To perform the necessary action
     */
    private void doActionPerform() throws Exception {
        TTException exception = null;
        try {
            final HashMap data = new HashMap();
            if (get_authorizeMap() == null) {
                data.put("MDSPrizedMoneyPaymentData", setMoneyPaymentData());
                if(CommonUtil.convertObjToStr(getRdoDefaulters()).equalsIgnoreCase("Y")){
                    data.put("DEFAULTER_BONUS_FORFIET",getForfietDefaulterBonus());
                }
                if (getTxtTotalAmtPaid().length() > 0) {
                    data.put("REFUND_AMOUNT", getTxtTotalAmtPaid());
                }
                //Added by sreekrishnan
                if(getAuctionTrans()!=null && getAuctionTrans().equals("Y")){
                    data.put("AUCTION_TRANS", getAuctionTrans());
                }
                //Added by sreekrishnan
                if(getBounsCommisionTransfer()){
                    data.put("TRASFER_BONUS_COMMISION", "Y");
                }
                if(isPartPay()){
                    data.put("PART_PAY", "Y");
                }
            } else {
                data.put(CommonConstants.AUTHORIZEMAP, get_authorizeMap());
                data.put("TRANS_ID", getTransId());
                if (!CommonUtil.convertObjToStr(getCashId()).equals("")) {
                    data.put("CASH_ID", getCashId());
                }
                data.put("MDSPrizedMoneyPaymentData", setMoneyPaymentData());
                data.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                if (TrueTransactMain.SERVICE_TAX_REQ.equals("Y")) {
                    data.put("SERVICE_TAX_AUTH", "SERVICE_TAX_AUTH");
                }
            }
            if (allowedTransactionDetailsTO != null && allowedTransactionDetailsTO.size() > 0) {
                if (transactionDetailsTO == null) {
                    transactionDetailsTO = new LinkedHashMap();
                }
                transactionDetailsTO.put(NOT_DELETED_TRANS_TOs, allowedTransactionDetailsTO);
                data.put("TransactionTO", transactionDetailsTO);
                allowedTransactionDetailsTO = null;
            }
            if (getCommand().equals("DELETE") || getCommand().equals("EDIT") && data != null && data.size() > 0) {
                data.put("TRANS_ID", getTransId());
                if (!CommonUtil.convertObjToStr(getCashId()).equals("")) {
                    data.put("CASH_ID", getCashId());
                }
                if (oldTransDetMap != null && oldTransDetMap.size() > 0) {
                    if (oldTransDetMap.containsKey("BONUS_AMT_TRANSACTION_TRANSFER")) {
                        data.put("BONUS_AMT_TRANSACTION_TRANSFER", oldTransDetMap.get("BONUS_AMT_TRANSACTION_TRANSFER"));
                    }
                    if (oldTransDetMap.containsKey("BONUS_AMT_TRANSACTION_CASH")) {
                        data.put("BONUS_AMT_TRANSACTION_CASH", oldTransDetMap.get("BONUS_AMT_TRANSACTION_CASH"));
                    }
                }
            }
       //     System.out.println("#### closureFlag : " + closureFlag);
            if (closureFlag) {
                data.put("MDS_CLOSURE", "MDS_CLOSURE");
            }
            data.put("COMMAND", getCommand());
            data.put(CommonConstants.MODULE, getModule());
            data.put(CommonConstants.SCREEN, getScreen());
            data.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
            data.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            data.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            data.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
         //   System.out.println("getSelectedBranchID()Y$$&&$&$"+getSelectedBranchID());
            if (getActionType() == ClientConstants.ACTIONTYPE_NEW && getLblServiceTaxval() != null && CommonUtil.convertObjToDouble(getLblServiceTaxval()) > 0) {
                data.put("serviceTaxDetails", getServiceTax_Map());
                data.put("serviceTaxDetailsTO", setServiceTaxDetails());
            }
            if(getAuctionTrans().equals("Y")){
              data.put("AUCTION_COMMISSION_TAX",getAuctionTransCommissionTaxAmount());  
            }else{
              data.put("AUCTION_COMMISSION_TAX",0.0);     
            }
         //   System.out.println("Data in MoneyPaymentData OB : " + data);
            HashMap proxyResultMap = proxy.execute(data, map);
            setProxyReturnMap(proxyResultMap);
            _authorizeMap = null;
            setResult(getActionType());
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            //  e.printStackTrace();
            if (e instanceof TTException) {
                System.out.println("bbbb");
                exception = (TTException) e;
                if (e instanceof TTException) {
                    Object[] dialogOption1 = {"OK"};
                    parseException.setDialogOptions(dialogOption1);
                    exception = (TTException) e;
                    parseException.logException(exception, true);
                    MDSPrizedMoneyPaymentUI ui = new MDSPrizedMoneyPaymentUI();
                    System.out.println("mmmmmmmlllll");
                    ui.cancelAction();

                }
            }

        }

    }

    /**
     * To populate data into the screen
     */
    public MDSPrizedMoneyPaymentTO setMoneyPaymentData() {
        final MDSPrizedMoneyPaymentTO objPaymentTO = new MDSPrizedMoneyPaymentTO();
        try {
            objPaymentTO.setCommand(getCommand());
            objPaymentTO.setSchemeName(getTxtSchemeName());
            objPaymentTO.setChittalNo(getTxtChittalNo());
            objPaymentTO.setDivisionNo(getTxtDivisionNo());
            objPaymentTO.setSubNo(getTxtSubNo());
            objPaymentTO.setDrawAuctionDate(DateUtil.getDateMMDDYYYY(getTdtDrawDate()));
            objPaymentTO.setTotalInstallments(getTxtTotalInst());
            objPaymentTO.setNoOfInstPaid(getTxtNoOfInstPaid());
            objPaymentTO.setNoOfOverdueInst(getTxtOverdueInst());
            objPaymentTO.setMemberNo(getTxtMemberNo());
            objPaymentTO.setMemberName(getLblMemberNameVal());
            objPaymentTO.setOverdueAmount(getTxtOverDueAmt());
            objPaymentTO.setPrizedInstNo(getTxtPrizedInstNo());
            objPaymentTO.setBonusAmount(getTxtBonusAmt());
            objPaymentTO.setBonusRecovered(getTxtBonusRecovered());
            objPaymentTO.setPenalAmount(getTxtPenalAmt());
            objPaymentTO.setCommisionAmount(getTxtCommisionAmt());
            objPaymentTO.setRefundAmount(getTxtAmountRefunded());
            objPaymentTO.setDiscountAmount(getTxtDiscountAmt());
            objPaymentTO.setNoticeAmount(getTxtNoticeAmt());
            objPaymentTO.setChargeAmount(getTxtChargeAmount());
            objPaymentTO.setAribitrationAmount(getTxtAribitrationAmt());
            objPaymentTO.setPrizedAmount(getTxtPrizedAmt());
            objPaymentTO.setNetAmount(getTxtNetAmt());
            objPaymentTO.setStatus(getAction());
            objPaymentTO.setStatusBy(TrueTransactMain.USER_ID);
            objPaymentTO.setStatusDt(ClientUtil.getCurrentDateWithTime());
            objPaymentTO.setBranchCode(TrueTransactMain.BRANCH_ID);
            objPaymentTO.setDefaulters(getRdoDefaulters());
            objPaymentTO.setDefaulter_marked(getDefaulter());
            objPaymentTO.setDefaulter_bonus_recoverd(getDefaulter_Bonus_recoverd());
            objPaymentTO.setDefaulter_comm(getDefaulter_Commission());
            objPaymentTO.setDefaulter_interst(getDefaulter_Interest());
            objPaymentTO.setChitalBonusAmt(getTxtChitalBonus());
            objPaymentTO.setPaymentTaxAmt(getPaymentTaxAmt()); // Added by nithya on 22-11-2019 for KD-910
            objPaymentTO.setDefaulterReceiptTaxAmt(getDefaulterReceiptTaxAmt()); // Added by nithya on 22-11-2019 for KD-910
        } catch (Exception e) {
            log.info("Error In setMDSPrizedMoneyPaymentTOData()");
            e.printStackTrace();
        }
        return objPaymentTO;
    }

    private String getCommand() {
        String command = null;
        System.out.println("actionType : " + actionType);
        switch (actionType) {
            case ClientConstants.ACTIONTYPE_NEW:
                command = CommonConstants.TOSTATUS_INSERT;
                break;
            case ClientConstants.ACTIONTYPE_EDIT:
                command = CommonConstants.TOSTATUS_UPDATE;
                break;
            case ClientConstants.ACTIONTYPE_DELETE:
                command = CommonConstants.TOSTATUS_DELETE;
                break;
            case ClientConstants.ACTIONTYPE_AUTHORIZE:
                command = CommonConstants.STATUS_AUTHORIZED;
                break;
            case ClientConstants.ACTIONTYPE_REJECT:
                command = CommonConstants.STATUS_REJECTED;
                break;
            case ClientConstants.ACTIONTYPE_EXCEPTION:
                command = CommonConstants.STATUS_EXCEPTION;
            default:
        }
        // System.out.println("command : " + command);
        return command;
    }

    protected void getCustomerAddressDetails(String value) {
        HashMap custAddressMap = new HashMap();
        custAddressMap.put("SCHEME_NAME", getTxtSchemeName());
        custAddressMap.put("CHITTAL_NO", getTxtChittalNo());
        custAddressMap.put("SUB_NO", CommonUtil.convertObjToInt(getTxtSubNo()));    //AJITH
        List lst = ClientUtil.executeQuery("getCustomerAddressDetailsinAppln", custAddressMap);
        if (lst != null && lst.size() > 0) {
            custAddressMap = (HashMap) lst.get(0);
            setLblHouseStNo(CommonUtil.convertObjToStr(custAddressMap.get("HOUSE_ST")));
            setLblArea(CommonUtil.convertObjToStr(custAddressMap.get("AREA")));
            setLblCity(CommonUtil.convertObjToStr(custAddressMap.get("CITY")));
            setLblState(CommonUtil.convertObjToStr(custAddressMap.get("STATE")));
            setLblpin(CommonUtil.convertObjToStr(custAddressMap.get("PIN")));
        }
        //        ttNotifyObservers();
    }

    private String getAction() {
        String action = null;
        // System.out.println("actionType : " + actionType);
        switch (actionType) {
            case ClientConstants.ACTIONTYPE_NEW:
                action = CommonConstants.STATUS_CREATED;
                break;
            case ClientConstants.ACTIONTYPE_EDIT:
                action = CommonConstants.STATUS_MODIFIED;
                break;
            case ClientConstants.ACTIONTYPE_DELETE:
                action = CommonConstants.STATUS_DELETED;
                break;
            case ClientConstants.ACTIONTYPE_AUTHORIZE:
                action = CommonConstants.STATUS_AUTHORIZED;
                break;
            case ClientConstants.ACTIONTYPE_REJECT:
                action = CommonConstants.STATUS_REJECTED;
                break;
            case ClientConstants.ACTIONTYPE_EXCEPTION:
                action = CommonConstants.STATUS_EXCEPTION;
                break;
            default:
        }
        // System.out.println("command : " + command);
        return action;
    }

    public void ttNotifyObservers() {
        notifyObservers();
    }

    // Setter method for txtSchemeName
    void setTxtSchemeName(String txtSchemeName) {
        this.txtSchemeName = txtSchemeName;
        setChanged();
    }
    // Getter method for txtSchemeName

    String getTxtSchemeName() {
        return this.txtSchemeName;
    }

    public Integer getTxtDivisionNo() {
        return txtDivisionNo;
    }

    public void setTxtDivisionNo(Integer txtDivisionNo) {
        this.txtDivisionNo = txtDivisionNo;
        setChanged();
    }

    public Integer getTxtTotalInst() {
        return txtTotalInst;
    }

    public void setTxtTotalInst(Integer txtTotalInst) {
        this.txtTotalInst = txtTotalInst;
        setChanged();
    }

    public Integer getTxtNoOfInstPaid() {
        return txtNoOfInstPaid;
    }

    public void setTxtNoOfInstPaid(Integer txtNoOfInstPaid) {
        this.txtNoOfInstPaid = txtNoOfInstPaid;
        setChanged();
    }

    public Double getTxtCommisionAmt() {
        return txtCommisionAmt;
    }

    public void setTxtCommisionAmt(Double txtCommisionAmt) {
        this.txtCommisionAmt = txtCommisionAmt;
        setChanged();
    }

    public Double getTxtBonusAmt() {
        return txtBonusAmt;
    }

    public void setTxtBonusAmt(Double txtBonusAmt) {
        this.txtBonusAmt = txtBonusAmt;
        setChanged();
    }

    public Integer getTxtPrizedInstNo() {
        return txtPrizedInstNo;
    }

    public void setTxtPrizedInstNo(Integer txtPrizedInstNo) {
        this.txtPrizedInstNo = txtPrizedInstNo;
        setChanged();
    }

    public Double getTxtOverDueAmt() {
        return txtOverDueAmt;
    }

    public void setTxtOverDueAmt(Double txtOverDueAmt) {
        this.txtOverDueAmt = txtOverDueAmt;
        setChanged();
    }

    public Double getTxtNetAmt() {
        return txtNetAmt;
    }

    public void setTxtNetAmt(Double txtNetAmt) {
        this.txtNetAmt = txtNetAmt;
        setChanged();
    }

    public Double getTxtAribitrationAmt() {
        return txtAribitrationAmt;
    }

    public void setTxtAribitrationAmt(Double txtAribitrationAmt) {
        this.txtAribitrationAmt = txtAribitrationAmt;
        setChanged();
    }

    public Double getTxtDiscountAmt() {
        return txtDiscountAmt;
    }

    public void setTxtDiscountAmt(Double txtDiscountAmt) {
        this.txtDiscountAmt = txtDiscountAmt;
        setChanged();
    }

    public Double getTxtPrizedAmt() {
        return txtPrizedAmt;
    }

    public void setTxtPrizedAmt(Double txtPrizedAmt) {
        this.txtPrizedAmt = txtPrizedAmt;
        setChanged();
    }

    public Double getTxtNoticeAmt() {
        return txtNoticeAmt;
    }

    public void setTxtNoticeAmt(Double txtNoticeAmt) {
        this.txtNoticeAmt = txtNoticeAmt;
        setChanged();
    }

    public Integer getTxtOverdueInst() {
        return txtOverdueInst;
    }

    public void setTxtOverdueInst(Integer txtOverdueInst) {
        this.txtOverdueInst = txtOverdueInst;
        setChanged();
    }

    // Setter method for tdtDrawDate
    void setTdtDrawDate(String tdtDrawDate) {
        this.tdtDrawDate = tdtDrawDate;
        setChanged();
    }
    // Getter method for tdtDrawDate

    String getTdtDrawDate() {
        return this.tdtDrawDate;
    }

    // Setter method for txtChittalNo
    void setTxtChittalNo(String txtChittalNo) {
        this.txtChittalNo = txtChittalNo;
        setChanged();
    }
    // Getter method for txtChittalNo

    String getTxtChittalNo() {
        return this.txtChittalNo;
    }

    // Setter method for txtApplicantsName
    void setTxtApplicantsName(String txtApplicantsName) {
        this.txtApplicantsName = txtApplicantsName;
        setChanged();
    }
    // Getter method for txtApplicantsName

    String getTxtApplicantsName() {
        return this.txtApplicantsName;
    }

    // Setter method for cboTransType
    void setCboTransType(String cboTransType) {
        this.cboTransType = cboTransType;
        setChanged();
    }
    // Getter method for cboTransType

    String getCboTransType() {
        return this.cboTransType;
    }

    // Setter method for txtTransactionAmt
    void setTxtTransactionAmt(String txtTransactionAmt) {
        this.txtTransactionAmt = txtTransactionAmt;
        setChanged();
    }
    // Getter method for txtTransactionAmt

    String getTxtTransactionAmt() {
        return this.txtTransactionAmt;
    }

    // Setter method for txtTransProductId
    void setTxtTransProductId(String txtTransProductId) {
        this.txtTransProductId = txtTransProductId;
        setChanged();
    }
    // Getter method for txtTransProductId

    String getTxtTransProductId() {
        return this.txtTransProductId;
    }

    // Setter method for cboProductType
    void setCboProductType(String cboProductType) {
        this.cboProductType = cboProductType;
        setChanged();
    }
    // Getter method for cboProductType

    String getCboProductType() {
        return this.cboProductType;
    }

    // Setter method for txtDebitAccNo
    void setTxtDebitAccNo(String txtDebitAccNo) {
        this.txtDebitAccNo = txtDebitAccNo;
        setChanged();
    }
    // Getter method for txtDebitAccNo

    String getTxtDebitAccNo() {
        return this.txtDebitAccNo;
    }

    // Setter method for tdtChequeDate
    void setTdtChequeDate(String tdtChequeDate) {
        this.tdtChequeDate = tdtChequeDate;
        setChanged();
    }
    // Getter method for tdtChequeDate

    String getTdtChequeDate() {
        return this.tdtChequeDate;
    }

    // Setter method for txtChequeNo
    void setTxtChequeNo(String txtChequeNo) {
        this.txtChequeNo = txtChequeNo;
        setChanged();
    }
    // Getter method for txtChequeNo

    String getTxtChequeNo() {
        return this.txtChequeNo;
    }

    // Setter method for txtChequeNo2
    void setTxtChequeNo2(String txtChequeNo2) {
        this.txtChequeNo2 = txtChequeNo2;
        setChanged();
    }
    // Getter method for txtChequeNo2

    String getTxtChequeNo2() {
        return this.txtChequeNo2;
    }

    // Setter method for cboInstrumentType
    void setCboInstrumentType(String cboInstrumentType) {
        this.cboInstrumentType = cboInstrumentType;
        setChanged();
    }
    // Getter method for cboInstrumentType

    String getCboInstrumentType() {
        return this.cboInstrumentType;
    }

    // Setter method for txtTokenNo
    void setTxtTokenNo(String txtTokenNo) {
        this.txtTokenNo = txtTokenNo;
        setChanged();
    }
    // Getter method for txtTokenNo

    String getTxtTokenNo() {
        return this.txtTokenNo;
    }

    // Setter method for chkThalayal
    void setChkThalayal(boolean chkThalayal) {
        this.chkThalayal = chkThalayal;
        setChanged();
    }
    // Getter method for chkThalayal

    boolean getChkThalayal() {
        return this.chkThalayal;
    }

    // Setter method for chkMunnal
    void setChkMunnal(boolean chkMunnal) {
        this.chkMunnal = chkMunnal;
        setChanged();
    }
    // Getter method for chkMunnal

    boolean getChkMunnal() {
        return this.chkMunnal;
    }

    // Setter method for chkChangedBetween
    void setChkChangedBetween(boolean chkChangedBetween) {
        this.chkChangedBetween = chkChangedBetween;
        setChanged();
    }
    // Getter method for chkChangedBetween

    boolean getChkChangedBetween() {
        return this.chkChangedBetween;
    }

    /**
     * Getter for property _authorizeMap.
     *
     * @return Value of property _authorizeMap.
     */
    public java.util.HashMap get_authorizeMap() {
        return _authorizeMap;
    }

    /**
     * Setter for property _authorizeMap.
     *
     * @param _authorizeMap New value of property _authorizeMap.
     */
    public void set_authorizeMap(java.util.HashMap _authorizeMap) {
        this._authorizeMap = _authorizeMap;
    }

    /**
     * Getter for property lblStatus.
     *
     * @return Value of property lblStatus.
     */
    public java.lang.String getLblStatus() {
        return lblStatus;
    }

    /**
     * Setter for property lblStatus.
     *
     * @param lblStatus New value of property lblStatus.
     */
    public void setLblStatus(java.lang.String lblStatus) {
        this.lblStatus = lblStatus;
    }

    /**
     * Getter for property result.
     *
     * @return Value of property result.
     */
    public int getResult() {
        return result;
    }

    /**
     * To set the status based on ActionType, either New, Edit, etc.,
     */
    public void setStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[this.getActionType()]);
        ttNotifyObservers();
    }

    /**
     * To update the Status based on result performed by doAction() method
     */
    public void setResultStatus() {
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        ttNotifyObservers();
    }

    /**
     * Setter for property result.
     *
     * @param result New value of property result.
     */
    public void setResult(int result) {
        this.result = result;
    }

    /**
     * Getter for property actionType.
     *
     * @return Value of property actionType.
     */
    public int getActionType() {
        return actionType;
    }

    /**
     * Setter for property actionType.
     *
     * @param actionType New value of property actionType.
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    /**
     * Getter for property txtMemberNo.
     *
     * @return Value of property txtMemberNo.
     */
    public java.lang.String getTxtMemberNo() {
        return txtMemberNo;
    }

    /**
     * Setter for property txtMemberNo.
     *
     * @param txtMemberNo New value of property txtMemberNo.
     */
    public void setTxtMemberNo(java.lang.String txtMemberNo) {
        this.txtMemberNo = txtMemberNo;
    }

    /**
     * Getter for property lblMemberNameVal.
     *
     * @return Value of property lblMemberNameVal.
     */
    public java.lang.String getLblMemberNameVal() {
        return lblMemberNameVal;
    }

    /**
     * Setter for property lblMemberNameVal.
     *
     * @param lblMemberNameVal New value of property lblMemberNameVal.
     */
    public void setLblMemberNameVal(java.lang.String lblMemberNameVal) {
        this.lblMemberNameVal = lblMemberNameVal;
    }

    /**
     * Getter for property allowedTransactionDetailsTO.
     *
     * @return Value of property allowedTransactionDetailsTO.
     */
    public java.util.LinkedHashMap getAllowedTransactionDetailsTO() {
        return allowedTransactionDetailsTO;
    }

    /**
     * Setter for property allowedTransactionDetailsTO.
     *
     * @param allowedTransactionDetailsTO New value of property
     * allowedTransactionDetailsTO.
     */
    public void setAllowedTransactionDetailsTO(java.util.LinkedHashMap allowedTransactionDetailsTO) {
        this.allowedTransactionDetailsTO = allowedTransactionDetailsTO;
    }

    /**
     * Getter for property transactionOB.
     *
     * @return Value of property transactionOB.
     */
    public com.see.truetransact.ui.common.transaction.TransactionOB getTransactionOB() {
        return transactionOB;
    }

    /**
     * Setter for property transactionOB.
     *
     * @param transactionOB New value of property transactionOB.
     */
    public void setTransactionOB(com.see.truetransact.ui.common.transaction.TransactionOB transactionOB) {
        this.transactionOB = transactionOB;
    }

    /**
     * Getter for property lblHouseStNo.
     *
     * @return Value of property lblHouseStNo.
     */
    public java.lang.String getLblHouseStNo() {
        return lblHouseStNo;
    }

    /**
     * Setter for property lblHouseStNo.
     *
     * @param lblHouseStNo New value of property lblHouseStNo.
     */
    public void setLblHouseStNo(java.lang.String lblHouseStNo) {
        this.lblHouseStNo = lblHouseStNo;
    }

    /**
     * Getter for property lblArea.
     *
     * @return Value of property lblArea.
     */
    public java.lang.String getLblArea() {
        return lblArea;
    }

    /**
     * Setter for property lblArea.
     *
     * @param lblArea New value of property lblArea.
     */
    public void setLblArea(java.lang.String lblArea) {
        this.lblArea = lblArea;
    }

    /**
     * Getter for property lblCity.
     *
     * @return Value of property lblCity.
     */
    public java.lang.String getLblCity() {
        return lblCity;
    }

    /**
     * Setter for property lblCity.
     *
     * @param lblCity New value of property lblCity.
     */
    public void setLblCity(java.lang.String lblCity) {
        this.lblCity = lblCity;
    }

    /**
     * Getter for property lblState.
     *
     * @return Value of property lblState.
     */
    public java.lang.String getLblState() {
        return lblState;
    }

    /**
     * Setter for property lblState.
     *
     * @param lblState New value of property lblState.
     */
    public void setLblState(java.lang.String lblState) {
        this.lblState = lblState;
    }

    /**
     * Getter for property lblpin.
     *
     * @return Value of property lblpin.
     */
    public java.lang.String getLblpin() {
        return lblpin;
    }

    /**
     * Setter for property lblpin.
     *
     * @param lblpin New value of property lblpin.
     */
    public void setLblpin(java.lang.String lblpin) {
        this.lblpin = lblpin;
    }

    /**
     * Getter for property transId.
     *
     * @return Value of property transId.
     */
    public java.lang.String getTransId() {
        return transId;
    }

    /**
     * Setter for property transId.
     *
     * @param transId New value of property transId.
     */
    public void setTransId(java.lang.String transId) {
        this.transId = transId;
    }

    /**
     * Getter for property cashId.
     *
     * @return Value of property cashId.
     */
    public java.lang.String getCashId() {
        return cashId;
    }

    /**
     * Setter for property cashId.
     *
     * @param cashId New value of property cashId.
     */
    public void setCashId(java.lang.String cashId) {
        this.cashId = cashId;
    }

    /**
     * Getter for property tblChangedDetails.
     *
     * @return Value of property tblChangedDetails.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblChangedDetails() {
        return tblChangedDetails;
    }

    /**
     * Setter for property tblChangedDetails.
     *
     * @param tblChangedDetails New value of property tblChangedDetails.
     */
    public void setTblChangedDetails(com.see.truetransact.clientutil.EnhancedTableModel tblChangedDetails) {
        this.tblChangedDetails = tblChangedDetails;
    }

    public Double getTxtAmountRefunded() {
        return txtAmountRefunded;
    }

    public void setTxtAmountRefunded(Double txtAmountRefunded) {
        this.txtAmountRefunded = txtAmountRefunded;
    }

    /**
     * Getter for property txtTotalAmtPaid.
     *
     * @return Value of property txtTotalAmtPaid.
     */
    public java.lang.String getTxtTotalAmtPaid() {
        return txtTotalAmtPaid;
    }

    /**
     * Setter for property txtTotalAmtPaid.
     *
     * @param txtTotalAmtPaid New value of property txtTotalAmtPaid.
     */
    public void setTxtTotalAmtPaid(java.lang.String txtTotalAmtPaid) {
        this.txtTotalAmtPaid = txtTotalAmtPaid;
    }

    public Integer getTxtSubNo() {
        return txtSubNo;
    }

    public void setTxtSubNo(Integer txtSubNo) {
        this.txtSubNo = txtSubNo;
    }

    /**
     * Getter for property rdoDefaulters.
     *
     * @return Value of property rdoDefaulters.
     */
    public java.lang.String getRdoDefaulters() {
        return rdoDefaulters;
    }

    /**
     * Setter for property rdoDefaulters.
     *
     * @param rdoDefaulters New value of property rdoDefaulters.
     */
    public void setRdoDefaulters(java.lang.String rdoDefaulters) {
        this.rdoDefaulters = rdoDefaulters;
    }

    public Double getTxtPenalAmt() {
        return txtPenalAmt;
    }

    public void setTxtPenalAmt(Double txtPenalAmt) {
        this.txtPenalAmt = txtPenalAmt;
    }

    public Double getTxtBonusRecovered() {
        return txtBonusRecovered;
    }

    public void setTxtBonusRecovered(Double txtBonusRecovered) {
        this.txtBonusRecovered = txtBonusRecovered;
    }

    /**
     * Getter for property instAmount.
     *
     * @return Value of property instAmount.
     */
    public java.lang.String getInstAmount() {
        return instAmount;
    }

    /**
     * Setter for property instAmount.
     *
     * @param instAmount New value of property instAmount.
     */
    public void setInstAmount(java.lang.String instAmount) {
        this.instAmount = instAmount;
    }

    public Double getTxtChargeAmount() {
        return txtChargeAmount;
    }

    public void setTxtChargeAmount(Double txtChargeAmount) {
        this.txtChargeAmount = txtChargeAmount;
    }

    public boolean checkAcNoWithoutProdType(String actNum) {
        HashMap mapData = new HashMap();
        boolean isExists = false;
        try {
            mapData.put("ACT_NUM", actNum);
            List mapDataList = ClientUtil.executeQuery("getActNumFromAllProducts", mapData);
            //System.out.println("#### mapDataList :" + mapDataList);
            if (mapDataList != null && mapDataList.size() > 0) {
                mapData = (HashMap) mapDataList.get(0);
                setSelectedBranchID(CommonUtil.convertObjToStr(mapData.get("BRANCH_ID")));
                isExists = true;
            } else {
				setSelectedBranchID(ProxyParameters.BRANCH_ID);
            }
            mapDataList = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        mapData = null;
        return isExists;
    }
    public ServiceTaxDetailsTO setServiceTaxDetails() {
        final ServiceTaxDetailsTO objservicetaxDetTo = new ServiceTaxDetailsTO();
        try {
            objservicetaxDetTo.setCommand(getCommand());
            objservicetaxDetTo.setStatus(CommonConstants.STATUS_CREATED);
            objservicetaxDetTo.setStatusBy(TrueTransactMain.USER_ID);
            objservicetaxDetTo.setAcct_Num(getTxtChittalNo());
            objservicetaxDetTo.setParticulars("MDS Prized Money Payment");
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
            if (serviceTax_Map != null && serviceTax_Map.containsKey("TOT_TAX_AMT")) {
                objservicetaxDetTo.setTotalTaxAmt(CommonUtil.convertObjToDouble(serviceTax_Map.get("TOT_TAX_AMT")));
            }
            if (serviceTax_Map != null && serviceTax_Map.containsKey(ServiceTaxCalculation.SWACHH_CESS)) {
                objservicetaxDetTo.setSwachhCess(CommonUtil.convertObjToDouble(serviceTax_Map.get(ServiceTaxCalculation.SWACHH_CESS)));
            }
            if (serviceTax_Map != null && serviceTax_Map.containsKey(ServiceTaxCalculation.KRISHIKALYAN_CESS)) {
                objservicetaxDetTo.setKrishiKalyan(CommonUtil.convertObjToDouble(serviceTax_Map.get(ServiceTaxCalculation.KRISHIKALYAN_CESS)));
            }
            double roudVal = objservicetaxDetTo.getTotalTaxAmt() - (objservicetaxDetTo.getServiceTaxAmt() + objservicetaxDetTo.getEducationCess() + objservicetaxDetTo.getHigherCess()+objservicetaxDetTo.getSwachhCess() + objservicetaxDetTo.getKrishiKalyan());
            ServiceTaxCalculation serviceTaxObj= new ServiceTaxCalculation();
            objservicetaxDetTo.setRoundVal(CommonUtil.convertObjToStr(serviceTaxObj.roundOffAmtForRoundVal(roudVal)));
            objservicetaxDetTo.setStatusDt(currDt);

            if (getCommand().equalsIgnoreCase("INSERT")) {
                objservicetaxDetTo.setCreatedBy(TrueTransactMain.USER_ID);
                objservicetaxDetTo.setCreatedDt(currDt);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objservicetaxDetTo;

    }
    
    // Added by nithya on 10-08-2017 for 7145
    public boolean isBannkSettlementForMDSChangeMember(String prodId, String schemeName){
        HashMap mapData = new HashMap();
        boolean isBankSettlement = false;
        try {
            mapData.put("PROD_ID", prodId);
            mapData.put("SCHEME_NAME", schemeName);
            List mapDataList = ClientUtil.executeQuery("getIsBankSettlementForMDSChangeMember", mapData);
            //System.out.println("#### mapDataList :" + mapDataList);
            if (mapDataList != null && mapDataList.size() > 0) {
                mapData = (HashMap) mapDataList.get(0);
                if (mapData.containsKey("CHANGE_MEMBER_SETTLEMENT") && mapData.get("CHANGE_MEMBER_SETTLEMENT") != null && !"".equals(mapData.get("CHANGE_MEMBER_SETTLEMENT"))) {
                    if (CommonUtil.convertObjToStr(mapData.get("CHANGE_MEMBER_SETTLEMENT")).equalsIgnoreCase("Y")) {
                        isBankSettlement = true;
                    } else {
                        isBankSettlement = false;
                    }
                } else {
                    isBankSettlement = false;
                }
            } else {
                isBankSettlement = false;
            }
            mapDataList = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        mapData = null;
        return isBankSettlement;
    }

    public double getDefaulterReceiptTaxAmt() {
        return defaulterReceiptTaxAmt;
    }

    public void setDefaulterReceiptTaxAmt(double defaulterReceiptTaxAmt) {
        this.defaulterReceiptTaxAmt = defaulterReceiptTaxAmt;
    }

    public double getPaymentTaxAmt() {
        return paymentTaxAmt;
    }

    public void setPaymentTaxAmt(double paymentTaxAmt) {
        this.paymentTaxAmt = paymentTaxAmt;
    }

    public double getAuctionTransCommissionTaxAmount() {
        return auctionTransCommissionTaxAmount;
    }

    public void setAuctionTransCommissionTaxAmount(double auctionTransCommissionTaxAmount) {
        this.auctionTransCommissionTaxAmount = auctionTransCommissionTaxAmount;
    }
        
}