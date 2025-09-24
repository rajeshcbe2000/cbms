/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * AccountClosingOB.java
 *
 * Created on August 13, 2003, 4:30 PM
 */

package com.see.truetransact.ui.termloan.charges;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Date;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.ui.deposit.lien.DepositLienUI;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.transferobject.operativeaccount.AccountClosingTO;
import com.see.truetransact.transferobject.termloan.chargesTo.TermLoanChargesTO;
import com.see.truetransact.transferobject.TOHeader;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.TTException;
import org.apache.log4j.Logger;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.transaction.TransactionOB;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.ui.common.CommonRB;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.EnhancedTableModel;

/**
 *
 * @author  Administrator
 * Modified by Karthik
 */
public class TermLoanChargesOB extends CObservable {
    private double lienAmount ;
    private static TermLoanChargesOB termLoanChargesOB;
    private double freezeAmount ;
    private String cboProductID = "";
    private String cboProductType="";
    private String cboChargesType="";
    private String txtAccountNumber = "";
    private Double txtChargesAmount=null;
    private String tdtChargesDate="";
    private String txtNoOfUnusedChequeLeafs = "";
    private String txtInterestPayable = "";
    private String txtChargeDetails = "";
    private String txtAccountClosingCharges = "";
    private String txtPayableBalance = "";
    private ComboBoxModel cbmProductID;
    private ComboBoxModel cbmProdType;
    private ComboBoxModel cbmChargeType;
    private final String AUTHORIZE="AUTHORIZE";
    private String accountHeadDesc;
    private String accountHeadId;
    private String customerName;
    private String availableBalance;
    private int actionType;
    private EnhancedTableModel tblChargesDetails;
    private EnhancedTableModel tblChargesTotAmt;
    private EnhancedTableModel tblTotAcctDetails;
    private int result;
    private String loanBehaves="";
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    
    private final String DELETED_TRANS_TOs = "DELETED_TRANS_TOs";
    private final String NOT_DELETED_TRANS_TOs = "NOT_DELETED_TRANS_TOs";
    
