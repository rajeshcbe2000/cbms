/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AuditEntryOB.java
 *
 * Created on February 25, 2004, 2:48 PM
 */

package com.see.truetransact.ui.transaction.auditEntry;

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
//import com.see.truetransact.ui.TrueTransactMain;
//import com.see.truetransact.ui.common.viewall.TableDialogUI;
import com.see.truetransact.transferobject.transaction.cash.CashTransactionTO;
import com.see.truetransact.transferobject.transaction.reconciliation.ReconciliationTO;
//import java.math.BigDecimal;
//import java.util.Observable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

//import com.see.truetransact.serverside.tds.tdscalc;
/**
 *
 * @author  rahul
 */
public class AuditEntryOB extends CObservable{
    
    private HashMap lookUpHash;
    private HashMap keyValue;
    private HashMap operationMap;
    
    private HashMap _authorizeMap;
    
    private ArrayList key;
    private ArrayList value;
    
    private ProxyFactory proxy = null;
    private HashMap procChargeHash = new HashMap();
    private ComboBoxModel cbmProdType;
    private ComboBoxModel cbmProdId;
    private ComboBoxModel cbmInputCurrency;
    private ComboBoxModel cbmInstrumentType;
    
    //tdscalc
    private HashMap tdsCalc =new HashMap();
    private int actionType;
    private int result;
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    private final static Logger log = Logger.getLogger(AuditEntryUI.class);
    
    final String DEBIT = "DEBIT";
    final String CREDIT = "CREDIT";
    final String INITIATORTYPE = "CASHIER";
    
    private String lblAccHdDesc;
    private String txtAccHdId;
    private String lblAccName;
    private String txtScreenName = "";
    private String lblTransactionId;
    private String lblTransDate;
    private String lblInitiatorId;
    private String depTransId = null;
    private double interestAmt = 0.0;
    private String depInterestAmt = "";
    
    private static CInternalFrame frame;
    private  double LimitAmt;
    private double oldAmount = 0.0;
    private String  LimitAmount;
    private String prodType;
    private String cr_cash;
    private String dr_cash;
    private ArrayList denominationList;
    private boolean isExist;
    private int result1=1;
    private boolean odExpired;
    private boolean flag=false;
    private String depLinkBatchId;
    private HashMap oaMap=new HashMap();
    private HashMap linkMap=new HashMap();
    private String asAnWhenCustomer =new String();
    private  double procAmount;
    private HashMap ALL_LOAN_AMOUNT;
    private List linkBathList=null;
    private static AuditEntryOB cashTransactionOB;
    private Date curDate = null;
    public boolean depositRenewalFlag = false;
    public String depositPenalAmt = null;
    public String depositPenalMonth = null;
    private String creatingFlexi = "";
    private double flexiAmount = 0.0;
    private String reconcile = "";
    private String balanceType = "";
    public HashMap reconcileMap;
    ReconciliationTO reconciliationTO = new ReconciliationTO();
    private String  AuthorizeBy;
    private String lblHouseName = "";
    private boolean penalWaiveOff=false;
    private boolean rebateInterest=false;
    private String closedAccNo="";
    private boolean hoAccount=false;
    private Map corpLoanMap = null; // For Corporate Loan purpose added by Rajesh
    ArrayList orgRespList=null;
    static {
        try {
            log.info("In CashTransactionOB Declaration");
            cashTransactionOB = new AuditEntryOB();
        } catch(Exception e) {
            log.info("Error in CashTransactionOB Declaration");
        }
    }
    
    public static AuditEntryOB getInstance(CInternalFrame frm) {
        frame = frm;
        return cashTransactionOB;
    }
    
    /** Creates a new instance of CashTransactionOB */
    public AuditEntryOB() throws Exception {
        curDate = ClientUtil.getCurrentDate();
        initianSetup();
    }
    
    public Date getCurrentDate() {
        return (Date)curDate.clone();
    }
    private void initianSetup() throws Exception{
        log.info("In initianSetup()");
        setOperationMap();
        try {
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            log.info(" Error In initianSetup()");
            e.printStackTrace();
        }
        fillDropdown();// To Fill all the Combo Boxes
    }
    
