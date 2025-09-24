/*
 * DailyDepositTransOB.java
 *
 * Created on February 25, 2004, 2:48 PM
 */
package com.see.truetransact.ui.transaction.dailyDepositTrans;

import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.serverside.transaction.common.TransactionFactory;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.transferobject.agent.AgentTO;
import java.util.Observable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import com.see.truetransact.transferobject.transaction.dailyDepositTrans.DailyDepositTransTO;
import com.see.truetransact.transferobject.transaction.transfer.TxTransferTO;
import com.see.truetransact.transferobject.product.deposits.DepositsProductTO;
import com.see.truetransact.transferobject.product.loan.LoanProductAccountTO;

import org.apache.log4j.Logger;

//import com.see.truetransact.serverside.tds.tdscalc;
/**
 *
 * @author  rahul
 */
public class DailyDepositTransOB extends CObservable {

    private HashMap lookUpHash;
    private HashMap keyValue;
    private HashMap operationMap;
    private HashMap _authorizeMap;
    private ArrayList key;
    private ArrayList value;
    private ProxyFactory proxy = null;
    private int actionType;
    private int result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private final static Logger log = Logger.getLogger(DailyDepositTransUI.class);
    final String DEBIT = "DEBIT";
    final String CREDIT = "CREDIT";
    final String INITIATORTYPE = "CASHIER";
    //    private String lblAccHdDesc;
    private String txtAccHdId;
    private String lblAccName;
    private String transStatus = "";
    private String lblTransactionId;
    private String lblTransDate;
    private String lblInitiatorId;
    private static CInternalFrame frame;
    private String transactionId;
    private double oldAmount = 0.0;
    private String cr_cash;
    private String dr_cash;
    private ArrayList denominationList;
    private ArrayList recordData, deleteData;
    //    private LinkedHashMap record;
    private CashTransactionTO cashTrans;
    private DailyDepositTransTO DailyTrnasTo;
    private String txtAccNo = "";
    private String txtInitiatorChannel = "";
    private boolean rdoTransactionType_Credit = false;
    private String txtAmount = "";
    private String balance = "";
    private String depSubNoStatus = "";
    private String prodId = "";
    private String prod_desc = "";
    private String depDailybNoK = "";
    private String depSubNoMode = "";
    private String prodType = "";
    private String flagSave="";

    public String getFlagSave() {
        return flagSave;
    }

    public void setFlagSave(String flagSave) {
        this.flagSave = flagSave;
    }

    public String getProdType() {
        return prodType;
    }

    public void setProdType(String prodType) {
        this.prodType = prodType;
        setChanged();
    }
    private final int YES = 0;
    private final int NO = 1;
    private final int CANCEL = 2;
    private TableModel tbmTransfer;
    private int operation;
    private String cboAgentType;
    private ComboBoxModel cbmAgentType;
    private Date tdtInstrumentDate = null;
    private double totalAmt;
    private double totalGlAmt;
    private static DailyDepositTransOB dailyDepositTransOB;
    private static Date currDt = null;
    static {
        try {
            currDt = ClientUtil.getCurrentDate();
            log.info("In DailyDepositTransOB Declaration");
            dailyDepositTransOB = new DailyDepositTransOB();
        } catch (Exception e) {
            System.out.println("eeeeeeeeeeeee" + e);
            log.info("Error in DailyDepositTransOB Declaration");
        }
    }

    public static DailyDepositTransOB getInstance(CInternalFrame frm) throws Exception {
        frame = frm;
        return dailyDepositTransOB;
    }

    /** Creates a new instance of DailyDepositTransOB */
    public DailyDepositTransOB() throws Exception {
        initianSetup();
        setTable();
        recordData = new ArrayList();
        deleteData = new ArrayList();
    }

    private void initianSetup() {
        log.info("In initianSetup()");       
        try {
             setOperationMap();
            proxy = ProxyFactory.createProxy();
            fillDropDown();
            setProductLbl();
        } catch (Exception e) {
            log.info(" Error In initianSetup()");
            e.printStackTrace();
        }        
    }

    private void setProductLbl() {
        if (CommonConstants.VENDOR != null && CommonConstants.VENDOR.equals("POLPULLY")) {
            HashMap hash = new HashMap();
            hash.put("DAILY", "DAILY");
            List lst = ClientUtil.executeQuery("getProductDescription", hash);
            hash = (HashMap) lst.get(0);
            setProdId(CommonUtil.convertObjToStr(hash.get("PROD_ID")));
            setProd_desc(CommonUtil.convertObjToStr(hash.get("PROD_DESC")));
            setTxtAccHd(CommonUtil.convertObjToStr(hash.get("ACCT_HEAD")));
        } else if (CommonConstants.VENDOR != null && CommonConstants.VENDOR.equals("KOORKKENCHERRY")) {
            HashMap hash = new HashMap();
            hash.put("DAILY", "DAILY");
            List lst = ClientUtil.executeQuery("getProductDescription", hash);
            hash = (HashMap) lst.get(0);
            setProdId(CommonUtil.convertObjToStr(hash.get("PROD_ID")));
            setProd_desc(CommonUtil.convertObjToStr(hash.get("PROD_DESC")));
            setTxtAccHd(CommonUtil.convertObjToStr(hash.get("ACCT_HEAD")));
        } else if (CommonConstants.VENDOR != null && CommonConstants.VENDOR.equals("CHELLANOOR")) {
            HashMap hash = new HashMap();
            hash.put("DAILY", "DAILY");
            List lst = ClientUtil.executeQuery("getProductDescription", hash);
            hash = (HashMap) lst.get(0);
            setProdId(CommonUtil.convertObjToStr(hash.get("PROD_ID")));
            setProd_desc(CommonUtil.convertObjToStr(hash.get("PROD_DESC")));
            setTxtAccHd(CommonUtil.convertObjToStr(hash.get("ACCT_HEAD")));
        } else {
            HashMap hash = new HashMap();
            hash.put("DAILY_LOAN", "DAILY_LOAN");
            List lst = ClientUtil.executeQuery("getLoanProductDescription", hash);
            hash = (HashMap) lst.get(0);
            setProdId(CommonUtil.convertObjToStr(hash.get("PROD_ID")));
            setProd_desc(CommonUtil.convertObjToStr(hash.get("PROD_DESC")));
            setTxtAccHd(CommonUtil.convertObjToStr(hash.get("ACCT_HEAD")));
        }
    }