    private final static Logger log = Logger.getLogger(TermLoanChargesOB.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    private String prodType="";
    private String loanInt;
    private HashMap map;
    private ProxyFactory proxy;
    private HashMap totalLoanAmount;
    //for filling dropdown
    private HashMap lookupMap;
    private HashMap param;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private ArrayList chargesTOs;
    private ArrayList tblChargesList;
    ArrayList tblHeadList;
    ArrayList tblHeadTotAmtList;
    ArrayList tblAccountHeadList;
    ArrayList chargeTypeList;
    private TermLoanChargesTO termloanChargesTO;
    private TransactionOB transactionOB;
    private LinkedHashMap allowedTransactionDetailsTO = null;
    private LinkedHashMap transactionDetailsTO = null;
    private LinkedHashMap deletedTransactionDetailsTO = null;
    TermLoanChargesRB objAccountClosingRB = new TermLoanChargesRB();
    private final   java.util.ResourceBundle objTermLoanRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.termloan.charges.TermLoanChargesRB", ProxyParameters.LANGUAGE);
    private static final CommonRB objCommonRB = new CommonRB();
    private double  deposit_pre_int=0;
    private String deposit_premature=null;
    private HashMap _authorizeMap ;
    private String prematureString="";
    private HashMap oldTransDetMap = null;
    private String   asAnWhen="";
    private String transProdId = "";
    private int operation=0;
    
    double postageCharges=0;
    double arbitaryCharges=0;
    double epcost=0;
    double legalCharges=0;
    double insuranceCharges=0;
    double misllanCharges=0;
    double executionCharges=0;
    double advertisementCharges=0;
    Date curDate=null;
    private String txtNarration = "";
    
    LinkedHashMap otherChargesMap;
    //    private boolean depTrans;
    static {
        try {
            termLoanChargesOB = new TermLoanChargesOB();
        } catch(Exception e) {
            log.info("try: " + e);
            parseException.logException(e,true);
            e.printStackTrace();
        }
        
    }
    /** Creates a new instance of AccountClosingOB */
    public TermLoanChargesOB() {
        try {
            proxy = ProxyFactory.createProxy();
            
            map = new HashMap();
            map.put(CommonConstants.JNDI, "TermLoanChargesJNDI");
            map.put(CommonConstants.HOME, "termloan.charges.TermLoanChargesHome");
            map.put(CommonConstants.REMOTE, "termloan.charges.TermLoanCharges");
            
            
        } catch (Exception e) {
            parseException.logException(e,true);
        }
        termLoanChargesOB();
    }
    private void termLoanChargesOB(){
        try{
             curDate = ClientUtil.getCurrentDate();
            fillDropdown();
            setTableValue();
            setTableTotAmtValue();
            setTableAllLoanAccountValue();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private void setTableValue(){
        tblHeadList=new ArrayList();
        tblHeadList.add(objTermLoanRB.getString("tblColumnChargeDetail1"));
        tblHeadList.add(objTermLoanRB.getString("tblColumnChargeDetail2"));
        tblHeadList.add(objTermLoanRB.getString("tblColumnChargeDetail3"));
        tblHeadList.add(objTermLoanRB.getString("tblColumnChargeDetail4"));
        tblHeadList.add(objTermLoanRB.getString("tblColumnChargeDetail5"));
        tblHeadList.add(objTermLoanRB.getString("tblColumnChargeDetail6"));
        tblChargesDetails=new EnhancedTableModel(null,tblHeadList);
        
    }
    private void setTableTotAmtValue(){
        tblHeadTotAmtList=new ArrayList();
        tblHeadTotAmtList.add(objTermLoanRB.getString("tblColumnChargeDetail1"));//tblColumnChargetTotAmt1
        tblHeadTotAmtList.add(objTermLoanRB.getString("tblColumnChargetTotAmt2"));
        
        tblChargesTotAmt=new EnhancedTableModel(null,tblHeadTotAmtList);
        
    }
    private void setTableAllLoanAccountValue(){
        tblAccountHeadList=new ArrayList();
        tblAccountHeadList.add(objTermLoanRB.getString("tblColumnChargetOtherDetails1"));
        tblAccountHeadList.add(objTermLoanRB.getString("tblColumnChargetOtherDetails2"));
        tblTotAcctDetails=new EnhancedTableModel(null,tblAccountHeadList);
    }
    /** To fill the comboboxes */
    private void fillDropdown() throws Exception{
        lookupMap = new HashMap();
        lookupMap.put(CommonConstants.JNDI, "LookUpJNDI");
        lookupMap.put(CommonConstants.HOME, "common.lookup.LookUpHome");
        lookupMap.put(CommonConstants.REMOTE, "common.lookup.LookUp");
        
        param = new java.util.HashMap();
        ArrayList lookupKeys=new ArrayList();
        key=new ArrayList();
        value=new ArrayList();
        chargeTypeList=new ArrayList();
        //            key.add("");
        //            value.add("");
        param.put(CommonConstants.MAP_NAME,null);
        //             cbmProdType=new ComboBoxModel(key,value);
        lookupKeys.add("PRODUCTTYPE_CHARGES");
        lookupKeys.add("TERMLOAN.CHARGE_TYPE");
        param.put(CommonConstants.PARAMFORQUERY,lookupKeys);
        
        
        final HashMap lookupValues = ClientUtil.populateLookupData(param);
        
        fillData((HashMap)lookupValues.get("PRODUCTTYPE_CHARGES"));//babu added 08-04-2014
        //        chargeTypeList=(ArrayList)lookupValues.get("PRODUCTTYPE");
        cbmProdType = new ComboBoxModel(key,value);
        for(int i=0;i<key.size();i++){
            
            if((key.get(i).equals("OA"))/*||(key.get(i).equals("TD"))*/||(key.get(i).equals("GL"))){
                cbmProdType.removeKeyAndElement("OA");
              //  cbmProdType.removeKeyAndElement("TD");
                cbmProdType.removeKeyAndElement("GL");
            }
        }
        fillData((HashMap)lookupValues.get("TERMLOAN.CHARGE_TYPE"));
        chargeTypeList=value;
        cbmChargeType=new ComboBoxModel(key,value);
        key = new ArrayList();
        value = new ArrayList();
        key.add("");
        value.add("");
        cbmProductID=new ComboBoxModel(key,value);
    }
    public String getAccountHeadForProductId(String productId) {
        /* may be the screen has been cleared, in that scenario we will have
         * the cboProductId as "", and we don;t want anything to be shown in
         * place of the account head description
         */
        
        if (productId == null || productId.equals("")) {
            return "";
        }
        /* based on the selection from the product combo box, one accound head
         * will be fetched from database and displayed on screen
         * same LookUp bean will be used for this purpose
         */
        /*
        HashMap hash;
        ArrayList key, value;
         
        hash = new HashMap();
        hash.put(CommonConstants.MAP_NAME,"getAccHead");
        hash.put(CommonConstants.PARAMFORQUERY, productId);
        hash = (HashMap)(ClientUtil.populateLookupData(hash)).get(CommonConstants.DATA);
        key = (ArrayList) hash.get(CommonConstants.KEY);
        value = (ArrayList) hash.get(CommonConstants.VALUE);
         
          // the 0th value is blank bydefault
        accountHeadId = (String)key.get(1);
        accountHeadDesc = (String)value.get(1);
         
        hash = null;
        key = null;
        value = null;*/
        if(this.getProdType()!=null && this.getProdType().equals("MDS"))
                return "";
        final HashMap accountHeadMap = new HashMap();
        accountHeadMap.put("PROD_ID",productId);
        final List resultList = ClientUtil.executeQuery("getAccountHeadProd"+this.getProdType(),accountHeadMap);
        final HashMap resultMap = (HashMap)resultList.get(0);
        accountHeadId = (resultMap.get("AC_HEAD").toString());
        accountHeadDesc = (resultMap.get("AC_HEAD_DESC").toString());
        
        return accountHeadId + " [" + accountHeadDesc + "]";
    }
    /**
     * Returns an instance of TermLoanOB
     *
     * @return TermLoanOB
     */
    public static TermLoanChargesOB getInstance() {
        return termLoanChargesOB;
    }
    
    /** To set the key & value for comboboxes */
    private void fillData(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get("KEY");
        value = (ArrayList)keyValue.get("VALUE");
    }
    
    /** To get data for comboboxes */
    private HashMap populateData(HashMap obj)  throws Exception{
        // System.out.println("obj in OB : " + obj);
        obj.put(CommonConstants.MAP_WHERE, getTxtAccountNumber());
        //        obj.put("ACT_NUM",getTxtAccountNumber());
        System.out.println("getTransProdId :"+getTransProdId());
        if(obj.containsKey("MODE") && obj.get("MODE")!=null && (!obj.get("MODE").equals("UPDATE")) && (!obj.get("MODE").equals("AUTHORIZE"))){
            obj.put("PROD_ID",getCbmProductID().getKeyForSelected());
            obj.put("PROD_TYPE",prodType);
        }
        obj.put("CURR_DATE", ClientUtil.getCurrentDateProperFormat());
        System.out.println("map in OB :MIDDLE " + obj);
        HashMap where = proxy.executeQuery(obj, map) ;
        
        // System.out.println("where : " + where);
        //        keyValue = (HashMap)where.get("AccountDetailsTO");
        //        transactionOB.setDetails((List)where.get("TransactionTO"));
        //        log.info("Got HashMap");
        return where;
    }
    
    
    void setTxtAccountNumber(String txtAccountNumber){
        this.txtAccountNumber = txtAccountNumber;
        setChanged();
    }
    String getTxtAccountNumber(){
        return this.txtAccountNumber;
    }
    
    void setTxtNoOfUnusedChequeLeafs(String txtNoOfUnusedChequeLeafs){
        this.txtNoOfUnusedChequeLeafs = txtNoOfUnusedChequeLeafs;
        setChanged();
    }
    String getTxtNoOfUnusedChequeLeafs(){
        return this.txtNoOfUnusedChequeLeafs;
    }
    
    void setTxtInterestPayable(String txtInterestPayable){
        this.txtInterestPayable = txtInterestPayable;
        setChanged();
    }
    String getTxtInterestPayable(){
        return this.txtInterestPayable;
    }
    
    void setTxtChargeDetails(String txtChargeDetails){
        this.txtChargeDetails = txtChargeDetails;
        setChanged();
    }
    String getTxtChargeDetails(){
        return this.txtChargeDetails;
    }
    
    public void setTxtAccountClosingCharges(String txtAccountClosingCharges){
        this.txtAccountClosingCharges = txtAccountClosingCharges;
        setChanged();
    }
    public String getTxtAccountClosingCharges(){
        return this.txtAccountClosingCharges;
    }
    
    void setTxtPayableBalance(String txtPayableBalance){
        this.txtPayableBalance = txtPayableBalance;
        setChanged();
    }
    String getTxtPayableBalance(){
        return this.txtPayableBalance;
    }
    
    
    public void setCbmProductID(ComboBoxModel cbmProductID){
        this.cbmProductID = cbmProductID;
        setChanged();
    }
    public ComboBoxModel getCbmProductID(){
        return this.cbmProductID;
    }
    
    public String getAccountHeadDesc() {
        return accountHeadDesc;
    }
    
    public void setAccountHeadDesc(String accountHeadDesc) {
        this.accountHeadDesc = accountHeadDesc;
        setChanged();
    }
    
    public String getAccountHeadId() {
        return this.accountHeadId;
    }
    
    public void setAccountHeadId(String accountHeadId) {
        this.accountHeadId = accountHeadId;
        setChanged();
    }
    
    public String getCustomerName() {
        return this.customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
        setChanged();
    }
    
    public String getAvailableBalance() {
        return this.availableBalance;
    }
    
    public void setAvailableBalance(String availableBalance) {
        this.availableBalance = availableBalance;
        setChanged();
    }
    
    public int getActionType(){
        return this.actionType;
    }
    
    public void setActionType(int actionType) {
        this.actionType = actionType;
        setChanged();
    }
    
    public int getResult(){
        return this.result;
    }
    
    public void setResult(int result) {
        this.result = result;
        setChanged();
    }
    
    public String getLblStatus(){
        return this.lblStatus;
    }
    
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }
    
    /** set the value of Account head ID and description based on the product selected
     * in the UI
     */
    public void getAccountHeadForProduct() {
        
        /* based on the selection from the product combo box, one accound head
         * will be fetched from database and displayed on screen
         * same LookUp bean will be used for this purpose
         */
        param.put(CommonConstants.MAP_NAME,"getAccHead");
        param.put(CommonConstants.PARAMFORQUERY, getCboProductID());
        try {
            final HashMap lookupValues = ClientUtil.populateLookupData(param);
            fillData((HashMap)lookupValues.get("DATA"));
            //If proper value is returned, then the size will be more than 1, else do nothing
            if( value.size() > 1 ){
                setAccountHeadId((String)value.get(1));
                setAccountHeadDesc((String)key.get(1));
            }
            ttNotifyObservers();
        } catch (Exception e) {
            parseException.logException(e,true);
        }
        
    }
    
    /** To get customername & balance info */
    //    public void getCustomerNameForAccountNumber() {
    
        /* based on the selection from the product combo box, one accound head
         * will be fetched from database and displayed on screen
         * same LookUp bean will be used for this purpose
         
        param.put(CommonConstants.MAP_NAME,"getCustomerName");
        param.put(CommonConstants.PARAMFORQUERY, getTxtAccountNumber());
        try {
            HashMap lookupValues = ClientUtil.populateLookupData(param);
            fillData((HashMap)lookupValues.get("DATA"));
            setCustomerName((String)value.get(1));
            setAvailableBalance((String)key.get(1));
            param.put(CommonConstants.MAP_NAME,"getAccountClosingCharges");
            lookupValues = ClientUtil.populateLookupData(param);
            fillData((HashMap)lookupValues.get("DATA"));
            this.txtAccountClosingCharges=(String)(value.get(1));
            setTxtAccountClosingCharges((String)(value.get(1)));
            // System.out.println(this.getTxtAccountClosingCharges());
            ttNotifyObservers();
        } catch (Exception e) {
            parseException.logException(e,true);
//        } */
    //        getAccountClosingCharges();
    //    }
    
    public boolean checkDeleteRecords(int row){
       TermLoanChargesTO objTermLoanChargesTO=(TermLoanChargesTO) chargesTOs.get(row);
       if(CommonUtil.convertObjToDouble(objTermLoanChargesTO.getPaidAmount()).doubleValue()>0){
           return true;
       }
       return false;
    }
    public void getAccountClosingCharges(HashMap param){
        ArrayList singleList=new ArrayList();
        ArrayList recordList=new ArrayList();
        param.put("MODE", getCommand());
        chargesTOs = new ArrayList();
        postageCharges=0;
        arbitaryCharges=0;
        epcost=0;
        legalCharges=0;
        insuranceCharges=0;
        misllanCharges=0;
        executionCharges=0;
        advertisementCharges=0;
        Date acctOpenDt=null;
        Date lastIntCalDt=null;
         double interestDoubleValue=0.0;
        // System.out.println("param ... : " + param);
        
        //for premature deposit closer
        
        try {
            HashMap dataMap = populateData(param);
            System.out.println("param Data Map :" + dataMap);
//            singleList=new ArrayList();
//            singleList.add("Asset Status");
//            singleList.add(CommonUtil.convertObjToStr(dataMap.get("ASSET_STATUS")));
//            recordList.add(singleList);
            singleList=new ArrayList();
            singleList.add("Rate of Interest");
            singleList.add(CommonUtil.convertObjToStr(dataMap.get("ROI"))+"%");
            recordList.add(singleList);
            singleList=new ArrayList();
            singleList.add("Acct Open Dt");
            singleList.add(CommonUtil.convertObjToStr(dataMap.get("ACCT_OPEN_DT")));
            recordList.add(singleList);
            singleList=new ArrayList();
            singleList.add("Last Int Cal Dt");
            acctOpenDt=DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(dataMap.get("ACCT_OPEN_DT")));
            lastIntCalDt=DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(dataMap.get("LAST_INT_CALC_DT")));
//            if(DateUtil.dateDiff(lastIntCalDt,acctOpenDt)>0)
//                singleList.add(CommonUtil.convertObjToStr(acctOpenDt));
//            else
                singleList.add(CommonUtil.convertObjToStr(dataMap.get("LAST_INT_CALC_DT")));
                
            recordList.add(singleList);
            
             singleList=new ArrayList();
            singleList.add("sanction Amt");
            String limit = CurrencyValidation.formatCrore( (dataMap.get("LIMIT")==null) ? "0" : dataMap.get("LIMIT").toString());
            singleList.add(limit);
            recordList.add(singleList);
            
            singleList=new ArrayList();
            singleList.add("Total Balance");
            String totalBalance = CurrencyValidation.formatCrore( (dataMap.get("TOTAL_BALANCE")==null) ? "0" : dataMap.get("TOTAL_BALANCE").toString());
            System.out.println("clearbalnce"+totalBalance);
            singleList.add(totalBalance);//dataMap.get("TOTAL_BALANCE")
            recordList.add(singleList);
            singleList=new ArrayList();
            singleList.add("No Of Installments");
            singleList.add(dataMap.get("NO_OF_INSTALLMENT"));
            recordList.add(singleList);
            if(dataMap.containsKey("SHOW_INSTALLMENT_NO")&& dataMap.get("SHOW_INSTALLMENT_NO")!=null){
            singleList=new ArrayList();
            singleList.add("Pending Installment Due");
            singleList.add(dataMap.get("SHOW_INSTALLMENT_NO"));
            recordList.add(singleList);
            }
            singleList=new ArrayList();
            if(dataMap.containsKey("INSTALL_TYPE") && dataMap.get("INSTALL_TYPE") !=null && dataMap.get("INSTALL_TYPE").equals("EMI"))
                singleList.add("Installment EMI Amt");
            else
                singleList.add("Installment Amt");
            String installmentAmt = CurrencyValidation.formatCrore( (dataMap.get("INSTALLMENT AMT")==null) ? "0" : dataMap.get("INSTALLMENT AMT").toString());
            singleList.add(installmentAmt);
            recordList.add(singleList);
            
            singleList=new ArrayList();
            singleList.add("principal Excess Paid");
            String paidPrincipal = CurrencyValidation.formatCrore( (dataMap.get("PAID_PRINCIPAL")==null) ? "0" : dataMap.get("PAID_PRINCIPAL").toString());
            singleList.add(paidPrincipal);//dataMap.get("PAID_INTEREST")
            recordList.add(singleList);
            
            singleList=new ArrayList();
            singleList.add("Interest Paid");
            String paidInterest = CurrencyValidation.formatCrore( (dataMap.get("PAID_INTEREST")==null) ? "0" : dataMap.get("PAID_INTEREST").toString());
            singleList.add(paidInterest);//dataMap.get("PAID_INTEREST")
            recordList.add(singleList);
            singleList=new ArrayList();
            singleList.add("Penal Interest Paid");
            String paidPenal = CurrencyValidation.formatCrore( (dataMap.get("PAID_PENAL_INTEREST")==null) ? "0" : dataMap.get("PAID_PENAL_INTEREST").toString());
            singleList.add(paidPenal);//dataMap.get("PAID_PENAL_INTEREST")
            recordList.add(singleList);
            
            singleList=new ArrayList();
            singleList.add("Penal Interest Due");
            String penal = CurrencyValidation.formatCrore( (dataMap.get("AccountPenalInterest")==null) ? "0" : dataMap.get("AccountPenalInterest").toString());
            double penalinterestDoubleValue = CommonUtil.convertObjToDouble(dataMap.get("AccountPenalInterest")).doubleValue();
            if(penalinterestDoubleValue>0.0){
                singleList.add(penal);//dataMap.get("AccountPenalInterest")
            }else
                 singleList.add("0.0");
            recordList.add(singleList);
            singleList=new ArrayList();
            singleList.add("Interest Due");
            String interest = CurrencyValidation.formatCrore( (dataMap.get("AccountInterest")==null) ? "0" : dataMap.get("AccountInterest").toString());
             interestDoubleValue = CommonUtil.convertObjToDouble(dataMap.get("AccountInterest")).doubleValue();
            if(interestDoubleValue>0.0){
                singleList.add(interest);//dataMap.get("AccountInterest")
            }else
                 singleList.add("0.0");
            recordList.add(singleList);
            
            if(interestDoubleValue<0.0){
                singleList=new ArrayList();
                singleList.add("Excess Int Collected");
                singleList.add(interest);
                recordList.add(singleList);
            }
            
            singleList=new ArrayList();
            singleList.add("Principal Due");
            String prinicpalDue = CurrencyValidation.formatCrore( (dataMap.get("PRINCIPAL_DUE")==null) ? "0" : dataMap.get("PRINCIPAL_DUE").toString());
            singleList.add(prinicpalDue);//dataMap.get("PRINCIPAL_DUE")
            recordList.add(singleList);
            //TOTAL DUE AMOUNT
//             singleList=new ArrayList();
//            singleList.add("Total Due Amount");
//             prinicpalDue = (dataMap.get("PRINCIPAL_DUE")==null) ? "0" : dataMap.get("PRINCIPAL_DUE").toString();
//             interest = (dataMap.get("AccountInterest")==null) ? "0" : dataMap.get("AccountInterest").toString();
//             penal =  (dataMap.get("AccountPenalInterest")==null) ? "0" : dataMap.get("AccountPenalInterest").toString();
//             double totDue=Double.parseDouble(prinicpalDue)+Double.parseDouble(interest)+Double.parseDouble(penal);
//            singleList.add(String.valueOf(totDue));//dataMap.get("PRINCIPAL_DUE")
//            recordList.add(singleList);
//            //
//     
//            Class objClasses[] = new Class[2];
//            objClasses[0] = new String().getClass();
//            objClasses[1] = new String().getClass();
//            tblTotAcctDetails.setColumnClasses(objClasses);
//            tblTotAcctDetails.setDataArrayList(recordList,tblAccountHeadList);//chargeTypeList
            
            
            //getArrayRows
            if(dataMap!=null &&dataMap.containsKey("TermLoanChargesTO") && dataMap.get("TermLoanChargesTO")!=null) {
                ArrayList totRecordList=new ArrayList();
                TermLoanChargesTO termloanChargeTo=null;
                List lst=(List)dataMap.get("TermLoanChargesTO");
                for(int i=0;i<lst.size();i++){
                    termloanChargeTo=new TermLoanChargesTO();
                    termloanChargeTo=(TermLoanChargesTO)lst.get(i);
                    if(termloanChargeTo.getCharge_Type().equals("ARBITRARY CHARGES") &&  termloanChargeTo.getAuthorize_Status() !=null && termloanChargeTo.getAuthorize_Status().equals("AUTHORIZED"))
                        arbitaryCharges+=CommonUtil.convertObjToDouble(termloanChargeTo.getAmount()).doubleValue()-CommonUtil.convertObjToDouble(termloanChargeTo.getPaidAmount()).doubleValue();
                    else if(termloanChargeTo.getCharge_Type().equals("EP_COST") &&  termloanChargeTo.getAuthorize_Status() !=null && termloanChargeTo.getAuthorize_Status().equals("AUTHORIZED"))
                        epcost+=CommonUtil.convertObjToDouble(termloanChargeTo.getAmount()).doubleValue()-CommonUtil.convertObjToDouble(termloanChargeTo.getPaidAmount()).doubleValue();
                    else if(termloanChargeTo.getCharge_Type().equals("EXECUTION DECREE CHARGES")&& termloanChargeTo.getAuthorize_Status() !=null && termloanChargeTo.getAuthorize_Status().equals("AUTHORIZED"))
                        executionCharges+=CommonUtil.convertObjToDouble(termloanChargeTo.getAmount()).doubleValue()-CommonUtil.convertObjToDouble(termloanChargeTo.getPaidAmount()).doubleValue();
                    else if(termloanChargeTo.getCharge_Type().equals("ADVERTISE CHARGES")&& termloanChargeTo.getAuthorize_Status() !=null && termloanChargeTo.getAuthorize_Status().equals("AUTHORIZED"))
                        advertisementCharges+=CommonUtil.convertObjToDouble(termloanChargeTo.getAmount()).doubleValue()-CommonUtil.convertObjToDouble(termloanChargeTo.getPaidAmount()).doubleValue();
                    else if(termloanChargeTo.getCharge_Type().equals("INSURANCE CHARGES") && termloanChargeTo.getAuthorize_Status() !=null && termloanChargeTo.getAuthorize_Status().equals("AUTHORIZED"))
                        insuranceCharges+=CommonUtil.convertObjToDouble(termloanChargeTo.getAmount()).doubleValue()-CommonUtil.convertObjToDouble(termloanChargeTo.getPaidAmount()).doubleValue();
                    else if(termloanChargeTo.getCharge_Type().equals("LEGAL CHARGES") && termloanChargeTo.getAuthorize_Status() !=null && termloanChargeTo.getAuthorize_Status().equals("AUTHORIZED"))
                        legalCharges+=CommonUtil.convertObjToDouble(termloanChargeTo.getAmount()).doubleValue()-CommonUtil.convertObjToDouble(termloanChargeTo.getPaidAmount()).doubleValue();
                    else if(termloanChargeTo.getCharge_Type().equals("MISCELLANEOUS CHARGES") &&  termloanChargeTo.getAuthorize_Status() !=null && termloanChargeTo.getAuthorize_Status().equals("AUTHORIZED"))
                        misllanCharges+=CommonUtil.convertObjToDouble(termloanChargeTo.getAmount()).doubleValue()-CommonUtil.convertObjToDouble(termloanChargeTo.getPaidAmount()).doubleValue();
                    else if(termloanChargeTo.getCharge_Type().equals("POSTAGE CHARGES") && termloanChargeTo.getAuthorize_Status() !=null && termloanChargeTo.getAuthorize_Status().equals("AUTHORIZED"))
                        postageCharges+=CommonUtil.convertObjToDouble(termloanChargeTo.getAmount()).doubleValue()-CommonUtil.convertObjToDouble(termloanChargeTo.getPaidAmount()).doubleValue();
                    else {
                        if(termloanChargeTo.getAuthorize_Status() !=null && termloanChargeTo.getAuthorize_Status().equals("AUTHORIZED")){
                            if (otherChargesMap==null) {
                                otherChargesMap = new LinkedHashMap();
                            }
                            otherChargesMap.put(termloanChargeTo.getCharge_Type(), termloanChargeTo.getAmount());
                        }
                    }
                    singleList=new ArrayList();
                    singleList=getArrayRows(termloanChargeTo);
                    totRecordList.add(singleList);
                    chargesTOs.add(termloanChargeTo);
                }
                tblChargesList = totRecordList;
                tblChargesDetails.setDataArrayList(totRecordList,tblHeadList);
                cbmProdType.setKeyForSelected(CommonUtil.convertObjToStr(param.get("PROD_TYPE")));
                cbmProductID.setKeyForSelected(CommonUtil.convertObjToStr(param.get("PROD_ID")));
            }
            double totalCharges = setTotAmtTableData();
            singleList=new ArrayList();
            singleList.add("Total Charges Amount");
            singleList.add(String.valueOf(totalCharges));//dataMap.get("PRINCIPAL_DUE")
            recordList.add(singleList);

            singleList=new ArrayList();
            singleList.add("Total Due Amount");
            prinicpalDue = (dataMap.get("PRINCIPAL_DUE")==null) ? "0" : dataMap.get("PRINCIPAL_DUE").toString();
            interest = (dataMap.get("AccountInterest")==null) ? "0" : dataMap.get("AccountInterest").toString();
            penal =  (dataMap.get("AccountPenalInterest")==null) ? "0" : dataMap.get("AccountPenalInterest").toString();
             double totDue= totalCharges + Double.parseDouble(prinicpalDue)+Double.parseDouble(interest)+Double.parseDouble(penal);
            singleList.add(String.valueOf(totDue));//dataMap.get("PRINCIPAL_DUE")
            recordList.add(singleList);
            //

            Class objClasses[] = new Class[2];
            objClasses[0] = new String().getClass();
            objClasses[1] = new String().getClass();
            tblTotAcctDetails.setColumnClasses(objClasses);
            tblTotAcctDetails.setDataArrayList(recordList,tblAccountHeadList);//chargeTypeList
            
            //            recordList=new ArrayList();
            //            System.out.println("chargeTypeList#####"+chargeTypeList);
            //            singleList=new ArrayList();
            //            singleList.add(chargeTypeList.get(1));
            //            singleList.add(new Double(postageCharges));
            //            recordList.add(singleList);
            //            singleList=new ArrayList();
            //            singleList.add(chargeTypeList.get(2));
            //            singleList.add(new Double(arbitaryCharges));
            //            recordList.add(singleList);
            //            singleList=new ArrayList();
            //            singleList.add(chargeTypeList.get(3));
            //            singleList.add(new Double(legalCharges));
            //            recordList.add(singleList);
            //            singleList=new ArrayList();
            //            singleList.add(chargeTypeList.get(4));
            //            singleList.add(new Double(insuranceCharges));
            //            recordList.add(singleList);
            //            singleList=new ArrayList();
            //            singleList.add(chargeTypeList.get(5));
            //            singleList.add(new Double(misllanCharges));
            //            recordList.add(singleList);
            //            singleList=new ArrayList();
            //            singleList.add(chargeTypeList.get(6));
            //            singleList.add(new Double(executionCharges));
            //            recordList.add(singleList);
            //            tblChargesTotAmt.setDataArrayList(recordList,tblHeadTotAmtList);
            //            cbmProdType.getDataForKey(CommonUtil.convertObjToStr(dataMap.get("")
            ttNotifyObservers();
            if(interestDoubleValue<0.0){
              int yes_no=ClientUtil.confirmationAlert("" +
              " Excess Interest Collected  On Retrospective Change of Interest.."+"\n"+
              "Do  You Want to Reverse back to customer");
              Date retDt=null;
              if(dataMap.containsKey("UPDATE_RET_APP_DT"))
                  retDt=(Date)dataMap.get("UPDATE_RET_APP_DT");
              if(yes_no==0)
                callExcessTransaction(interestDoubleValue,retDt);
            }
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    private void callExcessTransaction(double interestDoubleValue,Date ret){
        interestDoubleValue*= -1;
        HashMap totalMap =new HashMap();
        StringBuffer buf=new StringBuffer();
        double excessint=interestDoubleValue;
        try{
        totalMap.put("TRANSACTION_AMT",new Double(interestDoubleValue));
        if(postageCharges!=0)
            if(excessint>=postageCharges){
            excessint-=postageCharges;
            buf.append("Credit Postage Amount   "+postageCharges+"\n");
            totalMap.put("POSTAGE CHARGES",new Double(postageCharges));
        }else{
            buf.append("Credit Postage Amount   "+excessint+"\n");
            totalMap.put("POSTAGE CHARGES",new Double(excessint));
            excessint=0;
        }
        if(advertisementCharges!=0)
            if(excessint>=advertisementCharges){
            excessint-=advertisementCharges;
             buf.append("Credit Advertisement Amount   "+advertisementCharges+"\n");
            totalMap.put("ADVERTISE CHARGES",new Double(advertisementCharges));
             
        }else{
             buf.append("Credit Advertisement Amount   "+excessint+"\n");
            totalMap.put("ADVERTISE CHARGES",new Double(excessint));
              excessint=0;
            
        }
        if(arbitaryCharges!=0)
            if(excessint>=arbitaryCharges){
            excessint-=arbitaryCharges;
             buf.append("Credit Arbitrary Amount   "+arbitaryCharges+"\n");
            totalMap.put("ARBITRARY CHARGES",new Double(arbitaryCharges));
             
        }else{
             buf.append("Credit Arbitrary Amount   "+excessint+"\n");
            totalMap.put("ARBITRARY CHARGES",new Double(excessint));
              excessint=0;
            
        }
        if(epcost!=0)
            if(excessint>=epcost){
            excessint-=epcost;
             buf.append("Credit EP Cost Amount   "+epcost+"\n");
            totalMap.put("EP_COST",new Double(epcost));
             
        }else{
             buf.append("Credit EP Cost Amount   "+excessint+"\n");
            totalMap.put("EP_COST",new Double(excessint));
              excessint=0;
            
        }
        if(legalCharges!=0)
            if(excessint>=legalCharges){
            excessint-=legalCharges;
            buf.append("Credit Legal Amount   "+legalCharges+"\n");            
            totalMap.put("LEGAL CHARGES",new Double(legalCharges));
        }else{
              buf.append("Credit Legal Amount   "+excessint+"\n");            
            totalMap.put("LEGAL CHARGES",new Double(excessint));
            excessint=0;
        }
        if(insuranceCharges!=0)
        if(excessint>=insuranceCharges){
            excessint-=insuranceCharges;
             buf.append("Credit Insurance Amount   "+insuranceCharges+"\n");
            totalMap.put("INSURANCE CHARGES",new Double(insuranceCharges));
        }else{
            buf.append("Credit Insurance Amount   "+excessint+"\n");
            totalMap.put("INSURANCE CHARGES",new Double(excessint));
            excessint=0;
        }
        if(misllanCharges!=0)
        if(excessint>=misllanCharges){
            excessint-=misllanCharges;
             buf.append("Credit Misllan Amount   "+misllanCharges+"\n");
            totalMap.put("MISCELLANEOUS CHARGES",new Double(misllanCharges));
        }
        else{
               buf.append("Credit Misllan Amount   "+excessint+"\n");
            totalMap.put("MISCELLANEOUS CHARGES",new Double(excessint));
            excessint=0;
        }
        if(executionCharges!=0)
            if(excessint>=executionCharges){
            excessint-=executionCharges;
             buf.append("Credit Execution Amount   "+executionCharges+"\n");
            totalMap.put("EXECUTION DECREE CHARGES",new Double(executionCharges));
        }else{
              buf.append("Credit Execution Amount   "+excessint+"\n");
            totalMap.put("EXECUTION DECREE CHARGES",new Double(excessint));
            excessint=0;
        }
        if(excessint !=0){
             buf.append("Credit Loan Amount   "+excessint);
            totalMap.put("LOANAMT",new Double(excessint));
             
        }
          int value=ClientUtil.confirmationAlert("Debit InterestAccount Amount   :"+(interestDoubleValue)+"\n"
                                          + buf.toString());
          if(value==0){
            HashMap data=new HashMap();
            data.put(CommonConstants.MODULE, getModule());
            data.put(CommonConstants.SCREEN, getScreen());
            data.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
            data.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
             data.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            data.put("ALL_AMOUNT",totalMap);
            data.put("ACT_NUM",getTxtAccountNumber());
            data.put("EXCESS_TRANSACTION","EXCESS_TRANSACTION");
            HashMap proxyResultMap = proxy.execute(data, map);
            oldTransDetMap = null;
            setProxyReturnMap(proxyResultMap);
            setResult(actionType);
            showTransactionDetails( ret);
              System.out.println("Transaction part over");
          }else{
              return;
          }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void showTransactionDetails(Date ret){
        HashMap transMap=new HashMap();
        String displayStr="";
       HashMap updateretraspectiveMap=new HashMap();
            transMap.put("DEPOSIT_NO",getTxtAccountNumber());
                transMap.put("CURR_DT", ClientUtil.getCurrentDate());
                List lst = ClientUtil.executeQuery("getTransferTransAuthDetails", transMap);
                String batch_id=null;
                if(lst !=null && lst.size()>0){
                    displayStr += "Transfer Transaction Details...\n";
                    for(int i = 0;i<lst.size();i++){
                        transMap = (HashMap)lst.get(i);
                        batch_id=CommonUtil.convertObjToStr(transMap.get("BATCH_ID"));
                        displayStr += "Trans Id : "+transMap.get("TRANS_ID")+
                        "   Batch Id : "+transMap.get("BATCH_ID")+
                        "   Trans Type : "+transMap.get("TRANS_TYPE");
                        String actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                        if(actNum != null && !actNum.equals("")){
                            displayStr +="   Account No : "+transMap.get("ACT_NUM")+
                            "   Loan Amount : "+transMap.get("AMOUNT")+"\n";
                        }else{
                            displayStr += "   Ac Hd Desc : "+transMap.get("AC_HD_ID")+
                            " Amount : "+transMap.get("AMOUNT")+"\n";
                        }
                        System.out.println("#### :" +transMap);
                      
                    }
                    ClientUtil.showMessageWindow(""+displayStr);
                    updateretraspectiveMap.put("BATCH_ID", batch_id);
                    updateretraspectiveMap.put("RETRASPECTIVE_APP_DT", ret);
                    updateretraspectiveMap.put("ACT_NUM", getTxtAccountNumber());
                    ClientUtil.execute("updateRetraspectiveBatchId",updateretraspectiveMap); 
                }
    }
    
    public void deleteData(int selectedRow){
        TermLoanChargesTO termloanChargesTO=(TermLoanChargesTO)chargesTOs.get(selectedRow);
        termloanChargesTO.setStatus("DELETED");
        termloanChargesTO.setStatus_By(TrueTransactMain.USER_ID);
        termloanChargesTO.setStatus_Dt(ClientUtil.getCurrentDate());
        chargesTOs.set(selectedRow,termloanChargesTO);
        System.out.println("chargesTOsdelete"+chargesTOs);
    }
    public double setTotAmtTableData(){
        ArrayList recordList=new ArrayList();
        System.out.println("chargeTypeList#####"+chargeTypeList);
        String chargeType="";
        String arbitaryCharge ="";
        String epCharge="";
        String executionCharge ="";
        String insuranceCharge ="";
        String legalCharge ="";
        String misllanCharge ="";
        String postageCharge ="";
        String advertisementCharge ="";
        ArrayList singleList=new ArrayList();
        if(chargeTypeList !=null && chargeTypeList.size()>0)
            for(int i=0;i<chargeTypeList.size();i++){
        chargeType=CommonUtil.convertObjToStr(chargeTypeList.get(i));
        if (chargeType.equalsIgnoreCase("Arbitrary Charges")) {
            singleList=new ArrayList();
            singleList.add(chargeType);
            arbitaryCharge = CurrencyValidation.formatCrore((arbitaryCharges == 0.0) ? "0" : String.valueOf(arbitaryCharges));
            singleList.add(arbitaryCharge);//new Double(arbitaryCharges
            recordList.add(singleList);
        }
        else if (chargeType.equalsIgnoreCase("EP_COST")) {
            singleList=new ArrayList();
            singleList.add(chargeType);
            epCharge = CurrencyValidation.formatCrore((epcost == 0.0) ? "0" : String.valueOf(epcost));
            singleList.add(epCharge);
            recordList.add(singleList);
        }
        else

//        chargeType = CommonUtil.convertObjToStr(chargeTypeList.get(2));
        if (chargeType.equalsIgnoreCase("Execution Decree Charges")) {
            singleList = new ArrayList();
            singleList.add(chargeType);
             executionCharge = CurrencyValidation.formatCrore((executionCharges == 0.0) ? "0" : String.valueOf(executionCharges));
            singleList.add(executionCharge);//new Double(executionCharges)
            recordList.add(singleList);
        }else
//        chargeType = CommonUtil.convertObjToStr(chargeTypeList.get(3));
        if (chargeType.equalsIgnoreCase("Insurance Charges")) {
            singleList = new ArrayList();
            singleList.add(chargeType);
             insuranceCharge = CurrencyValidation.formatCrore((insuranceCharges == 0.0) ? "0" : String.valueOf(insuranceCharges));
            singleList.add(insuranceCharge);//new Double(insuranceCharges)
            recordList.add(singleList);
        }else
//        chargeType = CommonUtil.convertObjToStr(chargeTypeList.get(4));
        if (chargeType.equalsIgnoreCase("Legal Charges")) {
            singleList = new ArrayList();
            singleList.add(chargeType);
             legalCharge = CurrencyValidation.formatCrore((legalCharges == 0.0) ? "0" : String.valueOf(legalCharges));
            singleList.add(legalCharge);//new Double(legalCharges)
            recordList.add(singleList);
        }else

//        chargeType = CommonUtil.convertObjToStr(chargeTypeList.get(5));
        if (chargeType.equalsIgnoreCase("Miscellaneous Charges")) {
            singleList = new ArrayList();
            singleList.add(chargeType);
             misllanCharge = CurrencyValidation.formatCrore((misllanCharges == 0.0) ? "0" : String.valueOf(misllanCharges));
            singleList.add(misllanCharge);//new Double(misllanCharges)
            recordList.add(singleList);
        }else
//        chargeType = CommonUtil.convertObjToStr(chargeTypeList.get(6));
        if (chargeType.equalsIgnoreCase("Postage Charges")) {
            singleList = new ArrayList();
            singleList.add(chargeType);
             postageCharge = CurrencyValidation.formatCrore((postageCharges == 0.0) ? "0" : String.valueOf(postageCharges));
            singleList.add(postageCharge);//new Double(postageCharges
            recordList.add(singleList);
        }else
//        chargeType = CommonUtil.convertObjToStr(chargeTypeList.get(7));
        if (chargeType.equalsIgnoreCase("Advertisement Charges")) {
            singleList = new ArrayList();
            singleList.add(chargeType);
             advertisementCharge = CurrencyValidation.formatCrore((advertisementCharges == 0.0) ? "0" : String.valueOf(advertisementCharges));
            singleList.add(advertisementCharge);//new Double(advertisementCharges
            recordList.add(singleList);
        }
        }
        double otherChargesTotal = 0;
        if (otherChargesMap!=null && otherChargesMap.size()>0) {
            Object keys[] = otherChargesMap.keySet().toArray();
            for (int i=0; i<otherChargesMap.size(); i++) {
        singleList=new ArrayList();
                singleList.add(keys[i]);
                singleList.add(CurrencyValidation.formatCrore(String.valueOf(otherChargesMap.get(keys[i]))));
                otherChargesTotal+=CommonUtil.convertObjToDouble(otherChargesMap.get(keys[i])).doubleValue();
                recordList.add(singleList);
            }
        }
        
        singleList=new ArrayList();
        singleList.add("Total Charges Due");
        executionCharge = (executionCharges==0.0) ? "0" : String.valueOf(executionCharges);
        misllanCharge =(misllanCharges==0.0) ? "0" : String.valueOf(misllanCharges);
        insuranceCharge = (insuranceCharges==0.0) ? "0" : String.valueOf(insuranceCharges);
        legalCharge = (legalCharges==0.0) ? "0" : String.valueOf(legalCharges);
        arbitaryCharge = (arbitaryCharges==0.0) ? "0" : String.valueOf(arbitaryCharges);
        epCharge=(epcost==0) ? "0" : String.valueOf(epcost);
        String  postagetCharges = (postageCharges==0.0) ? "0" : String.valueOf(postageCharges);
         String  advCharge = (advertisementCharges==0.0) ? "0" : String.valueOf(advertisementCharges);
        double totDue=Double.parseDouble(executionCharge)+Double.parseDouble(misllanCharge)+Double.parseDouble(insuranceCharge)
        +Double.parseDouble(legalCharge)+Double.parseDouble(arbitaryCharge)+Double.parseDouble(postagetCharges) + Double.parseDouble(advCharge);
        totDue+=otherChargesTotal;
        singleList.add(String.valueOf(totDue));
        recordList.add(singleList);
        tblChargesTotAmt.setDataArrayList(recordList,tblHeadTotAmtList);
        return totDue;
    }
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    /** To perform the necessary operation */
    public void doAction() {
        try {
            
            //            if( actionType != ClientConstants.ACTIONTYPE_CANCEL ){
            
            doActionPerform();
            
            //            }
        } catch (Exception e) {
            // System.out.println("Error in doAction of A/c Closing OB....");
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    
    public void getProducts(){
        List list=null;
        ArrayList key=new ArrayList();
        ArrayList value=new ArrayList();
        key.add("");
        value.add("");
        HashMap data;
        if(getProdType()!=null && getProdType().equals("MDS")){
            HashMap lookUpHash = new HashMap();
            lookUpHash.put("PROD_ID", getProdType());
            List keyValue = null;
            setProdType(getProdType());
            keyValue = ClientUtil.executeQuery("getProductsForMDSNoticeARC", lookUpHash);
             if(keyValue!=null && keyValue.size()>0){
                int size=keyValue.size();
                for(int i=0;i<size;i++){
                    data=(HashMap)keyValue.get(i);
                    key.add(data.get("PROD_ID"));
                    value.add(data.get("PROD_DESC"));
                }
            }
        }
        else{
            list=ClientUtil.executeQuery("Transfer.getCreditProduct"+getProdType(),null);
            if(list!=null && list.size()>0){
                int size=list.size();
                for(int i=0;i<size;i++){
                    data=(HashMap)list.get(i);
                    key.add(data.get("PRODID"));
                    value.add(data.get("PRODDESC"));
                }
            }
        }
        data = null;
        cbmProductID=new ComboBoxModel(key,value);
        setChanged();
    }
    
    
    
    
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        final AccountClosingTO objAccountClosingTO = setAccountClosingData();
        objAccountClosingTO.setCommand(getCommand());
        objAccountClosingTO.setStatus(getAction());
        objAccountClosingTO.setStatusBy(TrueTransactMain.USER_ID);
        objAccountClosingTO.setStatusDt(ClientUtil.getCurrentDate());
        final HashMap data = new HashMap();
        
        if(getAuthorizeMap()!=null)
            data.put(CommonConstants.AUTHORIZEMAP, getAuthorizeMap()); //For Authorization added 28 Apr 2005
        data.put("MODE",getCommand());
        System.out.println("#### termloanChargesTO : "+chargesTOs);
        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SCREEN, getScreen());
        data.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
        data.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
        data.put("TermLoanChargesTOs",chargesTOs);
        System.out.println("data in A/c Closing OB : " + data);
        HashMap proxyResultMap = proxy.execute(data, map);
        oldTransDetMap = null;
        setProxyReturnMap(proxyResultMap);
        setResult(actionType);
        
    }
    
    private String getCommand(){
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
                command=AUTHORIZE;
                break;
            default:
        }
        // System.out.println("command : " + command);
        return command;
    }
    
    private String getAction(){
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
            default:
        }
        // System.out.println("command : " + command);
        return action;
    }
    
    private AccountClosingTO setAccountClosingData()  throws Exception{
        final AccountClosingTO objAccountClosingTO = new AccountClosingTO();
        objAccountClosingTO.setActNum( getTxtAccountNumber());
        objAccountClosingTO.setUnusedChk( CommonUtil.convertObjToDouble(this.getTxtNoOfUnusedChequeLeafs()));
        objAccountClosingTO.setActClosingChrg( CommonUtil.convertObjToDouble(this.getTxtAccountClosingCharges()));
        objAccountClosingTO.setIntPayable( CommonUtil.convertObjToDouble(this.getTxtInterestPayable()) );
        objAccountClosingTO.setChrgDetails( CommonUtil.convertObjToDouble(this.getTxtChargeDetails()) );
        objAccountClosingTO.setPayableBal( CommonUtil.convertObjToDouble(this.getTxtPayableBalance()));
        System.out.println("####accountto"+objAccountClosingTO);
        return objAccountClosingTO;
    }
    
    public void insertData(){
        if(tblChargesDetails.getRowCount()==0) {
            chargesTOs=new ArrayList();
            tblChargesList=new ArrayList();
            postageCharges=0;
            advertisementCharges=0;
            arbitaryCharges=0;
            epcost=0;
            legalCharges=0;
            insuranceCharges=0;
            misllanCharges=0;
            executionCharges=0;
        }
        termloanChargesTO =new TermLoanChargesTO();
        termloanChargesTO.setProd_Type(getProdType());
        termloanChargesTO.setProd_Id(getCboProductID());
        termloanChargesTO.setAct_num(getTxtAccountNumber());
        termloanChargesTO.setCharge_Type(getCboChargesType());
        termloanChargesTO.setChargeDt(getProperFormatDate(getTdtChargesDate()));
        termloanChargesTO.setAmount(getTxtChargesAmount());
        termloanChargesTO.setStatus(CommonConstants.STATUS_CREATED);
        termloanChargesTO.setStatus_By(ProxyParameters.USER_ID);
        termloanChargesTO.setNarration(getTxtNarration()); //KD-3483
        if(termloanChargesTO.getCharge_Type().equals("ARBITRARY CHARGES"))
            arbitaryCharges+=CommonUtil.convertObjToDouble(termloanChargesTO.getAmount()).doubleValue()-CommonUtil.convertObjToDouble(termloanChargesTO.getPaidAmount()).doubleValue();
        if(termloanChargesTO.getCharge_Type().equals("EP_COST"))
            epcost+=CommonUtil.convertObjToDouble(termloanChargesTO.getAmount()).doubleValue()-CommonUtil.convertObjToDouble(termloanChargesTO.getPaidAmount()).doubleValue();
        if(termloanChargesTO.getCharge_Type().equals("EXECUTION DECREE CHARGES"))
            executionCharges+=CommonUtil.convertObjToDouble(termloanChargesTO.getAmount()).doubleValue()-CommonUtil.convertObjToDouble(termloanChargesTO.getPaidAmount()).doubleValue();
        if(termloanChargesTO.getCharge_Type().equals("INSURANCE CHARGES"))
            insuranceCharges+=CommonUtil.convertObjToDouble(termloanChargesTO.getAmount()).doubleValue()-CommonUtil.convertObjToDouble(termloanChargesTO.getPaidAmount()).doubleValue();
        if(termloanChargesTO.getCharge_Type().equals("LEGAL CHARGES"))
            legalCharges+=CommonUtil.convertObjToDouble(termloanChargesTO.getAmount()).doubleValue()-CommonUtil.convertObjToDouble(termloanChargesTO.getPaidAmount()).doubleValue();
        if(termloanChargesTO.getCharge_Type().equals("MISCELLANEOUS CHARGES"))
            misllanCharges+=CommonUtil.convertObjToDouble(termloanChargesTO.getAmount()).doubleValue()-CommonUtil.convertObjToDouble(termloanChargesTO.getPaidAmount()).doubleValue();
        if(termloanChargesTO.getCharge_Type().equals("POSTAGE CHARGES"))
            postageCharges+=CommonUtil.convertObjToDouble(termloanChargesTO.getAmount()).doubleValue()-CommonUtil.convertObjToDouble(termloanChargesTO.getPaidAmount()).doubleValue();
         if(termloanChargesTO.getCharge_Type().equals("ADVERTISE CHARGES"))
            advertisementCharges+=CommonUtil.convertObjToDouble(termloanChargesTO.getAmount()).doubleValue()-CommonUtil.convertObjToDouble(termloanChargesTO.getPaidAmount()).doubleValue();
        setTotAmtTableData();
        ArrayList retrunList=getArrayRows(termloanChargesTO);
        chargesTOs.add(termloanChargesTO);
        tblChargesList.add(retrunList);
        tblChargesDetails.setDataArrayList(tblChargesList,tblHeadList);
        //        ttNotifyObservers();
    }
    public ArrayList getArrayRows(TermLoanChargesTO termloanChargesTO){
        ArrayList chargesArrayList=new ArrayList();
        chargesArrayList.add(termloanChargesTO.getCharge_Type());
        chargesArrayList.add(termloanChargesTO.getChargeDt());
        chargesArrayList.add(CurrencyValidation.formatCrore(CommonUtil.convertObjToStr(termloanChargesTO.getAmount())));
        chargesArrayList.add(CurrencyValidation.formatCrore(CommonUtil.convertObjToStr((termloanChargesTO.getPaidAmount()==null)? "0": CommonUtil.convertObjToStr(termloanChargesTO.getPaidAmount()))));
//        chargesArrayList.add(termloanChargesTO.getPaidAmount());
        chargesArrayList.add(termloanChargesTO.getAuthorize_Status());
        chargesArrayList.add(termloanChargesTO.getNarration()); //KD-3483
        return chargesArrayList;
    }
    
     public Date getProperFormatDate(Object obj){
        Date currDt = null;
        if (obj!=null && obj.toString().length()>0) {
            Date tempDt= DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            currDt=(Date)curDate.clone();
            currDt.setDate(tempDt.getDate());
            currDt.setMonth(tempDt.getMonth());
            currDt.setYear(tempDt.getYear());
        }
        return currDt;
    }
    public String getCommandForLtd(){//if ur coming through deposit closing screen means this method will work otherwise it won't work.
        String command = null;//last line continution for the purpose of LTD deposits.
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
                command=AUTHORIZE;
                break;
            default:
        }
        // System.out.println("command : " + command);
        return command;
    }
    
    public String getActionForLtd(){//if ur coming through deposit closing screen means this method will work otherwise it won't work.
        String action = null;//last line continution for the purpose of LTD deposits.
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
            default:
        }
        // System.out.println("command : " + command);
        return action;
    }
    