    // Set the value of JNDI and the Session Bean...
    private void setOperationMap() throws Exception{
        log.info("In setOperationMap()");
        
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "AuditEntryJNDI");
        operationMap.put(CommonConstants.HOME, "transaction.auditEntry.AuditEntryHome");
        operationMap.put(CommonConstants.REMOTE, "transaction.auditEntry.AuditEntry");
    }
    
    
    // To Fill the Combo boxes in the UI
    public void fillDropdown() throws Exception{
        log.info("In fillDropdown()");
        
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookup_keys = new ArrayList();
        
        lookup_keys.add("FOREX.CURRENCY");
        lookup_keys.add("INSTRUMENTTYPE");
        lookup_keys.add("PRODUCTTYPE");
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("FOREX.CURRENCY"));
        cbmInputCurrency = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("INSTRUMENTTYPE"));
        int idx=key.indexOf("ONLINE_TRANSFER");
        key.remove(idx);
        value.remove(idx);
        
        cbmInstrumentType = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("PRODUCTTYPE"));
        cbmProdType = new ComboBoxModel(key,value);
    }
    
    // Get the value from the Hash Map depending on the Value of Key...
    private void getKeyValue(HashMap keyValue)  throws Exception{
        log.info("In getKeyValue()");
        
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
        
    // To enter the Data into the Database...Called from doActionPerform()...
    public CashTransactionTO setCashTransaction() {
        log.info("In setCashTransaction()");
        
        final CashTransactionTO objCashTransactionTO = new CashTransactionTO();
        try{
            objCashTransactionTO.setTransId(CommonUtil.convertObjToStr(getLblTransactionId()));
            objCashTransactionTO.setAcHdId(CommonUtil.convertObjToStr(getTxtAccHd()));
            objCashTransactionTO.setScreenName(CommonUtil.convertObjToStr(getTxtScreenName()));
            
            if (!CommonUtil.convertObjToStr(getCboProdId()).equals(""))
                objCashTransactionTO.setProdId(CommonUtil.convertObjToStr(getCbmProdId().getKeyForSelected()));
            
            objCashTransactionTO.setProdType(CommonUtil.convertObjToStr(getCbmProdType().getKeyForSelected()));
            objCashTransactionTO.setActNum(CommonUtil.convertObjToStr(getTxtAccNo()));
            objCashTransactionTO.setInpAmount(CommonUtil.convertObjToDouble(getTxtInputAmt()));
            
            if (getCboInputCurrency() != null)
                objCashTransactionTO.setInpCurr(CommonUtil.convertObjToStr(getCbmInputCurrency().getKeyForSelected()));
            
            objCashTransactionTO.setAmount(CommonUtil.convertObjToDouble(getTxtAmount()));
            if (getRdoTransactionType_Debit() == true) {
                objCashTransactionTO.setTransType(DEBIT);
            } else if (getRdoTransactionType_Credit() == true) {
                objCashTransactionTO.setTransType(CREDIT);
            }
            
            if (getCboInstrumentType() != null) {
                getCbmInstrumentType().setSelectedItem(getCboInstrumentType());
                objCashTransactionTO.setInstType(CommonUtil.convertObjToStr(getCbmInstrumentType().getKeyForSelected()));
            }
            
            objCashTransactionTO.setBranchId(getSelectedBranchID());
            objCashTransactionTO.setStatusBy(ProxyParameters.USER_ID);
            
            objCashTransactionTO.setInstrumentNo1(CommonUtil.convertObjToStr(getTxtInstrumentNo1()));
            objCashTransactionTO.setInstrumentNo2(CommonUtil.convertObjToStr(getTxtInstrumentNo2()));
            
            Date InsDt = DateUtil.getDateMMDDYYYY(getTdtInstrumentDate());
            if(InsDt != null){
                Date insDate = (Date) curDate.clone();
                insDate.setDate(InsDt.getDate());
                insDate.setMonth(InsDt.getMonth());
                insDate.setYear(InsDt.getYear());
                //            objCashTransactionTO.setInstDt(DateUtil.getDateMMDDYYYY(getTdtInstrumentDate()));
                objCashTransactionTO.setInstDt(insDate);
            }else{
                objCashTransactionTO.setInstDt(DateUtil.getDateMMDDYYYY(getTdtInstrumentDate()));
            }
            
            //            Double tokenNo = getTokenDouble(getTxtTokenNo());
            objCashTransactionTO.setTokenNo(getTxtTokenNo());
            objCashTransactionTO.setInitTransId(ProxyParameters.USER_ID);
            objCashTransactionTO.setInitChannType(CommonUtil.convertObjToStr(getTxtInitiatorChannel()));
            objCashTransactionTO.setParticulars(CommonUtil.convertObjToStr(getTxtParticulars()));
            objCashTransactionTO.setNarration(CommonUtil.convertObjToStr(getTxtNarration()));
            objCashTransactionTO.setInitiatedBranch(ProxyParameters.BRANCH_ID);
            if(objCashTransactionTO.getProdType().equals("GL") || objCashTransactionTO.getProdType().equals("TL") || objCashTransactionTO.getProdType().equals("ATL")|| objCashTransactionTO.getProdType().equals("AD") || objCashTransactionTO.getProdType().equals("AAD") )
                objCashTransactionTO.setLinkBatchId(objCashTransactionTO.getActNum());
             if(CommonUtil.convertObjToStr(getCbmProdType().getKeyForSelected()).equals("GL"))
                objCashTransactionTO.setLinkBatchId(getClosedAccNo()); 
            objCashTransactionTO.setPanNo(CommonUtil.convertObjToStr(getTxtPanNo()));
            
            System.out.println("objCashTransactionTO:" + objCashTransactionTO);
        }catch(Exception e){
            log.info("Error In setInwardClearing()");
            e.printStackTrace();
        }
        return objCashTransactionTO;
    }
    
    private Double getTokenDouble(String tokenNo) {
        String s="";
        for (int i = 0; i<tokenNo.length(); i++) {
            char c = tokenNo.charAt(i);
            System.out.println("### c = "+c);
            if ((c>=65) && (c<=90)) continue;
            s+=String.valueOf(c);
            System.out.println("#### seperated  : "+s);
        }
        System.out.println("#### seperated  : "+s);
        return CommonUtil.convertObjToDouble(s);
    }
    
    //Do display the Data from the Database, in UI
    public void populateData(HashMap whereMap) {
        log.info("In populateData()");
        
        final HashMap mapData;
        try {
            mapData = proxy.executeQuery(whereMap, operationMap); //, frame);
            if(whereMap.containsKey("SELECTED_ROW"))
                mapData.put("SELECTED_ROW",whereMap.get("SELECTED_ROW"));
            System.out.println("#### MapData :"+mapData);
            populateOB(mapData);
            CashTransactionTO objCashTransactionTO = null;
            objCashTransactionTO = (CashTransactionTO)((List)mapData.get("CashTransactionTO")).get(0);
            if(objCashTransactionTO.getProdType().equals("TD")||objCashTransactionTO.getProdType().equals("TL")){
                whereMap.put("ACCOUNT NO",objCashTransactionTO.getActNum());
            }
        } catch( Exception e ) {
            log.info("Error In populateData()");
            e.printStackTrace();
        }
    }
    public HashMap loanInterestCalculationAsAndWhen(HashMap whereMap){
        HashMap mapData=new HashMap();
        try{//dont delete this methode check select dao
            List mapDataList = ClientUtil.executeQuery("", whereMap); //, frame);
            mapData=(HashMap)mapDataList.get(0);
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("#### MapData :"+mapData);
        return mapData;
    }
    private void populateOB(HashMap mapData) throws Exception{
        log.info("In populateOB()");
        System.out.println("########populate OB : "+mapData);
        CashTransactionTO objCashTransactionTO = null;
        //Taking the Value of Transaction Id from each Table...
        // Here the first Row is selected...
        if(mapData.containsKey("SELECTED_ROW"))
            objCashTransactionTO = (CashTransactionTO) ((List) mapData.get("CashTransactionTO")).get(CommonUtil.convertObjToInt(mapData.get("SELECTED_ROW")));
        else
            objCashTransactionTO = (CashTransactionTO) ((List) mapData.get("CashTransactionTO")).get(0);
        HashMap map=new HashMap();
        if(objCashTransactionTO.getLinkBatchId()!=null && objCashTransactionTO.getLinkBatchId().length()>0){
            map.put("ACT_NUM",objCashTransactionTO.getLinkBatchId());
            //            List lst=ClientUtil.executeQuery("IntCalculationDetail", map);
            //            if(lst !=null && lst.size()>0){
            //             map=(HashMap)lst.get(0);
            objCashTransactionTO.setActNum(objCashTransactionTO.getLinkBatchId());
            map=asAnWhenCustomerComesYesNO(objCashTransactionTO.getLinkBatchId());
            if(map !=null && map.containsKey("AS_CUSTOMER_COMES") && map.get("AS_CUSTOMER_COMES").equals("Y")){
                map.put("ACCT_NUM",objCashTransactionTO.getLinkBatchId());
                map.put("PROD_TYPE",objCashTransactionTO.getProdType());
                map.put("PROD_ID",objCashTransactionTO.getProdId());
                map.put("TRANS_ID",objCashTransactionTO.getTransId());
                map.put("TRANS_DT",objCashTransactionTO.getTransDt());
                map.put("NO_OF_INSTALLMENT",objCashTransactionTO.getAuthorizeRemarks());
                map.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
                setLinkMap(map);
                setLinkBathList(ClientUtil.executeQuery("getCashTransactionTOForAuthorzationLinkBatch", map));
                //            }
            }
        }
        
//        objCashTransactionTO = (CashTransactionTO) ((List) mapData.get("CashTransactionTO")).get(0);
        if(objCashTransactionTO.getLinkBatchId()!=null && objCashTransactionTO.getLinkBatchId().length()>0){
            objCashTransactionTO.setActNum(objCashTransactionTO.getLinkBatchId());
            String actNum = objCashTransactionTO.getActNum();
            if (actNum.lastIndexOf("_")!=-1)
                actNum = actNum.substring(0,actNum.lastIndexOf("_"));
            HashMap getActNumMap = new HashMap();
            getActNumMap.put("ACT_NUM",actNum);
            List lst = ClientUtil.executeQuery("getprodIdForEditDep", getActNumMap);
            System.out.println("##########lst :"+lst);
            if(lst.size()>0){
                getActNumMap = (HashMap)lst.get(0);
                objCashTransactionTO.setProdId(CommonUtil.convertObjToStr(getActNumMap.get("PROD_ID")));
                objCashTransactionTO.setProdType("TD");
                objCashTransactionTO.setAcHdId("");
            }
            System.out.println("##########objCashTransactionTO :"+objCashTransactionTO);
        }
        setCashTransactionTO(objCashTransactionTO);
        setDenominationList((ArrayList) mapData.get("DENOMINATION_LIST"));
        ttNotifyObservers();
    }
    public HashMap asAnWhenCustomerComesYesNO(String acct_no){
        HashMap map=new HashMap();
        map.put("ACT_NUM",acct_no);
        map.put("TRANS_DT", curDate.clone());
        map.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
        List lst=null;
        if(!CommonUtil.convertObjToStr(getCbmProdType().getKeyForSelected()).equals("AD"))
          lst=ClientUtil.executeQuery("IntCalculationDetail", map);
        else
            lst=ClientUtil.executeQuery("IntCalculationDetailAD", map);
       
        if(lst !=null && lst.size()>0){
            map=(HashMap)lst.get(0);
            setLinkMap(map);
        }
        return map;
    }
    // To Enter the values in the UI fields, from the database...
    private void setCashTransactionTO(CashTransactionTO objCashTransactionTO) throws Exception{
        log.info("In setCashTransactionTO()");
        // Set as a Lable
        setStatusBy(objCashTransactionTO.getStatusBy());
        setLblTransactionId(CommonUtil.convertObjToStr(objCashTransactionTO.getTransId()));

        getCbmProdType().setKeyForSelected(CommonUtil.convertObjToStr(objCashTransactionTO.getProdType())); // This line added by Rajesh
        setCboProdType((String) getCbmProdType().getDataForKey(CommonUtil.convertObjToStr(objCashTransactionTO.getProdType())));
        
        if (!CommonUtil.convertObjToStr(objCashTransactionTO.getProdId()).equals("")) {
            setCbmProdId(objCashTransactionTO.getProdType());
            getCbmProdId().setKeyForSelected(CommonUtil.convertObjToStr(objCashTransactionTO.getProdId())); // This line added by Rajesh
            setCboProdId((String) getCbmProdId().getDataForKey(CommonUtil.convertObjToStr(objCashTransactionTO.getProdId())));
        }
        setTxtAccHd(CommonUtil.convertObjToStr(objCashTransactionTO.getAcHdId()));
        setTxtAccNo(CommonUtil.convertObjToStr(objCashTransactionTO.getActNum()));
        setTxtInputAmt(CommonUtil.convertObjToStr(objCashTransactionTO.getInpAmount()));
        getCbmInputCurrency().setKeyForSelected(CommonUtil.convertObjToStr(objCashTransactionTO.getInpCurr())); // This line added by Rajesh
        setCboInputCurrency((String) getCbmInputCurrency().getDataForKey(CommonUtil.convertObjToStr(objCashTransactionTO.getInpCurr())));
        
        oldAmount = objCashTransactionTO.getAmount().doubleValue();
        setTxtAmount(CommonUtil.convertObjToStr(new Double(oldAmount)));
        //Set as a Lable...
        setLblTransDate(DateUtil.getStringDate(objCashTransactionTO.getTransDt()));//(DateUtil.getStringDate (objCashTransactionTO.getTransDt ()));
        
        if (CommonUtil.convertObjToStr(objCashTransactionTO.getTransType()).equals(DEBIT)) setRdoTransactionType_Debit(true);
        else setRdoTransactionType_Credit(true);
        
        getCbmInstrumentType().setKeyForSelected(CommonUtil.convertObjToStr(objCashTransactionTO.getInstType())); // This line added by Rajesh
        setCboInstrumentType((String) getCbmInstrumentType().getDataForKey(CommonUtil.convertObjToStr(objCashTransactionTO.getInstType())));
        setTxtInstrumentNo1(CommonUtil.convertObjToStr(objCashTransactionTO.getInstrumentNo1()));
        setTxtInstrumentNo2(CommonUtil.convertObjToStr(objCashTransactionTO.getInstrumentNo2()));
        setTdtInstrumentDate(DateUtil.getStringDate(objCashTransactionTO.getInstDt()));
        //        HashMap tokenIssue = new HashMap();
        //         String token= "";
        //         tokenIssue.put("CURRENT_DT", curDate.clone());
        //         tokenIssue.put("BRANCH_ID",ProxyParameters.BRANCH_ID);
        //         tokenIssue.put("RECEIVED_BY", TrueTransactMain.USER_ID);
        //         Integer tokNo = new Integer(CommonUtil.convertObjToInt(objCashTransactionTO.getTokenNo()));
        //         tokenIssue.put("TOKEN_NO", tokNo);
        //        List lst = ClientUtil.executeQuery("getSeriesNo", tokenIssue);
        //         if (lst.size()>0) {
        //                    tokenIssue =(HashMap)lst.get(0);
        //                    token=CommonUtil.convertObjToStr(tokenIssue.get("SERIES_NO"));
        //
        //
        //         }
        //         setTxtTokenNo (token+CommonUtil.convertObjToStr(objCashTransactionTO.getTokenNo()));
        
        setTxtTokenNo(CommonUtil.convertObjToStr(objCashTransactionTO.getTokenNo()));
        
        //Set as a Lable...
        setLblInitiatorId(CommonUtil.convertObjToStr(objCashTransactionTO.getInitTransId()));
        setTxtInitiatorChannel(CommonUtil.convertObjToStr(objCashTransactionTO.getInitChannType()));
        setTxtParticulars(CommonUtil.convertObjToStr(objCashTransactionTO.getParticulars()));
        setTxtNarration(CommonUtil.convertObjToStr(objCashTransactionTO.getNarration()));
        setTxtPanNo(CommonUtil.convertObjToStr(objCashTransactionTO.getPanNo()));
        
        this.setProdType(objCashTransactionTO.getProdType());
        setAuthorizeBy(CommonUtil.convertObjToStr(objCashTransactionTO.getAuthorizeBy()));
        
    }
    
    public boolean calcRecurringDates(String prodId) {
        HashMap recurrMap = new HashMap();
        recurrMap.put("PROD_ID",prodId);
        List lst = ClientUtil.executeQuery("getBehavesLikeForDeposit", recurrMap);
        if(lst.size()>0){
            HashMap recurringMap = (HashMap)lst.get(0);
            if(recurringMap.get("BEHAVES_LIKE").equals("RECURRING")){
                String depositNo = getTxtAccNo();
                if (depositNo.lastIndexOf("_")!=-1)
                    depositNo = depositNo.substring(0, depositNo.lastIndexOf("_"));
                recurrMap.put("DEPOSIT_NO",depositNo);
                lst = ClientUtil.executeQuery("getDepositAmountForRecurring", recurrMap);
                if(lst.size()>0){
                    recurringMap = (HashMap)lst.get(0);
                    double depAmt = CommonUtil.convertObjToDouble(recurringMap.get("DEPOSIT_AMT")).doubleValue();
                    double totInstall = CommonUtil.convertObjToDouble(recurringMap.get("TOTAL_INSTALLMENTS")).doubleValue();
                    double totPaid = CommonUtil.convertObjToDouble(recurringMap.get("TOTAL_INSTALL_PAID")).doubleValue();
                    double totAmt = CommonUtil.convertObjToDouble(recurringMap.get("TOTAL_BALANCE")).doubleValue();
                    double balAmt = depAmt * totInstall;
                    double payAmt = balAmt - totAmt;
                    double txtAmt = CommonUtil.convertObjToDouble(getTxtAmount()).doubleValue();
                    Date insDate = (Date) curDate.clone();
                    System.out.println("#######Current Date : "+insDate);
                    System.out.println("#######Current : "+recurringMap.get("MATURITY_DT"));
                    System.out.println("#######totAmt : "+totAmt);
                    System.out.println("#######balAmt : "+balAmt);
                    System.out.println("#######payAmt : "+payAmt);
                    double penalAmt = CommonUtil.convertObjToDouble(getDepositPenalAmt()).doubleValue();
                    if(penalAmt>0)
                       payAmt = payAmt + penalAmt; 
                    if(payAmt>=txtAmt){
                        //Commented By Suresh
//                        if(DateUtil.dateDiff((Date)recurringMap.get("MATURITY_DT"),insDate)<0){
//                            Date matDt = null;
//                            if(recurringMap.get("LAST_TRANS_DT")==null){
//                                //                            lastDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(recurringMap.get("DEPOSIT_DT")));
//                            }else {
//                                
//                                matDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(recurringMap.get("MATURITY_DT")));
//                                //                                Date currDt = insDate;
//                                long maturityDt = DateUtil.dateDiff(insDate,matDt);
//                                System.out.println("#######maturityDt : "+maturityDt);
//                                long installment = CommonUtil.convertObjToInt(recurringMap.get("TOTAL_INSTALLMENTS"));
//                                if(installment == 1){
//                                    if(maturityDt<=30){
//                                        Date addMatDate = DateUtil.addDays(insDate,30);
//                                        System.out.println("####*****Maturity Date : "+addMatDate);
//                                        HashMap updateMap = new HashMap();
//                                        updateMap.put("MATURITY_DT",addMatDate);
//                                        updateMap.put("DEPOSIT_NO",depositNo);
//                                        ClientUtil.execute("updateMaturityDate", updateMap);
//                                    }
//                                }else{
//                                }
//                            }
//                            //                    return true;
//                        }else{
//                            ClientUtil.showAlertWindow("This Deposit has Already Matured....");
//                            return false;
//                        }
                    }else{
                        ClientUtil.showAlertWindow("Exceeding the total Installments Amount...\n"+
                        "Balance Amount is "+payAmt);
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    // To perform Appropriate operation... Insert, Update, Delete...
    public void doAction() {
        TTException exception = null;
        log.info("In doAction()");
        
        try {
            
            // The following block added by Rajesh to avoid Save operation after Authorization.
            // If one person opened a transaction for Edit and another person opened the same 
            // transaction for Authorization, the system is allowing to save after Authorization also.  
            // So, after authorization again the GL gets updated and a/c level shadow credit/debit goes negative.
            // In this case the should not allow to save or some error message should display.  
            if ((!getLblTransactionId().equals("")) && getActionType()!=ClientConstants.ACTIONTYPE_AUTHORIZE && getActionType()!=ClientConstants.ACTIONTYPE_REJECT) {
                HashMap whereMap = new HashMap();
                whereMap.put("TRANS_ID", getLblTransactionId());
                //screen lock
                
            whereMap.put("USER_ID",ProxyParameters.USER_ID);
            whereMap.put("TRANS_DT", curDate.clone());
            whereMap.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
            List lstlock=ClientUtil.executeQuery("selectauthorizationLock", whereMap);
            if(lstlock !=null && lstlock.size()>0)
            {
                HashMap map=new HashMap();
                StringBuffer open=new StringBuffer();
                for(int i=0;i<lstlock.size();i++){
                    map=(HashMap)lstlock.get(i);
                    open.append ("\n"+"User Id  :"+" ");
                    open.append(CommonUtil.convertObjToStr(map.get("OPEN_BY"))+"\n");
                    open.append("Mode Of Operation  :" +" ");
                    open.append(CommonUtil.convertObjToStr(map.get("MODE_OF_OPERATION"))+" ");            
                }
                ClientUtil.showMessageWindow("already opened by"+open);
           
                return;
            }
                //
                whereMap.put("TRANS_DT", curDate.clone());
                whereMap.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
                List lst = ClientUtil.executeQuery("getCashAuthorizeStatus", whereMap);
                if (lst!=null && lst.size()>0) {
                    whereMap = (HashMap) lst.get(0);
                    String authStatus = CommonUtil.convertObjToStr(whereMap.get("AUTHORIZE_STATUS"));
                    String authBy = CommonUtil.convertObjToStr(whereMap.get("AUTHORIZE_BY"));
                    if (!authStatus.equals("")) {
                        actionType = ClientConstants.ACTIONTYPE_FAILED;
                        throw new TTException("This transaction already "+authStatus.toLowerCase()+" by "+authBy);
                    }
                }
            }
            // End
        
            //If actionType such as NEW, EDIT, DELETE, then proceed
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                //If actionType has got propervalue then doActionPerform, else throw error
                if( getCommand() != null || getAuthorizeMap() != null){
                    doActionPerform("");
                }
            } else {
                log.info("Action Type Not Defined In setCashTransactionTO()");
            }
        } catch (Exception e) {
            log.info("Error In doAction()");
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            e.printStackTrace();
            if(e instanceof TTException) {
                exception = (TTException) e;
            }
        }
        
        // If TT Exception
        if (exception != null) {
            HashMap exceptionHashMap = exception.getExceptionHashMap();
            System.out.println(exception+"exceptionmap###"+exceptionHashMap);
            if (exceptionHashMap != null) {
                ArrayList list = (ArrayList) exceptionHashMap.get(CommonConstants.EXCEPTION_LIST);
                //(list.get(0) instanceof String && "IB".equalsIgnoreCase((String)list.get(0))) ||
                if(list.size()==1 && list.get(0) instanceof String && ((String)list.get(0)).startsWith("SUSPECIOUS")||CommonUtil.convertObjToStr(list.get(0)).equals("AED")||CommonUtil.convertObjToStr(list.get(0)).equals("ESL")||
                CommonUtil.convertObjToStr(list.get(0)).equals("AEL")) {
                    Object[] dialogOption = {"Exception","Cancel"};
                    TTException e=new  TTException(CommonUtil.convertObjToStr(list.get(0)));
                    
                    parseException.setDialogOptions(dialogOption);
                    if(parseException.logException(e, true)==0) {//exception
                        try{
                            setResult(actionType);
                            doActionPerform("EXCEPTION");
                        } catch(Exception e1) {
                            log.info("Error In doAction()");
                            e1.printStackTrace();
                            if(e1 instanceof TTException) {
                                Object[] dialogOption1 = {"OK"};
                                parseException.setDialogOptions(dialogOption1);
                                exception = (TTException) e1;
                                parseException.logException(exception, true);
                            }
                        }
                    }
                    Object[] dialogOption1 = {"OK"};
                    parseException.setDialogOptions(dialogOption1);
                } 
                else  if(CommonUtil.convertObjToStr(list.get(0)).equals("MIN")){
                    Object[] dialogOption = {"Continue","Cancel"};
                    parseException.setDialogOptions(dialogOption);
                    if(parseException.logException(exception, true)==0) {
                        try{
                            setResult(actionType);
                            doActionPerform("EXCEPTION");
                        } catch(Exception e1) {
                            log.info("Error In doAction()");
                            e1.printStackTrace();
                            if(e1 instanceof TTException) {
                                Object[] dialogOption1 = {"OK"};
                                parseException.setDialogOptions(dialogOption1);
                                exception = (TTException) e1;
                                parseException.logException(exception, true);
                            }
                        }
                    }
                    Object[] dialogOption1 = {"OK"};
                    parseException.setDialogOptions(dialogOption1);
                }
                else {
                    parseException.logException(exception, true);
                }
            } else { // To Display Transaction No showing String message
                parseException.logException(exception, true);
                setResult(actionType);
            }
        }
    }
    
    private HashMap interestAmtgiving(){
        String actNum = getTxtAccNo();
        HashMap interestMap = new HashMap();
        interestMap.put("ACT_NUM",getTxtAccNo());
        List lst = ClientUtil.executeQuery("getIntForDeptIntTable", interestMap);
        if(lst.size()>0 && lst != null){
            interestMap = (HashMap)lst.get(0);
            String paidInt = CommonUtil.convertObjToStr(interestMap.get("PAID_INT"));
            Date applDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(interestMap.get("APPL_DT")));
        }
        return interestMap;
    }
    
    /** To perform the necessary action */
    private void doActionPerform(String parameter) throws Exception{
        log.info("In doActionPerform()");
        final HashMap data = new HashMap();
        String tranIdList="TRANS IDs\n";
        HashMap proxyReturnMap = new HashMap();
        data.put("ACT_CLOSING_MIN_BAL_CHECK_CASH_TRANSFER","ACT_CLOSING_MIN_BAL_CHECK_CASH_TRANSFER");
        if(getAuthorizeMap()!=null){
            setAsAnWhenCustomer("N");
        }
        //            ArrayList arrayList=setTransactionDetailTLAD();
        
        //        for(int j=0;j<arrayList.size();j++){
        //            CashTransactionTO objCashTransactionTO=(CashTransactionTO)arrayList.get(j);
        final CashTransactionTO objCashTransactionTO = setCashTransaction();
        objCashTransactionTO.setCommand(getCommand());
        if(!getReconcile().equals("") && getReconcile().equals("Y") && getCommand()!=null && getCommand().equals("INSERT")){
            ReconciliationTO reconciliationTO = new ReconciliationTO();
            reconciliationTO.setAcHdId(objCashTransactionTO.getAcHdId());
            reconciliationTO.setTransAmount(objCashTransactionTO.getAmount());
            reconciliationTO.setBalanceAmount(objCashTransactionTO.getAmount());
            reconciliationTO.setTransType(objCashTransactionTO.getTransType());
            reconciliationTO.setTransMode("CASH");
            reconciliationTO.setStatus(objCashTransactionTO.getStatus());
            reconciliationTO.setStatusBy(objCashTransactionTO.getStatusBy());
            reconciliationTO.setStatusDt(objCashTransactionTO.getStatusDt());
            reconciliationTO.setBranchId(objCashTransactionTO.getBranchId());
            reconciliationTO.setInitiatedBranch(objCashTransactionTO.getInitiatedBranch());
            data.put("ReconciliationTO",reconciliationTO);
        }
        if(!getReconcile().equals("") && getReconcile().equals("N"))
            data.put("LIST_OF_REDUCED",reconcileMap);
            
        if(isHoAccount()==true){
             if(getRdoTransactionType_Debit()){
                 data.put("RESPONDING","RESPONDING");
             }
             data.put("ORG_RESP_DETAILS",getOrgRespList());
        }
        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SCREEN, getScreen());
        data.put("PRODUCTTYPE",(String)cbmProdType.getKeyForSelected());
//        if(data.get("PRODUCTTYPE").equals("OA"))
//        {
//         String ACT_NUM= getTxtAccNo();
//         HashMap where = new HashMap();
//         where.put("ACT_NUM",ACT_NUM);
//         List list = ClientUtil.executeQuery("getTODDetails", where);
//         where=null;
//         where=(HashMap)list.get(0);
//         data.put("TYPE_OF_TOD",where.get("TYPE_OF_TOD"));
//         data.put("TOD_AMOUNT",where.get("TOD_AMOUNT"));
//        }
        if(ALL_LOAN_AMOUNT !=null && getRdoTransactionType_Credit() && (data.get("PRODUCTTYPE").equals("TL") || data.get("PRODUCTTYPE").equals("ATL") || data.get("PRODUCTTYPE").equals("AD"))){
            
            if(isPenalWaiveOff()){
                ALL_LOAN_AMOUNT.put("PENAL_WAIVE_OFF","Y");
            }else{
                ALL_LOAN_AMOUNT.put("PENAL_WAIVE_OFF","N");
            }
            if(isRebateInterest()){
                data.put("REBATE_INTEREST","Y");
            }else{
                data.put("REBATE_INTEREST","N");
            }
            data.put("ALL_AMOUNT",ALL_LOAN_AMOUNT);
        }
        if(getRdoTransactionType_Debit() && (data.get("PRODUCTTYPE").equals("ATL") || data.get("PRODUCTTYPE").equals("TL") || data.get("PRODUCTTYPE").equals("AD") || data.get("PRODUCTTYPE").equals("AAD")))
            data.put("DEBIT_LOAN_TYPE","DP");
        if(procChargeHash.size()>0 && result1==0)
            data.put("PROCCHARGEMAP",procChargeHash);
        if(parameter!=null && "EXCEPTION".equalsIgnoreCase(parameter))
            data.put("EXCEPTION","EXCEPTION");
        
        if(getRdoTransactionType_Credit() && data.get("PRODUCTTYPE").equals("TD")){
            HashMap prodMap = new HashMap();
            prodMap.put("PROD_ID",objCashTransactionTO.getProdId());
            List lst = ClientUtil.executeQuery("getBehavesLikeForDeposit", prodMap);
            if(lst!=null && lst.size()>0)
                prodMap = (HashMap)lst.get(0);
            if(prodMap.get("BEHAVES_LIKE").equals("RECURRING")){
                double penalAmt = CommonUtil.convertObjToDouble(getDepositPenalAmt()).doubleValue();
                double penalMonth = CommonUtil.convertObjToDouble(getDepositPenalMonth()).doubleValue();
                if(penalAmt>0){
                    data.put("DEPOSIT_PENAL_AMT",getDepositPenalAmt());  
                    data.put("DEPOSIT_PENAL_MONTH",getDepositPenalMonth());  
                }
            }            
        }
        
        //        if(!getDepLinkBatchId().equals("") && getDepLinkBatchId().equals("DEP_LINK")){
        if(getRdoTransactionType_Debit() && data.get("PRODUCTTYPE").equals("TD") &&
        !getDepInterestAmt().equals("") && getDepInterestAmt().equals("DEP_INTEREST_AMT")){
            data.put("DEPOSIT_INTEREST","DEPOSIT_INTEREST");
        }
        //        }
        if(getRdoTransactionType_Debit() && data.get("PRODUCTTYPE").equals("TD") &&
        !getDepInterestAmt().equals("") && !getDepInterestAmt().equals("DEP_INTEREST_AMT")){
            String act_Num = CommonUtil.convertObjToStr(getTxtAccNo());
            HashMap fdPaymentMap = new HashMap();
            if(act_Num.lastIndexOf("_")!=-1)
                act_Num = act_Num.substring(0,act_Num.lastIndexOf("_"));
            fdPaymentMap.put("DEPOSIT_NO",act_Num);
            List lstFDpay = ClientUtil.executeQuery("getInterestDeptIntTable", fdPaymentMap);
            if(lstFDpay!=null && lstFDpay.size()>0){
                fdPaymentMap = (HashMap)lstFDpay.get(0);
                String prodId = CommonUtil.convertObjToStr(objCashTransactionTO.getProdId());
                String fdPay = CommonUtil.convertObjToStr(fdPaymentMap.get("FD_CASH_PAYMENT"));
                HashMap behavesMap =new HashMap();
                behavesMap.put("PROD_ID",prodId);
                List lstBehave = ClientUtil.executeQuery("getBehavesLikeForDeposit", behavesMap);
                if(lstBehave!=null && lstBehave.size()>0){
                    behavesMap = (HashMap)lstBehave.get(0);
                    if(behavesMap.get("BEHAVES_LIKE").equals("FIXED") && fdPay!=null && fdPay.equals("Y") && getActionType()==ClientConstants.ACTIONTYPE_NEW){
                        ClientUtil.showAlertWindow("Transaction Has been already done Pending for Authorization...");
                        return;
                    }else{
                        HashMap interestMap = new HashMap();
                        HashMap prodMap = new HashMap();
                        prodMap.put("PROD_ID",objCashTransactionTO.getProdId());
                        List lst = ClientUtil.executeQuery("getBehavesLikeForDeposit", prodMap);
                        if(lst!=null && lst.size()>0)
                            prodMap = (HashMap)lst.get(0);
                        if(prodMap.get("BEHAVES_LIKE").equals("FIXED")){
                            prodMap.put("PRODUCT_TYPE","TD");
                            prodMap.put("ACT_NUM",getTxtAccNo());
                            double intAmt = 0.0;
                            double totAmt = 0.0;
                            lst = ClientUtil.executeQuery("getIntForDeptIntTable", prodMap);
                            if(lst!=null && lst.size()>0){
                                interestMap = (HashMap)lst.get(0);
                                interestMap.put("ACT_NUM",getTxtAccNo());
                                interestMap.put("PROD_ID",objCashTransactionTO.getProdId());
                                interestMap.put("PRODUCT_TYPE","TD");
                                interestMap.put("AMOUNT",getTxtAmount());
                                interestMap.put("INT_AMT", new Double(totAmt));
                                interestMap.put("INT_PAID_DATE",curDate.clone());
                                interestMap.put("PAYABLE_INTEREST", "PAYABLE_INTEREST");
                                System.out.println("Excpetion interestMap :"+interestMap);
                                data.put("INTERESTMAP",interestMap);
                                System.out.println("Excpetion data :"+data);
                            }
                        }
                    }                    
                }
            }
            objCashTransactionTO.setBranchId(getSelectedBranchID());
            objCashTransactionTO.setInstrumentNo1("INTEREST_AMT");
        }
        if(depositRenewalFlag == true){//this is atthe time renewal it will work 
            objCashTransactionTO.setInstrumentNo2("DEPOSIT_RENEWAL");            
        }
        if(objCashTransactionTO.getProdType().equals("TD") && getAuthorizeMap()!=null){
            HashMap interestMap = new HashMap();
            interestMap.put("ACT_NUM",objCashTransactionTO.getActNum());
            List lst = ClientUtil.executeQuery("getIntForDeptIntTable", interestMap);
            if(lst!=null && lst.size()>0){
                interestMap = (HashMap)lst.get(0);
                interestMap.put("PRODUCT_TYPE","TD");
                interestMap.put("AMOUNT",getTxtAmount());
                interestMap.put("INT_AMT", new Double(getTxtAmount()));
                interestMap.put("INT_PAID_DATE",curDate.clone());
                interestMap.put("PAYABLE_INTEREST", "PAYABLE_INTEREST");
                System.out.println("Excpetion interestMap :"+interestMap);
                data.put("INTERESTMAP",interestMap);
                System.out.println("Excpetion data :"+data);
            }		
        }
        if(!getCreatingFlexi().equals("") && getCreatingFlexi().equals("FLEXI_LIEN_CREATION")){
            data.put("FLEXI_LIEN_CREATION",getCreatingFlexi());
            data.put("FLEXI_AMOUNT",String.valueOf(getFlexiAmount()));
        }
        if(!getCreatingFlexi().equals("") && getCreatingFlexi().equals("FLEXI_LIEN_DELETION"))
            data.put("FLEXI_LIEN_DELETION",getCreatingFlexi());

//        if(!getCreatingFlexi().equals("") && getCreatingFlexi().equals("FLEXI_LIEN_REDUCING")){
//            data.put("FLEXI_LIEN_REDUCING",getCreatingFlexi());
//            data.put("FLEXI_AMOUNT",String.valueOf(getFlexiAmount()));
//        }
        System.out.println("Excpetion"+data);
        data.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
        if(getAuthorizeMap()!=null) {
            isExist=true;
            if(oaMap!=null)
                if(oaMap.containsKey("LINK_BATCH_ID"))
                    if(oaMap.get("LINK_BATCH_ID")!=null){
                        data.put("LINK_BATCH_ID",oaMap.get("LINK_BATCH_ID"));
                        data.put("BATCH_ID",oaMap.get("BATCH_ID"));
                        data.put("REMARKS","Due to Processing Charge");
                        
                    }
            data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap());
            //            HashMap checkClearBalance=new HashMap();
            //            String actNum=getTxtAccNo().substring(0,getTxtAccNo().lastIndexOf("_"));
            //            checkClearBalance.put("ACT_NUM",actNum);
            //            List lst=ClientUtil.executeQuery("getClearBalance", checkClearBalance);
            //            checkClearBalance=(HashMap)lst.get(0);
            //            long clearBalance=CommonUtil.convertObjToLong(checkClearBalance.get("CLEAR_BALANCE"));
            //            String LimitAt=getLimitAmount();
            //            LimitAmt=Double.parseDouble(LimitAt.replaceAll(",",""));
            //            System.out.println("##loanAmount"+LimitAmt);
            //            if(clearBalance==0){
            //                isExist=checkForOA();
            //                if(isExist==false){
            //                    ClientUtil.showMessageWindow("CUSTOMER NOT HAVING OPERATIVE ACCOUT OR SUFFICENT BALANCE DETECT FROM TERMLOAN AMOUNT");
            //                }
            //            }
            //            if(isExist){
            //                if( procChargeHash.containsKey("PROC_AMT")){
            //                    data.put("PROCCHARGEMAP",procChargeHash);
            System.out.println("observableside:  datamap"+data);
            //            try{ //test
            proxyReturnMap = proxy.execute(data,operationMap);
            setCreatingFlexi("");
            setFlexiAmount(0.0);
            //                }
            //                else
            //            } //test
            //            catch(Exception e){ //for testing
            //                    throw new  TTException("In Sufficent balance for operative account");
            //            }
            
            //, frame);
        } else {
            data.put("CashTransactionTO",objCashTransactionTO);
            data.put("DENOMINATION_LIST", getDenominationList());
            data.put("OLDAMOUNT", new Double(oldAmount));
            if (getCorpLoanMap()!=null && getCorpLoanMap().size()>0) { // For Corporate Loan purpose added by Rajesh
                data.put("CORP_LOAN_MAP", getCorpLoanMap());
            }
            proxyReturnMap = proxy.execute(data,operationMap);
            setProxyReturnMap(proxyReturnMap);//, frame);
            
        }
        System.out.println("proxyReturnMap data :"+proxyReturnMap);
        if (proxyReturnMap != null && proxyReturnMap.containsKey("ERRORLIST")) {
            ArrayList errList = (ArrayList) proxyReturnMap.get("ERRORLIST");
            String errMessage="";
            if (errList!=null && errList.size()>0) {
                for (int i=0; i<errList.size(); i++) {
                    String msg = CommonUtil.convertObjToStr(errList.get(i));
                    if (msg.length()>0)
                        errMessage = errMessage + msg + "\n";
                }
                if (errMessage.length()>0)
                    ClientUtil.showMessageWindow("errMessage");
            }
        }
        if (proxyReturnMap != null && proxyReturnMap.containsKey(CommonConstants.TRANS_ID)) {
            //                if(arrayList.size()==1)
            ClientUtil.showMessageWindow("Transaction No. : " + CommonUtil.convertObjToStr(proxyReturnMap.get(CommonConstants.TRANS_ID)));
            //                else{
            tranIdList+=proxyReturnMap.get(CommonConstants.TRANS_ID)+"\n";
            //                }
        }
        System.out.println("linkMap####"+linkMap);
        setDepTransId(CommonUtil.convertObjToStr(proxyReturnMap.get("TRANS_ID")));
        interestAmt = depTrans();
        setInterestAmt(interestAmt);
        //        }
//        if (proxyReturnMap != null && proxyReturnMap.containsKey(CommonConstants.TRANS_ID)) {
//            ClientUtil.showMessageWindow(tranIdList);
//        }
        setResult(actionType);
//        resetForm();
    }
    //as an whenc customer updation of charges
    private void chargesCollected(String act_num,String chargeType ,double amount)throws Exception{
        HashMap chargeMap=new HashMap();
        chargeMap.put("ACT_NUM",act_num);
        chargeMap.put("CHARGE_TYPE",chargeType);
        
        List lst= ClientUtil.executeQuery("getChargeDetailsforUpdate",chargeMap);
        if(lst!=null && lst.size()>0){
            for(int i=0;i<lst.size();i++){
                chargeMap=(HashMap)lst.get(i);
                chargeMap.put("CHARGE_NO",CommonUtil.convertObjToDouble(chargeMap.get("CHARGE_NO"))); //avoid class cast exception
                chargeMap.put("CHARGE_TYPE",CommonUtil.convertObjToStr(chargeMap.get("CHARGE_TYPE"))); //avoid class cast exception
                chargeMap.put("ACT_NUM",CommonUtil.convertObjToStr(chargeMap.get("ACT_NUM"))); //avoid class cast exception
                double paidAmt=CommonUtil.convertObjToDouble(chargeMap.get("PAID_AMT")).doubleValue();
                double payableAmt=CommonUtil.convertObjToDouble(chargeMap.get("PAYABLE_AMT")).doubleValue();
                if(amount>0){
                    if(amount>payableAmt){
                        amount-=payableAmt;
                        chargeMap.put("PAID_AMT",new Double(paidAmt+payableAmt));
                    }else {
                        chargeMap.put("PAID_AMT",new Double(paidAmt+amount));
                        amount=0;
                    }
                    ClientUtil.execute("updateChargeDetails", chargeMap);
                }
            }
        }
    }
    private ArrayList setTransactionDetailTLAD()throws Exception{
        ArrayList cashList=new ArrayList();
        double paidInterest=0;
        double transAmt =0.0;
        if(asAnWhenCustomer!=null && asAnWhenCustomer.length()>0 && asAnWhenCustomer.equals("Y")){// && actionType==ClientConstants.ACTIONTYPE_NEW){
            HashMap map=new HashMap();
            if(actionType==ClientConstants.ACTIONTYPE_DELETE){
                HashMap whereMap=new HashMap();
                whereMap.put(CommonConstants.MAP_WHERE,linkMap.get("ACCT_NUM"));
                
                List lst=ClientUtil.executeQuery("getCashTransactionTOForAuthorzation", whereMap );
                if(lst!=null && lst.size()>0){
                    cashList=new ArrayList();
                    for(int i=0;i<lst.size();i++){
                        CashTransactionTO objCashTransactionTO=(CashTransactionTO)lst.get(i);
                        cashList.add(objCashTransactionTO);
                    }
                }
                return cashList;
            }
            if(actionType==ClientConstants.ACTIONTYPE_NEW){
                map.put("PROD_ID",CommonUtil.convertObjToStr(getCbmProdId().getKeyForSelected()));
                List lst=ClientUtil.executeQuery("getInterestAndPenalIntActHead", map);
                if(lst!=null && lst.size()>0)
                    map=(HashMap)lst.get(0);
                transAmt=Double.parseDouble(getTxtAmount());
                //postage charges
                CashTransactionTO objCashTransactionTO = setCashTransaction();
                double postageCharges=CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("POSTAGE CHARGES")).doubleValue();
                if(transAmt>0 && postageCharges>0 ){
                    if( transAmt>=postageCharges){
                        transAmt-=postageCharges;
                        paidInterest=postageCharges;
                    }else{
                        paidInterest=transAmt;
                        transAmt-=postageCharges;
                        
                    }
                    objCashTransactionTO.setAmount(new Double(paidInterest));
                    objCashTransactionTO.setActNum("");
                    objCashTransactionTO.setProdId("");
                    objCashTransactionTO.setProdType("GL");
                    objCashTransactionTO.setAcHdId(CommonUtil.convertObjToStr(map.get("POSTAGE_CHARGES")));
                    objCashTransactionTO.setLinkBatchId(CommonUtil.convertObjToStr(getTxtAccNo()));
                    objCashTransactionTO.setAuthorizeRemarks("POSTAGE CHARGES");
                    cashList.add(objCashTransactionTO);
                }
                paidInterest=0;
                //arbitary charges
                objCashTransactionTO = setCashTransaction();
                double orbitaryCharges=CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("ARBITRARY CHARGES")).doubleValue();
                if(transAmt>0 && orbitaryCharges>0 ){
                    if( transAmt>=orbitaryCharges){
                        transAmt-=orbitaryCharges;
                        paidInterest=orbitaryCharges;
                    }else{
                        paidInterest=transAmt;
                        transAmt-=orbitaryCharges;
                        
                    }
                    objCashTransactionTO.setAmount(new Double(paidInterest));
                    objCashTransactionTO.setActNum("");
                    objCashTransactionTO.setProdId("");
                    objCashTransactionTO.setProdType("GL");
                    objCashTransactionTO.setAcHdId(CommonUtil.convertObjToStr(map.get("MISC_SERV_CHRG")));
                    objCashTransactionTO.setLinkBatchId(CommonUtil.convertObjToStr(getTxtAccNo()));
                    objCashTransactionTO.setAuthorizeRemarks("ARBITRARY CHARGES");
                    cashList.add(objCashTransactionTO);
                }
                paidInterest=0;
                //legal charges
                objCashTransactionTO = setCashTransaction();
                double legalCharges=CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("LEGAL CHARGES")).doubleValue();
                if(transAmt>0 && legalCharges>0 ){
                    if( transAmt>=legalCharges){
                        transAmt-=legalCharges;
                        paidInterest=legalCharges;
                    }else{
                        paidInterest=transAmt;
                        transAmt-=legalCharges;
                        
                    }
                    objCashTransactionTO.setAmount(new Double(paidInterest));
                    objCashTransactionTO.setActNum("");
                    objCashTransactionTO.setProdId("");
                    objCashTransactionTO.setProdType("GL");
                    objCashTransactionTO.setAcHdId(CommonUtil.convertObjToStr(map.get("NOTICE_CHARGES")));
                    objCashTransactionTO.setLinkBatchId(CommonUtil.convertObjToStr(getTxtAccNo()));
                    objCashTransactionTO.setAuthorizeRemarks("LEGAL CHARGES");
                    cashList.add(objCashTransactionTO);
                }
                paidInterest=0;
                //insurance charges
                objCashTransactionTO = setCashTransaction();
                double insuranceCharge=CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("INSURANCE CHARGES")).doubleValue();
                if(transAmt>0 && insuranceCharge>0 ){
                    if( transAmt>=insuranceCharge){
                        transAmt-=insuranceCharge;
                        paidInterest=insuranceCharge;
                    }else{
                        paidInterest=transAmt;
                        transAmt-=insuranceCharge;
                        
                    }
                    objCashTransactionTO.setAmount(new Double(paidInterest));
                    objCashTransactionTO.setActNum("");
                    objCashTransactionTO.setProdId("");
                    objCashTransactionTO.setProdType("GL");
                    objCashTransactionTO.setAcHdId(CommonUtil.convertObjToStr(map.get("MISC_SERV_CHRG")));
                    objCashTransactionTO.setLinkBatchId(CommonUtil.convertObjToStr(getTxtAccNo()));
                    objCashTransactionTO.setAuthorizeRemarks("INSURANCE CHARGES");
                    cashList.add(objCashTransactionTO);
                }
                paidInterest=0;
                //missleneous
                objCashTransactionTO = setCashTransaction();
                double miscellous=CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("MISCELLANEOUS CHARGES")).doubleValue();
                if(transAmt>0 && miscellous>0 ){
                    if( transAmt>=miscellous){
                        transAmt-=miscellous;
                        paidInterest=miscellous;
                    }else{
                        paidInterest=transAmt;
                        transAmt-=miscellous;
                        
                    }
                    objCashTransactionTO.setAmount(new Double(paidInterest));
                    objCashTransactionTO.setActNum("");
                    objCashTransactionTO.setProdId("");
                    objCashTransactionTO.setProdType("GL");
                    objCashTransactionTO.setAcHdId(CommonUtil.convertObjToStr(map.get("MISC_SERV_CHRG")));
                    objCashTransactionTO.setLinkBatchId(CommonUtil.convertObjToStr(getTxtAccNo()));
                    objCashTransactionTO.setAuthorizeRemarks("MISCELLANEOUS CHARGES");
                    cashList.add(objCashTransactionTO);
                }
                paidInterest=0;
                //execution degree
                objCashTransactionTO = setCashTransaction();
                double executionDegree=CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("EXECUTION DECREE CHARGES")).doubleValue();
                if(transAmt>0 && executionDegree>0 ){
                    if( transAmt>=executionDegree){
                        transAmt-=executionDegree;
                        paidInterest=executionDegree;
                    }else{
                        paidInterest=transAmt;
                        transAmt-=executionDegree;
                        
                    }
                    objCashTransactionTO.setAmount(new Double(paidInterest));
                    objCashTransactionTO.setActNum("");
                    objCashTransactionTO.setProdId("");
                    objCashTransactionTO.setProdType("GL");
                    objCashTransactionTO.setAcHdId(CommonUtil.convertObjToStr(map.get("MISC_SERV_CHRG")));
                    objCashTransactionTO.setLinkBatchId(CommonUtil.convertObjToStr(getTxtAccNo()));
                    objCashTransactionTO.setAuthorizeRemarks("PENAL_INT");
                    cashList.add(objCashTransactionTO);
                }
                paidInterest=0;
                //penal interest
                objCashTransactionTO = setCashTransaction();
                double penalInterest=CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("LOAN_CLOSING_PENAL_INT")).doubleValue();
                if(transAmt>0 && penalInterest>0 ){
                    if( transAmt>=penalInterest){
                        transAmt-=penalInterest;
                        paidInterest=penalInterest;
                    }else{
                        paidInterest=transAmt;
                        transAmt-=penalInterest;
                        
                    }
                    objCashTransactionTO.setAmount(new Double(paidInterest));
                    objCashTransactionTO.setActNum("");
                    objCashTransactionTO.setProdId("");
                    objCashTransactionTO.setProdType("GL");
                    objCashTransactionTO.setAcHdId(CommonUtil.convertObjToStr(map.get("PENAL_INT")));
                    objCashTransactionTO.setLinkBatchId(CommonUtil.convertObjToStr(getTxtAccNo()));
                    objCashTransactionTO.setAuthorizeRemarks("PENAL_INT");
                    cashList.add(objCashTransactionTO);
                }
                //interest
                objCashTransactionTO = setCashTransaction();
                double interest=CommonUtil.convertObjToDouble(ALL_LOAN_AMOUNT.get("INTEREST")).doubleValue();
                if(transAmt>0 && interest>0 ){
                    if( transAmt>=interest){
                        transAmt-=interest;
                        paidInterest=interest;
                    }else{
                        paidInterest=transAmt;
                        transAmt-=interest;
                        
                    }
                    objCashTransactionTO.setAmount(new Double(paidInterest));
                    objCashTransactionTO.setActNum("");
                    objCashTransactionTO.setProdId("");
                    objCashTransactionTO.setProdType("GL");
                    objCashTransactionTO.setAcHdId(CommonUtil.convertObjToStr(map.get("AC_DEBIT_INT")));
                    objCashTransactionTO.setLinkBatchId(CommonUtil.convertObjToStr(getTxtAccNo()));
                    objCashTransactionTO.setAuthorizeRemarks("INTEREST");
                    cashList.add(objCashTransactionTO);
                }
                //principal
                if(transAmt> 0.0) {
                    objCashTransactionTO = setCashTransaction();
                    objCashTransactionTO.setAmount(new Double(transAmt));
                    objCashTransactionTO.setLinkBatchId(CommonUtil.convertObjToStr(getTxtAccNo()));
                    cashList.add(objCashTransactionTO);
                }
            }
        }else{
            final CashTransactionTO objCashTransactionTO = setCashTransaction();
            cashList.add(objCashTransactionTO);
        }
        return cashList;
    }
    public double depTrans(){
        HashMap depMap = new HashMap();
        double intAmt = 0.0;
        double amt = 0.0;
        if(!getDepTransId().equals("") && getDepTransId().length()>0){
        depMap.put("TRANS_ID",getDepTransId());
        depMap.put("TRANS_DT", curDate.clone());
        depMap.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
        List lst = ClientUtil.executeQuery("getCashTransactionAmount", depMap);
        if(lst!=null && lst.size()>0)
            for(int i =0; i<lst.size();i++){
                depMap = (HashMap)lst.get(i);
                amt = CommonUtil.convertObjToDouble(depMap.get("AMOUNT")).doubleValue();
                intAmt = amt + intAmt;
            }
        }
        System.out.println("#####intAmt : "+intAmt);
        return intAmt;
    }
    
    
    public void checkBalanceOperativeAccount(){
        HashMap checkClearBalance=new HashMap();
        HashMap proxyReturnMap=new HashMap();
        boolean flagstatus=false;
        String actNum;
        procChargeHash=new HashMap();
        result1=1;
        if(getTxtAccNo().lastIndexOf("_")!=-1)
            actNum=getTxtAccNo().substring(0,getTxtAccNo().lastIndexOf("_"));
        else
            actNum=getTxtAccNo();
        checkClearBalance.put("ACT_NUM",actNum);
        List list=ClientUtil.executeQuery("getDisbursementDetailsTL",checkClearBalance);
        int NO_DISBURSEMENT=0;
        if(list.size()>0){
            HashMap hash=(HashMap)list.get(0);
            NO_DISBURSEMENT=CommonUtil.convertObjToInt(hash.get("NO_DISBURSEMENT"));
            
        }
        if(NO_DISBURSEMENT ==0){
            System.out.println("####checkBalance"+actNum);
            List lst=ClientUtil.executeQuery("getClearBalance", checkClearBalance);
            checkClearBalance=(HashMap)lst.get(0);
            long clearBalance=CommonUtil.convertObjToLong(checkClearBalance.get("CLEAR_BALANCE"));
            String LimitAt=getLimitAmount();
            LimitAmt=Double.parseDouble(LimitAt.replaceAll(",",""));
            System.out.println("##loanAmount"+LimitAmt);
            if(clearBalance==0){
                isExist=checkForOA();
                flagstatus=isFlag();
                
                
                
            }
            if(isExist){
                
                if(procChargeHash.containsKey("PROC_AMT")){
                    procChargeHash.put("FLAG_STATUS",new Boolean(flagstatus));
                    oaMap.put("PROCCHARGEMAP",procChargeHash);
                    
                }
                //            proxyReturnMap = proxy.execute(data,operationMap);  ///abi
            }

        }}
    
    boolean checkForOA(){
        double procPercentage=0;
        double oaAmt=0;
        int result=0;
        HashMap whereMap=new HashMap();
        HashMap actHash=new HashMap();
        HashMap hash=new HashMap();
        String prod_id="";
        String actnum="";
        if(getTxtAccNo().lastIndexOf("_")!=-1)
            actnum=getTxtAccNo().substring(0,getTxtAccNo().lastIndexOf("_"));
        else
            actnum=getTxtAccNo();
        
        //        whereMap.put(CommonConstants.MAP_NAME,"getProcPercentageTL");
        actHash.put("ACT_NUM",actnum);
        //  whereMap.put(CommonConstants.MAP_WHERE,actHash);
        System.out.println("###ACTNUM"+actnum);
        try{
            //            HashMap hash=ClientUtil.executeQuery("getProcPercentageTL", whereMap);
            List lst=(List)ClientUtil.executeQuery("getProcPercentageTL", actHash);
            
            if(lst !=null && lst.size()>0){
                HashMap procChargeMap=(HashMap)lst.get(0);
                procPercentage=CommonUtil.convertObjToDouble(procChargeMap.get("PROC_CHRG_PER")).doubleValue();
                prod_id=CommonUtil.convertObjToStr(procChargeMap.get("PROD_ID"));
                //            whereMap.put(CommonConstants.MAP_NAME,"getOABalanceTLCustomer");
                
                lst=(List)ClientUtil.executeQuery("getOABalanceTLCustomer", actHash);
                //            hash =proxy.executeQuery(whereMap, operationMap);
                
                if(lst !=null && lst.size()>0){
                    System.out.println("secondhash"+lst);
                    hash = (HashMap)lst.get(0);
                    hash.put("LOAN_PROD_ID",prod_id);
                    oaAmt = Double.parseDouble(hash.get("CLEAR_BALANCE").toString());
                }
                if (procPercentage > 0){
                    procChargeHash.putAll(hash);
                }
                
                
                procAmount= LimitAmt*(procPercentage/100);
                procChargeHash.put("LINK_BATCH_ID",actnum);
                if(procAmount<=oaAmt){
                    System.out.println(oaAmt+"OAAMT"+"PROCAMT#####"+procAmount);
                    procChargeHash.put("OA_ACT_NUM",hash.get("ACT_NUM"));
                    procChargeHash.put("OA_PROD_ID",hash.get("PROD_ID"));
                    procChargeHash.put("TL_PROD_ID", prod_id);
                    procChargeHash.put("PROC_AMT",new Double(procAmount));
                    ClientUtil.displayAlert("processing charge collected from operative");
                    result1=0;
                }
                else if (procAmount > oaAmt) {
                    //            result1=ClientUtil.confirmationAlert("Insufficient Balance in Operative A/c to Debit Processing amount.\n Do u want collect from LoanAccount");
                    ClientUtil.displayAlert("Insufficient Balance in Operative A/c processing charge not collected");
                    result1=1;
                    
                    
                    
                    
                    //                        String amountLimit=getLimitAmount();
                    //                        double amtLimit=Double.parseDouble(amountLimit.replaceAll(",",""));
                    //                        amtLimit-=procAmount;
                    //                        setLimitAmount("amtLimit");
                    procChargeHash.put("TL_ACT_NUM",actHash.get("ACT_NUM"));
                    procChargeHash.put("TL_PROD_ID", prod_id);
                    procChargeHash.put("PROC_AMT",new Double(procAmount));
                    setFlag(true);
                    System.out.println("conformation result  :"+result1);
                    if(result1==0)
                        //                 procChargeHash=null;
                        
                        return true;
                }
            } else
                return false;
        } catch(Exception e){
            System.out.println("ERROR"+e);
        }
        return true;
    }
    
    
    
    public void changeAmount(){
        
        if(result1 ==0){
            System.out.println("#####"+getTxtAmount().toString());
            String  amount=getTxtAmount();
            amount=amount.replaceAll(",", "");
            System.out.println("###amount"+amount);
            
            
            if(amount!=null){
                //               int result= ClientUtil.confirmationAlert("Insufficient of Operative Account Detect Processing Charge From TermLoan");
                double amt=Double.parseDouble(amount);
                if(result1 ==0)
                    amt-=procAmount;
                System.out.println(result +  "balanceamount###"+amt);
                
                setTxtAmount(String.valueOf(amt));
            }
        }
        
    }
    
    public boolean checkAcNoWithoutProdType(String actNum) {
        HashMap mapData=new HashMap();
        boolean isExists = false;
        try{//dont delete chck selectalldao
            mapData.put("ACT_NUM", actNum);
            List mapDataList = ClientUtil.executeQuery("getActNumFromAllProducts", mapData); 
            System.out.println("#### mapDataList :"+mapDataList);
            if (mapDataList!=null && mapDataList.size()>0) {
                mapData=(HashMap)mapDataList.get(0);
                setTxtAccNo(CommonUtil.convertObjToStr(mapData.get("ACT_NUM")));
                setSelectedBranchID(CommonUtil.convertObjToStr(mapData.get("BRANCH_ID")));
                cbmProdType.setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_TYPE")));
                setProdType(CommonUtil.convertObjToStr(mapData.get("PROD_TYPE")));
                setCbmProdId(getProdType());
//                getProducts();
                cbmProdId.setKeyForSelected(CommonUtil.convertObjToStr(mapData.get("PROD_ID")));
                isExists = true;
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
            }
            mapDataList = null;
        }catch(Exception e){
            e.printStackTrace();
        }
        mapData = null;
        return isExists;
    }
    
    public void getProducts(){
        List list=null;
        ArrayList key=new ArrayList();
        ArrayList value=new ArrayList();
        key.add("");
        value.add("");

        HashMap data;
        list=ClientUtil.executeQuery("Transfer.getCreditProduct"+getProdType(),null);
        if(list!=null && list.size()>0){
            int size=list.size();
            for(int i=0;i<size;i++){
                data=(HashMap)list.get(i);
                key.add(data.get("PRODID"));
                value.add(data.get("PRODDESC"));
            }
        }        
        data = null;        
        cbmProdType = new ComboBoxModel(key,value);
        setChanged();
    }
    
    public void getLinkBatchId(){
        
        String accNum=getTxtAccNo();
        oaMap=new HashMap();
        if(accNum.lastIndexOf("_")!=-1)
            accNum=accNum.substring(0,accNum.lastIndexOf("_"));
        HashMap hash=new HashMap();
        hash.put("ACT_NUM",accNum);
        List lst=ClientUtil.executeQuery("getLinkBatchValues", hash);
        System.out.println(hash+"####lst"+lst);
        if(lst.size()>0)
            oaMap=(HashMap)lst.get(0);
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
    private String getCommand() throws Exception{
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
    public int getActionType(){
        return actionType;
    }
    
    // Sets the value of the Action Type to the poperation we want to execute...
    public void setActionType(int actionType) {
        this.actionType = actionType;
        setChanged();
    }
    
    // To reset all the fields in the UI
    public void resetForm(){
        log.info("In resetForm()");
        oldAmount = 0.0;
        _authorizeMap = null;
        setCboProdId("");
        setCboProdType("");
        setTxtAccNo("");
        setTxtInitiatorChannel("");
        setRdoTransactionType_Debit(false);
        setRdoTransactionType_Credit(false);
        setTxtInputAmt("");
        setLblHouseName("");
        setCboInputCurrency("");
        setCboInstrumentType("");
        setTxtInstrumentNo1("");
        setTxtInstrumentNo2("");
        setTdtInstrumentDate("");
        setTxtTokenNo("");
        setTxtAmount("");
        setTxtParticulars("");
        setTxtNarration("");
        setProdType("");
        setDenominationList(null);
        setDepositPenalAmt("");
        setDepositPenalMonth("");
        this.setCr_cash("");
        this.setDr_cash("");
        linkMap=new HashMap();
        linkBathList=null;
        txtPanNo="";
        cboProdType = "";
        getCbmProdType().setKeyForSelected("");
        setSelectedBranchID(ProxyParameters.BRANCH_ID);
        penalWaiveOff=false;
        rebateInterest=false;
        setClosedAccNo("");
        setBalanceType("");
        setHoAccount(false);
        
        ttNotifyObservers();
    }
    
    //To reset all the Lables in the UI...
    public void resetLable(){
        this.setTxtAccHd("");
        this.setLblAccHdDesc("");
        
        this.setLblAccName("");
        
        this.setLblTransactionId("");
        this.setLblTransDate("");
        this.setLblInitiatorId("");
        
    }
    
    // To set and change the Status of the lable STATUS
    public void setResult(int result) {
        this.result = result;
        setChanged();
    }
    public int getResult(){
        return this.result;
    }
    
    // To set the Value of the lblStatus...
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }
    public String getLblStatus(){
        return lblStatus;
    }
    
    //To reset the Value of lblStatus after each save action...
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }
    
    /** To set the status based on ActionType, either New, Edit, etc., */
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
        ttNotifyObservers();
    }
    
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        ttNotifyObservers();
    }
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    
    private String cboProdId = "";
    private String cboProdType = "";
    private String txtAccNo = "";
    private String txtInitiatorChannel = "";
    private boolean rdoTransactionType_Debit = false;
    private boolean rdoTransactionType_Credit = false;
    private String txtInputAmt = "";
    private String cboInputCurrency = "";
    private String cboInstrumentType = "";
    private String txtInstrumentNo1 = "";
    private String txtInstrumentNo2 = "";
    private String tdtInstrumentDate = "";
    private String txtTokenNo = "";
    private String txtAmount = "";
    private String txtParticulars = "";
    private String txtPanNo="";
    private String txtNarration = "";
    
    void setInitiatorChannelValue(){
        setTxtInitiatorChannel(INITIATORTYPE);
        ttNotifyObservers();
    }
    
    
    void setCboProdId(String cboProdId){
        this.cboProdId = cboProdId;
        setChanged();
    }
    String getCboProdId(){
        return this.cboProdId;
    }
    
    
    public void setCbmProdId(String prodType) {
        if (CommonUtil.convertObjToStr(prodType).length()>1) {
            if (prodType.equals("GL")) {
                key = new ArrayList();
                value = new ArrayList();
            } else {
                try {
                    lookUpHash = new HashMap();
                    lookUpHash.put(CommonConstants.MAP_NAME,"Cash.getAccProduct" + prodType);
                    lookUpHash.put(CommonConstants.PARAMFORQUERY, null);
                    keyValue = ClientUtil.populateLookupData(lookUpHash);
                    getKeyValue((HashMap)keyValue.get(CommonConstants.DATA));
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
        cbmProdId = new ComboBoxModel(key,value);
        this.cbmProdId = cbmProdId;
        setChanged();
    }
    
    public List getDocumentDetails(String mapName, String mapWhere) {
        List lst=null;
        HashMap hash = new HashMap();
        hash.put(CommonConstants.MAP_NAME,mapName);
        hash.put(CommonConstants.MAP_WHERE,mapWhere);
        try{
            lst = (List)proxy.executeQuery(hash,operationMap).get("DOCUMENT_LIST");
        }
        catch(Exception e) {
            System.out.println(e);
        }
        return lst;
    }
    
    ComboBoxModel getCbmProdId(){
        return cbmProdId;
    }
    
    void setCboProdType(String cboProdType){
        this.cboProdType = cboProdType;
        setChanged();
    }
    String getCboProdType(){
        return this.cboProdType;
    }
    
    public void setCbmProdType(ComboBoxModel cbmProdType){
        this.cbmProdType = cbmProdType;
        setChanged();
    }
    
    ComboBoxModel getCbmProdType(){
        return cbmProdType;
    }
    
    void setTxtAccNo(String txtAccNo){
        this.txtAccNo = txtAccNo;
        setChanged();
    }
    String getTxtAccNo(){
        return this.txtAccNo;
    }
    
    void setTxtInitiatorChannel(String txtInitiatorChannel){
        this.txtInitiatorChannel = txtInitiatorChannel;
        setChanged();
    }
    String getTxtInitiatorChannel(){
        return this.txtInitiatorChannel;
    }
    
    void setRdoTransactionType_Debit(boolean rdoTransactionType_Debit){
        this.rdoTransactionType_Debit = rdoTransactionType_Debit;
        setChanged();
    }
    boolean getRdoTransactionType_Debit(){
        return this.rdoTransactionType_Debit;
    }
    
    void setRdoTransactionType_Credit(boolean rdoTransactionType_Credit){
        this.rdoTransactionType_Credit = rdoTransactionType_Credit;
        setChanged();
    }
    boolean getRdoTransactionType_Credit(){
        return this.rdoTransactionType_Credit;
    }
    
    void setTxtInputAmt(String txtInputAmt){
        this.txtInputAmt = txtInputAmt;
        setChanged();
    }
    String getTxtInputAmt(){
        return this.txtInputAmt;
    }
    void setCboInputCurrency(String cboInputCurrency){
        this.cboInputCurrency = cboInputCurrency;
        setChanged();
    }
    String getCboInputCurrency(){
        return this.cboInputCurrency;
    }
    
    public void setCbmInputCurrency(ComboBoxModel cbmInputCurrency){
        this.cbmInputCurrency = cbmInputCurrency;
        setChanged();
    }
    
    ComboBoxModel getCbmInputCurrency(){
        return cbmInputCurrency;
    }
    
    void setCboInstrumentType(String cboInstrumentType){
        this.cboInstrumentType = cboInstrumentType;
        setChanged();
    }
    String getCboInstrumentType(){
        return this.cboInstrumentType;
    }
    
    public void setCbmInstrumentType(ComboBoxModel cbmInstrumentType){
        this.cbmInstrumentType = cbmInstrumentType;
        setChanged();
    }
    
    ComboBoxModel getCbmInstrumentType(){
        return cbmInstrumentType;
    }
    
    void setTxtInstrumentNo1(String txtInstrumentNo1){
        this.txtInstrumentNo1 = txtInstrumentNo1;
        setChanged();
    }
    String getTxtInstrumentNo1(){
        return this.txtInstrumentNo1;
    }
    
    void setTxtInstrumentNo2(String txtInstrumentNo2){
        this.txtInstrumentNo2 = txtInstrumentNo2;
        setChanged();
    }
    String getTxtInstrumentNo2(){
        return this.txtInstrumentNo2;
    }
    
    void setTdtInstrumentDate(String tdtInstrumentDate){
        this.tdtInstrumentDate = tdtInstrumentDate;
        setChanged();
    }
    String getTdtInstrumentDate(){
        return this.tdtInstrumentDate;
    }
    
    void setTxtTokenNo(String txtTokenNo){
        this.txtTokenNo = txtTokenNo;
        setChanged();
    }
    String getTxtTokenNo(){
        return this.txtTokenNo;
    }
    
    void setTxtAmount(String txtAmount){
        this.txtAmount = txtAmount;
        setChanged();
    }
    String getTxtAmount(){
        return this.txtAmount;
    }
    
    void setTxtParticulars(String txtParticulars){
        this.txtParticulars = txtParticulars;
        setChanged();
    }
    String getTxtParticulars(){
        return this.txtParticulars;
    }
    
    //To Set the Lables In the UI...
    //==========================================
    public void setTxtAccHd(String txtAccHdId){
        this.txtAccHdId = txtAccHdId;
        setChanged();
    }
    public String getTxtAccHd(){
        return this.txtAccHdId;
    }
    
    public void setLblAccHdDesc(String lblAccHdDesc){
        this.lblAccHdDesc = lblAccHdDesc;
        setChanged();
    }
    public String getLblAccHdDesc(){
        return this.lblAccHdDesc;
    }
    
    // To apply TDS
    private void calculateTDs() {
        if(prodType.equals("TD") && getTxtAccNo()!=null) {
            HashMap tdsCalc =new HashMap();
            String deposit_No = CommonUtil.convertObjToStr(tdsCalc.get("DEPOSIT_NO"));
            if (deposit_No.lastIndexOf("_")!=-1)
                deposit_No = deposit_No.substring(0,deposit_No.lastIndexOf("_"));
            System.out.println("depositNo "+deposit_No);
            tdsCalc.put("DEPOSIT_NO",deposit_No);
            List list = ClientUtil.executeQuery("getCustNoforTDS",tdsCalc);
            tdsCalc =new HashMap();
            if(list.size()>0)
                System.out.println("depositNoforcalculateTDs : "+deposit_No);
            tdsCalc = (HashMap)list.get(0);
            //                tdsCalc.put("CUST_ID", tdsCalc.get("CUST_ID"));
            tdsCalc.put("PROD_ID",getCboProdId());
            tdsCalc.put("PROD_TYPE",getProdType());
            System.out.println("TDSCALC CUSTOMER "+tdsCalc);
            //                TdsCalc tdsCalculator = new TdsCalc(_branchCode);
            //                HashMap tdsMap = tdsCalculator.calculateTds((String)((HashMap)list.get(0)).get("CUST_ID"), null, null, ClientUtil.getCurrentDate(_branchCode), tdsCalc);
        }
        
    }
    
    public void setAccountHead() {
        try {
            System.out.println("here for edit"+(String)cbmProdId.getKeyForSelected());
            final HashMap accountHeadMap = new HashMap();
            accountHeadMap.put("PROD_ID",(String)cbmProdId.getKeyForSelected());
            if(!this.getProdType().equals("") && !this.getProdType().equals("GL") && !accountHeadMap.get("PROD_ID").equals("")){
                final List resultList = ClientUtil.executeQuery("getAccountHeadProd"+this.getProdType(), accountHeadMap);
                final HashMap resultMap = (HashMap)resultList.get(0);
                setTxtAccHd(resultMap.get("AC_HEAD").toString());
                setLblAccHdDesc(resultMap.get("AC_HEAD_DESC").toString());
                this.setCr_cash(((String)resultMap.get("CR_CASH")==null)?" ":((String)resultMap.get("CR_CASH")));
                this.setDr_cash(((String)resultMap.get("DR_CASH")==null)?"":((String)resultMap.get("DR_CASH")));
            }
        }catch(Exception e){
        }
    }
    // For setting the Name of the Account Number Holder...
    public void setLblAccName(String lblAccName){
        this.lblAccName = lblAccName;
        setChanged();
    }
    public String getLblAccName(){
        return this.lblAccName;
    }
    
    public void setAccountName(String AccountNo){
        HashMap resultMap = new HashMap();
        final HashMap accountNameMap = new HashMap();
        List resultList = new ArrayList();
        try {
            if (!prodType.equals("")) {
                accountNameMap.put("ACC_NUM",AccountNo);
                String pID = !prodType.equals("GL") ? getCbmProdId().getKeyForSelected().toString() : "";
                if(prodType.equals("GL") && getTxtAccNo().length()>0){
                    resultList = ClientUtil.executeQuery("getAccountNumberNameTL",accountNameMap);
                } else {
                    resultList = ClientUtil.executeQuery("getAccountNumberName"+this.getProdType(),accountNameMap);
                    List custHouseNameList = ClientUtil.executeQuery("getCustomerHouseName",accountNameMap);
                    if(custHouseNameList!=null && custHouseNameList.size()>0){
                        HashMap dataMap = new HashMap();
                        dataMap = (HashMap) custHouseNameList.get(0);
                        setLblHouseName(CommonUtil.convertObjToStr(dataMap.get("HOUSE_NAME")));
                    }
                }
                if(resultList != null && resultList.size() > 0){
                    if(!prodType.equals("GL")) {
                        HashMap dataMap = new HashMap();
                        accountNameMap.put("BRANCH_ID", getSelectedBranchID());
                        List lst = (List) ClientUtil.executeQuery("getProdIdForActNo"+prodType,accountNameMap);
                        if(lst != null && lst.size() > 0)
                            dataMap = (HashMap) lst.get(0);
                        if(dataMap.get("PROD_ID").equals(pID)){
                            resultMap = (HashMap)resultList.get(0);
                        }
                    } else {
                        resultMap = (HashMap)resultList.get(0);
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        if(resultMap.containsKey("CUSTOMER_NAME"))
            setLblAccName(resultMap.get("CUSTOMER_NAME").toString());
        else
            setLblAccName("");
        //            if(resultList != null){
        //                final HashMap resultMap = (HashMap)resultList.get(0);
        //                setLblAccName(resultMap.get("CUSTOMER_NAME").toString());
        //            } else {
        //                setLblAccName("");
        //            }
        //        }catch(Exception e){
        //
        //        }
    }
    
    // For setting the transaction Id in UI at the Time of Edit or Delete...
    public void setLblTransactionId(String lblTransactionId){
        this.lblTransactionId = lblTransactionId;
        setChanged();
    }
    public String getLblTransactionId(){
        return this.lblTransactionId;
    }
    
    // For setting the Name of the Clearing Date in Ui at the time of Edit and Delete...
    public void setLblTransDate(String lblTransDate){
        this.lblTransDate = lblTransDate;
        setChanged();
    }
    public String getLblTransDate(){
        return this.lblTransDate;
    }
    
    // For setting the Initiator Id in UI at the Time of Edit or Delete...
    public void setLblInitiatorId(String lblInitiatorId){
        this.lblInitiatorId = lblInitiatorId;
        setChanged();
    }
    public String getLblInitiatorId(){
        return this.lblInitiatorId;
    }
    
    /**
     * Getter for property prodType.
     * @return Value of property prodType.
     */
    public java.lang.String getProdType() {
        return prodType;
    }
    
    /**
     * Setter for property prodType.
     * @param prodType New value of property prodType.
     */
    public void setProdType(java.lang.String prodType) {
        this.prodType = prodType;
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
     * Getter for property LimitAmount.
     * @return Value of property LimitAmount.
     */
    public java.lang.String getLimitAmount() {
        return LimitAmount;
    }
    
    /**
     * Setter for property LimitAmount.
     * @param LimitAmount New value of property LimitAmount.
     */
    public void setLimitAmount(java.lang.String LimitAmount) {
        this.LimitAmount = LimitAmount;
    }
    
    /**
     * Getter for property flag.
     * @return Value of property flag.
     */
    public boolean isFlag() {
        return flag;
    }
    
    /**
     * Setter for property flag.
     * @param flag New value of property flag.
     */
    public void setFlag(boolean flag) {
        this.flag = flag;
    }
    
    /**
     * Getter for property ALL_LOAN_AMOUNT.
     * @return Value of property ALL_LOAN_AMOUNT.
     */
    public java.util.HashMap getALL_LOAN_AMOUNT() {
        return ALL_LOAN_AMOUNT;
    }
    
    /**
     * Setter for property ALL_LOAN_AMOUNT.
     * @param ALL_LOAN_AMOUNT New value of property ALL_LOAN_AMOUNT.
     */
    public void setALL_LOAN_AMOUNT(java.util.HashMap ALL_LOAN_AMOUNT) {
        this.ALL_LOAN_AMOUNT = ALL_LOAN_AMOUNT;
    }
    
    /**
     * Getter for property depTransId.
     * @return Value of property depTransId.
     */
    public java.lang.String getDepTransId() {
        return depTransId;
    }
    
    /**
     * Setter for property depTransId.
     * @param depTransId New value of property depTransId.
     */
    public void setDepTransId(java.lang.String depTransId) {
        this.depTransId = depTransId;
    }
    
    /**
     * Getter for property interestAmt.
     * @return Value of property interestAmt.
     */
    public double getInterestAmt() {
        return interestAmt;
    }
    
    /**
     * Setter for property interestAmt.
     * @param interestAmt New value of property interestAmt.
     */
    public void setInterestAmt(double interestAmt) {
        this.interestAmt = interestAmt;
    }
    
    /**
     * Getter for property odExpired.
     * @return Value of property odExpired.
     */
    public boolean isOdExpired() {
        return odExpired;
    }
    
    /**
     * Setter for property odExpired.
     * @param odExpired New value of property odExpired.
     */
    public void setOdExpired(boolean odExpired) {
        this.odExpired = odExpired;
    }
    
    /**
     * Getter for property depInterestAmt.
     * @return Value of property depInterestAmt.
     */
    public java.lang.String getDepInterestAmt() {
        return depInterestAmt;
    }
    
    /**
     * Setter for property depInterestAmt.
     * @param depInterestAmt New value of property depInterestAmt.
     */
    public void setDepInterestAmt(java.lang.String depInterestAmt) {
        this.depInterestAmt = depInterestAmt;
    }
    
    /**
     * Getter for property depLinkBatchId.
     * @return Value of property depLinkBatchId.
     */
    public java.lang.String getDepLinkBatchId() {
        return depLinkBatchId;
    }
    
    /**
     * Setter for property depLinkBatchId.
     * @param depLinkBatchId New value of property depLinkBatchId.
     */
    public void setDepLinkBatchId(java.lang.String depLinkBatchId) {
        this.depLinkBatchId = depLinkBatchId;
    }
    
    /**
     * Getter for property asAnWhenCustomer.
     * @return Value of property asAnWhenCustomer.
     */
    public java.lang.String getAsAnWhenCustomer() {
        return asAnWhenCustomer;
    }
    
    /**
     * Setter for property asAnWhenCustomer.
     * @param asAnWhenCustomer New value of property asAnWhenCustomer.
     */
    public void setAsAnWhenCustomer(java.lang.String asAnWhenCustomer) {
        this.asAnWhenCustomer = asAnWhenCustomer;
    }
    
    /**
     * Getter for property linkMap.
     * @return Value of property linkMap.
     */
    public java.util.HashMap getLinkMap() {
        return linkMap;
    }
    
    /**
     * Setter for property linkMap.
     * @param linkMap New value of property linkMap.
     */
    public void setLinkMap(java.util.HashMap linkMap) {
        this.linkMap = linkMap;
    }
    
    /**
     * Getter for property linkBathList.
     * @return Value of property linkBathList.
     */
    public java.util.List getLinkBathList() {
        return linkBathList;
    }
    
    /**
     * Setter for property linkBathList.
     * @param linkBathList New value of property linkBathList.
     */
    public void setLinkBathList(java.util.List linkBathList) {
        this.linkBathList = linkBathList;
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
     * Getter for property txtPanNo.
     * @return Value of property txtPanNo.
     */
    public java.lang.String getTxtPanNo() {
        return txtPanNo;
    }
    
    /**
     * Setter for property txtPanNo.
     * @param txtPanNo New value of property txtPanNo.
     */
    public void setTxtPanNo(java.lang.String txtPanNo) {
        this.txtPanNo = txtPanNo;
    }
    
    /**
     * Getter for property creatingFlexi.
     * @return Value of property creatingFlexi.
     */
    public java.lang.String getCreatingFlexi() {
        return creatingFlexi;
    }
    
    /**
     * Setter for property creatingFlexi.
     * @param creatingFlexi New value of property creatingFlexi.
     */
    public void setCreatingFlexi(java.lang.String creatingFlexi) {
        this.creatingFlexi = creatingFlexi;
    }
    
    /**
     * Getter for property flexiAmount.
     * @return Value of property flexiAmount.
     */
    public double getFlexiAmount() {
        return flexiAmount;
    }
    
    /**
     * Setter for property flexiAmount.
     * @param flexiAmount New value of property flexiAmount.
     */
    public void setFlexiAmount(double flexiAmount) {
        this.flexiAmount = flexiAmount;
    }
    
    /**
     * Getter for property reconcile.
     * @return Value of property reconcile.
     */
    public java.lang.String getReconcile() {
        return reconcile;
    }
    
    /**
     * Setter for property reconcile.
     * @param reconcile New value of property reconcile.
     */
    public void setReconcile(java.lang.String reconcile) {
        this.reconcile = reconcile;
    }
    
    /**
     * Getter for property balanceType.
     * @return Value of property balanceType.
     */
    public java.lang.String getBalanceType() {
        return balanceType;
    }
    
    /**
     * Setter for property balanceType.
     * @param balanceType New value of property balanceType.
     */
    public void setBalanceType(java.lang.String balanceType) {
        this.balanceType = balanceType;
    }    
    
    /**
     * Getter for property AuthorizeBy.
     * @return Value of property AuthorizeBy.
     */
    public java.lang.String getAuthorizeBy() {
        return AuthorizeBy;
    }
    
    /**
     * Setter for property AuthorizeBy.
     * @param AuthorizeBy New value of property AuthorizeBy.
     */
    public void setAuthorizeBy(java.lang.String AuthorizeBy) {
        this.AuthorizeBy = AuthorizeBy;
    }
    
    /**
     * Getter for property corpLoanMap.
     * @return Value of property corpLoanMap.
     */
    public java.util.Map getCorpLoanMap() {
        return corpLoanMap;
    }
    
    /**
     * Setter for property corpLoanMap.
     * @param corpLoanMap New value of property corpLoanMap.
     */
    public void setCorpLoanMap(java.util.Map corpLoanMap) {
        this.corpLoanMap = corpLoanMap;
    }
    
    /**
     * Getter for property lblHouseName.
     * @return Value of property lblHouseName.
     */
    public java.lang.String getLblHouseName() {
        return lblHouseName;
    }
    
    /**
     * Setter for property lblHouseName.
     * @param lblHouseName New value of property lblHouseName.
     */
    public void setLblHouseName(java.lang.String lblHouseName) {
        this.lblHouseName = lblHouseName;
    }
    
    /**
     * Getter for property txtNarration.
     * @return Value of property txtNarration.
     */
    public java.lang.String getTxtNarration() {
        return txtNarration;
    }
    
    /**
     * Setter for property txtNarration.
     * @param txtNarration New value of property txtNarration.
     */
    public void setTxtNarration(java.lang.String txtNarration) {
        this.txtNarration = txtNarration;
    }
    
    /**
     * Getter for property penalWaiveOff.
     * @return Value of property penalWaiveOff.
     */
    public boolean isPenalWaiveOff() {
        return penalWaiveOff;
    }
    
    /**
     * Setter for property penalWaiveOff.
     * @param penalWaiveOff New value of property penalWaiveOff.
     */
    public void setPenalWaiveOff(boolean penalWaiveOff) {
        this.penalWaiveOff = penalWaiveOff;
    }
    
    /**
     * Getter for property rebateInterest.
     * @return Value of property rebateInterest.
     */
    public boolean isRebateInterest() {
        return rebateInterest;
    }
    
    /**
     * Setter for property rebateInterest.
     * @param rebateInterest New value of property rebateInterest.
     */
    public void setRebateInterest(boolean rebateInterest) {
        this.rebateInterest = rebateInterest;
    }
    
    /**
     * Getter for property closedAccNo.
     * @return Value of property closedAccNo.
     */
    public java.lang.String getClosedAccNo() {
        return closedAccNo;
    }
    
    /**
     * Setter for property closedAccNo.
     * @param closedAccNo New value of property closedAccNo.
     */
    public void setClosedAccNo(java.lang.String closedAccNo) {
        this.closedAccNo = closedAccNo;
    }

    public boolean isHoAccount() {
        return hoAccount;
    }

    public void setHoAccount(boolean hoAccount) {
        this.hoAccount = hoAccount;
    }

    public ArrayList getOrgRespList() {
        return orgRespList;
    }

    public void setOrgRespList(ArrayList orgRespList) {
        this.orgRespList = orgRespList;
    }
    
    /**
     * Getter for property txtScreenName.
     * @return Value of property txtScreenName.
     */
    public String getTxtScreenName() {
        return txtScreenName;
    }
    
    /**
     * Setter for property txtScreenName.
     * @param txtScreenName New value of property txtScreenName.
     */
    public void setTxtScreenName(String txtScreenName) {
        this.txtScreenName = txtScreenName;
    }
    
}