    // Set the value of JNDI and the Session Bean...
    private void setOperationMap() throws Exception {
        log.info("In setOperationMap()");

        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "TransferJNDI");
        operationMap.put(CommonConstants.HOME, "transaction.transfer.TransferHome");
        operationMap.put(CommonConstants.REMOTE, "transaction.transfer.TransferRemote");
    }
  
    // To Fill the Combo boxes in the UI
    private void fillDropDown() throws Exception {
        HashMap where = new HashMap();
        where.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
        List lst = (List) ClientUtil.executeQuery("getAgentMasterDetails", where);
        System.out.println("########ListForAgent : " + lst);
        getMap(lst);
        System.out.println("keykeykeykeykeykey" + key);
        System.out.println("valuevaluevaluevaluevaluevalue" + value);
//        setCbmAgentType(new ComboBoxModel(key,value));
        System.out.println("bbbbbbbbbbbbb");
//        System.out.println("gggggggggggggg"+getCbmAgentType());
        cbmAgentType = new ComboBoxModel(key, value);
    }

    public void setAgentType() {
        try {
            fillDropDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getMap(List list) throws Exception {
        try{
        key = new ArrayList();
        value = new ArrayList();

        //The first values in the ArrayList key and value are empty String to display the
        //first row of all dropdowns to be empty String
        key.add("");
        value.add("");
        for (int i = 0, j = list.size(); i < j; i++) {

            key.add(((HashMap) list.get(i)).get("KEY"));
            System.out.println("keykey" + key);
            value.add(((HashMap) list.get(i)).get("VALUE"));
        }
        }catch(Exception e){
            e.printStackTrace();
        }
        //        keyValue = new HashMap();
        //        keyValue.put("KEY", key);
        //        keyValue.put("VALUE", value);
        //        return keyValue;

    }

    private HashMap populateData(HashMap obj, String actNum, String PROD_ID) throws Exception {
        // System.out.println("obj in OB : " + obj);
        obj.put(CommonConstants.MAP_WHERE, actNum);
        ///   System.out.println("getTransProdId :"+getTransProdId());

        obj.put("PROD_ID", PROD_ID);
        obj.put("PROD_TYPE", prodType);

        obj.put("CURR_DATE", ClientUtil.getCurrentDateProperFormat());
        // System.out.println("map in OB :MIDDLE " + obj);
        HashMap map = new HashMap();
        map.put(CommonConstants.JNDI, "AccountClosingJNDI");
        map.put(CommonConstants.HOME, "operativeaccount.AccountClosingHome");
        map.put(CommonConstants.REMOTE, "operativeaccount.AccountClosing");
        HashMap where = proxy.executeQuery(obj, map);

        // System.out.println("where : " + where);
        //        keyValue = (HashMap)where.get("AccountDetailsTO");
        //        transactionOB.setDetails((List)where.get("TransactionTO"));
        //        log.info("Got HashMap");
        return where;
    }
    private HashMap totalLoanAmount;

    public String getAccountClosingCharges(HashMap dataMap1) {
        HashMap param = new HashMap();
        //HashMap dataMap = null;
        try {
            param.put("MODE", getCommand());
            // System.out.println("param ... : " + param);
            double advancesCreditInterest = 0;
            double subsidyEditTransAmt = 0;
            double rebateEditTransAmt = 0;
            //for premature deposit closer

            param.put("NORMAL_CLOSER", "NORMAL_CLOSER");

            HashMap dataMap = populateData(param, dataMap1.get("ACT_NUM").toString(), dataMap1.get("PROD_ID").toString());
            System.out.println("param Data Map :" + dataMap);
            HashMap chargeMap = (HashMap) dataMap.get("AccountDetailsTO");
            //            List interest=(List)dataMap.get("AccountDetailsTO");
            //            HashMap loneInt=(HashMap)interest.get(0);
            HashMap intMap = new HashMap();
            if (dataMap1.get("PROD_TYPE").toString().equals("TL")) // setAsAnWhen(CommonUtil.convertObjToStr(chargeMap.get("AS_CUSTOMER_COMES")));
            {
                System.out.println("chargeMap : " + chargeMap);
            }

            if (dataMap1.get("PROD_TYPE").toString().equals("TL")) {
                double totalAmt = 0;
                double totalPenalAmt = 0;
                //                double clearBalance=CommonUtil.convertObjToDouble(chargeMap.get("CLEAR_BALANCE")).doubleValue();
                double LOAN_BALANCE_PRINCIPAL = CommonUtil.convertObjToDouble(totalLoanAmount.get("LOAN_BALANCE_PRINCIPAL")).doubleValue();
                if (LOAN_BALANCE_PRINCIPAL < 0) {
                    LOAN_BALANCE_PRINCIPAL = LOAN_BALANCE_PRINCIPAL * (-1);
                    totalLoanAmount.put("LOAN_BALANCE_PRINCIPAL", new Double(LOAN_BALANCE_PRINCIPAL));
                    //setAvailableBalance(String .valueOf(LOAN_BALANCE_PRINCIPAL));
                }

                // avilableLoanSubsidy =CommonUtil.convertObjToDouble(totalLoanAmount.get("AVAILABLE_SUBSIDY")).doubleValue();
                // if(avilableLoanSubsidy>0){
                //   int confirm= ClientUtil.confirmationAlert("Subsidy Amount Available for this Account!!!!"+"\n"+"Do you want to adjust the Subsidy.");
                //  if(confirm==0){
                ///       totalLoanAmount.put("AVAILABLE_SUBSIDY",new Double(avilableLoanSubsidy));
                //       subsidyEditTransAmt=avilableLoanSubsidy;
                // }else{
                //     avilableLoanSubsidy=0;
                //     subsidyEditTransAmt=avilableLoanSubsidy;
                //     totalLoanAmount.put("AVAILABLE_SUBSIDY",new Double(avilableLoanSubsidy));

                //  }
                // }
                int waiveconfirm = -1, penalwaiveconfirm = -1;

                if (CommonUtil.convertObjToStr(totalLoanAmount.get("INTEREST_WAIVER")).equals("Y")) {
                    waiveconfirm = ClientUtil.confirmationAlert("Do you want to waiveoff the Interest.");
                    if (waiveconfirm == 0) {
                        //  waiveOffInterest=true;
                        //  showEditTableUI(totalLoanAmount);
                        // waiveEditTransAmt= waiveOffEditInterestAmt();
                    } else {
                        //  waiveOffInterest=false;
                        //  waiveEditTransAmt=-1;
                    }
                }

                if (CommonUtil.convertObjToStr(totalLoanAmount.get("PENAL_WAIVER")).equals("Y") && CommonUtil.convertObjToDouble(totalLoanAmount.get("LOAN_CLOSING_PENAL_INT")).doubleValue() > 0) {
                    // penalwaiveconfirm=ClientUtil.confirmationAlert("Do you want to waiveoff the Penal Interest.");
                    if (penalwaiveconfirm == 0) {
                        //  waiveoffPenal=true;
//                    showEditTableUI(totalLoanAmount);
                        // waiveEditPenalTransAmt= CommonUtil.convertObjToDouble(totalLoanAmount.get("PENAL_INT")).doubleValue();//waiveOffEditInterestAmt();
                    } else {
                        //  waiveoffPenal=false;
                        //  waiveEditPenalTransAmt=0;
                    }
                }
                if (waiveconfirm != 0 && CommonUtil.convertObjToDouble(totalLoanAmount.get("ACCOUNT_CLOSING_PENAL_INT")).doubleValue() == 0) {
                    double rebateInterest = CommonUtil.convertObjToDouble(totalLoanAmount.get("REBATE_INTEREST")).doubleValue();
                    if (rebateInterest > 0) {
                        int confirm = ClientUtil.confirmationAlert("Rebate Interest Amount Available for this Account!!!!" + "\n" + "Do you want to adjust the Rebate Interest.");
                        if (confirm == 0) {
                            totalLoanAmount.put("REBATE_INTEREST", new Double(rebateInterest));
                            rebateEditTransAmt = rebateInterest;
                        } else {
                            rebateInterest = 0;
                            rebateEditTransAmt = rebateInterest;
                            totalLoanAmount.put("REBATE_INTEREST", new Double(rebateInterest));

                        }
                    }
                }

                //editableUI.show();

                //               setAvailableBalance(CommonUtil.convertObjToStr(chargeMap.get("CLEAR_BALANCE")));
                //            setTxtPayableBalance(CommonUtil.convertObjToStr(chargeMap.get("CLEAR_BALANCE")));
                setLoanInt(CommonUtil.convertObjToStr(dataMap.get("AccountInterest")));
                if (param.containsKey("NORMAL_CLOSER")) {
                    totalLoanAmount.put("CURR_MONTH_INT", new Double(getLoanInt()));
                }

                String emi = CommonUtil.convertObjToStr((totalLoanAmount.get("POSTAGE CHARGES") == null) ? "0" : totalLoanAmount.get("POSTAGE CHARGES"));
                totalAmt += Double.parseDouble(emi);
                emi = CommonUtil.convertObjToStr((totalLoanAmount.get("MISCELLANEOUS CHARGES") == null) ? "0" : totalLoanAmount.get("MISCELLANEOUS CHARGES"));
                totalAmt += Double.parseDouble(emi);
                emi = CommonUtil.convertObjToStr((totalLoanAmount.get("LEGAL CHARGES") == null) ? "0" : totalLoanAmount.get("LEGAL CHARGES"));
                totalAmt += Double.parseDouble(emi);
                emi = CommonUtil.convertObjToStr((totalLoanAmount.get("INSURANCE CHARGES") == null) ? "0" : totalLoanAmount.get("INSURANCE CHARGES"));
                totalAmt += Double.parseDouble(emi);
                emi = CommonUtil.convertObjToStr((totalLoanAmount.get("EXECUTION DECREE CHARGES") == null) ? "0" : totalLoanAmount.get("EXECUTION DECREE CHARGES"));
                totalAmt += Double.parseDouble(emi);
                emi = CommonUtil.convertObjToStr((totalLoanAmount.get("ARBITRARY CHARGES") == null) ? "0" : totalLoanAmount.get("ARBITRARY CHARGES"));
                totalAmt += Double.parseDouble(emi);
                emi = CommonUtil.convertObjToStr((totalLoanAmount.get("ADVERTISE CHARGES") == null) ? "0" : totalLoanAmount.get("ADVERTISE CHARGES"));
                totalAmt += Double.parseDouble(emi);
                emi = CommonUtil.convertObjToStr((totalLoanAmount.get("NOTICE CHARGES") == null) ? "0" : totalLoanAmount.get("NOTICE CHARGES"));
                totalAmt += Double.parseDouble(emi);

                totalAmt -= rebateEditTransAmt;
                // if(waiveEditTransAmt>0){
                //  totalAmt-=waiveEditTransAmt;
                //  }
                //   if(waiveEditPenalTransAmt>0){
                //      totalAmt-=CommonUtil.convertObjToDouble(totalLoanAmount.get("PENAL_INT")).doubleValue();
                //  }
                emi = CommonUtil.convertObjToStr((totalLoanAmount.get("CURR_MONTH_INT") == null) ? "0" : totalLoanAmount.get("CURR_MONTH_INT"));
                //                if( getDeposit_premature()!=null && getDeposit_premature().length()>0)
                totalAmt += Double.parseDouble(emi);// COMMENTED BY ABI FOR NOT TAKING INT FROM LOAN INSTALLMENT
                //                String prince=CommonUtil.convertObjToStr((totalLoanAmount.get("CURR_MONTH_PRINCEPLE")==null)? "0.00":totalLoanAmount.get("CURR_MONTH_PRINCEPLE"));
                //                totalAmt+=Double.parseDouble(prince);
                String Overprince = CommonUtil.convertObjToStr((totalLoanAmount.get("OVER_DUE_PRINCIPAL") == null) ? "0.00" : totalLoanAmount.get("OVER_DUE_PRINCIPAL"));
                totalAmt += Double.parseDouble(Overprince);
                String overDueInt = CommonUtil.convertObjToStr((totalLoanAmount.get("OVER_DUE_INTEREST") == null) ? "0.00" : totalLoanAmount.get("OVER_DUE_INTEREST"));
                totalAmt += Double.parseDouble(overDueInt);
                String penal = CommonUtil.convertObjToStr((totalLoanAmount.get("PENAL_INT") == null) ? "0.00" : totalLoanAmount.get("PENAL_INT"));
                totalAmt += Double.parseDouble(penal);
                setLoanInt(String.valueOf(totalAmt));
                //  setTxtInterestPayable(getLoanInt());
                //  if( getDeposit_premature()!=null && getDeposit_premature().length()>0){
                //      double calculateInt=CommonUtil.convertObjToDouble(totalLoanAmount.get("CALCULATE_INT")).doubleValue();
                //     totalAmt=CommonUtil.convertObjToDouble(totalLoanAmount.get("LOAN_BALANCE_PRINCIPAL")).doubleValue();
                //     totalAmt-=calculateInt;
                //  }else{
                System.out.println("totalLoanAmount 78888 ===" + totalLoanAmount);

                if (dataMap.containsKey("ADVANCES_CREDIT_INT") && dataMap.get("ADVANCES_CREDIT_INT") != null) {
                    double advCreditInterest = CommonUtil.convertObjToDouble(dataMap.get("ADVANCES_CREDIT_INT")).doubleValue();
                    System.out.println("totalAmt ===" + totalAmt);
                    System.out.println("advCreditInterest ===" + advCreditInterest);
                    //    System.out.println("getTxtInterestPayable() ==="+getTxtInterestPayable());
                    //  totalAmt+=-advCreditInterest;//babu
                    totalAmt += +advCreditInterest;
                    if (CommonUtil.convertObjToDouble(totalAmt) > 0) {
                        totalAmt = totalAmt - CommonUtil.convertObjToDouble(getLoanInt());
                        totalAmt = Math.abs(totalAmt);
                    }
                    advancesCreditInterest = advCreditInterest;
                    //    ClientUtil.showMessageWindow("Advances  Out Standing Amount :"+ CommonUtil.convertObjToDouble(totalLoanAmount.get("LOAN_BALANCE_PRINCIPAL")).doubleValue()+"\n"+
                    //                    "Advances Credit Interest :"  +advCreditInterest+"\n"+
                    //                     "Advances Debit Interest :"  +getTxtInterestPayable()+"\n"+
                    //                    "Remaining Due            :"+totalAmt);
                }
                // }
                // setTxtPayableBalance(CommonUtil.convertObjToStr(totalAmt));//babu
                //txtPayableBalance=CommonUtil.convertObjToStr(totalAmt);
                System.out.println("sssssssssss=====" + String.valueOf(totalAmt));
                //            setLoanInt(CommonUtil.convertObjToStr(dataMap.get("AccountInterest")));
                // setCustomerName(CommonUtil.convertObjToStr(chargeMap.get("Customer Name")));
                // setCustomerStreet(CommonUtil.convertObjToStr(chargeMap.get("HOUSE_NAME")));
            } else {

                intMap = (HashMap) dataMap.get("AccountInterest");
                //     setAccountHeadDesc(CommonUtil.convertObjToStr(chargeMap.get("ac_hd_desc")));
                //   setTxtAccountClosingCharges(String.valueOf(CommonUtil.convertObjToDouble(chargeMap.get("act_closing_chrg")).doubleValue()));
                //   setCustomerName(CommonUtil.convertObjToStr(chargeMap.get("Customer Name")));
                //   setCustomerStreet(CommonUtil.convertObjToStr(chargeMap.get("HOUSE_NAME")));
                //   setTxtNoOfUnusedChequeLeafs(String.valueOf(CommonUtil.convertObjToDouble(chargeMap.get("unused_chk")).doubleValue()));
                //   setAvailableBalance(CommonUtil.convertObjToStr(chargeMap.get("AVAILABLE_BALANCE")));
                //   setTxtInterestPayable(String.valueOf(CommonUtil.convertObjToDouble(intMap.get(getTxtAccountNumber())).doubleValue()));
                //   setTxtChargeDetails(CommonUtil.convertObjToStr(chargeMap.get("chrg_details")));
                //   setTxtPayableBalance(CommonUtil.convertObjToStr(chargeMap.get("payable_bal")));
            }

            //for as an premature
            // if( getAsAnWhen()!=null && getAsAnWhen().equals("Y")){
            //      totalLoanAmount.put("INTEREST",dataMap.get("AccountInterest"));
            //  }
            System.out.println("getLoneInt : " + getLoanInt());
            //  ttNotifyObservers();
        } catch (Exception e) {
            e.printStackTrace();
            parseException.logException(e, true);
        }
        return getLoanInt();

    }

    public java.lang.String getLoanInt() {
        return loanInt;
    }

    /**
     * Setter for property loanInt.
     * @param loanInt New value of property loanInt.
     */
    public void setLoanInt(java.lang.String loanInt) {
        this.loanInt = loanInt;
    }
    String loanInt = "";

    public HashMap getLoanBalance(HashMap dataMap) {
        HashMap retMap = new HashMap();
        try {
            retMap = proxy.executeQuery(dataMap, operationMap); //, frame);
            //  System.out.println("retMap INMMMMMM======"+retMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retMap;
    }
    // To enter the Data into the Database...Called from doActionPerform()...
    //Do display the Data from the Database, in UI

    public void populateData(HashMap whereMap) {
        log.info("In populateData()");

        final List lstData;
        try {
            System.out.println("### wheremap.... : " + whereMap);
            HashMap dailyMap = new HashMap();
            System.out.println("#whereMap.get(BATCH_ID).--- : " + whereMap.get("BATCH_ID"));
            System.out.println("#DDDDDDD-- : " + currDt.clone());
            System.out.println("#BRANCH-- : " + TrueTransactMain.BRANCH_ID);
            dailyMap.put("BATCH_ID", whereMap.get("BATCH_ID"));
            dailyMap.put("TRANS_DT", currDt.clone());
            dailyMap.put("INITIATED_BRANCH", TrueTransactMain.BRANCH_ID);
            //            mapData = proxy.executeQuery(whereMap, operationMap); //, frame);
            lstData = ClientUtil.executeQuery("getSelectCashTransactionTODAILY", dailyMap);
            System.out.println("#lstData : " + lstData + " ssssss---" + lstData.size());
            if (lstData != null && lstData.size() > 0) {
                System.out.println("### mapData.... : " + lstData);
                setLblTransactionId(CommonUtil.convertObjToStr(whereMap.get("BATCH_ID")));
                Date coll_dt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(whereMap.get("COLL_DT")));
                //            coll_dt=new Date(whereMap.get("COLL_DT")));
                setTdtInstrumentDate(coll_dt);
                setCboAgentType(CommonUtil.convertObjToStr(whereMap.get("AGENT_ID")));
                setLblInitiatorId(CommonUtil.convertObjToStr(whereMap.get("STATUS_BY")));

                setTotalGlAmt(CommonUtil.convertObjToDouble(whereMap.get("AMOUNT")).doubleValue());
                setTdtInstrumentDate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(whereMap.get("COLL_DT"))));
                
                populateOB(lstData);
            }
        } catch (Exception e) {
            System.out.println("Error In populateData()" + e);
            //.printStackTrace();
        }
    }

    private void populateOB(List lstData) throws Exception {
        log.info("In populateOB()");
        //Taking the Value of Transaction Id from each Table...
        // Here the first Row is selected...

        recordData = new ArrayList();
        //        List lst = (List) mapData.get("CashTransactionTO");

        int j = lstData.size();
        System.out.println("lst.size() : " + lstData.size());

        setTable();
        for (int i = 0; i < j; i++) {
            DailyDepositTransTO DailyTrnasTo = new DailyDepositTransTO();
            DailyTrnasTo = (DailyDepositTransTO) lstData.get(i);
            DailyTrnasTo.setScreenName(getScreen()); // Rework - KD-2034 by nithya
            System.out.println("DailyTrnasTo ------: " + DailyTrnasTo);
            setLblTransactionId(CommonUtil.convertObjToStr(DailyTrnasTo.getBatch_id()));
            setLblTransDate(DateUtil.getStringDate(DailyTrnasTo.getTrn_dt()));
            setProdType(DailyTrnasTo.getProd_Type());
            //            setLblInitiatorId(CommonUtil.convertObjToStr(cashTrans.getInitTransId()));
            recordData.add(DailyTrnasTo);
            System.out.println("ACC NUM-----" + DailyTrnasTo.getAcct_num());
            System.out.println("PPP YYUPEEE-----" + DailyTrnasTo.getProd_Type());
            System.out.println("TRANSSS YYUPEEE-----" + DailyTrnasTo.getTrans_type());
            if ((CommonConstants.VENDOR != null && CommonConstants.VENDOR.equals("KOORKKENCHERRY")) && (
                    getOperation() == ClientConstants.ACTIONTYPE_AUTHORIZE ||  getOperation() == ClientConstants.ACTIONTYPE_REJECT)) {
                setAccountName(DailyTrnasTo.getAcct_num(), DailyTrnasTo.getProd_Type());
            }
            else{
              setAccountName(DailyTrnasTo.getAcct_num(), DailyTrnasTo.getTrans_type());
            }
            ArrayList irRow = this.setRow(DailyTrnasTo);
            tbmTransfer.insertRow(tbmTransfer.getRowCount(), irRow);
        }
        tbmTransfer.fireTableDataChanged();
        System.out.println("mapData : " + lstData.size());
        //        setDenominationList((ArrayList) lstData.get("DENOMINATION_LIST"));
        ttNotifyObservers();
    }

    private void setTableAuthData() {
        ArrayList row;
        ArrayList rows = new ArrayList();
        DailyDepositTransTO obj;
        int size = this.recordData.size();
        for (int i = 0; i < size; i++) {
            obj = (DailyDepositTransTO) this.recordData.get(i);
            row = setRow(obj);
            rows.add(row);
        }
        setTable();
        tbmTransfer.setData(rows);
        tbmTransfer.fireTableDataChanged();
        obj = null;
    }

    // To Enter the values in the UI fields, from the database...
    // To perform Appropriate operation... Insert, Update, Delete...
    public void setTable() {
        ArrayList columnHeader = new ArrayList();
        columnHeader.add("Deposit No");
        columnHeader.add("Amount");
        columnHeader.add("Customer Name");

        if (getOperation() == ClientConstants.ACTIONTYPE_AUTHORIZE || getOperation() == ClientConstants.ACTIONTYPE_REJECT) {
            columnHeader.add("Verified");
        }
        if (getOperation() != ClientConstants.ACTIONTYPE_AUTHORIZE  && getOperation() != ClientConstants.ACTIONTYPE_REJECT ) {
        
        columnHeader.add("Prod Type");
        }
        ArrayList data = new ArrayList();
        tbmTransfer = new TableModel(data, columnHeader);
    }

    public void populateDailyTransfer(int rowNum) {
        System.out.println("rowNum :" + rowNum + "recordData :" + recordData + "cashTrans :" + DailyTrnasTo);
        System.out.println("cashTrans : +" + DailyTrnasTo);
        DailyDepositTransTO DailyTrnasTo = (DailyDepositTransTO) recordData.get(rowNum);
        System.out.println("cashTrans : +" + DailyTrnasTo);
        if (getOperation() == ClientConstants.ACTIONTYPE_AUTHORIZE || getOperation() == ClientConstants.ACTIONTYPE_REJECT) {
            HashMap authMap=new HashMap();
            authMap.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
            authMap.put("TRANS_DT", currDt.clone());
            authMap.put("ACT_NUM",DailyTrnasTo.getAcct_num());
            List authList =ClientUtil.executeQuery("getDataForAuthorizeWeekly", authMap);
            if(authList!=null && authList.size()>0){
                authMap=(HashMap) authList.get(0);
                this.setCboAgentType(CommonUtil.convertObjToStr(authMap.get("AGENT_NO")));
                setProdId(CommonUtil.convertObjToStr(authMap.get("PROD_ID")));
            }            
        }
        if(getFlagSave()!=null && getFlagSave().equals("IMPORT")){
            HashMap authMap=new HashMap();
            authMap.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
            authMap.put("TRANS_DT", currDt.clone());
            String deposit_No=DailyTrnasTo.getAcct_num();
            if (deposit_No.lastIndexOf("_") != -1) {
                deposit_No = deposit_No.substring(0, deposit_No.lastIndexOf("_"));
            }
            authMap.put("ACT_NUM",deposit_No);
          
            List authList =null;
          // authMap.put(CommonConstants.VENDOR,CommonConstants.VENDOR);
            if (CommonConstants.VENDOR != null && CommonConstants.VENDOR.equals("KOORKKENCHERRY")) {
               authList =ClientUtil.executeQuery("getDataForAuthorizeWeeklyImport", authMap);
            }else{
               authMap.put("DEPOSIT NO",deposit_No);
               authList =ClientUtil.executeQuery("getAccInfoDetails", authMap); 
            }
           if(authList!=null && authList.size()>0){
                authMap=(HashMap) authList.get(0);
                setProdId(CommonUtil.convertObjToStr(authMap.get("PROD_ID")));
            }
        }    
        this.setTransAmt(DailyTrnasTo);
    }

    private void setTransAmt(DailyDepositTransTO DailyTrnasTo) {
        System.out.println("setTransAmt" + DailyTrnasTo);
        setTxtAccNo(DailyTrnasTo.getAcct_num());
        setTxtAmount(DailyTrnasTo.getAmount().toString());
        //        setDepSubNoStatus(DailyTrnasTo.getStatus());
        setCboAgentType(DailyTrnasTo.getAgent_no());
        setStatusBy(DailyTrnasTo.getCreated_by());
        setTdtInstrumentDate(DailyTrnasTo.getColl_dt());
        setProdType(DailyTrnasTo.getProd_Type());
        setInitiatorChannelValue();
        //ttNotifyObservers();
    }

    public void populatALlTranferTO(int rowCount) {
        DailyDepositTransTO obj = null;
        if (getOperation() == ClientConstants.ACTIONTYPE_AUTHORIZE || getOperation() == ClientConstants.ACTIONTYPE_REJECT) {
            for (int i = 0; i < rowCount; i++) {
                obj = (DailyDepositTransTO) recordData.get(i);
                tbmTransfer.setValueAt("Yes", i, 3);
                setTransAmt(obj);
            }
        }
        obj = null;
    }

    public void populatTranferTO(int rowNum) {
        DailyDepositTransTO obj = (DailyDepositTransTO) recordData.get(rowNum);
        if (getOperation() == ClientConstants.ACTIONTYPE_AUTHORIZE || getOperation() == ClientConstants.ACTIONTYPE_REJECT) {
//            ArrayList row = (ArrayList)tbmTransfer.getDataArrayList().get(rowNum);
//            row.set(3, "Yes");
//            tbmTransfer.fireTableCellUpdated(3, rowNum);
            tbmTransfer.setValueAt("Yes", rowNum, 3);
        }
        this.setTransAmt(obj);
        obj = null;
    }

    private void setTableData() {
        ArrayList row;
        ArrayList rows = new ArrayList();
        CashTransactionTO cashTrans;
        int size = this.recordData.size();
        ArrayList rec;
        for (int i = 0; i < size; i++) {
            cashTrans = (CashTransactionTO) this.recordData.get(i);
            System.out.println("setTableData : " + cashTrans);
            row = setRow(DailyTrnasTo);
            rows.add(row);
        }
        tbmTransfer.setData(rows);
        tbmTransfer.fireTableDataChanged();
    }

    public DailyDepositTransTO setTOStatus(DailyDepositTransTO DailyTrnasTo) {
        //        DailyTrnasTo.setStatus(this.getTransStatus());
        DailyTrnasTo.setCreated_by(TrueTransactMain.USER_ID);
        DailyTrnasTo.setCreated_dt(currDt);
       
        if (this.getTransStatus().equalsIgnoreCase(CommonConstants.STATUS_CREATED)) {
            DailyTrnasTo.setBatch_id(getSelectedBranchID());
            DailyTrnasTo.setInitiatedBranch(TrueTransactMain.BRANCH_ID);
             DailyTrnasTo.setProd_Type(getProdType());
            //            DailyTrnasTo.setInitTransId(TrueTransactMain.USER_ID);
            //            DailyTrnasTo.setInitChannType(TrueTransactMain.BRANCH_ID);
        }
        return DailyTrnasTo;
    }

    public void deleteDailyData(int rowNum) {
        DailyDepositTransTO DailyTrnasTo = this.setTOStatus((DailyDepositTransTO) this.recordData.get(rowNum));
//        DailyTrnasTo.setStatus(CommonConstants.STATUS_DELETED);
        deleteData.add(DailyTrnasTo);
        recordData.remove(rowNum);//chandra
        HashMap wheremap1=new HashMap();
        wheremap1.put("AGENT_ID", DailyTrnasTo.getAgent_no());
        wheremap1.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
        String act_Num=CommonUtil.convertObjToStr(DailyTrnasTo.getAcct_num());
        if (act_Num.lastIndexOf("_") != -1) {
          act_Num = act_Num.substring(0, act_Num.lastIndexOf("_"));
        }
        wheremap1.put("REFERENCE_NO",act_Num);
        ClientUtil.execute("TruncateTempDailyKor", wheremap1);
        //recordData.remove(rowNum);
        tbmTransfer.removeRow(rowNum);
        tbmTransfer.fireTableDataChanged();
        cashTrans = null;
    }

    private ArrayList setRow(DailyDepositTransTO DailyTrnasTo) {
        ArrayList row = new ArrayList();
        row.add(DailyTrnasTo.getAcct_num());
        row.add(DailyTrnasTo.getAmount());
        if (getOperation() == ClientConstants.ACTIONTYPE_AUTHORIZE  || getOperation() == ClientConstants.ACTIONTYPE_REJECT) {
            try {
                setAccountName(DailyTrnasTo.getAcct_num(), DailyTrnasTo.getTrans_type());
            } catch (Exception e) {
                e.printStackTrace();
            }
            row.add(getLblAccName());
            row.add("No");
        } else {
            row.add(getLblAccName());
        }
        //row.add(getLblAccName());
        System.out.println("getProdType()66666 ======== " + getProdType());
        if (CommonConstants.VENDOR != null && CommonConstants.VENDOR.equals("KOORKKENCHERRY")) {
         if (getOperation() != ClientConstants.ACTIONTYPE_AUTHORIZE  || getOperation() != ClientConstants.ACTIONTYPE_REJECT) {
        row.add(getProdType());
         }
        }
        else{
          row.add(getProdType());  
        }
        return row;
    }

    private DailyDepositTransTO updateTransferTO(DailyDepositTransTO oldAmt, DailyDepositTransTO newAmt) {
        try {
            oldAmt.setAmount(newAmt.getAmount());
            //            oldAmt.setInpAmount(newAmt.getInpAmount());
            oldAmt.setCommand(CommonUtil.convertObjToStr(getCommand()));
            oldAmt.setParticulars(newAmt.getParticulars());
            oldAmt.setTrans_type("CREDIT");
            newAmt = null;
        } catch (Exception e) {
        }
        return oldAmt;
    }

    public int insertTransferData(int rowNo) {
        cashTrans = (CashTransactionTO) setDailyTransaction();

        DailyTrnasTo = (DailyDepositTransTO) SetingForDailydepositTrans();
        ArrayList row = new ArrayList();
        if (rowNo == -1) {
            cashTrans.setTransId(getLblTransactionId());
            //            row.add(cashTrans);
            row.add(DailyTrnasTo);
            //            recordData.add(cashTrans);
            recordData.add(DailyTrnasTo);
            //            ArrayList irRow = this.setRow(cashTrans);
            ArrayList irRow = this.setRow(DailyTrnasTo);
            tbmTransfer.insertRow(tbmTransfer.getRowCount(), irRow);
        } else {
            DailyTrnasTo = updateTransferTO((DailyDepositTransTO) recordData.get(rowNo), DailyTrnasTo);
            DailyTrnasTo = setTOStatus(DailyTrnasTo);
            ArrayList irRow = setRow(DailyTrnasTo);
            recordData.set(rowNo, DailyTrnasTo);
            tbmTransfer.removeRow(rowNo);
            tbmTransfer.insertRow(rowNo, irRow);
        }
        if (getFlagSave()!= null && !getFlagSave().equals("IMPORT")) {
                 insertTempdailyGroup(DailyTrnasTo);
        }
        tbmTransfer.fireTableDataChanged();
        ttNotifyObservers();
        return 0;
    }
    public void insertTempdailyGroup(DailyDepositTransTO DailyTrnasTo)
    {
            HashMap wheremap = new HashMap();
            wheremap.put("AGENT_ID",CommonUtil.convertObjToStr(DailyTrnasTo.getAgent_no()));
            String accNo=CommonUtil.convertObjToStr(DailyTrnasTo.getAcct_num());
//            if(DailyTrnasTo.getProd_Type()!=null && !CommonUtil.convertObjToStr(DailyTrnasTo.getProd_Type()).equalsIgnoreCase("RECURRING")){
//                if (accNo.lastIndexOf("_") != -1) {
//                    accNo = accNo.substring(0, accNo.lastIndexOf("_"));
//                }
//            }
        wheremap.put("REF_NO", accNo);
        wheremap.put("AMOUNT", CommonUtil.convertObjToDouble(DailyTrnasTo.getAmount()));
        wheremap.put("date1", getProperDateFormat(DailyTrnasTo.getTrn_dt()));
        wheremap.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
        wheremap.put("COMM_AMT", new Double(0));
        if (CommonConstants.VENDOR != null && CommonConstants.VENDOR.equals("CHELLANOOR")) {
            System.out.println("rishad"+DailyTrnasTo.getProd_Type());
            if (DailyTrnasTo.getProd_Type().equals("TD")) {
                HashMap refWhereMap = new HashMap();
                String depNo = "";
                if (accNo.contains("_")) {
                    depNo = accNo.substring(0, accNo.lastIndexOf("_"));
                } else {
                    depNo = accNo;
                }
                refWhereMap.put("DEPOSIT_NO", depNo);
                List refResult = ClientUtil.executeQuery("getDepositReferenceNo", refWhereMap);
                if (refResult != null && refResult.size() > 0) {
                    HashMap refResultMap = new HashMap();
                    refResultMap = (HashMap) refResult.get(0);
                    if (refResultMap.containsKey("REFERENCE_NO") && refResultMap.get("REFERENCE_NO") != null) {
                        wheremap.put("REF_NO", refResultMap.get("REFERENCE_NO"));
                        wheremap.put("REFERENCE_NO", refResultMap.get("REFERENCE_NO"));
                    }
                }
            } else {
                wheremap.put("REFERENCE_NO", accNo);
            }
            List existsList = ClientUtil.executeQuery("getDataAlreadyExistsTempDaily", wheremap);
            if (existsList != null && existsList.size() > 0) {
                ClientUtil.execute("TruncateTempDailyInd", wheremap);
            }
            ClientUtil.execute("InsertIntoTempChell", wheremap);
            
        } else {
            List existsList = ClientUtil.executeQuery("getDataAlreadyExistsTemp", wheremap);
            if (existsList != null && existsList.size() > 0) {
                wheremap.put("REFERENCE_NO", accNo);
                ClientUtil.execute("TruncateTempDailyKor", wheremap);
            }
            ClientUtil.execute("InsertIntoTempKor", wheremap);
            }
    }
    public int insertTableDataFromFile(ArrayList totCustList) {
        if (totCustList != null && totCustList.size() > 0) {
            for (int i = 0; i < totCustList.size(); i++) {
                ArrayList singleList = (ArrayList) totCustList.get(i);
                tbmTransfer.insertRow(tbmTransfer.getRowCount(), singleList);
                tbmTransfer.fireTableDataChanged();
                ttNotifyObservers();

            }
        }
        return 0;
    }
    
    public Date getProperDateFormat(Object obj) {
        Date currDate = null;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            currDate = (Date) currDt.clone();
            currDate.setDate(tempDt.getDate());
            currDate.setMonth(tempDt.getMonth());
            currDate.setYear(tempDt.getYear());
        }
        return currDate;
    }

    
    public void doAction() {
        TTException exception = null;
        log.info("In doAction()");
        try {
            //If actionType such as NEW, EDIT, DELETE, then proceed
            if (actionType != ClientConstants.ACTIONTYPE_CANCEL) {
                //If actionType has got propervalue then doActionPerform, else throw error
                if (getCommand() != null || getAuthorizeMap() != null) {
                    doActionPerform("");
                }
            } else {
                log.info("Action Type Not Defined In setDailyDepositTrans)");
            }
        } catch (Exception e) {
            log.info("Error In doAction()");
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            e.printStackTrace();
            if (e instanceof TTException) {
                exception = (TTException) e;
            }
        }

        // If TT Exception
        if (exception != null) {
            HashMap exceptionHashMap = exception.getExceptionHashMap();
            if (exceptionHashMap != null) {
                ArrayList list = (ArrayList) exceptionHashMap.get(CommonConstants.EXCEPTION_LIST);
                //(list.get(0) instanceof String && "IB".equalsIgnoreCase((String)list.get(0))) ||
                if (list.size() == 1 && list.get(0) instanceof String && ((String) list.get(0)).startsWith("SUSPECIOUS")) {
                    Object[] dialogOption = {"Exception", "Cancel"};
                    parseException.setDialogOptions(dialogOption);
                    if (parseException.logException(exception, true) == 0) {
                        try {
                            setResult(actionType);
                            doActionPerform("EXCEPTION");
                        } catch (Exception e1) {
                            log.info("Error In doAction()");
                            e1.printStackTrace();
                            if (e1 instanceof TTException) {
                                Object[] dialogOption1 = {"OK"};
                                parseException.setDialogOptions(dialogOption1);
                                exception = (TTException) e1;
                                parseException.logException(exception, true);
                            }
                        }
                    }
                    Object[] dialogOption1 = {"OK"};
                    parseException.setDialogOptions(dialogOption1);
                } else {
                    parseException.logException(exception, true);
                }
            } else { // To Display Transaction No showing String message
                parseException.logException(exception, true);
                setResult(actionType);
            }
        }
    }

    public void resetDepSubNo() {
        setTxtAccNo("");
        setTxtAmount("");
        setTxtInitiatorChannel("");
        setLblAccName("");
        setBalance("");
        try {
            setAccountName("", "");
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    private void setStatusForTOs() {
        DailyDepositTransTO DailyTrnasTo;
        this.setTransStatus(CommonConstants.STATUS_DELETED);
        int size = this.recordData.size();
        for (int i = 0; i < size; i++) {
            DailyTrnasTo = (DailyDepositTransTO) recordData.get(i);
            DailyTrnasTo = this.setTOStatus(DailyTrnasTo);
            recordData.set(i, DailyTrnasTo);
        }
        DailyTrnasTo = null;
    }

    private HashMap populateBean() throws Exception {
        HashMap dailyTrans = new HashMap();
        System.out.println("operation ===========  " + operation);
        //  System.out.println("operation ===========  "+operation);
        if (operation == ClientConstants.ACTIONTYPE_NEW) {
             if (CommonConstants.VENDOR != null && CommonConstants.VENDOR.equals("KOORKKENCHERRY")|| CommonConstants.VENDOR.equals("CHELLANOOR")) {
                 ArrayList cashTOs = setCashTo();
                if (cashTOs != null && cashTOs.size() > 0) {
                    dailyTrans.put("CashTransactionTO", cashTOs);
                }
                if(CommonConstants.VENDOR.equals("KOORKKENCHERRY") ){
                	ArrayList transferTOs = setTransferKoorTo();
                	if (transferTOs != null && transferTOs.size() > 0) {
                    	dailyTrans.put("TxTransferTO", transferTOs);
                	}
                }               
            }else if(CommonConstants.VENDOR.equals("PERINGANDUR") ){
                	ArrayList transferTOs = setTransferKoorTo();
                	if (transferTOs != null && transferTOs.size() > 0) {
                            dailyTrans.put("TxTransferTO", transferTOs);
                	}
           }else{
                 ArrayList transferTOs = setTransferTo();
                if (transferTOs != null && transferTOs.size() > 0) {
                    dailyTrans.put("TxTransferTO", transferTOs);
                }
            }
            dailyTrans.put("ACTION", "NEW");
            setOperation(ClientConstants.ACTIONTYPE_NEW);
            dailyTrans.put("COMMAND", CommonConstants.TOSTATUS_INSERT);
        } else if (operation == ClientConstants.ACTIONTYPE_EDIT) {
            ArrayList arrList = new ArrayList();
            HashMap whereMap = new HashMap();
//             ArrayList transferTOs = setTransferTo();
//             dailyTrans.put("TxTransferTO", transferTOs);
            whereMap.put("BATCHID", getLblTransactionId());
            whereMap.put("TRANS_DT", currDt.clone());
            whereMap.put("INITIATED_BRANCH", TrueTransactMain.BRANCH_ID);
            arrList = (ArrayList) ClientUtil.executeQuery("getBatchTxTransferTOs", whereMap);
            HashMap oldAmountMap = new HashMap();
            ArrayList txtTolist = new ArrayList();
            for (int i = 0; i < arrList.size(); i++) {

                TxTransferTO trnTo = (TxTransferTO) arrList.get(i);
                oldAmountMap.put(trnTo.getTransId(), trnTo.getAmount());
                trnTo.setStatus(CommonConstants.STATUS_MODIFIED);
                trnTo.setAmount(new Double(getTotalGlAmt()));
                trnTo.setInpAmount(new Double(getTotalGlAmt()));
                trnTo.setTransMode(CommonConstants.TX_TRANSFER);
                System.out.println("getTotalGlAmt()******" + getTotalGlAmt());
                txtTolist.add(trnTo);

            }
            dailyTrans.put("TxTransferTO", txtTolist);
            dailyTrans.put("OLDAMOUNT", oldAmountMap);
            dailyTrans.put("ACTION", "UPDATE");
            dailyTrans.put("BATCH_ID", getLblTransactionId());
            whereMap = null;
            oldAmountMap = null;
            arrList = null;
            setOperation(ClientConstants.ACTIONTYPE_EDIT);
            dailyTrans.put("COMMAND", CommonConstants.TOSTATUS_UPDATE);
        } else if (operation == ClientConstants.ACTIONTYPE_DELETE) {
//             ArrayList transferTOs = setTransferTo();
//            dailyTrans.put("TxTransferTO", transferTOs);
            setOperation(ClientConstants.ACTIONTYPE_DELETE);
            dailyTrans.put("COMMAND", CommonConstants.TOSTATUS_DELETE);
            setStatusForTOs();
            dailyTrans.put("ACTION", "DELETE");
            dailyTrans.put("BATCH_ID", getLblTransactionId());
            ArrayList arrList = new ArrayList();
            HashMap whereMap = new HashMap();
            whereMap.put("BATCHID", getLblTransactionId());
            whereMap.put("TRANS_DT", currDt.clone());
            whereMap.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
            arrList = (ArrayList) ClientUtil.executeQuery("getBatchTxTransferTOs", whereMap);
            HashMap oldAmountMap = new HashMap();
            ArrayList txtTolist = new ArrayList();
            for (int i = 0; i < arrList.size(); i++) {
                TxTransferTO trnTo = (TxTransferTO) arrList.get(i);
                trnTo.setStatus(CommonConstants.STATUS_DELETED);
                trnTo.setAmount(new Double(getTotalGlAmt()));
                trnTo.setInpAmount(new Double(getTotalGlAmt()));
                System.out.println("getTotalGlAmt()******" + getTotalGlAmt());
                txtTolist.add(trnTo);
                oldAmountMap.put(trnTo.getTransId(), trnTo.getAmount());
            }
            dailyTrans.put("TxTransferTO", txtTolist);
            //            dailyTrans.put("OLDAMOUNT", oldAmountMap);

            dailyTrans.put("BATCH_ID", getLblTransactionId());
            whereMap = null;
            oldAmountMap = null;
            arrList = null;

        }
        if (deleteData.size() > 0) {
            dailyTrans.put("DELETEDDATALIST", deleteData);
        }
        dailyTrans.put("DENOMINATION_LIST", getDenominationList());
        dailyTrans.put("DAILYDEPOSITTRANSTO", new ArrayList(recordData));
        if (CommonConstants.VENDOR != null && CommonConstants.VENDOR.equals("KOORKKENCHERRY")|| CommonConstants.VENDOR.equals("CHELLANOOR")) {
                dailyTrans.put("CASH_DAILY","CASH_DAILY");
        }
        
        //        dailyTrans.put("OLDAMOUNT", new Double(oldAmount));
        //        dailyTrans.put("PRODUCTTYPE", cashTrans.getProdType());
        dailyTrans.put(CommonConstants.MODULE, getModule());
        dailyTrans.put(CommonConstants.SCREEN, getScreen());
        System.out.println("########recordData.get(1) :" + recordData.get(0));
        System.out.println("########UpdatedailyTrans :" + dailyTrans);
        System.out.println("########recordData.get(1) :" + recordData.size());
        System.out.println("########recordData.get(1) :" + recordData);

        return dailyTrans;
    }

    private void denominationAmountCalc() {
    }

    /** To perform the necessary action */
    private void doActionPerform(String parameter) throws Exception {
        log.info("In doActionPerform()");
        final HashMap data = new HashMap();
        TTException exception = null;

        //        operationMap.put("DEPOSIT_NO",getTxtAccNo());
        //        operationMap.put("AMOUNT",getBalance());
        //        operationMap.put("AGENT_ID",getCboAgentType());
        //        operationMap.put("DENOMINATION_LIST", getDenominationList());
        //        operationMap.put("OLDAMOUNT", new Double(oldAmount));
        try {
            if (getAuthorizeMap() != null) {
                data.put(CommonConstants.MODULE, getModule());
                data.put(CommonConstants.SCREEN, getScreen());
                _authorizeMap.put("DAILY", "DAILY");
                data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
                System.out.println("recordData^^^^^^^^^^^$$$" + recordData);
                data.put("DAILYDEPOSITTRANSTO", recordData);
                System.out.println("data in auth^^^^^^^^^$$$" + data);
                data.put("BATCH_ID", getLblTransactionId());
                proxy.execute(data, operationMap);
                if (parameter != null && "EXCEPTION".equalsIgnoreCase(parameter)) {
                    data.put("EXCEPTION", "EXCEPTION");
                }
                System.out.println("Excpetion" + data);
            } else {

                //                ArrayList transferTOs = setTransferTo();
                //                data.put("TxTransferTO", transferTOs);
                data.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
                System.out.println("data SAVE ^^^^^^^^^$$$" + populateBean());
                HashMap proxyReturnMap = proxy.execute(populateBean(), operationMap);
                setProxyReturnMap(proxyReturnMap);//, frame);
                if (proxyReturnMap != null && proxyReturnMap.containsKey(CommonConstants.TRANS_ID)) {
                    ClientUtil.showMessageWindow("Daily Deposit Transaction No... : " + CommonUtil.convertObjToStr(proxyReturnMap.get(CommonConstants.TRANS_ID)));
                }
            }
            //        setResult(actionType);
            setResult(getOperation());
            operation = ClientConstants.ACTIONTYPE_CANCEL;
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            e.printStackTrace();

            if (e instanceof TTException) {
                exception = (TTException) e;
            }
        }
        // If TT Exception
        if (exception != null) {
            HashMap exceptionHashMap = exception.getExceptionHashMap();
            if (exceptionHashMap != null) {
                parseException.logException(exception, true);
            } else { // To Display Transaction No showing String message
                parseException.logException(exception, true);
                setResult(getOperation());
            }
        }

//        resetForm();
    }

    public void resetOBFields() {
        this.setCboAgentType("");
        this.tbmTransfer.setData(new ArrayList());
        this.tbmTransfer.fireTableDataChanged();
        this.recordData.clear();
        this.deleteData.clear();
    }

    public CashTransactionTO setDailyTransaction() {
        log.info("In setDailyTransaction()");
        CashTransactionTO objCashTransactionTO = new CashTransactionTO();
        try {
            objCashTransactionTO.setProdType("TD");
            objCashTransactionTO.setAcHdId(CommonUtil.convertObjToStr(getTxtAccHd()));
            objCashTransactionTO.setActNum(CommonUtil.convertObjToStr(getTxtAccNo()));
            objCashTransactionTO.setInpAmount(new Double(getOldAmount()));
            objCashTransactionTO.setAmount(CommonUtil.convertObjToDouble(getTxtAmount()));
            objCashTransactionTO.setTransType("CREDIT");
            objCashTransactionTO.setProdId(CommonUtil.convertObjToStr(getProdId()));
            objCashTransactionTO.setBranchId(getSelectedBranchID());
            objCashTransactionTO.setStatusBy(ProxyParameters.USER_ID);
            objCashTransactionTO.setInitTransId(ProxyParameters.USER_ID);
            objCashTransactionTO.setInitChannType(CommonUtil.convertObjToStr(getTxtInitiatorChannel()));
            objCashTransactionTO.setParticulars(getCboAgentType());
            objCashTransactionTO.setInitiatedBranch(ProxyParameters.BRANCH_ID);
            objCashTransactionTO.setCommand(CommonUtil.convertObjToStr(getCommand()));
            objCashTransactionTO.setStatus(CommonUtil.convertObjToStr(getTransStatus()));
            System.out.println("DailyDepositTransTO:" + objCashTransactionTO);
        } catch (Exception e) {
            log.info("Error In setInwardClearing()");
            e.printStackTrace();
        }
        return objCashTransactionTO;
    }

    // Setter for Authorization.
    public void setAuthorizeMap(HashMap authorizeMap) {
        _authorizeMap = authorizeMap;
    }

    // Getter for Authorization.
    public HashMap getAuthorizeMap() {
        return _authorizeMap;
    }

    // to decide which action Should be performed...
    private String getCommand() throws Exception {
        log.info("In getCommand()");

        String command = null;
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
            default:
        }
        return command;
    }

    // Returns the Current Value of Action type...
    public int getActionType() {
        return actionType;
    }

    // Sets the value of the Action Type to the poperation we want to execute...
    public void setActionType(int actionType) {
        this.actionType = actionType;
        setChanged();
    }

    // To reset all the fields in the UI
    public void resetForm() {
        log.info("In resetForm()");
        try{
        oldAmount = 0.0;
        _authorizeMap = null;
        setTxtAccNo("");
        setProdType("");
        setTxtAmount("");
        //        setTxtAccHd("");
        setCboAgentType("");
        setBalance("");
        setTxtInitiatorChannel("");
        setDenominationList(null);
        setLblTransDate(DateUtil.getStringDate((Date) currDt.clone()));
        ttNotifyObservers();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void resetTransactionDetails() {
        log.info("In resetForm()");
        oldAmount = 0.0;
        _authorizeMap = null;
        setTxtAccNo("");
        setProdType("");
        setTxtInitiatorChannel("");
        setRdoTransactionType_Credit(false);
        setTxtAmount("");
        //        setTxtAccHd("");
        setCboAgentType("");
        setBalance("");
        setTxtInitiatorChannel("");
        setDenominationList(null);
        ttNotifyObservers();
    }

    //To reset all the Lables in the UI...
    public void resetLable() {
        //        this.setTxtAccHd("");
        //        this.setCboAgentType("");
        this.setLblAccName("");
        //        this.setLblTransactionId("");
        //        this.setLblTransDate("");
        this.setBalance("");
        //        this.setLblInitiatorId("");
        //        this.setLblTransactionId("");
        //        this.setLblTransDate("");
        //        this.setLblInitiatorId("");

    }

    // To set and change the Status of the lable STATUS
    public void setResult(int result) {
        this.result = result;
        setChanged();
    }

    public int getResult() {
        return this.result;
    }

    // To set the Value of the lblStatus...
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }

    public String getLblStatus() {
        return lblStatus;
    }

    //To reset the Value of lblStatus after each save action...
    public void resetStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }

    /** To set the status based on ActionType, either New, Edit, etc., */
    public void setStatus() {
        this.setLblStatus(ClientConstants.ACTION_STATUS[this.getActionType()]);
        ttNotifyObservers();
    }

    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus() {
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        ttNotifyObservers();
    }

    public void ttNotifyObservers() {
        notifyObservers();
    }

    public void setInitiatorChannelValue() {
        setTxtInitiatorChannel(INITIATORTYPE);
        ttNotifyObservers();
    }

    public void setTxtAccNo(String txtAccNo) {
        this.txtAccNo = txtAccNo;
        setChanged();
    }

    public String getTxtAccNo() {
        return this.txtAccNo;
    }

    public void setTxtInitiatorChannel(String txtInitiatorChannel) {
        this.txtInitiatorChannel = txtInitiatorChannel;
        setChanged();
    }

    public String getTxtInitiatorChannel() {
        return this.txtInitiatorChannel;
    }

    void setRdoTransactionType_Credit(boolean rdoTransactionType_Credit) {
        this.rdoTransactionType_Credit = rdoTransactionType_Credit;
        setChanged();
    }

    boolean getRdoTransactionType_Credit() {
        return this.rdoTransactionType_Credit;
    }

    void setTxtAmount(String txtAmount) {
        this.txtAmount = txtAmount;
        setChanged();
    }

    String getTxtAmount() {
        return this.txtAmount;
    }

    //To Set the Lables In the UI...
    //==========================================
    public void setTxtAccHd(String txtAccHdId) {
        this.txtAccHdId = txtAccHdId;
        setChanged();
    }

    public String getTxtAccHd() {
        return this.txtAccHdId;
    }

    public void setAccountHead() {
        //        try {
        //        HashMap accHead = new HashMap();
        //        accHead.put("PROD_ID", CommonUtil.convertObjToStr("PROD_ID"));
        //        List lst =
        //
        //        }catch(Exception e){
        //        }
    }
    // For setting the Name of the Account Number Holder...

    public void setLblAccName(String lblAccName) {
        this.lblAccName = lblAccName;
        setChanged();
    }

    public String getLblAccName() {
        return this.lblAccName;
    }

    public int populateTransfer(String transId) {
        CashTransactionTO obj;
        int size = this.recordData.size();
        for (int i = 0; i < size; i++) {
            obj = (CashTransactionTO) recordData.get(i);
            if (obj.getTransId().equals(transId)) {
                populateDailyTransfer(i);
                obj = null;
                return i;
            }
        }
        return -1;
    }

    public void setAccountName(String AccountNo, String Prod_Type) throws Exception {
        if (AccountNo != null && AccountNo.length() > 0) {
            try {
                final HashMap accountNameMap = new HashMap();
                accountNameMap.put("ACC_NUM", AccountNo);
                List resultList = null;
                if (CommonConstants.VENDOR != null && CommonConstants.VENDOR.equals("PERINGANDUR")) {
                    if (Prod_Type.equals("SA")) {
                         resultList = ClientUtil.executeQuery("getAccountNumberNameSA", accountNameMap);
                    }
                    else if (Prod_Type.equals("OA")) {
                         resultList = ClientUtil.executeQuery("getAccountNumberNameOA", accountNameMap);
                    }        
                    else{
                       resultList = ClientUtil.executeQuery("getAccountNumberForDeposit", accountNameMap);  
                    }
                } 
                else if (CommonConstants.VENDOR != null && CommonConstants.VENDOR.equals("KOORKKENCHERRY")) {
                    if (Prod_Type.equals("SA")) {
                         resultList = ClientUtil.executeQuery("getAccountNumberNameSA", accountNameMap);
                    }
                    else if (Prod_Type.equals("OA")) {
                         resultList = ClientUtil.executeQuery("getAccountNumberNameOA", accountNameMap);
                    }        
                    else{
                       resultList = ClientUtil.executeQuery("getAccountNumberForDeposit", accountNameMap);  
                    }
                }
                else {
                    resultList = ClientUtil.executeQuery("getAccountNumberForDeposit", accountNameMap);
                }
                if (resultList != null && resultList.size() > 0) {
                    final HashMap resultMap = (HashMap) resultList.get(0);
                    setLblAccName(resultMap.get("CUSTOMER_NAME").toString());
                    setBalance(CommonUtil.convertObjToStr(resultMap.get("AMOUNT")));
                    //            setTxtAccNo(CommonUtil.convertObjToStr(resultMap.get("ACT_NUM")));
                    //            setTxtAccNo(AccountNo);
                    setTxtAccNo(AccountNo);
                    setProdType(Prod_Type);
                } else {
//                    Exception e = null;
//                    throw e;
                }
            } catch (Exception e) {
                throw e;
            }
        }
    }

    // For setting the transaction Id in UI at the Time of Edit or Delete...
    public void setLblTransactionId(String lblTransactionId) {
        this.lblTransactionId = lblTransactionId;
        setChanged();
    }

    public String getLblTransactionId() {
        return this.lblTransactionId;
    }

    // For setting the Name of the Clearing Date in Ui at the time of Edit and Delete...
    public void setLblTransDate(String lblTransDate) {
        this.lblTransDate = lblTransDate;
        setChanged();
    }

    public String getLblTransDate() {
        return this.lblTransDate;
    }

    // For setting the Initiator Id in UI at the Time of Edit or Delete...
    public void setLblInitiatorId(String lblInitiatorId) {
        this.lblInitiatorId = lblInitiatorId;
        setChanged();
    }

    public String getLblInitiatorId() {
        return this.lblInitiatorId;
    }

    /**
     * Getter for property cr_cash.
     * @return Value of property cr_cash.
     */
    public java.lang.String getCr_cash() {
        return cr_cash;
    }

    /**
     * Setter for property cr_cash.
     * @param cr_cash New value of property cr_cash.
     */
    public void setCr_cash(java.lang.String cr_cash) {
        this.cr_cash = cr_cash;
    }

    /**
     * Getter for property dr_cash.
     * @return Value of property dr_cash.
     */
    public java.lang.String getDr_cash() {
        return dr_cash;
    }

    /**
     * Setter for property dr_cash.
     * @param dr_cash New value of property dr_cash.
     */
    public void setDr_cash(java.lang.String dr_cash) {
        this.dr_cash = dr_cash;
    }

    /**
     * Getter for property denominationList.
     * @return Value of property denominationList.
     */
    public java.util.ArrayList getDenominationList() {
        return denominationList;
    }

    /**
     * Setter for property denominationList.
     * @param denominationList New value of property denominationList.
     */
    public void setDenominationList(java.util.ArrayList denominationList) {
        this.denominationList = denominationList;
    }

    /**
     * Getter for property balance.
     * @return Value of property balance.
     */
    public java.lang.String getBalance() {
        return balance;
    }

    /**
     * Setter for property balance.
     * @param balance New value of property balance.
     */
    public void setBalance(java.lang.String balance) {
        this.balance = balance;
    }

    /**
     * Getter for property cbmAgentType.
     * @return Value of property cbmAgentType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmAgentType() {
        return cbmAgentType;
    }

    /**
     * Setter for property cbmAgentType.
     * @param cbmAgentType New value of property cbmAgentType.
     */
    public void setCbmAgentType(com.see.truetransact.clientutil.ComboBoxModel cbmAgentType) {
        this.cbmAgentType = cbmAgentType;
    }

    /**
     * Getter for property depSubNoStatus.
     * @return Value of property depSubNoStatus.
     */
    public java.lang.String getDepSubNoStatus() {
        return depSubNoStatus;
    }

    /**
     * Setter for property depSubNoStatus.
     * @param depSubNoStatus New value of property depSubNoStatus.
     */
    public void setDepSubNoStatus(java.lang.String depSubNoStatus) {
        this.depSubNoStatus = depSubNoStatus;
    }

    /**
     * Getter for property cboAgentType.
     * @return Value of property cboAgentType.
     */
    public java.lang.String getCboAgentType() {
        return cboAgentType;
    }

    /**
     * Setter for property cboAgentType.
     * @param cboAgentType New value of property cboAgentType.
     */
    public void setCboAgentType(java.lang.String cboAgentType) {
        this.cboAgentType = cboAgentType;
        setChanged();
    }

    /**
     * Getter for property depDailybNoK.
     * @return Value of property depDailybNoK.
     */
    public java.lang.String getDepDailybNoK() {
        return depDailybNoK;
    }

    /**
     * Setter for property depDailybNoK.
     * @param depDailybNoK New value of property depDailybNoK.
     */
    public void setDepDailybNoK(java.lang.String depDailybNoK) {
        this.depDailybNoK = depDailybNoK;
    }

    /**
     * Getter for property depSubNoMode.
     * @return Value of property depSubNoMode.
     */
    public java.lang.String getDepSubNoMode() {
        return depSubNoMode;
    }

    /**
     * Setter for property depSubNoMode.
     * @param depSubNoMode New value of property depSubNoMode.
     */
    public void setDepSubNoMode(java.lang.String depSubNoMode) {
        this.depSubNoMode = depSubNoMode;
    }

    /**
     * Getter for property tbmTransfer.
     * @return Value of property tbmTransfer.
     */
    public com.see.truetransact.clientutil.TableModel getTbmTransfer() {
        return tbmTransfer;
    }

    /**
     * Setter for property tbmTransfer.
     * @param tbmTransfer New value of property tbmTransfer.
     */
    public void setTbmTransfer(com.see.truetransact.clientutil.TableModel tbmTransfer) {
        this.tbmTransfer = tbmTransfer;
        setChanged();
    }

    /**
     * Getter for property recordData.
     * @return Value of property recordData.
     */
    public java.util.ArrayList getRecordData() {
        return recordData;
    }
    //
    //    /**
    //     * Setter for property recordData.
    //     * @param recordData New value of property recordData.
    //     */
    //    public void setRecordData(java.util.ArrayList recordData) {
    //        this.recordData = recordData;
    //    }
    //
    //    /**
    //     * Getter for property .
    //     * @return Value of property record.
    //     */
    //    public java.util.LinkedHashMap getRecord() {
    //        return record;
    //    }

    /**
     * Setter for property record.
     * @param record New value of property record.
     */
    //    public void setRecord(java.util.LinkedHashMap record) {
    //        this.record = record;
    //    }
    /**
     * Getter for property deleteData.
     * @return Value of property deleteData.
     */
    public java.util.ArrayList getDeleteData() {
        return deleteData;
    }

    /**
     * Setter for property deleteData.
     * @param deleteData New value of property deleteData.
     */
    public void setDeleteData(java.util.ArrayList deleteData) {
        this.deleteData = deleteData;
    }

    /**
     * Getter for property prodId.
     * @return Value of property prodId.
     */
    public java.lang.String getProdId() {
        return prodId;
    }

    /**
     * Setter for property prodId.
     * @param prodId New value of property prodId.
     */
    public void setProdId(java.lang.String prodId) {
        this.prodId = prodId;
    }

    /**
     * Getter for property prod_desc.
     * @return Value of property prod_desc.
     */
    public java.lang.String getProd_desc() {
        return prod_desc;
    }

    /**
     * Setter for property prod_desc.
     * @param prod_desc New value of property prod_desc.
     */
    public void setProd_desc(java.lang.String prod_desc) {
        this.prod_desc = prod_desc;
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
     * Getter for property operation.
     * @return Value of property operation.
     */
    public int getOperation() {
        return operation;
    }

    /**
     * Setter for property operation.
     * @param operation New value of property operation.
     */
    public void setOperation(int operation) {
        this.operation = operation;
    }

    /**
     * Getter for property oldAmount.
     * @return Value of property oldAmount.
     */
    public double getOldAmount() {
        return oldAmount;
    }

    /**
     * Setter for property oldAmount.
     * @param oldAmount New value of property oldAmount.
     */
    public void setOldAmount(double oldAmount) {
        this.oldAmount = oldAmount;
    }

    public DailyDepositTransTO SetingForDailydepositTrans() {
        DailyDepositTransTO daily = new DailyDepositTransTO();
        daily.setColl_dt(getTdtInstrumentDate());
        System.out.println("getTxtAccNo() =====" + getTxtAccNo());
        daily.setAcct_num(CommonUtil.convertObjToStr(getTxtAccNo()));
        daily.setAgent_no(CommonUtil.convertObjToStr(getCboAgentType()));
        daily.setAmount(CommonUtil.convertObjToDouble(getTxtAmount()));
        daily.setCreated_by(ProxyParameters.USER_ID);
        daily.setCreated_dt(currDt);
        daily.setInitiatedBranch(ProxyParameters.BRANCH_ID);
        //        daily.setTotal_bal(new Double(getTotalAmt()));
        daily.setTrn_dt(currDt);
        daily.setTrans_mode(CommonConstants.TX_TRANSFER);
        daily.setTrans_type(CommonConstants.CREDIT);
        // daily.setTrans_type(getProdType());//"D");
        daily.setStatus(getTransStatus());
        daily.setBatch_id(getLblTransactionId());
        System.out.println("getProdType() =====" + getProdType());
        daily.setProd_Type(getProdType());
        daily.setScreenName(getScreen());// Rework - KD-2034 by nithya

        return daily;
    }

    /**
     * Getter for property tdtInstrumentDate.
     * @return Value of property tdtInstrumentDate.
     */
    public java.util.Date getTdtInstrumentDate() {
        return tdtInstrumentDate;
    }

    /**
     * Setter for property tdtInstrumentDate.
     * @param tdtInstrumentDate New value of property tdtInstrumentDate.
     */
    public void setTdtInstrumentDate(java.util.Date tdtInstrumentDate) {
        this.tdtInstrumentDate = tdtInstrumentDate;
    }

    /**
     * Getter for property totalAmt.
     * @return Value of property totalAmt.
     */
    public double getTotalAmt() {
        return totalAmt;
    }

    /**
     * Setter for property totalAmt.
     * @param totalAmt New value of property totalAmt.
     */
    public void setTotalAmt(double totalAmt) {
        this.totalAmt = totalAmt;
    }
    public ArrayList setCashTo(){
        try{
            ArrayList lst = new ArrayList();
            CashTransactionTO cashTO=new CashTransactionTO();
            HashMap where = new HashMap();
            HashMap result = new HashMap();
            List CollAmountList=null;
            where.put("AGENT_ID", getCboAgentType());
            where.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
            if(CommonConstants.VENDOR.equals("KOORKKENCHERRY")){
            CollAmountList = ClientUtil.executeQuery("getCollectionAmountCashKor", where);}
            else if(CommonConstants.VENDOR.equals("CHELLANOOR"))
            {
               CollAmountList = ClientUtil.executeQuery("getCollectionAmount", where);
            }
            for (int i = 0; i < CollAmountList.size(); i++) {
                result = (HashMap) CollAmountList.get(i);
                cashTO = new CashTransactionTO();
                cashTO.setAmount(CommonUtil.convertObjToDouble(result.get("AMOUNT")));
                cashTO.setInpAmount(CommonUtil.convertObjToDouble(result.get("AMOUNT")));
                cashTO.setAcHdId(CommonUtil.convertObjToStr(result.get("ACCT_HEAD")));
                cashTO.setTransType(CommonConstants.CREDIT);
                cashTO.setTransDt(currDt);
                cashTO.setInitTransId(ProxyParameters.USER_ID);
                cashTO.setBranchId(TrueTransactMain.BRANCH_ID);
                cashTO.setInitiatedBranch(TrueTransactMain.BRANCH_ID);
                //cashTO.setInstDt(getTdtInstrumentDate());
                cashTO.setProdType("GL");
                cashTO.setLinkBatchId(getCboAgentType());
                cashTO.setStatus(CommonConstants.STATUS_CREATED);
                cashTO.setStatusBy(ProxyParameters.USER_ID);
                cashTO.setTransModType(TransactionFactory.GL);
                cashTO.setStatusDt(currDt);
                cashTO.setParticulars(getCboAgentType() );
                cashTO.setInstType("VOUCHER");
                //cashTO.setAuthorizeStatus("DAILY");
                cashTO.setGlTransActNum(CommonUtil.convertObjToStr(getCboAgentType()));
                cashTO.setCommand(CommonUtil.convertObjToStr(getCommand()));
                lst.add(cashTO);
            }
            System.out.println("lst************" + lst);
            System.out.println("lst************" + lst.size());
            return lst;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public ArrayList setTransferKoorTo(){
        TxTransferTO trnsferTO = new TxTransferTO();
        ArrayList lst = new ArrayList();
        HashMap where1 = new HashMap();
        HashMap result1 = new HashMap();
        where1.put("AGENT_ID", getCboAgentType());
        where1.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
        
        List CollAmountIndCredit = ClientUtil.executeQuery("getCollectionAmountIndKor", where1);
        for (int j = 0; j < CollAmountIndCredit.size(); j++) {
            result1 = (HashMap) CollAmountIndCredit.get(j);
            trnsferTO = new TxTransferTO();
            trnsferTO.setProdType(CommonUtil.convertObjToStr(result1.get("PROD_TYPE")));
            trnsferTO.setProdId(CommonUtil.convertObjToStr(result1.get("PROD_ID")));
            trnsferTO.setTransModType(CommonUtil.convertObjToStr(result1.get("PROD_TYPE")));
            trnsferTO.setAcHdId(CommonUtil.convertObjToStr(result1.get("CR_HEAD")));
            trnsferTO.setAmount(CommonUtil.convertObjToDouble(result1.get("AMOUNT"))-CommonUtil.convertObjToDouble(result1.get("COMM_AMT")));
            trnsferTO.setInpAmount(CommonUtil.convertObjToDouble(result1.get("AMOUNT"))-CommonUtil.convertObjToDouble(result1.get("COMM_AMT")));
            trnsferTO.setTransType(CommonConstants.CREDIT);
            trnsferTO.setActNum(CommonUtil.convertObjToStr(result1.get("REFERENCE_NO")));
            trnsferTO.setTransMode(CommonConstants.TX_TRANSFER);
            trnsferTO.setBranchId(TrueTransactMain.BRANCH_ID);
            trnsferTO.setInitiatedBranch(TrueTransactMain.BRANCH_ID);
           // trnsferTO.setInstDt(getTdtInstrumentDate());
            trnsferTO.setTransDt(ClientUtil.getCurrentDateWithTime());
            trnsferTO.setParticulars(getCboAgentType() +" Agent Comm From "+CommonUtil.convertObjToStr(result1.get("REFERENCE_NO")));
            //trnsferTO.setLinkBatchId(getCboAgentType());
            trnsferTO.setStatus(CommonConstants.STATUS_CREATED);
            trnsferTO.setStatusBy(ProxyParameters.USER_ID);
            trnsferTO.setStatusDt(currDt);
            trnsferTO.setInstType("VOUCHER");
            trnsferTO.setAuthorizeStatus("DAILY");
            trnsferTO.setTransModType(CommonUtil.convertObjToStr(result1.get("PROD_TYPE")));
            trnsferTO.setGlTransActNum(CommonUtil.convertObjToStr(getCboAgentType()));
            lst.add(trnsferTO);
            if(CommonUtil.convertObjToDouble(result1.get("COMM_AMT"))>0){
                 trnsferTO = new TxTransferTO();
                trnsferTO.setProdType("GL");
                //trnsferTO.setProdId(CommonUtil.convertObjToStr(result1.get("PROD_ID")));
                trnsferTO.setAcHdId(CommonUtil.convertObjToStr(result1.get("COMM_COL_AC_HD_ID")));
                trnsferTO.setAmount(CommonUtil.convertObjToDouble(result1.get("COMM_AMT")));
                trnsferTO.setInpAmount(CommonUtil.convertObjToDouble(result1.get("COMM_AMT")));
                trnsferTO.setTransType(CommonConstants.CREDIT);
                trnsferTO.setTransMode(CommonConstants.TX_TRANSFER);
                trnsferTO.setTransModType(TransactionFactory.GL);
                trnsferTO.setBranchId(TrueTransactMain.BRANCH_ID);
                trnsferTO.setInitiatedBranch(TrueTransactMain.BRANCH_ID);
               // trnsferTO.setInstDt(getTdtInstrumentDate());
                trnsferTO.setTransDt(ClientUtil.getCurrentDateWithTime());
                trnsferTO.setParticulars(getCboAgentType() +" Agent Comm From "+CommonUtil.convertObjToStr(result1.get("REFERENCE_NO")));
                //trnsferTO.setLinkBatchId(getCboAgentType());
                trnsferTO.setStatus(CommonConstants.STATUS_CREATED);
                trnsferTO.setStatusBy(ProxyParameters.USER_ID);
                trnsferTO.setStatusDt(currDt);
                trnsferTO.setInstType("VOUCHER");
                trnsferTO.setAuthorizeStatus("DAILY");
                trnsferTO.setGlTransActNum(CommonUtil.convertObjToStr(getCboAgentType()));
                trnsferTO.setTransModType("GL");
                lst.add(trnsferTO);
                }
        }
        System.out.println("lst************" + lst);
           HashMap where = new HashMap();
            HashMap result = new HashMap();
            where.put("AGENT_ID", getCboAgentType());
            where.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
            List CollAmountListKorr = ClientUtil.executeQuery("getCollectionAmountCashKor", where);
            for (int i = 0; i < CollAmountListKorr.size(); i++) {
                result = (HashMap) CollAmountListKorr.get(i);
                trnsferTO = new TxTransferTO();
                trnsferTO.setAmount(CommonUtil.convertObjToDouble(result.get("AMOUNT")));
                trnsferTO.setInpAmount(CommonUtil.convertObjToDouble(result.get("AMOUNT")));
                trnsferTO.setAcHdId(CommonUtil.convertObjToStr(result.get("ACCT_HEAD")));
                trnsferTO.setTransType(CommonConstants.DEBIT);
                trnsferTO.setTransMode(CommonConstants.TX_TRANSFER);
                trnsferTO.setBranchId(TrueTransactMain.BRANCH_ID);
                trnsferTO.setInitiatedBranch(TrueTransactMain.BRANCH_ID);
              //  trnsferTO.setInstDt(getTdtInstrumentDate());
                trnsferTO.setTransDt(currDt);
                trnsferTO.setProdType("GL");
                trnsferTO.setTransModType(TransactionFactory.GL);
                //trnsferTO.setLinkBatchId(getCboAgentType());
                trnsferTO.setStatus(CommonConstants.STATUS_CREATED);
                trnsferTO.setStatusBy(ProxyParameters.USER_ID);
                trnsferTO.setStatusDt(currDt);
                trnsferTO.setParticulars(getCboAgentType() +" Agent Comm From "+CommonUtil.convertObjToStr(result1.get("REFERENCE_NO")));
                trnsferTO.setInstType("VOUCHER");
                trnsferTO.setAuthorizeStatus("DAILY");
                trnsferTO.setTransModType("GL");
                trnsferTO.setGlTransActNum(CommonUtil.convertObjToStr(getCboAgentType()));
                lst.add(trnsferTO);
            }
            System.out.println("lst************" + lst);
            System.out.println("lst************" + lst.size());
            return lst;
      
}
    
       public ArrayList setTransferTo() {
        HashMap DailyProdId = new HashMap();
        TxTransferTO trnsferTO = new TxTransferTO();
        if (CommonConstants.VENDOR != null && CommonConstants.VENDOR.equals("POLPULLY")) {
//            DailyProdId.put("PROD_ID", CommonUtil.convertObjToStr(getProdType()));
            DailyProdId.put("PROD_ID", CommonUtil.convertObjToStr(getProdId()));
        }else{
            DailyProdId.put("PROD_ID", CommonUtil.convertObjToStr(getProdId()));
        }        
        DailyProdId.put("AGENTID", getCboAgentType());
        List suspList = ClientUtil.executeQuery("getSelectAgentTO1", DailyProdId); //babu1
        System.out.println("suspList.suspList() == " + suspList);
        if (suspList != null && suspList.size() > 0) {
            AgentTO agSusTo = new AgentTO();
            agSusTo = (AgentTO) suspList.get(0);
            System.out.println("agSusTo.getCollSuspProdtype() == " + agSusTo.getCollSuspProdtype());
            if (agSusTo.getCollSuspProdtype() != null && agSusTo.getCollSuspProdtype().equals("GL")) {
                trnsferTO.setProdType(agSusTo.getCollSuspProdtype());
                trnsferTO.setAcHdId(agSusTo.getCollSuspACNum());

            } else if (agSusTo.getCollSuspProdtype() != null) {
                trnsferTO.setProdType(agSusTo.getCollSuspProdtype());
                trnsferTO.setActNum(agSusTo.getCollSuspACNum());
                trnsferTO.setProdId(agSusTo.getCollSuspProdID());
                trnsferTO.setLinkBatchId(agSusTo.getCollSuspProdID());
                HashMap prodMap = new HashMap();
                prodMap.put("PROD_ID", agSusTo.getCollSuspProdID());
                List prodList = ClientUtil.executeQuery("getAccountHeadProd" + agSusTo.getCollSuspProdtype(), prodMap);
                prodMap = null;
                System.out.println("prodList.size() --- " + prodList.size() + "prodList ==" + prodList);
                if (prodList != null && prodList.size() > 0) {
                    prodMap = (HashMap) prodList.get(0);
                    System.out.println("prodMap^^^^^^^^^^" + prodMap);
                    trnsferTO.setAcHdId(CommonUtil.convertObjToStr(prodMap.get("AC_HEAD")));

                } else {
                    ClientUtil.displayAlert("Debit Suspense Head is not Seted for This agent");
                    return null;
                }

            } else {
                ClientUtil.displayAlert("Debit Suspense Head is not Seted for This agent");
                return null;

            }

        } else {
            ClientUtil.displayAlert("Debit Suspense Head is not Seted for This agent");
            return null;
        }
        List prodLst = null;
        DepositsProductTO depositprod = null;
        LoanProductAccountTO loanprod = null;
        if (CommonConstants.VENDOR != null && CommonConstants.VENDOR.equals("POLPULLY")) {
            prodLst = ClientUtil.executeQuery("getSelectDepositsProductTO1", DailyProdId);
            depositprod = (DepositsProductTO) prodLst.get(0);
        } else if (CommonConstants.VENDOR != null && CommonConstants.VENDOR.equals("KOORKKENCHERRY")) {
            prodLst = ClientUtil.executeQuery("getSelectDepositsProductTO1", DailyProdId);
            depositprod = (DepositsProductTO) prodLst.get(0);
        } else if (CommonConstants.VENDOR != null && CommonConstants.VENDOR.equals("CHELLANOOR")) {
            prodLst = ClientUtil.executeQuery("getSelectDepositsProductTO1", DailyProdId);
            depositprod = (DepositsProductTO) prodLst.get(0);
        } else {
            DailyProdId.put("value", CommonUtil.convertObjToStr(getProdId()));
            prodLst = ClientUtil.executeQuery("getSelectLoanProductAccountTO", DailyProdId);
            loanprod = (LoanProductAccountTO) prodLst.get(0);
        }
        ArrayList lst = new ArrayList();
        //        TxTransferTO trnsferTO=new TxTransferTO();
        trnsferTO.setAmount(new Double(getTotalGlAmt()));
        trnsferTO.setInpAmount(new Double(getTotalGlAmt()));

        trnsferTO.setTransType(CommonConstants.DEBIT);
        trnsferTO.setTransMode(CommonConstants.TX_TRANSFER);
        trnsferTO.setBranchId(TrueTransactMain.BRANCH_ID);
        trnsferTO.setInitiatedBranch(TrueTransactMain.BRANCH_ID);
        trnsferTO.setInstDt(getTdtInstrumentDate());
        trnsferTO.setTransDt(ClientUtil.getCurrentDateWithTime());
        trnsferTO.setParticulars(getCboAgentType());
        //        trnsferTO.setProdId("GL");
        //        trnsferTO.setProdType("GL");
        //trnsferTO.setLinkBatchId(getCboAgentType());
        trnsferTO.setStatus(CommonConstants.STATUS_CREATED);
        trnsferTO.setStatusBy(ProxyParameters.USER_ID);
        trnsferTO.setStatusDt(currDt);
        trnsferTO.setInstType("VOUCHER");
        trnsferTO.setAuthorizeStatus("DAILY");       
        trnsferTO.setGlTransActNum(CommonUtil.convertObjToStr(getCboAgentType()));
        lst.add(trnsferTO);
        System.out.println("lst************" + lst);

        if (CommonConstants.VENDOR != null && CommonConstants.VENDOR.equals("POLPULLY")) {
            trnsferTO = new TxTransferTO();
            trnsferTO.setAmount(new Double(getTotalGlAmt()));
            trnsferTO.setInpAmount(new Double(getTotalGlAmt()));
            trnsferTO.setAcHdId(depositprod.getAcctHead());
            trnsferTO.setTransType(CommonConstants.CREDIT);
            trnsferTO.setTransMode(CommonConstants.TX_TRANSFER);
            trnsferTO.setBranchId(TrueTransactMain.BRANCH_ID);
            trnsferTO.setInitiatedBranch(TrueTransactMain.BRANCH_ID);
            trnsferTO.setInstDt(getTdtInstrumentDate());
            trnsferTO.setTransDt(currDt);
            trnsferTO.setProdType("GL");
            //trnsferTO.setLinkBatchId(getCboAgentType());
            trnsferTO.setStatus(CommonConstants.STATUS_CREATED);
            trnsferTO.setStatusBy(ProxyParameters.USER_ID);
            trnsferTO.setStatusDt(currDt);
            trnsferTO.setParticulars(getCboAgentType());
            trnsferTO.setInstType("VOUCHER");
            trnsferTO.setAuthorizeStatus("DAILY");
            trnsferTO.setGlTransActNum(CommonUtil.convertObjToStr(getCboAgentType()));
            lst.add(trnsferTO);
            System.out.println("lst************" + lst);
            System.out.println("lst************" + lst.size());
            return lst;
        } else if (CommonConstants.VENDOR != null && CommonConstants.VENDOR.equals("CHELLANOOR")) {
            HashMap where = new HashMap();
            HashMap result = new HashMap();
            where.put("AGENT_ID", getCboAgentType());
            where.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
            List CollAmountList = ClientUtil.executeQuery("getCollectionAmount", where);
            for (int i = 0; i < CollAmountList.size(); i++) {
                result = (HashMap) CollAmountList.get(i);

                trnsferTO = new TxTransferTO();
                trnsferTO.setAmount(CommonUtil.convertObjToDouble(result.get("AMOUNT")));
                trnsferTO.setInpAmount(CommonUtil.convertObjToDouble(result.get("AMOUNT")));
                //trnsferTO.setAcHdId(depositprod.getAcctHead());
                trnsferTO.setAcHdId(CommonUtil.convertObjToStr(result.get("ACCT_HEAD")));
                trnsferTO.setTransType(CommonConstants.CREDIT);
                trnsferTO.setTransMode(CommonConstants.TX_TRANSFER);
                trnsferTO.setBranchId(TrueTransactMain.BRANCH_ID);
                trnsferTO.setInitiatedBranch(TrueTransactMain.BRANCH_ID);
                trnsferTO.setInstDt(getTdtInstrumentDate());
                trnsferTO.setTransDt(currDt);
                //        trnsferTO.setProdId("GL");
                trnsferTO.setProdType("GL");
                //trnsferTO.setLinkBatchId(getCboAgentType());
                trnsferTO.setStatus(CommonConstants.STATUS_CREATED);
                trnsferTO.setStatusBy(ProxyParameters.USER_ID);
                trnsferTO.setStatusDt(currDt);
                trnsferTO.setParticulars(getCboAgentType());
                trnsferTO.setInstType("VOUCHER");
                trnsferTO.setAuthorizeStatus("DAILY");
                trnsferTO.setGlTransActNum(CommonUtil.convertObjToStr(getCboAgentType()));
                lst.add(trnsferTO);



            }

            return lst;
        } else //PERIGADOOR
        {
            //gg
            trnsferTO = new TxTransferTO();
            trnsferTO.setAmount(new Double(getTotalGlAmt()));
            trnsferTO.setInpAmount(new Double(getTotalGlAmt()));
            trnsferTO.setAcHdId(loanprod.getAcctHead());//loanprod.getA
            trnsferTO.setTransType(CommonConstants.CREDIT);
            trnsferTO.setTransMode(CommonConstants.TX_TRANSFER);
            trnsferTO.setBranchId(TrueTransactMain.BRANCH_ID);
            trnsferTO.setInitiatedBranch(TrueTransactMain.BRANCH_ID);
            trnsferTO.setInstDt(getTdtInstrumentDate());
            trnsferTO.setTransDt(currDt);
            //        trnsferTO.setProdId("GL");
            trnsferTO.setProdType("GL");
            //trnsferTO.setLinkBatchId(getCboAgentType());
            trnsferTO.setStatus(CommonConstants.STATUS_CREATED);
            trnsferTO.setStatusBy(ProxyParameters.USER_ID);
            trnsferTO.setStatusDt(currDt);
            trnsferTO.setParticulars(getCboAgentType());
            trnsferTO.setInstType("VOUCHER");
            trnsferTO.setAuthorizeStatus("DAILY");
            trnsferTO.setGlTransActNum(CommonUtil.convertObjToStr(getCboAgentType()));
            lst.add(trnsferTO);
            System.out.println("lst************" + lst);
            System.out.println("lst************" + lst.size());
            return lst;
        }
    }

    /**
     * Getter for property totalGlAmt.
     * @return Value of property totalGlAmt.
     */
    public double getTotalGlAmt() {
        return totalGlAmt;
    }

    /**
     * Setter for property totalGlAmt.
     * @param totalGlAmt New value of property totalGlAmt.
     */
    public void setTotalGlAmt(double totalGlAmt) {
        this.totalGlAmt = totalGlAmt;
    }

    public void resetMainPan() {
//        setCboAgentType("");
        setLblTransDate("");
        setLblInitiatorId("");
        setLblTransactionId("");
        //        setTotalGlAmt(new Double(null).doubleValue());
        ttNotifyObservers();
        //        setTotalGlAmt(CommonUtil.convertObjToDouble(new Double(null)).doubleValue());
    }
}