    /** To retrieve a particular customer's accountclosing record */
    public void getData(HashMap whereMap) {
        try{
            // System.out.println("Ob.getActionType() : " + getActionType());
            // System.out.println("whereMap in getData of OB : " + whereMap);
            // System.out.println("map in getData of OB : " + map);
            whereMap.put("MODE", getCommand());
            if(prodType.equals("TermLoan"))
                whereMap.put("PROD_TYPE",prodType);
            System.out.println("map in getData OB : " + whereMap);
            final HashMap data = (HashMap)proxy.executeQuery(whereMap,map);
            System.out.println(">>>>> data in OBSIDE##### : " + data);
            populateAccountClosingData(data);
            List list = (List) data.get("TransactionTO");
            System.out.println("transaction@@@####"+list);
            transactionOB.setDetails(list);
            allowedTransactionDetailsTO=transactionOB.getAllowedTransactionDetailsTO();
            //ttNotifyObservers();
        }catch(Exception e){
            parseException.logException(e,true);
            e.printStackTrace();
        }
    }
    
    /** To populate data into the screen */
    private void populateAccountClosingData(HashMap data) throws Exception{
        final AccountClosingTO objAccountClosingTO = new AccountClosingTO();
        System.out.println("data in populateAccountClosingData : " + data);
        HashMap accTo = (HashMap)data.get("AccountDetailsTO");
        HashMap accountInterest = (HashMap)data.get("AccountInterest");
        setStatusBy(CommonUtil.convertObjToStr(accTo.get("STATUS_BY")));
        setAuthorizeStatus(CommonUtil.convertObjToStr(accTo.get("AUTHORIZE_STATUS")));
        setCboProductID(CommonUtil.convertObjToStr(accTo.get("PROD_ID")));
        setCustomerName(CommonUtil.convertObjToStr(accTo.get("Customer Name")));
        setAccountHeadId(CommonUtil.convertObjToStr(accTo.get("AC_HD_ID")));
        
        
        if(prodType.equals("TermLoan")){
            setAvailableBalance(CommonUtil.convertObjToStr(accTo.get("LOAN_BALANCE_PRINCIPAL")));
            setTxtInterestPayable(CommonUtil.convertObjToStr(accTo.get("INT_PAYABLE")));
            setTxtAccountNumber(CommonUtil.convertObjToStr(accTo.get("ACCT_NUM")));
        }
        else{
            setAvailableBalance(CommonUtil.convertObjToStr(accTo.get("AVAILABLE_BALANCE")));
            //            setTxtInterestPayable(CommonUtil.convertObjToStr(accountInterest.get(getTxtAccountNumber())));
            setTxtInterestPayable(CommonUtil.convertObjToStr(accTo.get("INT_PAYABLE")));
            setTxtAccountNumber(CommonUtil.convertObjToStr(accTo.get("ACT_NUM")));
        }
        setTxtAccountClosingCharges(CommonUtil.convertObjToStr(accTo.get("ACT_CLOSING_CHRG")));
        
        int returndCheques = 0;
        
        setTxtNoOfUnusedChequeLeafs(String.valueOf(returndCheques));
        
        setTxtChargeDetails(CommonUtil.convertObjToStr(accTo.get("CHRG_DETAILS")));
        setTxtPayableBalance(CommonUtil.convertObjToStr(accTo.get("PAYABLE_BAL")));
        oldTransDetMap = new HashMap();
        if (data.containsKey("ACT_CLOSE_CHRG_TRANS_DETAILS"))
            oldTransDetMap.put("ACT_CLOSE_CHRG_TRANS_DETAILS", data.get("ACT_CLOSE_CHRG_TRANS_DETAILS"));
        if (data.containsKey("CHRG_DETAILS_TRANS_DETAILS"))
            oldTransDetMap.put("CHRG_DETAILS_TRANS_DETAILS", data.get("CHRG_DETAILS_TRANS_DETAILS"));
        if (data.containsKey("CASH_TRANS_DETAILS"))
            oldTransDetMap.put("CASH_TRANS_DETAILS", data.get("CASH_TRANS_DETAILS"));
        if (data.containsKey("TRANSFER_TRANS_DETAILS"))
            oldTransDetMap.put("TRANSFER_TRANS_DETAILS", data.get("TRANSFER_TRANS_DETAILS"));
        accTo = null;
    }
    
    public void resetForm(){
        setCboProductType("");
        setCboProductID("");
        setCboChargesType("");
        setTxtChargesAmount(null);
        setTxtAccountNumber("");
        setTdtChargesDate("");
        setTxtNarration(""); //KD-3483
        otherChargesMap = null;
        tblChargesDetails=new EnhancedTableModel(null,tblHeadList);
        tblChargesTotAmt=new EnhancedTableModel(null,tblHeadTotAmtList);
        tblTotAcctDetails=new EnhancedTableModel(null,tblAccountHeadList);
        ttNotifyObservers();
    }
    
    /** To set the status based on ActionType, either New, Edit, etc., */
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
        //        ttNotifyObservers();
    }
    
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        ttNotifyObservers();
    }
    
    public void updateAuthorizeStatus() {
        HashMap hash = null;
        String status = null;
        // System.out.println("Records'll be updated... ");
        if(getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE) {
            status = CommonConstants.STATUS_AUTHORIZED;
        } else if(getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
            status = CommonConstants.STATUS_REJECTED;
        } else if(getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION) {
            status = CommonConstants.STATUS_EXCEPTION;
        }
        
        hash = new HashMap();
        hash.put("ACCOUNTNO",txtAccountNumber);
        hash.put("STATUS", status);
        hash.put("USER_ID",TrueTransactMain.USER_ID);
        hash.put("ACCOUNT_STATUS","CLOSED");
        ClientUtil.execute("authorizeUpdateAccountCloseTO", hash);
        ClientUtil.execute("authorizeAcctStatus", hash);
        // System.out.println("Record Updated : " +hash);
        
    }
    
    
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
        // System.out.println("In OB of RemIssue : " + allowedTransactionDetailsTO);
        this.allowedTransactionDetailsTO = allowedTransactionDetailsTO;
    }
    
    public int showAlertWindow(String amtLimit) {
        int option = 1;
        try{
            String[] options = {objCommonRB.getString("cDialogOK")};
            option = COptionPane.showOptionDialog(null,amtLimit, CommonConstants.WARNINGTITLE,
            COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
            null, options, options[0]);
        }catch (Exception e){
            parseException.logException(e,true);
        }
        return option;
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
     * Getter for property _authorizeMap.
     * @return Value of property _authorizeMap.
     */
    public java.util.HashMap getAuthorizeMap() {
        return _authorizeMap;
    }
    
    /**
     * Setter for property _authorizeMap.
     * @param _authorizeMap New value of property _authorizeMap.
     */
    public void setAuthorizeMap(java.util.HashMap authorizeMap) {
        this._authorizeMap = authorizeMap;
    }
    
    /**
     * Getter for property loanInt.
     * @return Value of property loanInt.
     */
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
    
    /**
     * Getter for property loanBehaves.
     * @return Value of property loanBehaves.
     */
    public java.lang.String getLoanBehaves() {
        return loanBehaves;
    }
    
    /**
     * Setter for property loanBehaves.
     * @param loanBehaves New value of property loanBehaves.
     */
    public void setLoanBehaves(java.lang.String loanBehaves) {
        this.loanBehaves = loanBehaves;
    }
    
    /**
     * Getter for property totalLoanAmount.
     * @return Value of property totalLoanAmount.
     */
    public java.util.HashMap getTotalLoanAmount() {
        return totalLoanAmount;
    }
    
    /**
     * Setter for property totalLoanAmount.
     * @param totalLoanAmount New value of property totalLoanAmount.
     */
    public void setTotalLoanAmount(java.util.HashMap totalLoanAmount) {
        this.totalLoanAmount = totalLoanAmount;
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
     * Getter for property oldTransDetMap.
     * @return Value of property oldTransDetMap.
     */
    public java.util.HashMap getOldTransDetMap() {
        return oldTransDetMap;
    }
    
    /**
     * Setter for property oldTransDetMap.
     * @param oldTransDetMap New value of property oldTransDetMap.
     */
    public void setOldTransDetMap(java.util.HashMap oldTransDetMap) {
        this.oldTransDetMap = oldTransDetMap;
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
     * Getter for property cbmProdType.
     * @return Value of property cbmProdType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProdType() {
        return cbmProdType;
    }
    
    /**
     * Setter for property cbmProdType.
     * @param cbmProdType New value of property cbmProdType.
     */
    public void setCbmProdType(com.see.truetransact.clientutil.ComboBoxModel cbmProdType) {
        this.cbmProdType = cbmProdType;
    }
    
    /**
     * Getter for property cbmChargeType.
     * @return Value of property cbmChargeType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmChargeType() {
        return cbmChargeType;
    }
    
    /**
     * Setter for property cbmChargeType.
     * @param cbmChargeType New value of property cbmChargeType.
     */
    public void setCbmChargeType(com.see.truetransact.clientutil.ComboBoxModel cbmChargeType) {
        this.cbmChargeType = cbmChargeType;
    }
    
    /**
     * Getter for property tblChargesDetails.
     * @return Value of property tblChargesDetails.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblChargesDetails() {
        return tblChargesDetails;
    }
    
    /**
     * Setter for property tblChargesDetails.
     * @param tblChargesDetails New value of property tblChargesDetails.
     */
    public void setTblChargesDetails(com.see.truetransact.clientutil.EnhancedTableModel tblChargesDetails) {
        this.tblChargesDetails = tblChargesDetails;
    }
    
    /**
     * Getter for property tblChargesTotAmt.
     * @return Value of property tblChargesTotAmt.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblChargesTotAmt() {
        return tblChargesTotAmt;
    }
    
    /**
     * Setter for property tblChargesTotAmt.
     * @param tblChargesTotAmt New value of property tblChargesTotAmt.
     */
    public void setTblChargesTotAmt(com.see.truetransact.clientutil.EnhancedTableModel tblChargesTotAmt) {
        this.tblChargesTotAmt = tblChargesTotAmt;
    }
    
    /**
     * Getter for property tblTotAcctDetails.
     * @return Value of property tblTotAcctDetails.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblTotAcctDetails() {
        return tblTotAcctDetails;
    }
    
    /**
     * Setter for property tblTotAcctDetails.
     * @param tblTotAcctDetails New value of property tblTotAcctDetails.
     */
    public void setTblTotAcctDetails(com.see.truetransact.clientutil.EnhancedTableModel tblTotAcctDetails) {
        this.tblTotAcctDetails = tblTotAcctDetails;
    }
    
    /**
     * Getter for property cboProductType.
     * @return Value of property cboProductType.
     */
    public java.lang.String getCboProductType() {
        return cboProductType;
    }
    
    /**
     * Setter for property cboProductType.
     * @param cboProductType New value of property cboProductType.
     */
    public void setCboProductType(java.lang.String cboProductType) {
        this.cboProductType = cboProductType;
    }
    
    /**
     * Getter for property cboChargesType.
     * @return Value of property cboChargesType.
     */
    public java.lang.String getCboChargesType() {
        return cboChargesType;
    }
    
    /**
     * Setter for property cboChargesType.
     * @param cboChargesType New value of property cboChargesType.
     */
    public void setCboChargesType(java.lang.String cboChargesType) {
        this.cboChargesType = cboChargesType;
    }
    
    /**
     * Getter for property tdtChargesDate.
     * @return Value of property tdtChargesDate.
     */
    public java.lang.String getTdtChargesDate() {
        return tdtChargesDate;
    }
    
    /**
     * Setter for property tdtChargesDate.
     * @param tdtChargesDate New value of property tdtChargesDate.
     */
    public void setTdtChargesDate(java.lang.String tdtChargesDate) {
        this.tdtChargesDate = tdtChargesDate;
    }
    
    
    
    /**
     * Getter for property cboProductID.
     * @return Value of property cboProductID.
     */
    public java.lang.String getCboProductID() {
        return cboProductID;
    }
    
    /**
     * Setter for property cboProductID.
     * @param cboProductID New value of property cboProductID.
     */
    public void setCboProductID(java.lang.String cboProductID) {
        this.cboProductID = cboProductID;
    }
    
    /**
     * Getter for property txtChargesAmount.
     * @return Value of property txtChargesAmount.
     */
    public java.lang.Double getTxtChargesAmount() {
        return txtChargesAmount;
    }
    
    /**
     * Setter for property txtChargesAmount.
     * @param txtChargesAmount New value of property txtChargesAmount.
     */
    public void setTxtChargesAmount(java.lang.Double txtChargesAmount) {
        this.txtChargesAmount = txtChargesAmount;
    }
    
    /**
     * Getter for property tblChargesList.
     * @return Value of property tblChargesList.
     */
    public java.util.ArrayList getTblChargesList() {
        return tblChargesList;
    }
    
    /**
     * Setter for property tblChargesList.
     * @param tblChargesList New value of property tblChargesList.
     */
    public void setTblChargesList(java.util.ArrayList tblChargesList) {
        this.tblChargesList = tblChargesList;
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
     * Getter for property advertisementCharges.
     * @return Value of property advertisementCharges.
     */
    public double getAdvertisementCharges() {
        return advertisementCharges;
    }
    
    /**
     * Setter for property advertisementCharges.
     * @param advertisementCharges New value of property advertisementCharges.
     */
    public void setAdvertisementCharges(double advertisementCharges) {
        this.advertisementCharges = advertisementCharges;
    }

    //KD-3483
    public String getTxtNarration() {
        return txtNarration;
    }

    public void setTxtNarration(String txtNarration) {
        this.txtNarration = txtNarration;
    }
    
    
    
}
